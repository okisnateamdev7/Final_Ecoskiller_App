package com.ecoskiller.admin.mcp.security;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Validates Keycloak-issued JWTs for the Ecoskiller Admin Service.
 *
 * Security design:
 *  - Decodes JWT payload to extract admin_id and realm_roles
 *  - In production: verify signature against Keycloak JWKS endpoint
 *  - Validates expiry (exp claim) — rejects expired tokens
 *  - Validates issuer (iss claim) — must match Keycloak realm URL
 *  - Validates audience (aud claim) — must include "admin-service"
 *  - MFA claim checked for sensitive roles (SUPER_ADMIN)
 *
 * NOTE: Signature verification requires the org.json + a JWT lib (e.g. nimbus-jose-jwt).
 *       This implementation validates structure & claims; wire in full JWKS
 *       verification by replacing verifySignature() when deploying with the
 *       nimbus-jose-jwt library jar.
 */
public class JwtValidator {

    private static final Logger LOG         = Logger.getLogger(JwtValidator.class.getName());
    private static final String ISSUER      = System.getenv().getOrDefault(
        "KEYCLOAK_ISSUER", "https://keycloak.ecoskiller.internal/realms/ecoskiller");
    private static final String AUDIENCE    = "admin-service";
    private static final boolean STRICT_SIG = !"false".equalsIgnoreCase(
        System.getenv("JWT_STRICT_SIGNATURE"));

    // Roles known to the system
    public static final String SUPER_ADMIN         = "SUPER_ADMIN";
    public static final String COMPLIANCE_OFFICER  = "COMPLIANCE_OFFICER";
    public static final String CONTENT_MANAGER     = "CONTENT_MANAGER";
    public static final String OPS_LEAD            = "OPS_LEAD";
    public static final String FINANCE_MANAGER     = "FINANCE_MANAGER";

    /**
     * Validates the JWT and returns the admin_id (sub claim).
     * Throws SecurityException for any validation failure.
     */
    public String validate(String token) {
        if (token == null || token.isBlank()) {
            // Dev mode: allow anonymous with limited role
            if (!STRICT_SIG) {
                LOG.warning("[JWT] No token provided — running in dev/no-auth mode");
                return "dev-admin";
            }
            throw new SecurityException("Missing JWT token");
        }

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new SecurityException("Malformed JWT: expected 3 parts");
        }

        JSONObject payload = decodePayload(parts[1]);

        // 1. Expiry check
        long now = System.currentTimeMillis() / 1000;
        long exp = payload.optLong("exp", 0);
        if (exp > 0 && now > exp) {
            throw new SecurityException("JWT expired at " + exp + ", now=" + now);
        }

        // 2. Issuer check
        String iss = payload.optString("iss", "");
        if (STRICT_SIG && !ISSUER.equals(iss)) {
            throw new SecurityException("JWT issuer mismatch: got=" + iss);
        }

        // 3. Audience check
        boolean audOk = false;
        Object audObj = payload.opt("aud");
        if (audObj instanceof String s)       audOk = AUDIENCE.equals(s);
        if (audObj instanceof JSONArray arr)   audOk = arr.toList().contains(AUDIENCE);
        if (STRICT_SIG && !audOk) {
            throw new SecurityException("JWT audience missing '" + AUDIENCE + "'");
        }

        // 4. Signature verification (stub — wire in JWKS in production)
        if (STRICT_SIG) {
            verifySignature(parts);
        }

        String sub = payload.optString("sub", "");
        if (sub.isBlank()) throw new SecurityException("JWT missing sub claim");
        return sub;
    }

    /**
     * Extracts realm_roles from the JWT payload and returns them as a Set.
     * Keycloak puts roles under: realm_access.roles
     */
    public Set<String> getRoles(org.json.JSONObject req) {
        Set<String> roles = new HashSet<>();

        JSONObject meta = req.optJSONObject("_meta");
        String token = (meta != null) ? meta.optString("authorization", "") : "";
        if (token.startsWith("Bearer ")) token = token.substring(7);

        if (token.isBlank()) {
            if (!STRICT_SIG) roles.add(SUPER_ADMIN); // dev mode — grant all
            return roles;
        }

        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return roles;

            JSONObject payload = decodePayload(parts[1]);

            // Keycloak realm_access.roles
            JSONObject realmAccess = payload.optJSONObject("realm_access");
            if (realmAccess != null) {
                JSONArray arr = realmAccess.optJSONArray("roles");
                if (arr != null) arr.forEach(r -> roles.add(r.toString().toUpperCase()));
            }

            // Also check resource_access.admin-service.roles
            JSONObject resourceAccess = payload.optJSONObject("resource_access");
            if (resourceAccess != null) {
                JSONObject svcRoles = resourceAccess.optJSONObject(AUDIENCE);
                if (svcRoles != null) {
                    JSONArray arr = svcRoles.optJSONArray("roles");
                    if (arr != null) arr.forEach(r -> roles.add(r.toString().toUpperCase()));
                }
            }

            // MFA check for SUPER_ADMIN
            if (roles.contains(SUPER_ADMIN)) {
                boolean mfa = payload.optBoolean("amr_mfa", false);
                String  amr = payload.optString("acr", "");
                if (STRICT_SIG && !mfa && !amr.contains("mfa")) {
                    roles.remove(SUPER_ADMIN); // demote — MFA not satisfied
                    LOG.warning("[JWT] SUPER_ADMIN role stripped: MFA not satisfied");
                }
            }
        } catch (Exception e) {
            LOG.warning("[JWT] Could not extract roles: " + e.getMessage());
        }
        return roles;
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private JSONObject decodePayload(String base64Url) {
        try {
            byte[] bytes = Base64.getUrlDecoder().decode(padBase64(base64Url));
            return new JSONObject(new String(bytes, StandardCharsets.UTF_8));
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
     * Signature verification stub.
     * Production: fetch JWKS from Keycloak and verify RS256/ES256 signature
     * using nimbus-jose-jwt or java-jwt library.
     */
    private void verifySignature(String[] parts) {
        // TODO: Integrate with Keycloak JWKS endpoint:
        //   GET https://keycloak.ecoskiller.internal/realms/ecoskiller/protocol/openid-connect/certs
        // Use: com.nimbusds.jose.jwk.source.RemoteJWKSet
        // For now — log a warning and continue; replace before go-live.
        LOG.warning("[JWT] Signature verification is a stub — implement JWKS before production!");
    }
}
