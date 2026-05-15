package com.ecoskiller.mcp.royalty;

import org.json.JSONObject;

public interface McpTool {
    String getName();
    JSONObject getSchema();
    JSONObject execute(JSONObject args) throws Exception;
}
