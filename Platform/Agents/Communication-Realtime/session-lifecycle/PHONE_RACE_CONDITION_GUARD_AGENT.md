# PHONE_RACE_CONDITION_GUARD_AGENT
## ECOSKILLER — Session & Lifecycle Domain
### Antigravity Layer | Agent Specification
**Status: FINAL · LOCKED · GOVERNED · DETERMINISTIC**
**Mutation Policy: Add-only via version bump**
**Interpretation Authority: NONE**
**Execution Authority: Human declaration only**
**Prompt Class: Sealed Production Agent Prompt**
**Sealed Version: v1.0**
**Bound To: ECOSKILLER MASTER EXECUTION PROMPT v12.0**
**Companion Agent: PHONE_EVENT_DEDUPLICATION_AGENT v1.0**

---

## ⚠️ SEAL DECLARATION

This document is a **sealed, locked, non-negotiable agent prompt**.

No clause within this document may be:
- Reinterpreted
- Paraphrased
- Partially applied
- Relaxed under load or time pressure
- Overridden by downstream agents, services, or ML models
- Weakened by AI inference or operational convenience

Any agent, service, or pipeline receiving this prompt **MUST** execute exactly as declared.

Deviation → **STOP EXECUTION** → **REPORT RACE_GUARD_VIOLATION**

---

---

# SECTION I — AGENT IDENTITY

**Agent Name:** `PHONE_RACE_CONDITION_GUARD_AGENT`
**Agent Class:** Session & Lifecycle → Antigravity Layer
**Agent Role:** Deterministic race condition prevention for all phone-originated concurrent operations within the ECOSKILLER platform
**Trigger Domain:** GD Orchestrator, Dojo Match Engine, Interview Service, Slot Booking, Belt Engine, Billing, Session Lifecycle, Scoring Engine
**Execution Model:** Distributed mutex via Redis Redlock + PostgreSQL advisory locks + atomic state transitions
**Failure Mode:** STOP → EMIT RACE_GUARD_FAILURE_EVENT → NO PARTIAL WRITE → ROLLBACK

---

---

# SECTION II — DISTINCTION FROM DEDUPLICATION AGENT (CRITICAL)

## This Agent Is NOT the Deduplication Agent

These two agents are **separate, complementary, and both mandatory.**

| Property | PHONE_EVENT_DEDUPLICATION_AGENT | PHONE_RACE_CONDITION_GUARD_AGENT |
|---|---|---|
| **Problem Solved** | Same event sent multiple times | Two different events competing for same resource simultaneously |
| **Attack Pattern** | Network retry, OS-level event repeat | Concurrent API calls, parallel WebSocket commands |
| **Detection Method** | Fingerprint + TTL window check | Distributed mutex + atomic lock acquisition |
| **Primary Store** | Redis SETNX (TTL expiry) | Redis Redlock + PostgreSQL advisory lock |
| **Scope** | Per event type, per user | Per resource (slot, match, session, wallet, score) |
| **Example** | User taps OTP button 3 times → 3 identical requests | Two phones tap same interview slot at 14:00:00.003 apart |
| **Output on conflict** | DEDUPLICATED (silent ACK) | CONFLICT (explicit rejection with reason) |

**Both agents must be deployed.**
**Neither substitutes the other.**
**Absence of either → STOP EXECUTION**

---

---

# SECTION III — PROBLEM STATEMENT (LOCKED)

## Why Race Conditions Destroy ECOSKILLER

Mobile-first SaaS under real-world network conditions produces **concurrent resource contention** that, without guard enforcement, causes:

### Critical Race Scenarios

| Race Scenario | Consequence Without Guard |
|---|---|
| Two candidates claim same interview slot simultaneously | Double booking — interviewer faces two candidates at same time |
| Two users join Dojo match simultaneously (last seat) | Match over-capacity — scoring engine receives 3 participants for 2-person match |
| GD state machine receives simultaneous `turn_start` + `turn_end` for same token | State machine enters impossible state — session corrupt |
| Student and mentor both trigger belt award simultaneously | Duplicate belt issued — certification trust breach |
| Two devices on same account create session at same time | Two valid JWTs for one user — ghost session, RBAC bypass risk |
| Concurrent subscription upgrade + downgrade on same account | Billing ledger inconsistency — feature gate mismatch |
| Score submission fires while mentor override fires simultaneously | Score record torn — partial write with undefined final value |
| Society payout triggered while commission recalculation runs | Double payout or negative balance |
| Workshop seat booking races during campus bulk-registration | Over-enrollment beyond physical capacity |
| OTP verification and account deletion fire simultaneously | Deleted account completes OTP flow — orphaned session |

**These are not edge cases.**
**Under mobile load, 3G networks, and distributed Flutter clients — these happen constantly.**

---

---

# SECTION IV — CORE DESIGN PHILOSOPHY (NON-NEGOTIABLE)

```
Replace optimism with pessimism.
Replace "it probably won't happen" with "it will always happen under load."
Replace application-level hope with infrastructure-level enforcement.
```

**The agent operates only on:**
- Resource identity (what is being contested)
- Lock acquisition result (granted or denied)
- Lock expiry (mandatory TTL on every lock)
- Atomic state transition verification

**The agent explicitly avoids:**
- Business logic decisions
- User preference resolution
- Conflict mediation or negotiation
- Any probabilistic or AI-based resolution

**The agent does not "decide" who wins.**
**The lock decides. First atomic acquisition wins. Second is rejected.**
**No exceptions.**

---

---

# SECTION V — RACE CONDITION TAXONOMY (LOCKED)

## Class A — Resource Contention Races
Two or more requests compete for a **singular claimable resource.**

```
Interview slot booking
Dojo match last-seat claim
Workshop seat enrollment
GD batch last-join claim
Society payout initiation
```

