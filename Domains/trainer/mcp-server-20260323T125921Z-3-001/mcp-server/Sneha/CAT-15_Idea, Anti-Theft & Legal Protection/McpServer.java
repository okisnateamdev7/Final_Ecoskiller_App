package com.ecoskiller.mcp15;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * EcoSkiller MCP Server — CAT-15: Idea, Anti-Theft & Legal Protection
 *
 * Agents:
 *   1. IDEA_ATTRIBUTION_CHAIN_AGENT
 *   2. HASH_PROOF
 *   3. FEATURE_STORE_AGENT
 *   4. FAST_SIMILARITY
 *   5. deep_similarity
 *   6. COPY_PROBABILITY
 *   7. BEHAVIOR_STREAM_PROCESSOR
 *   8. ACCESS_LOG_TRACKER
 *
 * Transport: stdio (stdin/stdout)
 * Protocol:  JSON-RPC 2.0 / MCP 2024-11-05
 */
public class McpServer {

    private static final Logger LOGGER = Logger.getLogger(McpServer.class.getName());
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // ── agent registry ────────────────────────────────────────────────────────
    private static final Map<String, AgentHandler> AGENTS = new LinkedHashMap<>();

    static {
        AGENTS.put("idea_attribution_chain",  new IdeaAttributionChainAgent());
        AGENTS.put("hash_proof",              new HashProofAgent());
        AGENTS.put("feature_store",           new FeatureStoreAgent());
        AGENTS.put("fast_similarity",         new FastSimilarityAgent());
        AGENTS.put("deep_similarity",         new DeepSimilarityAgent());
        AGENTS.put("copy_probability",        new CopyProbabilityAgent());
        AGENTS.put("behavior_stream_processor", new BehaviorStreamProcessorAgent());
        AGENTS.put("access_log_tracker",      new AccessLogTrackerAgent());
    }

    // ── main loop ─────────────────────────────────────────────────────────────
    public static void main(String[] args) throws Exception {
        configureLogging();
        LOGGER.info("EcoSkiller MCP-15 server starting (stdio transport)");

        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                JsonNode req  = MAPPER.readTree(line);
                JsonNode resp = handleRequest(req);
                if (resp != null) {
                    out.println(MAPPER.writeValueAsString(resp));
                    out.flush();
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error processing request: " + line, e);
                ObjectNode err = errorResponse(null, -32700, "Parse error: " + e.getMessage());
                out.println(MAPPER.writeValueAsString(err));
                out.flush();
            }
        }
    }

    // ── request dispatcher ────────────────────────────────────────────────────
    private static JsonNode handleRequest(JsonNode req) {
        String  method = req.path("method").asText();
        JsonNode id    = req.has("id") ? req.get("id") : null;

        switch (method) {
            case "initialize":      return handleInitialize(req, id);
            case "tools/list":      return handleToolsList(id);
            case "tools/call":      return handleToolsCall(req, id);
            case "ping":            return successResponse(id, MAPPER.createObjectNode());
            default:
                if (id == null) return null;   // notification — no response
                return errorResponse(id, -32601, "Method not found: " + method);
        }
    }

    // ── initialize ────────────────────────────────────────────────────────────
    private static JsonNode handleInitialize(JsonNode req, JsonNode id) {
        ObjectNode result = MAPPER.createObjectNode();
        result.put("protocolVersion", "2024-11-05");

        ObjectNode serverInfo = result.putObject("serverInfo");
        serverInfo.put("name",    "mcp-15-idea-protection");
        serverInfo.put("version", "1.0.0");

        ObjectNode caps = result.putObject("capabilities");
        caps.putObject("tools");

        return successResponse(id, result);
    }

    // ── tools/list ────────────────────────────────────────────────────────────
    private static JsonNode handleToolsList(JsonNode id) {
        ArrayNode tools = MAPPER.createArrayNode();
        for (Map.Entry<String, AgentHandler> e : AGENTS.entrySet()) {
            tools.add(e.getValue().toolDefinition(e.getKey()));
        }
        ObjectNode result = MAPPER.createObjectNode();
        result.set("tools", tools);
        return successResponse(id, result);
    }

    // ── tools/call ────────────────────────────────────────────────────────────
    private static JsonNode handleToolsCall(JsonNode req, JsonNode id) {
        JsonNode  params   = req.path("params");
        String    toolName = params.path("name").asText();
        JsonNode  args     = params.path("arguments");

        AgentHandler handler = AGENTS.get(toolName);
        if (handler == null) {
            return errorResponse(id, -32602, "Unknown tool: " + toolName);
        }

        try {
            ObjectNode result = handler.execute(args);
            ObjectNode wrapper = MAPPER.createObjectNode();
            ArrayNode  content = wrapper.putArray("content");
            ObjectNode textBlock = content.addObject();
            textBlock.put("type", "text");
            textBlock.put("text", MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(result));
            return successResponse(id, wrapper);
        } catch (Exception e) {
            return errorResponse(id, -32603, "Agent error: " + e.getMessage());
        }
    }

    // ── JSON-RPC helpers ──────────────────────────────────────────────────────
    static ObjectNode successResponse(JsonNode id, JsonNode result) {
        ObjectNode resp = MAPPER.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.set("id", id);
        resp.set("result", result);
        return resp;
    }

    static ObjectNode errorResponse(JsonNode id, int code, String message) {
        ObjectNode resp  = MAPPER.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.set("id", id);
        ObjectNode error = resp.putObject("error");
        error.put("code",    code);
        error.put("message", message);
        return resp;
    }

    private static void configureLogging() {
        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        StreamHandler sh = new StreamHandler(System.err, new SimpleFormatter());
        sh.setLevel(Level.ALL);
        root.addHandler(sh);
        root.setLevel(Level.INFO);
    }
}
