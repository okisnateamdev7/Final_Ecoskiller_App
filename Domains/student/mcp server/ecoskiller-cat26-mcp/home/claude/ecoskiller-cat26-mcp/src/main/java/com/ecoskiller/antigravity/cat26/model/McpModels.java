package com.ecoskiller.antigravity.cat26.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * MCP Protocol Models — JSON-RPC 2.0
 * CAT-26: Organizer & Network Management — ANTIGRAVITY SEALED
 */
public class McpModels {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class McpRequest {
        public String jsonrpc;
        public String method;
        public Object params;
        public Object id;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class McpResponse {
        public String jsonrpc = "2.0";
        public Object result;
        public McpError error;
        public Object id;

        public static McpResponse ok(Object result, Object id) {
            McpResponse r = new McpResponse(); r.result = result; r.id = id; return r;
        }
        public static McpResponse err(int code, String msg, Object id) {
            McpResponse r = new McpResponse(); r.error = new McpError(code, msg); r.id = id; return r;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class McpError {
        public int code; public String message;
        public McpError(int code, String message) { this.code = code; this.message = message; }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class McpTool {
        public String name;
        public String description;
        @JsonProperty("inputSchema") public InputSchema inputSchema;
        public McpTool(String name, String description, InputSchema inputSchema) {
            this.name = name; this.description = description; this.inputSchema = inputSchema;
        }
    }

    public static class InputSchema {
        public String type = "object";
        public Map<String, PropSchema> properties;
        public List<String> required;
        public InputSchema(Map<String, PropSchema> properties, List<String> required) {
            this.properties = properties; this.required = required;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PropSchema {
        public String type; public String description;
        @JsonProperty("enum") public List<String> enumValues;
        public PropSchema(String type, String description) { this.type = type; this.description = description; }
        public PropSchema(String type, String description, List<String> e) {
            this.type = type; this.description = description; this.enumValues = e;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ToolCallResult {
        public List<ContentBlock> content;
        @JsonProperty("isError") public boolean isError;
        public ToolCallResult(String text, boolean isError) {
            this.content = List.of(new ContentBlock("text", text)); this.isError = isError;
        }
    }

    public static class ContentBlock {
        public String type; public String text;
        public ContentBlock(String type, String text) { this.type = type; this.text = text; }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AgentResult {
        public String agentName;
        public String category   = "CAT-26 Organizer & Network Management";
        public String mcpServer  = "26. Organizer & Network Management";
        public String status;
        public String timestamp;
        public Object payload;
        public String antigravityToken = "ANTIGRAVITY_SEALED_v1.0";

        public AgentResult(String agentName, String status, Object payload) {
            this.agentName = agentName; this.status = status; this.payload = payload;
            this.timestamp = Instant.now().toString();
        }
    }
}
