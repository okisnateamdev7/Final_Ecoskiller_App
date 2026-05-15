package io.ecoskiller.mcp.gd;

import io.ecoskiller.mcp.gd.json.Json;
import io.ecoskiller.mcp.gd.json.Json.*;

import java.io.*;
import java.util.logging.*;

/**
 * Ecoskiller | CAT-GD — Group Discussion Orchestrator MCP Server
 * ==============================================================
 * Pure Java — ZERO external dependencies.
 * Transport : stdio  (JSON-RPC 2.0)
 * MCP Version: 2024-11-05
 *
 * 16 Agents covering the complete GD session lifecycle:
 *   Session management, participant coordination, FreeSWITCH provisioning,
 *   WebRTC/PSTN bridging, audio recording, transcription, speaker diarisation,
 *   scoring pipeline, dropout/rejoin handling, consent/compliance,
 *   audit trail, quality normalisation, and multi-session scaling.
 *
 * Security:
 *   • Regex allowlists on all identifiers
 *   • JWT structure validation (no Runtime.exec)
 *   • Sensitive values (tokens, credentials) masked in output
 *   • Size limits on free-form payloads
 *   • Audit timestamps on every response
 *   • Log-injection sanitisation
 */
public class GDMCPServer {

    static final String SERVER_NAME    = "mcp-gd-orchestrator";
    static final String SERVER_VERSION = "1.0.0";
    static final String MCP_VERSION    = "2024-11-05";
    static final Logger LOGGER         = Logger.getLogger(GDMCPServer.class.getName());

    private static final GDAgents AGENTS = new GDAgents();

