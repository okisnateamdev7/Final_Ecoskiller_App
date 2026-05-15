package com.ecoskiller.mcp.models;

import com.ecoskiller.mcp.security.AggSecurityConfig;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Core aggregation engine for candidate-performance-aggregator.
 *
 * Implements:
 *   1. Weighted score aggregation: GD×w1 + Interview×w2 + Dojo×w3
 *   2. Partial participation handling: redistribute weights among participated tracks
 *   3. Rubric-based normalization (tenant rubric thresholds applied)
 *   4. Belt level mapping: BRONZE / SILVER / GOLD / PLATINUM
 *   5. Score explainability payload (per_track_breakdown, DPDPA 2023)
 *   6. Confidence level computation
 */
public class AggregationEngine {

    private final AggRepository repo = AggRepository.getInstance();

    /**
     * Perform complete aggregation for a (candidate_id, cycle_id) pair.
     * Uses distributed lock to prevent concurrent aggregation.
     */
    public AggModels.AggregateRecord aggregate(String tenantId, String candidateId,
                                                String cycleId, boolean forceRetry) {
        long startMs = System.currentTimeMillis();

        // Check if lock is already held (concurrent aggregation)
        if (!forceRetry && AggSecurityConfig.isLocked(candidateId, cycleId))
            throw new IllegalStateException("Aggregation already in progress for candidate=" + candidateId + " cycle=" + cycleId);

        // Acquire lock
        if (!AggSecurityConfig.acquireLock(candidateId, cycleId))
            throw new IllegalStateException("Failed to acquire aggregation lock — CONFLICT");

        try {
            return doAggregate(tenantId, candidateId, cycleId, startMs);
        } finally {
            AggSecurityConfig.releaseLock(candidateId, cycleId);
        }
    }

    private AggModels.AggregateRecord doAggregate(String tenantId, String candidateId,
                                                    String cycleId, long startMs) {
        AggModels.AggregateRecord record = repo.getAggregate(tenantId, candidateId, cycleId)
                .orElse(new AggModels.AggregateRecord(candidateId, cycleId, tenantId));
        record.status = AggModels.AggStatus.IN_PROGRESS;

        // Resolve cycle config (weights + participating tracks)
        Optional<AggModels.CycleConfig> cycleOpt = repo.getCycleConfig(cycleId);
        List<AggModels.AssessmentTrack> configuredTracks = cycleOpt
                .map(c -> c.tracks)
                .orElse(List.of(AggModels.AssessmentTrack.values()));

        // Resolve rubric for tenant (Redis cache → PostgreSQL fallback)
        String jobRoleId = cycleOpt.map(c -> c.jobRoleId).orElse("default");
        Optional<AggModels.ScoringRubric> rubricOpt = repo.getRubric(tenantId, jobRoleId);

        // Determine base weights (rubric → cycle override → env default)
        double baseGd   = rubricOpt.map(r -> r.gdWeight)       .orElse(AggSecurityConfig.getDefaultGdWeight());
        double baseInt  = rubricOpt.map(r -> r.interviewWeight) .orElse(AggSecurityConfig.getDefaultIntWeight());
        double baseDojo = rubricOpt.map(r -> r.dojoWeight)      .orElse(AggSecurityConfig.getDefaultDojoWeight());

        // Apply cycle-level weight overrides
        if (cycleOpt.isPresent() && !cycleOpt.get().weightOverrides.isEmpty()) {
            Map<String,Double> ov = cycleOpt.get().weightOverrides;
            if (ov.containsKey("GD"))        baseGd   = ov.get("GD");
            if (ov.containsKey("INTERVIEW"))  baseInt  = ov.get("INTERVIEW");
            if (ov.containsKey("DOJO"))       baseDojo = ov.get("DOJO");
        }

        // Retrieve actual track scores
        Optional<AggModels.TrackScore> gdScore   = repo.getTrackScore(tenantId, candidateId, cycleId, AggModels.AssessmentTrack.GD);
        Optional<AggModels.TrackScore> intScore  = repo.getTrackScore(tenantId, candidateId, cycleId, AggModels.AssessmentTrack.INTERVIEW);
        Optional<AggModels.TrackScore> dojoScore = repo.getTrackScore(tenantId, candidateId, cycleId, AggModels.AssessmentTrack.DOJO);

        // Partial participation: determine which configured tracks have scores
        boolean hasGd   = gdScore.isPresent()   && configuredTracks.contains(AggModels.AssessmentTrack.GD);
        boolean hasInt  = intScore.isPresent()   && configuredTracks.contains(AggModels.AssessmentTrack.INTERVIEW);
        boolean hasDojo = dojoScore.isPresent()  && configuredTracks.contains(AggModels.AssessmentTrack.DOJO);

        // Redistribute weights proportionally for partial participation
        double[] redist = redistributeWeights(baseGd, baseInt, baseDojo, hasGd, hasInt, hasDojo);
        double wGd = redist[0], wInt = redist[1], wDojo = redist[2];

        // Weighted aggregation
        double aggScore = 0;
        if (hasGd)   aggScore += gdScore.get().rawScore   * wGd;
        if (hasInt)  aggScore += intScore.get().rawScore  * wInt;
        if (hasDojo) aggScore += dojoScore.get().rawScore * wDojo;

        // Normalize to 0-100 using rubric thresholds
        double normalizedScore = normalizeScore(aggScore, rubricOpt);

        // Belt level mapping
        AggModels.BeltLevel belt = rubricOpt.isPresent()
                ? mapBeltWithRubric(normalizedScore, rubricOpt.get())
                : AggModels.BeltLevel.fromScore(normalizedScore);

        // Build per-track breakdown (score explainability, DPDPA 2023)
        List<AggModels.TrackBreakdown> breakdown = new ArrayList<>();
        breakdown.add(new AggModels.TrackBreakdown(AggModels.AssessmentTrack.GD,
                hasGd ? gdScore.get().rawScore : 0, wGd, hasGd));
        breakdown.add(new AggModels.TrackBreakdown(AggModels.AssessmentTrack.INTERVIEW,
                hasInt ? intScore.get().rawScore : 0, wInt, hasInt));
        breakdown.add(new AggModels.TrackBreakdown(AggModels.AssessmentTrack.DOJO,
                hasGd ? dojoScore.orElse(null) != null ? dojoScore.get().rawScore : 0 : 0, wDojo, hasDojo));

        List<AggModels.AssessmentTrack> participated = new ArrayList<>();
        if (hasGd)   participated.add(AggModels.AssessmentTrack.GD);
        if (hasInt)  participated.add(AggModels.AssessmentTrack.INTERVIEW);
        if (hasDojo) participated.add(AggModels.AssessmentTrack.DOJO);

        // Populate record
        record.gdScore           = hasGd   ? gdScore.get().rawScore   : 0;
        record.interviewScore    = hasInt  ? intScore.get().rawScore  : 0;
        record.dojoScore         = hasDojo ? dojoScore.get().rawScore : 0;
        record.weightsGd         = wGd;
        record.weightsInterview  = wInt;
        record.weightsDojo       = wDojo;
        record.aggregateScore    = aggScore;
        record.normalizedScore   = normalizedScore;
        record.beltEligibleLevel = belt;
        record.perTrackBreakdown = breakdown;
        record.tracksParticipated= participated;
        record.rubricVersion     = rubricOpt.map(r -> r.version).orElse("default-v1");
        record.status            = AggModels.AggStatus.COMPUTED;
        record.computedAt        = Instant.now();
        record.processingLatencyMs = String.valueOf(System.currentTimeMillis() - startMs);

        repo.saveAggregate(record);
        repo.inc("aggregations_succeeded_total");

        // Publish candidate.performance.computed Kafka event
        Map<String,Object> kafkaPayload = new LinkedHashMap<>(record.toMap());
        kafkaPayload.put("event_type", "candidate.performance.computed");
        repo.logKafkaEvent("candidate.performance.computed", kafkaPayload);

        repo.audit("AGGREGATE_COMPUTED", candidateId, cycleId, tenantId,
                "score=" + AggSecurityConfig.round(normalizedScore) + " belt=" + belt.name()
                        + " tracks=" + participated.size() + " latency=" + record.processingLatencyMs + "ms", true);

        return record;
    }

