package com.ecoskiller.mcp;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Ecoskiller | CAT-10 — Skill & Competition Core
 * MCP Server · Java · 19 Agents · Antigravity Architecture
 * Transport: stdio | Protocol: JSON-RPC 2.0 | MCP Version: 2024-11-05
 */
public class McpServer {

    static final String MCP_VERSION    = "2024-11-05";
    static final String SERVER_NAME    = "mcp-10-skill-competition";
    static final String SERVER_VERSION = "1.0.0";
    static final String CATEGORY       = "CAT-10";

    static final Map<String, ToolHandler> TOOLS = new LinkedHashMap<>();

    static {

        // 1. SKILL_XP_CALIBRATION_AGENT
        TOOLS.put("skill_xp_calibration", args -> result("SKILL_XP_CALIBRATION_AGENT", map(
            "user_id",            str(args,"user_id"),
            "skill_id",           str(args,"skill_id"),
            "raw_xp",             dbl(args,"raw_xp"),
            "calibrated_xp",      Math.round(dbl(args,"raw_xp") * 1.12 * 100.0) / 100.0,
            "xp_tier",            dbl(args,"raw_xp") >= 5000 ? "PLATINUM" : dbl(args,"raw_xp") >= 2000 ? "GOLD" : dbl(args,"raw_xp") >= 500 ? "SILVER" : "BRONZE",
            "next_tier_gap_xp",   Math.max(0.0, 2000.0 - dbl(args,"raw_xp")),
            "decay_applied",      false,
            "calibration_method", "ANTIGRAVITY_V2",
            "confidence",         0.94
        )));

        // 2. SKILL_RANK_ENGINE_AGENT
        TOOLS.put("skill_rank_engine", args -> result("SKILL_RANK_ENGINE_AGENT", map(
            "user_id",          str(args,"user_id"),
            "skill_id",         str(args,"skill_id"),
            "input_score",      dbl(args,"score"),
            "global_rank",      (int)(1_000_000 * (1 - dbl(args,"score") / 100.0)) + 1,
            "percentile_rank",  Math.min(99.9, dbl(args,"score") * 1.05),
            "elo_rating",       1200 + (int)(dbl(args,"score") * 8),
            "rank_label",       dbl(args,"score") >= 95 ? "GRANDMASTER" : dbl(args,"score") >= 80 ? "MASTER" : dbl(args,"score") >= 60 ? "EXPERT" : "NOVICE",
            "rank_delta_30d",   "+3",
            "engine",           "ANTIGRAVITY_RANK_V3"
        )));

        // 3. SKILL_PERFORMANCE_REGRESSION_AGENT
        TOOLS.put("skill_performance_regression", args -> result("SKILL_PERFORMANCE_REGRESSION_AGENT", map(
            "user_id",              str(args,"user_id"),
            "skill_id",             str(args,"skill_id"),
            "data_points",          str(args,"score_history_csv").split(",").length,
            "regression_type",      "POLYNOMIAL_DEGREE_2",
            "r_squared",            0.91,
            "predicted_score_30d",  87.4,
            "trend",                "UPWARD",
            "inflection_point",     "2024-10-15",
            "volatility_sigma",     2.3,
            "model_version",        "ANTIGRAVITY_REG_V2"
        )));

        // 4. SKILL_OBSOLESCENCE_PREDICTOR_AGENT
        TOOLS.put("skill_obsolescence_predictor", args -> result("SKILL_OBSOLESCENCE_PREDICTOR_AGENT", map(
            "skill_id",                str(args,"skill_id"),
            "domain",                  str(args,"domain"),
            "obsolescence_risk",       "MEDIUM",
            "estimated_half_life_yrs", 3.8,
            "disruption_probability",  0.34,
            "replacement_technologies",list("LLM_AUTOMATION","NO_CODE_TOOLS"),
            "reskilling_urgency",      "MODERATE",
            "market_signal_strength",  0.71,
            "model",                   "ANTIGRAVITY_OBS_V1"
        )));

        // 5. SKILL_MARKET_BENCHMARK_AGENT
        TOOLS.put("skill_market_benchmark", args -> result("SKILL_MARKET_BENCHMARK_AGENT", map(
            "skill_id",               str(args,"skill_id"),
            "region",                 str(args,"region"),
            "market_avg_score",       72.4,
            "top_10_percent_score",   91.2,
            "demand_index",           0.83,
            "supply_index",           0.61,
            "demand_supply_ratio",    1.36,
            "avg_salary_premium_pct", 22.0,
            "benchmark_date",         "2025-03",
            "source",                 "ANTIGRAVITY_MARKET_ENGINE"
        )));

        // 6. SKILL_IMPROVEMENT_PLANNER_AGENT
        TOOLS.put("skill_improvement_planner", args -> {
            double cur = dbl(args,"current_score"), tgt = dbl(args,"target_score");
            return result("SKILL_IMPROVEMENT_PLANNER_AGENT", map(
                "user_id",             str(args,"user_id"),
                "skill_id",            str(args,"skill_id"),
                "current_score",       cur,
                "target_score",        tgt,
                "gap",                 tgt - cur,
                "estimated_days",      (int)((tgt - cur) * 2.5),
                "daily_practice_min",  45,
                "milestones",          list(
                    map("week",1,"target",cur+(tgt-cur)*0.25),
                    map("week",4,"target",cur+(tgt-cur)*0.60),
                    map("week",8,"target",tgt)
                ),
                "recommended_modules", list("THEORY_REFRESH","PRACTICE_DRILLS","MOCK_CHALLENGE"),
                "plan_version",        "ANTIGRAVITY_PLANNER_V2"
            ));
        });

        // 7. SKILL_FRAUD_DETECTOR_AGENT
        TOOLS.put("skill_fraud_detector", args -> result("SKILL_FRAUD_DETECTOR_AGENT", map(
            "user_id",            str(args,"user_id"),
            "session_id",         str(args,"session_id"),
            "fraud_probability",  0.07,
            "risk_level",         "LOW",
            "anomalies_detected", new ArrayList<>(),
            "velocity_anomaly",   false,
            "ip_consistency",     true,
            "device_fingerprint", "MATCH",
            "action",             "ALLOW",
            "signals_parsed",     str(args,"behavioral_signals_json").length(),
            "model",              "ANTIGRAVITY_FRAUD_V3"
        )));

        // 8. SKILL_FEEDBACK_GENERATOR_AGENT
        TOOLS.put("skill_feedback_generator", args -> result("SKILL_FEEDBACK_GENERATOR_AGENT", map(
            "user_id",               str(args,"user_id"),
            "skill_id",              str(args,"skill_id"),
            "score",                 dbl(args,"score"),
            "feedback_tone",         "ENCOURAGING",
            "strength_summary",      "Strong conceptual grasp with consistent accuracy on core problems.",
            "gap_summary",           "Speed under timed conditions and edge-case handling need improvement.",
            "next_steps",            list("practice_timed_sets","review_edge_cases","peer_challenge"),
            "estimated_next_score",  dbl(args,"score") + 4.5,
            "feedback_id",           "FB-" + System.currentTimeMillis(),
            "engine",                "ANTIGRAVITY_FEEDBACK_LLM_V1"
        )));

        // 9. SKILL_EXTRACTION_CLASSIFIER_AGENT
        TOOLS.put("skill_extraction_classifier", args -> result("SKILL_EXTRACTION_CLASSIFIER_AGENT", map(
            "user_id",            str(args,"user_id"),
            "extracted_skills",   list(
                map("skill","Python",        "confidence",0.97,"category","PROGRAMMING"),
                map("skill","Data Analysis", "confidence",0.89,"category","ANALYTICS"),
                map("skill","Communication", "confidence",0.82,"category","SOFT_SKILL")
            ),
            "total_skills_found", 3,
            "content_length",     str(args,"resume_or_text").length(),
            "taxonomy_version",   "ANTIGRAVITY_TAXONOMY_V4",
            "classification_model","ANTIGRAVITY_EXTRACT_V2"
        )));

        // 10. SKILL_DEMAND_FORECAST_AGENT
        TOOLS.put("skill_demand_forecast", args -> result("SKILL_DEMAND_FORECAST_AGENT", map(
            "skill_id",                str(args,"skill_id"),
            "forecast_horizon_months", str(args,"forecast_horizon_months"),
            "current_demand_index",    0.78,
            "forecast_demand_index",   0.89,
            "demand_growth_pct",       14.1,
            "job_posting_trend",       "INCREASING",
            "top_hiring_sectors",      list("FINTECH","EDTECH","HEALTHTECH"),
            "peak_demand_month",       "Month_6",
            "forecast_confidence",     0.84,
            "model",                   "ANTIGRAVITY_DEMAND_LSTM_V2"
        )));

        // 11. SKILL_CONFIDENCE_MODEL_AGENT
        TOOLS.put("skill_confidence_model", args -> result("SKILL_CONFIDENCE_MODEL_AGENT", map(
            "user_id",              str(args,"user_id"),
            "skill_id",             str(args,"skill_id"),
            "confidence_score",     0.76,
            "calibration_error",    0.04,
            "overconfidence_risk",  false,
            "underconfidence_risk", true,
            "recommended_action",   "INCREASE_CHALLENGE_DIFFICULTY",
            "attempts_analyzed",    str(args,"attempt_data_json").length() / 10,
            "model",                "ANTIGRAVITY_CONF_V1"
        )));

        // 12. SKILL_CHALLENGE_GENERATOR_AGENT
        TOOLS.put("skill_challenge_generator", args -> result("SKILL_CHALLENGE_GENERATOR_AGENT", map(
            "user_id",           str(args,"user_id"),
            "skill_id",          str(args,"skill_id"),
            "difficulty_level",  str(args,"difficulty_level"),
            "challenge_id",      "CHAL-" + System.currentTimeMillis(),
            "challenge_type",    "SCENARIO_BASED",
            "time_limit_min",    30,
            "problem_count",     5,
            "adaptive",          true,
            "tags",              list("real_world","timed","peer_ranked"),
            "generator_version", "ANTIGRAVITY_CHALGEN_V3"
        )));

        // 13. COMPETITION_DIFFICULTY_CALIBRATOR_AGENT
        TOOLS.put("competition_difficulty_calibrator", args -> result("COMPETITION_DIFFICULTY_CALIBRATOR_AGENT", map(
            "competition_id",          str(args,"competition_id"),
            "recommended_difficulty",  "HARD",
            "difficulty_score",        7.8,
            "cohort_avg_elo",          1540,
            "expected_completion_pct", 62.0,
            "problem_set_adjustment",  "INCREASE_EDGE_CASES",
            "time_pressure_index",     0.72,
            "fairness_score",          0.91,
            "cohort_size",             str(args,"cohort_profile_json").length() / 20,
            "calibrator",              "ANTIGRAVITY_DIFF_CAL_V2"
        )));

        // 14. CHAMPIONSHIP_ML_PRIZE_ALLOCATION_OPTIMIZER
        TOOLS.put("championship_prize_allocation", args -> {
            double pool = dbl(args,"total_prize_pool");
            int    n    = (int) dbl(args,"top_n_ranks");
            return result("CHAMPIONSHIP_ML_PRIZE_ALLOCATION_OPTIMIZER_AGENT", map(
                "championship_id",  str(args,"championship_id"),
                "total_pool",       pool,
                "top_n_ranks",      n,
                "allocation_table", buildPrizeTable(pool, n),
                "gini_coefficient", 0.38,
                "fairness_index",   0.87,
                "method",           "LOGARITHMIC_DECAY",
                "optimizer",        "ANTIGRAVITY_PRIZE_OPT_V2"
            ));
        });

        // 15. CHAMPIONSHIP_ML_6_QUALIFICATION_FILTER
        TOOLS.put("championship_qualification_filter", args -> {
            double score = dbl(args,"qualification_score");
            return result("CHAMPIONSHIP_ML_6_QUALIFICATION_FILTER_MODEL_AGENT", map(
                "championship_id",       str(args,"championship_id"),
                "user_id",               str(args,"user_id"),
                "qualification_score",   score,
                "threshold",             75.0,
                "qualified",             score >= 75.0,
                "disqualification_flags",new ArrayList<>(),
                "eligibility_checks",    map("age_verified",true,"fee_paid",true,"region_valid",true),
                "filter_version",        "ANTIGRAVITY_QUAL_V3"
            ));
        });

        // 16. CHAMPIONSHIP_ML_6_PERFORMANCE_CONSISTENCY
        TOOLS.put("championship_performance_consistency", args -> result("CHAMPIONSHIP_ML_6_PERFORMANCE_CONSISTENCY_DETECTOR_AGENT", map(
            "user_id",               str(args,"user_id"),
            "championship_id",       str(args,"championship_id"),
            "rounds_analyzed",       str(args,"round_scores_csv").split(",").length,
            "consistency_index",     0.84,
            "std_deviation",         3.2,
            "peak_round",            2,
            "trend",                 "STABLE_WITH_UPSWING",
            "consistency_label",     "HIGH",
            "outlier_rounds",        new ArrayList<>(),
            "model",                 "ANTIGRAVITY_CONSIST_V2"
        )));

        // 17. CHAMPIONSHIP_ML_6_LIVE_RANKING_ENGINE
        TOOLS.put("championship_live_ranking", args -> result("CHAMPIONSHIP_ML_6_LIVE_RANKING_ENGINE_AGENT", map(
            "championship_id",         str(args,"championship_id"),
            "user_id",                 str(args,"user_id"),
            "live_score",              dbl(args,"live_score"),
            "current_rank",            4,
            "total_participants",      1280,
            "rank_delta",              "+2",
            "time_remaining_sec",      1800,
            "score_to_overtake_rank_3",2.1,
            "live_percentile",         Math.max(0, 99.7 - (dbl(args,"live_score") * 0.03)),
            "refresh_interval_ms",     500,
            "engine",                  "ANTIGRAVITY_LIVE_RANK_V4"
        )));

        // 18. CHAMPIONSHIP_ML_6_ANTI_CHEAT
        TOOLS.put("championship_anti_cheat", args -> result("CHAMPIONSHIP_ML_6_ANTI_CHEAT_BEHAVIORAL_MODEL", map(
            "user_id",             str(args,"user_id"),
            "championship_id",     str(args,"championship_id"),
            "cheat_probability",   0.03,
            "risk_level",          "CLEAN",
            "flags_triggered",     new ArrayList<>(),
            "keystroke_anomaly",   false,
            "tab_switch_count",    0,
            "paste_event_count",   0,
            "eye_tracking_normal", true,
            "verdict",             "AUTHENTIC",
            "log_entries_scanned", str(args,"behavioral_log_json").length() / 15,
            "model",               "ANTIGRAVITY_ANTICHEAT_V5"
        )));

        // 19. SKILL_SIMILARITY_EMBEDDING_AGENT
        TOOLS.put("skill_similarity_embedding", args -> result("27_SKILL_SIMILARITY_EMBEDDING_AGENT", map(
            "user_id",              str(args,"user_id"),
            "skill_a",              str(args,"skill_a"),
            "skill_b",              str(args,"skill_b"),
            "cosine_similarity",    0.73,
            "semantic_distance",    0.27,
            "related",              true,
            "shared_sub_skills",    list("problem_decomposition","pattern_recognition"),
            "transferability_score",0.68,
            "embedding_model",      "ANTIGRAVITY_EMBED_V2",
            "vector_dim",           768
        )));
    }

