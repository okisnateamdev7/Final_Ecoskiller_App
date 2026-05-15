package com.ecoskiller.billing.security;

import com.ecoskiller.billing.server.ValidationException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Security-first input validation layer.
 *
 * Every tool argument passes through this validator before any business logic runs.
 * Rules:
 *  - UUIDs are strict RFC-4122 format
 *  - Monetary amounts must be positive, non-zero, max 2 decimal places
 *  - String fields are length-bounded and stripped of dangerous characters
 *  - Enum fields are validated against an allow-list (no open-ended injection)
 *  - Tenant ID is never accepted from caller — it must come from JWT (mocked here
 *    as a required header; in production it is extracted by the API gateway)
 */
public final class RequestValidator {

    // ─────────────────────────────────────────────
    //  Constants
    // ─────────────────────────────────────────────

    private static final Pattern UUID_PATTERN =
        Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
                        Pattern.CASE_INSENSITIVE);

    private static final Pattern AMOUNT_PATTERN =
        Pattern.compile("^\\d{1,12}(\\.\\d{1,2})?$");

    private static final Pattern SAFE_STRING_PATTERN =
        Pattern.compile("^[\\w\\s\\-.,@+:/()#]*$");

    private static final int MAX_STRING_LENGTH   = 500;
    private static final int MAX_REASON_LENGTH   = 1000;
    private static final int MAX_NOTES_LENGTH    = 2000;

    private static final Set<String> VALID_PLAN_IDS = Set.of(
        "free", "professional", "enterprise"
    );

    private static final Set<String> VALID_CURRENCIES = Set.of(
        "INR", "USD", "EUR"
    );

    private static final Set<String> VALID_PAYMENT_STATUSES = Set.of(
        "pending", "processing", "completed", "failed", "refunded", "cancelled"
    );

    private static final Set<String> VALID_INVOICE_STATUSES = Set.of(
        "draft", "issued", "paid", "overdue", "cancelled"
    );

    private static final Set<String> VALID_SUBSCRIPTION_STATUSES = Set.of(
        "active", "cancelled", "suspended", "trial", "expired"
    );

    // ─────────────────────────────────────────────
    //  Public API
    // ─────────────────────────────────────────────

    /** Validate and return the string field, or throw ValidationException. */
    public String requireString(JsonNode args, String field) {
        String val = getString(args, field);
        if (val == null || val.isBlank()) {
            throw new ValidationException("Required field '" + field + "' is missing or empty");
        }
        return val;
    }

    public String requireUuid(JsonNode args, String field) {
        String val = requireString(args, field);
        return validateUuid(val, field);
    }

    public String optionalUuid(JsonNode args, String field) {
        String val = getString(args, field);
        if (val == null || val.isBlank()) return null;
        return validateUuid(val, field);
    }

    public String requirePlanId(JsonNode args, String field) {
        String val = requireString(args, field).toLowerCase();
        if (!VALID_PLAN_IDS.contains(val)) {
            throw new ValidationException("Invalid plan '" + val + "'. Allowed: " + VALID_PLAN_IDS);
        }
        return val;
    }

    public String requireCurrency(JsonNode args, String field) {
        String val = requireString(args, field).toUpperCase();
        if (!VALID_CURRENCIES.contains(val)) {
            throw new ValidationException("Invalid currency '" + val + "'. Allowed: " + VALID_CURRENCIES);
        }
        return val;
    }

    public String requirePaymentStatus(JsonNode args, String field) {
        String val = requireString(args, field).toLowerCase();
        if (!VALID_PAYMENT_STATUSES.contains(val)) {
            throw new ValidationException("Invalid payment status '" + val + "'");
        }
        return val;
    }

    public String requireInvoiceStatus(JsonNode args, String field) {
        String val = requireString(args, field).toLowerCase();
        if (!VALID_INVOICE_STATUSES.contains(val)) {
            throw new ValidationException("Invalid invoice status '" + val + "'");
        }
        return val;
    }

    public String requireSubscriptionStatus(JsonNode args, String field) {
        String val = requireString(args, field).toLowerCase();
        if (!VALID_SUBSCRIPTION_STATUSES.contains(val)) {
            throw new ValidationException("Invalid subscription status '" + val + "'");
        }
        return val;
    }

    public String requireAmount(JsonNode args, String field) {
        String val = requireString(args, field);
        if (!AMOUNT_PATTERN.matcher(val).matches()) {
            throw new ValidationException(
                "Field '" + field + "' must be a positive number with up to 2 decimal places");
        }
        double amount = Double.parseDouble(val);
        if (amount <= 0) {
            throw new ValidationException("Field '" + field + "' must be greater than 0");
        }
        if (amount > 999_999_999.99) {
            throw new ValidationException("Field '" + field + "' exceeds maximum allowed amount");
        }
        return val;
    }

    public String optionalSafeString(JsonNode args, String field, int maxLength) {
        String val = getString(args, field);
        if (val == null || val.isBlank()) return null;
        return validateSafeString(val, field, maxLength);
    }

    public int optionalPositiveInt(JsonNode args, String field, int defaultValue) {
        if (!args.has(field)) return defaultValue;
        int val = args.get(field).asInt(-1);
        if (val < 0) throw new ValidationException("Field '" + field + "' must be a non-negative integer");
        if (val > 10_000) throw new ValidationException("Field '" + field + "' exceeds maximum allowed value");
        return val;
    }

    public boolean optionalBoolean(JsonNode args, String field, boolean defaultValue) {
        if (!args.has(field)) return defaultValue;
        return args.get(field).asBoolean(defaultValue);
    }

    /** Generate a new UUID (audit IDs, idempotency keys, etc.) */
    public String generateId() {
        return UUID.randomUUID().toString();
    }

    // ─────────────────────────────────────────────
    //  Private helpers
    // ─────────────────────────────────────────────

    private String getString(JsonNode args, String field) {
        if (args == null || !args.has(field)) return null;
        JsonNode node = args.get(field);
        if (node.isNull()) return null;
        return node.asText();
    }

    private String validateUuid(String val, String field) {
        String trimmed = val.trim().toLowerCase();
        if (!UUID_PATTERN.matcher(trimmed).matches()) {
            throw new ValidationException("Field '" + field + "' must be a valid UUID (RFC-4122)");
        }
        return trimmed;
    }

    private String validateSafeString(String val, String field, int maxLen) {
        if (val.length() > maxLen) {
            throw new ValidationException(
                "Field '" + field + "' exceeds max length of " + maxLen + " characters");
        }
        if (!SAFE_STRING_PATTERN.matcher(val).matches()) {
            throw new ValidationException(
                "Field '" + field + "' contains disallowed characters");
        }
        return val.trim();
    }
}
