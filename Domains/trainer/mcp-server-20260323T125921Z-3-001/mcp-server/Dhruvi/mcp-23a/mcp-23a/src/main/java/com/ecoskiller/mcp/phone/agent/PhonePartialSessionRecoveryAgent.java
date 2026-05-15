package com.ecoskiller.mcp.phone.agent;

import com.ecoskiller.mcp.phone.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * AGENT-3: PHONE_PARTIAL_SESSION_RECOVERY_AGENT
 * Recovers partially completed phone sessions after call drops,
 * network failures, or bridge disconnections. Restores state
 * and re-links the caller to the correct session checkpoint.
 */
@Component
public class PhonePartialSessionRecoveryAgent {

    @Tool(name = "phone_session_recover",
          description = "Attempt recovery of a dropped phone session and restore caller state.")
    public AgentResponse recoverSession(
            @ToolParam(description = "Original session ID") String sessionId,
            @ToolParam(description = "Caller phone number E.164") String callerNumber,
            @ToolParam(description = "Drop reason: NETWORK | BRIDGE_FAILURE | USER_DROP | TIMEOUT") String dropReason,
            @ToolParam(description = "Recovery window seconds (max 300)") int recoveryWindowSec) {

        boolean recoverable = recoveryWindowSec <= 300 && !dropReason.equalsIgnoreCase("USER_DROP");

        return AgentResponse.ok("PHONE_PARTIAL_SESSION_RECOVERY_AGENT",
                "Recovery " + (recoverable ? "initiated" : "not possible") + " for session " + sessionId,
                Map.of(
                        "sessionId",        sessionId,
                        "callerNumber",     callerNumber,
                        "dropReason",       dropReason,
                        "recoverable",      recoverable,
                        "recoveryToken",    recoverable ? "RTOK-" + System.currentTimeMillis() : null,
                        "checkpointAt",     recoverable ? "AUDIO_STREAM_POSITION_00:12:34" : null,
                        "expiresInSec",     recoverable ? recoveryWindowSec : 0,
                        "status",           recoverable ? "RECOVERY_PENDING" : "NON_RECOVERABLE",
                        "initiatedAt",      Instant.now().toString()
                ));
    }

    @Tool(name = "phone_session_recovery_status",
          description = "Check the recovery status of a previously dropped phone session.")
    public AgentResponse getRecoveryStatus(
            @ToolParam(description = "Recovery token from recover call") String recoveryToken) {

        return AgentResponse.ok("PHONE_PARTIAL_SESSION_RECOVERY_AGENT",
                "Recovery status for token " + recoveryToken,
                Map.of(
                        "recoveryToken",    recoveryToken,
                        "status",           "RECOVERED",
                        "restoredAt",       Instant.now().toString(),
                        "checkpointUsed",   "AUDIO_STREAM_POSITION_00:12:34",
                        "callLegId",        "LEG-" + recoveryToken.hashCode(),
                        "sessionResumed",   true
                ));
    }

    @Tool(name = "phone_session_recovery_list",
          description = "List recent partial session recovery events for a tenant.")
    public AgentResponse listRecoveryEvents(
            @ToolParam(description = "Tenant ID") String tenantId,
            @ToolParam(description = "Last N events (max 50)") int lastN) {

        return AgentResponse.ok("PHONE_PARTIAL_SESSION_RECOVERY_AGENT",
                "Recovery events for tenant " + tenantId,
                Map.of(
                        "tenantId",   tenantId,
                        "totalEvents", 3,
                        "events", List.of(
                                Map.of("token","RTOK-001","status","RECOVERED",  "dropReason","NETWORK",       "at","2025-03-01T08:10:00Z"),
                                Map.of("token","RTOK-002","status","EXPIRED",    "dropReason","BRIDGE_FAILURE","at","2025-03-02T11:22:00Z"),
                                Map.of("token","RTOK-003","status","RECOVERED",  "dropReason","TIMEOUT",       "at","2025-03-03T14:05:00Z")
                        )
                ));
    }
}
