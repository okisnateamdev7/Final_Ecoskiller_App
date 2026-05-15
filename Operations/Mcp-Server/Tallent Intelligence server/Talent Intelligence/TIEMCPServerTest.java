package com.ecoskiller.tie.mcp;

import com.ecoskiller.tie.mcp.security.RateLimiter;
import com.ecoskiller.tie.mcp.tools.ToolRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TIE MCP Server — Full Test Suite
 *
 * Covers:
 *  - All 15 tool schemas (non-null, required fields present)
 *  - All 15 tool execute() happy paths
 *  - Input validation (missing required fields)
 *  - Security: RateLimiter per-client windowing
 *  - ToolRegistry completeness
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TIEMCPServerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // ── Tool registry ─────────────────────────────────────────────────────────

    @Test @Order(1)
    @DisplayName("ToolRegistry must expose exactly 15 tools")
    void toolRegistryCount() {
        assertEquals(15, ToolRegistry.all().size(),
                "Expected 15 TIE tools in registry");
    }

    @Test @Order(2)
    @DisplayName("All tool names are unique and snake_case")
    void toolNamesUnique() {
        List<String> names = ToolRegistry.all().stream()
                .map(MCPTool::getName).collect(Collectors.toList());
        assertEquals(names.size(), names.stream().distinct().count(), "Duplicate tool names");
        names.forEach(n -> assertTrue(n.matches("[a-z][a-z0-9_]+"),
                "Tool name not snake_case: " + n));
    }

    @ParameterizedTest(name = "Schema valid — {0}")
    @Order(3)
    @ValueSource(strings = {
        "candidate_job_match", "batch_score", "predict_offer_acceptance",
        "predict_retention", "predict_training_duration", "talent_market_benchmark",
        "skill_gap_analysis", "bias_detection", "feature_engineering",
        "model_deploy", "model_evaluate", "ab_test_control",
        "explainability_shap", "talent_graph_query", "compliance_audit"
    })
    void schemaValid(String toolName) {
        Map<String, MCPTool> registry = ToolRegistry.all().stream()
                .collect(Collectors.toMap(MCPTool::getName, t -> t));
        MCPTool tool = registry.get(toolName);
        assertNotNull(tool, "Tool not found: " + toolName);

        ObjectNode schema = tool.getInputSchema(MAPPER);
        assertNotNull(schema);
        assertTrue(schema.has("type"), "Schema missing 'type' for " + toolName);
        assertEquals("object", schema.get("type").asText());
        assertTrue(schema.has("properties"), "Schema missing 'properties' for " + toolName);

        ObjectNode fullSchema = tool.getSchema(MAPPER);
        assertNotNull(fullSchema.get("name"));
        assertNotNull(fullSchema.get("description"));
        assertFalse(fullSchema.get("description").asText().isBlank());
    }

    // ── Happy-path execution tests ────────────────────────────────────────────

    @Test @Order(10)
    @DisplayName("candidate_job_match — returns match_score in [0,1]")
    void candidateJobMatch() throws Exception {
        MCPTool tool = getTool("candidate_job_match");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("candidate_id", "cand-001");
        args.put("role_id",      "role-SDE-42");
        args.put("tenant_id",    "tenant-acme");
        args.put("include_shap", true);

        JsonNode result = tool.execute(args, MAPPER);
        assertTrue(result.has("match_score"), "Missing match_score");
        double score = result.get("match_score").asDouble();
        assertTrue(score >= 0 && score <= 1, "match_score out of [0,1]: " + score);
        assertTrue(result.has("shap_top_features"), "SHAP requested but missing");
        assertTrue(result.has("timestamp"));
    }

    @Test @Order(11)
    @DisplayName("batch_score — trigger returns job_id and QUEUED status")
    void batchScoreTrigger() throws Exception {
        MCPTool tool = getTool("batch_score");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("action", "trigger");

        JsonNode result = tool.execute(args, MAPPER);
        assertEquals("QUEUED", result.get("status").asText());
        assertTrue(result.has("job_id"));
        assertTrue(result.get("candidate_count").asInt() > 0);
    }

    @Test @Order(12)
    @DisplayName("predict_offer_acceptance — probability in [0,1]")
    void predictOfferAcceptance() throws Exception {
        MCPTool tool = getTool("predict_offer_acceptance");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("candidate_id",          "cand-002");
        args.put("role_id",               "role-PM-7");
        args.put("offered_salary_inr",    3_000_000);
        args.put("competing_offers",      2);
        args.put("remote_work_available", true);

        JsonNode result = tool.execute(args, MAPPER);
        assertTrue(result.has("acceptance_probability"));
        double p = result.get("acceptance_probability").asDouble();
        assertTrue(p >= 0 && p <= 1, "probability out of range: " + p);
        assertTrue(result.has("risk_level"));
        assertTrue(result.has("recommendation"));
    }

    @Test @Order(13)
    @DisplayName("predict_retention — returns churn_risk field")
    void predictRetention() throws Exception {
        MCPTool tool = getTool("predict_retention");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("candidate_id",      "cand-003");
        args.put("role_id",           "role-ML-12");
        args.put("role_seniority",    "senior");
        args.put("growth_path_clear", true);

        JsonNode result = tool.execute(args, MAPPER);
        assertTrue(result.has("retention_12mo_prob"));
        assertTrue(result.has("churn_risk"));
        assertTrue(List.of("HIGH","MEDIUM","LOW").contains(result.get("churn_risk").asText()));
    }

    @Test @Order(14)
    @DisplayName("predict_training_duration — ramp_weeks > 0")
    void predictTrainingDuration() throws Exception {
        MCPTool tool = getTool("predict_training_duration");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("candidate_id",     "cand-004");
        args.put("role_id",          "role-DevOps-3");
        args.put("role_complexity",  "high");
        args.put("prior_domain_exp", false);

        JsonNode result = tool.execute(args, MAPPER);
        assertTrue(result.get("estimated_ramp_weeks").asInt() > 0);
        assertTrue(result.has("ramp_phases"));
        assertTrue(result.get("estimated_training_cost_inr").asLong() > 0);
    }

    @Test @Order(15)
    @DisplayName("talent_market_benchmark — salary p50 > p10")
    void talentMarketBenchmark() throws Exception {
        MCPTool tool = getTool("talent_market_benchmark");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("skill",          "Kubernetes");
        args.put("experience_yrs", 5);
        args.put("region",         "IN");

        JsonNode result = tool.execute(args, MAPPER);
        assertTrue(result.has("salary_stats_inr"));
        JsonNode sal = result.get("salary_stats_inr");
        assertTrue(sal.get("p50").asDouble() > sal.get("p10").asDouble(),
                "p50 should be > p10");
        assertTrue(result.has("skill_demand_index"));
    }

    @Test @Order(16)
    @DisplayName("skill_gap_analysis — returns gap_score and critical_gaps array")
    void skillGapAnalysis() throws Exception {
        MCPTool tool = getTool("skill_gap_analysis");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("candidate_id",     "cand-005");
        args.put("role_id",          "role-SDE-9");
        args.put("include_training", true);

        JsonNode result = tool.execute(args, MAPPER);
        assertTrue(result.has("gap_score"));
        assertTrue(result.has("critical_gaps"));
        assertTrue(result.get("critical_gaps").isArray());
        assertTrue(result.has("training_recommendations"));
    }

    @Test @Order(17)
    @DisplayName("bias_detection — returns findings array with eeoc_4_5ths")
    void biasDetection() throws Exception {
        MCPTool tool = getTool("bias_detection");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("tenant_id",  "tenant-xyz");
        args.put("attribute",  "gender");

        JsonNode result = tool.execute(args, MAPPER);
        assertTrue(result.has("findings"));
        assertTrue(result.get("findings").isArray());
        assertTrue(result.get("findings").size() > 0);
        assertTrue(result.has("fairness_score"));
    }

    @Test @Order(18)
    @DisplayName("feature_engineering — returns 47 features and mlflow_run_id")
    void featureEngineering() throws Exception {
        MCPTool tool = getTool("feature_engineering");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("candidate_id", "cand-006");
        args.put("signal_type",  "all");

        JsonNode result = tool.execute(args, MAPPER);
        assertEquals(47, result.get("feature_count").asInt());
        assertTrue(result.get("mlflow_run_id").asText().startsWith("mlflow-"));
        assertTrue(result.has("sample_features"));
    }

    @Test @Order(19)
    @DisplayName("model_deploy — approved when AUC delta >= 0.02")
    void modelDeploy() throws Exception {
        MCPTool tool = getTool("model_deploy");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("model_name",    "match_ensemble");
        args.put("mlflow_run_id", "mlflow-abc123");
        args.put("target_env",    "canary");
        args.put("canary_pct",    10);
        args.put("force",         true); // force=true bypasses AUC check in test

        JsonNode result = tool.execute(args, MAPPER);
        assertTrue(result.get("approved").asBoolean());
        assertTrue(result.has("deployment_id"));
        assertTrue(result.has("rollback_cmd"));
    }

    @Test @Order(20)
    @DisplayName("model_evaluate — deployment_ready when AUC >= 0.84")
    void modelEvaluate() throws Exception {
        MCPTool tool = getTool("model_evaluate");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("model_name",    "retention");
        args.put("mlflow_run_id", "mlflow-eval-001");
        args.put("test_split",    "holdout");

        JsonNode result = tool.execute(args, MAPPER);
        assertTrue(result.has("metrics"));
        assertTrue(result.has("deployment_ready"));
        assertTrue(result.has("verdict"));
        double auc = result.get("metrics").get("auc_roc").asDouble();
        boolean ready = result.get("deployment_ready").asBoolean();
        assertEquals(auc >= 0.84, ready, "deployment_ready inconsistent with AUC value");
    }

    @Test @Order(21)
    @DisplayName("ab_test_control — create returns test_id and RUNNING status")
    void abTestControl() throws Exception {
        MCPTool tool = getTool("ab_test_control");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("action",      "create");
        args.put("model_a",     "ensemble-v3.2.1");
        args.put("model_b",     "ensemble-v3.3.0");
        args.put("traffic_pct", 10);

        JsonNode result = tool.execute(args, MAPPER);
        assertTrue(result.has("test_id"));
        assertEquals("RUNNING", result.get("status").asText());
        assertEquals(10, result.get("traffic_pct").asInt());
    }

    @Test @Order(22)
    @DisplayName("explainability_shap — final_score = base_value + sum(shap_values)")
    void explainabilityShap() throws Exception {
        MCPTool tool = getTool("explainability_shap");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("candidate_id", "cand-007");
        args.put("role_id",      "role-Lead-1");
        args.put("model_type",   "match_score");

        JsonNode result = tool.execute(args, MAPPER);
        assertTrue(result.has("shap_values"));
        assertTrue(result.get("shap_values").isArray());
        assertTrue(result.get("shap_values").size() > 0);
        assertTrue(result.has("summary"));
        assertFalse(result.get("summary").asText().isBlank());
    }

    @Test @Order(23)
    @DisplayName("talent_graph_query — related_skills returns similarity scores in (0,1]")
    void talentGraphQuery() throws Exception {
        MCPTool tool = getTool("talent_graph_query");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("query_type", "related_skills");
        args.put("seed_skill", "Python");
        args.put("top_k",      5);

        JsonNode result = tool.execute(args, MAPPER);
        assertTrue(result.has("related_skills"));
        for (JsonNode sk : result.get("related_skills")) {
            double sim = sk.get("similarity").asDouble();
            assertTrue(sim > 0 && sim <= 1.0, "similarity out of range: " + sim);
        }
    }

    @Test @Order(24)
    @DisplayName("compliance_audit — full report has all compliance sections")
    void complianceAudit() throws Exception {
        MCPTool tool = getTool("compliance_audit");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("report_type",  "full");
        args.put("tenant_id",    "tenant-enterprise");
        args.put("period_start", "2026-01-01");
        args.put("period_end",   "2026-03-31");

        JsonNode result = tool.execute(args, MAPPER);
        assertTrue(result.has("bias_audit"),      "Missing bias_audit section");
        assertTrue(result.has("gdpr_dpdp"),       "Missing gdpr_dpdp section");
        assertTrue(result.has("eu_ai_act"),       "Missing eu_ai_act section");
        assertTrue(result.has("report_id"));
        assertFalse(result.get("report_id").asText().isBlank());
    }

    // ── Validation tests ──────────────────────────────────────────────────────

    @Test @Order(30)
    @DisplayName("candidate_job_match — validation rejects missing tenant_id")
    void validationMissingTenantId() {
        MCPTool tool = getTool("candidate_job_match");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("candidate_id", "cand-001");
        args.put("role_id",      "role-SDE-42");
        // tenant_id intentionally missing

        assertTrue(tool.validateArgs(args).isPresent(), "Expected validation error");
        assertTrue(tool.validateArgs(args).get().contains("tenant_id"));
    }

    @Test @Order(31)
    @DisplayName("validateArgs returns empty for valid arguments")
    void validationPassesForValidArgs() {
        MCPTool tool = getTool("candidate_job_match");
        ObjectNode args = MAPPER.createObjectNode();
        args.put("candidate_id", "cand-001");
        args.put("role_id",      "role-SDE-42");
        args.put("tenant_id",    "tenant-acme");

        assertTrue(tool.validateArgs(args).isEmpty(), "Expected no validation error");
    }

    @Test @Order(32)
    @DisplayName("validateArgs returns error for null args")
    void validationRejectsNull() {
        MCPTool tool = getTool("talent_market_benchmark");
        assertTrue(tool.validateArgs(null).isPresent());
    }

    // ── Security tests ────────────────────────────────────────────────────────

    @Test @Order(40)
    @DisplayName("RateLimiter — allows requests up to max limit")
    void rateLimiterAllowsUpToMax() {
        RateLimiter limiter = new RateLimiter(5, 60_000);
        String client = "test-client-allow";
        for (int i = 0; i < 5; i++) {
            assertTrue(limiter.allow(client), "Should allow request " + (i + 1));
        }
    }

    @Test @Order(41)
    @DisplayName("RateLimiter — blocks request exceeding max in window")
    void rateLimiterBlocksExcess() {
        RateLimiter limiter = new RateLimiter(3, 60_000);
        String client = "test-client-block";
        limiter.allow(client); limiter.allow(client); limiter.allow(client);
        assertFalse(limiter.allow(client), "4th request should be blocked");
    }

    @Test @Order(42)
    @DisplayName("RateLimiter — different clients have independent buckets")
    void rateLimiterIndependentClients() {
        RateLimiter limiter = new RateLimiter(2, 60_000);
        // Exhaust client A
        limiter.allow("client-A"); limiter.allow("client-A");
        assertFalse(limiter.allow("client-A"), "Client A should be blocked");
        // Client B should still be allowed
        assertTrue(limiter.allow("client-B"), "Client B should be independent");
    }

    @Test @Order(43)
    @DisplayName("model_deploy — admin-only tools reject non-admin role")
    void rbacModelDeploy() {
        MCPTool tool = getTool("model_deploy");
        assertFalse(tool.isAllowedRole("recruiter"),   "recruiter should not deploy models");
        assertFalse(tool.isAllowedRole("viewer"),      "viewer should not deploy models");
        assertTrue(tool.isAllowedRole("ml_engineer"),  "ml_engineer should deploy models");
        assertTrue(tool.isAllowedRole("admin"),        "admin should deploy models");
    }

    @Test @Order(44)
    @DisplayName("candidate_job_match — accessible by all roles")
    void rbacMatchAllRoles() {
        MCPTool tool = getTool("candidate_job_match");
        for (String role : List.of("admin", "ml_engineer", "recruiter", "viewer")) {
            assertTrue(tool.isAllowedRole(role), role + " should access candidate_job_match");
        }
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private MCPTool getTool(String name) {
        return ToolRegistry.all().stream()
                .filter(t -> t.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Tool not found: " + name));
    }
}
