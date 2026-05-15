package com.ecoskiller.mcp.stability.config;

import com.ecoskiller.mcp.stability.agent.*;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers all 10 CAT-19 agents as MCP tools.
 */
@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider stabilityToolCallbacks(
            AgentHealthWatchdogAgent            agentHealthWatchdogAgent,
            EmergencyPlatformLockdownAgent      emergencyPlatformLockdownAgent,
            InsiderThreatMonitorAgent           insiderThreatMonitorAgent,
            InstituteTimetableOptimizationAgent instituteTimetableOptimizationAgent,
            IntegrationHealthMonitorAgent       integrationHealthMonitorAgent,
            ModelExplainabilityDiffAgent        modelExplainabilityDiffAgent,
            PolicyVersionControlAgent           policyVersionControlAgent,
            RegulatoryComplianceMappingAgent    regulatoryComplianceMappingAgent,
            ResourceConsumptionGuardAgent       resourceConsumptionGuardAgent,
            RevenueShareReconciliationAgent     revenueShareReconciliationAgent) {

        return MethodToolCallbackProvider.builder()
                .toolObjects(
                        agentHealthWatchdogAgent,
                        emergencyPlatformLockdownAgent,
                        insiderThreatMonitorAgent,
                        instituteTimetableOptimizationAgent,
                        integrationHealthMonitorAgent,
                        modelExplainabilityDiffAgent,
                        policyVersionControlAgent,
                        regulatoryComplianceMappingAgent,
                        resourceConsumptionGuardAgent,
                        revenueShareReconciliationAgent
                )
                .build();
    }
}
