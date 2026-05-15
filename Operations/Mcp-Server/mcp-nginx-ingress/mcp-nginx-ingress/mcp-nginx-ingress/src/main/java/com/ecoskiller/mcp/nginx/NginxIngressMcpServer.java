package com.ecoskiller.mcp.nginx;

import com.ecoskiller.mcp.nginx.config.ServerConfig;
import com.ecoskiller.mcp.nginx.security.RequestValidator;
import com.ecoskiller.mcp.nginx.tools.*;
import com.ecoskiller.mcp.nginx.util.JsonUtil;
import com.ecoskiller.mcp.nginx.util.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

/**
 * NGINX Ingress Controller MCP Server
 * Ecoskiller Platform — CAT-NGINX — Kubernetes Edge Gateway Management
 * Transport: stdio | Protocol: JSON-RPC 2.0 | MCP Version: 2024-11-05
 *
 * Security: All inputs validated, all tool calls authenticated,
 *           secrets never logged, audit trail maintained.
 */
public class NginxIngressMcpServer {

    private static final String SERVER_NAME    = "mcp-nginx-ingress";
    private static final String SERVER_VERSION = "1.0.0";
    private static final String MCP_VERSION    = "2024-11-05";

    private final Map<String, NginxTool> tools = new LinkedHashMap<>();
    private final RequestValidator       validator;
    private final ServerConfig           config;
    private final Logger                 logger;
    private volatile boolean             running = true;

    // ─── Boot ──────────────────────────────────────────────────────────────

    public NginxIngressMcpServer() {
        this.config    = ServerConfig.load();
        this.logger    = new Logger(config.getLogLevel());
        this.validator = new RequestValidator(config);
        registerTools();
        logger.info("NginxIngressMcpServer initialised — " + tools.size() + " tools registered");
    }

    private void registerTools() {
        tools.put("ingress_route_manager",       new IngressRouteManagerTool(config));
        tools.put("ssl_tls_manager",             new SslTlsManagerTool(config));
        tools.put("rate_limit_controller",       new RateLimitControllerTool(config));
        tools.put("load_balancer_config",        new LoadBalancerConfigTool(config));
        tools.put("health_check_monitor",        new HealthCheckMonitorTool(config));
        tools.put("waf_security_manager",        new WafSecurityManagerTool(config));
        tools.put("auth_enforcement",            new AuthEnforcementTool(config));
        tools.put("canary_deployment",           new CanaryDeploymentTool(config));
        tools.put("certificate_lifecycle",       new CertificateLifecycleTool(config));
        tools.put("traffic_shaping",             new TrafficShapingTool(config));
        tools.put("websocket_proxy",             new WebSocketProxyTool(config));
        tools.put("request_rewrite_engine",      new RequestRewriteEngineTool(config));
        tools.put("observability_exporter",      new ObservabilityExporterTool(config));
        tools.put("backend_upstream_manager",    new BackendUpstreamManagerTool(config));
        tools.put("configmap_hot_reload",        new ConfigMapHotReloadTool(config));
        tools.put("multi_cloud_ingress",         new MultiCloudIngressTool(config));
        tools.put("ddos_protection",             new DdosProtectionTool(config));
        tools.put("audit_compliance",            new AuditComplianceTool(config));
    }

    // ─── Main stdin/stdout loop ────────────────────────────────────────────

    public void run() throws IOException {
        BufferedReader  in  = new BufferedReader(new InputStreamReader(System.in,  StandardCharsets.UTF_8));
        PrintWriter     out = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

        logger.info("MCP server listening on stdio…");

        String line;
        while (running && (line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                String response = dispatch(line);
                if (response != null) {
                    out.println(response);
                }
            } catch (Exception e) {
                logger.error("Dispatch error: " + e.getMessage());
                String id = tryExtractId(line);
                out.println(JsonUtil.errorResponse(id, -32603, "Internal error: " + sanitise(e.getMessage())));
            }
        }
    }

    // ─── JSON-RPC 2.0 Dispatcher ──────────────────────────────────────────

