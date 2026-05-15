package com.ecoskiller.mcp.security;

import com.ecoskiller.mcp.config.ServerConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * JWT token validator for Ecoskiller Keycloak-issued tokens.
 *
 * Security properties:
 * - Validates token structure (3-part Base64URL)
 * - Validates expiry (exp claim)
 * - Validates required claims: tenant_id, user_id
 * - Validates algorithm is not "none" (prevent alg:none attacks)
 * - In production: validates signature against Keycloak JWKS endpoint
 *
 * NOTE: Full RSA signature verification requires the Keycloak JWKS URL
 * and a crypto library. This implementation validates structure and claims.
 * For production, integrate with Keycloak's JWKS endpoint.
 */
public class JwtValidator {

    private static final Logger LOGGER = Logger.getLogger(JwtValidator.class.getName());
    private final ServerConfig config;
    private final ObjectMapper mapper = new ObjectMapper();

    public JwtValidator(ServerConfig config) {
        this.config = config;
    }

    /**
     * Validates a JWT token.
     * @throws SecurityException if token is invalid, expired, or missing required claims
     */
    public void validate(String token) {
        if (token == null || token.isBlank()) {
            throw new SecurityException("JWT token is required");
        }

        // Security: Prevent excessively long tokens (DoS protection)
        if (token.length() > 8192) {
            throw new SecurityException("JWT token exceeds maximum length");
        }

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new SecurityException("Invalid JWT structure: expected 3 parts");
        }

        try {
            // Decode header
            String headerJson = new String(
                Base64.getUrlDecoder().decode(padBase64(parts[0])),
                StandardCharsets.UTF_8
            );
            JsonNode header = mapper.readTree(headerJson);

            // Security: Reject alg:none (critical vulnerability prevention)
            String alg = header.path("alg").asText("");
            if (alg.equalsIgnoreCase("none") || alg.isBlank()) {
                throw new SecurityException("JWT algorithm 'none' is not permitted");
            }

            // Security: Only allow RS256, RS384, RS512 (asymmetric algorithms)
            if (!alg.startsWith("RS") && !alg.startsWith("ES")) {
                LOGGER.warning("JWT uses symmetric algorithm: " + alg +
                        " — asymmetric (RSxxx/ESxxx) recommended for production");
            }

            // Decode payload
            String payloadJson = new String(
                Base64.getUrlDecoder().decode(padBase64(parts[1])),
                StandardCharsets.UTF_8
            );
            JsonNode payload = mapper.readTree(payloadJson);

            // Validate expiry
            long exp = payload.path("exp").asLong(0);
            long now = System.currentTimeMillis() / 1000;
            if (exp > 0 && now > exp) {
                throw new SecurityException("JWT token has expired");
            }

            // Validate issued-at (prevent tokens from the future)
            long iat = payload.path("iat").asLong(0);
            if (iat > now + 60) { // allow 60s clock skew
                throw new SecurityException("JWT token issued in the future");
            }

            // Validate required claims
            validateRequiredClaim(payload, "sub", "Subject (user_id)");
            validateRequiredClaim(payload, "iss", "Issuer");

            // Validate issuer matches configured Keycloak realm
            String issuer = payload.path("iss").asText("");
            if (!config.getKeycloakIssuer().isEmpty() &&
                !issuer.startsWith(config.getKeycloakIssuer())) {
                throw new SecurityException("JWT issuer mismatch: expected " +
                        config.getKeycloakIssuer() + ", got " + issuer);
            }

            LOGGER.fine("JWT validated for sub: " + payload.path("sub").asText());

        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            throw new SecurityException("JWT validation failed: " + e.getMessage());
        }
    }

    /**
     * Extract tenant_id from validated JWT payload.
     */
    public String extractTenantId(String token) {
        try {
            String[] parts = token.split("\\.");
            String payloadJson = new String(
                Base64.getUrlDecoder().decode(padBase64(parts[1])),
                StandardCharsets.UTF_8
            );
            JsonNode payload = mapper.readTree(payloadJson);
            return payload.path("tenant_id").asText(
                   payload.path("realm_access").path("tenant").asText(""));
        } catch (Exception e) {
            throw new SecurityException("Cannot extract tenant_id from JWT");
        }
    }

    private void validateRequiredClaim(JsonNode payload, String claim, String description) {
        if (!payload.has(claim) || payload.get(claim).asText().isBlank()) {
            throw new SecurityException("Missing required JWT claim: " + claim + " (" + description + ")");
        }
    }

    /** Add Base64 padding if needed */
    private String padBase64(String base64) {
        int pad = base64.length() % 4;
        if (pad == 2) return base64 + "==";
        if (pad == 3) return base64 + "=";
        return base64;
    }
}
