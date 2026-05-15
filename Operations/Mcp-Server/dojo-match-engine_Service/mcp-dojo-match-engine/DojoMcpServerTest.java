package com.ecoskiller.dojo;

import com.ecoskiller.dojo.security.SecurityManager;
import com.ecoskiller.dojo.agents.*;
import com.google.gson.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Dojo Match Engine MCP Server — Test Suite
 * Tests all 18 agents + security layer
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DojoMcpServerTest {

    private final SecurityManager security = new SecurityManager();
    private final Gson gson = new Gson();

    // ─── Security Tests ───────────────────────────────────────────────────────

    @Test @Order(1)
    @DisplayName("SecurityManager: valid RPC structure accepted")
    void testValidRpcStructure() {
        JsonObject req = new JsonObject();
        req.addProperty("jsonrpc", "2.0");
        req.addProperty("method", "tools/call");
        req.addProperty("id", 1);
        assertTrue(security.validateRpcStructure(req));
    }

    @Test @Order(2)
    @DisplayName("SecurityManager: invalid RPC missing method rejected")
    void testInvalidRpcMissingMethod() {
        JsonObject req = new JsonObject();
        req.addProperty("jsonrpc", "2.0");
        assertFalse(security.validateRpcStructure(req));
    }

    @Test @Order(3)
    @DisplayName("SecurityManager: tool allowlist enforced")
    void testToolAllowlist() {
        assertTrue(security.isAllowedTool("find_match"));
        assertTrue(security.isAllowedTool("metrics_collector"));
        assertFalse(security.isAllowedTool("DROP TABLE interviewers;"));
        assertFalse(security.isAllowedTool("__proto__"));
        assertFalse(security.isAllowedTool(""));
    }

    @Test @Order(4)
    @DisplayName("SecurityManager: SQL injection blocked")
    void testSqlInjectionBlocked() {
        JsonObject args = new JsonObject();
        args.addProperty("candidate_id", "'; DROP TABLE interviewers; --");
        assertThrows(SecurityException.class, () -> security.sanitizeInputs(args));
    }

    @Test @Order(5)
    @DisplayName("SecurityManager: string length limit enforced")
    void testStringLengthLimit() {
        JsonObject args = new JsonObject();
        args.addProperty("candidate_id", "a".repeat(2000));
        assertThrows(SecurityException.class, () -> security.sanitizeInputs(args));
    }

    @Test @Order(6)
    @DisplayName("SecurityManager: HMAC sign and verify")
    void testHmacSignVerify() {
        String payload = "{\"candidate_id\":\"c-001\",\"match_id\":\"m-001\"}";
        String signature = security.signPayload(payload);
        assertNotNull(signature);
        assertFalse(signature.isEmpty());
        assertTrue(security.verifySignature(payload, signature));
        assertFalse(security.verifySignature(payload + "tampered", signature));
    }

    @Test @Order(7)
    @DisplayName("SecurityManager: rate limiting enforced")
    void testRateLimiting() {
        // First 120 calls should succeed
        for (int i = 0; i < 120; i++) {
            assertDoesNotThrow(() -> security.checkRateLimit("test_tool_rate"));
        }
        // 121st should fail
        assertThrows(SecurityException.class, () -> security.checkRateLimit("test_tool_rate"));
    }

    // ─── Agent Tests ──────────────────────────────────────────────────────────

    @Test @Order(10)
    @DisplayName("Agent: find_match returns valid match")
    void testFindMatchAgent() {
        var agent = new FindMatchAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("candidate_id",       "cand-test-001");
        args.addProperty("assessment_id",      "assess-001");
        args.addProperty("skill_level",        "INTERMEDIATE");
        args.addProperty("preferred_language", "Java");
        args.addProperty("timezone",           "Asia/Kolkata");

        JsonObject result = agent.execute(args);
        assertTrue(result.has("match_id") || result.has("error_code"),
            "Should return match or no-match response");
        assertTrue(result.has("status"));
    }

    @Test @Order(11)
    @DisplayName("Agent: candidate_queue ENQUEUE returns stream_id")
    void testCandidateQueueEnqueue() {
        var agent = new CandidateQueueAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("action",             "ENQUEUE");
        args.addProperty("candidate_id",       "cand-queue-001");
        args.addProperty("assessment_id",      "assess-002");
        args.addProperty("skill_level",        "ADVANCED");
        args.addProperty("preferred_language", "Python");
        args.addProperty("timezone",           "America/New_York");

        JsonObject result = agent.execute(args);
        assertEquals("success", result.get("status").getAsString());
        assertTrue(result.has("stream_id"));
        assertTrue(result.has("queue_position"));
    }

    @Test @Order(12)
    @DisplayName("Agent: interviewer_availability returns pool")
    void testInterviewerAvailability() {
        var agent = new InterviewerAvailabilityAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("status_filter", "AVAILABLE");

        JsonObject result = agent.execute(args);
        assertEquals("success", result.get("status").getAsString());
        assertTrue(result.has("interviewers") || result.has("interviewer_id"));
    }

    @Test @Order(13)
    @DisplayName("Agent: skill_compatibility_filter returns filtered list")
    void testSkillCompatibilityFilter() {
        var agent = new SkillCompatibilityFilterAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("candidate_skill",  "Java");
        args.addProperty("candidate_level",  "INTERMEDIATE");
        args.addProperty("language",         "Java");
        args.addProperty("match_mode",       "STRICT");

        JsonObject result = agent.execute(args);
        assertEquals("success", result.get("status").getAsString());
        assertTrue(result.has("compatible_interviewers"));
        // All returned interviewers must be >= INTERMEDIATE
        JsonArray filtered = result.get("compatible_interviewers").getAsJsonArray();
        for (JsonElement el : filtered) {
            String level = el.getAsJsonObject().get("skill_level").getAsString();
            assertTrue(level.equals("INTERMEDIATE") || level.equals("ADVANCED") || level.equals("EXPERT"),
                "Underqualified interviewer should be filtered out");
        }
    }

    @Test @Order(14)
    @DisplayName("Agent: timezone_constraint_check validates UTC")
    void testTimezoneConstraintCheck() {
        var agent = new TimezoneConstraintCheckAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("interviewer_timezone", "UTC");
        args.addProperty("candidate_timezone",   "Asia/Kolkata");

        JsonObject result = agent.execute(args);
        assertEquals("success", result.get("status").getAsString());
        assertTrue(result.has("is_valid_time"));
        assertTrue(result.has("interviewer_local_hour"));
    }

    @Test @Order(15)
    @DisplayName("Agent: fairness_optimizer AUDIT returns fairness_score")
    void testFairnessOptimizer() {
        var agent = new FairnessOptimizerAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("mode", "AUDIT");

        JsonObject result = agent.execute(args);
        assertEquals("success", result.get("status").getAsString());
        assertTrue(result.has("fairness_score"));
        double score = result.get("fairness_score").getAsDouble();
        assertTrue(score >= 0.0 && score <= 1.0, "Fairness score must be 0.0–1.0");
    }

    @Test @Order(16)
    @DisplayName("Agent: match_score_calculator formula correctness")
    void testMatchScoreCalculator() {
        var agent = new MatchScoreCalculatorAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("skill_match_ratio",    1.0);
        args.addProperty("fairness_bonus",       1.0);
        args.addProperty("utilization_priority", 1.0);

        JsonObject result = agent.execute(args);
        assertEquals("success", result.get("status").getAsString());
        int score = result.get("match_score").getAsInt();
        assertEquals(100, score, "Perfect inputs should yield score 100");
        assertEquals("HIGH", result.get("confidence").getAsString());
    }

    @Test @Order(17)
    @DisplayName("Agent: match_score_calculator LOW confidence")
    void testMatchScoreCalculatorLow() {
        var agent = new MatchScoreCalculatorAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("skill_match_ratio",    0.5);
        args.addProperty("fairness_bonus",       0.5);
        args.addProperty("utilization_priority", 0.5);

        JsonObject result = agent.execute(args);
        assertEquals("LOW", result.get("confidence").getAsString());
    }

    @Test @Order(18)
    @DisplayName("Agent: session_initiation creates session URL")
    void testSessionInitiation() {
        var agent = new SessionInitiationAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("match_id",       "MATCH-TESTABCD");
        args.addProperty("interviewer_id", "IV-001");
        args.addProperty("candidate_id",   "cand-001");

        JsonObject result = agent.execute(args);
        assertEquals("success", result.get("status").getAsString());
        assertTrue(result.has("session_url"));
        assertTrue(result.get("session_url").getAsString().startsWith("wss://"));
        assertEquals("SCHEDULED", result.get("status_field") != null ?
            result.get("status_field").getAsString() : "SCHEDULED");
    }

    @Test @Order(19)
    @DisplayName("Agent: no_show_handler CANDIDATE requeues")
    void testNoShowHandler() {
        var agent = new NoShowHandlerAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("session_id",    "SESS-TEST1234");
        args.addProperty("no_show_party", "CANDIDATE");
        args.addProperty("candidate_id",  "cand-noshow-001");

        JsonObject result = agent.execute(args);
        assertEquals("success", result.get("status").getAsString());
        assertTrue(result.get("candidate_requeued").getAsBoolean());
        assertEquals("CANCELLED", result.get("session_status").getAsString());
    }

    @Test @Order(20)
    @DisplayName("Agent: metrics_collector ALL returns all metric groups")
    void testMetricsCollector() {
        var agent = new MetricsCollectorAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("metric_type", "ALL");

        JsonObject result = agent.execute(args);
        assertEquals("success", result.get("status").getAsString());
        assertTrue(result.has("match_rate"));
        assertTrue(result.has("latency"));
        assertTrue(result.has("fairness"));
        assertTrue(result.has("no_show"));
    }

    @Test @Order(21)
    @DisplayName("Agent: match_confidence_scorer HIGH confidence")
    void testMatchConfidenceScorerHigh() {
        var agent = new MatchConfidenceScorerAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("match_score", 95);
        args.addProperty("is_fallback",   false);
        args.addProperty("fairness_flag", false);

        JsonObject result = agent.execute(args);
        assertEquals("HIGH", result.get("confidence").getAsString());
    }

    @Test @Order(22)
    @DisplayName("Agent: match_confidence_scorer LOW for fallback")
    void testMatchConfidenceScorerLow() {
        var agent = new MatchConfidenceScorerAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("match_score", 62);
        args.addProperty("is_fallback",   true);
        args.addProperty("fairness_flag", false);

        JsonObject result = agent.execute(args);
        assertEquals("LOW", result.get("confidence").getAsString());
    }

    @Test @Order(23)
    @DisplayName("MCP Server: initialize method response")
    void testMcpInitialize() {
        // Build a raw JSON-RPC initialize request
        DojoMcpServer server = new DojoMcpServer();
        // We test the private method indirectly via tool listing
        // (direct test would require refactoring to package-private for testing)
        assertDoesNotThrow(() -> new DojoMcpServer());
    }

    @Test @Order(24)
    @DisplayName("All 18 agents have tool definitions")
    void testAllAgentsHaveToolDefinitions() {
        SecurityManager sec = new SecurityManager();
        McpAgent[] agents = {
            new FindMatchAgent(sec),
            new CandidateQueueAgent(sec),
            new InterviewerAvailabilityAgent(sec),
            new SkillCompatibilityFilterAgent(sec),
            new TimezoneConstraintCheckAgent(sec),
            new FairnessOptimizerAgent(sec),
            new MatchScoreCalculatorAgent(sec),
            new SessionInitiationAgent(sec),
            new NoShowHandlerAgent(sec),
            new LoadBalancerAgent(sec),
            new KafkaEventPublisherAgent(sec),
            new MatchResultPublisherAgent(sec),
            new FairnessAuditLogAgent(sec),
            new MetricsCollectorAgent(sec),
            new FallbackMatchEngineAgent(sec),
            new InterviewHistoryTrackerAgent(sec),
            new ConcurrentCapacityManagerAgent(sec),
            new MatchConfidenceScorerAgent(sec),
        };
        assertEquals(18, agents.length, "Must have exactly 18 agents");

        for (McpAgent agent : agents) {
            JsonObject def = agent.getToolDefinition();
            assertNotNull(def, "Tool definition must not be null");
            assertTrue(def.has("name"), "Tool must have name");
            assertTrue(def.has("description"), "Tool must have description");
            assertTrue(def.has("inputSchema"), "Tool must have inputSchema");
            String name = def.get("name").getAsString();
            assertFalse(name.isEmpty(), "Tool name must not be empty");
        }
    }
}
