package io.ecoskiller.mcp.docker.json;

import java.util.*;

/**
 * Minimal zero-dependency JSON library for the Ecoskiller MCP server.
 * Supports: objects, arrays, strings, numbers, booleans, null.
 * Read (parse) + Write (serialize) — all in pure Java stdlib.
 */
public final class Json {

    private Json() {}

    // ── Public factory methods ───────────────────────────────────────────────

    public static JObject object() { return new JObject(); }
    public static JArray  array()  { return new JArray();  }

    // ── Parse ────────────────────────────────────────────────────────────────

    /** Parse a JSON string into a JValue (JObject, JArray, JString, JNumber, JBool, JNull). */
    public static JValue parse(String json) {
        Parser p = new Parser(json.trim());
        return p.parseValue();
    }

    // ── Types ────────────────────────────────────────────────────────────────

    public interface JValue {
        String toJson();
        default String asText()   { return toString(); }
        default int    asInt()    { return 0; }
        default boolean asBoolean(){ return false; }
        default boolean has(String k) { return false; }
        default JValue  get(String k) { return JNull.INSTANCE; }
        default boolean isNull()   { return false; }
        default boolean isObject() { return false; }
        default boolean isArray()  { return false; }
    }

    // ── JNull ────────────────────────────────────────────────────────────────
    public static final class JNull implements JValue {
        public static final JNull INSTANCE = new JNull();
        private JNull() {}
        @Override public String toJson() { return "null"; }
        @Override public String asText() { return "null"; }
        @Override public boolean isNull() { return true; }
    }

    // ── JBool ────────────────────────────────────────────────────────────────
    public static final class JBool implements JValue {
        private final boolean val;
        public JBool(boolean val) { this.val = val; }
        @Override public String  toJson()           { return val ? "true" : "false"; }
        @Override public String  asText()            { return toJson(); }
        @Override public boolean asBoolean()         { return val; }
    }

    // ── JNumber ──────────────────────────────────────────────────────────────
    public static final class JNumber implements JValue {
        private final Number val;
        public JNumber(Number val) { this.val = val; }
        @Override public String toJson()  { return val.toString(); }
        @Override public String asText()  { return val.toString(); }
        @Override public int    asInt()   { return val.intValue(); }
        @Override public boolean asBoolean() { return val.intValue() != 0; }
    }

    // ── JString ──────────────────────────────────────────────────────────────
    public static final class JString implements JValue {
        private final String val;
        public JString(String val) { this.val = val; }
        @Override public String  toJson()    { return "\"" + escape(val) + "\""; }
        @Override public String  asText()    { return val; }
        @Override public boolean asBoolean() { return !val.isEmpty(); }
        @Override public int     asInt()     {
            try { return Integer.parseInt(val); } catch (NumberFormatException e) { return 0; }
        }
    }

    // ── JObject ──────────────────────────────────────────────────────────────
    public static final class JObject implements JValue {
        private final LinkedHashMap<String, JValue> map = new LinkedHashMap<>();

        public JObject put(String key, String  v)  { map.put(key, v == null ? JNull.INSTANCE : new JString(v)); return this; }
        public JObject put(String key, int     v)  { map.put(key, new JNumber(v));   return this; }
        public JObject put(String key, long    v)  { map.put(key, new JNumber(v));   return this; }
        public JObject put(String key, double  v)  { map.put(key, new JNumber(v));   return this; }
        public JObject put(String key, boolean v)  { map.put(key, new JBool(v));     return this; }
        public JObject put(String key, JValue  v)  { map.put(key, v == null ? JNull.INSTANCE : v); return this; }

        @Override public boolean has(String k)     { return map.containsKey(k); }
        @Override public JValue  get(String k)     { return map.getOrDefault(k, JNull.INSTANCE); }
        @Override public boolean isObject()         { return true; }

        @Override
        public String toJson() {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<String, JValue> e : map.entrySet()) {
                if (!first) sb.append(',');
                sb.append('"').append(escape(e.getKey())).append('"').append(':');
                sb.append(e.getValue().toJson());
                first = false;
            }
            sb.append('}');
            return sb.toString();
        }

        /** Pretty-print with 2-space indentation. */
        public String toPrettyJson() { return prettyPrint(toJson(), 0); }

