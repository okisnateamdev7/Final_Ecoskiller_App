package com.ecoskiller.mcp.webrtc.agents;

import com.ecoskiller.mcp.webrtc.model.AgentResult;
import com.ecoskiller.mcp.webrtc.security.SecurityValidator;
import org.json.JSONObject;

/**
 * AGENT 9 — WEBRTC_TURN_ALLOCATION
 *
 * Manages coturn TURN/STUN server allocations for WebRTC ICE relay.
 *
 * Per EcoSkiller spec:
 *   - coturn on media.ecoskiller.com (GCP e2-small / AWS t3.small)
 *   - UDP 3478 (STUN/TURN) + UDP 5349 (TURN-TLS) + UDP 49152–65535 (relay)
 *   - ~30% of users require TURN relay (enterprise/mobile restricted networks)
 *   - coturn uses time-limited HMAC-SHA1 credentials (not static passwords)
 *   - SRTP relayed encrypted — coturn never decrypts media
 *
 * Security: credentials are time-limited HMAC-SHA1; IPs/ports validated.
 */
public class WebRTCTurnAllocationAgent extends BaseAgent {

    private static final int TURN_CREDENTIAL_TTL_SECONDS = 86400; // 24h standard

    public WebRTCTurnAllocationAgent(SecurityValidator security) { super(security); }

    @Override
    public JSONObject getToolDefinition() {
        JSONObject props = new JSONObject()
            .put("session_id",   stringProp("Session ID requesting TURN allocation"))
            .put("user_id",      stringProp("Participant requiring TURN relay"))
            .put("client_ip",    stringProp("Client public IP address (for relay allocation)"))
            .put("protocol",     stringProp("Transport protocol: udp | tcp | tls"))
            .put("relay_port",   intProp("Requested relay port (0=auto-assign)", 0, 65535));

        return tool("webrtc_turn_allocation",
            "Allocate or query a coturn TURN relay for a WebRTC participant. Issues time-limited HMAC-SHA1 credentials. " +
            "Used by ~30% of Ecoskiller users on enterprise/mobile restricted networks.",
            schema(props, "session_id", "user_id", "client_ip", "protocol"));
    }

    @Override
    public AgentResult execute(JSONObject args) {
        String sessionId = security.requireSessionId(args, "session_id");
        String userId    = security.requireUserId(args, "user_id");
        String clientIp  = security.requireIpAddress(args, "client_ip");
        String protocol  = security.requireString(args, "protocol", 3, 8).toLowerCase();
        int    relayPort = args.has("relay_port") ? security.requirePort(args, "relay_port") : 0;

        if (!protocol.matches("udp|tcp|tls")) {
            throw new SecurityException("Invalid TURN transport protocol: " + protocol);
        }

        security.enforceRateLimit(userId);

        // Simulate HMAC-SHA1 time-limited credential generation
        long expiryTs      = System.currentTimeMillis() / 1000 + TURN_CREDENTIAL_TTL_SECONDS;
        String username    = expiryTs + ":" + userId;
        String credential  = "[HMAC-SHA1(" + username + ", coturn_secret) — not exposed in response]";

        // Assign relay port from coturn pool
        int assignedPort = (relayPort > 49152 && relayPort <= 65535) ? relayPort
                           : 49152 + Math.abs(userId.hashCode() % 16383);

        JSONObject allocation = new JSONObject()
            .put("turn_host",         "media.ecoskiller.com")
            .put("turn_port",         "udp".equals(protocol) ? 3478 : 5349)
            .put("relay_protocol",    protocol)
            .put("relay_port",        assignedPort)
            .put("relay_range",       "UDP 49152–65535")
            .put("username",          username)
            .put("credential_note",   credential)
            .put("ttl_seconds",       TURN_CREDENTIAL_TTL_SECONDS)
            .put("expires_epoch",     expiryTs);

        JSONObject security_info = new JSONObject()
            .put("credential_type",   "HMAC-SHA1 time-limited (not static password)")
            .put("media_encryption",  "SRTP packets relayed encrypted — coturn does NOT decrypt")
            .put("e2e_encryption",    "DTLS-SRTP end-to-end maintained through TURN relay")
            .put("stun_port",         3478)
            .put("stun_rfc",          "RFC 8489")
            .put("turn_rfc",          "RFC 5766");

        JSONObject data = new JSONObject()
            .put("session_id",     sessionId)
            .put("user_id",        userId)
            .put("client_ip",      clientIp)
            .put("allocation",     allocation)
            .put("security",       security_info)
            .put("infrastructure", "GCP e2-small / AWS t3.small at media.ecoskiller.com")
            .put("usage_note",     "~30% of Ecoskiller users require TURN relay; ~70% use direct/STUN path")
            .put("audit_event",    security.buildAuditEvent("TURN_ALLOCATE", sessionId, userId));

        return AgentResult.success("WEBRTC_TURN_ALLOCATION_AGENT")
            .message("TURN allocation: " + protocol.toUpperCase() + " relay at media.ecoskiller.com:" + assignedPort)
            .data(data)
            .build();
    }
}
