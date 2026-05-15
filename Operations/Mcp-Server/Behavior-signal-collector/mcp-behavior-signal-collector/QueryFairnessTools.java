package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.BehaviorSignalMCPServer;
import com.ecoskiller.mcp.models.*;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.SignalSecurityConfig;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/** Tools: get_signal_quality, get_assessment_signals, replay_assessment, get_metrics, get_kafka_event_log */
class QueryTools {

    private final SignalRepository repo = SignalRepository.getInstance();

    public String getSignalQuality(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        SignalSecurityConfig.requireRole(claims, SignalSecurityConfig.ROLE_RECRUITER, SignalSecurityConfig.ROLE_ADMIN);
        String tenantId     = SignalSecurityConfig.effectiveTenantId(claims, args);
        String assessmentId = SignalSecurityConfig.requireString(args, "assessment_id");

        List<SignalModels.BehaviorSignal> signals = repo.getSignals(tenantId, assessmentId);

        long total   = signals.size();
        long valid   = signals.stream().filter(s -> s.valid).count();
        long invalid = total - valid;
        long anomalies = signals.stream().filter(s -> !s.valid && s.validationReason.contains("ANOMALY")).count();

        // Breakdown by signal type
        Map<String, Map<String, Object>> breakdown = new LinkedHashMap<>();
        Map<String, List<SignalModels.BehaviorSignal>> grouped = new LinkedHashMap<>();
        for (SignalModels.BehaviorSignal s : signals)
            grouped.computeIfAbsent(s.signalType.name(), k -> new ArrayList<>()).add(s);

        for (Map.Entry<String, List<SignalModels.BehaviorSignal>> e : grouped.entrySet()) {
            long typeTotal = e.getValue().size();
            long typeValid = e.getValue().stream().filter(s -> s.valid).count();
            Map<String, Object> tb = new LinkedHashMap<>();
            tb.put("total",        typeTotal);
            tb.put("valid",        typeValid);
            tb.put("validity_rate",typeTotal > 0 ? Math.round((double)typeValid/typeTotal*100.0)/100.0 : 0);
            breakdown.put(e.getKey(), tb);
        }

        double validityRate = total > 0 ? (double) valid / total : 0;
        double completeness = grouped.size() > 0 ? Math.min(1.0, grouped.size() / 8.0) : 0; // 8 expected types
        double qualityScore = validityRate * 0.7 + completeness * 0.3;

        boolean alert = qualityScore < SignalSecurityConfig.getQualityThreshold();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("assessment_id",           assessmentId);
        data.put("total_signals",           total);
        data.put("valid_signals",           valid);
        data.put("invalid_signals",         invalid);
        data.put("validity_rate",           Math.round(validityRate*1000.0)/1000.0);
        data.put("anomalies_detected",      anomalies);
        data.put("signal_types_present",    grouped.size());
        data.put("feature_completeness",    Math.round(completeness*1000.0)/1000.0);
        data.put("data_quality_score",      Math.round(qualityScore*1000.0)/1000.0);
        data.put("quality_threshold",       SignalSecurityConfig.getQualityThreshold());
        data.put("quality_alert",           alert ? "⚠ ALERT: quality below threshold" : "✓ HEALTHY");
        data.put("signal_type_breakdown",   breakdown);
        data.put("recommendation",          alert
                ? "Review sensor/client connections; consider manual assessment review"
                : "Signal quality sufficient for automated scoring");
        return resp("OK", "Signal quality report generated", data);
    }

