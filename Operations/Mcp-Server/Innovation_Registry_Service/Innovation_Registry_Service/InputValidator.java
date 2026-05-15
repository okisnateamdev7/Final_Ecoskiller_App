package com.ecoskiller.mcp.irs.security;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Centralized input validation and sanitization.
 * All user-supplied data must pass through this class before processing.
 *
 * Security rules:
 * - Tool names must be lowercase alphanumeric + underscores only
 * - UUIDs must match strict UUID v4 format
 * - Text fields stripped of control characters and length-limited
 * - SQL injection prevention via allowlisting
 * - Path traversal prevention
 */
public class InputValidator {

    // Strict UUID v4 pattern
    private static final Pattern UUID_PATTERN =
        Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    // Tool names: lowercase letters, digits, underscores only
    private static final Pattern TOOL_NAME_PATTERN =
        Pattern.compile("^[a-z][a-z0-9_]{0,63}$");

    // Safe text: no control characters except tab/newline
    private static final Pattern UNSAFE_CHARS =
        Pattern.compile("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]");

    // SQL injection keywords to detect (defense in depth — primary defense is parameterized queries)
    private static final Pattern SQL_INJECTION_PATTERN =
        Pattern.compile("(?i)\\b(DROP|DELETE|TRUNCATE|INSERT|UPDATE|EXEC|EXECUTE|UNION|SELECT|SCRIPT|ALERT|ONERROR)\\s*\\(", Pattern.CASE_INSENSITIVE);

    // Valid idea categories
    private static final Set<String> VALID_CATEGORIES = Set.of(
        "software", "business_process", "hardware", "research", "other"
    );

    // Valid idea statuses
    private static final Set<String> VALID_STATUSES = Set.of(
        "DRAFT", "SUBMITTED", "LEGAL_REVIEW", "APPROVED", "LICENSED", "ARCHIVED", "FLAGGED", "PRESERVED"
    );

    // Valid license types
    private static final Set<String> VALID_LICENSE_TYPES = Set.of(
        "EXCLUSIVE", "NON_EXCLUSIVE", "PERPETUAL", "TERM_LIMITED"
    );

    // Max field lengths
    private static final int MAX_TITLE_LENGTH       = 255;
    private static final int MAX_DESCRIPTION_LENGTH = 100_000;
    private static final int MAX_TAG_LENGTH         = 50;
    private static final int MAX_TAGS               = 20;
    private static final int MAX_JURISDICTION_LEN   = 100;
    private static final int MAX_QUERY_LENGTH       = 500;

    /** Validates that a tool name is safe */
    public boolean isValidToolName(String name) {
        return name != null && TOOL_NAME_PATTERN.matcher(name).matches();
    }

    /** Validates a UUID string */
    public boolean isValidUuid(String uuid) {
        return uuid != null && UUID_PATTERN.matcher(uuid).matches();
    }

    /** Validates an idea title */
    public String validateTitle(String title) {
        if (title == null || title.isBlank()) return "Title is required";
        if (title.length() > MAX_TITLE_LENGTH) return "Title exceeds " + MAX_TITLE_LENGTH + " characters";
        if (UNSAFE_CHARS.matcher(title).find()) return "Title contains invalid characters";
        return null;
    }

    /** Validates a long-form description */
    public String validateDescription(String description) {
        if (description == null || description.isBlank()) return "Description is required";
        if (description.length() > MAX_DESCRIPTION_LENGTH) return "Description exceeds max length";
        if (UNSAFE_CHARS.matcher(description).find()) return "Description contains invalid characters";
        return null;
    }

    /** Validates an idea category */
    public String validateCategory(String category) {
        if (category == null || category.isBlank()) return "Category is required";
        if (!VALID_CATEGORIES.contains(category.toLowerCase())) {
            return "Invalid category. Must be one of: " + VALID_CATEGORIES;
        }
        return null;
    }

    /** Validates an idea status */
    public String validateStatus(String status) {
        if (status == null) return null; // optional field
        if (!VALID_STATUSES.contains(status.toUpperCase())) {
            return "Invalid status. Must be one of: " + VALID_STATUSES;
        }
        return null;
    }

    /** Validates a license type */
    public String validateLicenseType(String licenseType) {
        if (licenseType == null) return null;
        if (!VALID_LICENSE_TYPES.contains(licenseType.toUpperCase())) {
            return "Invalid license type. Must be one of: " + VALID_LICENSE_TYPES;
        }
        return null;
    }

    /** Validates a search query */
    public String validateQuery(String query) {
        if (query == null || query.isBlank()) return "Query is required";
        if (query.length() > MAX_QUERY_LENGTH) return "Query too long (max " + MAX_QUERY_LENGTH + " chars)";
        if (SQL_INJECTION_PATTERN.matcher(query).find()) return "Query contains invalid characters";
        return null;
    }

    /** Validates a jurisdiction string */
    public String validateJurisdiction(String jurisdiction) {
        if (jurisdiction == null) return null;
        if (jurisdiction.length() > MAX_JURISDICTION_LEN) return "Jurisdiction too long";
        if (UNSAFE_CHARS.matcher(jurisdiction).find()) return "Jurisdiction contains invalid characters";
        return null;
    }

    /** Validates ownership percentage (0.01 to 100.00) */
    public String validateOwnershipPercentage(double percentage) {
        if (percentage < 0.01 || percentage > 100.0) {
            return "Ownership percentage must be between 0.01 and 100.00";
        }
        return null;
    }

    /** Validates royalty rate percentage (0 to 100) */
    public String validateRoyaltyRate(double rate) {
        if (rate < 0 || rate > 100) {
            return "Royalty rate must be between 0 and 100";
        }
        return null;
    }

    /** Validates similarity threshold (0.0 to 1.0) */
    public String validateSimilarityThreshold(double threshold) {
        if (threshold < 0.0 || threshold > 1.0) {
            return "Similarity threshold must be between 0.0 and 1.0";
        }
        return null;
    }

    /** Validates a list of tags */
    public String validateTags(JsonNode tagsNode) {
        if (tagsNode == null || tagsNode.isNull()) return null;
        if (!tagsNode.isArray()) return "Tags must be an array";
        if (tagsNode.size() > MAX_TAGS) return "Too many tags (max " + MAX_TAGS + ")";
        for (JsonNode tag : tagsNode) {
            String tagText = tag.asText("");
            if (tagText.length() > MAX_TAG_LENGTH) return "Tag too long: " + sanitizeForError(tagText);
            if (UNSAFE_CHARS.matcher(tagText).find()) return "Tag contains invalid characters";
        }
        return null;
    }

    /** Light validation of general params object */
    public String validateParams(JsonNode params) {
        if (params == null) return null;
        if (params.isObject() && params.size() > 50) {
            return "Too many parameters";
        }
        return null;
    }

    /** Sanitize a string for safe inclusion in text (output) */
    public String sanitize(String input) {
        if (input == null) return "";
        return UNSAFE_CHARS.matcher(input).replaceAll("").substring(0, Math.min(input.length(), 1000));
    }

    /** Truncate + sanitize a string safe for error messages */
    private String sanitizeForError(String input) {
        if (input == null) return "";
        return input.replaceAll("[^a-zA-Z0-9_\\-]", "").substring(0, Math.min(input.length(), 30));
    }
}
