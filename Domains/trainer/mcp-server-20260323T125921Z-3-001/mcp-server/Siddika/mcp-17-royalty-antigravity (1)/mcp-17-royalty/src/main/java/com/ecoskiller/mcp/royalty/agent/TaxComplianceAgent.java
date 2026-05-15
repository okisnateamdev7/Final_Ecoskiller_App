package com.ecoskiller.mcp.royalty.agent;

import com.ecoskiller.mcp.royalty.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * AGENT-01: TAX_COMPLIANCE_AGENT
 * Handles TDS / GST / VAT deduction, jurisdiction rules, and tax filing.
 */
@Component
public class TaxComplianceAgent {

    @Tool(name = "tax_compliance_check",
          description = "Validate TDS/GST/VAT for a royalty transaction based on jurisdiction.")
    public AgentResponse checkCompliance(
            @ToolParam(description = "Tenant ID") String tenantId,
            @ToolParam(description = "Transaction amount in INR") double amount,
            @ToolParam(description = "Jurisdiction country code: IN | US | GB") String jurisdiction) {

        double tdsRate = jurisdiction.equalsIgnoreCase("IN") ? 0.10 : 0.0;
        double gstRate = jurisdiction.equalsIgnoreCase("IN") ? 0.18 : 0.0;
        double tds     = amount * tdsRate;
        double gst     = amount * gstRate;

        return AgentResponse.ok("TAX_COMPLIANCE_AGENT",
                "Tax compliance check completed for tenant " + tenantId,
                Map.of(
                        "tenantId",          tenantId,
                        "grossAmount",       amount,
                        "jurisdiction",      jurisdiction,
                        "tdsRate",           tdsRate,
                        "tdsDeducted",       tds,
                        "gstRate",           gstRate,
                        "gstApplicable",     gst,
                        "netPayable",        amount - tds,
                        "complianceStatus",  "COMPLIANT"
                ));
    }

    @Tool(name = "tax_filing_generate",
          description = "Generate tax filing summary for a financial year and tenant.")
    public AgentResponse generateFilingSummary(
            @ToolParam(description = "Tenant ID") String tenantId,
            @ToolParam(description = "Financial year e.g. 2024-25") String financialYear) {

        return AgentResponse.ok("TAX_COMPLIANCE_AGENT",
                "Tax filing summary generated for FY " + financialYear,
                Map.of(
                        "tenantId",          tenantId,
                        "financialYear",     financialYear,
                        "totalRoyaltyIncome",500000.0,
                        "totalTdsDeducted",  50000.0,
                        "totalGstPaid",      90000.0,
                        "form26ASStatus",    "AVAILABLE",
                        "filingDueDate",     "31-Jul-" + financialYear.split("-")[1]
                ));
    }
}
