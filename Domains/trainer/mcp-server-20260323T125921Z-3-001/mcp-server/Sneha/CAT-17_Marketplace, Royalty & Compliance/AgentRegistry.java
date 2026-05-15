package com.ecoskiller.mcp17;

import com.ecoskiller.mcp17.agents.*;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Central registry for all 18 CAT-17 agent tools.
 */
public class AgentRegistry {

    private final Map<String, ToolDefinition> tools = new LinkedHashMap<>();

    public AgentRegistry() {
        register(
            // Core royalty pipeline
            new TaxComplianceAgent(),
            new RoyaltySystemUnifiedAgent(),
            new RoyaltyCalculationAgent(),
            new RevenueIngestionAgent(),
            new RoyaltyWalletAgent(),
            new RoyaltyEscrowAgent(),
            new RoyaltyDistributionAgent(),
            // Marketplace & licensing
            new LicenseGenerationAgent(),
            new IdeaVisibilityControlAgent(),
            new IdeaEvaluationAgent(),
            new BusinessMatchingAgent(),
            new MarketDemandPredictionAgent(),
            new CompetitionEngineAgent(),
            // Compliance & lifecycle
            new DataRetentionPolicyAgent(),
            new AuditVerificationAgent(),
            new ContractLifecycleAgent(),
            // Network ops
            new SchoolAutoCreationAgent(),
            new ParentDashboardAgent()
        );
    }

    private void register(ToolDefinition... defs) {
        for (ToolDefinition d : defs) tools.put(d.getName(), d);
    }

    public ToolDefinition getTool(String name) { return tools.get(name); }
    public Collection<ToolDefinition> getAllTools() { return tools.values(); }
}
