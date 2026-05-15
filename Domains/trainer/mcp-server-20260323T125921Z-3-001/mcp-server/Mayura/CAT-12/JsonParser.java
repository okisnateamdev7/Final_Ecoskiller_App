package com.ecoskiller.mcp;

import java.util.*;

/**
 * Zero-dependency JSON parser for MCP requests.
 * Supports flat and nested objects, arrays, strings, numbers, booleans, null.
 */
public class JsonParser {

    private final String src;
    private int pos = 0;

    private JsonParser(String src) { this.src = src.trim(); }

    public static Map<String, Object> parse(String json) {
        JsonParser p = new JsonParser(json);
        Object result = p.parseValue();
        if (result instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) result;
            return map;
        }
        return new LinkedHashMap<>();
    }

    private Object parseValue() {
        skipWhitespace();
        if (pos >= src.length()) return null;
        char c = src.charAt(pos);
        if (c == '"')  return parseString();
        if (c == '{')  return parseObject();
        if (c == '[')  return parseArray();
        if (c == 't')  { pos += 4; return true; }
        if (c == 'f')  { pos += 5; return false; }
        if (c == 'n')  { pos += 4; return null; }
        return parseNumber();
    }

    private String parseString() {
        pos++; // skip opening "
        StringBuilder sb = new StringBuilder();
        while (pos < src.length()) {
            char c = src.charAt(pos++);
            if (c == '"') break;
            if (c == '\\' && pos < src.length()) {
                char e = src.charAt(pos++);
                switch (e) {
                    case '"':  sb.append('"');  break;
                    case '\\': sb.append('\\'); break;
                    case 'n':  sb.append('\n'); break;
                    case 'r':  sb.append('\r'); break;
                    case 't':  sb.append('\t'); break;
                    default:   sb.append(e);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private Map<String, Object> parseObject() {
        pos++; // skip {
        Map<String, Object> map = new LinkedHashMap<>();
        skipWhitespace();
        while (pos < src.length() && src.charAt(pos) != '}') {
            skipWhitespace();
            String key = parseString();
            skipWhitespace();
            if (pos < src.length() && src.charAt(pos) == ':') pos++; // skip :
            skipWhitespace();
            Object value = parseValue();
            map.put(key, value);
            skipWhitespace();
            if (pos < src.length() && src.charAt(pos) == ',') pos++; // skip ,
            skipWhitespace();
        }
        if (pos < src.length()) pos++; // skip }
        return map;
    }

    private List<Object> parseArray() {
        pos++; // skip [
        List<Object> list = new ArrayList<>();
        skipWhitespace();
        while (pos < src.length() && src.charAt(pos) != ']') {
            list.add(parseValue());
            skipWhitespace();
            if (pos < src.length() && src.charAt(pos) == ',') pos++;
            skipWhitespace();
        }
        if (pos < src.length()) pos++; // skip ]
        return list;
    }

    private Object parseNumber() {
        int start = pos;
        boolean isFloat = false;
        if (pos < src.length() && src.charAt(pos) == '-') pos++;
        while (pos < src.length() && (Character.isDigit(src.charAt(pos)) || src.charAt(pos) == '.' || src.charAt(pos) == 'e' || src.charAt(pos) == 'E' || src.charAt(pos) == '+' || src.charAt(pos) == '-')) {
            if (src.charAt(pos) == '.' || src.charAt(pos) == 'e' || src.charAt(pos) == 'E') isFloat = true;
            pos++;
        }
        String numStr = src.substring(start, pos);
        try {
            if (isFloat) return Double.parseDouble(numStr);
            return Long.parseLong(numStr);
        } catch (NumberFormatException e) {
            return numStr;
        }
    }

    private void skipWhitespace() {
        while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) pos++;
    }
}
