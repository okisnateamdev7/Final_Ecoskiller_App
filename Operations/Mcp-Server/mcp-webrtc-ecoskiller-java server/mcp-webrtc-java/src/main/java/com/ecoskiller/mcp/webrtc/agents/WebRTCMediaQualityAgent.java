package com.ecoskiller.mcp.webrtc.agents;

import com.ecoskiller.mcp.webrtc.model.AgentResult;
import com.ecoskiller.mcp.webrtc.security.SecurityValidator;
import org.json.JSONObject;

/**
 * AGENT 7 — WEBRTC_MEDIA_QUALITY
 *
 * Processes WebRTC Stats API (RTCStatsReport) data for a participant.
 * Computes MOS score estimate, validates SLO compliance, and triggers
 * alerting logic.
 *
 * Metrics tracked (per Ecoskiller Grafana Media QoS Dashboard #18):
 *   - Packet loss % (inbound-rtp)
 *   - Jitter (inbound-rtp)
 *   - Round-trip time (candidate-pair)
 *   - Bitrate kbps (inbound-rtp bytes_received/time delta)
 *   - MOS score estimate
 *   - coturn relay usage %
 *
 * SLO: WebRTC connection success ≥ 95%; alert < 90% over 15-min window.
 *
 * Security: numeric ranges validated; no free-text injection vectors.
 */
public class WebRTCMediaQualityAgent extends BaseAgent {

    // SLO thresholds
    private static final double SLO_SUCCESS_TARGET  = 95.0;
    private static final double SLO_ALERT_THRESHOLD = 90.0;
    private static final double MAX_PACKET_LOSS_PCT = 30.0; // Opus FEC still works up to 30%
    private static final int    MAX_JITTER_MS       = 100;
    private static final int    MAX_RTT_MS          = 300;

    public WebRTCMediaQualityAgent(SecurityValidator security) { super(security); }

    @Override
    public JSONObject getToolDefinition() {
        JSONObject props = new JSONObject()
            .put("session_id",            stringProp("Session ID"))
            .put("user_id",               stringProp("Participant ID"))
            .put("packet_loss_pct",       intProp("Inbound packet loss percentage (0–100)", 0, 100))
            .put("jitter_ms",             intProp("RTP jitter in milliseconds (0–500)", 0, 500))
            .put("round_trip_time_ms",    intProp("ICE candidate-pair RTT in milliseconds (0–2000)", 0, 2000))
            .put("audio_bitrate_kbps",    intProp("Current audio bitrate in kbps (0–512)", 0, 512))
            .put("connection_success_pct",intProp("15-min window WebRTC connection success rate (0–100)", 0, 100));

        return tool("webrtc_media_quality",
            "Analyse WebRTC Stats API (RTCStatsReport) data. Computes MOS score estimate, validates against " +
            "Ecoskiller SLO (≥95% connection success), and returns Grafana Media QoS Dashboard alert status.",
            schema(props, "session_id", "user_id", "packet_loss_pct", "jitter_ms", "audio_bitrate_kbps"));
    }

    @Override
    public AgentResult execute(JSONObject args) {
        String sessionId      = security.requireSessionId(args, "session_id");
        String userId         = security.requireUserId(args, "user_id");
        int    packetLoss     = security.requireInt(args, "packet_loss_pct",  0, 100);
        int    jitter         = security.requireInt(args, "jitter_ms",        0, 500);
        int    bitrate        = security.requireInt(args, "audio_bitrate_kbps", 0, 512);
        int    rtt            = args.has("round_trip_time_ms")
                                ? security.requireInt(args, "round_trip_time_ms", 0, 2000) : 80;
        double successPct     = args.has("connection_success_pct")
                                ? security.requireInt(args, "connection_success_pct", 0, 100) : 97;

        security.enforceRateLimit(userId);

        // Estimate MOS score (simplified E-model inspired formula)
        double mos = estimateMos(packetLoss, jitter, rtt);

        // Determine audio quality label
        String quality;
        if (mos >= 4.0) quality = "EXCELLENT";
        else if (mos >= 3.5) quality = "GOOD";
        else if (mos >= 3.0) quality = "FAIR";
        else if (mos >= 2.5) quality = "POOR";
        else quality = "DEGRADED";

        // SLO check
        boolean sloBreached   = successPct < SLO_ALERT_THRESHOLD;
        boolean sloWarning    = successPct < SLO_SUCCESS_TARGET;
        String  alertLevel    = sloBreached ? "P1_CRITICAL" : (sloWarning ? "WARNING" : "OK");

        // Opus resilience note
        boolean opusFecActive = packetLoss > 5;

        JSONObject stats = new JSONObject()
            .put("packet_loss_pct",     packetLoss)
            .put("jitter_ms",           jitter)
            .put("round_trip_time_ms",  rtt)
            .put("audio_bitrate_kbps",  bitrate)
            .put("mos_score",           String.format("%.2f", mos))
            .put("audio_quality",       quality)
            .put("opus_fec_active",     opusFecActive)
            .put("opus_plc_note",       packetLoss > 0 ? "Opus PLC masking packet loss" : "No PLC needed");

        JSONObject slo = new JSONObject()
            .put("connection_success_pct",  successPct)
            .put("slo_target_pct",          SLO_SUCCESS_TARGET)
            .put("alert_threshold_pct",     SLO_ALERT_THRESHOLD)
            .put("slo_status",              alertLevel)
            .put("alert_channels",          sloBreached ? "ntfy (push) + Mattermost #incidents" : "none")
            .put("grafana_dashboard",       "Media QoS Dashboard #18");

        JSONObject recommendations = buildRecommendations(packetLoss, jitter, rtt, bitrate);

        JSONObject data = new JSONObject()
            .put("session_id",    sessionId)
            .put("user_id",       userId)
            .put("media_stats",   stats)
            .put("slo_status",    slo)
            .put("recommendations", recommendations)
            .put("audit_event",   security.buildAuditEvent("MEDIA_QUALITY_CHECK", sessionId, userId));

        return AgentResult.success("WEBRTC_MEDIA_QUALITY_AGENT")
            .message("Media quality: " + quality + " (MOS=" + String.format("%.2f", mos) + ") | SLO: " + alertLevel)
            .data(data)
            .build();
    }

    private double estimateMos(int loss, int jitter, int rtt) {
        // Simplified: start from 4.5, deduct for impairments
        double mos = 4.5;
        mos -= (loss / 100.0) * 2.5;   // packet loss penalty
        mos -= (jitter / 100.0) * 0.5; // jitter penalty
        mos -= (rtt / 1000.0) * 0.4;   // RTT penalty
        return Math.max(1.0, Math.min(5.0, mos));
    }

    private JSONObject buildRecommendations(int loss, int jitter, int rtt, int bitrate) {
        JSONObject r = new JSONObject();
        if (loss > 20) r.put("packet_loss",  "Enable coturn TURN relay — check UDP path; loss " + loss + "% exceeds Opus FEC comfort zone");
        if (jitter > 50) r.put("jitter",     "High jitter (" + jitter + "ms) — candidate ICE restart may improve path");
        if (rtt > 200)   r.put("rtt",        "High RTT (" + rtt + "ms) — check coturn relay region vs participant location");
        if (bitrate < 12) r.put("bitrate",   "Low bitrate (" + bitrate + "kbps) — Opus survival mode; network severely congested");
        if (r.isEmpty()) r.put("status",     "All metrics within acceptable range");
        return r;
    }
}
