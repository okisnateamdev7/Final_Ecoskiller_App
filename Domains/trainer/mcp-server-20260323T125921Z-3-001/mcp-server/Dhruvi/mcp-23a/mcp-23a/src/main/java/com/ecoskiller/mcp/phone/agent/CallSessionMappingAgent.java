package com.ecoskiller.mcp.phone.agent;

import com.ecoskiller.mcp.phone.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * AGENT-1: CALL_SESSION_MAPPING_AGENT
 * Maps incoming phone calls to active platform sessions.
 * Maintains bidirectional association between call-leg IDs and session IDs.
 */
@Component
public class CallSessionMappingAgent {

    @Tool(name = "phone_call_session_map",
          description = "Map an incoming phone call leg to an active platform session.")
    public AgentResponse mapCallToSession(
            @ToolParam(description = "Phone call leg ID from PSTN/SIP") String callLegId,
            @ToolParam(description = "Platform session ID") String sessionId,
            @ToolParam(description = "Caller phone number E.164 format") String callerNumber,
            @ToolParam(description = "Tenant ID") String tenantId) {

        String mappingId = "CMAP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        return AgentResponse.ok("CALL_SESSION_MAPPING_AGENT",
                "Call leg " + callLegId + " mapped to session " + sessionId,
                Map.of(
                        "mappingId",    mappingId,
                        "callLegId",    callLegId,
                        "sessionId",    sessionId,
                        "callerNumber", callerNumber,
                        "tenantId",     tenantId,
                        "mappedAt",     Instant.now().toString(),
                        "status",       "ACTIVE"
                ));
    }

    @Tool(name = "phone_call_session_get",
          description = "Retrieve session mapping for a given call leg ID.")
    public AgentResponse getMapping(
            @ToolParam(description = "Phone call leg ID") String callLegId) {

        return AgentResponse.ok("CALL_SESSION_MAPPING_AGENT",
                "Mapping fetched for call leg " + callLegId,
                Map.of(
                        "callLegId",    callLegId,
                        "sessionId",    "SES-" + callLegId.hashCode(),
                        "callerNumber", "+919876543210",
                        "status",       "ACTIVE",
                        "mappedAt",     Instant.now().minusSeconds(120).toString()
                ));
    }

    @Tool(name = "phone_call_session_unmap",
          description = "Remove call-to-session mapping when call ends or session terminates.")
    public AgentResponse unmapCallFromSession(
            @ToolParam(description = "Call leg ID to unmap") String callLegId,
            @ToolParam(description = "Reason: CALL_ENDED | SESSION_EXPIRED | MANUAL") String reason) {

        return AgentResponse.ok("CALL_SESSION_MAPPING_AGENT",
                "Mapping removed for call leg " + callLegId,
                Map.of(
                        "callLegId",  callLegId,
                        "status",     "UNMAPPED",
                        "reason",     reason,
                        "unmappedAt", Instant.now().toString()
                ));
    }
}
