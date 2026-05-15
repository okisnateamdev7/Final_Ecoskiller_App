# PHONE_LATE_JOIN_POLICY_AGENT
**System:** ECOSKILLER  
**Layer:** Session & Lifecycle  
**Context:** Voice GD · Dojo Match · Interview Entry  
**Target Engine:** ANTIGRAVITY  
**Status:** SEALED · LOCKED · GOVERNED · DETERMINISTIC  
**Version:** 1.0.0  
**Mutation Policy:** Add-only via version bump  
**Interpretation Authority:** NONE  
**Execution Authority:** Human declaration only  

---

## ⛔ LOCK DECLARATION

```
AGENT_ID            = PHONE_LATE_JOIN_POLICY_AGENT
EXECUTION_ENGINE    = ANTIGRAVITY
DOMAIN              = Session & Lifecycle
LAYER               = Entry Enforcement + Mobile Gate + State Control
DETERMINISM_RULE    = Identical input → Identical output
FAILURE_MODE        = STOP → REPORT → NO PARTIAL OUTPUT
ANTIGRAVITY_SAFE    = TRUE
REINTERPRETATION    = FORBIDDEN
DISCRETION          = ZERO
MERCY_LOGIC         = DOES NOT EXIST
```

---

## I. AGENT PURPOSE (NON-NEGOTIABLE)

The `PHONE_LATE_JOIN_POLICY_AGENT` is a deterministic, rule-driven sub-agent operating within the **Session & Lifecycle** layer of ECOSKILLER.

Its mandate is:

> **To enforce the complete entry window policy for all phone (mobile) participants attempting to join Voice GD sessions, Dojo matches, or Interview rooms — with absolute enforcement of join deadlines, spectator downgrade, and permanent late-join blocking — without exception, override, or discretion.**

This agent governs:
- Phone/mobile participant entry window validation across all session types
- Hard-blocking of participants who attempt to join after the entry deadline
- Spectator-only downgrade enforcement for borderline late arrivals (where defined)
- Reconnection policy enforcement for mid-session mobile disconnects
- Network-drop vs. voluntary-exit classification for scoring fairness
- Real-time countdowns delivered to mobile frontend via WebSocket
- All state transitions logged immutably to PostgreSQL

This agent does NOT:
- Grant grace periods based on network claims
- Distinguish between "almost on time" and "late" — the window is binary
- Allow rejoining a session after a confirmed late-block or exit event
- Accept frontend-reported timestamps as authoritative

---

## II. SYSTEM ARCHITECTURE BINDING

### Stack Components Governed:

| Component | Role in This Agent |
|---|---|
| **Redis** | Authoritative entry window timer — TTL enforced, atomic state |
| **Session Orchestrator** | Consumes agent verdict — triggers BLOCKED or SPECTATOR state |
| **Voice GD Orchestrator** | Blocks late phone joiners from speaking queue entirely |
| **Dojo Match Engine** | Locks match roster at window close — no late additions |
| **Interview Service** | Slot lock enforcement — no entry after window close |
| **WebSocket Channel** | Delivers real-time join countdown to Flutter mobile UI |
| **PostgreSQL** | Immutable audit of all join attempts, blocks, spectator upgrades |
| **Kafka / RabbitMQ** | Emits `session.late_join_blocked`, `session.spectator_assigned`, `session.reconnect_denied` |
| **Notification Service** | Sends pre-session entry window reminders and post-block notifications |
| **Prometheus + Grafana** | Tracks late join rate, mobile drop rate, block frequency per session type |
| **Wazuh** | Flags repeated tactical lateness patterns as abuse signals |
| **OPA** | Enforces policy that only `SESSION_ORCHESTRATOR` role may invoke entry verdicts |

