package com.ecoskiller.mcp.tests;

import com.ecoskiller.mcp.agents.GenericAgent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AgentTestSuite {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        System.out.println("🧪 Testing CAT-18 Complete Server (50+ Agents)...\n");
        
        testSampleAgents();
    }

    private static void testSampleAgents() {
        int passed = 0;
        int failed = 0;

        String[] agentNames = {
            "CLICKHOUSE_METRIC_NORMALIZATION",
            "ERP_GO_REPORT_INTEGRATION",
            "PHONE_FEATURE_VECTOR_EMISSION",
            "MULTI_ENV_CONFIG",
            "PHONE_BACKUP_RESTORE",
            "USER_REGISTRATION",
            "KYC_VERIFICATION",
            "SCORE_BIAS_AUDIT",
            "PHONE_TENANT_BOUNDARY",
            "CALL_COST_CALC",
            "SPATIAL_PATTERN_MODEL",
            "OPEN_RESPONSE_EVALUATION",
            "CREATIVITY_DIVERGENCE",
            "INTELLIGENCE_REPORT"
        };

        System.out.println("Testing sample agents from each category:\n");

        for (int i = 0; i < agentNames.length; i++) {
            String agentName = agentNames[i];
            GenericAgent agent = new GenericAgent(agentName, "Test agent");
            String testName = String.format("[%d/14] %s", i + 1, agentName);
            
            try {
                ObjectNode testArgs = JsonNodeFactory.instance.objectNode();
                testArgs.put("test_param", "test_value");
                
                JsonNode result = agent.execute(agentName, testArgs);
                
                if (result != null && result.has("status")) {
                    System.out.println("✅ " + testName);
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

        System.out.println("\n" + "=".repeat(70));
        System.out.println("Sample Test Results: " + passed + " passed, " + failed + " failed");
        System.out.println("Total Agents in CAT-18 Complete: 50+");
        System.out.println("=".repeat(70));
    }
}
