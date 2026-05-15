package com.ecoskiller.mcp;

import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.SecurityConfig;

import java.io.*;
import java.util.*;

/**
 * Integration test runner for all 16 Application-Service MCP tools.
 *
 * Runs in two modes:
 *   java -cp mcp-application-service.jar com.ecoskiller.mcp.TestAgents
 *   java -cp mcp-application-service.jar com.ecoskiller.mcp.TestAgents --verbose
 *
 * Each test sends a JSON-RPC 2.0 request directly to the ToolRouter
 * (bypassing stdio so tests can run without subprocess).
 */
public class TestAgents {

    // -------------------------------------------------------------------------
    // Dev JWT tokens (HS256, secret=ecoskiller-dev-secret-change-in-prod)
    // Payload: { "sub": "<userId>", "tenant_id": "tenant-001",
    //            "email": "...", "exp": 9999999999,
    //            "realm_access": { "roles": ["<ROLE>"] } }
    // -------------------------------------------------------------------------
    private static final String ADMIN_TOKEN     = buildDevToken("admin-001",     "ADMIN");
    private static final String RECRUITER_TOKEN = buildDevToken("recruiter-001", "RECRUITER");
    private static final String CANDIDATE_TOKEN = buildDevToken("candidate-001", "CANDIDATE");
    private static final String TENANT_ID       = "tenant-001";

    private static int passed = 0;
    private static int failed = 0;
    private static boolean verbose = false;

    // Shared state across tests
    private static String createdAppId = null;

    public static void main(String[] args) {
        verbose = Arrays.asList(args).contains("--verbose");
        SecurityConfig.init();

        System.out.println("========================================");
        System.out.println(" Ecoskiller Application-Service Tests   ");
        System.out.println(" 16 Tools | Tenant: " + TENANT_ID);
        System.out.println("========================================");
        System.out.println();

        // === Group 1: Core CRUD ===
        runTest("1.  submit_application",                test_submitApplication());
        runTest("2.  get_application",                   test_getApplication());
        runTest("3.  list_applications",                 test_listApplications());
        runTest("4.  validate_application_eligibility",  test_validateEligibility());
        runTest("5.  check_duplicate_application",       test_checkDuplicate());

        // === Group 2: Status transitions ===
        runTest("6.  update_application_status → SCREENED", test_updateToScreened());
        runTest("7.  update_application_status → ASSESSED", test_updateToAssessed());

        // === Group 3: Scoring ===
        runTest("8.  process_score_event",               test_processScoreEvent());
        runTest("9.  evaluate_belt_eligibility",         test_evaluateBeltEligibility());

        // === Group 4: Hiring decision ===
        runTest("10. update_application_status → HIRED", test_updateToHired());

        // === Group 5: Audit & Compliance ===
        runTest("11. get_application_audit_log",         test_getAuditLog());
        runTest("12. export_compliance_report",          test_exportComplianceReport());

        // === Group 6: Analytics ===
        runTest("13. get_application_statistics",        test_getStatistics());
        runTest("14. search_applications",               test_searchApplications());

        // === Group 7: Recruiter ===
        runTest("15. get_recruiter_pipeline",            test_getRecruiterPipeline());
        runTest("16. get_assessment_pipeline_status",    test_getAssessmentPipelineStatus());

        // === Group 8: Security checks ===
        runTest("17. Security: missing token → reject",  test_missingToken());
        runTest("18. Security: wrong role → reject",     test_wrongRole());
        runTest("19. Security: SQL injection → reject",  test_sqlInjection());
        runTest("20. Security: duplicate idempotency",   test_duplicateIdempotency());
        runTest("21. State machine: invalid transition", test_invalidTransition());
        runTest("22. Bulk update (3 applications)",      test_bulkUpdate());

        // === Summary ===
        System.out.println();
        System.out.println("========================================");
        System.out.printf("  PASSED: %d  |  FAILED: %d  |  TOTAL: %d%n",
                passed, failed, passed + failed);
        System.out.println("========================================");
        System.exit(failed > 0 ? 1 : 0);
    }

    // =========================================================================
    // Tests
    // =========================================================================

