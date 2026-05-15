package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.*;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/** Tools: create_assessment_cycle, get_cycle_status, reschedule_cycle, cancel_cycle */
public class CycleManagementTools {

    private final OrchestratorRepository repo  = OrchestratorRepository.getInstance();
    private final KafkaEventPublisher    kafka = KafkaEventPublisher.getInstance();

    // ─────────────────────────────────────────────────────────────────────────
    // create_assessment_cycle
    // ─────────────────────────────────────────────────────────────────────────

    public String createCycle(Map<String, Object> args) {
        JwtClaims claims = auth(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_RECRUITER, SecurityConfig.ROLE_ADMIN,
                SecurityConfig.ROLE_SERVICE_ACCOUNT);
        String tenantId    = SecurityConfig.effectiveTenantId(claims, args);
        String jobId       = SecurityConfig.requireString(args, "job_id");
        String recruiterId = SecurityConfig.requireString(args, "recruiter_id");
        String candidateRaw= SecurityConfig.requireString(args, "candidate_ids");
        String tracksRaw   = SecurityConfig.requireString(args, "tracks");
        String scheduledStr= SecurityConfig.getString(args, "scheduled_at");
        int quorum         = SecurityConfig.getInt(args, "quorum_required", 3);

        // Parse candidates
        List<String> candidates = Arrays.stream(candidateRaw.split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        if (candidates.isEmpty()) throw new IllegalArgumentException("candidate_ids must not be empty");

        // Parse tracks
        List<AssessmentCycle.TrackType> tracks = new ArrayList<>();
        for (String t : tracksRaw.split(",")) {
            try { tracks.add(AssessmentCycle.TrackType.valueOf(t.trim().toUpperCase())); }
            catch (IllegalArgumentException e) { throw new IllegalArgumentException("Invalid track: " + t.trim()); }
        }
        if (tracks.isEmpty()) throw new IllegalArgumentException("tracks must not be empty");

        // Scheduled time
        Instant scheduledAt;
        try { scheduledAt = scheduledStr != null ? Instant.parse(scheduledStr) : Instant.now().plusSeconds(3600); }
        catch (Exception e) { throw new IllegalArgumentException("Invalid scheduled_at format: " + scheduledStr); }

        // Acquire distributed slot locks (Redis SET NX EX 30s in prod)
        String cycleId = SecurityConfig.generateId();
        List<String> locksFailed = new ArrayList<>();
        for (String cid : candidates) {
            String lockKey = "slot_lock:" + cid + ":" + cycleId;
            if (!SecurityConfig.acquireLock(lockKey, 30_000)) locksFailed.add(cid);
        }
        if (!locksFailed.isEmpty())
            throw new IllegalStateException("Slot locks could not be acquired for: " + locksFailed);

        // Create cycle
        AssessmentCycle cycle = new AssessmentCycle(cycleId, tenantId, jobId, recruiterId, tracks, scheduledAt);
        cycle.setTotalCandidates(candidates.size());
        cycle.activate();
        repo.saveCycle(cycle);

        // Create one session per track
        int quorumFinal = quorum;
        for (AssessmentCycle.TrackType track : tracks) {
            String sessionId = SecurityConfig.generateId();
            int q = track == AssessmentCycle.TrackType.INTERVIEW ? 2 : quorumFinal;
            AssessmentSession session = new AssessmentSession(sessionId, cycleId, tenantId,
                    track, candidates, scheduledAt, q);
            repo.saveSession(session);
            cycle.addSession(sessionId);
        }
        repo.saveCycle(cycle);

        return resp("CREATED", "Assessment cycle created with " + tracks.size() + " track(s)", cycle.toMap());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // get_cycle_status
    // ─────────────────────────────────────────────────────────────────────────

    public String getCycleStatus(Map<String, Object> args) {
        JwtClaims claims = auth(args);
        String tenantId  = SecurityConfig.effectiveTenantId(claims, args);
        String cycleId   = SecurityConfig.requireString(args, "cycle_id");

        AssessmentCycle cycle = repo.findCycle(tenantId, cycleId)
                .orElseThrow(() -> new IllegalArgumentException("Cycle not found: " + cycleId));

        List<AssessmentSession> sessions = repo.findSessionsByCycle(tenantId, cycleId);

        // Build status summary
        Map<String, Object> sessionSummary = new LinkedHashMap<>();
        for (AssessmentSession s : sessions) {
            Map<String, Object> ss = new LinkedHashMap<>();
            ss.put("state",             s.getState().name());
            ss.put("track_type",        s.getTrackType().name());
            ss.put("participant_count", s.getParticipantCount());
            ss.put("quorum_required",   s.getQuorumRequired());
            sessionSummary.put(s.getSessionId(), ss);
        }

        long activeSessions   = sessions.stream().filter(s ->
                s.getState() != AssessmentSession.SessionState.COMPLETED &&
                s.getState() != AssessmentSession.SessionState.CANCELLED).count();
        long completedSessions= sessions.stream().filter(s ->
                s.getState() == AssessmentSession.SessionState.COMPLETED).count();

        Map<String, Object> data = new LinkedHashMap<>();
        data.putAll(cycle.toMap());
        data.put("active_sessions",    activeSessions);
        data.put("completed_sessions", completedSessions);
        data.put("session_summary",    sessionSummary);
        data.put("next_scheduled_session", sessions.stream()
                .filter(s -> s.getState() == AssessmentSession.SessionState.SCHEDULED ||
                             s.getState() == AssessmentSession.SessionState.WAITING)
                .min(Comparator.comparing(AssessmentSession::getScheduledAt))
                .map(s -> s.getScheduledAt().toString()).orElse(null));

        return resp("OK", "Cycle status retrieved", data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // reschedule_cycle
    // ─────────────────────────────────────────────────────────────────────────

    public String rescheduleCycle(Map<String, Object> args) {
        JwtClaims claims   = auth(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_ADMIN);
        String tenantId    = SecurityConfig.effectiveTenantId(claims, args);
        String cycleId     = SecurityConfig.requireString(args, "cycle_id");
        String newTimeStr  = SecurityConfig.requireString(args, "new_start_time");
        String reason      = SecurityConfig.sanitise(SecurityConfig.getString(args, "reason"), "reason");

        Instant newTime;
        try { newTime = Instant.parse(newTimeStr); }
        catch (Exception e) { throw new IllegalArgumentException("Invalid new_start_time: " + newTimeStr); }

        AssessmentCycle cycle = repo.findCycle(tenantId, cycleId)
                .orElseThrow(() -> new IllegalArgumentException("Cycle not found: " + cycleId));

        if (cycle.getStatus() == AssessmentCycle.CycleStatus.COMPLETED ||
            cycle.getStatus() == AssessmentCycle.CycleStatus.CANCELLED)
            throw new IllegalStateException("Cannot reschedule a " + cycle.getStatus() + " cycle");

        // Update all SCHEDULED/WAITING sessions
        List<AssessmentSession> rescheduled = new ArrayList<>();
        for (String sid : cycle.getSessionIds()) {
            repo.findSession(tenantId, sid).ifPresent(s -> {
                if (s.getState() == AssessmentSession.SessionState.SCHEDULED ||
                    s.getState() == AssessmentSession.SessionState.WAITING) {
                    s.setScheduledAt(newTime);
                    repo.saveSession(s);
                    rescheduled.add(s);
                }
            });
        }
        cycle.setScheduledAt(newTime);
        repo.saveCycle(cycle);

        // Publish reschedule notification via Kafka (notification-service consumes)
        Map<String, Object> kafkaPayload = new LinkedHashMap<>();
        kafkaPayload.put("event_type",    "assessment.cycle.rescheduled");
        kafkaPayload.put("cycle_id",      cycleId);
        kafkaPayload.put("tenant_id",     tenantId);
        kafkaPayload.put("new_start_time",newTime.toString());
        kafkaPayload.put("reason",        reason);
        kafkaPayload.put("affected_sessions", rescheduled.stream().map(AssessmentSession::getSessionId)
                .collect(Collectors.toList()));
        repo.logKafkaEvent("assessment.cycle.rescheduled", kafkaPayload);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("cycle_id",            cycleId);
        data.put("new_start_time",      newTime.toString());
        data.put("rescheduled_sessions",rescheduled.size());
        data.put("reason",              reason);
        return resp("OK", rescheduled.size() + " session(s) rescheduled", data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // cancel_cycle
    // ─────────────────────────────────────────────────────────────────────────

    public String cancelCycle(Map<String, Object> args) {
        JwtClaims claims = auth(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_ADMIN, SecurityConfig.ROLE_MODERATOR);
        String tenantId  = SecurityConfig.effectiveTenantId(claims, args);
        String cycleId   = SecurityConfig.requireString(args, "cycle_id");
        String reason    = SecurityConfig.sanitise(SecurityConfig.getString(args, "reason"), "reason");

        AssessmentCycle cycle = repo.findCycle(tenantId, cycleId)
                .orElseThrow(() -> new IllegalArgumentException("Cycle not found: " + cycleId));

        if (cycle.getStatus() == AssessmentCycle.CycleStatus.CANCELLED)
            throw new IllegalStateException("Cycle already cancelled");

        // Cancel all non-terminal sessions
        int cancelledCount = 0;
        for (String sid : cycle.getSessionIds()) {
            Optional<AssessmentSession> opt = repo.findSession(tenantId, sid);
            if (opt.isPresent()) {
                AssessmentSession s = opt.get();
                if (s.getState() != AssessmentSession.SessionState.COMPLETED &&
                    s.getState() != AssessmentSession.SessionState.CANCELLED) {
                    s.cancel(claims.getUserId(), AssessmentSession.CancellationReason.MANUAL);
                    repo.saveSession(s);
                    kafka.publishSessionCancelled(s, reason != null ? reason : "MANUAL");
                    repo.incCancelled();
                    cancelledCount++;
                }
            }
        }

        cycle.cancel(reason != null ? reason : "MANUAL");
        repo.saveCycle(cycle);

        List<String> completedTrackNames = cycle.getTracks().stream()
                .map(Enum::name).collect(Collectors.toList());
        kafka.publishCycleCompleted(cycle, completedTrackNames);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("cycle_id",          cycleId);
        data.put("status",            "CANCELLED");
        data.put("reason",            reason);
        data.put("cancelled_sessions",cancelledCount);
        return resp("OK", "Cycle cancelled; " + cancelledCount + " session(s) terminated", data);
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
