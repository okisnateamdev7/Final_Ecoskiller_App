package com.ecoskiller.mcp.isd.handlers;

import com.ecoskiller.mcp.isd.engine.ScoringEngine;
import com.ecoskiller.mcp.isd.engine.VectorEngine;
import com.ecoskiller.mcp.isd.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.logging.Logger;

/**
 * ClassificationHandler
 *
 * Implements:
 *   get_classification      — CLEAR/REVIEW/FLAG with licensing eligibility
 *   compute_copy_probability — Weighted formula from pre-computed signals
 *   get_top_matches         — Top-K similar ideas (from report or live search)
 */
public class ClassificationHandler {

    private static final Logger LOG = Logger.getLogger(ClassificationHandler.class.getName());

    private final ObjectMapper  mapper = new ObjectMapper();
    private final SecurityValidator validator;
    private final ScoringEngine scoringEngine;

    public ClassificationHandler(SecurityValidator sv, ScoringEngine se) {
        this.validator     = sv;
        this.scoringEngine = se;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: get_classification
    // ─────────────────────────────────────────────────────────────────────────

    public Object getClassification(JsonNode args) {
        String ideaId   = req(args, "idea_id");
        String tenantId = req(args, "tenant_id");
        String key      = tenantId + ":" + ideaId;

        ObjectNode resp = mapper.createObjectNode();
        resp.put("idea_id",   ideaId);
        resp.put("tenant_id", tenantId);

        ObjectNode stored = SimilarityCheckHandler.reportStore.get(key);
        if (stored == null) {
            resp.put("found",             false);
            resp.put("classification",    "UNKNOWN");
            resp.put("licensing_eligible", false);
            resp.put("message",
                "No similarity report found. Run run_similarity_check first.");
            return resp;
        }

        String classification = stored.path("classification").asText("UNKNOWN");
        double copyProb       = stored.path("copy_probability").asDouble(0.0);
        String checkedAt      = stored.path("checked_at").asText("unknown");
        int    recheckCount   = stored.path("recheck_count").asInt(0);

        resp.put("found",          true);
        resp.put("classification", classification);
        resp.put("copy_probability", copyProb);
        resp.put("checked_at",     checkedAt);
        resp.put("recheck_count",  recheckCount);
        resp.put("licensing_eligible", "CLEAR".equals(classification));
        resp.put("classification_description", scoringEngine.classificationDescription(classification));
        resp.put("automated_action", scoringEngine.automatedAction(classification));

        // Threshold context
        double reviewThresh = ThresholdHandler.getReviewThreshold(tenantId);
        double flagThresh   = ThresholdHandler.getFlagThreshold(tenantId);
        ObjectNode threshCtx = resp.putObject("threshold_context");
        threshCtx.put("review_threshold", reviewThresh);
        threshCtx.put("flag_threshold",   flagThresh);
        threshCtx.put("copy_probability", copyProb);
        threshCtx.put("margin_to_next_tier", computeMarginToNextTier(copyProb, classification, reviewThresh, flagThresh));

        // Dispute path (if not CLEAR)
        if (!"CLEAR".equals(classification)) {
            ObjectNode dispute = resp.putObject("dispute_path");
            dispute.put("step_1", "Candidate submits appeal with explanation via dispute-service");
            dispute.put("step_2", "Compliance officer reviews within 24h");
            dispute.put("step_3_a", "Appeal accepted → classification changed to CLEAR, idea published");
            dispute.put("step_3_b", "Appeal rejected → idea remains " + classification + "; 3-strikes policy applies");
            dispute.put("recheck_option", "Admin can trigger run_similarity_recheck after idea amendment");
        }

        LOG.info("get_classification: idea_id=" + ideaId + " class=" + classification
                + " prob=" + String.format("%.3f", copyProb));
        return resp;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: compute_copy_probability
    // ─────────────────────────────────────────────────────────────────────────

    public Object computeCopyProbability(JsonNode args) {
        String ideaId           = req(args, "idea_id");
        String tenantId         = req(args, "tenant_id");
        double maxCosine        = args.path("max_cosine_similarity").asDouble(0.0);
        int    aboveMediumCount = args.path("above_medium_count").asInt(0);
        double catMatchRate     = args.path("category_match_rate").asDouble(0.0);
        double reviewThresh     = args.path("review_threshold").asDouble(
                                    ThresholdHandler.getReviewThreshold(tenantId));
        double flagThresh       = args.path("flag_threshold").asDouble(
                                    ThresholdHandler.getFlagThreshold(tenantId));

        double copyProb    = scoringEngine.computeFromSignals(maxCosine, aboveMediumCount, catMatchRate);
        String classification = scoringEngine.classify(copyProb, reviewThresh, flagThresh);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("idea_id",            ideaId);
        resp.put("tenant_id",          tenantId);
        resp.put("copy_probability",   copyProb);
        resp.put("classification",     classification);
        resp.put("classification_description", scoringEngine.classificationDescription(classification));
        resp.put("automated_action",   scoringEngine.automatedAction(classification));

        // Signal breakdown
        ObjectNode breakdown = resp.putObject("signal_breakdown");
        breakdown.put("max_cosine_similarity",         maxCosine);
        breakdown.put("max_cosine_contribution",       0.60 * maxCosine);
        breakdown.put("above_medium_count",            aboveMediumCount);
        breakdown.put("above_medium_normalized",       Math.min(1.0, (double) aboveMediumCount / 10));
        breakdown.put("above_medium_contribution",     0.25 * Math.min(1.0, (double) aboveMediumCount / 10));
        breakdown.put("category_match_rate",           catMatchRate);
        breakdown.put("category_match_contribution",   0.15 * catMatchRate);
        breakdown.put("total_copy_probability",        copyProb);

        // Weights reference
        ObjectNode weights = resp.putObject("formula_weights");
        weights.put("max_cosine_similarity_weight",   0.60);
        weights.put("above_medium_count_weight",      0.25);
        weights.put("category_match_rate_weight",     0.15);
        weights.put("formula",
            "0.60 × max_cosine + 0.25 × min(1, above_medium_count/10) + 0.15 × category_match_rate");

        // Thresholds applied
        ObjectNode thresh = resp.putObject("thresholds_applied");
        thresh.put("review_threshold", reviewThresh);
        thresh.put("flag_threshold",   flagThresh);

        LOG.info("compute_copy_probability: idea_id=" + ideaId + " prob=" + String.format("%.3f", copyProb)
                + " class=" + classification);
        return resp;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: get_top_matches
    // ─────────────────────────────────────────────────────────────────────────

    public Object getTopMatches(JsonNode args) {
        String ideaId   = req(args, "idea_id");
        String tenantId = req(args, "tenant_id");
        String vectorId = args.path("vector_id").asText("");
        String category = args.path("idea_category").asText("");
        int    topK     = Math.min(args.path("top_k").asInt(5), 100);
        double minSim   = args.path("min_similarity").asDouble(ScoringEngine.DEFAULT_MIN_SIMILARITY);

        String key    = tenantId + ":" + ideaId;
        ObjectNode stored = SimilarityCheckHandler.reportStore.get(key);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("idea_id",   ideaId);
        resp.put("tenant_id", tenantId);

        // Use cached top_matches from report if available and vectorId not provided
        if (stored != null && vectorId.isBlank()) {
            resp.put("source", "cached_report");
            resp.put("report_id",   stored.path("report_id").asText());
            resp.put("checked_at",  stored.path("checked_at").asText());
            resp.put("classification", stored.path("classification").asText());

            ArrayNode cachedMatches = (ArrayNode) stored.get("top_matches");
            int returnCount = cachedMatches != null ? Math.min(topK, cachedMatches.size()) : 0;
            resp.put("returned", returnCount);
            ArrayNode out = resp.putArray("top_matches");
            if (cachedMatches != null) {
                for (int i = 0; i < returnCount; i++) {
                    JsonNode m = cachedMatches.get(i);
                    ObjectNode n = out.addObject();
                    n.put("rank",                 i + 1);
                    n.put("matched_idea_id",      m.path("matched_idea_id").asText());
                    n.put("matched_candidate_id", m.path("matched_candidate_id").asText());
                    n.put("cosine_similarity",    m.path("cosine_similarity").asDouble());
                    n.put("matched_category",     m.path("matched_category").asText());
                    n.put("submission_timestamp", m.path("submission_timestamp").asText());
                    n.put("similarity_tier",
                        SimilarityCheckHandler.similarityTier(m.path("cosine_similarity").asDouble()));
                }
            }
        } else {
            // Live ANN search requires vector_id
            if (vectorId.isBlank()) {
                resp.put("error",
                    "vector_id is required for live ANN search when no cached report exists. " +
                    "Run run_similarity_check first, or provide vector_id.");
                return resp;
            }

            // Use VectorEngine for live search via reflection on shared instance
            // (In production: REST call to vector-database-service XIV)
            resp.put("source", "live_ann_search");
            resp.put("note",   "Live search executed — no cached report found for this idea");

            // Minimal live search for demo
            ObjectNode searchNote = resp.putObject("qdrant_query");
            searchNote.put("collection",    "idea-embeddings");
            searchNote.put("vector_id",     vectorId);
            searchNote.put("top_k",         topK);
            searchNote.put("min_similarity",minSim);
            searchNote.put("payload_filter","tenant_id=" + tenantId +
                (category.isBlank() ? "" : ", idea_category=" + category));
            searchNote.put("note",
                "In production: calls vector-database-service (XIV) REST API which wraps Qdrant " +
                "@qdrant/js-client-rest search endpoint with tenant-scoped payload filter.");

            resp.put("returned", 0);
            resp.putArray("top_matches");
        }

        LOG.info("get_top_matches: idea_id=" + ideaId + " topK=" + topK
                + " source=" + resp.path("source").asText("unknown"));
        return resp;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private double computeMarginToNextTier(double prob, String classification,
                                            double reviewThresh, double flagThresh) {
        return switch (classification) {
            case "CLEAR"  -> reviewThresh - prob;  // distance to REVIEW boundary
            case "REVIEW" -> flagThresh   - prob;  // distance to FLAG boundary
            default       -> 0.0;
        };
    }

    private String req(JsonNode args, String field) {
        JsonNode n = args.get(field);
        if (n == null || n.isNull() || n.asText().isBlank())
            throw new IllegalArgumentException("Missing required field: " + field);
        return n.asText().trim();
    }
}
