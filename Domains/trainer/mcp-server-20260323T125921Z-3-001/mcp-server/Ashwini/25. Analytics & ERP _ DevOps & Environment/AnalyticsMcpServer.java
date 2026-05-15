package com.ecoskiller.analytics.server;

import com.ecoskiller.analytics.agents.*;
import com.ecoskiller.analytics.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

import java.io.*;
import java.util.logging.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════╗
 * ║  Ecoskiller Analytics & DevOps MCP Server                               ║
 * ║  CAT-25 — Analytics & ERP / DevOps & Environment                        ║
 * ║  MCP Server ID : mcp-25-analytics-devops                                ║
 * ║  Namespace     : analytics                                               ║
 * ║  Protocol      : MCP 2024-11-05 / JSON-RPC 2.0 / stdio                  ║
 * ║  Agents        : 14                                                      ║
 * ╚══════════════════════════════════════════════════════════════════════════╝
 *
 * ── Group A: Analytics & ERP ──────────────────────────────────────────────
 *   1.  CLICKHOUSE_METRIC_NORMALIZATION_AGENT
 *   2.  ERP_GO_REPORT_INTEGRATION_AGENT
 *   3.  PHONE_FEATURE_VECTOR_EMISSION_AGENT
 *   4.  ATTENDANCE_BEHAVIOR_ANALYTICS_AGENT
 *   5.  ENROLLMENT_ANALYTICS_AGENT
 *
 * ── Group B: DevOps & Environment ────────────────────────────────────────
 *   6.  MULTI_ENVIRONMENT_PHONE_CONFIG_VALIDATOR_AGENT
 *   7.  PHONE_BACKUP_RESTORE_VALIDATION_AGENT
 *   8.  PHONE_END_TO_END_TRACE_AGENT
 *   9.  PHONE_EXTERNAL_WEBHOOK_AGENT
 *  10.  PHONE_INFRA_HEALTH_CHECK_AGENT
 *  11.  PHONE_MONITORING_CLOCK_AUTHORITY_AGENT
 *  12.  CROSS_NODE_TIME_DRIFT_MONITOR_AGENT
 *  13.  MODEL_GOVERNANCE_REGISTRY_AGENT
 *  14.  CROSS_SERVICE_TRACE_CORRELATION_AGENT
 */
public class AnalyticsMcpServer {

    public  static final String SERVER_NAME    = "mcp-25-analytics-devops";
    public  static final String SERVER_VERSION = "1.0.0";
    private static final String PROTOCOL_VER   = "2024-11-05";
    private static final String NAMESPACE       = "analytics";

    private static final Logger LOGGER = Logger.getLogger(AnalyticsMcpServer.class.getName());

    private final ObjectMapper  mapper   = new ObjectMapper();
    final         AgentRegistry registry = new AgentRegistry();

    // ── Constructor: register all 14 agents ───────────────────────────────

    public AnalyticsMcpServer() {
        // Group A — Analytics & ERP
        registry.register(new ClickhouseMetricNormalizationAgent()); //  1
        registry.register(new ErpGoReportIntegrationAgent());        //  2
        registry.register(new PhoneFeatureVectorEmissionAgent());    //  3
        registry.register(new AttendanceBehaviorAnalyticsAgent());   //  4
        registry.register(new EnrollmentAnalyticsAgent());           //  5

        // Group B — DevOps & Environment
        registry.register(new MultiEnvironmentPhoneConfigValidatorAgent()); //  6
        registry.register(new PhoneBackupRestoreValidationAgent());  //  7
        registry.register(new PhoneEndToEndTraceAgent());            //  8
        registry.register(new PhoneExternalWebhookAgent());          //  9
        registry.register(new PhoneInfraHealthCheckAgent());         // 10
        registry.register(new PhoneMonitoringClockAuthorityAgent()); // 11
        registry.register(new CrossNodeTimeDriftMonitorAgent());     // 12
        registry.register(new ModelGovernanceRegistryAgent());       // 13
        registry.register(new CrossServiceTraceCorrelationAgent());  // 14

        LOGGER.info("[" + SERVER_NAME + "] Registered " + registry.count() + "/14 agents.");
    }

    // ── MCP stdio transport loop ──────────────────────────────────────────

