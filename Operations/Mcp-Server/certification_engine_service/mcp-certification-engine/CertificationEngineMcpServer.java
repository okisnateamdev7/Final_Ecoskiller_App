package com.ecoskiller.mcp.server;

import com.ecoskiller.mcp.security.SecurityContext;
import com.ecoskiller.mcp.tools.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * Ecoskiller | Certification Engine — MCP Server (Java)
 *
 * Transport:    stdio / JSON-RPC 2.0
 * MCP Version:  2024-11-05
 * Priority:     HIGH
 * Security:     JWT/RBAC enforced per tool call (Keycloak 24.0)
 *
 * 18 tools covering:
 *   - Certificate issuance, revocation & verification
 *   - Belt level management & progression
 *   - Eligibility evaluation & rule management
 *   - Mentor verification workflows
 *   - Kafka event consumption & publication
 *   - MinIO/object-storage certificate file management
 *   - Audit logging & compliance
 *   - QR code verification
 *   - Multi-tenant isolation throughout
 */
public class CertificationEngineMcpServer {

    private static final Logger LOG = Logger.getLogger(CertificationEngineMcpServer.class.getName());
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Map<String, McpTool> toolRegistry = new LinkedHashMap<>();
    private final SecurityContext securityContext = new SecurityContext();

    public CertificationEngineMcpServer() {
        registerTools();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool Registration — 18 tools
    // ─────────────────────────────────────────────────────────────────────────

    private void registerTools() {
        // Certificate lifecycle
        register(new IssueCertificateTool(securityContext));
        register(new RevokeCertificateTool(securityContext));
        register(new GetCertificateDetailsTool(securityContext));
        register(new VerifyCertificateQrTool(securityContext));
        register(new ListCandidateCertificatesTool(securityContext));

        // Belt management
        register(new GetBeltHierarchyTool(securityContext));
        register(new AssignBeltLevelTool(securityContext));
        register(new GetCandidateBeltStatusTool(securityContext));
        register(new PromoteBeltLevelTool(securityContext));

        // Eligibility
        register(new EvaluateEligibilityTool(securityContext));
        register(new GetEligibilityRulesTool(securityContext));
        register(new UpdateEligibilityRulesTool(securityContext));

        // Mentor verification
        register(new TriggerMentorVerificationTool(securityContext));
        register(new GetMentorVerificationStatusTool(securityContext));

        // Kafka / events
        register(new IngestCertificationEventTool(securityContext));
        register(new PublishCertificationEventTool(securityContext));

        // Storage & audit
        register(new GetCertificateFileTool(securityContext));
        register(new GetCertificationAuditLogTool(securityContext));
    }

    private void register(McpTool tool) {
        toolRegistry.put(tool.getName(), tool);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Main I/O loop — stdin/stdout JSON-RPC 2.0
    // ─────────────────────────────────────────────────────────────────────────

    public void run() throws IOException {
        LOG.info("Ecoskiller Certification Engine MCP Server starting — awaiting JSON-RPC on stdin");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer   = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String response;
            try {
                response = handleRequest(MAPPER.readTree(line));
            } catch (Exception e) {
                LOG.warning("Parse error: " + e.getMessage());
                response = errorResponse(null, -32700, "Parse error: " + e.getMessage());
            }
            writer.println(response);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // JSON-RPC Dispatch
    // ─────────────────────────────────────────────────────────────────────────

    private String handleRequest(JsonNode req) throws Exception {
        String id     = req.has("id")     ? req.get("id").asText()     : null;
        String method = req.has("method") ? req.get("method").asText() : "";
        JsonNode params = req.has("params") ? req.get("params") : MAPPER.createObjectNode();

        return switch (method) {
            case "initialize"  -> handleInitialize(id, params);
            case "ping"        -> successResponse(id, MAPPER.createObjectNode());
            case "tools/list"  -> handleToolsList(id);
            case "tools/call"  -> handleToolCall(id, params);
            default            -> errorResponse(id, -32601, "Method not found: " + method);
        };
    }

    private String handleInitialize(String id, JsonNode params) throws Exception {
        ObjectNode result = MAPPER.createObjectNode();
        result.put("protocolVersion", "2024-11-05");
        result.putObject("capabilities").putObject("tools");
        ObjectNode info = result.putObject("serverInfo");
        info.put("name",        "certification-engine-mcp");
        info.put("version",     "1.0.0");
        info.put("description",
            "Ecoskiller Certification Engine MCP Server. " +
            "18 tools: certificate issuance/revocation/verification, belt level management, " +
            "eligibility rule evaluation, mentor verification, Kafka event handling, " +
            "MinIO file storage, and audit logging. JWT/RBAC enforced on every call.");
        return successResponse(id, result);
    }

    private String handleToolsList(String id) throws Exception {
        ArrayNode tools = MAPPER.createArrayNode();
        for (McpTool t : toolRegistry.values()) tools.add(t.getSchema());
        ObjectNode result = MAPPER.createObjectNode();
        result.set("tools", tools);
        return successResponse(id, result);
    }

    private String handleToolCall(String id, JsonNode params) throws Exception {
        String toolName = params.has("name")      ? params.get("name").asText()      : "";
        JsonNode args   = params.has("arguments") ? params.get("arguments")          : MAPPER.createObjectNode();

        // ── Security: require bearer_token ────────────────────────────────
        if (!args.has("bearer_token") || args.get("bearer_token").asText().isBlank()) {
            return errorResponse(id, -32001,
                "UNAUTHORIZED: bearer_token is required in arguments for all tool calls. " +
                "Provide a valid Keycloak-issued JWT.");
        }

        SecurityContext.ValidationResult auth =
            securityContext.validateToken(args.get("bearer_token").asText());
        if (!auth.valid()) {
            return errorResponse(id, -32001, "UNAUTHORIZED: " + auth.reason());
        }

        McpTool tool = toolRegistry.get(toolName);
        if (tool == null) {
            return errorResponse(id, -32602, "Unknown tool: " + toolName);
        }

        if (!tool.isAuthorized(auth)) {
            return errorResponse(id, -32001,
                "FORBIDDEN: role '" + auth.role() + "' cannot call '" + toolName +
                "'. Required: " + tool.requiredRole());
        }

        try {
            JsonNode toolResult = tool.execute(args, auth);
            ObjectNode content  = MAPPER.createObjectNode();
            ArrayNode  arr      = content.putArray("content");
            ObjectNode block    = arr.addObject();
            block.put("type", "text");
            block.put("text", MAPPER.writeValueAsString(toolResult));
            content.put("isError", false);
            return successResponse(id, content);
        } catch (SecurityException se) {
            return errorResponse(id, -32001, "FORBIDDEN: " + se.getMessage());
        } catch (IllegalArgumentException iae) {
            return errorResponse(id, -32602, "Invalid params: " + iae.getMessage());
        } catch (Exception e) {
            LOG.warning("Tool error [" + toolName + "]: " + e.getMessage());
            return errorResponse(id, -32603, "Internal error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Response helpers
    // ─────────────────────────────────────────────────────────────────────────

    private String successResponse(String id, JsonNode result) throws Exception {
        ObjectNode r = MAPPER.createObjectNode();
        r.put("jsonrpc", "2.0");
        if (id != null) r.put("id", id);
        r.set("result", result);
        return MAPPER.writeValueAsString(r);
    }

    private String errorResponse(String id, int code, String message) {
        try {
            ObjectNode r = MAPPER.createObjectNode();
            r.put("jsonrpc", "2.0");
            if (id != null) r.put("id", id);
            ObjectNode err = r.putObject("error");
            err.put("code", code);
            err.put("message", message);
            return MAPPER.writeValueAsString(r);
        } catch (Exception e) {
            return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32603,\"message\":\"Internal error\"}}";
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Entry point
    // ─────────────────────────────────────────────────────────────────────────

    public static void main(String[] args) throws IOException {
        Logger root = Logger.getLogger("");
        root.setLevel(Level.INFO);
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        StreamHandler sh = new StreamHandler(System.err, new SimpleFormatter());
        sh.setLevel(Level.INFO);
        root.addHandler(sh);
        new CertificationEngineMcpServer().run();
    }
}
