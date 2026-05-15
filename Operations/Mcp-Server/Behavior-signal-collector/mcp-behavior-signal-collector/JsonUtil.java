package com.ecoskiller.mcp.protocol;

import java.util.*;

/** Zero-dependency JSON serialiser / deserialiser for MCP protocol traffic. */
public final class JsonUtil {

    private JsonUtil() {}

    public static String toJson(Object value) {
        if (value == null)            return "null";
        if (value instanceof Boolean) return value.toString();
        if (value instanceof Number)  return value.toString();
        if (value instanceof String)  return quoteString((String) value);
        if (value instanceof List)    return serList((List<?>) value);
        if (value instanceof Map)     return serMap((Map<?, ?>) value);
        return quoteString(value.toString());
    }

    private static String serMap(Map<?, ?> m) {
        StringBuilder sb = new StringBuilder("{"); boolean first = true;
        for (Map.Entry<?, ?> e : m.entrySet()) {
            if (!first) sb.append(',');
            sb.append(quoteString(e.getKey().toString())).append(':').append(toJson(e.getValue()));
            first = false;
        }
        return sb.append('}').toString();
    }

    private static String serList(List<?> l) {
        StringBuilder sb = new StringBuilder("["); boolean first = true;
        for (Object o : l) { if (!first) sb.append(','); sb.append(toJson(o)); first = false; }
        return sb.append(']').toString();
    }

    private static String quoteString(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"':  sb.append("\\\""); break; case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n");  break; case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                default: if (c < 0x20) sb.append(String.format("\\u%04x",(int)c)); else sb.append(c);
            }
        }
        return sb.append('"').toString();
    }

    public static Map<String, Object> parseObject(String json) {
        Object v = new Parser(json.trim()).parse();
        if (!(v instanceof Map)) throw new IllegalArgumentException("Expected JSON object");
        @SuppressWarnings("unchecked") Map<String, Object> m = (Map<String, Object>) v;
        return m;
    }

    public static Map<String, Object> obj(String k, Object v) {
        Map<String, Object> m = new LinkedHashMap<>(); m.put(k, v); return m;
    }

    public static String getString(Map<String, Object> m, String k, String def) {
        Object v = m.get(k); return v != null ? v.toString() : def;
    }

    public static int getInt(Map<String, Object> m, String k, int def) {
        Object v = m.get(k); if (v == null) return def;
        try { return Integer.parseInt(v.toString()); } catch (NumberFormatException e) { return def; }
    }

    public static double getDouble(Map<String, Object> m, String k, double def) {
        Object v = m.get(k); if (v == null) return def;
        try { return Double.parseDouble(v.toString()); } catch (NumberFormatException e) { return def; }
    }

    /** Public entry point so external classes can parse arbitrary JSON values (arrays, objects). */
    public static class PublicParser extends Parser {
        public PublicParser(String src) { super(src.trim()); }
        public Object parse() { return super.parse(); }
    }

    private static class Parser {
        private final String src; private int pos;
        Parser(String src) { this.src = src; }

        Object parse() {
            skipWs(); if (pos >= src.length()) throw new IllegalArgumentException("EOF");
            char c = src.charAt(pos);
            if (c == '{') return parseObj(); if (c == '[') return parseArr();
            if (c == '"') return parseStr(); if (c == 't') return parseLit("true",Boolean.TRUE);
            if (c == 'f') return parseLit("false",Boolean.FALSE); if (c == 'n') return parseLit("null",null);
            return parseNum();
        }

        private Map<String, Object> parseObj() {
            expect('{'); Map<String, Object> m = new LinkedHashMap<>();
            skipWs(); if (peek() == '}') { pos++; return m; }
            while (true) {
                skipWs(); String k = parseStr(); skipWs(); expect(':'); skipWs();
                m.put(k, parse()); skipWs();
                if (peek() == '}') { pos++; break; } expect(',');
            }
            return m;
        }

        private List<Object> parseArr() {
            expect('['); List<Object> l = new ArrayList<>();
            skipWs(); if (peek() == ']') { pos++; return l; }
            while (true) { skipWs(); l.add(parse()); skipWs(); if (peek() == ']') { pos++; break; } expect(','); }
            return l;
        }

        private String parseStr() {
            expect('"'); StringBuilder sb = new StringBuilder();
            while (pos < src.length()) {
                char c = src.charAt(pos++);
                if (c == '"') break;
                if (c == '\\') {
                    char e = src.charAt(pos++);
                    switch (e) {
                        case '"': sb.append('"'); break; case '\\': sb.append('\\'); break;
                        case 'n': sb.append('\n'); break; case 'r': sb.append('\r'); break;
                        case 't': sb.append('\t'); break;
                        case 'u': sb.append((char)Integer.parseInt(src.substring(pos,pos+4),16)); pos+=4; break;
                        default: sb.append(e);
                    }
                } else sb.append(c);
            }
            return sb.toString();
        }

        private Object parseLit(String w, Object v) {
            if (src.startsWith(w, pos)) { pos += w.length(); return v; }
            throw new IllegalArgumentException("Bad literal");
        }

        private Object parseNum() {
            int s = pos;
            if (pos < src.length() && src.charAt(pos) == '-') pos++;
            while (pos < src.length() && "0123456789.eE+-".indexOf(src.charAt(pos)) >= 0) pos++;
            String n = src.substring(s, pos);
            return (n.contains(".")||n.contains("e")||n.contains("E")) ? Double.parseDouble(n) : Long.parseLong(n);
        }

        private void skipWs() { while (pos < src.length() && src.charAt(pos) <= ' ') pos++; }
        private char peek()   { return pos < src.length() ? src.charAt(pos) : 0; }
        private void expect(char c) {
            if (pos >= src.length() || src.charAt(pos) != c)
                throw new IllegalArgumentException("Expected '"+c+"' at "+pos);
            pos++;
        }
    }
}
