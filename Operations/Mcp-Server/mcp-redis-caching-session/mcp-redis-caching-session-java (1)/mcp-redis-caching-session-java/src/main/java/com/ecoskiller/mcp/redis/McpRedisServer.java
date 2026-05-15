package com.ecoskiller.mcp.redis;

import com.ecoskiller.mcp.redis.config.RedisConfig;
import com.ecoskiller.mcp.redis.security.ApiKeyValidator;
import com.ecoskiller.mcp.redis.security.AuditLogger;
import com.ecoskiller.mcp.redis.tools.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Ecoskiller — Redis MCP Server (Java)
 * CAT-INFRA-02 | Priority: HIGH
 *
 * Implements MCP protocol version 2024-11-05 over stdin/stdout (JSON-RPC 2.0).
 * Provides 20 secure tools for Redis caching and session management.
 *
 * Security features:
 *  - API key validation on every tool call
 *  - TLS enforced for Redis connection (production)
 *  - Input sanitisation — no raw key injection
 *  - Namespace enforcement — agents can only access their own prefix
 *  - Rate limiting per tool call (in-process sliding window)
 *  - Full audit logging to stderr / logback
 *  - No secrets logged
 */
public class McpRedisServer {

    private static final Logger log = LoggerFactory.getLogger(McpRedisServer.class);
    private static final String MCP_VERSION  = "2024-11-05";
    private static final String SERVER_NAME  = "mcp-redis-caching-session";
    private static final String SERVER_VER   = "1.0.0";

    private final ObjectMapper       json    = new ObjectMapper();
    private final RedisConfig        redisCfg;
    private final ApiKeyValidator    keyValidator;
    private final AuditLogger        audit;
    private final ToolRegistry       registry;

    public McpRedisServer() {
        this.redisCfg     = RedisConfig.fromEnvironment();
        this.keyValidator = new ApiKeyValidator();
        this.audit        = new AuditLogger();
        this.registry     = new ToolRegistry(redisCfg, audit);
    }

    public static void main(String[] args) {
        log.info("Starting {} v{}", SERVER_NAME, SERVER_VER);
        try {
            new McpRedisServer().run();
        } catch (Exception e) {
            log.error("Fatal server error", e);
            System.exit(1);
        }
    }

    /** Main event loop — reads JSON-RPC 2.0 messages from stdin, writes to stdout. */
    public void run() throws Exception {
        // Use line-buffered streams for JSON-RPC communication
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

        log.info("MCP server ready — listening on stdin");

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.strip();
            if (line.isEmpty()) continue;

            try {
                JsonNode request = json.readTree(line);
                String response  = handleRequest(request);
                if (response != null) {
                    writer.println(response);
                }
            } catch (Exception e) {
                log.error("Error processing message: {}", e.getMessage(), e);
                writer.println(errorResponse(null, -32700, "Parse error: " + e.getMessage()));
            }
        }
        log.info("stdin closed — shutting down");
    }

    // -------------------------------------------------------------------------
    // Request dispatcher
    // -------------------------------------------------------------------------

    private String handleRequest(JsonNode req) throws Exception {
        String  method = req.path("method").asText("");
        JsonNode id    = req.has("id") ? req.get("id") : null;
        JsonNode params = req.path("params");

        log.debug("Received method={}", method);

        return switch (method) {
            case "initialize"   -> handleInitialize(id, params);
            case "tools/list"   -> handleToolsList(id);
            case "tools/call"   -> handleToolCall(id, params);
            case "ping"         -> successResponse(id, json.createObjectNode());
            default             -> {
                // Notifications (no id) do not need a response
                if (id == null) yield null;
                yield errorResponse(id, -32601, "Method not found: " + method);
            }
        };
    }

    // -------------------------------------------------------------------------
    // initialize
    // -------------------------------------------------------------------------

    private String handleInitialize(JsonNode id, JsonNode params) throws Exception {
        ObjectNode caps = json.createObjectNode();
        caps.putObject("tools").put("listChanged", false);

        ObjectNode result = json.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);
        result.putObject("serverInfo")
              .put("name",    SERVER_NAME)
              .put("version", SERVER_VER);
        result.set("capabilities", caps);

        log.info("Client connected — protocolVersion={}", MCP_VERSION);
        return successResponse(id, result);
    }

    // -------------------------------------------------------------------------
    // tools/list
    // -------------------------------------------------------------------------

    private String handleToolsList(JsonNode id) throws Exception {
        ArrayNode tools = json.createArrayNode();
        for (McpTool tool : registry.allTools()) {
            tools.add(tool.schema());
        }
        ObjectNode result = json.createObjectNode();
        result.set("tools", tools);
        return successResponse(id, result);
    }

    // -------------------------------------------------------------------------
    // tools/call — with API key validation + audit logging
    // -------------------------------------------------------------------------

    private String handleToolCall(JsonNode id, JsonNode params) throws Exception {
        String   toolName  = params.path("name").asText("");
        JsonNode arguments = params.path("arguments");

        // ── Security: validate API key on every call ─────────────────────────
        String apiKey = arguments.path("api_key").asText("");
        if (!keyValidator.isValid(apiKey)) {
            audit.logUnauthorised(toolName, apiKey);
            return errorResponse(id, -32001, "Unauthorised: invalid or missing api_key");
        }

        // ── Dispatch to tool ──────────────────────────────────────────────────
        McpTool tool = registry.find(toolName);
        if (tool == null) {
            return errorResponse(id, -32002, "Unknown tool: " + toolName);
        }

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
            log.error("Tool {} threw exception: {}", toolName, e.getMessage(), e);
            audit.logError(toolName, e.getMessage());
            return errorResponse(id, -32003, "Tool execution failed: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // JSON-RPC helpers
    // -------------------------------------------------------------------------

    private String successResponse(JsonNode id, JsonNode result) throws Exception {
        ObjectNode resp = json.createObjectNode();
        resp.put("jsonrpc", "2.0");
        resp.set("id", id != null ? id : json.nullNode());
        resp.set("result", result);
        return json.writeValueAsString(resp);
    }

    private String errorResponse(JsonNode id, int code, String message) {
        try {
            ObjectNode err = json.createObjectNode();
            err.put("code",    code);
            err.put("message", message);
            ObjectNode resp = json.createObjectNode();
            resp.put("jsonrpc", "2.0");
            resp.set("id",     id != null ? id : json.nullNode());
            resp.set("error",  err);
            return json.writeValueAsString(resp);
        } catch (Exception e) {
            // Fallback — should never happen
            return "{\"jsonrpc\":\"2.0\",\"id\":null,\"error\":{\"code\":-32700,\"message\":\"Internal error\"}}";
        }
    }
}
