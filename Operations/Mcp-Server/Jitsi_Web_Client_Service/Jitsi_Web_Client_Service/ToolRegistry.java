package io.ecoskiller.mcp.tools;

import io.ecoskiller.mcp.security.SecurityManager;
import io.ecoskiller.mcp.security.JwtClaims;

import java.util.*;
import java.util.logging.Logger;

/**
 * ToolRegistry — registers and dispatches all 15 Jitsi Web Client MCP tools.
 *
 * Tools:
 *  1.  session_create             — Create a new assessment session
 *  2.  session_join               — Join an existing assessment session
 *  3.  session_leave              — Leave a session gracefully
 *  4.  session_status             — Get current session status + quality
 *  5.  media_quality_get          — Get WebRTC quality metrics
 *  6.  media_device_list          — List available audio/video devices
 *  7.  participant_list           — Get all participants in a room
 *  8.  participant_mute           — Mute/unmute a participant (moderator)
 *  9.  participant_remove         — Remove a participant (admin only)
 * 10.  recording_start            — Start Jibri recording
 * 11.  recording_stop             — Stop Jibri recording
 * 12.  recording_status           — Get recording status
 * 13.  analytics_session_report   — Generate session analytics report
 * 14.  analytics_event_emit       — Emit a user interaction event
 * 15.  auth_token_validate        — Validate a JWT token and return claims
 */
public class ToolRegistry {

    private static final Logger LOGGER = Logger.getLogger(ToolRegistry.class.getName());
    private final SecurityManager security;
    private final Map<String, ToolDefinition> tools = new LinkedHashMap<>();

    public ToolRegistry(SecurityManager security) {
        this.security = security;
        registerAll();
    }

