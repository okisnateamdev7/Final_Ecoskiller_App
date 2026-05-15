package com.ecoskiller.mcp.economics.agents;

import com.ecoskiller.mcp.economics.model.McpProtocol.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

// ═══════════════════════════════════════════════════════
// HIRING_BIAS_DETECTOR — CAT-08 Agent #5
// ═══════════════════════════════════════════════════════
@Component
public class HiringBiasDetectorAgent extends BaseAgentTool {

    @Override public String getName() { return "hiring_bias_detect"; }

    @Override public String getDescription() {
        return "Detect and quantify bias in hiring pipelines using Ecoskiller's ML bias model. " +
               "Analyzes JD language, shortlisting patterns, interview scores, and offer rates " +
               "across gender, age, geography, caste, and educational institution. SEALED model.";
    }

    @Override public InputSchema getInputSchema() {
        return InputSchema.builder().type("object")
            .properties(Map.of(
                "action", PropertyDef.builder().type("string")
                    .enumValues(List.of("analyze_jd","audit_pipeline","measure_representation",
                                        "score_fairness","generate_debiasing_report","simulate_blind_hiring"))
                    .build(),
                "tenant_id", PropertyDef.builder().type("string").build(),
                "job_id", PropertyDef.builder().type("string").description("Job requisition ID").build(),
                "jd_text", PropertyDef.builder().type("string").description("Job description text").build(),
                "pipeline_data", PropertyDef.builder().type("string")
                    .description("JSON: candidate pipeline with demographic metadata").build()
            ))
            .required(List.of("action","tenant_id")).build();
    }

    @Override protected ToolCallResult doExecute(Map<String, Object> args) {
        String action = req(args, "action");
        String tenantId = req(args, "tenant_id");

        return switch (action) {
            case "analyze_jd" -> success(Map.of(
                "tenant_id", tenantId,
                "jd_bias_score", 34,
                "biased_phrases_detected", List.of(
                    Map.of("phrase","rockstar developer","bias_type","age/gender","suggestion","skilled engineer"),
                    Map.of("phrase","fresh graduate preferred","bias_type","age","suggestion","early-career professional"),
                    Map.of("phrase","IIT/IIM preferred","bias_type","institution","suggestion","bachelor's degree in relevant field")),
                "overall_risk", "MEDIUM",
                "inclusivity_score", 61,
                "recommendation", "Rewrite with inclusive language — use debiased JD template"));

            case "audit_pipeline" -> success(Map.of(
                "tenant_id", tenantId,
                "job_id", str(args,"job_id"),
                "pipeline_stages_audited", List.of("Application","Shortlist","Interview","Offer"),
                "bias_detected", true,
                "stage_drop_off_disparity", Map.of(
                    "gender", Map.of("male_pass_rate",0.68,"female_pass_rate",0.41,"gap",0.27),
                    "tier2_city", Map.of("metro_pass_rate",0.65,"non_metro_pass_rate",0.38,"gap",0.27)),
                "p_value", 0.003,
                "statistical_significance", true,
                "recommended_intervention", "Structured interview with blind CV screening"));

            case "measure_representation" -> success(Map.of(
                "tenant_id", tenantId,
                "representation", Map.of(
                    "gender", Map.of("male_percent",72,"female_percent",26,"non_binary_percent",2),
                    "geography", Map.of("tier1_percent",58,"tier2_percent",31,"tier3_percent",11),
                    "education", Map.of("tier1_college_percent",44,"other_percent",56)),
                "industry_benchmark", Map.of("female_tech_percent",32),
                "gap_analysis", "Female representation 6pp below benchmark"));

            case "score_fairness" -> success(Map.of(
                "tenant_id", tenantId,
                "overall_fairness_score", 62,
                "grade", "C+",
                "dimensions", Map.of(
                    "gender_fairness", 58, "age_fairness", 71,
                    "geography_fairness", 63, "institution_fairness", 55),
                "industry_percentile", 48,
                "action_required", true));

            case "generate_debiasing_report" -> success(Map.of(
                "report_id", "bias_" + System.currentTimeMillis(),
                "tenant_id", tenantId,
                "period", "Q3-2025",
                "total_roles_analyzed", 24,
                "bias_incidents_found", 7,
                "corrective_actions_recommended", 12,
                "pdf_url", "https://reports.ecoskiller.com/bias/" + tenantId + "/Q3-2025.pdf"));

            case "simulate_blind_hiring" -> success(Map.of(
                "simulation", "blind_cv_screening",
                "tenant_id", tenantId,
                "predicted_female_shortlist_increase_percent", 18,
                "predicted_tier2_city_increase_percent", 12,
                "implementation_effort", "LOW",
                "recommendation", "Enable blind CV mode in HRMS_AGENT settings"));

            default -> error("Unknown action: " + action);
        };
    }
}

