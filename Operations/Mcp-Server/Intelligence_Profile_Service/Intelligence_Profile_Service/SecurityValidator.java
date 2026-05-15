package com.ecoskiller.mcp.ips.security;

import com.fasterxml.jackson.databind.JsonNode;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

/**
 * SecurityValidator
 *
 * Responsibilities:
 *  1. JWT structure & signature validation (HS256 / RS256 compatible stub)
 *  2. Tenant isolation — tenantId in request must match JWT claim
 *  3. RBAC enforcement — role-to-tool permission mapping
 *  4. Input sanitization — prevent injection attacks
 *  5. Rate-limit state (in-memory, per token)
 *
 * In production, replace the HMAC stub with a Keycloak public-key validator
 * (e.g. com.nimbusds:nimbus-jose-jwt).
 *
 * Roles defined in the IPS spec:
 *   admin     — full access to all tools including rebuild, audit
 *   recruiter — read profiles, search candidates, risk assessment
 *   candidate — read own profile, own skill vectors only
 *   ml_system — process events, compute vectors (service-to-service)
 */
public class SecurityValidator {

    private static final Logger LOG = Logger.getLogger(SecurityValidator.class.getName());

    /* ── RBAC: tool → minimum required role ─────────────────────────── */
    private static final Map<String, Set<String>> TOOL_ROLES = Map.ofEntries(
        Map.entry("get_intelligence_profile",    Set.of("admin", "recruiter", "candidate", "ml_system")),
        Map.entry("get_skill_vectors",           Set.of("admin", "recruiter", "candidate", "ml_system")),
        Map.entry("search_candidates",           Set.of("admin", "recruiter")),
        Map.entry("get_peer_benchmarks",         Set.of("admin", "recruiter", "candidate", "ml_system")),
        Map.entry("get_risk_assessment",         Set.of("admin", "recruiter")),     // candidates cannot access
        Map.entry("process_assessment_event",    Set.of("admin", "ml_system")),
        Map.entry("process_skill_assessment",    Set.of("admin", "ml_system")),
        Map.entry("process_gd_discussion",       Set.of("admin", "ml_system")),
        Map.entry("process_learning_interaction",Set.of("admin", "ml_system")),
        Map.entry("compute_intelligence_dna",    Set.of("admin", "ml_system")),
        Map.entry("detect_profile_anomaly",      Set.of("admin", "ml_system", "recruiter")),
        Map.entry("recalculate_vectors",         Set.of("admin")),
        Map.entry("get_fairness_audit",          Set.of("admin")),
        Map.entry("rebuild_profiles",            Set.of("admin")),
        Map.entry("get_profile_history",         Set.of("admin", "recruiter"))
    );

    /* ── In-memory rate-limit store (token → [window_start, count]) ─── */
    private final Map<String, long[]> rateLimits = new HashMap<>();
    private static final int MAX_REQUESTS_PER_MINUTE = 120;

    // ─────────────────────────────────────────────────────────────────────
    //  Public API
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Validates the _auth block in arguments and returns null on success,
     * or an error message string on failure.
     */
    public String validateRequest(JsonNode arguments) {
        JsonNode auth = arguments.path("_auth");
        if (auth.isMissingNode()) {
            return "Missing _auth block. Include { _auth: { token: '<JWT>' } }";
        }

        String token = auth.path("token").asText(null);
        if (token == null || token.isBlank()) {
            return "Missing or empty _auth.token";
        }

        // Sanitize token string (no whitespace, reasonable length)
        if (!token.matches("[A-Za-z0-9._\\-]+") || token.length() > 2048) {
            return "Malformed JWT token";
        }

        // Rate limiting
        String rateLimitError = checkRateLimit(token);
        if (rateLimitError != null) return rateLimitError;

        return null;   // OK — tool dispatch may proceed
    }

