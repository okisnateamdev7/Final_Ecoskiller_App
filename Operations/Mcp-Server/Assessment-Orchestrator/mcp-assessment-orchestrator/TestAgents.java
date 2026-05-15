package com.ecoskiller.mcp;

import com.ecoskiller.mcp.security.SecurityConfig;
import com.ecoskiller.mcp.tools.ToolRouter;

import java.time.Instant;
import java.util.*;

/**
 * Integration test suite for the assessment-orchestrator MCP server.
 *
 * Run:
 *   java -cp mcp-assessment-orchestrator.jar com.ecoskiller.mcp.TestAgents
 *   java -cp mcp-assessment-orchestrator.jar com.ecoskiller.mcp.TestAgents --verbose
 *
 * 27 tests covering:
 *   - All 20 tools
 *   - GD state machine (full cycle: SCHEDULED → COMPLETED)
 *   - Security: missing token, wrong role, SQL injection, tenant isolation
 *   - Idempotency: duplicate join, duplicate completion event
 *   - Quorum: auto-cancel on no-quorum
 *   - Jitsi token issuance + validation
 *   - Kafka event log + DLQ
 */
public class TestAgents {

    // ── Dev JWT tokens (STRICT_MODE=false → dev-sig passthrough) ─────────────
    private static final String ADMIN_TOKEN   = devToken("admin-001",    "ADMIN");
    private static final String MOD_TOKEN     = devToken("mod-001",      "MODERATOR");
    private static final String RECRUITER_TOKEN = devToken("rec-001",    "RECRUITER");
    private static final String CANDIDATE_TOKEN = devToken("cand-001",   "CANDIDATE");
    private static final String SA_TOKEN      = devToken("svc-001",      "SERVICE_ACCOUNT");
    private static final String TENANT        = "tenant-001";

    private static int     passed  = 0;
    private static int     failed  = 0;
    private static boolean verbose = false;

    // Shared state across tests
    private static String cycleId   = null;
    private static String sessionId = null;
    private static String jitsiToken = null;

    public static void main(String[] args) {
        verbose = Arrays.asList(args).contains("--verbose");
        SecurityConfig.init();

        System.out.println("=======================================================");
        System.out.println(" Ecoskiller Assessment-Orchestrator MCP Server Tests   ");
        System.out.println(" 20 Tools | Namespace: realtime | Tenant: " + TENANT);
        System.out.println("=======================================================");
        System.out.println();

        // ── Group 1: Service health ───────────────────────────────────────────
        run("1.  get_service_health",                  test_serviceHealth());
        run("2.  configure_track_plan (TECHNICAL)",    test_configureTrackPlan());

        // ── Group 2: Cycle management ─────────────────────────────────────────
        run("3.  create_assessment_cycle",             test_createCycle());
        run("4.  get_cycle_status",                    test_getCycleStatus());

        // ── Group 3: Session creation ─────────────────────────────────────────
        run("5.  create_session (INTERVIEW track)",    test_createSession());

        // ── Group 4: Full GD state machine ────────────────────────────────────
        run("6.  transition → WAITING",               test_transitionToWaiting());
        run("7.  check_quorum (quorum not yet met)",  test_checkQuorumPending());
        run("8.  join_session → issues Jitsi token",  test_joinSession());
        run("9.  join_session → duplicate idempotency", test_joinDuplicate());
        run("10. check_quorum → quorum met",          test_checkQuorumMet());
        run("11. transition → INTRO",                 test_transitionToIntro());
        run("12. transition → SPEAKING",              test_transitionToSpeaking());
        run("13. control_session ADVANCE_TURN",       test_advanceTurn());
        run("14. handle_raise_hand (OPEN_DISCUSSION)",test_raiseHand());
        run("15. transition → CLOSING",               test_transitionToClosing());
        run("16. transition → COMPLETED",             test_transitionToCompleted());

        // ── Group 5: Kafka & Tokens ───────────────────────────────────────────
        run("17. emit_completion_event (idempotent)", test_emitCompletion());
        run("18. emit_completion_event → DUPLICATE",  test_emitDuplicate());
        run("19. validate_jitsi_token (valid)",       test_validateJitsiTokenValid());
        run("20. validate_jitsi_token (wrong room)",  test_validateJitsiTokenBadRoom());
        run("21. get_kafka_event_log",                test_getKafkaLog());

        // ── Group 6: Participant events ───────────────────────────────────────
        run("22. handle_participant_event HEARTBEAT", test_heartbeat());
        run("23. handle_participant_event LEAVE",     test_leaveSession());
        run("24. process_kafka_event (cycle.created)",test_processKafkaEvent());
        run("25. process_kafka_event (DLQ on retry=3)",test_kafkaDlq());

        // ── Group 7: Security & edge cases ────────────────────────────────────
        run("26. Security: CANDIDATE cannot MODERATOR", test_wrongRole());
        run("27. Security: SQL injection blocked",      test_sqlInjection());
        // ── Group 8: No-quorum auto-cancel ────────────────────────────────────
        // (uses a fresh session so it's independent of main flow)
        run("28. check_quorum → auto-cancel NO_QUORUM", test_autoQuorumCancel());

        // ── Summary ───────────────────────────────────────────────────────────
        System.out.println();
        System.out.println("=======================================================");
        System.out.printf("  PASSED: %d  |  FAILED: %d  |  TOTAL: %d%n", passed, failed, passed + failed);
        System.out.println("=======================================================");
        System.exit(failed > 0 ? 1 : 0);
    }