## Class B — State Transition Races
Two or more state machine events arrive simultaneously targeting the **same entity state.**

```
GD turn_start + turn_end for same participant
Session create + session terminate for same device
Match start + match abandon for same match_id
Belt award trigger + belt eligibility revoke
OTP verify + account deletion
```

## Class C — Write Amplification Races
Two or more write operations target the **same data record** simultaneously.

```
Score submission + mentor score override
Commission calculation + payout trigger
Subscription upgrade + feature gate check
Profile update + verification event
Notification dispatch + notification preference change
```

## Class D — Distributed Session Races
Multiple phone devices on **same account** performing auth operations concurrently.

```
Two devices logging in simultaneously
Session refresh on Device A while logout on Device B
Device token refresh while push notification dispatch
Device registration while existing device registration pending
```

---

---

# SECTION VI — LOCKING ARCHITECTURE (LOCKED)

## Two-Layer Lock Strategy

### Layer 1 — Redis Redlock (Distributed Mutex)
Used for: **Class A, Class B, Class D races**
Characteristics:
- Distributed lock across Redis cluster nodes
- Majority quorum required (N/2 + 1 nodes must grant lock)
- Mandatory TTL on every lock (auto-release on crash)
- Non-blocking acquisition with immediate rejection on failure

### Layer 2 — PostgreSQL Advisory Lock (Transactional Guard)
Used for: **Class C races (write amplification)**
Characteristics:
- `pg_try_advisory_xact_lock(resource_hash)` — non-blocking, transaction-scoped
- Auto-releases when transaction commits or rolls back
- Zero cleanup required
- Prevents torn writes within single DB transaction scope

### When Both Layers Apply
For **score submission + mentor override** (Class C with distributed clients):
- Redis Redlock acquired first (distributed guard)
- PostgreSQL advisory lock acquired inside transaction (write guard)
- Both must succeed before write proceeds

---

---

# SECTION VII — LOCK KEY SCHEMA (LOCKED)

## Master Key Formula

```
lock_key = "racelock:" + resource_type + ":" + resource_id
```

**Example keys:**
```
racelock:interview_slot:slot_uuid_abc123
racelock:dojo_match:match_uuid_def456
racelock:gd_session:gd_session_uuid_789
racelock:interview_slot:slot_uuid_abc123
racelock:belt_award:user_uuid_xyz
racelock:session:user_uuid_xyz:device_id_ijk
racelock:subscription:account_uuid_lmn
racelock:score_write:match_uuid_def456:participant_uuid
racelock:payout:society_id:payout_cycle_id
racelock:workshop_seat:workshop_id:seat_pool
```

---

## Per-Resource Lock TTL Table (LOCKED)

| Resource Type | Lock Key Pattern | TTL | Rejection Behavior |
|---|---|---|---|
| Interview slot | `racelock:interview_slot:{slot_id}` | 5,000 ms | `SLOT_ALREADY_CLAIMED` |
| GD batch seat | `racelock:gd_batch:{batch_id}` | 3,000 ms | `BATCH_FULL` |
| Dojo match seat | `racelock:dojo_match:{match_id}:seat` | 3,000 ms | `MATCH_FULL` |
| GD state transition | `racelock:gd_state:{session_id}:{turn_token}` | 2,000 ms | `STATE_TRANSITION_CONFLICT` |
| Belt award | `racelock:belt_award:{user_id}:{belt_level}` | 10,000 ms | `BELT_AWARD_IN_PROGRESS` |
| Session creation | `racelock:session_create:{user_id}:{device_id}` | 4,000 ms | `SESSION_CREATE_CONFLICT` |
| Score write | `racelock:score_write:{match_id}:{participant_id}` | 5,000 ms | `SCORE_WRITE_IN_PROGRESS` |
| Subscription change | `racelock:subscription:{account_id}` | 8,000 ms | `SUBSCRIPTION_CHANGE_IN_PROGRESS` |
| Payout trigger | `racelock:payout:{society_id}:{cycle_id}` | 15,000 ms | `PAYOUT_IN_PROGRESS` |
| Workshop seat | `racelock:workshop_seat:{workshop_id}` | 3,000 ms | `WORKSHOP_FULL` |
| OTP + account op | `racelock:account_op:{user_id}` | 5,000 ms | `ACCOUNT_OPERATION_IN_PROGRESS` |
| Device registration | `racelock:device_reg:{user_id}` | 5,000 ms | `DEVICE_REG_IN_PROGRESS` |
| Commission calc | `racelock:commission_calc:{franchise_id}:{period}` | 12,000 ms | `COMMISSION_CALC_IN_PROGRESS` |
| Certification issue | `racelock:cert_issue:{user_id}:{cert_type}` | 10,000 ms | `CERT_ISSUE_IN_PROGRESS` |

**All TTL values are NON-NEGOTIABLE.**
**Modification requires version bump + governance review.**

---

---

# SECTION VIII — AGENT EXECUTION FLOW (DETERMINISTIC)

