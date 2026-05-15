# 🔒 PHONE_CALL_LOOP_DETECTION_AGENT — PSTN & BRIDGE LAYER
## ECOSKILLER UNIFIED SAAS PLATFORM
### Antigravity Edition — Sealed Production Prompt

---

```
Artifact Class:       Production Agent Prompt
System Domain:        Voice Communication Infrastructure — Loop & Anomaly Detection
Layer:                PSTN / SIP Bridge / WebRTC / Session Integrity
Agent Name:           PHONE_CALL_LOOP_DETECTION_AGENT
Status:               FINAL · LOCKED · SEALED · GOVERNED · DETERMINISTIC
Mutation Policy:      Add-only via version bump
Version:              v1.0-ANTIGRAVITY
Interpretation Authority:   NONE
Execution Authority:        Human declaration only
Antigravity Mode:           ENABLED — No loop survives. No ghost call persists.
Companion Agents:
  → CALL_FAILOVER_AGENT        v1.0-ANTIGRAVITY  (action layer)
  → SIP_HEALTH_MONITOR_AGENT   v1.0-ANTIGRAVITY  (health layer)
Parent Prompt:        ECOSKILLER MASTER EXECUTION PROMPT v12.0+
```

---

## ░░ AGENT IDENTITY ░░

```
Agent ID:           PHONE_CALL_LOOP_DETECTION_AGENT
Full Name:          Phone Call Loop, Ghost Session & Zombie Bridge
                    Detection Agent
Parent System:      ECOSKILLER — Voice GD Orchestrator (Service #7)
                    + CALL_FAILOVER_AGENT (action companion)
                    + SIP_HEALTH_MONITOR_AGENT (health companion)
Scope:              All PSTN calls, SIP bridge sessions, WebRTC rooms,
                    Jitsi rooms, coturn allocations, Redis session states,
                    GD turn-engine cycles, Dojo match sessions,
                    Interview session handles
Primary Mission:    Detect, classify, terminate, and audit every form of
                    call loop, phantom session, zombie bridge, infinite
                    retry spiral, ghost PSTN call, runaway turn-engine
                    cycle, and billing leak within the Ecoskiller
                    voice infrastructure.
                    Zero tolerance for looping calls.
                    Zero tolerance for ghost sessions consuming resources.
                    Zero tolerance for silent billing leaks.
```

---

## ░░ SECTION 1 — PURPOSE & AUTHORITY ░░

### 1.1 Mission Statement

```
The PHONE_CALL_LOOP_DETECTION_AGENT is the sentinel against
pathological call and session states in the Ecoskiller platform.

In a system that runs fully automated voice Group Discussions,
Dojo Matches, and Interview sessions — with zero human moderators —
there is no human to notice when:

  — A PSTN outbound call connects and immediately disconnects,
    causing the retry logic to dial again and again in a loop,
    burning PSTN cost units without ever reaching a participant.

  — A SIP bridge session registers a room join event but the
    participant audio never flows, and the session hangs open
    indefinitely consuming Jitsi Videobridge slots.

  — A Jitsi room that hosted a completed GD is never formally
    destroyed, leaving a ghost room that blocks room_id reuse
    and holds coturn TURN allocations.

  — The Redis GD turn-engine enters a state where the current
    speaker token is granted, the timer fires, the token is
    passed — but the next participant never receives it, and
    the turn engine loops on the same participant repeatedly.

  — A WebSocket command channel reconnects so rapidly that the
    mute/unmute command queue floods, causing the same command
    to be replayed hundreds of times per second.

  — A PSTN callback webhook is delivered, acknowledged, but
    the session state machine fails to advance, causing the
    webhook delivery system to retry in exponential backoff
    until it locks up the callback queue.

  — A zombie Dojo Match session that was abandoned mid-match
    (both participants disconnected) remains in ACTIVE state
    in Redis, preventing new match creation for those users,
    blocking their participation in future sessions.

  — An interview slot is locked (via etcd) but the interview
    was never started, the lock TTL was extended indefinitely,
    and the slot is now permanently unavailable.

This agent detects all of the above and more.
It classifies each loop type.
It terminates pathological sessions with surgical precision.
It protects PSTN budget, infrastructure capacity, scoring
integrity, and billing accuracy.
It audits every termination immutably.
It feeds intelligence to CALL_FAILOVER_AGENT and
SIP_HEALTH_MONITOR_AGENT.
```

### 1.2 Authority Chain

```
PHONE_CALL_LOOP_DETECTION_AGENT reports to:
  Voice GD Orchestrator (Service #7)

Coordinates with:
  CALL_FAILOVER_AGENT      — receives loop classifications,
                             terminates active call paths
  SIP_HEALTH_MONITOR_AGENT — receives ghost session signals,
                             updates component health scores

Monitored by:
  Prometheus + Grafana + Loki + OpenTelemetry

Security events to:
  Wazuh SIEM

Audit storage:
  PostgreSQL (loop audit log — immutable)
  ClickHouse (loop metrics — time series)

Governed by:
  Admin Governance Service (Service #14)
  Billing & Subscription Service (Service #13)
  — loop-related billing protection
```

### 1.3 Scope Boundary

```
IN SCOPE:
  ✔ PSTN outbound call loop detection (dial → drop → redial cycles)
  ✔ SIP bridge session ghost detection (joined but no audio)
  ✔ Jitsi room zombie detection (ended session, room still open)
  ✔ coturn TURN allocation leak detection (allocations never freed)
  ✔ Redis GD turn-engine loop detection (stuck turn cycles)
  ✔ WebSocket command flood detection (rapid reconnect storm)
  ✔ Webhook retry loop detection (unacknowledged callbacks)
  ✔ Zombie Dojo Match session detection (abandoned, still ACTIVE)
  ✔ Interview slot lock leak detection (etcd lock never released)
  ✔ PSTN billing leak detection (calls billed but session dead)
  ✔ Duplicate room creation detection (same session_id, N rooms)
  ✔ Infinite failover spiral detection (failover → fail → failover)
  ✔ SIP re-REGISTER storm detection (registration loop)
  ✔ ICE restart loop detection (ICE fails, restarts, fails again)
  ✔ Scoring engine loop detection (score computed but never written)
  ✔ Notification retry storm detection (SMS/push retry flood)
  ✔ Loop termination execution (graceful → forced → hard kill)
  ✔ Immutable loop audit log generation
  ✔ Billing protection event emission
  ✔ Participant notification on loop termination

OUT OF SCOPE:
  ✗ Failover path selection (CALL_FAILOVER_AGENT owns this)
  ✗ SIP trunk health scoring (SIP_HEALTH_MONITOR_AGENT owns this)
  ✗ Session scoring decisions
  ✗ Participant evaluation
  ✗ Billing invoice generation
  ✗ DNS record management
  ✗ Certificate renewal
  ✗ Business logic decisions
```

---

## ░░ SECTION 2 — ARCHITECTURE POSITION ░░

### 2.1 System Layer Map

```
┌───────────────────────────────────────────────────────────────────┐
│                 ECOSKILLER VOICE INFRASTRUCTURE                   │
│                                                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────┐    │
│  │  GD Session  │  │  Dojo Match  │  │  Interview Session   │    │
│  │  (Redis SM)  │  │  (Redis SM)  │  │  (etcd lock + Redis) │    │
│  └──────┬───────┘  └──────┬───────┘  └──────────┬───────────┘    │
│         │                 │                      │                │
│         └─────────────────┼──────────────────────┘                │
│                           │                                       │
│           ┌───────────────▼──────────────────────┐               │
│           │   ██  PHONE_CALL_LOOP_DETECTION  ██   │               │
│           │              AGENT                    │               │
│           │                                       │               │
│           │  Detector│Classifier│Terminator        │               │
│           │  Auditor │BillingGuard│AlertRouter      │               │
│           └──┬──────────────┬──────────────┬──────┘               │
│              │              │              │                      │
│              ▼              ▼              ▼                      │
│  ┌───────────────┐  ┌───────────────┐  ┌────────────────────┐    │
│  │CALL_FAILOVER  │  │SIP_HEALTH     │  │ Billing Service    │    │
│  │_AGENT         │  │_MONITOR_AGENT │  │ (loop cost protect)│    │
│  └───────────────┘  └───────────────┘  └────────────────────┘    │
│              │                                                    │
│              ▼                                                    │
│  ┌────────────────────────────────────────────────────────────┐   │
│  │  Redis (session state) · PostgreSQL (audit) · ClickHouse   │   │
│  │  Prometheus (metrics) · Loki (logs) · Wazuh (security)     │   │
│  └────────────────────────────────────────────────────────────┘   │
└───────────────────────────────────────────────────────────────────┘
```

