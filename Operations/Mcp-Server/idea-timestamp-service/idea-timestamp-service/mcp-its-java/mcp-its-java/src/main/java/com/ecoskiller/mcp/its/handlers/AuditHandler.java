package com.ecoskiller.mcp.its.handlers;

import com.ecoskiller.mcp.its.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * AuditHandler
 *
 * Implements:
 *   check_rate_limit      — Redis INCR/EXPIRE rate-limit counter inspection
 *   get_submission_audit  — Append-only audit log (innovation.idea_submission_audit)
 *
 * Also provides the static appendAudit() method used by all other handlers
 * to write to the shared append-only audit store.
 *
 * Per ITS spec:
 *   - Rate limit key: tenant:{tenant_id}:candidate:{candidate_id}:submissions:{date}
 *   - Default daily limit: 20 (configurable per tenant via admin-service)
 *   - Audit table: append-only, no UPDATE/DELETE
 *   - Audit fields: event_type, actor_id, description, timestamp, idea_id, tenant_id
 */
public class AuditHandler {

    private static final Logger LOG = Logger.getLogger(AuditHandler.class.getName());

    // Shared append-only audit store (production: PostgreSQL innovation.idea_submission_audit)
    // key = tenantId:ideaId  → list of audit entries
    // key = tenantId:*       → tenant-wide entries (rate-limit hits, etc.)
    static final Map<String, List<ObjectNode>> auditStore = new LinkedHashMap<>();

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator validator;