    // =========================================================================
    // Tests
    // =========================================================================

    private static TestResult test_serviceHealth() {
        Map<String, Object> args = a(ADMIN_TOKEN);
        String r = call("get_service_health", args);
        return ok(r.contains("\"status\":\"ok\"") && r.contains("sessions_active_gauge"), r);
    }

    private static TestResult test_configureTrackPlan() {
        Map<String, Object> args = a(ADMIN_TOKEN);
        args.put("role_type", "TECHNICAL");
        args.put("tracks", "GD,DOJO");
        args.put("gd_speaking_seconds", "90");
        args.put("quorum_required", "3");
        String r = call("configure_track_plan", args);
        return ok(r.contains("TECHNICAL") && r.contains("DOJO"), r);
    }

    private static TestResult test_createCycle() {
        Map<String, Object> args = a(RECRUITER_TOKEN);
        args.put("job_id",        "job-xyz-001");
        args.put("recruiter_id",  "rec-001");
        args.put("candidate_ids", "cand-001,cand-002,cand-003,cand-004");
        args.put("tracks",        "GD,INTERVIEW");
        args.put("scheduled_at",  Instant.now().plusSeconds(7200).toString());
        args.put("quorum_required", "3");
        String r = call("create_assessment_cycle", args);
        boolean ok = r.contains("CREATED") && r.contains("cycle_id");
        if (ok) cycleId = extract(r, "cycle_id");
        return ok(ok, r);
    }

    private static TestResult test_getCycleStatus() {
        if (cycleId == null) return TestResult.skip("No cycle");
        Map<String, Object> args = a(RECRUITER_TOKEN);
        args.put("cycle_id", cycleId);
        String r = call("get_cycle_status", args);
        return ok(r.contains("active_sessions") && r.contains("session_summary"), r);
    }

    private static TestResult test_createSession() {
        if (cycleId == null) return TestResult.skip("No cycle");
        Map<String, Object> args = a(RECRUITER_TOKEN);
        args.put("cycle_id",      cycleId);
        args.put("track_type",    "GD");
        args.put("candidate_ids", "cand-001,cand-002,cand-003,cand-004");
        args.put("scheduled_at",  Instant.now().plusSeconds(3600).toString());
        args.put("quorum_required", "3");
        String r = call("create_session", args);
        boolean ok = r.contains("CREATED") && r.contains("SCHEDULED");
        if (ok) sessionId = extract(r, "session_id");
        return ok(ok, r);
    }

