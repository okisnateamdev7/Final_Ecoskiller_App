package com.ecoskiller.userservice.server;

import com.ecoskiller.userservice.security.RateLimiter;
import com.ecoskiller.userservice.security.InputSanitizer;
import com.ecoskiller.userservice.model.ToolResult;
import com.ecoskiller.userservice.tools.*;
import com.ecoskiller.userservice.util.JsonUtils;
import com.ecoskiller.userservice.util.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * UserMcpServer — Ecoskiller user-service MCP Server
 *
 * Transport : stdio (stdin/stdout)
 * Protocol  : JSON-RPC 2.0 / MCP 2024-11-05
 * Namespace : core (Kubernetes)
 * Replicas  : 3–10 (HPA)
 *
 * Security:
 *   - InputSanitizer (11 layers: shell/SQL/XSS/email/UUID/tenant/enum/size/null-byte/PII/URL)
 *   - RateLimiter (60 req/min sliding window)
 *   - RLS enforcement: every tool requires tenant_id, cross-tenant access blocked
 *   - Immutable audit log on all mutations
 *   - Soft-delete only (never hard delete — GDPR/SOC2)
 *
 * Tools (20):
 *   1.  create_user              — Create user (auth.user_registered Kafka trigger)
 *   2.  get_user                 — Fetch profile (RLS enforced)
 *   3.  update_profile           — Update core profile fields + Kafka event
 *   4.  update_skills            — Add/remove skills with pgvector embeddings
 *   5.  update_preferences       — Theme, notifications, privacy, language
 *   6.  get_profile_completion   — Completion % for recruiter visibility algorithm
 *   7.  list_users               — Tenant-scoped user listing
 *   8.  search_by_skill          — Skill-based search (pgvector cosine similarity)
 *   9.  award_badge              — scoring.badge_awarded Kafka consumer
 *   10. unlock_scenario          — dojo.scenario_completed Kafka consumer
 *   11. update_streak            — Daily streak (Redis + PostgreSQL sync)
 *   12. manage_goals             — user_goals table: CREATE/PROGRESS/LIST
 *   13. manage_showcase          — profile_showcases (max 3 achievements)
 *   14. soft_delete_user         — GDPR soft delete (archived_at, never hard delete)
 *   15. get_audit_log            — user_audit_log → Elasticsearch forensics
 *   16. verify_user              — Email/phone verification from auth-service
 *   17. get_tenant_stats         — Dashboard analytics: type/domain/skill/completion
 *   18. unlock_theme             — Gamification theme unlock on milestone
 *   19. simulate_kafka_event     — Test all 4 inbound Kafka event flows
 *   20. get_skill_vector         — ML-consumable pgvector data for matching-service
 */
public class UserMcpServer {

    private static final String MCP_VERSION    = "2024-11-05";
    private static final String SERVER_NAME    = "user-service-mcp";
    private static final String SERVER_VERSION = "1.0.0";

    private final Map<String, McpTool> tools   = new LinkedHashMap<>();
    private final RateLimiter rateLimiter       = new RateLimiter(60, 60_000);
    private final Logger logger                 = new Logger("UserMcpServer");

    public UserMcpServer() {
        // Core profile management
        tools.put("create_user",            new CreateUserTool());
        tools.put("get_user",               new GetUserTool());
        tools.put("update_profile",         new UpdateProfileTool());
        tools.put("update_skills",          new UpdateSkillsTool());
        tools.put("update_preferences",     new UpdatePreferencesTool());
        tools.put("get_profile_completion", new GetProfileCompletionTool());

        // Query and search
        tools.put("list_users",             new ListUsersTool());
        tools.put("search_by_skill",        new SearchBySkillTool());
        tools.put("get_skill_vector",       new GetSkillVectorTool());

        // Gamification (Kafka event consumers)
        tools.put("award_badge",            new AwardBadgeTool());
        tools.put("unlock_scenario",        new UnlockScenarioTool());
        tools.put("update_streak",          new UpdateStreakTool());
        tools.put("manage_goals",           new ManageGoalsTool());
        tools.put("manage_showcase",        new ManageShowcaseTool());
        tools.put("unlock_theme",           new UnlockThemeTool());

        // Lifecycle and compliance
        tools.put("soft_delete_user",       new SoftDeleteUserTool());
        tools.put("verify_user",            new VerifyUserTool());
        tools.put("get_audit_log",          new GetAuditLogTool());

        // Analytics and integration
        tools.put("get_tenant_stats",       new GetTenantStatsTool());
        tools.put("simulate_kafka_event",   new SimulateKafkaEventTool());
    }

    // ── Server loop ───────────────────────────────────────────────────────────

    public void run() throws IOException {
        logger.info("user-service MCP Server v" + SERVER_VERSION +
                    " — " + tools.size() + " tools — RLS enforced");

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
            // Security: sanitize all arguments before execution
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
            new UserMcpServer().run();
        } catch (IOException e) {
            System.err.println("[FATAL] " + e.getMessage());
            System.exit(1);
        }
    }
}
