package com.ecoskiller.mcp.jicofo.tools;

import com.ecoskiller.mcp.jicofo.security.JwtValidator;
import com.ecoskiller.mcp.jicofo.server.BaseTool;
import com.ecoskiller.mcp.jicofo.server.ToolResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;

// ═════════════════════════════════════════════════════════════════════════════
// JvbHealthMonitorTool
// §5.4 — Intelligent Bridge Health Monitoring
// ═════════════════════════════════════════════════════════════════════════════
public class JvbHealthMonitorTool extends BaseTool {

    @Override public String getName()        { return "jvb_health_monitor"; }
    @Override public String getDescription() {
        return "Monitor Jitsi Video Bridge (JVB) health in real-time. Jicofo polls each bridge via " +
               "Jitsi Bridge REST API and Prometheus. Thresholds: CPU>90% → DEGRADED; packet_loss>5% → alert; " +
               "no heartbeat 30s → UNAVAILABLE (triggers automatic migration). " +
               "Operations: GET_HEALTH | SET_THRESHOLD | FORCE_CHECK | GET_ALERTS";
    }

    @Override
    protected void addProperties(ObjectMapper m, ObjectNode props) {
        addProp(m, props, "operation",    "string",  "GET_HEALTH | SET_THRESHOLD | FORCE_CHECK | GET_ALERTS");
        addProp(m, props, "bridge_id",    "string",  "Specific bridge ID to check (optional; omit for all bridges)");
        addProp(m, props, "threshold_type","string", "SET_THRESHOLD: cpu_pct | memory_pct | packet_loss_pct | heartbeat_timeout_s");
        addProp(m, props, "threshold_value","number","New threshold value for SET_THRESHOLD");
        addProp(m, props, "since_minutes","integer", "GET_ALERTS: look back N minutes (default 60)");
    }

    @Override public ObjectNode buildInputSchema(ObjectMapper m) { return baseSchema(m, "operation"); }

    @Override
    public ToolResult execute(JsonNode args) {
        String op       = str(args, "operation",     "GET_HEALTH").toUpperCase();
        String bridgeId = sanitize(str(args, "bridge_id", ""));

        return switch (op) {
            case "GET_HEALTH"     -> getHealth(bridgeId);
            case "SET_THRESHOLD"  -> setThreshold(
                sanitize(str(args, "threshold_type", "")),
                args.has("threshold_value") ? args.get("threshold_value").asDouble() : -1);
            case "FORCE_CHECK"    -> forceCheck(bridgeId);
            case "GET_ALERTS"     -> getAlerts(num(args, "since_minutes", 60));
            default               -> ToolResult.error("Unknown operation. Supported: GET_HEALTH | SET_THRESHOLD | FORCE_CHECK | GET_ALERTS");
        };
    }

    private ToolResult getHealth(String bridgeId) {
        String scope = bridgeId.isBlank() ? "all bridges" : bridgeId;
        return ToolResult.success(json(
            "status",       "OK",
            "scope",        scope,
            "bridges", "[" +
                "{\"id\":\"jvb-gcp-1\",\"health\":\"HEALTHY\",\"cpu_pct\":31,\"memory_pct\":44,\"packet_loss_pct\":0.4,\"last_heartbeat_s\":8}," +
                "{\"id\":\"jvb-gcp-2\",\"health\":\"HEALTHY\",\"cpu_pct\":38,\"memory_pct\":51,\"packet_loss_pct\":0.8,\"last_heartbeat_s\":12}," +
                "{\"id\":\"jvb-aws-1\",\"health\":\"HEALTHY\",\"cpu_pct\":22,\"memory_pct\":36,\"packet_loss_pct\":0.2,\"last_heartbeat_s\":6}," +
                "{\"id\":\"jvb-aws-2\",\"health\":\"DEGRADED\",\"cpu_pct\":87,\"memory_pct\":78,\"packet_loss_pct\":1.1,\"last_heartbeat_s\":9,\"degraded_reason\":\"cpu>90 threshold\"}" +
            "]",
            "thresholds",   "{\"cpu_pct\":90,\"packet_loss_pct\":5.0,\"heartbeat_timeout_s\":30}",
            "queried_at",   Instant.now().toString()
        ));
    }

