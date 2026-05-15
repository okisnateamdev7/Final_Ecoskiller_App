package com.ecoskiller.mcp.its;

import com.ecoskiller.mcp.its.handlers.ToolRegistry;
import com.ecoskiller.mcp.its.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;

/**
 * ITS MCP Server — Test Agent
 *
 * Validates all 12 tools against the ToolRegistry directly (unit-level).
 * Run: java -cp <jar> com.ecoskiller.mcp.its.TestAgents [--verbose]
 *
 * Test flow is realistic — it follows the actual idea lifecycle:
 *   1. list models / check rate limit (pre-flight)
 *   2. submit_idea
 *   3. get_timestamp_proof
 *   4. verify_idea_integrity (MATCH)
 *   5. verify_idea_integrity (MISMATCH — tamper simulation)
 *   6. verify_minio_object
 *   7. get_idea_ownership
 *   8. list_idea_versions
 *   9. get_submission_status
 *  10. revise_idea
 *  11. set_visibility
 *  12. get_submission_audit
 *  13. archive_idea
 *  14. Security: unknown tool rejection
 *  15. Security: injection payload rejection
 *  16. Security: bad UUID rejection
 */
public class TestAgents {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String TENANT    = "aaaaaaaa-1111-1111-1111-aaaaaaaaaaaa";
    private static final String CANDIDATE = "bbbbbbbb-2222-2222-2222-bbbbbbbbbbbb";

    private final ToolRegistry registry;
    private final boolean verbose;

    private int passed = 0;
    private int failed = 0;

    // Shared state across tests (idea_id set after submit)
    private String ideaId = null;

    public TestAgents(boolean verbose) {
        SecurityValidator sv = new SecurityValidator();
        this.registry = new ToolRegistry(sv);
        this.verbose  = verbose;
    }

    public static void main(String[] args) throws Exception {
        boolean verbose = Arrays.asList(args).contains("--verbose");
        TestAgents t = new TestAgents(verbose);
        t.runAll();
    }