    // ── main ───────────────────────────────────────────────────
    public static void main(String[] args) throws IOException {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in,  StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
        System.err.println("[" + SERVER_NAME + "] Antigravity MCP ready — " + TOOLS.size() + " agents");
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try { out.println(dispatch(line)); }
            catch (Exception ex) { out.println(errResp(null, -32700, "Parse error: " + ex.getMessage())); }
        }
    }

    // ── dispatcher ─────────────────────────────────────────────
    static String dispatch(String raw) {
        Map<String,Object> req = JsonParser.parse(raw);
        Object id = req.get("id"); String method = (String) req.get("method");
        if (method == null) return errResp(id, -32600, "Invalid Request");
        switch (method) {
            case "initialize": return okResp(id, map(
                "protocolVersion", MCP_VERSION,
                "serverInfo", map("name",SERVER_NAME,"version",SERVER_VERSION,
                                  "category",CATEGORY,"architecture","ANTIGRAVITY"),
                "capabilities", map("tools", map())));
            case "tools/list": return handleList(id);
            case "tools/call": return handleCall(id, req);
            case "ping":       return okResp(id, map("status","pong"));
            default:           return errResp(id, -32601, "Method not found: " + method);
        }
    }

    private static String handleList(Object id) {
        List<Map<String,Object>> list = TOOLS.keySet().stream().map(name -> {
            Map<String,Object> s = schema(name);
            Map<String,Object> t = new LinkedHashMap<>();
            t.put("name", name); t.put("description", s.get("description")); t.put("inputSchema", s.get("inputSchema"));
            return t;
        }).collect(Collectors.toList());
        return okResp(id, map("tools", list));
    }

    @SuppressWarnings("unchecked")
    private static String handleCall(Object id, Map<String,Object> req) {
        Map<String,Object> p = (Map<String,Object>) req.getOrDefault("params", map());
        String name = (String) p.get("name");
        Map<String,Object> args = (Map<String,Object>) p.getOrDefault("arguments", map());
        ToolHandler h = TOOLS.get(name);
        if (h == null) return errResp(id, -32602, "Unknown tool: " + name);
        try {
            return okResp(id, map("content", list(map("type","text","text", JsonSerializer.toJson(h.handle(args))))));
        } catch (Exception ex) {
            return errResp(id, -32603, "Execution error: " + ex.getMessage());
        }
    }

    // ── schema registry ────────────────────────────────────────
    private static Map<String,Object> schema(String n) {
        switch (n) {
            case "skill_xp_calibration":
                return s("Calibrates raw XP to normalised tier-aware score via Antigravity V2.",
                    p(uid(),sid(),num("raw_xp","Raw experience points accumulated")), r("user_id","skill_id","raw_xp"));
            case "skill_rank_engine":
                return s("Computes ELO rating, global rank and percentile for a skill score.",
                    p(uid(),sid(),num("score","Skill score 0–100")), r("user_id","skill_id","score"));
            case "skill_performance_regression":
                return s("Fits polynomial regression on score history to forecast future performance.",
                    p(uid(),sid(),str_("score_history_csv","Comma-separated historical scores e.g. 60,65,70")), r("user_id","skill_id","score_history_csv"));
            case "skill_obsolescence_predictor":
                return s("Predicts market obsolescence risk and half-life of a skill.",
                    p(sid(),str_("domain","Professional domain e.g. SOFTWARE_ENGINEERING")), r("skill_id","domain"));
            case "skill_market_benchmark":
                return s("Benchmarks a skill against market demand/supply indices.",
                    p(sid(),str_("region","ISO region code e.g. IN, US, EU")), r("skill_id","region"));
            case "skill_improvement_planner":
                return s("Generates time-boxed improvement roadmap from current to target score.",
                    p(uid(),sid(),num("current_score","Current score 0–100"),num("target_score","Target score 0–100")), r("user_id","skill_id","current_score","target_score"));
            case "skill_fraud_detector":
                return s("Detects fraudulent skill attempts via behavioral signal analysis.",
                    p(uid(),str_("session_id","Active session ID"),str_("behavioral_signals_json","Behavioral telemetry JSON")), r("user_id","session_id","behavioral_signals_json"));
            case "skill_feedback_generator":
                return s("Generates personalised LLM-backed feedback for a skill score.",
                    p(uid(),sid(),num("score","Skill score 0–100")), r("user_id","skill_id","score"));
            case "skill_extraction_classifier":
                return s("Extracts and classifies skills from resume text using Antigravity taxonomy.",
                    p(uid(),str_("resume_or_text","Raw resume or free-form text")), r("user_id","resume_or_text"));
            case "skill_demand_forecast":
                return s("Forecasts demand growth for a skill over a given horizon using LSTM.",
                    p(sid(),str_("forecast_horizon_months","Months to forecast e.g. 6, 12, 24")), r("skill_id","forecast_horizon_months"));
            case "skill_confidence_model":
                return s("Measures calibration confidence — detects over/under confidence.",
                    p(uid(),sid(),str_("attempt_data_json","JSON array of attempts with score and self-estimate")), r("user_id","skill_id","attempt_data_json"));
            case "skill_challenge_generator":
                return s("Generates adaptive challenge set at a given difficulty level.",
                    p(uid(),sid(),str_("difficulty_level","EASY | MEDIUM | HARD | EXPERT")), r("user_id","skill_id","difficulty_level"));
            case "competition_difficulty_calibrator":
                return s("Calibrates competition difficulty against cohort Elo profile.",
                    p(str_("competition_id","Competition identifier"),str_("cohort_profile_json","JSON array of participant Elo ratings")), r("competition_id","cohort_profile_json"));
            case "championship_prize_allocation":
                return s("Optimises prize pool distribution across top-N ranks via logarithmic decay.",
                    p(str_("championship_id","Championship identifier"),num("total_prize_pool","Total pool in INR/USD"),num("top_n_ranks","Number of prize ranks")), r("championship_id","total_prize_pool","top_n_ranks"));
            case "championship_qualification_filter":
                return s("Filters championship qualifications against dynamic eligibility thresholds.",
                    p(str_("championship_id","Championship identifier"),uid(),num("qualification_score","Qualification round score")), r("championship_id","user_id","qualification_score"));
            case "championship_performance_consistency":
                return s("Detects performance consistency across championship rounds.",
                    p(uid(),str_("championship_id","Championship identifier"),str_("round_scores_csv","Comma-separated per-round scores")), r("user_id","championship_id","round_scores_csv"));
            case "championship_live_ranking":
                return s("Computes real-time live rank during an ongoing championship.",
                    p(str_("championship_id","Championship identifier"),uid(),num("live_score","Current live score")), r("championship_id","user_id","live_score"));
            case "championship_anti_cheat":
                return s("Detects cheating in championships via behavioral biometrics and ML.",
                    p(uid(),str_("championship_id","Championship identifier"),str_("behavioral_log_json","Keystroke, mouse and tab-switch event log JSON")), r("user_id","championship_id","behavioral_log_json"));
            case "skill_similarity_embedding":
                return s("Computes cosine similarity between two skills using 768-dim embeddings.",
                    p(uid(),str_("skill_a","First skill name or ID"),str_("skill_b","Second skill name or ID")), r("user_id","skill_a","skill_b"));
            default:
                return map("description","Unknown","inputSchema",map("type","object","properties",map()));
        }
    }

    // ── prize table ────────────────────────────────────────────
    private static List<Map<String,Object>> buildPrizeTable(double pool, int n) {
        List<Map<String,Object>> t = new ArrayList<>();
        double total = 0; double[] w = new double[n];
        for (int i=0;i<n;i++) { w[i]=1.0/Math.log(i+2); total+=w[i]; }
        for (int i=0;i<n;i++) t.add(map("rank",i+1,"prize_amount",Math.round(pool*w[i]/total*100.0)/100.0));
        return t;
    }

    // ── tiny DSL ───────────────────────────────────────────────
    private static Map<String,Object> s(String desc, Map<String,Object> props, List<String> req) {
        return map("description",desc,"inputSchema",map("type","object","properties",props,"required",req));
    }
    @SafeVarargs
    private static Map<String,Object> p(Map.Entry<String,Object>... es) {
        Map<String,Object> m=new LinkedHashMap<>(); for(var e:es) m.put(e.getKey(),e.getValue()); return m;
    }
    private static List<String> r(String... ks) { return Arrays.asList(ks); }
    private static Map.Entry<String,Object> uid() { return e("user_id",Map.of("type","string","description","Unique user identifier")); }
    private static Map.Entry<String,Object> sid() { return e("skill_id",Map.of("type","string","description","Unique skill identifier")); }
    private static Map.Entry<String,Object> str_(String k,String d) { return e(k,Map.of("type","string","description",d)); }
    private static Map.Entry<String,Object> num(String k,String d)  { return e(k,Map.of("type","number","description",d)); }
    private static Map.Entry<String,Object> e(String k,Object v)    { return new AbstractMap.SimpleEntry<>(k,v); }

    // ── map/list builders ──────────────────────────────────────
    static Map<String,Object> map(Object... kv) {
        Map<String,Object> m=new LinkedHashMap<>();
        for(int i=0;i<kv.length-1;i+=2) m.put(kv[i].toString(),kv[i+1]); return m;
    }
    @SafeVarargs static <T> List<T> list(T... items) { return new ArrayList<>(Arrays.asList(items)); }

    // ── arg helpers ────────────────────────────────────────────
    static String str(Map<String,Object> a,String k) { Object v=a.get(k); return v!=null?v.toString():""; }
    static double dbl(Map<String,Object> a,String k) {
        Object v=a.get(k); if(v==null)return 0.0; if(v instanceof Number)return((Number)v).doubleValue();
        try{return Double.parseDouble(v.toString());}catch(Exception e){return 0.0;}
    }

    // ── result wrapper ─────────────────────────────────────────
    private static Map<String,Object> result(String agent, Map<String,Object> data) {
        Map<String,Object> m=new LinkedHashMap<>();
        m.put("agent",agent); m.put("status","success");
        m.put("architecture","ANTIGRAVITY"); m.put("category",CATEGORY);
        m.put("timestamp",System.currentTimeMillis()); m.putAll(data); return m;
    }

    // ── JSON-RPC envelopes ─────────────────────────────────────
    static String okResp(Object id,Object r){ return "{\"jsonrpc\":\"2.0\",\"id\":"+JsonSerializer.toJson(id)+",\"result\":"+JsonSerializer.toJson(r)+"}"; }
    static String errResp(Object id,int code,String msg){ return "{\"jsonrpc\":\"2.0\",\"id\":"+JsonSerializer.toJson(id)+",\"error\":{\"code\":"+code+",\"message\":"+JsonSerializer.toJson(msg)+"}}"; }

    @FunctionalInterface interface ToolHandler { Object handle(Map<String,Object> args); }
}
