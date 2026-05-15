package com.ecoskiller.trivy.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Optional;
import java.util.Set;

/**
 * Contract every Trivy MCP tool must implement.
 * Transport : stdio JSON-RPC 2.0 / MCP 2024-11-05
 * Security  : JWT (Keycloak RS256), RBAC, rate-limiting, audit logging
 */
public interface MCPTool {

    /** Unique snake_case tool name. */
    String getName();

    /** Human-readable description exposed to LLM clients. */
    String getDescription();

    /** Keycloak realm roles permitted to invoke this tool. */
    Set<String> getAllowedRoles();

    default boolean isAllowedRole(String role) {
        return getAllowedRoles().contains(role) || getAllowedRoles().contains("*");
    }

    /** MCP inputSchema (JSON Schema, type=object). */
    ObjectNode getInputSchema(ObjectMapper mapper);

    /** Full MCP tools/list tool descriptor. */
    default ObjectNode getSchema(ObjectMapper mapper) {
        ObjectNode tool = mapper.createObjectNode();
        tool.put("name", getName());
        tool.put("description", getDescription());
        tool.set("inputSchema", getInputSchema(mapper));
        return tool;
    }

    /**
     * Validate incoming arguments.
     * @return Optional.empty() if valid; Optional.of(message) if invalid.
     */
    Optional<String> validateArgs(JsonNode args);

    /**
     * Execute tool. Return JSON result node — server wraps it into MCP content[].
     */
    JsonNode execute(JsonNode args, ObjectMapper mapper) throws Exception;
}
