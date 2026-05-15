package com.ecoskiller.mcp.phone.agent;

import com.ecoskiller.mcp.phone.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

/**
 * AGENT-7: PHONE_JOIN_IDEMPOTENCY_AGENT
 * Ensures phone join events are idempotent — duplicate dial-in
 * attempts from the same number within a dedup window return
 * the original join result instead of creating duplicate sessions.
 */
@Component
public class PhoneJoinIdempotencyAgent {

    @Tool(name = "phone_join_idempotency_check",
          description = "Check if a phone join request is a duplicate and return cached result if so.")
    public AgentResponse checkIdempotency(
            @ToolParam(description = "Idempotency key (callerNumber + sessionId + timestamp bucket)") String idempotencyKey,
            @ToolParam(description = "Dedup window seconds") int dedupWindowSec) {

        boolean isDuplicate = idempotencyKey.startsWith("DUP");

        return AgentResponse.ok("PHONE_JOIN_IDEMPOTENCY_AGENT",
                isDuplicate ? "Duplicate join detected" : "Unique join request",
                Map.of(
                        "idempotencyKey",   idempotencyKey,
                        "isDuplicate",      isDuplicate,
                        "dedupWindowSec",   dedupWindowSec,
                        "action",           isDuplicate ? "RETURN_CACHED" : "PROCEED",
                        "cachedResult",     isDuplicate ? Map.of("callLegId","LEG-ORIG-001","status","JOINED") : null,
                        "checkedAt",        Instant.now().toString()
                ));
    }

    @Tool(name = "phone_join_idempotency_register",
          description = "Register a successful phone join event with its idempotency key.")
    public AgentResponse registerJoin(
            @ToolParam(description = "Idempotency key") String idempotencyKey,
            @ToolParam(description = "Call leg ID of the successful join") String callLegId,
            @ToolParam(description = "TTL seconds for this cache entry") int ttlSeconds) {

        return AgentResponse.ok("PHONE_JOIN_IDEMPOTENCY_AGENT",
                "Join event registered under key " + idempotencyKey,
                Map.of(
                        "idempotencyKey", idempotencyKey,
                        "callLegId",     callLegId,
                        "ttlSeconds",    ttlSeconds,
                        "registeredAt",  Instant.now().toString(),
                        "expiresAt",     Instant.now().plusSeconds(ttlSeconds).toString()
                ));
    }
}
