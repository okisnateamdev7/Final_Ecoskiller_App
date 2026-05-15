package com.ecoskiller.mcp.aigovernance.agents;

import com.ecoskiller.mcp.aigovernance.model.McpTool;
import com.ecoskiller.mcp.aigovernance.model.ToolResult;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Base interface for all MCP-04 AI Governance agents.
 */
public interface McpAgent {
    String getName();
    String getDescription();
    List<McpTool> getTools();
    ToolResult executeTool(String toolName, JsonNode arguments);
}
