package com.ecoskiller.mcp.keycloak.tools;

import org.json.JSONObject;
import org.json.JSONArray;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Tool: token_management
 *
 * Manages Keycloak token lifecycle:
 *   - revoke:   Invalidate an access or refresh token (logout)
 *   - logout_user: Terminate all sessions for a given user ID
 *   - list_sessions: List active sessions for a user
 *   - get_public_key: Fetch Keycloak's public key for local token validation (RS256)
 *
 * Used by API Gateway and microservices to enforce zero-trust token validation.
 */
public class TokenManagementTool implements McpTool {

    private final KeycloakClient kc = KeycloakClient.getInstance();

    @Override
    public String getName() { return "token_management"; }

    @Override
    public String getDescription() {
        return "Manage Keycloak token lifecycle: revoke tokens, logout users, list active sessions, "
            + "and fetch the realm's public key for local JWT validation (RS256). "
            + "Supports both access token and refresh token revocation.";
    }

    @Override
    public JSONObject getInputSchema() {
        return new JSONObject()
            .put("type", "object")
            .put("properties", new JSONObject()
                .put("action", new JSONObject()
                    .put("type", "string")
                    .put("enum", new JSONArray()
                        .put("revoke").put("logout_user").put("list_sessions").put("get_public_key"))
                    .put("description", "Token management action to perform"))
                .put("token", new JSONObject()
                    .put("type", "string")
                    .put("description", "Token to revoke (for revoke action)"))
                .put("token_type_hint", new JSONObject()
                    .put("type", "string")
                    .put("enum", new JSONArray().put("access_token").put("refresh_token"))
                    .put("description", "Hint for token type (default: refresh_token)"))
                .put("user_id", new JSONObject()
                    .put("type", "string")
                    .put("description", "User UUID for logout_user or list_sessions"))
            )
            .put("required", new JSONArray().put("action"));
    }

    @Override
    public JSONObject execute(JSONObject args) throws Exception {
        String action = args.getString("action");

        return switch (action) {
            case "revoke"        -> revokeToken(args);
            case "logout_user"   -> logoutUser(args);
            case "list_sessions" -> listUserSessions(args);
            case "get_public_key" -> getPublicKey();
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }

    private JSONObject revokeToken(JSONObject args) throws Exception {
        if (!args.has("token")) throw new IllegalArgumentException("token is required");
        String token = args.getString("token");
        String hint = args.optString("token_type_hint", "refresh_token");

        String revokeUrl = kc.getBaseUrl() + "/realms/" + kc.getRealm()
            + "/protocol/openid-connect/revoke";
        String body = "token=" + URLEncoder.encode(token, StandardCharsets.UTF_8)
            + "&token_type_hint=" + URLEncoder.encode(hint, StandardCharsets.UTF_8)
            + "&client_id=" + URLEncoder.encode(kc.getClientId(), StandardCharsets.UTF_8)
            + "&client_secret=" + URLEncoder.encode(System.getenv("KEYCLOAK_CLIENT_SECRET"), StandardCharsets.UTF_8);

        kc.postForm(revokeUrl, body, null);
        return new JSONObject()
            .put("status", "success")
            .put("action", "revoke")
            .put("token_type_hint", hint)
            .put("message", "Token has been revoked. Active sessions may still persist until session expires.");
    }

    private JSONObject logoutUser(JSONObject args) throws Exception {
        if (!args.has("user_id")) throw new IllegalArgumentException("user_id is required");
        String userId = args.getString("user_id");

        // POST to admin logout endpoint
        kc.post(kc.adminUser(userId) + "/logout", new JSONObject());

        return new JSONObject()
            .put("status", "success")
            .put("action", "logout_user")
            .put("user_id", userId)
            .put("message", "All sessions for user have been invalidated.");
    }

    private JSONObject listUserSessions(JSONObject args) throws Exception {
        if (!args.has("user_id")) throw new IllegalArgumentException("user_id is required");
        String userId = args.getString("user_id");

        JSONObject resp = kc.get(kc.adminUser(userId) + "/sessions");
        return new JSONObject()
            .put("status", "success")
            .put("action", "list_sessions")
            .put("user_id", userId)
            .put("sessions", resp.optJSONArray("sessions") != null
                ? resp.optJSONArray("sessions") : new JSONArray());
    }

    private JSONObject getPublicKey() throws Exception {
        // OIDC discovery gives public keys for local JWT validation
        String certsUrl = kc.getBaseUrl() + "/realms/" + kc.getRealm()
            + "/protocol/openid-connect/certs";
        JSONObject certs = kc.get(certsUrl, ""); // No auth needed for public keys
        return new JSONObject()
            .put("status", "success")
            .put("action", "get_public_key")
            .put("realm", kc.getRealm())
            .put("jwks_uri", certsUrl)
            .put("keys", certs.optJSONArray("keys"))
            .put("usage", "Use these keys to validate JWT signatures locally (RS256). "
                + "Cache with TTL of 1 hour. Rotate on key_id mismatch.");
    }
}
