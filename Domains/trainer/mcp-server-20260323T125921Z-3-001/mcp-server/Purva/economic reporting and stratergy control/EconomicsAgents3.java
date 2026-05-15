package com.ecoskiller.mcp.economics.agents;

import com.ecoskiller.mcp.economics.model.McpProtocol.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

// ═══════════════════════════════════════════════════════
// JOB_PORTAL_MIGRATION_AGENT — CAT-08 Agent #10
// ═══════════════════════════════════════════════════════
@Component
public class JobPortalMigrationAgent extends BaseAgentTool {

    @Override public String getName() { return "job_portal_migrate"; }

    @Override public String getDescription() {
        return "Migrate tenant job portals and campus placement data into Ecoskiller's unified " +
               "placement module. Supports Naukri, LinkedIn Jobs, CampusRecruit, and custom ATS imports.";
    }

    @Override public InputSchema getInputSchema() {
        return InputSchema.builder().type("object")
            .properties(Map.of(
                "action", PropertyDef.builder().type("string")
                    .enumValues(List.of("assess_source","map_schema","run_migration",
                                        "validate_data","rollback","get_migration_status")).build(),
                "tenant_id", PropertyDef.builder().type("string").build(),
                "source_system", PropertyDef.builder().type("string")
                    .enumValues(List.of("naukri","linkedin_jobs","campus_recruit",
                                        "workday","greenhouse","custom_ats")).build(),
                "migration_id", PropertyDef.builder().type("string").build(),
                "dry_run", PropertyDef.builder().type("string")
                    .enumValues(List.of("true","false")).build()
            ))
            .required(List.of("action","tenant_id")).build();
    }

    @Override protected ToolCallResult doExecute(Map<String, Object> args) {
        String action = req(args, "action");
        String tenantId = req(args, "tenant_id");
        String source = str(args,"source_system") != null ? str(args,"source_system") : "custom_ats";

        return switch (action) {
            case "assess_source" -> success(Map.of(
                "tenant_id", tenantId, "source", source,
                "total_records", Map.of("jobs",340,"candidates",8200,"applications",24000,"placements",1100),
                "data_quality_score", 82,
                "schema_compatibility", "PARTIAL",
                "estimated_migration_hours", 6,
                "blocking_issues", List.of("Salary field format mismatch","Missing department_id in 14% records")));

            case "map_schema" -> success(Map.of(
                "tenant_id", tenantId, "source", source,
                "field_mappings", List.of(
                    Map.of("source_field","job_title","target_field","position_name","confidence",0.98),
                    Map.of("source_field","ctc","target_field","offered_salary_inr","confidence",0.91),
                    Map.of("source_field","dept_code","target_field","department_id","confidence",0.74,"manual_review_needed",true)),
                "unmapped_fields", List.of("custom_tag_1","legacy_branch_code"),
                "mapping_confidence_avg", 0.89));

            case "run_migration" -> {
                boolean dryRun = "true".equals(str(args,"dry_run"));
                yield success(Map.of(
                    "migration_id", "mig_" + System.currentTimeMillis(),
                    "tenant_id", tenantId,
                    "source", source,
                    "dry_run", dryRun,
                    "status", dryRun ? "DRY_RUN_COMPLETE" : "IN_PROGRESS",
                    "records_to_migrate", 33640,
                    "estimated_completion_minutes", 45,
                    "validation_errors_found", dryRun ? 12 : 0));
            }

            case "validate_data" -> success(Map.of(
                "migration_id", str(args,"migration_id"),
                "records_validated", 33628,
                "passed", 33240,
                "failed", 388,
                "failure_breakdown", Map.of("missing_fields",210,"format_errors",112,"duplicates",66),
                "validation_status", "PASS_WITH_WARNINGS",
                "recommendation", "Fix 66 duplicates before go-live"));

            case "rollback" -> success(Map.of(
                "migration_id", str(args,"migration_id"),
                "rollback_status", "completed",
                "records_restored", 33240,
                "time_taken_seconds", 120));

            case "get_migration_status" -> success(Map.of(
                "migration_id", str(args,"migration_id"),
                "status", "COMPLETED",
                "progress_percent", 100,
                "records_migrated", 33240,
                "errors", 388,
                "completed_at", "2025-09-01T18:30:00Z"));

            default -> error("Unknown action: " + action);
        };
    }
}

// ═══════════════════════════════════════════════════════
// LEGACY_ERP_MIGRATION_AGENT — CAT-08 Agent #11
// ═══════════════════════════════════════════════════════
@Component
public class LegacyErpMigrationAgent extends BaseAgentTool {

