package com.ecoskiller.mcp.models;

import java.time.Instant;
import java.util.*;

/**
 * Assessment Session — one instance of a GD, Interview, or Dojo track.
 *
 * State machine (Redis-backed in production):
 *   SCHEDULED → WAITING → INTRO → SPEAKING:{candidateId} → OPEN_DISCUSSION
 *              → CLOSING → COMPLETED
 *                       ↘ CANCELLED  (from WAITING on no-quorum or manual cancel)
 *
 * GD phase durations (configurable via env):
 *   INTRO          : 60 s
 *   SPEAKING       : 90 s per candidate
 *   OPEN_DISCUSSION: 300 s (5 min)
 *   CLOSING        : 120 s (2 min)
 *
 * Redis key prefix: tenant:{tenant_id}:session:{session_id}:
 */
public class AssessmentSession {

    public enum SessionState {
        SCHEDULED, WAITING, INTRO, SPEAKING, OPEN_DISCUSSION, CLOSING, COMPLETED, CANCELLED
    }

    public enum CancellationReason { NO_QUORUM, MANUAL, TIMEOUT }

    // ── Fields ────────────────────────────────────────────────────────────────

    private String        sessionId;
    private String        cycleId;
    private String        tenantId;
    private AssessmentCycle.TrackType trackType;

    private SessionState  state;
    private String        currentSpeakerId;       // SPEAKING phase
    private List<String>  speakerQueue;            // ordered GD speaker roster
    private int           speakerIndex;            // index into speakerQueue

    private List<String>  raiseHandQueue;          // OPEN_DISCUSSION
    private int           participantCount;
    private int           quorumRequired;
    private List<String>  candidateIds;            // full registered roster

    private Instant       scheduledAt;
    private Instant       startedAt;
    private Instant       completedAt;
    private Instant       phaseStartedAt;          // when current phase began

    private long          introDurationSec;
    private long          speakingDurationSec;
    private long          openDiscussionDurationSec;
    private long          closingDurationSec;

    private String        idempotencyKey;          // session_{id}_completed
    private CancellationReason cancellationReason;
    private boolean       idempotencyKeyWritten;

    // Telemetry (ClickHouse in prod)
    private List<Map<String, Object>> stateTransitions;
    private Map<String, Long>         speakingTimeMap; // candidateId → seconds spoken
    private List<Map<String, Object>> dropEvents;

    // Participant roster: candidateId → status
    private Map<String, String> rosterStatus;

    // ── Constructor ───────────────────────────────────────────────────────────

    public AssessmentSession(String sessionId, String cycleId, String tenantId,
                             AssessmentCycle.TrackType trackType, List<String> candidateIds,
                             Instant scheduledAt, int quorumRequired) {
        this.sessionId        = sessionId;
        this.cycleId          = cycleId;
        this.tenantId         = tenantId;
        this.trackType        = trackType;
        this.candidateIds     = new ArrayList<>(candidateIds);
        this.quorumRequired   = quorumRequired;
        this.scheduledAt      = scheduledAt != null ? scheduledAt : Instant.now().plusSeconds(3600);
        this.state            = SessionState.SCHEDULED;
        this.speakerQueue     = new ArrayList<>(candidateIds);
        this.speakerIndex     = 0;
        this.raiseHandQueue   = new ArrayList<>();
        this.participantCount = 0;
        this.idempotencyKey   = "session_" + sessionId + "_completed";

        // Default GD phase durations (overridden by env in production)
        this.introDurationSec         = Long.parseLong(System.getenv().getOrDefault("GD_INTRO_DURATION_SECONDS", "60"));
        this.speakingDurationSec      = Long.parseLong(System.getenv().getOrDefault("GD_SPEAKING_DURATION_SECONDS", "90"));
        this.openDiscussionDurationSec= Long.parseLong(System.getenv().getOrDefault("GD_OPEN_DISCUSSION_DURATION_SECONDS", "300"));
        this.closingDurationSec       = Long.parseLong(System.getenv().getOrDefault("GD_CLOSING_DURATION_SECONDS", "120"));

        this.stateTransitions = new ArrayList<>();
        this.speakingTimeMap  = new LinkedHashMap<>();
        this.dropEvents       = new ArrayList<>();
        this.rosterStatus     = new LinkedHashMap<>();
        for (String cid : candidateIds) rosterStatus.put(cid, "PENDING");

        recordTransition("SYSTEM", null, SessionState.SCHEDULED.name());
    }

