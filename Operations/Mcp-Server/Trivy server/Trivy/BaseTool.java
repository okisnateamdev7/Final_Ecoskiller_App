package com.ecoskiller.trivy.mcp.tools;

import com.ecoskiller.trivy.mcp.MCPTool;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.Set;

/**
 * Abstract base for all Trivy MCP tools.
 * Provides schema builder helpers, role-set constants,
 * default validation, and numeric rounding utility.
 */
public abstract class BaseTool implements MCPTool {

    // ── Role sets ─────────────────────────────────────────────────────────────
    /** Every authenticated role. */
    protected static final Set<String> ALL_ROLES =
            Set.of("admin", "security_engineer", "devops_engineer", "developer", "viewer");

    /** Security and DevOps specialists + admins. */
    protected static final Set<String> SEC_OPS =
            Set.of("admin", "security_engineer", "devops_engineer");

    /** Security engineers and admins only. */
    protected static final Set<String> SEC_ONLY =
            Set.of("admin", "security_engineer");

    /** Admins only. */
    protected static final Set<String> ADMIN_ONLY = Set.of("admin");

    // ── Default validation ────────────────────────────────────────────────────
    @Override
    public Optional<String> validateArgs(JsonNode args) {
        if (args == null || args.isNull()) return Optional.of("arguments must not be null");
        return Optional.empty();
    }

    // ── Schema helpers ────────────────────────────────────────────────────────

    protected ObjectNode schema(ObjectMapper m) {
        ObjectNode s = m.createObjectNode(); s.put("type", "object"); return s;
    }
    protected ObjectNode strProp(ObjectMapper m, String desc) {
        ObjectNode p = m.createObjectNode(); p.put("type","string"); p.put("description",desc); return p;
    }
    protected ObjectNode numProp(ObjectMapper m, String desc) {
        ObjectNode p = m.createObjectNode(); p.put("type","number"); p.put("description",desc); return p;
    }
    protected ObjectNode intProp(ObjectMapper m, String desc) {
        ObjectNode p = m.createObjectNode(); p.put("type","integer"); p.put("description",desc); return p;
    }
    protected ObjectNode boolProp(ObjectMapper m, String desc) {
        ObjectNode p = m.createObjectNode(); p.put("type","boolean"); p.put("description",desc); return p;
    }
    protected ObjectNode arrStrProp(ObjectMapper m, String desc) {
        ObjectNode p = m.createObjectNode(); p.put("type","array"); p.put("description",desc);
        p.putObject("items").put("type","string"); return p;
    }
    protected void required(ObjectNode schema, String... fields) {
        ArrayNode r = schema.putArray("required");
        for (String f : fields) r.add(f);
    }

    // ── Numeric helpers ───────────────────────────────────────────────────────
    protected double round2(double v) { return Math.round(v * 100.0) / 100.0; }
    protected double round4(double v) { return Math.round(v * 10000.0) / 10000.0; }
}
