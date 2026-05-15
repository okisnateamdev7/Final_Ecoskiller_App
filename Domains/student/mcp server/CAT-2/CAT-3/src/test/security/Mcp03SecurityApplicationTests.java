package com.ecoskiller.mcp.security;

import com.ecoskiller.mcp.security.model.McpRequest;
import com.ecoskiller.mcp.security.model.McpResponse;
import com.ecoskiller.mcp.security.server.AgentRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class Mcp03SecurityApplicationTests {

    @Autowired
    AgentRegistry registry;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void contextLoads() {
        assertThat(registry).isNotNull();
    }

    @Test
    void allTwelveAgentsRegistered() {
        assertThat(registry.getAgentCount()).isEqualTo(12);
    }

    @Test
    void toolsAreRegistered() {
        assertThat(registry.getToolCount()).isGreaterThanOrEqualTo(12);
        System.out.println("Total tools registered: " + registry.getToolCount());
    }

    @Test
    void allExpectedAgentNamesPresent() {
        var names = registry.getAllAgents().stream()
                .map(a -> a.getName())
                .toList();

        assertThat(names).contains(
            "API_ECONOMY_AGENT",
            "AUTH_MIGRATION_AGENT",
            "CONSENT_MIGRATION_AGENT",
            "ENTERPRISE_INTEGRATION_AGENT",
            "ENV_PROMOTION_AGENT",
            "I18N_AGENT",
            "KEY_MANAGEMENT_AGENT",
            "REGION_MIGRATION_AGENT",
            "STORAGE_CONNECT_AGENT",
            "TENANT_CLONE_AGENT",
            "WEBHOOK_MARKETPLACE_AGENT",
            "ZERO_DOWNTIME_MIGRATION_AGENT"
        );
    }

    @Test
    void keyGenerateToolExecutes() {
        var agent = registry.findAgentForTool("key_generate");
        assertThat(agent).isPresent();

        ObjectNode args = objectMapper.createObjectNode();
        args.put("tenant_id", "test-tenant-001");
        args.put("key_type", "RSA-2048");
        args.put("purpose", "signing");

        var result = agent.get().executeTool("key_generate", args);
        assertThat(result.getIsError()).isFalse();
        assertThat(result.getContent()).isNotEmpty();
        System.out.println("key_generate result: " + result.getContent().get(0).getText());
    }

    @Test
    void zdmPlanToolExecutes() {
        var agent = registry.findAgentForTool("zdm_plan");
        assertThat(agent).isPresent();

        ObjectNode args = objectMapper.createObjectNode();
        args.put("tenant_id", "tenant-xyz");
        args.put("migration_type", "DB_SCHEMA");
        args.put("strategy", "BLUE_GREEN");

        var result = agent.get().executeTool("zdm_plan", args);
        assertThat(result.getIsError()).isFalse();
        System.out.println("zdm_plan result: " + result.getContent().get(0).getText());
    }

    @Test
    void unknownToolReturnsError() {
        var agent = registry.findAgentForTool("non_existent_tool");
        assertThat(agent).isEmpty();
    }
}
