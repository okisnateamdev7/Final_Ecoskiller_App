package com.ecoskiller.mcp.keycloak.tools;

import org.json.JSONObject;

/**
 * Contract every Keycloak IAM MCP tool must implement.
 */
public interface McpTool {
    /** Unique tool name (snake_case, used in JSON-RPC). */
    String getName();

    /** Human-readable description shown in tools/list. */
    String getDescription();

    /** JSON Schema for this tool's inputSchema. */
    JSONObject getInputSchema();

    /**
     * Execute the tool.
     *
     * @param args  Validated arguments from the MCP client.
     * @return      Result JSON object (serialized to text content).
     * @throws Exception  Any Keycloak / HTTP / validation error.
     */
    JSONObject execute(JSONObject args) throws Exception;
}
