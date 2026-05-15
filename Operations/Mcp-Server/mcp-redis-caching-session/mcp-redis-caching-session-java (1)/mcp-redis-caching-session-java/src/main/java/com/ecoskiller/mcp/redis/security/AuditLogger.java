package com.ecoskiller.mcp.redis.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

/**
 * Structured audit log for every MCP tool invocation.
 *
 * Security rules:
 *  - api_key is ALWAYS masked before logging (never appears in logs)
 *  - passwords / tokens in arguments are masked
 *  - Output is structured JSON → easy ingest by Wazuh SIEM
 */
public class AuditLogger {

    private static final Logger audit = LoggerFactory.getLogger("AUDIT");
    private final ObjectMapper  json  = new ObjectMapper();

    /** Log an incoming tool call (api_key masked). */
    public void logCall(String toolName, JsonNode arguments) {
        try {
            ObjectNode sanitised = (ObjectNode) json.readTree(arguments.toString());
            // Always mask the api_key — it must NEVER appear in logs
            if (sanitised.has("api_key")) {
                sanitised.put("api_key", "***MASKED***");
            }
            // Mask any other sensitive fields
            maskIfPresent(sanitised, "password");
            maskIfPresent(sanitised, "token");
            maskIfPresent(sanitised, "secret");

            audit.info("{\"event\":\"TOOL_CALL\",\"tool\":\"{}\",\"ts\":\"{}\",\"args\":{}}", 
                       toolName, Instant.now(), sanitised);
        } catch (Exception e) {
            audit.info("{\"event\":\"TOOL_CALL\",\"tool\":\"{}\",\"ts\":\"{}\",\"args\":\"[parse_error]\"}", 
                       toolName, Instant.now());
        }
    }

    /** Log the result of a tool call. */
    public void logResult(String toolName, boolean isError) {
        audit.info("{\"event\":\"TOOL_RESULT\",\"tool\":\"{}\",\"ts\":\"{}\",\"error\":{}}", 
                   toolName, Instant.now(), isError);
    }

    /** Log an unauthorised access attempt. */
    public void logUnauthorised(String toolName, String apiKey) {
        // Log only a safe prefix of the attempted key for debugging
        String safeKey = (apiKey != null && apiKey.length() > 4) 
                         ? apiKey.substring(0, 4) + "***" : "***";
        audit.warn("{\"event\":\"UNAUTHORISED\",\"tool\":\"{}\",\"ts\":\"{}\",\"key_prefix\":\"{}\"}", 
                   toolName, Instant.now(), safeKey);
    }

    /** Log a tool execution error. */
    public void logError(String toolName, String message) {
        audit.error("{\"event\":\"TOOL_ERROR\",\"tool\":\"{}\",\"ts\":\"{}\",\"message\":\"{}\"}", 
                    toolName, Instant.now(), sanitiseMessage(message));
    }

    private void maskIfPresent(ObjectNode node, String field) {
        if (node.has(field)) node.put(field, "***MASKED***");
    }

    /** Remove newlines from messages to prevent log injection. */
    private String sanitiseMessage(String msg) {
        if (msg == null) return "null";
        return msg.replace("\n", " ").replace("\r", " ").replace("\t", " ");
    }
}
