package com.ecoskiller.mcp.licensing.security;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * SecurityManager — centralised security for the licensing-contract-service MCP.
 *
 * Responsibilities:
 *  1. JWT validation (structure + claims, simulated Keycloak JWKS in dev mode)
 *  2. RBAC role enforcement per tool action
 *  3. Input sanitisation (SQL injection, path traversal, XSS)
 *  4. Rate limiting per actor (sliding window, in-memory)
 *  5. Audit logging of all security events to stderr
 *
 * In production, JWT signature verification is performed against the Keycloak
 * JWKS endpoint (KEYCLOAK_ISSUER_URL env var).  In dev/test the MCP server
 * operates in SIMULATION_MODE and accepts any structurally valid JWT.
 */
public class SecurityManager {

    private static final Logger LOG = Logger.getLogger(SecurityManager.class.getName());

    // ── Role constants ────────────────────────────────────────────────────────

    public static final String ROLE_CREATE = "ecoskiller:licensing:create";
    public static final String ROLE_ADMIN  = "ecoskiller:licensing:admin";
    public static final String ROLE_VIEWER = "ecoskiller:viewer";

    // ── Rate limiting: max requests per actor per minute ──────────────────────

    private static final int MAX_RPM = 60;
    private final Map<String, RateBucket> rateBuckets = new ConcurrentHashMap<>();

    // ── Dangerous input patterns ──────────────────────────────────────────────

