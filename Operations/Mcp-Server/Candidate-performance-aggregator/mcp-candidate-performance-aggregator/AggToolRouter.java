package com.ecoskiller.mcp.tools;

import java.util.*;

/**
 * Central dispatcher for all 20 candidate-performance-aggregator MCP tools.
 *
 * Groups:
 *   Score Ingestion   (4): ingest_track_score, ingest_kafka_event,
 *                          ingest_gd_snapshot, ingest_interview_snapshot
 *   Aggregation       (4): compute_aggregate, retry_aggregation,
 *                          batch_compute_aggregates, get_aggregation_status
 *   Rubric & Config   (4): configure_rubric, get_rubric, configure_cycle,
 *                          get_cycle_config
 *   Query             (4): get_aggregate_result, get_candidate_breakdown,
 *                          get_aggregation_history, get_score_history
 *   DLQ               (2): get_dlq_entries, resolve_dlq_entry
 *   Analytics         (2): get_metrics, get_audit_log
 */
public class AggToolRouter {

    private final ScoreIngestionTools  scoreIngestionTools;
    private final AggregationTools     aggregationTools;
    private final RubricConfigTools    rubricConfigTools;
    private final QueryDlqTools        queryDlqTools;

    public AggToolRouter() {
        this.scoreIngestionTools = new ScoreIngestionTools();
        this.aggregationTools    = new AggregationTools();
        this.rubricConfigTools   = new RubricConfigTools();
        this.queryDlqTools       = new QueryDlqTools();
    }

    public String dispatch(String toolName, Map<String,Object> args) {
        switch (toolName) {
            // Score Ingestion
            case "ingest_track_score":         return scoreIngestionTools.ingestTrackScore(args);
            case "ingest_kafka_event":         return scoreIngestionTools.ingestKafkaEvent(args);
            case "ingest_gd_snapshot":         return scoreIngestionTools.ingestGdSnapshot(args);
            case "ingest_interview_snapshot":  return scoreIngestionTools.ingestInterviewSnapshot(args);
            // Aggregation
            case "compute_aggregate":          return aggregationTools.computeAggregate(args);
            case "retry_aggregation":          return aggregationTools.retryAggregation(args);
            case "batch_compute_aggregates":   return aggregationTools.batchCompute(args);
            case "get_aggregation_status":     return aggregationTools.getAggregationStatus(args);
            // Rubric & Config
            case "configure_rubric":           return rubricConfigTools.configureRubric(args);
            case "get_rubric":                 return rubricConfigTools.getRubric(args);
            case "configure_cycle":            return rubricConfigTools.configureCycle(args);
            case "get_cycle_config":           return rubricConfigTools.getCycleConfig(args);
            // Query
            case "get_aggregate_result":       return queryDlqTools.getAggregateResult(args);
            case "get_candidate_breakdown":    return queryDlqTools.getCandidateBreakdown(args);
            case "get_aggregation_history":    return queryDlqTools.getAggregationHistory(args);
            case "get_score_history":          return queryDlqTools.getScoreHistory(args);
            // DLQ
            case "get_dlq_entries":            return queryDlqTools.getDlqEntries(args);
            case "resolve_dlq_entry":          return queryDlqTools.resolveDlqEntry(args);
            // Analytics
            case "get_metrics":                return queryDlqTools.getMetrics(args);
            case "get_audit_log":              return queryDlqTools.getAuditLog(args);
            default: throw new IllegalArgumentException("Unknown tool: " + toolName);
        }
    }

    // ── tools/list definition ─────────────────────────────────────────────────

