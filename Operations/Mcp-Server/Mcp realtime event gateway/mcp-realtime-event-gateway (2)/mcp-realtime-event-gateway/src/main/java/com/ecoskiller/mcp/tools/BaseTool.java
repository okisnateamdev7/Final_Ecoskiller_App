package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Shared base for all realtime-event-gateway MCP tools.
 * Provides: secure input validation, sanitization, response builders, schema helpers.
 */
public abstract class BaseTool implements McpTool {

    protected final ObjectMapper mapper = JsonUtil.createSecureMapper();

    // -----------------------------------------------------------------------
    // Response builders
    // -----------------------------------------------------------------------
    protected ObjectNode ok(String message) {
        ObjectNode r = mapper.createObjectNode();
        r.put("success",    true);
        r.put("message",    message);
        r.put("timestamp",  Instant.now().toString());
        r.put("request_id", UUID.randomUUID().toString());
        return r;
    }
    protected ObjectNode fail(String error) {
        ObjectNode r = mapper.createObjectNode();
        r.put("success",   false);
        r.put("error",     error);
        r.put("timestamp", Instant.now().toString());
        return r;
    }

    // -----------------------------------------------------------------------
    // Schema builder helper
    // -----------------------------------------------------------------------
    protected ObjectNode buildSchema(String name, String description, ObjectNode inputSchema) {
        ObjectNode s = mapper.createObjectNode();
        s.put("name",        name);
        s.put("description", description);
        s.set("inputSchema", inputSchema);
        return s;
    }

    // -----------------------------------------------------------------------
    // Input helpers — all validate + sanitize
    // -----------------------------------------------------------------------
    protected String requireString(JsonNode args, String field) {
        JsonNode n = args.path(field);
        if (n.isMissingNode() || n.isNull() || n.asText().isBlank())
            throw new IllegalArgumentException("Required field missing: " + field);
        String v = n.asText().trim();
        if (v.length() > 4096)
            throw new IllegalArgumentException("Field too long: " + field + " (max 4096)");
        return sanitize(v);
    }
    protected String optString(JsonNode args, String field, String def) {
        JsonNode n = args.path(field);
        if (n.isMissingNode() || n.isNull()) return def;
        return sanitize(n.asText().trim());
    }
    protected int optInt(JsonNode args, String field, int def) {
        JsonNode n = args.path(field);
        if (n.isMissingNode() || n.isNull()) return def;
        return n.asInt(def);
    }
    protected long optLong(JsonNode args, String field, long def) {
        JsonNode n = args.path(field);
        if (n.isMissingNode() || n.isNull()) return def;
        return n.asLong(def);
    }
    protected boolean optBool(JsonNode args, String field, boolean def) {
        JsonNode n = args.path(field);
        if (n.isMissingNode() || n.isNull()) return def;
        return n.asBoolean(def);
    }

    /** Strip null bytes and control characters */
    private String sanitize(String s) {
        if (s == null) return null;
        return s.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]", "");
    }

    // -----------------------------------------------------------------------
    // Domain validators
    // -----------------------------------------------------------------------
    protected void validateEventType(String type) {
        Set<String> valid = Set.of(
            "assessment.score.updated", "assessment.solution.submitted",
            "assessment.started",       "assessment.completed",
            "gd.speaker.changed",       "gd.session.started",       "gd.session.ended",
            "interview.status.changed", "interview.started",        "interview.ended",
            "timer.tick",               "timer.expired",
            "user_joined_assessment",   "user_left_assessment",
            "gateway.circuit_open",     "gateway.circuit_closed",
            "heartbeat",                "ack"
        );
        if (!valid.contains(type))
            throw new IllegalArgumentException(
                "Invalid event_type '" + type + "'. Must be one of: " + valid);
    }

    protected void validateRole(String role) {
        if (!Set.of("CANDIDATE", "INTERVIEWER", "OBSERVER", "ADMIN").contains(role.toUpperCase()))
            throw new IllegalArgumentException("Invalid role: " + role);
    }

    protected void validateCircuitState(String state) {
        if (!Set.of("OPEN", "CLOSED", "HALF_OPEN").contains(state.toUpperCase()))
            throw new IllegalArgumentException("Invalid circuit state: " + state);
    }

    // -----------------------------------------------------------------------
    // Schema property shorthand
    // -----------------------------------------------------------------------
    protected void prop(ObjectNode props, String name, String type, String description) {
        ObjectNode p = props.putObject(name);
        p.put("type",        type);
        p.put("description", description);
    }
    protected void propEnum(ObjectNode props, String name, String description, String... values) {
        ObjectNode p = props.putObject(name);
        p.put("type",        "string");
        p.put("description", description);
        var arr = p.putArray("enum");
        for (String v : values) arr.add(v);
    }
}
