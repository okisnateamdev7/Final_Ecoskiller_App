package com.ecoskiller.mcp.models;

import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

/**
 * Kafka Event Publisher stub.
 *
 * In production: replace with KafkaProducer<String, String> wired to:
 *   - Bootstrap servers from env APP_KAFKA_BROKERS
 *   - Apicurio Schema Registry for Avro/JSON schema enforcement
 *   - Topics with 3× replication factor
 *   - DLQ (dead-letter-queue) topics for failed deliveries
 *
 * Topics produced by Application-Service:
 *   job.applied              — candidate submitted application
 *   application.screened     — recruiter moved to SCREENED
 *   application.assessed     — score_computed processed
 *   application.rejected     — status changed to REJECTED
 *   application.hired        — status changed to HIRED
 */
public class KafkaEventPublisher {

    private static final Logger LOG = Logger.getLogger(KafkaEventPublisher.class.getName());

    // Singleton
    private static final KafkaEventPublisher INSTANCE = new KafkaEventPublisher();
    public static KafkaEventPublisher getInstance() { return INSTANCE; }

    // In-memory event log for audit / replay (production: Kafka + ClickHouse)
    private final List<Map<String, Object>> eventLog = Collections.synchronizedList(new ArrayList<>());

    private KafkaEventPublisher() {}

    // -------------------------------------------------------------------------
    // Domain events (one method per Kafka topic)
    // -------------------------------------------------------------------------

    public void publishJobApplied(Application app) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event_type",     "job.applied");
        payload.put("application_id", app.getApplicationId());
        payload.put("job_id",         app.getJobId());
        payload.put("candidate_id",   app.getCandidateId());
        payload.put("tenant_id",      app.getTenantId());
        payload.put("timestamp",      Instant.now().toString());
        emit("job.applied", payload);
    }

    public void publishApplicationScreened(Application app) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event_type",     "application.screened");
        payload.put("application_id", app.getApplicationId());
        payload.put("tenant_id",      app.getTenantId());
        payload.put("timestamp",      Instant.now().toString());
        emit("application.screened", payload);
    }

    public void publishApplicationAssessed(Application app, String sessionId) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event_type",       "application.assessed");
        payload.put("application_id",   app.getApplicationId());
        payload.put("overall_score",    app.getOverallScore());
        payload.put("assessment_type",  app.getAssessmentType().name());
        payload.put("session_id",       sessionId);
        payload.put("tenant_id",        app.getTenantId());
        payload.put("timestamp",        Instant.now().toString());
        emit("application.assessed", payload);
    }

    public void publishApplicationRejected(Application app) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event_type",       "application.rejected");
        payload.put("application_id",   app.getApplicationId());
        payload.put("rejection_reason", "see application record");
        payload.put("tenant_id",        app.getTenantId());
        payload.put("timestamp",        Instant.now().toString());
        emit("application.rejected", payload);
    }

    public void publishApplicationHired(Application app) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("event_type",     "application.hired");
        payload.put("application_id", app.getApplicationId());
        payload.put("job_id",         app.getJobId());
        payload.put("candidate_id",   app.getCandidateId());
        payload.put("tenant_id",      app.getTenantId());
        payload.put("timestamp",      Instant.now().toString());
        emit("application.hired", payload);
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    private void emit(String topic, Map<String, Object> payload) {
        payload.put("kafka_topic", topic);
        eventLog.add(payload);
        // Production: kafkaProducer.send(new ProducerRecord<>(topic, key, jsonPayload));
        LOG.info("[KAFKA→" + topic + "] " + payload.get("application_id"));
    }

    /** Returns the in-memory event log (for testing / audit). */
    public List<Map<String, Object>> getEventLog() {
        return Collections.unmodifiableList(eventLog);
    }
}
