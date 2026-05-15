package com.ecoskiller.mcp.security.server;

import com.ecoskiller.mcp.security.agents.McpAgent;
import com.ecoskiller.mcp.security.model.McpTool;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Agent Registry — discovers and indexes all CAT-03 agents and their tools.
 */
@Slf4j
@Service
public class AgentRegistry {

    private final List<McpAgent> agents;

    /** toolName → agent lookup map for fast dispatch */
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
            log.info("Registered agent: {} ({} tools)", agent.getName(), agent.getTools().size());
        }
        log.info("MCP-03 Security Server ready — {} agents, {} tools total",
                agents.size(), toolIndex.size());
    }

    public List<McpAgent> getAllAgents() {
        return agents;
    }

    public List<McpTool> getAllTools() {
        return agents.stream()
                .flatMap(a -> a.getTools().stream())
                .toList();
    }

    public Optional<McpAgent> findAgentForTool(String toolName) {
        return Optional.ofNullable(toolIndex.get(toolName));
    }

    public int getAgentCount()  { return agents.size(); }
    public int getToolCount()   { return toolIndex.size(); }
}
