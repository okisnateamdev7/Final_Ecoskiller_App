package com.ecoskiller.dojo.server;

import com.ecoskiller.dojo.agents.*;
import com.ecoskiller.dojo.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.logging.*;

/**
 * Ecoskiller Dojo MCP Server
 * CAT-16 — Dojo, Growth & School Ops
 * MCP Server: mcp-16-dojo  |  Namespace: realtime
 *
 * Agents (18):
 *   1.  XP_ENGINE_AGENT
 *   2.  TEACHER_INCENTIVE_AGENT
 *   3.  STREAK_ENGINE_AGENT
 *   4.  SHARE_CARD_GENERATOR_AGENT
 *   5.  SCHOOL_PERFORMANCE_ANALYTICS_AGENT
 *   6.  REPUTATION_SCORE_AGENT
 *   7.  RANK_COMPUTATION_AGENT
 *   8.  PROBLEM_DETECTION_SCORING_AGENT
 *   9.  PASSPORT_HISTORY_AGENT
 *  10.  PASSPORT_AGGREGATION_AGENT
 *  11.  LEVEL_PROGRESSION_AGENT
 *  12.  INVITE_TRACKING_AGENT
 *  13.  INNOVATION_SKILL_SCORING_AGENT
 *  14.  GD_SESSION_SCHEDULER_AGENT
 *  15.  GD_POST_SESSION_ANALYTICS_AGENT
 *  16.  GD_ATTENDANCE_TRACKING_AGENT
 *  17.  DIFFICULTY_ADAPTATION_AGENT
 *  18.  DAILY_MISSION_AGENT
 */
public class DojoMcpServer {

    static final String SERVER_NAME    = "mcp-16-dojo";
    static final String SERVER_VERSION = "1.0.0";
    static final String NAMESPACE      = "realtime";

    private static final Logger LOGGER = Logger.getLogger(DojoMcpServer.class.getName());

    private final ObjectMapper  mapper   = new ObjectMapper();
    private final AgentRegistry registry = new AgentRegistry();

    public DojoMcpServer() { registerAllAgents(); }

    // ──────────────────────────────────────────────────────────────────────
    // Agent registration (all 18 agents)
    // ──────────────────────────────────────────────────────────────────────

    private void registerAllAgents() {
        // Group 1 — XP, Incentive, Streak, Share, Analytics, Reputation
        registry.register(new XpEngineAgent());
        registry.register(new TeacherIncentiveAgent());
        registry.register(new StreakEngineAgent());
        registry.register(new ShareCardGeneratorAgent());
        registry.register(new SchoolPerformanceAnalyticsAgent());
        registry.register(new ReputationScoreAgent());
        // Group 2 — Rank, Problem Scoring, Passport, Level, Invite
        registry.register(new RankComputationAgent());
        registry.register(new ProblemDetectionScoringAgent());
        registry.register(new PassportHistoryAgent());
        registry.register(new PassportAggregationAgent());
        registry.register(new LevelProgressionAgent());
        registry.register(new InviteTrackingAgent());
        // Group 3 — Innovation, GD, Difficulty, Daily Mission
        registry.register(new InnovationSkillScoringAgent());
        registry.register(new GdSessionSchedulerAgent());
        registry.register(new GdPostSessionAnalyticsAgent());
        registry.register(new GdAttendanceTrackingAgent());
        registry.register(new DifficultyAdaptationAgent());
        registry.register(new DailyMissionAgent());
        LOGGER.info("[" + SERVER_NAME + "] Registered " + registry.count() + "/18 agents.");
    }

    // ──────────────────────────────────────────────────────────────────────
    // MCP Protocol Loop (stdio)
    // ──────────────────────────────────────────────────────────────────────

