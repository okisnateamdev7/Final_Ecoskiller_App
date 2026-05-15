package com.ecoskiller.mcp.licensing.tools;

import com.ecoskiller.mcp.licensing.security.SecurityManager;
import org.json.JSONArray;
import org.json.JSONObject;

public class ContractAmendmentTool extends BaseTool {
    public ContractAmendmentTool(SecurityManager security) { super(security); }

    @Override
    public String execute(JSONObject args) throws Exception {
        return switch (require(args, "action")) {
            case "submit_amendment" -> submit(args);
            case "get_amendments"   -> getAmendments(args);
            default -> throw new IllegalArgumentException("Unknown action. Supported: submit_amendment | get_amendments");
        };
    }

    private String submit(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_CREATE);
        String contractId  = security.requireUuid(args.optString("contract_id"), "contract_id");
        String actorId     = security.sanitiseText(require(args, "actor_id"), "actor_id");
        String reason      = security.sanitiseText(require(args, "amendment_reason"), "amendment_reason");
        boolean bothConsent= args.optBoolean("both_party_consent", false);

        if (!bothConsent) throw new IllegalArgumentException("both_party_consent must be true for amendments");

        JSONObject amended = new JSONObject();
        if (args.has("amended_royalty_rate")) {
            double rate = security.requireValidRoyaltyRate(args.getDouble("amended_royalty_rate"));
            amended.put("royalty_rate", rate);
        }
        if (args.has("amended_term_years")) {
            int years = security.requireValidTermYears(args.getInt("amended_term_years"));
            amended.put("term_years", years);
        }
        if (args.has("amended_territorial_scope")) {
            amended.put("territorial_scope", security.sanitiseText(args.getString("amended_territorial_scope"), "territorial_scope"));
        }
        if (amended.isEmpty()) throw new IllegalArgumentException("At least one field must be amended");

        String amendmentId  = newUuid();
        int    newVersion   = 3; // In production: SELECT MAX(version_number) + 1 FROM royalty.contract_versions
        LOG.info("[Amendment] submit contractId=" + contractId + " amendmentId=" + amendmentId);

        return ok(new JSONObject()
            .put("amendment_id",      amendmentId)
            .put("contract_id",       contractId)
            .put("new_version_number",newVersion)
            .put("amended_fields",    amended)
            .put("amendment_reason",  reason)
            .put("amended_by",        actorId)
            .put("amended_at",        nowIso())
            .put("contract_state",    "PENDING_SIGNATURE")
            .put("kafka_published",   "licensing.contract.amended + signature.requested")
            .put("next_step",         "Both parties must re-sign within 7 days")
            .put("simulation",        simulationNote()));
    }

    private String getAmendments(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_VIEWER, SecurityManager.ROLE_CREATE, SecurityManager.ROLE_ADMIN);
        String contractId = security.requireUuid(args.optString("contract_id"), "contract_id");

        JSONArray versions = new JSONArray()
            .put(new JSONObject()
                .put("version_number", 1).put("change_type", "INITIAL_CREATION")
                .put("changed_by", "system").put("created_at", nowIso()))
            .put(new JSONObject()
                .put("version_number", 2).put("change_type", "RATE_AMENDMENT")
                .put("changed_fields", new JSONObject().put("royalty_rate", 0.0003))
                .put("changed_by", "actor-uuid").put("change_reason", "Renegotiated rate").put("created_at", nowIso()));

        return ok(new JSONObject()
            .put("contract_id",  contractId)
            .put("total_versions", versions.length())
            .put("versions",     versions)
            .put("simulation",   simulationNote()));
    }
}
