package com.ecoskiller.mcp.minio.security;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * SecurityManager — enforces all security controls for the Ecoskiller MinIO MCP Server.
 *
 * Responsibilities:
 *  1. Credential loading — reads MINIO_ENDPOINT, MINIO_ACCESS_KEY, MINIO_SECRET_KEY
 *                          from environment variables only. Never from code or args.
 *  2. Input validation  — enforces S3 naming rules, key length, object key safety.
 *  3. Rate limiting     — per-tool sliding-window limiter (prevents abuse).
 *  4. Audit logging     — structured audit trail written to stderr (never stdout).
 *  5. Error sanitisation— strips stack traces / internal paths before returning errors.
 *
 * Security model:
 *  - No credentials in source code, logs, or MCP responses.
 *  - All stderr audit lines follow the format: [AUDIT] timestamp tool caller status.
 *  - Bucket-name validation: 3–63 chars, lowercase a–z 0–9 hyphen, no IP address.
 *  - Object-key validation: 1–1024 UTF-8 chars, no null bytes, no path traversal.
 *  - Content-type whitelist: common media types used by Ecoskiller assessments.
 */
public final class SecurityManager {

    // ── Naming rules (AWS/MinIO spec) ─────────────────────────────────────────

    /** S3 bucket name: 3–63 chars, lowercase alphanumeric + hyphens, no IP-like names. */
    private static final Pattern BUCKET_NAME_PATTERN =
            Pattern.compile("^(?!\\d{1,3}(\\.\\d{1,3}){3}$)[a-z0-9][a-z0-9\\-]{1,61}[a-z0-9]$");

    /** Very short single-segment buckets (2 chars are invalid). */
    private static final Pattern BUCKET_SHORT = Pattern.compile("^[a-z0-9]{1,2}$");

    /** Object key: no null bytes, no ../  traversal, max 1024 UTF-8 chars. */
    private static final Pattern OBJECT_KEY_TRAVERSAL = Pattern.compile("(^|/)\\.\\.(^|/|$)");

    /** Tenant ID used in bucket provisioning: alphanumeric + hyphen, max 48 chars. */
    private static final Pattern TENANT_ID_PATTERN = Pattern.compile("^[a-z0-9\\-]{1,48}$");

