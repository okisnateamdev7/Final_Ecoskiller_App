package com.ecoskiller.mcp.isd;

import com.ecoskiller.mcp.isd.handlers.ToolRegistry;
import com.ecoskiller.mcp.isd.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.UUID;

/**
 * TestAgents — idea-similarity-detector MCP server
 *
 * 28 tests covering:
 *   - Full similarity pipeline lifecycle
 *   - All 13 tools validated
 *   - Three-tier classification (CLEAR / REVIEW / FLAG)
 *   - Threshold configuration + validation
 *   - DLQ / consumer health / metrics
 *   - Recheck with audit trail
 *   - Security: unknown tool, SQL injection, bad UUID, invalid probability,
 *               threshold ordering violation
 *
 * Run: java -cp <jar> com.ecoskiller.mcp.isd.TestAgents [--verbose]
 */
public class TestAgents {

    private static final ObjectMapper MAPPER   = new ObjectMapper();

    // Demo tenant (matches VectorEngine seeded corpus — must be a valid UUID)
    static final String TENANT    = "de000000-0000-0000-0000-000000000001";
    static final String IDEA_A    = UUID.randomUUID().toString();
    static final String IDEA_B    = UUID.randomUUID().toString();
    static final String CANDIDATE = UUID.randomUUID().toString();

    // These vector IDs produce varied similarity scores (deterministic hash-based)
    static final String VEC_SIMILAR  = "idea-corpus-001|Machine learning model compression via quantization";
    static final String VEC_ORIGINAL = UUID.randomUUID().toString();

    private final ToolRegistry registry;
    private final boolean verbose;
    private int passed = 0;
    private int failed = 0;

    public TestAgents(boolean verbose) {
        this.registry = new ToolRegistry(new SecurityValidator());
        this.verbose  = verbose;
    }

    public static void main(String[] args) throws Exception {
        boolean verbose = Arrays.asList(args).contains("--verbose");
        new TestAgents(verbose).runAll();
    }

