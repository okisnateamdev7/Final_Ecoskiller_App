package com.ecoskiller.mcp26;

import java.util.List;

/** Represents a single MCP tool / agent definition. */
public class ToolDefinition {

    private final String       name;
    private final String       description;
    private final List<String> requiredFields;

    public ToolDefinition(String name, String description, List<String> requiredFields) {
        this.name           = name;
        this.description    = description;
        this.requiredFields = requiredFields;
    }

    public String getName() { return name; }

    /** Renders the tool as a JSON snippet for tools/list. */
    public String toJson() {
        // Build properties block
        StringBuilder props = new StringBuilder();
        for (int i = 0; i < requiredFields.size(); i++) {
            if (i > 0) props.append(",");
            props.append("\"").append(requiredFields.get(i)).append("\":{\"type\":\"string\"}");
        }

        // Build required array
        StringBuilder req = new StringBuilder("[");
        for (int i = 0; i < requiredFields.size(); i++) {
            if (i > 0) req.append(",");
            req.append("\"").append(requiredFields.get(i)).append("\"");
        }
        req.append("]");

        return "{\"name\":\"" + name + "\","
             + "\"description\":\"" + JsonUtil.escape(description) + "\","
             + "\"inputSchema\":{\"type\":\"object\","
             + "\"properties\":{" + props + "},"
             + "\"required\":" + req + "}}";
    }
}
