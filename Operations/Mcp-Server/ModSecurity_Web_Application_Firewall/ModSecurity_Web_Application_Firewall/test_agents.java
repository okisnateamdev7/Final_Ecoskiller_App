package com.ecoskiller.waf;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * test_agents.java — Test all 18 ModSecurity WAF MCP agents.
 *
 * Usage:
 *   javac -cp . test_agents.java McpWafServer.java
 *   java  test_agents             # pass/fail summary
 *   java  test_agents --verbose   # full JSON output
 */
public class test_agents {

    static boolean verbose = false;
    static int passed = 0, failed = 0;

    public static void main(String[] args) throws Exception {
        verbose = Arrays.asList(args).contains("--verbose");
        System.out.println("=== Ecoskiller ModSecurity WAF — Agent Tests ===\n");

        test("initialize",
            rpc("initialize", "{\"protocolVersion\":\"2024-11-05\",\"capabilities\":{}}"),
            "mcp-waf-modsecurity");

        test("ping",
            rpc("ping", "{}"),
            "pong");

        test("tools/list — 18 agents",
            rpc("tools/list", "{}"),
            "inspect_request");

        test("inspect_request — clean",
            callTool("inspect_request",
                "{\"method\":\"GET\",\"uri\":\"/api/assessments/123\"," +
                 "\"headers\":{\"User-Agent\":\"Mozilla/5.0\",\"Host\":\"api.ecoskiller.com\"}," +
                 "\"body\":\"\",\"client_ip\":\"10.0.0.1\"}"),
            "ALLOWED");

        test("inspect_request — SQL injection",
            callTool("inspect_request",
                "{\"method\":\"POST\",\"uri\":\"/api/assessments/123/submit\"," +
                 "\"headers\":{\"User-Agent\":\"Mozilla/5.0\"}," +
                 "\"body\":\"{\\\"answer\\\":\\\"1' OR '1'='1\\\"}\",\"client_ip\":\"198.51.100.1\"}"),
            "SQL_INJECTION");

        test("inspect_request — XSS in body",
            callTool("inspect_request",
                "{\"method\":\"POST\",\"uri\":\"/api/comment\"," +
                 "\"headers\":{\"User-Agent\":\"Chrome/120\"}," +
                 "\"body\":\"<script>alert(1)</script>\",\"client_ip\":\"198.51.100.2\"}"),
            "XSS");

        test("inspect_request — path traversal",
            callTool("inspect_request",
                "{\"method\":\"GET\",\"uri\":\"/api/files/../../etc/passwd\"," +
                 "\"headers\":{\"User-Agent\":\"Mozilla/5.0\"}," +
                 "\"body\":\"\",\"client_ip\":\"198.51.100.3\"}"),
            "PATH_TRAVERSAL");

        test("detect_sql_injection — UNION SELECT",
            callTool("detect_sql_injection",
                "{\"input\":\"1 UNION SELECT username,password FROM users--\"}"),
            "true");

        test("detect_sql_injection — clean",
            callTool("detect_sql_injection",
                "{\"input\":\"correct answer text here\"}"),
            "false");

        test("detect_xss — script tag",
            callTool("detect_xss",
                "{\"input\":\"<script>document.cookie</script>\"}"),
            "true");

        test("detect_xss — clean",
            callTool("detect_xss",
                "{\"input\":\"Hello, this is a normal answer.\"}"),
            "false");

        test("detect_command_injection — cmd injection",
            callTool("detect_command_injection",
                "{\"input\":\"test; cat /etc/passwd\"}"),
            "COMMAND_INJECTION");

        test("detect_command_injection — path traversal",
            callTool("detect_command_injection",
                "{\"input\":\"../../etc/shadow\"}"),
            "PATH_TRAVERSAL");

        test("rate_limit_check — under limit",
            callTool("rate_limit_check",
                "{\"client_ip\":\"192.168.1.1\",\"endpoint\":\"/api/assessments\",\"increment\":\"true\"}"),
            "ALLOW");

        test("detect_bot — sqlmap",
            callTool("detect_bot",
                "{\"user_agent\":\"sqlmap/1.7\",\"client_ip\":\"10.10.10.1\",\"endpoint\":\"/api\",\"method\":\"GET\"}"),
            "true");

        test("detect_bot — missing UA",
            callTool("detect_bot",
                "{\"user_agent\":\"\",\"client_ip\":\"10.10.10.2\",\"endpoint\":\"/api\",\"method\":\"GET\"}"),
            "Missing User-Agent");

        test("inspect_response — credit card leak",
            callTool("inspect_response",
                "{\"response_body\":\"User card: 4111111111111111 expires 12/26\"," +
                 "\"response_headers\":{\"Content-Type\":\"application/json\"}," +
                 "\"uri\":\"/api/user/profile\"}"),
            "credit_card_number");

        test("inspect_response — clean",
            callTool("inspect_response",
                "{\"response_body\":\"{\\\"score\\\":85,\\\"grade\\\":\\\"A\\\"}\"," +
                 "\"response_headers\":{\"Content-Type\":\"application/json\"}," +
                 "\"uri\":\"/api/results/456\"}"),
            "false");

        test("audit_log_query — all logs",
            callTool("audit_log_query",
                "{\"limit\":5}"),
            "returned");

        test("evaluate_custom_rule — add rule",
            callTool("evaluate_custom_rule",
                "{\"action\":\"add\",\"rule_id\":\"10001\",\"variable\":\"ARGS\"," +
                 "\"operator\":\"@contains\",\"pattern\":\"<\",\"rule_action\":\"block\"," +
                 "\"message\":\"HTML injection in assessment\"}"),
            "RULE_ADDED");

        test("evaluate_custom_rule — evaluate match",
            callTool("evaluate_custom_rule",
                "{\"action\":\"evaluate\",\"rule_id\":\"10001\",\"operator\":\"@contains\"," +
                 "\"pattern\":\"<\",\"input\":\"answer with <b>bold</b> text\"}"),
            "true");

        test("evaluate_custom_rule — list",
            callTool("evaluate_custom_rule", "{\"action\":\"list\"}"),
            "count");

        test("get_anomaly_score — read",
            callTool("get_anomaly_score",
                "{\"client_ip\":\"198.51.100.1\"}"),
            "current_score");

        test("get_anomaly_score — add score",
            callTool("get_anomaly_score",
                "{\"client_ip\":\"172.16.0.100\",\"add_score\":10,\"reason\":\"suspicious headers\"}"),
            "score_added");

        test("block_ip — block",
            callTool("block_ip",
                "{\"action\":\"block\",\"client_ip\":\"172.16.0.99\",\"reason\":\"repeated attacks\"}"),
            "BLOCKED");

        test("block_ip — list",
            callTool("block_ip", "{\"action\":\"list\"}"),
            "blocked_ips");

        test("block_ip — unblock",
            callTool("block_ip",
                "{\"action\":\"unblock\",\"client_ip\":\"172.16.0.99\"}"),
            "UNBLOCKED");

        test("whitelist_ip — add",
            callTool("whitelist_ip",
                "{\"action\":\"add\",\"client_ip\":\"10.0.0.50\",\"reason\":\"internal monitoring\"}"),
            "WHITELISTED");

        test("whitelist_ip — list",
            callTool("whitelist_ip", "{\"action\":\"list\"}"),
            "whitelisted_ips");

        test("geo_ip_check — allowed (IN)",
            callTool("geo_ip_check",
                "{\"client_ip\":\"1.1.1.1\",\"allowed_countries\":[\"IN\"],\"purpose\":\"assessment\"}"),
            "true");

        test("geo_ip_check — blocked country",
            callTool("geo_ip_check",
                "{\"client_ip\":\"51.1.1.1\",\"allowed_countries\":[\"IN\"],\"purpose\":\"assessment\"}"),
            "false");

        test("detect_data_leak — SSN in request",
            callTool("detect_data_leak",
                "{\"content\":\"candidate SSN: 123-45-6789\",\"direction\":\"response\",\"mask\":\"true\"}"),
            "social_security_number");

        test("detect_data_leak — API key",
            callTool("detect_data_leak",
                "{\"content\":\"{\\\"api_key\\\":\\\"sk-abc123xyz456def789ghi\\\"}\"," +
                 "\"direction\":\"response\",\"mask\":\"true\"}"),
            "api_key_or_secret");

        test("detect_data_leak — clean",
            callTool("detect_data_leak",
                "{\"content\":\"{\\\"name\\\":\\\"John\\\",\\\"score\\\":90}\",\"direction\":\"response\"}"),
            "false");

        test("get_security_events — all",
            callTool("get_security_events", "{\"limit\":10}"),
            "events");

        test("get_security_events — blocked only",
            callTool("get_security_events",
                "{\"event_type\":\"request_blocked\",\"limit\":5}"),
            "returned");

        test("update_rules — get_config",
            callTool("update_rules", "{\"action\":\"get_config\"}"),
            "waf_mode");

        test("update_rules — set mode DetectionOnly",
            callTool("update_rules", "{\"action\":\"set_mode\",\"mode\":\"DetectionOnly\"}"),
            "MODE_UPDATED");

        test("update_rules — disable XSS ruleset",
            callTool("update_rules", "{\"action\":\"disable_ruleset\",\"ruleset\":\"xss\"}"),
            "RULESETS_DISABLED");

        test("update_rules — enable all",
            callTool("update_rules", "{\"action\":\"enable_ruleset\",\"ruleset\":\"all\"}"),
            "RULESETS_ENABLED");

        test("health_check",
            callTool("health_check", "{}"),
            "HEALTHY");

        test("get_waf_stats",
            callTool("get_waf_stats", "{}"),
            "total_requests");

        // ── Summary ──
        System.out.println("\n═══════════════════════════════════");
        System.out.println("PASSED: " + passed + " | FAILED: " + failed +
            " | TOTAL: " + (passed + failed));
        System.out.println("═══════════════════════════════════");
        if (failed > 0) System.exit(1);
    }

