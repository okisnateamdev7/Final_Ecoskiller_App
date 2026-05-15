package com.ecoskiller.unleash.server;

import com.ecoskiller.unleash.security.RateLimiter;
import com.ecoskiller.unleash.security.InputSanitizer;
import com.ecoskiller.unleash.model.ToolResult;
import com.ecoskiller.unleash.tools.*;
import com.ecoskiller.unleash.util.JsonUtils;
import com.ecoskiller.unleash.util.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * UnleashMcpServer — Ecoskiller Unleash Feature Flag MCP Server
 *
 * Transport : stdio (stdin/stdout)
 * Protocol  : JSON-RPC 2.0 / MCP 2024-11-05
 * Tools     : 18
 * Security  : InputSanitizer (10 layers) + RateLimiter + ApiKeyValidator + Audit trail
 *
 * Tools:
 *   1.  create_flag             — Create flag (always DISABLED, naming convention enforced)
 *   2.  get_flag                — Full details: status, strategy, variants, audit, metrics
 *   3.  list_flags              — List with env/status/lifecycle/tenant/strategy filters
 *   4.  enable_flag             — Enable with audit trail (DPDPA 2023 compliance)
 *   5.  disable_flag            — ⚡ KILL SWITCH — instant disable, < 100ms propagation
 *   6.  delete_flag             — ARCHIVE (keeps audit) or permanent DELETE
 *   7.  gradual_rollout         — Canary: 0→1→5→10→25→50→100% consistent hash
 *   8.  update_strategy         — USERID/TENANT/GROUP/CUSTOM_CONSTRAINT targeting
 *   9.  add_variant             — A/B arms with weighted deterministic assignment
 *   10. evaluate_flag           — Mirror SDK isEnabled() + getVariant() with reasoning
 *   11. schedule_flag           — enable_at / disable_at time-window activation
 *   12. get_audit_log           — Immutable change history, forensic analysis
 *   13. get_metrics             — Eval counts, enabled%, variant distribution, cleanup hints
 *   14. get_variants            — Variant config: weights, traffic%, evals, payloads
 *   15. manage_lifecycle        — ACTIVE → DEPRECATED → ARCHIVED (prevent flag explosion)
 *   16. list_environments       — dev/test/staging/production with SLA targets
 *   17. get_compliance_report   — DPDPA 2023, IT Act 2000, ISO 27001 audit evidence
 *   18. bulk_export_import      — Export all flags as portable JSON snapshot (GitOps)
 */
public class UnleashMcpServer {

    private static final String MCP_VERSION    = "2024-11-05";
    private static final String SERVER_NAME    = "unleash-mcp-server";
    private static final String SERVER_VERSION = "1.0.0";

    private final Map<String, McpTool> tools = new LinkedHashMap<>();
    private final RateLimiter rateLimiter = new RateLimiter(60, 60_000);
    private final Logger logger = new Logger("UnleashMcpServer");

    public UnleashMcpServer() {
        tools.put("create_flag",           new CreateFlagTool());
        tools.put("get_flag",              new GetFlagTool());
        tools.put("list_flags",            new ListFlagsTool());
        tools.put("enable_flag",           new EnableFlagTool());
        tools.put("disable_flag",          new DisableFlagTool());
        tools.put("delete_flag",           new DeleteFlagTool());
        tools.put("gradual_rollout",       new GradualRolloutTool());
        tools.put("update_strategy",       new UpdateStrategyTool());
        tools.put("add_variant",           new AddVariantTool());
        tools.put("evaluate_flag",         new EvaluateFlagTool());
        tools.put("schedule_flag",         new ScheduleFlagTool());
        tools.put("get_audit_log",         new GetAuditLogTool());
        tools.put("get_metrics",           new GetMetricsTool());
        tools.put("get_variants",          new GetVariantsTool());
        tools.put("manage_lifecycle",      new ManageLifecycleTool());
        tools.put("list_environments",     new ListEnvironmentsTool());
        tools.put("get_compliance_report", new GetComplianceReportTool());
        tools.put("bulk_export_import",    new BulkExportImportTool());
    }

