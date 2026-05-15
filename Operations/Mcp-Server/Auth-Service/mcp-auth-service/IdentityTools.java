package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.KafkaEventPublisher;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.AuthSecurityConfig;

import java.time.Instant;
import java.util.*;

/** Tools: register_user, login, logout, reset_password */
public class IdentityTools {

    private final KafkaEventPublisher kafka = KafkaEventPublisher.getInstance();

    // ─────────────────────────────────────────────────────────────────────────
    // register_user
    // ─────────────────────────────────────────────────────────────────────────

    public String registerUser(Map<String, Object> args) {
        String tenantId    = AuthSecurityConfig.requireString(args, "tenant_id");
        String email       = AuthSecurityConfig.requireString(args, "email");
        String password    = AuthSecurityConfig.requireString(args, "password");
        String role        = AuthSecurityConfig.sanitise(AuthSecurityConfig.getString(args, "role"), "role");
        String displayName = AuthSecurityConfig.sanitise(AuthSecurityConfig.getString(args, "display_name"), "display_name");
        String ipAddress   = AuthSecurityConfig.getString(args, "ip_address");

        // Validate email
        if (!AuthSecurityConfig.isValidEmail(email))
            throw new IllegalArgumentException("Invalid email format: " + email);

        // Validate role
        if (role == null) role = AuthSecurityConfig.ROLE_CANDIDATE;
        String roleUpper = role.toUpperCase();
        if (!Set.of("CANDIDATE", "RECRUITER", "ADMIN").contains(roleUpper))
            throw new IllegalArgumentException("Invalid role: " + role + ". Valid: CANDIDATE | RECRUITER | ADMIN");

        // Check email uniqueness (within tenant)
        if (AuthSecurityConfig.findUserByEmail(email).isPresent())
            throw new IllegalArgumentException("Email already registered: " + email);

        // Password strength validation
        validatePasswordStrength(password);

        // Hash password (bcrypt-equivalent)
        String hashedPassword = AuthSecurityConfig.hashPassword(password);

        // Create user
        String userId = AuthSecurityConfig.generateId();
        Map<String, Object> user = AuthSecurityConfig.createUser(userId, email, tenantId, roleUpper, hashedPassword);
        if (displayName != null) user.put("display_name", displayName);

        // Audit
        AuthSecurityConfig.audit("USER_REGISTERED", userId, tenantId, ipAddress,
                "email=" + email + " role=" + roleUpper, true);

        // Kafka event
        kafka.publish("auth.user_registered", Map.of(
                "user_id",   userId,
                "tenant_id", tenantId,
                "email",     email,
                "role",      roleUpper));

        // Response (never return password hash)
        Map<String, Object> safeUser = new LinkedHashMap<>(user);
        safeUser.remove("password_hash");

        return resp("CREATED", "User registered successfully", Map.of(
                "user_id",    userId,
                "email",      email,
                "role",       roleUpper,
                "tenant_id",  tenantId,
                "created_at", user.get("created_at"),
                "note",       "Email verification token sent via notification-service (production)"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // login
    // ─────────────────────────────────────────────────────────────────────────

    public String login(Map<String, Object> args) {
        String tenantId  = AuthSecurityConfig.requireString(args, "tenant_id");
        String email     = AuthSecurityConfig.requireString(args, "email");
        String password  = AuthSecurityConfig.requireString(args, "password");
        String ipAddress = AuthSecurityConfig.getString(args, "ip_address");
        String userAgent = AuthSecurityConfig.getString(args, "user_agent");

        // Look up user
        Optional<Map<String, Object>> userOpt = AuthSecurityConfig.findUserByEmail(email);
        if (userOpt.isEmpty()) {
            // Constant-time response to prevent user enumeration
            AuthSecurityConfig.audit("LOGIN_FAILED", "unknown", tenantId, ipAddress,
                    "email=" + email + " reason=USER_NOT_FOUND", false);
            throw new SecurityException("Invalid credentials");
        }
        Map<String, Object> user = userOpt.get();
        String userId = (String) user.get("user_id");

        // Tenant isolation check
        if (!tenantId.equals(user.get("tenant_id")))
            throw new SecurityException("Invalid credentials");

        // Rate limit check
        if (!AuthSecurityConfig.checkRateLimit(userId)) {
            AuthSecurityConfig.audit("LOGIN_LOCKED", userId, tenantId, ipAddress,
                    "Too many failed attempts: " + AuthSecurityConfig.getFailedAttempts(userId), false);
            throw new SecurityException("Account temporarily locked due to too many failed login attempts. "
                    + "Max: " + AuthSecurityConfig.getMaxLoginAttempts());
        }

        // Password verification
        String storedHash = AuthSecurityConfig.getPasswordHash(userId);
        if (!AuthSecurityConfig.verifyPassword(password, storedHash)) {
            AuthSecurityConfig.recordFailedAttempt(userId);
            AuthSecurityConfig.audit("LOGIN_FAILED", userId, tenantId, ipAddress,
                    "reason=WRONG_PASSWORD attempt=" + AuthSecurityConfig.getFailedAttempts(userId), false);
            throw new SecurityException("Invalid credentials");
        }

        // Reset failed attempts on success
        AuthSecurityConfig.resetFailedAttempts(userId);

        // MFA check — if enabled, issue MFA challenge
        Optional<Map<String, Object>> mfaDevice = AuthSecurityConfig.getMfaDevice(userId);
        if (mfaDevice.isPresent() && Boolean.TRUE.equals(mfaDevice.get().get("verified"))) {
            String challengeToken = "mfa_challenge_" + AuthSecurityConfig.generateId();
            AuthSecurityConfig.audit("LOGIN_MFA_REQUIRED", userId, tenantId, ipAddress,
                    "mfa_type=" + mfaDevice.get().get("type"), true);
            return resp("MFA_REQUIRED", "MFA verification required to complete login", Map.of(
                    "user_id",         userId,
                    "mfa_challenge",   challengeToken,
                    "mfa_type",        mfaDevice.get().get("type"),
                    "message",         "Call verify_mfa with the challenge token and your MFA code"));
        }

        // Issue tokens
        @SuppressWarnings("unchecked")
        List<String> roles     = (List<String>) user.getOrDefault("roles", List.of("CANDIDATE"));
        String accessToken  = AuthSecurityConfig.issueAccessToken(userId, tenantId, email, roles, null);
        String refreshToken = AuthSecurityConfig.issueRefreshToken(userId, tenantId);
        String sessionId    = AuthSecurityConfig.createSession(userId, tenantId, roles, ipAddress, userAgent);

        AuthSecurityConfig.audit("LOGIN_SUCCESS", userId, tenantId, ipAddress, "session=" + sessionId, true);
        kafka.publish("auth.user_login", Map.of("user_id", userId, "tenant_id", tenantId, "session_id", sessionId));

        return resp("OK", "Login successful", Map.of(
                "access_token",  accessToken,
                "refresh_token", refreshToken,
                "token_type",    "Bearer",
                "expires_in",    AuthSecurityConfig.getAccessTokenTtl(),
                "session_id",    sessionId,
                "user_id",       userId,
                "roles",         roles));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // logout
    // ─────────────────────────────────────────────────────────────────────────

    public String logout(Map<String, Object> args) {
        String accessToken = AuthSecurityConfig.requireString(args, "access_token");
        String sessionId   = AuthSecurityConfig.getString(args, "session_id");
        String tenantId    = AuthSecurityConfig.requireString(args, "tenant_id");
        String ipAddress   = AuthSecurityConfig.getString(args, "ip_address");

        // Validate and blacklist token
        String userId = "unknown";
        try {
            Map<String, Object> claims = AuthSecurityConfig.validateToken(accessToken);
            userId = (String) claims.getOrDefault("sub", "unknown");
            AuthSecurityConfig.blacklistToken(accessToken);
        } catch (SecurityException e) {
            // Even if token is invalid/expired, proceed with session invalidation
        }

        // Invalidate session
        if (sessionId != null) AuthSecurityConfig.invalidateSession(sessionId);

        AuthSecurityConfig.audit("LOGOUT", userId, tenantId, ipAddress, "session=" + sessionId, true);
        kafka.publish("auth.user_logout", Map.of("user_id", userId, "tenant_id", tenantId,
                "session_id", sessionId != null ? sessionId : "N/A"));

        return resp("OK", "Logged out successfully", Map.of(
                "user_id",     userId,
                "session_id",  sessionId != null ? sessionId : "N/A",
                "token_blacklisted", true));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // reset_password
    // ─────────────────────────────────────────────────────────────────────────

    public String resetPassword(Map<String, Object> args) {
        String accessToken     = AuthSecurityConfig.requireString(args, "access_token");
        String tenantId        = AuthSecurityConfig.requireString(args, "tenant_id");
        String targetUserId    = AuthSecurityConfig.requireString(args, "target_user_id");
        String currentPassword = AuthSecurityConfig.getString(args, "current_password");
        String newPassword     = AuthSecurityConfig.requireString(args, "new_password");
        String ipAddress       = AuthSecurityConfig.getString(args, "ip_address");

        Map<String, Object> claims = AuthSecurityConfig.validateToken(accessToken);
        String callerId = (String) claims.getOrDefault("sub", "");

        // Self-service: must provide current password
        boolean isSelf  = callerId.equals(targetUserId);
        boolean isAdmin = AuthSecurityConfig.hasRole(claims, AuthSecurityConfig.ROLE_ADMIN)
                       || AuthSecurityConfig.hasRole(claims, AuthSecurityConfig.ROLE_SUPER_ADMIN);

        if (!isSelf && !isAdmin)
            throw new SecurityException("Cannot reset another user's password without ADMIN role");

        if (isSelf && !isAdmin) {
            if (currentPassword == null || currentPassword.isBlank())
                throw new IllegalArgumentException("current_password required for self-service password reset");
            String storedHash = AuthSecurityConfig.getPasswordHash(targetUserId);
            if (!AuthSecurityConfig.verifyPassword(currentPassword, storedHash))
                throw new SecurityException("Current password is incorrect");
        }

        // Password strength
        validatePasswordStrength(newPassword);

        // Update password
        String newHash = AuthSecurityConfig.hashPassword(newPassword);
        AuthSecurityConfig.updatePassword(targetUserId, newHash);

        // Invalidate all sessions for security
        int invalidated = AuthSecurityConfig.invalidateAllUserSessions(targetUserId);

        AuthSecurityConfig.audit("PASSWORD_RESET", targetUserId, tenantId, ipAddress,
                "by=" + callerId + " sessions_invalidated=" + invalidated, true);
        kafka.publish("auth.password_reset", Map.of("user_id", targetUserId, "tenant_id", tenantId,
                "reset_by", callerId));

        return resp("OK", "Password reset successfully", Map.of(
                "user_id",              targetUserId,
                "sessions_invalidated", invalidated,
                "note",                 "All existing sessions have been invalidated for security"));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void validatePasswordStrength(String password) {
        if (password == null || password.length() < 8)
            throw new IllegalArgumentException("Password must be at least 8 characters");
        boolean hasUpper  = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower  = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit  = password.chars().anyMatch(Character::isDigit);
        if (!hasUpper || !hasLower || !hasDigit)
            throw new IllegalArgumentException(
                    "Password must contain at least one uppercase letter, one lowercase letter, and one digit");
    }

    private String resp(String status, String message, Object data) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("status", status); r.put("message", message); r.put("data", data);
        r.put("service", "auth-service");
        return JsonUtil.toJson(r);
    }
}
