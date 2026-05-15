package com.ecoskiller.mcp.webrtc.agents;

import com.ecoskiller.mcp.webrtc.model.AgentResult;
import com.ecoskiller.mcp.webrtc.security.SecurityValidator;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * AGENT 11 — WEBRTC_PARTICIPANT_STATS
 *
 * Aggregates per-participant WebRTC metrics for an active GD/interview session.
 * Uses the W3C WebRTC Stats API (RTCStatsReport) data points:
 *   - inbound-rtp:  packet_loss, jitter, bytes_received
 *   - outbound-rtp: bytes_sent, packets_sent, retransmits
 *   - candidate-pair: round_trip_time, available_outgoing_bitrate
 *
 * Feeds: Grafana Media QoS Dashboard #18 and post-session AI scoring QA.
 *
 * Security: all numeric fields range-checked; user_ids validated.
 */
public class WebRTCParticipantStatsAgent extends BaseAgent {

    public WebRTCParticipantStatsAgent(SecurityValidator security) { super(security); }

    @Override
    public JSONObject getToolDefinition() {
        JSONObject props = new JSONObject()
            .put("session_id",           stringProp("Session ID"))
            .put("user_id",              stringProp("Participant to query stats for"))
            .put("bytes_received",       intProp("Inbound bytes received", 0, Integer.MAX_VALUE))
            .put("bytes_sent",           intProp("Outbound bytes sent", 0, Integer.MAX_VALUE))
            .put("packets_lost",         intProp("Packets lost (inbound-rtp)", 0, 100000))
            .put("packets_received",     intProp("Packets received (inbound-rtp)", 0, Integer.MAX_VALUE))
            .put("jitter",               intProp("RTP jitter in milliseconds", 0, 500))
            .put("retransmits",          intProp("Outbound retransmit count", 0, 10000))
            .put("available_bitrate_kbps", intProp("Available outgoing bitrate kbps", 0, 50000));

        return tool("webrtc_participant_stats",
            "Collect and analyse per-participant RTCStatsReport data. Used for Grafana Media QoS monitoring, " +
            "AI scoring QA (correlate low scores with network degradation), and SLO alerting.",
            schema(props, "session_id", "user_id"));
    }

    @Override
    public AgentResult execute(JSONObject args) {
        String sessionId       = security.requireSessionId(args, "session_id");
        String userId          = security.requireUserId(args, "user_id");

        int bytesRx   = args.has("bytes_received") ? security.requireInt(args, "bytes_received",  0, Integer.MAX_VALUE) : 0;
        int bytesTx   = args.has("bytes_sent")     ? security.requireInt(args, "bytes_sent",       0, Integer.MAX_VALUE) : 0;
        int pktsLost  = args.has("packets_lost")   ? security.requireInt(args, "packets_lost",     0, 100000)            : 0;
        int pktsRx    = args.has("packets_received")? security.requireInt(args, "packets_received",0, Integer.MAX_VALUE) : 1;
        int jitter    = args.has("jitter")          ? security.requireInt(args, "jitter",           0, 500)               : 0;
        int retransmits= args.has("retransmits")   ? security.requireInt(args, "retransmits",      0, 10000)              : 0;
        int bitrate   = args.has("available_bitrate_kbps") ? security.requireInt(args, "available_bitrate_kbps", 0, 50000) : 100;

        security.enforceRateLimit(userId);

        double lossRate = pktsRx > 0 ? (pktsLost * 100.0 / (pktsLost + pktsRx)) : 0.0;

        // Build RTCStatsReport simulation
        JSONObject inboundRtp = new JSONObject()
            .put("type",            "inbound-rtp")
            .put("kind",            "audio")
            .put("bytes_received",  bytesRx)
            .put("packets_lost",    pktsLost)
            .put("packets_received",pktsRx)
            .put("jitter",          jitter)
            .put("packet_loss_pct", String.format("%.2f", lossRate));

        JSONObject outboundRtp = new JSONObject()
            .put("type",            "outbound-rtp")
            .put("kind",            "audio")
            .put("bytes_sent",      bytesTx)
            .put("retransmits",     retransmits);

        JSONObject candidatePair = new JSONObject()
            .put("type",                       "candidate-pair")
            .put("available_outgoing_bitrate_kbps", bitrate)
            .put("connection_state",            bitrate > 20 ? "connected" : "degraded");

        // Quality assessment
        String overallQuality = lossRate < 5 && jitter < 30 ? "GOOD"
                              : lossRate < 15 && jitter < 60 ? "FAIR" : "POOR";

        JSONObject scoringQa = new JSONObject()
            .put("packet_loss_pct",   String.format("%.2f", lossRate))
            .put("stt_risk",          lossRate > 15 ? "HIGH — STT accuracy may be impaired; review AI score" : "LOW")
            .put("opus_fec_coverage", lossRate > 5 ? "FEC active — recovering up to ~20% loss" : "Not needed")
            .put("overall_quality",   overallQuality);

        JSONObject data = new JSONObject()
            .put("session_id",      sessionId)
            .put("user_id",         userId)
            .put("inbound_rtp",     inboundRtp)
            .put("outbound_rtp",    outboundRtp)
            .put("candidate_pair",  candidatePair)
            .put("scoring_qa",      scoringQa)
            .put("grafana_dashboard", "Media QoS Dashboard #18")
            .put("webrtc_stats_api","W3C RTCStatsReport")
            .put("audit_event",     security.buildAuditEvent("PARTICIPANT_STATS", sessionId, userId));

        return AgentResult.success("WEBRTC_PARTICIPANT_STATS_AGENT")
            .message("Stats for " + userId + ": quality=" + overallQuality + " loss=" + String.format("%.1f%%", lossRate))
            .data(data)
            .build();
    }
}
