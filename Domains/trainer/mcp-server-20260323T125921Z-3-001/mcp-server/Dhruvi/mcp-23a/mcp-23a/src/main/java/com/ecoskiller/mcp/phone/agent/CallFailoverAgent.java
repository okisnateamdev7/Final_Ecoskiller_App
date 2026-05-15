package com.ecoskiller.mcp.phone.agent;

import com.ecoskiller.mcp.phone.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

/**
 * AGENT-17: CALL_FAILOVER_AGENT
 * Detects SIP/PSTN bridge failures and executes failover to a
 * backup provider or region. Minimises participant disruption
 * by seamlessly migrating active call legs.
 */
@Component
public class CallFailoverAgent {

    @Tool(name = "phone_failover_trigger",
          description = "Trigger a call failover from a failing bridge to a backup provider/region.")
    public AgentResponse triggerFailover(
            @ToolParam(description = "Bridge ID experiencing failure") String failingBridgeId,
            @ToolParam(description = "Failure type: PROVIDER_DOWN | HIGH_LATENCY | PACKET_LOSS | REGION_OUTAGE") String failureType,
            @ToolParam(description = "Backup provider: EXOTEL | OZONETEL | TATA_COMM | TWILIO") String backupProvider,
            @ToolParam(description = "Backup region: ap-south-1 | us-east-1 | eu-west-1") String backupRegion) {

        String failoverBridgeId = "BRG-FAILOVER-" + System.currentTimeMillis();

        return AgentResponse.ok("CALL_FAILOVER_AGENT",
                "Failover initiated from bridge " + failingBridgeId,
                Map.of(
                        "failingBridgeId",  failingBridgeId,
                        "failureType",      failureType,
                        "backupProvider",   backupProvider,
                        "backupRegion",     backupRegion,
                        "newBridgeId",      failoverBridgeId,
                        "legsToMigrate",    8,
                        "status",           "FAILOVER_IN_PROGRESS",
                        "estimatedSec",     12,
                        "initiatedAt",      Instant.now().toString()
                ));
    }

    @Tool(name = "phone_failover_status",
          description = "Get the current status of an ongoing or completed failover.")
    public AgentResponse getFailoverStatus(
            @ToolParam(description = "Original failing bridge ID") String originalBridgeId) {

        return AgentResponse.ok("CALL_FAILOVER_AGENT",
                "Failover status for bridge " + originalBridgeId,
                Map.of(
                        "originalBridgeId",  originalBridgeId,
                        "newBridgeId",       "BRG-FAILOVER-001",
                        "legsMigrated",      8,
                        "legsFailed",        0,
                        "status",            "COMPLETED",
                        "completedAt",       Instant.now().minusSeconds(30).toString(),
                        "durationSec",       11
                ));
    }

    @Tool(name = "phone_failover_policy_set",
          description = "Configure automatic failover policy for a tenant.")
    public AgentResponse setFailoverPolicy(
            @ToolParam(description = "Tenant ID") String tenantId,
            @ToolParam(description = "Auto-failover enabled: true | false") boolean autoFailover,
            @ToolParam(description = "Latency threshold ms to trigger failover") int latencyThresholdMs,
            @ToolParam(description = "Primary provider") String primaryProvider,
            @ToolParam(description = "Fallback provider") String fallbackProvider) {

        return AgentResponse.ok("CALL_FAILOVER_AGENT",
                "Failover policy configured for tenant " + tenantId,
                Map.of(
                        "tenantId",            tenantId,
                        "autoFailover",        autoFailover,
                        "latencyThresholdMs",  latencyThresholdMs,
                        "primaryProvider",     primaryProvider,
                        "fallbackProvider",    fallbackProvider,
                        "configuredAt",        Instant.now().toString()
                ));
    }
}