        @Override public String asText() { return toJson(); }
    }

    // ── JArray ───────────────────────────────────────────────────────────────
    public static final class JArray implements JValue {
        private final List<JValue> list = new ArrayList<>();

        public JArray add(String  v) { list.add(v == null ? JNull.INSTANCE : new JString(v)); return this; }
        public JArray add(int     v) { list.add(new JNumber(v));  return this; }
        public JArray add(boolean v) { list.add(new JBool(v));    return this; }
        public JArray add(JValue  v) { list.add(v == null ? JNull.INSTANCE : v); return this; }

        public int   size()          { return list.size(); }
        public JValue get(int i)     { return list.get(i); }

        @Override public boolean isArray() { return true; }

        @Override
        public String toJson() {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (JValue v : list) {
                if (!first) sb.append(',');
                sb.append(v.toJson());
                first = false;
            }
            sb.append(']');
            return sb.toString();
        }

        @Override public String asText() { return toJson(); }
    }

    // ── Escape ───────────────────────────────────────────────────────────────

    public static String escape(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder(s.length() + 8);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"'  -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default   -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }

    // ── Pretty Printer ───────────────────────────────────────────────────────

    private static String prettyPrint(String json, int baseIndent) {
        StringBuilder sb = new StringBuilder();
        int indent = baseIndent;
        boolean inString = false;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
                sb.append(c);
            } else if (!inString) {
                switch (c) {
                    case '{', '[' -> { sb.append(c).append('\n'); indent++; appendIndent(sb, indent); }
                    case '}', ']' -> { sb.append('\n'); indent--; appendIndent(sb, indent); sb.append(c); }
                    case ','      -> { sb.append(c).append('\n'); appendIndent(sb, indent); }
                    case ':'      -> sb.append(": ");
                    default       -> sb.append(c);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static void appendIndent(StringBuilder sb, int n) {
        sb.append("  ".repeat(Math.max(0, n)));
    }

    // ── Parser ───────────────────────────────────────────────────────────────

    private static final class Parser {
        private final String src;
        private int pos;

        Parser(String src) { this.src = src; this.pos = 0; }

        JValue parseValue() {
            skipWS();
            if (pos >= src.length()) return JNull.INSTANCE;
            char c = src.charAt(pos);
            return switch (c) {
                case '{' -> parseObject();
                case '[' -> parseArray();
                case '"' -> parseString();
                case 't', 'f' -> parseBool();
                case 'n' -> parseNull();
                default  -> parseNumber();
            };
        }

        JObject parseObject() {
            JObject obj = new JObject();
            pos++; // skip {
            skipWS();
            if (peek() == '}') { pos++; return obj; }
            while (pos < src.length()) {
                skipWS();
                String key = ((JString) parseString()).asText();
                skipWS();
                if (peek() == ':') pos++;
                JValue val = parseValue();
                obj.put(key, val);
                skipWS();
                if (peek() == ',') { pos++; } else break;
            }
            skipWS();
            if (peek() == '}') pos++;
            return obj;
        }

        JArray parseArray() {
            JArray arr = new JArray();
            pos++; // skip [
            skipWS();
            if (peek() == ']') { pos++; return arr; }
            while (pos < src.length()) {
                arr.add(parseValue());
                skipWS();
                if (peek() == ',') { pos++; } else break;
            }
            skipWS();
            if (peek() == ']') pos++;
            return arr;
        }

        JString parseString() {
            pos++; // skip "
            StringBuilder sb = new StringBuilder();
            while (pos < src.length()) {
                char c = src.charAt(pos++);
                if (c == '"') break;
                if (c == '\\' && pos < src.length()) {
                    char esc = src.charAt(pos++);
                    switch (esc) {
                        case '"'  -> sb.append('"');
                        case '\\' -> sb.append('\\');
                        case '/'  -> sb.append('/');
                        case 'n'  -> sb.append('\n');
                        case 'r'  -> sb.append('\r');
                        case 't'  -> sb.append('\t');
                        case 'u'  -> {
                            String hex = src.substring(pos, Math.min(pos + 4, src.length()));
                            sb.append((char) Integer.parseInt(hex, 16));
                            pos += 4;
                        }
                        default   -> sb.append(esc);
                    }
                } else {
                    sb.append(c);
                }
            }
            return new JString(sb.toString());
        }

        JBool parseBool() {
            if (src.startsWith("true", pos))  { pos += 4; return new JBool(true);  }
            if (src.startsWith("false", pos)) { pos += 5; return new JBool(false); }
            pos++; return new JBool(false);
        }

        JNull parseNull() {
            if (src.startsWith("null", pos)) pos += 4;
            return JNull.INSTANCE;
        }

        JNumber parseNumber() {
            int start = pos;
            if (peek() == '-') pos++;
            while (pos < src.length() && (Character.isDigit(src.charAt(pos)) || src.charAt(pos) == '.' || src.charAt(pos) == 'e' || src.charAt(pos) == 'E' || src.charAt(pos) == '+' || src.charAt(pos) == '-')) pos++;
            String num = src.substring(start, pos);
            try {
                if (num.contains(".") || num.contains("e") || num.contains("E"))
                    return new JNumber(Double.parseDouble(num));
                return new JNumber(Long.parseLong(num));
            } catch (NumberFormatException e) {
                return new JNumber(0);
            }
        }

        void skipWS() { while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) pos++; }
        char peek()   { return pos < src.length() ? src.charAt(pos) : 0; }
    }
}
