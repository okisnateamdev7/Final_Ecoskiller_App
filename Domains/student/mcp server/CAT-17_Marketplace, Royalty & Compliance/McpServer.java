package com.ecoskiller.mcp17;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.Scanner;

/**
 * EcoSkiller MCP Server — CAT-17: Marketplace, Royalty & Compliance
 * Transport: stdio (stdin/stdout) | Protocol: JSON-RPC 2.0 | MCP Version: 2024-11-05
 */
public class McpServer {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final AgentRegistry REGISTRY = new AgentRegistry();

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                JsonNode request = MAPPER.readTree(line);
                String response = handleRequest(request);
                if (response != null) {
                    writer.println(response);
                }
            } catch (Exception e) {
                writer.println(errorResponse(null, -32700, "Parse error: " + e.getMessage()));
            }
        }
    }

    private static String handleRequest(JsonNode req) throws Exception {
        String id = req.has("id") ? req.get("id").asText() : null;
        String method = req.has("method") ? req.get("method").asText() : "";

        return switch (method) {
            case "initialize"       -> handleInitialize(id, req);
            case "tools/list"       -> handleToolsList(id);
            case "tools/call"       -> handleToolsCall(id, req);
            case "ping"             -> okResponse(id, MAPPER.createObjectNode());
            default                 -> errorResponse(id, -32601, "Method not found: " + method);
        };
    }

    // ── initialize ──────────────────────────────────────────────────────────
    private static String handleInitialize(String id, JsonNode req) throws Exception {
        ObjectNode result = MAPPER.createObjectNode();
        result.put("protocolVersion", "2024-11-05");
        result.put("serverName", "mcp-17-royalty");
        result.put("serverVersion", "1.0.0");

        ObjectNode capabilities = result.putObject("capabilities");
        capabilities.putObject("tools");

        ObjectNode info = result.putObject("serverInfo");
        info.put("name", "EcoSkiller CAT-17 — Marketplace, Royalty & Compliance");
        info.put("version", "1.0.0");

        return okResponse(id, result);
    }

    // ── tools/list ──────────────────────────────────────────────────────────
    private static String handleToolsList(String id) throws Exception {
        ObjectNode result = MAPPER.createObjectNode();
        ArrayNode tools = result.putArray("tools");
        for (ToolDefinition tool : REGISTRY.getAllTools()) {
            tools.add(tool.toJson(MAPPER));
        }
        return okResponse(id, result);
    }

    // ── tools/call ──────────────────────────────────────────────────────────
    private static String handleToolsCall(String id, JsonNode req) throws Exception {
        JsonNode params = req.path("params");
        String toolName = params.path("name").asText("");
        JsonNode args = params.path("arguments");

        ToolDefinition tool = REGISTRY.getTool(toolName);
        if (tool == null) {
            return errorResponse(id, -32602, "Unknown tool: " + toolName);
        }

        ObjectNode callResult = tool.execute(args, MAPPER);
        ObjectNode result = MAPPER.createObjectNode();
        ArrayNode content = result.putArray("content");
        ObjectNode textBlock = content.addObject();
        textBlock.put("type", "text");
        textBlock.put("text", MAPPER.writeValueAsString(callResult));

        return okResponse(id, result);
    }

    // ── helpers ─────────────────────────────────────────────────────────────
    static String okResponse(String id, ObjectNode result) throws Exception {
        ObjectNode resp = MAPPER.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.put("id", id);
        resp.set("result", result);
        return MAPPER.writeValueAsString(resp);
    }

    static String errorResponse(String id, int code, String message) {
        try {
            ObjectNode resp = MAPPER.createObjectNode();
            resp.put("jsonrpc", "2.0");
            if (id != null) resp.put("id", id);
            ObjectNode error = resp.putObject("error");
            error.put("code", code);
            error.put("message", message);
            return MAPPER.writeValueAsString(resp);
        } catch (Exception e) {
            return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32603,\"message\":\"Internal error\"}}";
        }
    }
}
