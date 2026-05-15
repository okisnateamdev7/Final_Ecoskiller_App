package com.ecoskiller.mcp.kafka.tools;

import com.ecoskiller.mcp.kafka.security.McpSecurityManager;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Implements all 15 Kafka MCP tools for the Ecoskiller platform.
 *
 * Topics defined in the Kafka Service Specification v1.0:
 *   user.created | job.applied | interview.completed | gd.completed
 *   match.scored | belt.eligible | invoice.generated | idea.submitted
 *   idea.fingerprinted | royalty.calculated
 *
 * DLQ convention:  {topic}.dlq
 * Consumer group:  {service-name}-{topic-name}-consumer
 * Environment prefix: dev.* / test.* / stage.* / (none for prod)
 */
public class KafkaTools {

    private static final Logger log = LoggerFactory.getLogger(KafkaTools.class);

    /** All production topics defined in the Ecoskiller Kafka specification */
    public static final List<String> ECOSKILLER_TOPICS = List.of(
        "user.created",
        "job.applied",
        "interview.completed",
        "gd.completed",
        "match.scored",
        "belt.eligible",
        "invoice.generated",
        "idea.submitted",
        "idea.fingerprinted",
        "royalty.calculated"
    );

    /** Partition counts per spec (3 = low-volume, 6 = high-volume) */
    private static final Map<String, Integer> TOPIC_PARTITIONS = Map.of(
        "user.created",         3,
        "job.applied",          6,
        "interview.completed",  6,
        "gd.completed",         6,
        "match.scored",         3,
        "belt.eligible",        3,
        "invoice.generated",    3,
        "idea.submitted",       3,
        "idea.fingerprinted",   3,
        "royalty.calculated",   3
    );

    /** Retention in milliseconds per spec */
    private static final Map<String, Long> TOPIC_RETENTION_MS = Map.of(
        "user.created",         7L  * 24 * 3600 * 1000,
        "job.applied",          30L * 24 * 3600 * 1000,
        "interview.completed",  30L * 24 * 3600 * 1000,
        "gd.completed",         30L * 24 * 3600 * 1000,
        "match.scored",         30L * 24 * 3600 * 1000,
        "belt.eligible",        30L * 24 * 3600 * 1000,
        "invoice.generated",    90L * 24 * 3600 * 1000,
        "idea.submitted",       30L * 24 * 3600 * 1000,
        "idea.fingerprinted",   30L * 24 * 3600 * 1000,
        "royalty.calculated",   90L * 24 * 3600 * 1000
    );

    private final McpSecurityManager security;
    private final Properties baseKafkaProps;
    private final String environment; // dev / test / stage / prod

