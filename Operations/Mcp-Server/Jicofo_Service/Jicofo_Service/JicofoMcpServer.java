package com.ecoskiller.mcp.jicofo.server;

import com.ecoskiller.mcp.jicofo.security.JwtValidator;
import com.ecoskiller.mcp.jicofo.security.RateLimiter;
import com.ecoskiller.mcp.jicofo.security.AuditLogger;
import com.ecoskiller.mcp.jicofo.tools.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Jicofo MCP Server — Ecoskiller Platform
 *
 * Implements Model Context Protocol (MCP) v2024-11-05 over stdio (JSON-RPC 2.0).
 * Provides 12 tools covering the full Jicofo WebRTC conference management lifecycle:
 *   - Room lifecycle management
 *   - Participant join/leave coordination
 *   - JVB bridge selection & health monitoring
 *   - JWT token validation
 *   - PSTN bridge integration
 *   - Session state management (Redis keys)
 *   - Speaking turn enforcement
 *   - Bridge failover orchestration
 *   - Audio-only mode control
 *   - Observability (health, metrics)
 *
 * Security:
 *   - All tool calls require a valid bearer JWT (CALLER_JWT env var or per-request)
 *   - Rate limiting: 100 req/min per caller identity
 *   - Full audit log of every tool invocation
 *   - Input sanitization on all parameters
 */
public class JicofoMcpServer {

