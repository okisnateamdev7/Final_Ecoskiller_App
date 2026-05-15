package com.ecoskiller.mcp.webrtc.agents;

import com.ecoskiller.mcp.webrtc.model.AgentResult;
import com.ecoskiller.mcp.webrtc.security.SecurityValidator;
import org.json.JSONObject;

/**
 * AGENT 10 — WEBRTC_PSTN_BRIDGE
 *
 * Manages PSTN phone participant integration via FreeSWITCH mod_verto.
 *
 * Per EcoSkiller spec:
 *   - FreeSWITCH v1.10-release with mod_verto (WebRTC-compatible Verto over WebSocket)
 *   - PSTN caller appears as native WebRTC participant in JVB
 *   - DTLS-SRTP encryption: FreeSWITCH mod_verto ↔ JVB
 *   - Vosk STT processes PSTN audio identically to browser participants
 *   - Kafka event: phone_user_joined with type='phone' vs type='webrtc'
 *   - Phase 2 feature (not Phase 1)
 *
 * Security: phone number validated (E.164 format); no SIP credentials exposed.
 */
public class WebRTCPstnBridgeAgent extends BaseAgent {

    // E.164 phone number: +[country][number], 7-15 digits
    private static final java.util.regex.Pattern E164_PATTERN =
        java.util.regex.Pattern.compile("^\\+[1-9]\\d{6,14}$");

    public WebRTCPstnBridgeAgent(SecurityValidator security) { super(security); }

    @Override
    public JSONObject getToolDefinition() {
        JSONObject props = new JSONObject()
            .put("session_id",     stringProp("GD session ID for PSTN participant to join"))
            .put("phone_number",   stringProp("Caller phone number in E.164 format (+919876543210)"))
            .put("participant_name", stringProp("Display name for PSTN participant in GD room"))
            .put("action",         stringProp("Bridge action: join | leave | status"));

        return tool("webrtc_pstn_bridge",
            "Connect a PSTN phone caller to an active WebRTC GD session via FreeSWITCH mod_verto. " +
            "Phone participant appears as native WebRTC participant in Jitsi JVB. Phase 2 feature.",
            schema(props, "session_id", "phone_number", "action"));
    }

    @Override
    public AgentResult execute(JSONObject args) {
        String sessionId  = security.requireSessionId(args, "session_id");
        String phone      = security.requireString(args, "phone_number", 8, 16);
        String action     = security.requireString(args, "action", 4, 8);
        String name       = security.optionalSafeString(args, "participant_name", 64)
                            .orElse("Phone Participant");

        // Validate E.164
        if (!E164_PATTERN.matcher(phone).matches()) {
            throw new SecurityException("Invalid phone number — must be E.164 format: " + phone.substring(0, Math.min(4, phone.length())) + "***");
        }
        if (!action.matches("join|leave|status")) {
            throw new SecurityException("Invalid PSTN action: " + action);
        }

        security.enforceRateLimit("pstn:" + sessionId);

        // Mask phone for response (privacy)
        String maskedPhone = phone.substring(0, Math.min(4, phone.length())) + "***" +
                             phone.substring(Math.max(0, phone.length() - 2));

        String kafkaEvent = "join".equals(action)
            ? "{ \"event\": \"phone_user_joined\", \"type\": \"phone\", \"session_id\": \"" + sessionId + "\", \"masked_phone\": \"" + maskedPhone + "\" }"
            : "{ \"event\": \"phone_user_left\",   \"type\": \"phone\", \"session_id\": \"" + sessionId + "\" }";

        JSONObject freeswitchConfig = new JSONObject()
            .put("module",            "mod_verto")
            .put("protocol",          "Verto over WebSocket (WebRTC-compatible)")
            .put("freeswitch_version","v1.10-release")
            .put("jvb_endpoint",      "wss://jitsi.ecoskiller.com:8080/verto")
            .put("srtp_enabled",      true)
            .put("dtls_srtp",         "FreeSWITCH mod_verto ↔ JVB: DTLS-SRTP encrypted");

        JSONObject sttConfig = new JSONObject()
            .put("engine",            "Vosk (alphacep/kaldi-vosk-server:latest)")
            .put("audio_source",      "JVB RTP stream (same pipeline as browser participants)")
            .put("scoring",           "Identical AI scoring to WebRTC browser participants")
            .put("language",          "en-IN (Indian English primary)");

        JSONObject data = new JSONObject()
            .put("session_id",        sessionId)
            .put("phone_number",      maskedPhone)
            .put("participant_name",  name)
            .put("action",            action)
            .put("participant_type",  "phone")
            .put("freeswitch",        freeswitchConfig)
            .put("vosk_stt",          sttConfig)
            .put("kafka_event",       kafkaEvent)
            .put("phase",             "Phase 2 — PSTN bridge")
            .put("audio_path",        "PSTN → FreeSWITCH mod_conference → mod_verto → JVB (WebRTC) → Vosk STT")
            .put("audit_event",       security.buildAuditEvent("PSTN_" + action.toUpperCase(), sessionId, maskedPhone));

        return AgentResult.success("WEBRTC_PSTN_BRIDGE_AGENT")
            .message("PSTN bridge action=" + action + " for " + maskedPhone + " in session=" + sessionId)
            .data(data)
            .build();
    }
}