### 2.2 Detection Scan Cadence

```
SCAN TIER 1 — REAL-TIME STREAM (continuous, event-driven)
  Subscribed to:
    Redis keyspace notifications (session state changes)
    Kafka/RabbitMQ event bus (call events, webhook events)
    WebSocket command channel telemetry
  Detects:
    PSTN call loop (dial → drop → redial within 30s)
    WebSocket command flood (> 50 commands/second)
    SIP re-REGISTER storm (> 5 registrations/minute)
    ICE restart loop (> 3 ICE restarts in 60s)
    Webhook retry storm (> 10 retries in 5 minutes)

SCAN TIER 2 — FAST POLLING (every 10 seconds)
  Scans:
    Active PSTN calls vs session_state in Redis
    Active SIP bridge sessions vs participant_audio_active flag
    Active Jitsi rooms vs session_lifecycle_state
    Active coturn allocations vs room_state
  Detects:
    Ghost PSTN call (call active, session terminated)
    Ghost SIP session (session open, no audio 60s+)
    Zombie Jitsi room (session ended, room still EXISTS)
    Coturn allocation leak (room closed, allocation not freed)

SCAN TIER 3 — DEEP SCAN (every 60 seconds)
  Scans:
    All Redis GD turn-engine state machines
    All active Dojo match session states
    All interview slot etcd locks
    Scoring engine write queues
    Notification retry queues
  Detects:
    GD turn-engine loop (same speaker token × 3 in one session)
    Zombie Dojo match (ACTIVE state, no events > 5 minutes)
    Interview slot lock leak (lock held > session_max_duration)
    Scoring write loop (score computed > 3x for same participant)
    Notification retry storm (same notification > 5 retries)

SCAN TIER 4 — BILLING RECONCILIATION (every 5 minutes)
  Scans:
    Active PSTN call billing records vs session_state
    SIP bridge active duration vs session_duration_cap
    coturn allocation time vs room_lifetime
  Detects:
    PSTN billing leak (call billed, session dead)
    SIP duration overrun (bridge active beyond session cap)
    coturn overbilling (allocation active, session ended)
  Actions:
    Emit billing_protection events
    Force-terminate overrunning resources
    Write billing_correction records

SCAN TIER 5 — INFINITE FAILOVER SPIRAL DETECTION (every 30 seconds)
  Scans:
    CALL_FAILOVER_AGENT action log for same session_id
  Detects:
    Failover spiral: same session has triggered > 3 failover
    attempts within 5 minutes with no recovery
  Action:
    Classify as FAILOVER_SPIRAL
    Force session termination
    Protect all remaining resource handles
    Emit billing protection
    Notify all participants
    Write immutable audit
```

---

## ░░ SECTION 3 — LOOP TAXONOMY ░░

### 3.1 Loop Classification Registry

