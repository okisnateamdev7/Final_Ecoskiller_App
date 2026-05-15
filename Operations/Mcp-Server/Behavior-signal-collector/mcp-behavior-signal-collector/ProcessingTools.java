package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.*;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.SignalSecurityConfig;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/** Tools: extract_features, normalize_signals, detect_anomalies, aggregate_signals */
public class ProcessingTools {

    private final SignalRepository repo = SignalRepository.getInstance();

    // ─────────────────────────────────────────────────────────────────────────
    // extract_features
    // ─────────────────────────────────────────────────────────────────────────

    public String extractFeatures(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        String tenantId     = SignalSecurityConfig.effectiveTenantId(claims, args);
        String assessmentId = SignalSecurityConfig.requireString(args, "assessment_id");
        String signalType   = SignalSecurityConfig.requireString(args, "signal_type");
        String valuesStr    = SignalSecurityConfig.requireString(args, "values");
        String windowLabel  = SignalSecurityConfig.sanitise(SignalSecurityConfig.getString(args,"window_label"),"window_label");

        List<Double> values = parseDoubleList(valuesStr);
        if (values.isEmpty()) throw new IllegalArgumentException("values must not be empty");

        SignalModels.SignalFeatures features = new SignalModels.SignalFeatures(signalType, values);

        repo.audit("FEATURE_EXTRACTION", assessmentId, tenantId,
                "signal_type="+signalType+" count="+values.size()
                        +(windowLabel!=null?" window="+windowLabel:""), true);

        Map<String, Object> data = new LinkedHashMap<>(features.toMap());
        data.put("assessment_id",  assessmentId);
        data.put("window_label",   windowLabel);
        data.put("input_count",    values.size());
        data.put("model_note",     "Production: scipy/numpy vectorized computation; IsolationForest for multivariate anomalies");
        return resp("OK", "Features extracted for " + signalType, data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // normalize_signals
    // ─────────────────────────────────────────────────────────────────────────

    public String normalizeSignals(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        String tenantId     = SignalSecurityConfig.effectiveTenantId(claims, args);
        String assessmentId = SignalSecurityConfig.requireString(args, "assessment_id");
        String signalType   = SignalSecurityConfig.requireString(args, "signal_type");
        String valuesStr    = SignalSecurityConfig.requireString(args, "values");
        String cohort       = SignalSecurityConfig.sanitise(
                SignalSecurityConfig.getString(args,"cohort"),"cohort");
        boolean imputeMissing = !"false".equalsIgnoreCase(SignalSecurityConfig.getString(args,"impute_missing"));

        if (cohort == null) cohort = "ENGINEER";
        cohort = cohort.toUpperCase();

        List<Double> values = parseDoubleList(valuesStr);
        if (values.isEmpty()) throw new IllegalArgumentException("values must not be empty");

        // Cohort-specific normalization parameters (production: from Redis cache)
        Map<String, double[]> cohortParams = getCohortNormParams(signalType, cohort);
        double cohortMean  = cohortParams.get("mean")[0];
        double cohortStdDev= cohortParams.get("std_dev")[0];

        List<Map<String, Object>> normalized = new ArrayList<>();
        int imputed = 0, outOfRange = 0;
        double sumNorm = 0;

        for (int i = 0; i < values.size(); i++) {
            double raw = values.get(i);
            double normVal;
            boolean wasImputed = false;
            String status = "OK";

            if (Double.isNaN(raw) || raw < 0) {
                if (imputeMissing) {
                    raw = cohortMean; wasImputed = true; imputed++;
                    status = "IMPUTED";
                } else {
                    status = "REJECTED_MISSING";
                    normalized.add(Map.of("index",i,"raw",raw,"normalized",0,"status",status));
                    continue;
                }
            }

            // Z-score normalization: (x - mean) / stdDev, then clip to [0,1]
            normVal = cohortStdDev > 0 ? (raw - cohortMean) / cohortStdDev : 0;
            // Sigmoid clamp to [0,1]
            normVal = 1.0 / (1.0 + Math.exp(-normVal));

            if (normVal < 0.01 || normVal > 0.99) outOfRange++;
            sumNorm += normVal;

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("index",      i);
            entry.put("raw",        raw);
            entry.put("normalized", Math.round(normVal * 1000.0) / 1000.0);
            entry.put("status",     status);
            entry.put("imputed",    wasImputed);
            normalized.add(entry);
        }

        double avgNorm = values.size() > 0 ? sumNorm / values.size() : 0;

        repo.audit("NORMALIZATION", assessmentId, tenantId,
                "signal_type="+signalType+" cohort="+cohort+" imputed="+imputed, true);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("assessment_id",       assessmentId);
        data.put("signal_type",         signalType);
        data.put("cohort",              cohort);
        data.put("count",               values.size());
        data.put("imputed_count",       imputed);
        data.put("out_of_range_count",  outOfRange);
        data.put("avg_normalized_value",Math.round(avgNorm * 1000.0) / 1000.0);
        data.put("cohort_mean",         cohortMean);
        data.put("cohort_std_dev",      cohortStdDev);
        data.put("normalized_values",   normalized);
        data.put("note", "Cohort normalization prevents bias: " + cohort + " cohort parameters applied");
        return resp("OK", "Signals normalized using " + cohort + " cohort parameters", data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // detect_anomalies
    // ─────────────────────────────────────────────────────────────────────────

    public String detectAnomalies(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        String tenantId     = SignalSecurityConfig.effectiveTenantId(claims, args);
        String assessmentId = SignalSecurityConfig.requireString(args, "assessment_id");
        String signalType   = SignalSecurityConfig.requireString(args, "signal_type");
        String valuesStr    = SignalSecurityConfig.requireString(args, "values");
        String handling     = SignalSecurityConfig.getString(args, "handling");
        String iqrMultStr   = SignalSecurityConfig.getString(args, "iqr_multiplier");

        if (handling == null) handling = "FLAG";
        handling = handling.toUpperCase();
        double iqrMult = iqrMultStr != null ? Double.parseDouble(iqrMultStr)
                : SignalSecurityConfig.getIqrMultiplier();

        List<Double> values = parseDoubleList(valuesStr);
        if (values.isEmpty()) throw new IllegalArgumentException("values must not be empty");

        // IQR anomaly detection
        List<Double> sorted = new ArrayList<>(values);
        Collections.sort(sorted);

        double q1  = percentile(sorted, 25);
        double q3  = percentile(sorted, 75);
        double iqr = q3 - q1;
        double lb  = q1 - iqrMult * iqr;
        double ub  = q3 + iqrMult * iqr;

        List<SignalModels.AnomalyResult> anomalies = new ArrayList<>();
        List<Double> cleanValues = new ArrayList<>();
        int anomalyCount = 0;
        double p99 = percentile(sorted, 99);
        double totalSeverity = 0;

        for (int i = 0; i < values.size(); i++) {
            double v = values.get(i);
            boolean isAnomaly = v < lb || v > ub;

            // Also check range validation
            SignalSecurityConfig.SignalValidationResult rangeCheck =
                    SignalSecurityConfig.validateSignalValue(signalType, v);
            if (!rangeCheck.valid) isAnomaly = true;

            double severity = 0;
            if (isAnomaly) {
                anomalyCount++;
                severity = Math.min(1.0, Math.abs(v - (v > ub ? ub : lb)) / (iqr > 0 ? iqr : 1));
                totalSeverity += severity;
            }

            String signalId = "sig_" + assessmentId + "_" + i;
            String reason   = isAnomaly
                    ? (v < lb ? "BELOW_LOWER_BOUND("+round(lb)+")" : (v > ub ? "ABOVE_UPPER_BOUND("+round(ub)+")" : "RANGE_INVALID"))
                    : "OK";

            anomalies.add(new SignalModels.AnomalyResult(signalId, signalType, v,
                    isAnomaly, "IQR", reason, severity));

            // Apply handling
            switch (handling) {
                case "REMOVE": if (!isAnomaly) cleanValues.add(v); break;
                case "CAP":    cleanValues.add(isAnomaly ? Math.min(Math.max(v, lb), p99) : v); break;
                case "FLAG":   cleanValues.add(v); break; // keep all, just flagged
                default:       cleanValues.add(v);
            }
        }

        double avgSeverity = anomalyCount > 0 ? totalSeverity / anomalyCount : 0;
        double qualityScore = values.size() > 0
                ? ((values.size() - anomalyCount) / (double) values.size()) * (1 - avgSeverity)
                : 1.0;

        repo.inc("anomalies_detected");
        repo.audit("ANOMALY_DETECTION", assessmentId, tenantId,
                "signal_type="+signalType+" detected="+anomalyCount
                        +" handling="+handling+" quality="+round(qualityScore), true);

        List<Map<String, Object>> anomalyMaps = anomalies.stream()
                .filter(a -> a.isAnomaly)
                .map(SignalModels.AnomalyResult::toMap)
                .collect(java.util.stream.Collectors.toList());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("assessment_id",    assessmentId);
        data.put("signal_type",      signalType);
        data.put("total_signals",    values.size());
        data.put("anomaly_count",    anomalyCount);
        data.put("anomaly_rate",     round((double) anomalyCount / values.size()));
        data.put("quality_score",    round(qualityScore));
        data.put("iqr_bounds",       Map.of("q1",round(q1),"q3",round(q3),"iqr",round(iqr),"lower",round(lb),"upper",round(ub)));
        data.put("handling",         handling);
        data.put("clean_value_count",cleanValues.size());
        data.put("anomalies",        anomalyMaps);
        data.put("quality_alert",    qualityScore < SignalSecurityConfig.getQualityThreshold()
                ? "ALERT: quality_score " + round(qualityScore) + " < threshold " + SignalSecurityConfig.getQualityThreshold()
                : "OK");
        return resp("OK", anomalyCount + " anomalies detected in " + values.size() + " signals", data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // aggregate_signals
    // ─────────────────────────────────────────────────────────────────────────

    public String aggregateSignals(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        SignalSecurityConfig.requireRole(claims, SignalSecurityConfig.ROLE_SERVICE_ACCOUNT, SignalSecurityConfig.ROLE_ADMIN);
        String tenantId     = SignalSecurityConfig.effectiveTenantId(claims, args);
        String assessmentId = SignalSecurityConfig.requireString(args, "assessment_id");
        String signalTypeFilter = SignalSecurityConfig.getString(args, "signal_type");
        int windowMinutes   = SignalSecurityConfig.getInt(args, "window_minutes", 1);
        boolean allTypes    = "ALL".equalsIgnoreCase(signalTypeFilter) || signalTypeFilter == null;

        List<SignalModels.BehaviorSignal> signals = repo.getSignals(tenantId, assessmentId);
        if (signals.isEmpty())
            return resp("OK", "No signals found for assessment", Map.of("assessment_id", assessmentId, "aggregates", List.of()));

        // Group by signal type
        Map<String, List<SignalModels.BehaviorSignal>> grouped = new LinkedHashMap<>();
        for (SignalModels.BehaviorSignal s : signals) {
            String type = s.signalType.name();
            if (!allTypes && !type.equalsIgnoreCase(signalTypeFilter)) continue;
            grouped.computeIfAbsent(type, k -> new ArrayList<>()).add(s);
        }

        List<Map<String, Object>> aggregates = new ArrayList<>();
        Instant windowStart = Instant.now().minusSeconds(windowMinutes * 60L);

        for (Map.Entry<String, List<SignalModels.BehaviorSignal>> entry : grouped.entrySet()) {
            List<Double> validValues = entry.getValue().stream()
                    .filter(s -> s.valid)
                    .map(s -> s.value)
                    .collect(Collectors.toList());

            SignalModels.AggregatedSignal agg = new SignalModels.AggregatedSignal(
                    assessmentId, tenantId, entry.getKey(),
                    windowStart, entry.getValue(), validValues);
            repo.saveAggregate(agg);
            repo.inc("aggregates_total");
            aggregates.add(agg.toMap());
        }

        repo.audit("SIGNAL_AGGREGATION", assessmentId, tenantId,
                "types="+grouped.size()+" window_min="+windowMinutes, true);

        return resp("OK", aggregates.size() + " signal types aggregated", Map.of(
                "assessment_id",    assessmentId,
                "window_minutes",   windowMinutes,
                "signal_types",     grouped.size(),
                "aggregates",       aggregates));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private List<Double> parseDoubleList(String csv) {
        List<Double> result = new ArrayList<>();
        // Try JSON array first
        if (csv.trim().startsWith("[")) {
            @SuppressWarnings("unchecked")
            List<Object> parsed = (List<Object>) new JsonUtil.PublicParser(csv).parse();
            for (Object o : parsed) {
                try { result.add(Double.parseDouble(o.toString())); }
                catch (NumberFormatException ignored) {}
            }
            return result;
        }
        // CSV fallback
        for (String s : csv.split(",")) {
            try { result.add(Double.parseDouble(s.trim())); }
            catch (NumberFormatException ignored) {}
        }
        return result;
    }

    private double percentile(List<Double> sorted, double pct) {
        if (sorted.isEmpty()) return 0;
        double idx = (pct / 100.0) * (sorted.size() - 1);
        int lo = (int) Math.floor(idx), hi = (int) Math.ceil(idx);
        if (lo == hi) return sorted.get(lo);
        return sorted.get(lo) + (idx - lo) * (sorted.get(hi) - sorted.get(lo));
    }

    private Map<String, double[]> getCohortNormParams(String signalType, String cohort) {
        // Production: from Redis cache populated by ML training pipeline
        Map<String, Map<String, double[]>> cohortTable = new LinkedHashMap<>();
        // keyboard_latency: cohort → [mean, stdDev]
        Map<String, double[]> keyboardParams = Map.of(
                "ENGINEER", new double[]{200.0, 80.0},
                "DESIGNER", new double[]{250.0, 100.0},
                "MANAGER",  new double[]{280.0, 90.0},
                "SALES",    new double[]{260.0, 95.0});
        Map<String, double[]> speechParams = Map.of(
                "ENGINEER", new double[]{120.0, 30.0},
                "DESIGNER", new double[]{130.0, 35.0},
                "MANAGER",  new double[]{150.0, 40.0},
                "SALES",    new double[]{160.0, 45.0});
        Map<String, double[]> sentimentParams = Map.of(
                "ENGINEER", new double[]{0.2, 0.3},
                "DESIGNER", new double[]{0.3, 0.3},
                "MANAGER",  new double[]{0.25, 0.3},
                "SALES",    new double[]{0.35, 0.3});
        cohortTable.put("KEYBOARD_LATENCY", keyboardParams);
        cohortTable.put("SPEECH_RATE_WPM",  speechParams);
        cohortTable.put("SENTIMENT_SCORE",  sentimentParams);

        Map<String, double[]> typeParams = cohortTable.getOrDefault(signalType.toUpperCase(),
                Map.of(cohort, new double[]{100.0, 50.0}));
        double[] params = typeParams.getOrDefault(cohort, new double[]{100.0, 50.0});

        Map<String, double[]> result = new LinkedHashMap<>();
        result.put("mean",    new double[]{params[0]});
        result.put("std_dev", new double[]{params[1]});
        return result;
    }

    private double round(double v) { return Math.round(v * 1000.0) / 1000.0; }

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
