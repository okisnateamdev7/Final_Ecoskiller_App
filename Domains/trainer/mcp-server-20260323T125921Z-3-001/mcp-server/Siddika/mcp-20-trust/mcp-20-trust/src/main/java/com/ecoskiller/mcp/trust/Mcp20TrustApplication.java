package com.ecoskiller.mcp.trust;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MCP-20 Trust, Identity & Safeguards Server
 * CAT-20 — 10 Agents
 * Namespace: core | Priority: HIGH
 *
 * Agents:
 *   1. AGENT_ACCESS_PERMISSION_FIREWALL
 *   2. AGENT_FAILURE_RECOVERY_AGENT
 *   3. CHILD_PROTECTION_EVIDENCE_AGENT
 *   4. CONSENT_AND_PARENT_PERMISSION_AGENT
 *   5. CROSS_PLATFORM_TRUST_SCORE_AGENT
 *   6. EVIDENCE_PRESERVATION_AGENT
 *   7. IDEA_DISPUTE_RESOLUTION_AGENT
 *   8. IDENTITY_ASSURANCE_AGENT
 *   9. USER_RIGHTS_EXPLANATION_AGENT
 *  10. VENDOR_RISK_EVALUATION_AGENT
 */
@SpringBootApplication
public class Mcp20TrustApplication {

    private static final Logger log = LoggerFactory.getLogger(Mcp20TrustApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(Mcp20TrustApplication.class, args);
        log.info("========================================");
        log.info("  MCP-20 Trust & Identity Server UP");
        log.info("  CAT-20 | 10 Agents | Namespace: core");
        log.info("  Port: 8220");
        log.info("========================================");
    }
}