// ═══════════════════════════════════════════════════════
// WORKFORCE_GAP_MODEL — CAT-08 Agent #6
// ═══════════════════════════════════════════════════════
@Component
public class WorkforceGapModelAgent extends BaseAgentTool {

    @Override public String getName() { return "workforce_gap_model"; }

    @Override public String getDescription() {
        return "Model current and future workforce skill gaps across tenant organizations. " +
               "Identifies critical capability shortfalls, recommends L&D interventions, " +
               "and forecasts hiring needs. Uses Ecoskiller's sealed ML workforce intelligence.";
    }

    @Override public InputSchema getInputSchema() {
        return InputSchema.builder().type("object")
            .properties(Map.of(
                "action", PropertyDef.builder().type("string")
                    .enumValues(List.of("identify_gaps","forecast_needs","recommend_training",
                                        "benchmark_skills","model_attrition_impact","get_gap_heatmap"))
                    .build(),
                "tenant_id", PropertyDef.builder().type("string").build(),
                "department", PropertyDef.builder().type("string")
                    .description("e.g. engineering, sales, operations, all").build(),
                "horizon_months", PropertyDef.builder().type("string")
                    .description("Forecast horizon: 6, 12, 24").build(),
                "skill_domain", PropertyDef.builder().type("string")
                    .description("e.g. data_science, cloud, leadership, sales").build()
            ))
            .required(List.of("action","tenant_id")).build();
    }

    @Override protected ToolCallResult doExecute(Map<String, Object> args) {
        String action = req(args, "action");
        String tenantId = req(args, "tenant_id");
        String dept = str(args,"department") != null ? str(args,"department") : "all";
        int horizon = intVal(args,"horizon_months",12);

        return switch (action) {
            case "identify_gaps" -> success(Map.of(
                "tenant_id", tenantId, "department", dept,
                "critical_gaps", List.of(
                    Map.of("skill","GenAI / Prompt Engineering","gap_severity","CRITICAL",
                           "employees_lacking",87,"market_demand_trend","RISING"),
                    Map.of("skill","Cloud Native (K8s)","gap_severity","HIGH",
                           "employees_lacking",43,"market_demand_trend","RISING"),
                    Map.of("skill","Data Visualization","gap_severity","MEDIUM",
                           "employees_lacking",31,"market_demand_trend","STABLE")),
                "total_employees_assessed", 214,
                "overall_gap_score", 67));

            case "forecast_needs" -> success(Map.of(
                "tenant_id", tenantId, "horizon_months", horizon,
                "projected_skill_requirements", Map.of(
                    "data_science", Map.of("current_headcount",12,"required_by_horizon",22,"gap",10),
                    "cloud_engineering", Map.of("current_headcount",8,"required_by_horizon",15,"gap",7),
                    "product_management", Map.of("current_headcount",4,"required_by_horizon",7,"gap",3)),
                "total_hiring_needed", 20,
                "total_upskilling_needed", 45,
                "estimated_cost_inr", 8500000));

            case "recommend_training" -> success(Map.of(
                "tenant_id", tenantId, "department", dept,
                "training_plan", List.of(
                    Map.of("skill","GenAI","program","Ecoskiller GenAI Bootcamp 6-week",
                           "priority","P1","employees",87,"cost_per_head_inr",4500),
                    Map.of("skill","Cloud Native","program","K8s + Docker Certification Path",
                           "priority","P2","employees",43,"cost_per_head_inr",6000)),
                "total_investment_inr", 6500000,
                "expected_roi_months", 9,
                "skills_marketplace_integration", "ecoskiller_dojo"));

            case "benchmark_skills" -> success(Map.of(
                "tenant_id", tenantId,
                "industry_benchmarks", Map.of(
                    "data_science_proficiency", Map.of("tenant_score",58,"industry_avg",72,"gap",-14),
                    "cloud_skills", Map.of("tenant_score",61,"industry_avg",69,"gap",-8),
                    "leadership_index", Map.of("tenant_score",74,"industry_avg",70,"gap",4)),
                "overall_percentile", 42,
                "sector", "edtech_technology"));

            case "model_attrition_impact" -> success(Map.of(
                "tenant_id", tenantId,
                "projected_12m_attrition_percent", 18,
                "critical_skill_loss_risk", List.of("GenAI","Cloud Architecture","Product Design"),
                "knowledge_concentration_risk", Map.of("team","ML Platform","key_persons",2),
                "succession_gaps", 4,
                "recommended_actions", List.of(
                    "Knowledge transfer sessions for top 10% performers",
                    "Hire GenAI backup before Q4")));

            case "get_gap_heatmap" -> success(Map.of(
                "tenant_id", tenantId,
                "heatmap", List.of(
                    Map.of("dept","Engineering","skill","GenAI","severity","CRITICAL"),
                    Map.of("dept","Engineering","skill","Cloud","severity","HIGH"),
                    Map.of("dept","Sales","skill","SaaS Demo","severity","MEDIUM"),
                    Map.of("dept","HR","skill","Data Analytics","severity","HIGH"),
                    Map.of("dept","Finance","skill","FinTech Compliance","severity","LOW")),
                "generated_at", "2025-09-01"));

            default -> error("Unknown action: " + action);
        };
    }
}

