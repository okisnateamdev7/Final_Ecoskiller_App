package com.ecoskiller.mcp.licensing.tools;

import com.ecoskiller.mcp.licensing.security.SecurityManager;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Set;

public class ComplianceAuditTool extends BaseTool {
    private static final Set<String> VALID_ACTIONS = Set.of(
        "CONTRACT_CREATED","STATE_TRANSITIONED","AMENDED","SIGNED","TERMINATED","ARCHIVED");

    public ComplianceAuditTool(SecurityManager security) { super(security); }

    @Override
    public String execute(JSONObject args) throws Exception {
        return switch (require(args, "action")) {
            case "log_event"           -> logEvent(args);
            case "query_audit_trail"   -> queryAuditTrail(args);
            case "check_worm_archive"  -> checkWormArchive(args);
            case "trigger_archive"     -> triggerArchive(args);
            default -> throw new IllegalArgumentException("Unknown action. Supported: log_event | query_audit_trail | check_worm_archive | trigger_archive");
        };
    }

    private String logEvent(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_CREATE, SecurityManager.ROLE_ADMIN);
        String contractId    = security.requireUuid(args.optString("contract_id"), "contract_id");
        String actorId       = security.sanitiseText(require(args, "actor_id"), "actor_id");
        String auditAction   = security.sanitiseText(require(args, "audit_action"), "audit_action");
        if (!VALID_ACTIONS.contains(auditAction)) {
            throw new IllegalArgumentException("audit_action must be one of: " + VALID_ACTIONS);
        }
        String prevState     = security.sanitiseText(opt(args, "previous_state", ""), "previous_state");
        String newState      = security.sanitiseText(opt(args, "new_state", ""), "new_state");
        String metadataJson  = security.sanitiseText(opt(args, "metadata_json", "{}"), "metadata_json");

        LOG.info("[ComplianceAudit] log_event contractId=" + contractId + " action=" + auditAction);

        // In production: INSERT into ClickHouse royalty_audit_log
        String eventId = newUuid();
        return ok(new JSONObject()
            .put("event_id",        eventId)
            .put("contract_id",     contractId)
            .put("actor_id",        actorId)
            .put("audit_action",    auditAction)
            .put("previous_state",  prevState)
            .put("new_state",       newState)
            .put("event_ts",        nowIso())
            .put("storage",         "ClickHouse royalty_audit_log (append-only, immutable)")
            .put("simulation",      simulationNote()));
    }

    private String queryAuditTrail(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_ADMIN);
        String contractId = security.requireUuid(args.optString("contract_id"), "contract_id");
        String dateFrom   = security.sanitiseText(opt(args, "date_from", ""), "date_from");
        String dateTo     = security.sanitiseText(opt(args, "date_to", ""), "date_to");

        JSONArray events = new JSONArray()
            .put(new JSONObject().put("audit_action","CONTRACT_CREATED").put("actor_id","system").put("event_ts", nowIso()))
            .put(new JSONObject().put("audit_action","STATE_TRANSITIONED").put("previous_state","DRAFT").put("new_state","PENDING_REVIEW").put("event_ts", nowIso()))
            .put(new JSONObject().put("audit_action","SIGNED").put("actor_id","candidate-uuid").put("event_ts", nowIso()))
            .put(new JSONObject().put("audit_action","ARCHIVED").put("actor_id","system").put("event_ts", nowIso()));

        return ok(new JSONObject()
            .put("contract_id",  contractId)
            .put("events",       events)
            .put("total_events", events.length())
            .put("query_engine", "ClickHouse royalty_audit_log")
            .put("simulation",   simulationNote()));
    }

    private String checkWormArchive(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_VIEWER, SecurityManager.ROLE_ADMIN);
        String contractId = security.requireUuid(args.optString("contract_id"), "contract_id");

        return ok(new JSONObject()
            .put("contract_id",       contractId)
            .put("archive_status",    "ARCHIVED")
            .put("archive_url",       "minio://ecoskiller-royalty-contracts/" + contractId + ".pdf")
            .put("object_lock_mode",  "GOVERNANCE")
            .put("retention_years",   15)
            .put("retention_until",   "2041-01-01")
            .put("archive_etag",      "[MinIO ETag of immutable object]")
            .put("compliance",        "India IT Act 2000 — 15-year electronic record retention + GST audit compliance")
            .put("simulation",        simulationNote()));
    }

    private String triggerArchive(JSONObject args) {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_ADMIN);
        String contractId = security.requireUuid(args.optString("contract_id"), "contract_id");
        String tenantId   = security.sanitiseText(opt(args, "tenant_id", "default"), "tenant_id");

        LOG.info("[ComplianceAudit] trigger_archive contractId=" + contractId);

        return ok(new JSONObject()
            .put("contract_id",       contractId)
            .put("tenant_id",         tenantId)
            .put("kafka_published",   "archive.requested -> Immutable Archive Service")
            .put("payload",           new JSONObject()
                .put("contract_id",       contractId)
                .put("tenant_id",         tenantId)
                .put("retention_years",   15)
                .put("object_lock_mode",  "GOVERNANCE"))
            .put("archive_bucket",    "ecoskiller-royalty-contracts-" + tenantId)
            .put("simulation",        simulationNote()));
    }
}
