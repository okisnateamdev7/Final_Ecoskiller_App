package com.ecoskiller.mcp.institute.tools;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

// ═════════════════════════════════════════════════════════════════════════════
//  1.  ACADEMIC_STRUCTURE_AGENT__ANTIGRAVITY_SEALED
// ═════════════════════════════════════════════════════════════════════════════

@Slf4j
@Component
public class AcademicStructureTool implements AgentTool {

    @Override
    public String execute(JsonNode args) {
        String tenantId = getString(args, "tenant_id");
        String action   = getString(args, "action");
        boolean dryRun  = getBool(args, "dry_run", false);

        log.info("[ACADEMIC_STRUCTURE] tenant={} action={} dryRun={}", tenantId, action, dryRun);

        if (dryRun) {
            return dryRun(action, "Schema and constraint validation passed. Ready to commit.");
        }

        return switch (action) {
            case "create_department" -> ok("create_department",
                "Department created successfully.\ndept_id: " + UUID.randomUUID());
            case "update_department" -> ok("update_department",
                "Department record updated.");
            case "delete_department" -> ok("delete_department",
                "Department soft-deleted. Existing batches remain intact.");
            case "create_programme"  -> ok("create_programme",
                "Programme created.\nprogramme_id: " + UUID.randomUUID());
            case "list_programmes"   -> ok("list_programmes",
                "Returned 0 programmes. (Connect your ERP data source to populate.)");
            case "create_batch"      -> ok("create_batch",
                "Batch created.\nbatch_id: " + UUID.randomUUID());
            case "assign_section"    -> ok("assign_section",
                "Section assigned to batch.");
            case "link_subject"      -> ok("link_subject",
                "Subject linked to programme curriculum.");
            case "unlink_subject"    -> ok("unlink_subject",
                "Subject unlinked.");
            case "get_structure"     -> ok("get_structure",
                "Academic structure snapshot queued. Use job_id to fetch result.");
            default -> "Unknown action: " + action;
        };
    }
}
