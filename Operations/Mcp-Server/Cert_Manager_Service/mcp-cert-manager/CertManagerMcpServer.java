package com.ecoskiller.mcp.cert.server;

import com.ecoskiller.mcp.cert.models.JsonRpc;
import com.ecoskiller.mcp.cert.models.ToolDefinition;
import com.ecoskiller.mcp.cert.models.ToolResult;
import com.ecoskiller.mcp.cert.security.SecurityValidator;
import com.ecoskiller.mcp.cert.tools.CertManagerTools;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * CertManagerMcpServer — Main entrypoint.
 *
 * Transport  : stdio (stdin / stdout), newline-delimited JSON
 * Protocol   : JSON-RPC 2.0
 * MCP Version: 2024-11-05
 * Agents     : 12 Cert Manager tools
 *
 * Security posture:
 *  • All inputs validated by SecurityValidator before execution
 *  • Per-tool rate limiting (100 req / 60 s)
 *  • Private keys NEVER in code — Vault only
 *  • No PII on blockchain — hash only (GDPR)
 *  • Stack traces never sent to client
 *  • Tenant isolation enforced at every layer
 *  • Secrets masked in all log output
 */
public final class CertManagerMcpServer {

    private static final Logger log = LoggerFactory.getLogger(CertManagerMcpServer.class);

    private static final String SERVER_NAME    = "mcp-cert-manager";
    private static final String SERVER_VERSION = "1.0.0";
    private static final String MCP_VERSION    = "2024-11-05";

    private final ObjectMapper     mapper;
    private final SecurityValidator security;
    private final CertManagerTools  tools;

    public CertManagerMcpServer() {
        this.mapper   = buildMapper();
        this.security = new SecurityValidator();
        this.tools    = new CertManagerTools(security, mapper);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // MAIN
    // ═══════════════════════════════════════════════════════════════════════

    public static void main(String[] args) {
        log.info("EcoSkiller Cert Manager MCP Server starting — version={} mcp={}",
                SERVER_VERSION, MCP_VERSION);
        new CertManagerMcpServer().run();
    }

    private void run() {
        BufferedReader in  = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(
                new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

        log.info("MCP server ready — listening on stdin");

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
        log.info("stdin closed — shutting down");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // MESSAGE HANDLING
    // ═══════════════════════════════════════════════════════════════════════

    String handleLine(String line) {
        Object requestId = null;
        try {
            JsonNode root = mapper.readTree(line);

            if (!root.has("jsonrpc") || !"2.0".equals(root.get("jsonrpc").asText()))
                return errorJson(JsonRpc.INVALID_REQUEST, "jsonrpc must be '2.0'", null);

            requestId = extractId(root);
            String method = root.path("method").asText(null);

            if (method == null)
                return errorJson(JsonRpc.INVALID_REQUEST, "Missing 'method' field", requestId);

            log.debug("[IN] method={} id={}", method, requestId);

            JsonNode params = root.path("params");

            return switch (method) {
                case "initialize" -> handleInitialize(params, requestId);
                case "ping"       -> handlePing(requestId);
                case "tools/list" -> handleToolsList(requestId);
                case "tools/call" -> handleToolsCall(params, requestId);
                default           -> errorJson(JsonRpc.METHOD_NOT_FOUND,
                                              "Unknown method: " + method, requestId);
            };

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            log.warn("JSON parse error: {}", e.getMessage());
            return errorJson(JsonRpc.PARSE_ERROR, "Invalid JSON: " + e.getOriginalMessage(), requestId);
        } catch (Exception e) {
            log.error("Unhandled error", e);
            return errorJson(JsonRpc.INTERNAL_ERROR, "Internal server error", requestId);
        }
    }

    // ── initialize ────────────────────────────────────────────────────────

    private String handleInitialize(JsonNode params, Object id) throws Exception {
        String clientVersion = params.path("protocolVersion").asText("unknown");
        log.info("Client initializing — protocolVersion={}", clientVersion);

        Map<String, Object> result = Map.of(
            "protocolVersion", MCP_VERSION,
            "serverInfo", Map.of(
                "name",        SERVER_NAME,
                "version",     SERVER_VERSION,
                "description", "EcoSkiller Cert Manager MCP Server — 12 certificate lifecycle agents"
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
        return successJson(Map.of("tools", defs), id);
    }

    // ── tools/call ────────────────────────────────────────────────────────

    private String handleToolsCall(JsonNode params, Object id) throws Exception {
        if (params == null || params.isNull())
            return errorJson(JsonRpc.INVALID_PARAMS, "tools/call requires params", id);

        String toolName = params.path("name").asText(null);
        if (toolName == null || toolName.isBlank())
            return errorJson(JsonRpc.INVALID_PARAMS, "Missing 'name' in params", id);

        // Tool name allowlist format check before any dispatch
        if (!toolName.matches("[a-zA-Z0-9_]{1,80}"))
            return errorJson(JsonRpc.SECURITY_VIOLATION, "Invalid tool name format", id);

        JsonNode arguments = params.path("arguments");

        try {
            ToolResult result = tools.dispatch(toolName, arguments);
            return successJson(result, id);

        } catch (SecurityException se) {
            log.warn("[SECURITY] Tool='{}' blocked: {}", toolName,
                    SecurityValidator.sanitizeForLog(se.getMessage()));
            return errorJson(JsonRpc.SECURITY_VIOLATION, se.getMessage(), id);

        } catch (IllegalArgumentException iae) {
            return errorJson(JsonRpc.INVALID_PARAMS, iae.getMessage(), id);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // JSON HELPERS
    // ═══════════════════════════════════════════════════════════════════════

    private String successJson(Object result, Object id) throws Exception {
        return mapper.writeValueAsString(new JsonRpc.Response(result, id));
    }

    private String errorJson(int code, String message, Object id) {
        try {
            return mapper.writeValueAsString(new JsonRpc.ErrorResponse(code, message, id));
        } catch (Exception e) {
            return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":" + code +
                   ",\"message\":\"" + message.replace("\"", "'") + "\"},\"id\":null}";
        }
    }

    private static Object extractId(JsonNode root) {
        if (!root.has("id") || root.get("id").isNull()) return null;
        JsonNode n = root.get("id");
        return n.isNumber() ? n.asLong() : n.asText();
    }

    private static ObjectMapper buildMapper() {
        ObjectMapper m = new ObjectMapper();
        m.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return m;
    }
}
