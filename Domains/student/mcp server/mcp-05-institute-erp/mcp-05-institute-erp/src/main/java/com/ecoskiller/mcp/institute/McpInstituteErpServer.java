package com.ecoskiller.mcp.institute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ecoskiller — MCP-05 Institute ERP Suite
 *
 * <p>Antigravity MCP Server that exposes all 12 CAT-05 agents as MCP tools
 * over HTTP/JSON-RPC 2.0.  The "SEALED" agents enforce strict input validation
 * and schema contracts defined in their respective agent descriptors.</p>
 *
 * <pre>
 *   POST /mcp          ← JSON-RPC envelope (tools/call, tools/list, initialize)
 *   GET  /mcp/health   ← health probe
 *   GET  /mcp/manifest ← server manifest (id, version, tools)
 * </pre>
 */
@SpringBootApplication
public class McpInstituteErpServer {

    public static void main(String[] args) {
        SpringApplication.run(McpInstituteErpServer.class, args);
    }
}
