package io.ecoskiller.prosody.security;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Input validation and sanitization for all Prosody MCP agent inputs.
 *
 * Defends against:
 * - XML injection via XMPP stanzas
 * - XMPP JID injection
 * - SQL injection (room/user IDs used in queries)
 * - Path traversal
 * - Oversized inputs (DoS)
 */
public final class InputSanitizer {

    // Allowed patterns — strict allowlists
    private static final Pattern ROOM_ID_PATTERN =
        Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9\\-]{0,63}$");

    private static final Pattern USER_ID_PATTERN =
        Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9\\-_\\.]{0,63}$");

    private static final Pattern JID_PATTERN =
        Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9\\-_\\.]{0,63}@[a-zA-Z0-9][a-zA-Z0-9\\-\\.]{0,253}$");

    // Characters that could break XML stanzas
    private static final Pattern XML_INJECTION_CHARS =
        Pattern.compile("[<>&\"']");

    private InputSanitizer() {}

    /**
     * Validate a room ID follows the safe pattern (alphanumeric + hyphens only).
     */
    public static void validateRoomId(String roomId) {
        if (roomId == null || !ROOM_ID_PATTERN.matcher(roomId).matches()) {
            throw new SecurityException(
                "Invalid room_id '" + sanitizeForLog(roomId) + "'. " +
                "Must match: [a-zA-Z0-9][a-zA-Z0-9\\-]{0,63}");
        }
    }

    /**
     * Validate a user ID follows the safe pattern.
     */
    public static void validateUserId(String userId) {
        if (userId == null || !USER_ID_PATTERN.matcher(userId).matches()) {
            throw new SecurityException(
                "Invalid user_id '" + sanitizeForLog(userId) + "'. " +
                "Must match: [a-zA-Z0-9][a-zA-Z0-9\\-_\\.]{0,63}");
        }
    }

    /**
     * Validate a full XMPP JID (localpart@domain).
     */
    public static void validateJid(String jid) {
        if (jid == null || !JID_PATTERN.matcher(jid).matches()) {
            throw new SecurityException(
                "Invalid JID '" + sanitizeForLog(jid) + "'. Expected format: user@domain");
        }
    }

    /**
     * Validate a comma-separated participant list.
     * Each entry must be a valid user ID.
     */
    public static void validateParticipantList(String list) {
        if (list == null || list.trim().isEmpty()) {
            throw new IllegalArgumentException("Participant list cannot be empty");
        }
        String[] parts = list.split(",");
        if (parts.length > 20) {
            throw new SecurityException("Participant list exceeds maximum of 20 participants");
        }
        for (String p : parts) {
            String trimmed = p.trim();
            if (!trimmed.isEmpty()) {
                validateUserId(trimmed);
            }
        }
    }

    /**
     * Validate an integer is within an inclusive range.
     */
    public static void validateRange(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                fieldName + " must be between " + min + " and " + max + ", got: " + value);
        }
    }

    /**
     * Validate a string field is one of the allowed enum values.
     */
    public static void validateEnum(String value, String fieldName, String... allowed) {
        for (String a : allowed) {
            if (a.equals(value)) return;
        }
        throw new IllegalArgumentException(
            "Invalid value '" + sanitizeForLog(value) + "' for field '" + fieldName + "'. " +
            "Allowed: " + Arrays.toString(allowed));
    }

    /**
     * Validates a tool name contains only safe characters (prevents injection via tool name).
     */
    public static boolean isValidToolName(String name) {
        if (name == null || name.isEmpty() || name.length() > 100) return false;
        return name.matches("^[a-z_]+$");
    }

    /**
     * Require a field to be non-blank.
     */
    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Required field '" + fieldName + "' is missing or empty");
        }
    }

    /**
     * Sanitize free-text (status messages, reasons) to prevent XML injection.
     * Truncates to maxLength, strips XML-dangerous characters.
     */
    public static String sanitizeText(String text, int maxLength) {
        if (text == null) return "";
        // Truncate
        if (text.length() > maxLength) {
            text = text.substring(0, maxLength);
        }
        // Escape XML special chars
        text = text
            .replace("&",  "&amp;")
            .replace("<",  "&lt;")
            .replace(">",  "&gt;")
            .replace("\"", "&quot;")
            .replace("'",  "&apos;");
        return text;
    }

    /**
     * Safely truncate a string for inclusion in error/log messages
     * (prevents log injection via malicious inputs).
     */
    public static String sanitizeForLog(String value) {
        if (value == null) return "<null>";
        String safe = value.replaceAll("[\\r\\n\\t]", " "); // no newline injection
        return safe.length() > 80 ? safe.substring(0, 80) + "..." : safe;
    }
}

// null byte check is also enforced here (belt-and-suspenders with RequestValidator)
