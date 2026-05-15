package com.ecoskiller.mcp.kafka.server;

import com.ecoskiller.mcp.kafka.security.McpSecurityManager;
import com.ecoskiller.mcp.kafka.tools.KafkaTools;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Ecoskiller Kafka MCP Server — CAT-KAFKA
 *
 * Transport : stdio (stdin/stdout)
 * Protocol  : JSON-RPC 2.0 / MCP 2024-11-05
 * Security  : Bearer token auth + rate limiting + SASL/SSL Kafka
 *
 * Run:
 *   java -jar mcp-kafka-service-1.0.0.jar
 *
 * Required env vars:
 *   MCP_API_TOKEN             — Bearer token for MCP clients
 *   KAFKA_BOOTSTRAP_SERVERS   — e.g. kafka-0.kafka-headless.messaging.svc.cluster.local:9092
 *   KAFKA_SASL_USERNAME
 *   KAFKA_SASL_PASSWORD
 *   KAFKA_SECURITY_PROTOCOL   — SASL_SSL (prod) | PLAINTEXT (dev)
 *   ECOSKILLER_ENV            — prod | stage | test | dev  (default: dev)
 */
public class KafkaMcpServer {

    private static final Logger log = LoggerFactory.getLogger(KafkaMcpServer.class);
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "ecoskiller-kafka-mcp";
    private static final String SERVER_VERSION = "1.0.0";

    private final ObjectMapper mapper = new ObjectMapper();
    private final McpSecurityManager security = new McpSecurityManager();
    private final KafkaTools kafkaTools = new KafkaTools(security);

    // ── Entry point ──────────────────────────────────────────────────────────

    public static void main(String[] args) {
        log.info("Starting {} v{}", SERVER_NAME, SERVER_VERSION);
        try {
            new KafkaMcpServer().run();
        } catch (Exception e) {
            log.error("Fatal server error", e);
            System.exit(1);
        }
    }

    public void run() throws IOException {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter writer = new PrintWriter(
            new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

        log.info("{} ready — listening on stdio", SERVER_NAME);

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String response = handleRequest(line);
            if (response != null) {
                writer.println(response);
            }
        }
        log.info("{} shutting down — stdin closed", SERVER_NAME);
    }

    // ── Request dispatcher ────────────────────────────────────────────────────

    String handleRequest(String rawJson) {
        JsonNode req;
        try {
            req = mapper.readTree(rawJson);
        } catch (Exception e) {
            return errorResponse(null, -32700, "Parse error: invalid JSON");
        }

        JsonNode idNode = req.get("id");
        Object id = idNode == null ? null : (idNode.isNumber() ? idNode.asLong() : idNode.asText());
        String method = req.has("method") ? req.get("method").asText() : "";
        JsonNode params = req.has("params") ? req.get("params") : mapper.createObjectNode();

        // ── Auth check (skip for initialize / ping) ───────────────────────────
        if (!method.equals("initialize") && !method.equals("ping")) {
            String token = extractToken(params);
            if (!security.isAuthorised(token)) {
                return errorResponse(id, -32001, "Unauthorised: invalid or missing Bearer token");
            }
            if (!security.checkRateLimit(token != null ? token : "anonymous")) {
                return errorResponse(id, -32002, "Too many requests — rate limit exceeded");
            }
        }

        return switch (method) {
            case "initialize"  -> handleInitialize(id, params);
            case "ping"        -> successResponse(id, mapper.createObjectNode());
            case "tools/list"  -> handleToolsList(id);
            case "tools/call"  -> handleToolsCall(id, params);
            default            -> errorResponse(id, -32601, "Method not found: " + method);
        };
    }

    // ── MCP method handlers ───────────────────────────────────────────────────

    private String handleInitialize(Object id, JsonNode params) {
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);

        ObjectNode serverInfo = result.putObject("serverInfo");
        serverInfo.put("name", SERVER_NAME);
        serverInfo.put("version", SERVER_VERSION);
        serverInfo.put("description",
            "Ecoskiller Kafka MCP Server — Apache Kafka 3.7.0 management for the Ecoskiller platform");

        ObjectNode capabilities = result.putObject("capabilities");
        capabilities.putObject("tools");

