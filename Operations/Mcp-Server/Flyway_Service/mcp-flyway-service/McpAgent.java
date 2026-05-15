package com.ecoskiller.flyway;

import com.google.gson.JsonObject;

/**
 * MCP Agent contract for all Flyway Service agents.
 * Each agent provides a tool definition (JSON Schema) and executes business logic.
 */
public interface McpAgent {

    /** Returns the MCP tool definition: name, description, inputSchema. */
    JsonObject getToolDefinition();

    /** Executes agent logic. Input is pre-validated and sanitized by SecurityManager. */
    JsonObject execute(JsonObject args);
}