### Antipatterns (FORBIDDEN):
- Using server wall-clock instead of Redis TTL as join window authority
- Allowing phone clients to self-report join timestamps
- Implementing "soft blocks" that allow muted participation after window close
- Granting frontend retry on `LATE_BLOCK` verdicts
- Storing join window state in application memory (stateless services — state in Redis only)
- Any conditional logic that evaluates "reason for lateness"

---

## III. SESSION TYPES & JOIN WINDOW POLICY

| Session Type | Join Window Opens | Join Window Closes | Late Result | Reconnect After Drop |
|---|---|---|---|---|
| `VOICE_GD` | T-5 min before start | T+0 (session start) | `SPECTATOR_ONLY` — no speaking rights | `DENIED` — slot lost |
| `VOICE_GD` (2nd+ batch) | T-3 min before start | T+0 | `HARD_BLOCKED` — no entry at all | `DENIED` |
| `DOJO_MATCH` | T-2 min before start | T+0 | `HARD_BLOCKED` — match proceeds without | `DENIED` |
| `INTERVIEW_SLOT` | T-10 min before start | T+5 min grace | `HARD_BLOCKED` — slot marked NO_SHOW | `DENIED` |
| `DOJO_TOURNAMENT` | T-5 min before start | T+0 | `HARD_BLOCKED` — forfeit recorded | `DENIED` |

**Definitions:**
- `HARD_BLOCKED` — Entry refused. Session proceeds without participant. Score recorded as ABSENT.
- `SPECTATOR_ONLY` — Read-only observer. Cannot speak, cannot be scored, cannot receive belt/cert credit.
- `DENIED` — Reconnection request rejected unconditionally. No retry pathway.

**The join window is a Redis TTL. When TTL = 0, the window is permanently closed.**

---

## IV. ENTRY WINDOW STATE MACHINE

```
[SESSION_SCHEDULED]
        ↓
[WINDOW_OPEN]  ← Redis TTL starts counting down
        ↓
  ┌─────┴────────────────────────────────┐
  │                                      │
[JOINED_VALID]                    [WINDOW_CLOSED]  ← TTL elapsed
  │                                      │
[PARTICIPANT_ACTIVE]         ┌───────────┴────────────┐
  │                          │                         │
  │                  [SPECTATOR_ASSIGNED]      [HARD_BLOCKED]
  │                  (GD only, if defined)     (all other types)
  │                          │                         │
  │                  [AUDIT_LOGGED]            [AUDIT_LOGGED]
  │                  [EVENT_EMITTED]           [EVENT_EMITTED]
  ↓                          ↓                         ↓
[SESSION_RUNNING]    [READ_ONLY_OBSERVER]       [NO_ENTRY]
  │
  │  ← Mid-session network drop on phone
  ↓
[DROP_DETECTED]
  │
  ├── If drop < 30s → [RECONNECT_WINDOW_OPEN] (30s grace)
  │         ↓
  │   [RECONNECT_SUCCESS] or [RECONNECT_EXPIRED]
  │         ↓                      ↓
  │   [SESSION_RESUME]      [SLOT_FORFEITED + AUDIT_LOGGED]
  │
  └── If drop > 30s → [HARD_EXIT] → [SLOT_FORFEITED + AUDIT_LOGGED]
```

**State Rules:**
- States are strictly sequential — no backward transitions
- `HARD_BLOCKED` is terminal — no appeal pathway within the system
- `SPECTATOR_ONLY` is terminal for that session — cannot be upgraded to participant
- `RECONNECT_EXPIRED` is terminal — no second reconnect window
- All state transitions write to PostgreSQL audit log atomically

---

## V. REDIS KEY SCHEMA (ENFORCED)

```
session_window:{session_type}:{session_id}         → OPEN|CLOSED            [TTL = window duration]
session_participants:{session_id}                  → SET of user_ids         [TTL = session duration]
session_join_attempt:{session_id}:{user_id}        → attempt_timestamp       [TTL = session duration]
session_status:{session_id}:{user_id}              → PENDING|JOINED|SPECTATOR|BLOCKED|DROPPED|FORFEITED
session_reconnect:{session_id}:{user_id}           → reconnect_granted_flag  [TTL = 30s]
session_phone_drop:{session_id}:{user_id}          → drop_timestamp          [TTL = 60s]
```

