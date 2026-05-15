package com.ecoskiller.mcp.server;

import com.ecoskiller.mcp.json.JsonValue;
import com.ecoskiller.mcp.json.JsonValue.*;
import com.ecoskiller.mcp.security.SecurityManager;
import com.ecoskiller.mcp.tools.ToolRegistry;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Ecoskiller Job Service — MCP Server (Java 17)
 *
 * Transport  : stdio (JSON-RPC 2.0)
 * MCP Version: 2024-11-05
 * Security   : Keycloak JWT required on every tool call
 * Tools      : 16 agents covering the full job lifecycle
 */
public class JobServiceMcpServer {

    private static final Logger log = Logger.getLogger(JobServiceMcpServer.class.getName());

    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "ecoskiller-job-service";
    private static final String SERVER_VER  = "1.0.0";

    private final SecurityManager security = new SecurityManager();
    private final ToolRegistry    registry = new ToolRegistry(security);

    // ── Entry point ───────────────────────────────────────────────────────
    public static void main(String[] args) {
        log.info("Ecoskiller Job Service MCP Server " + SERVER_VER + " starting...");
        new JobServiceMcpServer().run();
    }

    // ── stdio event loop ──────────────────────────────────────────────────
    private void run() {
        Scanner     scanner = new Scanner(new InputStreamReader(System.in,  StandardCharsets.UTF_8));
        PrintWriter out     = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            try {
                JsonValue parsed = JsonValue.parse(line);
                if (!(parsed instanceof JsonObject req))
                    throw new IllegalArgumentException("Request must be a JSON object");
                String response = dispatch(req);
                if (response != null) out.println(response);
            } catch (Exception e) {
                out.println(error(null, -32700, "Parse error: " + clean(e.getMessage())));
            }
        }
    }

    // ── JSON-RPC 2.0 dispatch ─────────────────────────────────────────────
    private String dispatch(JsonObject req) {
        JsonValue id     = req.get("id");
        String    method = req.getString("method");
        if (method == null) method = "";

        try {
            return switch (method) {
                case "initialize"  -> handleInitialize(id);
                case "ping"        -> ok(id, JsonValue.object());
                case "tools/list"  -> handleToolsList(id);
                case "tools/call"  -> handleToolCall(id, req.getObject("params"));
                default            -> error(id, -32601, "Method not found: " + method);
            };
        } catch (SecurityException se) {
            return error(id, -32001, "Security: " + clean(se.getMessage()));
        } catch (IllegalArgumentException iae) {
            return error(id, -32602, "Invalid params: " + clean(iae.getMessage()));
        } catch (Exception e) {
            log.severe("Internal error in [" + method + "]: " + e);
            return error(id, -32603, "Internal server error");
        }
    }

    // ── initialize ────────────────────────────────────────────────────────
    private String handleInitialize(JsonValue id) {
        return ok(id, JsonValue.object()
            .put("protocolVersion", MCP_VERSION)
            .set("capabilities",   JsonValue.object().set("tools", JsonValue.object()))
            .set("serverInfo",     JsonValue.object().put("name", SERVER_NAME).put("version", SERVER_VER)));
    }

    // ── tools/list ────────────────────────────────────────────────────────
    private String handleToolsList(JsonValue id) {
        return ok(id, JsonValue.object().set("tools", registry.getToolSchemas()));
    }

    // ── tools/call ────────────────────────────────────────────────────────
    private String handleToolCall(JsonValue id, JsonObject params) {
        if (params == null)
            return error(id, -32602, "Missing params object");

        String     toolName = params.require("name");
        JsonObject args     = params.getObject("arguments");
        if (args == null) args = JsonValue.object();

        // ── Security gate — every call must carry a valid Keycloak JWT ──
        String token = args.getString("bearer_token");
        if (token == null) token = "";
        security.validateToken(token, toolName);

        JsonObject toolResult = registry.call(toolName, args);

        JsonArray content = JsonValue.array()
            .add(JsonValue.object().put("type", "text").put("text", toolResult.toJson()));

        return ok(id, JsonValue.object().set("content", content).put("isError", false));
    }

    // ── Response builders ─────────────────────────────────────────────────
    private String ok(JsonValue id, JsonObject result) {
        JsonObject r = JsonValue.object().put("jsonrpc", "2.0");
        if (id != null) r.set("id", id);
        r.set("result", result);
        return r.toJson();
    }

    private String error(JsonValue id, int code, String msg) {
        JsonObject r = JsonValue.object().put("jsonrpc", "2.0");
        if (id != null) r.set("id", id);
        r.set("error", JsonValue.object().put("code", code).put("message", msg));
        return r.toJson();
    }

    /** Remove package names from user-facing error messages. */
    private String clean(String msg) {
        if (msg == null) return "unknown error";
        return msg.replaceAll("com\\.ecoskiller\\.[\\w.]+:", "").trim();
    }
}
