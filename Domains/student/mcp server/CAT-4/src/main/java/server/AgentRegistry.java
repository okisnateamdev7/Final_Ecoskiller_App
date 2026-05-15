package com.ecoskiller.mcp.aigovernance.server;

import com.ecoskiller.mcp.aigovernance.agents.McpAgent;
import com.ecoskiller.mcp.aigovernance.model.McpTool;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Auto-discovers all CAT-04 agents and indexes their tools.
 */
@Slf4j
@Service
public class AgentRegistry {

    private final List<McpAgent> agents;
    private final Map<String, McpAgent> toolIndex = new HashMap<>();

    public AgentRegistry(List<McpAgent> agents) {
        this.agents = agents;
    }

    @PostConstruct
    public void init() {
        for (McpAgent agent : agents) {
            for (McpTool tool : agent.getTools()) {
                toolIndex.put(tool.getName(), agent);
            }
            log.info("Registered agent: {} | tools: {}", agent.getName(), agent.getTools().size());
        }
        log.info("MCP-04 AI Governance ready — {} agents, {} tools", agents.size(), toolIndex.size());
    }

    public List<McpAgent> getAllAgents()        { return agents; }
    public List<McpTool> getAllTools()           { return agents.stream().flatMap(a -> a.getTools().stream()).toList(); }
    public Optional<McpAgent> findAgentForTool(String toolName) { return Optional.ofNullable(toolIndex.get(toolName)); }
    public int getAgentCount()                  { return agents.size(); }
    public int getToolCount()                   { return toolIndex.size(); }
}
