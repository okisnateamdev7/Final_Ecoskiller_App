package com.ecoskiller.mcp.royalty.tools;

import org.json.JSONObject;
import java.math.BigDecimal;

/**
 * ROYALTY_WALLET_AGENT — Tool: royalty_wallet
 *
 * Manages the creator's royalty wallet — the running balance of
 * accrued but unpaid royalties. Tracks:
 *   - pending_balance (accrued, not yet paid)
 *   - paid_ytd (total paid this year)
 *   - earned_ytd (total earned this year)
 *   - next_payout_date and estimated_amount
 *   - payout_method preference
 *   - minimum_payout_threshold check
 *
 * Multi-currency wallet: tracks balances per currency.
 */
public class RoyaltyWalletTool extends BaseTool {

    @Override public String getName() { return "royalty_wallet"; }

    @Override
    public JSONObject getSchema() {
        JSONObject p = new JSONObject()
                .put("action",             str("get_balance|update_payout_method|check_threshold|add_funds|withdraw|get_history"))
                .put("creator_id",         str("Creator unique ID"))
                .put("currency",           str("Currency: INR|USD|EUR|GBP|USDC"))
                .put("payout_method",      str("ACH|WIRE|STABLECOIN|BANK_TRANSFER"))
                .put("amount",             num("Amount to add or check"))
                .put("wallet_address",     str("Crypto wallet address (for STABLECOIN payouts)"))
                .put("bank_account",       str("Bank account reference (masked, for ACH/WIRE)"))
                .put("minimum_threshold",  num("Minimum balance before payout triggers (default: 100)"));
        return schema(getName(),
                "ROYALTY_WALLET_AGENT — Creator royalty wallet management: balance, payout method, " +
                "threshold checks, multi-currency support. Source of truth for pending creator compensation.",
                p, "action", "creator_id");
    }

    @Override
    public JSONObject execute(JSONObject a) throws Exception {
        String action    = a.getString("action");
        String creatorId = a.getString("creator_id");
        String currency  = a.optString("currency","INR");

        return switch (action) {
            case "get_balance" -> json(new JSONObject()
                    .put("creator_id",        creatorId)
                    .put("currency",          currency)
                    .put("pending_balance",   "0.00")
                    .put("earned_ytd",        "0.00")
                    .put("paid_ytd",          "0.00")
                    .put("next_payout_date",  "End of current month")
                    .put("payout_method",     "BANK_TRANSFER")
                    .put("above_threshold",   false)
                    .put("minimum_threshold", "100.00")
                    .put("note",              "Fetch live balance from PostgreSQL ledger")
                    .put("timestamp",         now()));
            case "update_payout_method" -> {
                String method  = a.getString("payout_method");
                String address = a.optString("wallet_address","");
                String bank    = a.optString("bank_account","");
                JSONObject res = new JSONObject()
                        .put("creator_id",    creatorId)
                        .put("payout_method", method)
                        .put("wallet_set",    method.equals("STABLECOIN") ? address : bank)
                        .put("status",        "UPDATED")
                        .put("verified",      false)
                        .put("next_step",     "Verify account before first payout")
                        .put("timestamp",     now());
                yield json(res);
            }
            case "check_threshold" -> {
                double balance  = a.optDouble("amount", 0);
                double minThresh= a.optDouble("minimum_threshold", 100);
                boolean eligible = balance >= minThresh;
                yield json(new JSONObject()
                        .put("creator_id",        creatorId)
                        .put("current_balance",   balance)
                        .put("minimum_threshold", minThresh)
                        .put("payout_eligible",   eligible)
                        .put("message", eligible
                                ? "Creator eligible for payout"
                                : "Balance below minimum threshold of " + minThresh));
            }
            case "add_funds" -> {
                double  amount = a.optDouble("amount",0);
                BigDecimal bd  = money(amount);
                yield json(new JSONObject()
                        .put("creator_id",  creatorId)
                        .put("added",       bd.toPlainString())
                        .put("currency",    currency)
                        .put("kafka_event", "royalty.accrued")
                        .put("ledger",      "DEBIT A/R-Licensees / CREDIT Royalty-Revenue")
                        .put("timestamp",   now()));
            }
            case "withdraw" ->
                json(new JSONObject()
                        .put("creator_id", creatorId)
                        .put("action",     "Payout queued via royalty_distribution tool")
                        .put("kafka_event","payout.queued")
                        .put("timestamp",  now()));
            case "get_history" ->
                json(new JSONObject()
                        .put("creator_id",    creatorId)
                        .put("transactions",  "Fetch from immutable ledger — POST /royalties/history")
                        .put("timestamp",     now()));
            default -> text("Unknown action: " + action);
        };
    }
}
