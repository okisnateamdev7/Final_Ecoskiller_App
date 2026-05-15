package com.ecoskiller.flyway.security;

import com.google.gson.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * SecurityManager — Central security layer for the Flyway Service MCP Server.
 *
 * Security layers:
 *  1. JSON-RPC structure validation
 *  2. Tool allowlist (explicit permit list — deny by default)
 *  3. Input sanitization (SQL injection, path traversal, XSS, length limits)
 *  4. HMAC-SHA256 request signing & verification
 *  5. Immutable audit logging (stderr, never stdout)
 *  6. Per-tool rate limiting (120 calls/min in-memory)
 *  7. Schema name validation (only known Ecoskiller schemas allowed)
 *  8. Migration version format validation (V{n}__{desc}.sql pattern)
 */
public class SecurityManager {

    private static final Logger LOGGER = Logger.getLogger(SecurityManager.class.getName());

    // ── 1. Tool Allowlist ─────────────────────────────────────────────────────

    private static final Set<String> ALLOWED_TOOLS = Set.of(
        "migration_apply",
        "migration_status",
        "migration_validate",
        "migration_repair",
        "migration_baseline",
        "migration_info",
        "schema_drift_detector",
        "schema_history_query",
        "checksum_validator",
        "multi_schema_manager",
        "helm_init_container_status",
        "kubernetes_secret_validator",
        "gitlab_pipeline_trigger",
        "environment_parity_checker",
        "dr_schema_consistency",
        "dr_runbook_executor",
        "rls_policy_tracker",
        "compliance_audit_reporter"
    );

    // ── 2. Known Ecoskiller Schemas ────────────────────────────────────────────

    public static final Set<String> KNOWN_SCHEMAS = Set.of(
        "core", "billing", "analytics", "realtime", "admin", "notification",
        "scoring", "certification", "interview", "job", "phone_bridge",
        "royalty", "innovation", "intelligence", "legal"
    );

    // ── 3. Known Environments ─────────────────────────────────────────────────

    public static final Set<String> KNOWN_ENVIRONMENTS = Set.of("dev", "test", "stage", "prod");

    // ── 4. Input Validation Constants ─────────────────────────────────────────

    private static final int     MAX_STRING_LENGTH = 2048;
    private static final int     MAX_NESTING_DEPTH = 6;
    private static final Pattern SQL_INJECTION      = Pattern.compile(
        "(?i)(;\\s*drop|;\\s*delete|;\\s*truncate|;\\s*insert|;\\s*update|" +
        "--\\s|/\\*|\\*/|xp_|exec\\s*\\(|union\\s+select|<script|javascript:)");
    private static final Pattern PATH_TRAVERSAL     = Pattern.compile("\\.\\./|\\.\\./|%2e%2e");
    private static final Pattern SAFE_KEY           = Pattern.compile("[\\w\\-\\.]+");
    private static final Pattern MIGRATION_VERSION  = Pattern.compile("V\\d+__[\\w\\-\\.]+\\.sql");

    // ── 5. Rate Limiting ──────────────────────────────────────────────────────

    private final Map<String, List<Long>> callTimestamps  = new HashMap<>();
    private static final int  RATE_LIMIT_PER_MINUTE       = 120;
    private static final long RATE_WINDOW_MS               = 60_000L;

    // ── 6. HMAC Key ───────────────────────────────────────────────────────────

    private final byte[] hmacKey;

    public SecurityManager() {
        String keyEnv = System.getenv("FLYWAY_MCP_HMAC_KEY");
        this.hmacKey = (keyEnv != null && !keyEnv.isBlank())
            ? keyEnv.getBytes(StandardCharsets.UTF_8)
            : "flyway-dev-key-change-in-production".getBytes(StandardCharsets.UTF_8);
        LOGGER.info("[Security] HMAC key source: " + (keyEnv != null ? "ENV (FLYWAY_MCP_HMAC_KEY)" : "DEFAULT — change in prod!"));
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /** Validates JSON-RPC 2.0 request structure. */
    public boolean validateRpcStructure(JsonObject req) {
        if (req == null) return false;
        if (!req.has("jsonrpc") || !req.has("method")) return false;
        if (!"2.0".equals(req.get("jsonrpc").getAsString())) return false;
        String method = req.get("method").getAsString();
        if (method == null || method.isBlank() || method.length() > 128) return false;
        return method.matches("[\\w/]+");
    }

    /** Checks whether a tool name is in the explicit allowlist. */
    public boolean isAllowedTool(String toolName) {
        return toolName != null && !toolName.isBlank() && ALLOWED_TOOLS.contains(toolName);
    }

    /** Deep-sanitizes all string values in a JsonObject. */
    public JsonObject sanitizeInputs(JsonObject raw) {
        return sanitizeObject(raw, 0);
    }

    /** Rate-limits tool invocations. Throws SecurityException if limit exceeded. */
    public synchronized void checkRateLimit(String toolName) {
        long now = System.currentTimeMillis();
        List<Long> ts = callTimestamps.computeIfAbsent(toolName, k -> new ArrayList<>());
        ts.removeIf(t -> now - t > RATE_WINDOW_MS);
        if (ts.size() >= RATE_LIMIT_PER_MINUTE) {
            throw new SecurityException("Rate limit exceeded for: " + toolName + " (" + RATE_LIMIT_PER_MINUTE + "/min)");
        }
        ts.add(now);
    }

    /** Signs a payload with HMAC-SHA256. */
    public String signPayload(String payload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(hmacKey, "HmacSHA256"));
            return Base64.getEncoder().encodeToString(
                mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new SecurityException("HMAC signing failed: " + e.getMessage());
        }
    }

