package com.ecoskiller.mcp.security;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * SecurityManager — all security enforcement for the Job Service MCP.
 *
 * 1. JWT structure + claims validation (Base64URL header.payload.sig)
 * 2. Expiry enforcement (exp claim)
 * 3. RBAC per tool  (admin > recruiter > candidate > service)
 * 4. Rate limiting  (60 calls / 60 s per JWT subject)
 * 5. Input sanitisation — SQL injection / XSS / path traversal blocked
 * 6. alg:none attack blocked
 *
 * PRODUCTION NOTE:
 *   Replace the structural JWT check in parseAndValidate() with a real
 *   RS256 signature verification using Keycloak's JWKS endpoint:
 *   https://keycloak.ecoskiller.com/realms/{realm}/protocol/openid-connect/certs
 */
public class SecurityManager {

    private static final Logger log = Logger.getLogger(SecurityManager.class.getName());

    // ── Rate limiting ─────────────────────────────────────────────────────
    private static final int  RATE_MAX    = 60;
    private static final long RATE_WINDOW = 60_000L;
    private final Map<String, RateWindow> rateMap = new ConcurrentHashMap<>();

    // ── RBAC: tool → minimum required role ───────────────────────────────
    private static final Map<String, String> TOOL_ROLE = Map.ofEntries(
        Map.entry("job_create",              "recruiter"),
        Map.entry("job_get",                 "candidate"),
        Map.entry("job_update",              "recruiter"),
        Map.entry("job_deactivate",          "recruiter"),
        Map.entry("job_list",                "candidate"),
        Map.entry("job_bulk_import",         "recruiter"),
        Map.entry("job_moderate",            "admin"),
        Map.entry("job_visibility_update",   "recruiter"),
        Map.entry("job_salary_band_validate","admin"),
        Map.entry("job_seo_generate",        "recruiter"),
        Map.entry("job_search_fulltext",     "candidate"),
        Map.entry("job_status_transition",   "recruiter"),
        Map.entry("job_audit_trail",         "admin"),
        Map.entry("job_domain_stats",        "admin"),
        Map.entry("job_health",              "service"),
        Map.entry("job_schema",              "candidate")
    );

    // Role hierarchy — lower index = higher privilege
    private static final List<String> HIERARCHY = List.of("admin","recruiter","candidate","service");

    // ── Injection patterns to block ───────────────────────────────────────
    private static final List<String> DANGEROUS = List.of(
        "'","--",";","/*","*/","xp_","drop ","delete ","insert ","update ",
        "exec(","execute(","<script","javascript:","../","..\\","%2e%2e","union select"
    );

    // ─────────────────────────────────────────────────────────────────────
    // Public API
    // ─────────────────────────────────────────────────────────────────────

    /** Full security gate — throws SecurityException on any violation. */
    public TokenClaims validateToken(String token, String toolName) {
        if (token == null || token.isBlank())
            throw new SecurityException("Missing bearer_token — Keycloak JWT required");

        TokenClaims claims = parseAndValidate(token);
        enforceExpiry(claims);
        enforceRateLimit(claims.subject());
        enforceRbac(claims, toolName);

        log.info(String.format("AUTH OK | sub=%s role=%s tool=%s domain=%s",
            claims.subject(), claims.role(), toolName, claims.domain()));
        return claims;
    }

