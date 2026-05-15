package com.ecoskiller.mcp.models;

import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

/**
 * Kafka event publisher stub.
 *
 * Production: KafkaJS producer with Apicurio Schema Registry validation.
 * Topics produced:
 *   gd.completed                  (6 partitions)
 *   interview.completed           (6 partitions)
 *   assessment.session.cancelled  (3 partitions)
 *   assessment.candidate.absent   (3 partitions)
 *   assessment.cycle.completed    (3 partitions)
 *   assessment.session.started    (6 partitions)
 */
public class KafkaEventPublisher {

    private static final Logger LOG = Logger.getLogger(KafkaEventPublisher.class.getName());
    private static final KafkaEventPublisher INSTANCE = new KafkaEventPublisher();
    public static KafkaEventPublisher getInstance() { return INSTANCE; }

    private final OrchestratorRepository repo = OrchestratorRepository.getInstance();

    private KafkaEventPublisher() {}

    // ── gd.completed ──────────────────────────────────────────────────────────

    public void publishGdCompleted(AssessmentSession session) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event_type",                "gd.completed");
        payload.put("session_id",                session.getSessionId());
        payload.put("cycle_id",                  session.getCycleId());
        payload.put("tenant_id",                 session.getTenantId());
        payload.put("track_type",                session.getTrackType().name());
        payload.put("candidate_roster",          session.getCandidateIds());
        payload.put("speaking_time_per_candidate", session.getSpeakingTimeMap());
        payload.put("idempotency_key",           session.getIdempotencyKey());
        payload.put("completed_at",              Instant.now().toString());
        emit("gd.completed", payload);
    }

    // ── interview.completed ───────────────────────────────────────────────────

    public void publishInterviewCompleted(AssessmentSession session) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event_type",      "interview.completed");
        payload.put("session_id",      session.getSessionId());
        payload.put("cycle_id",        session.getCycleId());
        payload.put("tenant_id",       session.getTenantId());
        payload.put("candidate_ids",   session.getCandidateIds());
        payload.put("idempotency_key", session.getIdempotencyKey());
        payload.put("completed_at",    Instant.now().toString());
        emit("interview.completed", payload);
    }

    // ── assessment.session.cancelled ──────────────────────────────────────────

    public void publishSessionCancelled(AssessmentSession session, String reason) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event_type",           "assessment.session.cancelled");
        payload.put("session_id",           session.getSessionId());
        payload.put("cycle_id",             session.getCycleId());
        payload.put("tenant_id",            session.getTenantId());
        payload.put("cancellation_reason",  reason);
        payload.put("affected_candidates",  session.getCandidateIds());
        payload.put("timestamp",            Instant.now().toString());
        emit("assessment.session.cancelled", payload);
    }

    // ── assessment.candidate.absent ───────────────────────────────────────────

    public void publishCandidateAbsent(AssessmentSession session, String candidateId) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event_type",   "assessment.candidate.absent");
        payload.put("candidate_id", candidateId);
        payload.put("session_id",   session.getSessionId());
        payload.put("cycle_id",     session.getCycleId());
        payload.put("tenant_id",    session.getTenantId());
        payload.put("track_type",   session.getTrackType().name());
        payload.put("scheduled_at", session.getScheduledAt().toString());
        payload.put("timestamp",    Instant.now().toString());
        emit("assessment.candidate.absent", payload);
    }

    // ── assessment.cycle.completed ────────────────────────────────────────────

    public void publishCycleCompleted(AssessmentCycle cycle, List<String> completedTracks) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event_type",       "assessment.cycle.completed");
        payload.put("cycle_id",         cycle.getCycleId());
        payload.put("tenant_id",        cycle.getTenantId());
        payload.put("job_id",           cycle.getJobId());
        payload.put("completed_tracks", completedTracks);
        payload.put("completion_time",  Instant.now().toString());
        emit("assessment.cycle.completed", payload);
    }

    // ── assessment.session.started ────────────────────────────────────────────

    public void publishSessionStarted(AssessmentSession session) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event_type",       "assessment.session.started");
        payload.put("session_id",       session.getSessionId());
        payload.put("cycle_id",         session.getCycleId());
        payload.put("tenant_id",        session.getTenantId());
        payload.put("track_type",       session.getTrackType().name());
        payload.put("participant_count", session.getParticipantCount());
        payload.put("started_at",       Instant.now().toString());
        emit("assessment.session.started", payload);
    }

    // ── Internal ──────────────────────────────────────────────────────────────

    private void emit(String topic, Map<String, Object> payload) {
        repo.logKafkaEvent(topic, payload);
        LOG.info("[KAFKA→" + topic + "] session=" + payload.getOrDefault("session_id", payload.get("cycle_id")));
    }
}
