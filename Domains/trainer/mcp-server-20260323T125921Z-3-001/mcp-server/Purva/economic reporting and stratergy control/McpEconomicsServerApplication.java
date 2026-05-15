package com.ecoskiller.mcp.economics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * mcp-08-economics — Ecoskiller MCP Server
 * CAT-08: Economics, Reporting & Strategic Control
 * 17 Agents | MCP Protocol 2024-11-05 | Java 17 + Spring Boot 3.2
 */
@SpringBootApplication
public class McpEconomicsServerApplication {
    public static void main(String[] args) {
        System.out.println("""
            ╔═════════════════════════════════════════════════════════╗
            ║    Ecoskiller — mcp-08-economics                        ║
            ║    CAT-08: Economics, Reporting & Strategic Control      ║
            ║    MCP Protocol 2024-11-05 | Java 17 | Port 8708        ║
            ╚═════════════════════════════════════════════════════════╝
            """);
        SpringApplication.run(McpEconomicsServerApplication.class, args);
    }
}
