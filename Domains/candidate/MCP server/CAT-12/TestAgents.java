package com.ecoskiller.mcp;

import java.lang.reflect.Method;

/**
 * CAT-12 Institute + HR Predictive Systems
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
        banner("mcp-12-institute-hr  |  Antigravity CAT-12 Tests");

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
        tool("team_compatibility_gnn",
            "{\"team_id\":\"T-001\",\"member_count\":5}",
            "compatibility_score");

        tool("talent_retrieval_vector_engine",
            "{\"search_query\":\"Senior ML engineer with LLM experience\",\"top_k\":5}",
            "match_score");

        tool("workforce_gap_model",
            "{\"org_id\":\"ORG-001\",\"department\":\"ENGINEERING\",\"headcount\":80}",
            "critical_gaps_count");

        tool("hiring_bias_detector",
            "{\"pipeline_id\":\"PIPE-2025-Q1\",\"candidates_audited\":240}",
            "overall_fairness_score");

        tool("digital_twin_simulation",
            "{\"entity_id\":\"INST-004\",\"entity_type\":\"INSTITUTE\",\"horizon_months\":12}",
            "scenario_outcomes");

        tool("candidate_ranking_model",
            "{\"job_id\":\"JOB-501\",\"candidate_count\":5}",
            "top_recommendation");

        tool("student_distribution_model",
            "{\"institute_id\":\"INST-004\",\"academic_year\":\"2024-25\",\"total_students\":1200}",
            "at_risk_count");

        tool("salary_prediction_model",
            "{\"candidate_id\":\"c-1021\",\"role\":\"Senior Data Engineer\",\"experience_years\":5,\"location\":\"Bangalore\"}",
            "predicted_salary");

        tool("retention_probability_model",
            "{\"employee_id\":\"e-8821\"}",
            "retention_probability_12m");

        tool("promotion_probability_model",
            "{\"employee_id\":\"e-8821\",\"current_level\":\"L4\"}",
            "promotion_probability_12m");

        tool("placement_probability_model",
            "{\"student_id\":\"s-001\",\"graduation_year\":\"2025\"}",
            "placement_probability");

        tool("performance_forecast_model",
            "{\"employee_id\":\"e-8821\",\"current_score\":78,\"forecast_months\":12}",
            "predicted_performance");

        tool("interview_semantic_analyzer",
            "{\"interview_id\":\"INT-001\",\"candidate_id\":\"c-1021\",\"transcript_text\":\"I have led three data pipeline migrations and improved ingestion throughput by 40 percent.\"}",
            "hire_signal");

        tool("institute_revenue_forecast",
            "{\"institute_id\":\"INST-004\",\"forecast_year\":\"2025\",\"current_revenue_inr\":50000000}",
            "annual_projected_inr");

        tool("institute_ranking_model",
            "{\"institute_id\":\"INST-004\",\"ranking_scope\":\"NATIONAL\"}",
            "composite_rank");

        tool("institute_performance_predictor",
            "{\"institute_id\":\"INST-004\",\"current_index\":72,\"horizon_months\":12}",
            "predicted_index_12m");

        tool("curriculum_gap_detector",
            "{\"institute_id\":\"INST-004\",\"program\":\"B.Tech CSE\"}",
            "critical_gap_count");

        tool("class_intelligence_aggregator",
            "{\"class_id\":\"CLS-2025-A\",\"institute_id\":\"INST-004\",\"student_count\":60}",
            "cohort_strength");

        tool("candidate_summary_generator",
            "{\"candidate_id\":\"c-1021\"}",
            "summary_id");

        tool("benchmark_comparison_engine",
            "{\"entity_id\":\"INST-004\",\"entity_type\":\"INSTITUTE\",\"benchmark_scope\":\"NATIONAL\"}",
            "benchmark_percentile");

        // ── Summary ────────────────────────────────────────────────────
        System.out.println("\n" + "═".repeat(56));
        System.out.printf("  Results : %d passed | %d failed%n", passed, failed);
        System.out.println("═".repeat(56));
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
            else    { failed++; System.out.printf("  ✗  FAIL  %s  (missing: %s)%n", label, expectKey); }
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
        String line = "═".repeat(56);
        System.out.println(line);
        System.out.println("  " + title);
        System.out.println(line + "\n");
    }
}
