package com.ecoskiller.mcp.aigovernance.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class McpTool {
    private String name;
    private String description;
    private InputSchema inputSchema;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class InputSchema {
        private String type = "object";
        private Map<String, PropertyDef> properties;
        private List<String> required;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class PropertyDef {
        private String type;
        private String description;
        @JsonProperty("enum")
        private List<String> enumValues;
    }
}
