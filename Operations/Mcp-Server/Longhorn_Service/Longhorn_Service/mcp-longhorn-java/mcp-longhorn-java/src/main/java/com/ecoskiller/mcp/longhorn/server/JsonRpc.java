package com.ecoskiller.mcp.longhorn.server;

import java.util.*;

/**
 * Lightweight JSON-RPC 2.0 response builder.
 * No external dependencies — pure Java string construction.
 */
public final class JsonRpc {

    private JsonRpc() {}

    public static String result(String id, Object result) {
        return "{\"jsonrpc\":\"2.0\",\"id\":" + id + ",\"result\":" + toJson(result) + "}";
    }

    public static String error(String id, int code, String message) {
        return "{\"jsonrpc\":\"2.0\",\"id\":" + id
                + ",\"error\":{\"code\":" + code
                + ",\"message\":" + jsonString(message) + "}}";
    }

    public static String toolResult(String id, Object data) {
        List<Map<String, Object>> content = new ArrayList<>();
        Map<String, Object> textBlock = new LinkedHashMap<>();
        textBlock.put("type", "text");
        textBlock.put("text", toJson(data));
        content.add(textBlock);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("content", content);
        payload.put("isError", false);
        return result(id, payload);
    }

    public static String toolError(String id, String message) {
        List<Map<String, Object>> content = new ArrayList<>();
        Map<String, Object> textBlock = new LinkedHashMap<>();
        textBlock.put("type", "text");
        textBlock.put("text", message);
        content.add(textBlock);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("content", content);
        payload.put("isError", true);
        return result(id, payload);
    }

    /** Convenience: build a simple string→string map */
    public static Map<String, Object> obj(String... pairs) {
        Map<String, Object> m = new LinkedHashMap<>();
        for (int i = 0; i + 1 < pairs.length; i += 2) {
            m.put(pairs[i], pairs[i + 1]);
        }
        return m;
    }

    @SuppressWarnings("unchecked")
    public static String toJson(Object value) {
        if (value == null)                          return "null";
        if (value instanceof Boolean)               return value.toString();
        if (value instanceof Number)                return value.toString();
        if (value instanceof String)                return jsonString((String) value);
        if (value instanceof List) {
            List<?> list = (List<?>) value;
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(toJson(list.get(i)));
            }
            return sb.append("]").toString();
        }
        if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<String, Object> e : map.entrySet()) {
                if (!first) sb.append(",");
                sb.append(jsonString(e.getKey())).append(":").append(toJson(e.getValue()));
                first = false;
            }
            return sb.append("}").toString();
        }
        return jsonString(value.toString());
    }

    private static String jsonString(String s) {
        return "\"" + s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                + "\"";
    }
}
