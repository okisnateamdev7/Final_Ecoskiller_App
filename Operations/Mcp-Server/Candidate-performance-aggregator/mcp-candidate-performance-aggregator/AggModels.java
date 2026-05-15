package com.ecoskiller.mcp.models;

import java.time.Instant;
import java.util.*;

/**
 * Domain models for the candidate-performance-aggregator service.
 *
 * Belt levels (normalized score ranges):
 *   BRONZE   : 0  – 49
 *   SILVER   : 50 – 69
 *   GOLD     : 70 – 84
 *   PLATINUM : 85 – 100
 *
 * Tracks: GD (35%) · INTERVIEW (40%) · DOJO (25%)
 */
public class AggModels {

    // ── Belt Level ─────────────────────────────────────────────────────────────

    public enum BeltLevel {
        BRONZE(0, 49), SILVER(50, 69), GOLD(70, 84), PLATINUM(85, 100);

        public final int minScore, maxScore;
        BeltLevel(int min, int max) { this.minScore=min; this.maxScore=max; }

        public static BeltLevel fromScore(double score) {
            for (BeltLevel b : values())
                if (score >= b.minScore && score <= b.maxScore) return b;
            return BRONZE;
        }
    }

    // ── Assessment tracks ──────────────────────────────────────────────────────

    public enum AssessmentTrack {
        GD, INTERVIEW, DOJO;
        public static AssessmentTrack fromString(String s) {
            try { return valueOf(s.toUpperCase()); }
            catch (Exception e) { return GD; }
        }
    }

    // ── Aggregation status ─────────────────────────────────────────────────────

    public enum AggStatus {
        PENDING,      // Waiting for all tracks
        IN_PROGRESS,  // Lock acquired, computing
        COMPUTED,     // Successfully aggregated
        FAILED,       // Unrecoverable failure → DLQ
        RETRYING      // Retry in progress
    }

    // ── Track score record ─────────────────────────────────────────────────────

    public static class TrackScore {
        public final String         scoreId;
        public final String         eventId;
        public final String         candidateId;
        public final String         cycleId;
        public final String         tenantId;
        public final AssessmentTrack track;
        public final double          rawScore;     // 0-100
        public final Instant         scoredAt;
        public final Map<String,Object> metadata;
        public boolean duplicate = false;

        public TrackScore(String eventId, String candidateId, String cycleId,
                          String tenantId, AssessmentTrack track, double rawScore,
                          Instant scoredAt, Map<String,Object> metadata) {
            this.scoreId     = UUID.randomUUID().toString();
            this.eventId     = eventId;
            this.candidateId = candidateId;
            this.cycleId     = cycleId;
            this.tenantId    = tenantId;
            this.track       = track;
            this.rawScore    = rawScore;
            this.scoredAt    = scoredAt != null ? scoredAt : Instant.now();
            this.metadata    = metadata != null ? metadata : new LinkedHashMap<>();
        }

        public Map<String,Object> toMap() {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("score_id",     scoreId);
            m.put("event_id",     eventId);
            m.put("candidate_id", candidateId);
            m.put("cycle_id",     cycleId);
            m.put("tenant_id",    tenantId);
            m.put("track",        track.name());
            m.put("raw_score",    rawScore);
            m.put("scored_at",    scoredAt.toString());
            m.put("duplicate",    duplicate);
            return m;
        }
    }

    // ── Per-track breakdown (score explainability payload, DPDPA 2023) ─────────

    public static class TrackBreakdown {
        public final AssessmentTrack track;
        public final double rawScore;
        public final double weightApplied;
        public final double normalizedContribution;  // rawScore × weight
        public final boolean participated;

        public TrackBreakdown(AssessmentTrack track, double rawScore, double weight, boolean participated) {
            this.track                  = track;
            this.rawScore               = rawScore;
            this.weightApplied          = weight;
            this.normalizedContribution = participated ? rawScore * weight : 0;
            this.participated           = participated;
        }

