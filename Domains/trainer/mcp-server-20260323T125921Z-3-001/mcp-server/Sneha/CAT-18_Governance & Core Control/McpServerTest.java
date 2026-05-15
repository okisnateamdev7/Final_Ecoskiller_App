package com.ecoskiller.mcp18;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration-style tests for mcp-18-governance MCP server.
 * Each test sends a JSON-RPC request via piped stdin/stdout.
 */
class McpServerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // ── helpers ──────────────────────────────────────────────────
    private JsonNode send(String requestJson) throws Exception {
        ByteArrayInputStream  in  = new ByteArrayInputStream((requestJson + "\n").getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        InputStream  origIn  = System.in;
        PrintStream  origOut = System.out;

        System.setIn(in);
        System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
        try {
            McpServer.main(new String[]{});
        } catch (Exception ignored) {}
        System.setIn(origIn);
        System.setOut(origOut);

        String response = out.toString(StandardCharsets.UTF_8).trim();
        assertFalse(response.isEmpty(), "Server returned empty response for: " + requestJson);
        return MAPPER.readTree(response);
    }

    // ── INITIALIZE ───────────────────────────────────────────────
    @Test
    void testInitialize() throws Exception {
        String req = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"initialize\",\"params\":{}}";
        JsonNode resp = send(req);

        assertEquals("2.0", resp.path("jsonrpc").asText());
        assertEquals("mcp-18-governance", resp.path("result").path("serverInfo").path("name").asText());
        assertEquals("2024-11-05", resp.path("result").path("protocolVersion").asText());
    }

    // ── TOOLS/LIST ───────────────────────────────────────────────
    @Test
    void testToolsList() throws Exception {
        String req = "{\"jsonrpc\":\"2.0\",\"id\":2,\"method\":\"tools/list\",\"params\":{}}";
        JsonNode resp = send(req);

        JsonNode tools = resp.path("result").path("tools");
        assertTrue(tools.isArray(), "tools should be an array");
        assertEquals(2, tools.size(), "Expected exactly 2 tools");

        // Tool names
        boolean hasRateLimit     = false;
        boolean hasTraceability  = false;
        for (JsonNode t : tools) {
            String name = t.path("name").asText();
            if ("api_rate_limit_enforcement".equals(name)) hasRateLimit    = true;
            if ("decision_traceability".equals(name))      hasTraceability = true;
        }
        assertTrue(hasRateLimit,    "Missing tool: api_rate_limit_enforcement");
        assertTrue(hasTraceability, "Missing tool: decision_traceability");
    }

    // ── TOOL: api_rate_limit_enforcement — check (allowed) ───────
    @Test
    void testRateLimitCheck() throws Exception {
        String req = """
            {"jsonrpc":"2.0","id":3,"method":"tools/call","params":{
              "name":"api_rate_limit_enforcement",
              "arguments":{
                "client_id":"tenant-001",
                "endpoint":"/api/v1/jobs",
                "action":"check",
                "tier":"pro"
              }
            }}""";
        JsonNode resp = send(req);

        String text = resp.path("result").path("content").get(0).path("text").asText();
        JsonNode payload = MAPPER.readTree(text);

        assertEquals("API_RATE_LIMIT_ENFORCEMENT_AGENT_COMPLETE", payload.path("agent").asText());
        assertTrue(payload.has("allowed"),        "Response must have 'allowed' field");
        assertTrue(payload.has("status"),         "Response must have 'status' field");
        assertTrue(payload.has("remaining"),      "Response must have 'remaining' field");
        assertTrue(payload.path("effective_max").asInt() > 0, "effective_max must be positive");
    }

    // ── TOOL: api_rate_limit_enforcement — get_quota ─────────────
    @Test
    void testRateLimitGetQuota() throws Exception {
        String req = """
            {"jsonrpc":"2.0","id":4,"method":"tools/call","params":{
              "name":"api_rate_limit_enforcement",
              "arguments":{
                "client_id":"tenant-enterprise-X",
                "action":"get_quota",
                "tier":"enterprise"
              }
            }}""";
        JsonNode resp = send(req);
        String text = resp.path("result").path("content").get(0).path("text").asText();
        JsonNode payload = MAPPER.readTree(text);

        assertTrue(payload.has("quota_details"), "get_quota should return quota_details");
        assertEquals(1000, payload.path("quota_details").path("max_requests").asInt(),
            "Enterprise tier should have 1000 max_requests");
    }

    // ── TOOL: decision_traceability — record ─────────────────────
    @Test
    void testDecisionRecord() throws Exception {
        String req = """
            {"jsonrpc":"2.0","id":5,"method":"tools/call","params":{
              "name":"decision_traceability",
              "arguments":{
                "action":"record",
                "actor_id":"scoring-agent-7",
                "actor_type":"agent",
                "decision_type":"scoring",
                "rationale":"Candidate scored 82/100 on skill assessment.",
                "inputs_json":"{\"candidate_id\":\"C-101\",\"test_id\":\"T-55\"}",
                "output_json":"{\"score\":82,\"grade\":\"B+\"}",
                "reference_id":"C-101"
              }
            }}""";
        JsonNode resp = send(req);
        String text = resp.path("result").path("content").get(0).path("text").asText();
        JsonNode payload = MAPPER.readTree(text);

        assertEquals("DECISION_TRACEABILITY_AGENT_COMPLETE", payload.path("agent").asText());
        assertEquals("RECORDED", payload.path("status").asText());
        assertTrue(payload.path("recorded").path("immutable").asBoolean(), "recorded.immutable must be true");
        assertFalse(payload.path("recorded").path("decision_id").asText().isEmpty());
    }

    // ── TOOL: decision_traceability — verify_integrity ───────────
    @Test
    void testDecisionVerifyIntegrity() throws Exception {
        String req = """
            {"jsonrpc":"2.0","id":6,"method":"tools/call","params":{
              "name":"decision_traceability",
              "arguments":{
                "action":"verify_integrity",
                "decision_id":"DEC-ABCDEF123456"
              }
            }}""";
        JsonNode resp = send(req);
        String text = resp.path("result").path("content").get(0).path("text").asText();
        JsonNode payload = MAPPER.readTree(text);

        assertEquals("INTEGRITY_OK", payload.path("status").asText());
        assertTrue(payload.path("integrity_ok").asBoolean());
        assertFalse(payload.path("tampered").asBoolean());
    }

    // ── TOOL: decision_traceability — get_chain ──────────────────
    @Test
    void testDecisionGetChain() throws Exception {
        String req = """
            {"jsonrpc":"2.0","id":7,"method":"tools/call","params":{
              "name":"decision_traceability",
              "arguments":{
                "action":"get_chain",
                "actor_id":"agent-42"
              }
            }}""";
        JsonNode resp = send(req);
        String text = resp.path("result").path("content").get(0).path("text").asText();
        JsonNode payload = MAPPER.readTree(text);

        assertTrue(payload.path("chain").isArray());
        assertTrue(payload.path("chain_length").asInt() > 0);
        assertEquals("GENESIS", payload.path("chain").get(0).path("prev_hash").asText(),
            "First block's prev_hash should be GENESIS");
    }

    // ── UNKNOWN TOOL ─────────────────────────────────────────────
    @Test
    void testUnknownTool() throws Exception {
        String req = """
            {"jsonrpc":"2.0","id":8,"method":"tools/call","params":{
              "name":"does_not_exist",
              "arguments":{}
            }}""";
        JsonNode resp = send(req);
        assertTrue(resp.path("result").path("isError").asBoolean(), "Should return isError:true for unknown tool");
    }

    // ── PING ─────────────────────────────────────────────────────
    @Test
    void testPing() throws Exception {
        String req = "{\"jsonrpc\":\"2.0\",\"id\":9,\"method\":\"ping\",\"params\":{}}";
        JsonNode resp = send(req);
        assertTrue(resp.has("result"), "ping should return a result");
    }
}