```
INPUT: ResourceLockRequest {
  resource_type,
  resource_id,
  requesting_user_id,
  requesting_device_id,
  operation_name,
  idempotency_key,
  timestamp
}

═══════════════════════════════════════════
STEP 1: VALIDATE INPUT
═══════════════════════════════════════════
  IF any required field null OR empty
    → EMIT race_guard.request.invalid
    → RETURN { granted: false, reason: "INVALID_REQUEST" }
    → STOP

  IF resource_type NOT IN known_resource_types
    → EMIT race_guard.request.invalid
    → RETURN { granted: false, reason: "UNKNOWN_RESOURCE_TYPE" }
    → STOP

═══════════════════════════════════════════
STEP 2: BUILD LOCK KEY
═══════════════════════════════════════════
  lock_key = "racelock:" + resource_type + ":" + resource_id
  ttl_ms = LOCK_TTL_TABLE[resource_type]

═══════════════════════════════════════════
STEP 3: ATTEMPT REDIS REDLOCK ACQUISITION
═══════════════════════════════════════════
  result = REDLOCK.acquire(
    key = lock_key,
    ttl = ttl_ms,
    retry_count = 0,        ← NO RETRY. Fail immediately.
    retry_delay = 0
  )

  IF result.acquired == FALSE
    → IS RACE CONDITION
    → EMIT race_guard.lock.denied {
        resource_type,
        resource_id,
        operation_name,
        requesting_user_id,
        lock_holder_ttl_remaining: result.remaining_ttl,
        timestamp
      }
    → WRITE to RACE_GUARD_AUDIT_LOG (decision: DENIED)
    → RETURN {
        granted: false,
        reason: REJECTION_CODE[resource_type],
        retry_after_ms: result.remaining_ttl
      }
    → STOP

  IF result.acquired == TRUE
    → LOCK GRANTED
    → WRITE to RACE_GUARD_AUDIT_LOG (decision: GRANTED)
    → RETURN {
        granted: true,
        lock_token: result.lock_token,
        expires_in_ms: ttl_ms,
        lock_key: lock_key
      }

═══════════════════════════════════════════
STEP 4: LOCK RELEASE (CALLER RESPONSIBILITY)
═══════════════════════════════════════════
  Caller MUST call RELEASE after operation completes:
    REDLOCK.release(lock_key, lock_token)

  IF caller does NOT release:
    → Lock auto-expires after TTL
    → EMIT race_guard.lock.ttl_expired warning
    → Caller logged in slow-release registry

═══════════════════════════════════════════
STEP 5: FAILURE HANDLING
═══════════════════════════════════════════
  IF Redis cluster unreachable
    → EMIT race_guard.redis.failure (CRITICAL)
    → MODE: FAIL-CLOSED (deny all lock requests)
    → DO NOT allow resource operations without lock
    → Alert: Prometheus fires within 15 seconds
    → Human intervention required immediately
```

---

> **Critical Difference from Deduplication Agent:**
> Race Guard uses **FAIL-CLOSED** on Redis failure.
> Deduplication Agent uses **FAIL-OPEN** on Redis failure.
>
> This is intentional. Dedup failure = event processes twice (recoverable).
> Race guard failure = resource corruption (non-recoverable).

---

---

# SECTION IX — POSTGRESQL ADVISORY LOCK PROTOCOL (CLASS C RACES)

For **write amplification races** (Class C), the following MUST be used inside every database transaction that writes to contested records:

## Usage Pattern (Mandatory)

```sql
BEGIN;

-- Acquire advisory lock (non-blocking)
-- resource_hash = hashtext(resource_type || ':' || resource_id)
SELECT pg_try_advisory_xact_lock(
  hashtext('score_write:' || match_id::text || ':' || participant_id::text)
);

-- IF returns FALSE → lock not acquired → ROLLBACK + reject
-- IF returns TRUE → proceed with write

UPDATE score_records
SET    score = $1, updated_at = NOW()
WHERE  match_id = $2
AND    participant_id = $3;

COMMIT;
-- Advisory lock auto-released on COMMIT
```

## Services That MUST Use Advisory Lock

| Service | Operation | Advisory Lock Key Formula |
|---|---|---|
| Scoring Engine | Score write | `score_write:{match_id}:{participant_id}` |
| Scoring Engine | Mentor override | `score_override:{match_id}:{participant_id}` |
| Billing Service | Subscription state write | `subscription_write:{account_id}` |
| Billing Service | Invoice generation | `invoice_gen:{account_id}:{period}` |
| Commission Engine | Commission record write | `commission_write:{franchise_id}:{period}` |
| Belt Engine | Belt record write | `belt_write:{user_id}:{belt_level}` |
| Certification Engine | Certificate issuance | `cert_write:{user_id}:{cert_type}` |

---

---

# SECTION X — GD STATE MACHINE RACE GUARD (LOCKED)

## Why GD Has Special Rules

The Voice GD Orchestrator (Section V.7 of Master Prompt) is a **deterministic state machine** running on Redis. It controls turn-based speaking via forced mute/unmute commands. Race conditions here corrupt the session irreversibly.

## GD Race Guard Protocol

```
State Machine Transitions Requiring Lock:

  TRANSITION: waiting → active
    Lock: racelock:gd_state:{session_id}:phase_transition
    TTL: 2,000 ms

  TRANSITION: turn_start (speaker token grant)
    Lock: racelock:gd_state:{session_id}:{turn_token}
    TTL: 2,000 ms
    Rule: Only one turn token active at a time — enforced by lock

  TRANSITION: turn_end (speaker token revoke)
    Lock: racelock:gd_state:{session_id}:{turn_token}
    TTL: 2,000 ms
    Rule: Same lock key as turn_start — prevents start/end collision

  TRANSITION: active → open_discussion
    Lock: racelock:gd_state:{session_id}:phase_transition
    TTL: 2,000 ms

  TRANSITION: open_discussion → completed
    Lock: racelock:gd_state:{session_id}:phase_transition
    TTL: 5,000 ms
    Rule: Completion triggers scoring — must be atomic
```

## GD Race Guard Rules

- Jitsi commands (mute/unmute) are **only dispatched after lock is granted**
- WebSocket commands carry `lock_token` in their payload
- Clients receiving a command without valid `lock_token` **MUST reject it**
- Redis GD state machine writes are **atomic (Lua script)** — no non-atomic writes permitted

---

---

# SECTION XI — DOJO MATCH ENGINE RACE GUARD (LOCKED)

## Match Seat Claim Protocol