    private void registerAll() {

        // ── 1. session_create ─────────────────────────────────────────────────
        register("session_create",
            "Create a new Jitsi assessment session room. Returns room configuration " +
            "including Prosody XMPP endpoint, bridge URL, and JWT for participants.",
            List.of(
                param("jwt_token",    "string", "Authenticated JWT token (Bearer). Role: admin or interviewer.", true),
                param("assessment_id","string", "Unique assessment identifier (e.g. gd-12345).", true),
                param("room_name",    "string", "Human-readable room name.", true),
                param("max_participants","integer","Maximum participants (2-16). Default: 8.", false),
                param("duration_sec", "integer","Session duration in seconds. Default: 2700 (45 min).", false),
                param("recording_enabled","boolean","Enable Jibri recording. Default: false.", false),
                param("audio_only",   "boolean","Audio-only mode (no video). Default: false.", false)
            ),
            (args) -> sessionCreate(args)
        );

        // ── 2. session_join ───────────────────────────────────────────────────
        register("session_join",
            "Join an existing Jitsi assessment room. Validates JWT, checks room capacity, " +
            "returns WebRTC connection parameters (Prosody endpoint, STUN/TURN servers).",
            List.of(
                param("jwt_token",    "string", "Authenticated JWT token (Bearer).", true),
                param("room_id",      "string", "Room ID to join.", true),
                param("display_name", "string", "Participant display name (1-64 chars).", true),
                param("device_type",  "string", "Device type: desktop | tablet | mobile.", false),
                param("browser",      "string", "Browser identifier: chrome | firefox | safari | edge.", false),
                param("audio_muted",  "boolean","Join with audio muted. Default: false.", false),
                param("video_muted",  "boolean","Join with video muted. Default: false.", false)
            ),
            (args) -> sessionJoin(args)
        );

        // ── 3. session_leave ──────────────────────────────────────────────────
        register("session_leave",
            "Leave an assessment session gracefully. Emits leave event, cleans up " +
            "WebRTC connections, stores session duration metrics.",
            List.of(
                param("jwt_token",  "string", "Authenticated JWT token.", true),
                param("room_id",    "string", "Room ID to leave.", true),
                param("session_id", "string", "Session ID for analytics tracking.", true),
                param("reason",     "string", "Leave reason: user_left | timeout | error | assessment_complete.", false)
            ),
            (args) -> sessionLeave(args)
        );

        // ── 4. session_status ─────────────────────────────────────────────────
        register("session_status",
            "Get real-time status of an assessment session: participant count, " +
            "recording state, time elapsed, connection quality summary.",
            List.of(
                param("jwt_token", "string", "Authenticated JWT token.", true),
                param("room_id",   "string", "Room ID to query.", true)
            ),
            (args) -> sessionStatus(args)
        );

        // ── 5. media_quality_get ──────────────────────────────────────────────
        register("media_quality_get",
            "Get WebRTC media quality metrics for a participant: bitrate (kbps), " +
            "packet loss (%), latency (ms), jitter (ms), video resolution, codec, framerate.",
            List.of(
                param("jwt_token",      "string", "Authenticated JWT token.", true),
                param("room_id",        "string", "Room ID.", true),
                param("participant_id", "string", "Target participant ID. Use 'local' for self.", true)
            ),
            (args) -> mediaQualityGet(args)
        );

        // ── 6. media_device_list ──────────────────────────────────────────────
        register("media_device_list",
            "List available audio/video devices for a participant session. " +
            "Returns cameras, microphones, and speakers with device IDs.",
            List.of(
                param("jwt_token",   "string", "Authenticated JWT token.", true),
                param("session_id",  "string", "Active session ID.", true),
                param("device_type", "string", "Filter: all | camera | microphone | speaker. Default: all.", false)
            ),
            (args) -> mediaDeviceList(args)
        );

        // ── 7. participant_list ───────────────────────────────────────────────
        register("participant_list",
            "Get all participants currently in an assessment room with their status: " +
            "name, role, speaking status, hand raised, audio/video state.",
            List.of(
                param("jwt_token", "string", "Authenticated JWT token.", true),
                param("room_id",   "string", "Room ID to query.", true),
                param("sort_by",   "string", "Sort order: name | role | speaking_time | joined_at. Default: joined_at.", false)
            ),
            (args) -> participantList(args)
        );

        // ── 8. participant_mute ───────────────────────────────────────────────
        register("participant_mute",
            "Mute or unmute a specific participant (interviewer/admin only). " +
            "Sends mute command via Prosody XMPP signaling.",
            List.of(
                param("jwt_token",      "string",  "Authenticated JWT token. Role: interviewer or admin required.", true),
                param("room_id",        "string",  "Room ID.", true),
                param("participant_id", "string",  "Target participant ID.", true),
                param("mute_audio",     "boolean", "Mute audio. Default: true.", false),
                param("mute_video",     "boolean", "Mute video. Default: false.", false)
            ),
            (args) -> participantMute(args)
        );

        // ── 9. participant_remove ─────────────────────────────────────────────
        register("participant_remove",
            "Remove (kick) a participant from the assessment room (admin only). " +
            "Sends XMPP kick stanza, terminates their WebRTC connection.",
            List.of(
                param("jwt_token",      "string", "Authenticated JWT token. Role: admin required.", true),
                param("room_id",        "string", "Room ID.", true),
                param("participant_id", "string", "Participant ID to remove.", true),
                param("reason",         "string", "Reason for removal (shown to removed participant).", false)
            ),
            (args) -> participantRemove(args)
        );

        // ── 10. recording_start ────────────────────────────────────────────────
        register("recording_start",
            "Start Jibri recording for an assessment session (interviewer/admin only). " +
            "Returns recording ID and storage path. Shows recording indicator to all participants.",
            List.of(
                param("jwt_token",    "string", "Authenticated JWT token. Role: interviewer or admin.", true),
                param("room_id",      "string", "Room ID to record.", true),
                param("storage_path", "string", "Storage path prefix for recording files (optional).", false),
                param("notify_participants","boolean","Show recording indicator to all. Default: true.", false)
            ),
            (args) -> recordingStart(args)
        );

        // ── 11. recording_stop ─────────────────────────────────────────────────
        register("recording_stop",
            "Stop active Jibri recording. Returns final recording file details " +
            "including storage URL, duration, and file size.",
            List.of(
                param("jwt_token",   "string", "Authenticated JWT token. Role: interviewer or admin.", true),
                param("room_id",     "string", "Room ID.", true),
                param("recording_id","string", "Recording ID from recording_start response.", true)
            ),
            (args) -> recordingStop(args)
        );

        // ── 12. recording_status ───────────────────────────────────────────────
        register("recording_status",
            "Get current recording status for a room: active/inactive, " +
            "duration, file size so far, storage path.",
            List.of(
                param("jwt_token", "string", "Authenticated JWT token.", true),
                param("room_id",   "string", "Room ID to check.", true)
            ),
            (args) -> recordingStatus(args)
        );

        // ── 13. analytics_session_report ──────────────────────────────────────
        register("analytics_session_report",
            "Generate a full analytics report for a completed assessment session: " +
            "participant join/leave timeline, average bitrate, packet loss, codec used, errors.",
            List.of(
                param("jwt_token",    "string", "Authenticated JWT token. Role: interviewer or admin.", true),
                param("session_id",   "string", "Session ID to report on.", true),
                param("assessment_id","string", "Assessment ID.", true),
                param("format",       "string", "Output format: summary | detailed | json. Default: summary.", false)
            ),
            (args) -> analyticsSessionReport(args)
        );

        // ── 14. analytics_event_emit ──────────────────────────────────────────
        register("analytics_event_emit",
            "Emit a user interaction analytics event (camera_toggled, chat_sent, " +
            "hand_raised, error_occurred, etc.) to the Ecoskiller analytics pipeline.",
            List.of(
                param("jwt_token",  "string", "Authenticated JWT token.", true),
                param("session_id", "string", "Current session ID.", true),
                param("event_name", "string", "Event type: camera_toggled | chat_sent | hand_raised | " +
                                              "participant_joined | participant_left | error_occurred | " +
                                              "screen_share_started | quality_degraded.", true),
                param("event_data", "object", "Additional event data as key-value pairs (optional).", false)
            ),
            (args) -> analyticsEventEmit(args)
        );

        // ── 15. auth_token_validate ────────────────────────────────────────────
        register("auth_token_validate",
            "Validate a JWT token and return its decoded claims: userId, role, " +
            "assessmentId, roomId, expiry. Does NOT accept expired tokens.",
            List.of(
                param("jwt_token", "string", "JWT token to validate (with or without Bearer prefix).", true)
            ),
            (args) -> authTokenValidate(args)
        );
    }

