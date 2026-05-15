package com.ecoskiller.ranking.security;

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
 * SecurityManager — Central security layer for the Candidate Ranking Engine MCP Server.
 *
 * Layers:
 *  1. JSON-RPC structure validation
 *  2. Tool allowlist (18 tools — explicit permit, deny-by-default)
 *  3. Input sanitisation (SQL injection, XSS, length limits, nesting depth)
 *  4. UUID format validation for tenant_id, job_id, candidate_id
 *  5. Score range validation (0.0 – 100.0)
 *  6. HMAC-SHA256 request signing & verification
 *  7. Immutable audit logging (stderr only — never stdout)
 *  8. Per-tool rate limiting (120 calls/min)
 */
public class SecurityManager {

    private static final Logger LOGGER = Logger.getLogger(SecurityManager.class.getName());

    // ── Tool Allowlist ────────────────────────────────────────────────────────

    private static final Set<String> ALLOWED_TOOLS = Set.of(
        "score_aggregator",
        "weighted_score_calculator",
        "rank_computer",
        "tie_resolver",
        "dimension_score_normalizer",
        "intelligence_signal_ingester",
        "xi_vector_integrator",
        "leaderboard_manager",
        "shortlist_generator",
        "cohort_stats_calculator",
        "kafka_event_consumer",
        "kafka_event_publisher",
        "dlq_handler",
        "belt_eligibility_evaluator",
        "rank_recomputer",
        "score_correction_handler",
        "metrics_reporter",
        "weight_matrix_manager"
    );

    // ── Valid Kafka Topics ────────────────────────────────────────────────────

    public static final Set<String> VALID_KAFKA_TOPICS = Set.of(
        "gd.completed",
        "interview.completed",
        "match.scored",
        "intelligence.signal.emitted",
        "assessment.score.corrected",
        "candidate.rank.computed",
        "ranking.recomputed",
        "gd.completed.dlq",
        "interview.completed.dlq",
        "match.scored.dlq"
    );

    // ── Valid Assessment Dimensions ────────────────────────────────────────────

    public static final Set<String> VALID_DIMENSIONS = Set.of(
        "communication", "problem_solving", "technical", "collaboration",
        "leadership", "innovation", "adaptability", "consistency"
    );

    // ── Valid Belt Tiers ──────────────────────────────────────────────────────

    public static final Set<String> VALID_BELT_TIERS = Set.of(
        "WHITE", "YELLOW", "ORANGE", "GREEN", "BLUE", "PURPLE", "RED", "BROWN", "BLACK"
    );

    // ── Input Validation ──────────────────────────────────────────────────────

    private static final int     MAX_STRING_LENGTH = 2048;
    private static final int     MAX_NESTING       = 6;
    private static final Pattern UUID_PATTERN      = Pattern.compile(
        "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern SQL_INJECTION      = Pattern.compile(
        "(?i)(;\\s*drop|;\\s*delete|union\\s+select|--\\s|/\\*|\\*/|xp_|exec\\s*\\()");
    private static final Pattern XSS               = Pattern.compile(
        "(?i)(<script|javascript:|onerror=|onload=|<iframe|<object)");
    private static final Pattern SAFE_KEY           = Pattern.compile("[\\w\\-.]+");

    // ── Rate Limiting ─────────────────────────────────────────────────────────

    private final Map<String, List<Long>> callTs = new HashMap<>();
    private static final int  RATE_LIMIT = 120;
    private static final long RATE_WIN   = 60_000L;

    // ── HMAC ──────────────────────────────────────────────────────────────────

    private final byte[] hmacKey;

