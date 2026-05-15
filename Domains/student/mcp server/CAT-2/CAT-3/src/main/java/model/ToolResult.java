package com.ecoskiller.mcp.security.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * MCP Tool call result
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToolResult {
    private List<ContentBlock> content;
    private Boolean isError;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContentBlock {
        private String type;   // "text" | "image" | "resource"
        private String text;
    }

    public static ToolResult text(String text) {
        return ToolResult.builder()
                .content(List.of(ContentBlock.builder().type("text").text(text).build()))
                .isError(false)
                .build();
    }

    public static ToolResult error(String message) {
        return ToolResult.builder()
                .content(List.of(ContentBlock.builder().type("text").text("ERROR: " + message).build()))
                .isError(true)
                .build();
    }
}
