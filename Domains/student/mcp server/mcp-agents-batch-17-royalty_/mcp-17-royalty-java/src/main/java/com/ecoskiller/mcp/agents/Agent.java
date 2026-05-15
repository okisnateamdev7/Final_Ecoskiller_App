package com.ecoskiller.mcp.agents;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Base interface for all MCP agents.
 * Implements the execute method for tool invocation.
 */
public interface Agent {
    /**
     * Execute a tool call within this agent
     * @param toolName The name of the tool to execute
     * @param arguments The arguments passed to the tool
     * @return JSON result of the tool execution
     */
    JsonNode execute(String toolName, JsonNode arguments) throws Exception;

    /**
     * Get the agent name
     */
    String getAgentName();

    /**
     * Get the agent description
     */
    String getDescription();
}
