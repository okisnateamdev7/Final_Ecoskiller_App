package com.ecoskiller.mcp.its.handlers;

import com.ecoskiller.mcp.its.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

/**
 * SubmissionHandler
 *
 * Implements:
 *   submit_idea  — SHA-256 hash, authoritative timestamp, MinIO archive, Kafka publish
 *   revise_idea  — Versioned re-submission with new hash & timestamp
 *
 * Per ITS spec §3.1–3.7:
 *   - SHA-256 from Node.js crypto module equivalent → Java MessageDigest("SHA-256")
 *   - Canonical JSON: deterministic serialization title + description + attachment bytes
 *   - submitted_at from PostgreSQL transaction clock (simulated as Instant.now UTC)
 *   - MinIO key: ideas/{idea_id}/v{n}/submission.json
 *   - Kafka topic: idea.submitted (Avro / Apicurio schema-validated in production)
 *   - Rate limit: Redis INCR/EXPIRE pattern (simulated per tenant+candidate+date)
 *
 * Storage (simulated — production: PostgreSQL innovation.ideas + MinIO):
 *   - ideaStore: in-memory map representing innovation.ideas rows
 *   - versionStore: in-memory map representing innovation.idea_versions rows
 *   - rateLimitStore: in-memory map representing Redis rate counters
 *   - auditStore: shared with AuditHandler via static reference
 */
public class SubmissionHandler {

    private static final Logger LOG = Logger.getLogger(SubmissionHandler.class.getName());

    // Per ITS spec §8: max submissions per candidate per tenant per day (default)
    public static final int DEFAULT_DAILY_LIMIT = 20;

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator validator;

    // Shared in-process stores (production: PostgreSQL + Redis + MinIO)
    static final Map<String, ObjectNode>       ideaStore      = new LinkedHashMap<>();
    static final Map<String, List<ObjectNode>> versionStore   = new LinkedHashMap<>();
    // key = tenantId:candidateId:date  → count
    static final Map<String, Integer>          rateLimitStore = new LinkedHashMap<>();