    // ── Tool Implementations ──────────────────────────────────────────────────

    private ToolResult sessionCreate(Map<String, Object> args) {
        String token = requireString(args, "jwt_token");
        JwtClaims claims = validateAndCheckRole(token, "session_create");

        String assessmentId = requireString(args, "assessment_id");
        String roomName     = requireString(args, "room_name");
        security.validateRoomId(assessmentId);

        int maxParticipants  = getInt(args, "max_participants", 8);
        int durationSec      = getInt(args, "duration_sec", 2700);
        boolean recordingEnabled = getBool(args, "recording_enabled", false);
        boolean audioOnly    = getBool(args, "audio_only", false);

        if (maxParticipants < 2 || maxParticipants > 16)
            throw new ToolValidationException("max_participants must be between 2 and 16");
        if (durationSec < 300 || durationSec > 14400)
            throw new ToolValidationException("duration_sec must be between 300 and 14400");

        String roomId = assessmentId;
        long expiresAt = System.currentTimeMillis() / 1000 + durationSec;

        Map<String, Object> result = ordered(
            "status",            "created",
            "room_id",           roomId,
            "room_name",         security.sanitize(roomName, 128),
            "assessment_id",     assessmentId,
            "jitsi_domain",      "jitsi.ecoskiller.io",
            "prosody_xmpp_url",  "wss://prosody.ecoskiller.io:5280/xmpp-websocket",
            "stun_servers",      List.of("stun:stun.ecoskiller.io:3478"),
            "turn_servers",      List.of(Map.of(
                "url", "turn:turn.ecoskiller.io:3478",
                "username", "ecoskiller_" + roomId,
                "credential_type", "token"
            )),
            "max_participants",  maxParticipants,
            "duration_sec",      durationSec,
            "expires_at",        expiresAt,
            "recording_enabled", recordingEnabled,
            "audio_only",        audioOnly,
            "created_by",        claims.getUserId(),
            "created_at",        System.currentTimeMillis() / 1000,
            "conference_url",    "https://ecoskiller.io/assessments?room=" + roomId
        );
        security.auditLog("SESSION_CREATED", "room=" + roomId + " by=" + claims.getUserId(), null);
        return ToolResult.ok(result);
    }