**Key Rules:**
- All keys namespace under `session_` prefix
- `session_window` key is set atomically at session creation — `SET ... EX {window_seconds} NX`
- No TTL extension permitted on `session_window` after initial set
- Participant roster locked atomically when window closes — Redis `SINTERSTORE` pattern
- All phone join attempts logged with server-side timestamp (never client-reported)

---

## VI. ENFORCEMENT FLOW (DETERMINISTIC)

### 6.1 Pre-Session: Entry Window Activation
```
1. Session Orchestrator creates session
2. Agent sets: SET session_window:{type}:{session_id} OPEN EX {window_seconds} NX
3. Agent sets: participant roster as PENDING for all scheduled users
4. Notification Service dispatches:
   → T-30 min: "Your {session_type} starts in 30 minutes. Join window opens at T-5."
   → T-5 min:  "Join window is now OPEN. You have {X} minutes to join."
   → T-2 min:  "⚠️ Join window closes in 2 minutes."
5. WebSocket pushes live countdown to Flutter mobile UI
```

### 6.2 Join Attempt Validation
```
1. Phone client sends JOIN request with {session_id, user_id, device_token}
2. Agent checks: GET session_window:{type}:{session_id}
   → If key exists (OPEN): proceed to step 3
   → If key missing (TTL elapsed = CLOSED): proceed to LATE BLOCK flow (6.3)
3. Verify user is in scheduled participant roster for this session
   → If NOT in roster: REJECT with ERR-SESSION-NOT-REGISTERED
4. Verify no existing JOINED status for this user in this session (no double-join)
5. Atomically add user to active participant set
6. Set session_status:{session_id}:{user_id} → JOINED
7. Emit: session.participant_joined → Kafka
8. Write audit log: action=JOINED, device_type=PHONE, timestamp=server_time
9. Session Orchestrator confirms participant added to speaking queue (GD) or match roster (Dojo)
```

### 6.3 Late Join Block Flow
```
1. session_window key is missing (TTL elapsed) → window is CLOSED
2. Check session type:
   → VOICE_GD (first batch): Set session_status → SPECTATOR_ONLY
      a. Emit: session.spectator_assigned → Kafka
      b. Notify: "You joined late. You may observe this session but cannot participate or be scored."
      c. GD Orchestrator: user excluded from speaking queue, mic permanently locked
      d. Score Engine: records ABSENT for this user's GD attempt
   → All other types (DOJO, INTERVIEW, TOURNAMENT, VOICE_GD 2nd+): Set session_status → HARD_BLOCKED
      a. Emit: session.late_join_blocked → Kafka
      b. Notify: "Entry window closed. You have been blocked from this session."
      c. Score Engine: records ABSENT / NO_SHOW
      d. If INTERVIEW: slot marked NO_SHOW — recruiter notified
3. Write audit log: action=LATE_JOIN_BLOCKED, device_type=PHONE, attempted_at=server_time
4. Return HTTP 403 to client with code ERR-MATCH-2007 (LATE_JOIN_BLOCKED)
5. Frontend: show terminal "Session Closed" screen — no retry CTA
```

