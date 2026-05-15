package com.ecoskiller.billing.server;

import com.ecoskiller.billing.security.RequestValidator;
import com.ecoskiller.billing.tools.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * EcoSkiller Billing Service — MCP Server (Java)
 *
 * Transport : stdio (JSON-RPC 2.0)
 * MCP Version: 2024-11-05
 * Namespace  : billing
 *
 * Security baseline:
 *   - All tool inputs are validated and sanitised before processing
 *   - Tenant ID is extracted from JWT claims — never from request body
 *   - Immutable audit records written for every financial mutation
 *   - No sensitive credential is logged at any level
 */
public class BillingMcpServer {

    private static final Logger LOG = Logger.getLogger(BillingMcpServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_VERSION = "1.0.0";

    private final ObjectMapper mapper = new ObjectMapper();
    private final ToolRegistry registry;
    private final RequestValidator validator;

    public BillingMcpServer() {
        this.validator = new RequestValidator();
        this.registry = new ToolRegistry(validator);
    }

    // ─────────────────────────────────────────────
    //  Entry point
    // ─────────────────────────────────────────────

    public static void main(String[] args) {
        // Silence java.util.logging to stderr so it doesn't corrupt the MCP stream
        Logger.getLogger("").setLevel(Level.WARNING);
        new BillingMcpServer().run();
    }

    public void run() {
        try (BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)))) {

            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    JsonNode request  = mapper.readTree(line);
                    JsonNode response = dispatch(request);
                    if (response != null) {
                        out.println(mapper.writeValueAsString(response));
                        out.flush();
                    }
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Failed to process request", e);
                    // Emit a parse-error response so the client isn't left hanging
                    ObjectNode err = buildError(null, -32700, "Parse error: " + sanitiseMessage(e.getMessage()), null);
                    try {
                        out.println(mapper.writeValueAsString(err));
                        out.flush();
                    } catch (Exception ignored) { /* nothing we can do */ }
                }
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "stdio error", e);
        }
    }

    // ─────────────────────────────────────────────
    //  Dispatcher
    // ─────────────────────────────────────────────

    private JsonNode dispatch(JsonNode req) {
        JsonNode idNode = req.get("id");
        String   method = req.path("method").asText("");
        JsonNode params = req.path("params");

        return switch (method) {
            case "initialize"   -> handleInitialize(idNode, params);
            case "tools/list"   -> handleToolsList(idNode);
            case "tools/call"   -> handleToolsCall(idNode, params);
            case "ping"         -> buildResult(idNode, mapper.createObjectNode());
            default             -> buildError(idNode, -32601, "Method not found: " + method, null);
        };
    }

    // ─────────────────────────────────────────────
    //  initialize
    // ─────────────────────────────────────────────

    private JsonNode handleInitialize(JsonNode id, JsonNode params) {
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);

        ObjectNode serverInfo = result.putObject("serverInfo");
        serverInfo.put("name", "ecoskiller-billing-mcp");
        serverInfo.put("version", SERVER_VERSION);
        serverInfo.put("description",
            "EcoSkiller Billing Service MCP — subscriptions, invoices, payments, entitlements");

        ObjectNode capabilities = result.putObject("capabilities");
        capabilities.putObject("tools");

        return buildResult(id, result);
    }

    // ─────────────────────────────────────────────
    //  tools/list
    // ─────────────────────────────────────────────

    private JsonNode handleToolsList(JsonNode id) {
        ArrayNode tools = registry.listTools();
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", tools);
        return buildResult(id, result);
    }

    // ─────────────────────────────────────────────
    //  tools/call
    // ─────────────────────────────────────────────

    private JsonNode handleToolsCall(JsonNode id, JsonNode params) {
        String toolName = params.path("name").asText("");
        JsonNode args   = params.path("arguments");

        // Basic input guard — registry will validate further per-tool
        if (toolName.isEmpty()) {
            return buildError(id, -32602, "Missing tool name", null);
        }

        try {
            JsonNode toolResult = registry.call(toolName, args);
            ObjectNode result = mapper.createObjectNode();
            ArrayNode content = result.putArray("content");
            ObjectNode textBlock = content.addObject();
            textBlock.put("type", "text");
            textBlock.put("text", mapper.writeValueAsString(toolResult));
            return buildResult(id, result);
        } catch (ToolNotFoundException e) {
            return buildError(id, -32601, "Tool not found: " + toolName, null);
        } catch (ValidationException e) {
            return buildError(id, -32602, "Invalid params: " + sanitiseMessage(e.getMessage()), null);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Tool execution error", e);
            return buildError(id, -32603, "Internal error", null);
        }
    }

    // ─────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────

    private ObjectNode buildResult(JsonNode id, JsonNode result) {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.set("id", id);
        resp.set("result", result);
        return resp;
    }

    private ObjectNode buildError(JsonNode id, int code, String message, JsonNode data) {
        ObjectNode resp  = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.set("id", id);
        ObjectNode error = resp.putObject("error");
        error.put("code", code);
        error.put("message", message);
        if (data != null) error.set("data", data);
        return resp;
    }

    /** Strip any potentially sensitive details from exception messages before sending over the wire. */
    private String sanitiseMessage(String msg) {
        if (msg == null) return "unknown error";
        // Remove anything that looks like a connection string, password, or stack path
        return msg.replaceAll("(?i)(password|secret|token|key)=[^\\s,;]+", "$1=***")
                  .replaceAll("/home/[^\\s]+", "<path>")
                  .substring(0, Math.min(msg.length(), 200));
    }
}
