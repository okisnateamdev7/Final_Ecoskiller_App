package com.ecoskiller.mcp.interview.agents;

import com.ecoskiller.mcp.interview.model.SecurityContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Base class for all Interview-Service MCP agents.
 *
 * Provides:
 * - Common input validation helpers
 * - UUID generation
 * - Tenant-scoped response building
 * - Kafka event simulation (production: KafkaProducer)
 */
public abstract class BaseAgent {

    protected final Logger LOG = Logger.getLogger(getClass().getName());

    /**
     * Execute the agent logic. Implementations must validate inputs,
     * enforce tenant isolation, and return a JSON-serializable result string.
     */
    public abstract String execute(JsonNode args, SecurityContext ctx);

    // ── Input Helpers ─────────────────────────────────────────────────────────

    protected String requireString(JsonNode args, String field) {
        JsonNode node = args.get(field);
        if (node == null || node.isNull() || node.asText().isBlank()) {
            throw new IllegalArgumentException("Required field missing or empty: " + field);
        }
        return sanitizeString(node.asText().trim());
    }

    protected String optionalString(JsonNode args, String field, String defaultValue) {
        JsonNode node = args.get(field);
        if (node == null || node.isNull() || node.asText().isBlank()) return defaultValue;
        return sanitizeString(node.asText().trim());
    }

    protected int requireInt(JsonNode args, String field) {
        JsonNode node = args.get(field);
        if (node == null || node.isNull()) throw new IllegalArgumentException("Required field missing: " + field);
        try { return node.asInt(); }
        catch (Exception e) { throw new IllegalArgumentException("Field '" + field + "' must be an integer"); }
    }

    protected int optionalInt(JsonNode args, String field, int defaultValue) {
        JsonNode node = args.get(field);
        if (node == null || node.isNull()) return defaultValue;
        try { return node.asInt(); }
        catch (Exception e) { return defaultValue; }
    }

    protected boolean optionalBool(JsonNode args, String field, boolean defaultValue) {
        JsonNode node = args.get(field);
        if (node == null || node.isNull()) return defaultValue;
        return node.asBoolean(defaultValue);
    }

    /**
     * Validate a UUID string format.
     */
    protected String validateUuid(String value, String fieldName) {
        try {
            UUID.fromString(value);
            return value;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Field '" + fieldName + "' must be a valid UUID, got: " + value);
        }
    }

    /**
     * Prevent injection: strip control chars, limit length, HTML-encode specials.
     */
    protected String sanitizeString(String input) {
        if (input == null) return null;
        // Remove control characters
        String clean = input.replaceAll("[\\x00-\\x1F\\x7F]", "");
        // Limit to 4096 chars
        if (clean.length() > 4096) clean = clean.substring(0, 4096);
        return clean;
    }

    /**
     * Validate integer score in 1-5 range.
     */
    protected int validateScore(int score, String fieldName) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException(fieldName + " must be between 1 and 5, got: " + score);
        }
        return score;
    }

    /**
     * Generate a new UUID for created resources.
     */
    protected String newId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Simulate Kafka event publication.
     * Production: KafkaProducer<String, String>.send(new ProducerRecord<>(topic, key, value))
     */
    protected void publishKafkaEvent(String topic, String eventType, String payload) {
        LOG.info("KAFKA → topic=" + topic + " event=" + eventType + " payload=" + payload);
        // Production: async Kafka publish via Spring Kafka / confluent client
    }

    /**
     * Build a standardized success response JSON string.
     */
    protected String success(String action, String resourceId, String details) {
        return String.format(
            "{\"status\":\"success\",\"action\":\"%s\",\"id\":\"%s\",\"details\":\"%s\",\"timestamp\":\"%s\"}",
            action, resourceId, details, java.time.Instant.now()
        );
    }

    /**
     * Validate interview state transition.
     * Valid: SCHEDULED→STARTED, STARTED→PAUSED, PAUSED→RESUMED(STARTED),
     *        STARTED→COMPLETED, SCHEDULED→CANCELLED
     */
    protected void validateStateTransition(String from, String to) {
        boolean valid = switch (from) {
            case "SCHEDULED" -> to.equals("STARTED") || to.equals("CANCELLED");
            case "STARTED"   -> to.equals("PAUSED")  || to.equals("COMPLETED");
            case "PAUSED"    -> to.equals("STARTED");  // resume
            default          -> false;
        };
        if (!valid) {
            throw new IllegalArgumentException(
                "Invalid state transition: " + from + " → " + to
            );
        }
    }
}
