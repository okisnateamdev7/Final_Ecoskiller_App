package com.ecoskiller.mcp.helm;

import com.ecoskiller.mcp.helm.handlers.ToolRegistry;
import com.ecoskiller.mcp.helm.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.UUID;

/**
 * TestAgents — Helm Service MCP Server
 * 30 tests covering all 15 tools + 8 security tests
 *
 * Run: java -cp <jar> com.ecoskiller.mcp.helm.TestAgents [--verbose]
 */
public class TestAgents {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Valid Helm release names (RFC 1123)
    static final String REL_NEW    = "helm-service-test";
    static final String REL_EXIST  = "auth-service";   // pre-seeded in prod namespace
    static final String NS_PROD    = "prod";
    static final String NS_CORE    = "core";
    static final String NS_DEV     = "dev";
    static final String CHART      = "ecoskiller/helm-service";
    static final String VERSION    = "1.0.0";
    static final String P1         = UUID.randomUUID().toString();
    static final String P2         = UUID.randomUUID().toString();
    static final String SESSION_ID_HOLDER[] = {null}; // captured from session_create

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
        System.out.println("===============================================================");
        System.out.println(" Ecoskiller | Helm Service MCP — Test Suite");
        System.out.println("===============================================================");

        // ── Helm list / status (seeded releases) ─────────────────────────
        test("helm_list_releases (all namespaces)",
            args(), "helm_list_releases",
            n -> n.has("releases") && n.path("total_releases").asInt() >= 5);

        test("helm_list_releases (filter prod)",
            args().put("namespace", NS_PROD), "helm_list_releases",
            n -> n.path("namespace_filter").asText().equals("prod")
              && n.path("total_releases").asInt() >= 1);

        test("helm_get_status (auth-service — seeded)",
            args().put("release_name", REL_EXIST).put("namespace", NS_PROD),
            "helm_get_status",
            n -> n.path("found").asBoolean()
              && n.has("status") && n.has("revision") && n.has("kubernetes_resources"));

        test("helm_get_values (auth-service user values)",
            args().put("release_name", REL_EXIST).put("namespace", NS_PROD)
                  .put("all_values", false),
            "helm_get_values",
            n -> n.path("found").asBoolean() && n.has("user_supplied_values"));

        test("helm_get_values (all_values=true includes chart defaults)",
            args().put("release_name", REL_EXIST).put("namespace", NS_PROD)
                  .put("all_values", true),
            "helm_get_values",
            n -> n.path("found").asBoolean() && n.has("chart_defaults"));

        test("helm_get_history (auth-service)",
            args().put("release_name", REL_EXIST).put("namespace", NS_PROD),
            "helm_get_history",
            n -> n.path("found").asBoolean() && n.has("history")
              && n.path("total_revisions").asInt() >= 1);

        // ── Helm install ──────────────────────────────────────────────────
        test("helm_install (new release)",
            args().put("release_name", REL_NEW).put("namespace", NS_DEV)
                  .put("chart_name",   CHART).put("chart_version", VERSION)
                  .put("environment",  "dev")
                  .put("values_yaml",  "replicaCount: 1\nimage.tag: 1.0.0"),
            "helm_install",
            n -> "success".equals(n.path("status").asText())
              && n.path("revision").asInt() == 1
              && n.has("helm_command") && n.has("kafka_event_published"));

        test("helm_install (duplicate — expect error)",
            args().put("release_name", REL_NEW).put("namespace", NS_DEV)
                  .put("chart_name",   CHART),
            "helm_install",
            n -> "RELEASE_EXISTS".equals(n.path("error").asText()));

        // ── Helm upgrade ──────────────────────────────────────────────────
        test("helm_upgrade (existing release)",
            args().put("release_name", REL_NEW).put("namespace", NS_DEV)
                  .put("chart_version","1.1.0").put("environment", "dev"),
            "helm_upgrade",
            n -> "success".equals(n.path("status").asText())
              && n.path("new_revision").asInt() == 2
              && n.path("previous_revision").asInt() == 1
              && n.has("helm_command"));

        test("helm_upgrade (--install on non-existing release)",
            args().put("release_name","brand-new-svc").put("namespace", NS_DEV)
                  .put("chart_name",  "ecoskiller/job-service")
                  .put("install",     true),
            "helm_upgrade",
            n -> "success".equals(n.path("status").asText())
              && n.path("was_install").asBoolean());