### 6.4 Mid-Session Phone Drop Handling
```
1. WebRTC / Jitsi detects participant audio stream loss (phone network drop)
2. Agent receives: session.participant_disconnected event
3. Server timestamps drop: SET session_phone_drop:{session_id}:{user_id} {timestamp} EX 60
4. Open 30-second reconnect window: SET session_reconnect:{session_id}:{user_id} GRANTED EX 30
5. GD Orchestrator: skip this user's speaking turn if drop occurs during their token
   → Log: action=TURN_SKIPPED_NETWORK_DROP
   → Score: turn counts as SKIPPED (not penalized as interrupt — network-classified)
6. If reconnect within 30s:
   → Restore participant status: session_status → JOINED
   → Notify: "Reconnected. Resuming from current position."
   → Emit: session.reconnect_success → Kafka
7. If reconnect NOT within 30s (TTL elapses on reconnect key):
   → Set session_status → HARD_EXIT
   → Remove from speaking queue permanently
   → Score Engine: all remaining turns = FORFEITED
   → Emit: session.reconnect_denied → Kafka
   → Notify: "You were disconnected and could not reconnect in time. Session forfeited."
   → Write audit log: action=HARD_EXIT_RECONNECT_EXPIRED
```

### 6.5 Voluntary Exit Detection
```
1. Phone client sends explicit disconnect / app close event
2. Agent classifies: VOLUNTARY_EXIT (vs. network drop which has no goodbye event)
3. Set session_status → VOLUNTARY_EXIT
4. NO reconnect window granted (voluntary = intentional)
5. Emit: session.voluntary_exit → Kafka
6. Score Engine: all remaining turns = FORFEITED, dominance/exit penalty applied
7. Write audit log: action=VOLUNTARY_EXIT
8. GD Orchestrator / Dojo Engine: proceeds without this participant, no pause
```

---

## VII. NETWORK DROP vs. VOLUNTARY EXIT CLASSIFICATION

| Signal | Classification | Reconnect Window | Score Treatment |
|---|---|---|---|
| Stream loss, no disconnect event | `NETWORK_DROP` | 30 seconds | Skipped turns not penalized |
| Explicit app close / browser tab close | `VOLUNTARY_EXIT` | NONE | Full forfeit penalty |
| Repeated rapid disconnects (>2 in session) | `ABUSE_PATTERN` | NONE on 3rd | Wazuh flagged + forfeit |
| Drop during own speaking turn | `NETWORK_DROP_TURN` | 30 seconds | Turn recorded as SKIPPED |
| Drop during others' turns | `NETWORK_DROP_PASSIVE` | 30 seconds | No scoring impact |

**Classification is always server-side. Client signal is a hint only.**

---

## VIII. FAILURE HANDLING (ZERO DISCRETION)

| Failure Scenario | Agent Action |
|---|---|
| Redis unreachable at join attempt | STOP validation → return `SERVICE_UNAVAILABLE` — NO fallback to DB |
| session_window key missing | Treat as CLOSED → execute Late Block Flow |
| Participant not in scheduled roster | REJECT with `ERR-SESSION-NOT-REGISTERED` — no provisional entry |
| Duplicate join attempt (same user, same session) | Return existing status — no re-processing |
| Client claims it joined before window closed | Client timestamp ignored — Redis server timestamp is law |
| Admin requests manual late join exception | Requires `ADMIN_LATE_JOIN_OVERRIDE` event with audit trail — only for INTERVIEW type |
| Phone client sends fake disconnect/reconnect cycle | Wazuh pattern detection — 3rd occurrence = PERMANENT_BLOCK for session |
| Session Orchestrator unavailable | Queue join event to Kafka — process on recovery — no live bypass |

---

## IX. EVENTS EMITTED (KAFKA TOPICS)

| Event | Trigger | Consumers |
|---|---|---|
| `session.participant_joined` | Successful join within window | Orchestrator, Analytics, Notification |
| `session.window_closed` | Join window TTL elapses | Orchestrator, Analytics |
| `session.late_join_blocked` | Join attempt after window close | Orchestrator, Score Engine, Notification, Wazuh |
| `session.spectator_assigned` | Late GD join → spectator downgrade | GD Orchestrator, Score Engine, Notification |
| `session.participant_disconnected` | Mid-session network drop detected | Agent (triggers reconnect flow) |
| `session.reconnect_success` | Phone reconnects within 30s | Orchestrator, Analytics |
| `session.reconnect_denied` | Reconnect window expired | Score Engine, Notification, Analytics |
| `session.voluntary_exit` | Explicit disconnect signal received | Score Engine, Notification, Wazuh |
| `session.hard_exit` | Reconnect expired + slot forfeited | Score Engine, Notification, Analytics |
| `session.abuse_pattern_detected` | >2 rapid disconnects in session | Wazuh, Admin Governance Service |

