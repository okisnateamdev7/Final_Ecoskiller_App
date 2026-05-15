package com.ecoskiller.mcp.keycloak.tools;

import org.json.JSONObject;
import org.json.JSONArray;

import java.util.*;

/**
 * Tool: bulk_user_import
 *
 * Import multiple users in a single call.
 * Accepts a JSON array of user objects.
 * Used for:
 *   - Campus hiring round setup (import all students from CSV → JSON)
 *   - Initial platform onboarding
 *   - Batch recruiter account creation
 *
 * Returns per-user success/failure results.
 * Maximum 500 users per call (to stay within Keycloak rate limits).
 */
public class BulkUserImportTool implements McpTool {

    private final KeycloakClient kc = KeycloakClient.getInstance();
    private static final int MAX_BATCH = 500;

    @Override
    public String getName() { return "bulk_user_import"; }

    @Override
    public String getDescription() {
        return "Import multiple users into Keycloak in a single call. "
            + "Accepts a JSON array of user objects with email, name, role, and attributes. "
            + "Used for campus hiring batch setup, bulk recruiter onboarding, and CSV imports. "
            + "Returns per-user success/failure. Max 500 users per call.";
    }

    @Override
    public JSONObject getInputSchema() {
        return new JSONObject()
            .put("type", "object")
            .put("properties", new JSONObject()
                .put("users", new JSONObject()
                    .put("type", "array")
                    .put("description", "Array of user objects to import")
                    .put("maxItems", MAX_BATCH)
                    .put("items", new JSONObject()
                        .put("type", "object")
                        .put("properties", new JSONObject()
                            .put("email", new JSONObject().put("type", "string"))
                            .put("first_name", new JSONObject().put("type", "string"))
                            .put("last_name", new JSONObject().put("type", "string"))
                            .put("username", new JSONObject().put("type", "string"))
                            .put("temporary_password", new JSONObject().put("type", "string"))
                            .put("roles", new JSONObject()
                                .put("type", "array")
                                .put("items", new JSONObject().put("type", "string")))
                            .put("attributes", new JSONObject().put("type", "object"))
                        )
                        .put("required", new JSONArray().put("email"))
                    ))
                .put("default_role", new JSONObject()
                    .put("type", "string")
                    .put("description", "Default role to assign if not specified per user (e.g. candidate)"))
                .put("default_temporary_password", new JSONObject()
                    .put("type", "string")
                    .put("description", "Default temporary password for users without explicit password"))
                .put("send_welcome_email", new JSONObject()
                    .put("type", "boolean")
                    .put("description", "Send welcome email to each imported user (default: false)"))
            )
            .put("required", new JSONArray().put("users"));
    }

    @Override
    public JSONObject execute(JSONObject args) throws Exception {
        JSONArray users = args.getJSONArray("users");
        if (users.length() > MAX_BATCH) {
            throw new IllegalArgumentException("Max " + MAX_BATCH + " users per import call. Got: " + users.length());
        }

        String defaultRole = args.optString("default_role", "candidate");
        String defaultPwd  = args.optString("default_temporary_password", null);
        boolean sendEmail  = args.optBoolean("send_welcome_email", false);

        int created = 0, failed = 0;
        JSONArray results = new JSONArray();

        for (int i = 0; i < users.length(); i++) {
            JSONObject u = users.getJSONObject(i);
            String email = u.optString("email", "");
            if (email.isBlank()) {
                results.put(new JSONObject().put("email", "(blank)").put("status", "skipped").put("reason", "email required"));
                failed++;
                continue;
            }

            try {
                String username = u.optString("username", email);
                JSONObject userPayload = new JSONObject()
                    .put("email", email)
                    .put("username", username)
                    .put("firstName", u.optString("first_name", ""))
                    .put("lastName", u.optString("last_name", ""))
                    .put("enabled", true)
                    .put("emailVerified", false);

                // Credentials
                String pwd = u.has("temporary_password") ? u.getString("temporary_password") : defaultPwd;
                if (pwd != null && !pwd.isBlank()) {
                    userPayload.put("credentials", new JSONArray().put(new JSONObject()
                        .put("type", "password").put("value", pwd).put("temporary", true)));
                }

                // Attributes
                if (u.has("attributes")) {
                    JSONObject rawAttrs = u.getJSONObject("attributes");
                    JSONObject kcAttrs = new JSONObject();
                    for (String key : rawAttrs.keySet()) {
                        kcAttrs.put(key, new JSONArray().put(rawAttrs.get(key)));
                    }
                    userPayload.put("attributes", kcAttrs);
                }

                kc.post(kc.adminUsers(), userPayload);
                created++;

                JSONObject res = new JSONObject().put("email", email).put("status", "created");
                if (sendEmail) res.put("welcome_email", "queued");
                results.put(res);

            } catch (Exception e) {
                failed++;
                results.put(new JSONObject()
                    .put("email", email)
                    .put("status", "failed")
                    .put("reason", e.getMessage()));
            }
        }

        return new JSONObject()
            .put("status", "completed")
            .put("total", users.length())
            .put("created", created)
            .put("failed", failed)
            .put("results", results)
            .put("next_step", "Assign roles using role_management tool if not pre-assigned.");
    }
}
