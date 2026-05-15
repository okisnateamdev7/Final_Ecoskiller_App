package com.ecoskiller.mcp.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║     ECOSKILLER — MCP-03 SECURITY SERVER                         ║
 * ║     CAT-03: Security, Tenancy & Migration Control               ║
 * ║     Agents: 12  |  Priority: CRITICAL  |  Namespace: core      ║
 * ╚══════════════════════════════════════════════════════════════════╝
 *
 * Agents in this server:
 *   01. API_ECONOMY_AGENT
 *   02. AUTH_MIGRATION_AGENT
 *   03. CONSENT_MIGRATION_AGENT
 *   04. ENTERPRISE_INTEGRATION_AGENT
 *   05. ENV_PROMOTION_AGENT
 *   06. I18N_AGENT
 *   07. KEY_MANAGEMENT_AGENT
 *   08. REGION_MIGRATION_AGENT
 *   09. STORAGE_CONNECT_AGENT
 *   10. TENANT_CLONE_AGENT
 *   11. WEBHOOK_MARKETPLACE_AGENT
 *   12. ZERO_DOWNTIME_MIGRATION_AGENT
 */
@Slf4j
@SpringBootApplication
public class Mcp03SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(Mcp03SecurityApplication.class, args);
        log.info("══════════════════════════════════════════════════");
        log.info("  MCP-03 Security Server started on port 8003");
        log.info("  Endpoint: POST http://localhost:8003/mcp");
        log.info("  Health:   GET  http://localhost:8003/mcp/health");
        log.info("  Info:     GET  http://localhost:8003/mcp/info");
        log.info("══════════════════════════════════════════════════");
    }
}
