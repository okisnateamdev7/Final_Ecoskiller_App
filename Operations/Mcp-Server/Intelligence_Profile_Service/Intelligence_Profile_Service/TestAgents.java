package com.ecoskiller.mcp.ips;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

/**
 * TestAgents — quick pass/fail test for all 15 IPS MCP tools.
 *
 * Usage:
 *   java -cp target/mcp-ips.jar com.ecoskiller.mcp.ips.TestAgents
 *   java -cp target/mcp-ips.jar com.ecoskiller.mcp.ips.TestAgents --verbose
 *
 * The server process is spawned as a child process communicating via stdio.
 * Each test sends a JSON-RPC request and validates the response.
 */
public class TestAgents {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String TENANT_ID = "7f3a9b2c-1234-4567-89ab-000000000001";
    private static final String USER_ID   = "a1b2c3d4-5678-90ab-cdef-111111111111";

    // JWT with recruiter + ml_system roles — base64url encoded
    // Payload: {"sub":"test-user","tenantId":"<TENANT_ID>","roles":["admin","recruiter","ml_system"],"exp":9999999999}
    private static final String TEST_JWT =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
        ".eyJzdWIiOiJ0ZXN0LXVzZXIiLCJ0ZW5hbnRJZCI6" +
        "IjdmM2E5YjJjLTEyMzQtNDU2Ny04OWFiLTAwMDAwMDAwMDAwMSIsInJvbGVzIjpbImFkbWluIiwicmVjcnVpdGVyIiwibWxfc3lzdGVtIl0sImV4cCI6OTk5OTk5OTk5OX0" +
        ".placeholder-sig";

    static boolean verbose = false;

