package com.ecoskiller.mcp.integrations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * mcp-07-integrations — Ecoskiller MCP Server
 *
 * CAT-07: One-Click Integrations
 * 18 Agents | MCP Protocol 2024-11-05 | Java 17 + Spring Boot 3.2
 *
 * Agents:
 *   sso_integrate, calendar_sync, analytics_connect, automation_connect,
 *   data_warehouse_connect, deployment_checklist, digilocker_connect,
 *   digilocker_quick_reference, digilocker_technical_spec, esign_connect,
 *   implementation_guide, llm_connect, marketing_connect, payment_connect,
 *   prompt_integrity_verify, cat07_readme, video_connect, whatsapp_connect
 */
@SpringBootApplication
public class McpIntegrationsServerApplication {

    public static void main(String[] args) {
        System.out.println("""
            ╔════════════════════════════════════════════════════╗
            ║    Ecoskiller — mcp-07-integrations               ║
            ║    CAT-07: One-Click Integrations                  ║
            ║    MCP Protocol 2024-11-05 | Java 17               ║
            ╚════════════════════════════════════════════════════╝
            """);
        SpringApplication.run(McpIntegrationsServerApplication.class, args);
    }
}
