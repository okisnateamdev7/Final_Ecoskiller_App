package com.ecoskiller.mcp.royalty;

import com.ecoskiller.mcp.royalty.security.AuditLogger;
import com.ecoskiller.mcp.royalty.security.InputValidator;
import com.ecoskiller.mcp.royalty.tools.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ecoskiller MCP-17 — Royalty Engine MCP Server
 *
 * CAT-17: Marketplace, Royalty & Compliance
 * 18 agents | Java 17 | MCP 2024-11-05 | JSON-RPC 2.0 over stdio
 *
 * Agents:
 *  1  tax_compliance            TAX_COMPLIANCE_AGENT
 *  2  school_auto_creation      SCHOOL_AUTO_CREATION_AGENT
 *  3  royalty_wallet            ROYALTY_WALLET_AGENT
 *  4  royalty_system_unified    ROYALTY_SYSTEM_UNIFIED_AGENT
 *  5  royalty_escrow            ROYALTY_ESCROW_AGENT
 *  6  royalty_distribution      ROYALTY_DISTRIBUTION_AGENT
 *  7  royalty_calculation       ROYALTY_CALCULATION_AGENT
 *  8  revenue_ingestion         REVENUE_INGESTION_AGENT
 *  9  parent_dashboard          PARENT_DASHBOARD_AGENT
 * 10  market_demand_prediction  MARKET_DEMAND_PREDICTION_AGENT
 * 11  license_generation        LICENSE_GENERATION_AGENT
 * 12  idea_visibility_control   IDEA_VISIBILITY_CONTROL_AGENT
 * 13  idea_evaluation           IDEA_EVALUATION_AGENT
 * 14  data_retention_policy     DATA_RETENTION_POLICY_AGENT
 * 15  contract_lifecycle        CONTRACT_LIFECYCLE_AGENT
 * 16  competition_engine        COMPETITION_ENGINE_AGENT
 * 17  business_matching         BUSINESS_MATCHING_AGENT
 * 18  audit_verification        AUDIT_VERIFICATION_AGENT
 */
public class RoyaltyEngineMcpServer {

    private static final Logger LOGGER = Logger.getLogger(RoyaltyEngineMcpServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "mcp-17-royalty";
    private static final String SERVER_VER  = "1.0.0";

    private final Map<String, McpTool> tools = new HashMap<>();
    private final AuditLogger          audit;
    private final InputValidator       validator;

    public RoyaltyEngineMcpServer() {
        this.audit     = new AuditLogger();
        this.validator = new InputValidator();
        registerAll();
    }

    private void registerAll() {
        register(new TaxComplianceTool());
        register(new SchoolAutoCreationTool());
        register(new RoyaltyWalletTool());
        register(new RoyaltySystemUnifiedTool());
        register(new RoyaltyEscrowTool());
        register(new RoyaltyDistributionTool());
        register(new RoyaltyCalculationTool());
        register(new RevenueIngestionTool());
        register(new ParentDashboardTool());
        register(new MarketDemandPredictionTool());
        register(new LicenseGenerationTool());
        register(new IdeaVisibilityControlTool());
        register(new IdeaEvaluationTool());
        register(new DataRetentionPolicyTool());
        register(new ContractLifecycleTool());
        register(new CompetitionEngineTool());
        register(new BusinessMatchingTool());
        register(new AuditVerificationTool());
    }

    private void register(McpTool t) { tools.put(t.getName(), t); }

    // ─── Main Loop ───────────────────────────────────────────────────────────────

    public void run() throws IOException {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in,  StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
        LOGGER.info("[MCP-17] Royalty Engine server starting — " + SERVER_VER);
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                JSONObject req  = new JSONObject(line);
                JSONObject resp = handle(req);
                if (resp != null) out.println(resp);
            } catch (Exception e) {
                out.println(err(null, -32700, "Parse error: " + e.getMessage()));
            }
        }
    }

    JSONObject handle(JSONObject req) {
        Object id     = req.opt("id");
        String method = req.optString("method", "");
        audit.logRequest(method);
        return switch (method) {
            case "initialize"  -> handleInit(id, req);
            case "ping"        -> ok(id, new JSONObject());
            case "tools/list"  -> handleList(id);
            case "tools/call"  -> handleCall(id, req);
            default -> {
                if (method.startsWith("notifications/")) yield null;
                yield err(id, -32601, "Method not found: " + method);
            }
        };
    }

    private JSONObject handleInit(Object id, JSONObject req) {
        JSONObject result = new JSONObject()
                .put("protocolVersion", MCP_VERSION)
                .put("serverInfo", new JSONObject().put("name", SERVER_NAME).put("version", SERVER_VER))
                .put("capabilities", new JSONObject().put("tools", new JSONObject().put("listChanged", false)));
        LOGGER.info("[MCP-17] Client initialized: " + req.optJSONObject("clientInfo"));
        return ok(id, result);
    }

    private JSONObject handleList(Object id) {
        JSONArray arr = new JSONArray();
        tools.values().forEach(t -> arr.put(t.getSchema()));
        return ok(id, new JSONObject().put("tools", arr));
    }

    private JSONObject handleCall(Object id, JSONObject req) {
        JSONObject params = req.optJSONObject("params");
        if (params == null) return err(id, -32602, "Missing params");
        String     name = params.optString("name");
        JSONObject args = params.optJSONObject("arguments");
        if (args == null) args = new JSONObject();

        McpTool tool = tools.get(name);
        if (tool == null) return err(id, -32602, "Unknown tool: " + name);

        try {
            validator.validate(name, args);
        } catch (SecurityException se) {
            audit.logSecurityViolation(name, se.getMessage());
            return err(id, -32602, "Validation failed: " + se.getMessage());
        }

        try {
            JSONObject result = tool.execute(args);
            audit.logSuccess(name, args.optString("creator_id", args.optString("idea_id", "-")));
            return ok(id, result);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "[MCP-17] Tool error: " + name, e);
            audit.logError(name, e.getMessage());
            JSONArray content = new JSONArray().put(new JSONObject().put("type","text").put("text","Error: "+e.getMessage()));
            return ok(id, new JSONObject().put("content", content).put("isError", true));
        }
    }

    static JSONObject ok(Object id, JSONObject result) {
        JSONObject r = new JSONObject().put("jsonrpc","2.0").put("result", result);
        if (id != null) r.put("id", id);
        return r;
    }
    static JSONObject err(Object id, int code, String msg) {
        JSONObject r = new JSONObject().put("jsonrpc","2.0")
                .put("error", new JSONObject().put("code", code).put("message", msg));
        if (id != null) r.put("id", id);
        return r;
    }

    public static void main(String[] args) throws IOException {
        new RoyaltyEngineMcpServer().run();
    }
}
