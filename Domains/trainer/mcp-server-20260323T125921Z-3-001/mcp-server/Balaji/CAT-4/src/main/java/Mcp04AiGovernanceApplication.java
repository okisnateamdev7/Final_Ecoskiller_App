package com.ecoskiller.mcp.aigovernance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║     ECOSKILLER — MCP-04 AI GOVERNANCE SERVER                    ║
 * ║     CAT-04: AI & Intelligence Governance                        ║
 * ║     Agents: 12  |  Priority: HIGH  |  Namespace: core          ║
 * ╚══════════════════════════════════════════════════════════════════╝
 *
 * Agents in this server:
 *   01. AI_PERMISSION_FIREWALL_AGENT
 *   02. BEHAVIOR_ANALYTICS_AGENT
 *   03. COMMUNITY_HEALTH_AGENT
 *   04. ETHICS_AGENT
 *   05. HUMAN_AI_INTERFACE_AGENT
 *   06. INCENTIVE_AGENT
 *   07. ML_REHYDRATION_AGENT
 *   08. ML_ROUTING_AGENT
 *   09. MODEL_RISK_AGENT
 *   10. POWER_BALANCE_AGENT
 *   11. REPUTATION_AGENT
 *   12. STRATEGIC_SIMULATION_AGENT
 */
@Slf4j
@SpringBootApplication
public class Mcp04AiGovernanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Mcp04AiGovernanceApplication.class, args);
        log.info("══════════════════════════════════════════════════");
        log.info("  MCP-04 AI Governance Server started on port 8004");
        log.info("  Endpoint: POST http://localhost:8004/mcp");
        log.info("  Health:   GET  http://localhost:8004/mcp/health");
        log.info("  Info:     GET  http://localhost:8004/mcp/info");
        log.info("══════════════════════════════════════════════════");
    }
}
