package io.ecoskiller.mcp.utils;

import java.util.*;

/**
 * Zero-dependency JSON utility for MCP JSON-RPC protocol.
 *
 * Handles:
 *   - toJson()        : serialize Map/List/primitives to JSON string
 *   - parseObject()   : parse a JSON object string to Map<String,Object>
 *   - successResponse : build a JSON-RPC 2.0 success response
 *   - errorResponse   : build a JSON-RPC 2.0 error response
 */
public class JsonUtils {

    // ── Serialization ─────────────────────────────────────────────────────────

    public static String toJson(Object obj) {
        if (obj == null)                 return "null";
        if (obj instanceof String)       return quoteString((String) obj);
        if (obj instanceof Boolean)      return obj.toString();
        if (obj instanceof Number)       return obj.toString();
        if (obj instanceof Map)          return mapToJson((Map<?, ?>) obj);
        if (obj instanceof List)         return listToJson((List<?>) obj);
        if (obj instanceof Object[])     return arrayToJson((Object[]) obj);
        // Fallback: quote it
        return quoteString(obj.toString());
    }

    private static String mapToJson(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) sb.append(",");
            sb.append(quoteString(entry.getKey().toString()));
            sb.append(":");
            sb.append(toJson(entry.getValue()));
            first = false;
        }
        return sb.append("}").toString();
    }

    private static String listToJson(List<?> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(list.get(i)));
        }
        return sb.append("]").toString();
    }

    private static String arrayToJson(Object[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(arr[i]));
        }
        return sb.append("]").toString();
    }

    private static String quoteString(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"'  -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default   -> {
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else          sb.append(c);
                }
            }
        }
        return sb.append("\"").toString();
    }

    // ── JSON-RPC 2.0 response builders ────────────────────────────────────────

    public static String successResponse(Object id, Object result) {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("jsonrpc", "2.0");
        resp.put("id",      id);
        resp.put("result",  result);
        return toJson(resp);
    }

    public static String errorResponse(Object id, int code, String message) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("code",    code);
        error.put("message", message);

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("jsonrpc", "2.0");
        resp.put("id",      id);
        resp.put("error",   error);
        return toJson(resp);
    }

    // ── Parsing ───────────────────────────────────────────────────────────────

    /**
     * Parse a JSON object string into a Map<String, Object>.
     * Supports: nested objects, arrays, strings, numbers, booleans, null.
     * Throws RuntimeException on malformed input.
     */
    public static Map<String, Object> parseObject(String json) {
        json = json.trim();
        if (!json.startsWith("{"))
            throw new RuntimeException("Expected JSON object starting with '{'");
        Parser p = new Parser(json);
        Object result = p.parseValue();
        if (result instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) result;
            return map;
        }
        throw new RuntimeException("Expected JSON object, got: " + result.getClass().getSimpleName());
    }

    // ── Internal Parser ───────────────────────────────────────────────────────

    private static class Parser {
        private final String src;
        private int pos;

        Parser(String src) { this.src = src; this.pos = 0; }

        Object parseValue() {
            skipWhitespace();
            if (pos >= src.length()) throw new RuntimeException("Unexpected end of JSON");
            char c = src.charAt(pos);
            return switch (c) {
                case '{' -> parseObject();
                case '[' -> parseArray();
                case '"' -> parseString();
                case 't' -> parseLiteral("true",  Boolean.TRUE);
                case 'f' -> parseLiteral("false", Boolean.FALSE);
                case 'n' -> parseLiteral("null",  null);
                default  -> {
                    if (c == '-' || Character.isDigit(c)) yield parseNumber();
                    throw new RuntimeException("Unexpected char: " + c + " at pos " + pos);
                }
            };
        }

        Map<String, Object> parseObject() {
            expect('{');
            Map<String, Object> map = new LinkedHashMap<>();
            skipWhitespace();
            if (peek() == '}') { pos++; return map; }
            while (true) {
                skipWhitespace();
                String key = parseString();
                skipWhitespace();
                expect(':');
                Object val = parseValue();
                map.put(key, val);
                skipWhitespace();
                char next = peek();
                if (next == '}') { pos++; break; }
                if (next == ',') { pos++; }
                else throw new RuntimeException("Expected ',' or '}' at pos " + pos);
            }
            return map;
        }

        List<Object> parseArray() {
            expect('[');
            List<Object> list = new ArrayList<>();
            skipWhitespace();
            if (peek() == ']') { pos++; return list; }
            while (true) {
                list.add(parseValue());
                skipWhitespace();
                char next = peek();
                if (next == ']') { pos++; break; }
                if (next == ',') { pos++; }
                else throw new RuntimeException("Expected ',' or ']' at pos " + pos);
            }
            return list;
        }

        String parseString() {
            expect('"');
            StringBuilder sb = new StringBuilder();
            while (pos < src.length()) {
                char c = src.charAt(pos++);
                if (c == '"') return sb.toString();
                if (c == '\\') {
                    char esc = src.charAt(pos++);
                    sb.append(switch (esc) {
                        case '"'  -> '"';
                        case '\\' -> '\\';
                        case '/'  -> '/';
                        case 'n'  -> '\n';
                        case 'r'  -> '\r';
                        case 't'  -> '\t';
                        case 'u'  -> { String hex = src.substring(pos, pos + 4); pos += 4;
                                       yield (char) Integer.parseInt(hex, 16); }
                        default   -> esc;
                    });
                } else {
                    sb.append(c);
                }
            }
            throw new RuntimeException("Unterminated string");
        }

        Number parseNumber() {
            int start = pos;
            if (peek() == '-') pos++;
            while (pos < src.length() && (Character.isDigit(src.charAt(pos)) || src.charAt(pos) == '.'
                    || src.charAt(pos) == 'e' || src.charAt(pos) == 'E'
                    || src.charAt(pos) == '+' || src.charAt(pos) == '-' && pos > start)) pos++;
            String numStr = src.substring(start, pos);
            if (numStr.contains(".") || numStr.contains("e") || numStr.contains("E"))
                return Double.parseDouble(numStr);
            try { return Long.parseLong(numStr); }
            catch (NumberFormatException e) { return Double.parseDouble(numStr); }
        }

        Object parseLiteral(String literal, Object value) {
            if (src.startsWith(literal, pos)) { pos += literal.length(); return value; }
            throw new RuntimeException("Expected '" + literal + "' at pos " + pos);
        }

        void skipWhitespace() { while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) pos++; }
        void expect(char c) { if (pos >= src.length() || src.charAt(pos) != c)
            throw new RuntimeException("Expected '" + c + "' at pos " + pos); pos++; }
        char peek() { return pos < src.length() ? src.charAt(pos) : 0; }
    }
}
