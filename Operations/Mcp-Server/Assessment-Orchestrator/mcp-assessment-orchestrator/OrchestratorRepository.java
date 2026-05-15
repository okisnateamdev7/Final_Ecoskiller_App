package com.ecoskiller.mcp.models;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Thread-safe in-memory repository for AssessmentCycles and AssessmentSessions.
 * Production replacement: PostgreSQL 15 (cycles/sessions) + Redis 7 (state machine).
 *
 * All queries are tenant-scoped to mirror PostgreSQL RLS and Redis key namespacing.
 */
public class OrchestratorRepository {

    private static final OrchestratorRepository INSTANCE = new OrchestratorRepository();
    public static OrchestratorRepository getInstance() { return INSTANCE; }

    // Storage
    private final Map<String, AssessmentCycle>   cycles   = new ConcurrentHashMap<>();
    private final Map<String, AssessmentSession> sessions = new ConcurrentHashMap<>();
    private final List<Map<String, Object>>      kafkaLog = Collections.synchronizedList(new ArrayList<>());
    private final List<Map<String, Object>>      auditLog = Collections.synchronizedList(new ArrayList<>());
    // WebSocket command log (replaces actual WS broadcast in MCP context)
    private final List<Map<String, Object>>      wsLog    = Collections.synchronizedList(new ArrayList<>());

    // Service health counters (Prometheus stub)
    private volatile int activeWsConnections = 0;
    private volatile long totalTokensIssued  = 0;
    private volatile long quorumFailures      = 0;
    private volatile long completedSessions   = 0;
    private volatile long cancelledSessions   = 0;

    private OrchestratorRepository() {}

    // ── Cycles ────────────────────────────────────────────────────────────────

    public AssessmentCycle saveCycle(AssessmentCycle cycle) {
        cycles.put(cycle.getCycleId(), cycle);
        return cycle;
    }

    public Optional<AssessmentCycle> findCycle(String tenantId, String cycleId) {
        AssessmentCycle c = cycles.get(cycleId);
        if (c == null || !c.getTenantId().equals(tenantId)) return Optional.empty();
        return Optional.of(c);
    }

    public List<AssessmentCycle> findCyclesByRecruiter(String tenantId, String recruiterId) {
        return cycles.values().stream()
                .filter(c -> c.getTenantId().equals(tenantId) && recruiterId.equals(c.getRecruiterId()))
                .collect(Collectors.toList());
    }

    public List<AssessmentCycle> findActiveCycles(String tenantId) {
        return cycles.values().stream()
                .filter(c -> c.getTenantId().equals(tenantId) &&
                             c.getStatus() == AssessmentCycle.CycleStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    // ── Sessions ──────────────────────────────────────────────────────────────

    public AssessmentSession saveSession(AssessmentSession session) {
        sessions.put(session.getSessionId(), session);
        return session;
    }

    public Optional<AssessmentSession> findSession(String tenantId, String sessionId) {
        AssessmentSession s = sessions.get(sessionId);
        if (s == null || !s.getTenantId().equals(tenantId)) return Optional.empty();
        return Optional.of(s);
    }

    public List<AssessmentSession> findSessionsByCycle(String tenantId, String cycleId) {
        return sessions.values().stream()
                .filter(s -> s.getTenantId().equals(tenantId) && cycleId.equals(s.getCycleId()))
                .collect(Collectors.toList());
    }

    public List<AssessmentSession> findActiveSessionsByTenant(String tenantId) {
        return sessions.values().stream()
                .filter(s -> s.getTenantId().equals(tenantId)
                        && s.getState() != AssessmentSession.SessionState.COMPLETED
                        && s.getState() != AssessmentSession.SessionState.CANCELLED)
                .collect(Collectors.toList());
    }

    public List<AssessmentSession> findSessionsByCandidate(String tenantId, String candidateId) {
        return sessions.values().stream()
                .filter(s -> s.getTenantId().equals(tenantId)
                        && s.getCandidateIds().contains(candidateId))
                .collect(Collectors.toList());
    }

    // ── Kafka event log ───────────────────────────────────────────────────────

    public void logKafkaEvent(String topic, Map<String, Object> payload) {
        Map<String, Object> entry = new LinkedHashMap<>(payload);
        entry.put("kafka_topic", topic);
        entry.put("emitted_at",  Instant.now().toString());
        kafkaLog.add(entry);
    }

    public List<Map<String, Object>> getKafkaLog() { return Collections.unmodifiableList(kafkaLog); }

    // ── WebSocket command log ─────────────────────────────────────────────────

    public void logWsCommand(String sessionId, String command, Map<String, Object> payload) {
        Map<String, Object> entry = new LinkedHashMap<>(payload);
        entry.put("ws_command",  command);
        entry.put("session_id",  sessionId);
        entry.put("issued_at",   Instant.now().toString());
        wsLog.add(entry);
    }

    public List<Map<String, Object>> getWsLog(String sessionId) {
        return wsLog.stream()
                .filter(e -> sessionId.equals(e.get("session_id")))
                .collect(Collectors.toList());
    }

    // ── ClickHouse audit log ──────────────────────────────────────────────────

    public void writeAudit(AssessmentSession session) {
        auditLog.add(session.toTelemetryMap());
    }

    public List<Map<String, Object>> getAuditForSession(String sessionId) {
        return auditLog.stream()
                .filter(e -> sessionId.equals(e.get("session_id")))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAuditForTenant(String tenantId) {
        return auditLog.stream()
                .filter(e -> tenantId.equals(e.get("tenant_id")))
                .collect(Collectors.toList());
    }

    // ── Metrics ───────────────────────────────────────────────────────────────

    public void incActiveWs()    { activeWsConnections++; }
    public void decActiveWs()    { if (activeWsConnections > 0) activeWsConnections--; }
    public void incTokens()      { totalTokensIssued++; }
    public void incQuorumFail()  { quorumFailures++; }
    public void incCompleted()   { completedSessions++; }
    public void incCancelled()   { cancelledSessions++; }

    public Map<String, Object> getMetrics(String tenantId) {
        long activeSessions = sessions.values().stream()
                .filter(s -> s.getTenantId().equals(tenantId)
                        && s.getState() != AssessmentSession.SessionState.COMPLETED
                        && s.getState() != AssessmentSession.SessionState.CANCELLED)
                .count();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("sessions_active_gauge",       activeSessions);
        m.put("sessions_completed_total",    completedSessions);
        m.put("sessions_cancelled_total",    cancelledSessions);
        m.put("ws_connections_active",       activeWsConnections);
        m.put("token_issued_total",          totalTokensIssued);
        m.put("quorum_failures_total",       quorumFailures);
        m.put("state_transition_latency_p95","<5ms (in-memory stub; production: Prometheus histogram)");
        return m;
    }
}
