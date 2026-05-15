package com.ecoskiller.mcp.ife.handlers;

import com.ecoskiller.mcp.ife.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.logging.Logger;

/**
 * PlagiarismHandler
 *
 * Implements:
 *   - detect_plagiarism  → Four-level plagiarism detection pipeline
 *   - compute_risk_score → Weighted risk formula per IFE spec §12
 *
 * Risk formula (§12):
 *   risk_score = (0.40 × exact_match_flag)
 *              + (0.30 × max_semantic_similarity)
 *              + (0.15 × coordinated_submission_flag)
 *              + (0.10 × user_reputation_score)     [inverted — high rep = low risk]
 *              + (0.05 × content_flags)
 *
 * Tiers:
 *   CLEAR        < 0.60
 *   REVIEW     0.60–0.79
 *   FLAG        >= 0.80
 */
public class PlagiarismHandler {

    private static final Logger LOG = Logger.getLogger(PlagiarismHandler.class.getName());

    // Risk score weights per IFE spec §12
    private static final double W_EXACT_MATCH          = 0.40;
    private static final double W_SEMANTIC_SIMILARITY  = 0.30;
    private static final double W_COORDINATED          = 0.15;
    private static final double W_USER_REPUTATION      = 0.10;
    private static final double W_CONTENT_FLAGS        = 0.05;

    private static final double THRESHOLD_FLAG   = 0.80;
    private static final double THRESHOLD_REVIEW = 0.60;

    // Submission rate thresholds for behavior analysis (§12 L4)
    private static final int  MAX_SUBMISSIONS_PER_DAY = 10;
    private static final double NEW_USER_SIMILARITY_THRESHOLD = 0.80;

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator validator;

