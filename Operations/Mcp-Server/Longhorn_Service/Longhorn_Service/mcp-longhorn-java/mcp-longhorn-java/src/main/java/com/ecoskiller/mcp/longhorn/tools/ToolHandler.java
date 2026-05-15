package com.ecoskiller.mcp.longhorn.tools;

import java.util.Map;

/**
 * Contract for every Longhorn MCP tool.
 */
public interface ToolHandler {

    /**
     * Returns the JSON Schema descriptor for this tool as a Map
     * (serialised to JSON by JsonRpc.toJson).
     */
    Map<String, Object> getSchema(String toolName);

    /**
     * Executes the tool with the validated arguments map.
     *
     * @param args Validated input arguments
     * @return A serialisable result object (Map / List / String / Number / Boolean)
     * @throws IllegalArgumentException for user-facing validation errors
     */
    Object execute(Map<String, Object> args);
}
