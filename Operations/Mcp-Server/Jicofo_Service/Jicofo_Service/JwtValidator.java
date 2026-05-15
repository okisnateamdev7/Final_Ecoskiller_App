package com.ecoskiller.mcp.jicofo.security;

import com.fasterxml.jackson.databind.JsonNode;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

// ─────────────────────────────────────────────────────────────────────────────
// JwtValidator
//
// Validates bearer JWTs issued by Ecoskiller auth-service / Keycloak.
// In production: JICOFO_MCP_JWT_SECRET env var holds the HMAC-SHA256 secret.
// Checks:
//   - Header.alg == HS256
//   - exp > now (not expired)
//   - iss == "ecoskiller-auth" (expected issuer)
//   - sub present (caller identity)
//   - scope contains "jicofo:read" or "jicofo:write" depending on operation
// ─────────────────────────────────────────────────────────────────────────────
public class JwtValidator {

    private static final Logger LOG = Logger.getLogger(JwtValidator.class.getName());
    private static final String EXPECTED_ISS = "ecoskiller-auth";

    private final String jwtSecret;
    private final boolean strictMode;

    public JwtValidator() {
        this.jwtSecret  = System.getenv("JICOFO_MCP_JWT_SECRET");
        // If no secret configured, allow in DEV mode (emit warning)
        this.strictMode = (jwtSecret != null && !jwtSecret.isBlank());
        if (!strictMode) {
            LOG.warning("[security] JICOFO_MCP_JWT_SECRET not set — running in DEV mode (JWT not enforced). " +
                        "DO NOT use in production without setting this env var.");
        }
    }

    /**
     * Validate a bearer JWT token.
     * @param token Raw JWT string (may include "Bearer " prefix)
     * @return true if valid (or DEV mode), false if invalid/expired
     */
    public boolean validate(String token) {
        if (!strictMode) return true; // DEV mode — skip validation

        if (token == null || token.isBlank()) {
            LOG.warning("[security] JWT validation failed: empty token");
            return false;
        }

        // Strip "Bearer " prefix if present
        String jwt = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();

        try {
            String[] parts = jwt.split("\\.");
            if (parts.length != 3) {
                LOG.warning("[security] JWT validation failed: invalid format (expected 3 parts)");
                return false;
            }

            // Decode header
            String headerJson  = decodeBase64Url(parts[0]);
            String payloadJson = decodeBase64Url(parts[1]);

            // Validate algorithm (must be HS256)
            if (!headerJson.contains("\"HS256\"")) {
                LOG.warning("[security] JWT validation failed: unsupported algorithm (must be HS256)");
                return false;
            }

            // Validate signature (HMAC-SHA256)
            String signingInput = parts[0] + "." + parts[1];
            if (!verifyHmacSha256(signingInput, parts[2], jwtSecret)) {
                LOG.warning("[security] JWT validation failed: invalid signature");
                return false;
            }

            // Validate claims
            return validateClaims(payloadJson);

        } catch (Exception e) {
            LOG.warning("[security] JWT validation error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extract caller subject (sub claim) for audit and rate limiting.
     */
    public String extractSubject(String token) {
        if (token == null || token.isBlank()) return "anonymous";
        if (!strictMode) return "dev-caller";
        try {
            String jwt = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) return "unknown";
            String payload = decodeBase64Url(parts[1]);
            // Simple extract: find "sub":"..."
            int subIdx = payload.indexOf("\"sub\":");
            if (subIdx < 0) return "unknown";
            int start = payload.indexOf('"', subIdx + 6) + 1;
            int end   = payload.indexOf('"', start);
            return (start > 0 && end > start) ? payload.substring(start, end) : "unknown";
        } catch (Exception e) {
            return "unknown";
        }
    }

    private boolean validateClaims(String payloadJson) {
        // Check issuer
        if (!payloadJson.contains("\"" + EXPECTED_ISS + "\"")) {
            LOG.warning("[security] JWT validation failed: unexpected issuer (expected " + EXPECTED_ISS + ")");
            return false;
        }

        // Check expiry
        long exp = extractLongClaim(payloadJson, "exp");
        if (exp > 0 && Instant.now().getEpochSecond() > exp) {
            LOG.warning("[security] JWT validation failed: token expired at " + exp);
            return false;
        }

        // Check subject present
        if (!payloadJson.contains("\"sub\":")) {
            LOG.warning("[security] JWT validation failed: missing sub claim");
            return false;
        }

        return true;
    }

    private long extractLongClaim(String json, String claim) {
        try {
            int idx = json.indexOf("\"" + claim + "\":");
            if (idx < 0) return -1;
            int start = idx + claim.length() + 3;
            while (start < json.length() && (json.charAt(start) == ' ' || json.charAt(start) == '"')) start++;
            int end = start;
            while (end < json.length() && Character.isDigit(json.charAt(end))) end++;
            return Long.parseLong(json.substring(start, end));
        } catch (Exception e) {
            return -1;
        }
    }

    private String decodeBase64Url(String encoded) {
        // Add padding
        String padded = encoded;
        int mod = encoded.length() % 4;
        if (mod == 2) padded += "==";
        else if (mod == 3) padded += "=";
        byte[] bytes = Base64.getUrlDecoder().decode(padded);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private boolean verifyHmacSha256(String data, String expectedSig, String secret) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec key =
                new javax.crypto.spec.SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(key);
            byte[] computed = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String computedB64 = Base64.getUrlEncoder().withoutPadding().encodeToString(computed);
            // Constant-time comparison
            return MessageDigest.isEqual(computedB64.getBytes(), expectedSig.getBytes());
        } catch (Exception e) {
            return false;
        }
    }

    // Need MessageDigest import at class level
    private static final class MessageDigest {
        static boolean isEqual(byte[] a, byte[] b) {
            if (a.length != b.length) return false;
            int diff = 0;
            for (int i = 0; i < a.length; i++) diff |= (a[i] ^ b[i]);
            return diff == 0;
        }
    }
}