    void runAll() throws Exception {
        System.out.println("=======================================================");
        System.out.println(" Ecoskiller | idea-timestamp-service MCP — Test Suite");
        System.out.println("=======================================================");

        // ── Pre-flight ────────────────────────────────────────────────────
        test("check_rate_limit (pre-submit)",
            args().put("tenant_id", TENANT).put("candidate_id", CANDIDATE),
            "check_rate_limit",
            n -> n.has("rate_limited") && n.has("daily_limit") && n.has("current_count"));

        // ── Core submission flow ──────────────────────────────────────────
        testCapture("submit_idea",
            args().put("tenant_id",       TENANT)
                  .put("candidate_id",    CANDIDATE)
                  .put("title",           "Federated Learning Privacy Framework")
                  .put("description",     "Enable ML model training across decentralised data sources without sharing raw data")
                  .put("visibility_level","INTERNAL")
                  .put("idea_category",   "software")
                  .put("technical_details","Secure aggregation with differential privacy and gradient compression"),
            n -> {
                boolean ok = n.has("idea_id") && n.has("submitted_at") && n.has("content_hash")
                          && n.has("minio_object_key") && n.has("minio_etag")
                          && "PENDING_FINGERPRINT".equals(n.path("status").asText());
                if (ok) this.ideaId = n.get("idea_id").asText();
                return ok;
            });

        System.out.println("  [INFO] idea_id captured: " + ideaId);

        // ── Timestamp proof ───────────────────────────────────────────────
        test("get_timestamp_proof",
            args().put("tenant_id", TENANT).put("idea_id", ideaId),
            "get_timestamp_proof",
            n -> n.path("found").asBoolean()
              && n.has("timestamp_proof")
              && n.get("timestamp_proof").has("submitted_at")
              && n.get("timestamp_proof").has("content_hash"));

        // ── Version list ──────────────────────────────────────────────────
        test("list_idea_versions",
            args().put("tenant_id", TENANT).put("idea_id", ideaId),
            "list_idea_versions",
            n -> n.path("found").asBoolean()
              && n.path("version_count").asInt() >= 1
              && n.has("versions") && n.get("versions").isArray());

        // ── Ownership ─────────────────────────────────────────────────────
        test("get_idea_ownership",
            args().put("tenant_id", TENANT).put("idea_id", ideaId),
            "get_idea_ownership",
            n -> n.path("found").asBoolean()
              && n.has("ownership_record")
              && n.get("ownership_record").has("submitted_at")
              && n.has("licensing_eligibility"));

        // ── Status ────────────────────────────────────────────────────────
        test("get_submission_status (PENDING_FINGERPRINT)",
            args().put("tenant_id", TENANT).put("idea_id", ideaId),
            "get_submission_status",
            n -> n.path("found").asBoolean()
              && "PENDING_FINGERPRINT".equals(n.path("status").asText()));

        // ── Integrity — MATCH ─────────────────────────────────────────────
        test("verify_idea_integrity (MATCH)",
            args().put("tenant_id",      TENANT)
                  .put("idea_id",        ideaId)
                  .put("title",          "Federated Learning Privacy Framework")
                  .put("description",    "Enable ML model training across decentralised data sources without sharing raw data")
                  .put("technical_details","Secure aggregation with differential privacy and gradient compression"),
            "verify_idea_integrity",
            n -> "MATCH".equals(n.path("status").asText())
              && !n.path("tamper_detected").asBoolean());

        // ── Integrity — MISMATCH (tamper simulation) ──────────────────────
        test("verify_idea_integrity (MISMATCH — tamper)",
            args().put("tenant_id",   TENANT)
                  .put("idea_id",     ideaId)
                  .put("title",       "TAMPERED TITLE — someone changed this")
                  .put("description", "This description was modified after submission"),
            "verify_idea_integrity",
            n -> "MISMATCH".equals(n.path("status").asText())
              && n.path("tamper_detected").asBoolean()
              && n.has("tamper_evidence"));

        // ── MinIO verification ────────────────────────────────────────────
        test("verify_minio_object",
            args().put("tenant_id", TENANT).put("idea_id", ideaId),
            "verify_minio_object",
            n -> n.path("object_exists").asBoolean()
              && n.path("etag_match").asBoolean()
              && "OBJECT_INTACT".equals(n.path("status").asText()));

        // ── Revision ─────────────────────────────────────────────────────
        test("revise_idea",
            args().put("tenant_id",       TENANT)
                  .put("idea_id",         ideaId)
                  .put("candidate_id",    CANDIDATE)
                  .put("title",           "Federated Learning Privacy Framework v2")
                  .put("description",     "Revised: Enable ML training across decentralised nodes with secure aggregation and DP")
                  .put("visibility_level","MARKETPLACE")
                  .put("revision_reason", "Added differential privacy details and expanded use-case section"),
            "revise_idea",
            n -> n.path("version").asInt() == 2
              && n.has("content_hash")
              && n.has("previous_version")
              && "PENDING_FINGERPRINT".equals(n.path("status").asText())
              && n.path("kafka_event_published").path("is_revision").asBoolean());

        // ── Versions after revision ───────────────────────────────────────
        test("list_idea_versions (after revision — expect 2)",
            args().put("tenant_id", TENANT).put("idea_id", ideaId),
            "list_idea_versions",
            n -> n.path("version_count").asInt() == 2);

        // ── Visibility ────────────────────────────────────────────────────
        test("set_visibility (MARKETPLACE)",
            args().put("tenant_id",       TENANT)
                  .put("idea_id",         ideaId)
                  .put("candidate_id",    CANDIDATE)
                  .put("visibility_level","MARKETPLACE"),
            "set_visibility",
            n -> n.path("success").asBoolean()
              && "MARKETPLACE".equals(n.path("new_visibility").asText()));

        test("set_visibility (back to INTERNAL)",
            args().put("tenant_id",       TENANT)
                  .put("idea_id",         ideaId)
                  .put("candidate_id",    CANDIDATE)
                  .put("visibility_level","INTERNAL"),
            "set_visibility",
            n -> n.path("success").asBoolean()
              && "INTERNAL".equals(n.path("new_visibility").asText()));

        // ── Audit log ─────────────────────────────────────────────────────
        test("get_submission_audit (all events)",
            args().put("tenant_id", TENANT).put("idea_id", ideaId),
            "get_submission_audit",
            n -> n.has("audit_entries")
              && n.get("audit_entries").isArray()
              && n.path("total_entries").asInt() > 0);

        test("get_submission_audit (filter VISIBILITY_CHANGED)",
            args().put("tenant_id", TENANT).put("idea_id", ideaId)
                  .put("event_type_filter", "VISIBILITY_CHANGED"),
            "get_submission_audit",
            n -> n.path("filtered_entries").asInt() >= 1);

        test("get_submission_audit (filter INTEGRITY_VERIFIED)",
            args().put("tenant_id", TENANT).put("idea_id", ideaId)
                  .put("event_type_filter", "INTEGRITY_VERIFIED"),
            "get_submission_audit",
            n -> n.path("filtered_entries").asInt() >= 1);

        // ── Rate limit after submissions ──────────────────────────────────
        test("check_rate_limit (after 2 submissions)",
            args().put("tenant_id", TENANT).put("candidate_id", CANDIDATE),
            "check_rate_limit",
            n -> n.path("current_count").asInt() >= 1 && !n.path("rate_limited").asBoolean());

        // ── Archive ───────────────────────────────────────────────────────
        test("archive_idea",
            args().put("tenant_id",    TENANT)
                  .put("idea_id",      ideaId)
                  .put("candidate_id", CANDIDATE)
                  .put("archive_reason","Testing complete — archiving for compliance validation"),
            "archive_idea",
            n -> n.path("success").asBoolean()
              && "ARCHIVED".equals(n.path("new_status").asText())
              && n.has("retention_policy"));

        test("get_submission_status (after archive — ARCHIVED)",
            args().put("tenant_id", TENANT).put("idea_id", ideaId),
            "get_submission_status",
            n -> "ARCHIVED".equals(n.path("status").asText()));

        test("archive_idea (already archived — expect error)",
            args().put("tenant_id",    TENANT)
                  .put("idea_id",      ideaId)
                  .put("candidate_id", CANDIDATE),
            "archive_idea",
            n -> !n.path("success").asBoolean()
              && "ALREADY_ARCHIVED".equals(n.path("error").asText()));

        // ── Security tests ────────────────────────────────────────────────
        testSecurity("security: unknown tool rejected",
            "unknown_dangerous_tool", args(),
            n -> n.has("error"));

        testSecurity("security: SQL injection in title rejected",
            "submit_idea",
            args().put("tenant_id",       TENANT)
                  .put("candidate_id",    CANDIDATE)
                  .put("title",           "'; DROP TABLE ideas; --")
                  .put("description",     "Injection attempt")
                  .put("visibility_level","PRIVATE"),
            n -> n.has("error"));

        testSecurity("security: bad UUID in idea_id rejected",
            "get_timestamp_proof",
            args().put("tenant_id", TENANT).put("idea_id", "not-a-valid-uuid"),
            n -> n.has("error"));

        testSecurity("security: XSS in description rejected",
            "submit_idea",
            args().put("tenant_id",       TENANT)
                  .put("candidate_id",    CANDIDATE)
                  .put("title",           "Good title")
                  .put("description",     "<script>alert('xss')</script>")
                  .put("visibility_level","PRIVATE"),
            n -> n.has("error"));

        testSecurity("security: invalid visibility_level rejected",
            "submit_idea",
            args().put("tenant_id",       TENANT)
                  .put("candidate_id",    CANDIDATE)
                  .put("title",           "Test idea")
                  .put("description",     "Valid description")
                  .put("visibility_level","PUBLIC"),  // invalid
            n -> n.has("error"));

        // Summary
        System.out.println("\n=======================================================");
        System.out.printf("  TOTAL: %d/%d PASSED%n", passed, passed + failed);
        if (failed > 0) System.out.printf("  FAILED: %d%n", failed);
        System.out.println("=======================================================");
        System.exit(failed > 0 ? 1 : 0);
    }

