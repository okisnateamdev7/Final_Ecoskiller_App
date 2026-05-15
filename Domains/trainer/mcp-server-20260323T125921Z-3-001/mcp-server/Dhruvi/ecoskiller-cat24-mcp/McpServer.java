package com.ecoskiller.mcp.server;
import com.ecoskiller.mcp.protocol.JsonRpcHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
/**
 * Ecoskiller MCP Server — CAT-24
 * Scoring & Fairness / Security & Compliance / Billing & Quota / Event & Contract
 * Server ID: mcp-24-scoring-billing | 36 Agents
 * Transport: stdio | Protocol: JSON-RPC 2.0 | MCP Version: 2024-11-05
 */
public class McpServer {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
        JsonRpcHandler handler = new JsonRpcHandler();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String response = handler.handle(line);
            if (response != null) writer.println(response);
        }
    }
}
