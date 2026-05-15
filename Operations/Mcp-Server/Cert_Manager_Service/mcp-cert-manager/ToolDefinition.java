package com.ecoskiller.mcp.cert.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;

/** MCP tool definition returned in tools/list. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ToolDefinition {
    public String     name;
    public String     description;
    public ObjectNode inputSchema;

    public ToolDefinition() {}
    public ToolDefinition(String name, String description, ObjectNode inputSchema) {
        this.name = name; this.description = description; this.inputSchema = inputSchema;
    }
}
