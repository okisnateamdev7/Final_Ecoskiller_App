package com.ecoskiller.mcp.ife;

import com.ecoskiller.mcp.ife.handlers.*;
import com.ecoskiller.mcp.ife.security.SecurityValidator;
import com.ecoskiller.mcp.ife.protocol.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.*;

/**
 * Ecoskiller · Idea Fingerprint Engine (IFE)
 * MCP Server — CAT-XII | Innovation Engine IP Protection
 *
 * Transport : stdio (stdin/stdout)
 * Protocol  : JSON-RPC 2.0 / MCP 2024-11-05
 *
 * Agents (14 tools):
 *  1. compute_fingerprint          — SHA-3-256 deterministic fingerprinting
 *  2. compute_fingerprint_batch    — Batch fingerprinting (async, job-based)
 *  3. generate_embedding           — 384-dim Sentence-BERT embeddings
 *  4. similarity_search            — Qdrant ANN sub-100ms vector search
 *  5. verify_fingerprint           — Tamper detection / integrity check
 *  6. get_fingerprint              — Retrieve stored fingerprint by idea_id
 *  7. get_similarity_report        — Plagiarism risk + duplicate report
 *  8. detect_plagiarism            — Multi-level plagiarism scoring
 *  9. index_embedding              — Qdrant upsert + tenant-scoped indexing
 * 10. delete_embedding             — Remove embedding on idea archive/delete
 * 11. reindex_tenant               — Full tenant re-index (weekly maintenance)
 * 12. get_batch_job_status         — Poll async batch job progress
 * 13. list_embedding_models        — Available Sentence-BERT model versions
 * 14. compute_risk_score           — Weighted plagiarism risk scorer
 */
public class IdeaFingerprintMcpServer {

    private static final Logger LOG = Logger.getLogger(IdeaFingerprintMcpServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "ecoskiller-mcp-ife";
    private static final String SERVER_VERSION = "1.0.0";

    private final ObjectMapper mapper = new ObjectMapper();
    private final ToolRegistry toolRegistry;
    private final SecurityValidator securityValidator;

    public IdeaFingerprintMcpServer() {
        this.securityValidator = new SecurityValidator();
        this.toolRegistry = new ToolRegistry(securityValidator);
    }

    public static void main(String[] args) throws Exception {
        configureLogging();
        LOG.info("IFE MCP Server starting — " + SERVER_NAME + " v" + SERVER_VERSION);
        new IdeaFingerprintMcpServer().run();
    }

    private static void configureLogging() {
        // Route all logs to stderr so stdout stays clean for MCP JSON-RPC
        Logger root = Logger.getLogger("");
        root.setLevel(Level.INFO);
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        StreamHandler stderr = new StreamHandler(System.err, new SimpleFormatter());
        stderr.setLevel(Level.ALL);
        root.addHandler(stderr);
    }

    private void run() throws Exception {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter stdout = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

        String line;
        while ((line = stdin.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            try {
                JsonNode request = mapper.readTree(line);
                String response = dispatch(request);
                if (response != null) {
                    stdout.println(response);
                }
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Error processing request: " + line, e);
                stdout.println(buildParseError(line));
            }
        }
        LOG.info("IFE MCP Server shutting down — stdin closed");
    }

    private String dispatch(JsonNode req) throws Exception {
        String method = req.path("method").asText("");
        JsonNode id = req.get("id");
        JsonNode params = req.path("params");

        return switch (method) {
            case "initialize" -> handleInitialize(id, params);
            case "tools/list"  -> handleToolsList(id);
            case "tools/call"  -> handleToolCall(id, params);
            case "ping"        -> buildResult(id, mapper.createObjectNode());
            default            -> buildMethodNotFound(id, method);
        };
    }

    // ─── initialize ───────────────────────────────────────────────────────────

    private String handleInitialize(JsonNode id, JsonNode params) throws Exception {
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);

        ObjectNode info = result.putObject("serverInfo");
        info.put("name", SERVER_NAME);
        info.put("version", SERVER_VERSION);

        ObjectNode capabilities = result.putObject("capabilities");
        capabilities.putObject("tools");

        return buildResult(id, result);
    }

    // ─── tools/list ───────────────────────────────────────────────────────────

    private String handleToolsList(JsonNode id) throws Exception {
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", toolRegistry.getToolDefinitions());
        return buildResult(id, result);
    }

    // ─── tools/call ───────────────────────────────────────────────────────────

    private String handleToolCall(JsonNode id, JsonNode params) throws Exception {
        String toolName = params.path("name").asText("");
        JsonNode arguments = params.path("arguments");

        // Security: validate all inputs before dispatching
        securityValidator.validateToolCall(toolName, arguments);

        Object toolResult = toolRegistry.call(toolName, arguments);

        ObjectNode result = mapper.createObjectNode();
        ArrayNode content = result.putArray("content");
        ObjectNode textBlock = content.addObject();
        textBlock.put("type", "text");
        textBlock.put("text", mapper.writeValueAsString(toolResult));
        return buildResult(id, result);
    }

    // ─── JSON-RPC helpers ─────────────────────────────────────────────────────

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
        ObjectNode error = resp.putObject("error");
        error.put("code", code);
        error.put("message", message);
        return mapper.writeValueAsString(resp);
    }

    private String buildMethodNotFound(JsonNode id, String method) throws Exception {
        return buildError(id, -32601, "Method not found: " + method);
    }

    private String buildParseError(String raw) {
        return "{\"jsonrpc\":\"2.0\",\"id\":null,\"error\":{\"code\":-32700,\"message\":\"Parse error\"}}";
    }
}
