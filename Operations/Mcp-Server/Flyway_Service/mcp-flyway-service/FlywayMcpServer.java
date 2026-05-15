package com.ecoskiller.flyway;

import com.ecoskiller.flyway.security.SecurityManager;
import com.ecoskiller.flyway.agents.*;
import com.google.gson.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * Flyway Service MCP Server
 * Ecoskiller Platform — Database Schema Version Control & Automated Migration Engine
 *
 * PostgreSQL 15 | Helm Init Containers | GitLab CI/CD | 13-Service Schema Governance
 *
 * Transport:   stdio (stdin/stdout)
 * Format:      JSON-RPC 2.0
 * MCP Version: 2024-11-05
 * Security:    HMAC-SHA256 signing, input sanitization, tool allowlist, rate limiting, audit log
 *
 * Methods: initialize, tools/list, tools/call, ping
 */
public class FlywayMcpServer {

    private static final Logger LOGGER = Logger.getLogger(FlywayMcpServer.class.getName());
    private static final String MCP_VERSION  = "2024-11-05";
    private static final String SERVER_NAME  = "flyway-service-mcp";
    private static final String SERVER_VERSION = "1.0.0";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final SecurityManager security = new SecurityManager();
    private final Map<String, McpAgent> agents = new LinkedHashMap<>();

    public FlywayMcpServer() {
        registerAgents();
    }

    // ── Agent Registration ────────────────────────────────────────────────────

    private void registerAgents() {
        // Core migration lifecycle
        agents.put("migration_apply",              new MigrationApplyAgent(security));
        agents.put("migration_status",             new MigrationStatusAgent(security));
        agents.put("migration_validate",           new MigrationValidateAgent(security));
        agents.put("migration_repair",             new MigrationRepairAgent(security));
        agents.put("migration_baseline",           new MigrationBaselineAgent(security));
        agents.put("migration_info",               new MigrationInfoAgent(security));

        // Schema & drift management
        agents.put("schema_drift_detector",        new SchemaDriftDetectorAgent(security));
        agents.put("schema_history_query",         new SchemaHistoryQueryAgent(security));
        agents.put("checksum_validator",           new ChecksumValidatorAgent(security));
        agents.put("multi_schema_manager",         new MultiSchemaManagerAgent(security));

        // Helm & Kubernetes integration
        agents.put("helm_init_container_status",   new HelmInitContainerStatusAgent(security));
        agents.put("kubernetes_secret_validator",  new KubernetesSecretValidatorAgent(security));

        // CI/CD pipeline
        agents.put("gitlab_pipeline_trigger",     new GitlabPipelineTriggerAgent(security));
        agents.put("environment_parity_checker",  new EnvironmentParityCheckerAgent(security));

        // DR & recovery
        agents.put("dr_schema_consistency",        new DrSchemaConsistencyAgent(security));
        agents.put("dr_runbook_executor",          new DrRunbookExecutorAgent(security));

        // Security & compliance
        agents.put("rls_policy_tracker",           new RlsPolicyTrackerAgent(security));
        agents.put("compliance_audit_reporter",    new ComplianceAuditReporterAgent(security));
    }

    // ── MCP Server Loop ───────────────────────────────────────────────────────

    public void run() throws IOException {
        LOGGER.info("[FlywayMcpServer] Starting " + SERVER_NAME + " v" + SERVER_VERSION);
        LOGGER.info("[FlywayMcpServer] " + agents.size() + " agents registered");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String response = handleRequest(line);
            writer.println(response);
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

    // ── MCP Method Handlers ───────────────────────────────────────────────────

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

        // Security: allowlist check
        if (!security.isAllowedTool(toolName)) {
            return errorResponse(id, -32001, "Tool not permitted: " + toolName);
        }

        McpAgent agent = agents.get(toolName);
        if (agent == null) {
            return errorResponse(id, -32601, "Unknown tool: " + toolName);
        }

        JsonObject args      = p.has("arguments") ? p.get("arguments").getAsJsonObject() : new JsonObject();
        JsonObject sanitized = security.sanitizeInputs(args);

        // Audit every tool invocation
        security.auditLog(toolName, sanitized);

        JsonObject toolResult = agent.execute(sanitized);

        JsonObject result = new JsonObject();
        JsonArray  content = new JsonArray();
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

    // ── Response Builders ─────────────────────────────────────────────────────

    private String successResponse(JsonElement id, JsonObject result) {
        JsonObject resp = new JsonObject();
        resp.addProperty("jsonrpc", "2.0");
        resp.add("id", id);
        resp.add("result", result);
        return gson.toJson(resp);
    }

    private String errorResponse(JsonElement id, int code, String message) {
        JsonObject resp  = new JsonObject();
        resp.addProperty("jsonrpc", "2.0");
        resp.add("id", id != null ? id : JsonNull.INSTANCE);
        JsonObject error = new JsonObject();
        error.addProperty("code",    code);
        error.addProperty("message", message);
        resp.add("error", error);
        return gson.toJson(resp);
    }

    private JsonElement getIdSafe(JsonObject req) {
        return req.has("id") ? req.get("id") : JsonNull.INSTANCE;
    }

    // ── Entry Point ───────────────────────────────────────────────────────────

    public static void main(String[] args) throws IOException {
        // All logging → stderr. stdout is EXCLUSIVELY the MCP channel.
        Logger root = Logger.getLogger("");
        root.setLevel(Level.INFO);
        for (Handler h : root.getHandlers()) {
            h.setStream(System.err);
        }
        new FlywayMcpServer().run();
    }
}
