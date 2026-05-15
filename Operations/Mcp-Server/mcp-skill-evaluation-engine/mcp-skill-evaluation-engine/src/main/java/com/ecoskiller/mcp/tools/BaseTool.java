package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.UUID;

/**
 * Abstract base class providing shared functionality for all MCP tools.
 * Handles schema construction, input validation, and secure response building.
 */
public abstract class BaseTool implements McpTool {

    protected final ObjectMapper mapper = JsonUtil.createSecureMapper();

    /** Build a standard success response */
    protected ObjectNode successResponse(String message) {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("success", true);
        resp.put("message", message);
        resp.put("timestamp", Instant.now().toString());
        resp.put("request_id", UUID.randomUUID().toString());
        return resp;
    }

    /** Build a standard error response */
    protected ObjectNode errorResponse(String error) {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("success", false);
        resp.put("error", error);
        resp.put("timestamp", Instant.now().toString());
        return resp;
    }

    /** Build the MCP schema definition node */
    protected ObjectNode buildSchema(String name, String description, ObjectNode inputSchema) {
        ObjectNode schema = mapper.createObjectNode();
        schema.put("name", name);
        schema.put("description", description);
        schema.set("inputSchema", inputSchema);
        return schema;
    }

    /** Get required string parameter, throws if missing/empty */
    protected String requireString(JsonNode args, String field) {
        JsonNode node = args.path(field);
        if (node.isMissingNode() || node.isNull() || node.asText().isBlank()) {
            throw new IllegalArgumentException("Required field missing: " + field);
        }
        // Security: basic sanitization — strip control characters
        String val = node.asText().trim();
        if (val.length() > 4096) {
            throw new IllegalArgumentException("Field too long: " + field + " (max 4096 chars)");
        }
        return sanitizeString(val);
    }

    /** Get optional string with default */
    protected String optString(JsonNode args, String field, String defaultValue) {
        JsonNode node = args.path(field);
        if (node.isMissingNode() || node.isNull()) return defaultValue;
        return sanitizeString(node.asText().trim());
    }

    /** Get required integer */
    protected int requireInt(JsonNode args, String field) {
        JsonNode node = args.path(field);
        if (node.isMissingNode() || node.isNull()) {
            throw new IllegalArgumentException("Required field missing: " + field);
        }
        return node.asInt();
    }

    /** Get optional double */
    protected double optDouble(JsonNode args, String field, double defaultValue) {
        JsonNode node = args.path(field);
        if (node.isMissingNode() || node.isNull()) return defaultValue;
        return node.asDouble(defaultValue);
    }

    /** Security: strip dangerous characters from string input */
    private String sanitizeString(String input) {
        if (input == null) return null;
        // Remove null bytes and other control chars (keep tab/newline)
        return input.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]", "");
    }

    /** Validate score is in 0-100 range */
    protected void validateScore(double score, String fieldName) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException(fieldName + " must be between 0 and 100");
        }
    }

    /** Validate assessment type */
    protected void validateAssessmentType(String type) {
        java.util.Set<String> validTypes = java.util.Set.of("GD", "INTERVIEW", "CODING", "TEST");
        if (!validTypes.contains(type.toUpperCase())) {
            throw new IllegalArgumentException("Invalid assessment_type. Must be one of: " +
                    String.join(", ", validTypes));
        }
    }
}