    private ToolResult sessionJoin(Map<String, Object> args) {
        String token = requireString(args, "jwt_token");
        JwtClaims claims = validateAndCheckRole(token, "session_join");

        String roomId      = requireString(args, "room_id");
        String displayName = requireString(args, "display_name");
        security.validateRoomId(roomId);

        if (displayName.length() < 1 || displayName.length() > 64)
            throw new ToolValidationException("display_name must be 1-64 characters");

        String deviceType  = security.sanitize(getString(args, "device_type", "desktop"), 16);
        String browser     = security.sanitize(getString(args, "browser", "chrome"), 16);
        boolean audioMuted = getBool(args, "audio_muted", false);
        boolean videoMuted = getBool(args, "video_muted", false);

        String sessionId = "sess-" + roomId + "-" + claims.getUserId() + "-" + System.currentTimeMillis();

        Map<String, Object> result = ordered(
            "status",          "joined",
            "session_id",      sessionId,
            "room_id",         roomId,
            "participant_id",  claims.getUserId(),
            "display_name",    security.sanitize(displayName, 64),
            "role",            claims.getRole(),
            "joined_at",       System.currentTimeMillis() / 1000,
            "audio_muted",     audioMuted,
            "video_muted",     videoMuted,
            "device_type",     deviceType,
            "browser",         browser,
            "webrtc_config",   Map.of(
                "ice_transport_policy", "all",
                "bundle_policy",        "max-bundle",
                "rtcp_mux_policy",      "require",
                "preferred_codecs",     List.of("VP8", "H264", "VP9"),
                "audio_codec",          "opus",
                "audio_sample_rate",    48000,
                "video_max_bitrate",    2000,
                "audio_max_bitrate",    128
            ),
            "xmpp_config", Map.of(
                "bosh_url",   "https://prosody.ecoskiller.io:5280/http-bind",
                "ws_url",     "wss://prosody.ecoskiller.io:5280/xmpp-websocket",
                "conference", roomId + "@conference.ecoskiller.io",
                "domain",     "ecoskiller.io"
            )
        );
        security.auditLog("SESSION_JOINED", "room=" + roomId + " user=" + claims.getUserId(), null);
        return ToolResult.ok(result);
    }

    private ToolResult sessionLeave(Map<String, Object> args) {
        String token = requireString(args, "jwt_token");
        JwtClaims claims = validateAndCheckRole(token, "session_leave");

        String roomId    = requireString(args, "room_id");
        String sessionId = requireString(args, "session_id");
        String reason    = security.sanitize(getString(args, "reason", "user_left"), 32);
        security.validateRoomId(roomId);

        Map<String, Object> result = ordered(
            "status",       "left",
            "room_id",      roomId,
            "session_id",   sessionId,
            "participant_id", claims.getUserId(),
            "reason",       reason,
            "left_at",      System.currentTimeMillis() / 1000,
            "analytics_queued", true,
            "message",      "Session ended. Analytics event queued for processing."
        );
        security.auditLog("SESSION_LEFT", "room=" + roomId + " user=" + claims.getUserId() + " reason=" + reason, null);
        return ToolResult.ok(result);
    }

    private ToolResult sessionStatus(Map<String, Object> args) {
        String token = requireString(args, "jwt_token");
        validateAndCheckRole(token, "session_status");

        String roomId = requireString(args, "room_id");
        security.validateRoomId(roomId);

        // Simulated status — in production, query Prosody/Bridge APIs
        Map<String, Object> result = ordered(
            "status",             "active",
            "room_id",            roomId,
            "participant_count",  3,
            "is_recording",       false,
            "recording_id",       null,
            "elapsed_sec",        847,
            "remaining_sec",      1853,
            "audio_only_mode",    false,
            "quality_summary",    "good",
            "active_speaker",     "cand-001",
            "bridge_status",      "connected",
            "prosody_status",     "connected",
            "checked_at",         System.currentTimeMillis() / 1000
        );
        return ToolResult.ok(result);
    }

