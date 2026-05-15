package com.ecoskiller.ranking;

import com.google.gson.JsonObject;

/**
 * MCP Agent contract for all Candidate Ranking Engine agents.
 */
public interface McpAgent {
    JsonObject getToolDefinition();
    JsonObject execute(JsonObject args);
}