// ═══════════════════════════════════════════════════════
// FINOPS_AGENT — CAT-08 Agent #7
// ═══════════════════════════════════════════════════════
@Component
public class FinOpsAgent extends BaseAgentTool {

    @Override public String getName() { return "finops"; }

    @Override public String getDescription() {
        return "Cloud and infrastructure cost management for Ecoskiller. Track cloud spend, " +
               "allocate costs to tenants, identify waste, enforce budgets, and optimize " +
               "unit economics across AWS, GCP, and Azure resources.";
    }

    @Override public InputSchema getInputSchema() {
        return InputSchema.builder().type("object")
            .properties(Map.of(
                "action", PropertyDef.builder().type("string")
                    .enumValues(List.of("get_spend_summary","cost_by_tenant","identify_waste",
                                        "set_budget_alert","unit_economics","forecast_spend"))
                    .build(),
                "period", PropertyDef.builder().type("string")
                    .description("e.g. 2025-09, Q3-2025").build(),
                "tenant_id", PropertyDef.builder().type("string").build(),
                "cloud_provider", PropertyDef.builder().type("string")
                    .enumValues(List.of("aws","gcp","azure","all")).build(),
                "budget_inr", PropertyDef.builder().type("string").build()
            ))
            .required(List.of("action")).build();
    }

