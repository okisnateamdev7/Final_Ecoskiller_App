package com.ecoskiller.antigravity.server;

import com.ecoskiller.antigravity.agents.*;
import com.ecoskiller.antigravity.models.AgentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ecoskiller Antigravity MCP Server — Unit Tests")
class AntigravityMcpServerTest {

    private AntigravityMcpServer server;
    private ObjectMapper         mapper;

    @BeforeEach
    void setUp() {
        server = new AntigravityMcpServer();
        mapper = new ObjectMapper();
    }

    // ─── Protocol Tests ───────────────────────────────────────────────────

    @Test @DisplayName("initialize → returns protocolVersion and serverInfo")
    void testInitialize() throws Exception {
        String req  = """
            {"jsonrpc":"2.0","id":1,"method":"initialize",
             "params":{"protocolVersion":"2024-11-05","clientInfo":{"name":"test","version":"1"}}}
            """;
        String resp = server.handleMessage(req);
        assertTrue(resp.contains("mcp-01-platform"));
        assertTrue(resp.contains("1.0.0"));
        assertTrue(resp.contains("2024-11-05"));
        assertFalse(resp.contains("\"error\""));
    }

    @Test @DisplayName("tools/list → returns all 11 agents' tools")
    void testToolsList() throws Exception {
        String req  = "{\"jsonrpc\":\"2.0\",\"id\":2,\"method\":\"tools/list\",\"params\":{}}";
        String resp = server.handleMessage(req);
        assertTrue(resp.contains("antigravity_get_config"));
        assertTrue(resp.contains("antigravity_scan_dependencies"));
        assertTrue(resp.contains("antigravity_health_check"));
        assertTrue(resp.contains("antigravity_route_request"));
        assertTrue(resp.contains("antigravity_evaluate_platform"));
        assertTrue(resp.contains("antigravity_scan_tech_debt"));
        assertTrue(resp.contains("antigravity_validate_truth"));
        assertTrue(resp.contains("antigravity_list_api_versions"));
        assertTrue(resp.contains("antigravity_check_schema_compatibility"));
        assertTrue(resp.contains("antigravity_bootstrap_server"));
        assertTrue(resp.contains("antigravity_render_dashboard"));
    }

    @Test @DisplayName("ping → returns empty result")
    void testPing() {
        String resp = server.handleMessage("{\"jsonrpc\":\"2.0\",\"id\":3,\"method\":\"ping\",\"params\":{}}");
        assertTrue(resp.contains("\"result\""));
        assertFalse(resp.contains("\"error\""));
    }

    @Test @DisplayName("unknown method → JSON-RPC error -32601")
    void testUnknownMethod() {
        String resp = server.handleMessage("{\"jsonrpc\":\"2.0\",\"id\":4,\"method\":\"nonexistent\",\"params\":{}}");
        assertTrue(resp.contains("-32601"));
    }

    @Test @DisplayName("malformed JSON → parse error -32700")
    void testMalformedJson() {
        String resp = server.handleMessage("{bad json}}}");
        assertTrue(resp.contains("-32700"));
    }

    // ─── Agent Tool Call Tests ────────────────────────────────────────────

    @Test @DisplayName("CONFIGURATION_AGENT: antigravity_get_config")
    void testGetConfig() {
        String req  = """
            {"jsonrpc":"2.0","id":5,"method":"tools/call",
             "params":{"name":"antigravity_get_config","arguments":{"environment":"staging","namespace":"core"}}}
            """;
        String resp = server.handleMessage(req);
        assertTrue(resp.contains("SUCCESS"));
        assertTrue(resp.contains("mcp-01-platform"));
        assertFalse(resp.contains("\"isError\":true"));
    }

    @Test @DisplayName("CONFIGURATION_AGENT: antigravity_toggle_feature_flag")
    void testToggleFlag() {
        String req = """
            {"jsonrpc":"2.0","id":6,"method":"tools/call",
             "params":{"name":"antigravity_toggle_feature_flag",
                       "arguments":{"flag_name":"ENABLE_TRUTH_ENGINE","enabled":"false","environment":"production"}}}
            """;
        String resp = server.handleMessage(req);
        assertTrue(resp.contains("ENABLE_TRUTH_ENGINE"));
    }

