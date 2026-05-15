package com.ecoskiller.mcp.session.tools;

import com.ecoskiller.mcp.session.client.SessionControlClient;
import com.ecoskiller.mcp.session.client.SessionControlClient.ServiceResponse;
import com.ecoskiller.mcp.session.config.SessionControlConfig;
import com.ecoskiller.mcp.session.security.AuditLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Session Lifecycle Tools (6)
 *
 *  1. session_create        — Provision a new assessment session
 *  2. session_get           — Retrieve session metadata and current state
 *  3. session_list          — List sessions (filterable by tenant, state, type)
 *  4. session_update_state  — Admin state transitions (SUSPEND, RESUME, TERMINATE)
 *  5. session_extend        — Extend session expiry time
 *  6. session_archive       — Archive a completed session to cold storage
 */

// ─────────────────────────────────────────────────────────────────────────────
// 1. session_create
// ─────────────────────────────────────────────────────────────────────────────
class SessionCreateTool extends BaseTool {

    SessionCreateTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "session_create"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",          apiKeyProp());
        p.set("assessment_id",    strProp("UUID of the assessment configuration to run"));
        p.set("assessment_type",  enumProp("Type of assessment session",
                                           "GROUP_DISCUSSION", "INTERVIEW", "CODING_CHALLENGE"));
        p.set("participant_ids",  strProp("Comma-separated list of participant UUIDs to invite"));
        p.set("duration_secs",    intProp("Session duration in seconds (min: 60, max: " + cfg.maxDurationSecs + ")"));
        p.set("max_participants", intProp("Maximum concurrent participants allowed (default: 8)"));
        p.set("tenant_id",        tenantIdProp());
        p.set("idempotency_key",  idempotencyKeyProp());
        p.set("metadata",         strProp("Optional JSON string of custom metadata (difficulty, job_id, etc.)"));
        return buildSchema(name(),
            "Provision a new assessment session. Allocates a Jitsi room, generates access tokens, " +
            "validates participant list, and publishes session.created Kafka event. " +
            "State machine starts at CREATED → WAITING_FOR_PARTICIPANTS.",
            p, "assessment_id", "assessment_type", "participant_ids", "duration_secs");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String assessmentId   = requireString(args, "assessment_id");
        String type           = requireString(args, "assessment_type").toUpperCase();
        String participantIds = requireString(args, "participant_ids");
        int    duration       = validateDuration(optInt(args, "duration_secs", 3600));
        int    maxP           = optInt(args, "max_participants", 8);
        String tenantId       = resolveTenantId(args);
        String idempKey       = optString(args, "idempotency_key", "");
        String metadata       = optString(args, "metadata", "{}");

        if (!VALID_TYPES.contains(type)) {
            return ToolResult.error("Invalid assessment_type: " + type + ". Must be one of: " + VALID_TYPES);
        }
        if (maxP < 1 || maxP > 50) {
            return ToolResult.error("max_participants must be between 1 and 50");
        }

        // Build participant ID array
        String[] pids = participantIds.split(",");
        if (pids.length == 0) return ToolResult.error("participant_ids must contain at least one UUID");

        ObjectNode body = json.createObjectNode();
        body.put("assessmentId",   assessmentId);
        body.put("assessmentType", type);
        body.put("duration",       duration);
        body.put("maxParticipants",maxP);
        if (!tenantId.isEmpty())  body.put("tenantId", tenantId);
        if (!idempKey.isEmpty())  body.put("idempotencyKey", idempKey);

        com.fasterxml.jackson.databind.node.ArrayNode pidArray = json.createArrayNode();
        for (String pid : pids) { String t = pid.strip(); if (!t.isEmpty()) pidArray.add(t); }
        body.set("participantIds", pidArray);

        try {
            JsonNode meta = json.readTree(metadata);
            body.set("metadata", meta);
        } catch (Exception e) {
            body.put("metadata", metadata); // store as string if invalid JSON
        }

        ServiceResponse r = client.createSession(json.writeValueAsString(body));
        if (!r.isSuccess()) return serviceError(r.statusCode(), r.body(), "session_create");

        ObjectNode out = json.createObjectNode();
        out.put("success",          true);
        out.put("session_id",       r.body().path("sessionId").asText(""));
        out.put("state",            r.body().path("state").asText("CREATED"));
        out.put("jitsi_room_id",    r.body().path("jitsiRoomId").asText(""));
        out.put("created_at",       r.body().path("createdAt").asText(""));
        out.put("expires_at",       r.body().path("expiresAt").asText(""));
        out.put("assessment_type",  type);
        out.put("duration_secs",    duration);
        out.put("max_participants", maxP);
        out.put("message", "Session created — waiting for participants to join");
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 2. session_get
// ─────────────────────────────────────────────────────────────────────────────
class SessionGetTool extends BaseTool {

    SessionGetTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "session_get"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",    apiKeyProp());
        p.set("session_id", sessionIdProp());
        return buildSchema(name(),
            "Retrieve full session metadata including current state, active participant count, " +
            "timestamps, Jitsi room ID, and custom metadata.",
            p, "session_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId = validateSessionId(requireString(args, "session_id"));
        ServiceResponse r = client.getSession(sessionId);

        if (r.isNotFound()) return ToolResult.error("Session not found: " + sessionId);
        if (r.isForbidden()) return ToolResult.error("Access denied — check tenant_id or permissions");
        if (!r.isSuccess()) return serviceError(r.statusCode(), r.body(), "session_get");

        ObjectNode out = json.createObjectNode();
        out.put("session_id",          r.body().path("sessionId").asText(sessionId));
        out.put("state",               r.body().path("state").asText(""));
        out.put("assessment_type",     r.body().path("assessmentType").asText(""));
        out.put("active_participants", r.body().path("activeParticipants").asInt(0));
        out.put("max_participants",    r.body().path("maxParticipants").asInt(0));
        out.put("created_at",          r.body().path("createdAt").asText(""));
        out.put("started_at",          r.body().path("startedAt").asText(""));
        out.put("expires_at",          r.body().path("expiresAt").asText(""));
        out.put("completed_at",        r.body().path("completedAt").asText(""));
        out.put("jitsi_room_id",       r.body().path("jitsiRoomId").asText(""));
        out.put("version",             r.body().path("version").asInt(0));
        out.set("metadata",            r.body().path("metadata"));
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 3. session_list
// ─────────────────────────────────────────────────────────────────────────────
class SessionListTool extends BaseTool {

    SessionListTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "session_list"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",         apiKeyProp());
        p.set("tenant_id",       tenantIdProp());
        p.set("state",           enumProp("Filter by state",
                                          "CREATED","WAITING_FOR_PARTICIPANTS","IN_PROGRESS",
                                          "SUSPENDED","COMPLETED","ARCHIVED","ALL"));
        p.set("assessment_type", enumProp("Filter by type", "GROUP_DISCUSSION","INTERVIEW","CODING_CHALLENGE","ALL"));
        p.set("limit",           intProp("Max sessions to return (default: 20, max: 100)"));
        p.set("offset",          intProp("Pagination offset (default: 0)"));
        return buildSchema(name(),
            "List assessment sessions for a tenant with optional state and type filters. " +
            "Returns session summaries for recruiter dashboards and admin views.",
            p);
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String tenantId = resolveTenantId(args);
        String state    = optString(args, "state", "ALL");
        String type     = optString(args, "assessment_type", "ALL");
        int    limit    = Math.min(optInt(args, "limit", 20), 100);
        int    offset   = Math.max(optInt(args, "offset", 0), 0);

        List<String> qs = new ArrayList<>();
        qs.add("limit=" + limit);
        qs.add("offset=" + offset);
        if (!tenantId.isEmpty())        qs.add("tenantId=" + tenantId);
        if (!"ALL".equals(state))       qs.add("state=" + state);
        if (!"ALL".equals(type))        qs.add("assessmentType=" + type);

        ServiceResponse r = client.listSessions(String.join("&", qs));
        if (!r.isSuccess()) return serviceError(r.statusCode(), r.body(), "session_list");

        JsonNode sessions = r.body().has("sessions") ? r.body().get("sessions") : r.body();
        int total = r.body().path("total").asInt(sessions.isArray() ? sessions.size() : 0);

        ObjectNode out = json.createObjectNode();
        out.put("total",   total);
        out.put("limit",   limit);
        out.put("offset",  offset);
        out.set("sessions", sessions);
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 4. session_update_state — admin state transitions
// ─────────────────────────────────────────────────────────────────────────────
class SessionUpdateStateTool extends BaseTool {

    // Valid FSM transitions (from spec section 3)
    private static final java.util.Map<String, java.util.Set<String>> TRANSITIONS = java.util.Map.of(
        "CREATED",                  java.util.Set.of("WAITING_FOR_PARTICIPANTS", "COMPLETED"),
        "WAITING_FOR_PARTICIPANTS", java.util.Set.of("IN_PROGRESS", "COMPLETED"),
        "IN_PROGRESS",              java.util.Set.of("SUSPENDED", "COMPLETED"),
        "SUSPENDED",                java.util.Set.of("IN_PROGRESS", "COMPLETED"),
        "COMPLETED",                java.util.Set.of("ARCHIVED")
    );

    SessionUpdateStateTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "session_update_state"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",         apiKeyProp());
        p.set("session_id",      sessionIdProp());
        p.set("target_state",    enumProp("Target state for FSM transition",
                                          "WAITING_FOR_PARTICIPANTS","IN_PROGRESS",
                                          "SUSPENDED","COMPLETED","ARCHIVED"));
        p.set("reason",          strProp("Reason for state change (required for audit trail)"));
        p.set("version",         intProp("Current session version for optimistic locking (prevents race conditions)"));
        p.set("idempotency_key", idempotencyKeyProp());
        return buildSchema(name(),
            "Perform an admin state transition on a session FSM. " +
            "Valid transitions: CREATED→WAITING, WAITING→IN_PROGRESS, IN_PROGRESS→SUSPENDED, " +
            "IN_PROGRESS→COMPLETED, SUSPENDED→IN_PROGRESS, COMPLETED→ARCHIVED. " +
            "Optimistic locking via 'version' prevents concurrent update conflicts.",
            p, "session_id", "target_state", "reason");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId   = validateSessionId(requireString(args, "session_id"));
        String targetState = requireString(args, "target_state").toUpperCase();
        String reason      = requireString(args, "reason");
        int    version     = optInt(args, "version", -1);
        String idempKey    = optString(args, "idempotency_key", "");

        if (!VALID_STATES.contains(targetState)) {
            return ToolResult.error("Invalid target_state: " + targetState);
        }

        ObjectNode body = json.createObjectNode();
        body.put("state",  targetState);
        body.put("reason", reason);
        if (version >= 0)      body.put("version", version);
        if (!idempKey.isEmpty()) body.put("idempotencyKey", idempKey);

        ServiceResponse r = client.updateSessionState(sessionId, json.writeValueAsString(body));

        if (r.isConflict()) {
            return ToolResult.error("Conflict: concurrent state change or version mismatch. " +
                                    "Fetch current session version with session_get and retry.");
        }
        if (r.isForbidden()) return ToolResult.error("Access denied — insufficient role for state transition");
        if (!r.isSuccess()) return serviceError(r.statusCode(), r.body(), "session_update_state");

        ObjectNode out = json.createObjectNode();
        out.put("success",      true);
        out.put("session_id",   sessionId);
        out.put("new_state",    targetState);
        out.put("reason",       reason);
        out.put("version",      r.body().path("version").asInt(version + 1));
        out.put("message",      "Session state transitioned to " + targetState);
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 5. session_extend — extend session expiry time
// ─────────────────────────────────────────────────────────────────────────────
class SessionExtendTool extends BaseTool {

    SessionExtendTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "session_extend"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",         apiKeyProp());
        p.set("session_id",      sessionIdProp());
        p.set("extend_by_secs",  intProp("Seconds to add to the current expiry time (max: 3600)"));
        p.set("reason",          strProp("Reason for extension (for audit trail)"));
        p.set("idempotency_key", idempotencyKeyProp());
        return buildSchema(name(),
            "Extend a session's expiry time. Use when assessors need more time. " +
            "Maximum single extension is 3600s (1 hour). Multiple extensions are allowed. " +
            "Publishes session.extended Kafka event.",
            p, "session_id", "extend_by_secs", "reason");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId   = validateSessionId(requireString(args, "session_id"));
        int    extendBy    = optInt(args, "extend_by_secs", 1800);
        String reason      = requireString(args, "reason");
        String idempKey    = optString(args, "idempotency_key", "");

        if (extendBy < 60 || extendBy > 3600) {
            return ToolResult.error("extend_by_secs must be between 60 and 3600 seconds");
        }

        ObjectNode body = json.createObjectNode();
        body.put("action",      "EXTEND");
        body.put("extendBySecs", extendBy);
        body.put("reason",       reason);
        if (!idempKey.isEmpty()) body.put("idempotencyKey", idempKey);

        ServiceResponse r = client.updateSessionState(sessionId, json.writeValueAsString(body));
        if (!r.isSuccess()) return serviceError(r.statusCode(), r.body(), "session_extend");

        ObjectNode out = json.createObjectNode();
        out.put("success",        true);
        out.put("session_id",     sessionId);
        out.put("extended_by_secs", extendBy);
        out.put("new_expires_at", r.body().path("expiresAt").asText(""));
        out.put("reason",         reason);
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 6. session_archive
// ─────────────────────────────────────────────────────────────────────────────
class SessionArchiveTool extends BaseTool {

    SessionArchiveTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "session_archive"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",   apiKeyProp());
        p.set("session_id",sessionIdProp());
        p.set("confirm",   strProp("Must be 'CONFIRM' — safety guard"));
        return buildSchema(name(),
            "Archive a COMPLETED session to cold storage. " +
            "Triggers Kafka event for analytics pipeline and removes from active cache. " +
            "Session data remains accessible via audit log. Requires confirm=CONFIRM.",
            p, "session_id", "confirm");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId = validateSessionId(requireString(args, "session_id"));
        String confirm   = requireString(args, "confirm");

        if (!"CONFIRM".equals(confirm)) {
            return ToolResult.error("Safety guard: set confirm=CONFIRM to archive a session");
        }

        ServiceResponse r = client.deleteSession(sessionId);
        if (!r.isSuccess()) return serviceError(r.statusCode(), r.body(), "session_archive");

        ObjectNode out = json.createObjectNode();
        out.put("archived",   true);
        out.put("session_id", sessionId);
        out.put("message",    "Session archived to cold storage — data retained in audit log");
        return ToolResult.ok(json.writeValueAsString(out));
    }
}
