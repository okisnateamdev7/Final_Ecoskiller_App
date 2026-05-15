package com.ecoskiller.mcp.integrations.tools;

import com.ecoskiller.mcp.integrations.agents.AgentTool;
import com.ecoskiller.mcp.integrations.model.McpProtocol.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ToolRegistry — auto-discovers all AgentTool beans (the 18 CAT-07 agents)
 * and exposes them for MCP tools/list and tools/call dispatch.
 */
@Component
public class ToolRegistry {

    private static final Logger log = LoggerFactory.getLogger(ToolRegistry.class);

    private final Map<String, AgentTool> toolsByName;

    public ToolRegistry(List<AgentTool> agentTools) {
        this.toolsByName = agentTools.stream()
                .collect(Collectors.toMap(AgentTool::getName, Function.identity()));
    }

    @PostConstruct
    public void init() {
        log.info("╔══════════════════════════════════════════╗");
        log.info("║  mcp-07-integrations — ToolRegistry      ║");
        log.info("╠══════════════════════════════════════════╣");
        toolsByName.keySet().stream().sorted().forEach(name ->
            log.info("║  ✓ {}", String.format("%-40s", name) + "║"));
        log.info("╠══════════════════════════════════════════╣");
        log.info("║  Total tools registered: {}              ║", toolsByName.size());
        log.info("╚══════════════════════════════════════════╝");
    }

    /** Build the MCP tools/list response */
    public ToolsListResult listTools() {
        List<Tool> tools = toolsByName.values().stream()
                .map(t -> Tool.builder()
                        .name(t.getName())
                        .description(t.getDescription())
                        .inputSchema(t.getInputSchema())
                        .build())
                .sorted((a, b) -> a.getName().compareTo(b.getName()))
                .collect(Collectors.toList());
        return ToolsListResult.builder().tools(tools).build();
    }

    /** Dispatch a tools/call request to the correct agent */
    public ToolCallResult callTool(String toolName, Map<String, Object> arguments) {
        AgentTool tool = toolsByName.get(toolName);
        if (tool == null) {
            log.warn("Tool not found: {}", toolName);
            return ToolCallResult.builder()
                    .isError(true)
                    .content(List.of(ContentBlock.builder()
                            .type("text")
                            .text("ERROR: Tool '" + toolName + "' not found in mcp-07-integrations. " +
                                  "Available tools: " + toolsByName.keySet().stream().sorted().toList())
                            .build()))
                    .build();
        }
        return tool.execute(arguments);
    }

    public Optional<AgentTool> findTool(String name) {
        return Optional.ofNullable(toolsByName.get(name));
    }

    public int getToolCount() {
        return toolsByName.size();
    }
}
