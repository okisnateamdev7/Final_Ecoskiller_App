package com.ecoskiller.mcp.security;

import java.util.List;

/**
 * Immutable value object representing claims extracted from a validated Keycloak JWT.
 *
 * Fields:
 *   userId   — Keycloak subject (UUID)
 *   tenantId — tenant_id claim (maps to a Keycloak realm)
 *   email    — user's email
 *   roles    — realm roles: CANDIDATE | RECRUITER | ADMIN
 */
public final class JwtClaims {

    private final String       userId;
    private final String       tenantId;
    private final String       email;
    private final List<String> roles;

    public JwtClaims(String userId, String tenantId, String email, List<String> roles) {
        this.userId   = userId;
        this.tenantId = tenantId;
        this.email    = email;
        this.roles    = roles != null ? roles : List.of();
    }

    public String       getUserId()   { return userId; }
    public String       getTenantId() { return tenantId; }
    public String       getEmail()    { return email; }
    public List<String> getRoles()    { return roles; }

    public boolean hasRole(String role) {
        return roles.stream().anyMatch(r -> r.equalsIgnoreCase(role));
    }

    @Override
    public String toString() {
        return "JwtClaims{userId='" + userId + "', tenantId='" + tenantId + "', roles=" + roles + '}';
    }
}