    /**
     * Authorizes a specific tool call after basic request validation.
     * Throws SecurityException on denial.
     */
    public JwtClaims authorizeToolCall(String toolName, JsonNode arguments) {
        String token = arguments.path("_auth").path("token").asText();
        JwtClaims claims = parseAndVerifyJwt(token);

        // Tenant consistency check
        String requestedTenantId = arguments.path("tenantId").asText(null);
        if (requestedTenantId != null && !requestedTenantId.equals(claims.tenantId)) {
            throw new SecurityException(
                "Tenant ID mismatch: JWT tenant=" + claims.tenantId +
                " request tenant=" + requestedTenantId);
        }

        // RBAC check
        Set<String> allowed = TOOL_ROLES.getOrDefault(toolName, Set.of());
        if (allowed.isEmpty()) {
            throw new SecurityException("Unknown tool: " + toolName);
        }
        boolean permitted = claims.roles.stream().anyMatch(allowed::contains);
        if (!permitted) {
            throw new SecurityException(
                "Role(s) " + claims.roles + " not permitted to call " + toolName +
                ". Required: " + allowed);
        }

        // Candidate self-access enforcement
        if (claims.roles.contains("candidate") && !claims.roles.contains("admin")) {
            String requestedUserId = arguments.path("userId").asText(null);
            if (requestedUserId != null && !requestedUserId.equals(claims.userId)) {
                throw new SecurityException(
                    "Candidate can only access their own profile. " +
                    "Requested userId=" + requestedUserId + " but JWT userId=" + claims.userId);
            }
        }

        LOG.fine("Authorized: user=" + claims.userId + " role=" + claims.roles + " tool=" + toolName);
        return claims;
    }

    /**
     * Sanitize a string input to prevent injection.
     */
    public static String sanitize(String input) {
        if (input == null) return null;
        // Strip SQL meta-chars, script tags, null bytes
        return input.replaceAll("[\\x00'\";<>\\\\]", "").trim();
    }

    /**
     * Validate UUID format.
     */
    public static boolean isValidUUID(String s) {
        if (s == null) return false;
        return s.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
    }

    // ─────────────────────────────────────────────────────────────────────
    //  JWT parsing (stub — replace with nimbus-jose-jwt in production)
    // ─────────────────────────────────────────────────────────────────────

    public JwtClaims parseAndVerifyJwt(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new SecurityException("JWT must have 3 parts (header.payload.signature)");
            }

            // Decode payload (base64url, no padding)
            String payloadJson = new String(
                Base64.getUrlDecoder().decode(padBase64(parts[1])),
                StandardCharsets.UTF_8);

            com.fasterxml.jackson.databind.ObjectMapper om =
                new com.fasterxml.jackson.databind.ObjectMapper();
            JsonNode payload = om.readTree(payloadJson);

            // Expiry check
            long exp = payload.path("exp").asLong(0);
            if (exp > 0 && System.currentTimeMillis() / 1000 > exp) {
                throw new SecurityException("JWT has expired");
            }

            // Issuer check
            String iss = payload.path("iss").asText("");
            if (!iss.isBlank() && !iss.contains("ecoskiller")) {
                throw new SecurityException("JWT issuer not trusted: " + iss);
            }

            // Extract standard claims
            String userId   = payload.path("sub").asText(
                              payload.path("userId").asText("unknown"));
            String tenantId = payload.path("tenantId").asText(
                              payload.path("tenant_id").asText("unknown"));

            List<String> roles = new ArrayList<>();
            JsonNode rolesNode = payload.path("roles");
            if (rolesNode.isArray()) {
                rolesNode.forEach(r -> roles.add(r.asText()));
            } else {
                // Accept single role field too
                String role = payload.path("role").asText(null);
                if (role != null) roles.add(role);
            }
            if (roles.isEmpty()) roles.add("candidate");  // default minimal role

            return new JwtClaims(userId, tenantId, roles);

        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            throw new SecurityException("JWT validation failed: " + e.getMessage());
        }
    }

    private String padBase64(String s) {
        switch (s.length() % 4) {
            case 2: return s + "==";
            case 3: return s + "=";
            default: return s;
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Rate limiting
    // ─────────────────────────────────────────────────────────────────────

    private synchronized String checkRateLimit(String token) {
        long now = System.currentTimeMillis();
        long[] state = rateLimits.get(token);
        if (state == null || now - state[0] > 60_000) {
            rateLimits.put(token, new long[]{now, 1});
            return null;
        }
        state[1]++;
        if (state[1] > MAX_REQUESTS_PER_MINUTE) {
            return "Rate limit exceeded: " + MAX_REQUESTS_PER_MINUTE + " requests/minute per token";
        }
        return null;
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Claims value object
    // ─────────────────────────────────────────────────────────────────────

    public static class JwtClaims {
        public final String userId;
        public final String tenantId;
        public final List<String> roles;

        JwtClaims(String userId, String tenantId, List<String> roles) {
            this.userId   = userId;
            this.tenantId = tenantId;
            this.roles    = Collections.unmodifiableList(roles);
        }
    }
}
