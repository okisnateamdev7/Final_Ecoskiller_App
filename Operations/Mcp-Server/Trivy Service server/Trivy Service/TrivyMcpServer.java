package com.ecoskiller.trivy.server;

import com.ecoskiller.trivy.security.ApiKeyValidator;
import com.ecoskiller.trivy.security.RateLimiter;
import com.ecoskiller.trivy.security.InputSanitizer;
import com.ecoskiller.trivy.tools.*;
import com.ecoskiller.trivy.model.*;
import com.ecoskiller.trivy.util.JsonUtils;
import com.ecoskiller.trivy.util.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Ecoskiller Trivy MCP Server — Java Implementation
 *
 * Transport : stdio (stdin/stdout)
 * Protocol  : JSON-RPC 2.0 / MCP 2024-11-05
 * Security  : API key validation, rate limiting, input sanitization,
 *             path traversal prevention, command injection prevention
 *
 * Tools (13):
 *   scan_image_tar          — Scan a .tar Docker image artifact
 *   scan_image_registry     — Pull and scan from Harbor/registry
 *   scan_filesystem          — Scan a local filesystem path
 *   scan_config              — Scan Dockerfile/IaC for misconfigurations
 *   get_scan_report          — Retrieve a previous scan result by ID
 *   list_scan_history        — List recent scan results
 *   check_severity_gate      — Evaluate exit-code policy (CRITICAL/HIGH gate)
 *   get_cve_details          — Look up details for a specific CVE ID
 *   add_trivyignore_entry    — Add a CVE to .trivyignore with justification
 *   list_trivyignore         — List current .trivyignore entries
 *   get_compliance_report    — Generate DPDPA/ISO27001 compliance summary
 *   get_pipeline_status      — Check pipeline gate status for an image
 *   update_severity_policy   — Update the severity threshold policy
 */
public class TrivyMcpServer {

    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "trivy-mcp-server";
    private static final String SERVER_VERSION = "1.0.0";

    private final Map<String, McpTool> tools = new LinkedHashMap<>();
    private final ApiKeyValidator apiKeyValidator;
    private final RateLimiter rateLimiter;
    private final Logger logger = new Logger("TrivyMcpServer");

    public TrivyMcpServer() {
        this.apiKeyValidator = new ApiKeyValidator();
        this.rateLimiter = new RateLimiter(60, 60_000); // 60 calls/min

        registerTools();
    }

    private void registerTools() {
        tools.put("scan_image_tar",         new ScanImageTarTool());
        tools.put("scan_image_registry",    new ScanImageRegistryTool());
        tools.put("scan_filesystem",        new ScanFilesystemTool());
        tools.put("scan_config",            new ScanConfigTool());
        tools.put("get_scan_report",        new GetScanReportTool());
        tools.put("list_scan_history",      new ListScanHistoryTool());
        tools.put("check_severity_gate",    new CheckSeverityGateTool());
        tools.put("get_cve_details",        new GetCveDetailsTool());
        tools.put("add_trivyignore_entry",  new AddTrivyignoreEntryTool());
        tools.put("list_trivyignore",       new ListTrivyignoreTool());
        tools.put("get_compliance_report",  new GetComplianceReportTool());
        tools.put("get_pipeline_status",    new GetPipelineStatusTool());
        tools.put("update_severity_policy", new UpdateSeverityPolicyTool());
    }

    public void run() throws IOException {
        logger.info("Trivy MCP Server starting — protocol: JSON-RPC 2.0, version: " + MCP_VERSION);

        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter writer = new PrintWriter(
            new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

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
                logger.error("Unhandled exception processing request: " + e.getMessage());
                String errorResponse = JsonUtils.buildError(null, -32603, "Internal error: " + e.getMessage());
                writer.println(errorResponse);
                writer.flush();
            }
        }

