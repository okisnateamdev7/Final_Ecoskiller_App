package com.ecoskiller.mcp;

import com.ecoskiller.mcp.config.ServerConfig;
import com.ecoskiller.mcp.security.JwtValidator;
import com.ecoskiller.mcp.security.RateLimiter;
import com.ecoskiller.mcp.tools.*;
import com.ecoskiller.mcp.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for Skill Evaluation Engine MCP Server.
 * Covers: tool execution, security controls, input validation, edge cases.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SkillEvaluationEngineTest {

    private final ObjectMapper mapper = JsonUtil.createSecureMapper();

    // ---- TOOL TESTS -------------------------------------------------------

    @Test @Order(1)
    @DisplayName("evaluate_session: happy path returns all 8 competency dimensions")
    void testEvaluateSession_happyPath() throws Exception {
        EvaluateSessionTool tool = new EvaluateSessionTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("session_id",      "sess-001");
        args.put("participant_id",  "part-001");
        args.put("tenant_id",       "tenant-abc");
        args.put("assessment_type", "GD");

        JsonNode result = tool.execute(args);
        assertTrue(result.path("success").asBoolean());
        assertEquals("EVALUATING", result.path("status").asText());
        assertNotNull(result.path("evaluation_id").asText());
        JsonNode comp = result.path("initial_competency_scores");
        assertTrue(comp.has("technical_depth"));
        assertTrue(comp.has("communication"));
        assertTrue(comp.has("collaboration"));
        assertTrue(comp.has("innovation"));
        assertEquals(8, comp.size(), "Must have exactly 8 competency dimensions");
    }

    @Test @Order(2)
    @DisplayName("evaluate_session: invalid assessment_type throws IllegalArgumentException")
    void testEvaluateSession_invalidType() {
        EvaluateSessionTool tool = new EvaluateSessionTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("session_id",      "sess-002");
        args.put("participant_id",  "part-002");
        args.put("tenant_id",       "tenant-abc");
        args.put("assessment_type", "INVALID_TYPE");

        assertThrows(IllegalArgumentException.class, () -> tool.execute(args));
    }

    @Test @Order(3)
    @DisplayName("evaluate_session: missing required fields throws IllegalArgumentException")
    void testEvaluateSession_missingFields() {
        EvaluateSessionTool tool = new EvaluateSessionTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("session_id", "sess-003");
        // Missing participant_id, tenant_id, assessment_type

        assertThrows(IllegalArgumentException.class, () -> tool.execute(args));
    }

    @Test @Order(4)
    @DisplayName("evaluate_candidate: weighted aggregation is correct")
    void testEvaluateCandidate_weightedAggregation() throws Exception {
        EvaluateCandidateTool tool = new EvaluateCandidateTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("participant_id",   "part-001");
        args.put("tenant_id",        "tenant-abc");
        args.put("gd_score",         80.0);   // weight 35% -> 28
        args.put("interview_score",  90.0);   // weight 40% -> 36
        args.put("coding_score",     70.0);   // weight 20% -> 14
        args.put("test_score",       60.0);   // weight  5% ->  3
        // total weight = 100%, weighted avg = (80*0.35 + 90*0.40 + 70*0.20 + 60*0.05) / 1.0 = 82

        JsonNode result = tool.execute(args);
        assertTrue(result.path("success").asBoolean());
        double overall = result.path("overall_score").asDouble();
        assertEquals(82.0, overall, 0.5, "Weighted average should be ~82");
        assertEquals(100, result.path("assessment_coverage_pct").asInt());
    }

    @Test @Order(5)
    @DisplayName("evaluate_candidate: score out of range rejected")
    void testEvaluateCandidate_scoreOutOfRange() {
        EvaluateCandidateTool tool = new EvaluateCandidateTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("participant_id", "part-001");
        args.put("tenant_id",      "tenant-abc");
        args.put("gd_score",       150.0); // invalid

        assertThrows(IllegalArgumentException.class, () -> tool.execute(args));
    }

    @Test @Order(6)
    @DisplayName("process_code_submission: perfect score on all tests")
    void testProcessCodeSubmission_perfectScore() throws Exception {
        ProcessCodeSubmissionTool tool = new ProcessCodeSubmissionTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("session_id",       "sess-001");
        args.put("participant_id",   "part-001");
        args.put("tenant_id",        "tenant-abc");
        args.put("language",         "JAVA");
        args.put("test_pass_count",  10);
        args.put("test_total_count", 10);
        args.put("execution_time_ms", 100);
        args.put("has_comments",     true);

        JsonNode result = tool.execute(args);
        assertTrue(result.path("success").asBoolean());
        double techScore = result.path("technical_depth_score").asDouble();
        assertTrue(techScore > 70, "High-quality submission should score >70");
    }

    @Test @Order(7)
    @DisplayName("adjust_difficulty: high score + fast time → INCREASE")
    void testAdjustDifficulty_increase() throws Exception {
        AdjustDifficultyTool tool = new AdjustDifficultyTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("session_id",          "sess-001");
        args.put("participant_id",      "part-001");
        args.put("tenant_id",           "tenant-abc");
        args.put("current_score",       85.0);
        args.put("time_spent_minutes",  3.0);

        JsonNode result = tool.execute(args);
        assertEquals("INCREASE", result.path("recommendation").asText());
    }

    @Test @Order(8)
    @DisplayName("adjust_difficulty: low score + long time → DECREASE")
    void testAdjustDifficulty_decrease() throws Exception {
        AdjustDifficultyTool tool = new AdjustDifficultyTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("session_id",         "sess-001");
        args.put("participant_id",     "part-001");
        args.put("tenant_id",          "tenant-abc");
        args.put("current_score",      35.0);
        args.put("time_spent_minutes", 25.0);

        JsonNode result = tool.execute(args);
        assertEquals("DECREASE", result.path("recommendation").asText());
    }

    @Test @Order(9)
    @DisplayName("detect_bias: large variance → flagged")
    void testDetectBias_flagged() throws Exception {
        DetectBiasTool tool = new DetectBiasTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("session_id",         "sess-001");
        args.put("tenant_id",          "tenant-abc");
        args.put("human_score",        50.0);
        args.put("algorithmic_score",  80.0); // 37.5% variance → exceeds 15% threshold

        JsonNode result = tool.execute(args);
        assertTrue(result.path("bias_flagged").asBoolean(), "Should flag >15% variance");
        assertEquals("SESSION_QUEUED_FOR_HUMAN_REVIEW", result.path("action").asText());
    }

    @Test @Order(10)
    @DisplayName("detect_bias: small variance → not flagged")
    void testDetectBias_notFlagged() throws Exception {
        DetectBiasTool tool = new DetectBiasTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("session_id",        "sess-002");
        args.put("tenant_id",         "tenant-abc");
        args.put("human_score",       78.0);
        args.put("algorithmic_score", 80.0); // 2.5% variance → under threshold

        JsonNode result = tool.execute(args);
        assertFalse(result.path("bias_flagged").asBoolean(), "Small variance should not be flagged");
    }

    @Test @Order(11)
    @DisplayName("update_scoring_weights: weights not summing to 1.0 → rejected")
    void testUpdateScoringWeights_invalidSum() {
        UpdateScoringWeightsTool tool = new UpdateScoringWeightsTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("tenant_id",    "tenant-abc");
        args.put("weights_json", "{\"technical_depth\":0.5,\"communication\":0.1}"); // sum = 0.6

        assertThrows(IllegalArgumentException.class, () -> tool.execute(args));
    }

    @Test @Order(12)
    @DisplayName("update_scoring_weights: valid weights accepted")
    void testUpdateScoringWeights_valid() throws Exception {
        UpdateScoringWeightsTool tool = new UpdateScoringWeightsTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("tenant_id",    "tenant-abc");
        args.put("weights_json",
            "{\"technical_depth\":0.20,\"communication\":0.15,\"problem_solving\":0.15," +
            "\"collaboration\":0.12,\"leadership\":0.12,\"adaptability\":0.10," +
            "\"domain_knowledge\":0.10,\"innovation\":0.06}"); // sum = 1.00

        JsonNode result = tool.execute(args);
        assertTrue(result.path("success").asBoolean());
        assertTrue(result.path("postgres_updated").asBoolean());
    }

    @Test @Order(13)
    @DisplayName("extract_nlp_features: transcript too long → rejected")
    void testExtractNlpFeatures_tooLong() {
        ExtractNlpFeaturesTool tool = new ExtractNlpFeaturesTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("session_id",     "sess-001");
        args.put("participant_id", "part-001");
        args.put("tenant_id",      "tenant-abc");
        args.put("transcript",     "x".repeat(10_001)); // exceeds 10,000 limit

        assertThrows(IllegalArgumentException.class, () -> tool.execute(args));
    }

    @Test @Order(14)
    @DisplayName("health_check: returns UP status for all components")
    void testHealthCheck() throws Exception {
        HealthCheckTool tool = new HealthCheckTool();
        JsonNode result = tool.execute(mapper.createObjectNode());
        assertTrue(result.path("success").asBoolean());
        assertEquals("UP", result.path("status").asText());
        assertEquals("UP", result.path("components").path("postgresql").path("status").asText());
        assertEquals("UP", result.path("components").path("kafka").path("status").asText());
        assertEquals("UP", result.path("components").path("redis").path("status").asText());
    }

    @Test @Order(15)
    @DisplayName("get_metrics: returns SLA compliance and key metrics")
    void testGetMetrics() throws Exception {
        GetMetricsTool tool = new GetMetricsTool();
        JsonNode result = tool.execute(mapper.createObjectNode());
        assertTrue(result.path("success").asBoolean());
        assertTrue(result.path("metrics").has("competency_evaluation_duration_ms_p95"));
        assertTrue(result.path("metrics").has("websocket_active_connections"));
        assertTrue(result.path("sla_compliance_pct").asDouble() > 0);
    }

    // ---- SECURITY TESTS ---------------------------------------------------

    @Test @Order(20)
    @DisplayName("Security: Rate limiter blocks after limit exceeded")
    void testRateLimiter_blocksExcessRequests() {
        RateLimiter limiter = new RateLimiter(5); // 5 req/min for test
        String key = "test-client";

        for (int i = 0; i < 5; i++) {
            assertTrue(limiter.allowRequest(key), "Request " + (i+1) + " should be allowed");
        }
        assertFalse(limiter.allowRequest(key), "6th request should be blocked");
    }

    @Test @Order(21)
    @DisplayName("Security: Rate limiter resets after window")
    void testRateLimiter_resetsAfterWindow() {
        RateLimiter limiter = new RateLimiter(3);
        String key = "test-client-2";

        for (int i = 0; i < 3; i++) limiter.allowRequest(key);
        assertFalse(limiter.allowRequest(key));

        limiter.reset(key); // simulate window reset
        assertTrue(limiter.allowRequest(key), "Should allow after reset");
    }

    @Test @Order(22)
    @DisplayName("Security: JWT validator rejects malformed tokens")
    void testJwtValidator_rejectsMalformed() {
        ServerConfig config = ServerConfig.load();
        JwtValidator validator = new JwtValidator(config);

        assertThrows(SecurityException.class, () -> validator.validate("not.a.jwt"));
        assertThrows(SecurityException.class, () -> validator.validate(""));
        assertThrows(SecurityException.class, () -> validator.validate(null));
    }

    @Test @Order(23)
    @DisplayName("Security: JWT validator rejects alg:none attack")
    void testJwtValidator_rejectsAlgNone() {
        ServerConfig config = ServerConfig.load();
        JwtValidator validator = new JwtValidator(config);

        // Craft a token with alg:none header
        String header  = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"alg\":\"none\",\"typ\":\"JWT\"}".getBytes());
        String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"sub\":\"user1\",\"exp\":9999999999}".getBytes());
        String token   = header + "." + payload + ".";

        assertThrows(SecurityException.class, () -> validator.validate(token),
                "alg:none must be rejected");
    }

    @Test @Order(24)
    @DisplayName("Security: JWT validator rejects expired tokens")
    void testJwtValidator_rejectsExpiredToken() {
        ServerConfig config = ServerConfig.load();
        JwtValidator validator = new JwtValidator(config);

        // Token expired in 2020
        String header  = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"alg\":\"RS256\",\"typ\":\"JWT\"}".getBytes());
        String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"sub\":\"user1\",\"iss\":\"https://auth.ecoskiller.com/realms/test\"," +
                        "\"exp\":1577836800,\"iat\":1577836700}".getBytes());
        String token   = header + "." + payload + ".fakesig";

        assertThrows(SecurityException.class, () -> validator.validate(token),
                "Expired token must be rejected");
    }

    @Test @Order(25)
    @DisplayName("Security: Input sanitization strips control characters")
    void testInputSanitization_stripsControlChars() throws Exception {
        EvaluateSessionTool tool = new EvaluateSessionTool();
        ObjectNode args = mapper.createObjectNode();
        // Attempt to inject null bytes / control chars in session_id
        args.put("session_id",      "sess-\u0000001\u0001\u0002");
        args.put("participant_id",  "part-001");
        args.put("tenant_id",       "tenant-abc");
        args.put("assessment_type", "GD");

        // Should not throw — control chars are stripped, not rejected
        JsonNode result = tool.execute(args);
        assertTrue(result.path("success").asBoolean());
        // The session_id in response should not contain null bytes
        assertFalse(result.path("session_id").asText().contains("\u0000"));
    }

    @Test @Order(26)
    @DisplayName("Security: Field length limit enforced")
    void testInputSanitization_lengthLimit() {
        EvaluateSessionTool tool = new EvaluateSessionTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("session_id",      "x".repeat(5000)); // exceeds 4096 limit
        args.put("participant_id",  "part-001");
        args.put("tenant_id",       "tenant-abc");
        args.put("assessment_type", "GD");

        assertThrows(IllegalArgumentException.class, () -> tool.execute(args));
    }
}
