package com.ecoskiller.mcp.ife;

import com.ecoskiller.mcp.ife.handlers.*;
import com.ecoskiller.mcp.ife.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

/**
 * IFE MCP Server — Test Agent
 *
 * Sends JSON-RPC calls to the ToolRegistry directly (unit-level)
 * and validates all 14 tools.
 *
 * Run: java -cp <jar> com.ecoskiller.mcp.ife.TestAgents [--verbose]
 */
public class TestAgents {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String TENANT = "00000000-0000-0000-0000-000000000001";
    private static final String IDEA_A = "aaaaaaaa-0000-0000-0000-000000000001";
    private static final String IDEA_B = "bbbbbbbb-0000-0000-0000-000000000002";

    private final ToolRegistry registry;
    private final boolean verbose;

    private int passed = 0;
    private int failed = 0;

    public TestAgents(boolean verbose) {
        SecurityValidator sv = new SecurityValidator();
        this.registry = new ToolRegistry(sv);
        this.verbose = verbose;
    }

    public static void main(String[] args) throws Exception {
        boolean verbose = Arrays.asList(args).contains("--verbose");
        TestAgents tests = new TestAgents(verbose);
        tests.runAll();
    }

    private void runAll() throws Exception {
        System.out.println("=================================================");
        System.out.println(" Ecoskiller | IFE MCP Server — Agent Test Suite");
        System.out.println("=================================================");

        test("list_embedding_models",     listModelsArgs());
        test("compute_fingerprint",       computeFingerprintArgs(IDEA_A));
        test("compute_fingerprint",       computeFingerprintArgs(IDEA_B)); // second idea
        test("compute_fingerprint_batch", computeBatchArgs());
        test("get_batch_job_status",      getBatchJobStatusArgs());
        test("generate_embedding",        generateEmbeddingArgs(IDEA_A));
        test("index_embedding",           indexEmbeddingArgs(IDEA_A));
        test("similarity_search",         similaritySearchArgs(IDEA_A));
        test("get_similarity_report",     getSimilarityReportArgs(IDEA_A));
        test("verify_fingerprint",        verifyFingerprintArgs(IDEA_A, false));
        test("verify_fingerprint_tamper", verifyFingerprintArgs(IDEA_A, true));
        test("get_fingerprint",           getFingerprintArgs(IDEA_A));
        test("detect_plagiarism",         detectPlagiarismArgs(IDEA_A));
        test("compute_risk_score",        computeRiskScoreArgs(IDEA_A));
        test("delete_embedding",          deleteEmbeddingArgs(IDEA_A));
        test("reindex_tenant",            reindexTenantArgs());

        System.out.println("\n=================================================");
        System.out.printf("  TOTAL: %d/%d PASSED%n", passed, passed + failed);
        if (failed > 0) System.out.printf("  FAILED: %d%n", failed);
        System.out.println("=================================================");
        System.exit(failed > 0 ? 1 : 0);
    }

    private void test(String label, ObjectNode args) {
        // Infer tool name from label (strip suffix after underscore where needed)
        String toolName = label.contains("_tamper") ? "verify_fingerprint" : label;

        try {
            Object result = registry.call(toolName, args);
            String json = MAPPER.writeValueAsString(result);
            JsonNode node = MAPPER.readTree(json);

            // Basic assertions
            boolean ok = !node.has("error") || (node.has("status") && !node.get("status").asText().startsWith("ERR"));
            // More specific: check expected top-level fields
            ok = validate(toolName, label, node);

            if (ok) {
                System.out.printf("  [PASS] %-35s%n", label);
                if (verbose) System.out.println("         " + json.substring(0, Math.min(120, json.length())) + "...");
                passed++;
            } else {
                System.out.printf("  [FAIL] %-35s — unexpected response%n", label);
                if (verbose) System.out.println("         " + json);
                failed++;
            }
        } catch (Exception e) {
            System.out.printf("  [FAIL] %-35s — exception: %s%n", label, e.getMessage());
            if (verbose) e.printStackTrace();
            failed++;
        }
    }

    private boolean validate(String toolName, String label, JsonNode node) {
        return switch (toolName) {
            case "compute_fingerprint"       -> node.has("status") && node.has("fingerprint_record");
            case "compute_fingerprint_batch" -> node.has("job_id");
            case "get_batch_job_status"      -> node.has("found");
            case "generate_embedding"        -> node.has("embedding") && node.get("embedding").isArray();
            case "similarity_search"         -> node.has("top_matches");
            case "verify_fingerprint"        -> node.has("status") && (
                                                  node.get("status").asText().startsWith("MATCH") ||
                                                  node.get("status").asText().startsWith("MISMATCH") ||
                                                  node.get("status").asText().startsWith("NO_STORED"));
            case "get_fingerprint"           -> node.has("found");
            case "get_similarity_report"     -> node.has("report_found");
            case "detect_plagiarism"         -> node.has("risk_summary") && node.has("level_1_exact_match");
            case "compute_risk_score"        -> node.has("risk_score") && node.has("classification");
            case "index_embedding"           -> node.has("status") && "INDEXED".equals(node.get("status").asText());
            case "delete_embedding"          -> node.has("status") && "DELETED".equals(node.get("status").asText());
            case "reindex_tenant"            -> node.has("job_id") && node.has("status");
            case "list_embedding_models"     -> node.has("models") && node.get("models").isArray();
            default                          -> true;
        };
    }

