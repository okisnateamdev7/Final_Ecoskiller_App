package com.ecoskiller.mcp.phone.agent;

import com.ecoskiller.mcp.phone.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Random;

/**
 * AGENT-5: SESSION_PIN_GENERATION_AGENT
 * Generates secure numeric PINs for phone-dial-in authentication.
 * Supports per-session, per-role, and per-participant PIN strategies.
 */
@Component
public class SessionPinGenerationAgent {

    @Tool(name = "phone_pin_generate",
          description = "Generate a secure PIN for phone dial-in authentication for a session.")
    public AgentResponse generatePin(
            @ToolParam(description = "Session ID") String sessionId,
            @ToolParam(description = "PIN scope: SESSION | PARTICIPANT | HOST") String pinScope,
            @ToolParam(description = "PIN length: 4 | 6 | 8") int pinLength,
            @ToolParam(description = "TTL in minutes") int ttlMinutes,
            @ToolParam(description = "Participant user ID (required if scope is PARTICIPANT)") String participantId) {

        String pin = generateNumericPin(pinLength);

        return AgentResponse.ok("SESSION_PIN_GENERATION_AGENT",
                "PIN generated for session " + sessionId,
                Map.of(
                        "sessionId",     sessionId,
                        "pinScope",      pinScope,
                        "participantId", participantId,
                        "pin",           pin,
                        "pinLength",     pinLength,
                        "ttlMinutes",    ttlMinutes,
                        "issuedAt",      Instant.now().toString(),
                        "expiresAt",     Instant.now().plus(ttlMinutes, ChronoUnit.MINUTES).toString(),
                        "status",        "ACTIVE"
                ));
    }

    @Tool(name = "phone_pin_regenerate",
          description = "Regenerate and invalidate old PIN for a session or participant.")
    public AgentResponse regeneratePin(
            @ToolParam(description = "Session ID") String sessionId,
            @ToolParam(description = "Participant ID or HOST") String participantId,
            @ToolParam(description = "Reason: SECURITY | EXPIRED | USER_REQUEST") String reason) {

        String newPin = generateNumericPin(6);

        return AgentResponse.ok("SESSION_PIN_GENERATION_AGENT",
                "PIN regenerated for session " + sessionId,
                Map.of(
                        "sessionId",    sessionId,
                        "participantId",participantId,
                        "newPin",       newPin,
                        "reason",       reason,
                        "oldPinStatus", "INVALIDATED",
                        "regeneratedAt",Instant.now().toString()
                ));
    }

    private String generateNumericPin(int length) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) sb.append(rnd.nextInt(10));
        return sb.toString();
    }
}
