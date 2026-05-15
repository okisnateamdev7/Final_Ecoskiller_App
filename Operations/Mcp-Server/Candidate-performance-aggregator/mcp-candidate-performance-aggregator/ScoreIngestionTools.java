package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.*;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.AggSecurityConfig;

import java.time.Instant;
import java.util.*;

/** Tools: ingest_track_score, ingest_kafka_event, ingest_gd_snapshot, ingest_interview_snapshot */
public class ScoreIngestionTools {

    private final AggRepository repo = AggRepository.getInstance();

    // ─────────────────────────────────────────────────────────────────────────
    // ingest_track_score
    // ─────────────────────────────────────────────────────────────────────────

    public String ingestTrackScore(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_SERVICE_ACCOUNT, AggSecurityConfig.ROLE_ADMIN);
        String tenantId     = AggSecurityConfig.effectiveTenant(claims, args);
        String candidateId  = AggSecurityConfig.requireString(args, "candidate_id");
        String cycleId      = AggSecurityConfig.requireString(args, "cycle_id");
        String eventId      = AggSecurityConfig.requireString(args, "event_id");
        String trackStr     = AggSecurityConfig.requireString(args, "track");
        String rawScoreStr  = AggSecurityConfig.requireString(args, "raw_score");
        String scoredAtStr  = AggSecurityConfig.getString(args, "scored_at");
        String metadataStr  = AggSecurityConfig.getString(args, "metadata");

        double rawScore;
        try { rawScore = Double.parseDouble(rawScoreStr); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("raw_score must be a number"); }
        if (rawScore < 0 || rawScore > 100) throw new IllegalArgumentException("raw_score must be 0-100");

        AggModels.AssessmentTrack track = AggModels.AssessmentTrack.fromString(trackStr);

        // Deduplication
        if (!AggSecurityConfig.registerEventId(eventId))
            return resp("DUPLICATE", "Event already processed: " + eventId,
                    Map.of("event_id", eventId, "candidate_id", candidateId, "action", "SKIPPED"));

        Instant scoredAt;
        try { scoredAt = scoredAtStr != null ? Instant.parse(scoredAtStr) : Instant.now(); }
        catch (Exception e) { scoredAt = Instant.now(); }

        Map<String,Object> metadata = new LinkedHashMap<>();
        if (metadataStr != null && !metadataStr.isBlank()) {
            try { metadata = JsonUtil.parseObject(metadataStr); } catch (Exception ignored) {}
        }

        AggModels.TrackScore score = new AggModels.TrackScore(eventId, candidateId, cycleId, tenantId, track, rawScore, scoredAt, metadata);
        repo.saveTrackScore(score);
        repo.audit("TRACK_SCORE_INGESTED", candidateId, cycleId, tenantId,
                "track=" + track + " score=" + rawScore + " event=" + eventId, true);
        repo.inc("events_consumed_total");

        return resp("CREATED", "Track score ingested", score.toMap());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ingest_kafka_event (match.scored — primary trigger)
    // ─────────────────────────────────────────────────────────────────────────

    public String ingestKafkaEvent(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_SERVICE_ACCOUNT, AggSecurityConfig.ROLE_ADMIN);
        String tenantId      = AggSecurityConfig.effectiveTenant(claims, args);
        String payloadJson   = AggSecurityConfig.requireString(args, "event_payload");
        String messageId     = AggSecurityConfig.getString(args, "message_id");
        boolean autoAggregate= AggSecurityConfig.getBool(args, "auto_aggregate", true);

        // Dedup by kafka message_id
        if (messageId != null && !AggSecurityConfig.registerEventId("kafka:" + messageId))
            return resp("DUPLICATE", "Kafka message already processed: " + messageId,
                    Map.of("message_id", messageId));

        Map<String,Object> event;
        try { event = JsonUtil.parseObject(payloadJson); }
        catch (Exception e) { throw new IllegalArgumentException("Invalid event_payload JSON: " + e.getMessage()); }

        String eventId      = (String) event.getOrDefault("event_id",   AggSecurityConfig.generateId());
        String candidateId  = (String) event.get("candidate_id");
        String cycleId      = (String) event.get("cycle_id");
        String trackStr     = (String) event.getOrDefault("track", "DOJO");
        Object scoreObj     = event.get("raw_score");
        String tsStr        = (String) event.get("scored_at");

        if (candidateId == null || cycleId == null || scoreObj == null)
            throw new IllegalArgumentException("event_payload missing: candidate_id, cycle_id, raw_score");

        // Dedup by event_id
        if (!AggSecurityConfig.registerEventId(eventId))
            return resp("DUPLICATE", "Event already processed: " + eventId, Map.of("event_id", eventId));

