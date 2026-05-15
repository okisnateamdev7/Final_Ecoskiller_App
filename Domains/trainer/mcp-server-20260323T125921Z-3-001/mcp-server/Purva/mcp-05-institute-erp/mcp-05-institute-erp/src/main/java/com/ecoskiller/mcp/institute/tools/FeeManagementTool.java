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
public class FeeManagementTool implements AgentTool {

    @Override
    public String execute(JsonNode args) {
        String tenantId  = getString(args, "tenant_id");
        String action    = getString(args, "action");
        String studentId = getString(args, "student_id");
        double amount    = args.path("amount").asDouble(0);

        log.info("[FEE_MGMT] tenant={} action={} student={} amount={}",
                tenantId, action, studentId, amount);

        return switch (action) {
            case "create_fee_structure"  -> ok("create_fee_structure",
                "Fee structure created.\nfee_structure_id: " + UUID.randomUUID());
            case "update_fee_structure"  -> ok("update_fee_structure",
                "Fee structure updated.");
            case "assign_to_batch"       -> ok("assign_to_batch",
                "Fee structure assigned to batch.");
            case "record_payment"        -> ok("record_payment",
                "Payment of ₹" + (amount / 100) + " recorded for student=" +
                studentId + "\nreceipt_id: " + UUID.randomUUID());
            case "apply_concession"      -> ok("apply_concession",
                "Concession applied for student=" + studentId);
            case "get_dues"              -> ok("get_dues",
                "Outstanding dues for student=" + studentId + ": ₹0.00 (demo)");
            case "generate_receipt"      -> ok("generate_receipt",
                "Receipt URL: /api/fee/receipt/" + UUID.randomUUID());
            case "send_reminder"         -> ok("send_reminder",
                "Fee reminder queued for student=" + studentId);
            case "get_collection_report" -> queued("get_collection_report",
                UUID.randomUUID().toString());
            default -> "Unknown action: " + action;
        };
    }
}