        public Map<String,Object> toMap() {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("track",                  track.name());
            m.put("raw_score",              Math.round(rawScore*100.0)/100.0);
            m.put("weight_applied",         weightApplied);
            m.put("normalized_contribution",Math.round(normalizedContribution*100.0)/100.0);
            m.put("participated",           participated);
            return m;
        }
    }

    // ── Aggregate performance record ───────────────────────────────────────────

    public static class AggregateRecord {
        public final String         aggregateId;
        public final String         candidateId;
        public final String         cycleId;
        public final String         tenantId;
        public double               gdScore;
        public double               interviewScore;
        public double               dojoScore;
        public double               weightsGd;
        public double               weightsInterview;
        public double               weightsDojo;
        public double               aggregateScore;   // weighted sum before normalization
        public double               normalizedScore;  // 0-100 rubric-normalized
        public BeltLevel            beltEligibleLevel;
        public List<TrackBreakdown> perTrackBreakdown;
        public List<AssessmentTrack> tracksParticipated;
        public String               rubricVersion;
        public AggStatus            status;
        public int                  retryCount;
        public Instant              computedAt;
        public String               processingLatencyMs;

        public AggregateRecord(String candidateId, String cycleId, String tenantId) {
            this.aggregateId       = UUID.randomUUID().toString();
            this.candidateId       = candidateId;
            this.cycleId           = cycleId;
            this.tenantId          = tenantId;
            this.status            = AggStatus.PENDING;
            this.retryCount        = 0;
            this.perTrackBreakdown = new ArrayList<>();
            this.tracksParticipated= new ArrayList<>();
        }

        public Map<String,Object> toMap() {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("aggregate_id",       aggregateId);
            m.put("candidate_id",       candidateId);
            m.put("cycle_id",           cycleId);
            m.put("tenant_id",          tenantId);
            m.put("gd_score",           Math.round(gdScore*100.0)/100.0);
            m.put("interview_score",    Math.round(interviewScore*100.0)/100.0);
            m.put("dojo_score",         Math.round(dojoScore*100.0)/100.0);
            m.put("weights_used",       Map.of("gd",weightsGd,"interview",weightsInterview,"dojo",weightsDojo));
            m.put("aggregate_score",    Math.round(aggregateScore*100.0)/100.0);
            m.put("normalized_score",   Math.round(normalizedScore*100.0)/100.0);
            m.put("belt_eligible_level",beltEligibleLevel!=null?beltEligibleLevel.name():"UNKNOWN");
            m.put("per_track_breakdown",perTrackBreakdown.stream().map(TrackBreakdown::toMap).collect(java.util.stream.Collectors.toList()));
            m.put("tracks_participated",tracksParticipated.stream().map(Enum::name).collect(java.util.stream.Collectors.toList()));
            m.put("rubric_version",     rubricVersion);
            m.put("status",             status.name());
            m.put("retry_count",        retryCount);
            m.put("computed_at",        computedAt!=null?computedAt.toString():null);
            m.put("processing_latency_ms", processingLatencyMs);
            return m;
        }
    }

    // ── Scoring rubric (per-tenant, per-role) ─────────────────────────────────

    public static class ScoringRubric {
        public final String         rubricId;
        public final String         tenantId;
        public final String         jobRoleId;
        public final double         gdWeight;
        public final double         interviewWeight;
        public final double         dojoWeight;
        public final double         bronzeThreshold;
        public final double         silverThreshold;
        public final double         goldThreshold;
        public final double         platinumThreshold;
        public final String         version;
        public final Instant        createdAt;

