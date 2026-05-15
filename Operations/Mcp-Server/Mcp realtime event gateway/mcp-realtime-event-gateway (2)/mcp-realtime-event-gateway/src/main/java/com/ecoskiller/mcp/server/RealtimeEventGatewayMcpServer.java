package com.ecoskiller.mcp.server;

import com.ecoskiller.mcp.config.ServerConfig;
import com.ecoskiller.mcp.security.JwtValidator;
import com.ecoskiller.mcp.security.RateLimiter;
import com.ecoskiller.mcp.tools.*;
import com.ecoskiller.mcp.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * Ecoskiller | realtime-event-gateway — MCP Server (Java)
 * Real-Time Event Streaming & WebSocket Gateway
 *
 * Manages: WebSocket connections, Kafka consumer groups, Redis pub-sub,
 * presence tracking, event routing, circuit breaking, message replay.
 *
 * Protocol : JSON-RPC 2.0 over stdio
 * MCP Version: 2024-11-05
 * Security  : JWT validation, rate limiting, origin checking, input sanitization
 */
public class RealtimeEventGatewayMcpServer {

    private static final Logger LOGGER = Logger.getLogger(
            RealtimeEventGatewayMcpServer.class.getName());

    private static final String MCP_VERSION  = "2024-11-05";
    private static final String SERVER_NAME  = "mcp-realtime-event-gateway";
    private static final String SERVER_VERSION = "1.0.0";

    private final ObjectMapper mapper = JsonUtil.createSecureMapper();
    private final Map<String, McpTool> tools = new LinkedHashMap<>();
    private final JwtValidator  jwtValidator;
    private final RateLimiter   rateLimiter;
    private final ServerConfig  config;

    public RealtimeEventGatewayMcpServer() {
        this.config       = ServerConfig.load();
        this.jwtValidator = new JwtValidator(config);
        this.rateLimiter  = new RateLimiter(config.getRateLimitPerMinute());
        registerTools();
        setupLogging();
        LOGGER.info("RealtimeEventGatewayMcpServer initialised — " + tools.size() + " tools");
    }

    // -----------------------------------------------------------------------
    // Tool registration
    // -----------------------------------------------------------------------
    private void registerTools() {
        // 1 · WebSocket Connection Management
        tools.put("connect_client",              new ConnectClientTool());
        tools.put("disconnect_client",           new DisconnectClientTool());
        tools.put("get_connection_info",         new GetConnectionInfoTool());
        tools.put("list_active_connections",     new ListActiveConnectionsTool());

        // 2 · Event Subscription Management
        tools.put("subscribe_to_assessment",     new SubscribeToAssessmentTool());
        tools.put("unsubscribe_from_assessment", new UnsubscribeFromAssessmentTool());
        tools.put("get_subscriptions",           new GetSubscriptionsTool());

        // 3 · Event Publishing & Routing
        tools.put("publish_event",               new PublishEventTool());
        tools.put("route_kafka_event",           new RouteKafkaEventTool());
        tools.put("broadcast_to_assessment",     new BroadcastToAssessmentTool());
        tools.put("broadcast_redis_pubsub",      new BroadcastRedisPubSubTool());

        // 4 · Message Buffer & Replay
        tools.put("get_message_buffer",          new GetMessageBufferTool());
        tools.put("replay_missed_messages",      new ReplayMissedMessagesTool());
        tools.put("acknowledge_message",         new AcknowledgeMessageTool());

        // 5 · Presence & Awareness
        tools.put("track_presence",              new TrackPresenceTool());
        tools.put("get_presence",                new GetPresenceTool());
        tools.put("emit_presence_event",         new EmitPresenceEventTool());

        // 6 · Circuit Breaker & Rate Limiting
        tools.put("get_circuit_breaker_status",  new GetCircuitBreakerStatusTool());
        tools.put("trip_circuit_breaker",        new TripCircuitBreakerTool());
        tools.put("reset_circuit_breaker",       new ResetCircuitBreakerTool());

        // 7 · Kafka Consumer Management
        tools.put("get_kafka_consumer_status",   new GetKafkaConsumerStatusTool());
        tools.put("commit_kafka_offset",         new CommitKafkaOffsetTool());

        // 8 · Authentication & Token
        tools.put("validate_client_token",       new ValidateClientTokenTool());
        tools.put("revoke_client_token",         new RevokeClientTokenTool());

        // 9 · Timer & Heartbeat
        tools.put("send_heartbeat",              new SendHeartbeatTool());
        tools.put("emit_timer_tick",             new EmitTimerTickTool());

        // 10 · Health & Observability
        tools.put("health_check",                new HealthCheckTool());
        tools.put("get_metrics",                 new GetMetricsTool());
    }

