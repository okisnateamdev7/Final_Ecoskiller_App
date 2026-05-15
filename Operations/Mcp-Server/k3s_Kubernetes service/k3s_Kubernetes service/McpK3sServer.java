package com.ecoskiller.mcp.k3s.server;

import com.ecoskiller.mcp.k3s.security.SecurityValidator;
import com.ecoskiller.mcp.k3s.security.AuditLogger;
import com.ecoskiller.mcp.k3s.agents.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * Ecoskiller | CAT-K3S — k3s Kubernetes Orchestration MCP Server
 * Secure MCP Server in Java | 20 Agents | Priority: CRITICAL
 *
 * Transport: stdio (stdin/stdout)
 * Format:    JSON-RPC 2.0
 * MCP Version: 2024-11-05
 * Security:  Input validation, RBAC checks, audit logging, rate limiting
 */
public class McpK3sServer {

    private static final Logger LOGGER = Logger.getLogger(McpK3sServer.class.getName());
    private static final String MCP_VERSION  = "2024-11-05";
    private static final String SERVER_NAME  = "mcp-k3s-kubernetes";
    private static final String SERVER_VER   = "1.0.0";

    private final ObjectMapper mapper        = new ObjectMapper();
    private final SecurityValidator security = new SecurityValidator();
    private final AuditLogger audit          = new AuditLogger();
    private final Map<String, K3sAgent> agents = new LinkedHashMap<>();

