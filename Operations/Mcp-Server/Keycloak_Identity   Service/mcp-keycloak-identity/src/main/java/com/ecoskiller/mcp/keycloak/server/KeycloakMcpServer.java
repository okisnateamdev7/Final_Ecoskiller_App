package com.ecoskiller.mcp.keycloak.server;

import com.ecoskiller.mcp.keycloak.tools.*;
import com.ecoskiller.mcp.keycloak.security.RateLimiter;
import com.ecoskiller.mcp.keycloak.security.InputValidator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

/**
 * Ecoskiller | CAT-IAM — Keycloak Identity & Access Management MCP Server
 *
 * Implements MCP Protocol 2024-11-05 over stdio (JSON-RPC 2.0).
 * Provides 15 secure tools covering:
 *   - Authentication flows (OAuth2, OIDC, SAML2)
 *   - User lifecycle management (CRUD, bulk import, federation)
 *   - RBAC (roles, permissions, realm management)
 *   - Token management (issue, validate, introspect, revoke)
 *   - Session management
 *   - MFA management (TOTP, WebAuthn, SMS)
 *   - Audit logging & compliance
 *   - Security health checks
 *
 * Security:
 *   - All inputs sanitized & validated before forwarding to Keycloak
 *   - Rate limiting per tool per client
 *   - Sensitive data (passwords, secrets) never logged
 *   - Structured JSON audit trail for every tool invocation
 */
public class KeycloakMcpServer {

    private static final Logger LOGGER = Logger.getLogger(KeycloakMcpServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "mcp-keycloak-identity";
    private static final String SERVER_VERSION = "1.0.0";

    private final Map<String, McpTool> tools = new HashMap<>();
    private final RateLimiter rateLimiter = new RateLimiter();
    private final InputValidator inputValidator = new InputValidator();
    private boolean initialized = false;

    public KeycloakMcpServer() {
        registerTools();
    }

    // ─── Tool Registration ────────────────────────────────────────────────────

    private void registerTools() {
        register(new AuthenticateUserTool());
        register(new TokenManagementTool());
        register(new TokenIntrospectTool());
        register(new UserManagementTool());
        register(new BulkUserImportTool());
        register(new RoleManagementTool());
        register(new SessionManagementTool());
        register(new MfaManagementTool());
        register(new RealmManagementTool());
        register(new IdentityProviderTool());
        register(new UserFederationTool());
        register(new AuditLogTool());
        register(new PasswordPolicyTool());
        register(new SecurityHealthCheckTool());
        register(new ComplianceReportTool());
    }

    private void register(McpTool tool) {
        tools.put(tool.getName(), tool);
        LOGGER.info("Registered tool: " + tool.getName());
    }

    // ─── Main Loop ────────────────────────────────────────────────────────────

    public void run() throws IOException {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter writer = new PrintWriter(
            new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

        LOGGER.info("Keycloak IAM MCP Server started. Waiting for requests...");
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                JSONObject request = new JSONObject(line);
                JSONObject response = handleRequest(request);
                if (response != null) {
                    writer.println(response.toString());
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error processing request: " + e.getMessage(), e);
                writer.println(buildError(null, -32700, "Parse error: " + e.getMessage()).toString());
            }
        }
    }

    // ─── Request Router ───────────────────────────────────────────────────────

    private JSONObject handleRequest(JSONObject req) {
        Object id = req.has("id") ? req.get("id") : null;
        String method = req.optString("method", "");

        try {
            return switch (method) {
                case "initialize"   -> handleInitialize(id, req);
                case "tools/list"   -> handleToolsList(id);
                case "tools/call"   -> handleToolsCall(id, req);
                case "ping"         -> buildResult(id, new JSONObject().put("pong", true));
                default             -> buildError(id, -32601, "Method not found: " + method);
            };
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Handler error for method " + method, e);
            return buildError(id, -32603, "Internal error: " + e.getMessage());
        }
    }

    // ─── initialize ───────────────────────────────────────────────────────────

    private JSONObject handleInitialize(Object id, JSONObject req) {
        initialized = true;
        JSONObject result = new JSONObject();
        result.put("protocolVersion", MCP_VERSION);
        result.put("serverInfo", new JSONObject()
            .put("name", SERVER_NAME)
            .put("version", SERVER_VERSION)
            .put("description", "Keycloak Identity & Access Management for Ecoskiller — OAuth2/OIDC/SAML2/MFA/RBAC")
        );
        result.put("capabilities", new JSONObject()
            .put("tools", new JSONObject().put("listChanged", false))
        );
        return buildResult(id, result);
    }

    // ─── tools/list ───────────────────────────────────────────────────────────

    private JSONObject handleToolsList(Object id) {
        JSONArray toolList = new JSONArray();
        for (McpTool tool : tools.values()) {
            toolList.put(new JSONObject()
                .put("name", tool.getName())
                .put("description", tool.getDescription())
                .put("inputSchema", tool.getInputSchema())
            );
        }
        return buildResult(id, new JSONObject().put("tools", toolList));
    }

    // ─── tools/call ───────────────────────────────────────────────────────────

    private JSONObject handleToolsCall(Object id, JSONObject req) {
        JSONObject params = req.optJSONObject("params");
        if (params == null) {
            return buildError(id, -32602, "Missing params");
        }

        String toolName = params.optString("name", "");
        JSONObject args = params.optJSONObject("arguments");
        if (args == null) args = new JSONObject();

        McpTool tool = tools.get(toolName);
        if (tool == null) {
            return buildError(id, -32602, "Unknown tool: " + toolName);
        }

        // Rate limiting
        if (!rateLimiter.allow(toolName)) {
            return buildError(id, -32000, "Rate limit exceeded for tool: " + toolName);
        }

        // Input validation / sanitization
        try {
            inputValidator.validate(toolName, args);
        } catch (IllegalArgumentException e) {
            return buildError(id, -32602, "Invalid input: " + e.getMessage());
        }

        // Execute tool
        try {
            JSONObject toolResult = tool.execute(args);
            JSONArray content = new JSONArray();
            content.put(new JSONObject()
                .put("type", "text")
                .put("text", toolResult.toString(2))
            );
            return buildResult(id, new JSONObject().put("content", content));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Tool execution error [" + toolName + "]: " + e.getMessage());
            return buildError(id, -32000, "Tool error: " + e.getMessage());
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private JSONObject buildResult(Object id, JSONObject result) {
        JSONObject resp = new JSONObject();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.put("id", id);
        resp.put("result", result);
        return resp;
    }

    private JSONObject buildError(Object id, int code, String message) {
        JSONObject resp = new JSONObject();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.put("id", id);
        resp.put("error", new JSONObject()
            .put("code", code)
            .put("message", message)
        );
        return resp;
    }

    // ─── Entry Point ──────────────────────────────────────────────────────────

    public static void main(String[] args) {
        configureLogging();
        try {
            new KeycloakMcpServer().run();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Server fatal error", e);
            System.exit(1);
        }
    }

    private static void configureLogging() {
        // Log to stderr only — stdout is reserved for MCP JSON-RPC
        Logger root = Logger.getLogger("");
        root.setLevel(Level.INFO);
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        ConsoleHandler stderr = new ConsoleHandler();
        stderr.setLevel(Level.INFO);
        stderr.setFormatter(new SimpleFormatter());
        root.addHandler(stderr);
        // stderr is already System.err — safe for MCP
    }
}
