package com.ecoskiller.mcp.models;

import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

/**
 * Kafka event publisher stub for auth-service.
 * Production: node-rdkafka on topic "auth" (3 partitions, 30-day retention).
 *
 * Events produced:
 *   auth.user_registered, auth.user_login, auth.user_logout,
 *   auth.mfa_enabled, auth.mfa_disabled, auth.token_revoked,
 *   auth.password_reset
 */
public class KafkaEventPublisher {

    private static final Logger LOG = Logger.getLogger(KafkaEventPublisher.class.getName());
    private static final KafkaEventPublisher INSTANCE = new KafkaEventPublisher();
    public static KafkaEventPublisher getInstance() { return INSTANCE; }

    private final List<Map<String, Object>> eventLog = Collections.synchronizedList(new ArrayList<>());

    private KafkaEventPublisher() {}

    public void publish(String eventType, Map<String, Object> payload) {
        Map<String, Object> entry = new LinkedHashMap<>(payload);
        entry.put("event_type", eventType);
        entry.put("kafka_topic","auth");
        entry.put("emitted_at", Instant.now().toString());
        eventLog.add(entry);
        LOG.info("[KAFKA→auth] " + eventType + " | user=" + payload.getOrDefault("user_id", "?"));
    }

    public List<Map<String, Object>> getEventLog() { return Collections.unmodifiableList(eventLog); }
}