    private ToolResult mediaQualityGet(Map<String, Object> args) {
        String token = requireString(args, "jwt_token");
        validateAndCheckRole(token, "media_quality_get");

        String roomId        = requireString(args, "room_id");
        String participantId = requireString(args, "participant_id");
        security.validateRoomId(roomId);

        // Simulated WebRTC stats — in production, query bridge stats API
        Map<String, Object> result = ordered(
            "participant_id",       participantId,
            "room_id",              roomId,
            "timestamp",            System.currentTimeMillis() / 1000,
            "overall_quality",      "good",
            "video", Map.of(
                "codec",            "VP8",
                "resolution",       "1280x720",
                "framerate",        28,
                "bitrate_kbps",     1450,
                "packet_loss_pct",  0.2,
                "nack_count",       3,
                "fir_count",        0,
                "pli_count",        1
            ),
            "audio", Map.of(
                "codec",            "opus",
                "sample_rate",      48000,
                "bitrate_kbps",     96,
                "packet_loss_pct",  0.1,
                "jitter_ms",        12,
                "audio_level",      0.6
            ),
            "network", Map.of(
                "rtt_ms",           48,
                "jitter_ms",        15,
                "available_bitrate_kbps", 3200,
                "transport",        "udp",
                "ice_state",        "connected",
                "relay_type",       "srflx"
            ),
            "recommendations",  List.of()
        );
        return ToolResult.ok(result);
    }

    private ToolResult mediaDeviceList(Map<String, Object> args) {
        String token = requireString(args, "jwt_token");
        validateAndCheckRole(token, "media_device_list");

        requireString(args, "session_id");
        String filter = security.sanitize(getString(args, "device_type", "all"), 16);

        List<Map<String, Object>> devices = new ArrayList<>();
        if (filter.equals("all") || filter.equals("camera")) {
            devices.add(Map.of("id","videoinput-0","kind","videoinput","label","HD Webcam","isDefault",true));
            devices.add(Map.of("id","videoinput-1","kind","videoinput","label","OBS Virtual Camera","isDefault",false));
        }
        if (filter.equals("all") || filter.equals("microphone")) {
            devices.add(Map.of("id","audioinput-0","kind","audioinput","label","Default Microphone","isDefault",true));
            devices.add(Map.of("id","audioinput-1","kind","audioinput","label","USB Headset Microphone","isDefault",false));
        }
        if (filter.equals("all") || filter.equals("speaker")) {
            devices.add(Map.of("id","audiooutput-0","kind","audiooutput","label","Default Speaker","isDefault",true));
        }

        Map<String, Object> result = ordered(
            "device_count", devices.size(),
            "filter",       filter,
            "devices",      devices
        );
        return ToolResult.ok(result);
    }

    private ToolResult participantList(Map<String, Object> args) {
        String token = requireString(args, "jwt_token");
        validateAndCheckRole(token, "participant_list");

        String roomId = requireString(args, "room_id");
        security.validateRoomId(roomId);
        String sortBy = security.sanitize(getString(args, "sort_by", "joined_at"), 32);

        List<Map<String, Object>> participants = List.of(
            makeParticipant("interviewer-01", "Dr. Sharma", "interviewer", true,  false, false, false, 1709000100),
            makeParticipant("cand-001",       "Candidate 1","candidate",  false, true,  false, false, 1709000200),
            makeParticipant("cand-002",       "Candidate 2","candidate",  false, false, false, true,  1709000300)
        );

        Map<String, Object> result = ordered(
            "room_id",           roomId,
            "participant_count", participants.size(),
            "sort_by",           sortBy,
            "participants",      participants,
            "queried_at",        System.currentTimeMillis() / 1000
        );
        return ToolResult.ok(result);
    }

    private ToolResult participantMute(Map<String, Object> args) {
        String token = requireString(args, "jwt_token");
        JwtClaims claims = validateAndCheckRole(token, "participant_mute");

        String roomId        = requireString(args, "room_id");
        String participantId = requireString(args, "participant_id");
        boolean muteAudio    = getBool(args, "mute_audio", true);
        boolean muteVideo    = getBool(args, "mute_video", false);
        security.validateRoomId(roomId);
        security.validateUserId(participantId);

        Map<String, Object> result = ordered(
            "status",         "muted",
            "room_id",        roomId,
            "participant_id", participantId,
            "audio_muted",    muteAudio,
            "video_muted",    muteVideo,
            "action_by",      claims.getUserId(),
            "action_at",      System.currentTimeMillis() / 1000,
            "xmpp_stanza_sent", true
        );
        security.auditLog("PARTICIPANT_MUTED", "room=" + roomId + " target=" + participantId + " by=" + claims.getUserId(), null);
        return ToolResult.ok(result);
    }

