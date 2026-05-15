package com.ecoskiller.airflow.mcp.security;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Validates Keycloak JWTs for the Ecoskiller Airflow MCP Server.
 *
 * Validates: exp (expiry), iss (issuer), aud (audience: airflow-service)
 * Extracts: realm_access.roles for RBAC
 *
 * Production: Wire in JWKS signature verification via nimbus-jose-jwt:
 *   GET https://keycloak.ecoskiller.internal/realms/ecoskiller/protocol/openid-connect/certs
 */
public class JwtValidator {

    private static final Logger LOG     = Logger.getLogger(JwtValidator.class.getName());
    private static final String ISSUER  = System.getenv().getOrDefault(
        "KEYCLOAK_ISSUER", "https://keycloak.ecoskiller.internal/realms/ecoskiller");
    private static final String AUDIENCE = "airflow-service";
    private static final boolean STRICT  = !"false".equalsIgnoreCase(
        System.getenv("JWT_STRICT_SIGNATURE"));

    // Roles allowed in this service
    public static final String SUPER_ADMIN        = "SUPER_ADMIN";
    public static final String OPS_LEAD           = "OPS_LEAD";
    public static final String COMPLIANCE_OFFICER = "COMPLIANCE_OFFICER";

    public String validate(String token) {
        if (token == null || token.isBlank()) {
            if (!STRICT) { LOG.warning("[JWT] Dev mode — no token"); return "dev-user"; }
            throw new SecurityException("Missing JWT token");
        }
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new SecurityException("Malformed JWT");

        JSONObject payload = decodePayload(parts[1]);

        // Expiry
        long now = System.currentTimeMillis() / 1000;
        long exp = payload.optLong("exp", 0);
        if (exp > 0 && now > exp)
            throw new SecurityException("JWT expired at " + exp + ", now=" + now);

        // Issuer
        String iss = payload.optString("iss", "");
        if (STRICT && !ISSUER.equals(iss))
            throw new SecurityException("JWT issuer mismatch: " + iss);

        // Audience
        boolean audOk = false;
        Object aud = payload.opt("aud");
        if (aud instanceof String s)      audOk = AUDIENCE.equals(s);
        if (aud instanceof JSONArray arr)  audOk = arr.toList().contains(AUDIENCE);
        if (STRICT && !audOk)
            throw new SecurityException("JWT audience missing '" + AUDIENCE + "'");

        if (STRICT) verifySignatureStub(parts);

        String sub = payload.optString("sub", "");
        if (sub.isBlank()) throw new SecurityException("JWT missing sub claim");
        return sub;
    }

    public Set<String> getRoles(JSONObject req) {
        Set<String> roles = new HashSet<>();
        JSONObject payload = extractPayload(req);
        if (payload == null) {
            if (!STRICT) { roles.add(SUPER_ADMIN); roles.add(OPS_LEAD); }
            return roles;
        }
        // realm_access.roles (Keycloak standard)
        JSONObject ra = payload.optJSONObject("realm_access");
        if (ra != null) {
            JSONArray arr = ra.optJSONArray("roles");
            if (arr != null) arr.forEach(r -> roles.add(r.toString().toUpperCase()));
        }
        // resource_access.airflow-service.roles
        JSONObject resAccess = payload.optJSONObject("resource_access");
        if (resAccess != null) {
            JSONObject svc = resAccess.optJSONObject(AUDIENCE);
            if (svc != null) {
                JSONArray arr = svc.optJSONArray("roles");
                if (arr != null) arr.forEach(r -> roles.add(r.toString().toUpperCase()));
            }
        }
        // MFA required for SUPER_ADMIN
        if (roles.contains(SUPER_ADMIN) && STRICT) {
            boolean mfa = payload.optBoolean("amr_mfa", false);
            String  acr = payload.optString("acr", "");
            if (!mfa && !acr.contains("mfa")) {
                roles.remove(SUPER_ADMIN);
                LOG.warning("[JWT] SUPER_ADMIN stripped: MFA not satisfied");
            }
        }
        return roles;
    }

    // ── Internal helpers ───────────────────────────────────────────────────────

    private JSONObject extractPayload(JSONObject req) {
        JSONObject meta = req.optJSONObject("_meta");
        String token = (meta != null) ? meta.optString("authorization", "") : "";
        if (token.startsWith("Bearer ")) token = token.substring(7);
        if (token.isBlank()) return null;
        try {
            String[] p = token.split("\\.");
            return (p.length >= 2) ? decodePayload(p[1]) : null;
        } catch (Exception e) { return null; }
    }

    private JSONObject decodePayload(String b64url) {
        try {
            byte[] bytes = Base64.getUrlDecoder().decode(pad(b64url));
            return new JSONObject(new String(bytes, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new SecurityException("Cannot decode JWT payload: " + e.getMessage());
        }
    }

    private String pad(String s) {
        return switch (s.length() % 4) { case 2 -> s+"=="; case 3 -> s+"="; default -> s; };
    }

    private void verifySignatureStub(String[] parts) {
        // TODO: integrate nimbus-jose-jwt JWKS verification before production
        LOG.warning("[JWT] Signature verification is a stub — wire in JWKS before production!");
    }
}