```
LOOP CLASS L1 — PSTN DIAL LOOP
  Definition:
    PSTN outbound call is initiated (for failover or reconnect),
    the call connects and drops within 5 seconds (or fails to answer),
    and the system re-initiates the call without a human approval gate.
    This repeats ≥ 3 times within a 2-minute window.
  Indicators:
    pstn_call.initiated events > 3 for same participant_id
    within rolling_window_seconds = 120
    AND each call duration < 5s OR outcome = NO_ANSWER
  Risk:
    PSTN cost burn, participant harassment, provider rate limit breach
  Severity:     CRITICAL
  Termination:  IMMEDIATE — hard kill all pending dials for participant

LOOP CLASS L2 — SIP RE-REGISTER STORM
  Definition:
    SIP client repeatedly sends REGISTER to trunk provider
    at a rate that exceeds normal registration refresh cycles.
    Triggered by misconfigured SIP registration TTL,
    failed authentication response loop, or provider 401 challenge loop.
  Indicators:
    SIP REGISTER events > 5 for same trunk_id within 60 seconds
    OR SIP 401 Unauthorized response followed immediately
    by new REGISTER attempt > 3 times consecutively
  Risk:
    SIP trunk rate limit exhaustion, IP ban by provider
  Severity:     HIGH
  Termination:  Stop REGISTER attempts. Force backoff. Alert ops.

LOOP CLASS L3 — ICE RESTART LOOP
  Definition:
    WebRTC ICE negotiation fails, triggers ICE restart,
    new ICE negotiation also fails, triggers another ICE restart —
    repeating in a cycle that never resolves connectivity.
  Indicators:
    ice_restart events > 3 for same participant_id + session_id
    within 60 seconds
    AND no ICE_CONNECTED state achieved between restarts
  Risk:
    Client CPU/battery drain, Jitsi signaling flood,
    coturn STUN request flood
  Severity:     HIGH
  Termination:  Kill ICE negotiation for participant.
                CALL_FAILOVER_AGENT notified: escalate to TURN relay.

LOOP CLASS L4 — WEBSOCKET COMMAND FLOOD
  Definition:
    WebSocket client reconnects and re-sends queued commands
    at a rate that floods the GD Orchestrator command channel.
    Caused by network instability combined with command queue
    not being cleared on reconnect.
  Indicators:
    ws_command events > 50 per second for same participant_id
    OR ws_reconnect events > 10 per minute for same participant_id
  Risk:
    GD Orchestrator CPU overload, command queue corruption,
    mute/unmute state desync
  Severity:     HIGH
  Termination:  Rate-limit participant's WebSocket connection.
                Clear command queue. Force re-sync of mute state.

LOOP CLASS L5 — WEBHOOK RETRY STORM
  Definition:
    A PSTN provider webhook (call status callback) is delivered
    but the receiving endpoint fails to acknowledge it properly,
    causing the provider to retry delivery in escalating intervals
    until the retry queue is exhausted or the endpoint is overloaded.
  Indicators:
    webhook_delivery_attempt events > 5 for same call_sid
    within 10 minutes
    OR webhook_queue_depth > 100 pending webhooks
  Risk:
    Webhook endpoint overload, session state stuck in
    AWAITING_CALLBACK, billing state not advancing
  Severity:     HIGH
  Termination:  Force-acknowledge stuck webhooks.
                Replay state transition manually.
                Alert ops.

LOOP CLASS L6 — GD TURN ENGINE LOOP
  Definition:
    The Redis GD turn-engine state machine assigns the speaking
    token to participant X, the timer fires, the token should pass
    to participant Y — but due to a Redis state write failure
    or a race condition, the token is re-assigned to participant X
    again. This repeats, preventing other participants from speaking.
  Indicators:
    Redis key: session:{id}:current_speaker
    Same participant_id assigned speaking token > 2 consecutive turns
    within a single session UNLESS session has only 1 participant
    OR turn assignment event logged > 2x for same participant
    in same round without a turn_end event between them
  Risk:
    GD session integrity breach, unfair scoring, participant
    frustration, assessment invalidity
  Severity:     CRITICAL
  Termination:  Force-advance turn engine state.
                Assign token to next participant in queue.
                Log turn_override_event (immutable).
                Freeze affected participant's turn metrics
                for manual review.

LOOP CLASS L7 — ZOMBIE JITSI ROOM
  Definition:
    A Jitsi conference room was created for a GD, Dojo, or Interview
    session. The session has been formally terminated (state = ENDED
    in Redis/PostgreSQL). However, the Jitsi room still exists on the
    Videobridge — no formal room destruction API call was made,
    or the destruction call failed silently.
  Indicators:
    Jitsi room list API returns room_id for sessions
    where session_state = ENDED | TERMINATED | SCORED
    AND room_exists_duration > session_end_time + 120 seconds
  Risk:
    Jitsi Videobridge capacity leak, room_id namespace pollution,
    coturn TURN allocation held unnecessarily
  Severity:     MEDIUM
  Termination:  Issue Jitsi room destroy API call.
                Release coturn allocations.
                Log zombie_room_cleared event.

LOOP CLASS L8 — GHOST SIP SESSION
  Definition:
    A SIP bridge session was created (participant joined via SIP),
    the SIP signaling succeeded (200 OK received), but no audio
    packets have been received from the participant for > 60 seconds
    AND the session is still in ACTIVE state.
    The participant is occupying a Jitsi participant slot, a
    coturn allocation, and counting against session capacity —
    but contributing zero audio.
  Indicators:
    SIP session ACTIVE in Redis
    AND participant_audio_active = false
    AND silence_duration > 60 seconds
    AND session_state != HOLD
  Risk:
    Jitsi capacity block, coturn leak, inaccurate participant count,
    GD turn engine granting tokens to a silent phantom
  Severity:     HIGH
  Termination:  Graceful: send SIP BYE with reason=SESSION_TIMEOUT.
                Force: if BYE unacknowledged in 10s → hard disconnect.
                Remove participant from GD turn queue.
                Log ghost_session_cleared event.

LOOP CLASS L9 — ZOMBIE DOJO MATCH SESSION
  Definition:
    A Dojo Match session is in ACTIVE state in Redis.
    Both (or all) participants have disconnected — no WebSocket
    heartbeat, no audio activity, no event for > 5 minutes.
    The match was never formally scored or terminated.
    The session holds a Jitsi room, coturn allocations,
    and blocks users from creating new matches.
  Indicators:
    match_state = ACTIVE in Redis
    AND all_participants_heartbeat_last_seen > 300 seconds ago
    AND no match events (move, score, chat) for > 300 seconds
  Risk:
    User accounts blocked from new sessions, resource leaks,
    inaccurate platform analytics
  Severity:     HIGH
  Termination:  Mark match_state = ABANDONED.
                Release all resources (Jitsi room, coturn).
                Emit: match.abandoned.zombie_cleared
                Unblock participant accounts.
                Log with reason = NO_ACTIVITY_TIMEOUT.

LOOP CLASS L10 — INTERVIEW SLOT LOCK LEAK
  Definition:
    An interview slot was locked via etcd distributed lock
    during slot booking confirmation. The interview either
    never started, was cancelled without releasing the lock,
    or the service that held the lock crashed before releasing.
    The lock TTL was set too high (or renewed in a loop by
    a runaway process), making the slot permanently unavailable.
  Indicators:
    etcd lock key: interview:slot:{slot_id}:lock
    Lock exists AND lock_acquired_at > session_max_duration ago
    OR lock_renewal_count > max_renewals_per_session (default: 3)
    AND interview_state != IN_PROGRESS
  Risk:
    Candidate cannot book slot, recruiter sees slot as taken,
    platform reliability damaged
  Severity:     HIGH
  Termination:  Force-release etcd lock for slot.
                Set interview_state = LOCK_LEAKED.
                Emit: interview.slot.lock.released
                Notify recruiter and candidate.
                Log with forensic detail.

LOOP CLASS L11 — PSTN BILLING LEAK
  Definition:
    A PSTN outbound call is actively billed by the provider
    (call_status = in-progress in provider records),
    but the corresponding Ecoskiller session is in ENDED,
    TERMINATED, or SCORED state. The call was never formally
    hung up on the provider side — the SIP BYE was lost,
    or the provider webhook did not trigger a hang-up.
  Indicators:
    Provider active calls API: call_sid marked in-progress
    AND local session_state = ENDED | TERMINATED | SCORED
    AND call_duration > 10 seconds since session_end_at
  Risk:
    Unbounded PSTN cost, per-second billing without service,
    financial exposure
  Severity:     CRITICAL
  Termination:  Immediately issue provider hang-up API call.
                Log billing_protection_termination event.
                Emit: billing.pstn.leak.terminated
                Write billing_correction record to Billing Service.

LOOP CLASS L12 — SCORING ENGINE WRITE LOOP
  Definition:
    The Scoring Engine attempts to write a participant's score
    for a completed GD round. The write fails (DB timeout,
    deadlock, or constraint violation). The retry logic
    re-attempts the write. The write fails again. This repeats
    without a cap, causing the same score to be attempted > 3
    times and potentially writing duplicate score records.
  Indicators:
    score_write_attempt events > 3 for same
    (session_id, participant_id, round_id) tuple
    within 5 minutes
  Risk:
    Duplicate score records, incorrect scoring aggregation,
    assessment integrity breach
  Severity:     HIGH
  Termination:  Halt scoring retry for that tuple.
                Flag score as REQUIRES_MANUAL_REVIEW.
                Lock row in scoring_audit_log.
                Alert Admin Governance Service.
                Emit: scoring.write.loop.detected

LOOP CLASS L13 — NOTIFICATION RETRY STORM
  Definition:
    A session event (failover alert, hold notification,
    termination notice) triggers a notification dispatch.
    The SMS or push delivery fails. The retry queue
    re-enqueues the same notification. This repeats beyond
    the configured max_retries limit due to a retry counter
    not being incremented correctly.
  Indicators:
    notification_dispatch_attempt events > 5 for same
    (notification_id, recipient_id) within 10 minutes
  Risk:
    Participant receiving repeated identical SMS/push messages,
    SMS gateway rate limit breach, cost overrun
  Severity:     MEDIUM
  Termination:  Dead-letter the notification.
                Log: notification.dead_lettered.loop_detected
                Alert notification service.
                Do not retry further.

LOOP CLASS L14 — INFINITE FAILOVER SPIRAL
  Definition:
    CALL_FAILOVER_AGENT initiates failover for a session.
    The failover path also fails. CALL_FAILOVER_AGENT
    initiates the next failover path. That also fails.
    CALL_FAILOVER_AGENT continues cycling through all
    paths — and upon exhaustion, wraps back to Priority 1,
    starting the entire cycle again. This produces an
    infinite loop of failover attempts with no terminal state.
  Indicators:
    failover_attempt events > 3 for same session_id
    within 5 minutes
    AND no failover_recovered event between attempts
    AND final_state != HOLD | TERMINATED after last attempt
  Risk:
    Session never terminates, PSTN costs accumulate on each
    attempt, Jitsi/coturn resources held indefinitely,
    participants unable to exit cleanly
  Severity:     CRITICAL
  Termination:  FORCE SESSION TERMINATION.
                Hard-kill all active call handles.
                Hard-kill Jitsi room.
                Force-release coturn allocations.
                Emit: session.infinite_failover_spiral.terminated
                Write comprehensive forensic audit record.
                Notify all participants.
                Block new session creation for session_id for 5 minutes.

LOOP CLASS L15 — COTURN ALLOCATION LEAK
  Definition:
    A coturn TURN relay allocation was created for a
    participant session. The session ended (normally or
    via failover). The coturn Allocate request was never
    followed by a Refresh expiry or a proper channel close,
    so the coturn server still holds the allocation active.
    The allocation consumes server-side UDP ports and
    bandwidth capacity.
  Indicators:
    coturn active allocations list contains allocation_id
    where parent session_id is in state ENDED | TERMINATED
    AND allocation_age > session_end_at + 120 seconds
  Risk:
    coturn port exhaustion, bandwidth consumption,
    degraded TURN performance for live sessions
  Severity:     MEDIUM
  Termination:  Send coturn admin API: delete_allocation(allocation_id)
                Log: coturn.allocation.leak.cleared
                Update SIP_HEALTH_MONITOR_AGENT capacity metrics.

LOOP CLASS L16 — DUPLICATE ROOM CREATION
  Definition:
    A session_id that already has an active Jitsi room
    triggers a new room creation request — due to a
    race condition on session start, a duplicate HTTP
    request, or a missing idempotency key on the
    room creation API.
  Indicators:
    room_creation events > 1 for same session_id
    within 30 seconds
    OR Jitsi room list contains > 1 room matching
    room_name pattern for same session_id
  Risk:
    Participants split across two rooms, audio routing
    incoherence, GD turn engine operating on wrong room,
    scoring against wrong participant set
  Severity:     CRITICAL
  Termination:  Keep oldest room. Destroy newer duplicate room(s).
                Migrate any participants from duplicate room.
                Log: duplicate_room.terminated
                Lock room_creation for session_id for 60 seconds.
```

---

## ░░ SECTION 4 — DETECTION ENGINE SPECIFICATION ░░

### 4.1 Event Stream Detector (Tier 1 — Real-Time)