    public SecurityManager() {
        String k = System.getenv("RANKING_MCP_HMAC_KEY");
        this.hmacKey = (k != null && !k.isBlank())
            ? k.getBytes(StandardCharsets.UTF_8)
            : "ranking-dev-key-change-in-prod".getBytes(StandardCharsets.UTF_8);
        LOGGER.info("[Security] HMAC key: " + (k != null ? "ENV" : "DEFAULT — change in prod!"));
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public boolean validateRpcStructure(JsonObject req) {
        if (req == null || !req.has("jsonrpc") || !req.has("method")) return false;
        if (!"2.0".equals(req.get("jsonrpc").getAsString())) return false;
        String m = req.get("method").getAsString();
        return m != null && !m.isBlank() && m.length() <= 128 && m.matches("[\\w/]+");
    }

    public boolean isAllowedTool(String name) {
        return name != null && !name.isBlank() && ALLOWED_TOOLS.contains(name);
    }

    public JsonObject sanitizeInputs(JsonObject raw) {
        return sanitizeObject(raw, 0);
    }

    public synchronized void checkRateLimit(String tool) {
        long now = System.currentTimeMillis();
        List<Long> ts = callTs.computeIfAbsent(tool, k -> new ArrayList<>());
        ts.removeIf(t -> now - t > RATE_WIN);
        if (ts.size() >= RATE_LIMIT)
            throw new SecurityException("Rate limit exceeded for: " + tool);
        ts.add(now);
    }

    public boolean isValidUuid(String s) {
        return s != null && UUID_PATTERN.matcher(s).matches();
    }

    public boolean isValidScore(double score) {
        return score >= 0.0 && score <= 100.0;
    }

    public boolean isValidTopic(String topic) {
        return topic != null && VALID_KAFKA_TOPICS.contains(topic);
    }

    public boolean isValidDimension(String dim) {
        return dim != null && VALID_DIMENSIONS.contains(dim);
    }

    public boolean isValidBelt(String belt) {
        return belt != null && VALID_BELT_TIERS.contains(belt.toUpperCase());
    }

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

    public boolean verifySignature(String payload, String sig) {
        return MessageDigest.isEqual(
            signPayload(payload).getBytes(StandardCharsets.UTF_8),
            sig.getBytes(StandardCharsets.UTF_8));
    }

    public void auditLog(String tool, JsonObject sanitizedArgs) {
        LOGGER.info(String.format("[AUDIT] ts=%s tool=%s keys=%s", Instant.now(), tool, sanitizedArgs.keySet()));
    }

    // ── Safe Accessors ────────────────────────────────────────────────────────

    public String  str (JsonObject a, String k, String d)   { return a.has(k) && !a.get(k).isJsonNull() ? a.get(k).getAsString()  : d; }
    public int     num (JsonObject a, String k, int d)      { return a.has(k) && !a.get(k).isJsonNull() ? a.get(k).getAsInt()     : d; }
    public double  dbl (JsonObject a, String k, double d)   { return a.has(k) && !a.get(k).isJsonNull() ? a.get(k).getAsDouble()  : d; }
    public boolean bool(JsonObject a, String k, boolean d)  { return a.has(k) && !a.get(k).isJsonNull() ? a.get(k).getAsBoolean() : d; }

    // ── Sanitisation Internals ────────────────────────────────────────────────

    private JsonObject sanitizeObject(JsonObject obj, int depth) {
        if (depth > MAX_NESTING) throw new SecurityException("Input nesting depth exceeded: " + MAX_NESTING);
        JsonObject clean = new JsonObject();
        for (Map.Entry<String, JsonElement> e : obj.entrySet()) {
            clean.add(sanitizeKey(e.getKey()), sanitizeElement(e.getValue(), depth + 1));
        }
        return clean;
    }

    private String sanitizeKey(String key) {
        if (key == null || key.isBlank()) throw new SecurityException("Empty key");
        if (key.length() > 128)           throw new SecurityException("Key too long");
        if (!SAFE_KEY.matcher(key).matches()) throw new SecurityException("Unsafe key: " + key);
        return key;
    }

    private JsonElement sanitizeElement(JsonElement el, int depth) {
        if (el == null || el.isJsonNull()) return JsonNull.INSTANCE;
        if (el.isJsonPrimitive()) {
            JsonPrimitive p = el.getAsJsonPrimitive();
            return p.isString() ? new JsonPrimitive(sanitizeString(p.getAsString())) : p;
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
        if (SQL_INJECTION.matcher(s).find()) throw new SecurityException("SQL injection pattern detected");
        if (XSS.matcher(s).find())           throw new SecurityException("XSS pattern detected");
        return s;
    }
}