    public String getAssessmentSignals(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        String tenantId     = SignalSecurityConfig.effectiveTenantId(claims, args);
        String assessmentId = SignalSecurityConfig.requireString(args, "assessment_id");
        String signalType   = SignalSecurityConfig.getString(args, "signal_type");
        boolean validOnly   = "true".equalsIgnoreCase(SignalSecurityConfig.getString(args,"valid_only"));
        int limit           = Math.min(SignalSecurityConfig.getInt(args,"limit",100), 1000);

        List<SignalModels.BehaviorSignal> signals = signalType != null
                ? repo.getSignalsByType(tenantId, assessmentId, signalType)
                : repo.getSignals(tenantId, assessmentId);

        // CANDIDATE: own signals only
        if (SignalSecurityConfig.hasRole(claims, SignalSecurityConfig.ROLE_CANDIDATE)
                && !SignalSecurityConfig.hasRole(claims, SignalSecurityConfig.ROLE_ADMIN)) {
            String userId = (String) claims.get("sub");
            signals = signals.stream().filter(s -> userId.equals(s.candidateId)).collect(Collectors.toList());
        }

        if (validOnly) signals = signals.stream().filter(s -> s.valid).collect(Collectors.toList());

        List<SignalModels.BehaviorSignal> page = signals.stream().limit(limit).collect(Collectors.toList());
        List<Map<String, Object>> items = page.stream().map(SignalModels.BehaviorSignal::toMap).collect(Collectors.toList());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("assessment_id",assessmentId);
        data.put("total_matching",signals.size());
        data.put("limit",        limit);
        data.put("signal_type",  signalType != null ? signalType : "ALL");
        data.put("valid_only",   validOnly);
        data.put("signals",      items);
        return resp("OK", page.size() + " signals retrieved", data);
    }

    public String replayAssessment(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        SignalSecurityConfig.requireRole(claims, SignalSecurityConfig.ROLE_ADMIN);
        String tenantId       = SignalSecurityConfig.effectiveTenantId(claims, args);
        String assessmentId   = SignalSecurityConfig.requireString(args, "assessment_id");
        boolean includeRaw    = !"false".equalsIgnoreCase(SignalSecurityConfig.getString(args,"include_raw"));
        boolean includeAgg    = !"false".equalsIgnoreCase(SignalSecurityConfig.getString(args,"include_aggregated"));

        List<SignalModels.BehaviorSignal>    raw  = includeRaw  ? repo.getSignals(tenantId, assessmentId) : List.of();
        List<SignalModels.AggregatedSignal>  aggs = includeAgg  ? repo.getAggregates(tenantId, assessmentId) : List.of();

        repo.audit("REPLAY_EXPORT", assessmentId, tenantId,
                "raw="+raw.size()+" aggregated="+aggs.size()+" by="+claims.get("sub"), true);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("assessment_id",      assessmentId);
        data.put("exported_by",        claims.get("sub"));
        data.put("exported_at",        Instant.now().toString());
        data.put("raw_signal_count",   raw.size());
        data.put("aggregate_count",    aggs.size());
        data.put("immutable_note",     "Signals are immutable — replay produces identical results to original processing");
        data.put("compliance",         "DPDP Act 2023 / GDPR Article 17 — 7-year retention");
        if (includeRaw) data.put("raw_signals",
                raw.stream().map(SignalModels.BehaviorSignal::toMap).collect(Collectors.toList()));
        if (includeAgg) data.put("aggregated_signals",
                aggs.stream().map(SignalModels.AggregatedSignal::toMap).collect(Collectors.toList()));
        return resp("OK", "Assessment replay exported (" + raw.size() + " raw, " + aggs.size() + " aggregated)", data);
    }

    public String getMetrics(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        SignalSecurityConfig.requireRole(claims, SignalSecurityConfig.ROLE_ADMIN, SignalSecurityConfig.ROLE_SERVICE_ACCOUNT);
        String tenantId = SignalSecurityConfig.effectiveTenantId(claims, args);

        Map<String, Object> metrics = repo.getPrometheusMetrics(tenantId);
        metrics.put("behavior_signal_processing_latency_p95_ms","<500 (target SLA)");
        metrics.put("behavior_active_assessments",      "N/A (production: Redis active_assessments counter)");
        metrics.put("behavior_kafka_consumer_lag",       "N/A (production: KafkaJS consumer group lag)");
        metrics.put("server",                            BehaviorSignalMCPServer.SERVER_NAME);
        metrics.put("version",                           BehaviorSignalMCPServer.SERVER_VERSION);
        metrics.put("namespace",                         "realtime");
        metrics.put("checked_at",                        Instant.now().toString());

        return resp("OK", "Prometheus metrics retrieved", metrics);
    }

