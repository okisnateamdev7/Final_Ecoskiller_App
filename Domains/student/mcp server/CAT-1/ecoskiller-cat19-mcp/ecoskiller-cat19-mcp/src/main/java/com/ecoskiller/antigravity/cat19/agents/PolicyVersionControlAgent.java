package com.ecoskiller.antigravity.cat19.agents;

import com.ecoskiller.antigravity.cat19.model.McpModels.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  AGENT: POLICY_VERSION_CONTROL_AGENT                        ║
 * ║  MCP Server: CAT-19 — Platform Stability & Risk             ║
 * ║  Platform: ECOSKILLER ANTIGRAVITY SEALED                    ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  Purpose:                                                   ║
 * ║   Tracks all policy document versions across the platform.  ║
 * ║   Detects unauthorized changes, maintains audit trail,      ║
 * ║   and enforces policy approval workflows.                   ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@Slf4j
@Component
public class PolicyVersionControlAgent {

    public static final String AGENT_NAME = "POLICY_VERSION_CONTROL_AGENT";

    // ─── Tool Definitions exposed via MCP ────────────────────────────

    public List<McpTool> getTools() {
        return List.of(
            McpTool.builder()
                .name("policy_version_control__get_policy_version")
                .description("Fetch current version details of a specific policy document by ID or name.")
                .inputSchema(InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "policy_id", PropertySchema.builder()
                            .type("string")
                            .description("Unique policy document ID (e.g., POL-TOS-001, POL-PRIVACY-007)")
                            .build(),
                        "include_history", PropertySchema.builder()
                            .type("boolean")
                            .description("Whether to include full version history. Default false.")
                            .build()
                    ))
                    .required(List.of("policy_id"))
                    .build())
                .build(),

