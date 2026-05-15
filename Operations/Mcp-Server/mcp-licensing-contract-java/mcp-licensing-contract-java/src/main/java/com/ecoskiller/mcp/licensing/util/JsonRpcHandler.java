package com.ecoskiller.mcp.licensing.util;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Utility for building MCP-compliant JSON-RPC 2.0 response envelopes.
 */
public class JsonRpcHandler {

    public JSONObject success(Object id, JSONObject result) {
        return base(id).put("result", result);
    }

    public JSONObject toolSuccess(Object id, String text) {
        JSONArray content = new JSONArray()
                .put(new JSONObject().put("type", "text").put("text", text));
        return base(id).put("result", new JSONObject()
                .put("content", content)
                .put("isError", false));
    }

    public JSONObject toolError(Object id, String message) {
        JSONArray content = new JSONArray()
                .put(new JSONObject().put("type", "text").put("text", "ERROR: " + message));
        return base(id).put("result", new JSONObject()
                .put("content", content)
                .put("isError", true));
    }

    public JSONObject parseError(Object id) {
        return error(id, -32700, "Parse error", null);
    }

    public JSONObject methodNotFound(Object id, String method) {
        return error(id, -32601, "Method not found: " + method, null);
    }

    public JSONObject invalidParams(Object id, String detail) {
        return error(id, -32602, "Invalid params: " + detail, null);
    }

    private JSONObject error(Object id, int code, String message, Object data) {
        JSONObject err = new JSONObject().put("code", code).put("message", message);
        if (data != null) err.put("data", data);
        return base(id).put("error", err);
    }

    private JSONObject base(Object id) {
        JSONObject obj = new JSONObject().put("jsonrpc", "2.0");
        if (id != null) obj.put("id", id); else obj.put("id", JSONObject.NULL);
        return obj;
    }
}