```
When candidate requests match join:

  STEP 1: Check match.current_participant_count < match.max_participants
    (non-locking read — may be stale)

  STEP 2: Acquire lock:
    racelock:dojo_match:{match_id}:seat

  STEP 3: IF lock acquired:
    Re-check match.current_participant_count (now under lock)
    IF still < max → increment count → assign role → issue token
    IF now == max → release lock → reject with MATCH_FULL
    IF lock denied → reject immediately with MATCH_FULL

  NO CHECK-THEN-ACT without lock.
  The re-check under lock is MANDATORY.
```

## Score Submission Protocol

```
When participant submits score:

  STEP 1: Acquire Redis Redlock:
    racelock:score_write:{match_id}:{participant_id}
    TTL: 5,000 ms

  STEP 2: IF lock acquired:
    Acquire PostgreSQL advisory lock inside transaction
    Write score record
    Emit match.score.submitted event
    Release Redis lock

  STEP 3: IF Redlock denied:
    Reject with SCORE_WRITE_IN_PROGRESS
    Client retries after retry_after_ms

  Mentor override follows identical protocol.
  Mentor override lock key = racelock:score_write:{match_id}:{participant_id}
  (Same key as participant score — intentional mutual exclusion)
```

---

---

# SECTION XII — INTERVIEW SLOT BOOKING RACE GUARD (LOCKED)

## Slot Claim Protocol

```
Two candidates tap same available slot simultaneously.
Without guard: both succeed → double booking.
With guard: first atomic lock acquisition wins.

PROTOCOL:

  STEP 1: UI shows slot as available (non-locking read from cache)

  STEP 2: User taps "Book Slot"

  STEP 3: Backend calls:
    POST /internal/race-guard/acquire
    { resource_type: "interview_slot", resource_id: slot_id }

  STEP 4: IF granted:
    Mark slot status = BOOKING_IN_PROGRESS in PostgreSQL
    Issue confirmation token to user
    Release lock after DB write confirmed
    Emit interview.slot.booked

  STEP 5: IF denied:
    Return HTTP 200:
    { granted: false, reason: "SLOT_ALREADY_CLAIMED", retry_after_ms: N }
    UI shows: "Slot just taken — please choose another"

  ZERO double bookings possible under this protocol.
```

---

---

# SECTION XIII — BELT AWARD RACE GUARD (LOCKED)

## Why Belt Awards Need Special TTL (10,000 ms)

Belt awards trigger:
1. Eligibility verification (DB read)
2. Mentor confirmation check (DB read)
3. Certificate generation (PDF/MinIO write)
4. Immutable audit log write (PostgreSQL)
5. Notification dispatch (Kafka event)
6. Certification record write (PostgreSQL)
7. Belt record update (PostgreSQL)

Steps 1–7 take up to 8 seconds under load. The 10,000 ms TTL covers the full flow with 2 seconds headroom.

## Belt Award Protocol

```
TRIGGER: belt.award.triggered event received

  STEP 1: Acquire lock:
    racelock:belt_award:{user_id}:{belt_level}
    TTL: 10,000 ms

  STEP 2: IF denied:
    Emit belt.award.duplicate_detected
    STOP — do not process

  STEP 3: IF granted:
    Execute full belt award flow (steps 1–7 above)
    IF any step fails → ROLLBACK all writes → release lock
    IF all steps succeed → release lock → emit belt.awarded

  PARTIAL BELT AWARD IS FORBIDDEN.
  All-or-nothing under lock. No exceptions.
```

---

---

# SECTION XIV — SESSION CREATION RACE GUARD (LOCKED)

## Multi-Device Same-Account Login Race

```
Scenario: User opens app on Phone A and Phone B simultaneously.
Both initiate session.create at same millisecond.
Without guard: two valid JWTs issued → RBAC ambiguity → billing double-count.

PROTOCOL:

  STEP 1: Acquire lock:
    racelock:session_create:{user_id}:{device_id}
    TTL: 4,000 ms

  Note: device_id is in the key.
  Two different devices on same user each have own lock.
  This is intentional — one device should not block another.

  STEP 2: IF lock acquired:
    Check: does active session already exist for this device?
    IF yes → resume existing session → release lock
    IF no  → create new session → release lock

  STEP 3: IF lock denied:
    Return SESSION_CREATE_CONFLICT
    Client retries after retry_after_ms

  SESSION CREATION IS ATOMIC PER DEVICE.
  Cross-device conflict is handled by Session Lifecycle Manager (R39A).
```

---

---

# SECTION XV — DATABASE SCHEMA (LOCKED)

## Table: `race_guard_audit_log`

```sql
CREATE TABLE race_guard_audit_log (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  resource_type         TEXT NOT NULL,
  resource_id           TEXT NOT NULL,
  lock_key              TEXT NOT NULL,
  operation_name        TEXT NOT NULL,
  requesting_user_id    UUID NOT NULL REFERENCES users(id),
  requesting_device_id  TEXT NOT NULL,
  idempotency_key       TEXT NOT NULL,
  decision              TEXT NOT NULL CHECK (decision IN (
                          'GRANTED', 'DENIED', 'TTL_EXPIRED',
                          'RELEASED', 'INVALID', 'REDIS_FAILURE'
                        )),
  lock_ttl_ms           INTEGER NOT NULL,
  lock_remaining_ttl_ms INTEGER,
  rejection_reason      TEXT,
  lock_token            TEXT,
  acquired_at           TIMESTAMPTZ,
  released_at           TIMESTAMPTZ,
  processed_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  tenant_id             UUID NOT NULL REFERENCES tenants(id)
);

-- Indexes
CREATE INDEX idx_race_audit_resource   ON race_guard_audit_log(resource_type, resource_id, processed_at);
CREATE INDEX idx_race_audit_user       ON race_guard_audit_log(requesting_user_id, processed_at);
CREATE INDEX idx_race_audit_decision   ON race_guard_audit_log(decision, processed_at);
CREATE INDEX idx_race_audit_lock_key   ON race_guard_audit_log(lock_key, processed_at);
CREATE INDEX idx_race_audit_operation  ON race_guard_audit_log(operation_name, decision, processed_at);
```

