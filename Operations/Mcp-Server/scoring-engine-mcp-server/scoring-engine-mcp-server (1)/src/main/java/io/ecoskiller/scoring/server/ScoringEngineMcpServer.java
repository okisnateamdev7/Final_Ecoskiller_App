package io.ecoskiller.scoring.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import io.ecoskiller.scoring.agents.*;
import io.ecoskiller.scoring.config.ServerConfig;
import io.ecoskiller.scoring.security.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.*;

/**
 * Ecoskiller Scoring Engine MCP Server (CAT-SCORING-ENGINE)
 *
 * Secure Java MCP Server — JSON-RPC 2.0 over stdio.
 * 18 agents covering the full AI-powered assessment scoring pipeline:
 *   score submission, GD/interview/dojo scoring, belt eligibility,
 *   score explanations (DPDPA 2023), bias detection/fairness,
 *   model version management, audit logging, candidate rights.
 *
 * Security layers:
 *   - JWT HMAC-SHA256 + 5-min cache (constant-time comparison)
 *   - Strict input validation (assessment types, belt levels, model versions, dimensions)
 *   - Dimension weight sum validation (must = 100%)
 *   - Score range enforcement (0-100)
 *   - Token bucket rate limiting
 *   - 64KB payload cap + null byte detection
 *   - Audit logging to stderr (ELK-ready, SOC 2 / DPDPA 2023)
 *   - All secrets from env vars / Kubernetes Secrets
 */
public class ScoringEngineMcpServer {

    private static final Logger LOG = Logger.getLogger(ScoringEngineMcpServer.class.getName());
    private static final String MCP_VERSION    = "2024-11-05";
    private static final String SERVER_VERSION = "1.0.0";
    private static final String SERVER_NAME    = "mcp-scoring-engine-ecoskiller";

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, AgentHandler> agents = new LinkedHashMap<>();
    private final RateLimiter rateLimiter;
    private final RequestValidator validator;
    private final AuditLogger audit;
    private final ServerConfig config;

    public ScoringEngineMcpServer() {
        this.config      = ServerConfig.load();
        this.rateLimiter = new RateLimiter(config);
        this.validator   = new RequestValidator();
        this.audit       = new AuditLogger(config);
        registerAgents();
        LOG.info("ScoringEngineMcpServer initialised — " + agents.size() + " agents loaded.");
    }

    private void registerAgents() {
        // Core scoring
        agents.put("score_submit_request",        new ScoreSubmitRequestAgent(config, audit));
        agents.put("score_get",                   new ScoreGetAgent(config, audit));
        agents.put("gd_score_compute",            new GdScoreComputeAgent(config, audit));
        agents.put("interview_score_compute",     new InterviewScoreComputeAgent(config, audit));
        agents.put("dojo_score_compute",          new DojoScoreComputeAgent(config, audit));
        agents.put("score_rescore",               new ScoreRescoreAgent(config, audit));
        // Belt & Explanation
        agents.put("belt_eligibility_determine",  new BeltEligibilityDetermineAgent(config, audit));
        agents.put("score_explanation_generate",  new ScoreExplanationGenerateAgent(config, audit));
        // Bias & Model
        agents.put("bias_detection_run",          new BiasDetectionRunAgent(config, audit));
        agents.put("model_version_manage",        new ModelVersionManageAgent(config, audit));
        agents.put("model_performance_metrics",   new ModelPerformanceMetricsAgent(config, audit));
        agents.put("score_history_get",           new ScoreHistoryGetAgent(config, audit));
        // Admin & Compliance
        agents.put("scoring_audit_log_query",     new ScoringAuditLogQueryAgent(config, audit));
        agents.put("queue_status_get",            new QueueStatusGetAgent(config, audit));
        agents.put("score_manual_override",       new ScoreManualOverrideAgent(config, audit));
        agents.put("dimension_weight_update",     new DimensionWeightUpdateAgent(config, audit));
        agents.put("service_health",              new ServiceHealthAgent(config, audit));
        agents.put("candidate_rights_request",    new CandidateRightsRequestAgent(config, audit));
    }

    // ── Main loop ────────────────────────────────────────────────────────────

