package com.ecoskiller.antigravity.cat13.server;

import com.ecoskiller.antigravity.cat13.model.McpModels.*;
import com.ecoskiller.antigravity.cat13.tools.Cat13ToolRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.*;
import java.util.*;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  MCP Server — stdio transport                                   ║
 * ║  CAT-13: Enterprise Optimization + Trust Infrastructure         ║
 * ║  ECOSKILLER ANTIGRAVITY SEALED                                  ║
 * ╠══════════════════════════════════════════════════════════════════╣
 * ║  Transport : stdin/stdout (JSON-RPC 2.0)                        ║
 * ║  Compatible: Claude Desktop, Cursor, any MCP client             ║
 * ║  Agents    : 18 | Tools: 38                                     ║
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public class McpStdioServer {

    private static final ObjectMapper MAPPER = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);

    private final BufferedReader in;
    private final PrintWriter out;

    public McpStdioServer(InputStream inputStream, OutputStream outputStream) {
        this.in  = new BufferedReader(new InputStreamReader(inputStream));
        this.out = new PrintWriter(new OutputStreamWriter(outputStream), true);
    }

    /** Main server loop — reads JSON-RPC from stdin, writes response to stdout */
    public void run() {
        log("CAT-13 ANTIGRAVITY MCP Server started — 18 agents, 38 tools");
        String line;
        try {
            while ((line = in.readLine()) != null) {
                if (line.isBlank()) continue;
                try {
                    handleLine(line.trim());
                } catch (Exception e) {
                    log("Error handling line: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            log("Server loop ended: " + e.getMessage());
        }
        log("CAT-13 MCP Server stopped.");
    }

    @SuppressWarnings("unchecked")
    private void handleLine(String line) throws Exception {
        Map<String, Object> req = MAPPER.readValue(line, Map.class);
        Object id     = req.get("id");
        String method = (String) req.get("method");

        McpResponse response = switch (method) {
            case "initialize"  -> handleInitialize(id);
            case "initialized" -> null; // notification, no response
            case "tools/list"  -> handleToolsList(id);
            case "tools/call"  -> handleToolsCall(req, id);
            case "ping"        -> McpResponse.ok(Map.of("pong", true), id);
            default            -> McpResponse.err(-32601, "Method not found: " + method, id);
        };

        if (response != null) {
            out.println(MAPPER.writeValueAsString(response));
        }
    }

    private McpResponse handleInitialize(Object id) {
        return McpResponse.ok(Map.of(
            "protocolVersion", "2024-11-05",
            "capabilities", Map.of(
                "tools",     Map.of("listChanged", false),
                "resources", Map.of("subscribe", false, "listChanged", false),
                "logging",   Map.of()
            ),
            "serverInfo", Map.of(
                "name",    "ecoskiller-cat13-antigravity-mcp",
                "version", "1.0.0-ANTIGRAVITY-SEALED"
            )
        ), id);
    }

    private McpResponse handleToolsList(Object id) {
        return McpResponse.ok(Map.of("tools", Cat13ToolRegistry.getAllTools()), id);
    }

    @SuppressWarnings("unchecked")
    private McpResponse handleToolsCall(Map<String, Object> req, Object id) {
        try {
            Map<String, Object> params    = (Map<String, Object>) req.get("params");
            String toolName               = (String) params.get("name");
            Map<String, Object> arguments = (Map<String, Object>) params.getOrDefault("arguments", Map.of());

            log("tools/call → " + toolName);

            AgentResult result = Cat13ToolRegistry.dispatch(toolName, arguments);

            String resultJson = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            ToolCallResult toolCallResult = new ToolCallResult(resultJson, "ERROR".equals(result.status));
            return McpResponse.ok(toolCallResult, id);

        } catch (Exception e) {
            log("tools/call error: " + e.getMessage());
            return McpResponse.err(-32603, "Internal error: " + e.getMessage(), id);
        }
    }

    private static void log(String msg) {
        System.err.println("[CAT-13-MCP] " + msg);
    }
}
