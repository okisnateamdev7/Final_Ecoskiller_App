package com.ecoskiller.websocket.model;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Session — Ecoskiller WebSocket session domain model.
 *
 * Covers all three session types from the specification:
 *   GD_ORCHESTRATOR  : Group Discussion (4-8 participants, state machine)
 *   INTERVIEW         : Candidate + Recruiter (2-client, 99.9% SLO)
 *   DOJO_MATCH        : Competitive coding (2 candidates)
 *
 * GD State machine: WAITING → INTRO → SPEAKING:{candidateId} → OPEN_DISCUSSION → CLOSING → COMPLETED
 * Redis key patterns: gd:{sessionId}:state | :timer | :current_speaker | :queue | :result
 * All keys prefixed: tenant:{tenantId}:{above} for multi-tenant isolation
 */
public class Session {

    // ── Enums ─────────────────────────────────────────────────────────────────

    public enum SessionType { GD_ORCHESTRATOR, INTERVIEW, DOJO_MATCH }

    public enum GdState {
        WAITING, INTRO, SPEAKING, OPEN_DISCUSSION, CLOSING, COMPLETED
    }

    public enum InterviewState {
        SCHEDULED, STARTED, PAUSED, COMPLETED, CANCELLED
    }

    public enum DojoState {
        WAITING, STARTED, PEER_SUBMITTED, COMPLETED
    }

    public enum ConnectionState { CONNECTED, DISCONNECTED, RECONNECTING }

    public enum Environment { DEV, TEST, STAGE, PROD }

    // ── Phase durations (seconds) per spec ───────────────────────────────────
    public static final int INTRO_DURATION_S          = 60;
    public static final int SPEAKING_DURATION_S       = 90;
    public static final int OPEN_DISCUSSION_DURATION_S= 300;
    public static final int CLOSING_DURATION_S        = 120;

    // ── Core identity ─────────────────────────────────────────────────────────
    private final String      sessionId;
    private final String      tenantId;   // multi-tenant isolation boundary
    private final SessionType sessionType;
    private final Instant     createdAt;
    private       Instant     startedAt;
    private       Instant     completedAt;

    // ── Participants ──────────────────────────────────────────────────────────
    private final List<String>            participantIds     = new ArrayList<>();
    private final Map<String, ConnectionState> connectionStates = new ConcurrentHashMap<>();

    // ── GD state machine ──────────────────────────────────────────────────────
    private GdState   gdState         = GdState.WAITING;
    private String    currentSpeakerId;
    private final List<String> raiseHandQueue = new ArrayList<>(); // Redis LIST equivalent
    private int       timerRemaining  = 0;
    private String    gdPhase;

    // ── Interview state ───────────────────────────────────────────────────────
    private InterviewState interviewState  = InterviewState.SCHEDULED;
    private String         candidateId;
    private String         recruiterId;
    private boolean        noteLocked      = false;
    private int            elapsedSeconds  = 0;
    private int            durationSeconds = 3600; // default 60 min

    // ── Dojo match state ──────────────────────────────────────────────────────
    private DojoState dojoState     = DojoState.WAITING;
    private String    matchId;
    private String    scenarioId;
    private int       dojoTimerRemaining = 0;
    private final Set<String> submittedCandidates = new HashSet<>();

    // ── Event log ─────────────────────────────────────────────────────────────
    private final List<WsEvent> eventLog = new ArrayList<>();

    // ── SLO tracking ─────────────────────────────────────────────────────────
    private long  connectionDrops   = 0;
    private long  totalHeartbeats   = 0;
    private long  failedHeartbeats  = 0;

    // ── Environment ───────────────────────────────────────────────────────────
    private Environment environment = Environment.PROD;

