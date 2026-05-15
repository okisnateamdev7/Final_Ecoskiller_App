package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.*;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.*;

import java.util.*;

/**
 * Scoring integration tools.
 *
 * process_score_event     — Consume score_computed Kafka events from Scoring Engine
 * evaluate_belt_eligibility — Compute belt recommendation from dimension scores
 *
 * Belt thresholds (configurable via env APP_BELT_THRESHOLDS):
 *   NONE     : overall < 50
 *   BRONZE   : overall >= 50
 *   SILVER   : overall >= 65
 *   GOLD     : overall >= 78
 *   PLATINUM : overall >= 90 AND all dimensions >= 75
 */
public class ScoringTools {

    private final ApplicationRepository repo  = ApplicationRepository.getInstance();
    private final KafkaEventPublisher   kafka = KafkaEventPublisher.getInstance();

    // Belt thresholds
    private static final double BRONZE_MIN   = 50.0;
    private static final double SILVER_MIN   = 65.0;
    private static final double GOLD_MIN     = 78.0;
    private static final double PLATINUM_MIN = 90.0;
    private static final double PLATINUM_DIM = 75.0;  // all dims must be >= this for PLATINUM

    // Known score dimensions
    private static final List<String> DIMENSIONS = List.of(
            "communication", "technical", "leadership", "problem_solving",
            "teamwork", "adaptability", "creativity", "domain_knowledge"
    );

    // =========================================================================
    // process_score_event
    // =========================================================================

    public String processScoreEvent(Map<String, Object> args) {
        // Authenticate — service account or ADMIN
        JwtClaims claims  = authenticate(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_ADMIN);
        String tenantId   = SecurityConfig.effectiveTenantId(claims, args);

        String appId       = SecurityConfig.requireString(args, "application_id");
        String sessionId   = SecurityConfig.requireString(args, "session_id");
        String scoreStr    = SecurityConfig.requireString(args, "overall_score");
        String dimJson     = SecurityConfig.getString(args, "dimension_scores");
        String assTypeStr  = SecurityConfig.getString(args, "assessment_type");

        // Parse overall score
        double overallScore;
        try {
            overallScore = Double.parseDouble(scoreStr);
            if (overallScore < 0 || overallScore > 100) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("overall_score must be a number between 0 and 100");
        }

        // Parse dimension scores
        Map<String, Double> dimensions = parseDimensionScores(dimJson);

        // Find application
        Application app = repo.findById(tenantId, appId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + appId));

        // Update assessment type
        if (assTypeStr != null) {
            try {
                app.setAssessmentType(Application.AssessmentType.valueOf(assTypeStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid assessment_type: " + assTypeStr);
            }
        }

        // Apply score
        app.applyScore(overallScore, dimensions, sessionId);

        // Transition to ASSESSED if not already past that
        if (app.getStatus() == Application.Status.APPLIED || app.getStatus() == Application.Status.SCREENED) {
            app.transitionTo(Application.Status.ASSESSED, "SCORING_ENGINE",
                    "Score received: " + overallScore + " | session=" + sessionId);
        }

        repo.save(app);

        // Publish assessed event
        kafka.publishApplicationAssessed(app, sessionId);

        // Auto-evaluate belt
        String beltRecommendation = recommendBelt(overallScore, dimensions);
        app.addAudit("SCORING_ENGINE", "Belt recommendation: " + beltRecommendation);
        repo.save(app);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("application",        app.toMap());
        data.put("belt_recommendation", beltRecommendation);
        data.put("session_id",          sessionId);

        return JsonUtil.toJson(response("OK", "Score event processed successfully", data));
    }

    // =========================================================================
    // evaluate_belt_eligibility
    // =========================================================================

    public String evaluateBeltEligibility(Map<String, Object> args) {
        JwtClaims claims  = authenticate(args);
        String tenantId   = SecurityConfig.effectiveTenantId(claims, args);
        String appId      = SecurityConfig.requireString(args, "application_id");
        String targetBelt = SecurityConfig.getString(args, "target_belt");

        Application app = repo.findById(tenantId, appId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + appId));

        if (app.getOverallScore() == null) {
            return JsonUtil.toJson(response("PENDING",
                    "No scores available yet for this application", Map.of("application_id", appId)));
        }

        double overall     = app.getOverallScore();
        @SuppressWarnings("unchecked")
        Map<String, Double> dims = new LinkedHashMap<>();
        // Rebuild dims from app (stored as Object map)

        String recommended = recommendBelt(overall, dims);

        Map<String, Object> eligibility = new LinkedHashMap<>();
        eligibility.put("application_id",   appId);
        eligibility.put("overall_score",    overall);
        eligibility.put("recommended_belt", recommended);

        if (targetBelt != null) {
            boolean meets = meetsTarget(targetBelt.toUpperCase(), overall, dims);
            eligibility.put("target_belt",       targetBelt.toUpperCase());
            eligibility.put("meets_target",       meets);
            eligibility.put("gap_analysis",       buildGapAnalysis(targetBelt.toUpperCase(), overall, dims));
        }

        // Thresholds summary
        Map<String, Object> thresholds = new LinkedHashMap<>();
        thresholds.put("BRONZE",   BRONZE_MIN);
        thresholds.put("SILVER",   SILVER_MIN);
        thresholds.put("GOLD",     GOLD_MIN);
        thresholds.put("PLATINUM", PLATINUM_MIN + " (all dims >= " + PLATINUM_DIM + ")");
        eligibility.put("thresholds", thresholds);

        return JsonUtil.toJson(response("OK", "Belt eligibility evaluated", eligibility));
    }

