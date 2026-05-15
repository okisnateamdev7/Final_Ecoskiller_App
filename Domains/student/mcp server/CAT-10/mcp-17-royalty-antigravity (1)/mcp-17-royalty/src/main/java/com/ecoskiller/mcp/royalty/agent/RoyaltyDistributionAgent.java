package com.ecoskiller.mcp.royalty.agent;

import com.ecoskiller.mcp.royalty.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * AGENT-06: ROYALTY_DISTRIBUTION_AGENT
 * Distributes calculated royalties to stakeholders:
 * Creator / Franchise / Platform based on split rules.
 */
@Component
public class RoyaltyDistributionAgent {

    @Tool(name = "royalty_distribute",
          description = "Distribute royalty amounts to all stakeholders per split rule.")
    public AgentResponse distribute(
            @ToolParam(description = "Source transaction ID") String transactionId,
            @ToolParam(description = "Net distributable amount after tax") double netAmount,
            @ToolParam(description = "Content creator user ID") String creatorId,
            @ToolParam(description = "Franchise ID") String franchiseId,
            @ToolParam(description = "Rule: STANDARD | PREMIUM | COMPETITION") String ruleType) {

        double[] split = switch (ruleType.toUpperCase()) {
            case "PREMIUM"     -> new double[]{0.70, 0.15, 0.15};
            case "COMPETITION" -> new double[]{0.50, 0.25, 0.25};
            default            -> new double[]{0.60, 0.20, 0.20};
        };

        return AgentResponse.ok("ROYALTY_DISTRIBUTION_AGENT",
                "Royalty distributed for " + transactionId,
                Map.of(
                        "transactionId", transactionId,
                        "netAmount",     netAmount,
                        "ruleType",      ruleType,
                        "distributions", List.of(
                                Map.of("recipient",creatorId,   "role","CREATOR",   "amount",netAmount * split[0], "pct",(int)(split[0]*100)),
                                Map.of("recipient",franchiseId, "role","FRANCHISE", "amount",netAmount * split[1], "pct",(int)(split[1]*100)),
                                Map.of("recipient","PLATFORM",  "role","PLATFORM",  "amount",netAmount * split[2], "pct",(int)(split[2]*100))
                        ),
                        "status",        "COMPLETED",
                        "distributedAt", Instant.now().toString()
                ));
    }

    @Tool(name = "distribution_rule_get",
          description = "Get royalty split percentages for a given rule type.")
    public AgentResponse getRule(
            @ToolParam(description = "Rule: STANDARD | PREMIUM | COMPETITION") String ruleType) {

        Map<String, Object> split = switch (ruleType.toUpperCase()) {
            case "PREMIUM"     -> Map.of("creator",70,"franchise",15,"platform",15);
            case "COMPETITION" -> Map.of("creator",50,"franchise",25,"platform",25);
            default            -> Map.of("creator",60,"franchise",20,"platform",20);
        };

        return AgentResponse.ok("ROYALTY_DISTRIBUTION_AGENT",
                "Distribution rule for " + ruleType,
                Map.of("ruleType", ruleType, "splitPercentages", split));
    }
}
