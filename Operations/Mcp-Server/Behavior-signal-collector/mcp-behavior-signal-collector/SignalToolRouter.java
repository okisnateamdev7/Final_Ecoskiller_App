package com.ecoskiller.mcp.tools;

import java.util.*;

/**
 * Central dispatcher for all 20 behavior-signal-collector MCP tools.
 *
 * Groups:
 *   Ingestion    (3): ingest_signals, ingest_kafka_event, configure_assessment
 *   Processing   (4): extract_features, normalize_signals, detect_anomalies, aggregate_signals
 *   Pipeline     (3): run_full_pipeline, fuse_multimodal_signals, detect_signal_drift
 *   Query        (4): get_signal_quality, get_assessment_signals, replay_assessment, get_metrics
 *   Kafka        (2): publish_aggregated_signals, get_kafka_event_log
 *   Fairness     (2): analyze_fairness, add_cohort_data
 *   Audit        (2): get_audit_log, get_quality_report
 */
public class SignalToolRouter {

    private final IngestionTools    ingestionTools;
    private final ProcessingTools   processingTools;
    private final PipelineTools     pipelineTools;
    private final QueryTools        queryTools;
    private final FairnessAuditTools fairnessAuditTools;    public SignalToolRouter() {
        this.ingestionTools    = new IngestionTools();
        this.processingTools   = new ProcessingTools();
        this.pipelineTools     = new PipelineTools();
        this.queryTools        = new QueryTools();
        this.fairnessAuditTools= new FairnessAuditTools();
    }

    public String dispatch(String toolName, Map<String, Object> args) {
        switch (toolName) {
            // Ingestion
            case "ingest_signals":           return ingestionTools.ingestSignals(args);
            case "ingest_kafka_event":       return ingestionTools.ingestKafkaEvent(args);
            case "configure_assessment":     return ingestionTools.configureAssessment(args);
            // Processing
            case "extract_features":         return processingTools.extractFeatures(args);
            case "normalize_signals":        return processingTools.normalizeSignals(args);
            case "detect_anomalies":         return processingTools.detectAnomalies(args);
            case "aggregate_signals":        return processingTools.aggregateSignals(args);
            // Pipeline
            case "run_full_pipeline":        return pipelineTools.runFullPipeline(args);
            case "fuse_multimodal_signals":  return pipelineTools.fuseMultimodalSignals(args);
            case "detect_signal_drift":      return pipelineTools.detectSignalDrift(args);
            // Query
            case "get_signal_quality":       return queryTools.getSignalQuality(args);
            case "get_assessment_signals":   return queryTools.getAssessmentSignals(args);
            case "replay_assessment":        return queryTools.replayAssessment(args);
            case "get_metrics":              return queryTools.getMetrics(args);
            // Kafka
            case "publish_aggregated_signals":return pipelineTools.publishAggregatedSignals(args);
            case "get_kafka_event_log":      return queryTools.getKafkaEventLog(args);
            // Fairness + Audit
            case "analyze_fairness":         return fairnessAuditTools.analyzeFairness(args);
            case "add_cohort_data":          return fairnessAuditTools.addCohortData(args);
            case "get_audit_log":            return fairnessAuditTools.getAuditLog(args);
            case "get_quality_report":       return fairnessAuditTools.getQualityReport(args);

            default: throw new IllegalArgumentException("Unknown tool: " + toolName);
        }
    }

    // ── tools/list definition ─────────────────────────────────────────────────

