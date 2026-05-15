package com.ecoskiller.mcp.aigovernance.server;

import com.ecoskiller.mcp.aigovernance.agents.McpAgent;
import com.ecoskiller.mcp.aigovernance.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * MCP-04 AI Governance — JSON-RPC 2.0 Controller
 *
 *   POST /mcp        → MCP protocol endpoint
 *   GET  /mcp/health → Health check
 *   GET  /mcp/info   → Server info & agent list
 */
@Slf4j
@RestController
@RequestMapping("/mcp")
@RequiredArgsConstructor
public class McpServerController {

    private final AgentRegistry registry;
    private final ObjectMapper objectMapper;

    @Value("${mcp.server.name}")    private String serverName;
    @Value("${mcp.server.version}") private String serverVersion;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public McpResponse handle(@RequestBody McpRequest request) {
        log.debug("MCP request: method={} id={}", request.getMethod(), request.getId());
        try {
            return switch (request.getMethod()) {
                case "initialize"  -> handleInitialize(request);
                case "tools/list"  -> handleToolsList(request);
                case "tools/call"  -> handleToolCall(request);
                case "ping"        -> McpResponse.success(request.getId(), Map.of("status", "pong"));
                default            -> McpResponse.error(request.getId(), -32601, "Method not found: " + request.getMethod());
            };
        } catch (Exception e) {
            log.error("Error handling MCP request", e);
            return McpResponse.error(request.getId(), -32603, "Internal error: " + e.getMessage());
        }
    }

    private McpResponse handleInitialize(McpRequest request) {
        ObjectNode caps = objectMapper.createObjectNode();
        caps.putObject("tools").put("listChanged", false);

        ObjectNode result = objectMapper.createObjectNode();
        result.put("protocolVersion", "2024-11-05");
        result.put("serverName", serverName);
        result.put("serverVersion", serverVersion);
        result.set("capabilities", caps);

        ObjectNode info = result.putObject("serverInfo");
        info.put("name",        serverName);
        info.put("version",     serverVersion);
        info.put("category",    "CAT-04");
        info.put("description", "AI & Intelligence Governance");
        info.put("namespace",   "core");
        info.put("priority",    "HIGH");
        info.put("agentCount",  registry.getAgentCount());
        info.put("toolCount",   registry.getToolCount());

        return McpResponse.success(request.getId(), result);
    }

    private McpResponse handleToolsList(McpRequest request) {
        ObjectNode result = objectMapper.createObjectNode();
        result.set("tools", objectMapper.valueToTree(registry.getAllTools()));
        return McpResponse.success(request.getId(), result);
    }

    private McpResponse handleToolCall(McpRequest request) {
        JsonNode params = request.getParams();
        if (params == null || !params.has("name")) {
            return McpResponse.error(request.getId(), -32602, "Invalid params: 'name' is required");
        }
        String toolName   = params.get("name").asText();
        JsonNode arguments = params.has("arguments") ? params.get("arguments") : objectMapper.createObjectNode();

        Optional<McpAgent> agentOpt = registry.findAgentForTool(toolName);
        if (agentOpt.isEmpty()) {
            return McpResponse.error(request.getId(), -32602, "Tool not found: " + toolName);
        }
        McpAgent agent = agentOpt.get();
        log.debug("Dispatching tool '{}' to agent '{}'", toolName, agent.getName());
        return McpResponse.success(request.getId(), agent.executeTool(toolName, arguments));
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("status", "UP", "server", serverName, "agents", registry.getAgentCount(), "tools", registry.getToolCount());
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        return Map.of(
            "server",   serverName,
            "version",  serverVersion,
            "category", "CAT-04 — AI & Intelligence Governance",
            "namespace","core",
            "priority", "HIGH",
            "agents",   registry.getAllAgents().stream().map(a -> Map.of(
                "name",       a.getName(),
                "description",a.getDescription(),
                "toolCount",  a.getTools().size()
            )).toList()
        );
    }
}
