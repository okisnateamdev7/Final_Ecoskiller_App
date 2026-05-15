package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.KafkaEventPublisher;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.AuthSecurityConfig;

import java.time.Instant;
import java.util.*;

/** Tools: refresh_token, validate_token, revoke_token, introspect_token */
public class TokenTools {

    private final KafkaEventPublisher kafka = KafkaEventPublisher.getInstance();

    // ─────────────────────────────────────────────────────────────────────────
    // refresh_token
    // ─────────────────────────────────────────────────────────────────────────

    public String refreshToken(Map<String, Object> args) {
        String refreshTokenRaw = AuthSecurityConfig.requireString(args, "refresh_token");
        String tenantId        = AuthSecurityConfig.requireString(args, "tenant_id");
        String ipAddress       = AuthSecurityConfig.getString(args, "ip_address");

        // Validate refresh token
        Map<String, Object> claims;
        try {
            claims = AuthSecurityConfig.validateToken(refreshTokenRaw);
        } catch (SecurityException e) {
            AuthSecurityConfig.audit("REFRESH_FAILED", "unknown", tenantId, ipAddress,
                    "reason=" + e.getMessage(), false);
            throw new SecurityException("Refresh token invalid: " + e.getMessage());
        }

        // Must be a refresh token type
        if (!"refresh".equals(claims.get("type")))
            throw new SecurityException("Provided token is not a refresh token");

        String userId = (String) claims.getOrDefault("sub", "");

        // Lookup user for fresh role data
        Optional<Map<String, Object>> userOpt = AuthSecurityConfig.findUserById(userId);
        if (userOpt.isEmpty()) throw new SecurityException("User not found: " + userId);
        Map<String, Object> user = userOpt.get();

        // Invalidate old refresh token (rotation — prevents reuse)
        AuthSecurityConfig.blacklistToken(refreshTokenRaw);
        Map<String, Map<String, Object>> refreshStore = AuthSecurityConfig.getRefreshStore();
        refreshStore.remove(refreshTokenRaw);

        // Issue new tokens
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) user.getOrDefault("roles", List.of("CANDIDATE"));
        String email        = (String) user.getOrDefault("email", userId + "@ecoskiller.dev");
        String newAccess    = AuthSecurityConfig.issueAccessToken(userId, tenantId, email, roles, null);
        String newRefresh   = AuthSecurityConfig.issueRefreshToken(userId, tenantId);

        AuthSecurityConfig.audit("TOKEN_REFRESHED", userId, tenantId, ipAddress, "rotation=true", true);

        return resp("OK", "Tokens refreshed successfully (refresh token rotated)", Map.of(
                "access_token",  newAccess,
                "refresh_token", newRefresh,
                "token_type",    "Bearer",
                "expires_in",    AuthSecurityConfig.getAccessTokenTtl(),
                "user_id",       userId));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // validate_token
    // ─────────────────────────────────────────────────────────────────────────

