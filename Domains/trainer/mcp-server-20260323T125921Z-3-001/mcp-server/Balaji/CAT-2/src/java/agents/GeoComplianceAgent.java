package com.ecoskiller.mcp.governance.agents;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * GEO_COMPLIANCE_AGENT
 *
 * Manages geographic data residency and cross-border compliance:
 * - Data residency enforcement per regulation
 * - Cross-border data transfer approvals
 * - Regional regulatory requirement mapping
 * - Geo-fencing policy for data access
 */
@Service
public class GeoComplianceAgent {

    @Tool(name = "geo_check_data_residency",
          description = "Check if a tenant's data is stored in compliant regions per their "
                      + "contractual and regulatory requirements (e.g. DPDP requires India storage).")
    public Map<String, Object> checkDataResidency(
            @ToolParam(description = "Tenant ID") String tenantId,
            @ToolParam(description = "Regulation to check: DPDP | GDPR | CCPA | LOCAL") String regulation) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",      "GEO_COMPLIANCE_AGENT");
        result.put("action",     "CHECK_DATA_RESIDENCY");
        result.put("tenant_id",  tenantId);
        result.put("regulation", regulation);
        result.put("compliant",  true);
        result.put("data_locations", List.of(
            Map.of("region", "ap-south-1 (Mumbai)",    "data_type", "PRIMARY",  "compliant", true),
            Map.of("region", "ap-south-2 (Hyderabad)", "data_type", "REPLICA",  "compliant", true),
            Map.of("region", "eu-west-1 (Ireland)",    "data_type", "ANALYTICS","compliant", false,
                   "issue", "GDPR analytics transfer requires SCCs")
        ));
        result.put("violations", 1);
        result.put("action_required", true);
        return result;
    }

    @Tool(name = "geo_approve_cross_border_transfer",
          description = "Evaluate and approve or deny a cross-border data transfer request "
                      + "based on applicable regulations and existing legal agreements.")
    public Map<String, Object> approveCrossBorderTransfer(
            @ToolParam(description = "Source country ISO code, e.g. 'IN'") String sourceCountry,
            @ToolParam(description = "Destination country ISO code, e.g. 'DE'") String destCountry,
            @ToolParam(description = "Data classification: PII | FINANCIAL | HEALTH | PUBLIC") String dataClass,
            @ToolParam(description = "Transfer purpose: ANALYTICS | BACKUP | PROCESSING | SHARING") String purpose) {

        boolean approved = !dataClass.equals("HEALTH");
        Map<String, Object> result = new HashMap<>();
        result.put("agent",          "GEO_COMPLIANCE_AGENT");
        result.put("action",         "CROSS_BORDER_TRANSFER");
        result.put("source_country", sourceCountry);
        result.put("dest_country",   destCountry);
        result.put("data_class",     dataClass);
        result.put("purpose",        purpose);
        result.put("approved",       approved);
        result.put("legal_basis",    approved ? "Standard Contractual Clauses (SCCs)" : "NOT_PERMITTED");
        result.put("conditions",     approved
                ? List.of("Encrypt data in transit", "Log all accesses", "Delete after 90 days")
                : List.of("Health data cross-border transfer requires DPA approval"));
        return result;
    }

    @Tool(name = "geo_map_regulatory_requirements",
          description = "Map all applicable regulatory requirements for a given country and "
                      + "data type. Returns a checklist of what must be implemented.")
    public Map<String, Object> mapRegulatoryRequirements(
            @ToolParam(description = "Country ISO code, e.g. 'IN' for India") String countryCode,
            @ToolParam(description = "Industry sector: EDUCATION | FINANCE | HEALTHCARE | GENERAL") String sector) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",        "GEO_COMPLIANCE_AGENT");
        result.put("action",       "MAP_REGULATORY_REQUIREMENTS");
        result.put("country",      countryCode);
        result.put("sector",       sector);
        result.put("regulations",  List.of(
            Map.of("name", "DPDP 2023",         "applicability", "MANDATORY",   "status", "ACTIVE"),
            Map.of("name", "IT Act 2000",        "applicability", "MANDATORY",   "status", "ACTIVE"),
            Map.of("name", "NEP 2020 Data Rules","applicability", "RECOMMENDED", "status", "DRAFT"),
            Map.of("name", "RBI DPSS",           "applicability", "CONDITIONAL", "status", "ACTIVE")
        ));
        result.put("key_requirements", List.of(
            "Data localization for sensitive personal data",
            "Consent management with withdrawal support",
            "Data Principal rights: access, correction, erasure",
            "Data fiduciary registration with DPB"
        ));
        return result;
    }
}