    public List<Map<String,Object>> getToolsDefinition() {
        List<Map<String,Object>> tools = new ArrayList<>();

        // ── Score Ingestion ───────────────────────────────────────────────────
        tools.add(t("ingest_track_score",
            "Ingest a single assessment track score for a candidate in a cycle. " +
            "Validates score 0-100. Deduplicates by event_id (Redis dedup:{event_id} TTL 48h). " +
            "Tracks: GD (group discussion), INTERVIEW, DOJO (coding match). " +
            "This feeds the aggregation pipeline — call compute_aggregate after ingesting " +
            "all expected tracks. SERVICE_ACCOUNT or ADMIN role required.",
            p("auth_token",     "string","Bearer JWT (SERVICE_ACCOUNT or ADMIN)"),
            p("tenant_id",      "string","Tenant identifier (multi-tenant RLS isolation)"),
            p("candidate_id",   "string","Candidate UUID"),
            p("cycle_id",       "string","Assessment cycle UUID"),
            p("event_id",       "string","Unique event ID for deduplication"),
            p("track",          "string","GD | INTERVIEW | DOJO"),
            p("raw_score",      "string","Raw track score 0-100"),
            p("scored_at",      "string","ISO-8601 scoring timestamp"),
            p("metadata",       "string","Optional JSON: {difficulty, assessor_id, session_id}")
        ));

        tools.add(t("ingest_kafka_event",
            "Consume a match.scored Kafka event from the scoring-engine. " +
            "PRIMARY EVENT-DRIVEN ENTRY POINT — triggered by scoring-engine after dojo match. " +
            "Parses the match.scored payload, extracts candidate_id, cycle_id, raw_score, track. " +
            "Deduplicates by event_id. Triggers full aggregation pipeline automatically " +
            "after score ingestion. SERVICE_ACCOUNT only.",
            p("auth_token",    "string","Bearer JWT (SERVICE_ACCOUNT)"),
            p("tenant_id",     "string","Tenant identifier"),
            p("event_payload", "string","JSON: {event_id, candidate_id, cycle_id, track, raw_score, tenant_id, scored_at}"),
            p("auto_aggregate","string","true = trigger compute_aggregate after ingestion (default true)"),
            p("message_id",    "string","Kafka message ID for dedup (Kafka partition offset)")
        ));

        tools.add(t("ingest_gd_snapshot",
            "Consume a gd.scored.snapshot event — internal snapshot of GD score " +
            "from the scoring-engine for the candidate in the current cycle. " +
            "Topic: gd.scored.snapshot (6 partitions, 30-day retention). " +
            "Stores the GD track score for later aggregation. SERVICE_ACCOUNT only.",
            p("auth_token",   "string","Bearer JWT (SERVICE_ACCOUNT)"),
            p("tenant_id",    "string","Tenant identifier"),
            p("candidate_id", "string","Candidate UUID"),
            p("cycle_id",     "string","Assessment cycle UUID"),
            p("event_id",     "string","GD snapshot event ID for deduplication"),
            p("gd_score",     "string","GD raw score 0-100"),
            p("session_id",   "string","GD session UUID (from gd-orchestrator)"),
            p("scored_at",    "string","ISO-8601 scoring timestamp")
        ));

        tools.add(t("ingest_interview_snapshot",
            "Consume an interview.scored.snapshot event — internal snapshot of interview score " +
            "from the scoring-engine for the candidate in the current cycle. " +
            "Topic: interview.scored.snapshot (6 partitions, 30-day retention). " +
            "Stores the INTERVIEW track score for later aggregation. SERVICE_ACCOUNT only.",
            p("auth_token",   "string","Bearer JWT (SERVICE_ACCOUNT)"),
            p("tenant_id",    "string","Tenant identifier"),
            p("candidate_id", "string","Candidate UUID"),
            p("cycle_id",     "string","Assessment cycle UUID"),
            p("event_id",     "string","Interview snapshot event ID for deduplication"),
            p("interview_score","string","Interview raw score 0-100"),
            p("interview_id", "string","Interview session UUID"),
            p("scored_at",    "string","ISO-8601 scoring timestamp")
        ));

        // ── Aggregation ───────────────────────────────────────────────────────
        tools.add(t("compute_aggregate",
            "Execute the full aggregation pipeline for a (candidate_id, cycle_id) pair. " +
            "Steps: (1) Acquire distributed lock (Redis agg_lock TTL 30s), " +
            "(2) Read track scores, (3) Detect partial participation → redistribute weights, " +
            "(4) Weighted aggregation: GD×w1 + Interview×w2 + Dojo×w3, " +
            "(5) Rubric normalization → normalized_score 0-100, " +
            "(6) Belt level mapping: BRONZE/SILVER/GOLD/PLATINUM, " +
            "(7) Persist to PostgreSQL (candidate_performance_aggregates + audit_trail), " +
            "(8) Write to ClickHouse audit log, " +
            "(9) Publish candidate.performance.computed Kafka event. " +
            "Idempotent: returns existing result if already computed (unless force_recompute=true).",
            p("auth_token",      "string","Bearer JWT (SERVICE_ACCOUNT or ADMIN)"),
            p("tenant_id",       "string","Tenant identifier"),
            p("candidate_id",    "string","Candidate UUID"),
            p("cycle_id",        "string","Assessment cycle UUID"),
            p("force_recompute", "string","true = ignore existing result, recompute (ADMIN only)")
        ));

        tools.add(t("retry_aggregation",
            "POST /api/v1/aggregations/retry equivalent. " +
            "Manually enqueue a re-aggregation for a (candidate_id, cycle_id) pair. " +
            "Used by admin-service to recover from failed aggregations or DLQ entries. " +
            "Returns 409-equivalent if aggregation already in progress (lock held). " +
            "Increments retry_count. Publishes result event on success, DLQ event after 3 failures. " +
            "Requires ecoskiller:aggregator:admin role (ADMIN).",
            p("auth_token",    "string","Bearer JWT (ADMIN only)"),
            p("tenant_id",     "string","Tenant identifier"),
            p("candidate_id",  "string","Candidate UUID"),
            p("cycle_id",      "string","Assessment cycle UUID"),
            p("reason",        "string","Reason for retry (audit trail)")
        ));

        tools.add(t("batch_compute_aggregates",
            "Compute aggregates for multiple candidates in a single call. " +
            "Accepts comma-separated candidate_ids, all for the same cycle_id. " +
            "Max 50 candidates per batch. Returns per-candidate results and batch summary. " +
            "Skips candidates already computed unless force_recompute=true. " +
            "SERVICE_ACCOUNT or ADMIN only.",
            p("auth_token",      "string","Bearer JWT (SERVICE_ACCOUNT or ADMIN)"),
            p("tenant_id",       "string","Tenant identifier"),
            p("candidate_ids",   "string","Comma-separated candidate UUIDs (max 50)"),
            p("cycle_id",        "string","Assessment cycle UUID"),
            p("force_recompute", "string","true = recompute already-computed (default false)")
        ));

        tools.add(t("get_aggregation_status",
            "GET /api/v1/aggregations/:candidate_id/:cycle_id equivalent. " +
            "Returns current aggregation status: PENDING | IN_PROGRESS | COMPUTED | FAILED | RETRYING. " +
            "If COMPUTED: includes normalized_score, belt_eligible_level, per_track_breakdown, computed_at. " +
            "If FAILED: includes failure_reason, retry_count, dlq_entry_id. " +
            "CANDIDATE can only view own status; RECRUITER/ADMIN can view any.",
            p("auth_token",   "string","Bearer JWT"),
            p("tenant_id",    "string","Tenant identifier"),
            p("candidate_id", "string","Candidate UUID"),
            p("cycle_id",     "string","Assessment cycle UUID")
        ));

        // ── Rubric & Config ───────────────────────────────────────────────────
        tools.add(t("configure_rubric",
            "Create or update a scoring rubric for a tenant + job_role. " +
            "Weights: gd_weight + interview_weight + dojo_weight must sum to 1.0. " +
            "Thresholds define belt level boundaries (0-100): " +
            "bronze_threshold, silver_threshold, gold_threshold, platinum_threshold. " +
            "Stored in PostgreSQL ecoskiller.scoring_rubrics + cached in Redis (TTL 5 min). " +
            "ADMIN only. Version is auto-incremented on each update.",
            p("auth_token",           "string","Bearer JWT (ADMIN only)"),
            p("tenant_id",            "string","Tenant identifier"),
            p("job_role_id",          "string","Job role identifier (or 'default' for tenant-wide)"),
            p("gd_weight",            "string","GD track weight 0.0-1.0 (e.g. 0.35)"),
            p("interview_weight",     "string","Interview track weight (e.g. 0.40)"),
            p("dojo_weight",          "string","Dojo track weight (e.g. 0.25)"),
            p("bronze_threshold",     "string","Min score for BRONZE (default 0)"),
            p("silver_threshold",     "string","Min score for SILVER (default 50)"),
            p("gold_threshold",       "string","Min score for GOLD (default 70)"),
            p("platinum_threshold",   "string","Min score for PLATINUM (default 85)")
        ));

        tools.add(t("get_rubric",
            "Retrieve the scoring rubric for a tenant + job_role. " +
            "Returns weights, belt thresholds, version, and created_at. " +
            "Falls back to tenant 'default' rubric if job_role-specific rubric not found. " +
            "RECRUITER or ADMIN role required.",
            p("auth_token", "string","Bearer JWT (RECRUITER or ADMIN)"),
            p("tenant_id",  "string","Tenant identifier"),
            p("job_role_id","string","Job role ID (or 'default')")
        ));

        tools.add(t("configure_cycle",
            "Register an assessment cycle configuration: participating tracks + weight overrides. " +
            "Stored in Redis cycle_config:{cycle_id} (TTL 10 min) + PostgreSQL ecoskiller.assessment_cycles. " +
            "Allows per-cycle weight overrides (e.g. engineering roles: higher dojo weight). " +
            "ADMIN role required. Cycle config is read by compute_aggregate to determine " +
            "which tracks are expected and what weights to apply.",
            p("auth_token",      "string","Bearer JWT (ADMIN)"),
            p("tenant_id",       "string","Tenant identifier"),
            p("cycle_id",        "string","Assessment cycle UUID"),
            p("job_role_id",     "string","Job role for rubric lookup"),
            p("tracks",          "string","Comma-separated: GD,INTERVIEW,DOJO (omit absent tracks)"),
            p("gd_weight",       "string","Optional: override GD weight for this cycle"),
            p("interview_weight","string","Optional: override interview weight"),
            p("dojo_weight",     "string","Optional: override dojo weight"),
            p("status",          "string","ACTIVE | COMPLETED | CANCELLED (default ACTIVE)")
        ));

        tools.add(t("get_cycle_config",
            "GET /api/v1/cycles/:cycle_id/config equivalent. " +
            "Returns resolved cycle configuration: tracks, weight overrides, job_role_id, status. " +
            "RECRUITER or ADMIN role required.",
            p("auth_token", "string","Bearer JWT (RECRUITER or ADMIN)"),
            p("tenant_id",  "string","Tenant identifier"),
            p("cycle_id",   "string","Assessment cycle UUID")
        ));

        // ── Query ─────────────────────────────────────────────────────────────
        tools.add(t("get_aggregate_result",
            "Retrieve the final aggregated performance record for a candidate+cycle. " +
            "Returns: normalized_score, belt_eligible_level, per_track_breakdown (explainability), " +
            "weights_used, rubric_version, computed_at, processing_latency_ms. " +
            "CANDIDATE can only view own results; RECRUITER/ADMIN can view any.",
            p("auth_token",   "string","Bearer JWT"),
            p("tenant_id",    "string","Tenant identifier"),
            p("candidate_id", "string","Candidate UUID"),
            p("cycle_id",     "string","Assessment cycle UUID")
        ));

        tools.add(t("get_candidate_breakdown",
            "Retrieve the score explainability breakdown for a candidate (DPDPA 2023 transparency). " +
            "Returns per_track_breakdown with: track, raw_score, weight_applied, " +
            "normalized_contribution, participated. Enables candidates to understand " +
            "exactly how their final score was computed — supports right to explanation. " +
            "CANDIDATE can view own; RECRUITER/ADMIN can view any.",
            p("auth_token",   "string","Bearer JWT"),
            p("tenant_id",    "string","Tenant identifier"),
            p("candidate_id", "string","Candidate UUID"),
            p("cycle_id",     "string","Assessment cycle UUID")
        ));

        tools.add(t("get_aggregation_history",
            "List all aggregation records for a tenant (or candidate). " +
            "Filterable by cycle_id, status, belt_level. Used by analytics-service " +
            "and recruiter dashboards for candidate ranking and scoring distribution views.",
            p("auth_token",   "string","Bearer JWT (RECRUITER or ADMIN)"),
            p("tenant_id",    "string","Tenant identifier"),
            p("candidate_id", "string","Optional: filter to specific candidate"),
            p("cycle_id",     "string","Optional: filter to specific cycle"),
            p("belt_level",   "string","Optional: BRONZE | SILVER | GOLD | PLATINUM"),
            p("limit",        "string","Max records (default 50)")
        ));

        tools.add(t("get_score_history",
            "Retrieve raw track score history for a candidate across cycles. " +
            "Filterable by track type. Shows deduplication status, event_ids. " +
            "CANDIDATE can only view own scores; RECRUITER/ADMIN can view any.",
            p("auth_token",   "string","Bearer JWT"),
            p("tenant_id",    "string","Tenant identifier"),
            p("candidate_id", "string","Candidate UUID"),
            p("track",        "string","Optional: GD | INTERVIEW | DOJO (default ALL)"),
            p("limit",        "string","Max records (default 50)")
        ));

        // ── DLQ ───────────────────────────────────────────────────────────────
        tools.add(t("get_dlq_entries",
            "Retrieve entries from the dead-letter queue (candidate.performance.aggregated.dlq). " +
            "DLQ entries are created after max_retry_count (3) failed aggregation attempts. " +
            "Each entry contains: original_event, failure_reason, retry_count. " +
            "Used by ops team for manual remediation. ADMIN only.",
            p("auth_token",  "string","Bearer JWT (ADMIN only)"),
            p("tenant_id",   "string","Tenant identifier"),
            p("status",      "string","Optional: PENDING | RESOLVED | REQUEUED (default PENDING)")
        ));

        tools.add(t("resolve_dlq_entry",
            "Mark a DLQ entry as RESOLVED or REQUEUED for manual remediation. " +
            "REQUEUED: triggers a retry_aggregation call for the candidate+cycle. " +
            "RESOLVED: marks as manually resolved without retry. " +
            "Updates DLQ entry status. Publishes ops alert event. ADMIN only.",
            p("auth_token",  "string","Bearer JWT (ADMIN only)"),
            p("tenant_id",   "string","Tenant identifier"),
            p("dlq_id",      "string","DLQ entry UUID"),
            p("action",      "string","RESOLVED | REQUEUED"),
            p("notes",       "string","Resolution notes for audit trail")
        ));

        // ── Analytics ─────────────────────────────────────────────────────────
        tools.add(t("get_metrics",
            "GET /metrics equivalent — Prometheus service-level indicators. " +
            "Returns: events_consumed_total, aggregations_succeeded_total, " +
            "aggregations_failed_total, processing_latency_p95, dlq_events_total, " +
            "rubric_cache_hit_rate, kafka_consumer_lag, belt_distribution, aggregates_by_status. " +
            "Mirrors Grafana dashboard metrics. ADMIN or SERVICE_ACCOUNT.",
            p("auth_token","string","Bearer JWT (ADMIN or SERVICE_ACCOUNT)"),
            p("tenant_id", "string","Tenant identifier for scoped metrics")
        ));

        tools.add(t("get_audit_log",
            "Retrieve the immutable aggregation audit log (ClickHouse ecoskiller.perf_agg_audit_log). " +
            "Captures: AGGREGATE_COMPUTED, AGGREGATE_RETRIED, AGGREGATE_FAILED, DLQ_ENQUEUED, " +
            "RUBRIC_UPDATED, CYCLE_CONFIGURED. DPDPA 2023 compliance — 3-year retention. " +
            "Filterable by candidate_id. ADMIN only.",
            p("auth_token",   "string","Bearer JWT (ADMIN only)"),
            p("tenant_id",    "string","Tenant identifier"),
            p("candidate_id", "string","Optional: filter to specific candidate"),
            p("limit",        "string","Max entries (default 100, max 1000)")
        ));

        return tools;
    }

    // ── Schema helpers ────────────────────────────────────────────────────────

    private Map<String,Object> t(String name, String desc, Map<String,Object>... props) {
        Map<String,Object> tool = new LinkedHashMap<>();
        tool.put("name", name); tool.put("description", desc);
        Map<String,Object> schema = new LinkedHashMap<>();
        schema.put("type","object");
        Map<String,Object> properties = new LinkedHashMap<>();
        for (Map<String,Object> prop : props) {
            String pName = (String) prop.remove("_name");
            properties.put(pName, prop);
        }
        schema.put("properties", properties);
        schema.put("required", List.of("auth_token","tenant_id"));
        tool.put("inputSchema", schema);
        return tool;
    }
    private Map<String,Object> p(String name, String type, String desc) {
        Map<String,Object> m=new LinkedHashMap<>(); m.put("_name",name); m.put("type",type); m.put("description",desc); return m;
    }
}