        public ScoringRubric(String tenantId, String jobRoleId,
                              double gdW, double intW, double dojoW,
                              double bronze, double silver, double gold, double platinum,
                              String version) {
            this.rubricId          = UUID.randomUUID().toString();
            this.tenantId          = tenantId;
            this.jobRoleId         = jobRoleId;
            this.gdWeight          = gdW;
            this.interviewWeight   = intW;
            this.dojoWeight        = dojoW;
            this.bronzeThreshold   = bronze;
            this.silverThreshold   = silver;
            this.goldThreshold     = gold;
            this.platinumThreshold = platinum;
            this.version           = version;
            this.createdAt         = Instant.now();
        }

        public Map<String,Object> toMap() {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("rubric_id",          rubricId);
            m.put("tenant_id",          tenantId);
            m.put("job_role_id",        jobRoleId);
            m.put("weights",            Map.of("gd",gdWeight,"interview",interviewWeight,"dojo",dojoWeight));
            m.put("thresholds",         Map.of("bronze",bronzeThreshold,"silver",silverThreshold,"gold",goldThreshold,"platinum",platinumThreshold));
            m.put("version",            version);
            m.put("created_at",         createdAt.toString());
            return m;
        }
    }

    // ── Assessment cycle config ────────────────────────────────────────────────

    public static class CycleConfig {
        public final String               cycleId;
        public final String               tenantId;
        public final String               jobRoleId;
        public final List<AssessmentTrack> tracks;
        public final Map<String,Double>    weightOverrides;
        public final String               status; // ACTIVE / COMPLETED / CANCELLED
        public final Instant              createdAt;

        public CycleConfig(String cycleId, String tenantId, String jobRoleId,
                           List<AssessmentTrack> tracks, Map<String,Double> overrides, String status) {
            this.cycleId         = cycleId;
            this.tenantId        = tenantId;
            this.jobRoleId       = jobRoleId;
            this.tracks          = tracks != null ? tracks : List.of(AssessmentTrack.GD,AssessmentTrack.INTERVIEW,AssessmentTrack.DOJO);
            this.weightOverrides = overrides != null ? overrides : new LinkedHashMap<>();
            this.status          = status != null ? status : "ACTIVE";
            this.createdAt       = Instant.now();
        }

        public Map<String,Object> toMap() {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("cycle_id",        cycleId);
            m.put("tenant_id",       tenantId);
            m.put("job_role_id",     jobRoleId);
            m.put("tracks",          tracks.stream().map(Enum::name).collect(java.util.stream.Collectors.toList()));
            m.put("weight_overrides",weightOverrides);
            m.put("status",          status);
            m.put("created_at",      createdAt.toString());
            return m;
        }
    }

    // ── DLQ entry ─────────────────────────────────────────────────────────────

    public static class DlqEntry {
        public final String dlqId;
        public final String candidateId;
        public final String cycleId;
        public final String tenantId;
        public final String originalEventId;
        public final String failureReason;
        public final int    retryCount;
        public final String originalEvent;
        public final Instant enqueuedAt;
        public String status; // PENDING / RESOLVED / REQUEUED

        public DlqEntry(String candidateId, String cycleId, String tenantId,
                        String originalEventId, String failureReason, int retryCount, String originalEvent) {
            this.dlqId           = UUID.randomUUID().toString();
            this.candidateId     = candidateId;
            this.cycleId         = cycleId;
            this.tenantId        = tenantId;
            this.originalEventId = originalEventId;
            this.failureReason   = failureReason;
            this.retryCount      = retryCount;
            this.originalEvent   = originalEvent;
            this.enqueuedAt      = Instant.now();
            this.status          = "PENDING";
        }

        public Map<String,Object> toMap() {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("dlq_id",            dlqId);
            m.put("candidate_id",      candidateId);
            m.put("cycle_id",          cycleId);
            m.put("tenant_id",         tenantId);
            m.put("original_event_id", originalEventId);
            m.put("failure_reason",    failureReason);
            m.put("retry_count",       retryCount);
            m.put("original_event",    originalEvent);
            m.put("enqueued_at",       enqueuedAt.toString());
            m.put("status",            status);
            return m;
        }
    }
}
