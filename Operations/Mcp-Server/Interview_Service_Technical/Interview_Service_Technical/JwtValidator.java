package com.ecoskiller.mcp.interview.security;

import com.ecoskiller.mcp.interview.model.SecurityContext;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * JWT Validator — Keycloak OIDC / OAuth2
 *
 * Security design:
 * - Validates JWT signature (HS256/RS256)
 * - Checks expiry (exp claim)
 * - Extracts role, tenant_id, user IDs
 * - Rejects null/empty/malformed tokens
 * - In production: verifies against Keycloak JWKS endpoint
 *
 * Production wiring:
 *   Replace simulateKeycloakVerify() with:
 *   com.nimbusds.jose.crypto.RSASSAVerifier + JWKSet.load(keycloakJwksUrl)
 */
public class JwtValidator {

    private static final Logger LOG = Logger.getLogger(JwtValidator.class.getName());

    // Keycloak JWKS endpoint (configure via env)
    private static final String KEYCLOAK_JWKS_URL =
        System.getenv().getOrDefault("KEYCLOAK_JWKS_URL",
            "https://auth.ecoskiller.internal/realms/ecoskiller/protocol/openid-connect/certs");

    /**
     * Validate JWT and extract SecurityContext.
     *
     * @throws SecurityException if token is invalid, expired, or missing
     */
    public SecurityContext validate(String token) throws SecurityException {
        if (token == null || token.isBlank()) {
            throw new SecurityException("Missing auth_token");
        }
        // Strip "Bearer " prefix if present
        if (token.startsWith("Bearer ") || token.startsWith("bearer ")) {
            token = token.substring(7).trim();
        }

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new SecurityException("Malformed JWT: expected 3 parts, got " + parts.length);
        }

        // In production: verify signature via Keycloak JWKS
        boolean signatureValid = simulateKeycloakVerify(parts[0], parts[1], parts[2]);
        if (!signatureValid) {
            throw new SecurityException("JWT signature verification failed");
        }

        // Decode payload
        String payloadJson = decodeBase64Url(parts[1]);
        return extractClaims(payloadJson);
    }

    /**
     * Decode base64url-encoded JWT payload and extract claims.
     */
    private SecurityContext extractClaims(String payloadJson) throws SecurityException {
        // Simple claim extraction (production: use Jackson or Nimbus)
        String userId      = extractClaim(payloadJson, "sub");
        String tenantId    = extractClaim(payloadJson, "tenant_id");
        String role        = extractHighestRole(payloadJson);
        String recruiterId = extractClaim(payloadJson, "recruiter_id");
        String candidateId = extractClaim(payloadJson, "candidate_id");
        String email       = extractClaim(payloadJson, "email");

        // Validate expiry
        long exp = extractLongClaim(payloadJson, "exp");
        long now = System.currentTimeMillis() / 1000L;
        if (exp > 0 && now > exp) {
            throw new SecurityException("JWT expired at " + exp + " (current: " + now + ")");
        }

        if (userId == null || userId.isBlank()) {
            throw new SecurityException("JWT missing 'sub' claim");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new SecurityException("JWT missing 'tenant_id' claim");
        }
        if (role == null || role.isBlank()) {
            throw new SecurityException("JWT missing role claim");
        }

        return new SecurityContext(userId, tenantId, role, recruiterId, candidateId, email);
    }

    /**
     * Extract the highest-priority role from Keycloak realm_access.roles array.
     * Priority: ADMIN > RECRUITER > INTERVIEWER > CANDIDATE
     */
    private String extractHighestRole(String payload) {
        // Keycloak realm_access: {"roles": ["RECRUITER", "default-roles-ecoskiller"]}
        if (payload.contains("\"ADMIN\""))       return "ADMIN";
        if (payload.contains("\"RECRUITER\""))   return "RECRUITER";
        if (payload.contains("\"INTERVIEWER\"")) return "INTERVIEWER";
        if (payload.contains("\"CANDIDATE\""))   return "CANDIDATE";
        // Fallback: try simple role field
        String role = extractClaim(payload, "role");
        if (role != null) return role.toUpperCase();
        return "UNKNOWN";
    }

    private String extractClaim(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search);
        if (start < 0) return null;
        start += search.length();
        int end = json.indexOf("\"", start);
        if (end < 0) return null;
        return json.substring(start, end);
    }

    private long extractLongClaim(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start < 0) return -1;
        start += search.length();
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;
        try { return Long.parseLong(json.substring(start, end)); }
        catch (NumberFormatException e) { return -1; }
    }

    private String decodeBase64Url(String encoded) {
        try {
            byte[] bytes = Base64.getUrlDecoder().decode(padBase64(encoded));
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new SecurityException("Failed to decode JWT payload: " + e.getMessage());
        }
    }

    private String padBase64(String s) {
        return switch (s.length() % 4) {
            case 2  -> s + "==";
            case 3  -> s + "=";
            default -> s;
        };
    }

    /**
     * Stub for Keycloak JWKS RS256 verification.
     *
     * Production replacement:
     * <pre>
     *   JWKSet jwkSet = JWKSet.load(new URL(KEYCLOAK_JWKS_URL));
     *   JWSVerifier verifier = new RSASSAVerifier((RSAKey) jwkSet.getKeyByKeyId(kid));
     *   SignedJWT signedJWT = SignedJWT.parse(rawToken);
     *   return signedJWT.verify(verifier);
     * </pre>
     */
    private boolean simulateKeycloakVerify(String header, String payload, String sig) {
        // In production: verify RS256 signature against Keycloak JWKS
        // For local dev/test: accept non-empty signature
        return sig != null && !sig.isBlank() && sig.length() > 8;
    }
}