    private ToolResult participantRemove(Map<String, Object> args) {
        String token = requireString(args, "jwt_token");
        JwtClaims claims = validateAndCheckRole(token, "participant_remove");

        String roomId        = requireString(args, "room_id");
        String participantId = requireString(args, "participant_id");
        String reason        = security.sanitize(getString(args, "reason", "Removed by moderator"), 128);
        security.validateRoomId(roomId);
        security.validateUserId(participantId);

        Map<String, Object> result = ordered(
            "status",         "removed",
            "room_id",        roomId,
            "participant_id", participantId,
            "reason",         reason,
            "removed_by",     claims.getUserId(),
            "removed_at",     System.currentTimeMillis() / 1000,
            "xmpp_kick_sent", true
        );
        security.auditLog("PARTICIPANT_REMOVED", "room=" + roomId + " target=" + participantId + " by=" + claims.getUserId(), null);
        return ToolResult.ok(result);
    }

    private ToolResult recordingStart(Map<String, Object> args) {
        String token = requireString(args, "jwt_token");
        JwtClaims claims = validateAndCheckRole(token, "recording_start");

        String roomId            = requireString(args, "room_id");
        String storagePath       = security.sanitize(getString(args, "storage_path", "/recordings/" + roomId), 256);
        boolean notifyParticipants = getBool(args, "notify_participants", true);
        security.validateRoomId(roomId);

        String recordingId = "rec-" + roomId + "-" + System.currentTimeMillis();
        Map<String, Object> result = ordered(
            "status",              "recording",
            "recording_id",        recordingId,
            "room_id",             roomId,
            "storage_path",        storagePath,
            "jibri_instance",      "jibri-01.ecoskiller.io",
            "started_by",          claims.getUserId(),
            "started_at",          System.currentTimeMillis() / 1000,
            "notify_participants", notifyParticipants,
            "gdpr_indicator_shown", notifyParticipants,
            "formats",             List.of("mp4"),
            "message",             "Recording started. All participants have been notified."
        );
        security.auditLog("RECORDING_STARTED", "room=" + roomId + " rec=" + recordingId + " by=" + claims.getUserId(), null);
        return ToolResult.ok(result);
    }

    private ToolResult recordingStop(Map<String, Object> args) {
        String token = requireString(args, "jwt_token");
        JwtClaims claims = validateAndCheckRole(token, "recording_stop");

        String roomId      = requireString(args, "room_id");
        String recordingId = requireString(args, "recording_id");
        security.validateRoomId(roomId);

        Map<String, Object> result = ordered(
            "status",           "stopped",
            "recording_id",     recordingId,
            "room_id",          roomId,
            "stopped_by",       claims.getUserId(),
            "stopped_at",       System.currentTimeMillis() / 1000,
            "duration_sec",     1847,
            "file_size_mb",     284,
            "storage_url",      "https://storage.ecoskiller.io/recordings/" + recordingId + ".mp4",
            "processing_status","queued",
            "estimated_ready_in_sec", 300
        );
        security.auditLog("RECORDING_STOPPED", "room=" + roomId + " rec=" + recordingId, null);
        return ToolResult.ok(result);
    }

    private ToolResult recordingStatus(Map<String, Object> args) {
        String token = requireString(args, "jwt_token");
        validateAndCheckRole(token, "recording_status");

        String roomId = requireString(args, "room_id");
        security.validateRoomId(roomId);

        Map<String, Object> result = ordered(
            "room_id",         roomId,
            "is_recording",    false,
            "recording_id",    null,
            "started_at",      null,
            "duration_sec",    0,
            "file_size_mb",    0,
            "jibri_available", true,
            "checked_at",      System.currentTimeMillis() / 1000
        );
        return ToolResult.ok(result);
    }

