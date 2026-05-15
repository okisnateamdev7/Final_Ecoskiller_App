package com.ecoskiller.antigravity.cat26;

import com.ecoskiller.antigravity.cat26.server.McpStdioServer;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  ECOSKILLER ANTIGRAVITY — CAT-26 MCP SERVER                    ║
 * ║  Organizer & Network Management                                 ║
 * ║  Version: 1.0.0-ANTIGRAVITY-SEALED                             ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  29 Agents | 58 Tools | Transport: stdio (JSON-RPC 2.0)        ║
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public class Cat26McpServerApplication {
    public static void main(String[] args) {
        System.err.println("""
            ╔══════════════════════════════════════════════════════════════╗
            ║  ECOSKILLER ANTIGRAVITY — CAT-26 MCP SERVER STARTING...    ║
            ║  Organizer & Network Management                             ║
            ║  29 Agents | 58 Tools | stdio transport                    ║
            ╚══════════════════════════════════════════════════════════════╝
            """);
        new McpStdioServer(System.in, System.out).run();
    }
}
