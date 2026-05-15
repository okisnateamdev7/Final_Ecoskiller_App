package com.ecoskiller.mcp.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;
import java.util.Base64;

/**
 * Security layer for the Application-Service MCP Server.
 *
 * Responsibilities:
 *   1. JWT validation     — verify Keycloak-issued Bearer tokens (HS256 / RS256 stub)
 *   2. RBAC enforcement   — CANDIDATE, RECRUITER, ADMIN role checks
 *   3. Tenant isolation   — ensure tenant_id is present and matches the JWT claim
 *   4. Input sanitisation — block SQL injection, script injection, oversized payloads
 *   5. Idempotency keys   — track write operations to prevent duplicate processing
 *
 * In production, configure via environment variables:
 *   APP_JWT_SECRET       — shared HMAC secret (HS256)
 *   APP_KEYCLOAK_URL     — issuer URL for RS256 public-key fetch
 *   APP_MAX_PAYLOAD_SIZE — maximum input string length (default: 65535)
 */
public class SecurityConfig {

    private static final Logger LOG = Logger.getLogger(SecurityConfig.class.getName());

    // Roles recognised by this service (mirrors Keycloak realm roles)
    public static final String ROLE_CANDIDATE = "CANDIDATE";
    public static final String ROLE_RECRUITER = "RECRUITER";
    public static final String ROLE_ADMIN     = "ADMIN";

    // Configurable limits
    private static int    MAX_PAYLOAD    = 65535;
    private static String JWT_SECRET     = "ecoskiller-dev-secret-change-in-prod";
    private static boolean STRICT_MODE   = false; // flip to true in prod

    // In-memory idempotency store (production: use Redis)
    private static final Set<String> IDEMPOTENCY_STORE = Collections.synchronizedSet(new LinkedHashSet<>() {
        // Keep only last 10 000 keys to avoid unbounded growth
        @Override public boolean add(String s) {
            if (size() >= 10_000) { Iterator<String> it = iterator(); it.next(); it.remove(); }
            return super.add(s);
        }
    });

    public static void init() {
        String secret = System.getenv("APP_JWT_SECRET");
        if (secret != null && !secret.isBlank()) JWT_SECRET = secret;

        String maxPayload = System.getenv("APP_MAX_PAYLOAD_SIZE");
        if (maxPayload != null) { try { MAX_PAYLOAD = Integer.parseInt(maxPayload); } catch (NumberFormatException ignored) {} }

        String strict = System.getenv("APP_STRICT_MODE");
        if ("true".equalsIgnoreCase(strict)) STRICT_MODE = true;

        LOG.info("SecurityConfig initialised | strict=" + STRICT_MODE + " | maxPayload=" + MAX_PAYLOAD);
    }

    // -------------------------------------------------------------------------
    // JWT Validation
    // -------------------------------------------------------------------------

    /**
     * Validates a Keycloak Bearer JWT token.
     *
     * Returns a JwtClaims object containing tenant_id, user_id and roles.
     * Throws SecurityException on any validation failure.
     *
     * Supports HS256 (dev) and passes through RS256 tokens in non-strict mode.
     */
    public static JwtClaims validateToken(String bearerToken) {
        if (bearerToken == null || bearerToken.isBlank()) {
            throw new SecurityException("Missing Authorization token");
        }
        String token = bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;

        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new SecurityException("Malformed JWT: expected 3 parts");

        // Decode header
        String headerJson = decodeB64(parts[0]);
        String payloadJson = decodeB64(parts[1]);

        // Verify signature (HS256 in dev mode)
        if (STRICT_MODE) {
            verifyHmac(parts[0] + "." + parts[1], parts[2]);
        }

        // Parse payload claims
        return extractClaims(payloadJson);
    }