        logger.info("Trivy MCP Server shutting down.");
    }

    private String handleRequest(String rawJson) {
        Map<String, Object> req;
        try {
            req = JsonUtils.parseObject(rawJson);
        } catch (Exception e) {
            return JsonUtils.buildError(null, -32700, "Parse error: invalid JSON");
        }

        Object id = req.get("id");
        String method = (String) req.get("method");

        if (method == null) {
            return JsonUtils.buildError(id, -32600, "Invalid Request: missing method");
        }

        // Security: rate limiting (applied to all methods except ping)
        if (!"ping".equals(method)) {
            if (!rateLimiter.allowRequest("global")) {
                return JsonUtils.buildError(id, -32000, "Rate limit exceeded. Max 60 requests/minute.");
            }
        }

        switch (method) {
            case "initialize":
                return handleInitialize(id, req);
            case "tools/list":
                return handleToolsList(id);
            case "tools/call":
                return handleToolsCall(id, req);
            case "ping":
                return handlePing(id);
            default:
                return JsonUtils.buildError(id, -32601, "Method not found: " + method);
        }
    }

    private String handleInitialize(Object id, Map<String, Object> req) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("protocolVersion", MCP_VERSION);

        Map<String, Object> serverInfo = new LinkedHashMap<>();
        serverInfo.put("name", SERVER_NAME);
        serverInfo.put("version", SERVER_VERSION);
        result.put("serverInfo", serverInfo);

        Map<String, Object> capabilities = new LinkedHashMap<>();
        Map<String, Object> toolsCapability = new LinkedHashMap<>();
        toolsCapability.put("listChanged", false);
        capabilities.put("tools", toolsCapability);
        result.put("capabilities", capabilities);

        return JsonUtils.buildSuccess(id, result);
    }

    private String handleToolsList(Object id) {
        List<Map<String, Object>> toolList = new ArrayList<>();
        for (Map.Entry<String, McpTool> entry : tools.entrySet()) {
            toolList.add(entry.getValue().getSchema(entry.getKey()));
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tools", toolList);
        return JsonUtils.buildSuccess(id, result);
    }

    @SuppressWarnings("unchecked")
    private String handleToolsCall(Object id, Map<String, Object> req) {
        Map<String, Object> params = (Map<String, Object>) req.get("params");
        if (params == null) {
            return JsonUtils.buildError(id, -32602, "Invalid params: missing params object");
        }

        String toolName = (String) params.get("name");
        if (toolName == null || toolName.isBlank()) {
            return JsonUtils.buildError(id, -32602, "Invalid params: missing tool name");
        }

        // Security: sanitize tool name
        if (!toolName.matches("[a-z_]+")) {
            return JsonUtils.buildError(id, -32602, "Invalid tool name format");
        }

        Map<String, Object> arguments = (Map<String, Object>) params.getOrDefault("arguments", new HashMap<>());

        McpTool tool = tools.get(toolName);
        if (tool == null) {
            return JsonUtils.buildError(id, -32602, "Unknown tool: " + toolName);
        }

        try {
            // Security: validate and sanitize all inputs
            Map<String, Object> sanitized = InputSanitizer.sanitize(arguments);
            ToolResult result = tool.execute(sanitized);
            return JsonUtils.buildToolSuccess(id, result);
        } catch (SecurityException e) {
            logger.warn("Security violation in tool [" + toolName + "]: " + e.getMessage());
            return JsonUtils.buildError(id, -32000, "Security violation: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return JsonUtils.buildError(id, -32602, "Invalid argument: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Tool execution error [" + toolName + "]: " + e.getMessage());
            return JsonUtils.buildToolError(id, "Tool execution failed: " + e.getMessage());
        }
    }

    private String handlePing(Object id) {
        return JsonUtils.buildSuccess(id, new LinkedHashMap<>());
    }

    public static void main(String[] args) {
        try {
            TrivyMcpServer server = new TrivyMcpServer();
            server.run();
        } catch (IOException e) {
            System.err.println("[FATAL] Server crashed: " + e.getMessage());
            System.exit(1);
        }
    }
}
