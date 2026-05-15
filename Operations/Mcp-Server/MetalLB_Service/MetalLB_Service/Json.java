package com.ecoskiller.mcp.metallb.json;

import java.util.*;

/**
 * Minimal JSON builder — pure Java stdlib, no external dependencies.
 * Handles the subset of JSON needed for MCP (objects, arrays, strings, numbers, booleans, null).
 */
public class Json {

    // ── Value types ──────────────────────────────────────────────────────

    public interface Node {
        String toJson();
    }

    public static class Obj implements Node {
        private final LinkedHashMap<String, Node> fields = new LinkedHashMap<>();

        public Obj put(String key, String value) {
            fields.put(key, value == null ? new Null() : new Str(value));
            return this;
        }
        public Obj put(String key, long value)    { fields.put(key, new Num(value));  return this; }
        public Obj put(String key, double value)  { fields.put(key, new Dbl(value));  return this; }
        public Obj put(String key, boolean value) { fields.put(key, new Bool(value)); return this; }
        public Obj put(String key, Node value)    { fields.put(key, value == null ? new Null() : value); return this; }
        public Obj putNull(String key)            { fields.put(key, new Null()); return this; }

        public boolean has(String key) { return fields.containsKey(key); }
        public LinkedHashMap<String, Node> fields() { return fields; }
        public Node get(String key)    { return fields.get(key); }

        public String str(String key)  {
            Node n = fields.get(key);
            return (n instanceof Str s) ? s.value : null;
        }
        public long num(String key) {
            Node n = fields.get(key);
            if (n instanceof Num nm) return nm.value;
            if (n instanceof Str s)  { try { return Long.parseLong(s.value); } catch (Exception e) { return 0; } }
            return 0;
        }
        public boolean bool(String key) {
            Node n = fields.get(key);
            return (n instanceof Bool b) && b.value;
        }
        public Obj obj(String key) {
            Node n = fields.get(key);
            return (n instanceof Obj o) ? o : null;
        }
        public Arr arr(String key) {
            Node n = fields.get(key);
            return (n instanceof Arr a) ? a : null;
        }

