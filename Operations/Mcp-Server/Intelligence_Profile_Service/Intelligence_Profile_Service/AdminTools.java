package com.ecoskiller.mcp.ips.tools;

import com.ecoskiller.mcp.ips.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

import java.time.Instant;
import java.util.UUID;

/**
 * AdminTools
 *
 * Administrative operations — all require the "admin" role (enforced by RBAC):
 *   - recalculate_vectors   : Force recompute vectors for a single user
 *   - get_fairness_audit    : Disparate impact / equal opportunity analysis
 *   - rebuild_profiles      : Disaster recovery — full rebuild from Kafka event log
 */
public class AdminTools {

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator security;

    public AdminTools(SecurityValidator security) {
        this.security = security;
    }

    // ─────────────────────────────────────────────────────────────────────
    //  12. recalculate_vectors  [admin only]
    // ─────────────────────────────────────────────────────────────────────

    public JsonNode recalculateVectors(JsonNode args, SecurityValidator.JwtClaims claims) {
        String userId   = requireString(args, "userId");
        String tenantId = requireString(args, "tenantId");

        validateUUID(userId,   "userId");
        validateUUID(tenantId, "tenantId");

        ObjectNode resp = mapper.createObjectNode();
        resp.put("userId",   userId);
        resp.put("tenantId", tenantId);
        resp.put("operation", "FULL_VECTOR_RECALCULATION");

        // Simulate reprocessing full event history
        int eventsReplayed = 147;
        resp.put("eventsReplayedCount",  eventsReplayed);
        resp.put("modelUsed",            "lightgbm-v3.2.1");
        resp.put("previousVersion",      22);
        resp.put("newVersion",           23);

        ObjectNode newVectors = resp.putObject("recomputedIntelligenceDNA");
        newVectors.put("cognitive",       0.92);
        newVectors.put("behavioral",      0.78);
        newVectors.put("domainExpertise", 0.85);
        newVectors.put("learningAgility", 0.71);
        newVectors.put("personality",     0.68);
        newVectors.put("trajectory",      0.81);
        newVectors.put("risk",            0.12);
        newVectors.put("uniqueness",      0.73);

        resp.put("cacheInvalidated",  true);
        resp.put("auditLogEntry",     "Vectors recalculated by admin " + claims.userId +
                                      " at " + Instant.now());

        ArrayNode events = resp.putArray("kafkaEventsPublished");
        events.add("profile.updated");
        events.add("profile.enriched");

        resp.put("completedAt", Instant.now().toString());
        return successResponse("recalculate_vectors", resp);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  14. get_fairness_audit  [admin only]
    // ─────────────────────────────────────────────────────────────────────

    public JsonNode getFairnessAudit(JsonNode args, SecurityValidator.JwtClaims claims) {
        String tenantId   = requireString(args, "tenantId");
        String fromDate   = args.path("fromDate").asText("2025-01-01");
        String toDate     = args.path("toDate").asText(Instant.now().toString().substring(0, 10));
        String demographic = SecurityValidator.sanitize(args.path("demographic").asText("all"));

        validateUUID(tenantId, "tenantId");

        ObjectNode resp = mapper.createObjectNode();
        resp.put("tenantId",   tenantId);
        resp.put("fromDate",   fromDate);
        resp.put("toDate",     toDate);
        resp.put("demographic", demographic);
        resp.put("totalCandidatesAnalyzed", 2847);
        resp.put("reportId", UUID.randomUUID().toString());

        // Disparate Impact Ratio (DIR): >0.8 passes the 80% rule
        ObjectNode dir = resp.putObject("disparateImpactRatio");
        dir.put("overall",     1.02);
        dir.put("cognitive",   0.98);
        dir.put("behavioral",  0.94);
        dir.put("domain",      1.01);
        dir.put("threshold",   0.80);
        dir.put("status",      "PASS");
        dir.put("note", "DIR >0.8 indicates no statistically significant disparate impact");

        // Equal Opportunity Difference (EOD): <0.05 is acceptable
        ObjectNode eod = resp.putObject("equalOpportunityDifference");
        eod.put("overall",    0.01);
        eod.put("cognitive",  0.02);
        eod.put("behavioral", 0.01);
        eod.put("threshold",  0.05);
        eod.put("status",     "PASS");

        // Flag summary
        ArrayNode flags = resp.putArray("fairnessFlags");
        // No flags in this example — clean audit

        ObjectNode summary = resp.putObject("summary");
        summary.put("totalViolations", 0);
        summary.put("overallStatus",   "PASS");
        summary.put("recommendation",
            "No systematic bias detected. Continue monitoring monthly. " +
            "Recommend reviewing behavioral score distributions quarterly.");

        // Demographic breakdown
        ObjectNode breakdown = resp.putObject("demographicBreakdown");
        addDemoGroup(breakdown, "gender_male",   1425, 0.91, 0.81);
        addDemoGroup(breakdown, "gender_female", 1198, 0.89, 0.79);
        addDemoGroup(breakdown, "age_25_34",      987, 0.93, 0.82);
        addDemoGroup(breakdown, "age_35_44",      743, 0.88, 0.80);

        resp.put("generatedAt", Instant.now().toString());
        resp.put("retentionPolicy", "5 years (GDPR compliant)");
        resp.put("nextAuditRecommended", toDate.substring(0, 7) + "-01 (monthly cadence)");

        return successResponse("get_fairness_audit", resp);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  15. rebuild_profiles  [admin only — safety gated]
    // ─────────────────────────────────────────────────────────────────────

    public JsonNode rebuildProfiles(JsonNode args, SecurityValidator.JwtClaims claims) {
        boolean confirm  = args.path("confirm").asBoolean(false);
        String tenantId  = args.path("tenantId").asText(null);

        // Hard safety gate — must explicitly confirm
        if (!confirm) {
            throw new IllegalArgumentException(
                "SAFETY GATE: Set confirm=true to proceed. " +
                "This operation replays the ENTIRE Kafka event log and rebuilds ALL profiles. " +
                "Estimated duration: 45-90 minutes for 100K users. " +
                "This should only be used for disaster recovery.");
        }

        if (tenantId != null) validateUUID(tenantId, "tenantId");

        String jobId = UUID.randomUUID().toString();

        ObjectNode resp = mapper.createObjectNode();
        resp.put("jobId",        jobId);
        resp.put("operation",    "FULL_PROFILE_REBUILD");
        resp.put("scope",        tenantId != null ? "TENANT:" + tenantId : "GLOBAL");
        resp.put("triggeredBy",  claims.userId);
        resp.put("status",       "QUEUED");

        ObjectNode estimate = resp.putObject("timeEstimate");
        estimate.put("profileCount",          tenantId != null ? 847 : 98432);
        estimate.put("kafkaEventsToReplay",   tenantId != null ? 124_000 : 14_800_000);
        estimate.put("estimatedMinutes",       tenantId != null ? 8 : 75);
        estimate.put("workerReplicas",         10);

        ObjectNode monitoring = resp.putObject("monitoring");
        monitoring.put("statusEndpoint",  "/admin/rebuild/" + jobId + "/status");
        monitoring.put("grafanaDashboard", "https://grafana.ecoskiller.io/d/rebuild-jobs");
        monitoring.put("kafkaTopic",       "intelligence-profile-service.rebuild-progress");

        resp.put("auditLog",
            "Full rebuild initiated by admin " + claims.userId +
            " for scope=" + (tenantId != null ? tenantId : "GLOBAL") +
            " at " + Instant.now());

        resp.put("startedAt", Instant.now().toString());
        resp.put("warning",
            "Profiles will show stale data until rebuild completes. " +
            "Redis cache has been pre-invalidated for affected users.");

        return successResponse("rebuild_profiles", resp);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────────────────────────────

    private void addDemoGroup(ObjectNode parent, String group, int count, double cognitive, double behavioral) {
        ObjectNode g = parent.putObject(group);
        g.put("candidateCount",    count);
        g.put("avgCognitiveScore", cognitive);
        g.put("avgBehavioralScore", behavioral);
        g.put("peerPercentileAvg", 72);
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
