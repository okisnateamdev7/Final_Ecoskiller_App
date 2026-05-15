package com.ecoskiller.mcp.isd;

import com.ecoskiller.mcp.isd.handlers.ToolRegistry;
import com.ecoskiller.mcp.isd.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.*;

/**
 * Ecoskiller · idea-similarity-detector
 * MCP Server — CAT-XII | Innovation Engine · Anti-Theft Layer
 *
 * Transport  : stdio (stdin/stdout)
 * Protocol   : JSON-RPC 2.0 / MCP 2024-11-05
 * Namespace  : analytics (k3s)
 *
 * Agents (13 tools):
 *  1.  run_similarity_check        — ANN search + copy-probability score + CLEAR/REVIEW/FLAG classification
 *  2.  run_similarity_recheck      — Idempotent re-execution for amended ideas or corpus growth
 *  3.  get_similarity_report       — Retrieve stored report from innovation.idea_similarity_reports
 *  4.  get_similarity_audit        — Full audit trail from innovation.idea_similarity_audit
 *  5.  get_classification          — Return tier: CLEAR | REVIEW | FLAG for a given idea
 *  6.  compute_copy_probability    — Weighted scoring formula from ANN results
 *  7.  get_top_matches             — Top-K similar ideas with cosine scores and metadata
 *  8.  get_threshold_config        — Per-tenant threshold config (Redis cache key)
 *  9.  set_threshold_config        — Update per-tenant CLEAR/REVIEW/FLAG boundaries (admin)
 * 10.  get_dlq_status              — DLQ message count + retry state for a tenant/idea
 * 11.  get_consumer_health         — Kafka consumer lag, Qdrant status, DLQ alert state
 * 12.  get_similarity_metrics      — Prometheus metrics snapshot (latency, classification counts)
 * 13.  list_similarity_reports     — Paginated list of all reports for a tenant
 */
public class IdeaSimilarityDetectorMcpServer {

    private static final Logger LOG = Logger.getLogger(IdeaSimilarityDetectorMcpServer.class.getName());

    public static final String SERVER_NAME    = "ecoskiller-mcp-idea-similarity-detector";
    public static final String SERVER_VERSION = "1.0.0";
    public static final String MCP_VERSION    = "2024-11-05";

    private final ObjectMapper mapper   = new ObjectMapper();
    private final ToolRegistry registry;

    public IdeaSimilarityDetectorMcpServer() {
        SecurityValidator sv = new SecurityValidator();
        this.registry = new ToolRegistry(sv);
    }

    public static void main(String[] args) throws Exception {
        configureLogging();
        LOG.info(SERVER_NAME + " v" + SERVER_VERSION + " starting");
        new IdeaSimilarityDetectorMcpServer().run();
    }

    private static void configureLogging() {
        // ALL logs to stderr — stdout reserved for MCP JSON-RPC
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

        // Security validation before every dispatch
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
