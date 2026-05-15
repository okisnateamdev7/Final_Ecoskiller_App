package com.ecoskiller.mcp.k3s.agents;

import java.util.List;
import java.util.Map;

/**
 * Contract every k3s MCP agent must implement.
 */
public interface K3sAgent {

    /** Human-readable description of what this agent does. */
    String getDescription();

    /** Parameter name → description map (used in JSON schema generation). */
    Map<String, String> getParameters();

    /** List of required parameter names. */
    List<String> getRequiredParameters();

    /** Execute the agent with the given sanitized arguments. */
    String execute(Map<String, String> args);
}
