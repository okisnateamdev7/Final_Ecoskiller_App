package com.ecoskiller.mcp.rbac.server;

import com.ecoskiller.mcp.rbac.tools.*;
import com.ecoskiller.mcp.rbac.security.InputSanitizer;
import com.ecoskiller.mcp.rbac.audit.AuditLogger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.logging.*;

/**
 * Ecoskiller — CAT-RBAC: Kubernetes RBAC MCP Server
 *
 * Transport : stdio (JSON-RPC 2.0)
 * MCP Version: 2024-11-05
 * Priority  : HIGH — Security-Critical
 *
 * Agents (18):
 *  1. rbac_check_access
 *  2. rbac_create_role
 *  3. rbac_update_role
 *  4. rbac_delete_role
 *  5. rbac_get_role
 *  6. rbac_list_roles
 *  7. rbac_create_role_binding
 *  8. rbac_delete_role_binding
 *  9. rbac_list_role_bindings
 * 10. rbac_create_cluster_role
 * 11. rbac_delete_cluster_role
 * 12. rbac_list_cluster_roles
 * 13. rbac_bind_cluster_role
 * 14. rbac_list_service_accounts
 * 15. rbac_get_user_permissions
 * 16. rbac_audit_log_query
 * 17. rbac_validate_policy
 * 18. rbac_sync_gitops_status
 */
public class McpRbacServer {

    private static final Logger LOG = Logger.getLogger(McpRbacServer.class.getName());
    private static final String SERVER_NAME    = "mcp-rbac-k8s";
    private static final String SERVER_VERSION = "1.0.0";
    private static final String MCP_VERSION    = "2024-11-05";

    private final ObjectMapper  mapper   = new ObjectMapper();
    private final ToolRegistry  registry = new ToolRegistry();
    private final InputSanitizer sanitizer = new InputSanitizer();
    private final AuditLogger   audit    = new AuditLogger();

    public static void main(String[] args) throws Exception {
        configureLogging();
        new McpRbacServer().run();
    }

    /** Redirect all JUL logging to stderr so stdout stays clean for JSON-RPC. */
    private static void configureLogging() {
        LogManager.getLogManager().reset();
        Logger root = Logger.getLogger("");
        ConsoleHandler h = new ConsoleHandler();
        h.setStream(System.err);
        h.setLevel(Level.ALL);
        root.addHandler(h);
        root.setLevel(Level.INFO);
    }