    /** Sanitise a user-supplied string field. Throws SecurityException on dangerous input. */
    public String sanitise(String value, String field) {
        if (value == null) return null;
        String lower = value.toLowerCase(Locale.ROOT);
        for (String p : DANGEROUS) {
            if (lower.contains(p))
                throw new SecurityException("Injection attempt detected in field: " + field);
        }
        // Strip control chars
        String cleaned = value.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]", "");
        if (cleaned.length() > 10_000)
            throw new IllegalArgumentException("Field too long: " + field + " (max 10000 chars)");
        return cleaned;
    }

    // ─────────────────────────────────────────────────────────────────────
    // JWT parsing
    // ─────────────────────────────────────────────────────────────────────

    private TokenClaims parseAndValidate(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3)
            throw new SecurityException("Malformed JWT: expected header.payload.signature");

        validateHeader(parts[0]);
        Map<String, String> claims = decodePayload(parts[1]);

        String subject = claims.getOrDefault("sub", "");
        String realm   = claims.getOrDefault("realm", "ecoskiller");
        String role    = bestRole(claims.getOrDefault("roles", "candidate"));
        String domain  = claims.getOrDefault("domain", "Technology");
        long   exp     = parseLong(claims.getOrDefault("exp", "0"));

        if (subject.isBlank())
            throw new SecurityException("JWT missing 'sub' claim");

        return new TokenClaims(subject, realm, role, domain, exp);
    }

    private void validateHeader(String headerB64) {
        try {
            String h = new String(Base64.getUrlDecoder().decode(pad(headerB64)), StandardCharsets.UTF_8);
            if (!h.contains("\"alg\""))
                throw new SecurityException("JWT header missing 'alg' claim");
            if (h.toLowerCase(Locale.ROOT).contains("\"none\""))
                throw new SecurityException("JWT alg:none is not permitted");
        } catch (IllegalArgumentException e) {
            throw new SecurityException("JWT header is invalid Base64URL");
        }
    }

    private Map<String, String> decodePayload(String payloadB64) {
        try {
            String json = new String(Base64.getUrlDecoder().decode(pad(payloadB64)), StandardCharsets.UTF_8);
            return flatJsonToMap(json);
        } catch (Exception e) {
            throw new SecurityException("Cannot decode JWT payload");
        }
    }

    /** Minimal flat-JSON to Map parser — only handles string/number values. */
    private Map<String, String> flatJsonToMap(String json) {
        Map<String, String> m = new HashMap<>();
        json = json.trim().replaceAll("^\\{|\\}$", "");
        // Split on commas that are not inside quotes
        String[] pairs = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (String pair : pairs) {
            String[] kv = pair.split(":", 2);
            if (kv.length == 2) {
                String k = kv[0].trim().replaceAll("^\"|\"$", "");
                String v = kv[1].trim().replaceAll("^\"|\"$", "");
                m.put(k, v);
            }
        }
        return m;
    }

    private String pad(String s) {
        int mod = s.length() % 4;
        if (mod == 2) return s + "==";
        if (mod == 3) return s + "=";
        return s;
    }

    private long parseLong(String s) {
        try { return Long.parseLong(s.trim()); } catch (NumberFormatException e) { return 0L; }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Enforcement helpers
    // ─────────────────────────────────────────────────────────────────────

    private void enforceExpiry(TokenClaims c) {
        long now = System.currentTimeMillis() / 1000;
        if (c.exp() > 0 && now > c.exp())
            throw new SecurityException("JWT has expired — re-authenticate via Keycloak");
    }

    private void enforceRbac(TokenClaims c, String tool) {
        String required = TOOL_ROLE.getOrDefault(tool, "admin");
        if (!hasRole(c.role(), required))
            throw new SecurityException(String.format(
                "Access denied: tool '%s' requires role '%s', caller has '%s'",
                tool, required, c.role()));
    }

    private boolean hasRole(String callerRole, String required) {
        int ci = HIERARCHY.indexOf(callerRole);
        int ri = HIERARCHY.indexOf(required);
        if (ci < 0 || ri < 0) return false;
        return ci <= ri; // lower index = higher privilege
    }

    private String bestRole(String rolesStr) {
        String[] parts = rolesStr.split(",");
        for (String h : HIERARCHY)
            for (String r : parts)
                if (r.trim().equalsIgnoreCase(h)) return h;
        return "candidate";
    }

    private void enforceRateLimit(String subject) {
        long now = System.currentTimeMillis();
        RateWindow w = rateMap.computeIfAbsent(subject, k -> new RateWindow(now));
        synchronized (w) {
            if (now - w.start > RATE_WINDOW) { w.start = now; w.count.set(0); }
            if (w.count.incrementAndGet() > RATE_MAX)
                throw new SecurityException("Rate limit exceeded: " + RATE_MAX + " req/min for: " + subject);
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Inner types
    // ─────────────────────────────────────────────────────────────────────

    public record TokenClaims(String subject, String realm, String role, String domain, long exp) {}

    private static class RateWindow {
        volatile long start;
        final AtomicInteger count = new AtomicInteger(0);
        RateWindow(long start) { this.start = start; }
    }
}
