package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.*;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.SignalSecurityConfig;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/** Tools: run_full_pipeline, fuse_multimodal_signals, detect_signal_drift, publish_aggregated_signals */
public class PipelineTools {

    private final SignalRepository repo = SignalRepository.getInstance();

    // ─────────────────────────────────────────────────────────────────────────
    // run_full_pipeline
    // ─────────────────────────────────────────────────────────────────────────

    public String runFullPipeline(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        SignalSecurityConfig.requireRole(claims, SignalSecurityConfig.ROLE_SERVICE_ACCOUNT, SignalSecurityConfig.ROLE_ADMIN);
        String tenantId     = SignalSecurityConfig.effectiveTenantId(claims, args);
        String assessmentId = SignalSecurityConfig.requireString(args, "assessment_id");
        String signalFilter = SignalSecurityConfig.getString(args, "signal_types");
        boolean doPublish   = !"false".equalsIgnoreCase(SignalSecurityConfig.getString(args,"publish"));

        long pipelineStart = System.currentTimeMillis();

        List<SignalModels.BehaviorSignal> signals = repo.getSignals(tenantId, assessmentId);
        if (signals.isEmpty())
            return resp("OK", "No signals to process", Map.of("assessment_id",assessmentId,"stages","EMPTY"));

        // ── Stage 1: Validate ─────────────────────────────────────────────────
        int totalInput = signals.size();
        int validated = 0, rejected = 0;
        for (SignalModels.BehaviorSignal s : signals) {
            SignalSecurityConfig.SignalValidationResult vr =
                    SignalSecurityConfig.validateSignalValue(s.signalType.name(), s.value);
            if (!vr.valid) { s.valid=false; s.validationReason=vr.reason; s.validityScore=0; rejected++; }
            else validated++;
        }

        // ── Stage 2: Feature extraction per signal type ───────────────────────
        Map<String, List<SignalModels.BehaviorSignal>> grouped = new LinkedHashMap<>();
        for (SignalModels.BehaviorSignal s : signals) {
            if (signalFilter != null && !signalFilter.contains(s.signalType.name())) continue;
            grouped.computeIfAbsent(s.signalType.name(), k -> new ArrayList<>()).add(s);
        }
        Map<String, SignalModels.SignalFeatures> featuresMap = new LinkedHashMap<>();
        for (Map.Entry<String, List<SignalModels.BehaviorSignal>> e : grouped.entrySet()) {
            List<Double> validVals = e.getValue().stream()
                    .filter(s -> s.valid).map(s -> s.value).collect(Collectors.toList());
            if (!validVals.isEmpty())
                featuresMap.put(e.getKey(), new SignalModels.SignalFeatures(e.getKey(), validVals));
        }

        // ── Stage 3: Anomaly detection (IQR) ─────────────────────────────────
        int anomalyCount = 0;
        for (Map.Entry<String, List<SignalModels.BehaviorSignal>> e : grouped.entrySet()) {
            List<Double> vals = e.getValue().stream().map(s -> s.value).collect(Collectors.toList());
            if (vals.size() < 4) continue;
            List<Double> sorted = new ArrayList<>(vals); Collections.sort(sorted);
            double q1=percentile(sorted,25), q3=percentile(sorted,75), iqr=q3-q1;
            double lb=q1-1.5*iqr, ub=q3+1.5*iqr;
            for (SignalModels.BehaviorSignal s : e.getValue()) {
                if (s.value < lb || s.value > ub) {
                    s.valid=false; s.validationReason="IQR_ANOMALY"; s.validityScore=0.5;
                    anomalyCount++;
                }
            }
        }
        repo.inc("anomalies_detected");

        // ── Stage 4: Aggregate ────────────────────────────────────────────────
        List<SignalModels.AggregatedSignal> aggregates = new ArrayList<>();
        Instant windowStart = Instant.now().minusSeconds(60);
        for (Map.Entry<String, List<SignalModels.BehaviorSignal>> e : grouped.entrySet()) {
            List<Double> validVals = e.getValue().stream()
                    .filter(s -> s.valid).map(s -> s.value).collect(Collectors.toList());
            SignalModels.AggregatedSignal agg = new SignalModels.AggregatedSignal(
                    assessmentId, tenantId, e.getKey(), windowStart, e.getValue(), validVals);
            repo.saveAggregate(agg);
            aggregates.add(agg);
        }

        // ── Stage 5: Publish to Kafka ─────────────────────────────────────────
        int published = 0;
        if (doPublish) {
            for (SignalModels.AggregatedSignal agg : aggregates) {
                Map<String, Object> payload = new LinkedHashMap<>(agg.toMap());
                payload.put("processing_latency_ms", System.currentTimeMillis() - pipelineStart);
                repo.logKafkaEvent("behavior.signals.aggregated", payload);
                published++;
            }
        }

        double overallQuality = signals.size() > 0
                ? (double)(validated - anomalyCount) / signals.size() : 0;

        long latencyMs = System.currentTimeMillis() - pipelineStart;

        repo.audit("FULL_PIPELINE", assessmentId, tenantId,
                "input="+totalInput+" validated="+validated+" anomalies="+anomalyCount
                        +" published="+published+" latency="+latencyMs+"ms", true);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("assessment_id",       assessmentId);
        data.put("pipeline_stages",     List.of("VALIDATE","FEATURE_EXTRACT","ANOMALY_DETECT","AGGREGATE","KAFKA_PUBLISH"));
        data.put("signals_input",       totalInput);
        data.put("signals_validated",   validated);
        data.put("signals_rejected",    rejected);
        data.put("anomalies_detected",  anomalyCount);
        data.put("signal_types_processed", grouped.size());
        data.put("features_extracted",  featuresMap.size());
        data.put("aggregates_computed", aggregates.size());
        data.put("kafka_events_published", published);
        data.put("overall_quality_score",  Math.round(overallQuality*1000.0)/1000.0);
        data.put("processing_latency_ms",  latencyMs);
        data.put("quality_alert", overallQuality < SignalSecurityConfig.getQualityThreshold()
                ? "ALERT: quality below threshold " + SignalSecurityConfig.getQualityThreshold()
                : "HEALTHY");
        return resp("OK", "Full pipeline completed in "+latencyMs+"ms", data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // fuse_multimodal_signals
    // ─────────────────────────────────────────────────────────────────────────

    public String fuseMultimodalSignals(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        String tenantId     = SignalSecurityConfig.effectiveTenantId(claims, args);
        String assessmentId = SignalSecurityConfig.requireString(args, "assessment_id");
        String modality     = SignalSecurityConfig.getString(args, "modality");
        if (modality == null) modality = "ALL";
        modality = modality.toUpperCase();
        boolean includeConf = !"false".equalsIgnoreCase(SignalSecurityConfig.getString(args,"include_confidence_score"));

        // Get all signals for this assessment
        List<SignalModels.BehaviorSignal> all = repo.getSignals(tenantId, assessmentId);
        Map<String, List<Double>> byType = new LinkedHashMap<>();
        for (SignalModels.BehaviorSignal s : all) {
            if (s.valid) byType.computeIfAbsent(s.signalType.name(), k -> new ArrayList<>()).add(s.value);
        }

        Map<String, Object> fusedVector = new LinkedHashMap<>();
        List<String> missingSignals = new ArrayList<>();

        // ── Coding modality ───────────────────────────────────────────────────
        if ("ALL".equals(modality) || "CODING".equals(modality)) {
            Map<String, Object> coding = new LinkedHashMap<>();
            coding.put("keyboard_latency_mean",       aggregateMean(byType, "KEYBOARD_LATENCY", missingSignals));
            coding.put("code_commit_frequency_mean",  aggregateMean(byType, "CODE_COMMIT_FREQUENCY", missingSignals));
            coding.put("solution_revision_count",     aggregateSum(byType, "SOLUTION_REVISION_COUNT", missingSignals));
            coding.put("completeness",                computeCompleteness(coding));
            fusedVector.put("coding", coding);
        }

        // ── GD modality ───────────────────────────────────────────────────────
        if ("ALL".equals(modality) || "GD".equals(modality)) {
            Map<String, Object> gd = new LinkedHashMap<>();
            gd.put("turn_taking_duration_mean", aggregateMean(byType, "TURN_TAKING_DURATION", missingSignals));
            gd.put("speech_rate_wpm_mean",      aggregateMean(byType, "SPEECH_RATE_WPM", missingSignals));
            gd.put("sentiment_score_mean",      aggregateMean(byType, "SENTIMENT_SCORE", missingSignals));
            gd.put("filler_word_ratio_mean",    aggregateMean(byType, "FILLER_WORD_RATIO", missingSignals));
            gd.put("interruption_count",        aggregateSum(byType, "INTERRUPTION_COUNT", missingSignals));
            gd.put("completeness",              computeCompleteness(gd));
            fusedVector.put("gd", gd);
        }

        // ── Interview modality ────────────────────────────────────────────────
        if ("ALL".equals(modality) || "INTERVIEW".equals(modality)) {
            Map<String, Object> interview = new LinkedHashMap<>();
            interview.put("speech_pause_duration_mean",aggregateMean(byType,"SPEECH_PAUSE_DURATION", missingSignals));
            interview.put("sentiment_score_mean",       aggregateMean(byType,"SENTIMENT_SCORE", missingSignals));
            interview.put("speech_rate_wpm_mean",       aggregateMean(byType,"SPEECH_RATE_WPM", missingSignals));
            if (includeConf)
                interview.put("confidence_score_mean",  aggregateMean(byType,"CONFIDENCE_SCORE", missingSignals));
            interview.put("completeness",               computeCompleteness(interview));
            fusedVector.put("interview", interview);
        }

        // Overall signal count
        int totalSignals = all.size();
        int validSignals = (int) all.stream().filter(s -> s.valid).count();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("assessment_id",  assessmentId);
        data.put("modality",       modality);
        data.put("fused_vector",   fusedVector);
        data.put("missing_signals",missingSignals);
        data.put("total_signals",  totalSignals);
        data.put("valid_signals",  validSignals);
        data.put("graceful_degradation", !missingSignals.isEmpty()
                ? "ACTIVE: scoring model will operate without: " + missingSignals
                : "NOT_NEEDED");
        return resp("OK", "Multimodal signal fusion complete", data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // detect_signal_drift
    // ─────────────────────────────────────────────────────────────────────────

    public String detectSignalDrift(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        SignalSecurityConfig.requireRole(claims, SignalSecurityConfig.ROLE_SERVICE_ACCOUNT, SignalSecurityConfig.ROLE_ADMIN);
        String tenantId      = SignalSecurityConfig.effectiveTenantId(claims, args);
        String assessmentId  = SignalSecurityConfig.requireString(args, "assessment_id");
        String signalType    = SignalSecurityConfig.requireString(args, "signal_type");
        String baselineMeanStr=SignalSecurityConfig.requireString(args, "baseline_mean");
        String driftThrStr   = SignalSecurityConfig.getString(args, "drift_threshold_pct");

        double baselineMean   = Double.parseDouble(baselineMeanStr);
        double driftThreshold = driftThrStr != null ? Double.parseDouble(driftThrStr) : 30.0;

        // Get current signals for this type
        List<SignalModels.BehaviorSignal> signals = repo.getSignalsByType(tenantId, assessmentId, signalType);
        if (signals.isEmpty())
            return resp("OK", "No signals available for drift detection",
                    Map.of("assessment_id",assessmentId,"signal_type",signalType,"drift_detected",false));

        double currentMean = signals.stream()
                .filter(s -> s.valid).mapToDouble(s -> s.value).average().orElse(0);
        double driftPct = baselineMean > 0
                ? Math.abs(currentMean - baselineMean) / baselineMean * 100.0 : 0;
        boolean driftDetected = driftPct > driftThreshold;
        String direction = currentMean > baselineMean ? "UPWARD" : "DOWNWARD";

        String recommendedAction = "NONE";
        if (driftDetected) {
            if (driftPct > 50) recommendedAction = "QUARANTINE_ASSESSMENT";
            else if (driftPct > 30) recommendedAction = "FLAG_FOR_REVIEW";
        }

        List<String> possibleCauses = new ArrayList<>();
        if (driftDetected) {
            possibleCauses.add("Keyboard/input device malfunction");
            possibleCauses.add("Network latency spike");
            possibleCauses.add("Candidate stress response");
            possibleCauses.add("Sensor hardware failure");
            if (driftPct > 50) possibleCauses.add("Assessment environment anomaly");
        }

        repo.audit("DRIFT_DETECTION", assessmentId, tenantId,
                "signal_type="+signalType+" drift_pct="+round(driftPct)+" detected="+driftDetected, true);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("assessment_id",      assessmentId);
        data.put("signal_type",        signalType);
        data.put("baseline_mean",      baselineMean);
        data.put("current_mean",       round(currentMean));
        data.put("drift_pct",          round(driftPct));
        data.put("drift_threshold_pct",driftThreshold);
        data.put("drift_detected",     driftDetected);
        data.put("direction",          direction);
        data.put("recommended_action", recommendedAction);
        data.put("possible_causes",    possibleCauses);
        data.put("signal_count",       signals.size());
        return resp("OK", driftDetected
                ? "DRIFT DETECTED: "+round(driftPct)+"% (threshold "+driftThreshold+"%)"
                : "No significant drift detected", data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // publish_aggregated_signals
    // ─────────────────────────────────────────────────────────────────────────

    public String publishAggregatedSignals(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        SignalSecurityConfig.requireRole(claims, SignalSecurityConfig.ROLE_SERVICE_ACCOUNT, SignalSecurityConfig.ROLE_ADMIN);
        String tenantId     = SignalSecurityConfig.effectiveTenantId(claims, args);
        String assessmentId = SignalSecurityConfig.requireString(args, "assessment_id");
        String signalFilter = SignalSecurityConfig.getString(args, "signal_type");

        List<SignalModels.AggregatedSignal> aggregates = repo.getAggregates(tenantId, assessmentId);
        if (aggregates.isEmpty())
            return resp("OK", "No aggregates to publish (run aggregate_signals first)",
                    Map.of("assessment_id",assessmentId,"published",0));

        long pubStart = System.currentTimeMillis();
        int published = 0;

        for (SignalModels.AggregatedSignal agg : aggregates) {
            if (signalFilter != null && !signalFilter.equalsIgnoreCase(agg.signalType)) continue;

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("assessment_id",        agg.assessmentId);
            payload.put("tenant_id",            agg.tenantId);
            payload.put("timestamp",            Instant.now().toString());
            payload.put("signal_type",          agg.signalType);
            payload.put("value",                agg.mean);
            payload.put("features_json",        JsonUtil.toJson(agg.features.toMap()));
            payload.put("quality_score",        agg.qualityScore);
            payload.put("processing_latency_ms",System.currentTimeMillis() - pubStart);
            payload.put("count",                agg.count);
            payload.put("variance",             agg.variance);

            repo.logKafkaEvent("behavior.signals.aggregated", payload);
            published++;
        }

        repo.audit("KAFKA_PUBLISH", assessmentId, tenantId, "published="+published, true);

        return resp("OK", published + " aggregated signals published to Kafka", Map.of(
                "assessment_id",   assessmentId,
                "topic",           "behavior.signals.aggregated",
                "events_published",published,
                "consumer",        "scoring-engine"));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private double aggregateMean(Map<String, List<Double>> byType, String type, List<String> missing) {
        List<Double> vals = byType.get(type);
        if (vals == null || vals.isEmpty()) { missing.add(type); return 0; }
        return Math.round(vals.stream().mapToDouble(d->d).average().orElse(0) * 1000.0) / 1000.0;
    }

    private double aggregateSum(Map<String, List<Double>> byType, String type, List<String> missing) {
        List<Double> vals = byType.get(type);
        if (vals == null || vals.isEmpty()) { missing.add(type); return 0; }
        return vals.stream().mapToDouble(d->d).sum();
    }

    private double computeCompleteness(Map<String, Object> modalityMap) {
        long nonZero = modalityMap.values().stream()
                .filter(v -> v instanceof Double && (Double)v > 0).count();
        long total = modalityMap.values().stream().filter(v -> v instanceof Double).count();
        return total > 0 ? Math.round((double) nonZero / total * 100.0) / 100.0 : 0;
    }

    private double percentile(List<Double> sorted, double pct) {
        if (sorted.isEmpty()) return 0;
        double idx = (pct/100.0)*(sorted.size()-1);
        int lo=(int)Math.floor(idx), hi=(int)Math.ceil(idx);
        if (lo==hi) return sorted.get(lo);
        return sorted.get(lo)+(idx-lo)*(sorted.get(hi)-sorted.get(lo));
    }

    private double round(double v) { return Math.round(v*1000.0)/1000.0; }

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