    private static final Logger LOG = Logger.getLogger(JicofoMcpServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "jicofo-mcp";
    private static final String SERVER_VERSION = "1.0.0";

    private final ObjectMapper mapper = new ObjectMapper();
    private final ToolRegistry toolRegistry;
    private final JwtValidator jwtValidator;
    private final RateLimiter rateLimiter;
    private final AuditLogger auditLogger;

    public JicofoMcpServer() {
        this.jwtValidator  = new JwtValidator();
        this.rateLimiter   = new RateLimiter(100, 60_000); // 100 req/min
        this.auditLogger   = new AuditLogger();
        this.toolRegistry  = new ToolRegistry();
        registerTools();
    }

    // ─── Tool Registration ───────────────────────────────────────────────────

    private void registerTools() {
        toolRegistry.register(new RoomLifecycleTool());
        toolRegistry.register(new ParticipantManagementTool());
        toolRegistry.register(new JvbBridgeSelectionTool());
        toolRegistry.register(new JvbHealthMonitorTool());
        toolRegistry.register(new JwtTokenValidationTool(jwtValidator));
        toolRegistry.register(new SessionStateTool());
        toolRegistry.register(new SpeakingTurnEnforcementTool());
        toolRegistry.register(new BridgeFailoverTool());
        toolRegistry.register(new AudioOnlyModeTool());
        toolRegistry.register(new PstnBridgeTool());
        toolRegistry.register(new HealthStatusTool());
        toolRegistry.register(new MetricsTool());
    }

    // ─── Main Loop ────────────────────────────────────────────────────────────

    public void run() throws IOException {
        LOG.info("[jicofo-mcp] Server starting — MCP " + MCP_VERSION);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter writer  = new PrintWriter(new BufferedOutputStream(System.out), true)) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String response = handleRequest(line);
                if (response != null) {
                    writer.println(response);
                    writer.flush();
                }
            }
        }
        LOG.info("[jicofo-mcp] Server shutting down");
    }

    // ─── Request Dispatch ────────────────────────────────────────────────────

    private String handleRequest(String rawJson) {
        JsonNode request;
        try {
            request = mapper.readTree(rawJson);
        } catch (Exception e) {
            return buildParseError();
        }

        String id     = request.has("id")     ? request.get("id").asText()     : null;
        String method = request.has("method") ? request.get("method").asText() : "";
        JsonNode params = request.has("params") ? request.get("params") : mapper.createObjectNode();

        try {
            return switch (method) {
                case "initialize"  -> handleInitialize(id, params);
                case "ping"        -> buildResult(id, mapper.createObjectNode());
                case "tools/list"  -> handleToolsList(id);
                case "tools/call"  -> handleToolCall(id, params);
                default            -> buildMethodNotFound(id, method);
            };
        } catch (Exception e) {
            LOG.severe("[jicofo-mcp] Internal error: " + e.getMessage());
            return buildInternalError(id, e.getMessage());
        }
    }

    // ─── MCP Handlers ────────────────────────────────────────────────────────

    private String handleInitialize(String id, JsonNode params) {
        ObjectNode capabilities = mapper.createObjectNode();
        ObjectNode toolsCap     = mapper.createObjectNode();
        capabilities.set("tools", toolsCap);

        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);
        result.set("capabilities", capabilities);

        ObjectNode serverInfo = mapper.createObjectNode();
        serverInfo.put("name",    SERVER_NAME);
        serverInfo.put("version", SERVER_VERSION);
        result.set("serverInfo", serverInfo);

        return buildResult(id, result);
    }

    private String handleToolsList(String id) {
        ArrayNode tools = mapper.createArrayNode();
        for (McpTool tool : toolRegistry.getAll()) {
            tools.add(tool.getToolDefinition(mapper));
        }
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", tools);
        return buildResult(id, result);
    }

    private String handleToolCall(String id, JsonNode params) {
        String toolName = params.has("name") ? params.get("name").asText() : "";
        JsonNode args   = params.has("arguments") ? params.get("arguments") : mapper.createObjectNode();

        // ── Security: JWT validation ──────────────────────────────────────────
        String callerJwt = extractCallerJwt(args);
        if (!jwtValidator.validate(callerJwt)) {
            auditLogger.log(toolName, "DENIED", "invalid_jwt", args);
            return buildError(id, -32001, "Unauthorized: invalid or missing caller JWT");
        }

        String callerId = jwtValidator.extractSubject(callerJwt);

        // ── Security: Rate limiting ───────────────────────────────────────────
        if (!rateLimiter.allow(callerId)) {
            auditLogger.log(toolName, "RATE_LIMITED", callerId, args);
            return buildError(id, -32002, "Rate limit exceeded: max 100 requests/minute");
        }

        // ── Tool dispatch ─────────────────────────────────────────────────────
        McpTool tool = toolRegistry.get(toolName);
        if (tool == null) {
            auditLogger.log(toolName, "NOT_FOUND", callerId, args);
            return buildMethodNotFound(id, toolName);
        }

        auditLogger.log(toolName, "INVOKED", callerId, args);

        try {
            ToolResult toolResult = tool.execute(args);
            auditLogger.log(toolName, toolResult.isError() ? "ERROR" : "SUCCESS", callerId, args);
            return buildToolResult(id, toolResult);
        } catch (Exception e) {
            auditLogger.log(toolName, "EXCEPTION", callerId, args);
            return buildInternalError(id, e.getMessage());
        }
    }

    // ─── JWT Extraction ───────────────────────────────────────────────────────

    private String extractCallerJwt(JsonNode args) {
        // 1. From request argument
        if (args.has("caller_jwt")) return args.get("caller_jwt").asText();
        // 2. From environment variable (server-level auth for trusted callers)
        String envJwt = System.getenv("JICOFO_MCP_CALLER_JWT");
        return envJwt != null ? envJwt : "";
    }

    // ─── JSON-RPC Response Builders ───────────────────────────────────────────

    private String buildResult(String id, JsonNode result) {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.put("id", id);
        resp.set("result", result);
        return toJson(resp);
    }

    private String buildToolResult(String id, ToolResult toolResult) {
        ObjectNode result = mapper.createObjectNode();
        ArrayNode  content = mapper.createArrayNode();
        ObjectNode textBlock = mapper.createObjectNode();
        textBlock.put("type", "text");
        textBlock.put("text", toolResult.getContent());
        content.add(textBlock);
        result.set("content", content);
        result.put("isError", toolResult.isError());
        return buildResult(id, result);
    }

    private String buildError(String id, int code, String message) {
        ObjectNode resp  = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.put("id", id);
        ObjectNode error = mapper.createObjectNode();
        error.put("code",    code);
        error.put("message", message);
        resp.set("error", error);
        return toJson(resp);
    }

    private String buildParseError()              { return buildError(null, -32700, "Parse error"); }
    private String buildMethodNotFound(String id, String m) { return buildError(id, -32601, "Method not found: " + m); }
    private String buildInternalError(String id, String msg){ return buildError(id, -32603, "Internal error: " + msg); }

    private String toJson(ObjectNode node) {
        try { return mapper.writeValueAsString(node); }
        catch (Exception e) { return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32603,\"message\":\"Serialization error\"}}"; }
    }

    // ─── Entry Point ─────────────────────────────────────────────────────────

    public static void main(String[] args) throws IOException {
        new JicofoMcpServer().run();
    }
}
