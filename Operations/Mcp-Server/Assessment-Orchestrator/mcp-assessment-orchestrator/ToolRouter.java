package com.ecoskiller.mcp.tools;

import java.util.*;

/**
 * Central dispatcher for all 20 assessment-orchestrator MCP tools.
 *
 * Tool groups:
 *   Cycle Management  (4): create_assessment_cycle, get_cycle_status,
 *                          reschedule_cycle, cancel_cycle
 *   Session Lifecycle (5): create_session, join_session, get_session_state,
 *                          transition_session_state, check_quorum
 *   Session Control   (4): control_session, handle_raise_hand,
 *                          handle_participant_event, process_kafka_event
 *   Kafka & Tokens    (3): emit_completion_event, validate_jitsi_token,
 *                          get_kafka_event_log
 *   Audit & Analytics (4): get_session_audit_log, get_speaking_time_stats,
 *                          get_service_health, configure_track_plan
 */
public class ToolRouter {

    private final CycleManagementTools  cycleTools;
    private final SessionLifecycleTools sessionTools;
    private final SessionControlTools   controlTools;
    private final KafkaTokenTools       kafkaTokenTools;
    private final AuditAnalyticsTools   auditTools;

    public ToolRouter() {
        this.cycleTools      = new CycleManagementTools();
        this.sessionTools    = new SessionLifecycleTools();
        this.controlTools    = new SessionControlTools();
        this.kafkaTokenTools = new KafkaTokenTools();
        this.auditTools      = new AuditAnalyticsTools();
    }

    public String dispatch(String toolName, Map<String, Object> args) {
        switch (toolName) {
            // ── Cycle Management ──────────────────────────────────────────────
            case "create_assessment_cycle":   return cycleTools.createCycle(args);
            case "get_cycle_status":          return cycleTools.getCycleStatus(args);
            case "reschedule_cycle":          return cycleTools.rescheduleCycle(args);
            case "cancel_cycle":              return cycleTools.cancelCycle(args);

            // ── Session Lifecycle ─────────────────────────────────────────────
            case "create_session":            return sessionTools.createSession(args);
            case "join_session":              return sessionTools.joinSession(args);
            case "get_session_state":         return sessionTools.getSessionState(args);
            case "transition_session_state":  return sessionTools.transitionState(args);
            case "check_quorum":              return sessionTools.checkQuorum(args);

            // ── Session Control ───────────────────────────────────────────────
            case "control_session":           return controlTools.controlSession(args);
            case "handle_raise_hand":         return controlTools.handleRaiseHand(args);
            case "handle_participant_event":  return controlTools.handleParticipantEvent(args);
            case "process_kafka_event":       return controlTools.processKafkaEvent(args);

            // ── Kafka & Tokens ────────────────────────────────────────────────
            case "emit_completion_event":     return kafkaTokenTools.emitCompletionEvent(args);
            case "validate_jitsi_token":      return kafkaTokenTools.validateJitsiToken(args);
            case "get_kafka_event_log":       return kafkaTokenTools.getKafkaEventLog(args);

            // ── Audit & Analytics ─────────────────────────────────────────────
            case "get_session_audit_log":     return auditTools.getSessionAuditLog(args);
            case "get_speaking_time_stats":   return auditTools.getSpeakingTimeStats(args);
            case "get_service_health":        return auditTools.getServiceHealth(args);
            case "configure_track_plan":      return auditTools.configureTrackPlan(args);

            default: throw new IllegalArgumentException("Unknown tool: " + toolName);
        }
    }

    // ── Tool definitions for tools/list ──────────────────────────────────────

