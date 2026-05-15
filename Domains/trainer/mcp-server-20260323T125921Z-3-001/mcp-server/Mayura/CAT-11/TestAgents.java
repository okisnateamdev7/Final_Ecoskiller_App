package com.ecoskiller.mcp;

import java.lang.reflect.Method;

/**
 * CAT-11 Championship Advanced + Parent Predictive AI
 * Test harness — 23 tests (3 protocol + 20 agents)
 *
 * Usage:
 *   java -cp out com.ecoskiller.mcp.TestAgents
 *   java -cp out com.ecoskiller.mcp.TestAgents --verbose
 */
public class TestAgents {

    private static int passed = 0, failed = 0;
    private static boolean verbose = false;

    public static void main(String[] args) {
        verbose = args.length > 0 && args[0].equals("--verbose");

        banner("mcp-11-championship-parent-ai | Antigravity CAT-11 Tests");

        // ── Protocol ──────────────────────────────────────────────────
        call("initialize",
            "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"initialize\",\"params\":{}}",
            "protocolVersion");

        call("tools/list",
            "{\"jsonrpc\":\"2.0\",\"id\":2,\"method\":\"tools/list\",\"params\":{}}",
            "tools");

        call("ping",
            "{\"jsonrpc\":\"2.0\",\"id\":3,\"method\":\"ping\",\"params\":{}}",
            "pong");

        // ── 20 Agents ─────────────────────────────────────────────────
        tool("regional_distribution_engine",
            "{\"championship_id\":\"CHAMP-2025\",\"total_participants\":1200}",
            "region_allocations");

        tool("parent_llm_insight_narrative",
            "{\"student_id\":\"s-001\",\"parent_id\":\"p-001\"}",
            "narrative_id");

        tool("parent_llm_career_guidance",
            "{\"student_id\":\"s-001\",\"age\":14}",
            "career_pathways");

        tool("learning_behavior_drift",
            "{\"student_id\":\"s-001\",\"observation_days\":30}",
            "drift_detected");

        tool("leaderboard_stability_model",
            "{\"championship_id\":\"CHAMP-2025\",\"student_id\":\"s-001\",\"current_rank\":5}",
            "stability_index");

        tool("income_prediction_model",
            "{\"student_id\":\"s-001\",\"current_age\":14,\"skill_profile_json\":\"{\\\"logical\\\":84,\\\"spatial\\\":79}\"}",
            "predicted_income_at_25");

        tool("career_probability_model",
            "{\"student_id\":\"s-001\",\"target_career\":\"Data Scientist\",\"current_age\":14}",
            "probability");

        tool("weakness_impact_model",
            "{\"student_id\":\"s-001\"}",
            "composite_weakness_impact");

        tool("spectator_analytics_model",
            "{\"championship_id\":\"CHAMP-2025\"}",
            "spectator_count");

        tool("score_normalization_engine",
            "{\"student_id\":\"s-001\",\"raw_score\":82,\"cohort_mean\":70,\"cohort_std_dev\":12}",
            "normalized_score");

        tool("plateau_detection_model",
            "{\"student_id\":\"s-001\",\"skill_id\":\"python\"}",
            "plateau_detected");

        tool("peer_comparison_engine",
            "{\"student_id\":\"s-001\",\"comparison_scope\":\"NATIONAL\"}",
            "student_percentile");

        tool("parent_llm_growth_strategy",
            "{\"student_id\":\"s-001\",\"parent_id\":\"p-001\"}",
            "strategy_id");

        tool("learning_behavior_drift_v2",
            "{\"student_id\":\"s-001\",\"window_days\":14}",
            "drift_vector");

        tool("dropout_risk_model",
            "{\"student_id\":\"s-001\"}",
            "dropout_probability");

        tool("competition_forecast_engine",
            "{\"championship_id\":\"CHAMP-2025\",\"student_id\":\"s-001\",\"current_score\":88.5}",
            "predicted_final_rank");

        tool("championship_problem_generation",
            "{\"championship_id\":\"CHAMP-2025\",\"round_number\":3,\"difficulty_target\":\"HARD\"}",
            "problems_generated");

        tool("championship_ai_judging",
            "{\"submission_id\":\"SUB-001\",\"student_id\":\"s-001\",\"problem_id\":\"P004\"}",
            "awarded_score");

        tool("champion_authenticity_model",
            "{\"student_id\":\"s-001\",\"championship_id\":\"CHAMP-2025\"}",
            "authenticity_score");

        tool("academic_intelligence_correlation",
            "{\"student_id\":\"s-001\",\"academic_gpa\":8.4}",
            "correlation_matrix");

        // ── Summary ────────────────────────────────────────────────────
        System.out.println("\n" + "═".repeat(54));
        System.out.printf("  Results : %d passed | %d failed%n", passed, failed);
        System.out.println("═".repeat(54));
    }

    // ── helpers ──────────────────────────────────────────────────────────
    private static void tool(String name, String argsJson, String expectKey) {
        call(name,
            "{\"jsonrpc\":\"2.0\",\"id\":99,\"method\":\"tools/call\","
          + "\"params\":{\"name\":\"" + name + "\",\"arguments\":" + argsJson + "}}",
            expectKey);
    }

    private static void call(String label, String req, String expectKey) {
        try {
            Method m = McpServer.class.getDeclaredMethod("dispatch", String.class);
            m.setAccessible(true);
            String res = (String) m.invoke(null, req);
            boolean ok = res.contains(expectKey);
            if (ok) { passed++; System.out.printf("  ✓  PASS  %s%n", label); }
            else    { failed++; System.out.printf("  ✗  FAIL  %s  (missing key: %s)%n", label, expectKey); }
            if (verbose) {
                System.out.println("       REQ : " + req);
                System.out.println("       RES : " + res);
            }
        } catch (Exception ex) {
            failed++;
            System.out.printf("  ✗  ERR   %s — %s%n", label, ex.getMessage());
        }
    }

    private static void banner(String title) {
        String line = "═".repeat(54);
        System.out.println(line);
        System.out.println("  " + title);
        System.out.println(line + "\n");
    }
}
