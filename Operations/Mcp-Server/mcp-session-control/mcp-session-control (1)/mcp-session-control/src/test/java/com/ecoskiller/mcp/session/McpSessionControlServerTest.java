package com.ecoskiller.mcp.session;

import com.ecoskiller.mcp.session.client.SessionControlClient;
import com.ecoskiller.mcp.session.config.SessionControlConfig;
import com.ecoskiller.mcp.session.security.AuditLogger;
import com.ecoskiller.mcp.session.tools.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Session Control MCP Server.
 *
 * Security tests run without the service.
 * Integration tests require Session-Control-Service on localhost:3000.
 *
 *   export MCP_API_KEY=test-secret-key-session
 *   export SESSION_SERVICE_BASE_URL=http://localhost:3000
 *   mvn test
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class McpSessionControlServerTest {

    private static final String       TEST_API_KEY = "test-secret-key-session";
    private static final ObjectMapper JSON         = new ObjectMapper();

    private static ToolRegistry registry;

    @BeforeAll
    static void setup() {
        SessionControlConfig cfg    = SessionControlConfig.fromEnvironment();
        SessionControlClient client = new SessionControlClient(cfg);
        AuditLogger          audit  = new AuditLogger();
        registry = new ToolRegistry(client, cfg, audit);
    }

    private ObjectNode args(String... kv) {
        ObjectNode n = JSON.createObjectNode();
        n.put("api_key", TEST_API_KEY);
        for (int i = 0; i + 1 < kv.length; i += 2) n.put(kv[i], kv[i + 1]);
        return n;
    }

    private ToolResult call(String toolName, ObjectNode arguments) throws Exception {
        McpTool tool = registry.find(toolName);
        assertNotNull(tool, "Tool not found: " + toolName);
        return tool.execute(arguments);
    }

    // ── Registry ──────────────────────────────────────────────────────────────

    @Test @Order(1)
    void testRegistryHas24Tools() {
        assertEquals(24, registry.size(), "Should have exactly 24 tools");
    }

    @Test @Order(2)
    void testAllToolsHaveValidSchemas() {
        for (McpTool t : registry.allTools()) {
            ObjectNode s = t.schema();
            assertNotNull(s.get("name"),        t.name() + " missing name");
            assertNotNull(s.get("description"), t.name() + " missing description");
            assertNotNull(s.get("inputSchema"), t.name() + " missing inputSchema");
            JsonNode req = s.path("inputSchema").path("required");
            assertTrue(req.toString().contains("api_key"), t.name() + " must require api_key");
        }
    }

    // ── MCP Protocol ──────────────────────────────────────────────────────────

    @Test @Order(3)
    void testInitializeProtocol() throws Exception {
        McpSessionControlServer server = new McpSessionControlServer();
        String req = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"initialize\",\"params\":{}}";
        var m = McpSessionControlServer.class.getDeclaredMethod("handleRequest", JsonNode.class);
        m.setAccessible(true);
        String resp = (String) m.invoke(server, JSON.readTree(req));
        JsonNode r = JSON.readTree(resp);
        assertEquals("2024-11-05",            r.path("result").path("protocolVersion").asText());
        assertEquals("mcp-session-control",   r.path("result").path("serverInfo").path("name").asText());
    }

    @Test @Order(4)
    void testToolsListReturns24() throws Exception {
        McpSessionControlServer server = new McpSessionControlServer();
        String req = "{\"jsonrpc\":\"2.0\",\"id\":2,\"method\":\"tools/list\",\"params\":{}}";
        var m = McpSessionControlServer.class.getDeclaredMethod("handleRequest", JsonNode.class);
        m.setAccessible(true);
        String resp = (String) m.invoke(server, JSON.readTree(req));
        assertEquals(24, JSON.readTree(resp).path("result").path("tools").size());
    }

    // ── Input Validation / Security Tests (no service needed) ─────────────────

    @Test @Order(10)
    void testInvalidSessionIdRejected() {
        assertThrows(Exception.class, () ->
            call("session_get", args("session_id", "../../../etc/passwd"))
        );
    }

    @Test @Order(11)
    void testEmptySessionIdRejected() {
        assertThrows(Exception.class, () ->
            call("session_get", args("session_id", ""))
        );
    }

    @Test @Order(12)
    void testInvalidParticipantIdRejected() {
        assertThrows(Exception.class, () ->
            call("participant_heartbeat", args(
                "session_id", "550e8400-e29b-41d4-a716-446655440000",
                "participant_id", "<script>alert(1)</script>"))
        );
    }

    @Test @Order(13)
    void testDurationTooShortRejected() throws Exception {
        ToolResult r = call("session_create", args(
            "assessment_id",   "550e8400-e29b-41d4-a716-446655440001",
            "assessment_type", "GROUP_DISCUSSION",
            "participant_ids", "550e8400-e29b-41d4-a716-446655440002",
            "duration_secs",   "30"  // < 60s minimum
        ));
        assertTrue(r.isError() || r.content().contains("error") || r.content().contains("60"));
    }

    @Test @Order(14)
    void testInvalidAssessmentTypeRejected() throws Exception {
        ToolResult r = call("session_create", args(
            "assessment_id",   "550e8400-e29b-41d4-a716-446655440001",
            "assessment_type", "INVALID_TYPE",
            "participant_ids", "550e8400-e29b-41d4-a716-446655440002",
            "duration_secs",   "3600"
        ));
        assertTrue(r.isError() || r.content().contains("error"));
    }

    @Test @Order(15)
    void testInvalidRoleRejected() throws Exception {
        ToolResult r = call("participant_join", args(
            "session_id",      "550e8400-e29b-41d4-a716-446655440000",
            "participant_id",  "550e8400-e29b-41d4-a716-446655440001",
            "role",            "HACKER",
            "jwt_token",       "eyJhbGciOiJSUzI1NiJ9.test.signature"
        ));
        assertTrue(r.isError() || r.content().contains("error"));
    }

    @Test @Order(16)
    void testArchiveRequiresConfirm() throws Exception {
        ToolResult r = call("session_archive", args(
            "session_id", "550e8400-e29b-41d4-a716-446655440000",
            "confirm",    "NO"
        ));
        assertTrue(r.isError() || r.content().contains("CONFIRM"));
    }

    @Test @Order(17)
    void testEvictRequiresConfirm() throws Exception {
        ToolResult r = call("participant_evict", args(
            "session_id",     "550e8400-e29b-41d4-a716-446655440000",
            "participant_id", "550e8400-e29b-41d4-a716-446655440001",
            "reason",         "Disruptive behaviour",
            "confirm",        "WRONG"
        ));
        assertTrue(r.isError() || r.content().contains("CONFIRM"));
    }

    @Test @Order(18)
    void testTerminateRequiresConfirm() throws Exception {
        ToolResult r = call("session_terminate", args(
            "session_id", "550e8400-e29b-41d4-a716-446655440000",
            "reason",     "Test",
            "confirm",    ""
        ));
        assertTrue(r.isError() || r.content().contains("CONFIRM"));
    }

    @Test @Order(19)
    void testJitsiForceEndRequiresConfirm() throws Exception {
        ToolResult r = call("jitsi_force_end", args(
            "session_id", "550e8400-e29b-41d4-a716-446655440000",
            "reason",     "Test",
            "confirm",    "nope"
        ));
        assertTrue(r.isError() || r.content().contains("CONFIRM"));
    }

    @Test @Order(20)
    void testExtendDurationTooLong() throws Exception {
        ToolResult r = call("session_extend", args(
            "session_id",      "550e8400-e29b-41d4-a716-446655440000",
            "extend_by_secs",  "9999",
            "reason",          "Test"
        ));
        assertTrue(r.isError() || r.content().contains("error"));
    }

    @Test @Order(21)
    void testJitsiMuteInvalidTarget() throws Exception {
        ToolResult r = call("jitsi_mute", args(
            "session_id",      "550e8400-e29b-41d4-a716-446655440000",
            "participant_id",  "../malicious",
            "mute",            "true"
        ));
        assertTrue(r.isError() || r.content().contains("error"));
    }

    @Test @Order(22)
    void testShortJwtTokenRejected() throws Exception {
        ToolResult r = call("participant_join", args(
            "session_id",     "550e8400-e29b-41d4-a716-446655440000",
            "participant_id", "550e8400-e29b-41d4-a716-446655440001",
            "role",           "PARTICIPANT",
            "jwt_token",      "short"
        ));
        assertTrue(r.isError() || r.content().contains("JWT") || r.content().contains("error"));
    }

    // ── Integration Tests (require service on localhost:3000) ─────────────────

    @Test @Order(30)
    void testServiceHealth() throws Exception {
        ToolResult r = call("service_health", args());
        assertFalse(r.isError());
        JsonNode j = JSON.readTree(r.content());
        assertNotNull(j.get("live"));
        assertNotNull(j.get("ready"));
        assertNotNull(j.get("healthy"));
        assertNotNull(j.get("latency_ms"));
    }

    @Test @Order(31)
    void testSessionStats() throws Exception {
        ToolResult r = call("session_stats", args());
        assertFalse(r.isError());
        JsonNode j = JSON.readTree(r.content());
        // Stats should return even if 0 sessions
        assertNotNull(j.get("active_sessions"));
    }
}
