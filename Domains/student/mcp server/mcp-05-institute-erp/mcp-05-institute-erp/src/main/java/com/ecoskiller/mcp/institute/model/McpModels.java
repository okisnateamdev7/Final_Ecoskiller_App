package com.ecoskiller.mcp.institute.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

// ─────────────────────────────────────────────────────────────────────────────
//  JSON-RPC 2.0 envelope
// ─────────────────────────────────────────────────────────────────────────────

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class McpRequest {
    private String jsonrpc = "2.0";
    private String id;
    private String method;
    private JsonNode params;
}

// ─────────────────────────────────────────────────────────────────────────────

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
class McpResponse {
    private String jsonrpc = "2.0";
    private String id;
    private Object result;
    private McpError error;

    public static McpResponse ok(String id, Object result) {
        return McpResponse.builder().jsonrpc("2.0").id(id).result(result).build();
    }

    public static McpResponse err(String id, int code, String message) {
        return McpResponse.builder()
                .jsonrpc("2.0").id(id)
                .error(new McpError(code, message))
                .build();
    }
}

// ─────────────────────────────────────────────────────────────────────────────

@Data
@AllArgsConstructor
@NoArgsConstructor
class McpError {
    private int code;
    private String message;
}

// ─────────────────────────────────────────────────────────────────────────────
//  MCP Tool descriptor (returned by tools/list)
// ─────────────────────────────────────────────────────────────────────────────

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
class McpTool {
    private String name;
    private String description;
    private Map<String, Object> inputSchema;   // JSON Schema object
    private String agentRef;                    // Antigravity agent file reference
    private boolean sealed;                     // ANTIGRAVITY_SEALED flag
}

// ─────────────────────────────────────────────────────────────────────────────
//  tools/list result
// ─────────────────────────────────────────────────────────────────────────────

@Data
@AllArgsConstructor
@NoArgsConstructor
class ToolListResult {
    private List<McpTool> tools;
}

// ─────────────────────────────────────────────────────────────────────────────
//  tools/call result
// ─────────────────────────────────────────────────────────────────────────────

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
class ToolCallResult {
    private List<ContentBlock> content;
    private boolean isError;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContentBlock {
        private String type;    // "text"
        private String text;
    }

    public static ToolCallResult text(String text) {
        return ToolCallResult.builder()
                .content(List.of(new ContentBlock("text", text)))
                .isError(false)
                .build();
    }

    public static ToolCallResult error(String message) {
        return ToolCallResult.builder()
                .content(List.of(new ContentBlock("text", message)))
                .isError(true)
                .build();
    }
}
