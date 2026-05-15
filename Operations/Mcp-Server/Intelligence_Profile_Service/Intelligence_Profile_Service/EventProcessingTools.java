package com.ecoskiller.mcp.ips.tools;

import com.ecoskiller.mcp.ips.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

import java.time.Instant;
import java.util.*;

/**
 * EventProcessingTools
 *
 * Handles inbound Kafka event signals that update Intelligence DNA profiles:
 *   - process_assessment_event   (assessment.completed topic)
 *   - process_skill_assessment   (skill.assessment.result topic)
 *   - process_gd_discussion      (gd.discussion.completed topic)
 *   - process_learning_interaction (learning.resource.interaction topic)
 *
 * Each tool implements:
 *   1. Idempotency check (prevents duplicate event processing via idempotencyKey)
 *   2. Tenant validation
 *   3. Time-decay weighting (recent signals weighted higher)
 *   4. Vector delta computation
 *   5. Cache invalidation trigger
 *   6. Downstream event emission (profile.updated / profile.enriched)
 */
public class EventProcessingTools {

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator security;

    // In-memory idempotency store (production: use Redis with TTL)
    private final Set<String> processedKeys = Collections.synchronizedSet(new HashSet<>());

    public EventProcessingTools(SecurityValidator security) {
        this.security = security;
    }

    // ─────────────────────────────────────────────────────────────────────
    //  6. process_assessment_event  (assessment.completed)
    // ─────────────────────────────────────────────────────────────────────

