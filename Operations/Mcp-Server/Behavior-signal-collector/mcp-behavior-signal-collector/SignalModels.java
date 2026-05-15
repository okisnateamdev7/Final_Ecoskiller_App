package com.ecoskiller.mcp.models;

import java.time.Instant;
import java.util.*;

/**
 * Domain models for the behavior-signal-collector pipeline.
 *
 * BehaviorSignal     — raw signal event from client/sensor
 * AggregatedSignal   — 1-minute window aggregate
 * SignalFeatures     — ML-ready feature vector extracted from a signal list
 * AssessmentSignalStore — in-memory store (production: ClickHouse + PostgreSQL)
 */
public class SignalModels {

    // ── Signal types enum ──────────────────────────────────────────────────────

    public enum SignalType {
        KEYBOARD_LATENCY, MOUSE_MOVEMENT_VELOCITY, GAZE_FIXATION_DURATION,
        GAZE_FIXATION_X, GAZE_FIXATION_Y, SPEECH_PAUSE_DURATION, SPEECH_RATE_WPM,
        SENTIMENT_SCORE, CODE_COMMIT_FREQUENCY, SOLUTION_REVISION_COUNT,
        TURN_TAKING_DURATION, CONFIDENCE_SCORE, GESTURE_VELOCITY,
        FILLER_WORD_RATIO, INTERRUPTION_COUNT, CUSTOM;

        public static SignalType fromString(String s) {
            try { return valueOf(s.toUpperCase()); }
            catch (IllegalArgumentException e) { return CUSTOM; }
        }
    }

    public enum SignalSource {
        WEB_CLIENT, MOBILE_APP, INTERVIEW_RECORDER, GD_ORCHESTRATOR,
        ASSESSMENT_SERVICE, EYE_TRACKER, AUDIO_ANALYZER, SCREEN_RECORDER, UNKNOWN;

        public static SignalSource fromString(String s) {
            try { return valueOf(s.toUpperCase()); }
            catch (IllegalArgumentException e) { return UNKNOWN; }
        }
    }

    // ── Raw signal ─────────────────────────────────────────────────────────────

    public static class BehaviorSignal {
        public final String signalId;
        public final String messageId;      // dedup key
        public final String assessmentId;
        public final String tenantId;
        public final String candidateId;
        public final SignalType signalType;
        public final SignalSource source;
        public final double value;
        public final Instant timestamp;
        public final Map<String, Object> metadata;
        public boolean valid = true;
        public String  validationReason = "OK";
        public double  validityScore    = 1.0;

        public BehaviorSignal(String messageId, String assessmentId, String tenantId,
                              String candidateId, SignalType signalType, SignalSource source,
                              double value, Instant timestamp, Map<String, Object> metadata) {
            this.signalId    = UUID.randomUUID().toString();
            this.messageId   = messageId;
            this.assessmentId= assessmentId;
            this.tenantId    = tenantId;
            this.candidateId = candidateId;
            this.signalType  = signalType;
            this.source      = source;
            this.value       = value;
            this.timestamp   = timestamp != null ? timestamp : Instant.now();
            this.metadata    = metadata != null ? metadata : new LinkedHashMap<>();
        }

        public Map<String, Object> toMap() {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("signal_id",     signalId);
            m.put("message_id",    messageId);
            m.put("assessment_id", assessmentId);
            m.put("tenant_id",     tenantId);
            m.put("candidate_id",  candidateId);
            m.put("signal_type",   signalType.name());
            m.put("source",        source.name());
            m.put("value",         value);
            m.put("timestamp",     timestamp.toString());
            m.put("valid",         valid);
            m.put("validity_score",validityScore);
            m.put("validation_reason", validationReason);
            return m;
        }
    }

    // ── Statistical features ───────────────────────────────────────────────────

    public static class SignalFeatures {
        public final String       signalType;
        public final int          count;
        public final double       mean;
        public final double       variance;
        public final double       stdDev;
        public final double       min;
        public final double       max;
        public final double       p50;
        public final double       p95;
        public final double       skewness;
        public final double       trend;          // slope of linear regression
        public final double       normalizedMean; // z-score normalized
        public final Instant      computedAt;

