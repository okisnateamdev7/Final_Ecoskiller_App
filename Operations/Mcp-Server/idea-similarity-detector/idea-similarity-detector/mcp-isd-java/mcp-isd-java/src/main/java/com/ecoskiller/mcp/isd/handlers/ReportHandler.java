package com.ecoskiller.mcp.isd.handlers;

import com.ecoskiller.mcp.isd.engine.ScoringEngine;
import com.ecoskiller.mcp.isd.engine.VectorEngine;
import com.ecoskiller.mcp.isd.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * ReportHandler
 *
 * Implements:
 *   get_similarity_report   — Retrieve stored report from innovation.idea_similarity_reports
 *   get_similarity_audit    — Full audit trail from innovation.idea_similarity_audit
 *   list_similarity_reports — Paginated listing with classification filter
 */
public class ReportHandler {

    private static final Logger LOG = Logger.getLogger(ReportHandler.class.getName());

    private final ObjectMapper  mapper = new ObjectMapper();
    private final SecurityValidator validator;
    private final VectorEngine  vectorEngine;
    private final ScoringEngine scoringEngine;

    public ReportHandler(SecurityValidator sv, VectorEngine ve, ScoringEngine se) {
        this.validator    = sv;
        this.vectorEngine = ve;
        this.scoringEngine = se;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: get_similarity_report
    // ─────────────────────────────────────────────────────────────────────────

    public Object getSimilarityReport(JsonNode args) {
        String ideaId   = req(args, "idea_id");
        String tenantId = req(args, "tenant_id");
        String key      = tenantId + ":" + ideaId;

        ObjectNode resp = mapper.createObjectNode();
        resp.put("idea_id",   ideaId);
        resp.put("tenant_id", tenantId);

        ObjectNode stored = SimilarityCheckHandler.reportStore.get(key);
        if (stored == null) {
            resp.put("found",   false);
            resp.put("message",
                "No similarity report found in session cache. " +
                "In production: SELECT * FROM innovation.idea_similarity_reports " +
                "WHERE idea_id=? AND tenant_id=? (PostgreSQL RLS enforced).");
            addSchemaNote(resp);
            return resp;
        }

        resp.put("found", true);
        resp.set("report", stored.deepCopy());

        // Licensing eligibility gate (per ISD spec §9.3)
        String classification = stored.path("classification").asText("UNKNOWN");
        ObjectNode licensing = resp.putObject("licensing_eligibility");
        licensing.put("eligible",   "CLEAR".equals(classification));
        licensing.put("classification", classification);
        licensing.put("note",
            "CLEAR".equals(classification)
                ? "Idea passed similarity check. Eligible for licensing-contract-service (Module XIII)."
                : "Only CLEAR ideas are eligible for licensing. Current: " + classification + ".");

        LOG.info("get_similarity_report: idea_id=" + ideaId + " found=true class=" + classification);
        return resp;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: get_similarity_audit
    // ─────────────────────────────────────────────────────────────────────────

    public Object getSimilarityAudit(JsonNode args) {
        String ideaId   = req(args, "idea_id");
        String tenantId = req(args, "tenant_id");
        int    limit    = Math.min(args.path("limit").asInt(50), 500);
        String key      = tenantId + ":" + ideaId;

        List<ObjectNode> allEntries = SimilarityCheckHandler.auditStore.getOrDefault(key, List.of());
        List<ObjectNode> paginated  = allEntries.size() > limit
            ? allEntries.subList(0, limit) : allEntries;

        ObjectNode resp = mapper.createObjectNode();
        resp.put("idea_id",       ideaId);
        resp.put("tenant_id",     tenantId);
        resp.put("total_entries", allEntries.size());
        resp.put("returned",      paginated.size());
        resp.put("order",         "chronological ASC (oldest recheck first)");

        ArrayNode entries = resp.putArray("audit_entries");
        paginated.forEach(e -> {
            ObjectNode entry = entries.addObject();
            entry.put("recheck_snapshot_report_id", e.path("report_id").asText());
            entry.put("classification",             e.path("classification").asText());
            entry.put("copy_probability",           e.path("copy_probability").asDouble());
            entry.put("k_results_count",            e.path("k_results_count").asInt());
            entry.put("checked_at",                 e.path("checked_at").asText());
            entry.put("recheck_count",              e.path("recheck_count").asInt());
        });

        if (allEntries.isEmpty()) {
            ObjectNode note = resp.putObject("note");
            note.put("message",
                "No audit entries found. Audit entries are created on each recheck " +
                "(run_similarity_recheck). Initial checks do not create audit entries.");
            note.put("table",        "innovation.idea_similarity_audit");
            note.put("immutability", "Append-only: INSERT only, no UPDATE/DELETE");
        }

        ObjectNode schema = resp.putObject("schema_reference");
        schema.put("table",    "innovation.idea_similarity_audit");
        schema.put("purpose",  "Stores full previous report on each recheck for dispute resolution");
        schema.put("rls",      "Row-Level Security enforced by tenant_id");
        schema.put("retention","Retained for dispute resolution lifetime (indefinite)");

        LOG.info("get_similarity_audit: idea_id=" + ideaId + " entries=" + allEntries.size());
        return resp;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: list_similarity_reports
    // ─────────────────────────────────────────────────────────────────────────

    public Object listSimilarityReports(JsonNode args) {
        String tenantId    = req(args, "tenant_id");
        String classFilter = args.path("classification").asText("").toUpperCase();
        String catFilter   = args.path("idea_category").asText("").toLowerCase();
        int    limit       = Math.min(args.path("limit").asInt(20), 500);
        int    page        = Math.max(args.path("page").asInt(0), 0);

        // Collect all reports for this tenant
        List<ObjectNode> tenantReports = SimilarityCheckHandler.reportStore.entrySet().stream()
            .filter(e -> e.getKey().startsWith(tenantId + ":"))
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());

        // Apply filters
        if (!classFilter.isBlank()) {
            tenantReports = tenantReports.stream()
                .filter(r -> classFilter.equals(r.path("classification").asText()))
                .collect(Collectors.toList());
        }
        if (!catFilter.isBlank()) {
            tenantReports = tenantReports.stream()
                .filter(r -> catFilter.equalsIgnoreCase(r.path("idea_category").asText()))
                .collect(Collectors.toList());
        }

        int total = tenantReports.size();
        int from  = page * limit;
        int to    = Math.min(from + limit, total);
        List<ObjectNode> pageResults = from >= total ? List.of() : tenantReports.subList(from, to);

        // Classification distribution
        long clearCount  = tenantReports.stream().filter(r -> "CLEAR".equals(r.path("classification").asText())).count();
        long reviewCount = tenantReports.stream().filter(r -> "REVIEW".equals(r.path("classification").asText())).count();
        long flagCount   = tenantReports.stream().filter(r -> "FLAG".equals(r.path("classification").asText())).count();

        ObjectNode resp = mapper.createObjectNode();
        resp.put("tenant_id",           tenantId);
        resp.put("total_reports",       total);
        resp.put("page",                page);
        resp.put("limit",               limit);
        resp.put("returned",            pageResults.size());
        resp.put("classification_filter", classFilter.isBlank() ? "ALL" : classFilter);
        resp.put("category_filter",     catFilter.isBlank() ? "ALL" : catFilter);

        ObjectNode dist = resp.putObject("classification_distribution");
        dist.put("CLEAR",  clearCount);
        dist.put("REVIEW", reviewCount);
        dist.put("FLAG",   flagCount);
        dist.put("total",  total);

        ArrayNode reports = resp.putArray("reports");
        pageResults.forEach(r -> {
            ObjectNode entry = reports.addObject();
            entry.put("report_id",       r.path("report_id").asText());
            entry.put("idea_id",         r.path("idea_id").asText());
            entry.put("classification",  r.path("classification").asText());
            entry.put("copy_probability",r.path("copy_probability").asDouble());
            entry.put("k_results_count", r.path("k_results_count").asInt());
            entry.put("checked_at",      r.path("checked_at").asText());
            entry.put("recheck_count",   r.path("recheck_count").asInt());
            entry.put("idea_category",   r.path("idea_category").asText());
        });

        if (total == 0) {
            resp.put("note",
                "No reports found. Run run_similarity_check to populate. " +
                "In production: SELECT * FROM innovation.idea_similarity_reports " +
                "WHERE tenant_id=? ORDER BY checked_at DESC LIMIT ? OFFSET ?");
        }

        LOG.info("list_similarity_reports: tenant=" + tenantId + " total=" + total
                + " page=" + page + " filter=" + (classFilter.isBlank() ? "ALL" : classFilter));
        return resp;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void addSchemaNote(ObjectNode resp) {
        ObjectNode note = resp.putObject("schema_note");
        note.put("table",   "innovation.idea_similarity_reports");
        note.put("columns",
            "report_id, tenant_id, idea_id, candidate_id, copy_probability, " +
            "classification (enum), top_matches (JSONB), k_results_count, " +
            "category_match_rate, checked_at (timestamptz), recheck_count");
        note.put("rls",     "Row-Level Security enforced by tenant_id");
        note.put("index",   "(tenant_id, idea_id) — primary access pattern");
    }

    private String req(JsonNode args, String field) {
        JsonNode n = args.get(field);
        if (n == null || n.isNull() || n.asText().isBlank())
            throw new IllegalArgumentException("Missing required field: " + field);
        return n.asText().trim();
    }
}
