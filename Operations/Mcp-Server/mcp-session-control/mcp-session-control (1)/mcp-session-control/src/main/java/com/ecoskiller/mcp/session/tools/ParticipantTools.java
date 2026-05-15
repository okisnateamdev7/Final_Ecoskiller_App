package com.ecoskiller.mcp.session.tools;

import com.ecoskiller.mcp.session.client.SessionControlClient;
import com.ecoskiller.mcp.session.client.SessionControlClient.ServiceResponse;
import com.ecoskiller.mcp.session.config.SessionControlConfig;
import com.ecoskiller.mcp.session.security.AuditLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Participant Management Tools (6)
 *
 *  7.  participant_join          — Participant join request with JWT validation
 *  8.  participant_leave         — Participant voluntary leave / disconnect
 *  9.  participant_list          — List all active participants in a session
 *  10. participant_heartbeat     — Update presence heartbeat (prevents timeout eviction)
 *  11. participant_evict         — Force-evict a participant (admin action)
 *  12. participant_update_role   — Change a participant's role (e.g. promote to ASSESSOR)
 */

// ─────────────────────────────────────────────────────────────────────────────
// 7. participant_join
// ─────────────────────────────────────────────────────────────────────────────
class ParticipantJoinTool extends BaseTool {

    ParticipantJoinTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "participant_join"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",         apiKeyProp());
        p.set("session_id",      sessionIdProp());
        p.set("participant_id",  participantIdProp());
        p.set("role",            enumProp("Participant role", "PARTICIPANT","ASSESSOR","OBSERVER","MODERATOR"));
        p.set("jwt_token",       strProp("JWT token issued by auth-service (RS256). Used for identity validation."));
        p.set("idempotency_key", idempotencyKeyProp());
        p.set("metadata",        strProp("Optional JSON metadata (device_type, location, etc.)"));
        return buildSchema(name(),
            "Submit a participant join request. Validates JWT (RS256 via Keycloak), " +
            "checks tenant membership, enforces max_participants limit, and verifies session is " +
            "WAITING_FOR_PARTICIPANTS or IN_PROGRESS. Publishes session.participant_joined Kafka event. " +
            "Returns Jitsi room credentials on success.",
            p, "session_id", "participant_id", "role", "jwt_token");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId     = validateSessionId(requireString(args, "session_id"));
        String participantId = validateParticipantId(requireString(args, "participant_id"));
        String role          = requireString(args, "role").toUpperCase();
        String jwtToken      = requireString(args, "jwt_token");
        String idempKey      = optString(args, "idempotency_key", "");
        String metadata      = optString(args, "metadata", "{}");

        if (!VALID_ROLES.contains(role)) {
            return ToolResult.error("Invalid role: " + role + ". Must be one of: " + VALID_ROLES);
        }
        if (jwtToken.length() < 20) {
            return ToolResult.error("jwt_token appears invalid — expected a full RS256 JWT");
        }

        ObjectNode body = json.createObjectNode();
        body.put("participantId",  participantId);
        body.put("role",           role);
        body.put("token",          jwtToken);
        if (!idempKey.isEmpty()) body.put("idempotencyKey", idempKey);
        try {
            body.set("metadata", json.readTree(metadata));
        } catch (Exception e) {
            body.put("metadata", metadata);
        }

        ServiceResponse r = client.joinSession(sessionId, json.writeValueAsString(body));

        if (r.isConflict())  return ToolResult.error("Participant already joined this session (idempotent)");
        if (r.isForbidden()) return ToolResult.error("Join denied — invalid JWT, wrong tenant, or insufficient role");
        if (!r.isSuccess())  return serviceError(r.statusCode(), r.body(), "participant_join");

        ObjectNode out = json.createObjectNode();
        out.put("success",         true);
        out.put("session_id",      sessionId);
        out.put("participant_id",  participantId);
        out.put("role",            role);
        out.put("joined_at",       r.body().path("joinedAt").asText(""));
        out.put("jitsi_token",     r.body().path("jitsiToken").asText(""));
        out.put("jitsi_room_id",   r.body().path("jitsiRoomId").asText(""));
        out.put("message", "Participant joined — use jitsi_token to connect to the room");
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 8. participant_leave
// ─────────────────────────────────────────────────────────────────────────────
class ParticipantLeaveTool extends BaseTool {

    ParticipantLeaveTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "participant_leave"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",         apiKeyProp());
        p.set("session_id",      sessionIdProp());
        p.set("participant_id",  participantIdProp());
        p.set("reason",          enumProp("Disconnect reason",
                                          "VOLUNTARY","NETWORK_ERROR","TIMEOUT","SESSION_ENDED"));
        p.set("idempotency_key", idempotencyKeyProp());
        return buildSchema(name(),
            "Record a participant leaving the session. Updates presence status, " +
            "records left_at timestamp, and publishes session.participant_left Kafka event. " +
            "Triggers cleanup of participant-specific resources.",
            p, "session_id", "participant_id", "reason");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId     = validateSessionId(requireString(args, "session_id"));
        String participantId = validateParticipantId(requireString(args, "participant_id"));
        String reason        = requireString(args, "reason").toUpperCase();
        String idempKey      = optString(args, "idempotency_key", "");

        if (!VALID_DISCONNECT_REASONS.contains(reason)) {
            return ToolResult.error("Invalid reason: " + reason + ". Must be one of: " + VALID_DISCONNECT_REASONS);
        }

        ObjectNode body = json.createObjectNode();
        body.put("reason", reason);
        if (!idempKey.isEmpty()) body.put("idempotencyKey", idempKey);

        ServiceResponse r = client.leaveSession(sessionId, participantId, json.writeValueAsString(body));
        if (!r.isSuccess()) return serviceError(r.statusCode(), r.body(), "participant_leave");

        ObjectNode out = json.createObjectNode();
        out.put("success",        true);
        out.put("session_id",     sessionId);
        out.put("participant_id", participantId);
        out.put("reason",         reason);
        out.put("left_at",        r.body().path("leftAt").asText(""));
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 9. participant_list
// ─────────────────────────────────────────────────────────────────────────────
class ParticipantListTool extends BaseTool {

    ParticipantListTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "participant_list"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",    apiKeyProp());
        p.set("session_id", sessionIdProp());
        return buildSchema(name(),
            "List all participants in a session with their current presence status, role, " +
            "join time, and last heartbeat. Used for real-time recruiter dashboards.",
            p, "session_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId = validateSessionId(requireString(args, "session_id"));
        ServiceResponse r = client.listParticipants(sessionId);

        if (r.isNotFound())  return ToolResult.error("Session not found: " + sessionId);
        if (!r.isSuccess())  return serviceError(r.statusCode(), r.body(), "participant_list");

        JsonNode participants = r.body().has("participants") ? r.body().get("participants") : r.body();
        int total = participants.isArray() ? participants.size() : 0;

        // Count by role
        int assessors = 0, observers = 0, activeP = 0;
        if (participants.isArray()) {
            for (JsonNode p : participants) {
                String role = p.path("role").asText("").toUpperCase();
                if ("ASSESSOR".equals(role))  assessors++;
                if ("OBSERVER".equals(role))  observers++;
                if (p.path("leftAt").asText("").isEmpty()) activeP++;
            }
        }

        ObjectNode out = json.createObjectNode();
        out.put("session_id",         sessionId);
        out.put("total_participants", total);
        out.put("active_count",       activeP);
        out.put("assessor_count",     assessors);
        out.put("observer_count",     observers);
        out.set("participants",       participants);
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 10. participant_heartbeat
// ─────────────────────────────────────────────────────────────────────────────
class ParticipantHeartbeatTool extends BaseTool {

    ParticipantHeartbeatTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "participant_heartbeat"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",        apiKeyProp());
        p.set("session_id",     sessionIdProp());
        p.set("participant_id", participantIdProp());
        return buildSchema(name(),
            "Update a participant's last_heartbeat timestamp to prevent automatic timeout eviction. " +
            "Must be called at least every " + cfg.heartbeatTimeoutSecs + " seconds (configurable via " +
            "SESSION_HEARTBEAT_TIMEOUT). If heartbeat is absent, participant is auto-evicted.",
            p, "session_id", "participant_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId     = validateSessionId(requireString(args, "session_id"));
        String participantId = validateParticipantId(requireString(args, "participant_id"));

        ServiceResponse r = client.heartbeat(sessionId, participantId);
        if (!r.isSuccess()) return serviceError(r.statusCode(), r.body(), "participant_heartbeat");

        ObjectNode out = json.createObjectNode();
        out.put("success",           true);
        out.put("session_id",        sessionId);
        out.put("participant_id",    participantId);
        out.put("heartbeat_at",      r.body().path("lastHeartbeat").asText(""));
        out.put("next_required_by_secs", cfg.heartbeatTimeoutSecs);
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 11. participant_evict — force-evict a participant (admin)
// ─────────────────────────────────────────────────────────────────────────────
class ParticipantEvictTool extends BaseTool {

    ParticipantEvictTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "participant_evict"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",        apiKeyProp());
        p.set("session_id",     sessionIdProp());
        p.set("participant_id", participantIdProp());
        p.set("reason",         strProp("Reason for eviction (required for audit trail)"));
        p.set("confirm",        strProp("Must be 'CONFIRM' — safety guard for admin eviction"));
        return buildSchema(name(),
            "Force-evict a participant from an active session. Admin/moderator action only. " +
            "Disconnects participant from Jitsi, publishes eviction event, and logs to audit trail. " +
            "Requires confirm=CONFIRM.",
            p, "session_id", "participant_id", "reason", "confirm");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId     = validateSessionId(requireString(args, "session_id"));
        String participantId = validateParticipantId(requireString(args, "participant_id"));
        String reason        = requireString(args, "reason");
        String confirm       = requireString(args, "confirm");

        if (!"CONFIRM".equals(confirm)) {
            return ToolResult.error("Safety guard: set confirm=CONFIRM to evict a participant");
        }

        ServiceResponse r = client.evictParticipant(sessionId, participantId);
        if (r.isForbidden()) return ToolResult.error("Eviction denied — insufficient role (requires ASSESSOR or MODERATOR)");
        if (!r.isSuccess())  return serviceError(r.statusCode(), r.body(), "participant_evict");

        ObjectNode out = json.createObjectNode();
        out.put("evicted",        true);
        out.put("session_id",     sessionId);
        out.put("participant_id", participantId);
        out.put("reason",         reason);
        out.put("message",        "Participant evicted — disconnected from Jitsi room");
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 12. participant_update_role
// ─────────────────────────────────────────────────────────────────────────────
class ParticipantUpdateRoleTool extends BaseTool {

    ParticipantUpdateRoleTool(SessionControlClient c, SessionControlConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "participant_update_role"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",        apiKeyProp());
        p.set("session_id",     sessionIdProp());
        p.set("participant_id", participantIdProp());
        p.set("new_role",       enumProp("New role to assign", "PARTICIPANT","ASSESSOR","OBSERVER","MODERATOR"));
        p.set("reason",         strProp("Reason for role change (for audit trail)"));
        return buildSchema(name(),
            "Change a participant's role mid-session. Example: promote a PARTICIPANT to ASSESSOR " +
            "to allow score viewing, or demote to OBSERVER. Publishes role_changed event.",
            p, "session_id", "participant_id", "new_role", "reason");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String sessionId     = validateSessionId(requireString(args, "session_id"));
        String participantId = validateParticipantId(requireString(args, "participant_id"));
        String newRole       = requireString(args, "new_role").toUpperCase();
        String reason        = requireString(args, "reason");

        if (!VALID_ROLES.contains(newRole)) {
            return ToolResult.error("Invalid new_role: " + newRole + ". Must be one of: " + VALID_ROLES);
        }

        ObjectNode body = json.createObjectNode();
        body.put("role",   newRole);
        body.put("reason", reason);

        ServiceResponse r = client.updateParticipantRole(sessionId, participantId, json.writeValueAsString(body));
        if (r.isForbidden()) return ToolResult.error("Role update denied — insufficient permissions");
        if (!r.isSuccess())  return serviceError(r.statusCode(), r.body(), "participant_update_role");

        ObjectNode out = json.createObjectNode();
        out.put("success",        true);
        out.put("session_id",     sessionId);
        out.put("participant_id", participantId);
        out.put("new_role",       newRole);
        out.put("reason",         reason);
        out.put("message",        "Role updated to " + newRole);
        return ToolResult.ok(json.writeValueAsString(out));
    }
}
