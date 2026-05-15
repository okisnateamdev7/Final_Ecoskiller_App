package com.ecoskiller.mcp;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ╔═══════════════════════════════════════════════════════════════════╗
 * ║  Ecoskiller | CAT-12                                             ║
 * ║  Institute + HR Predictive Systems                               ║
 * ║  MCP Server · Java · 20 Agents · Antigravity Architecture       ║
 * ║  Transport : stdio  |  Protocol : JSON-RPC 2.0                  ║
 * ║  MCP Version : 2024-11-05                                       ║
 * ╚═══════════════════════════════════════════════════════════════════╝
 */
public class McpServer {

    static final String MCP_VERSION    = "2024-11-05";
    static final String SERVER_NAME    = "mcp-12-institute-hr";
    static final String SERVER_VERSION = "1.0.0";
    static final String CATEGORY       = "CAT-12";
    static final String ARCH           = "ANTIGRAVITY";

    static final Map<String, ToolHandler> TOOLS = new LinkedHashMap<>();

    static {

        // ── 1. TEAM_COMPATIBILITY_GNN ────────────────────────────────────
        TOOLS.put("team_compatibility_gnn", args -> result(
            "TEAM_COMPATIBILITY_GNN_ANTIGRAVITY", map(
            "team_id",               str(args, "team_id"),
            "members_analyzed",      (int) dbl(args, "member_count"),
            "compatibility_score",   0.83,
            "compatibility_label",   "HIGH",
            "graph_nodes",           (int) dbl(args, "member_count"),
            "graph_edges",           (int) (dbl(args, "member_count") * (dbl(args, "member_count") - 1) / 2),
            "role_balance_score",    0.79,
            "communication_density", 0.71,
            "conflict_risk",         "LOW",
            "synergy_clusters",      list(
                map("cluster_id", "C1", "members", list("u1","u2","u3"), "synergy_score", 0.91),
                map("cluster_id", "C2", "members", list("u4","u5"),      "synergy_score", 0.86)
            ),
            "weakest_link_role",     "COORDINATOR",
            "recommended_additions", list("STRATEGIC_THINKER", "DOMAIN_EXPERT"),
            "gnn_model",             "ANTIGRAVITY_TEAM_GNN_V3"
        )));

        // ── 2. TALENT_RETRIEVAL_VECTOR_ENGINE ───────────────────────────
        TOOLS.put("talent_retrieval_vector_engine", args -> result(
            "TALENT_RETRIEVAL_VECTOR_ENGINE_ANTIGRAVITY", map(
            "query",               str(args, "search_query"),
            "top_k",               (int) dbl(args, "top_k"),
            "results", list(
                map("candidate_id","c-1021","name","Arjun Mehta",   "match_score",0.96,"skills",list("Python","ML","Leadership")),
                map("candidate_id","c-0874","name","Priya Nair",    "match_score",0.93,"skills",list("Data Science","SQL","Communication")),
                map("candidate_id","c-1150","name","Rahul Sharma",  "match_score",0.91,"skills",list("Java","System Design","Agile")),
                map("candidate_id","c-0662","name","Sneha Patel",   "match_score",0.88,"skills",list("Product Management","Analytics")),
                map("candidate_id","c-0391","name","Dev Krishnan",  "match_score",0.85,"skills",list("Entrepreneurship","Strategy"))
            ),
            "vector_dim",          768,
            "index_size",          48200,
            "retrieval_latency_ms",14,
            "embedding_model",     "ANTIGRAVITY_TALENT_EMBED_V2"
        )));

        // ── 3. WORKFORCE_GAP_MODEL ───────────────────────────────────────
        TOOLS.put("workforce_gap_model", args -> result(
            "ECOSKILLER_Workforce_Gap_Model_SEALED", map(
            "org_id",              str(args, "org_id"),
            "department",          str(args, "department"),
            "total_headcount",     (int) dbl(args, "headcount"),
            "gap_analysis", list(
                map("skill","Advanced ML Engineering",    "current_coverage",0.31,"required_coverage",0.70,"gap_severity","CRITICAL"),
                map("skill","Cloud Architecture (AWS)",   "current_coverage",0.45,"required_coverage",0.65,"gap_severity","HIGH"),
                map("skill","Data Governance & Privacy",  "current_coverage",0.52,"required_coverage",0.60,"gap_severity","MEDIUM"),
                map("skill","LLM Fine-tuning",            "current_coverage",0.12,"required_coverage",0.40,"gap_severity","CRITICAL"),
                map("skill","Product Strategy",           "current_coverage",0.60,"required_coverage",0.65,"gap_severity","LOW")
            ),
            "critical_gaps_count", 2,
            "recommended_hires",   4,
            "upskilling_candidates", 7,
            "cost_to_close_gap_inr", 2400000,
            "timeline_months",       6,
            "model",               "ANTIGRAVITY_WORKFORCE_GAP_V2"
        )));

        // ── 4. HIRING_BIAS_DETECTOR ──────────────────────────────────────
        TOOLS.put("hiring_bias_detector", args -> result(
            "ECOSKILLER_Hiring_Bias_Detector_SEALED", map(
            "pipeline_id",         str(args, "pipeline_id"),
            "candidates_audited",  (int) dbl(args, "candidates_audited"),
            "bias_detected",       true,
            "bias_dimensions", list(
                map("dimension","Gender",          "bias_score",0.18,"direction","AGAINST_FEMALE","stage","SHORTLISTING"),
                map("dimension","Institution Tier","bias_score",0.31,"direction","FAVOUR_TIER1",  "stage","INTERVIEW"),
                map("dimension","Age",             "bias_score",0.09,"direction","AGAINST_35PLUS","stage","FINAL_OFFER")
            ),
            "overall_fairness_score", 0.71,
            "regulatory_risk",        "MEDIUM",
            "recommended_actions", list(
                "ANONYMISE_CVNAME_INSTITUTION_AT_SHORTLISTING",
                "CALIBRATE_INTERVIEW_RUBRIC_AGAINST_JD",
                "ADD_STRUCTURED_SCORING_FOR_FINAL_ROUND"
            ),
            "audit_timestamp",     new Date().toString(),
            "model",               "ANTIGRAVITY_BIAS_DETECT_V2"
        )));

        // ── 5. DIGITAL_TWIN_SIMULATION_ENGINE ───────────────────────────
        TOOLS.put("digital_twin_simulation", args -> result(
            "ECOSKILLER_Digital_Twin_Simulation_Engine_SEALED", map(
            "entity_id",           str(args, "entity_id"),
            "entity_type",         str(args, "entity_type"),
            "simulation_horizon_months", (int) dbl(args, "horizon_months"),
            "scenarios_run",       3,
            "scenario_outcomes", list(
                map("scenario","OPTIMISTIC","projected_performance",92.4,"revenue_impact_pct",+18.0,"risk","LOW"),
                map("scenario","BASELINE",  "projected_performance",78.1,"revenue_impact_pct",  +6.0,"risk","MEDIUM"),
                map("scenario","PESSIMISTIC","projected_performance",61.3,"revenue_impact_pct", -8.0,"risk","HIGH")
            ),
            "recommended_scenario", "OPTIMISTIC",
            "key_leverage_factors", list("TALENT_DENSITY","CURRICULUM_ALIGNMENT","INDUSTRY_CONNECT"),
            "twin_fidelity_score",  0.87,
            "model",                "ANTIGRAVITY_DIGITAL_TWIN_V3"
        )));

        // ── 6. CANDIDATE_RANKING_MODEL ───────────────────────────────────
        TOOLS.put("candidate_ranking_model", args -> result(
            "CANDIDATE_RANKING_MODEL_ANTIGRAVITY", map(
            "job_id",              str(args, "job_id"),
            "candidates_evaluated",(int) dbl(args, "candidate_count"),
            "ranked_candidates", list(
                map("rank",1,"candidate_id","c-1021","composite_score",94.2,"strengths",list("Technical Depth","Communication")),
                map("rank",2,"candidate_id","c-0874","composite_score",91.8,"strengths",list("Analytical Thinking","Domain Knowledge")),
                map("rank",3,"candidate_id","c-1150","composite_score",89.4,"strengths",list("System Thinking","Team Collaboration")),
                map("rank",4,"candidate_id","c-0662","composite_score",86.0,"strengths",list("Strategic Vision","Execution Speed")),
                map("rank",5,"candidate_id","c-0391","composite_score",82.7,"strengths",list("Entrepreneurial Mindset","Risk Tolerance"))
            ),
            "scoring_weights", map(
                "technical_skill", 0.40, "soft_skills", 0.25,
                "culture_fit",     0.20, "growth_potential", 0.15
            ),
            "top_recommendation",  "c-1021",
            "model",               "ANTIGRAVITY_CAND_RANK_V3"
        )));

        // ── 7. STUDENT_DISTRIBUTION_MODEL ───────────────────────────────
        TOOLS.put("student_distribution_model", args -> result(
            "ANTIGRAVITY_STUDENT_DISTRIBUTION_MODEL", map(
            "institute_id",        str(args, "institute_id"),
            "academic_year",       str(args, "academic_year"),
            "total_students",      (int) dbl(args, "total_students"),
            "distribution_by_performance", map(
                "EXCEPTIONAL",  0.08,
                "HIGH",         0.22,
                "AVERAGE",      0.41,
                "BELOW_AVERAGE",0.21,
                "AT_RISK",      0.08
            ),
            "geographic_distribution", map(
                "URBAN", 0.54, "SEMI_URBAN", 0.28, "RURAL", 0.18
            ),
            "gender_ratio", map("MALE", 0.54, "FEMALE", 0.44, "OTHER", 0.02),
            "at_risk_count",           (int)(dbl(args,"total_students") * 0.08),
            "intervention_recommended",true,
            "model",                   "ANTIGRAVITY_STU_DIST_V2"
        )));

        // ── 8. SALARY_PREDICTION_MODEL ───────────────────────────────────
        TOOLS.put("salary_prediction_model", args -> result(
            "ANTIGRAVITY_SALARY_PREDICTION_MODEL_v1", map(
            "candidate_id",        str(args, "candidate_id"),
            "role",                str(args, "role"),
            "experience_years",    dbl(args, "experience_years"),
            "location",            str(args, "location"),
            "predicted_salary", map(
                "currency",        "INR",
                "annual_min",      (long)(dbl(args,"experience_years") * 200000 + 400000),
                "annual_median",   (long)(dbl(args,"experience_years") * 250000 + 600000),
                "annual_max",      (long)(dbl(args,"experience_years") * 350000 + 900000)
            ),
            "market_percentile",   72,
            "negotiation_range",   "12-18%",
            "key_salary_drivers",  list("SKILL_RARITY","DOMAIN_DEMAND","LOCATION_PREMIUM","CERT_PREMIUM"),
            "benchmark_source",    "ANTIGRAVITY_SALARY_BENCH_2025",
            "model",               "ANTIGRAVITY_SALARY_V1"
        )));

        // ── 9. RETENTION_PROBABILITY_MODEL ──────────────────────────────
        TOOLS.put("retention_probability_model", args -> result(
            "ANTIGRAVITY_RETENTION_PROBABILITY_MODEL_v1", map(
            "employee_id",         str(args, "employee_id"),
            "retention_probability_12m", 0.81,
            "flight_risk",         "LOW",
            "risk_factors", list(
                map("factor","Compensation below market",  "weight",0.35,"present",false),
                map("factor","Low growth visibility",      "weight",0.28,"present",true),
                map("factor","Manager relationship score", "weight",0.20,"present",false),
                map("factor","Work-life balance stress",   "weight",0.17,"present",true)
            ),
            "engagement_score",    0.74,
            "predicted_tenure_months", 28,
            "recommended_actions", list(
                "CAREER_PATH_CLARITY_DISCUSSION",
                "FLEXIBLE_WORK_POLICY_OFFER",
                "SKILL_DEVELOPMENT_SPONSORSHIP"
            ),
            "model",               "ANTIGRAVITY_RETENTION_V1"
        )));

        // ── 10. PROMOTION_PROBABILITY_MODEL ─────────────────────────────
        TOOLS.put("promotion_probability_model", args -> result(
            "Antigravity_Promotion_Probability_Model", map(
            "employee_id",         str(args, "employee_id"),
            "current_level",       str(args, "current_level"),
            "promotion_probability_12m", 0.63,
            "promotion_readiness", "NEAR_READY",
            "readiness_gaps", list(
                map("competency","Strategic Influence",  "current",3,"required",4,"gap",1),
                map("competency","Cross-team Leadership","current",3,"required",4,"gap",1),
                map("competency","Technical Depth",      "current",4,"required",4,"gap",0)
            ),
            "estimated_promotion_month", 9,
            "sponsor_required",    true,
            "visibility_score",    0.68,
            "model",               "ANTIGRAVITY_PROMO_PROB_V2"
        )));

        // ── 11. PLACEMENT_PROBABILITY_MODEL ─────────────────────────────
        TOOLS.put("placement_probability_model", args -> result(
            "ANTIGRAVITY_PLACEMENT_PROBABILITY_MODEL", map(
            "student_id",              str(args, "student_id"),
            "graduation_year",         str(args, "graduation_year"),
            "placement_probability",   0.78,
            "placement_label",         "HIGH",
            "expected_package_inr", map(
                "min", 600000L, "median", 900000L, "max", 1800000L
            ),
            "top_matching_companies",  list("Infosys","Flipkart","Razorpay","BYJU'S","Amazon"),
            "domain_fit_scores", map(
                "SOFTWARE_ENGINEERING", 0.88,
                "DATA_SCIENCE",         0.81,
                "PRODUCT_MANAGEMENT",   0.74,
                "CONSULTING",           0.62
            ),
            "interview_readiness",     0.71,
            "model",                   "ANTIGRAVITY_PLACEMENT_V2"
        )));

        // ── 12. PERFORMANCE_FORECAST_MODEL ──────────────────────────────
        TOOLS.put("performance_forecast_model", args -> result(
            "ANTIGRAVITY_PERFORMANCE_FORECAST_MODEL_v1", map(
            "employee_id",             str(args, "employee_id"),
            "forecast_horizon_months", (int) dbl(args, "forecast_months"),
            "current_performance_score", dbl(args, "current_score"),
            "predicted_performance", list(
                map("month", 3, "score", dbl(args,"current_score") * 1.04, "confidence", 0.89),
                map("month", 6, "score", dbl(args,"current_score") * 1.08, "confidence", 0.82),
                map("month", 12,"score", dbl(args,"current_score") * 1.14, "confidence", 0.74)
            ),
            "trend",                   "IMPROVING",
            "performance_ceiling",     dbl(args,"current_score") * 1.22,
            "key_growth_levers",       list("MENTORSHIP_ACCESS","STRETCH_ASSIGNMENTS","TECH_CERTIFICATIONS"),
            "model",                   "ANTIGRAVITY_PERF_FORECAST_V1"
        )));

        // ── 13. INTERVIEW_SEMANTIC_ANALYZER ─────────────────────────────
        TOOLS.put("interview_semantic_analyzer", args -> result(
            "Antigravity_Interview_Semantic_Analyzer", map(
            "interview_id",         str(args, "interview_id"),
            "candidate_id",         str(args, "candidate_id"),
            "transcript_length",    str(args, "transcript_text").split("\\s+").length,
            "semantic_scores", map(
                "technical_depth",    82,
                "communication",      78,
                "problem_structuring",85,
                "cultural_alignment", 74,
                "leadership_signals", 69
            ),
            "sentiment_arc",        "CONFIDENT_START_SLIGHT_FATIGUE_END",
            "keyword_density", map(
                "domain_keywords",  0.18,
                "action_verbs",     0.12,
                "quantified_impact",0.09
            ),
            "red_flags",            list(),
            "overall_signal_score", 77,
            "hire_signal",          "RECOMMEND",
            "model",                "ANTIGRAVITY_INTERVIEW_SEM_V2"
        )));

        // ── 14. INSTITUTE_REVENUE_FORECAST ──────────────────────────────
        TOOLS.put("institute_revenue_forecast", args -> result(
            "ANTIGRAVITY_INSTITUTE_REVENUE_FORECAST_MODEL_v1.0", map(
            "institute_id",          str(args, "institute_id"),
            "forecast_year",         str(args, "forecast_year"),
            "current_revenue_inr",   dbl(args, "current_revenue_inr"),
            "revenue_forecast", list(
                map("quarter","Q1","projected_inr", dbl(args,"current_revenue_inr") * 0.24,"growth_pct", 8.0),
                map("quarter","Q2","projected_inr", dbl(args,"current_revenue_inr") * 0.27,"growth_pct",12.0),
                map("quarter","Q3","projected_inr", dbl(args,"current_revenue_inr") * 0.26,"growth_pct",11.0),
                map("quarter","Q4","projected_inr", dbl(args,"current_revenue_inr") * 0.28,"growth_pct",14.0)
            ),
            "annual_projected_inr",  dbl(args,"current_revenue_inr") * 1.11,
            "revenue_growth_pct",    11.2,
            "growth_drivers",        list("NEW_BATCH_ENROLLMENT","PLACEMENT_PREMIUM_FEES","CORPORATE_TRAINING"),
            "risk_factors",          list("ATTRITION_SPIKE","COMPETITION_PRICING"),
            "model",                 "ANTIGRAVITY_INST_REV_FORE_V1"
        )));

        // ── 15. INSTITUTE_RANKING_MODEL ──────────────────────────────────
        TOOLS.put("institute_ranking_model", args -> result(
            "ANTIGRAVITY_INSTITUTE_RANKING_MODEL", map(
            "institute_id",         str(args, "institute_id"),
            "ranking_scope",        str(args, "ranking_scope"),
            "composite_rank",       14,
            "rank_score",           78.4,
            "dimension_scores", map(
                "academic_outcomes",   82,
                "placement_rate",      79,
                "faculty_quality",     74,
                "infrastructure",      71,
                "industry_connect",    83,
                "student_satisfaction",77,
                "research_output",     61
            ),
            "rank_delta_vs_last_year", "+3",
            "peer_institutes",         list("INST-004","INST-011","INST-029"),
            "strength_pillars",        list("INDUSTRY_CONNECT","ACADEMIC_OUTCOMES"),
            "improvement_areas",       list("RESEARCH_OUTPUT","INFRASTRUCTURE"),
            "model",                   "ANTIGRAVITY_INST_RANK_V2"
        )));

        // ── 16. INSTITUTE_PERFORMANCE_PREDICTOR ─────────────────────────
        TOOLS.put("institute_performance_predictor", args -> result(
            "ANTIGRAVITY_INSTITUTE_PERFORMANCE_PREDICTOR_v1.0", map(
            "institute_id",              str(args, "institute_id"),
            "prediction_horizon_months", (int) dbl(args, "horizon_months"),
            "current_performance_index", dbl(args, "current_index"),
            "predicted_index_6m",        dbl(args,"current_index") * 1.06,
            "predicted_index_12m",       dbl(args,"current_index") * 1.12,
            "performance_trajectory",    "STEADY_GROWTH",
            "key_performance_levers", list(
                map("lever","Faculty Retention",      "impact_weight",0.32,"current_score",0.74),
                map("lever","Curriculum Freshness",   "impact_weight",0.28,"current_score",0.68),
                map("lever","Placement Partnerships", "impact_weight",0.22,"current_score",0.81),
                map("lever","Student NPS",            "impact_weight",0.18,"current_score",0.71)
            ),
            "risk_of_decline",           0.14,
            "model",                     "ANTIGRAVITY_INST_PERF_V1"
        )));

        // ── 17. CURRICULUM_GAP_DETECTOR ─────────────────────────────────
        TOOLS.put("curriculum_gap_detector", args -> result(
            "ANTIGRAVITY_CURRICULUM_GAP_DETECTOR_v1.0", map(
            "institute_id",    str(args, "institute_id"),
            "program",         str(args, "program"),
            "gaps_detected", list(
                map("topic","Generative AI & LLMs",      "industry_demand",0.91,"curriculum_coverage",0.12,"priority","CRITICAL"),
                map("topic","Cloud-Native Architecture", "industry_demand",0.84,"curriculum_coverage",0.31,"priority","HIGH"),
                map("topic","Data Engineering Pipelines","industry_demand",0.79,"curriculum_coverage",0.40,"priority","HIGH"),
                map("topic","MLOps & Model Deployment",  "industry_demand",0.77,"curriculum_coverage",0.18,"priority","CRITICAL"),
                map("topic","Ethical AI & Governance",   "industry_demand",0.65,"curriculum_coverage",0.20,"priority","MEDIUM")
            ),
            "total_gaps",            5,
            "critical_gap_count",    2,
            "curriculum_freshness_score", 0.54,
            "recommended_updates",   list("ADD_GEN_AI_MODULE","UPDATE_CLOUD_TRACK","INTRODUCE_MLOPS_LAB"),
            "industry_alignment_pct", 61.0,
            "model",                 "ANTIGRAVITY_CURRICULUM_GAP_V1"
        )));

        // ── 18. CLASS_INTELLIGENCE_AGGREGATOR ───────────────────────────
        TOOLS.put("class_intelligence_aggregator", args -> result(
            "ANTIGRAVITY_CLASS_INTELLIGENCE_AGGREGATOR", map(
            "class_id",             str(args, "class_id"),
            "institute_id",         str(args, "institute_id"),
            "student_count",        (int) dbl(args, "student_count"),
            "aggregate_intelligence", map(
                "logical_avg",         74.2,
                "spatial_avg",         71.8,
                "linguistic_avg",      68.4,
                "entrepreneurial_avg", 66.1,
                "interpersonal_avg",   72.9,
                "kinesthetic_avg",     63.7
            ),
            "top_performers_pct",     0.18,
            "at_risk_students_pct",   0.11,
            "class_iq_band",          "AVERAGE_TO_HIGH",
            "cohort_strength",        "LOGICAL_AND_SPATIAL",
            "cohort_gap",             "ENTREPRENEURIAL",
            "recommended_teaching_style","PROBLEM_BASED_LEARNING",
            "model",                  "ANTIGRAVITY_CLASS_INTEL_V2"
        )));

        // ── 19. CANDIDATE_SUMMARY_GENERATOR ─────────────────────────────
        TOOLS.put("candidate_summary_generator", args -> result(
            "Antigravity_Candidate_Summary_Generator", map(
            "candidate_id",      str(args, "candidate_id"),
            "summary_id",        "SUM-" + System.currentTimeMillis(),
            "executive_summary", "A highly analytical candidate with 5+ years in data engineering, demonstrating advanced logical reasoning (top 8%) and strong collaborative skills. Ideal fit for senior IC or early tech-lead roles.",
            "key_strengths",     list("Statistical Modelling","Pipeline Architecture","Cross-functional Collaboration","Fast Learner"),
            "development_areas", list("Executive Communication","Stakeholder Influence"),
            "culture_fit_score", 0.82,
            "role_fit_score",    0.89,
            "unique_differentiator","Published 2 open-source data tools with 1.2K GitHub stars",
            "recommended_roles", list("Senior Data Engineer","ML Platform Lead","Analytics Engineering Manager"),
            "summary_confidence",0.91,
            "model",             "ANTIGRAVITY_CAND_SUM_V2"
        )));

        // ── 20. BENCHMARK_COMPARISON_ENGINE ─────────────────────────────
        TOOLS.put("benchmark_comparison_engine", args -> result(
            "ANTIGRAVITY_BENCHMARK_COMPARISON_ENGINE_v1.0", map(
            "entity_id",           str(args, "entity_id"),
            "entity_type",         str(args, "entity_type"),
            "benchmark_scope",     str(args, "benchmark_scope"),
            "overall_benchmark_score", 76.8,
            "benchmark_percentile",    68,
            "dimension_benchmarks", list(
                map("dimension","Technical Capability", "entity_score",82,"benchmark_avg",74,"delta","+8","status","ABOVE"),
                map("dimension","Process Maturity",     "entity_score",71,"benchmark_avg",78,"delta","-7","status","BELOW"),
                map("dimension","Innovation Index",     "entity_score",79,"benchmark_avg",72,"delta","+7","status","ABOVE"),
                map("dimension","Talent Quality",       "entity_score",84,"benchmark_avg",76,"delta","+8","status","ABOVE"),
                map("dimension","Cost Efficiency",      "entity_score",68,"benchmark_avg",73,"delta","-5","status","BELOW")
            ),
            "above_benchmark_count",   3,
            "below_benchmark_count",   2,
            "top_peer",                "ENTITY_0042",
            "gap_to_top_peer",         11.4,
            "improvement_priority",    list("PROCESS_MATURITY","COST_EFFICIENCY"),
            "model",                   "ANTIGRAVITY_BENCHMARK_COMP_V1"
        )));
    }

