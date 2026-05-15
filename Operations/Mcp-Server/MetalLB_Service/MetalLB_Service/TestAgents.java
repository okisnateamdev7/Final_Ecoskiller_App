package com.ecoskiller.mcp.metallb;

import com.ecoskiller.mcp.metallb.json.Json;
import com.ecoskiller.mcp.metallb.security.SecurityManager;
import com.ecoskiller.mcp.metallb.tools.ToolRegistry;
import com.ecoskiller.mcp.metallb.tools.ToolNotFoundException;

import java.util.*;

/**
 * TestAgents — Full test suite for MetalLB MCP Server
 * 50+ assertions across security, tool registry, and all 20 tools.
 *
 * Run: java -cp out com.ecoskiller.mcp.metallb.TestAgents
 *      java -cp out com.ecoskiller.mcp.metallb.TestAgents --verbose
 */
public class TestAgents {

    private static int passed = 0, failed = 0;
    private static boolean verbose = false;

    public static void main(String[] args) throws Exception {
        verbose = args.length > 0 && "--verbose".equals(args[0]);

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║   MetalLB MCP Server — Test Suite                   ║");
        System.out.println("║   Ecoskiller CAT-OPS | 20 Tools + Security          ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        ToolRegistry registry = new ToolRegistry();
        registry.registerAll();
        SecurityManager sec = new SecurityManager();

        // ── JSON Parser ───────────────────────────────────────────────────
        section("JSON PARSER");

        test("Parse simple object", () -> {
            Json.Obj o = (Json.Obj) Json.parse("{\"key\":\"value\",\"num\":42}");
            assertEquals("value", Json.strField(o, "key", ""));
            assertEquals(42L, Json.numField(o, "num", 0));
        });

        test("Parse nested object", () -> {
            Json.Obj o = (Json.Obj) Json.parse("{\"a\":{\"b\":\"deep\"}}");
            Json.Obj inner = o.obj("a");
            assertNotNull(inner);
            assertEquals("deep", Json.strField(inner, "b", ""));
        });

        test("Parse array", () -> {
            Json.Arr a = (Json.Arr) Json.parse("[\"x\",\"y\",\"z\"]");
            assertEquals(3, a.size());
        });

        test("Parse boolean and null", () -> {
            Json.Obj o = (Json.Obj) Json.parse("{\"t\":true,\"f\":false,\"n\":null}");
            assertTrue(o.bool("t"));
            assertTrue(!o.bool("f"));
        });

        test("Serialize and re-parse roundtrip", () -> {
            Json.Obj original = Json.obj().put("name","metallb").put("version","1.0.0").put("count", 20L);
            String json = original.toJson();
            Json.Obj parsed = (Json.Obj) Json.parse(json);
            assertEquals("metallb", Json.strField(parsed,"name",""));
            assertEquals(20L, Json.numField(parsed,"count",0));
        });

        test("String escaping in JSON output", () -> {
            Json.Obj o = Json.obj().put("msg", "line1\nline2\ttab\"quote");
            String json = o.toJson();
            assertTrue(json.contains("\\n"));
            assertTrue(json.contains("\\t"));
            assertTrue(json.contains("\\\""));
        });

        // ── Security Layer ────────────────────────────────────────────────
        section("SECURITY LAYER");

        test("Valid JSON-RPC 2.0 request accepted", () -> {
            Json.Obj req = (Json.Obj) Json.parse("{\"jsonrpc\":\"2.0\",\"method\":\"tools/list\",\"id\":1}");
            assertTrue(sec.validateRequest(req));
        });

        test("Missing jsonrpc field rejected", () -> {
            Json.Obj req = (Json.Obj) Json.parse("{\"method\":\"ping\",\"id\":1}");
            assertTrue(!sec.validateRequest(req));
        });

        test("Wrong jsonrpc version rejected", () -> {
            Json.Obj req = (Json.Obj) Json.parse("{\"jsonrpc\":\"1.0\",\"method\":\"ping\"}");
            assertTrue(!sec.validateRequest(req));
        });

        test("Missing method field rejected", () -> {
            Json.Obj req = (Json.Obj) Json.parse("{\"jsonrpc\":\"2.0\",\"id\":1}");
            assertTrue(!sec.validateRequest(req));
        });

        test("Valid protocol versions accepted", () -> {
            assertTrue(sec.isAllowedProtocolVersion("2024-11-05"));
            assertTrue(sec.isAllowedProtocolVersion("2025-03-26"));
        });

        test("Invalid protocol version rejected", () -> {
            assertTrue(!sec.isAllowedProtocolVersion("2020-01-01"));
            assertTrue(!sec.isAllowedProtocolVersion(""));
            assertTrue(!sec.isAllowedProtocolVersion(null));
        });

        test("All 20 tool names accepted", () -> {
            for (String name : sec.getAllowedTools()) {
                assertTrue(sec.validateToolName(name));
            }
        });

        test("Exactly 20 tools in whitelist", () -> {
            assertEquals(20, sec.getAllowedTools().size());
        });

        test("Unknown tool name rejected", () -> {
            assertTrue(!sec.validateToolName("evil_tool"));
            assertTrue(!sec.validateToolName("rm -rf /"));
            assertTrue(!sec.validateToolName(""));
            assertTrue(!sec.validateToolName(null));
        });

        test("Valid IP address in args accepted", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"peer_ip\":\"172.16.0.1\"}");
            assertTrue(sec.sanitizeArguments(a));
        });