    public String getKafkaEventLog(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        SignalSecurityConfig.requireRole(claims, SignalSecurityConfig.ROLE_ADMIN, SignalSecurityConfig.ROLE_SERVICE_ACCOUNT);
        String tenantId     = SignalSecurityConfig.effectiveTenantId(claims, args);
        String assessmentId = SignalSecurityConfig.getString(args, "assessment_id");
        int limit           = Math.min(SignalSecurityConfig.getInt(args,"limit",50), 500);

        List<Map<String, Object>> log = repo.getKafkaLog(tenantId, assessmentId, limit);

        return resp("OK", log.size() + " Kafka events retrieved", Map.of(
                "total",         log.size(),
                "limit",         limit,
                "assessment_id", assessmentId != null ? assessmentId : "ALL",
                "events",        log));
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


/** Tools: analyze_fairness, add_cohort_data, get_audit_log, get_quality_report */
class FairnessAuditTools {

    private final SignalRepository repo = SignalRepository.getInstance();

    public String analyzeFairness(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        SignalSecurityConfig.requireRole(claims, SignalSecurityConfig.ROLE_ADMIN);
        String tenantId    = SignalSecurityConfig.effectiveTenantId(claims, args);
        String signalType  = SignalSecurityConfig.requireString(args, "signal_type").toUpperCase();
        String cohortA     = SignalSecurityConfig.requireString(args, "cohort_a");
        String cohortB     = SignalSecurityConfig.requireString(args, "cohort_b");
        double threshold   = SignalSecurityConfig.getDouble(args, "threshold", 0.7);

        Map<String, List<Double>> cohortData = repo.getCohortData(tenantId, signalType);
        List<Double> dataA = cohortData.getOrDefault(cohortA, List.of());
        List<Double> dataB = cohortData.getOrDefault(cohortB, List.of());

        if (dataA.isEmpty() || dataB.isEmpty())
            return resp("INSUFFICIENT_DATA",
                    "Insufficient cohort data (use add_cohort_data to populate baselines)",
                    Map.of("cohort_a_count",dataA.size(),"cohort_b_count",dataB.size()));

        double meanA = dataA.stream().mapToDouble(d->d).average().orElse(0);
        double meanB = dataB.stream().mapToDouble(d->d).average().orElse(0);
        double stdA  = stdDev(dataA, meanA), stdB = stdDev(dataB, meanB);

        // Distribution parity (normalized difference)
        double distributionParity = meanA > 0 ? Math.abs(meanA - meanB) / meanA : 0;
        boolean biased = distributionParity > 0.15;

        // Differential impact (fraction above threshold)
        double impactA = dataA.stream().filter(v -> v >= threshold).count() / (double) dataA.size();
        double impactB = dataB.stream().filter(v -> v >= threshold).count() / (double) dataB.size();
        double disparateImpactRatio = impactA > 0 ? impactB / impactA : 0;
        boolean disparateImpact = disparateImpactRatio < 0.8; // 80% rule (US EEOC standard)

        // Recommended interventions
        List<String> interventions = new ArrayList<>();
        if (biased)           interventions.add("COHORT_NORMALIZE: Apply cohort-specific normalization");
        if (disparateImpact)  interventions.add("THRESHOLD_TUNE: Adjust scoring threshold for equalized impact");
        if (biased)           interventions.add("CAUSAL_ANALYSIS: Investigate whether difference is causation or correlation");
        if (distributionParity > 0.30) interventions.add("SIGNAL_EXCLUDE: Consider excluding " + signalType + " from scoring model");

        double fairnessScore = 1.0 - (distributionParity * 0.5 + (disparateImpact ? 0.5 : 0));

        repo.audit("FAIRNESS_ANALYSIS", "CROSS_ASSESSMENT", tenantId,
                "signal="+signalType+" cohorts="+cohortA+"/"+cohortB+" score="+Math.round(fairnessScore*100.0)/100.0, true);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("signal_type",          signalType);
        data.put("cohort_a",             cohortA);
        data.put("cohort_b",             cohortB);
        data.put("cohort_a_mean",        Math.round(meanA*1000.0)/1000.0);
        data.put("cohort_b_mean",        Math.round(meanB*1000.0)/1000.0);
        data.put("cohort_a_std_dev",     Math.round(stdA*1000.0)/1000.0);
        data.put("cohort_b_std_dev",     Math.round(stdB*1000.0)/1000.0);
        data.put("distribution_parity_diff", Math.round(distributionParity*1000.0)/1000.0);
        data.put("bias_detected",        biased);
        data.put("hiring_threshold",     threshold);
        data.put("impact_rate_a",        Math.round(impactA*1000.0)/1000.0);
        data.put("impact_rate_b",        Math.round(impactB*1000.0)/1000.0);
        data.put("disparate_impact_ratio",Math.round(disparateImpactRatio*1000.0)/1000.0);
        data.put("disparate_impact_detected", disparateImpact);
        data.put("fairness_score",       Math.round(fairnessScore*1000.0)/1000.0);
        data.put("recommended_interventions", interventions);
        data.put("compliance_note",      "DPDP Act 2023 / Equal Employment Opportunity compliance analysis");
        return resp(biased || disparateImpact ? "BIAS_DETECTED" : "FAIR",
                biased || disparateImpact ? "Potential bias detected in signal: " + signalType
                        : "Signal appears fair across cohorts", data);
    }

    public String addCohortData(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        SignalSecurityConfig.requireRole(claims, SignalSecurityConfig.ROLE_ADMIN, SignalSecurityConfig.ROLE_SERVICE_ACCOUNT);
        String tenantId   = SignalSecurityConfig.effectiveTenantId(claims, args);
        String signalType = SignalSecurityConfig.requireString(args, "signal_type");
        String cohortName = SignalSecurityConfig.requireString(args, "cohort_name");
        String valueStr   = SignalSecurityConfig.requireString(args, "value");

        double value = Double.parseDouble(valueStr);
        repo.addCohortDataPoint(tenantId, signalType.toUpperCase(), cohortName.toLowerCase(), value);

        Map<String, List<Double>> cohortData = repo.getCohortData(tenantId, signalType.toUpperCase());
        int cohortSize = cohortData.getOrDefault(cohortName.toLowerCase(), List.of()).size();

        return resp("OK", "Cohort data point added", Map.of(
                "signal_type",  signalType,
                "cohort_name",  cohortName,
                "value",        value,
                "cohort_size",  cohortSize));
    }

    public String getAuditLog(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        SignalSecurityConfig.requireRole(claims, SignalSecurityConfig.ROLE_ADMIN);
        String tenantId     = SignalSecurityConfig.effectiveTenantId(claims, args);
        String assessmentId = SignalSecurityConfig.getString(args, "assessment_id");
        int limit           = Math.min(SignalSecurityConfig.getInt(args,"limit",100), 1000);

        List<Map<String, Object>> log = repo.getAuditLog(tenantId, assessmentId, limit);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("total",          log.size());
        data.put("limit",          limit);
        data.put("assessment_id",  assessmentId != null ? assessmentId : "ALL");
        data.put("entries",        log);
        data.put("compliance_note","Immutable audit trail — DPDP Act 2023 (7-year retention)");
        return resp("OK", log.size() + " audit entries retrieved", data);
    }

    public String getQualityReport(Map<String, Object> args) {
        Map<String, Object> claims = auth(args);
        SignalSecurityConfig.requireRole(claims, SignalSecurityConfig.ROLE_RECRUITER, SignalSecurityConfig.ROLE_ADMIN);
        String tenantId     = SignalSecurityConfig.effectiveTenantId(claims, args);
        String assessmentId = SignalSecurityConfig.requireString(args, "assessment_id");

        List<SignalModels.BehaviorSignal>   signals    = repo.getSignals(tenantId, assessmentId);
        List<SignalModels.AggregatedSignal> aggregates = repo.getAggregates(tenantId, assessmentId);
        Optional<Map<String, Object>>       config     = repo.getConfig(assessmentId);

        long total   = signals.size();
        long valid   = signals.stream().filter(s -> s.valid).count();
        long anomalies = signals.stream().filter(s -> !s.valid && s.validationReason.contains("ANOMALY")).count();
        double validRate  = total > 0 ? (double) valid / total : 0;

        Map<String, Object> byType = new LinkedHashMap<>();
        Map<String, List<SignalModels.BehaviorSignal>> grouped = new LinkedHashMap<>();
        for (SignalModels.BehaviorSignal s : signals)
            grouped.computeIfAbsent(s.signalType.name(), k -> new ArrayList<>()).add(s);
        for (Map.Entry<String, List<SignalModels.BehaviorSignal>> e : grouped.entrySet()) {
            long tv = e.getValue().stream().filter(s -> s.valid).count();
            byType.put(e.getKey(), Map.of("count", e.getValue().size(), "valid", tv,
                    "rate", e.getValue().size()>0 ? Math.round((double)tv/e.getValue().size()*100.0)/100.0 : 0));
        }

        // Drift alerts
        List<String> alerts = new ArrayList<>();
        if (validRate < SignalSecurityConfig.getQualityThreshold())
            alerts.add("QUALITY_BELOW_THRESHOLD: " + Math.round(validRate*100.0) + "% valid");
        if (anomalies > total * 0.1) alerts.add("HIGH_ANOMALY_RATE: " + anomalies + " anomalies (" + Math.round((double)anomalies/total*100.0) + "%)");
        if (grouped.size() < 3) alerts.add("LOW_SIGNAL_DIVERSITY: only " + grouped.size() + " signal types present");

        double qualityScore = validRate * 0.6 + Math.min(1.0, grouped.size() / 8.0) * 0.4;

        Map<String, Object> report = new LinkedHashMap<>();
        report.put("assessment_id",        assessmentId);
        report.put("report_generated_at",  Instant.now().toString());
        report.put("total_signals",        total);
        report.put("valid_signals",        valid);
        report.put("validity_rate",        Math.round(validRate*1000.0)/1000.0);
        report.put("anomalies_detected",   anomalies);
        report.put("signal_types_present", grouped.size());
        report.put("aggregates_computed",  aggregates.size());
        report.put("data_quality_score",   Math.round(qualityScore*1000.0)/1000.0);
        report.put("quality_threshold",    SignalSecurityConfig.getQualityThreshold());
        report.put("signal_type_breakdown",byType);
        report.put("alerts",               alerts);
        report.put("assessment_config",    config.orElse(Map.of("note","No config registered")));
        report.put("scoring_recommendation",
                alerts.isEmpty() ? "PROCEED: Signal quality sufficient for automated scoring"
                        : "REVIEW: " + alerts.size() + " alert(s) require attention before finalizing scores");
        return resp(alerts.isEmpty() ? "HEALTHY" : "NEEDS_REVIEW",
                "Quality report generated for " + assessmentId, report);
    }

    private double stdDev(List<Double> values, double mean) {
        if (values.isEmpty()) return 0;
        double sumSq = values.stream().mapToDouble(v -> (v-mean)*(v-mean)).sum();
        return Math.sqrt(sumSq / values.size());
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