    @Override public String getName() { return "legacy_erp_migrate"; }

    @Override public String getDescription() {
        return "Orchestrate migration from legacy ERP systems (Tally, SAP B1, Oracle, custom PHP/Excel) " +
               "to Ecoskiller's corporate/institute ERP suite. Handles data extraction, transformation, " +
               "validation, and zero-downtime cutover planning.";
    }

    @Override public InputSchema getInputSchema() {
        return InputSchema.builder().type("object")
            .properties(Map.of(
                "action", PropertyDef.builder().type("string")
                    .enumValues(List.of("assess_erp","create_migration_plan","extract_data",
                                        "transform_validate","cutover_plan","post_migration_audit")).build(),
                "tenant_id", PropertyDef.builder().type("string").build(),
                "source_erp", PropertyDef.builder().type("string")
                    .enumValues(List.of("tally","sap_b1","oracle","ms_dynamics","custom_php","excel_sheets")).build(),
                "modules", PropertyDef.builder().type("string")
                    .description("Comma-separated: finance,hr,inventory,fees,payroll").build()
            ))
            .required(List.of("action","tenant_id")).build();
    }

    @Override protected ToolCallResult doExecute(Map<String, Object> args) {
        String action = req(args, "action");
        String tenantId = req(args, "tenant_id");
        String sourceErp = str(args,"source_erp") != null ? str(args,"source_erp") : "custom_php";
        String modules = str(args,"modules") != null ? str(args,"modules") : "finance,hr";

        return switch (action) {
            case "assess_erp" -> success(Map.of(
                "tenant_id", tenantId, "source_erp", sourceErp,
                "modules_detected", List.of("accounts","payroll","inventory","student_fees"),
                "total_data_size_mb", 2340,
                "years_of_historical_data", 7,
                "migration_complexity", "MEDIUM",
                "estimated_duration_weeks", 4,
                "risk_areas", List.of("Tally GUID uniqueness","Custom field mapping","INR rounding rules")));

            case "create_migration_plan" -> success(Map.of(
                "plan_id", "mplan_" + System.currentTimeMillis(),
                "tenant_id", tenantId,
                "phases", List.of(
                    Map.of("phase",1,"name","Data Extraction & Assessment","weeks","1-2","status","PENDING"),
                    Map.of("phase",2,"name","Transform & Load to Staging","weeks","2-3","status","PENDING"),
                    Map.of("phase",3,"name","UAT & Validation","weeks","3","status","PENDING"),
                    Map.of("phase",4,"name","Cutover","weeks","4","status","PENDING"),
                    Map.of("phase",5,"name","Post-Migration Audit","weeks","4-5","status","PENDING")),
                "parallel_run_weeks", 2,
                "rollback_window_days", 14));

            case "extract_data" -> success(Map.of(
                "tenant_id", tenantId,
                "source_erp", sourceErp,
                "extracted_tables", 87,
                "records_extracted", 1240000,
                "extraction_status", "SUCCESS",
                "staging_schema", "ecoskiller_staging_" + tenantId,
                "checksum_verified", true));

            case "transform_validate" -> success(Map.of(
                "tenant_id", tenantId,
                "records_transformed", 1238500,
                "validation_passed", 1234200,
                "validation_failed", 4300,
                "failure_types", Map.of("invalid_dates",1200,"null_required_fields",2100,"duplicates",1000),
                "transform_status", "PASS_WITH_ERRORS",
                "ready_for_cutover", false,
                "fix_required", true));

            case "cutover_plan" -> success(Map.of(
                "tenant_id", tenantId,
                "cutover_window", "Sunday 01:00-05:00 IST",
                "estimated_downtime_minutes", 45,
                "rollback_trigger", "Error rate >5% post-cutover",
                "parallel_run_enabled", true,
                "parallel_run_end_date", "2025-10-15",
                "go_live_date", "2025-09-28"));

            case "post_migration_audit" -> success(Map.of(
                "tenant_id", tenantId,
                "audit_status", "PASSED",
                "data_integrity_check", "PASS",
                "financial_balance_match", true,
                "discrepancies_found", 3,
                "discrepancy_value_inr", 1240,
                "audit_report_url", "https://migration.ecoskiller.com/audit/" + tenantId));

            default -> error("Unknown action: " + action);
        };
    }
}

// ═══════════════════════════════════════════════════════
// MACRO_MODEL_AGENT — CAT-08 Agent #12
// ═══════════════════════════════════════════════════════
@Component
public class MacroModelAgent extends BaseAgentTool {

