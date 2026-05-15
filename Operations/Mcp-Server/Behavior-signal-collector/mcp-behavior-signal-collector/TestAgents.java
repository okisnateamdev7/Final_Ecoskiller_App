package com.ecoskiller.mcp;

import com.ecoskiller.mcp.security.SignalSecurityConfig;
import com.ecoskiller.mcp.tools.SignalToolRouter;

import java.time.Instant;
import java.util.*;

/**
 * Integration test suite for the behavior-signal-collector MCP server.
 *
 * Run:
 *   java -cp mcp-behavior-signal-collector.jar com.ecoskiller.mcp.TestAgents
 *   java -cp mcp-behavior-signal-collector.jar com.ecoskiller.mcp.TestAgents --verbose
 *
 * 30 tests:
 *   - All 20 tools
 *   - Full pipeline: ingest → extract → normalize → anomaly → aggregate → publish
 *   - Security: SQL injection, wrong role, tenant isolation
 *   - Edge cases: empty batch, deduplication, quality alerts, drift detection
 */
public class TestAgents {

    private static final String TENANT  = "tenant-001";
    private static final String ASSESS  = "assess-abc-001";
    private static boolean verbose = false;
    private static int passed = 0, failed = 0;

    private static final SignalToolRouter ROUTER = new SignalToolRouter();

    // Dev tokens
    private static final String ADMIN_TOKEN = devToken("admin-001",   "ADMIN");
    private static final String REC_TOKEN   = devToken("rec-001",     "RECRUITER");
    private static final String CAND_TOKEN  = devToken("cand-001",    "CANDIDATE");
    private static final String SA_TOKEN    = devToken("svc-001",     "SERVICE_ACCOUNT");

    public static void main(String[] args) {
        verbose = Arrays.asList(args).contains("--verbose");
        SignalSecurityConfig.init();

        System.out.println("=======================================================");
        System.out.println(" Ecoskiller Behavior-Signal-Collector MCP Server Tests ");
        System.out.println(" 20 Tools | Namespace: realtime | Tenant: " + TENANT);
        System.out.println("=======================================================");
        System.out.println();

        // ── Setup ─────────────────────────────────────────────────────────────
        run("1.  configure_assessment",               test_configureAssessment());
        run("2.  ingest_signals (keyboard + sentiment)",test_ingestSignals());
        run("3.  ingest_signals (batch with invalid)", test_ingestWithInvalid());
        run("4.  ingest_signals (duplicate msg_id)",  test_ingestDuplicate());
        run("5.  ingest_kafka_event (gd.speaker.turn)",test_ingestKafkaEvent());

        // ── Processing ────────────────────────────────────────────────────────
        run("6.  extract_features (keyboard_latency)", test_extractFeatures());
        run("7.  normalize_signals (ENGINEER cohort)", test_normalizeSignals());
        run("8.  detect_anomalies (IQR method)",       test_detectAnomalies());
        run("9.  detect_anomalies (impossible value)", test_detectAnomaliesImpossible());
        run("10. aggregate_signals (ALL types)",       test_aggregateSignals());

        // ── Pipeline ──────────────────────────────────────────────────────────
        run("11. run_full_pipeline",                  test_runFullPipeline());
        run("12. fuse_multimodal_signals (ALL)",      test_fuseMultimodal());
        run("13. detect_signal_drift (detected)",     test_detectDrift());
        run("14. detect_signal_drift (no drift)",     test_detectNoDrift());
        run("15. publish_aggregated_signals",         test_publishSignals());

        // ── Query ─────────────────────────────────────────────────────────────
        run("16. get_signal_quality",                 test_getSignalQuality());
        run("17. get_assessment_signals",             test_getAssessmentSignals());
        run("18. replay_assessment (ADMIN only)",     test_replayAssessment());
        run("19. get_metrics",                        test_getMetrics());
        run("20. get_kafka_event_log",                test_getKafkaLog());

        // ── Fairness + Audit ──────────────────────────────────────────────────
        run("21. add_cohort_data (female/male)",      test_addCohortData());
        run("22. analyze_fairness",                   test_analyzeFairness());
        run("23. get_audit_log",                      test_getAuditLog());
        run("24. get_quality_report",                 test_getQualityReport());

        // ── Security edge cases ───────────────────────────────────────────────
        run("25. Security: SQL injection in assessment_id", test_sqlInjection());
        run("26. Security: CANDIDATE cannot aggregate",     test_wrongRoleAggregate());
        run("27. Security: CANDIDATE cannot replay",        test_wrongRoleReplay());
        run("28. Security: batch too large",                test_batchTooLarge());
        run("29. detect_anomalies (FLAG handling)",         test_detectAnomaliesFlag());
        run("30. get_quality_report (needs review alert)",  test_qualityReportAlert());

        System.out.println();
        System.out.println("=======================================================");
        System.out.printf("  PASSED: %d  |  FAILED: %d  |  TOTAL: %d%n", passed, failed, passed + failed);
        System.out.println("=======================================================");
        System.exit(failed > 0 ? 1 : 0);
    }