    public void run() throws Exception {
        LOG.info("[McpRbacServer] Starting " + SERVER_NAME + " v" + SERVER_VERSION);
        registry.registerAll();

        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                String response = handleRequest(line);
                if (response != null) {
                    out.println(response);
                    out.flush();
                }
            } catch (Exception e) {
                LOG.severe("[McpRbacServer] Fatal handler error: " + e.getMessage());
                String errResp = buildErrorResponse(null, -32700, "Parse error: " + e.getMessage());
                out.println(errResp);
                out.flush();
            }
        }
        LOG.info("[McpRbacServer] stdin closed — shutting down.");
    }

    // ─────────────────────────────────────────────────────────────────
    //  Request router
    // ─────────────────────────────────────────────────────────────────

    private String handleRequest(String raw) throws Exception {
        JsonNode req = mapper.readTree(raw);

        // Validate JSON-RPC 2.0 structure
        if (!req.has("jsonrpc") || !"2.0".equals(req.get("jsonrpc").asText())) {
            return buildErrorResponse(extractId(req), -32600, "Invalid Request: missing jsonrpc field");
        }
        if (!req.has("method")) {
            return buildErrorResponse(extractId(req), -32600, "Invalid Request: missing method");
        }

        String method = req.get("method").asText();
        JsonNode id   = req.get("id");

        // Security: sanitize all incoming params
        JsonNode params = req.has("params") ? sanitizer.sanitizeNode(req.get("params")) : mapper.createObjectNode();

        return switch (method) {
            case "initialize"  -> handleInitialize(id, params);
            case "ping"        -> buildResult(id, mapper.createObjectNode());
            case "tools/list"  -> handleToolsList(id);
            case "tools/call"  -> handleToolCall(id, params);
            default            -> buildErrorResponse(id, -32601, "Method not found: " + method);
        };
    }

    // ─────────────────────────────────────────────────────────────────
    //  initialize
    // ─────────────────────────────────────────────────────────────────

    private String handleInitialize(JsonNode id, JsonNode params) throws Exception {
        ObjectNode capabilities = mapper.createObjectNode();
        ObjectNode tools        = mapper.createObjectNode();
        tools.put("listChanged", false);
        capabilities.set("tools", tools);

        ObjectNode serverInfo = mapper.createObjectNode();
        serverInfo.put("name",    SERVER_NAME);
        serverInfo.put("version", SERVER_VERSION);

        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);
        result.set("capabilities",   capabilities);
        result.set("serverInfo",     serverInfo);

        LOG.info("[McpRbacServer] Client initialized. MCP=" + MCP_VERSION);
        return buildResult(id, result);
    }

    // ─────────────────────────────────────────────────────────────────
    //  tools/list
    // ─────────────────────────────────────────────────────────────────

    private String handleToolsList(JsonNode id) throws Exception {
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", registry.getToolDefinitions());
        return buildResult(id, result);
    }

    // ─────────────────────────────────────────────────────────────────
    //  tools/call
    // ─────────────────────────────────────────────────────────────────

    private String handleToolCall(JsonNode id, JsonNode params) throws Exception {
        if (!params.has("name")) {
            return buildErrorResponse(id, -32602, "Invalid params: 'name' required");
        }
        String toolName = params.get("name").asText();
        JsonNode args   = params.has("arguments") ? params.get("arguments") : mapper.createObjectNode();

        LOG.info("[tools/call] tool=" + toolName);
        audit.log("TOOL_CALL", toolName, args.toString());

        try {
            JsonNode toolResult = registry.call(toolName, args);
            ObjectNode result   = mapper.createObjectNode();
            ArrayNode  content  = mapper.createArrayNode();

            ObjectNode textBlock = mapper.createObjectNode();
            textBlock.put("type", "text");
            textBlock.put("text", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(toolResult));
            content.add(textBlock);

            result.set("content",  content);
            result.put("isError", false);

            audit.log("TOOL_RESULT", toolName, "success");
            return buildResult(id, result);

        } catch (ToolNotFoundException e) {
            return buildErrorResponse(id, -32601, "Tool not found: " + toolName);
        } catch (ValidationException e) {
            audit.log("VALIDATION_FAIL", toolName, e.getMessage());
            return buildErrorResponse(id, -32602, "Validation error: " + e.getMessage());
        } catch (Exception e) {
            audit.log("TOOL_ERROR", toolName, e.getMessage());
            LOG.severe("[tools/call] Error in tool " + toolName + ": " + e.getMessage());
            return buildErrorResponse(id, -32603, "Internal error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────────────────────────

    private String buildResult(JsonNode id, JsonNode result) throws Exception {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null && !id.isNull()) resp.set("id", id);
        resp.set("result", result);
        return mapper.writeValueAsString(resp);
    }

    private String buildErrorResponse(JsonNode id, int code, String message) {
        try {
            ObjectNode resp  = mapper.createObjectNode();
            ObjectNode error = mapper.createObjectNode();
            error.put("code",    code);
            error.put("message", message);
            resp.put("jsonrpc", "2.0");
            if (id != null && !id.isNull()) resp.set("id", id);
            resp.set("error", error);
            return mapper.writeValueAsString(resp);
        } catch (Exception ex) {
            return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32700,\"message\":\"serialization failure\"}}";
        }
    }

    private JsonNode extractId(JsonNode req) {
        return req != null && req.has("id") ? req.get("id") : null;
    }
}
