package com.ecoskiller.mcp.nginx.util;

import java.util.*;

/**
 * Minimal JSON serialiser / deserialiser — zero external dependencies.
 *
 * Supports: String, Number, Boolean, null, Map<String,Object>, List<Object>.
 * For MCP server traffic these types are sufficient.
 */
public final class JsonUtil {

    private JsonUtil() {}

    // ── Serialise ─────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    public static String toJson(Object value) {
        if (value == null)              return "null";
        if (value instanceof String)    return quoteString((String) value);
        if (value instanceof Boolean)   return value.toString();
        if (value instanceof Number)    return value.toString();
        if (value instanceof Map) {
            Map<?, ?> m = (Map<?, ?>) value;
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> e : m.entrySet()) {
                if (!first) sb.append(',');
                sb.append(quoteString(String.valueOf(e.getKey())));
                sb.append(':');
                sb.append(toJson(e.getValue()));
                first = false;
            }
            return sb.append('}').toString();
        }
        if (value instanceof List) {
            List<?> l = (List<?>) value;
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object item : l) {
                if (!first) sb.append(',');
                sb.append(toJson(item));
                first = false;
            }
            return sb.append(']').toString();
        }
        return quoteString(value.toString());
    }

    private static String quoteString(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b");  break;
                case '\f': sb.append("\\f");  break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.append('"').toString();
    }

    // ── MCP response builders ─────────────────────────────────────────────

    public static String successResponse(Object id, Object result) {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("jsonrpc", "2.0");
        resp.put("id", id);
        resp.put("result", result);
        return toJson(resp);
    }

    public static String errorResponse(Object id, int code, String message) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("code", code);
        error.put("message", message);
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("jsonrpc", "2.0");
        resp.put("id", id);
        resp.put("error", error);
        return toJson(resp);
    }

    // ── Deserialise ───────────────────────────────────────────────────────

    public static Map<String, Object> parseObject(String json) {
        Object v = new Parser(json).parseValue();
        if (v instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> m = (Map<String, Object>) v;
            return m;
        }
        throw new IllegalArgumentException("Expected JSON object");
    }

    // ── Recursive descent parser ──────────────────────────────────────────

    private static class Parser {
        private final String src;
        private int pos = 0;

        Parser(String src) { this.src = src.trim(); }

        Object parseValue() {
            skipWs();
            if (pos >= src.length()) throw new IllegalArgumentException("Unexpected EOF");
            char c = src.charAt(pos);
            if (c == '{')  return parseObject();
            if (c == '[')  return parseArray();
            if (c == '"')  return parseString();
            if (c == 't')  return parseLiteral("true",  Boolean.TRUE);
            if (c == 'f')  return parseLiteral("false", Boolean.FALSE);
            if (c == 'n')  return parseLiteral("null",  null);
            if (c == '-' || Character.isDigit(c)) return parseNumber();
            throw new IllegalArgumentException("Unexpected char: " + c + " at pos " + pos);
        }

        private Map<String, Object> parseObject() {
            expect('{');
            Map<String, Object> m = new LinkedHashMap<>();
            skipWs();
            if (peek() == '}') { pos++; return m; }
            while (true) {
                skipWs();
                String key = parseString();
                skipWs();
                expect(':');
                m.put(key, parseValue());
                skipWs();
                char sep = src.charAt(pos++);
                if (sep == '}') break;
                if (sep != ',') throw new IllegalArgumentException("Expected ',' or '}' at pos " + (pos - 1));
            }
            return m;
        }

        private List<Object> parseArray() {
            expect('[');
            List<Object> l = new ArrayList<>();
            skipWs();
            if (peek() == ']') { pos++; return l; }
            while (true) {
                l.add(parseValue());
                skipWs();
                char sep = src.charAt(pos++);
                if (sep == ']') break;
                if (sep != ',') throw new IllegalArgumentException("Expected ',' or ']' at pos " + (pos - 1));
            }
            return l;
        }

        private String parseString() {
            expect('"');
            StringBuilder sb = new StringBuilder();
            while (pos < src.length()) {
                char c = src.charAt(pos++);
                if (c == '"') return sb.toString();
                if (c == '\\') {
                    char esc = src.charAt(pos++);
                    switch (esc) {
                        case '"':  sb.append('"');  break;
                        case '\\': sb.append('\\'); break;
                        case '/':  sb.append('/');  break;
                        case 'b':  sb.append('\b'); break;
                        case 'f':  sb.append('\f'); break;
                        case 'n':  sb.append('\n'); break;
                        case 'r':  sb.append('\r'); break;
                        case 't':  sb.append('\t'); break;
                        case 'u': {
                            String hex = src.substring(pos, pos + 4); pos += 4;
                            sb.append((char) Integer.parseInt(hex, 16));
                            break;
                        }
                        default: sb.append(esc);
                    }
                } else {
                    sb.append(c);
                }
            }
            throw new IllegalArgumentException("Unterminated string");
        }

        private Number parseNumber() {
            int start = pos;
            if (peek() == '-') pos++;
            while (pos < src.length() && (Character.isDigit(src.charAt(pos)) || src.charAt(pos) == '.'))
                pos++;
            if (pos < src.length() && (src.charAt(pos) == 'e' || src.charAt(pos) == 'E')) {
                pos++;
                if (pos < src.length() && (src.charAt(pos) == '+' || src.charAt(pos) == '-')) pos++;
                while (pos < src.length() && Character.isDigit(src.charAt(pos))) pos++;
            }
            String num = src.substring(start, pos);
            if (num.contains(".") || num.contains("e") || num.contains("E")) {
                return Double.parseDouble(num);
            }
            try { return Long.parseLong(num); }
            catch (NumberFormatException e) { return Double.parseDouble(num); }
        }

        private Object parseLiteral(String lit, Object val) {
            if (src.startsWith(lit, pos)) { pos += lit.length(); return val; }
            throw new IllegalArgumentException("Expected '" + lit + "' at pos " + pos);
        }

        private void expect(char c) {
            if (pos >= src.length() || src.charAt(pos) != c)
                throw new IllegalArgumentException("Expected '" + c + "' at pos " + pos);
            pos++;
        }

        private char peek() {
            return pos < src.length() ? src.charAt(pos) : 0;
        }

        private void skipWs() {
            while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) pos++;
        }
    }
}
