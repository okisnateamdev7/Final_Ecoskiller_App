package com.ecoskiller.mcp.session.client;

import com.ecoskiller.mcp.session.config.SessionControlConfig;
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
 * HTTP client for the Ecoskiller Session-Control-Service REST API.
 *
 * Covers all endpoints defined in the OpenAPI 3.0 contract (doc section 11):
 *   POST   /sessions
 *   GET    /sessions/{id}
 *   GET    /sessions
 *   PATCH  /sessions/{id}/state
 *   DELETE /sessions/{id}
 *   POST   /sessions/{id}/participants/join
 *   POST   /sessions/{id}/participants/{pid}/leave
 *   GET    /sessions/{id}/participants
 *   PATCH  /sessions/{id}/participants/{pid}/heartbeat
 *   DELETE /sessions/{id}/participants/{pid}
 *   GET    /sessions/{id}/audit-log
 *   GET    /health/live
 *   GET    /health/ready
 *   GET    /metrics
 */
public class SessionControlClient {

    private static final Logger log = LoggerFactory.getLogger(SessionControlClient.class);

    private final SessionControlConfig cfg;
    private final HttpClient           http;
    private final ObjectMapper         json = new ObjectMapper();
    private final String               base;

    public SessionControlClient(SessionControlConfig cfg) {
        this.cfg  = cfg;
        this.base = cfg.baseUrl.replaceAll("/$", "");

        HttpClient.Builder b = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(cfg.timeoutMs))
                .version(HttpClient.Version.HTTP_1_1);

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
                b.sslContext(sc);
            } catch (Exception e) {
                log.warn("Could not configure TLS bypass: {}", e.getMessage());
            }
        }
        this.http = b.build();
    }

    // ── Core HTTP methods ──────────────────────────────────────────────────────

    public ServiceResponse get(String path) throws Exception {
        return execute(builder(path).GET().build());
    }

    public ServiceResponse post(String path, String body) throws Exception {
        return execute(builder(path)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build());
    }

    public ServiceResponse patch(String path, String body) throws Exception {
        return execute(builder(path)
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build());
    }

    public ServiceResponse delete(String path) throws Exception {
        return execute(builder(path).DELETE().build());
    }

    // ── Session API methods ───────────────────────────────────────────────────

    /** POST /sessions — Create a new assessment session. */
    public ServiceResponse createSession(String body) throws Exception {
        return post("/sessions", body);
    }

    /** GET /sessions/{id} — Get session by ID. */
    public ServiceResponse getSession(String sessionId) throws Exception {
        return get("/sessions/" + enc(sessionId));
    }

    /** GET /sessions — List sessions (with optional query params). */
    public ServiceResponse listSessions(String queryString) throws Exception {
        String qs = (queryString == null || queryString.isEmpty()) ? "" : "?" + queryString;
        return get("/sessions" + qs);
    }

    /** PATCH /sessions/{id}/state — Transition session state. */
    public ServiceResponse updateSessionState(String sessionId, String body) throws Exception {
        return patch("/sessions/" + enc(sessionId) + "/state", body);
    }

    /** DELETE /sessions/{id} — Archive/soft-delete a session. */
    public ServiceResponse deleteSession(String sessionId) throws Exception {
        return delete("/sessions/" + enc(sessionId));
    }

    /** POST /sessions/{id}/participants/join — Participant join. */
    public ServiceResponse joinSession(String sessionId, String body) throws Exception {
        return post("/sessions/" + enc(sessionId) + "/participants/join", body);
    }

    /** POST /sessions/{id}/participants/{pid}/leave — Participant leave. */
    public ServiceResponse leaveSession(String sessionId, String participantId, String body) throws Exception {
        return post("/sessions/" + enc(sessionId) + "/participants/" + enc(participantId) + "/leave", body);
    }

    /** GET /sessions/{id}/participants — List participants. */
    public ServiceResponse listParticipants(String sessionId) throws Exception {
        return get("/sessions/" + enc(sessionId) + "/participants");
    }

    /** PATCH /sessions/{id}/participants/{pid}/heartbeat — Update heartbeat. */
    public ServiceResponse heartbeat(String sessionId, String participantId) throws Exception {
        return patch("/sessions/" + enc(sessionId) + "/participants/" + enc(participantId) + "/heartbeat", "{}");
    }

    /** DELETE /sessions/{id}/participants/{pid} — Evict participant. */
    public ServiceResponse evictParticipant(String sessionId, String participantId) throws Exception {
        return delete("/sessions/" + enc(sessionId) + "/participants/" + enc(participantId));
    }

    /** PATCH /sessions/{id}/participants/{pid}/role — Update participant role. */
    public ServiceResponse updateParticipantRole(String sessionId, String participantId, String body) throws Exception {
        return patch("/sessions/" + enc(sessionId) + "/participants/" + enc(participantId) + "/role", body);
    }

    /** GET /sessions/{id}/audit-log — Get audit log for session. */
    public ServiceResponse getAuditLog(String sessionId, String queryString) throws Exception {
        String qs = (queryString == null || queryString.isEmpty()) ? "" : "?" + queryString;
        return get("/sessions/" + enc(sessionId) + "/audit-log" + qs);
    }

    /** GET /sessions/{id}/events — Get session events/history. */
    public ServiceResponse getSessionEvents(String sessionId) throws Exception {
        return get("/sessions/" + enc(sessionId) + "/events");
    }

    /** POST /sessions/{id}/jitsi/token — Generate Jitsi room token. */
    public ServiceResponse getJitsiToken(String sessionId, String body) throws Exception {
        return post("/sessions/" + enc(sessionId) + "/jitsi/token", body);
    }

    /** POST /sessions/{id}/jitsi/mute — Mute a participant in Jitsi. */
    public ServiceResponse muteParticipant(String sessionId, String body) throws Exception {
        return post("/sessions/" + enc(sessionId) + "/jitsi/mute", body);
    }

    /** POST /sessions/{id}/jitsi/force-end — Force-end Jitsi room. */
    public ServiceResponse forceEndJitsi(String sessionId, String body) throws Exception {
        return post("/sessions/" + enc(sessionId) + "/jitsi/force-end", body);
    }

    /** GET /health/live — Liveness probe. */
    public ServiceResponse healthLive() throws Exception {
        return get("/health/live");
    }

    /** GET /health/ready — Readiness probe. */
    public ServiceResponse healthReady() throws Exception {
        return get("/health/ready");
    }

    /** GET /sessions/stats — Active session statistics. */
    public ServiceResponse getStats(String tenantId) throws Exception {
        String qs = tenantId.isEmpty() ? "" : "?tenantId=" + enc(tenantId);
        return get("/sessions/stats" + qs);
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private HttpRequest.Builder builder(String path) {
        HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(URI.create(base + path))
                .timeout(Duration.ofMillis(cfg.timeoutMs))
                .header("Accept", "application/json");

        if ("bearer".equals(cfg.authType) && !cfg.bearerToken.isEmpty()) {
            b.header("Authorization", "Bearer " + cfg.bearerToken);
        }
        if (!cfg.defaultTenantId.isEmpty()) {
            b.header("X-Tenant-ID", cfg.defaultTenantId);
        }
        return b;
    }

    private ServiceResponse execute(HttpRequest req) throws Exception {
        log.debug("Request {} {}", req.method(), req.uri());
        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        String body = resp.body() != null ? resp.body() : "{}";
        JsonNode node;
        try {
            node = json.readTree(body.isEmpty() ? "{}" : body);
        } catch (Exception e) {
            node = json.createObjectNode();
            ((com.fasterxml.jackson.databind.node.ObjectNode) node).put("raw", body);
        }
        log.debug("Response {} {}", resp.statusCode(), req.uri());
        return new ServiceResponse(resp.statusCode(), node, body);
    }

    private String enc(String s) {
        try { return java.net.URLEncoder.encode(s, StandardCharsets.UTF_8).replace("+", "%20"); }
        catch (Exception e) { return s; }
    }

    /** HTTP response container. */
    public record ServiceResponse(int statusCode, JsonNode body, String rawBody) {
        public boolean isSuccess()  { return statusCode >= 200 && statusCode < 300; }
        public boolean isNotFound() { return statusCode == 404; }
        public boolean isConflict() { return statusCode == 409; }
        public boolean isForbidden(){ return statusCode == 403; }
    }
}