    public List<Map<String, Object>> getToolsDefinition() {
        List<Map<String, Object>> tools = new ArrayList<>();

        // ── Cycle Management ──────────────────────────────────────────────────
        tools.add(t("create_assessment_cycle",
            "Initialise a new assessment cycle from an assessment.cycle.created Kafka trigger. " +
            "Creates the cycle record, builds the ordered track execution queue (GD→Interview→Dojo " +
            "or tenant-configured order), acquires distributed slot locks for all candidate/slot " +
            "combinations, and persists to PostgreSQL. Emits assessment.session.started per track. " +
            "Requires: RECRUITER or SERVICE_ACCOUNT role.",
            p("auth_token",    "string", "Bearer JWT (RECRUITER or SERVICE_ACCOUNT)"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("job_id",        "string", "Job posting ID triggering this cycle"),
            p("recruiter_id",  "string", "Recruiter user ID"),
            p("candidate_ids", "string", "Comma-separated list of candidate user IDs"),
            p("tracks",        "string", "Ordered track list: GD,INTERVIEW,DOJO (or subset)"),
            p("scheduled_at",  "string", "ISO-8601 start time (e.g. 2026-04-01T10:00:00Z)"),
            p("quorum_required","string","Min participants for session to proceed (default: 3)")
        ));

        tools.add(t("get_cycle_status",
            "Returns full cycle status: active_sessions, completed_tracks, candidate_roster_summary, " +
            "next_scheduled_session. Used by recruiter dashboard and admin-service.",
            p("auth_token", "string", "Bearer JWT"),
            p("tenant_id",  "string", "Tenant identifier"),
            p("cycle_id",   "string", "Assessment cycle UUID")
        ));

        tools.add(t("reschedule_cycle",
            "Admin-only: reschedule all WAITING/SCHEDULED sessions in a cycle to a new time slot. " +
            "Cascades to Redis timer updates and triggers notification-service via Kafka. " +
            "Acquires new distributed slot locks; releases old ones.",
            p("auth_token",     "string", "Bearer JWT (ADMIN only)"),
            p("tenant_id",      "string", "Tenant identifier"),
            p("cycle_id",       "string", "Cycle UUID to reschedule"),
            p("new_start_time", "string", "ISO-8601 new start time"),
            p("reason",         "string", "Reschedule reason for notification message")
        ));

        tools.add(t("cancel_cycle",
            "Cancel an active assessment cycle. Terminates all active sessions within the cycle, " +
            "releases all Redis locks, emits assessment.session.cancelled per session and " +
            "assessment.cycle.completed with CANCELLED status. Cascades absent marks to all rosters.",
            p("auth_token", "string", "Bearer JWT (ADMIN or MODERATOR)"),
            p("tenant_id",  "string", "Tenant identifier"),
            p("cycle_id",   "string", "Cycle UUID to cancel"),
            p("reason",     "string", "MANUAL | TIMEOUT | RECRUITER_REQUEST")
        ));

        // ── Session Lifecycle ─────────────────────────────────────────────────
        tools.add(t("create_session",
            "Create a new assessment session within a cycle for a specific track (GD / INTERVIEW / DOJO). " +
            "Sets state to SCHEDULED. Persists to PostgreSQL ecoskiller.assessment_sessions. " +
            "Writes Redis state key: tenant:{tenant_id}:session:{session_id}:state.",
            p("auth_token",     "string", "Bearer JWT (RECRUITER or SERVICE_ACCOUNT)"),
            p("tenant_id",      "string", "Tenant identifier"),
            p("cycle_id",       "string", "Parent cycle UUID"),
            p("track_type",     "string", "GD | INTERVIEW | DOJO"),
            p("candidate_ids",  "string", "Comma-separated candidate IDs for this session"),
            p("scheduled_at",   "string", "ISO-8601 session start time"),
            p("quorum_required","string", "Min participants needed (default: 3 for GD, 2 for Interview)")
        ));

        tools.add(t("join_session",
            "Candidate or recruiter join a session. Validates Keycloak JWT + session state (must be " +
            "WAITING or INTRO). Issues a Jitsi JWT room token (5-min TTL, HS256). " +
            "Increments participant count in Redis. Returns jitsi_token, ws_endpoint, session_state. " +
            "409 if session not in WAITING/INTRO state.",
            p("auth_token",    "string", "Bearer JWT (CANDIDATE or RECRUITER)"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("session_id",    "string", "Session UUID to join"),
            p("display_name",  "string", "Participant display name for Jitsi room"),
            p("idempotency_key","string","Unique join key to prevent double-join")
        ));

        tools.add(t("get_session_state",
            "Returns the full Redis session state object: current_state, current_speaker_id, " +
            "timer_remaining_seconds, queue_length, participant_count, started_at, track_type, " +
            "raise_hand_queue, roster_status. Used by analytics-service and frontend clients.",
            p("auth_token",  "string", "Bearer JWT (any authenticated role)"),
            p("tenant_id",   "string", "Tenant identifier"),
            p("session_id",  "string", "Session UUID")
        ));

        tools.add(t("transition_session_state",
            "Advance a session through its Redis state machine: " +
            "SCHEDULED→WAITING→INTRO→SPEAKING→OPEN_DISCUSSION→CLOSING→COMPLETED. " +
            "Enforces valid transitions, writes idempotency key on COMPLETED, " +
            "and triggers appropriate Kafka events. Timer-driven transitions are also handled here.",
            p("auth_token",  "string", "Bearer JWT (MODERATOR, ADMIN, or SERVICE_ACCOUNT)"),
            p("tenant_id",   "string", "Tenant identifier"),
            p("session_id",  "string", "Session UUID"),
            p("target_state","string", "WAITING|INTRO|SPEAKING|OPEN_DISCUSSION|CLOSING|COMPLETED|CANCELLED"),
            p("actor_id",    "string", "Who triggered this transition (user ID or TIMER_EXPIRY)")
        ));

        tools.add(t("check_quorum",
            "Check quorum status for a session in WAITING state. If T-2 minutes before start and " +
            "quorum not met, auto-cancels the session, releases Redis locks, emits " +
            "assessment.session.cancelled (NO_QUORUM), and marks all candidates ABSENT.",
            p("auth_token",  "string", "Bearer JWT (any role with viewer access)"),
            p("tenant_id",   "string", "Tenant identifier"),
            p("session_id",  "string", "Session UUID"),
            p("auto_cancel", "string", "true = auto-cancel if quorum not met at threshold")
        ));

        // ── Session Control ───────────────────────────────────────────────────
        tools.add(t("control_session",
            "Moderator control endpoint: FORCE_MUTE, FORCE_UNMUTE, ADVANCE_TURN, END_SESSION. " +
            "Requires ecoskiller:session:moderator role. Validates action against current state machine " +
            "state before executing. Broadcasts appropriate WebSocket command to session participants. " +
            "400 if action invalid for current state.",
            p("auth_token",          "string", "Bearer JWT (MODERATOR role required)"),
            p("tenant_id",           "string", "Tenant identifier"),
            p("session_id",          "string", "Session UUID"),
            p("action",              "string", "FORCE_MUTE|FORCE_UNMUTE|ADVANCE_TURN|END_SESSION"),
            p("target_candidate_id", "string", "Target candidate for FORCE_MUTE/UNMUTE (optional for others)")
        ));

        tools.add(t("handle_raise_hand",
            "Process a RAISE_HAND WebSocket message from a candidate during OPEN_DISCUSSION phase. " +
            "Adds candidate to Redis raise-hand LIST (gd:{session_id}:queue). " +
            "Broadcasts queue_updated to all participants. " +
            "Returns position in queue and estimated wait seconds.",
            p("auth_token",    "string", "Bearer JWT (CANDIDATE)"),
            p("tenant_id",     "string", "Tenant identifier"),
            p("session_id",    "string", "Session UUID"),
            p("candidate_id",  "string", "Candidate raising hand")
        ));

        tools.add(t("handle_participant_event",
            "Process participant WebSocket events: HEARTBEAT, LEAVE_SESSION, PARTICIPANT_JOINED, " +
            "SPEAKER_DISCONNECT. Updates last_seen Redis key for HEARTBEAT. For LEAVE_SESSION, " +
            "marks candidate COMPLETED in roster. For SPEAKER_DISCONNECT, opens 10-second grace " +
            "window before auto-advancing to next speaker.",
            p("auth_token",   "string", "Bearer JWT"),
            p("tenant_id",    "string", "Tenant identifier"),
            p("session_id",   "string", "Session UUID"),
            p("event_type",   "string", "HEARTBEAT|LEAVE_SESSION|PARTICIPANT_JOINED|SPEAKER_DISCONNECT"),
            p("candidate_id", "string", "Candidate triggering the event"),
            p("client_ts",    "string", "Client timestamp (for HEARTBEAT latency tracking)")
        ));

        tools.add(t("process_kafka_event",
            "Consume an incoming Kafka event from upstream producers: assessment.cycle.created, " +
            "interview.session.requested, dojo.match.requested, assessment.cycle.cancelled. " +
            "Routes to appropriate handler based on event type. SERVICE_ACCOUNT role required. " +
            "Implements DLQ logic: max 3 retries with exponential backoff.",
            p("auth_token",  "string", "Bearer JWT (SERVICE_ACCOUNT)"),
            p("tenant_id",   "string", "Tenant identifier"),
            p("event_type",  "string", "assessment.cycle.created|interview.session.requested|dojo.match.requested|assessment.cycle.cancelled"),
            p("event_payload","string","JSON string of the Kafka event payload"),
            p("retry_count", "string", "Current retry count (0-3; triggers DLQ on 4th failure)")
        ));

        // ── Kafka & Tokens ────────────────────────────────────────────────────
        tools.add(t("emit_completion_event",
            "Emit idempotent completion Kafka event for a finished session. " +
            "Writes idempotency_key (session_{id}_completed) to Redis (TTL 24h) BEFORE emitting. " +
            "Publishes gd.completed or interview.completed with full candidate roster and " +
            "speaking_time_per_candidate. Archives session to ClickHouse audit log. " +
            "No-op if idempotency key already exists (prevents double-scoring).",
            p("auth_token",  "string", "Bearer JWT (MODERATOR, ADMIN, or SERVICE_ACCOUNT)"),
            p("tenant_id",   "string", "Tenant identifier"),
            p("session_id",  "string", "Session UUID"),
            p("force",       "string", "true = bypass idempotency check (ADMIN only, use with care)")
        ));

        tools.add(t("validate_jitsi_token",
            "Validate a previously issued Jitsi JWT room token. Checks signature, expiry, " +
            "room name (must match session_id), and role claim. Returns decoded claims if valid. " +
            "Used by NGINX Ingress auth_request or internal validation.",
            p("auth_token",   "string", "Bearer JWT (service validation)"),
            p("tenant_id",    "string", "Tenant identifier"),
            p("jitsi_token",  "string", "The Jitsi JWT token to validate"),
            p("session_id",   "string", "Expected session ID (room name match check)")
        ));

        tools.add(t("get_kafka_event_log",
            "Retrieve the in-memory Kafka event log for debugging and audit. " +
            "In production, this queries ClickHouse or a Kafka consumer offset log. " +
            "Filterable by topic and session_id.",
            p("auth_token",  "string", "Bearer JWT (ADMIN or SERVICE_ACCOUNT)"),
            p("tenant_id",   "string", "Tenant identifier"),
            p("topic",       "string", "Optional: filter by topic (e.g. gd.completed)"),
            p("session_id",  "string", "Optional: filter by session_id"),
            p("limit",       "string", "Max events to return (default: 50)")
        ));

        // ── Audit & Analytics ─────────────────────────────────────────────────
        tools.add(t("get_session_audit_log",
            "Retrieve the full ClickHouse session telemetry: all state transitions, turn durations, " +
            "drop events, participant timeline, and total duration. Required for DPDPA 2023 compliance " +
            "and candidate challenge responses.",
            p("auth_token",  "string", "Bearer JWT (RECRUITER or ADMIN)"),
            p("tenant_id",   "string", "Tenant identifier"),
            p("session_id",  "string", "Session UUID")
        ));

        tools.add(t("get_speaking_time_stats",
            "Returns per-candidate speaking time distribution for a GD session. " +
            "Source: ecoskiller.gd_speaking_time_distributions (ClickHouse). " +
            "Consumed by analytics-service fairness and participation dashboards.",
            p("auth_token",  "string", "Bearer JWT (RECRUITER or ADMIN)"),
            p("tenant_id",   "string", "Tenant identifier"),
            p("session_id",  "string", "GD session UUID")
        ));

        tools.add(t("get_service_health",
            "Returns service health status matching GET /health: kafka, redis, pg connection status, " +
            "ws_connections active, sessions_active_gauge, quorum_failures_total, token_issued_total. " +
            "Used by Kubernetes liveness/readiness probes and ops dashboards.",
            p("auth_token", "string", "Bearer JWT (ADMIN or no auth for k8s probe)"),
            p("tenant_id",  "string", "Tenant identifier for scoped metrics")
        ));

        tools.add(t("configure_track_plan",
            "Configure a tenant's assessment track execution plan for a specific role/job type. " +
            "Stores in PostgreSQL ecoskiller.assessment_cycle_config and caches in Redis (TTL 10 min). " +
            "Determines which tracks run (GD|INTERVIEW|DOJO) and in what order. ADMIN only.",
            p("auth_token",  "string", "Bearer JWT (ADMIN)"),
            p("tenant_id",   "string", "Tenant identifier"),
            p("role_type",   "string", "Job role type (e.g. TECHNICAL, MANAGEMENT, SALES)"),
            p("tracks",      "string", "Ordered track list: GD,DOJO or GD,INTERVIEW etc."),
            p("gd_speaking_seconds",     "string", "Override SPEAKING phase duration (default 90)"),
            p("gd_open_discussion_seconds","string","Override OPEN_DISCUSSION duration (default 300)"),
            p("quorum_required",         "string", "Min participants for GD sessions")
        ));

        return tools;
    }

    // ── Schema helpers ────────────────────────────────────────────────────────

    private Map<String, Object> t(String name, String description, Map<String, Object>... props) {
        Map<String, Object> tool = new LinkedHashMap<>();
        tool.put("name", name);
        tool.put("description", description);
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        Map<String, Object> properties = new LinkedHashMap<>();
        List<String> required = new ArrayList<>(Arrays.asList("auth_token", "tenant_id"));
        for (Map<String, Object> p : props) {
            String pName = (String) p.remove("_name");
            properties.put(pName, p);
        }
        schema.put("properties", properties);
        schema.put("required", required);
        tool.put("inputSchema", schema);
        return tool;
    }

    private Map<String, Object> p(String name, String type, String description) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("_name", name); m.put("type", type); m.put("description", description);
        return m;
    }
}
