package com.ecoskiller.mcp.stability.agent;

import com.ecoskiller.mcp.stability.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * AGENT-07: POLICY_VERSION_CONTROL_AGENT
 * Manages versioning of platform policies: royalty rules,
 * compliance policies, rate limits — with diff and rollback.
 */
@Component
public class PolicyVersionControlAgent {

    @Tool(name = "policy_version_publish",
          description = "Publish a new version of a platform policy.")
    public AgentResponse publish(
            @ToolParam(description = "Policy name e.g. ROYALTY_SPLIT_POLICY | RATE_LIMIT_POLICY") String policyName,
            @ToolParam(description = "New version e.g. v3.0") String version,
            @ToolParam(description = "Change summary") String changeSummary,
            @ToolParam(description = "Author user ID") String authorId,
            @ToolParam(description = "Effective date YYYY-MM-DD") String effectiveDate) {

        return AgentResponse.ok("POLICY_VERSION_CONTROL_AGENT",
                "Policy " + policyName + " " + version + " published",
                Map.of(
                        "policyId",       "POL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                        "policyName",     policyName,
                        "version",        version,
                        "changeSummary",  changeSummary,
                        "authorId",       authorId,
                        "publishedAt",    Instant.now().toString(),
                        "effectiveDate",  effectiveDate,
                        "status",         "SCHEDULED",
                        "rollbackVersion","v2.9"
                ));
    }

    @Tool(name = "policy_version_diff",
          description = "Show field-level diff between two versions of a policy.")
    public AgentResponse diff(
            @ToolParam(description = "Policy name") String policyName,
            @ToolParam(description = "Older version e.g. v2.9") String versionA,
            @ToolParam(description = "Newer version e.g. v3.0") String versionB) {

        return AgentResponse.ok("POLICY_VERSION_CONTROL_AGENT",
                "Policy diff: " + policyName + " " + versionA + " → " + versionB,
                Map.of(
                        "policyName",     policyName,
                        "versionA",       versionA,
                        "versionB",       versionB,
                        "changes", List.of(
                                Map.of("field","creator_royalty_pct","old","60%","new","62%","type","MODIFIED"),
                                Map.of("field","escrow_hold_days",   "old","7",  "new","5",  "type","MODIFIED"),
                                Map.of("field","platinum_bonus_tier","old","N/A","new","65%","type","ADDED")
                        ),
                        "totalChanges",    3,
                        "breakingChanges", 0
                ));
    }

    @Tool(name = "policy_version_rollback",
          description = "Rollback a policy to a previous version.")
    public AgentResponse rollback(
            @ToolParam(description = "Policy name") String policyName,
            @ToolParam(description = "Target version to rollback to") String targetVersion,
            @ToolParam(description = "Reason for rollback") String reason,
            @ToolParam(description = "Admin ID authorizing rollback") String adminId) {

        return AgentResponse.ok("POLICY_VERSION_CONTROL_AGENT",
                "Policy " + policyName + " rolled back to " + targetVersion,
                Map.of(
                        "policyName",    policyName,
                        "rolledBackTo",  targetVersion,
                        "reason",        reason,
                        "authorizedBy",  adminId,
                        "rolledBackAt",  Instant.now().toString(),
                        "status",        "ACTIVE"
                ));
    }
}
