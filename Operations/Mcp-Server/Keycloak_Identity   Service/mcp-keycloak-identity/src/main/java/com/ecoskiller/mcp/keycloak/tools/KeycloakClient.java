package com.ecoskiller.mcp.keycloak.tools;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

/**
 * Keycloak REST API client.
 *
 * All Keycloak communication passes through this class.
 * Configuration is loaded from environment variables (never hardcoded).
 *
 * Environment Variables (required):
 *   KEYCLOAK_URL          Base URL, e.g. https://keycloak.ecoskiller.com
 *   KEYCLOAK_REALM        Default realm, e.g. ecoskiller
 *   KEYCLOAK_CLIENT_ID    Admin client ID
 *   KEYCLOAK_CLIENT_SECRET  Admin client secret (confidential)
 *
 * Optional:
 *   KEYCLOAK_TIMEOUT_SECS   HTTP timeout (default 10)
 *   KEYCLOAK_TLS_VERIFY     true/false (default true — never disable in prod)
 */
public class KeycloakClient {

    private static final Logger LOGGER = Logger.getLogger(KeycloakClient.class.getName());

    // ─── Config ───────────────────────────────────────────────────────────────
    private final String baseUrl;
    private final String realm;
    private final String clientId;
    private final String clientSecret;
    private final int timeoutSecs;
    private final HttpClient httpClient;

    // Cached admin token (avoid fetching on every call)
    private volatile String cachedAdminToken = null;
    private volatile long tokenExpiresAt = 0;

    private static final KeycloakClient INSTANCE = new KeycloakClient();
    public static KeycloakClient getInstance() { return INSTANCE; }

    private KeycloakClient() {
        this.baseUrl = requireEnv("KEYCLOAK_URL").replaceAll("/$", "");
        this.realm = System.getenv().getOrDefault("KEYCLOAK_REALM", "ecoskiller");
        this.clientId = requireEnv("KEYCLOAK_CLIENT_ID");
        this.clientSecret = requireEnv("KEYCLOAK_CLIENT_SECRET");
        this.timeoutSecs = Integer.parseInt(System.getenv().getOrDefault("KEYCLOAK_TIMEOUT_SECS", "10"));

        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(timeoutSecs))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

        LOGGER.info("KeycloakClient configured: " + baseUrl + " realm=" + realm);
    }

    private static String requireEnv(String key) {
        String val = System.getenv(key);
        if (val == null || val.isBlank()) {
            throw new IllegalStateException("Required environment variable not set: " + key);
        }
        return val.trim();
    }

    // ─── Admin Token Management ───────────────────────────────────────────────

    /**
     * Obtain (or return cached) admin access token via client_credentials grant.
     * Token is refreshed 30 seconds before expiry.
     */
    public synchronized String getAdminToken() throws Exception {
        long now = System.currentTimeMillis() / 1000L;
        if (cachedAdminToken != null && now < tokenExpiresAt - 30) {
            return cachedAdminToken;
        }

        String tokenUrl = baseUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        String body = "grant_type=client_credentials"
            + "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
            + "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8);

        JSONObject resp = postForm(tokenUrl, body, null);
        cachedAdminToken = resp.getString("access_token");
        int expiresIn = resp.optInt("expires_in", 300);
        tokenExpiresAt = now + expiresIn;
        LOGGER.fine("Admin token refreshed, expires in " + expiresIn + "s");
        return cachedAdminToken;
    }

    // ─── HTTP Helpers ─────────────────────────────────────────────────────────

    public JSONObject get(String path) throws Exception {
        return get(path, null);
    }

    public JSONObject get(String path, String token) throws Exception {
        String url = path.startsWith("http") ? path : baseUrl + path;
        String bearer = token != null ? token : getAdminToken();
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + bearer)
            .header("Accept", "application/json")
            .timeout(Duration.ofSeconds(timeoutSecs))
            .GET()
            .build();
        return execute(req);
    }

    public JSONObject post(String path, JSONObject body) throws Exception {
        String url = path.startsWith("http") ? path : baseUrl + path;
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + getAdminToken())
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .timeout(Duration.ofSeconds(timeoutSecs))
            .POST(HttpRequest.BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
            .build();
        return execute(req);
    }

    public JSONObject put(String path, JSONObject body) throws Exception {
        String url = path.startsWith("http") ? path : baseUrl + path;
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + getAdminToken())
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .timeout(Duration.ofSeconds(timeoutSecs))
            .PUT(HttpRequest.BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
            .build();
        return execute(req);
    }

    public JSONObject delete(String path) throws Exception {
        String url = path.startsWith("http") ? path : baseUrl + path;
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + getAdminToken())
            .header("Accept", "application/json")
            .timeout(Duration.ofSeconds(timeoutSecs))
            .DELETE()
            .build();
        return execute(req);
    }

    public JSONObject postForm(String url, String formBody, String bearerToken) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Accept", "application/json")
            .timeout(Duration.ofSeconds(timeoutSecs))
            .POST(HttpRequest.BodyPublishers.ofString(formBody, StandardCharsets.UTF_8));
        if (bearerToken != null) {
            builder.header("Authorization", "Bearer " + bearerToken);
        }
        return execute(builder.build());
    }

    private JSONObject execute(HttpRequest req) throws Exception {
        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        String body = resp.body();
        int status = resp.statusCode();

        if (status >= 400) {
            String errMsg = "Keycloak HTTP " + status + " for " + req.uri().getPath();
            LOGGER.warning(errMsg + " | body=" + body);
            throw new RuntimeException(errMsg + ": " + body);
        }

        if (body == null || body.isBlank()) {
            return new JSONObject().put("status", status).put("body", "");
        }

        try {
            return new JSONObject(body);
        } catch (Exception e) {
            // Some Keycloak endpoints return arrays
            return new JSONObject().put("status", status).put("raw", body);
        }
    }

    // ─── Convenience Admin Path Builders ────────────────────────────────────

    public String adminRealm()                    { return "/admin/realms/" + realm; }
    public String adminRealm(String r)            { return "/admin/realms/" + r; }
    public String adminUsers()                    { return adminRealm() + "/users"; }
    public String adminUser(String id)            { return adminUsers() + "/" + id; }
    public String adminRoles()                    { return adminRealm() + "/roles"; }
    public String adminRole(String name)          { return adminRoles() + "/" + name; }
    public String adminSessions()                 { return adminRealm() + "/sessions"; }
    public String adminEvents()                   { return adminRealm() + "/events"; }
    public String adminIdps()                     { return adminRealm() + "/identity-provider/instances"; }
    public String oidcToken()                     { return "/realms/" + realm + "/protocol/openid-connect/token"; }
    public String oidcIntrospect()                { return "/realms/" + realm + "/protocol/openid-connect/token/introspect"; }
    public String oidcUserinfo()                  { return "/realms/" + realm + "/protocol/openid-connect/userinfo"; }

    public String getRealm()     { return realm; }
    public String getBaseUrl()   { return baseUrl; }
    public String getClientId()  { return clientId; }
    // NOTE: getClientSecret() intentionally NOT exposed — secrets never leave this class
}
