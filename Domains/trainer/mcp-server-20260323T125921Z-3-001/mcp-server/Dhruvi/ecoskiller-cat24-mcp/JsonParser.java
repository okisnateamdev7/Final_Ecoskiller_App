package com.ecoskiller.mcp.protocol;

import java.util.*;

/**
 * Zero-dependency JSON parser sufficient for MCP JSON-RPC messages.
 * Handles objects, arrays, strings, numbers, booleans, null.
 */
public class JsonParser {

    private final String src;
    private int pos = 0;

    private JsonParser(String src) {
        this.src = src;
    }

    public static Map<String, Object> parse(String json) {
        JsonParser p = new JsonParser(json.trim());
        Object val = p.parseValue();
        if (val instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) val;
            return map;
        }
        throw new RuntimeException("Top-level value is not a JSON object");
    }

    private Object parseValue() {
        skipWhitespace();
        if (pos >= src.length()) throw new RuntimeException("Unexpected end of input");
        char c = src.charAt(pos);
        if (c == '{') return parseObject();
        if (c == '[') return parseArray();
        if (c == '"') return parseString();
        if (c == 't') { pos += 4; return Boolean.TRUE; }
        if (c == 'f') { pos += 5; return Boolean.FALSE; }
        if (c == 'n') { pos += 4; return null; }
        return parseNumber();
    }

    private Map<String, Object> parseObject() {
        Map<String, Object> map = new LinkedHashMap<>();
        pos++; // '{'
        skipWhitespace();
        if (pos < src.length() && src.charAt(pos) == '}') { pos++; return map; }
        while (pos < src.length()) {
            skipWhitespace();
            String key = parseString();
            skipWhitespace();
            pos++; // ':'
            Object val = parseValue();
            map.put(key, val);
            skipWhitespace();
            if (pos >= src.length()) break;
            char c = src.charAt(pos);
            if (c == '}') { pos++; break; }
            if (c == ',') pos++;
        }
        return map;
    }

    private List<Object> parseArray() {
        List<Object> list = new ArrayList<>();
        pos++; // '['
        skipWhitespace();
        if (pos < src.length() && src.charAt(pos) == ']') { pos++; return list; }
        while (pos < src.length()) {
            list.add(parseValue());
            skipWhitespace();
            if (pos >= src.length()) break;
            char c = src.charAt(pos);
            if (c == ']') { pos++; break; }
            if (c == ',') pos++;
        }
        return list;
    }

    private String parseString() {
        pos++; // opening '"'
        StringBuilder sb = new StringBuilder();
        while (pos < src.length()) {
            char c = src.charAt(pos++);
            if (c == '"') break;
            if (c == '\\') {
                char esc = src.charAt(pos++);
                switch (esc) {
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case '/': sb.append('/'); break;
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    case 'b': sb.append('\b'); break;
                    case 'f': sb.append('\f'); break;
                    case 'u':
                        String hex = src.substring(pos, pos + 4);
                        sb.append((char) Integer.parseInt(hex, 16));
                        pos += 4;
                        break;
                    default: sb.append(esc);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private Object parseNumber() {
        int start = pos;
        while (pos < src.length() && ",}] \t\n\r".indexOf(src.charAt(pos)) < 0) pos++;
        String token = src.substring(start, pos);
        try {
            if (token.contains(".") || token.contains("e") || token.contains("E")) {
                return Double.parseDouble(token);
            }
            return Long.parseLong(token);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid number: " + token);
        }
    }

    private void skipWhitespace() {
        while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) pos++;
    }
}