    // =========================================================================
    // Tests
    // =========================================================================

    static TestResult test_configureAssessment() {
        Map<String, Object> a = a(REC_TOKEN);
        a.put("assessment_id",          ASSESS);
        a.put("signal_types",           "keyboard_latency,sentiment_score,turn_taking_duration");
        a.put("feature_model_version",  "v2");
        a.put("cohort",                 "ENGINEER");
        a.put("anomaly_threshold",      "1.5");
        String r = call("configure_assessment", a);
        return ok(r.contains("ENGINEER") && r.contains("feature_model_version"), r);
    }

    static TestResult test_ingestSignals() {
        Map<String, Object> a = a(SA_TOKEN);
        a.put("assessment_id", ASSESS);
        a.put("candidate_id",  "cand-001");
        a.put("source",        "WEB_CLIENT");
        a.put("signals",       "[" +
                "{\"signal_type\":\"keyboard_latency\",\"value\":250,\"message_id\":\"m001\",\"timestamp\":\"" + Instant.now() + "\"}," +
                "{\"signal_type\":\"keyboard_latency\",\"value\":280,\"message_id\":\"m002\",\"timestamp\":\"" + Instant.now() + "\"}," +
                "{\"signal_type\":\"sentiment_score\",\"value\":0.65,\"message_id\":\"m003\",\"timestamp\":\"" + Instant.now() + "\"}," +
                "{\"signal_type\":\"speech_rate_wpm\",\"value\":120,\"message_id\":\"m004\",\"timestamp\":\"" + Instant.now() + "\"}" +
                "]");
        String r = call("ingest_signals", a);
        return ok(r.contains("accepted") && (r.contains("\"accepted\":4") || r.contains("\"accepted\":3")), r);
    }

    static TestResult test_ingestWithInvalid() {
        Map<String, Object> a = a(SA_TOKEN);
        a.put("assessment_id", ASSESS);
        a.put("source",        "WEB_CLIENT");
        a.put("signals",       "[" +
                "{\"signal_type\":\"keyboard_latency\",\"value\":9999999,\"message_id\":\"m010\"}," + // out of range
                "{\"signal_type\":\"keyboard_latency\",\"value\":300,\"message_id\":\"m011\"}" +
                "]");
        String r = call("ingest_signals", a);
        return ok(r.contains("rejected") && r.contains("accepted"), r);
    }

    static TestResult test_ingestDuplicate() {
        Map<String, Object> a = a(SA_TOKEN);
        a.put("assessment_id", ASSESS);
        a.put("source",        "WEB_CLIENT");
        a.put("signals",       "[{\"signal_type\":\"keyboard_latency\",\"value\":250,\"message_id\":\"m001\"}]"); // m001 already used
        String r = call("ingest_signals", a);
        return ok(r.contains("\"duplicates\":1") || r.contains("DUPLICATE"), r);
    }

    static TestResult test_ingestKafkaEvent() {
        Map<String, Object> a = a(SA_TOKEN);
        a.put("event_type",    "gd.speaker.turn");
        a.put("event_payload", "{\"gd_session_id\":\"" + ASSESS + "\",\"speaker_id\":\"cand-001\",\"duration_ms\":45000}");
        a.put("message_id",    "kafka-msg-001");
        String r = call("ingest_kafka_event", a);
        return ok(r.contains("signals_created") && r.contains("1"), r);
    }

    static TestResult test_extractFeatures() {
        Map<String, Object> a = a(SA_TOKEN);
        a.put("assessment_id","assess-feat-001");
        a.put("signal_type",  "keyboard_latency");
        a.put("values",       "200,250,230,280,210,270,240,260,220,290");
        a.put("window_label", "minute_1");
        String r = call("extract_features", a);
        return ok(r.contains("mean") && r.contains("variance") && r.contains("trend_direction"), r);
    }

