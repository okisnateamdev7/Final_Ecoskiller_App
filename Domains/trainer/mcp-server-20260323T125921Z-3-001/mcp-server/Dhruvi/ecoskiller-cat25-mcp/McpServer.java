package com.ecoskiller.mcp.server;
import com.ecoskiller.mcp.protocol.JsonRpcHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Ecoskiller MCP Server — CAT-25
 * Analytics & ERP / DevOps & Environment
 * Server ID: mcp-25-analytics-devops | 13 Agents
 * Transport: stdio | JSON-RPC 2.0 | MCP Version: 2024-11-05
 */
public class McpServer {
    public static void main(String[] args) throws IOException {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in,  StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
        JsonRpcHandler h   = new JsonRpcHandler();
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String resp = h.handle(line);
            if (resp != null) out.println(resp);
        }
    }
}
