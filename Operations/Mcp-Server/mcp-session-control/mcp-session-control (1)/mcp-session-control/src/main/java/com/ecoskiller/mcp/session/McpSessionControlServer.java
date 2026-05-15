package com.ecoskiller.mcp.session;

import com.ecoskiller.mcp.session.client.SessionControlClient;
import com.ecoskiller.mcp.session.config.SessionControlConfig;
import com.ecoskiller.mcp.session.security.ApiKeyValidator;
import com.ecoskiller.mcp.session.security.AuditLogger;
import com.ecoskiller.mcp.session.tools.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Ecoskiller — Session Control MCP Server (Java)
 * CAT-REALTIME-06 | Priority: HIGH
 *
 * Implements MCP protocol 2024-11-05 over stdin/stdout (JSON-RPC 2.0).
 * Wraps the Session-Control-Service REST API with 24 secure tools.
 *
 * Tool categories:
 *   Session Lifecycle        (6) — create, get, list, update state, extend, archive
 *   Participant Management   (6) — join, leave, list, heartbeat, evict, update role
 *   State Machine Control    (4) — transition, suspend, resume, terminate
 *   Jitsi Room Management    (3) — get token, mute, force-end
 *   Audit & Compliance       (3) — audit log, session events, export
 *   Health & Monitoring      (2) — health check, active sessions stats
 */
public class McpSessionControlServer {

    private static final Logger log = LoggerFactory.getLogger(McpSessionControlServer.class);
    static final String MCP_VERSION = "2024-11-05";
    static final String SERVER_NAME = "mcp-session-control";
    static final String SERVER_VER  = "1.0.0";

    private final ObjectMapper          json = new ObjectMapper();
    private final SessionControlConfig  cfg;
    private final SessionControlClient  client;
    private final ApiKeyValidator       keyValidator;
    private final AuditLogger           audit;
    private final ToolRegistry          registry;

    public McpSessionControlServer() {
        this.cfg          = SessionControlConfig.fromEnvironment();
        this.client       = new SessionControlClient(cfg);
        this.keyValidator = new ApiKeyValidator(cfg);
        this.audit        = new AuditLogger();
        this.registry     = new ToolRegistry(client, cfg, audit);
    }

    public static void main(String[] args) {
        log.info("Starting {} v{}", SERVER_NAME, SERVER_VER);
        try {
            new McpSessionControlServer().run();
        } catch (Exception e) {
            log.error("Fatal error", e);
            System.exit(1);
        }
    }

    public void run() throws Exception {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

        log.info("MCP Session Control server ready — base URL: {}", cfg.baseUrl);

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.strip();
            if (line.isEmpty()) continue;
            try {
                JsonNode req = json.readTree(line);
                String resp  = handleRequest(req);
                if (resp != null) writer.println(resp);
            } catch (Exception e) {
                log.error("Error processing message", e);
                writer.println(errorResponse(null, -32700, "Parse error: " + e.getMessage()));
            }
        }
        log.info("stdin closed — shutting down");
    }

    // -------------------------------------------------------------------------
    String handleRequest(JsonNode req) throws Exception {
        String   method = req.path("method").asText("");
        JsonNode id     = req.has("id") ? req.get("id") : null;
        JsonNode params = req.path("params");

        return switch (method) {
            case "initialize" -> handleInitialize(id);
            case "tools/list" -> handleToolsList(id);
            case "tools/call" -> handleToolCall(id, params);
            case "ping"       -> successResponse(id, json.createObjectNode());
            default           -> id == null ? null
                                 : errorResponse(id, -32601, "Method not found: " + method);
        };
    }

    private String handleInitialize(JsonNode id) throws Exception {
        ObjectNode caps = json.createObjectNode();
        caps.putObject("tools").put("listChanged", false);
        ObjectNode res  = json.createObjectNode();
        res.put("protocolVersion", MCP_VERSION);
        res.putObject("serverInfo").put("name", SERVER_NAME).put("version", SERVER_VER);
        res.set("capabilities", caps);
        log.info("Client connected — MCP {}", MCP_VERSION);
        return successResponse(id, res);
    }

    private String handleToolsList(JsonNode id) throws Exception {
        ArrayNode tools = json.createArrayNode();
        for (McpTool t : registry.allTools()) tools.add(t.schema());
        ObjectNode res = json.createObjectNode();
        res.set("tools", tools);
        return successResponse(id, res);
    }

    private String handleToolCall(JsonNode id, JsonNode params) throws Exception {
        String   toolName  = params.path("name").asText("");
        JsonNode arguments = params.path("arguments");

        // ── Security: API key check ──────────────────────────────────────────
        String apiKey = arguments.path("api_key").asText("");
        if (!keyValidator.isValid(apiKey)) {
            audit.logUnauthorised(toolName, apiKey);
            return errorResponse(id, -32001, "Unauthorised: invalid or missing api_key");
        }

        McpTool tool = registry.find(toolName);
        if (tool == null) return errorResponse(id, -32002, "Unknown tool: " + toolName);

        try {
            audit.logCall(toolName, arguments);
            ToolResult result = tool.execute(arguments);
            audit.logResult(toolName, result.isError());

            ObjectNode content = json.createObjectNode();
            content.put("type", "text");
            content.put("text", result.content());
            ArrayNode contents = json.createArrayNode();
            contents.add(content);
            ObjectNode resp = json.createObjectNode();
            resp.set("content", contents);
            resp.put("isError", result.isError());
            return successResponse(id, resp);
        } catch (Exception e) {
            log.error("Tool {} error: {}", toolName, e.getMessage(), e);
            audit.logError(toolName, e.getMessage());
            return errorResponse(id, -32003, "Tool execution failed: " + e.getMessage());
        }
    }

    // ── JSON-RPC helpers ──────────────────────────────────────────────────────
    private String successResponse(JsonNode id, JsonNode result) throws Exception {
        ObjectNode r = json.createObjectNode();
        r.put("jsonrpc", "2.0");
        r.set("id",     id != null ? id : json.nullNode());
        r.set("result", result);
        return json.writeValueAsString(r);
    }

    private String errorResponse(JsonNode id, int code, String message) {
        try {
            ObjectNode err = json.createObjectNode();
            err.put("code", code); err.put("message", message);
            ObjectNode r = json.createObjectNode();
            r.put("jsonrpc","2.0");
            r.set("id",    id != null ? id : json.nullNode());
            r.set("error", err);
            return json.writeValueAsString(r);
        } catch (Exception e) {
            return "{\"jsonrpc\":\"2.0\",\"id\":null,\"error\":{\"code\":-32700,\"message\":\"Internal error\"}}";
        }
    }
}
