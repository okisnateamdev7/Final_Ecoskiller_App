package com.ecoskiller.dojo.security;

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
 * SecurityManager — Central security layer for Dojo Match Engine MCP Server.
 *
 * Responsibilities:
 *  1. JSON-RPC structure validation
 *  2. Tool allowlist enforcement
 *  3. Input sanitization (XSS, injection, length limits)
 *  4. HMAC-SHA256 request signing / verification
 *  5. Immutable audit logging
 *  6. Rate limiting per tool
 */
public class SecurityManager {

    private static final Logger LOGGER = Logger.getLogger(SecurityManager.class.getName());

    // ── Allowlisted tools (explicit permit list — deny by default) ─────────────
    private static final Set<String> ALLOWED_TOOLS = Set.of(
        "find_match",
        "candidate_queue",
        "interviewer_availability",
        "skill_compatibility_filter",
        "timezone_constraint_check",
        "fairness_optimizer",
        "match_score_calculator",
        "session_initiation",
        "no_show_handler",
        "load_balancer",
        "kafka_event_publisher",
        "match_result_publisher",
        "fairness_audit_log",
        "metrics_collector",
        "fallback_match_engine",
        "interview_history_tracker",
        "concurrent_capacity_manager",
        "match_confidence_scorer"
    );

    // ── Input validation constants ─────────────────────────────────────────────
    private static final int    MAX_STRING_LENGTH = 1024;
    private static final int    MAX_NESTING_DEPTH = 5;
    private static final Pattern SAFE_STRING       = Pattern.compile("^[\\w\\s\\-.:@+/]+$");
    private static final Pattern SQL_INJECTION      = Pattern.compile(
        "(?i)(select|insert|update|delete|drop|union|exec|script|<|>|'|\"|;|--|/\\*)");

    // ── Rate limiting (in-memory, per tool) ───────────────────────────────────
    private final Map<String, List<Long>> callTimestamps = new HashMap<>();
    private static final int   RATE_LIMIT_PER_MINUTE = 120;
    private static final long  RATE_WINDOW_MS         = 60_000L;

    // ── HMAC key (in production: load from env / Kubernetes Secret) ───────────
    private final byte[] hmacKey;

    public SecurityManager() {
        String keyEnv = System.getenv("DOJO_HMAC_KEY");
        this.hmacKey = (keyEnv != null && !keyEnv.isBlank())
            ? keyEnv.getBytes(StandardCharsets.UTF_8)
            : "dojo-dev-key-change-in-production".getBytes(StandardCharsets.UTF_8);
        LOGGER.info("[Security] SecurityManager initialized. HMAC key source: "
            + (keyEnv != null ? "ENV" : "DEFAULT (change in prod!)"));
    }

    // ── 1. JSON-RPC Structure Validation ──────────────────────────────────────

    public boolean validateRpcStructure(JsonObject req) {
        if (req == null) return false;
        if (!req.has("jsonrpc") || !req.has("method")) return false;
        if (!"2.0".equals(req.get("jsonrpc").getAsString())) return false;
        String method = req.get("method").getAsString();
        if (method == null || method.isBlank() || method.length() > 128) return false;
        // method must be alphanumeric/slash/underscore
        if (!method.matches("[\\w/]+")) return false;
        return true;
    }

    // ── 2. Tool Allowlist ─────────────────────────────────────────────────────

    public boolean isAllowedTool(String toolName) {
        if (toolName == null || toolName.isBlank()) return false;
        return ALLOWED_TOOLS.contains(toolName);
    }

    // ── 3. Input Sanitization ─────────────────────────────────────────────────

    /**
     * Deep-sanitizes a JsonObject: trims strings, enforces length limits,
     * rejects SQL/XSS patterns, limits nesting depth.
     */
    public JsonObject sanitizeInputs(JsonObject raw) {
        return sanitizeObject(raw, 0);
    }

