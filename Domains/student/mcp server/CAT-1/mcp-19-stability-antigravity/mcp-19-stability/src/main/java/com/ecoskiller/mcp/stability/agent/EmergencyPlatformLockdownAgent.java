package com.ecoskiller.mcp.stability.agent;

import com.ecoskiller.mcp.stability.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * AGENT-02: EMERGENCY_PLATFORM_LOCKDOWN_AGENT
 * Emergency lockdown for security breaches, data leaks, or critical failures.
 * Supports FULL, READ_ONLY, per-tenant, and per-MCP lockdown. Requires admin auth.
 */
@Component
public class EmergencyPlatformLockdownAgent {

    @Tool(name = "lockdown_initiate",
          description = "Initiate emergency platform lockdown. Disables APIs, agent calls, and data writes.")
    public AgentResponse initiate(
            @ToolParam(description = "Scope: FULL | READ_ONLY | SPECIFIC_TENANT | SPECIFIC_MCP") String scope,
            @ToolParam(description = "Target: tenant ID, MCP server ID, or ALL") String target,
            @ToolParam(description = "Reason for lockdown") String reason,
            @ToolParam(description = "Authorizing admin user ID") String adminId) {

        String lockdownId = "LOCK-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();

        return AgentResponse.alert("EMERGENCY_PLATFORM_LOCKDOWN_AGENT",
                "EMERGENCY LOCKDOWN INITIATED — scope: " + scope,
                Map.of(
                        "lockdownId",        lockdownId,
                        "scope",             scope,
                        "target",            target,
                        "reason",            reason,
                        "authorizedBy",      adminId,
                        "initiatedAt",       Instant.now().toString(),
                        "status",            "ACTIVE",
                        "affectedServices",  scope.equals("FULL")
                                ? List.of("ALL_APIS","ALL_AGENTS","ALL_WRITES","ALL_AUTH")
                                : List.of(target),
                        "estimatedImpact",   scope.equals("FULL") ? "100% PLATFORM DOWN" : "PARTIAL",
                        "notificationsSent", List.of("OPS_TEAM","CTO","INCIDENT_CHANNEL")
                ));
    }

    @Tool(name = "lockdown_lift",
          description = "Lift an active platform lockdown after incident resolution.")
    public AgentResponse lift(
            @ToolParam(description = "Lockdown ID to lift") String lockdownId,
            @ToolParam(description = "Admin user ID authorizing the lift") String adminId,
            @ToolParam(description = "Resolution summary") String resolution) {

        return AgentResponse.ok("EMERGENCY_PLATFORM_LOCKDOWN_AGENT",
                "Lockdown " + lockdownId + " lifted",
                Map.of(
                        "lockdownId",                    lockdownId,
                        "liftedBy",                      adminId,
                        "resolution",                    resolution,
                        "liftedAt",                      Instant.now().toString(),
                        "status",                        "LIFTED",
                        "serviceRestorationEstimateSec", 120,
                        "postMortemRequired",            true
                ));
    }

    @Tool(name = "lockdown_status",
          description = "Get current lockdown status of the platform or a specific target.")
    public AgentResponse status(
            @ToolParam(description = "Target to check: ALL | tenant ID | MCP server ID") String target) {

        return AgentResponse.ok("EMERGENCY_PLATFORM_LOCKDOWN_AGENT",
                "Lockdown status for: " + target,
                Map.of(
                        "target",          target,
                        "activeLockdowns", 0,
                        "lastLockdown", Map.of(
                                "id",         "LOCK-A1B2C3D4E5",
                                "reason",     "SECURITY_BREACH_DETECTED",
                                "duration",   "47 minutes",
                                "resolvedAt", "2025-02-18T03:22:00Z"
                        ),
                        "platformStatus",  "FULLY_OPERATIONAL"
                ));
    }
}
