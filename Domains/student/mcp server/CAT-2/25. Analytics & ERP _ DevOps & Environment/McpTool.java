package com.ecoskiller.analytics.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class McpTool {
    private final String name, description;
    private final ObjectNode inputSchema;

    public McpTool(String name, String description, ObjectNode inputSchema) {
        this.name = name; this.description = description; this.inputSchema = inputSchema;
    }
    public String getName() { return name; }
    public ObjectNode toJson(ObjectMapper m) {
        ObjectNode n = m.createObjectNode();
        n.put("name", name); n.put("description", description); n.set("inputSchema", inputSchema);
        return n;
    }
}
