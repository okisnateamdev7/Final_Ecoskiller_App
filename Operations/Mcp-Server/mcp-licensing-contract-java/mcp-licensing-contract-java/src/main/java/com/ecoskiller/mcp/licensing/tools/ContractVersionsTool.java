package com.ecoskiller.mcp.licensing.tools;

import com.ecoskiller.mcp.licensing.security.SecurityManager;
import org.json.JSONArray;
import org.json.JSONObject;

public class ContractVersionsTool extends BaseTool {
    public ContractVersionsTool(SecurityManager security) { super(security); }

    @Override
    public String execute(JSONObject args) throws Exception {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_VIEWER, SecurityManager.ROLE_CREATE, SecurityManager.ROLE_ADMIN);
        String contractId  = security.requireUuid(args.optString("contract_id"), "contract_id");
        int    versionNum  = args.optInt("version_number", -1);

        LOG.info("[ContractVersions] contractId=" + contractId + " version=" + versionNum);

        // In production: SELECT from royalty.contract_versions WHERE contract_id = ?
        JSONArray versions = new JSONArray()
            .put(new JSONObject()
                .put("version_number",       1)
                .put("contract_status",      "DRAFT")
                .put("royalty_rate",         0.0002)
                .put("term_years",           10)
                .put("changed_by",           "system")
                .put("change_reason",        "Initial contract creation")
                .put("version_document_url", "[MinIO pre-signed URL — expires 15 min]")
                .put("created_at",           nowIso()))
            .put(new JSONObject()
                .put("version_number",       2)
                .put("contract_status",      "ACTIVE")
                .put("royalty_rate",         0.0003)
                .put("term_years",           12)
                .put("changed_by",           "actor-uuid")
                .put("change_reason",        "Rate renegotiated — approved by both parties")
                .put("changed_fields",       new JSONObject().put("royalty_rate", "0.0002 -> 0.0003").put("term_years", "10 -> 12"))
                .put("version_document_url", "[MinIO pre-signed URL — expires 15 min]")
                .put("created_at",           nowIso()));

        JSONObject payload = new JSONObject()
            .put("contract_id",  contractId)
            .put("total_versions", versions.length())
            .put("simulation",   simulationNote());

        if (versionNum > 0) {
            payload.put("version", versions.length() >= versionNum ? versions.getJSONObject(versionNum - 1) : JSONObject.NULL);
        } else {
            payload.put("versions", versions);
        }

        return ok(payload);
    }
}
