package com.ecoskiller.mcp.rbac.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Security layer: sanitizes all incoming MCP tool arguments.
 *
 * Protections:
 *  - Strips null bytes and control characters
 *  - Limits string length to prevent DoS
 *  - Blocks JSON injection attempts
 *  - Logs suspicious patterns
 */
public class InputSanitizer {

    private static final Logger LOG = Logger.getLogger(InputSanitizer.class.getName());

    private static final int    MAX_STRING_LENGTH = 65_536; // 64KB per field
    private static final int    MAX_ARRAY_SIZE    = 500;
    private static final int    MAX_DEPTH         = 10;

    // Patterns that should never appear in RBAC field values
    private static final Set<String> SUSPICIOUS_PATTERNS = Set.of(
        "${", "#{", "<%", "%>", "<script", "</script",
        "\u0000", "../", "..\\", "cmd.exe", "/bin/sh", "/bin/bash"
    );

    private final ObjectMapper mapper = new ObjectMapper();

    /** Recursively sanitize a JSON node. Returns a clean copy. */
    public JsonNode sanitizeNode(JsonNode node) {
        return sanitizeNode(node, 0);
    }

    private JsonNode sanitizeNode(JsonNode node, int depth) {
        if (depth > MAX_DEPTH) {
            LOG.warning("[InputSanitizer] Max depth exceeded — truncating nested structure");
            return mapper.createObjectNode();
        }

        if (node == null || node.isNull()) return node;

        if (node.isTextual()) {
            return new TextNode(sanitizeString(node.asText()));
        }

        if (node.isObject()) {
            ObjectNode clean = mapper.createObjectNode();
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String key = sanitizeString(entry.getKey());
                JsonNode val = sanitizeNode(entry.getValue(), depth + 1);
                clean.set(key, val);
            }
            return clean;
        }

        if (node.isArray()) {
            ArrayNode clean = mapper.createArrayNode();
            int count = 0;
            for (JsonNode item : node) {
                if (count++ >= MAX_ARRAY_SIZE) {
                    LOG.warning("[InputSanitizer] Array truncated at " + MAX_ARRAY_SIZE + " elements");
                    break;
                }
                clean.add(sanitizeNode(item, depth + 1));
            }
            return clean;
        }

        // numbers, booleans — pass through as-is
        return node;
    }

    private String sanitizeString(String input) {
        if (input == null) return "";

        // Length guard
        if (input.length() > MAX_STRING_LENGTH) {
            LOG.warning("[InputSanitizer] String truncated from " + input.length() + " chars");
            input = input.substring(0, MAX_STRING_LENGTH);
        }

        // Strip null bytes and non-printable control characters (except tab, newline, CR)
        input = input.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]", "");

        // Check for suspicious injection patterns
        String lower = input.toLowerCase();
        for (String pattern : SUSPICIOUS_PATTERNS) {
            if (lower.contains(pattern.toLowerCase())) {
                LOG.warning("[InputSanitizer] SUSPICIOUS PATTERN detected: '" + pattern +
                            "' in input (first 100 chars): " + input.substring(0, Math.min(100, input.length())));
                // Don't throw — log and strip the pattern
                input = input.replace(pattern, "");
            }
        }

        return input;
    }
}
