package com.ecoskiller.mcp.longhorn.tools;

import java.util.*;

/**
 * Base class providing schema-building helpers shared by all Longhorn tools.
 */
public abstract class BaseTool implements ToolHandler {

    @Override
    public Map<String, Object> getSchema(String toolName) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("name", toolName);
        schema.put("description", description());

        Map<String, Object> inputSchema = new LinkedHashMap<>();
        inputSchema.put("type", "object");
        inputSchema.put("properties", properties());
        inputSchema.put("required", requiredParams());
        schema.put("inputSchema", inputSchema);

        return schema;
    }

    protected abstract String description();
    protected abstract Map<String, Object> properties();
    protected List<String> requiredParams() { return Collections.emptyList(); }

    // ─── schema helpers ─────────────────────────────────────────────────

    protected Map<String, Object> strProp(String desc) {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("type", "string");
        p.put("description", desc);
        return p;
    }

    protected Map<String, Object> strPropEnum(String desc, String... values) {
        Map<String, Object> p = strProp(desc);
        p.put("enum", Arrays.asList(values));
        return p;
    }

    protected Map<String, Object> intProp(String desc, int min, int max) {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("type", "integer");
        p.put("description", desc);
        p.put("minimum", min);
        p.put("maximum", max);
        return p;
    }

    protected Map<String, Object> boolProp(String desc) {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("type", "boolean");
        p.put("description", desc);
        return p;
    }

    // ─── argument helpers ────────────────────────────────────────────────

    protected String require(Map<String, Object> args, String key) {
        Object v = args.get(key);
        if (v == null || v.toString().isBlank())
            throw new IllegalArgumentException("Missing required argument: " + key);
        return v.toString().trim();
    }

    protected String optional(Map<String, Object> args, String key, String defaultVal) {
        Object v = args.get(key);
        return (v == null) ? defaultVal : v.toString().trim();
    }

    protected int optionalInt(Map<String, Object> args, String key, int defaultVal) {
        Object v = args.get(key);
        if (v == null) return defaultVal;
        try { return ((Number) v).intValue(); }
        catch (ClassCastException e) { return Integer.parseInt(v.toString()); }
    }

    protected boolean optionalBool(Map<String, Object> args, String key, boolean defaultVal) {
        Object v = args.get(key);
        if (v == null) return defaultVal;
        return Boolean.parseBoolean(v.toString());
    }

    // ─── response builder ────────────────────────────────────────────────

    protected Map<String, Object> success(String message, Map<String, Object> extra) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("status", "success");
        r.put("message", message);
        r.put("timestamp", java.time.Instant.now().toString());
        if (extra != null) r.putAll(extra);
        return r;
    }
}
