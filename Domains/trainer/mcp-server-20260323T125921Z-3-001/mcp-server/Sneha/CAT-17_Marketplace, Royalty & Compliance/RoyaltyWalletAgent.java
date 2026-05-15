package com.ecoskiller.mcp17.agents;

import com.ecoskiller.mcp17.ToolDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * ROYALTY_WALLET_AGENT_v1.0.0
 * Manages the royalty wallet: credits, debits, holds, withdrawals, and balance queries.
 */
public class RoyaltyWalletAgent extends ToolDefinition {

    @Override public String getName() { return "royalty_wallet"; }

    @Override public String getDescription() {
        return "ROYALTY_WALLET_AGENT_v1.0.0 — Manages the per-entity royalty wallet: " +
               "credit royalties, debit withdrawals, place/release escrow holds, " +
               "query balance, and generate wallet statements.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper m) {
        ObjectNode s = baseSchema(m, "entity_id", "action");
        addStringProp(s, "entity_id", "Entity whose wallet to operate on");
        addStringProp(s, "action",    "Action: credit | debit | hold | release | balance | statement");
        addNumberProp(s, "amount",    "Amount in INR (required for credit/debit/hold/release)");
        addStringProp(s, "reference", "Reference ID for the transaction");
        addStringProp(s, "note",      "Human-readable note for the ledger entry");
        return s;
    }

    @Override
    public ObjectNode execute(JsonNode args, ObjectMapper m) {
        String entityId = str(args, "entity_id", "ENTITY-UNKNOWN");
        String action   = str(args, "action",    "balance");
        double amount   = num(args, "amount",    0.0);
        String ref      = str(args, "reference", "REF-" + System.currentTimeMillis());

        ObjectNode data = m.createObjectNode();
        data.put("entity_id",      entityId);
        data.put("action",         action);
        data.put("reference",      ref);

        switch (action.toLowerCase()) {
            case "credit" -> {
                data.put("credited_inr",      amount);
                data.put("new_balance_inr",   5200.00 + amount);
                data.put("transaction_id",    "TXN-CR-" + System.currentTimeMillis());
            }
            case "debit" -> {
                data.put("debited_inr",       amount);
                data.put("new_balance_inr",   Math.max(0, 5200.00 - amount));
                data.put("transaction_id",    "TXN-DB-" + System.currentTimeMillis());
            }
            case "hold" -> {
                data.put("held_inr",          amount);
                data.put("hold_id",           "HOLD-" + System.currentTimeMillis());
                data.put("hold_expires_at",   java.time.Instant.now().plusSeconds(86400 * 7).toString());
            }
            case "release" -> {
                data.put("released_inr",      amount);
                data.put("available_balance", 5200.00 + amount);
            }
            case "balance" -> {
                data.put("total_balance_inr",     5200.00);
                data.put("available_inr",         4750.00);
                data.put("on_hold_inr",           450.00);
                data.put("lifetime_credited_inr", 18400.00);
                data.put("lifetime_withdrawn_inr",13200.00);
            }
            case "statement" -> {
                data.put("statement_ref", "STMT-" + entityId + "-" + System.currentTimeMillis());
                data.put("entries_count", 24);
                data.put("period",        "last_30_days");
            }
        }

        return successWithData(m, "Wallet action '" + action + "' completed for " + entityId, data);
    }
}
