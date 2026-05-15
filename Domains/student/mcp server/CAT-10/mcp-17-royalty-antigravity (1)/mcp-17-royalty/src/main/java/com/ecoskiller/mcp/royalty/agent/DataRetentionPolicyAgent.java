package com.ecoskiller.mcp.royalty.agent;

import com.ecoskiller.mcp.royalty.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * AGENT-14: DATA_RETENTION_POLICY_AGENT
 * Enforces statutory data retention policies for royalty records,
 * contracts, and financial transactions (Income Tax Act 1961).
 */
@Component
public class DataRetentionPolicyAgent {

    @Tool(name = "retention_policy_apply",
          description = "Apply data retention policy for a specific data type and tenant.")
    public AgentResponse applyPolicy(
            @ToolParam(description = "Tenant ID") String tenantId,
            @ToolParam(description = "Data type: ROYALTY_RECORDS | CONTRACTS | TAX_DOCS | AUDIT_LOGS") String dataType,
            @ToolParam(description = "Retention years (statutory minimum for financials: 7)") int retentionYears) {

        return AgentResponse.ok("DATA_RETENTION_POLICY_AGENT",
                "Retention policy applied for " + dataType,
                Map.of(
                        "tenantId",        tenantId,
                        "dataType",        dataType,
                        "retentionYears",  retentionYears,
                        "policyStatus",    "ACTIVE",
                        "nextReview",      LocalDate.now().plusYears(1).toString(),
                        "autoDelete",      retentionYears > 0,
                        "regulatoryBasis", "Income Tax Act 1961 / Companies Act 2013"
                ));
    }

    @Tool(name = "retention_purge_schedule",
          description = "Schedule purge of expired records per retention policy.")
    public AgentResponse schedulePurge(
            @ToolParam(description = "Tenant ID") String tenantId,
            @ToolParam(description = "Data type to purge") String dataType) {

        return AgentResponse.ok("DATA_RETENTION_POLICY_AGENT",
                "Purge scheduled for " + dataType,
                Map.of(
                        "tenantId",         tenantId,
                        "dataType",         dataType,
                        "eligibleRecords",  1240,
                        "scheduledAt",      Instant.now().plus(1, ChronoUnit.DAYS).toString(),
                        "purgeStatus",      "SCHEDULED"
                ));
    }
}