    public static void main(String[] args) throws Exception {
        verbose = Arrays.asList(args).contains("--verbose");

        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║   Intelligence Profile Service — MCP Agent Tests         ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        List<TestCase> tests = buildTestCases();
        int passed = 0, failed = 0;

        for (TestCase tc : tests) {
            String result = runTest(tc);
            boolean ok = result.equals("PASS");
            if (ok) passed++; else failed++;
            System.out.printf("  [%s] %-40s %s%n",
                ok ? "✓" : "✗", tc.toolName, ok ? "" : "← " + result);
            if (verbose) System.out.println();
        }

        System.out.println();
        System.out.println("─".repeat(60));
        System.out.printf("  Results: %d/%d passed%n", passed, tests.size());
        if (failed > 0) {
            System.out.println("  SOME TESTS FAILED");
            System.exit(1);
        } else {
            System.out.println("  ALL TESTS PASSED ✓");
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Test case definitions
    // ─────────────────────────────────────────────────────────────────────

    private static List<TestCase> buildTestCases() {
        List<TestCase> tests = new ArrayList<>();

        Map<String, Object> auth = Map.of("token", TEST_JWT);

        // 1. get_intelligence_profile
        tests.add(new TestCase("get_intelligence_profile",
            Map.of("_auth", auth, "userId", USER_ID, "tenantId", TENANT_ID, "include", "all"),
            "intelligenceVectors"));

        // 2. get_skill_vectors
        tests.add(new TestCase("get_skill_vectors",
            Map.of("_auth", auth, "userId", USER_ID, "tenantId", TENANT_ID),
            "skills"));

        // 3. search_candidates
        tests.add(new TestCase("search_candidates",
            Map.of("_auth", auth, "tenantId", TENANT_ID,
                "skills", List.of("Python", "Kubernetes"),
                "skillMinSimilarity", 0.75, "limit", 10),
            "results"));

        // 4. get_peer_benchmarks
        tests.add(new TestCase("get_peer_benchmarks",
            Map.of("_auth", auth, "tenantId", TENANT_ID, "skillName", "Python"),
            "skillBenchmark"));

        // 5. get_risk_assessment
        tests.add(new TestCase("get_risk_assessment",
            Map.of("_auth", auth, "userId", USER_ID, "tenantId", TENANT_ID),
            "churnRisk"));

        // 6. process_assessment_event
        tests.add(new TestCase("process_assessment_event",
            Map.of("_auth", auth, "userId", USER_ID, "tenantId", TENANT_ID,
                "assessmentType", "coding_challenge",
                "scores", Map.of("cognitive", 0.88, "domain", 0.82, "speed", 0.76, "correctness", 0.91),
                "durationSeconds", 3600,
                "idempotencyKey", UUID.randomUUID().toString()),
            "vectorDeltas"));

        // 7. process_skill_assessment
        tests.add(new TestCase("process_skill_assessment",
            Map.of("_auth", auth, "userId", USER_ID, "tenantId", TENANT_ID,
                "skillName", "Python", "proficiencyLevel", 5,
                "confidenceScore", 0.92,
                "idempotencyKey", UUID.randomUUID().toString()),
            "embeddingComputed"));

        // 8. process_gd_discussion
        tests.add(new TestCase("process_gd_discussion",
            Map.of("_auth", auth, "userId", USER_ID, "tenantId", TENANT_ID,
                "sessionId", UUID.randomUUID().toString(),
                "speechCount", 8, "collaborationScore", 0.82,
                "ideaQuality", 0.75, "sentimentScore", 0.3,
                "idempotencyKey", UUID.randomUUID().toString()),
            "vectorDeltas"));

        // 9. process_learning_interaction
        tests.add(new TestCase("process_learning_interaction",
            Map.of("_auth", auth, "userId", USER_ID, "tenantId", TENANT_ID,
                "resourceId", UUID.randomUUID().toString(),
                "resourceType", "course",
                "timeSpentMinutes", 90, "completionPercentage", 85.0,
                "quizScore", 0.88,
                "idempotencyKey", UUID.randomUUID().toString()),
            "updatedLearningAgilityVector"));

        // 10. compute_intelligence_dna
        tests.add(new TestCase("compute_intelligence_dna",
            Map.of("_auth", auth, "userId", USER_ID, "tenantId", TENANT_ID,
                "forceFullRecompute", false),
            "intelligenceDNA"));

        // 11. detect_profile_anomaly
        tests.add(new TestCase("detect_profile_anomaly",
            Map.of("_auth", auth, "userId", USER_ID, "tenantId", TENANT_ID,
                "sensitivityThreshold", 0.3),
            "anomalyScore"));

        // 12. recalculate_vectors
        tests.add(new TestCase("recalculate_vectors",
            Map.of("_auth", auth, "userId", USER_ID, "tenantId", TENANT_ID),
            "recomputedIntelligenceDNA"));

        // 13. get_fairness_audit
        tests.add(new TestCase("get_fairness_audit",
            Map.of("_auth", auth, "tenantId", TENANT_ID,
                "fromDate", "2025-01-01", "toDate", "2025-03-01",
                "demographic", "all"),
            "disparateImpactRatio"));

        // 14. rebuild_profiles  (tenant-scoped, confirm=true)
        tests.add(new TestCase("rebuild_profiles",
            Map.of("_auth", auth, "tenantId", TENANT_ID, "confirm", true),
            "jobId"));

        // 15. get_profile_history
        tests.add(new TestCase("get_profile_history",
            Map.of("_auth", auth, "userId", USER_ID, "tenantId", TENANT_ID,
                "limit", 5),
            "history"));

        return tests;
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Test runner (direct method call — no subprocess needed for unit test)
    // ─────────────────────────────────────────────────────────────────────

    private static String runTest(TestCase tc) {
        try {
            // Build JSON-RPC request
            Map<String, Object> req = new LinkedHashMap<>();
            req.put("jsonrpc", "2.0");
            req.put("id",      1);
            req.put("method",  "tools/call");
            req.put("params",  Map.of("name", tc.toolName, "arguments", tc.arguments));

            String reqJson  = MAPPER.writeValueAsString(req);
            if (verbose) System.out.println("  → " + reqJson);

            // Send to server via pipe simulation
            String respJson = callServerDirect(reqJson);
            if (verbose) System.out.println("  ← " + respJson);

            JsonNode resp = MAPPER.readTree(respJson);

            if (resp.has("error")) {
                return "ERROR: " + resp.path("error").path("message").asText();
            }

            // Validate expected field exists in response
            String contentText = resp.path("result").path("content")
                                     .path(0).path("text").asText("{}");
            JsonNode data = MAPPER.readTree(contentText);
            JsonNode inner = data.path("data");

            if (!inner.has(tc.expectedField)) {
                // Try top-level too
                if (!data.has(tc.expectedField)) {
                    return "MISSING_FIELD: " + tc.expectedField;
                }
            }

            return "PASS";

        } catch (Exception e) {
            return "EXCEPTION: " + e.getMessage();
        }
    }

    /**
     * Calls the server by piping the JSON through its stdin handling directly.
     * Avoids spawning a subprocess for unit tests.
     */
    private static String callServerDirect(String reqJson) throws Exception {
        com.ecoskiller.mcp.ips.server.McpServer server =
            new com.ecoskiller.mcp.ips.server.McpServer();

        // Use reflection to call handleMessage since it's private
        java.lang.reflect.Method m = server.getClass()
            .getDeclaredMethod("handleMessage", String.class);
        m.setAccessible(true);
        return (String) m.invoke(server, reqJson);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  TestCase value object
    // ─────────────────────────────────────────────────────────────────────

    static class TestCase {
        final String toolName;
        final Map<String, Object> arguments;
        final String expectedField;

        TestCase(String toolName, Map<String, Object> arguments, String expectedField) {
            this.toolName      = toolName;
            this.arguments     = arguments;
            this.expectedField = expectedField;
        }
    }
}
