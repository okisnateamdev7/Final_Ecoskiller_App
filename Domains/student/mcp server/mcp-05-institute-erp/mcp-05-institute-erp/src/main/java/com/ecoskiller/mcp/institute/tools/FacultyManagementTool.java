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
public class FacultyManagementTool implements AgentTool {

    private static final java.util.Set<String> HR_OPS = java.util.Set.of(
        "create_faculty", "deactivate", "initiate_appraisal"
    );

    @Override
    public String execute(JsonNode args) {
        String tenantId  = getString(args, "tenant_id");
        String action    = getString(args, "action");
        String facultyId = getString(args, "faculty_id");
        String hrToken   = getString(args, "hr_token");

        log.info("[FACULTY_MGMT] tenant={} action={} faculty={}", tenantId, action, facultyId);

        // HR approval guard
        if (HR_OPS.contains(action) && hrToken.isBlank()) {
            return "[ANTIGRAVITY_SEALED] Action '" + action +
                   "' requires an hr_token (HR approval workflow must be completed first).";
        }

        return switch (action) {
            case "create_faculty"     -> ok("create_faculty",
                "Faculty profile created.\nfaculty_id: " + UUID.randomUUID());
            case "update_profile"     -> ok("update_profile",
                "Faculty profile updated for faculty_id=" + facultyId);
            case "deactivate"         -> ok("deactivate",
                "Faculty deactivated. Payroll stop queued.");
            case "assign_subject"     -> ok("assign_subject",
                "Subject assigned to faculty=" + facultyId);
            case "unassign_subject"   -> ok("unassign_subject",
                "Subject unassigned.");
            case "set_workload"       -> ok("set_workload",
                "Workload updated for faculty=" + facultyId);
            case "get_workload"       -> ok("get_workload",
                "Workload: 0 hrs/week (connect ERP data source).");
            case "initiate_appraisal" -> ok("initiate_appraisal",
                "Appraisal cycle initiated.\nappraisal_id: " + UUID.randomUUID());
            case "get_appraisal"      -> ok("get_appraisal",
                "Appraisal report for faculty=" + facultyId + " returned.");
            default -> "Unknown action: " + action;
        };
    }
}
