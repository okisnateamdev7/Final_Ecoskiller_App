package com.ecoskiller.mcp;

import java.lang.reflect.Method;
import java.util.*;

/**
 * CAT-10 Skill & Competition Core — Test harness for all 19 agents.
 * Usage:
 *   java -cp out com.ecoskiller.mcp.TestAgents
 *   java -cp out com.ecoskiller.mcp.TestAgents --verbose
 */
public class TestAgents {

    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        boolean v = args.length > 0 && args[0].equals("--verbose");
        System.out.println("══════════════════════════════════════════════");
        System.out.println("  mcp-10-skill-competition | Antigravity Tests");
        System.out.println("══════════════════════════════════════════════\n");

        // Protocol
        call(v, "initialize",  "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"initialize\",\"params\":{}}", "protocolVersion");
        call(v, "tools/list",  "{\"jsonrpc\":\"2.0\",\"id\":2,\"method\":\"tools/list\",\"params\":{}}",  "tools");
        call(v, "ping",        "{\"jsonrpc\":\"2.0\",\"id\":3,\"method\":\"ping\",\"params\":{}}",         "pong");

        // 19 agents
        tool(v, "skill_xp_calibration",
            "{\"user_id\":\"u1\",\"skill_id\":\"python\",\"raw_xp\":1500}", "calibrated_xp");

        tool(v, "skill_rank_engine",
            "{\"user_id\":\"u1\",\"skill_id\":\"python\",\"score\":82}", "elo_rating");

        tool(v, "skill_performance_regression",
            "{\"user_id\":\"u1\",\"skill_id\":\"python\",\"score_history_csv\":\"60,65,70,74,79\"}", "r_squared");

        tool(v, "skill_obsolescence_predictor",
            "{\"skill_id\":\"excel\",\"domain\":\"DATA_ANALYTICS\"}", "estimated_half_life_yrs");

        tool(v, "skill_market_benchmark",
            "{\"skill_id\":\"python\",\"region\":\"IN\"}", "demand_supply_ratio");

        tool(v, "skill_improvement_planner",
            "{\"user_id\":\"u1\",\"skill_id\":\"python\",\"current_score\":62,\"target_score\":85}", "estimated_days");

        tool(v, "skill_fraud_detector",
            "{\"user_id\":\"u1\",\"session_id\":\"sess-99\",\"behavioral_signals_json\":\"{\\\"clicks\\\":12}\"}", "fraud_probability");

        tool(v, "skill_feedback_generator",
            "{\"user_id\":\"u1\",\"skill_id\":\"python\",\"score\":78}", "feedback_id");

        tool(v, "skill_extraction_classifier",
            "{\"user_id\":\"u1\",\"resume_or_text\":\"5 years Python, machine learning, team leadership\"}", "total_skills_found");

        tool(v, "skill_demand_forecast",
            "{\"skill_id\":\"python\",\"forecast_horizon_months\":\"12\"}", "demand_growth_pct");

        tool(v, "skill_confidence_model",
            "{\"user_id\":\"u1\",\"skill_id\":\"python\",\"attempt_data_json\":\"[{\\\"score\\\":80,\\\"estimate\\\":90}]\"}", "confidence_score");

        tool(v, "skill_challenge_generator",
            "{\"user_id\":\"u1\",\"skill_id\":\"python\",\"difficulty_level\":\"HARD\"}", "challenge_id");

        tool(v, "competition_difficulty_calibrator",
            "{\"competition_id\":\"COMP-001\",\"cohort_profile_json\":\"[1500,1540,1600,1450]\"}", "recommended_difficulty");

        tool(v, "championship_prize_allocation",
            "{\"championship_id\":\"CHAMP-2025\",\"total_prize_pool\":100000,\"top_n_ranks\":5}", "allocation_table");

        tool(v, "championship_qualification_filter",
            "{\"championship_id\":\"CHAMP-2025\",\"user_id\":\"u1\",\"qualification_score\":82}", "qualified");

        tool(v, "championship_performance_consistency",
            "{\"user_id\":\"u1\",\"championship_id\":\"CHAMP-2025\",\"round_scores_csv\":\"78,81,80,85\"}", "consistency_index");

        tool(v, "championship_live_ranking",
            "{\"championship_id\":\"CHAMP-2025\",\"user_id\":\"u1\",\"live_score\":88.5}", "current_rank");

        tool(v, "championship_anti_cheat",
            "{\"user_id\":\"u1\",\"championship_id\":\"CHAMP-2025\",\"behavioral_log_json\":\"{\\\"tabs\\\":0,\\\"pastes\\\":0}\"}", "verdict");

        tool(v, "skill_similarity_embedding",
            "{\"user_id\":\"u1\",\"skill_a\":\"Python\",\"skill_b\":\"R_Language\"}", "cosine_similarity");

        System.out.println("\n══════════════════════════════════════════════");
        System.out.printf("  Results: %d passed | %d failed%n", passed, failed);
        System.out.println("══════════════════════════════════════════════");
    }

    private static void tool(boolean v, String name, String argsJson, String expectKey) {
        String req = "{\"jsonrpc\":\"2.0\",\"id\":99,\"method\":\"tools/call\","
                   + "\"params\":{\"name\":\"" + name + "\",\"arguments\":" + argsJson + "}}";
        call(v, name, req, expectKey);
    }

    private static void call(boolean v, String label, String req, String expectKey) {
        try {
            Method m = McpServer.class.getDeclaredMethod("dispatch", String.class);
            m.setAccessible(true);
            String res = (String) m.invoke(null, req);
            boolean ok = res.contains(expectKey);
            if (ok) { passed++; System.out.printf("  ✓  PASS  %s%n", label); }
            else    { failed++; System.out.printf("  ✗  FAIL  %s  (missing: %s)%n", label, expectKey); }
            if (v) {
                System.out.println("       REQ: " + req);
                System.out.println("       RES: " + res);
            }
        } catch (Exception e) {
            failed++;
            System.out.printf("  ✗  ERR   %s — %s%n", label, e.getMessage());
        }
    }
}
