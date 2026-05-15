package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.*;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/** Tools: create_session, join_session, get_session_state, transition_session_state, check_quorum */
public class SessionLifecycleTools {

    private final OrchestratorRepository repo  = OrchestratorRepository.getInstance();
    private final KafkaEventPublisher    kafka = KafkaEventPublisher.getInstance();

    // ─────────────────────────────────────────────────────────────────────────
    // create_session
    // ─────────────────────────────────────────────────────────────────────────

    public String createSession(Map<String, Object> args) {
        JwtClaims claims = auth(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_RECRUITER, SecurityConfig.ROLE_ADMIN,
                SecurityConfig.ROLE_SERVICE_ACCOUNT);
        String tenantId   = SecurityConfig.effectiveTenantId(claims, args);
        String cycleId    = SecurityConfig.requireString(args, "cycle_id");
        String trackStr   = SecurityConfig.requireString(args, "track_type");
        String candidateRaw = SecurityConfig.requireString(args, "candidate_ids");
        String scheduledStr = SecurityConfig.getString(args, "scheduled_at");

        AssessmentCycle.TrackType track;
        try { track = AssessmentCycle.TrackType.valueOf(trackStr.toUpperCase()); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("Invalid track_type: " + trackStr); }

        List<String> candidates = Arrays.stream(candidateRaw.split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        if (candidates.isEmpty()) throw new IllegalArgumentException("candidate_ids must not be empty");

        int quorum = SecurityConfig.getInt(args, "quorum_required",
                track == AssessmentCycle.TrackType.INTERVIEW ? 2 : 3);

        Instant scheduledAt;
        try { scheduledAt = scheduledStr != null ? Instant.parse(scheduledStr) : Instant.now().plusSeconds(3600); }
        catch (Exception e) { throw new IllegalArgumentException("Invalid scheduled_at: " + scheduledStr); }

        // Validate cycle exists for this tenant
        repo.findCycle(tenantId, cycleId)
                .orElseThrow(() -> new IllegalArgumentException("Cycle not found: " + cycleId));

        String sessionId = SecurityConfig.generateId();
        AssessmentSession session = new AssessmentSession(sessionId, cycleId, tenantId,
                track, candidates, scheduledAt, quorum);
        repo.saveSession(session);

        return resp("CREATED", "Session created with state SCHEDULED", session.toMap());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // join_session  (issues Jitsi JWT)
    // ─────────────────────────────────────────────────────────────────────────

    public String joinSession(Map<String, Object> args) {
        JwtClaims claims = auth(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_CANDIDATE, SecurityConfig.ROLE_RECRUITER,
                SecurityConfig.ROLE_MODERATOR, SecurityConfig.ROLE_ADMIN);
        String tenantId      = SecurityConfig.effectiveTenantId(claims, args);
        String sessionId     = SecurityConfig.requireString(args, "session_id");
        String displayName   = SecurityConfig.sanitise(SecurityConfig.getString(args, "display_name"), "display_name");
        String idempotencyKey = SecurityConfig.getString(args, "idempotency_key");

        AssessmentSession session = repo.findSession(tenantId, sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        // Session must be in WAITING or INTRO
        if (session.getState() != AssessmentSession.SessionState.WAITING &&
            session.getState() != AssessmentSession.SessionState.INTRO)
            throw new IllegalStateException("Cannot join session in state: " + session.getState()
                    + " (must be WAITING or INTRO)");

        // Idempotency — prevent double-join
        String idempKey = idempotencyKey != null ? idempotencyKey
                : "join_" + sessionId + "_" + claims.getUserId();
        if (!SecurityConfig.checkIdempotency(idempKey)) {
            return resp("DUPLICATE", "Already joined this session", Map.of(
                    "session_id", sessionId, "user_id", claims.getUserId()));
        }

        // Determine role: moderator for RECRUITER/MODERATOR/ADMIN, attendee for CANDIDATE
        boolean isModerator = claims.hasRole(SecurityConfig.ROLE_RECRUITER) ||
                              claims.hasRole(SecurityConfig.ROLE_MODERATOR) ||
                              claims.hasRole(SecurityConfig.ROLE_ADMIN);

        // Issue Jitsi token
        String dn = displayName != null ? displayName : claims.getUserId();
        Map<String, Object> tokenInfo = SecurityConfig.issueJitsiToken(sessionId, claims.getUserId(), dn, isModerator);
        repo.incTokens();

        // Update session participant count
        session.participantJoined(claims.getUserId());
        repo.saveSession(session);
        repo.incActiveWs();

        // Broadcast PARTICIPANT_JOINED via WS log
        repo.logWsCommand(sessionId, "PARTICIPANT_JOINED", Map.of(
                "candidate_id", claims.getUserId(),
                "display_name", dn,
                "participant_count", session.getParticipantCount(),
                "is_moderator", isModerator));

        Map<String, Object> data = new LinkedHashMap<>(tokenInfo);
        data.put("session_state",           session.getState().name());
        data.put("current_participant_count", session.getParticipantCount());
        data.put("quorum_required",         session.getQuorumRequired());
        data.put("quorum_met",              session.hasQuorum());
        data.put("track_type",              session.getTrackType().name());

        return resp("OK", "Joined session successfully — Jitsi token issued", data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // get_session_state
    // ─────────────────────────────────────────────────────────────────────────

    public String getSessionState(Map<String, Object> args) {
        JwtClaims claims = auth(args);
        String tenantId  = SecurityConfig.effectiveTenantId(claims, args);
        String sessionId = SecurityConfig.requireString(args, "session_id");

        AssessmentSession session = repo.findSession(tenantId, sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        // CANDIDATE: only sessions they're registered in
        if (claims.hasRole(SecurityConfig.ROLE_CANDIDATE) && !claims.hasRole(SecurityConfig.ROLE_ADMIN)) {
            if (!session.getCandidateIds().contains(claims.getUserId()))
                throw new SecurityException("CANDIDATE can only view sessions they are registered in");
        }

        Map<String, Object> data = session.toMap();
        data.put("ws_command_log_count", repo.getWsLog(sessionId).size());
        data.put("phase_expired",        session.isPhaseExpired());
        return resp("OK", "Session state retrieved", data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // transition_session_state
    // ─────────────────────────────────────────────────────────────────────────

    public String transitionState(Map<String, Object> args) {
        JwtClaims claims   = auth(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_MODERATOR, SecurityConfig.ROLE_ADMIN,
                SecurityConfig.ROLE_SERVICE_ACCOUNT);
        String tenantId    = SecurityConfig.effectiveTenantId(claims, args);
        String sessionId   = SecurityConfig.requireString(args, "session_id");
        String targetStr   = SecurityConfig.requireString(args, "target_state");
        String actorId     = SecurityConfig.getString(args, "actor_id");
        if (actorId == null) actorId = claims.getUserId();

        AssessmentSession session = repo.findSession(tenantId, sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        AssessmentSession.SessionState prev = session.getState();

        // Execute transition
        switch (targetStr.toUpperCase()) {
            case "WAITING":
                session.openWaiting(actorId); break;
            case "INTRO":
                session.startIntro(actorId);
                kafka.publishSessionStarted(session);
                break;
            case "SPEAKING":
                session.startSpeaking(actorId); break;
            case "OPEN_DISCUSSION":
                session.startOpenDiscussion(actorId); break;
            case "CLOSING":
                session.startClosing(actorId); break;
            case "COMPLETED":
                session.complete(actorId);
                repo.incCompleted();
                repo.writeAudit(session);
                break;
            case "CANCELLED":
                session.cancel(actorId, AssessmentSession.CancellationReason.MANUAL);
                repo.incCancelled();
                kafka.publishSessionCancelled(session, "MANUAL");
                break;
            default:
                throw new IllegalArgumentException("Unknown target_state: " + targetStr);
        }

        repo.saveSession(session);

        // Broadcast STATE_CHANGE via WS log
        repo.logWsCommand(sessionId, "STATE_CHANGE", Map.of(
                "previous_state", prev.name(),
                "new_state",      session.getState().name(),
                "transition_ts",  Instant.now().toString()));

        Map<String, Object> data = session.toMap();
        data.put("previous_state",     prev.name());
        data.put("transition_actor",   actorId);
        return resp("OK", "Session transitioned: " + prev + " → " + session.getState(), data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // check_quorum
    // ─────────────────────────────────────────────────────────────────────────

    public String checkQuorum(Map<String, Object> args) {
        JwtClaims claims   = auth(args);
        String tenantId    = SecurityConfig.effectiveTenantId(claims, args);
        String sessionId   = SecurityConfig.requireString(args, "session_id");
        boolean autoCancel = "true".equalsIgnoreCase(SecurityConfig.getString(args, "auto_cancel"));

        AssessmentSession session = repo.findSession(tenantId, sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        boolean quorumMet = session.hasQuorum();
        long secsUntilStart = session.getScheduledAt().getEpochSecond() - Instant.now().getEpochSecond();
        long cancelThresholdSec = Long.parseLong(
                System.getenv().getOrDefault("QUORUM_CANCEL_LEAD_TIME_MINUTES", "2")) * 60;

        boolean shouldAutoCancel = autoCancel && !quorumMet && secsUntilStart <= cancelThresholdSec
                && session.getState() == AssessmentSession.SessionState.WAITING;

        String action = "NONE";
        if (shouldAutoCancel) {
            session.cancel("QUORUM_MONITOR", AssessmentSession.CancellationReason.NO_QUORUM);
            // Mark all roster as CANCELLED
            for (String cid : session.getCandidateIds()) {
                session.markAbsent(cid);
                kafka.publishCandidateAbsent(session, cid);
            }
            kafka.publishSessionCancelled(session, "NO_QUORUM");
            repo.saveSession(session);
            repo.incCancelled();
            repo.incQuorumFail();
            action = "AUTO_CANCELLED";
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("session_id",          sessionId);
        data.put("state",               session.getState().name());
        data.put("participant_count",   session.getParticipantCount());
        data.put("quorum_required",     session.getQuorumRequired());
        data.put("quorum_met",          quorumMet);
        data.put("seconds_until_start", secsUntilStart);
        data.put("cancel_threshold_sec",cancelThresholdSec);
        data.put("auto_cancel_action",  action);
        if (shouldAutoCancel) data.put("cancellation_reason", "NO_QUORUM");

        return resp("OK",
                quorumMet ? "Quorum met" : ("Quorum not met (" + session.getParticipantCount()
                        + "/" + session.getQuorumRequired() + ")" + (shouldAutoCancel ? " — AUTO CANCELLED" : "")),
                data);
    }

    // ─ helpers ───────────────────────────────────────────────────────────────

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
