package com.ecoskiller.mcp19;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Ecoskiller | CAT-19 — Platform Stability & Risk
 * MCP Server in Java | 6 Agents | Priority: HIGH
 *
 * Agents:
 *  1. AGENT_HEALTH_WATCHDOG_AGENT
 *  2. EMERGENCY_PLATFORM_LOCKDOWN_AGENT
 *  3. INSIDER_THREAT_MONITOR_AGENT
 *  4. INSTITUTE_TIMETABLE_OPTIMIZATION_AGENT
 *  5. INTEGRATION_HEALTH_MONITOR_AGENT
 *  6. MODEL_EXPLAINABILITY_DIFF_AGENT
 *
 * Transport : stdio (stdin/stdout)
 * Format    : JSON-RPC 2.0
 * MCP Ver.  : 2024-11-05
 */
public class McpServer {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "mcp-19-platform-stability";
    private static final String SERVER_VERSION = "1.0.0";

    // ------------------------------------------------------------------ //
    //  Tool registry
    // ------------------------------------------------------------------ //
    private static final Map<String, AgentHandler> TOOLS = new HashMap<>();

    static {
        TOOLS.put("agent_health_watchdog",          new AgentHealthWatchdogAgent());
        TOOLS.put("emergency_platform_lockdown",    new EmergencyPlatformLockdownAgent());
        TOOLS.put("insider_threat_monitor",         new InsiderThreatMonitorAgent());
        TOOLS.put("institute_timetable_optimization", new InstituteTimetableOptimizationAgent());
        TOOLS.put("integration_health_monitor",     new IntegrationHealthMonitorAgent());
        TOOLS.put("model_explainability_diff",      new ModelExplainabilityDiffAgent());
    }

    // ------------------------------------------------------------------ //
    //  Entry point
    // ------------------------------------------------------------------ //
    public static void main(String[] args) throws Exception {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
        PrintStream    out = new PrintStream(System.out, true, "UTF-8");

        System.err.println("[mcp-19] Server started. Listening on stdin…");

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            try {
                JsonNode request  = MAPPER.readTree(line);
                String   response = handleRequest(request);
                if (response != null) {
                    out.println(response);
                }
            } catch (Exception e) {
                System.err.println("[mcp-19] Parse error: " + e.getMessage());
                out.println(errorResponse(null, -32700, "Parse error: " + e.getMessage()));
            }
        }
    }

    // ------------------------------------------------------------------ //
    //  JSON-RPC dispatcher
    // ------------------------------------------------------------------ //
    private static String handleRequest(JsonNode req) throws Exception {
        String method = req.path("method").asText("");
        JsonNode id   = req.path("id");
        JsonNode idNode = id.isMissingNode() ? null : id;

        return switch (method) {
            case "initialize"   -> handleInitialize(idNode, req);
            case "tools/list"   -> handleToolsList(idNode);
            case "tools/call"   -> handleToolsCall(idNode, req);
            case "ping"         -> successResponse(idNode, MAPPER.createObjectNode());
            default             -> errorResponse(idNode, -32601, "Method not found: " + method);
        };
    }

    // ------------------------------------------------------------------ //
    //  initialize
    // ------------------------------------------------------------------ //
    private static String handleInitialize(JsonNode id, JsonNode req) throws Exception {
        ObjectNode result = MAPPER.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);

        ObjectNode capabilities = MAPPER.createObjectNode();
        capabilities.set("tools", MAPPER.createObjectNode());
        result.set("capabilities", capabilities);

        ObjectNode serverInfo = MAPPER.createObjectNode();
        serverInfo.put("name",    SERVER_NAME);
        serverInfo.put("version", SERVER_VERSION);
        result.set("serverInfo", serverInfo);

        return successResponse(id, result);
    }

    // ------------------------------------------------------------------ //
    //  tools/list
    // ------------------------------------------------------------------ //
    private static String handleToolsList(JsonNode id) throws Exception {
        ArrayNode toolsArray = MAPPER.createArrayNode();

        for (Map.Entry<String, AgentHandler> entry : TOOLS.entrySet()) {
            ObjectNode tool = MAPPER.createObjectNode();
            tool.put("name",        entry.getKey());
            tool.put("description", entry.getValue().description());
            tool.set("inputSchema",  entry.getValue().inputSchema());
            toolsArray.add(tool);
        }

        ObjectNode result = MAPPER.createObjectNode();
        result.set("tools", toolsArray);
        return successResponse(id, result);
    }

    // ------------------------------------------------------------------ //
    //  tools/call
    // ------------------------------------------------------------------ //
    private static String handleToolsCall(JsonNode id, JsonNode req) throws Exception {
        JsonNode params   = req.path("params");
        String   toolName = params.path("name").asText("");
        JsonNode toolArgs = params.path("arguments");

        AgentHandler handler = TOOLS.get(toolName);
        if (handler == null) {
            return errorResponse(id, -32602, "Unknown tool: " + toolName);
        }

        try {
            JsonNode agentResult = handler.execute(toolArgs);

            ArrayNode content = MAPPER.createArrayNode();
            ObjectNode textBlock = MAPPER.createObjectNode();
            textBlock.put("type", "text");
            textBlock.put("text", MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(agentResult));
            content.add(textBlock);

            ObjectNode result = MAPPER.createObjectNode();
            result.set("content", content);
            result.put("isError", false);
            return successResponse(id, result);

        } catch (Exception e) {
            ArrayNode content = MAPPER.createArrayNode();
            ObjectNode errBlock = MAPPER.createObjectNode();
            errBlock.put("type", "text");
            errBlock.put("text", "Agent error: " + e.getMessage());
            content.add(errBlock);

            ObjectNode result = MAPPER.createObjectNode();
            result.set("content", content);
            result.put("isError", true);
            return successResponse(id, result);
        }
    }

    // ------------------------------------------------------------------ //
    //  JSON-RPC helpers
    // ------------------------------------------------------------------ //
    static String successResponse(JsonNode id, JsonNode result) throws Exception {
        ObjectNode resp = MAPPER.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.set("id", id);
        resp.set("result", result);
        return MAPPER.writeValueAsString(resp);
    }

    static String errorResponse(JsonNode id, int code, String message) {
        try {
            ObjectNode resp  = MAPPER.createObjectNode();
            resp.put("jsonrpc", "2.0");
            if (id != null) resp.set("id", id);
            ObjectNode error = MAPPER.createObjectNode();
            error.put("code",    code);
            error.put("message", message);
            resp.set("error", error);
            return MAPPER.writeValueAsString(resp);
        } catch (Exception e) {
            return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32603,\"message\":\"Internal error\"}}";
        }
    }
}