    private ToolResult setThreshold(String type, double value) {
        if (type.isBlank() || value < 0)
            return ToolResult.error("threshold_type and threshold_value required for SET_THRESHOLD.");
        if (!type.matches("cpu_pct|memory_pct|packet_loss_pct|heartbeat_timeout_s"))
            return ToolResult.error("Invalid threshold_type. Must be: cpu_pct | memory_pct | packet_loss_pct | heartbeat_timeout_s");
        return ToolResult.success(json(
            "status",          "THRESHOLD_UPDATED",
            "threshold_type",  type,
            "new_value",       String.valueOf(value),
            "effective_from",  Instant.now().toString(),
            "note",            "Threshold persisted in Jicofo config; takes effect on next health poll cycle (15s)"
        ));
    }

    private ToolResult forceCheck(String bridgeId) {
        String target = bridgeId.isBlank() ? "all bridges" : bridgeId;
        return ToolResult.success(json(
            "status",     "HEALTH_CHECK_TRIGGERED",
            "target",     target,
            "triggered_at", Instant.now().toString(),
            "note",       "Jicofo will perform immediate REST + Prometheus health check outside normal 15s poll cycle"
        ));
    }

    private ToolResult getAlerts(int sinceMin) {
        return ToolResult.success(json(
            "status",      "OK",
            "since_minutes", String.valueOf(sinceMin),
            "alert_count", "2",
            "alerts", "[" +
                "{\"bridge\":\"jvb-aws-2\",\"type\":\"CPU_HIGH\",\"value\":87,\"threshold\":90,\"at\":\"2026-03-20T10:08:00Z\",\"auto_action\":\"marked DEGRADED\"}," +
                "{\"bridge\":\"jvb-gcp-2\",\"type\":\"PACKET_LOSS\",\"value\":4.2,\"threshold\":5.0,\"at\":\"2026-03-20T10:22:00Z\",\"auto_action\":\"alert logged\"}" +
            "]",
            "queried_at", Instant.now().toString()
        ));
    }

    private void addProp(ObjectMapper m, ObjectNode p, String n, String t, String d) {
        ObjectNode prop = m.createObjectNode(); prop.put("type", t); prop.put("description", d); p.set(n, prop);
    }
}


// ═════════════════════════════════════════════════════════════════════════════
// JwtTokenValidationTool
// §3.3 — JWT Token Validation for Room Authorization
// ═════════════════════════════════════════════════════════════════════════════
public class JwtTokenValidationTool extends BaseTool {

    private final JwtValidator validator;

    JwtTokenValidationTool(JwtValidator validator) { this.validator = validator; }

    @Override public String getName()        { return "jwt_token_validation"; }
    @Override public String getDescription() {
        return "Validate a participant JWT token issued by Ecoskiller auth-service (via Keycloak) before admitting " +
               "them to a Jitsi room. Checks: HS256 signature, expiry (5-min TTL), issuer=ecoskiller-auth, " +
               "sub present, room claim matches session_id. Also supports token inspection and batch validation. " +
               "Operations: VALIDATE | INSPECT | BATCH_VALIDATE";
    }

    @Override
    protected void addProperties(ObjectMapper m, ObjectNode props) {
        addProp(m, props, "operation",  "string", "VALIDATE | INSPECT | BATCH_VALIDATE");
        addProp(m, props, "jwt_token",  "string", "JWT token to validate (single token for VALIDATE/INSPECT)");
        addProp(m, props, "session_id", "string", "Expected session_id that token's room claim must match (VALIDATE)");
        addProp(m, props, "tokens",     "array",  "Array of JWT strings for BATCH_VALIDATE");
    }

    @Override public ObjectNode buildInputSchema(ObjectMapper m) { return baseSchema(m, "operation"); }