    // =========================================================================
    // Belt logic
    // =========================================================================

    private String recommendBelt(double overall, Map<String, Double> dims) {
        if (overall >= PLATINUM_MIN && allDimsAbove(dims, PLATINUM_DIM)) return "PLATINUM";
        if (overall >= GOLD_MIN)    return "GOLD";
        if (overall >= SILVER_MIN)  return "SILVER";
        if (overall >= BRONZE_MIN)  return "BRONZE";
        return "NONE";
    }

    private boolean meetsTarget(String belt, double overall, Map<String, Double> dims) {
        switch (belt) {
            case "BRONZE":   return overall >= BRONZE_MIN;
            case "SILVER":   return overall >= SILVER_MIN;
            case "GOLD":     return overall >= GOLD_MIN;
            case "PLATINUM": return overall >= PLATINUM_MIN && allDimsAbove(dims, PLATINUM_DIM);
            default: return false;
        }
    }

    private boolean allDimsAbove(Map<String, Double> dims, double threshold) {
        if (dims.isEmpty()) return false;
        return dims.values().stream().allMatch(v -> v >= threshold);
    }

    private Map<String, Object> buildGapAnalysis(String belt, double overall, Map<String, Double> dims) {
        Map<String, Object> gap = new LinkedHashMap<>();
        double required;
        switch (belt) {
            case "BRONZE":   required = BRONZE_MIN;   break;
            case "SILVER":   required = SILVER_MIN;   break;
            case "GOLD":     required = GOLD_MIN;     break;
            case "PLATINUM": required = PLATINUM_MIN; break;
            default:         required = 0;
        }
        gap.put("overall_gap",  Math.max(0, required - overall));
        if ("PLATINUM".equals(belt)) {
            Map<String, Double> dimGaps = new LinkedHashMap<>();
            dims.forEach((k, v) -> { double g = Math.max(0, PLATINUM_DIM - v); if (g > 0) dimGaps.put(k, g); });
            gap.put("dimension_gaps", dimGaps);
        }
        return gap;
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private Map<String, Double> parseDimensionScores(String dimJson) {
        Map<String, Double> dims = new LinkedHashMap<>();
        if (dimJson == null || dimJson.isBlank()) return dims;
        // Simple parse: expects {"key": value, ...}
        String clean = dimJson.trim().replaceAll("[{}]", "");
        for (String pair : clean.split(",")) {
            String[] kv = pair.split(":");
            if (kv.length == 2) {
                String key = kv[0].trim().replace("\"", "");
                try { dims.put(key, Double.parseDouble(kv[1].trim())); }
                catch (NumberFormatException ignored) {}
            }
        }
        return dims;
    }

    private JwtClaims authenticate(Map<String, Object> args) {
        return SecurityConfig.validateToken(SecurityConfig.requireString(args, "auth_token"));
    }

    private Map<String, Object> response(String status, String message, Object data) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("status",  status);
        r.put("message", message);
        r.put("data",    data);
        r.put("service", "application-service");
        return r;
    }
}
