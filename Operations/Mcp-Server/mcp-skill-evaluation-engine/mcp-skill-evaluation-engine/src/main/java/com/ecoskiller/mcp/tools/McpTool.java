package com.ecoskiller.mcp.tools;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Base interface for all MCP tool implementations.
 * Every tool must define its JSON schema and execute logic.
 */
public interface McpTool {
    /** Returns the MCP tool schema (name, description, inputSchema). */
    JsonNode getSchema();

    /** Executes the tool with the provided arguments. */
    JsonNode execute(JsonNode arguments) throws Exception;
}