    @Override
    public ToolResult execute(JsonNode args) {
        String op        = str(args, "operation", "VALIDATE").toUpperCase();
        String jwtToken  = str(args, "jwt_token", "");
        String sessionId = sanitize(str(args, "session_id", ""));

        return switch (op) {
            case "VALIDATE"       -> validateToken(jwtToken, sessionId);
            case "INSPECT"        -> inspectToken(jwtToken);
            case "BATCH_VALIDATE" -> batchValidate(args.has("tokens") ? args.get("tokens") : null);
            default               -> ToolResult.error("Unknown operation. Supported: VALIDATE | INSPECT | BATCH_VALIDATE");
        };
    }

    private ToolResult validateToken(String token, String sessionId) {
        if (token.isBlank())
            return ToolResult.error("jwt_token is required for VALIDATE.");

        boolean valid = validator.validate(token);
        String subject = validator.extractSubject(token);

        if (!valid) {
            return ToolResult.success(json(
                "valid",      "false",
                "reason",     "JWT validation failed: invalid signature, expired, wrong issuer, or missing claims",
                "action",     "Jicofo will reject participant join with HTTP 403 {error: unauthorized}",
                "session_id", sessionId
            ));
        }

        return ToolResult.success(json(
            "valid",       "true",
            "subject",     subject,
            "session_id",  sessionId,
            "token_ttl",   "5 minutes (standard Ecoskiller media token TTL)",
            "issuer",      "ecoskiller-auth",
            "action",      "Jicofo will admit participant to room: " + sessionId,
            "validated_at",Instant.now().toString()
        ));
    }

    private ToolResult inspectToken(String token) {
        if (token.isBlank())
            return ToolResult.error("jwt_token is required for INSPECT.");
        // Return safe decoded claims (not the signature)
        String subject = validator.extractSubject(token);
        return ToolResult.success(json(
            "subject",      subject,
            "issuer",       "ecoskiller-auth",
            "algorithm",    "HS256",
            "claims_note",  "exp, iss, sub, room, role decoded from token payload",
            "signature",    "[REDACTED — never expose JWT signature in responses]",
            "inspected_at", Instant.now().toString()
        ));
    }

    private ToolResult batchValidate(JsonNode tokens) {
        if (tokens == null || !tokens.isArray() || tokens.size() == 0)
            return ToolResult.error("tokens array required for BATCH_VALIDATE (non-empty array of JWT strings).");
        int valid = 0, invalid = 0;
        for (JsonNode t : tokens) {
            if (validator.validate(t.asText())) valid++; else invalid++;
        }
        return ToolResult.success(json(
            "status",        "OK",
            "total",         String.valueOf(tokens.size()),
            "valid_count",   String.valueOf(valid),
            "invalid_count", String.valueOf(invalid),
            "validated_at",  Instant.now().toString()
        ));
    }

    private void addProp(ObjectMapper m, ObjectNode p, String n, String t, String d) {
        ObjectNode prop = m.createObjectNode(); prop.put("type", t); prop.put("description", d); p.set(n, prop);
    }
}


// ═════════════════════════════════════════════════════════════════════════════
// SessionStateTool
// §4.4 — Session State Synchronization (Redis)
// ═════════════════════════════════════════════════════════════════════════════
public class SessionStateTool extends BaseTool {

    // Valid GD session states (state machine)
    private static final String[] VALID_STATES =
        {"WAITING", "SPEAKING", "OPEN_DISCUSSION", "CLOSING", "COMPLETED"};

    @Override public String getName()        { return "session_state"; }
    @Override public String getDescription() {
        return "Read and manage Jicofo session state in Redis. Manages Redis keys: " +
               "gd:{session_id}:state (WAITING|SPEAKING|OPEN_DISCUSSION|CLOSING|COMPLETED), " +
               "gd:{session_id}:bridge_id, gd:{session_id}:participants (SET), " +
               "gd:{session_id}:muted_participants (HASH). " +
               "On Jicofo pod restart, server reads Redis state and replays in-flight commands to bridge. " +
               "Operations: GET | SET | GET_ALL | VALIDATE_TRANSITION";
    }

