package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.security.SecurityContext;
import com.ecoskiller.mcp.server.McpTool;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Abstract base for all Certification Engine MCP tools.
 * Provides schema-builder helpers, argument extractors, and security plumbing.
 */
public abstract class BaseTool implements McpTool {

    protected static final ObjectMapper MAPPER = new ObjectMapper();
    protected final SecurityContext securityCtx;

    protected BaseTool(SecurityContext ctx) {
        this.securityCtx = ctx;
    }

    // ─── Schema builders ──────────────────────────────────────────────────

    protected ObjectNode baseSchema(String description) {
        ObjectNode schema = MAPPER.createObjectNode();
        schema.put("name", getName());
        schema.put("description", description);
        ObjectNode input = schema.putObject("inputSchema");
        input.put("type", "object");
        ObjectNode props = input.putObject("properties");
        ObjectNode bt = props.putObject("bearer_token");
        bt.put("type", "string");
        bt.put("description",
            "Keycloak 24.0 JWT Bearer token. Required on every call. " +
            "Must carry tenant_id, role, sub, and exp claims.");
        return schema;
    }

    protected void addProp(ObjectNode schema, String name, String type, String desc) {
        ObjectNode props = (ObjectNode) schema.get("inputSchema").get("properties");
        ObjectNode p = props.putObject(name);
        p.put("type", type);
        p.put("description", desc);
    }

    protected void addStringProp(ObjectNode schema, String name, String desc) {
        addProp(schema, name, "string", desc);
    }

    protected void addIntProp(ObjectNode schema, String name, String desc) {
        addProp(schema, name, "integer", desc);
    }

    protected void addNumberProp(ObjectNode schema, String name, String desc) {
        addProp(schema, name, "number", desc);
    }

    protected void addBoolProp(ObjectNode schema, String name, String desc) {
        addProp(schema, name, "boolean", desc);
    }

    protected void setRequired(ObjectNode schema, String... names) {
        var arr = ((ObjectNode) schema.get("inputSchema")).putArray("required");
        arr.add("bearer_token");
        for (String n : names) arr.add(n);
    }

    // ─── Argument helpers ──────────────────────────────────────────────────

    protected String requireString(JsonNode args, String key) {
        if (!args.has(key) || args.get(key).asText("").isBlank())
            throw new IllegalArgumentException("Missing required argument: " + key);
        return args.get(key).asText();
    }

    protected String optString(JsonNode args, String key, String def) {
        return args.has(key) && !args.get(key).isNull() ? args.get(key).asText(def) : def;
    }

    protected int optInt(JsonNode args, String key, int def) {
        return args.has(key) ? args.get(key).asInt(def) : def;
    }

    protected boolean optBool(JsonNode args, String key, boolean def) {
        return args.has(key) ? args.get(key).asBoolean(def) : def;
    }

    // ─── Cert ID generator ────────────────────────────────────────────────

    protected String newCertId() {
        return "cert-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    protected String nowIso() {
        return java.time.Instant.now().toString();
    }

    protected String futureIso(int plusDays) {
        return java.time.Instant.now()
            .plus(java.time.Duration.ofDays(plusDays)).toString();
    }
}
