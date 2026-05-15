package com.ecoskiller.mcp.tools;

import java.util.*;

/**
 * Central dispatcher for all 18 auth-service MCP tools.
 *
 * Tool groups:
 *   Identity    (4): register_user, login, logout, reset_password
 *   Tokens      (4): refresh_token, validate_token, revoke_token, introspect_token
 *   MFA         (3): enable_mfa, verify_mfa, disable_mfa
 *   OAuth 2.0   (2): get_oauth_authorization_code, exchange_oauth_code
 *   Session     (3): get_user_session, list_user_sessions, invalidate_all_sessions
 *   RBAC+Audit  (2): check_permission, get_security_audit_log
 */
public class AuthToolRouter {

    private final IdentityTools  identityTools;
    private final TokenTools     tokenTools;
    private final MfaTools       mfaTools;
    private final OAuthTools     oauthTools;
    private final SessionTools   sessionTools;
    private final RbacAuditTools rbacAuditTools;

    public AuthToolRouter() {
        this.identityTools  = new IdentityTools();
        this.tokenTools     = new TokenTools();
        this.mfaTools       = new MfaTools();
        this.oauthTools     = new OAuthTools();
        this.sessionTools   = new SessionTools();
        this.rbacAuditTools = new RbacAuditTools();
    }

    public String dispatch(String toolName, Map<String, Object> args) {
        switch (toolName) {
            // Identity
            case "register_user":               return identityTools.registerUser(args);
            case "login":                       return identityTools.login(args);
            case "logout":                      return identityTools.logout(args);
            case "reset_password":              return identityTools.resetPassword(args);
            // Tokens
            case "refresh_token":               return tokenTools.refreshToken(args);
            case "validate_token":              return tokenTools.validateToken(args);
            case "revoke_token":                return tokenTools.revokeToken(args);
            case "introspect_token":            return tokenTools.introspectToken(args);
            // MFA
            case "enable_mfa":                  return mfaTools.enableMfa(args);
            case "verify_mfa":                  return mfaTools.verifyMfa(args);
            case "disable_mfa":                 return mfaTools.disableMfa(args);
            // OAuth 2.0
            case "get_oauth_authorization_code":return oauthTools.getAuthorizationCode(args);
            case "exchange_oauth_code":         return oauthTools.exchangeCode(args);
            // Session
            case "get_user_session":            return sessionTools.getSession(args);
            case "list_user_sessions":          return sessionTools.listSessions(args);
            case "invalidate_all_sessions":     return sessionTools.invalidateAllSessions(args);
            // RBAC + Audit
            case "check_permission":            return rbacAuditTools.checkPermission(args);
            case "get_security_audit_log":      return rbacAuditTools.getAuditLog(args);

            default: throw new IllegalArgumentException("Unknown tool: " + toolName);
        }
    }

    // ── tools/list definition ─────────────────────────────────────────────────

