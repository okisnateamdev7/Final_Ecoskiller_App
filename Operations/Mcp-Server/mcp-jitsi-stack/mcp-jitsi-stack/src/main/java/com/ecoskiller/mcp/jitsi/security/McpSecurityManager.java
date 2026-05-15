package com.ecoskiller.mcp.jitsi.security;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * McpSecurityManager — centralised security enforcement for the Jitsi MCP server.
 *
 * Defences:
 *   1. Raw input size cap (prevents memory exhaustion / DoS)
 *   2. Tool-name allow-list (prevents arbitrary dispatch)
 *   3. Argument depth / size limits (prevents JSON-bomb attacks)
 *   4. String field length caps (prevents buffer overflow in downstream)
 *   5. Injection-pattern blocking (SQL / XMPP / shell injection via room names)
 *   6. Error message sanitisation (no stack traces / internal paths to caller)
 */
public class McpSecurityManager {

    // ── Limits ────────────────────────────────────────────────────────────────
    private static final int MAX_RAW_INPUT_BYTES    = 64_000;   // 64 KB per request
    private static final int MAX_ARGUMENT_DEPTH     = 5;
    private static final int MAX_STRING_FIELD_LEN   = 2_048;
    private static final int MAX_ARRAY_ELEMENTS     = 100;

    // ── Allowed tool names (exact match) ─────────────────────────────────────
    private static final Set<String> ALLOWED_TOOLS = Set.of(
        "jvb_stream_routing",
        "jvb_quality_metrics",
        "jicofo_room_lifecycle",
        "jicofo_participant_routing",
        "prosody_jwt_validation",
        "prosody_xmpp_signalling",
        "jitsi_web_session",
        "coturn_nat_traversal",
        "gd_session_orchestrator",
        "media_security_audit",
        "freeswitch_pstn_bridge",
        "jitsi_health_monitor"
    );

    // ── Injection patterns ────────────────────────────────────────────────────
    // Covers: SQL injection, XMPP stanza injection, shell meta-chars, path traversal
    private static final Pattern INJECTION_PATTERN = Pattern.compile(
        "(?i)(--|;|\\bDROP\\b|\\bSELECT\\b|\\bINSERT\\b|\\bDELETE\\b|" +
        "<message|<presence|<iq|</|\\$\\(|`|\\.\\.[\\\\/]|" +
        "[;&|><`$\\\\])"
    );

    // Session ID / room name: only alphanumeric + underscore + hyphen
    private static final Pattern SAFE_ID_PATTERN =
        Pattern.compile("^[a-zA-Z0-9_\\-]{1,128}$");

    // JWT: header.payload.signature — base64url segments only
    private static final Pattern JWT_SHAPE_PATTERN =
        Pattern.compile("^[A-Za-z0-9_\\-]+\\.[A-Za-z0-9_\\-]+\\.[A-Za-z0-9_\\-]+$");

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Validate raw JSON string before any parsing.
     * Rejects oversized payloads that could cause memory exhaustion.
     */
    public void validateRawInput(String raw) {
        if (raw == null) throw new SecurityException("Null input");
        if (raw.length() > MAX_RAW_INPUT_BYTES) {
            throw new SecurityException("Request exceeds maximum allowed size");
        }
    }

    /**
     * Tool name must be in the allow-list — no reflection-based or unknown dispatch.
     */
    public void validateToolName(String toolName) {
        if (toolName == null || toolName.isBlank()) {
            throw new SecurityException("Tool name is required");
        }
        if (!ALLOWED_TOOLS.contains(toolName)) {
            throw new SecurityException("Unknown tool: access denied");
        }
    }

    /**
     * Recursively validate argument tree: depth, size, string length, injection patterns.
     */
    public void validateArguments(JsonNode node) {
        validateNode(node, 0);
    }

    /**
     * Validate a session_id / room_name field specifically.
     */
    public void validateSessionId(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new SecurityException("session_id/room_name is required");
        }
        if (!SAFE_ID_PATTERN.matcher(sessionId).matches()) {
            throw new SecurityException("session_id contains invalid characters");
        }
    }

    /**
     * Validate JWT token shape (does NOT verify signature — that is Prosody's job).
     * Only ensures it looks like a valid 3-part JWT before passing downstream.
     */
    public void validateJwtShape(String token) {
        if (token == null || token.isBlank()) {
            throw new SecurityException("JWT token is required");
        }
        if (token.length() > 4096) {
            throw new SecurityException("JWT token exceeds maximum length");
        }
        if (!JWT_SHAPE_PATTERN.matcher(token).matches()) {
            throw new SecurityException("JWT token has invalid format");
        }
    }

    /**
     * Sanitize error messages before sending to caller.
     * Strips stack traces, file paths, and internal detail.
     */
    public String sanitizeErrorMessage(String message) {
        if (message == null) return "Internal error";
        // Remove anything that looks like a Java stack trace line or file path
        String sanitized = message
            .replaceAll("at [a-zA-Z0-9.$_]+\\([^)]*\\)", "")
            .replaceAll("/[a-zA-Z0-9/_\\-.]+\\.java:\\d+", "")
            .replaceAll("Exception in thread \"[^\"]+\"", "")
            .trim();
        return sanitized.isEmpty() ? "Internal error" : sanitized;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void validateNode(JsonNode node, int depth) {
        if (node == null) return;
        if (depth > MAX_ARGUMENT_DEPTH) {
            throw new SecurityException("Argument nesting depth exceeds limit");
        }

        if (node.isTextual()) {
            String val = node.asText();
            if (val.length() > MAX_STRING_FIELD_LEN) {
                throw new SecurityException("String field exceeds maximum length");
            }
            if (INJECTION_PATTERN.matcher(val).find()) {
                throw new SecurityException("Potential injection detected in argument");
            }
        } else if (node.isArray()) {
            if (node.size() > MAX_ARRAY_ELEMENTS) {
                throw new SecurityException("Array exceeds maximum element count");
            }
            node.forEach(child -> validateNode(child, depth + 1));
        } else if (node.isObject()) {
            node.fields().forEachRemaining(entry -> {
                // Validate key
                if (entry.getKey().length() > 128) {
                    throw new SecurityException("Object key exceeds maximum length");
                }
                validateNode(entry.getValue(), depth + 1);
            });
        }
    }
}
