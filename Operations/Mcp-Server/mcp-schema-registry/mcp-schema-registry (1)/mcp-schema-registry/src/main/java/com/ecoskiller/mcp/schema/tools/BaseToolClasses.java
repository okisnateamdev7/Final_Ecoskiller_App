package com.ecoskiller.mcp.schema.tools;

import com.ecoskiller.mcp.schema.client.ApicurioClient;
import com.ecoskiller.mcp.schema.config.SchemaRegistryConfig;
import com.ecoskiller.mcp.schema.security.AuditLogger;
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
        return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n").replace("\r","\\r");
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// BaseTool — shared helpers
// ─────────────────────────────────────────────────────────────────────────────
abstract class BaseTool implements McpTool {

    protected final ApicurioClient       client;
    protected final SchemaRegistryConfig cfg;
    protected final AuditLogger          audit;
    protected final ObjectMapper         json = new ObjectMapper();

    /** Valid Apicurio artifact types. */
    protected static final java.util.Set<String> VALID_TYPES = java.util.Set.of(
        "AVRO", "JSON", "PROTOBUF", "OPENAPI", "ASYNCAPI", "GRAPHQL", "KCONNECT", "WSDL", "XSD"
    );

    /** Valid compatibility modes. */
    protected static final java.util.Set<String> VALID_COMPAT = java.util.Set.of(
        "BACKWARD", "BACKWARD_TRANSITIVE", "FORWARD", "FORWARD_TRANSITIVE",
        "FULL", "FULL_TRANSITIVE", "NONE"
    );

    /** Valid artifact states. */
    protected static final java.util.Set<String> VALID_STATES = java.util.Set.of(
        "ENABLED", "DISABLED", "DEPRECATED"
    );

    BaseTool(ApicurioClient client, SchemaRegistryConfig cfg, AuditLogger audit) {
        this.client = client;
        this.cfg    = cfg;
        this.audit  = audit;
    }

    // ── Input helpers ─────────────────────────────────────────────────────────

    protected String requireString(JsonNode args, String field) {
        JsonNode n = args.path(field);
        if (n.isMissingNode() || n.isNull() || n.asText("").isEmpty())
            throw new IllegalArgumentException("Required field '" + field + "' is missing");
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

    /** Resolve group: use provided value, fall back to config default. */
    protected String resolveGroup(JsonNode args) {
        return optString(args, "group", cfg.defaultGroup);
    }

    /** Sanitise artifact ID — reject path traversal and special chars. */
    protected String sanitiseArtifactId(String id) {
        if (id == null || id.isEmpty()) throw new IllegalArgumentException("artifact_id must not be empty");
        if (id.contains("..") || id.contains("/") || id.contains("\\"))
            throw new IllegalArgumentException("artifact_id contains illegal path characters");
        if (id.length() > 512)
            throw new IllegalArgumentException("artifact_id too long (max 512)");
        return id;
    }

    /** Validate compatibility mode string. */
    protected String validateCompat(String mode) {
        if (!VALID_COMPAT.contains(mode.toUpperCase()))
            throw new IllegalArgumentException("Invalid compatibility mode: " + mode
                + ". Must be one of: " + VALID_COMPAT);
        return mode.toUpperCase();
    }

    /** Validate artifact type. */
    protected String validateType(String type) {
        if (!VALID_TYPES.contains(type.toUpperCase()))
            throw new IllegalArgumentException("Invalid artifact type: " + type
                + ". Must be one of: " + VALID_TYPES);
        return type.toUpperCase();
    }

    // ── Apicurio error helper ─────────────────────────────────────────────────

    /** Build a clean error message from an Apicurio error response. */
    protected ToolResult apicurioError(int status, JsonNode body, String context) {
        String msg = body.path("message").asText(body.path("title").asText("Apicurio error"));
        return ToolResult.error(context + " — HTTP " + status + ": " + msg);
    }

    /** Wrap a successful Apicurio response into a ToolResult. */
    protected ToolResult wrapSuccess(com.ecoskiller.mcp.schema.client.ApicurioClient.ApicurioResponse r,
                                      String context) throws Exception {
        if (!r.isSuccess()) return apicurioError(r.statusCode(), r.body(), context);
        ObjectNode out = json.createObjectNode();
        out.put("status",  r.statusCode());
        out.set("data",    r.body());
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

    protected ObjectNode strProp(String desc) {
        ObjectNode n = json.createObjectNode(); n.put("type","string"); n.put("description",desc); return n;
    }
    protected ObjectNode intProp(String desc) {
        ObjectNode n = json.createObjectNode(); n.put("type","integer"); n.put("description",desc); return n;
    }
    protected ObjectNode enumProp(String desc, String... vals) {
        ObjectNode n = json.createObjectNode(); n.put("type","string"); n.put("description",desc);
        ArrayNode e = json.createArrayNode(); for (String v : vals) e.add(v); n.set("enum",e); return n;
    }
    protected ObjectNode apiKeyProp() {
        return strProp("MCP server API key (MCP_API_KEY env var). Required for all calls.");
    }
    protected ObjectNode groupProp() {
        return strProp("Artifact group (tenant/namespace). Defaults to '" + cfg.defaultGroup + "' if omitted.");
    }
    protected ObjectNode artifactIdProp() {
        return strProp("Artifact ID — typically the Kafka topic name (e.g. candidate-scored, assessment-completed)");
    }
    protected ObjectNode versionProp() {
        return strProp("Schema version number or 'latest' (default: latest)");
    }
}