        return successResponse(id, result);
    }

    private String handleToolsList(Object id) {
        ArrayNode tools = mapper.createArrayNode();
        for (ToolDefinition def : TOOL_DEFINITIONS) {
            tools.add(def.toJsonNode(mapper));
        }
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", tools);
        return successResponse(id, result);
    }

    private String handleToolsCall(Object id, JsonNode params) {
        String toolName = params.has("name") ? params.get("name").asText() : "";
        JsonNode args = params.has("arguments") ? params.get("arguments") : mapper.createObjectNode();
        String token = extractToken(params);

        // Audit log
        Map<String, Object> auditParams = new LinkedHashMap<>();
        args.fields().forEachRemaining(e -> auditParams.put(e.getKey(), e.getValue().asText()));
        security.auditToolCall(toolName, token != null ? token : "unknown", auditParams);

        try {
            Object toolResult = dispatchTool(toolName, args);
            String json = mapper.writeValueAsString(toolResult);

            ObjectNode content = mapper.createObjectNode();
            ArrayNode contentArr = content.putArray("content");
            ObjectNode textContent = contentArr.addObject();
            textContent.put("type", "text");
            textContent.put("text", json);

            return successResponse(id, content);
        } catch (SecurityException e) {
            log.warn("Security violation in tool={}: {}", toolName, e.getMessage());
            return errorResponse(id, -32003, "Security violation: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return errorResponse(id, -32602, "Invalid params: " + e.getMessage());
        } catch (Exception e) {
            log.error("Tool execution error for {}", toolName, e);
            return errorResponse(id, -32000, "Tool error: " + sanitise(e.getMessage()));
        }
    }

    // ── Tool dispatcher ───────────────────────────────────────────────────────

    private Object dispatchTool(String toolName, JsonNode args) throws Exception {
        return switch (toolName) {

            case "list_topics" ->
                kafkaTools.listTopics(bool(args, "ecoskiller_only", false));

            case "create_topic" ->
                kafkaTools.createTopic(
                    require(args, "topic_name"),
                    intVal(args, "partitions", 3),
                    intVal(args, "replication_factor", 3),
                    longVal(args, "retention_ms", 0),
                    bool(args, "create_dlq", true)
                );

            case "describe_topic" ->
                kafkaTools.describeTopic(require(args, "topic_name"));

            case "delete_topic" ->
                kafkaTools.deleteTopic(
                    require(args, "topic_name"),
                    bool(args, "delete_dlq_also", false)
                );

            case "list_consumer_groups" ->
                kafkaTools.listConsumerGroups();

            case "describe_consumer_group" ->
                kafkaTools.describeConsumerGroup(require(args, "group_id"));

            case "get_consumer_lag" ->
                kafkaTools.getConsumerLag(
                    require(args, "group_id"),
                    require(args, "topic_name")
                );

            case "publish_event" ->
                kafkaTools.publishEvent(
                    require(args, "topic_name"),
                    strVal(args, "key", null),
                    require(args, "value_json")
                );

            case "list_dlq_topics" ->
                kafkaTools.listDlqTopics();

            case "get_dlq_messages" ->
                kafkaTools.getDlqMessages(
                    require(args, "topic_name"),
                    intVal(args, "max_messages", 20)
                );

            case "get_topic_offsets" ->
                kafkaTools.getTopicOffsets(require(args, "topic_name"));

            case "provision_ecoskiller_topics" ->
                kafkaTools.provisionEcoskillerTopics(
                    intVal(args, "replication_factor", 3)
                );

            case "get_cluster_info" ->
                kafkaTools.getClusterInfo();

            case "reset_consumer_offset" ->
                kafkaTools.resetConsumerOffset(
                    require(args, "group_id"),
                    require(args, "topic_name"),
                    strVal(args, "strategy", "earliest")
                );

            case "get_ecoskiller_topic_map" ->
                kafkaTools.getEcoskillerTopicMap();

            default ->
                throw new IllegalArgumentException("Unknown tool: " + toolName);
        };
    }

    // ── JSON-RPC helpers ──────────────────────────────────────────────────────

    private String successResponse(Object id, Object result) {
        try {
            ObjectNode resp = mapper.createObjectNode();
            resp.put("jsonrpc", "2.0");
            if (id != null) resp.set("id", mapper.valueToTree(id));
            resp.set("result", mapper.valueToTree(result));
            return mapper.writeValueAsString(resp);
        } catch (Exception e) {
            log.error("Error serialising response", e);
            return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32603,\"message\":\"Internal error\"}}";
        }
    }

    private String errorResponse(Object id, int code, String message) {
        try {
            ObjectNode resp = mapper.createObjectNode();
            resp.put("jsonrpc", "2.0");
            if (id != null) resp.set("id", mapper.valueToTree(id));
            ObjectNode err = resp.putObject("error");
            err.put("code", code);
            err.put("message", sanitise(message));
            return mapper.writeValueAsString(resp);
        } catch (Exception e) {
            return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32603,\"message\":\"Internal error\"}}";
        }
    }

    // ── Param extraction helpers ──────────────────────────────────────────────

    private String require(JsonNode args, String field) {
        JsonNode node = args.get(field);
        if (node == null || node.isNull() || node.asText().isBlank()) {
            throw new IllegalArgumentException("Required parameter missing: " + field);
        }
        return security.sanitiseString(node.asText());
    }

    private String strVal(JsonNode args, String field, String defaultVal) {
        JsonNode node = args.get(field);
        if (node == null || node.isNull()) return defaultVal;
        return security.sanitiseString(node.asText());
    }

    private int intVal(JsonNode args, String field, int defaultVal) {
        JsonNode node = args.get(field);
        if (node == null || node.isNull()) return defaultVal;
        try { return Integer.parseInt(node.asText()); }
        catch (NumberFormatException e) { return defaultVal; }
    }

    private long longVal(JsonNode args, String field, long defaultVal) {
        JsonNode node = args.get(field);
        if (node == null || node.isNull()) return defaultVal;
        try { return Long.parseLong(node.asText()); }
        catch (NumberFormatException e) { return defaultVal; }
    }

    private boolean bool(JsonNode args, String field, boolean defaultVal) {
        JsonNode node = args.get(field);
        if (node == null || node.isNull()) return defaultVal;
        return node.asBoolean(defaultVal);
    }

    private String extractToken(JsonNode params) {
        if (params.has("_auth")) return params.get("_auth").asText();
        if (params.has("meta") && params.get("meta").has("authorization")) {
            return params.get("meta").get("authorization").asText();
        }
        return System.getenv("MCP_API_TOKEN"); // fallback for trusted local use
    }

    private String sanitise(String s) {
        if (s == null) return "null";
        return s.replaceAll("[\\x00-\\x1F]", "").length() > 500
            ? s.substring(0, 500) : s;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Tool schema definitions (MCP tools/list)
    // ═══════════════════════════════════════════════════════════════════════

    record ToolDefinition(String name, String description, Map<String, Object> inputSchema) {
        JsonNode toJsonNode(ObjectMapper mapper) {
            return mapper.valueToTree(Map.of(
                "name", name,
                "description", description,
                "inputSchema", inputSchema
            ));
        }
    }

    private static final List<ToolDefinition> TOOL_DEFINITIONS = List.of(

        new ToolDefinition("list_topics",
            "List all Kafka topics in the cluster. Optionally filter to only Ecoskiller canonical topics " +
            "(user.created, gd.completed, invoice.generated, etc.)",
            Map.of("type", "object", "properties", Map.of(
                "ecoskiller_only", Map.of("type", "boolean", "description",
                    "If true, return only the 10 Ecoskiller spec-defined topics. Default: false.")
            ))),

        new ToolDefinition("create_topic",
            "Create a Kafka topic following Ecoskiller naming conventions. " +
            "Automatically applies spec-defined partition counts and retention policies for canonical topics. " +
            "Also creates a .dlq companion topic by default.",
            Map.of("type", "object", "required", List.of("topic_name"), "properties", Map.of(
                "topic_name",         Map.of("type", "string",  "description", "Topic name (e.g. gd.completed)"),
                "partitions",         Map.of("type", "integer", "description", "Partition count. Spec default used for canonical topics."),
                "replication_factor", Map.of("type", "integer", "description", "Replication factor. Default: 3 (spec minimum)."),
                "retention_ms",       Map.of("type", "integer", "description", "Retention in milliseconds. 0 = use spec default."),
                "create_dlq",         Map.of("type", "boolean", "description", "Also create {topic}.dlq companion. Default: true.")
            ))),

        new ToolDefinition("describe_topic",
            "Get full details for a Kafka topic: partition count, replication, ISR, leader, and key config values.",
            Map.of("type", "object", "required", List.of("topic_name"), "properties", Map.of(
                "topic_name", Map.of("type", "string", "description", "Topic name to describe")
            ))),

        new ToolDefinition("delete_topic",
            "Delete a Kafka topic. BLOCKED in production — use GitLab CI pipeline for production changes.",
            Map.of("type", "object", "required", List.of("topic_name"), "properties", Map.of(
                "topic_name",     Map.of("type", "string",  "description", "Topic name to delete"),
                "delete_dlq_also", Map.of("type", "boolean", "description", "Also delete the .dlq companion topic. Default: false.")
            ))),

        new ToolDefinition("list_consumer_groups",
            "List all Kafka consumer groups. Indicates which groups follow the Ecoskiller naming convention " +
            "({service-name}-{topic-name}-consumer).",
            Map.of("type", "object", "properties", Map.of())),

        new ToolDefinition("describe_consumer_group",
            "Describe a consumer group: state, coordinator, members, and assigned partitions.",
            Map.of("type", "object", "required", List.of("group_id"), "properties", Map.of(
                "group_id", Map.of("type", "string", "description",
                    "Consumer group ID (e.g. scoring-engine-gd.completed-consumer)")
            ))),

        new ToolDefinition("get_consumer_lag",
            "Get consumer lag per partition for a group+topic combination. " +
            "SLO alert threshold: lag > 5000 messages for > 10 minutes (Prometheus/Grafana).",
            Map.of("type", "object", "required", List.of("group_id", "topic_name"), "properties", Map.of(
                "group_id",   Map.of("type", "string", "description", "Consumer group ID"),
                "topic_name", Map.of("type", "string", "description", "Kafka topic name")
            ))),

        new ToolDefinition("publish_event",
            "Publish a single JSON event to a Kafka topic. For admin/test use. " +
            "Uses acks=all and idempotent producer for durability.",
            Map.of("type", "object", "required", List.of("topic_name", "value_json"), "properties", Map.of(
                "topic_name", Map.of("type", "string", "description", "Target topic"),
                "key",        Map.of("type", "string", "description", "Partition key (e.g. tenant_id or session_id)"),
                "value_json", Map.of("type", "string", "description", "JSON event payload string")
            ))),

        new ToolDefinition("list_dlq_topics",
            "List all Dead-Letter Queue topics (*.dlq). " +
            "Grafana alert fires when any DLQ accumulates > 10 messages in a 5-minute window.",
            Map.of("type", "object", "properties", Map.of())),

        new ToolDefinition("get_dlq_messages",
            "Read up to N messages from a DLQ topic for inspection. Read-only — does not commit offsets. " +
            "Use after a consumer failure to inspect poison-pill events before replaying.",
            Map.of("type", "object", "required", List.of("topic_name"), "properties", Map.of(
                "topic_name",   Map.of("type", "string",  "description", "Topic name or .dlq topic name"),
                "max_messages", Map.of("type", "integer", "description", "Max messages to return (1-100). Default: 20.")
            ))),

        new ToolDefinition("get_topic_offsets",
            "Get beginning and end offsets (and estimated message count) per partition for a topic.",
            Map.of("type", "object", "required", List.of("topic_name"), "properties", Map.of(
                "topic_name", Map.of("type", "string", "description", "Kafka topic name")
            ))),

        new ToolDefinition("provision_ecoskiller_topics",
            "Idempotent bootstrap: creates ALL 10 Ecoskiller canonical topics and their DLQ companions " +
            "with spec-correct partitions, retention, and replication. Safe to call multiple times.",
            Map.of("type", "object", "properties", Map.of(
                "replication_factor", Map.of("type", "integer", "description",
                    "Replication factor for all topics. Default: 3 (minimum for production).")
            ))),

        new ToolDefinition("get_cluster_info",
            "Get Kafka cluster metadata: broker list, cluster ID, controller, node count, deployment context.",
            Map.of("type", "object", "properties", Map.of())),

        new ToolDefinition("reset_consumer_offset",
            "Reset consumer group offsets for a topic (earliest = replay all, latest = skip to now). " +
            "Group must be INACTIVE (no active members) before resetting.",
            Map.of("type", "object", "required", List.of("group_id", "topic_name"), "properties", Map.of(
                "group_id",   Map.of("type", "string", "description", "Consumer group ID to reset"),
                "topic_name", Map.of("type", "string", "description", "Topic to reset offsets for"),
                "strategy",   Map.of("type", "string", "enum", List.of("earliest", "latest"),
                    "description", "earliest = replay from beginning, latest = skip to now. Default: earliest.")
            ))),

        new ToolDefinition("get_ecoskiller_topic_map",
            "Returns the complete Ecoskiller Kafka topic specification: all 10 topics with producers, " +
            "consumers, consumer group names, DLQ topics, partition keys, retention, and triggered actions.",
            Map.of("type", "object", "properties", Map.of()))
    );
}
