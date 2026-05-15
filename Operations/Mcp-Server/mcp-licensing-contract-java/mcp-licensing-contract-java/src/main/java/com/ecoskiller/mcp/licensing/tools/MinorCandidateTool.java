package com.ecoskiller.mcp.licensing.tools;

import com.ecoskiller.mcp.licensing.security.SecurityManager;
import org.json.JSONObject;

public class MinorCandidateTool extends BaseTool {
    public MinorCandidateTool(SecurityManager security) { super(security); }

    @Override
    public String execute(JSONObject args) throws Exception {
        return switch (require(args, "action")) {
            case "check_minor_status"          -> checkMinorStatus(args);
            case "record_guardian_consent"     -> recordGuardianConsent(args);
            case "trigger_ownership_transfer"  -> triggerOwnershipTransfer(args);
            default -> throw new IllegalArgumentException("Unknown action. Supported: check_minor_status | record_guardian_consent | trigger_ownership_transfer");
        };
    }

    private String checkMinorStatus(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_CREATE, SecurityManager.ROLE_ADMIN);
        String candidateId = security.requireUuid(args.optString("candidate_id"), "candidate_id");

        // In production: check minor_flag_cache:{candidateId} in Redis (TTL 1hr)
        // On cache miss: REST call to user-service for age flag
        boolean isMinor = false; // Simulated

        LOG.info("[Minor] check_minor_status candidateId=" + candidateId + " isMinor=" + isMinor);

        return ok(new JSONObject()
            .put("candidate_id",           candidateId)
            .put("is_minor",               isMinor)
            .put("guardian_consent_required", isMinor)
            .put("cache_key",              "minor_flag_cache:" + candidateId)
            .put("cache_ttl_seconds",      3600)
            .put("data_source",            isMinor ? "user-service age flag" : "Redis cache")
            .put("legal_basis",            "Indian Contract Act 2013, Section 11 — competency to contract")
            .put("simulation",             simulationNote()));
    }

    private String recordGuardianConsent(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_CREATE, SecurityManager.ROLE_ADMIN);
        String candidateId        = security.requireUuid(args.optString("candidate_id"), "candidate_id");
        String contractId         = security.requireUuid(args.optString("contract_id"), "contract_id");
        String guardianId         = security.sanitiseText(require(args, "guardian_id"), "guardian_id");
        String consentReferenceId = security.sanitiseText(require(args, "consent_reference_id"), "consent_reference_id");
        String tenantId           = security.sanitiseText(opt(args, "tenant_id", "default"), "tenant_id");

        LOG.info("[Minor] record_guardian_consent candidateId=" + candidateId + " contractId=" + contractId);

        // In production:
        // 1. Validate consentReferenceId with Innovation Trust Governance Service
        // 2. UPDATE royalty.contracts SET guardian_consent_required=false, consent_reference_id=?, guardian_co_signatory_id=?
        // 3. Add guardian as a signatory in royalty.contract_signatures
        // 4. Contract can now advance from DRAFT to PENDING_REVIEW

        return ok(new JSONObject()
            .put("candidate_id",             candidateId)
            .put("contract_id",              contractId)
            .put("guardian_id",              guardianId)
            .put("consent_reference_id",     consentReferenceId)
            .put("tenant_id",                tenantId)
            .put("consent_recorded_at",      nowIso())
            .put("contract_unblocked",       true)
            .put("dpdpa_compliance",         "Consent reference stored per DPDPA 2023 requirements")
            .put("guardian_signatory_added", true)
            .put("note",                     "Contract can now advance from DRAFT to PENDING_REVIEW")
            .put("simulation",               simulationNote()));
    }

    private String triggerOwnershipTransfer(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_ADMIN);
        String candidateId = security.requireUuid(args.optString("candidate_id"), "candidate_id");
        String tenantId    = security.sanitiseText(opt(args, "tenant_id", "default"), "tenant_id");

        LOG.info("[Minor] trigger_ownership_transfer candidateId=" + candidateId);

        // In production: triggered by Apache Airflow on candidate's 18th birthday
        // Removes guardian co-signatory requirement from future amendments
        // Publishes licensing.ownership.transferred Kafka event

        return ok(new JSONObject()
            .put("candidate_id",                 candidateId)
            .put("tenant_id",                    tenantId)
            .put("transfer_effective_date",      nowIso())
            .put("guardian_requirement_removed", true)
            .put("kafka_published",              "licensing.ownership.transferred -> Innovation Trust Governance + notification-service + user-service")
            .put("affected_contracts",           "[In production: all ACTIVE contracts for this candidateId]")
            .put("legal_basis",                  "Indian Contract Act Section 11 — candidate now legally competent to contract independently")
            .put("airflow_trigger",              "licensing.ownership.transfer.due scheduled job")
            .put("simulation",                   simulationNote()));
    }
}