    // ── State machine ─────────────────────────────────────────────────────────

    public void openWaiting(String actorId) {
        validateFrom(SessionState.SCHEDULED);
        setState(actorId, SessionState.WAITING);
    }

    public void startIntro(String actorId) {
        validateFrom(SessionState.WAITING);
        if (participantCount < quorumRequired)
            throw new IllegalStateException("Quorum not met: " + participantCount + "/" + quorumRequired);
        setState(actorId, SessionState.INTRO);
        this.startedAt = Instant.now();
        this.phaseStartedAt = Instant.now();
    }

    public void startSpeaking(String actorId) {
        // Allowed from INTRO or SPEAKING (advancing to next speaker)
        if (state != SessionState.INTRO && state != SessionState.SPEAKING)
            throw new IllegalStateException("Cannot start SPEAKING from " + state);
        if (speakerIndex >= speakerQueue.size()) {
            startOpenDiscussion(actorId);
            return;
        }
        this.currentSpeakerId = speakerQueue.get(speakerIndex++);
        this.state = SessionState.SPEAKING;
        this.phaseStartedAt = Instant.now();
        recordTransition(actorId, null, "SPEAKING:" + currentSpeakerId);
    }

    public void advanceTurn(String actorId) {
        if (state != SessionState.SPEAKING)
            throw new IllegalStateException("No active speaker to advance");
        // Record speaking time
        if (currentSpeakerId != null && phaseStartedAt != null) {
            long secs = Instant.now().getEpochSecond() - phaseStartedAt.getEpochSecond();
            speakingTimeMap.merge(currentSpeakerId, secs, Long::sum);
        }
        if (speakerIndex < speakerQueue.size()) startSpeaking(actorId);
        else startOpenDiscussion(actorId);
    }

    public void startOpenDiscussion(String actorId) {
        this.state = SessionState.OPEN_DISCUSSION;
        this.phaseStartedAt = Instant.now();
        this.currentSpeakerId = null;
        recordTransition(actorId, null, SessionState.OPEN_DISCUSSION.name());
    }

    public void startClosing(String actorId) {
        validateFrom(SessionState.OPEN_DISCUSSION);
        setState(actorId, SessionState.CLOSING);
    }

    public void complete(String actorId) {
        if (state == SessionState.COMPLETED) return; // already done
        if (state != SessionState.CLOSING && state != SessionState.OPEN_DISCUSSION)
            throw new IllegalStateException("Cannot complete from " + state);
        this.state       = SessionState.COMPLETED;
        this.completedAt = Instant.now();
        recordTransition(actorId, state.name(), SessionState.COMPLETED.name());
    }

    public void cancel(String actorId, CancellationReason reason) {
        if (state == SessionState.COMPLETED || state == SessionState.CANCELLED)
            throw new IllegalStateException("Session already in terminal state: " + state);
        this.state              = SessionState.CANCELLED;
        this.completedAt        = Instant.now();
        this.cancellationReason = reason;
        recordTransition(actorId, null, "CANCELLED:" + reason);
    }

    // ── Participant management ─────────────────────────────────────────────────

    public void participantJoined(String candidateId) {
        participantCount++;
        rosterStatus.put(candidateId, "JOINED");
    }

    public void participantLeft(String candidateId) {
        rosterStatus.put(candidateId, "COMPLETED");
        if (candidateId.equals(currentSpeakerId)) {
            dropEvents.add(Map.of("candidate_id", candidateId, "timestamp", Instant.now().toString(), "event", "SPEAKER_DISCONNECT"));
        }
    }

    public void markAbsent(String candidateId) { rosterStatus.put(candidateId, "ABSENT"); }

    // ── Raise-hand queue ──────────────────────────────────────────────────────

    public int raiseHand(String candidateId) {
        if (state != SessionState.OPEN_DISCUSSION)
            throw new IllegalStateException("Raise hand only allowed during OPEN_DISCUSSION");
        if (!raiseHandQueue.contains(candidateId)) raiseHandQueue.add(candidateId);
        return raiseHandQueue.indexOf(candidateId);
    }

    public String grantNextRaiseHand() {
        if (raiseHandQueue.isEmpty()) return null;
        return raiseHandQueue.remove(0);
    }

    // ── Timer helpers ─────────────────────────────────────────────────────────