        test("helm_upgrade (not found, install=false — expect error)",
            args().put("release_name","does-not-exist").put("namespace", NS_PROD),
            "helm_upgrade",
            n -> "RELEASE_NOT_FOUND".equals(n.path("error").asText()));

        // ── Helm rollback ─────────────────────────────────────────────────
        test("helm_rollback (to previous revision)",
            args().put("release_name", REL_NEW).put("namespace", NS_DEV),
            "helm_rollback",
            n -> "success".equals(n.path("status").asText())
              && n.path("rolled_back_to_revision").asInt() == 1
              && n.has("helm_command") && n.has("kafka_event_published"));

        test("helm_rollback (single revision — expect error)",
            args().put("release_name", "auth-service").put("namespace", NS_PROD),
            "helm_rollback",
            n -> "NO_PREVIOUS_REVISION".equals(n.path("error").asText()));

        // ── Helm lint ─────────────────────────────────────────────────────
        test("helm_lint_chart (passes)",
            args().put("chart_name",  CHART).put("environment", "prod")
                  .put("values_yaml", "replicaCount: 2\nresources:\n  requests:\n    cpu: 200m"),
            "helm_lint_chart",
            n -> n.path("lint_passed").asBoolean() && n.has("helm_command"));

        test("helm_lint_chart (warning — plain text password)",
            args().put("chart_name",  CHART)
                  .put("values_yaml", "db.password: mysecret123"),
            "helm_lint_chart",
            n -> n.has("warnings") && n.path("warning_count").asInt() >= 1);

        // ── Helm diff ─────────────────────────────────────────────────────
        test("helm_diff_upgrade (change detected)",
            args().put("release_name", REL_EXIST).put("namespace", NS_PROD)
                  .put("chart_version","2.0.0"),
            "helm_diff_upgrade",
            n -> n.path("diff_computed").asBoolean()
              && n.path("changes_detected").asBoolean()
              && n.has("modified_resources") && n.has("helm_command"));

        // ── Helm uninstall ────────────────────────────────────────────────
        test("helm_uninstall (with confirmation)",
            args().put("release_name",   REL_NEW).put("namespace", NS_DEV)
                  .put("confirm_uninstall", true),
            "helm_uninstall",
            n -> "success".equals(n.path("status").asText())
              && n.has("uninstalled_at") && n.has("helm_command"));

        // ── Session lifecycle ─────────────────────────────────────────────
        testCapture("session_create",
            args().put("topic",           "AI in Healthcare: Opportunities and Challenges")
                  .put("max_participants", 4)
                  .put("intro_duration_seconds",  60)
                  .put("rebuttal_duration_seconds",30)
                  .put("language",         "en"),
            n -> {
                boolean ok = n.has("session_id") && n.has("room_name") && n.has("join_url")
                          && "pending".equals(n.path("state").asText())
                          && n.has("speaking_protocol") && n.has("privacy_compliance_dpdp2023");
                if (ok) SESSION_ID_HOLDER[0] = n.get("session_id").asText();
                return ok;
            });

        System.out.println("  [INFO] session_id captured: " + SESSION_ID_HOLDER[0]);

        test("session_join (participant 1 — auto-starts session)",
            args().put("session_id",    SESSION_ID_HOLDER[0])
                  .put("participant_id",P1)
                  .put("consent_given", true)
                  .put("candidate_name","Alice"),
            "session_join",
            n -> n.path("speaking_position").asInt() == 1
              && "muted".equals(n.path("mic_state").asText())
              && n.has("speaking_order_info") && n.has("kafka_event_published"));

        test("session_join (participant 2)",
            args().put("session_id",    SESSION_ID_HOLDER[0])
                  .put("participant_id",P2)
                  .put("consent_given", true)
                  .put("candidate_name","Bob"),
            "session_join",
            n -> n.path("speaking_position").asInt() == 2 && n.has("jitsi_note"));

        test("session_get_status (after joins)",
            args().put("session_id", SESSION_ID_HOLDER[0]),
            "session_get_status",
            n -> n.path("found").asBoolean()
              && "active".equals(n.path("state").asText())
              && n.path("participant_count").asInt() == 2
              && n.has("speaking_queue") && n.has("session_protocol"));

        test("session_advance_turn (Alice → Bob)",
            args().put("session_id",         SESSION_ID_HOLDER[0])
                  .put("current_speaker_id", P1),
            "session_advance_turn",
            n -> P1.equals(n.path("previous_speaker_id").asText())
              && n.has("next_speaker_id")
              && n.has("jitsi_commands_issued")
              && n.has("kafka_events_published")
              && n.path("round").asInt() >= 1);

