package com.ecoskiller.mcp.tools;

import com.fasterxml.jackson.databind.JsonNode;

/** Base interface for all realtime-event-gateway MCP tools. */
public interface McpTool {
    JsonNode getSchema();
    JsonNode execute(JsonNode arguments) throws Exception;
}