    /**
     * Redistribute weights proportionally when some tracks are missing.
     * Example: if candidate only has GD + Interview (no Dojo):
     *   total = 0.35 + 0.40 = 0.75 → GD = 0.35/0.75 = 0.467, Interview = 0.40/0.75 = 0.533
     */
    public double[] redistributeWeights(double wGd, double wInt, double wDojo,
                                         boolean hasGd, boolean hasInt, boolean hasDojo) {
        double totalParticipated = 0;
        if (hasGd)   totalParticipated += wGd;
        if (hasInt)  totalParticipated += wInt;
        if (hasDojo) totalParticipated += wDojo;

        if (totalParticipated <= 0) return new double[]{wGd, wInt, wDojo};

        return new double[]{
            hasGd   ? wGd   / totalParticipated : 0,
            hasInt  ? wInt  / totalParticipated : 0,
            hasDojo ? wDojo / totalParticipated : 0
        };
    }

    /** Normalize aggregate score to 0-100 scale using rubric thresholds. */
    public double normalizeScore(double rawAggScore, Optional<AggModels.ScoringRubric> rubricOpt) {
        // Raw aggregate is already in 0-100 scale (weighted sum of 0-100 scores)
        // Apply rubric-defined threshold mapping if available
        if (rubricOpt.isEmpty()) return Math.min(100, Math.max(0, rawAggScore));

        AggModels.ScoringRubric rubric = rubricOpt.get();
        double max = rubric.platinumThreshold; // e.g. 90 → maps to 100
        if (max <= 0) return rawAggScore;

        double normalized = (rawAggScore / max) * 100.0;
        return Math.min(100, Math.max(0, normalized));
    }

    /** Map belt level using rubric thresholds rather than hardcoded defaults. */
    private AggModels.BeltLevel mapBeltWithRubric(double score, AggModels.ScoringRubric rubric) {
        if (score >= rubric.platinumThreshold) return AggModels.BeltLevel.PLATINUM;
        if (score >= rubric.goldThreshold)     return AggModels.BeltLevel.GOLD;
        if (score >= rubric.silverThreshold)   return AggModels.BeltLevel.SILVER;
        return AggModels.BeltLevel.BRONZE;
    }
}
