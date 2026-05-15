package com.ecoskiller.mcp.session.tools;

import com.ecoskiller.mcp.session.client.SessionControlClient;
import com.ecoskiller.mcp.session.client.SessionControlClient.ServiceResponse;
import com.ecoskiller.mcp.session.config.SessionControlConfig;
import com.ecoskiller.mcp.session.security.AuditLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

// ─────────────────────────────────────────────────────────────────────────────
// McpTool interface
// ─────────────────────────────────────────────────────────────────────────────
interface McpTool {
    String     name();
    ObjectNode schema();
    ToolResult execute(JsonNode arguments) throws Exception;
}

// ─────────────────────────────────────────────────────────────────────────────
// ToolResult record
// ─────────────────────────────────────────────────────────────────────────────
record ToolResult(String content, boolean isError) {
    static ToolResult ok(String c)    { return new ToolResult(c, false); }
    static ToolResult error(String m) {
        return new ToolResult("{\"error\":\"" + esc(m) + "\"}", true);
    }
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\","\\\\").replace("\"","\\\"")
                .replace("\n","\\n").replace("\r","\\r");
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// BaseTool — shared helpers for all tools
// ─────────────────────────────────────────────────────────────────────────────
abstract class BaseTool implements McpTool {

    protected final SessionControlClient client;
    protected final SessionControlConfig cfg;
    protected final AuditLogger          audit;
    protected final ObjectMapper         json = new ObjectMapper();

    // Valid session states from the FSM (section 3 of spec)
    protected static final java.util.Set<String> VALID_STATES = java.util.Set.of(
        "CREATED", "WAITING_FOR_PARTICIPANTS", "IN_PROGRESS", "SUSPENDED", "COMPLETED", "ARCHIVED"
    );

    // Valid assessment types (section 5)
    protected static final java.util.Set<String> VALID_TYPES = java.util.Set.of(
        "GROUP_DISCUSSION", "INTERVIEW", "CODING_CHALLENGE"
    );

    // Valid participant roles
    protected static final java.util.Set<String> VALID_ROLES = java.util.Set.of(
        "PARTICIPANT", "ASSESSOR", "OBSERVER", "MODERATOR"
    );

    // Valid disconnect reasons
    protected static final java.util.Set<String> VALID_DISCONNECT_REASONS = java.util.Set.of(
        "VOLUNTARY", "NETWORK_ERROR", "TIMEOUT", "EVICTED", "SESSION_ENDED"
    );

    BaseTool(SessionControlClient client, SessionControlConfig cfg, AuditLogger audit) {
        this.client = client;
        this.cfg    = cfg;
        this.audit  = audit;
    }

    // ── Input helpers ─────────────────────────────────────────────────────────

    protected String requireString(JsonNode args, String field) {
        JsonNode n = args.path(field);
        if (n.isMissingNode() || n.isNull() || n.asText("").isEmpty())
            throw new IllegalArgumentException("Required field '" + field + "' is missing or empty");
        return n.asText();
    }

    protected String optString(JsonNode args, String field, String def) {
        JsonNode n = args.path(field);
        return (n.isMissingNode() || n.isNull()) ? def : n.asText(def);
    }

    protected int optInt(JsonNode args, String field, int def) {
        JsonNode n = args.path(field);
        return (n.isMissingNode() || n.isNull()) ? def : n.asInt(def);
    }

    protected boolean optBool(JsonNode args, String field, boolean def) {
        JsonNode n = args.path(field);
        return (n.isMissingNode() || n.isNull()) ? def : n.asBoolean(def);
    }

    /**
     * Validate a session ID — must be a valid UUID format.
     * Prevents path traversal and injection attacks.
     */
    protected String validateSessionId(String id) {
        if (id == null || id.isEmpty()) throw new IllegalArgumentException("session_id must not be empty");
        if (!id.matches("[0-9a-fA-F\\-]{8,64}"))
            throw new IllegalArgumentException("session_id contains invalid characters — expected UUID format");
        return id;
    }