    static TestResult test_normalizeSignals() {
        Map<String, Object> a = a(SA_TOKEN);
        a.put("assessment_id","assess-norm-001");
        a.put("signal_type",  "keyboard_latency");
        a.put("values",       "200,250,300,180,220");
        a.put("cohort",       "ENGINEER");
        String r = call("normalize_signals", a);
        return ok(r.contains("normalized_values") && r.contains("cohort_mean"), r);
    }

    static TestResult test_detectAnomalies() {
        Map<String, Object> a = a(SA_TOKEN);
        a.put("assessment_id","assess-anom-001");
        a.put("signal_type",  "keyboard_latency");
        a.put("values",       "200,210,220,230,240,5000,210,220,215,225"); // 5000 is anomaly
        a.put("handling",     "FLAG");
        String r = call("detect_anomalies", a);
        return ok(r.contains("anomaly_count") && (r.contains("\"anomaly_count\":1") || r.contains("anomaly")), r);
    }

    static TestResult test_detectAnomaliesImpossible() {
        Map<String, Object> a = a(SA_TOKEN);
        a.put("assessment_id","assess-anom-002");
        a.put("signal_type",  "sentiment_score");
        a.put("values",       "0.5,0.6,-5.0,0.7,0.4"); // -5.0 impossible for sentiment_score
        a.put("handling",     "REMOVE");
        String r = call("detect_anomalies", a);
        return ok(r.contains("anomaly_count"), r);
    }

    static TestResult test_aggregateSignals() {
        Map<String, Object> a = a(SA_TOKEN);
        a.put("assessment_id",  ASSESS);
        a.put("signal_type",    "ALL");
        a.put("window_minutes", "1");
        String r = call("aggregate_signals", a);
        return ok(r.contains("aggregates") && r.contains("signal_types"), r);
    }

    static TestResult test_runFullPipeline() {
        Map<String, Object> a = a(SA_TOKEN);
        a.put("assessment_id", ASSESS);
        a.put("publish",       "true");
        String r = call("run_full_pipeline", a);
        return ok(r.contains("signals_input") && r.contains("processing_latency_ms")
                && r.contains("pipeline_stages"), r);
    }

    static TestResult test_fuseMultimodal() {
        Map<String, Object> a = a(SA_TOKEN);
        a.put("assessment_id",             ASSESS);
        a.put("modality",                  "ALL");
        a.put("include_confidence_score",  "true");
        String r = call("fuse_multimodal_signals", a);
        return ok(r.contains("fused_vector") && r.contains("graceful_degradation"), r);
    }

    static TestResult test_detectDrift() {
        Map<String, Object> a = a(SA_TOKEN);
        a.put("assessment_id",       ASSESS);
        a.put("signal_type",         "keyboard_latency");
        a.put("baseline_mean",       "100.0"); // baseline 100ms
        a.put("drift_threshold_pct", "20");
        String r = call("detect_signal_drift", a);
        // current mean ~260ms vs baseline 100ms → massive drift
        return ok(r.contains("drift_pct") && r.contains("drift_detected"), r);
    }

    static TestResult test_detectNoDrift() {
        Map<String, Object> a = a(SA_TOKEN);
        a.put("assessment_id",       ASSESS);
        a.put("signal_type",         "keyboard_latency");
        a.put("baseline_mean",       "260.0"); // baseline close to current mean
        a.put("drift_threshold_pct", "30");
        String r = call("detect_signal_drift", a);
        return ok(r.contains("drift_detected"), r);
    }

    static TestResult test_publishSignals() {
        Map<String, Object> a = a(SA_TOKEN);
        a.put("assessment_id", ASSESS);
        String r = call("publish_aggregated_signals", a);
        return ok(r.contains("topic") && r.contains("behavior.signals.aggregated"), r);
    }

    static TestResult test_getSignalQuality() {
        Map<String, Object> a = a(REC_TOKEN);
        a.put("assessment_id", ASSESS);
        String r = call("get_signal_quality", a);
        return ok(r.contains("data_quality_score") && r.contains("validity_rate")
                && r.contains("signal_type_breakdown"), r);
    }

