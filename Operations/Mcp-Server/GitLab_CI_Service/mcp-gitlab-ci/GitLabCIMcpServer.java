package com.ecoskiller.mcp.gitlab.server;

import com.ecoskiller.mcp.gitlab.models.JsonRpc;
import com.ecoskiller.mcp.gitlab.models.ToolDefinition;
import com.ecoskiller.mcp.gitlab.models.ToolResult;
import com.ecoskiller.mcp.gitlab.security.SecurityValidator;
import com.ecoskiller.mcp.gitlab.tools.GitLabCITools;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * GitLabCIMcpServer — Main entrypoint.
 *
 * Transport  : stdio (stdin / stdout), newline-delimited JSON
 * Protocol   : JSON-RPC 2.0
 * MCP Version: 2024-11-05
 * Agents     : 13 GitLab CI tools
 *
 * Security posture:
 *  • All input validated before execution (SecurityValidator)
 *  • Per-tool rate limiting (100 req / 60 s)
 *  • Secrets never logged (masked variables)
 *  • Stack traces never exposed to client
 *  • Input length caps on all string fields
 */
public final class GitLabCIMcpServer {

    private static final Logger log = LoggerFactory.getLogger(GitLabCIMcpServer.class);

    // MCP server metadata
    private static final String SERVER_NAME    = "mcp-gitlab-ci";
    private static final String SERVER_VERSION = "1.0.0";
    private static final String MCP_VERSION    = "2024-11-05";

    private final ObjectMapper      mapper;
    private final SecurityValidator security;
    private final GitLabCITools     tools;

    public GitLabCIMcpServer() {
        this.mapper   = buildMapper();
        this.security = new SecurityValidator();
        this.tools    = new GitLabCITools(security, mapper);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // MAIN — stdio event loop
    // ═══════════════════════════════════════════════════════════════════════

    public static void main(String[] args) {
        log.info("EcoSkiller GitLab CI MCP Server starting — version={} mcp={}", SERVER_VERSION, MCP_VERSION);
        new GitLabCIMcpServer().run();
    }

    private void run() {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in,  StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter   (new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

        log.info("MCP server ready. Listening on stdin...");

        String line;
        try {
            while ((line = in.readLine()) != null) {
                line = line.strip();
                if (line.isEmpty()) continue;
                String response = handleLine(line);
                if (response != null) {
                    out.println(response);
                    log.debug("[OUT] {}", response);
                }
            }
        } catch (IOException e) {
            log.error("Fatal IO error on stdin", e);
            System.exit(1);
        }

        log.info("stdin closed — MCP server shutting down");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // MESSAGE HANDLING
    // ═══════════════════════════════════════════════════════════════════════

    String handleLine(String line) {
        Object requestId = null;
        try {
            JsonNode root = mapper.readTree(line);

            // Validate JSON-RPC envelope
            if (!root.has("jsonrpc") || !"2.0".equals(root.get("jsonrpc").asText())) {
                return errorJson(JsonRpc.INVALID_REQUEST, "jsonrpc must be '2.0'", null);
            }

            requestId = extractId(root);
            String method = root.path("method").asText(null);

            if (method == null) {
                return errorJson(JsonRpc.INVALID_REQUEST, "Missing 'method' field", requestId);
            }

            log.debug("[IN] method={} id={}", method, requestId);

            JsonNode params = root.path("params");

            return switch (method) {
                case "initialize"  -> handleInitialize(params, requestId);
                case "ping"        -> handlePing(requestId);
                case "tools/list"  -> handleToolsList(requestId);
                case "tools/call"  -> handleToolsCall(params, requestId);
                default            -> errorJson(JsonRpc.METHOD_NOT_FOUND,
                                               "Unknown method: " + method, requestId);
            };

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            log.warn("JSON parse error: {}", e.getMessage());
            return errorJson(JsonRpc.PARSE_ERROR, "Invalid JSON: " + e.getOriginalMessage(), requestId);
        } catch (Exception e) {
            log.error("Unhandled error processing request", e);
            // Never expose internal stack traces to client
            return errorJson(JsonRpc.INTERNAL_ERROR, "Internal server error", requestId);
        }
    }

    // ── initialize ────────────────────────────────────────────────────────

    private String handleInitialize(JsonNode params, Object id) throws Exception {
        String clientVersion = params.path("protocolVersion").asText("unknown");
        log.info("Client initializing — protocol={}", clientVersion);

        Map<String, Object> result = Map.of(
            "protocolVersion", MCP_VERSION,
            "serverInfo", Map.of(
                "name",    SERVER_NAME,
                "version", SERVER_VERSION,
                "description", "EcoSkiller GitLab CI/CD MCP Server — 13 pipeline agents"
            ),
            "capabilities", Map.of(
                "tools", Map.of("listChanged", false)
            )
        );
        return successJson(result, id);
    }

    // ── ping ──────────────────────────────────────────────────────────────

    private String handlePing(Object id) throws Exception {
        return successJson(Map.of("status", "pong", "server", SERVER_NAME), id);
    }

    // ── tools/list ────────────────────────────────────────────────────────

    private String handleToolsList(Object id) throws Exception {
        List<ToolDefinition> defs = tools.getAllDefinitions();
        Map<String, Object> result = Map.of("tools", defs);
        return successJson(result, id);
    }

    // ── tools/call ────────────────────────────────────────────────────────

    private String handleToolsCall(JsonNode params, Object id) throws Exception {
        if (params == null || params.isNull()) {
            return errorJson(JsonRpc.INVALID_PARAMS, "tools/call requires params", id);
        }

        String toolName = params.path("name").asText(null);
        if (toolName == null || toolName.isBlank()) {
            return errorJson(JsonRpc.INVALID_PARAMS, "Missing 'name' in params", id);
        }

        // Sanitize tool name itself before logging or dispatching
        if (!toolName.matches("[a-zA-Z0-9_]{1,80}")) {
            return errorJson(JsonRpc.SECURITY_VIOLATION, "Invalid tool name format", id);
        }

        JsonNode arguments = params.path("arguments");

        try {
            ToolResult result = tools.dispatch(toolName, arguments);
            return successJson(result, id);

        } catch (SecurityException se) {
            log.warn("[SECURITY] Tool='{}' blocked: {}", toolName, se.getMessage());
            return errorJson(JsonRpc.SECURITY_VIOLATION, se.getMessage(), id);

        } catch (IllegalArgumentException iae) {
            return errorJson(JsonRpc.INVALID_PARAMS, iae.getMessage(), id);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // JSON HELPERS
    // ═══════════════════════════════════════════════════════════════════════

    private String successJson(Object result, Object id) throws Exception {
        JsonRpc.Response resp = new JsonRpc.Response(result, id);
        return mapper.writeValueAsString(resp);
    }

    private String errorJson(int code, String message, Object id) {
        try {
            return mapper.writeValueAsString(new JsonRpc.ErrorResponse(code, message, id));
        } catch (Exception e) {
            // Absolute last resort — raw JSON
            return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":" + code +
                   ",\"message\":\"" + message.replace("\"", "'") + "\"},\"id\":null}";
        }
    }

    private static Object extractId(JsonNode root) {
        if (!root.has("id") || root.get("id").isNull()) return null;
        JsonNode idNode = root.get("id");
        if (idNode.isNumber()) return idNode.asLong();
        return idNode.asText();
    }

    private static ObjectMapper buildMapper() {
        ObjectMapper m = new ObjectMapper();
        m.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return m;
    }
}
