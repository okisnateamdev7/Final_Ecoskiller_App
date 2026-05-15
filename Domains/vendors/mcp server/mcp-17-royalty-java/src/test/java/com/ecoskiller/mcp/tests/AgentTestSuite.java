package com.ecoskiller.mcp.tests;

import com.ecoskiller.mcp.agents.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test suite for all 18 CAT-17 agents
 */
public class AgentTestSuite {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        System.out.println("🧪 Testing CAT-17 Royalty Agents...\n");
        
        testAllAgents();
    }

    private static void testAllAgents() {
        int passed = 0;
        int failed = 0;

        // Agent instances
        Agent[] agents = {
            new TaxComplianceAgent(),
            new SchoolAutoCreationAgent(),
            new RoyaltyWalletAgent(),
            new RoyaltySystemUnifiedAgent(),
            new RoyaltyEscrowAgent(),
            new RoyaltyDistributionAgent(),
            new RoyaltyCalculationAgent(),
            new RevenueIngestionAgent(),
            new ParentDashboardAgent(),
            new MarketDemandPredictionAgent(),
            new LicenseGenerationAgent(),
            new IdeaVisibilityControlAgent(),
            new IdeaEvaluationAgent(),
            new DataRetentionPolicyAgent(),
            new ContractLifecycleAgent(),
            new CompetitionEngineAgent(),
            new BusinessMatchingAgent(),
            new AuditVerificationAgent()
        };

        for (int i = 0; i < agents.length; i++) {
            Agent agent = agents[i];
            String testName = String.format("[%d/18] %s", i + 1, agent.getAgentName());
            
            try {
                ObjectNode testArgs = JsonNodeFactory.instance.objectNode();
                testArgs.put("test", "true");
                
                JsonNode result = agent.execute(agent.getAgentName(), testArgs);
                
                if (result != null && result.has("status")) {
                    System.out.println("✅ " + testName);
                    System.out.println("   └─ " + agent.getDescription());
                    passed++;
                } else {
                    System.out.println("❌ " + testName + " - Invalid response");
                    failed++;
                }
            } catch (Exception e) {
                System.out.println("❌ " + testName + " - " + e.getMessage());
                failed++;
            }
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("Test Results: " + passed + " passed, " + failed + " failed");
        System.out.println("=".repeat(60));
    }
}
