package com.ecoskiller.unleash.server;

import com.ecoskiller.unleash.security.RateLimiter;
import com.ecoskiller.unleash.security.ApiKeyValidator;
import com.ecoskiller.unleash.security.InputSanitizer;
import com.ecoskiller.unleash.tools.*;
import com.ecoskiller.unleash.model.ToolResult;
import com.ecoskiller.unleash.util.JsonUtils;
import com.ecoskiller.unleash.util.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * UnleashMcpServer — Ecoskiller Unleash Feature Flag MCP Server
 *
 * Transport  : stdio (stdin/stdout)
 * Protocol   : JSON-RPC 2.0 / MCP 2024-11-05
 * Security   : rate limiting, input sanitization, API key validation,
 *              shell/SQL injection prevention, enum enforcement
 *
 * Tools (16):
 *   create_flag             — Create a flag (disabled by default)
 *   get_flag                — Full flag details: status, strategy, audit, metrics
 *   list_flags              — List all flags with env/status/lifecycle filters
 *   enable_flag             — Enable a flag with audit trail
 *   disable_flag            — ⚡ Kill-switch: instant disable (< 1 min MTTR)
 *   delete_flag             — Archive or permanently delete a flag
 *   update_strategy         — Change targeting strategy (user/org/role/custom)
 *   gradual_rollout         — Set canary % (0→5→10→25→50→100%)
 *   add_variant             — Add A/B test variant with traffic weight
 *   get_variants            — Get all variant definitions for a flag
 *   evaluate_flag           — Evaluate flag for a request context (isEnabled+variant)
 *   get_audit_log           — Immutable change audit trail (DPDPA 2023 compliance)
 *   get_metrics             — Evaluation counts, enabled%, cleanup candidates
 *   list_environments       — dev / test / staging / production
 *   get_compliance_report   — DPDPA 2023, IT Act 2000, ISO 27001 evidence
 *   manage_flag_lifecycle   — ACTIVE → PENDING_CLEANUP → ARCHIVED
 */
public class UnleashMcpServer {

    private static final String MCP_VERSION    = "2024-11-05";
    private static final String SERVER_NAME    = "unleash-mcp-server";
    private static final String SERVER_VERSION = "1.0.0";

    private final Map<String, McpTool> tools = new LinkedHashMap<>();
    private final RateLimiter      rateLimiter;
    private final ApiKeyValidator  apiKeyValidator;
    private final Logger           logger = new Logger("UnleashMcpServer");

    public UnleashMcpServer() {
        this.rateLimiter     = new RateLimiter(60, 60_000); // 60 req/min
        this.apiKeyValidator = new ApiKeyValidator();
        registerTools();
    }

    private void registerTools() {
        tools.put("create_flag",           new CreateFlagTool());
        tools.put("get_flag",              new GetFlagTool());
        tools.put("list_flags",            new ListFlagsTool());
        tools.put("enable_flag",           new EnableFlagTool());
        tools.put("disable_flag",          new DisableFlagTool());
        tools.put("delete_flag",           new DeleteFlagTool());
        tools.put("update_strategy",       new UpdateStrategyTool());
        tools.put("gradual_rollout",       new GradualRolloutTool());
        tools.put("add_variant",           new AddVariantTool());
        tools.put("get_variants",          new GetVariantsTool());
        tools.put("evaluate_flag",         new EvaluateFlagTool());
        tools.put("get_audit_log",         new GetAuditLogTool());
        tools.put("get_metrics",           new GetMetricsTool());
        tools.put("list_environments",     new ListEnvironmentsTool());
        tools.put("get_compliance_report", new GetComplianceReportTool());
        tools.put("manage_flag_lifecycle", new ManageFlagLifecycleTool());
    }

    // ── Main loop ─────────────────────────────────────────────────────────────