    // ─── helpers ──────────────────────────────────────────────

    static String rpc(String method, String params) {
        return "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"" + method + "\",\"params\":" + params + "}";
    }

    static String callTool(String toolName, String argsJson) {
        return "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"tools/call\"," +
               "\"params\":{\"name\":\"" + toolName + "\",\"arguments\":" + argsJson + "}}";
    }

    static void test(String label, String request, String expectedSubstring) {
        try {
            // Pipe request through McpWafServer.dispatch (reflective call to package-private method)
            java.lang.reflect.Method dispatch = McpWafServer.class
                .getDeclaredMethod("dispatch", String.class);
            dispatch.setAccessible(true);
            String response = (String) dispatch.invoke(null, request);

            if (verbose) {
                System.out.println("── " + label);
                System.out.println("  REQ:  " + request.substring(0, Math.min(120, request.length())));
                System.out.println("  RESP: " + response.substring(0, Math.min(300, response.length())));
                System.out.println();
            }

            if (response != null && response.contains(expectedSubstring)) {
                System.out.println("[PASS] " + label);
                passed++;
            } else {
                System.out.println("[FAIL] " + label);
                System.out.println("       Expected: " + expectedSubstring);
                System.out.println("       Got:      " + (response == null ? "null" :
                    response.substring(0, Math.min(200, response.length()))));
                failed++;
            }
        } catch (Exception e) {
            System.out.println("[FAIL] " + label + " — Exception: " + e.getMessage());
            failed++;
        }
    }
}