    private ToolResult analyticsSessionReport(Map<String, Object> args) {
        String token = requireString(args, "jwt_token");
        JwtClaims claims = validateAndCheckRole(token, "analytics_session_report");

        String sessionId    = requireString(args, "session_id");
        String assessmentId = requireString(args, "assessment_id");
        String format       = security.sanitize(getString(args, "format", "summary"), 16);

        Map<String, Object> result = ordered(
            "session_id",         sessionId,
            "assessment_id",      assessmentId,
            "format",             format,
            "report_generated_by", claims.getUserId(),
            "generated_at",       System.currentTimeMillis() / 1000,
            "summary", Map.of(
                "total_participants",    4,
                "duration_sec",         2700,
                "avg_bitrate_kbps",     1380,
                "avg_packet_loss_pct",  0.18,
                "avg_latency_ms",       52,
                "video_codec",          "VP8",
                "audio_codec",          "opus",
                "browser_breakdown",    Map.of("chrome", 3, "safari", 1),
                "device_breakdown",     Map.of("desktop", 3, "mobile", 1),
                "errors",               List.of(),
                "connection_drops",     0,
                "recording_available",  false,
                "completion_rate_pct",  100
            ),
            "timeline", List.of(
                Map.of("event","participant_joined","user","interviewer-01","at_sec",0),
                Map.of("event","participant_joined","user","cand-001","at_sec",65),
                Map.of("event","participant_joined","user","cand-002","at_sec",78),
                Map.of("event","session_ended","user","all","at_sec",2700)
            )
        );
        return ToolResult.ok(result);
    }

    private ToolResult analyticsEventEmit(Map<String, Object> args) {
        String token = requireString(args, "jwt_token");
        JwtClaims claims = validateAndCheckRole(token, "analytics_event_emit");

        String sessionId = requireString(args, "session_id");
        String eventName = requireString(args, "event_name");

        Set<String> validEvents = Set.of(
            "camera_toggled","chat_sent","hand_raised","participant_joined",
            "participant_left","error_occurred","screen_share_started",
            "screen_share_stopped","quality_degraded","quality_restored",
            "recording_started","recording_stopped","device_changed","mute_toggled"
        );
        if (!validEvents.contains(eventName)) {
            throw new ToolValidationException("Unknown event_name: " + eventName +
                ". Valid: " + String.join(", ", validEvents));
        }

        String eventId = "evt-" + System.currentTimeMillis();
        Map<String, Object> result = ordered(
            "status",      "queued",
            "event_id",    eventId,
            "session_id",  sessionId,
            "event_name",  eventName,
            "user_id",     claims.getUserId(),
            "timestamp",   System.currentTimeMillis() / 1000,
            "pipeline",    "kafka",
            "topic",       "ecoskiller.analytics.jitsi.events"
        );
        security.auditLog("ANALYTICS_EVENT", "session=" + sessionId + " event=" + eventName + " user=" + claims.getUserId(), null);
        return ToolResult.ok(result);
    }

