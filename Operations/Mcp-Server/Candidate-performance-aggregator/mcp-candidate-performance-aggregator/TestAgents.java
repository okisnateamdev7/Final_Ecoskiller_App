package com.ecoskiller.mcp;

import com.ecoskiller.mcp.security.AggSecurityConfig;
import com.ecoskiller.mcp.tools.AggToolRouter;

import java.time.Instant;
import java.util.*;

/**
 * Integration test suite — 32 tests covering all 20 tools.
 * Run:
 *   java -cp mcp-candidate-performance-aggregator.jar com.ecoskiller.mcp.TestAgents
 *   java -cp mcp-candidate-performance-aggregator.jar com.ecoskiller.mcp.TestAgents --verbose
 */
public class TestAgents {

    private static final String TENANT  = "tenant-001";
    private static final String CAND1   = "cand-alice-001";
    private static final String CAND2   = "cand-bob-002";
    private static final String CYCLE1  = "cycle-spring-2026";
    private static boolean verbose = false;
    private static int passed = 0, failed = 0;

    private static final AggToolRouter ROUTER = new AggToolRouter();

    private static final String ADMIN_TOKEN = devToken("admin-001", "ADMIN");
    private static final String REC_TOKEN   = devToken("rec-001",   "RECRUITER");
    private static final String CAND_TOKEN  = devToken(CAND1,       "CANDIDATE");
    private static final String SA_TOKEN    = devToken("svc-001",   "SERVICE_ACCOUNT");

    private static String dlqId = null;

    public static void main(String[] args) {
        verbose = Arrays.asList(args).contains("--verbose");
        AggSecurityConfig.init();

        System.out.println("=============================================================");
        System.out.println(" Ecoskiller CPA MCP Server Tests                             ");
        System.out.println(" 20 Tools | Namespace: ecoskiller/prod | Tenant: " + TENANT);
        System.out.println(" Pipeline: GD+Interview+Dojo → Weighted Agg → Belt Level      ");
        System.out.println("=============================================================");
        System.out.println();

        // ── Setup ─────────────────────────────────────────────────────────────
        run("1.  configure_rubric (ENGINEER role)",      test_configureRubric());
        run("2.  get_rubric",                            test_getRubric());
        run("3.  configure_cycle (GD+INTERVIEW+DOJO)",  test_configureCycle());
        run("4.  get_cycle_config",                      test_getCycleConfig());

        // ── Score ingestion ───────────────────────────────────────────────────
        run("5.  ingest_gd_snapshot (cand1)",            test_ingestGdSnapshot());
        run("6.  ingest_interview_snapshot (cand1)",     test_ingestInterviewSnapshot());
        run("7.  ingest_track_score (DOJO - cand1)",     test_ingestDojoScore());
        run("8.  ingest_track_score (duplicate event)",  test_ingestDuplicate());
        run("9.  ingest_kafka_event (match.scored)",     test_ingestKafkaEvent());
        run("10. ingest_track_score (GD - cand2)",       test_ingestCand2Scores());

        // ── Aggregation ───────────────────────────────────────────────────────
        run("11. compute_aggregate (cand1 - all 3 tracks)", test_computeAggregate());
        run("12. compute_aggregate (idempotent return)",    test_computeIdempotent());
        run("13. get_aggregation_status (cand1)",           test_getAggStatus());
        run("14. compute_aggregate (cand2 partial - no dojo)", test_computePartial());
        run("15. retry_aggregation",                     test_retryAggregation());
        run("16. batch_compute_aggregates",              test_batchCompute());

        // ── Query ─────────────────────────────────────────────────────────────
        run("17. get_aggregate_result",                  test_getAggResult());
        run("18. get_candidate_breakdown (explainability)", test_getBreakdown());
        run("19. get_aggregation_history",               test_getHistory());
        run("20. get_score_history",                     test_getScoreHistory());

        // ── DLQ ───────────────────────────────────────────────────────────────
        run("21. get_dlq_entries",                       test_getDlqEntries());
        run("22. resolve_dlq_entry",                     test_resolveDlq());

        // ── Analytics ─────────────────────────────────────────────────────────
        run("23. get_metrics",                           test_getMetrics());
        run("24. get_audit_log",                         test_getAuditLog());

        // ── Security ──────────────────────────────────────────────────────────
        run("25. Security: SQL injection in candidate_id", test_sqlInjection());
        run("26. Security: CANDIDATE cannot see other's",  test_candidateIsolation());
        run("27. Security: RECRUITER cannot configure",    test_recruiterCannotConfig());
        run("28. Security: CANDIDATE cannot ingest score", test_candidateCannotIngest());
        run("29. Security: invalid JWT → rejected",        test_invalidToken());
        run("30. Security: score > 100 → rejected",        test_invalidScore());
        run("31. Security: weights not summing to 1.0",    test_invalidWeights());
        run("32. get_candidate_breakdown (CANDIDATE self)", test_candidateSelfBreakdown());

        System.out.println();
        System.out.println("=============================================================");
        System.out.printf("  PASSED: %d  |  FAILED: %d  |  TOTAL: %d%n", passed, failed, passed + failed);
        System.out.println("=============================================================");
        System.exit(failed > 0 ? 1 : 0);
    }

