package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.*;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.*;

import java.time.Instant;
import java.util.*;

/** Tools: ingest_signals, ingest_kafka_event, configure_assessment */
public class IngestionTools {

    private final SignalRepository repo = SignalRepository.getInstance();

    // ─────────────────────────────────────────────────────────────────────────
    // ingest_signals
    // ─────────────────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    public String ingestSignals(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        SignalSecurityConfig.requireRole(claims,
                SignalSecurityConfig.ROLE_SERVICE_ACCOUNT,
                SignalSecurityConfig.ROLE_CANDIDATE,
                SignalSecurityConfig.ROLE_RECRUITER,
                SignalSecurityConfig.ROLE_ADMIN);
        String tenantId    = SignalSecurityConfig.effectiveTenantId(claims, args);
        String assessmentId= SignalSecurityConfig.requireString(args, "assessment_id");
        String candidateId = SignalSecurityConfig.getString(args, "candidate_id");
        String signalsJson = SignalSecurityConfig.requireString(args, "signals");
        String sourceName  = SignalSecurityConfig.sanitise(SignalSecurityConfig.getString(args,"source"),"source");

        // Parse signals array
        List<Object> signalList;
        try {
            Object parsed = new com.ecoskiller.mcp.protocol.JsonUtil.PublicParser(signalsJson).parse();
            if (!(parsed instanceof List)) throw new IllegalArgumentException("signals must be a JSON array");
            signalList = (List<Object>) parsed;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid signals JSON: " + e.getMessage());
        }

        // Batch size check
        if (signalList.size() > SignalSecurityConfig.getMaxBatchSize())
            throw new IllegalArgumentException("Batch too large: " + signalList.size()
                    + " > max " + SignalSecurityConfig.getMaxBatchSize());

        SignalModels.SignalSource source = SignalModels.SignalSource.fromString(
                sourceName != null ? sourceName : "UNKNOWN");

        int accepted = 0, rejected = 0, duplicates = 0;
        List<Map<String, Object>> results = new ArrayList<>();

        for (Object raw : signalList) {
            if (!(raw instanceof Map)) { rejected++; continue; }
            Map<String, Object> event = (Map<String, Object>) raw;

            String messageId  = SignalSecurityConfig.getString(event, "message_id");
            String signalType = SignalSecurityConfig.getString(event, "signal_type");
            Object valueObj   = event.get("value");
            String tsStr      = SignalSecurityConfig.getString(event, "timestamp");

            // Deduplication
            if (messageId != null && !SignalSecurityConfig.checkDeduplication(messageId)) {
                duplicates++;
                results.add(Map.of("message_id", messageId, "status", "DUPLICATE"));
                continue;
            }

            // Required fields
            if (signalType == null || valueObj == null) {
                rejected++;
                results.add(Map.of("message_id", messageId!=null?messageId:"?", "status","REJECTED","reason","MISSING_FIELDS"));
                continue;
            }

            // Parse value
            double value;
            try { value = Double.parseDouble(valueObj.toString()); }
            catch (NumberFormatException e) {
                rejected++;
                results.add(Map.of("message_id", messageId!=null?messageId:"?", "status","REJECTED","reason","INVALID_VALUE"));
                continue;
            }

            // Range validation
            SignalSecurityConfig.SignalValidationResult validation =
                    SignalSecurityConfig.validateSignalValue(signalType, value);

            Instant ts;
            try { ts = tsStr != null ? Instant.parse(tsStr) : Instant.now(); }
            catch (Exception e) { ts = Instant.now(); }

            @SuppressWarnings("unchecked")
            Map<String, Object> metadata = event.get("metadata") instanceof Map
                    ? (Map<String, Object>) event.get("metadata") : new LinkedHashMap<>();

            SignalModels.BehaviorSignal signal = new SignalModels.BehaviorSignal(
                    messageId != null ? messageId : SignalSecurityConfig.generateId(),
                    assessmentId, tenantId,
                    candidateId != null ? candidateId : (String) claims.get("sub"),
                    SignalModels.SignalType.fromString(signalType), source,
                    value, ts, metadata);

            if (!validation.valid) {
                signal.valid           = false;
                signal.validationReason= validation.reason;
                signal.validityScore   = 0.0;
                rejected++;
            } else {
                accepted++;
            }

            repo.saveSignal(signal);
            results.add(Map.of(
                    "message_id",  signal.messageId,
                    "signal_id",   signal.signalId,
                    "status",      validation.valid ? "ACCEPTED" : "REJECTED",
                    "reason",      validation.reason));
        }

        repo.audit("SIGNAL_INGEST", assessmentId, tenantId,
                "accepted="+accepted+" rejected="+rejected+" duplicates="+duplicates, true);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("assessment_id", assessmentId);
        data.put("total_received",signalList.size());
        data.put("accepted",      accepted);
        data.put("rejected",      rejected);
        data.put("duplicates",    duplicates);
        data.put("results",       results.subList(0, Math.min(results.size(), 100)));
        data.put("note",          results.size() > 100 ? "First 100 results shown" : "All results shown");
        return resp("OK", accepted + " signals ingested", data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ingest_kafka_event
    // ─────────────────────────────────────────────────────────────────────────

    public String ingestKafkaEvent(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        SignalSecurityConfig.requireRole(claims, SignalSecurityConfig.ROLE_SERVICE_ACCOUNT, SignalSecurityConfig.ROLE_ADMIN);
        String tenantId    = SignalSecurityConfig.effectiveTenantId(claims, args);
        String eventType   = SignalSecurityConfig.requireString(args, "event_type");
        String payloadJson = SignalSecurityConfig.requireString(args, "event_payload");
        String messageId   = SignalSecurityConfig.getString(args, "message_id");

        // Deduplication
        if (messageId != null && !SignalSecurityConfig.checkDeduplication("kafka:" + messageId))
            return resp("DUPLICATE", "Kafka event already processed: " + messageId,
                    Map.of("message_id", messageId, "event_type", eventType));

        // Parse payload
        Map<String, Object> payload;
        try { payload = JsonUtil.parseObject(payloadJson); }
        catch (Exception e) { throw new IllegalArgumentException("Invalid event_payload JSON: " + e.getMessage()); }

        String assessmentId = (String) payload.getOrDefault("assessment_id",
                payload.getOrDefault("gd_session_id",
                payload.getOrDefault("interview_id", "unknown")));

        List<SignalModels.BehaviorSignal> createdSignals = new ArrayList<>();

        switch (eventType) {
            case "assessment.submission":
                createSignal(createdSignals, messageId, assessmentId, tenantId, null,
                        "SOLUTION_REVISION_COUNT", SignalModels.SignalSource.ASSESSMENT_SERVICE,
                        parseDouble(payload, "revision_count", 0), null, payload);
                createSignal(createdSignals, null, assessmentId, tenantId, null,
                        "CODE_COMMIT_FREQUENCY", SignalModels.SignalSource.ASSESSMENT_SERVICE,
                        parseDouble(payload, "code_length", 0) / 1000.0, null, payload);
                break;
            case "gd.speaker.turn":
                createSignal(createdSignals, messageId, assessmentId, tenantId,
                        (String) payload.get("speaker_id"),
                        "TURN_TAKING_DURATION", SignalModels.SignalSource.GD_ORCHESTRATOR,
                        parseDouble(payload, "duration_ms", 0), null, payload);
                break;
            case "interview.recorder_event":
                createSignal(createdSignals, messageId, assessmentId, tenantId, null,
                        "SENTIMENT_SCORE", SignalModels.SignalSource.INTERVIEW_RECORDER,
                        parseDouble(payload, "audio_sentiment_score", 0), null, payload);
                createSignal(createdSignals, null, assessmentId, tenantId, null,
                        "SPEECH_RATE_WPM", SignalModels.SignalSource.INTERVIEW_RECORDER,
                        parseDouble(payload, "speech_rate", 0), null, payload);
                break;
            default:
                throw new IllegalArgumentException("Unknown event_type: " + eventType);
        }

        for (SignalModels.BehaviorSignal s : createdSignals) repo.saveSignal(s);

        repo.audit("KAFKA_EVENT_INGESTED", assessmentId, tenantId,
                "event_type="+eventType+" signals_created="+createdSignals.size(), true);

        return resp("OK", createdSignals.size() + " signals created from Kafka event", Map.of(
                "event_type",     eventType,
                "assessment_id",  assessmentId,
                "signals_created",createdSignals.size()));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // configure_assessment
    // ─────────────────────────────────────────────────────────────────────────

    public String configureAssessment(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        SignalSecurityConfig.requireRole(claims, SignalSecurityConfig.ROLE_RECRUITER, SignalSecurityConfig.ROLE_ADMIN);
        String tenantId    = SignalSecurityConfig.effectiveTenantId(claims, args);
        String assessmentId= SignalSecurityConfig.requireString(args, "assessment_id");
        String signalTypes = SignalSecurityConfig.getString(args, "signal_types");
        String modelVersion= SignalSecurityConfig.sanitise(SignalSecurityConfig.getString(args,"feature_model_version"),"feature_model_version");
        String cohort      = SignalSecurityConfig.sanitise(SignalSecurityConfig.getString(args,"cohort"),"cohort");
        String anomalyThr  = SignalSecurityConfig.getString(args, "anomaly_threshold");
        String qualityThr  = SignalSecurityConfig.getString(args, "quality_threshold");

        Map<String, Object> config = new LinkedHashMap<>();
        config.put("assessment_id",       assessmentId);
        config.put("tenant_id",           tenantId);
        config.put("signal_types",        signalTypes != null ? Arrays.asList(signalTypes.split(",")) : List.of());
        config.put("feature_model_version",modelVersion != null ? modelVersion : "v2");
        config.put("cohort",              cohort != null ? cohort.toUpperCase() : "ENGINEER");
        config.put("anomaly_threshold",   anomalyThr != null ? Double.parseDouble(anomalyThr) : 1.5);
        config.put("quality_threshold",   qualityThr != null ? Double.parseDouble(qualityThr) : 0.85);
        config.put("configured_by",       claims.get("sub"));
        config.put("configured_at",       Instant.now().toString());
        config.put("redis_cache_ttl_sec", 3600);

        repo.saveConfig(assessmentId, config);
        repo.audit("ASSESSMENT_CONFIGURED", assessmentId, tenantId,
                "cohort="+config.get("cohort")+" model="+config.get("feature_model_version"), true);

        return resp("OK", "Assessment configured for signal collection", config);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void createSignal(List<SignalModels.BehaviorSignal> list, String messageId,
                               String assessmentId, String tenantId, String candidateId,
                               String signalType, SignalModels.SignalSource source,
                               double value, Instant ts, Map<String, Object> metadata) {
        SignalModels.BehaviorSignal s = new SignalModels.BehaviorSignal(
                messageId != null ? messageId : SignalSecurityConfig.generateId(),
                assessmentId, tenantId, candidateId,
                SignalModels.SignalType.fromString(signalType), source,
                value, ts, metadata);
        SignalSecurityConfig.SignalValidationResult vr =
                SignalSecurityConfig.validateSignalValue(signalType, value);
        if (!vr.valid) { s.valid=false; s.validationReason=vr.reason; s.validityScore=0.0; }
        list.add(s);
    }

    private double parseDouble(Map<String, Object> m, String key, double def) {
        Object v = m.get(key); if (v==null) return def;
        try { return Double.parseDouble(v.toString()); } catch (NumberFormatException e) { return def; }
    }

    private Map<String, Object> auth(Map<String, Object> args) {
        return SignalSecurityConfig.validateToken(SignalSecurityConfig.requireString(args,"auth_token"));
    }

    private String resp(String status, String message, Object data) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("status",status); r.put("message",message); r.put("data",data);
        r.put("service","behavior-signal-collector");
        return JsonUtil.toJson(r);
    }
}
