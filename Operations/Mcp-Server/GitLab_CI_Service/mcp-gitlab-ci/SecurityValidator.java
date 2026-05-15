package com.ecoskiller.mcp.gitlab.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * SecurityValidator — Defence-in-depth for all incoming MCP tool calls.
 *
 * Responsibilities:
 * 1. Input sanitisation — rejects shell-injection, path-traversal, and SQL-injection patterns.
 * 2. Rate limiting — per-tool sliding-window (100 req / 60 s default, configurable).
 * 3. Argument length caps — prevents DoS via oversized payloads.
 * 4. Allowlist enforcement — only whitelisted environments and branch names pass.
 *
 * This class is intentionally final; it must not be subclassed or mocked in production.
 */
public final class SecurityValidator {

    private static final Logger log = LoggerFactory.getLogger(SecurityValidator.class);

    // ── Dangerous-pattern regexes ─────────────────────────────────────────────
    private static final Pattern SHELL_INJECTION = Pattern.compile(
            "[;&|`$<>\\\\]|\\$\\{|\\$\\(|\\beval\\b|\\bexec\\b|\\bsystem\\b",
            Pattern.CASE_INSENSITIVE
    );
    private static final Pattern PATH_TRAVERSAL = Pattern.compile(
            "\\.\\./|\\.\\./|%2e%2e|%252e|/etc/|/proc/|/sys/",
            Pattern.CASE_INSENSITIVE
    );
    private static final Pattern SQL_INJECTION = Pattern.compile(
            "('\\s*(or|and)\\s*')|union\\s+select|drop\\s+table|;\\s*--",
            Pattern.CASE_INSENSITIVE
    );
    private static final Pattern SCRIPT_INJECTION = Pattern.compile(
            "<script|javascript:|data:text/html|on\\w+=",
            Pattern.CASE_INSENSITIVE
    );

    // ── Allowlists ────────────────────────────────────────────────────────────
    private static final java.util.Set<String> ALLOWED_ENVIRONMENTS =
            java.util.Set.of("dev", "test", "stage", "prod");

    private static final Pattern BRANCH_NAME = Pattern.compile(
            "^(feature/[a-zA-Z0-9._-]+|develop|staging|main)$"
    );

    private static final Pattern SERVICE_NAME = Pattern.compile(
            "^[a-zA-Z0-9_-]{1,80}$"
    );

    private static final Pattern GIT_SHA = Pattern.compile(
            "^[0-9a-f]{7,40}$"
    );

    // ── Rate-limiting state ───────────────────────────────────────────────────
    private static final int MAX_REQUESTS_PER_WINDOW = 100;
    private static final long WINDOW_MS = 60_000L; // 60 seconds

    private final Map<String, RateWindow> rateWindows = new ConcurrentHashMap<>();

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Validate a raw string argument against all injection patterns.
     *
     * @param fieldName  Human-readable field name (for error messages only)
     * @param value      The value to validate
     * @param maxLength  Maximum permitted length
     * @throws SecurityException if the value fails any check
     */
    public void validateString(String fieldName, String value, int maxLength) {
        if (value == null) {
            throw new SecurityException("Field '" + fieldName + "' must not be null");
        }
        if (value.length() > maxLength) {
            throw new SecurityException(
                    "Field '" + fieldName + "' exceeds maximum length " + maxLength +
                    " (got " + value.length() + ")");
        }
        if (SHELL_INJECTION.matcher(value).find()) {
            log.warn("[SECURITY] Shell-injection pattern in field '{}': {}", fieldName, sanitizeForLog(value));
            throw new SecurityException("Field '" + fieldName + "' contains forbidden shell metacharacters");
        }
        if (PATH_TRAVERSAL.matcher(value).find()) {
            log.warn("[SECURITY] Path-traversal pattern in field '{}': {}", fieldName, sanitizeForLog(value));
            throw new SecurityException("Field '" + fieldName + "' contains a path traversal sequence");
        }
        if (SQL_INJECTION.matcher(value).find()) {
            log.warn("[SECURITY] SQL-injection pattern in field '{}': {}", fieldName, sanitizeForLog(value));
            throw new SecurityException("Field '" + fieldName + "' contains a SQL injection pattern");
        }
        if (SCRIPT_INJECTION.matcher(value).find()) {
            log.warn("[SECURITY] Script-injection pattern in field '{}': {}", fieldName, sanitizeForLog(value));
            throw new SecurityException("Field '" + fieldName + "' contains a script injection pattern");
        }
    }

    /** Validate that an environment is one of dev / test / stage / prod. */
    public void validateEnvironment(String env) {
        if (env == null || !ALLOWED_ENVIRONMENTS.contains(env.toLowerCase())) {
            throw new SecurityException(
                    "Invalid environment '" + env + "'. Must be one of: " + ALLOWED_ENVIRONMENTS);
        }
    }

    /** Validate a Git branch name against the EcoSkiller branch-naming policy. */
    public void validateBranchName(String branch) {
        if (branch == null || !BRANCH_NAME.matcher(branch).matches()) {
            throw new SecurityException(
                    "Invalid branch name '" + sanitizeForLog(branch) +
                    "'. Must match pattern: feature/<name>, develop, staging, or main");
        }
    }

    /** Validate a microservice name. */
    public void validateServiceName(String service) {
        if (service == null || !SERVICE_NAME.matcher(service).matches()) {
            throw new SecurityException(
                    "Invalid service name '" + sanitizeForLog(service) +
                    "'. Only alphanumeric, dash, and underscore allowed (max 80 chars)");
        }
    }

    /** Validate a git commit SHA (short 7-char or full 40-char). */
    public void validateGitSha(String sha) {
        if (sha == null || !GIT_SHA.matcher(sha).matches()) {
            throw new SecurityException(
                    "Invalid git SHA '" + sanitizeForLog(sha) +
                    "'. Must be 7-40 lowercase hex characters");
        }
    }

    /**
     * Enforce rate limit for a given tool name.
     *
     * @param toolName  The MCP tool being invoked
     * @throws SecurityException if the rate limit is exceeded
     */
    public void checkRateLimit(String toolName) {
        RateWindow window = rateWindows.computeIfAbsent(toolName, k -> new RateWindow());
        if (!window.tryAcquire()) {
            log.warn("[SECURITY] Rate limit exceeded for tool '{}'", toolName);
            throw new SecurityException(
                    "Rate limit exceeded for tool '" + toolName +
                    "'. Max " + MAX_REQUESTS_PER_WINDOW + " requests per 60 seconds");
        }
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    /** Truncate + strip control chars before writing to logs — prevents log injection. */
    private static String sanitizeForLog(String value) {
        if (value == null) return "<null>";
        return value.replaceAll("[\\p{Cntrl}]", "?")
                    .substring(0, Math.min(value.length(), 120));
    }

    // ── Rate-window inner class ───────────────────────────────────────────────

    private static final class RateWindow {
        private final AtomicInteger count = new AtomicInteger(0);
        private volatile long windowStart = System.currentTimeMillis();

        synchronized boolean tryAcquire() {
            long now = System.currentTimeMillis();
            if (now - windowStart > WINDOW_MS) {
                windowStart = now;
                count.set(0);
            }
            return count.incrementAndGet() <= MAX_REQUESTS_PER_WINDOW;
        }
    }
}
