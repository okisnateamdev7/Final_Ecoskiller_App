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
public class ExamEngineTool implements AgentTool {

    @Override
    public String execute(JsonNode args) {
        String tenantId  = getString(args, "tenant_id");
        String action    = getString(args, "action");
        String examId    = getString(args, "exam_id");
        String approverId= getString(args, "approver_id");

        log.info("[EXAM_ENGINE] tenant={} action={} examId={}", tenantId, action, examId);

        // Dual-approval guard for publish_result
        if ("publish_result".equals(action) && approverId.isBlank()) {
            return "[ANTIGRAVITY_SEALED] publish_result requires approver_id. " +
                   "Please provide the second approver's UUID.";
        }

        return switch (action) {
            case "schedule_exam"         -> ok("schedule_exam",
                "Exam scheduled.\nexam_id: " + UUID.randomUUID());
            case "assign_invigilator"    -> ok("assign_invigilator",
                "Invigilator assigned to exam=" + examId);
            case "generate_hall_tickets" -> queued("generate_hall_tickets",
                UUID.randomUUID().toString());
            case "enter_marks"           -> ok("enter_marks",
                "Marks recorded for exam=" + examId);
            case "bulk_enter_marks"      -> queued("bulk_enter_marks",
                UUID.randomUUID().toString());
            case "compute_result"        -> queued("compute_result",
                UUID.randomUUID().toString() + "\nexam_id: " + examId);
            case "publish_result"        -> ok("publish_result",
                "Result published.\nexam_id : " + examId +
                "\napprover: " + approverId);
            case "get_timetable"         -> ok("get_timetable",
                "Timetable retrieved for exam=" + examId);
            case "get_result_sheet"      -> ok("get_result_sheet",
                "Result sheet URL: /api/exam/" + examId + "/result-sheet");
            default -> "Unknown action: " + action;
        };
    }
}
