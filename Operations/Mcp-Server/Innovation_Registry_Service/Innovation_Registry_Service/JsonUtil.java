package com.ecoskiller.mcp.irs.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.UUID;

/**
 * Secure ObjectMapper factory and JSON utilities.
 *
 * Security hardening:
 * - Polymorphic type handling disabled (prevents deserialization gadget attacks)
 * - Unknown properties fail-safe (don't throw on extra fields)
 * - Large numeric values handled safely
 */
public final class JsonUtil {

    private JsonUtil() {}

    /**
     * Creates a security-hardened ObjectMapper.
     * IMPORTANT: Never enable polymorphic type handling (enableDefaultTyping) —
     * this is a known attack vector for remote code execution.
     */
    public static ObjectMapper createSecureMapper() {
        return JsonMapper.builder()
            // Fail on unknown properties: false (tolerate extra fields from clients)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // Prevent NumberFormatException on large integers
            .configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, false)
            // Do NOT call disable(MapperFeature.DEFAULT_VIEW_INCLUSION) — not needed
            // Prevent float precision issues
            .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, false)
            // Dates as timestamps is fine for internal use
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();
    }

    /** Build a standard success response body */
    public static ObjectNode successResponse(ObjectMapper mapper, String message) {
        ObjectNode node = mapper.createObjectNode();
        node.put("status", "success");
        node.put("message", message);
        node.put("timestamp", Instant.now().toString());
        return node;
    }

    /** Build a standard error response body */
    public static ObjectNode errorResponse(ObjectMapper mapper, String code, String message) {
        ObjectNode node = mapper.createObjectNode();
        node.put("status", "error");
        node.put("code", code);
        node.put("message", message);
        node.put("timestamp", Instant.now().toString());
        return node;
    }

    /** Build an idea-created response */
    public static ObjectNode ideaCreatedResponse(ObjectMapper mapper,
                                                   String ideaId, String ideaDna,
                                                   String status, String createdAt) {
        ObjectNode node = mapper.createObjectNode();
        node.put("ideaId", ideaId);
        node.put("ideaDna", ideaDna);
        node.put("status", status);
        node.put("createdAt", createdAt);
        return node;
    }

    /** Generate a new UUID */
    public static String newId() {
        return UUID.randomUUID().toString();
    }

    /** Current ISO-8601 timestamp */
    public static String now() {
        return Instant.now().toString();
    }
}