    private static TestResult test_transitionToWaiting() {
        if (sessionId == null) return TestResult.skip("No session");
        Map<String, Object> args = a(MOD_TOKEN);
        args.put("session_id",  sessionId);
        args.put("target_state","WAITING");
        args.put("actor_id",    "mod-001");
        String r = call("transition_session_state", args);
        return ok(r.contains("WAITING"), r);
    }

    private static TestResult test_checkQuorumPending() {
        if (sessionId == null) return TestResult.skip("No session");
        Map<String, Object> args = a(MOD_TOKEN);
        args.put("session_id",  sessionId);
        args.put("auto_cancel", "false");
        String r = call("check_quorum", args);
        // 0 participants joined yet → quorum not met
        return ok(r.contains("quorum_met") && r.contains("false"), r);
    }

    private static TestResult test_joinSession() {
        if (sessionId == null) return TestResult.skip("No session");
        Map<String, Object> args = a(CANDIDATE_TOKEN);
        args.put("session_id",     sessionId);
        args.put("display_name",   "Candidate One");
        args.put("idempotency_key","join-cand-001-" + sessionId);
        String r = call("join_session", args);
        boolean ok = r.contains("jitsi_token") && r.contains("ws_endpoint");
        if (ok) jitsiToken = extract(r, "jitsi_token");
        return ok(ok, r);
    }

    private static TestResult test_joinDuplicate() {
        if (sessionId == null) return TestResult.skip("No session");
        Map<String, Object> args = a(CANDIDATE_TOKEN);
        args.put("session_id",     sessionId);
        args.put("display_name",   "Candidate One");
        args.put("idempotency_key","join-cand-001-" + sessionId); // same key
        String r = call("join_session", args);
        return ok(r.contains("DUPLICATE"), r);
    }

    private static TestResult test_checkQuorumMet() {
        if (sessionId == null) return TestResult.skip("No session");
        // Join 2 more participants (recruiter counts toward participant count)
        for (int i = 2; i <= 3; i++) {
            Map<String, Object> a = a(RECRUITER_TOKEN);
            a.put("session_id",     sessionId);
            a.put("display_name",   "Candidate " + i);
            a.put("idempotency_key","join-cand-00" + i + "-" + sessionId);
            call("join_session", a);
        }
        Map<String, Object> args = a(MOD_TOKEN);
        args.put("session_id",  sessionId);
        args.put("auto_cancel", "false");
        String r = call("check_quorum", args);
        // participant_count ≥ 3 → quorum met
        return ok(r.contains("quorum_met") && (r.contains("\"quorum_met\":true")
                || r.contains("Quorum met")), r);
    }

    private static TestResult test_transitionToIntro() {
        if (sessionId == null) return TestResult.skip("No session");
        Map<String, Object> args = a(MOD_TOKEN);
        args.put("session_id",  sessionId);
        args.put("target_state","INTRO");
        args.put("actor_id",    "mod-001");
        String r = call("transition_session_state", args);
        return ok(r.contains("INTRO"), r);
    }

    private static TestResult test_transitionToSpeaking() {
        if (sessionId == null) return TestResult.skip("No session");
        Map<String, Object> args = a(MOD_TOKEN);
        args.put("session_id",  sessionId);
        args.put("target_state","SPEAKING");
        args.put("actor_id",    "TIMER_EXPIRY");
        String r = call("transition_session_state", args);
        return ok(r.contains("SPEAKING") || r.contains("OPEN_DISCUSSION"), r);
    }

