package com.ecoskiller.mcp.security;

import com.ecoskiller.mcp.config.ServerConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * JWT validator for Ecoskiller Keycloak-issued tokens.
 *
 * Security guarantees:
 *  • Structural validation (3-part Base64URL)
 *  • Rejects alg:none (CVE class prevention)
 *  • Expiry (exp) validation
 *  • Future-issued token rejection (iat > now + 60s clock skew)
 *  • Issuer (iss) validation against configured Keycloak realm
 *  • DoS protection — token length capped at 8192 bytes
 *  • Required claims enforcement (sub, iss)
 *
 * Production note: Full RSA signature verification requires the Keycloak
 * JWKS endpoint (https://auth.ecoskiller.com/realms/{realm}/protocol/openid-connect/certs).
 * Integrate java-jwt or nimbus-jose-jwt for RS256 signature validation.
 */
public class JwtValidator {

    private static final Logger LOGGER = Logger.getLogger(JwtValidator.class.getName());
    private final ServerConfig config;
    private final ObjectMapper mapper = new ObjectMapper();

    public JwtValidator(ServerConfig config) { this.config = config; }

    /** Validate JWT — throws SecurityException on any failure */
    public void validate(String token) {
        if (token == null || token.isBlank())
            throw new SecurityException("JWT token is required");
        if (token.length() > 8192)
            throw new SecurityException("JWT token exceeds maximum length (DoS protection)");

        String[] parts = token.split("\\.");
        if (parts.length != 3)
            throw new SecurityException("Invalid JWT structure: expected 3 dot-separated parts");

        try {
            // --- Header ---
            String headerJson = decode(parts[0]);
            JsonNode header   = mapper.readTree(headerJson);

            // Security: reject alg:none (prevents signature bypass attack)
            String alg = header.path("alg").asText("");
            if (alg.equalsIgnoreCase("none") || alg.isBlank())
                throw new SecurityException("JWT algorithm 'none' is rejected");
            if (!alg.startsWith("RS") && !alg.startsWith("ES"))
                LOGGER.warning("JWT uses non-asymmetric algorithm: " + alg +
                        " — RS256/ES256 recommended for production");

            // --- Payload ---
            String payloadJson = decode(parts[1]);
            JsonNode payload   = mapper.readTree(payloadJson);

            // Expiry check
            long exp = payload.path("exp").asLong(0);
            long now = System.currentTimeMillis() / 1000;
            if (exp > 0 && now > exp)
                throw new SecurityException("JWT token has expired (exp=" + exp + ", now=" + now + ")");

            // Future-issued check (prevent tokens from the future)
            long iat = payload.path("iat").asLong(0);
            if (iat > now + 60)
                throw new SecurityException("JWT issued in the future (iat=" + iat + ")");

            // Required claims
            requireClaim(payload, "sub", "Subject (user_id)");
            requireClaim(payload, "iss", "Issuer");

            // Issuer validation
            String iss = payload.path("iss").asText("");
            if (!config.getKeycloakIssuer().isEmpty() && !iss.startsWith(config.getKeycloakIssuer()))
                throw new SecurityException("JWT issuer mismatch. Expected prefix: " +
                        config.getKeycloakIssuer() + ", got: " + iss);

            LOGGER.fine("JWT validated: sub=" + payload.path("sub").asText());

        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            throw new SecurityException("JWT parse/validation failed: " + e.getMessage());
        }
    }

    /** Extract user_id from validated token */
    public String extractUserId(String token) {
        try {
            String payload = decode(token.split("\\.")[1]);
            return mapper.readTree(payload).path("sub").asText("");
        } catch (Exception e) {
            throw new SecurityException("Cannot extract user_id from JWT");
        }
    }

    /** Extract roles from validated token */
    public java.util.List<String> extractRoles(String token) {
        try {
            String payload = decode(token.split("\\.")[1]);
            JsonNode roles = mapper.readTree(payload).path("roles");
            java.util.List<String> result = new java.util.ArrayList<>();
            if (roles.isArray()) roles.forEach(r -> result.add(r.asText()));
            return result;
        } catch (Exception e) {
            return java.util.Collections.emptyList();
        }
    }

    private void requireClaim(JsonNode payload, String claim, String description) {
        if (!payload.has(claim) || payload.get(claim).asText().isBlank())
            throw new SecurityException("Missing required JWT claim: " + claim + " (" + description + ")");
    }

    private String decode(String b64) {
        String padded = switch (b64.length() % 4) {
            case 2  -> b64 + "==";
            case 3  -> b64 + "=";
            default -> b64;
        };
        return new String(Base64.getUrlDecoder().decode(padded), StandardCharsets.UTF_8);
    }
}
