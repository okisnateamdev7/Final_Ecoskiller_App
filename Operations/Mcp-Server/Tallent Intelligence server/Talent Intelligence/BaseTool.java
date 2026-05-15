package com.ecoskiller.tie.mcp.tools;

import com.ecoskiller.tie.mcp.MCPTool;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Optional;
import java.util.Set;

/**
 * Convenience base class for all TIE MCP tools.
 * Provides schema building helpers and default validation.
 */
public abstract class BaseTool implements MCPTool {

    @Override
    public Optional<String> validateArgs(JsonNode args) {
        if (args == null || args.isNull()) return Optional.of("arguments must not be null");
        return Optional.empty(); // subclasses override for stricter checks
    }

    // ── Schema builders ───────────────────────────────────────────────────────

    protected ObjectNode emptySchema(ObjectMapper m) {
        ObjectNode s = m.createObjectNode();
        s.put("type", "object");
        s.set("properties", m.createObjectNode());
        return s;
    }

    protected ObjectNode stringProp(ObjectMapper m, String description) {
        ObjectNode p = m.createObjectNode();
        p.put("type", "string");
        p.put("description", description);
        return p;
    }

    protected ObjectNode numberProp(ObjectMapper m, String description) {
        ObjectNode p = m.createObjectNode();
        p.put("type", "number");
        p.put("description", description);
        return p;
    }

    protected ObjectNode intProp(ObjectMapper m, String description) {
        ObjectNode p = m.createObjectNode();
        p.put("type", "integer");
        p.put("description", description);
        return p;
    }

    protected ObjectNode boolProp(ObjectMapper m, String description) {
        ObjectNode p = m.createObjectNode();
        p.put("type", "boolean");
        p.put("description", description);
        return p;
    }

    protected ObjectNode arrayOfStringProp(ObjectMapper m, String description) {
        ObjectNode p = m.createObjectNode();
        p.put("type", "array");
        p.put("description", description);
        ObjectNode items = p.putObject("items");
        items.put("type", "string");
        return p;
    }

    // ── Common role sets ──────────────────────────────────────────────────────

    /** Everyone with a valid token. */
    protected static final Set<String> ALL_ROLES =
            Set.of("admin", "ml_engineer", "recruiter", "viewer");

    /** Only admins and ML engineers. */
    protected static final Set<String> ML_ROLES =
            Set.of("admin", "ml_engineer");

    /** Admin only. */
    protected static final Set<String> ADMIN_ONLY =
            Set.of("admin");

    // ── Numeric helpers ────────────────────────────────────────────────────

    protected double round(double v) {
        return Math.round(v * 10000.0) / 10000.0;
    }
}