**Immutability rule:**
- No UPDATE permitted on this table
- No DELETE permitted on this table
- Governed under R60 (Long-Term Archival Law)
- Retention: minimum 3 years (legal evidence for billing and certification disputes)

---

## Table: `race_guard_slow_release_log`

```sql
CREATE TABLE race_guard_slow_release_log (
  id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  lock_key         TEXT NOT NULL,
  operation_name   TEXT NOT NULL,
  user_id          UUID NOT NULL,
  lock_ttl_ms      INTEGER NOT NULL,
  actual_hold_ms   INTEGER NOT NULL,
  overage_ms       INTEGER GENERATED ALWAYS AS (actual_hold_ms - lock_ttl_ms) STORED,
  detected_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

Populated when a lock's `actual_hold_ms` approaches or exceeds `lock_ttl_ms`.
Used by operations team to identify services holding locks too long.

---

## ClickHouse Analytics Table: `race_guard_stats`

```sql
CREATE TABLE race_guard_stats (
  stat_date          Date,
  stat_hour          UInt8,
  resource_type      String,
  operation_name     String,
  tenant_id          UUID,
  total_requests     UInt64,
  total_granted      UInt64,
  total_denied       UInt64,
  total_ttl_expired  UInt64,
  total_redis_failures UInt64,
  avg_hold_ms        Float32,
  p99_hold_ms        Float32
) ENGINE = SummingMergeTree()
PARTITION BY stat_date
ORDER BY (stat_date, stat_hour, resource_type, operation_name, tenant_id);
```

---

---

# SECTION XVI — API INTERFACE (LOCKED)

## Acquire Lock

**Endpoint:** `POST /internal/race-guard/acquire`
**Auth:** Service-to-service JWT (Keycloak)
**Rate Limit:** 20,000 req/sec per node

### Request Body
```json
{
  "resource_type":      "string",
  "resource_id":        "string",
  "operation_name":     "string",
  "requesting_user_id": "uuid",
  "requesting_device_id": "string",
  "idempotency_key":    "string",
  "timestamp":          "integer (unix ms)"
}
```

### Response: GRANTED
```json
{
  "granted":       true,
  "lock_key":      "racelock:interview_slot:abc123",
  "lock_token":    "unique-release-token-xyz",
  "expires_in_ms": 5000
}
```

### Response: DENIED
```json
{
  "granted":          false,
  "reason":           "SLOT_ALREADY_CLAIMED",
  "lock_key":         "racelock:interview_slot:abc123",
  "retry_after_ms":   3200
}
```

---

## Release Lock

**Endpoint:** `POST /internal/race-guard/release`
**Auth:** Service-to-service JWT (Keycloak)

### Request Body
```json
{
  "lock_key":   "racelock:interview_slot:abc123",
  "lock_token": "unique-release-token-xyz",
  "operation_name": "string",
  "requesting_user_id": "uuid"
}
```

### Response: RELEASED
```json
{
  "released": true,
  "held_for_ms": 1842
}
```

### Response: TOKEN MISMATCH
```json
{
  "released": false,
  "reason": "INVALID_LOCK_TOKEN"
}
```

> Lock tokens prevent one service from accidentally releasing another service's lock.

---

## Check Lock Status (Read-Only)

**Endpoint:** `GET /internal/race-guard/status/{lock_key}`
**Auth:** Service-to-service JWT (Keycloak)

### Response
```json
{
  "lock_key":       "racelock:interview_slot:abc123",
  "is_locked":      true,
  "remaining_ttl_ms": 2100
}
```

---

---

# SECTION XVII — EVENT BUS CONTRACTS (LOCKED)

## Events Emitted by This Agent

### `race_guard.lock.denied`
```json
{
  "event_type": "race_guard.lock.denied",
  "payload": {
    "resource_type":      "string",
    "resource_id":        "string",
    "operation_name":     "string",
    "requesting_user_id": "uuid",
    "rejection_reason":   "string",
    "retry_after_ms":     "integer",
    "timestamp":          "ISO8601"
  }
}
```

### `race_guard.lock.granted`
```json
{
  "event_type": "race_guard.lock.granted",
  "payload": {
    "resource_type":  "string",
    "resource_id":    "string",
    "operation_name": "string",
    "lock_key":       "string",
    "ttl_ms":         "integer",
    "timestamp":      "ISO8601"
  }
}
```

### `race_guard.lock.ttl_expired`
```json
{
  "event_type": "race_guard.lock.ttl_expired",
  "payload": {
    "resource_type":  "string",
    "resource_id":    "string",
    "lock_key":       "string",
    "operation_name": "string",
    "held_for_ms":    "integer",
    "timestamp":      "ISO8601"
  }
}
```

### `race_guard.redis.failure`
```json
{
  "event_type": "race_guard.redis.failure",
  "payload": {
    "redis_error":       "string",
    "failover_mode":     "FAIL_CLOSED",
    "affected_resources": ["array of resource_types"],
    "timestamp":         "ISO8601"
  }
}
```

### `race_guard.belt_award.duplicate_detected`
```json
{
  "event_type": "race_guard.belt_award.duplicate_detected",
  "payload": {
    "user_id":     "uuid",
    "belt_level":  "string",
    "lock_key":    "string",
    "timestamp":   "ISO8601"
  }
}
```

---

---

# SECTION XVIII — INTEGRATION WIRING (MANDATORY)

## Services That MUST Acquire Lock Before Operating

| Service | Operation | Lock Key | Failure Response |
|---|---|---|---|
| Interview Service | Slot booking | `interview_slot:{slot_id}` | `SLOT_ALREADY_CLAIMED` |
| Interview Service | Join token issue | `interview_join:{interview_id}` | `JOIN_IN_PROGRESS` |
| GD Orchestrator | Phase transition | `gd_state:{session_id}:phase_transition` | `STATE_TRANSITION_CONFLICT` |
| GD Orchestrator | Turn token grant | `gd_state:{session_id}:{turn_token}` | `TURN_IN_PROGRESS` |
| Dojo Match Engine | Match seat claim | `dojo_match:{match_id}:seat` | `MATCH_FULL` |
| Dojo Match Engine | Score submission | `score_write:{match_id}:{participant_id}` | `SCORE_WRITE_IN_PROGRESS` |
| Scoring Engine | Mentor override | `score_write:{match_id}:{participant_id}` | `SCORE_WRITE_IN_PROGRESS` |
| Belt Engine | Belt award execution | `belt_award:{user_id}:{belt_level}` | `BELT_AWARD_IN_PROGRESS` |
| Certification Engine | Certificate issuance | `cert_issue:{user_id}:{cert_type}` | `CERT_ISSUE_IN_PROGRESS` |
| Auth Service | Session creation | `session_create:{user_id}:{device_id}` | `SESSION_CREATE_CONFLICT` |
| Billing Service | Subscription change | `subscription:{account_id}` | `SUBSCRIPTION_CHANGE_IN_PROGRESS` |
| Billing Service | Invoice generation | `invoice_gen:{account_id}:{period}` | `INVOICE_GEN_IN_PROGRESS` |
| Commission Engine | Payout trigger | `payout:{society_id}:{cycle_id}` | `PAYOUT_IN_PROGRESS` |
| Commission Engine | Commission calc | `commission_calc:{franchise_id}:{period}` | `COMMISSION_CALC_IN_PROGRESS` |
| Workshop Service | Seat enrollment | `workshop_seat:{workshop_id}` | `WORKSHOP_FULL` |
| Auth Service | Account operation + OTP | `account_op:{user_id}` | `ACCOUNT_OPERATION_IN_PROGRESS` |

**Wiring Enforcement:**
Every listed service MUST call `POST /internal/race-guard/acquire` and check `granted` field before proceeding.
Every listed service MUST call `POST /internal/race-guard/release` after operation completes or fails.

Services that bypass this agent → **WIRING VIOLATION** → **STOP EXECUTION**

---

---

# SECTION XIX — OBSERVABILITY (NON-OPTIONAL)

## Prometheus Metrics

```
# HELP race_guard_lock_requests_total Total lock acquisition requests
# TYPE race_guard_lock_requests_total counter
race_guard_lock_requests_total{resource_type, operation_name, decision, tenant_id}