        test("Valid CIDR in args accepted", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"ip\":\"10.0.0.100/32\"}");
            assertTrue(sec.sanitizeArguments(a));
        });

        test("Shell injection in args rejected", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"service_name\":\"nginx; rm -rf /\"}");
            assertTrue(!sec.sanitizeArguments(a));
        });

        test("Path traversal in args rejected", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"namespace\":\"../etc/passwd\"}");
            assertTrue(!sec.sanitizeArguments(a));
        });

        test("Oversized string in args rejected", () -> {
            String big = "x".repeat(600);
            Json.Obj a = Json.obj().put("service_name", big);
            assertTrue(!sec.sanitizeArguments(a));
        });

        test("Negative number in args rejected", () -> {
            Json.Obj a = Json.obj().put("asn", -1L);
            assertTrue(!sec.sanitizeArguments(a));
        });

        test("Null args accepted", () -> {
            assertTrue(sec.sanitizeArguments(null));
        });

        test("Empty args accepted", () -> {
            assertTrue(sec.sanitizeArguments(Json.obj()));
        });

        test("Backtick injection rejected", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"pool\":\"pool`whoami`\"}");
            assertTrue(!sec.sanitizeArguments(a));
        });

        test("Dollar sign injection rejected", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"namespace\":\"default$(id)\"}");
            assertTrue(!sec.sanitizeArguments(a));
        });

        // ── Tool Registry ─────────────────────────────────────────────────
        section("TOOL REGISTRY");

        test("Exactly 20 tools registered", () -> {
            assertEquals(20, registry.size());
        });

        test("All tools have name, description, inputSchema", () -> {
            for (Json.Obj def : registry.getToolDefinitions()) {
                String name = Json.strField(def,"name","?");
                assertNotNull(def.get("name"));
                assertNotNull(def.get("description"));
                assertNotNull(def.get("inputSchema"));
                assertTrue(Json.strField(def,"description","").length() > 20);
            }
        });

        test("ToolNotFoundException for unknown tool", () -> {
            try {
                registry.callTool("nonexistent_tool", Json.obj());
                assertTrue(false); // should not reach
            } catch (ToolNotFoundException e) {
                assertTrue(true);
            }
        });

        // ── IP Pool Tools ─────────────────────────────────────────────────
        section("IP POOL TOOLS (5)");

        test("metallb_get_status — shows key components", () -> {
            String r = registry.callTool("metallb_get_status", Json.obj());
            assertContains(r, "Controller Pod");
            assertContains(r, "Speaker DaemonSet");
            assertContains(r, "BGP");
            assertContains(r, "ECMP");
        });

        test("metallb_list_ip_pools — both clouds", () -> {
            String r = registry.callTool("metallb_list_ip_pools", Json.obj());
            assertContains(r, "gcp-pool");
            assertContains(r, "aws-pool");
            assertContains(r, "172.16.0");
            assertContains(r, "10.0.0");
        });

        test("metallb_list_ip_pools — gcp filter", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"cloud\":\"gcp\"}");
            String r = registry.callTool("metallb_list_ip_pools", a);
            assertContains(r, "gcp-pool");
            assertContains(r, "172.16.0");
        });

        test("metallb_allocate_ip — success returns assigned IP", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"service_name\":\"test-svc\",\"namespace\":\"default\",\"pool_name\":\"gcp-pool\"}");
            String r = registry.callTool("metallb_allocate_ip", a);
            assertContains(r, "IP Allocated Successfully");
            assertContains(r, "test-svc");
            assertContains(r, "172.16.0");
        });

        test("metallb_allocate_ip — unknown pool returns error", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"service_name\":\"svc\",\"namespace\":\"ns\",\"pool_name\":\"bad-pool\"}");
            String r = registry.callTool("metallb_allocate_ip", a);
            assertContains(r, "Pool not found");
        });

        test("metallb_release_ip — success", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"service_name\":\"nginx\",\"pool_name\":\"gcp-pool\",\"ip\":\"172.16.0.100\"}");
            String r = registry.callTool("metallb_release_ip", a);
            assertContains(r, "IP Released Successfully");
            assertContains(r, "172.16.0.100");
        });

        test("metallb_check_pool_exhaustion — shows Prometheus metrics", () -> {
            String r = registry.callTool("metallb_check_pool_exhaustion", Json.obj());
            assertContains(r, "metallb_allocations_used");
            assertContains(r, "metallb_pool_capacity");
        });

        test("metallb_expand_pool — generates YAML", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"pool_name\":\"gcp-pool\",\"new_end_ip\":\"172.16.0.250\"}");
            String r = registry.callTool("metallb_expand_pool", a);
            assertContains(r, "Pool Expansion Plan Generated");
            assertContains(r, "172.16.0.250");
            assertContains(r, "kubectl apply");
            assertContains(r, "IPAddressPool");
        });

        // ── BGP Tools ─────────────────────────────────────────────────────
        section("BGP TOOLS (3)");

        test("metallb_get_bgp_status — shows peers", () -> {
            String r = registry.callTool("metallb_get_bgp_status", Json.obj());
            assertContains(r, "ESTABLISHED");
            assertContains(r, "64512");
            assertContains(r, "gcp-router");
            assertContains(r, "aws-gateway");
        });

        test("metallb_configure_bgp — generates BGPPeer YAML", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"peer_ip\":\"172.16.0.1\",\"peer_asn\":\"64513\",\"cloud\":\"gcp\"}");
            String r = registry.callTool("metallb_configure_bgp", a);
            assertContains(r, "BGPPeer");
            assertContains(r, "172.16.0.1");
            assertContains(r, "64513");
            assertContains(r, "kubectl apply");
        });

        test("metallb_get_bgp_sessions — ECMP detail", () -> {
            String r = registry.callTool("metallb_get_bgp_sessions", Json.obj());
            assertContains(r, "k3s-node-1");
            assertContains(r, "ECMP");
            assertContains(r, "ESTABLISHED");
        });

        // ── L2 Tools ──────────────────────────────────────────────────────
        section("L2 TOOLS (2)");

        test("metallb_get_l2_status — shows ARP state", () -> {
            String r = registry.callTool("metallb_get_l2_status", Json.obj());
            assertContains(r, "ARP");
            assertContains(r, "Leader");
            assertContains(r, "Gratuitous ARP");
        });

        test("metallb_configure_l2 — generates L2Advertisement YAML", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"pool_name\":\"gcp-pool\"}");
            String r = registry.callTool("metallb_configure_l2", a);
            assertContains(r, "L2Advertisement");
            assertContains(r, "gcp-pool");
            assertContains(r, "kubectl apply");
        });

        // ── Service Tools ──────────────────────────────────────────────────
        section("SERVICE TOOLS (2)");

        test("metallb_list_services — shows active services", () -> {
            String r = registry.callTool("metallb_list_services", Json.obj());
            assertContains(r, "nginx-ingress");
            assertContains(r, "gcp-pool");
            assertContains(r, "BGP");
        });

        test("metallb_assign_service_ip — auto-assign YAML", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"service_name\":\"my-svc\",\"namespace\":\"prod\",\"pool_name\":\"gcp-pool\"}");
            String r = registry.callTool("metallb_assign_service_ip", a);
            assertContains(r, "LoadBalancer");
            assertContains(r, "my-svc");
            assertContains(r, "gcp-pool");
        });

        test("metallb_assign_service_ip — specific IP annotation", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"service_name\":\"svc\",\"namespace\":\"ns\",\"pool_name\":\"gcp-pool\",\"ip\":\"172.16.0.150\"}");
            String r = registry.callTool("metallb_assign_service_ip", a);
            assertContains(r, "172.16.0.150");
            assertContains(r, "loadBalancerIPs");
        });

        // ── Node Health Tools ──────────────────────────────────────────────
        section("NODE HEALTH TOOLS (2)");

        test("metallb_check_node_health — all 3 nodes shown", () -> {
            String r = registry.callTool("metallb_check_node_health", Json.obj());
            assertContains(r, "k3s-node-1");
            assertContains(r, "k3s-node-2");
            assertContains(r, "k3s-node-3");
            assertContains(r, "Ready");
        });

        test("metallb_simulate_failover — BGP timeline", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"node\":\"k3s-node-1\",\"mode\":\"bgp\"}");
            String r = registry.callTool("metallb_simulate_failover", a);
            assertContains(r, "DRY RUN");
            assertContains(r, "BGP");
            assertContains(r, "ECMP");
            assertContains(r, "60 seconds");
        });

        test("metallb_simulate_failover — L2 timeline", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"node\":\"k3s-node-2\",\"mode\":\"l2\"}");
            String r = registry.callTool("metallb_simulate_failover", a);
            assertContains(r, "DRY RUN");
            assertContains(r, "ARP");
            assertContains(r, "10 seconds");
        });

        // ── Metrics ───────────────────────────────────────────────────────
        section("METRICS TOOLS (1)");

        test("metallb_get_metrics — shows Prometheus + AlertManager", () -> {
            String r = registry.callTool("metallb_get_metrics", Json.obj());
            assertContains(r, "metallb_allocations_used");
            assertContains(r, "metallb_bgp_sessions_up");
            assertContains(r, "AlertManager");
            assertContains(r, "PagerDuty");
        });

        // ── Config Tools ──────────────────────────────────────────────────
        section("CONFIG TOOLS (3)");

        test("metallb_get_config — returns full YAML", () -> {
            String r = registry.callTool("metallb_get_config", Json.obj());
            assertContains(r, "IPAddressPool");
            assertContains(r, "BGPPeer");
            assertContains(r, "BGPAdvertisement");
            assertContains(r, "64512");
        });

        test("metallb_validate_config — all checks pass", () -> {
            String r = registry.callTool("metallb_validate_config", Json.obj());
            assertContains(r, "ALL CHECKS PASSED");
            assertContains(r, "17/17");
        });

        test("metallb_audit_log — shows recent events", () -> {
            String r = registry.callTool("metallb_audit_log", Json.obj());
            assertContains(r, "IP_ALLOCATED");
            assertContains(r, "BGP_ROUTE_ANNOUNCED");
            assertContains(r, "nginx-ingress");
        });

        // ── Troubleshoot ──────────────────────────────────────────────────
        section("TROUBLESHOOT TOOLS (1)");

        test("metallb_troubleshoot_service — full diagnostic output", () -> {
            Json.Obj a = (Json.Obj) Json.parse("{\"service_name\":\"broken-svc\",\"namespace\":\"staging\"}");
            String r = registry.callTool("metallb_troubleshoot_service", a);
            assertContains(r, "broken-svc");
            assertContains(r, "staging");
            assertContains(r, "POOL EXHAUSTION");
            assertContains(r, "BGP");
            assertContains(r, "kubectl");
            assertContains(r, "RBAC");
        });

        // ── MCP Protocol Integration ──────────────────────────────────────
        section("MCP PROTOCOL INTEGRATION");

        test("tools/list response is valid JSON with tools array", () -> {
            // Simulate what the server would return
            Json.Arr tools = Json.arr();
            for (Json.Obj def : registry.getToolDefinitions()) tools.add(def);
            Json.Obj result = Json.obj().put("tools", tools);
            Json.Obj response = Json.obj()
                .put("jsonrpc","2.0")
                .put("id", Json.obj()) // dummy
                .put("result", result);
            String json = response.toJson();
            // Re-parse to validate
            Json.Obj parsed = (Json.Obj) Json.parse(json);
            assertNotNull(parsed.get("result"));
            Json.Obj res = parsed.obj("result");
            assertNotNull(res);
            assertNotNull(res.get("tools"));
        });

        test("Error response has correct JSON-RPC structure", () -> {
            Json.Obj error = Json.obj().put("code", -32601L).put("message","Method not found");
            Json.Obj response = Json.obj()
                .put("jsonrpc","2.0")
                .put("error", error);
            String json = response.toJson();
            Json.Obj parsed = (Json.Obj) Json.parse(json);
            assertNotNull(parsed.get("error"));
            Json.Obj err = parsed.obj("error");
            assertEquals(-32601L, Json.numField(err,"code",0));
        });

        test("Tool call result has content array with text block", () -> {
            Json.Obj content = Json.obj().put("type","text").put("text","hello");
            Json.Arr contentArr = Json.arr().add(content);
            Json.Obj result = Json.obj().put("content",contentArr).put("isError",false);
            String json = result.toJson();
            Json.Obj parsed = (Json.Obj) Json.parse(json);
            assertTrue(!parsed.bool("isError"));
            assertNotNull(parsed.get("content"));
        });

        // ── Summary ───────────────────────────────────────────────────────
        int total = passed + failed;
        System.out.println("\n" + "═".repeat(57));
        System.out.printf("  RESULTS: %d passed | %d failed | %d total%n", passed, failed, total);
        System.out.println("═".repeat(57));
        if (failed == 0) {
            System.out.println("  ✅ ALL TESTS PASSED");
        } else {
            System.out.println("  ❌ " + failed + " TEST(S) FAILED");
            System.exit(1);
        }
    }

    // ── Assertion helpers ────────────────────────────────────────────────

    static void section(String name) {
        System.out.println("\n── " + name + " " + "─".repeat(Math.max(2, 50 - name.length())));
    }

    static void test(String name, TestCase tc) {
        try {
            tc.run();
            passed++;
            System.out.printf("  ✅ %s%n", name);
        } catch (Throwable e) {
            failed++;
            System.out.printf("  ❌ %s%n", name);
            if (verbose) System.out.printf("     → %s%n", e.getMessage());
        }
    }

    static void assertContains(String text, String sub) {
        if (!text.contains(sub))
            throw new AssertionError("Expected to contain: '" + sub + "'");
    }
    static void assertEquals(Object expected, Object actual) {
        if (!Objects.equals(expected, actual))
            throw new AssertionError("Expected: " + expected + " but was: " + actual);
    }
    static void assertNotNull(Object val) {
        if (val == null) throw new AssertionError("Expected non-null value");
    }
    static void assertTrue(boolean condition) {
        if (!condition) throw new AssertionError("Expected true");
    }

    @FunctionalInterface interface TestCase { void run() throws Exception; }
}