    // -----------------------------------------------------------------------
    // Logging — stderr only (stdout reserved for MCP JSON-RPC)
    // -----------------------------------------------------------------------
    private void setupLogging() {
        Logger root = Logger.getLogger("");
        root.setLevel(config.getLogLevel());
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        StreamHandler stderrHandler = new StreamHandler(System.err, new SimpleFormatter());
        stderrHandler.setLevel(config.getLogLevel());
        root.addHandler(stderrHandler);
    }

    // -----------------------------------------------------------------------
    // Main read loop
    // -----------------------------------------------------------------------
    public void run() throws IOException {
        LOGGER.info("MCP Server started — waiting on stdin...");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter   writer  = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                JsonNode  req  = mapper.readTree(line);
                JsonNode  resp = handleRequest(req);
                if (resp != null) {
                    writer.println(mapper.writeValueAsString(resp));
                    writer.flush();
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Fatal parse error", e);
                ObjectNode err = mapper.createObjectNode();
                err.put("jsonrpc", "2.0"); err.putNull("id");
                ObjectNode errBody = err.putObject("error");
                errBody.put("code", -32700); errBody.put("message", "Parse error");
                writer.println(mapper.writeValueAsString(err));
                writer.flush();
            }
        }
        LOGGER.info("MCP Server shutdown (stdin closed).");
    }

    // -----------------------------------------------------------------------
    // Request dispatcher
    // -----------------------------------------------------------------------
    private JsonNode handleRequest(JsonNode req) {
        String id     = req.has("id") ? req.get("id").asText() : null;
        String method = req.has("method") ? req.get("method").asText() : "";

        if (!rateLimiter.allowRequest("global"))
            return buildError(id, -32000,
                    "Rate limit exceeded (" + config.getRateLimitPerMinute() + " req/min)");

        try {
            return switch (method) {
                case "initialize"  -> handleInitialize(id, req);
                case "tools/list"  -> handleToolsList(id);
                case "tools/call"  -> handleToolCall(id, req);
                case "ping"        -> buildResult(id, mapper.createObjectNode());
                default            -> buildError(id, -32601, "Method not found: " + method);
            };
        } catch (SecurityException se) {
            LOGGER.warning("Security violation: " + se.getMessage());
            return buildError(id, -32001, "Security error: " + se.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Request error", e);
            return buildError(id, -32603, "Internal error: " + e.getMessage());
        }
    }

    private JsonNode handleInitialize(String id, JsonNode req) {
        if (config.isJwtValidationEnabled()) {
            String token = req.path("params").path("_auth_token").asText(null);
            if (token != null && !token.isEmpty()) jwtValidator.validate(token);
        }
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);
        ObjectNode info = result.putObject("serverInfo");
        info.put("name", SERVER_NAME); info.put("version", SERVER_VERSION);
        result.putObject("capabilities").putObject("tools");
        ObjectNode meta = result.putObject("_meta");
        meta.put("service",       "realtime-event-gateway");
        meta.put("namespace",     "realtime");
        meta.put("maxConnections", 10000);
        meta.put("maxEventsPerSec", 50000);
        meta.put("latencySla",    "<100ms p99");
        meta.put("securityLevel", "HIGH");
        LOGGER.info("Client initialised MCP session");
        return buildResult(id, result);
    }

    private JsonNode handleToolsList(String id) {
        ArrayNode list = mapper.createArrayNode();
        for (McpTool t : tools.values()) list.add(t.getSchema());
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", list);
        return buildResult(id, result);
    }

    private JsonNode handleToolCall(String id, JsonNode req) {
        JsonNode params    = req.path("params");
        String   toolName  = params.path("name").asText();
        JsonNode arguments = params.path("arguments");

        if (!tools.containsKey(toolName)) {
            LOGGER.warning("Unknown tool: " + toolName);
            return buildError(id, -32602, "Unknown tool: " + toolName);
        }

        // JWT required for sensitive administrative tools
        if (config.isJwtValidationEnabled() && isSensitiveTool(toolName)) {
            String token = arguments.path("_auth_token").asText(null);
            if (token == null || token.isEmpty())
                return buildError(id, -32001, "Authentication required for: " + toolName);
            jwtValidator.validate(token);
        }

        // client_id / assessment_id format validation
        if (hasClientId(toolName)) {
            String cid = arguments.path("client_id").asText(null);
            if (cid != null && !isValidId(cid))
                return buildError(id, -32602, "Invalid client_id format");
        }
        if (hasAssessmentId(toolName)) {
            String aid = arguments.path("assessment_id").asText(null);
            if (aid != null && !isValidId(aid))
                return buildError(id, -32602, "Invalid assessment_id format");
        }

        LOGGER.info("Tool call: " + toolName);
        try {
            JsonNode toolResult = tools.get(toolName).execute(arguments);
            ObjectNode content  = mapper.createObjectNode();
            ArrayNode  arr      = content.putArray("content");
            ObjectNode text     = arr.addObject();
            text.put("type", "text");
            text.put("text", mapper.writeValueAsString(toolResult));
            return buildResult(id, content);
        } catch (IllegalArgumentException iae) {
            return buildError(id, -32602, "Invalid params: " + iae.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Tool error: " + toolName, e);
            return buildError(id, -32603, "Tool error: " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // Security helpers
    // -----------------------------------------------------------------------
    private static final Set<String> SENSITIVE_TOOLS = Set.of(
            "trip_circuit_breaker", "reset_circuit_breaker",
            "revoke_client_token",  "commit_kafka_offset",
            "get_metrics");

    private boolean isSensitiveTool(String name) { return SENSITIVE_TOOLS.contains(name); }

    private boolean hasClientId(String name) {
        return name.contains("client") || name.contains("connect") ||
               name.contains("disconnect") || name.contains("subscribe") ||
               name.contains("presence") || name.contains("heartbeat") ||
               name.contains("token") || name.contains("replay") ||
               name.contains("acknowledge");
    }
    private boolean hasAssessmentId(String name) {
        return name.contains("assessment") || name.contains("broadcast") ||
               name.contains("presence");
    }
    /** Allowlist: alphanumeric, hyphens, underscores — prevents injection */
    private boolean isValidId(String id) {
        return id != null && id.matches("^[a-zA-Z0-9_-]{1,128}$");
    }

    // -----------------------------------------------------------------------
    // JSON-RPC helpers
    // -----------------------------------------------------------------------
    private ObjectNode buildResult(String id, JsonNode result) {
        ObjectNode r = mapper.createObjectNode();
        r.put("jsonrpc", "2.0");
        if (id != null) r.put("id", id);
        r.set("result", result);
        return r;
    }
    private ObjectNode buildError(String id, int code, String message) {
        ObjectNode r = mapper.createObjectNode();
        r.put("jsonrpc", "2.0");
        if (id != null) r.put("id", id);
        ObjectNode err = r.putObject("error");
        err.put("code", code); err.put("message", message);
        return r;
    }

    public static void main(String[] args) throws IOException {
        new RealtimeEventGatewayMcpServer().run();
    }
}
