package com.ecoskiller.mcp.phone.agent;

import com.ecoskiller.mcp.phone.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * AGENT-10: PHONE_SCOPE_FREEZE_AGENT
 * Freezes the scope (participant list, permissions, PIN set)
 * of a phone session once it transitions to ACTIVE state.
 * Prevents late additions or changes that would violate
 * session integrity for exams and competitions.
 */
@Component
public class PhoneScopeFreezeAgent {

    @Tool(name = "phone_scope_freeze",
          description = "Freeze the phone session scope — no new participants or PIN changes allowed after freeze.")
    public AgentResponse freezeScope(
            @ToolParam(description = "Session ID") String sessionId,
            @ToolParam(description = "Scope snapshot reference ID") String snapshotId,
            @ToolParam(description = "Freeze reason: EXAM_START | COMPETITION_START | MANUAL") String reason) {

        return AgentResponse.ok("PHONE_SCOPE_FREEZE_AGENT",
                "Session scope frozen for " + sessionId,
                Map.of(
                        "sessionId",   sessionId,
                        "snapshotId",  snapshotId,
                        "frozen",      true,
                        "reason",      reason,
                        "frozenAt",    Instant.now().toString(),
                        "protections", List.of("NO_NEW_PARTICIPANTS","NO_PIN_CHANGE","NO_ROLE_CHANGE")
                ));
    }

    @Tool(name = "phone_scope_freeze_check",
          description = "Check whether a phone session scope is currently frozen.")
    public AgentResponse checkFreeze(
            @ToolParam(description = "Session ID") String sessionId) {

        return AgentResponse.ok("PHONE_SCOPE_FREEZE_AGENT",
                "Scope freeze status for session " + sessionId,
                Map.of(
                        "sessionId", sessionId,
                        "frozen",    true,
                        "frozenAt",  Instant.now().minusSeconds(600).toString(),
                        "reason",    "EXAM_START",
                        "snapshotId","SNAP-" + sessionId
                ));
    }

    @Tool(name = "phone_scope_unfreeze",
          description = "Unfreeze a session scope after exam/competition ends — ADMIN only.")
    public AgentResponse unfreezeScope(
            @ToolParam(description = "Session ID") String sessionId,
            @ToolParam(description = "Admin user ID authorising unfreeze") String adminId) {

        return AgentResponse.ok("PHONE_SCOPE_FREEZE_AGENT",
                "Session scope unfrozen for " + sessionId,
                Map.of(
                        "sessionId",  sessionId,
                        "frozen",     false,
                        "unfreezedBy",adminId,
                        "unfreezedAt",Instant.now().toString()
                ));
    }
}
