package com.ecoskiller.mcp.server;

import com.ecoskiller.mcp.security.SecurityContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Contract for every MCP tool in the Candidate Ranking Engine.
 * Each tool declares:
 *   - its name and JSON schema (for tools/list)
 *   - required RBAC role
 *   - execution logic (with tenant isolation enforced)
 */
public interface McpTool {

    /** Unique tool name, used in tools/call dispatch. */
    String getName();

    /** Minimum Keycloak RBAC role required to invoke this tool. */
    String requiredRole();

    /**
     * JSON Schema descriptor returned in tools/list.
     * Must include: name, description, inputSchema.
     */
    ObjectNode getSchema();

    /**
     * Check whether the authenticated principal has sufficient role.
     * Default: RBAC hierarchy — admin > recruiter > system > readonly
     */
    default boolean isAuthorized(SecurityContext.ValidationResult auth) {
        return RoleHierarchy.covers(auth.role(), requiredRole());
    }

    /**
     * Execute the tool with validated arguments and auth context.
     * Implementations must:
     *   1. Extract tenant_id from auth (never from args alone)
     *   2. Scope all data access to that tenant_id
     *   3. Return a JsonNode result (will be JSON-serialised into MCP content)
     *
     * @param args  tool arguments (from tools/call "arguments")
     * @param auth  validated security context — provides tenant_id and role
     * @return      result payload as JsonNode
     * @throws IllegalArgumentException for bad/missing arguments
     * @throws SecurityException        for tenant boundary violations
     */
    JsonNode execute(JsonNode args, SecurityContext.ValidationResult auth) throws Exception;

    // ─── Role hierarchy helper ─────────────────────────────────────────────

    final class RoleHierarchy {
        private static final java.util.Map<String, Integer> LEVELS = java.util.Map.of(
            "admin",    40,
            "recruiter",30,
            "system",   20,
            "readonly", 10
        );

        /** Returns true if 'actual' role has at least the level of 'required'. */
        public static boolean covers(String actual, String required) {
            int a = LEVELS.getOrDefault(actual.toLowerCase(), 0);
            int r = LEVELS.getOrDefault(required.toLowerCase(), 0);
            return a >= r;
        }

        private RoleHierarchy() {}
    }
}
