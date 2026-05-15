package com.ecoskiller.mcp.governance.config;

import com.ecoskiller.mcp.governance.agents.*;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP Server Configuration for mcp-02-governance
 *
 * Registers all 12 CAT-02 agents as MCP tools.
 */
@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider auditComplianceTools(AuditComplianceAgent agent) {
        return MethodToolCallbackProvider.builder().toolObjects(agent).build();
    }

    @Bean
    public ToolCallbackProvider backupDrTools(BackupDrAgent agent) {
        return MethodToolCallbackProvider.builder().toolObjects(agent).build();
    }

    @Bean
    public ToolCallbackProvider billingGovernanceTools(BillingGovernanceAgent agent) {
        return MethodToolCallbackProvider.builder().toolObjects(agent).build();
    }

    @Bean
    public ToolCallbackProvider dataGovernanceTools(DataGovernanceAgent agent) {
        return MethodToolCallbackProvider.builder().toolObjects(agent).build();
    }

    @Bean
    public ToolCallbackProvider devsecopsTools(DevsecopsAgent agent) {
        return MethodToolCallbackProvider.builder().toolObjects(agent).build();
    }

    @Bean
    public ToolCallbackProvider forensicsTools(ForensicsAgent agent) {
        return MethodToolCallbackProvider.builder().toolObjects(agent).build();
    }

    @Bean
    public ToolCallbackProvider geoComplianceTools(GeoComplianceAgent agent) {
        return MethodToolCallbackProvider.builder().toolObjects(agent).build();
    }

    @Bean
    public ToolCallbackProvider legalPolicyTools(LegalPolicyAgent agent) {
        return MethodToolCallbackProvider.builder().toolObjects(agent).build();
    }

    @Bean
    public ToolCallbackProvider moderationTools(ModerationAgent agent) {
        return MethodToolCallbackProvider.builder().toolObjects(agent).build();
    }

    @Bean
    public ToolCallbackProvider policyEvolutionTools(PolicyEvolutionAgent agent) {
        return MethodToolCallbackProvider.builder().toolObjects(agent).build();
    }

    @Bean
    public ToolCallbackProvider tenancyGovernanceTools(TenancyGovernanceAgent agent) {
        return MethodToolCallbackProvider.builder().toolObjects(agent).build();
    }

    @Bean
    public ToolCallbackProvider zeroTrustTools(ZeroTrustAgent agent) {
        return MethodToolCallbackProvider.builder().toolObjects(agent).build();
    }
}
