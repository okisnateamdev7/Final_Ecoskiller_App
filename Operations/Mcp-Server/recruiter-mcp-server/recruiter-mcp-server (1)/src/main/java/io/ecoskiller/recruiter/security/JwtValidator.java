package io.ecoskiller.recruiter.security;

import io.ecoskiller.recruiter.config.ServerConfig;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JWT validator for Keycloak-issued tokens (HMAC-SHA256 / HS256).
 *
 * Claims expected:
 *   sub          — recruiter_id
 *   roles        — ["RECRUITER"] or ["ADMIN"]
 *   company_id   — tenant company
 *   tenant_id    — multi-tenant isolation key
 *   exp          — expiration epoch seconds
 *
 * Security:
 *   - Constant-time HMAC comparison (prevents timing attacks)
 *   - Full token never logged; only SHA-256 hash prefix used as cache key
 *   - 5-minute TTL cache keyed by token hash
 *   - Expiration re-checked even on cache hit
 */
public final class JwtValidator {

    private final String jwtSecret;
    private final ConcurrentHashMap<String, Cached> cache = new ConcurrentHashMap<>();
    private static final long CACHE_TTL_MS = 5 * 60 * 1000L;

    public JwtValidator(ServerConfig config) {
        this.jwtSecret = config.getJwtSecret();
    }

    public ValidationResult validate(String token, String expectedRole) {
        if (token == null || token.isBlank())
            return ValidationResult.fail("Token is null or empty");

        String cacheKey = hashToken(token);
        Cached cached = cache.get(cacheKey);
        if (cached != null && !cached.isExpired()) {
            if (cached.result.isValid()) {
                long exp = parseLong(cached.result.getClaim("exp"), 0);
                if (exp > 0 && Instant.now().getEpochSecond() > exp) {
                    cache.remove(cacheKey);
                    return ValidationResult.fail("Token expired (cache re-check)");
                }
            }
            return cached.result;
        }

        String[] parts = token.split("\\.");
        if (parts.length != 3)
            return store(cacheKey, ValidationResult.fail("Invalid JWT: expected 3 parts"));

        // Verify algorithm
        Map<String, String> header = decodeBase64Json(parts[0]);
        if (!"HS256".equals(header.get("alg")))
            return store(cacheKey, ValidationResult.fail("Unsupported alg: " + header.get("alg")));

        // Verify signature (constant-time)
        String sigInput = parts[0] + "." + parts[1];
        String expected = hmacSha256Base64Url(sigInput, jwtSecret);
        if (!constantTimeEquals(expected, parts[2]))
            return store(cacheKey, ValidationResult.fail("Signature invalid"));

        Map<String, String> claims = decodeBase64Json(parts[1]);

        // Check expiry
        long exp = parseLong(claims.get("exp"), 0);
        if (exp > 0 && Instant.now().getEpochSecond() > exp)
            return store(cacheKey, ValidationResult.fail("Token expired"));

        // Check required claims
        String sub = claims.get("sub");
        if (sub == null || sub.isBlank())
            return store(cacheKey, ValidationResult.fail("Missing sub (recruiter_id) claim"));

        // Validate recruiter ID format from claim
        try { InputSanitizer.validateRecruiterId(sub); }
        catch (SecurityException e) { return store(cacheKey, ValidationResult.fail("Invalid sub claim format")); }

        // Role check if requested
        if (expectedRole != null && !expectedRole.isBlank()) {
            String roles = claims.getOrDefault("roles", "");
            if (!roles.toUpperCase().contains(expectedRole.toUpperCase()))
                return store(cacheKey, ValidationResult.fail(
                    "Required role '" + expectedRole + "' not found in token"));
        }

        return store(cacheKey, ValidationResult.success(claims));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Map<String, String> decodeBase64Json(String b64url) {
        try {
            String padded = b64url + "==".substring(0, (4 - b64url.length() % 4) % 4);
            String json = new String(Base64.getUrlDecoder().decode(padded), StandardCharsets.UTF_8);
            Map<String, String> map = new LinkedHashMap<>();
            // Simple flat JSON parser for JWT claims
            json = json.replaceAll("^\\{|\\}$", "");
            for (String pair : json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")) {
                String[] kv = pair.split(":", 2);
                if (kv.length == 2) {
                    String k = kv[0].replaceAll("[\"\\s]", "");
                    String v = kv[1].replaceAll("^[\"\\s]+|[\"\\s]+$", "");
                    map.put(k, v);
                }
            }
            return map;
        } catch (Exception e) { return Collections.emptyMap(); }
    }

    private String hmacSha256Base64Url(String data, String secret) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            mac.init(new javax.crypto.spec.SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) { throw new RuntimeException("HMAC failed", e); }
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) return false;
        int r = 0;
        for (int i = 0; i < a.length(); i++) r |= a.charAt(i) ^ b.charAt(i);
        return r == 0;
    }

    private String hashToken(String token) {
        try {
            java.security.MessageDigest d = java.security.MessageDigest.getInstance("SHA-256");
            byte[] h = d.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(h).substring(0, 32);
        } catch (Exception e) { return token.substring(0, Math.min(token.length(), 32)); }
    }

    private ValidationResult store(String key, ValidationResult r) {
        cache.put(key, new Cached(r, System.currentTimeMillis()));
        return r;
    }

    private long parseLong(String s, long def) {
        try { return s == null ? def : Long.parseLong(s.trim()); }
        catch (NumberFormatException e) { return def; }
    }

    // ── Record types ─────────────────────────────────────────────────────────

    private record Cached(ValidationResult result, long ts) {
        boolean isExpired() { return System.currentTimeMillis() - ts > CACHE_TTL_MS; }
    }

    public static final class ValidationResult {
        private final boolean valid;
        private final String error;
        private final Map<String, String> claims;

        private ValidationResult(boolean valid, String error, Map<String, String> claims) {
            this.valid = valid; this.error = error;
            this.claims = claims == null ? Map.of() : Collections.unmodifiableMap(claims);
        }
        public static ValidationResult success(Map<String, String> claims) { return new ValidationResult(true, null, claims); }
        public static ValidationResult fail(String error) { return new ValidationResult(false, error, null); }
        public boolean isValid() { return valid; }
        public String getError() { return error; }
        public String getClaim(String k) { return claims.getOrDefault(k, ""); }
        public String getClaim(String k, String def) { return claims.getOrDefault(k, def); }
    }
}