    @Test @DisplayName("DEPENDENCY_RISK_AGENT: antigravity_scan_dependencies")
    void testScanDeps() {
        String req = """
            {"jsonrpc":"2.0","id":7,"method":"tools/call",
             "params":{"name":"antigravity_scan_dependencies",
                       "arguments":{"mcp_server":"mcp-01-platform","severity_threshold":"HIGH"}}}
            """;
        String resp = server.handleMessage(req);
        assertTrue(resp.contains("DEPENDENCY_RISK"));
        assertTrue(resp.contains("scanned_at"));
    }

    @Test @DisplayName("OBSERVABILITY_AGENT: antigravity_health_check")
    void testHealthCheck() {
        String req = """
            {"jsonrpc":"2.0","id":8,"method":"tools/call",
             "params":{"name":"antigravity_health_check","arguments":{"include_agents":"true"}}}
            """;
        String resp = server.handleMessage(req);
        assertTrue(resp.contains("HEALTHY"));
        assertTrue(resp.contains("99.97"));
    }

    @Test @DisplayName("ORCHESTRATION_GOVERNOR_AGENT: antigravity_route_request")
    void testRouteRequest() {
        String req = """
            {"jsonrpc":"2.0","id":9,"method":"tools/call",
             "params":{"name":"antigravity_route_request",
                       "arguments":{"intent":"skill assessment","payload":"{}","priority":"HIGH"}}}
            """;
        String resp = server.handleMessage(req);
        assertTrue(resp.contains("mcp-10-skill"));
    }

    @Test @DisplayName("TRUTH_ENGINE_AGENT: antigravity_validate_truth")
    void testValidateTruth() {
        String req = """
            {"jsonrpc":"2.0","id":10,"method":"tools/call",
             "params":{"name":"antigravity_validate_truth",
                       "arguments":{"claim":"user scored 95 in math","data_type":"score","source":"internal"}}}
            """;
        String resp = server.handleMessage(req);
        assertTrue(resp.contains("confidence"));
    }

    @Test @DisplayName("SYSTEM_SETUP_AGENT: antigravity_bootstrap_server")
    void testBootstrapServer() {
        String req = """
            {"jsonrpc":"2.0","id":11,"method":"tools/call",
             "params":{"name":"antigravity_bootstrap_server",
                       "arguments":{"mcp_server_id":"mcp-28-test","namespace":"core","environment":"staging"}}}
            """;
        String resp = server.handleMessage(req);
        assertTrue(resp.contains("bootstrapped"));
        assertTrue(resp.contains("mcp-28-test"));
    }

    @Test @DisplayName("UI_AGENT: antigravity_render_dashboard")
    void testRenderDashboard() {
        String req = """
            {"jsonrpc":"2.0","id":12,"method":"tools/call",
             "params":{"name":"antigravity_render_dashboard",
                       "arguments":{"user_role":"admin","tenant_id":"ecoskiller"}}}
            """;
        String resp = server.handleMessage(req);
        assertTrue(resp.contains("GREEN"));
        assertTrue(resp.contains("99.97%"));
    }

    @Test @DisplayName("tools/call: unknown tool → error in content")
    void testUnknownTool() {
        String req = """
            {"jsonrpc":"2.0","id":13,"method":"tools/call",
             "params":{"name":"nonexistent_tool","arguments":{}}}
            """;
        String resp = server.handleMessage(req);
        assertTrue(resp.contains("-32602") || resp.contains("Tool not found"));
    }

    // ─── Direct Agent Tests ───────────────────────────────────────────────

    @Test @DisplayName("ConfigurationAgent direct: list feature flags")
    void testConfigAgentDirect() {
        ConfigurationAgent agent = new ConfigurationAgent();
        assertEquals("ANTIGRAVITY_CONFIGURATION_AGENT", agent.getName());
        assertEquals(4, agent.getTools().size());

        ObjectNode args = mapper.createObjectNode();
        args.put("environment", "production");
        AgentResponse resp = agent.execute("antigravity_list_feature_flags", args);
        assertFalse(resp.isError());
        assertTrue(resp.toJson(mapper).contains("ENABLE_TRUTH_ENGINE"));
    }

    @Test @DisplayName("OrchestrationGovernorAgent: routing table has 29 servers")
    void testRoutingTable() {
        OrchestrationGovernorAgent agent = new OrchestrationGovernorAgent();
        ObjectNode args = mapper.createObjectNode();
        AgentResponse resp = agent.execute("antigravity_get_routing_table", args);
        assertFalse(resp.isError());
        assertTrue(resp.toJson(mapper).contains("\"total\":29"));
    }
}
