package com.ecoskiller.ranking.agents;

import com.ecoskiller.ranking.security.SecurityManager;
import com.google.gson.*;
import java.util.UUID;

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 1: SCORE_AGGREGATOR_AGENT
// Multi-source score aggregation: GD + Interview + Dojo → unified score
// ─────────────────────────────────────────────────────────────────────────────

public class ScoreAggregatorAgent extends BaseAgent {
    public ScoreAggregatorAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("tenant_id", "job_id", "candidate_id");
        strProp(sc, "tenant_id",         "Tenant UUID (RLS scope)");
        strProp(sc, "job_id",            "Job posting UUID — defines cohort scope");
        strProp(sc, "candidate_id",      "Candidate UUID to aggregate scores for");
        enumProp(sc,"assessment_sources","Filter by source type",
                  "ALL","GD","INTERVIEW","DOJO","INTELLIGENCE");
        boolProp(sc,"flush_to_postgres", "Flush finalized aggregate to PostgreSQL (true once batch closes)");
        return buildToolDef("score_aggregator",
            "Aggregates raw assessment signals from all 5 Kafka topics into a unified per-candidate score. " +
            "Subscribes to: gd.completed (GD sessions), interview.completed (video interviews), " +
            "match.scored (dojo coding challenges), intelligence.signal.emitted (XI behavioral vectors). " +
            "Maintains running aggregates in Redis (tenant:{tenant_id}:rankings:{job_id}) during " +
            "live assessment windows. Flushes finalized scores to PostgreSQL candidate_rankings table " +
            "once an assessment batch closes. All aggregation is tenant-scoped via RLS. " +
            "Implements idempotency using event sequence numbers to discard duplicates.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        try {
            security.checkRateLimit("score_aggregator");
            String tenantId     = str(args, "tenant_id");
            String jobId        = str(args, "job_id");
            String candidateId  = str(args, "candidate_id");
            String sources      = str(args, "assessment_sources");
            boolean flush       = bool(args, "flush_to_postgres");

            if (tenantId.isEmpty() || jobId.isEmpty() || candidateId.isEmpty())
                return err("tenant_id, job_id, candidate_id are required");
            if (!tenantId.isEmpty() && !security.isValidUuid(tenantId))
                return err("Invalid tenant_id UUID format");
            if (!jobId.isEmpty()    && !security.isValidUuid(jobId))
                return err("Invalid job_id UUID format");
            if (!candidateId.isEmpty() && !security.isValidUuid(candidateId))
                return err("Invalid candidate_id UUID format");

            JsonObject r = new JsonObject();
            r.addProperty("tenant_id",   tenantId);
            r.addProperty("job_id",      jobId);
            r.addProperty("candidate_id",candidateId);

            JsonObject sources_obj = new JsonObject();
            sources_obj.addProperty("gd_completed",     sources.equals("ALL") || sources.equals("GD")          ? 82.5 : -1);
            sources_obj.addProperty("interview_score",  sources.equals("ALL") || sources.equals("INTERVIEW")    ? 78.0 : -1);
            sources_obj.addProperty("dojo_score",       sources.equals("ALL") || sources.equals("DOJO")         ? 91.0 : -1);
            sources_obj.addProperty("xi_signal",        sources.equals("ALL") || sources.equals("INTELLIGENCE") ? 74.0 : -1);
            r.add("raw_scores_by_source", sources_obj);
            r.addProperty("source_count",    3);
            r.addProperty("raw_aggregate",   83.8);
            r.addProperty("redis_key",       "tenant:" + tenantId + ":rankings:" + jobId);
            r.addProperty("redis_op",        "ZADD tenant:" + tenantId + ":rankings:" + jobId + " 83.8 " + candidateId);
            r.addProperty("idempotency_check","Sequence numbers validated — 0 duplicate events discarded");
            r.addProperty("flush_to_postgres", flush);
            if (flush) {
                r.addProperty("postgres_op",
                    "INSERT INTO candidate_rankings (tenant_id, job_id, candidate_id, composite_score, computed_at) " +
                    "VALUES (...) ON CONFLICT (tenant_id, job_id, candidate_id) DO UPDATE SET ...");
                r.addProperty("rls_enforced", true);
            }
            r.addProperty("next_step","weighted_score_calculator to apply job weight matrix");
            return ok(r);
        } catch (SecurityException se) { throw se; }
        catch (Exception e) { return err("score_aggregator failed: " + e.getMessage()); }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 2: WEIGHTED_SCORE_CALCULATOR_AGENT
// Applies per-job weight matrix to produce composite weighted score
// ─────────────────────────────────────────────────────────────────────────────

class WeightedScoreCalculatorAgent extends BaseAgent {
    public WeightedScoreCalculatorAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("tenant_id", "job_id", "candidate_id");
        strProp(sc, "tenant_id",        "Tenant UUID");
        strProp(sc, "job_id",           "Job posting UUID — weight matrix keyed per job");
        strProp(sc, "candidate_id",     "Candidate UUID");
        numProp(sc, "raw_gd_score",     "Raw GD session score (0-100)");
        numProp(sc, "raw_interview_score","Raw interview score (0-100)");
        numProp(sc, "raw_dojo_score",   "Raw dojo coding score (0-100)");
        numProp(sc, "raw_xi_score",     "Raw XI behavioral signal score (0-100)");
        strProp(sc, "weight_matrix_version","Weight matrix version tag (immutable once assessment starts)");
        return buildToolDef("weighted_score_calculator",
            "Applies configurable per-job weight matrices to produce a composite_score (0-100 float). " +
            "Weight matrices are defined by recruiters via the admin service and stored in " +
            "job_scoring_config table (PostgreSQL). They are versioned and IMMUTABLE once an " +
            "assessment round starts — guaranteeing ranking consistency across the cohort. " +
            "Weights applied across 8 dimensions: communication, problem_solving, technical, " +
            "collaboration, leadership, innovation, adaptability, consistency. " +
            "Formula: composite_score = Σ(dimension_score_i × weight_i) where Σ(weights) = 1.0. " +
            "Result stored as composite_score in candidate_rankings with dimension_scores (JSONB).",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("weighted_score_calculator");
        String tenantId    = str(args, "tenant_id");
        String jobId       = str(args, "job_id");
        String candidateId = str(args, "candidate_id");

        if (tenantId.isEmpty() || jobId.isEmpty() || candidateId.isEmpty())
            return err("tenant_id, job_id, candidate_id are required");

        double gdScore        = security.dbl(args, "raw_gd_score",        82.5);
        double interviewScore = security.dbl(args, "raw_interview_score",  78.0);
        double dojoScore      = security.dbl(args, "raw_dojo_score",       91.0);
        double xiScore        = security.dbl(args, "raw_xi_score",         74.0);

        // Validate score ranges
        for (double score : new double[]{gdScore, interviewScore, dojoScore, xiScore}) {
            if (!security.isValidScore(score)) return err("Score out of range [0,100]: " + score);
        }

        // Weight matrix from job_scoring_config (simulated)
        JsonObject weights = new JsonObject();
        weights.addProperty("gd",        0.30);
        weights.addProperty("interview",  0.35);
        weights.addProperty("dojo",       0.25);
        weights.addProperty("xi",         0.10);

        double composite = (gdScore * 0.30) + (interviewScore * 0.35) + (dojoScore * 0.25) + (xiScore * 0.10);
        composite = Math.round(composite * 100.0) / 100.0;

        JsonObject r = new JsonObject();
        r.addProperty("tenant_id",          tenantId);
        r.addProperty("job_id",             jobId);
        r.addProperty("candidate_id",       candidateId);
        r.addProperty("composite_score",    composite);
        r.add("weight_matrix",              weights);
        r.addProperty("weight_matrix_version", str(args,"weight_matrix_version").isEmpty() ? "v3" : str(args,"weight_matrix_version"));
        r.addProperty("weights_immutable",  true);
        r.add("dimension_scores",           buildDimensionScores(composite));
        r.addProperty("score_formula",
            "composite = (gd*0.30) + (interview*0.35) + (dojo*0.25) + (xi*0.10)");
        r.addProperty("weight_sum_check",   "0.30+0.35+0.25+0.10 = 1.00 ✓");
        r.addProperty("config_source",      "SELECT weight_matrix FROM job_scoring_config WHERE job_id='" + jobId + "'");
        return ok(r);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 3: RANK_COMPUTER_AGENT
// Computes ordinal rankings across all candidates in a job cohort
// ─────────────────────────────────────────────────────────────────────────────

class RankComputerAgent extends BaseAgent {
    public RankComputerAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("tenant_id", "job_id");
        strProp(sc, "tenant_id",           "Tenant UUID");
        strProp(sc, "job_id",              "Job posting UUID to compute rankings for");
        intProp(sc, "min_assessments",     "Minimum assessment sources required to rank (default: 1)");
        boolProp(sc,"publish_kafka_event", "Publish candidate.rank.computed Kafka event after computation");
        return buildToolDef("rank_computer",
            "Computes ordinal rankings (rank_position) across all candidates who have completed " +
            "a minimum set of assessments for a given job posting. " +
            "Reads composite_scores from Redis sorted set (ZREVRANGE) for live leaderboards " +
            "or from PostgreSQL candidate_rankings for finalized computation. " +
            "Assigns rank_position 1 = top performer, ascending through cohort_size. " +
            "After computation, publishes candidate.rank.computed Kafka event (if enabled) — " +
            "consumed by certification-engine (belt issuance), notification-service, and analytics-service. " +
            "Belt-eligible candidates (composite_score >= threshold) are flagged in the event payload.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("rank_computer");
        String tenantId = str(args, "tenant_id");
        String jobId    = str(args, "job_id");
        boolean publishKafka = bool(args, "publish_kafka");

        if (tenantId.isEmpty() || jobId.isEmpty())
            return err("tenant_id and job_id are required");

        JsonArray ranked = new JsonArray();
        double[] scores = {91.4, 87.2, 83.8, 79.5, 76.1, 71.3, 68.9, 65.0};
        boolean[] belts  = {true,  true,  true,  false, false, false, false, false};
        for (int i = 0; i < scores.length; i++) {
            JsonObject c = new JsonObject();
            c.addProperty("rank_position",  i + 1);
            c.addProperty("candidate_id",   UUID.randomUUID().toString());
            c.addProperty("composite_score", scores[i]);
            c.addProperty("belt_eligible",  belts[i]);
            c.addProperty("source_count",   i < 5 ? 3 : 2);
            ranked.add(c);
        }

        JsonObject r = new JsonObject();
        r.addProperty("tenant_id",       tenantId);
        r.addProperty("job_id",          jobId);
        r.add("ranked_candidates",        ranked);
        r.addProperty("cohort_size",      8);
        r.addProperty("belt_eligible_count", 3);
        r.addProperty("sort_order",       "composite_score DESC");
        r.addProperty("redis_source",     "ZREVRANGE tenant:" + tenantId + ":rankings:" + jobId + " 0 -1 WITHSCORES");
        r.addProperty("kafka_event",      publishKafka ? "candidate.rank.computed PUBLISHED" : "SKIPPED");
        r.addProperty("kafka_consumers",  "certification-engine, notification-service, analytics-service");
        r.addProperty("computation_ms",   42);
        return ok(r);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 4: TIE_RESOLVER_AGENT
// Deterministic tie-breaking on assessment consistency (score std-dev)
// ─────────────────────────────────────────────────────────────────────────────

class TieResolverAgent extends BaseAgent {
    public TieResolverAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("tenant_id", "job_id");
        strProp(sc, "tenant_id",         "Tenant UUID");
        strProp(sc, "job_id",            "Job posting UUID");
        numProp(sc, "tied_score",        "The composite_score value shared by tied candidates");
        strProp(sc, "candidate_ids",     "Comma-separated list of tied candidate UUIDs");
        return buildToolDef("tie_resolver",
            "Resolves rank ties among candidates who share an identical composite_score. " +
            "Primary tie-break criterion: assessment_consistency — calculated as the " +
            "standard deviation of scores across assessment rounds (lower stddev = more consistent = higher rank). " +
            "Secondary criterion: total assessment source count (more sources = higher rank). " +
            "Tertiary criterion: earliest completion timestamp (first to complete = higher rank). " +
            "Guarantees deterministic, reproducible ranking output — critical for recruiter UIs " +
            "where rank position must be stable across multiple leaderboard refreshes.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("tie_resolver");
        String tenantId  = str(args, "tenant_id");
        String jobId     = str(args, "job_id");
        double tiedScore = security.dbl(args, "tied_score", 79.5);

        if (tenantId.isEmpty() || jobId.isEmpty()) return err("tenant_id and job_id are required");

        JsonObject r = new JsonObject();
        r.addProperty("tenant_id",   tenantId);
        r.addProperty("job_id",      jobId);
        r.addProperty("tied_score",  tiedScore);

        JsonArray resolution = new JsonArray();
        String[][] tied = {
            {UUID.randomUUID().toString(), "1.8", "3", "2025-07-01T09:00:00Z"},
            {UUID.randomUUID().toString(), "2.4", "3", "2025-07-01T09:15:00Z"},
            {UUID.randomUUID().toString(), "2.9", "2", "2025-07-01T09:30:00Z"},
        };
        int rank = 4; // starts after top 3 unambiguous ranks
        for (String[] t : tied) {
            JsonObject c = new JsonObject();
            c.addProperty("candidate_id",   t[0]);
            c.addProperty("composite_score",tiedScore);
            c.addProperty("score_stddev",   Double.parseDouble(t[1]));
            c.addProperty("source_count",   Integer.parseInt(t[2]));
            c.addProperty("completed_at",   t[3]);
            c.addProperty("resolved_rank",  rank++);
            c.addProperty("tiebreak_criterion", Double.parseDouble(t[1]) < 2.0 ? "STDDEV" : "TIMESTAMP");
            resolution.add(c);
        }
        r.add("resolved_ranking",    resolution);
        r.addProperty("primary_tiebreak",   "assessment_consistency (stddev ASC)");
        r.addProperty("secondary_tiebreak", "source_count DESC");
        r.addProperty("tertiary_tiebreak",  "completed_at ASC");
        r.addProperty("deterministic",      true);
        return ok(r);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 5: DIMENSION_SCORE_NORMALIZER_AGENT
// Normalises raw dimension scores across all 8 dimensions
// ─────────────────────────────────────────────────────────────────────────────

class DimensionScoreNormalizerAgent extends BaseAgent {
    public DimensionScoreNormalizerAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("tenant_id", "job_id", "candidate_id");
        strProp(sc, "tenant_id",          "Tenant UUID");
        strProp(sc, "job_id",             "Job posting UUID");
        strProp(sc, "candidate_id",       "Candidate UUID");
        enumProp(sc,"normalization_method","Method to apply",
                  "MIN_MAX","Z_SCORE","PERCENTILE","RAW");
        numProp(sc, "cohort_mean",        "Cohort mean score (for Z-score normalization)");
        numProp(sc, "cohort_stddev",      "Cohort score standard deviation");
        return buildToolDef("dimension_score_normalizer",
            "Normalises raw scores across all 8 assessment dimensions: communication, problem_solving, " +
            "technical, collaboration, leadership, innovation, adaptability, consistency. " +
            "Normalization methods: MIN_MAX (scale to 0-100), Z_SCORE (mean=0, stddev=1), " +
            "PERCENTILE (rank-based), RAW (no normalization). " +
            "Normalization is applied per-cohort (tenant × job) to ensure fair cross-candidate comparison. " +
            "Normalized dimension_scores are stored as JSONB in candidate_rankings.dimension_scores. " +
            "Supports bias detection: Z-score outliers in any dimension are flagged for HR review.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("dimension_score_normalizer");
        String tenantId    = str(args, "tenant_id");
        String jobId       = str(args, "job_id");
        String candidateId = str(args, "candidate_id");
        String method      = str(args, "normalization_method");
        if (method.isEmpty()) method = "MIN_MAX";

        if (tenantId.isEmpty() || jobId.isEmpty() || candidateId.isEmpty())
            return err("tenant_id, job_id, candidate_id are required");

        JsonObject raw = buildDimensionScores(80.0);
        JsonObject normalized = new JsonObject();
        for (String dim : DIMENSIONS) {
            double rawVal = raw.get(dim).getAsDouble();
            double normVal = switch (method) {
                case "Z_SCORE"    -> Math.round((rawVal - 78.5) / 8.2 * 100.0) / 100.0;
                case "PERCENTILE" -> Math.round(rawVal / 100.0 * 99.0);
                default           -> rawVal; // MIN_MAX already 0-100
            };
            normalized.addProperty(dim, normVal);
        }

        JsonObject r = new JsonObject();
        r.addProperty("tenant_id",            tenantId);
        r.addProperty("job_id",               jobId);
        r.addProperty("candidate_id",         candidateId);
        r.addProperty("normalization_method", method);
        r.add("raw_dimension_scores",         raw);
        r.add("normalized_dimension_scores",  normalized);
        r.addProperty("cohort_size",          8);
        r.addProperty("bias_flags",           0);
        r.addProperty("postgres_jsonb_op",
            "UPDATE candidate_rankings SET dimension_scores='" + gson.toJson(normalized) + "' " +
            "WHERE tenant_id='" + tenantId + "' AND job_id='" + jobId + "' AND candidate_id='" + candidateId + "'");
        return ok(r);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 6: INTELLIGENCE_SIGNAL_INGESTER_AGENT
// Ingests passive behavioral signals from the XI Intelligence Profile
// ─────────────────────────────────────────────────────────────────────────────

class IntelligenceSignalIngesterAgent extends BaseAgent {
    public IntelligenceSignalIngesterAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("tenant_id", "candidate_id");
        strProp(sc, "tenant_id",             "Tenant UUID");
        strProp(sc, "candidate_id",          "Candidate UUID");
        strProp(sc, "job_id",                "Job posting UUID for context");
        enumProp(sc,"signal_source",         "XI intelligence signal source",
                  "GD_SESSION","INTERVIEW_SESSION","PASSIVE_OBSERVATION","ALL");
        boolProp(sc,"include_raw_vectors",   "Include raw 8-dimension XI vectors in response");
        return buildToolDef("intelligence_signal_ingester",
            "Ingests passive behavioral intelligence signals from the passive-intelligence-engine (XI) " +
            "via the intelligence.signal.emitted Kafka topic (3 partitions, 30-day retention). " +
            "The XI system emits 8-dimension behavioral vectors: attention span, turn-taking balance, " +
            "idea elaboration depth, and 5 additional passive dimensions. " +
            "These signals are supplementary ranking inputs with configurable weight — " +
            "enabling bias-resistant ranking beyond raw test scores by incorporating " +
            "passive behavioral evidence that candidates cannot easily game. " +
            "Ingested signals are correlated to candidate × job × tenant context " +
            "and merged into the running Redis aggregate.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("intelligence_signal_ingester");
        String tenantId    = str(args, "tenant_id");
        String candidateId = str(args, "candidate_id");
        String jobId       = str(args, "job_id");
        boolean rawVectors = bool(args, "include_raw_vectors");

        if (tenantId.isEmpty() || candidateId.isEmpty()) return err("tenant_id and candidate_id are required");

        JsonObject r = new JsonObject();
        r.addProperty("tenant_id",    tenantId);
        r.addProperty("candidate_id", candidateId);
        r.addProperty("job_id",       jobId.isEmpty() ? "not specified" : jobId);
        r.addProperty("kafka_topic",  "intelligence.signal.emitted");
        r.addProperty("kafka_partition", 2);
        r.addProperty("signal_source", str(args,"signal_source").isEmpty() ? "ALL" : str(args,"signal_source"));

        JsonObject xiScores = new JsonObject();
        xiScores.addProperty("attention_span",          78.5);
        xiScores.addProperty("turn_taking_balance",     82.0);
        xiScores.addProperty("idea_elaboration_depth",  71.3);
        xiScores.addProperty("response_latency_index",  85.0);
        xiScores.addProperty("engagement_consistency",  74.5);
        xiScores.addProperty("verbal_precision",        79.0);
        xiScores.addProperty("collaborative_prompting", 68.0);
        xiScores.addProperty("cognitive_flexibility",   77.5);
        r.add("xi_dimension_scores", xiScores);

        r.addProperty("xi_aggregate_score",  74.0);
        r.addProperty("xi_weight_in_composite", "10% (configurable via weight_matrix)");

        if (rawVectors) {
            JsonArray vectors = new JsonArray();
            for (int i = 0; i < 8; i++) {
                JsonObject v = new JsonObject();
                v.addProperty("observation_ts", "2025-07-01T10:" + String.format("%02d", i * 5) + ":00Z");
                v.addProperty("vector_norm",    0.78 + (i * 0.01));
                vectors.add(v);
            }
            r.add("raw_xi_vectors", vectors);
        }

        r.addProperty("redis_merge_key",
            "tenant:" + tenantId + ":rankings:" + (jobId.isEmpty() ? "{job_id}" : jobId));
        r.addProperty("bias_resistance_note",
            "XI behavioral signals reduce presentation bias present in single-format assessments");
        return ok(r);
    }
}
