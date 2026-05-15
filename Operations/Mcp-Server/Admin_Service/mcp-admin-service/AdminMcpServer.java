package com.ecoskiller.admin.mcp;

import com.ecoskiller.admin.mcp.security.JwtValidator;
import com.ecoskiller.admin.mcp.security.RateLimiter;
import com.ecoskiller.admin.mcp.security.AuditLogger;
import com.ecoskiller.admin.mcp.tools.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Ecoskiller | Admin-Service MCP Server (Java)
 * CAT-ADMIN — Platform Administration, Compliance & Operations
 *
 * Security features:
 *  - JWT validation (Keycloak OIDC)
 *  - Role-Based Access Control (RBAC)
 *  - Request rate limiting
 *  - Immutable audit logging
 *  - Input sanitization
 *  - TLS enforcement hint in responses
 */
public class AdminMcpServer {

    private static final Logger LOG = Logger.getLogger(AdminMcpServer.class.getName());
    private static final String MCP_VERSION  = "2024-11-05";
    private static final String SERVER_NAME  = "mcp-admin-service";
    private static final String SERVER_VER   = "1.0.0";

    private final Map<String, ToolHandler> tools = new HashMap<>();
    private final JwtValidator    jwtValidator;
    private final RateLimiter     rateLimiter;
    private final AuditLogger     auditLogger;

    public AdminMcpServer() {
        this.jwtValidator = new JwtValidator();
        this.rateLimiter  = new RateLimiter(100, 60_000); // 100 req/min
        this.auditLogger  = new AuditLogger();
        registerAllTools();
    }

    // ── Tool registration ────────────────────────────────────────────────────

    private void registerAllTools() {
        // 1. Recruiter Management
        register(new RecruiterCreateTool(auditLogger));
        register(new RecruiterListTool());
        register(new RecruiterGetTool());
        register(new RecruiterUpdateTool(auditLogger));
        register(new RecruiterSuspendTool(auditLogger));
        register(new RecruiterDeleteTool(auditLogger));
        register(new RecruiterQuotaAdjustTool(auditLogger));

        // 2. DPDPA / Compliance
        register(new DsarCreateTool(auditLogger));
        register(new DsarListTool());
        register(new DsarGetTool());
        register(new DsarCompleteTool(auditLogger));
        register(new ErasureCreateTool(auditLogger));
        register(new ErasureListTool());

        // 3. Audit Log
        register(new AuditLogQueryTool());

        // 4. Questions & Content
        register(new QuestionListTool());
        register(new QuestionCreateTool(auditLogger));
        register(new QuestionUpdateTool(auditLogger));
        register(new QuestionDeleteTool(auditLogger));
        register(new QuestionABTestTool(auditLogger));
        register(new QuestionAnalyticsTool());

        // 5. Scoring Models
        register(new ModelListTool());
        register(new ModelMetricsTool());
        register(new ModelPromoteTool(auditLogger));
        register(new ModelRollbackTool(auditLogger));

        // 6. Score Override
        register(new ScoreOverrideTool(auditLogger));

        // 7. System Health
        register(new HealthServicesTool());
        register(new HealthKafkaTool());
        register(new HealthDatabaseTool());
        register(new HealthKubernetesTool());
        register(new ServiceLogsTool());

        // 8. Alerts & Configuration
        register(new AlertListTool());
        register(new AlertRulesListTool());
        register(new AlertRuleCreateTool(auditLogger));
        register(new AlertRuleUpdateTool(auditLogger));

        // 9. Reports & Exports
        register(new ReportGenerateTool());
        register(new ReportRecruiterPerformanceTool());
        register(new ReportAssessmentMetricsTool());
        register(new ReportComplianceTool());
        register(new DataExportTool(auditLogger));

        // 10. Billing / Invoice
        register(new InvoiceAdjustTool(auditLogger));
    }

    private void register(ToolHandler h) {
        tools.put(h.getName(), h);
    }

    // ── Main MCP loop ────────────────────────────────────────────────────────

