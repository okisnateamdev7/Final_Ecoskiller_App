package com.ecoskiller.trivy.mcp.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * JWT Validator — Keycloak RS256 token verification.
 *
 * Token source priority (first non-null wins):
 *   1. params._meta.auth.token       (MCP convention)
 *   2. params.clientInfo.token       (Claude Desktop fallback)
 *   3. params.arguments._bearer      (per-call bearer)
 *
 * Production path:
 *   Uncomment the jose4j dependency in pom.xml and replace
 *   verifyStructure() with full JWKS RS256 verification.
 *   Clearly marked with TODO below.
 *
 * Dev path:
 *   Set env var TRIVY_JWT_BYPASS=true to skip validation entirely.
 *   THIS MUST NEVER BE SET IN PRODUCTION.
 */
public class JwtValidator {

    private static final Logger LOG = Logger.getLogger(JwtValidator.class.getName());

    private static final String KEYCLOAK_ISSUER =
            System.getenv().getOrDefault(
                    "KEYCLOAK_ISSUER",
                    "https://auth.ecoskiller.com/realms/ecoskiller");

    /** Set TRIVY_JWT_BYPASS=true in dev/test only. Never in production. */
    private static final boolean BYPASS =
            "true".equalsIgnoreCase(System.getenv("TRIVY_JWT_BYPASS"));

    private final ObjectMapper mapper = new ObjectMapper();

    public boolean validate(JsonNode req) {
        if (BYPASS) {
            LOG.warning("[SECURITY] TRIVY_JWT_BYPASS=true — JWT validation DISABLED (dev only!)");
            return true;
        }
        String token = extractToken(req);
        if (token == null || token.isBlank()) {
            LOG.warning("[SECURITY] No JWT token in request");
            return false;
        }
        try {
            return verifyStructure(token);
        } catch (Exception e) {
            LOG.warning("[SECURITY] JWT verification failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extract the highest-priority role from JWT realm_access.roles.
     * Priority: admin > security_engineer > devops_engineer > developer > viewer
     */
    public String extractRole(JsonNode req) {
        if (BYPASS) return "security_engineer"; // safe dev default
        String token = extractToken(req);
        if (token == null) return "anonymous";
        try {
            JsonNode payload = decodePayload(token);
            JsonNode roles   = payload.path("realm_access").path("roles");
            if (roles.isArray()) {
                for (String r : new String[]{"admin", "security_engineer", "devops_engineer", "developer", "viewer"}) {
                    for (JsonNode role : roles) {
                        if (r.equals(role.asText())) return r;
                    }
                }
            }
        } catch (Exception ignored) {}
        return "anonymous";
    }

    // ── Private ────────────────────────────────────────────────────────────────

    private String extractToken(JsonNode req) {
        JsonNode m = req.path("params").path("_meta").path("auth").path("token");
        if (!m.isMissingNode() && !m.isNull()) return m.asText();
        JsonNode ci = req.path("params").path("clientInfo").path("token");
        if (!ci.isMissingNode() && !ci.isNull()) return ci.asText();
        JsonNode b = req.path("params").path("arguments").path("_bearer");
        if (!b.isMissingNode() && !b.isNull()) return b.asText();
        return null;
    }

    /**
     * Production TODO: Replace this stub with real JWKS RS256 verification:
     *
     *   HttpsJwks httpsJkws = new HttpsJwks(KEYCLOAK_ISSUER + "/protocol/openid-connect/certs");
     *   HttpsJwksVerificationKeyResolver resolver = new HttpsJwksVerificationKeyResolver(httpsJkws);
     *   JwtConsumer consumer = new JwtConsumerBuilder()
     *       .setRequireExpirationTime()
     *       .setAllowedClockSkewInSeconds(30)
     *       .setRequireSubject()
     *       .setExpectedIssuer(KEYCLOAK_ISSUER)
     *       .setVerificationKeyResolver(resolver)
     *       .build();
     *   consumer.processToClaims(token);  // throws on any failure
     *   return true;
     */
    private boolean verifyStructure(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new IllegalArgumentException("Not a JWT — expected 3 parts");

        JsonNode payload = decodePayload(token);

        // Expiry check
        long exp = payload.path("exp").asLong(0);
        if (exp > 0 && exp < System.currentTimeMillis() / 1000L) {
            throw new SecurityException("Token expired at epoch " + exp);
        }
        // Issuer check
        String iss = payload.path("iss").asText("");
        if (!BYPASS && !KEYCLOAK_ISSUER.equals(iss)) {
            throw new SecurityException("Invalid issuer: " + iss + " (expected " + KEYCLOAK_ISSUER + ")");
        }

        LOG.fine("[SECURITY] JWT structure + expiry OK. Signature: STUB — replace with JWKS RS256");
        return true;
    }

    private JsonNode decodePayload(String token) throws Exception {
        String[] parts = token.split("\\.");
        byte[] bytes   = Base64.getUrlDecoder().decode(addPadding(parts[1]));
        return mapper.readTree(new String(bytes, StandardCharsets.UTF_8));
    }

    private String addPadding(String b64) {
        return b64 + "==".substring(0, (4 - b64.length() % 4) % 4);
    }
}
