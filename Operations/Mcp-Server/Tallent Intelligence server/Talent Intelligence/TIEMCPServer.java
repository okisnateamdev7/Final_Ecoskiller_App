package com.ecoskiller.tie.mcp;

import com.ecoskiller.tie.mcp.security.JwtValidator;
import com.ecoskiller.tie.mcp.security.AuditLogger;
import com.ecoskiller.tie.mcp.security.RateLimiter;
import com.ecoskiller.tie.mcp.tools.ToolRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Talent Intelligence Engine — MCP Server (Java)
 * Ecoskiller | CAT-TIE — AI/ML Inference & Talent Analytics
 *
 * Transport: stdio (stdin/stdout)
 * Protocol:  JSON-RPC 2.0 / MCP 2024-11-05
 * Security:  JWT validation (Keycloak), RBAC, rate limiting, audit logging
 *
 * Agents (15):
 *   1.  candidate_job_match          — Real-time candidate-to-role match scoring
 *   2.  batch_score                  — Batch evaluation of 100K+ candidates
 *   3.  predict_offer_acceptance     — Offer acceptance probability prediction
 *   4.  predict_retention            — 12-month retention risk prediction
 *   5.  predict_training_duration    — Ramp-up time estimation
 *   6.  talent_market_benchmark      — Salary/skill market analytics
 *   7.  skill_gap_analysis           — Skill gap detection & recommendations
 *   8.  bias_detection               — Fairness & disparate impact monitoring
 *   9.  feature_engineering          — Feature extraction from raw signals
 *  10.  model_deploy                 — Deploy trained model from staging → prod
 *  11.  model_evaluate               — Evaluate model on holdout test set
 *  12.  ab_test_control              — A/B test routing & shadow deployment
 *  13.  explainability_shap          — SHAP value computation for predictions
 *  14.  talent_graph_query           — Skill knowledge graph traversal
 *  15.  compliance_audit             — Fairness audit log & compliance report
 */
public class TIEMCPServer {

    private static final Logger LOG = Logger.getLogger(TIEMCPServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "tie-mcp-server";
    private static final String SERVER_VERSION = "1.0.0";

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, MCPTool> toolRegistry = new LinkedHashMap<>();
    private final JwtValidator jwtValidator;
    private final AuditLogger auditLogger;
    private final RateLimiter rateLimiter;
    private final ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2
    );

