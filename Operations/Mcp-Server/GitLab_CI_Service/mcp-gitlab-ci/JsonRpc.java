package com.ecoskiller.mcp.gitlab.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Immutable JSON-RPC 2.0 envelope models used throughout the MCP server.
 * Jackson serialisation/deserialisation is field-based; no public setters.
 */
public final class JsonRpc {

    private JsonRpc() {}

    // ── Request ───────────────────────────────────────────────────────────────

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class Request {
        public String jsonrpc;
        public String method;
        public JsonNode params;
        public Object id; // String | Integer | null

        public Request() {}

        public Request(String jsonrpc, String method, JsonNode params, Object id) {
            this.jsonrpc = jsonrpc;
            this.method  = method;
            this.params  = params;
            this.id      = id;
        }
    }

    // ── Response (success) ────────────────────────────────────────────────────

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class Response {
        public String jsonrpc = "2.0";
        public Object result;
        public Object id;

        public Response() {}

        public Response(Object result, Object id) {
            this.result = result;
            this.id     = id;
        }
    }

    // ── Response (error) ─────────────────────────────────────────────────────

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class ErrorResponse {
        public String jsonrpc = "2.0";
        public RpcError error;
        public Object id;

        public ErrorResponse() {}

        public ErrorResponse(int code, String message, Object id) {
            this.error = new RpcError(code, message);
            this.id    = id;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class RpcError {
        public int code;
        public String message;

        public RpcError() {}

        public RpcError(int code, String message) {
            this.code    = code;
            this.message = message;
        }
    }

    // ── Standard error codes ──────────────────────────────────────────────────

    public static final int PARSE_ERROR      = -32700;
    public static final int INVALID_REQUEST  = -32600;
    public static final int METHOD_NOT_FOUND = -32601;
    public static final int INVALID_PARAMS   = -32602;
    public static final int INTERNAL_ERROR   = -32603;

    // Custom application codes (in the -32000 to -32099 range)
    public static final int SECURITY_VIOLATION = -32000;
    public static final int RATE_LIMIT_EXCEEDED = -32001;
}
