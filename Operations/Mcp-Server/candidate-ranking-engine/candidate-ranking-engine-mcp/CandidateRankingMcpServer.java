package com.ecoskiller.ranking;

import com.ecoskiller.ranking.security.SecurityManager;
import com.ecoskiller.ranking.agents.*;
import com.google.gson.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * Candidate Ranking Engine — MCP Server
 * Ecoskiller Platform | Multi-dimensional Candidate Scoring & Ranking
 *
 * Aggregates raw assessment signals (GD, Interview, Dojo) into
 * normalized weighted composite scores, powers recruiter leaderboards,
 * shortlisting workflows, and belt certification triggers.
 *
 * Transport:   stdio (stdin/stdout)
 * Format:      JSON-RPC 2.0
 * MCP Version: 2024-11-05
 * Security:    HMAC-SHA256, input sanitisation, tool allowlist, rate limiting, audit log
 */
public class CandidateRankingMcpServer {

    private static final Logger LOGGER = Logger.getLogger(CandidateRankingMcpServer.class.getName());
    private static final String MCP_VERSION    = "2024-11-05";
    private static final String SERVER_NAME    = "candidate-ranking-engine-mcp";
    private static final String SERVER_VERSION = "1.0.0";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final SecurityManager security = new SecurityManager();
    private final Map<String, McpAgent> agents = new LinkedHashMap<>();

    public CandidateRankingMcpServer() {
        // ── Core scoring & ranking ─────────────────────────────────────────────
        agents.put("score_aggregator",             new ScoreAggregatorAgent(security));
        agents.put("weighted_score_calculator",    new WeightedScoreCalculatorAgent(security));
        agents.put("rank_computer",                new RankComputerAgent(security));
        agents.put("tie_resolver",                 new TieResolverAgent(security));
        agents.put("dimension_score_normalizer",   new DimensionScoreNormalizerAgent(security));

        // ── Intelligence profile ───────────────────────────────────────────────
        agents.put("intelligence_signal_ingester", new IntelligenceSignalIngesterAgent(security));
        agents.put("xi_vector_integrator",         new XiVectorIntegratorAgent(security));

        // ── Leaderboard & shortlisting ─────────────────────────────────────────
        agents.put("leaderboard_manager",          new LeaderboardManagerAgent(security));
        agents.put("shortlist_generator",          new ShortlistGeneratorAgent(security));
        agents.put("cohort_stats_calculator",      new CohortStatsCalculatorAgent(security));

        // ── Kafka event lifecycle ──────────────────────────────────────────────
        agents.put("kafka_event_consumer",         new KafkaEventConsumerAgent(security));
        agents.put("kafka_event_publisher",        new KafkaEventPublisherAgent(security));
        agents.put("dlq_handler",                  new DlqHandlerAgent(security));

        // ── Belt & certification ───────────────────────────────────────────────
        agents.put("belt_eligibility_evaluator",   new BeltEligibilityEvaluatorAgent(security));

        // ── Recomputation & corrections ────────────────────────────────────────
        agents.put("rank_recomputer",              new RankRecomputerAgent(security));
        agents.put("score_correction_handler",     new ScoreCorrectionHandlerAgent(security));

        // ── Observability & config ─────────────────────────────────────────────
        agents.put("metrics_reporter",             new MetricsReporterAgent(security));
        agents.put("weight_matrix_manager",        new WeightMatrixManagerAgent(security));
    }

    // ── Server Loop ───────────────────────────────────────────────────────────

