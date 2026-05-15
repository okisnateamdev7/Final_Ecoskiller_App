package com.ecoskiller.mcp.isd.handlers;

import com.ecoskiller.mcp.isd.engine.ScoringEngine;
import com.ecoskiller.mcp.isd.engine.VectorEngine;
import com.ecoskiller.mcp.isd.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;
import java.util.function.Function;

/**
 * ToolRegistry — 13-tool registry with full MCP schema definitions and handler dispatch.
 */
public class ToolRegistry {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, Function<JsonNode, Object>> handlers;

    public ToolRegistry(SecurityValidator sv) {
        VectorEngine  ve = new VectorEngine();
        ScoringEngine se = new ScoringEngine();

        SimilarityCheckHandler   check   = new SimilarityCheckHandler(sv, ve, se);
        ReportHandler            report  = new ReportHandler(sv, ve, se);
        ClassificationHandler    cls     = new ClassificationHandler(sv, se);
        ThresholdHandler         thresh  = new ThresholdHandler(sv);
        OperationsHandler        ops     = new OperationsHandler(sv);

        this.handlers = Map.ofEntries(
            Map.entry("run_similarity_check",      check::runSimilarityCheck),
            Map.entry("run_similarity_recheck",    check::runSimilarityRecheck),
            Map.entry("get_similarity_report",     report::getSimilarityReport),
            Map.entry("get_similarity_audit",      report::getSimilarityAudit),
            Map.entry("list_similarity_reports",   report::listSimilarityReports),
            Map.entry("get_classification",        cls::getClassification),
            Map.entry("compute_copy_probability",  cls::computeCopyProbability),
            Map.entry("get_top_matches",           cls::getTopMatches),
            Map.entry("get_threshold_config",      thresh::getThresholdConfig),
            Map.entry("set_threshold_config",      thresh::setThresholdConfig),
            Map.entry("get_dlq_status",            ops::getDlqStatus),
            Map.entry("get_consumer_health",       ops::getConsumerHealth),
            Map.entry("get_similarity_metrics",    ops::getSimilarityMetrics)
        );
    }

    public Object call(String toolName, JsonNode arguments) {
        Function<JsonNode, Object> h = handlers.get(toolName);
        if (h == null) {
            ObjectNode err = mapper.createObjectNode();
            err.put("error", "Unknown tool: " + toolName);
            return err;
        }
        try {
            return h.apply(arguments);
        } catch (IllegalArgumentException | SecurityException e) {
            ObjectNode err = mapper.createObjectNode();
            err.put("error", e.getMessage());
            return err;
        }
    }

    // ─── Tool definitions ────────────────────────────────────────────────────