        double rawScore;
        try { rawScore = Double.parseDouble(scoreObj.toString()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("raw_score must be a number"); }
        if (rawScore < 0 || rawScore > 100) throw new IllegalArgumentException("raw_score 0-100 required");

        AggModels.AssessmentTrack track = AggModels.AssessmentTrack.fromString(trackStr);
        Instant scoredAt;
        try { scoredAt = tsStr != null ? Instant.parse(tsStr) : Instant.now(); }
        catch (Exception e) { scoredAt = Instant.now(); }

        AggModels.TrackScore score = new AggModels.TrackScore(eventId, candidateId, cycleId, tenantId, track, rawScore, scoredAt,
                Map.of("source", "kafka", "event_type", "match.scored"));
        repo.saveTrackScore(score);
        repo.inc("events_consumed_total");

        repo.audit("KAFKA_EVENT_INGESTED", candidateId, cycleId, tenantId,
                "event=match.scored track=" + track + " score=" + rawScore, true);

        Map<String,Object> result = new LinkedHashMap<>(score.toMap());
        String aggregateStatus = "SKIPPED_AUTO_AGGREGATE_FALSE";

        // Auto-trigger aggregation
        if (autoAggregate) {
            try {
                AggregationEngine engine = new AggregationEngine();
                AggModels.AggregateRecord agg = engine.aggregate(tenantId, candidateId, cycleId, false);
                result.put("auto_aggregation", Map.of(
                        "status","COMPUTED",
                        "normalized_score", agg.normalizedScore,
                        "belt_eligible_level", agg.beltEligibleLevel.name(),
                        "aggregate_id", agg.aggregateId));
                aggregateStatus = "COMPUTED";
            } catch (Exception e) {
                result.put("auto_aggregation", Map.of("status","SKIPPED","reason", e.getMessage()));
                aggregateStatus = "PARTIAL";
            }
        }

        result.put("auto_aggregate_status", aggregateStatus);
        return resp("OK", "Kafka event match.scored ingested", result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ingest_gd_snapshot
    // ─────────────────────────────────────────────────────────────────────────

    public String ingestGdSnapshot(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_SERVICE_ACCOUNT, AggSecurityConfig.ROLE_ADMIN);
        String tenantId    = AggSecurityConfig.effectiveTenant(claims, args);
        String candidateId = AggSecurityConfig.requireString(args, "candidate_id");
        String cycleId     = AggSecurityConfig.requireString(args, "cycle_id");
        String eventId     = AggSecurityConfig.requireString(args, "event_id");
        String gdScoreStr  = AggSecurityConfig.requireString(args, "gd_score");
        String sessionId   = AggSecurityConfig.getString(args, "session_id");
        String scoredAtStr = AggSecurityConfig.getString(args, "scored_at");

        double gdScore;
        try { gdScore = Double.parseDouble(gdScoreStr); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("gd_score must be a number"); }
        if (gdScore < 0 || gdScore > 100) throw new IllegalArgumentException("gd_score must be 0-100");

        if (!AggSecurityConfig.registerEventId(eventId))
            return resp("DUPLICATE", "GD snapshot already processed: " + eventId,
                    Map.of("event_id", eventId, "topic", "gd.scored.snapshot"));

        Instant scoredAt;
        try { scoredAt = scoredAtStr != null ? Instant.parse(scoredAtStr) : Instant.now(); }
        catch (Exception e) { scoredAt = Instant.now(); }

        Map<String,Object> meta = new LinkedHashMap<>();
        meta.put("source","kafka"); meta.put("topic","gd.scored.snapshot");
        if (sessionId != null) meta.put("session_id", sessionId);

        AggModels.TrackScore score = new AggModels.TrackScore(eventId, candidateId, cycleId, tenantId,
                AggModels.AssessmentTrack.GD, gdScore, scoredAt, meta);
        repo.saveTrackScore(score);
        repo.inc("events_consumed_total");
        repo.audit("GD_SNAPSHOT_INGESTED", candidateId, cycleId, tenantId,
                "gd_score=" + gdScore + " session=" + sessionId, true);

        return resp("OK", "GD snapshot ingested (gd.scored.snapshot)", score.toMap());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ingest_interview_snapshot
    // ─────────────────────────────────────────────────────────────────────────

    public String ingestInterviewSnapshot(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_SERVICE_ACCOUNT, AggSecurityConfig.ROLE_ADMIN);
        String tenantId         = AggSecurityConfig.effectiveTenant(claims, args);
        String candidateId      = AggSecurityConfig.requireString(args, "candidate_id");
        String cycleId          = AggSecurityConfig.requireString(args, "cycle_id");
        String eventId          = AggSecurityConfig.requireString(args, "event_id");
        String interviewScoreStr= AggSecurityConfig.requireString(args, "interview_score");
        String interviewId      = AggSecurityConfig.getString(args, "interview_id");
        String scoredAtStr      = AggSecurityConfig.getString(args, "scored_at");

        double interviewScore;
        try { interviewScore = Double.parseDouble(interviewScoreStr); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("interview_score must be a number"); }
        if (interviewScore < 0 || interviewScore > 100) throw new IllegalArgumentException("interview_score must be 0-100");

        if (!AggSecurityConfig.registerEventId(eventId))
            return resp("DUPLICATE", "Interview snapshot already processed: " + eventId,
                    Map.of("event_id", eventId, "topic", "interview.scored.snapshot"));

        Instant scoredAt;
        try { scoredAt = scoredAtStr != null ? Instant.parse(scoredAtStr) : Instant.now(); }
        catch (Exception e) { scoredAt = Instant.now(); }

        Map<String,Object> meta = new LinkedHashMap<>();
        meta.put("source","kafka"); meta.put("topic","interview.scored.snapshot");
        if (interviewId != null) meta.put("interview_id", interviewId);

        AggModels.TrackScore score = new AggModels.TrackScore(eventId, candidateId, cycleId, tenantId,
                AggModels.AssessmentTrack.INTERVIEW, interviewScore, scoredAt, meta);
        repo.saveTrackScore(score);
        repo.inc("events_consumed_total");
        repo.audit("INTERVIEW_SNAPSHOT_INGESTED", candidateId, cycleId, tenantId,
                "interview_score=" + interviewScore + " interview_id=" + interviewId, true);

        return resp("OK", "Interview snapshot ingested (interview.scored.snapshot)", score.toMap());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    Map<String,Object> auth(Map<String,Object> a) {
        return AggSecurityConfig.validateToken(AggSecurityConfig.requireString(a,"auth_token"));
    }
    String resp(String status, String message, Object data) {
        Map<String,Object> r = new LinkedHashMap<>();
        r.put("status",status); r.put("message",message); r.put("data",data);
        r.put("service","candidate-performance-aggregator");
        return JsonUtil.toJson(r);
    }
}
