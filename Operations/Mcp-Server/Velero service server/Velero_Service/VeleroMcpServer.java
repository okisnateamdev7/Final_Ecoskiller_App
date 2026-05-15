package com.ecoskiller.mcp.velero.server;

import com.ecoskiller.mcp.velero.security.SecurityManager;
import com.ecoskiller.mcp.velero.tools.*;
import com.ecoskiller.mcp.velero.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * EcoSkiller — CAT-16 Velero MCP Server
 * Kubernetes Cluster Backup, Restore & Disaster Recovery
 *
 * Transport  : stdio (stdin/stdout) — JSON-RPC 2.0
 * MCP Version: 2024-11-05
 * Security   : Input validation, command injection prevention, audit logging
 */
public class VeleroMcpServer {

    private static final Logger LOGGER = Logger.getLogger(VeleroMcpServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "mcp-velero";
    private static final String SERVER_VERSION = "1.0.0";

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityManager security = new SecurityManager();
    private final ToolRegistry toolRegistry;

    // ── 16 Agents ──────────────────────────────────────────────────────────
    private final BackupCreateTool             backupCreateTool;
    private final BackupListTool               backupListTool;
    private final BackupDeleteTool             backupDeleteTool;
    private final BackupStatusTool             backupStatusTool;
    private final BackupDescribeTool           backupDescribeTool;
    private final RestoreCreateTool            restoreCreateTool;
    private final RestoreListTool              restoreListTool;
    private final RestoreStatusTool            restoreStatusTool;
    private final RestoreDescribeTool          restoreDescribeTool;
    private final ScheduleListTool             scheduleListTool;
    private final ScheduleCreateTool           scheduleCreateTool;
    private final ScheduleDeleteTool           scheduleDeleteTool;
    private final BackupStorageLocationTool    backupStorageLocationTool;
    private final BackupIntegrityCheckTool     backupIntegrityCheckTool;
    private final DrDrillLogTool               drDrillLogTool;
    private final ClusterHealthTool            clusterHealthTool;

    public VeleroMcpServer() {
        this.backupCreateTool             = new BackupCreateTool(security);
        this.backupListTool               = new BackupListTool(security);
        this.backupDeleteTool             = new BackupDeleteTool(security);
        this.backupStatusTool             = new BackupStatusTool(security);
        this.backupDescribeTool           = new BackupDescribeTool(security);
        this.restoreCreateTool            = new RestoreCreateTool(security);
        this.restoreListTool              = new RestoreListTool(security);
        this.restoreStatusTool            = new RestoreStatusTool(security);
        this.restoreDescribeTool          = new RestoreDescribeTool(security);
        this.scheduleListTool             = new ScheduleListTool(security);
        this.scheduleCreateTool           = new ScheduleCreateTool(security);
        this.scheduleDeleteTool           = new ScheduleDeleteTool(security);
        this.backupStorageLocationTool    = new BackupStorageLocationTool(security);
        this.backupIntegrityCheckTool     = new BackupIntegrityCheckTool(security);
        this.drDrillLogTool               = new DrDrillLogTool(security);
        this.clusterHealthTool            = new ClusterHealthTool(security);

        this.toolRegistry = new ToolRegistry(
            backupCreateTool, backupListTool, backupDeleteTool, backupStatusTool,
            backupDescribeTool, restoreCreateTool, restoreListTool, restoreStatusTool,
            restoreDescribeTool, scheduleListTool, scheduleCreateTool, scheduleDeleteTool,
            backupStorageLocationTool, backupIntegrityCheckTool, drDrillLogTool, clusterHealthTool
        );
    }

    // ── Main loop ───────────────────────────────────────────────────────────
    public void start() throws IOException {
        LOGGER.info("[VeleroMCP] Starting " + SERVER_NAME + " v" + SERVER_VERSION);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    writer = new PrintWriter(new OutputStreamWriter(System.out), true);

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                String response = handleRequest(line);
                if (response != null) {
                    writer.println(response);
                    writer.flush();
                }
            } catch (Exception e) {
                LOGGER.severe("[VeleroMCP] Unhandled error: " + e.getMessage());
                writer.println(errorResponse(null, -32603, "Internal error: " + e.getMessage()));
                writer.flush();
            }
        }
    }

    // ── Request dispatcher ──────────────────────────────────────────────────
    private String handleRequest(String raw) throws Exception {
        JsonNode req = mapper.readTree(raw);
        String   id     = req.has("id")     ? req.get("id").asText()     : null;
        String   method = req.has("method") ? req.get("method").asText() : "";

        LOGGER.fine("[VeleroMCP] method=" + method + " id=" + id);

        return switch (method) {
            case "initialize"    -> handleInitialize(id, req);
            case "ping"          -> handlePing(id);
            case "tools/list"    -> handleToolsList(id);
            case "tools/call"    -> handleToolsCall(id, req);
            default              -> errorResponse(id, -32601, "Method not found: " + method);
        };
    }

    // ── initialize ──────────────────────────────────────────────────────────
    private String handleInitialize(String id, JsonNode req) throws Exception {
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);

        ObjectNode serverInfo = result.putObject("serverInfo");
        serverInfo.put("name",    SERVER_NAME);
        serverInfo.put("version", SERVER_VERSION);

        ObjectNode capabilities = result.putObject("capabilities");
        capabilities.putObject("tools");

        return successResponse(id, result);
    }

    // ── ping ────────────────────────────────────────────────────────────────
    private String handlePing(String id) throws Exception {
        return successResponse(id, mapper.createObjectNode());
    }

    // ── tools/list ──────────────────────────────────────────────────────────
    private String handleToolsList(String id) throws Exception {
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", toolRegistry.getToolDefinitions(mapper));
        return successResponse(id, result);
    }

    // ── tools/call ──────────────────────────────────────────────────────────
    private String handleToolsCall(String id, JsonNode req) throws Exception {
        JsonNode params    = req.path("params");
        String   toolName  = params.path("name").asText("");
        JsonNode arguments = params.path("arguments");

        // ── Security: audit every tool call ─────────────────────────────
        security.auditLog("TOOL_CALL", toolName, arguments.toString());

        BaseTool tool = toolRegistry.getTool(toolName);
        if (tool == null) {
            return errorResponse(id, -32602, "Unknown tool: " + toolName);
        }

        ToolResult toolResult = tool.execute(arguments);
        ObjectNode result     = mapper.createObjectNode();
        ArrayNode  content    = result.putArray("content");
        ObjectNode textNode   = content.addObject();
        textNode.put("type", "text");
        textNode.put("text", toolResult.getText());
        result.put("isError", toolResult.isError());

        return successResponse(id, result);
    }

    // ── JSON-RPC helpers ────────────────────────────────────────────────────
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
            ObjectNode err = resp.putObject("error");
            err.put("code",    code);
            err.put("message", message);
            return mapper.writeValueAsString(resp);
        } catch (Exception e) {
            return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32603,\"message\":\"Fatal serialisation error\"}}";
        }
    }

    // ── Entry point ─────────────────────────────────────────────────────────
    public static void main(String[] args) throws Exception {
        // Configure logging to stderr so stdout stays clean for JSON-RPC
        Logger root = Logger.getLogger("");
        root.setLevel(Level.INFO);
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        ConsoleHandler stderr = new ConsoleHandler();
        stderr.setStream(System.err);
        stderr.setLevel(Level.ALL);
        root.addHandler(stderr);

        new VeleroMcpServer().start();
    }
}
