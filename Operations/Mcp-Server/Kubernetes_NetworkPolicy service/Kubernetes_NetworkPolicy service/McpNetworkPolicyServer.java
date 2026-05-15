package com.ecoskiller.mcp.networkpolicy.server;

import com.ecoskiller.mcp.networkpolicy.security.SecurityManager;
import com.ecoskiller.mcp.networkpolicy.tools.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ecoskiller Kubernetes NetworkPolicy MCP Server
 *
 * CAT-INFRA | Kubernetes NetworkPolicy — Pod-Level Network Security & Traffic Control
 * Transport: stdio | Protocol: JSON-RPC 2.0 | MCP Version: 2024-11-05
 *
 * Tools (14 agents):
 *   1.  apply_network_policy        — Apply/update a NetworkPolicy YAML to a namespace
 *   2.  get_network_policy          — Fetch a specific NetworkPolicy by name & namespace
 *   3.  list_network_policies       — List all NetworkPolicies (optionally filter by namespace)
 *   4.  delete_network_policy       — Delete a NetworkPolicy
 *   5.  validate_network_policy     — Dry-run validate policy YAML before applying
 *   6.  apply_default_deny          — Deploy default-deny-all ingress+egress policy to a namespace
 *   7.  check_pod_connectivity      — Check if two pods can communicate per active policies
 *   8.  get_policy_violations       — Query policy violation logs/metrics from Prometheus
 *   9.  list_policy_metrics         — Fetch enforcement metrics: allowed/denied/latency
 *  10.  get_namespace_isolation      — Show isolation posture for a namespace (all policies)
 *  11.  audit_policy_changes        — Return K8s audit log entries for policy create/update/delete
 *  12.  export_compliance_report    — Generate SOC2/HIPAA/GDPR compliance snapshot
 *  13.  sync_policies_gitops        — Trigger ArgoCD/Flux reconcile for network policy changes
 *  14.  emergency_allow_traffic     — Create a temporary emergency allow rule (auto-expiry TTL)
 */
public class McpNetworkPolicyServer {

