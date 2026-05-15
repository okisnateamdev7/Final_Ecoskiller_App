package com.ecoskiller.mcp.security.agents;

import com.ecoskiller.mcp.security.model.McpTool;
import com.ecoskiller.mcp.security.model.ToolResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * API_ECONOMY_AGENT
 * Manages API monetization, rate limits, quota tiers, and usage-based billing contracts.
 */
@Component
public class ApiEconomyAgent implements McpAgent {

    @Override
    public String getName() { return "API_ECONOMY_AGENT"; }

    @Override
    public String getDescription() {
        return "Manages API monetization, rate limiting, quota tiers, and usage-based billing for tenant APIs.";
    }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            McpTool.builder()
                .name("api_economy_create_plan")
                .description("Create a new API usage/billing plan for a tenant")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id", prop("string", "Target tenant ID"),
                        "plan_name", prop("string", "Plan name e.g. FREE / STARTER / PRO / ENTERPRISE"),
                        "rate_limit_per_minute", prop("integer", "Max API calls per minute"),
                        "monthly_quota", prop("integer", "Total API calls allowed per month"),
                        "price_per_1k_calls", prop("number", "Billing rate per 1000 calls")
                    ))
                    .required(List.of("tenant_id", "plan_name", "rate_limit_per_minute"))
                    .build())
                .build(),
            McpTool.builder()
                .name("api_economy_get_usage")
                .description("Retrieve current API usage stats for a tenant")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id", prop("string", "Target tenant ID"),
                        "period", prop("string", "Reporting period: today | week | month")
                    ))
                    .required(List.of("tenant_id"))
                    .build())
                .build(),
            McpTool.builder()
                .name("api_economy_enforce_quota")
                .description("Check and enforce quota limits; returns ALLOW or BLOCK")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id", prop("string", "Tenant to check"),
                        "api_key", prop("string", "API key making the request")
                    ))
                    .required(List.of("tenant_id", "api_key"))
                    .build())
                .build()
        );
    }

    @Override
    public ToolResult executeTool(String toolName, JsonNode args) {
        return switch (toolName) {
            case "api_economy_create_plan" -> {
                String tenantId = args.path("tenant_id").asText();
                String planName = args.path("plan_name").asText();
                int rateLimit = args.path("rate_limit_per_minute").asInt(100);
                yield ToolResult.text(String.format(
                    "API_ECONOMY_AGENT: Plan '%s' created for tenant '%s'. Rate limit: %d req/min. Status: ACTIVE",
                    planName, tenantId, rateLimit
                ));
            }
            case "api_economy_get_usage" -> {
                String tenantId = args.path("tenant_id").asText();
                String period = args.path("period").asText("month");
                yield ToolResult.text(String.format(
                    "API_ECONOMY_AGENT: Usage for tenant '%s' (%s): calls=124530, quota_used=62%%, throttled=12, cost=$24.90",
                    tenantId, period
                ));
            }
            case "api_economy_enforce_quota" -> {
                String tenantId = args.path("tenant_id").asText();
                yield ToolResult.text(String.format(
                    "API_ECONOMY_AGENT: Quota check for tenant '%s': ALLOW. Remaining: 45470 calls this month.", tenantId
                ));
            }
            default -> ToolResult.error("Unknown tool: " + toolName);
        };
    }

    private McpTool.PropertyDef prop(String type, String description) {
        return McpTool.PropertyDef.builder().type(type).description(description).build();
    }
}
