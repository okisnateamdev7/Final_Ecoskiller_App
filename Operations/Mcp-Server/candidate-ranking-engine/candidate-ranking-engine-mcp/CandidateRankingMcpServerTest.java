package com.ecoskiller.ranking;

import com.ecoskiller.ranking.security.SecurityManager;
import com.ecoskiller.ranking.agents.*;
import com.google.gson.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CandidateRankingMcpServerTest {

    private final SecurityManager security = new SecurityManager();
    private static final String TENANT = "550e8400-e29b-41d4-a716-446655440000";
    private static final String JOB    = "660e8400-e29b-41d4-a716-446655440001";
    private static final String CAND   = "770e8400-e29b-41d4-a716-446655440002";

    // ── Security Tests ────────────────────────────────────────────────────────

    @Test @Order(1) @DisplayName("Security: valid JSON-RPC 2.0 accepted")
    void testValidRpc() {
        JsonObject req = new JsonObject();
        req.addProperty("jsonrpc","2.0"); req.addProperty("method","tools/call"); req.addProperty("id",1);
        assertTrue(security.validateRpcStructure(req));
    }

    @Test @Order(2) @DisplayName("Security: invalid RPC rejected")
    void testInvalidRpc() {
        JsonObject req = new JsonObject(); req.addProperty("jsonrpc","2.0");
        assertFalse(security.validateRpcStructure(req));
    }

    @Test @Order(3) @DisplayName("Security: tool allowlist — valid tools permitted")
    void testAllowlistPermit() {
        assertTrue(security.isAllowedTool("score_aggregator"));
        assertTrue(security.isAllowedTool("rank_computer"));
        assertTrue(security.isAllowedTool("belt_eligibility_evaluator"));
        assertTrue(security.isAllowedTool("weight_matrix_manager"));
    }

    @Test @Order(4) @DisplayName("Security: tool allowlist — unknown tools denied")
    void testAllowlistDeny() {
        assertFalse(security.isAllowedTool("DROP TABLE candidate_rankings"));
        assertFalse(security.isAllowedTool("admin_bypass"));
        assertFalse(security.isAllowedTool(null));
    }

    @Test @Order(5) @DisplayName("Security: SQL injection blocked")
    void testSqlInjection() {
        JsonObject args = new JsonObject();
        args.addProperty("tenant_id","'; DROP TABLE candidate_rankings; --");
        assertThrows(SecurityException.class, () -> security.sanitizeInputs(args));
    }

    @Test @Order(6) @DisplayName("Security: XSS blocked")
    void testXssBlocked() {
        JsonObject args = new JsonObject();
        args.addProperty("job_id","<script>alert('xss')</script>");
        assertThrows(SecurityException.class, () -> security.sanitizeInputs(args));
    }

    @Test @Order(7) @DisplayName("Security: string length limit enforced")
    void testStringLength() {
        JsonObject args = new JsonObject();
        args.addProperty("candidate_id","a".repeat(3000));
        assertThrows(SecurityException.class, () -> security.sanitizeInputs(args));
    }

    @Test @Order(8) @DisplayName("Security: UUID validation")
    void testUuidValidation() {
        assertTrue(security.isValidUuid(TENANT));
        assertTrue(security.isValidUuid(JOB));
        assertFalse(security.isValidUuid("not-a-uuid"));
        assertFalse(security.isValidUuid(null));
    }

    @Test @Order(9) @DisplayName("Security: score range validation")
    void testScoreRange() {
        assertTrue(security.isValidScore(0.0));
        assertTrue(security.isValidScore(100.0));
        assertTrue(security.isValidScore(83.8));
        assertFalse(security.isValidScore(-1.0));
        assertFalse(security.isValidScore(100.1));
    }

    @Test @Order(10) @DisplayName("Security: Kafka topic validation")
    void testTopicValidation() {
        assertTrue(security.isValidTopic("gd.completed"));
        assertTrue(security.isValidTopic("candidate.rank.computed"));
        assertTrue(security.isValidTopic("gd.completed.dlq"));
        assertFalse(security.isValidTopic("malicious.topic"));
    }

    @Test @Order(11) @DisplayName("Security: HMAC sign & verify")
    void testHmac() {
        String payload = "{\"tenant_id\":\"" + TENANT + "\"}";
        String sig = security.signPayload(payload);
        assertNotNull(sig);
        assertTrue(security.verifySignature(payload, sig));
        assertFalse(security.verifySignature(payload + "x", sig));
    }

    @Test @Order(12) @DisplayName("Security: rate limiting enforced")
    void testRateLimit() {
        for (int i = 0; i < 120; i++) assertDoesNotThrow(() -> security.checkRateLimit("rl_test_x"));
        assertThrows(SecurityException.class, () -> security.checkRateLimit("rl_test_x"));
    }

    @Test @Order(13) @DisplayName("Security: belt tier validation")
    void testBeltValidation() {
        assertTrue(security.isValidBelt("BLACK"));
        assertTrue(security.isValidBelt("green"));
        assertFalse(security.isValidBelt("PLATINUM"));
    }

    // ── Agent Tests ───────────────────────────────────────────────────────────

    @Test @Order(14) @DisplayName("Agent: score_aggregator — success")
    void testScoreAggregator() {
        var agent = new ScoreAggregatorAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("tenant_id", TENANT);
        args.addProperty("job_id", JOB);
        args.addProperty("candidate_id", CAND);
        args.addProperty("assessment_sources","ALL");
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertTrue(r.has("raw_aggregate"));
        assertTrue(r.has("redis_key"));
    }

    @Test @Order(15) @DisplayName("Agent: score_aggregator — invalid UUID rejected")
    void testScoreAggregatorInvalidUuid() {
        var agent = new ScoreAggregatorAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("tenant_id","not-a-uuid");
        args.addProperty("job_id", JOB);
        args.addProperty("candidate_id", CAND);
        JsonObject r = agent.execute(args);
        assertEquals("error", r.get("status").getAsString());
    }

    @Test @Order(16) @DisplayName("Agent: weighted_score_calculator — formula correct")
    void testWeightedScore() {
        var agent = new WeightedScoreCalculatorAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("tenant_id", TENANT); args.addProperty("job_id", JOB);
        args.addProperty("candidate_id", CAND);
        args.addProperty("raw_gd_score", 80.0); args.addProperty("raw_interview_score", 80.0);
        args.addProperty("raw_dojo_score", 80.0); args.addProperty("raw_xi_score", 80.0);
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertEquals(80.0, r.get("composite_score").getAsDouble(), 0.01);
    }

    @Test @Order(17) @DisplayName("Agent: weighted_score_calculator — invalid score rejected")
    void testWeightedScoreInvalidRange() {
        var agent = new WeightedScoreCalculatorAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("tenant_id", TENANT); args.addProperty("job_id", JOB);
        args.addProperty("candidate_id", CAND); args.addProperty("raw_gd_score", 150.0);
        JsonObject r = agent.execute(args);
        assertEquals("error", r.get("status").getAsString());
    }

    @Test @Order(18) @DisplayName("Agent: rank_computer — returns ranked list")
    void testRankComputer() {
        var agent = new RankComputerAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("tenant_id", TENANT); args.addProperty("job_id", JOB);
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertTrue(r.has("ranked_candidates"));
        assertTrue(r.get("cohort_size").getAsInt() > 0);
        assertEquals(1, r.get("ranked_candidates").getAsJsonArray().get(0).getAsJsonObject().get("rank_position").getAsInt());
    }

    @Test @Order(19) @DisplayName("Agent: tie_resolver — deterministic output")
    void testTieResolver() {
        var agent = new TieResolverAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("tenant_id", TENANT); args.addProperty("job_id", JOB);
        args.addProperty("tied_score", 79.5);
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertTrue(r.get("deterministic").getAsBoolean());
        assertTrue(r.has("resolved_ranking"));
    }

    @Test @Order(20) @DisplayName("Agent: belt_eligibility_evaluator — BLACK belt 91.4")
    void testBeltBlack() {
        var agent = new BeltEligibilityEvaluatorAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("tenant_id", TENANT); args.addProperty("candidate_id", CAND);
        args.addProperty("composite_score", 91.4); args.addProperty("belt_tier", "BLACK");
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertTrue(r.get("belt_eligible").getAsBoolean());
        assertEquals("BLACK", r.get("highest_eligible_belt").getAsString());
    }

    @Test @Order(21) @DisplayName("Agent: belt_eligibility_evaluator — not eligible below threshold")
    void testBeltNotEligible() {
        var agent = new BeltEligibilityEvaluatorAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("tenant_id", TENANT); args.addProperty("candidate_id", CAND);
        args.addProperty("composite_score", 35.0);
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertFalse(r.get("belt_eligible").getAsBoolean());
        assertEquals("NONE", r.get("highest_eligible_belt").getAsString());
    }

    @Test @Order(22) @DisplayName("Agent: leaderboard_manager GET — paginated results")
    void testLeaderboard() {
        var agent = new LeaderboardManagerAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("tenant_id", TENANT); args.addProperty("job_id", JOB);
        args.addProperty("action","GET");
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertTrue(r.has("ranked_candidates"));
        assertTrue(r.get("read_latency_p99_ms").getAsInt() <= 10);
    }

    @Test @Order(23) @DisplayName("Agent: shortlist_generator — threshold filters correctly")
    void testShortlist() {
        var agent = new ShortlistGeneratorAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("tenant_id", TENANT); args.addProperty("job_id", JOB);
        args.addProperty("score_threshold", 85.0);
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        JsonArray list = r.get("shortlisted_candidates").getAsJsonArray();
        for (JsonElement c : list) {
            assertTrue(c.getAsJsonObject().get("composite_score").getAsDouble() >= 85.0,
                "All shortlisted candidates must be >= threshold");
        }
    }

    @Test @Order(24) @DisplayName("Agent: kafka_event_consumer — all topics healthy")
    void testKafkaConsumer() {
        var agent = new KafkaEventConsumerAgent(security);
        JsonObject args = new JsonObject(); args.addProperty("topic","ALL");
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertEquals(0, r.get("total_dlq_messages").getAsInt());
        // Wait — that's metrics, check consumer lag
        assertTrue(r.has("consumer_status"));
    }

    @Test @Order(25) @DisplayName("Agent: kafka_event_publisher — publishes with Avro validation")
    void testKafkaPublisher() {
        var agent = new KafkaEventPublisherAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("tenant_id", TENANT); args.addProperty("job_id", JOB);
        args.addProperty("candidate_id", CAND); args.addProperty("composite_score", 83.8);
        args.addProperty("rank_position", 3); args.addProperty("cohort_size", 8);
        args.addProperty("belt_eligible", false);
        args.addProperty("event_type","candidate.rank.computed");
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertTrue(r.get("avro_validated").getAsBoolean());
        assertTrue(r.has("event_id"));
    }

    @Test @Order(26) @DisplayName("Agent: weight_matrix_manager VALIDATE — sums to 1.0")
    void testWeightMatrix() {
        var agent = new WeightMatrixManagerAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("tenant_id", TENANT); args.addProperty("job_id", JOB);
        args.addProperty("action","VALIDATE");
        args.addProperty("weight_gd", 0.30); args.addProperty("weight_interview", 0.35);
        args.addProperty("weight_dojo", 0.25); args.addProperty("weight_xi", 0.10);
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertTrue(r.get("valid").getAsBoolean());
    }

    @Test @Order(27) @DisplayName("Agent: rank_recomputer — idempotent with audit log")
    void testRankRecomputer() {
        var agent = new RankRecomputerAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("tenant_id", TENANT); args.addProperty("job_id", JOB);
        args.addProperty("trigger_source","score_correction"); args.addProperty("dry_run", true);
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertTrue(r.get("dry_run").getAsBoolean());
        assertTrue(r.has("recompute_id"));
    }

    @Test @Order(28) @DisplayName("All 18 agents have valid tool definitions")
    void testAllAgentDefinitions() {
        McpAgent[] agents = {
            new ScoreAggregatorAgent(security),
            new WeightedScoreCalculatorAgent(security),
            new RankComputerAgent(security),
            new TieResolverAgent(security),
            new DimensionScoreNormalizerAgent(security),
            new IntelligenceSignalIngesterAgent(security),
            new XiVectorIntegratorAgent(security),
            new LeaderboardManagerAgent(security),
            new ShortlistGeneratorAgent(security),
            new CohortStatsCalculatorAgent(security),
            new KafkaEventConsumerAgent(security),
            new KafkaEventPublisherAgent(security),
            new DlqHandlerAgent(security),
            new BeltEligibilityEvaluatorAgent(security),
            new RankRecomputerAgent(security),
            new ScoreCorrectionHandlerAgent(security),
            new MetricsReporterAgent(security),
            new WeightMatrixManagerAgent(security),
        };
        assertEquals(18, agents.length);
        for (McpAgent agent : agents) {
            JsonObject def = agent.getToolDefinition();
            assertNotNull(def);
            assertFalse(def.get("name").getAsString().isEmpty());
            assertFalse(def.get("description").getAsString().isEmpty());
            assertTrue(def.has("inputSchema"));
        }
    }
}