    // =========================================================================
    // Tests
    // =========================================================================

    static TestResult test_configureRubric() {
        Map<String,Object> a = a(ADMIN_TOKEN);
        a.put("job_role_id","ENGINEER");
        a.put("gd_weight","0.35"); a.put("interview_weight","0.40"); a.put("dojo_weight","0.25");
        a.put("bronze_threshold","0"); a.put("silver_threshold","50"); a.put("gold_threshold","70"); a.put("platinum_threshold","85");
        String r = call("configure_rubric", a);
        return ok(r.contains("CREATED") && r.contains("version"), r);
    }

    static TestResult test_getRubric() {
        Map<String,Object> a = a(REC_TOKEN);
        a.put("job_role_id","ENGINEER");
        String r = call("get_rubric", a);
        return ok(r.contains("weights") && r.contains("thresholds"), r);
    }

    static TestResult test_configureCycle() {
        Map<String,Object> a = a(ADMIN_TOKEN);
        a.put("cycle_id",    CYCLE1);
        a.put("job_role_id", "ENGINEER");
        a.put("tracks",      "GD,INTERVIEW,DOJO");
        a.put("status",      "ACTIVE");
        String r = call("configure_cycle", a);
        return ok(r.contains("CREATED") && r.contains("tracks"), r);
    }

    static TestResult test_getCycleConfig() {
        Map<String,Object> a = a(REC_TOKEN);
        a.put("cycle_id", CYCLE1);
        String r = call("get_cycle_config", a);
        return ok(r.contains("cycle_id") && r.contains("ACTIVE"), r);
    }

    static TestResult test_ingestGdSnapshot() {
        Map<String,Object> a = a(SA_TOKEN);
        a.put("candidate_id","cand-alice-001"); a.put("cycle_id",CYCLE1);
        a.put("event_id","ev-gd-001"); a.put("gd_score","78");
        a.put("session_id","sess-gd-001");
        a.put("scored_at", Instant.now().minusSeconds(3600*3).toString());
        String r = call("ingest_gd_snapshot", a);
        return ok(r.contains("OK") && r.contains("score_id"), r);
    }

    static TestResult test_ingestInterviewSnapshot() {
        Map<String,Object> a = a(SA_TOKEN);
        a.put("candidate_id","cand-alice-001"); a.put("cycle_id",CYCLE1);
        a.put("event_id","ev-int-001"); a.put("interview_score","85");
        a.put("interview_id","int-session-001");
        a.put("scored_at", Instant.now().minusSeconds(3600*2).toString());
        String r = call("ingest_interview_snapshot", a);
        return ok(r.contains("OK") && r.contains("score_id"), r);
    }

    static TestResult test_ingestDojoScore() {
        Map<String,Object> a = a(SA_TOKEN);
        a.put("candidate_id","cand-alice-001"); a.put("cycle_id",CYCLE1);
        a.put("event_id","ev-dojo-001"); a.put("track","DOJO"); a.put("raw_score","72");
        a.put("scored_at", Instant.now().minusSeconds(3600).toString());
        String r = call("ingest_track_score", a);
        return ok(r.contains("CREATED") && r.contains("score_id"), r);
    }

    static TestResult test_ingestDuplicate() {
        Map<String,Object> a = a(SA_TOKEN);
        a.put("candidate_id","cand-alice-001"); a.put("cycle_id",CYCLE1);
        a.put("event_id","ev-gd-001"); // duplicate
        a.put("track","GD"); a.put("raw_score","78");
        String r = call("ingest_track_score", a);
        return ok(r.contains("DUPLICATE"), r);
    }