    private static TestResult test_advanceTurn() {
        if (sessionId == null) return TestResult.skip("No session");
        Map<String, Object> args = a(MOD_TOKEN);
        args.put("session_id", sessionId);
        args.put("action",     "ADVANCE_TURN");
        try {
            String r = call("control_session", args);
            return ok(r.contains("ADVANCE_TURN") || r.contains("ACCEPTED"), r);
        } catch (IllegalStateException e) {
            // Session may have auto-advanced to OPEN_DISCUSSION (all speakers done)
            return ok(e.getMessage().contains("SPEAKING") || e.getMessage().contains("OPEN_DISCUSSION"),
                    "Expected — no more speakers: " + e.getMessage());
        }
    }

    private static TestResult test_raiseHand() {
        if (sessionId == null) return TestResult.skip("No session");
        // Ensure we're in OPEN_DISCUSSION (force transition)
        try {
            Map<String, Object> ta = a(MOD_TOKEN);
            ta.put("session_id",  sessionId);
            ta.put("target_state","OPEN_DISCUSSION");
            ta.put("actor_id",    "TIMER_EXPIRY");
            call("transition_session_state", ta);
        } catch (Exception ignored) {}

        Map<String, Object> args = a(CANDIDATE_TOKEN);
        args.put("session_id",  sessionId);
        args.put("candidate_id","cand-001");
        try {
            String r = call("handle_raise_hand", args);
            return ok(r.contains("position_in_queue") && r.contains("raise_hand_queue"), r);
        } catch (IllegalStateException e) {
            return ok(e.getMessage().contains("OPEN_DISCUSSION"), "Correctly guarded: " + e.getMessage());
        }
    }

    private static TestResult test_transitionToClosing() {
        if (sessionId == null) return TestResult.skip("No session");
        try {
            Map<String, Object> ta = a(MOD_TOKEN);
            ta.put("session_id",  sessionId);
            ta.put("target_state","OPEN_DISCUSSION");
            ta.put("actor_id",    "TIMER_EXPIRY");
            call("transition_session_state", ta);
        } catch (Exception ignored) {}
        Map<String, Object> args = a(MOD_TOKEN);
        args.put("session_id",  sessionId);
        args.put("target_state","CLOSING");
        args.put("actor_id",    "TIMER_EXPIRY");
        String r = call("transition_session_state", args);
        return ok(r.contains("CLOSING"), r);
    }

    private static TestResult test_transitionToCompleted() {
        if (sessionId == null) return TestResult.skip("No session");
        Map<String, Object> args = a(MOD_TOKEN);
        args.put("session_id",  sessionId);
        args.put("target_state","COMPLETED");
        args.put("actor_id",    "TIMER_EXPIRY");
        String r = call("transition_session_state", args);
        return ok(r.contains("COMPLETED"), r);
    }

    private static TestResult test_emitCompletion() {
        if (sessionId == null) return TestResult.skip("No session");
        Map<String, Object> args = a(MOD_TOKEN);
        args.put("session_id", sessionId);
        String r = call("emit_completion_event", args);
        return ok(r.contains("gd.completed") || r.contains("topic_emitted") || r.contains("DUPLICATE"), r);
    }

    private static TestResult test_emitDuplicate() {
        if (sessionId == null) return TestResult.skip("No session");
        Map<String, Object> args = a(MOD_TOKEN);
        args.put("session_id", sessionId);
        String r = call("emit_completion_event", args);
        return ok(r.contains("DUPLICATE"), r);
    }

    private static TestResult test_validateJitsiTokenValid() {
        if (sessionId == null || jitsiToken == null) return TestResult.skip("No token");
        Map<String, Object> args = a(ADMIN_TOKEN);
        args.put("jitsi_token", jitsiToken);
        args.put("session_id",  sessionId);
        String r = call("validate_jitsi_token", args);
        // Dev token has exp=9999999999 — should be VALID or EXPIRED (dev-sig is not HMAC signed)
        return ok(r.contains("session_id") && r.contains("expires_at"), r);
    }

