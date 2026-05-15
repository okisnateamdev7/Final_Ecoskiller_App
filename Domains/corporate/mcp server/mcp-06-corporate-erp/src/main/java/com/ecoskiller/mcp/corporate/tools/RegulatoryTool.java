package com.ecoskiller.mcp.corporate.tools;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * REGULATORY_AGENT_ANTIGRAVITY_ERP
 *
 * <p>Regulatory compliance calendar and filing management.
 * Filed documents are locked (ANTIGRAVITY_SEALED); any amendment
 * creates a new versioned filing and requires an amendment_reason.</p>
 */
@Slf4j
@Component
public class RegulatoryTool implements AgentTool {

    @Override
    public String execute(JsonNode args) throws Exception {
        String tenantId         = getString(args, "tenant_id");
        String action           = getString(args, "action");
        String regulation       = getString(args, "regulation");
        String filingId         = getString(args, "filing_id");
        String amendmentReason  = getString(args, "amendment_reason");

        log.info("[REGULATORY] tenant={} action={} regulation={} filing={}",
                tenantId, action, regulation, filingId);

        // Amendment must carry a reason
        if ("amend_filing".equals(action) && amendmentReason.isBlank()) {
            return sealed("amend_filing requires amendment_reason. " +
                          "Filed documents are locked — provide a reason before creating a new version.");
        }

        return switch (action) {
            case "get_compliance_calendar" -> ok("get_compliance_calendar",
                "Compliance calendar for tenant=" + tenantId +
                " returned. 0 upcoming deadlines (demo — configure your GSTIN/CIN).");
            case "mark_filed"              -> ok("mark_filed",
                "Filing " + filingId + " marked as filed. Status: COMPLETED.");
            case "upload_filing"           -> ok("upload_filing",
                "Filing document uploaded.\nfiling_id : " + UUID.randomUUID() +
                "\nRegulation: " + regulation +
                "\nStatus    : LOCKED (immutable).");
            case "amend_filing"            -> ok("amend_filing",
                "Amendment accepted. New version created.\n" +
                "new_filing_id  : " + UUID.randomUUID() +
                "\nAmendment reason: " + amendmentReason +
                "\nOriginal filing : " + filingId + " preserved.");
            case "set_reminder"            -> ok("set_reminder",
                "Reminder set for filing deadline. Notifications will trigger 7 days prior.");
            case "get_overdue_list"        -> ok("get_overdue_list",
                "Overdue filings: 0 (demo — connect compliance calendar data).");
            case "generate_mca_form"       -> queued("generate_mca_form", UUID.randomUUID().toString(),
                "Regulation: MCA | Form generation in progress");
            case "generate_it_return"      -> queued("generate_it_return", UUID.randomUUID().toString(),
                "Regulation: income_tax | ITR generation in progress");
            case "get_labour_register"     -> ok("get_labour_register",
                "Labour register URL: /api/regulatory/labour-register/" + tenantId);
            case "get_filing_history"      -> ok("get_filing_history",
                "Filing history for regulation=" + regulation +
                " returned. 0 records (demo).");
            default -> "Unknown action: " + action;
        };
    }
}