    static TestResult test_ingestKafkaEvent() {
        Map<String,Object> a = a(SA_TOKEN);
        a.put("event_payload", "{\"event_id\":\"ev-kafka-001\",\"candidate_id\":\"cand-bob-002\"," +
                "\"cycle_id\":\"" + CYCLE1 + "\",\"track\":\"DOJO\",\"raw_score\":65," +
                "\"tenant_id\":\"tenant-001\",\"scored_at\":\"" + Instant.now() + "\"}");
        a.put("message_id","kafka-msg-001");
        a.put("auto_aggregate","false");
        String r = call("ingest_kafka_event", a);
        return ok(r.contains("match.scored") && r.contains("score_id"), r);
    }

    static TestResult test_ingestCand2Scores() {
        // Bob has only GD + INTERVIEW (partial — no DOJO from kafka counts separately)
        Map<String,Object> gd = a(SA_TOKEN);
        gd.put("candidate_id",CAND2); gd.put("cycle_id",CYCLE1);
        gd.put("event_id","ev-gd-bob-001"); gd.put("gd_score","60");
        gd.put("session_id","sess-gd-bob");
        call("ingest_gd_snapshot", gd);

        Map<String,Object> intv = a(SA_TOKEN);
        intv.put("candidate_id",CAND2); intv.put("cycle_id",CYCLE1);
        intv.put("event_id","ev-int-bob-001"); intv.put("interview_score","70");
        intv.put("interview_id","int-bob-001");
        String r = call("ingest_interview_snapshot", intv);
        return ok(r.contains("OK"), r);
    }

    static TestResult test_computeAggregate() {
        Map<String,Object> a = a(SA_TOKEN);
        a.put("candidate_id","cand-alice-001"); a.put("cycle_id",CYCLE1);
        String r = call("compute_aggregate", a);
        return ok(r.contains("normalized_score") && r.contains("belt_eligible_level")
                && r.contains("per_track_breakdown"), r);
    }

    static TestResult test_computeIdempotent() {
        Map<String,Object> a = a(SA_TOKEN);
        a.put("candidate_id","cand-alice-001"); a.put("cycle_id",CYCLE1);
        String r = call("compute_aggregate", a);
        return ok(r.contains("idempotent_return"), r);
    }

    static TestResult test_getAggStatus() {
        Map<String,Object> a = a(REC_TOKEN);
        a.put("candidate_id","cand-alice-001"); a.put("cycle_id",CYCLE1);
        String r = call("get_aggregation_status", a);
        return ok(r.contains("COMPUTED") && r.contains("normalized_score"), r);
    }

    static TestResult test_computePartial() {
        // Bob only has GD + INTERVIEW → weights redistributed
        Map<String,Object> a = a(SA_TOKEN);
        a.put("candidate_id",CAND2); a.put("cycle_id",CYCLE1);
        String r = call("compute_aggregate", a);
        return ok(r.contains("normalized_score") && r.contains("belt_eligible_level"), r);
    }

    static TestResult test_retryAggregation() {
        Map<String,Object> a = a(ADMIN_TOKEN);
        a.put("candidate_id","cand-alice-001"); a.put("cycle_id",CYCLE1);
        a.put("reason","Test retry for validation");
        String r = call("retry_aggregation", a);
        return ok(r.contains("normalized_score") || r.contains("COMPUTED") || r.contains("CONFLICT"), r);
    }

    static TestResult test_batchCompute() {
        Map<String,Object> a = a(SA_TOKEN);
        a.put("candidate_ids","cand-alice-001,cand-bob-002");
        a.put("cycle_id",CYCLE1);
        String r = call("batch_compute_aggregates", a);
        return ok(r.contains("total") && r.contains("results"), r);
    }

    static TestResult test_getAggResult() {
        Map<String,Object> a = a(REC_TOKEN);
        a.put("candidate_id","cand-alice-001"); a.put("cycle_id",CYCLE1);
        String r = call("get_aggregate_result", a);
        return ok(r.contains("normalized_score") && r.contains("belt_eligible_level"), r);
    }

    static TestResult test_getBreakdown() {
        Map<String,Object> a = a(REC_TOKEN);
        a.put("candidate_id","cand-alice-001"); a.put("cycle_id",CYCLE1);
        String r = call("get_candidate_breakdown", a);
        return ok(r.contains("per_track_breakdown") && r.contains("dpdpa_note")
                && r.contains("weight_applied"), r);
    }

    static TestResult test_getHistory() {
        Map<String,Object> a = a(REC_TOKEN);
        a.put("limit","10");
        String r = call("get_aggregation_history", a);
        return ok(r.contains("records") && r.contains("total"), r);
    }