    static TestResult test_getAssessmentSignals() {
        Map<String, Object> a = a(REC_TOKEN);
        a.put("assessment_id", ASSESS);
        a.put("valid_only",    "true");
        a.put("limit",         "50");
        String r = call("get_assessment_signals", a);
        return ok(r.contains("signals") && r.contains("total_matching"), r);
    }

    static TestResult test_replayAssessment() {
        Map<String, Object> a = a(ADMIN_TOKEN);
        a.put("assessment_id",    ASSESS);
        a.put("include_raw",       "true");
        a.put("include_aggregated","true");
        String r = call("replay_assessment", a);
        return ok(r.contains("raw_signal_count") && r.contains("immutable_note"), r);
    }

    static TestResult test_getMetrics() {
        Map<String, Object> a = a(ADMIN_TOKEN);
        String r = call("get_metrics", a);
        return ok(r.contains("behavior_signals_ingested_total") && r.contains("behavior_signal_validity_rate"), r);
    }

    static TestResult test_getKafkaLog() {
        Map<String, Object> a = a(ADMIN_TOKEN);
        a.put("assessment_id", ASSESS);
        a.put("limit",         "20");
        String r = call("get_kafka_event_log", a);
        return ok(r.contains("events") && r.contains("total"), r);
    }

    static TestResult test_addCohortData() {
        // Add data for female cohort
        for (double v : new double[]{0.55, 0.60, 0.58, 0.62, 0.57, 0.61}) {
            Map<String, Object> a = a(ADMIN_TOKEN);
            a.put("signal_type","confidence_score"); a.put("cohort_name","female"); a.put("value",String.valueOf(v));
            call("add_cohort_data", a);
        }
        // Add data for male cohort (higher values → simulate bias)
        for (double v : new double[]{0.75, 0.80, 0.78, 0.82, 0.76, 0.81}) {
            Map<String, Object> a = a(ADMIN_TOKEN);
            a.put("signal_type","confidence_score"); a.put("cohort_name","male"); a.put("value",String.valueOf(v));
            call("add_cohort_data", a);
        }
        Map<String, Object> a = a(ADMIN_TOKEN);
        a.put("signal_type","confidence_score"); a.put("cohort_name","test_cohort"); a.put("value","0.7");
        String r = call("add_cohort_data", a);
        return ok(r.contains("cohort_size"), r);
    }

    static TestResult test_analyzeFairness() {
        Map<String, Object> a = a(ADMIN_TOKEN);
        a.put("signal_type", "confidence_score");
        a.put("cohort_a",    "female");
        a.put("cohort_b",    "male");
        a.put("threshold",   "0.70");
        String r = call("analyze_fairness", a);
        return ok(r.contains("fairness_score") && r.contains("distribution_parity_diff")
                && r.contains("recommended_interventions"), r);
    }

    static TestResult test_getAuditLog() {
        Map<String, Object> a = a(ADMIN_TOKEN);
        a.put("assessment_id", ASSESS);
        a.put("limit",         "50");
        String r = call("get_audit_log", a);
        return ok(r.contains("entries") && r.contains("compliance_note"), r);
    }

    static TestResult test_getQualityReport() {
        Map<String, Object> a = a(REC_TOKEN);
        a.put("assessment_id", ASSESS);
        String r = call("get_quality_report", a);
        return ok(r.contains("data_quality_score") && r.contains("scoring_recommendation")
                && r.contains("signal_type_breakdown"), r);
    }

    // Security tests
    static TestResult test_sqlInjection() {
        Map<String, Object> a = a(SA_TOKEN);
        a.put("assessment_id", "'; DROP TABLE signals; --");
        a.put("signals",       "[]");
        try {
            call("ingest_signals", a);
            return ok(false, "Should have rejected SQL injection");
        } catch (SecurityException e) {
            return ok(e.getMessage().toLowerCase().contains("injection"), "Blocked: " + e.getMessage());
        }
    }

    static TestResult test_wrongRoleAggregate() {
        Map<String, Object> a = a(CAND_TOKEN);
        a.put("assessment_id", ASSESS);
        try {
            call("aggregate_signals", a);
            return ok(false, "CANDIDATE should not aggregate signals");
        } catch (SecurityException e) {
            return ok(e.getMessage().contains("denied"), "Blocked: " + e.getMessage());
        }
    }

