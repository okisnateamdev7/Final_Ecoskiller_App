package com.ecoskiller.mcp16;

import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.*;

/**
 * EcoSkiller MCP-16 — Dojo, Growth & School Ops
 *
 * Transport : stdio (stdin/stdout)
 * Protocol  : JSON-RPC 2.0 / MCP 2024-11-05
 * Agents    : DAILY_MISSION_AGENT_ANTIGRAVITY
 */
public class McpServer {

    private static final Logger LOG = Logger.getLogger(McpServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final DailyMissionAgent dailyMissionAgent = new DailyMissionAgent();

    public static void main(String[] args) throws IOException {
        // Log to stderr only — stdout is reserved for MCP JSON-RPC
        configureLogging();
        LOG.info("EcoSkiller MCP-16 Dojo Server starting...");
        new McpServer().run();
    }

    private void run() throws IOException {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in,  StandardCharsets.UTF_8));
        PrintWriter   out  = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            LOG.fine(">> " + line);
            String response = handleRequest(line);
            if (response != null) {
                LOG.fine("<< " + response);
                out.println(response);
            }
        }
        LOG.info("MCP-16 server shutting down.");
    }

    // -------------------------------------------------------------------------
    // Request dispatcher
    // -------------------------------------------------------------------------

    private String handleRequest(String raw) {
        JsonObject req;
        try {
            req = JsonParser.parseString(raw).getAsJsonObject();
        } catch (Exception e) {
            return errorResponse(null, -32700, "Parse error: " + e.getMessage());
        }

        JsonElement idEl     = req.get("id");
        String      method   = req.has("method") ? req.get("method").getAsString() : "";
        JsonObject  params   = req.has("params") ? req.getAsJsonObject("params") : new JsonObject();

        // Notifications (no id) — no response required
        if (idEl == null || idEl.isJsonNull()) {
            return null;
        }

        try {
            JsonElement result = switch (method) {
                case "initialize"   -> handleInitialize(params);
                case "ping"         -> new JsonObject();
                case "tools/list"   -> handleToolsList();
                case "tools/call"   -> handleToolsCall(params);
                default             -> throw new McpException(-32601, "Method not found: " + method);
            };
            return successResponse(idEl, result);
        } catch (McpException e) {
            return errorResponse(idEl, e.code, e.getMessage());
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Unhandled error", e);
            return errorResponse(idEl, -32603, "Internal error: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // MCP method handlers
    // -------------------------------------------------------------------------

    private JsonElement handleInitialize(JsonObject params) {
        JsonObject info = new JsonObject();
        info.addProperty("name",    "mcp-16-dojo");
        info.addProperty("version", "1.0.0");

        JsonObject capabilities = new JsonObject();
        capabilities.add("tools", new JsonObject());

        JsonObject result = new JsonObject();
        result.addProperty("protocolVersion", MCP_VERSION);
        result.add("serverInfo",   info);
        result.add("capabilities", capabilities);
        return result;
    }

    private JsonElement handleToolsList() {
        JsonArray tools = new JsonArray();
        tools.add(dailyMissionAgent.toolDefinition());
        JsonObject result = new JsonObject();
        result.add("tools", tools);
        return result;
    }

    private JsonElement handleToolsCall(JsonObject params) {
        String toolName = params.has("name") ? params.get("name").getAsString() : "";
        JsonObject args = params.has("arguments") ? params.getAsJsonObject("arguments") : new JsonObject();

        if ("daily_mission".equals(toolName)) {
            return dailyMissionAgent.execute(args);
        }
        throw new McpException(-32602, "Unknown tool: " + toolName);
    }

    // -------------------------------------------------------------------------
    // JSON-RPC helpers
    // -------------------------------------------------------------------------

    private String successResponse(JsonElement id, JsonElement result) {
        JsonObject resp = new JsonObject();
        resp.addProperty("jsonrpc", "2.0");
        resp.add("id", id);
        resp.add("result", result);
        return GSON.toJson(resp);
    }

    private String errorResponse(JsonElement id, int code, String message) {
        JsonObject err = new JsonObject();
        err.addProperty("code",    code);
        err.addProperty("message", message);

        JsonObject resp = new JsonObject();
        resp.addProperty("jsonrpc", "2.0");
        resp.add("id",    id != null ? id : JsonNull.INSTANCE);
        resp.add("error", err);
        return GSON.toJson(resp);
    }

    // -------------------------------------------------------------------------
    // Logging setup
    // -------------------------------------------------------------------------

    private static void configureLogging() {
        Logger root = Logger.getLogger("");
        // Remove default console handler (writes to stdout — would corrupt MCP stream)
        for (Handler h : root.getHandlers()) root.removeHandler(h);

        ConsoleHandler stderr = new ConsoleHandler() {
            { setOutputStream(System.err); }
        };
        stderr.setLevel(Level.ALL);
        stderr.setFormatter(new SimpleFormatter());
        root.addHandler(stderr);
        root.setLevel(Level.INFO);
    }

    // -------------------------------------------------------------------------
    // Internal exception
    // -------------------------------------------------------------------------

    static class McpException extends RuntimeException {
        final int code;
        McpException(int code, String message) {
            super(message);
            this.code = code;
        }
    }
}
