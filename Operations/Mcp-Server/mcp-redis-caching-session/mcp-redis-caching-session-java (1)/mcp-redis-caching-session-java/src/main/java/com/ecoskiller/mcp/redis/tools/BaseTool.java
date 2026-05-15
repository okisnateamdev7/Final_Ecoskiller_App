package com.ecoskiller.mcp.redis.tools;

import com.ecoskiller.mcp.redis.config.RedisConfig;
import com.ecoskiller.mcp.redis.security.AuditLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import redis.clients.jedis.Jedis;

/**
 * Shared base for all Redis MCP tools.
 * Provides:
 *  - Connection pool access
 *  - JSON schema builder helpers
 *  - Input validation & sanitisation utilities
 *  - Consistent error formatting
 */
public abstract class BaseTool implements McpTool {

    protected final RedisConfig  cfg;
    protected final AuditLogger  audit;
    protected final ObjectMapper json = new ObjectMapper();

    // Maximum key length to prevent DoS via very long key names
    private static final int MAX_KEY_LENGTH = 512;

    protected BaseTool(RedisConfig cfg, AuditLogger audit) {
        this.cfg   = cfg;
        this.audit = audit;
    }

    // -------------------------------------------------------------------------
    // Connection helper — always use try-with-resources
    // -------------------------------------------------------------------------

    /** Return a Jedis connection from the pool. Must be closed after use. */
    protected Jedis conn() {
        return cfg.getConnection();
    }

    // -------------------------------------------------------------------------
    // Input validation
    // -------------------------------------------------------------------------

    /**
     * Validate and sanitise a Redis key segment.
     * Prevents: empty keys, keys with control characters, overly long keys.
     */
    protected String validateKey(String key, String fieldName) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be empty");
        }
        if (key.length() > MAX_KEY_LENGTH) {
            throw new IllegalArgumentException(fieldName + " exceeds max length " + MAX_KEY_LENGTH);
        }
        // Reject characters that could cause command injection via key names
        if (key.chars().anyMatch(c -> c < 32 && c != 9)) {
            throw new IllegalArgumentException(fieldName + " contains illegal control characters");
        }
        return key.strip();
    }

    /** Get a required string argument or throw. */
    protected String requireString(com.fasterxml.jackson.databind.JsonNode args, String field) {
        com.fasterxml.jackson.databind.JsonNode node = args.path(field);
        if (node.isMissingNode() || node.isNull() || node.asText("").isEmpty()) {
            throw new IllegalArgumentException("Required argument '" + field + "' is missing");
        }
        return node.asText();
    }

    /** Get an optional string argument with a default. */
    protected String optString(com.fasterxml.jackson.databind.JsonNode args, String field, String def) {
        com.fasterxml.jackson.databind.JsonNode node = args.path(field);
        return node.isMissingNode() || node.isNull() ? def : node.asText(def);
    }

    /** Get an optional long argument with a default. */
    protected long optLong(com.fasterxml.jackson.databind.JsonNode args, String field, long def) {
        com.fasterxml.jackson.databind.JsonNode node = args.path(field);
        return node.isMissingNode() || node.isNull() ? def : node.asLong(def);
    }

    /** Get an optional int argument with a default. */
    protected int optInt(com.fasterxml.jackson.databind.JsonNode args, String field, int def) {
        com.fasterxml.jackson.databind.JsonNode node = args.path(field);
        return node.isMissingNode() || node.isNull() ? def : node.asInt(def);
    }

    // -------------------------------------------------------------------------
    // JSON Schema builder helpers
    // -------------------------------------------------------------------------

    protected ObjectNode buildSchema(String toolName, String description, ObjectNode properties,
                                     String... requiredFields) {
        ObjectNode schema = json.createObjectNode();
        schema.put("name", toolName);
        schema.put("description", description);

        ObjectNode inputSchema = json.createObjectNode();
        inputSchema.put("type", "object");
        inputSchema.set("properties", properties);

        ArrayNode required = json.createArrayNode();
        // api_key is always required
        required.add("api_key");
        for (String f : requiredFields) required.add(f);
        inputSchema.set("required", required);

        schema.set("inputSchema", inputSchema);
        return schema;
    }

    /** Build a string property node. */
    protected ObjectNode strProp(String description) {
        ObjectNode n = json.createObjectNode();
        n.put("type", "string");
        n.put("description", description);
        return n;
    }

    /** Build a string property node with enum values. */
    protected ObjectNode enumProp(String description, String... values) {
        ObjectNode n = json.createObjectNode();
        n.put("type", "string");
        n.put("description", description);
        ArrayNode enums = json.createArrayNode();
        for (String v : values) enums.add(v);
        n.set("enum", enums);
        return n;
    }

    /** Build a number property node. */
    protected ObjectNode numProp(String description) {
        ObjectNode n = json.createObjectNode();
        n.put("type", "number");
        n.put("description", description);
        return n;
    }

    /** Build an integer property node. */
    protected ObjectNode intProp(String description) {
        ObjectNode n = json.createObjectNode();
        n.put("type", "integer");
        n.put("description", description);
        return n;
    }

    /** Standard api_key property — included in every tool schema. */
    protected ObjectNode apiKeyProp() {
        ObjectNode n = json.createObjectNode();
        n.put("type",        "string");
        n.put("description", "MCP server API key (MCP_API_KEY env var). Required for all calls.");
        return n;
    }

    // -------------------------------------------------------------------------
    // Namespaced key helpers
    // -------------------------------------------------------------------------

    /** Build a fully-qualified Redis key: ecoskiller:{namespace}:{key} */
    protected String key(String namespace, String key) {
        return cfg.ns(namespace, validateKey(key, "key"));
    }
}
