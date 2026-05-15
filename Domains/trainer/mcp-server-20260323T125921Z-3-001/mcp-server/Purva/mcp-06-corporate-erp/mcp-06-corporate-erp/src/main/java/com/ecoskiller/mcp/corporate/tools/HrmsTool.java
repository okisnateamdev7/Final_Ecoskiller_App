package com.ecoskiller.mcp.corporate.tools;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

/**
 * HRMS_AGENT_ANTIGRAVITY_SEALED
 *
 * <p>Human Resource Management System. Salary-grade changes and
 * offboarding require both hr_token and manager_token
 * (ANTIGRAVITY_SEALED dual-approval constraint).</p>
 */
@Slf4j
@Component
public class HrmsTool implements AgentTool {

    private static final Set<String> DUAL_APPROVAL_OPS =
        Set.of("offboard_employee", "update_salary_grade");

    @Override
    public String execute(JsonNode args) throws Exception {
        String tenantId    = getString(args, "tenant_id");
        String action      = getString(args, "action");
        String employeeId  = getString(args, "employee_id");
        String managerToken= getString(args, "manager_token");
        String hrToken     = getString(args, "hr_token");

        log.info("[HRMS] tenant={} action={} employee={}", tenantId, action, employeeId);

        if (DUAL_APPROVAL_OPS.contains(action)) {
            if (managerToken.isBlank() || hrToken.isBlank()) {
                return sealed("Action '" + action + "' requires BOTH manager_token AND hr_token. " +
                              "Initiate the dual-approval workflow before calling this tool.");
            }
        }

        return switch (action) {
            case "onboard_employee"      -> ok("onboard_employee",
                "Employee onboarded.\nemployee_id: " + UUID.randomUUID());
            case "update_employee"       -> ok("update_employee",
                "Employee profile updated for employee_id=" + employeeId);
            case "offboard_employee"     -> ok("offboard_employee",
                "Offboarding complete for employee_id=" + employeeId +
                "\nPayroll stop queued. System access revoked.");
            case "apply_leave"           -> ok("apply_leave",
                "Leave application submitted.\nleave_id: " + UUID.randomUUID());
            case "approve_leave"         -> ok("approve_leave",
                "Leave approved for employee_id=" + employeeId);
            case "reject_leave"          -> ok("reject_leave",
                "Leave rejected. Reason communicated.");
            case "update_salary_grade"   -> ok("update_salary_grade",
                "Salary grade updated for employee_id=" + employeeId +
                "\nEffective from next payroll cycle.");
            case "get_org_chart"         -> ok("get_org_chart",
                "Org chart URL: /api/hrms/org-chart/" + tenantId);
            case "start_performance_cycle" -> ok("start_performance_cycle",
                "Performance cycle started.\ncycle_id: " + UUID.randomUUID());
            case "submit_appraisal"      -> ok("submit_appraisal",
                "Appraisal submitted for employee_id=" + employeeId);
            case "get_headcount_report"  -> queued("get_headcount_report", UUID.randomUUID().toString(),
                "Scope: all active employees");
            case "get_compliance_summary"-> ok("get_compliance_summary",
                "Compliance summary: PF/ESIC enrollments up to date (demo).");
            default -> "Unknown action: " + action;
        };
    }
}