    public void run() throws Exception {
        LOGGER.info("[" + SERVER_NAME + "] v" + SERVER_VERSION + " starting on stdio transport...");
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String response = handleMessage(line);
            if (response != null) out.println(response);
        }
    }

    // ── Message dispatch ─────────────────────────────────────────────────

    String handleMessage(String rawJson) {
        try {
            ObjectNode req = (ObjectNode) mapper.readTree(rawJson);
            String method  = req.path("method").asText("");
            Object id      = req.has("id") ? req.get("id") : null;

            return switch (method) {
                case "initialize"     -> handleInitialize(id);
                case "tools/list"     -> handleToolsList(id);
                case "tools/call"     -> handleToolCall(req, id);
                case "resources/list" -> buildResult(id, mapper.createObjectNode().set("resources", mapper.createArrayNode()));
                case "prompts/list"   -> buildResult(id, mapper.createObjectNode().set("prompts",   mapper.createArrayNode()));
                case "ping"           -> buildResult(id, mapper.createObjectNode());
                default               -> buildError(id, -32601, "Method not found: " + method);
            };
        } catch (Exception e) {
            return buildError(null, -32700, "Parse error: " + e.getMessage());
        }
    }

    // ── Handlers ─────────────────────────────────────────────────────────

    private String handleInitialize(Object id) {
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", PROTOCOL_VER);

        ObjectNode info = mapper.createObjectNode();
        info.put("name",      SERVER_NAME);
        info.put("version",   SERVER_VERSION);
        info.put("namespace", NAMESPACE);
        info.put("agents",    registry.count());
        info.put("category",  "CAT-25 — Analytics & ERP / DevOps & Environment");
        result.set("serverInfo", info);

        ObjectNode caps = mapper.createObjectNode();
        caps.set("tools",     mapper.createObjectNode().put("listChanged", false));
        caps.set("resources", mapper.createObjectNode().put("subscribe",   false));
        caps.set("prompts",   mapper.createObjectNode().put("listChanged", false));
        result.set("capabilities", caps);

        return buildResult(id, result);
    }

    private String handleToolsList(Object id) {
        ArrayNode tools = mapper.createArrayNode();
        for (BaseAgent agent : registry.all())
            for (McpTool tool : agent.getTools())
                tools.add(tool.toJson(mapper));

        ObjectNode result = mapper.createObjectNode();
        result.set("tools", tools);
        result.put("count", tools.size());
        return buildResult(id, result);
    }

    private String handleToolCall(ObjectNode req, Object id) {
        try {
            ObjectNode params   = (ObjectNode) req.path("params");
            String     toolName = params.path("name").asText("");
            ObjectNode args     = params.has("arguments")
                ? (ObjectNode) params.get("arguments")
                : mapper.createObjectNode();

            for (BaseAgent agent : registry.all())
                for (McpTool tool : agent.getTools())
                    if (tool.getName().equals(toolName))
                        return buildToolResult(id, agent.execute(toolName, args));

            return buildError(id, -32602, "Tool not found: " + toolName);
        } catch (Exception e) {
            return buildError(id, -32603, "Tool error: " + e.getMessage());
        }
    }

    // ── JSON-RPC builders ─────────────────────────────────────────────────

    private String buildResult(Object id, ObjectNode result) {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0"); setId(resp, id); resp.set("result", result);
        return resp.toString();
    }

    private String buildToolResult(Object id, AgentResponse ar) {
        ObjectNode resp   = mapper.createObjectNode();
        resp.put("jsonrpc","2.0"); setId(resp, id);
        ObjectNode result  = mapper.createObjectNode();
        ArrayNode  content = mapper.createArrayNode();
        ObjectNode text    = mapper.createObjectNode();
        text.put("type","text"); text.put("text", ar.toJson(mapper)); content.add(text);
        result.set("content", content); result.put("isError", ar.isError());
        resp.set("result", result);
        return resp.toString();
    }

    private String buildError(Object id, int code, String message) {
        ObjectNode resp  = mapper.createObjectNode();
        resp.put("jsonrpc","2.0"); setId(resp, id);
        ObjectNode error = mapper.createObjectNode();
        error.put("code", code); error.put("message", message);
        resp.set("error", error);
        return resp.toString();
    }

    private void setId(ObjectNode resp, Object id) {
        if      (id instanceof Integer i) resp.put("id", i);
        else if (id instanceof String  s) resp.put("id", s);
        else                              resp.putNull("id");
    }

    // ── Entry point ──────────────────────────────────────────────────────

    public static void main(String[] args) throws Exception {
        LogManager.getLogManager().reset();
        Logger root = Logger.getLogger("");
        Handler h   = new StreamHandler(System.err, new SimpleFormatter());
        h.setLevel(Level.ALL); root.addHandler(h); root.setLevel(Level.INFO);
        new AnalyticsMcpServer().run();
    }
}
