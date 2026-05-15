package com.ecoskiller.mcp.models;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Thread-safe in-memory repository for behavioral signals.
 * Production: ClickHouse (time-series) + PostgreSQL (audit) + Redis (cache).
 *
 * All queries are tenant-scoped.
 */
public class SignalRepository {

    private static final Logger LOG = Logger.getLogger(SignalRepository.class.getName());
    private static final SignalRepository INSTANCE = new SignalRepository();
    public static SignalRepository getInstance() { return INSTANCE; }

    // Raw signals: assessmentId → [signals]
    private final Map<String, List<SignalModels.BehaviorSignal>> rawSignals = new ConcurrentHashMap<>();
    // Aggregated signals: assessmentId → [aggregates]
    private final Map<String, List<SignalModels.AggregatedSignal>> aggregated = new ConcurrentHashMap<>();
    // Assessment configs: assessmentId → config map
    private final Map<String, Map<String, Object>> assessmentConfigs = new ConcurrentHashMap<>();
    // Kafka event log
    private final List<Map<String, Object>> kafkaLog = Collections.synchronizedList(new ArrayList<>());
    // Audit log (PostgreSQL in prod)
    private final List<Map<String, Object>> auditLog = Collections.synchronizedList(new ArrayList<>());
    // Metrics counters
    private final Map<String, Long> counters = new ConcurrentHashMap<>();
    // Fairness cohort store: tenantId → signalType → cohortName → [values]
    private final Map<String, Map<String, Map<String, List<Double>>>> cohortData = new ConcurrentHashMap<>();

    private SignalRepository() {}

    // ── Raw signal storage ────────────────────────────────────────────────────

    public void saveSignal(SignalModels.BehaviorSignal signal) {
        rawSignals.computeIfAbsent(signal.assessmentId, k -> Collections.synchronizedList(new ArrayList<>()))
                  .add(signal);
        inc("signals_total");
        if (signal.valid) inc("signals_valid"); else inc("signals_invalid");
    }

    public List<SignalModels.BehaviorSignal> getSignals(String tenantId, String assessmentId) {
        return rawSignals.getOrDefault(assessmentId, Collections.emptyList()).stream()
                .filter(s -> tenantId.equals(s.tenantId))
                .collect(Collectors.toList());
    }

    public List<SignalModels.BehaviorSignal> getSignalsByType(String tenantId, String assessmentId, String type) {
        return getSignals(tenantId, assessmentId).stream()
                .filter(s -> type.equalsIgnoreCase(s.signalType.name()))
                .collect(Collectors.toList());
    }

    public long countSignals(String tenantId, String assessmentId) {
        return getSignals(tenantId, assessmentId).size();
    }

    // ── Aggregated signal storage ─────────────────────────────────────────────

    public void saveAggregate(SignalModels.AggregatedSignal agg) {
        aggregated.computeIfAbsent(agg.assessmentId, k -> Collections.synchronizedList(new ArrayList<>()))
                  .add(agg);
        inc("aggregates_total");
    }

    public List<SignalModels.AggregatedSignal> getAggregates(String tenantId, String assessmentId) {
        return aggregated.getOrDefault(assessmentId, Collections.emptyList()).stream()
                .filter(a -> tenantId.equals(a.tenantId))
                .collect(Collectors.toList());
    }

    // ── Assessment config ─────────────────────────────────────────────────────

    public void saveConfig(String assessmentId, Map<String, Object> config) {
        assessmentConfigs.put(assessmentId, config);
    }

    public Optional<Map<String, Object>> getConfig(String assessmentId) {
        return Optional.ofNullable(assessmentConfigs.get(assessmentId));
    }

    // ── Kafka event log ───────────────────────────────────────────────────────

    public void logKafkaEvent(String topic, Map<String, Object> payload) {
        Map<String, Object> entry = new LinkedHashMap<>(payload);
        entry.put("kafka_topic", topic);
        entry.put("emitted_at",  Instant.now().toString());
        kafkaLog.add(entry);
        LOG.info("[KAFKA→" + topic + "] assessment=" + payload.getOrDefault("assessment_id","?"));
    }

    public List<Map<String, Object>> getKafkaLog(String tenantId, String assessmentId, int limit) {
        return kafkaLog.stream()
                .filter(e -> assessmentId == null || assessmentId.equals(e.get("assessment_id")))
                .filter(e -> tenantId == null || tenantId.equals(e.get("tenant_id")))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // ── Audit log ─────────────────────────────────────────────────────────────

    public void audit(String eventType, String assessmentId, String tenantId, String details, boolean success) {
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("event_type",   eventType);
        entry.put("assessment_id",assessmentId);
        entry.put("tenant_id",    tenantId);
        entry.put("details",      details);
        entry.put("success",      success);
        entry.put("timestamp",    Instant.now().toString());
        auditLog.add(entry);
    }

    public List<Map<String, Object>> getAuditLog(String tenantId, String assessmentId, int limit) {
        return auditLog.stream()
                .filter(e -> tenantId == null || tenantId.equals(e.get("tenant_id")))
                .filter(e -> assessmentId == null || assessmentId.equals(e.get("assessment_id")))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // ── Fairness cohort data ──────────────────────────────────────────────────

    public void addCohortDataPoint(String tenantId, String signalType, String cohortName, double value) {
        cohortData.computeIfAbsent(tenantId, k -> new ConcurrentHashMap<>())
                  .computeIfAbsent(signalType, k -> new ConcurrentHashMap<>())
                  .computeIfAbsent(cohortName, k -> Collections.synchronizedList(new ArrayList<>()))
                  .add(value);
    }

    public Map<String, List<Double>> getCohortData(String tenantId, String signalType) {
        return cohortData.getOrDefault(tenantId, Collections.emptyMap())
                         .getOrDefault(signalType, Collections.emptyMap());
    }

    // ── Metrics ───────────────────────────────────────────────────────────────

    public void inc(String counter) { counters.merge(counter, 1L, Long::sum); }
    public long get(String counter)  { return counters.getOrDefault(counter, 0L); }

    public Map<String, Object> getPrometheusMetrics(String tenantId) {
        long total   = get("signals_total");
        long valid   = get("signals_valid");
        long invalid = get("signals_invalid");
        long aggs    = get("aggregates_total");
        long anomalies = get("anomalies_detected");

        Map<String, Object> m = new LinkedHashMap<>();
        m.put("behavior_signals_ingested_total",           total);
        m.put("behavior_signals_valid_total",              valid);
        m.put("behavior_signals_invalid_total",            invalid);
        m.put("behavior_aggregates_computed_total",        aggs);
        m.put("behavior_anomaly_detection_rate",           total > 0 ? (double) anomalies / total : 0);
        m.put("behavior_signal_validity_rate",             total > 0 ? (double) valid / total : 0);
        m.put("behavior_signal_completeness_percent",      total > 0 ? Math.min(100.0, valid * 100.0 / Math.max(1, total)) : 0);
        m.put("behavior_kafka_events_published",           kafkaLog.size());
        m.put("behavior_audit_log_entries",                auditLog.size());
        return m;
    }
}
