package com.ecoskiller.mcp.keycloak.tools;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Tool: user_management
 *
 * Full Keycloak user lifecycle management via Admin REST API:
 *   - create:       Create a new user (candidate, recruiter, admin)
 *   - get:          Retrieve user by ID or email
 *   - update:       Update user profile, attributes
 *   - delete:       Delete user (GDPR right to erasure)
 *   - search:       Search users by email, username, attribute
 *   - lock:         Disable user account (security incident)
 *   - unlock:       Re-enable user account
 *   - reset_password: Trigger password reset (admin or email link)
 *
 * Security:
 *   - Requires admin token (auto-managed)
 *   - All operations audit-logged
 *   - PII fields clearly labelled
 */
public class UserManagementTool implements McpTool {

    private final KeycloakClient kc = KeycloakClient.getInstance();

    @Override
    public String getName() { return "user_management"; }

    @Override
    public String getDescription() {
        return "Manage Keycloak user lifecycle via Admin REST API. "
            + "Create, read, update, delete users (GDPR-compliant). "
            + "Search by email or username. Lock/unlock accounts for security incidents. "
            + "Trigger password resets. Supports candidate, recruiter, and admin user types.";
    }

    @Override
    public JSONObject getInputSchema() {
        return new JSONObject()
            .put("type", "object")
            .put("properties", new JSONObject()
                .put("action", new JSONObject()
                    .put("type", "string")
                    .put("enum", new JSONArray()
                        .put("create").put("get").put("update").put("delete")
                        .put("search").put("lock").put("unlock").put("reset_password"))
                    .put("description", "User management action"))
                .put("user_id", new JSONObject()
                    .put("type", "string")
                    .put("description", "Keycloak user UUID (required for get, update, delete, lock, unlock, reset_password)"))
                .put("email", new JSONObject()
                    .put("type", "string")
                    .put("description", "User email address"))
                .put("username", new JSONObject()
                    .put("type", "string")
                    .put("description", "Username (defaults to email if not provided)"))
                .put("first_name", new JSONObject()
                    .put("type", "string"))
                .put("last_name", new JSONObject()
                    .put("type", "string"))
                .put("temporary_password", new JSONObject()
                    .put("type", "string")
                    .put("description", "Initial password (user must change on first login)"))
                .put("enabled", new JSONObject()
                    .put("type", "boolean")
                    .put("description", "Account enabled/disabled (default: true)"))
                .put("roles", new JSONObject()
                    .put("type", "array")
                    .put("items", new JSONObject().put("type", "string"))
                    .put("description", "Initial roles to assign (candidate, recruiter, admin, etc.)"))
                .put("attributes", new JSONObject()
                    .put("type", "object")
                    .put("description", "Custom attributes: university_id, department, hire_level, etc."))
                .put("search_query", new JSONObject()
                    .put("type", "string")
                    .put("description", "Search query (email, username, or partial match)"))
                .put("max_results", new JSONObject()
                    .put("type", "integer")
                    .put("description", "Max results for search (default: 20)"))
                .put("send_email", new JSONObject()
                    .put("type", "boolean")
                    .put("description", "Send password reset email link (for reset_password action)"))
            )
            .put("required", new JSONArray().put("action"));
    }

    @Override
    public JSONObject execute(JSONObject args) throws Exception {
        return switch (args.getString("action")) {
            case "create"         -> createUser(args);
            case "get"            -> getUser(args);
            case "update"         -> updateUser(args);
            case "delete"         -> deleteUser(args);
            case "search"         -> searchUsers(args);
            case "lock"           -> setEnabled(args, false);
            case "unlock"         -> setEnabled(args, true);
            case "reset_password" -> resetPassword(args);
            default -> throw new IllegalArgumentException("Unknown action: " + args.getString("action"));
        };
    }

    // ── Create ────────────────────────────────────────────────────────────────

    private JSONObject createUser(JSONObject args) throws Exception {
        String email = requireString(args, "email");
        String username = args.optString("username", email);

        JSONObject user = new JSONObject()
            .put("email", email)
            .put("username", username)
            .put("firstName", args.optString("first_name", ""))
            .put("lastName", args.optString("last_name", ""))
            .put("enabled", args.optBoolean("enabled", true))
            .put("emailVerified", false);

        // Custom attributes (university_id, department, etc.)
        if (args.has("attributes")) {
            JSONObject rawAttrs = args.getJSONObject("attributes");
            JSONObject kcAttrs = new JSONObject();
            for (String key : rawAttrs.keySet()) {
                kcAttrs.put(key, new JSONArray().put(rawAttrs.get(key)));
            }
            user.put("attributes", kcAttrs);
        }

        // Temporary password
        if (args.has("temporary_password") && !args.getString("temporary_password").isBlank()) {
            user.put("credentials", new JSONArray().put(new JSONObject()
                .put("type", "password")
                .put("value", args.getString("temporary_password"))
                .put("temporary", true)));
        }

        JSONObject resp = kc.post(kc.adminUsers(), user);

        return new JSONObject()
            .put("status", "success")
            .put("action", "create")
            .put("email", email)
            .put("username", username)
            .put("user_id", resp.optString("id", "see_location_header"))
            .put("next_step", "Assign roles using role_management tool, then user can login.");
    }