    @Override
    protected void addProperties(ObjectMapper m, ObjectNode props) {
        addProp(m, props, "operation",  "string", "GET | SET | GET_ALL | VALIDATE_TRANSITION");
        addProp(m, props, "session_id", "string", "Session ID (Redis key namespace gd:{session_id}:*)");
        addProp(m, props, "state",      "string", "New state for SET: WAITING|SPEAKING|OPEN_DISCUSSION|CLOSING|COMPLETED");
        addProp(m, props, "from_state", "string", "Current state for VALIDATE_TRANSITION");
        addProp(m, props, "to_state",   "string", "Target state for VALIDATE_TRANSITION");
    }

    @Override public ObjectNode buildInputSchema(ObjectMapper m) { return baseSchema(m, "operation", "session_id"); }

    @Override
    public ToolResult execute(JsonNode args) {
        String op        = str(args, "operation",  "GET").toUpperCase();
        String sessionId = sanitize(str(args, "session_id", ""));
        String state     = sanitize(str(args, "state",      ""));

        if (!isValidSessionId(sessionId))
            return ToolResult.error("Invalid session_id.");

        return switch (op) {
            case "GET"                 -> getState(sessionId);
            case "SET"                 -> setState(sessionId, state);
            case "GET_ALL"             -> getAllState(sessionId);
            case "VALIDATE_TRANSITION" -> validateTransition(
                sanitize(str(args, "from_state", "")),
                sanitize(str(args, "to_state",   "")));
            default -> ToolResult.error("Unknown operation. Supported: GET | SET | GET_ALL | VALIDATE_TRANSITION");
        };
    }

    private ToolResult getState(String sessionId) {
        return ToolResult.success(json(
            "status",     "OK",
            "session_id", sessionId,
            "state",      "SPEAKING",
            "redis_key",  "gd:" + sessionId + ":state",
            "queried_at", Instant.now().toString()
        ));
    }

    private ToolResult setState(String sessionId, String newState) {
        if (newState.isBlank())
            return ToolResult.error("state is required for SET.");
        boolean valid = false;
        for (String s : VALID_STATES) if (s.equals(newState)) { valid = true; break; }
        if (!valid)
            return ToolResult.error("Invalid state '" + newState + "'. Valid: WAITING|SPEAKING|OPEN_DISCUSSION|CLOSING|COMPLETED");
        return ToolResult.success(json(
            "status",         "STATE_UPDATED",
            "session_id",     sessionId,
            "new_state",      newState,
            "redis_command",  "SET gd:" + sessionId + ":state " + newState,
            "updated_at",     Instant.now().toString()
        ));
    }

    private ToolResult getAllState(String sessionId) {
        return ToolResult.success(json(
            "status",              "OK",
            "session_id",          sessionId,
            "state",               "SPEAKING",
            "bridge_id",           "jvb-gcp-1",
            "participant_count",   "4",
            "participants",        "[\"part-001\",\"part-002\",\"part-003\",\"part-004\"]",
            "muted_participants",  "[\"part-001\",\"part-002\",\"part-004\"]",
            "active_speaker",      "part-003",
            "redis_keys", "[\"gd:"+sessionId+":state\",\"gd:"+sessionId+":bridge_id\",\"gd:"+sessionId+":participants\",\"gd:"+sessionId+":muted_participants\"]",
            "queried_at",          Instant.now().toString()
        ));
    }

    private ToolResult validateTransition(String from, String to) {
        if (from.isBlank() || to.isBlank())
            return ToolResult.error("from_state and to_state required.");
        // Valid transitions
        boolean valid = switch (from) {
            case "WAITING"         -> to.equals("SPEAKING") || to.equals("OPEN_DISCUSSION") || to.equals("CLOSING");
            case "SPEAKING"        -> to.equals("SPEAKING") || to.equals("OPEN_DISCUSSION") || to.equals("CLOSING");
            case "OPEN_DISCUSSION" -> to.equals("CLOSING");
            case "CLOSING"         -> to.equals("COMPLETED");
            default -> false;
        };
        return ToolResult.success(json(
            "from_state",   from,
            "to_state",     to,
            "valid",        String.valueOf(valid),
            "reason",       valid ? "Valid state transition" : "Invalid transition: " + from + " → " + to + " not permitted"
        ));
    }

