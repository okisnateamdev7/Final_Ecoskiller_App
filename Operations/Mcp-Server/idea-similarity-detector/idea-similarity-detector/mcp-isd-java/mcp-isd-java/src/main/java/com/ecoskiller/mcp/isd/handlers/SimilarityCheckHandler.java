package com.ecoskiller.mcp.isd.handlers;

import com.ecoskiller.mcp.isd.engine.ScoringEngine;
import com.ecoskiller.mcp.isd.engine.VectorEngine;
import com.ecoskiller.mcp.isd.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

/**
 * SimilarityCheckHandler
 *
 * Implements:
 *   run_similarity_check   — Core ANN + scoring + classification + Kafka publish
 *   run_similarity_recheck — Idempotent re-execution with audit archiving
 *
 * Shared report store (production: PostgreSQL innovation.idea_similarity_reports + RLS)
 * is accessible by ReportHandler and ClassificationHandler via static field.
 */
public class SimilarityCheckHandler {

    private static final Logger LOG = Logger.getLogger(SimilarityCheckHandler.class.getName());

    private final ObjectMapper  mapper = new ObjectMapper();
    private final SecurityValidator validator;
    private final VectorEngine  vectorEngine;
    private final ScoringEngine scoringEngine;

    // Shared stores (production: PostgreSQL RLS-enforced tables)
    // key = tenantId:ideaId
    static final Map<String, ObjectNode>       reportStore = new LinkedHashMap<>();
    static final Map<String, List<ObjectNode>> auditStore  = new LinkedHashMap<>();