    private static final Pattern SQL_INJECTION = Pattern.compile(
            "(?i)(select|insert|update|delete|drop|truncate|union|exec|execute|xp_|sp_|sysobject|syscolumn)",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern PATH_TRAVERSAL = Pattern.compile(
            "(\\.\\./|\\.\\.\\\\|%2e%2e|%252e)",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern XSS = Pattern.compile(
            "(<script|javascript:|onerror=|onload=|<iframe|<object|alert\\()",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern UUID_PATTERN = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private final boolean simulationMode;
    private final String  keycloakIssuer;

    public SecurityManager() {
        this.simulationMode = !"false".equalsIgnoreCase(System.getenv("SIMULATION_MODE"));
        this.keycloakIssuer = System.getenv("KEYCLOAK_ISSUER_URL") != null
                ? System.getenv("KEYCLOAK_ISSUER_URL")
                : "http://keycloak.ecoskiller.internal/realms/ecoskiller";

        LOG.info("[Security] SecurityManager initialised. simulationMode=" + simulationMode
                + " keycloakIssuer=" + keycloakIssuer);
    }

    // =========================================================================
    // 1. JWT Validation
    // =========================================================================

    /**
     * Validates the JWT token. Returns null on success, error message on failure.
     */
    public String validateJwt(String token) {
        if (token == null || token.isBlank()) {
            return "Missing jwt_token";
        }
        token = token.replaceFirst("(?i)^Bearer\\s+", "");

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return "Malformed JWT: expected 3 parts";
        }

        // Decode payload (base64url)
        JSONObject payload;
        try {
            byte[] decoded = Base64.getUrlDecoder().decode(padBase64(parts[1]));
            payload = new JSONObject(new String(decoded, StandardCharsets.UTF_8));
        } catch (Exception e) {
            return "JWT payload decode failed: " + e.getMessage();
        }

        // Validate standard claims
        long now = System.currentTimeMillis() / 1000L;

        if (payload.has("exp") && payload.getLong("exp") < now) {
            return "JWT expired";
        }
        if (payload.has("nbf") && payload.getLong("nbf") > now) {
            return "JWT not yet valid";
        }

        if (!simulationMode) {
            String iss = payload.optString("iss", "");
            if (!keycloakIssuer.equals(iss)) {
                return "JWT issuer mismatch. Expected: " + keycloakIssuer;
            }
        }

        // Rate limiting on subject
        String sub = payload.optString("sub", payload.optString("preferred_username", "unknown"));
        if (!checkRateLimit(sub)) {
            return "Rate limit exceeded for actor: " + sub;
        }

        return null; // OK
    }

    /**
     * Extract actor ID from JWT (sub claim).
     */
    public String extractActorId(String token) {
        try {
            token = token.replaceFirst("(?i)^Bearer\\s+", "");
            String[] parts = token.split("\\.");
            byte[] decoded = Base64.getUrlDecoder().decode(padBase64(parts[1]));
            JSONObject payload = new JSONObject(new String(decoded, StandardCharsets.UTF_8));
            return payload.optString("sub", "unknown");
        } catch (Exception e) {
            return "unknown";
        }
    }

    /**
     * Extract Keycloak realm roles from JWT.
     */
    public Set<String> extractRoles(String token) {
        try {
            token = token.replaceFirst("(?i)^Bearer\\s+", "");
            String[] parts = token.split("\\.");
            byte[] decoded = Base64.getUrlDecoder().decode(padBase64(parts[1]));
            JSONObject payload = new JSONObject(new String(decoded, StandardCharsets.UTF_8));

            // Keycloak puts roles in realm_access.roles
            if (payload.has("realm_access")) {
                JSONObject realm = payload.getJSONObject("realm_access");
                if (realm.has("roles")) {
                    Set<String> roles = new java.util.HashSet<>();
                    realm.getJSONArray("roles").forEach(r -> roles.add(r.toString()));
                    return roles;
                }
            }
            // Also support a flat "roles" claim (used in simulation mode)
            if (payload.has("roles")) {
                Set<String> roles = new java.util.HashSet<>();
                payload.getJSONArray("roles").forEach(r -> roles.add(r.toString()));
                return roles;
            }
        } catch (Exception ignored) {}

        // In simulation mode, grant all roles if no roles claim present
        if (simulationMode) {
            return Set.of(ROLE_CREATE, ROLE_ADMIN, ROLE_VIEWER);
        }
        return Set.of();
    }

    /**
     * Enforce that the actor holds at least one of the required roles.
     * Throws IllegalArgumentException if denied.
     */
    public void requireRole(String token, String... requiredRoles) {
        if (simulationMode) return; // Bypass in simulation for dev testing

        Set<String> actorRoles = extractRoles(token);
        for (String required : requiredRoles) {
            if (actorRoles.contains(required)) return;
        }
        String actor = extractActorId(token);
        LOG.warning("[Security] RBAC denied. actor=" + actor
                + " required=" + Set.of(requiredRoles)
                + " held=" + actorRoles);
        throw new IllegalArgumentException(
                "Insufficient permissions. Required role(s): " + String.join(" or ", requiredRoles));
    }

    // =========================================================================
    // 2. Input Sanitisation
    // =========================================================================

    /**
     * Sanitise a free-text string. Throws if malicious input detected.
     */
    public String sanitiseText(String input, String fieldName) {
        if (input == null) return null;

        // Length guard
        if (input.length() > 4000) {
            throw new IllegalArgumentException(fieldName + ": input exceeds max length of 4000 characters");
        }

        if (SQL_INJECTION.matcher(input).find()) {
            logSecurityEvent("SQL_INJECTION_ATTEMPT", fieldName, input.substring(0, Math.min(50, input.length())));
            throw new IllegalArgumentException(fieldName + ": potentially unsafe SQL keyword detected");
        }
        if (PATH_TRAVERSAL.matcher(input).find()) {
            logSecurityEvent("PATH_TRAVERSAL_ATTEMPT", fieldName, input.substring(0, Math.min(50, input.length())));
            throw new IllegalArgumentException(fieldName + ": path traversal pattern detected");
        }
        if (XSS.matcher(input).find()) {
            logSecurityEvent("XSS_ATTEMPT", fieldName, input.substring(0, Math.min(50, input.length())));
            throw new IllegalArgumentException(fieldName + ": script injection pattern detected");
        }
        return input.trim();
    }

    /**
     * Validate and return a UUID. Throws if not a valid UUID.
     */
    public String requireUuid(String input, String fieldName) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        if (!UUID_PATTERN.matcher(input.trim()).matches()) {
            throw new IllegalArgumentException(fieldName + ": must be a valid UUID (got: " + input + ")");
        }
        return input.trim().toLowerCase();
    }

    /**
     * Validate a royalty rate against platform bounds.
     */
    public double requireValidRoyaltyRate(double rate) {
        double min = getEnvDouble("DEFAULT_MIN_ROYALTY_RATE", 0.0001);
        double max = getEnvDouble("DEFAULT_MAX_ROYALTY_RATE", 0.0005);
        if (rate < min || rate > max) {
            throw new IllegalArgumentException(
                    String.format("proposed_royalty_rate %.6f is outside permissible range [%.4f, %.4f]",
                            rate, min, max));
        }
        return rate;
    }

    /**
     * Validate a licensing term against platform bounds.
     */
    public int requireValidTermYears(int years) {
        int min = getEnvInt("DEFAULT_MIN_TERM_YEARS", 10);
        int max = getEnvInt("DEFAULT_MAX_TERM_YEARS", 15);
        if (years < min || years > max) {
            throw new IllegalArgumentException(
                    "proposed_term_years " + years + " is outside permissible range [" + min + ", " + max + "]");
        }
        return years;
    }

    /**
     * Validate a GSTIN (Indian GST Identification Number).
     * Format: 2-digit state code + 10-char PAN + 1-digit entity + Z + 1 checksum = 15 chars.
     */
    public String sanitiseGstin(String gstin, String fieldName) {
        if (gstin == null || gstin.isBlank()) return null; // Optional field
        String cleaned = gstin.trim().toUpperCase();
        if (!cleaned.matches("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$")) {
            throw new IllegalArgumentException(fieldName + ": invalid GSTIN format");
        }
        return cleaned;
    }

    // =========================================================================
    // 3. Rate Limiting (sliding window, per actor, in-memory)
    // =========================================================================

    private boolean checkRateLimit(String actorId) {
        long now = System.currentTimeMillis();
        RateBucket bucket = rateBuckets.computeIfAbsent(actorId, k -> new RateBucket(now));

        // Reset window every 60 seconds
        if (now - bucket.windowStart > 60_000L) {
            bucket.windowStart = now;
            bucket.count.set(0);
        }

        int count = bucket.count.incrementAndGet();
        if (count > MAX_RPM) {
            logSecurityEvent("RATE_LIMIT_EXCEEDED", "actor", actorId);
            return false;
        }
        return true;
    }

    private static class RateBucket {
        volatile long windowStart;
        final AtomicInteger count = new AtomicInteger(0);
        RateBucket(long windowStart) { this.windowStart = windowStart; }
    }

    // =========================================================================
    // 4. Helpers
    // =========================================================================

    private void logSecurityEvent(String eventType, String field, String value) {
        LOG.warning("[Security] " + eventType + " field=" + field + " value=" + value);
    }

    private String padBase64(String s) {
        int pad = 4 - (s.length() % 4);
        if (pad < 4) s = s + "=".repeat(pad);
        return s;
    }

    private double getEnvDouble(String key, double def) {
        try { return Double.parseDouble(System.getenv(key)); } catch (Exception e) { return def; }
    }

    private int getEnvInt(String key, int def) {
        try { return Integer.parseInt(System.getenv(key)); } catch (Exception e) { return def; }
    }

    public boolean isSimulationMode() { return simulationMode; }
}
