package com.ecoskiller.mcp.jicofo.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

// ─────────────────────────────────────────────────────────────────────────────
// McpTool — base interface every tool implements
// ─────────────────────────────────────────────────────────────────────────────
public interface McpTool {
    /** The unique snake_case tool name registered in MCP tools/list */
    String getName();
    /** Human-readable description surfaced to the LLM */
    String getDescription();
    /** Execute the tool with validated JSON arguments */
    ToolResult execute(JsonNode args) throws Exception;
    /** Return the full MCP tool definition (name, description, inputSchema) */
    default ObjectNode getToolDefinition(ObjectMapper mapper) {
        ObjectNode def    = mapper.createObjectNode();
        def.put("name",        getName());
        def.put("description", getDescription());
        def.set("inputSchema", buildInputSchema(mapper));
        return def;
    }
    /** Build JSON Schema for the tool's input parameters */
    ObjectNode buildInputSchema(ObjectMapper mapper);
}

// ─────────────────────────────────────────────────────────────────────────────
// ToolResult — wraps tool output
// ─────────────────────────────────────────────────────────────────────────────
public class ToolResult {
    private final String  content;
    private final boolean error;

    private ToolResult(String content, boolean error) {
        this.content = content;
        this.error   = error;
    }

    public static ToolResult success(String content) { return new ToolResult(content, false); }
    public static ToolResult error(String message)   { return new ToolResult(message, true); }

    public String  getContent() { return content; }
    public boolean isError()    { return error; }
}

// ─────────────────────────────────────────────────────────────────────────────
// ToolRegistry — stores and looks up tools by name
// ─────────────────────────────────────────────────────────────────────────────
public class ToolRegistry {
    private final Map<String, McpTool> tools = new LinkedHashMap<>();

    public void register(McpTool tool) { tools.put(tool.getName(), tool); }
    public McpTool          get(String name) { return tools.get(name); }
    public Collection<McpTool> getAll()      { return tools.values(); }
}

// ─────────────────────────────────────────────────────────────────────────────
// BaseTool — shared schema helper for all tools
// ─────────────────────────────────────────────────────────────────────────────
public abstract class BaseTool implements McpTool {

    protected final ObjectMapper mapper = new ObjectMapper();

    /** Build a minimal JSON Schema with common security param + custom properties */
    protected ObjectNode baseSchema(ObjectMapper m, String... requiredFields) {
        ObjectNode schema     = m.createObjectNode();
        schema.put("type",    "object");
        ObjectNode properties = m.createObjectNode();

        // Every tool requires caller_jwt for authentication
        ObjectNode jwtProp = m.createObjectNode();
        jwtProp.put("type",        "string");
        jwtProp.put("description", "Bearer JWT issued by auth-service/Keycloak. Required for all calls.");
        properties.set("caller_jwt", jwtProp);

        addProperties(m, properties);
        schema.set("properties", properties);

        // required fields always include caller_jwt
        var required = m.createArrayNode();
        required.add("caller_jwt");
        for (String f : requiredFields) required.add(f);
        schema.set("required", required);

        return schema;
    }

    /** Subclasses add their specific properties into the properties ObjectNode */
    protected abstract void addProperties(ObjectMapper m, ObjectNode properties);

    /** Convenience: get string param or default */
    protected String str(JsonNode args, String key, String def) {
        return args.has(key) ? args.get(key).asText(def) : def;
    }

    /** Convenience: get int param or default */
    protected int num(JsonNode args, String key, int def) {
        return args.has(key) ? args.get(key).asInt(def) : def;
    }

    /** Convenience: get boolean param or default */
    protected boolean bool(JsonNode args, String key, boolean def) {
        return args.has(key) ? args.get(key).asBoolean(def) : def;
    }

    /** Sanitize string input — strip potential injection chars */
    protected String sanitize(String input) {
        if (input == null) return "";
        return input.replaceAll("[<>\"'`;\\\\]", "").trim();
    }

    /** Validate UUID-like session_id format */
    protected boolean isValidSessionId(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) return false;
        return sessionId.matches("[a-zA-Z0-9\\-_]{4,64}");
    }

    /** Validate participant_id format */
    protected boolean isValidParticipantId(String participantId) {
        if (participantId == null || participantId.isBlank()) return false;
        return participantId.matches("[a-zA-Z0-9\\-_@.]{2,64}");
    }

    /** Format a JSON-like response string */
    protected String json(String... kvPairs) {
        StringBuilder sb = new StringBuilder("{\n");
        for (int i = 0; i < kvPairs.length; i += 2) {
            String key = kvPairs[i];
            String val = kvPairs[i + 1];
            sb.append("  \"").append(key).append("\": ");
            // If val looks like a number/boolean/array/object, don't quote it
            if (val.matches("-?\\d+(\\.\\d+)?") || val.equals("true") || val.equals("false")
                    || val.startsWith("{") || val.startsWith("[") || val.equals("null")) {
                sb.append(val);
            } else {
                sb.append("\"").append(val.replace("\"", "\\\"")).append("\"");
            }
            if (i + 2 < kvPairs.length) sb.append(",");
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