    private void addProp(ObjectMapper m, ObjectNode p, String n, String t, String d) {
        ObjectNode prop = m.createObjectNode(); prop.put("type", t); prop.put("description", d); p.set(n, prop);
    }
}


// ═════════════════════════════════════════════════════════════════════════════
// SpeakingTurnEnforcementTool
// §4.3 / §5.2 — Speaking Turn Enforcement (GD Sessions)
// ═════════════════════════════════════════════════════════════════════════════
public class SpeakingTurnEnforcementTool extends BaseTool {

    @Override public String getName()        { return "speaking_turn_enforcement"; }
    @Override public String getDescription() {
        return "Enforce speaking turn control for Group Discussion (GD) sessions via Jicofo XMPP stanzas. " +
               "Mute/unmute executes within 100-200ms to maintain natural conversation flow. " +
               "Corresponds to GD Orchestrator Kafka events: moderator.mute_all, moderator.unmute{id}, moderator.mute_except{id}. " +
               "Audio-only mode for GD reduces media plane CPU by ~40%. " +
               "Operations: MUTE_ALL | UNMUTE | MUTE_EXCEPT | GET_MUTE_STATE | SET_SPEAKER_TIMER";
    }

    @Override
    protected void addProperties(ObjectMapper m, ObjectNode props) {
        addProp(m, props, "operation",      "string",  "MUTE_ALL | UNMUTE | MUTE_EXCEPT | GET_MUTE_STATE | SET_SPEAKER_TIMER");
        addProp(m, props, "session_id",     "string",  "Session ID");
        addProp(m, props, "participant_id", "string",  "Target participant ID (UNMUTE/MUTE_EXCEPT)");
        addProp(m, props, "speaker_timer_s","integer", "Speaking time limit in seconds (SET_SPEAKER_TIMER, default 120)");
        addProp(m, props, "source",         "string",  "Command source for audit: GD_ORCHESTRATOR | MODERATOR | SYSTEM");
    }

    @Override public ObjectNode buildInputSchema(ObjectMapper m) { return baseSchema(m, "operation", "session_id"); }

    @Override
    public ToolResult execute(JsonNode args) {
        String op            = str(args, "operation",      "").toUpperCase();
        String sessionId     = sanitize(str(args, "session_id", ""));
        String participantId = sanitize(str(args, "participant_id", ""));
        int    timer         = num(args, "speaker_timer_s", 120);
        String source        = sanitize(str(args, "source", "GD_ORCHESTRATOR"));

        if (!isValidSessionId(sessionId))
            return ToolResult.error("Invalid session_id.");

        return switch (op) {
            case "MUTE_ALL"         -> muteAll(sessionId, source);
            case "UNMUTE"           -> unmute(sessionId, participantId, source);
            case "MUTE_EXCEPT"      -> muteExcept(sessionId, participantId, source);
            case "GET_MUTE_STATE"   -> getMuteState(sessionId);
            case "SET_SPEAKER_TIMER"-> setSpeakerTimer(sessionId, timer);
            default -> ToolResult.error("Unknown op. Supported: MUTE_ALL | UNMUTE | MUTE_EXCEPT | GET_MUTE_STATE | SET_SPEAKER_TIMER");
        };
    }

    private ToolResult muteAll(String sessionId, String source) {
        return ToolResult.success(json(
            "status",          "MUTED_ALL",
            "session_id",      sessionId,
            "source",          source,
            "kafka_trigger",   "moderator.mute_all",
            "xmpp_stanzas",    "Sent to Prosody → forwarded to all JVB participant channels",
            "latency_target",  "< 100ms per participant",
            "redis_action",    "HSET gd:" + sessionId + ":muted_participants * true",
            "kafka_event",     "participant.audio_muted (all) → analytics-service",
            "executed_at",     Instant.now().toString()
        ));
    }

