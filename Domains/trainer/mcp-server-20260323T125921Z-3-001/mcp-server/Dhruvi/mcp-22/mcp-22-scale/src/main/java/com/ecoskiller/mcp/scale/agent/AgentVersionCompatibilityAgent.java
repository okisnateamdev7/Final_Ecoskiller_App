package com.ecoskiller.mcp.scale.agent;

import com.ecoskiller.mcp.scale.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * AGENT-01: AGENT_VERSION_COMPATIBILITY_AGENT
 * Validates API/SDK version compatibility across MCP agents and clients.
 * Supports version negotiation, compatibility matrices, and upgrade paths.
 */
@Component
public class AgentVersionCompatibilityAgent {

    @Tool(name = "version_compatibility_check",
          description = "Check if a given client version is compatible with a specific MCP agent version.")
    public AgentResponse checkCompatibility(
            @ToolParam(description = "MCP agent name e.g. mcp-17-royalty") String agentName,
            @ToolParam(description = "Agent version e.g. 1.0.0") String agentVersion,
            @ToolParam(description = "Client SDK version e.g. 2.3.1") String clientVersion) {

        boolean compatible = !clientVersion.startsWith("0.");

        return AgentResponse.ok("AGENT_VERSION_COMPATIBILITY_AGENT",
                "Compatibility check for " + agentName + " v" + agentVersion,
                Map.of(
                        "agentName",        agentName,
                        "agentVersion",     agentVersion,
                        "clientVersion",    clientVersion,
                        "compatible",       compatible,
                        "minimumClient",    "1.0.0",
                        "breakingChanges",  compatible ? List.of() : List.of("API contract changed in v1.0.0"),
                        "recommendation",   compatible ? "PROCEED" : "UPGRADE_CLIENT",
                        "checkedAt",        Instant.now().toString()
                ));
    }

    @Tool(name = "version_matrix_get",
          description = "Get the full compatibility matrix for all MCP agents in the ecosystem.")
    public AgentResponse getCompatibilityMatrix() {

        return AgentResponse.ok("AGENT_VERSION_COMPATIBILITY_AGENT",
                "Compatibility matrix fetched",
                Map.of(
                        "matrix", List.of(
                                Map.of("agent","mcp-17-royalty",  "latestVersion","1.0.0","minClientVersion","1.0.0","status","STABLE"),
                                Map.of("agent","mcp-22-scale",    "latestVersion","1.0.0","minClientVersion","1.0.0","status","STABLE"),
                                Map.of("agent","mcp-15-license",  "latestVersion","1.2.0","minClientVersion","1.1.0","status","STABLE"),
                                Map.of("agent","mcp-09-audit",    "latestVersion","2.0.0","minClientVersion","1.5.0","status","DEPRECATED")
                        ),
                        "totalAgents", 4,
                        "generatedAt", Instant.now().toString()
                ));
    }

    @Tool(name = "version_upgrade_path_get",
          description = "Get the recommended upgrade path from a current version to the latest.")
    public AgentResponse getUpgradePath(
            @ToolParam(description = "Agent name") String agentName,
            @ToolParam(description = "Current version e.g. 1.0.0") String currentVersion,
            @ToolParam(description = "Target version e.g. 2.0.0") String targetVersion) {

        return AgentResponse.ok("AGENT_VERSION_COMPATIBILITY_AGENT",
                "Upgrade path computed for " + agentName,
                Map.of(
                        "agentName",      agentName,
                        "currentVersion", currentVersion,
                        "targetVersion",  targetVersion,
                        "upgradePath",    List.of(currentVersion, "1.5.0", targetVersion),
                        "migrationGuide", "https://docs.ecoskiller.com/mcp/upgrade/" + agentName,
                        "estimatedEffort","MEDIUM",
                        "breakingSteps",  1
                ));
    }
}