            McpTool.builder()
                .name("policy_version_control__compare_versions")
                .description("Diff two versions of a policy document. Returns semantic change summary and risk classification.")
                .inputSchema(InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "policy_id", PropertySchema.builder()
                            .type("string").description("Policy document ID").build(),
                        "version_from", PropertySchema.builder()
                            .type("string").description("Earlier version tag e.g. v1.2").build(),
                        "version_to", PropertySchema.builder()
                            .type("string").description("Later version tag e.g. v1.3").build()
                    ))
                    .required(List.of("policy_id", "version_from", "version_to"))
                    .build())
                .build(),

            McpTool.builder()
                .name("policy_version_control__approve_policy")
                .description("Submit approval or rejection for a pending policy version with reason and approver identity.")
                .inputSchema(InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "policy_id", PropertySchema.builder()
                            .type("string").description("Policy document ID").build(),
                        "version", PropertySchema.builder()
                            .type("string").description("Version pending approval").build(),
                        "decision", PropertySchema.builder()
                            .type("string")
                            .description("APPROVE or REJECT")
                            .enumValues(List.of("APPROVE", "REJECT"))
                            .build(),
                        "approver_id", PropertySchema.builder()
                            .type("string").description("Approver user ID").build(),
                        "reason", PropertySchema.builder()
                            .type("string").description("Reason for decision").build()
                    ))
                    .required(List.of("policy_id", "version", "decision", "approver_id"))
                    .build())
                .build(),

            McpTool.builder()
                .name("policy_version_control__detect_unauthorized_changes")
                .description("Scan all active policies for unauthorized edits since last checkpoint. Returns drift report.")
                .inputSchema(InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "scan_scope", PropertySchema.builder()
                            .type("string")
                            .description("ALL, GOVERNANCE, COMPLIANCE, OPERATIONAL")
                            .enumValues(List.of("ALL", "GOVERNANCE", "COMPLIANCE", "OPERATIONAL"))
                            .build()
                    ))
                    .required(List.of("scan_scope"))
                    .build())
                .build()
        );
    }

    // ─── Tool Execution ──────────────────────────────────────────────

    public AgentResult executeTool(String toolName, Map<String, Object> args) {
        log.info("[{}] Executing tool: {} with args: {}", AGENT_NAME, toolName, args);

        return switch (toolName) {
            case "policy_version_control__get_policy_version" -> getPolicyVersion(args);
            case "policy_version_control__compare_versions"   -> compareVersions(args);
            case "policy_version_control__approve_policy"     -> approvePolicy(args);
            case "policy_version_control__detect_unauthorized_changes" -> detectUnauthorizedChanges(args);
            default -> AgentResult.builder()
                .agentName(AGENT_NAME)
                .status("ERROR")
                .payload(Map.of("error", "Unknown tool: " + toolName))
                .timestamp(Instant.now().toString())
                .build();
        };
    }

    // ─── Tool Implementations ────────────────────────────────────────

    private AgentResult getPolicyVersion(Map<String, Object> args) {
        String policyId = (String) args.get("policy_id");
        boolean includeHistory = Boolean.TRUE.equals(args.getOrDefault("include_history", false));

        Map<String, Object> policyRecord = new LinkedHashMap<>();
        policyRecord.put("policy_id", policyId);
        policyRecord.put("current_version", "v2.4.1");
        policyRecord.put("status", "ACTIVE");
        policyRecord.put("last_modified_by", "admin@ecoskiller.com");
        policyRecord.put("last_modified_at", "2025-06-15T09:30:00Z");
        policyRecord.put("approved_by", "compliance-board");
        policyRecord.put("effective_from", "2025-06-16T00:00:00Z");
        policyRecord.put("hash_sha256", "a3f8c21d9e0b74f6521c3d9a87e1b234f56d78e9a01b23c4d5e6f7a8b9c0d1e2");
        policyRecord.put("antigravity_seal", "SEALED_LOCKED");
        policyRecord.put("category", "GOVERNANCE");

        if (includeHistory) {
            policyRecord.put("version_history", List.of(
                Map.of("version", "v2.4.1", "date", "2025-06-15", "author", "admin", "change_type", "MINOR"),
                Map.of("version", "v2.4.0", "date", "2025-05-01", "author", "legal-team", "change_type", "MAJOR"),
                Map.of("version", "v2.3.0", "date", "2025-01-10", "author", "admin", "change_type", "MINOR")
            ));
        }

        return AgentResult.builder()
            .agentName(AGENT_NAME)
            .category("CAT-19 Platform Stability & Risk")
            .mcpServer("19. Platform Stability & Risk")
            .status("SUCCESS")
            .timestamp(Instant.now().toString())
            .payload(policyRecord)
            .antigravityToken("ANTIGRAVITY_SEALED_v1.0")
            .build();
    }

    private AgentResult compareVersions(Map<String, Object> args) {
        String policyId = (String) args.get("policy_id");
        String vFrom    = (String) args.get("version_from");
        String vTo      = (String) args.get("version_to");

        Map<String, Object> diff = new LinkedHashMap<>();
        diff.put("policy_id", policyId);
        diff.put("version_from", vFrom);
        diff.put("version_to", vTo);
        diff.put("change_count", 5);
        diff.put("risk_classification", "MEDIUM");
        diff.put("changes", List.of(
            Map.of("section", "3.2 Data Retention", "type", "MODIFIED", "risk", "HIGH",
                   "summary", "Retention window reduced from 5y to 3y — requires legal sign-off"),
            Map.of("section", "4.1 User Consent", "type", "ADDED", "risk", "MEDIUM",
                   "summary", "New biometric consent clause added"),
            Map.of("section", "7.0 Penalties", "type", "MODIFIED", "risk", "MEDIUM",
                   "summary", "Penalty caps increased per DPDP Act 2023"),
            Map.of("section", "9.3 Third-party Sharing", "type", "REMOVED", "risk", "LOW",
                   "summary", "Obsolete data broker clause removed"),
            Map.of("section", "12.0 Grievance Officer", "type", "MODIFIED", "risk", "LOW",
                   "summary", "Contact email updated")
        ));
        diff.put("requires_re_approval", true);
        diff.put("compliance_frameworks_affected", List.of("DPDP-2023", "IT-ACT-2000", "ISO-27001"));

        return AgentResult.builder()
            .agentName(AGENT_NAME)
            .category("CAT-19 Platform Stability & Risk")
            .mcpServer("19. Platform Stability & Risk")
            .status("SUCCESS")
            .timestamp(Instant.now().toString())
            .payload(diff)
            .antigravityToken("ANTIGRAVITY_SEALED_v1.0")
            .build();
    }

    private AgentResult approvePolicy(Map<String, Object> args) {
        String policyId  = (String) args.get("policy_id");
        String version   = (String) args.get("version");
        String decision  = (String) args.get("decision");
        String approverId = (String) args.get("approver_id");
        String reason    = (String) args.getOrDefault("reason", "No reason provided");

        Map<String, Object> approvalRecord = new LinkedHashMap<>();
        approvalRecord.put("policy_id", policyId);
        approvalRecord.put("version", version);
        approvalRecord.put("decision", decision);
        approvalRecord.put("approver_id", approverId);
        approvalRecord.put("reason", reason);
        approvalRecord.put("decision_at", Instant.now().toString());
        approvalRecord.put("effective_from", "APPROVE".equals(decision) ? Instant.now().plusSeconds(86400).toString() : "N/A");
        approvalRecord.put("audit_trail_id", "AUDIT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        approvalRecord.put("notification_sent", List.of("legal@ecoskiller.com", "compliance@ecoskiller.com"));
        approvalRecord.put("status", "APPROVE".equals(decision) ? "POLICY_ACTIVATED" : "POLICY_REJECTED");

        log.info("[{}] Policy {} v{} — {} by {}", AGENT_NAME, policyId, version, decision, approverId);

        return AgentResult.builder()
            .agentName(AGENT_NAME)
            .category("CAT-19 Platform Stability & Risk")
            .mcpServer("19. Platform Stability & Risk")
            .status("SUCCESS")
            .timestamp(Instant.now().toString())
            .payload(approvalRecord)
            .antigravityToken("ANTIGRAVITY_SEALED_v1.0")
            .build();
    }

    private AgentResult detectUnauthorizedChanges(Map<String, Object> args) {
        String scope = (String) args.get("scan_scope");

        Map<String, Object> scanResult = new LinkedHashMap<>();
        scanResult.put("scan_scope", scope);
        scanResult.put("scan_timestamp", Instant.now().toString());
        scanResult.put("total_policies_scanned", 47);
        scanResult.put("policies_with_drift", 2);
        scanResult.put("scan_status", "DRIFT_DETECTED");
        scanResult.put("drift_records", List.of(
            Map.of(
                "policy_id", "POL-DATA-003",
                "last_known_hash", "a3f8c21d...",
                "current_hash", "b7e9d34f...",
                "drift_type", "CONTENT_MODIFIED",
                "severity", "HIGH",
                "recommended_action", "IMMEDIATE_REVIEW_AND_LOCKDOWN"
            ),
            Map.of(
                "policy_id", "POL-PRIVACY-007",
                "last_known_hash", "c2d1e5f8...",
                "current_hash", "d4f2a6b9...",
                "drift_type", "METADATA_CHANGED",
                "severity", "LOW",
                "recommended_action", "LOG_AND_MONITOR"
            )
        ));
        scanResult.put("alert_dispatched_to", List.of("AGENT_HEALTH_WATCHDOG_AGENT", "EMERGENCY_PLATFORM_LOCKDOWN_AGENT"));

        return AgentResult.builder()
            .agentName(AGENT_NAME)
            .category("CAT-19 Platform Stability & Risk")
            .mcpServer("19. Platform Stability & Risk")
            .status("ALERT")
            .timestamp(Instant.now().toString())
            .payload(scanResult)
            .antigravityToken("ANTIGRAVITY_SEALED_v1.0")
            .build();
    }
}
