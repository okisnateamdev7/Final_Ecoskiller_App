package com.ecoskiller.mcp.json;

import java.util.*;

/**
 * JsonValue — minimal zero-dependency JSON builder/parser (Java 17).
 * Used throughout the MCP server so no external Jackson dependency is needed.
 */
public abstract class JsonValue {

    // ── Factories ─────────────────────────────────────────────────────────
    public static JsonObject    object() { return new JsonObject(); }
    public static JsonArray     array()  { return new JsonArray();  }
    public static JsonPrimitive of(String  v) { return new JsonPrimitive(v,     Type.STRING); }
    public static JsonPrimitive of(long    v) { return new JsonPrimitive(v,     Type.NUMBER); }
    public static JsonPrimitive of(int     v) { return new JsonPrimitive((long)v, Type.NUMBER); }
    public static JsonPrimitive of(double  v) { return new JsonPrimitive(v,     Type.NUMBER); }
    public static JsonPrimitive of(boolean v) { return new JsonPrimitive(v,     Type.BOOL);   }
    public static JsonPrimitive ofNull()      { return new JsonPrimitive(null,  Type.NULL);   }

    enum Type { STRING, NUMBER, BOOL, NULL }

    public abstract String toJson();
    @Override public String toString() { return toJson(); }

    // ════════════════════════════════════════════════════════════════════
    // JsonObject
    // ════════════════════════════════════════════════════════════════════
    public static class JsonObject extends JsonValue {
        private final LinkedHashMap<String, JsonValue> map = new LinkedHashMap<>();

        public JsonObject put(String k, String  v) { map.put(k, of(v));    return this; }
        public JsonObject put(String k, long    v) { map.put(k, of(v));    return this; }
        public JsonObject put(String k, int     v) { map.put(k, of(v));    return this; }
        public JsonObject put(String k, boolean v) { map.put(k, of(v));    return this; }
        public JsonObject putNull(String k)        { map.put(k, ofNull()); return this; }
        public JsonObject set(String k, JsonValue v) { if (v != null) map.put(k, v); return this; }

        public boolean    has(String k)    { return map.containsKey(k); }
        public JsonValue  get(String k)    { return map.get(k); }
        public Map<String, JsonValue> entries() { return Collections.unmodifiableMap(map); }

        public String getString(String k) {
            JsonValue v = map.get(k);
            if (v instanceof JsonPrimitive p) return p.raw == null ? null : p.raw.toString();
            return null;
        }
        public long getLong(String k) {
            JsonValue v = map.get(k);
            if (v instanceof JsonPrimitive p && p.raw instanceof Number n) return n.longValue();
            return 0L;
        }
        public int  getInt(String k)     { return (int) getLong(k); }
        public boolean getBoolean(String k) {
            JsonValue v = map.get(k);
            if (v instanceof JsonPrimitive p && p.raw instanceof Boolean b) return b;
            return false;
        }
        public JsonObject getObject(String k) {
            JsonValue v = map.get(k);
            return v instanceof JsonObject o ? o : null;
        }
        public JsonArray getArray(String k) {
            JsonValue v = map.get(k);
            return v instanceof JsonArray a ? a : null;
        }

        /** Require a non-blank string field or throw IllegalArgumentException. */
        public String require(String k) {
            String v = getString(k);
            if (v == null || v.isBlank())
                throw new IllegalArgumentException("Required field missing: " + k);
            return v;
        }

        @Override public String toJson() {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (var e : map.entrySet()) {
                if (!first) sb.append(",");
                sb.append(quote(e.getKey())).append(":").append(e.getValue().toJson());
                first = false;
            }
            return sb.append("}").toString();
        }
    }

    // ════════════════════════════════════════════════════════════════════
    // JsonArray
    // ════════════════════════════════════════════════════════════════════
    public static class JsonArray extends JsonValue {
        private final List<JsonValue> list = new ArrayList<>();

        public JsonArray add(String   v) { list.add(of(v)); return this; }
        public JsonArray add(long     v) { list.add(of(v)); return this; }
        public JsonArray add(boolean  v) { list.add(of(v)); return this; }
        public JsonArray add(JsonValue v) { if (v != null) list.add(v); return this; }

        public int       size()     { return list.size(); }
        public JsonValue get(int i) { return list.get(i); }
        public List<JsonValue> items() { return Collections.unmodifiableList(list); }