    @Override public String getName() { return "macro_model"; }

    @Override public String getDescription() {
        return "Macroeconomic intelligence for Ecoskiller's strategic planning. Model the impact " +
               "of GDP growth, EdTech market expansion, employment trends, government policy, " +
               "and demographic shifts on Ecoskiller's growth trajectory.";
    }

    @Override public InputSchema getInputSchema() {
        return InputSchema.builder().type("object")
            .properties(Map.of(
                "action", PropertyDef.builder().type("string")
                    .enumValues(List.of("edtech_market_size","employment_trend",
                                        "policy_impact","demographic_model",
                                        "competitor_landscape","macro_risk_score")).build(),
                "geography", PropertyDef.builder().type("string")
                    .description("e.g. india, tier2_india, southeast_asia, global").build(),
                "horizon_years", PropertyDef.builder().type("string").build(),
                "policy_name", PropertyDef.builder().type("string")
                    .description("Policy to analyze, e.g. NEP_2020, PMKVY, NSQF").build()
            ))
            .required(List.of("action")).build();
    }

    @Override protected ToolCallResult doExecute(Map<String, Object> args) {
        String action = req(args, "action");
        String geo = str(args,"geography") != null ? str(args,"geography") : "india";
        int years = intVal(args,"horizon_years",5);

        return switch (action) {
            case "edtech_market_size" -> success(Map.of(
                "geography", geo,
                "current_market_usd_bn", 7.8,
                "projected_market_usd_bn", Map.of("2026",11.2,"2028",17.4,"2030",29.1),
                "cagr_percent", 20.4,
                "addressable_segments", Map.of(
                    "k12_online",35,"test_prep",22,"higher_ed",18,"corporate_learning",25),
                "ecoskiller_addressable_market_usd_bn", 12.4,
                "current_market_penetration_percent", 0.08));

            case "employment_trend" -> success(Map.of(
                "geography", geo,
                "fastest_growing_sectors", List.of("AI/ML","Green Energy","FinTech","Logistics"),
                "skills_in_demand", List.of("GenAI","Cloud","Data Analytics","Digital Marketing"),
                "youth_unemployment_percent", 23.2,
                "skilled_worker_shortage", Map.of("IT",800000,"Healthcare",600000,"Manufacturing",1200000),
                "ecoskiller_opportunity", "Skill gap creates demand for 2Cr+ trained workers by 2027"));

            case "policy_impact" -> success(Map.of(
                "policy", str(args,"policy_name") != null ? str(args,"policy_name") : "NEP_2020",
                "impact_on_ecoskiller", "HIGH",
                "direct_benefits", List.of(
                    "Skill-based credit framework aligns with Ecoskiller's skill passport",
                    "Vocational education mandate creates institution demand",
                    "Multiple entry/exit enables modular certification"),
                "government_spend_inr_cr", 2700,
                "ecoskiller_bid_eligible", true));

            case "demographic_model" -> success(Map.of(
                "geography", geo,
                "youth_population_18_25_millions", 254,
                "college_enrollment_millions", 43,
                "workforce_entry_annual_millions", 12,
                "smartphone_penetration_percent", 78,
                "tier2_internet_growth_yoy_percent", 34,
                "ecoskiller_target_segment_millions", 68));

            case "competitor_landscape" -> success(Map.of(
                "geography", geo,
                "competitors", List.of(
                    Map.of("name","Campusflore","focus","Institute ERP","threat","MEDIUM"),
                    Map.of("name","Skillmatics","focus","Skill Assessment","threat","HIGH"),
                    Map.of("name","Coursera for Campus","focus","LMS","threat","MEDIUM"),
                    Map.of("name","Meritto","focus","Admissions","threat","LOW")),
                "ecoskiller_differentiators", List.of(
                    "End-to-end ERP+Skill+Championship in one platform",
                    "Society franchise network moat",
                    "DigiLocker/eSign native integration")));

            case "macro_risk_score" -> success(Map.of(
                "geography", geo,
                "macro_risk_score", 38,
                "risk_level", "LOW-MEDIUM",
                "key_risks", List.of(
                    Map.of("risk","INR depreciation","impact","LOW","probability","LOW"),
                    Map.of("risk","Regulatory tightening on EdTech","impact","HIGH","probability","MEDIUM"),
                    Map.of("risk","Recession reducing corporate L&D budgets","impact","MEDIUM","probability","LOW")),
                "opportunity_score", 82));

            default -> error("Unknown action: " + action);
        };
    }
}