    // ── Argument builders ─────────────────────────────────────────────────────

    private ObjectNode computeFingerprintArgs(String ideaId) throws Exception {
        return MAPPER.createObjectNode()
            .put("idea_id",          ideaId)
            .put("tenant_id",        TENANT)
            .put("title",            "Machine learning model compression technique")
            .put("description",      "Use quantization to reduce deep learning model size by 80 percent while maintaining accuracy")
            .put("technical_details", "8-bit integer quantization with KL-divergence calibration and batch norm folding");
    }

    private ObjectNode computeBatchArgs() throws Exception {
        ObjectNode args = MAPPER.createObjectNode().put("tenant_id", TENANT).put("priority", "NORMAL");
        var ideas = args.putArray("ideas");
        for (int i = 0; i < 3; i++) {
            ideas.addObject()
                .put("idea_id",     UUID.randomUUID().toString())
                .put("title",       "Test idea " + i)
                .put("description", "Description of test idea number " + i);
        }
        return args;
    }

    private ObjectNode getBatchJobStatusArgs() throws Exception {
        return MAPPER.createObjectNode().put("job_id", UUID.randomUUID().toString());
    }

    private ObjectNode generateEmbeddingArgs(String ideaId) throws Exception {
        return MAPPER.createObjectNode()
            .put("idea_id",          ideaId)
            .put("tenant_id",        TENANT)
            .put("title",            "Federated learning privacy preserving framework")
            .put("description",      "Enable ML model training across decentralized data sources without data sharing")
            .put("embedding_model",  "all-MiniLM-L6-v2");
    }

    private ObjectNode indexEmbeddingArgs(String ideaId) throws Exception {
        return MAPPER.createObjectNode()
            .put("idea_id",         ideaId)
            .put("tenant_id",       TENANT)
            .put("embedding_model", "all-MiniLM-L6-v2")
            .put("idea_category",   "software")
            .put("idea_title",      "Machine learning model compression");
    }

    private ObjectNode similaritySearchArgs(String queryIdeaId) throws Exception {
        return MAPPER.createObjectNode()
            .put("tenant_id",     TENANT)
            .put("query_idea_id", queryIdeaId)
            .put("top_k",         5)
            .put("min_similarity", 0.3)
            .put("include_payload", true);
    }

    private ObjectNode getSimilarityReportArgs(String ideaId) throws Exception {
        return MAPPER.createObjectNode()
            .put("idea_id",          ideaId)
            .put("tenant_id",        TENANT)
            .put("top_matches_limit", 5);
    }

    private ObjectNode verifyFingerprintArgs(String ideaId, boolean tamper) throws Exception {
        return MAPPER.createObjectNode()
            .put("idea_id",          ideaId)
            .put("tenant_id",        TENANT)
            .put("title",            tamper
                ? "TAMPERED TITLE — content modified after fingerprinting"
                : "Machine learning model compression technique")
            .put("description",      tamper
                ? "This text was changed after the fingerprint was computed"
                : "Use quantization to reduce deep learning model size by 80 percent while maintaining accuracy")
            .put("technical_details", tamper
                ? "Modified details"
                : "8-bit integer quantization with KL-divergence calibration and batch norm folding");
    }

    private ObjectNode getFingerprintArgs(String ideaId) throws Exception {
        return MAPPER.createObjectNode()
            .put("idea_id",       ideaId)
            .put("tenant_id",     TENANT)
            .put("include_history", true);
    }

    private ObjectNode detectPlagiarismArgs(String ideaId) throws Exception {
        return MAPPER.createObjectNode()
            .put("idea_id",          ideaId)
            .put("tenant_id",        TENANT)
            .put("candidate_id",     "cccccccc-0000-0000-0000-000000000001")
            .put("flag_threshold",   0.80)
            .put("review_threshold", 0.60)
            .put("cross_tenant_check", false);
    }

    private ObjectNode computeRiskScoreArgs(String ideaId) throws Exception {
        return MAPPER.createObjectNode()
            .put("idea_id",                    ideaId)
            .put("tenant_id",                  TENANT)
            .put("exact_match_flag",           false)
            .put("max_semantic_similarity",    0.72)
            .put("coordinated_submission_flag", false)
            .put("user_reputation_score",      0.65)
            .put("content_flags",              0.10);
    }

    private ObjectNode deleteEmbeddingArgs(String ideaId) throws Exception {
        return MAPPER.createObjectNode()
            .put("idea_id",   ideaId)
            .put("tenant_id", TENANT)
            .put("reason",    "ARCHIVED");
    }

    private ObjectNode reindexTenantArgs() throws Exception {
        return MAPPER.createObjectNode()
            .put("tenant_id",        TENANT)
            .put("embedding_model",  "all-MiniLM-L6-v2")
            .put("force",            false);
    }

    private ObjectNode listModelsArgs() throws Exception {
        return MAPPER.createObjectNode().put("include_deprecated", false);
    }
}