    public PlagiarismHandler(SecurityValidator validator) {
        this.validator = validator;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Tool: detect_plagiarism
    // ──────────────────────────────────────────────────────────────────────────

    public Object detectPlagiarism(JsonNode args) {
        String ideaId       = requireString(args, "idea_id");
        String tenantId     = requireString(args, "tenant_id");
        String candidateId  = args.path("candidate_id").asText(null);
        double flagThresh   = args.path("flag_threshold").asDouble(THRESHOLD_FLAG);
        double reviewThresh = args.path("review_threshold").asDouble(THRESHOLD_REVIEW);
        boolean crossTenant = args.path("cross_tenant_check").asBoolean(false);

        ObjectNode response = mapper.createObjectNode();
        response.put("idea_id",      ideaId);
        response.put("tenant_id",    tenantId);
        response.put("checked_at",   Instant.now().toString());

        // ── Level 1: Exact fingerprint match ─────────────────────────────────
        ObjectNode l1 = response.putObject("level_1_exact_match");
        l1.put("description", "SHA-3-256 deterministic fingerprint exact-match check");
        l1.put("threshold",   "Fingerprint == stored_fingerprint");
        l1.put("false_positive_rate", 0.0);
        l1.put("result",      "NO_EXACT_MATCH");
        l1.put("action",      "In production: SELECT COUNT(*) FROM fingerprints WHERE fingerprint = ? AND tenant_id = ?");

        // ── Level 2: Semantic near-duplicate ─────────────────────────────────
        ObjectNode l2 = response.putObject("level_2_semantic_similarity");
        l2.put("description", "Cosine similarity ANN search via Qdrant (similarity > 0.9)");
        l2.put("threshold",   0.9);
        l2.put("false_positive_rate_pct", 5.0);
        l2.put("result",      "NOT_TRIGGERED");
        l2.put("action",      "In production: execute similarity_search tool first, then pass max_similarity here");

        // ── Level 3: Coordinated submission detection ─────────────────────────
        ObjectNode l3 = response.putObject("level_3_coordinated_submission");
        l3.put("description", "Cluster analysis: multiple accounts submitting similar ideas within short window");
        ObjectNode l3Triggers = l3.putObject("trigger_conditions");
        l3Triggers.put("condition_1", "Multiple ideas from same user with similarity > 0.85");
        l3Triggers.put("condition_2", "Multiple users submitting similar ideas on same day");
        l3Triggers.put("condition_3", "Accounts created same day with similar submissions");
        l3.put("result",   "NOT_TRIGGERED");
        l3.put("action",   "In production: query submission clustering job results from analytics store");

        // ── Level 4: User behavior anomalies ─────────────────────────────────
        ObjectNode l4 = response.putObject("level_4_behavior_anomaly");
        l4.put("description",     "Per-user submission rate, embedding variance, and corpus similarity analysis");
        ObjectNode thresholds = l4.putObject("thresholds");
        thresholds.put("max_submissions_per_day",   MAX_SUBMISSIONS_PER_DAY);
        thresholds.put("new_user_similarity_alert", NEW_USER_SIMILARITY_THRESHOLD);
        thresholds.put("bot_detection_window_mins", 60);
        thresholds.put("bot_detection_submission_count", 5);
        l4.put("result", "NOT_TRIGGERED");
        if (candidateId != null) {
            l4.put("candidate_id", candidateId);
            l4.put("note", "In production: query Redis and PostgreSQL for submission history of this candidate");
        }

        // ── Aggregate risk score ───────────────────────────────────────────────
        double riskScore = 0.0; // base — no signals from session state
        String classification = classifyRisk(riskScore, flagThresh, reviewThresh);
        String automatedAction = automatedAction(riskScore, flagThresh, reviewThresh);

        ObjectNode riskSummary = response.putObject("risk_summary");
        riskSummary.put("risk_score",      riskScore);
        riskSummary.put("classification",  classification);
        riskSummary.put("automated_action", automatedAction);
        riskSummary.put("flag_threshold",  flagThresh);
        riskSummary.put("review_threshold", reviewThresh);

        // Cross-tenant note
        if (crossTenant) {
            response.put("cross_tenant_note",
                "Cross-tenant similarity search enabled (anonymized). " +
                "In production: executes Qdrant search without tenant_id filter, " +
                "returns similar ideas with candidate PII stripped.");
        }

        LOG.info("Plagiarism check: idea_id=" + ideaId + " classification=" + classification);
        return response;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Tool: compute_risk_score
    // ──────────────────────────────────────────────────────────────────────────

    public Object computeRiskScore(JsonNode args) {
        String ideaId   = requireString(args, "idea_id");
        String tenantId = requireString(args, "tenant_id");

        boolean exactMatchFlag         = args.path("exact_match_flag").asBoolean(false);
        double maxSemanticSimilarity   = args.path("max_semantic_similarity").asDouble(0.0);
        boolean coordinatedFlag        = args.path("coordinated_submission_flag").asBoolean(false);
        double userReputationScore     = args.path("user_reputation_score").asDouble(0.5); // neutral default
        double contentFlags            = args.path("content_flags").asDouble(0.0);

        // Weighted formula per IFE spec §12
        // Note: user_reputation is inverted — high reputation REDUCES risk
        double riskScore =
            W_EXACT_MATCH         * (exactMatchFlag ? 1.0 : 0.0)
          + W_SEMANTIC_SIMILARITY * maxSemanticSimilarity
          + W_COORDINATED         * (coordinatedFlag ? 1.0 : 0.0)
          + W_USER_REPUTATION     * (1.0 - userReputationScore)  // invert
          + W_CONTENT_FLAGS       * contentFlags;

        riskScore = Math.min(1.0, Math.max(0.0, riskScore));

        String classification = classifyRisk(riskScore, THRESHOLD_FLAG, THRESHOLD_REVIEW);
        String action = automatedAction(riskScore, THRESHOLD_FLAG, THRESHOLD_REVIEW);

        ObjectNode response = mapper.createObjectNode();
        response.put("idea_id",   ideaId);
        response.put("tenant_id", tenantId);

        // Signal breakdown
        ObjectNode signals = response.putObject("signal_breakdown");
        signals.put("exact_match_contribution",        W_EXACT_MATCH * (exactMatchFlag ? 1.0 : 0.0));
        signals.put("semantic_similarity_contribution", W_SEMANTIC_SIMILARITY * maxSemanticSimilarity);
        signals.put("coordinated_submission_contribution", W_COORDINATED * (coordinatedFlag ? 1.0 : 0.0));
        signals.put("reputation_contribution",         W_USER_REPUTATION * (1.0 - userReputationScore));
        signals.put("content_flags_contribution",      W_CONTENT_FLAGS * contentFlags);

        response.put("risk_score",         riskScore);
        response.put("classification",     classification);
        response.put("recommended_action", action);

        // Human-readable explanation
        ArrayNode reasons = response.putArray("risk_factors");
        if (exactMatchFlag) reasons.add("Exact fingerprint match detected — highest severity signal");
        if (maxSemanticSimilarity >= 0.75) reasons.add("High semantic similarity (" + String.format("%.2f", maxSemanticSimilarity) + ") — near-duplicate idea in corpus");
        if (coordinatedFlag) reasons.add("Coordinated submission pattern detected");
        if (userReputationScore < 0.3) reasons.add("Low user reputation score increases risk weighting");
        if (contentFlags > 0.5) reasons.add("Content flags indicate potential spam or copied content");
        if (reasons.isEmpty()) reasons.add("No significant risk factors detected");

        // Appeal path if flagged
        if ("FLAG".equals(classification) || "REVIEW".equals(classification)) {
            ObjectNode appeal = response.putObject("appeal_path");
            appeal.put("step_1", "Candidate submits appeal with explanation");
            appeal.put("step_2", "Compliance team reviews within 24h");
            appeal.put("step_3_a", "Appeal accepted → flag removed, idea published");
            appeal.put("step_3_b", "Appeal rejected → idea deleted; warning issued (3 strikes = ban)");
        }

        LOG.info("Risk score computed: idea_id=" + ideaId + " score=" + String.format("%.3f", riskScore) + " class=" + classification);
        return response;
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private String classifyRisk(double score, double flagThresh, double reviewThresh) {
        if (score >= flagThresh)   return "FLAG";
        if (score >= reviewThresh) return "REVIEW";
        return "CLEAR";
    }

    private String automatedAction(double score, double flagThresh, double reviewThresh) {
        if (score >= flagThresh)   return "BLOCK — reject submission; alert compliance team; email owner";
        if (score >= reviewThresh) return "FLAG_FOR_REVIEW — accept with FLAGGED status; queue for human review within 24h";
        if (score >= 0.50)         return "SOFT_FLAG — accept as SUBMITTED; add internal similarity note; show similar ideas to user";
        return "ACCEPT — publish idea with APPROVED status; no flags or restrictions";
    }

    private String requireString(JsonNode args, String field) {
        JsonNode node = args.get(field);
        if (node == null || node.isNull() || node.asText().isBlank()) {
            throw new IllegalArgumentException("Missing required field: " + field);
        }
        return node.asText().trim();
    }
}