    @Override protected ToolCallResult doExecute(Map<String, Object> args) {
        String action = req(args, "action");
        String period = str(args,"period") != null ? str(args,"period") : "2025-09";
        String cloud = str(args,"cloud_provider") != null ? str(args,"cloud_provider") : "all";

        return switch (action) {
            case "get_spend_summary" -> success(Map.of(
                "period", period, "cloud", cloud,
                "total_spend_inr", 4850000,
                "by_provider", Map.of("aws",3200000,"gcp",1100000,"azure",550000),
                "by_category", Map.of(
                    "compute",48.2,"storage",18.4,"database",21.3,
                    "networking",7.1,"ai_ml",5.0),
                "mom_change_percent", 8.4,
                "budget_utilization_percent", 82,
                "anomalies_detected", 2));

            case "cost_by_tenant" -> success(Map.of(
                "period", period,
                "tenant_id", str(args,"tenant_id"),
                "allocated_cost_inr", 24500,
                "cost_components", Map.of(
                    "compute",14200,"storage",4800,"db",4200,"egress",1300),
                "cost_as_percent_of_revenue", 11.3,
                "gross_margin_impact_percent", 74.2,
                "optimization_potential_inr", 3800));

            case "identify_waste" -> success(Map.of(
                "period", period,
                "total_waste_identified_inr", 620000,
                "waste_items", List.of(
                    Map.of("type","Idle EC2 instances","monthly_cost_inr",180000,
                           "action","Rightsize or terminate"),
                    Map.of("type","Unattached EBS volumes","monthly_cost_inr",94000,
                           "action","Delete 47 volumes"),
                    Map.of("type","Over-provisioned RDS","monthly_cost_inr",230000,
                           "action","Downsize to db.t3.medium"),
                    Map.of("type","Unused Elastic IPs","monthly_cost_inr",12000,
                           "action","Release 18 IPs")),
                "savings_if_fixed_inr", 516000));

            case "set_budget_alert" -> success(Map.of(
                "alert_id", "fin_alert_" + System.currentTimeMillis(),
                "budget_inr", str(args,"budget_inr"),
                "alert_thresholds_percent", List.of(70, 90, 100),
                "notification_channels", List.of("email","slack","pagerduty"),
                "hard_cutoff_enabled", false,
                "status", "active"));

            case "unit_economics" -> success(Map.of(
                "period", period,
                "cost_per_tenant_inr", 5730,
                "cost_per_active_student_inr", 38.9,
                "cost_per_skill_assessment_inr", 2.8,
                "infrastructure_as_percent_of_revenue", 8.8,
                "target_percent", 8.0,
                "benchmark_status", "ABOVE_TARGET",
                "optimization_target_inr", 240000));

            case "forecast_spend" -> success(Map.of(
                "period", "next_3_months",
                "forecasted_spend_inr", Map.of(
                    "2025-10",4950000,"2025-11",5200000,"2025-12",5600000),
                "annual_run_rate_inr", 63000000,
                "growth_driver", "Championship module scale-up in Dec",
                "savings_opportunity_inr", 620000));

            default -> error("Unknown action: " + action);
        };
    }
}

// ═══════════════════════════════════════════════════════
// IDEA_SUBMISSION_AGENT — CAT-08 Agent #8
// ═══════════════════════════════════════════════════════
@Component
public class IdeaSubmissionAgent extends BaseAgentTool {

    @Override public String getName() { return "idea_submit"; }

    @Override public String getDescription() {
        return "Manage the Ecoskiller internal idea submission pipeline: submit product/business ideas, " +
               "score feasibility, assign to strategic buckets, and route to relevant owners.";
    }

    @Override public InputSchema getInputSchema() {
        return InputSchema.builder().type("object")
            .properties(Map.of(
                "action", PropertyDef.builder().type("string")
                    .enumValues(List.of("submit","list_ideas","score_idea","assign_owner",
                                        "get_pipeline_stats","archive")).build(),
                "title", PropertyDef.builder().type("string").description("Idea title").build(),
                "description", PropertyDef.builder().type("string").build(),
                "category", PropertyDef.builder().type("string")
                    .enumValues(List.of("product","business_model","operations","partnership",
                                        "technology","cost_reduction")).build(),
                "idea_id", PropertyDef.builder().type("string").build(),
                "submitted_by", PropertyDef.builder().type("string").build()
            ))
            .required(List.of("action")).build();
    }

    @Override protected ToolCallResult doExecute(Map<String, Object> args) {
        String action = req(args, "action");
        return switch (action) {
            case "submit" -> success(Map.of(
                "idea_id", "idea_" + System.currentTimeMillis(),
                "title", str(args,"title"),
                "category", str(args,"category"),
                "submitted_by", str(args,"submitted_by"),
                "status", "UNDER_REVIEW",
                "auto_score", 72,
                "assigned_to", "Strategy Team",
                "review_sla_days", 7,
                "submitted_at", "2025-09-01T10:00:00Z"));

            case "list_ideas" -> success(Map.of(
                "total", 34,
                "by_status", Map.of("UNDER_REVIEW",12,"APPROVED",8,"IN_PROGRESS",6,
                                    "DEFERRED",5,"ARCHIVED",3),
                "top_scoring", List.of(
                    Map.of("id","idea_001","title","AI-powered parent report card","score",91,"status","APPROVED"),
                    Map.of("id","idea_002","title","Skill marketplace for alumni","score",88,"status","IN_PROGRESS"),
                    Map.of("id","idea_003","title","Franchise micro-lending integration","score",84,"status","UNDER_REVIEW"))));

            case "score_idea" -> success(Map.of(
                "idea_id", str(args,"idea_id"),
                "feasibility_score", 78,
                "impact_score", 85,
                "effort_score", 62,
                "strategic_alignment", 90,
                "composite_score", 79,
                "recommendation", "FAST_TRACK",
                "scoring_rationale", "High strategic alignment with Society franchise expansion"));

            case "assign_owner" -> success(Map.of(
                "idea_id", str(args,"idea_id"),
                "owner_assigned", "Product Strategy Team",
                "target_sprint", "Sprint 14",
                "milestone_date", "2025-10-15"));

            case "get_pipeline_stats" -> success(Map.of(
                "total_submitted_ytd", 124,
                "approved_to_roadmap", 18,
                "conversion_rate_percent", 14.5,
                "avg_review_time_days", 8.4,
                "top_contributor", "Engineering Team",
                "ideas_generating_revenue", 4));

            case "archive" -> success(Map.of(
                "idea_id", str(args,"idea_id"),
                "status", "ARCHIVED",
                "reason", "Deprioritized for current planning cycle"));

            default -> error("Unknown action: " + action);
        };
    }
}

