package com.ecoskiller.mcp.corporate.tools;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * DMS_AGENT_ANTIGRAVITY_ERP
 *
 * <p>Document Management System. Permanent deletion requires dual
 * authorisation (ANTIGRAVITY_SEALED constraint).</p>
 */
@Slf4j
@Component
public class DmsTool implements AgentTool {

    @Override
    public String execute(JsonNode args) throws Exception {
        String tenantId     = getString(args, "tenant_id");
        String action       = getString(args, "action");
        String docId        = getString(args, "doc_id");
        String authoriserId = getString(args, "authoriser_id");

        log.info("[DMS] tenant={} action={} doc={}", tenantId, action, docId);

        if ("delete".equals(action) && authoriserId.isBlank()) {
            return sealed("Permanent document deletion requires authoriser_id " +
                          "(dual-authorisation policy). All deletions are audit-logged.");
        }

        return switch (action) {
            case "upload_document"    -> ok("upload_document",
                "Document uploaded.\ndoc_id  : " + UUID.randomUUID() +
                "\nversion : 1");
            case "update_metadata"    -> ok("update_metadata",
                "Document metadata updated for doc_id=" + docId);
            case "get_document"       -> ok("get_document",
                "Document URL: /api/dms/" + docId + "/download");
            case "new_version"        -> ok("new_version",
                "New version created.\ndoc_id  : " + docId +
                "\nversion : N+1");
            case "search"             -> ok("search",
                "Search complete. 0 documents matched (demo — index your content).");
            case "tag_document"       -> ok("tag_document",
                "Tags applied to doc_id=" + docId);
            case "set_retention_policy" -> ok("set_retention_policy",
                "Retention policy set for doc_id=" + docId);
            case "archive"            -> ok("archive",
                "Document archived. No longer visible in active views.");
            case "delete"             -> ok("delete",
                "Document permanently deleted.\nAuthorised by: " + authoriserId +
                "\nAudit record created.");
            case "get_audit_trail"    -> ok("get_audit_trail",
                "Audit trail: /api/dms/" + docId + "/audit");
            default -> "Unknown action: " + action;
        };
    }
}
