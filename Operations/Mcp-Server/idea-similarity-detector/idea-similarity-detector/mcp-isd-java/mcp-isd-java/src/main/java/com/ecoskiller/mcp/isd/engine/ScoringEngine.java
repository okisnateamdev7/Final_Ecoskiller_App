package com.ecoskiller.mcp.isd.engine;

import java.util.List;

/**
 * ScoringEngine
 *
 * Implements the weighted copy-probability scoring formula and
 * CLEAR / REVIEW / FLAG three-tier classification logic.
 *
 * Per ISD spec §3.3:
 *   Primary input: maximum cosine similarity score from top-K ANN results
 *   Secondary inputs:
 *     - count of results above medium-similarity sub-threshold (0.60)
 *     - semantic category match rate among top-K results
 *
 * Formula (derived from spec):
 *   copy_probability = (
 *       0.60 * max_cosine_similarity
 *     + 0.25 * normalized_above_medium_count
 *     + 0.15 * category_match_rate
 *   )
 *   Clamped to [0.0, 1.0]
 *
 * Default thresholds (per spec §3.3, configurable per tenant via admin-service):
 *   CLEAR  : copy_probability < 0.60
 *   REVIEW : 0.60 <= copy_probability < 0.80
 *   FLAG   : copy_probability >= 0.80
 */
public class ScoringEngine {

    // Default classification thresholds (spec §3.3 / F2)
    public static final double DEFAULT_REVIEW_THRESHOLD = 0.60;
    public static final double DEFAULT_FLAG_THRESHOLD   = 0.80;

    // Minimum ANN score threshold to include a result (spec §5.3)
    public static final double DEFAULT_MIN_SIMILARITY   = 0.40;

    // Medium similarity sub-threshold for secondary input
    private static final double MEDIUM_SIMILARITY       = 0.60;

    // Maximum expected count for normalizing "above medium" signal
    private static final int    MAX_EXPECTED_ABOVE      = 10;

    // Scoring weights
    private static final double W_MAX_COSINE            = 0.60;
    private static final double W_ABOVE_MEDIUM_COUNT    = 0.25;
    private static final double W_CATEGORY_MATCH_RATE   = 0.15;

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Compute copy_probability from a list of ANN similarity matches.
     * Returns 0.0 if matches list is empty.
     */
    public double computeCopyProbability(
            List<VectorEngine.SimilarityMatch> matches,
            String queryCategory,
            double reviewThreshold,
            double flagThreshold) {

        if (matches == null || matches.isEmpty()) return 0.0;

        // Primary: max cosine similarity
        double maxCosine = matches.stream()
            .mapToDouble(VectorEngine.SimilarityMatch::cosineSimilarity)
            .max()
            .orElse(0.0);

        // Secondary 1: count of results above medium threshold
        long aboveMedium = matches.stream()
            .filter(m -> m.cosineSimilarity() >= MEDIUM_SIMILARITY)
            .count();
        double normalizedAbove = Math.min(1.0, (double) aboveMedium / MAX_EXPECTED_ABOVE);

        // Secondary 2: category match rate
        double categoryMatchRate = computeCategoryMatchRate(matches, queryCategory);

        // Weighted score
        double score = W_MAX_COSINE          * maxCosine
                     + W_ABOVE_MEDIUM_COUNT  * normalizedAbove
                     + W_CATEGORY_MATCH_RATE * categoryMatchRate;

        return Math.min(1.0, Math.max(0.0, score));
    }

    /**
     * Simplified score computation from pre-computed signal values
     * (used by compute_copy_probability tool).
     */
    public double computeFromSignals(
            double maxCosineSimilarity,
            int aboveMediumCount,
            double categoryMatchRate) {

        double normalizedAbove = Math.min(1.0, (double) aboveMediumCount / MAX_EXPECTED_ABOVE);
        double score = W_MAX_COSINE          * maxCosineSimilarity
                     + W_ABOVE_MEDIUM_COUNT  * normalizedAbove
                     + W_CATEGORY_MATCH_RATE * categoryMatchRate;
        return Math.min(1.0, Math.max(0.0, score));
    }

    /**
     * Three-tier classification (per ISD spec §3.3 / F2).
     * Thresholds are configurable per tenant; these are the defaults.
     */
    public String classify(double copyProbability, double reviewThreshold, double flagThreshold) {
        if (copyProbability >= flagThreshold)   return "FLAG";
        if (copyProbability >= reviewThreshold) return "REVIEW";
        return "CLEAR";
    }

    /** Fraction of top-K matches that share the query idea's category. */
    public double computeCategoryMatchRate(
            List<VectorEngine.SimilarityMatch> matches,
            String queryCategory) {

        if (matches == null || matches.isEmpty() || queryCategory == null || queryCategory.isBlank())
            return 0.0;
        long matches_same_cat = matches.stream()
            .filter(m -> queryCategory.equalsIgnoreCase(m.matchedCategory()))
            .count();
        return (double) matches_same_cat / matches.size();
    }

    /** Human-readable description of a classification tier. */
    public String classificationDescription(String tier) {
        return switch (tier) {
            case "CLEAR"  -> "copy_probability < 0.60 — idea appears sufficiently original. " +
                             "Eligible for marketplace listing and licensing.";
            case "REVIEW" -> "copy_probability 0.60–0.79 — borderline similarity detected. " +
                             "Queued for human compliance review within 24h. " +
                             "Candidate notified: submission under review.";
            case "FLAG"   -> "copy_probability >= 0.80 — high similarity detected. " +
                             "Dispute workflow initiated. Candidate notified of flag.";
            default       -> "Unknown classification tier: " + tier;
        };
    }

    /** Automated action triggered by classification (per ISD spec §3.4). */
    public String automatedAction(String tier) {
        return switch (tier) {
            case "CLEAR"  ->
                "Kafka: idea.similarity.cleared published → " +
                "idea-registry-service marks idea VERIFIED_ORIGINAL; " +
                "notification-service sends confirmation to candidate.";
            case "REVIEW" ->
                "Kafka: idea.similarity.review published → " +
                "admin-service queues for human review; " +
                "notification-service alerts candidate of pending status.";
            case "FLAG"   ->
                "Kafka: idea.similarity.flagged published → " +
                "dispute-service initiates dispute workflow; " +
                "admin-service governance queue updated; " +
                "notification-service alerts candidate of flag.";
            default -> "No automated action defined for tier: " + tier;
        };
    }
}