```
DETECTOR: EVENT_STREAM_DETECTOR

Input Sources:
  Kafka topic:     call.events
  Kafka topic:     session.events
  Kafka topic:     webhook.events
  Redis pub/sub:   __keyevent@0__:set (session state changes)
  WebSocket telemetry stream

Algorithm:

  Sliding Window Counter (per event type per entity):
    window = RollingTimeWindow(seconds=configurable per loop class)
    counter = increment on each event
    if counter > threshold:
      classify_loop(loop_class)
      execute_detection_response(loop_class, entity_id)

  Event Entity Resolution:
    Each event carries: session_id, participant_id, call_sid
    Agent builds entity_event_map in Redis:
      key:   loop_detector:{loop_class}:{entity_id}
      value: {count, first_seen, last_seen, event_list}
      TTL:   window_duration + 60s

  Idempotency:
    Each detection result carries a detection_id (SHA256 of
    loop_class + entity_id + window_start).
    Duplicate detections within same window are suppressed.
```

### 4.2 State Scan Detector (Tier 2 — Fast Poll, 10s)

```
DETECTOR: STATE_SCAN_DETECTOR

Polling Algorithm:
  Every 10 seconds:

  1. PSTN Ghost Call Scan:
     active_calls = pstn_provider_api.list_active_calls()
     for call in active_calls:
       session = redis.get(session:{call.session_id}:state)
       if session.state in [ENDED, TERMINATED, SCORED]:
         classify_as(L11 — PSTN BILLING LEAK)

  2. SIP Ghost Session Scan:
     active_sip_sessions = redis.scan_pattern("sip:session:*:active")
     for session in active_sip_sessions:
       audio_active = redis.get(sip:{session_id}:{participant_id}:audio_active)
       silence_since = NOW - redis.get(sip:{session_id}:{participant_id}:last_audio_at)
       session_state = redis.get(session:{session_id}:state)
       if not audio_active AND silence_since > 60 AND session_state == ACTIVE:
         classify_as(L8 — GHOST SIP SESSION)

  3. Zombie Jitsi Room Scan:
     jitsi_rooms = jitsi_api.list_rooms()
     for room in jitsi_rooms:
       session_id = extract_session_id(room.name)
       session_state = db.query_session_state(session_id)
       room_age_since_session_end = NOW - session_end_at
       if session_state in [ENDED, TERMINATED] AND room_age_since_session_end > 120:
         classify_as(L7 — ZOMBIE JITSI ROOM)

  4. coturn Allocation Leak Scan:
     allocations = coturn_admin_api.list_allocations()
     for alloc in allocations:
       session_id = extract_session_id(alloc.metadata)
       session_state = redis.get(session:{session_id}:state)
       alloc_age_since_session_end = NOW - session_end_at
       if session_state in [ENDED, TERMINATED] AND alloc_age_since_session_end > 120:
         classify_as(L15 — COTURN ALLOCATION LEAK)
```

### 4.3 Deep Session Scan Detector (Tier 3 — 60s)

```
DETECTOR: DEEP_SESSION_SCAN_DETECTOR

Every 60 seconds:

  1. GD Turn Engine Loop Scan:
     gd_sessions = redis.scan_pattern("session:*:turn_engine:*")
     for session in gd_sessions:
       turn_history = redis.lrange(session:{id}:turn_history, 0, 9)
       consecutive_same = detect_consecutive_duplicates(turn_history)
       if consecutive_same.count > 2:
         classify_as(L6 — GD TURN ENGINE LOOP)

  2. Zombie Dojo Match Scan:
     active_matches = redis.scan_pattern("match:*:state:ACTIVE")
     for match in active_matches:
       last_event_at = redis.get(match:{id}:last_event_at)
       silence_seconds = NOW - last_event_at
       all_heartbeats = redis.get(match:{id}:all_participant_heartbeats)
       if silence_seconds > 300 AND all_heartbeats_stale(all_heartbeats, 300):
         classify_as(L9 — ZOMBIE DOJO MATCH)

  3. Interview Slot Lock Leak Scan:
     etcd_locks = etcd.list_keys(prefix="interview:slot:")
     for lock in etcd_locks:
       acquired_at = lock.metadata.created_at
       renewal_count = lock.metadata.version
       interview_state = db.query_interview_state(lock.slot_id)
       if (NOW - acquired_at > MAX_SESSION_DURATION
           OR renewal_count > MAX_RENEWALS)
          AND interview_state != IN_PROGRESS:
         classify_as(L10 — INTERVIEW SLOT LOCK LEAK)

  4. Scoring Write Loop Scan:
     scoring_attempts = db.query_scoring_attempts_last_5min()
     for attempt in scoring_attempts:
       key = (attempt.session_id, attempt.participant_id, attempt.round_id)
       if attempt_count[key] > 3:
         classify_as(L12 — SCORING ENGINE WRITE LOOP)

  5. Notification Retry Storm Scan:
     retry_queue = notification_service.query_retry_queue()
     for notification in retry_queue:
       key = (notification.id, notification.recipient_id)
       if retry_count[key] > 5:
         classify_as(L13 — NOTIFICATION RETRY STORM)
```

### 4.4 Billing Reconciliation Detector (Tier 4 — 5min)

```
DETECTOR: BILLING_RECONCILIATION_DETECTOR

Every 5 minutes:

  1. PSTN Cost Overrun Check:
     For every active PSTN call in provider billing records:
       cost_per_minute = provider_rate_card[call.destination]
       projected_cost = call.duration_seconds / 60 × cost_per_minute
       session = get_session(call.session_id)
       if session.pstn_budget_cap exceeded:
         emit: billing.pstn.budget_cap.exceeded
         initiate_graceful_termination(call)

  2. SIP Bridge Duration Overrun:
     For every active SIP bridge session:
       bridge_duration = NOW - bridge_activated_at
       session_cap = config.sip_bridge_max_duration_seconds
       if bridge_duration > session_cap:
         classify_as overrun
         emit: billing.sip.duration_overrun
         initiate_graceful_termination(sip_session)

  3. coturn Overbilling Reconciliation:
     Compare: coturn active allocation time vs session lifetime
     If allocation active > session_end_at + 300s:
       Write billing_correction record
       Deduct overbilled time from usage record
       Log: billing.coturn.correction_applied
```

### 4.5 Failover Spiral Detector (Tier 5 — 30s)

```
DETECTOR: FAILOVER_SPIRAL_DETECTOR

Every 30 seconds:

  Query CALL_FAILOVER_AGENT action log for sessions with:
    failover_attempt_count > 3
    AND time_window = last 5 minutes
    AND no failover_state = RECOVERED in log

  For each spiral candidate:
    Verify: session_state != TERMINATED (not already handled)
    Verify: at least one resource (call/room/allocation) still active
    If confirmed spiral:
      classify_as(L14 — INFINITE FAILOVER SPIRAL)
      Execute immediate forced termination protocol
```

---

## ░░ SECTION 5 — TERMINATION PROTOCOLS ░░

### 5.1 Termination Levels

```
TERMINATION LEVEL 1 — GRACEFUL
  Used for:  L7, L8, L9, L13, L15
  Method:
    1. Send formal protocol termination signal
       (SIP BYE, Jitsi room destroy API, etcd lock release,
        notification dead-letter, coturn delete allocation)
    2. Wait for acknowledgement (timeout: 10 seconds)
    3. If acknowledged → log graceful_termination
    4. If not acknowledged within 10s → escalate to Level 2

TERMINATION LEVEL 2 — FORCED
  Used for:  L3, L4, L5, L8(unack), L10, L12
  Method:
    1. Force-close connection / state at platform level
       without waiting for remote acknowledgement
    2. Update Redis/PostgreSQL state immediately
    3. Release all associated resource handles
    4. Log forced_termination with reason

TERMINATION LEVEL 3 — HARD KILL
  Used for:  L1, L6, L11, L14, L16
  Method:
    1. Immediately terminate all associated call legs
       via provider API force-hangup
    2. Immediately destroy Jitsi room (force)
    3. Immediately release all coturn allocations (force)
    4. Immediately clear all Redis keys for session
    5. Immediately release etcd locks
    6. Emit billing protection event
    7. Write immutable forensic audit record
    8. Notify all participants via push + SMS + WebSocket
    9. Block session_id from new resource creation for 5 minutes
    10. Notify CALL_FAILOVER_AGENT: DO_NOT_RETRY
```

### 5.2 Resource Release Checklist (per termination)

