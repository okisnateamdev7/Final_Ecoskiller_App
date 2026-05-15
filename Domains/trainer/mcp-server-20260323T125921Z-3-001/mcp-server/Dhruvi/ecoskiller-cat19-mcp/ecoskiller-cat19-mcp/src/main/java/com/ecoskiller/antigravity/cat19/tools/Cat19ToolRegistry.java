package com.ecoskiller.antigravity.cat19.tools;

import com.ecoskiller.antigravity.cat19.agents.*;
import com.ecoskiller.antigravity.cat19.model.McpModels.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CAT-19 Tool Registry
 * Aggregates tools from all 4 Platform Stability & Risk agents.
 * Routes tool calls to the appropriate agent.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class Cat19ToolRegistry {

    private final PolicyVersionControlAgent      policyVersionControlAgent;
    private final RegulatoryComplianceMappingAgent regulatoryComplianceMappingAgent;
    private final ResourceConsumptionGuardAgent  resourceConsumptionGuardAgent;
    private final RevenueShareReconciliationAgent revenueShareReconciliationAgent;

    /**
     * Returns all tools registered across all 4 CAT-19 agents.
     */
    public List<McpTool> getAllTools() {
        List<McpTool> allTools = new ArrayList<>();
        allTools.addAll(policyVersionControlAgent.getTools());
        allTools.addAll(regulatoryComplianceMappingAgent.getTools());
        allTools.addAll(resourceConsumptionGuardAgent.getTools());
        allTools.addAll(revenueShareReconciliationAgent.getTools());

        log.info("[CAT19-REGISTRY] Total tools registered: {}", allTools.size());
        return allTools;
    }

    /**
     * Route a tool call to the correct agent based on tool name prefix.
     */
    public AgentResult dispatch(String toolName, Map<String, Object> arguments) {
        log.info("[CAT19-REGISTRY] Dispatching tool: {}", toolName);

        if (toolName.startsWith("policy_version_control__")) {
            return policyVersionControlAgent.executeTool(toolName, arguments);

        } else if (toolName.startsWith("regulatory_compliance__")) {
            return regulatoryComplianceMappingAgent.executeTool(toolName, arguments);

        } else if (toolName.startsWith("resource_guard__")) {
            return resourceConsumptionGuardAgent.executeTool(toolName, arguments);

        } else if (toolName.startsWith("revenue_reconciliation__")) {
            return revenueShareReconciliationAgent.executeTool(toolName, arguments);

        } else {
            log.warn("[CAT19-REGISTRY] No agent found for tool: {}", toolName);
            return AgentResult.builder()
                .agentName("CAT19_REGISTRY")
                .status("ERROR")
                .payload(Map.of(
                    "error", "No agent registered for tool: " + toolName,
                    "available_prefixes", List.of(
                        "policy_version_control__",
                        "regulatory_compliance__",
                        "resource_guard__",
                        "revenue_reconciliation__"
                    )
                ))
                .build();
        }
    }
}
