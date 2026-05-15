package com.ecoskiller.mcp.licensing.tools;

import com.ecoskiller.mcp.licensing.security.SecurityManager;
import org.json.JSONObject;

public class ContractTerminationTool extends BaseTool {
    public ContractTerminationTool(SecurityManager security) { super(security); }

    @Override
    public String execute(JSONObject args) throws Exception {
        return switch (require(args, "action")) {
            case "initiate"      -> initiate(args);
            case "check_status"  -> checkStatus(args);
            default -> throw new IllegalArgumentException("Unknown action. Supported: initiate | check_status");
        };
    }

    private String initiate(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_ADMIN);
        String contractId       = security.requireUuid(args.optString("contract_id"), "contract_id");
        String terminatingParty = security.sanitiseText(require(args, "terminating_party_id"), "terminating_party_id");
        String reason           = security.sanitiseText(require(args, "termination_reason"), "termination_reason");
        String effectiveDate    = security.sanitiseText(require(args, "effective_date"), "effective_date");

        LOG.info("[Termination] initiate contractId=" + contractId + " party=" + terminatingParty);

        // In production:
        // 1. REST call to Royalty Audit & Compliance Service to validate minimum royalty compliance
        // 2. If compliant: advance state to TERMINATED, publish licensing.contract.terminated
        // 3. If non-compliant: return 403 with compliance violation details

        String terminationId = newUuid();
        return ok(new JSONObject()
            .put("termination_id",                terminationId)
            .put("contract_id",                   contractId)
            .put("termination_status",            "PENDING_COMPLIANCE_CHECK")
            .put("terminating_party_id",          terminatingParty)
            .put("termination_reason",            reason)
            .put("requested_effective_date",      effectiveDate)
            .put("estimated_resolution_ts",       nowIso())
            .put("compliance_check_service",      "Royalty Audit & Compliance Service (async REST)")
            .put("kafka_on_approval",             "licensing.contract.terminated -> Royalty Wallet Service + Royalty Accounting Engine")
            .put("simulation",                    simulationNote()));
    }

    private String checkStatus(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_VIEWER, SecurityManager.ROLE_ADMIN);
        String contractId = security.requireUuid(args.optString("contract_id"), "contract_id");

        return ok(new JSONObject()
            .put("contract_id",          contractId)
            .put("termination_status",   "PENDING_COMPLIANCE_CHECK")
            .put("compliance_result",    "PENDING")
            .put("checked_at",           nowIso())
            .put("simulation",           simulationNote()));
    }
}