    // ── Server loop ───────────────────────────────────────────────────────────

    public void run() throws IOException {
        logger.info("Unleash MCP Server v" + SERVER_VERSION + " starting — " + tools.size() + " tools registered");
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in,  StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                String response = handle(line);
                if (response != null) { out.println(response); out.flush(); }
            } catch (Exception e) {
                logger.error("Unhandled: " + e.getMessage());
                out.println(JsonUtils.err(null, -32603, "Internal error: " + e.getMessage()));
                out.flush();
            }
        }
        logger.info("Server shutting down.");
    }

    // ── Request dispatch ──────────────────────────────────────────────────────

    private String handle(String raw) {
        Map<String, Object> req;
        try { req = JsonUtils.parse(raw); }
        catch (Exception e) { return JsonUtils.err(null, -32700, "Parse error: invalid JSON"); }

        Object id     = req.get("id");
        String method = (String) req.get("method");
        if (method == null) return JsonUtils.err(id, -32600, "Invalid Request: missing method");

        // Rate limit all non-ping calls
        if (!"ping".equals(method) && !rateLimiter.allow("global"))
            return JsonUtils.err(id, -32000, "Rate limit exceeded — max 60 requests/minute.");

        switch (method) {
            case "initialize":  return handleInitialize(id);
            case "tools/list":  return handleToolsList(id);
            case "tools/call":  return handleToolsCall(id, req);
            case "ping":        return JsonUtils.ok(id, new LinkedHashMap<>());
            default:            return JsonUtils.err(id, -32601, "Method not found: " + method);
        }
    }

    private String handleInitialize(Object id) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("protocolVersion", MCP_VERSION);
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("name", SERVER_NAME); info.put("version", SERVER_VERSION);
        res.put("serverInfo", info);
        Map<String, Object> caps = new LinkedHashMap<>();
        Map<String, Object> tc   = new LinkedHashMap<>();
        tc.put("listChanged", false);
        caps.put("tools", tc);
        res.put("capabilities", caps);
        return JsonUtils.ok(id, res);
    }

    private String handleToolsList(Object id) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map.Entry<String, McpTool> e : tools.entrySet())
            list.add(e.getValue().getSchema(e.getKey()));
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("tools", list);
        return JsonUtils.ok(id, res);
    }

    @SuppressWarnings("unchecked")
    private String handleToolsCall(Object id, Map<String, Object> req) {
        Map<String, Object> params = (Map<String, Object>) req.get("params");
        if (params == null) return JsonUtils.err(id, -32602, "Missing params object");

        String toolName = (String) params.get("name");
        if (toolName == null || toolName.isBlank())
            return JsonUtils.err(id, -32602, "Missing tool name");

        // Security: validate tool name format before lookup
        if (!toolName.matches("[a-z_]+"))
            return JsonUtils.err(id, -32602, "Invalid tool name format: " + toolName);

        Map<String, Object> arguments = (Map<String, Object>)
            params.getOrDefault("arguments", new HashMap<>());

        McpTool tool = tools.get(toolName);
        if (tool == null) return JsonUtils.err(id, -32602, "Unknown tool: " + toolName);

        try {
            // Security: sanitize ALL arguments before passing to tool
            Map<String, Object> sanitized = InputSanitizer.sanitize(arguments);
            ToolResult result = tool.execute(sanitized);
            return JsonUtils.toolOk(id, result);
        } catch (SecurityException e) {
            logger.warn("Security violation [" + toolName + "]: " + e.getMessage());
            return JsonUtils.err(id, -32000, "Security violation: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return JsonUtils.err(id, -32602, "Invalid argument: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Tool error [" + toolName + "]: " + e.getMessage());
            return JsonUtils.toolErr(id, "Tool failed: " + e.getMessage());
        }
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    public static void main(String[] args) {
        try {
            new UnleashMcpServer().run();
        } catch (IOException e) {
            System.err.println("[FATAL] " + e.getMessage());
            System.exit(1);
        }
    }
}
