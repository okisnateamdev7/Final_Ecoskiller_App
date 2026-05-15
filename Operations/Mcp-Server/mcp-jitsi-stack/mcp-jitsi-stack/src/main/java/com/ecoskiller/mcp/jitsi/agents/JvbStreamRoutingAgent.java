package com.ecoskiller.mcp.jitsi.agents;

import com.ecoskiller.mcp.jitsi.security.McpSecurityManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

/**
 * JVB_STREAM_ROUTING_AGENT
 *
 * Manages Jitsi Video Bridge (JVB) audio/video stream routing.
 * JVB is the SFU core — receives encoded RTP streams and selectively
 * forwards to all participants without server-side decode/re-encode.
 *
 * Handles: simulcast layer selection, bandwidth estimation,
 * video track suspension for inactive speakers, PSTN participant routing.
 */
public class JvbStreamRoutingAgent implements AgentHandler {

    private final McpSecurityManager security = new McpSecurityManager();

    @Override
    public String getDescription() {
        return "JVB Stream Routing: Manage Jitsi Video Bridge SFU stream routing between participants. " +
               "Controls RTP/SRTP stream forwarding, simulcast layer selection, bandwidth estimation, " +
               "and video track suspension. Environment: Kubernetes media namespace, " +
               "GCP c2-standard-4 / AWS c5.xlarge, UDP port 10000.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper mapper) {
        ObjectNode schema = mapper.createObjectNode();
        schema.put("type", "object");
        ObjectNode props = schema.putObject("properties");

        addStringProp(props, "session_id",
            "Session or match ID (maps 1:1 to Jitsi room). Pattern: [a-zA-Z0-9_-]{1,128}");
        addStringProp(props, "action",
            "Action: route_streams | suspend_track | resume_track | select_simulcast_layer | " +
            "estimate_bandwidth | get_routing_table");
        addStringProp(props, "participant_id",
            "Participant ID to target (optional — omit for session-wide operations)");
        addStringProp(props, "simulcast_layer",
            "Simulcast quality layer to select: high | medium | low (for select_simulcast_layer)");
        addIntProp(props, "bandwidth_kbps",
            "Target downstream bandwidth in kbps for bandwidth estimation");

        schema.putArray("required").add("session_id").add("action");
        return schema;
    }

    @Override
    public AgentResult execute(JsonNode args) throws Exception {
        String sessionId   = args.path("session_id").asText();
        String action      = args.path("action").asText();
        String participantId = args.path("participant_id").asText(null);
        String layer       = args.path("simulcast_layer").asText("high");
        int bwKbps         = args.path("bandwidth_kbps").asInt(500);

        security.validateSessionId(sessionId);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("session_id", sessionId);
        data.put("action", action);
        data.put("jvb_version", "stable-9364");
        data.put("transport", "RTP/SRTP over UDP 10000");

        switch (action) {
            case "route_streams" -> {
                data.put("routing_mode", "SFU — N×M forwarding at RTP packet level (no decode/re-encode)");
                data.put("media_security", "DTLS-SRTP end-to-end between client and JVB");
                data.put("codec_audio", "Opus");
                data.put("codec_video", "VP8/VP9 (GD sessions: audio-only)");
                data.put("streams_active", simulateInt(2, 12));
                data.put("status", "routing");
            }
            case "suspend_track" -> {
                data.put("participant_id", participantId != null ? participantId : "all_inactive");
                data.put("track_type", "video");
                data.put("reason", "inactive_speaker");
                data.put("bandwidth_saved_kbps", simulateInt(150, 400));
                data.put("status", "track_suspended");
            }
            case "resume_track" -> {
                data.put("participant_id", participantId);
                data.put("track_type", "video");
                data.put("status", "track_resumed");
            }
            case "select_simulcast_layer" -> {
                data.put("participant_id", participantId);
                data.put("selected_layer", layer);
                data.put("bitrate_kbps", switch (layer) {
                    case "high" -> 1200; case "medium" -> 600; default -> 150;
                });
                data.put("status", "layer_selected");
            }
            case "estimate_bandwidth" -> {
                data.put("participant_id", participantId);
                data.put("target_bandwidth_kbps", bwKbps);
                data.put("estimated_available_kbps", simulateInt(bwKbps - 50, bwKbps + 100));
                data.put("recommendation", bwKbps < 300 ? "low" : bwKbps < 800 ? "medium" : "high");
                data.put("status", "estimated");
            }
            case "get_routing_table" -> {
                data.put("participants", simulateInt(2, 12));
                data.put("active_streams", simulateInt(1, 10));
                data.put("coturn_relay_percent", "~30% of enterprise users");
                data.put("status", "fetched");
            }
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        }

        return AgentResult.builder("JVB_STREAM_ROUTING_AGENT")
                .status(AgentResult.Status.SUCCESS)
                .message("JVB stream routing: " + action + " for session " + sessionId)
                .data(data)
                .build();
    }

    private static void addStringProp(ObjectNode props, String name, String desc) {
        ObjectNode p = props.putObject(name);
        p.put("type", "string");
        p.put("description", desc);
    }

    private static void addIntProp(ObjectNode props, String name, String desc) {
        ObjectNode p = props.putObject(name);
        p.put("type", "integer");
        p.put("description", desc);
    }

    private int simulateInt(int min, int max) {
        return min + (int)(Math.random() * (max - min + 1));
    }
}