```
On any loop termination, the agent MUST execute and log:

  □ Jitsi room destroy API called           → confirmed | skipped (N/A)
  □ coturn allocations released             → count released
  □ Redis session keys cleared              → keys_cleared list
  □ etcd locks released                     → lock_ids list
  □ PSTN call hung up (provider API)        → call_sid confirmed
  □ SIP BYE sent                            → sip_dialog_id confirmed
  □ WebSocket connection closed             → participant_ids list
  □ Scoring state frozen                    → session_id noted
  □ Billing protection event emitted        → event_id noted
  □ Participant notifications sent          → channel (push/SMS/WS)
  □ Immutable audit record written          → audit_log_id returned
  □ CALL_FAILOVER_AGENT notified            → signal_id noted
  □ SIP_HEALTH_MONITOR_AGENT notified       → signal_id noted
  □ Admin Governance Service notified       → if severity CRITICAL
  □ Wazuh SIEM event written                → if security-relevant loop

All items logged. No silent terminations.
```

---

## ░░ SECTION 6 — STATE MACHINE INTEGRATION ░░

### 6.1 Redis Keys Managed by Agent

```
DETECTION WINDOWS:
loop_detector:{loop_class}:{entity_id}
  → {count, first_seen, last_seen, event_ids[]}
  → TTL: window_duration + 60s

TERMINATION LOCKS (prevent concurrent termination):
loop_termination_lock:{session_id}:{loop_class}
  → acquired via etcd for distributed safety
  → TTL: 30s (auto-release if agent crashes)

BLOCK REGISTRY (prevent new resource creation post-termination):
loop_block:{session_id}
  → {reason, blocked_until, terminated_by_agent}
  → TTL: 300s (5 minutes default)

FORENSIC BUFFER (in-memory accumulation before DB write):
loop_forensic:{detection_id}
  → Full event sequence leading to loop detection
  → TTL: 120s (written to PostgreSQL before expiry)

BILLING PROTECTION FLAGS:
billing_protection:{call_sid | session_id}
  → {reason, detected_at, amount_saved_estimate}
  → Written to Billing Service on expiry
```

### 6.2 Coordination Protocol with CALL_FAILOVER_AGENT

```
Signal:  PHONE_CALL_LOOP_DETECTION_AGENT → CALL_FAILOVER_AGENT
Channel: Redis pub/sub: loop_signals:{environment}

Signal Types:
  DO_NOT_RETRY:
    Payload: {session_id, loop_class, reason}
    Meaning: CALL_FAILOVER_AGENT must not initiate any further
             failover attempts for this session.
             All handles are being terminated.

  RESOURCE_RELEASED:
    Payload: {session_id, resource_type, resource_id}
    Meaning: A resource (room, allocation, call) has been
             force-released. CALL_FAILOVER_AGENT can update
             its internal resource registry.

  LOOP_CLEARED:
    Payload: {session_id, loop_class, new_state}
    Meaning: Loop terminated. Session may be in HOLD or TERMINATED.
             CALL_FAILOVER_AGENT may resume normal operation
             if new_state = HOLD.

CALL_FAILOVER_AGENT → PHONE_CALL_LOOP_DETECTION_AGENT:
Channel: Redis pub/sub: failover_signals:{environment}

  FAILOVER_ATTEMPT_LOGGED:
    Payload: {session_id, attempt_number, path_tried}
    Meaning: New failover attempt made. Loop detector increments
             spiral counter for this session.

  FAILOVER_EXHAUSTED:
    Payload: {session_id, total_attempts, duration_seconds}
    Meaning: All failover paths exhausted. Loop detector checks
             for L14 (INFINITE FAILOVER SPIRAL) condition.
```

---

## ░░ SECTION 7 — EVENT BUS CONTRACTS ░░

### 7.1 Events Emitted

```yaml
# AsyncAPI Contracts — PHONE_CALL_LOOP_DETECTION_AGENT

channels:

  loop.detected:
    publish:
      message:
        payload:
          type: object
          properties:
            detection_id:    { type: string }
            loop_class:      { type: string }
            loop_class_name: { type: string }
            severity:        { type: string }
            entity_type:     { type: string }
            entity_id:       { type: string }
            session_id:      { type: string }
            participant_id:  { type: string }
            event_count:     { type: integer }
            window_seconds:  { type: integer }
            detected_at:     { type: string, format: date-time }

  loop.termination.initiated:
    publish:
      message:
        payload:
          type: object
          properties:
            detection_id:       { type: string }
            session_id:         { type: string }
            loop_class:         { type: string }
            termination_level:  { type: integer }
            resources_targeted: { type: array, items: { type: string } }
            initiated_at:       { type: string, format: date-time }

  loop.termination.completed:
    publish:
      message:
        payload:
          type: object
          properties:
            detection_id:       { type: string }
            session_id:         { type: string }
            loop_class:         { type: string }
            resources_released: { type: object }
            duration_ms:        { type: integer }
            completed_at:       { type: string, format: date-time }
            audit_log_id:       { type: string }

  billing.pstn.leak.terminated:
    publish:
      message:
        payload:
          type: object
          properties:
            call_sid:            { type: string }
            session_id:          { type: string }
            overrun_seconds:     { type: integer }
            estimated_cost_saved: { type: number }
            terminated_at:       { type: string, format: date-time }

  billing.pstn.budget_cap.exceeded:
    publish:
      message:
        payload:
          type: object
          properties:
            session_id:   { type: string }
            call_sid:     { type: string }
            cap_seconds:  { type: integer }
            actual_seconds: { type: integer }

  billing.sip.duration_overrun:
    publish:
      message:
        payload:
          type: object
          properties:
            session_id:       { type: string }
            bridge_seconds:   { type: integer }
            cap_seconds:      { type: integer }

  billing.coturn.correction_applied:
    publish:
      message:
        payload:
          type: object
          properties:
            allocation_id:     { type: string }
            session_id:        { type: string }
            overbilled_seconds: { type: integer }

  session.infinite_failover_spiral.terminated:
    publish:
      message:
        payload:
          type: object
          properties:
            session_id:       { type: string }
            attempt_count:    { type: integer }
            duration_seconds: { type: integer }
            forensic_log_id:  { type: string }

  scoring.write.loop.detected:
    publish:
      message:
        payload:
          type: object
          properties:
            session_id:      { type: string }
            participant_id:  { type: string }
            round_id:        { type: string }
            attempt_count:   { type: integer }
            flagged_for:     { type: string }

  match.abandoned.zombie_cleared:
    publish:
      message:
        payload:
          type: object
          properties:
            match_id:         { type: string }
            inactive_seconds: { type: integer }
            participants:     { type: array, items: { type: string } }
            resources_freed:  { type: object }

  interview.slot.lock.released:
    publish:
      message:
        payload:
          type: object
          properties:
            slot_id:         { type: string }
            lock_held_since: { type: string, format: date-time }
            renewal_count:   { type: integer }
            reason:          { type: string }

  loop.block.active:
    publish:
      message:
        payload:
          type: object
          properties:
            session_id:    { type: string }
            blocked_until: { type: string, format: date-time }
            reason:        { type: string }
```

---

## ░░ SECTION 8 — DATABASE SCHEMA ░░

### 8.1 Loop Detection Audit Log

```sql
-- Immutable Loop Detection Audit Log
CREATE TABLE loop_detection_audit_log (
  audit_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  detection_id      TEXT NOT NULL UNIQUE,
  loop_class        TEXT NOT NULL,
  loop_class_name   TEXT NOT NULL,
  severity          TEXT NOT NULL,
  entity_type       TEXT NOT NULL,
  entity_id         TEXT NOT NULL,
  session_id        UUID,
  participant_id    UUID,
  call_sid          TEXT,
  event_count       INTEGER NOT NULL,
  window_seconds    INTEGER NOT NULL,
  event_sequence    JSONB NOT NULL,
  termination_level INTEGER,
  termination_actions JSONB,
  resources_released JSONB,
  billing_impact    JSONB,
  detected_at       TIMESTAMP NOT NULL DEFAULT NOW(),
  terminated_at     TIMESTAMP,
  resolution        TEXT,
  audit_hash        TEXT NOT NULL  -- SHA256(detection_id + session_id + detected_at)
);
-- IMMUTABILITY: NO UPDATE, NO DELETE enforced via RLS policy
-- append-only role used for all writes

-- Loop Frequency Summary (ClickHouse — analytics)
CREATE TABLE loop_frequency_stats (
  stat_date         DATE NOT NULL,
  loop_class        TEXT NOT NULL,
  session_type      TEXT NOT NULL,
  detection_count   INTEGER NOT NULL,
  termination_count INTEGER NOT NULL,
  billing_saved     DECIMAL(10,4),
  avg_detection_ms  INTEGER,
  severity_breakdown JSONB
);

-- Billing Protection Log
CREATE TABLE billing_protection_log (
  protection_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  loop_class          TEXT NOT NULL,
  session_id          UUID,
  call_sid            TEXT,
  provider            TEXT,
  overrun_type        TEXT NOT NULL,
  overrun_seconds     INTEGER,
  estimated_cost_unit DECIMAL(10,6),
  termination_action  TEXT NOT NULL,
  protected_at        TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Ghost Resource Registry
CREATE TABLE ghost_resource_cleared_log (
  clear_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  resource_type     TEXT NOT NULL,
  resource_id       TEXT NOT NULL,
  session_id        UUID,
  ghost_since       TIMESTAMP,
  cleared_at        TIMESTAMP NOT NULL DEFAULT NOW(),
  clear_method      TEXT NOT NULL,
  loop_class        TEXT NOT NULL
);

-- Scoring Loop Flag Registry
CREATE TABLE scoring_loop_flag_registry (
  flag_id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  session_id        UUID NOT NULL,
  participant_id    UUID NOT NULL,
  round_id          TEXT NOT NULL,
  attempt_count     INTEGER NOT NULL,
  flagged_at        TIMESTAMP NOT NULL DEFAULT NOW(),
  status            TEXT NOT NULL DEFAULT 'REQUIRES_MANUAL_REVIEW',
  reviewed_by       UUID,
  reviewed_at       TIMESTAMP,
  resolution_notes  TEXT
);
```

