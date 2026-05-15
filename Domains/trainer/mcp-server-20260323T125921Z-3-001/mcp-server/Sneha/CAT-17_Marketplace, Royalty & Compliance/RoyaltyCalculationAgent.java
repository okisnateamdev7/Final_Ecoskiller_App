package com.ecoskiller.mcp17.agents;

import com.ecoskiller.mcp17.ToolDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * ROYALTY_CALCULATION_AGENT_SEALED
 * Calculates royalties for idea creators, trainers, and content owners using tiered rate tables.
 */
public class RoyaltyCalculationAgent extends ToolDefinition {

    @Override public String getName() { return "royalty_calculation"; }

    @Override public String getDescription() {
        return "ROYALTY_CALCULATION_AGENT_SEALED — Computes tiered royalties for idea creators, " +
               "trainers, and content owners. Applies rate tables, deductions, platform fees, " +
               "and outputs a signed royalty ledger entry.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper m) {
        ObjectNode s = baseSchema(m, "entity_id", "role", "gross_revenue");
        addStringProp(s, "entity_id",     "Creator/trainer/owner entity ID");
        addStringProp(s, "role",          "Role: idea_creator | trainer | content_owner | organizer");
        addNumberProp(s, "gross_revenue", "Gross revenue attributed to this entity (INR)");
        addStringProp(s, "cycle_id",      "Royalty cycle reference");
        addNumberProp(s, "platform_fee_pct", "Platform fee % (default 20)");
        addStringProp(s, "tier",          "Tier: bronze | silver | gold | platinum");
        return s;
    }

    @Override
    public ObjectNode execute(JsonNode args, ObjectMapper m) {
        String entityId    = str(args, "entity_id",       "ENTITY-UNKNOWN");
        String role        = str(args, "role",            "idea_creator");
        double gross       = num(args, "gross_revenue",   0.0);
        double platformFee = num(args, "platform_fee_pct",20.0) / 100.0;
        String tier        = str(args, "tier",            "bronze");
        String cycleId     = str(args, "cycle_id",        "CYCLE-CURRENT");

        double royaltyRate  = resolveRoyaltyRate(role, tier);
        double platformCut  = gross * platformFee;
        double netRevenue   = gross - platformCut;
        double royaltyAmt   = netRevenue * royaltyRate;
        double taxDeduction = royaltyAmt * 0.10; // TDS 10%
        double payable      = royaltyAmt - taxDeduction;

        ObjectNode breakdown = m.createObjectNode();
        breakdown.put("gross_revenue",       round(gross));
        breakdown.put("platform_fee_pct",    platformFee * 100);
        breakdown.put("platform_deduction",  round(platformCut));
        breakdown.put("net_revenue",         round(netRevenue));
        breakdown.put("royalty_rate_pct",    royaltyRate * 100);
        breakdown.put("royalty_amount",      round(royaltyAmt));
        breakdown.put("tds_deduction_10pct", round(taxDeduction));
        breakdown.put("net_payable",         round(payable));

        ObjectNode data = m.createObjectNode();
        data.put("entity_id",   entityId);
        data.put("role",        role);
        data.put("tier",        tier);
        data.put("cycle_id",    cycleId);
        data.put("ledger_ref",  "LED-" + entityId + "-" + cycleId);
        data.set("breakdown",   breakdown);

        return successWithData(m,
            "Royalty calculated for " + entityId + ": ₹" + round(payable) + " payable", data);
    }

    private double resolveRoyaltyRate(String role, String tier) {
        double base = switch (role.toLowerCase()) {
            case "idea_creator"   -> 0.30;
            case "trainer"        -> 0.25;
            case "content_owner"  -> 0.20;
            case "organizer"      -> 0.15;
            default               -> 0.20;
        };
        double tierBonus = switch (tier.toLowerCase()) {
            case "platinum" -> 0.05;
            case "gold"     -> 0.03;
            case "silver"   -> 0.01;
            default         -> 0.00;
        };
        return Math.min(base + tierBonus, 0.50);
    }

    private double round(double v) { return Math.round(v * 100.0) / 100.0; }
}
