package com.ecoskiller.mcp.corporate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ecoskiller — MCP-06 Corporate ERP Suite
 *
 * <p>Antigravity MCP Server exposing all 12 CAT-06 agents as JSON-RPC 2.0
 * tools over HTTP.  SEALED agents enforce strict tenant isolation, approval
 * workflows, and financial-grade audit trails.</p>
 *
 * <pre>
 *   POST /mcp          ← JSON-RPC 2.0
 *   GET  /mcp/health   ← liveness probe
 *   GET  /mcp/manifest ← server identity + tool listing
 * </pre>
 */
@SpringBootApplication
public class McpCorporateErpServer {
    public static void main(String[] args) {
        SpringApplication.run(McpCorporateErpServer.class, args);
    }
}