    /** Constant-time HMAC verification. */
    public boolean verifySignature(String payload, String signature) {
        return MessageDigest.isEqual(
            signPayload(payload).getBytes(StandardCharsets.UTF_8),
            signature.getBytes(StandardCharsets.UTF_8));
    }

    /** Validates a Flyway migration file version name (V{n}__{desc}.sql). */
    public boolean isValidMigrationFile(String filename) {
        return filename != null && MIGRATION_VERSION.matcher(filename).matches();
    }

    /** Validates a schema name against known Ecoskiller schemas. */
    public boolean isKnownSchema(String schema) {
        return schema != null && KNOWN_SCHEMAS.contains(schema.toLowerCase());
    }

    /** Validates an environment name. */
    public boolean isKnownEnvironment(String env) {
        return env != null && KNOWN_ENVIRONMENTS.contains(env.toLowerCase());
    }

    /** Writes an audit log entry to stderr. */
    public void auditLog(String toolName, JsonObject sanitizedArgs) {
        LOGGER.info(String.format("[AUDIT] ts=%s tool=%s args_keys=%s",
            Instant.now(), toolName, sanitizedArgs.keySet()));
    }

    // ── Sanitization Internals ────────────────────────────────────────────────

    private JsonObject sanitizeObject(JsonObject obj, int depth) {
        if (depth > MAX_NESTING_DEPTH) {
            throw new SecurityException("Input nesting depth exceeded: " + MAX_NESTING_DEPTH);
        }
        JsonObject clean = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            String key = sanitizeKey(entry.getKey());
            clean.add(key, sanitizeElement(entry.getValue(), depth + 1));
        }
        return clean;
    }

    private String sanitizeKey(String key) {
        if (key == null || key.isBlank()) throw new SecurityException("Empty key in input");
        if (key.length() > 128)          throw new SecurityException("Key too long: " + key.length());
        if (!SAFE_KEY.matcher(key).matches()) throw new SecurityException("Unsafe key: " + key);
        return key;
    }

    private JsonElement sanitizeElement(JsonElement el, int depth) {
        if (el == null || el.isJsonNull()) return JsonNull.INSTANCE;
        if (el.isJsonPrimitive()) {
            JsonPrimitive p = el.getAsJsonPrimitive();
            if (p.isString()) return new JsonPrimitive(sanitizeString(p.getAsString()));
            return p;
        }
        if (el.isJsonObject()) return sanitizeObject(el.getAsJsonObject(), depth);
        if (el.isJsonArray()) {
            JsonArray arr = new JsonArray();
            for (JsonElement item : el.getAsJsonArray()) arr.add(sanitizeElement(item, depth));
            return arr;
        }
        return JsonNull.INSTANCE;
    }

    private String sanitizeString(String s) {
        if (s == null) return "";
        s = s.trim();
        if (s.length() > MAX_STRING_LENGTH) throw new SecurityException("String too long: " + s.length());
        if (SQL_INJECTION.matcher(s).find())  throw new SecurityException("SQL injection pattern detected");
        if (PATH_TRAVERSAL.matcher(s).find()) throw new SecurityException("Path traversal pattern detected");
        return s;
    }

    // ── Safe Accessors ────────────────────────────────────────────────────────

    public String getString(JsonObject args, String key, String def) {
        return args.has(key) && !args.get(key).isJsonNull() ? args.get(key).getAsString() : def;
    }

    public int getInt(JsonObject args, String key, int def) {
        return args.has(key) && !args.get(key).isJsonNull() ? args.get(key).getAsInt() : def;
    }

    public boolean getBool(JsonObject args, String key, boolean def) {
        return args.has(key) && !args.get(key).isJsonNull() ? args.get(key).getAsBoolean() : def;
    }
}