    // ── Whitelisted content types (Ecoskiller assessment media) ───────────────
    private static final java.util.Set<String> ALLOWED_CONTENT_TYPES = java.util.Set.of(
            "video/mp4", "video/webm", "video/ogg",
            "audio/mpeg", "audio/wav", "audio/ogg",
            "application/pdf",
            "image/jpeg", "image/png", "image/gif", "image/webp",
            "text/plain", "text/csv",
            "application/json",
            "application/zip",
            "application/octet-stream",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    // ── Rate limiting: max 60 calls per tool per minute ───────────────────────
    private static final int RATE_LIMIT_PER_MINUTE = 60;
    private final Map<String, RateWindow> rateLimitMap = new ConcurrentHashMap<>();

    // ── Credentials (loaded once at startup from env) ─────────────────────────
    private final String endpoint;
    private final String accessKey;
    private final String secretKey;
    private final boolean tlsEnabled;

    public SecurityManager() {
        this.endpoint   = requireEnv("MINIO_ENDPOINT");
        this.accessKey  = requireEnv("MINIO_ACCESS_KEY");
        this.secretKey  = requireEnv("MINIO_SECRET_KEY");
        // TLS is ON by default; set MINIO_TLS_DISABLED=true only for local dev
        this.tlsEnabled = !"true".equalsIgnoreCase(System.getenv("MINIO_TLS_DISABLED"));
        audit("STARTUP", "server", "credentials_loaded endpoint=" + sanitiseEndpoint(endpoint));
    }

    // ── Public credential accessors ───────────────────────────────────────────

    public String getEndpoint()  { return endpoint;  }
    public String getAccessKey() { return accessKey; }
    public String getSecretKey() { return secretKey; }
    public boolean isTlsEnabled() { return tlsEnabled; }

    // ── Input validation ──────────────────────────────────────────────────────

    /**
     * Validates a bucket name per S3 spec.
     * @throws SecurityException if invalid
     */
    public void validateBucketName(String name) {
        if (name == null || name.isEmpty()) {
            throw new SecurityException("Bucket name must not be empty.");
        }
        if (BUCKET_SHORT.matcher(name).matches()) {
            throw new SecurityException("Bucket name too short (min 3 chars): " + name);
        }
        if (name.length() > 63) {
            throw new SecurityException("Bucket name too long (max 63 chars): " + name.length());
        }
        if (!BUCKET_NAME_PATTERN.matcher(name).matches()) {
            throw new SecurityException(
                    "Invalid bucket name. Must be lowercase alphanumeric/hyphens, 3–63 chars. Got: " + name);
        }
    }

    /**
     * Validates an object key per S3 spec.
     * @throws SecurityException if invalid
     */
    public void validateObjectKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new SecurityException("Object key must not be empty.");
        }
        if (key.length() > 1024) {
            throw new SecurityException("Object key too long (max 1024 chars): " + key.length());
        }
        if (key.contains("\0")) {
            throw new SecurityException("Object key contains null byte.");
        }
        if (OBJECT_KEY_TRAVERSAL.matcher(key).find()) {
            throw new SecurityException("Object key contains path traversal sequence: " + key);
        }
    }

    /**
     * Validates a content-type against the Ecoskiller whitelist.
     * Falls back to application/octet-stream for unknown types.
     */
    public String validateAndNormaliseContentType(String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return "application/octet-stream";
        }
        String lower = contentType.trim().toLowerCase();
        return ALLOWED_CONTENT_TYPES.contains(lower) ? lower : "application/octet-stream";
    }

    /**
     * Validates a tenant ID used in bucket provisioning.
     * @throws SecurityException if invalid
     */
    public void validateTenantId(String tenantId) {
        if (tenantId == null || tenantId.isEmpty()) {
            throw new SecurityException("Tenant ID must not be empty.");
        }
        if (!TENANT_ID_PATTERN.matcher(tenantId).matches()) {
            throw new SecurityException(
                    "Invalid tenant ID. Must be lowercase alphanumeric/hyphens, max 48 chars. Got: " + tenantId);
        }
    }

    /**
     * Validates a URL TTL (presigned URL expiry).
     * Enforced range: 60 seconds – 7 days.
     */
    public int validatePresignedTtlSeconds(int ttlSeconds) {
        if (ttlSeconds < 60)       return 3600;   // default 1 hour
        if (ttlSeconds > 604_800)  return 604_800; // cap at 7 days
        return ttlSeconds;
    }

    /**
     * Validates that a JSON policy string is structurally plausible (basic check).
     * @throws SecurityException if obviously invalid
     */
    public void validatePolicyJson(String policy) {
        if (policy == null || policy.isBlank()) {
            throw new SecurityException("Policy JSON must not be empty.");
        }
        String trimmed = policy.trim();
        if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
            throw new SecurityException("Policy must be a JSON object.");
        }
        if (trimmed.length() > 20_000) {
            throw new SecurityException("Policy JSON too large (max 20KB).");
        }
    }

    // ── Rate limiting ─────────────────────────────────────────────────────────

    /**
     * Checks rate limit for a given tool name.
     * @throws SecurityException if rate limit exceeded
     */
    public void checkRateLimit(String toolName) {
        RateWindow window = rateLimitMap.computeIfAbsent(toolName, k -> new RateWindow());
        if (!window.tryConsume()) {
            throw new SecurityException(
                    "Rate limit exceeded for tool '" + toolName + "'. Max " + RATE_LIMIT_PER_MINUTE + " calls/min.");
        }
    }

    // ── Audit logging ─────────────────────────────────────────────────────────

    /**
     * Writes a structured audit line to stderr.
     * Format: [AUDIT] ISO-timestamp TOOL CALLER STATUS [extra]
     */
    public void audit(String tool, String caller, String status) {
        System.err.printf("[AUDIT] %s tool=%s caller=%s status=%s%n",
                Instant.now(), tool, caller, status);
    }

    /**
     * Sanitises an exception for safe inclusion in MCP responses.
     * Strips stack traces and internal path information.
     */
    public String sanitiseError(Throwable t) {
        if (t instanceof SecurityException) {
            return "Security violation: " + t.getMessage();
        }
        // For all other exceptions: log detail to stderr, return generic message
        System.err.println("[ERROR] " + Instant.now() + " " + t.getClass().getSimpleName()
                + ": " + t.getMessage());
        return "Operation failed: " + t.getClass().getSimpleName()
                + (t.getMessage() != null ? " — " + truncate(t.getMessage(), 200) : "");
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private static String requireEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable not set: " + name
                    + ". Set MINIO_ENDPOINT, MINIO_ACCESS_KEY, MINIO_SECRET_KEY before starting.");
        }
        return value.trim();
    }

    /** Removes credentials from endpoint URL for safe logging. */
    private static String sanitiseEndpoint(String endpoint) {
        return endpoint.replaceAll("://[^@]+@", "://***@");
    }

    private static String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max) + "...";
    }

    // ── Inner class: sliding-window rate limiter ──────────────────────────────

    private static final class RateWindow {
        private long windowStart = System.currentTimeMillis();
        private final AtomicInteger count = new AtomicInteger(0);

        synchronized boolean tryConsume() {
            long now = System.currentTimeMillis();
            if (now - windowStart >= 60_000) {
                windowStart = now;
                count.set(0);
            }
            return count.incrementAndGet() <= RATE_LIMIT_PER_MINUTE;
        }
    }
}