    public long getPhaseRemainingSeconds() {
        if (phaseStartedAt == null) return 0;
        long elapsed = Instant.now().getEpochSecond() - phaseStartedAt.getEpochSecond();
        long duration = getPhaseDuration();
        return Math.max(0, duration - elapsed);
    }

    public long getPhaseDuration() {
        switch (state) {
            case INTRO:            return introDurationSec;
            case SPEAKING:         return speakingDurationSec;
            case OPEN_DISCUSSION:  return openDiscussionDurationSec;
            case CLOSING:          return closingDurationSec;
            default: return 0;
        }
    }

    public boolean isPhaseExpired() {
        return phaseStartedAt != null && getPhaseRemainingSeconds() == 0;
    }

    // ── Quorum check ──────────────────────────────────────────────────────────

    public boolean hasQuorum() { return participantCount >= quorumRequired; }

    // ── Serialisation ─────────────────────────────────────────────────────────

    public Map<String, Object> toMap() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("session_id",            sessionId);
        m.put("cycle_id",              cycleId);
        m.put("tenant_id",             tenantId);
        m.put("track_type",            trackType.name());
        m.put("state",                 state.name());
        m.put("current_speaker_id",    currentSpeakerId);
        m.put("participant_count",     participantCount);
        m.put("quorum_required",       quorumRequired);
        m.put("speaker_queue",         speakerQueue);
        m.put("speaker_index",         speakerIndex);
        m.put("raise_hand_queue",      raiseHandQueue);
        m.put("roster_status",         rosterStatus);
        m.put("timer_remaining_seconds", getPhaseRemainingSeconds());
        m.put("phase_duration_seconds",  getPhaseDuration());
        m.put("scheduled_at",          scheduledAt.toString());
        m.put("started_at",            startedAt != null ? startedAt.toString() : null);
        m.put("completed_at",          completedAt != null ? completedAt.toString() : null);
        m.put("idempotency_key",       idempotencyKey);
        m.put("cancellation_reason",   cancellationReason != null ? cancellationReason.name() : null);
        m.put("redis_key_prefix",      "tenant:" + tenantId + ":session:" + sessionId + ":");
        return m;
    }

    public Map<String, Object> toTelemetryMap() {
        Map<String, Object> m = toMap();
        m.put("state_transitions",   stateTransitions);
        m.put("speaking_time_map",   speakingTimeMap);
        m.put("drop_events",         dropEvents);
        return m;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void validateFrom(SessionState expected) {
        if (state != expected)
            throw new IllegalStateException("Expected state " + expected + " but was " + state);
    }

    private void setState(String actorId, SessionState newState) {
        String prev = state.name();
        this.state = newState;
        this.phaseStartedAt = Instant.now();
        recordTransition(actorId, prev, newState.name());
    }

    private void recordTransition(String actorId, String from, String to) {
        Map<String, Object> t = new LinkedHashMap<>();
        t.put("from",      from);
        t.put("to",        to);
        t.put("actor_id",  actorId);
        t.put("timestamp", Instant.now().toString());
        stateTransitions.add(t);
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String       getSessionId()       { return sessionId; }
    public String       getCycleId()         { return cycleId; }
    public String       getTenantId()        { return tenantId; }
    public AssessmentCycle.TrackType getTrackType() { return trackType; }
    public SessionState getState()           { return state; }
    public String       getCurrentSpeakerId(){ return currentSpeakerId; }
    public int          getParticipantCount(){ return participantCount; }
    public int          getQuorumRequired()  { return quorumRequired; }
    public List<String> getCandidateIds()    { return candidateIds; }
    public Instant      getScheduledAt()     { return scheduledAt; }
    public List<String> getRaiseHandQueue()  { return raiseHandQueue; }
    public String       getIdempotencyKey()  { return idempotencyKey; }
    public Map<String, Long> getSpeakingTimeMap() { return speakingTimeMap; }
    public List<Map<String, Object>> getStateTransitions() { return stateTransitions; }
    public boolean      isIdempotencyKeyWritten() { return idempotencyKeyWritten; }
    public void         markIdempotencyKeyWritten() { this.idempotencyKeyWritten = true; }
    public void         setScheduledAt(Instant t)   { this.scheduledAt = t; }
    public Map<String, String> getRosterStatus()     { return rosterStatus; }
    public CancellationReason getCancellationReason(){ return cancellationReason; }
}
