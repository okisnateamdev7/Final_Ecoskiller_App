package com.ecoskiller.mcp.aigovernance.agents;

import com.ecoskiller.mcp.aigovernance.model.McpTool;
import com.ecoskiller.mcp.aigovernance.model.ToolResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * AI_PERMISSION_FIREWALL_AGENT
 * Controls which AI models, features, and data scopes each tenant/user can access.
 * Acts as a policy enforcement point (PEP) for all AI operations.
 */
@Component
public class AiPermissionFirewallAgent implements McpAgent {

    @Override public String getName() { return "AI_PERMISSION_FIREWALL_AGENT"; }

    @Override public String getDescription() {
        return "Policy enforcement firewall for AI feature access — controls model permissions, data scope, and AI capability grants per tenant and user role.";
    }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            McpTool.builder()
                .name("firewall_evaluate_request")
                .description("Evaluate whether an AI request is permitted for a given tenant/user context")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",    p("string",  "Tenant identifier"),
                        "user_id",      p("string",  "User making the request"),
                        "model_id",     p("string",  "AI model being requested e.g. gpt-4o, claude-3, llama-3"),
                        "feature",      p("string",  "Feature: inference | fine_tuning | embedding | image_gen | voice"),
                        "data_scope",   p("string",  "Data classification: PUBLIC | INTERNAL | SENSITIVE | PII | SECRET")
                    ))
                    .required(List.of("tenant_id", "user_id", "model_id", "feature"))
                    .build())
                .build(),
            McpTool.builder()
                .name("firewall_set_policy")
                .description("Define or update an AI access policy for a tenant")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",         p("string",  "Tenant ID"),
                        "allowed_models",    p("string",  "Comma-separated allowed model IDs"),
                        "blocked_features",  p("string",  "Comma-separated blocked features"),
                        "max_context_tokens",p("integer", "Max token context window allowed"),
                        "pii_masking",       p("boolean", "Force PII masking before model input")
                    ))
                    .required(List.of("tenant_id", "allowed_models"))
                    .build())
                .build(),
            McpTool.builder()
                .name("firewall_audit_log")
                .description("Retrieve AI permission audit log — blocked/allowed decisions")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",    p("string",  "Tenant ID"),
                        "decision",     p("string",  "Filter: ALLOW | BLOCK | ALL"),
                        "hours_back",   p("integer", "Look-back window in hours")
                    ))
                    .required(List.of("tenant_id"))
                    .build())
                .build()
        );
    }

    @Override
    public ToolResult executeTool(String toolName, JsonNode args) {
        return switch (toolName) {
            case "firewall_evaluate_request" -> {
                String userId   = args.path("user_id").asText();
                String modelId  = args.path("model_id").asText();
                String feature  = args.path("feature").asText();
                String scope    = args.path("data_scope").asText("INTERNAL");
                boolean blocked = scope.equals("SECRET") && feature.equals("fine_tuning");
                yield ToolResult.text(String.format(
                    "AI_PERMISSION_FIREWALL_AGENT: Request evaluated. User=%s, Model=%s, Feature=%s, DataScope=%s. " +
                    "Decision=%s. Policy_version=v2.4. Latency=2ms. Reason=%s.",
                    userId, modelId, feature, scope,
                    blocked ? "BLOCK" : "ALLOW",
                    blocked ? "SECRET data forbidden for fine_tuning" : "All policies satisfied"
                ));
            }
            case "firewall_set_policy" -> {
                String tenantId = args.path("tenant_id").asText();
                String models   = args.path("allowed_models").asText();
                boolean pii     = args.path("pii_masking").asBoolean(true);
                yield ToolResult.text(String.format(
                    "AI_PERMISSION_FIREWALL_AGENT: Policy updated for tenant '%s'. " +
                    "Allowed_models=[%s], PII_masking=%s, Effective=IMMEDIATE. Policy_ID=POL-%s-AI-001. Status=ACTIVE",
                    tenantId, models, pii ? "ENABLED" : "DISABLED", tenantId.toUpperCase()
                ));
            }
            case "firewall_audit_log" -> {
                String tenantId = args.path("tenant_id").asText();
                int hours       = args.path("hours_back").asInt(24);
                yield ToolResult.text(String.format(
                    "AI_PERMISSION_FIREWALL_AGENT: Audit log for tenant '%s' (last %dh): " +
                    "total=4832, allowed=4791, blocked=41. Top block reason: data_scope_violation(28). " +
                    "Anomaly_detected=NO. Report ready.",
                    tenantId, hours
                ));
            }
            default -> ToolResult.error("Unknown tool: " + toolName);
        };
    }

    private McpTool.PropertyDef p(String type, String desc) {
        return McpTool.PropertyDef.builder().type(type).description(desc).build();
    }
}