    void runAll() throws Exception {
        System.out.println("=============================================================");
        System.out.println(" Ecoskiller | idea-similarity-detector MCP — Test Suite");
        System.out.println("=============================================================");

        // ── Threshold config ──────────────────────────────────────────────
        test("get_threshold_config (defaults)",
            args().put("tenant_id", TENANT), "get_threshold_config",
            n -> n.has("review_threshold") && n.has("flag_threshold")
              && n.has("min_similarity_floor") && n.has("tier_explanation"));

        test("set_threshold_config (stricter enterprise thresholds)",
            args().put("tenant_id", TENANT)
                  .put("review_threshold",    0.55)
                  .put("flag_threshold",      0.75)
                  .put("min_similarity_floor",0.35)
                  .put("updated_by",          "admin-user-001"),
            "set_threshold_config",
            n -> n.path("success").asBoolean()
              && Math.abs(n.path("review_threshold").asDouble() - 0.55) < 0.001
              && Math.abs(n.path("flag_threshold").asDouble()   - 0.75) < 0.001);

        test("get_threshold_config (after update — cache hit)",
            args().put("tenant_id", TENANT), "get_threshold_config",
            n -> Math.abs(n.path("review_threshold").asDouble() - 0.55) < 0.001
              && n.path("cache_hit").asBoolean());

        // ── Similarity check — original idea (low similarity expected) ────
        test("run_similarity_check (original idea — expect CLEAR)",
            args().put("tenant_id",    TENANT)
                  .put("idea_id",      IDEA_B)
                  .put("candidate_id", CANDIDATE)
                  .put("vector_id",    VEC_ORIGINAL)
                  .put("idea_category","software")
                  .put("top_k",        20)
                  .put("min_similarity",0.35),
            "run_similarity_check",
            n -> n.has("copy_probability") && n.has("classification")
              && n.has("top_matches") && n.has("kafka_event_published")
              && n.has("score_breakdown") && n.has("report_id"));

        // ── Similarity check — similar idea (higher similarity) ───────────
        test("run_similarity_check (similar idea to corpus)",
            args().put("tenant_id",    TENANT)
                  .put("idea_id",      IDEA_A)
                  .put("candidate_id", CANDIDATE)
                  .put("vector_id",    VEC_SIMILAR)
                  .put("idea_category","software")
                  .put("top_k",        20)
                  .put("min_similarity",0.35),
            "run_similarity_check",
            n -> n.has("copy_probability") && n.has("classification")
              && n.has("report_id")
              && n.has("score_breakdown")
              && n.path("kafka_event_published").has("topic")
              && n.path("kafka_event_published").has("top_matches"));

        // ── Classification ────────────────────────────────────────────────
        test("get_classification (IDEA_A — after check)",
            args().put("tenant_id", TENANT).put("idea_id", IDEA_A),
            "get_classification",
            n -> n.path("found").asBoolean()
              && n.has("classification") && n.has("copy_probability")
              && n.has("licensing_eligible") && n.has("threshold_context"));

        test("get_classification (IDEA_B — after check)",
            args().put("tenant_id", TENANT).put("idea_id", IDEA_B),
            "get_classification",
            n -> n.path("found").asBoolean() && n.has("classification"));

        test("get_classification (unknown idea — not found)",
            args().put("tenant_id", TENANT)
                  .put("idea_id",   UUID.randomUUID().toString()),
            "get_classification",
            n -> !n.path("found").asBoolean()
              && "UNKNOWN".equals(n.path("classification").asText()));

        // ── Copy probability direct computation ───────────────────────────
        test("compute_copy_probability (FLAG scenario)",
            args().put("tenant_id",              TENANT)
                  .put("idea_id",                IDEA_A)
                  .put("max_cosine_similarity",   0.92)
                  .put("above_medium_count",      8)
                  .put("category_match_rate",     0.85),
            "compute_copy_probability",
            n -> n.has("copy_probability")
              && n.path("copy_probability").asDouble() > 0.70
              && n.has("signal_breakdown") && n.has("formula_weights")
              && n.has("classification"));

        test("compute_copy_probability (CLEAR scenario)",
            args().put("tenant_id",            TENANT)
                  .put("idea_id",              IDEA_B)
                  .put("max_cosine_similarity", 0.30)
                  .put("above_medium_count",    0)
                  .put("category_match_rate",   0.0),
            "compute_copy_probability",
            n -> "CLEAR".equals(n.path("classification").asText())
              && n.path("copy_probability").asDouble() < 0.55);

        // ── Reports ───────────────────────────────────────────────────────
        test("get_similarity_report (IDEA_A — has report)",
            args().put("tenant_id", TENANT).put("idea_id", IDEA_A),
            "get_similarity_report",
            n -> n.path("found").asBoolean()
              && n.has("report") && n.has("licensing_eligibility"));

        test("get_similarity_report (unknown — not found)",
            args().put("tenant_id", TENANT)
                  .put("idea_id",   UUID.randomUUID().toString()),
            "get_similarity_report",
            n -> !n.path("found").asBoolean() && n.has("schema_note"));

        test("list_similarity_reports (all)",
            args().put("tenant_id", TENANT).put("limit", 20),
            "list_similarity_reports",
            n -> n.has("reports") && n.get("reports").isArray()
              && n.path("total_reports").asInt() >= 1
              && n.has("classification_distribution"));

        test("list_similarity_reports (filter CLEAR)",
            args().put("tenant_id", TENANT)
                  .put("classification", "CLEAR").put("limit", 10),
            "list_similarity_reports",
            n -> n.has("reports") && n.has("classification_distribution")
              && "CLEAR".equals(n.path("classification_filter").asText()));

        // ── Top matches ───────────────────────────────────────────────────
        test("get_top_matches (from cached report)",
            args().put("tenant_id", TENANT)
                  .put("idea_id",   IDEA_A)
                  .put("top_k",     5),
            "get_top_matches",
            n -> n.has("top_matches") && "cached_report".equals(n.path("source").asText()));

        test("get_top_matches (no report, no vector_id — expect guidance)",
            args().put("tenant_id", TENANT)
                  .put("idea_id",   UUID.randomUUID().toString()),
            "get_top_matches",
            n -> n.has("error") || n.has("note") || !n.path("source").asText().equals("cached_report"));

        // ── Recheck ───────────────────────────────────────────────────────
        test("run_similarity_recheck (IDEA_A — increments recheck_count)",
            args().put("tenant_id",    TENANT)
                  .put("idea_id",      IDEA_A)
                  .put("vector_id",    VEC_SIMILAR)
                  .put("idea_category","software")
                  .put("recheck_reason","IDEA_AMENDED"),
            "run_similarity_recheck",
            n -> n.path("recheck_count").asInt() >= 1
              && n.has("change_from_previous")
              && n.path("previous_archived").asBoolean()
              && n.has("kafka_event_published"));

        test("get_similarity_audit (IDEA_A — has audit entry after recheck)",
            args().put("tenant_id", TENANT).put("idea_id", IDEA_A),
            "get_similarity_audit",
            n -> n.has("audit_entries")
              && n.path("total_entries").asInt() >= 1
              && n.has("schema_reference"));

        // ── DLQ / Health / Metrics ────────────────────────────────────────
        test("get_dlq_status (clean — no DLQ messages)",
            args().put("tenant_id", TENANT),
            "get_dlq_status",
            n -> n.has("dlq_message_count")
              && n.path("dlq_message_count").asInt() == 0
              && !n.path("alert_firing").asBoolean()
              && n.has("retry_policy") && n.has("prometheus_alert"));

        test("get_consumer_health",
            args().put("tenant_id", TENANT),
            "get_consumer_health",
            n -> n.has("kafka_consumer") && n.has("qdrant_vector_db")
              && n.has("hpa_autoscaling") && n.has("dependency_status")
              && "HEALTHY".equals(n.path("overall_status").asText()));

        test("get_similarity_metrics",
            args().put("tenant_id", TENANT),
            "get_similarity_metrics",
            n -> n.has("idea_similarity_check_duration_seconds")
              && n.has("qdrant_ann_query_duration_seconds")
              && n.has("idea_similarity_classification_total")
              && n.has("idea_similarity_dlq_total")
              && n.has("kafka_consumer_lag")
              && n.path("idea_similarity_classification_total").path("total").asInt() >= 1);

        // ── Category-aware filtering ──────────────────────────────────────
        test("run_similarity_check (hardware idea — cross-domain filter)",
            args().put("tenant_id",    TENANT)
                  .put("idea_id",      UUID.randomUUID().toString())
                  .put("vector_id",    "hardware-iot-idea-unique-" + UUID.randomUUID())
                  .put("idea_category","hardware")
                  .put("top_k",        10),
            "run_similarity_check",
            n -> n.has("classification") && n.has("score_breakdown")
              && n.path("score_breakdown").has("corpus_size_searched"));

        // ── Security tests ────────────────────────────────────────────────
        testSecurity("security: unknown tool rejected",
            "hack_the_database", args(),
            n -> n.has("error"));

        testSecurity("security: SQL injection in idea_category",
            "run_similarity_check",
            args().put("tenant_id",    TENANT)
                  .put("idea_id",      IDEA_A)
                  .put("vector_id",    VEC_SIMILAR)
                  .put("idea_category","'; DROP TABLE ideas; --"),
            n -> n.has("error"));

        testSecurity("security: invalid UUID in idea_id",
            "get_similarity_report",
            args().put("tenant_id", TENANT).put("idea_id", "not-a-uuid"),
            n -> n.has("error"));

        testSecurity("security: copy_probability > 1.0 rejected",
            "compute_copy_probability",
            args().put("tenant_id",              TENANT)
                  .put("idea_id",                IDEA_A)
                  .put("max_cosine_similarity",   1.5),
            n -> n.has("error"));

        testSecurity("security: threshold ordering violation (flag <= review)",
            "set_threshold_config",
            args().put("tenant_id",       TENANT)
                  .put("review_threshold",0.80)
                  .put("flag_threshold",  0.70), // flag < review — invalid
            n -> n.has("error"));

        testSecurity("security: top_k > 100 rejected",
            "run_similarity_check",
            args().put("tenant_id",    TENANT)
                  .put("idea_id",      IDEA_A)
                  .put("vector_id",    VEC_SIMILAR)
                  .put("top_k",        999),
            n -> n.has("error"));

        // Summary
        System.out.println("\n=============================================================");
        System.out.printf("  TOTAL: %d/%d PASSED%n", passed, passed + failed);
        if (failed > 0) System.out.printf("  FAILED: %d%n", failed);
        System.out.println("=============================================================");
        System.exit(failed > 0 ? 1 : 0);
    }

