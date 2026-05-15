package com.ecoskiller.mcp.royalty.agent;

import com.ecoskiller.mcp.royalty.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * AGENT-07: ROYALTY_CALCULATION_AGENT
 * Calculates royalty amounts using tiered rates.
 * Tiers: BRONZE 40% | SILVER 50% | GOLD 55% | PLATINUM 60%
 */
@Component
public class RoyaltyCalculationAgent {

    @Tool(name = "royalty_calculate",
          description = "Calculate royalty from gross revenue based on creator tier and revenue type.")
    public AgentResponse calculate(
            @ToolParam(description = "Gross revenue amount (INR)") double grossRevenue,
            @ToolParam(description = "Revenue type: COURSE_SALE | SUBSCRIPTION | COMPETITION_FEE | LICENSE") String revenueType,
            @ToolParam(description = "Creator tier: BRONZE | SILVER | GOLD | PLATINUM") String creatorTier) {

        double rate       = getRoyaltyRate(creatorTier);
        double grossRoyal = grossRevenue * rate;
        double tds        = grossRoyal * 0.10;
        double netRoyal   = grossRoyal - tds;

        return AgentResponse.ok("ROYALTY_CALCULATION_AGENT",
                "Royalty calculated for " + revenueType,
                Map.of(
                        "grossRevenue",  grossRevenue,
                        "revenueType",   revenueType,
                        "creatorTier",   creatorTier,
                        "royaltyRate",   rate,
                        "grossRoyalty",  grossRoyal,
                        "tdsDeducted",   tds,
                        "netRoyalty",    netRoyal
                ));
    }

    @Tool(name = "royalty_rate_lookup",
          description = "Look up royalty rate for a creator tier.")
    public AgentResponse lookupRate(
            @ToolParam(description = "Creator tier: BRONZE | SILVER | GOLD | PLATINUM") String creatorTier) {

        double rate = getRoyaltyRate(creatorTier);
        return AgentResponse.ok("ROYALTY_CALCULATION_AGENT",
                "Rate for tier " + creatorTier,
                Map.of("creatorTier", creatorTier, "royaltyRate", rate, "ratePercent", (int)(rate * 100) + "%"));
    }

    private double getRoyaltyRate(String tier) {
        return switch (tier.toUpperCase()) {
            case "PLATINUM" -> 0.60;
            case "GOLD"     -> 0.55;
            case "SILVER"   -> 0.50;
            default         -> 0.40; // BRONZE
        };
    }
}
