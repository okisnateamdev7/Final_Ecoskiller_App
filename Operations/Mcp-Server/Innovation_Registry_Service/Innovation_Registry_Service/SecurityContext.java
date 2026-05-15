package com.ecoskiller.mcp.irs.security;

import java.util.Base64;
import java.util.UUID;

/**
 * SecurityContext holds per-request identity information.
 *
 * In production:
 * - JWT is validated via auth-service (HS256/RS256)
 * - tenant_id extracted from JWT claims
 * - user_id extracted from JWT claims
 * - Role-based authorization enforced per tool
 *
 * This implementation provides the interface and simulates
 * a verified security context for MCP tool operations.
 */
public class SecurityContext {

    public enum Role {
        CANDIDATE,      // Can submit, view own ideas
        IDEA_OWNER,     // Can modify, license own ideas
        LEGAL_TEAM,     // Can approve, review all ideas
        ADMIN,          // Can flag, archive, manage all
        SERVICE_ACCOUNT // Internal service-to-service calls
    }

    private final String userId;
    private final String tenantId;
    private final Role role;
    private final boolean authenticated;

    private SecurityContext(String userId, String tenantId, Role role, boolean authenticated) {
        this.userId = userId;
        this.tenantId = tenantId;
        this.role = role;
        this.authenticated = authenticated;
    }

    /** Build from a validated JWT payload (in production, from auth-service) */
    public static SecurityContext fromJwt(String jwtToken) {
        // Production: verify signature against auth-service public key
        // Extract claims: sub (userId), tid (tenantId), roles
        // For MCP server: token passed in tool params or MCP session headers
        if (jwtToken == null || jwtToken.isBlank()) {
            return unauthenticated();
        }
        try {
            // Decode JWT payload (second segment, base64url)
            String[] parts = jwtToken.split("\\.");
            if (parts.length != 3) return unauthenticated();
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            // In production: parse JSON payload and verify against auth-service
            // Simplified extraction for MCP context:
            String userId = extractClaim(payload, "sub");
            String tenantId = extractClaim(payload, "tid");
            String roleStr = extractClaim(payload, "role");
            Role role = parseRole(roleStr);
            if (userId == null || tenantId == null) return unauthenticated();
            return new SecurityContext(userId, tenantId, role, true);
        } catch (Exception e) {
            return unauthenticated();
        }
    }

    /** Create an unauthenticated context */
    public static SecurityContext unauthenticated() {
        return new SecurityContext(null, null, null, false);
    }

    /** Build service-account context for internal operations */
    public static SecurityContext serviceAccount(String tenantId) {
        return new SecurityContext("service-account", tenantId, Role.SERVICE_ACCOUNT, true);
    }

    public boolean isAuthenticated() { return authenticated; }
    public String getUserId()        { return userId; }
    public String getTenantId()      { return tenantId; }
    public Role getRole()            { return role; }

    public boolean hasRole(Role required) {
        if (!authenticated || role == null) return false;
        return switch (required) {
            case CANDIDATE      -> true;  // All authenticated users can act as candidates
            case IDEA_OWNER     -> role == Role.IDEA_OWNER || role == Role.ADMIN || role == Role.SERVICE_ACCOUNT;
            case LEGAL_TEAM     -> role == Role.LEGAL_TEAM || role == Role.ADMIN;
            case ADMIN          -> role == Role.ADMIN;
            case SERVICE_ACCOUNT -> role == Role.SERVICE_ACCOUNT;
        };
    }

    /** Enforce tenant isolation: user's tenant must match resource's tenant */
    public boolean canAccessTenant(String resourceTenantId) {
        if (!authenticated) return false;
        if (role == Role.ADMIN) return true;  // Admins are cross-tenant
        return tenantId != null && tenantId.equals(resourceTenantId);
    }

    private static String extractClaim(String jsonPayload, String claim) {
        // Simple claim extraction for demo; production uses a JSON parser
        String key = "\"" + claim + "\"";
        int idx = jsonPayload.indexOf(key);
        if (idx < 0) return null;
        int colonIdx = jsonPayload.indexOf(':', idx + key.length());
        if (colonIdx < 0) return null;
        int quoteStart = jsonPayload.indexOf('"', colonIdx + 1);
        int quoteEnd   = jsonPayload.indexOf('"', quoteStart + 1);
        if (quoteStart < 0 || quoteEnd < 0) return null;
        return jsonPayload.substring(quoteStart + 1, quoteEnd);
    }

    private static Role parseRole(String roleStr) {
        if (roleStr == null) return Role.CANDIDATE;
        return switch (roleStr.toUpperCase()) {
            case "LEGAL_TEAM"     -> Role.LEGAL_TEAM;
            case "ADMIN"          -> Role.ADMIN;
            case "IDEA_OWNER"     -> Role.IDEA_OWNER;
            case "SERVICE_ACCOUNT"-> Role.SERVICE_ACCOUNT;
            default               -> Role.CANDIDATE;
        };
    }
}