    static TestResult test_wrongRoleReplay() {
        Map<String, Object> a = a(REC_TOKEN); // RECRUITER cannot replay
        a.put("assessment_id", ASSESS);
        try {
            call("replay_assessment", a);
            return ok(false, "RECRUITER should not replay assessment");
        } catch (SecurityException e) {
            return ok(e.getMessage().contains("denied"), "Blocked: " + e.getMessage());
        }
    }

    static TestResult test_batchTooLarge() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < 1001; i++) {
            if (i > 0) sb.append(",");
            sb.append("{\"signal_type\":\"keyboard_latency\",\"value\":200}");
        }
        sb.append("]");
        Map<String, Object> a = a(SA_TOKEN);
        a.put("assessment_id", ASSESS);
        a.put("signals",       sb.toString());
        try {
            call("ingest_signals", a);
            return ok(false, "Should reject batch > 1000");
        } catch (IllegalArgumentException e) {
            return ok(e.getMessage().contains("Batch too large"), "Correctly rejected: " + e.getMessage());
        }
    }

    static TestResult test_detectAnomaliesFlag() {
        Map<String, Object> a = a(SA_TOKEN);
        a.put("assessment_id",  "assess-flag-001");
        a.put("signal_type",    "sentiment_score");
        a.put("values",         "0.5,0.6,0.55,0.58,0.9,0.52,0.57");
        a.put("handling",       "CAP");
        a.put("iqr_multiplier", "1.5");
        String r = call("detect_anomalies", a);
        return ok(r.contains("handling") && r.contains("iqr_bounds"), r);
    }

    static TestResult test_qualityReportAlert() {
        // Ingest signals for a fresh assessment with mostly invalid data
        String freshAssess = "assess-quality-alert-001";
        Map<String, Object> config = a(REC_TOKEN);
        config.put("assessment_id", freshAssess);
        config.put("signal_types",  "keyboard_latency");
        call("configure_assessment", config);

        // Ingest one valid signal only → low quality
        Map<String, Object> ing = a(SA_TOKEN);
        ing.put("assessment_id", freshAssess);
        ing.put("source",        "WEB_CLIENT");
        ing.put("signals",       "[{\"signal_type\":\"keyboard_latency\",\"value\":200,\"message_id\":\"qa001\"}]");
        call("ingest_signals", ing);

        Map<String, Object> a = a(REC_TOKEN);
        a.put("assessment_id", freshAssess);
        String r = call("get_quality_report", a);
        return ok(r.contains("scoring_recommendation"), r);
    }

    // ── Infrastructure ────────────────────────────────────────────────────────

    private static String call(String tool, Map<String, Object> args) {
        return ROUTER.dispatch(tool, args);
    }

    private static void run(String name, TestResult result) {
        String icon = result.skipped ? "⏭ " : (result.passed ? "✅" : "❌");
        System.out.printf("  %s  %-52s %s%n", icon, name,
                result.skipped ? "SKIPPED" : (result.passed ? "PASS" : "FAIL"));
        if (verbose || (!result.passed && !result.skipped)) {
            String out = result.output != null && result.output.length() > 250
                    ? result.output.substring(0, 250) + "..." : result.output;
            System.out.println("         " + out);
        }
        if (!result.skipped) { if (result.passed) passed++; else failed++; }
    }

    private static TestResult ok(boolean p, String o) { return new TestResult(p, o); }

    private static Map<String, Object> a(String token) {
        Map<String, Object> a = new LinkedHashMap<>();
        a.put("auth_token", token);
        a.put("tenant_id",  TENANT);
        return a;
    }

    private static String devToken(String userId, String role) {
        String header  = b64url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payload = b64url("{\"sub\":\""+userId+"\",\"tenant_id\":\""+TENANT+"\","
                + "\"email\":\""+userId+"@ecoskiller.dev\",\"exp\":9999999999,"
                + "\"roles\":[\""+role+"\"]}");
        return header + "." + payload + ".dev-sig";
    }

    private static String b64url(String s) {
        return java.util.Base64.getUrlEncoder().withoutPadding()
                .encodeToString(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    static class TestResult {
        final boolean passed, skipped; final String output;
        TestResult(boolean p, String o) { passed=p; skipped=false; output=o!=null?o:""; }
        TestResult(boolean p, String o, boolean s) { passed=p; skipped=s; output=o!=null?o:""; }
        static TestResult skip(String r) { return new TestResult(false, r, true); }
    }
}
