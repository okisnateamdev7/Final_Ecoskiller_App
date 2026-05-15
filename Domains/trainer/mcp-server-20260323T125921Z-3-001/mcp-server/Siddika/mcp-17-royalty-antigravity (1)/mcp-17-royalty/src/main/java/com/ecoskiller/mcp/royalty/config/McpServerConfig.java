package com.ecoskiller.mcp.royalty.config;

import com.ecoskiller.mcp.royalty.agent.*;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers all 18 CAT-17 agents as MCP tools.
 */
@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider royaltyToolCallbacks(
            TaxComplianceAgent           taxComplianceAgent,
            SchoolAutoCreationAgent      schoolAutoCreationAgent,
            RoyaltyWalletAgent           royaltyWalletAgent,
            RoyaltySystemUnifiedAgent    royaltySystemUnifiedAgent,
            RoyaltyEscrowAgent           royaltyEscrowAgent,
            RoyaltyDistributionAgent     royaltyDistributionAgent,
            RoyaltyCalculationAgent      royaltyCalculationAgent,
            RevenueIngestionAgent        revenueIngestionAgent,
            ParentDashboardAgent         parentDashboardAgent,
            MarketDemandPredictionAgent  marketDemandPredictionAgent,
            LicenseGenerationAgent       licenseGenerationAgent,
            IdeaVisibilityControlAgent   ideaVisibilityControlAgent,
            IdeaEvaluationAgent          ideaEvaluationAgent,
            DataRetentionPolicyAgent     dataRetentionPolicyAgent,
            ContractLifecycleAgent       contractLifecycleAgent,
            CompetitionEngineAgent       competitionEngineAgent,
            BusinessMatchingAgent        businessMatchingAgent,
            AuditVerificationAgent       auditVerificationAgent) {

        return MethodToolCallbackProvider.builder()
                .toolObjects(
                        taxComplianceAgent,
                        schoolAutoCreationAgent,
                        royaltyWalletAgent,
                        royaltySystemUnifiedAgent,
                        royaltyEscrowAgent,
                        royaltyDistributionAgent,
                        royaltyCalculationAgent,
                        revenueIngestionAgent,
                        parentDashboardAgent,
                        marketDemandPredictionAgent,
                        licenseGenerationAgent,
                        ideaVisibilityControlAgent,
                        ideaEvaluationAgent,
                        dataRetentionPolicyAgent,
                        contractLifecycleAgent,
                        competitionEngineAgent,
                        businessMatchingAgent,
                        auditVerificationAgent
                )
                .build();
    }
}
