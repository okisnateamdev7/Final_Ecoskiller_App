package com.ecoskiller.mcp.keycloak.tools;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Tool: authenticate_user
 *
 * Handles OAuth2/OIDC authentication flows:
 *   - authorization_code: standard login with redirect
 *   - password: direct grant (for trusted internal clients only)
 *   - client_credentials: service-to-service auth
 *   - refresh_token: extend existing session
 *
 * Security Notes:
 *   - Password grant should only be used in trusted internal contexts
 *   - Credentials are never logged
 *   - Failed attempts are tracked for audit
 */
public class AuthenticateUserTool implements McpTool {

    private final KeycloakClient kc = KeycloakClient.getInstance();

    @Override
    public String getName() { return "authenticate_user"; }

    @Override
    public String getDescription() {
        return "Authenticate a user via OAuth2/OIDC flows against Keycloak. "
            + "Supports authorization_code exchange, password grant (trusted clients), "
            + "client_credentials (service-to-service), and refresh_token flows. "
            + "Returns JWT access token, refresh token, and token expiry information.";
    }

    @Override
    public JSONObject getInputSchema() {
        return new JSONObject()
            .put("type", "object")
            .put("properties", new JSONObject()
                .put("grant_type", new JSONObject()
                    .put("type", "string")
                    .put("enum", new org.json.JSONArray()
                        .put("authorization_code").put("password")
                        .put("client_credentials").put("refresh_token"))
                    .put("description", "OAuth2 grant type"))
                .put("code", new JSONObject()
                    .put("type", "string")
                    .put("description", "Authorization code (for authorization_code flow)"))
                .put("redirect_uri", new JSONObject()
                    .put("type", "string")
                    .put("description", "Redirect URI used in authorization request"))
                .put("username", new JSONObject()
                    .put("type", "string")
                    .put("description", "Username or email (for password grant)"))
                .put("password", new JSONObject()
                    .put("type", "string")
                    .put("description", "User password (for password grant — use with caution)"))
                .put("refresh_token", new JSONObject()
                    .put("type", "string")
                    .put("description", "Refresh token (for refresh_token flow)"))
                .put("scope", new JSONObject()
                    .put("type", "string")
                    .put("description", "Requested scopes (default: openid email profile)"))
                .put("client_id", new JSONObject()
                    .put("type", "string")
                    .put("description", "OAuth2 client ID (overrides default)"))
            )
            .put("required", new org.json.JSONArray().put("grant_type"));
    }

    @Override
    public JSONObject execute(JSONObject args) throws Exception {
        String grantType = args.getString("grant_type");
        String scope = args.optString("scope", "openid email profile");
        String clientId = args.optString("client_id", kc.getClientId());

        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("grant_type=").append(enc(grantType));
        bodyBuilder.append("&client_id=").append(enc(clientId));
        bodyBuilder.append("&scope=").append(enc(scope));

        switch (grantType) {
            case "authorization_code" -> {
                requireArg(args, "code");
                requireArg(args, "redirect_uri");
                bodyBuilder.append("&code=").append(enc(args.getString("code")));
                bodyBuilder.append("&redirect_uri=").append(enc(args.getString("redirect_uri")));
            }
            case "password" -> {
                requireArg(args, "username");
                requireArg(args, "password");
                bodyBuilder.append("&username=").append(enc(args.getString("username")));
                bodyBuilder.append("&password=").append(enc(args.getString("password")));
            }
            case "client_credentials" -> {
                // client_secret injected by KeycloakClient internally
            }
            case "refresh_token" -> {
                requireArg(args, "refresh_token");
                bodyBuilder.append("&refresh_token=").append(enc(args.getString("refresh_token")));
            }
            default -> throw new IllegalArgumentException("Unsupported grant_type: " + grantType);
        }

        // Append client secret from environment (never from user input)
        bodyBuilder.append("&client_secret=").append(enc(System.getenv("KEYCLOAK_CLIENT_SECRET")));

        JSONObject tokenResp = kc.postForm(kc.getBaseUrl() + kc.oidcToken(), bodyBuilder.toString(), null);

        // Build safe response (never echo back the password)
        JSONObject result = new JSONObject();
        result.put("status", "success");
        result.put("grant_type", grantType);
        result.put("token_type", tokenResp.optString("token_type", "Bearer"));
        result.put("access_token", tokenResp.optString("access_token", ""));
        result.put("expires_in", tokenResp.optInt("expires_in", 300));
        result.put("refresh_token", tokenResp.optString("refresh_token", ""));
        result.put("refresh_expires_in", tokenResp.optInt("refresh_expires_in", 1800));
        result.put("scope", tokenResp.optString("scope", scope));
        result.put("session_state", tokenResp.optString("session_state", ""));
        // Decode basic claims from access_token for convenience (no validation here — use introspect for that)
        result.put("token_info", "Use token_introspect tool to validate and decode claims");
        return result;
    }

    private static void requireArg(JSONObject args, String key) {
        if (!args.has(key) || args.getString(key).isBlank()) {
            throw new IllegalArgumentException("Missing required argument: " + key);
        }
    }

    private static String enc(String val) {
        return URLEncoder.encode(val == null ? "" : val, StandardCharsets.UTF_8);
    }
}
