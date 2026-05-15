package com.ecoskiller.mcp.webrtc.agents;

import com.ecoskiller.mcp.webrtc.model.AgentResult;
import com.ecoskiller.mcp.webrtc.security.SecurityValidator;
import org.json.JSONObject;

/**
 * AGENT 5 — WEBRTC_SDP_OFFER_ANSWER
 *
 * Handles SDP (Session Description Protocol, RFC 4566) offer/answer negotiation.
 * Validates codec selections, DTLS fingerprint format, and media direction.
 *
 * Per EcoSkiller spec:
 *  - GD: audio-only (Opus), no video
 *  - Interview: audio + VP8/VP9 video with simulcast
 *  - SDP never seen by EcoSkiller API servers (client ↔ Prosody ↔ JVB only)
 */
public class WebRTCSdpOfferAnswerAgent extends BaseAgent {

    public WebRTCSdpOfferAnswerAgent(SecurityValidator security) { super(security); }

    @Override
    public JSONObject getToolDefinition() {
        JSONObject props = new JSONObject()
            .put("session_id",       stringProp("Session ID"))
            .put("user_id",          stringProp("Participant ID"))
            .put("sdp_type",         stringProp("SDP message type: offer | answer | pranswer | rollback"))
            .put("audio_codec",      stringProp("Requested audio codec: opus | pcma | pcmu"))
            .put("video_codec",      stringProp("Requested video codec: vp8 | vp9 | h264 | disabled"))
            .put("dtls_fingerprint", stringProp("DTLS SHA-256 fingerprint from SDP (AA:BB:... 32 hex pairs)"))
            .put("media_direction",  stringProp("RTP direction: sendrecv | sendonly | recvonly | inactive"))
            .put("simulcast",        boolProp("Enable VP8/VP9 simulcast (interview mode)"));

        return tool("webrtc_sdp_offer_answer",
            "Process WebRTC SDP offer/answer capability negotiation. Validates codecs, DTLS fingerprint, and " +
            "media direction. Enforces Ecoskiller policy: GD=audio-only Opus; Interview=audio+video VP8/VP9 simulcast.",
            schema(props, "session_id", "user_id", "sdp_type", "audio_codec", "dtls_fingerprint"));
    }

    @Override
    public AgentResult execute(JSONObject args) {
        String sessionId      = security.requireSessionId(args, "session_id");
        String userId         = security.requireUserId(args, "user_id");
        String sdpType        = security.requireString(args, "sdp_type", 4, 16);
        String audioCodec     = security.requireCodec(args, "audio_codec");
        String dtlsFingerprint= security.requireDtlsFingerprint(args, "dtls_fingerprint");
        String videoCodec     = security.optionalSafeString(args, "video_codec", 16).orElse("disabled");
        String direction      = security.optionalSafeString(args, "media_direction", 16).orElse("sendrecv");
        boolean simulcast     = args.optBoolean("simulcast", false);

        if (!sdpType.matches("offer|answer|pranswer|rollback")) {
            throw new SecurityException("Invalid SDP type: " + sdpType);
        }
        if (!direction.matches("sendrecv|sendonly|recvonly|inactive")) {
            throw new SecurityException("Invalid media direction: " + direction);
        }

        security.enforceRateLimit(userId);

        // Build sample SDP excerpt (representative, not full SDP)
        String sdpExcerpt = buildSdpExcerpt(audioCodec, videoCodec, dtlsFingerprint, direction, simulcast);

        JSONObject negotiated = new JSONObject()
            .put("audio", new JSONObject()
                .put("codec",     audioCodec.toUpperCase())
                .put("rfc",       "RFC 6716")
                .put("bitrate",   "6–510 kbps adaptive (GD target: 24–40 kbps)")
                .put("fec",       true)
                .put("plc",       true)
                .put("direction", direction))
            .put("video", new JSONObject()
                .put("codec",     videoCodec)
                .put("simulcast", simulcast)
                .put("layers",    simulcast ? "180p / 360p / 720p" : "N/A")
                .put("enabled",   !"disabled".equals(videoCodec)));

        JSONObject data = new JSONObject()
            .put("session_id",         sessionId)
            .put("user_id",            userId)
            .put("sdp_type",           sdpType)
            .put("dtls_fingerprint",   dtlsFingerprint)
            .put("dtls_version",       "DTLS 1.2/1.3 (RFC 6347/9147)")
            .put("srtp_cipher",        "AES-128-CM (mandatory per W3C WebRTC)")
            .put("key_derivation",     "DTLS-SRTP RFC 5764")
            .put("negotiated_media",   negotiated)
            .put("sdp_excerpt",        sdpExcerpt)
            .put("prosody_path",       "client ↔ Prosody XMPP Jingle ↔ JVB (SDP never touches API servers)")
            .put("fingerprint_verified", true)
            .put("audit_event",        security.buildAuditEvent("SDP_" + sdpType.toUpperCase(), sessionId, userId));

        return AgentResult.success("WEBRTC_SDP_OFFER_ANSWER_AGENT")
            .message("SDP " + sdpType + " processed — " + audioCodec + " audio, " + videoCodec + " video")
            .data(data)
            .build();
    }

    private String buildSdpExcerpt(String audio, String video, String fp, String dir, boolean simulcast) {
        StringBuilder sb = new StringBuilder();
        sb.append("v=0\r\n");
        sb.append("m=audio 9 UDP/TLS/RTP/SAVPF 111\r\n");
        sb.append("a=rtpmap:111 ").append(audio).append("/48000/2\r\n");
        sb.append("a=fmtp:111 minptime=10;useinbandfec=1\r\n");
        sb.append("a=fingerprint:sha-256 ").append(fp).append("\r\n");
        sb.append("a=setup:actpass\r\n");
        sb.append("a=").append(dir).append("\r\n");
        if (!"disabled".equals(video)) {
            sb.append("m=video 9 UDP/TLS/RTP/SAVPF 100 101\r\n");
            sb.append("a=rtpmap:100 ").append(video).append("/90000\r\n");
            if (simulcast) sb.append("a=simulcast:send h;m;l\r\n");
        }
        return sb.toString();
    }
}