    public Session(String sessionId, String tenantId, SessionType sessionType) {
        if (sessionId == null || sessionId.isBlank()) throw new IllegalArgumentException("sessionId required");
        if (tenantId  == null || tenantId.isBlank())  throw new IllegalArgumentException("tenantId required (multi-tenant isolation)");
        this.sessionId   = sessionId;
        this.tenantId    = tenantId;
        this.sessionType = sessionType;
        this.createdAt   = Instant.now();
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String      getSessionId()          { return sessionId; }
    public String      getTenantId()           { return tenantId; }
    public SessionType getSessionType()        { return sessionType; }
    public Instant     getCreatedAt()          { return createdAt; }
    public Instant     getStartedAt()          { return startedAt; }
    public Instant     getCompletedAt()        { return completedAt; }
    public List<String> getParticipantIds()    { return Collections.unmodifiableList(participantIds); }
    public ConnectionState getConnectionState(String pid) { return connectionStates.getOrDefault(pid, ConnectionState.DISCONNECTED); }
    public GdState     getGdState()            { return gdState; }
    public String      getCurrentSpeakerId()   { return currentSpeakerId; }
    public List<String> getRaiseHandQueue()    { return Collections.unmodifiableList(raiseHandQueue); }
    public int         getTimerRemaining()     { return timerRemaining; }
    public String      getGdPhase()            { return gdPhase; }
    public InterviewState getInterviewState()  { return interviewState; }
    public String      getCandidateId()        { return candidateId; }
    public String      getRecruiterId()        { return recruiterId; }
    public boolean     isNoteLocked()          { return noteLocked; }
    public int         getElapsedSeconds()     { return elapsedSeconds; }
    public int         getDurationSeconds()    { return durationSeconds; }
    public DojoState   getDojoState()          { return dojoState; }
    public String      getMatchId()            { return matchId; }
    public String      getScenarioId()         { return scenarioId; }
    public int         getDojoTimerRemaining() { return dojoTimerRemaining; }
    public Set<String> getSubmittedCandidates(){ return Collections.unmodifiableSet(submittedCandidates); }
    public List<WsEvent> getEventLog()         { return Collections.unmodifiableList(eventLog); }
    public long        getConnectionDrops()    { return connectionDrops; }
    public double      getSloPercent()         {
        long total = totalHeartbeats + failedHeartbeats;
        if (total == 0) return 100.0;
        return (totalHeartbeats * 100.0) / total;
    }
    public Environment getEnvironment()        { return environment; }

    // ── Redis key helpers (per spec section 10.3) ─────────────────────────────
    public String redisKey(String suffix) {
        return "tenant:" + tenantId + ":gd:" + sessionId + ":" + suffix;
    }

    /** WSS URL per spec section 10.2 */
    public String getWssUrl() {
        switch (environment) {
            case DEV:   return "ws://localhost:8080/ws/session/" + sessionId;
            case TEST:  return "wss://test-api.ecoskiller.com/ws/session/" + sessionId;
            case STAGE: return "wss://stage-api.ecoskiller.com/ws/session/" + sessionId;
            default:    return "wss://api.ecoskiller.com/ws/session/" + sessionId;
        }
    }

    // ── Mutators ──────────────────────────────────────────────────────────────
    public void setEnvironment(Environment e)         { environment = e; }
    public void setCandidateId(String id)             { candidateId = id; }
    public void setRecruiterId(String id)             { recruiterId = id; }
    public void setDurationSeconds(int d)             { durationSeconds = d; }
    public void setMatchId(String id)                 { matchId = id; }
    public void setScenarioId(String id)              { scenarioId = id; }
    public void setDojoTimerRemaining(int t)          { dojoTimerRemaining = t; }
    public void setTimerRemaining(int t)              { timerRemaining = t; }
    public void setNoteLocked(boolean v)              { noteLocked = v; }
    public void setElapsedSeconds(int v)              { elapsedSeconds = v; }

    public void addParticipant(String pid) {
        if (!participantIds.contains(pid)) participantIds.add(pid);
        connectionStates.put(pid, ConnectionState.CONNECTED);
    }

    public void setConnectionState(String pid, ConnectionState state) {
        connectionStates.put(pid, state);
        if (state == ConnectionState.DISCONNECTED) connectionDrops++;
    }

    public void recordHeartbeat(String pid, boolean success) {
        if (success) totalHeartbeats++; else failedHeartbeats++;
    }

    // ── GD state machine transitions ──────────────────────────────────────────

    public void transitionGdState(GdState newState, String speakerId) {
        GdState prev = gdState;
        gdState = newState;
        switch (newState) {
            case INTRO:            timerRemaining = INTRO_DURATION_S;  gdPhase = "INTRO"; break;
            case SPEAKING:         timerRemaining = SPEAKING_DURATION_S; currentSpeakerId = speakerId; gdPhase = "SPEAKING"; break;
            case OPEN_DISCUSSION:  timerRemaining = OPEN_DISCUSSION_DURATION_S; gdPhase = "OPEN_DISCUSSION"; break;
            case CLOSING:          timerRemaining = CLOSING_DURATION_S; gdPhase = "CLOSING"; break;
            case COMPLETED:        completedAt = Instant.now(); gdPhase = "COMPLETED"; break;
            case WAITING:          gdPhase = "WAITING"; break;
        }
        if (newState == GdState.WAITING || newState == GdState.INTRO || newState == GdState.CLOSING)
            currentSpeakerId = null;
        logEvent("GD_STATE_CHANGE", prev.name() + " → " + newState.name() + (speakerId != null ? " speaker=" + speakerId : ""));
    }

    public boolean raiseHand(String candidateId) {
        if (gdState != GdState.OPEN_DISCUSSION) return false;
        if (!raiseHandQueue.contains(candidateId)) { raiseHandQueue.add(candidateId); return true; }
        return false;
    }

    public String advanceSpeaker() {
        if (!raiseHandQueue.isEmpty()) {
            String next = raiseHandQueue.remove(0);
            transitionGdState(GdState.SPEAKING, next);
            return next;
        }
        return null;
    }

    // ── Interview state machine ───────────────────────────────────────────────

    public void startInterview() {
        interviewState = InterviewState.STARTED;
        startedAt = Instant.now();
        logEvent("INTERVIEW_STARTED", "candidateId=" + candidateId + " recruiterId=" + recruiterId);
    }

    public void pauseInterview(String reason) {
        interviewState = InterviewState.PAUSED;
        logEvent("INTERVIEW_PAUSED", "reason=" + reason);
    }

    public void resumeInterview() {
        interviewState = InterviewState.STARTED;
        logEvent("INTERVIEW_RESUMED", "");
    }

    public void completeInterview() {
        interviewState = InterviewState.COMPLETED;
        completedAt = Instant.now();
        logEvent("INTERVIEW_COMPLETED", "duration=" + elapsedSeconds + "s");
    }

    // ── Dojo state machine ────────────────────────────────────────────────────

    public void startDojoMatch() {
        dojoState = DojoState.STARTED;
        startedAt = Instant.now();
        logEvent("MATCH_START", "matchId=" + matchId + " scenarioId=" + scenarioId);
    }

    public boolean submitDojo(String candidateId) {
        if (submittedCandidates.contains(candidateId)) return false;
        submittedCandidates.add(candidateId);
        logEvent("PEER_SUBMITTED", "candidateId=" + candidateId);
        if (submittedCandidates.size() >= 2) { dojoState = DojoState.COMPLETED; completedAt = Instant.now(); }
        return true;
    }

    public void completeDojoMatch() {
        dojoState = DojoState.COMPLETED;
        completedAt = Instant.now();
        logEvent("MATCH_END", "matchId=" + matchId);
    }

    private void logEvent(String type, String detail) {
        eventLog.add(new WsEvent(type, sessionId, detail));
    }

    // ── Inner type ────────────────────────────────────────────────────────────

    public static class WsEvent {
        public final String  type;
        public final String  sessionId;
        public final String  detail;
        public final Instant timestamp;
        public WsEvent(String type, String sessionId, String detail) {
            this.type = type; this.sessionId = sessionId;
            this.detail = detail; this.timestamp = Instant.now();
        }
    }
}
