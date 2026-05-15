package com.ecoskiller.mcp.governance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ecoskiller MCP-02 Governance Server
 *
 * CAT-02 — Governance & Compliance
 * Namespace : core
 * Priority  : HIGH
 * Agents    : 12
 *
 * Agents hosted:
 *  1.  AUDIT_COMPLIANCE_AGENT
 *  2.  BACKUP_DR_AGENT
 *  3.  BILLING_GOVERNANCE_AGENT
 *  4.  DATA_GOVERNANCE_AGENT
 *  5.  DEVSECOPS_AGENT
 *  6.  FORENSICS_AGENT
 *  7.  GEO_COMPLIANCE_AGENT
 *  8.  LEGAL_POLICY_AGENT
 *  9.  MODERATION_AGENT
 *  10. POLICY_EVOLUTION_AGENT
 *  11. TENANCY_GOVERNANCE_AGENT
 *  12. ZERO_TRUST_AGENT
 */
@SpringBootApplication
public class McpGovernanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpGovernanceApplication.class, args);
    }
}
