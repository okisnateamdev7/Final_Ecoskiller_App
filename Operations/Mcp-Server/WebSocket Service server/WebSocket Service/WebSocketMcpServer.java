package com.ecoskiller.websocket.server;

import com.ecoskiller.websocket.security.RateLimiter;
import com.ecoskiller.websocket.security.InputSanitizer;
import com.ecoskiller.websocket.model.ToolResult;
import com.ecoskiller.websocket.tools.*;
import com.ecoskiller.websocket.util.JsonUtils;
import com.ecoskiller.websocket.util.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * WebSocketMcpServer — Ecoskiller WebSocket Service MCP Server
 *
 * Transport : stdio (stdin/stdout)
 * Protocol  : JSON-RPC 2.0 / MCP 2024-11-05
 * Namespace : realtime (gd-orchestrator + interview-service + dojo-match-engine)
 *
 * Security:
 *   - InputSanitizer (10 layers: shell/SQL/XSS/sessionId/tenantId/JWT/URL/enum/size/nullbyte)
 *   - RateLimiter (60 req/min sliding window)
 *   - Multi-tenant isolation: tenant_id required on all session operations
 *   - JWT format validation before simulated WSS upgrade
 *
 * Tools (20):
 *   1.  create_session         — Create GD/Interview/Dojo WebSocket session
 *   2.  connect_participant    — JWT auth + admission (mirrors NGINX auth_request)
 *   3.  start_gd_session       — WAITING→INTRO, MUTE_ALL, Redis TTL set
 *   4.  advance_gd_state       — Full GD state machine transitions
 *   5.  raise_hand             — RAISE_HAND → Redis LIST → QUEUE_UPDATE
 *   6.  push_timer_update      — Server-authoritative TIMER_UPDATE (1s resolution)
 *   7.  push_mute_event        — MUTE_ALL (all) / UNMUTE (speaker only)
 *   8.  start_interview        — INTERVIEW_STARTED to both parties
 *   9.  control_interview      — PAUSE/RESUME/END → Kafka interview.completed
 *   10. push_note_lock         — NOTE_LOCK to recruiter only
 *   11. start_dojo_match       — MATCH_START + SCENARIO_ASSIGN
 *   12. handle_dojo_submission — PEER_SUBMITTED (no solution) → MATCH_END
 *   13. handle_disconnection   — Graceful failover + speaker advance
 *   14. get_session_state      — Full state + Redis keys + SLO + event log
 *   15. list_sessions          — Tenant-scoped session listing
 *   16. get_slo_metrics        — Interview 99.9% / GD 99% SLO dashboard
 *   17. end_session            — SESSION_END → Kafka gd.completed/interview.completed
 *   18. get_redis_keys         — All Redis key patterns per spec §10.3
 *   19. validate_jwt           — JWT format + NGINX auth_request flow simulation
 *   20. get_env_config         — WSS URLs per environment per spec §10.2
 */
public class WebSocketMcpServer {

    private static final String MCP_VERSION    = "2024-11-05";
    private static final String SERVER_NAME    = "websocket-service-mcp";
    private static final String SERVER_VERSION = "1.0.0";

    private final Map<String, McpTool> tools = new LinkedHashMap<>();
    private final RateLimiter rateLimiter     = new RateLimiter(60, 60_000);
    private final Logger logger               = new Logger("WebSocketMcpServer");

    public WebSocketMcpServer() {
        // Session lifecycle
        tools.put("create_session",          new CreateSessionTool());
        tools.put("connect_participant",     new ConnectParticipantTool());
        tools.put("end_session",             new EndSessionTool());

        // GD Orchestrator
        tools.put("start_gd_session",        new StartGdSessionTool());
        tools.put("advance_gd_state",        new AdvanceGdStateTool());
        tools.put("raise_hand",              new RaiseHandTool());
        tools.put("push_timer_update",       new PushTimerUpdateTool());
        tools.put("push_mute_event",         new PushMuteEventTool());

        // Interview Service
        tools.put("start_interview",         new StartInterviewTool());
        tools.put("control_interview",       new ControlInterviewTool());
        tools.put("push_note_lock",          new PushNoteLockTool());

        // Dojo Match Engine
        tools.put("start_dojo_match",        new StartDojoMatchTool());
        tools.put("handle_dojo_submission",  new HandleDojoSubmissionTool());

        // Connection management
        tools.put("handle_disconnection",    new HandleDisconnectionTool());

        // Observability
        tools.put("get_session_state",       new GetSessionStateTool());
        tools.put("list_sessions",           new ListSessionsTool());
        tools.put("get_slo_metrics",         new GetSloMetricsTool());
        tools.put("get_redis_keys",          new GetRedisKeysTool());

        // Auth & configuration
        tools.put("validate_jwt",            new ValidateJwtTool());
        tools.put("get_env_config",          new GetEnvironmentConfigTool());
    }

