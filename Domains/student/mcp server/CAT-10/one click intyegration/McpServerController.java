package com.ecoskiller.mcp.integrations.server;

import com.ecoskiller.mcp.integrations.model.McpProtocol.*;
import com.ecoskiller.mcp.integrations.tools.ToolRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Map;

/**
 * McpServerController — HTTP transport for the MCP protocol.
 *
 * Supports two transport modes:
 *   1. HTTP POST /mcp  — Standard JSON-RPC 2.0 (request/response)
 *   2. GET /mcp/sse   — Server-Sent Events stream (for MCP SSE transport)
 *
 * Protocol: MCP 2024-11-05
 */
@RestController
@RequestMapping("/mcp")
public class McpServerController {

    private static final Logger log = LoggerFactory.getLogger(McpServerController.class);
    private static final String MCP_VERSION = "2024-11-05";

    private final ToolRegistry toolRegistry;
    private final ObjectMapper objectMapper;

    public McpServerController(ToolRegistry toolRegistry, ObjectMapper objectMapper) {
        this.toolRegistry = toolRegistry;
        this.objectMapper = objectMapper;
    }

    // ─────────────────────────────────────────────
    // Primary HTTP JSON-RPC endpoint
    // ─────────────────────────────────────────────

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonRpcResponse> handleRequest(@RequestBody JsonRpcRequest request) {
        log.debug("MCP Request: method={} id={}", request.getMethod(), request.getId());
        JsonRpcResponse response = dispatch(request);
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────────
    // SSE transport (GET /mcp/sse)
    // ─────────────────────────────────────────────

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> sseStream() {
        return Flux.interval(Duration.ofSeconds(30))
                .map(i -> "data: {\"jsonrpc\":\"2.0\",\"method\":\"ping\"}\n\n");
    }

    // ─────────────────────────────────────────────
    // Health check
    // ─────────────────────────────────────────────

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "server", "mcp-07-integrations",
            "version", "1.0.0",
            "protocol", MCP_VERSION,
            "tools_registered", toolRegistry.getToolCount()
        );
    }

    // ─────────────────────────────────────────────
    // JSON-RPC dispatcher
    // ─────────────────────────────────────────────

    private JsonRpcResponse dispatch(JsonRpcRequest request) {
        try {
            Object result = switch (request.getMethod()) {
                case "initialize" -> handleInitialize();
                case "initialized" -> null; // notification — no response needed
                case "tools/list" -> toolRegistry.listTools();
                case "tools/call" -> handleToolsCall(request.getParams());
                case "ping" -> Map.of("pong", true);
                default -> throw new McpMethodNotFoundError(request.getMethod());
            };

            // notifications return null — skip response
            if (result == null && "initialized".equals(request.getMethod())) {
                return null;
            }

            return JsonRpcResponse.builder()
                    .jsonrpc("2.0")
                    .id(request.getId())
                    .result(result)
                    .build();

        } catch (McpMethodNotFoundError e) {
            return errorResponse(request.getId(), -32601, "Method not found: " + e.getMessage(), null);
        } catch (McpInvalidParamsError e) {
            return errorResponse(request.getId(), -32602, "Invalid params: " + e.getMessage(), null);
        } catch (Exception e) {
            log.error("Internal error handling method={}", request.getMethod(), e);
            return errorResponse(request.getId(), -32603, "Internal error: " + e.getMessage(), null);
        }
    }

    // ─────────────────────────────────────────────
    // Method handlers
    // ─────────────────────────────────────────────

    private InitializeResult handleInitialize() {
        return InitializeResult.builder()
                .protocolVersion(MCP_VERSION)
                .serverInfo(ServerInfo.builder()
                        .name("mcp-07-integrations")
                        .version("1.0.0")
                        .build())
                .capabilities(Capabilities.builder()
                        .tools(ToolsCapability.builder().listChanged(false).build())
                        .build())
                .build();
    }

    @SuppressWarnings("unchecked")
    private ToolCallResult handleToolsCall(Object params) {
        if (params == null) throw new McpInvalidParamsError("params is required for tools/call");

        ToolCallParams callParams;
        try {
            callParams = objectMapper.convertValue(params, ToolCallParams.class);
        } catch (Exception e) {
            throw new McpInvalidParamsError("Cannot parse tool call params: " + e.getMessage());
        }

        if (callParams.getName() == null || callParams.getName().isBlank()) {
            throw new McpInvalidParamsError("Tool 'name' is required");
        }

        Map<String, Object> arguments = callParams.getArguments() != null
                ? callParams.getArguments()
                : Map.of();

        return toolRegistry.callTool(callParams.getName(), arguments);
    }

    // ─────────────────────────────────────────────
    // Error helpers
    // ─────────────────────────────────────────────

    private JsonRpcResponse errorResponse(String id, int code, String message, Object data) {
        return JsonRpcResponse.builder()
                .jsonrpc("2.0")
                .id(id)
                .error(JsonRpcError.builder().code(code).message(message).data(data).build())
                .build();
    }

    // ─────────────────────────────────────────────
    // Internal exceptions
    // ─────────────────────────────────────────────

    private static class McpMethodNotFoundError extends RuntimeException {
        McpMethodNotFoundError(String method) { super(method); }
    }

    private static class McpInvalidParamsError extends RuntimeException {
        McpInvalidParamsError(String msg) { super(msg); }
    }
}
