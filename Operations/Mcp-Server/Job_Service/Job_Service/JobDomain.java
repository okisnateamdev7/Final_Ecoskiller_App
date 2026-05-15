package com.ecoskiller.mcp.model;

import java.util.*;

/**
 * Core domain types for Ecoskiller Job Service.
 *
 * Domain isolation: Arts | Commerce | Science | Technology | Administration
 * Maps directly to Kafka topic partitioning: jobs.<domain>
 */
public final class JobDomain {

    private JobDomain() {}

    // ── Valid domains ─────────────────────────────────────────────────────
    public static final Set<String> VALID = Set.of(
        "Arts","Commerce","Science","Technology","Administration"
    );

    // ── Job status state machine ──────────────────────────────────────────
    public enum Status {
        DRAFT, MODERATION_REQUIRED, PUBLISHED, CLOSED, ARCHIVED, REJECTED;

        public static boolean canTransition(Status from, Status to) {
            return switch (from) {
                case DRAFT               -> to == PUBLISHED || to == MODERATION_REQUIRED;
                case MODERATION_REQUIRED -> to == PUBLISHED || to == REJECTED;
                case PUBLISHED           -> to == CLOSED;
                case CLOSED              -> to == ARCHIVED;
                case ARCHIVED, REJECTED  -> false; // terminal
            };
        }
    }

    // ── Salary bands ──────────────────────────────────────────────────────
    public record SalaryBand(String domain, long min, long max, String currency) {
        public static final Map<String, SalaryBand> DEFAULTS = Map.of(
            "Arts",           new SalaryBand("Arts",           15_000,    250_000, "INR"),
            "Commerce",       new SalaryBand("Commerce",       20_000,    500_000, "INR"),
            "Science",        new SalaryBand("Science",        25_000,    600_000, "INR"),
            "Technology",     new SalaryBand("Technology",     30_000,  1_500_000, "INR"),
            "Administration", new SalaryBand("Administration", 20_000,    400_000, "INR")
        );
        public boolean isWithin(long v) { return v >= min && v <= max; }
    }

    // ── Visibility ────────────────────────────────────────────────────────
    public enum Visibility { PUBLIC, TENANT_ONLY, RECRUITER }

    // ── Validators ────────────────────────────────────────────────────────
    public static void requireDomain(String d) {
        if (!VALID.contains(d))
            throw new IllegalArgumentException("Invalid domain '" + d + "'. Must be one of: " + VALID);
    }

    public static void requireNonBlank(String v, String field) {
        if (v == null || v.isBlank())
            throw new IllegalArgumentException("Field '" + field + "' is required");
    }

    public static void requirePositive(long v, String field) {
        if (v <= 0)
            throw new IllegalArgumentException("Field '" + field + "' must be positive");
    }
}