    public SubmissionHandler(SecurityValidator sv) {
        this.validator = sv;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: submit_idea
    // ─────────────────────────────────────────────────────────────────────────

    public Object submitIdea(JsonNode args) {
        String tenantId       = require(args, "tenant_id");
        String candidateId    = require(args, "candidate_id");
        String title          = require(args, "title");
        String description    = require(args, "description");
        String visibility     = require(args, "visibility_level").toUpperCase();
        String category       = args.path("idea_category").asText("general");
        String techDetails    = args.path("technical_details").asText("");
        int    attachCount    = args.path("attachment_count").asInt(0);

        // ── Rate limit check (Redis INCR pattern) ─────────────────────────
        String today    = LocalDate.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE);
        String rlKey    = tenantId + ":" + candidateId + ":" + today;
        int    rlCount  = rateLimitStore.getOrDefault(rlKey, 0);
        if (rlCount >= DEFAULT_DAILY_LIMIT) {
            ObjectNode err = mapper.createObjectNode();
            err.put("error", "RATE_LIMIT_EXCEEDED");
            err.put("message", "Candidate has reached the daily submission limit of " + DEFAULT_DAILY_LIMIT);
            err.put("retry_after_utc", today + "T23:59:59Z");
            err.put("http_status", 429);
            AuditHandler.appendAudit(tenantId, null, "RATE_LIMIT_HIT", candidateId,
                "Rate limit exceeded for candidate " + candidateId + " on " + today, mapper);
            return err;
        }

        // ── Generate idea_id + authoritative submitted_at ─────────────────
        String  ideaId      = UUID.randomUUID().toString();
        Instant submittedAt = Instant.now();           // production: PostgreSQL transaction clock

        // ── SHA-256 content hash (per ITS spec §3.2) ─────────────────────
        // Canonical payload: title + description + techDetails (+ attachment byte content in production)
        String canonical    = buildCanonicalJson(ideaId, tenantId, candidateId,
                                                 title, description, techDetails, attachCount);
        String contentHash  = sha256(canonical);

        // ── MinIO object key (per ITS spec §3.4) ─────────────────────────
        String minioKey     = "ideas/" + ideaId + "/v1/submission.json";
        String minioEtag    = md5Hex(canonical); // production: real MinIO putObject ETag

        // ── Persist to innovation.ideas (per ITS spec §3.5) ──────────────
        ObjectNode record = mapper.createObjectNode();
        record.put("idea_id",          ideaId);
        record.put("tenant_id",        tenantId);
        record.put("candidate_id",     candidateId);
        record.put("idea_title",       title);           // pgcrypto-encrypted for PRIVATE in prod
        record.put("description_hash", sha256(description));
        record.put("content_hash",     contentHash);     // immutable after creation
        record.put("submitted_at",     submittedAt.toString()); // IMMUTABLE
        record.put("minio_object_key", minioKey);
        record.put("minio_etag",       minioEtag);
        record.put("visibility_level", visibility);
        record.put("idea_category",    category);
        record.put("status",           "PENDING_FINGERPRINT");
        record.put("version",          1);
        record.put("created_at",       submittedAt.toString());
        ideaStore.put(storeKey(tenantId, ideaId), record);

        // Also seed version record v1
        versionStore.computeIfAbsent(storeKey(tenantId, ideaId), k -> new ArrayList<>())
            .add(buildVersionRecord(ideaId, tenantId, 1, contentHash,
                                    submittedAt, minioKey, minioEtag, null));

        // ── Increment rate-limit counter ──────────────────────────────────
        rateLimitStore.put(rlKey, rlCount + 1);

        // ── Audit log ─────────────────────────────────────────────────────
        AuditHandler.appendAudit(tenantId, ideaId, "CREATED", candidateId,
            "Idea submitted. SHA-256=" + contentHash + " visibility=" + visibility +
            " category=" + category + " attachments=" + attachCount, mapper);

        // ── Build response (per ITS spec §6.1) ───────────────────────────
        ObjectNode resp = mapper.createObjectNode();
        resp.put("idea_id",          ideaId);
        resp.put("version",          1);
        resp.put("submitted_at",     submittedAt.toString());
        resp.put("content_hash",     contentHash);
        resp.put("minio_object_key", minioKey);
        resp.put("minio_etag",       minioEtag);
        resp.put("visibility_level", visibility);
        resp.put("status",           "PENDING_FINGERPRINT");
        resp.put("idea_category",    category);
        resp.put("rate_limit_count_today", rlCount + 1);
        resp.put("rate_limit_daily_limit", DEFAULT_DAILY_LIMIT);

        // Kafka event payload (per ITS spec §6.3)
        ObjectNode kafkaEvent = resp.putObject("kafka_event_published");
        kafkaEvent.put("topic",           "idea.submitted");
        kafkaEvent.put("key",             tenantId + ":" + ideaId);
        kafkaEvent.put("idea_id",         ideaId);
        kafkaEvent.put("tenant_id",       tenantId);
        kafkaEvent.put("candidate_id",    candidateId);
        kafkaEvent.put("version",         1);
        kafkaEvent.put("submitted_at",    submittedAt.toString());
        kafkaEvent.put("content_hash",    contentHash);
        kafkaEvent.put("minio_object_key",minioKey);
        kafkaEvent.put("visibility_level",visibility);
        kafkaEvent.put("idea_category",   category);
        kafkaEvent.put("is_revision",     false);
        kafkaEvent.put("schema_version",  "idea.submitted.v1");

        // Production notes
        ObjectNode notes = resp.putObject("production_notes");
        notes.put("submitted_at_source",
            "PostgreSQL transaction-clock NOW() at write open — monotonic server clock, NOT client-supplied");
        notes.put("content_hash_algo", "SHA-256 via Java MessageDigest");
        notes.put("minio_bucket",      "ecoskiller-ideas-" + tenantId);
        notes.put("minio_versioning",  "ENABLED — original v1 preserved even on subsequent writes");
        notes.put("pgcrypto",
            visibility.equals("PRIVATE")
                ? "idea_title + description_summary encrypted with AES-256-CBC (pgcrypto, per-tenant key)"
                : "Not applicable — PRIVATE only");
        notes.put("kafka_consumers",
            "idea-dna-fingerprint-engine, analytics-service, notification-service, search-indexer");

        LOG.info("submit_idea: idea_id=" + ideaId + " tenant=" + tenantId + " candidate=" + candidateId
                + " visibility=" + visibility + " hash=" + contentHash.substring(0, 8) + "...");
        return resp;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: revise_idea
    // ─────────────────────────────────────────────────────────────────────────

    public Object reviseIdea(JsonNode args) {
        String ideaId        = require(args, "idea_id");
        String tenantId      = require(args, "tenant_id");
        String candidateId   = require(args, "candidate_id");
        String title         = require(args, "title");
        String description   = require(args, "description");
        String visibility    = require(args, "visibility_level").toUpperCase();
        String revReason     = args.path("revision_reason").asText("");
        String category      = args.path("idea_category").asText("");
        String techDetails   = args.path("technical_details").asText("");

        String key           = storeKey(tenantId, ideaId);

        // ── Ownership check ───────────────────────────────────────────────
        ObjectNode existing  = ideaStore.get(key);
        if (existing == null) {
            ObjectNode err = mapper.createObjectNode();
            err.put("error", "IDEA_NOT_FOUND");
            err.put("message", "No idea found for idea_id=" + ideaId + " in tenant=" + tenantId
                + ". Submit via submit_idea first.");
            return err;
        }
        String originalOwner = existing.path("candidate_id").asText();
        if (!originalOwner.equals(candidateId)) {
            ObjectNode err = mapper.createObjectNode();
            err.put("error", "UNAUTHORIZED");
            err.put("message", "Only the original candidate (" + originalOwner
                + ") may revise this idea.");
            return err;
        }

        // ── Determine new version number ──────────────────────────────────
        List<ObjectNode> versions   = versionStore.getOrDefault(key, new ArrayList<>());
        int              newVersion = versions.size() + 1;
        Instant          revisedAt  = Instant.now();

        // ── New SHA-256 for revised content ───────────────────────────────
        String canonical   = buildCanonicalJson(ideaId, tenantId, candidateId,
                                                title, description, techDetails, 0);
        String contentHash = sha256(canonical);
        String minioKey    = "ideas/" + ideaId + "/v" + newVersion + "/submission.json";
        String minioEtag   = md5Hex(canonical);

        // ── Append to innovation.idea_versions (append-only, per spec §6.4) ──
        String effectiveCategory = category.isBlank() ? existing.path("idea_category").asText("general") : category;
        ObjectNode vRec = buildVersionRecord(ideaId, tenantId, newVersion, contentHash,
                                             revisedAt, minioKey, minioEtag, revReason);
        versions.add(vRec);
        versionStore.put(key, versions);

        // ── Update parent idea record (status resets, immutables unchanged) ──
        existing.put("status",           "PENDING_FINGERPRINT");
        existing.put("idea_title",       title);
        existing.put("description_hash", sha256(description));
        existing.put("visibility_level", visibility);
        existing.put("idea_category",    effectiveCategory);
        existing.put("version",          newVersion);
        // submitted_at and original content_hash remain unchanged in innovation.ideas
        // (only the current_version and status are mutable)

        // ── Audit ─────────────────────────────────────────────────────────
        AuditHandler.appendAudit(tenantId, ideaId, "REVISED", candidateId,
            "Revision v" + newVersion + ". New SHA-256=" + contentHash +
            (revReason.isBlank() ? "" : ". Reason: " + revReason), mapper);

        // ── Response ──────────────────────────────────────────────────────
        ObjectNode resp = mapper.createObjectNode();
        resp.put("idea_id",            ideaId);
        resp.put("version",            newVersion);
        resp.put("submitted_at",       revisedAt.toString());
        resp.put("content_hash",       contentHash);
        resp.put("previous_version",   newVersion - 1);
        resp.put("minio_object_key",   minioKey);
        resp.put("minio_etag",         minioEtag);
        resp.put("visibility_level",   visibility);
        resp.put("status",             "PENDING_FINGERPRINT");
        resp.put("revision_reason",    revReason);
        resp.put("original_submitted_at",
            existing.path("created_at").asText()); // v1 timestamp unchanged

        ObjectNode kafkaEvent = resp.putObject("kafka_event_published");
        kafkaEvent.put("topic",            "idea.submitted");
        kafkaEvent.put("idea_id",          ideaId);
        kafkaEvent.put("tenant_id",        tenantId);
        kafkaEvent.put("candidate_id",     candidateId);
        kafkaEvent.put("version",          newVersion);
        kafkaEvent.put("submitted_at",     revisedAt.toString());
        kafkaEvent.put("content_hash",     contentHash);
        kafkaEvent.put("minio_object_key", minioKey);
        kafkaEvent.put("visibility_level", visibility);
        kafkaEvent.put("is_revision",      true);
        kafkaEvent.put("parent_idea_id",   ideaId);
        kafkaEvent.put("schema_version",   "idea.submitted.v1");

        LOG.info("revise_idea: idea_id=" + ideaId + " v" + newVersion + " tenant=" + tenantId
                + " hash=" + contentHash.substring(0, 8) + "...");
        return resp;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Canonical JSON payload for SHA-256.
     * Production (Node.js): JSON.stringify({ ideaId, tenantId, candidateId, version,
     *   submittedAt, title, description, attachments: [{filename, content_hash, size_bytes}] })
     * Here we use a deterministic concatenation as a faithful equivalent.
     */
    private String buildCanonicalJson(String ideaId, String tenantId, String candidateId,
                                      String title, String description, String techDetails,
                                      int attachCount) {
        return "{\"idea_id\":\"" + ideaId +
               "\",\"tenant_id\":\"" + tenantId +
               "\",\"candidate_id\":\"" + candidateId +
               "\",\"title\":\"" + escapeJson(title) +
               "\",\"description\":\"" + escapeJson(description) +
               "\",\"technical_details\":\"" + escapeJson(techDetails) +
               "\",\"attachment_count\":" + attachCount + "}";
    }

    private ObjectNode buildVersionRecord(String ideaId, String tenantId, int versionNum,
                                          String contentHash, Instant submittedAt,
                                          String minioKey, String minioEtag, String revReason) {
        ObjectNode v = mapper.createObjectNode();
        v.put("version_id",      UUID.randomUUID().toString());
        v.put("idea_id",         ideaId);
        v.put("tenant_id",       tenantId);
        v.put("version_number",  versionNum);
        v.put("content_hash",    contentHash);
        v.put("submitted_at",    submittedAt.toString());
        v.put("minio_object_key",minioKey);
        v.put("minio_etag",      minioEtag);
        if (revReason != null && !revReason.isBlank())
            v.put("revision_reason", revReason);
        v.put("created_at",      submittedAt.toString());
        return v;
    }

    /** SHA-256 → lowercase hex */
    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(64);
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 unavailable", e);
        }
    }

    /** MD5 → lowercase hex (simulates MinIO ETag for plain objects) */
    public static String md5Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(32);
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5 unavailable", e);
        }
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    static String storeKey(String tenantId, String ideaId) {
        return tenantId + ":" + ideaId;
    }

    private String require(JsonNode args, String field) {
        JsonNode n = args.get(field);
        if (n == null || n.isNull() || n.asText().isBlank())
            throw new IllegalArgumentException("Missing required field: " + field);
        return n.asText().trim();
    }
}
