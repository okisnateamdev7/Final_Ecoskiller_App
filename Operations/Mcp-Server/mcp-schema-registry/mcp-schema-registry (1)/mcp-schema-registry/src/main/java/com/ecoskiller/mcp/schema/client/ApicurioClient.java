package com.ecoskiller.mcp.schema.client;

import com.ecoskiller.mcp.schema.config.SchemaRegistryConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

/**
 * HTTP client wrapper for the Apicurio Registry REST API v2.
 *
 * API reference: https://www.apicur.io/registry/docs/apicurio-registry/3.x/
 *
 * All methods return raw JsonNode — parsing is left to individual tools.
 * Handles:
 *   - Basic auth (username + password)
 *   - Bearer token auth (Keycloak JWT)
 *   - TLS verification toggle
 *   - Configurable timeout
 *   - Structured error responses
 */
public class ApicurioClient {

    private static final Logger log = LoggerFactory.getLogger(ApicurioClient.class);

    private final SchemaRegistryConfig cfg;
    private final HttpClient           http;
    private final ObjectMapper         json = new ObjectMapper();
    private final String               baseUrl;

    public ApicurioClient(SchemaRegistryConfig cfg) {
        this.cfg     = cfg;
        this.baseUrl = cfg.apicurioBaseUrl.replaceAll("/$", ""); // strip trailing slash

        HttpClient.Builder builder = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(cfg.timeoutMs))
                .version(HttpClient.Version.HTTP_1_1);