    public List<Map<String, Object>> getToolsDefinition() {
        List<Map<String, Object>> tools = new ArrayList<>();

        // ── Ingestion ─────────────────────────────────────────────────────────
        tools.add(t("ingest_signals",
            "Batch ingest raw behavioral signals (POST /signal/ingest). " +
            "Accepts up to 1000 events per request from web clients, mobile apps, " +
            "interview recorders, GD orchestrators. " +
            "Validates: required fields, value ranges per signal type, timestamps. " +
            "Deduplicates by message_id. Returns per-signal validation results. " +
            "Signal types: keyboard_latency (0-5000ms), mouse_movement_velocity, " +
            "gaze_fixation_duration, speech_pause_duration, speech_rate_wpm, " +
            "sentiment_score (-1 to 1), code_commit_frequency, solution_revision_count, " +
            "turn_taking_duration, confidence_score (0-1), gesture_velocity.",
            p("auth_token",    "string", "Bearer JWT (SERVICE_ACCOUNT, CANDIDATE, or RECRUITER)"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("assessment_id", "string", "Assessment session UUID"),
            p("candidate_id",  "string", "Candidate user ID"),
            p("signals",       "string", "JSON array: [{signal_type, value, timestamp, message_id, source, metadata}]"),
            p("source",        "string", "WEB_CLIENT|MOBILE_APP|INTERVIEW_RECORDER|GD_ORCHESTRATOR|EYE_TRACKER|AUDIO_ANALYZER")
        ));

        tools.add(t("ingest_kafka_event",
            "Consume a backend Kafka event and ingest its behavioral signals. " +
            "Event types: assessment.submission (code_length, revision_count), " +
            "gd.speaker.turn (duration_ms, position), interview.recorder_event (sentiment, speech_rate). " +
            "Transforms event payload into behavioral signal records and runs validation.",
            p("auth_token",    "string", "Bearer JWT (SERVICE_ACCOUNT)"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("event_type",    "string", "assessment.submission|gd.speaker.turn|interview.recorder_event"),
            p("event_payload", "string", "JSON string of the Kafka event payload"),
            p("message_id",    "string", "Kafka message ID for deduplication")
        ));

        tools.add(t("configure_assessment",
            "Register signal collection configuration for an assessment. " +
            "Specifies which signal types to collect, feature extraction model version, " +
            "anomaly detection thresholds, and cohort normalization parameters. " +
            "Cached in Redis (TTL 1 hour). Required before signal ingestion.",
            p("auth_token",            "string", "Bearer JWT (RECRUITER or ADMIN)"),
            p("tenant_id",             "string", "Tenant identifier"),
            p("assessment_id",         "string", "Assessment UUID to configure"),
            p("signal_types",          "string", "Comma-separated: keyboard_latency,mouse_movement_velocity,..."),
            p("feature_model_version", "string", "Feature extraction model version: v1|v2|v3"),
            p("cohort",                "string", "Normalization cohort: job role (e.g. ENGINEER, DESIGNER, MANAGER)"),
            p("anomaly_threshold",     "string", "IQR multiplier override for this assessment (default 1.5)"),
            p("quality_threshold",     "string", "Min quality score 0-1 (default 0.85)")
        ));

        // ── Processing ────────────────────────────────────────────────────────
        tools.add(t("extract_features",
            "Extract ML-ready statistical features from raw signal values for a given signal type. " +
            "Computes: mean, variance, std_dev, min, max, p50, p95, skewness, " +
            "linear trend (slope), trend_direction (IMPROVING/DECLINING/STABLE), " +
            "and z-score normalized_mean. " +
            "Input: list of raw values (comma-separated). " +
            "Output: full feature vector for the scoring model.",
            p("auth_token",   "string", "Bearer JWT"),
            p("tenant_id",    "string", "Tenant identifier"),
            p("assessment_id","string", "Assessment UUID (for audit)"),
            p("signal_type",  "string", "Signal type being extracted"),
            p("values",       "string", "Comma-separated raw signal values (e.g. '450,500,480,520')"),
            p("window_label", "string", "Optional: time window label for logging (e.g. 'minute_3')")
        ));

        tools.add(t("normalize_signals",
            "Normalize signal features to 0-1 range using z-score normalization. " +
            "Normalization parameters are cohort-specific (per job role) to prevent bias. " +
            "Example: typing speed for engineers normalized differently than designers. " +
            "Handles missing features via mean imputation (flags as incomplete). " +
            "Returns normalized_value and normalization metadata.",
            p("auth_token",    "string", "Bearer JWT"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("assessment_id", "string", "Assessment UUID"),
            p("signal_type",   "string", "Signal type to normalize"),
            p("values",        "string", "Comma-separated raw values"),
            p("cohort",        "string", "Normalization cohort: ENGINEER|DESIGNER|MANAGER|SALES (default ENGINEER)"),
            p("impute_missing","string", "true = impute missing with cohort mean; false = reject (default true)")
        ));

        tools.add(t("detect_anomalies",
            "Detect outliers in a signal list using IQR method. " +
            "IQR method: flags values outside Q1 - 1.5*IQR or Q3 + 1.5*IQR. " +
            "Also checks range validation (keyboard_latency=999999ms → IMPOSSIBLE). " +
            "Handling options: REMOVE (drop from aggregation), CAP (cap at p99), " +
            "FLAG (mark unreliable but keep). " +
            "Computes anomaly severity and quality_score for the batch.",
            p("auth_token",    "string", "Bearer JWT"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("assessment_id", "string", "Assessment UUID"),
            p("signal_type",   "string", "Signal type being analysed"),
            p("values",        "string", "Comma-separated raw values"),
            p("handling",      "string", "REMOVE|CAP|FLAG (default FLAG)"),
            p("iqr_multiplier","string", "IQR multiplier (default 1.5; higher = fewer detections)")
        ));

        tools.add(t("aggregate_signals",
            "Aggregate signals in a 1-minute tumbling window. " +
            "Per window: computes count, mean, variance, min, max, quality_score, anomaly_count. " +
            "Groups by assessment_id + signal_type + time window. " +
            "Outputs AggregatedSignal record ready for Kafka publishing and ClickHouse storage.",
            p("auth_token",    "string", "Bearer JWT (SERVICE_ACCOUNT or ADMIN)"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("assessment_id", "string", "Assessment UUID to aggregate"),
            p("signal_type",   "string", "Signal type to aggregate (or 'ALL' for all types)"),
            p("window_minutes","string", "Aggregation window in minutes (default 1)")
        ));

        // ── Pipeline ──────────────────────────────────────────────────────────
        tools.add(t("run_full_pipeline",
            "Execute the complete behavioral signal processing pipeline for stored signals: " +
            "Validate → Deduplicate → Feature Extract → Normalize → Anomaly Detect → " +
            "Aggregate → Publish to Kafka → Store to ClickHouse. " +
            "Returns pipeline summary: signals_processed, anomalies_detected, " +
            "features_extracted, kafka_events_published, quality_score.",
            p("auth_token",    "string", "Bearer JWT (SERVICE_ACCOUNT or ADMIN)"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("assessment_id", "string", "Assessment UUID to process"),
            p("signal_types",  "string", "Optional: filter pipeline to specific signal types"),
            p("publish",       "string", "true = publish to Kafka (default true)")
        ));

        tools.add(t("fuse_multimodal_signals",
            "Fuse signals across assessment modalities into a unified holistic signal vector. " +
            "Coding: keyboard_latency + code_commit_frequency + solution_revision_count. " +
            "GD: turn_taking_duration + speech_rate_wpm + sentiment_score + filler_word_ratio. " +
            "Interview: speech_pause_duration + sentiment_score + confidence_score. " +
            "Produces fused_vector with per-dimension normalized scores for the scoring model.",
            p("auth_token",    "string", "Bearer JWT"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("assessment_id", "string", "Assessment UUID"),
            p("modality",      "string", "CODING|GD|INTERVIEW|ALL (default ALL)"),
            p("include_confidence_score","string","true = include overall confidence_score (default true)")
        ));

        tools.add(t("detect_signal_drift",
            "Detect if signal distributions have drifted from the historical baseline. " +
            "Compares current window mean to baseline mean. " +
            "Flags if drift > 30% (configurable). " +
            "Causes: keyboard malfunction, network lag, candidate stress, sensor failure. " +
            "Returns drift_detected, drift_pct, direction, and recommended action.",
            p("auth_token",       "string", "Bearer JWT (SERVICE_ACCOUNT or ADMIN)"),
            p("tenant_id",        "string", "Tenant identifier"),
            p("assessment_id",    "string", "Assessment UUID"),
            p("signal_type",      "string", "Signal type to check for drift"),
            p("baseline_mean",    "string", "Historical baseline mean for this signal type"),
            p("drift_threshold_pct","string","Drift threshold percentage (default 30)")
        ));

        tools.add(t("publish_aggregated_signals",
            "Publish enriched aggregated signals to Kafka topic behavior.signals.aggregated. " +
            "Payload per event: {assessment_id, timestamp, signal_type, value, features_json, " +
            "quality_score, processing_latency_ms}. " +
            "Consumed by scoring-engine for real-time ML scoring. " +
            "Implements idempotency: skips already-published aggregates.",
            p("auth_token",    "string", "Bearer JWT (SERVICE_ACCOUNT or ADMIN)"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("assessment_id", "string", "Assessment UUID to publish signals for"),
            p("signal_type",   "string", "Optional: filter to specific signal type (default all)")
        ));

        // ── Query ─────────────────────────────────────────────────────────────
        tools.add(t("get_signal_quality",
            "GET /assessments/{id}/signal-quality equivalent. " +
            "Returns: total_signals_collected, validity_rate, anomalies_detected, " +
            "feature_completeness, data_quality_score, signal_type_breakdown. " +
            "Used by assessment reviewer before finalizing scores. " +
            "Alerts if quality_score < threshold (default 0.85).",
            p("auth_token",    "string", "Bearer JWT (RECRUITER or ADMIN)"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("assessment_id", "string", "Assessment UUID")
        ));

        tools.add(t("get_assessment_signals",
            "Retrieve stored signals for an assessment with filtering. " +
            "Filterable by signal_type, time range, valid_only flag. " +
            "Paginated (default 100 per page). " +
            "CANDIDATE can only see own assessment signals; RECRUITER and ADMIN see all.",
            p("auth_token",    "string", "Bearer JWT"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("assessment_id", "string", "Assessment UUID"),
            p("signal_type",   "string", "Optional: filter to specific signal type"),
            p("valid_only",    "string", "true = only valid signals; false = all (default false)"),
            p("limit",         "string", "Max signals to return (default 100, max 1000)")
        ));

        tools.add(t("replay_assessment",
            "Replay all stored signals for a fairness/bias audit. " +
            "GET /assessments/{id}/signals/export equivalent. " +
            "Returns all raw + aggregated signals (JSON format). " +
            "Used for: candidate dispute resolution, fairness audits, bias analysis, " +
            "model recomputation with different parameters. " +
            "ADMIN only. Immutable replay — signals never modified.",
            p("auth_token",    "string", "Bearer JWT (ADMIN only)"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("assessment_id", "string", "Assessment UUID to replay"),
            p("include_raw",   "string", "true = include raw signals (default true)"),
            p("include_aggregated","string","true = include aggregated (default true)")
        ));

        tools.add(t("get_metrics",
            "GET /metrics equivalent. Returns Prometheus metrics: " +
            "behavior_signals_ingested_total, behavior_signal_validity_rate, " +
            "behavior_anomaly_detection_rate, behavior_signal_completeness_percent, " +
            "behavior_kafka_events_published. " +
            "Also includes processing_latency_p95 (stub) and active_assessments count.",
            p("auth_token", "string", "Bearer JWT (ADMIN or SERVICE_ACCOUNT)"),
            p("tenant_id",  "string", "Tenant identifier for scoped metrics")
        ));

        tools.add(t("get_kafka_event_log",
            "Retrieve the Kafka event log for debugging and audit. " +
            "Filterable by assessment_id. Returns last N events from the in-memory log. " +
            "Production: queries Kafka consumer offset or ClickHouse event archive.",
            p("auth_token",    "string", "Bearer JWT (ADMIN or SERVICE_ACCOUNT)"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("assessment_id", "string", "Optional: filter by assessment_id"),
            p("limit",         "string", "Max events to return (default 50)")
        ));

        // ── Fairness + Audit ──────────────────────────────────────────────────
        tools.add(t("analyze_fairness",
            "Fairness analysis: compares signal distributions across cohorts (gender, region, etc.). " +
            "Detects differential impact: do signals impact hiring decisions differently by cohort? " +
            "Computes: distribution parity, differential impact ratio, intercept bias (threshold drift). " +
            "Outputs fairness_score, biased_signals list, and recommended interventions " +
            "(NORMALIZE, EXCLUDE, THRESHOLD_TUNE). DPDP Act 2023 / GDPR compliance support.",
            p("auth_token",   "string", "Bearer JWT (ADMIN only)"),
            p("tenant_id",    "string", "Tenant identifier"),
            p("signal_type",  "string", "Signal type to analyze for fairness"),
            p("cohort_a",     "string", "Name of cohort A (e.g. 'female')"),
            p("cohort_b",     "string", "Name of cohort B (e.g. 'male')"),
            p("threshold",    "string", "Hiring threshold for differential impact analysis (default 0.7)")
        ));

        tools.add(t("add_cohort_data",
            "Add a signal data point to a fairness cohort for comparative analysis. " +
            "Builds cohort baselines used by analyze_fairness. " +
            "ADMIN or SERVICE_ACCOUNT only. Tenant-scoped.",
            p("auth_token",  "string", "Bearer JWT (ADMIN or SERVICE_ACCOUNT)"),
            p("tenant_id",   "string", "Tenant identifier"),
            p("signal_type", "string", "Signal type"),
            p("cohort_name", "string", "Cohort identifier (e.g. 'female', 'male', 'engineer')"),
            p("value",       "string", "Signal value to add to cohort baseline")
        ));

        tools.add(t("get_audit_log",
            "Retrieve the immutable signal processing audit log (PostgreSQL). " +
            "Captures: ingestion, validation pass/reject, feature extraction, " +
            "anomaly detection, storage, Kafka publishing. " +
            "DPDP Act 2023 compliant (7-year retention). Used for compliance checks " +
            "and candidate dispute resolution. ADMIN only.",
            p("auth_token",    "string", "Bearer JWT (ADMIN)"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("assessment_id", "string", "Optional: filter to specific assessment"),
            p("limit",         "string", "Max entries to return (default 100)")
        ));

        tools.add(t("get_quality_report",
            "Generate a comprehensive signal quality report for an assessment. " +
            "Includes: total signals, validity rate per signal type, anomaly breakdown, " +
            "feature completeness, quality score, drift alerts, and recommendations. " +
            "Used by reviewers before finalizing AI-generated scores. " +
            "Alerts if quality < threshold (default 0.85).",
            p("auth_token",    "string", "Bearer JWT (RECRUITER or ADMIN)"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("assessment_id", "string", "Assessment UUID")
        ));

        return tools;
    }

    // ── Schema helpers ────────────────────────────────────────────────────────

    private Map<String, Object> t(String name, String description, Map<String, Object>... props) {
        Map<String, Object> tool = new LinkedHashMap<>();
        tool.put("name", name); tool.put("description", description);
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        Map<String, Object> properties = new LinkedHashMap<>();
        List<String> required = new ArrayList<>(List.of("auth_token","tenant_id"));
        for (Map<String, Object> prop : props) {
            String pName = (String) prop.remove("_name");
            properties.put(pName, prop);
        }
        schema.put("properties", properties); schema.put("required", required);
        tool.put("inputSchema", schema);
        return tool;
    }

    private Map<String, Object> p(String name, String type, String description) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("_name", name); m.put("type", type); m.put("description", description);
        return m;
    }
}
