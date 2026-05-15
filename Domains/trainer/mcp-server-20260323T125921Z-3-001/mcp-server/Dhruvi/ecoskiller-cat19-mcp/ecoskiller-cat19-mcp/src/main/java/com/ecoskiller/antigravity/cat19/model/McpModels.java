package com.ecoskiller.antigravity.cat19.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * MCP Protocol Models — JSON-RPC 2.0 compliant
 * CAT-19: Platform Stability & Risk — ANTIGRAVITY
 */
public class McpModels {

    // ─── JSON-RPC Request ────────────────────────────────────────────
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class McpRequest {
        private String jsonrpc = "2.0";
        private String method;
        private Object params;
        private Object id;
    }

    // ─── JSON-RPC Response ───────────────────────────────────────────
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class McpResponse {
        private String jsonrpc = "2.0";
        private Object result;
        private McpError error;
        private Object id;
    }

    // ─── MCP Error ───────────────────────────────────────────────────
    @Data
    @Builder
    public static class McpError {
        private int code;
        private String message;
        private Object data;
    }

    // ─── Server Info ─────────────────────────────────────────────────
    @Data
    @Builder
    public static class ServerInfo {
        private String name;
        private String version;
        private ProtocolVersion protocolVersion;
        private ServerCapabilities capabilities;
    }

    @Data
    @Builder
    public static class ProtocolVersion {
        private int major;
        private int minor;
        private int patch;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ServerCapabilities {
        private ToolsCapability tools;
        private ResourcesCapability resources;
        private LoggingCapability logging;
    }

    @Data
    @Builder
    public static class ToolsCapability {
        private boolean listChanged;
    }

    @Data
    @Builder
    public static class ResourcesCapability {
        private boolean subscribe;
        private boolean listChanged;
    }

    @Data
    @Builder
    public static class LoggingCapability {}

    // ─── Tool Definition ─────────────────────────────────────────────
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class McpTool {
        private String name;
        private String description;
        @JsonProperty("inputSchema")
        private InputSchema inputSchema;
    }

    @Data
    @Builder
    public static class InputSchema {
        private String type = "object";
        private Map<String, PropertySchema> properties;
        private List<String> required;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PropertySchema {
        private String type;
        private String description;
        @JsonProperty("enum")
        private List<String> enumValues;
        private String defaultValue;
    }

    // ─── Tool Call ───────────────────────────────────────────────────
    @Data
    public static class ToolCallParams {
        private String name;
        private Map<String, Object> arguments;
    }

    // ─── Tool Result ─────────────────────────────────────────────────
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ToolResult {
        private List<ContentBlock> content;
        @JsonProperty("isError")
        private boolean isError;
    }

    @Data
    @Builder
    public static class ContentBlock {
        private String type;
        private String text;
    }

    // ─── Tools List Result ───────────────────────────────────────────
    @Data
    @Builder
    public static class ToolsListResult {
        private List<McpTool> tools;
    }

    // ─── Agent Result Wrapper ────────────────────────────────────────
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AgentResult {
        private String agentName;
        private String category;
        private String mcpServer;
        private String status;
        private String timestamp;
        private Object payload;
        private String antigravityToken;
    }
}
