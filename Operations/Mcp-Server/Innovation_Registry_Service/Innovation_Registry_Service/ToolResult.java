package com.ecoskiller.mcp.irs.tool;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Immutable result returned from a tool execution.
 */
public final class ToolResult {
    private final JsonNode data;
    private final boolean error;

    private ToolResult(JsonNode data, boolean error) {
        this.data = data;
        this.error = error;
    }

    public static ToolResult success(JsonNode data) {
        return new ToolResult(data, false);
    }

    public static ToolResult error(JsonNode data) {
        return new ToolResult(data, true);
    }

    public JsonNode getData() { return data; }
    public boolean isError()  { return error; }
}
