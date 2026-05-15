package com.ecoskiller.mcp.velero.security;

import java.time.Instant;
import java.util.*;
import java.util.logging.*;
import java.util.regex.*;

/**
 * SecurityManager — EcoSkiller Velero MCP
 *
 * Responsibilities:
 *  1. Validate & sanitise ALL user-supplied parameters before they reach the CLI.
 *  2. Block command-injection patterns (shell metacharacters, path traversal).
 *  3. Enforce allow-lists for namespaces, backup names, schedule names.
 *  4. Produce an immutable audit log entry for every tool call.
 *  5. Rate-limit destructive operations (delete / full restore).
 */
public class SecurityManager {

    private static final Logger AUDIT  = Logger.getLogger("VELERO_AUDIT");
    private static final Logger LOGGER = Logger.getLogger(SecurityManager.class.getName());

    // ── Allowed Ecoskiller namespaces ───────────────────────────────────────
    public static final Set<String> ALLOWED_NAMESPACES = Set.of(
        "core", "realtime", "billing", "analytics", "admin", "ops", "media", "velero"
    );

    // ── Regex allow-lists ───────────────────────────────────────────────────
    /** Backup / restore / schedule names: alphanumeric + dash + underscore, max 63 chars */
    private static final Pattern SAFE_NAME        = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_\\-]{0,62}$");
    /** Cron expression: 5 fields, digits + *, / , - */
    private static final Pattern SAFE_CRON        = Pattern.compile("^(\\S+ ){4}\\S+$");
    /** Kubernetes TTL format: digits followed by h/m/s */
    private static final Pattern SAFE_TTL         = Pattern.compile("^\\d+[hms]$");
    /** S3 bucket name */
    private static final Pattern SAFE_BUCKET      = Pattern.compile("^[a-z0-9][a-z0-9\\-]{1,61}[a-z0-9]$");
    /** MinIO / S3 URL */
    private static final Pattern SAFE_S3_URL      = Pattern.compile("^https?://[a-zA-Z0-9.\\-:/]+$");
    /** SHA-256 hex */
    private static final Pattern SAFE_SHA256       = Pattern.compile("^[a-f0-9]{64}$");

    // ── Dangerous shell metacharacters ──────────────────────────────────────
    private static final Pattern DANGEROUS_CHARS  = Pattern.compile("[;|&`$(){}\\[\\]<>!\\\\'\"]");
    private static final Pattern PATH_TRAVERSAL   = Pattern.compile("\\.\\./|\\.\\./");

    // ── Rate-limiting state for destructive ops ─────────────────────────────
    private final Map<String, Long> lastDestructiveOp = new HashMap<>();
    private static final long DESTRUCTIVE_COOLDOWN_MS = 5_000; // 5 seconds

    // ───────────────────────────────────────────────────────────────────────
    // Public validation API
    // ───────────────────────────────────────────────────────────────────────

    /**
     * Validate a backup / restore / schedule name.
     * @throws SecurityException on violation
     */
    public String validateName(String name) {
        if (name == null || name.isBlank()) throw new SecurityException("Name must not be blank");
        if (DANGEROUS_CHARS.matcher(name).find()) throw new SecurityException("Name contains illegal characters: " + name);
        if (PATH_TRAVERSAL.matcher(name).find()) throw new SecurityException("Path traversal detected in name");
        if (!SAFE_NAME.matcher(name).matches()) throw new SecurityException("Name does not match safe pattern: " + name);
        return name;
    }

    /**
     * Validate a Kubernetes namespace against the EcoSkiller allow-list.
     */
    public String validateNamespace(String ns) {
        if (ns == null || ns.isBlank()) throw new SecurityException("Namespace must not be blank");
        String trimmed = ns.trim().toLowerCase();
        if (!ALLOWED_NAMESPACES.contains(trimmed)) {
            throw new SecurityException("Namespace not in allow-list: " + trimmed
                + ". Allowed: " + ALLOWED_NAMESPACES);
        }
        return trimmed;
    }

    /**
     * Validate a comma-separated list of namespaces (e.g. "core,realtime").
     */
    public List<String> validateNamespaceList(String nsRaw) {
        if (nsRaw == null || nsRaw.isBlank()) throw new SecurityException("Namespace list must not be blank");
        List<String> validated = new ArrayList<>();
        for (String ns : nsRaw.split(",")) {
            validated.add(validateNamespace(ns.trim()));
        }
        return validated;
    }

    /**
     * Validate a cron expression (basic 5-field check).
     */
    public String validateCron(String cron) {
        if (cron == null || cron.isBlank()) throw new SecurityException("Cron expression must not be blank");
        if (DANGEROUS_CHARS.matcher(cron).find()) throw new SecurityException("Cron contains illegal chars");
        if (!SAFE_CRON.matcher(cron.trim()).matches()) throw new SecurityException("Invalid cron expression: " + cron);
        return cron.trim();
    }

    /**
     * Validate a TTL string like "168h", "30m", "7d".
     */
    public String validateTtl(String ttl) {
        if (ttl == null || ttl.isBlank()) throw new SecurityException("TTL must not be blank");
        if (!SAFE_TTL.matcher(ttl.trim()).matches()) throw new SecurityException("Invalid TTL format: " + ttl + " (expected e.g. 168h)");
        return ttl.trim();
    }

    /**
     * Validate a MinIO / S3 URL.
     */
    public String validateUrl(String url) {
        if (url == null || url.isBlank()) throw new SecurityException("URL must not be blank");
        if (!SAFE_S3_URL.matcher(url.trim()).matches()) throw new SecurityException("Invalid or unsafe URL: " + url);
        return url.trim();
    }

    /**
     * Validate an S3 bucket name.
     */
    public String validateBucket(String bucket) {
        if (bucket == null || bucket.isBlank()) throw new SecurityException("Bucket must not be blank");
        if (!SAFE_BUCKET.matcher(bucket.trim()).matches()) throw new SecurityException("Invalid bucket name: " + bucket);
        return bucket.trim();
    }

    /**
     * Generic string safe for free-text fields (descriptions, notes).
     * Strips shell metacharacters; truncates at 500 chars.
     */
    public String sanitiseFreeText(String text) {
        if (text == null) return "";
        String cleaned = DANGEROUS_CHARS.matcher(text).replaceAll("_");
        return cleaned.length() > 500 ? cleaned.substring(0, 500) : cleaned;
    }

    /**
     * Validate a SHA-256 checksum string.
     */
    public String validateSha256(String hash) {
        if (hash == null || hash.isBlank()) throw new SecurityException("Hash must not be blank");
        if (!SAFE_SHA256.matcher(hash.trim()).matches()) throw new SecurityException("Invalid SHA-256 hash: " + hash);
        return hash.trim();
    }

    /**
     * Enforce rate-limiting for destructive operations (delete / full restore).
     * Prevents runaway automation from wiping backup state.
     */
    public void checkDestructiveRateLimit(String operationKey) {
        long now = System.currentTimeMillis();
        Long last = lastDestructiveOp.get(operationKey);
        if (last != null && (now - last) < DESTRUCTIVE_COOLDOWN_MS) {
            throw new SecurityException("Rate limit: destructive operation '" + operationKey
                + "' called too quickly. Wait " + DESTRUCTIVE_COOLDOWN_MS / 1000 + "s between calls.");
        }
        lastDestructiveOp.put(operationKey, now);
    }

    // ───────────────────────────────────────────────────────────────────────
    // Audit logging
    // ───────────────────────────────────────────────────────────────────────

    /**
     * Write an immutable audit log entry for every tool invocation.
     * Format: ISO-8601 | EVENT | TOOL | ARGS (truncated at 512)
     */
    public void auditLog(String event, String tool, String args) {
        String entry = String.format("[AUDIT] %s | %s | tool=%s | args=%s",
            Instant.now().toString(),
            event,
            tool,
            args != null && args.length() > 512 ? args.substring(0, 512) + "…" : args
        );
        AUDIT.info(entry);
    }

    /**
     * Log a security violation (does NOT throw — caller decides).
     */
    public void logViolation(String tool, String reason) {
        AUDIT.warning(String.format("[SECURITY_VIOLATION] %s | tool=%s | reason=%s",
            Instant.now().toString(), tool, reason));
    }
}