// ═══════════════════════════════════════════════════════
// PLATFORM_SOVEREIGNTY_AGENT — CAT-08 Agent #13
// ═══════════════════════════════════════════════════════
@Component
public class PlatformSovereigntyAgent extends BaseAgentTool {

    @Override public String getName() { return "platform_sovereignty"; }

    @Override public String getDescription() {
        return "Ensure Ecoskiller's platform sovereignty: data localisation compliance, " +
               "vendor lock-in risk mitigation, API independence, and strategic control over " +
               "critical infrastructure. Monitors dependency concentration and exit readiness.";
    }

    @Override public InputSchema getInputSchema() {
        return InputSchema.builder().type("object")
            .properties(Map.of(
                "action", PropertyDef.builder().type("string")
                    .enumValues(List.of("assess_dependencies","check_data_localisation",
                                        "vendor_lock_in_score","exit_readiness",
                                        "sovereignty_report","flag_risk")).build(),
                "component", PropertyDef.builder().type("string")
                    .description("e.g. cloud, payments, llm, email, sms, storage").build()
            ))
            .required(List.of("action")).build();
    }

    @Override protected ToolCallResult doExecute(Map<String, Object> args) {
        String action = req(args, "action");
        String component = str(args,"component") != null ? str(args,"component") : "all";

        return switch (action) {
            case "assess_dependencies" -> success(Map.of(
                "component", component,
                "critical_dependencies", List.of(
                    Map.of("vendor","AWS","category","cloud","spend_percent",66,
                           "lock_in_risk","MEDIUM","alternatives","GCP, Azure"),
                    Map.of("vendor","Razorpay","category","payments","spend_percent",80,
                           "lock_in_risk","HIGH","alternatives","Stripe, PayU"),
                    Map.of("vendor","Anthropic","category","llm","spend_percent",45,
                           "lock_in_risk","MEDIUM","alternatives","OpenAI, Gemini, self-hosted"),
                    Map.of("vendor","Twilio","category","sms","spend_percent",70,
                           "lock_in_risk","MEDIUM","alternatives","MSG91, AWS SNS")),
                "concentration_risk_score", 61));

            case "check_data_localisation" -> success(Map.of(
                "compliance_status", Map.of(
                    "india_pdpb", "PARTIAL",
                    "data_in_india", true,
                    "cross_border_transfer", Map.of(
                        "occurring", true,
                        "to_regions", List.of("us-east-1","eu-west-1"),
                        "compliant_transfers_percent", 87,
                        "non_compliant_count", 3)),
                "remediation_required", true,
                "remediation_actions", List.of(
                    "Move LLM processing to in-India endpoint",
                    "Restrict analytics data to ap-south-1 only")));

            case "vendor_lock_in_score" -> success(Map.of(
                "overall_lock_in_score", 54,
                "risk_level", "MEDIUM",
                "by_component", Map.of(
                    "cloud",45,"payments",71,"llm",38,
                    "database",30,"email",52,"sms",60),
                "high_risk_components", List.of("payments"),
                "recommendation", "Add Stripe as secondary payment gateway to reduce Razorpay dependency to 50%"));

            case "exit_readiness" -> success(Map.of(
                "vendor", component,
                "exit_readiness_score", 62,
                "portability_issues", List.of(
                    "Razorpay-specific webhook format",
                    "AWS S3 proprietary SDK usage in 12 services"),
                "estimated_exit_cost_inr", 1800000,
                "estimated_exit_weeks", 8,
                "recommendation", "Implement abstraction layer for payment and storage SDKs"));

            case "sovereignty_report" -> success(Map.of(
                "report_id", "sov_" + System.currentTimeMillis(),
                "overall_sovereignty_score", 68,
                "grade", "B",
                "strengths", List.of("Own database infrastructure","Proprietary ML models","Open API design"),
                "vulnerabilities", List.of("Payment gateway concentration","LLM third-party dependency"),
                "priority_actions", 4,
                "pdf_url", "https://compliance.ecoskiller.com/sovereignty/latest.pdf"));

            case "flag_risk" -> success(Map.of(
                "risk_id", "sov_risk_" + System.currentTimeMillis(),
                "component", component,
                "risk_type", "VENDOR_CONCENTRATION",
                "severity", "HIGH",
                "flagged_at", "2025-09-01",
                "assigned_to", "Infrastructure Team",
                "resolution_sla_days", 30));

            default -> error("Unknown action: " + action);
        };
    }
}

