package com.ecoskiller.antigravity.cat19.server;

import com.ecoskiller.antigravity.cat19.model.McpModels.*;
import com.ecoskiller.antigravity.cat19.tools.Cat19ToolRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  MCP Server Controller                                      ║
 * ║  CAT-19: Platform Stability & Risk — ANTIGRAVITY            ║
 * ║  Transport: HTTP + SSE (JSON-RPC 2.0)                       ║
 * ╚══════════════════════════════════════════════════════════════╝
 *
 * Endpoints:
 *   POST /mcp          — JSON-RPC message handler
 *   GET  /mcp/sse      — SSE transport (Claude Desktop compatible)
 *   GET  /mcp/health   — Server health check
 *   GET  /mcp/manifest — Agent manifest
 */
@Slf4j
@RestController
@RequestMapping("/mcp")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class McpServerController {

    private final Cat19ToolRegistry toolRegistry;
    private final ObjectMapper objectMapper;
    private final ExecutorService sseExecutor = Executors.newCachedThreadPool();

    // ─── MCP JSON-RPC Handler ─────────────────────────────────────────

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<McpResponse> handleMcpMessage(@RequestBody Map<String, Object> rawRequest) {
        Object id     = rawRequest.get("id");
        String method = (String) rawRequest.get("method");

        log.info("[MCP-SERVER] method={} id={}", method, id);

        try {
            Object result = switch (method) {
                case "initialize"       -> handleInitialize(rawRequest);
                case "tools/list"       -> handleToolsList();
                case "tools/call"       -> handleToolsCall(rawRequest);
                case "ping"             -> Map.of("pong", true, "server", "CAT-19-ANTIGRAVITY");
                default -> {
                    log.warn("[MCP-SERVER] Unknown method: {}", method);
                    yield Map.of("error", "Method not found: " + method);
                }
            };

            return ResponseEntity.ok(
                McpResponse.builder()
                    .jsonrpc("2.0")
                    .result(result)
                    .id(id)
                    .build()
            );

        } catch (Exception e) {
            log.error("[MCP-SERVER] Error handling method {}: {}", method, e.getMessage(), e);
            return ResponseEntity.ok(
                McpResponse.builder()
                    .jsonrpc("2.0")
                    .error(McpError.builder()
                        .code(-32603)
                        .message("Internal error: " + e.getMessage())
                        .build())
                    .id(id)
                    .build()
            );
        }
    }

    // ─── SSE Transport (Claude Desktop / Cursor compatible) ──────────

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter handleSse() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseExecutor.execute(() -> {
            try {
                // Send server capabilities on connect
                Map<String, Object> initEvent = new LinkedHashMap<>();
                initEvent.put("type", "server_info");
                initEvent.put("server", buildServerInfo());
                emitter.send(SseEmitter.event()
                    .name("message")
                    .data(objectMapper.writeValueAsString(initEvent)));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    // ─── Health & Manifest ───────────────────────────────────────────

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "server", "CAT-19-PLATFORM-STABILITY-RISK-MCP",
            "platform", "ECOSKILLER-ANTIGRAVITY",
            "version", "1.0.0-ANTIGRAVITY-SEALED",
            "agents_registered", 4,
            "tools_registered", toolRegistry.getAllTools().size(),
            "timestamp", new Date().toString()
        ));
    }

    @GetMapping("/manifest")
    public ResponseEntity<Map<String, Object>> manifest() {
        Map<String, Object> manifest = new LinkedHashMap<>();
        manifest.put("mcp_server", "19. Platform Stability & Risk");
        manifest.put("platform", "ECOSKILLER ANTIGRAVITY");
        manifest.put("version", "1.0.0-ANTIGRAVITY-SEALED");
        manifest.put("category", "CAT-19");
        manifest.put("agents", List.of(
            Map.of("name", "POLICY_VERSION_CONTROL_AGENT",         "tools", 4, "status", "ACTIVE"),
            Map.of("name", "REGULATORY_COMPLIANCE_MAPPING_AGENT",  "tools", 3, "status", "ACTIVE"),
            Map.of("name", "RESOURCE_CONSUMPTION_GUARD_AGENT",     "tools", 4, "status", "ACTIVE"),
            Map.of("name", "REVENUE_SHARE_RECONCILIATION_AGENT",   "tools", 4, "status", "ACTIVE")
        ));
        manifest.put("total_tools", toolRegistry.getAllTools().size());
        manifest.put("transport", List.of("HTTP/JSON-RPC-2.0", "SSE"));
        return ResponseEntity.ok(manifest);
    }

    // ─── MCP Method Implementations ──────────────────────────────────

    @SuppressWarnings("unchecked")
    private Map<String, Object> handleInitialize(Map<String, Object> request) {
        log.info("[MCP-SERVER] Initializing connection...");
        ServerInfo info = buildServerInfo();
        return Map.of(
            "protocolVersion", "2024-11-05",
            "capabilities", Map.of(
                "tools",     Map.of("listChanged", false),
                "resources", Map.of("subscribe", false, "listChanged", false),
                "logging",   Map.of()
            ),
            "serverInfo", Map.of(
                "name",    "ecoskiller-cat19-antigravity-mcp",
                "version", "1.0.0-ANTIGRAVITY-SEALED"
            )
        );
    }

    private Map<String, Object> handleToolsList() {
        return Map.of("tools", toolRegistry.getAllTools());
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> handleToolsCall(Map<String, Object> request) {
        Map<String, Object> params    = (Map<String, Object>) request.get("params");
        String toolName               = (String) params.get("name");
        Map<String, Object> arguments = (Map<String, Object>) params.getOrDefault("arguments", Map.of());

        log.info("[MCP-SERVER] tools/call → tool={}", toolName);

        AgentResult result = toolRegistry.dispatch(toolName, arguments);

        String resultText;
        try {
            resultText = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        } catch (Exception e) {
            resultText = result.toString();
        }

        return Map.of(
            "content", List.of(Map.of("type", "text", "text", resultText)),
            "isError", "ERROR".equals(result.getStatus())
        );
    }

    // ─── Helpers ─────────────────────────────────────────────────────

    private ServerInfo buildServerInfo() {
        return ServerInfo.builder()
            .name("ecoskiller-cat19-antigravity-mcp")
            .version("1.0.0-ANTIGRAVITY-SEALED")
            .protocolVersion(ProtocolVersion.builder().major(2024).minor(11).patch(5).build())
            .capabilities(ServerCapabilities.builder()
                .tools(ToolsCapability.builder().listChanged(false).build())
                .resources(ResourcesCapability.builder().subscribe(false).listChanged(false).build())
                .logging(LoggingCapability.builder().build())
                .build())
            .build();
    }
}
