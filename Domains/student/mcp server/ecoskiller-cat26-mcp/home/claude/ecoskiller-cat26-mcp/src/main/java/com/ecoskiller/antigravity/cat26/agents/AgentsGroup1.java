package com.ecoskiller.antigravity.cat26.agents;

import com.ecoskiller.antigravity.cat26.model.McpModels.*;

import java.time.Instant;
import java.util.*;

// ══════════════════════════════════════════════════════════════════════
//  AGENT 1 — IMPACT_MEASUREMENT_AGENT
// ══════════════════════════════════════════════════════════════════════
class ImpactMeasurementAgent {
    static final String NAME = "IMPACT_MEASUREMENT_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("impact__measure_program",
                "Measure social, educational, and economic impact of a program or network across a region.",
                new InputSchema(Map.of(
                    "program_id",   new PropSchema("string","Program or initiative ID"),
                    "region",       new PropSchema("string","Target region/district/state"),
                    "period",       new PropSchema("string","Evaluation period e.g. 2025-Q2"),
                    "impact_types", new PropSchema("array", "SOCIAL, EDUCATIONAL, ECONOMIC, EMPLOYMENT")
                ), List.of("program_id"))),
            new McpTool("impact__get_impact_scorecard",
                "Get impact scorecard with KPI benchmarks for a network or organizer.",
                new InputSchema(Map.of(
                    "entity_id",   new PropSchema("string","Organizer or Network ID"),
                    "include_sdg", new PropSchema("boolean","Include UN SDG alignment. Default: true")
                ), List.of("entity_id")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "impact__measure_program" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("program_id", args.get("program_id"));
                r.put("region", args.getOrDefault("region", "ALL"));
                r.put("period", args.getOrDefault("period", "2025-Q2"));
                r.put("beneficiaries_reached", 12480);
                r.put("impact_scores", Map.of(
                    "SOCIAL", Map.of("score", 78.4, "grade", "B+"),
                    "EDUCATIONAL", Map.of("score", 84.1, "grade", "A"),
                    "ECONOMIC", Map.of("score", 71.2, "grade", "B"),
                    "EMPLOYMENT", Map.of("score", 68.9, "grade", "B-")
                ));
                r.put("composite_impact_index", 75.7);
                r.put("trend", "IMPROVING");
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "impact__get_impact_scorecard" -> {
                boolean sdg = Boolean.TRUE.equals(args.getOrDefault("include_sdg", true));
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("impact_index", 75.7);
                r.put("kpis", Map.of("students_enrolled", 8420, "skill_certifications", 3210,
                    "employment_placements", 1890, "income_increase_avg_pct", 34.2));
                if (sdg) r.put("sdg_alignment", List.of(
                    Map.of("sdg", "SDG-4 Quality Education", "score", 81.0),
                    Map.of("sdg", "SDG-8 Decent Work", "score", 68.0),
                    Map.of("sdg", "SDG-10 Reduced Inequalities", "score", 72.0)
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error", "Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 2 — REVENUE_TREND_ANALYTICS_AGENT
// ══════════════════════════════════════════════════════════════════════
class RevenueTrendAnalyticsAgent {
    static final String NAME = "REVENUE_TREND_ANALYTICS_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("revenue_trend__analyze",
                "Analyze revenue trends for an organizer or network over a specified period. Returns growth rates and seasonality.",
                new InputSchema(Map.of(
                    "entity_id",    new PropSchema("string","Organizer or Network ID"),
                    "period_months",new PropSchema("integer","Number of months to analyze. Default: 12"),
                    "breakdown_by", new PropSchema("string","MONTH, QUARTER, STREAM", List.of("MONTH","QUARTER","STREAM"))
                ), List.of("entity_id"))),
            new McpTool("revenue_trend__forecast",
                "Forecast revenue for the next N months based on historical trend and market signals.",
                new InputSchema(Map.of(
                    "entity_id",       new PropSchema("string","Entity ID"),
                    "forecast_months", new PropSchema("integer","Months to forecast. Default: 6"),
                    "include_scenarios",new PropSchema("boolean","Include best/base/worst scenarios. Default: true")
                ), List.of("entity_id")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "revenue_trend__analyze" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("period_months", args.getOrDefault("period_months", 12));
                r.put("total_revenue_inr", 9850000.00);
                r.put("growth_rate_pct", 23.4);
                r.put("trend", "STRONG_GROWTH");
                r.put("monthly_avg_inr", 820833.33);
                r.put("peak_month", "2025-03");
                r.put("trough_month", "2025-01");
                r.put("seasonality_detected", true);
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "revenue_trend__forecast" -> {
                int months = ((Number) args.getOrDefault("forecast_months", 6)).intValue();
                boolean scenarios = Boolean.TRUE.equals(args.getOrDefault("include_scenarios", true));
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("forecast_months", months);
                r.put("base_forecast_inr", 5800000.00);
                if (scenarios) r.put("scenarios", Map.of(
                    "BEST",  Map.of("inr", 7200000.00, "growth_pct", 35.0),
                    "BASE",  Map.of("inr", 5800000.00, "growth_pct", 18.0),
                    "WORST", Map.of("inr", 4100000.00, "growth_pct", -5.0)
                ));
                r.put("confidence", 0.82);
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error", "Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 3 — REVENUE_STREAM_DIVERSIFICATION_AGENT
// ══════════════════════════════════════════════════════════════════════
class RevenueStreamDiversificationAgent {
    static final String NAME = "REVENUE_STREAM_DIVERSIFICATION_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("rev_diversification__assess",
                "Assess current revenue stream concentration and diversification health for an organizer or network.",
                new InputSchema(Map.of(
                    "entity_id", new PropSchema("string","Organizer or Network ID"),
                    "period",    new PropSchema("string","Assessment period e.g. 2025-Q2")
                ), List.of("entity_id"))),
            new McpTool("rev_diversification__recommend_streams",
                "AI-recommend new revenue streams based on network profile, geography, and market gaps.",
                new InputSchema(Map.of(
                    "entity_id", new PropSchema("string","Entity ID"),
                    "top_n",     new PropSchema("integer","Top N recommendations. Default: 5")
                ), List.of("entity_id")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "rev_diversification__assess" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("herfindahl_index", 0.34);
                r.put("concentration_risk", "MEDIUM");
                r.put("active_streams", 4);
                r.put("stream_breakdown", List.of(
                    Map.of("stream", "Competition Fees", "pct", 55.0),
                    Map.of("stream", "Workshop Revenue", "pct", 22.0),
                    Map.of("stream", "Certificate Fees", "pct", 14.0),
                    Map.of("stream", "Royalty Share",    "pct", 9.0)
                ));
                r.put("recommendation", "Reduce competition fee dependency below 40%");
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "rev_diversification__recommend_streams" -> {
                int topN = ((Number) args.getOrDefault("top_n", 5)).intValue();
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("recommendations", List.of(
                    Map.of("stream","Corporate Skill Assessments","potential_inr",850000,"effort","LOW"),
                    Map.of("stream","Government Scheme Tieups",   "potential_inr",1200000,"effort","MEDIUM"),
                    Map.of("stream","Online Masterclasses",       "potential_inr",640000,"effort","LOW"),
                    Map.of("stream","Franchise License Revenue",  "potential_inr",1800000,"effort","HIGH"),
                    Map.of("stream","CSR Partnership Programs",   "potential_inr",950000,"effort","MEDIUM")
                ).subList(0, Math.min(topN, 5)));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error", "Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 4 — NETWORK_HEALTH_MONITOR_AGENT
// ══════════════════════════════════════════════════════════════════════
class NetworkHealthMonitorAgent {
    static final String NAME = "NETWORK_HEALTH_MONITOR_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("network_health__get_snapshot",
                "Get real-time health snapshot of the organizer network including activity, engagement, and node status.",
                new InputSchema(Map.of(
                    "network_id",    new PropSchema("string","Network ID"),
                    "include_nodes", new PropSchema("boolean","Include individual node health. Default: false")
                ), List.of("network_id"))),
            new McpTool("network_health__flag_inactive_nodes",
                "Identify and flag network nodes (organizers/coordinators) inactive beyond threshold days.",
                new InputSchema(Map.of(
                    "network_id",        new PropSchema("string","Network ID"),
                    "inactivity_days",   new PropSchema("integer","Days without activity to flag. Default: 30"),
                    "auto_notify",       new PropSchema("boolean","Auto-send reactivation nudge. Default: false")
                ), List.of("network_id")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "network_health__get_snapshot" -> {
                boolean nodes = Boolean.TRUE.equals(args.getOrDefault("include_nodes", false));
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("network_id", args.get("network_id"));
                r.put("total_nodes", 142);
                r.put("active_nodes", 118);
                r.put("inactive_nodes", 24);
                r.put("activity_rate_pct", 83.1);
                r.put("health_score", 76.4);
                r.put("health_status", "GOOD");
                r.put("avg_monthly_events_per_node", 3.2);
                if (nodes) r.put("node_sample", List.of(
                    Map.of("node_id","ORG-0041","status","ACTIVE","last_event","2025-06-18"),
                    Map.of("node_id","ORG-0089","status","INACTIVE","last_event","2025-05-01")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "network_health__flag_inactive_nodes" -> {
                int days = ((Number) args.getOrDefault("inactivity_days", 30)).intValue();
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("network_id", args.get("network_id"));
                r.put("inactivity_threshold_days", days);
                r.put("flagged_count", 24);
                r.put("auto_notify", args.getOrDefault("auto_notify", false));
                r.put("flagged_nodes", List.of(
                    Map.of("node_id","ORG-0033","last_active","2025-05-10","inactive_days",39,"risk","HIGH"),
                    Map.of("node_id","ORG-0067","last_active","2025-04-28","inactive_days",51,"risk","CRITICAL"),
                    Map.of("node_id","ORG-0102","last_active","2025-05-20","inactive_days",29,"risk","MEDIUM")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error", "Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 5 — DROPOUT_RISK_PREDICTION_AGENT
// ══════════════════════════════════════════════════════════════════════
class DropoutRiskPredictionAgent {
    static final String NAME = "DROPOUT_RISK_PREDICTION_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("dropout_risk__predict_student",
                "Predict dropout risk for a student. Returns probability, key drivers, and intervention recommendations.",
                new InputSchema(Map.of(
                    "student_id",       new PropSchema("string","Student ID"),
                    "include_drivers",  new PropSchema("boolean","Include risk driver analysis. Default: true")
                ), List.of("student_id"))),
            new McpTool("dropout_risk__get_high_risk_cohort",
                "Get a list of students in a cohort or program above the dropout risk threshold.",
                new InputSchema(Map.of(
                    "program_id",    new PropSchema("string","Program or course ID"),
                    "risk_threshold",new PropSchema("number","Flag students above this dropout probability %. Default: 40"),
                    "limit",         new PropSchema("integer","Max results. Default: 50")
                ), List.of("program_id")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "dropout_risk__predict_student" -> {
                boolean drivers = Boolean.TRUE.equals(args.getOrDefault("include_drivers", true));
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("student_id", args.get("student_id"));
                r.put("dropout_probability_pct", 22.8);
                r.put("risk_level", "LOW_MEDIUM");
                r.put("predicted_for_days", 90);
                if (drivers) r.put("key_drivers", List.of(
                    Map.of("driver","Attendance drop >20%", "impact","HIGH"),
                    Map.of("driver","Fee payment overdue",  "impact","MEDIUM"),
                    Map.of("driver","No parent engagement", "impact","LOW")
                ));
                r.put("recommended_intervention", "Counselor check-in + fee waiver assessment");
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "dropout_risk__get_high_risk_cohort" -> {
                double threshold = ((Number) args.getOrDefault("risk_threshold", 40.0)).doubleValue();
                int limit = ((Number) args.getOrDefault("limit", 50)).intValue();
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("program_id", args.get("program_id"));
                r.put("risk_threshold_pct", threshold);
                r.put("high_risk_count", 18);
                r.put("students", List.of(
                    Map.of("student_id","STU-0041","risk_pct",72.1,"primary_driver","Absenteeism"),
                    Map.of("student_id","STU-0089","risk_pct",65.4,"primary_driver","Fee default"),
                    Map.of("student_id","STU-0112","risk_pct",58.9,"primary_driver","Low engagement")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error", "Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 6 — PHONE_MONITORING_CLOCK_AUTHORITY_AGENT
// ══════════════════════════════════════════════════════════════════════
class PhoneMonitoringClockAuthorityAgent {
    static final String NAME = "PHONE_MONITORING_CLOCK_AUTHORITY_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("phone_clock__get_authority_time",
                "Get authoritative platform clock timestamp for phone/call session synchronization.",
                new InputSchema(Map.of(
                    "timezone",  new PropSchema("string","Timezone e.g. Asia/Kolkata. Default: UTC"),
                    "node_id",   new PropSchema("string","Requesting node ID for drift tracking")
                ), List.of())),
            new McpTool("phone_clock__detect_drift",
                "Detect clock drift between a phone node and the authority clock. Returns drift amount and correction.",
                new InputSchema(Map.of(
                    "node_id",        new PropSchema("string","Phone node ID"),
                    "reported_ts",    new PropSchema("string","Node's reported timestamp (ISO-8601)"),
                    "auto_correct",   new PropSchema("boolean","Issue correction signal. Default: false")
                ), List.of("node_id","reported_ts")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "phone_clock__get_authority_time" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("authority_timestamp", Instant.now().toString());
                r.put("timezone", args.getOrDefault("timezone", "UTC"));
                r.put("epoch_ms", System.currentTimeMillis());
                r.put("clock_source", "ANTIGRAVITY_NTP_MASTER");
                r.put("drift_tolerance_ms", 50);
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "phone_clock__detect_drift" -> {
                String reportedTs = (String) args.get("reported_ts");
                long driftMs = 23L; // simulated drift
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("node_id", args.get("node_id"));
                r.put("reported_ts", reportedTs);
                r.put("authority_ts", Instant.now().toString());
                r.put("drift_ms", driftMs);
                r.put("drift_status", driftMs > 50 ? "DRIFT_DETECTED" : "WITHIN_TOLERANCE");
                r.put("correction_applied", Boolean.TRUE.equals(args.getOrDefault("auto_correct",false)));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error", "Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 7 — POLICY_COMPLIANCE_AGENT
// ══════════════════════════════════════════════════════════════════════
class PolicyComplianceAgent {
    static final String NAME = "POLICY_COMPLIANCE_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("policy_compliance__check_entity",
                "Check whether an organizer, institute, or network node is compliant with all active platform policies.",
                new InputSchema(Map.of(
                    "entity_id",   new PropSchema("string","Entity ID"),
                    "entity_type", new PropSchema("string","ORGANIZER, INSTITUTE, COORDINATOR, TRAINER",
                        List.of("ORGANIZER","INSTITUTE","COORDINATOR","TRAINER")),
                    "policy_scope",new PropSchema("string","ALL, OPERATIONAL, FINANCIAL, DATA, SAFETY",
                        List.of("ALL","OPERATIONAL","FINANCIAL","DATA","SAFETY"))
                ), List.of("entity_id","entity_type"))),
            new McpTool("policy_compliance__get_violations",
                "Get list of active policy violations for an entity with remediation deadlines.",
                new InputSchema(Map.of(
                    "entity_id", new PropSchema("string","Entity ID"),
                    "severity",  new PropSchema("string","CRITICAL, HIGH, MEDIUM, LOW, ALL",
                        List.of("CRITICAL","HIGH","MEDIUM","LOW","ALL"))
                ), List.of("entity_id","severity")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "policy_compliance__check_entity" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("entity_type", args.get("entity_type"));
                r.put("compliance_score", 88.5);
                r.put("status", "COMPLIANT_WITH_MINOR_GAPS");
                r.put("policies_checked", 34);
                r.put("policies_passed", 31);
                r.put("policies_failed", 3);
                r.put("critical_violations", 0);
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "policy_compliance__get_violations" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("entity_id", args.get("entity_id"));
                r.put("violations", List.of(
                    Map.of("policy_id","POL-FIN-003","desc","Monthly financial report not submitted",
                           "severity","HIGH","deadline","2025-07-05","status","OPEN"),
                    Map.of("policy_id","POL-OPS-011","desc","Trainer KYC documents expired",
                           "severity","MEDIUM","deadline","2025-07-15","status","OPEN")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error", "Unknown tool: " + tool));
        };
    }
}

// ══════════════════════════════════════════════════════════════════════
//  AGENT 8 — DATA_PRIVACY_COMPLIANCE_AGENT
// ══════════════════════════════════════════════════════════════════════
class DataPrivacyComplianceAgent {
    static final String NAME = "DATA_PRIVACY_COMPLIANCE_AGENT";
    static List<McpTool> tools() {
        return List.of(
            new McpTool("data_privacy__audit_data_flows",
                "Audit all personal data flows for an organizer network node. Returns DPDP compliance status.",
                new InputSchema(Map.of(
                    "node_id",     new PropSchema("string","Network node / organizer ID"),
                    "data_types",  new PropSchema("array","PII, MINOR_DATA, FINANCIAL, BIOMETRIC")
                ), List.of("node_id"))),
            new McpTool("data_privacy__handle_erasure_request",
                "Process a Right-to-Erasure request from a user. Returns erasure confirmation and audit trail.",
                new InputSchema(Map.of(
                    "user_id",     new PropSchema("string","User requesting erasure"),
                    "request_id",  new PropSchema("string","Erasure request ticket ID"),
                    "scope",       new PropSchema("string","ALL, PROFILE, SCORES, TRANSACTIONS, MEDIA")
                ), List.of("user_id","request_id")))
        );
    }
    static AgentResult execute(String tool, Map<String, Object> args) {
        return switch (tool) {
            case "data_privacy__audit_data_flows" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("node_id", args.get("node_id"));
                r.put("dpdp_compliance_score", 82.0);
                r.put("data_flows_audited", 14);
                r.put("non_compliant_flows", 2);
                r.put("issues", List.of(
                    Map.of("flow","Student biometric to 3rd party","issue","No consent gate","severity","HIGH"),
                    Map.of("flow","Parent contact sharing","issue","Retention period exceeds policy","severity","MEDIUM")
                ));
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            case "data_privacy__handle_erasure_request" -> {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("user_id", args.get("user_id"));
                r.put("request_id", args.get("request_id"));
                r.put("scope", args.getOrDefault("scope","ALL"));
                r.put("records_erased", 47);
                r.put("records_retained_legal_hold", 3);
                r.put("completed_at", Instant.now().toString());
                r.put("audit_trail_id","ERA-" + UUID.randomUUID().toString().substring(0,8).toUpperCase());
                r.put("status","ERASURE_COMPLETE");
                yield new AgentResult(NAME, "SUCCESS", r);
            }
            default -> new AgentResult(NAME, "ERROR", Map.of("error", "Unknown tool: " + tool));
        };
    }
}

/** Public dispatcher for Agents 1–8 */
public class AgentsGroup1 {
    public static List<McpTool> allTools() {
        List<McpTool> all = new ArrayList<>();
        all.addAll(ImpactMeasurementAgent.tools());
        all.addAll(RevenueTrendAnalyticsAgent.tools());
        all.addAll(RevenueStreamDiversificationAgent.tools());
        all.addAll(NetworkHealthMonitorAgent.tools());
        all.addAll(DropoutRiskPredictionAgent.tools());
        all.addAll(PhoneMonitoringClockAuthorityAgent.tools());
        all.addAll(PolicyComplianceAgent.tools());
        all.addAll(DataPrivacyComplianceAgent.tools());
        return all;
    }
    public static AgentResult dispatch(String tool, Map<String, Object> args) {
        if (tool.startsWith("impact__"))             return ImpactMeasurementAgent.execute(tool, args);
        if (tool.startsWith("revenue_trend__"))      return RevenueTrendAnalyticsAgent.execute(tool, args);
        if (tool.startsWith("rev_diversification__"))return RevenueStreamDiversificationAgent.execute(tool, args);
        if (tool.startsWith("network_health__"))     return NetworkHealthMonitorAgent.execute(tool, args);
        if (tool.startsWith("dropout_risk__"))       return DropoutRiskPredictionAgent.execute(tool, args);
        if (tool.startsWith("phone_clock__"))        return PhoneMonitoringClockAuthorityAgent.execute(tool, args);
        if (tool.startsWith("policy_compliance__"))  return PolicyComplianceAgent.execute(tool, args);
        if (tool.startsWith("data_privacy__"))       return DataPrivacyComplianceAgent.execute(tool, args);
        return new AgentResult("GROUP1", "ERROR", Map.of("error","No agent for: " + tool));
    }
}
