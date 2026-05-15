package com.ecoskiller.mcp.security.agents;

import com.ecoskiller.mcp.security.model.McpTool;
import com.ecoskiller.mcp.security.model.ToolResult;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Base interface for all MCP-03 Security agents.
 * Every agent must declare its tools and handle tool calls.
 */
public interface McpAgent {

    /** Unique agent name — matches the CAT-03 specification */
    String getName();

    /** Human-readable description */
    String getDescription();

    /** All tools this agent exposes */
    List<McpTool> getTools();

    /** Execute a tool call and return a result */
    ToolResult executeTool(String toolName, JsonNode arguments);
}
