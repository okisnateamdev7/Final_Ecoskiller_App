package com.ecoskiller.antigravity.cat13;

import com.ecoskiller.antigravity.cat13.server.McpStdioServer;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  ECOSKILLER ANTIGRAVITY — CAT-13 MCP SERVER                    ║
 * ║  Enterprise Optimization + Trust Infrastructure                 ║
 * ║  Version: 1.0.0-ANTIGRAVITY-SEALED                             ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  18 Agents | 38 Tools | Transport: stdio (JSON-RPC 2.0)        ║
 * ╚══════════════════════════════════════════════════════════════════╝
 *
 * Usage:
 *   java -jar cat13-enterprise-optimization-trust-mcp-1.0.0-ANTIGRAVITY.jar
 *
 * Claude Desktop config:
 *   "command": "java"
 *   "args": ["-jar", "/path/to/cat13-...jar"]
 */
public class Cat13McpServerApplication {

    public static void main(String[] args) {
        System.err.println("""
            ╔══════════════════════════════════════════════════════════════╗
            ║  ECOSKILLER ANTIGRAVITY — CAT-13 MCP SERVER STARTING...    ║
            ║  Enterprise Optimization + Trust Infrastructure             ║
            ║  18 Agents | 38 Tools | stdio transport                    ║
            ╚══════════════════════════════════════════════════════════════╝
            """);

        McpStdioServer server = new McpStdioServer(System.in, System.out);
        server.run();
    }
}
