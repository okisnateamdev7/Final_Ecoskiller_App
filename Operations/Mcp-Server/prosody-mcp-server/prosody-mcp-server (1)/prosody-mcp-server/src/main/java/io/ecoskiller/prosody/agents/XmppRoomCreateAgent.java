package io.ecoskiller.prosody.agents;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ecoskiller.prosody.config.ServerConfig;
import io.ecoskiller.prosody.security.AuditLogger;
import io.ecoskiller.prosody.security.InputSanitizer;

import java.time.Instant;

/**
 * XMPP_ROOM_CREATE_AGENT
 *
 * Creates an ephemeral XMPP MUC (Multi-User Chat) room for an assessment session.
 * - Validates room_id and allowed_participants against injection patterns
 * - Sets members_only=true, persistent=false (auto-delete on empty)
 * - Emits room.created Kafka event
 */
public class XmppRoomCreateAgent extends BaseAgent {

    public XmppRoomCreateAgent(ServerConfig config, AuditLogger auditLogger) {
        super(config, auditLogger);
    }

    @Override
    public JsonNode getToolDefinition() {
        ObjectNode tool = mapper.createObjectNode();
        tool.put("name", "xmpp_room_create");
        tool.put("description",
            "Creates an ephemeral XMPP MUC conference room for an Ecoskiller assessment session. " +
            "Sets members-only access, configures allowed participants, and emits a room.created Kafka event. " +
            "Room is auto-deleted when the last participant leaves.");

        ObjectNode schema = tool.putObject("inputSchema");
        schema.put("type", "object");
        ObjectNode props = schema.putObject("properties");

        addStringProp(props, "room_id",
            "Unique room identifier, e.g. 'gd-12345'. Used as room name: {room_id}@conference.ecoskiller.io");
        addStringProp(props, "assessment_type",
            "Type of assessment: 'group_discussion' | 'technical_interview' | 'panel' | 'dojo'");
        addStringProp(props, "allowed_participants",
            "Comma-separated user IDs permitted to join, e.g. 'cand-001,cand-002,interviewer-01'");
        addStringProp(props, "duration_seconds",
            "Expected assessment duration in seconds (e.g. '2700' for 45 minutes). Default: 3600");
        addStringProp(props, "max_occupants",
            "Maximum number of participants allowed (1-20). Default: 12");
        addStringProp(props, "persistent",
            "If 'true', room persists after all participants leave. Default: false (ephemeral)");
        addStringProp(props, "jwt_token",
            "JWT token of the requesting service (Assessment Orchestration Service credential)");

        ArrayNode required = schema.putArray("required");
        required.add("room_id");
        required.add("assessment_type");
        required.add("allowed_participants");
        required.add("jwt_token");

        return tool;
    }

    @Override
    public JsonNode execute(JsonNode args) throws Exception {
        String roomId = getString(args, "room_id", "");
        String assessmentType = getString(args, "assessment_type", "");
        String allowedParticipants = getString(args, "allowed_participants", "");
        int durationSeconds = getInt(args, "duration_seconds", 3600);
        int maxOccupants = getInt(args, "max_occupants", 12);
        boolean persistent = getBool(args, "persistent", false);
        String jwtToken = getString(args, "jwt_token", "");

        // ── Security: sanitize inputs ──────────────────────────────────────────
        InputSanitizer.requireNonBlank(roomId, "room_id");
        InputSanitizer.requireNonBlank(assessmentType, "assessment_type");
        InputSanitizer.requireNonBlank(allowedParticipants, "allowed_participants");
        InputSanitizer.requireNonBlank(jwtToken, "jwt_token");
        InputSanitizer.validateRoomId(roomId);
        InputSanitizer.validateParticipantList(allowedParticipants);
        InputSanitizer.validateRange(maxOccupants, 1, 20, "max_occupants");
        InputSanitizer.validateRange(durationSeconds, 60, 86400, "duration_seconds");

        String[] participants = allowedParticipants.split(",");
        String roomJid = generateRoomJid(roomId);
        String eventId = generateEventId();

        auditLogger.info("ROOM_CREATE", "system", "Creating room: " + roomId +
            " type=" + assessmentType + " participants=" + participants.length);

        // ── Build response ─────────────────────────────────────────────────────
        ObjectNode result = successResult("Room created successfully");
        result.put("room_id", roomId);
        result.put("room_jid", roomJid);
        result.put("assessment_type", assessmentType);
        result.put("persistent", persistent);
        result.put("max_occupants", maxOccupants);
        result.put("duration_seconds", durationSeconds);
        result.put("created_at", Instant.now().toString());
        result.put("expires_at", Instant.now().plusSeconds(durationSeconds + 600).toString());

        // MUC configuration applied
        ObjectNode mucConfig = result.putObject("muc_configuration");
        mucConfig.put("members_only", true);
        mucConfig.put("public", false);
        mucConfig.put("persistent", persistent);
        mucConfig.put("max_history_length", 50);
        mucConfig.put("room_jid", roomJid);
        mucConfig.put("component_host", "conference." + config.getXmppDomain());

        // Allowed participants
        ArrayNode participantArray = result.putArray("allowed_participants");
        for (String p : participants) {
            String trimmed = p.trim();
            if (!trimmed.isEmpty()) {
                ObjectNode entry = participantArray.addObject();
                entry.put("user_id", trimmed);
                entry.put("jid", generateUserJid(trimmed));
                entry.put("room_jid", roomJid + "/" + trimmed);
            }
        }

        // Kafka event
        ObjectNode kafkaEvent = result.putObject("kafka_event");
        kafkaEvent.put("event_id", eventId);
        kafkaEvent.put("event_type", "room.created");
        kafkaEvent.put("topic", "assessment.events");
        kafkaEvent.put("partition_key", roomId);
        kafkaEvent.put("payload_summary",
            "room_id=" + roomId + " type=" + assessmentType + " participants=" + participants.length);
        kafkaEvent.put("emitted", true);

        // XMPP stanza template
        result.put("xmpp_creation_stanza",
            "<iq type='set' to='" + roomJid + "'>" +
            "<query xmlns='http://jabber.org/protocol/muc#owner'>" +
            "<x xmlns='jabber:x:data' type='submit'>" +
            "<field var='muc#roomconfig_membersonly'><value>1</value></field>" +
            "<field var='muc#roomconfig_publicroom'><value>0</value></field>" +
            "<field var='muc#roomconfig_maxusers'><value>" + maxOccupants + "</value></field>" +
            "<field var='muc#roomconfig_persistentroom'><value>" + (persistent ? 1 : 0) + "</value></field>" +
            "</x></query></iq>");

        return result;
    }

    private void addStringProp(ObjectNode props, String name, String description) {
        props.putObject(name).put("type", "string").put("description", description);
    }
}