    public void run() throws Exception {
        LOGGER.info("[" + SERVER_NAME + "] Starting on stdio transport...");
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String response = handleMessage(line);
            if (response != null) out.println(response);
        }
    }

    // ──────────────────────────────────────────────────────────────────────
    // Message dispatch
    // ──────────────────────────────────────────────────────────────────────

    String handleMessage(String rawJson) {
        try {
            ObjectNode req = (ObjectNode) mapper.readTree(rawJson);
            String method  = req.path("method").asText("");
            Object id      = req.has("id") ? req.get("id") : null;

            return switch (method) {
                case "initialize"     -> handleInitialize(id);
                case "tools/list"     -> handleToolsList(id);
                case "tools/call"     -> handleToolCall(req, id);
                case "resources/list" -> buildResult(id, mapper.createObjectNode().set("resources", mapper.createArrayNode()));
                case "prompts/list"   -> buildResult(id, mapper.createObjectNode().set("prompts",   mapper.createArrayNode()));
                case "ping"           -> buildResult(id, mapper.createObjectNode());
                default               -> buildError(id, -32601, "Method not found: " + method);
            };
        } catch (Exception e) {
            return buildError(null, -32700, "Parse error: " + e.getMessage());
        }
    }

    // ──────────────────────────────────────────────────────────────────────
    // Handlers
    // ──────────────────────────────────────────────────────────────────────

    private String handleInitialize(Object id) {
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", "2024-11-05");
        ObjectNode info = mapper.createObjectNode();
        info.put("name",    SERVER_NAME);
        info.put("version", SERVER_VERSION);
        result.set("serverInfo", info);
        ObjectNode caps = mapper.createObjectNode();
        caps.set("tools",     mapper.createObjectNode().put("listChanged", false));
        caps.set("resources", mapper.createObjectNode().put("subscribe",   false));
        caps.set("prompts",   mapper.createObjectNode().put("listChanged", false));
        result.set("capabilities", caps);
        return buildResult(id, result);
    }

    private String handleToolsList(Object id) {
        ArrayNode tools = mapper.createArrayNode();
        for (BaseAgent agent : registry.all())
            for (McpTool tool : agent.getTools())
                tools.add(tool.toJson(mapper));
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", tools);
        return buildResult(id, result);
    }

    private String handleToolCall(ObjectNode req, Object id) {
        try {
            ObjectNode params   = (ObjectNode) req.path("params");
            String     toolName = params.path("name").asText("");
            ObjectNode args     = params.has("arguments")
                ? (ObjectNode) params.get("arguments")
                : mapper.createObjectNode();

            for (BaseAgent agent : registry.all())
                for (McpTool tool : agent.getTools())
                    if (tool.getName().equals(toolName)) {
                        AgentResponse resp = agent.execute(toolName, args);
                        return buildToolResult(id, resp);
                    }

            return buildError(id, -32602, "Tool not found: " + toolName);
        } catch (Exception e) {
            return buildError(id, -32603, "Tool execution error: " + e.getMessage());
        }
    }

    // ──────────────────────────────────────────────────────────────────────
    // JSON-RPC helpers
    // ──────────────────────────────────────────────────────────────────────

    private String buildResult(Object id, ObjectNode result) {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        setId(resp, id);
        resp.set("result", result);
        return resp.toString();
    }

    private String buildToolResult(Object id, AgentResponse agentResp) {
        ObjectNode resp    = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        setId(resp, id);
        ObjectNode result  = mapper.createObjectNode();
        ArrayNode  content = mapper.createArrayNode();
        ObjectNode text    = mapper.createObjectNode();
        text.put("type", "text");
        text.put("text", agentResp.toJson(mapper));
        content.add(text);
        result.set("content", content);
        result.put("isError", agentResp.isError());
        resp.set("result", result);
        return resp.toString();
    }

    private String buildError(Object id, int code, String message) {
        ObjectNode resp  = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        setId(resp, id);
        ObjectNode error = mapper.createObjectNode();
        error.put("code",    code);
        error.put("message", message);
        resp.set("error", error);
        return resp.toString();
    }

    private void setId(ObjectNode resp, Object id) {
        if (id instanceof Integer i) resp.put("id", i);
        else if (id instanceof String s) resp.put("id", s);
        else resp.putNull("id");
    }

    // ──────────────────────────────────────────────────────────────────────
    // Entry point
    // ──────────────────────────────────────────────────────────────────────

    public static void main(String[] args) throws Exception {
        setupLogging();
        new DojoMcpServer().run();
    }

    private static void setupLogging() {
        LogManager.getLogManager().reset();
        Logger root = Logger.getLogger("");
        Handler h   = new StreamHandler(System.err, new SimpleFormatter());
        h.setLevel(Level.ALL);
        root.addHandler(h);
        root.setLevel(Level.INFO);
    }
}