    public SimilarityCheckHandler(SecurityValidator sv, VectorEngine ve, ScoringEngine se) {
        this.validator     = sv;
        this.vectorEngine  = ve;
        this.scoringEngine = se;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: run_similarity_check
    // ─────────────────────────────────────────────────────────────────────────

    public Object runSimilarityCheck(JsonNode args) {
        String ideaId      = req(args, "idea_id");
        String tenantId    = req(args, "tenant_id");
        String vectorId    = req(args, "vector_id");
        String candidateId = args.path("candidate_id").asText("");
        String category    = args.path("idea_category").asText("");
        int    topK        = Math.min(args.path("top_k").asInt(20), 100);
        double minSim      = args.path("min_similarity").asDouble(ScoringEngine.DEFAULT_MIN_SIMILARITY);

        // Fetch thresholds (per-tenant; defaults used here)
        double reviewThresh = ThresholdHandler.getReviewThreshold(tenantId);
        double flagThresh   = ThresholdHandler.getFlagThreshold(tenantId);

        long start = System.currentTimeMillis();

        // ── Step 1: ANN search (per ISD spec §3.2) ─────────────────────────
        List<VectorEngine.SimilarityMatch> matches = vectorEngine.search(
            tenantId, vectorId, ideaId, category, topK, minSim);

        long qdrantMs = System.currentTimeMillis() - start;

        // ── Step 2: Score + classify (per ISD spec §3.3) ───────────────────
        double copyProb        = scoringEngine.computeCopyProbability(matches, category, reviewThresh, flagThresh);
        String classification  = scoringEngine.classify(copyProb, reviewThresh, flagThresh);
        double catMatchRate    = scoringEngine.computeCategoryMatchRate(matches, category);

        // ── Step 3: Persist report (per ISD spec §3.5) ─────────────────────
        String reportId   = UUID.randomUUID().toString();
        String checkedAt  = Instant.now().toString();
        String storeKey   = tenantId + ":" + ideaId;

        ObjectNode report = buildReport(reportId, tenantId, ideaId, candidateId, vectorId,
            copyProb, classification, matches, catMatchRate, checkedAt, 1, category);
        reportStore.put(storeKey, report);

        long totalMs = System.currentTimeMillis() - start;

        // ── Step 4: Build Kafka event payload (per ISD spec §6.1–6.2) ─────
        ObjectNode kafkaEvent = buildKafkaEvent(tenantId, ideaId, candidateId, copyProb,
            classification, matches, catMatchRate, checkedAt);

        // ── Build response ─────────────────────────────────────────────────
        ObjectNode resp = mapper.createObjectNode();
        resp.put("report_id",          reportId);
        resp.put("idea_id",            ideaId);
        resp.put("tenant_id",          tenantId);
        resp.put("vector_id",          vectorId);
        resp.put("copy_probability",   copyProb);
        resp.put("classification",     classification);
        resp.put("category_match_rate",catMatchRate);
        resp.put("k_results_count",    matches.size());
        resp.put("checked_at",         checkedAt);
        resp.put("qdrant_query_ms",    qdrantMs);
        resp.put("total_check_ms",     totalMs);

        resp.put("classification_description", scoringEngine.classificationDescription(classification));
        resp.put("automated_action",           scoringEngine.automatedAction(classification));

        // Top 5 matches (per spec §6.2: top_matches array max 5)
        ArrayNode topMatches = resp.putArray("top_matches");
        matches.stream().limit(5).forEach(m -> {
            ObjectNode n = topMatches.addObject();
            n.put("matched_idea_id",        m.matchedIdeaId());
            n.put("matched_candidate_id",   m.matchedCandidateId());
            n.put("matched_title",          m.matchedTitle());
            n.put("cosine_similarity",      m.cosineSimilarity());
            n.put("matched_category",       m.matchedCategory());
            n.put("submission_timestamp",   m.submissionTimestamp());
            n.put("similarity_tier",        similarityTier(m.cosineSimilarity()));
        });

        resp.set("kafka_event_published", kafkaEvent);

        // Score breakdown (transparency)
        ObjectNode scoreBreakdown = resp.putObject("score_breakdown");
        double maxCosine = matches.stream().mapToDouble(VectorEngine.SimilarityMatch::cosineSimilarity)
                                          .max().orElse(0.0);
        long aboveMedium = matches.stream().filter(m -> m.cosineSimilarity() >= 0.60).count();
        scoreBreakdown.put("max_cosine_similarity",    maxCosine);
        scoreBreakdown.put("above_medium_count",       aboveMedium);
        scoreBreakdown.put("category_match_rate",      catMatchRate);
        scoreBreakdown.put("w_max_cosine",             0.60);
        scoreBreakdown.put("w_above_medium_count",     0.25);
        scoreBreakdown.put("w_category_match_rate",    0.15);
        scoreBreakdown.put("review_threshold_applied", reviewThresh);
        scoreBreakdown.put("flag_threshold_applied",   flagThresh);
        scoreBreakdown.put("corpus_size_searched",     vectorEngine.corpusSize(tenantId));

        LOG.info("run_similarity_check: idea_id=" + ideaId + " classification=" + classification
                + " copy_prob=" + String.format("%.3f", copyProb) + " matches=" + matches.size()
                + " total_ms=" + totalMs);
        return resp;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: run_similarity_recheck
    // ─────────────────────────────────────────────────────────────────────────

    public Object runSimilarityRecheck(JsonNode args) {
        String ideaId        = req(args, "idea_id");
        String tenantId      = req(args, "tenant_id");
        String vectorId      = req(args, "vector_id");
        String category      = args.path("idea_category").asText("");
        String recheckReason = args.path("recheck_reason").asText("ADMIN_REQUEST");
        int    topK          = Math.min(args.path("top_k").asInt(20), 100);

        String storeKey = tenantId + ":" + ideaId;

        // Archive previous report (per ISD spec §3.7)
        ObjectNode previous = reportStore.get(storeKey);
        if (previous != null) {
            auditStore.computeIfAbsent(storeKey, k -> new ArrayList<>())
                      .add(previous.deepCopy());
        }

        int recheckCount = (previous != null ? previous.path("recheck_count").asInt(0) : 0) + 1;

        // Run fresh ANN search
        double reviewThresh = ThresholdHandler.getReviewThreshold(tenantId);
        double flagThresh   = ThresholdHandler.getFlagThreshold(tenantId);
        double minSim       = ScoringEngine.DEFAULT_MIN_SIMILARITY;

        long start = System.currentTimeMillis();
        List<VectorEngine.SimilarityMatch> matches = vectorEngine.search(
            tenantId, vectorId, ideaId, category, topK, minSim);
        long qdrantMs = System.currentTimeMillis() - start;

        double copyProb       = scoringEngine.computeCopyProbability(matches, category, reviewThresh, flagThresh);
        String classification = scoringEngine.classify(copyProb, reviewThresh, flagThresh);
        double catMatchRate   = scoringEngine.computeCategoryMatchRate(matches, category);

        String reportId  = UUID.randomUUID().toString();
        String checkedAt = Instant.now().toString();

        // Store new report (overwrite with incremented recheck_count)
        ObjectNode report = buildReport(reportId, tenantId, ideaId,
            previous != null ? previous.path("candidate_id").asText("") : "",
            vectorId, copyProb, classification, matches, catMatchRate, checkedAt, recheckCount, category);
        reportStore.put(storeKey, report);

        long totalMs = System.currentTimeMillis() - start;

        // Build response
        ObjectNode resp = mapper.createObjectNode();
        resp.put("report_id",          reportId);
        resp.put("idea_id",            ideaId);
        resp.put("tenant_id",          tenantId);
        resp.put("recheck_count",      recheckCount);
        resp.put("recheck_reason",     recheckReason);
        resp.put("copy_probability",   copyProb);
        resp.put("classification",     classification);
        resp.put("category_match_rate",catMatchRate);
        resp.put("k_results_count",    matches.size());
        resp.put("checked_at",         checkedAt);
        resp.put("qdrant_query_ms",    qdrantMs);
        resp.put("total_check_ms",     totalMs);
        resp.put("previous_archived",  previous != null);
        resp.put("classification_description", scoringEngine.classificationDescription(classification));
        resp.put("automated_action",           scoringEngine.automatedAction(classification));

        if (previous != null) {
            ObjectNode diff = resp.putObject("change_from_previous");
            String prevClass = previous.path("classification").asText("UNKNOWN");
            double prevProb  = previous.path("copy_probability").asDouble(0.0);
            diff.put("previous_classification",  prevClass);
            diff.put("previous_copy_probability", prevProb);
            diff.put("classification_changed",   !prevClass.equals(classification));
            diff.put("probability_delta",        copyProb - prevProb);
        }

        ArrayNode topMatches = resp.putArray("top_matches");
        matches.stream().limit(5).forEach(m -> {
            ObjectNode n = topMatches.addObject();
            n.put("matched_idea_id",      m.matchedIdeaId());
            n.put("matched_candidate_id", m.matchedCandidateId());
            n.put("cosine_similarity",    m.cosineSimilarity());
            n.put("matched_category",     m.matchedCategory());
            n.put("similarity_tier",      similarityTier(m.cosineSimilarity()));
        });

        resp.set("kafka_event_published", buildKafkaEvent(tenantId, ideaId, "", copyProb,
            classification, matches, catMatchRate, checkedAt));

        LOG.info("run_similarity_recheck: idea_id=" + ideaId + " recheck_count=" + recheckCount
                + " classification=" + classification + " reason=" + recheckReason);
        return resp;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private ObjectNode buildReport(String reportId, String tenantId, String ideaId,
                                    String candidateId, String vectorId, double copyProb,
                                    String classification, List<VectorEngine.SimilarityMatch> matches,
                                    double catMatchRate, String checkedAt, int recheckCount,
                                    String category) {
        ObjectNode r = mapper.createObjectNode();
        r.put("report_id",         reportId);
        r.put("tenant_id",         tenantId);
        r.put("idea_id",           ideaId);
        r.put("candidate_id",      candidateId);
        r.put("vector_id",         vectorId);
        r.put("copy_probability",  copyProb);
        r.put("classification",    classification);
        r.put("k_results_count",   matches.size());
        r.put("category_match_rate", catMatchRate);
        r.put("checked_at",        checkedAt);
        r.put("recheck_count",     recheckCount);
        r.put("idea_category",     category);

        ArrayNode top = r.putArray("top_matches");
        matches.stream().limit(5).forEach(m -> {
            ObjectNode n = top.addObject();
            n.put("matched_idea_id",      m.matchedIdeaId());
            n.put("matched_candidate_id", m.matchedCandidateId());
            n.put("cosine_similarity",    m.cosineSimilarity());
            n.put("matched_category",     m.matchedCategory());
            n.put("submission_timestamp", m.submissionTimestamp());
        });
        return r;
    }

    private ObjectNode buildKafkaEvent(String tenantId, String ideaId, String candidateId,
                                        double copyProb, String classification,
                                        List<VectorEngine.SimilarityMatch> matches,
                                        double catMatchRate, String checkedAt) {
        ObjectNode e = mapper.createObjectNode();
        // Topic depends on classification (per ISD spec §6.1)
        String topic = switch (classification) {
            case "FLAG"   -> "idea.similarity.flagged";
            case "REVIEW" -> "idea.similarity.review";
            default       -> "idea.similarity.cleared";
        };
        e.put("topic",               topic);
        e.put("tenant_id",           tenantId);
        e.put("idea_id",             ideaId);
        e.put("candidate_id",        candidateId);
        e.put("copy_probability",    copyProb);
        e.put("classification",      classification);
        e.put("category_match_rate", catMatchRate);
        e.put("k_results_count",     matches.size());
        e.put("checked_at",          checkedAt);
        e.put("schema_version",      "idea.similarity.v1");

        ArrayNode topMatches = e.putArray("top_matches");
        matches.stream().limit(5).forEach(m -> {
            ObjectNode n = topMatches.addObject();
            n.put("matched_idea_id",      m.matchedIdeaId());
            n.put("matched_candidate_id", m.matchedCandidateId());
            n.put("cosine_similarity",    m.cosineSimilarity());
            n.put("submission_timestamp", m.submissionTimestamp());
        });

        String consumers = switch (classification) {
            case "FLAG"   -> "notification-service, admin-service, dispute-service";
            case "REVIEW" -> "admin-service, notification-service";
            default       -> "idea-registry-service, notification-service, analytics-service";
        };
        e.put("kafka_consumers", consumers);
        return e;
    }

    public static String similarityTier(double sim) {
        if (sim >= 0.90) return "VERY_SIMILAR_LIKELY_DUPLICATE";
        if (sim >= 0.75) return "RELATED_WORTH_REVIEWING";
        if (sim >= 0.50) return "SOMEWHAT_RELATED_FOR_CONTEXT";
        return "NOT_RELATED";
    }

    private String req(JsonNode args, String field) {
        JsonNode n = args.get(field);
        if (n == null || n.isNull() || n.asText().isBlank())
            throw new IllegalArgumentException("Missing required field: " + field);
        return n.asText().trim();
    }
}
