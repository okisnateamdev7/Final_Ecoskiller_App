package com.ecoskiller.mcp.nat.server;

import com.ecoskiller.mcp.nat.agents.*;
import com.ecoskiller.mcp.nat.model.*;
import com.ecoskiller.mcp.nat.security.SecurityManager;
import com.ecoskiller.mcp.nat.util.JsonUtil;
import com.ecoskiller.mcp.nat.util.Logger;

import java.io.*;
import java.util.*;

/**
 * Linux NAT Gateway MCP Server
 * Ecoskiller | Network Address Translation Gateway — Egress Traffic Management
 * Multi-Cloud: GCP (asia-south1) + AWS (ap-south-1)
 * MCP Protocol: JSON-RPC 2.0 over stdio
 *
 * Security: Input validation, rate limiting, audit logging on all tool calls
 */
public class NatGatewayMcpServer {

    private static final String SERVER_NAME    = "mcp-nat-gateway";
    private static final String SERVER_VERSION = "1.0.0";
    private static final String MCP_VERSION    = "2024-11-05";

    private final Map<String, NatAgent> agents = new LinkedHashMap<>();
    private final SecurityManager securityManager = new SecurityManager();
    private final Logger logger = new Logger("NatGatewayMcpServer");

    public NatGatewayMcpServer() {
        registerAgents();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Agent Registration
    // ─────────────────────────────────────────────────────────────────────────

    private void registerAgents() {
        register(new SnatMasqueradeAgent());
        register(new ConntrackManagerAgent());
        register(new EgressPolicyAgent());
        register(new VrrpFailoverAgent());
        register(new PerformanceTuningAgent());
        register(new TrafficMonitorAgent());
        register(new IptablesRulesAgent());
        register(new PacketFlowDiagnosticsAgent());
        register(new NatSessionTimeoutAgent());
        register(new MtuFragmentationAgent());
        register(new AuditTrailAgent());
        register(new PrometheusMetricsAgent());
        register(new HealthCheckAgent());
        register(new KeepalivdConfigAgent());
        register(new EgressWhitelistAgent());
        register(new ConnectionRateLimiterAgent());
        register(new SysctlParametersAgent());
        register(new GatewayStatusAgent());
    }

    private void register(NatAgent agent) {
        agents.put(agent.toolName(), agent);
        logger.info("Registered agent: " + agent.toolName());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Main stdio loop
    // ─────────────────────────────────────────────────────────────────────────

    public void run() throws IOException {
        logger.info("Linux NAT Gateway MCP Server starting — " + SERVER_NAME + " v" + SERVER_VERSION);
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String response = handleRequest(line);
            if (response != null) {
                out.println(response);
                out.flush();
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // JSON-RPC Dispatch
    // ─────────────────────────────────────────────────────────────────────────

    private String handleRequest(String rawJson) {
        Map<String, Object> req;
        try {
            req = JsonUtil.parseObject(rawJson);
        } catch (Exception e) {
            return JsonUtil.errorResponse(null, -32700, "Parse error: " + e.getMessage());
        }

        Object id     = req.get("id");
        String method = (String) req.get("method");

        if (!securityManager.validateRequest(req)) {
            logger.warn("SECURITY: Request rejected — validation failed for method: " + method);
            return JsonUtil.errorResponse(id, -32600, "Invalid Request: security validation failed");
        }

        if (method == null) {
            return JsonUtil.errorResponse(id, -32600, "Invalid Request: missing method");
        }

        try {
            return switch (method) {
                case "initialize"  -> handleInitialize(id, req);
                case "tools/list"  -> handleToolsList(id);
                case "tools/call"  -> handleToolCall(id, req);
                case "ping"        -> JsonUtil.successResponse(id, Map.of("pong", true));
                default            -> JsonUtil.errorResponse(id, -32601, "Method not found: " + method);
            };
        } catch (Exception e) {
            logger.error("Unhandled error for method " + method + ": " + e.getMessage());
            return JsonUtil.errorResponse(id, -32603, "Internal error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MCP Protocol Handlers
    // ─────────────────────────────────────────────────────────────────────────

    private String handleInitialize(Object id, Map<String, Object> req) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("protocolVersion", MCP_VERSION);
        result.put("serverInfo", Map.of(
            "name",    SERVER_NAME,
            "version", SERVER_VERSION
        ));
        result.put("capabilities", Map.of(
            "tools", Map.of("listChanged", false)
        ));
        logger.info("Client initialized — MCP handshake complete");
        return JsonUtil.successResponse(id, result);
    }

    private String handleToolsList(Object id) {
        List<Map<String, Object>> tools = new ArrayList<>();
        for (NatAgent agent : agents.values()) {
            tools.add(agent.toolDefinition());
        }
        return JsonUtil.successResponse(id, Map.of("tools", tools));
    }

    @SuppressWarnings("unchecked")
    private String handleToolCall(Object id, Map<String, Object> req) {
        Map<String, Object> params = (Map<String, Object>) req.getOrDefault("params", Map.of());
        String toolName  = (String) params.get("name");
        Map<String, Object> args = (Map<String, Object>) params.getOrDefault("arguments", Map.of());

        if (toolName == null || toolName.isBlank()) {
            return JsonUtil.errorResponse(id, -32602, "Invalid params: missing tool name");
        }

        // Security: sanitize all input args
        args = securityManager.sanitizeArgs(args);

        NatAgent agent = agents.get(toolName);
        if (agent == null) {
            return JsonUtil.errorResponse(id, -32601, "Unknown tool: " + toolName);
        }

        logger.audit("TOOL_CALL tool=" + toolName + " args=" + args);

        try {
            ToolResult result = agent.execute(args, securityManager);
            logger.audit("TOOL_RESULT tool=" + toolName + " status=" + result.status());
            return JsonUtil.successResponse(id, Map.of(
                "content", List.of(Map.of("type", "text", "text", result.toJson()))
            ));
        } catch (SecurityException se) {
            logger.warn("SECURITY VIOLATION on tool=" + toolName + ": " + se.getMessage());
            return JsonUtil.errorResponse(id, -32600, "Security violation: " + se.getMessage());
        } catch (Exception e) {
            logger.error("Tool execution failed: tool=" + toolName + " error=" + e.getMessage());
            return JsonUtil.errorResponse(id, -32603, "Tool execution error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Entry Point
    // ─────────────────────────────────────────────────────────────────────────

    public static void main(String[] args) throws IOException {
        new NatGatewayMcpServer().run();
    }
}
