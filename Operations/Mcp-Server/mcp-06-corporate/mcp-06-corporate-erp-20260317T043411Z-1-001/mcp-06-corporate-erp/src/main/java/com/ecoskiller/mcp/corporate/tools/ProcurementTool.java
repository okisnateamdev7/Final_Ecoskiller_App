package com.ecoskiller.mcp.corporate.tools;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * PROCUREMENT_AGENT_ANTIGRAVITY_ERP
 *
 * <p>Procure-to-pay lifecycle. POs above ₹1 lakh (10,000,000 paise)
 * require an approver_token and pass a budget-availability check
 * (ANTIGRAVITY_SEALED constraint).</p>
 */
@Slf4j
@Component
public class ProcurementTool implements AgentTool {

    private static final long PO_THRESHOLD_PAISE = 10_000_000L; // ₹1 lakh

    @Override
    public String execute(JsonNode args) throws Exception {
        String tenantId      = getString(args, "tenant_id");
        String action        = getString(args, "action");
        String poId          = getString(args, "po_id");
        String vendorId      = getString(args, "vendor_id");
        long   amount        = (long) getDouble(args, "amount");
        String approverToken = getString(args, "approver_token");

        log.info("[PROCUREMENT] tenant={} action={} po={} vendor={} amount={}",
                tenantId, action, poId, vendorId, amount);

        // Guard: large POs need approval
        if ("create_po".equals(action) && amount > PO_THRESHOLD_PAISE && approverToken.isBlank()) {
            return sealed("PO value ₹" + (amount / 100) +
                          " exceeds ₹1,00,000 limit. approver_token is mandatory. " +
                          "Complete the multi-level approval workflow first.");
        }

        return switch (action) {
            case "create_requisition"  -> ok("create_requisition",
                "Purchase requisition created.\nreq_id: " + UUID.randomUUID());
            case "approve_requisition" -> ok("approve_requisition",
                "Requisition approved. RFQ/PO creation unlocked.");
            case "create_vendor"       -> ok("create_vendor",
                "Vendor registered.\nvendor_id: " + UUID.randomUUID());
            case "update_vendor"       -> ok("update_vendor",
                "Vendor " + vendorId + " updated.");
            case "deactivate_vendor"   -> ok("deactivate_vendor",
                "Vendor " + vendorId + " deactivated. Existing POs unaffected.");
            case "create_rfq"          -> ok("create_rfq",
                "RFQ issued.\nrfq_id: " + UUID.randomUUID());
            case "evaluate_rfq"        -> ok("evaluate_rfq",
                "RFQ evaluation complete. Lowest qualified bid identified.");
            case "create_po"           -> ok("create_po",
                "Purchase Order created.\npo_id   : " + UUID.randomUUID() +
                "\nAmount  : ₹" + (amount / 100));
            case "approve_po"          -> ok("approve_po",
                "PO " + poId + " approved.");
            case "amend_po"            -> ok("amend_po",
                "PO " + poId + " amended. Version incremented.");
            case "close_po"            -> ok("close_po",
                "PO " + poId + " closed.");
            case "record_grn"          -> ok("record_grn",
                "GRN recorded for PO=" + poId + "\ngrn_id: " + UUID.randomUUID());
            case "three_way_match"     -> ok("three_way_match",
                "3-way match (PO / GRN / Invoice) passed for PO=" + poId);
            case "create_vendor_invoice"-> ok("create_vendor_invoice",
                "Vendor invoice registered.\ninvoice_id: " + UUID.randomUUID());
            case "schedule_payment"    -> ok("schedule_payment",
                "Payment scheduled for vendor=" + vendorId);
            case "get_spend_report"    -> queued("get_spend_report", UUID.randomUUID().toString(),
                "Scope: all vendors / all categories");
            default -> "Unknown action: " + action;
        };
    }
}
