package io.ecoskiller.recruiter.security;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Input validation for all Recruiter MCP agent inputs.
 *
 * Guards against:
 * - SQL injection via recruiter/tenant IDs
 * - Email header injection
 * - URL injection in webhook endpoints
 * - Null bytes and log injection
 * - Path traversal
 * - Oversized inputs (DoS)
 */
public final class InputSanitizer {

    // Strict allowlists
    private static final Pattern UUID_PATTERN =
        Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private static final Pattern ALPHANUMERIC_ID_PATTERN =
        Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9\\-_]{0,63}$");

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[a-zA-Z0-9._%+\\-]{1,64}@[a-zA-Z0-9.\\-]{1,253}\\.[a-zA-Z]{2,10}$");

    private static final Pattern HTTPS_URL_PATTERN =
        Pattern.compile("^https://[a-zA-Z0-9.\\-_]{1,253}(:[0-9]{1,5})?(/[a-zA-Z0-9.\\-_/~%+?=&]*)?$");

    private static final Pattern SAFE_TAG_PATTERN =
        Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9\\-_ ]{0,49}$");

    private static final Pattern TIER_PATTERN =
        Pattern.compile("^(starter|professional|enterprise)$", Pattern.CASE_INSENSITIVE);

    private static final Pattern ROLE_PATTERN =
        Pattern.compile("^(RECRUITER|ADMIN|HIRING_MANAGER|INTERVIEWER)$");

    private InputSanitizer() {}

    // ── ID Validators ─────────────────────────────────────────────────────────

    public static void validateRecruiterId(String id) {
        if (id == null || (!UUID_PATTERN.matcher(id).matches() && !ALPHANUMERIC_ID_PATTERN.matcher(id).matches())) {
            throw new SecurityException("Invalid recruiter_id format: " + sanitizeForLog(id));
        }
    }

    public static void validateCandidateId(String id) {
        if (id == null || (!UUID_PATTERN.matcher(id).matches() && !ALPHANUMERIC_ID_PATTERN.matcher(id).matches())) {
            throw new SecurityException("Invalid candidate_id format: " + sanitizeForLog(id));
        }
    }

    public static void validateJobId(String id) {
        if (id == null || (!UUID_PATTERN.matcher(id).matches() && !ALPHANUMERIC_ID_PATTERN.matcher(id).matches())) {
            throw new SecurityException("Invalid job_id format: " + sanitizeForLog(id));
        }
    }

    public static void validateTenantId(String id) {
        if (id == null || !ALPHANUMERIC_ID_PATTERN.matcher(id).matches()) {
            throw new SecurityException("Invalid tenant_id format: " + sanitizeForLog(id));
        }
    }

    public static void validateWebhookId(String id) {
        if (id == null || (!UUID_PATTERN.matcher(id).matches() && !ALPHANUMERIC_ID_PATTERN.matcher(id).matches())) {
            throw new SecurityException("Invalid webhook_id format: " + sanitizeForLog(id));
        }
    }

    // ── Field Validators ──────────────────────────────────────────────────────

    public static void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new SecurityException("Invalid email format: " + sanitizeForLog(email));
        }
    }

    /**
     * Webhook URLs must be HTTPS only (no HTTP, no internal IPs, no file://).
     * Prevents SSRF attacks via recruiter-controlled webhook endpoints.
     */
    public static void validateWebhookUrl(String url) {
        if (url == null || !HTTPS_URL_PATTERN.matcher(url).matches()) {
            throw new SecurityException("Webhook URL must be HTTPS and well-formed: " + sanitizeForLog(url));
        }
        // Block internal/private IP ranges (SSRF protection)
        String lower = url.toLowerCase();
        if (lower.contains("localhost") || lower.contains("127.0.0.1") ||
            lower.contains("10.") || lower.contains("172.16.") ||
            lower.contains("192.168.") || lower.contains("169.254.") ||
            lower.contains("metadata.google") || lower.contains("0.0.0.0")) {
            throw new SecurityException("Webhook URL must not target internal/private addresses (SSRF prevention)");
        }
    }

    public static void validateSubscriptionTier(String tier) {
        if (tier == null || !TIER_PATTERN.matcher(tier).matches()) {
            throw new SecurityException("Invalid subscription tier '" + sanitizeForLog(tier)
                + "'. Allowed: starter, professional, enterprise");
        }
    }

    public static void validateRole(String role) {
        if (role == null || !ROLE_PATTERN.matcher(role).matches()) {
            throw new SecurityException("Invalid role '" + sanitizeForLog(role)
                + "'. Allowed: RECRUITER, ADMIN, HIRING_MANAGER, INTERVIEWER");
        }
    }

    public static void validateTag(String tag) {
        if (tag != null && !tag.isBlank() && !SAFE_TAG_PATTERN.matcher(tag).matches()) {
            throw new SecurityException("Invalid tag '" + sanitizeForLog(tag)
                + "'. Use alphanumeric, hyphens, underscores, spaces only (max 50 chars)");
        }
    }

    public static void validateEnum(String value, String field, String... allowed) {
        for (String a : allowed) { if (a.equalsIgnoreCase(value)) return; }
        throw new IllegalArgumentException("Invalid value '" + sanitizeForLog(value)
            + "' for '" + field + "'. Allowed: " + Arrays.toString(allowed));
    }

    public static void requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Required field '" + field + "' is missing or blank");
        }
    }

    public static void validateRange(int value, int min, int max, String field) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(field + " must be between " + min + " and " + max + ", got: " + value);
        }
    }

    public static boolean isValidToolName(String name) {
        return name != null && !name.isEmpty() && name.length() <= 100 && name.matches("^[a-z_]+$");
    }

    /**
     * Sanitize free-form text (notes, reasons) — HTML/SQL escape and truncate.
     */
    public static String sanitizeText(String text, int maxLen) {
        if (text == null) return "";
        if (text.length() > maxLen) text = text.substring(0, maxLen);
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
            .replaceAll("[\\x00-\\x08\\x0b\\x0c\\x0e-\\x1f]", ""); // strip control chars
    }

    /** Safe truncation for log messages — prevents log injection. */
    public static String sanitizeForLog(String v) {
        if (v == null) return "<null>";
        return v.replaceAll("[\\r\\n\\t]", " ").substring(0, Math.min(v.length(), 80));
    }
}
