package com.ecoskiller.antigravity.server;

import com.ecoskiller.antigravity.agents.*;
import com.ecoskiller.antigravity.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * Ecoskiller Antigravity MCP Server
 * CAT-01 — Core Platform & Meta Architecture
 * MCP Server: mcp-01-platform
 *
 * Agents (11):
 *   1.  ANTIGRAVITY_CONFIGURATION_AGENT
 *   2.  ANTIGRAVITY_DEPENDENCY_RISK_AGENT
 *   3.  ANTIGRAVITY_OBSERVABILITY_AGENT
 *   4.  ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT
 *   5.  ANTIGRAVITY_PLATFORM_EVALUATION_AGENT
 *   6.  ANTIGRAVITY_TECH_DEBT_AGENT
 *   7.  ANTIGRAVITY_TRUTH_ENGINE_AGENT
 *   8.  ECOSKILLER_ANTIGRAVITY_API_AGENT
 *   9.  ECOSKILLER_ANTIGRAVITY_SCHEMA_COMPATIBILITY_AGENT
 *  10.  ECOSKILLER_ANTIGRAVITY_SYSTEM_SETUP_AGENT
 *  11.  ECOSKILLER_ANTIGRAVITY_UI_AGENT
 */
public class AntigravityMcpServer {

    private static final Logger LOGGER = Logger.getLogger(AntigravityMcpServer.class.getName());
    static final String SERVER_NAME    = "mcp-01-platform";
    static final String SERVER_VERSION = "1.0.0";
    static final String NAMESPACE      = "core";

    private final ObjectMapper   mapper = new ObjectMapper();
    private final AgentRegistry  registry;

    public AntigravityMcpServer() {
        this.registry = new AgentRegistry();
        registerAllAgents();
    }

    // ──────────────────────────────────────────────────────────────────────
    // Agent Registration
    // ──────────────────────────────────────────────────────────────────────

    private void registerAllAgents() {
        registry.register(new ConfigurationAgent());
        registry.register(new DependencyRiskAgent());
        registry.register(new ObservabilityAgent());
        registry.register(new OrchestrationGovernorAgent());
        registry.register(new PlatformEvaluationAgent());
        registry.register(new TechDebtAgent());
        registry.register(new TruthEngineAgent());
        registry.register(new ApiAgent());
        registry.register(new SchemaCompatibilityAgent());
        registry.register(new SystemSetupAgent());
        registry.register(new UiAgent());
        LOGGER.info("[" + SERVER_NAME + "] Registered " + registry.count() + " agents.");
    }

    // ──────────────────────────────────────────────────────────────────────
    // MCP Protocol Loop  (stdio transport)
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
    // Message Dispatch
    // ──────────────────────────────────────────────────────────────────────

    String handleMessage(String rawJson) {
        try {
            ObjectNode req = (ObjectNode) mapper.readTree(rawJson);
            String method  = req.path("method").asText("");
            Object id      = req.has("id") ? req.get("id") : null;

            return switch (method) {
                case "initialize"       -> handleInitialize(req, id);
                case "tools/list"       -> handleToolsList(id);
                case "tools/call"       -> handleToolCall(req, id);
                case "resources/list"   -> handleResourcesList(id);
                case "prompts/list"     -> handlePromptsList(id);
                case "ping"             -> buildResult(id, mapper.createObjectNode());
                default                 -> buildError(id, -32601, "Method not found: " + method);
            };
        } catch (Exception e) {
            LOGGER.severe("Parse error: " + e.getMessage());
            return buildError(null, -32700, "Parse error: " + e.getMessage());
        }
    }

    // ──────────────────────────────────────────────────────────────────────
    // Handlers
    // ──────────────────────────────────────────────────────────────────────

    private String handleInitialize(ObjectNode req, Object id) {
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", "2024-11-05");

        ObjectNode info = mapper.createObjectNode();
        info.put("name",    SERVER_NAME);
        info.put("version", SERVER_VERSION);
        result.set("serverInfo", info);

        ObjectNode caps = mapper.createObjectNode();
        caps.set("tools",     mapper.createObjectNode().put("listChanged", false));
        caps.set("resources", mapper.createObjectNode().put("subscribe", false).put("listChanged", false));
        caps.set("prompts",   mapper.createObjectNode().put("listChanged", false));
        result.set("capabilities", caps);

        return buildResult(id, result);
    }

    private String handleToolsList(Object id) {
        ArrayNode tools = mapper.createArrayNode();
        for (BaseAgent agent : registry.all()) {
            for (McpTool tool : agent.getTools()) {
                tools.add(tool.toJson(mapper));
            }
        }
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

            for (BaseAgent agent : registry.all()) {
                for (McpTool tool : agent.getTools()) {
                    if (tool.getName().equals(toolName)) {
                        AgentResponse resp = agent.execute(toolName, args);
                        return buildToolResult(id, resp);
                    }
                }
            }
            return buildError(id, -32602, "Tool not found: " + toolName);

        } catch (Exception e) {
            LOGGER.severe("Tool call error: " + e.getMessage());
            return buildError(id, -32603, "Tool execution error: " + e.getMessage());
        }
    }

    private String handleResourcesList(Object id) {
        ObjectNode result = mapper.createObjectNode();
        result.set("resources", mapper.createArrayNode());
        return buildResult(id, result);
    }

    private String handlePromptsList(Object id) {
        ObjectNode result = mapper.createObjectNode();
        result.set("prompts", mapper.createArrayNode());
        return buildResult(id, result);
    }

    // ──────────────────────────────────────────────────────────────────────
    // JSON-RPC helpers
    // ──────────────────────────────────────────────────────────────────────

    private String buildResult(Object id, ObjectNode result) {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id instanceof Integer i) resp.put("id", i);
        else if (id instanceof String s) resp.put("id", s);
        else resp.putNull("id");
        resp.set("result", result);
        return resp.toString();
    }

    private String buildToolResult(Object id, AgentResponse agentResp) {
        ObjectNode resp    = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id instanceof Integer i) resp.put("id", i);
        else if (id instanceof String s) resp.put("id", s);
        else resp.putNull("id");

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
        if (id instanceof Integer i) resp.put("id", i);
        else if (id instanceof String s) resp.put("id", s);
        else resp.putNull("id");
        ObjectNode error = mapper.createObjectNode();
        error.put("code",    code);
        error.put("message", message);
        resp.set("error", error);
        return resp.toString();
    }

    // ──────────────────────────────────────────────────────────────────────
    // Entry point
    // ──────────────────────────────────────────────────────────────────────

    public static void main(String[] args) throws Exception {
        setupLogging();
        new AntigravityMcpServer().run();
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
