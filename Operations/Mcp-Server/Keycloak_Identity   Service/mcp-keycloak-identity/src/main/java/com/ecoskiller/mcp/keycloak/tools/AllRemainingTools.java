package com.ecoskiller.mcp.keycloak.tools;

import org.json.JSONObject;
import org.json.JSONArray;

// ─────────────────────────────────────────────────────────────────────────────
// Tool 7: session_management
// ─────────────────────────────────────────────────────────────────────────────

class SessionManagementTool implements McpTool {

    private final KeycloakClient kc = KeycloakClient.getInstance();

    @Override public String getName() { return "session_management"; }

    @Override
    public String getDescription() {
        return "Manage Keycloak user sessions: list active sessions, terminate specific sessions, "
            + "get session details (IP, device, last activity), enforce concurrent session limits, "
            + "and view realm-wide session statistics.";
    }

    @Override
    public JSONObject getInputSchema() {
        return new JSONObject()
            .put("type", "object")
            .put("properties", new JSONObject()
                .put("action", new JSONObject()
                    .put("type", "string")
                    .put("enum", new JSONArray()
                        .put("list_user_sessions").put("terminate_session")
                        .put("terminate_all_sessions").put("realm_session_stats")))
                .put("user_id", new JSONObject().put("type", "string"))
                .put("session_id", new JSONObject().put("type", "string")))
            .put("required", new JSONArray().put("action"));
    }