        public SignalFeatures(String signalType, List<Double> values) {
            this.signalType  = signalType;
            this.count       = values.size();
            this.computedAt  = Instant.now();

            if (values.isEmpty()) {
                mean=variance=stdDev=min=max=p50=p95=skewness=trend=normalizedMean=0; return;
            }

            List<Double> sorted = new ArrayList<>(values);
            Collections.sort(sorted);

            double sum = 0; for (double v : values) sum += v;
            mean = sum / count;
            min  = sorted.get(0);
            max  = sorted.get(sorted.size()-1);
            p50  = percentile(sorted, 50);
            p95  = percentile(sorted, 95);

            double sumSq = 0;
            for (double v : values) sumSq += (v - mean) * (v - mean);
            variance = sumSq / count;
            stdDev   = Math.sqrt(variance);

            // Skewness (Pearson's approximation)
            skewness = stdDev > 0 ? (mean - p50) / stdDev : 0;

            // Trend (slope of linear regression y=values, x=index)
            double xMean = (count - 1.0) / 2.0;
            double num = 0, den = 0;
            for (int i = 0; i < count; i++) {
                num += (i - xMean) * (values.get(i) - mean);
                den += (i - xMean) * (i - xMean);
            }
            trend = den > 0 ? num / den : 0;

            // Z-score normalization of mean relative to range
            normalizedMean = stdDev > 0 ? (mean - min) / (max - min) : 0;
        }

        private static double percentile(List<Double> sorted, double pct) {
            if (sorted.isEmpty()) return 0;
            double idx = (pct / 100.0) * (sorted.size() - 1);
            int lo = (int) Math.floor(idx), hi = (int) Math.ceil(idx);
            if (lo == hi) return sorted.get(lo);
            return sorted.get(lo) + (idx - lo) * (sorted.get(hi) - sorted.get(lo));
        }

        public Map<String, Object> toMap() {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("signal_type",    signalType);
            m.put("count",          count);
            m.put("mean",           round(mean));
            m.put("variance",       round(variance));
            m.put("std_dev",        round(stdDev));
            m.put("min",            round(min));
            m.put("max",            round(max));
            m.put("p50",            round(p50));
            m.put("p95",            round(p95));
            m.put("skewness",       round(skewness));
            m.put("trend",          round(trend));
            m.put("trend_direction",trend > 0.01 ? "IMPROVING" : trend < -0.01 ? "DECLINING" : "STABLE");
            m.put("normalized_mean",round(normalizedMean));
            m.put("computed_at",    computedAt.toString());
            return m;
        }

        private double round(double v) { return Math.round(v * 1000.0) / 1000.0; }
    }

    // ── 1-minute window aggregate ──────────────────────────────────────────────

    public static class AggregatedSignal {
        public final String  aggregateId;
        public final String  assessmentId;
        public final String  tenantId;
        public final String  signalType;
        public final Instant windowStart;
        public final Instant windowEnd;
        public final int     count;
        public final double  mean;
        public final double  variance;
        public final double  min;
        public final double  max;
        public final double  qualityScore;  // fraction of valid signals
        public final int     anomalyCount;
        public final SignalFeatures features;

        public AggregatedSignal(String assessmentId, String tenantId, String signalType,
                                Instant windowStart, List<BehaviorSignal> signals,
                                List<Double> validValues) {
            this.aggregateId  = UUID.randomUUID().toString();
            this.assessmentId = assessmentId;
            this.tenantId     = tenantId;
            this.signalType   = signalType;
            this.windowStart  = windowStart;
            this.windowEnd    = Instant.now();
            this.count        = signals.size();
            this.anomalyCount = (int) signals.stream().filter(s -> !s.valid).count();
            this.qualityScore = count > 0 ? (double)(count - anomalyCount) / count : 0;
            this.features     = new SignalFeatures(signalType, validValues);
            this.mean         = features.mean;
            this.variance     = features.variance;
            this.min          = features.min;
            this.max          = features.max;
        }

        public Map<String, Object> toMap() {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("aggregate_id",  aggregateId);
            m.put("assessment_id", assessmentId);
            m.put("tenant_id",     tenantId);
            m.put("signal_type",   signalType);
            m.put("window_start",  windowStart.toString());
            m.put("window_end",    windowEnd.toString());
            m.put("count",         count);
            m.put("mean",          features.mean);
            m.put("variance",      features.variance);
            m.put("min",           features.min);
            m.put("max",           features.max);
            m.put("quality_score", qualityScore);
            m.put("anomaly_count", anomalyCount);
            m.put("features",      features.toMap());
            return m;
        }
    }

    // ── Anomaly detection result ───────────────────────────────────────────────

    public static class AnomalyResult {
        public final String signalId;
        public final String signalType;
        public final double value;
        public final boolean isAnomaly;
        public final String  method;
        public final String  reason;
        public final double  severity; // 0-1

        public AnomalyResult(String signalId, String signalType, double value,
                             boolean isAnomaly, String method, String reason, double severity) {
            this.signalId  = signalId; this.signalType = signalType; this.value = value;
            this.isAnomaly = isAnomaly; this.method = method; this.reason = reason;
            this.severity  = severity;
        }

        public Map<String, Object> toMap() {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("signal_id",   signalId); m.put("signal_type", signalType);
            m.put("value",       value);    m.put("is_anomaly",  isAnomaly);
            m.put("method",      method);   m.put("reason",      reason);
            m.put("severity",    severity);
            return m;
        }
    }
}