    public ArrayNode getToolDefinitions() {
        ArrayNode tools = mapper.createArrayNode();

        tools.add(tool("run_similarity_check",
            "Core pipeline: Run a full similarity check for a newly fingerprinted idea.\n" +
            "Workflow per ISD spec §3.1–3.5:\n" +
            "(1) Retrieve 384-dim embedding from Qdrant by vector_id (avoids recomputation).\n" +
            "(2) Execute top-K=20 ANN cosine search in idea-embeddings collection, " +
            "tenant-scoped via payload filter, with optional category-aware filtering.\n" +
            "(3) Compute weighted copy_probability score " +
            "(0.60×max_cosine + 0.25×above_medium_count + 0.15×category_match_rate).\n" +
            "(4) Classify: CLEAR (<0.60) | REVIEW (0.60–0.79) | FLAG (≥0.80).\n" +
            "(5) Persist full report to innovation.idea_similarity_reports (PostgreSQL RLS).\n" +
            "(6) Publish Kafka event: idea.similarity.cleared|review|flagged.\n" +
            "Returns: copy_probability, classification, top_matches (max 5), " +
            "category_match_rate, k_results_count, checked_at.",
            schema()
                .required("idea_id", "tenant_id", "vector_id")
                .string("idea_id",       "UUID of the idea being checked")
                .string("tenant_id",     "Tenant scope UUID — search restricted within tenant")
                .string("candidate_id",  "UUID of submitting candidate (optional — stored in report)")
                .string("vector_id",     "Qdrant point ID for the idea's stored 384-dim embedding")
                .string("idea_category", "Semantic category tag for category-aware ANN filtering (optional)")
                .integer("top_k",        "Max ANN results to retrieve (1–100, default 20)")
                .number("min_similarity","Minimum cosine similarity floor (0.0–1.0, default 0.40)")
                .build()));

        tools.add(tool("run_similarity_recheck",
            "Idempotent re-execution of similarity check for an existing idea.\n" +
            "Required in two scenarios per ISD spec §3.7:\n" +
            "(1) Idea text amended after initial submission — new embedding available.\n" +
            "(2) Corpus growth: new ideas added that may retroactively conflict with " +
            "previously cleared ideas.\n" +
            "Previous report is OVERWRITTEN in innovation.idea_similarity_reports. " +
            "Previous version is archived to innovation.idea_similarity_audit.\n" +
            "recheck_count incremented on each execution.",
            schema()
                .required("idea_id", "tenant_id", "vector_id")
                .string("idea_id",      "UUID of the idea to recheck")
                .string("tenant_id",    "Tenant scope UUID")
                .string("vector_id",    "Qdrant point ID for the updated embedding")
                .string("idea_category","Category for ANN filtering (optional)")
                .string("recheck_reason","Reason: IDEA_AMENDED | CORPUS_GROWTH | ADMIN_REQUEST (optional)")
                .integer("top_k",       "Max ANN results (default 20)")
                .build()));

        tools.add(tool("get_similarity_report",
            "Retrieve the stored similarity report for an idea from " +
            "innovation.idea_similarity_reports (PostgreSQL RLS enforced).\n" +
            "Fields: report_id, tenant_id, idea_id, candidate_id, copy_probability, " +
            "classification, top_matches (JSONB), k_results_count, category_match_rate, " +
            "checked_at, recheck_count.\n" +
            "Used by admin review workflows, dispute resolution, and licensing eligibility checks.",
            schema()
                .required("idea_id", "tenant_id")
                .string("idea_id",  "UUID of the idea")
                .string("tenant_id","Tenant scope UUID")
                .build()));

        tools.add(tool("get_similarity_audit",
            "Retrieve the append-only audit log for similarity checks on an idea.\n" +
            "Table: innovation.idea_similarity_audit — stores full previous report on " +
            "each recheck, enabling dispute resolution with historical record.\n" +
            "Returns chronological list of all check runs: report snapshot, recheck_reason, " +
            "checked_at, classification history.",
            schema()
                .required("idea_id", "tenant_id")
                .string("idea_id",  "UUID of the idea")
                .string("tenant_id","Tenant scope UUID")
                .integer("limit",   "Max audit entries (1–500, default 50)")
                .build()));

        tools.add(tool("list_similarity_reports",
            "Paginated list of all similarity reports for a tenant.\n" +
            "Supports filtering by classification tier and idea category.\n" +
            "Ordered by checked_at DESC (most recent first).\n" +
            "PostgreSQL RLS enforced — only reports within tenant_id returned.",
            schema()
                .required("tenant_id")
                .string("tenant_id",    "Tenant scope UUID")
                .string("classification","Filter by tier: CLEAR | REVIEW | FLAG (optional)")
                .string("idea_category","Filter by category (optional)")
                .integer("limit",       "Results per page (1–500, default 20)")
                .integer("page",        "Zero-indexed page number (default 0)")
                .build()));

        tools.add(tool("get_classification",
            "Return the current classification tier for an idea.\n" +
            "Returns: CLEAR | REVIEW | FLAG, copy_probability, classification_description, " +
            "automated_action, checked_at, and licensing_eligible flag.\n" +
            "Only ideas with CLEAR classification are eligible for licensing (Module XIII).",
            schema()
                .required("idea_id", "tenant_id")
                .string("idea_id",  "UUID of the idea")
                .string("tenant_id","Tenant scope UUID")
                .build()));

        tools.add(tool("compute_copy_probability",
            "Compute or recompute the weighted copy_probability from pre-computed ANN signals.\n" +
            "Formula: 0.60×max_cosine_similarity + 0.25×(above_medium_count/10) + 0.15×category_match_rate\n" +
            "Useful for: threshold tuning simulations, what-if analysis, admin overrides.\n" +
            "Returns: copy_probability, classification, signal_breakdown, recommended_action.",
            schema()
                .required("idea_id", "tenant_id")
                .string("idea_id",            "UUID of the idea")
                .string("tenant_id",          "Tenant scope UUID")
                .number("max_cosine_similarity","Highest cosine score from ANN search [0.0–1.0]")
                .integer("above_medium_count", "Number of ANN results with similarity >= 0.60")
                .number("category_match_rate", "Fraction of top-K results matching idea category [0.0–1.0]")
                .number("review_threshold",    "Override REVIEW threshold (default 0.60)")
                .number("flag_threshold",      "Override FLAG threshold (default 0.80)")
                .build()));

        tools.add(tool("get_top_matches",
            "Return the top-K similar ideas for a given idea from the stored similarity report.\n" +
            "If no stored report exists, executes a live ANN search.\n" +
            "Each match includes: matched_idea_id, matched_candidate_id, cosine_similarity, " +
            "matched_category, submission_timestamp, similarity_tier.\n" +
            "Used by admin review UI and dispute resolution workflows.",
            schema()
                .required("idea_id", "tenant_id")
                .string("idea_id",      "UUID of the idea")
                .string("tenant_id",    "Tenant scope UUID")
                .string("vector_id",    "Qdrant point ID for live search (required if no cached report)")
                .string("idea_category","Category filter for live search (optional)")
                .integer("top_k",       "Max matches to return (1–100, default 5)")
                .number("min_similarity","Minimum cosine score filter [0.0–1.0, default 0.40]")
                .build()));

        tools.add(tool("get_threshold_config",
            "Retrieve the per-tenant similarity threshold configuration.\n" +
            "Thresholds are cached in Redis (key: tenant:{tenant_id}:similarity:thresholds, TTL: 5 min).\n" +
            "Cache miss triggers REST call to admin-service.\n" +
            "Returns: review_threshold, flag_threshold, min_similarity_floor, cache_ttl_remaining_s.",
            schema()
                .required("tenant_id")
                .string("tenant_id","Tenant scope UUID")
                .build()));

        tools.add(tool("set_threshold_config",
            "Update per-tenant threshold configuration (admin role required).\n" +
            "Writes to Redis cache immediately (TTL: 5 min) and triggers admin-service REST update.\n" +
            "Semantic ordering enforced: flag_threshold > review_threshold > min_similarity_floor.\n" +
            "Enterprise tenants may set stricter thresholds for higher originality standards.",
            schema()
                .required("tenant_id")
                .string("tenant_id",          "Tenant scope UUID")
                .number("review_threshold",    "REVIEW tier lower bound (default 0.60). Must be < flag_threshold.")
                .number("flag_threshold",      "FLAG tier lower bound (default 0.80). Must be > review_threshold.")
                .number("min_similarity_floor","Minimum ANN score floor (default 0.40). Discard results below this.")
                .string("updated_by",          "Admin user UUID performing the change (optional)")
                .build()));

        tools.add(tool("get_dlq_status",
            "Inspect Dead-Letter Queue (DLQ) state for idea.fingerprinted.dlq topic.\n" +
            "Per ISD spec §3.6 / Appendix B: three-retry policy (1s, 4s, 16s) before DLQ.\n" +
            "DLQ alert: Prometheus fires when DLQ count > 0 within 5-minute window.\n" +
            "Returns: dlq_message_count, retry_attempts, last_failure_reason, " +
            "alert_firing, remediation_steps.",
            schema()
                .required("tenant_id")
                .string("tenant_id","Tenant scope UUID")
                .string("idea_id",  "Filter for a specific idea (optional)")
                .build()));

        tools.add(tool("get_consumer_health",
            "Retrieve Kafka consumer health and Qdrant connection status.\n" +
            "Per ISD spec §3.6 and Appendix C post-deploy smoke test.\n" +
            "Returns: kafka_consumer_lag (idea.fingerprinted topic), qdrant_status, " +
            "dlq_count, hpa_replicas (current/min/max), consumer_group_id, last_heartbeat.",
            schema()
                .required("tenant_id")
                .string("tenant_id","Tenant scope UUID")
                .build()));

        tools.add(tool("get_similarity_metrics",
            "Return Prometheus metrics snapshot for the similarity detector.\n" +
            "Per ISD spec §6.4: exposes metrics on /metrics endpoint.\n" +
            "Metrics: idea_similarity_check_duration_seconds (histogram), " +
            "qdrant_ann_query_duration_seconds (histogram), " +
            "idea_similarity_classification_total{classification} (counter per tier), " +
            "idea_similarity_dlq_total (counter), kafka_consumer_lag (gauge).",
            schema()
                .required("tenant_id")
                .string("tenant_id","Tenant scope UUID")
                .build()));

        return tools;
    }