    public String validateToken(Map<String, Object> args) {
        String accessToken  = AuthSecurityConfig.requireString(args, "access_token");
        String tenantId     = AuthSecurityConfig.requireString(args, "tenant_id");
        String requiredRole = AuthSecurityConfig.getString(args, "required_role");

        try {
            Map<String, Object> claims = AuthSecurityConfig.validateToken(accessToken);

            // Optional tenant isolation check
            String tokenTenant = (String) claims.get("tenant_id");
            if (tokenTenant != null && !tenantId.equals(tokenTenant)
                    && !AuthSecurityConfig.hasRole(claims, AuthSecurityConfig.ROLE_SUPER_ADMIN))
                throw new SecurityException("Tenant mismatch: token=" + tokenTenant + " expected=" + tenantId);

            // Optional role assertion
            if (requiredRole != null && !requiredRole.isBlank()) {
                if (!AuthSecurityConfig.hasRole(claims, requiredRole))
                    throw new SecurityException("Required role not present: " + requiredRole);
            }

            long exp = toLong(claims.get("exp"));
            long now = Instant.now().getEpochSecond();

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("valid",           true);
            data.put("sub",             claims.get("sub"));
            data.put("tenant_id",       claims.get("tenant_id"));
            data.put("email",           claims.get("email"));
            data.put("roles",           claims.get("roles"));
            data.put("jti",             claims.get("jti"));
            data.put("type",            claims.get("type"));
            data.put("expires_at",      exp > 0 ? Instant.ofEpochSecond(exp).toString() : "N/A");
            data.put("seconds_remaining", exp > 0 ? Math.max(0, exp - now) : -1);
            data.put("blacklisted",     false);

            return resp("VALID", "Token is valid", data);

        } catch (SecurityException e) {
            return resp("INVALID", "Token validation failed: " + e.getMessage(), Map.of(
                    "valid",  false,
                    "error",  e.getMessage(),
                    "action", "Re-authenticate or refresh token"));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // revoke_token
    // ─────────────────────────────────────────────────────────────────────────

    public String revokeToken(Map<String, Object> args) {
        String callerToken   = AuthSecurityConfig.requireString(args, "access_token");
        String tokenToRevoke = AuthSecurityConfig.requireString(args, "token_to_revoke");
        String tenantId      = AuthSecurityConfig.requireString(args, "tenant_id");
        String reason        = AuthSecurityConfig.sanitise(
                AuthSecurityConfig.getString(args, "reason"), "reason");

        // Authenticate caller
        Map<String, Object> callerClaims = AuthSecurityConfig.validateToken(callerToken);
        String callerId  = (String) callerClaims.getOrDefault("sub", "");
        boolean isAdmin  = AuthSecurityConfig.hasRole(callerClaims, AuthSecurityConfig.ROLE_ADMIN)
                        || AuthSecurityConfig.hasRole(callerClaims, AuthSecurityConfig.ROLE_SUPER_ADMIN);

        // Decode token to revoke (for ownership check)
        String targetUserId = "unknown";
        try {
            String[] parts = tokenToRevoke.split("\\.");
            if (parts.length == 3) {
                String payload = AuthSecurityConfig.decodeB64(parts[1]);
                int si = payload.indexOf("\"sub\":\"");
                if (si >= 0) { int s = si + 7; int e = payload.indexOf('"', s); targetUserId = payload.substring(s, e); }
            }
        } catch (Exception ignored) {}

        // RBAC: users can only revoke their own tokens
        if (!isAdmin && !callerId.equals(targetUserId))
            throw new SecurityException("Cannot revoke another user's token without ADMIN role");

        // Blacklist the token
        AuthSecurityConfig.blacklistToken(tokenToRevoke);
        // Also remove from refresh store if it's a refresh token
        AuthSecurityConfig.getRefreshStore().remove(tokenToRevoke);

        String revokeReason = reason != null ? reason : "LOGOUT";
        AuthSecurityConfig.audit("TOKEN_REVOKED", targetUserId, tenantId, null,
                "by=" + callerId + " reason=" + revokeReason, true);
        kafka.publish("auth.token_revoked", Map.of(
                "user_id",   targetUserId,
                "tenant_id", tenantId,
                "revoked_by",callerId,
                "reason",    revokeReason));

        return resp("OK", "Token revoked and blacklisted", Map.of(
                "target_user_id", targetUserId,
                "revoked_by",     callerId,
                "reason",         revokeReason,
                "blacklisted",    true));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // introspect_token  (RFC 7662)
    // ─────────────────────────────────────────────────────────────────────────

    public String introspectToken(Map<String, Object> args) {
        String token    = AuthSecurityConfig.requireString(args, "token");
        String tenantId = AuthSecurityConfig.requireString(args, "tenant_id");
        String clientId = AuthSecurityConfig.getString(args, "client_id");

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("client_id", clientId);

        try {
            Map<String, Object> claims = AuthSecurityConfig.validateToken(token);
            long exp = toLong(claims.get("exp"));
            long now = Instant.now().getEpochSecond();

            data.put("active",     true);
            data.put("sub",        claims.get("sub"));
            data.put("tenant_id",  claims.get("tenant_id"));
            data.put("email",      claims.get("email"));
            data.put("roles",      claims.get("roles"));
            data.put("scope",      "openid profile email");   // default Ecoskiller scopes
            data.put("token_type", "access_token");
            data.put("exp",        exp);
            data.put("iat",        claims.get("iat"));
            data.put("jti",        claims.get("jti"));
            data.put("seconds_remaining", exp > 0 ? Math.max(0, exp - now) : -1);
            return resp("OK", "Token is active", data);

        } catch (SecurityException e) {
            // Per RFC 7662: inactive token returns { active: false } not an error
            data.put("active", false);
            data.put("reason", e.getMessage());
            return resp("OK", "Token is inactive", data);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private long toLong(Object v) {
        if (v == null) return -1;
        try { return Long.parseLong(v.toString()); } catch (NumberFormatException e) { return -1; }
    }

    private String resp(String status, String message, Object data) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("status", status); r.put("message", message); r.put("data", data);
        r.put("service", "auth-service");
        return JsonUtil.toJson(r);
    }
}
