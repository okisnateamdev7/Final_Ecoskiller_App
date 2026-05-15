package com.ecoskiller.mcp.licensing.tools;

import com.ecoskiller.mcp.licensing.security.SecurityManager;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class ContractLifecycleTool extends BaseTool {

    private static final Map<String, Set<String>> TRANSITIONS = Map.of(
        "DRAFT",             Set.of("PENDING_REVIEW"),
        "PENDING_REVIEW",    Set.of("PENDING_SIGNATURE", "DRAFT"),
        "PENDING_SIGNATURE", Set.of("ACTIVE", "PENDING_REVIEW"),
        "ACTIVE",            Set.of("SUSPENDED", "TERMINATED", "PENDING_SIGNATURE"),
        "SUSPENDED",         Set.of("ACTIVE", "TERMINATED"),
        "TERMINATED",        Set.of()
    );

    private static final Set<String> TERMINAL = Set.of("TERMINATED");
    private static final Set<String> VALID_TERRITORIAL = Set.of("national", "regional", "global");
    private static final Set<String> VALID_USAGE = Set.of("exclusive", "non-exclusive");

    public ContractLifecycleTool(SecurityManager security) { super(security); }

    @Override
    public String execute(JSONObject args) throws Exception {
        return switch (require(args, "action")) {
            case "create"        -> create(args);
            case "get"           -> get(args);
            case "advance_state" -> advanceState(args);
            default -> throw new IllegalArgumentException("Unknown action. Supported: create | get | advance_state");
        };
    }

    private String create(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_CREATE);
        String ideaId     = security.requireUuid(args.optString("idea_id"), "idea_id");
        String bizId      = security.requireUuid(args.optString("licensee_business_id"), "licensee_business_id");
        String candidateId= security.requireUuid(args.optString("idea_owner_candidate_id"), "idea_owner_candidate_id");
        String tenantId   = security.sanitiseText(require(args, "tenant_id"), "tenant_id");
        double rate       = security.requireValidRoyaltyRate(args.optDouble("proposed_royalty_rate", -1));
        int    years      = security.requireValidTermYears(args.optInt("proposed_term_years", -1));
        String territorial= opt(args, "territorial_scope", "national");
        if (!VALID_TERRITORIAL.contains(territorial)) throw new IllegalArgumentException("territorial_scope must be: national|regional|global");
        String usage      = opt(args, "usage_scope", "non-exclusive");
        if (!VALID_USAGE.contains(usage)) throw new IllegalArgumentException("usage_scope must be: exclusive|non-exclusive");
        String gstin      = security.sanitiseGstin(args.optString("licensee_gstin", null), "licensee_gstin");
        String sac        = security.sanitiseText(opt(args, "hsn_sac_code", "9973"), "hsn_sac_code");
        String contractId = newUuid();
        LocalDate start   = LocalDate.now();
        LocalDate end     = start.plusYears(years);

        LOG.info("[ContractLifecycle] create contractId=" + contractId + " tenant=" + tenantId);

        JSONObject contract = new JSONObject()
            .put("contract_id",               contractId)
            .put("contract_status",           "DRAFT")
            .put("version_number",            1)
            .put("idea_id",                   ideaId)
            .put("idea_owner_candidate_id",   candidateId)
            .put("licensee_business_id",      bizId)
            .put("tenant_id",                 tenantId)
            .put("royalty_rate",              rate)
            .put("royalty_rate_percent",      String.format("%.4f%%", rate * 100))
            .put("term_years",                years)
            .put("contract_start_date",       start.toString())
            .put("contract_end_date",         end.toString())
            .put("renewal_eligible_from",     end.minusMonths(6).toString())
            .put("territorial_scope",         territorial)
            .put("usage_scope",               usage)
            .put("licensee_gstin",            gstin != null ? gstin : JSONObject.NULL)
            .put("hsn_sac_code",              sac)
            .put("royalty_nature",            "IPR_LICENSING")
            .put("document_generation_status","PENDING")
            .put("created_at",                nowIso())
            .put("initiated_by",              security.extractActorId(args.optString("jwt_token")));

        return ok(new JSONObject()
            .put("contract",        contract)
            .put("kafka_published", "document.generation.requested")
            .put("next_step",       "Await document.generated from Legal Document Generation Service")
            .put("simulation",      simulationNote()));
    }

    private String get(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_VIEWER, SecurityManager.ROLE_CREATE, SecurityManager.ROLE_ADMIN);
        String contractId = security.requireUuid(args.optString("contract_id"), "contract_id");
        LOG.info("[ContractLifecycle] get contractId=" + contractId);

        JSONObject contract = new JSONObject()
            .put("contract_id",       contractId)
            .put("contract_status",   "ACTIVE")
            .put("version_number",    2)
            .put("royalty_rate",      0.0003)
            .put("term_years",        12)
            .put("territorial_scope", "national")
            .put("usage_scope",       "non-exclusive")
            .put("signature_status",  new JSONObject()
                .put("candidate",             "SIGNED")
                .put("business_representative","SIGNED")
                .put("guardian",              "N/A"))
            .put("document_url",      "[15-min pre-signed MinIO URL — generated per request]")
            .put("archive_url",       "minio://ecoskiller-royalty-contracts/" + contractId + ".pdf")
            .put("signed_at",         nowIso())
            .put("created_at",        nowIso());

        return ok(new JSONObject().put("contract", contract).put("simulation", simulationNote()));
    }

    private String advanceState(JSONObject args) {
        String contractId = security.requireUuid(args.optString("contract_id"), "contract_id");
        String newState   = security.sanitiseText(require(args, "new_state"), "new_state");
        String actorId    = security.sanitiseText(require(args, "actor_id"), "actor_id");
        String reason     = security.sanitiseText(opt(args, "reason", "No reason provided"), "reason");

        if (TERMINAL.contains(newState)) {
            security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_ADMIN);
        } else {
            security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_CREATE, SecurityManager.ROLE_ADMIN);
        }

        String currentState = "ACTIVE"; // In production: read from PostgreSQL
        Set<String> allowed = TRANSITIONS.getOrDefault(currentState, Set.of());
        if (!allowed.contains(newState)) {
            throw new IllegalArgumentException("Invalid transition: " + currentState + " -> " + newState + ". Allowed: " + allowed);
        }

        LOG.info("[ContractLifecycle] advance_state " + currentState + " -> " + newState + " contractId=" + contractId);

        String kafka = switch (newState) {
            case "ACTIVE"     -> "licensing.contract.signed + archive.requested";
            case "TERMINATED" -> "licensing.contract.terminated";
            default           -> "internal state update only";
        };

        return ok(new JSONObject()
            .put("contract_id",     contractId)
            .put("previous_state",  currentState)
            .put("new_state",       newState)
            .put("transitioned_by", actorId)
            .put("reason",          reason)
            .put("transitioned_at", nowIso())
            .put("kafka_published", kafka)
            .put("audit_logged",    "STATE_TRANSITIONED -> ClickHouse royalty_audit_log")
            .put("simulation",      simulationNote()));
    }
}
