package com.ecoskiller.mcp.search.tools;

import com.ecoskiller.mcp.search.McpTool;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Base for all 18 Search Indexer MCP tools.
 * Provides schema builders, response helpers, and index name conventions.
 */
public abstract class BaseTool implements McpTool {

    // ─── Schema helpers ──────────────────────────────────────────────────────────

    protected JSONObject schema(String name, String desc, JSONObject props, String... required) {
        JSONObject s = new JSONObject().put("type","object").put("properties", props);
        if (required.length > 0) {
            JSONArray r = new JSONArray();
            for (String req : required) r.put(req);
            s.put("required", r);
        }
        return new JSONObject().put("name", name).put("description", desc).put("inputSchema", s);
    }

    protected JSONObject str(String d)  { return new JSONObject().put("type","string").put("description",d); }
    protected JSONObject intP(String d) { return new JSONObject().put("type","integer").put("description",d); }
    protected JSONObject bool(String d) { return new JSONObject().put("type","boolean").put("description",d); }
    protected JSONObject num(String d)  { return new JSONObject().put("type","number").put("description",d); }

    // ─── Response helpers ─────────────────────────────────────────────────────────

    protected JSONObject text(String msg) {
        return new JSONObject().put("content",
                new JSONArray().put(new JSONObject().put("type","text").put("text", msg)));
    }

    protected JSONObject json(JSONObject data) { return text(data.toString(2)); }

    // ─── Index name convention ────────────────────────────────────────────────────

    /** Build tenant-scoped index name: tenant-{tenantId}-{docType} */
    protected String indexName(String tenantId, String docType) {
        return "tenant-" + tenantId + "-" + docType;
    }

    /** ISO-8601 timestamp */
    protected String now() { return java.time.Instant.now().toString(); }

    /** Generate a deterministic document ID */
    protected String docId(String tenantId, String docType, String entityId) {
        return tenantId + "_" + docType + "_" + entityId;
    }
}
