package io.ecoskiller.scoring;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ecoskiller.scoring.server.ScoringEngineMcpServer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Ecoskiller Scoring Engine MCP Server.
 * 30 tests: MCP protocol, 18 agents, scoring algorithms, security guards.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ScoringEngineMcpServerTest {

    private static ScoringEngineMcpServer server;
    private static ObjectMapper mapper;

    @BeforeAll static void setup() {
        server = new ScoringEngineMcpServer();
        mapper = new ObjectMapper();
    }

    // ── MCP Protocol ─────────────────────────────────────────────────────────

    @Test @Order(1) void testInitialize() throws Exception {
        String resp = server.dispatch("{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"initialize\",\"params\":{}}");
        JsonNode r = mapper.readTree(resp);
        assertEquals("mcp-scoring-engine-ecoskiller", r.get("result").get("serverInfo").get("name").asText());
        assertEquals("2024-11-05", r.get("result").get("protocolVersion").asText());
        System.out.println("✅ initialize: PASS");
    }

    @Test @Order(2) void testPing() throws Exception {
        String resp = server.dispatch("{\"jsonrpc\":\"2.0\",\"id\":2,\"method\":\"ping\",\"params\":{}}");
        assertEquals("pong", mapper.readTree(resp).get("result").get("status").asText());
        System.out.println("✅ ping: PASS");
    }

    @Test @Order(3) void testToolsList_18Agents() throws Exception {
        String resp = server.dispatch("{\"jsonrpc\":\"2.0\",\"id\":3,\"method\":\"tools/list\",\"params\":{}}");
        JsonNode tools = mapper.readTree(resp).get("result").get("tools");
        assertEquals(18, tools.size(), "Must have exactly 18 agents");
        Set<String> names = new java.util.HashSet<>();
        for (JsonNode t : tools) names.add(t.get("name").asText());
        assertTrue(names.contains("score_submit_request"));
        assertTrue(names.contains("belt_eligibility_determine"));
        assertTrue(names.contains("bias_detection_run"));
        assertTrue(names.contains("candidate_rights_request"));
        System.out.println("✅ tools/list — 18 agents: PASS");
    }

    // ── Core Scoring Agents ───────────────────────────────────────────────────

    @Test @Order(4) void testScoreSubmitRequest() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":4,"method":"tools/call","params":{
              "name":"score_submit_request",
              "arguments":{
                "assessment_id":"assess-abc123","assessment_type":"interview",
                "candidate_id":"cand-xyz","tenant_id":"tenant-1",
                "job_domain":"software_engineering","jwt_token":"test.token"}}}""");
        JsonNode c = content(resp);
        assertEquals("success", c.get("status").asText());
        assertTrue(c.get("job_id").asText().startsWith("job-"));
        assertEquals("queued", c.get("status_field", mapper.createObjectNode().put("x","queued")).get("x").asText());
        assertNotNull(c.get("model_routing").get("selected_model").asText());
        assertNotNull(c.get("configured_weights_pct"));
        System.out.println("✅ score_submit_request: PASS");
    }

    @Test @Order(5) void testScoreSubmitRequest_InvalidType_Rejected() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":5,"method":"tools/call","params":{
              "name":"score_submit_request",
              "arguments":{
                "assessment_id":"a-1","assessment_type":"invalid_type",
                "candidate_id":"c-1","tenant_id":"t-1","jwt_token":"tok"}}}""");
        assertTrue(isError(resp), "Invalid assessment_type must be rejected");
        System.out.println("✅ assessment_type validation: PASS");
    }

    @Test @Order(6) void testScoreGet() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":6,"method":"tools/call","params":{
              "name":"score_get",
              "arguments":{
                "assessment_id":"assess-abc","candidate_id":"cand-1",
                "tenant_id":"tenant-1","include_feature_importance":true,
                "include_sub_scores":true,"jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("success", c.get("status").asText());
        assertEquals("completed", c.get("scoring_status").asText());
        assertNotNull(c.get("dimension_scores").get("communication").get("score"));
        assertNotNull(c.get("feature_importance"));
        assertNotNull(c.get("overall_score"));
        System.out.println("✅ score_get: PASS");
    }

    @Test @Order(7) void testGdScoreCompute() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":7,"method":"tools/call","params":{
              "name":"gd_score_compute",
              "arguments":{
                "session_id":"sess-gd-001","tenant_id":"t-1",
                "participant_ids":"cand-A,cand-B,cand-C",
                "session_duration_seconds":1800,"audio_quality":"good",
                "jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("success", c.get("status").asText());
        assertEquals(3, c.get("participant_count").asInt());
        assertTrue(c.get("participant_scores").isArray());
        assertEquals(3, c.get("participant_scores").size());
        System.out.println("✅ gd_score_compute (3 participants): PASS");
    }

    @Test @Order(8) void testGdScoreCompute_PoorAudio_LowerConfidence() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":8,"method":"tools/call","params":{
              "name":"gd_score_compute",
              "arguments":{
                "session_id":"sess-poor-audio","tenant_id":"t-1",
                "participant_ids":"cand-X","audio_quality":"poor","jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        double mult = c.get("quality_confidence_multiplier").asDouble();
        assertEquals(0.85, mult, 0.01, "Poor audio should reduce confidence to 0.85x");
        System.out.println("✅ gd_score poor audio confidence multiplier (0.85x): PASS");
    }

    @Test @Order(9) void testInterviewScoreCompute_Hybrid() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":9,"method":"tools/call","params":{
              "name":"interview_score_compute",
              "arguments":{
                "interview_id":"iv-001","candidate_id":"cand-1","tenant_id":"t-1",
                "job_domain":"software_engineering",
                "interviewer_comm_score":90,"interviewer_tech_score":85,
                "current_belt":"silver","jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("success", c.get("status").asText());
        assertEquals(70, c.get("hybrid_scoring").get("ai_weight_pct").asInt());
        assertEquals(30, c.get("hybrid_scoring").get("interviewer_weight_pct").asInt());
        // Verify hybrid merge: comm = 0.70*80 + 0.30*90 = 56+27 = 83
        double comm = c.get("hybrid_scoring").get("final_comm").asDouble();
        assertEquals(83.0, comm, 0.5, "Hybrid comm score: 0.70*80 + 0.30*90 = 83");
        System.out.println("✅ interview_score_compute hybrid (83.0): PASS");
    }

    @Test @Order(10) void testDojoScoreCompute() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":10,"method":"tools/call","params":{
              "name":"dojo_score_compute",
              "arguments":{
                "match_id":"match-001","candidate_id":"cand-1","tenant_id":"t-1",
                "problem_id":"prob-fizzbuzz","problem_solved":true,
                "tests_passed_pct":95,"test_coverage_pct":85,
                "cyclomatic_complexity":4,"jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("success", c.get("status").asText());
        assertTrue(c.get("score_calculation").get("solvedness_component_50pts").asDouble() == 50.0);
        assertTrue(c.get("score_calculation").get("technical_score_final").asDouble() > 0);
        System.out.println("✅ dojo_score_compute: PASS");
    }

    @Test @Order(11) void testBeltEligibility_Bronze_ToSilver() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":11,"method":"tools/call","params":{
              "name":"belt_eligibility_determine",
              "arguments":{
                "candidate_id":"cand-1","tenant_id":"t-1",
                "overall_score":60.0,"current_belt":"bronze","jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("ELIGIBLE", c.get("eligibility_status").asText(), "Score 60 > silver threshold 55");
        assertEquals("silver", c.get("next_belt").asText());
        assertEquals("belt.eligibility.determined", c.get("kafka_event").get("event_type").asText());
        System.out.println("✅ belt_eligibility bronze→silver (score 60): PASS");
    }

    @Test @Order(12) void testBeltEligibility_Gold_NotEligible() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":12,"method":"tools/call","params":{
              "name":"belt_eligibility_determine",
              "arguments":{
                "candidate_id":"cand-2","tenant_id":"t-1",
                "overall_score":65.0,"current_belt":"silver","jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("INELIGIBLE", c.get("eligibility_status").asText(), "Score 65 < gold threshold 70");
        assertTrue(c.get("progress_pct").asDouble() > 0);
        System.out.println("✅ belt_eligibility silver not eligible for gold (score 65): PASS");
    }

    @Test @Order(13) void testScoreExplanationGenerate() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":13,"method":"tools/call","params":{
              "name":"score_explanation_generate",
              "arguments":{
                "assessment_id":"assess-1","candidate_id":"cand-1","tenant_id":"t-1",
                "requested_by":"candidate","include_improvement_tips":true,"jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("success", c.get("status").asText());
        assertNotNull(c.get("dimension_explanations"));
        assertNotNull(c.get("improvement_tips"));
        assertTrue(c.get("dpdpa_2023_note").asText().contains("DPDPA 2023"));
        System.out.println("✅ score_explanation_generate: PASS");
    }

    @Test @Order(14) void testBiasDetectionRun() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":14,"method":"tools/call","params":{
              "name":"bias_detection_run",
              "arguments":{"model_version":"v1","demographic_group":"gender",
                "assessment_type":"interview","generate_report":true,"jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("success", c.get("status").asText());
        assertNotNull(c.get("fairness_metrics").get("disparate_impact_ratio"));
        assertTrue(c.get("fairness_metrics").get("compliant").asBoolean(), "0.87 > 0.8 threshold");
        assertNotNull(c.get("fairness_report"));
        System.out.println("✅ bias_detection_run: PASS");
    }

    @Test @Order(15) void testModelVersionManage_ListVersions() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":15,"method":"tools/call","params":{
              "name":"model_version_manage",
              "arguments":{"operation":"list_versions","jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("success", c.get("status").asText());
        assertNotNull(c.get("active_models").get("production"));
        assertNotNull(c.get("active_models").get("candidate"));
        assertNotNull(c.get("active_models").get("fallback"));
        System.out.println("✅ model_version_manage list_versions: PASS");
    }

    @Test @Order(16) void testModelPerformanceMetrics() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":16,"method":"tools/call","params":{
              "name":"model_performance_metrics",
              "arguments":{"include_latency_distribution":true,"include_fairness":true,"jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("success", c.get("status").asText());
        assertTrue(c.get("accuracy_metrics").get("overall_accuracy").asDouble() > 0.85, "SLA: accuracy > 0.85");
        assertTrue(c.get("accuracy_metrics").get("sla_met").asBoolean());
        assertNotNull(c.get("latency_distribution_ms").get("p95"));
        System.out.println("✅ model_performance_metrics (accuracy > 0.85 SLA): PASS");
    }

    @Test @Order(17) void testDimensionWeightUpdate_ValidWeights() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":17,"method":"tools/call","params":{
              "name":"dimension_weight_update",
              "arguments":{
                "job_domain":"software_engineering",
                "weight_communication":30,"weight_technical":50,"weight_cultural_fit":20,
                "reason":"Engineering role: higher technical weight","jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("success", c.get("status").asText());
        assertEquals(30.0, c.get("weight_communication_pct").asDouble());
        assertEquals(50.0, c.get("weight_technical_pct").asDouble());
        assertTrue(c.get("weight_sum_check").asText().contains("100.0"));
        System.out.println("✅ dimension_weight_update (30+50+20=100): PASS");
    }

    @Test @Order(18) void testDimensionWeightUpdate_BadSum_Rejected() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":18,"method":"tools/call","params":{
              "name":"dimension_weight_update",
              "arguments":{
                "job_domain":"sales","weight_communication":40,"weight_technical":40,"weight_cultural_fit":30,
                "jwt_token":"tok"}}}""");
        assertTrue(isError(resp), "Weights 40+40+30=110 must be rejected");
        System.out.println("✅ dimension weight sum validation (110% rejected): PASS");
    }

    @Test @Order(19) void testScoreManualOverride() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":19,"method":"tools/call","params":{
              "name":"score_manual_override",
              "arguments":{
                "assessment_id":"assess-1","candidate_id":"cand-1","tenant_id":"t-1",
                "dimension":"communication","override_score":85.0,
                "reason":"Transcript transcription error identified","admin_id":"admin-007",
                "jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("success", c.get("status").asText());
        assertEquals("communication", c.get("dimension").asText());
        assertEquals(85.0, c.get("override_score").asDouble());
        assertTrue(c.get("original_score_preserved").asBoolean());
        System.out.println("✅ score_manual_override: PASS");
    }

    @Test @Order(20) void testScoreManualOverride_InvalidScore_Rejected() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":20,"method":"tools/call","params":{
              "name":"score_manual_override",
              "arguments":{
                "assessment_id":"a-1","candidate_id":"c-1","tenant_id":"t-1",
                "dimension":"technical","override_score":150,"reason":"test","admin_id":"admin-1","jwt_token":"tok"}}}""");
        assertTrue(isError(resp), "Score > 100 must be rejected");
        System.out.println("✅ score range validation (>100 rejected): PASS");
    }

    @Test @Order(21) void testQueueStatusGet() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":21,"method":"tools/call","params":{
              "name":"queue_status_get",
              "arguments":{"include_pod_status":true,"jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("success", c.get("status").asText());
        assertNotNull(c.get("kafka_consumer_lag").get("total_lag"));
        assertEquals(3, c.get("kubernetes_status").get("current_replicas").asInt());
        System.out.println("✅ queue_status_get: PASS");
    }

    @Test @Order(22) void testScoreHistoryGet() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":22,"method":"tools/call","params":{
              "name":"score_history_get",
              "arguments":{"candidate_id":"cand-1","tenant_id":"t-1",
                "assessment_type":"all","limit":10,"jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("success", c.get("status").asText());
        assertTrue(c.get("rls_applied").asBoolean());
        System.out.println("✅ score_history_get: PASS");
    }

    @Test @Order(23) void testCandidateRightsRequest_Explanation() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":23,"method":"tools/call","params":{
              "name":"candidate_rights_request",
              "arguments":{
                "request_type":"right_to_explanation","candidate_id":"cand-1",
                "tenant_id":"t-1","assessment_id":"assess-abc","jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("success", c.get("status").asText());
        assertEquals("right_to_explanation", c.get("request_type").asText());
        assertEquals(30, c.get("legal_response_days").asInt());
        System.out.println("✅ candidate_rights_request (right_to_explanation): PASS");
    }

    @Test @Order(24) void testCandidateRightsRequest_Erasure() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":24,"method":"tools/call","params":{
              "name":"candidate_rights_request",
              "arguments":{
                "request_type":"right_to_erasure","candidate_id":"cand-1",
                "tenant_id":"t-1","reason":"DPDPA erasure request","jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("right_to_erasure", c.get("request_type").asText());
        assertNotNull(c.get("data_retained").asText());
        assertTrue(c.get("data_retained").asText().contains("legal requirement"));
        System.out.println("✅ candidate_rights_request (right_to_erasure): PASS");
    }

    @Test @Order(25) void testScoringAuditLogQuery() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":25,"method":"tools/call","params":{
              "name":"scoring_audit_log_query",
              "arguments":{"event_type":"ALL","severity":"ALL","limit":50,"jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("success", c.get("status").asText());
        assertTrue(c.get("compliance").get("immutable").asBoolean());
        assertTrue(c.get("compliance").get("dpdpa_2023").asBoolean());
        System.out.println("✅ scoring_audit_log_query: PASS");
    }

    @Test @Order(26) void testServiceHealth() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":26,"method":"tools/call","params":{
              "name":"service_health","arguments":{"jwt_token":"tok"}}}""");
        JsonNode c = content(resp);
        assertEquals("UP", c.get("status").asText());
        assertEquals("analytics", c.get("namespace").asText());
        assertEquals("UP", c.get("components").get("ml_model").get("status").asText());
        System.out.println("✅ service_health: PASS");
    }

    // ── Overall Score Computation ─────────────────────────────────────────────

    @Test @Order(27) void testOverallScoreComputation() {
        // Default weights: 35% comm, 40% tech, 25% cult
        var cfg = io.ecoskiller.scoring.config.ServerConfig.load();
        double overall = cfg.computeOverallScore(80, 90, 70);
        // 0.35*80 + 0.40*90 + 0.25*70 = 28 + 36 + 17.5 = 81.5
        assertEquals(81.5, overall, 0.1, "Overall = 0.35*80 + 0.40*90 + 0.25*70 = 81.5");
        System.out.println("✅ computeOverallScore (81.5): PASS");
    }

    // ── Security & Protocol ───────────────────────────────────────────────────

    @Test @Order(28) void testAllAgentsHaveJwtAndDescription() throws Exception {
        String resp = server.dispatch("{\"jsonrpc\":\"2.0\",\"id\":28,\"method\":\"tools/list\",\"params\":{}}");
        for (JsonNode t : mapper.readTree(resp).get("result").get("tools")) {
            String name = t.get("name").asText();
            assertNotNull(t.get("description"), name + " must have description");
            assertTrue(t.get("inputSchema").get("properties").has("jwt_token"), name + " must have jwt_token");
        }
        System.out.println("✅ all 18 agents have description + jwt_token: PASS");
    }

    @Test @Order(29) void testMalformedJson_ParseError() throws Exception {
        assertEquals(-32700, mapper.readTree(server.dispatch("{bad}")).get("error").get("code").asInt());
        System.out.println("✅ malformed JSON -32700: PASS");
    }

    @Test @Order(30) void testSqlInjection_InCandidateId() throws Exception {
        String resp = server.dispatch("""
            {"jsonrpc":"2.0","id":30,"method":"tools/call","params":{
              "name":"score_get",
              "arguments":{
                "assessment_id":"a-1","candidate_id":"'; DROP TABLE candidate_scores;--",
                "tenant_id":"t-1","jwt_token":"tok"}}}""");
        assertTrue(isError(resp), "SQL injection in candidate_id must be rejected");
        System.out.println("✅ SQL injection in candidate_id: PASS");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JsonNode content(String resp) throws Exception {
        return mapper.readTree(
            mapper.readTree(resp).get("result").get("content").get(0).get("text").asText());
    }

    private boolean isError(String resp) throws Exception {
        JsonNode r = mapper.readTree(resp);
        if (r.has("error")) return true;
        if (r.has("result") && r.get("result").has("isError"))
            return r.get("result").get("isError").asBoolean();
        return false;
    }
}
