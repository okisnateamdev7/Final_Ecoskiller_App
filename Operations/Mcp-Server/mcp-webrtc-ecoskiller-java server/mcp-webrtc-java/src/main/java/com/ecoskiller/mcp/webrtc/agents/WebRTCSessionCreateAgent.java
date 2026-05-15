package com.ecoskiller.mcp.webrtc.agents;

import com.ecoskiller.mcp.webrtc.model.AgentResult;
import com.ecoskiller.mcp.webrtc.security.SecurityValidator;
import org.json.JSONObject;

import java.util.UUID;

/**
 * AGENT 1 — WEBRTC_SESSION_CREATE
 *
 * Creates a new WebRTC session (GD / Interview / Dojo).
 * Validates all inputs, enforces session mode rules:
 *   - GD:        audio-only, startWithVideoMuted=true
 *   - Interview: audio+video, startWithVideoMuted=false
 *   - Dojo:      audio-only (optional), no mandatory video
 *
 * Security: session_id generated server-side (UUID), tenant & user validated.
 */
public class WebRTCSessionCreateAgent extends BaseAgent {

    public WebRTCSessionCreateAgent(SecurityValidator security) { super(security); }

    @Override
    public JSONObject getToolDefinition() {
        JSONObject props = new JSONObject()
            .put("tenant_id",    stringProp("Tenant identifier (4–32 alphanumeric chars)"))
            .put("user_id",      stringProp("Host/organiser user ID"))
            .put("session_mode", stringProp("Session type: gd | interview | dojo | pstn"))
            .put("topic",        stringProp("GD/Interview topic (max 256 chars)"))
            .put("max_participants", intProp("Maximum allowed participants", 2, 50));

        return tool("webrtc_session_create",
            "Create a new WebRTC session on the Ecoskiller platform. Returns session_id, room_name, and Jitsi config.",
            schema(props, "tenant_id", "user_id", "session_mode"));
    }

    @Override
    public AgentResult execute(JSONObject args) {
        // ── Security ──────────────────────────────────────────────────
        String tenantId = security.requireTenantId(args, "tenant_id");
        String userId   = security.requireUserId(args, "user_id");
        String mode     = security.requireSessionMode(args, "session_mode");
        String topic    = security.optionalSafeString(args, "topic", 256).orElse("(no topic)");
        int    maxP     = args.has("max_participants")
                          ? security.requireInt(args, "max_participants", 2, 50)
                          : 12;

        security.enforceRateLimit(tenantId + ":" + userId);

        // ── Business logic ────────────────────────────────────────────
        String sessionId = UUID.randomUUID().toString();
        String roomName  = "gd_" + tenantId + "_" + sessionId.replace("-", "").substring(0, 12);
        boolean videoEnabled = "interview".equals(mode);

        JSONObject jitsiConfig = new JSONObject()
            .put("roomName", roomName)
            .put("startWithAudioMuted", true)
            .put("startWithVideoMuted", !videoEnabled)
            .put("disableVideo", !videoEnabled)
            .put("prejoinPageEnabled", false)
            .put("disableDeepLinking", true)
            .put("toolbarButtons", videoEnabled
                ? new org.json.JSONArray(new String[]{"microphone", "camera", "hangup"})
                : new org.json.JSONArray(new String[]{"microphone", "hangup"}));

        JSONObject data = new JSONObject()
            .put("session_id",      sessionId)
            .put("room_name",       roomName)
            .put("tenant_id",       tenantId)
            .put("host_user_id",    userId)
            .put("session_mode",    mode)
            .put("topic",           topic)
            .put("max_participants",maxP)
            .put("video_enabled",   videoEnabled)
            .put("encryption",      "DTLS-SRTP AES-128-CM (mandatory)")
            .put("audio_codec",     "Opus RFC 6716 (6–510 kbps adaptive)")
            .put("video_codec",     videoEnabled ? "VP8/VP9 with simulcast" : "disabled")
            .put("jitsi_config",    jitsiConfig)
            .put("slo_target",      "WebRTC connection success ≥ 95%")
            .put("audit_event",     security.buildAuditEvent("SESSION_CREATE", sessionId, userId));

        return AgentResult.success("WEBRTC_SESSION_CREATE_AGENT")
            .message("WebRTC session created for mode=" + mode)
            .data(data)
            .build();
    }
}