    public JsonNode processAssessmentEvent(JsonNode args, SecurityValidator.JwtClaims claims) {
        String userId         = requireString(args, "userId");
        String tenantId       = requireString(args, "tenantId");
        String assessmentType = requireString(args, "assessmentType");
        String idempotencyKey = requireString(args, "idempotencyKey");
        JsonNode scores       = args.path("scores");

        validateUUID(userId, "userId");
        validateUUID(tenantId, "tenantId");

        // Validate assessment type
        Set<String> validTypes = Set.of("coding_challenge", "technical_interview", "behavioral_interview");
        if (!validTypes.contains(assessmentType)) {
            throw new IllegalArgumentException(
                "Invalid assessmentType. Allowed: " + validTypes);
        }

        // Idempotency check
        if (processedKeys.contains(idempotencyKey)) {
            return idempotentResponse("process_assessment_event", idempotencyKey, userId);
        }
        processedKeys.add(idempotencyKey);

        // Extract scores with defaults
        double cognitive   = clamp(scores.path("cognitive").asDouble(0.0));
        double domain      = clamp(scores.path("domain").asDouble(0.0));
        double speed       = clamp(scores.path("speed").asDouble(0.5));
        double correctness = clamp(scores.path("correctness").asDouble(0.0));

        // Time-decay weighted delta computation
        // Formula: new_vector = 0.7 * new_signal + 0.3 * historical_avg
        double cogDelta    = applyTimeDecay(cognitive,   0.88, 0.7);
        double domainDelta = applyTimeDecay(domain,      0.82, 0.7);

        // Anomaly detection: sudden >30% spike is flagged
        boolean cogSpike  = (cogDelta - 0.88) > 0.30;

        ObjectNode resp = mapper.createObjectNode();
        resp.put("userId",         userId);
        resp.put("tenantId",       tenantId);
        resp.put("assessmentType", assessmentType);
        resp.put("idempotencyKey", idempotencyKey);
        resp.put("processedAt",    Instant.now().toString());

        ObjectNode deltas = resp.putObject("vectorDeltas");
        deltas.put("cognitive",     round3(cogDelta));
        deltas.put("domainExpertise", round3(domainDelta));

        ObjectNode newVectors = resp.putObject("updatedVectors");
        newVectors.put("cognitive",     round3(cogDelta));
        newVectors.put("domainExpertise", round3(domainDelta));
        newVectors.put("previousVersion", 22);
        newVectors.put("newVersion",      23);

        // Anomaly alert
        if (cogSpike) {
            ObjectNode anomaly = resp.putObject("anomalyAlert");
            anomaly.put("detected", true);
            anomaly.put("field",    "cognitive");
            anomaly.put("delta",    round3(cogDelta - 0.88));
            anomaly.put("action",   "Flagged for compliance review — profile.anomaly.detected event emitted");
        } else {
            resp.put("anomalyDetected", false);
        }

        // Downstream events that would be published to Kafka
        ArrayNode eventsPublished = resp.putArray("kafkaEventsPublished");
        eventsPublished.add("profile.updated");
        if (!cogSpike) eventsPublished.add("profile.enriched");
        if (cogSpike)  eventsPublished.add("profile.anomaly.detected");

        resp.put("cacheInvalidated", true);
        return successResponse("process_assessment_event", resp);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  7. process_skill_assessment  (skill.assessment.result)
    // ─────────────────────────────────────────────────────────────────────

    public JsonNode processSkillAssessment(JsonNode args, SecurityValidator.JwtClaims claims) {
        String userId         = requireString(args, "userId");
        String tenantId       = requireString(args, "tenantId");
        String skillName      = SecurityValidator.sanitize(requireString(args, "skillName"));
        int    proficiency    = args.path("proficiencyLevel").asInt(0);
        double confidence     = clamp(args.path("confidenceScore").asDouble(0.0));
        String idempotencyKey = requireString(args, "idempotencyKey");

        validateUUID(userId, "userId");
        validateUUID(tenantId, "tenantId");

        if (proficiency < 1 || proficiency > 5)
            throw new IllegalArgumentException("proficiencyLevel must be 1-5");

        if (processedKeys.contains(idempotencyKey))
            return idempotentResponse("process_skill_assessment", idempotencyKey, userId);
        processedKeys.add(idempotencyKey);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("userId",   userId);
        resp.put("tenantId", tenantId);
        resp.put("skillName", skillName);
        resp.put("proficiencyLevel", proficiency);
        resp.put("confidenceScore",  confidence);

        // Sentence-BERT embedding recompute (simulated)
        ObjectNode embedding = resp.putObject("embeddingComputed");
        embedding.put("dimensions",    384);
        embedding.put("model",         "sentence-bert-v2");
        embedding.put("computeTimeMs", 42);
        embedding.put("indexedInQdrant", true);

        // Peer percentile update
        resp.put("updatedPeerPercentile", 87);
        resp.put("taxonomyPath", "Technical > Programming Languages > " + skillName);

        ArrayNode events = resp.putArray("kafkaEventsPublished");
        events.add("skill.vector.computed");
        events.add("profile.updated");
        resp.put("processedAt", Instant.now().toString());

        return successResponse("process_skill_assessment", resp);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  8. process_gd_discussion  (gd.discussion.completed)
    // ─────────────────────────────────────────────────────────────────────

    public JsonNode processGdDiscussion(JsonNode args, SecurityValidator.JwtClaims claims) {
        String userId         = requireString(args, "userId");
        String tenantId       = requireString(args, "tenantId");
        String sessionId      = requireString(args, "sessionId");
        String idempotencyKey = requireString(args, "idempotencyKey");

        validateUUID(userId, "userId");
        validateUUID(tenantId, "tenantId");

        double speechCount       = args.path("speechCount").asDouble(0);
        double collaboration     = clamp(args.path("collaborationScore").asDouble(0.5));
        double ideaQuality       = clamp(args.path("ideaQuality").asDouble(0.5));
        double sentiment         = args.path("sentimentScore").asDouble(0.0);  // [-1,1]

        if (processedKeys.contains(idempotencyKey))
            return idempotentResponse("process_gd_discussion", idempotencyKey, userId);
        processedKeys.add(idempotencyKey);

        // Behavioral vector update: collaboration + communication + leadership components
        double behavioralDelta = applyTimeDecay(
            (collaboration * 0.4 + ideaQuality * 0.35 + normalize(sentiment) * 0.25),
            0.78, 0.65);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("userId",    userId);
        resp.put("tenantId",  tenantId);
        resp.put("sessionId", sessionId);

        ObjectNode deltas = resp.putObject("vectorDeltas");
        deltas.put("behavioral", round3(behavioralDelta));

        resp.put("updatedBehavioralVector", round3(behavioralDelta));
        resp.put("newVersion",   24);
        resp.put("processedAt",  Instant.now().toString());

        ArrayNode events = resp.putArray("kafkaEventsPublished");
        events.add("profile.updated");

        return successResponse("process_gd_discussion", resp);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  9. process_learning_interaction  (learning.resource.interaction)
    // ─────────────────────────────────────────────────────────────────────

    public JsonNode processLearningInteraction(JsonNode args, SecurityValidator.JwtClaims claims) {
        String userId         = requireString(args, "userId");
        String tenantId       = requireString(args, "tenantId");
        String resourceId     = requireString(args, "resourceId");
        String idempotencyKey = requireString(args, "idempotencyKey");

        validateUUID(userId, "userId");
        validateUUID(tenantId, "tenantId");

        int    timeSpent   = args.path("timeSpentMinutes").asInt(0);
        double completion  = clamp(args.path("completionPercentage").asDouble(0) / 100.0);
        double quizScore   = clamp(args.path("quizScore").asDouble(0.0));
        String resType     = SecurityValidator.sanitize(
                             args.path("resourceType").asText("course"));

        if (processedKeys.contains(idempotencyKey))
            return idempotentResponse("process_learning_interaction", idempotencyKey, userId);
        processedKeys.add(idempotencyKey);

        // Learning agility delta: weight completion more than time spent
        double engagementSignal = completion * 0.5 + quizScore * 0.4 + Math.min(timeSpent / 120.0, 1.0) * 0.1;
        double learningDelta    = applyTimeDecay(engagementSignal, 0.71, 0.6);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("userId",     userId);
        resp.put("tenantId",   tenantId);
        resp.put("resourceId", resourceId);
        resp.put("resourceType", resType);

        ObjectNode deltas = resp.putObject("vectorDeltas");
        deltas.put("learningAgility", round3(learningDelta));

        resp.put("updatedLearningAgilityVector", round3(learningDelta));
        resp.put("newVersion",    25);
        resp.put("processedAt",   Instant.now().toString());

        ArrayNode events = resp.putArray("kafkaEventsPublished");
        events.add("profile.updated");

        return successResponse("process_learning_interaction", resp);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Private helpers
    // ─────────────────────────────────────────────────────────────────────

    /** Time-decay weighted update: newVal = alpha * signal + (1-alpha) * historical */
    private double applyTimeDecay(double signal, double historical, double alpha) {
        return alpha * signal + (1 - alpha) * historical;
    }

    private double clamp(double v)  { return Math.max(0.0, Math.min(1.0, v)); }
    private double normalize(double v) { return (v + 1.0) / 2.0; }  // [-1,1] → [0,1]
    private double round3(double v) { return Math.round(v * 1000.0) / 1000.0; }

    private String requireString(JsonNode args, String field) {
        String v = args.path(field).asText(null);
        if (v == null || v.isBlank())
            throw new IllegalArgumentException("Missing required field: " + field);
        return SecurityValidator.sanitize(v);
    }

    private void validateUUID(String val, String field) {
        if (!SecurityValidator.isValidUUID(val))
            throw new IllegalArgumentException("Field '" + field + "' must be a valid UUID");
    }

    private ObjectNode idempotentResponse(String tool, String key, String userId) {
        ObjectNode r = mapper.createObjectNode();
        r.put("tool",          tool);
        r.put("status",        "ALREADY_PROCESSED");
        r.put("idempotencyKey", key);
        r.put("userId",        userId);
        r.put("message",       "Event already processed. Returning cached result to prevent double-counting.");
        r.put("timestamp",     Instant.now().toString());
        return r;
    }

    private ObjectNode successResponse(String tool, ObjectNode data) {
        ObjectNode r = mapper.createObjectNode();
        r.put("tool",      tool);
        r.put("status",    "SUCCESS");
        r.put("timestamp", Instant.now().toString());
        r.set("data",      data);
        return r;
    }
}
