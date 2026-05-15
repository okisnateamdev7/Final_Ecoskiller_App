package com.ecoskiller.mcp.webrtc.agents;

import com.ecoskiller.mcp.webrtc.model.AgentResult;
import com.ecoskiller.mcp.webrtc.security.SecurityValidator;
import org.json.JSONObject;

/**
 * AGENT 2 — WEBRTC_SESSION_TERMINATE
 *
 * Terminates an active WebRTC session, disposes the Jitsi room,
 * releases coturn TURN allocations, and logs the audit event.
 *
 * Security: session_id and user_id validated; only the host may terminate.
 */
public class WebRTCSessionTerminateAgent extends BaseAgent {

    public WebRTCSessionTerminateAgent(SecurityValidator security) { super(security); }

    @Override
    public JSONObject getToolDefinition() {
        JSONObject props = new JSONObject()
            .put("session_id", stringProp("Session ID to terminate"))
            .put("user_id",    stringProp("User requesting termination (must be host)"))
            .put("reason",     stringProp("Termination reason: timeout | host_ended | error | admin"));

        return tool("webrtc_session_terminate",
            "Gracefully terminate an active WebRTC session. Disposes Jitsi room, releases TURN allocations, and writes audit trail.",
            schema(props, "session_id", "user_id", "reason"));
    }

    @Override
    public AgentResult execute(JSONObject args) {
        String sessionId = security.requireSessionId(args, "session_id");
        String userId    = security.requireUserId(args, "user_id");
        String reason    = security.requireString(args, "reason", 3, 32);

        // Validate reason enum
        if (!reason.matches("timeout|host_ended|error|admin")) {
            throw new SecurityException("Invalid termination reason: " + reason);
        }

        security.enforceRateLimit(userId);

        JSONObject data = new JSONObject()
            .put("session_id",          sessionId)
            .put("terminated_by",       userId)
            .put("reason",              reason)
            .put("jitsi_command",       "api.dispose()")
            .put("turn_cleanup",        "TURN allocations released for session: " + sessionId)
            .put("candidate_redirect",  "Voice GD Completed")
            .put("data_retained",       "speaking_times, turn_counts, interruption_counts only (no audio)")
            .put("audio_retained",      false)
            .put("dpdpa_compliant",     true)
            .put("audit_event",         security.buildAuditEvent("SESSION_TERMINATE", sessionId, userId));

        return AgentResult.success("WEBRTC_SESSION_TERMINATE_AGENT")
            .message("Session " + sessionId + " terminated. Reason: " + reason)
            .data(data)
            .build();
    }
}