    static TestResult test_getScoreHistory() {
        Map<String,Object> a = a(REC_TOKEN);
        a.put("candidate_id","cand-alice-001");
        String r = call("get_score_history", a);
        return ok(r.contains("scores") && r.contains("total"), r);
    }

    static TestResult test_getDlqEntries() {
        Map<String,Object> a = a(ADMIN_TOKEN);
        String r = call("get_dlq_entries", a);
        boolean ok = r.contains("entries") && r.contains("total");
        if (ok) {
            // Try to extract a dlq_id if any exist
            int idx = r.indexOf("\"dlq_id\":\"");
            if (idx >= 0) {
                int s = idx + 10, e = r.indexOf('"', s);
                if (e > s) dlqId = r.substring(s, e);
            }
        }
        return ok(ok, r);
    }

    static TestResult test_resolveDlq() {
        if (dlqId == null) {
            // Force create a DLQ entry by simulating a failure
            Map<String,Object> a = a(SA_TOKEN);
            a.put("candidate_id","cand-dlq-test"); a.put("cycle_id","cycle-nonexistent");
            a.put("event_id","ev-dlq-001"); a.put("track","GD"); a.put("raw_score","80");
            call("ingest_track_score", a);
            // Try to get DLQ entries again
            Map<String,Object> dq = a(ADMIN_TOKEN);
            String dr = call("get_dlq_entries", dq);
            int idx = dr.indexOf("\"dlq_id\":\"");
            if (idx >= 0) { int s=idx+10, e=dr.indexOf('"',s); if(e>s) dlqId=dr.substring(s,e); }
            if (dlqId == null) return TestResult.skip("No DLQ entries to resolve");
        }
        Map<String,Object> a = a(ADMIN_TOKEN);
        a.put("dlq_id", dlqId);
        a.put("action", "RESOLVED");
        a.put("notes",  "Test resolution");
        String r = call("resolve_dlq_entry", a);
        return ok(r.contains("RESOLVED") || r.contains("resolved"), r);
    }

    static TestResult test_getMetrics() {
        Map<String,Object> a = a(ADMIN_TOKEN);
        String r = call("get_metrics", a);
        return ok(r.contains("events_consumed_total") && r.contains("aggregations_succeeded_total")
                && r.contains("belt_distribution"), r);
    }

    static TestResult test_getAuditLog() {
        Map<String,Object> a = a(ADMIN_TOKEN);
        a.put("limit","50");
        String r = call("get_audit_log", a);
        return ok(r.contains("entries") && r.contains("persistence") && r.contains("ClickHouse"), r);
    }

    // ── Security tests ────────────────────────────────────────────────────────

    static TestResult test_sqlInjection() {
        Map<String,Object> a = a(SA_TOKEN);
        a.put("candidate_id","'; DROP TABLE aggregates; --");
        a.put("cycle_id",CYCLE1); a.put("event_id","ev-evil"); a.put("track","GD"); a.put("raw_score","80");
        try { call("ingest_track_score", a); return ok(false,"Should block SQL injection"); }
        catch (SecurityException e) { return ok(e.getMessage().contains("injection"), "Blocked: "+e.getMessage()); }
    }

    static TestResult test_candidateIsolation() {
        Map<String,Object> a = a(CAND_TOKEN); // cand-alice token
        a.put("candidate_id",CAND2); a.put("cycle_id",CYCLE1); // trying to view bob
        try { call("get_aggregate_result", a); return ok(false,"CANDIDATE should not see other's data"); }
        catch (SecurityException e) { return ok(e.getMessage().contains("CANDIDATE"), "Blocked: "+e.getMessage()); }
    }

    static TestResult test_recruiterCannotConfig() {
        Map<String,Object> a = a(REC_TOKEN);
        a.put("job_role_id","DESIGNER");
        a.put("gd_weight","0.33"); a.put("interview_weight","0.34"); a.put("dojo_weight","0.33");
        try { call("configure_rubric", a); return ok(false,"RECRUITER cannot configure rubrics"); }
        catch (SecurityException e) { return ok(e.getMessage().contains("denied"), "Blocked: "+e.getMessage()); }
    }

    static TestResult test_candidateCannotIngest() {
        Map<String,Object> a = a(CAND_TOKEN);
        a.put("candidate_id",CAND1); a.put("cycle_id",CYCLE1);
        a.put("event_id","ev-cand-inject"); a.put("track","GD"); a.put("raw_score","100");
        try { call("ingest_track_score", a); return ok(false,"CANDIDATE cannot ingest scores"); }
        catch (SecurityException e) { return ok(e.getMessage().contains("denied"), "Blocked: "+e.getMessage()); }
    }