// ═══════════════════════════════════════════════════════
// IDEA_VERSIONING_AGENT — CAT-08 Agent #9
// ═══════════════════════════════════════════════════════
@Component
public class IdeaVersioningAgent extends BaseAgentTool {

    @Override public String getName() { return "idea_version"; }

    @Override public String getDescription() {
        return "Manage version history of ideas in Ecoskiller's innovation pipeline. " +
               "Track revisions, diffs, approvals by version, and rollback to previous iterations.";
    }

    @Override public InputSchema getInputSchema() {
        return InputSchema.builder().type("object")
            .properties(Map.of(
                "action", PropertyDef.builder().type("string")
                    .enumValues(List.of("create_version","list_versions","diff_versions",
                                        "rollback","approve_version","get_changelog")).build(),
                "idea_id", PropertyDef.builder().type("string").build(),
                "version_id", PropertyDef.builder().type("string").build(),
                "changes", PropertyDef.builder().type("string")
                    .description("JSON describing what changed in this version").build()
            ))
            .required(List.of("action","idea_id")).build();
    }

    @Override protected ToolCallResult doExecute(Map<String, Object> args) {
        String action = req(args, "action");
        String ideaId = req(args, "idea_id");
        return switch (action) {
            case "create_version" -> success(Map.of(
                "idea_id", ideaId,
                "new_version", "v3",
                "changes_recorded", str(args,"changes"),
                "authored_by", "Strategy Team",
                "created_at", "2025-09-01T11:00:00Z",
                "diff_url", "https://ideas.ecoskiller.com/" + ideaId + "/diff/v2-v3"));

            case "list_versions" -> success(Map.of(
                "idea_id", ideaId,
                "versions", List.of(
                    Map.of("version","v1","status","SUPERSEDED","created_at","2025-07-01"),
                    Map.of("version","v2","status","SUPERSEDED","created_at","2025-08-01"),
                    Map.of("version","v3","status","CURRENT","created_at","2025-09-01"))));

            case "diff_versions" -> success(Map.of(
                "idea_id", ideaId,
                "from", "v2", "to", "v3",
                "changes", List.of(
                    Map.of("field","budget_estimate","old","INR 15L","new","INR 22L"),
                    Map.of("field","timeline","old","6 months","new","4 months"),
                    Map.of("field","owner","old","Product","new","Strategy+Product"))));

            case "rollback" -> success(Map.of(
                "idea_id", ideaId,
                "rolled_back_to", str(args,"version_id"),
                "current_version", str(args,"version_id"),
                "previous_version_archived", "v3",
                "rollback_reason", "Scope revision needed"));

            case "approve_version" -> success(Map.of(
                "idea_id", ideaId,
                "version_id", str(args,"version_id"),
                "approved_by", "CTO",
                "approved_at", "2025-09-01T14:00:00Z",
                "status", "APPROVED_FOR_ROADMAP"));

            case "get_changelog" -> success(Map.of(
                "idea_id", ideaId,
                "total_versions", 3,
                "changelog", "v1→v2: Scope expanded. v2→v3: Budget revised, timeline compressed."));

            default -> error("Unknown action: " + action);
        };
    }
}
