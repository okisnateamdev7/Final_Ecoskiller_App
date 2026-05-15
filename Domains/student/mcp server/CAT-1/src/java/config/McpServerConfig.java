package com.ecoskiller.mcp.platform.config;

import com.ecoskiller.mcp.platform.agents.*;
import org.springframework.ai.mcp.server.McpAsyncServer;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP Server Configuration for mcp-01-platform
 *
 * Registers all 11 CAT-01 agents as MCP tools.
 * Each agent is exposed as a separate ToolCallbackProvider bean
 * so Spring AI MCP auto-discovers them.
 */
@Configuration
public class McpServerConfig {

    // ── Agent 1 ──────────────────────────────────────────────────────────────
    @Bean
    public ToolCallbackProvider antigravityConfigurationTools(
            AntigravityConfigurationAgent agent) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(agent)
                .build();
    }

    // ── Agent 2 ──────────────────────────────────────────────────────────────
    @Bean
    public ToolCallbackProvider antigravityDependencyRiskTools(
            AntigravityDependencyRiskAgent agent) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(agent)
                .build();
    }

    // ── Agent 3 ──────────────────────────────────────────────────────────────
    @Bean
    public ToolCallbackProvider antigravityObservabilityTools(
            AntigravityObservabilityAgent agent) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(agent)
                .build();
    }

    // ── Agent 4 ──────────────────────────────────────────────────────────────
    @Bean
    public ToolCallbackProvider antigravityOrchestrationGovernorTools(
            AntigravityOrchestrationGovernorAgent agent) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(agent)
                .build();
    }

    // ── Agent 5 ──────────────────────────────────────────────────────────────
    @Bean
    public ToolCallbackProvider antigravityPlatformEvaluationTools(
            AntigravityPlatformEvaluationAgent agent) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(agent)
                .build();
    }

    // ── Agent 6 ──────────────────────────────────────────────────────────────
    @Bean
    public ToolCallbackProvider antigravityTechDebtTools(
            AntigravityTechDebtAgent agent) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(agent)
                .build();
    }

    // ── Agent 7 ──────────────────────────────────────────────────────────────
    @Bean
    public ToolCallbackProvider antigravityTruthEngineTools(
            AntigravityTruthEngineAgent agent) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(agent)
                .build();
    }

    // ── Agent 8 ──────────────────────────────────────────────────────────────
    @Bean
    public ToolCallbackProvider ecoskillerAntigravityApiTools(
            EcoskillerAntigravityApiAgent agent) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(agent)
                .build();
    }

    // ── Agent 9 ──────────────────────────────────────────────────────────────
    @Bean
    public ToolCallbackProvider ecoskillerSchemaCompatibilityTools(
            EcoskillerAntigravitySchemaCompatibilityAgent agent) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(agent)
                .build();
    }

    // ── Agent 10 ─────────────────────────────────────────────────────────────
    @Bean
    public ToolCallbackProvider ecoskillerSystemSetupTools(
            EcoskillerAntigravitySystemSetupAgent agent) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(agent)
                .build();
    }

    // ── Agent 11 ─────────────────────────────────────────────────────────────
    @Bean
    public ToolCallbackProvider ecoskillerUiTools(
            EcoskillerAntigravityUiAgent agent) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(agent)
                .build();
    }
}
