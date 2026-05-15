package com.ecoskiller.mcp.wazuh.server;

import com.ecoskiller.mcp.wazuh.security.SecurityManager;
import com.ecoskiller.mcp.wazuh.tools.*;
import com.ecoskiller.mcp.wazuh.model.ToolResult;
import com.ecoskiller.mcp.wazuh.util.WazuhApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

import java.io.*;
import java.util.logging.*;

/**
 * EcoSkiller — CAT-08 Wazuh Security Monitoring MCP Server
 *
 * Transport  : stdio (stdin/stdout) — JSON-RPC 2.0
 * MCP Version: 2024-11-05
 * Agents     : 20
 *
 * Environment variables required:
 *   WAZUH_API_URL   — Wazuh Manager REST API base URL (https://...)
 *   WAZUH_API_TOKEN — Bearer token for Wazuh API authentication
 *
 * All logging goes to stderr. stdout is reserved for pure JSON-RPC 2.0.
 */
public class WazuhMcpServer {

    private static final Logger LOGGER = Logger.getLogger(WazuhMcpServer.class.getName());
    private static final String MCP_VERSION    = "2024-11-05";
    private static final String SERVER_NAME    = "mcp-wazuh";
    private static final String SERVER_VERSION = "1.0.0";

    private final ObjectMapper    mapper   = new ObjectMapper();
    private final SecurityManager security = new SecurityManager();
    private final WazuhApiClient  api      = new WazuhApiClient();
    private final ToolRegistry    registry;

    public WazuhMcpServer() {
        registry = new ToolRegistry(
            new AlertQueryTool           (security, api),  //  1
            new AlertGetTool             (security, api),  //  2
            new AgentListTool            (security, api),  //  3
            new AgentStatusTool          (security, api),  //  4
            new AgentRestartTool         (security, api),  //  5
            new RuleQueryTool            (security, api),  //  6
            new RuleGetTool              (security, api),  //  7
            new FimEventsTool            (security, api),  //  8
            new VulnerabilityScanTool    (security, api),  //  9
            new ComplianceReportTool     (security, api),  // 10
            new ThreatIntelligenceTool   (security, api),  // 11
            new ActiveResponseTool       (security, api),  // 12
            new IncidentRunbookTool      (security, api),  // 13
            new BehavioralAnomalyTool    (security, api),  // 14
            new AgentOverviewTool        (security, api),  // 15
            new KafkaAlertStatusTool     (security, api),  // 16
            new ForensicQueryTool        (security, api),  // 17
            new ManagerHealthTool        (security, api),  // 18
            new FalsePositiveExceptionTool(security, api), // 19
            new ComplianceAuditTrailTool (security, api)   // 20
        );
        LOGGER.info("[WazuhMCP] Registered " + registry.size() + " tools");
    }

    // ── Main stdio loop ────────────────────────────────────────────────────
    public void start() throws IOException {
        LOGGER.info("[WazuhMCP] Starting " + SERVER_NAME + " v" + SERVER_VERSION);
        LOGGER.info("[WazuhMCP] Wazuh API: " + api.getBaseUrl());

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    writer = new PrintWriter(new OutputStreamWriter(System.out), true);

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                String response = handleRequest(line);
                if (response != null) { writer.println(response); writer.flush(); }
            } catch (Exception e) {
                LOGGER.severe("[WazuhMCP] Unhandled: " + e.getMessage());
                writer.println(errorResponse(null, -32603, "Internal error: " + e.getMessage()));
                writer.flush();
            }
        }
    }

    // ── JSON-RPC 2.0 dispatcher ────────────────────────────────────────────
    private String handleRequest(String raw) throws Exception {
        JsonNode req    = mapper.readTree(raw);
        String   id     = req.has("id")     ? req.get("id").asText()     : null;
        String   method = req.has("method") ? req.get("method").asText() : "";

        LOGGER.fine("[WazuhMCP] method=" + method + " id=" + id);

        return switch (method) {
            case "initialize"  -> handleInitialize(id);
            case "ping"        -> successResponse(id, mapper.createObjectNode());
            case "tools/list"  -> handleToolsList(id);
            case "tools/call"  -> handleToolsCall(id, req);
            default            -> errorResponse(id, -32601, "Method not found: " + method);
        };
    }

    private String handleInitialize(String id) throws Exception {
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);
        result.putObject("serverInfo")
            .put("name", SERVER_NAME)
            .put("version", SERVER_VERSION);
        result.putObject("capabilities").putObject("tools");
        return successResponse(id, result);
    }

    private String handleToolsList(String id) throws Exception {
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", registry.getToolDefinitions(mapper));
        return successResponse(id, result);
    }

    private String handleToolsCall(String id, JsonNode req) throws Exception {
        JsonNode params    = req.path("params");
        String   toolName  = params.path("name").asText("");
        JsonNode arguments = params.path("arguments");

        security.auditLog("TOOL_CALL", toolName, arguments.toString());

        BaseTool tool = registry.getTool(toolName);
        if (tool == null) return errorResponse(id, -32602, "Unknown tool: " + toolName);

        ToolResult toolResult = tool.execute(arguments);
        ObjectNode result  = mapper.createObjectNode();
        ArrayNode  content = result.putArray("content");
        content.addObject().put("type", "text").put("text", toolResult.getText());
        result.put("isError", toolResult.isError());
        return successResponse(id, result);
    }

    // ── JSON-RPC helpers ───────────────────────────────────────────────────
    private String successResponse(String id, JsonNode result) throws Exception {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.put("id", id);
        resp.set("result", result);
        return mapper.writeValueAsString(resp);
    }

    private String errorResponse(String id, int code, String message) {
        try {
            ObjectNode resp = mapper.createObjectNode();
            resp.put("jsonrpc", "2.0");
            if (id != null) resp.put("id", id);
            resp.putObject("error").put("code", code).put("message", message);
            return mapper.writeValueAsString(resp);
        } catch (Exception e) {
            return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32603,\"message\":\"Fatal\"}}";
        }
    }

    // ── Entry point ────────────────────────────────────────────────────────
    public static void main(String[] args) throws Exception {
        Logger root = Logger.getLogger("");
        root.setLevel(Level.INFO);
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        ConsoleHandler stderr = new ConsoleHandler();
        stderr.setStream(System.err);
        stderr.setLevel(Level.ALL);
        root.addHandler(stderr);

        new WazuhMcpServer().start();
    }
}
