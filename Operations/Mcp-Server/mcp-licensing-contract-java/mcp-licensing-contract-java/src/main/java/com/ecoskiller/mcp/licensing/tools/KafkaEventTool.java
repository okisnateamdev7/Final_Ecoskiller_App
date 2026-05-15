package com.ecoskiller.mcp.licensing.tools;

import com.ecoskiller.mcp.licensing.security.SecurityManager;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.Map;

public class KafkaEventTool extends BaseTool {

    private static final List<String> PUBLISHED_TOPICS = List.of(
        "licensing.contract.signed",
        "licensing.contract.amended",
        "licensing.contract.terminated",
        "licensing.contract.signature.timeout",
        "licensing.ownership.transferred",
        "document.generation.requested",
        "signature.requested",
        "archive.requested"
    );
    private static final List<String> CONSUMED_TOPICS = List.of(
        "idea.marketplace.license.intent",
        "document.generated",
        "document.signed",
        "innovation.trust.consent.approved",
        "licensing.ownership.transfer.due"
    );

    public KafkaEventTool(SecurityManager security) { super(security); }

    @Override
    public String execute(JSONObject args) throws Exception {
        return switch (require(args, "action")) {
            case "publish_event" -> publishEvent(args);
            case "inspect_dlq"   -> inspectDlq(args);
            case "list_topics"   -> listTopics(args);
            default -> throw new IllegalArgumentException("Unknown action. Supported: publish_event | inspect_dlq | list_topics");
        };
    }

    private String publishEvent(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_ADMIN);
        String topic      = security.sanitiseText(require(args, "topic"), "topic");
        String eventType  = security.sanitiseText(require(args, "event_type"), "event_type");
        String contractId = security.requireUuid(args.optString("contract_id", newUuid()), "contract_id");
        String tenantId   = security.sanitiseText(opt(args, "tenant_id", "default"), "tenant_id");
        String payload    = security.sanitiseText(opt(args, "payload_json", "{}"), "payload_json");

        if (!PUBLISHED_TOPICS.contains(topic)) {
            throw new IllegalArgumentException("topic must be one of the published topics: " + PUBLISHED_TOPICS);
        }

        LOG.info("[KafkaEvent] publish_event topic=" + topic + " contractId=" + contractId);

        // In production: KafkaProducer.send(topic, contractId, payload) with schema validation against Apicurio

        String messageId = newUuid();
        return ok(new JSONObject()
            .put("message_id",          messageId)
            .put("topic",               topic)
            .put("event_type",          eventType)
            .put("contract_id",         contractId)
            .put("tenant_id",           tenantId)
            .put("published_at",        nowIso())
            .put("schema_validated",    true)
            .put("schema_registry",     "Apicurio Schema Registry (self-hosted)")
            .put("partition_key",       contractId)
            .put("dlq_policy",          "Max 3 retries with exponential backoff; then *.dlq topic")
            .put("consumer_group",      "licensing-contract-service-" + topic + "-consumer")
            .put("simulation",          simulationNote()));
    }

    private String inspectDlq(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_ADMIN);
        String topic = security.sanitiseText(opt(args, "topic", "all"), "topic");

        JSONArray dlqEntries = new JSONArray()
            .put(new JSONObject()
                .put("dlq_topic",    "document.generated.dlq")
                .put("contract_id",  newUuid())
                .put("error",        "Legal Document Generation Service timeout after 3 retries")
                .put("retry_count",  3)
                .put("failed_at",    nowIso()));

        return ok(new JSONObject()
            .put("topic_filter",    topic)
            .put("dlq_entries",     dlqEntries)
            .put("total_dlq_count", dlqEntries.length())
            .put("action_required", "Manual review — check Legal Document Generation Service health")
            .put("simulation",      simulationNote()));
    }

    private String listTopics(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_VIEWER, SecurityManager.ROLE_ADMIN);

        JSONArray published = new JSONArray();
        PUBLISHED_TOPICS.forEach(t -> published.put(new JSONObject()
            .put("topic", t).put("direction", "PUBLISH").put("partitions", t.contains("signed") ? 6 : 3)));

        JSONArray consumed = new JSONArray();
        CONSUMED_TOPICS.forEach(t -> consumed.put(new JSONObject()
            .put("topic", t).put("direction", "CONSUME")
            .put("consumer_group", "licensing-contract-service-" + t + "-consumer")));

        return ok(new JSONObject()
            .put("published_topics", published)
            .put("consumed_topics",  consumed)
            .put("kafka_version",    "3.7.0")
            .put("schema_registry",  "Apicurio (self-hosted)")
            .put("dlq_policy",       "*.dlq — max 3 retries with exponential backoff")
            .put("simulation",       simulationNote()));
    }
}