    // ── main ─────────────────────────────────────────────────────────────
    public static void main(String[] args) throws IOException {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in,  StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
        System.err.println("[" + SERVER_NAME + "] Antigravity MCP ready — " + TOOLS.size() + " agents");
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try   { out.println(dispatch(line)); }
            catch (Exception ex) { out.println(errResp(null, -32700, "Parse error: " + ex.getMessage())); }
        }
    }

    // ── dispatcher ───────────────────────────────────────────────────────
    static String dispatch(String raw) {
        Map<String,Object> req = JsonParser.parse(raw);
        Object id     = req.get("id");
        String method = (String) req.get("method");
        if (method == null) return errResp(id, -32600, "Invalid Request");
        switch (method) {
            case "initialize": return okResp(id, map(
                "protocolVersion", MCP_VERSION,
                "serverInfo", map("name",SERVER_NAME,"version",SERVER_VERSION,
                                  "category",CATEGORY,"architecture",ARCH),
                "capabilities", map("tools", map())));
            case "tools/list": return handleList(id);
            case "tools/call": return handleCall(id, req);
            case "ping":       return okResp(id, map("status","pong","server",SERVER_NAME));
            default:           return errResp(id, -32601, "Method not found: " + method);
        }
    }

    private static String handleList(Object id) {
        List<Map<String,Object>> list = TOOLS.keySet().stream().map(name -> {
            Map<String,Object> s = schema(name);
            Map<String,Object> t = new LinkedHashMap<>();
            t.put("name",name); t.put("description",s.get("description")); t.put("inputSchema",s.get("inputSchema"));
            return t;
        }).collect(Collectors.toList());
        return okResp(id, map("tools", list));
    }

    @SuppressWarnings("unchecked")
    private static String handleCall(Object id, Map<String,Object> req) {
        Map<String,Object> p = (Map<String,Object>) req.getOrDefault("params", map());
        String name          = (String) p.get("name");
        Map<String,Object> a = (Map<String,Object>) p.getOrDefault("arguments", map());
        ToolHandler h = TOOLS.get(name);
        if (h == null) return errResp(id, -32602, "Unknown tool: " + name);
        try {
            return okResp(id, map("content",
                list(map("type","text","text", JsonSerializer.toJson(h.handle(a))))));
        } catch (Exception ex) {
            return errResp(id, -32603, "Execution error: " + ex.getMessage());
        }
    }

    // ── schema registry ──────────────────────────────────────────────────
    private static Map<String,Object> schema(String n) {
        switch (n) {
            case "team_compatibility_gnn":
                return s("Analyses team compatibility using a Graph Neural Network over member interaction graphs.",
                    p(str_("team_id","Unique team identifier"),
                      num("member_count","Number of team members")),
                    r("team_id","member_count"));
            case "talent_retrieval_vector_engine":
                return s("Semantic vector search over talent pool to surface top-K matching candidates.",
                    p(str_("search_query","Natural language talent search query"),
                      num("top_k","Number of top candidates to return (default 5)")),
                    r("search_query","top_k"));
            case "workforce_gap_model":
                return s("Identifies critical skill gaps in an organisation department with remediation plan.",
                    p(str_("org_id","Organisation identifier"),
                      str_("department","Department name e.g. ENGINEERING"),
                      num("headcount","Current department headcount")),
                    r("org_id","department","headcount"));
            case "hiring_bias_detector":
                return s("Audits a hiring pipeline for gender, institution, and age bias across funnel stages.",
                    p(str_("pipeline_id","Hiring pipeline identifier"),
                      num("candidates_audited","Total candidates audited in the pipeline")),
                    r("pipeline_id","candidates_audited"));
            case "digital_twin_simulation":
                return s("Runs optimistic / baseline / pessimistic simulations on a digital twin of an entity.",
                    p(str_("entity_id","Entity ID — institute, team, or employee"),
                      str_("entity_type","INSTITUTE | TEAM | EMPLOYEE"),
                      num("horizon_months","Simulation horizon in months")),
                    r("entity_id","entity_type","horizon_months"));
            case "candidate_ranking_model":
                return s("Ranks candidates for a job opening using multi-dimensional composite scoring.",
                    p(str_("job_id","Job opening identifier"),
                      num("candidate_count","Number of candidates to rank")),
                    r("job_id","candidate_count"));
            case "student_distribution_model":
                return s("Models student performance, geographic, and demographic distribution for an institute.",
                    p(str_("institute_id","Institute identifier"),
                      str_("academic_year","Academic year e.g. 2024-25"),
                      num("total_students","Total enrolled students")),
                    r("institute_id","academic_year","total_students"));
            case "salary_prediction_model":
                return s("Predicts salary range for a candidate based on role, experience, and location.",
                    p(str_("candidate_id","Candidate identifier"),
                      str_("role","Target job role title"),
                      num("experience_years","Years of relevant experience"),
                      str_("location","City or region e.g. Bangalore, Mumbai")),
                    r("candidate_id","role","experience_years","location"));
            case "retention_probability_model":
                return s("Predicts 12-month employee retention probability and surfaces flight risk factors.",
                    p(str_("employee_id","Employee identifier")),
                    r("employee_id"));
            case "promotion_probability_model":
                return s("Calculates promotion probability and readiness gap analysis for an employee.",
                    p(str_("employee_id","Employee identifier"),
                      str_("current_level","Current job level e.g. L3, L4, SENIOR")),
                    r("employee_id","current_level"));
            case "placement_probability_model":
                return s("Predicts campus placement probability, expected package, and company fit for a student.",
                    p(str_("student_id","Student identifier"),
                      str_("graduation_year","Expected graduation year e.g. 2025")),
                    r("student_id","graduation_year"));
            case "performance_forecast_model":
                return s("Forecasts employee performance scores at 3, 6, and 12 month horizons.",
                    p(str_("employee_id","Employee identifier"),
                      num("current_score","Current performance score 0–100"),
                      num("forecast_months","Forecast horizon in months")),
                    r("employee_id","current_score","forecast_months"));
            case "interview_semantic_analyzer":
                return s("Semantically analyses an interview transcript for signal quality and hire recommendation.",
                    p(str_("interview_id","Interview session identifier"),
                      str_("candidate_id","Candidate identifier"),
                      str_("transcript_text","Full interview transcript text")),
                    r("interview_id","candidate_id","transcript_text"));
            case "institute_revenue_forecast":
                return s("Forecasts quarterly and annual revenue for an institute by growth drivers.",
                    p(str_("institute_id","Institute identifier"),
                      str_("forecast_year","Forecast year e.g. 2025"),
                      num("current_revenue_inr","Current annual revenue in INR")),
                    r("institute_id","forecast_year","current_revenue_inr"));
            case "institute_ranking_model":
                return s("Computes composite ranking score for an institute across 7 dimensions.",
                    p(str_("institute_id","Institute identifier"),
                      str_("ranking_scope","CITY | STATE | NATIONAL | GLOBAL")),
                    r("institute_id","ranking_scope"));
            case "institute_performance_predictor":
                return s("Predicts institute performance index at 6 and 12 month horizons.",
                    p(str_("institute_id","Institute identifier"),
                      num("current_index","Current performance index 0–100"),
                      num("horizon_months","Prediction horizon in months")),
                    r("institute_id","current_index","horizon_months"));
            case "curriculum_gap_detector":
                return s("Detects curriculum gaps vs current industry demand by topic coverage analysis.",
                    p(str_("institute_id","Institute identifier"),
                      str_("program","Program name e.g. B.Tech CSE, MBA")),
                    r("institute_id","program"));
            case "class_intelligence_aggregator":
                return s("Aggregates multi-intelligence scores across an entire class cohort.",
                    p(str_("class_id","Class or cohort identifier"),
                      str_("institute_id","Institute identifier"),
                      num("student_count","Number of students in the class")),
                    r("class_id","institute_id","student_count"));
            case "candidate_summary_generator":
                return s("Generates an AI executive summary for a candidate suitable for hiring panels.",
                    p(str_("candidate_id","Candidate identifier")),
                    r("candidate_id"));
            case "benchmark_comparison_engine":
                return s("Benchmarks an entity against industry or peer peers across multiple dimensions.",
                    p(str_("entity_id","Entity identifier — institute, team, or org"),
                      str_("entity_type","INSTITUTE | TEAM | ORGANISATION"),
                      str_("benchmark_scope","CITY | NATIONAL | INDUSTRY")),
                    r("entity_id","entity_type","benchmark_scope"));
            default:
                return map("description","Unknown tool",
                           "inputSchema",map("type","object","properties",map()));
        }
    }

    // ── tiny schema DSL ──────────────────────────────────────────────────
    private static Map<String,Object> s(String d, Map<String,Object> props, List<String> req) {
        return map("description",d,"inputSchema",map("type","object","properties",props,"required",req));
    }
    @SafeVarargs
    private static Map<String,Object> p(Map.Entry<String,Object>... es) {
        Map<String,Object> m = new LinkedHashMap<>();
        for (var e : es) m.put(e.getKey(), e.getValue()); return m;
    }
    private static List<String> r(String... ks) { return Arrays.asList(ks); }
    private static Map.Entry<String,Object> str_(String k,String d) { return e(k, Map.of("type","string","description",d)); }
    private static Map.Entry<String,Object> num(String k,String d)  { return e(k, Map.of("type","number","description",d)); }
    private static Map.Entry<String,Object> e(String k,Object v)    { return new AbstractMap.SimpleEntry<>(k,v); }

    // ── map / list builders ──────────────────────────────────────────────
    static Map<String,Object> map(Object... kv) {
        Map<String,Object> m = new LinkedHashMap<>();
        for (int i = 0; i < kv.length - 1; i += 2) m.put(kv[i].toString(), kv[i+1]);
        return m;
    }
    @SafeVarargs
    static <T> List<T> list(T... items) { return new ArrayList<>(Arrays.asList(items)); }

    // ── arg helpers ──────────────────────────────────────────────────────
    static String str(Map<String,Object> a, String k) {
        Object v = a.get(k); return v != null ? v.toString() : "";
    }
    static double dbl(Map<String,Object> a, String k) {
        Object v = a.get(k);
        if (v == null) return 0.0;
        if (v instanceof Number) return ((Number) v).doubleValue();
        try { return Double.parseDouble(v.toString()); } catch (Exception e2) { return 0.0; }
    }

    // ── result wrapper ───────────────────────────────────────────────────
    private static Map<String,Object> result(String agent, Map<String,Object> data) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("agent", agent);      m.put("status", "success");
        m.put("architecture", ARCH); m.put("category", CATEGORY);
        m.put("timestamp", System.currentTimeMillis());
        m.putAll(data);
        return m;
    }

    // ── JSON-RPC envelopes ───────────────────────────────────────────────
    static String okResp(Object id, Object r) {
        return "{\"jsonrpc\":\"2.0\",\"id\":" + JsonSerializer.toJson(id)
             + ",\"result\":"                  + JsonSerializer.toJson(r) + "}";
    }
    static String errResp(Object id, int code, String msg) {
        return "{\"jsonrpc\":\"2.0\",\"id\":" + JsonSerializer.toJson(id)
             + ",\"error\":{\"code\":"         + code
             + ",\"message\":"                + JsonSerializer.toJson(msg) + "}}";
    }

    @FunctionalInterface
    interface ToolHandler { Object handle(Map<String,Object> args); }
}
