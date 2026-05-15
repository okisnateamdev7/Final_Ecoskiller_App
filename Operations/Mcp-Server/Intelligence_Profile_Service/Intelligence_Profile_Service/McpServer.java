package com.ecoskiller.mcp.ips.server;

import com.ecoskiller.mcp.ips.security.SecurityValidator;
import com.ecoskiller.mcp.ips.tools.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.*;

/**
 * Intelligence Profile Service — MCP Server (Java)
 * Ecoskiller | CAT-IPS — AI-Powered Talent Assessment & Analytics
 *
 * Transport : stdio (stdin/stdout)
 * Protocol  : JSON-RPC 2.0 / MCP 2024-11-05
 * Security  : JWT validation, tenant isolation, RBAC enforcement
 *
 * Agents (15):
 *  1.  get_intelligence_profile
 *  2.  get_skill_vectors
 *  3.  search_candidates
 *  4.  get_peer_benchmarks
 *  5.  get_risk_assessment
 *  6.  process_assessment_event
 *  7.  process_skill_assessment
 *  8.  process_gd_discussion
 *  9.  process_learning_interaction
 * 10.  compute_intelligence_dna
 * 11.  detect_profile_anomaly
 * 12.  recalculate_vectors
 * 13.  get_fairness_audit
 * 14.  rebuild_profiles
 * 15.  get_profile_history
 */
public class McpServer {

    private static final String MCP_VERSION   = "2024-11-05";
    private static final String SERVER_NAME   = "mcp-ips-intelligence-profile";
    private static final String SERVER_VERSION = "1.0.0";

    private static final Logger LOG = Logger.getLogger(McpServer.class.getName());

    private final ObjectMapper  mapper;
    private final SecurityValidator security;
    private final ToolRegistry  registry;

    public McpServer() {
        this.mapper   = new ObjectMapper();
        this.security = new SecurityValidator();
        this.registry = new ToolRegistry(security);
        configureLogging();
    }

    // ─────────────────────────────────────────────────────────
    //  Entry point
    // ─────────────────────────────────────────────────────────

    public static void main(String[] args) {
        new McpServer().run();
    }

    public void run() {
        LOG.info("Intelligence Profile Service MCP Server starting (stdio transport)…");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true)) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String response = handleMessage(line);
                if (response != null) {
                    writer.println(response);
                }
            }
        } catch (IOException e) {
            LOG.severe("Fatal I/O error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Message dispatcher
    // ─────────────────────────────────────────────────────────

    private String handleMessage(String raw) {
        JsonNode req = null;
        try {
            req = mapper.readTree(raw);
            String method = req.path("method").asText();
            JsonNode id    = req.path("id");

            return switch (method) {
                case "initialize"  -> handleInitialize(id, req);
                case "tools/list"  -> handleToolsList(id);
                case "tools/call"  -> handleToolsCall(id, req);
                case "ping"        -> buildResult(id, mapper.createObjectNode());
                default            -> buildError(id, -32601, "Method not found: " + method, null);
            };

        } catch (Exception e) {
            JsonNode id = (req != null) ? req.path("id") : NullNode.getInstance();
            LOG.warning("Parse/dispatch error: " + e.getMessage());
            return buildError(id, -32700, "Parse error", e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────
    //  initialize
    // ─────────────────────────────────────────────────────────

    private String handleInitialize(JsonNode id, JsonNode req) throws Exception {
        ObjectNode caps = mapper.createObjectNode();
        caps.putObject("tools").put("listChanged", false);

        ObjectNode serverInfo = mapper.createObjectNode();
        serverInfo.put("name",    SERVER_NAME);
        serverInfo.put("version", SERVER_VERSION);

        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);
        result.set("capabilities",   caps);
        result.set("serverInfo",      serverInfo);
        result.put("instructions",
            "Intelligence Profile Service — builds, maintains, and enriches " +
            "permanent Intelligence DNA profiles for every Ecoskiller user. " +
            "All tools require a valid JWT bearer token in the _auth.token field. " +
            "Multi-tenant: tenantId must match the JWT claim.");

        return buildResult(id, result);
    }

    // ─────────────────────────────────────────────────────────
    //  tools/list
    // ─────────────────────────────────────────────────────────

    private String handleToolsList(JsonNode id) throws Exception {
        ArrayNode tools = registry.getToolDefinitions();
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", tools);
        return buildResult(id, result);
    }

    // ─────────────────────────────────────────────────────────
    //  tools/call
    // ─────────────────────────────────────────────────────────

    private String handleToolsCall(JsonNode id, JsonNode req) throws Exception {
        JsonNode params    = req.path("params");
        String  toolName   = params.path("name").asText();
        JsonNode arguments = params.path("arguments");

        // ── Security gate: validate JWT before any tool executes ──
        String authError = security.validateRequest(arguments);
        if (authError != null) {
            return buildError(id, -32001, "UNAUTHORIZED", authError);
        }

        try {
            JsonNode toolResult = registry.dispatch(toolName, arguments);
            ObjectNode result   = mapper.createObjectNode();
            ArrayNode content   = mapper.createArrayNode();
            ObjectNode textNode = mapper.createObjectNode();
            textNode.put("type", "text");
            textNode.put("text", mapper.writerWithDefaultPrettyPrinter()
                                        .writeValueAsString(toolResult));
            content.add(textNode);
            result.set("content", content);
            result.put("isError", false);
            return buildResult(id, result);

        } catch (SecurityException se) {
            return buildError(id, -32002, "FORBIDDEN", se.getMessage());
        } catch (IllegalArgumentException ae) {
            return buildError(id, -32602, "INVALID_PARAMS", ae.getMessage());
        } catch (Exception e) {
            LOG.severe("Tool execution error [" + toolName + "]: " + e.getMessage());
            return buildError(id, -32000, "TOOL_ERROR", e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────
    //  JSON-RPC helpers
    // ─────────────────────────────────────────────────────────

    private String buildResult(JsonNode id, JsonNode result) throws Exception {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        resp.set("id",      id);
        resp.set("result",  result);
        return mapper.writeValueAsString(resp);
    }

    private String buildError(JsonNode id, int code, String message, String data) {
        try {
            ObjectNode resp  = mapper.createObjectNode();
            resp.put("jsonrpc", "2.0");
            resp.set("id",      (id != null) ? id : NullNode.getInstance());
            ObjectNode error = mapper.createObjectNode();
            error.put("code",    code);
            error.put("message", message);
            if (data != null) error.put("data", data);
            resp.set("error", error);
            return mapper.writeValueAsString(resp);
        } catch (Exception e) {
            return "{\"jsonrpc\":\"2.0\",\"id\":null,\"error\":{\"code\":-32700,\"message\":\"Internal error\"}}";
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Logging — stderr only so stdout stays clean for MCP
    // ─────────────────────────────────────────────────────────

    private void configureLogging() {
        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        StreamHandler stderr = new StreamHandler(System.err, new SimpleFormatter());
        stderr.setLevel(Level.ALL);
        root.addHandler(stderr);
        root.setLevel(Level.INFO);
    }
}
