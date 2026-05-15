package com.ecoskiller.mcp.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

// ============================================================
// GET MESSAGE BUFFER
// ============================================================
public class GetMessageBufferTool extends BaseTool {
    @Override public JsonNode getSchema() {
        ObjectNode s = mapper.createObjectNode(); s.put("type","object");
        ObjectNode p = s.putObject("properties");
        prop(p,"client_id",    "string","Client UUID whose buffer to inspect");
        prop(p,"from_msg_id",  "string","Optional: retrieve messages after this message ID");
        prop(p,"limit",        "integer","Max messages to return (default 20, max 100)");
        s.putArray("required").add("client_id");
        return buildSchema("get_message_buffer",
            "Inspect the in-memory ring buffer for a WebSocket client. " +
            "Buffer holds last 100 messages (~100KB per client). " +
            "Used on reconnect to check what needs replaying. " +
            "Buffer is discarded if client is disconnected >5 minutes (stale events irrelevant).", s);
    }
    @Override public JsonNode execute(JsonNode args) {
        String clientId  = requireString(args, "client_id");
        String fromMsgId = optString(args, "from_msg_id", null);
        int    limit     = Math.min(optInt(args, "limit", 20), 100);

        ObjectNode r = ok("Message buffer retrieved");
        r.put("client_id",         clientId);
        r.put("buffer_capacity",   100);
        r.put("buffer_used",       42);
        r.put("oldest_message_id", "msg-001");
        r.put("newest_message_id", "msg-042");
        r.put("from_msg_id_filter", fromMsgId != null ? fromMsgId : "none");
        r.put("messages_returned", Math.min(limit, 42));
        r.put("compression",       "gzip");
        r.put("estimated_size_kb", 38.4);

        ArrayNode sample = r.putArray("sample_messages");
        addMsg(sample, "msg-040", "assessment.score.updated",   Instant.now().minusSeconds(30).toString());
        addMsg(sample, "msg-041", "gd.speaker.changed",         Instant.now().minusSeconds(20).toString());
        addMsg(sample, "msg-042", "timer.tick",                 Instant.now().minusSeconds(10).toString());
        return r;
    }
    private void addMsg(ArrayNode arr, String id, String type, String ts) {
        ObjectNode m = arr.addObject();
        m.put("message_id", id); m.put("event_type", type); m.put("timestamp", ts);
    }
}

// ============================================================
// REPLAY MISSED MESSAGES
// ============================================================
class ReplayMissedMessagesTool extends BaseTool {
    @Override public JsonNode getSchema() {
        ObjectNode s = mapper.createObjectNode(); s.put("type","object");
        ObjectNode p = s.putObject("properties");
        prop(p,"client_id",     "string","Reconnecting client UUID");
        prop(p,"from_msg_id",   "string","Replay messages after this ID (client's last acked msg)");
        prop(p,"assessment_id", "string","Assessment context for scoped replay");
        s.putArray("required").add("client_id").add("from_msg_id");
        return buildSchema("replay_missed_messages",
            "Replay buffered messages to a reconnecting client. " +
            "Client sends its last acknowledged message_id on reconnect. " +
            "Gateway sends all messages after that ID from ring buffer (in order). " +
            "Buffer window: 5 minutes. After 5 min, buffer discarded (old events irrelevant). " +
            "Enables seamless reconnection without data loss for short disconnections.", s);
    }
    @Override public JsonNode execute(JsonNode args) {
        String clientId  = requireString(args, "client_id");
        String fromMsgId = requireString(args, "from_msg_id");
        String assessId  = optString(args, "assessment_id", null);

        // Calculate simulated gap
        ObjectNode r = ok("Missed messages replayed");
        r.put("client_id",        clientId);
        r.put("from_msg_id",      fromMsgId);
        r.put("assessment_id",    assessId != null ? assessId : "all");
        r.put("messages_replayed", 7);
        r.put("replay_started_at", Instant.now().toString());
        r.put("buffer_window_min", 5);
        r.put("replay_complete",   true);
        r.put("delivery_order",   "SEQUENTIAL");

        ArrayNode replayed = r.putArray("replayed_event_types");
        replayed.add("gd.speaker.changed");
        replayed.add("assessment.score.updated");
        replayed.add("timer.tick");
        replayed.add("timer.tick");
        replayed.add("timer.tick");
        replayed.add("assessment.score.updated");
        replayed.add("timer.tick");
        return r;
    }
}

// ============================================================
// ACKNOWLEDGE MESSAGE
// ============================================================
class AcknowledgeMessageTool extends BaseTool {
    @Override public JsonNode getSchema() {
        ObjectNode s = mapper.createObjectNode(); s.put("type","object");
        ObjectNode p = s.putObject("properties");
        prop(p,"client_id",  "string","Client UUID");
        prop(p,"message_id", "string","Message ID to acknowledge");
        s.putArray("required").add("client_id").add("message_id");
        return buildSchema("acknowledge_message",
            "Mark a message as acknowledged by the client. " +
            "Gateway sends ack: {message_id, acked: true}. " +
            "Unacked messages tracked — if no ack within 5s, gateway retries (up to 3 attempts). " +
            "On reconnect: client sends last acked message_id for replay continuity.", s);
    }
    @Override public JsonNode execute(JsonNode args) {
        String clientId = requireString(args, "client_id");
        String msgId    = requireString(args, "message_id");
        ObjectNode r = ok("Message acknowledged");
        r.put("client_id",    clientId);
        r.put("message_id",   msgId);
        r.put("acked",        true);
        r.put("acked_at",     Instant.now().toString());
        r.put("retry_cleared", true);
        return r;
    }
}
