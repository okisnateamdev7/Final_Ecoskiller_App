package com.ecoskiller.mcp.irs.handler;

import com.ecoskiller.mcp.irs.security.InputValidator;
import com.ecoskiller.mcp.irs.tool.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Routes incoming MCP JSON-RPC requests to the appropriate tool handlers.
 * Handles: initialize, tools/list, tools/call, ping
 */
public class McpRequestHandler {

    private static final Logger LOGGER = Logger.getLogger(McpRequestHandler.class.getName());

    private final Map<String, IrsTool> tools;
    private final InputValidator validator;

    public McpRequestHandler() {
        this.validator = new InputValidator();
        this.tools = Map.ofEntries(
            Map.entry("register_idea",            new RegisterIdeaTool()),
            Map.entry("get_idea",                 new GetIdeaTool()),
            Map.entry("update_idea",              new UpdateIdeaTool()),
            Map.entry("search_similar_ideas",     new SearchSimilarIdeasTool()),
            Map.entry("add_co_owner",             new AddCoOwnerTool()),
            Map.entry("transfer_ownership",       new TransferOwnershipTool()),
            Map.entry("search_ideas",             new SearchIdeasTool()),
            Map.entry("bulk_analyze_ideas",       new BulkAnalyzeIdeasTool()),
            Map.entry("get_audit_log",            new GetAuditLogTool()),
            Map.entry("submit_for_approval",      new SubmitForApprovalTool()),
            Map.entry("approve_idea",             new ApproveIdeaTool()),
            Map.entry("license_idea",             new LicenseIdeaTool()),
            Map.entry("archive_idea",             new ArchiveIdeaTool()),
            Map.entry("verify_idea_dna",          new VerifyIdeaDnaTool()),
            Map.entry("get_licensing_status",     new GetLicensingStatusTool()),
            Map.entry("flag_idea",                new FlagIdeaTool()),
            Map.entry("get_similarity_report",    new GetSimilarityReportTool()),
            Map.entry("health_check",             new HealthCheckTool())
        );
    }

    public String handle(String method, JsonNode id, JsonNode params,
                         ObjectMapper mapper, String mcpVersion,
                         String serverName, String serverVersion) throws Exception {

        return switch (method) {
            case "initialize"   -> handleInitialize(id, params, mapper, mcpVersion, serverName, serverVersion);
            case "tools/list"   -> handleToolsList(id, mapper);
            case "tools/call"   -> handleToolsCall(id, params, mapper);
            case "ping"         -> handlePing(id, mapper);
            default             -> createErrorResponse(id, -32601, "Method not found: " + sanitize(method), mapper);
        };
    }

    // ── initialize ────────────────────────────────────────────────────────────

    private String handleInitialize(JsonNode id, JsonNode params, ObjectMapper mapper,
                                    String mcpVersion, String serverName, String serverVersion) throws Exception {
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", mcpVersion);

        ObjectNode serverInfo = mapper.createObjectNode();
        serverInfo.put("name", serverName);
        serverInfo.put("version", serverVersion);
        result.set("serverInfo", serverInfo);

        ObjectNode capabilities = mapper.createObjectNode();
        capabilities.set("tools", mapper.createObjectNode());
        result.set("capabilities", capabilities);

        return success(id, result, mapper);
    }

    // ── tools/list ────────────────────────────────────────────────────────────

    private String handleToolsList(JsonNode id, ObjectMapper mapper) throws Exception {
        ArrayNode toolsArray = mapper.createArrayNode();

        for (Map.Entry<String, IrsTool> entry : tools.entrySet()) {
            IrsTool tool = entry.getValue();
            ObjectNode toolDef = mapper.createObjectNode();
            toolDef.put("name", entry.getKey());
            toolDef.put("description", tool.getDescription());
            toolDef.set("inputSchema", mapper.readTree(tool.getInputSchema()));
            toolsArray.add(toolDef);
        }

        ObjectNode result = mapper.createObjectNode();
        result.set("tools", toolsArray);
        return success(id, result, mapper);
    }

    // ── tools/call ────────────────────────────────────────────────────────────

    private String handleToolsCall(JsonNode id, JsonNode params, ObjectMapper mapper) throws Exception {
        if (!params.has("name")) {
            return createErrorResponse(id, -32602, "Invalid params: missing tool name", mapper);
        }

        String toolName = params.get("name").asText();

        // Security: validate tool name to prevent injection
        if (!validator.isValidToolName(toolName)) {
            return createErrorResponse(id, -32602, "Invalid tool name", mapper);
        }

        IrsTool tool = tools.get(toolName);
        if (tool == null) {
            return createErrorResponse(id, -32601, "Tool not found: " + sanitize(toolName), mapper);
        }

        JsonNode toolParams = params.has("arguments") ? params.get("arguments") : mapper.createObjectNode();

        // Security: validate params before passing to tool
        String validationError = validator.validateParams(toolParams);
        if (validationError != null) {
            return createErrorResponse(id, -32602, "Invalid params: " + validationError, mapper);
        }

        try {
            ToolResult toolResult = tool.execute(toolParams, mapper);
            return buildToolCallResponse(id, toolResult, mapper);
        } catch (SecurityException e) {
            LOGGER.warning("Security violation in tool " + toolName + ": " + e.getMessage());
            return createErrorResponse(id, -32603, "Security violation", mapper);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(id, -32602, "Invalid argument: " + sanitize(e.getMessage()), mapper);
        } catch (Exception e) {
            LOGGER.warning("Tool execution error: " + e.getMessage());
            return createErrorResponse(id, -32603, "Tool execution error", mapper);
        }
    }

    // ── ping ──────────────────────────────────────────────────────────────────

    private String handlePing(JsonNode id, ObjectMapper mapper) throws Exception {
        return success(id, mapper.createObjectNode(), mapper);
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private String buildToolCallResponse(JsonNode id, ToolResult result, ObjectMapper mapper) throws Exception {
        ObjectNode response = mapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        if (id != null) response.set("id", id); else response.putNull("id");

        ObjectNode resultNode = mapper.createObjectNode();
        ArrayNode content = mapper.createArrayNode();

        ObjectNode textContent = mapper.createObjectNode();
        textContent.put("type", "text");
        textContent.put("text", mapper.writeValueAsString(result.getData()));
        content.add(textContent);

        resultNode.set("content", content);
        resultNode.put("isError", result.isError());
        response.set("result", resultNode);
        return mapper.writeValueAsString(response);
    }

    private String success(JsonNode id, ObjectNode result, ObjectMapper mapper) throws Exception {
        ObjectNode response = mapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        if (id != null) response.set("id", id); else response.putNull("id");
        response.set("result", result);
        return mapper.writeValueAsString(response);
    }

    private String createErrorResponse(JsonNode id, int code, String message, ObjectMapper mapper) {
        try {
            ObjectNode response = mapper.createObjectNode();
            response.put("jsonrpc", "2.0");
            if (id != null) response.set("id", id); else response.putNull("id");
            ObjectNode error = mapper.createObjectNode();
            error.put("code", code);
            error.put("message", message);
            response.set("error", error);
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return "{\"jsonrpc\":\"2.0\",\"id\":null,\"error\":{\"code\":-32603,\"message\":\"Internal error\"}}";
        }
    }

    private String sanitize(String input) {
        if (input == null) return "";
        return input.replaceAll("[^a-zA-Z0-9_\\-./: ]", "").substring(0, Math.min(input.length(), 100));
    }
}