    public TIEMCPServer() {
        this.jwtValidator = new JwtValidator();
        this.auditLogger  = new AuditLogger();
        this.rateLimiter  = new RateLimiter(100, 60_000); // 100 calls / min per client
        registerTools();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool Registration
    // ─────────────────────────────────────────────────────────────────────────

    private void registerTools() {
        for (MCPTool tool : ToolRegistry.all()) {
            toolRegistry.put(tool.getName(), tool);
        }
        LOG.info("[TIE-MCP] Registered " + toolRegistry.size() + " tools");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Main Loop — stdin/stdout JSON-RPC 2.0
    // ─────────────────────────────────────────────────────────────────────────

    public void start() throws IOException {
        LOG.info("[TIE-MCP] Starting server v" + SERVER_VERSION);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter   writer  = new PrintWriter(new OutputStreamWriter(System.out), true);
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isBlank()) continue;
            final String request = line;
            // Synchronous per-line processing (MCP stdio protocol)
            String response = handleRawRequest(request);
            if (response != null) {
                writer.println(response);
                writer.flush();
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Request Dispatch
    // ─────────────────────────────────────────────────────────────────────────

    private String handleRawRequest(String raw) {
        try {
            JsonNode req = mapper.readTree(raw);
            String   id  = req.has("id") ? req.get("id").asText() : null;
            String   method = req.path("method").asText("");

            return switch (method) {
                case "initialize"  -> handleInitialize(id, req);
                case "ping"        -> buildResult(id, mapper.createObjectNode());
                case "tools/list"  -> handleToolsList(id, req);
                case "tools/call"  -> handleToolCall(id, req);
                default            -> buildError(id, -32601, "Method not found: " + method);
            };
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Parse error", e);
            return buildError(null, -32700, "Parse error: " + e.getMessage());
        }
    }

    private String handleInitialize(String id, JsonNode req) throws Exception {
        ObjectNode caps = mapper.createObjectNode();
        ObjectNode tools = caps.putObject("tools");
        tools.put("listChanged", false);

        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);
        result.set("capabilities", caps);

        ObjectNode info = result.putObject("serverInfo");
        info.put("name", SERVER_NAME);
        info.put("version", SERVER_VERSION);

        LOG.info("[TIE-MCP] Client initialized: "
                + req.path("params").path("clientInfo").path("name").asText("unknown"));
        return buildResult(id, result);
    }

    private String handleToolsList(String id, JsonNode req) throws Exception {
        // Security: validate JWT on listing (prevents enumeration by unauthenticated clients)
        String clientId = extractClientId(req);
        if (!jwtValidator.validate(req)) {
            auditLogger.warn(clientId, "tools/list", "UNAUTHORIZED");
            return buildError(id, -32001, "Unauthorized: valid JWT required");
        }

        ArrayNode toolsArray = mapper.createArrayNode();
        for (MCPTool tool : toolRegistry.values()) {
            toolsArray.add(tool.getSchema(mapper));
        }
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", toolsArray);
        return buildResult(id, result);
    }

    private String handleToolCall(String id, JsonNode req) throws Exception {
        JsonNode params   = req.path("params");
        String   toolName = params.path("name").asText();
        JsonNode args     = params.path("arguments");
        String   clientId = extractClientId(req);

        // ── 1. Authentication ─────────────────────────────────────────────
        if (!jwtValidator.validate(req)) {
            auditLogger.warn(clientId, "tools/call:" + toolName, "UNAUTHORIZED");
            return buildError(id, -32001, "Unauthorized: valid JWT required");
        }

        // ── 2. Rate Limiting ──────────────────────────────────────────────
        if (!rateLimiter.allow(clientId)) {
            auditLogger.warn(clientId, "tools/call:" + toolName, "RATE_LIMITED");
            return buildError(id, -32029, "Rate limit exceeded: max 100 req/min");
        }

        // ── 3. Tool Lookup ────────────────────────────────────────────────
        MCPTool tool = toolRegistry.get(toolName);
        if (tool == null) {
            return buildError(id, -32602, "Unknown tool: " + toolName);
        }

        // ── 4. RBAC ───────────────────────────────────────────────────────
        String role = jwtValidator.extractRole(req);
        if (!tool.isAllowedRole(role)) {
            auditLogger.warn(clientId, "tools/call:" + toolName, "FORBIDDEN role=" + role);
            return buildError(id, -32003, "Forbidden: role '" + role + "' cannot call " + toolName);
        }

        // ── 5. Input Validation ───────────────────────────────────────────
        Optional<String> validationError = tool.validateArgs(args);
        if (validationError.isPresent()) {
            return buildError(id, -32602, "Invalid arguments: " + validationError.get());
        }

        // ── 6. Execute ────────────────────────────────────────────────────
        auditLogger.info(clientId, "tools/call:" + toolName, "INVOKED");
        long start = System.currentTimeMillis();
        try {
            JsonNode output = tool.execute(args, mapper);
            long ms = System.currentTimeMillis() - start;
            auditLogger.info(clientId, "tools/call:" + toolName, "OK latency=" + ms + "ms");

            ArrayNode content = mapper.createArrayNode();
            ObjectNode textBlock = mapper.createObjectNode();
            textBlock.put("type", "text");
            textBlock.put("text", mapper.writeValueAsString(output));
            content.add(textBlock);

            ObjectNode result = mapper.createObjectNode();
            result.set("content", content);
            result.put("isError", false);
            return buildResult(id, result);

        } catch (Exception e) {
            long ms = System.currentTimeMillis() - start;
            auditLogger.error(clientId, "tools/call:" + toolName, "ERROR: " + e.getMessage() + " latency=" + ms + "ms");
            LOG.log(Level.SEVERE, "Tool execution error", e);
            return buildError(id, -32000, "Execution error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private String extractClientId(JsonNode req) {
        return req.path("params").path("clientInfo").path("name").asText("anonymous");
    }

    private String buildResult(String id, JsonNode result) {
        try {
            ObjectNode resp = mapper.createObjectNode();
            resp.put("jsonrpc", "2.0");
            if (id != null) resp.put("id", id);
            resp.set("result", result);
            return mapper.writeValueAsString(resp);
        } catch (Exception e) {
            return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32700,\"message\":\"Serialization error\"}}";
        }
    }

    private String buildError(String id, int code, String message) {
        try {
            ObjectNode resp  = mapper.createObjectNode();
            resp.put("jsonrpc", "2.0");
            if (id != null) resp.put("id", id);
            ObjectNode error = resp.putObject("error");
            error.put("code", code);
            error.put("message", message);
            return mapper.writeValueAsString(resp);
        } catch (Exception e) {
            return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32700,\"message\":\"Fatal error\"}}";
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Entry Point
    // ─────────────────────────────────────────────────────────────────────────

    public static void main(String[] args) throws IOException {
        // Redirect JUL logs to stderr so stdout stays clean for JSON-RPC
        System.setErr(System.err);
        Logger root = Logger.getLogger("");
        Arrays.stream(root.getHandlers()).forEach(h -> h.setStream(System.err));

        new TIEMCPServer().start();
    }
}
