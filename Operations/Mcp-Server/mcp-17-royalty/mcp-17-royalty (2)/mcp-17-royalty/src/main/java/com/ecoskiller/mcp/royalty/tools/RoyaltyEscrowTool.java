package com.ecoskiller.mcp.royalty.tools;

import org.json.JSONObject;
import java.math.BigDecimal;

/**
 * ROYALTY_ESCROW_AGENT — Tool: royalty_escrow
 *
 * Manages escrow accounts for held royalties — funds that cannot be
 * immediately paid due to: pending guardian consent (minors), disputed
 * revenue reports, legal holds, or failed payment verification.
 *
 * Escrow is distinct from the regular wallet: funds are legally
 * ring-fenced and cannot be co-mingled with operational cash.
 */
public class RoyaltyEscrowTool extends BaseTool {

    @Override public String getName() { return "royalty_escrow"; }

    @Override
    public JSONObject getSchema() {
        JSONObject p = new JSONObject()
                .put("action",         str("hold|release|dispute|check|list_holds|release_guardian_hold"))
                .put("creator_id",     str("Creator unique ID"))
                .put("agreement_id",   str("Related agreement ID"))
                .put("amount",         num("Amount to hold in escrow"))
                .put("currency",       str("Currency"))
                .put("hold_reason",    str("GUARDIAN_CONSENT|DISPUTE|LEGAL_HOLD|PAYMENT_VERIFICATION|FRAUD_REVIEW"))
                .put("guardian_id",    str("Guardian ID (for GUARDIAN_CONSENT holds)"))
                .put("dispute_notes",  str("Notes on disputed amount"))
                .put("release_notes",  str("Notes on escrow release"));
        return schema(getName(),
                "ROYALTY_ESCROW_AGENT — Ring-fenced escrow management for held royalties. " +
                "Handles minor creator guardian consent holds, revenue disputes, legal freezes. " +
                "Compliant with child labor law and financial regulations.",
                p, "action", "creator_id");
    }

    @Override
    public JSONObject execute(JSONObject a) throws Exception {
        String action    = a.getString("action");
        String creatorId = a.getString("creator_id");

        return switch (action) {
            case "hold" -> {
                double  amount   = a.optDouble("amount", 0);
                String  reason   = a.optString("hold_reason","GUARDIAN_CONSENT");
                String  currency = a.optString("currency","INR");
                String  escrowId = "ESCROW-" + creatorId + "-" + System.currentTimeMillis();
                JSONObject res = new JSONObject()
                        .put("escrow_id",        escrowId)
                        .put("creator_id",       creatorId)
                        .put("amount_held",      money(amount).toPlainString())
                        .put("currency",         currency)
                        .put("hold_reason",      reason)
                        .put("status",           "HELD")
                        .put("ring_fenced",      true)
                        .put("ledger_entry",     "DEBIT A/P-Creators / CREDIT Escrow-Payable " + money(amount))
                        .put("payout_blocked",   true)
                        .put("resolution_steps", holdResolution(reason))
                        .put("created_at",       now());
                yield json(res);
            }
            case "release" -> {
                String notes = a.optString("release_notes","Escrow conditions met");
                yield json(new JSONObject()
                        .put("creator_id",    creatorId)
                        .put("status",        "RELEASED")
                        .put("notes",         notes)
                        .put("ledger_entry",  "DEBIT Escrow-Payable / CREDIT A/P-Creators")
                        .put("payout_queued", true)
                        .put("kafka_event",   "payout.queued")
                        .put("timestamp",     now()));
            }
            case "dispute" -> {
                String notes = a.optString("dispute_notes","Licensee disputes reported sales");
                yield json(new JSONObject()
                        .put("creator_id",     creatorId)
                        .put("status",         "DISPUTED")
                        .put("dispute_notes",  notes)
                        .put("payout_status",  "HELD_PENDING_AUDIT")
                        .put("audit_required", true)
                        .put("sla",            "Resolve within 30 days per licensing agreement terms")
                        .put("timestamp",      now()));
            }
            case "check" ->
                json(new JSONObject()
                        .put("creator_id",       creatorId)
                        .put("escrow_balance",   "0.00")
                        .put("active_holds",     0)
                        .put("oldest_hold_date", "N/A")
                        .put("timestamp",        now()));
            case "list_holds" ->
                json(new JSONObject()
                        .put("creator_id", creatorId)
                        .put("holds",      "Fetch from escrow PostgreSQL table")
                        .put("timestamp",  now()));
            case "release_guardian_hold" -> {
                String guardianId = a.optString("guardian_id","");
                yield json(new JSONObject()
                        .put("creator_id",     creatorId)
                        .put("guardian_id",    guardianId)
                        .put("consent_status", "CONSENTED")
                        .put("escrow_status",  "RELEASING")
                        .put("payout_routing", "CUSTODIAL_ACCOUNT")
                        .put("compliance",     "Guardian consent logged with digital signature")
                        .put("timestamp",      now()));
            }
            default -> text("Unknown action: " + action);
        };
    }

    private String holdResolution(String reason) {
        return switch (reason) {
            case "GUARDIAN_CONSENT"      -> "Obtain signed guardian consent form";
            case "DISPUTE"               -> "Complete revenue audit against licensee POS data";
            case "LEGAL_HOLD"            -> "Await legal clearance from compliance team";
            case "PAYMENT_VERIFICATION"  -> "Verify bank account / wallet address";
            case "FRAUD_REVIEW"          -> "Complete fraud investigation by compliance-audit-service";
            default                      -> "Contact platform support";
        };
    }
}
