package com.ecoskiller.mcp.stability.agent;

import com.ecoskiller.mcp.stability.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * AGENT-09: RESOURCE_CONSUMPTION_GUARD_AGENT
 * Guards against runaway resource usage: CPU, memory, DB connections,
 * API quota, and storage per tenant and agent.
 */
@Component
public class ResourceConsumptionGuardAgent {

    @Tool(name = "resource_usage_check",
          description = "Check current resource consumption for a tenant, agent, or MCP server.")
    public AgentResponse usageCheck(
            @ToolParam(description = "Entity ID: tenant ID or agent name") String entityId,
            @ToolParam(description = "Entity type: TENANT | AGENT | MCP_SERVER") String entityType,
            @ToolParam(description = "Resource: CPU | MEMORY | DB_CONN | STORAGE | API_QUOTA | ALL") String resource) {

        return AgentResponse.ok("RESOURCE_CONSUMPTION_GUARD_AGENT",
                "Resource usage check for " + entityId,
                Map.of(
                        "entityId",   entityId,
                        "entityType", entityType,
                        "checkedAt",  Instant.now().toString(),
                        "resources", Map.of(
                                "CPU",        Map.of("usedPct",34.2,"limit",80,"status","OK"),
                                "MEMORY",     Map.of("usedPct",61.5,"limit",85,"status","OK"),
                                "DB_CONN",    Map.of("active", 42,  "limit",100,"status","OK"),
                                "STORAGE_GB", Map.of("usedGB", 128, "limitGB",500,"status","OK"),
                                "API_CALLS",  Map.of("today",12840,"dailyLimit",50000,"status","OK")
                        ),
                        "overallStatus", "WITHIN_LIMITS",
                        "alerts",        List.of()
                ));
    }

    @Tool(name = "resource_limit_set",
          description = "Set resource consumption limit for a tenant with breach action.")
    public AgentResponse setLimit(
            @ToolParam(description = "Tenant ID") String tenantId,
            @ToolParam(description = "Resource type: CPU | MEMORY | STORAGE | API_CALLS") String resource,
            @ToolParam(description = "Limit value (percent or count)") double limit,
            @ToolParam(description = "Action on breach: THROTTLE | ALERT | KILL") String breachAction) {

        return AgentResponse.ok("RESOURCE_CONSUMPTION_GUARD_AGENT",
                "Limit set for " + resource + " on tenant " + tenantId,
                Map.of(
                        "tenantId",     tenantId,
                        "resource",     resource,
                        "limit",        limit,
                        "breachAction", breachAction,
                        "configId",     "RCG-" + System.currentTimeMillis(),
                        "status",       "ACTIVE"
                ));
    }

    @Tool(name = "resource_breach_history",
          description = "Get history of resource limit breaches for a tenant.")
    public AgentResponse breachHistory(
            @ToolParam(description = "Tenant ID") String tenantId,
            @ToolParam(description = "Period: TODAY | THIS_WEEK | THIS_MONTH") String period) {

        return AgentResponse.ok("RESOURCE_CONSUMPTION_GUARD_AGENT",
                "Breach history for tenant " + tenantId,
                Map.of(
                        "tenantId", tenantId,
                        "period",   period,
                        "totalBreaches", 2,
                        "breaches", List.of(
                                Map.of("resource","MEMORY",   "peakPct",92,    "at","2025-02-20T14:30:00Z","action","THROTTLED","durationMin",8),
                                Map.of("resource","API_CALLS","peakCount",52000,"at","2025-02-14T11:00:00Z","action","ALERTED",  "durationMin",25)
                        )
                ));
    }
}
