package com.ecoskiller.analytics.mcp;

import com.ecoskiller.analytics.mcp.security.JwtValidator;
import com.ecoskiller.analytics.mcp.security.RateLimiter;
import com.ecoskiller.analytics.mcp.security.AuditLogger;
import com.ecoskiller.analytics.mcp.tools.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Ecoskiller | Analytics-Service MCP Server (Java)
 * CAT-ANALYTICS — Real-Time Analytics, Dashboards & Business Intelligence
 *
 * 32 tools covering:
 *   • Funnel Analytics        • Recruiter Performance
 *   • Candidate Score History • Platform Health & KPIs
 *   • Dashboard Management    • Event Ingestion Status
 *   • Leaderboards            • Kafka Consumer Monitoring
 *   • Compliance / DPDPA      • Anomaly Detection & Alerts
 *   • Data Export             • Bias Monitoring
 *
 * Security:
 *   - JWT validation (Keycloak OIDC)
 *   - Role-Based Access Control (RBAC)
 *   - Tenant-scoped query isolation (multi-tenant)
 *   - Per-admin rate limiting
 *   - Immutable audit trail
 *   - Input sanitisation (SQL injection, XSS, null-byte)
 */
public class AnalyticsMcpServer {

    private static final Logger LOG = Logger.getLogger(AnalyticsMcpServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "mcp-analytics-service";
    private static final String SERVER_VER  = "1.0.0";

    private final Map<String, ToolHandler> tools = new HashMap<>();
    private final JwtValidator  jwtValidator;
    private final RateLimiter   rateLimiter;
    private final AuditLogger   auditLogger;

    public AnalyticsMcpServer() {
        this.jwtValidator = new JwtValidator();
        this.rateLimiter  = new RateLimiter(120, 60_000); // 120 req/min
        this.auditLogger  = new AuditLogger();
        registerAllTools();
    }

    // ── Tool registration ────────────────────────────────────────────────────

    private void registerAllTools() {

        // 1. Dashboard
        register(new DashboardGetTool());
        register(new DashboardDataTool(auditLogger));
        register(new DashboardListTool());
        register(new DashboardCreateTool(auditLogger));

        // 2. Funnel Analytics
        register(new FunnelJobTool(auditLogger));
        register(new FunnelPlatformTool(auditLogger));
        register(new FunnelCohortTool(auditLogger));

        // 3. Recruiter Analytics
        register(new RecruiterPerformanceTool(auditLogger));
        register(new RecruiterTeamBenchmarkTool(auditLogger));
        register(new RecruiterNoShowTool(auditLogger));
        register(new RecruiterLeaderboardTool(auditLogger));
        register(new RecruiterTimeToHireTool(auditLogger));

        // 4. Candidate Analytics
        register(new CandidateScoreHistoryTool(auditLogger));
        register(new CandidateProgressTool(auditLogger));
        register(new CandidateScoreBreakdownTool(auditLogger));
        register(new CandidateBeltAdvancementTool(auditLogger));

        // 5. Platform Health & KPIs
        register(new PlatformHealthTool());
        register(new PlatformKpiTool());
        register(new PlatformModelPerformanceTool());

        // 6. Kafka / Event Ingestion
        register(new KafkaConsumerLagTool());
        register(new EventIngestionStatusTool());
        register(new EventIngestionHistoryTool(auditLogger));

        // 7. Leaderboard & Jobs
        register(new JobLeaderboardTool(auditLogger));
        register(new JobFunnelDailyTool(auditLogger));

        // 8. Anomaly Detection & Alerts
        register(new AnomalyDetectTool());
        register(new AlertRuleListTool());
        register(new AlertRuleCreateTool(auditLogger));

        // 9. Compliance & DPDPA
        register(new ComplianceAuditLogTool(auditLogger));
        register(new ComplianceDataExportTool(auditLogger));
        register(new BiasMontitoringTool(auditLogger));

        // 10. Data Export
        register(new ReportExportTool(auditLogger));
        register(new MetricsPrometheousTool());
    }

    private void register(ToolHandler h) { tools.put(h.getName(), h); }

    // ── MCP stdio loop ───────────────────────────────────────────────────────

    public void run() throws IOException {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in,  StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

        LOG.info("[AnalyticsMCPServer] Listening on stdio…");

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            JSONObject resp;
            try {
                resp = dispatch(new JSONObject(line));
            } catch (Exception e) {
                resp = errorResp(null, -32700, "Parse error: " + sanitize(e.getMessage()));
            }
            out.println(resp.toString());
        }
    }

    // ── Dispatch ─────────────────────────────────────────────────────────────

