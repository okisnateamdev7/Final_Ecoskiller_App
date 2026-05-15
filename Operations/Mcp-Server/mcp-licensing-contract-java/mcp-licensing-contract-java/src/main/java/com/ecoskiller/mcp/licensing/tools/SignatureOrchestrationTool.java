package com.ecoskiller.mcp.licensing.tools;

import com.ecoskiller.mcp.licensing.security.SecurityManager;
import org.json.JSONArray;
import org.json.JSONObject;

public class SignatureOrchestrationTool extends BaseTool {
    public SignatureOrchestrationTool(SecurityManager security) { super(security); }

    @Override
    public String execute(JSONObject args) throws Exception {
        return switch (require(args, "action")) {
            case "request_signatures" -> requestSignatures(args);
            case "get_status"         -> getStatus(args);
            case "record_signed"      -> recordSigned(args);
            case "handle_timeout"     -> handleTimeout(args);
            default -> throw new IllegalArgumentException("Unknown action. Supported: request_signatures | get_status | record_signed | handle_timeout");
        };
    }

    private String requestSignatures(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_CREATE, SecurityManager.ROLE_ADMIN);
        String contractId      = security.requireUuid(args.optString("contract_id"), "contract_id");
        String documentUrl     = security.sanitiseText(opt(args, "document_url", "[MinIO pre-signed URL]"), "document_url");
        String deadline        = security.sanitiseText(opt(args, "signature_deadline", "7 days from now"), "signature_deadline");

        JSONArray signatories = new JSONArray()
            .put(new JSONObject().put("role", "candidate").put("status", "PENDING"))
            .put(new JSONObject().put("role", "business_representative").put("status", "PENDING"));

        LOG.info("[Signature] request_signatures contractId=" + contractId);

        return ok(new JSONObject()
            .put("contract_id",        contractId)
            .put("document_url",       documentUrl)
            .put("signatories",        signatories)
            .put("signature_deadline", deadline)
            .put("signature_ttl_days", System.getenv("SIGNATURE_TTL_DAYS") != null ? System.getenv("SIGNATURE_TTL_DAYS") : "7")
            .put("kafka_published",    "signature.requested -> Digital Signature Service")
            .put("tracking_table",     "royalty.contract_signatures (rows inserted per signatory)")
            .put("simulation",         simulationNote()));
    }

    private String getStatus(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_VIEWER, SecurityManager.ROLE_CREATE, SecurityManager.ROLE_ADMIN);
        String contractId = security.requireUuid(args.optString("contract_id"), "contract_id");

        return ok(new JSONObject()
            .put("contract_id", contractId)
            .put("overall_status", "PARTIALLY_SIGNED")
            .put("signatories", new JSONArray()
                .put(new JSONObject().put("role","candidate").put("status","SIGNED").put("signed_at", nowIso()))
                .put(new JSONObject().put("role","business_representative").put("status","PENDING").put("deadline", nowIso())))
            .put("all_parties_signed", false)
            .put("simulation", simulationNote()));
    }

    private String recordSigned(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_CREATE, SecurityManager.ROLE_ADMIN);
        String contractId    = security.requireUuid(args.optString("contract_id"), "contract_id");
        String signerId      = security.sanitiseText(require(args, "signer_id"), "signer_id");
        String signerRole    = security.sanitiseText(require(args, "signer_role"), "signer_role");
        String sigHash       = security.sanitiseText(require(args, "signature_hash"), "signature_hash");

        LOG.info("[Signature] record_signed contractId=" + contractId + " signerRole=" + signerRole);

        // In production: UPDATE royalty.contract_signatures SET status=SIGNED, signature_hash=?, signed_at=NOW()
        // If all_parties_signed: advance contract to ACTIVE, publish licensing.contract.signed

        return ok(new JSONObject()
            .put("contract_id",     contractId)
            .put("signer_id",       signerId)
            .put("signer_role",     signerRole)
            .put("signature_hash",  sigHash)
            .put("signed_at",       nowIso())
            .put("all_parties_signed", false)
            .put("note",            "When all_parties_signed=true: contract advances to ACTIVE and licensing.contract.signed is published")
            .put("simulation",      simulationNote()));
    }

    private String handleTimeout(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_ADMIN);
        String contractId = security.requireUuid(args.optString("contract_id"), "contract_id");

        LOG.info("[Signature] handle_timeout contractId=" + contractId);

        return ok(new JSONObject()
            .put("contract_id",           contractId)
            .put("timeout_action",        "Contract reverted to PENDING_REVIEW")
            .put("kafka_published",       "licensing.contract.signature.timeout -> notification-service + admin-service")
            .put("timed_out_at",          nowIso())
            .put("pending_signatories",   new JSONArray().put("business_representative"))
            .put("simulation",            simulationNote()));
    }
}
