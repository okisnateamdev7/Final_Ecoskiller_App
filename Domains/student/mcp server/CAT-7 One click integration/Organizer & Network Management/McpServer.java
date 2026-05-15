package com.ecoskiller.mcp26;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Ecoskiller | CAT-26 — Organizer & Network Management
 * MCP Server in Java | 29 Agents | Zero external dependencies
 * Transport: stdio | Format: JSON-RPC 2.0 | MCP: 2024-11-05
 */
public class McpServer {
    private static final PrintStream OUT = System.out;

    public static void main(String[] args) throws Exception {
        AgentRegistry registry = new AgentRegistry();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                String method = JsonUtil.extractString(line, "method");
                String id     = JsonUtil.extractRaw(line, "id");
                String params = JsonUtil.extractObject(line, "params");
                String response = switch (method) {
                    case "initialize" -> handleInitialize(id);
                    case "ping"       -> buildSuccess(id, "{}");
                    case "tools/list" -> handleToolsList(id, registry);
                    case "tools/call" -> handleToolsCall(id, params, registry);
                    default           -> buildError(id, -32601, "Method not found: " + method);
                };
                OUT.println(response);
                OUT.flush();
            } catch (Exception e) {
                OUT.println(buildError("null", -32700, "Parse error: " + e.getMessage()));
                OUT.flush();
            }
        }
    }

    private static String handleInitialize(String id) {
        return buildSuccess(id, "{\"protocolVersion\":\"2024-11-05\",\"serverName\":\"mcp-26-organizer\","
            + "\"serverVersion\":\"1.0.0\",\"capabilities\":{\"tools\":{}}}");
    }

    private static String handleToolsList(String id, AgentRegistry registry) {
        StringBuilder sb = new StringBuilder("{\"tools\":[");
        boolean first = true;
        for (ToolDefinition td : registry.getAllTools()) {
            if (!first) sb.append(",");
            sb.append(td.toJson());
            first = false;
        }
        sb.append("]}");
        return buildSuccess(id, sb.toString());
    }

    private static String handleToolsCall(String id, String params, AgentRegistry registry) {
        String toolName = JsonUtil.extractString(params, "name");
        String toolArgs = JsonUtil.extractObject(params, "arguments");
        String output   = registry.invoke(toolName, toolArgs);
        String escaped  = output.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n").replace("\r","");
        return buildSuccess(id, "{\"content\":[{\"type\":\"text\",\"text\":\"" + escaped + "\"}]}");
    }

    static String buildSuccess(String id, String resultJson) {
        return "{\"jsonrpc\":\"2.0\",\"id\":" + id + ",\"result\":" + resultJson + "}";
    }

    static String buildError(String id, int code, String message) {
        return "{\"jsonrpc\":\"2.0\",\"id\":" + id
            + ",\"error\":{\"code\":" + code + ",\"message\":\"" + message.replace("\"","'") + "\"}}";
    }
}
