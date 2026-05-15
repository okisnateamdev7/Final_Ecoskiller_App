package com.ecoskiller.mcp.redis.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Contract every MCP tool must implement.
 */
public interface McpTool {

    /** Unique tool name as registered with the MCP client. */
    String name();

    /**
     * JSON Schema for this tool — returned in tools/list.
     * Must include: name, description, inputSchema (JSON Schema object).
     */
    ObjectNode schema();

    /**
     * Execute the tool with the supplied arguments.
     * Arguments always include a validated api_key field (already checked by dispatcher).
     */
    ToolResult execute(JsonNode arguments) throws Exception;
}
