package com.ecoskiller.tie.mcp.security;

import com.fasterxml.jackson.databind.JsonNode;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * JWT Validator — Keycloak RS256 token verification.
 *
 * In production: replace stub with full JWKS-backed RS256 verification
 * using the jose4j or nimbus-jose-jwt library. For local dev / testing,
 * the ENV var TIE_JWT_BYPASS=true disables validation (never in prod).
 *
 * Token source priority:
 *   1. params._meta.auth.token  (MCP convention)
 *   2. params.clientInfo.token  (fallback)
 *   3. ENV TIE_JWT_BYPASS       (dev only — logs a loud warning)
 */
public class JwtValidator {

    private static final Logger LOG = Logger.getLogger(JwtValidator.class.getName());

    // In production, load from ENV: KEYCLOAK_JWKS_URI
    private static final String KEYCLOAK_ISSUER =
            System.getenv().getOrDefault("KEYCLOAK_ISSUER", "https://auth.ecoskiller.com/realms/ecoskiller");

    private static final boolean BYPASS =
            "true".equalsIgnoreCase(System.getenv("TIE_JWT_BYPASS"));

    public boolean validate(JsonNode req) {
        if (BYPASS) {
            LOG.warning("[SECURITY] TIE_JWT_BYPASS=true — JWT validation DISABLED (dev mode only)");
            return true;
        }

        String token = extractToken(req);
        if (token == null || token.isBlank()) {
            LOG.warning("[SECURITY] No JWT token found in request");
            return false;
        }

        try {
            return verifyToken(token);
        } catch (Exception e) {
            LOG.warning("[SECURITY] JWT verification failed: " + e.getMessage());
            return false;
        }
    }

    public String extractRole(JsonNode req) {
        if (BYPASS) return "recruiter"; // default dev role
        String token = extractToken(req);
        if (token == null) return "anonymous";
        try {
            JsonNode payload = decodePayload(token);
            // Keycloak realm_access.roles
            JsonNode roles = payload.path("realm_access").path("roles");
            if (roles.isArray()) {
                // Priority: admin > ml_engineer > recruiter > viewer
                for (String r : new String[]{"admin", "ml_engineer", "recruiter", "viewer"}) {
                    for (JsonNode role : roles) {
                        if (r.equals(role.asText())) return r;
                    }
                }
            }
        } catch (Exception ignored) {}
        return "anonymous";
    }

    // ── Private ──────────────────────────────────────────────────────────────

    private String extractToken(JsonNode req) {
        // 1. MCP meta auth
        JsonNode meta = req.path("params").path("_meta").path("auth").path("token");
        if (!meta.isMissingNode() && !meta.isNull()) return meta.asText();
        // 2. clientInfo token
        JsonNode ci = req.path("params").path("clientInfo").path("token");
        if (!ci.isMissingNode() && !ci.isNull()) return ci.asText();
        // 3. Arguments-level bearer (tools/call)
        JsonNode bearer = req.path("params").path("arguments").path("_bearer");
        if (!bearer.isMissingNode() && !bearer.isNull()) return bearer.asText();
        return null;
    }

    /**
     * Production implementation stub.
     * TODO: replace with JWKS RS256 verification using jose4j:
     *   JwksVerificationKeyResolver resolver = new JwksVerificationKeyResolver(jwks);
     *   JwtConsumer consumer = new JwtConsumerBuilder()
     *       .setRequireExpirationTime()
     *       .setAllowedClockSkewInSeconds(30)
     *       .setRequireSubject()
     *       .setExpectedIssuer(KEYCLOAK_ISSUER)
     *       .setVerificationKeyResolver(resolver)
     *       .build();
     *   consumer.processToClaims(token);
     */
    private boolean verifyToken(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new IllegalArgumentException("Not a JWT (expected 3 parts)");

        JsonNode payload = decodePayload(token);

        // Check expiration
        long exp = payload.path("exp").asLong(0);
        if (exp > 0 && exp < System.currentTimeMillis() / 1000L) {
            throw new SecurityException("Token expired at " + exp);
        }

        // Check issuer
        String iss = payload.path("iss").asText("");
        if (!BYPASS && !KEYCLOAK_ISSUER.equals(iss)) {
            throw new SecurityException("Invalid issuer: " + iss);
        }

        // Signature verification — MUST be replaced with real crypto in prod
        // Using jose4j / nimbus-jose-jwt recommended
        LOG.fine("[SECURITY] JWT structure+expiry valid. Signature: STUB — replace with JWKS");
        return true;
    }

    private JsonNode decodePayload(String token) throws Exception {
        String[] parts = token.split("\\.");
        byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
        String payloadJson  = new String(payloadBytes, StandardCharsets.UTF_8);
        return new com.fasterxml.jackson.databind.ObjectMapper().readTree(payloadJson);
    }
}
