package com.ecoskiller.mcp.ips.tools;

import com.ecoskiller.mcp.ips.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * AnalyticsTools
 *
 * Advanced analytics operations:
 *   - compute_intelligence_dna    (LightGBM/XGBoost model inference)
 *   - detect_profile_anomaly      (outlier detection)
 *   - get_profile_history         (immutable event-sourced version log)
 */
public class AnalyticsTools {

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator security;
    private final Random rng = new Random(42L); // deterministic for demo

    public AnalyticsTools(SecurityValidator security) {
        this.security = security;
    }

    // ─────────────────────────────────────────────────────────────────────
    //  10. compute_intelligence_dna
    // ─────────────────────────────────────────────────────────────────────

    public JsonNode computeIntelligenceDna(JsonNode args, SecurityValidator.JwtClaims claims) {
        String  userId       = requireString(args, "userId");
        String  tenantId     = requireString(args, "tenantId");
        boolean fullRecompute = args.path("forceFullRecompute").asBoolean(false);

        validateUUID(userId,   "userId");
        validateUUID(tenantId, "tenantId");

        // Simulate LightGBM model inference
        // Production: call MLflow model registry for the latest deployed model
        long startMs = System.currentTimeMillis();

        ObjectNode dna = mapper.createObjectNode();
        // 8-dimension vector (each component computed by separate sub-model)
        dna.put("cognitive",       computeDimension(0.88, 0.04));   // +/- noise
        dna.put("behavioral",      computeDimension(0.78, 0.03));
        dna.put("domainExpertise", computeDimension(0.85, 0.02));
        dna.put("learningAgility", computeDimension(0.71, 0.05));
        dna.put("personality",     computeDimension(0.68, 0.02));
        dna.put("trajectory",      computeDimension(0.81, 0.03));
        dna.put("risk",            computeDimension(0.12, 0.01));
        dna.put("uniqueness",      computeDimension(0.73, 0.04));

        long inferenceMs = System.currentTimeMillis() - startMs;

        ObjectNode resp = mapper.createObjectNode();
        resp.put("userId",   userId);
        resp.put("tenantId", tenantId);
        resp.put("computeMode", fullRecompute ? "FULL_RECOMPUTE" : "INCREMENTAL");
        resp.set("intelligenceDNA", dna);
        resp.put("newVersion", 23);
        resp.put("previousVersion", 22);
        resp.put("modelUsed",  "lightgbm-v3.2.1");
        resp.put("modelRegistryUri", "mlflow://ecoskiller/ips-model/production");
        resp.put("eventsConsumed",   fullRecompute ? 147 : 3);
        resp.put("timeDecayApplied", true);
        resp.put("inferenceTimeMs",  inferenceMs);

        // Sub-model explanations (SHAP-style feature importance)
        ObjectNode explain = resp.putObject("featureImportance");
        explain.put("codingScore",         0.38);
        explain.put("problemSolvingSpeed", 0.22);
        explain.put("collaborationScore",  0.19);
        explain.put("learningVelocity",    0.12);
        explain.put("assessmentConsistency", 0.09);

        ArrayNode events = resp.putArray("kafkaEventsPublished");
        events.add("profile.updated");
        events.add("profile.enriched");

        resp.put("computedAt", Instant.now().toString());
        return successResponse("compute_intelligence_dna", resp);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  11. detect_profile_anomaly
    // ─────────────────────────────────────────────────────────────────────

    public JsonNode detectProfileAnomaly(JsonNode args, SecurityValidator.JwtClaims claims) {
        String userId    = requireString(args, "userId");
        String tenantId  = requireString(args, "tenantId");
        double threshold = args.path("sensitivityThreshold").asDouble(0.3);

        validateUUID(userId,   "userId");
        validateUUID(tenantId, "tenantId");

        if (threshold < 0 || threshold > 1)
            throw new IllegalArgumentException("sensitivityThreshold must be in [0,1]");

        // Anomaly detection model:
        // 1. Compare current vector against rolling 90-day history
        // 2. Compute z-score for each dimension
        // 3. Flag dimensions with |z-score| > 2.5
        // 4. Check assessment consistency (same skill, multiple sessions, variance)

        ObjectNode resp = mapper.createObjectNode();
        resp.put("userId",   userId);
        resp.put("tenantId", tenantId);
        resp.put("analysisThreshold", threshold);

        double anomalyScore = 0.05;  // Simulated — below threshold
        resp.put("anomalyScore", anomalyScore);
        resp.put("anomalyDetected", anomalyScore > threshold);

        ObjectNode dimAnalysis = resp.putObject("dimensionAnalysis");
        addDimCheck(dimAnalysis, "cognitive",       0.92, 0.88, 0.04, false);
        addDimCheck(dimAnalysis, "behavioral",      0.78, 0.76, 0.02, false);
        addDimCheck(dimAnalysis, "domainExpertise", 0.85, 0.83, 0.02, false);
        addDimCheck(dimAnalysis, "learningAgility", 0.71, 0.70, 0.01, false);
        addDimCheck(dimAnalysis, "risk",            0.12, 0.11, 0.01, false);

        ArrayNode checks = resp.putArray("consistencyChecks");
        addConsistencyCheck(checks, "Python",     new double[]{0.90, 0.91, 0.92, 0.93}, false);
        addConsistencyCheck(checks, "Kubernetes", new double[]{0.75, 0.77, 0.78},       false);

        resp.put("verdict",    "CLEAN");
        resp.put("reviewRequired", false);
        resp.put("analyzedAt", Instant.now().toString());

        return successResponse("detect_profile_anomaly", resp);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  13 (Analytics). get_profile_history
    // ─────────────────────────────────────────────────────────────────────

    public JsonNode getProfileHistory(JsonNode args, SecurityValidator.JwtClaims claims) {
        String userId    = requireString(args, "userId");
        String tenantId  = requireString(args, "tenantId");
        String fromTs    = args.path("fromTimestamp").asText(null);
        String toTs      = args.path("toTimestamp").asText(null);
        int    limit     = Math.min(args.path("limit").asInt(20), 100);

        validateUUID(userId,   "userId");
        validateUUID(tenantId, "tenantId");

        // Generate simulated immutable history (production: query profile_history table)
        ArrayNode history = mapper.createArrayNode();
        Instant now = Instant.now();

        String[] events = {
            "assessment.completed",
            "skill.assessment.result",
            "gd.discussion.completed",
            "assessment.completed",
            "learning.resource.interaction",
            "user.profile.updated"
        };
        String[] fields = {
            "cognitive_vector,domain_expertise_vector",
            "skill_vectors,peer_percentiles",
            "behavioral_vector",
            "cognitive_vector",
            "learning_agility_vector",
            "trajectory_vector"
        };

        for (int i = 0; i < Math.min(events.length, limit); i++) {
            ObjectNode snapshot = mapper.createObjectNode();
            snapshot.put("version",       22 - i);
            snapshot.put("sourceEvent",   events[i]);
            snapshot.put("changedFields", fields[i]);
            snapshot.put("timestamp",     now.minusSeconds((long)(i + 1) * 86400).toString());
            snapshot.put("sourceEventId", UUID.randomUUID().toString());

            ObjectNode vec = snapshot.putObject("vectorSnapshot");
            vec.put("cognitive",       0.92 - i * 0.01);
            vec.put("behavioral",      0.78 - i * 0.005);
            vec.put("domainExpertise", 0.85 - i * 0.008);

            history.add(snapshot);
        }

        ObjectNode resp = mapper.createObjectNode();
        resp.put("userId",   userId);
        resp.put("tenantId", tenantId);
        resp.set("history",  history);
        resp.put("totalVersions", 23);
        resp.put("returnedCount", history.size());
        resp.put("oldestEventAt", now.minusSeconds(180 * 86400L).toString()); // 6 months
        resp.put("queryAt",       Instant.now().toString());
        resp.put("note", "All history records are immutable. " +
                         "Source events are permanently stored in Kafka + profile_history table.");

        return successResponse("get_profile_history", resp);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────────────────────────────

    private double computeDimension(double base, double noise) {
        // Simulate model output with small variance
        double raw = base + (rng.nextDouble() - 0.5) * noise * 2;
        return Math.round(Math.max(0, Math.min(1, raw)) * 1000.0) / 1000.0;
    }

    private void addDimCheck(ObjectNode parent, String dim,
                              double current, double baseline, double delta, boolean flagged) {
        ObjectNode d = parent.putObject(dim);
        d.put("current",  current);
        d.put("baseline", baseline);
        d.put("delta",    delta);
        d.put("zScore",   delta / 0.05);  // simplified z-score
        d.put("flagged",  flagged);
    }

    private void addConsistencyCheck(ArrayNode arr, String skill, double[] sessions, boolean anomalous) {
        ObjectNode c = mapper.createObjectNode();
        c.put("skill", skill);
        double sum = 0; for (double s : sessions) sum += s;
        double avg = sum / sessions.length;
        double maxDiff = 0;
        for (double s : sessions) maxDiff = Math.max(maxDiff, Math.abs(s - avg));
        c.put("sessionCount",    sessions.length);
        c.put("averageScore",    Math.round(avg * 100) / 100.0);
        c.put("maxDeviation",    Math.round(maxDiff * 100) / 100.0);
        c.put("anomalous",       anomalous);
        arr.add(c);
    }

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

    private ObjectNode successResponse(String tool, ObjectNode data) {
        ObjectNode r = mapper.createObjectNode();
        r.put("tool",      tool);
        r.put("status",    "SUCCESS");
        r.put("timestamp", Instant.now().toString());
        r.set("data",      data);
        return r;
    }
}