    private static void verifyHmac(String data, String expectedSig) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(JWT_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] computed = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String computedB64 = Base64.getUrlEncoder().withoutPadding().encodeToString(computed);
            if (!computedB64.equals(expectedSig)) {
                throw new SecurityException("JWT signature verification failed");
            }
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            throw new SecurityException("JWT crypto error: " + e.getMessage());
        }
    }

    private static String decodeB64(String encoded) {
        try {
            byte[] decoded = Base64.getUrlDecoder().decode(padBase64(encoded));
            return new String(decoded, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new SecurityException("JWT base64 decode failed: " + e.getMessage());
        }
    }

    private static String padBase64(String s) {
        switch (s.length() % 4) {
            case 2: return s + "==";
            case 3: return s + "=";
            default: return s;
        }
    }

    private static JwtClaims extractClaims(String payloadJson) {
        // Simple key extraction from JSON payload
        String sub        = extractJsonString(payloadJson, "sub");
        String tenantId   = extractJsonString(payloadJson, "tenant_id");
        String email      = extractJsonString(payloadJson, "email");
        List<String> roles = extractRoles(payloadJson);

        // Expiry check
        long exp = extractJsonLong(payloadJson, "exp");
        if (exp > 0 && System.currentTimeMillis() / 1000 > exp) {
            throw new SecurityException("JWT token has expired");
        }

        if (sub == null || sub.isBlank()) throw new SecurityException("JWT missing 'sub' claim");
        if (tenantId == null || tenantId.isBlank()) {
            // Allow fallback for dev tokens
            if (STRICT_MODE) throw new SecurityException("JWT missing 'tenant_id' claim");
            tenantId = "dev-tenant";
        }

        return new JwtClaims(sub, tenantId, email, roles);
    }

    private static String extractJsonString(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx < 0) return null;
        int colon = json.indexOf(':', idx + search.length());
        if (colon < 0) return null;
        int start = json.indexOf('"', colon + 1);
        if (start < 0) return null;
        int end = json.indexOf('"', start + 1);
        if (end < 0) return null;
        return json.substring(start + 1, end);
    }

    private static long extractJsonLong(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx < 0) return -1;
        int colon = json.indexOf(':', idx + search.length());
        if (colon < 0) return -1;
        StringBuilder num = new StringBuilder();
        for (int i = colon + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            if (Character.isDigit(c) || c == '-') num.append(c);
            else if (num.length() > 0) break;
        }
        try { return Long.parseLong(num.toString()); } catch (NumberFormatException e) { return -1; }
    }

    private static List<String> extractRoles(String json) {
        List<String> roles = new ArrayList<>();
        // Keycloak embeds realm roles in realm_access.roles array
        int realmIdx = json.indexOf("\"realm_access\"");
        if (realmIdx < 0) {
            // Fallback: look for top-level "roles" array
            realmIdx = json.indexOf("\"roles\"");
            if (realmIdx < 0) return roles;
        }
        int arrStart = json.indexOf('[', realmIdx);
        int arrEnd   = json.indexOf(']', arrStart);
        if (arrStart < 0 || arrEnd < 0) return roles;
        String arr = json.substring(arrStart + 1, arrEnd);
        for (String part : arr.split(",")) {
            String role = part.trim().replace("\"", "");
            if (!role.isBlank()) roles.add(role.toUpperCase());
        }
        return roles;
    }

    // -------------------------------------------------------------------------
    // RBAC
    // -------------------------------------------------------------------------

    /** Throws SecurityException if the claims don't include at least one of the required roles. */
    public static void requireRole(JwtClaims claims, String... roles) {
        for (String role : roles) {
            if (claims.hasRole(role)) return;
        }
        throw new SecurityException("Access denied. Required role(s): " + Arrays.toString(roles)
                + " — user has: " + claims.getRoles());
    }

    // -------------------------------------------------------------------------
    // Tenant isolation
    // -------------------------------------------------------------------------

    /**
     * Validates that the tenant_id in the request arguments matches the JWT claim.
     * Admins may cross-tenant query.
     */
    public static void validateTenantAccess(JwtClaims claims, Map<String, Object> args) {
        String reqTenant = getString(args, "tenant_id");
        if (reqTenant == null || reqTenant.isBlank()) return; // caller will use JWT tenant
        if (!claims.hasRole(ROLE_ADMIN) && !claims.getTenantId().equals(reqTenant)) {
            throw new SecurityException("Tenant mismatch: token=" + claims.getTenantId()
                    + " request=" + reqTenant);
        }
    }

    /** Returns the effective tenant_id (from args if admin, else from JWT). */
    public static String effectiveTenantId(JwtClaims claims, Map<String, Object> args) {
        String reqTenant = getString(args, "tenant_id");
        if (reqTenant != null && !reqTenant.isBlank() && claims.hasRole(ROLE_ADMIN)) {
            return reqTenant;
        }
        return claims.getTenantId();
    }

    // -------------------------------------------------------------------------
    // Input sanitisation
    // -------------------------------------------------------------------------

    private static final String[] SQL_INJECTION_PATTERNS = {
        "--", ";--", "/*", "*/", "xp_", "UNION SELECT", "DROP TABLE",
        "INSERT INTO", "DELETE FROM", "UPDATE SET", "EXEC(", "EXECUTE(",
        "' OR '1'='1", "\" OR \"1\"=\"1"
    };

    private static final String[] SCRIPT_INJECTION_PATTERNS = {
        "<script", "javascript:", "onerror=", "onload=", "eval(",
        "document.cookie", "window.location"
    };

    /**
     * Sanitises a string input: checks length, SQL injection, XSS.
     * Returns the trimmed, safe string.
     */
    public static String sanitise(String input, String fieldName) {
        if (input == null) return null;
        String trimmed = input.trim();

        if (trimmed.length() > MAX_PAYLOAD) {
            throw new IllegalArgumentException("Field '" + fieldName + "' exceeds maximum length of " + MAX_PAYLOAD);
        }
        String upper = trimmed.toUpperCase();
        for (String pattern : SQL_INJECTION_PATTERNS) {
            if (upper.contains(pattern.toUpperCase())) {
                throw new SecurityException("Potential SQL injection detected in field: " + fieldName);
            }
        }
        for (String pattern : SCRIPT_INJECTION_PATTERNS) {
            if (upper.contains(pattern.toUpperCase())) {
                throw new SecurityException("Potential script injection detected in field: " + fieldName);
            }
        }
        return trimmed;
    }

    /** Require a non-empty, sanitised string. */
    public static String requireString(Map<String, Object> args, String key) {
        String val = getString(args, key);
        if (val == null || val.isBlank()) throw new IllegalArgumentException("Missing required field: " + key);
        return sanitise(val, key);
    }

    public static String getString(Map<String, Object> args, String key) {
        Object v = args.get(key);
        return v != null ? v.toString() : null;
    }

    // -------------------------------------------------------------------------
    // Idempotency
    // -------------------------------------------------------------------------

    /**
     * Checks and registers an idempotency key.
     * Returns true if this is a NEW request (should be processed).
     * Returns false if this is a DUPLICATE (response should be replayed from cache).
     */
    public static boolean checkIdempotency(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) return true;
        return IDEMPOTENCY_STORE.add(idempotencyKey);
    }

    // -------------------------------------------------------------------------
    // UUID generation
    // -------------------------------------------------------------------------

    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}