    // ── DSL helpers ───────────────────────────────────────────────────────────

    private ObjectNode tool(String name, String description, ObjectNode inputSchema) {
        ObjectNode t = mapper.createObjectNode();
        t.put("name",        name);
        t.put("description", description);
        t.set("inputSchema", inputSchema);
        return t;
    }

    private SchemaBuilder schema() { return new SchemaBuilder(mapper); }

    private static class SchemaBuilder {
        private final ObjectMapper mapper;
        private final ObjectNode   schema;
        private final ObjectNode   props;
        private final ArrayNode    required;

        SchemaBuilder(ObjectMapper m) {
            this.mapper   = m;
            this.schema   = m.createObjectNode();
            this.props    = m.createObjectNode();
            this.required = m.createArrayNode();
            schema.put("type", "object");
            schema.set("properties", props);
        }
        SchemaBuilder required(String... fields) {
            for (String f : fields) required.add(f); return this; }
        SchemaBuilder string(String name, String desc) {
            ObjectNode p = mapper.createObjectNode();
            p.put("type", "string"); p.put("description", desc);
            props.set(name, p); return this; }
        SchemaBuilder integer(String name, String desc) {
            ObjectNode p = mapper.createObjectNode();
            p.put("type", "integer"); p.put("description", desc);
            props.set(name, p); return this; }
        SchemaBuilder number(String name, String desc) {
            ObjectNode p = mapper.createObjectNode();
            p.put("type", "number"); p.put("description", desc);
            props.set(name, p); return this; }
        ObjectNode build() {
            if (required.size() > 0) schema.set("required", required);
            return schema; }
    }
}
