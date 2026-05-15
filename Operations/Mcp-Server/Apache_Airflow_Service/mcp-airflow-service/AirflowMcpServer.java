package com.ecoskiller.airflow.mcp;

import com.ecoskiller.airflow.mcp.security.JwtValidator;
import com.ecoskiller.airflow.mcp.security.RateLimiter;
import com.ecoskiller.airflow.mcp.security.AuditLogger;
import com.ecoskiller.airflow.mcp.tools.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Ecoskiller | Apache Airflow Service MCP Server (Java 17)
 * CAT-AIRFLOW — Workflow Orchestration & Scheduled Job Management
 *
 * 28 tools covering:
 *   • DAG Management           • Task Instance Control
 *   • Scheduled DAG Runs       • XCom Data Access
 *   • Kafka Event Publishing   • Database Maintenance
 *   • Certificate Expiry       • Data Retention / DPDPA
 *   • Billing Cycle Trigger    • Analytics Reports
 *   • K8s Pod Monitoring       • Observability / Metrics
 *
 * Security:
 *   - JWT validation (Keycloak OIDC)
 *   - Role-Based Access Control (SUPER_ADMIN, OPS_LEAD, COMPLIANCE_OFFICER)
 *   - Rate limiting (100 req/min)
 *   - Immutable audit logging
 *   - Input sanitisation (SQL injection, path traversal, null-byte)
 */
public class AirflowMcpServer {

    private static final Logger LOG = Logger.getLogger(AirflowMcpServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "mcp-airflow-service";
    private static final String SERVER_VER  = "1.0.0";

    private final Map<String, ToolHandler> tools = new HashMap<>();
    private final JwtValidator jwtValidator;
    private final RateLimiter  rateLimiter;
    private final AuditLogger  auditLogger;

    public AirflowMcpServer() {
        this.jwtValidator = new JwtValidator();
        this.rateLimiter  = new RateLimiter(100, 60_000);
        this.auditLogger  = new AuditLogger();
        registerAllTools();
    }

    // ── Tool registration ────────────────────────────────────────────────────

    private void registerAllTools() {

        // 1. DAG Management
        register(new DagListTool());
        register(new DagGetTool());
        register(new DagPauseTool(auditLogger));
        register(new DagUnpauseTool(auditLogger));
        register(new DagDeleteTool(auditLogger));
        register(new DagTriggerTool(auditLogger));

        // 2. DAG Runs
        register(new DagRunListTool());
        register(new DagRunGetTool());
        register(new DagRunClearTool(auditLogger));

        // 3. Task Instances
        register(new TaskInstanceListTool());
        register(new TaskInstanceGetTool());
        register(new TaskInstanceLogTool());
        register(new TaskInstanceClearTool(auditLogger));
        register(new TaskInstanceSetStateTool(auditLogger));

        // 4. XCom
        register(new XComGetTool());
        register(new XComListTool());

        // 5. Scheduled Ecoskiller DAGs
        register(new BillingCycleTriggerTool(auditLogger));
        register(new AnalyticsReportWeeklyTool(auditLogger));
        register(new CertificateExpiryCheckTool(auditLogger));
        register(new DataRetentionCleanupTool(auditLogger));
        register(new DatabaseMaintenanceTool(auditLogger));

        // 6. Kafka Events (Airflow publishes on DAG completion)
        register(new KafkaEventStatusTool());
        register(new KafkaEventPublishTool(auditLogger));

        // 7. Observability / Monitoring
        register(new AirflowHealthTool());
        register(new DagMetricsTool());
        register(new KubernetesPodStatusTool());

        // 8. Connections & Variables
        register(new ConnectionListTool());
        register(new VariableGetTool());
    }

    private void register(ToolHandler h) { tools.put(h.getName(), h); }

    // ── MCP stdio loop ───────────────────────────────────────────────────────

    public void run() throws IOException {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in,  StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

        LOG.info("[AirflowMCPServer] Listening on stdio…");
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
                case "initialize" -> handleInit(id);
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

    private JSONObject handleInit(Object id) {
        return successResp(id, new JSONObject()
            .put("protocolVersion", MCP_VERSION)
            .put("serverInfo", new JSONObject()
                .put("name",    SERVER_NAME)
                .put("version", SERVER_VER)
                .put("description",
                     "Ecoskiller Apache Airflow Service — Workflow Orchestration & Scheduled Job Management"))
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
        JSONObject params = req.optJSONObject("params");
        if (params == null) params = new JSONObject();
        String     toolName = params.optString("name", "");
        JSONObject args     = params.optJSONObject("arguments");
        if (args == null) args = new JSONObject();

        // ── Security gate ──────────────────────────────────────────────────
        String userId = validateAuth(req);
        rateLimiter.check(userId);

        ToolHandler handler = tools.get(toolName);
        if (handler == null) return errorResp(id, -32601, "Unknown tool: " + toolName);

        // RBAC
        handler.checkRole(userId, jwtValidator.getRoles(req));

        // Input sanitisation (path traversal + SQL injection + null-byte)
        sanitizeArgs(args);

        JSONObject result = handler.execute(args, userId);
        auditLogger.logToolCall(userId, toolName, args, result);

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
                    .replace("\u0000", "")                                            // null byte
                    .replaceAll("\\.\\./", "")                                        // path traversal
                    .replaceAll("(?i)<script[^>]*>.*?</script>", "")                  // XSS
                    .replaceAll("(?i)(;\\s*drop\\s+|;\\s*delete\\s+|'\\s*or\\s+'1'='1)", "") // SQLi
                    .trim();
                if (clean.length() > 8_000) clean = clean.substring(0, 8_000);
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

    public static void main(String[] args) throws Exception {
        new AirflowMcpServer().run();
    }
}