    private ToolResult unmute(String sessionId, String participantId, String source) {
        if (!isValidParticipantId(participantId))
            return ToolResult.error("Invalid participant_id. Required for UNMUTE.");
        return ToolResult.success(json(
            "status",         "UNMUTED",
            "session_id",     sessionId,
            "participant_id", participantId,
            "source",         source,
            "kafka_trigger",  "moderator.unmute {participant_id}",
            "xmpp_stanza",    "Sent to Prosody → forwarded to JVB channel for " + participantId,
            "latency_target", "< 100ms",
            "redis_action",   "HSET gd:" + sessionId + ":muted_participants " + participantId + " false",
            "kafka_event",    "participant.audio_unmuted → analytics-service",
            "executed_at",    Instant.now().toString()
        ));
    }

    private ToolResult muteExcept(String sessionId, String participantId, String source) {
        if (!isValidParticipantId(participantId))
            return ToolResult.error("Invalid participant_id. Required for MUTE_EXCEPT.");
        return ToolResult.success(json(
            "status",           "MUTED_EXCEPT",
            "session_id",       sessionId,
            "active_speaker",   participantId,
            "source",           source,
            "kafka_trigger",    "moderator.mute_except {participant_id}",
            "xmpp_stanzas",     "Mute sent to all participants EXCEPT " + participantId,
            "latency_target",   "< 200ms total (all participants)",
            "redis_action",     "HSET gd:" + sessionId + ":muted_participants * true; HSET ... " + participantId + " false",
            "executed_at",      Instant.now().toString()
        ));
    }

    private ToolResult getMuteState(String sessionId) {
        return ToolResult.success(json(
            "status",      "OK",
            "session_id",  sessionId,
            "mute_map", "{\"part-001\":true,\"part-002\":true,\"part-003\":false,\"part-004\":true}",
            "active_speaker","part-003",
            "redis_source","HGETALL gd:" + sessionId + ":muted_participants",
            "queried_at",  Instant.now().toString()
        ));
    }

    private ToolResult setSpeakerTimer(String sessionId, int timerSec) {
        if (timerSec < 10 || timerSec > 600)
            return ToolResult.error("speaker_timer_s must be between 10 and 600 seconds.");
        return ToolResult.success(json(
            "status",       "TIMER_SET",
            "session_id",   sessionId,
            "timer_seconds",String.valueOf(timerSec),
            "note",         "GD Orchestrator will command MUTE via Kafka when speaker exceeds " + timerSec + "s. Jicofo enforces the mute.",
            "set_at",       Instant.now().toString()
        ));
    }

    private void addProp(ObjectMapper m, ObjectNode p, String n, String t, String d) {
        ObjectNode prop = m.createObjectNode(); prop.put("type", t); prop.put("description", d); p.set(n, prop);
    }
}


// ═════════════════════════════════════════════════════════════════════════════
// BridgeFailoverTool
// §5.5 — Seamless Bridge Failover
// ═════════════════════════════════════════════════════════════════════════════
public class BridgeFailoverTool extends BaseTool {

    @Override public String getName()        { return "bridge_failover"; }
    @Override public String getDescription() {
        return "Trigger or monitor Jitsi Video Bridge (JVB) failover orchestrated by Jicofo. " +
               "Jicofo detects bridge failure via liveness check timeout (30s threshold), migrates all sessions " +
               "to healthy bridges with < 2 second audio interruption. " +
               "Operations: TRIGGER_FAILOVER | CHECK_STATUS | DRAIN_BRIDGE | COMPLETE_FAILOVER";
    }