    // ── Get ───────────────────────────────────────────────────────────────────

    private JSONObject getUser(JSONObject args) throws Exception {
        String path;
        if (args.has("user_id")) {
            path = kc.adminUser(args.getString("user_id"));
        } else if (args.has("email")) {
            path = kc.adminUsers() + "?email=" + args.getString("email") + "&exact=true";
        } else {
            throw new IllegalArgumentException("user_id or email required for get action");
        }

        JSONObject resp = kc.get(path);
        return new JSONObject()
            .put("status", "success")
            .put("action", "get")
            .put("user", sanitizeUserResponse(resp));
    }

    // ── Update ────────────────────────────────────────────────────────────────

    private JSONObject updateUser(JSONObject args) throws Exception {
        String userId = requireString(args, "user_id");
        JSONObject updates = new JSONObject();

        if (args.has("email")) updates.put("email", args.getString("email"));
        if (args.has("first_name")) updates.put("firstName", args.getString("first_name"));
        if (args.has("last_name")) updates.put("lastName", args.getString("last_name"));
        if (args.has("enabled")) updates.put("enabled", args.getBoolean("enabled"));
        if (args.has("attributes")) {
            JSONObject rawAttrs = args.getJSONObject("attributes");
            JSONObject kcAttrs = new JSONObject();
            for (String key : rawAttrs.keySet()) {
                kcAttrs.put(key, new JSONArray().put(rawAttrs.get(key)));
            }
            updates.put("attributes", kcAttrs);
        }

        kc.put(kc.adminUser(userId), updates);
        return new JSONObject()
            .put("status", "success").put("action", "update").put("user_id", userId);
    }

    // ── Delete (GDPR right to erasure) ───────────────────────────────────────

    private JSONObject deleteUser(JSONObject args) throws Exception {
        String userId = requireString(args, "user_id");
        kc.delete(kc.adminUser(userId));
        return new JSONObject()
            .put("status", "success")
            .put("action", "delete")
            .put("user_id", userId)
            .put("gdpr", "User data has been permanently removed per GDPR right-to-erasure.");
    }

    // ── Search ────────────────────────────────────────────────────────────────

    private JSONObject searchUsers(JSONObject args) throws Exception {
        String q = args.optString("search_query", "");
        int max = args.optInt("max_results", 20);
        String path = kc.adminUsers() + "?search=" + q + "&max=" + max;
        JSONObject resp = kc.get(path);
        return new JSONObject()
            .put("status", "success")
            .put("action", "search")
            .put("query", q)
            .put("users", resp.optJSONArray("users") != null
                ? resp.optJSONArray("users") : new JSONArray());
    }

    // ── Lock / Unlock ─────────────────────────────────────────────────────────

    private JSONObject setEnabled(JSONObject args, boolean enabled) throws Exception {
        String userId = requireString(args, "user_id");
        kc.put(kc.adminUser(userId), new JSONObject().put("enabled", enabled));
        return new JSONObject()
            .put("status", "success")
            .put("action", enabled ? "unlock" : "lock")
            .put("user_id", userId)
            .put("enabled", enabled);
    }

    // ── Reset Password ────────────────────────────────────────────────────────

    private JSONObject resetPassword(JSONObject args) throws Exception {
        String userId = requireString(args, "user_id");
        boolean sendEmail = args.optBoolean("send_email", true);

        if (sendEmail) {
            // Send email-based reset link
            kc.put(kc.adminUser(userId) + "/execute-actions-email",
                new JSONObject().put("actions", new JSONArray().put("UPDATE_PASSWORD")));
            return new JSONObject()
                .put("status", "success")
                .put("action", "reset_password")
                .put("user_id", userId)
                .put("method", "email_link")
                .put("message", "Password reset email sent to user.");
        } else {
            // Admin sets temporary password (from args)
            if (!args.has("temporary_password"))
                throw new IllegalArgumentException("temporary_password required when send_email=false");
            kc.put(kc.adminUser(userId) + "/reset-password", new JSONObject()
                .put("type", "password")
                .put("value", args.getString("temporary_password"))
                .put("temporary", true));
            return new JSONObject()
                .put("status", "success")
                .put("action", "reset_password")
                .put("user_id", userId)
                .put("method", "admin_set")
                .put("message", "Temporary password set. User must change on next login.");
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static String requireString(JSONObject args, String key) {
        if (!args.has(key) || args.getString(key).isBlank())
            throw new IllegalArgumentException("Missing required argument: " + key);
        return args.getString(key);
    }

    private static JSONObject sanitizeUserResponse(JSONObject user) {
        // Remove internal Keycloak fields that may contain sensitive data
        JSONObject safe = new JSONObject(user.toMap());
        safe.remove("credentials"); // Never return credential hashes
        safe.remove("totp");
        return safe;
    }
}