    // ── Test runners ──────────────────────────────────────────────────────────

    @FunctionalInterface
    interface Assertion { boolean test(JsonNode node); }

    private void test(String label, ObjectNode args, String toolName, Assertion assertion) {
        internalTest(label, args, toolName, assertion, false);
    }

    private void testCapture(String toolName, ObjectNode args, Assertion assertion) {
        internalTest(toolName, args, toolName, assertion, false);
    }

    private void testSecurity(String label, String toolName, ObjectNode args, Assertion assertion) {
        internalTest(label, args, toolName, assertion, true);
    }

    private void internalTest(String label, ObjectNode args, String toolName,
                               Assertion assertion, boolean expectError) {
        try {
            SecurityValidator sv = new SecurityValidator();
            sv.validateToolCall(toolName, args);

            Object result = registry.call(toolName, args);
            JsonNode node = MAPPER.readTree(MAPPER.writeValueAsString(result));

            boolean ok = assertion.test(node);
            printResult(label, ok, MAPPER.writeValueAsString(node));
            if (ok) passed++; else failed++;

        } catch (SecurityException | IllegalArgumentException e) {
            // For security tests, catching an exception = the rejection happened = PASS
            if (expectError) {
                System.out.printf("  [PASS] %-50s (security exception: %s)%n", label,
                    e.getMessage().length() > 40 ? e.getMessage().substring(0, 40) + "..." : e.getMessage());
                passed++;
            } else {
                System.out.printf("  [FAIL] %-50s — unexpected exception: %s%n", label, e.getMessage());
                failed++;
            }
        } catch (Exception e) {
            System.out.printf("  [FAIL] %-50s — exception: %s%n", label, e.getMessage());
            if (verbose) e.printStackTrace();
            failed++;
        }
    }

    private void printResult(String label, boolean ok, String json) {
        if (ok) {
            System.out.printf("  [PASS] %-50s%n", label);
            if (verbose)
                System.out.println("         " + json.substring(0, Math.min(150, json.length())) + "...");
        } else {
            System.out.printf("  [FAIL] %-50s%n", label);
            System.out.println("         RESPONSE: " + json.substring(0, Math.min(250, json.length())));
        }
    }

    private ObjectNode args() { return MAPPER.createObjectNode(); }
}
