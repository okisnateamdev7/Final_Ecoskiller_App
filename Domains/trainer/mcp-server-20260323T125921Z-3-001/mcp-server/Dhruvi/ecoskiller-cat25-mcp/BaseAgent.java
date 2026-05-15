package com.ecoskiller.mcp.agents;

import java.util.*;

/**
 * Convenience base class for schema building and common agent utilities.
 */
public abstract class BaseAgent implements Agent {

    // ── Schema builders ────────────────────────────────────────────────────

    protected Map<String, Object> schema(String... requiredAndProps) {
        // requiredAndProps: pairs of (name, type) where type may be "string","number","boolean","array","object"
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        Map<String, Object> props = new LinkedHashMap<>();
        List<String> required = new ArrayList<>();

        for (int i = 0; i + 1 < requiredAndProps.length; i += 2) {
            String name = requiredAndProps[i];
            String type = requiredAndProps[i + 1];
            Map<String, Object> prop = new LinkedHashMap<>();
            prop.put("type", type);
            props.put(name, prop);
            required.add(name);
        }
        schema.put("properties", props);
        if (!required.isEmpty()) schema.put("required", required);
        return schema;
    }

    protected Map<String, Object> schemaOptional() {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        schema.put("properties", new LinkedHashMap<>());
        return schema;
    }

    // ── Argument extractors ───────────────────────────────────────────────

    protected String str(Map<String, Object> args, String key, String defaultVal) {
        Object v = args.get(key);
        return v != null ? v.toString() : defaultVal;
    }

    protected int intVal(Map<String, Object> args, String key, int defaultVal) {
        Object v = args.get(key);
        if (v == null) return defaultVal;
        if (v instanceof Number) return ((Number) v).intValue();
        try { return Integer.parseInt(v.toString()); } catch (Exception e) { return defaultVal; }
    }

    protected boolean bool(Map<String, Object> args, String key, boolean defaultVal) {
        Object v = args.get(key);
        if (v == null) return defaultVal;
        if (v instanceof Boolean) return (Boolean) v;
        return Boolean.parseBoolean(v.toString());
    }

    // ── Result formatter ──────────────────────────────────────────────────

    protected String result(String agentName, String status, String... lines) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(agentName).append("] status=").append(status).append("\n");
        for (String line : lines) {
            sb.append("  ").append(line).append("\n");
        }
        return sb.toString().trim();
    }
}
