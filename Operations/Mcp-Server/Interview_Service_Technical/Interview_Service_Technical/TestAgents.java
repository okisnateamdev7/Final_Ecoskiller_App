package com.ecoskiller.mcp.interview;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

/**
 * Test runner for all 18 Interview-Service MCP agents.
 *
 * Usage:
 *   java -cp interview-service-mcp.jar com.ecoskiller.mcp.interview.TestAgents
 *   java -cp interview-service-mcp.jar com.ecoskiller.mcp.interview.TestAgents --verbose
 */
public class TestAgents {

    // ── Test JWT (base64url-encoded, not real) ─────────────────────────────
    // Payload: {"sub":"user-001","tenant_id":"tenant-001","RECRUITER":true,
    //           "recruiter_id":"rec-001","email":"test@ecoskiller.com","exp":9999999999}
    private static final String TEST_JWT_RECRUITER =
        "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9" +
        ".eyJzdWIiOiJ1c2VyLTAwMSIsInRlbmFudF9pZCI6InRlbmFudC0wMDEiLCJSRUNSVUlURVIiOnRydWUsInJlY3J1aXRlcl9pZCI6InJlYy0wMDEiLCJlbWFpbCI6InRlc3RAZWNvc2tpbGxlci5jb20iLCJleHAiOjk5OTk5OTk5OTl9" +
        ".c2lnbmF0dXJlX3BsYWNlaG9sZGVyX2Zvcl90ZXN0aW5n";

    private static final String TEST_JWT_CANDIDATE =
        "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9" +
        ".eyJzdWIiOiJjYW5kLTAwMSIsInRlbmFudF9pZCI6InRlbmFudC0wMDEiLCJDQU5ESURBVEUiOnRydWUsImNhbmRpZGF0ZV9pZCI6ImNhbmQtMDAxIiwiZW1haWwiOiJjYW5kaWRhdGVAZWNvc2tpbGxlci5jb20iLCJleHAiOjk5OTk5OTk5OTl9" +
        ".c2lnbmF0dXJlX3BsYWNlaG9sZGVyX2Zvcl90ZXN0aW5n";

    private static final String INTERVIEW_ID = "a1b2c3d4-e5f6-7890-abcd-ef1234567890";

    private final ObjectMapper mapper = new ObjectMapper();
    private int passed = 0;
    private int failed = 0;

    public static void main(String[] args) throws Exception {
        boolean verbose = Arrays.asList(args).contains("--verbose");
        new TestAgents().runAll(verbose);
    }

    private void runAll(boolean verbose) throws Exception {
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println(" Ecoskiller Interview-Service MCP — Agent Tests");
        System.out.println("═══════════════════════════════════════════════════\n");

        test("initialize",              buildInit(),                            verbose, false);
        test("tools/list",              buildToolsList(),                       verbose, false);
        test("schedule_interview",      buildSchedule(),                        verbose, false);
        test("get_interview",           buildGet(),                             verbose, false);
        test("update_interview",        buildUpdate(),                          verbose, false);
        test("join_interview",          buildJoin(),                            verbose, false);
        test("pause_interview",         buildPause(),                           verbose, false);
        test("resume_interview",        buildResume(),                          verbose, false);
        test("complete_interview",      buildComplete(),                        verbose, false);
        test("get_interview_result",    buildGetResult(),                       verbose, false);
        test("submit_feedback",         buildSubmitFeedback(),                  verbose, false);
        test("get_questions",           buildGetQuestions(),                    verbose, false);
        test("log_answer",              buildLogAnswer(),                       verbose, false);
        test("list_interviews",         buildListInterviews(),                  verbose, false);
        test("reschedule_interview",    buildReschedule(),                      verbose, false);
        test("grant_recording_consent", buildConsentCandidate(),                verbose, false);
        test("delete_recording",        buildDeleteRecording(),                 verbose, false);
        test("get_analytics",           buildAnalytics(),                       verbose, false);
        test("detect_no_show",          buildNoShow(),                          verbose, false);
        test("get_session_health",      buildHealth(),                          verbose, false);

        // Security tests
        System.out.println("\n─── Security Tests ───");
        test("SEC: missing JWT",        buildMissingJwt(),                      verbose, true);
        test("SEC: malformed JWT",      buildMalformedJwt(),                    verbose, true);
        test("SEC: candidate→pause",    buildCandidatePause(),                  verbose, true);

        System.out.println("\n═══════════════════════════════════════════════════");
        System.out.printf("  Results: %d passed, %d failed, %d total%n",
            passed, failed, passed + failed);
        System.out.println("═══════════════════════════════════════════════════");

        if (failed > 0) System.exit(1);
    }

