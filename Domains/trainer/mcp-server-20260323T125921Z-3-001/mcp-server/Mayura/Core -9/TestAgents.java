package com.ecoskiller.mcp;

import java.util.*;

/**
 * Quick test: sends JSON-RPC requests directly to McpServer logic
 * without spawning a subprocess.
 */
public class TestAgents {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        boolean verbose = args.length > 0 && args[0].equals("--verbose");

        System.out.println("=== mcp-core-intelligence — Agent Tests ===\n");

        // initialize
        test("initialize", verbose,
            "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"initialize\",\"params\":{}}",
            "protocolVersion");

        // tools/list
        test("tools/list", verbose,
            "{\"jsonrpc\":\"2.0\",\"id\":2,\"method\":\"tools/list\",\"params\":{}}",
            "tools");

        // ping
        test("ping", verbose,
            "{\"jsonrpc\":\"2.0\",\"id\":3,\"method\":\"ping\",\"params\":{}}",
            "pong");

        // 19 agent calls
        callTool(verbose, "spatial_pattern_model",
            "{\"user_id\":\"u1\",\"input_data\":\"1,0,1,1,0\"}",
            "spatial_patterns_detected");

        callTool(verbose, "reflective_depth_analyzer",
            "{\"user_id\":\"u1\",\"response_text\":\"I think I failed because I assumed too early.\"}",
            "depth_score");

        callTool(verbose, "population_percentile_engine",
            "{\"user_id\":\"u1\",\"raw_score\":85,\"domain\":\"logical\"}",
            "percentile");

        callTool(verbose, "open_response_evaluation",
            "{\"user_id\":\"u1\",\"response_text\":\"The solution requires iterative refinement.\",\"rubric_id\":\"RUB-101\"}",
            "total_score");

        callTool(verbose, "naturalistic_classification_model",
            "{\"user_id\":\"u1\",\"observation_data\":\"spotted eagle near river\"}",
            "naturalistic_iq_estimate");

        callTool(verbose, "musical_frequency_model",
            "{\"user_id\":\"u1\",\"audio_metadata\":\"{\\\"tempo\\\":120,\\\"pitch\\\":440}\"}",
            "pitch_accuracy");

        callTool(verbose, "logical_scoring_model",
            "{\"user_id\":\"u1\",\"answer_sequence\":\"A,B,C,A,D,B,C\"}",
            "logic_score");

        callTool(verbose, "linguistic_structural_analyzer",
            "{\"user_id\":\"u1\",\"text_input\":\"Despite the challenges, the team persisted with remarkable cohesion.\"}",
            "syntax_complexity_score");

        callTool(verbose, "learning_velocity_model",
            "{\"user_id\":\"u1\",\"session_id\":\"sess-42\"}",
            "velocity_score");

        callTool(verbose, "kinesthetic_motion_model",
            "{\"user_id\":\"u1\",\"motion_data\":\"0.1,0.4,0.9,1.2,0.8\"}",
            "motor_precision_score");

        callTool(verbose, "intrapersonal_behavioral_model",
            "{\"user_id\":\"u1\",\"profile_data\":\"{\\\"anxiety_level\\\":3,\\\"goal_orientation\\\":8}\"}",
            "self_regulation_score");

        callTool(verbose, "interpersonal_graph_model",
            "{\"user_id\":\"u1\",\"social_graph_json\":\"{\\\"nodes\\\":47,\\\"edges\\\":120}\"}",
            "centrality_score");

        callTool(verbose, "intelligence_report_generator",
            "{\"user_id\":\"u1\",\"report_type\":\"FULL\"}",
            "report_id");

        callTool(verbose, "intelligence_growth_timeseries",
            "{\"user_id\":\"u1\",\"start_date\":\"2024-01-01\",\"end_date\":\"2024-12-31\"}",
            "growth_rate_percent");

        callTool(verbose, "entrepreneurial_risk_model",
            "{\"user_id\":\"u1\",\"idea_data\":\"EdTech platform for rural students\"}",
            "risk_tolerance_score");

        callTool(verbose, "debate_quality_analyzer",
            "{\"user_id\":\"u1\",\"debate_transcript\":\"Opponent claims AI replaces teachers. I disagree.\"}",
            "argument_strength_score");

        callTool(verbose, "creativity_divergence_agent",
            "{\"user_id\":\"u1\",\"task_input\":\"List all uses of a brick\"}",
            "divergence_quotient");

        callTool(verbose, "cognitive_stability_index",
            "{\"user_id\":\"u1\",\"session_log\":\"login\\nanswer\\nanswer\\nbreak\\nanswer\"}",
            "stability_index");

        callTool(verbose, "ai_collaboration_efficiency_model",
            "{\"user_id\":\"u1\",\"ai_session_data\":\"prompt1 response1 prompt2 response2\"}",
            "collaboration_score");

        System.out.println("\n=== Results: " + passed + " passed | " + failed + " failed ===");
    }

    private static void callTool(boolean verbose, String toolName, String argsJson, String expectedKey) {
        String payload = "{\"jsonrpc\":\"2.0\",\"id\":99,\"method\":\"tools/call\","
            + "\"params\":{\"name\":\"" + toolName + "\",\"arguments\":" + argsJson + "}}";
        test(toolName, verbose, payload, expectedKey);
    }

    private static void test(String label, boolean verbose, String request, String expectedKey) {
        try {
            // Use the reflection-free approach: call handleRequest via stdin simulation
            // Instead, we directly parse the response by calling the logic
            String response = invokeServer(request);
            if (verbose) {
                System.out.println("[" + label + "]");
                System.out.println("  REQ: " + request);
                System.out.println("  RES: " + response);
            }
            boolean ok = response.contains(expectedKey);
            if (ok) {
                passed++;
                System.out.println("  ✓  PASS  " + label);
            } else {
                failed++;
                System.out.println("  ✗  FAIL  " + label + " (missing key: " + expectedKey + ")");
                if (!verbose) System.out.println("       RES: " + response);
            }
        } catch (Exception e) {
            failed++;
            System.out.println("  ✗  ERROR " + label + " — " + e.getMessage());
        }
    }

    // Reflection-free invocation: reuse McpServer logic via a package-private bridge
    private static String invokeServer(String request) throws Exception {
        // McpServer.handleRequest is private, but TestAgents is in same package.
        // We use reflection here only for the test harness.
        java.lang.reflect.Method m = McpServer.class.getDeclaredMethod("handleRequest", String.class);
        m.setAccessible(true);
        return (String) m.invoke(null, request);
    }
}