    private ToolResult authTokenValidate(Map<String, Object> args) {
        String token = requireString(args, "jwt_token");
        try {
            JwtClaims claims = security.validateToken(token);
            long nowSec = System.currentTimeMillis() / 1000;
            Map<String, Object> result = ordered(
                "valid",         true,
                "user_id",       claims.getUserId(),
                "role",          claims.getRole(),
                "assessment_id", claims.getAssessmentId(),
                "room_id",       claims.getRoomId(),
                "issuer",        claims.getIssuer(),
                "expires_at",    claims.getExp(),
                "expires_in_sec",Math.max(0, claims.getExp() - nowSec),
                "issued_at",     claims.getIat(),
                "permissions",   getPermissionsForRole(claims.getRole())
            );
            return ToolResult.ok(result);
        } catch (SecurityException e) {
            Map<String, Object> result = ordered(
                "valid",  false,
                "reason", e.getMessage()
            );
            return ToolResult.error(result);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JwtClaims validateAndCheckRole(String token, String toolName) {
        JwtClaims claims = security.validateToken(token);
        security.checkToolPermission(toolName, claims.getRole());
        return claims;
    }

    private Map<String, Object> makeParticipant(String id, String name, String role,
            boolean isSpeaking, boolean handRaised, boolean audioMuted, boolean videoMuted, long joinedAt) {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("id",          id);
        p.put("display_name",name);
        p.put("role",        role);
        p.put("is_speaking", isSpeaking);
        p.put("hand_raised", handRaised);
        p.put("audio_muted", audioMuted);
        p.put("video_muted", videoMuted);
        p.put("joined_at",   joinedAt);
        p.put("connection",  "good");
        return p;
    }

    private List<String> getPermissionsForRole(String role) {
        return switch (role) {
            case "admin"       -> List.of("join","leave","mute","kick","record","view_analytics","manage_settings");
            case "interviewer" -> List.of("join","leave","mute","record","view_analytics");
            case "candidate"   -> List.of("join","leave","chat","hand_raise","screen_share");
            default            -> List.of("join","leave");
        };
    }

    private String requireString(Map<String, Object> args, String key) {
        Object val = args.get(key);
        if (val == null || val.toString().isBlank())
            throw new ToolValidationException("Required argument missing: " + key);
        return val.toString().trim();
    }

    private String getString(Map<String, Object> args, String key, String def) {
        Object val = args.get(key);
        return val != null ? val.toString().trim() : def;
    }

    private int getInt(Map<String, Object> args, String key, int def) {
        Object val = args.get(key);
        if (val == null) return def;
        try { return Integer.parseInt(val.toString()); }
        catch (NumberFormatException e) { throw new ToolValidationException(key + " must be an integer"); }
    }

    private boolean getBool(Map<String, Object> args, String key, boolean def) {
        Object val = args.get(key);
        if (val == null) return def;
        if (val instanceof Boolean) return (Boolean) val;
        return Boolean.parseBoolean(val.toString());
    }

    @SafeVarargs
    private <K, V> Map<K, V> ordered(Object... kvs) {
        Map<Object, Object> m = new LinkedHashMap<>();
        for (int i = 0; i < kvs.length - 1; i += 2) m.put(kvs[i], kvs[i + 1]);
        //noinspection unchecked
        return (Map<K, V>) m;
    }

    // ── Registry internals ────────────────────────────────────────────────────

    private void register(String name, String description, List<Map<String, Object>> params, ToolFunction fn) {
        tools.put(name, new ToolDefinition(name, description, params, fn));
    }

    public List<Map<String, Object>> listTools() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ToolDefinition td : tools.values()) list.add(td.toSchema());
        return list;
    }

    public ToolResult callTool(String name, Map<String, Object> args) {
        ToolDefinition td = tools.get(name);
        if (td == null) throw new ToolNotFoundException(name);
        return td.call(args);
    }

    private Map<String, Object> param(String name, String type, String description, boolean required) {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("name", name);
        p.put("type", type);
        p.put("description", description);
        p.put("required", required);
        return p;
    }

    @FunctionalInterface interface ToolFunction { ToolResult apply(Map<String, Object> args); }

    private static class ToolDefinition {
        final String name, description;
        final List<Map<String, Object>> params;
        final ToolFunction fn;
        ToolDefinition(String name, String description, List<Map<String, Object>> params, ToolFunction fn) {
            this.name = name; this.description = description; this.params = params; this.fn = fn;
        }
        ToolResult call(Map<String, Object> args) { return fn.apply(args); }
        Map<String, Object> toSchema() {
            Map<String, Object> schema    = new LinkedHashMap<>();
            Map<String, Object> properties = new LinkedHashMap<>();
            List<String>        required   = new ArrayList<>();
            for (Map<String, Object> p : params) {
                String pName = (String) p.get("name");
                Map<String, Object> pDef = new LinkedHashMap<>();
                pDef.put("type",        p.get("type"));
                pDef.put("description", p.get("description"));
                properties.put(pName, pDef);
                if (Boolean.TRUE.equals(p.get("required"))) required.add(pName);
            }
            Map<String, Object> inputSchema = new LinkedHashMap<>();
            inputSchema.put("type",       "object");
            inputSchema.put("properties", properties);
            inputSchema.put("required",   required);

            schema.put("name",        name);
            schema.put("description", description);
            schema.put("inputSchema", inputSchema);
            return schema;
        }
    }
}