    private String dispatch(String raw) {
        Map<String, Object> req;
        try {
            req = JsonUtil.parseObject(raw);
        } catch (Exception e) {
            return JsonUtil.errorResponse(null, -32700, "Parse error");
        }

        if (!validator.validateJsonRpc(req)) {
            return JsonUtil.errorResponse(req.get("id"), -32600, "Invalid Request");
        }

        String id     = req.get("id") != null ? req.get("id").toString() : null;
        String method = (String) req.get("method");

        logger.debug("→ " + method + " [id=" + id + "]");

        switch (method) {
            case "initialize":       return handleInitialize(id, req);
            case "tools/list":       return handleToolsList(id);
            case "tools/call":       return handleToolsCall(id, req);
            case "ping":             return JsonUtil.successResponse(id, Map.of());
            case "shutdown":         running = false; return JsonUtil.successResponse(id, Map.of());
            default:
                return JsonUtil.errorResponse(id, -32601, "Method not found: " + method);
        }
    }

    // ─── Handlers ─────────────────────────────────────────────────────────

    private String handleInitialize(String id, Map<String, Object> req) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("protocolVersion", MCP_VERSION);
        result.put("serverInfo", Map.of(
            "name",    SERVER_NAME,
            "version", SERVER_VERSION
        ));
        result.put("capabilities", Map.of(
            "tools",    Map.of("listChanged", false),
            "security", Map.of("inputValidation", true, "auditLogging", true)
        ));
        logger.info("Client initialised — protocol " + MCP_VERSION);
        return JsonUtil.successResponse(id, result);
    }

    private String handleToolsList(String id) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map.Entry<String, NginxTool> entry : tools.entrySet()) {
            list.add(entry.getValue().descriptor());
        }
        return JsonUtil.successResponse(id, Map.of("tools", list));
    }

    private String handleToolsCall(String id, Map<String, Object> req) {
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) req.getOrDefault("params", Map.of());
        String toolName  = (String) params.get("name");
        @SuppressWarnings("unchecked")
        Map<String, Object> args = (Map<String, Object>) params.getOrDefault("arguments", Map.of());

        if (toolName == null || !tools.containsKey(toolName)) {
            return JsonUtil.errorResponse(id, -32602, "Unknown tool: " + toolName);
        }

        // Security: validate & sanitise all arguments before dispatch
        try {
            validator.validateToolArguments(toolName, args);
        } catch (SecurityException se) {
            logger.warn("Security violation on tool=" + toolName + ": " + se.getMessage());
            return JsonUtil.errorResponse(id, -32602, "Security validation failed: " + se.getMessage());
        }

        NginxTool tool = tools.get(toolName);
        logger.info("Calling tool: " + toolName);

        try {
            Object result = tool.execute(args);
            logger.info("Tool " + toolName + " succeeded");
            return JsonUtil.successResponse(id, Map.of(
                "content", List.of(Map.of("type", "text", "text", JsonUtil.toJson(result)))
            ));
        } catch (Exception e) {
            logger.error("Tool " + toolName + " failed: " + e.getMessage());
            return JsonUtil.errorResponse(id, -32603, "Tool execution error: " + sanitise(e.getMessage()));
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────

    private String tryExtractId(String raw) {
        try {
            Map<String, Object> m = JsonUtil.parseObject(raw);
            return m.get("id") != null ? m.get("id").toString() : null;
        } catch (Exception e) { return null; }
    }

    /** Strip stack-trace details from user-facing messages. */
    private String sanitise(String msg) {
        if (msg == null) return "Unknown error";
        // Never expose file paths or internal class names
        return msg.replaceAll("(at com\\.|java\\.|sun\\.|\\$)[^\\s]+", "[internal]")
                  .replaceAll("/[^\\s]+\\.java:[0-9]+", "[source]");
    }

    // ─── Entry ────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        try {
            new NginxIngressMcpServer().run();
        } catch (Exception e) {
            System.err.println("[FATAL] " + e.getMessage());
            System.exit(1);
        }
    }
}