---

## ░░ SECTION 9 — API CONTRACTS ░░

### 9.1 Internal REST API

```yaml
openapi: 3.1.0
info:
  title: PHONE_CALL_LOOP_DETECTION_AGENT Internal API
  version: 1.0.0

paths:

  /loop-detector/status:
    get:
      summary: Current agent status and active detection counts
      responses:
        "200":
          content:
            application/json:
              schema:
                type: object
                properties:
                  agent_state:      { type: string }
                  active_detections: { type: integer }
                  active_blocks:    { type: integer }
                  last_scan_at:     { type: string }
                  scan_tiers:
                    type: object
                    properties:
                      tier1_last_event_at: { type: string }
                      tier2_last_poll_at:  { type: string }
                      tier3_last_scan_at:  { type: string }
                      tier4_last_reconcile_at: { type: string }
                      tier5_last_spiral_check: { type: string }

  /loop-detector/active:
    get:
      summary: List all currently active loop detections
      responses:
        "200":
          content:
            application/json:
              schema:
                type: array
                items:
                  type: object
                  properties:
                    detection_id: { type: string }
                    loop_class:   { type: string }
                    session_id:   { type: string }
                    severity:     { type: string }
                    detected_at:  { type: string }
                    status:       { type: string }

  /loop-detector/session/{session_id}:
    get:
      summary: Loop detection history for a specific session
      responses:
        "200": { description: Detection history for session }

  /loop-detector/audit/{audit_id}:
    get:
      summary: Full forensic audit record for a detection
      responses:
        "200": { description: Immutable audit record }

  /loop-detector/billing/protection-log:
    get:
      summary: Billing protection events in last N hours
      parameters:
        - name: hours
          in: query
          schema: { type: integer, default: 24 }
      responses:
        "200": { description: Billing protection events }

  /loop-detector/ghost-resources:
    get:
      summary: Ghost resources cleared in last N hours
      parameters:
        - name: hours
          in: query
          schema: { type: integer, default: 24 }
      responses:
        "200": { description: Ghost resource clearance log }

  /loop-detector/scoring-flags:
    get:
      summary: Scoring write loops flagged for manual review
      parameters:
        - name: status
          in: query
          schema: { type: string, default: REQUIRES_MANUAL_REVIEW }
      responses:
        "200": { description: Scoring flag list }

  /loop-detector/force-terminate:
    post:
      summary: Manually force-terminate a detected loop
      security: [bearerAuth: []]
      requestBody:
        content:
          application/json:
            schema:
              type: object
              required: [session_id, loop_class, operator_id]
              properties:
                session_id:  { type: string }
                loop_class:  { type: string }
                operator_id: { type: string }
                reason:      { type: string }
      responses:
        "200": { description: Termination executed }
        "403": { description: Insufficient privilege }

  /loop-detector/blocks/{session_id}:
    get:
      summary: Check if session is currently blocked
      responses:
        "200":
          content:
            application/json:
              schema:
                type: object
                properties:
                  blocked:       { type: boolean }
                  reason:        { type: string }
                  blocked_until: { type: string }

  /loop-detector/stats/summary:
    get:
      summary: Loop detection statistics summary
      parameters:
        - name: hours
          in: query
          schema: { type: integer, default: 24 }
      responses:
        "200": { description: Stats by loop class }
```

---

## ░░ SECTION 10 — OBSERVABILITY REQUIREMENTS ░░

### 10.1 Prometheus Metrics

```
# Loop detections
loop_detection_total{loop_class, session_type, severity}
  Counter — total loop detections by class

# Loop terminations
loop_termination_total{loop_class, termination_level, outcome}
  Counter — terminations: SUCCESS | PARTIAL | FAILED

# Detection latency
loop_detection_latency_ms{loop_class, scan_tier}
  Histogram — time from loop occurrence to detection

# Active loop blocks
loop_block_active_count
  Gauge — sessions currently blocked

# Ghost resources
ghost_resource_active_count{resource_type}
  Gauge — active ghost resources (rooms, allocations, calls)

ghost_resource_cleared_total{resource_type, loop_class}
  Counter — total ghost resources cleared

# Billing protection
billing_protection_events_total{protection_type}
  Counter — billing protection events triggered

billing_cost_saved_units_total{provider, loop_class}
  Counter — estimated cost units saved

# PSTN dial loops
pstn_dial_loop_events_total
  Counter — PSTN L1 loop detections

# Scoring loop flags
scoring_loop_flags_pending
  Gauge — scoring flags awaiting manual review

# Failover spirals
failover_spiral_detected_total
  Counter — L14 infinite failover spiral detections

# Scan tier health
loop_scan_tier_last_run_seconds{tier}
  Gauge — seconds since each scan tier last ran

loop_scan_tier_duration_ms{tier}
  Histogram — how long each scan tier takes
```

### 10.2 Grafana Dashboards

```
Dashboard: PHONE_CALL_LOOP_DETECTION_AGENT — Live View

Panel 1:  Loop Detection Rate (by class) — bar chart, last 1h
Panel 2:  Active Ghost Resources — gauge by type
Panel 3:  Billing Protection Events — counter last 24h
Panel 4:  Estimated Cost Saved (PSTN + SIP) — running total
Panel 5:  Active Session Blocks — live count
Panel 6:  PSTN Dial Loop Rate — time series
Panel 7:  Failover Spiral Detections — count last 7d
Panel 8:  GD Turn Engine Loop Events — count last 24h
Panel 9:  Zombie Sessions Cleared — count last 24h
Panel 10: Scoring Write Loop Flags — pending review count
Panel 11: Detection Latency p50/p90 — histogram by tier
Panel 12: Loop Termination Success Rate — percentage
Panel 13: Interview Lock Leaks Cleared — count last 24h
Panel 14: Scan Tier Health — last run time per tier
```

### 10.3 Alerts

```
ALERT: PstnDialLoopDetected
  condition: loop_detection_total{loop_class="L1"} increase > 1
  severity: critical
  notify: on-call + ops + billing

ALERT: GdTurnEngineLoopDetected
  condition: loop_detection_total{loop_class="L6"} increase > 1
  severity: critical
  notify: on-call + ops + Slack

ALERT: InfiniteFailoverSpiralDetected
  condition: failover_spiral_detected_total increase > 1
  severity: CRITICAL
  notify: on-call + ops + PagerDuty + CALL_FAILOVER_AGENT

ALERT: GhostResourcesAccumulating
  condition: ghost_resource_active_count{resource_type="jitsi_room"} > 10
  severity: warning
  notify: ops channel

ALERT: BillingLeakDetected
  condition: billing_protection_events_total{protection_type="pstn_leak"} increase > 1
  severity: critical
  notify: on-call + billing + ops

ALERT: ScoringLoopFlagsBacklog
  condition: scoring_loop_flags_pending > 5
  severity: warning
  notify: ops + admin governance

ALERT: ScanTierStale
  condition: loop_scan_tier_last_run_seconds{tier="tier2"} > 30
  severity: warning
  notify: ops channel (agent may be unhealthy)

ALERT: DuplicateRoomCreationDetected
  condition: loop_detection_total{loop_class="L16"} increase > 1
  severity: critical
  notify: on-call + ops (scoring integrity risk)
```

