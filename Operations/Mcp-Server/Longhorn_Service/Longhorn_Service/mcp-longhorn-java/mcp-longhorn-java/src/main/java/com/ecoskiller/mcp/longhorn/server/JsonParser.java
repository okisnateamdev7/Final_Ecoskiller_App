package com.ecoskiller.mcp.longhorn.server;

import java.util.*;

/**
 * Minimal recursive-descent JSON parser.
 * Supports: null, boolean, number, string, array, object.
 * No external dependencies required.
 */
public final class JsonParser {

    private final String src;
    private int pos;

    private JsonParser(String src) {
        this.src = src;
        this.pos = 0;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> parse(String json) {
        Object val = new JsonParser(json).parseValue();
        if (!(val instanceof Map)) {
            throw new IllegalArgumentException("Expected JSON object at root");
        }
        return (Map<String, Object>) val;
    }

    private Object parseValue() {
        skipWs();
        if (pos >= src.length()) throw new IllegalArgumentException("Unexpected end of JSON");
        char c = src.charAt(pos);
        return switch (c) {
            case '{' -> parseObject();
            case '[' -> parseArray();
            case '"' -> parseString();
            case 't' -> parseLiteral("true",  Boolean.TRUE);
            case 'f' -> parseLiteral("false", Boolean.FALSE);
            case 'n' -> parseLiteral("null",  null);
            default  -> parseNumber();
        };
    }

    private Map<String, Object> parseObject() {
        expect('{');
        Map<String, Object> map = new LinkedHashMap<>();
        skipWs();
        if (peek() == '}') { pos++; return map; }
        while (true) {
            skipWs();
            String key = parseString();
            skipWs();
            expect(':');
            Object val = parseValue();
            map.put(key, val);
            skipWs();
            char next = src.charAt(pos++);
            if (next == '}') break;
            if (next != ',') throw new IllegalArgumentException("Expected ',' or '}' in object at " + pos);
        }
        return map;
    }

    private List<Object> parseArray() {
        expect('[');
        List<Object> list = new ArrayList<>();
        skipWs();
        if (peek() == ']') { pos++; return list; }
        while (true) {
            list.add(parseValue());
            skipWs();
            char next = src.charAt(pos++);
            if (next == ']') break;
            if (next != ',') throw new IllegalArgumentException("Expected ',' or ']' in array at " + pos);
        }
        return list;
    }

    private String parseString() {
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
                    case 'b'  -> '\b';
                    case 'f'  -> '\f';
                    case 'u'  -> {
                        String hex = src.substring(pos, pos + 4);
                        pos += 4;
                        yield (char) Integer.parseInt(hex, 16);
                    }
                    default -> esc;
                });
            } else {
                sb.append(c);
            }
        }
        throw new IllegalArgumentException("Unterminated string");
    }

    private Object parseLiteral(String literal, Object value) {
        if (src.startsWith(literal, pos)) {
            pos += literal.length();
            return value;
        }
        throw new IllegalArgumentException("Invalid literal at " + pos);
    }

    private Number parseNumber() {
        int start = pos;
        if (pos < src.length() && src.charAt(pos) == '-') pos++;
        while (pos < src.length() && Character.isDigit(src.charAt(pos))) pos++;
        boolean isDecimal = false;
        if (pos < src.length() && src.charAt(pos) == '.') {
            isDecimal = true;
            pos++;
            while (pos < src.length() && Character.isDigit(src.charAt(pos))) pos++;
        }
        if (pos < src.length() && (src.charAt(pos) == 'e' || src.charAt(pos) == 'E')) {
            isDecimal = true;
            pos++;
            if (pos < src.length() && (src.charAt(pos) == '+' || src.charAt(pos) == '-')) pos++;
            while (pos < src.length() && Character.isDigit(src.charAt(pos))) pos++;
        }
        String num = src.substring(start, pos);
        // NOTE: must use if/else, NOT ternary — ternary (double, long) causes numeric promotion
        // to double even when isDecimal=false, losing Long type information.
        if (isDecimal) {
            return Double.parseDouble(num);
        } else {
            return Long.parseLong(num);
        }
    }

    private void skipWs() {
        while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) pos++;
    }

    private char peek() {
        skipWs();
        return pos < src.length() ? src.charAt(pos) : 0;
    }

    private void expect(char c) {
        skipWs();
        if (pos >= src.length() || src.charAt(pos) != c) {
            throw new IllegalArgumentException("Expected '" + c + "' at position " + pos);
        }
        pos++;
    }
}
