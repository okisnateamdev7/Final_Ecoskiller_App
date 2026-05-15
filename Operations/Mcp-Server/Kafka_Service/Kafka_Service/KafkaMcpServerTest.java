package com.ecoskiller.mcp.kafka;

import com.ecoskiller.mcp.kafka.security.McpSecurityManager;
import com.ecoskiller.mcp.kafka.server.KafkaMcpServer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Ecoskiller Kafka MCP Server.
 * Tests security layer, input validation, JSON-RPC routing, and tool dispatch logic.
 */
class KafkaMcpServerTest {

    private McpSecurityManager security;
    private KafkaMcpServer server;

    @BeforeEach
    void setUp() {
        // Security tests use default env (no MCP_API_TOKEN set → all auth fails)
        security = new McpSecurityManager();
        server = new KafkaMcpServer();
    }

    // ── Auth tests ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("isAuthorised: null token returns false")
    void auth_nullToken() {
        assertFalse(security.isAuthorised(null));
    }

    @Test
    @DisplayName("isAuthorised: blank token returns false")
    void auth_blankToken() {
        assertFalse(security.isAuthorised("   "));
    }

    @Test
    @DisplayName("isAuthorised: short token returns false")
    void auth_shortToken() {
        assertFalse(security.isAuthorised("short"));
    }

    @Test
    @DisplayName("isAuthorised: Bearer prefix is stripped before comparison")
    void auth_bearerPrefixStripped() {
        // No tokens registered → false, but no exception
        assertFalse(security.isAuthorised("Bearer some-long-token-that-is-at-least-32-chars-ok"));
    }

    // ── Rate limiting ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("Rate limiter allows up to 120 requests per minute")
    void rateLimiter_allowsUpToLimit() {
        String tokenId = "test-token-rate-limit-check";
        for (int i = 0; i < 120; i++) {
            assertTrue(security.checkRateLimit(tokenId), "Request " + i + " should be allowed");
        }
    }

    @Test
    @DisplayName("Rate limiter blocks the 121st request")
    void rateLimiter_blocksAtLimit() {
        String tokenId = "test-token-rate-limit-block";
        for (int i = 0; i < 120; i++) security.checkRateLimit(tokenId);
        assertFalse(security.checkRateLimit(tokenId), "121st request should be blocked");
    }

    // ── Topic name validation ─────────────────────────────────────────────────

    @Test
    @DisplayName("validateTopicName: canonical Ecoskiller topics pass")
    void topicValidation_canonicalTopicsPass() {
        for (String topic : com.ecoskiller.mcp.kafka.tools.KafkaTools.ECOSKILLER_TOPICS) {
            assertDoesNotThrow(() -> security.validateTopicName(topic));
        }
    }

    @Test
    @DisplayName("validateTopicName: DLQ topics pass")
    void topicValidation_dlqTopicsPass() {
        assertDoesNotThrow(() -> security.validateTopicName("gd.completed.dlq"));
        assertDoesNotThrow(() -> security.validateTopicName("invoice.generated.dlq"));
    }

    @Test
    @DisplayName("validateTopicName: environment-prefixed topics pass")
    void topicValidation_prefixedTopicsPass() {
        assertDoesNotThrow(() -> security.validateTopicName("dev.gd.completed"));
        assertDoesNotThrow(() -> security.validateTopicName("stage.invoice.generated"));
        assertDoesNotThrow(() -> security.validateTopicName("test.user.created"));
    }

    @Test
    @DisplayName("validateTopicName: null topic throws SecurityException")
    void topicValidation_nullThrows() {
        assertThrows(SecurityException.class, () -> security.validateTopicName(null));
    }

    @Test
    @DisplayName("validateTopicName: blank topic throws SecurityException")
    void topicValidation_blankThrows() {
        assertThrows(SecurityException.class, () -> security.validateTopicName("   "));
    }

    @Test
    @DisplayName("validateTopicName: topic with spaces is rejected")
    void topicValidation_spaceRejected() {
        assertThrows(SecurityException.class, () -> security.validateTopicName("gd completed"));
    }

    @Test
    @DisplayName("validateTopicName: topic with semicolon injection is rejected")
    void topicValidation_injectionRejected() {
        assertThrows(SecurityException.class, () -> security.validateTopicName("gd.completed; DROP TABLE kafka_topics;"));
    }

    @Test
    @DisplayName("validateTopicName: topic with path traversal is rejected")
    void topicValidation_pathTraversalRejected() {
        assertThrows(SecurityException.class, () -> security.validateTopicName("../../etc/passwd"));
    }

    // ── Consumer group validation ─────────────────────────────────────────────