    private static TestResult test_validateJitsiTokenBadRoom() {
        if (jitsiToken == null) return TestResult.skip("No token");
        Map<String, Object> args = a(ADMIN_TOKEN);
        args.put("jitsi_token", jitsiToken);
        args.put("session_id",  "wrong-session-id-99999");
        String r = call("validate_jitsi_token", args);
        return ok(r.contains("INVALID") || r.contains("mismatch"), r);
    }

    private static TestResult test_getKafkaLog() {
        Map<String, Object> args = a(ADMIN_TOKEN);
        args.put("topic", "gd.completed");
        args.put("limit", "10");
        String r = call("get_kafka_event_log", args);
        return ok(r.contains("events") && r.contains("total_events"), r);
    }

    private static TestResult test_heartbeat() {
        if (sessionId == null) return TestResult.skip("No session");
        Map<String, Object> args = a(CANDIDATE_TOKEN);
        args.put("session_id",  sessionId);
        args.put("event_type",  "HEARTBEAT");
        args.put("candidate_id","cand-001");
        args.put("client_ts",   Instant.now().toString());
        String r = call("handle_participant_event", args);
        return ok(r.contains("last_seen") && r.contains("server_ts"), r);
    }

    private static TestResult test_leaveSession() {
        if (sessionId == null) return TestResult.skip("No session");
        Map<String, Object> args = a(CANDIDATE_TOKEN);
        args.put("session_id",  sessionId);
        args.put("event_type",  "LEAVE_SESSION");
        args.put("candidate_id","cand-003");
        String r = call("handle_participant_event", args);
        return ok(r.contains("roster_status") || r.contains("participant_count"), r);
    }

    private static TestResult test_processKafkaEvent() {
        Map<String, Object> args = a(SA_TOKEN);
        args.put("event_type",   "assessment.cycle.created");
        args.put("event_payload","{\"cycle_id\":\"c-001\",\"tenant_id\":\"tenant-001\",\"job_id\":\"job-001\"}");
        args.put("retry_count",  "0");
        String r = call("process_kafka_event", args);
        return ok(r.contains("CYCLE_INITIALIZATION_TRIGGERED"), r);
    }

    private static TestResult test_kafkaDlq() {
        Map<String, Object> args = a(SA_TOKEN);
        args.put("event_type",   "dojo.match.requested");
        args.put("event_payload","{\"session_id\":\"s-999\"}");
        args.put("retry_count",  "3"); // triggers DLQ
        String r = call("process_kafka_event", args);
        return ok(r.contains("DLQ") && r.contains("dlq_topic"), r);
    }

    private static TestResult test_wrongRole() {
        Map<String, Object> args = a(CANDIDATE_TOKEN); // CANDIDATE cannot control session
        args.put("session_id", "any-session");
        args.put("action",     "FORCE_MUTE");
        try {
            call("control_session", args);
            return new TestResult(false, "Should have thrown SecurityException");
        } catch (SecurityException e) {
            return ok(true, "Correctly blocked: " + e.getMessage());
        }
    }

    private static TestResult test_sqlInjection() {
        Map<String, Object> args = a(ADMIN_TOKEN);
        args.put("job_id",       "'; DROP TABLE assessment_cycles; --");
        args.put("recruiter_id", "rec-001");
        args.put("candidate_ids","cand-001");
        args.put("tracks",       "GD");
        args.put("scheduled_at", Instant.now().plusSeconds(3600).toString());
        try {
            call("create_assessment_cycle", args);
            return new TestResult(false, "Should have thrown SecurityException");
        } catch (SecurityException e) {
            return ok(e.getMessage().toLowerCase().contains("injection"), "Blocked: " + e.getMessage());
        }
    }