    // ── Server loop ───────────────────────────────────────────────────────────

    public void run() throws IOException {
        logger.info("WebSocket MCP Server v" + SERVER_VERSION +
                    " — " + tools.size() + " tools — realtime namespace");

        BufferedReader in  = new BufferedReader(
            new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(
            new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                String response = handle(line);
                if (response != null) { out.println(response); out.flush(); }
            } catch (Exception e) {
                logger.error("Unhandled: " + e.getMessage());
                out.println(JsonUtils.err(null, -32603, "Internal error: " + e.getMessage()));
                out.flush();
            }
        }
        logger.info("Server shutting down.");
    }

    // ── Request dispatch ──────────────────────────────────────────────────────

    private String handle(String raw) {
        Map<String, Object> req;
        try { req = JsonUtils.parse(raw); }
        catch (Exception e) { return JsonUtils.err(null, -32700, "Parse error: invalid JSON"); }

        Object id     = req.get("id");
        String method = (String) req.get("method");
        if (method == null) return JsonUtils.err(id, -32600, "Missing method");

        if (!"ping".equals(method) && !rateLimiter.allow("global"))
            return JsonUtils.err(id, -32000, "Rate limit exceeded — 60 req/min max.");

        switch (method) {
            case "initialize":  return handleInit(id);
            case "tools/list":  return handleList(id);
            case "tools/call":  return handleCall(id, req);
            case "ping":        return JsonUtils.ok(id, new LinkedHashMap<>());
            default:            return JsonUtils.err(id, -32601, "Method not found: " + method);
        }
    }

    private String handleInit(Object id) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("protocolVersion", MCP_VERSION);
        Map<String, Object> si = new LinkedHashMap<>();
        si.put("name", SERVER_NAME); si.put("version", SERVER_VERSION);
        r.put("serverInfo", si);
        Map<String, Object> caps = new LinkedHashMap<>();
        Map<String, Object> tc   = new LinkedHashMap<>();
        tc.put("listChanged", false);
        caps.put("tools", tc);
        r.put("capabilities", caps);
        return JsonUtils.ok(id, r);
    }

    private String handleList(Object id) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map.Entry<String, McpTool> e : tools.entrySet())
            list.add(e.getValue().getSchema(e.getKey()));
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("tools", list);
        return JsonUtils.ok(id, r);
    }

    @SuppressWarnings("unchecked")
    private String handleCall(Object id, Map<String, Object> req) {
        Map<String, Object> params = (Map<String, Object>) req.get("params");
        if (params == null) return JsonUtils.err(id, -32602, "Missing params");

        String toolName = (String) params.get("name");
        if (toolName == null || toolName.isBlank())
            return JsonUtils.err(id, -32602, "Missing tool name");

        // Security: validate tool name format before lookup
        if (!toolName.matches("[a-z_]+"))
            return JsonUtils.err(id, -32602, "Invalid tool name format: " + toolName);

        Map<String, Object> arguments = (Map<String, Object>)
            params.getOrDefault("arguments", new HashMap<>());

        McpTool tool = tools.get(toolName);
        if (tool == null)
            return JsonUtils.err(id, -32602, "Unknown tool: " + toolName);

        try {
            // Security: sanitize ALL arguments before execution
            Map<String, Object> sanitized = InputSanitizer.sanitize(arguments);
            ToolResult result = tool.execute(sanitized);
            return JsonUtils.toolOk(id, result);
        } catch (SecurityException e) {
            logger.warn("Security violation [" + toolName + "]: " + e.getMessage());
            return JsonUtils.err(id, -32000, "Security violation: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return JsonUtils.err(id, -32602, "Invalid argument: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Tool error [" + toolName + "]: " + e.getMessage());
            return JsonUtils.toolErr(id, "Tool failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            new WebSocketMcpServer().run();
        } catch (IOException e) {
            System.err.println("[FATAL] " + e.getMessage());
            System.exit(1);
        }
    }
}
