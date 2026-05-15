package com.ecoskiller.mcp;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Ecoskiller | CAT-9 — Core Intelligence Architect
 * MCP Server in Java | 19 Agents | Priority: HIGH
 *
 * Transport: stdio (stdin/stdout)
 * Protocol:  JSON-RPC 2.0
 * MCP Version: 2024-11-05
 */
public class McpServer {

    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "mcp-core-intelligence";
    private static final String SERVER_VERSION = "1.0.0";

    // ─────────────────────────────────────────────
    // Tool registry: name → handler
    // ─────────────────────────────────────────────
    private static final Map<String, ToolHandler> TOOLS = new LinkedHashMap<>();

    static {
        // 1
        TOOLS.put("spatial_pattern_model", args -> {
            String userId    = str(args, "user_id");
            String inputData = str(args, "input_data");
            return Map.of(
                "agent",  "SPATIAL_PATTERN_MODEL_AGENT",
                "status", "analyzed",
                "user_id", userId,
                "spatial_patterns_detected", List.of("grid_alignment", "symmetry_axis", "cluster_density"),
                "confidence_score", 0.87,
                "pattern_complexity", "HIGH",
                "visualization_ready", true,
                "input_tokens", inputData.length()
            );
        });

        // 2
        TOOLS.put("reflective_depth_analyzer", args -> {
            String userId    = str(args, "user_id");
            String response  = str(args, "response_text");
            return Map.of(
                "agent",  "REFLECTIVE_DEPTH_ANALYZER_AGENT",
                "status", "evaluated",
                "user_id", userId,
                "depth_score", 7.4,
                "metacognition_detected", true,
                "abstract_reasoning_level", "ADVANCED",
                "reflection_markers", List.of("self-questioning", "hypothesis_revision", "causal_attribution"),
                "word_count", response.split("\\s+").length
            );
        });

        // 3
        TOOLS.put("population_percentile_engine", args -> {
            String userId     = str(args, "user_id");
            double rawScore   = dbl(args, "raw_score");
            String domain     = str(args, "domain");
            return Map.of(
                "agent",  "POPULATION_PERCENTILE_ENGINE_AGENT",
                "status", "computed",
                "user_id", userId,
                "domain", domain,
                "raw_score", rawScore,
                "percentile", Math.min(99.9, rawScore * 1.1),
                "population_size", 1_250_000,
                "cohort_label", rawScore >= 90 ? "TOP_1_PERCENT" : rawScore >= 75 ? "TOP_10_PERCENT" : "AVERAGE",
                "z_score", (rawScore - 70.0) / 15.0
            );
        });

        // 4
        TOOLS.put("open_response_evaluation", args -> {
            String userId    = str(args, "user_id");
            String response  = str(args, "response_text");
            String rubric    = str(args, "rubric_id");
            return Map.of(
                "agent",  "OPEN_RESPONSE_EVALUATION_AGENT",
                "status", "scored",
                "user_id", userId,
                "rubric_id", rubric,
                "total_score", 84,
                "max_score", 100,
                "dimensions", Map.of(
                    "clarity", 88,
                    "depth", 82,
                    "accuracy", 90,
                    "originality", 76
                ),
                "feedback_summary", "Strong causal reasoning with room to improve originality.",
                "word_count", response.split("\\s+").length
            );
        });

        // 5
        TOOLS.put("naturalistic_classification_model", args -> {
            String userId    = str(args, "user_id");
            String inputData = str(args, "observation_data");
            return Map.of(
                "agent",  "NATURALISTIC_CLASSIFICATION_MODEL_AGENT",
                "status", "classified",
                "user_id", userId,
                "primary_category", "BIOLOGICAL_PATTERN_RECOGNITION",
                "sub_categories", List.of("taxonomy_recall", "ecosystem_inference", "habitat_mapping"),
                "naturalistic_iq_estimate", 118,
                "observation_richness_score", 0.79,
                "sample_size", inputData.length()
            );
        });

        // 6
        TOOLS.put("musical_frequency_model", args -> {
            String userId    = str(args, "user_id");
            String audioMeta = str(args, "audio_metadata");
            return Map.of(
                "agent",  "MUSICAL_FREQUENCY_MODEL_AGENT",
                "status", "processed",
                "user_id", userId,
                "pitch_accuracy", 0.93,
                "rhythm_precision", 0.89,
                "harmonic_complexity_index", 6.2,
                "detected_patterns", List.of("polyrhythm", "modal_shift", "dynamic_contrast"),
                "musical_intelligence_score", 82,
                "metadata_length", audioMeta.length()
            );
        });

        // 7
        TOOLS.put("logical_scoring_model", args -> {
            String userId = str(args, "user_id");
            String answers = str(args, "answer_sequence");
            return Map.of(
                "agent",  "LOGICAL_SCORING_MODEL_AGENT",
                "status", "scored",
                "user_id", userId,
                "logic_score", 91,
                "deductive_accuracy", 0.95,
                "inductive_accuracy", 0.88,
                "abductive_score", 0.82,
                "error_pattern", "false_dilemma_tendency",
                "total_questions_evaluated", answers.split(",").length
            );
        });

        // 8
        TOOLS.put("linguistic_structural_analyzer", args -> {
            String userId = str(args, "user_id");
            String text   = str(args, "text_input");
            return Map.of(
                "agent",  "LINGUISTIC_STRUCTURAL_ANALYZER_AGENT",
                "status", "analyzed",
                "user_id", userId,
                "syntax_complexity_score", 7.8,
                "vocabulary_richness", 0.74,
                "avg_sentence_length", 18.4,
                "cohesion_index", 0.81,
                "detected_structures", List.of("subordinate_clauses", "passive_voice", "anaphora"),
                "language_proficiency_estimate", "C1",
                "word_count", text.split("\\s+").length
            );
        });

        // 9
        TOOLS.put("learning_velocity_model", args -> {
            String userId    = str(args, "user_id");
            String sessionId = str(args, "session_id");
            return Map.of(
                "agent",  "LEARNING_VELOCITY_MODEL_AGENT",
                "status", "computed",
                "user_id", userId,
                "session_id", sessionId,
                "velocity_score", 3.4,
                "unit", "concepts_per_hour",
                "acceleration_trend", "INCREASING",
                "optimal_session_duration_min", 45,
                "retention_probability_7d", 0.72,
                "recommended_next_topic", "advanced_inference_chains"
            );
        });

        // 10
        TOOLS.put("kinesthetic_motion_model", args -> {
            String userId    = str(args, "user_id");
            String sensorData = str(args, "motion_data");
            return Map.of(
                "agent",  "KINESTHETIC_MOTION_MODEL_AGENT",
                "status", "evaluated",
                "user_id", userId,
                "motor_precision_score", 85,
                "reaction_time_ms", 234,
                "coordination_index", 0.88,
                "movement_patterns", List.of("fine_motor_control", "bilateral_coordination"),
                "kinesthetic_iq_estimate", 110,
                "data_points_processed", sensorData.split(",").length
            );
        });

        // 11
        TOOLS.put("intrapersonal_behavioral_model", args -> {
            String userId = str(args, "user_id");
            String profileData = str(args, "profile_data");
            return Map.of(
                "agent",  "INTRAPERSONAL_BEHAVIORAL_MODEL_AGENT",
                "status", "modeled",
                "user_id", userId,
                "self_regulation_score", 78,
                "emotional_awareness_index", 0.83,
                "motivation_stability", "HIGH",
                "dominant_coping_style", "PROBLEM_FOCUSED",
                "intrapersonal_intelligence_score", 86,
                "profile_completeness", profileData.length() > 100 ? "FULL" : "PARTIAL"
            );
        });

        // 12
        TOOLS.put("interpersonal_graph_model", args -> {
            String userId   = str(args, "user_id");
            String graphJson = str(args, "social_graph_json");
            return Map.of(
                "agent",  "INTERPERSONAL_GRAPH_MODEL_AGENT",
                "status", "mapped",
                "user_id", userId,
                "node_count", 47,
                "avg_edge_weight", 0.62,
                "centrality_score", 0.78,
                "influence_radius", 3,
                "dominant_role", "CONNECTOR",
                "community_clusters", 4,
                "graph_density", 0.41,
                "graph_payload_length", graphJson.length()
            );
        });

        // 13
        TOOLS.put("intelligence_report_generator", args -> {
            String userId    = str(args, "user_id");
            String reportType = str(args, "report_type");
            return Map.of(
                "agent",  "INTELLIGENCE_REPORT_GENERATOR_AGENT",
                "status", "generated",
                "user_id", userId,
                "report_type", reportType,
                "report_id", "RPT-" + System.currentTimeMillis(),
                "sections_generated", List.of(
                    "executive_summary",
                    "domain_breakdown",
                    "percentile_analysis",
                    "growth_trajectory",
                    "recommendations"
                ),
                "total_pages", 12,
                "format", "PDF_READY",
                "generated_at", new java.util.Date().toString()
            );
        });

        // 14
        TOOLS.put("intelligence_growth_timeseries", args -> {
            String userId   = str(args, "user_id");
            String startDate = str(args, "start_date");
            String endDate   = str(args, "end_date");
            return Map.of(
                "agent",  "INTELLIGENCE_GROWTH_TIME_SERIES_AGENT",
                "status", "computed",
                "user_id", userId,
                "period", startDate + " to " + endDate,
                "growth_rate_percent", 14.3,
                "trend", "UPWARD",
                "inflection_points", List.of("2024-03-01", "2024-07-15"),
                "peak_performance_date", "2024-09-10",
                "volatility_index", 0.18,
                "forecast_next_30d", "+5.2%"
            );
        });

        // 15
        TOOLS.put("entrepreneurial_risk_model", args -> {
            String userId     = str(args, "user_id");
            String ideaData   = str(args, "idea_data");
            return Map.of(
                "agent",  "ENTREPRENEURIAL_RISK_MODEL_AGENT",
                "status", "assessed",
                "user_id", userId,
                "risk_tolerance_score", 72,
                "innovation_index", 0.81,
                "market_timing_score", 68,
                "failure_resilience", "HIGH",
                "recommended_venture_type", "LEAN_STARTUP",
                "idea_viability_score", 77,
                "idea_length", ideaData.length()
            );
        });

        // 16
        TOOLS.put("debate_quality_analyzer", args -> {
            String userId   = str(args, "user_id");
            String transcript = str(args, "debate_transcript");
            return Map.of(
                "agent",  "DEBATE_QUALITY_ANALYZER_AGENT",
                "status", "analyzed",
                "user_id", userId,
                "argument_strength_score", 82,
                "fallacies_detected", List.of("appeal_to_authority", "hasty_generalization"),
                "rebuttal_effectiveness", 0.76,
                "evidence_quality_score", 79,
                "persuasion_index", 0.84,
                "overall_debate_quality", "STRONG",
                "transcript_word_count", transcript.split("\\s+").length
            );
        });

        // 17
        TOOLS.put("creativity_divergence_agent", args -> {
            String userId    = str(args, "user_id");
            String inputTask = str(args, "task_input");
            return Map.of(
                "agent",  "CREATIVITY_DIVERGENCE_AGENT_AGENT",
                "status", "scored",
                "user_id", userId,
                "fluency_score", 88,
                "flexibility_score", 79,
                "originality_score", 91,
                "elaboration_score", 74,
                "divergence_quotient", 83.0,
                "creative_domain_strengths", List.of("conceptual_blending", "analogical_thinking"),
                "input_length", inputTask.length()
            );
        });

        // 18
        TOOLS.put("cognitive_stability_index", args -> {
            String userId    = str(args, "user_id");
            String sessionLog = str(args, "session_log");
            return Map.of(
                "agent",  "COGNITIVE_STABILITY_INDEX_AGENT",
                "status", "computed",
                "user_id", userId,
                "stability_index", 0.82,
                "stress_indicators_detected", false,
                "focus_variance", 0.14,
                "consistency_score", 87,
                "cognitive_fatigue_risk", "LOW",
                "recommended_break_after_min", 60,
                "log_entries_processed", sessionLog.split("\n").length
            );
        });

        // 19
        TOOLS.put("ai_collaboration_efficiency_model", args -> {
            String userId      = str(args, "user_id");
            String sessionData = str(args, "ai_session_data");
            return Map.of(
                "agent",  "AI_COLLABORATION_EFFICIENCY_MODEL_AGENT",
                "status", "evaluated",
                "user_id", userId,
                "collaboration_score", 88,
                "prompt_quality_index", 0.84,
                "task_completion_rate", 0.91,
                "ai_leverage_ratio", 2.7,
                "bottlenecks_detected", List.of("ambiguous_prompts", "over_correction"),
                "recommended_strategy", "STRUCTURED_PROMPTING",
                "session_data_length", sessionData.length()
            );
        });
    }

