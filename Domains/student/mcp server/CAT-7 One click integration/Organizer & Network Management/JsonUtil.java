package com.ecoskiller.mcp26;

/**
 * Minimal JSON utility — no external libraries.
 * Handles the simple flat JSON produced / consumed by the MCP protocol.
 */
public class JsonUtil {

    /** Extract a quoted string value for a given key from a flat JSON object. */
    public static String extractString(String json, String key) {
        if (json == null || json.isBlank()) return "";
        String search = "\"" + key + "\"";
        int ki = json.indexOf(search);
        if (ki < 0) return "";
        int colon = json.indexOf(':', ki + search.length());
        if (colon < 0) return "";
        int start = json.indexOf('"', colon + 1);
        if (start < 0) return "";
        int end = json.indexOf('"', start + 1);
        if (end < 0) return "";
        return json.substring(start + 1, end);
    }

    /**
     * Extract the raw value (number, string, null, true/false) for a key.
     * Used for the JSON-RPC "id" field which may be a number or string.
     */
    public static String extractRaw(String json, String key) {
        if (json == null || json.isBlank()) return "null";
        String search = "\"" + key + "\"";
        int ki = json.indexOf(search);
        if (ki < 0) return "null";
        int colon = json.indexOf(':', ki + search.length());
        if (colon < 0) return "null";
        // skip whitespace
        int pos = colon + 1;
        while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) pos++;
        if (pos >= json.length()) return "null";
        char c = json.charAt(pos);
        if (c == '"') {
            int end = json.indexOf('"', pos + 1);
            return end < 0 ? "null" : json.substring(pos, end + 1);
        }
        // number, boolean, null
        int end = pos;
        while (end < json.length() && ",}]".indexOf(json.charAt(end)) < 0) end++;
        return json.substring(pos, end).trim();
    }

    /**
     * Extract a nested JSON object value for a given key.
     * Returns "{}" if not found.
     */
    public static String extractObject(String json, String key) {
        if (json == null || json.isBlank()) return "{}";
        String search = "\"" + key + "\"";
        int ki = json.indexOf(search);
        if (ki < 0) return "{}";
        int colon = json.indexOf(':', ki + search.length());
        if (colon < 0) return "{}";
        int brace = json.indexOf('{', colon + 1);
        if (brace < 0) return "{}";
        int depth = 0;
        int i = brace;
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') { depth--; if (depth == 0) return json.substring(brace, i + 1); }
            i++;
        }
        return "{}";
    }

    /** Build a flat JSON object from alternating key-value string pairs. */
    public static String buildObject(String... kvPairs) {
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < kvPairs.length - 1; i += 2) {
            if (i > 0) sb.append(",");
            sb.append("\"").append(kvPairs[i]).append("\":\"")
              .append(kvPairs[i + 1].replace("\"", "\\\"")).append("\"");
        }
        sb.append("}");
        return sb.toString();
    }

    /** Escape a plain string for safe embedding inside a JSON string value. */
    public static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }
}
