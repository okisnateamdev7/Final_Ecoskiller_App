package io.ecoskiller.mcp.server;

import io.ecoskiller.mcp.security.SecurityManager;
import io.ecoskiller.mcp.tools.*;
import io.ecoskiller.mcp.utils.JsonUtils;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * Jitsi Web Client Service — MCP Server (Java)
 * Ecoskiller Platform | CAT-JITSI — Assessment Video Conferencing
 *
 * Transport : stdio (stdin/stdout)
 * Protocol  : JSON-RPC 2.0
 * MCP Ver   : 2024-11-05
 * Security  : JWT validation, input sanitization, rate limiting, audit logging
 */
public class JitsiMcpServer {

    private static final Logger LOGGER = Logger.getLogger(JitsiMcpServer.class.getName());
    private static final String SERVER_NAME    = "jitsi-web-client-mcp";
    private static final String SERVER_VERSION = "1.0.0";
    private static final String MCP_VERSION    = "2024-11-05";

    private final ToolRegistry toolRegistry;
    private final SecurityManager securityManager;
    private boolean initialized = false;

    public JitsiMcpServer() {
        this.securityManager = new SecurityManager();
        this.toolRegistry    = new ToolRegistry(securityManager);
    }

    public void start() throws IOException {
        configureLogging();
        LOGGER.info("[JitsiMCP] Server starting — " + SERVER_NAME + " v" + SERVER_VERSION);

        BufferedReader stdin  = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    stdout = new PrintWriter(new OutputStreamWriter(System.out), true);

        String line;
        while ((line = stdin.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            try {
                String response = handleRequest(line);
                if (response != null) {
                    stdout.println(response);
                    stdout.flush();
                }
            } catch (Exception e) {
                LOGGER.severe("[JitsiMCP] Unhandled error: " + e.getMessage());
                String err = JsonUtils.errorResponse(null, -32700, "Parse error: " + sanitizeMessage(e.getMessage()));
                stdout.println(err);
                stdout.flush();
            }
        }
        LOGGER.info("[JitsiMCP] Server shutting down.");
    }

    private String handleRequest(String rawJson) {
        // Security: basic size guard (10 MB max)
        if (rawJson.length() > 10_000_000) {
            return JsonUtils.errorResponse(null, -32700, "Request too large");
        }

        Map<String, Object> req;
        try {
            req = JsonUtils.parseObject(rawJson);
        } catch (Exception e) {
            return JsonUtils.errorResponse(null, -32700, "Invalid JSON");
        }

        Object id     = req.get("id");
        String method = (String) req.getOrDefault("method", "");

        // Validate JSON-RPC 2.0 structure
        if (!"2.0".equals(req.get("jsonrpc"))) {
            return JsonUtils.errorResponse(id, -32600, "Invalid JSON-RPC version");
        }

        securityManager.auditLog("REQUEST", method, id);

        // Rate limiting
        if (!securityManager.checkRateLimit("global")) {
            return JsonUtils.errorResponse(id, -32000, "Rate limit exceeded");
        }

        switch (method) {
            case "initialize":    return handleInitialize(id, req);
            case "ping":          return handlePing(id);
            case "tools/list":    return handleToolsList(id);
            case "tools/call":    return handleToolsCall(id, req);
            default:
                return JsonUtils.errorResponse(id, -32601, "Method not found: " + method);
        }
    }

    @SuppressWarnings("unchecked")
    private String handleInitialize(Object id, Map<String, Object> req) {
        Map<String, Object> params = (Map<String, Object>) req.getOrDefault("params", new HashMap<>());
        String clientVersion = (String) params.getOrDefault("protocolVersion", "unknown");
        LOGGER.info("[JitsiMCP] Initialize — client protocol: " + clientVersion);
        initialized = true;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("protocolVersion", MCP_VERSION);

        Map<String, Object> serverInfo = new LinkedHashMap<>();
        serverInfo.put("name", SERVER_NAME);
        serverInfo.put("version", SERVER_VERSION);
        result.put("serverInfo", serverInfo);

        Map<String, Object> capabilities = new LinkedHashMap<>();
        Map<String, Object> toolsCap = new LinkedHashMap<>();
        toolsCap.put("listChanged", false);
        capabilities.put("tools", toolsCap);
        result.put("capabilities", capabilities);

        result.put("instructions",
            "Jitsi Web Client MCP Server for Ecoskiller Platform. " +
            "Provides 15 tools for managing real-time assessment video conferencing, " +
            "WebRTC sessions, media quality, authentication, and analytics. " +
            "All operations require valid JWT tokens. Use tools/list to see available tools.");

        return JsonUtils.successResponse(id, result);
    }

    private String handlePing(Object id) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "ok");
        result.put("server", SERVER_NAME);
        result.put("timestamp", System.currentTimeMillis());
        return JsonUtils.successResponse(id, result);
    }

    private String handleToolsList(Object id) {
        if (!initialized) {
            return JsonUtils.errorResponse(id, -32002, "Server not initialized. Call initialize first.");
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tools", toolRegistry.listTools());
        return JsonUtils.successResponse(id, result);
    }

    @SuppressWarnings("unchecked")
    private String handleToolsCall(Object id, Map<String, Object> req) {
        if (!initialized) {
            return JsonUtils.errorResponse(id, -32002, "Server not initialized.");
        }

        Map<String, Object> params = (Map<String, Object>) req.getOrDefault("params", new HashMap<>());
        String toolName = (String) params.get("name");
        Map<String, Object> args = (Map<String, Object>) params.getOrDefault("arguments", new HashMap<>());

        if (toolName == null || toolName.isBlank()) {
            return JsonUtils.errorResponse(id, -32602, "Missing tool name");
        }

        // Security: sanitize tool name
        if (!toolName.matches("[a-zA-Z0-9_-]{1,64}")) {
            return JsonUtils.errorResponse(id, -32602, "Invalid tool name format");
        }

        try {
            ToolResult toolResult = toolRegistry.callTool(toolName, args);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("content", Collections.singletonList(
                Map.of("type", "text", "text", toolResult.toJson())
            ));
            result.put("isError", toolResult.isError());
            return JsonUtils.successResponse(id, result);
        } catch (ToolNotFoundException e) {
            return JsonUtils.errorResponse(id, -32601, "Tool not found: " + toolName);
        } catch (ToolValidationException e) {
            return JsonUtils.errorResponse(id, -32602, "Invalid arguments: " + sanitizeMessage(e.getMessage()));
        } catch (Exception e) {
            LOGGER.warning("[JitsiMCP] Tool error [" + toolName + "]: " + e.getMessage());
            return JsonUtils.errorResponse(id, -32000, "Tool execution error: " + sanitizeMessage(e.getMessage()));
        }
    }

    private String sanitizeMessage(String msg) {
        if (msg == null) return "Unknown error";
        // Strip potential injection chars
        return msg.replaceAll("[<>\"'\\\\]", "").substring(0, Math.min(msg.length(), 200));
    }

    private void configureLogging() {
        // Log to stderr so stdout stays clean for JSON-RPC
        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        StreamHandler stderrHandler = new StreamHandler(System.err, new SimpleFormatter());
        stderrHandler.setLevel(Level.ALL);
        root.addHandler(stderrHandler);
        root.setLevel(Level.INFO);
    }

    public static void main(String[] args) throws IOException {
        new JitsiMcpServer().start();
    }
}
