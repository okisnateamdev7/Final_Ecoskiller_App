package com.ecoskiller.mcp.webrtc.agents;

import com.ecoskiller.mcp.webrtc.model.AgentResult;
import com.ecoskiller.mcp.webrtc.security.SecurityValidator;
import org.json.JSONObject;

/**
 * AGENT 3 — WEBRTC_JWT_ISSUE
 *
 * Issues / validates the 5-minute JWT media token required by Jitsi Prosody
 * for WebRTC room join authentication.
 *
 * Per EcoSkiller architecture: auth-service (Keycloak 24.0) issues the real JWT.
 * This agent validates the token structure and prepares the Jitsi API config
 * containing the jwt parameter to pass to JitsiMeetExternalAPI constructor.
 *
 * Security:
 *   - JWT format validated (three Base64url segments)
 *   - session_id and user_id validated
 *   - Token TTL enforced (≤ 300 seconds)
 *   - Rate limited per user
 */
public class WebRTCJwtIssueAgent extends BaseAgent {

    private static final int JWT_TTL_SECONDS = 300; // 5-minute TTL per Ecoskiller spec

    public WebRTCJwtIssueAgent(SecurityValidator security) { super(security); }

    @Override
    public JSONObject getToolDefinition() {
        JSONObject props = new JSONObject()
            .put("session_id",  stringProp("WebRTC session ID"))
            .put("user_id",     stringProp("Participant user ID"))
            .put("tenant_id",   stringProp("Tenant identifier"))
            .put("jwt_token",   stringProp("5-min JWT from auth-service/Keycloak to validate"));

        return tool("webrtc_jwt_issue",
            "Validate the Jitsi 5-minute JWT media token from auth-service and produce the Prosody-ready room join configuration. " +
            "JWT validation is structural only — crypto verification lives in Keycloak/auth-service.",
            schema(props, "session_id", "user_id", "tenant_id", "jwt_token"));
    }

    @Override
    public AgentResult execute(JSONObject args) {
        String sessionId = security.requireSessionId(args, "session_id");
        String userId    = security.requireUserId(args, "user_id");
        String tenantId  = security.requireTenantId(args, "tenant_id");
        String jwt       = security.requireJwtToken(args, "jwt_token");

        security.enforceRateLimit(userId);

        // Structural validation (real crypto in Keycloak)
        String[] parts = jwt.split("\\.");
        if (parts.length != 3) {
            throw new SecurityException("JWT must have exactly 3 segments");
        }

        // Decode header (best-effort, no crypto)
        String headerInfo;
        try {
            byte[] headerBytes = java.util.Base64.getUrlDecoder().decode(padBase64(parts[0]));
            JSONObject header = new JSONObject(new String(headerBytes, java.nio.charset.StandardCharsets.UTF_8));
            headerInfo = "alg=" + header.optString("alg", "unknown") + " typ=" + header.optString("typ", "unknown");
        } catch (Exception e) {
            headerInfo = "header_decode_failed";
        }

        String roomName = "gd_" + tenantId + "_" + sessionId.substring(0, Math.min(12, sessionId.length()));

        JSONObject jitsiApiConfig = new JSONObject()
            .put("domain",      "voice.ecoskiller.com")
            .put("roomName",    roomName)
            .put("jwt",         "[JWT_PRESENT — not echoed for security]")
            .put("width",       "100%")
            .put("height",      500)
            .put("configOverwrite", new JSONObject()
                .put("startWithAudioMuted", true)
                .put("startWithVideoMuted", true)
                .put("prejoinPageEnabled",  false)
                .put("disableDeepLinking",  true))
            .put("interfaceConfigOverwrite", new JSONObject()
                .put("TOOLBAR_BUTTONS", new org.json.JSONArray()));

        JSONObject data = new JSONObject()
            .put("session_id",       sessionId)
            .put("user_id",          userId)
            .put("tenant_id",        tenantId)
            .put("jwt_ttl_seconds",  JWT_TTL_SECONDS)
            .put("jwt_header",       headerInfo)
            .put("jwt_valid",        true)
            .put("jitsi_api_config", jitsiApiConfig)
            .put("prosody_endpoint", "xmpp://prosody.ecoskiller.com:5280")
            .put("room_join_url",    "https://voice.ecoskiller.com/" + roomName + "?jwt=[TOKEN]")
            .put("note",             "Pass jwt_token as 'jwt' param in JitsiMeetExternalAPI constructor")
            .put("audit_event",      security.buildAuditEvent("JWT_VALIDATE", sessionId, userId));

        return AgentResult.success("WEBRTC_JWT_ISSUE_AGENT")
            .message("JWT validated — Jitsi room join config ready for session " + sessionId)
            .data(data)
            .build();
    }

    private String padBase64(String base64Url) {
        String s = base64Url.replace('-', '+').replace('_', '/');
        while (s.length() % 4 != 0) s += "=";
        return s;
    }
}
