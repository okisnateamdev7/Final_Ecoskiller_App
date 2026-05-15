package com.ecoskiller.mcp.agents;

import java.util.Map;

/**
 * Base contract for all CAT-23 agents.
 */
public interface Agent {
    /** The MCP tool name (snake_case). */
    String toolName();

    /** One-line description shown in tools/list. */
    String description();

    /** JSON Schema object for the tool's input parameters. */
    Map<String, Object> inputSchema();

    /** Execute the agent logic with the given arguments and return a text result. */
    String execute(Map<String, Object> arguments);
}
