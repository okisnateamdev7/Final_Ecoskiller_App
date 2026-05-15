package com.ecoskiller.mcp19;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Contract every CAT-19 agent must implement.
 */
public interface AgentHandler {

    /** Human-readable description shown in tools/list. */
    String description();

    /** JSON Schema for the tool's input parameters. */
    ObjectNode inputSchema();

    /**
     * Execute the agent logic.
     *
     * @param args  parsed JSON arguments from the MCP call
     * @return      JsonNode result to be serialised back to the caller
     */
    JsonNode execute(JsonNode args) throws Exception;
}