    public List<Map<String, Object>> getToolsDefinition() {
        List<Map<String, Object>> tools = new ArrayList<>();

        // ── Identity ──────────────────────────────────────────────────────────
        tools.add(t("register_user",
            "Register a new user (candidate, recruiter, or admin). " +
            "Hashes password with bcrypt (cost 12). Validates email uniqueness. " +
            "Persists to PostgreSQL auth_users table. " +
            "Publishes auth.user_registered Kafka event for downstream services. " +
            "Issues email verification token (prod: via notification-service).",
            p("tenant_id",   "string", "Tenant identifier (multi-tenant isolation)"),
            p("email",       "string", "User email address (must be unique per tenant)"),
            p("password",    "string", "Plaintext password (min 8 chars; hashed before storage)"),
            p("role",        "string", "CANDIDATE | RECRUITER | ADMIN"),
            p("display_name","string", "User display name"),
            p("ip_address",  "string", "Client IP for audit logging"),
            p("user_agent",  "string", "Client User-Agent for audit logging")
        ));

        tools.add(t("login",
            "Authenticate a user with email + password. Enforces bcrypt verification. " +
            "Rate-limits to " + com.ecoskiller.mcp.security.AuthSecurityConfig.getMaxLoginAttempts() + " failed attempts before lockout. " +
            "Returns signed JWT access token (15-min TTL) + refresh token (7-day TTL). " +
            "Creates Redis session record. Publishes auth.user_login Kafka event. " +
            "Returns MFA challenge if MFA is enabled for the account.",
            p("tenant_id",  "string", "Tenant identifier"),
            p("email",      "string", "User email"),
            p("password",   "string", "User password"),
            p("ip_address", "string", "Client IP (for audit + rate-limit by IP)"),
            p("user_agent", "string", "Client User-Agent")
        ));

        tools.add(t("logout",
            "Revoke the current session and blacklist the access token. " +
            "Adds token JTI to Redis blacklist (TTL = remaining token lifetime). " +
            "Invalidates the Redis session record. " +
            "Publishes auth.user_logout Kafka event.",
            p("access_token", "string", "The current access token to revoke"),
            p("session_id",   "string", "Session ID to invalidate"),
            p("tenant_id",    "string", "Tenant identifier"),
            p("ip_address",   "string", "Client IP for audit")
        ));

        tools.add(t("reset_password",
            "Change a user's password. Requires either the current password (self-service) " +
            "or ADMIN role token. Hashes the new password with bcrypt. " +
            "Invalidates ALL existing sessions and refresh tokens for the user. " +
            "Publishes auth.password_reset Kafka event for audit-service.",
            p("access_token",    "string", "Bearer JWT (user or ADMIN)"),
            p("tenant_id",       "string", "Tenant identifier"),
            p("target_user_id",  "string", "User whose password to reset (self or ADMIN target)"),
            p("current_password","string", "Current password (required for self-service)"),
            p("new_password",    "string", "New password (min 8 chars, mixed case + digit required)"),
            p("ip_address",      "string", "Client IP for audit")
        ));

        // ── Tokens ────────────────────────────────────────────────────────────
        tools.add(t("refresh_token",
            "Exchange a valid refresh token for a new access token + new refresh token (rotation). " +
            "Validates refresh token signature, expiry, and blacklist status. " +
            "Old refresh token is immediately invalidated after exchange. " +
            "Returns new access_token (15-min) + new refresh_token (7-day). " +
            "Rate-limited: max 10 refreshes per hour per user.",
            p("refresh_token","string", "The current refresh token"),
            p("tenant_id",    "string", "Tenant identifier"),
            p("ip_address",   "string", "Client IP for audit")
        ));

        tools.add(t("validate_token",
            "Validate a JWT access token: verify signature, check expiry, and confirm not blacklisted. " +
            "Used by the API Gateway auth_request middleware for every inbound request. " +
            "Returns decoded claims { sub, tenant_id, email, roles, exp, jti } on success. " +
            "Returns 401 context on any validation failure.",
            p("access_token", "string", "Bearer JWT to validate"),
            p("tenant_id",    "string", "Optional: expected tenant_id for cross-tenant leak detection"),
            p("required_role","string", "Optional: assert a specific role is present in token claims")
        ));

        tools.add(t("revoke_token",
            "Immediately revoke a specific token by adding its JTI to the Redis blacklist. " +
            "Works for both access tokens and refresh tokens. " +
            "ADMIN can revoke any user's token; users can only revoke their own. " +
            "Publishes auth.token_revoked Kafka event for audit-service.",
            p("access_token",   "string", "Caller's JWT (for authentication and role check)"),
            p("token_to_revoke","string", "The token to blacklist"),
            p("tenant_id",      "string", "Tenant identifier"),
            p("reason",         "string", "Revocation reason: LOGOUT | SECURITY_INCIDENT | ADMIN_ACTION | PASSWORD_RESET")
        ));

        tools.add(t("introspect_token",
            "OAuth 2.0 token introspection (RFC 7662). Returns token metadata: " +
            "active (bool), sub, tenant_id, scope, exp, roles. " +
            "Used by resource servers to verify opaque or JWT tokens without re-decoding. " +
            "Adds a consistent validation interface regardless of token format.",
            p("token",     "string", "Token to introspect (access or refresh)"),
            p("tenant_id", "string", "Tenant identifier"),
            p("client_id", "string", "OAuth client ID making the introspection request")
        ));

        // ── MFA ───────────────────────────────────────────────────────────────
        tools.add(t("enable_mfa",
            "Provision TOTP MFA for a user account (RFC 6238). " +
            "Generates a 20-byte cryptographically random TOTP secret (Base32 encoded). " +
            "Returns the secret, QR provisioning URI (for authenticator apps), and 8 backup codes. " +
            "MFA is not marked active until verify_mfa is called with a valid code. " +
            "Publishes auth.mfa_enabled Kafka event on successful verification.",
            p("access_token",   "string", "Bearer JWT (authenticated user)"),
            p("tenant_id",      "string", "Tenant identifier"),
            p("mfa_type",       "string", "TOTP | SMS (TOTP recommended; SMS requires Twilio)"),
            p("phone_number",   "string", "Phone number for SMS MFA (ignored if mfa_type=TOTP)")
        ));

        tools.add(t("verify_mfa",
            "Verify a 6-digit TOTP code or SMS OTP. Validates within ±1 time window (30s). " +
            "On first verification after enable_mfa, marks the MFA device as active. " +
            "Returns a short-lived MFA session token on success for step-up auth flows. " +
            "3 consecutive failures lock the MFA device (re-provision required).",
            p("access_token",  "string", "Bearer JWT (authenticated user)"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("mfa_code",      "string", "6-digit TOTP code or SMS OTP"),
            p("mfa_type",      "string", "TOTP | SMS"),
            p("ip_address",    "string", "Client IP for audit")
        ));

        tools.add(t("disable_mfa",
            "Remove MFA from a user account. Requires valid MFA code + current password " +
            "(prevents social-engineering MFA removal). " +
            "Deletes the TOTP secret from the device store. " +
            "Invalidates all backup codes. Publishes auth.mfa_disabled Kafka event.",
            p("access_token",    "string", "Bearer JWT"),
            p("tenant_id",       "string", "Tenant identifier"),
            p("current_password","string", "Current password (required as secondary verification)"),
            p("mfa_code",        "string", "Valid current TOTP code (proves device is in hand)")
        ));

        // ── OAuth 2.0 ─────────────────────────────────────────────────────────
        tools.add(t("get_oauth_authorization_code",
            "OAuth 2.0 authorization code grant (RFC 6749 §4.1). " +
            "Validates client_id, redirect_uri, and scope against oauth_clients table. " +
            "Issues a single-use authorization code (10-minute TTL). " +
            "Scope maps to Ecoskiller features: view-assessments, submit-responses, grade-submissions. " +
            "Returns the code for the client to exchange via exchange_oauth_code.",
            p("access_token",  "string", "Bearer JWT (authenticated user)"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("client_id",     "string", "OAuth client application ID"),
            p("redirect_uri",  "string", "Client redirect URI (must match registered URI)"),
            p("scope",         "string", "Space-separated scopes: view-assessments submit-responses grade-submissions"),
            p("state",         "string", "CSRF protection state parameter (returned unchanged)")
        ));

        tools.add(t("exchange_oauth_code",
            "Exchange a valid OAuth 2.0 authorization code for access + refresh tokens. " +
            "Code is single-use and expires after 10 minutes. " +
            "Validates client_id, code authenticity, and expiry. " +
            "Returns { access_token, refresh_token, token_type, expires_in, scope }.",
            p("code",         "string", "The authorization code from get_oauth_authorization_code"),
            p("client_id",    "string", "OAuth client application ID"),
            p("client_secret","string", "OAuth client secret (validated against oauth_clients table)"),
            p("redirect_uri", "string", "Must exactly match the redirect_uri used to get the code"),
            p("tenant_id",    "string", "Tenant identifier")
        ));

        // ── Session ───────────────────────────────────────────────────────────
        tools.add(t("get_user_session",
            "Retrieve a specific session from the Redis session store. " +
            "Returns: user_id, tenant_id, roles, ip_address, user_agent, created_at, expires_at, active. " +
            "Users can only view their own sessions; ADMINs can view any.",
            p("access_token", "string", "Bearer JWT"),
            p("tenant_id",    "string", "Tenant identifier"),
            p("session_id",   "string", "Session ID to retrieve")
        ));

        tools.add(t("list_user_sessions",
            "List all active sessions for a user across all devices. " +
            "Each session shows: device info (user_agent), IP, creation time, expiry. " +
            "Users can only list their own sessions; ADMINs can list any user's sessions. " +
            "Useful for 'security devices' dashboard view.",
            p("access_token",    "string", "Bearer JWT"),
            p("tenant_id",       "string", "Tenant identifier"),
            p("target_user_id",  "string", "User ID whose sessions to list (self or ADMIN target)")
        ));

        tools.add(t("invalidate_all_sessions",
            "Invalidate ALL active sessions for a user (logout from all devices). " +
            "Blacklists all active access tokens for the user. " +
            "Removes all refresh tokens from the store. " +
            "Publishes auth.user_logout Kafka event with reason=ALL_DEVICES. " +
            "Common trigger: account compromise, password reset, role change.",
            p("access_token",    "string", "Bearer JWT (self or ADMIN)"),
            p("tenant_id",       "string", "Tenant identifier"),
            p("target_user_id",  "string", "User whose sessions to invalidate (self or ADMIN target)"),
            p("reason",          "string", "SECURITY_INCIDENT | PASSWORD_RESET | ADMIN_ACTION | USER_REQUEST")
        ));

        // ── RBAC + Audit ──────────────────────────────────────────────────────
        tools.add(t("check_permission",
            "Check whether a user has a specific permission/scope on a resource. " +
            "Validates the JWT, extracts roles, and evaluates against the Ecoskiller " +
            "RBAC matrix: CANDIDATE (view-own, submit), RECRUITER (view-all, grade), " +
            "ADMIN (full access), SUPER_ADMIN (cross-tenant). " +
            "Returns { permitted: bool, role, scope, reason }.",
            p("access_token", "string", "Bearer JWT to check"),
            p("tenant_id",    "string", "Tenant identifier"),
            p("resource",     "string", "Resource being accessed: assessment | application | candidate | job"),
            p("action",       "string", "Action: read | write | grade | delete | admin")
        ));

        tools.add(t("get_security_audit_log",
            "Retrieve the immutable security audit log for compliance (SOC 2, GDPR). " +
            "Captures: login success/failure, logout, MFA events, token revocation, " +
            "password resets, role changes, IP addresses, and User-Agents. " +
            "ADMIN access required. Filterable by user_id, event_type, and date range.",
            p("access_token",  "string", "Bearer JWT (ADMIN required)"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("user_id_filter","string", "Optional: filter to specific user's events"),
            p("event_type",    "string", "Optional: LOGIN | LOGOUT | MFA_VERIFY | TOKEN_REVOKED | PASSWORD_RESET"),
            p("limit",         "string", "Max audit entries to return (default 100, max 500)")
        ));

        return tools;
    }

    // ── Schema helpers ────────────────────────────────────────────────────────

    private Map<String, Object> t(String name, String description, Map<String, Object>... props) {
        Map<String, Object> tool = new LinkedHashMap<>();
        tool.put("name", name);
        tool.put("description", description);
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        Map<String, Object> properties = new LinkedHashMap<>();
        List<String> required = new ArrayList<>(List.of("tenant_id"));
        for (Map<String, Object> prop : props) {
            String pName = (String) prop.remove("_name");
            properties.put(pName, prop);
        }
        schema.put("properties", properties);
        schema.put("required", required);
        tool.put("inputSchema", schema);
        return tool;
    }

    private Map<String, Object> p(String name, String type, String description) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("_name", name); m.put("type", type); m.put("description", description);
        return m;
    }
}