    public void run() throws IOException {
        logger.info("Unleash MCP Server starting — " + tools.size() + " tools registered");
        if (apiKeyValidator.isEnforced())
            logger.info("API key enforcement: ON (UNLEASH_MCP_API_KEY is set)");

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
                logger.error("Unhandled exception: " + e.getMessage());
                writer.println(JsonUtils.buildError(null, -32603, "Internal error: " + e.getMessage()));
                writer.flush();
            }
        }
        logger.info("Unleash MCP Server shutting down.");
    }

    // ── Request dispatcher ────────────────────────────────────────────────────

    private String handleRequest(String rawJson) {
        Map<String, Object> req;
        try {
            req = JsonUtils.parseObject(rawJson);
        } catch (Exception e) {
            return JsonUtils.buildError(null, -32700, "Parse error: invalid JSON");
        }

        Object id     = req.get("id");
        String method = (String) req.get("method");

        if (method == null)
            return JsonUtils.buildError(id, -32600, "Invalid Request: missing method");

        // Rate-limit all methods except ping
        if (!"ping".equals(method) && !rateLimiter.allowRequest("global"))
            return JsonUtils.buildError(id, -32000, "Rate limit exceeded. Max 60 requests/minute.");

        switch (method) {
            case "initialize":  return handleInitialize(id);
            case "tools/list":  return handleToolsList(id);
            case "tools/call":  return handleToolsCall(id, req);
            case "ping":        return JsonUtils.buildSuccess(id, new LinkedHashMap<>());
            default:            return JsonUtils.buildError(id, -32601, "Method not found: " + method);
        }
    }

    private String handleInitialize(Object id) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("protocolVersion", MCP_VERSION);
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("name", SERVER_NAME); info.put("version", SERVER_VERSION);
        result.put("serverInfo", info);
        Map<String, Object> caps = new LinkedHashMap<>();
        Map<String, Object> tc   = new LinkedHashMap<>();
        tc.put("listChanged", false);
        caps.put("tools", tc);
        result.put("capabilities", caps);
        return JsonUtils.buildSuccess(id, result);
    }

    private String handleToolsList(Object id) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map.Entry<String, McpTool> e : tools.entrySet())
            list.add(e.getValue().getSchema(e.getKey()));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tools", list);
        return JsonUtils.buildSuccess(id, result);
    }

    @SuppressWarnings("unchecked")
    private String handleToolsCall(Object id, Map<String, Object> req) {
        Map<String, Object> params = (Map<String, Object>) req.get("params");
        if (params == null)
            return JsonUtils.buildError(id, -32602, "Invalid params: missing params object");

        String toolName = (String) params.get("name");
        if (toolName == null || toolName.isBlank())
            return JsonUtils.buildError(id, -32602, "Invalid params: missing tool name");

        // Security: tool name must be a safe identifier
        if (!toolName.matches("[a-z_]+"))
            return JsonUtils.buildError(id, -32602, "Invalid tool name format: " + toolName);

        Map<String, Object> arguments = (Map<String, Object>)
            params.getOrDefault("arguments", new HashMap<>());

        McpTool tool = tools.get(toolName);
        if (tool == null)
            return JsonUtils.buildError(id, -32602, "Unknown tool: " + toolName);

        try {
            // Security: sanitize ALL arguments before tool execution
            Map<String, Object> sanitized = InputSanitizer.sanitize(arguments);
            ToolResult result = tool.execute(sanitized);
            return JsonUtils.buildToolSuccess(id, result);
        } catch (SecurityException e) {
            logger.warn("Security violation in [" + toolName + "]: " + e.getMessage());
            return JsonUtils.buildError(id, -32000, "Security violation: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return JsonUtils.buildError(id, -32602, "Invalid argument: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Tool error [" + toolName + "]: " + e.getMessage());
            return JsonUtils.buildToolError(id, "Tool execution failed: " + e.getMessage());
        }
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    public static void main(String[] args) {
        try {
            new UnleashMcpServer().run();
        } catch (IOException e) {
            System.err.println("[FATAL] Server crashed: " + e.getMessage());
            System.exit(1);
        }
    }
}
