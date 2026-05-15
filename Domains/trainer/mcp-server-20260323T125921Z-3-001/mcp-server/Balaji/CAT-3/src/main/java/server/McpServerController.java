package com.ecoskiller.mcp.security.server;

import com.ecoskiller.mcp.security.agents.McpAgent;
import com.ecoskiller.mcp.security.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * MCP-03 Security Server — Main JSON-RPC 2.0 Controller
 *
 * Endpoints:
 *   POST /mcp         → Main MCP JSON-RPC endpoint
 *   GET  /mcp/health  → Health check
 *   GET  /mcp/info    → Server capabilities info
 */
@Slf4j
@RestController
@RequestMapping("/mcp")
@RequiredArgsConstructor
public class McpServerController {

    private final AgentRegistry registry;
    private final ObjectMapper objectMapper;

    @Value("${mcp.server.name}")     private String serverName;
    @Value("${mcp.server.version}")  private String serverVersion;

    // ─────────────────────────────────────────────────────────────────────────
    // Main MCP endpoint — handles all JSON-RPC 2.0 methods
    // ─────────────────────────────────────────────────────────────────────────

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public McpResponse handle(@RequestBody McpRequest request) {
        log.debug("MCP request: method={}, id={}", request.getMethod(), request.getId());

        try {
            return switch (request.getMethod()) {

                // MCP handshake
                case "initialize" -> handleInitialize(request);

                // List all tools from all agents
                case "tools/list" -> handleToolsList(request);

                // Execute a tool
                case "tools/call" -> handleToolCall(request);

                // Ping
                case "ping" -> McpResponse.success(request.getId(), Map.of("status", "pong"));

                default -> McpResponse.error(request.getId(), -32601,
                        "Method not found: " + request.getMethod());
            };
        } catch (Exception e) {
            log.error("Error handling MCP request", e);
            return McpResponse.error(request.getId(), -32603, "Internal error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // initialize — MCP handshake, return server capabilities
    // ─────────────────────────────────────────────────────────────────────────

    private McpResponse handleInitialize(McpRequest request) {
        ObjectNode capabilities = objectMapper.createObjectNode();
        ObjectNode toolsCap = capabilities.putObject("tools");
        toolsCap.put("listChanged", false);

        ObjectNode result = objectMapper.createObjectNode();
        result.put("protocolVersion", "2024-11-05");
        result.put("serverName", serverName);
        result.put("serverVersion", serverVersion);
        result.set("capabilities", capabilities);

        // Server info
        ObjectNode info = result.putObject("serverInfo");
        info.put("name", serverName);
        info.put("version", serverVersion);
        info.put("category", "CAT-03");
        info.put("description", "Security, Tenancy & Migration Control");
        info.put("namespace", "core");
        info.put("priority", "CRITICAL");
        info.put("agentCount", registry.getAgentCount());
        info.put("toolCount", registry.getToolCount());

        log.info("MCP Initialize handshake completed");
        return McpResponse.success(request.getId(), result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // tools/list — return all tools from all 12 agents
    // ─────────────────────────────────────────────────────────────────────────

    private McpResponse handleToolsList(McpRequest request) {
        List<McpTool> tools = registry.getAllTools();
        ObjectNode result = objectMapper.createObjectNode();
        result.set("tools", objectMapper.valueToTree(tools));
        return McpResponse.success(request.getId(), result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // tools/call — dispatch to the correct agent
    // ─────────────────────────────────────────────────────────────────────────

    private McpResponse handleToolCall(McpRequest request) {
        JsonNode params = request.getParams();

        if (params == null || !params.has("name")) {
            return McpResponse.error(request.getId(), -32602, "Invalid params: 'name' is required");
        }

        String toolName = params.get("name").asText();
        JsonNode arguments = params.has("arguments") ? params.get("arguments")
                : objectMapper.createObjectNode();

        Optional<McpAgent> agentOpt = registry.findAgentForTool(toolName);

        if (agentOpt.isEmpty()) {
            return McpResponse.error(request.getId(), -32602,
                    "Tool not found: " + toolName);
        }

        McpAgent agent = agentOpt.get();
        log.debug("Dispatching tool '{}' to agent '{}'", toolName, agent.getName());

        ToolResult toolResult = agent.executeTool(toolName, arguments);
        return McpResponse.success(request.getId(), toolResult);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Utility endpoints
    // ─────────────────────────────────────────────────────────────────────────

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "server", serverName,
            "agents", registry.getAgentCount(),
            "tools", registry.getToolCount()
        );
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        return Map.of(
            "server", serverName,
            "version", serverVersion,
            "category", "CAT-03 — Security, Tenancy & Migration Control",
            "namespace", "core",
            "priority", "CRITICAL",
            "agents", registry.getAllAgents().stream().map(a -> Map.of(
                "name", a.getName(),
                "description", a.getDescription(),
                "toolCount", a.getTools().size()
            )).toList()
        );
    }
}
