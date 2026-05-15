package com.ecoskiller.mcp.jitsi.agents;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Base contract for all Jitsi MCP agents.
 */
public interface AgentHandler {

    /** Human-readable description for tools/list */
    String getDescription();

    /** JSON Schema for this tool's input arguments */
    ObjectNode getInputSchema(ObjectMapper mapper);

    /** Execute the agent logic and return a structured result */
    AgentResult execute(JsonNode arguments) throws Exception;
}
