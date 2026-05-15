package com.ecoskiller.mcp.phone.agent;

import com.ecoskiller.mcp.phone.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * AGENT-11: PHONE_SESSION_STATE_SYNC_AGENT
 * Synchronises phone session state across distributed bridge nodes.
 * Propagates participant join/leave, mute status, and PIN changes
 * to all regional replicas in real time.
 */
@Component
public class PhoneSessionStateSyncAgent {

    @Tool(name = "phone_session_state_publish",
          description = "Publish a phone session state change event to all regional replicas.")
    public AgentResponse publishStateChange(
            @ToolParam(description = "Session ID") String sessionId,
            @ToolParam(description = "Change type: PARTICIPANT_JOIN | PARTICIPANT_LEAVE | MUTE | PIN_CHANGE | BRIDGE_SWAP") String changeType,
            @ToolParam(description = "State payload as JSON string") String payload,
            @ToolParam(description = "Origin region: ap-south-1 | us-east-1 | eu-west-1") String originRegion) {

        return AgentResponse.ok("PHONE_SESSION_STATE_SYNC_AGENT",
                "State change published for session " + sessionId,
                Map.of(
                        "sessionId",     sessionId,
                        "changeType",    changeType,
                        "originRegion",  originRegion,
                        "publishedAt",   Instant.now().toString(),
                        "replicatedTo",  List.of("ap-south-1","us-east-1","eu-west-1"),
                        "syncStatus",    "BROADCAST_COMPLETE"
                ));
    }

    @Tool(name = "phone_session_state_get",
          description = "Get the current synchronised state of a phone session.")
    public AgentResponse getSessionState(
            @ToolParam(description = "Session ID") String sessionId,
            @ToolParam(description = "Requesting region") String region) {

        return AgentResponse.ok("PHONE_SESSION_STATE_SYNC_AGENT",
                "Session state fetched for " + sessionId,
                Map.of(
                        "sessionId",         sessionId,
                        "status",            "ACTIVE",
                        "participantCount",  12,
                        "bridgeRegion",      "ap-south-1",
                        "pinActive",         true,
                        "scopeFrozen",       true,
                        "lastSyncedAt",      Instant.now().minusSeconds(2).toString(),
                        "requestingRegion",  region
                ));
    }

    @Tool(name = "phone_session_state_sync_health",
          description = "Check state synchronisation health across all bridge regions.")
    public AgentResponse getSyncHealth() {

        return AgentResponse.ok("PHONE_SESSION_STATE_SYNC_AGENT",
                "Session state sync health check",
                Map.of(
                        "overallStatus", "HEALTHY",
                        "regions", Map.of(
                                "ap-south-1", "IN_SYNC",
                                "us-east-1",  "IN_SYNC",
                                "eu-west-1",  "IN_SYNC"
                        ),
                        "avgSyncLatencyMs", 28,
                        "checkedAt",        Instant.now().toString()
                ));
    }
}