        test("session_advance_turn (invalid speaker)",
            args().put("session_id",         SESSION_ID_HOLDER[0])
                  .put("current_speaker_id", UUID.randomUUID().toString()),
            "session_advance_turn",
            n -> "SPEAKER_NOT_IN_SESSION".equals(n.path("error").asText()));

        test("session_get_metrics (aggregated after session)",
            args().put("session_id", SESSION_ID_HOLDER[0])
                  .put("include_events", true),
            "session_get_metrics",
            n -> n.has("participant_metrics") && n.has("session_summary")
              && n.has("data_privacy_note") && n.has("event_log")
              && n.path("session_summary").has("total_turns_completed"));

        test("session_get_status (unknown session)",
            args().put("session_id", UUID.randomUUID().toString()),
            "session_get_status",
            n -> !n.path("found").asBoolean());

        // ── Security tests ────────────────────────────────────────────────
        testSec("security: unknown tool rejected",
            "rm_rf_slash", args(),
            n -> n.has("error"));

        testSec("security: invalid release_name (capitals) rejected",
            "helm_install",
            args().put("release_name","MyService").put("namespace",NS_DEV).put("chart_name",CHART),
            n -> n.has("error"));

        testSec("security: invalid namespace rejected",
            "helm_get_status",
            args().put("release_name","auth-service").put("namespace","kube-public-hacked"),
            n -> n.has("error"));

        testSec("security: shell injection in chart_name rejected",
            "helm_install",
            args().put("release_name","test-svc").put("namespace",NS_DEV)
                  .put("chart_name","ecoskiller/auth; rm -rf /"),
            n -> n.has("error"));

        testSec("security: uninstall without confirm rejected",
            "helm_uninstall",
            args().put("release_name",REL_EXIST).put("namespace",NS_PROD)
                  .put("confirm_uninstall",false),
            n -> n.has("error"));

        testSec("security: session_join without consent rejected",
            "session_join",
            args().put("session_id",   SESSION_ID_HOLDER[0] != null ? SESSION_ID_HOLDER[0] : UUID.randomUUID().toString())
                  .put("participant_id",UUID.randomUUID().toString())
                  .put("consent_given", false),
            n -> n.has("error"));

        testSec("security: invalid chart_version format rejected",
            "helm_upgrade",
            args().put("release_name",REL_EXIST).put("namespace",NS_PROD)
                  .put("chart_version","../../../etc/passwd"),
            n -> n.has("error"));

        testSec("security: max_participants out of range rejected",
            "session_create",
            args().put("topic","Test").put("max_participants", 99),
            n -> n.has("error"));

        // Summary
        System.out.println("\n===============================================================");
        System.out.printf("  TOTAL: %d/%d PASSED%n", passed, passed + failed);
        if (failed > 0) System.out.printf("  FAILED: %d%n", failed);
        System.out.println("===============================================================");
        System.exit(failed > 0 ? 1 : 0);
    }

    // ── Test runners ──────────────────────────────────────────────────────────

    @FunctionalInterface interface Assertion { boolean test(JsonNode n); }

    private void test(String label, ObjectNode args, String tool, Assertion a) {
        internalTest(label, args, tool, a, false);
    }
    private void testCapture(String tool, ObjectNode args, Assertion a) {
        internalTest(tool, args, tool, a, false);
    }
    private void testSec(String label, String tool, ObjectNode args, Assertion a) {
        internalTest(label, args, tool, a, true);
    }

    private void internalTest(String label, ObjectNode args, String tool,
                               Assertion assertion, boolean expectError) {
        try {
            new SecurityValidator().validateToolCall(tool, args);
            Object result = registry.call(tool, args);
            JsonNode node = MAPPER.readTree(MAPPER.writeValueAsString(result));
            boolean ok = assertion.test(node);
            printResult(label, ok, MAPPER.writeValueAsString(node));
            if (ok) passed++; else failed++;
        } catch (SecurityException | IllegalArgumentException e) {
            if (expectError) {
                System.out.printf("  [PASS] %-55s (rejected: %s)%n", label,
                    e.getMessage().length()>50 ? e.getMessage().substring(0,50)+"..." : e.getMessage());
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
            if (verbose) System.out.println("  " + json.substring(0, Math.min(160, json.length())) + "...");
        } else {
            System.out.printf("  [FAIL] %-55s%n", label);
            System.out.println("         " + json.substring(0, Math.min(280, json.length())));
        }
    }

    private ObjectNode args() { return MAPPER.createObjectNode(); }
}
