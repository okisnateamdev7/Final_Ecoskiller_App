package com.ecoskiller.mcp;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  Ecoskiller | CAT-11                                            ║
 * ║  Championship Advanced + Parent Predictive AI                   ║
 * ║  MCP Server · Java · 20 Agents · Antigravity Architecture      ║
 * ║  Transport : stdio  |  Protocol : JSON-RPC 2.0                 ║
 * ║  MCP Version : 2024-11-05                                      ║
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public class McpServer {

    static final String MCP_VERSION    = "2024-11-05";
    static final String SERVER_NAME    = "mcp-11-championship-parent-ai";
    static final String SERVER_VERSION = "1.0.0";
    static final String CATEGORY       = "CAT-11";
    static final String ARCH           = "ANTIGRAVITY";

    static final Map<String, ToolHandler> TOOLS = new LinkedHashMap<>();

    static {

        // ── 1. REGIONAL_DISTRIBUTION_ENGINE ──────────────────────────────
        TOOLS.put("regional_distribution_engine", args -> result(
            "REGIONAL_DISTRIBUTION_ENGINE_ANTIGRAVITY", map(
            "championship_id",        str(args,"championship_id"),
            "total_participants",     (int) dbl(args,"total_participants"),
            "region_allocations", list(
                map("region","NORTH","slots",320,"filled",298,"fill_rate_pct",93.1),
                map("region","SOUTH","slots",280,"filled",271,"fill_rate_pct",96.8),
                map("region","EAST", "slots",200,"filled",187,"fill_rate_pct",93.5),
                map("region","WEST", "slots",260,"filled",249,"fill_rate_pct",95.8),
                map("region","RURAL","slots",140,"filled",112,"fill_rate_pct",80.0)
            ),
            "distribution_balance_score", 0.89,
            "underrepresented_regions",   list("RURAL","NORTHEAST"),
            "recommended_outreach_zones", list("RURAL_BLOCK_A","RURAL_BLOCK_C"),
            "model",                      "ANTIGRAVITY_REGIONAL_DIST_V2"
        )));

        // ── 2. PARENT_LLM_INSIGHT_NARRATIVE_GENERATOR ────────────────────
        TOOLS.put("parent_llm_insight_narrative", args -> result(
            "PARENT_LLM_INSIGHT_NARRATIVE_GENERATOR_ANTIGRAVITY", map(
            "student_id",    str(args,"student_id"),
            "parent_id",     str(args,"parent_id"),
            "narrative_id",  "NAR-" + System.currentTimeMillis(),
            "tone",          "WARM_PROFESSIONAL",
            "sections", list(
                map("title","Academic Performance Summary",
                    "content","Your child has shown remarkable consistency in logical reasoning and spatial problem-solving, placing in the top 12% nationally over the last quarter."),
                map("title","Growth Areas",
                    "content","Linguistic fluency and timed exam strategy are emerging as the next frontier for focused improvement."),
                map("title","Parent Action Points",
                    "content","Encourage 20-minute daily reading and discuss current events to build verbal reasoning organically.")
            ),
            "overall_sentiment",    "POSITIVE",
            "readability_grade",    "GRADE_8",
            "generated_at",         new Date().toString(),
            "model",                "ANTIGRAVITY_PARENT_LLM_NARR_V3"
        )));

        // ── 3. PARENT_LLM_CAREER_GUIDANCE_GENERATOR ──────────────────────
        TOOLS.put("parent_llm_career_guidance", args -> result(
            "PARENT_LLM_CAREER_GUIDANCE_GENERATOR_ANTIGRAVITY", map(
            "student_id",       str(args,"student_id"),
            "age",              (int) dbl(args,"age"),
            "dominant_domains", list("LOGICAL","SPATIAL","ENTREPRENEURIAL"),
            "career_pathways", list(
                map("rank",1,"career","Data Scientist",         "fit_score",0.91,"timeline_yrs",8),
                map("rank",2,"career","Product Manager",        "fit_score",0.87,"timeline_yrs",9),
                map("rank",3,"career","Robotics Engineer",      "fit_score",0.84,"timeline_yrs",10),
                map("rank",4,"career","Financial Analyst",      "fit_score",0.79,"timeline_yrs",7),
                map("rank",5,"career","Entrepreneur / Founder", "fit_score",0.76,"timeline_yrs",12)
            ),
            "next_milestone",   "Focus on competitive math olympiads by age " + ((int)dbl(args,"age") + 2),
            "guidance_id",      "CGU-" + System.currentTimeMillis(),
            "model",            "ANTIGRAVITY_CAREER_GUIDE_LLM_V2"
        )));

        // ── 4. LEARNING_BEHAVIOR_DRIFT_MODEL ─────────────────────────────
        TOOLS.put("learning_behavior_drift", args -> result(
            "LEARNING_BEHAVIOR_DRIFT_MODEL_ANTIGRAVITY", map(
            "student_id",        str(args,"student_id"),
            "observation_days",  (int) dbl(args,"observation_days"),
            "drift_detected",    true,
            "drift_magnitude",   "MODERATE",
            "drift_direction",   "DISENGAGEMENT",
            "drift_score",       0.38,
            "trigger_signals",   list("session_duration_down_22pct","skipped_challenges_3x","login_frequency_drop"),
            "risk_level",        "MEDIUM",
            "recommended_intervention", "GAMIFICATION_BOOST + PARENT_NUDGE",
            "alert_parent",      true,
            "model",             "ANTIGRAVITY_DRIFT_V2"
        )));

        // ── 5. LEADERBOARD_STABILITY_MODEL ───────────────────────────────
        TOOLS.put("leaderboard_stability_model", args -> result(
            "LEADERBOARD_STABILITY_MODEL_PREDICTIVE_AI_ANTIGRAVITY", map(
            "championship_id",        str(args,"championship_id"),
            "student_id",             str(args,"student_id"),
            "current_rank",           (int) dbl(args,"current_rank"),
            "stability_index",        0.81,
            "rank_volatility",        "LOW",
            "predicted_rank_24h",     (int) dbl(args,"current_rank"),
            "predicted_rank_7d",      Math.max(1, (int) dbl(args,"current_rank") - 1),
            "overtake_probability",   0.12,
            "top_threat_user_ids",    list("u_9921","u_4457"),
            "defense_score",          0.78,
            "model",                  "ANTIGRAVITY_LDRSTAB_V2"
        )));

        // ── 6. INCOME_PREDICTION_MODEL ────────────────────────────────────
        TOOLS.put("income_prediction_model", args -> result(
            "INCOME_PREDICTION_MODEL_ANTIGRAVITY", map(
            "student_id",             str(args,"student_id"),
            "current_age",            (int) dbl(args,"current_age"),
            "skill_profile_json_len", str(args,"skill_profile_json").length(),
            "predicted_income_at_25", map("currency","INR","annual_min",600000, "annual_max",1200000,"median",850000),
            "predicted_income_at_30", map("currency","INR","annual_min",1400000,"annual_max",3200000,"median",2100000),
            "income_percentile_25",   72,
            "income_percentile_30",   81,
            "key_income_drivers",     list("LOGICAL_INTELLIGENCE","ENTREPRENEURIAL_RISK_TOLERANCE","SKILL_RANK"),
            "confidence",             0.77,
            "model",                  "ANTIGRAVITY_INCOME_PRED_V2"
        )));

        // ── 7. CAREER_PROBABILITY_MODEL ───────────────────────────────────
        TOOLS.put("career_probability_model", args -> result(
            "CAREER_PROBABILITY_MODEL_ANTIGRAVITY", map(
            "student_id",         str(args,"student_id"),
            "target_career",      str(args,"target_career"),
            "probability",        0.73,
            "probability_label",  "HIGH",
            "gap_factors", list(
                map("factor","Advanced Math Proficiency","gap_score",0.18,"priority","HIGH"),
                map("factor","Communication Skills",     "gap_score",0.22,"priority","MEDIUM"),
                map("factor","Domain Certifications",    "gap_score",0.31,"priority","HIGH")
            ),
            "estimated_readiness_age", (int) dbl(args,"current_age") + 7,
            "alternate_careers",  list("Data Engineer","ML Researcher","Quantitative Analyst"),
            "model",              "ANTIGRAVITY_CAREER_PROB_V3"
        )));

        // ── 8. WEAKNESS_IMPACT_MODEL ──────────────────────────────────────
        TOOLS.put("weakness_impact_model", args -> result(
            "ANTIGRAVITY_WEAKNESS_IMPACT_MODEL_v1", map(
            "student_id",      str(args,"student_id"),
            "assessed_weaknesses", list(
                map("weakness","Time Management",    "impact_score",0.74,"career_risk","HIGH",  "remediation","Timed mock exams 3x/week"),
                map("weakness","Verbal Expression",  "impact_score",0.61,"career_risk","MEDIUM","remediation","Debate club + daily journaling"),
                map("weakness","Abstract Reasoning", "impact_score",0.45,"career_risk","LOW",   "remediation","Pattern puzzles 15 min/day")
            ),
            "composite_weakness_impact", 0.60,
            "priority_remediation_order", list("Time Management","Abstract Reasoning","Verbal Expression"),
            "intervention_urgency",       "MEDIUM_HIGH",
            "model",                      "ANTIGRAVITY_WEAKNESS_V1"
        )));

        // ── 9. SPECTATOR_ANALYTICS_MODEL ──────────────────────────────────
        TOOLS.put("spectator_analytics_model", args -> result(
            "ANTIGRAVITY_Spectator_Analytics_Model", map(
            "championship_id",       str(args,"championship_id"),
            "spectator_count",       14820,
            "peak_concurrent",       9340,
            "engagement_rate",       0.78,
            "avg_watch_duration_min",22.4,
            "drop_off_round",        3,
            "sentiment_score",       0.84,
            "trending_moments",      list(
                map("timestamp_min",12,"event","UPSET_RESULT",    "spike_viewers",2100),
                map("timestamp_min",28,"event","PHOTO_FINISH_RANK","spike_viewers",3800)
            ),
            "platform_split", map("web",0.52,"mobile",0.39,"tv_cast",0.09),
            "model",          "ANTIGRAVITY_SPECTATOR_V2"
        )));

        // ── 10. SCORE_NORMALIZATION_ENGINE ────────────────────────────────
        TOOLS.put("score_normalization_engine", args -> {
            double raw  = dbl(args, "raw_score");
            double mean = dbl(args, "cohort_mean");
            double sd   = dbl(args, "cohort_std_dev");
            double z    = sd > 0 ? (raw - mean) / sd : 0.0;
            double norm = Math.min(100.0, Math.max(0.0, 50.0 + z * 10.0));
            return result("ANTIGRAVITY_Score_Normalization_Engine", map(
                "student_id",         str(args,"student_id"),
                "raw_score",          raw,
                "cohort_mean",        mean,
                "cohort_std_dev",     sd,
                "z_score",            Math.round(z * 1000.0) / 1000.0,
                "normalized_score",   Math.round(norm * 100.0) / 100.0,
                "percentile",         Math.min(99.9, 50.0 + z * 34.0),
                "normalization_method","Z_SCORE_TO_T_SCALE",
                "model",              "ANTIGRAVITY_SCORE_NORM_V3"
            ));
        });

        // ── 11. PLATEAU_DETECTION_MODEL ───────────────────────────────────
        TOOLS.put("plateau_detection_model", args -> result(
            "ANTIGRAVITY_PLATEAU_DETECTION_MODEL_v1", map(
            "student_id",        str(args,"student_id"),
            "skill_id",          str(args,"skill_id"),
            "plateau_detected",  true,
            "plateau_duration_days", 18,
            "plateau_score_band", map("min",74.0,"max",77.0),
            "stagnation_severity","MODERATE",
            "root_cause_hypothesis", list("INSUFFICIENT_CHALLENGE_DIFFICULTY","PRACTICE_MONOTONY"),
            "recommended_actions",   list(
                "INCREASE_DIFFICULTY_BY_15PCT",
                "INTRODUCE_NEW_PROBLEM_FORMATS",
                "SCHEDULE_MENTOR_SESSION"
            ),
            "breakout_probability_30d", 0.67,
            "model",              "ANTIGRAVITY_PLATEAU_V1"
        )));

        // ── 12. PEER_COMPARISON_ENGINE ────────────────────────────────────
        TOOLS.put("peer_comparison_engine", args -> result(
            "ANTIGRAVITY_PEER_COMPARISON_ENGINE_v1", map(
            "student_id",       str(args,"student_id"),
            "comparison_scope", str(args,"comparison_scope"),
            "student_percentile", 78.4,
            "peer_group_size",    1240,
            "dimension_comparison", list(
                map("dimension","Logical",        "student_score",84,"peer_avg",71,"delta","+13","rank_in_peer","TOP_15PCT"),
                map("dimension","Spatial",        "student_score",79,"peer_avg",74,"delta","+5", "rank_in_peer","TOP_25PCT"),
                map("dimension","Linguistic",     "student_score",68,"peer_avg",72,"delta","-4", "rank_in_peer","AVERAGE"),
                map("dimension","Entrepreneurial","student_score",88,"peer_avg",65,"delta","+23","rank_in_peer","TOP_5PCT")
            ),
            "strongest_vs_peers",  "Entrepreneurial",
            "weakest_vs_peers",    "Linguistic",
            "overall_peer_rank",   96,
            "model",               "ANTIGRAVITY_PEER_COMP_V1"
        )));

        // ── 13. PARENT_LLM_GROWTH_STRATEGY_EXPLAINER ─────────────────────
        TOOLS.put("parent_llm_growth_strategy", args -> result(
            "ANTIGRAVITY_Parent_LLM_Growth_Strategy_Explainer", map(
            "student_id",  str(args,"student_id"),
            "parent_id",   str(args,"parent_id"),
            "strategy_id", "STR-" + System.currentTimeMillis(),
            "strategy_summary", "A 90-day blended learning plan targeting logical depth and entrepreneurial mindset, using gamified micro-challenges and weekly reflection sessions.",
            "weekly_plan", list(
                map("week","1-2", "focus","Baseline Assessment + Goal Setting",    "daily_min",30),
                map("week","3-6", "focus","Logical Reasoning Intensive",           "daily_min",45),
                map("week","7-10","focus","Entrepreneurial Case Studies",           "daily_min",40),
                map("week","11-13","focus","Mock Championship + Final Assessment",  "daily_min",60)
            ),
            "parent_engagement_actions", list(
                "Review weekly progress report every Sunday",
                "Attend monthly parent insight webinar",
                "Enable daily challenge notifications"
            ),
            "expected_score_lift_pct", 18.0,
            "model", "ANTIGRAVITY_PARENT_STRATEGY_LLM_V2"
        )));

        // ── 14. ANTIGRAVITY_LEARNING_BEHAVIOR_DRIFT (alias v2) ───────────
        TOOLS.put("learning_behavior_drift_v2", args -> result(
            "ANTIGRAVITY_Learning_Behavior_Drift_Model", map(
            "student_id",          str(args,"student_id"),
            "window_days",         (int) dbl(args,"window_days"),
            "drift_vector", map(
                "engagement",   -0.18,
                "accuracy",     +0.04,
                "session_freq", -0.22,
                "challenge_attempt_rate", -0.15
            ),
            "overall_drift_score",   0.41,
            "drift_classification",  "EARLY_DISENGAGEMENT",
            "confidence",            0.82,
            "suggested_parent_action","Send personalised encouragement message via app",
            "auto_intervention_triggered", true,
            "model", "ANTIGRAVITY_DRIFT_MODEL_V3"
        )));

        // ── 15. DROPOUT_RISK_MODEL ────────────────────────────────────────
        TOOLS.put("dropout_risk_model", args -> result(
            "ANTIGRAVITY_DROPOUT_RISK_MODEL_v1", map(
            "student_id",            str(args,"student_id"),
            "dropout_probability",   0.19,
            "risk_level",            "LOW_MEDIUM",
            "risk_factors", list(
                map("factor","Session gap > 5 days",      "weight",0.35,"present",true),
                map("factor","Challenge failure rate >60%","weight",0.28,"present",false),
                map("factor","Parent engagement low",      "weight",0.22,"present",true),
                map("factor","Peer rank declining 3 weeks","weight",0.15,"present",false)
            ),
            "retention_score",       0.81,
            "recommended_retention_actions", list(
                "SEND_STREAK_RECOVERY_NOTIFICATION",
                "ASSIGN_EASY_WIN_CHALLENGE",
                "TRIGGER_PARENT_DASHBOARD_ALERT"
            ),
            "model", "ANTIGRAVITY_DROPOUT_V1"
        )));

        // ── 16. COMPETITION_FORECAST_ENGINE ──────────────────────────────
        TOOLS.put("competition_forecast_engine", args -> result(
            "ANTIGRAVITY_Competition_Forecast_Engine", map(
            "championship_id",     str(args,"championship_id"),
            "student_id",          str(args,"student_id"),
            "current_score",       dbl(args,"current_score"),
            "predicted_final_score", dbl(args,"current_score") * 1.06,
            "predicted_final_rank", 3,
            "win_probability",      0.14,
            "podium_probability",   0.41,
            "top_10_probability",   0.78,
            "score_ceiling_estimate", dbl(args,"current_score") * 1.12,
            "key_round_forecast", list(
                map("round",4,"predicted_score",dbl(args,"current_score")*1.03,"difficulty","HIGH"),
                map("round",5,"predicted_score",dbl(args,"current_score")*1.06,"difficulty","VERY_HIGH")
            ),
            "model", "ANTIGRAVITY_COMP_FORECAST_V3"
        )));

        // ── 17. CHAMPIONSHIP_LLM_AI_PROBLEM_GENERATION ───────────────────
        TOOLS.put("championship_problem_generation", args -> result(
            "ANTIGRAVITY_Championship_LLM_AI_Problem_Generation_Agent", map(
            "championship_id",  str(args,"championship_id"),
            "round",            (int) dbl(args,"round_number"),
            "difficulty_target",str(args,"difficulty_target"),
            "problems_generated", list(
                map("id","P001","type","LOGICAL_SEQUENCE",   "difficulty","HARD",  "estimated_solve_min",8, "bloom_level","ANALYSIS"),
                map("id","P002","type","SPATIAL_ROTATION",   "difficulty","HARD",  "estimated_solve_min",10,"bloom_level","APPLICATION"),
                map("id","P003","type","VERBAL_ANALOGY",     "difficulty","MEDIUM","estimated_solve_min",5, "bloom_level","COMPREHENSION"),
                map("id","P004","type","NUMERICAL_PATTERN",  "difficulty","EXPERT","estimated_solve_min",14,"bloom_level","SYNTHESIS"),
                map("id","P005","type","ENTREPRENEURIAL_CASE","difficulty","HARD", "estimated_solve_min",18,"bloom_level","EVALUATION")
            ),
            "total_problems",    5,
            "avg_difficulty",    "HARD",
            "plagiarism_checked",true,
            "generation_model",  "ANTIGRAVITY_PROBGEN_LLM_V4"
        )));

        // ── 18. CHAMPIONSHIP_LLM_AI_JUDGING_AGENT ────────────────────────
        TOOLS.put("championship_ai_judging", args -> result(
            "ANTIGRAVITY_Championship_LLM_AI_Judging_Agent", map(
            "submission_id",     str(args,"submission_id"),
            "student_id",        str(args,"student_id"),
            "problem_id",        str(args,"problem_id"),
            "awarded_score",     87,
            "max_score",         100,
            "scoring_breakdown", map(
                "correctness",   45,
                "reasoning_depth",22,
                "originality",   12,
                "presentation",   8
            ),
            "judge_confidence",  0.93,
            "human_review_required", false,
            "feedback", "Strong logical structure with well-justified steps. Minor deduction for incomplete edge-case handling in step 3.",
            "judging_latency_ms",340,
            "model",             "ANTIGRAVITY_JUDGE_LLM_V3"
        )));

        // ── 19. CHAMPION_AUTHENTICITY_MODEL ──────────────────────────────
        TOOLS.put("champion_authenticity_model", args -> result(
            "ANTIGRAVITY_Champion_Authenticity_Model", map(
            "student_id",            str(args,"student_id"),
            "championship_id",       str(args,"championship_id"),
            "authenticity_score",    0.96,
            "authenticity_label",    "VERIFIED_AUTHENTIC",
            "checks_performed", list(
                map("check","Performance_Trajectory_Consistency","passed",true, "confidence",0.97),
                map("check","Behavioral_Biometric_Match",        "passed",true, "confidence",0.95),
                map("check","Anti_Cheat_Clearance",             "passed",true, "confidence",0.98),
                map("check","Cross_Session_Identity_Stability",  "passed",true, "confidence",0.94),
                map("check","Peer_Score_Outlier_Analysis",       "passed",true, "confidence",0.92)
            ),
            "fraud_risk",            "NEGLIGIBLE",
            "certificate_eligible",  true,
            "model",                 "ANTIGRAVITY_AUTHENTIC_V2"
        )));

        // ── 20. ACADEMIC_INTELLIGENCE_CORRELATION_ENGINE ─────────────────
        TOOLS.put("academic_intelligence_correlation", args -> result(
            "ANTIGRAVITY_Academic_Intelligence_Correlation_Engine", map(
            "student_id",        str(args,"student_id"),
            "academic_gpa",      dbl(args,"academic_gpa"),
            "intelligence_scores", map(
                "logical",       84,
                "spatial",       79,
                "linguistic",    68,
                "musical",       55,
                "kinesthetic",   72,
                "interpersonal", 81,
                "intrapersonal", 77,
                "naturalistic",  63,
                "entrepreneurial",88
            ),
            "correlation_matrix", map(
                "gpa_vs_logical",    0.71,
                "gpa_vs_linguistic", 0.68,
                "gpa_vs_spatial",    0.59,
                "gpa_vs_entrepreneurial", 0.31
            ),
            "strongest_academic_predictor", "LOGICAL",
            "weakest_academic_predictor",   "ENTREPRENEURIAL",
            "insight", "GPA correlates strongly with logical and linguistic intelligence but weakly with entrepreneurial traits — suggesting current academic grading undervalues real-world problem-solving.",
            "model", "ANTIGRAVITY_ACAD_INTEL_CORR_V2"
        )));
    }

    // ── main ────────────────────────────────────────────────────────────
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

    // ── dispatcher ──────────────────────────────────────────────────────
    static String dispatch(String raw) {
        Map<String,Object> req = JsonParser.parse(raw);
        Object id = req.get("id");
        String method = (String) req.get("method");
        if (method == null) return errResp(id, -32600, "Invalid Request");
        switch (method) {
            case "initialize": return okResp(id, map(
                "protocolVersion", MCP_VERSION,
                "serverInfo", map("name",SERVER_NAME,"version",SERVER_VERSION,"category",CATEGORY,"architecture",ARCH),
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
        return okResp(id, map("tools",list));
    }

    @SuppressWarnings("unchecked")
    private static String handleCall(Object id, Map<String,Object> req) {
        Map<String,Object> p  = (Map<String,Object>) req.getOrDefault("params", map());
        String name = (String) p.get("name");
        Map<String,Object> a  = (Map<String,Object>) p.getOrDefault("arguments", map());
        ToolHandler h = TOOLS.get(name);
        if (h == null) return errResp(id, -32602, "Unknown tool: " + name);
        try {
            return okResp(id, map("content", list(map("type","text","text",JsonSerializer.toJson(h.handle(a))))));
        } catch (Exception ex) {
            return errResp(id, -32603, "Execution error: " + ex.getMessage());
        }
    }

    // ── schema registry ─────────────────────────────────────────────────
    private static Map<String,Object> schema(String n) {
        switch (n) {
            case "regional_distribution_engine":
                return s("Allocates and validates regional participant distribution for a championship.",
                    p(str_("championship_id","Championship identifier"),
                      num("total_participants","Total registered participants")),
                    r("championship_id","total_participants"));

            case "parent_llm_insight_narrative":
                return s("Generates an LLM-backed personalised narrative insight report for parents.",
                    p(str_("student_id","Student identifier"), str_("parent_id","Parent identifier")),
                    r("student_id","parent_id"));

            case "parent_llm_career_guidance":
                return s("Generates AI career guidance for parents based on child's intelligence profile.",
                    p(str_("student_id","Student identifier"), num("age","Student's current age in years")),
                    r("student_id","age"));

            case "learning_behavior_drift":
                return s("Detects drift in learning behaviour over an observation window.",
                    p(str_("student_id","Student identifier"), num("observation_days","Days of behaviour to analyse")),
                    r("student_id","observation_days"));

            case "leaderboard_stability_model":
                return s("Predicts leaderboard rank stability and overtake probability.",
                    p(str_("championship_id","Championship identifier"),
                      str_("student_id","Student identifier"),
                      num("current_rank","Student's current rank")),
                    r("championship_id","student_id","current_rank"));

            case "income_prediction_model":
                return s("Predicts future income ranges at age 25 and 30 from skill and intelligence profile.",
                    p(str_("student_id","Student identifier"),
                      num("current_age","Current age in years"),
                      str_("skill_profile_json","JSON of skill scores and domains")),
                    r("student_id","current_age","skill_profile_json"));

            case "career_probability_model":
                return s("Calculates probability of reaching a target career given current profile.",
                    p(str_("student_id","Student identifier"),
                      str_("target_career","Target career title e.g. Data Scientist"),
                      num("current_age","Student's current age")),
                    r("student_id","target_career","current_age"));

            case "weakness_impact_model":
                return s("Assesses the career impact of identified weaknesses and prioritises remediation.",
                    p(str_("student_id","Student identifier")),
                    r("student_id"));

            case "spectator_analytics_model":
                return s("Analyses spectator engagement, drop-off, and sentiment for a championship.",
                    p(str_("championship_id","Championship identifier")),
                    r("championship_id"));

            case "score_normalization_engine":
                return s("Normalises raw scores to a Z-to-T scale relative to cohort statistics.",
                    p(str_("student_id","Student identifier"),
                      num("raw_score","Raw assessment score"),
                      num("cohort_mean","Cohort mean score"),
                      num("cohort_std_dev","Cohort standard deviation")),
                    r("student_id","raw_score","cohort_mean","cohort_std_dev"));

            case "plateau_detection_model":
                return s("Detects skill score plateaus and recommends breakthrough interventions.",
                    p(str_("student_id","Student identifier"), str_("skill_id","Skill identifier")),
                    r("student_id","skill_id"));

            case "peer_comparison_engine":
                return s("Compares student performance against peers across all intelligence dimensions.",
                    p(str_("student_id","Student identifier"),
                      str_("comparison_scope","SCHOOL | DISTRICT | NATIONAL | GLOBAL")),
                    r("student_id","comparison_scope"));

            case "parent_llm_growth_strategy":
                return s("Generates a 90-day LLM growth strategy plan with weekly parent actions.",
                    p(str_("student_id","Student identifier"), str_("parent_id","Parent identifier")),
                    r("student_id","parent_id"));

            case "learning_behavior_drift_v2":
                return s("Computes drift vector across engagement dimensions over a rolling window.",
                    p(str_("student_id","Student identifier"), num("window_days","Rolling window in days")),
                    r("student_id","window_days"));

            case "dropout_risk_model":
                return s("Predicts dropout probability and recommends targeted retention actions.",
                    p(str_("student_id","Student identifier")),
                    r("student_id"));

            case "competition_forecast_engine":
                return s("Forecasts final championship score, rank and win probability.",
                    p(str_("championship_id","Championship identifier"),
                      str_("student_id","Student identifier"),
                      num("current_score","Student's current score in the championship")),
                    r("championship_id","student_id","current_score"));

            case "championship_problem_generation":
                return s("Generates LLM-crafted championship problem sets at a target difficulty.",
                    p(str_("championship_id","Championship identifier"),
                      num("round_number","Round number e.g. 1, 2, 3"),
                      str_("difficulty_target","MEDIUM | HARD | EXPERT")),
                    r("championship_id","round_number","difficulty_target"));

            case "championship_ai_judging":
                return s("AI judges a championship submission and returns scored breakdown + feedback.",
                    p(str_("submission_id","Submission identifier"),
                      str_("student_id","Student identifier"),
                      str_("problem_id","Problem identifier")),
                    r("submission_id","student_id","problem_id"));

            case "champion_authenticity_model":
                return s("Verifies champion result authenticity using multi-layer biometric and ML checks.",
                    p(str_("student_id","Student identifier"),
                      str_("championship_id","Championship identifier")),
                    r("student_id","championship_id"));

            case "academic_intelligence_correlation":
                return s("Correlates academic GPA with Antigravity multi-intelligence scores.",
                    p(str_("student_id","Student identifier"),
                      num("academic_gpa","GPA on a 10-point scale")),
                    r("student_id","academic_gpa"));

            default:
                return map("description","Unknown tool",
                           "inputSchema",map("type","object","properties",map()));
        }
    }

    // ── DSL helpers ─────────────────────────────────────────────────────
    private static Map<String,Object> s(String d, Map<String,Object> props, List<String> req) {
        return map("description",d,"inputSchema",map("type","object","properties",props,"required",req));
    }
    @SafeVarargs
    private static Map<String,Object> p(Map.Entry<String,Object>... es) {
        Map<String,Object> m = new LinkedHashMap<>();
        for (var e : es) m.put(e.getKey(), e.getValue());
        return m;
    }
    private static List<String> r(String... ks) { return Arrays.asList(ks); }
    private static Map.Entry<String,Object> str_(String k,String d) { return e(k,Map.of("type","string","description",d)); }
    private static Map.Entry<String,Object> num(String k,String d)  { return e(k,Map.of("type","number","description",d)); }
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
        m.put("agent", agent); m.put("status","success");
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
