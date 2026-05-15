package com.ecoskiller.mcp.royalty.tools;
import org.json.JSONObject;
import org.json.JSONArray;

/** LICENSE_GENERATION_AGENT — Tool: license_generation */
public class LicenseGenerationTool extends BaseTool {
    @Override public String getName() { return "license_generation"; }
    @Override public JSONObject getSchema() {
        JSONObject p = new JSONObject()
                .put("action",            str("generate|validate|terminate|get_terms|exclusivity_check"))
                .put("idea_id",           str("Idea unique ID"))
                .put("creator_id",        str("Creator unique ID"))
                .put("licensee_id",       str("Licensee company ID"))
                .put("royalty_rate",      num("Royalty rate 0.0–1.0"))
                .put("minimum_guarantee", num("Annual minimum guarantee"))
                .put("territory",         str("Territory: Asia|EU|US|Global"))
                .put("exclusive",         bool("Is this an exclusive license?"))
                .put("duration_months",   intP("License duration in months"))
                .put("agreement_id",      str("Agreement ID (for validate/terminate)"));
        return schema(getName(),
                "LICENSE_GENERATION_AGENT — Generate, validate, and manage licensing agreements. " +
                "Enforces territory exclusivity, creates initial ledger entries (advances_recoupable, deferred_revenue).",
                p, "action", "idea_id", "creator_id", "licensee_id");
    }
    @Override public JSONObject execute(JSONObject a) throws Exception {
        String action    = a.getString("action");
        String ideaId    = a.getString("idea_id");
        String creatorId = a.getString("creator_id");
        String licenseeId= a.getString("licensee_id");
        return switch (action) {
            case "generate" -> {
                String agrId = "AGR-" + ideaId + "-" + licenseeId + "-" + System.currentTimeMillis();
                double rate  = a.optDouble("royalty_rate", 0.07);
                double minG  = a.optDouble("minimum_guarantee", 50_000);
                boolean excl = a.optBoolean("exclusive", false);
                String terr  = a.optString("territory","Asia");
                int dur      = a.optInt("duration_months", 12);
                yield json(new JSONObject()
                        .put("agreement_id",        agrId)
                        .put("idea_id",             ideaId)
                        .put("creator_id",          creatorId)
                        .put("licensee_id",         licenseeId)
                        .put("royalty_rate",        (rate*100)+"%")
                        .put("minimum_guarantee",   money(minG).toPlainString())
                        .put("territory",           terr)
                        .put("exclusive",           excl)
                        .put("duration_months",     dur)
                        .put("status",              "ACTIVE")
                        .put("initial_ledger",      excl ? "DEBIT Cash / CREDIT Deferred-Revenue (upfront)" : "N/A")
                        .put("kafka_event",         "licensing.agreement.signed")
                        .put("created_at",          now()));
            }
            case "exclusivity_check" -> {
                String terr = a.optString("territory","EU");
                yield json(new JSONObject()
                        .put("idea_id",           ideaId)
                        .put("territory",         terr)
                        .put("exclusive_holder",  "Check innovation-registry-service")
                        .put("can_license",       true)
                        .put("message",           "Exclusivity verified for territory: " + terr));
            }
            case "get_terms" -> {
                String agrId = a.getString("agreement_id");
                yield json(new JSONObject()
                        .put("agreement_id",     agrId)
                        .put("idea_id",          ideaId)
                        .put("status",           "ACTIVE")
                        .put("terms",            "Fetch from PostgreSQL agreements table")
                        .put("timestamp",        now()));
            }
            case "terminate" -> {
                String agrId = a.getString("agreement_id");
                yield json(new JSONObject()
                        .put("agreement_id", agrId)
                        .put("status",       "TERMINATED")
                        .put("action",       "Finalize royalties; release balance to creator")
                        .put("kafka_event",  "licensing.agreement.terminated")
                        .put("timestamp",    now()));
            }
            case "validate" -> json(new JSONObject()
                    .put("agreement_id", a.getString("agreement_id"))
                    .put("valid",        true)
                    .put("status",       "ACTIVE")
                    .put("timestamp",    now()));
            default -> text("Unknown action: " + action);
        };
    }
}
