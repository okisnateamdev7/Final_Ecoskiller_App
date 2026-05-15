package com.ecoskiller.mcp15;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Contract every CAT-15 agent must implement.
 */
public interface AgentHandler {

    /**
     * Returns the MCP tool definition (name, description, inputSchema).
     */
    ObjectNode toolDefinition(String toolName);

    /**
     * Executes the agent logic and returns a result ObjectNode.
     */
    ObjectNode execute(JsonNode args) throws Exception;
}
