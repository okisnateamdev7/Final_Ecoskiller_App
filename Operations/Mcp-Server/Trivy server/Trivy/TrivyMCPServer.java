package com.ecoskiller.trivy.mcp;

import com.ecoskiller.trivy.mcp.security.JwtValidator;
import com.ecoskiller.trivy.mcp.security.AuditLogger;
import com.ecoskiller.trivy.mcp.security.RateLimiter;
import com.ecoskiller.trivy.mcp.tools.ToolRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Trivy Security Scanner Service — MCP Server (Java 17)
 * Ecoskiller Platform | ops namespace
 *
 * Transport  : stdio (stdin/stdout)
 * Protocol   : JSON-RPC 2.0 / MCP 2024-11-05
 * Security   : JWT (Keycloak RS256), RBAC, sliding-window rate limiting,
 *              structured JSON audit logging
 *
 * Agents (14):
 *  1.  scan_image              — Scan Docker image for CVEs (CRITICAL/HIGH/MEDIUM/LOW)
 *  2.  scan_registry           — Scan Harbor registry image by reference
 *  3.  scan_filesystem         — Scan filesystem/repo for vulnerabilities
 *  4.  scan_iac                — Scan Terraform/OpenTofu IaC for misconfigurations
 *  5.  scan_k8s_manifests      — Scan Kubernetes YAML/Helm for security misconfigurations
 *  6.  generate_sbom           — Generate CycloneDX/SPDX SBOM for an image
 *  7.  policy_check            — Evaluate scan results against environment policy (dev/stage/prod)
 *  8.  trivy_db_update         — Trigger/check vulnerability database update
 *  9.  exception_management    — Manage .trivyignore waivers (add/list/revoke)
 * 10.  export_sarif            — Export findings as SARIF 2.1.0 for IDE/GitLab SAST
 *  11. harbor_tag_metadata     — Write Trivy scan labels to Harbor image metadata
 *  12. ci_pipeline_gate        — CI/CD pipeline gate decision (pass/fail)
 *  13. compliance_report       — Generate NIST/OWASP/DPDPA/PCI-DSS compliance report
 *  14. scan_history            — Query historical scan results from ClickHouse
 */
public class TrivyMCPServer {

    private static final Logger LOG = Logger.getLogger(TrivyMCPServer.class.getName());
    private static final String MCP_VERSION    = "2024-11-05";
    private static final String SERVER_NAME    = "trivy-mcp-server";
    private static final String SERVER_VERSION = "1.0.0";

    private final ObjectMapper  mapper       = new ObjectMapper();
    private final Map<String, MCPTool> tools = new LinkedHashMap<>();
    private final JwtValidator  jwtValidator;
    private final AuditLogger   auditLogger;
    private final RateLimiter   rateLimiter;

