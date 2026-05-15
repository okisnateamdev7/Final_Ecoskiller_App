package io.ecoskiller.prosody.agents;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Contract for all Prosody MCP agents.
 */
public interface AgentHandler {

    /** Returns the MCP tool definition (name, description, inputSchema). */
    JsonNode getToolDefinition();

    /** Executes the agent with the given arguments and returns a result node. */
    JsonNode execute(JsonNode args) throws Exception;
}