    public void run() throws IOException {
        LOG.info("ScoringEngineMcpServer listening on stdin/stdout (JSON-RPC 2.0 / MCP 2024-11-05)");
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in,  StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) { String r = dispatch(line); if (r != null) out.println(r); }
        }
    }

    // ── JSON-RPC dispatcher ──────────────────────────────────────────────────

    String dispatch(String raw) {
        JsonNode req;
        try { req = mapper.readTree(raw); }
        catch (Exception e) { return err(null, -32700, "Parse error: " + e.getMessage()); }

        JsonNode idNode = req.get("id");
        Object id       = idNode == null ? null : (idNode.isNumber() ? idNode.asLong() : idNode.asText());
        String method   = req.has("method") ? req.get("method").asText() : "";
        JsonNode params = req.has("params") ? req.get("params") : mapper.createObjectNode();

        String key = clientKey(params);
        if (!rateLimiter.allow(key)) {
            audit.warn("RATE_LIMIT", key, "method=" + method);
            return err(id, -32000, "Rate limit exceeded.");
        }

        try {
            return switch (method) {
                case "initialize"  -> handleInit(id);
                case "ping"        -> ok(id, mapper.createObjectNode().put("status","pong"));
                case "tools/list"  -> handleList(id);
                case "tools/call"  -> handleCall(id, params);
                default            -> err(id, -32601, "Method not found: " + method);
            };
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Dispatch error: " + method, e);
            return err(id, -32603, "Internal error: " + e.getMessage());
        }
    }

    private String handleInit(Object id) throws Exception {
        audit.info("INITIALIZE","server","MCP handshake");
        ObjectNode res = mapper.createObjectNode();
        res.put("protocolVersion", MCP_VERSION);
        ObjectNode si = res.putObject("serverInfo");
        si.put("name", SERVER_NAME); si.put("version", SERVER_VERSION);
        si.put("description","Ecoskiller Scoring Engine MCP — AI-Powered Assessment Scoring & Belt Advancement");
        res.putObject("capabilities").putObject("tools");
        return ok(id, res);
    }

    private String handleList(Object id) throws Exception {
        ArrayNode tools = mapper.createArrayNode();
        for (AgentHandler h : agents.values()) tools.add(h.getToolDefinition());
        ObjectNode res = mapper.createObjectNode(); res.set("tools", tools);
        return ok(id, res);
    }

    private String handleCall(Object id, JsonNode params) throws Exception {
        if (!params.has("name")) return err(id, -32602, "Missing tool name");

        String toolName = params.get("name").asText();
        JsonNode args   = params.has("arguments") ? params.get("arguments") : mapper.createObjectNode();

        if (!validator.isValidToolName(toolName)) {
            audit.warn("INVALID_TOOL","unknown",toolName);
            return err(id, -32602, "Invalid tool name: " + toolName);
        }

        AgentHandler agent = agents.get(toolName);
        if (agent == null) return err(id, -32601, "Unknown tool: " + toolName);

        List<String> errs = validator.validate(toolName, args);
        if (!errs.isEmpty()) {
            audit.warn("VALIDATION_FAIL","unknown", toolName + ": " + errs);
            return err(id, -32602, "Validation errors: " + errs);
        }

        audit.info("TOOL_CALL", clientKey(args), toolName);

        try {
            JsonNode result = agent.execute(args);
            ObjectNode res = mapper.createObjectNode();
            res.putArray("content").addObject().put("type","text").put("text", mapper.writeValueAsString(result));
            res.put("isError", false);
            return ok(id, res);
        } catch (SecurityException se) {
            audit.error("SECURITY_VIOLATION", clientKey(args), toolName + ": " + se.getMessage());
            ObjectNode res = mapper.createObjectNode();
            res.putArray("content").addObject().put("type","text").put("text", "{\"error\":\"" + esc(se.getMessage()) + "\"}");
            res.put("isError", true);
            return ok(id, res);
        } catch (Exception e) {
            audit.error("AGENT_ERROR", clientKey(args), toolName + ": " + e.getMessage());
            ObjectNode res = mapper.createObjectNode();
            res.putArray("content").addObject().put("type","text").put("text", "{\"error\":\"" + esc(e.getMessage()) + "\"}");
            res.put("isError", true);
            return ok(id, res);
        }
    }

    private String ok(Object id, JsonNode result) throws Exception {
        ObjectNode r = mapper.createObjectNode();
        r.put("jsonrpc","2.0"); r.set("id", idNode(id)); r.set("result", result);
        return mapper.writeValueAsString(r);
    }

    private String err(Object id, int code, String msg) {
        try {
            ObjectNode r = mapper.createObjectNode();
            r.put("jsonrpc","2.0"); r.set("id", idNode(id));
            r.putObject("error").put("code",code).put("message",msg);
            return mapper.writeValueAsString(r);
        } catch (Exception ex) { return "{\"jsonrpc\":\"2.0\",\"id\":null,\"error\":{\"code\":-32603,\"message\":\"Serialisation error\"}}"; }
    }

    private JsonNode idNode(Object id) {
        if (id == null) return mapper.nullNode();
        if (id instanceof Long l) return mapper.getNodeFactory().numberNode(l);
        return mapper.getNodeFactory().textNode(id.toString());
    }

    private String clientKey(JsonNode p) {
        if (p != null && p.has("jwt_token")) {
            String t = p.get("jwt_token").asText();
            return t.length() > 16 ? t.substring(0,16) + "..." : t;
        }
        return "anonymous";
    }

    private String esc(String s) {
        if (s == null) return "null";
        return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n");
    }

    public static void main(String[] args) throws IOException {
        setupLogging();
        LOG.info("Starting Ecoskiller Scoring Engine MCP Server v" + SERVER_VERSION);
        new ScoringEngineMcpServer().run();
    }

    private static void setupLogging() {
        Logger root = Logger.getLogger(""); root.setLevel(Level.INFO);
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        ConsoleHandler h = new ConsoleHandler();
        h.setStream(System.err); h.setLevel(Level.ALL); h.setFormatter(new SimpleFormatter());
        root.addHandler(h);
    }
}