    private void test(String name, String requestJson, boolean verbose, boolean expectError) throws Exception {
        try {
            InterviewMcpServer server = new InterviewMcpServer();
            // Use reflection to call handleRequest for testing
            var handleRequest = InterviewMcpServer.class.getDeclaredMethod("handleRequest", JsonNode.class);
            handleRequest.setAccessible(true);

            JsonNode req  = mapper.readTree(requestJson);
            JsonNode resp = (JsonNode) handleRequest.invoke(server, req);
            String   json = mapper.writeValueAsString(resp);

            boolean hasError = json.contains("\"isError\":true") ||
                               (resp.has("error") && !resp.get("error").isNull());

            boolean ok = expectError ? hasError : !hasError;

            if (ok) {
                System.out.printf("  ✅ PASS  %-35s%n", name);
                if (verbose) System.out.println("     → " + json.substring(0, Math.min(200, json.length())));
                passed++;
            } else {
                System.out.printf("  ❌ FAIL  %-35s%n", name);
                System.out.println("     → " + json);
                failed++;
            }
        } catch (Exception e) {
            System.out.printf("  ❌ ERROR %-35s → %s%n", name, e.getMessage());
            failed++;
        }
    }

    // ── Request Builders ──────────────────────────────────────────────────────

    private String buildInit() {
        return "{\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"initialize\",\"params\":{\"protocolVersion\":\"2024-11-05\"}}";
    }

    private String buildToolsList() {
        return "{\"jsonrpc\":\"2.0\",\"id\":\"2\",\"method\":\"tools/list\",\"params\":{}}";
    }

