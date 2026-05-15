package com.ecoskiller.mcp.metallb.server;

import com.ecoskiller.mcp.metallb.json.Json;
import com.ecoskiller.mcp.metallb.security.SecurityManager;
import com.ecoskiller.mcp.metallb.tools.ToolRegistry;
import com.ecoskiller.mcp.metallb.tools.ToolNotFoundException;

import java.io.*;
import java.util.logging.*;

/**
 * MetalLBMcpServer — Main entry point
 * Ecoskiller CAT-OPS | Bare-Metal Load Balancer MCP Server
 *
 * Transport : stdio (stdin/stdout)
 * Protocol  : JSON-RPC 2.0
 * MCP Ver   : 2024-11-05
 * Tools     : 20
 * Security  : Input validation, whitelist, injection prevention
 */
public class MetalLBMcpServer {

    private static final String MCP_VERSION  = "2024-11-05";
    private static final String SERVER_NAME  = "mcp-metallb";
    private static final String SERVER_VER   = "1.0.0";

    private final SecurityManager security = new SecurityManager();
    private final ToolRegistry    registry = new ToolRegistry();
    private boolean initialized = false;

    public static void main(String[] args) throws Exception {
        configureLogging();
        new MetalLBMcpServer().run();
    }

    private static void configureLogging() {
        // All logs → stderr only; stdout is reserved for JSON-RPC
        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.WARNING);
        root.addHandler(ch);
        root.setLevel(Level.WARNING);
    }

    public void run() throws Exception {
        registry.registerAll();

        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                Json.Node parsed = Json.parse(line);
                // Security: validate JSON-RPC envelope
                if (!security.validateRequest(parsed)) {
                    sendError(out, null, -32600, "Invalid Request: security validation failed");
                    continue;
                }
                Json.Obj req = (Json.Obj) parsed;
                String response = handleRequest(req);
                if (response != null) out.println(response);
            } catch (Exception e) {
                System.err.println("[mcp-metallb] Parse error: " + e.getMessage());
                sendError(out, null, -32700, "Parse error");
            }
        }
    }

    private String handleRequest(Json.Obj req) {
        String method = Json.strField(req, "method", "");
        Json.Node idNode = req.get("id");
        String id = idNode != null ? idNode.toJson() : "null";
        Json.Obj params = req.obj("params");

        return switch (method) {
            case "initialize"                -> handleInitialize(id, params);
            case "tools/list"                -> handleToolsList(id);
            case "tools/call"                -> handleToolCall(id, params);
            case "ping"                      -> buildResult(id, Json.obj());
            case "notifications/initialized" -> null;
            default -> buildError(id, -32601, "Method not found: " + method);
        };
    }

    private String handleInitialize(String id, Json.Obj params) {
        String clientVer = params != null ? Json.strField(params, "protocolVersion", "") : "";
        if (!security.isAllowedProtocolVersion(clientVer)) {
            return buildError(id, -32600, "Unsupported protocol version: " + clientVer);
        }
        initialized = true;

        Json.Obj serverInfo = Json.obj().put("name", SERVER_NAME).put("version", SERVER_VER);
        Json.Obj capabilities = Json.obj().put("tools", Json.obj());
        Json.Obj result = Json.obj()
            .put("protocolVersion", MCP_VERSION)
            .put("serverInfo", serverInfo)
            .put("capabilities", capabilities);
        return buildResult(id, result);
    }

    private String handleToolsList(String id) {
        if (!initialized) return buildError(id, -32002, "Server not initialized");
        Json.Arr tools = Json.arr();
        for (Json.Obj def : registry.getToolDefinitions()) tools.add(def);
        return buildResult(id, Json.obj().put("tools", tools));
    }

    private String handleToolCall(String id, Json.Obj params) {
        if (!initialized) return buildError(id, -32002, "Server not initialized");
        if (params == null) return buildError(id, -32602, "Missing params");

        String toolName = Json.strField(params, "name", "");
        Json.Node argsNode = params.get("arguments");
        Json.Obj args = (argsNode instanceof Json.Obj o) ? o : Json.obj();

        // Security: validate tool name and sanitize arguments
        if (!security.validateToolName(toolName)) {
            return buildError(id, -32602, "Invalid or unknown tool: " + toolName);
        }
        if (!security.sanitizeArguments(args)) {
            return buildError(id, -32602, "Invalid or unsafe arguments detected");
        }

        try {
            String text = registry.callTool(toolName, args);
            Json.Obj content = Json.obj().put("type", "text").put("text", text);
            Json.Arr contentArr = Json.arr().add(content);
            Json.Obj result = Json.obj().put("content", contentArr).put("isError", false);
            return buildResult(id, result);
        } catch (ToolNotFoundException e) {
            return buildError(id, -32601, "Tool not found: " + toolName);
        } catch (Exception e) {
            System.err.println("[mcp-metallb] Tool error [" + toolName + "]: " + e.getMessage());
            Json.Obj content = Json.obj().put("type", "text").put("text", "Error: " + e.getMessage());
            Json.Arr contentArr = Json.arr().add(content);
            Json.Obj result = Json.obj().put("content", contentArr).put("isError", true);
            return buildResult(id, result);
        }
    }

    // ── JSON-RPC response builders ───────────────────────────────────────

    private String buildResult(String id, Json.Obj result) {
        return Json.obj()
            .put("jsonrpc", "2.0")
            .put("id", rawNode(id))
            .put("result", result)
            .toJson();
    }

    private String buildError(String id, int code, String message) {
        Json.Obj error = Json.obj().put("code", (long) code).put("message", message);
        return Json.obj()
            .put("jsonrpc", "2.0")
            .put("id", rawNode(id))
            .put("error", error)
            .toJson();
    }

    private void sendError(PrintWriter out, String id, int code, String message) {
        out.println(buildError(id != null ? id : "null", code, message));
    }

    /** Wrap a pre-serialised JSON fragment as a Node (for id field which may be number/string/null) */
    private Json.Node rawNode(String raw) {
        if (raw == null || raw.equals("null")) return new Json.Null();
        // Already serialised — emit as-is
        return new Json.Node() { public String toJson() { return raw; } };
    }
}
