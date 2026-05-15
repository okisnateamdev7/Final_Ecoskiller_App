package com.ecoskiller.mcp.aigovernance;

import com.ecoskiller.mcp.aigovernance.server.AgentRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class Mcp04AiGovernanceApplicationTests {

    @Autowired AgentRegistry registry;
    @Autowired ObjectMapper objectMapper;

    @Test
    void contextLoads() {
        assertThat(registry).isNotNull();
    }

    @Test
    void allTwelveAgentsRegistered() {
        assertThat(registry.getAgentCount()).isEqualTo(12);
    }

    @Test
    void minimumToolCount() {
        assertThat(registry.getToolCount()).isGreaterThanOrEqualTo(36);
        System.out.println("Total tools: " + registry.getToolCount());
    }

    @Test
    void allExpectedAgentNamesPresent() {
        var names = registry.getAllAgents().stream().map(a -> a.getName()).toList();
        assertThat(names).contains(
            "AI_PERMISSION_FIREWALL_AGENT",
            "BEHAVIOR_ANALYTICS_AGENT",
            "COMMUNITY_HEALTH_AGENT",
            "ETHICS_AGENT",
            "HUMAN_AI_INTERFACE_AGENT",
            "INCENTIVE_AGENT",
            "ML_REHYDRATION_AGENT",
            "ML_ROUTING_AGENT",
            "MODEL_RISK_AGENT",
            "POWER_BALANCE_AGENT",
            "REPUTATION_AGENT",
            "STRATEGIC_SIMULATION_AGENT"
        );
    }

    @Test
    void firewallEvaluateToolWorks() {
        var agent = registry.findAgentForTool("firewall_evaluate_request");
        assertThat(agent).isPresent();

        ObjectNode args = objectMapper.createObjectNode();
        args.put("tenant_id", "test-tenant");
        args.put("user_id", "user-001");
        args.put("model_id", "claude-3");
        args.put("feature", "inference");
        args.put("data_scope", "INTERNAL");

        var result = agent.get().executeTool("firewall_evaluate_request", args);
        assertThat(result.getIsError()).isFalse();
        assertThat(result.getContent().get(0).getText()).contains("ALLOW");
        System.out.println(result.getContent().get(0).getText());
    }

    @Test
    void ethicsBiasAuditWorks() {
        var agent = registry.findAgentForTool("ethics_bias_audit");
        assertThat(agent).isPresent();

        ObjectNode args = objectMapper.createObjectNode();
        args.put("tenant_id", "test-tenant");
        args.put("model_id", "skill-rank-v3");
        args.put("protected_attributes", "gender,age,region");
        args.put("metric", "demographic_parity");

        var result = agent.get().executeTool("ethics_bias_audit", args);
        assertThat(result.getIsError()).isFalse();
        System.out.println(result.getContent().get(0).getText());
    }

    @Test
    void simulationRunWorks() {
        var agent = registry.findAgentForTool("simulation_run");
        assertThat(agent).isPresent();

        ObjectNode args = objectMapper.createObjectNode();
        args.put("tenant_id", "test-tenant");
        args.put("simulation_type", "USER_GROWTH");
        args.put("time_horizon_days", 90);
        args.put("monte_carlo_runs", 500);

        var result = agent.get().executeTool("simulation_run", args);
        assertThat(result.getIsError()).isFalse();
        System.out.println(result.getContent().get(0).getText());
    }

    @Test
    void unknownToolReturnsEmpty() {
        assertThat(registry.findAgentForTool("nonexistent_tool")).isEmpty();
    }
}
