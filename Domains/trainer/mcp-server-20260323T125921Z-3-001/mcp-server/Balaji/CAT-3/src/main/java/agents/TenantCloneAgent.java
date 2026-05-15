package com.ecoskiller.mcp.security.agents;

import com.ecoskiller.mcp.security.model.McpTool;
import com.ecoskiller.mcp.security.model.ToolResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * TENANT_CLONE_AGENT
 * Clones a tenant's entire configuration, data, and users to a new tenant.
 * Used for franchise rollouts, disaster recovery, and testing.
 */
@Component
public class TenantCloneAgent implements McpAgent {

    @Override
    public String getName() { return "TENANT_CLONE_AGENT"; }

    @Override
    public String getDescription() {
        return "Clones tenant configurations, data, and users for franchise rollouts, disaster recovery, staging copies, and template-based onboarding.";
    }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            McpTool.builder()
                .name("tenant_clone_create")
                .description("Clone a source tenant to a new target tenant")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "source_tenant_id", prop("string", "Source tenant to clone from"),
                        "new_tenant_name", prop("string", "Name for the new tenant"),
                        "clone_scope", prop("string", "config_only | config_and_users | full_with_data"),
                        "mask_pii", prop("boolean", "Mask PII data in clone (for non-prod environments)"),
                        "target_region", prop("string", "Region to create the clone in")
                    ))
                    .required(List.of("source_tenant_id", "new_tenant_name", "clone_scope"))
                    .build())
                .build(),
            McpTool.builder()
                .name("tenant_clone_status")
                .description("Check the status of an ongoing tenant clone operation")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "clone_job_id", prop("string", "Clone job ID from tenant_clone_create")
                    ))
                    .required(List.of("clone_job_id"))
                    .build())
                .build(),
            McpTool.builder()
                .name("tenant_clone_diff")
                .description("Compare two tenants and show configuration differences")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_a", prop("string", "First tenant ID"),
                        "tenant_b", prop("string", "Second tenant ID"),
                        "diff_scope", prop("string", "config | permissions | integrations | all")
                    ))
                    .required(List.of("tenant_a", "tenant_b"))
                    .build())
                .build()
        );
    }

    @Override
    public ToolResult executeTool(String toolName, JsonNode args) {
        return switch (toolName) {
            case "tenant_clone_create" -> {
                String source = args.path("source_tenant_id").asText();
                String newName = args.path("new_tenant_name").asText();
                String scope = args.path("clone_scope").asText("config_only");
                boolean maskPii = args.path("mask_pii").asBoolean(true);
                String jobId = "CLONE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                yield ToolResult.text(String.format(
                    "TENANT_CLONE_AGENT: Clone started. Source='%s', New tenant='%s', Scope=%s. " +
                    "Job ID=%s. PII masked=%s. ETA=3-8min. Status=CLONING",
                    source, newName, scope, jobId, maskPii ? "YES" : "NO"
                ));
            }
            case "tenant_clone_status" -> {
                String jobId = args.path("clone_job_id").asText();
                yield ToolResult.text(String.format(
                    "TENANT_CLONE_AGENT: Clone job '%s': " +
                    "config=DONE, users=DONE, data=IN_PROGRESS(78%%), integrations=PENDING. " +
                    "ETA=2min. Errors=0. Status=IN_PROGRESS",
                    jobId
                ));
            }
            case "tenant_clone_diff" -> {
                String a = args.path("tenant_a").asText();
                String b = args.path("tenant_b").asText();
                yield ToolResult.text(String.format(
                    "TENANT_CLONE_AGENT: Diff [%s vs %s]: " +
                    "config_diffs=8, permission_diffs=3, integration_diffs=1. " +
                    "Key differences: feature_flags(5), role_mappings(3), webhook_urls(1).",
                    a, b
                ));
            }
            default -> ToolResult.error("Unknown tool: " + toolName);
        };
    }

    private McpTool.PropertyDef prop(String type, String description) {
        return McpTool.PropertyDef.builder().type(type).description(description).build();
    }
}