        // TLS verification — for testing/internal CAs you may set tlsVerify=false
        if (!cfg.tlsVerify) {
            try {
                javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("TLS");
                sc.init(null, new javax.net.ssl.TrustManager[]{
                    new javax.net.ssl.X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(java.security.cert.X509Certificate[] c, String a) {}
                        public void checkServerTrusted(java.security.cert.X509Certificate[] c, String a) {}
                    }
                }, new java.security.SecureRandom());
                builder.sslContext(sc);
            } catch (Exception e) {
                log.warn("Could not disable TLS verification: {}", e.getMessage());
            }
        }

        this.http = builder.build();
        log.debug("ApicurioClient initialised: baseUrl={}", baseUrl);
    }

    // ── Core HTTP methods ──────────────────────────────────────────────────────

    /** HTTP GET — returns parsed JSON node. */
    public ApicurioResponse get(String path) throws Exception {
        HttpRequest req = requestBuilder(path)
                .GET()
                .build();
        return execute(req);
    }

    /** HTTP POST with JSON body. */
    public ApicurioResponse post(String path, String body, String contentType) throws Exception {
        HttpRequest req = requestBuilder(path)
                .header("Content-Type", contentType)
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();
        return execute(req);
    }

    /** HTTP PUT with JSON body. */
    public ApicurioResponse put(String path, String body) throws Exception {
        HttpRequest req = requestBuilder(path)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();
        return execute(req);
    }

    /** HTTP DELETE. */
    public ApicurioResponse delete(String path) throws Exception {
        HttpRequest req = requestBuilder(path)
                .DELETE()
                .build();
        return execute(req);
    }

    // ── High-level Apicurio API calls ──────────────────────────────────────────

    /** List all subjects (artifact IDs in the default group). */
    public ApicurioResponse listSubjects(String group) throws Exception {
        return get("/groups/" + encode(group) + "/artifacts");
    }

    /** Register a new schema version (artifact). */
    public ApicurioResponse registerSchema(String group, String artifactId,
                                            String schemaContent, String artifactType,
                                            String ifExists) throws Exception {
        String path = "/groups/" + encode(group) + "/artifacts"
                    + "?ifExists=" + ifExists;
        String ct = artifactTypeToContentType(artifactType);
        return post(path, schemaContent, ct + "; artifactId=" + artifactId);
    }

    /** Get a schema by artifact ID and version. */
    public ApicurioResponse getSchema(String group, String artifactId, String version) throws Exception {
        String v = (version == null || version.isEmpty() || "latest".equals(version)) ? "latest" : version;
        return get("/groups/" + encode(group) + "/artifacts/" + encode(artifactId)
                 + "/versions/" + encode(v));
    }

    /** Get schema metadata. */
    public ApicurioResponse getArtifactMeta(String group, String artifactId) throws Exception {
        return get("/groups/" + encode(group) + "/artifacts/" + encode(artifactId) + "/meta");
    }

    /** List all versions of a schema. */
    public ApicurioResponse listVersions(String group, String artifactId) throws Exception {
        return get("/groups/" + encode(group) + "/artifacts/" + encode(artifactId) + "/versions");
    }

    /** Delete a schema (all versions). */
    public ApicurioResponse deleteSchema(String group, String artifactId) throws Exception {
        return delete("/groups/" + encode(group) + "/artifacts/" + encode(artifactId));
    }

    /** Delete a specific version. */
    public ApicurioResponse deleteVersion(String group, String artifactId, String version) throws Exception {
        return delete("/groups/" + encode(group) + "/artifacts/" + encode(artifactId)
                    + "/versions/" + encode(version));
    }

    /** Get the global artifact rule (compatibility). */
    public ApicurioResponse getCompatibilityRule(String group, String artifactId) throws Exception {
        return get("/groups/" + encode(group) + "/artifacts/" + encode(artifactId)
                 + "/rules/COMPATIBILITY");
    }

    /** Set the compatibility rule on an artifact. */
    public ApicurioResponse setCompatibilityRule(String group, String artifactId, String mode) throws Exception {
        String body = "{\"type\":\"COMPATIBILITY\",\"config\":\"" + mode + "\"}";
        // Try PUT first (update existing rule), fall back to POST (create)
        ApicurioResponse r = put("/groups/" + encode(group) + "/artifacts/" + encode(artifactId)
                                + "/rules/COMPATIBILITY", body);
        if (r.statusCode == 404) {
            r = post("/groups/" + encode(group) + "/artifacts/" + encode(artifactId) + "/rules",
                     body, "application/json");
        }
        return r;
    }

    /** Test compatibility: check a new schema against the existing versions. */
    public ApicurioResponse testCompatibility(String group, String artifactId,
                                               String version, String schemaContent,
                                               String artifactType) throws Exception {
        String v   = (version == null || version.isEmpty() || "latest".equals(version)) ? "latest" : version;
        String ct  = artifactTypeToContentType(artifactType);
        return post("/groups/" + encode(group) + "/artifacts/" + encode(artifactId)
                  + "/versions/" + encode(v) + "/compatibility",
                    schemaContent, ct);
    }

    /** Validate a record against a schema (Apicurio content rules). */
    public ApicurioResponse validateContent(String group, String artifactId,
                                             String version, String record) throws Exception {
        // Apicurio v2: validate by posting to /versions/{version}/content
        // We POST the record and expect 200 if valid, 400 if invalid
        String path = "/groups/" + encode(group) + "/artifacts/" + encode(artifactId)
                    + "/versions/" + encode(version) + "/references";
        // Use custom validation endpoint
        return post("/groups/" + encode(group) + "/artifacts/" + encode(artifactId)
                  + "/test", record, "application/json");
    }

    /** Update artifact state (ENABLED, DISABLED, DEPRECATED). */
    public ApicurioResponse updateState(String group, String artifactId, String state) throws Exception {
        String body = "{\"state\":\"" + state + "\"}";
        return put("/groups/" + encode(group) + "/artifacts/" + encode(artifactId)
                 + "/state", body);
    }

    /** Update artifact metadata (name, description, labels). */
    public ApicurioResponse updateMeta(String group, String artifactId, String metaJson) throws Exception {
        return put("/groups/" + encode(group) + "/artifacts/" + encode(artifactId) + "/meta", metaJson);
    }

    /** Search artifacts by keyword. */
    public ApicurioResponse searchArtifacts(String query, String group, String artifactType,
                                             int limit, int offset) throws Exception {
        StringBuilder sb = new StringBuilder("/search/artifacts?limit=" + limit + "&offset=" + offset);
        if (query    != null && !query.isEmpty())        sb.append("&name=").append(encode(query));
        if (group    != null && !group.isEmpty())        sb.append("&group=").append(encode(group));
        if (artifactType != null && !artifactType.isEmpty()) sb.append("&artifactType=").append(encode(artifactType));
        return get(sb.toString());
    }

    /** Get global rules. */
    public ApicurioResponse getGlobalRules() throws Exception {
        return get("/admin/rules");
    }

    /** Set a global rule. */
    public ApicurioResponse setGlobalRule(String ruleType, String config) throws Exception {
        String body = "{\"type\":\"" + ruleType + "\",\"config\":\"" + config + "\"}";
        ApicurioResponse r = put("/admin/rules/" + ruleType, body);
        if (r.statusCode == 404) {
            r = post("/admin/rules", body, "application/json");
        }
        return r;
    }

    /** Health check. */
    public ApicurioResponse health() throws Exception {
        return get("/system/info");
    }

    /** List groups (tenants). */
    public ApicurioResponse listGroups() throws Exception {
        return get("/groups");
    }

    /** Create a group. */
    public ApicurioResponse createGroup(String groupId, String description) throws Exception {
        String body = "{\"id\":\"" + groupId + "\",\"description\":\"" + description + "\"}";
        return post("/groups", body, "application/json");
    }

    /** Delete a group. */
    public ApicurioResponse deleteGroup(String groupId) throws Exception {
        return delete("/groups/" + encode(groupId));
    }

    // ── Internals ─────────────────────────────────────────────────────────────

    private HttpRequest.Builder requestBuilder(String path) {
        HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .timeout(Duration.ofMillis(cfg.timeoutMs))
                .header("Accept", "application/json");

        // Auth
        switch (cfg.authType) {
            case "basic" -> {
                String creds = Base64.getEncoder().encodeToString(
                        (cfg.username + ":" + cfg.password).getBytes(StandardCharsets.UTF_8));
                b.header("Authorization", "Basic " + creds);
            }
            case "bearer" -> b.header("Authorization", "Bearer " + cfg.bearerToken);
            // "none" — no auth header
        }
        return b;
    }

    private ApicurioResponse execute(HttpRequest req) throws Exception {
        log.debug("Apicurio {} {}", req.method(), req.uri());
        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        log.debug("Apicurio response: {} {}", resp.statusCode(), req.uri());

        String body = resp.body() != null ? resp.body() : "{}";
        JsonNode node;
        try {
            node = json.readTree(body.isEmpty() ? "{}" : body);
        } catch (Exception e) {
            // Body may be plain text (e.g. schema content)
            node = json.createObjectNode();
            ((com.fasterxml.jackson.databind.node.ObjectNode) node).put("raw", body);
        }
        return new ApicurioResponse(resp.statusCode(), node, body);
    }

    private String artifactTypeToContentType(String type) {
        return switch (type.toUpperCase()) {
            case "AVRO"     -> "application/json"; // Avro schemas are JSON
            case "JSON"     -> "application/json";
            case "PROTOBUF" -> "application/x-protobuf";
            case "OPENAPI"  -> "application/json";
            default         -> "application/json";
        };
    }

    private String encode(String s) {
        try {
            return java.net.URLEncoder.encode(s, StandardCharsets.UTF_8).replace("+", "%20");
        } catch (Exception e) {
            return s;
        }
    }

    /** Response container. */
    public record ApicurioResponse(int statusCode, JsonNode body, String rawBody) {
        public boolean isSuccess() { return statusCode >= 200 && statusCode < 300; }
        public boolean isNotFound() { return statusCode == 404; }
        public boolean isConflict()  { return statusCode == 409; }
    }
}
