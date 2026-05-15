package com.ecoskiller.mcp.economics.server;

import com.ecoskiller.mcp.economics.model.McpProtocol.*;
import com.ecoskiller.mcp.economics.tools.ToolRegistry;
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
 * McpServerController — MCP 2024-11-05, HTTP + SSE transport
 * Endpoint: POST /mcp | GET /mcp/sse | GET /mcp/health
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonRpcResponse> handle(@RequestBody JsonRpcRequest request) {
        log.debug("MCP [{} id={}]", request.getMethod(), request.getId());
        return ResponseEntity.ok(dispatch(request));
    }

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> sse() {
        return Flux.interval(Duration.ofSeconds(30))
                .map(i -> "data: {\"jsonrpc\":\"2.0\",\"method\":\"ping\"}\n\n");
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("status","UP","server","mcp-08-economics","version","1.0.0",
                      "protocol",MCP_VERSION,"tools",toolRegistry.getToolCount());
    }

    private JsonRpcResponse dispatch(JsonRpcRequest req) {
        try {
            Object result = switch (req.getMethod()) {
                case "initialize" -> InitializeResult.builder()
                        .protocolVersion(MCP_VERSION)
                        .serverInfo(ServerInfo.builder().name("mcp-08-economics").version("1.0.0").build())
                        .capabilities(Capabilities.builder()
                                .tools(ToolsCapability.builder().listChanged(false).build()).build())
                        .build();
                case "initialized" -> null;
                case "tools/list" -> toolRegistry.listTools();
                case "tools/call" -> handleCall(req.getParams());
                case "ping" -> Map.of("pong", true);
                default -> throw new MethodNotFound(req.getMethod());
            };
            if (result == null) return null;
            return JsonRpcResponse.builder().jsonrpc("2.0").id(req.getId()).result(result).build();
        } catch (MethodNotFound e) {
            return err(req.getId(), -32601, "Method not found: " + e.getMessage());
        } catch (InvalidParams e) {
            return err(req.getId(), -32602, "Invalid params: " + e.getMessage());
        } catch (Exception e) {
            log.error("Internal error", e);
            return err(req.getId(), -32603, "Internal error: " + e.getMessage());
        }
    }

    private ToolCallResult handleCall(Object params) {
        if (params == null) throw new InvalidParams("params required");
        ToolCallParams p;
        try { p = objectMapper.convertValue(params, ToolCallParams.class); }
        catch (Exception e) { throw new InvalidParams("Cannot parse: " + e.getMessage()); }
        if (p.getName() == null || p.getName().isBlank()) throw new InvalidParams("name required");
        return toolRegistry.callTool(p.getName(), p.getArguments() != null ? p.getArguments() : Map.of());
    }

    private JsonRpcResponse err(String id, int code, String msg) {
        return JsonRpcResponse.builder().jsonrpc("2.0").id(id)
                .error(JsonRpcError.builder().code(code).message(msg).build()).build();
    }

    private static class MethodNotFound extends RuntimeException { MethodNotFound(String m) { super(m); } }
    private static class InvalidParams extends RuntimeException { InvalidParams(String m) { super(m); } }
}