    private JsonObject sanitizeObject(JsonObject obj, int depth) {
        if (depth > MAX_NESTING_DEPTH) {
            throw new SecurityException("Input nesting depth exceeded limit of " + MAX_NESTING_DEPTH);
        }
        JsonObject clean = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            String key = sanitizeKey(entry.getKey());
            JsonElement val = sanitizeElement(entry.getValue(), depth + 1);
            clean.add(key, val);
        }
        return clean;
    }

    private String sanitizeKey(String key) {
        if (key == null || key.isBlank()) throw new SecurityException("Empty key in input");
        if (key.length() > 128) throw new SecurityException("Key too long: " + key.length());
        if (!key.matches("[\\w\\-\\.]+")) throw new SecurityException("Unsafe key: " + key);
        return key;
    }

    private JsonElement sanitizeElement(JsonElement el, int depth) {
        if (el == null || el.isJsonNull()) return JsonNull.INSTANCE;
        if (el.isJsonPrimitive()) {
            JsonPrimitive p = el.getAsJsonPrimitive();
            if (p.isString()) {
                return new JsonPrimitive(sanitizeString(p.getAsString()));
            }
            return p; // numbers/booleans are safe as-is
        }
        if (el.isJsonObject()) return sanitizeObject(el.getAsJsonObject(), depth);
        if (el.isJsonArray()) {
            JsonArray arr = new JsonArray();
            for (JsonElement item : el.getAsJsonArray()) {
                arr.add(sanitizeElement(item, depth));
            }
            return arr;
        }
        return JsonNull.INSTANCE;
    }

    private String sanitizeString(String s) {
        if (s == null) return "";
        s = s.trim();
        if (s.length() > MAX_STRING_LENGTH) {
            throw new SecurityException("String value exceeds max length: " + s.length());
        }
        if (SQL_INJECTION.matcher(s).find()) {
            throw new SecurityException("Potentially unsafe pattern detected in input");
        }
        return s;
    }

    // ── 4. HMAC-SHA256 Signing & Verification ─────────────────────────────────

    public String signPayload(String payload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(hmacKey, "HmacSHA256"));
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new SecurityException("HMAC signing failed: " + e.getMessage());
        }
    }

    public boolean verifySignature(String payload, String signature) {
        String expected = signPayload(payload);
        return MessageDigest.isEqual(
            expected.getBytes(StandardCharsets.UTF_8),
            signature.getBytes(StandardCharsets.UTF_8)
        );
    }

    // ── 5. Audit Logging ──────────────────────────────────────────────────────

    public void auditLog(String toolName, JsonObject sanitizedArgs) {
        String timestamp = Instant.now().toString();
        // Log to stderr (never stdout — that's the MCP channel)
        LOGGER.info(String.format("[AUDIT] ts=%s tool=%s args_keys=%s",
            timestamp, toolName, sanitizedArgs.keySet()));
    }

    // ── 6. Rate Limiting ──────────────────────────────────────────────────────

    public synchronized void checkRateLimit(String toolName) {
        long now = System.currentTimeMillis();
        List<Long> timestamps = callTimestamps.computeIfAbsent(toolName, k -> new ArrayList<>());
        // Remove old timestamps outside the window
        timestamps.removeIf(t -> now - t > RATE_WINDOW_MS);
        if (timestamps.size() >= RATE_LIMIT_PER_MINUTE) {
            throw new SecurityException("Rate limit exceeded for tool: " + toolName
                + " (" + RATE_LIMIT_PER_MINUTE + "/min)");
        }
        timestamps.add(now);
    }

    // ── Utility ───────────────────────────────────────────────────────────────

    /** Safely get a string arg with a default. */
    public String getString(JsonObject args, String key, String defaultVal) {
        if (args.has(key) && !args.get(key).isJsonNull()) {
            return args.get(key).getAsString();
        }
        return defaultVal;
    }

    public int getInt(JsonObject args, String key, int defaultVal) {
        if (args.has(key) && !args.get(key).isJsonNull()) {
            return args.get(key).getAsInt();
        }
        return defaultVal;
    }

    public double getDouble(JsonObject args, String key, double defaultVal) {
        if (args.has(key) && !args.get(key).isJsonNull()) {
            return args.get(key).getAsDouble();
        }
        return defaultVal;
    }
}
