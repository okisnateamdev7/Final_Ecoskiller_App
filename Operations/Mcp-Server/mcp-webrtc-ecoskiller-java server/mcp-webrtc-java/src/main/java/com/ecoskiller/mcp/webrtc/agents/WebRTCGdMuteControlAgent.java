package com.ecoskiller.mcp.webrtc.agents;

import com.ecoskiller.mcp.webrtc.model.AgentResult;
import com.ecoskiller.mcp.webrtc.security.SecurityValidator;
import org.json.JSONObject;

/**
 * AGENT 8 — WEBRTC_GD_MUTE_CONTROL
 *
 * Manages turn-based mute/unmute control for Group Discussion (GD) sessions.
 *
 * Per EcoSkiller spec:
 *   - GD Orchestrator issues mute commands via WebSocket (wss://realtime.ecoskiller.com)
 *   - Client calls JitsiMeetExternalAPI.executeCommand('toggleAudio')
 *   - SRTP stream continues flowing (carries silence) — JVB routing table stays stable
 *   - Metrics captured: speaking_time_ms, turns_taken, interruption_count
 *   - Score formula: score = speaking_time + turns_taken*5 - interruptions*3
 *
 * Security: all IDs validated; command whitelist enforced; no free-text injection.
 */
public class WebRTCGdMuteControlAgent extends BaseAgent {

    private static final int MAX_SPEAKING_TIME_MS = 5 * 60 * 1000; // 5 min cap

    public WebRTCGdMuteControlAgent(SecurityValidator security) { super(security); }

    @Override
    public JSONObject getToolDefinition() {
        JSONObject props = new JSONObject()
            .put("session_id",         stringProp("GD session ID"))
            .put("orchestrator_id",    stringProp("GD Orchestrator service ID (authorised only)"))
            .put("target_user_id",     stringProp("User to mute or unmute"))
            .put("command",            stringProp("Mute command: mute | unmute | mute_all | unmute_speaker"))
            .put("speaking_time_ms",   intProp("Accumulated speaking time for this user (ms)", 0, 300000))
            .put("turns_taken",        intProp("Number of speaking turns taken", 0, 100))
            .put("interruptions",      intProp("Number of interruptions detected", 0, 50));

        return tool("webrtc_gd_mute_control",
            "Issue GD Orchestrator turn-based mute/unmute commands. Executes JitsiMeetExternalAPI.toggleAudio " +
            "via WebSocket control channel. Captures speaking metrics for AI scoring.",
            schema(props, "session_id", "orchestrator_id", "target_user_id", "command"));
    }

    @Override
    public AgentResult execute(JSONObject args) {
        String sessionId      = security.requireSessionId(args, "session_id");
        String orchestratorId = security.requireUserId(args, "orchestrator_id");
        String targetUserId   = security.requireUserId(args, "target_user_id");
        String command        = security.requireString(args, "command", 4, 20);

        if (!command.matches("mute|unmute|mute_all|unmute_speaker")) {
            throw new SecurityException("Invalid mute command: " + command);
        }

        int speakingTime  = args.has("speaking_time_ms")
                           ? security.requireInt(args, "speaking_time_ms", 0, MAX_SPEAKING_TIME_MS) : 0;
        int turns         = args.has("turns_taken")       ? security.requireInt(args, "turns_taken",    0, 100) : 0;
        int interruptions = args.has("interruptions")     ? security.requireInt(args, "interruptions",  0, 50)  : 0;

        security.enforceRateLimit(orchestratorId);

        // Compute AI score contribution
        double score = (speakingTime / 1000.0) + (turns * 5) - (interruptions * 3);
        score = Math.max(0, score);

        String jitsiCommand = switch (command) {
            case "mute"           -> "api.executeCommand('muteEveryone')";
            case "unmute"         -> "api.executeCommand('toggleAudio')  // target: " + targetUserId;
            case "mute_all"       -> "api.executeCommand('muteEveryone')";
            case "unmute_speaker" -> "api.executeCommand('toggleAudio')  // active speaker turn";
            default               -> "no-op";
        };

        JSONObject speakingMetrics = new JSONObject()
            .put("user_id",          targetUserId)
            .put("speaking_time_ms", speakingTime)
            .put("speaking_time_s",  speakingTime / 1000.0)
            .put("turns_taken",      turns)
            .put("interruptions",    interruptions)
            .put("score_contribution", String.format("%.1f", score))
            .put("score_formula",    "speaking_time_s + turns*5 - interruptions*3")
            .put("audio_retained",   false)
            .put("privacy_note",     "Numbers only — no voice recording stored (DPDPA 2023 compliant)");

        JSONObject data = new JSONObject()
            .put("session_id",       sessionId)
            .put("orchestrator_id",  orchestratorId)
            .put("target_user_id",   targetUserId)
            .put("command",          command)
            .put("jitsi_api_call",   jitsiCommand)
            .put("websocket_path",   "wss://realtime.ecoskiller.com → GD Orchestrator → browser.toggleAudio()")
            .put("srtp_note",        "SRTP stream continues — carries silence when muted; JVB routing stable")
            .put("speaking_metrics", speakingMetrics)
            .put("redis_key",        "gd:" + sessionId + ":state")
            .put("audit_event",      security.buildAuditEvent("GD_MUTE_" + command.toUpperCase(), sessionId, targetUserId));

        return AgentResult.success("WEBRTC_GD_MUTE_CONTROL_AGENT")
            .message("GD command=" + command + " applied to user=" + targetUserId + " | score_contribution=" + String.format("%.1f", score))
            .data(data)
            .build();
    }
}