        @Override public String toJson() {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(list.get(i).toJson());
            }
            return sb.append("]").toString();
        }
    }

    // ════════════════════════════════════════════════════════════════════
    // JsonPrimitive
    // ════════════════════════════════════════════════════════════════════
    public static class JsonPrimitive extends JsonValue {
        final Object raw;
        final Type   type;
        JsonPrimitive(Object raw, Type type) { this.raw = raw; this.type = type; }

        public String  asText()    { return raw == null ? "" : raw.toString(); }
        public long    asLong()    { return raw instanceof Number n ? n.longValue() : 0L; }
        public int     asInt()     { return (int) asLong(); }
        public boolean asBoolean() { return raw instanceof Boolean b && b; }

        @Override public String toJson() {
            return switch (type) {
                case NULL   -> "null";
                case BOOL   -> raw.toString();
                case NUMBER -> raw.toString();
                case STRING -> quote((String) raw);
            };
        }
    }

    // ── String quoting helper ─────────────────────────────────────────────
    public static String quote(String s) {
        if (s == null) return "null";
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
                    else sb.append(c);
                }
            }
        }
        return sb.append("\"").toString();
    }

    // ════════════════════════════════════════════════════════════════════
    // Parser
    // ════════════════════════════════════════════════════════════════════
    public static JsonValue parse(String json) {
        if (json == null || json.isBlank()) throw new IllegalArgumentException("Empty JSON");
        return new Parser(json.trim()).parseValue();
    }

    private static class Parser {
        final String s; int pos;
        Parser(String s) { this.s = s; }

        JsonValue parseValue() {
            skipWs();
            if (pos >= s.length()) throw new IllegalArgumentException("Unexpected end of JSON");
            return switch (s.charAt(pos)) {
                case '{' -> parseObject();
                case '[' -> parseArray();
                case '"' -> of(parseString());
                case 't' -> { consume("true");  yield of(true);  }
                case 'f' -> { consume("false"); yield of(false); }
                case 'n' -> { consume("null");  yield ofNull();  }
                default  -> parseNumber();
            };
        }

        JsonObject parseObject() {
            expect('{'); skipWs();
            JsonObject obj = new JsonObject();
            while (pos < s.length() && s.charAt(pos) != '}') {
                skipWs(); String k = parseString(); skipWs(); expect(':'); skipWs();
                obj.set(k, parseValue()); skipWs();
                if (pos < s.length() && s.charAt(pos) == ',') { pos++; skipWs(); }
            }
            expect('}'); return obj;
        }

        JsonArray parseArray() {
            expect('['); skipWs();
            JsonArray arr = new JsonArray();
            while (pos < s.length() && s.charAt(pos) != ']') {
                arr.add(parseValue()); skipWs();
                if (pos < s.length() && s.charAt(pos) == ',') { pos++; skipWs(); }
            }
            expect(']'); return arr;
        }

        String parseString() {
            expect('"');
            StringBuilder sb = new StringBuilder();
            while (pos < s.length() && s.charAt(pos) != '"') {
                char c = s.charAt(pos++);
                if (c == '\\') {
                    char e = s.charAt(pos++);
                    switch (e) {
                        case '"'  -> sb.append('"');  case '\\' -> sb.append('\\');
                        case 'n'  -> sb.append('\n'); case 'r'  -> sb.append('\r');
                        case 't'  -> sb.append('\t');
                        case 'u'  -> { String h = s.substring(pos, pos + 4); pos += 4; sb.append((char) Integer.parseInt(h, 16)); }
                        default   -> sb.append(e);
                    }
                } else sb.append(c);
            }
            expect('"'); return sb.toString();
        }

        JsonPrimitive parseNumber() {
            int start = pos;
            if (pos < s.length() && s.charAt(pos) == '-') pos++;
            while (pos < s.length() && Character.isDigit(s.charAt(pos))) pos++;
            boolean fl = false;
            if (pos < s.length() && s.charAt(pos) == '.') {
                fl = true; pos++;
                while (pos < s.length() && Character.isDigit(s.charAt(pos))) pos++;
            }
            if (pos < s.length() && (s.charAt(pos) == 'e' || s.charAt(pos) == 'E')) {
                fl = true; pos++;
                if (pos < s.length() && (s.charAt(pos) == '+' || s.charAt(pos) == '-')) pos++;
                while (pos < s.length() && Character.isDigit(s.charAt(pos))) pos++;
            }
            String n = s.substring(start, pos);
            return fl ? of(Double.parseDouble(n)) : of(Long.parseLong(n));
        }

        void skipWs()  { while (pos < s.length() && Character.isWhitespace(s.charAt(pos))) pos++; }
        void expect(char c) {
            if (pos >= s.length() || s.charAt(pos) != c)
                throw new IllegalArgumentException("Expected '" + c + "' at pos " + pos);
            pos++;
        }
        void consume(String w) {
            if (!s.startsWith(w, pos)) throw new IllegalArgumentException("Expected '" + w + "' at pos " + pos);
            pos += w.length();
        }
    }
}