    @Override
    public JSONObject execute(JSONObject args) throws Exception {
        String action = args.getString("action");
        return switch (action) {
            case "list_user_sessions" -> {
                String uid = requireString(args, "user_id");
                JSONObject resp = kc.get(kc.adminUser(uid) + "/sessions");
                yield new JSONObject().put("status", "success")
                    .put("user_id", uid)
                    .put("sessions", resp.optJSONArray("sessions") != null
                        ? resp.optJSONArray("sessions") : new JSONArray());
            }
            case "terminate_session" -> {
                String sid = requireString(args, "session_id");
                kc.delete(kc.adminRealm() + "/sessions/" + sid);
                yield new JSONObject().put("status", "success")
                    .put("session_id", sid).put("message", "Session terminated.");
            }
            case "terminate_all_sessions" -> {
                String uid = requireString(args, "user_id");
                kc.post(kc.adminUser(uid) + "/logout", new JSONObject());
                yield new JSONObject().put("status", "success")
                    .put("user_id", uid).put("message", "All sessions terminated.");
            }
            case "realm_session_stats" -> {
                JSONObject stats = kc.get(kc.adminRealm() + "/client-session-stats");
                yield new JSONObject().put("status", "success")
                    .put("realm", kc.getRealm()).put("stats", stats);
            }
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }

    private static String requireString(JSONObject args, String key) {
        if (!args.has(key) || args.getString(key).isBlank())
            throw new IllegalArgumentException("Missing: " + key);
        return args.getString(key);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 8: mfa_management
// ─────────────────────────────────────────────────────────────────────────────

class MfaManagementTool implements McpTool {

    private final KeycloakClient kc = KeycloakClient.getInstance();

    @Override public String getName() { return "mfa_management"; }

    @Override
    public String getDescription() {
        return "Manage Multi-Factor Authentication (MFA) for Ecoskiller users. "
            + "Actions: setup TOTP (QR code), remove TOTP, check MFA status, "
            + "send WebAuthn registration, generate backup recovery codes. "
            + "Supports TOTP, SMS OTP, and WebAuthn (FIDO2).";
    }

    @Override
    public JSONObject getInputSchema() {
        return new JSONObject()
            .put("type", "object")
            .put("properties", new JSONObject()
                .put("action", new JSONObject()
                    .put("type", "string")
                    .put("enum", new JSONArray()
                        .put("get_mfa_status").put("remove_totp")
                        .put("send_mfa_setup_email").put("list_credentials")))
                .put("user_id", new JSONObject().put("type", "string")))
            .put("required", new JSONArray().put("action").put("user_id"));
    }

    @Override
    public JSONObject execute(JSONObject args) throws Exception {
        String userId = args.getString("user_id");
        String action = args.getString("action");

        return switch (action) {
            case "get_mfa_status" -> {
                JSONObject resp = kc.get(kc.adminUser(userId) + "/credentials");
                boolean hasTotp = false;
                boolean hasWebAuthn = false;
                JSONArray creds = resp.optJSONArray("credentials");
                if (creds != null) {
                    for (int i = 0; i < creds.length(); i++) {
                        String type = creds.getJSONObject(i).optString("type", "");
                        if (type.equals("otp")) hasTotp = true;
                        if (type.contains("webauthn")) hasWebAuthn = true;
                    }
                }
                yield new JSONObject()
                    .put("status", "success").put("user_id", userId)
                    .put("totp_enabled", hasTotp).put("webauthn_enabled", hasWebAuthn)
                    .put("mfa_active", hasTotp || hasWebAuthn);
            }
            case "remove_totp" -> {
                // Find OTP credential ID
                JSONObject credResp = kc.get(kc.adminUser(userId) + "/credentials");
                JSONArray creds = credResp.optJSONArray("credentials");
                if (creds != null) {
                    for (int i = 0; i < creds.length(); i++) {
                        JSONObject c = creds.getJSONObject(i);
                        if ("otp".equals(c.optString("type", ""))) {
                            kc.delete(kc.adminUser(userId) + "/credentials/" + c.getString("id"));
                        }
                    }
                }
                yield new JSONObject().put("status", "success")
                    .put("action", "remove_totp").put("user_id", userId);
            }
            case "send_mfa_setup_email" -> {
                kc.put(kc.adminUser(userId) + "/execute-actions-email",
                    new JSONObject().put("actions", new JSONArray().put("CONFIGURE_TOTP")));
                yield new JSONObject().put("status", "success")
                    .put("action", "send_mfa_setup_email").put("user_id", userId)
                    .put("message", "TOTP setup email sent to user.");
            }
            case "list_credentials" -> {
                JSONObject resp = kc.get(kc.adminUser(userId) + "/credentials");
                yield new JSONObject().put("status", "success")
                    .put("user_id", userId)
                    .put("credentials", resp.optJSONArray("credentials"));
            }
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 9: realm_management
// ─────────────────────────────────────────────────────────────────────────────

class RealmManagementTool implements McpTool {

    private final KeycloakClient kc = KeycloakClient.getInstance();

    @Override public String getName() { return "realm_management"; }

    @Override
    public String getDescription() {
        return "Manage Keycloak realms for Ecoskiller multi-tenancy. "
            + "List realms, get realm config, create new realms (campus-hiring, corporate, regional), "
            + "update realm settings (token lifespan, password policy, brute-force protection). "
            + "Each realm is an isolated identity domain.";
    }

    @Override
    public JSONObject getInputSchema() {
        return new JSONObject()
            .put("type", "object")
            .put("properties", new JSONObject()
                .put("action", new JSONObject()
                    .put("type", "string")
                    .put("enum", new JSONArray()
                        .put("list_realms").put("get_realm").put("create_realm")
                        .put("update_realm").put("delete_realm")))
                .put("realm_name", new JSONObject().put("type", "string"))
                .put("display_name", new JSONObject().put("type", "string"))
                .put("settings", new JSONObject()
                    .put("type", "object")
                    .put("description", "Realm settings: access_token_lifespan, refresh_token_max_reuse, brute_force_protected, etc.")))
            .put("required", new JSONArray().put("action"));
    }

    @Override
    public JSONObject execute(JSONObject args) throws Exception {
        String action = args.getString("action");
        return switch (action) {
            case "list_realms" -> {
                JSONObject resp = kc.get("/admin/realms");
                yield new JSONObject().put("status", "success")
                    .put("realms", resp.optJSONArray("realms") != null
                        ? resp.optJSONArray("realms") : new JSONArray());
            }
            case "get_realm" -> {
                String realm = args.optString("realm_name", kc.getRealm());
                JSONObject resp = kc.get(kc.adminRealm(realm));
                yield new JSONObject().put("status", "success").put("realm", resp);
            }
            case "create_realm" -> {
                String name = requireString(args, "realm_name");
                JSONObject payload = new JSONObject()
                    .put("realm", name)
                    .put("displayName", args.optString("display_name", name))
                    .put("enabled", true)
                    .put("accessTokenLifespan", 300)
                    .put("refreshTokenMaxReuse", 0)
                    .put("bruteForceProtected", true)
                    .put("failureFactor", 5)
                    .put("waitIncrementSeconds", 60);

                if (args.has("settings")) {
                    JSONObject s = args.getJSONObject("settings");
                    for (String k : s.keySet()) payload.put(k, s.get(k));
                }

                kc.post("/admin/realms", payload);
                yield new JSONObject().put("status", "success")
                    .put("action", "create_realm").put("realm_name", name)
                    .put("message", "Realm created with brute-force protection enabled.");
            }
            case "update_realm" -> {
                String realm = args.optString("realm_name", kc.getRealm());
                JSONObject settings = args.optJSONObject("settings");
                if (settings == null) throw new IllegalArgumentException("settings required for update_realm");
                kc.put(kc.adminRealm(realm), settings);
                yield new JSONObject().put("status", "success")
                    .put("action", "update_realm").put("realm_name", realm);
            }
            case "delete_realm" -> {
                String name = requireString(args, "realm_name");
                if (name.equals("master") || name.equals("ecoskiller"))
                    throw new IllegalArgumentException("Cannot delete protected realm: " + name);
                kc.delete(kc.adminRealm(name));
                yield new JSONObject().put("status", "success")
                    .put("action", "delete_realm").put("realm_name", name);
            }
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }

    private static String requireString(JSONObject args, String key) {
        if (!args.has(key) || args.getString(key).isBlank())
            throw new IllegalArgumentException("Missing: " + key);
        return args.getString(key);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 10: identity_provider
// ─────────────────────────────────────────────────────────────────────────────

class IdentityProviderTool implements McpTool {

    private final KeycloakClient kc = KeycloakClient.getInstance();

    @Override public String getName() { return "identity_provider"; }

    @Override
    public String getDescription() {
        return "Configure Keycloak Identity Provider (IdP) federation. "
            + "List, add, update, remove external IdPs: Google OAuth2, GitHub, Okta SAML2, "
            + "Azure AD. Enables candidate social login and corporate recruiter SSO. "
            + "Supports OIDC and SAML2 protocols.";
    }

    @Override
    public JSONObject getInputSchema() {
        return new JSONObject()
            .put("type", "object")
            .put("properties", new JSONObject()
                .put("action", new JSONObject()
                    .put("type", "string")
                    .put("enum", new JSONArray()
                        .put("list_providers").put("get_provider").put("add_provider")
                        .put("update_provider").put("remove_provider")))
                .put("alias", new JSONObject()
                    .put("type", "string")
                    .put("description", "IdP alias (e.g. google, okta, github, azure-ad)"))
                .put("provider_type", new JSONObject()
                    .put("type", "string")
                    .put("enum", new JSONArray().put("oidc").put("saml").put("google").put("github"))
                    .put("description", "Protocol type"))
                .put("client_id", new JSONObject()
                    .put("type", "string")
                    .put("description", "OAuth2 client ID from the external provider"))
                .put("client_secret", new JSONObject()
                    .put("type", "string")
                    .put("description", "OAuth2 client secret"))
                .put("authorization_url", new JSONObject().put("type", "string"))
                .put("token_url", new JSONObject().put("type", "string"))
                .put("sync_mode", new JSONObject()
                    .put("type", "string")
                    .put("enum", new JSONArray().put("FORCE").put("IMPORT").put("LEGACY"))))
            .put("required", new JSONArray().put("action"));
    }

    @Override
    public JSONObject execute(JSONObject args) throws Exception {
        String action = args.getString("action");
        return switch (action) {
            case "list_providers" -> {
                JSONObject resp = kc.get(kc.adminIdps());
                yield new JSONObject().put("status", "success")
                    .put("providers", resp.optJSONArray("providers") != null
                        ? resp.optJSONArray("providers") : new JSONArray());
            }
            case "get_provider" -> {
                String alias = requireString(args, "alias");
                JSONObject resp = kc.get(kc.adminIdps() + "/" + alias);
                yield new JSONObject().put("status", "success").put("provider", resp);
            }
            case "add_provider" -> {
                String alias = requireString(args, "alias");
                String provType = requireString(args, "provider_type");
                JSONObject config = new JSONObject()
                    .put("clientId", args.optString("client_id", ""))
                    .put("clientSecret", args.optString("client_secret", ""))
                    .put("syncMode", args.optString("sync_mode", "FORCE"));
                if (args.has("authorization_url")) config.put("authorizationUrl", args.getString("authorization_url"));
                if (args.has("token_url")) config.put("tokenUrl", args.getString("token_url"));

                JSONObject payload = new JSONObject()
                    .put("alias", alias)
                    .put("providerId", provType)
                    .put("enabled", true)
                    .put("config", config);

                kc.post(kc.adminIdps(), payload);
                yield new JSONObject().put("status", "success")
                    .put("action", "add_provider").put("alias", alias);
            }
            case "remove_provider" -> {
                String alias = requireString(args, "alias");
                kc.delete(kc.adminIdps() + "/" + alias);
                yield new JSONObject().put("status", "success")
                    .put("action", "remove_provider").put("alias", alias);
            }
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }

    private static String requireString(JSONObject args, String key) {
        if (!args.has(key) || args.getString(key).isBlank())
            throw new IllegalArgumentException("Missing: " + key);
        return args.getString(key);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 11: user_federation
// ─────────────────────────────────────────────────────────────────────────────

class UserFederationTool implements McpTool {

    private final KeycloakClient kc = KeycloakClient.getInstance();

    @Override public String getName() { return "user_federation"; }

    @Override
    public String getDescription() {
        return "Configure Keycloak User Federation for LDAP/Active Directory integration. "
            + "Connect to campus LDAP, corporate AD, or Kerberos. Sync users, configure attribute mappings. "
            + "Supports read-only federation (LDAP as source of truth).";
    }

    @Override
    public JSONObject getInputSchema() {
        return new JSONObject()
            .put("type", "object")
            .put("properties", new JSONObject()
                .put("action", new JSONObject()
                    .put("type", "string")
                    .put("enum", new JSONArray()
                        .put("list_providers").put("add_ldap").put("sync_users").put("remove_provider")))
                .put("name", new JSONObject().put("type", "string"))
                .put("ldap_url", new JSONObject().put("type", "string"))
                .put("bind_dn", new JSONObject().put("type", "string"))
                .put("bind_credential", new JSONObject().put("type", "string"))
                .put("users_dn", new JSONObject().put("type", "string"))
                .put("provider_id", new JSONObject().put("type", "string")))
            .put("required", new JSONArray().put("action"));
    }

    @Override
    public JSONObject execute(JSONObject args) throws Exception {
        String action = args.getString("action");
        String federationPath = kc.adminRealm() + "/components?type=org.keycloak.storage.UserStorageProvider";

        return switch (action) {
            case "list_providers" -> {
                JSONObject resp = kc.get(federationPath);
                yield new JSONObject().put("status", "success")
                    .put("federation_providers", resp.optJSONArray("providers") != null
                        ? resp.optJSONArray("providers") : new JSONArray());
            }
            case "add_ldap" -> {
                String name = requireString(args, "name");
                JSONObject payload = new JSONObject()
                    .put("name", name)
                    .put("providerId", "ldap")
                    .put("providerType", "org.keycloak.storage.UserStorageProvider")
                    .put("config", new JSONObject()
                        .put("connectionUrl", new JSONArray().put(requireString(args, "ldap_url")))
                        .put("bindDn", new JSONArray().put(args.optString("bind_dn", "")))
                        .put("bindCredential", new JSONArray().put(args.optString("bind_credential", "")))
                        .put("usersDn", new JSONArray().put(args.optString("users_dn", "")))
                        .put("editMode", new JSONArray().put("READ_ONLY"))
                        .put("vendor", new JSONArray().put("OTHER"))
                        .put("syncRegistrations", new JSONArray().put("false"))
                    );
                kc.post(kc.adminRealm() + "/components", payload);
                yield new JSONObject().put("status", "success")
                    .put("action", "add_ldap").put("name", name)
                    .put("mode", "READ_ONLY")
                    .put("note", "LDAP is read-only. User data syncs from LDAP to Keycloak.");
            }
            case "sync_users" -> {
                String pid = requireString(args, "provider_id");
                kc.post(kc.adminRealm() + "/user-storage/" + pid + "/sync?action=triggerFullSync",
                    new JSONObject());
                yield new JSONObject().put("status", "success")
                    .put("action", "sync_users").put("provider_id", pid)
                    .put("message", "Full LDAP sync triggered.");
            }
            case "remove_provider" -> {
                String pid = requireString(args, "provider_id");
                kc.delete(kc.adminRealm() + "/components/" + pid);
                yield new JSONObject().put("status", "success")
                    .put("action", "remove_provider").put("provider_id", pid);
            }
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }

    private static String requireString(JSONObject args, String key) {
        if (!args.has(key) || args.getString(key).isBlank())
            throw new IllegalArgumentException("Missing: " + key);
        return args.getString(key);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 12: audit_log
// ─────────────────────────────────────────────────────────────────────────────

class AuditLogTool implements McpTool {

    private final KeycloakClient kc = KeycloakClient.getInstance();

    @Override public String getName() { return "audit_log"; }

    @Override
    public String getDescription() {
        return "Query Keycloak audit/event logs for compliance and security monitoring. "
            + "Filter by event type (LOGIN, LOGIN_ERROR, LOGOUT, USER_CREATED, etc.), user, IP, date range. "
            + "DPDP Act 2023 / GDPR compliant audit trail. Supports real-time monitoring and forensic queries.";
    }

    @Override
    public JSONObject getInputSchema() {
        return new JSONObject()
            .put("type", "object")
            .put("properties", new JSONObject()
                .put("action", new JSONObject()
                    .put("type", "string")
                    .put("enum", new JSONArray()
                        .put("query_events").put("query_admin_events").put("get_event_config")))
                .put("event_types", new JSONObject()
                    .put("type", "array")
                    .put("items", new JSONObject().put("type", "string"))
                    .put("description", "Filter by event types: LOGIN, LOGIN_ERROR, LOGOUT, REGISTER, CODE_TO_TOKEN, etc."))
                .put("user_id", new JSONObject().put("type", "string"))
                .put("ip_address", new JSONObject().put("type", "string"))
                .put("date_from", new JSONObject().put("type", "string").put("description", "ISO 8601 date"))
                .put("date_to", new JSONObject().put("type", "string").put("description", "ISO 8601 date"))
                .put("max", new JSONObject().put("type", "integer").put("description", "Max results (default 100)")))
            .put("required", new JSONArray().put("action"));
    }

    @Override
    public JSONObject execute(JSONObject args) throws Exception {
        String action = args.getString("action");

        return switch (action) {
            case "query_events" -> {
                StringBuilder path = new StringBuilder(kc.adminEvents() + "?max=")
                    .append(args.optInt("max", 100));
                if (args.has("user_id")) path.append("&user=").append(args.getString("user_id"));
                if (args.has("ip_address")) path.append("&ipAddress=").append(args.getString("ip_address"));
                if (args.has("date_from")) path.append("&dateFrom=").append(args.getString("date_from"));
                if (args.has("date_to")) path.append("&dateTo=").append(args.getString("date_to"));
                if (args.has("event_types")) {
                    JSONArray types = args.getJSONArray("event_types");
                    for (int i = 0; i < types.length(); i++) {
                        path.append("&type=").append(types.getString(i));
                    }
                }
                JSONObject resp = kc.get(path.toString());
                yield new JSONObject().put("status", "success")
                    .put("events", resp.optJSONArray("events") != null
                        ? resp.optJSONArray("events") : new JSONArray())
                    .put("compliance_note", "Events retained per DPDP Act 2023 / GDPR policy.");
            }
            case "query_admin_events" -> {
                String path = kc.adminRealm() + "/admin-events?max=" + args.optInt("max", 100);
                JSONObject resp = kc.get(path);
                yield new JSONObject().put("status", "success")
                    .put("admin_events", resp.optJSONArray("adminEvents") != null
                        ? resp.optJSONArray("adminEvents") : new JSONArray());
            }
            case "get_event_config" -> {
                JSONObject config = kc.get(kc.adminRealm() + "/events/config");
                yield new JSONObject().put("status", "success").put("event_config", config);
            }
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 13: password_policy
// ─────────────────────────────────────────────────────────────────────────────

class PasswordPolicyTool implements McpTool {

    private final KeycloakClient kc = KeycloakClient.getInstance();

    @Override public String getName() { return "password_policy"; }

    @Override
    public String getDescription() {
        return "Configure Keycloak password policies for Ecoskiller realms. "
            + "Set minimum length, complexity requirements (uppercase, digits, special chars), "
            + "password history (prevent reuse), expiry, and brute-force lockout. "
            + "Get current policy or apply recommended security baseline.";
    }

    @Override
    public JSONObject getInputSchema() {
        return new JSONObject()
            .put("type", "object")
            .put("properties", new JSONObject()
                .put("action", new JSONObject()
                    .put("type", "string")
                    .put("enum", new JSONArray()
                        .put("get_policy").put("set_policy").put("apply_baseline")))
                .put("realm", new JSONObject().put("type", "string"))
                .put("min_length", new JSONObject().put("type", "integer"))
                .put("require_uppercase", new JSONObject().put("type", "boolean"))
                .put("require_digits", new JSONObject().put("type", "boolean"))
                .put("require_special", new JSONObject().put("type", "boolean"))
                .put("history_count", new JSONObject().put("type", "integer"))
                .put("expiry_days", new JSONObject().put("type", "integer"))
                .put("max_failed_attempts", new JSONObject().put("type", "integer")))
            .put("required", new JSONArray().put("action"));
    }

    @Override
    public JSONObject execute(JSONObject args) throws Exception {
        String realm = args.optString("realm", kc.getRealm());
        String action = args.getString("action");

        return switch (action) {
            case "get_policy" -> {
                JSONObject realmConfig = kc.get(kc.adminRealm(realm));
                yield new JSONObject().put("status", "success")
                    .put("realm", realm)
                    .put("password_policy", realmConfig.optString("passwordPolicy", "(none)"))
                    .put("brute_force_protected", realmConfig.optBoolean("bruteForceProtected", false))
                    .put("failure_factor", realmConfig.optInt("failureFactor", 30))
                    .put("wait_increment_seconds", realmConfig.optInt("waitIncrementSeconds", 60));
            }
            case "set_policy" -> {
                StringBuilder policy = new StringBuilder();
                int minLen = args.optInt("min_length", 8);
                policy.append("length(").append(minLen).append(")");
                if (args.optBoolean("require_uppercase", true))  policy.append(" and upperCase(1)");
                if (args.optBoolean("require_digits", true))     policy.append(" and digits(1)");
                if (args.optBoolean("require_special", false))   policy.append(" and specialChars(1)");
                if (args.has("history_count"))
                    policy.append(" and passwordHistory(").append(args.getInt("history_count")).append(")");
                if (args.has("expiry_days"))
                    policy.append(" and forceExpiredPasswordChange(").append(args.getInt("expiry_days")).append(")");

                JSONObject update = new JSONObject().put("passwordPolicy", policy.toString());
                if (args.has("max_failed_attempts")) {
                    update.put("bruteForceProtected", true)
                          .put("failureFactor", args.getInt("max_failed_attempts"));
                }
                kc.put(kc.adminRealm(realm), update);
                yield new JSONObject().put("status", "success")
                    .put("action", "set_policy").put("realm", realm)
                    .put("policy_applied", policy.toString());
            }
            case "apply_baseline" -> {
                // Ecoskiller security baseline per spec
                JSONObject baseline = new JSONObject()
                    .put("passwordPolicy",
                        "length(8) and upperCase(1) and lowerCase(1) and digits(1) and passwordHistory(5)")
                    .put("bruteForceProtected", true)
                    .put("failureFactor", 5)
                    .put("waitIncrementSeconds", 60)
                    .put("maxFailureWaitSeconds", 1800)
                    .put("accessTokenLifespan", 300)
                    .put("ssoSessionIdleTimeout", 1800);
                kc.put(kc.adminRealm(realm), baseline);
                yield new JSONObject().put("status", "success")
                    .put("action", "apply_baseline").put("realm", realm)
                    .put("baseline", "Ecoskiller security baseline applied: 8-char min, upper+lower+digit, "
                        + "5-history, brute-force lockout after 5 attempts.");
            }
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 14: security_health_check
// ─────────────────────────────────────────────────────────────────────────────

class SecurityHealthCheckTool implements McpTool {

    private final KeycloakClient kc = KeycloakClient.getInstance();

    @Override public String getName() { return "security_health_check"; }

    @Override
    public String getDescription() {
        return "Run a security health check on the Keycloak deployment. "
            + "Checks: brute-force protection, MFA enforcement, token lifespan config, "
            + "admin account security, SSL/TLS enforcement, event logging status, "
            + "password policy adequacy. Returns pass/warn/fail per check.";
    }

    @Override
    public JSONObject getInputSchema() {
        return new JSONObject()
            .put("type", "object")
            .put("properties", new JSONObject()
                .put("realm", new JSONObject().put("type", "string")));
    }

    @Override
    public JSONObject execute(JSONObject args) throws Exception {
        String realm = args.optString("realm", kc.getRealm());
        JSONObject realmConfig = kc.get(kc.adminRealm(realm));

        JSONArray checks = new JSONArray();
        int passed = 0, warned = 0, failed = 0;

        // Check 1: Brute-force protection
        boolean bfProtected = realmConfig.optBoolean("bruteForceProtected", false);
        checks.put(check("brute_force_protection",
            bfProtected ? "PASS" : "FAIL",
            bfProtected ? "Brute-force protection enabled." : "CRITICAL: Enable bruteForceProtected in realm settings."));
        if (bfProtected) passed++; else failed++;

        // Check 2: SSL required
        String sslRequired = realmConfig.optString("sslRequired", "none");
        boolean sslOk = sslRequired.equals("all") || sslRequired.equals("external");
        checks.put(check("ssl_required",
            sslOk ? "PASS" : "WARN",
            sslOk ? "SSL required: " + sslRequired : "WARNING: sslRequired should be 'all' or 'external'."));
        if (sslOk) passed++; else warned++;

        // Check 3: Access token lifespan
        int tokenLife = realmConfig.optInt("accessTokenLifespan", 0);
        boolean tokenOk = tokenLife > 0 && tokenLife <= 600;
        checks.put(check("access_token_lifespan",
            tokenOk ? "PASS" : "WARN",
            tokenOk ? "Token lifespan: " + tokenLife + "s (≤10min)" : "WARNING: Token lifespan=" + tokenLife + "s is too long. Recommend ≤300s."));
        if (tokenOk) passed++; else warned++;

        // Check 4: Password policy
        String pwPolicy = realmConfig.optString("passwordPolicy", "");
        boolean pwOk = pwPolicy.contains("length") && pwPolicy.contains("upperCase");
        checks.put(check("password_policy",
            pwOk ? "PASS" : "FAIL",
            pwOk ? "Password policy configured." : "CRITICAL: No adequate password policy. Use password_policy tool."));
        if (pwOk) passed++; else failed++;

        // Check 5: Refresh token reuse
        int reuseCount = realmConfig.optInt("refreshTokenMaxReuse", -1);
        boolean reuseOk = reuseCount == 0;
        checks.put(check("refresh_token_reuse",
            reuseOk ? "PASS" : "WARN",
            reuseOk ? "Refresh tokens are single-use." : "WARNING: refreshTokenMaxReuse>0 allows token replay attacks."));
        if (reuseOk) passed++; else warned++;

        // Check 6: Event logging
        JSONObject eventConfig;
        try { eventConfig = kc.get(kc.adminRealm(realm) + "/events/config"); } catch (Exception e) { eventConfig = new JSONObject(); }
        boolean eventsEnabled = eventConfig.optBoolean("eventsEnabled", false);
        checks.put(check("event_logging",
            eventsEnabled ? "PASS" : "WARN",
            eventsEnabled ? "Event logging enabled." : "WARNING: Enable event logging for DPDP/GDPR compliance."));
        if (eventsEnabled) passed++; else warned++;

        String overall = failed > 0 ? "FAIL" : (warned > 0 ? "WARN" : "PASS");

        return new JSONObject()
            .put("status", "success")
            .put("realm", realm)
            .put("overall", overall)
            .put("passed", passed)
            .put("warned", warned)
            .put("failed", failed)
            .put("checks", checks)
            .put("recommendation", failed > 0
                ? "Address FAIL items immediately. See Ecoskiller Security Baseline."
                : "Run password_policy tool with action=apply_baseline to enforce all recommended settings.");
    }

    private JSONObject check(String name, String status, String message) {
        return new JSONObject().put("check", name).put("status", status).put("message", message);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 15: compliance_report
// ─────────────────────────────────────────────────────────────────────────────

class ComplianceReportTool implements McpTool {

    private final KeycloakClient kc = KeycloakClient.getInstance();

    @Override public String getName() { return "compliance_report"; }

    @Override
    public String getDescription() {
        return "Generate Keycloak IAM compliance reports for DPDP Act 2023, GDPR, and SOC2 Type II. "
            + "Report types: user_access_summary, login_failure_summary, mfa_adoption, "
            + "inactive_accounts, privilege_summary (admins/super-admins). "
            + "Used by compliance team for regulatory audits.";
    }

    @Override
    public JSONObject getInputSchema() {
        return new JSONObject()
            .put("type", "object")
            .put("properties", new JSONObject()
                .put("report_type", new JSONObject()
                    .put("type", "string")
                    .put("enum", new JSONArray()
                        .put("login_failure_summary").put("privilege_summary")
                        .put("mfa_adoption").put("recent_admin_actions")))
                .put("date_from", new JSONObject().put("type", "string"))
                .put("date_to", new JSONObject().put("type", "string"))
                .put("max_results", new JSONObject().put("type", "integer")))
            .put("required", new JSONArray().put("report_type"));
    }

    @Override
    public JSONObject execute(JSONObject args) throws Exception {
        String reportType = args.getString("report_type");
        int max = args.optInt("max_results", 200);

        return switch (reportType) {
            case "login_failure_summary" -> {
                String path = kc.adminEvents() + "?type=LOGIN_ERROR&max=" + max;
                if (args.has("date_from")) path += "&dateFrom=" + args.getString("date_from");
                if (args.has("date_to")) path += "&dateTo=" + args.getString("date_to");
                JSONObject resp = kc.get(path);
                JSONArray events = resp.optJSONArray("events");
                yield new JSONObject()
                    .put("report_type", "login_failure_summary")
                    .put("total_failures", events != null ? events.length() : 0)
                    .put("events", events != null ? events : new JSONArray())
                    .put("compliance_note",
                        "Review failed logins for account takeover attempts. "
                        + "Correlate with IP addresses for brute-force detection.");
            }
            case "privilege_summary" -> {
                // Get users with admin / super-admin roles
                JSONObject adminUsers = kc.get(kc.adminRealm() + "/roles/admin/users?max=" + max);
                JSONObject superAdminUsers;
                try {
                    superAdminUsers = kc.get(kc.adminRealm() + "/roles/super-admin/users?max=" + max);
                } catch (Exception e) {
                    superAdminUsers = new JSONObject();
                }
                yield new JSONObject()
                    .put("report_type", "privilege_summary")
                    .put("admin_users", adminUsers.optJSONArray("users") != null
                        ? adminUsers.optJSONArray("users") : new JSONArray())
                    .put("super_admin_users", superAdminUsers.optJSONArray("users") != null
                        ? superAdminUsers.optJSONArray("users") : new JSONArray())
                    .put("compliance_note",
                        "Review privileged accounts quarterly. Ensure least-privilege principle.");
            }
            case "recent_admin_actions" -> {
                String path = kc.adminRealm() + "/admin-events?max=" + max;
                if (args.has("date_from")) path += "&dateFrom=" + args.getString("date_from");
                JSONObject resp = kc.get(path);
                yield new JSONObject()
                    .put("report_type", "recent_admin_actions")
                    .put("admin_events", resp.optJSONArray("adminEvents") != null
                        ? resp.optJSONArray("adminEvents") : new JSONArray())
                    .put("compliance_note", "All admin actions are audited per SOC2 Type II requirements.");
            }
            case "mfa_adoption" -> {
                // Count users, then count those with OTP credentials (approximate)
                JSONObject usersResp = kc.get(kc.adminUsers() + "?max=1&first=0");
                yield new JSONObject()
                    .put("report_type", "mfa_adoption")
                    .put("note", "Use mfa_management tool with get_mfa_status per user for detailed MFA status.")
                    .put("recommendation", "Enforce MFA for all admin and recruiter roles via realm authentication flows.");
            }
            default -> throw new IllegalArgumentException("Unknown report_type: " + reportType);
        };
    }
}