    public TrivyMCPServer() {
        this.jwtValidator = new JwtValidator();
        this.auditLogger  = new AuditLogger();
        this.rateLimiter  = new RateLimiter(); // 100 req / 60 s per client
        for (MCPTool t : ToolRegistry.all()) tools.put(t.getName(), t);
        LOG.info("[TRIVY-MCP] Registered " + tools.size() + " tools");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Main stdin/stdout loop
    // ─────────────────────────────────────────────────────────────────────────

    public void start() throws IOException {
        LOG.info("[TRIVY-MCP] Server v" + SERVER_VERSION + " starting (stdio transport)");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    writer = new PrintWriter(new OutputStreamWriter(System.out), true);
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isBlank()) continue;
            String response = dispatch(line);
            if (response != null) { writer.println(response); writer.flush(); }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Request Dispatch
    // ─────────────────────────────────────────────────────────────────────────

    private String dispatch(String raw) {
        try {
            JsonNode req    = mapper.readTree(raw);
            String   id     = req.has("id") ? req.get("id").asText() : null;
            String   method = req.path("method").asText("");

            return switch (method) {
                case "initialize"  -> handleInitialize(id, req);
                case "ping"        -> buildResult(id, mapper.createObjectNode());
                case "tools/list"  -> handleToolsList(id, req);
                case "tools/call"  -> handleToolCall(id, req);
                default            -> buildError(id, -32601, "Method not found: " + method);
            };
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Parse error", e);
            return buildError(null, -32700, "Parse error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Handlers
    // ─────────────────────────────────────────────────────────────────────────

    private String handleInitialize(String id, JsonNode req) throws Exception {
        ObjectNode caps  = mapper.createObjectNode();
        caps.putObject("tools").put("listChanged", false);

        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);
        result.set("capabilities", caps);
        ObjectNode info = result.putObject("serverInfo");
        info.put("name", SERVER_NAME);
        info.put("version", SERVER_VERSION);

        String client = req.path("params").path("clientInfo").path("name").asText("unknown");
        LOG.info("[TRIVY-MCP] Client initialized: " + client);
        return buildResult(id, result);
    }

    private String handleToolsList(String id, JsonNode req) throws Exception {
        // Require valid JWT even for listing tools (prevents enumeration)
        String clientId = clientId(req);
        if (!jwtValidator.validate(req)) {
            auditLogger.warn(clientId, "tools/list", "UNAUTHORIZED");
            return buildError(id, -32001, "Unauthorized: valid JWT required");
        }
        ArrayNode arr = mapper.createArrayNode();
        for (MCPTool t : tools.values()) arr.add(t.getSchema(mapper));
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", arr);
        return buildResult(id, result);
    }

    private String handleToolCall(String id, JsonNode req) throws Exception {
        JsonNode params   = req.path("params");
        String   toolName = params.path("name").asText();
        JsonNode args     = params.path("arguments");
        String   clientId = clientId(req);

        // ── 1. Authentication ──────────────────────────────────────────────
        if (!jwtValidator.validate(req)) {
            auditLogger.warn(clientId, "call:" + toolName, "UNAUTHORIZED");
            return buildError(id, -32001, "Unauthorized: valid JWT required");
        }
        // ── 2. Rate Limiting ───────────────────────────────────────────────
        if (!rateLimiter.allow(clientId)) {
            auditLogger.warn(clientId, "call:" + toolName, "RATE_LIMITED");
            return buildError(id, -32029, "Rate limit exceeded (max 100 req/min)");
        }
        // ── 3. Tool Lookup ─────────────────────────────────────────────────
        MCPTool tool = tools.get(toolName);
        if (tool == null) return buildError(id, -32602, "Unknown tool: " + toolName);

        // ── 4. RBAC ────────────────────────────────────────────────────────
        String role = jwtValidator.extractRole(req);
        if (!tool.isAllowedRole(role)) {
            auditLogger.warn(clientId, "call:" + toolName, "FORBIDDEN role=" + role);
            return buildError(id, -32003, "Forbidden: role '" + role + "' cannot call " + toolName);
        }
        // ── 5. Input Validation ────────────────────────────────────────────
        Optional<String> err = tool.validateArgs(args);
        if (err.isPresent()) return buildError(id, -32602, "Invalid arguments: " + err.get());

        // ── 6. Execute ─────────────────────────────────────────────────────
        auditLogger.info(clientId, "call:" + toolName, "INVOKED");
        long start = System.currentTimeMillis();
        try {
            JsonNode output = tool.execute(args, mapper);
            long ms = System.currentTimeMillis() - start;
            auditLogger.info(clientId, "call:" + toolName, "OK latency=" + ms + "ms");

            ArrayNode content = mapper.createArrayNode();
            ObjectNode block  = mapper.createObjectNode();
            block.put("type", "text");
            block.put("text", mapper.writeValueAsString(output));
            content.add(block);

            ObjectNode result = mapper.createObjectNode();
            result.set("content", content);
            result.put("isError", false);
            return buildResult(id, result);

        } catch (Exception e) {
            long ms = System.currentTimeMillis() - start;
            auditLogger.error(clientId, "call:" + toolName, e.getMessage() + " latency=" + ms + "ms");
            LOG.log(Level.SEVERE, "Tool execution error: " + toolName, e);
            return buildError(id, -32000, "Execution error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private String clientId(JsonNode req) {
        return req.path("params").path("clientInfo").path("name").asText("anonymous");
    }

    private String buildResult(String id, JsonNode result) {
        try {
            ObjectNode r = mapper.createObjectNode();
            r.put("jsonrpc", "2.0");
            if (id != null) r.put("id", id);
            r.set("result", result);
            return mapper.writeValueAsString(r);
        } catch (Exception e) {
            return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32700,\"message\":\"Serialization error\"}}";
        }
    }

    private String buildError(String id, int code, String message) {
        try {
            ObjectNode r  = mapper.createObjectNode();
            r.put("jsonrpc", "2.0");
            if (id != null) r.put("id", id);
            ObjectNode err = r.putObject("error");
            err.put("code", code);
            err.put("message", message);
            return mapper.writeValueAsString(r);
        } catch (Exception e) {
            return "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32700,\"message\":\"Fatal error\"}}";
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Entry Point
    // ─────────────────────────────────────────────────────────────────────────

    public static void main(String[] args) throws IOException {
        // Route JUL logs to stderr — stdout must stay clean for JSON-RPC
        Logger root = Logger.getLogger("");
        Arrays.stream(root.getHandlers()).forEach(h -> {
            if (h instanceof java.util.logging.ConsoleHandler) {
                ((java.util.logging.ConsoleHandler) h).setOutputStream(System.err);
            }
        });
        new TrivyMCPServer().start();
    }
}
