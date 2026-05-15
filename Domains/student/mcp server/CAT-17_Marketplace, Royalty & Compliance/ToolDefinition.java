package com.ecoskiller.mcp17;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Base class for every CAT-17 agent tool.
 */
public abstract class ToolDefinition {

    public abstract String getName();
    public abstract String getDescription();
    public abstract ObjectNode getInputSchema(ObjectMapper mapper);

    /** Execute the tool and return a result payload. */
    public abstract ObjectNode execute(JsonNode args, ObjectMapper mapper);

    /** Serialise this tool to the MCP tools/list format. */
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.put("name", getName());
        node.put("description", getDescription());
        node.set("inputSchema", getInputSchema(mapper));
        return node;
    }

    // ── Schema helpers ───────────────────────────────────────────────────────

    protected ObjectNode baseSchema(ObjectMapper mapper, String... required) {
        ObjectNode schema = mapper.createObjectNode();
        schema.put("type", "object");
        schema.putObject("properties");
        if (required.length > 0) {
            var arr = schema.putArray("required");
            for (String r : required) arr.add(r);
        }
        return schema;
    }

    protected void addStringProp(ObjectNode schema, String name, String description) {
        ObjectNode prop = ((ObjectNode) schema.get("properties")).putObject(name);
        prop.put("type", "string");
        prop.put("description", description);
    }

    protected void addNumberProp(ObjectNode schema, String name, String description) {
        ObjectNode prop = ((ObjectNode) schema.get("properties")).putObject(name);
        prop.put("type", "number");
        prop.put("description", description);
    }

    protected void addBoolProp(ObjectNode schema, String name, String description) {
        ObjectNode prop = ((ObjectNode) schema.get("properties")).putObject(name);
        prop.put("type", "boolean");
        prop.put("description", description);
    }

    protected void addArrayProp(ObjectNode schema, String name, String description, String itemType) {
        ObjectNode prop = ((ObjectNode) schema.get("properties")).putObject(name);
        prop.put("type", "array");
        prop.put("description", description);
        prop.putObject("items").put("type", itemType);
    }

    // ── Response helpers ─────────────────────────────────────────────────────

    protected ObjectNode success(ObjectMapper mapper, String message) {
        ObjectNode r = mapper.createObjectNode();
        r.put("status", "success");
        r.put("message", message);
        r.put("agent", getName());
        r.put("timestamp", java.time.Instant.now().toString());
        return r;
    }

    protected ObjectNode successWithData(ObjectMapper mapper, String message, ObjectNode data) {
        ObjectNode r = success(mapper, message);
        r.set("data", data);
        return r;
    }

    protected String str(JsonNode args, String key, String defaultVal) {
        JsonNode n = args.get(key);
        return (n != null && !n.isNull()) ? n.asText() : defaultVal;
    }

    protected double num(JsonNode args, String key, double defaultVal) {
        JsonNode n = args.get(key);
        return (n != null && !n.isNull()) ? n.asDouble() : defaultVal;
    }

    protected boolean bool(JsonNode args, String key, boolean defaultVal) {
        JsonNode n = args.get(key);
        return (n != null && !n.isNull()) ? n.asBoolean() : defaultVal;
    }
}
