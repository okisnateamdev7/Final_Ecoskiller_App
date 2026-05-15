package com.ecoskiller.mcp.session.tools;

import com.ecoskiller.mcp.session.client.SessionControlClient;
import com.ecoskiller.mcp.session.client.SessionControlClient.ServiceResponse;
import com.ecoskiller.mcp.session.config.SessionControlConfig;
import com.ecoskiller.mcp.session.security.AuditLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Remaining tools in three categories:
 *
 *  Jitsi Room Management (3):
 *   13. jitsi_get_token     — Generate a Jitsi room JWT token for a participant
 *   14. jitsi_mute          — Mute a participant in the Jitsi room
 *   15. jitsi_force_end     — Force-end the Jitsi room (emergency stop)
 *
 *  Audit & Compliance (3):
 *   16. session_audit_log   — Retrieve immutable audit log for a session
 *   17. session_events      — Get full event history (Kafka events published)
 *   18. session_export      — Export complete session data for compliance/archival
 *
 *  State Machine Helpers (3):
 *   19. session_start       — Shortcut: WAITING_FOR_PARTICIPANTS → IN_PROGRESS
 *   20. session_suspend     — Shortcut: IN_PROGRESS → SUSPENDED (emergency pause)
 *   21. session_resume      — Shortcut: SUSPENDED → IN_PROGRESS
 *   22. session_terminate   — Shortcut: any active state → COMPLETED + cleanup
 *
 *  Health & Monitoring (2):
 *   23. service_health      — Check Session-Control-Service liveness + readiness
 *   24. session_stats       — Active session statistics for monitoring dashboards
 */

// ═════════════════════════════════════════════════════════════════════════════
// JITSI ROOM MANAGEMENT
// ═════════════════════════════════════════════════════════════════════════════

// ─────────────────────────────────────────────────────────────────────────────
// 13. jitsi_get_token
// ─────────────────────────────────────────────────────────────────────────────
class JitsiGetTokenTool extends BaseTool {

    JitsiGetTokenTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "jitsi_get_token"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",        apiKeyProp());
        p.set("session_id",     sessionIdProp());
        p.set("participant_id", participantIdProp());
        p.set("role",           enumProp("Jitsi moderator role", "moderator", "member"));
        p.set("ttl_secs",       intProp("Token TTL in seconds (default: 300 = 5 minutes, per spec)"));
        return buildSchema(name(),
            "Generate a short-lived Jitsi room JWT token (5-minute TTL per spec) for a participant. " +
            "Token is scoped to the session's Jitsi room and the participant's user ID. " +
            "Use this token to connect to the WebRTC session.",
            p, "session_id", "participant_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId     = validateSessionId(requireString(args, "session_id"));
        String participantId = validateParticipantId(requireString(args, "participant_id"));
        String role          = optString(args, "role", "member");
        int    ttl           = Math.min(optInt(args, "ttl_secs", 300), 600); // cap at 10 min

        if (!java.util.Set.of("moderator","member").contains(role.toLowerCase())) {
            return ToolResult.error("Invalid role: must be 'moderator' or 'member'");
        }

        ObjectNode body = json.createObjectNode();
        body.put("participantId", participantId);
        body.put("role",          role.toLowerCase());
        body.put("ttlSecs",       ttl);

        ServiceResponse r = client.getJitsiToken(sessionId, json.writeValueAsString(body));
        if (!r.isSuccess()) return serviceError(r.statusCode(), r.body(), "jitsi_get_token");

        ObjectNode out = json.createObjectNode();
        out.put("success",        true);
        out.put("session_id",     sessionId);
        out.put("participant_id", participantId);
        out.put("jitsi_token",    r.body().path("token").asText(""));
        out.put("jitsi_room_id",  r.body().path("roomId").asText(""));
        out.put("jitsi_server",   r.body().path("server").asText(""));
        out.put("ttl_secs",       ttl);
        out.put("expires_at",     r.body().path("expiresAt").asText(""));
        out.put("message", "Use jitsi_token with the Jitsi SDK/API to join the WebRTC room");
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 14. jitsi_mute
// ─────────────────────────────────────────────────────────────────────────────
class JitsiMuteTool extends BaseTool {

    JitsiMuteTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "jitsi_mute"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",        apiKeyProp());
        p.set("session_id",     sessionIdProp());
        p.set("participant_id", strProp("Participant UUID to mute/unmute. Use 'ALL' to mute entire room."));
        p.set("mute",           boolProp("true = mute, false = unmute"));
        p.set("track",          enumProp("Which track to affect", "audio", "video", "both"));
        return buildSchema(name(),
            "Mute or unmute a participant (or all participants) in the Jitsi room. " +
            "Moderator action — requires ASSESSOR or MODERATOR role. " +
            "Passes through to Jitsi REST API via session-control-service.",
            p, "session_id", "participant_id", "mute");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId     = validateSessionId(requireString(args, "session_id"));
        String participantId = requireString(args, "participant_id");
        boolean mute         = optBool(args, "mute", true);
        String track         = optString(args, "track", "audio");

        // Allow "ALL" or valid UUID
        if (!"ALL".equals(participantId)) {
            participantId = validateParticipantId(participantId);
        }

        ObjectNode body = json.createObjectNode();
        body.put("participantId", participantId);
        body.put("mute",          mute);
        body.put("track",         track);

        ServiceResponse r = client.muteParticipant(sessionId, json.writeValueAsString(body));
        if (r.isForbidden()) return ToolResult.error("Mute denied — requires ASSESSOR or MODERATOR role");
        if (!r.isSuccess())  return serviceError(r.statusCode(), r.body(), "jitsi_mute");

        ObjectNode out = json.createObjectNode();
        out.put("success",        true);
        out.put("session_id",     sessionId);
        out.put("participant_id", participantId);
        out.put("muted",          mute);
        out.put("track",          track);
        out.put("message", (mute ? "Muted" : "Unmuted") + " " + track + " for " + participantId);
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 15. jitsi_force_end
// ─────────────────────────────────────────────────────────────────────────────
class JitsiForceEndTool extends BaseTool {

    JitsiForceEndTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "jitsi_force_end"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",   apiKeyProp());
        p.set("session_id",sessionIdProp());
        p.set("reason",    strProp("Reason for force-ending the Jitsi room (for audit)"));
        p.set("confirm",   strProp("Must be 'CONFIRM'"));
        return buildSchema(name(),
            "Force-end the Jitsi WebRTC room, disconnecting all participants immediately. " +
            "Emergency action — use when session must stop regardless of participant state. " +
            "Also transitions session to COMPLETED and publishes session.terminated event.",
            p, "session_id", "reason", "confirm");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId = validateSessionId(requireString(args, "session_id"));
        String reason    = requireString(args, "reason");
        String confirm   = requireString(args, "confirm");

        if (!"CONFIRM".equals(confirm)) {
            return ToolResult.error("Safety guard: set confirm=CONFIRM to force-end a Jitsi room");
        }

        ObjectNode body = json.createObjectNode();
        body.put("reason", reason);

        ServiceResponse r = client.forceEndJitsi(sessionId, json.writeValueAsString(body));
        if (r.isForbidden()) return ToolResult.error("Force-end denied — requires MODERATOR role");
        if (!r.isSuccess())  return serviceError(r.statusCode(), r.body(), "jitsi_force_end");

        ObjectNode out = json.createObjectNode();
        out.put("success",    true);
        out.put("session_id", sessionId);
        out.put("reason",     reason);
        out.put("message",    "Jitsi room force-ended — all participants disconnected");
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// AUDIT & COMPLIANCE
// ═════════════════════════════════════════════════════════════════════════════

// ─────────────────────────────────────────────────────────────────────────────
// 16. session_audit_log
// ─────────────────────────────────────────────────────────────────────────────
class SessionAuditLogTool extends BaseTool {

    SessionAuditLogTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "session_audit_log"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",    apiKeyProp());
        p.set("session_id", sessionIdProp());
        p.set("limit",      intProp("Max log entries (default: 50, max: 500)"));
        p.set("offset",     intProp("Pagination offset"));
        p.set("event_type", strProp("Filter by event type (e.g. STATE_CHANGED, PARTICIPANT_JOINED)"));
        return buildSchema(name(),
            "Retrieve the immutable audit log for a session. Captures all state changes, " +
            "participant actions, admin operations, and system events. " +
            "Supports GDPR, SOC 2, and HIPAA compliance requirements.",
            p, "session_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId = validateSessionId(requireString(args, "session_id"));
        int    limit     = Math.min(optInt(args, "limit", 50), 500);
        int    offset    = Math.max(optInt(args, "offset", 0), 0);
        String evType    = optString(args, "event_type", "");

        String qs = "limit=" + limit + "&offset=" + offset;
        if (!evType.isEmpty()) qs += "&eventType=" + evType;

        ServiceResponse r = client.getAuditLog(sessionId, qs);
        if (r.isNotFound()) return ToolResult.error("Session not found: " + sessionId);
        if (!r.isSuccess()) return serviceError(r.statusCode(), r.body(), "session_audit_log");

        JsonNode entries = r.body().has("entries") ? r.body().get("entries") : r.body();
        int total = r.body().path("total").asInt(entries.isArray() ? entries.size() : 0);

        ObjectNode out = json.createObjectNode();
        out.put("session_id", sessionId);
        out.put("total",      total);
        out.put("limit",      limit);
        out.put("offset",     offset);
        out.set("entries",    entries);
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 17. session_events — get Kafka events published for a session
// ─────────────────────────────────────────────────────────────────────────────
class SessionEventsTool extends BaseTool {

    SessionEventsTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "session_events"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",    apiKeyProp());
        p.set("session_id", sessionIdProp());
        return buildSchema(name(),
            "Get the ordered list of Kafka events published for a session: " +
            "session.created, session.started, session.participant_joined, " +
            "session.participant_left, session.completed, session.terminated_due_to_timeout. " +
            "Used for debugging, replay, and downstream service verification.",
            p, "session_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId = validateSessionId(requireString(args, "session_id"));
        ServiceResponse r = client.getSessionEvents(sessionId);

        if (r.isNotFound()) return ToolResult.error("Session not found: " + sessionId);
        if (!r.isSuccess()) return serviceError(r.statusCode(), r.body(), "session_events");

        JsonNode events = r.body().has("events") ? r.body().get("events") : r.body();
        int count = events.isArray() ? events.size() : 0;

        ObjectNode out = json.createObjectNode();
        out.put("session_id",  sessionId);
        out.put("event_count", count);
        out.set("events",      events);
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 18. session_export — full session export for compliance / archival
// ─────────────────────────────────────────────────────────────────────────────
class SessionExportTool extends BaseTool {

    SessionExportTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "session_export"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",    apiKeyProp());
        p.set("session_id", sessionIdProp());
        p.set("include_audit_log", boolProp("Include full audit log (default: true)"));
        p.set("include_events",    boolProp("Include Kafka event history (default: true)"));
        return buildSchema(name(),
            "Export complete session data including metadata, participant list, audit log, " +
            "and event history. Used for compliance reporting, dispute resolution, " +
            "and GDPR data subject access requests.",
            p, "session_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String  sessionId      = validateSessionId(requireString(args, "session_id"));
        boolean includeAudit   = optBool(args, "include_audit_log", true);
        boolean includeEvents  = optBool(args, "include_events", true);

        // Fetch session
        ServiceResponse sessionR = client.getSession(sessionId);
        if (!sessionR.isSuccess()) return serviceError(sessionR.statusCode(), sessionR.body(), "session_export");

        // Fetch participants
        ServiceResponse partR = client.listParticipants(sessionId);

        ObjectNode out = json.createObjectNode();
        out.put("session_id",    sessionId);
        out.put("exported_at",   java.time.Instant.now().toString());
        out.set("session",       sessionR.body());
        out.set("participants",  partR.isSuccess() ? partR.body() : json.createObjectNode());

        if (includeAudit) {
            ServiceResponse auditR = client.getAuditLog(sessionId, "limit=500&offset=0");
            out.set("audit_log", auditR.isSuccess() ? auditR.body() : json.createObjectNode());
        }
        if (includeEvents) {
            ServiceResponse evR = client.getSessionEvents(sessionId);
            out.set("events", evR.isSuccess() ? evR.body() : json.createObjectNode());
        }

        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// STATE MACHINE SHORTCUTS
// ═════════════════════════════════════════════════════════════════════════════

// ─────────────────────────────────────────────────────────────────────────────
// 19. session_start — WAITING_FOR_PARTICIPANTS → IN_PROGRESS
// ─────────────────────────────────────────────────────────────────────────────
class SessionStartTool extends BaseTool {

    SessionStartTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "session_start"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",         apiKeyProp());
        p.set("session_id",      sessionIdProp());
        p.set("version",         intProp("Session version for optimistic locking"));
        p.set("idempotency_key", idempotencyKeyProp());
        return buildSchema(name(),
            "Start an assessment session: transitions WAITING_FOR_PARTICIPANTS → IN_PROGRESS. " +
            "Publishes session.started Kafka event. Participants can now fully interact.",
            p, "session_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId = validateSessionId(requireString(args, "session_id"));
        int    version   = optInt(args, "version", -1);
        String idempKey  = optString(args, "idempotency_key", "");

        ObjectNode body = json.createObjectNode();
        body.put("state", "IN_PROGRESS");
        body.put("reason", "Session started by admin/assessor");
        if (version >= 0)      body.put("version", version);
        if (!idempKey.isEmpty()) body.put("idempotencyKey", idempKey);

        ServiceResponse r = client.updateSessionState(sessionId, json.writeValueAsString(body));
        if (r.isConflict())  return ToolResult.error("Conflict: session is not in WAITING_FOR_PARTICIPANTS state or version mismatch");
        if (!r.isSuccess())  return serviceError(r.statusCode(), r.body(), "session_start");

        ObjectNode out = json.createObjectNode();
        out.put("success",    true);
        out.put("session_id", sessionId);
        out.put("state",      "IN_PROGRESS");
        out.put("started_at", r.body().path("startedAt").asText(""));
        out.put("message",    "Session is now IN_PROGRESS — participants can interact");
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 20. session_suspend
// ─────────────────────────────────────────────────────────────────────────────
class SessionSuspendTool extends BaseTool {

    SessionSuspendTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "session_suspend"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",    apiKeyProp());
        p.set("session_id", sessionIdProp());
        p.set("reason",     strProp("Reason for suspension (shown to participants)"));
        p.set("version",    intProp("Session version for optimistic locking"));
        return buildSchema(name(),
            "Suspend an IN_PROGRESS session (emergency pause). Transitions to SUSPENDED state. " +
            "Timer pauses. Participants remain connected but cannot interact. " +
            "Use session_resume to continue.",
            p, "session_id", "reason");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId = validateSessionId(requireString(args, "session_id"));
        String reason    = requireString(args, "reason");
        int    version   = optInt(args, "version", -1);

        ObjectNode body = json.createObjectNode();
        body.put("state",  "SUSPENDED");
        body.put("reason", reason);
        if (version >= 0) body.put("version", version);

        ServiceResponse r = client.updateSessionState(sessionId, json.writeValueAsString(body));
        if (!r.isSuccess()) return serviceError(r.statusCode(), r.body(), "session_suspend");

        ObjectNode out = json.createObjectNode();
        out.put("success",      true);
        out.put("session_id",   sessionId);
        out.put("state",        "SUSPENDED");
        out.put("reason",       reason);
        out.put("message",      "Session suspended — use session_resume to continue");
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 21. session_resume
// ─────────────────────────────────────────────────────────────────────────────
class SessionResumeTool extends BaseTool {

    SessionResumeTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "session_resume"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",    apiKeyProp());
        p.set("session_id", sessionIdProp());
        p.set("reason",     strProp("Reason for resuming (optional)"));
        p.set("version",    intProp("Session version for optimistic locking"));
        return buildSchema(name(),
            "Resume a SUSPENDED session → IN_PROGRESS. Timer resumes from where it paused.",
            p, "session_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId = validateSessionId(requireString(args, "session_id"));
        String reason    = optString(args, "reason", "Session resumed by admin");
        int    version   = optInt(args, "version", -1);

        ObjectNode body = json.createObjectNode();
        body.put("state",  "IN_PROGRESS");
        body.put("reason", reason);
        if (version >= 0) body.put("version", version);

        ServiceResponse r = client.updateSessionState(sessionId, json.writeValueAsString(body));
        if (!r.isSuccess()) return serviceError(r.statusCode(), r.body(), "session_resume");

        ObjectNode out = json.createObjectNode();
        out.put("success",    true);
        out.put("session_id", sessionId);
        out.put("state",      "IN_PROGRESS");
        out.put("message",    "Session resumed — IN_PROGRESS");
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 22. session_terminate
// ─────────────────────────────────────────────────────────────────────────────
class SessionTerminateTool extends BaseTool {

    SessionTerminateTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "session_terminate"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",    apiKeyProp());
        p.set("session_id", sessionIdProp());
        p.set("reason",     strProp("Reason for termination (required for audit)"));
        p.set("confirm",    strProp("Must be 'CONFIRM'"));
        return buildSchema(name(),
            "Terminate any active session immediately → COMPLETED state. " +
            "Disconnects all participants, destroys Jitsi room, publishes session.completed, " +
            "and triggers analytics pipeline. Use when session must end regardless of status.",
            p, "session_id", "reason", "confirm");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId = validateSessionId(requireString(args, "session_id"));
        String reason    = requireString(args, "reason");
        String confirm   = requireString(args, "confirm");

        if (!"CONFIRM".equals(confirm)) {
            return ToolResult.error("Safety guard: set confirm=CONFIRM to terminate a session");
        }

        ObjectNode body = json.createObjectNode();
        body.put("state",  "COMPLETED");
        body.put("reason", reason);
        body.put("force",  true);

        ServiceResponse r = client.updateSessionState(sessionId, json.writeValueAsString(body));
        if (!r.isSuccess()) return serviceError(r.statusCode(), r.body(), "session_terminate");

        ObjectNode out = json.createObjectNode();
        out.put("success",       true);
        out.put("session_id",    sessionId);
        out.put("state",         "COMPLETED");
        out.put("reason",        reason);
        out.put("completed_at",  r.body().path("completedAt").asText(""));
        out.put("message", "Session terminated → COMPLETED. Analytics pipeline triggered.");
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// HEALTH & MONITORING
// ═════════════════════════════════════════════════════════════════════════════

// ─────────────────────────────────────────────────────────────────────────────
// 23. service_health
// ─────────────────────────────────────────────────────────────────────────────
class ServiceHealthTool extends BaseTool {

    ServiceHealthTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "service_health"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key", apiKeyProp());
        return buildSchema(name(),
            "Check Session-Control-Service liveness and readiness probes. " +
            "Returns service version, database status, Redis connectivity, and Kafka status. " +
            "Maps to /health/live and /health/ready Kubernetes probes.",
            p);
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        long start = System.currentTimeMillis();
        ServiceResponse liveR  = client.healthLive();
        long latency = System.currentTimeMillis() - start;
        ServiceResponse readyR = client.healthReady();

        ObjectNode out = json.createObjectNode();
        out.put("service_url",     cfg.baseUrl);
        out.put("latency_ms",      latency);
        out.put("live",            liveR.isSuccess());
        out.put("ready",           readyR.isSuccess());
        out.put("healthy",         liveR.isSuccess() && readyR.isSuccess());

        if (liveR.isSuccess()) {
            out.set("live_details",  liveR.body());
        } else {
            out.put("live_error",    "HTTP " + liveR.statusCode());
        }

        if (readyR.isSuccess()) {
            out.set("ready_details", readyR.body());
        } else {
            out.put("ready_error",   "HTTP " + readyR.statusCode() + " — service not ready");
        }

        out.put("latency_alert",   latency > 100);
        out.put("message", (liveR.isSuccess() && readyR.isSuccess())
                ? "Service healthy — " + latency + "ms"
                : "Service unhealthy — check live/ready status");
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 24. session_stats
// ─────────────────────────────────────────────────────────────────────────────
class SessionStatsTool extends BaseTool {

    SessionStatsTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "session_stats"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",   apiKeyProp());
        p.set("tenant_id", tenantIdProp());
        return buildSchema(name(),
            "Get real-time statistics: active sessions, participant counts by state, " +
            "session types distribution, and avg duration. " +
            "Used for recruiter dashboards and capacity planning.",
            p);
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String tenantId = resolveTenantId(args);
        ServiceResponse r = client.getStats(tenantId);

        if (!r.isSuccess()) return serviceError(r.statusCode(), r.body(), "session_stats");

        ObjectNode out = json.createObjectNode();
        out.put("tenant_id",          tenantId);
        out.put("active_sessions",    r.body().path("activeSessions").asInt(0));
        out.put("total_participants", r.body().path("totalParticipants").asInt(0));
        out.set("by_state",           r.body().path("byState"));
        out.set("by_type",            r.body().path("byType"));
        out.put("avg_duration_secs",  r.body().path("avgDurationSecs").asInt(0));
        out.put("sessions_today",     r.body().path("sessionsToday").asInt(0));
        out.set("raw",                r.body());
        return ToolResult.ok(json.writeValueAsString(out));
    }
}
