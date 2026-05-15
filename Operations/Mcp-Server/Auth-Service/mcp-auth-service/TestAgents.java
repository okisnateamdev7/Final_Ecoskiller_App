package com.ecoskiller.mcp;

import com.ecoskiller.mcp.security.AuthSecurityConfig;
import com.ecoskiller.mcp.tools.AuthToolRouter;

import java.util.*;

/**
 * Integration test suite for the auth-service MCP server.
 *
 * Run:
 *   java -cp mcp-auth-service.jar com.ecoskiller.mcp.TestAgents
 *   java -cp mcp-auth-service.jar com.ecoskiller.mcp.TestAgents --verbose
 *
 * 30 tests:
 *   - All 18 tools
 *   - Security: SQL injection, XSS, wrong role, rate-limit lockout, blacklist
 *   - Token lifecycle: issue → validate → refresh (rotation) → revoke → introspect
 *   - MFA: provision → verify → disable
 *   - OAuth: get code → exchange
 *   - Session: create → list → invalidate-all
 *   - RBAC matrix checks
 *   - Audit log retrieval
 */
public class TestAgents {

    private static final String TENANT = "tenant-001";
    private static boolean verbose = false;
    private static int passed = 0, failed = 0;

    // Shared state
    private static String userId        = null;
    private static String accessToken   = null;
    private static String refreshToken  = null;
    private static String sessionId     = null;
    private static String adminToken    = null;
    private static String oauthCode     = null;

    private static final AuthToolRouter ROUTER = new AuthToolRouter();

    public static void main(String[] args) {
        verbose = Arrays.asList(args).contains("--verbose");
        AuthSecurityConfig.init();

        System.out.println("================================================");
        System.out.println(" Ecoskiller Auth-Service MCP Server Tests       ");
        System.out.println(" 18 Tools | Namespace: core | Tenant: " + TENANT);
        System.out.println("================================================");
        System.out.println();

        // ── Identity ──────────────────────────────────────────────────────────
        run("1.  register_user (RECRUITER)",           test_registerUser());
        run("2.  register_user (ADMIN)",                test_registerAdmin());
        run("3.  register_user (duplicate email)",      test_registerDuplicate());
        run("4.  register_user (weak password)",        test_weakPassword());
        run("5.  login (success)",                      test_login());
        run("6.  login (wrong password)",               test_loginWrongPassword());
        run("7.  login (rate-limit lockout)",           test_rateLimitLockout());

        // ── Tokens ────────────────────────────────────────────────────────────
        run("8.  validate_token (valid)",               test_validateToken());
        run("9.  validate_token (with role assert)",    test_validateTokenRole());
        run("10. introspect_token",                     test_introspectToken());
        run("11. refresh_token (rotation)",             test_refreshToken());
        run("12. revoke_token",                         test_revokeToken());
        run("13. validate_token (after revoke → INVALID)", test_validateAfterRevoke());

        // ── MFA ───────────────────────────────────────────────────────────────
        run("14. enable_mfa (TOTP provisioning)",       test_enableMfa());
        run("15. verify_mfa (invalid code → failure)",  test_verifyMfaInvalid());
        run("16. disable_mfa (password mismatch → reject)", test_disableMfaWrongPassword());

        // ── OAuth 2.0 ─────────────────────────────────────────────────────────
        run("17. get_oauth_authorization_code",         test_getAuthCode());
        run("18. exchange_oauth_code",                  test_exchangeCode());
        run("19. exchange_oauth_code (reuse → reject)", test_reuseCode());

        // ── Session ───────────────────────────────────────────────────────────
        run("20. get_user_session",                     test_getSession());
        run("21. list_user_sessions",                   test_listSessions());
        run("22. invalidate_all_sessions",              test_invalidateAll());

        // ── RBAC ──────────────────────────────────────────────────────────────
        run("23. check_permission RECRUITER grade assessment → PERMITTED", test_rbacPermitted());
        run("24. check_permission CANDIDATE delete job → DENIED",          test_rbacDenied());
        run("25. reset_password (self-service)",        test_resetPassword());

        // ── Audit ─────────────────────────────────────────────────────────────
        run("26. get_security_audit_log",               test_getAuditLog());

        // ── Security edge cases ───────────────────────────────────────────────
        run("27. Security: SQL injection in email",     test_sqlInjection());
        run("28. Security: XSS in display_name",        test_xssInjection());
        run("29. Security: CANDIDATE cannot get audit", test_wrongRoleAudit());
        run("30. Security: expired/invalid token",      test_invalidToken());

        System.out.println();
        System.out.println("================================================");
        System.out.printf("  PASSED: %d  |  FAILED: %d  |  TOTAL: %d%n", passed, failed, passed + failed);
        System.out.println("================================================");
        System.exit(failed > 0 ? 1 : 0);
    }