    public void run() throws IOException {
        BufferedReader  in  = new BufferedReader(new InputStreamReader(System.in,  StandardCharsets.UTF_8));
        PrintWriter     out = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

        LOG.info("[AdminMCPServer] Listening on stdio…");

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            JSONObject resp;
            try {
                JSONObject req = new JSONObject(line);
                resp = dispatch(req);
            } catch (Exception e) {
                resp = errorResponse(null, -32700, "Parse error: " + sanitize(e.getMessage()));
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
                case "initialize"  -> handleInitialize(req, id);
                case "ping"        -> pong(id);
                case "tools/list"  -> handleToolsList(req, id);
                case "tools/call"  -> handleToolCall(req, id);
                default            -> errorResponse(id, -32601, "Method not found: " + method);
            };
        } catch (SecurityException se) {
            auditLogger.logSecurityEvent("BLOCKED", method, se.getMessage());
            return errorResponse(id, 403, "Forbidden: " + sanitize(se.getMessage()));
        } catch (Exception e) {
            LOG.warning("Dispatch error: " + e.getMessage());
            return errorResponse(id, -32603, "Internal error");
        }
    }

    // ── Handlers ─────────────────────────────────────────────────────────────

    private JSONObject handleInitialize(JSONObject req, Object id) {
        JSONObject result = new JSONObject()
            .put("protocolVersion", MCP_VERSION)
            .put("serverInfo", new JSONObject()
                .put("name",    SERVER_NAME)
                .put("version", SERVER_VER)
                .put("description", "Ecoskiller Admin Service — Platform Administration, Compliance & Operations")
            )
            .put("capabilities", new JSONObject()
                .put("tools", new JSONObject().put("listChanged", false))
            );
        return successResponse(id, result);
    }

    private JSONObject handleToolsList(JSONObject req, Object id) {
        // Security: require JWT for tools/list in production
        validateAuth(req);

        JSONArray arr = new JSONArray();
        for (ToolHandler h : tools.values()) {
            arr.put(new JSONObject()
                .put("name",        h.getName())
                .put("description", h.getDescription())
                .put("inputSchema", h.getInputSchema())
            );
        }
        return successResponse(id, new JSONObject().put("tools", arr));
    }

    private JSONObject handleToolCall(JSONObject req, Object id) {
        JSONObject params     = req.optJSONObject("params");
        if (params == null) params = new JSONObject();

        String toolName       = params.optString("name", "");
        JSONObject toolArgs   = params.optJSONObject("arguments");
        if (toolArgs == null) toolArgs = new JSONObject();

        // ── Security gate ────────────────────────────────────────────────────
        String adminId = validateAuth(req);
        rateLimiter.check(adminId);

        ToolHandler handler = tools.get(toolName);
        if (handler == null) {
            return errorResponse(id, -32601, "Unknown tool: " + toolName);
        }

        // Role check
        handler.checkRole(adminId, jwtValidator.getRoles(req));

        // Input sanitisation
        sanitizeArgs(toolArgs);

        // Execute
        JSONObject toolResult = handler.execute(toolArgs, adminId);

        // Audit all tool calls
        auditLogger.logToolCall(adminId, toolName, toolArgs, toolResult);

        return successResponse(id, new JSONObject()
            .put("content", new JSONArray().put(new JSONObject()
                .put("type", "text")
                .put("text", toolResult.toString(2))
            ))
        );
    }

    // ── Auth helpers ──────────────────────────────────────────────────────────

    private String validateAuth(JSONObject req) {
        JSONObject meta = req.optJSONObject("_meta");
        String token = (meta != null) ? meta.optString("authorization", "") : "";
        // Strip "Bearer " prefix if present
        if (token.startsWith("Bearer ")) token = token.substring(7);
        return jwtValidator.validate(token); // throws SecurityException if invalid
    }

    // ── Input sanitisation ────────────────────────────────────────────────────

    private void sanitizeArgs(JSONObject args) {
        for (String key : args.keySet()) {
            Object val = args.get(key);
            if (val instanceof String s) {
                // Strip SQL injection patterns, script tags, null bytes
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

    private static JSONObject successResponse(Object id, JSONObject result) {
        return new JSONObject()
            .put("jsonrpc", "2.0")
            .put("id",      id)
            .put("result",  result);
    }

    private static JSONObject errorResponse(Object id, int code, String msg) {
        return new JSONObject()
            .put("jsonrpc", "2.0")
            .put("id",      id)
            .put("error",   new JSONObject()
                .put("code",    code)
                .put("message", msg));
    }

    private static JSONObject pong(Object id) {
        return successResponse(id, new JSONObject().put("pong", true));
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    public static void main(String[] args) throws Exception {
        new AdminMcpServer().run();
    }
}
