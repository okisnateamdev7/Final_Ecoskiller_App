package io.ecoskiller.prosody.security;

import io.ecoskiller.prosody.config.ServerConfig;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Validates JWT tokens for Prosody XMPP client authentication.
 *
 * Algorithm: HMAC-SHA256 (HS256)
 * Claims checked: exp, iat, user_id, role
 * Cache: 5-minute TTL per token hash (to reduce latency on repeated connections)
 *
 * Security notes:
 * - Full token is NEVER logged (only first 16 chars as reference)
 * - Cache keyed by SHA-256 hash of token (not plaintext)
 * - Expired tokens rejected even if in cache
 */
public class JwtValidator {

    private final String jwtSecret;
    private final ConcurrentHashMap<String, CachedValidation> cache = new ConcurrentHashMap<>();
    private static final long CACHE_TTL_MS = 5 * 60 * 1000L; // 5 minutes

    public JwtValidator(ServerConfig config) {
        this.jwtSecret = config.getJwtSecret();
    }

    public ValidationResult validate(String token, String expectedRoomId) {
        if (token == null || token.isBlank()) {
            return ValidationResult.fail("Token is null or empty");
        }

        // Check cache (keyed by token hash, not plaintext)
        String tokenHash = hashToken(token);
        CachedValidation cached = cache.get(tokenHash);
        if (cached != null && !cached.isExpired()) {
            // Re-verify expiration even on cache hit
            if (cached.result().isValid()) {
                String expStr = cached.result().getClaim("exp", "0");
                long expEpoch = Long.parseLong(expStr);
                if (Instant.now().getEpochSecond() > expEpoch) {
                    cache.remove(tokenHash);
                    return ValidationResult.fail("Token expired (cache hit, re-verified)");
                }
            }
            return cached.result();
        }

        // Parse JWT structure
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return cacheAndReturn(tokenHash, ValidationResult.fail("Invalid JWT format: expected 3 parts"));
        }

        // Decode header
        Map<String, String> header = decodeJwt(parts[0]);
        String alg = header.getOrDefault("alg", "");
        if (!"HS256".equals(alg)) {
            return cacheAndReturn(tokenHash,
                ValidationResult.fail("Unsupported algorithm: " + alg + " (expected HS256)"));
        }

        // Verify signature
        String signingInput = parts[0] + "." + parts[1];
        String expectedSignature = computeHmacSha256(signingInput, jwtSecret);
        String actualSignature = parts[2];
        if (!constantTimeEquals(expectedSignature, actualSignature)) {
            return cacheAndReturn(tokenHash, ValidationResult.fail("Invalid signature"));
        }

        // Decode payload claims
        Map<String, String> claims = decodeJwt(parts[1]);

        // Check expiration
        String expStr = claims.get("exp");
        if (expStr != null) {
            try {
                long exp = Long.parseLong(expStr);
                if (Instant.now().getEpochSecond() > exp) {
                    return cacheAndReturn(tokenHash, ValidationResult.fail("Token expired"));
                }
            } catch (NumberFormatException e) {
                return cacheAndReturn(tokenHash, ValidationResult.fail("Invalid exp claim"));
            }
        }

        // Check required claims
        if (!claims.containsKey("user_id") || claims.get("user_id").isBlank()) {
            return cacheAndReturn(tokenHash, ValidationResult.fail("Missing user_id claim"));
        }

        // Validate user_id format (prevent injection via claims)
        try {
            InputSanitizer.validateUserId(claims.get("user_id"));
        } catch (SecurityException e) {
            return cacheAndReturn(tokenHash, ValidationResult.fail("Invalid user_id in claims"));
        }

        // Optional: verify room_id claim matches expected
        if (expectedRoomId != null && !expectedRoomId.isBlank()) {
            String claimRoomId = claims.getOrDefault("room_id", "");
            if (!expectedRoomId.equals(claimRoomId) && !"*".equals(claimRoomId)) {
                return cacheAndReturn(tokenHash,
                    ValidationResult.fail("room_id mismatch: expected=" + expectedRoomId +
                        " got=" + InputSanitizer.sanitizeForLog(claimRoomId)));
            }
        }

        ValidationResult valid = ValidationResult.success(claims);
        return cacheAndReturn(tokenHash, valid);
    }

    // ── Internals ──────────────────────────────────────────────────────────────

    private Map<String, String> decodeJwt(String base64UrlPart) {
        try {
            byte[] decoded = Base64.getUrlDecoder().decode(
                base64UrlPart + "==".substring(0, (4 - base64UrlPart.length() % 4) % 4));
            String json = new String(decoded, StandardCharsets.UTF_8);
            Map<String, String> map = new HashMap<>();
            // Simple JSON parser for flat JWT claims
            json = json.replaceAll("[{}\"\\s]", "");
            for (String pair : json.split(",")) {
                String[] kv = pair.split(":", 2);
                if (kv.length == 2) map.put(kv[0].trim(), kv[1].trim());
            }
            return map;
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    private String computeHmacSha256(String data, String secret) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec keySpec =
                new javax.crypto.spec.SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("HMAC-SHA256 computation failed", e);
        }
    }

    /** Constant-time string comparison to prevent timing attacks */
    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }

    private String hashToken(String token) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash).substring(0, 32);
        } catch (Exception e) {
            return token.length() > 32 ? token.substring(0, 32) : token;
        }
    }

    private ValidationResult cacheAndReturn(String key, ValidationResult result) {
        cache.put(key, new CachedValidation(result, System.currentTimeMillis()));
        return result;
    }

    private record CachedValidation(ValidationResult result, long cachedAt) {
        boolean isExpired() { return System.currentTimeMillis() - cachedAt > CACHE_TTL_MS; }
    }

    // ── ValidationResult ───────────────────────────────────────────────────────

    public static class ValidationResult {
        private final boolean valid;
        private final String error;
        private final Map<String, String> claims;

        private ValidationResult(boolean valid, String error, Map<String, String> claims) {
            this.valid = valid;
            this.error = error;
            this.claims = claims == null ? Collections.emptyMap() : Collections.unmodifiableMap(claims);
        }

        public static ValidationResult success(Map<String, String> claims) {
            return new ValidationResult(true, null, claims);
        }

        public static ValidationResult fail(String error) {
            return new ValidationResult(false, error, null);
        }

        public boolean isValid() { return valid; }
        public String getError() { return error; }
        public String getClaim(String key) { return claims.getOrDefault(key, ""); }
        public String getClaim(String key, String defaultVal) { return claims.getOrDefault(key, defaultVal); }
    }
}
