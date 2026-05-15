package com.ecoskiller.mcp.redis.tools;

import com.ecoskiller.mcp.redis.config.RedisConfig;
import com.ecoskiller.mcp.redis.security.AuditLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import redis.clients.jedis.Jedis;

import java.time.Instant;

/**
 * Tool: pubsub_publish
 *
 * Publish an event message to a Redis Pub/Sub channel.
 *
 * Key channels in Ecoskiller (from spec):
 *  - assessment:completed    → triggers leaderboard update, ML scoring
 *  - leaderboard:updated     → Web UI receives live rank changes
 *  - session:invalidated     → Broadcast logout to all sessions
 *  - candidate:profile:updated → Cache invalidation event
 *
 * Note: This tool publishes only. Subscribers are microservices
 * (assessment-service, notification-service, web socket gateway).
 * Redis pub/sub is fire-and-forget — no delivery guarantee.
 */
public class PubSubPublishTool extends BaseTool {

    /** Allowlist of valid channel prefixes — prevents arbitrary channel creation. */
    private static final java.util.Set<String> ALLOWED_CHANNEL_PREFIXES = java.util.Set.of(
        "assessment", "leaderboard", "session", "candidate",
        "recruiter", "notification", "competition", "royalty"
    );

    public PubSubPublishTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }
    @Override public String name() { return "pubsub_publish"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key", apiKeyProp());
        props.set("channel", strProp(
            "Pub/Sub channel name (e.g. assessment:completed, leaderboard:updated, session:invalidated). " +
            "Must start with one of: assessment, leaderboard, session, candidate, recruiter, notification, competition, royalty"));
        props.set("message", strProp("Message payload (JSON string recommended for structured events)"));
        props.set("event_type", strProp("Optional: event type tag for filtering (e.g. SCORE_UPDATED, RANK_CHANGED)"));
        return buildSchema(name(),
                "Publish an event to a Redis Pub/Sub channel. Fire-and-forget delivery to all " +
                "active subscribers (microservices, WebSocket gateways, notification services). " +
                "Use for real-time events: assessment completion, leaderboard updates, cache invalidation.",
                props, "channel", "message");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String channel   = requireString(args, "channel");
        String message   = requireString(args, "message");
        String eventType = optString(args, "event_type", "");

        // Security: validate channel prefix against allowlist
        validateChannelPrefix(channel);
        validateKey(channel, "channel");

        // Enrich message with metadata envelope
        ObjectNode envelope = json.createObjectNode();
        envelope.put("channel",    channel);
        envelope.put("payload",    message);
        envelope.put("event_type", eventType);
        envelope.put("timestamp",  Instant.now().toString());
        envelope.put("source",     "mcp-redis-caching-session");

        String envelopeStr = json.writeValueAsString(envelope);

        try (Jedis j = conn()) {
            long subscribers = j.publish(channel, envelopeStr);

            ObjectNode r = json.createObjectNode();
            r.put("published",         true);
            r.put("channel",           channel);
            r.put("active_subscribers", subscribers);
            r.put("event_type",        eventType);
            r.put("timestamp",         Instant.now().toString());
            r.put("note", subscribers == 0
                    ? "no active subscribers — message not delivered (fire-and-forget)"
                    : "delivered to " + subscribers + " subscriber(s)");
            return ToolResult.ok(json.writeValueAsString(r));
        }
    }

    private void validateChannelPrefix(String channel) {
        String prefix = channel.contains(":") ? channel.split(":")[0] : channel;
        if (!ALLOWED_CHANNEL_PREFIXES.contains(prefix)) {
            throw new IllegalArgumentException(
                "Channel prefix '" + prefix + "' is not allowed. " +
                "Must be one of: " + ALLOWED_CHANNEL_PREFIXES);
        }
    }
}