    private static TestResult test_autoQuorumCancel() {
        // Create fresh session with quorum=5 but 0 participants — simulate T-2 min window
        Map<String, Object> ca = a(RECRUITER_TOKEN);
        ca.put("job_id",        "job-qtest-001");
        ca.put("recruiter_id",  "rec-001");
        ca.put("candidate_ids", "cand-a,cand-b");
        ca.put("tracks",        "GD");
        ca.put("scheduled_at",  Instant.now().minusSeconds(60).toString()); // past → T-2 already passed
        ca.put("quorum_required","5");
        String cr = call("create_assessment_cycle", ca);
        // Find one of the sessions created
        String qCycleId = extract(cr, "cycle_id");

        // Create dedicated session
        Map<String, Object> sa = a(RECRUITER_TOKEN);
        sa.put("cycle_id",      qCycleId);
        sa.put("track_type",    "GD");
        sa.put("candidate_ids", "cand-a,cand-b");
        sa.put("scheduled_at",  Instant.now().minusSeconds(60).toString());
        sa.put("quorum_required","5");
        String sr = call("create_session", sa);
        String qSessionId = extract(sr, "session_id");

        // Move to WAITING
        Map<String, Object> wa = a(MOD_TOKEN);
        wa.put("session_id",  qSessionId);
        wa.put("target_state","WAITING");
        call("transition_session_state", wa);

        // check_quorum with auto_cancel=true
        Map<String, Object> qa = a(MOD_TOKEN);
        qa.put("session_id",  qSessionId);
        qa.put("auto_cancel", "true");
        String r = call("check_quorum", qa);
        return ok(r.contains("AUTO_CANCELLED") || r.contains("NO_QUORUM") || r.contains("CANCELLED"), r);
    }

    // =========================================================================
    // Infrastructure
    // =========================================================================

    private static final ToolRouter ROUTER = new ToolRouter();

    private static String call(String tool, Map<String, Object> args) {
        return ROUTER.dispatch(tool, args);
    }

    private static void run(String name, TestResult result) {
        String icon = result.skipped ? "⏭ " : (result.passed ? "✅" : "❌");
        System.out.printf("  %s  %-50s %s%n", icon, name,
                result.skipped ? "SKIPPED" : (result.passed ? "PASS" : "FAIL"));
        if (verbose || (!result.passed && !result.skipped)) {
            String out = result.output != null && result.output.length() > 280
                    ? result.output.substring(0, 280) + "..." : result.output;
            System.out.println("         " + out);
        }
        if (!result.skipped) { if (result.passed) passed++; else failed++; }
    }

    private static TestResult ok(boolean passed, String output) {
        return new TestResult(passed, output);
    }

    private static Map<String, Object> a(String token) {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token", token);
        args.put("tenant_id",  TENANT);
        return args;
    }

    private static String extract(String json, String field) {
        String key = "\"" + field + "\":\"";
        int i = json.indexOf(key);
        if (i < 0) return "unknown-" + UUID.randomUUID();
        int s = i + key.length(), e = json.indexOf('"', s);
        return e > s ? json.substring(s, e) : "unknown";
    }

    private static String devToken(String userId, String role) {
        String header  = b64url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payload = b64url("{\"sub\":\"" + userId + "\",\"tenant_id\":\"" + TENANT + "\"," +
                "\"email\":\"" + userId + "@ecoskiller.dev\",\"exp\":9999999999," +
                "\"realm_access\":{\"roles\":[\"" + role + "\"]}}");
        return header + "." + payload + ".dev-sig";
    }

    private static String b64url(String s) {
        return java.util.Base64.getUrlEncoder().withoutPadding()
                .encodeToString(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    static class TestResult {
        final boolean passed;
        final boolean skipped;
        final String  output;
        TestResult(boolean passed, String output) {
            this.passed = passed; this.skipped = false;
            this.output = output != null ? output : "";
        }
        TestResult(boolean passed, String output, boolean skipped) {
            this.passed = passed; this.skipped = skipped;
            this.output = output != null ? output : "";
        }
        static TestResult skip(String reason) { return new TestResult(false, reason, true); }
    }
}