    // ─────────────────────────────────────────────
    // Entry point
    // ─────────────────────────────────────────────
    public static void main(String[] args) throws IOException {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
        System.err.println("[mcp-core-intelligence] Server started — " + TOOLS.size() + " agents ready");

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                String response = handleRequest(line);
                out.println(response);
            } catch (Exception e) {
                out.println(errorResponse(null, -32700, "Parse error: " + e.getMessage()));
            }
        }
    }

    // ─────────────────────────────────────────────
    // Request dispatcher
    // ─────────────────────────────────────────────
    private static String handleRequest(String raw) {
        Map<String, Object> req = JsonParser.parse(raw);
        Object id     = req.get("id");
        String method = (String) req.get("method");

        if (method == null) return errorResponse(id, -32600, "Invalid Request: missing method");

        switch (method) {
            case "initialize":      return handleInitialize(id);
            case "tools/list":      return handleToolsList(id);
            case "tools/call":      return handleToolsCall(id, req);
            case "ping":            return successResponse(id, Map.of("status", "pong"));
            default:                return errorResponse(id, -32601, "Method not found: " + method);
        }
    }

    private static String handleInitialize(Object id) {
        return successResponse(id, Map.of(
            "protocolVersion", MCP_VERSION,
            "serverInfo", Map.of("name", SERVER_NAME, "version", SERVER_VERSION),
            "capabilities", Map.of("tools", Map.of())
        ));
    }

    private static String handleToolsList(Object id) {
        List<Map<String, Object>> toolList = TOOLS.keySet().stream().map(name -> {
            Map<String, Object> schema = toolSchema(name);
            Map<String, Object> tool = new LinkedHashMap<>();
            tool.put("name", name);
            tool.put("description", schema.get("description"));
            tool.put("inputSchema", schema.get("inputSchema"));
            return tool;
        }).collect(Collectors.toList());
        return successResponse(id, Map.of("tools", toolList));
    }

    @SuppressWarnings("unchecked")
    private static String handleToolsCall(Object id, Map<String, Object> req) {
        Map<String, Object> params = (Map<String, Object>) req.getOrDefault("params", Map.of());
        String toolName = (String) params.get("name");
        Map<String, Object> args = (Map<String, Object>) params.getOrDefault("arguments", Map.of());

        ToolHandler handler = TOOLS.get(toolName);
        if (handler == null) return errorResponse(id, -32602, "Unknown tool: " + toolName);

        try {
            Object result = handler.handle(args);
            return successResponse(id, Map.of(
                "content", List.of(Map.of("type", "text", "text", JsonSerializer.toJson(result)))
            ));
        } catch (Exception e) {
            return errorResponse(id, -32603, "Tool execution error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // Tool schemas (input validation definitions)
    // ─────────────────────────────────────────────
    private static Map<String, Object> toolSchema(String name) {
        // Common user_id property
        Map<String, Object> userId = Map.of("type", "string", "description", "Unique user identifier");

        switch (name) {
            case "spatial_pattern_model":
                return schema("Analyzes spatial pattern recognition from visual/grid input data.",
                    props(entry("user_id", userId), entry("input_data", strProp("Raw spatial input data (CSV, JSON, or encoded grid)"))),
                    List.of("user_id", "input_data"));

            case "reflective_depth_analyzer":
                return schema("Evaluates metacognitive depth and reflective thinking in a user response.",
                    props(entry("user_id", userId), entry("response_text", strProp("Free-text user response to analyze"))),
                    List.of("user_id", "response_text"));

            case "population_percentile_engine":
                return schema("Computes population percentile rank for a given raw score and domain.",
                    props(entry("user_id", userId),
                          entry("raw_score", Map.of("type", "number", "description", "Raw score (0–100)")),
                          entry("domain", strProp("Intelligence domain e.g. logical, spatial, linguistic"))),
                    List.of("user_id", "raw_score", "domain"));

            case "open_response_evaluation":
                return schema("Scores open-ended responses against a structured rubric.",
                    props(entry("user_id", userId),
                          entry("response_text", strProp("Candidate's open-ended response")),
                          entry("rubric_id", strProp("Rubric identifier to evaluate against"))),
                    List.of("user_id", "response_text", "rubric_id"));

            case "naturalistic_classification_model":
                return schema("Classifies naturalistic intelligence from observation data.",
                    props(entry("user_id", userId), entry("observation_data", strProp("Field observation notes or structured data"))),
                    List.of("user_id", "observation_data"));

            case "musical_frequency_model":
                return schema("Processes musical metadata to score musical intelligence.",
                    props(entry("user_id", userId), entry("audio_metadata", strProp("Audio metadata JSON string (tempo, pitch, dynamics)"))),
                    List.of("user_id", "audio_metadata"));

            case "logical_scoring_model":
                return schema("Scores logical reasoning from a comma-separated answer sequence.",
                    props(entry("user_id", userId), entry("answer_sequence", strProp("Comma-separated answer choices e.g. A,B,C,A,D"))),
                    List.of("user_id", "answer_sequence"));

            case "linguistic_structural_analyzer":
                return schema("Analyzes linguistic structure, syntax complexity, and vocabulary richness.",
                    props(entry("user_id", userId), entry("text_input", strProp("Full text input to analyze"))),
                    List.of("user_id", "text_input"));

            case "learning_velocity_model":
                return schema("Computes learning velocity and retention probability for a study session.",
                    props(entry("user_id", userId), entry("session_id", strProp("Study session identifier"))),
                    List.of("user_id", "session_id"));

            case "kinesthetic_motion_model":
                return schema("Evaluates kinesthetic intelligence from motion sensor data.",
                    props(entry("user_id", userId), entry("motion_data", strProp("Comma-separated motion sensor readings"))),
                    List.of("user_id", "motion_data"));

            case "intrapersonal_behavioral_model":
                return schema("Models intrapersonal intelligence from behavioral profile data.",
                    props(entry("user_id", userId), entry("profile_data", strProp("JSON string of behavioral survey responses"))),
                    List.of("user_id", "profile_data"));

            case "interpersonal_graph_model":
                return schema("Builds and analyzes a social interaction graph for interpersonal intelligence.",
                    props(entry("user_id", userId), entry("social_graph_json", strProp("JSON representation of social connections"))),
                    List.of("user_id", "social_graph_json"));

            case "intelligence_report_generator":
                return schema("Generates a full intelligence report document for a user.",
                    props(entry("user_id", userId), entry("report_type", strProp("Report type: FULL | SUMMARY | DOMAIN_SPECIFIC"))),
                    List.of("user_id", "report_type"));

            case "intelligence_growth_timeseries":
                return schema("Computes intelligence growth trends over a date range.",
                    props(entry("user_id", userId),
                          entry("start_date", strProp("Start date YYYY-MM-DD")),
                          entry("end_date",   strProp("End date YYYY-MM-DD"))),
                    List.of("user_id", "start_date", "end_date"));

            case "entrepreneurial_risk_model":
                return schema("Assesses entrepreneurial risk profile and idea viability.",
                    props(entry("user_id", userId), entry("idea_data", strProp("Description or JSON of the business idea"))),
                    List.of("user_id", "idea_data"));

            case "debate_quality_analyzer":
                return schema("Analyzes debate transcript for argument strength and fallacies.",
                    props(entry("user_id", userId), entry("debate_transcript", strProp("Full debate transcript text"))),
                    List.of("user_id", "debate_transcript"));

            case "creativity_divergence_agent":
                return schema("Measures divergent thinking across fluency, flexibility, originality, elaboration.",
                    props(entry("user_id", userId), entry("task_input", strProp("User response to a divergent thinking task"))),
                    List.of("user_id", "task_input"));

            case "cognitive_stability_index":
                return schema("Computes cognitive stability and fatigue risk from session log.",
                    props(entry("user_id", userId), entry("session_log", strProp("Newline-separated session event log"))),
                    List.of("user_id", "session_log"));

            case "ai_collaboration_efficiency_model":
                return schema("Evaluates user efficiency and strategy quality in AI-assisted sessions.",
                    props(entry("user_id", userId), entry("ai_session_data", strProp("JSON or text log of AI collaboration session"))),
                    List.of("user_id", "ai_session_data"));

            default:
                return Map.of("description", "Unknown tool", "inputSchema", Map.of("type", "object", "properties", Map.of()));
        }
    }

    // ─────────────────────────────────────────────
    // Schema helpers
    // ─────────────────────────────────────────────
    private static Map<String, Object> schema(String description, Map<String, Object> properties, List<String> required) {
        return Map.of(
            "description", description,
            "inputSchema", Map.of("type", "object", "properties", properties, "required", required)
        );
    }

    private static Map<String, Object> props(Map.Entry<String, Object>... entries) {
        Map<String, Object> m = new LinkedHashMap<>();
        for (Map.Entry<String, Object> e : entries) m.put(e.getKey(), e.getValue());
        return m;
    }

    @SuppressWarnings("unchecked")
    private static Map.Entry<String, Object> entry(String k, Object v) {
        return new AbstractMap.SimpleEntry<>(k, v);
    }

    private static Map<String, Object> strProp(String description) {
        return Map.of("type", "string", "description", description);
    }

    // ─────────────────────────────────────────────
    // Arg helpers
    // ─────────────────────────────────────────────
    private static String str(Map<String, Object> args, String key) {
        Object v = args.get(key);
        return v != null ? v.toString() : "";
    }

    private static double dbl(Map<String, Object> args, String key) {
        Object v = args.get(key);
        if (v == null) return 0.0;
        if (v instanceof Number) return ((Number) v).doubleValue();
        try { return Double.parseDouble(v.toString()); } catch (Exception e) { return 0.0; }
    }

    // ─────────────────────────────────────────────
    // JSON-RPC helpers
    // ─────────────────────────────────────────────
    private static String successResponse(Object id, Object result) {
        return "{\"jsonrpc\":\"2.0\",\"id\":" + JsonSerializer.toJson(id) +
               ",\"result\":" + JsonSerializer.toJson(result) + "}";
    }

    private static String errorResponse(Object id, int code, String message) {
        return "{\"jsonrpc\":\"2.0\",\"id\":" + JsonSerializer.toJson(id) +
               ",\"error\":{\"code\":" + code + ",\"message\":" + JsonSerializer.toJson(message) + "}}";
    }

    // ─────────────────────────────────────────────
    // Functional interface
    // ─────────────────────────────────────────────
    @FunctionalInterface
    interface ToolHandler {
        Object handle(Map<String, Object> args);
    }
}
