package com.ecoskiller.mcp.helm;

import com.ecoskiller.mcp.helm.handlers.ToolRegistry;
import com.ecoskiller.mcp.helm.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.*;

/**
 * Ecoskiller · Helm Service
 * MCP Server — Helm v3.x Kubernetes Package Manager + GD Session Orchestrator
 *
 * Transport  : stdio (stdin/stdout)
 * Protocol   : JSON-RPC 2.0 / MCP 2024-11-05
 *
 * Agents (15 tools):
 *
 * ── Helm K8s Package Manager (CI/CD pipeline: GitLab → Harbor → Helm → k3s) ──
 *  1.  helm_install           — Deploy a new release from a chart
 *  2.  helm_upgrade           — Upgrade or install an existing release
 *  3.  helm_rollback          — Roll back a release to a previous revision
 *  4.  helm_uninstall         — Remove a release and its resources
 *  5.  helm_list_releases     — List all releases across namespaces/environments
 *  6.  helm_get_status        — Detailed status of a release
 *  7.  helm_get_values        — Retrieve effective values for a deployed release
 *  8.  helm_get_history       — Full revision history of a release
 *  9.  helm_lint_chart        — Validate a chart before deployment
 * 10.  helm_diff_upgrade      — Preview changes before upgrading (helm-diff plugin)
 *
 * ── Helm Session Orchestrator (GD Assessment Sessions) ──
 * 11.  session_create         — Create a new GD session room
 * 12.  session_join           — Register participant join, return ordered position
 * 13.  session_get_status     — Get session state, speaking queue, current speaker
 * 14.  session_advance_turn   — Mark turn complete, trigger next speaker
 * 15.  session_get_metrics    — Retrieve aggregated session metrics after completion
 */
public class HelmMcpServer {

    private static final Logger LOG = Logger.getLogger(HelmMcpServer.class.getName());

    public static final String SERVER_NAME    = "ecoskiller-mcp-helm-service";
    public static final String SERVER_VERSION = "1.0.0";
    public static final String MCP_VERSION    = "2024-11-05";

    private final ObjectMapper mapper   = new ObjectMapper();
    private final ToolRegistry registry;

    public HelmMcpServer() {
        SecurityValidator sv = new SecurityValidator();
        this.registry = new ToolRegistry(sv);
    }

    public static void main(String[] args) throws Exception {
        configureLogging();
        LOG.info(SERVER_NAME + " v" + SERVER_VERSION + " starting");
        new HelmMcpServer().run();
    }

    private static void configureLogging() {
        Logger root = Logger.getLogger("");
        root.setLevel(Level.INFO);
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        StreamHandler sh = new StreamHandler(System.err, new SimpleFormatter());
        sh.setLevel(Level.ALL);
        root.addHandler(sh);
    }

    private void run() throws Exception {
        BufferedReader stdin  = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter   stdout = new PrintWriter(
                new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
        String line;
        while ((line = stdin.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                JsonNode req = mapper.readTree(line);
                String response = dispatch(req);
                if (response != null) stdout.println(response);
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Error processing: " + line, e);
                stdout.println("{\"jsonrpc\":\"2.0\",\"id\":null,"
                        + "\"error\":{\"code\":-32700,\"message\":\"Parse error\"}}");
            }
        }
        LOG.info(SERVER_NAME + " shutting down");
    }

    private String dispatch(JsonNode req) throws Exception {
        String method   = req.path("method").asText("");
        JsonNode id     = req.get("id");
        JsonNode params = req.path("params");
        return switch (method) {
            case "initialize"  -> handleInitialize(id);
            case "tools/list"  -> handleToolsList(id);
            case "tools/call"  -> handleToolCall(id, params);
            case "ping"        -> buildResult(id, mapper.createObjectNode());
            default            -> buildError(id, -32601, "Method not found: " + method);
        };
    }

    private String handleInitialize(JsonNode id) throws Exception {
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);
        ObjectNode info = result.putObject("serverInfo");
        info.put("name",    SERVER_NAME);
        info.put("version", SERVER_VERSION);
        result.putObject("capabilities").putObject("tools");
        return buildResult(id, result);
    }

    private String handleToolsList(JsonNode id) throws Exception {
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", registry.getToolDefinitions());
        return buildResult(id, result);
    }

    private String handleToolCall(JsonNode id, JsonNode params) throws Exception {
        String   toolName  = params.path("name").asText("");
        JsonNode arguments = params.path("arguments");
        new SecurityValidator().validateToolCall(toolName, arguments);
        Object toolResult = registry.call(toolName, arguments);
        ObjectNode result  = mapper.createObjectNode();
        ArrayNode  content = result.putArray("content");
        ObjectNode block   = content.addObject();
        block.put("type", "text");
        block.put("text", mapper.writeValueAsString(toolResult));
        return buildResult(id, result);
    }

    private String buildResult(JsonNode id, Object result) throws Exception {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.set("id", id);
        resp.set("result", mapper.valueToTree(result));
        return mapper.writeValueAsString(resp);
    }

    private String buildError(JsonNode id, int code, String message) throws Exception {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.set("id", id);
        ObjectNode err = resp.putObject("error");
        err.put("code",    code);
        err.put("message", message);
        return mapper.writeValueAsString(resp);
    }
}
