package com.ecoskiller.mcp.its;

import com.ecoskiller.mcp.its.handlers.ToolRegistry;
import com.ecoskiller.mcp.its.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.*;

/**
 * Ecoskiller · idea-timestamp-service
 * MCP Server — CAT-XII | Innovation Engine · Idea Registry Layer
 *
 * Transport  : stdio (stdin/stdout)
 * Protocol   : JSON-RPC 2.0 / MCP 2024-11-05
 * Namespace  : core (k3s)
 *
 * Agents (12 tools):
 *  1.  submit_idea             — SHA-256 hash + authoritative timestamp + MinIO archive + Kafka emit
 *  2.  revise_idea             — Versioned re-submission with new hash & timestamp
 *  3.  get_timestamp_proof     — Retrieve immutable timestamp + content_hash + minio_etag
 *  4.  list_idea_versions      — All versions with timestamps, hashes, status
 *  5.  verify_idea_integrity   — Recompute SHA-256, compare against stored → tamper detection
 *  6.  get_submission_status   — Idea status: PENDING_FINGERPRINT → VERIFIED_ORIGINAL | FLAGGED | LICENSED
 *  7.  set_visibility          — Update visibility: PRIVATE | INTERNAL | MARKETPLACE
 *  8.  archive_idea            — Soft-archive (immutable record kept for IP / compliance)
 *  9.  check_rate_limit        — Inspect Redis rate-limit counter for a candidate
 * 10.  get_submission_audit    — Append-only audit log for a submission
 * 11.  verify_minio_object     — Re-fetch MinIO ETag and cross-check against PostgreSQL
 * 12.  get_idea_ownership      — Ownership record: candidate_id, submitted_at, visibility, status
 */
public class IdeaTimestampMcpServer {

    private static final Logger LOG = Logger.getLogger(IdeaTimestampMcpServer.class.getName());

    public static final String SERVER_NAME    = "ecoskiller-mcp-idea-timestamp-service";
    public static final String SERVER_VERSION = "1.0.0";
    public static final String MCP_VERSION    = "2024-11-05";

    private final ObjectMapper  mapper   = new ObjectMapper();
    private final ToolRegistry  registry;

    public IdeaTimestampMcpServer() {
        SecurityValidator sv = new SecurityValidator();
        this.registry = new ToolRegistry(sv);
    }

    // ── entry point ──────────────────────────────────────────────────────────

    public static void main(String[] args) throws Exception {
        configureLogging();
        LOG.info(SERVER_NAME + " v" + SERVER_VERSION + " starting");
        new IdeaTimestampMcpServer().run();
    }

    private static void configureLogging() {
        // ALL logs → stderr so stdout stays clean for MCP JSON-RPC
        Logger root = Logger.getLogger("");
        root.setLevel(Level.INFO);
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        StreamHandler sh = new StreamHandler(System.err, new SimpleFormatter());
        sh.setLevel(Level.ALL);
        root.addHandler(sh);
    }

    // ── main loop ─────────────────────────────────────────────────────────────

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
        LOG.info(SERVER_NAME + " shutting down — stdin closed");
    }

    // ── dispatcher ───────────────────────────────────────────────────────────

    private String dispatch(JsonNode req) throws Exception {
        String method = req.path("method").asText("");
        JsonNode id   = req.get("id");
        JsonNode params = req.path("params");

        return switch (method) {
            case "initialize"  -> handleInitialize(id);
            case "tools/list"  -> handleToolsList(id);
            case "tools/call"  -> handleToolCall(id, params);
            case "ping"        -> buildResult(id, mapper.createObjectNode());
            default            -> buildError(id, -32601, "Method not found: " + method);
        };
    }

    // ── initialize ───────────────────────────────────────────────────────────

    private String handleInitialize(JsonNode id) throws Exception {
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);
        ObjectNode info = result.putObject("serverInfo");
        info.put("name",    SERVER_NAME);
        info.put("version", SERVER_VERSION);
        result.putObject("capabilities").putObject("tools");
        return buildResult(id, result);
    }

    // ── tools/list ───────────────────────────────────────────────────────────

    private String handleToolsList(JsonNode id) throws Exception {
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", registry.getToolDefinitions());
        return buildResult(id, result);
    }

    // ── tools/call ───────────────────────────────────────────────────────────

    private String handleToolCall(JsonNode id, JsonNode params) throws Exception {
        String   toolName = params.path("name").asText("");
        JsonNode arguments = params.path("arguments");

        // Security validation before every dispatch
        SecurityValidator sv = new SecurityValidator();
        sv.validateToolCall(toolName, arguments);

        Object toolResult = registry.call(toolName, arguments);

        ObjectNode result  = mapper.createObjectNode();
        ArrayNode  content = result.putArray("content");
        ObjectNode block   = content.addObject();
        block.put("type", "text");
        block.put("text", mapper.writeValueAsString(toolResult));
        return buildResult(id, result);
    }

    // ── JSON-RPC helpers ─────────────────────────────────────────────────────

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