    public void run() throws IOException {
        LOGGER.info("[CandidateRankingMcpServer] Starting " + SERVER_NAME + " v" + SERVER_VERSION);
        LOGGER.info("[CandidateRankingMcpServer] " + agents.size() + " agents registered");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            writer.println(handleRequest(line));
            writer.flush();
        }
    }

    // ── Request Dispatch ──────────────────────────────────────────────────────

    private String handleRequest(String rawJson) {
        JsonObject req;
        try {
            req = JsonParser.parseString(rawJson).getAsJsonObject();
        } catch (Exception e) {
            return errorResponse(null, -32700, "Parse error: " + e.getMessage());
        }

        if (!security.validateRpcStructure(req)) {
            return errorResponse(getIdSafe(req), -32600, "Invalid Request");
        }

        String  method = req.get("method").getAsString();
        JsonElement id = req.get("id");
        JsonElement params = req.has("params") ? req.get("params") : new JsonObject();

        try {
            return switch (method) {
                case "initialize"  -> handleInitialize(id);
                case "tools/list"  -> handleToolsList(id);
                case "tools/call"  -> handleToolsCall(id, params);
                case "ping"        -> successResponse(id, new JsonObject());
                default            -> errorResponse(id, -32601, "Method not found: " + method);
            };
        } catch (SecurityException se) {
            LOGGER.warning("[SECURITY] " + se.getMessage());
            return errorResponse(id, -32001, "Security violation: " + se.getMessage());
        } catch (Exception e) {
            LOGGER.severe("[ERROR] " + e.getMessage());
            return errorResponse(id, -32603, "Internal error: " + e.getMessage());
        }
    }

    private String handleInitialize(JsonElement id) {
        JsonObject result = new JsonObject();
        result.addProperty("protocolVersion", MCP_VERSION);
        JsonObject info = new JsonObject();
        info.addProperty("name",    SERVER_NAME);
        info.addProperty("version", SERVER_VERSION);
        result.add("serverInfo", info);
        JsonObject caps = new JsonObject();
        caps.add("tools", new JsonObject());
        result.add("capabilities", caps);
        return successResponse(id, result);
    }

    private String handleToolsList(JsonElement id) {
        JsonArray tools = new JsonArray();
        for (McpAgent a : agents.values()) tools.add(a.getToolDefinition());
        JsonObject result = new JsonObject();
        result.add("tools", tools);
        return successResponse(id, result);
    }

    private String handleToolsCall(JsonElement id, JsonElement params) {
        JsonObject p = params.getAsJsonObject();
        if (!p.has("name")) return errorResponse(id, -32602, "Missing tool name");

        String toolName = p.get("name").getAsString();
        if (!security.isAllowedTool(toolName))
            return errorResponse(id, -32001, "Tool not permitted: " + toolName);

        McpAgent agent = agents.get(toolName);
        if (agent == null) return errorResponse(id, -32601, "Unknown tool: " + toolName);

        JsonObject args      = p.has("arguments") ? p.get("arguments").getAsJsonObject() : new JsonObject();
        JsonObject sanitized = security.sanitizeInputs(args);
        security.auditLog(toolName, sanitized);

        JsonObject toolResult = agent.execute(sanitized);

        JsonObject result = new JsonObject();
        JsonArray  content = new JsonArray();
        JsonObject text = new JsonObject();
        text.addProperty("type", "text");
        text.addProperty("text", gson.toJson(toolResult));
        content.add(text);
        result.add("content", content);
        result.addProperty("isError", toolResult.has("error"));
        return successResponse(id, result);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String successResponse(JsonElement id, JsonObject result) {
        JsonObject r = new JsonObject();
        r.addProperty("jsonrpc", "2.0");
        r.add("id", id);
        r.add("result", result);
        return gson.toJson(r);
    }

    private String errorResponse(JsonElement id, int code, String message) {
        JsonObject r = new JsonObject();
        r.addProperty("jsonrpc", "2.0");
        r.add("id", id != null ? id : JsonNull.INSTANCE);
        JsonObject err = new JsonObject();
        err.addProperty("code",    code);
        err.addProperty("message", message);
        r.add("error", err);
        return gson.toJson(r);
    }

    private JsonElement getIdSafe(JsonObject req) {
        return req.has("id") ? req.get("id") : JsonNull.INSTANCE;
    }

    public static void main(String[] args) throws IOException {
        // All logging → stderr only. stdout is exclusively the MCP channel.
        Logger root = Logger.getLogger("");
        root.setLevel(Level.INFO);
        for (Handler h : root.getHandlers()) h.setStream(System.err);
        new CandidateRankingMcpServer().run();
    }
}