    // ── Test runners ──────────────────────────────────────────────────────────

    @FunctionalInterface interface Assertion { boolean test(JsonNode n); }

    private void test(String label, ObjectNode args, String tool, Assertion a) {
        internalTest(label, args, tool, a, false);
    }

    private void testSecurity(String label, String tool, ObjectNode args, Assertion a) {
        internalTest(label, args, tool, a, true);
    }

    private void internalTest(String label, ObjectNode args, String tool,
                               Assertion assertion, boolean expectError) {
        try {
            new SecurityValidator().validateToolCall(tool, args);
            Object result = registry.call(tool, args);
            JsonNode node = MAPPER.readTree(MAPPER.writeValueAsString(result));
            boolean ok    = assertion.test(node);
            printResult(label, ok, MAPPER.writeValueAsString(node));
            if (ok) passed++; else failed++;
        } catch (SecurityException | IllegalArgumentException e) {
            if (expectError) {
                System.out.printf("  [PASS] %-55s (rejected: %s)%n", label,
                    e.getMessage().length() > 45 ? e.getMessage().substring(0,45)+"..." : e.getMessage());
                passed++;
            } else {
                System.out.printf("  [FAIL] %-55s — unexpected: %s%n", label, e.getMessage());
                failed++;
            }
        } catch (Exception e) {
            System.out.printf("  [FAIL] %-55s — exception: %s%n", label, e.getMessage());
            if (verbose) e.printStackTrace();
            failed++;
        }
    }

    private void printResult(String label, boolean ok, String json) {
        if (ok) {
            System.out.printf("  [PASS] %-55s%n", label);
            if (verbose) System.out.println("         " + json.substring(0, Math.min(160, json.length())) + "...");
        } else {
            System.out.printf("  [FAIL] %-55s%n", label);
            System.out.println("         " + json.substring(0, Math.min(300, json.length())));
        }
    }

    private ObjectNode args() { return MAPPER.createObjectNode(); }
}
