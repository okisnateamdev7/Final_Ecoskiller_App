package com.ecoskiller.mcp.protocol;

import com.ecoskiller.mcp.tools.AuthToolRouter;

import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Logger;

/** JSON-RPC 2.0 protocol handler — routes to AuthToolRouter. */
public class MCPProtocolHandler {

    private static final Logger LOG = Logger.getLogger(MCPProtocolHandler.class.getName());
    private final PrintWriter   out;
    private final AuthToolRouter router;
    private boolean             initialized = false;

    public MCPProtocolHandler(PrintWriter out) {
        this.out    = out;
        this.router = new AuthToolRouter();
    }

    public void handle(String rawJson) {
        String id = "null"; String method = "";
        try {
            Map<String, Object> req = JsonUtil.parseObject(rawJson);
            id     = JsonUtil.getString(req, "id", "null");
            method = JsonUtil.getString(req, "method", "");

            switch (method) {
                case "initialize":
                    initialized = true;
                    Map<String, Object> res = new LinkedHashMap<>();
                    res.put("protocolVersion", com.ecoskiller.mcp.AuthServiceMCPServer.MCP_VERSION);
                    res.put("capabilities", JsonUtil.obj("tools", "{}"));
                    Map<String, Object> info = new LinkedHashMap<>();
                    info.put("name",    com.ecoskiller.mcp.AuthServiceMCPServer.SERVER_NAME);
                    info.put("version", com.ecoskiller.mcp.AuthServiceMCPServer.SERVER_VERSION);
                    res.put("serverInfo", info);
                    sendResult(id, res);
                    break;

                case "ping":
                    sendResult(id, JsonUtil.obj("pong", "true")); break;

                case "tools/list":
                    sendResult(id, JsonUtil.obj("tools", router.getToolsDefinition())); break;

                case "tools/call":
                    if (!initialized) { sendError(id, -32002, "Server not initialized", null); break; }
                    @SuppressWarnings("unchecked")
                    Map<String, Object> params = (Map<String, Object>) req.getOrDefault("params", Collections.emptyMap());
                    String toolName = JsonUtil.getString(params, "name", "");
                    @SuppressWarnings("unchecked")
                    Map<String, Object> toolArgs = (Map<String, Object>) params.getOrDefault("arguments", Collections.emptyMap());
                    if (toolName.isEmpty()) { sendError(id, -32602, "Missing tool name", null); break; }
                    try {
                        String result = router.dispatch(toolName, toolArgs);
                        Map<String, Object> content = new LinkedHashMap<>();
                        content.put("type", "text"); content.put("text", result);
                        Map<String, Object> toolRes = new LinkedHashMap<>();
                        toolRes.put("content", Collections.singletonList(content));
                        sendResult(id, toolRes);
                    } catch (IllegalArgumentException e) {
                        sendError(id, -32602, e.getMessage(), null);
                    } catch (SecurityException e) {
                        sendError(id, -32001, "Security violation: " + e.getMessage(), null);
                    }
                    break;

                case "notifications/initialized": break;
                default: sendError(id, -32601, "Method not found: " + method, null);
            }
        } catch (Exception e) {
            LOG.warning("RPC error id=" + id + ": " + e.getMessage());
            sendError(id, -32603, "Internal error: " + e.getMessage(), null);
        }
    }

    private void sendResult(String id, Object result) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("jsonrpc","2.0"); r.put("id", parseId(id)); r.put("result", result);
        emit(JsonUtil.toJson(r));
    }
    private void sendError(String id, int code, String message, Object data) {
        Map<String, Object> err = new LinkedHashMap<>();
        err.put("code", code); err.put("message", message);
        if (data != null) err.put("data", data);
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("jsonrpc","2.0"); r.put("id", parseId(id)); r.put("error", err);
        emit(JsonUtil.toJson(r));
    }
    private void emit(String line) { out.println(line); out.flush(); }
    private Object parseId(String raw) {
        if ("null".equals(raw)) return null;
        try { return Integer.parseInt(raw); } catch (NumberFormatException e) { return raw; }
    }
}
