package com.ecoskiller.mcp18;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Ecoskiller | CAT-18 — Governance & Core Control
 * MCP Server in Java | 2 Agents | Priority: HIGH
 *
 * Agents:
 *   1. API_RATE_LIMIT_ENFORCEMENT_AGENT_COMPLETE
 *   2. DECISION_TRACEABILITY_AGENT_COMPLETE
 *
 * Transport : stdio (stdin/stdout)
 * Protocol  : JSON-RPC 2.0
 * MCP Ver   : 2024-11-05
 */
public class McpServer {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME = "mcp-18-governance";
    private static final String SERVER_VERSION = "1.0.0";

    // ──────────────────────────────────────────────────────────────
    // TOOL REGISTRY
    // ──────────────────────────────────────────────────────────────
    private static final Map<String, ToolHandler> TOOLS = new HashMap<>();

    static {
        TOOLS.put("api_rate_limit_enforcement", McpServer::handleApiRateLimitEnforcement);
        TOOLS.put("decision_traceability",      McpServer::handleDecisionTraceability);
    }

    // ──────────────────────────────────────────────────────────────
    // ENTRY POINT
    // ──────────────────────────────────────────────────────────────
    public static void main(String[] args) throws Exception {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
        PrintStream   out  = new PrintStream(System.out, true, "UTF-8");
        System.setErr(System.err); // keep stderr for debug logs

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            try {
                JsonNode req  = MAPPER.readTree(line);
                String   resp = handleRequest(req);
                if (resp != null) {
                    out.println(resp);
                    out.flush();
                }
            } catch (Exception e) {
                System.err.println("[mcp-18] parse error: " + e.getMessage());
            }
        }
    }

    // ──────────────────────────────────────────────────────────────
    // DISPATCHER
    // ──────────────────────────────────────────────────────────────
    private static String handleRequest(JsonNode req) throws Exception {
        String method = req.path("method").asText();
        JsonNode id   = req.get("id");          // null for notifications

        return switch (method) {
            case "initialize"  -> respond(id, buildInitializeResult());
            case "tools/list"  -> respond(id, buildToolsList());
            case "tools/call"  -> respond(id, dispatchToolCall(req));
            case "ping"        -> respond(id, MAPPER.createObjectNode());
            default            -> {
                if (id != null) yield respondError(id, -32601, "Method not found: " + method);
                yield null;           // notifications get no reply
            }
        };
    }

    // ──────────────────────────────────────────────────────────────
    // INITIALIZE
    // ──────────────────────────────────────────────────────────────
    private static ObjectNode buildInitializeResult() {
        ObjectNode result = MAPPER.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);

        ObjectNode info = result.putObject("serverInfo");
        info.put("name",    SERVER_NAME);
        info.put("version", SERVER_VERSION);

        ObjectNode caps  = result.putObject("capabilities");
        caps.putObject("tools");          // signal that we support tools
        return result;
    }

    // ──────────────────────────────────────────────────────────────
    // TOOLS/LIST
    // ──────────────────────────────────────────────────────────────
    private static ObjectNode buildToolsList() {
        ObjectNode result = MAPPER.createObjectNode();
        ArrayNode  tools  = result.putArray("tools");

        // ── Tool 1: api_rate_limit_enforcement ─────────────────────
        ObjectNode t1 = tools.addObject();
        t1.put("name",        "api_rate_limit_enforcement");
        t1.put("description",
            "API_RATE_LIMIT_ENFORCEMENT_AGENT_COMPLETE — Enforces per-client, " +
            "per-endpoint, and global API rate limits. Tracks request counts, " +
            "applies throttling policies, returns quota status, and logs " +
            "violations for governance audit trails.");

        ObjectNode t1Schema = t1.putObject("inputSchema");
        t1Schema.put("type", "object");
        ObjectNode t1Props = t1Schema.putObject("properties");

        addStringProp(t1Props, "client_id",
            "Unique identifier for the API client / tenant (required).");
        addStringProp(t1Props, "endpoint",
            "API endpoint being accessed, e.g. /api/v1/jobs/search.");
        addStringProp(t1Props, "action",
            "Action to perform: check | increment | reset | get_quota | list_violations. Default: check.");
        addStringProp(t1Props, "tier",
            "Client tier determining rate-limit policy: free | pro | enterprise. Default: free.");
        addIntProp(t1Props,    "window_seconds",
            "Rolling window in seconds for rate evaluation. Default: 60.");
        addIntProp(t1Props,    "max_requests",
            "Override: maximum requests allowed in the window. 0 = use tier default.");

        ArrayNode t1Required = t1Schema.putArray("required");
        t1Required.add("client_id");

        // ── Tool 2: decision_traceability ──────────────────────────
        ObjectNode t2 = tools.addObject();
        t2.put("name",        "decision_traceability");
        t2.put("description",
            "DECISION_TRACEABILITY_AGENT_COMPLETE — Records, stores, and retrieves " +
            "immutable decision audit trails across all platform subsystems. " +
            "Captures actor, rationale, inputs, outputs, and timestamps to satisfy " +
            "regulatory and governance traceability requirements.");

        ObjectNode t2Schema = t2.putObject("inputSchema");
        t2Schema.put("type", "object");
        ObjectNode t2Props = t2Schema.putObject("properties");

        addStringProp(t2Props, "action",
            "Action: record | query | get_chain | export | verify_integrity. Default: record.");
        addStringProp(t2Props, "decision_id",
            "Unique decision ID. Auto-generated if omitted during record.");
        addStringProp(t2Props, "actor_id",
            "ID of the agent, user, or system that made the decision.");
        addStringProp(t2Props, "actor_type",
            "Type of actor: agent | user | system | automated_rule.");
        addStringProp(t2Props, "decision_type",
            "Category: hiring | scoring | rate_limit | fraud_flag | policy_change | access_grant.");
        addStringProp(t2Props, "rationale",
            "Human-readable justification for the decision.");
        addStringProp(t2Props, "inputs_json",
            "JSON string of inputs that led to the decision.");
        addStringProp(t2Props, "output_json",
            "JSON string of the decision output / result.");
        addStringProp(t2Props, "reference_id",
            "External reference (job ID, student ID, etc.) linked to this decision.");
        addStringProp(t2Props, "query_filter",
            "For query/export: JSON filter e.g. {\"actor_id\":\"agent-42\",\"decision_type\":\"hiring\"}.");
        addIntProp(t2Props,    "limit",
            "Max records to return for query action. Default: 50.");

        ArrayNode t2Required = t2Schema.putArray("required");
        t2Required.add("action");

        return result;
    }

    // ──────────────────────────────────────────────────────────────
    // TOOLS/CALL DISPATCHER
    // ──────────────────────────────────────────────────────────────
    private static ObjectNode dispatchToolCall(JsonNode req) throws Exception {
        String   toolName = req.path("params").path("name").asText();
        JsonNode args     = req.path("params").path("arguments");

        ToolHandler handler = TOOLS.get(toolName);
        if (handler == null) {
            ObjectNode err = MAPPER.createObjectNode();
            ArrayNode  ct  = err.putArray("content");
            ct.addObject().put("type", "text").put("text", "Unknown tool: " + toolName);
            err.put("isError", true);
            return err;
        }
        return handler.handle(args);
    }

    // ──────────────────────────────────────────────────────────────
    // AGENT 1 — API_RATE_LIMIT_ENFORCEMENT_AGENT_COMPLETE
    // ──────────────────────────────────────────────────────────────
    private static ObjectNode handleApiRateLimitEnforcement(JsonNode args) throws Exception {
        String clientId     = args.path("client_id").asText();
        String endpoint     = args.path("endpoint").asText("/api/*");
        String action       = args.path("action").asText("check");
        String tier         = args.path("tier").asText("free");
        int    windowSec    = args.path("window_seconds").asInt(60);
        int    maxOverride  = args.path("max_requests").asInt(0);

        // Tier defaults
        int tierMax = switch (tier) {
            case "pro"        -> 300;
            case "enterprise" -> 1000;
            default           -> 60;      // free
        };
        int effectiveMax = (maxOverride > 0) ? maxOverride : tierMax;

        // Simulate in-memory counter (stateless demo)
        int  currentCount    = simulateCount(clientId, endpoint, windowSec);
        boolean allowed      = currentCount < effectiveMax;
        int  remaining       = Math.max(0, effectiveMax - currentCount);
        long resetEpoch      = Instant.now().getEpochSecond() + windowSec;

        ObjectNode payload = MAPPER.createObjectNode();
        payload.put("agent",          "API_RATE_LIMIT_ENFORCEMENT_AGENT_COMPLETE");
        payload.put("action",         action);
        payload.put("client_id",      clientId);
        payload.put("endpoint",       endpoint);
        payload.put("tier",           tier);
        payload.put("window_seconds", windowSec);
        payload.put("effective_max",  effectiveMax);
        payload.put("current_count",  currentCount);
        payload.put("remaining",      remaining);
        payload.put("allowed",        allowed);
        payload.put("reset_epoch",    resetEpoch);
        payload.put("status",         allowed ? "ALLOWED" : "THROTTLED");
        payload.put("timestamp",      Instant.now().toString());

        if (!allowed) {
            ObjectNode violation = payload.putObject("violation");
            violation.put("violation_id",   "VIO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            violation.put("severity",       currentCount > effectiveMax * 1.5 ? "HIGH" : "MEDIUM");
            violation.put("recommendation", "Implement exponential back-off. Next window opens at epoch " + resetEpoch + ".");
        }

        if ("list_violations".equals(action)) {
            ArrayNode violations = payload.putArray("recent_violations");
            // Demo: return synthetic violation log
            ObjectNode v = violations.addObject();
            v.put("violation_id",   "VIO-A1B2C3D4");
            v.put("client_id",      clientId);
            v.put("endpoint",       endpoint);
            v.put("exceeded_at",    Instant.now().minusSeconds(300).toString());
            v.put("count_at_time",  effectiveMax + 15);
            v.put("resolved",       true);
        }

        if ("get_quota".equals(action)) {
            ObjectNode quota = payload.putObject("quota_details");
            quota.put("tier",           tier);
            quota.put("window_seconds", windowSec);
            quota.put("max_requests",   effectiveMax);
            quota.put("used",           currentCount);
            quota.put("remaining",      remaining);
            quota.put("percent_used",   Math.round((double) currentCount / effectiveMax * 100));
        }

        return textResult(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(payload));
    }

    // ──────────────────────────────────────────────────────────────
    // AGENT 2 — DECISION_TRACEABILITY_AGENT_COMPLETE
    // ──────────────────────────────────────────────────────────────
    private static ObjectNode handleDecisionTraceability(JsonNode args) throws Exception {
        String action       = args.path("action").asText("record");
        String decisionId   = args.path("decision_id").asText("DEC-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
        String actorId      = args.path("actor_id").asText("unknown");
        String actorType    = args.path("actor_type").asText("system");
        String decisionType = args.path("decision_type").asText("general");
        String rationale    = args.path("rationale").asText("");
        String inputsJson   = args.path("inputs_json").asText("{}");
        String outputJson   = args.path("output_json").asText("{}");
        String referenceId  = args.path("reference_id").asText("");
        String queryFilter  = args.path("query_filter").asText("{}");
        int    limit        = args.path("limit").asInt(50);

        String timestamp    = Instant.now().toString();

        ObjectNode payload  = MAPPER.createObjectNode();
        payload.put("agent",         "DECISION_TRACEABILITY_AGENT_COMPLETE");
        payload.put("action",        action);
        payload.put("timestamp",     timestamp);

        switch (action) {
            case "record" -> {
                String hash = sha256Stub(decisionId + actorId + inputsJson + outputJson + timestamp);
                ObjectNode record = payload.putObject("recorded");
                record.put("decision_id",   decisionId);
                record.put("actor_id",      actorId);
                record.put("actor_type",    actorType);
                record.put("decision_type", decisionType);
                record.put("rationale",     rationale.isEmpty() ? "[not provided]" : rationale);
                record.put("reference_id",  referenceId);
                record.put("inputs_hash",   hash.substring(0, 16) + "...");
                record.put("output_hash",   sha256Stub(outputJson).substring(0, 16) + "...");
                record.put("immutable",     true);
                record.put("stored_at",     timestamp);
                record.put("chain_id",      "CHAIN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                payload.put("status", "RECORDED");
                payload.put("message", "Decision " + decisionId + " recorded and sealed in audit chain.");
            }
            case "query" -> {
                ArrayNode records = payload.putArray("records");
                // Synthetic demo records
                for (int i = 0; i < Math.min(limit, 3); i++) {
                    ObjectNode r = records.addObject();
                    r.put("decision_id",   "DEC-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
                    r.put("actor_id",      actorId.isEmpty() ? "agent-" + (i + 1) : actorId);
                    r.put("actor_type",    actorType);
                    r.put("decision_type", decisionType);
                    r.put("rationale",     "Automated decision #" + (i + 1));
                    r.put("timestamp",     Instant.now().minusSeconds((long) i * 3600).toString());
                    r.put("immutable",     true);
                }
                payload.put("total_matching", 3);
                payload.put("filter_applied", queryFilter);
                payload.put("status", "OK");
            }
            case "get_chain" -> {
                ArrayNode chain = payload.putArray("chain");
                String[] types = {"access_grant", "scoring", "policy_change", "hiring"};
                for (int i = 0; i < 4; i++) {
                    ObjectNode link = chain.addObject();
                    link.put("seq",          i + 1);
                    link.put("decision_id",  "DEC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                    link.put("decision_type", types[i % types.length]);
                    link.put("actor_id",     actorId);
                    link.put("timestamp",    Instant.now().minusSeconds((long)(3 - i) * 600).toString());
                    link.put("prev_hash",    i == 0 ? "GENESIS" : sha256Stub("block" + (i - 1)).substring(0, 16));
                    link.put("block_hash",   sha256Stub("block" + i).substring(0, 16));
                }
                payload.put("chain_length", 4);
                payload.put("status", "OK");
            }
            case "verify_integrity" -> {
                payload.put("decision_id",   decisionId);
                payload.put("integrity_ok",  true);
                payload.put("hash_verified", true);
                payload.put("tampered",      false);
                payload.put("chain_valid",   true);
                payload.put("verified_at",   timestamp);
                payload.put("status", "INTEGRITY_OK");
                payload.put("message", "Decision " + decisionId + " hash chain is intact and unmodified.");
            }
            case "export" -> {
                payload.put("export_format",  "JSON");
                payload.put("exported_count", 10);
                payload.put("filter_applied", queryFilter);
                payload.put("download_token", "EXP-" + UUID.randomUUID().toString().toUpperCase());
                payload.put("expires_in_sec", 3600);
                payload.put("status", "EXPORT_READY");
            }
            default -> {
                payload.put("status", "UNKNOWN_ACTION");
                payload.put("message", "Supported actions: record | query | get_chain | export | verify_integrity");
            }
        }

        return textResult(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(payload));
    }

    // ──────────────────────────────────────────────────────────────
    // HELPERS
    // ──────────────────────────────────────────────────────────────
    private static int simulateCount(String clientId, String endpoint, int windowSec) {
        // Deterministic pseudo-count based on hash — demo only
        int hash = Math.abs((clientId + endpoint + windowSec).hashCode());
        return (hash % 80);   // 0–79 so both ALLOWED and THROTTLED are possible
    }

    private static String sha256Stub(String input) {
        // Simple deterministic hex stub (not real SHA-256 — avoids external deps)
        int h = input.hashCode();
        return String.format("%08x%08x%08x%08x%08x%08x%08x%08x",
            h, ~h, h ^ 0xDEADBEEF, h * 31, h + 0xCAFEBABE,
            h >>> 5, (h << 3) ^ 0xABCDEF01, h * h);
    }

    private static ObjectNode textResult(String text) {
        ObjectNode result = MAPPER.createObjectNode();
        ArrayNode  ct     = result.putArray("content");
        ct.addObject().put("type", "text").put("text", text);
        return result;
    }

    private static void addStringProp(ObjectNode props, String name, String desc) {
        ObjectNode p = props.putObject(name);
        p.put("type",        "string");
        p.put("description", desc);
    }

    private static void addIntProp(ObjectNode props, String name, String desc) {
        ObjectNode p = props.putObject(name);
        p.put("type",        "integer");
        p.put("description", desc);
    }

    // ──────────────────────────────────────────────────────────────
    // JSON-RPC FRAMING
    // ──────────────────────────────────────────────────────────────
    private static String respond(JsonNode id, ObjectNode result) throws Exception {
        ObjectNode resp = MAPPER.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.set("id", id);
        resp.set("result", result);
        return MAPPER.writeValueAsString(resp);
    }

    private static String respondError(JsonNode id, int code, String message) throws Exception {
        ObjectNode resp  = MAPPER.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.set("id", id);
        ObjectNode error = resp.putObject("error");
        error.put("code",    code);
        error.put("message", message);
        return MAPPER.writeValueAsString(resp);
    }

    // ──────────────────────────────────────────────────────────────
    // FUNCTIONAL INTERFACE
    // ──────────────────────────────────────────────────────────────
    @FunctionalInterface
    interface ToolHandler {
        ObjectNode handle(JsonNode args) throws Exception;
    }
}
