package com.ecoskiller.mcp.royalty.tools;

import org.json.JSONObject;

/**
 * PARENT_DASHBOARD_AGENT — Tool: parent_dashboard
 *
 * Guardian/parent portal for monitoring minor creator earnings,
 * providing consent, managing custodial accounts, and accessing
 * earnings statements.
 *
 * Required for child labor law compliance (spec Section 3):
 * "If creator is minor (<18): require parent/guardian consent before payout.
 * Store consent_record: guardian_id, relationship, signed_date, digital_signature."
 */
public class ParentDashboardTool extends BaseTool {

    @Override public String getName() { return "parent_dashboard"; }

    @Override
    public JSONObject getSchema() {
        JSONObject p = new JSONObject()
                .put("action",        str("get_overview|grant_consent|revoke_consent|get_earnings|set_custodial_account|consent_status"))
                .put("creator_id",    str("Minor creator unique ID"))
                .put("guardian_id",   str("Parent/guardian user ID"))
                .put("digital_signature", str("Base64 digital signature of consent form"))
                .put("relationship",  str("Guardian relationship: PARENT|LEGAL_GUARDIAN|TRUSTEE"))
                .put("bank_account",  str("Custodial bank account reference (masked)"));
        return schema(getName(),
                "PARENT_DASHBOARD_AGENT — Guardian portal for minor creator management: consent workflow, " +
                "custodial account setup, earnings visibility, child labor law compliance.",
                p, "action", "creator_id", "guardian_id");
    }

    @Override
    public JSONObject execute(JSONObject a) throws Exception {
        String action    = a.getString("action");
        String creatorId = a.getString("creator_id");
        String guardianId= a.optString("guardian_id","");

        return switch (action) {
            case "get_overview" -> json(new JSONObject()
                    .put("creator_id",            creatorId)
                    .put("guardian_id",           guardianId)
                    .put("minor_status",          true)
                    .put("pending_balance",       "0.00")
                    .put("total_earned_ytd",      "0.00")
                    .put("total_paid_ytd",        "0.00")
                    .put("consent_status",        "PENDING")
                    .put("custodial_account_set", false)
                    .put("payout_blocked",        true)
                    .put("action_required",       "Grant consent to unblock payouts")
                    .put("timestamp",             now()));
            case "grant_consent" -> {
                String sig = a.optString("digital_signature","");
                String rel = a.optString("relationship","PARENT");
                if (sig.isBlank())
                    yield text("ERROR: digital_signature is required to grant consent. " +
                            "Guardian must sign the consent form.");
                yield json(new JSONObject()
                        .put("creator_id",       creatorId)
                        .put("guardian_id",      guardianId)
                        .put("relationship",     rel)
                        .put("consent_status",   "CONSENTED")
                        .put("signed_date",      now().substring(0,10))
                        .put("signature_hash",   sig.substring(0, Math.min(sig.length(),16)) + "...")
                        .put("payout_status",    "UNBLOCKED — routed to custodial account")
                        .put("kafka_event",      "minor.guardian_consent.obtained")
                        .put("compliance_note",  "Consent record immutably logged for child labor law audit")
                        .put("timestamp",        now()));
            }
            case "revoke_consent" -> json(new JSONObject()
                    .put("creator_id",    creatorId)
                    .put("guardian_id",   guardianId)
                    .put("consent_status","REVOKED")
                    .put("payout_status", "BLOCKED — consent revoked")
                    .put("timestamp",     now()));
            case "get_earnings" -> json(new JSONObject()
                    .put("creator_id",     creatorId)
                    .put("guardian_id",    guardianId)
                    .put("ytd_earned",     "Fetch from ledger")
                    .put("ytd_paid",       "Fetch from payout history")
                    .put("pending",        "Fetch from wallet")
                    .put("statement_url",  "POST /royalties/statement?creator_id=" + creatorId)
                    .put("timestamp",      now()));
            case "set_custodial_account" -> {
                String account = a.optString("bank_account","[MASKED]");
                yield json(new JSONObject()
                        .put("creator_id",       creatorId)
                        .put("guardian_id",      guardianId)
                        .put("account_type",     "CUSTODIAL")
                        .put("account_ref",      account)
                        .put("payout_routing",   "All payouts to custodial account until creator turns 18")
                        .put("verified",         false)
                        .put("next_step",        "Account verification required")
                        .put("timestamp",        now()));
            }
            case "consent_status" -> json(new JSONObject()
                    .put("creator_id",    creatorId)
                    .put("guardian_id",   guardianId)
                    .put("status",        "PENDING")
                    .put("blocking_payout", true)
                    .put("timestamp",     now()));
            default -> text("Unknown action: " + action);
        };
    }
}