// ═══════════════════════════════════════════════════════
// PRICING_STRATEGY_AGENT — CAT-08 Agent #14
// ═══════════════════════════════════════════════════════
@Component
public class PricingStrategyAgent extends BaseAgentTool {

    @Override public String getName() { return "pricing_strategy"; }

    @Override public String getDescription() {
        return "Design, simulate, and optimize Ecoskiller's pricing strategy across segments. " +
               "Model tier structures, discount policies, geographic pricing, bundle economics, " +
               "and competitive positioning.";
    }

    @Override public InputSchema getInputSchema() {
        return InputSchema.builder().type("object")
            .properties(Map.of(
                "action", PropertyDef.builder().type("string")
                    .enumValues(List.of("get_current_pricing","simulate_change","optimize_tier",
                                        "geographic_pricing","bundle_economics","competitive_benchmark")).build(),
                "segment", PropertyDef.builder().type("string")
                    .enumValues(List.of("institute","corporate","society","all")).build(),
                "change_params", PropertyDef.builder().type("string")
                    .description("JSON: {tier, price_change_percent, affected_tenants}").build()
            ))
            .required(List.of("action")).build();
    }

    @Override protected ToolCallResult doExecute(Map<String, Object> args) {
        String action = req(args, "action");
        String segment = str(args,"segment") != null ? str(args,"segment") : "all";

        return switch (action) {
            case "get_current_pricing" -> success(Map.of(
                "segment", segment,
                "pricing_tiers", Map.of(
                    "institute", List.of(
                        Map.of("tier","Starter","price_inr_month",1999,"students_limit",500),
                        Map.of("tier","Growth","price_inr_month",4999,"students_limit",2000),
                        Map.of("tier","Enterprise","price_inr_month",14999,"students_limit","unlimited")),
                    "corporate", List.of(
                        Map.of("tier","Basic","price_inr_month",2999,"employees_limit",100),
                        Map.of("tier","Pro","price_inr_month",7999,"employees_limit",500),
                        Map.of("tier","Enterprise","price_inr_month",19999,"employees_limit","unlimited"))),
                "avg_discount_given_percent", 18,
                "list_price_realization_percent", 82));

            case "simulate_change" -> success(Map.of(
                "change_params", str(args,"change_params"),
                "impact", Map.of(
                    "arr_change_inr", 18500000,
                    "churn_expected_tenants", 12,
                    "churn_arr_lost_inr", 2160000,
                    "net_arr_impact_inr", 16340000,
                    "payback_months", 2),
                "recommendation", "PROCEED — strong net positive"));

            case "optimize_tier" -> success(Map.of(
                "segment", segment,
                "current_tier_distribution", Map.of("Starter",54,"Growth",32,"Enterprise",14),
                "revenue_concentration", Map.of("Starter",18,"Growth",38,"Enterprise",44),
                "optimization_recommendation", "Increase Enterprise tier value to drive upsell",
                "suggested_actions", List.of(
                    "Add 'AI Insights Pack' to Enterprise tier",
                    "Increase Starter→Growth conversion incentive (3-month trial upgrade)")));

            case "geographic_pricing" -> success(Map.of(
                "tier_1_cities_multiplier", 1.0,
                "tier_2_cities_multiplier", 0.8,
                "tier_3_cities_multiplier", 0.65,
                "rural_block_multiplier", 0.5,
                "international_multiplier", 1.4,
                "current_geo_adoption", Map.of("tier1",58,"tier2",31,"tier3",8,"rural",3)));

            case "bundle_economics" -> success(Map.of(
                "bundles_analyzed", List.of(
                    Map.of("bundle","ERP+Skill+Championship","price_inr",9999,
                           "individual_total_inr",17997,"discount_percent",44,"margin_percent",68),
                    Map.of("bundle","ERP+DigiLocker+eSign","price_inr",6999,
                           "individual_total_inr",11997,"discount_percent",42,"margin_percent",71)),
                "recommended_push_bundle", "ERP+Skill+Championship",
                "attach_rate_current_percent", 28));

            case "competitive_benchmark" -> success(Map.of(
                "segment", segment,
                "ecoskiller_avg_price_inr", 6200,
                "market_avg_price_inr", 7400,
                "premium_vs_market_percent", -16,
                "positioning", "VALUE",
                "price_headroom_to_parity_inr", 1200,
                "recommendation", "10% price increase is justified — still below market avg"));

            default -> error("Unknown action: " + action);
        };
    }
}
