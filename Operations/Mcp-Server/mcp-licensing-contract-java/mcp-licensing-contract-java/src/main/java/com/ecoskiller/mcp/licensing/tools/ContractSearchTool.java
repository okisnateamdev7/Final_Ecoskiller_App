package com.ecoskiller.mcp.licensing.tools;

import com.ecoskiller.mcp.licensing.security.SecurityManager;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Set;

public class ContractSearchTool extends BaseTool {
    private static final Set<String> VALID_STATUSES = Set.of(
        "DRAFT","PENDING_REVIEW","PENDING_SIGNATURE","ACTIVE","SUSPENDED","TERMINATED");

    public ContractSearchTool(SecurityManager security) { super(security); }

    @Override
    public String execute(JSONObject args) throws Exception {
        security.requireRole(args.optString("jwt_token"), SecurityManager.ROLE_VIEWER, SecurityManager.ROLE_CREATE, SecurityManager.ROLE_ADMIN);

        String status     = args.optString("status", "");
        if (!status.isEmpty() && !VALID_STATUSES.contains(status)) {
            throw new IllegalArgumentException("status must be one of: " + VALID_STATUSES);
        }
        String ideaId     = args.optString("idea_id", "");
        String candidateId= args.optString("candidate_id", "");
        String bizId      = args.optString("licensee_business_id", "");
        String tenantId   = security.sanitiseText(opt(args, "tenant_id", ""), "tenant_id");
        String dateFrom   = security.sanitiseText(opt(args, "date_from", ""), "date_from");
        String dateTo     = security.sanitiseText(opt(args, "date_to", ""), "date_to");
        int    page       = Math.max(1, args.optInt("page", 1));
        int    pageSize   = Math.min(100, Math.max(1, args.optInt("page_size", 20)));

        LOG.info("[ContractSearch] status=" + status + " page=" + page + " pageSize=" + pageSize);

        // In production: SELECT from royalty.contracts with RLS (tenant_id scoped), pagination
        JSONArray results = new JSONArray()
            .put(new JSONObject()
                .put("contract_id",     newUuid())
                .put("current_state",   "ACTIVE")
                .put("royalty_rate",    0.0003)
                .put("term_years",      12)
                .put("version_count",   2)
                .put("created_at",      nowIso())
                .put("note",            "[Full document not returned — use contract_lifecycle get for pre-signed URL]"));

        return ok(new JSONObject()
            .put("results",     results)
            .put("page",        page)
            .put("page_size",   pageSize)
            .put("total_count", 1)
            .put("filters_applied", new JSONObject()
                .put("status",    status.isEmpty() ? "all" : status)
                .put("tenant_id", tenantId.isEmpty() ? "all" : tenantId))
            .put("security_note", "Document content excluded from search results. Pre-signed MinIO URLs issued per-request via contract_lifecycle.get to prevent logging of sensitive content.")
            .put("simulation",  simulationNote()));
    }
}
