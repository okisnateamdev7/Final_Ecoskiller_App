package io.ecoskiller.royalty.security;

import com.fasterxml.jackson.databind.JsonNode;
import io.ecoskiller.royalty.config.ServerConfig;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.regex.Pattern;

// ═══════════════════════════════════════════════════════════════════════════════
// INPUT SANITIZER
// ═══════════════════════════════════════════════════════════════════════════════
public final class InputSanitizer {

    private static final Pattern ID_PATTERN      = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9\\-_]{0,63}$");
    private static final Pattern UUID_PATTERN    = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    private static final Pattern IP_TYPE_PATTERN = Pattern.compile("^(problem|interview_question|discussion_scenario|idea|premium_content)$");
    private static final Pattern HASH_PATTERN    = Pattern.compile("^(sha256:|sha512:|md5:)[a-fA-F0-9]{32,128}$");
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("^(creator_balance|platform_earnings|tax_withholding|disputed_balance|payout_pending|payout_completed|refund)$");
    private static final Pattern CREATOR_TYPE    = Pattern.compile("^(individual|foreign|corporate|nonprofit|government)$");
    private static final Pattern TIER_PATTERN    = Pattern.compile("^(tier1|tier2|tier3)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern DATE_PATTERN    = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    private InputSanitizer() {}

    public static void validateId(String id, String field) {
        if (id == null || (!ID_PATTERN.matcher(id).matches() && !UUID_PATTERN.matcher(id).matches()))
            throw new SecurityException("Invalid " + field + " format: " + sanitizeForLog(id));
    }

    public static void validateIpType(String type) {
        if (type == null || !IP_TYPE_PATTERN.matcher(type).matches())
            throw new SecurityException("Invalid ip_type '" + sanitizeForLog(type) + "'. Allowed: problem|interview_question|discussion_scenario|idea|premium_content");
    }

    public static void validateContentHash(String hash) {
        if (hash == null || !HASH_PATTERN.matcher(hash).matches())
            throw new SecurityException("Invalid content_hash format. Expected sha256:/sha512: prefix + hex digest");
    }

    public static void validateAccountType(String type) {
        if (type == null || !ACCOUNT_PATTERN.matcher(type).matches())
            throw new SecurityException("Invalid account_type '" + sanitizeForLog(type) + "'");
    }

    public static void validateCreatorType(String type) {
        if (type == null || !CREATOR_TYPE.matcher(type).matches())
            throw new SecurityException("Invalid creator_type '" + sanitizeForLog(type) + "'. Allowed: individual|foreign|corporate|nonprofit|government");
    }

    public static void validateTier(String tier) {
        if (tier == null || !TIER_PATTERN.matcher(tier).matches())
            throw new SecurityException("Invalid tier '" + sanitizeForLog(tier) + "'. Allowed: tier1|tier2|tier3");
    }

    public static void validateDate(String date, String field) {
        if (date != null && !date.isBlank() && !DATE_PATTERN.matcher(date).matches())
            throw new IllegalArgumentException(field + " must be YYYY-MM-DD format, got: " + sanitizeForLog(date));
    }

    public static void validatePositiveAmount(double amount, String field) {
        if (amount < 0 || amount > 100_000_000)
            throw new IllegalArgumentException(field + " must be between 0 and ₹10 crore, got: " + amount);
    }

    public static void validateSplitPercentage(double pct, String field) {
        if (pct <= 0 || pct > 100)
            throw new IllegalArgumentException(field + " must be 0-100%, got: " + pct);
    }

    public static void validateRange(int v, int min, int max, String field) {
        if (v < min || v > max) throw new IllegalArgumentException(field + " must be " + min + "-" + max + ", got: " + v);
    }

    public static void requireNonBlank(String v, String field) {
        if (v == null || v.isBlank()) throw new IllegalArgumentException("Required field '" + field + "' is missing");
    }

    public static void validateEnum(String v, String field, String... allowed) {
        for (String a : allowed) if (a.equalsIgnoreCase(v)) return;
        throw new IllegalArgumentException("Invalid '" + field + "': " + sanitizeForLog(v) + ". Allowed: " + Arrays.toString(allowed));
    }

    public static boolean isValidToolName(String name) {
        return name != null && !name.isEmpty() && name.length() <= 100 && name.matches("^[a-z_]+$");
    }

    /** Sanitize free-text: strip control chars, truncate, HTML-escape. */
    public static String sanitizeText(String text, int maxLen) {
        if (text == null) return "";
        if (text.length() > maxLen) text = text.substring(0, maxLen);
        return text
            .replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
            .replace("\"", "&quot;").replace("'", "&#39;")
            .replaceAll("[\\x00-\\x08\\x0b\\x0c\\x0e-\\x1f]", "");
    }

    public static String sanitizeForLog(String v) {
        if (v == null) return "<null>";
        return v.replaceAll("[\\r\\n\\t]", " ").substring(0, Math.min(v.length(), 80));
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// JWT VALIDATOR — HMAC-SHA256, 5-min cache, constant-time comparison
// ═══════════════════════════════════════════════════════════════════════════════
class JwtValidator {
    private final String secret;
    private final ConcurrentHashMap<String, CachedResult> cache = new ConcurrentHashMap<>();
    private static final long TTL_MS = 5 * 60 * 1000L;

    JwtValidator(ServerConfig c) { this.secret = c.getJwtSecret(); }

    ValidationResult validate(String token, String requiredRole) {
        if (token == null || token.isBlank()) return ValidationResult.fail("Token is null or empty");
        String cacheKey = hashToken(token);
        CachedResult cached = cache.get(cacheKey);
        if (cached != null && !cached.expired()) {
            if (cached.result.isValid()) {
                long exp = parseLong(cached.result.getClaim("exp"), 0);
                if (exp > 0 && Instant.now().getEpochSecond() > exp) { cache.remove(cacheKey); return ValidationResult.fail("Token expired (cache re-check)"); }
            }
            return cached.result;
        }
        String[] parts = token.split("\\.");
        if (parts.length != 3) return store(cacheKey, ValidationResult.fail("Invalid JWT: expected 3 parts"));
        Map<String, String> header = decodeJson(parts[0]);
        if (!"HS256".equals(header.get("alg"))) return store(cacheKey, ValidationResult.fail("Unsupported alg: " + header.get("alg")));
        String expected = hmac(parts[0] + "." + parts[1], secret);
        if (!constantTimeEq(expected, parts[2])) return store(cacheKey, ValidationResult.fail("Signature invalid"));
        Map<String, String> claims = decodeJson(parts[1]);
        long exp = parseLong(claims.get("exp"), 0);
        if (exp > 0 && Instant.now().getEpochSecond() > exp) return store(cacheKey, ValidationResult.fail("Token expired"));
        String sub = claims.get("sub");
        if (sub == null || sub.isBlank()) return store(cacheKey, ValidationResult.fail("Missing sub claim"));
        if (requiredRole != null && !requiredRole.isBlank()) {
            String roles = claims.getOrDefault("roles", "");
            if (!roles.toUpperCase().contains(requiredRole.toUpperCase()))
                return store(cacheKey, ValidationResult.fail("Role '" + requiredRole + "' required"));
        }
        return store(cacheKey, ValidationResult.success(claims));
    }

    private Map<String, String> decodeJson(String b64) {
        try {
            String pad = b64 + "==".substring(0, (4 - b64.length() % 4) % 4);
            String json = new String(Base64.getUrlDecoder().decode(pad), StandardCharsets.UTF_8);
            Map<String, String> m = new LinkedHashMap<>();
            json = json.replaceAll("^\\{|\\}$", "");
            for (String p : json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")) {
                String[] kv = p.split(":", 2);
                if (kv.length == 2) m.put(kv[0].replaceAll("[\"\\s]",""), kv[1].replaceAll("^[\"\\s]+|[\"\\s]+$",""));
            }
            return m;
        } catch (Exception e) { return Map.of(); }
    }

    private String hmac(String data, String key) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            mac.init(new javax.crypto.spec.SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) { throw new RuntimeException("HMAC failed", e); }
    }

    private boolean constantTimeEq(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) return false;
        int r = 0; for (int i = 0; i < a.length(); i++) r |= a.charAt(i) ^ b.charAt(i); return r == 0;
    }

    private String hashToken(String token) {
        try {
            byte[] h = java.security.MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(h).substring(0, 32);
        } catch (Exception e) { return token.substring(0, Math.min(32, token.length())); }
    }

    private ValidationResult store(String k, ValidationResult r) { cache.put(k, new CachedResult(r, System.currentTimeMillis())); return r; }
    private long parseLong(String s, long d) { try { return s == null ? d : Long.parseLong(s.trim()); } catch (Exception e) { return d; } }

    record CachedResult(ValidationResult result, long ts) { boolean expired() { return System.currentTimeMillis() - ts > TTL_MS; } }

    static final class ValidationResult {
        private final boolean valid; private final String error; private final Map<String, String> claims;
        private ValidationResult(boolean v, String e, Map<String, String> c) { valid = v; error = e; claims = c == null ? Map.of() : Collections.unmodifiableMap(c); }
        static ValidationResult success(Map<String, String> c) { return new ValidationResult(true, null, c); }
        static ValidationResult fail(String e) { return new ValidationResult(false, e, null); }
        boolean isValid() { return valid; }
        String getError() { return error; }
        String getClaim(String k) { return claims.getOrDefault(k, ""); }
        String getClaim(String k, String d) { return claims.getOrDefault(k, d); }
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// RATE LIMITER — token bucket, per-client key
// ═══════════════════════════════════════════════════════════════════════════════
class BucketState {
    final AtomicInteger sec = new AtomicInteger();
    final AtomicInteger min = new AtomicInteger();
    volatile long secBucket = System.currentTimeMillis() / 1000;
    volatile long minBucket = System.currentTimeMillis() / 60_000;
}

public class RateLimiter {
    private final int maxSec, maxMin;
    private final ConcurrentHashMap<String, BucketState> states = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> banned = new ConcurrentHashMap<>();

    public RateLimiter(ServerConfig c) { maxSec = c.getRateLimitPerSec(); maxMin = c.getRateLimitPerMin(); }

    public boolean allow(String key) {
        Long exp = banned.get(key);
        if (exp != null) { if (exp == 0 || System.currentTimeMillis() < exp) return false; banned.remove(key); }
        BucketState s = states.computeIfAbsent(key, k -> new BucketState());
        long ns = System.currentTimeMillis() / 1000, nm = System.currentTimeMillis() / 60_000;
        if (s.secBucket != ns) { s.sec.set(0); s.secBucket = ns; }
        if (s.minBucket != nm) { s.min.set(0); s.minBucket = nm; }
        return s.sec.incrementAndGet() <= maxSec && s.min.incrementAndGet() <= maxMin;
    }

    public void ban(String key, int secs) { banned.put(key, secs == 0 ? 0L : System.currentTimeMillis() + secs * 1000L); }
    public void unban(String key) { banned.remove(key); }
    public boolean isBanned(String key) { Long e = banned.get(key); if (e == null) return false; if (e == 0 || System.currentTimeMillis() < e) return true; banned.remove(key); return false; }
}


// ═══════════════════════════════════════════════════════════════════════════════
// AUDIT LOGGER — structured, SOC 2 / DPDPA 2023 compliant
// ═══════════════════════════════════════════════════════════════════════════════
public class AuditLogger {
    private static final Logger LOG = Logger.getLogger(AuditLogger.class.getName());
    private static final int BUFFER = 2000;
    private final boolean enabled;
    private final ConcurrentLinkedDeque<AuditEvent> buf = new ConcurrentLinkedDeque<>();

    public AuditLogger(ServerConfig c) { enabled = c.isAuditEnabled(); }

    public void info(String event, String actor, String detail)  { log("INFO",  event, actor, detail); }
    public void warn(String event, String actor, String detail)  { log("WARN",  event, actor, detail); }
    public void error(String event, String actor, String detail) { log("ERROR", event, actor, detail); }

    private void log(String level, String event, String actor, String detail) {
        if (!enabled) return;
        String msg = String.format("[AUDIT] %s | %s | actor=%s | %s | ts=%s",
            level, InputSanitizer.sanitizeForLog(event),
            InputSanitizer.sanitizeForLog(actor),
            InputSanitizer.sanitizeForLog(detail), Instant.now());
        System.err.println(msg);
        buf.addLast(new AuditEvent(level, event, actor, detail, Instant.now().toString()));
        while (buf.size() > BUFFER) buf.pollFirst();
    }

    public List<AuditEvent> query(String eventFilter, int limit) {
        List<AuditEvent> r = new ArrayList<>();
        for (AuditEvent e : new ArrayDeque<>(buf)) {
            if ("ALL".equals(eventFilter) || e.eventType().equalsIgnoreCase(eventFilter)) {
                r.add(e); if (r.size() >= limit) break;
            }
        }
        return r;
    }

    public record AuditEvent(String level, String eventType, String actor, String detail, String timestamp) {}
}


// ═══════════════════════════════════════════════════════════════════════════════
// REQUEST VALIDATOR — payload size + null byte check
// ═══════════════════════════════════════════════════════════════════════════════
public class RequestValidator {
    private static final int MAX_BYTES = 65536;

    public boolean isValidToolName(String n) { return InputSanitizer.isValidToolName(n); }

    public List<String> validate(String tool, JsonNode args) {
        List<String> errors = new ArrayList<>();
        if (args == null) return errors;
        String raw = args.toString();
        if (raw.length() > MAX_BYTES) errors.add("Payload (" + raw.length() + " bytes) exceeds 64KB limit");
        if (raw.contains("\u0000")) errors.add("Payload contains null bytes — potential injection");
        return errors;
    }
}