    static TestResult test_invalidToken() {
        Map<String,Object> a = new LinkedHashMap<>();
        a.put("auth_token","not.a.real.token"); a.put("tenant_id",TENANT);
        a.put("candidate_id",CAND1); a.put("cycle_id",CYCLE1);
        try { call("get_aggregate_result", a); return ok(false,"Should reject invalid token"); }
        catch (SecurityException e) { return ok(true, "Correctly rejected: "+e.getMessage()); }
    }

    static TestResult test_invalidScore() {
        Map<String,Object> a = a(SA_TOKEN);
        a.put("candidate_id",CAND1); a.put("cycle_id",CYCLE1);
        a.put("event_id","ev-invalid-score"); a.put("track","GD"); a.put("raw_score","150");
        try { call("ingest_track_score", a); return ok(false,"Should reject score > 100"); }
        catch (IllegalArgumentException e) { return ok(e.getMessage().contains("0-100"), "Correctly rejected: "+e.getMessage()); }
    }

    static TestResult test_invalidWeights() {
        Map<String,Object> a = a(ADMIN_TOKEN);
        a.put("job_role_id","BAD");
        a.put("gd_weight","0.50"); a.put("interview_weight","0.50"); a.put("dojo_weight","0.50"); // sum=1.5
        try { call("configure_rubric", a); return ok(false,"Weights not summing to 1.0 should fail"); }
        catch (IllegalArgumentException e) { return ok(e.getMessage().contains("1.0"), "Correctly rejected: "+e.getMessage()); }
    }

    static TestResult test_candidateSelfBreakdown() {
        // First ingest scores for CAND1 in new cycle, compute, then CANDIDATE views self
        String selfCycle = "cycle-self-test";
        Map<String,Object> cfg = a(ADMIN_TOKEN);
        cfg.put("cycle_id",selfCycle); cfg.put("job_role_id","ENGINEER"); cfg.put("tracks","GD,INTERVIEW");
        call("configure_cycle", cfg);

        Map<String,Object> gd = a(SA_TOKEN);
        gd.put("candidate_id",CAND1); gd.put("cycle_id",selfCycle);
        gd.put("event_id","ev-self-gd-001"); gd.put("gd_score","75"); gd.put("session_id","sess-self");
        call("ingest_gd_snapshot", gd);

        Map<String,Object> intv = a(SA_TOKEN);
        intv.put("candidate_id",CAND1); intv.put("cycle_id",selfCycle);
        intv.put("event_id","ev-self-int-001"); intv.put("interview_score","80"); intv.put("interview_id","int-self");
        call("ingest_interview_snapshot", intv);

        Map<String,Object> comp = a(SA_TOKEN);
        comp.put("candidate_id",CAND1); comp.put("cycle_id",selfCycle);
        call("compute_aggregate", comp);

        // Now CANDIDATE views own breakdown
        Map<String,Object> a = a(CAND_TOKEN);
        a.put("candidate_id",CAND1); a.put("cycle_id",selfCycle);
        String r = call("get_candidate_breakdown", a);
        return ok(r.contains("per_track_breakdown") && r.contains("dpdpa_note"), r);
    }

    // ── Infrastructure ────────────────────────────────────────────────────────

    private static String call(String tool, Map<String,Object> args) { return ROUTER.dispatch(tool, args); }

    private static void run(String name, TestResult result) {
        String icon = result.skipped ? "⏭ " : (result.passed ? "✅" : "❌");
        System.out.printf("  %s  %-56s %s%n", icon, name, result.skipped?"SKIPPED":(result.passed?"PASS":"FAIL"));
        if (verbose || (!result.passed && !result.skipped)) {
            String out = result.output!=null&&result.output.length()>280 ? result.output.substring(0,280)+"..." : result.output;
            System.out.println("         " + out);
        }
        if (!result.skipped) { if (result.passed) passed++; else failed++; }
    }

    private static TestResult ok(boolean p, String o) { return new TestResult(p, o); }

    private static Map<String,Object> a(String token) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("auth_token", token); m.put("tenant_id", TENANT); return m;
    }

    private static String devToken(String userId, String role) {
        String h = b64url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String p = b64url("{\"sub\":\""+userId+"\",\"tenant_id\":\""+TENANT+"\","
                +"\"email\":\""+userId+"@ecoskiller.dev\",\"exp\":9999999999,"
                +"\"roles\":[\""+role+"\"]}");
        return h+"."+p+".dev-sig";
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