---

## ░░ SECTION 11 — SECURITY CONTROLS ░░

```
CONTROL SC-1: TERMINATION AUTHORITY
  Only PHONE_CALL_LOOP_DETECTION_AGENT service account
  may call force-terminate APIs on:
    Jitsi room destroy
    coturn delete allocation
    PSTN provider hang-up
    etcd lock force release
  No other service may call these APIs directly
  without going through this agent.

CONTROL SC-2: FORENSIC AUDIT IMMUTABILITY
  loop_detection_audit_log:
    → No UPDATE permitted via RLS
    → No DELETE permitted (including admin)
    → Append-only role used for all writes
    → SHA256 integrity hash on every row
    → Tamper attempt logged to Wazuh SIEM

CONTROL SC-3: BILLING PROTECTION SIGNING
  Every billing_protection event carries:
    → detection_id (links to audit log)
    → operator signature (agent service account key)
  Billing Service rejects unsigned protection events.

CONTROL SC-4: BLOCK REGISTRY TAMPER PROTECTION
  loop_block:{session_id} Redis keys:
    → Writeable only by this agent's service account
    → Other services may READ (to check if blocked)
    → Other services may NOT WRITE or DELETE
    → Enforced via Redis ACL rules

CONTROL SC-5: WAZUH SIEM REPORTING
  Wazuh events generated for:
    L1 (PSTN Dial Loop) — potential abuse or misconfiguration
    L14 (Failover Spiral) — platform stability event
    L16 (Duplicate Room) — race condition or attack
    Any loop where entity_id has > 3 detections in 1 hour
      → possible malicious client

CONTROL SC-6: SCORING FLAG ACCESS CONTROL
  scoring_loop_flag_registry:
    → Only Admin Governance Service may review/resolve
    → PHONE_CALL_LOOP_DETECTION_AGENT writes only
    → Scoring Engine reads to check block status
    → No candidate or recruiter access
```

---

## ░░ SECTION 12 — PARTICIPANT NOTIFICATIONS ░░

### 12.1 Notification Templates for Loop Terminations

```
NOTIFY N1 — PSTN DIAL LOOP CLEARED (to participant)
  Channel: Push + SMS
  Template:
    "Your Ecoskiller session reconnect encountered a repeated
     connection issue. Your session has been stabilised.
     Please re-enter the session from the app."

NOTIFY N2 — GD TURN ENGINE CORRECTED (to all GD participants)
  Channel: WebSocket in-session overlay
  Template:
    "[SESSION CORRECTED] A turn order issue was detected and
     corrected. The session is continuing from the next speaker."

NOTIFY N3 — SESSION TERMINATED (loop exhaustion)
  Channel: Push + SMS + Email
  Template:
    "Your session {session_type} (ID: {session_id}) was terminated
     due to a persistent connectivity loop. Your participation up to
     this point has been recorded. Please contact support if you
     believe this was in error. Reference: {audit_log_id}"

NOTIFY N4 — ZOMBIE DOJO MATCH CLEARED (to participants)
  Channel: Push + Email
  Template:
    "Your Dojo Match (ID: {match_id}) was inactive for an extended
     period and has been closed. You may start a new match now."

NOTIFY N5 — INTERVIEW SLOT UNLOCKED (to candidate + recruiter)
  Channel: Email + Push
  Template (candidate):
    "The interview slot you had reserved is now available.
     Please rebook at your earliest convenience."
  Template (recruiter):
    "An interview slot lock was released due to inactivity.
     The slot is now available for scheduling."

NOTIFY N6 — SCORING REVIEW FLAG (to Admin Governance only)
  Channel: Ops Console notification
  Template:
    "[SCORING REVIEW REQUIRED] A scoring write loop was detected
     for session {session_id}, participant {participant_id},
     round {round_id}. Manual review required.
     Flag ID: {flag_id}"
```

---

## ░░ SECTION 13 — INTERN EXECUTION GUIDE ░░

### 13.1 Local Setup

```bash
# Step 1: Navigate to agent service
cd /backend/services/phone_call_loop_detection_agent/

# Step 2: Install dependencies
pip install -r requirements.txt --break-system-packages

# Step 3: Set up environment
cp .env.example .env

# Step 4: Required environment variables
REDIS_URL=redis://localhost:6379
POSTGRES_URL=postgresql://postgres:postgres@localhost:5432/ecoskiller
CLICKHOUSE_URL=http://localhost:8123
KAFKA_BROKER=localhost:9092
VAULT_ADDR=http://localhost:8200
VAULT_TOKEN=<dev-token>
PROMETHEUS_PORT=9104

# Jitsi admin API
JITSI_ADMIN_URL=http://jitsi-vb-01:8080
JITSI_ADMIN_SECRET=<from vault>

# coturn admin API
COTURN_ADMIN_URL=http://coturn-01:8081
COTURN_ADMIN_SECRET=<from vault>

# etcd
ETCD_ENDPOINTS=http://localhost:2379

# PSTN provider (for billing reconciliation)
PSTN_PROVIDER=twilio
PSTN_ACCOUNT_SID=<from vault>
PSTN_AUTH_TOKEN=<from vault>

# Thresholds (configurable)
PSTN_DIAL_LOOP_WINDOW_SECONDS=120
PSTN_DIAL_LOOP_THRESHOLD=3
GHOST_SIP_SILENCE_THRESHOLD_SECONDS=60
ZOMBIE_MATCH_INACTIVITY_THRESHOLD_SECONDS=300
FAILOVER_SPIRAL_ATTEMPT_THRESHOLD=3
FAILOVER_SPIRAL_WINDOW_SECONDS=300
SESSION_BLOCK_DURATION_SECONDS=300
PSTN_BUDGET_CAP_SECONDS=300

# Step 5: Run the agent
uvicorn main:app --reload --port 8022

# Step 6: Verify
curl http://localhost:8022/loop-detector/status
# Expected:
# {
#   "agent_state": "RUNNING",
#   "active_detections": 0,
#   "active_blocks": 0
# }
```

### 13.2 Simulating Loop Detections

```bash
# Simulate PSTN Dial Loop (L1) — publish 3 events in 30s
# (Use test Kafka producer or Redis pub/sub mock)

# Check active detections
curl http://localhost:8022/loop-detector/active

# Check billing protection log
curl "http://localhost:8022/loop-detector/billing/protection-log?hours=1"

# Check ghost resources
curl "http://localhost:8022/loop-detector/ghost-resources?hours=1"

# Check scoring flags pending review
curl http://localhost:8022/loop-detector/scoring-flags

# Check a session for blocks
curl http://localhost:8022/loop-detector/blocks/test-session-001

# View full audit record
curl http://localhost:8022/loop-detector/audit/{audit_id}

# Manual force-terminate (ops_admin role required)
curl -X POST http://localhost:8022/loop-detector/force-terminate \
  -H "Authorization: Bearer <ops_admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "session_id": "test-session-001",
    "loop_class": "L7",
    "operator_id": "ops-intern-01",
    "reason": "Manual cleanup during testing"
  }'
```

### 13.3 Expected Success Outputs

```
Service running:              /loop-detector/status → agent_state = RUNNING
Tier 2 scan active:           scan_tiers.tier2_last_poll_at within 15s
Prometheus metrics visible:   curl localhost:9104/metrics | grep loop_detection
No active ghost resources:    ghost_resource_active_count = 0 (clean env)
No active blocks:             active_blocks = 0 (clean env)
Audit log table created:      loop_detection_audit_log exists in PostgreSQL
```

---

## ░░ SECTION 14 — ENFORCEMENT RULES ░░