    // =========================================================================
    // Tests
    // =========================================================================

    static TestResult test_registerUser() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("tenant_id",    TENANT);
        args.put("email",        "recruiter@ecoskiller.dev");
        args.put("password",     "SecureP@ss1");
        args.put("role",         "RECRUITER");
        args.put("display_name", "Test Recruiter");
        args.put("ip_address",   "10.0.0.1");
        String r = call("register_user", args);
        boolean ok = r.contains("CREATED") && r.contains("user_id");
        if (ok) userId = extract(r, "user_id");
        return ok(ok, r);
    }

    static TestResult test_registerAdmin() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("tenant_id", TENANT);
        args.put("email",     "admin@ecoskiller.dev");
        args.put("password",  "AdminP@ss1");
        args.put("role",      "ADMIN");
        args.put("ip_address","10.0.0.1");
        String r = call("register_user", args);
        // Login as admin to get admin token
        if (r.contains("CREATED")) {
            Map<String, Object> la = new LinkedHashMap<>();
            la.put("tenant_id","tenant-001"); la.put("email","admin@ecoskiller.dev");
            la.put("password","AdminP@ss1");
            String lr = call("login", la);
            if (lr.contains("access_token")) adminToken = extract(lr, "access_token");
        }
        return ok(r.contains("CREATED"), r);
    }

    static TestResult test_registerDuplicate() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("tenant_id","tenant-001");
        args.put("email",    "recruiter@ecoskiller.dev"); // same email
        args.put("password", "SecureP@ss1");
        args.put("role",     "CANDIDATE");
        try {
            call("register_user", args);
            return ok(false, "Should have rejected duplicate email");
        } catch (IllegalArgumentException e) {
            return ok(e.getMessage().contains("already registered"), "Correctly blocked: " + e.getMessage());
        }
    }

    static TestResult test_weakPassword() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("tenant_id","tenant-001");
        args.put("email",    "new@test.dev");
        args.put("password", "weak");
        args.put("role",     "CANDIDATE");
        try {
            call("register_user", args);
            return ok(false, "Should have rejected weak password");
        } catch (IllegalArgumentException e) {
            // Any password validation rejection is correct
            return ok(true, "Correctly rejected: " + e.getMessage());
        }
    }

    static TestResult test_login() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("tenant_id","tenant-001");
        args.put("email",    "recruiter@ecoskiller.dev");
        args.put("password", "SecureP@ss1");
        args.put("ip_address","10.0.0.1");
        args.put("user_agent","Mozilla/5.0");
        String r = call("login", args);
        boolean ok = r.contains("access_token") && r.contains("session_id");
        if (ok) {
            accessToken  = extract(r, "access_token");
            refreshToken = extract(r, "refresh_token");
            sessionId    = extract(r, "session_id");
        }
        return ok(ok, r);
    }

    static TestResult test_loginWrongPassword() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("tenant_id","tenant-001");
        args.put("email",    "recruiter@ecoskiller.dev");
        args.put("password", "WrongPass99");
        try {
            call("login", args);
            return ok(false, "Should have rejected wrong password");
        } catch (SecurityException e) {
            return ok(e.getMessage().contains("Invalid credentials"), "Correctly rejected: " + e.getMessage());
        }
    }

    static TestResult test_rateLimitLockout() {
        // Register a fresh user and hammer login attempts
        Map<String, Object> reg = new LinkedHashMap<>();
        reg.put("tenant_id","tenant-001"); reg.put("email","locktest@ecoskiller.dev");
        reg.put("password","LockP@ss1"); reg.put("role","CANDIDATE");
        call("register_user", reg);

        // 5 failed attempts → should lock
        for (int i = 0; i < 5; i++) {
            try {
                Map<String, Object> la = new LinkedHashMap<>();
                la.put("tenant_id","tenant-001"); la.put("email","locktest@ecoskiller.dev");
                la.put("password","WrongPass!!"); call("login", la);
            } catch (SecurityException ignored) {}
        }
        // 6th attempt should be locked
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("tenant_id","tenant-001"); args.put("email","locktest@ecoskiller.dev");
        args.put("password","WrongPass!!");
        try {
            call("login", args);
            return ok(false, "Should be locked");
        } catch (SecurityException e) {
            return ok(e.getMessage().contains("locked"), "Correctly locked: " + e.getMessage());
        }
    }

    static TestResult test_validateToken() {
        if (accessToken == null) return TestResult.skip("No access token");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token", accessToken);
        args.put("tenant_id",    TENANT);
        String r = call("validate_token", args);
        return ok(r.contains("VALID") && r.contains("\"valid\":true"), r);
    }

    static TestResult test_validateTokenRole() {
        if (accessToken == null) return TestResult.skip("No access token");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token", accessToken);
        args.put("tenant_id",    TENANT);
        args.put("required_role","RECRUITER");
        String r = call("validate_token", args);
        return ok(r.contains("VALID"), r);
    }

    static TestResult test_introspectToken() {
        if (accessToken == null) return TestResult.skip("No access token");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("token",     accessToken);
        args.put("tenant_id", TENANT);
        args.put("client_id", "ecoskiller-dashboard");
        String r = call("introspect_token", args);
        return ok(r.contains("\"active\":true") && r.contains("token_type"), r);
    }

    static TestResult test_refreshToken() {
        if (refreshToken == null) return TestResult.skip("No refresh token");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("refresh_token", refreshToken);
        args.put("tenant_id",     TENANT);
        args.put("ip_address",    "10.0.0.1");
        String r = call("refresh_token", args);
        boolean ok = r.contains("access_token") && r.contains("refresh_token");
        if (ok) {
            accessToken  = extract(r, "access_token");
            refreshToken = extract(r, "refresh_token");
        }
        return ok(ok, r);
    }

    static TestResult test_revokeToken() {
        if (accessToken == null || adminToken == null) return TestResult.skip("No tokens");
        // Revoke with admin token
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token",    adminToken);
        args.put("token_to_revoke", accessToken);
        args.put("tenant_id",       TENANT);
        args.put("reason",          "SECURITY_INCIDENT");
        String r = call("revoke_token", args);
        return ok(r.contains("blacklisted") && r.contains("true"), r);
    }

    static TestResult test_validateAfterRevoke() {
        if (accessToken == null) return TestResult.skip("No access token");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token", accessToken);
        args.put("tenant_id",    TENANT);
        String r = call("validate_token", args);
        // After revocation, token should be invalid (blacklisted)
        return ok(r.contains("INVALID") || r.contains("revoked"), r);
    }

    static TestResult test_enableMfa() {
        // Re-login to get a fresh token
        Map<String, Object> la = new LinkedHashMap<>();
        la.put("tenant_id","tenant-001"); la.put("email","recruiter@ecoskiller.dev");
        la.put("password","SecureP@ss1");
        String lr = call("login", la);
        if (lr.contains("access_token")) accessToken = extract(lr, "access_token");

        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token", accessToken);
        args.put("tenant_id",    TENANT);
        args.put("mfa_type",     "TOTP");
        String r = call("enable_mfa", args);
        return ok(r.contains("totp_secret") && r.contains("qr_provisioning_uri")
                && r.contains("backup_codes"), r);
    }

    static TestResult test_verifyMfaInvalid() {
        if (accessToken == null) return TestResult.skip("No access token");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token", accessToken);
        args.put("tenant_id",    TENANT);
        args.put("mfa_code",     "000000"); // invalid code
        args.put("mfa_type",     "TOTP");
        try {
            call("verify_mfa", args);
            return ok(false, "Should reject invalid TOTP code");
        } catch (SecurityException e) {
            return ok(e.getMessage().contains("Invalid MFA") || e.getMessage().contains("attempt"),
                    "Correctly rejected: " + e.getMessage());
        }
    }

    static TestResult test_disableMfaWrongPassword() {
        if (accessToken == null) return TestResult.skip("No access token");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token",     accessToken);
        args.put("tenant_id",        TENANT);
        args.put("current_password", "WrongPass99");
        args.put("mfa_code",         "123456");
        try {
            call("disable_mfa", args);
            return ok(false, "Should reject wrong password or unverified MFA");
        } catch (SecurityException e) {
            return ok(e.getMessage().contains("password") || e.getMessage().contains("incorrect"),
                    "Correctly rejected (wrong password): " + e.getMessage());
        } catch (IllegalStateException e) {
            // MFA device exists but not yet verified — this is also a correct security guard
            return ok(e.getMessage().contains("not currently enabled"),
                    "Correctly rejected (MFA not verified yet): " + e.getMessage());
        }
    }

    static TestResult test_getAuthCode() {
        if (accessToken == null) return TestResult.skip("No access token");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token", accessToken);
        args.put("tenant_id",    TENANT);
        args.put("client_id",    "ecoskiller-dashboard");
        args.put("redirect_uri", "https://dashboard.ecoskiller.com/callback");
        args.put("scope",        "openid profile view-assessments");
        args.put("state",        "csrf-abc-123");
        String r = call("get_oauth_authorization_code", args);
        boolean ok = r.contains("code") && r.contains("redirect_uri");
        if (ok) oauthCode = extract(r, "code");
        return ok(ok, r);
    }

    static TestResult test_exchangeCode() {
        if (oauthCode == null) return TestResult.skip("No oauth code");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("code",          oauthCode);
        args.put("client_id",     "ecoskiller-dashboard");
        args.put("client_secret", "ecoskiller-client-secret");
        args.put("redirect_uri",  "https://dashboard.ecoskiller.com/callback");
        args.put("tenant_id",     TENANT);
        String r = call("exchange_oauth_code", args);
        return ok(r.contains("access_token") && r.contains("refresh_token"), r);
    }

    static TestResult test_reuseCode() {
        if (oauthCode == null) return TestResult.skip("No oauth code");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("code",      oauthCode); // same code — already used
        args.put("client_id", "ecoskiller-dashboard");
        args.put("tenant_id", TENANT);
        try {
            call("exchange_oauth_code", args);
            return ok(false, "Should reject already-used code");
        } catch (SecurityException e) {
            return ok(e.getMessage().contains("already used"), "Correctly rejected: " + e.getMessage());
        }
    }

    static TestResult test_getSession() {
        if (adminToken == null || sessionId == null) return TestResult.skip("No session/admin token");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token", adminToken);
        args.put("tenant_id",    TENANT);
        args.put("session_id",   sessionId);
        String r = call("get_user_session", args);
        return ok(r.contains("session_id") || r.contains("user_id"), r);
    }

    static TestResult test_listSessions() {
        if (adminToken == null || userId == null) return TestResult.skip("No admin token");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token",   adminToken);
        args.put("tenant_id",      TENANT);
        args.put("target_user_id", userId);
        String r = call("list_user_sessions", args);
        return ok(r.contains("sessions") && r.contains("session_count"), r);
    }

    static TestResult test_invalidateAll() {
        if (adminToken == null || userId == null) return TestResult.skip("No admin token");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token",   adminToken);
        args.put("tenant_id",      TENANT);
        args.put("target_user_id", userId);
        args.put("reason",         "SECURITY_INCIDENT");
        String r = call("invalidate_all_sessions", args);
        return ok(r.contains("sessions_invalidated"), r);
    }

    static TestResult test_rbacPermitted() {
        if (accessToken == null) {
            // Re-login
            Map<String, Object> la = new LinkedHashMap<>();
            la.put("tenant_id","tenant-001"); la.put("email","recruiter@ecoskiller.dev");
            la.put("password","SecureP@ss1");
            String lr = call("login", la);
            if (lr.contains("access_token")) accessToken = extract(lr, "access_token");
        }
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token", accessToken);
        args.put("tenant_id",    TENANT);
        args.put("resource",     "assessment");
        args.put("action",       "grade");
        String r = call("check_permission", args);
        return ok(r.contains("PERMITTED"), r);
    }

    static TestResult test_rbacDenied() {
        Map<String, Object> regArgs = new LinkedHashMap<>();
        regArgs.put("tenant_id","tenant-001"); regArgs.put("email","cand@ecoskiller.dev");
        regArgs.put("password","CandP@ss1"); regArgs.put("role","CANDIDATE");
        call("register_user", regArgs);
        Map<String, Object> la = new LinkedHashMap<>();
        la.put("tenant_id","tenant-001"); la.put("email","cand@ecoskiller.dev"); la.put("password","CandP@ss1");
        String lr = call("login", la);
        String candToken = extract(lr, "access_token");

        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token", candToken);
        args.put("tenant_id",    TENANT);
        args.put("resource",     "job");
        args.put("action",       "delete");
        String r = call("check_permission", args);
        return ok(r.contains("DENIED"), r);
    }

    static TestResult test_resetPassword() {
        // Re-login to get fresh token
        Map<String, Object> la = new LinkedHashMap<>();
        la.put("tenant_id","tenant-001"); la.put("email","recruiter@ecoskiller.dev"); la.put("password","SecureP@ss1");
        String lr = call("login", la);
        String freshToken = extract(lr, "access_token");
        String freshUserId = extract(lr, "user_id");

        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token",     freshToken);
        args.put("tenant_id",        TENANT);
        args.put("target_user_id",   freshUserId);
        args.put("current_password", "SecureP@ss1");
        args.put("new_password",     "NewSecureP@ss2");
        args.put("ip_address",       "10.0.0.1");
        String r = call("reset_password", args);
        return ok(r.contains("sessions_invalidated"), r);
    }

    static TestResult test_getAuditLog() {
        if (adminToken == null) return TestResult.skip("No admin token");
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token", adminToken);
        args.put("tenant_id",    TENANT);
        args.put("limit",        "50");
        String r = call("get_security_audit_log", args);
        return ok(r.contains("entries") && r.contains("compliance_note"), r);
    }

    static TestResult test_sqlInjection() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("tenant_id","tenant-001");
        args.put("email",    "'; DROP TABLE auth_users; --@test.com");
        args.put("password", "SecureP@ss1");
        args.put("role",     "CANDIDATE");
        try {
            call("register_user", args);
            return ok(false, "Should have rejected SQL injection");
        } catch (SecurityException e) {
            return ok(e.getMessage().toLowerCase().contains("injection"), "Blocked: " + e.getMessage());
        }
    }

    static TestResult test_xssInjection() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("tenant_id",    "tenant-001");
        args.put("email",        "xss@test.dev");
        args.put("password",     "SecureP@ss1");
        args.put("role",         "CANDIDATE");
        args.put("display_name", "<script>alert('xss')</script>");
        try {
            call("register_user", args);
            return ok(false, "Should have rejected XSS");
        } catch (SecurityException e) {
            return ok(e.getMessage().toLowerCase().contains("injection"), "Blocked: " + e.getMessage());
        }
    }

    static TestResult test_wrongRoleAudit() {
        // Register and login as CANDIDATE
        Map<String, Object> rr = new LinkedHashMap<>();
        rr.put("tenant_id","tenant-001"); rr.put("email","cand2@ecoskiller.dev");
        rr.put("password","CandP@ss2"); rr.put("role","CANDIDATE");
        call("register_user", rr);
        Map<String, Object> la = new LinkedHashMap<>();
        la.put("tenant_id","tenant-001"); la.put("email","cand2@ecoskiller.dev"); la.put("password","CandP@ss2");
        String lr = call("login", la);
        String candToken = extract(lr, "access_token");

        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token", candToken);
        args.put("tenant_id",    TENANT);
        try {
            call("get_security_audit_log", args);
            return ok(false, "CANDIDATE should not access audit log");
        } catch (SecurityException e) {
            return ok(e.getMessage().contains("Access denied"), "Correctly blocked: " + e.getMessage());
        }
    }

    static TestResult test_invalidToken() {
        Map<String, Object> args = new LinkedHashMap<>();
        args.put("access_token", "eyJhbGciOiJIUzI1NiJ9.totally.invalid");
        args.put("tenant_id",    TENANT);
        String r = call("validate_token", args);
        return ok(r.contains("INVALID"), r);
    }

    // ── Infrastructure ────────────────────────────────────────────────────────

    private static String call(String tool, Map<String, Object> args) {
        return ROUTER.dispatch(tool, args);
    }

    private static void run(String name, TestResult result) {
        String icon = result.skipped ? "⏭ " : (result.passed ? "✅" : "❌");
        System.out.printf("  %s  %-52s %s%n", icon, name,
                result.skipped ? "SKIPPED" : (result.passed ? "PASS" : "FAIL"));
        if (verbose || (!result.passed && !result.skipped)) {
            String out = result.output != null && result.output.length() > 250
                    ? result.output.substring(0, 250) + "..." : result.output;
            System.out.println("         " + out);
        }
        if (!result.skipped) { if (result.passed) passed++; else failed++; }
    }

    private static TestResult ok(boolean passed, String output) {
        return new TestResult(passed, output);
    }

    private static String extract(String json, String field) {
        String key = "\"" + field + "\":\"";
        int i = json.indexOf(key);
        if (i < 0) return "unknown-" + UUID.randomUUID();
        int s = i + key.length(), e = json.indexOf('"', s);
        return e > s ? json.substring(s, e) : "unknown";
    }

    static class TestResult {
        final boolean passed, skipped; final String output;
        TestResult(boolean p, String o) { passed=p; skipped=false; output=o!=null?o:""; }
        TestResult(boolean p, String o, boolean s) { passed=p; skipped=s; output=o!=null?o:""; }
        static TestResult skip(String r) { return new TestResult(false, r, true); }
    }
}