    public KafkaTools(McpSecurityManager security) {
        this.security = security;
        this.baseKafkaProps = security.buildSecureKafkaProperties();
        this.environment = Optional.ofNullable(System.getenv("ECOSKILLER_ENV")).orElse("dev");
        log.info("KafkaTools initialised for environment={}", environment);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Tool 1: list_topics
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Lists all Kafka topics visible to this client.
     * Can optionally filter to only Ecoskiller canonical topics.
     *
     * @param ecoskillerOnly if true, returns only the 10 spec-defined topics
     * @return map of topic → partition count
     */
    public Map<String, Object> listTopics(boolean ecoskillerOnly) throws Exception {
        try (AdminClient admin = buildAdmin()) {
            ListTopicsOptions opts = new ListTopicsOptions().listInternal(false);
            Set<String> names = admin.listTopics(opts).names().get();

            if (ecoskillerOnly) {
                String prefix = topicPrefix();
                names = names.stream()
                    .filter(n -> ECOSKILLER_TOPICS.stream().anyMatch(t -> n.equals(prefix + t) || n.equals(t)))
                    .collect(Collectors.toSet());
            }

            Map<String, TopicDescription> descriptions =
                admin.describeTopics(new ArrayList<>(names)).allTopicNames().get();

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("environment", environment);
            result.put("topic_prefix", topicPrefix());
            result.put("total_topics", names.size());

            List<Map<String, Object>> topics = new ArrayList<>();
            for (Map.Entry<String, TopicDescription> e : descriptions.entrySet()) {
                Map<String, Object> t = new LinkedHashMap<>();
                t.put("name", e.getKey());
                t.put("partitions", e.getValue().partitions().size());
                t.put("replication_factor", e.getValue().partitions().isEmpty() ? 0
                    : e.getValue().partitions().get(0).replicas().size());
                t.put("is_dlq", e.getKey().endsWith(".dlq"));
                topics.add(t);
            }
            topics.sort(Comparator.comparing(m -> (String) m.get("name")));
            result.put("topics", topics);
            return result;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Tool 2: create_topic
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Creates a Kafka topic (and its .dlq companion) following Ecoskiller spec.
     * Uses the spec-defined partition count and retention policy if the topic
     * is a canonical Ecoskiller topic, otherwise uses caller-supplied values.
     */
    public Map<String, Object> createTopic(String topicName, int partitions, int replicationFactor,
                                            long retentionMs, boolean createDlq) throws Exception {
        security.validateTopicName(topicName);

        String prefixed = prefixedTopic(topicName);
        String dlqTopic = prefixed + ".dlq";

        // Use spec defaults for known topics
        if (TOPIC_PARTITIONS.containsKey(topicName)) {
            partitions = TOPIC_PARTITIONS.get(topicName);
        }
        if (TOPIC_RETENTION_MS.containsKey(topicName)) {
            retentionMs = TOPIC_RETENTION_MS.get(topicName);
        }
        if (partitions <= 0) partitions = 3;
        if (replicationFactor <= 0) replicationFactor = 3;
        if (retentionMs <= 0) retentionMs = 30L * 24 * 3600 * 1000;

        Map<String, String> topicConfig = new HashMap<>();
        topicConfig.put("retention.ms", String.valueOf(retentionMs));
        topicConfig.put("min.insync.replicas", "2");

        List<NewTopic> newTopics = new ArrayList<>();
        newTopics.add(new NewTopic(prefixed, partitions, (short) replicationFactor).configs(topicConfig));

        if (createDlq) {
            Map<String, String> dlqConfig = new HashMap<>();
            dlqConfig.put("retention.ms", String.valueOf(retentionMs * 2)); // DLQ kept longer
            dlqConfig.put("min.insync.replicas", "2");
            newTopics.add(new NewTopic(dlqTopic, 3, (short) replicationFactor).configs(dlqConfig));
        }

        try (AdminClient admin = buildAdmin()) {
            admin.createTopics(newTopics).all().get();
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "created");
        result.put("topic", prefixed);
        result.put("partitions", partitions);
        result.put("replication_factor", replicationFactor);
        result.put("retention_ms", retentionMs);
        result.put("retention_days", retentionMs / (24 * 3600 * 1000));
        if (createDlq) result.put("dlq_topic", dlqTopic);
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Tool 3: describe_topic
    // ═══════════════════════════════════════════════════════════════════════

    public Map<String, Object> describeTopic(String topicName) throws Exception {
        security.validateTopicName(topicName);
        String prefixed = prefixedTopic(topicName);

        try (AdminClient admin = buildAdmin()) {
            TopicDescription desc = admin.describeTopics(List.of(prefixed))
                .allTopicNames().get().get(prefixed);

            ConfigResource cr = new ConfigResource(ConfigResource.Type.TOPIC, prefixed);
            Config cfg = admin.describeConfigs(List.of(cr)).all().get().get(cr);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("topic", prefixed);
            result.put("partitions", desc.partitions().size());
            result.put("replication_factor", desc.partitions().isEmpty() ? 0
                : desc.partitions().get(0).replicas().size());
            result.put("is_internal", desc.isInternal());

            // Extract key config entries
            Map<String, String> configs = new LinkedHashMap<>();
            for (String key : List.of("retention.ms", "retention.bytes", "min.insync.replicas",
                                       "cleanup.policy", "compression.type", "max.message.bytes")) {
                ConfigEntry ce = cfg.get(key);
                if (ce != null) configs.put(key, ce.value());
            }
            result.put("config", configs);

            // Partition details
            List<Map<String, Object>> partitionInfo = new ArrayList<>();
            desc.partitions().forEach(pi -> {
                Map<String, Object> p = new LinkedHashMap<>();
                p.put("partition", pi.partition());
                p.put("leader", pi.leader() != null ? pi.leader().id() : -1);
                p.put("replicas", pi.replicas().stream().map(n -> n.id()).collect(Collectors.toList()));
                p.put("isr", pi.isr().stream().map(n -> n.id()).collect(Collectors.toList()));
                partitionInfo.add(p);
            });
            result.put("partition_details", partitionInfo);

            // Spec metadata
            if (TOPIC_RETENTION_MS.containsKey(topicName)) {
                result.put("spec_retention_days", TOPIC_RETENTION_MS.get(topicName) / (24 * 3600 * 1000));
            }
            return result;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Tool 4: delete_topic
    // ═══════════════════════════════════════════════════════════════════════

    public Map<String, Object> deleteTopic(String topicName, boolean deleteDlqAlso) throws Exception {
        security.validateTopicName(topicName);
        String prefixed = prefixedTopic(topicName);

        if (environment.equals("prod")) {
            throw new SecurityException("Topic deletion is BLOCKED in production environment. "
                + "Use GitLab CI pipeline for production topic changes.");
        }

        List<String> toDelete = new ArrayList<>();
        toDelete.add(prefixed);
        if (deleteDlqAlso) toDelete.add(prefixed + ".dlq");

        try (AdminClient admin = buildAdmin()) {
            admin.deleteTopics(toDelete).all().get();
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "deleted");
        result.put("deleted_topics", toDelete);
        result.put("environment", environment);
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Tool 5: list_consumer_groups
    // ═══════════════════════════════════════════════════════════════════════

    public Map<String, Object> listConsumerGroups() throws Exception {
        try (AdminClient admin = buildAdmin()) {
            Collection<ConsumerGroupListing> groups = admin.listConsumerGroups().all().get();

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("total", groups.size());

            List<Map<String, Object>> groupList = new ArrayList<>();
            for (ConsumerGroupListing g : groups) {
                Map<String, Object> entry = new LinkedHashMap<>();
                entry.put("group_id", g.groupId());
                entry.put("is_simple", g.isSimpleConsumerGroup());
                entry.put("matches_ecoskiller_convention",
                    g.groupId().matches("[a-z0-9\\-]+-[a-z0-9.\\-]+-consumer"));
                groupList.add(entry);
            }
            groupList.sort(Comparator.comparing(m -> (String) m.get("group_id")));
            result.put("groups", groupList);
            return result;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Tool 6: describe_consumer_group
    // ═══════════════════════════════════════════════════════════════════════

    public Map<String, Object> describeConsumerGroup(String groupId) throws Exception {
        security.validateConsumerGroup(groupId);

        try (AdminClient admin = buildAdmin()) {
            ConsumerGroupDescription desc = admin.describeConsumerGroups(List.of(groupId))
                .all().get().get(groupId);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("group_id", groupId);
            result.put("state", desc.state().toString());
            result.put("coordinator_id", desc.coordinator().id());
            result.put("partition_assignor", desc.partitionAssignor());
            result.put("member_count", desc.members().size());

            List<Map<String, Object>> members = new ArrayList<>();
            desc.members().forEach(m -> {
                Map<String, Object> member = new LinkedHashMap<>();
                member.put("member_id", m.consumerId());
                member.put("client_id", m.clientId());
                member.put("host", m.host());
                member.put("assigned_partitions", m.assignment().topicPartitions().stream()
                    .map(tp -> tp.topic() + ":" + tp.partition())
                    .collect(Collectors.toList()));
                members.add(member);
            });
            result.put("members", members);
            return result;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Tool 7: get_consumer_lag  (core SLO metric — alert > 5000 msgs)
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Returns consumer lag per partition for a group+topic combination.
     * SLO: alert fires when lag > 5000 for > 10 minutes (Prometheus / Grafana).
     */
    public Map<String, Object> getConsumerLag(String groupId, String topicName) throws Exception {
        security.validateConsumerGroup(groupId);
        security.validateTopicName(topicName);

        String prefixed = prefixedTopic(topicName);

        try (AdminClient admin = buildAdmin()) {
            // 1. Fetch committed offsets for this group
            Map<TopicPartition, OffsetAndMetadata> committed =
                admin.listConsumerGroupOffsets(groupId)
                    .partitionsToOffsetAndMetadata().get();

            // 2. Fetch end offsets (log-end-offset) using a throw-away consumer
            List<TopicPartition> partitions = committed.keySet().stream()
                .filter(tp -> tp.topic().equals(prefixed))
                .collect(Collectors.toList());

            if (partitions.isEmpty()) {
                // No committed offsets — try to enumerate partitions from topic description
                try {
                    TopicDescription desc = admin.describeTopics(List.of(prefixed))
                        .allTopicNames().get().get(prefixed);
                    if (desc != null) {
                        for (int i = 0; i < desc.partitions().size(); i++) {
                            partitions.add(new TopicPartition(prefixed, i));
                        }
                    }
                } catch (Exception ignored) {}
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("group_id", groupId);
            result.put("topic", prefixed);

            if (partitions.isEmpty()) {
                result.put("status", "no_offsets_found");
                result.put("lag_total", 0);
                return result;
            }

            Properties consumerProps = new Properties();
            consumerProps.putAll(baseKafkaProps);
            consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId + "-lag-inspector");
            consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

            Map<TopicPartition, Long> endOffsets;
            try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
                endOffsets = consumer.endOffsets(partitions);
            }

            long totalLag = 0;
            List<Map<String, Object>> partitionLags = new ArrayList<>();
            for (TopicPartition tp : partitions) {
                long end = endOffsets.getOrDefault(tp, 0L);
                OffsetAndMetadata oam = committed.get(tp);
                long committedOffset = oam != null ? oam.offset() : 0L;
                long lag = Math.max(0, end - committedOffset);
                totalLag += lag;

                Map<String, Object> pl = new LinkedHashMap<>();
                pl.put("partition", tp.partition());
                pl.put("committed_offset", committedOffset);
                pl.put("end_offset", end);
                pl.put("lag", lag);
                partitionLags.add(pl);
            }

            result.put("lag_total", totalLag);
            result.put("slo_threshold", 5000);
            result.put("slo_breached", totalLag > 5000);
            result.put("partition_lags", partitionLags);
            return result;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Tool 8: publish_event  (producer — for testing/admin use)
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Publishes a single JSON event to a Kafka topic.
     * Intended for admin / test use — not for high-throughput production publishing.
     */
    public Map<String, Object> publishEvent(String topicName, String key, String valueJson) throws Exception {
        security.validateTopicName(topicName);
        String prefixed = prefixedTopic(topicName);
        String sanitisedValue = security.sanitiseString(valueJson);

        Properties producerProps = new Properties();
        producerProps.putAll(baseKafkaProps);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.ACKS_CONFIG, "all");          // strongest durability
        producerProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        producerProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        producerProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps)) {
            ProducerRecord<String, String> record = new ProducerRecord<>(prefixed, key, sanitisedValue);
            RecordMetadata meta = producer.send(record).get();

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("status", "published");
            result.put("topic", meta.topic());
            result.put("partition", meta.partition());
            result.put("offset", meta.offset());
            result.put("timestamp", meta.timestamp());
            return result;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Tool 9: list_dlq_topics
    // ═══════════════════════════════════════════════════════════════════════

    public Map<String, Object> listDlqTopics() throws Exception {
        try (AdminClient admin = buildAdmin()) {
            Set<String> allTopics = admin.listTopics().names().get();
            List<String> dlqTopics = allTopics.stream()
                .filter(t -> t.endsWith(".dlq"))
                .sorted()
                .collect(Collectors.toList());

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("total_dlq_topics", dlqTopics.size());
            result.put("dlq_topics", dlqTopics);
            result.put("grafana_alert_threshold", "DLQ depth > 10 messages in 5 min window");
            return result;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Tool 10: get_dlq_messages  (inspect failed events without re-deploys)
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Reads up to maxMessages from a DLQ topic starting from offset 0.
     * Does NOT commit offsets — read-only inspection.
     */
    public Map<String, Object> getDlqMessages(String topicName, int maxMessages) throws Exception {
        String dlqTopic = topicName.endsWith(".dlq") ? topicName : topicName + ".dlq";
        security.validateTopicName(dlqTopic);

        if (maxMessages <= 0 || maxMessages > 100) maxMessages = 20;

        Properties consumerProps = new Properties();
        consumerProps.putAll(baseKafkaProps);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "mcp-dlq-inspector-" + UUID.randomUUID());
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        consumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxMessages);

        List<Map<String, Object>> messages = new ArrayList<>();

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
            consumer.subscribe(List.of(dlqTopic));
            consumer.poll(Duration.ofSeconds(5)).forEach(record -> {
                Map<String, Object> msg = new LinkedHashMap<>();
                msg.put("partition", record.partition());
                msg.put("offset", record.offset());
                msg.put("timestamp", record.timestamp());
                msg.put("key", record.key());
                msg.put("value", security.sanitiseString(record.value()));
                messages.add(msg);
            });
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("dlq_topic", dlqTopic);
        result.put("messages_returned", messages.size());
        result.put("replay_instruction",
            "To replay: use kafka-consumer-groups.sh --reset-offsets or admin-service replay API");
        result.put("messages", messages);
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Tool 11: get_topic_offsets  (end offsets per partition)
    // ═══════════════════════════════════════════════════════════════════════

    public Map<String, Object> getTopicOffsets(String topicName) throws Exception {
        security.validateTopicName(topicName);
        String prefixed = prefixedTopic(topicName);

        try (AdminClient admin = buildAdmin()) {
            TopicDescription desc = admin.describeTopics(List.of(prefixed))
                .allTopicNames().get().get(prefixed);

            List<TopicPartition> partitions = new ArrayList<>();
            for (int i = 0; i < desc.partitions().size(); i++) {
                partitions.add(new TopicPartition(prefixed, i));
            }

            Properties consumerProps = new Properties();
            consumerProps.putAll(baseKafkaProps);
            consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "mcp-offset-inspector");
            consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

            Map<TopicPartition, Long> beginOffsets;
            Map<TopicPartition, Long> endOffsets;
            try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
                beginOffsets = consumer.beginningOffsets(partitions);
                endOffsets = consumer.endOffsets(partitions);
            }

            List<Map<String, Object>> offsetInfo = new ArrayList<>();
            long totalMessages = 0;
            for (TopicPartition tp : partitions) {
                long begin = beginOffsets.getOrDefault(tp, 0L);
                long end = endOffsets.getOrDefault(tp, 0L);
                long count = Math.max(0, end - begin);
                totalMessages += count;

                Map<String, Object> p = new LinkedHashMap<>();
                p.put("partition", tp.partition());
                p.put("begin_offset", begin);
                p.put("end_offset", end);
                p.put("message_count", count);
                offsetInfo.add(p);
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("topic", prefixed);
            result.put("total_messages_estimated", totalMessages);
            result.put("partitions", offsetInfo);
            return result;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Tool 12: provision_ecoskiller_topics  (idempotent bootstrap)
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Creates ALL Ecoskiller canonical topics + their DLQs in one call.
     * Safe to call multiple times — skips topics that already exist.
     */
    public Map<String, Object> provisionEcoskillerTopics(int replicationFactor) throws Exception {
        if (replicationFactor <= 0) replicationFactor = 3;
        final int rf = replicationFactor;

        List<NewTopic> newTopics = new ArrayList<>();
        for (String topic : ECOSKILLER_TOPICS) {
            String prefixed = prefixedTopic(topic);
            String dlq = prefixed + ".dlq";
            int parts = TOPIC_PARTITIONS.getOrDefault(topic, 3);
            long retentionMs = TOPIC_RETENTION_MS.getOrDefault(topic, 30L * 24 * 3600 * 1000);

            Map<String, String> cfg = Map.of(
                "retention.ms", String.valueOf(retentionMs),
                "min.insync.replicas", "2"
            );
            Map<String, String> dlqCfg = Map.of(
                "retention.ms", String.valueOf(retentionMs * 2),
                "min.insync.replicas", "2"
            );

            newTopics.add(new NewTopic(prefixed, parts, (short) rf).configs(cfg));
            newTopics.add(new NewTopic(dlq, 3, (short) rf).configs(dlqCfg));
        }

        List<String> created = new ArrayList<>();
        List<String> alreadyExisted = new ArrayList<>();

        try (AdminClient admin = buildAdmin()) {
            CreateTopicsResult result = admin.createTopics(newTopics);
            for (NewTopic nt : newTopics) {
                try {
                    result.values().get(nt.name()).get();
                    created.add(nt.name());
                } catch (ExecutionException ex) {
                    if (ex.getCause() instanceof org.apache.kafka.common.errors.TopicExistsException) {
                        alreadyExisted.add(nt.name());
                    } else {
                        throw ex;
                    }
                }
            }
        }

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("environment", environment);
        out.put("replication_factor", replicationFactor);
        out.put("created", created);
        out.put("already_existed", alreadyExisted);
        out.put("total_topics_expected", newTopics.size());
        return out;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Tool 13: get_cluster_info
    // ═══════════════════════════════════════════════════════════════════════

    public Map<String, Object> getClusterInfo() throws Exception {
        try (AdminClient admin = buildAdmin()) {
            DescribeClusterResult cluster = admin.describeCluster();

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("cluster_id", cluster.clusterId().get());
            result.put("controller_id", cluster.controller().get().id());

            List<Map<String, Object>> nodes = new ArrayList<>();
            cluster.nodes().get().forEach(node -> {
                Map<String, Object> n = new LinkedHashMap<>();
                n.put("id", node.id());
                n.put("host", node.host());
                n.put("port", node.port());
                n.put("rack", node.rack());
                nodes.add(n);
            });
            result.put("brokers", nodes);
            result.put("broker_count", nodes.size());
            result.put("kafka_version", "3.7.0");
            result.put("metadata_mode", "KRaft");
            result.put("environment", environment);
            result.put("deployment", "k3s Data Worker Pool — GCP n2-standard-8 / AWS m5.2xlarge");
            return result;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Tool 14: reset_consumer_offset  (replay support)
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Resets the consumer group offset for a topic to the earliest position,
     * enabling full event replay. Group must be inactive (no active members).
     */
    public Map<String, Object> resetConsumerOffset(String groupId, String topicName,
                                                    String strategy) throws Exception {
        security.validateConsumerGroup(groupId);
        security.validateTopicName(topicName);

        String prefixed = prefixedTopic(topicName);

        // Verify group is inactive
        try (AdminClient admin = buildAdmin()) {
            ConsumerGroupDescription desc = admin.describeConsumerGroups(List.of(groupId))
                .all().get().get(groupId);

            if (!desc.members().isEmpty()) {
                throw new IllegalStateException(
                    "Cannot reset offsets: consumer group '" + groupId
                    + "' has " + desc.members().size() + " active member(s). "
                    + "Stop all consumers before resetting.");
            }

            TopicDescription topicDesc = admin.describeTopics(List.of(prefixed))
                .allTopicNames().get().get(prefixed);

            List<TopicPartition> partitions = new ArrayList<>();
            for (int i = 0; i < topicDesc.partitions().size(); i++) {
                partitions.add(new TopicPartition(prefixed, i));
            }

            Map<TopicPartition, RecordsToDelete> toDelete = null;
            Map<TopicPartition, OffsetAndMetadata> newOffsets = new LinkedHashMap<>();

            // Determine target offsets
            Properties consumerProps = new Properties();
            consumerProps.putAll(baseKafkaProps);
            consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

            try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
                Map<TopicPartition, Long> targetOffsets = "earliest".equalsIgnoreCase(strategy)
                    ? consumer.beginningOffsets(partitions)
                    : consumer.endOffsets(partitions);

                for (Map.Entry<TopicPartition, Long> e : targetOffsets.entrySet()) {
                    newOffsets.put(e.getKey(), new OffsetAndMetadata(e.getValue()));
                }
            }

            admin.alterConsumerGroupOffsets(groupId, newOffsets).all().get();

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("status", "reset_complete");
            result.put("group_id", groupId);
            result.put("topic", prefixed);
            result.put("strategy", strategy);
            result.put("partitions_reset", newOffsets.size());
            result.put("new_offsets", newOffsets.entrySet().stream()
                .collect(Collectors.toMap(
                    e -> e.getKey().topic() + ":" + e.getKey().partition(),
                    e -> e.getValue().offset()
                )));
            return result;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Tool 15: get_ecoskiller_topic_map  (full spec reference)
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Returns the full Ecoskiller topic specification map as defined in the
     * Kafka Service Specification v1.0 — including producers, consumers,
     * DLQ topics, partition keys, and retention policies.
     */
    public Map<String, Object> getEcoskillerTopicMap() {
        List<Map<String, Object>> topicMap = new ArrayList<>();

        topicMap.add(buildTopicSpec("user.created", "user-service", 3,
            List.of("notification-service", "analytics-service"),
            "tenant_id", "7 days",
            "Welcome email dispatch; user profile analytics entry"));

        topicMap.add(buildTopicSpec("job.applied", "application-service", 6,
            List.of("notification-service", "analytics-service", "billing-service"),
            "tenant_id", "30 days",
            "Email to recruiter; funnel stage update; usage meter increment"));

        topicMap.add(buildTopicSpec("interview.completed", "interview-service", 6,
            List.of("scoring-engine", "analytics-service"),
            "session_id", "30 days",
            "Multi-dimension score computation; funnel analytics update"));

        topicMap.add(buildTopicSpec("gd.completed", "gd-orchestrator", 6,
            List.of("scoring-engine", "analytics-service", "notification-service"),
            "session_id", "30 days",
            "Bulk GD score compute; results email; ClickHouse session analytics"));

        topicMap.add(buildTopicSpec("match.scored", "scoring-engine", 3,
            List.of("certification-engine", "analytics-service"),
            "candidate_id", "30 days",
            "Belt eligibility check; dojo performance record"));

        topicMap.add(buildTopicSpec("belt.eligible", "certification-engine", 3,
            List.of("notification-service", "admin-service"),
            "candidate_id", "30 days",
            "Mentor confirmation flow trigger; candidate notification"));

        topicMap.add(buildTopicSpec("invoice.generated", "billing-service", 3,
            List.of("notification-service"),
            "tenant_id", "90 days",
            "Invoice email to recruiter with PDF attachment via MinIO link"));

        topicMap.add(buildTopicSpec("idea.submitted", "innovation-service", 3,
            List.of("royalty-engine", "analytics-service"),
            "candidate_id", "30 days",
            "IP timestamping; similarity detection pipeline entry"));

        topicMap.add(buildTopicSpec("idea.fingerprinted", "innovation-service", 3,
            List.of("royalty-engine"),
            "idea_id", "30 days",
            "Similarity score routing; royalty calculation trigger"));

        topicMap.add(buildTopicSpec("royalty.calculated", "royalty-engine", 3,
            List.of("billing-service", "analytics-service"),
            "tenant_id", "90 days",
            "Double-entry royalty accounting flow; payout processing"));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("spec_version", "v1.0 — 2026");
        result.put("kafka_version", "3.7.0");
        result.put("replication_factor", 3);
        result.put("schema_registry", "Apicurio Registry (self-hosted)");
        result.put("dlq_policy", "3 retries → publish to {topic}.dlq");
        result.put("consumer_group_pattern", "{service-name}-{topic-name}-consumer");
        result.put("environment_prefix", topicPrefix().isEmpty() ? "(none — production)" : topicPrefix());
        result.put("topics", topicMap);
        return result;
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private Map<String, Object> buildTopicSpec(String topic, String producer, int partitions,
                                                List<String> consumers, String partitionKey,
                                                String retention, String action) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("topic", topic);
        m.put("dlq_topic", topic + ".dlq");
        m.put("producer", producer);
        m.put("partitions", partitions);
        m.put("consumers", consumers);
        m.put("consumer_groups", consumers.stream()
            .map(c -> c + "-" + topic + "-consumer")
            .collect(Collectors.toList()));
        m.put("partition_key", partitionKey);
        m.put("retention", retention);
        m.put("action_triggered", action);
        return m;
    }

    private String topicPrefix() {
        return switch (environment) {
            case "dev"   -> "dev.";
            case "test"  -> "test.";
            case "stage" -> "stage.";
            default      -> "";   // prod — no prefix per spec
        };
    }

    private String prefixedTopic(String topic) {
        String prefix = topicPrefix();
        return topic.startsWith(prefix) ? topic : prefix + topic;
    }

    private AdminClient buildAdmin() {
        Properties props = new Properties();
        props.putAll(baseKafkaProps);
        return AdminClient.create(props);
    }
}