        @Override
        public String toJson() {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<String, Node> e : fields.entrySet()) {
                if (!first) sb.append(',');
                sb.append(Str.escape(e.getKey())).append(':').append(e.getValue().toJson());
                first = false;
            }
            return sb.append('}').toString();
        }
    }

    public static class Arr implements Node {
        private final List<Node> items = new ArrayList<>();

        public Arr add(String v)  { items.add(v == null ? new Null() : new Str(v)); return this; }
        public Arr add(long v)    { items.add(new Num(v));  return this; }
        public Arr add(boolean v) { items.add(new Bool(v)); return this; }
        public Arr add(Node v)    { items.add(v == null ? new Null() : v); return this; }

        public int size() { return items.size(); }
        public Node get(int i) { return i >= 0 && i < items.size() ? items.get(i) : null; }
        public List<Node> items() { return Collections.unmodifiableList(items); }

        @Override
        public String toJson() {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < items.size(); i++) {
                if (i > 0) sb.append(',');
                sb.append(items.get(i).toJson());
            }
            return sb.append(']').toString();
        }
    }

    public static class Str implements Node {
        public final String value;
        public Str(String v) { this.value = v; }
        @Override public String toJson() { return escape(value); }

        static String escape(String s) {
            StringBuilder sb = new StringBuilder("\"");
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                switch (c) {
                    case '"'  -> sb.append("\\\"");
                    case '\\' -> sb.append("\\\\");
                    case '\n' -> sb.append("\\n");
                    case '\r' -> sb.append("\\r");
                    case '\t' -> sb.append("\\t");
                    default   -> { if (c < 0x20) sb.append(String.format("\\u%04x", (int)c)); else sb.append(c); }
                }
            }
            return sb.append('"').toString();
        }
    }

    public static class Num implements Node {
        public final long value;
        public Num(long v) { this.value = v; }
        @Override public String toJson() { return Long.toString(value); }
    }

    public static class Dbl implements Node {
        public final double value;
        public Dbl(double v) { this.value = v; }
        @Override public String toJson() { return Double.toString(value); }
    }

    public static class Bool implements Node {
        public final boolean value;
        public Bool(boolean v) { this.value = v; }
        @Override public String toJson() { return value ? "true" : "false"; }
    }

    public static class Null implements Node {
        @Override public String toJson() { return "null"; }
    }

    // ── Factory methods ──────────────────────────────────────────────────

    public static Obj obj()   { return new Obj(); }
    public static Arr arr()   { return new Arr(); }

    // ── Parser ───────────────────────────────────────────────────────────
    // Minimal recursive-descent JSON parser

    public static Node parse(String json) {
        return new Parser(json.trim()).parseValue();
    }

    private static class Parser {
        private final String s;
        private int pos = 0;

        Parser(String s) { this.s = s; }

        Node parseValue() {
            skipWs();
            if (pos >= s.length()) throw new RuntimeException("Unexpected end of JSON");
            char c = s.charAt(pos);
            return switch (c) {
                case '{' -> parseObject();
                case '[' -> parseArray();
                case '"' -> parseString();
                case 't' -> parseLiteral("true",  new Bool(true));
                case 'f' -> parseLiteral("false", new Bool(false));
                case 'n' -> parseLiteral("null",  new Null());
                default  -> parseNumber();
            };
        }

        Obj parseObject() {
            expect('{'); Obj obj = new Obj(); skipWs();
            if (peek() == '}') { pos++; return obj; }
            while (true) {
                skipWs();
                String key = ((Str) parseString()).value;
                skipWs(); expect(':');
                skipWs(); obj.put(key, parseValue());
                skipWs();
                if (peek() == '}') { pos++; break; }
                expect(',');
            }
            return obj;
        }

        Arr parseArray() {
            expect('['); Arr arr = new Arr(); skipWs();
            if (peek() == ']') { pos++; return arr; }
            while (true) {
                skipWs(); arr.add(parseValue()); skipWs();
                if (peek() == ']') { pos++; break; }
                expect(',');
            }
            return arr;
        }

        Str parseString() {
            expect('"');
            StringBuilder sb = new StringBuilder();
            while (pos < s.length()) {
                char c = s.charAt(pos++);
                if (c == '"') break;
                if (c == '\\') {
                    char esc = s.charAt(pos++);
                    switch (esc) {
                        case '"'  -> sb.append('"');
                        case '\\' -> sb.append('\\');
                        case '/'  -> sb.append('/');
                        case 'n'  -> sb.append('\n');
                        case 'r'  -> sb.append('\r');
                        case 't'  -> sb.append('\t');
                        case 'u'  -> { sb.append((char) Integer.parseInt(s.substring(pos, pos+4), 16)); pos += 4; }
                        default   -> sb.append(esc);
                    }
                } else sb.append(c);
            }
            return new Str(sb.toString());
        }

        Node parseNumber() {
            int start = pos;
            boolean isFloat = false;
            if (pos < s.length() && s.charAt(pos) == '-') pos++;
            while (pos < s.length() && Character.isDigit(s.charAt(pos))) pos++;
            if (pos < s.length() && s.charAt(pos) == '.') { isFloat = true; pos++;
                while (pos < s.length() && Character.isDigit(s.charAt(pos))) pos++;
            }
            if (pos < s.length() && (s.charAt(pos) == 'e' || s.charAt(pos) == 'E')) {
                isFloat = true; pos++;
                if (pos < s.length() && (s.charAt(pos) == '+' || s.charAt(pos) == '-')) pos++;
                while (pos < s.length() && Character.isDigit(s.charAt(pos))) pos++;
            }
            String numStr = s.substring(start, pos);
            if (isFloat) return new Dbl(Double.parseDouble(numStr));
            return new Num(Long.parseLong(numStr));
        }

        Node parseLiteral(String lit, Node val) {
            if (s.startsWith(lit, pos)) { pos += lit.length(); return val; }
            throw new RuntimeException("Invalid literal at pos " + pos);
        }

        void skipWs() { while (pos < s.length() && Character.isWhitespace(s.charAt(pos))) pos++; }
        char peek()   { return pos < s.length() ? s.charAt(pos) : 0; }
        void expect(char c) {
            if (pos >= s.length() || s.charAt(pos) != c)
                throw new RuntimeException("Expected '" + c + "' at pos " + pos + " got " + (pos < s.length() ? s.charAt(pos) : "EOF"));
            pos++;
        }
    }

    // ── Convenience ──────────────────────────────────────────────────────

    /** Safe string extraction from a Node */
    public static String str(Node n, String def) {
        if (n instanceof Str s) return s.value;
        if (n instanceof Obj o) return def;
        return def;
    }

    /** Extract string from Obj field, with default */
    public static String strField(Obj obj, String key, String def) {
        if (obj == null) return def;
        Node n = obj.get(key);
        if (n instanceof Str s) return s.value;
        if (n instanceof Num nm) return Long.toString(nm.value);
        return def;
    }

    /** Extract long from Obj field, with default */
    public static long numField(Obj obj, String key, long def) {
        if (obj == null) return def;
        Node n = obj.get(key);
        if (n instanceof Num nm) return nm.value;
        if (n instanceof Str s)  { try { return Long.parseLong(s.value); } catch (Exception e) { return def; } }
        return def;
    }
}
