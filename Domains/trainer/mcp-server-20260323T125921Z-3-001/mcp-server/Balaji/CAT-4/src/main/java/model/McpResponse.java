package com.ecoskiller.mcp.aigovernance.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class McpResponse {
    private String jsonrpc = "2.0";
    private String id;
    private Object result;
    private McpError error;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class McpError {
        private int code;
        private String message;
        private Object data;
    }

    public static McpResponse success(String id, Object result) {
        return McpResponse.builder().jsonrpc("2.0").id(id).result(result).build();
    }

    public static McpResponse error(String id, int code, String message) {
        return McpResponse.builder().jsonrpc("2.0").id(id)
                .error(McpError.builder().code(code).message(message).build()).build();
    }
}