    /**
     * Validate participant ID — must be a valid UUID format.
     */
    protected String validateParticipantId(String id) {
        if (id == null || id.isEmpty()) throw new IllegalArgumentException("participant_id must not be empty");
        if (!id.matches("[0-9a-fA-F\\-]{8,64}"))
            throw new IllegalArgumentException("participant_id contains invalid characters — expected UUID format");
        return id;
    }

    /** Validate session duration against configured maximum. */
    protected int validateDuration(int seconds) {
        if (seconds < 60) throw new IllegalArgumentException("duration must be at least 60 seconds");
        if (seconds > cfg.maxDurationSecs)
            throw new IllegalArgumentException("duration " + seconds + "s exceeds maximum allowed "
                    + cfg.maxDurationSecs + "s (" + (cfg.maxDurationSecs / 3600) + "h)");
        return seconds;
    }

    // ── Service error helpers ─────────────────────────────────────────────────

    protected ToolResult serviceError(int status, JsonNode body, String context) {
        String msg = body.path("message").asText(
                     body.path("error").asText("Service error"));
        return ToolResult.error(context + " — HTTP " + status + ": " + msg);
    }

    protected ToolResult wrapSuccess(ServiceResponse r, String context) throws Exception {
        if (!r.isSuccess()) return serviceError(r.statusCode(), r.body(), context);
        ObjectNode out = json.createObjectNode();
        out.put("status", r.statusCode());
        out.set("data",   r.body());
        return ToolResult.ok(json.writeValueAsString(out));
    }

    // ── JSON Schema builder helpers ───────────────────────────────────────────

    protected ObjectNode buildSchema(String name, String desc, ObjectNode props, String... required) {
        ObjectNode s = json.createObjectNode();
        s.put("name", name);
        s.put("description", desc);
        ObjectNode input = json.createObjectNode();
        input.put("type", "object");
        input.set("properties", props);
        ArrayNode req = json.createArrayNode();
        req.add("api_key");
        for (String r : required) req.add(r);
        input.set("required", req);
        s.set("inputSchema", input);
        return s;
    }

    protected ObjectNode strProp(String desc)  {
        ObjectNode n = json.createObjectNode(); n.put("type","string");  n.put("description",desc); return n;
    }
    protected ObjectNode intProp(String desc)  {
        ObjectNode n = json.createObjectNode(); n.put("type","integer"); n.put("description",desc); return n;
    }
    protected ObjectNode boolProp(String desc) {
        ObjectNode n = json.createObjectNode(); n.put("type","boolean"); n.put("description",desc); return n;
    }
    protected ObjectNode enumProp(String desc, String... vals) {
        ObjectNode n = json.createObjectNode(); n.put("type","string"); n.put("description",desc);
        ArrayNode e = json.createArrayNode(); for (String v : vals) e.add(v); n.set("enum",e); return n;
    }
    protected ObjectNode apiKeyProp() {
        return strProp("MCP server API key (MCP_API_KEY env var). Required for all calls.");
    }
    protected ObjectNode sessionIdProp() {
        return strProp("Session UUID (from session_create response)");
    }
    protected ObjectNode participantIdProp() {
        return strProp("Participant UUID (user ID from auth-service)");
    }
    protected ObjectNode tenantIdProp() {
        return strProp("Tenant ID for multi-tenant isolation. Uses SESSION_DEFAULT_TENANT_ID if omitted.");
    }
    protected ObjectNode idempotencyKeyProp() {
        return strProp("Idempotency key (UUID) — prevents duplicate operations. Required for write operations.");
    }

    /** Resolve tenant ID from args or fall back to config default. */
    protected String resolveTenantId(JsonNode args) {
        String t = optString(args, "tenant_id", cfg.defaultTenantId);
        return t.isEmpty() ? cfg.defaultTenantId : t;
    }
}