    public AuditHandler(SecurityValidator sv) {
        this.validator = sv;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: check_rate_limit
    // ─────────────────────────────────────────────────────────────────────────

    public Object checkRateLimit(JsonNode args) {
        String tenantId    = require(args, "tenant_id");
        String candidateId = require(args, "candidate_id");
        String dateStr     = args.path("date").asText("");

        // Resolve date
        LocalDate date = dateStr.isBlank()
            ? LocalDate.now(ZoneOffset.UTC)
            : LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
        String today = date.format(DateTimeFormatter.ISO_DATE);

        // Redis key pattern: tenant:{tenant_id}:candidate:{candidate_id}:submissions:{date}
        String redisKey    = tenantId + ":" + candidateId + ":" + today;
        int    currentCount = SubmissionHandler.rateLimitStore.getOrDefault(redisKey, 0);
        int    dailyLimit   = SubmissionHandler.DEFAULT_DAILY_LIMIT;
        int    remaining    = Math.max(0, dailyLimit - currentCount);
        boolean limited     = currentCount >= dailyLimit;

        // Compute reset time (end of UTC day)
        String resetAt = today + "T23:59:59Z";

        ObjectNode resp = mapper.createObjectNode();
        resp.put("tenant_id",             tenantId);
        resp.put("candidate_id",          candidateId);
        resp.put("date",                  today);
        resp.put("current_count",         currentCount);
        resp.put("daily_limit",           dailyLimit);
        resp.put("remaining_submissions", remaining);
        resp.put("reset_at_utc",          resetAt);
        resp.put("rate_limited",          limited);

        if (limited) {
            resp.put("message",
                "Candidate has reached the daily submission limit of " + dailyLimit + ". " +
                "Next submissions allowed after " + resetAt + " (UTC day rollover).");
            resp.put("http_status_on_submit", 429);
            resp.put("retry_after_header", resetAt);
        } else {
            resp.put("message", "Candidate is within rate limits. " + remaining
                + " submission(s) remaining today.");
        }

        ObjectNode impl = resp.putObject("redis_implementation");
        impl.put("key_pattern",  "tenant:{tenant_id}:candidate:{candidate_id}:submissions:{date}");
        impl.put("operation",    "INCR key → current_count; EXPIRE key to end of UTC day on first INCR");
        impl.put("ttl_strategy", "Key expires at 00:00 UTC daily — automatic daily reset");
        impl.put("production",   "ioredis client with TLS, EXPIRE set on first INCR only");
        impl.put("limit_source", "Per-tenant config from admin-service REST, cached in Redis 1h TTL");

        LOG.info("check_rate_limit: tenant=" + tenantId + " candidate=" + candidateId
                + " count=" + currentCount + "/" + dailyLimit + " date=" + today);
        return resp;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: get_submission_audit
    // ─────────────────────────────────────────────────────────────────────────

    public Object getSubmissionAudit(JsonNode args) {
        String ideaId       = require(args, "idea_id");
        String tenantId     = require(args, "tenant_id");
        String eventFilter  = args.path("event_type_filter").asText("").toUpperCase();
        int    limit        = Math.min(args.path("limit").asInt(100), 500);

        String auditKey   = tenantId + ":" + ideaId;
        List<ObjectNode> allEntries = auditStore.getOrDefault(auditKey, new ArrayList<>());

        // Apply optional event_type filter
        List<ObjectNode> filtered = eventFilter.isBlank()
            ? allEntries
            : allEntries.stream()
                .filter(e -> eventFilter.equals(e.path("event_type").asText()))
                .collect(Collectors.toList());

        // Apply limit (entries are already ordered by timestamp ascending)
        List<ObjectNode> paginated = filtered.size() > limit
            ? filtered.subList(0, limit)
            : filtered;

        ObjectNode resp = mapper.createObjectNode();
        resp.put("idea_id",          ideaId);
        resp.put("tenant_id",        tenantId);
        resp.put("total_entries",    allEntries.size());
        resp.put("filtered_entries", filtered.size());
        resp.put("returned_entries", paginated.size());
        resp.put("event_filter",     eventFilter.isBlank() ? "ALL" : eventFilter);
        resp.put("order",            "timestamp ASC (oldest first)");

        ArrayNode entries = resp.putArray("audit_entries");
        paginated.forEach(e -> entries.add(e.deepCopy()));

        if (allEntries.isEmpty()) {
            ObjectNode note = resp.putObject("note");
            note.put("message",
                "No audit entries in session cache for idea_id=" + ideaId +
                ". In production: SELECT * FROM innovation.idea_submission_audit " +
                "WHERE idea_id=? AND tenant_id=? ORDER BY timestamp ASC");
            note.put("table",        "innovation.idea_submission_audit");
            note.put("immutability", "Append-only: INSERT only, no UPDATE or DELETE");
            note.put("rls",          "Row-Level Security enforced by tenant_id");
        }

        ObjectNode schema = resp.putObject("schema_reference");
        schema.put("event_types",
            "CREATED | SUBMITTED | REVISED | APPROVED | FLAGGED | LICENSED | ARCHIVED | " +
            "VISIBILITY_CHANGED | INTEGRITY_VERIFIED | TAMPER_DETECTED | RATE_LIMIT_HIT | OWNERSHIP_QUERIED");
        schema.put("retention", "Audit logs retained 3 years (platform compliance)");
        schema.put("use_cases",
            "Abuse detection, support escalations, IP dispute evidence, GDPR audit trails, " +
            "patent evidence, compliance audits");

        LOG.info("get_submission_audit: idea_id=" + ideaId + " total=" + allEntries.size()
                + " filtered=" + filtered.size());
        return resp;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Static helper — all handlers write audit entries here
    // ─────────────────────────────────────────────────────────────────────────

    public static void appendAudit(String tenantId, String ideaId,
                                    String eventType, String actorId,
                                    String description, ObjectMapper mapper) {
        String key = tenantId + ":" + (ideaId != null ? ideaId : "NO_IDEA");
        auditStore.computeIfAbsent(key, k -> new ArrayList<>())
            .add(buildEntry(tenantId, ideaId, eventType, actorId, description, mapper));
    }

    private static ObjectNode buildEntry(String tenantId, String ideaId,
                                          String eventType, String actorId,
                                          String description, ObjectMapper mapper) {
        ObjectNode e = mapper.createObjectNode();
        e.put("audit_id",    UUID.randomUUID().toString());
        e.put("timestamp",   Instant.now().toString());
        e.put("tenant_id",   tenantId);
        if (ideaId != null) e.put("idea_id", ideaId);
        e.put("event_type",  eventType);
        e.put("actor_id",    actorId != null ? actorId : "system");
        e.put("description", description);
        return e;
    }

    private String require(JsonNode args, String field) {
        JsonNode n = args.get(field);
        if (n == null || n.isNull() || n.asText().isBlank())
            throw new IllegalArgumentException("Missing required field: " + field);
        return n.asText().trim();
    }
}
