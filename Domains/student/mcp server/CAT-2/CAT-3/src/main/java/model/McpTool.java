package com.ecoskiller.mcp.security.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * MCP Tool definition — describes an agent's callable tool
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class McpTool {
    private String name;
    private String description;
    private InputSchema inputSchema;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InputSchema {
        private String type = "object";
        private Map<String, PropertyDef> properties;
        private java.util.List<String> required;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PropertyDef {
        private String type;
        private String description;
        @com.fasterxml.jackson.annotation.JsonProperty("enum")
        private java.util.List<String> enumValues;
    }
}