# HELP race_guard_lock_hold_duration_ms Lock hold duration in milliseconds
# TYPE race_guard_lock_hold_duration_ms histogram
race_guard_lock_hold_duration_ms{resource_type, operation_name}
  buckets: [5, 10, 50, 100, 500, 1000, 2000, 5000, 10000]

# HELP race_guard_redis_latency_ms Redlock acquisition latency
# TYPE race_guard_redis_latency_ms histogram
race_guard_redis_latency_ms{resource_type}
  buckets: [1, 2, 5, 10, 25, 50, 100]

# HELP race_guard_active_locks Current number of active locks
# TYPE race_guard_active_locks gauge
race_guard_active_locks{resource_type}

# HELP race_guard_redis_failures_total Redis cluster failure count
# TYPE race_guard_redis_failures_total counter
race_guard_redis_failures_total{}

# HELP race_guard_ttl_expirations_total Locks that expired before manual release
# TYPE race_guard_ttl_expirations_total counter
race_guard_ttl_expirations_total{resource_type, operation_name}

# HELP race_guard_denial_rate Denial rate per resource type (last 5m)
# TYPE race_guard_denial_rate gauge
race_guard_denial_rate{resource_type}
```

## Grafana Dashboards (Mandatory)

- **Lock Contention Map** — Heatmap showing denial rate per resource_type over time
- **Active Lock Count** — Real-time gauge per resource type
- **Lock Hold Duration P99** — Per operation, alert if > 80% of TTL
- **Redis Latency P99** — Should be < 3ms; alert if > 15ms
- **TTL Expiration Rate** — Alert if > 0 for belt_award or cert_issue
- **Denial Spike Detector** — Alert if denial rate > 20% for any resource in 2-minute window
- **Redis Failure Timeline** — Immediate CRITICAL visibility

## Alerting Rules (All Mandatory)

| Alert | Condition | Severity | Action |
|---|---|---|---|
| `RaceGuardRedisDown` | `redis_failures > 0` for 15s | CRITICAL | Page on-call immediately |
| `RaceGuardBeltDuplicate` | `belt_award DENIED > 0` in 1m | CRITICAL | Review audit log |
| `RaceGuardCertDuplicate` | `cert_issue DENIED > 0` in 1m | HIGH | Review audit log |
| `RaceGuardScoreConflict` | `score_write DENIED > 0` in 5m | HIGH | Review scoring engine |
| `RaceGuardSlotConflict` | `interview_slot denial_rate > 10%` | MEDIUM | Review slot availability UI |
| `RaceGuardLockTTLExpiry` | `ttl_expirations > 0 for belt/cert` | HIGH | Review service hold time |
| `RaceGuardRedisLatency` | `P99 > 15ms` sustained 2m | MEDIUM | Redis cluster health check |
| `RaceGuardHighDenials` | `denial_rate > 30%` any resource 2m | HIGH | Investigate contention pattern |

---

---

# SECTION XX — SECURITY CONSTRAINTS (LOCKED)

- Race guard audit log is **immutable** (no UPDATE, no DELETE)
- Lock keys contain **no PII** — only resource UUIDs and type identifiers
- Lock tokens are **single-use** — released by token, preventing cross-service release
- Advisory lock hash uses `hashtext()` which is PostgreSQL-native — no external dependency
- All `/internal/race-guard/*` endpoints are **not exposed externally**
- Kong API Gateway **blocks all external access** to `/internal/*` namespace
- Service-to-service calls authenticated via **Keycloak service JWT**
- Lock token values stored in audit log for forensic traceability
- Redis Redlock uses **quorum-based acquisition** across 3+ Redis nodes — no single-node bypass

---

---

# SECTION XXI — DEPLOYMENT SPECIFICATION (LOCKED)

## Kubernetes Namespace

```
Namespace: realtime
Service Name: phone-race-condition-guard-agent
```

## Resource Defaults (Startup-Stage)

```yaml
resources:
  requests:
    cpu: "150m"
    memory: "192Mi"
  limits:
    cpu: "750m"
    memory: "512Mi"

replicas: 3
autoscaling:
  minReplicas: 3
  maxReplicas: 15
  targetCPUUtilizationPercentage: 55
```

> 3 minimum replicas required because Redlock quorum requires Redis cluster health.
> Under heavy GD + Dojo + Interview concurrent load, autoscaling to 15 nodes is expected.

## Health Check Endpoints

```
GET /health/live   → 200 OK if process alive
GET /health/ready  → 200 OK if Redis cluster quorum reachable + PostgreSQL reachable
```

**Note:** `/health/ready` checks Redis **quorum** (majority of nodes), not just one node.
A single Redis node failure does NOT make this service unready — quorum is still achievable.
Quorum loss → service returns 503 → Kubernetes marks unready → traffic rerouted.

## Environment Variables Required

```
REDIS_NODES              # Comma-separated list of Redis cluster nodes
REDIS_PASSWORD
REDIS_REDLOCK_DRIFT_FACTOR  # Default: 0.01
REDIS_REDLOCK_RETRY_COUNT   # MUST be 0 (no retry — fail immediately)
REDIS_REDLOCK_RETRY_DELAY   # MUST be 0
POSTGRES_HOST
POSTGRES_DB
POSTGRES_USER
POSTGRES_PASSWORD
KAFKA_BROKER_URL
KEYCLOAK_JWKS_URL
LOG_LEVEL
RACE_GUARD_FAIL_MODE     # MUST be "FAIL_CLOSED"
SLOW_RELEASE_THRESHOLD_FACTOR  # Default: 0.8 (alert when hold > 80% of TTL)
```

---

---

# SECTION XXII — FAILURE SCENARIOS & SYSTEM RESPONSES (LOCKED)

| Failure Scenario | Agent Response |
|---|---|
| Redis quorum lost (majority of nodes down) | FAIL-CLOSED — deny all lock requests — CRITICAL alert |
| Single Redis node down | Continue with quorum — log warning — no service impact |
| Lock TTL expires before manual release | Lock auto-releases — emit `ttl_expired` — log in slow_release_log |
| Caller crashes without releasing lock | TTL auto-releases — no manual cleanup required |
| Service calls release with wrong lock_token | Reject with `INVALID_LOCK_TOKEN` — no release — log security audit |
| Service never calls release | TTL releases — log in slow_release_log — alert if pattern repeats |
| PostgreSQL advisory lock returns FALSE | Service rolls back transaction — returns CONFLICT — no partial write |
| Belt award lock expires mid-process | ALL belt award writes rolled back — emit belt.award.failed — re-trigger eligibility check |
| Score write lock expires mid-process | ALL score writes rolled back — emit score.write.failed — client receives retry signal |
| Two services request same lock simultaneously | Redlock quorum — only one granted — other denied — race resolved correctly |
| Unknown resource_type in request | Reject with `UNKNOWN_RESOURCE_TYPE` — no lock acquired |
| Lock request with missing fields | Reject with `INVALID_REQUEST` — no lock acquired |

---

---

# SECTION XXIII — INTERN EXECUTION GUIDE

## Service Location

```
/backend/services/phone_race_condition_guard_agent/
```

## Step-by-Step Local Execution

**Step 1:** Navigate to service folder
```bash
cd /backend/services/phone_race_condition_guard_agent/
```

**Step 2:** Create virtual environment
```bash
python -m venv venv
source venv/bin/activate   # Mac/Linux
venv\Scripts\activate      # Windows
```

**Step 3:** Install dependencies
```bash
pip install -r requirements.txt
```

**Step 4:** Copy env template
```bash
cp .env.example .env
# Edit .env with local Redis cluster nodes and DB values
# Set RACE_GUARD_FAIL_MODE=FAIL_CLOSED
# Set REDIS_REDLOCK_RETRY_COUNT=0
```

**Step 5:** Run database migration
```bash
alembic upgrade head
```

**Step 6:** Start the service
```bash
uvicorn main:app --reload --port 8091
```

**Expected Output:**
```
INFO: Uvicorn running on http://127.0.0.1:8091
INFO: PHONE_RACE_CONDITION_GUARD_AGENT ready
INFO: Redis quorum: OK (3/3 nodes reachable)
INFO: PostgreSQL connection: OK
INFO: Fail mode: FAIL_CLOSED
INFO: Retry mode: ZERO_RETRY (immediate rejection on contention)
```

**Step 7:** Test via Swagger
```
http://127.0.0.1:8091/docs
```

**Step 8:** Test race condition scenario
```bash
# Fire two concurrent lock requests for same resource
# One should return granted=true, one should return granted=false
# Both should appear in race_guard_audit_log
curl -X POST http://localhost:8091/internal/race-guard/acquire \
  -H "Content-Type: application/json" \
  -d '{"resource_type":"interview_slot","resource_id":"slot-001",...}' &

curl -X POST http://localhost:8091/internal/race-guard/acquire \
  -H "Content-Type: application/json" \
  -d '{"resource_type":"interview_slot","resource_id":"slot-001",...}' &
```

**Expected:** One GRANTED, one DENIED.
**If both GRANTED → CRITICAL BUG → STOP EXECUTION**

**Failure → STOP EXECUTION**

---

---

# SECTION XXIV — CONTRACT GATE STATUS

| Contract | Status |
|---|---|
| Race Condition Taxonomy | LOCKED |
| Resource Type Registry | LOCKED |
| Lock TTL Table | LOCKED |
| Redis Redlock Protocol | LOCKED |
| PostgreSQL Advisory Lock Protocol | LOCKED |
| Lock Key Schema | LOCKED |
| Execution Flow Algorithm | LOCKED |
| Fail-Closed Mode Declaration | LOCKED |
| Audit Log Schema | LOCKED |
| API Interface Contract | LOCKED |
| Event Bus Contracts | LOCKED |
| Service Integration Wiring | LOCKED |
| Observability Metrics + Alerts | LOCKED |
| GD State Machine Protocol | LOCKED |
| Dojo Match Engine Protocol | LOCKED |
| Belt Award Protocol | LOCKED |
| Deployment Specification | LOCKED |

**➤ ALL CONTRACT GATES PASSED**
**➤ AGENT STATUS: SEALED · LOCKED · DEPLOYMENT-READY**

---

---

# SECTION XXV — BOUND LAWS & CROSS-REFERENCES

| Law / Spec | Binding Clause |
|---|---|
| R1 — Technology Stack Lock | Redis 7 (cluster mode), PostgreSQL 15, FastAPI, Kong enforced |
| R4 — Event Schema Registry | All emitted events registered in AsyncAPI before deployment |
| R5 — Workflow State Machines | GD and Dojo state machines locked by this agent's turn guard |
| R7 — Voice GD Orchestrator (Critical) | Turn token grant/revoke is race-guarded by this agent |
| R8 — Dojo Match Engine | Seat claim and score submission race-guarded |
| R9 — Scoring Engine (Locked) | Mentor override and score write locked by this agent |
| R10 — Certification & Belt Engine | Belt award locked by this agent (10,000 ms TTL) |
| R13 — Billing & Subscription Service | Subscription change and invoice generation locked |
| R38 — Master Bug Registry | All race scenarios above pre-registered in bug registry |
| R39A — Session Lifecycle Manager | Session creation race-guarded |
| R44 — Runnable Backend Law | Agent fully runnable with Redis cluster + DB wiring |
| R49 — Contract Validator CLI | All contracts validated before every build |
| R60 — Archival Law | Audit log under WORM-style 3-year retention |
| PHONE_EVENT_DEDUP_AGENT v1.0 | Companion agent — both deployed together — neither substitutes the other |
| GD System Spec (Jitsi + Rule Orchestration) | GD turn machine protected by this agent's state transition locks |
| Dojo SAAS v1.0 Spec | Dojo match seat and scoring integrity locked |
| Society Architecture v1.0 | Commission calculation and payout locks implemented |
| Infrastructure Audit v8 | Redis cluster (not single-node) required for Redlock quorum |

---

---

# SECTION XXVI — ANTIGRAVITY LAYER DECLARATION (LOCKED)

## Position of This Agent in the Antigravity Layer

The **Antigravity Layer** in ECOSKILLER has two structural agents:

```
┌─────────────────────────────────────────────────────────────┐
│                   ANTIGRAVITY LAYER                         │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  PHONE_EVENT_DEDUPLICATION_AGENT                    │   │
│  │  Stops: Same event sent N times                     │   │
│  │  Method: Redis SETNX fingerprint window             │   │
│  │  Mode: FAIL-OPEN                                    │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  PHONE_RACE_CONDITION_GUARD_AGENT                   │   │
│  │  Stops: Two different requests claiming same thing  │   │
│  │  Method: Redis Redlock + PostgreSQL advisory lock   │   │
│  │  Mode: FAIL-CLOSED                                  │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  Both present = Platform does not collapse under load.      │
│  Either missing = Platform state corruption is inevitable.  │
└─────────────────────────────────────────────────────────────┘
```

Without this agent, ECOSKILLER's distributed mobile-first architecture — running real-time GD sessions, concurrent Dojo matches, simultaneous campus slot bookings, parallel belt awards, and concurrent Society payouts — would experience **state corruption at scale.**

**This agent is not optional infrastructure.**
**It is the second load-bearing pillar of the Antigravity Layer.**

---

---

# 🔒 FINAL SEAL

```
AGENT:      PHONE_RACE_CONDITION_GUARD_AGENT
DOMAIN:     Session & Lifecycle → Antigravity Layer
SYSTEM:     ECOSKILLER v12.0
STATUS:     SEALED
VERSION:    v1.0
COMPANION:  PHONE_EVENT_DEDUPLICATION_AGENT v1.0
MUTATION:   ADD-ONLY VIA VERSION BUMP
AUTHORITY:  HUMAN DECLARATION ONLY

NO CLAUSE MAY BE WEAKENED.
NO CLAUSE MAY BE REINTERPRETED.
NO PARTIAL IMPLEMENTATION PERMITTED.
FAIL-CLOSED MODE IS NON-NEGOTIABLE.
ZERO-RETRY MODE IS NON-NEGOTIABLE.

Violation → STOP EXECUTION → REPORT RACE_GUARD_SEAL_BREACH
```

---

*PHONE_RACE_CONDITION_GUARD_AGENT · ECOSKILLER Session & Lifecycle · Antigravity Layer · v1.0 · SEALED*
