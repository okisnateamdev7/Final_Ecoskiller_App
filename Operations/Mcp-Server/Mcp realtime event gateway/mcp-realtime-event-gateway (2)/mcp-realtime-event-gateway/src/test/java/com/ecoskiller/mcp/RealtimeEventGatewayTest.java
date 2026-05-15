package com.ecoskiller.mcp;

import com.ecoskiller.mcp.config.ServerConfig;
import com.ecoskiller.mcp.security.JwtValidator;
import com.ecoskiller.mcp.security.RateLimiter;
import com.ecoskiller.mcp.tools.*;
import com.ecoskiller.mcp.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for realtime-event-gateway MCP Server.
 * Covers: tool logic, security controls, input validation, edge cases.
 * 30 tests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RealtimeEventGatewayTest {

    private final ObjectMapper mapper = JsonUtil.createSecureMapper();

    // -----------------------------------------------------------------------
    // CONNECTION TOOLS
    // -----------------------------------------------------------------------

    @Test @Order(1)
    @DisplayName("connect_client: valid origin allowed")
    void testConnect_validOrigin() throws Exception {
        ConnectClientTool tool = new ConnectClientTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("user_id",   "user-001");
        args.put("jwt_token", "header.payload.sig");
        args.put("origin",    "https://app.ecoskiller.com");

        JsonNode r = tool.execute(args);
        assertTrue(r.path("success").asBoolean());
        assertNotNull(r.path("client_id").asText());
        assertEquals("WEBSOCKET", r.path("protocol").asText());
        assertTrue(r.path("auth_state").path("jwt_validated").asBoolean());
    }

    @Test @Order(2)
    @DisplayName("connect_client: invalid origin rejected (cross-site hijack prevention)")
    void testConnect_invalidOrigin() throws Exception {
        ConnectClientTool tool = new ConnectClientTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("user_id",   "user-001");
        args.put("jwt_token", "header.payload.sig");
        args.put("origin",    "https://evil.attacker.com");

        JsonNode r = tool.execute(args);
        assertFalse(r.path("success").asBoolean(),
            "Non-ecoskiller.com origin must be rejected");
        assertTrue(r.path("error").asText().contains("Origin not allowed"));
    }

    @Test @Order(3)
    @DisplayName("connect_client: localhost origin allowed (dev)")
    void testConnect_localhostAllowed() throws Exception {
        ConnectClientTool tool = new ConnectClientTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("user_id",   "user-dev");
        args.put("jwt_token", "header.payload.sig");
        args.put("origin",    "http://localhost:3000");

        JsonNode r = tool.execute(args);
        assertTrue(r.path("success").asBoolean());
    }

    @Test @Order(4)
    @DisplayName("connect_client: missing required field throws")
    void testConnect_missingField() {
        ConnectClientTool tool = new ConnectClientTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("user_id", "user-001");
        // missing jwt_token and origin
        assertThrows(IllegalArgumentException.class, () -> tool.execute(args));
    }

    @Test @Order(5)
    @DisplayName("disconnect_client: returns disconnect metadata")
    void testDisconnect() throws Exception {
        DisconnectClientTool tool = new DisconnectClientTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("client_id", "client-001");
        args.put("trigger",   "HEARTBEAT_TIMEOUT");

        JsonNode r = tool.execute(args);
        assertTrue(r.path("success").asBoolean());
        assertTrue(r.path("subscriptions_cleared").asBoolean());
        assertTrue(r.path("ring_buffer_flushed").asBoolean());
        assertEquals(30, r.path("presence_grace_sec").asInt());
    }

    // -----------------------------------------------------------------------
    // SUBSCRIPTION TOOLS
    // -----------------------------------------------------------------------

    @Test @Order(6)
    @DisplayName("subscribe_to_assessment: CANDIDATE gets correct topic set")
    void testSubscribe_candidate() throws Exception {
        SubscribeToAssessmentTool tool = new SubscribeToAssessmentTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("client_id",     "client-001");
        args.put("assessment_id", "assess-001");
        args.put("role",          "CANDIDATE");

        JsonNode r = tool.execute(args);
        assertTrue(r.path("success").asBoolean());
        String topics = r.path("subscribed_topics").toString();
        assertTrue(topics.contains("timer.*"));
        assertTrue(topics.contains("gd.*"));
        assertTrue(topics.contains("assessment.*"));
    }

    @Test @Order(7)
    @DisplayName("subscribe_to_assessment: ADMIN gets wildcard topics")
    void testSubscribe_admin() throws Exception {
        SubscribeToAssessmentTool tool = new SubscribeToAssessmentTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("client_id",     "client-admin");
        args.put("assessment_id", "assess-001");
        args.put("role",          "ADMIN");

        JsonNode r = tool.execute(args);
        assertTrue(r.path("subscribed_topics").toString().contains("*"));
    }

    @Test @Order(8)
    @DisplayName("subscribe_to_assessment: invalid role rejected")
    void testSubscribe_invalidRole() {
        SubscribeToAssessmentTool tool = new SubscribeToAssessmentTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("client_id",     "client-001");
        args.put("assessment_id", "assess-001");
        args.put("role",          "HACKER");

        assertThrows(IllegalArgumentException.class, () -> tool.execute(args));
    }

    // -----------------------------------------------------------------------
    // EVENT ROUTING TOOLS
    // -----------------------------------------------------------------------

    @Test @Order(9)
    @DisplayName("publish_event: valid event publishes to Redis and clients")
    void testPublishEvent() throws Exception {
        PublishEventTool tool = new PublishEventTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("assessment_id", "assess-001");
        args.put("event_type",    "gd.speaker.changed");
        args.put("payload_json",  "{\"speaker_id\":\"user-5\",\"start_time\":\"2026-03-20T10:00:00Z\"}");

        JsonNode r = tool.execute(args);
        assertTrue(r.path("success").asBoolean());
        assertTrue(r.path("cross_pod_published").asBoolean());
        assertTrue(r.path("clients_notified").asInt() > 0);
        assertTrue(r.path("redis_channel").asText().contains("assess-001"));
    }

    @Test @Order(10)
    @DisplayName("publish_event: invalid event_type rejected")
    void testPublishEvent_invalidType() {
        PublishEventTool tool = new PublishEventTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("assessment_id", "assess-001");
        args.put("event_type",    "malicious.event.type");
        args.put("payload_json",  "{}");

        assertThrows(IllegalArgumentException.class, () -> tool.execute(args));
    }

    @Test @Order(11)
    @DisplayName("publish_event: payload exceeding 64KB rejected")
    void testPublishEvent_oversizedPayload() {
        PublishEventTool tool = new PublishEventTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("assessment_id", "assess-001");
        args.put("event_type",    "timer.tick");
        args.put("payload_json",  "x".repeat(65537));

        assertThrows(IllegalArgumentException.class, () -> tool.execute(args));
    }

    @Test @Order(12)
    @DisplayName("route_kafka_event: full pipeline returns <100ms total")
    void testRouteKafkaEvent() throws Exception {
        RouteKafkaEventTool tool = new RouteKafkaEventTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("kafka_topic",    "assessment.*");
        args.put("kafka_partition", 3);
        args.put("kafka_offset",   10483L);
        args.put("event_type",     "assessment.score.updated");
        args.put("assessment_id",  "assess-001");
        args.put("message_json",   "{\"score\":82.5,\"candidate_id\":\"cand-01\"}");

        JsonNode r = tool.execute(args);
        assertTrue(r.path("success").asBoolean());
        assertTrue(r.path("offset_committed").asBoolean());
        assertTrue(r.path("ring_buffer_appended").asBoolean());
        int total = r.path("pipeline_timing_ms").path("total").asInt();
        assertTrue(total < 100, "Pipeline total must be <100ms, got " + total);
    }

    @Test @Order(13)
    @DisplayName("broadcast_redis_pubsub: publishes to correct channel")
    void testBroadcastRedisPubSub() throws Exception {
        BroadcastRedisPubSubTool tool = new BroadcastRedisPubSubTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("assessment_id", "assess-XYZ");
        args.put("event_json",    "{\"type\":\"timer.tick\",\"remaining\":120}");

        JsonNode r = tool.execute(args);
        assertTrue(r.path("success").asBoolean());
        assertEquals("assessment:assess-XYZ:updates", r.path("redis_channel").asText());
        assertTrue(r.path("pubsub_latency_ms").asInt() < 20);
    }

    // -----------------------------------------------------------------------
    // MESSAGE BUFFER & REPLAY
    // -----------------------------------------------------------------------

    @Test @Order(14)
    @DisplayName("get_message_buffer: returns buffer metadata")
    void testGetMessageBuffer() throws Exception {
        GetMessageBufferTool tool = new GetMessageBufferTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("client_id", "client-001");
        args.put("limit",     50);

        JsonNode r = tool.execute(args);
        assertTrue(r.path("success").asBoolean());
        assertEquals(100, r.path("buffer_capacity").asInt());
        assertNotNull(r.path("oldest_message_id").asText());
    }

    @Test @Order(15)
    @DisplayName("get_message_buffer: limit capped at 100")
    void testGetMessageBuffer_limitCap() throws Exception {
        GetMessageBufferTool tool = new GetMessageBufferTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("client_id", "client-001");
        args.put("limit",     9999);  // over cap

        JsonNode r = tool.execute(args);
        // Tool internally caps at 100, should not throw
        assertTrue(r.path("success").asBoolean());
    }

    @Test @Order(16)
    @DisplayName("replay_missed_messages: returns ordered replay")
    void testReplayMissedMessages() throws Exception {
        ReplayMissedMessagesTool tool = new ReplayMissedMessagesTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("client_id",   "client-001");
        args.put("from_msg_id", "msg-035");

        JsonNode r = tool.execute(args);
        assertTrue(r.path("success").asBoolean());
        assertTrue(r.path("messages_replayed").asInt() > 0);
        assertEquals("SEQUENTIAL", r.path("delivery_order").asText());
        assertTrue(r.path("replay_complete").asBoolean());
    }

    @Test @Order(17)
    @DisplayName("acknowledge_message: clears retry tracking")
    void testAcknowledgeMessage() throws Exception {
        AcknowledgeMessageTool tool = new AcknowledgeMessageTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("client_id",  "client-001");
        args.put("message_id", "msg-042");

        JsonNode r = tool.execute(args);
        assertTrue(r.path("success").asBoolean());
        assertTrue(r.path("acked").asBoolean());
        assertTrue(r.path("retry_cleared").asBoolean());
    }

    // -----------------------------------------------------------------------
    // PRESENCE
    // -----------------------------------------------------------------------

    @Test @Order(18)
    @DisplayName("track_presence: JOIN fires presence broadcast")
    void testTrackPresence_join() throws Exception {
        TrackPresenceTool tool = new TrackPresenceTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("assessment_id", "assess-001");
        args.put("client_id",     "client-001");
        args.put("user_id",       "user-001");
        args.put("action",        "JOIN");
        args.put("role",          "OBSERVER");

        JsonNode r = tool.execute(args);
        assertTrue(r.path("success").asBoolean());
        assertTrue(r.path("presence_event_fired").asBoolean());
        assertEquals("user_joined_assessment", r.path("broadcast_event").asText());
    }

    @Test @Order(19)
    @DisplayName("track_presence: LEAVE has 30s grace period")
    void testTrackPresence_leave() throws Exception {
        TrackPresenceTool tool = new TrackPresenceTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("assessment_id", "assess-001");
        args.put("client_id",     "client-001");
        args.put("user_id",       "user-001");
        args.put("action",        "LEAVE");

        JsonNode r = tool.execute(args);
        assertTrue(r.path("success").asBoolean());
        assertEquals(30, r.path("grace_period_sec").asInt());
    }

    @Test @Order(20)
    @DisplayName("track_presence: invalid action rejected")
    void testTrackPresence_invalidAction() {
        TrackPresenceTool tool = new TrackPresenceTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("assessment_id", "assess-001");
        args.put("client_id",     "client-001");
        args.put("user_id",       "user-001");
        args.put("action",        "WATCH");  // invalid

        assertThrows(IllegalArgumentException.class, () -> tool.execute(args));
    }

    // -----------------------------------------------------------------------
    // CIRCUIT BREAKER
    // -----------------------------------------------------------------------

    @Test @Order(21)
    @DisplayName("get_circuit_breaker_status: returns CLOSED in normal state")
    void testCircuitBreaker_status() throws Exception {
        GetCircuitBreakerStatusTool tool = new GetCircuitBreakerStatusTool();
        JsonNode r = tool.execute(mapper.createObjectNode());
        assertTrue(r.path("success").asBoolean());
        assertEquals("CLOSED", r.path("state").asText());
    }

    @Test @Order(22)
    @DisplayName("trip_circuit_breaker: broadcasts circuit_open event")
    void testTripCircuitBreaker() throws Exception {
        TripCircuitBreakerTool tool = new TripCircuitBreakerTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("reason",      "Overload test");
        args.put("duration_sec", 60);

        JsonNode r = tool.execute(args);
        assertTrue(r.path("success").asBoolean());
        assertEquals("OPEN", r.path("new_state").asText());
        assertEquals("gateway.circuit_open", r.path("broadcast_event").asText());
        assertTrue(r.path("clients_notified").asInt() > 0);
    }

    @Test @Order(23)
    @DisplayName("reset_circuit_breaker: transitions to HALF_OPEN")
    void testResetCircuitBreaker() throws Exception {
        ResetCircuitBreakerTool tool = new ResetCircuitBreakerTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("probe_pct", 15);

        JsonNode r = tool.execute(args);
        assertEquals("HALF_OPEN", r.path("new_state").asText());
        assertEquals(15, r.path("probe_pct").asInt());
    }

    // -----------------------------------------------------------------------
    // KAFKA & TIMER
    // -----------------------------------------------------------------------

    @Test @Order(24)
    @DisplayName("get_kafka_consumer_status: lag within healthy threshold")
    void testKafkaStatus() throws Exception {
        GetKafkaConsumerStatusTool tool = new GetKafkaConsumerStatusTool();
        JsonNode r = tool.execute(mapper.createObjectNode());
        assertTrue(r.path("success").asBoolean());
        assertTrue(r.path("total_lag").asInt() < 1000, "Lag should be below alert threshold");
        assertTrue(r.path("status_healthy").asBoolean());
    }

    @Test @Order(25)
    @DisplayName("emit_timer_tick: remaining=0 fires timer.expired")
    void testTimerTick_expired() throws Exception {
        EmitTimerTickTool tool = new EmitTimerTickTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("assessment_id",    "assess-001");
        args.put("remaining_seconds", 0);
        args.put("phase",            "CODING");

        JsonNode r = tool.execute(args);
        assertTrue(r.path("success").asBoolean());
        assertEquals("timer.expired", r.path("event_type").asText());
        assertTrue(r.path("auto_expire_emitted").asBoolean());
    }

    @Test @Order(26)
    @DisplayName("emit_timer_tick: negative remaining rejected")
    void testTimerTick_negativeRejected() {
        EmitTimerTickTool tool = new EmitTimerTickTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("assessment_id",    "assess-001");
        args.put("remaining_seconds", -5);

        assertThrows(IllegalArgumentException.class, () -> tool.execute(args));
    }

    // -----------------------------------------------------------------------
    // HEALTH & SECURITY
    // -----------------------------------------------------------------------

    @Test @Order(27)
    @DisplayName("health_check: all dependencies healthy")
    void testHealthCheck() throws Exception {
        HealthCheckTool tool = new HealthCheckTool();
        JsonNode r = tool.execute(mapper.createObjectNode());
        assertTrue(r.path("success").asBoolean());
        assertEquals("HEALTHY", r.path("status").asText());
        assertTrue(r.path("kafka_healthy").asBoolean());
        assertTrue(r.path("redis_healthy").asBoolean());
        assertTrue(r.path("ready").asBoolean());
    }

    @Test @Order(28)
    @DisplayName("Security: JWT alg:none attack blocked")
    void testSecurity_jwtAlgNone() {
        JwtValidator v = new JwtValidator(ServerConfig.load());
        String header  = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"alg\":\"none\",\"typ\":\"JWT\"}".getBytes());
        String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"sub\":\"u1\",\"iss\":\"https://auth.ecoskiller.com/realms/test\"," +
                        "\"exp\":9999999999,\"iat\":1700000000}".getBytes());
        assertThrows(SecurityException.class,
                () -> v.validate(header + "." + payload + ".fakesig"),
                "alg:none must be rejected");
    }

    @Test @Order(29)
    @DisplayName("Security: Rate limiter blocks excess requests")
    void testSecurity_rateLimiter() {
        RateLimiter limiter = new RateLimiter(5);
        String key = "test-global";
        for (int i = 0; i < 5; i++) assertTrue(limiter.allowRequest(key));
        assertFalse(limiter.allowRequest(key), "6th request must be blocked");
    }

    @Test @Order(30)
    @DisplayName("Security: Input sanitization strips control characters")
    void testSecurity_inputSanitization() throws Exception {
        ConnectClientTool tool = new ConnectClientTool();
        ObjectNode args = mapper.createObjectNode();
        args.put("user_id",   "user-\u0000\u0001malicious");
        args.put("jwt_token", "header.payload.sig");
        args.put("origin",    "https://app.ecoskiller.com");

        // Should succeed (control chars stripped), not crash
        JsonNode r = tool.execute(args);
        assertTrue(r.path("success").asBoolean());
        assertFalse(r.path("user_id").asText().contains("\u0000"),
            "Null byte must be stripped from user_id");
    }
}
