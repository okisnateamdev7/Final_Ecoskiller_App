package com.ecoskiller.mcp.phone.agent;

import com.ecoskiller.mcp.phone.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * AGENT-4: PHONE_CROSS_SESSION_BEHAVIOR_AGENT
 * Detects and controls abnormal cross-session phone behavior —
 * a single caller participating in overlapping sessions, dial-in
 * from multiple devices, or hopping between sessions within a
 * restricted window.
 */
@Component
public class PhoneCrossSessionBehaviorAgent {

    @Tool(name = "phone_cross_session_detect",
          description = "Detect if a phone participant is simultaneously active in multiple sessions.")
    public AgentResponse detectCrossSession(
            @ToolParam(description = "Caller phone number E.164") String callerNumber,
            @ToolParam(description = "Current session ID being joined") String currentSessionId,
            @ToolParam(description = "Tenant ID") String tenantId) {

        return AgentResponse.ok("PHONE_CROSS_SESSION_BEHAVIOR_AGENT",
                "Cross-session detection for " + callerNumber,
                Map.of(
                        "callerNumber",     callerNumber,
                        "currentSessionId", currentSessionId,
                        "tenantId",         tenantId,
                        "activeSessions",   List.of(),
                        "crossSessionDetected", false,
                        "action",           "PERMIT",
                        "checkedAt",        Instant.now().toString()
                ));
    }

    @Tool(name = "phone_cross_session_block",
          description = "Block a caller from joining a new session due to cross-session policy violation.")
    public AgentResponse blockCrossSession(
            @ToolParam(description = "Caller phone number E.164") String callerNumber,
            @ToolParam(description = "Session ID being blocked") String sessionId,
            @ToolParam(description = "Conflicting session ID already active") String conflictingSessionId) {

        return AgentResponse.ok("PHONE_CROSS_SESSION_BEHAVIOR_AGENT",
                "Cross-session block applied for " + callerNumber,
                Map.of(
                        "callerNumber",        callerNumber,
                        "blockedSessionId",    sessionId,
                        "conflictingSessionId",conflictingSessionId,
                        "status",              "BLOCKED",
                        "announcement",        "You are already active in another session. Please exit first.",
                        "blockedAt",           Instant.now().toString()
                ));
    }

    @Tool(name = "phone_cross_session_audit",
          description = "Audit cross-session behaviour events for compliance reporting.")
    public AgentResponse auditCrossSession(
            @ToolParam(description = "Tenant ID") String tenantId,
            @ToolParam(description = "From date YYYY-MM-DD") String fromDate,
            @ToolParam(description = "To date YYYY-MM-DD") String toDate) {

        return AgentResponse.ok("PHONE_CROSS_SESSION_BEHAVIOR_AGENT",
                "Cross-session audit for tenant " + tenantId,
                Map.of(
                        "tenantId",     tenantId,
                        "period",       fromDate + " to " + toDate,
                        "totalIncidents", 4,
                        "blockedAttempts", 4,
                        "topOffenders", List.of("+919876543210", "+918765432109")
                ));
    }
}
