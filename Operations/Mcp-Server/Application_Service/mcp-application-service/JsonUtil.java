package com.ecoskiller.mcp.protocol;

import java.util.*;

/**
 * Zero-dependency JSON serialiser / deserialiser.
 *
 * Handles the subset of JSON used in MCP:
 *   - Objects   { "key": value, ... }
 *   - Arrays    [ value, ... ]
 *   - Strings, numbers, booleans, null
 *
 * NOT a general-purpose JSON library — use only for MCP protocol traffic.
 */
public final class JsonUtil {

    private JsonUtil() {}

    // -------------------------------------------------------------------------
    // Serialisation
    // -------------------------------------------------------------------------

    public static String toJson(Object value) {
        if (value == null)                          return "null";
        if (value instanceof Boolean)               return value.toString();
        if (value instanceof Number)                return value.toString();
        if (value instanceof String)                return quoteString((String) value);
        if (value instanceof List)                  return serializeList((List<?>) value);
        if (value instanceof Map)                   return serializeMap((Map<?, ?>) value);
        return quoteString(value.toString());
    }

    @SuppressWarnings("unchecked")
    private static String serializeMap(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<?, ?> e : map.entrySet()) {
            if (!first) sb.append(',');
            sb.append(quoteString(e.getKey().toString()))
              .append(':')
              .append(toJson(e.getValue()));
            first = false;
        }
        return sb.append('}').toString();
    }

    private static String serializeList(List<?> list) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Object item : list) {
            if (!first) sb.append(',');
            sb.append(toJson(item));
            first = false;
        }
        return sb.append(']').toString();
    }

    private static String quoteString(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                default:
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else          sb.append(c);
            }
        }
        return sb.append('"').toString();
    }

    // -------------------------------------------------------------------------
    // Deserialisation
    // -------------------------------------------------------------------------

    public static Map<String, Object> parseObject(String json) {
        Parser p = new Parser(json.trim());
        Object v = p.parse();
        if (!(v instanceof Map)) throw new IllegalArgumentException("Expected JSON object, got: " + json);
        @SuppressWarnings("unchecked")
        Map<String, Object> m = (Map<String, Object>) v;
        return m;
    }

    // -------------------------------------------------------------------------
    // Convenience helpers
    // -------------------------------------------------------------------------

    /** Build a single-key map quickly. */
    public static Map<String, Object> obj(String key, Object value) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put(key, value);
        return m;
    }

    public static String getString(Map<String, Object> m, String key, String def) {
        Object v = m.get(key);
        return v != null ? v.toString() : def;
    }

    public static boolean getBoolean(Map<String, Object> m, String key, boolean def) {
        Object v = m.get(key);
        if (v == null) return def;
        if (v instanceof Boolean) return (Boolean) v;
        return Boolean.parseBoolean(v.toString());
    }

    public static int getInt(Map<String, Object> m, String key, int def) {
        Object v = m.get(key);
        if (v == null) return def;
        try { return Integer.parseInt(v.toString()); }
        catch (NumberFormatException e) { return def; }
    }

    // -------------------------------------------------------------------------
    // Recursive descent parser
    // -------------------------------------------------------------------------

    private static class Parser {
        private final String src;
        private int pos;

        Parser(String src) { this.src = src; }

        Object parse() {
            skipWs();
            if (pos >= src.length()) throw new IllegalArgumentException("Unexpected EOF");
            char c = src.charAt(pos);
            if (c == '{') return parseObject();
            if (c == '[') return parseArray();
            if (c == '"') return parseString();
            if (c == 't') return parseLiteral("true",  Boolean.TRUE);
            if (c == 'f') return parseLiteral("false", Boolean.FALSE);
            if (c == 'n') return parseLiteral("null",  null);
            return parseNumber();
        }

        private Map<String, Object> parseObject() {
            expect('{');
            Map<String, Object> map = new LinkedHashMap<>();
            skipWs();
            if (peek() == '}') { pos++; return map; }
            while (true) {
                skipWs();
                String key = parseString();
                skipWs(); expect(':'); skipWs();
                Object val = parse();
                map.put(key, val);
                skipWs();
                if (peek() == '}') { pos++; break; }
                expect(',');
            }
            return map;
        }

        private List<Object> parseArray() {
            expect('[');
            List<Object> list = new ArrayList<>();
            skipWs();
            if (peek() == ']') { pos++; return list; }
            while (true) {
                skipWs();
                list.add(parse());
                skipWs();
                if (peek() == ']') { pos++; break; }
                expect(',');
            }
            return list;
        }

        private String parseString() {
            expect('"');
            StringBuilder sb = new StringBuilder();
            while (pos < src.length()) {
                char c = src.charAt(pos++);
                if (c == '"') break;
                if (c == '\\') {
                    char e = src.charAt(pos++);
                    switch (e) {
                        case '"': sb.append('"');  break;
                        case '\\':sb.append('\\'); break;
                        case 'n': sb.append('\n'); break;
                        case 'r': sb.append('\r'); break;
                        case 't': sb.append('\t'); break;
                        case 'u':
                            String hex = src.substring(pos, pos + 4);
                            sb.append((char) Integer.parseInt(hex, 16));
                            pos += 4; break;
                        default: sb.append(e);
                    }
                } else sb.append(c);
            }
            return sb.toString();
        }

        private Object parseLiteral(String word, Object val) {
            if (src.startsWith(word, pos)) { pos += word.length(); return val; }
            throw new IllegalArgumentException("Invalid literal at " + pos);
        }

        private Object parseNumber() {
            int start = pos;
            if (pos < src.length() && src.charAt(pos) == '-') pos++;
            while (pos < src.length() && (Character.isDigit(src.charAt(pos)) || src.charAt(pos) == '.' || src.charAt(pos) == 'e' || src.charAt(pos) == 'E' || src.charAt(pos) == '+' || src.charAt(pos) == '-')) pos++;
            String num = src.substring(start, pos);
            if (num.contains(".") || num.contains("e") || num.contains("E"))
                return Double.parseDouble(num);
            return Long.parseLong(num);
        }

        private void skipWs() { while (pos < src.length() && src.charAt(pos) <= ' ') pos++; }
        private char peek()   { return pos < src.length() ? src.charAt(pos) : 0; }
        private void expect(char c) {
            if (pos >= src.length() || src.charAt(pos) != c)
                throw new IllegalArgumentException("Expected '" + c + "' at " + pos + " in: " + src);
            pos++;
        }
    }
}
