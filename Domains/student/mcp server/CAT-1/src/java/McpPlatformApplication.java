package com.ecoskiller.mcp.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ecoskiller MCP-01 Platform Server
 *
 * CAT-01 — Core Platform & Meta Architecture
 * Namespace : core
 * Priority  : MEDIUM
 * Agents    : 11
 *
 * Agents hosted:
 *  1.  ANTIGRAVITY_CONFIGURATION_AGENT
 *  2.  ANTIGRAVITY_DEPENDENCY_RISK_AGENT
 *  3.  ANTIGRAVITY_OBSERVABILITY_AGENT
 *  4.  ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT
 *  5.  ANTIGRAVITY_PLATFORM_EVALUATION_AGENT
 *  6.  ANTIGRAVITY_TECH_DEBT_AGENT
 *  7.  ANTIGRAVITY_TRUTH_ENGINE_AGENT
 *  8.  ECOSKILLER_ANTIGRAVITY_API_AGENT
 *  9.  ECOSKILLER_ANTIGRAVITY_SCHEMA_COMPATIBILITY_AGENT
 *  10. ECOSKILLER_ANTIGRAVITY_SYSTEM_SETUP_AGENT
 *  11. ECOSKILLER_ANTIGRAVITY_UI_AGENT
 */
@SpringBootApplication
public class McpPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpPlatformApplication.class, args);
    }
}
