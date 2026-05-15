package com.ecoskiller.mcp.scale.config;

import com.ecoskiller.mcp.scale.agent.*;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers all 10 CAT-22 agents as MCP tools.
 */
@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider scaleToolCallbacks(
            AgentVersionCompatibilityAgent   agentVersionCompatibilityAgent,
            DeprecationEnforcementAgent      deprecationEnforcementAgent,
            GlobalTimeEventNormalizationAgent globalTimeEventNormalizationAgent,
            PublicEventRiskAssessmentAgent   publicEventRiskAssessmentAgent,
            PublicTransparencyLogAgent       publicTransparencyLogAgent,
            RightToErasureAgent              rightToErasureAgent,
            RoyaltyDisputeResolutionAgent    royaltyDisputeResolutionAgent,
            SchoolGrowthForecastAgent        schoolGrowthForecastAgent,
            UnitEconomicsEngineAgent         unitEconomicsEngineAgent,
            CrossAgentDependencyResolutionAgent crossAgentDependencyResolutionAgent) {

        return MethodToolCallbackProvider.builder()
                .toolObjects(
                        agentVersionCompatibilityAgent,
                        deprecationEnforcementAgent,
                        globalTimeEventNormalizationAgent,
                        publicEventRiskAssessmentAgent,
                        publicTransparencyLogAgent,
                        rightToErasureAgent,
                        royaltyDisputeResolutionAgent,
                        schoolGrowthForecastAgent,
                        unitEconomicsEngineAgent,
                        crossAgentDependencyResolutionAgent
                )
                .build();
    }
}
