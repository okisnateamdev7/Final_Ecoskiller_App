package com.ecoskiller.mcp.longhorn.server;

import com.ecoskiller.mcp.longhorn.security.InputValidator;
import com.ecoskiller.mcp.longhorn.security.RateLimiter;
import com.ecoskiller.mcp.longhorn.tools.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.*;

/**
 * Ecoskiller | CAT-INFRA — Longhorn Storage MCP Server
 *
 * Distributed Block Storage Management for Kubernetes (k3s)
 * MCP Server in Java | 12 Agents | Priority: HIGH
 *
 * Transport: stdio (stdin/stdout)
 * Format:    JSON-RPC 2.0
 * MCP Version: 2024-11-05
 *
 * Security:
 *   - All inputs sanitised and validated before processing
 *   - Rate limiting per tool call
 *   - No secrets exposed in responses
 *   - Structured logging (no sensitive data in logs)
 */
public class LonghornMcpServer {

    private static final Logger LOGGER = Logger.getLogger(LonghornMcpServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "mcp-longhorn";
    private static final String SERVER_VERSION = "1.0.0";

    private final Map<String, ToolHandler> toolRegistry = new LinkedHashMap<>();
    private final InputValidator validator = new InputValidator();
    private final RateLimiter rateLimiter = new RateLimiter(60, 60_000); // 60 calls/minute

    public LonghornMcpServer() {
        registerTools();
    }

    private void registerTools() {
        // Storage provisioning
        toolRegistry.put("volume_provision",        new VolumeProvisionTool());
        toolRegistry.put("volume_expand",           new VolumeExpandTool());
        toolRegistry.put("volume_delete",           new VolumeDeleteTool());
        toolRegistry.put("volume_list",             new VolumeListTool());

        // Snapshot management
        toolRegistry.put("snapshot_create",         new SnapshotCreateTool());
        toolRegistry.put("snapshot_list",           new SnapshotListTool());
        toolRegistry.put("snapshot_restore",        new SnapshotRestoreTool());
        toolRegistry.put("snapshot_delete",         new SnapshotDeleteTool());

        // Backup & recovery
        toolRegistry.put("backup_trigger",          new BackupTriggerTool());
        toolRegistry.put("backup_status",           new BackupStatusTool());

        // Health & observability
        toolRegistry.put("replica_health",          new ReplicaHealthTool());
        toolRegistry.put("storage_metrics",         new StorageMetricsTool());
    }

    public void run() throws IOException {
        configureLogging();
        LOGGER.info("Longhorn MCP Server starting — version " + SERVER_VERSION);

        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8)), true);

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String response = handleRequest(line);
            if (response != null) {
                out.println(response);
            }
        }

        LOGGER.info("Longhorn MCP Server shutting down.");
    }

    private String handleRequest(String rawJson) {
        String id = "null";
        try {
            // Basic JSON-RPC parse (no external deps)
            Map<String, Object> req = JsonParser.parse(rawJson);
            id = String.valueOf(req.getOrDefault("id", "null"));
            String method = (String) req.get("method");

            if (method == null) {
                return JsonRpc.error(id, -32600, "Invalid Request: missing method");
            }

            return switch (method) {
                case "initialize"  -> handleInitialize(id, req);
                case "ping"        -> JsonRpc.result(id, JsonRpc.obj("status", "ok"));
                case "tools/list"  -> handleToolsList(id);
                case "tools/call"  -> handleToolCall(id, req);
                default            -> JsonRpc.error(id, -32601, "Method not found: " + method);
            };

        } catch (Exception e) {
            LOGGER.warning("Request handling error: " + e.getMessage());
            return JsonRpc.error(id, -32603, "Internal error");
        }
    }

    private String handleInitialize(String id, Map<String, Object> req) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("protocolVersion", MCP_VERSION);

        Map<String, Object> serverInfo = new LinkedHashMap<>();
        serverInfo.put("name", SERVER_NAME);
        serverInfo.put("version", SERVER_VERSION);
        result.put("serverInfo", serverInfo);

        Map<String, Object> capabilities = new LinkedHashMap<>();
        capabilities.put("tools", Collections.singletonMap("listChanged", false));
        result.put("capabilities", capabilities);

        return JsonRpc.result(id, result);
    }

    private String handleToolsList(String id) {
        List<Map<String, Object>> tools = new ArrayList<>();
        for (Map.Entry<String, ToolHandler> entry : toolRegistry.entrySet()) {
            tools.add(entry.getValue().getSchema(entry.getKey()));
        }
        return JsonRpc.result(id, Collections.singletonMap("tools", tools));
    }

    @SuppressWarnings("unchecked")
    private String handleToolCall(String id, Map<String, Object> req) {
        Map<String, Object> params = (Map<String, Object>) req.getOrDefault("params", Collections.emptyMap());
        String toolName = (String) params.get("name");
        Map<String, Object> args = (Map<String, Object>) params.getOrDefault("arguments", Collections.emptyMap());

        // Security: rate limiting
        if (!rateLimiter.allowRequest(toolName)) {
            return JsonRpc.toolError(id, "Rate limit exceeded. Max 60 calls/minute per tool.");
        }

        // Security: tool name validation
        if (!validator.isValidToolName(toolName)) {
            return JsonRpc.toolError(id, "Invalid tool name.");
        }

        ToolHandler handler = toolRegistry.get(toolName);
        if (handler == null) {
            return JsonRpc.error(id, -32601, "Unknown tool: " + toolName);
        }

        // Security: validate all arguments before passing to handler
        try {
            validator.validateArguments(args);
        } catch (SecurityException e) {
            LOGGER.warning("Input validation failed for tool [" + toolName + "]: " + e.getMessage());
            return JsonRpc.toolError(id, "Input validation failed: " + e.getMessage());
        }

        try {
            Object result = handler.execute(args);
            return JsonRpc.toolResult(id, result);
        } catch (IllegalArgumentException e) {
            return JsonRpc.toolError(id, e.getMessage());
        } catch (Exception e) {
            LOGGER.warning("Tool execution error [" + toolName + "]: " + e.getMessage());
            return JsonRpc.toolError(id, "Tool execution failed.");
        }
    }

    private void configureLogging() {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.INFO);
        // Only log to stderr — stdout is reserved for MCP JSON-RPC
        for (Handler h : rootLogger.getHandlers()) {
            rootLogger.removeHandler(h);
        }
        StreamHandler stderrHandler = new StreamHandler(System.err, new SimpleFormatter());
        stderrHandler.setLevel(Level.ALL);
        rootLogger.addHandler(stderrHandler);
    }

    public static void main(String[] args) throws IOException {
        new LonghornMcpServer().run();
    }
}