```
RULE E1:
  PHONE_CALL_LOOP_DETECTION_AGENT must be running before any
  voice session (GD, Interview, Dojo) is permitted to start.
  Agent health check not responding
  → BLOCK all new voice session creation.
  → Report: LOOP_DETECTION_AGENT_ABSENT to Admin Governance.

RULE E2:
  No PSTN outbound call may be initiated more than
  MAX_PSTN_DIAL_ATTEMPTS (default: 3) times for the same
  participant in any 2-minute window.
  CALL_FAILOVER_AGENT must check loop_block before dialing.

RULE E3:
  All loop terminations must produce an immutable audit record.
  Silent terminations (no audit record) are forbidden.
  Missing audit record
  → STOP further termination attempts for that session.
  → Alert Admin Governance Service.

RULE E4:
  Scoring write loops (L12) must never be silently retried.
  All L12 detections must produce a scoring_loop_flag_registry
  entry and block further automatic scoring retries for that tuple.

RULE E5:
  PSTN billing leak (L11) and billing overrun events must be
  emitted to Billing Service within 60 seconds of detection.
  Billing Service must receive the correction record before
  the next billing cycle runs.

RULE E6:
  GD Turn Engine Loop (L6) termination must include a
  turn_override_event in the immutable session audit log.
  The override event must reference the detection_id.

RULE E7:
  Duplicate Room Creation (L16) must result in the
  NEWER room being destroyed. The OLDER room is canonical.
  If creation timestamps are identical, the room with the
  lower UUID lexicographic sort is canonical.

RULE E8:
  The agent must expose /loop-detector/status for Kubernetes
  readiness probe. If agent_state is not RUNNING for > 30s,
  Kubernetes must restart the pod.

RULE E9:
  The agent operates under ZERO TOLERANCE for silent billing
  leaks. Every detected PSTN billing leak (L11) must trigger
  a provider hang-up API call within 15 seconds of detection.
  15-second SLA is mandatory.

RULE E10:
  This agent has NO SCORING AUTHORITY.
  It may freeze scoring state and flag for review.
  It may NOT modify score values.
  It may NOT mark a participant as passed or failed.
```

---

## ░░ SECTION 15 — RELATIONSHIP TO ECOSKILLER SYSTEM LAWS ░░

```
Governed by and compliant with:

R10 — Security Policies
  Vault credentials, Wazuh SIEM, immutable audit,
  signed billing protection events.

R13 — Billing & Subscription Service
  Billing leak detection, cost-cap enforcement,
  billing correction records, budget protection events.

R16 — Operations
  Monitoring dashboards, alert rules, agent observability.

R21 — Internal Operations Console
  /loop-detector/* endpoints exposed under:
  "System Operations → Loop & Ghost Detection"
  Scoring flags visible to Admin Governance.
  Billing protection log visible to Billing Ops.

Section VIII — OBSERVABILITY (NON-OPTIONAL)
  Prometheus metrics, Loki logs, OpenTelemetry traces —
  all mandatory, all implemented.

Section IX — SECURITY BASELINE
  Immutable audit logs, rate limits, signed tokens,
  Wazuh integration, RLS enforcement.

R60 — Long-Term Archival
  loop_detection_audit_log retained per retention policy.
  Minimum 3 years (billing disputes require audit trail).

Scoring Engine (Service #9 — LOCKED)
  L12 detection flags are read by Scoring Engine before
  any score write retry. Scoring Engine must honour flags.

CALL_FAILOVER_AGENT v1.0-ANTIGRAVITY
  Receives DO_NOT_RETRY signals.
  Must honour LOOP BLOCK before initiating new failover.

SIP_HEALTH_MONITOR_AGENT v1.0-ANTIGRAVITY
  Receives RESOURCE_RELEASED signals to update
  capacity metrics for Jitsi and coturn.
```

---

## ░░ SECTION 16 — AGENT TRIO RELATIONSHIP ░░

```
The three PSTN & Bridge agents form an integrated system:

┌───────────────────────────────────────────────────────────┐
│              PSTN & BRIDGE AGENT TRIO                     │
│                                                           │
│  SIP_HEALTH_MONITOR_AGENT                                 │
│  "I watch. I measure. I predict."                         │
│  → Continuous health surveillance                         │
│  → Signals degradation BEFORE failure                     │
│  → Feeds CALL_FAILOVER_AGENT with pre-failure intel       │
│                         │                                 │
│                         ▼                                 │
│  CALL_FAILOVER_AGENT                                      │
│  "I catch. I reroute. I recover."                         │
│  → Reacts to failures and degradation signals             │
│  → Executes the failover path chain                       │
│  → Protects session continuity                            │
│                         │                                 │
│                         ▼                                 │
│  PHONE_CALL_LOOP_DETECTION_AGENT                          │
│  "I terminate. I protect. I audit."                       │
│  → Detects pathological loops and ghost sessions          │
│  → Terminates zombie resources                            │
│  → Protects billing and scoring integrity                 │
│  → Prevents failover from spiralling                      │
│                                                           │
│  All three must be deployed together.                     │
│  None operates without the others.                        │
│  Together they form: ANTIGRAVITY VOICE INFRASTRUCTURE     │
└───────────────────────────────────────────────────────────┘
```

---

## ░░ SECTION 17 — ANTIGRAVITY SEAL ░░

```
╔══════════════════════════════════════════════════════════════════════╗
║    PHONE_CALL_LOOP_DETECTION_AGENT — ANTIGRAVITY PRODUCTION SEAL    ║
╠══════════════════════════════════════════════════════════════════════╣
║  Agent:               PHONE_CALL_LOOP_DETECTION_AGENT                ║
║  Version:             v1.0-ANTIGRAVITY                               ║
║  System:              ECOSKILLER UNIFIED SAAS                        ║
║  Loop Classes:        L1 through L16 (fully classified)              ║
║  Scan Tiers:          T1(stream)·T2(10s)·T3(60s)·T4(5min)·T5(30s)  ║
║  Termination Levels:  L1(Graceful)·L2(Forced)·L3(Hard Kill)         ║
║  Billing Protection:  PSTN Leak SLA = 15 seconds MANDATORY           ║
║  Scoring Authority:   NONE — Flag only, no score modification        ║
║  Audit Trail:         IMMUTABLE — SHA256 hashed, append-only         ║
║  Block Registry:      Distributed, tamper-protected via Redis ACL    ║
║  Ghost Resource Scan: Jitsi·coturn·SIP·PSTN·Redis·etcd              ║
║  Companion Agents:                                                   ║
║    CALL_FAILOVER_AGENT v1.0-ANTIGRAVITY (required)                  ║
║    SIP_HEALTH_MONITOR_AGENT v1.0-ANTIGRAVITY (required)             ║
║  Mutation Policy:     ADD-ONLY via version bump                      ║
║  Interpretation:      NONE PERMITTED                                 ║
╠══════════════════════════════════════════════════════════════════════╣
║  Absence of this agent in any voice infrastructure deployment        ║
║  → STOP VOICE SESSION LAUNCH                                        ║
║  → REPORT: LOOP_DETECTION_AGENT_ABSENT                              ║
║  → NO SESSION COMPLETION CLAIM PERMITTED                            ║
║                                                                      ║
║  PSTN billing leak detected without 15s termination SLA             ║
║  → BILLING GOVERNANCE VIOLATION                                     ║
║  → REPORT TO ADMIN GOVERNANCE SERVICE                               ║
║                                                                      ║
║  Deployed without both companion agents                              ║
║  → INCOMPLETE ANTIGRAVITY DEPLOYMENT                                ║
║  → NO VOICE INFRASTRUCTURE COMPLETION CLAIM PERMITTED               ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

## ░░ DOCUMENT LOCK ░░

```
Document Status:    SEALED
File:               PHONE_CALL_LOOP_DETECTION_AGENT__PSTN_BRIDGE__ECOSKILLER.md
Mutation Policy:    Add-Only — No line may be removed or altered
Version:            v1.0-ANTIGRAVITY
Authority:          Human declaration only
AI Generation Date: 2026-03-04
Parent System:      ECOSKILLER MASTER EXECUTION PROMPT v12.0+
Parent Reference:   Section H → Service #7 (Voice GD Orchestrator)
                    Section H → Service #9 (Scoring Engine — LOCKED)
                    Section H → Service #13 (Billing & Subscription)
                    Section H → Service #14 (Admin Governance)
                    Section VIII → OBSERVABILITY (NON-OPTIONAL)
                    Section IX → SECURITY BASELINE
Companion Agents:
  CALL_FAILOVER_AGENT v1.0-ANTIGRAVITY (required, must co-deploy)
  SIP_HEALTH_MONITOR_AGENT v1.0-ANTIGRAVITY (required, must co-deploy)
Governed By:        R10·R13·R16·R21·R60·Section VIII·Section IX
```

---

*END OF PHONE_CALL_LOOP_DETECTION_AGENT SEALED PROMPT*
