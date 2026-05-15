package com.ecoskiller.mcp.minio.protocol;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * McpProtocol — JSON-RPC 2.0 data structures for the MCP protocol.
 *
 * MCP Version: 2024-11-05
 * Transport:   stdio (stdin → server, stdout → client, stderr → audit logs)
 *
 * Methods handled:
 *   initialize   — handshake + capability advertisement
 *   tools/list   — returns all 24 tool definitions
 *   tools/call   — dispatches to MinioToolHandler
 *   ping         — keepalive
 */
public final class McpProtocol {

    private McpProtocol() {} // utility class

    // ── Constants ─────────────────────────────────────────────────────────────

    public static final String MCP_VERSION       = "2024-11-05";
    public static final String SERVER_NAME       = "mcp-minio-ecoskiller";
    public static final String SERVER_VERSION    = "1.0.0";

    public static final String METHOD_INITIALIZE = "initialize";
    public static final String METHOD_TOOLS_LIST = "tools/list";
    public static final String METHOD_TOOLS_CALL = "tools/call";
    public static final String METHOD_PING       = "ping";

    // ── Request ───────────────────────────────────────────────────────────────

    /** Represents a parsed incoming JSON-RPC 2.0 request. */
    public static final class Request {
        public final String      jsonrpc;
        public final Object      id;       // String | Number | null
        public final String      method;
        public final JsonObject  params;   // may be null

        public Request(String jsonrpc, Object id, String method, JsonObject params) {
            this.jsonrpc = jsonrpc;
            this.id      = id;
            this.method  = method;
            this.params  = params != null ? params : new JsonObject();
        }

        /** Returns a named string parameter, or defaultValue if absent. */
        public String getString(String key, String defaultValue) {
            if (params != null && params.has(key)) {
                JsonElement el = params.get(key);
                return el.isJsonNull() ? defaultValue : el.getAsString();
            }
            return defaultValue;
        }

        public int getInt(String key, int defaultValue) {
            if (params != null && params.has(key)) {
                try { return params.get(key).getAsInt(); }
                catch (Exception e) { return defaultValue; }
            }
            return defaultValue;
        }

        public boolean getBoolean(String key, boolean defaultValue) {
            if (params != null && params.has(key)) {
                try { return params.get(key).getAsBoolean(); }
                catch (Exception e) { return defaultValue; }
            }
            return defaultValue;
        }

        /** For tools/call — extracts the arguments sub-object. */
        public JsonObject getArguments() {
            if (params != null && params.has("arguments")
                    && params.get("arguments").isJsonObject()) {
                return params.getAsJsonObject("arguments");
            }
            return new JsonObject();
        }

        /** For tools/call — extracts the tool name. */
        public String getToolName() {
            return getString("name", "");
        }
    }

    // ── Response helpers ──────────────────────────────────────────────────────

    /** Builds a successful JSON-RPC 2.0 response. */
    public static JsonObject successResponse(Object id, JsonElement result) {
        JsonObject resp = new JsonObject();
        resp.addProperty("jsonrpc", "2.0");
        addId(resp, id);
        resp.add("result", result);
        return resp;
    }

    /** Builds a JSON-RPC 2.0 error response. */
    public static JsonObject errorResponse(Object id, int code, String message) {
        JsonObject error = new JsonObject();
        error.addProperty("code",    code);
        error.addProperty("message", message);

        JsonObject resp = new JsonObject();
        resp.addProperty("jsonrpc", "2.0");
        addId(resp, id);
        resp.add("error", error);
        return resp;
    }

    /** Wraps a plain text message as an MCP tool content block. */
    public static JsonObject textContent(String text) {
        JsonObject content = new JsonObject();
        content.addProperty("type", "text");
        content.addProperty("text", text);
        return content;
    }

    /** Builds the tools/call result envelope. */
    public static JsonObject toolResult(boolean isError, String text) {
        com.google.gson.JsonArray contentArray = new com.google.gson.JsonArray();
        contentArray.add(textContent(text));

        JsonObject result = new JsonObject();
        result.add("content", contentArray);
        result.addProperty("isError", isError);
        return result;
    }

    // ── Error codes (JSON-RPC 2.0 + MCP extensions) ───────────────────────────

    public static final int ERR_PARSE_ERROR     = -32700;
    public static final int ERR_INVALID_REQUEST = -32600;
    public static final int ERR_METHOD_NOT_FOUND= -32601;
    public static final int ERR_INVALID_PARAMS  = -32602;
    public static final int ERR_INTERNAL        = -32603;

    // ── Private helpers ───────────────────────────────────────────────────────

    private static void addId(JsonObject obj, Object id) {
        if (id == null) {
            obj.add("id", com.google.gson.JsonNull.INSTANCE);
        } else if (id instanceof Number) {
            obj.addProperty("id", (Number) id);
        } else {
            obj.addProperty("id", id.toString());
        }
    }
}
