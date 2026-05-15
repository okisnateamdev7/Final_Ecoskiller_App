package com.ecoskiller.mcp.protocol;

import com.ecoskiller.mcp.tools.ToolRouter;

import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Logger;

/**
 * JSON-RPC 2.0 protocol handler for the MCP stdio transport.
 *
 * Handles:
 *   initialize       — capability negotiation
 *   ping             — health check
 *   tools/list       — enumerate all 16 tools
 *   tools/call       — execute a specific tool
 *
 * Every response is written as a single-line JSON object followed by '\n'.
 * All internal logs go to stderr; stdout is reserved for MCP messages only.
 */
public class MCPProtocolHandler {

    private static final Logger LOG = Logger.getLogger(MCPProtocolHandler.class.getName());

    private final PrintWriter out;
    private final ToolRouter  toolRouter;
    private boolean           initialized = false;

    public MCPProtocolHandler(PrintWriter out) {
        this.out        = out;
        this.toolRouter = new ToolRouter();
    }

    // -------------------------------------------------------------------------
    // Public entry point
    // -------------------------------------------------------------------------

    public void handle(String rawJson) {
        String id     = "null";
        String method = "";
        try {
            Map<String, Object> req = JsonUtil.parseObject(rawJson);
            id     = JsonUtil.getString(req, "id", "null");
            method = JsonUtil.getString(req, "method", "");

            LOG.fine("RPC method=" + method + " id=" + id);

            switch (method) {
                case "initialize":      handleInitialize(id, req); break;
                case "ping":            sendResult(id, JsonUtil.obj("pong", "true")); break;
                case "tools/list":      handleToolsList(id); break;
                case "tools/call":      handleToolsCall(id, req); break;
                case "notifications/initialized": /* no-op */ break;
                default:
                    sendError(id, -32601, "Method not found: " + method, null);
            }

        } catch (Exception e) {
            LOG.warning("RPC error id=" + id + " method=" + method + " : " + e.getMessage());
            sendError(id, -32603, "Internal error: " + e.getMessage(), null);
        }
    }

    // -------------------------------------------------------------------------
    // Method handlers
    // -------------------------------------------------------------------------

    private void handleInitialize(String id, Map<String, Object> req) {
        initialized = true;
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("protocolVersion", com.ecoskiller.mcp.ApplicationMCPServer.MCP_VERSION);
        result.put("capabilities", JsonUtil.obj("tools", "{}"));

        Map<String, Object> info = new LinkedHashMap<>();
        info.put("name",    com.ecoskiller.mcp.ApplicationMCPServer.SERVER_NAME);
        info.put("version", com.ecoskiller.mcp.ApplicationMCPServer.SERVER_VERSION);
        result.put("serverInfo", info);

        sendResult(id, result);
    }

    private void handleToolsList(String id) {
        sendResult(id, JsonUtil.obj("tools", toolRouter.getToolsDefinition()));
    }

    @SuppressWarnings("unchecked")
    private void handleToolsCall(String id, Map<String, Object> req) {
        if (!initialized) {
            sendError(id, -32002, "Server not initialized", null);
            return;
        }
        Map<String, Object> params   = (Map<String, Object>) req.getOrDefault("params", Collections.emptyMap());
        String toolName              = JsonUtil.getString(params, "name", "");
        Map<String, Object> args     = (Map<String, Object>) params.getOrDefault("arguments", Collections.emptyMap());

        if (toolName.isEmpty()) {
            sendError(id, -32602, "Missing tool name", null);
            return;
        }

        try {
            String toolResult = toolRouter.dispatch(toolName, args);
            Map<String, Object> content = new LinkedHashMap<>();
            content.put("type", "text");
            content.put("text", toolResult);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("content", Collections.singletonList(content));
            sendResult(id, result);

        } catch (IllegalArgumentException e) {
            sendError(id, -32602, e.getMessage(), null);
        } catch (SecurityException e) {
            sendError(id, -32001, "Security violation: " + e.getMessage(), null);
        }
    }

    // -------------------------------------------------------------------------
    // Response helpers
    // -------------------------------------------------------------------------

    private void sendResult(String id, Object result) {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("jsonrpc", "2.0");
        resp.put("id",      parseId(id));
        resp.put("result",  result);
        emit(JsonUtil.toJson(resp));
    }

    private void sendError(String id, int code, String message, Object data) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("code",    code);
        error.put("message", message);
        if (data != null) error.put("data", data);

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("jsonrpc", "2.0");
        resp.put("id",      parseId(id));
        resp.put("error",   error);
        emit(JsonUtil.toJson(resp));
    }

    /** Write exactly one line to stdout — the MCP transport boundary. */
    private void emit(String line) {
        out.println(line);
        out.flush();
    }

    /** IDs can be integer or string in JSON-RPC 2.0. */
    private Object parseId(String raw) {
        if ("null".equals(raw)) return null;
        try { return Integer.parseInt(raw); }
        catch (NumberFormatException e) { return raw; }
    }
}
