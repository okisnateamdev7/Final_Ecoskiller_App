package com.ecoskiller.mcp.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * SecurityContext — JWT validation and tenant/role extraction.
 *
 * In production, validation is performed against Keycloak 24.0 JWKS.
 * This implementation:
 *   - Parses the JWT payload (Base64url-decoded)
 *   - Extracts tenant_id, role, sub (candidate/recruiter ID), exp
 *   - Validates expiry
 *   - Validates required claims are present and non-empty
 *
 * For production deployment, replace the JWKS_URI constant with the
 * real Keycloak realm URL and enable signature verification via a
 * cached JWKS fetch + RS256 verification (e.g., nimbus-jose-jwt).
 *
 * SECURITY NOTES:
 *  - tenant_id is ALWAYS sourced from the JWT claim, never from tool args
 *  - Tokens signed with alg:none are unconditionally rejected
 *  - Expired tokens are rejected with a clear reason message
 */
public class SecurityContext {

    private static final Logger LOG = Logger.getLogger(SecurityContext.class.getName());
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // In production: replace with your Keycloak realm JWKS endpoint
    public static final String JWKS_URI =
        "https://auth.ecoskiller.com/realms/ecoskiller/protocol/openid-connect/certs";

    // ─────────────────────────────────────────────────────────────────────────
    // Public API
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Validate a bearer token string.
     * Returns a ValidationResult — check .valid() before using claims.
     */
    public ValidationResult validateToken(String bearerToken) {
        if (bearerToken == null || bearerToken.isBlank()) {
            return ValidationResult.invalid("Token is null or blank");
        }

        // Strip "Bearer " prefix if present
        String token = bearerToken.startsWith("Bearer ")
            ? bearerToken.substring(7).trim()
            : bearerToken.trim();

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return ValidationResult.invalid("Malformed JWT: expected 3 parts, got " + parts.length);
        }

        // ── Reject alg:none ────────────────────────────────────────────────
        try {
            String headerJson = decodeBase64Url(parts[0]);
            JsonNode header = MAPPER.readTree(headerJson);
            String alg = header.has("alg") ? header.get("alg").asText("") : "";
            if (alg.equalsIgnoreCase("none") || alg.isBlank()) {
                return ValidationResult.invalid(
                    "SECURITY VIOLATION: alg:none tokens are rejected unconditionally");
            }
        } catch (Exception e) {
            return ValidationResult.invalid("Cannot parse JWT header: " + e.getMessage());
        }

        // ── Parse payload ──────────────────────────────────────────────────
        JsonNode payload;
        try {
            String payloadJson = decodeBase64Url(parts[1]);
            payload = MAPPER.readTree(payloadJson);
        } catch (Exception e) {
            return ValidationResult.invalid("Cannot parse JWT payload: " + e.getMessage());
        }

        // ── Expiry check ───────────────────────────────────────────────────
        if (payload.has("exp")) {
            long exp = payload.get("exp").asLong(0L);
            long now = System.currentTimeMillis() / 1000L;
            if (now > exp) {
                return ValidationResult.invalid(
                    "Token expired at " + exp + " (current time: " + now + ")");
            }
        }

        // ── Required claims ────────────────────────────────────────────────
        String tenantId = extractClaim(payload, "tenant_id");
        if (tenantId == null) {
            return ValidationResult.invalid("Missing required claim: tenant_id");
        }

        String role = extractClaim(payload, "role");
        if (role == null) {
            // Fall back to realm_access.roles[0] (Keycloak default structure)
            role = extractKeycloakRole(payload);
        }
        if (role == null) {
            return ValidationResult.invalid("Missing required claim: role (or realm_access.roles)");
        }

        String sub = extractClaim(payload, "sub");
        if (sub == null) {
            return ValidationResult.invalid("Missing required claim: sub");
        }

        // ── Signature verification note ────────────────────────────────────
        // In production: verify RS256 signature using JWKS from JWKS_URI.
        // For MCP server scaffolding, signature verification is marked with
        // a clear TODO to integrate nimbus-jose-jwt or similar.
        // TODO: verify(parts[0] + "." + parts[1], parts[2], fetchJwks(JWKS_URI))

        LOG.fine("Token validated — tenant=" + tenantId + " role=" + role + " sub=" + sub);
        return ValidationResult.valid(tenantId, role, sub);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private String decodeBase64Url(String encoded) {
        // Handle URL-safe base64 without padding
        String padded = encoded
            .replace('-', '+')
            .replace('_', '/');
        int pad = padded.length() % 4;
        if (pad == 2) padded += "==";
        else if (pad == 3) padded += "=";
        return new String(Base64.getDecoder().decode(padded), StandardCharsets.UTF_8);
    }

    private String extractClaim(JsonNode payload, String claim) {
        if (payload.has(claim) && !payload.get(claim).asText("").isBlank()) {
            return payload.get(claim).asText();
        }
        return null;
    }

    private String extractKeycloakRole(JsonNode payload) {
        try {
            JsonNode realmAccess = payload.get("realm_access");
            if (realmAccess != null && realmAccess.has("roles")) {
                JsonNode roles = realmAccess.get("roles");
                // Priority order: admin > recruiter > system > readonly
                for (String prio : new String[]{"admin", "recruiter", "system", "readonly"}) {
                    for (JsonNode r : roles) {
                        if (r.asText("").equalsIgnoreCase(prio)) return prio;
                    }
                }
                if (roles.size() > 0) return roles.get(0).asText();
            }
        } catch (Exception ignored) {}
        return null;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tenant guard — call from tools to enforce tenant boundary
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Throws SecurityException if the tool argument tenant_id does not match
     * the JWT-derived tenant_id. Always call this when args contain tenant_id.
     */
    public void assertTenantMatch(String jwtTenantId, String argTenantId) {
        if (argTenantId != null && !argTenantId.equals(jwtTenantId)) {
            throw new SecurityException(
                "Cross-tenant access denied: JWT tenant=" + jwtTenantId +
                ", requested tenant=" + argTenantId);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ValidationResult
    // ─────────────────────────────────────────────────────────────────────────

    public record ValidationResult(
        boolean valid,
        String tenantId,
        String role,
        String sub,
        String reason
    ) {
        public static ValidationResult valid(String tenantId, String role, String sub) {
            return new ValidationResult(true, tenantId, role, sub, null);
        }

        public static ValidationResult invalid(String reason) {
            return new ValidationResult(false, null, null, null, reason);
        }
    }
}
