package com.ecoskiller.mcp.cert.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * SecurityValidator — Defence-in-depth for all incoming MCP tool calls.
 *
 * Layers:
 *  1. Shell / path / SQL / script injection blocking
 *  2. Domain-specific allowlists (belt levels, algorithms, blockchain networks, etc.)
 *  3. Per-tool sliding-window rate limiting (100 req / 60 s)
 *  4. Argument length caps (DoS prevention)
 *  5. UUID format enforcement (prevents certificate ID spoofing)
 *  6. Log sanitisation (prevent log injection of control chars)
 *
 * This class is intentionally final — must not be subclassed or mocked in production.
 */
public final class SecurityValidator {

    private static final Logger log = LoggerFactory.getLogger(SecurityValidator.class);

    // ── Injection-pattern regexes ─────────────────────────────────────────
    private static final Pattern SHELL_INJECTION = Pattern.compile(
            "[;&|`$<>\\\\]|\\$\\{|\\$\\(|\\beval\\b|\\bexec\\b|\\bsystem\\b",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern PATH_TRAVERSAL = Pattern.compile(
            "\\.\\./|\\.\\./|%2e%2e|%252e|/etc/|/proc/|/sys/",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern SQL_INJECTION = Pattern.compile(
            "('\\s*(or|and)\\s*')|union\\s+select|drop\\s+table|;\\s*--",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern SCRIPT_INJECTION = Pattern.compile(
            "<script|javascript:|data:text/html|on\\w+=",
            Pattern.CASE_INSENSITIVE);

    // ── Domain allowlists ─────────────────────────────────────────────────
    public static final Set<String> ALLOWED_BELT_LEVELS =
            Set.of("bronze", "silver", "gold", "platinum", "diamond");

    public static final Set<String> ALLOWED_ENVIRONMENTS =
            Set.of("dev", "test", "stage", "prod");

    public static final Set<String> ALLOWED_SIGN_ALGOS =
            Set.of("RS256", "ES256", "EdDSA");

    public static final Set<String> ALLOWED_BLOCKCHAIN_NETWORKS =
            Set.of("hyperledger-fabric", "ethereum", "polygon", "none");

    public static final Set<String> ALLOWED_CERT_STATUS =
            Set.of("VALID", "EXPIRED", "REVOKED", "INVALID_SIGNATURE", "PENDING");

    public static final Set<String> ALLOWED_SHARE_CHANNELS =
            Set.of("linkedin", "twitter", "email", "qr", "url");

    public static final Set<String> ALLOWED_REVOCATION_REASONS =
            Set.of("misconduct", "exam_fraud", "audit_finding", "candidate_request",
                   "data_correction", "policy_violation");

    // Pattern: UUID v4
    private static final Pattern UUID_PATTERN = Pattern.compile(
            "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
            Pattern.CASE_INSENSITIVE);

    // Candidate ID: alphanumeric + dash/underscore, 1-64 chars
    private static final Pattern CANDIDATE_ID_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_-]{1,64}$");

    // Tenant ID
    private static final Pattern TENANT_ID_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_-]{1,64}$");

    // ── Rate limiting ─────────────────────────────────────────────────────
    private static final int  MAX_REQ_PER_WINDOW = 100;
    private static final long WINDOW_MS          = 60_000L;

    private final Map<String, RateWindow> rateWindows = new ConcurrentHashMap<>();

    // ── Public API ────────────────────────────────────────────────────────

    /** Generic string safety check — injection + length cap. */
    public void validateString(String fieldName, String value, int maxLength) {
        if (value == null)
            throw new SecurityException("Field '" + fieldName + "' must not be null");
        if (value.length() > maxLength)
            throw new SecurityException("Field '" + fieldName + "' exceeds max length " +
                    maxLength + " (got " + value.length() + ")");
        if (SHELL_INJECTION.matcher(value).find())  reject(fieldName, "shell metacharacters");
        if (PATH_TRAVERSAL.matcher(value).find())   reject(fieldName, "path traversal sequence");
        if (SQL_INJECTION.matcher(value).find())    reject(fieldName, "SQL injection pattern");
        if (SCRIPT_INJECTION.matcher(value).find()) reject(fieldName, "script injection pattern");
    }

    /** Validate UUID v4 format (certificate_id, tenant_id references). */
    public void validateUUID(String fieldName, String value) {
        if (value == null || !UUID_PATTERN.matcher(value).matches())
            throw new SecurityException("Field '" + fieldName + "' is not a valid UUID v4: " +
                    sanitizeForLog(value));
    }

    /** Generate a fresh secure UUID (used when agents create new certificates). */
    public static String newCertId() {
        return UUID.randomUUID().toString();
    }

    public void validateBeltLevel(String belt) {
        if (belt == null || !ALLOWED_BELT_LEVELS.contains(belt.toLowerCase()))
            throw new SecurityException("Invalid belt level '" + sanitizeForLog(belt) +
                    "'. Allowed: " + ALLOWED_BELT_LEVELS);
    }

    public void validateEnvironment(String env) {
        if (env == null || !ALLOWED_ENVIRONMENTS.contains(env.toLowerCase()))
            throw new SecurityException("Invalid environment '" + sanitizeForLog(env) +
                    "'. Allowed: " + ALLOWED_ENVIRONMENTS);
    }

    public void validateSignAlgo(String algo) {
        if (algo == null || !ALLOWED_SIGN_ALGOS.contains(algo))
            throw new SecurityException("Invalid signing algorithm '" + sanitizeForLog(algo) +
                    "'. Allowed: " + ALLOWED_SIGN_ALGOS);
    }

    public void validateBlockchainNetwork(String network) {
        if (network == null || !ALLOWED_BLOCKCHAIN_NETWORKS.contains(network.toLowerCase()))
            throw new SecurityException("Invalid blockchain network '" + sanitizeForLog(network) +
                    "'. Allowed: " + ALLOWED_BLOCKCHAIN_NETWORKS);
    }

    public void validateRevocationReason(String reason) {
        if (reason == null || !ALLOWED_REVOCATION_REASONS.contains(reason.toLowerCase()))
            throw new SecurityException("Invalid revocation reason '" + sanitizeForLog(reason) +
                    "'. Allowed: " + ALLOWED_REVOCATION_REASONS);
    }

    public void validateShareChannel(String channel) {
        if (channel == null || !ALLOWED_SHARE_CHANNELS.contains(channel.toLowerCase()))
            throw new SecurityException("Invalid share channel '" + sanitizeForLog(channel) +
                    "'. Allowed: " + ALLOWED_SHARE_CHANNELS);
    }

    public void validateCandidateId(String id) {
        if (id == null || !CANDIDATE_ID_PATTERN.matcher(id).matches())
            throw new SecurityException("Invalid candidate_id '" + sanitizeForLog(id) +
                    "'. Must be alphanumeric/dash/underscore, max 64 chars");
    }

    public void validateTenantId(String id) {
        if (id == null || !TENANT_ID_PATTERN.matcher(id).matches())
            throw new SecurityException("Invalid tenant_id '" + sanitizeForLog(id) +
                    "'. Must be alphanumeric/dash/underscore, max 64 chars");
    }

    public void validateScore(double score) {
        if (score < 0.0 || score > 100.0)
            throw new SecurityException("Score out of range: " + score +
                    ". Must be 0.0–100.0");
    }

    /** Per-tool rate limiting (100 req / 60 s sliding window). */
    public void checkRateLimit(String toolName) {
        RateWindow w = rateWindows.computeIfAbsent(toolName, k -> new RateWindow());
        if (!w.tryAcquire()) {
            log.warn("[SECURITY] Rate limit exceeded for tool '{}'", toolName);
            throw new SecurityException("Rate limit exceeded for tool '" + toolName +
                    "'. Max " + MAX_REQ_PER_WINDOW + " requests per 60 seconds");
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private void reject(String field, String reason) {
        log.warn("[SECURITY] Field '{}' contains {}", field, reason);
        throw new SecurityException("Field '" + field + "' contains a " + reason);
    }

    /** Strip control chars before writing to logs — prevents log injection. */
    public static String sanitizeForLog(String value) {
        if (value == null) return "<null>";
        return value.replaceAll("[\\p{Cntrl}]", "?")
                    .substring(0, Math.min(value.length(), 120));
    }

    // ── Rate-window ───────────────────────────────────────────────────────

    private static final class RateWindow {
        private final AtomicInteger count = new AtomicInteger(0);
        private volatile long windowStart = System.currentTimeMillis();

        synchronized boolean tryAcquire() {
            long now = System.currentTimeMillis();
            if (now - windowStart > WINDOW_MS) {
                windowStart = now;
                count.set(0);
            }
            return count.incrementAndGet() <= MAX_REQ_PER_WINDOW;
        }
    }
}
