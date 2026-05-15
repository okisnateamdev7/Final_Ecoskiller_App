package com.ecoskiller.mcp.phone.config;

import com.ecoskiller.mcp.phone.agent.*;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers all 21 CAT-23a agents as MCP tools.
 */
@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider phoneToolCallbacks(
            CallSessionMappingAgent               callSessionMappingAgent,
            PhoneLateJoinPolicyAgent              phoneLateJoinPolicyAgent,
            PhonePartialSessionRecoveryAgent      phonePartialSessionRecoveryAgent,
            PhoneCrossSessionBehaviorAgent        phoneCrossSessionBehaviorAgent,
            SessionPinGenerationAgent             sessionPinGenerationAgent,
            PhoneRaceConditionGuardAgent          phoneRaceConditionGuardAgent,
            PhoneJoinIdempotencyAgent             phoneJoinIdempotencyAgent,
            PhoneEventDeduplicationAgent          phoneEventDeduplicationAgent,
            PhoneAppendOnlyEnforcementAgent       phoneAppendOnlyEnforcementAgent,
            PhoneScopeFreezeAgent                 phoneScopeFreezeAgent,
            PhoneSessionStateSyncAgent            phoneSessionStateSyncAgent,
            CrossRegionFailbackReconciliationAgent crossRegionFailbackReconciliationAgent,
            PstnBridgeControlAgent                pstnBridgeControlAgent,
            SipHealthMonitorAgent                 sipHealthMonitorAgent,
            SipRouteCostOptimizationAgent         sipRouteCostOptimizationAgent,
            MultiRegionBridgeRoutingAgent         multiRegionBridgeRoutingAgent,
            CallFailoverAgent                     callFailoverAgent,
            PhoneCallLoopDetectionAgent           phoneCallLoopDetectionAgent,
            PhonePinAuthAgent                     phonePinAuthAgent,
            PhoneEntryValidationAgent             phoneEntryValidationAgent,
            PhoneExpiryEnforcementAgent           phoneExpiryEnforcementAgent) {

        return MethodToolCallbackProvider.builder()
                .toolObjects(
                        callSessionMappingAgent,
                        phoneLateJoinPolicyAgent,
                        phonePartialSessionRecoveryAgent,
                        phoneCrossSessionBehaviorAgent,
                        sessionPinGenerationAgent,
                        phoneRaceConditionGuardAgent,
                        phoneJoinIdempotencyAgent,
                        phoneEventDeduplicationAgent,
                        phoneAppendOnlyEnforcementAgent,
                        phoneScopeFreezeAgent,
                        phoneSessionStateSyncAgent,
                        crossRegionFailbackReconciliationAgent,
                        pstnBridgeControlAgent,
                        sipHealthMonitorAgent,
                        sipRouteCostOptimizationAgent,
                        multiRegionBridgeRoutingAgent,
                        callFailoverAgent,
                        phoneCallLoopDetectionAgent,
                        phonePinAuthAgent,
                        phoneEntryValidationAgent,
                        phoneExpiryEnforcementAgent
                )
                .build();
    }
}
