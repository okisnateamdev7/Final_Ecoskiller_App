package com.ecoskiller.mcp.irs.tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Base interface for all Innovation Registry Service MCP tools.
 */
public interface IrsTool {
    String getDescription();
    String getInputSchema();
    ToolResult execute(JsonNode params, ObjectMapper mapper) throws Exception;
}
