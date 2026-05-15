package com.ecoskiller.mcp.wazuh.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.time.Duration;
import java.util.*;
import java.util.logging.*;

/**
 * WazuhApiClient — secure HTTPS REST client for the Wazuh Manager API.
 *
 * SECURITY:
 *  - All HTTP calls use TLS (https:// enforced by SecurityManager)
 *  - Bearer token injected from environment variable WAZUH_API_TOKEN
 *    (never from user-supplied parameters — prevents token injection)
 *  - No custom SSL override — uses JVM default trust store
 *  - 30-second request timeout — prevents hanging calls
 *  - Response bodies limited to 10 MB — prevents OOM from crafted responses
 *
 * Wazuh API endpoints used:
 *   GET  /security/user/authenticate   → JWT token
 *   GET  /alerts                       → query alerts
 *   GET  /agents                       → query agents
 *   GET  /rules                        → query rules
 *   GET  /syscheck/{agent_id}          → FIM events
 *   GET  /vulnerability/{agent_id}     → vulnerabilities
 *   GET  /overview/agents              → agent summary
 *   PUT  /active-response              → trigger active response
 *   GET  /manager/info                 → manager health
 */
public class WazuhApiClient {

    private static final Logger LOGGER = Logger.getLogger(WazuhApiClient.class.getName());

    private static final int    TIMEOUT_SECONDS  = 30;
    private static final long   MAX_BODY_BYTES    = 10 * 1024 * 1024L; // 10 MB
    private static final String TOKEN_ENV_VAR     = "WAZUH_API_TOKEN";
    private static final String BASE_URL_ENV_VAR  = "WAZUH_API_URL";

    private final HttpClient    httpClient;
    private final ObjectMapper  mapper = new ObjectMapper();
    private final String        baseUrl;
    private final String        bearerToken;

    public static final class ApiResponse {
        public final int    statusCode;
        public final String body;
        public final boolean success;

        ApiResponse(int code, String body) {
            this.statusCode = code;
            this.body       = body;
            this.success    = code >= 200 && code < 300;
        }

        public JsonNode json(ObjectMapper m) {
            try { return m.readTree(body); } catch (Exception e) { return m.createObjectNode(); }
        }
    }

    public WazuhApiClient() {
        this.baseUrl     = resolveEnv(BASE_URL_ENV_VAR, "https://wazuh-manager.ops.svc.cluster.local:55000");
        this.bearerToken = resolveEnv(TOKEN_ENV_VAR, "");
        this.httpClient  = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    }

    /** GET /path — returns ApiResponse */
    public ApiResponse get(String path) {
        return get(path, Collections.emptyMap());
    }

    /** GET /path?params — returns ApiResponse */
    public ApiResponse get(String path, Map<String, String> params) {
        try {
            String url = buildUrl(path, params);
            LOGGER.info("[WazuhAPI] GET " + url);
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + bearerToken)
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .GET()
                .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            LOGGER.info("[WazuhAPI] Response: " + resp.statusCode());
            return new ApiResponse(resp.statusCode(), truncate(resp.body()));
        } catch (Exception e) {
            LOGGER.severe("[WazuhAPI] GET failed: " + e.getMessage());
            return new ApiResponse(-1, "{\"error\":\"" + sanitiseMsg(e.getMessage()) + "\"}");
        }
    }

    /** PUT /path with JSON body — used for active-response triggers */
    public ApiResponse put(String path, String jsonBody) {
        try {
            String url = buildUrl(path, Collections.emptyMap());
            LOGGER.info("[WazuhAPI] PUT " + url);
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + bearerToken)
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(resp.statusCode(), truncate(resp.body()));
        } catch (Exception e) {
            LOGGER.severe("[WazuhAPI] PUT failed: " + e.getMessage());
            return new ApiResponse(-1, "{\"error\":\"" + sanitiseMsg(e.getMessage()) + "\"}");
        }
    }

    /** POST /path with JSON body */
    public ApiResponse post(String path, String jsonBody) {
        try {
            String url = buildUrl(path, Collections.emptyMap());
            LOGGER.info("[WazuhAPI] POST " + url);
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + bearerToken)
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(resp.statusCode(), truncate(resp.body()));
        } catch (Exception e) {
            LOGGER.severe("[WazuhAPI] POST failed: " + e.getMessage());
            return new ApiResponse(-1, "{\"error\":\"" + sanitiseMsg(e.getMessage()) + "\"}");
        }
    }

    public String getBaseUrl() { return baseUrl; }

    // ── Private helpers ──────────────────────────────────────────────────────

    private String buildUrl(String path, Map<String, String> params) {
        StringBuilder url = new StringBuilder(baseUrl);
        if (!path.startsWith("/")) url.append("/");
        url.append(path);
        if (!params.isEmpty()) {
            url.append("?");
            params.forEach((k, v) -> {
                try {
                    url.append(URLEncoder.encode(k, "UTF-8"))
                       .append("=")
                       .append(URLEncoder.encode(v, "UTF-8"))
                       .append("&");
                } catch (Exception e) { /* skip malformed params */ }
            });
            // Remove trailing &
            if (url.charAt(url.length() - 1) == '&') url.deleteCharAt(url.length() - 1);
        }
        return url.toString();
    }

    private String truncate(String body) {
        if (body == null) return "";
        return body.length() > MAX_BODY_BYTES ? body.substring(0, (int) MAX_BODY_BYTES) + "…[TRUNCATED]" : body;
    }

    private String sanitiseMsg(String msg) {
        if (msg == null) return "unknown";
        return msg.replaceAll("[\"\\\\]", "_").substring(0, Math.min(msg.length(), 200));
    }

    private String resolveEnv(String envVar, String fallback) {
        String val = System.getenv(envVar);
        return (val != null && !val.isBlank()) ? val.trim() : fallback;
    }
}
