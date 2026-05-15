package com.ecoskiller.mcp17.agents;

import com.ecoskiller.mcp17.ToolDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * ROYALTY_ESCROW_AGENT
 * Holds royalty funds in escrow pending verification, dispute resolution, or release conditions.
 */
public class RoyaltyEscrowAgent extends ToolDefinition {

    @Override public String getName() { return "royalty_escrow"; }

    @Override public String getDescription() {
        return "ROYALTY_ESCROW_AGENT — Places royalty amounts in escrow pending verification, " +
               "tax validation, or dispute resolution. Manages hold, conditional release, " +
               "forced release, and escrow dispute workflows.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper m) {
        ObjectNode s = baseSchema(m, "action", "entity_id");
        addStringProp(s, "action",        "Action: hold | release | dispute | force_release | status");
        addStringProp(s, "entity_id",     "Beneficiary entity ID");
        addNumberProp(s, "amount",        "Amount to hold/release in INR");
        addStringProp(s, "escrow_id",     "Existing escrow ID (for release/dispute/status)");
        addStringProp(s, "reason",        "Reason for hold or dispute");
        addStringProp(s, "release_condition", "Condition string for conditional release");
        addStringProp(s, "expires_at",    "ISO-8601 expiry of the hold");
        addBoolProp  (s, "auto_release",  "Auto-release when condition is met");
        return s;
    }

    @Override
    public ObjectNode execute(JsonNode args, ObjectMapper m) {
        String action   = str(args, "action",    "status");
        String entityId = str(args, "entity_id", "ENTITY-UNKNOWN");
        double amount   = num(args, "amount",    0.0);
        String escrowId = str(args, "escrow_id",
                           "ESC-" + System.currentTimeMillis());

        ObjectNode data = m.createObjectNode();
        data.put("escrow_id",  escrowId);
        data.put("entity_id",  entityId);
        data.put("action",     action);

        switch (action.toLowerCase()) {
            case "hold" -> {
                data.put("amount_held_inr",    amount);
                data.put("reason",             str(args, "reason", "Pending verification"));
                data.put("release_condition",  str(args, "release_condition", "MANUAL"));
                data.put("expires_at",         str(args, "expires_at",
                                                java.time.Instant.now().plusSeconds(86400 * 30).toString()));
                data.put("auto_release",       bool(args, "auto_release", false));
                data.put("status",             "HELD");
            }
            case "release" -> {
                data.put("amount_released_inr", amount);
                data.put("released_at",         java.time.Instant.now().toString());
                data.put("status",              "RELEASED");
                data.put("payout_triggered",    true);
            }
            case "dispute" -> {
                data.put("dispute_ref",  "DISP-" + escrowId);
                data.put("reason",       str(args, "reason", "Disputed"));
                data.put("status",       "DISPUTED");
                data.put("escalated_at", java.time.Instant.now().toString());
            }
            case "force_release" -> {
                data.put("amount_released_inr", amount);
                data.put("released_at",         java.time.Instant.now().toString());
                data.put("status",              "FORCE_RELEASED");
                data.put("override_authority",  "PLATFORM_ADMIN");
            }
            case "status" -> {
                data.put("status",              "HELD");
                data.put("amount_held_inr",     amount > 0 ? amount : 1200.00);
                data.put("days_remaining",      18);
                data.put("release_condition",   "TAX_VERIFIED");
            }
        }

        return successWithData(m,
            "Escrow action '" + action + "' completed for " + escrowId, data);
    }
}