    private static final Logger LOG = Logger.getLogger(McpNetworkPolicyServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "ecoskiller-k8s-networkpolicy";
    private static final String SERVER_VERSION = "1.0.0";

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, ToolHandler> tools = new HashMap<>();
    private final SecurityManager securityManager;

    public McpNetworkPolicyServer() {
        this.securityManager = new SecurityManager();
        registerTools();
    }

    private void registerTools() {
        tools.put("apply_network_policy",      new ApplyNetworkPolicyTool(securityManager));
        tools.put("get_network_policy",        new GetNetworkPolicyTool(securityManager));
        tools.put("list_network_policies",     new ListNetworkPoliciesTool(securityManager));
        tools.put("delete_network_policy",     new DeleteNetworkPolicyTool(securityManager));
        tools.put("validate_network_policy",   new ValidateNetworkPolicyTool(securityManager));
        tools.put("apply_default_deny",        new ApplyDefaultDenyTool(securityManager));
        tools.put("check_pod_connectivity",    new CheckPodConnectivityTool(securityManager));
        tools.put("get_policy_violations",     new GetPolicyViolationsTool(securityManager));
        tools.put("list_policy_metrics",       new ListPolicyMetricsTool(securityManager));
        tools.put("get_namespace_isolation",   new GetNamespaceIsolationTool(securityManager));
        tools.put("audit_policy_changes",      new AuditPolicyChangesTool(securityManager));
        tools.put("export_compliance_report",  new ExportComplianceReportTool(securityManager));
        tools.put("sync_policies_gitops",      new SyncPoliciesGitOpsTool(securityManager));
        tools.put("emergency_allow_traffic",   new EmergencyAllowTrafficTool(securityManager));
    }

    public void run() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        String line;

        LOG.info("[MCP] " + SERVER_NAME + " v" + SERVER_VERSION + " started (stdio)");

        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String response = null;
            try {
                response = handleMessage(line);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Unhandled error", e);
                response = errorResponse(null, -32603, "Internal error: " + e.getMessage());
            }

            if (response != null) {
                out.println(response);
                out.flush();
            }
        }
    }

    private String handleMessage(String raw) {
        JsonNode req;
        try {
            req = mapper.readTree(raw);
        } catch (Exception e) {
            return errorResponse(null, -32700, "Parse error: invalid JSON");
        }

        JsonNode idNode = req.get("id");
        Object id = idNode == null ? null : (idNode.isInt() ? idNode.asInt() : idNode.asText());
        String method = req.path("method").asText(null);

        if (method == null) {
            return errorResponse(id, -32600, "Invalid Request: missing 'method'");
        }

        try {
            return switch (method) {
                case "initialize"   -> handleInitialize(id, req);
                case "ping"         -> handlePing(id);
                case "tools/list"   -> handleToolsList(id);
                case "tools/call"   -> handleToolsCall(id, req);
                default             -> errorResponse(id, -32601, "Method not found: " + method);
            };
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Error handling method=" + method, e);
            return errorResponse(id, -32603, "Internal error: " + e.getMessage());
        }
    }

    // ─── Handlers ────────────────────────────────────────────────────────────

    private String handleInitialize(Object id, JsonNode req) {
        ObjectNode caps = mapper.createObjectNode();
        caps.putObject("tools");

        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);
        result.set("capabilities", caps);

        ObjectNode serverInfo = mapper.createObjectNode();
        serverInfo.put("name", SERVER_NAME);
        serverInfo.put("version", SERVER_VERSION);
        result.set("serverInfo", serverInfo);

        return successResponse(id, result);
    }

    private String handlePing(Object id) {
        return successResponse(id, mapper.createObjectNode());
    }

    private String handleToolsList(Object id) {
        ArrayNode toolsArray = mapper.createArrayNode();
        for (Map.Entry<String, ToolHandler> entry : tools.entrySet()) {
            toolsArray.add(entry.getValue().getSchema());
        }
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", toolsArray);
        return successResponse(id, result);
    }

    private String handleToolsCall(Object id, JsonNode req) {
        JsonNode params = req.path("params");
        String toolName = params.path("name").asText(null);
        JsonNode arguments = params.path("arguments");

        if (toolName == null) {
            return errorResponse(id, -32602, "Invalid params: 'name' is required");
        }

        ToolHandler handler = tools.get(toolName);
        if (handler == null) {
            return errorResponse(id, -32601, "Tool not found: " + toolName);
        }

        // Security gate: authenticate + authorise every call
        SecurityManager.ValidationResult secResult = securityManager.validate(toolName, arguments);
        if (!secResult.isAllowed()) {
            return toolErrorResponse(id, "SECURITY_VIOLATION: " + secResult.getReason());
        }

        ToolResult result = handler.execute(arguments);
        return toolResultResponse(id, result);
    }

    // ─── Response builders ────────────────────────────────────────────────────

    private String successResponse(Object id, JsonNode result) {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id instanceof Integer) resp.put("id", (Integer) id);
        else if (id != null) resp.put("id", id.toString());
        else resp.putNull("id");
        resp.set("result", result);
        return resp.toString();
    }

    private String errorResponse(Object id, int code, String message) {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id instanceof Integer) resp.put("id", (Integer) id);
        else if (id != null) resp.put("id", id.toString());
        else resp.putNull("id");
        ObjectNode err = resp.putObject("error");
        err.put("code", code);
        err.put("message", message);
        return resp.toString();
    }

    private String toolResultResponse(Object id, ToolResult result) {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id instanceof Integer) resp.put("id", (Integer) id);
        else if (id != null) resp.put("id", id.toString());
        else resp.putNull("id");

        ObjectNode res = resp.putObject("result");
        ArrayNode content = res.putArray("content");
        ObjectNode textBlock = content.addObject();
        textBlock.put("type", "text");
        textBlock.put("text", result.getText());
        if (result.isError()) res.put("isError", true);
        return resp.toString();
    }

    private String toolErrorResponse(Object id, String message) {
        return toolResultResponse(id, ToolResult.error(message));
    }

    public static void main(String[] args) {
        try {
            new McpNetworkPolicyServer().run();
        } catch (IOException e) {
            System.err.println("[FATAL] Server crashed: " + e.getMessage());
            System.exit(1);
        }
    }
}