All events carry: `user_id`, `session_id`, `session_type`, `device_type=PHONE`, `timestamp`, `tenant_id`

---

## X. AUDIT LOG SCHEMA (POSTGRESQL — IMMUTABLE)

```sql
CREATE TABLE session_join_audit_log (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id       UUID NOT NULL,
  user_id         UUID NOT NULL,
  session_id      UUID NOT NULL,
  session_type    VARCHAR(30) NOT NULL,   -- VOICE_GD, DOJO_MATCH, INTERVIEW_SLOT, DOJO_TOURNAMENT
  device_type     VARCHAR(10) NOT NULL DEFAULT 'PHONE',
  action          VARCHAR(40) NOT NULL,
  -- JOINED | LATE_JOIN_BLOCKED | SPECTATOR_ASSIGNED | HARD_BLOCKED
  -- NETWORK_DROP | RECONNECT_SUCCESS | RECONNECT_DENIED | VOLUNTARY_EXIT
  -- HARD_EXIT | TURN_SKIPPED_NETWORK_DROP | ABUSE_PATTERN_DETECTED
  attempted_at    TIMESTAMPTZ NOT NULL,   -- server-side timestamp only
  window_closed_at TIMESTAMPTZ,          -- recorded at window close
  ip_address      INET,
  device_token    TEXT,
  notes           TEXT
);

-- Row-level security enforced per tenant
ALTER TABLE session_join_audit_log ENABLE ROW LEVEL SECURITY;

-- WORM enforcement — no UPDATE or DELETE permitted
REVOKE UPDATE, DELETE ON session_join_audit_log FROM PUBLIC;
```

---

## XI. SCORING IMPACT RULES (BINDING ON SCORE ENGINE)

| Join Outcome | Score Engine Instruction |
|---|---|
| `JOINED_VALID` | Full scoring participation — all turns eligible |
| `SPECTATOR_ONLY` | No score generated — session marked as OBSERVED, NOT ATTEMPTED |
| `HARD_BLOCKED` | Score = ABSENT — counts against user's GD/Dojo attempt history |
| `NETWORK_DROP + RECONNECT_SUCCESS` | Skipped turns during drop = SKIPPED (not penalized) |
| `RECONNECT_DENIED / HARD_EXIT` | All remaining turns = FORFEITED — forfeit penalty applied |
| `VOLUNTARY_EXIT` | All remaining turns = FORFEITED — exit penalty applied |
| `ABUSE_PATTERN_DETECTED` | Session score voided — misconduct flag raised |

**Score Engine must consume `session_join_audit_log` — never self-determine join validity.**

---

## XII. MOBILE (FLUTTER) UI CONTRACT

```
⚠️ ANTIGRAVITY MUST NOT build retry UI after HARD_BLOCKED or LATE_JOIN_BLOCKED
⚠️ ANTIGRAVITY MUST render join countdown from WebSocket channel only
⚠️ ANTIGRAVITY MUST NOT accept or display client-side join timestamps
⚠️ ANTIGRAVITY MUST show SPECTATOR badge clearly when status = SPECTATOR_ONLY
⚠️ ANTIGRAVITY MUST disable all speaking controls when status = SPECTATOR_ONLY
⚠️ ANTIGRAVITY MUST show reconnect countdown only during 30s RECONNECT_WINDOW
⚠️ ANTIGRAVITY MUST NOT show reconnect option after RECONNECT_EXPIRED
```

### Widget Contracts (Flutter Mobile):

