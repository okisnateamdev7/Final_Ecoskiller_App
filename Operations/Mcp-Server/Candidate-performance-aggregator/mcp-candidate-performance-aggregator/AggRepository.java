package com.ecoskiller.mcp.models;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Thread-safe in-memory repository.
 * Production: PostgreSQL 15 (RLS) + Redis 7 + ClickHouse (audit).
 * All queries are tenant-scoped.
 */
public class AggRepository {

    private static final Logger LOG = Logger.getLogger(AggRepository.class.getName());
    private static final AggRepository INSTANCE = new AggRepository();
    public static AggRepository getInstance() { return INSTANCE; }

    // Track scores: tenantId → (candidateId:cycleId → [TrackScore])
    private final Map<String, Map<String, List<AggModels.TrackScore>>>    trackScores   = new ConcurrentHashMap<>();
    // Aggregate records: tenantId → (candidateId:cycleId → AggregateRecord)
    private final Map<String, Map<String, AggModels.AggregateRecord>>     aggregates    = new ConcurrentHashMap<>();
    // Rubrics: tenantId → (jobRoleId → ScoringRubric)
    private final Map<String, Map<String, AggModels.ScoringRubric>>       rubrics       = new ConcurrentHashMap<>();
    // Cycle configs: cycleId → CycleConfig
    private final Map<String, AggModels.CycleConfig>                       cycleConfigs  = new ConcurrentHashMap<>();
    // DLQ: dlqId → DlqEntry
    private final Map<String, AggModels.DlqEntry>                          dlq           = new ConcurrentHashMap<>();
    // Kafka event log
    private final List<Map<String, Object>>                                kafkaLog      = Collections.synchronizedList(new ArrayList<>());
    // Audit log (ClickHouse in prod)
    private final List<Map<String, Object>>                                auditLog      = Collections.synchronizedList(new ArrayList<>());
    // Metrics counters
    private final Map<String, Long>                                        counters      = new ConcurrentHashMap<>();

    private AggRepository() {}

    // ── Track scores ──────────────────────────────────────────────────────────

    public void saveTrackScore(AggModels.TrackScore score) {
        String key = score.candidateId + ":" + score.cycleId;
        trackScores.computeIfAbsent(score.tenantId, k -> new ConcurrentHashMap<>())
                   .computeIfAbsent(key, k -> Collections.synchronizedList(new ArrayList<>()))
                   .add(score);
        inc("track_scores_total");
    }

    public List<AggModels.TrackScore> getTrackScores(String tenantId, String candidateId, String cycleId) {
        String key = candidateId + ":" + cycleId;
        return trackScores.getOrDefault(tenantId, Collections.emptyMap())
                          .getOrDefault(key, Collections.emptyList());
    }

    public Optional<AggModels.TrackScore> getTrackScore(String tenantId, String candidateId, String cycleId, AggModels.AssessmentTrack track) {
        return getTrackScores(tenantId, candidateId, cycleId).stream()
                .filter(s -> s.track == track && !s.duplicate)
                .findFirst();
    }

    // ── Aggregates ────────────────────────────────────────────────────────────

    public void saveAggregate(AggModels.AggregateRecord record) {
        aggregates.computeIfAbsent(record.tenantId, k -> new ConcurrentHashMap<>())
                  .put(record.candidateId + ":" + record.cycleId, record);
    }

    public Optional<AggModels.AggregateRecord> getAggregate(String tenantId, String candidateId, String cycleId) {
        return Optional.ofNullable(aggregates.getOrDefault(tenantId, Collections.emptyMap())
                .get(candidateId + ":" + cycleId));
    }

    public List<AggModels.AggregateRecord> getAggregates(String tenantId) {
        return new ArrayList<>(aggregates.getOrDefault(tenantId, Collections.emptyMap()).values());
    }

    // ── Rubrics ───────────────────────────────────────────────────────────────

    public void saveRubric(AggModels.ScoringRubric rubric) {
        rubrics.computeIfAbsent(rubric.tenantId, k -> new ConcurrentHashMap<>())
               .put(rubric.jobRoleId != null ? rubric.jobRoleId : "default", rubric);
    }

    public Optional<AggModels.ScoringRubric> getRubric(String tenantId, String jobRoleId) {
        Map<String, AggModels.ScoringRubric> tr = rubrics.getOrDefault(tenantId, Collections.emptyMap());
        AggModels.ScoringRubric r = tr.get(jobRoleId);
        if (r == null) r = tr.get("default");
        return Optional.ofNullable(r);
    }

