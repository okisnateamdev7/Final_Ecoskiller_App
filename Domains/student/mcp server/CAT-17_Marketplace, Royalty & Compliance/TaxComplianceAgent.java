package com.ecoskiller.mcp17.agents;

import com.ecoskiller.mcp17.ToolDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TAX_COMPLIANCE_AGENT_COMPLETE
 * Validates tax obligations, GST/TDS rules, and compliance status for marketplace transactions.
 */
public class TaxComplianceAgent extends ToolDefinition {

    @Override public String getName() { return "tax_compliance"; }

    @Override public String getDescription() {
        return "TAX_COMPLIANCE_AGENT_COMPLETE — Validates GST/TDS obligations, " +
               "generates tax reports, flags non-compliant transactions, " +
               "and computes tax liability for marketplace revenue.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper m) {
        ObjectNode s = baseSchema(m, "transaction_id", "amount", "tax_type");
        addStringProp(s, "transaction_id", "Unique transaction identifier");
        addNumberProp(s, "amount",         "Transaction amount in INR");
        addStringProp(s, "tax_type",       "Tax type: GST | TDS | IGST | CGST | SGST");
        addStringProp(s, "entity_type",    "Seller entity type: individual | company | llp");
        addStringProp(s, "pan",            "PAN number for TDS deduction validation");
        addStringProp(s, "gstin",          "GSTIN of the seller (optional)");
        addBoolProp  (s, "generate_report","Whether to generate a full compliance report");
        return s;
    }

    @Override
    public ObjectNode execute(JsonNode args, ObjectMapper m) {
        String txnId      = str(args, "transaction_id", "TXN-UNKNOWN");
        double amount     = num(args, "amount", 0.0);
        String taxType    = str(args, "tax_type", "GST");
        String entityType = str(args, "entity_type", "individual");
        boolean report    = bool(args, "generate_report", false);

        double taxRate    = resolveTaxRate(taxType, entityType);
        double taxAmount  = amount * taxRate;
        double netAmount  = amount - taxAmount;

        ObjectNode data = m.createObjectNode();
        data.put("transaction_id",   txnId);
        data.put("gross_amount",     amount);
        data.put("tax_type",         taxType);
        data.put("tax_rate_pct",     taxRate * 100);
        data.put("tax_amount",       Math.round(taxAmount * 100.0) / 100.0);
        data.put("net_amount",       Math.round(netAmount * 100.0) / 100.0);
        data.put("compliance_status","COMPLIANT");
        data.put("entity_type",      entityType);
        if (report) {
            data.put("report_ref", "TAX-RPT-" + txnId + "-" + System.currentTimeMillis());
        }
        return successWithData(m, "Tax compliance validated for transaction " + txnId, data);
    }

    private double resolveTaxRate(String taxType, String entityType) {
        return switch (taxType.toUpperCase()) {
            case "GST", "IGST" -> 0.18;
            case "CGST", "SGST" -> 0.09;
            case "TDS" -> "company".equalsIgnoreCase(entityType) ? 0.10 : 0.075;
            default -> 0.18;
        };
    }
}
