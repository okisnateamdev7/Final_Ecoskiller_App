package com.ecoskiller.mcp.phone.agent;

import com.ecoskiller.mcp.phone.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

/**
 * AGENT-2: PHONE_LATE_JOIN_POLICY_AGENT
 * Enforces late-join rules for phone participants attempting to join
 * an already-in-progress session. Applies grace windows and hard cutoffs.
 */
@Component
public class PhoneLateJoinPolicyAgent {

    @Tool(name = "phone_late_join_evaluate",
          description = "Evaluate whether a late-joining phone participant is permitted to join an active session.")
    public AgentResponse evaluateLateJoin(
            @ToolParam(description = "Session ID") String sessionId,
            @ToolParam(description = "Caller phone number E.164") String callerNumber,
            @ToolParam(description = "Session elapsed minutes since start") int elapsedMinutes,
            @ToolParam(description = "Session type: EXAM | LECTURE | COMPETITION | MEETING") String sessionType) {

        int gracePeriod = switch (sessionType.toUpperCase()) {
            case "EXAM"        -> 10;
            case "COMPETITION" -> 5;
            case "LECTURE"     -> 20;
            default            -> 15;
        };

        boolean permitted = elapsedMinutes <= gracePeriod;

        return AgentResponse.ok("PHONE_LATE_JOIN_POLICY_AGENT",
                "Late join evaluation: " + (permitted ? "PERMITTED" : "DENIED"),
                Map.of(
                        "sessionId",      sessionId,
                        "callerNumber",   callerNumber,
                        "elapsedMinutes", elapsedMinutes,
                        "sessionType",    sessionType,
                        "gracePeriodMin", gracePeriod,
                        "permitted",      permitted,
                        "denyReason",     permitted ? null : "Grace period of " + gracePeriod + " min exceeded",
                        "evaluatedAt",    Instant.now().toString()
                ));
    }

    @Tool(name = "phone_late_join_policy_get",
          description = "Get configured late-join grace window and policy for a session type.")
    public AgentResponse getPolicy(
            @ToolParam(description = "Session type: EXAM | LECTURE | COMPETITION | MEETING") String sessionType) {

        int gracePeriod = switch (sessionType.toUpperCase()) {
            case "EXAM"        -> 10;
            case "COMPETITION" -> 5;
            case "LECTURE"     -> 20;
            default            -> 15;
        };

        return AgentResponse.ok("PHONE_LATE_JOIN_POLICY_AGENT",
                "Late-join policy for " + sessionType,
                Map.of(
                        "sessionType",     sessionType,
                        "gracePeriodMin",  gracePeriod,
                        "action",          "DENY_AFTER_GRACE",
                        "playAnnouncement",true,
                        "notifyHost",      true
                ));
    }
}