    private String buildSchedule() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"3\",\"method\":\"tools/call\",\"params\":{\"name\":\"schedule_interview\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"job_id\":\"b1c2d3e4-e5f6-7890-abcd-ef1234567891\"," +
            "\"candidate_id\":\"c1d2e3f4-e5f6-7890-abcd-ef1234567892\"," +
            "\"recruiter_id\":\"d1e2f3a4-e5f6-7890-abcd-ef1234567893\"," +
            "\"scheduled_time\":\"2026-04-15T10:00:00Z\",\"duration_minutes\":30," +
            "\"recording_enabled\":true}}}", TEST_JWT_RECRUITER);
    }

    private String buildGet() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"4\",\"method\":\"tools/call\",\"params\":{\"name\":\"get_interview\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"interview_id\":\"%s\"}}}", TEST_JWT_RECRUITER, INTERVIEW_ID);
    }

    private String buildUpdate() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"5\",\"method\":\"tools/call\",\"params\":{\"name\":\"update_interview\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"interview_id\":\"%s\",\"scheduled_time\":\"2026-04-20T14:00:00Z\"}}}", TEST_JWT_RECRUITER, INTERVIEW_ID);
    }

    private String buildJoin() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"6\",\"method\":\"tools/call\",\"params\":{\"name\":\"join_interview\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"interview_id\":\"%s\",\"participant_type\":\"RECRUITER\"}}}", TEST_JWT_RECRUITER, INTERVIEW_ID);
    }

    private String buildPause() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"7\",\"method\":\"tools/call\",\"params\":{\"name\":\"pause_interview\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"interview_id\":\"%s\"}}}", TEST_JWT_RECRUITER, INTERVIEW_ID);
    }

    private String buildResume() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"8\",\"method\":\"tools/call\",\"params\":{\"name\":\"resume_interview\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"interview_id\":\"%s\"}}}", TEST_JWT_RECRUITER, INTERVIEW_ID);
    }

    private String buildComplete() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"9\",\"method\":\"tools/call\",\"params\":{\"name\":\"complete_interview\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"interview_id\":\"%s\",\"completion_reason\":\"NORMAL\"}}}", TEST_JWT_RECRUITER, INTERVIEW_ID);
    }

    private String buildGetResult() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"10\",\"method\":\"tools/call\",\"params\":{\"name\":\"get_interview_result\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"interview_id\":\"%s\"}}}", TEST_JWT_RECRUITER, INTERVIEW_ID);
    }

    private String buildSubmitFeedback() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"11\",\"method\":\"tools/call\",\"params\":{\"name\":\"submit_feedback\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"interview_id\":\"%s\"," +
            "\"communication_score\":4,\"technical_score\":4,\"cultural_fit_score\":3," +
            "\"comments\":\"Strong candidate, good problem-solving skills.\"," +
            "\"recommendation\":\"HIRE\"}}}", TEST_JWT_RECRUITER, INTERVIEW_ID);
    }

    private String buildGetQuestions() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"12\",\"method\":\"tools/call\",\"params\":{\"name\":\"get_questions\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"interview_id\":\"%s\"}}}", TEST_JWT_RECRUITER, INTERVIEW_ID);
    }

    private String buildLogAnswer() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"13\",\"method\":\"tools/call\",\"params\":{\"name\":\"log_answer\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"interview_id\":\"%s\"," +
            "\"question_id\":\"q1\",\"answer_text\":\"I have 5 years of backend development experience...\",\"duration_seconds\":180}}}", TEST_JWT_CANDIDATE, INTERVIEW_ID);
    }

    private String buildListInterviews() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"14\",\"method\":\"tools/call\",\"params\":{\"name\":\"list_interviews\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"status\":\"SCHEDULED\",\"page\":1,\"page_size\":10}}}", TEST_JWT_RECRUITER);
    }

    private String buildReschedule() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"15\",\"method\":\"tools/call\",\"params\":{\"name\":\"reschedule_interview\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"interview_id\":\"%s\"," +
            "\"new_scheduled_time\":\"2026-04-25T11:00:00Z\",\"reason\":\"NO_SHOW\"}}}", TEST_JWT_RECRUITER, INTERVIEW_ID);
    }

    private String buildConsentCandidate() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"16\",\"method\":\"tools/call\",\"params\":{\"name\":\"grant_recording_consent\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"interview_id\":\"%s\",\"consent_granted\":true}}}", TEST_JWT_CANDIDATE, INTERVIEW_ID);
    }

    private String buildDeleteRecording() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"17\",\"method\":\"tools/call\",\"params\":{\"name\":\"delete_recording\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"interview_id\":\"%s\",\"reason\":\"Privacy request\"}}}", TEST_JWT_RECRUITER, INTERVIEW_ID);
    }

    private String buildAnalytics() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"18\",\"method\":\"tools/call\",\"params\":{\"name\":\"get_analytics\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"date_from\":\"2026-01-01T00:00:00Z\",\"metric_type\":\"ALL\"}}}", TEST_JWT_RECRUITER);
    }

    private String buildNoShow() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"19\",\"method\":\"tools/call\",\"params\":{\"name\":\"detect_no_show\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"interview_id\":\"%s\"}}}", TEST_JWT_RECRUITER, INTERVIEW_ID);
    }

    private String buildHealth() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"20\",\"method\":\"tools/call\",\"params\":{\"name\":\"get_session_health\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"interview_id\":\"%s\"}}}", TEST_JWT_RECRUITER, INTERVIEW_ID);
    }

    // ── Security Test Cases ────────────────────────────────────────────────────

    private String buildMissingJwt() {
        return "{\"jsonrpc\":\"2.0\",\"id\":\"s1\",\"method\":\"tools/call\",\"params\":{\"name\":\"get_interview\",\"arguments\":{" +
            "\"interview_id\":\"" + INTERVIEW_ID + "\"}}}";
    }

    private String buildMalformedJwt() {
        return "{\"jsonrpc\":\"2.0\",\"id\":\"s2\",\"method\":\"tools/call\",\"params\":{\"name\":\"get_interview\",\"arguments\":{" +
            "\"auth_token\":\"not.a.valid\",\"interview_id\":\"" + INTERVIEW_ID + "\"}}}";
    }

    private String buildCandidatePause() {
        return String.format("{\"jsonrpc\":\"2.0\",\"id\":\"s3\",\"method\":\"tools/call\",\"params\":{\"name\":\"pause_interview\",\"arguments\":{" +
            "\"auth_token\":\"%s\",\"interview_id\":\"%s\"}}}", TEST_JWT_CANDIDATE, INTERVIEW_ID);
    }
}