    // ── Cycle configs ─────────────────────────────────────────────────────────

    public void saveCycleConfig(AggModels.CycleConfig config) {
        cycleConfigs.put(config.cycleId, config);
    }

    public Optional<AggModels.CycleConfig> getCycleConfig(String cycleId) {
        return Optional.ofNullable(cycleConfigs.get(cycleId));
    }

    // ── DLQ ──────────────────────────────────────────────────────────────────

    public void addDlqEntry(AggModels.DlqEntry entry) {
        dlq.put(entry.dlqId, entry);
        inc("dlq_events_total");
        LOG.warning("[DLQ] candidate=" + entry.candidateId + " cycle=" + entry.cycleId + " reason=" + entry.failureReason);
    }

    public List<AggModels.DlqEntry> getDlqEntries(String tenantId) {
        return dlq.values().stream()
                .filter(e -> tenantId == null || tenantId.equals(e.tenantId))
                .collect(Collectors.toList());
    }

    public Optional<AggModels.DlqEntry> getDlqEntry(String dlqId) {
        return Optional.ofNullable(dlq.get(dlqId));
    }

    public void updateDlqStatus(String dlqId, String status) {
        dlq.computeIfPresent(dlqId, (k, v) -> { v.status = status; return v; });
    }

    // ── Kafka event log ───────────────────────────────────────────────────────

    public void logKafkaEvent(String topic, Map<String,Object> payload) {
        Map<String,Object> entry = new LinkedHashMap<>(payload);
        entry.put("kafka_topic", topic);
        entry.put("emitted_at",  Instant.now().toString());
        kafkaLog.add(entry);
        LOG.info("[KAFKA→" + topic + "] candidate=" + payload.getOrDefault("candidate_id","?"));
    }

    public List<Map<String,Object>> getKafkaLog(String tenantId, int limit) {
        return kafkaLog.stream()
                .filter(e -> tenantId == null || tenantId.equals(e.get("tenant_id")))
                .limit(limit).collect(Collectors.toList());
    }

    // ── Audit log ─────────────────────────────────────────────────────────────

    public void audit(String eventType, String candidateId, String cycleId, String tenantId, String details, boolean success) {
        Map<String,Object> e = new LinkedHashMap<>();
        e.put("event_type",  eventType); e.put("candidate_id", candidateId);
        e.put("cycle_id",    cycleId);   e.put("tenant_id",    tenantId);
        e.put("details",     details);   e.put("success",      success);
        e.put("timestamp",   Instant.now().toString());
        auditLog.add(e);
    }

    public List<Map<String,Object>> getAuditLog(String tenantId, String candidateId, int limit) {
        return auditLog.stream()
                .filter(e -> tenantId == null || tenantId.equals(e.get("tenant_id")))
                .filter(e -> candidateId == null || candidateId.equals(e.get("candidate_id")))
                .limit(limit).collect(Collectors.toList());
    }

    // ── Metrics ───────────────────────────────────────────────────────────────

    public void inc(String counter) { counters.merge(counter, 1L, Long::sum); }
    public long get(String counter) { return counters.getOrDefault(counter, 0L); }

    public Map<String,Object> getMetrics(String tenantId) {
        List<AggModels.AggregateRecord> allAggs = getAggregates(tenantId);
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("events_consumed_total",       get("events_consumed_total"));
        m.put("aggregations_succeeded_total",get("aggregations_succeeded_total"));
        m.put("aggregations_failed_total",   get("aggregations_failed_total"));
        m.put("dlq_events_total",            get("dlq_events_total"));
        m.put("track_scores_total",          get("track_scores_total"));
        m.put("rubric_cache_hit_rate",       "0.87 (simulated)");
        m.put("processing_latency_p95",      "<200ms (target SLA)");
        m.put("kafka_consumer_lag",          "N/A (production: KafkaJS consumer group lag)");
        m.put("total_aggregates",            allAggs.size());
        Map<String,Long> byStatus = new LinkedHashMap<>();
        for (AggModels.AggStatus s : AggModels.AggStatus.values())
            byStatus.put(s.name(), allAggs.stream().filter(a -> a.status == s).count());
        m.put("aggregates_by_status", byStatus);
        Map<String,Long> byBelt = new LinkedHashMap<>();
        for (AggModels.BeltLevel b : AggModels.BeltLevel.values())
            byBelt.put(b.name(), allAggs.stream().filter(a -> a.beltEligibleLevel == b).count());
        m.put("belt_distribution", byBelt);
        return m;
    }
}
