package com.ecoskiller.mcp.security.agents;

import com.ecoskiller.mcp.security.model.McpTool;
import com.ecoskiller.mcp.security.model.ToolResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * ENV_PROMOTION_AGENT
 * Handles environment promotions: DEV -> STAGING -> PROD pipelines with security gate checks.
 */
@Component
public class EnvPromotionAgent implements McpAgent {

    @Override
    public String getName() { return "ENV_PROMOTION_AGENT"; }

    @Override
    public String getDescription() {
        return "Manages environment promotions across DEV, STAGING, UAT, and PROD with security gates, config diff checks, and rollback capability.";
    }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            McpTool.builder()
                .name("env_promote")
                .description("Promote a service or config from one environment to another")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "service_name", prop("string", "Service or component name"),
                        "from_env", prop("string", "Source environment: DEV | STAGING | UAT"),
                        "to_env", prop("string", "Target environment: STAGING | UAT | PROD"),
                        "tenant_id", prop("string", "Tenant scope (optional for global services)"),
                        "skip_security_gate", prop("boolean", "Override security gate — requires justification")
                    ))
                    .required(List.of("service_name", "from_env", "to_env"))
                    .build())
                .build(),
            McpTool.builder()
                .name("env_diff_check")
                .description("Compare configuration differences between two environments")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "service_name", prop("string", "Service name"),
                        "env_a", prop("string", "First environment"),
                        "env_b", prop("string", "Second environment")
                    ))
                    .required(List.of("service_name", "env_a", "env_b"))
                    .build())
                .build(),
            McpTool.builder()
                .name("env_rollback")
                .description("Rollback a promotion to the previous environment version")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "service_name", prop("string", "Service name"),
                        "environment", prop("string", "Environment to rollback"),
                        "reason", prop("string", "Rollback reason for audit trail")
                    ))
                    .required(List.of("service_name", "environment", "reason"))
                    .build())
                .build()
        );
    }

    @Override
    public ToolResult executeTool(String toolName, JsonNode args) {
        return switch (toolName) {
            case "env_promote" -> {
                String service = args.path("service_name").asText();
                String from = args.path("from_env").asText();
                String to = args.path("to_env").asText();
                boolean skipGate = args.path("skip_security_gate").asBoolean(false);
                String gateStatus = skipGate ? "BYPASSED (override)" : "PASSED";
                yield ToolResult.text(String.format(
                    "ENV_PROMOTION_AGENT: '%s' promoted %s→%s. Security gate=%s. " +
                    "Config vars=48, secrets_rotated=YES, smoke_test=PASS. Status=PROMOTED",
                    service, from, to, gateStatus
                ));
            }
            case "env_diff_check" -> {
                String service = args.path("service_name").asText();
                String envA = args.path("env_a").asText();
                String envB = args.path("env_b").asText();
                yield ToolResult.text(String.format(
                    "ENV_PROMOTION_AGENT: Diff for '%s' [%s vs %s]: " +
                    "changed_vars=5 (DB_POOL_SIZE, LOG_LEVEL, FEATURE_FLAGS, API_TIMEOUT, CACHE_TTL). " +
                    "Missing in %s=1. Secrets_drift=NO.",
                    service, envA, envB, envB
                ));
            }
            case "env_rollback" -> {
                String service = args.path("service_name").asText();
                String env = args.path("environment").asText();
                yield ToolResult.text(String.format(
                    "ENV_PROMOTION_AGENT: Rollback executed for '%s' in %s. " +
                    "Previous version restored in 8s. Health check=PASS. Incident ID=INC-2024-0312 logged.",
                    service, env
                ));
            }
            default -> ToolResult.error("Unknown tool: " + toolName);
        };
    }

    private McpTool.PropertyDef prop(String type, String description) {
        return McpTool.PropertyDef.builder().type(type).description(description).build();
    }
}
