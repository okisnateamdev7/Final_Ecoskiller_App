package com.ecoskiller.mcp;

import java.util.*;

/**
 * Zero-dependency JSON serializer for MCP responses.
 */
public class JsonSerializer {

    @SuppressWarnings("unchecked")
    public static String toJson(Object obj) {
        if (obj == null)                         return "null";
        if (obj instanceof String)               return "\"" + escape((String) obj) + "\"";
        if (obj instanceof Boolean)              return obj.toString();
        if (obj instanceof Number)               return obj.toString();
        if (obj instanceof Map)                  return mapToJson((Map<String, Object>) obj);
        if (obj instanceof List)                 return listToJson((List<?>) obj);
        if (obj instanceof Object[])             return listToJson(Arrays.asList((Object[]) obj));
        return "\"" + escape(obj.toString()) + "\"";
    }

    private static String mapToJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("{");
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> e = it.next();
            sb.append("\"").append(escape(e.getKey())).append("\":").append(toJson(e.getValue()));
            if (it.hasNext()) sb.append(",");
        }
        return sb.append("}").toString();
    }

    private static String listToJson(List<?> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(toJson(list.get(i)));
            if (i < list.size() - 1) sb.append(",");
        }
        return sb.append("]").toString();
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
