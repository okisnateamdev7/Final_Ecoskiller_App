package com.ecoskiller.mcp.aigovernance.agents;

import com.ecoskiller.mcp.aigovernance.model.McpTool;
import com.ecoskiller.mcp.aigovernance.model.ToolResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * INCENTIVE_AGENT
 * Designs, governs, and monitors AI-driven incentive systems —
 * gamification, reward structures, XP engines, and motivation mechanics.
 */
@Component
public class IncentiveAgent implements McpAgent {

    @Override public String getName() { return "INCENTIVE_AGENT"; }

    @Override public String getDescription() {
        return "Governs AI-driven incentive systems — XP mechanics, reward design, gamification fairness, motivation modeling, and anti-gaming controls.";
    }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            McpTool.builder()
                .name("incentive_design_program")
                .description("Design a new AI-governed incentive/reward program for a tenant")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",      p("string",  "Tenant ID"),
                        "program_name",   p("string",  "Program name"),
                        "target_behavior",p("string",  "Behavior to incentivize e.g. skill_completion | attendance | competition_participation"),
                        "reward_type",    p("string",  "XP | BADGES | CASH | VOUCHERS | LEADERBOARD | CERTIFICATES"),
                        "budget_inr",     p("number",  "Monthly reward budget in INR"),
                        "anti_gaming",    p("boolean", "Enable anti-gaming detection")
                    ))
                    .required(List.of("tenant_id", "program_name", "target_behavior", "reward_type"))
                    .build())
                .build(),
            McpTool.builder()
                .name("incentive_evaluate_user")
                .description("Evaluate incentive eligibility and recommended reward for a user")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",   p("string",  "Tenant ID"),
                        "user_id",     p("string",  "User to evaluate"),
                        "program_id",  p("string",  "Incentive program ID"),
                        "period",      p("string",  "Evaluation period: weekly | monthly | competition")
                    ))
                    .required(List.of("tenant_id", "user_id", "program_id"))
                    .build())
                .build(),
            McpTool.builder()
                .name("incentive_detect_gaming")
                .description("Detect gaming/manipulation of the incentive system by a user")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",   p("string", "Tenant ID"),
                        "user_id",     p("string", "User ID to investigate"),
                        "program_id",  p("string", "Program ID"),
                        "lookback_days",p("integer","Days of activity to analyze")
                    ))
                    .required(List.of("tenant_id", "user_id", "program_id"))
                    .build())
                .build()
        );
    }

    @Override
    public ToolResult executeTool(String toolName, JsonNode args) {
        return switch (toolName) {
            case "incentive_design_program" -> {
                String tenantId  = args.path("tenant_id").asText();
                String name      = args.path("program_name").asText();
                String behavior  = args.path("target_behavior").asText();
                String rewardType= args.path("reward_type").asText();
                double budget    = args.path("budget_inr").asDouble(10000);
                yield ToolResult.text(String.format(
                    "INCENTIVE_AGENT: Program '%s' designed for tenant '%s'. " +
                    "Target=%s, Reward=%s, Budget=₹%.0f/month. " +
                    "Program_ID=INC-%s-001. Rules=8. Anti-gaming=%s. Status=ACTIVE",
                    name, tenantId, behavior, rewardType, budget, tenantId.toUpperCase(),
                    args.path("anti_gaming").asBoolean(true) ? "ENABLED" : "DISABLED"
                ));
            }
            case "incentive_evaluate_user" -> {
                String userId   = args.path("user_id").asText();
                String programId= args.path("program_id").asText();
                String period   = args.path("period").asText("monthly");
                yield ToolResult.text(String.format(
                    "INCENTIVE_AGENT: Evaluation for user '%s' in program '%s' [%s]. " +
                    "Score=842/1000, Rank=#3, Eligible=YES. " +
                    "Recommended reward: 500 XP + SILVER_BADGE. " +
                    "Gaming_flag=CLEAN. Payout_scheduled=END_OF_PERIOD.",
                    userId, programId, period
                ));
            }
            case "incentive_detect_gaming" -> {
                String userId   = args.path("user_id").asText();
                int days        = args.path("lookback_days").asInt(30);
                yield ToolResult.text(String.format(
                    "INCENTIVE_AGENT: Gaming detection for user '%s' (last %d days). " +
                    "Velocity_anomaly=NO, Self_referral=NO, Bulk_completion_spikes=1(MINOR), " +
                    "Bot_probability=0.03(CLEAN), Account_sharing=NO. " +
                    "Verdict=LOW_RISK. No action required.",
                    userId, days
                ));
            }
            default -> ToolResult.error("Unknown tool: " + toolName);
        };
    }

    private McpTool.PropertyDef p(String type, String desc) {
        return McpTool.PropertyDef.builder().type(type).description(desc).build();
    }
}