    private static TestResult test_submitApplication() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",      CANDIDATE_TOKEN);
        args.put("tenant_id",       TENANT_ID);
        args.put("job_id",          "job-abc-123");
        args.put("candidate_id",    "candidate-001");
        args.put("assessment_type", "GD");
        args.put("idempotency_key", "idem-001");
        args.put("cover_note",      "I am excited to apply for this role.");

        String result = callTool("submit_application", args);
        boolean ok = result.contains("CREATED") && result.contains("application_id");
        if (ok) {
            // Extract application_id for subsequent tests
            createdAppId = extractField(result, "application_id");
        }
        return new TestResult(ok, result);
    }

    private static TestResult test_getApplication() {
        if (createdAppId == null) return TestResult.skip("No application created");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",     CANDIDATE_TOKEN);
        args.put("tenant_id",      TENANT_ID);
        args.put("application_id", createdAppId);
        String result = callTool("get_application", args);
        return new TestResult(result.contains("\"status\":\"OK\"") || result.contains("status"), result);
    }

    private static TestResult test_listApplications() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token", ADMIN_TOKEN);
        args.put("tenant_id",  TENANT_ID);
        args.put("page_size",  "10");
        String result = callTool("list_applications", args);
        return new TestResult(result.contains("items") && result.contains("total"), result);
    }

    private static TestResult test_validateEligibility() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",   RECRUITER_TOKEN);
        args.put("tenant_id",    TENANT_ID);
        args.put("job_id",       "job-new-999");
        args.put("candidate_id", "candidate-001");
        String result = callTool("validate_application_eligibility", args);
        return new TestResult(result.contains("eligible"), result);
    }

    private static TestResult test_checkDuplicate() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",   RECRUITER_TOKEN);
        args.put("tenant_id",    TENANT_ID);
        args.put("job_id",       "job-abc-123");
        args.put("candidate_id", "candidate-001");
        String result = callTool("check_duplicate_application", args);
        // Should be true (we submitted this combo in test_submitApplication)
        return new TestResult(result.contains("\"duplicate\":true"), result);
    }

    private static TestResult test_updateToScreened() {
        if (createdAppId == null) return TestResult.skip("No application created");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",     RECRUITER_TOKEN);
        args.put("tenant_id",      TENANT_ID);
        args.put("application_id", createdAppId);
        args.put("new_status",     "SCREENED");
        args.put("reason",         "Profile looks strong, advancing to assessment.");
        String result = callTool("update_application_status", args);
        return new TestResult(result.contains("SCREENED"), result);
    }

    private static TestResult test_updateToAssessed() {
        if (createdAppId == null) return TestResult.skip("No application created");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",     RECRUITER_TOKEN);
        args.put("tenant_id",      TENANT_ID);
        args.put("application_id", createdAppId);
        args.put("new_status",     "ASSESSED");
        args.put("reason",         "GD session completed.");
        String result = callTool("update_application_status", args);
        return new TestResult(result.contains("ASSESSED"), result);
    }

    private static TestResult test_processScoreEvent() {
        if (createdAppId == null) return TestResult.skip("No application created");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",       ADMIN_TOKEN);
        args.put("tenant_id",        TENANT_ID);
        args.put("application_id",   createdAppId);
        args.put("session_id",       "session-gd-001");
        args.put("overall_score",    "82.5");
        args.put("dimension_scores", "{\"communication\":85,\"technical\":80,\"leadership\":83}");
        args.put("assessment_type",  "GD");
        String result = callTool("process_score_event", args);
        return new TestResult(result.contains("belt_recommendation") && result.contains("82"), result);
    }

    private static TestResult test_evaluateBeltEligibility() {
        if (createdAppId == null) return TestResult.skip("No application created");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",     ADMIN_TOKEN);
        args.put("tenant_id",      TENANT_ID);
        args.put("application_id", createdAppId);
        args.put("target_belt",    "GOLD");
        String result = callTool("evaluate_belt_eligibility", args);
        return new TestResult(result.contains("recommended_belt"), result);
    }

    private static TestResult test_updateToHired() {
        if (createdAppId == null) return TestResult.skip("No application created");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",     RECRUITER_TOKEN);
        args.put("tenant_id",      TENANT_ID);
        args.put("application_id", createdAppId);
        args.put("new_status",     "HIRED");
        args.put("reason",         "Excellent GD performance. Offer extended.");
        String result = callTool("update_application_status", args);
        return new TestResult(result.contains("HIRED"), result);
    }

    private static TestResult test_getAuditLog() {
        if (createdAppId == null) return TestResult.skip("No application created");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",     ADMIN_TOKEN);
        args.put("tenant_id",      TENANT_ID);
        args.put("application_id", createdAppId);
        String result = callTool("get_application_audit_log", args);
        return new TestResult(result.contains("audit_trail") && result.contains("DPDPA"), result);
    }

    private static TestResult test_exportComplianceReport() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",   ADMIN_TOKEN);
        args.put("tenant_id",    TENANT_ID);
        args.put("from_date",    "2020-01-01");
        args.put("to_date",      "2099-12-31");
        String result = callTool("export_compliance_report", args);
        return new TestResult(result.contains("compliance_standard") && result.contains("DPDPA"), result);
    }

    private static TestResult test_getStatistics() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token", ADMIN_TOKEN);
        args.put("tenant_id",  TENANT_ID);
        String result = callTool("get_application_statistics", args);
        return new TestResult(result.contains("funnel") && result.contains("hire_rate"), result);
    }

    private static TestResult test_searchApplications() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token", ADMIN_TOKEN);
        args.put("tenant_id",  TENANT_ID);
        args.put("query",      "HIRED");
        args.put("page_size",  "5");
        String result = callTool("search_applications", args);
        return new TestResult(result.contains("results") && result.contains("total_hits"), result);
    }

    private static TestResult test_getRecruiterPipeline() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",   RECRUITER_TOKEN);
        args.put("tenant_id",    TENANT_ID);
        args.put("recruiter_id", "recruiter-001");
        String result = callTool("get_recruiter_pipeline", args);
        return new TestResult(result.contains("pipeline") && result.contains("summary"), result);
    }

    private static TestResult test_getAssessmentPipelineStatus() {
        if (createdAppId == null) return TestResult.skip("No application created");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",     ADMIN_TOKEN);
        args.put("tenant_id",      TENANT_ID);
        args.put("application_id", createdAppId);
        String result = callTool("get_assessment_pipeline_status", args);
        return new TestResult(result.contains("stages") && result.contains("progress_pct"), result);
    }

    // =========================================================================
    // Security tests
    // =========================================================================

    private static TestResult test_missingToken() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token", "");
        args.put("tenant_id",  TENANT_ID);
        args.put("job_id",     "job-x");
        args.put("candidate_id", "cand-x");
        args.put("idempotency_key", "idem-missing");
        try {
            String r = callTool("submit_application", args);
            return new TestResult(false, "Should have thrown — got: " + r);
        } catch (SecurityException | IllegalArgumentException e) {
            return new TestResult(true, "Correctly rejected: " + e.getMessage());
        }
    }

    private static TestResult test_wrongRole() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",      CANDIDATE_TOKEN);
        args.put("tenant_id",       TENANT_ID);
        args.put("application_ids", "app-x");
        args.put("new_status",      "SCREENED");
        try {
            String r = callTool("bulk_update_status", args);
            return new TestResult(false, "Should have thrown — got: " + r);
        } catch (SecurityException | IllegalArgumentException e) {
            return new TestResult(true, "Correctly rejected: " + e.getMessage());
        }
    }

    private static TestResult test_sqlInjection() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token", ADMIN_TOKEN);
        args.put("tenant_id",  TENANT_ID);
        args.put("query",      "'; DROP TABLE applications; --");
        try {
            String r = callTool("search_applications", args);
            return new TestResult(false, "Should have thrown — got: " + r);
        } catch (SecurityException | IllegalArgumentException e) {
            return new TestResult(e.getMessage() != null && e.getMessage().toLowerCase().contains("injection"),
                    "Correctly rejected SQL injection: " + e.getMessage());
        }
    }

    private static TestResult test_duplicateIdempotency() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",      CANDIDATE_TOKEN);
        args.put("tenant_id",       TENANT_ID);
        args.put("job_id",          "job-zzz-999");
        args.put("candidate_id",    "candidate-001");
        args.put("assessment_type", "NONE");
        args.put("idempotency_key", "idem-001");  // same key used in test 1
        String result = callTool("submit_application", args);
        return new TestResult(result.contains("DUPLICATE"), result);
    }

    private static TestResult test_invalidTransition() {
        if (createdAppId == null) return new TestResult(false, "No application created");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",     RECRUITER_TOKEN);
        args.put("tenant_id",      TENANT_ID);
        args.put("application_id", createdAppId);
        args.put("new_status",     "REJECTED");
        try {
            String r = callTool("update_application_status", args);
            return new TestResult(false, "Should have rejected HIRED → REJECTED — got: " + r);
        } catch (IllegalStateException | SecurityException e) {
            return new TestResult(true, "Correctly blocked invalid transition: " + e.getMessage());
        }
    }

    private static TestResult test_bulkUpdate() {
        String[] appIds = new String[3];
        for (int i = 0; i < 3; i++) {
            Map<String, Object> args = new LinkedHashMap<>();
            args.put("auth_token",      CANDIDATE_TOKEN);
            args.put("tenant_id",       TENANT_ID);
            args.put("job_id",          "job-bulk-" + i);
            args.put("candidate_id",    "candidate-001");
            args.put("assessment_type", "INTERVIEW");
            args.put("idempotency_key", "bulk-idem-" + System.nanoTime() + "-" + i);
            String result = callTool("submit_application", args);
            appIds[i] = extractField(result, "application_id");
        }
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("auth_token",      RECRUITER_TOKEN);
        args.put("tenant_id",       TENANT_ID);
        args.put("application_ids", String.join(",", appIds));
        args.put("new_status",      "SCREENED");
        args.put("reason",          "Batch screening completed");
        String result = callTool("bulk_update_status", args);
        return new TestResult(result.contains("succeeded") && result.contains("\"succeeded\":3"), result);
    }

    // =========================================================================
    // Infrastructure
    // =========================================================================

    private static String callTool(String toolName, Map<String, Object> args) {
        com.ecoskiller.mcp.tools.ToolRouter router = new com.ecoskiller.mcp.tools.ToolRouter();
        return router.dispatch(toolName, args);
    }

    private static void runTest(String name, TestResult result) {
        String icon = result.passed ? "✅" : (result.skipped ? "⏭ " : "❌");
        System.out.printf("  %s  %-45s %s%n", icon, name, result.skipped ? "SKIPPED" : (result.passed ? "PASS" : "FAIL"));
        if (verbose || !result.passed) {
            String display = result.output.length() > 300
                    ? result.output.substring(0, 300) + "..." : result.output;
            System.out.println("         " + display);
        }
        if (!result.skipped) {
            if (result.passed) passed++; else failed++;
        }
    }

    // =========================================================================
    // Dev JWT builder (HS256 with well-known dev secret)
    // =========================================================================

    private static String buildDevToken(String userId, String role) {
        String header  = b64url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        // exp = 9999999999 (year 2286 — effectively never expires in dev)
        String payload = b64url(
            "{\"sub\":\"" + userId + "\"," +
            "\"tenant_id\":\"tenant-001\"," +
            "\"email\":\"" + userId + "@ecoskiller.dev\"," +
            "\"exp\":9999999999," +
            "\"realm_access\":{\"roles\":[\"" + role + "\"]}}");
        // Signature intentionally absent for dev (STRICT_MODE=false skips verification)
        return header + "." + payload + ".dev-sig";
    }

    private static String b64url(String s) {
        return java.util.Base64.getUrlEncoder().withoutPadding()
                .encodeToString(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    private static String extractField(String json, String field) {
        String search = "\"" + field + "\":\"";
        int idx = json.indexOf(search);
        if (idx < 0) return "unknown-" + UUID.randomUUID();
        int start = idx + search.length();
        int end   = json.indexOf('"', start);
        return end > start ? json.substring(start, end) : "unknown";
    }

    // =========================================================================
    // TestResult
    // =========================================================================

    static class TestResult {
        final boolean passed;
        final boolean skipped;
        final String  output;

        TestResult(boolean passed, String output) {
            this.passed  = passed;
            this.skipped = false;
            this.output  = output != null ? output : "";
        }

        TestResult(boolean passed, String output, boolean skipped) {
            this.passed  = passed;
            this.skipped = skipped;
            this.output  = output != null ? output : "";
        }

        static TestResult skip(String reason) {
            return new TestResult(false, reason, true);
        }
    }
}
