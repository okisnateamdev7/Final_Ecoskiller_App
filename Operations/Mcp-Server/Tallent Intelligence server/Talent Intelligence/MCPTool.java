package com.ecoskiller.tie.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Optional;
import java.util.Set;

/**
 * Contract every TIE MCP tool must implement.
 */
public interface MCPTool {

    /** Unique tool name (snake_case). */
    String getName();

    /** Human-readable description shown to LLM clients. */
    String getDescription();

    /** Roles permitted to call this tool (Keycloak realm roles). */
    Set<String> getAllowedRoles();

    default boolean isAllowedRole(String role) {
        return getAllowedRoles().contains(role) || getAllowedRoles().contains("*");
    }

    /**
     * JSON Schema for the input arguments (MCP inputSchema format).
     * Build using the provided ObjectMapper so the schema node matches the
     * server's mapper configuration.
     */
    ObjectNode getInputSchema(ObjectMapper mapper);

    /**
     * Full tool descriptor as expected by MCP tools/list.
     */
    default ObjectNode getSchema(ObjectMapper mapper) {
        ObjectNode tool = mapper.createObjectNode();
        tool.put("name", getName());
        tool.put("description", getDescription());
        tool.set("inputSchema", getInputSchema(mapper));
        return tool;
    }

    /**
     * Validate incoming arguments.
     * Return Optional.empty() if valid, or Optional.of(errorMessage) if not.
     */
    Optional<String> validateArgs(JsonNode args);

    /**
     * Execute the tool logic and return the result payload.
     * The server wraps the returned node in MCP content[] automatically.
     */
    JsonNode execute(JsonNode args, ObjectMapper mapper) throws Exception;
}