    // ─────────────────────────────────────────────────────────────────────────
    public static void main(String[] args) throws Exception {
        setupLogger();
        LOGGER.info("[MCP] " + SERVER_NAME + " v" + SERVER_VERSION + " starting on stdio");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in,  "UTF-8"));
        PrintStream    writer = new PrintStream(System.out, true, "UTF-8");

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                String response = handleMessage(line);
                if (response != null) { writer.println(response); writer.flush(); }
            } catch (Exception e) {
                LOGGER.severe("[MCP] Fatal: " + sanitize(e.getMessage()));
                writer.println(errorResponse(null, -32700, "Parse error"));
                writer.flush();
            }
        }
    }

    // ── Dispatcher ────────────────────────────────────────────────────────────
    private static String handleMessage(String raw) {
        JValue req;
        try { req = Json.parse(raw); }
        catch (Exception e) { return errorResponse(null, -32700, "Parse error"); }

        if (!(req instanceof JObject obj))
            return errorResponse(null, -32600, "Invalid Request");
        if (!obj.has("jsonrpc") || !obj.has("method"))
            return errorResponse(null, -32600, "Missing jsonrpc or method");

        String method = obj.get("method").asText();
        JValue id     = obj.has("id") ? obj.get("id") : null;
        JValue params = obj.has("params") ? obj.get("params") : Json.object();

        LOGGER.info("[MCP] method=" + sanitize(method));

        return switch (method) {
            case "initialize"                -> handleInitialize(id);
            case "ping"                      -> successResponse(id, Json.object());
            case "tools/list"                -> handleToolsList(id);
            case "tools/call"                -> handleToolCall(id, params);
            case "notifications/initialized" -> null;
            default -> errorResponse(id, -32601, "Method not found: " + sanitize(method));
        };
    }

    // ── initialize ────────────────────────────────────────────────────────────
    private static String handleInitialize(JValue id) {
        JObject result = Json.object()
            .put("protocolVersion", MCP_VERSION)
            .put("capabilities", Json.object()
                .put("tools", Json.object().put("listChanged", false)))
            .put("serverInfo", Json.object()
                .put("name",    SERVER_NAME)
                .put("version", SERVER_VERSION));
        return successResponse(id, result);
    }

    // ── tools/list ────────────────────────────────────────────────────────────
    private static String handleToolsList(JValue id) {
        JArray tools = Json.array();

        tools.add(tool("session_lifecycle_manager",
            "Creates and manages GD session state machine: WAITING → SCHEDULED → ACTIVE → RECORDING → COMPLETED / FAILED. " +
            "Handles transitions on time, participant thresholds, and operator commands.",
            schema(p("action",     "string", "create | transition | get | list | fail"),
                   p("session_id", "string", "UUID session identifier (required for all except create)"),
                   p("topic",      "string", "GD discussion topic (required for create)"),
                   p("scheduled_time","string","ISO-8601 scheduled start time (required for create)"),
                   p("target_state","string","Target state for transition: SCHEDULED|ACTIVE|RECORDING|COMPLETED|FAILED"))));

        tools.add(tool("participant_coordinator",
            "Enrol, track, and validate GD participants. Manages candidate roster, join method (WebRTC/PSTN), " +
            "prevents duplicate or unauthorised joins, validates against planned roster.",
            schema(p("action",       "string",  "enrol | join | leave | list | validate_roster"),
                   p("session_id",   "string",  "Target session UUID"),
                   p("candidate_id", "string",  "Candidate identifier"),
                   p("join_method",  "string",  "webrtc | pstn | callback"),
                   p("jwt_token",    "string",  "JWT from Keycloak (validated, masked in output)"))));

        tools.add(tool("freeswitch_room_provisioner",
            "Provision and tear down FreeSWITCH SIP conference rooms. Creates room sip:gd-{session_id}@freeswitch.internal, " +
            "allocates MOH channels, sets max duration, enables auto-record.",
            schema(p("action",     "string", "provision | configure | teardown | status"),
                   p("session_id", "string", "Session UUID — room name: gd-{session_id}"),
                   p("max_duration_min","integer","Maximum conference duration 5-20 minutes"),
                   p("auto_record", "boolean","Enable FreeSWITCH conference auto-recording"))));

        tools.add(tool("webrtc_sip_bridge",
            "Manage WebRTC-to-SIP bridges for browser participants. Creates SIP user agent, " +
            "registers to FreeSWITCH, joins conference. Transparent to web user.",
            schema(p("action",       "string", "create_bridge | destroy_bridge | status | list_bridges"),
                   p("session_id",   "string", "GD session UUID"),
                   p("candidate_id", "string", "Candidate whose bridge to manage"),
                   p("webrtc_sdp",   "string", "WebRTC SDP offer from browser client (max 8KB)"))));

        tools.add(tool("pstn_call_handler",
            "Handle PSTN phone participants: IVR PIN validation, consent prompt (DPDP Act 2023), " +
            "conference transfer, ANI tracking, callback initiation.",
            schema(p("action",      "string", "validate_pin | initiate_callback | transfer_to_conference | get_status"),
                   p("session_id",  "string", "GD session UUID"),
                   p("phone_number","string", "E.164 phone number e.g. +919876543210"),
                   p("pin_code",    "string", "4-8 digit IVR PIN for session authentication"),
                   p("consent_given","boolean","DPDP Act 2023 consent flag from IVR prompt"))));

        tools.add(tool("audio_recording_manager",
            "Control FreeSWITCH conference recording. Start/stop WAV capture, upload to MinIO " +
            "s3://recordings/gd-{session_id}.wav, track file size/duration, validate integrity checksum.",
            schema(p("action",     "string",  "start | stop | upload | status | delete"),
                   p("session_id", "string",  "GD session UUID"),
                   p("format",     "string",  "wav_8khz | wav_16khz | wav_48khz"),
                   p("force_delete","boolean","Soft-delete for DPDP user rights request"))));

        tools.add(tool("speaker_turn_tracker",
            "Real-time speaker turn tracking: who spoke, when, for how long. " +
            "Stores to PostgreSQL gd_speaker_turns. Detects monologue (>70% time), silence, interruptions.",
            schema(p("action",     "string",  "record_turn | get_turns | analyse_distribution | get_metrics"),
                   p("session_id", "string",  "GD session UUID"),
                   p("speaker_id", "string",  "Candidate ID who spoke"),
                   p("start_time_sec","integer","Turn start relative to session start (seconds)"),
                   p("end_time_sec",  "integer","Turn end relative to session start (seconds)"))));

        tools.add(tool("transcription_pipeline",
            "Orchestrate Whisper STT transcription job. Submit WAV file, poll status, store transcript " +
            "in PostgreSQL gd_transcripts. Returns text + word-level timestamps + language detection.",
            schema(p("action",     "string", "submit | poll_status | store | get_transcript"),
                   p("session_id", "string", "GD session UUID"),
                   p("recording_url","string","MinIO WAV URL for transcription input"),
                   p("language",   "string", "Expected language hint: auto | en | hi | te | ta | kn"))));

        tools.add(tool("scoring_event_emitter",
            "Validate scoring prerequisites and emit gd.session.completed Kafka event. " +
            "Verifies transcript, speaker turns, participant list are all present before emitting.",
            schema(p("action",     "string", "validate_prerequisites | emit_event | check_status"),
                   p("session_id", "string", "GD session UUID"),
                   p("kafka_topic","string", "Target Kafka topic: gd.session.completed | gd.session.failed"),
                   p("force_emit", "boolean","Emit even if transcript pending (partial scoring)"))));

        tools.add(tool("dropout_rejoin_handler",
            "Handle participant dropout and rejoin. Allows rejoin within 2-minute window, " +
            "marks absent after window expires, records join/drop events for fairness scoring.",
            schema(p("action",       "string",  "record_dropout | attempt_rejoin | mark_absent | get_events"),
                   p("session_id",   "string",  "GD session UUID"),
                   p("candidate_id", "string",  "Candidate who dropped or is rejoining"),
                   p("dropout_reason","string", "network | voluntary | timeout | pstn_drop"),
                   p("rejoin_attempt","boolean","True if this is a rejoin attempt"))));

        tools.add(tool("timeout_duration_enforcer",
            "Enforce session timing rules: min 5 min (FAILED if less), max 20 min (auto-stop). " +
            "Start/check timers, log overrun events, auto-transition on expiry.",
            schema(p("action",     "string",  "start_timer | check_timer | enforce_max | log_overrun | get_status"),
                   p("session_id", "string",  "GD session UUID"),
                   p("min_duration_min","integer","Minimum valid session duration (default 5)"),
                   p("max_duration_min","integer","Maximum session duration before auto-stop (default 20)"))));

        tools.add(tool("audio_quality_normaliser",
            "Classify participant audio quality (PSTN 8kHz vs WebRTC 48kHz), configure FreeSWITCH " +
            "unified 16kHz codec, and generate scoring fairness adjustment tags.",
            schema(p("action",       "string", "classify | configure_codec | get_adjustments | tag_participant"),
                   p("session_id",   "string", "GD session UUID"),
                   p("candidate_id", "string", "Candidate to classify or configure"),
                   p("audio_type",   "string", "pstn | webrtc | unknown"),
                   p("sample_rate_hz","integer","Detected sample rate: 8000 | 16000 | 48000"))));

        tools.add(tool("redis_state_sync",
            "Manage Redis session cache and pub-sub. Sync state for <500ms client updates, " +
            "publish participant join/leave events, manage session roster key gd-session:{session_id}.",
            schema(p("action",     "string", "sync_state | publish_event | get_cached | invalidate | subscribe_info"),
                   p("session_id", "string", "GD session UUID"),
                   p("event_type", "string", "participant_joined | participant_left | state_changed | turn_started | session_ended"),
                   p("payload",    "string", "JSON event payload to publish (max 4KB)"))));

        tools.add(tool("consent_compliance_manager",
            "DPDP Act 2023 compliance: record consent, enforce retention (recordings 90 days, " +
            "transcripts 1 year), handle deletion requests, generate compliance audit report.",
            schema(p("action",      "string",  "record_consent | check_consent | schedule_deletion | delete_data | compliance_report"),
                   p("session_id",  "string",  "GD session UUID"),
                   p("candidate_id","string",  "Candidate whose consent/data to manage"),
                   p("consent_type","string",  "audio_recording | transcript | scoring_analysis"),
                   p("retention_override","boolean","Override default retention policy for dispute hold"))));

        tools.add(tool("session_audit_logger",
            "Record all session events to ClickHouse immutable audit log. Events: session_created, " +
            "candidate_joined, audio_started, disconnect, state_change. SOC2 and DPDP compliant.",
            schema(p("action",      "string", "log_event | query_log | export_session_log | get_summary"),
                   p("session_id",  "string", "GD session UUID"),
                   p("event_type",  "string", "session_created|candidate_joined|audio_started|disconnect|state_change|scoring_emitted"),
                   p("actor_id",    "string", "Who triggered the event: candidate_id or operator_id"),
                   p("event_detail","string", "JSON event detail (max 2KB)"))));

        tools.add(tool("scaling_load_manager",
            "Manage session affinity, replica capacity, and HPA triggers. " +
            "Assigns sessions to orchestrator replicas using session_id % num_replicas hashing. " +
            "Reports CPU/memory/session-count metrics for Kubernetes autoscaling decisions.",
            schema(p("action",        "string",  "assign_replica | get_capacity | report_metrics | rebalance | get_affinity"),
                   p("session_id",    "string",  "GD session UUID to assign or check"),
                   p("num_replicas",  "integer", "Current active orchestrator replica count (1-10)"),
                   p("replica_id",    "integer", "Specific replica ID to query (0-indexed)"),
                   p("force_rebalance","boolean","Force session rebalance across replicas"))));

        return successResponse(null, Json.object().put("tools", tools));
    }

    // ── tools/call ────────────────────────────────────────────────────────────
    private static String handleToolCall(JValue id, JValue paramsVal) {
        if (!(paramsVal instanceof JObject params))
            return errorResponse(id, -32602, "params must be object");
        if (!params.has("name"))
            return errorResponse(id, -32602, "Missing tool name");

        String toolName = params.get("name").asText();
        if (!toolName.matches("[a-z_]{3,60}"))
            return errorResponse(id, -32602, "Invalid tool name format");

        JValue argsVal = params.has("arguments") ? params.get("arguments") : Json.object();
        if (!(argsVal instanceof JObject args))
            return errorResponse(id, -32602, "arguments must be object");

        LOGGER.info("[TOOL] " + toolName);

        try {
            JObject agentResult = AGENTS.dispatch(toolName, args);
            JObject result = Json.object()
                .put("content", Json.array().add(
                    Json.object().put("type","text").put("text", agentResult.toJson())))
                .put("isError", false);
            return successResponse(id, result);

        } catch (SecurityException se) {
            LOGGER.warning("[SECURITY] " + sanitize(se.getMessage()));
            return errorResponse(id, -32003, "Security violation: " + sanitize(se.getMessage()));
        } catch (IllegalArgumentException iae) {
            return errorResponse(id, -32602, "Invalid arguments: " + sanitize(iae.getMessage()));
        } catch (Exception e) {
            LOGGER.severe("[TOOL ERROR] " + sanitize(e.getMessage()));
            return errorResponse(id, -32000, "Tool error: " + sanitize(e.getMessage()));
        }
    }

    // ── JSON-RPC helpers ──────────────────────────────────────────────────────
    static String successResponse(JValue id, JObject result) {
        JObject r = Json.object().put("jsonrpc", "2.0");
        if (id != null) r.put("id", id);
        r.put("result", result);
        return r.toJson();
    }

    static String errorResponse(JValue id, int code, String message) {
        JObject r = Json.object().put("jsonrpc", "2.0");
        if (id != null) r.put("id", id);
        r.put("error", Json.object().put("code", code).put("message", message));
        return r.toJson();
    }

    // ── Schema helpers ────────────────────────────────────────────────────────
    private static JObject tool(String name, String desc, JObject schema) {
        return Json.object().put("name", name).put("description", desc).put("inputSchema", schema);
    }

    private static JObject schema(JObject... props) {
        JObject properties = Json.object();
        JArray  required   = Json.array();
        for (JObject prop : props) {
            String nm = prop.get("_n").asText();
            properties.put(nm, Json.object()
                .put("type",        prop.get("type").asText())
                .put("description", prop.get("desc").asText()));
            required.add(nm);
        }
        return Json.object().put("type","object").put("properties", properties).put("required", required);
    }

    static JObject p(String name, String type, String desc) {
        return Json.object().put("_n", name).put("type", type).put("desc", desc);
    }

    // ── Security helpers ──────────────────────────────────────────────────────
    public static String sanitize(String input) {
        if (input == null) return "null";
        String clean = input.replaceAll("[\\p{Cntrl}]", "");
        return clean.length() > 200 ? clean.substring(0, 200) : clean;
    }

    // ── Logger ────────────────────────────────────────────────────────────────
    private static void setupLogger() {
        Logger root = Logger.getLogger("");
        for (var h : root.getHandlers()) root.removeHandler(h);
        StreamHandler sh = new StreamHandler(System.err, new SimpleFormatter());
        sh.setLevel(Level.ALL);
        root.addHandler(sh);
        root.setLevel(Level.INFO);
    }
}
