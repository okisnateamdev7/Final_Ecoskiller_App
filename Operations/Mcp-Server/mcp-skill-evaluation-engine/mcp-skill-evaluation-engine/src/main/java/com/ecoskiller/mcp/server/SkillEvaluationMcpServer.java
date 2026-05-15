package com.ecoskiller.mcp.server;

import com.ecoskiller.mcp.config.ServerConfig;
import com.ecoskiller.mcp.security.JwtValidator;
import com.ecoskiller.mcp.security.RateLimiter;
import com.ecoskiller.mcp.tools.*;
import com.ecoskiller.mcp.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * Ecoskiller | Skill Evaluation Engine — MCP Server (Java)
 * CAT-SEE — Competency Assessment & Real-Time Evaluation
 *
 * Protocol: JSON-RPC 2.0 over stdio
 * MCP Version: 2024-11-05
 * Security: JWT validation, rate limiting, input sanitization, audit logging
 */
public class SkillEvaluationMcpServer {

    private static final Logger LOGGER = Logger.getLogger(SkillEvaluationMcpServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "mcp-skill-evaluation-engine";
    private static final String SERVER_VERSION = "1.0.0";

    private final ObjectMapper mapper = JsonUtil.createSecureMapper();
    private final Map<String, McpTool> tools = new LinkedHashMap<>();
    private final JwtValidator jwtValidator;
    private final RateLimiter rateLimiter;
    private final ServerConfig config;

    public SkillEvaluationMcpServer() {
        this.config = ServerConfig.load();
        this.jwtValidator = new JwtValidator(config);
        this.rateLimiter = new RateLimiter(config.getRateLimitPerMinute());
        registerTools();
        setupLogging();
        LOGGER.info("SkillEvaluationMcpServer initialized with " + tools.size() + " tools");
    }

    private void registerTools() {
        // 1. Core Evaluation Tools
        tools.put("evaluate_session",          new EvaluateSessionTool());
        tools.put("evaluate_candidate",        new EvaluateCandidateTool());
        tools.put("get_competency_scores",     new GetCompetencyScoresTool());
        tools.put("get_competency_history",    new GetCompetencyHistoryTool());

        // 2. Signal Processing Tools
        tools.put("process_speech_signal",     new ProcessSpeechSignalTool());
        tools.put("process_code_submission",   new ProcessCodeSubmissionTool());
        tools.put("process_behavioral_signal", new ProcessBehavioralSignalTool());
        tools.put("extract_nlp_features",      new ExtractNlpFeaturesTool());

        // 3. Session Management Tools
        tools.put("start_evaluation_session",  new StartEvaluationSessionTool());
        tools.put("end_evaluation_session",    new EndEvaluationSessionTool());
        tools.put("get_session_state",         new GetSessionStateTool());
        tools.put("update_session_metadata",   new UpdateSessionMetadataTool());

        // 4. Adaptive Difficulty Tools
        tools.put("adjust_difficulty",         new AdjustDifficultyTool());
        tools.put("get_difficulty_level",      new GetDifficultyLevelTool());

        // 5. Bias & Fairness Tools
        tools.put("detect_bias",               new DetectBiasTool());
        tools.put("get_fairness_report",       new GetFairnessReportTool());

        // 6. Skill Progression Tools
        tools.put("get_skill_progression",     new GetSkillProgressionTool());
        tools.put("detect_skill_regression",   new DetectSkillRegressionTool());

        // 7. Algorithm Configuration Tools
        tools.put("get_competency_framework",  new GetCompetencyFrameworkTool());
        tools.put("update_scoring_weights",    new UpdateScoringWeightsTool());

        // 8. Health & Observability Tools
        tools.put("health_check",              new HealthCheckTool());
        tools.put("get_metrics",               new GetMetricsTool());
    }

    private void setupLogging() {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(config.getLogLevel());
        // Ensure logs go to stderr, not stdout (stdout is reserved for MCP JSON-RPC)
        for (Handler h : rootLogger.getHandlers()) {
            rootLogger.removeHandler(h);
        }
        StreamHandler stderrHandler = new StreamHandler(System.err, new SimpleFormatter());
        stderrHandler.setLevel(config.getLogLevel());
        rootLogger.addHandler(stderrHandler);
    }

    public void run() throws IOException {
        LOGGER.info("MCP Server started. Listening on stdin...");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            try {
                JsonNode request = mapper.readTree(line);
                JsonNode response = handleRequest(request);
                if (response != null) {
                    String responseStr = mapper.writeValueAsString(response);
                    writer.println(responseStr);
                    writer.flush();
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Fatal error processing request", e);
                ObjectNode errResp = mapper.createObjectNode();
                errResp.put("jsonrpc", "2.0");
                errResp.putNull("id");
                ObjectNode err = errResp.putObject("error");
                err.put("code", -32700);
                err.put("message", "Parse error");
                writer.println(mapper.writeValueAsString(errResp));
                writer.flush();
            }
        }
        LOGGER.info("MCP Server shutting down (stdin closed).");
    }

    private JsonNode handleRequest(JsonNode request) {
        String id = request.has("id") ? request.get("id").asText() : null;
        String method = request.has("method") ? request.get("method").asText() : "";

        // Security: Rate limiting per client session
        if (!rateLimiter.allowRequest("global")) {
            return buildError(id, -32000, "Rate limit exceeded. Maximum " +
                    config.getRateLimitPerMinute() + " requests/minute.");
        }

        try {
            return switch (method) {
                case "initialize"   -> handleInitialize(id, request);
                case "tools/list"   -> handleToolsList(id);
                case "tools/call"   -> handleToolCall(id, request);
                case "ping"         -> buildResult(id, mapper.createObjectNode());
                default             -> buildError(id, -32601, "Method not found: " + method);
            };
        } catch (SecurityException se) {
            LOGGER.warning("Security violation: " + se.getMessage());
            return buildError(id, -32001, "Security error: " + se.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Request handling error", e);
            return buildError(id, -32603, "Internal error: " + e.getMessage());
        }
    }

    private JsonNode handleInitialize(String id, JsonNode request) {
        // Validate client JWT token if provided in params
        if (config.isJwtValidationEnabled()) {
            JsonNode params = request.path("params");
            String token = params.path("_auth_token").asText(null);
            if (token != null && !token.isEmpty()) {
                jwtValidator.validate(token); // throws SecurityException on failure
            }
        }

        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);

        ObjectNode serverInfo = result.putObject("serverInfo");
        serverInfo.put("name", SERVER_NAME);
        serverInfo.put("version", SERVER_VERSION);

        ObjectNode capabilities = result.putObject("capabilities");
        capabilities.putObject("tools");

        ObjectNode meta = result.putObject("_meta");
        meta.put("service", "skill-evaluation-engine");
        meta.put("namespace", "analytics");
        meta.put("tier", "Tier2-HotPath");
        meta.put("securityLevel", "HIGH");

        LOGGER.info("Client initialized MCP session");
        return buildResult(id, result);
    }

    private JsonNode handleToolsList(String id) {
        ArrayNode toolList = mapper.createArrayNode();
        for (McpTool tool : tools.values()) {
            toolList.add(tool.getSchema());
        }
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", toolList);
        return buildResult(id, result);
    }

    private JsonNode handleToolCall(String id, JsonNode request) {
        JsonNode params = request.path("params");
        String toolName = params.path("name").asText();
        JsonNode arguments = params.path("arguments");

        // Security: Validate tool name against allowlist
        if (!tools.containsKey(toolName)) {
            LOGGER.warning("Unknown tool call attempted: " + toolName);
            return buildError(id, -32602, "Unknown tool: " + toolName);
        }

        // Security: Validate JWT if required for sensitive tools
        if (config.isJwtValidationEnabled() && isSensitiveTool(toolName)) {
            String token = arguments.path("_auth_token").asText(null);
            if (token == null || token.isEmpty()) {
                return buildError(id, -32001, "Authentication required for tool: " + toolName);
            }
            jwtValidator.validate(token);
        }

        // Security: Validate tenant_id is present and valid for tenant-scoped tools
        if (isTenantScopedTool(toolName)) {
            String tenantId = arguments.path("tenant_id").asText(null);
            if (tenantId == null || tenantId.isBlank() || !isValidTenantId(tenantId)) {
                return buildError(id, -32602, "Valid tenant_id is required");
            }
        }

        LOGGER.info("Tool call: " + toolName + " | tenant: " +
                arguments.path("tenant_id").asText("N/A"));

        McpTool tool = tools.get(toolName);
        try {
            JsonNode toolResult = tool.execute(arguments);
            ObjectNode content = mapper.createObjectNode();
            ArrayNode contentArray = content.putArray("content");
            ObjectNode textContent = contentArray.addObject();
            textContent.put("type", "text");
            textContent.put("text", mapper.writeValueAsString(toolResult));
            return buildResult(id, content);
        } catch (IllegalArgumentException iae) {
            return buildError(id, -32602, "Invalid params: " + iae.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Tool execution error: " + toolName, e);
            return buildError(id, -32603, "Tool error: " + e.getMessage());
        }
    }

    private boolean isSensitiveTool(String toolName) {
        return Set.of("update_scoring_weights", "get_fairness_report",
                "get_competency_framework", "detect_bias").contains(toolName);
    }

    private boolean isTenantScopedTool(String toolName) {
        return !Set.of("health_check", "get_metrics", "ping").contains(toolName);
    }

    private boolean isValidTenantId(String tenantId) {
        // Allowlist: alphanumeric, hyphens, underscores only (prevent injection)
        return tenantId != null && tenantId.matches("^[a-zA-Z0-9_-]{1,64}$");
    }

    private ObjectNode buildResult(String id, JsonNode result) {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.put("id", id);
        resp.set("result", result);
        return resp;
    }

    private ObjectNode buildError(String id, int code, String message) {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.put("id", id);
        ObjectNode error = resp.putObject("error");
        error.put("code", code);
        error.put("message", message);
        return resp;
    }

    public static void main(String[] args) throws IOException {
        SkillEvaluationMcpServer server = new SkillEvaluationMcpServer();
        server.run();
    }
}
