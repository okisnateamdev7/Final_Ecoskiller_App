package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.*;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.*;

import java.time.Instant;
import java.util.*;

/** Tools: control_session, handle_raise_hand, handle_participant_event, process_kafka_event */
public class SessionControlTools {

    private final OrchestratorRepository repo  = OrchestratorRepository.getInstance();
    private final KafkaEventPublisher    kafka = KafkaEventPublisher.getInstance();

    // ─────────────────────────────────────────────────────────────────────────
    // control_session  (ecoskiller:session:moderator)
    // ─────────────────────────────────────────────────────────────────────────

    public String controlSession(Map<String, Object> args) {
        JwtClaims claims  = auth(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_MODERATOR, SecurityConfig.ROLE_ADMIN);
        String tenantId   = SecurityConfig.effectiveTenantId(claims, args);
        String sessionId  = SecurityConfig.requireString(args, "session_id");
        String action     = SecurityConfig.requireString(args, "action").toUpperCase();
        String targetCand = SecurityConfig.getString(args, "target_candidate_id");

        AssessmentSession session = repo.findSession(tenantId, sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        AssessmentSession.SessionState state = session.getState();
        String wsCommand;
        Map<String, Object> wsPayload = new LinkedHashMap<>();
        wsPayload.put("issued_by", claims.getUserId());
        wsPayload.put("timestamp", Instant.now().toString());

        switch (action) {
            case "FORCE_MUTE":
                if (state == AssessmentSession.SessionState.COMPLETED ||
                    state == AssessmentSession.SessionState.CANCELLED)
                    throw new IllegalStateException("Cannot FORCE_MUTE in state: " + state);
                wsCommand = "MUTE_COMMAND";
                wsPayload.put("action", "MUTE");
                wsPayload.put("reason", "MODERATOR_FORCE_MUTE");
                if (targetCand != null) wsPayload.put("target_candidate_id", targetCand);
                break;

            case "FORCE_UNMUTE":
                if (state != AssessmentSession.SessionState.SPEAKING &&
                    state != AssessmentSession.SessionState.OPEN_DISCUSSION)
                    throw new IllegalStateException("FORCE_UNMUTE only valid in SPEAKING or OPEN_DISCUSSION state");
                wsCommand = "MUTE_COMMAND";
                wsPayload.put("action", "UNMUTE");
                wsPayload.put("reason", "MODERATOR_FORCE_UNMUTE");
                if (targetCand != null) wsPayload.put("target_candidate_id", targetCand);
                break;

            case "ADVANCE_TURN":
                if (state != AssessmentSession.SessionState.SPEAKING)
                    throw new IllegalStateException("ADVANCE_TURN only valid during SPEAKING state");
                session.advanceTurn(claims.getUserId());
                repo.saveSession(session);
                wsCommand = "STATE_CHANGE";
                wsPayload.put("new_state",     session.getState().name());
                wsPayload.put("current_speaker",session.getCurrentSpeakerId());
                wsPayload.put("reason",        "MODERATOR_ADVANCE_TURN");
                break;

            case "END_SESSION":
                if (state == AssessmentSession.SessionState.COMPLETED ||
                    state == AssessmentSession.SessionState.CANCELLED)
                    throw new IllegalStateException("Session already in terminal state: " + state);
                session.complete(claims.getUserId());
                repo.saveSession(session);
                repo.incCompleted();
                repo.writeAudit(session);
                wsCommand = "SESSION_END";
                wsPayload.put("session_id",         sessionId);
                wsPayload.put("completion_status",  "MODERATOR_ENDED");
                wsPayload.put("redirect_url",       "/dashboard/results/" + sessionId);
                emitCompletionKafka(session);
                break;

            default:
                throw new IllegalArgumentException("Invalid action: " + action
                        + ". Valid: FORCE_MUTE | FORCE_UNMUTE | ADVANCE_TURN | END_SESSION");
        }

        repo.logWsCommand(sessionId, wsCommand, wsPayload);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("session_id",   sessionId);
        data.put("action",       action);
        data.put("ws_command",   wsCommand);
        data.put("ws_payload",   wsPayload);
        data.put("session_state",session.getState().name());
        return resp("ACCEPTED", "Control action queued: " + action, data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // handle_raise_hand
    // ─────────────────────────────────────────────────────────────────────────

    public String handleRaiseHand(Map<String, Object> args) {
        JwtClaims claims  = auth(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_CANDIDATE, SecurityConfig.ROLE_ADMIN);
        String tenantId   = SecurityConfig.effectiveTenantId(claims, args);
        String sessionId  = SecurityConfig.requireString(args, "session_id");
        String candidateId= SecurityConfig.requireString(args, "candidate_id");

        // CANDIDATE must be their own raise hand
        if (claims.hasRole(SecurityConfig.ROLE_CANDIDATE) && !claims.getUserId().equals(candidateId))
            throw new SecurityException("CANDIDATE can only raise their own hand");

        AssessmentSession session = repo.findSession(tenantId, sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        int position = session.raiseHand(candidateId); // throws if not OPEN_DISCUSSION
        long estimatedWait = position * 60L; // rough 60s per person ahead

        repo.saveSession(session);

        // Broadcast queue update to all participants
        repo.logWsCommand(sessionId, "RAISE_HAND_ACK", Map.of(
                "candidate_id",          candidateId,
                "position_in_queue",     position,
                "estimated_wait_seconds",estimatedWait,
                "queue_size",            session.getRaiseHandQueue().size()));

        repo.logWsCommand(sessionId, "QUEUE_UPDATED", Map.of(
                "raise_hand_queue",session.getRaiseHandQueue(),
                "queue_size",      session.getRaiseHandQueue().size()));

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("session_id",           sessionId);
        data.put("candidate_id",         candidateId);
        data.put("position_in_queue",    position);
        data.put("estimated_wait_seconds",estimatedWait);
        data.put("raise_hand_queue",     session.getRaiseHandQueue());
        return resp("OK", "Hand raised — position " + position + " in queue", data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // handle_participant_event
    // ─────────────────────────────────────────────────────────────────────────

    public String handleParticipantEvent(Map<String, Object> args) {
        JwtClaims claims  = auth(args);
        String tenantId   = SecurityConfig.effectiveTenantId(claims, args);
        String sessionId  = SecurityConfig.requireString(args, "session_id");
        String eventType  = SecurityConfig.requireString(args, "event_type").toUpperCase();
        String candidateId= SecurityConfig.requireString(args, "candidate_id");
        String clientTs   = SecurityConfig.getString(args, "client_ts");

        AssessmentSession session = repo.findSession(tenantId, sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("session_id",    sessionId);
        result.put("candidate_id",  candidateId);
        result.put("event_type",    eventType);

        switch (eventType) {
            case "HEARTBEAT":
                // Update last_seen Redis key (simulated)
                result.put("last_seen",      Instant.now().toString());
                result.put("client_ts",      clientTs);
                result.put("server_ts",      Instant.now().toString());
                result.put("session_state",  session.getState().name());
                result.put("timer_remaining",session.getPhaseRemainingSeconds());
                break;

            case "LEAVE_SESSION":
                session.participantLeft(candidateId);
                repo.saveSession(session);
                repo.decActiveWs();
                // If this was the speaker → broadcast ADVANCE_TURN
                if (candidateId.equals(session.getCurrentSpeakerId())) {
                    session.advanceTurn("SPEAKER_LEAVE");
                    repo.saveSession(session);
                    repo.logWsCommand(sessionId, "STATE_CHANGE", Map.of(
                            "reason", "SPEAKER_LEAVE",
                            "new_state", session.getState().name(),
                            "current_speaker", String.valueOf(session.getCurrentSpeakerId())));
                }
                result.put("roster_status",       session.getRosterStatus().get(candidateId));
                result.put("participant_count",   session.getParticipantCount());
                break;

            case "PARTICIPANT_JOINED":
                session.participantJoined(candidateId);
                repo.saveSession(session);
                repo.incActiveWs();
                repo.logWsCommand(sessionId, "PARTICIPANT_JOINED", Map.of(
                        "candidate_id",     candidateId,
                        "participant_count",session.getParticipantCount()));
                result.put("participant_count",  session.getParticipantCount());
                result.put("quorum_met",         session.hasQuorum());
                break;

            case "SPEAKER_DISCONNECT":
                // 10-second grace window — in production Redis TTL handles this
                repo.logWsCommand(sessionId, "SPEAKER_DISCONNECT_GRACE", Map.of(
                        "candidate_id",     candidateId,
                        "grace_window_sec", 10,
                        "message",          "Auto-advance after 10s if not reconnected"));
                result.put("grace_window_seconds", 10);
                result.put("current_speaker",      session.getCurrentSpeakerId());
                result.put("action",               "GRACE_WINDOW_OPENED");
                break;

            default:
                throw new IllegalArgumentException("Unknown event_type: " + eventType
                        + ". Valid: HEARTBEAT | LEAVE_SESSION | PARTICIPANT_JOINED | SPEAKER_DISCONNECT");
        }

        return resp("OK", "Participant event handled: " + eventType, result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // process_kafka_event
    // ─────────────────────────────────────────────────────────────────────────

    public String processKafkaEvent(Map<String, Object> args) {
        JwtClaims claims   = auth(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_SERVICE_ACCOUNT, SecurityConfig.ROLE_ADMIN);
        String tenantId    = SecurityConfig.effectiveTenantId(claims, args);
        String eventType   = SecurityConfig.requireString(args, "event_type");
        String payloadJson = SecurityConfig.requireString(args, "event_payload");
        int retryCount     = SecurityConfig.getInt(args, "retry_count", 0);

        // DLQ check: max 3 retries
        if (retryCount >= 3) {
            Map<String, Object> dlqData = new LinkedHashMap<>();
            dlqData.put("event_type",  eventType);
            dlqData.put("retry_count", retryCount);
            dlqData.put("dlq_topic",   eventType + ".dlq");
            dlqData.put("action",      "PUBLISHED_TO_DLQ");
            repo.logKafkaEvent(eventType + ".dlq", dlqData);
            return resp("DLQ", "Event forwarded to DLQ after " + retryCount + " retries", dlqData);
        }

        // Parse payload (simple key extraction)
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("event_type",   eventType);
        result.put("tenant_id",    tenantId);
        result.put("retry_count",  retryCount);

        switch (eventType) {
            case "assessment.cycle.created":
                result.put("action", "CYCLE_INITIALIZATION_TRIGGERED");
                result.put("note",   "Use create_assessment_cycle tool with parsed payload fields");
                break;
            case "interview.session.requested":
                result.put("action", "INTERVIEW_SESSION_SCHEDULING_TRIGGERED");
                result.put("note",   "Use create_session tool with track_type=INTERVIEW");
                break;
            case "dojo.match.requested":
                result.put("action", "DOJO_SESSION_SCHEDULING_TRIGGERED");
                result.put("note",   "Use create_session tool with track_type=DOJO");
                break;
            case "assessment.cycle.cancelled":
                result.put("action", "CYCLE_CANCELLATION_TRIGGERED");
                result.put("note",   "Use cancel_cycle tool with reason=MANUAL");
                break;
            default:
                throw new IllegalArgumentException("Unknown Kafka event_type: " + eventType);
        }

        result.put("payload_received",  payloadJson.length() + " chars");
        result.put("processed_at",      Instant.now().toString());
        result.put("max_retries",       3);
        result.put("next_retry_backoff","exponential: " + (long)(1000 * Math.pow(2, retryCount)) + "ms");

        return resp("OK", "Kafka event processed: " + eventType, result);
    }

    // ─ helpers ───────────────────────────────────────────────────────────────

    private void emitCompletionKafka(AssessmentSession session) {
        if (!SecurityConfig.checkIdempotency(session.getIdempotencyKey())) return;
        session.markIdempotencyKeyWritten();
        if (session.getTrackType() == AssessmentCycle.TrackType.GD)
            kafka.publishGdCompleted(session);
        else if (session.getTrackType() == AssessmentCycle.TrackType.INTERVIEW)
            kafka.publishInterviewCompleted(session);
    }

    private JwtClaims auth(Map<String, Object> args) {
        return SecurityConfig.validateToken(SecurityConfig.requireString(args, "auth_token"));
    }

    private String resp(String status, String message, Object data) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("status", status); r.put("message", message); r.put("data", data);
        r.put("service", "assessment-orchestrator");
        return JsonUtil.toJson(r);
    }
}