    private JSONObject dispatch(JSONObject req) {
        Object id     = req.opt("id");
        String method = req.optString("method", "");
        try {
            return switch (method) {
                case "initialize" -> handleInit(req, id);
                case "ping"       -> pong(id);
                case "tools/list" -> handleToolsList(req, id);
                case "tools/call" -> handleToolCall(req, id);
                default           -> errorResp(id, -32601, "Method not found: " + method);
            };
        } catch (SecurityException se) {
            auditLogger.logSecurityEvent("BLOCKED", method, se.getMessage());
            return errorResp(id, 403, "Forbidden: " + sanitize(se.getMessage()));
        } catch (Exception e) {
            LOG.warning("Dispatch error: " + e.getMessage());
            return errorResp(id, -32603, "Internal error");
        }
    }

    // ── Handlers ─────────────────────────────────────────────────────────────

    private JSONObject handleInit(JSONObject req, Object id) {
        return successResp(id, new JSONObject()
            .put("protocolVersion", MCP_VERSION)
            .put("serverInfo", new JSONObject()
                .put("name",    SERVER_NAME)
                .put("version", SERVER_VER)
                .put("description", "Ecoskiller Analytics Service — Real-Time Analytics, Dashboards & BI"))
            .put("capabilities", new JSONObject()
                .put("tools", new JSONObject().put("listChanged", false))));
    }

    private JSONObject handleToolsList(JSONObject req, Object id) {
        validateAuth(req);
        JSONArray arr = new JSONArray();
        for (ToolHandler h : tools.values()) {
            arr.put(new JSONObject()
                .put("name",        h.getName())
                .put("description", h.getDescription())
                .put("inputSchema", h.getInputSchema()));
        }
        return successResp(id, new JSONObject().put("tools", arr));
    }

    private JSONObject handleToolCall(JSONObject req, Object id) {
        JSONObject params   = req.optJSONObject("params");
        if (params == null) params = new JSONObject();
        String     toolName = params.optString("name", "");
        JSONObject args     = params.optJSONObject("arguments");
        if (args == null) args = new JSONObject();

        // Security gate
        String adminId = validateAuth(req);
        rateLimiter.check(adminId);

        ToolHandler handler = tools.get(toolName);
        if (handler == null) return errorResp(id, -32601, "Unknown tool: " + toolName);

        // RBAC
        handler.checkRole(adminId, jwtValidator.getRoles(req));

        // Tenant isolation — inject tenantId from JWT into args
        String tenantId = jwtValidator.getTenantId(req);
        args.put("_tenant_id", tenantId);

        // Input sanitisation
        sanitizeArgs(args);

        JSONObject result = handler.execute(args, adminId);
        auditLogger.logToolCall(adminId, toolName, args, result);

        return successResp(id, new JSONObject()
            .put("content", new JSONArray().put(new JSONObject()
                .put("type", "text")
                .put("text", result.toString(2)))));
    }

    // ── Auth helpers ──────────────────────────────────────────────────────────

    private String validateAuth(JSONObject req) {
        JSONObject meta = req.optJSONObject("_meta");
        String token = (meta != null) ? meta.optString("authorization", "") : "";
        if (token.startsWith("Bearer ")) token = token.substring(7);
        return jwtValidator.validate(token);
    }

    // ── Input sanitisation ────────────────────────────────────────────────────

    private void sanitizeArgs(JSONObject args) {
        for (String key : args.keySet()) {
            Object val = args.get(key);
            if (val instanceof String s) {
                String clean = s
                    .replace("\u0000", "")
                    .replaceAll("(?i)<script[^>]*>.*?</script>", "")
                    .replaceAll("(?i)(;\\s*drop\\s+|;\\s*delete\\s+|'\\s*or\\s+'1'='1)", "")
                    .trim();
                if (clean.length() > 10_000) clean = clean.substring(0, 10_000);
                args.put(key, clean);
            }
        }
    }

    private static String sanitize(String s) {
        if (s == null) return "unknown";
        return s.replaceAll("[\r\n\t]", " ").substring(0, Math.min(s.length(), 200));
    }

    // ── Response builders ─────────────────────────────────────────────────────

    static JSONObject successResp(Object id, JSONObject result) {
        return new JSONObject().put("jsonrpc","2.0").put("id",id).put("result",result);
    }
    static JSONObject errorResp(Object id, int code, String msg) {
        return new JSONObject().put("jsonrpc","2.0").put("id",id)
            .put("error", new JSONObject().put("code",code).put("message",msg));
    }
    private static JSONObject pong(Object id) {
        return successResp(id, new JSONObject().put("pong", true));
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    public static void main(String[] args) throws Exception {
        new AnalyticsMcpServer().run();
    }
}
