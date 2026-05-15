package com.ecoskiller.mcp.session.tools;

import com.ecoskiller.mcp.session.client.SessionControlClient;
import com.ecoskiller.mcp.session.config.SessionControlConfig;
import com.ecoskiller.mcp.session.security.AuditLogger;

import java.util.*;

/**
 * Central registry for all 24 Session Control MCP tools.
 *
 * Categories:
 *   Session Lifecycle        (6): session_create, session_get, session_list,
 *                                 session_update_state, session_extend, session_archive
 *   Participant Management   (6): participant_join, participant_leave, participant_list,
 *                                 participant_heartbeat, participant_evict, participant_update_role
 *   Jitsi Room Management    (3): jitsi_get_token, jitsi_mute, jitsi_force_end
 *   Audit & Compliance       (3): session_audit_log, session_events, session_export
 *   State Machine Shortcuts  (4): session_start, session_suspend, session_resume, session_terminate
 *   Health & Monitoring      (2): service_health, session_stats
 *                          ─────
 *   Total                   24 tools
 */
public class ToolRegistry {

    private final Map<String, McpTool> tools = new LinkedHashMap<>();

    public ToolRegistry(SessionControlClient client, SessionControlConfig cfg, AuditLogger audit) {

        // ── Session Lifecycle ──────────────────────────────────────────────────
        register(new SessionCreateTool(client, cfg, audit));
        register(new SessionGetTool(client, cfg, audit));
        register(new SessionListTool(client, cfg, audit));
        register(new SessionUpdateStateTool(client, cfg, audit));
        register(new SessionExtendTool(client, cfg, audit));
        register(new SessionArchiveTool(client, cfg, audit));

        // ── Participant Management ────────────────────────────────────────────
        register(new ParticipantJoinTool(client, cfg, audit));
        register(new ParticipantLeaveTool(client, cfg, audit));
        register(new ParticipantListTool(client, cfg, audit));
        register(new ParticipantHeartbeatTool(client, cfg, audit));
        register(new ParticipantEvictTool(client, cfg, audit));
        register(new ParticipantUpdateRoleTool(client, cfg, audit));

        // ── Jitsi Room Management ─────────────────────────────────────────────
        register(new JitsiGetTokenTool(client, cfg, audit));
        register(new JitsiMuteTool(client, cfg, audit));
        register(new JitsiForceEndTool(client, cfg, audit));

        // ── Audit & Compliance ────────────────────────────────────────────────
        register(new SessionAuditLogTool(client, cfg, audit));
        register(new SessionEventsTool(client, cfg, audit));
        register(new SessionExportTool(client, cfg, audit));

        // ── State Machine Shortcuts ───────────────────────────────────────────
        register(new SessionStartTool(client, cfg, audit));
        register(new SessionSuspendTool(client, cfg, audit));
        register(new SessionResumeTool(client, cfg, audit));
        register(new SessionTerminateTool(client, cfg, audit));

        // ── Health & Monitoring ───────────────────────────────────────────────
        register(new ServiceHealthTool(client, cfg, audit));
        register(new SessionStatsTool(client, cfg, audit));
    }

    private void register(McpTool t) { tools.put(t.name(), t); }
    public McpTool find(String name)           { return tools.get(name); }
    public Collection<McpTool> allTools()      { return Collections.unmodifiableCollection(tools.values()); }
    public int size()                          { return tools.size(); }
}
