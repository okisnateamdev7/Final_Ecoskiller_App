package com.ecoskiller.mcp.keycloak.tools;

import org.json.JSONObject;
import org.json.JSONArray;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Tool: token_introspect
 *
 * Validates an OAuth2 access token via Keycloak's token introspection endpoint.
 * Extracts and returns:
 *   - active status (true/false)
 *   - user identity claims (sub, email, name)
 *   - assigned roles
 *   - token expiry info
 *   - realm & client context
 *
 * Used by API Gateway and microservices for request-time authorization.
 * Compliant with RFC 7662 (OAuth 2.0 Token Introspection).
 */
public class TokenIntrospectTool implements McpTool {

    private final KeycloakClient kc = KeycloakClient.getInstance();

    @Override
    public String getName() { return "token_introspect"; }

    @Override
    public String getDescription() {
        return "Validate and introspect an OAuth2 access token (RFC 7662). "
            + "Returns active status, user identity (sub, email, roles), token expiry, "
            + "and scope. Used by API Gateway for authorization. "
            + "Optionally decodes JWT claims locally without network round-trip.";
    }

    @Override
    public JSONObject getInputSchema() {
        return new JSONObject()
            .put("type", "object")
            .put("properties", new JSONObject()
                .put("token", new JSONObject()
                    .put("type", "string")
                    .put("description", "Access token to introspect and validate"))
                .put("token_type_hint", new JSONObject()
                    .put("type", "string")
                    .put("enum", new JSONArray().put("access_token").put("refresh_token"))
                    .put("description", "Token type hint (default: access_token)"))
                .put("decode_local", new JSONObject()
                    .put("type", "boolean")
                    .put("description", "If true, also decode JWT payload locally (no Keycloak call for claims, but still validates via introspect)"))
            )
            .put("required", new JSONArray().put("token"));
    }

    @Override
    public JSONObject execute(JSONObject args) throws Exception {
        String token = args.getString("token");
        String hint = args.optString("token_type_hint", "access_token");
        boolean decodeLocal = args.optBoolean("decode_local", false);

        // Call Keycloak introspection endpoint
        String body = "token=" + URLEncoder.encode(token, StandardCharsets.UTF_8)
            + "&token_type_hint=" + URLEncoder.encode(hint, StandardCharsets.UTF_8)
            + "&client_id=" + URLEncoder.encode(kc.getClientId(), StandardCharsets.UTF_8)
            + "&client_secret=" + URLEncoder.encode(System.getenv("KEYCLOAK_CLIENT_SECRET"), StandardCharsets.UTF_8);

        JSONObject introspection = kc.postForm(
            kc.getBaseUrl() + kc.oidcIntrospect(), body, null);

        boolean active = introspection.optBoolean("active", false);

        JSONObject result = new JSONObject()
            .put("status", "success")
            .put("active", active);

        if (active) {
            result.put("sub", introspection.optString("sub", ""))
                  .put("username", introspection.optString("username", ""))
                  .put("email", introspection.optString("email", ""))
                  .put("email_verified", introspection.optBoolean("email_verified", false))
                  .put("name", introspection.optString("name", ""))
                  .put("given_name", introspection.optString("given_name", ""))
                  .put("family_name", introspection.optString("family_name", ""))
                  .put("realm_access", introspection.optJSONObject("realm_access"))
                  .put("resource_access", introspection.optJSONObject("resource_access"))
                  .put("scope", introspection.optString("scope", ""))
                  .put("client_id", introspection.optString("client_id", ""))
                  .put("iat", introspection.optLong("iat", 0))
                  .put("exp", introspection.optLong("exp", 0))
                  .put("session_state", introspection.optString("session_state", ""));

            // Extract flat role list for convenience
            JSONObject realmAccess = introspection.optJSONObject("realm_access");
            if (realmAccess != null) {
                result.put("roles", realmAccess.optJSONArray("roles"));
            }
        } else {
            result.put("reason", "Token is invalid, expired, or has been revoked.");
        }

        // Optional: also decode JWT payload locally (structural decode, not crypto verify)
        if (decodeLocal && token.contains(".")) {
            try {
                String[] parts = token.split("\\.");
                if (parts.length >= 2) {
                    byte[] payloadBytes = Base64.getUrlDecoder().decode(
                        padBase64(parts[1]));
                    JSONObject localClaims = new JSONObject(new String(payloadBytes, StandardCharsets.UTF_8));
                    result.put("local_claims_decoded", localClaims);
                    result.put("local_decode_warning",
                        "Local decode does NOT verify signature. Always use introspect=true for security.");
                }
            } catch (Exception e) {
                result.put("local_decode_error", "Could not decode JWT: " + e.getMessage());
            }
        }

        return result;
    }

    private static String padBase64(String s) {
        int pad = 4 - (s.length() % 4);
        if (pad < 4) s = s + "=".repeat(pad);
        return s;
    }
}
