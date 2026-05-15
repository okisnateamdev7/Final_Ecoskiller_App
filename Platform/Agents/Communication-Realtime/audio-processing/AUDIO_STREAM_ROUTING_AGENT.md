# AUDIO_STREAM_ROUTING_AGENT
## ECOSKILLER — ANTIGRAVITY AUDIO PROCESSING SUBSYSTEM
### Status: FINAL · LOCKED · SEALED · DETERMINISTIC
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only
### Agent Version: v1.0.0

---

> **SEAL DECLARATION**
> This document is a fully locked, self-contained agent prompt for the
> AUDIO_STREAM_ROUTING_AGENT component within the Ecoskiller Audio Processing layer.
> No clause may be removed, weakened, reworded, or reinterpreted.
> Any modification without version bump → INVALID EXECUTION.
> Bound to: ECOSKILLER MASTER EXECUTION PROMPT v12.0 (UNIFIED)
> Bound to: Voice GD Orchestrator (Section V, Core Microservice #7)
> Bound to: AUTOMATED VOICE GROUP DISCUSSION SYSTEM (Jitsi + Rule-Driven Orchestration)

---

## SECTION 1 — AGENT IDENTITY

```
Agent Name:         AUDIO_STREAM_ROUTING_AGENT
System Context:     Ecoskiller Antigravity Audio Processing Layer
Domain:             realtime (Kubernetes namespace)
Parent Service:     Voice GD Orchestrator
Execution Model:    Deterministic State Machine — NO AI JUDGMENT
Routing Mode:       Rule-driven, token-gated, SFU-mediated
Media Protocol:     WebRTC (SRTP encrypted)
SFU Backend:        Self-hosted Jitsi Videobridge
Signaling Channel:  WebSocket (backend → frontend command bus)
State Store:        Redis (deterministic, single source of truth)
Database:           PostgreSQL (session metadata, participant mapping, audit logs)
```

---

## SECTION 2 — AGENT PURPOSE (NON-NEGOTIABLE)

The AUDIO_STREAM_ROUTING_AGENT is the enforcement layer between
the Voice GD Orchestrator state machine and the Jitsi SFU audio infrastructure.

Its singular mandate:

```
At any instant in time, exactly ONE participant's microphone is open.
All others are forcibly muted.
No exception exists.
No negotiation exists.
No participant self-manages their microphone state.
```

This agent replaces:
- The human moderator
- The human timekeeper
- The human enforcer
- Any AI speech evaluator

It does NOT replace:
- The Jitsi Videobridge (audio routing hardware)
- The WebRTC protocol (audio transport)
- The Redis state machine (GD session state)

---

## SECTION 3 — SYSTEM BOUNDARY (LOCKED)

### 3.1 What the Agent OWNS

| Responsibility                    | Owner                        |
|-----------------------------------|------------------------------|
| Token grant                       | AUDIO_STREAM_ROUTING_AGENT   |
| Force unmute active speaker       | AUDIO_STREAM_ROUTING_AGENT   |
| Force mute all others             | AUDIO_STREAM_ROUTING_AGENT   |
| Countdown timer enforcement       | AUDIO_STREAM_ROUTING_AGENT   |
| Interrupt attempt detection       | AUDIO_STREAM_ROUTING_AGENT   |
| Token expiry → auto-mute          | AUDIO_STREAM_ROUTING_AGENT   |
| Mic-open duration logging         | AUDIO_STREAM_ROUTING_AGENT   |
| Network drop handling             | AUDIO_STREAM_ROUTING_AGENT   |
| Silence-during-token logging      | AUDIO_STREAM_ROUTING_AGENT   |

### 3.2 What the Agent DOES NOT OWN

| Out-of-scope responsibility       | True Owner                   |
|-----------------------------------|------------------------------|
| Audio encoding / decoding         | WebRTC (browser)             |
| Stream routing / mixing           | Jitsi Videobridge (SFU)      |
| NAT traversal                     | coturn (TURN/STUN)           |
| Session state machine             | Redis + GD Orchestrator      |
| Scoring calculation               | Scoring Engine (Service #9)  |
| Room creation                     | Voice GD Orchestrator        |
| Participant authentication        | Auth Service (Service #1)    |
| Audit log persistence             | PostgreSQL                   |

### 3.3 CRITICAL PRINCIPLE (INVIOLABLE)

```
The agent never touches raw audio.
The agent never evaluates speech content.
The agent only controls the PERMISSION to speak.
```

---

## SECTION 4 — AGENT ARCHITECTURE

### 4.1 Technology Stack (Locked — inherits from ECOSKILLER R1)

```
Runtime:            Node.js (preferred) or Spring Boot
State Engine:       Redis (deterministic state machine, SETEX + pub/sub)
Command Channel:    WebSocket (backend → frontend)
Media Control API:  Jitsi Meet IFrame API + Jitsi REST API
Persistence:        PostgreSQL (audit, session metadata)
Event Bus:          Apache Kafka (gd.turn.started / gd.turn.ended / gd.completed)
Observability:      Prometheus metrics + Loki logs + OpenTelemetry traces
Security:           Signed media tokens (JWT, short-lived, per-participant)
```

### 4.2 Kubernetes Deployment Namespace

```
Namespace:          realtime
Service Name:       audio-stream-routing-agent
Port:               8080 (WebSocket + REST health)
Replicas:           ≥ 2 (session-affinity required)
Session Affinity:   Redis-backed (not pod-local)
```

### 4.3 Dependency Graph

```
AUDIO_STREAM_ROUTING_AGENT
    ├── READS FROM       → Redis (GD state machine key)
    ├── WRITES TO        → Redis (mute state, timer state, interrupt count)
    ├── COMMANDS VIA     → WebSocket (frontend mute/unmute events)
    ├── CALLS            → Jitsi REST API (force mute/unmute)
    ├── EMITS EVENTS TO  → Kafka (gd.turn.started, gd.turn.ended, gd.mic.interrupted)
    └── WRITES AUDIT TO  → PostgreSQL (participant_mic_log, session_audit)
```

---

## SECTION 5 — ANTIGRAVITY ROUTING ENGINE (CORE)

### 5.1 Philosophy of Antigravity Routing

Standard audio conferencing allows gravity — the natural pull of dominant speakers.
The ANTIGRAVITY engine neutralizes this:

```
Antigravity Rule:
    Dominance is structurally impossible.
    Interruption is structurally impossible.
    Silence penalty is structurally logged.
    Fairness is structurally enforced.
```

No speaker can seize the floor.
No speaker can hold the floor beyond their token.
Every speaker gets identical structural opportunity.

### 5.2 Speaking Token Model

```
Token = {
    session_id:     UUID,
    round_id:       UUID,
    participant_id: UUID,
    allocated_ms:   integer (duration in milliseconds),
    granted_at:     timestamp (UTC),
    expires_at:     timestamp (UTC),
    round_type:     ENUM [INTRO | REBUTTAL | OPEN | CONCLUSION],
    status:         ENUM [ACTIVE | EXPIRED | FORFEITED | INTERRUPTED]
}
```

Token issuance is deterministic:
- `sort_by_join_time()` — primary ordering
- Ties broken by `participant_id` alphabetical order
- No human decision in ordering

### 5.3 Round Duration Table (Default — Human-Configurable Pre-Session)

| Round Type   | Duration   | Behavior                                        |
|--------------|------------|-------------------------------------------------|
| INTRO        | 60 seconds | Structured turn-based, mute enforced            |
| REBUTTAL     | 30 seconds | Structured turn-based, mute enforced            |
| OPEN         | Variable   | All unmuted, interrupts still logged            |
| CONCLUSION   | 45 seconds | Structured turn-based, mute enforced            |

Duration values are stored in PostgreSQL `gd_session_config`.
They are read once at session initialization.
They cannot be modified mid-session.

---

## SECTION 6 — DETERMINISTIC ROUTING ALGORITHM (SEALED)

### 6.1 Pre-Session Initialization

```
FUNCTION initialize_routing(session_id):

    participants = QUERY PostgreSQL:
        SELECT participant_id, display_name, join_timestamp
        FROM gd_participants
        WHERE session_id = :session_id
          AND join_status = 'ADMITTED'
        ORDER BY join_timestamp ASC, participant_id ASC

    SET Redis:
        KEY = "gd:{session_id}:participant_queue"
        VALUE = ordered list of participant_ids
        TTL = session_max_duration_seconds + 300

    SET Redis:
        KEY = "gd:{session_id}:routing_state"
        VALUE = "INITIALIZED"

    EMIT Kafka event: gd.routing.initialized
```

### 6.2 Token Grant Cycle (Per Turn)

```
FUNCTION grant_next_token(session_id, round_id, round_type):

    // Fetch queue state atomically
    REDIS ATOMIC:
        participant_id = LPOP "gd:{session_id}:participant_queue"
        IF participant_id IS NULL:
            → END ROUND → call finalize_round(session_id, round_id)
            RETURN

    duration_ms = FETCH round_type duration from session_config

    token = {
        session_id,
        round_id,
        participant_id,
        allocated_ms:  duration_ms,
        granted_at:    NOW_UTC(),
        expires_at:    NOW_UTC() + duration_ms,
        round_type,
        status:        "ACTIVE"
    }

    // Persist token
    SET Redis:
        KEY = "gd:{session_id}:active_token"
        VALUE = JSON(token)
        TTL = (duration_ms / 1000) + 5  // 5s grace buffer

    // Execute mute enforcement
    CALL force_mute_all(session_id)
    CALL force_unmute(session_id, participant_id)

    // Push WebSocket commands to all clients
    BROADCAST WebSocket:
        {
            "event":          "TOKEN_GRANTED",
            "session_id":     session_id,
            "round_id":       round_id,
            "speaker_id":     participant_id,
            "duration_ms":    duration_ms,
            "expires_at":     token.expires_at,
            "round_type":     round_type
        }

    // Start expiry countdown in Redis
    SET Redis:
        KEY = "gd:{session_id}:token_timer"
        VALUE = expires_at (epoch ms)
        TTL = (duration_ms / 1000) + 10

    // Start server-side expiry watcher
    SCHEDULE token_expiry_handler(session_id, token.expires_at)

    EMIT Kafka: gd.turn.started {session_id, round_id, participant_id, round_type}
    LOG PostgreSQL: participant_mic_log (OPEN event)
```

### 6.3 Token Expiry Handler

```
FUNCTION token_expiry_handler(session_id, expires_at):

    WAIT until NOW_UTC() >= expires_at

    token = GET Redis "gd:{session_id}:active_token"

    IF token IS NULL OR token.status != "ACTIVE":
        RETURN  // Already handled (forfeit or network drop)

    // Measure actual mic-open duration
    actual_open_ms = NOW_UTC() - token.granted_at

    // Log silence if participant did not speak
    IF actual_open_ms < MIN_SPEECH_THRESHOLD_MS:
        LOG PostgreSQL:
            event_type = "SILENCE_DURING_TOKEN"
            participant_id = token.participant_id

    // Mute the active speaker
    CALL force_mute(session_id, token.participant_id)

    // Update token status
    UPDATE Redis token: status = "EXPIRED"

    BROADCAST WebSocket:
        { "event": "TOKEN_EXPIRED", "speaker_id": token.participant_id }

    EMIT Kafka: gd.turn.ended {session_id, participant_id, actual_open_ms}
    LOG PostgreSQL: participant_mic_log (CLOSE event, duration = actual_open_ms)

    // Grant next token
    CALL grant_next_token(session_id, round_id, round_type)
```

### 6.4 Force Mute All — Jitsi API Invocation

```
FUNCTION force_mute_all(session_id):

    participants = GET Redis "gd:{session_id}:participant_queue" (full snapshot)
    active_token = GET Redis "gd:{session_id}:active_token"
    active_participant = active_token.participant_id

    FOR EACH participant_id IN participants:
        IF participant_id != active_participant:
            CALL Jitsi REST API:
                POST /colibri/conferences/{room_name}/participants/{jitsi_id}/mute
                Body: { "muted": true }

            SEND WebSocket to participant frontend:
                { "event": "FORCE_MUTED", "participant_id": participant_id }

    LOG Redis:
        KEY = "gd:{session_id}:mute_state:{participant_id}"
        VALUE = "MUTED"
```

### 6.5 Force Unmute Active Speaker — Jitsi API Invocation

```
FUNCTION force_unmute(session_id, participant_id):

    jitsi_participant_id = QUERY PostgreSQL:
        SELECT jitsi_participant_id
        FROM gd_jitsi_mapping
        WHERE session_id = :session_id
          AND participant_id = :participant_id

    CALL Jitsi REST API:
        POST /colibri/conferences/{room_name}/participants/{jitsi_id}/mute
        Body: { "muted": false }

    SEND WebSocket to participant frontend:
        { "event": "FORCE_UNMUTED", "participant_id": participant_id }

    SET Redis:
        KEY = "gd:{session_id}:mute_state:{participant_id}"
        VALUE = "UNMUTED"
```

### 6.6 Interrupt Attempt Detection

```
FUNCTION detect_interrupt_attempt(session_id, attempting_participant_id):

    active_token = GET Redis "gd:{session_id}:active_token"

    IF attempting_participant_id != active_token.participant_id:
        // Unauthorized mic activation detected
        CALL force_mute(session_id, attempting_participant_id)

        INCR Redis:
            KEY = "gd:{session_id}:interrupt_count:{attempting_participant_id}"
            TTL = session_max_duration_seconds

        LOG PostgreSQL:
            event_type = "INTERRUPT_ATTEMPT"
            participant_id = attempting_participant_id
            timestamp = NOW_UTC()

        BROADCAST WebSocket:
            {
                "event":          "INTERRUPT_BLOCKED",
                "participant_id": attempting_participant_id
            }

        EMIT Kafka: gd.mic.interrupted {session_id, attempting_participant_id}
```

---

## SECTION 7 — OPEN DISCUSSION ROUND ROUTING

After all structured rounds complete:

```
FUNCTION activate_open_discussion(session_id):

    // Unmute all participants simultaneously
    FOR EACH participant_id IN all_admitted_participants:
        CALL Jitsi REST API: force_unmute(participant_id)

    SET Redis:
        KEY = "gd:{session_id}:routing_state"
        VALUE = "OPEN_DISCUSSION"

    START speaking_time_tracker(session_id) // per-participant mic-open measurement
    START interrupt_detector(session_id)    // dominance penalty still active
    START open_round_timer(session_id, configured_open_round_duration_ms)

    BROADCAST WebSocket:
        { "event": "OPEN_DISCUSSION_STARTED", "session_id": session_id }

FUNCTION speaking_time_tracker(session_id):
    // WebRTC audio level events → detect which participant is actively speaking
    // Log to Redis: "gd:{session_id}:open_speaking_log:{participant_id}"
    // Data: cumulative_ms_spoken, peak_dominance_window

FUNCTION dominance_penalty_check(session_id):
    // If any participant exceeds DOMINANCE_THRESHOLD_PERCENT of open round total time
    // LOG to PostgreSQL: open_round_dominance_flag
    // Applied as negative weight in Scoring Engine
```

---

## SECTION 8 — FAILURE HANDLING (NON-NEGOTIABLE)

| Failure Event          | Agent Action                                              |
|------------------------|----------------------------------------------------------|
| Network drop (participant) | Token FORFEITED. Queue advances. No retry.           |
| Mic hardware failure   | Token marked SILENT. Queue advances. No retry.            |
| Jitsi API timeout      | Retry once (500ms). If fails → log ENFORCEMENT_FAILURE.  |
| Participant exits early | Remove from queue. All future tokens skip them.          |
| Rejoin attempt         | DENIED. Status logged. Queue not restored.               |
| Redis unavailable      | HALT SESSION. Emit gd.session.halted. Alert operations.  |
| WebSocket disconnect   | Re-issue mute command on reconnect via session state.    |
| Token timer drift      | Redis SETEX is authoritative. Browser timer is display only.|

```
No retries for participant failures.
No discretionary logic.
No human override without Admin Governance Service (Service #14) action.
```

---

## SECTION 9 — DATA CAPTURE CONTRACT (LOCKED)

### 9.1 Captured — PostgreSQL Tables

```sql
-- participant_mic_log
CREATE TABLE participant_mic_log (
    log_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id      UUID NOT NULL,
    participant_id  UUID NOT NULL,
    round_id        UUID NOT NULL,
    round_type      TEXT NOT NULL,
    event_type      TEXT NOT NULL,  -- OPEN | CLOSE | INTERRUPT | SILENCE | FORFEIT
    mic_open_ms     INTEGER,
    timestamp       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- session_routing_audit
CREATE TABLE session_routing_audit (
    audit_id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id      UUID NOT NULL,
    event_type      TEXT NOT NULL,  -- TURN_GRANTED | TURN_EXPIRED | OPEN_STARTED | SESSION_END
    participant_id  UUID,
    payload         JSONB,
    timestamp       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- interrupt_log
CREATE TABLE interrupt_log (
    interrupt_id    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id      UUID NOT NULL,
    participant_id  UUID NOT NULL,
    interrupt_count INTEGER NOT NULL DEFAULT 0,
    timestamp       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

### 9.2 NOT Captured — Absolute Prohibition

```
Voice recordings:                  NEVER STORED
Speech content / transcripts:      NEVER STORED
Tone or emotion analysis:          NEVER PERFORMED
Accent or language detection:      NEVER PERFORMED
AI inference on speech:            NEVER PERFORMED
Raw audio bytes:                   NEVER TOUCHED BY BACKEND
```

Violation of the above → IMMEDIATE SECURITY INCIDENT.

---

## SECTION 10 — SCORING DATA OUTPUT CONTRACT

The AUDIO_STREAM_ROUTING_AGENT produces exactly the following numeric outputs
for consumption by the Scoring Engine (Service #9):

```json
{
  "session_id": "uuid",
  "participant_id": "uuid",
  "turns_allocated":          3,
  "turns_completed":          3,
  "turns_skipped":            0,
  "turns_forfeited":          0,
  "total_mic_open_ms":        145000,
  "silence_during_token_count": 0,
  "interrupt_attempts":       1,
  "open_round_speaking_ms":   42000,
  "open_round_dominance_flag": false,
  "network_drops":            0,
  "early_exit":               false
}
```

No subjective fields.
No confidence scores.
No personality labels.
Only measurable, auditable behavior.

Scoring formula (Scoring Engine jurisdiction — not this agent):
```
score =
    + (turns_completed / turns_allocated) × 20
    + (total_mic_open_ms / allocated_total_ms) × 20
    + (silence_during_token_count == 0 ? 10 : 0)
    - (interrupt_attempts × 2)
    - (open_round_dominance_flag ? 5 : 0)
    - (early_exit ? 10 : 0)
```

---

## SECTION 11 — WEBSOCKET COMMAND PROTOCOL (LOCKED)

### 11.1 Agent → Frontend Commands

All commands are server-authoritative. Frontend obeys. Frontend does not negotiate.

```json
// Token granted to speaker
{
    "event":       "TOKEN_GRANTED",
    "speaker_id":  "uuid",
    "duration_ms": 60000,
    "expires_at":  "2024-01-01T10:05:00Z",
    "round_type":  "INTRO"
}

// Token expired
{
    "event":      "TOKEN_EXPIRED",
    "speaker_id": "uuid"
}

// Force mute command
{
    "event":          "FORCE_MUTED",
    "participant_id": "uuid"
}

// Force unmute command
{
    "event":          "FORCE_UNMUTED",
    "participant_id": "uuid"
}

// Interrupt blocked
{
    "event":          "INTERRUPT_BLOCKED",
    "participant_id": "uuid"
}

// Open discussion activated
{
    "event":        "OPEN_DISCUSSION_STARTED",
    "duration_ms":  300000
}

// Session terminated
{
    "event":      "SESSION_TERMINATED",
    "reason":     "ROUNDS_COMPLETE | FAILURE | ADMIN_HALT"
}
```

### 11.2 Frontend → Agent Events (Monitored Only)

```json
// Client reports mic activity (used for interrupt detection)
{
    "event":          "MIC_ACTIVE",
    "participant_id": "uuid",
    "session_id":     "uuid"
}

// Client reports network degradation
{
    "event":          "NETWORK_DEGRADED",
    "participant_id": "uuid",
    "session_id":     "uuid"
}
```

Frontend events are inputs only.
Frontend cannot grant itself a token.
Frontend cannot unmute itself.

---

## SECTION 12 — REDIS STATE MACHINE KEYS (CANONICAL)

```
gd:{session_id}:routing_state         → INITIALIZED | RUNNING | OPEN_DISCUSSION | ENDED
gd:{session_id}:active_token          → JSON (current active token)
gd:{session_id}:token_timer           → epoch_ms expiry timestamp
gd:{session_id}:participant_queue     → ordered list (LPOP to consume)
gd:{session_id}:mute_state:{pid}      → MUTED | UNMUTED
gd:{session_id}:interrupt_count:{pid} → integer (cumulative)
gd:{session_id}:open_speaking_log:{pid} → cumulative_ms_spoken in open round
gd:{session_id}:round_index           → integer (current round number)
gd:{session_id}:session_config        → JSON (all round durations, participant list)
```

All keys prefixed with `gd:` are owned by the realtime namespace.
No other service may write to these keys.
Reads are permitted by the Scoring Engine at session end only.

---

## SECTION 13 — KAFKA EVENT SCHEMA

```json
// gd.routing.initialized
{
    "event":        "gd.routing.initialized",
    "session_id":   "uuid",
    "participant_count": 6,
    "timestamp":    "ISO8601"
}

// gd.turn.started
{
    "event":         "gd.turn.started",
    "session_id":    "uuid",
    "round_id":      "uuid",
    "participant_id":"uuid",
    "round_type":    "INTRO",
    "allocated_ms":  60000,
    "timestamp":     "ISO8601"
}

// gd.turn.ended
{
    "event":         "gd.turn.ended",
    "session_id":    "uuid",
    "round_id":      "uuid",
    "participant_id":"uuid",
    "actual_open_ms":54200,
    "end_reason":    "EXPIRED | FORFEITED | NETWORK_DROP",
    "timestamp":     "ISO8601"
}

// gd.mic.interrupted
{
    "event":              "gd.mic.interrupted",
    "session_id":         "uuid",
    "attempting_pid":     "uuid",
    "active_speaker_pid": "uuid",
    "timestamp":          "ISO8601"
}

// gd.completed
{
    "event":      "gd.completed",
    "session_id": "uuid",
    "timestamp":  "ISO8601"
}
```

---

## SECTION 14 — OBSERVABILITY REQUIREMENTS (NON-OPTIONAL)

### 14.1 Prometheus Metrics

```
gd_token_grants_total{session_id, round_type}
gd_token_expirations_total{session_id, end_reason}
gd_interrupt_attempts_total{session_id}
gd_force_mute_operations_total{session_id}
gd_jitsi_api_latency_ms{operation}
gd_jitsi_api_failures_total{operation, error_code}
gd_websocket_command_latency_ms
gd_redis_write_latency_ms
gd_active_sessions_gauge
```

### 14.2 Loki Log Events (Structured JSON)

Every Redis write, Jitsi API call, WebSocket broadcast, and Kafka emit
must produce a structured log entry with:
```json
{
    "level":      "INFO | WARN | ERROR",
    "service":    "audio-stream-routing-agent",
    "session_id": "uuid",
    "event":      "event_name",
    "latency_ms": integer,
    "timestamp":  "ISO8601"
}
```

### 14.3 OpenTelemetry Traces

Each `grant_next_token` invocation produces one trace span covering:
- Redis read (queue + config)
- Jitsi mute-all API calls
- Jitsi unmute API call
- WebSocket broadcast
- Kafka event emit
- PostgreSQL log write

End-to-end token grant latency target: **< 200ms P99**

---

## SECTION 15 — SECURITY BASELINE

```
JWT Tokens:     Short-lived (15 min) per-participant signed tokens
                issued by Auth Service before session join.

Jitsi Tokens:   Room-scoped, participant-scoped, expiring at session end.
                Backend generates. Frontend receives. Backend validates.

WebSocket Auth: Every WebSocket connection authenticated with JWT on handshake.
                Unauthenticated connections rejected immediately.

Jitsi REST API: Internal network only. Never exposed to public ingress.
                Protected by Kubernetes NetworkPolicy.

Redis:          No public exposure. Internal Kubernetes service only.
                Accessed only by realtime namespace services.

Audit Logs:     Immutable append-only PostgreSQL writes.
                Row-level security enforced per tenant.
                No delete permitted by any service role.

Rate Limiting:  Max 1 WebSocket command per 50ms per participant.
                Burst protection via Redis rate limiter.
```

---

## SECTION 16 — DEPLOYMENT SPECIFICATION

### 16.1 Kubernetes Manifest (Reference)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: audio-stream-routing-agent
  namespace: realtime
spec:
  replicas: 2
  selector:
    matchLabels:
      app: audio-stream-routing-agent
  template:
    metadata:
      labels:
        app: audio-stream-routing-agent
    spec:
      containers:
        - name: agent
          image: ecoskiller/audio-stream-routing-agent:latest
          ports:
            - containerPort: 8080
          env:
            - name: REDIS_URL
              valueFrom:
                secretKeyRef:
                  name: redis-secret
                  key: url
            - name: JITSI_REST_URL
              value: "http://jitsi-service.media.svc.cluster.local:8080"
            - name: KAFKA_BROKER
              value: "kafka.ops.svc.cluster.local:9092"
            - name: PG_URL
              valueFrom:
                secretKeyRef:
                  name: postgres-secret
                  key: realtime-dsn
          readinessProbe:
            httpGet:
              path: /health
              port: 8080
            initialDelaySeconds: 5
            periodSeconds: 10
          livenessProbe:
            httpGet:
              path: /health
              port: 8080
            initialDelaySeconds: 15
            periodSeconds: 20
          resources:
            requests:
              cpu: "100m"
              memory: "256Mi"
            limits:
              cpu: "500m"
              memory: "512Mi"
```

### 16.2 Service

```yaml
apiVersion: v1
kind: Service
metadata:
  name: audio-stream-routing-agent
  namespace: realtime
spec:
  selector:
    app: audio-stream-routing-agent
  ports:
    - port: 8080
      targetPort: 8080
  type: ClusterIP
```

---

## SECTION 17 — INTERN EXECUTION INSTRUCTIONS

### Step 1 — Prerequisites
```bash
# Verify tools installed
docker --version
kubectl version --client
redis-cli --version
```

### Step 2 — Project Location
```
/backend/services/realtime/audio_stream_routing_agent/
```

### Step 3 — Start Service (Local Dev)
```bash
cd /backend/services/realtime/audio_stream_routing_agent/
cp .env.example .env          # fill REDIS_URL, JITSI_REST_URL, PG_URL
npm install                   # install dependencies
npm run dev                   # starts on port 8080
```

### Step 4 — Expected Output
```
[AUDIO_STREAM_ROUTING_AGENT] Listening on port 8080
[AUDIO_STREAM_ROUTING_AGENT] Redis connected: OK
[AUDIO_STREAM_ROUTING_AGENT] PostgreSQL connected: OK
[AUDIO_STREAM_ROUTING_AGENT] Kafka producer ready
[AUDIO_STREAM_ROUTING_AGENT] Health endpoint: http://localhost:8080/health
```

### Step 5 — Health Check
```bash
curl http://localhost:8080/health
# Expected: {"status":"UP","redis":"OK","postgres":"OK","kafka":"OK"}
```

### Step 6 — Run Tests
```bash
npm test
# Expected: ALL TESTS PASSED
```

Failure at any step → **STOP EXECUTION** → report to senior developer.

---

## SECTION 18 — FINAL EXECUTION RULES (INVIOLABLE)

```
1. One active speaking token exists at any moment.             NON-NEGOTIABLE.
2. Backend is the single authority on mic state.               NON-NEGOTIABLE.
3. Frontend is a display layer only.                          NON-NEGOTIABLE.
4. Redis state is the single source of truth.                 NON-NEGOTIABLE.
5. No audio content is captured or analyzed.                  NON-NEGOTIABLE.
6. No AI judgment is applied to routing decisions.            NON-NEGOTIABLE.
7. All actions are logged, timestamped, and auditable.        NON-NEGOTIABLE.
8. Failure → skip → advance → log. No retry for participants. NON-NEGOTIABLE.
9. Jitsi executes. Backend governs. Agent enforces.           NON-NEGOTIABLE.
10. All outputs are numeric, reproducible, and bias-free.     NON-NEGOTIABLE.
```

---

## SECTION 19 — BINDING DECLARATIONS

```
Bound to ECOSKILLER Master Prompt:     v12.0 UNIFIED
Bound to Service Contract:             Voice GD Orchestrator (Service #7)
Bound to Infrastructure Law:           R1 Technology Stack Lock
Bound to Security Baseline:            Section IX of Infrastructure Audit
Bound to Observability Law:            Section VIII (NON-OPTIONAL)
Bound to Scoring Engine Interface:     Service #9 data contract
Bound to Event Schema Registry:        gd.* event namespace
Bound to Kubernetes Namespace:         realtime
Bound to Jitsi Media Stack:            Self-hosted, Section IV (LOCKED)
```

---

## SECTION 20 — AGENT SEAL

```
╔══════════════════════════════════════════════════════════════════╗
║         AUDIO_STREAM_ROUTING_AGENT — PROMPT SEALED              ║
║                                                                  ║
║  Version:       v1.0.0                                           ║
║  System:        ECOSKILLER Antigravity Audio Processing          ║
║  Status:        FINAL · LOCKED · DETERMINISTIC                   ║
║  Mutation:      Add-only via version bump ONLY                   ║
║  Authority:     Human declaration only                           ║
║                                                                  ║
║  WebRTC is the physics.                                          ║
║  Jitsi is the engine.                                            ║
║  Redis is the memory.                                            ║
║  Backend is the law.                                             ║
║  AUDIO_STREAM_ROUTING_AGENT is the enforcer.                     ║
║                                                                  ║
║  Candidates operate under constraint.                            ║
║  Output is measurable behavior.                                  ║
║  Bias has no entry point.                                        ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*End of AUDIO_STREAM_ROUTING_AGENT Sealed Prompt — ECOSKILLER v12.0*