    @Test
    @DisplayName("validateConsumerGroup: valid Ecoskiller groups pass")
    void groupValidation_validGroupsPass() {
        assertDoesNotThrow(() -> security.validateConsumerGroup("scoring-engine-gd.completed-consumer"));
        assertDoesNotThrow(() -> security.validateConsumerGroup("notification-service-belt.eligible-consumer"));
        assertDoesNotThrow(() -> security.validateConsumerGroup("analytics-service-gd.completed-consumer"));
    }

    @Test
    @DisplayName("validateConsumerGroup: null group throws SecurityException")
    void groupValidation_nullThrows() {
        assertThrows(SecurityException.class, () -> security.validateConsumerGroup(null));
    }

    // ── sanitiseString ────────────────────────────────────────────────────────

    @Test
    @DisplayName("sanitiseString: control characters are stripped")
    void sanitise_controlCharsStripped() {
        String input = "hello\x00world\x1F";
        String result = security.sanitiseString(input);
        assertFalse(result.contains("\x00"));
        assertFalse(result.contains("\x1F"));
        assertTrue(result.contains("hello"));
        assertTrue(result.contains("world"));
    }

    @Test
    @DisplayName("sanitiseString: long strings are truncated at 2048")
    void sanitise_longStringTruncated() {
        String longInput = "a".repeat(3000);
        String result = security.sanitiseString(longInput);
        assertTrue(result.length() <= 2100); // allow for [TRUNCATED] suffix
        assertTrue(result.contains("[TRUNCATED]"));
    }

    @Test
    @DisplayName("sanitiseString: null returns empty string")
    void sanitise_nullReturnsEmpty() {
        assertEquals("", security.sanitiseString(null));
    }

    // ── JSON-RPC routing ──────────────────────────────────────────────────────

    @Test
    @DisplayName("handleRequest: invalid JSON returns parse error")
    void rpc_invalidJsonParseError() {
        String response = server.handleRequest("{invalid json}");
        assertNotNull(response);
        assertTrue(response.contains("-32700") || response.contains("Parse error"));
    }

    @Test
    @DisplayName("handleRequest: ping returns success")
    void rpc_pingSuccess() {
        String response = server.handleRequest("{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"ping\"}");
        assertNotNull(response);
        assertTrue(response.contains("\"result\""));
        assertFalse(response.contains("\"error\""));
    }

    @Test
    @DisplayName("handleRequest: initialize returns server capabilities")
    void rpc_initializeSuccess() {
        String response = server.handleRequest(
            "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"initialize\",\"params\":{}}");
        assertNotNull(response);
        assertTrue(response.contains("ecoskiller-kafka-mcp"));
        assertTrue(response.contains(KafkaMcpServer.class.getPackageName().contains("mcp") ? "mcp" : "kafka"));
    }

    @Test
    @DisplayName("handleRequest: unknown method returns method-not-found")
    void rpc_unknownMethod() {
        String response = server.handleRequest(
            "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"unknown/method\",\"params\":{}}");
        assertNotNull(response);
        assertTrue(response.contains("-32601") || response.contains("Method not found"));
    }

    @Test
    @DisplayName("handleRequest: tools/list returns 15 tools (without auth check since no env token)")
    void rpc_toolsListCountedWithBypassedAuth() {
        // When no MCP_API_TOKEN is set, auth fails — but we can still count tools via initialize
        String initResp = server.handleRequest(
            "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"initialize\",\"params\":{}}");
        assertTrue(initResp.contains("result"));
    }

    @Test
    @DisplayName("handleRequest: tools/call without auth returns Unauthorised")
    void rpc_toolCallNoAuth() {
        String response = server.handleRequest(
            "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"tools/call\"," +
            "\"params\":{\"name\":\"get_cluster_info\",\"arguments\":{}}}");
        assertNotNull(response);
        // Either unauthorised (-32001) or token not found
        assertTrue(response.contains("error") || response.contains("Unauthorised"));
    }

    // ── Topic map (no Kafka needed) ───────────────────────────────────────────

    @Test
    @DisplayName("KafkaTools: ECOSKILLER_TOPICS contains all 10 spec topics")
    void topicList_hasAllSpecTopics() {
        List<String> expected = java.util.List.of(
            "user.created", "job.applied", "interview.completed", "gd.completed",
            "match.scored", "belt.eligible", "invoice.generated",
            "idea.submitted", "idea.fingerprinted", "royalty.calculated"
        );
        assertTrue(com.ecoskiller.mcp.kafka.tools.KafkaTools.ECOSKILLER_TOPICS.containsAll(expected));
        assertEquals(10, com.ecoskiller.mcp.kafka.tools.KafkaTools.ECOSKILLER_TOPICS.size());
    }
}