    public McpK3sServer() {
        registerAgents();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Agent Registration
    // ─────────────────────────────────────────────────────────────────────────
    private void registerAgents() {
        agents.put("k3s_cluster_status",           new ClusterStatusAgent());
        agents.put("k3s_node_management",          new NodeManagementAgent());
        agents.put("k3s_pod_lifecycle",            new PodLifecycleAgent());
        agents.put("k3s_deployment_manager",       new DeploymentManagerAgent());
        agents.put("k3s_service_discovery",        new ServiceDiscoveryAgent());
        agents.put("k3s_ingress_controller",       new IngressControllerAgent());
        agents.put("k3s_horizontal_autoscaler",    new HorizontalAutoscalerAgent());
        agents.put("k3s_persistent_volume",        new PersistentVolumeAgent());
        agents.put("k3s_config_secrets",           new ConfigSecretsAgent());
        agents.put("k3s_rbac_policy",              new RbacPolicyAgent());
        agents.put("k3s_network_policy",           new NetworkPolicyAgent());
        agents.put("k3s_rolling_update",           new RollingUpdateAgent());
        agents.put("k3s_etcd_backup",              new EtcdBackupAgent());
        agents.put("k3s_observability",            new ObservabilityAgent());
        agents.put("k3s_gitops_argocd",            new GitopsArgoCDAgent());
        agents.put("k3s_multicloud_failover",      new MulticloudFailoverAgent());
        agents.put("k3s_security_scanner",         new SecurityScannerAgent());
        agents.put("k3s_disaster_recovery",        new DisasterRecoveryAgent());
        agents.put("k3s_compliance_audit",         new ComplianceAuditAgent());
        agents.put("k3s_cluster_upgrade",          new ClusterUpgradeAgent());

        LOGGER.info("[BOOT] Registered " + agents.size() + " k3s agents");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Main stdio loop
    // ─────────────────────────────────────────────────────────────────────────
    public void run() throws IOException {
        LOGGER.info("[BOOT] " + SERVER_NAME + " v" + SERVER_VER + " starting (stdio transport)");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            try {
                // Security: sanitize raw input length before parse
                security.validateRawInput(line);

                JsonNode request  = mapper.readTree(line);
                JsonNode response = handleRequest(request);

                if (response != null) {
                    writer.println(mapper.writeValueAsString(response));
                    writer.flush();
                }
            } catch (SecurityException se) {
                audit.logSecurityViolation("RAW_INPUT", se.getMessage());
                JsonNode err = buildError(null, -32600, "Security violation: " + se.getMessage());
                writer.println(mapper.writeValueAsString(err));
                writer.flush();
            } catch (Exception e) {
                LOGGER.warning("[ERROR] Parse failure: " + e.getMessage());
                JsonNode err = buildError(null, -32700, "Parse error");
                writer.println(mapper.writeValueAsString(err));
                writer.flush();
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // JSON-RPC Dispatcher
    // ─────────────────────────────────────────────────────────────────────────
    private JsonNode handleRequest(JsonNode req) {
        JsonNode idNode = req.get("id");
        String   method = req.has("method") ? req.get("method").asText() : "";
        JsonNode params = req.has("params") ? req.get("params") : mapper.createObjectNode();

        // Security: validate method name
        if (!security.validateMethodName(method)) {
            audit.logSecurityViolation("METHOD_INJECT", method);
            return buildError(idNode, -32600, "Invalid method name");
        }

        try {
            return switch (method) {
                case "initialize"  -> handleInitialize(idNode, params);
                case "ping"        -> buildResult(idNode, mapper.createObjectNode());
                case "tools/list"  -> handleToolsList(idNode);
                case "tools/call"  -> handleToolCall(idNode, params);
                default            -> buildError(idNode, -32601, "Method not found: " + method);
            };
        } catch (Exception e) {
            LOGGER.warning("[ERROR] Handler exception: " + e.getMessage());
            return buildError(idNode, -32603, "Internal error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // initialize
    // ─────────────────────────────────────────────────────────────────────────
    private JsonNode handleInitialize(JsonNode id, JsonNode params) {
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);

        ObjectNode serverInfo = result.putObject("serverInfo");
        serverInfo.put("name",    SERVER_NAME);
        serverInfo.put("version", SERVER_VER);

        ObjectNode caps = result.putObject("capabilities");
        caps.putObject("tools");

        audit.logEvent("INITIALIZE", "Server handshake completed");
        return buildResult(id, result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // tools/list
    // ─────────────────────────────────────────────────────────────────────────
    private JsonNode handleToolsList(JsonNode id) {
        ArrayNode tools = mapper.createArrayNode();

        for (Map.Entry<String, K3sAgent> entry : agents.entrySet()) {
            K3sAgent agent = entry.getValue();
            ObjectNode tool = mapper.createObjectNode();
            tool.put("name",        entry.getKey());
            tool.put("description", agent.getDescription());

            ObjectNode schema = tool.putObject("inputSchema");
            schema.put("type", "object");
            ObjectNode props = schema.putObject("properties");
            ArrayNode  req   = schema.putArray("required");

            for (Map.Entry<String, String> p : agent.getParameters().entrySet()) {
                ObjectNode param = props.putObject(p.getKey());
                param.put("type",        "string");
                param.put("description", p.getValue());
            }
            for (String r : agent.getRequiredParameters()) {
                req.add(r);
            }
            tools.add(tool);
        }

        ObjectNode result = mapper.createObjectNode();
        result.set("tools", tools);
        return buildResult(id, result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // tools/call
    // ─────────────────────────────────────────────────────────────────────────
    private JsonNode handleToolCall(JsonNode id, JsonNode params) {
        // Security: validate params structure
        if (!security.validateToolCallParams(params)) {
            audit.logSecurityViolation("TOOL_CALL_PARAMS", params.toString());
            return buildError(id, -32600, "Invalid tool call parameters");
        }

        String toolName = params.has("name") ? params.get("name").asText() : "";
        JsonNode args   = params.has("arguments") ? params.get("arguments")
                                                  : mapper.createObjectNode();

        // Security: validate tool name
        if (!security.validateToolName(toolName)) {
            audit.logSecurityViolation("TOOL_NAME_INJECT", toolName);
            return buildError(id, -32600, "Invalid tool name");
        }

        K3sAgent agent = agents.get(toolName);
        if (agent == null) {
            return buildError(id, -32601, "Tool not found: " + toolName);
        }

        // Security: sanitize all argument values
        Map<String, String> sanitizedArgs = security.sanitizeArguments(args);
        if (sanitizedArgs == null) {
            audit.logSecurityViolation("ARGUMENT_INJECTION", toolName + " args=" + args);
            return buildError(id, -32600, "Argument validation failed");
        }

        // Rate limiting check
        if (!security.checkRateLimit(toolName)) {
            audit.logSecurityViolation("RATE_LIMIT", toolName);
            return buildError(id, -32029, "Rate limit exceeded for: " + toolName);
        }

        // Execute agent
        audit.logEvent("TOOL_CALL", toolName + " args=" + sanitizedArgs.keySet());
        String agentResult = agent.execute(sanitizedArgs);

        ObjectNode content = mapper.createObjectNode();
        content.put("type", "text");
        content.put("text", agentResult);

        ArrayNode  contents = mapper.createArrayNode();
        contents.add(content);

        ObjectNode result = mapper.createObjectNode();
        result.set("content", contents);
        result.put("isError", false);

        return buildResult(id, result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // JSON-RPC helpers
    // ─────────────────────────────────────────────────────────────────────────
    private JsonNode buildResult(JsonNode id, JsonNode result) {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.set("id", id);
        resp.set("result", result);
        return resp;
    }

    private JsonNode buildError(JsonNode id, int code, String message) {
        ObjectNode resp  = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.set("id", id);
        ObjectNode error = resp.putObject("error");
        error.put("code",    code);
        error.put("message", message);
        return resp;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Entry point
    // ─────────────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        // Redirect JUL to stderr so stdout stays clean for JSON-RPC
        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        StreamHandler sh = new StreamHandler(System.err, new SimpleFormatter());
        sh.setLevel(Level.INFO);
        root.addHandler(sh);
        root.setLevel(Level.INFO);

        try {
            new McpK3sServer().run();
        } catch (IOException e) {
            LOGGER.severe("[FATAL] " + e.getMessage());
            System.exit(1);
        }
    }
}