**JoinWindowCountdownWidget**
```
Input:  WebSocket event { remaining_seconds, window_status: OPEN|CLOSED }
States: OPEN (green) | WARNING ≤120s (amber) | CLOSED (red)
On CLOSED: Show "Window Closed — Entry Not Permitted" — no join CTA
Lifecycle: Disposed on JOINED or HARD_BLOCKED
```

**SessionStatusBannerWidget**
```
Input:  session_status from WebSocket
States: ACTIVE | SPECTATOR | BLOCKED | RECONNECTING | FORFEITED
On SPECTATOR: Show "Observer Mode — You cannot speak or be scored"
On BLOCKED: Show "Session entry denied" — navigate to dashboard after 5s
On FORFEITED: Show "You have been removed from this session"
```

**ReconnectTimerWidget**
```
Input:  WebSocket event { reconnect_remaining_seconds }
Visible: ONLY when status = RECONNECTING
On 0s: Hide widget, show "Reconnect window expired" toast, navigate to dashboard
No "Try Again" button rendered after expiry
```

---

## XIII. OBSERVABILITY & ALERTING

| Metric | Source | Alert Threshold |
|---|---|---|
| `late_join_block_rate` | Prometheus | >15% of sessions in 1 hour |
| `phone_drop_rate` | Prometheus | >20% of phone participants per session |
| `reconnect_failure_rate` | Prometheus | >30% of drops result in HARD_EXIT |
| `abuse_pattern_events` | Wazuh | Any event → immediate flag |
| `spectator_assignment_rate` | ClickHouse | >10% of GD participants → product investigation |

Grafana dashboard: **Session Lifecycle — Phone Entry & Drop Monitor**

---

## XIV. PRODUCTION READINESS CHECKLIST

```
[ ] Redis NX+TTL atomic SET verified for session_window keys
[ ] Join window close event confirmed firing to Kafka
[ ] SPECTATOR_ONLY state: speaking controls disabled in Flutter UI confirmed
[ ] HARD_BLOCKED: no retry CTA rendered in Flutter UI confirmed
[ ] Reconnect 30s window: timer visible only during reconnect window confirmed
[ ] PostgreSQL audit table: UPDATE and DELETE blocked at DB level
[ ] Score Engine: consuming join audit log (not self-determining) confirmed
[ ] Wazuh: abuse_pattern_detected alert rule configured
[ ] Client timestamp never accepted as authoritative — server timestamp enforced
[ ] OPA policy: only SESSION_ORCHESTRATOR may invoke entry verdicts
[ ] Interview NO_SHOW: recruiter notification on HARD_BLOCKED confirmed
[ ] Prometheus metrics: late_join_block_rate and phone_drop_rate dashboards live
[ ] All 10 Kafka events confirmed publishing and consuming
[ ] VOLUNTARY_EXIT vs NETWORK_DROP classification logic integration tested
```

Absence of any checked item → **STOP EXECUTION**

---

## XV. FINAL LOCK

```
AGENT_LOCK_HASH         = PHONE_LATE_JOIN_POLICY_AGENT_v1.0.0
READY_FOR               = ANTIGRAVITY_PRODUCTION
ANTIGRAVITY_CONFUSION   = IMPOSSIBLE
REINTERPRETATION        = FORBIDDEN
GRACE_PERIOD_LOGIC      = DOES NOT EXIST
EXTENSION_BY_AI         = FORBIDDEN
OVERRIDE_BY_USER        = FORBIDDEN
ADMIN_OVERRIDE          = INTERVIEW_TYPE_ONLY + FULL_AUDIT_REQUIRED
```

> **The join window is binary. Open or closed. No in-between.**  
> **Redis is the clock. Server timestamp is the witness. The phone has no vote.**  
> **Late is late. The session does not wait. The score does not forgive.**  
> **This agent is the entry gate for every phone participant in ECOSKILLER.**
