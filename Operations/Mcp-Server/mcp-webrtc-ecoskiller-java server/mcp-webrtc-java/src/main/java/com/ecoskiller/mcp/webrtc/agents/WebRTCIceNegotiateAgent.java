package com.ecoskiller.mcp.webrtc.agents;

import com.ecoskiller.mcp.webrtc.model.AgentResult;
import com.ecoskiller.mcp.webrtc.security.SecurityValidator;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * AGENT 4 — WEBRTC_ICE_NEGOTIATE
 *
 * Handles ICE (Interactive Connectivity Establishment, RFC 8445) negotiation.
 * Processes ICE candidates and returns the optimal connectivity path:
 *   1. host (direct LAN)     — fastest, no relay
 *   2. srflx (STUN/coturn)  — discovers public NAT IP
 *   3. relay (TURN/coturn)  — for symmetric NAT / corporate firewall (~30% of users)
 *
 * Security: candidate strings validated, IP/port bounds checked.
 */
public class WebRTCIceNegotiateAgent extends BaseAgent {

    public WebRTCIceNegotiateAgent(SecurityValidator security) { super(security); }

    @Override
    public JSONObject getToolDefinition() {
        JSONObject props = new JSONObject()
            .put("session_id",      stringProp("Session ID"))
            .put("user_id",         stringProp("Participant user ID"))
            .put("ice_candidate",   stringProp("ICE candidate string (starts with 'candidate:')"))
            .put("sdp_mid",         stringProp("SDP media ID (e.g. 'audio', '0')"))
            .put("candidate_type",  stringProp("Candidate type: host | srflx | relay"))
            .put("ip_address",      stringProp("Candidate IP address"))
            .put("port",            intProp("Candidate port", 1, 65535));

        return tool("webrtc_ice_negotiate",
            "Process an ICE candidate for a WebRTC session. Returns connectivity decision and coturn relay config " +
            "for NAT traversal. Approx 70% direct/STUN, 30% TURN relay per Ecoskiller infrastructure data.",
            schema(props, "session_id", "user_id", "ice_candidate", "sdp_mid", "candidate_type"));
    }

    @Override
    public AgentResult execute(JSONObject args) {
        String sessionId     = security.requireSessionId(args, "session_id");
        String userId        = security.requireUserId(args, "user_id");
        String iceCandidate  = security.requireIceCandidate(args, "ice_candidate");
        String sdpMid        = security.requireString(args, "sdp_mid", 1, 16);
        String candidateType = security.requireString(args, "candidate_type", 4, 8);

        if (!candidateType.matches("host|srflx|relay")) {
            throw new SecurityException("Invalid candidate_type: " + candidateType);
        }

        security.enforceRateLimit(userId);

        String ip   = security.optionalSafeString(args, "ip_address", 45).orElse("0.0.0.0");
        int    port = args.has("port") ? security.requirePort(args, "port") : 0;

        // Simulate ICE decision
        String connectivity;
        String coturnAction;
        JSONObject coturnConfig = new JSONObject();

        switch (candidateType) {
            case "host":
                connectivity = "DIRECT — no relay required (LAN/local path)";
                coturnAction = "none";
                break;
            case "srflx":
                connectivity = "STUN server-reflexive — public NAT IP discovered via coturn:3478";
                coturnAction = "stun_binding";
                coturnConfig.put("stun_host", "media.ecoskiller.com").put("stun_port", 3478);
                break;
            case "relay":
            default:
                connectivity = "TURN relay — full relay via coturn (symmetric NAT / firewall)";
                coturnAction = "turn_allocate";
                coturnConfig
                    .put("turn_host",  "media.ecoskiller.com")
                    .put("turn_port",  3478)
                    .put("turn_tls_port", 5349)
                    .put("relay_range", "UDP 49152–65535")
                    .put("encryption", "SRTP packets relayed encrypted — coturn does NOT decrypt");
                break;
        }

        JSONArray iceServers = new JSONArray()
            .put(new JSONObject()
                .put("urls", "stun:media.ecoskiller.com:3478"))
            .put(new JSONObject()
                .put("urls", "turn:media.ecoskiller.com:3478")
                .put("username", "[dynamically-issued-coturn-credential]")
                .put("credential","[time-limited-hmac-sha1]"));

        JSONObject data = new JSONObject()
            .put("session_id",     sessionId)
            .put("user_id",        userId)
            .put("sdp_mid",        sdpMid)
            .put("candidate_type", candidateType)
            .put("connectivity",   connectivity)
            .put("coturn_action",  coturnAction)
            .put("coturn_config",  coturnConfig)
            .put("ice_servers",    iceServers)
            .put("ice_standard",   "RFC 8445 — Interactive Connectivity Establishment")
            .put("consent_freshness", "STUN binding every ~15s (RFC 7675) maintains NAT binding")
            .put("slo_note",       "WebRTC connection success SLO ≥ 95%; alert < 90% over 15-min window")
            .put("user_distribution", "~70% direct/STUN, ~30% TURN relay (enterprise/mobile restricted networks)")
            .put("audit_event",    security.buildAuditEvent("ICE_NEGOTIATE", sessionId, userId));

        return AgentResult.success("WEBRTC_ICE_NEGOTIATE_AGENT")
            .message("ICE candidate processed: " + candidateType + " → " + connectivity)
            .data(data)
            .build();
    }
}
