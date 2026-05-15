package com.ecoskiller.mcp.security.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MCP JSON-RPC 2.0 Request
 * Standard MCP protocol message format
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class McpRequest {
    private String jsonrpc = "2.0";
    private String id;
    private String method;
    private JsonNode params;
}
