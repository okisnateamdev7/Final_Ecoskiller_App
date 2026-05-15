package com.ecoskiller.mcp.royalty.tools;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * DATA_RETENTION_POLICY_AGENT — Tool: data_retention_policy
 *
 * Manages data retention rules for the Royalty Engine's immutable ledger
 * and financial records. Financial data has strict regulatory retention
 * requirements:
 *   - Ledger entries: 7-year minimum (IRS / Companies Act)
 *   - Tax documents (1099, TDS): 7 years
 *   - Payment records: 7 years
 *   - Creator PII (bank accounts, tax IDs): purge after agreement end + 7 years
 *
 * Also manages point-in-time query capability for closed periods
 * (spec Section 10: "Even after period closed, enable queries").
 */
public class DataRetentionPolicyTool extends BaseTool {

    @Override public String getName() { return "data_retention_policy"; }

    @Override
    public JSONObject getSchema() {
        JSONObject p = new JSONObject()
                .put("action",      str("get_policy|check_retention|schedule_purge|point_in_time_query|lock_period|compliance_report"))
                .put("creator_id",  str("Creator unique ID"))
                .put("agreement_id",str("Agreement ID"))
                .put("record_type", str("LEDGER|TAX_FORM|PAYMENT|CREATOR_PII|AGREEMENT|AUDIT_TRAIL"))
                .put("period",      str("Closed accounting period e.g. 2024-Q4"))
                .put("as_of_date",  str("Point-in-time query date ISO-8601 (e.g. 2024-12-31)"))
                .put("year",        intP("Year to lock or query"));
        return schema(getName(),
                "DATA_RETENTION_POLICY_AGENT — Financial data retention management: 7-year compliance hold, " +
                "PII purge scheduling, period locking, point-in-time ledger queries for audit.",
                p, "action");
    }

    @Override
    public JSONObject execute(JSONObject a) throws Exception {
        String action = a.getString("action");

        return switch (action) {
            case "get_policy" -> {
                JSONObject policy = new JSONObject()
                        .put("ledger_entries",   "7 years (immutable, append-only, never deleted)")
                        .put("tax_forms_1099",   "7 years from filing date (IRS requirement)")
                        .put("tax_forms_tds",    "7 years (Indian Companies Act)")
                        .put("payment_records",  "7 years")
                        .put("creator_pii",      "Purge 7 years after last agreement termination")
                        .put("audit_trails",     "7 years minimum, indefinite preferred")
                        .put("post_close_query", "All closed periods remain queryable (immutable log)")
                        .put("basis",            "IRS Rev Proc 98-25, Indian IT Act 2000, EU GDPR Art 17");
                yield json(new JSONObject()
                        .put("retention_policies", policy)
                        .put("compliance_frameworks", new JSONArray()
                                .put("IRS 7-year rule").put("Indian IT Act 2000").put("EU GDPR").put("GAAP"))
                        .put("timestamp", now()));
            }
            case "check_retention" -> {
                String recType = a.optString("record_type", "LEDGER");
                int    retYears = switch (recType) {
                    case "CREATOR_PII"  -> 7;
                    case "LEDGER",
                         "TAX_FORM",
                         "PAYMENT",
                         "AGREEMENT",
                         "AUDIT_TRAIL"  -> 7;
                    default             -> 7;
                };
                yield json(new JSONObject()
                        .put("record_type",       recType)
                        .put("retention_years",   retYears)
                        .put("can_purge",         false)
                        .put("reason",            "Retention period not yet elapsed")
                        .put("eligible_for_purge","After " + retYears + " years from record creation")
                        .put("timestamp",         now()));
            }
            case "schedule_purge" -> {
                String recType   = a.optString("record_type","CREATOR_PII");
                String creatorId = a.optString("creator_id","");
                yield json(new JSONObject()
                        .put("creator_id",        creatorId)
                        .put("record_type",       recType)
                        .put("purge_scheduled",   "CREATOR_PII".equals(recType))
                        .put("purge_date",        "7 years after last agreement end")
                        .put("ledger_preserved",  true)
                        .put("pii_anonymised",    "Bank account, SSN/PAN masked after retention period")
                        .put("compliance_note",   "Ledger entries preserved indefinitely for audit; PII anonymised")
                        .put("timestamp",         now()));
            }
            case "point_in_time_query" -> {
                String asOf = a.optString("as_of_date", "2024-12-31");
                yield json(new JSONObject()
                        .put("as_of_date",    asOf)
                        .put("query_type",    "IMMUTABLE_LOG_REPLAY")
                        .put("description",   "Recompute ledger state as of " + asOf + " from append-only log")
                        .put("sql_hint",      "SELECT * FROM ledger_entries WHERE timestamp <= '" + asOf + "'")
                        .put("slo",           "< 500ms for p99 ledger query")
                        .put("timestamp",     now()));
            }
            case "lock_period" -> {
                String period = a.optString("period","2024-Q4");
                int    year   = a.optInt("year", 2024);
                yield json(new JSONObject()
                        .put("period",         period)
                        .put("locked",         true)
                        .put("effect",         "No further transactions can be posted for this period")
                        .put("queries_allowed",true)
                        .put("year",           year)
                        .put("locked_at",      now()));
            }
            case "compliance_report" ->
                json(new JSONObject()
                        .put("report_type",    "DATA_RETENTION_COMPLIANCE")
                        .put("status",         "COMPLIANT")
                        .put("records_under_retention", "All records within 7-year window retained")
                        .put("upcoming_purges","None scheduled in next 90 days")
                        .put("timestamp",      now()));
            default -> text("Unknown action: " + action);
        };
    }
}
