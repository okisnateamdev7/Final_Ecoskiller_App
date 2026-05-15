package com.ecoskiller.mcp.corporate.tools;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

/**
 * PAYROLL_AGENT_ANTIGRAVITY_ERP
 *
 * <p>End-to-end payroll processing with statutory deductions.
 * CFO sign-off required before bank disbursement file release
 * (ANTIGRAVITY_SEALED constraint).</p>
 */
@Slf4j
@Component
public class PayrollTool implements AgentTool {

    private static final Set<String> CFO_OPS = Set.of("approve_payroll", "release_bank_file");

    @Override
    public String execute(JsonNode args) throws Exception {
        String tenantId     = getString(args, "tenant_id");
        String action       = getString(args, "action");
        String payrollMonth = getString(args, "payroll_month");
        String employeeId   = getString(args, "employee_id");
        String cfoToken     = getString(args, "cfo_token");

        log.info("[PAYROLL] tenant={} action={} month={} emp={}",
                tenantId, action, payrollMonth, employeeId);

        if (CFO_OPS.contains(action) && cfoToken.isBlank()) {
            return sealed("Action '" + action + "' requires cfo_token. " +
                          "CFO must approve the payroll run before bank file can be released.");
        }

        return switch (action) {
            case "run_payroll"          -> queued("run_payroll", UUID.randomUUID().toString(),
                "Month   : " + payrollMonth + "\nScope   : all active employees");
            case "preview_payroll"      -> queued("preview_payroll", UUID.randomUUID().toString(),
                "Month: " + payrollMonth + " — no posting, preview only");
            case "approve_payroll"      -> ok("approve_payroll",
                "Payroll approved by CFO for month=" + payrollMonth +
                "\nReady for bank file release.");
            case "release_bank_file"    -> ok("release_bank_file",
                "Bank disbursement file released.\nfile_id : " + UUID.randomUUID() +
                "\nFormat  : NEFT/RTGS combined");
            case "generate_payslips"    -> queued("generate_payslips", UUID.randomUUID().toString(),
                "Month: " + payrollMonth + " | Format: PDF");
            case "compute_tds"          -> queued("compute_tds", UUID.randomUUID().toString(),
                "TDS computation for month=" + payrollMonth);
            case "generate_form16"      -> queued("generate_form16", UUID.randomUUID().toString(),
                "Employee: " + employeeId);
            case "get_payroll_register" -> queued("get_payroll_register", UUID.randomUUID().toString(),
                "Month: " + payrollMonth);
            case "reprocess_employee"   -> ok("reprocess_employee",
                "Payroll reprocessed for employee_id=" + employeeId);
            case "get_statutory_summary"-> ok("get_statutory_summary",
                "Statutory summary (PF/ESIC/PT/TDS) for month=" + payrollMonth +
                " returned. 0 amounts (demo).");
            default -> "Unknown action: " + action;
        };
    }
}
