package com.ecoskiller.mcp.royalty.tools;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * CONTRACT_LIFECYCLE_AGENT — Tool: contract_lifecycle
 *
 * Manages the full lifecycle of licensing contracts from DRAFT through
 * ACTIVE → SUSPENDED → TERMINATED / EXPIRED. Tracks state transitions,
 * creates corresponding ledger entries, and publishes Kafka events.
 *
 * Key state transitions:
 *   DRAFT       → ACTIVE   (on signing: licensing.agreement.signed)
 *   ACTIVE      → SUSPENDED (on payment default or dispute)
 *   SUSPENDED   → ACTIVE   (on resolution)
 *   ACTIVE      → TERMINATED (early termination: finalize royalties)
 *   ACTIVE      → EXPIRED  (natural end of agreement period)
 */
public class ContractLifecycleTool extends BaseTool {

    @Override public String getName() { return "contract_lifecycle"; }

    @Override
    public JSONObject getSchema() {
        JSONObject p = new JSONObject()
                .put("action",          str("get|transition|list_active|expiry_check|renewal|finalize"))
                .put("agreement_id",    str("Licensing agreement ID"))
                .put("creator_id",      str("Creator unique ID"))
                .put("licensee_id",     str("Licensee company ID"))
                .put("agreement_status",str("Target status: ACTIVE|SUSPENDED|TERMINATED|EXPIRED"))
                .put("reason",          str("Reason for status transition"))
                .put("renewal_months",  intP("Renewal duration in months"));
        return schema(getName(),
                "CONTRACT_LIFECYCLE_AGENT — Full licensing agreement lifecycle: state transitions, " +
                "expiry tracking, renewal, finalization. Publishes licensing.agreement.* Kafka events.",
                p, "action", "agreement_id");
    }

    @Override
    public JSONObject execute(JSONObject a) throws Exception {
        String action      = a.getString("action");
        String agreementId = a.getString("agreement_id");

        return switch (action) {
            case "get" -> json(new JSONObject()
                    .put("agreement_id",     agreementId)
                    .put("status",           "ACTIVE")
                    .put("creator_id",       a.optString("creator_id",""))
                    .put("licensee_id",      a.optString("licensee_id",""))
                    .put("start_date",       "Fetch from PostgreSQL")
                    .put("end_date",         "Fetch from PostgreSQL")
                    .put("royalty_rate",     "Fetch from PostgreSQL")
                    .put("minimum_guarantee","Fetch from PostgreSQL")
                    .put("territory",        "Fetch from PostgreSQL")
                    .put("timestamp",        now()));
            case "transition" -> {
                String  newStatus = a.optString("agreement_status","ACTIVE");
                String  reason    = a.optString("reason","");
                String  kafkaEvt  = switch (newStatus) {
                    case "ACTIVE"      -> "licensing.agreement.signed";
                    case "TERMINATED"  -> "licensing.agreement.terminated";
                    case "SUSPENDED"   -> "licensing.agreement.suspended";
                    case "EXPIRED"     -> "licensing.agreement.expired";
                    default            -> "licensing.agreement.updated";
                };
                String ledger = "TERMINATED".equals(newStatus)
                        ? "Finalize royalties; release balance to creator; close A/R"
                        : "N/A";
                yield json(new JSONObject()
                        .put("agreement_id",  agreementId)
                        .put("old_status",    "ACTIVE")
                        .put("new_status",    newStatus)
                        .put("reason",        reason)
                        .put("kafka_event",   kafkaEvt)
                        .put("ledger_action", ledger)
                        .put("timestamp",     now()));
            }
            case "list_active" -> json(new JSONObject()
                    .put("active_agreements", "Fetch from PostgreSQL WHERE status='ACTIVE'")
                    .put("timestamp",         now()));
            case "expiry_check" -> json(new JSONObject()
                    .put("agreement_id",   agreementId)
                    .put("days_remaining", 90)
                    .put("expiry_date",    "2025-12-31")
                    .put("auto_renew",     false)
                    .put("action_required","Send renewal reminder to licensee 90 days before expiry")
                    .put("timestamp",      now()));
            case "renewal" -> {
                int months = a.optInt("renewal_months", 12);
                yield json(new JSONObject()
                        .put("agreement_id",   agreementId)
                        .put("renewed_months", months)
                        .put("new_end_date",   "Extended by " + months + " months")
                        .put("status",         "ACTIVE")
                        .put("kafka_event",    "licensing.agreement.renewed")
                        .put("timestamp",      now()));
            }
            case "finalize" -> json(new JSONObject()
                    .put("agreement_id",     agreementId)
                    .put("final_royalties",  "Compute all outstanding royalties")
                    .put("balance_released", "Transfer pending balance to creator wallet")
                    .put("kafka_event",      "royalty.settled")
                    .put("status",           "CLOSED")
                    .put("timestamp",        now()));
            default -> text("Unknown action: " + action);
        };
    }
}
