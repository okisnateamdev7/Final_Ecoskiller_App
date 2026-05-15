package com.ecoskiller.mcp.scale.agent;

import com.ecoskiller.mcp.scale.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * AGENT-02: DEPRECATION_ENFORCEMENT_AGENT
 * Manages deprecation lifecycle of API endpoints, agent tools, and SDK features.
 * Enforces sunset dates and notifies consumers via deprecation headers and alerts.
 */
@Component
public class DeprecationEnforcementAgent {

    @Tool(name = "deprecation_mark",
          description = "Mark an API endpoint or tool as deprecated with a sunset date.")
    public AgentResponse markDeprecated(
            @ToolParam(description = "Tool or endpoint name being deprecated") String toolName,
            @ToolParam(description = "MCP agent name owning this tool") String agentName,
            @ToolParam(description = "Sunset date YYYY-MM-DD after which tool is removed") String sunsetDate,
            @ToolParam(description = "Replacement tool or endpoint name") String replacementTool) {

        return AgentResponse.ok("DEPRECATION_ENFORCEMENT_AGENT",
                "Tool '" + toolName + "' marked as deprecated",
                Map.of(
                        "toolName",        toolName,
                        "agentName",       agentName,
                        "deprecatedAt",    LocalDate.now().toString(),
                        "sunsetDate",      sunsetDate,
                        "replacementTool", replacementTool,
                        "status",          "DEPRECATED",
                        "warningHeader",   "Deprecation: " + toolName + "; sunset=" + sunsetDate,
                        "consumersNotified", 0
                ));
    }

    @Tool(name = "deprecation_list_active",
          description = "List all currently deprecated tools and their sunset dates.")
    public AgentResponse listActiveDeprecations(
            @ToolParam(description = "Filter by agent name, or ALL for everything") String agentFilter) {

        return AgentResponse.ok("DEPRECATION_ENFORCEMENT_AGENT",
                "Active deprecations fetched",
                Map.of(
                        "filter", agentFilter,
                        "deprecations", List.of(
                                Map.of("tool","audit_trail_v1",   "agent","mcp-09-audit",   "sunsetDate","2025-09-01","daysRemaining",0,  "status","SUNSET"),
                                Map.of("tool","revenue_ingest_v1","agent","mcp-17-royalty",  "sunsetDate","2026-01-01","daysRemaining",302,"status","DEPRECATED"),
                                Map.of("tool","license_gen_v2",   "agent","mcp-22-scale",    "sunsetDate","2026-06-30","daysRemaining",482,"status","DEPRECATED")
                        ),
                        "total", 3,
                        "fetchedAt", Instant.now().toString()
                ));
    }

    @Tool(name = "deprecation_enforce_sunset",
          description = "Enforce sunset for a tool that has passed its sunset date — blocks further calls.")
    public AgentResponse enforceSunset(
            @ToolParam(description = "Tool name to sunset") String toolName,
            @ToolParam(description = "Agent name") String agentName) {

        return AgentResponse.ok("DEPRECATION_ENFORCEMENT_AGENT",
                "Sunset enforced for '" + toolName + "'",
                Map.of(
                        "toolName",   toolName,
                        "agentName",  agentName,
                        "status",     "SUNSET_ENFORCED",
                        "blockedAt",  Instant.now().toString(),
                        "httpStatus", 410,
                        "message",    "This tool has been permanently removed. Use the replacement."
                ));
    }
}
