package com.ecoskiller.mcp.economics.tools;

import com.ecoskiller.mcp.economics.agents.AgentTool;
import com.ecoskiller.mcp.economics.model.McpProtocol.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        log.info("║  mcp-08-economics — ToolRegistry         ║");
        log.info("╠══════════════════════════════════════════╣");
        toolsByName.keySet().stream().sorted()
                .forEach(n -> log.info("║  ✓ {}", String.format("%-40s", n) + "║"));
        log.info("╠══════════════════════════════════════════╣");
        log.info("║  Tools registered: {}                    ║", toolsByName.size());
        log.info("╚══════════════════════════════════════════╝");
    }

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

    public ToolCallResult callTool(String name, Map<String, Object> arguments) {
        AgentTool tool = toolsByName.get(name);
        if (tool == null) {
            log.warn("Tool not found: {}", name);
            return ToolCallResult.builder().isError(true)
                    .content(List.of(ContentBlock.builder().type("text")
                            .text("ERROR: Tool '" + name + "' not found. Available: " +
                                  toolsByName.keySet().stream().sorted().toList())
                            .build()))
                    .build();
        }
        return tool.execute(arguments);
    }

    public Optional<AgentTool> findTool(String name) { return Optional.ofNullable(toolsByName.get(name)); }
    public int getToolCount() { return toolsByName.size(); }
}
