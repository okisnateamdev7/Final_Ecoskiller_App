package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.KafkaEventPublisher;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.AuthSecurityConfig;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/** Tools: enable_mfa, verify_mfa, disable_mfa */
public class MfaTools {

    private final KafkaEventPublisher kafka = KafkaEventPublisher.getInstance();
    // MFA failure counter per user (3 failures → device locked)
    private static final Map<String, Integer> MFA_FAILURES = new ConcurrentHashMap<>();

    // ─────────────────────────────────────────────────────────────────────────
    // enable_mfa
    // ─────────────────────────────────────────────────────────────────────────

    public String enableMfa(Map<String, Object> args) {
        String accessToken = AuthSecurityConfig.requireString(args, "access_token");
        String tenantId    = AuthSecurityConfig.requireString(args, "tenant_id");
        String mfaType     = AuthSecurityConfig.sanitise(
                AuthSecurityConfig.getString(args, "mfa_type"), "mfa_type");
        String phoneNumber = AuthSecurityConfig.sanitise(
                AuthSecurityConfig.getString(args, "phone_number"), "phone_number");

        Map<String, Object> claims = AuthSecurityConfig.validateToken(accessToken);
        String userId = (String) claims.getOrDefault("sub", "");
        String email  = (String) claims.getOrDefault("email", userId + "@ecoskiller.dev");

        if (mfaType == null) mfaType = "TOTP";
        String mfaTypeUpper = mfaType.toUpperCase();
        if (!Set.of("TOTP", "SMS").contains(mfaTypeUpper))
            throw new IllegalArgumentException("Invalid mfa_type: " + mfaType + ". Valid: TOTP | SMS");

        if ("SMS".equals(mfaTypeUpper) && (phoneNumber == null || phoneNumber.isBlank()))
            throw new IllegalArgumentException("phone_number required for SMS MFA");

        // Check if MFA already enabled and verified
        Optional<Map<String, Object>> existing = AuthSecurityConfig.getMfaDevice(userId);
        if (existing.isPresent() && Boolean.TRUE.equals(existing.get().get("verified")))
            throw new IllegalStateException("MFA is already enabled for this account. Disable first.");

        // Generate TOTP secret
        Map<String, Object> totpData = AuthSecurityConfig.generateTotpSecret(userId, email);

        // Store device (unverified until verify_mfa called)
        Map<String, Object> device = new LinkedHashMap<>();
        device.put("user_id",      userId);
        device.put("type",         mfaTypeUpper);
        device.put("secret",       totpData.get("totp_secret")); // stored encrypted in prod (Vault)
        device.put("phone_number", phoneNumber);
        device.put("verified",     false);
        device.put("created_at",   Instant.now().toString());
        device.put("backup_codes", totpData.get("backup_codes")); // hashed in prod
        AuthSecurityConfig.storeMfaDevice(userId, device);

        AuthSecurityConfig.audit("MFA_PROVISIONED", userId, tenantId, null,
                "type=" + mfaTypeUpper + " pending_verification=true", true);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("user_id",            userId);
        data.put("mfa_type",           mfaTypeUpper);
        data.put("totp_secret",        totpData.get("totp_secret"));
        data.put("qr_provisioning_uri",totpData.get("qr_provisioning_uri"));
        data.put("backup_codes",       totpData.get("backup_codes"));
        data.put("status",             "PENDING_VERIFICATION");
        data.put("next_step",          "Call verify_mfa with a code from your authenticator app to activate MFA");
        data.put("production_note",    "In production: secret is encrypted and stored in Vault; QR URI sent via secure channel");

        return resp("OK", "MFA provisioned — verification required to activate", data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // verify_mfa
    // ─────────────────────────────────────────────────────────────────────────

    public String verifyMfa(Map<String, Object> args) {
        String accessToken = AuthSecurityConfig.requireString(args, "access_token");
        String tenantId    = AuthSecurityConfig.requireString(args, "tenant_id");
        String mfaCode     = AuthSecurityConfig.requireString(args, "mfa_code");
        String ipAddress   = AuthSecurityConfig.getString(args, "ip_address");

        Map<String, Object> claims = AuthSecurityConfig.validateToken(accessToken);
        String userId = (String) claims.getOrDefault("sub", "");

        Optional<Map<String, Object>> deviceOpt = AuthSecurityConfig.getMfaDevice(userId);
        if (deviceOpt.isEmpty())
            throw new IllegalStateException("No MFA device found. Call enable_mfa first.");
        Map<String, Object> device = deviceOpt.get();

        // Check lock (3 consecutive failures)
        int failures = MFA_FAILURES.getOrDefault(userId, 0);
        if (failures >= 3) {
            AuthSecurityConfig.audit("MFA_LOCKED", userId, tenantId, ipAddress,
                    "failures=" + failures, false);
            throw new SecurityException("MFA device locked after 3 failed attempts. Re-provision required.");
        }

        // Validate code length
        String cleanCode = mfaCode.trim().replaceAll("[^0-9]", "");
        if (cleanCode.length() != 6)
            throw new IllegalArgumentException("MFA code must be exactly 6 digits");

        // Verify TOTP
        String secret = (String) device.get("secret");
        boolean valid = AuthSecurityConfig.verifyTotp(secret, cleanCode);

        if (!valid) {
            MFA_FAILURES.merge(userId, 1, Integer::sum);
            int remaining = 3 - MFA_FAILURES.get(userId);
            AuthSecurityConfig.audit("MFA_VERIFY_FAILED", userId, tenantId, ipAddress,
                    "attempts_remaining=" + remaining, false);
            throw new SecurityException("Invalid MFA code. " + remaining + " attempt(s) remaining before lockout.");
        }

        // Success — reset failures
        MFA_FAILURES.remove(userId);

        boolean wasFirstVerification = !Boolean.TRUE.equals(device.get("verified"));
        device.put("verified",   true);
        device.put("verified_at",Instant.now().toString());
        AuthSecurityConfig.storeMfaDevice(userId, device);

        // Update user record
        AuthSecurityConfig.findUserById(userId).ifPresent(u -> u.put("mfa_enabled", true));

        if (wasFirstVerification) {
            kafka.publish("auth.mfa_enabled", Map.of("user_id", userId, "tenant_id", tenantId,
                    "type", device.get("type")));
        }

        AuthSecurityConfig.audit("MFA_VERIFY_SUCCESS", userId, tenantId, ipAddress,
                "first_verification=" + wasFirstVerification, true);

        // Issue a short-lived MFA session token (for step-up auth flows)
        String mfaSessionToken = "mfa_verified_" + AuthSecurityConfig.generateId();

        return resp("OK", wasFirstVerification ? "MFA activated successfully" : "MFA code verified", Map.of(
                "user_id",          userId,
                "mfa_active",       true,
                "mfa_type",         device.get("type"),
                "mfa_session_token",mfaSessionToken,
                "first_activation", wasFirstVerification));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // disable_mfa
    // ─────────────────────────────────────────────────────────────────────────

    public String disableMfa(Map<String, Object> args) {
        String accessToken     = AuthSecurityConfig.requireString(args, "access_token");
        String tenantId        = AuthSecurityConfig.requireString(args, "tenant_id");
        String currentPassword = AuthSecurityConfig.requireString(args, "current_password");
        String mfaCode         = AuthSecurityConfig.requireString(args, "mfa_code");

        Map<String, Object> claims = AuthSecurityConfig.validateToken(accessToken);
        String userId = (String) claims.getOrDefault("sub", "");

        Optional<Map<String, Object>> deviceOpt = AuthSecurityConfig.getMfaDevice(userId);
        if (deviceOpt.isEmpty() || !Boolean.TRUE.equals(deviceOpt.get().get("verified")))
            throw new IllegalStateException("MFA is not currently enabled for this account");

        // Verify current password (secondary auth)
        String storedHash = AuthSecurityConfig.getPasswordHash(userId);
        if (!AuthSecurityConfig.verifyPassword(currentPassword, storedHash))
            throw new SecurityException("Current password is incorrect");

        // Verify MFA code (proves device is in hand — anti-social-engineering)
        Map<String, Object> device = deviceOpt.get();
        String secret = (String) device.get("secret");
        String cleanCode = mfaCode.trim().replaceAll("[^0-9]", "");
        if (!AuthSecurityConfig.verifyTotp(secret, cleanCode))
            throw new SecurityException("Invalid MFA code — cannot disable MFA without a valid current code");

        // Remove device + backup codes
        AuthSecurityConfig.removeMfaDevice(userId);
        MFA_FAILURES.remove(userId);
        AuthSecurityConfig.findUserById(userId).ifPresent(u -> u.put("mfa_enabled", false));

        AuthSecurityConfig.audit("MFA_DISABLED", userId, tenantId, null, "by=" + userId, true);
        kafka.publish("auth.mfa_disabled", Map.of("user_id", userId, "tenant_id", tenantId));

        return resp("OK", "MFA disabled successfully", Map.of(
                "user_id",    userId,
                "mfa_active", false,
                "note",       "All backup codes have been invalidated"));
    }

    private String resp(String status, String message, Object data) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("status", status); r.put("message", message); r.put("data", data);
        r.put("service", "auth-service");
        return JsonUtil.toJson(r);
    }
}
