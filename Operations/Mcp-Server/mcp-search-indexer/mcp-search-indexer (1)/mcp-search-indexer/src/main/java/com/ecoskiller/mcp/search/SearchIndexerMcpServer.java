package com.ecoskiller.mcp.search;

import com.ecoskiller.mcp.search.security.AuditLogger;
import com.ecoskiller.mcp.search.security.InputValidator;
import com.ecoskiller.mcp.search.tools.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ecoskiller Search Indexer MCP Server
 *
 * CAT: search-indexer — Core Search Infrastructure
 * 18 Tools | Java 17 | MCP 2024-11-05 | JSON-RPC 2.0 over stdio
 *
 * Tools:
 *  1  candidate_search          CANDIDATE_SEARCH_AGENT
 *  2  job_search                JOB_SEARCH_AGENT
 *  3  index_document            INDEX_DOCUMENT_AGENT
 *  4  bulk_index                BULK_INDEX_AGENT
 *  5  delete_document           DELETE_DOCUMENT_AGENT
 *  6  reindex                   REINDEX_AGENT
 *  7  index_health              INDEX_HEALTH_AGENT
 *  8  event_consumer            EVENT_CONSUMER_AGENT
 *  9  document_transform        DOCUMENT_TRANSFORM_AGENT
 * 10  faceted_search            FACETED_SEARCH_AGENT
 * 11  tenant_index_manager      TENANT_INDEX_MANAGER_AGENT
 * 12  index_mapping             INDEX_MAPPING_AGENT
 * 13  search_analytics          SEARCH_ANALYTICS_AGENT
 * 14  query_audit_log           QUERY_AUDIT_LOG_AGENT
 * 15  ilm_policy                ILM_POLICY_AGENT
 * 16  cluster_manager           CLUSTER_MANAGER_AGENT
 * 17  gdpr_compliance           GDPR_COMPLIANCE_AGENT
 * 18  disaster_recovery         DISASTER_RECOVERY_AGENT
 */
public class SearchIndexerMcpServer {

    private static final Logger LOGGER = Logger.getLogger(SearchIndexerMcpServer.class.getName());
    static final String MCP_VERSION = "2024-11-05";
    static final String SERVER_NAME = "mcp-search-indexer";
    static final String SERVER_VER  = "1.0.0";

    private final Map<String, McpTool> tools = new HashMap<>();
    final AuditLogger   audit;
    final InputValidator validator;

    public SearchIndexerMcpServer() {
        this.audit     = new AuditLogger();
        this.validator = new InputValidator();
        registerAll();
    }

    private void registerAll() {
        register(new CandidateSearchTool());
        register(new JobSearchTool());
        register(new IndexDocumentTool());
        register(new BulkIndexTool());
        register(new DeleteDocumentTool());
        register(new ReindexTool());
        register(new IndexHealthTool());
        register(new EventConsumerTool());
        register(new DocumentTransformTool());
        register(new FacetedSearchTool());
        register(new TenantIndexManagerTool());
        register(new IndexMappingTool());
        register(new SearchAnalyticsTool());
        register(new QueryAuditLogTool());
        register(new IlmPolicyTool());
        register(new ClusterManagerTool());
        register(new GdprComplianceTool());
        register(new DisasterRecoveryTool());
    }

    private void register(McpTool t) { tools.put(t.getName(), t); }

    // ─── Main Loop ───────────────────────────────────────────────────────────────

    public void run() throws IOException {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in,  StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
        LOGGER.info("[MCP] Search Indexer server starting — " + SERVER_VER);

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
        LOGGER.info("[MCP] Client initialized: " + req.optJSONObject("clientInfo"));
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
            audit.logSuccess(name, args.optString("tenant_id", args.optString("index_name", "-")));
            return ok(id, result);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "[MCP] Tool error: " + name, e);
            audit.logError(name, e.getMessage());
            JSONArray content = new JSONArray().put(
                    new JSONObject().put("type","text").put("text","Error: " + e.getMessage()));
            return ok(id, new JSONObject().put("content", content).put("isError", true));
        }
    }

    // ─── Response builders ────────────────────────────────────────────────────────

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
        new SearchIndexerMcpServer().run();
    }
}
