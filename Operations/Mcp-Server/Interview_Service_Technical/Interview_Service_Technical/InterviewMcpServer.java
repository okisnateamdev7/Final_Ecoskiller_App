package com.ecoskiller.mcp.interview;

import com.ecoskiller.mcp.interview.security.JwtValidator;
import com.ecoskiller.mcp.interview.security.RateLimiter;
import com.ecoskiller.mcp.interview.security.AuditLogger;
import com.ecoskiller.mcp.interview.agents.*;
import com.ecoskiller.mcp.interview.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Ecoskiller Interview-Service MCP Server (Java)
 * CAT: Interview & Assessment | Priority: HIGH
 *
 * Transport: stdio (stdin/stdout)
 * Protocol: JSON-RPC 2.0 / MCP 2024-11-05
 *
 * Security Features:
 *  - JWT validation (Keycloak OIDC)
 *  - Rate limiting per tenant
 *  - Immutable audit logging
 *  - Multi-tenant isolation (tenant_id scoping)
 *  - Input sanitisation & schema validation
 */
public class InterviewMcpServer {

    private static final Logger LOG = Logger.getLogger(InterviewMcpServer.class.getName());
    private static final String MCP_VERSION = "2024-11-05";
    private static final String SERVER_NAME  = "ecoskiller-interview-service";
    private static final String SERVER_VER   = "1.0.0";

    private final ObjectMapper mapper = new ObjectMapper();
    private final JwtValidator  jwtValidator  = new JwtValidator();
    private final RateLimiter   rateLimiter   = new RateLimiter();
    private final AuditLogger   auditLogger   = new AuditLogger();

    // ── Agents ────────────────────────────────────────────────────────────────
    private final ScheduleInterviewAgent      scheduleAgent      = new ScheduleInterviewAgent();
    private final GetInterviewAgent           getAgent           = new GetInterviewAgent();
    private final UpdateInterviewAgent        updateAgent        = new UpdateInterviewAgent();
    private final JoinInterviewAgent          joinAgent          = new JoinInterviewAgent();
    private final PauseInterviewAgent         pauseAgent         = new PauseInterviewAgent();
    private final ResumeInterviewAgent        resumeAgent        = new ResumeInterviewAgent();
    private final CompleteInterviewAgent      completeAgent      = new CompleteInterviewAgent();
    private final GetInterviewResultAgent     resultAgent        = new GetInterviewResultAgent();
    private final SubmitFeedbackAgent         feedbackAgent      = new SubmitFeedbackAgent();
    private final GetQuestionsAgent           questionsAgent     = new GetQuestionsAgent();
    private final LogAnswerAgent              logAnswerAgent     = new LogAnswerAgent();
    private final ListInterviewsAgent         listAgent          = new ListInterviewsAgent();
    private final RescheduleInterviewAgent    rescheduleAgent    = new RescheduleInterviewAgent();
    private final RecordingConsentAgent       consentAgent       = new RecordingConsentAgent();
    private final DeleteRecordingAgent        deleteRecordingAgent = new DeleteRecordingAgent();
    private final AnalyticsAgent              analyticsAgent     = new AnalyticsAgent();
    private final NoShowDetectionAgent        noShowAgent        = new NoShowDetectionAgent();
    private final SessionHealthAgent          healthAgent        = new SessionHealthAgent();

    public static void main(String[] args) throws IOException {
        new InterviewMcpServer().run();
    }

    private void run() throws IOException {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);

