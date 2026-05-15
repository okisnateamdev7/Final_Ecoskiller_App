package com.ecoskiller.dojo;

import com.ecoskiller.dojo.security.SecurityManager;
import com.ecoskiller.dojo.agents.*;
import com.google.gson.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * Dojo Match Engine MCP Server
 * Ecoskiller | CAT-DOJO — Real-Time Candidate-Interview Matching & Session Allocation
 *
 * Transport: stdio (stdin/stdout)
 * Format:    JSON-RPC 2.0
 * MCP Version: 2024-11-05
 * Security:  HMAC-SHA256 request signing, input sanitization, audit logging
 */
public class DojoMcpServer {

    private static final Logger LOGGER = Logger.getLogger(DojoMcpServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "dojo-match-engine";
    private static final String SERVER_VERSION = "1.0.0";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final SecurityManager security = new SecurityManager();
    private final Map<String, McpAgent> agents = new LinkedHashMap<>();

    public DojoMcpServer() {
        registerAgents();
    }

    private void registerAgents() {
        agents.put("find_match",                  new FindMatchAgent(security));
        agents.put("candidate_queue",             new CandidateQueueAgent(security));
        agents.put("interviewer_availability",    new InterviewerAvailabilityAgent(security));
        agents.put("skill_compatibility_filter",  new SkillCompatibilityFilterAgent(security));
        agents.put("timezone_constraint_check",   new TimezoneConstraintCheckAgent(security));
        agents.put("fairness_optimizer",          new FairnessOptimizerAgent(security));
        agents.put("match_score_calculator",      new MatchScoreCalculatorAgent(security));
        agents.put("session_initiation",          new SessionInitiationAgent(security));
        agents.put("no_show_handler",             new NoShowHandlerAgent(security));
        agents.put("load_balancer",               new LoadBalancerAgent(security));
        agents.put("kafka_event_publisher",       new KafkaEventPublisherAgent(security));
        agents.put("match_result_publisher",      new MatchResultPublisherAgent(security));
        agents.put("fairness_audit_log",          new FairnessAuditLogAgent(security));
        agents.put("metrics_collector",           new MetricsCollectorAgent(security));
        agents.put("fallback_match_engine",       new FallbackMatchEngineAgent(security));
        agents.put("interview_history_tracker",   new InterviewHistoryTrackerAgent(security));
        agents.put("concurrent_capacity_manager", new ConcurrentCapacityManagerAgent(security));
        agents.put("match_confidence_scorer",     new MatchConfidenceScorerAgent(security));
    }

    public void run() throws IOException {
        LOGGER.info("[DojoMcpServer] Starting " + SERVER_NAME + " v" + SERVER_VERSION);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer  = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String response = handleRequest(line);
            writer.println(response);
            writer.flush();
        }
    }

    private String handleRequest(String rawJson) {
        JsonObject req;
        try {
            req = JsonParser.parseString(rawJson).getAsJsonObject();
        } catch (Exception e) {
            return errorResponse(null, -32700, "Parse error: " + e.getMessage());
        }

        // Security: sanitize and validate JSON-RPC structure
        if (!security.validateRpcStructure(req)) {
            return errorResponse(getIdSafe(req), -32600, "Invalid Request");
        }

        String method = req.get("method").getAsString();
        JsonElement id = req.get("id");
        JsonElement params = req.has("params") ? req.get("params") : new JsonObject();

        try {
            return switch (method) {
                case "initialize"  -> handleInitialize(id, params);
                case "tools/list"  -> handleToolsList(id);
                case "tools/call"  -> handleToolsCall(id, params);
                case "ping"        -> handlePing(id);
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

    // ─── MCP Handlers ──────────────────────────────────────────────────────────

    private String handleInitialize(JsonElement id, JsonElement params) {
        JsonObject result = new JsonObject();
        result.addProperty("protocolVersion", MCP_VERSION);

        JsonObject serverInfo = new JsonObject();
        serverInfo.addProperty("name",    SERVER_NAME);
        serverInfo.addProperty("version", SERVER_VERSION);
        result.add("serverInfo", serverInfo);

        JsonObject capabilities = new JsonObject();
        capabilities.add("tools", new JsonObject());
        result.add("capabilities", capabilities);

        return successResponse(id, result);
    }

    private String handleToolsList(JsonElement id) {
        JsonArray tools = new JsonArray();
        for (McpAgent agent : agents.values()) {
            tools.add(agent.getToolDefinition());
        }
        JsonObject result = new JsonObject();
        result.add("tools", tools);
        return successResponse(id, result);
    }

    private String handleToolsCall(JsonElement id, JsonElement params) {
        JsonObject p = params.getAsJsonObject();

        if (!p.has("name")) {
            return errorResponse(id, -32602, "Missing tool name");
        }

        String toolName = p.get("name").getAsString();

        // Security: validate tool name against allowlist
        if (!security.isAllowedTool(toolName)) {
            return errorResponse(id, -32001, "Tool not permitted: " + toolName);
        }

        McpAgent agent = agents.get(toolName);
        if (agent == null) {
            return errorResponse(id, -32601, "Unknown tool: " + toolName);
        }

        JsonObject args = p.has("arguments") ? p.get("arguments").getAsJsonObject() : new JsonObject();

        // Security: sanitize all input arguments
        JsonObject sanitized = security.sanitizeInputs(args);

        // Audit log
        security.auditLog(toolName, sanitized);

        JsonObject toolResult = agent.execute(sanitized);

        JsonObject result = new JsonObject();
        JsonArray content = new JsonArray();
        JsonObject textContent = new JsonObject();
        textContent.addProperty("type", "text");
        textContent.addProperty("text", gson.toJson(toolResult));
        content.add(textContent);
        result.add("content", content);
        result.addProperty("isError", toolResult.has("error"));

        return successResponse(id, result);
    }

    private String handlePing(JsonElement id) {
        return successResponse(id, new JsonObject());
    }

    // ─── Response helpers ──────────────────────────────────────────────────────

    private String successResponse(JsonElement id, JsonObject result) {
        JsonObject resp = new JsonObject();
        resp.addProperty("jsonrpc", "2.0");
        resp.add("id", id);
        resp.add("result", result);
        return gson.toJson(resp);
    }

    private String errorResponse(JsonElement id, int code, String message) {
        JsonObject resp = new JsonObject();
        resp.addProperty("jsonrpc", "2.0");
        resp.add("id", id != null ? id : JsonNull.INSTANCE);
        JsonObject error = new JsonObject();
        error.addProperty("code", code);
        error.addProperty("message", message);
        resp.add("error", error);
        return gson.toJson(resp);
    }

    private JsonElement getIdSafe(JsonObject req) {
        return req.has("id") ? req.get("id") : JsonNull.INSTANCE;
    }

    public static void main(String[] args) throws IOException {
        // Configure logging to stderr (never stdout — stdout is MCP channel)
        System.setErr(System.err);
        Logger root = Logger.getLogger("");
        root.setLevel(Level.INFO);
        for (Handler h : root.getHandlers()) {
            h.setStream(System.err);
        }

        new DojoMcpServer().run();
    }
}
