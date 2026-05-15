package com.ecoskiller.mcp.integrations.agents;

import com.ecoskiller.mcp.integrations.model.McpProtocol.InputSchema;
import com.ecoskiller.mcp.integrations.model.McpProtocol.ToolCallResult;

import java.util.Map;

/**
 * Base contract for every CAT-07 agent tool.
 *
 * Each of the 18 agents in mcp-07-integrations implements this interface.
 * The MCP server's tool registry discovers them automatically via Spring's
 * component scan and exposes them over the MCP protocol.
 */
public interface AgentTool {

    /** Unique tool name — must match the agent file name convention e.g. sso_integrate */
    String getName();

    /** Human-readable description shown to the LLM client */
    String getDescription();

    /** JSON Schema for the tool's input arguments */
    InputSchema getInputSchema();

    /**
     * Execute the tool with the given arguments.
     *
     * @param arguments key-value map parsed from the client's tool_call
     * @return MCP-compliant ToolCallResult (may contain text or error)
     */
    ToolCallResult execute(Map<String, Object> arguments);
}