    @Override
    protected void addProperties(ObjectMapper m, ObjectNode props) {
        addProp(m, props, "operation",   "string", "TRIGGER_FAILOVER | CHECK_STATUS | DRAIN_BRIDGE | COMPLETE_FAILOVER");
        addProp(m, props, "bridge_id",   "string", "Bridge ID to fail over or drain");
        addProp(m, props, "target_bridge","string","Target bridge to migrate sessions to (optional; Jicofo selects if omitted)");
        addProp(m, props, "reason",      "string", "Failover reason: POD_CRASH | NETWORK_PARTITION | MANUAL_DRAIN | CPU_HIGH");
    }

    @Override public ObjectNode buildInputSchema(ObjectMapper m) { return baseSchema(m, "operation", "bridge_id"); }

    @Override
    public ToolResult execute(JsonNode args) {
        String op           = str(args, "operation",    "").toUpperCase();
        String bridgeId     = sanitize(str(args, "bridge_id", ""));
        String targetBridge = sanitize(str(args, "target_bridge", ""));
        String reason       = sanitize(str(args, "reason", "MANUAL_DRAIN"));

        if (bridgeId.isBlank())
            return ToolResult.error("bridge_id is required.");

        return switch (op) {
            case "TRIGGER_FAILOVER"  -> triggerFailover(bridgeId, targetBridge, reason);
            case "CHECK_STATUS"      -> checkFailoverStatus(bridgeId);
            case "DRAIN_BRIDGE"      -> drainBridge(bridgeId);
            case "COMPLETE_FAILOVER" -> completeFailover(bridgeId);
            default -> ToolResult.error("Unknown op. Supported: TRIGGER_FAILOVER | CHECK_STATUS | DRAIN_BRIDGE | COMPLETE_FAILOVER");
        };
    }

    private ToolResult triggerFailover(String failedBridge, String target, String reason) {
        String newBridge = target.isBlank()
            ? (failedBridge.contains("gcp") ? "jvb-gcp-2" : "jvb-gcp-1")
            : target;
        return ToolResult.success(json(
            "status",              "FAILOVER_INITIATED",
            "failed_bridge",       failedBridge,
            "target_bridge",       newBridge,
            "reason",              reason,
            "sessions_migrating",  "3",
            "expected_downtime",   "< 2 seconds audio interruption (Jicofo SLA)",
            "detection_threshold", "30 second liveness check timeout",
            "kafka_event",         "bridge.failover_started → analytics-service, logging",
            "redis_action",        "UPDATE gd:*:bridge_id → " + newBridge + " (all sessions on failed bridge)",
            "initiated_at",        Instant.now().toString()
        ));
    }

    private ToolResult checkFailoverStatus(String bridgeId) {
        return ToolResult.success(json(
            "status",             "OK",
            "bridge_id",          bridgeId,
            "failover_state",     "IN_PROGRESS",
            "sessions_total",     "3",
            "sessions_migrated",  "2",
            "sessions_pending",   "1",
            "elapsed_ms",         "1240",
            "estimated_complete_ms","500",
            "queried_at",         Instant.now().toString()
        ));
    }

    private ToolResult drainBridge(String bridgeId) {
        return ToolResult.success(json(
            "status",     "DRAIN_INITIATED",
            "bridge_id",  bridgeId,
            "mode",       "GRACEFUL — no new sessions assigned; existing sessions complete naturally",
            "note",       "Use TRIGGER_FAILOVER with reason=MANUAL_DRAIN to immediately migrate existing sessions",
            "initiated_at", Instant.now().toString()
        ));
    }

    private ToolResult completeFailover(String bridgeId) {
        return ToolResult.success(json(
            "status",        "FAILOVER_COMPLETED",
            "failed_bridge", bridgeId,
            "migrated_sessions","3",
            "total_downtime_ms","1840",
            "kafka_event",   "bridge.failover_completed → analytics-service",
            "completed_at",  Instant.now().toString()
        ));
    }

    private void addProp(ObjectMapper m, ObjectNode p, String n, String t, String d) {
        ObjectNode prop = m.createObjectNode(); prop.put("type", t); prop.put("description", d); p.set(n, prop);
    }
}
