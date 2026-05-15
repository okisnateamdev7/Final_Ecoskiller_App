package com.ecoskiller.dojo;

import com.google.gson.JsonObject;

/**
 * Base interface for all Dojo Match Engine MCP agents.
 * Every agent must provide its tool definition (JSON Schema)
 * and execute business logic given validated+sanitized args.
 */
public interface McpAgent {

    /** Returns the MCP tool definition (name, description, inputSchema). */
    JsonObject getToolDefinition();

    /** Executes the agent logic. Input is already sanitized by SecurityManager. */
    JsonObject execute(JsonObject args);
}