        LOG.info("Ecoskiller Interview-Service MCP Server started (v" + SERVER_VER + ")");

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                JsonNode request  = mapper.readTree(line);
                JsonNode response = handleRequest(request);
                if (response != null) {
                    out.println(mapper.writeValueAsString(response));
                }
            } catch (Exception e) {
                LOG.warning("Parse error: " + e.getMessage());
                out.println(buildParseError());
            }
        }
    }

    // ── Request Router ────────────────────────────────────────────────────────

    private JsonNode handleRequest(JsonNode req) {
        String id     = req.has("id")     ? req.get("id").asText()     : null;
        String method = req.has("method") ? req.get("method").asText() : "";
        JsonNode params = req.path("params");

        return switch (method) {
            case "initialize"   -> handleInitialize(id, params);
            case "ping"         -> buildResult(id, mapper.createObjectNode());
            case "tools/list"   -> handleToolsList(id);
            case "tools/call"   -> handleToolCall(id, params);
            default             -> buildError(id, -32601, "Method not found: " + method);
        };
    }

    // ── Initialize ────────────────────────────────────────────────────────────

    private JsonNode handleInitialize(String id, JsonNode params) {
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", MCP_VERSION);

        ObjectNode serverInfo = result.putObject("serverInfo");
        serverInfo.put("name",    SERVER_NAME);
        serverInfo.put("version", SERVER_VER);

        ObjectNode capabilities = result.putObject("capabilities");
        capabilities.putObject("tools");
        return buildResult(id, result);
    }

    // ── Tools List ────────────────────────────────────────────────────────────

    private JsonNode handleToolsList(String id) {
        ArrayNode tools = mapper.createArrayNode();
        for (ToolDefinition def : getToolDefinitions()) {
            ObjectNode tool = mapper.createObjectNode();
            tool.put("name", def.name());
            tool.put("description", def.description());
            tool.set("inputSchema", mapper.valueToTree(def.schema()));
            tools.add(tool);
        }
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", tools);
        return buildResult(id, result);
    }

    // ── Tool Call ─────────────────────────────────────────────────────────────

    private JsonNode handleToolCall(String id, JsonNode params) {
        String toolName = params.path("name").asText();
        JsonNode args   = params.path("arguments");

        // ── Security: JWT validation ───────────────────────────────────────
        String jwt = args.path("auth_token").asText(null);
        SecurityContext ctx;
        try {
            ctx = jwtValidator.validate(jwt);
        } catch (SecurityException e) {
            auditLogger.log("SECURITY", toolName, "JWT_INVALID", args);
            return buildToolError(id, "UNAUTHORIZED: " + e.getMessage());
        }

        // ── Security: Rate limiting ────────────────────────────────────────
        if (!rateLimiter.allow(ctx.tenantId(), toolName)) {
            auditLogger.log("RATE_LIMIT", toolName, ctx.tenantId(), args);
            return buildToolError(id, "RATE_LIMITED: Too many requests for tenant " + ctx.tenantId());
        }

        // ── Security: Role-based access control ───────────────────────────
        try {
            enforceRbac(toolName, ctx.role());
        } catch (SecurityException e) {
            auditLogger.log("RBAC_DENIED", toolName, ctx.tenantId(), args);
            return buildToolError(id, "FORBIDDEN: " + e.getMessage());
        }

        // ── Dispatch ──────────────────────────────────────────────────────
        auditLogger.log("TOOL_CALL", toolName, ctx.tenantId(), args);
        try {
            String result = dispatch(toolName, args, ctx);
            auditLogger.log("TOOL_SUCCESS", toolName, ctx.tenantId(), null);
            return buildToolResult(id, result);
        } catch (IllegalArgumentException e) {
            return buildToolError(id, "INVALID_INPUT: " + e.getMessage());
        } catch (Exception e) {
            LOG.severe("Agent error [" + toolName + "]: " + e.getMessage());
            return buildToolError(id, "INTERNAL_ERROR: " + e.getMessage());
        }
    }

    // ── RBAC Enforcement ──────────────────────────────────────────────────────

    private void enforceRbac(String tool, String role) {
        Set<String> recruiterOnly = Set.of(
            "schedule_interview", "update_interview", "pause_interview",
            "resume_interview", "complete_interview", "submit_feedback",
            "reschedule_interview", "delete_recording", "get_analytics",
            "detect_no_show"
        );
        Set<String> candidateAllowed = Set.of(
            "get_interview", "join_interview", "get_questions",
            "log_answer", "grant_recording_consent", "get_session_health"
        );
        boolean isRecruiter  = role.equals("RECRUITER") || role.equals("INTERVIEWER") || role.equals("ADMIN");
        boolean isCandidate  = role.equals("CANDIDATE");

        if (recruiterOnly.contains(tool) && !isRecruiter) {
            throw new SecurityException("Tool '" + tool + "' requires RECRUITER/INTERVIEWER role. Got: " + role);
        }
        // All authenticated roles can call list_interviews (filtered by role server-side)
    }

    // ── Agent Dispatch ────────────────────────────────────────────────────────

    private String dispatch(String tool, JsonNode args, SecurityContext ctx) {
        return switch (tool) {
            case "schedule_interview"       -> scheduleAgent.execute(args, ctx);
            case "get_interview"            -> getAgent.execute(args, ctx);
            case "update_interview"         -> updateAgent.execute(args, ctx);
            case "join_interview"           -> joinAgent.execute(args, ctx);
            case "pause_interview"          -> pauseAgent.execute(args, ctx);
            case "resume_interview"         -> resumeAgent.execute(args, ctx);
            case "complete_interview"       -> completeAgent.execute(args, ctx);
            case "get_interview_result"     -> resultAgent.execute(args, ctx);
            case "submit_feedback"          -> feedbackAgent.execute(args, ctx);
            case "get_questions"            -> questionsAgent.execute(args, ctx);
            case "log_answer"               -> logAnswerAgent.execute(args, ctx);
            case "list_interviews"          -> listAgent.execute(args, ctx);
            case "reschedule_interview"     -> rescheduleAgent.execute(args, ctx);
            case "grant_recording_consent"  -> consentAgent.execute(args, ctx);
            case "delete_recording"         -> deleteRecordingAgent.execute(args, ctx);
            case "get_analytics"            -> analyticsAgent.execute(args, ctx);
            case "detect_no_show"           -> noShowAgent.execute(args, ctx);
            case "get_session_health"       -> healthAgent.execute(args, ctx);
            default -> throw new IllegalArgumentException("Unknown tool: " + tool);
        };
    }

    // ── Tool Definitions ──────────────────────────────────────────────────────

    private List<ToolDefinition> getToolDefinitions() {
        return List.of(
            new ToolDefinition("schedule_interview",
                "Schedule a new one-on-one video interview. Creates interview record and reserves Jitsi room. " +
                "Publishes interview.scheduled.acked Kafka event.",
                Map.of("type", "object",
                    "required", List.of("auth_token","job_id","candidate_id","recruiter_id","scheduled_time","duration_minutes"),
                    "properties", Map.of(
                        "auth_token",      prop("string","Keycloak JWT bearer token"),
                        "job_id",          prop("string","Job posting UUID"),
                        "candidate_id",    prop("string","Candidate user UUID"),
                        "recruiter_id",    prop("string","Recruiter user UUID"),
                        "scheduled_time",  prop("string","ISO-8601 datetime for interview"),
                        "duration_minutes",prop("integer","Duration: 15, 30, 60 or custom"),
                        "template_id",     prop("string","Interview question template UUID (optional)"),
                        "recording_enabled",prop("boolean","Whether to enable recording (requires candidate consent)"),
                        "tenant_id",       prop("string","Tenant UUID for multi-tenant isolation")
                    )
                )
            ),
            new ToolDefinition("get_interview",
                "Retrieve full interview details: status, participants, scores, feedback. Enforces tenant_id RLS.",
                Map.of("type","object",
                    "required", List.of("auth_token","interview_id"),
                    "properties", Map.of(
                        "auth_token",   prop("string","Keycloak JWT bearer token"),
                        "interview_id", prop("string","Interview UUID")
                    )
                )
            ),
            new ToolDefinition("update_interview",
                "Update interview details: reschedule, change interviewer, or cancel. Validates state transitions.",
                Map.of("type","object",
                    "required", List.of("auth_token","interview_id"),
                    "properties", Map.of(
                        "auth_token",       prop("string","Keycloak JWT bearer token"),
                        "interview_id",     prop("string","Interview UUID"),
                        "scheduled_time",   prop("string","New scheduled datetime (optional)"),
                        "recruiter_id",     prop("string","New recruiter UUID (optional)"),
                        "status",           prop("string","New status (CANCELLED only via this endpoint)")
                    )
                )
            ),
            new ToolDefinition("join_interview",
                "Request Jitsi room access. Returns short-lived media JWT (5-min TTL) and jitsi_url. " +
                "Validates camera/microphone permissions.",
                Map.of("type","object",
                    "required", List.of("auth_token","interview_id","participant_type"),
                    "properties", Map.of(
                        "auth_token",       prop("string","Keycloak JWT bearer token"),
                        "interview_id",     prop("string","Interview UUID"),
                        "participant_type", prop("string","RECRUITER | CANDIDATE | OBSERVER")
                    )
                )
            ),
            new ToolDefinition("pause_interview",
                "Pause active interview session (interviewer only). Max pause: 5 minutes. Timer suspended.",
                Map.of("type","object",
                    "required", List.of("auth_token","interview_id"),
                    "properties", Map.of(
                        "auth_token",   prop("string","Keycloak JWT bearer token"),
                        "interview_id", prop("string","Interview UUID")
                    )
                )
            ),
            new ToolDefinition("resume_interview",
                "Resume a paused interview session. Restores timer from suspension point.",
                Map.of("type","object",
                    "required", List.of("auth_token","interview_id"),
                    "properties", Map.of(
                        "auth_token",   prop("string","Keycloak JWT bearer token"),
                        "interview_id", prop("string","Interview UUID")
                    )
                )
            ),
            new ToolDefinition("complete_interview",
                "Mark interview as completed. Triggers async AI scoring pipeline via scoring-engine. " +
                "Publishes interview.completed Kafka event.",
                Map.of("type","object",
                    "required", List.of("auth_token","interview_id"),
                    "properties", Map.of(
                        "auth_token",   prop("string","Keycloak JWT bearer token"),
                        "interview_id", prop("string","Interview UUID"),
                        "completion_reason", prop("string","NORMAL | TIMEOUT | TECHNICAL_ISSUE")
                    )
                )
            ),
            new ToolDefinition("get_interview_result",
                "Retrieve final merged result: AI scores (40%) + interviewer feedback (60%), dimension breakdowns, recommendation.",
                Map.of("type","object",
                    "required", List.of("auth_token","interview_id"),
                    "properties", Map.of(
                        "auth_token",   prop("string","Keycloak JWT bearer token"),
                        "interview_id", prop("string","Interview UUID")
                    )
                )
            ),
            new ToolDefinition("submit_feedback",
                "Interviewer submits post-interview feedback: communication_score, technical_score, cultural_fit_score, " +
                "comments, recommendation (HIRE|MAYBE|REJECT). Triggers score merge.",
                Map.of("type","object",
                    "required", List.of("auth_token","interview_id","communication_score","technical_score","cultural_fit_score","recommendation"),
                    "properties", Map.of(
                        "auth_token",           prop("string","Keycloak JWT bearer token"),
                        "interview_id",         prop("string","Interview UUID"),
                        "communication_score",  prop("integer","Score 1-5: clarity, articulation, listening"),
                        "technical_score",      prop("integer","Score 1-5: depth, accuracy, problem-solving"),
                        "cultural_fit_score",   prop("integer","Score 1-5: alignment with company values"),
                        "comments",             prop("string","Open-ended interviewer feedback"),
                        "recommendation",       prop("string","HIRE | MAYBE | REJECT")
                    )
                )
            ),
            new ToolDefinition("get_questions",
                "Retrieve interview question template for the session. Returns ordered questions with estimated time.",
                Map.of("type","object",
                    "required", List.of("auth_token","interview_id"),
                    "properties", Map.of(
                        "auth_token",   prop("string","Keycloak JWT bearer token"),
                        "interview_id", prop("string","Interview UUID")
                    )
                )
            ),
            new ToolDefinition("log_answer",
                "Log candidate response to a specific interview question. Records answer_text and duration.",
                Map.of("type","object",
                    "required", List.of("auth_token","interview_id","question_id","answer_text"),
                    "properties", Map.of(
                        "auth_token",       prop("string","Keycloak JWT bearer token"),
                        "interview_id",     prop("string","Interview UUID"),
                        "question_id",      prop("string","Question UUID"),
                        "answer_text",      prop("string","Candidate's spoken/typed answer"),
                        "duration_seconds", prop("integer","Time taken to answer in seconds")
                    )
                )
            ),
            new ToolDefinition("list_interviews",
                "List interviews filtered by status, date range, recruiter_id or candidate_id. " +
                "Results scoped by tenant_id and caller role.",
                Map.of("type","object",
                    "required", List.of("auth_token"),
                    "properties", Map.of(
                        "auth_token",    prop("string","Keycloak JWT bearer token"),
                        "status",        prop("string","SCHEDULED | STARTED | PAUSED | COMPLETED | CANCELLED | NO_SHOW"),
                        "date_from",     prop("string","ISO-8601 date filter start"),
                        "date_to",       prop("string","ISO-8601 date filter end"),
                        "recruiter_id",  prop("string","Filter by recruiter UUID"),
                        "candidate_id",  prop("string","Filter by candidate UUID"),
                        "page",          prop("integer","Pagination page number (default: 1)"),
                        "page_size",     prop("integer","Page size (default: 20, max: 100)")
                    )
                )
            ),
            new ToolDefinition("reschedule_interview",
                "Reschedule interview (no-show recovery or recruiter request). Notifies candidate via notification-service.",
                Map.of("type","object",
                    "required", List.of("auth_token","interview_id","new_scheduled_time","reason"),
                    "properties", Map.of(
                        "auth_token",         prop("string","Keycloak JWT bearer token"),
                        "interview_id",       prop("string","Interview UUID"),
                        "new_scheduled_time", prop("string","ISO-8601 new scheduled datetime"),
                        "reason",             prop("string","NO_SHOW | RECRUITER_REQUEST | CANDIDATE_REQUEST | TECHNICAL_ISSUE")
                    )
                )
            ),
            new ToolDefinition("grant_recording_consent",
                "Candidate grants consent to interview recording (DPDPA 2023 compliant). " +
                "Consent timestamp logged immutably. Required before recording can begin.",
                Map.of("type","object",
                    "required", List.of("auth_token","interview_id","consent_granted"),
                    "properties", Map.of(
                        "auth_token",      prop("string","Keycloak JWT bearer token"),
                        "interview_id",    prop("string","Interview UUID"),
                        "consent_granted", prop("boolean","true = consent granted, false = consent denied")
                    )
                )
            ),
            new ToolDefinition("delete_recording",
                "Candidate requests recording deletion (right-to-erasure, DPDPA 2023). " +
                "Soft-delete from MinIO within 30-day retention window. Audit copy retained.",
                Map.of("type","object",
                    "required", List.of("auth_token","interview_id"),
                    "properties", Map.of(
                        "auth_token",   prop("string","Keycloak JWT bearer token"),
                        "interview_id", prop("string","Interview UUID"),
                        "reason",       prop("string","Optional deletion reason")
                    )
                )
            ),
            new ToolDefinition("get_analytics",
                "Recruiter dashboard analytics: avg duration, no-show rate, score distributions, QoS metrics. " +
                "Reads from ClickHouse immutable event log.",
                Map.of("type","object",
                    "required", List.of("auth_token"),
                    "properties", Map.of(
                        "auth_token",    prop("string","Keycloak JWT bearer token"),
                        "date_from",     prop("string","Analytics period start (ISO-8601)"),
                        "date_to",       prop("string","Analytics period end (ISO-8601)"),
                        "recruiter_id",  prop("string","Scope analytics to specific recruiter"),
                        "metric_type",   prop("string","ALL | QOS | SCORES | NO_SHOW | DURATION")
                    )
                )
            ),
            new ToolDefinition("detect_no_show",
                "Trigger no-show detection for an interview. Checks if candidate joined within grace period (5 min). " +
                "Publishes interview.no_show.detected if confirmed. Notifies recruiter.",
                Map.of("type","object",
                    "required", List.of("auth_token","interview_id"),
                    "properties", Map.of(
                        "auth_token",   prop("string","Keycloak JWT bearer token"),
                        "interview_id", prop("string","Interview UUID")
                    )
                )
            ),
            new ToolDefinition("get_session_health",
                "Retrieve real-time WebSocket/Jitsi session health: audio jitter, video bitrate, " +
                "packet loss, round-trip latency, participant connection status.",
                Map.of("type","object",
                    "required", List.of("auth_token","interview_id"),
                    "properties", Map.of(
                        "auth_token",   prop("string","Keycloak JWT bearer token"),
                        "interview_id", prop("string","Interview UUID")
                    )
                )
            )
        );
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Map<String, Object> prop(String type, String description) {
        return Map.of("type", type, "description", description);
    }

    private JsonNode buildResult(String id, Object result) {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.put("id", id);
        resp.set("result", mapper.valueToTree(result));
        return resp;
    }

    private JsonNode buildToolResult(String id, String text) {
        ObjectNode content = mapper.createObjectNode();
        content.put("type", "text");
        content.put("text", text);
        ArrayNode contents = mapper.createArrayNode();
        contents.add(content);
        ObjectNode result = mapper.createObjectNode();
        result.set("content", contents);
        result.put("isError", false);
        return buildResult(id, result);
    }

    private JsonNode buildToolError(String id, String message) {
        ObjectNode content = mapper.createObjectNode();
        content.put("type", "text");
        content.put("text", message);
        ArrayNode contents = mapper.createArrayNode();
        contents.add(content);
        ObjectNode result = mapper.createObjectNode();
        result.set("content", contents);
        result.put("isError", true);
        return buildResult(id, result);
    }

    private JsonNode buildError(String id, int code, String message) {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("jsonrpc", "2.0");
        if (id != null) resp.put("id", id);
        ObjectNode error = resp.putObject("error");
        error.put("code", code);
        error.put("message", message);
        return resp;
    }

    private String buildParseError() {
        return "{\"jsonrpc\":\"2.0\",\"id\":null,\"error\":{\"code\":-32700,\"message\":\"Parse error\"}}";
    }

    // ── Tool Definition record ─────────────────────────────────────────────────
    record ToolDefinition(String name, String description, Map<String, Object> schema) {}
}
