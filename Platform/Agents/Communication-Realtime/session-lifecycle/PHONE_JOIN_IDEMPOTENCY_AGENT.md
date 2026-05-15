# PHONE_JOIN_IDEMPOTENCY_AGENT
## Session & Lifecycle — Antigravity Layer

```
ECOSKILLER — PHONE_JOIN_IDEMPOTENCY_AGENT
Status:                  FINAL · LOCKED · GOVERNED · DETERMINISTIC
Mutation Policy:         Add-only via version bump
Interpretation Authority: NONE
Execution Authority:     Human declaration only
Agent Class:             Session & Lifecycle / Antigravity Tier
Agent Code:              ECSK-AGENT-JOIN-IDEM-002
Version:                 1.0.0 SEALED
```

---

## SECTION A — AGENT IDENTITY

```
Agent Name:         PHONE_JOIN_IDEMPOTENCY_AGENT
Agent Code:         ECSK-AGENT-JOIN-IDEM-002
Domain:             Session & Lifecycle
Sub-Domain:         Room Join · Entry Lock · Idempotency Enforcement
Tier:               Antigravity
System Context:     ECOSKILLER Unified SaaS Platform
Covered Surfaces:
  - Voice GD Room Join (Jitsi)
  - Dojo Match Room Join (LiveKit)
  - Interview Room Join (LiveKit)
  - Campus Placement Portal Join Flows
  - Dojo-for-Arts Arena Join
Execution Mode:     Event-Driven · Stateless Agent · Stateful Infrastructure
Determinism Rule:   Identical join_request_id → Identical idempotent outcome
Failure Mode:       STOP → LOG → EMIT DEAD_LETTER_EVENT → NO DUPLICATE ROOM ENTRY
Version:            1.0.0 SEALED
```

---

## SECTION B — AGENT PURPOSE

The `PHONE_JOIN_IDEMPOTENCY_AGENT` is the **sole authority** for
guaranteeing that every phone-originated join request into any
ECOSKILLER room (GD, Dojo, Interview, Arena) is processed
**exactly once**, regardless of:

- Network retries from Flutter mobile clients
- Double-tap join button presses
- Background OS app restores
- OS-killed app relaunches with cached deep-links
- WebSocket reconnections during join flow
- Duplicate Kafka events from upstream services
- Race conditions across multiple replicas of room services

It enforces that:

- A participant occupies **exactly one seat** in a room at any time.
- A join token is issued **exactly once** per eligible join window.
- Duplicate join attempts return the **original cached response**,
  never create a second session.
- All join attempts — successful, duplicate, rejected, or timed-out —
  are immutably logged with full forensic detail.
- The GD Orchestrator, Dojo Match Engine, and Interview Service
  receive **clean, deduplicated join signals** only.

**Human judgment is removed.**
**AI inference is removed.**
**Only idempotency keys, state machines, and enforcement remain.**

---

## SECTION C — CORE DESIGN PHILOSOPHY

```
Replace retry chaos with idempotency keys
Replace duplicate seats with atomic seat claims
Replace race conditions with distributed locks
Replace ambiguous join state with deterministic outcomes
Replace phone reconnect panic with graceful re-entry
```

The system explicitly avoids:

- Allowing two active sessions for one participant in one room
- Issuing two media tokens for the same (user_id, room_id) pair
- Charging billing events twice for a single join
- Emitting `participant.joined` Kafka events more than once per join

The system operates only on:

- Idempotency keys
- Distributed locks
- Atomic seat claims
- Deterministic join window enforcement
- Cryptographic join tokens

---

## SECTION D — NON-NEGOTIABLE OPERATING RULES

```
RULE-1:  Every join request MUST carry a client-generated
         idempotency_key (UUID v4). Requests without one are REJECTED.

RULE-2:  Idempotency keys are scoped to (user_id, room_id, join_type).
         Cross-room reuse of a key is a SECURITY VIOLATION.

RULE-3:  Idempotency key TTL = join_window_duration + 300s grace.
         After TTL, key expires and room cannot be re-entered.

RULE-4:  Seat claim is atomic. Redis SETNX is the lock.
         No two threads may claim the same seat simultaneously.

RULE-5:  Media token (Jitsi JWT / LiveKit token) is issued ONCE.
         Duplicate requests return the CACHED token, not a new one.

RULE-6:  join_window_start and join_window_end are set by the
         GD Orchestrator / Match Engine at room creation.
         This agent ENFORCES them — it does NOT set them.

RULE-7:  Late joiners (past join_window_end) become SPECTATORS only.
         They receive no media token and no speaking rights.
         This is enforced per the GD Automated System document.

RULE-8:  Rejoin after disconnection is permitted ONLY within
         the active session window and only if original seat
         is still held (Redis seat key still exists).
         Rejoin issues the SAME cached token — no new token.

RULE-9:  If a room is FULL (max_participants reached),
         join returns ROOM_FULL immediately. No waiting.

RULE-10: All join outcomes (JOINED, DUPLICATE, REJECTED,
         LATE, ROOM_FULL, TOKEN_SERVED_FROM_CACHE) are
         emitted as Kafka events and written to audit log.

RULE-11: Agent never directly controls Jitsi or LiveKit.
         It issues tokens. Media stack obeys tokens.
         Backend governs. Jitsi executes.

RULE-12: Billing event `participant.seat.claimed` fires ONCE
         per idempotency key, never on duplicate resolution.
```

Violation of any rule → `STOP AGENT` → `EMIT JOIN_IDEMPOTENCY_FAILURE EVENT`

---

## SECTION E — SYSTEM PLACEMENT (NON-NEGOTIABLE)

### Architecture Position

```
Flutter App / PWA (Phone Client)
        │
        │  POST /join  { idempotency_key, room_id, user_id, join_type }
        ▼
  Kong API Gateway  (rate limit: 5 join requests / 10s per user)
        │
        ▼
  PHONE_JOIN_IDEMPOTENCY_AGENT          ← THIS AGENT
        │
        ├──▶ Redis
        │       ├── Idempotency Key Store  (HASH, TTL-bound)
        │       ├── Seat Claim Lock        (SETNX, atomic)
        │       ├── Token Cache            (STRING, TTL = session duration)
        │       └── Room Participant Set   (SADD, cardinality = seat count)
        │
        ├──▶ PostgreSQL
        │       ├── join_attempts          (full audit record)
        │       └── room_seats             (persistent seat map)
        │
        ├──▶ Kafka
        │       ├── PUBLISH room.participant.joined
        │       ├── PUBLISH room.join.duplicate_resolved
        │       ├── PUBLISH room.join.rejected
        │       └── PUBLISH room.participant.seat.claimed (billing trigger)
        │
        ├──▶ GD Voice Orchestrator         (consumes room.participant.joined)
        ├──▶ Dojo Match Engine             (consumes room.participant.joined)
        ├──▶ Interview Service             (consumes room.participant.joined)
        └──▶ Billing Service              (consumes room.participant.seat.claimed)
```

### Traffic Class

```
Primary:   HTTP REST — POST /api/v1/rooms/{room_id}/join
Secondary: HTTP REST — GET  /api/v1/rooms/{room_id}/join/status
Async:     Kafka events (outbound only — agent is publisher, not consumer)
```

### Forbidden Interactions

```
✗  Agent may NOT call Jitsi API directly.
✗  Agent may NOT call LiveKit server API directly.
✗  Agent may NOT set join_window_start / join_window_end
   (these are owned by GD Orchestrator / Match Engine).
✗  Agent may NOT score, rank, or evaluate participants.
✗  Agent may NOT access ClickHouse or OpenSearch.
✗  Agent may NOT issue JWT access tokens (Auth Service owns this).
✗  Agent may NOT modify room topics or GD batch assignments.
```

---

## SECTION F — JOIN FLOW STATE MACHINE

### States per (user_id, room_id) pair

```
ABSENT → JOINING → SEAT_CLAIMED → TOKEN_ISSUED → ACTIVE
                                               → DISCONNECTED → REJOINING → ACTIVE
              │
              ├── LATE_SPECTATOR   (join after window_end)
              ├── REJECTED         (ineligible, banned, RBAC fail)
              └── ROOM_FULL        (max_participants reached)
```

### Transition Table

| From            | Event                          | To               | Action                                                           |
|-----------------|-------------------------------|------------------|------------------------------------------------------------------|
| ABSENT          | POST /join (first, in window) | JOINING          | Check eligibility, acquire Redis lock                            |
| JOINING         | Seat available                | SEAT_CLAIMED     | SETNX seat key, write room_seats row, emit seat.claimed          |
| JOINING         | Room full                     | ROOM_FULL        | Release lock, return 409, emit join.rejected(ROOM_FULL)          |
| JOINING         | Ineligible (RBAC/ban)         | REJECTED         | Release lock, return 403, emit join.rejected(INELIGIBLE)         |
| JOINING         | After window_end              | LATE_SPECTATOR   | Spectator token issued (no mic rights), emit join.spectator      |
| SEAT_CLAIMED    | Token issued                  | TOKEN_ISSUED     | Cache token in Redis, emit participant.joined                    |
| TOKEN_ISSUED    | Client connected to room      | ACTIVE           | Notify GD Orchestrator / Match Engine via Kafka                  |
| ACTIVE          | Network drop / app background | DISCONNECTED     | Retain seat claim in Redis, start rejoin grace timer             |
| DISCONNECTED    | POST /join (duplicate key)    | REJOINING        | Validate idempotency key, serve CACHED token                     |
| REJOINING       | Token served                  | ACTIVE           | Emit join.duplicate_resolved (no new billing event)              |
| ACTIVE          | Room terminated               | ABSENT           | Release seat claim, delete Redis keys, emit participant.left     |
| REJOINING       | Rejoin grace expired          | ABSENT           | Seat released, rejoin denied, emit seat.released                 |
| ABSENT          | POST /join (duplicate key,    | DUPLICATE_STALE  | Return 410 GONE — window closed, key expired                     |
|                 | key TTL expired)              |                  |                                                                  |

### Idempotency Resolution Matrix

| Scenario                                           | Agent Response                                 |
|----------------------------------------------------|------------------------------------------------|
| First join, valid window, seat available           | 201 CREATED — new seat + new token             |
| Duplicate join, same idempotency_key, seat active  | 200 OK — cached token returned, no side effects|
| Duplicate join, same idempotency_key, disconnected | 200 OK — cached token returned, rejoin path    |
| Duplicate join, different idempotency_key, same room| 409 CONFLICT — already seated in this room    |
| Join after window closed, first attempt            | 200 OK (Spectator) — no mic rights             |
| Join after window closed, duplicate attempt        | 200 OK (Spectator) — cached spectator token    |
| Room full, any join attempt                        | 409 ROOM_FULL                                  |
| Ineligible user (RBAC, ban, wrong tenant)          | 403 FORBIDDEN                                  |
| Expired idempotency key (TTL elapsed)              | 410 GONE                                       |
| Malformed request (missing idempotency_key)        | 400 BAD REQUEST                                |

---

## SECTION G — DATA LAYER CONTRACTS

### PostgreSQL Schema

```sql
-- Join Attempt Audit Table (immutable, append-only)
CREATE TABLE join_attempts (
    attempt_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    idempotency_key    UUID NOT NULL,
    user_id            UUID NOT NULL REFERENCES users(id),
    room_id            UUID NOT NULL,
    room_type          TEXT NOT NULL CHECK (room_type IN
                         ('gd_voice','dojo_match','interview','arena')),
    tenant_id          UUID NOT NULL REFERENCES tenants(id),
    device_platform    TEXT CHECK (device_platform IN
                         ('android','ios','pwa','desktop')),
    join_outcome       TEXT NOT NULL CHECK (join_outcome IN (
                         'JOINED','DUPLICATE_RESOLVED','REJECTED',
                         'LATE_SPECTATOR','ROOM_FULL','GONE','BAD_REQUEST')),
    rejection_reason   TEXT,
    token_served_from  TEXT CHECK (token_served_from IN ('FRESH','CACHE')),
    client_ip          INET,
    user_agent         TEXT,
    attempted_at       TIMESTAMPTZ DEFAULT now(),
    resolved_at        TIMESTAMPTZ
);

-- Room Seat Map (persistent seat ownership)
CREATE TABLE room_seats (
    seat_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    room_id            UUID NOT NULL,
    room_type          TEXT NOT NULL,
    user_id            UUID NOT NULL,
    tenant_id          UUID NOT NULL,
    idempotency_key    UUID NOT NULL UNIQUE,
    seat_status        TEXT NOT NULL DEFAULT 'ACTIVE'
                         CHECK (seat_status IN ('ACTIVE','DISCONNECTED','RELEASED')),
    claimed_at         TIMESTAMPTZ DEFAULT now(),
    disconnected_at    TIMESTAMPTZ,
    released_at        TIMESTAMPTZ,
    UNIQUE (room_id, user_id)  -- one seat per user per room
);

-- Indexes
CREATE INDEX idx_join_attempts_idem_key   ON join_attempts(idempotency_key);
CREATE INDEX idx_join_attempts_user_room  ON join_attempts(user_id, room_id);
CREATE INDEX idx_room_seats_room_id       ON room_seats(room_id);
CREATE INDEX idx_room_seats_user_id       ON room_seats(user_id);
```

### Redis Key Contracts

```
# Idempotency Key Store
Type:    HASH
Key:     idem:{idempotency_key}
Fields:
  - user_id           UUID
  - room_id           UUID
  - room_type         TEXT
  - outcome           TEXT
  - token_ref         TEXT   (Redis key of cached token, NOT raw token)
  - resolved_at       EPOCH_MS
TTL:     join_window_duration + 300s grace (set at key creation)

# Seat Claim Lock (atomic SETNX)
Type:    STRING
Key:     seat:{room_id}:{user_id}
Value:   idempotency_key
TTL:     room_session_duration + 120s buffer
Use:     SETNX only — never SET. Prevents double-seat race.

# Token Cache
Type:    STRING
Key:     token:{room_id}:{user_id}
Value:   <encrypted_media_token>   (Jitsi JWT or LiveKit token)
TTL:     room_session_duration + 60s
Note:    Value is AES-256-GCM encrypted. Raw token never stored in plain.

# Room Participant Set (cardinality = live seat count)
Type:    SET
Key:     room:{room_id}:participants
Members: user_id (UUID strings)
Use:     SADD on seat claim, SREM on seat release
         SCARD to check against max_participants

# Rejoin Grace Timer
Type:    STRING
Key:     rejoin:{room_id}:{user_id}:grace
Value:   "1"
TTL:     REJOIN_GRACE_SECONDS (default: 120s, configurable per room_type)
Note:    Expires → seat auto-released, SREM from participant set
```

---

## SECTION H — API CONTRACTS

### REST Endpoints

```yaml
# PRIMARY: JOIN ROOM
POST /api/v1/rooms/{room_id}/join
Headers:
  Authorization:     Bearer <access_token>
  Idempotency-Key:   <UUID v4>            # REQUIRED — 400 if absent
Body:
  {
    "room_type":        "gd_voice | dojo_match | interview | arena",
    "device_platform":  "android | ios | pwa | desktop"
  }

Response 201 CREATED (new seat, fresh token):
  {
    "join_outcome":     "JOINED",
    "seat_id":          "<UUID>",
    "media_token":      "<Jitsi-JWT or LiveKit-token>",
    "room_name":        "<room_identifier>",
    "participant_role": "ACTIVE | SPECTATOR",
    "join_window_end":  "<ISO8601>",
    "token_served_from":"FRESH"
  }

Response 200 OK (duplicate, cached token returned):
  {
    "join_outcome":     "DUPLICATE_RESOLVED",
    "seat_id":          "<UUID>",
    "media_token":      "<cached-token>",
    "room_name":        "<room_identifier>",
    "participant_role": "ACTIVE | SPECTATOR",
    "join_window_end":  "<ISO8601>",
    "token_served_from":"CACHE"
  }

Response 200 OK (late, spectator only):
  {
    "join_outcome":     "LATE_SPECTATOR",
    "media_token":      "<spectator-token-no-mic>",
    "participant_role": "SPECTATOR",
    "token_served_from":"FRESH | CACHE"
  }

Response 409 CONFLICT (room full):
  { "error": "ROOM_FULL", "max_participants": 6, "current": 6 }

Response 403 FORBIDDEN (ineligible):
  { "error": "INELIGIBLE", "reason": "<RBAC | BANNED | WRONG_TENANT>" }

Response 410 GONE (idempotency key expired):
  { "error": "GONE", "reason": "JOIN_WINDOW_CLOSED" }

Response 400 BAD REQUEST (missing idempotency key):
  { "error": "MISSING_IDEMPOTENCY_KEY" }

---

# QUERY: JOIN STATUS (for reconnect polling)
GET /api/v1/rooms/{room_id}/join/status
Headers:
  Authorization:   Bearer <access_token>
  Idempotency-Key: <UUID v4>

Response 200:
  {
    "seat_status":       "ACTIVE | DISCONNECTED | RELEASED | ABSENT",
    "participant_role":  "ACTIVE | SPECTATOR",
    "rejoin_grace_remaining_seconds": 87
  }
```

---

## SECTION I — KAFKA EVENT SCHEMA (ASYNCAPI)

```yaml
asyncapi: 2.6.0
info:
  title: PHONE_JOIN_IDEMPOTENCY_AGENT Events
  version: 1.0.0

channels:

  room.participant.joined:
    publish:
      message:
        payload:
          idempotency_key:  { type: string, format: uuid }
          user_id:          { type: string, format: uuid }
          room_id:          { type: string, format: uuid }
          room_type:        { type: string }
          participant_role: { type: string }
          device_platform:  { type: string }
          tenant_id:        { type: string }
          joined_at:        { type: string, format: date-time }
          token_source:     { type: string }   # FRESH | CACHE

  room.join.duplicate_resolved:
    publish:
      message:
        payload:
          idempotency_key:  { type: string }
          user_id:          { type: string }
          room_id:          { type: string }
          resolved_at:      { type: string }
          token_source:     { type: string }   # always CACHE
          note:             { type: string }   # "no billing event fired"

  room.join.rejected:
    publish:
      message:
        payload:
          idempotency_key:  { type: string }
          user_id:          { type: string }
          room_id:          { type: string }
          rejection_reason: { type: string }   # ROOM_FULL | INELIGIBLE | GONE
          attempted_at:     { type: string }

  room.participant.seat.claimed:
    publish:
      description: >
        Billing trigger. Emitted ONCE per idempotency_key.
        NEVER emitted on duplicate resolution.
      message:
        payload:
          idempotency_key:  { type: string }
          user_id:          { type: string }
          room_id:          { type: string }
          room_type:        { type: string }
          tenant_id:        { type: string }
          claimed_at:       { type: string }

  room.participant.spectator.joined:
    publish:
      message:
        payload:
          user_id:          { type: string }
          room_id:          { type: string }
          joined_at:        { type: string }
          note:             { type: string }   # "no mic rights, no billing"

  room.participant.seat.released:
    publish:
      message:
        payload:
          user_id:          { type: string }
          room_id:          { type: string }
          release_reason:   { type: string }   # GRACE_EXPIRED | ROOM_ENDED | USER_LEFT
          released_at:      { type: string }

  room.join.idempotency_failure:
    publish:
      message:
        payload:
          idempotency_key:  { type: string }
          user_id:          { type: string }
          room_id:          { type: string }
          error_code:       { type: string }
          error_detail:     { type: string }
          occurred_at:      { type: string }
```

---

## SECTION J — CORE ALGORITHMS

### Algorithm 1 — Atomic Seat Claim (SETNX-based)

```
FUNCTION claim_seat(user_id, room_id, idempotency_key):

  # Step 1: Check idempotency key — has this join been processed?
  existing = HGETALL idem:{idempotency_key}
  IF existing exists:
    RETURN resolve_duplicate(existing)   # Algorithm 2

  # Step 2: Validate join window
  window = GET room:{room_id}:window  # {start, end, max_participants}
  now = server_utc_timestamp()

  IF now > window.end:
    RETURN issue_spectator_token(user_id, room_id, idempotency_key)

  IF now < window.start:
    RETURN REJECTED(reason="WINDOW_NOT_OPEN")

  # Step 3: Check if user already seated (different idempotency_key)
  existing_seat = GET seat:{room_id}:{user_id}
  IF existing_seat exists AND existing_seat != idempotency_key:
    RETURN CONFLICT(reason="ALREADY_SEATED_DIFFERENT_KEY")

  # Step 4: Check room capacity
  current_count = SCARD room:{room_id}:participants
  IF current_count >= window.max_participants:
    RETURN REJECTED(reason="ROOM_FULL")

  # Step 5: Atomic seat claim
  claimed = SETNX seat:{room_id}:{user_id} idempotency_key
  IF NOT claimed:
    RETURN REJECTED(reason="SEAT_RACE_LOST")   # Another replica won

  # Step 6: Add to participant set
  SADD room:{room_id}:participants user_id

  # Step 7: Issue media token
  token = TokenIssuer.generate(user_id, room_id, room_type)
  SET token:{room_id}:{user_id} ENCRYPT(token) EX room_session_duration+60

  # Step 8: Write idempotency record
  HMSET idem:{idempotency_key}
    user_id         user_id
    room_id         room_id
    outcome         JOINED
    token_ref       token:{room_id}:{user_id}
    resolved_at     now()
  EXPIRE idem:{idempotency_key} (window.end - now + 300)

  # Step 9: Persist to PostgreSQL (async, non-blocking)
  ASYNC write room_seats + join_attempts

  # Step 10: Emit events
  EMIT room.participant.seat.claimed   # billing trigger — ONCE
  EMIT room.participant.joined         # GD / Dojo / Interview consumers

  RETURN 201 CREATED { token, seat_id, role: ACTIVE }
```

### Algorithm 2 — Duplicate Resolution

```
FUNCTION resolve_duplicate(existing_idem_record):

  # Serve cached token — ZERO side effects
  cached_token = DECRYPT GET {existing_idem_record.token_ref}

  IF cached_token exists:
    EMIT room.join.duplicate_resolved   # no billing event
    RETURN 200 OK {
      token: cached_token,
      token_served_from: CACHE,
      outcome: DUPLICATE_RESOLVED
    }

  # Token cache expired but seat may still be valid (edge case)
  seat = GET seat:{room_id}:{user_id}
  IF seat exists:
    token = TokenIssuer.generate(user_id, room_id, room_type)
    SET token:{room_id}:{user_id} ENCRYPT(token) EX 300  # short grace
    EMIT room.join.duplicate_resolved
    RETURN 200 OK { token: token, token_served_from: REGENERATED }

  # Both expired — join window fully closed
  RETURN 410 GONE
```

### Algorithm 3 — Rejoin Grace (Antigravity — Network Drop Recovery)

```
FUNCTION handle_rejoin(user_id, room_id, idempotency_key):

  # Step 1: Validate idempotency key is the same as original join
  existing = HGETALL idem:{idempotency_key}
  IF NOT existing:
    RETURN 410 GONE   # Original join window expired

  # Step 2: Check seat still held
  seat = GET seat:{room_id}:{user_id}
  IF NOT seat:
    RETURN 403 FORBIDDEN(reason="SEAT_RELEASED_GRACE_EXPIRED")

  # Step 3: Check rejoin grace window
  grace = EXISTS rejoin:{room_id}:{user_id}:grace
  IF NOT grace:
    RETURN 403 FORBIDDEN(reason="GRACE_WINDOW_EXPIRED")

  # Step 4: Serve CACHED token (never reissue)
  RETURN resolve_duplicate(existing)   # Algorithm 2
```

### Algorithm 4 — Seat Release (Room End or Grace Expiry)

```
FUNCTION release_seat(user_id, room_id, reason):

  DEL  seat:{room_id}:{user_id}
  SREM room:{room_id}:participants user_id
  DEL  token:{room_id}:{user_id}
  DEL  rejoin:{room_id}:{user_id}:grace

  UPDATE room_seats SET seat_status='RELEASED', released_at=now()
  WHERE user_id=user_id AND room_id=room_id

  EMIT room.participant.seat.released { reason: reason }
```

### Algorithm 5 — Late Spectator Token Issuance

```
FUNCTION issue_spectator_token(user_id, room_id, idempotency_key):

  # Spectator tokens grant listen access, NO mic rights
  # Compliant with GD document: "Late joiners become spectators only"

  token = TokenIssuer.generate_spectator(user_id, room_id)
  SET token:{room_id}:{user_id}:spectator ENCRYPT(token) EX 300

  HMSET idem:{idempotency_key}
    outcome       LATE_SPECTATOR
    token_ref     token:{room_id}:{user_id}:spectator
    resolved_at   now()
  EXPIRE idem:{idempotency_key} 300

  EMIT room.participant.spectator.joined

  RETURN 200 OK {
    outcome:          LATE_SPECTATOR,
    participant_role: SPECTATOR,
    media_token:      token
  }
```

---

## SECTION K — FAILURE HANDLING

| Failure Condition                          | Agent Action                                                      |
|--------------------------------------------|-------------------------------------------------------------------|
| Redis SETNX race lost (two replicas)       | Losing replica returns SEAT_RACE_LOST · Client retries cleanly    |
| Redis unavailable                          | Fallback to PostgreSQL seat check (read-only) · DEGRADED_MODE alert|
| PostgreSQL write failure (async)           | Redis remains source-of-truth · Alert Ops · Async retry queue     |
| Kafka publish failure                      | Retry ×3 exponential backoff · Dead-letter on 3rd failure         |
| Token issuer (Jitsi/LiveKit) timeout       | Return 503, client retries with SAME idempotency_key              |
| Idempotency key missing from request       | 400 BAD_REQUEST immediately — no processing                       |
| Idempotency key reused across rooms        | 422 UNPROCESSABLE — security violation logged                     |
| Rejoin grace expired mid-reconnect         | 403 FORBIDDEN — seat released, new join required (new window)     |
| Room not found                             | 404 NOT_FOUND immediately                                         |
| Agent process crash (Kubernetes restart)   | Redis preserves all seat state — agent resumes stateless          |
| Billing service unreachable                | seat.claimed event goes to Kafka dead-letter · retried by billing |
| Concurrent joins from same user same room  | First SETNX wins · Second returns DUPLICATE_RESOLVED              |

**No retries on:**
- INELIGIBLE rejections (permanent policy)
- ROOM_FULL (deterministic, no queue)
- 410 GONE (window is closed, immutable)
- Missing idempotency key (client error, must fix client)

---

## SECTION L — ROOM-TYPE SPECIFIC RULES

### GD Voice Room (Jitsi-backed)

```
max_participants:        6 (configurable per GD batch)
join_window_duration:    Strictly enforced per GD Orchestrator
late_join_behavior:      SPECTATOR only — no speaking token
token_type:              Jitsi JWT (room-scoped, short-lived)
rejoin_grace_seconds:    120
idempotency_key_ttl:     join_window_duration + 300s
mic_rights_on_spectator: FALSE  ← enforced in Jitsi JWT claims
billing_event:           room.participant.seat.claimed (once per join)
notes:                   Per automated GD system — "Late joiners become
                         spectators only. No exceptions."
```

### Dojo Match Room (LiveKit-backed)

```
max_participants:        2 (1v1 match)
join_window_duration:    Match start window (set by Match Engine)
late_join_behavior:      REJECTED — no spectator for 1v1
token_type:              LiveKit token (room = match_id, metadata = role)
rejoin_grace_seconds:    60
idempotency_key_ttl:     match_duration + 300s
mic_rights_on_spectator: N/A (late join rejected entirely)
billing_event:           room.participant.seat.claimed
notes:                   Match requires both participants seated.
                         If one fails to join → Match Engine handles timeout.
```

### Interview Room (LiveKit-backed)

```
max_participants:        2 (candidate + interviewer) or as configured
join_window_duration:    Slot window (set by Interview Service)
late_join_behavior:      GRACE 300s — spectator mode then REJECTED
token_type:              LiveKit token
rejoin_grace_seconds:    300 (interview context — longer grace)
idempotency_key_ttl:     slot_duration + 600s
billing_event:           room.participant.seat.claimed
```

### Dojo-for-Arts Arena (LiveKit-backed)

```
max_participants:        Configurable (2–8 depending on scenario)
join_window_duration:    Arena session window
late_join_behavior:      SPECTATOR with observe-only token
token_type:              LiveKit token (metadata = role binding)
rejoin_grace_seconds:    90
idempotency_key_ttl:     arena_duration + 300s
billing_event:           room.participant.seat.claimed
```

---

## SECTION M — SECURITY BASELINE

```
✔  Idempotency keys: UUID v4, client-generated, scoped to (user, room)
✔  Media tokens: AES-256-GCM encrypted in Redis — never stored plain
✔  Token issuance: signed by Jitsi JWT secret / LiveKit API secret
     — secrets in HashiCorp Vault (never in env vars or code)
✔  Spectator tokens: carry explicit no-mic claim in JWT payload
✔  RBAC check: every join validated against Keycloak/OPA policies
✔  Tenant isolation: room_id validated against user's tenant_id
     — cross-tenant join = immediate 403
✔  Rate limiting: 5 join requests per 10s per user at Kong gateway
✔  IP logging: all join attempts log client IP for forensic audit
✔  Replay protection: idempotency key TTL prevents old-key replay
✔  Audit log: immutable append-only (PostgreSQL row-level security)
✔  PII in logs: only user_id UUID — no names, emails, or tokens
✔  Admin override: force_release_seat requires Super Admin + MFA (R40)
✔  WAF: ModSecurity on ingress — OWASP Top-10 protection active
```

---

## SECTION N — OBSERVABILITY HOOKS

### Prometheus Metrics

```
ecoskiller_join_attempts_total{room_type, outcome, device_platform}
ecoskiller_join_duplicate_resolved_total{room_type}
ecoskiller_join_seat_race_total{room_type}
ecoskiller_join_late_spectator_total{room_type}
ecoskiller_join_rejected_total{room_type, reason}
ecoskiller_seat_claim_latency_ms (histogram — p50/p95/p99)
ecoskiller_token_cache_hit_total{room_type}
ecoskiller_token_cache_miss_total{room_type}
ecoskiller_seat_release_total{room_type, reason}
ecoskiller_rejoin_grace_expired_total{room_type}
ecoskiller_join_redis_error_total
```

### Required Grafana Dashboards

```
- Join Outcome Distribution per Room Type (pie chart)
- Duplicate Resolution Rate vs. Fresh Join Rate (time series)
- Seat Claim Latency p50/p95/p99 (histogram panel)
- Room Capacity Utilization (SCARD per active room)
- Late Spectator Join Rate (counter)
- Rejoin Grace Expiry Rate (counter)
- Token Cache Hit Rate (ratio panel)
- Join Failure Rate with Alert Threshold
```

### Alert Rules

```yaml
alerts:
  - name: HighDuplicateJoinRate
    condition: rate(ecoskiller_join_duplicate_resolved_total[5m]) > 0.2
    severity: warning
    note: Indicates client retry storm — investigate Flutter reconnect logic

  - name: SeatClaimRaceSpike
    condition: rate(ecoskiller_join_seat_race_total[1m]) > 5
    severity: warning
    note: Multiple replicas competing — check Redis SETNX health

  - name: JoinRedisErrorCritical
    condition: rate(ecoskiller_join_redis_error_total[1m]) > 0.01
    severity: critical
    action: page_on_call_engineer

  - name: JoinTokenCacheMissSpike
    condition: rate(ecoskiller_token_cache_miss_total[5m]) > 50
    severity: warning
    note: Tokens expiring before use — check TTL config vs room duration
```

---

## SECTION O — AGENT DEPLOYMENT SPEC

### Kubernetes Placement

```
Namespace:  realtime
Deployment: phone-join-idempotency-agent
Replicas:   3 (min) → 8 (max, HPA on HTTP request rate)
```

### Resource Limits (Startup-Stage Budget — R25)

```yaml
resources:
  requests:
    cpu:    "100m"
    memory: "128Mi"
  limits:
    cpu:    "500m"
    memory: "512Mi"
```

### Environment Variables (Template — no hardcoded secrets)

```env
REDIS_URL=redis://redis-service:6379
POSTGRES_URL=postgresql://join_agent_user:${DB_PASS}@postgres-service:5432/ecoskiller
KAFKA_BROKERS=kafka-service:9092
VAULT_ADDR=http://vault-service:8200
VAULT_TOKEN=${VAULT_TOKEN}
JITSI_JWT_SECRET_PATH=secret/jitsi/jwt_secret
LIVEKIT_API_KEY_PATH=secret/livekit/api_key
LIVEKIT_API_SECRET_PATH=secret/livekit/api_secret
GD_JOIN_WINDOW_DEFAULT_SECONDS=600
DOJO_JOIN_WINDOW_DEFAULT_SECONDS=180
INTERVIEW_REJOIN_GRACE_SECONDS=300
DOJO_REJOIN_GRACE_SECONDS=60
GD_REJOIN_GRACE_SECONDS=120
MAX_JOIN_RATE_PER_USER_PER_10S=5
```

### Health Check Endpoints

```
GET /health   → 200 { status: "healthy", redis: "ok", kafka: "ok", vault: "ok" }
GET /ready    → 200 when all dependencies reachable
GET /metrics  → Prometheus scrape (port 9090)
```

---

## SECTION P — INTERN EXECUTION INSTRUCTIONS (R26 · R46 COMPLIANCE)

### File Structure

```
/backend/services/phone_join_idempotency_agent/
├── main.py                          # FastAPI app entrypoint
├── join_handler.py                  # Core join request handler
├── algorithms.py                    # All 5 algorithms (Sections J)
├── token_issuer.py                  # Jitsi JWT + LiveKit token generation
├── seat_manager.py                  # Redis seat claim / release
├── idempotency_store.py             # Redis idempotency key operations
├── kafka_producer.py                # Event emitter
├── db_models.py                     # SQLAlchemy: join_attempts, room_seats
├── rbac_checker.py                  # Keycloak / OPA eligibility validation
├── vault_client.py                  # HashiCorp Vault secret fetch
├── migrations/
│   └── 001_join_idempotency.sql    # DB migration: join_attempts + room_seats
├── config.py                        # Env var loader
├── health.py                        # /health + /ready endpoints
├── Dockerfile
├── requirements.txt
└── README_INTERN.md                 # Intern step-by-step guide
```

### Intern Step-by-Step Run

```bash
# Step 1 — Navigate to service folder
cd /backend/services/phone_join_idempotency_agent/

# Step 2 — Create virtual environment
python -m venv venv

# Step 3 — Activate
source venv/bin/activate        # Mac/Linux
venv\Scripts\activate           # Windows

# Step 4 — Install dependencies
pip install -r requirements.txt

# Step 5 — Copy environment template
cp .env.example .env
# Fill in Redis, PostgreSQL, Kafka, Vault values for local dev

# Step 6 — Run DB migration
psql -U ecoskiller -d ecoskiller -f migrations/001_join_idempotency.sql
# Expected: CREATE TABLE × 2, CREATE INDEX × 4

# Step 7 — Start agent
uvicorn main:app --reload --port 8081

# Expected output:
# INFO:  PHONE_JOIN_IDEMPOTENCY_AGENT started on http://0.0.0.0:8081
# INFO:  Redis connection: OK
# INFO:  Kafka connection: OK
# INFO:  PostgreSQL connection: OK
# INFO:  Vault connection: OK
```

### Intern Test Sequence

```bash
# Test 1: First join (should return 201)
curl -X POST http://localhost:8081/api/v1/rooms/test-room-001/join \
  -H "Authorization: Bearer <token>" \
  -H "Idempotency-Key: 550e8400-e29b-41d4-a716-446655440000" \
  -d '{"room_type":"gd_voice","device_platform":"android"}'
# Expected: 201 CREATED, join_outcome: JOINED, token_served_from: FRESH

# Test 2: Duplicate join (same idempotency_key — should return 200)
curl -X POST http://localhost:8081/api/v1/rooms/test-room-001/join \
  -H "Authorization: Bearer <token>" \
  -H "Idempotency-Key: 550e8400-e29b-41d4-a716-446655440000" \
  -d '{"room_type":"gd_voice","device_platform":"android"}'
# Expected: 200 OK, join_outcome: DUPLICATE_RESOLVED, token_served_from: CACHE

# Test 3: Missing idempotency key (should return 400)
curl -X POST http://localhost:8081/api/v1/rooms/test-room-001/join \
  -H "Authorization: Bearer <token>" \
  -d '{"room_type":"gd_voice","device_platform":"android"}'
# Expected: 400 BAD_REQUEST, error: MISSING_IDEMPOTENCY_KEY

# Test 4: Check Kafka event in topic
# Open another terminal: kafka-console-consumer --topic room.participant.joined
# Should see ONE event — not two (idempotency verified)
```

### Success Condition

```
✔ Agent running on port 8081
✔ GET /health returns 200
✔ Test 1 → 201 with fresh token
✔ Test 2 → 200 with same cached token (idempotency confirmed)
✔ Test 3 → 400 (validation enforced)
✔ Kafka topic room.participant.joined has exactly 1 message after Test 1+2
✔ PostgreSQL join_attempts has 2 rows (one JOINED, one DUPLICATE_RESOLVED)
✔ PostgreSQL room_seats has 1 row (not 2)
✔ Redis: seat:{room_id}:{user_id} key exists
✔ Redis: idem:{idempotency_key} HASH exists
```

Failure at any step → STOP EXECUTION → REPORT INTERN_SETUP_FAILURE

---

## SECTION Q — SERVICE WIRING MATRIX (R23 COMPLIANCE)

| Service                  | Interaction Type  | Direction          | Purpose                                          |
|--------------------------|-------------------|--------------------|--------------------------------------------------|
| Kong API Gateway         | HTTP Proxy        | Client → Agent     | Rate-limited join requests                       |
| Auth Service / Keycloak  | HTTP REST         | Agent → Auth       | Token validation + RBAC eligibility check        |
| Open Policy Agent        | HTTP REST         | Agent → OPA        | Policy-as-code join authorization                |
| Redis                    | Redis Protocol    | Agent ↔ Redis      | Idempotency store, seat lock, token cache        |
| PostgreSQL               | SQL               | Agent → DB         | Join audit log, room seat map                    |
| Kafka                    | Publish           | Agent → Kafka      | All join lifecycle events                        |
| HashiCorp Vault          | HTTP REST         | Agent → Vault      | Jitsi JWT secret + LiveKit API key/secret        |
| GD Voice Orchestrator    | Kafka Subscribe   | GD → Agent events  | Consumes room.participant.joined                 |
| Dojo Match Engine        | Kafka Subscribe   | Dojo → Agent events| Consumes room.participant.joined                 |
| Interview Service        | Kafka Subscribe   | Interview → events | Consumes room.participant.joined                 |
| Billing Service          | Kafka Subscribe   | Billing → events   | Consumes room.participant.seat.claimed           |
| Admin Console (R40)      | HTTP REST         | Admin → Agent      | Force seat release, join audit inspection        |
| Observability Stack      | Prometheus Scrape | Prometheus → Agent | Metrics collection                               |

---

## SECTION R — CONTRACT GATE DECLARATIONS (R49 COMPLIANCE)

```
CONTRACT-001: Join Flow State Machine (Section F)         → LOCKED
CONTRACT-002: Idempotency Resolution Matrix (Section F)   → LOCKED
CONTRACT-003: PostgreSQL Schema (Section G)               → LOCKED
CONTRACT-004: Redis Key Contracts (Section G)             → LOCKED
CONTRACT-005: REST API Contract (Section H)               → LOCKED
CONTRACT-006: Kafka Event Schema (Section I)              → LOCKED
CONTRACT-007: Algorithm Definitions — 5 algorithms (J)    → LOCKED
CONTRACT-008: Room-Type Specific Rules (Section L)        → LOCKED
CONTRACT-009: Security Baseline (Section M)               → LOCKED
CONTRACT-010: Observability Metrics (Section N)           → LOCKED
CONTRACT-011: Kubernetes Deployment Spec (Section O)      → LOCKED
CONTRACT-012: Service Wiring Matrix (Section Q)           → LOCKED
```

All contracts are:

```
✔ Declared
✔ Deterministic
✔ Machine-validatable via R49 Contract Validator CLI
✔ Add-only (no contract may be silently removed)
✔ Bound to ECOSKILLER Master Execution Prompt v12.0
```

---

## SECTION S — ANTIGRAVITY GUARANTEE

The **Antigravity** designation means this agent ensures join
idempotency survives every phone-side failure condition:

```
✔ User double-taps "Join" button
  → Second request returns 200 DUPLICATE_RESOLVED, same token
  → Zero duplicate seats created

✔ Flutter app backgrounded during join (OS pauses network)
  → Request retried on resume with same idempotency_key
  → Cached token served — no new seat claimed

✔ OS kills app mid-join (memory pressure)
  → App relaunches with deep-link containing room_id
  → GET /join/status checks seat — ACTIVE or DISCONNECTED
  → Rejoin grace window serves cached token

✔ Network drops after seat claim, before token delivery
  → Retry with same idempotency_key
  → Token found in Redis cache — served immediately

✔ Two simultaneous retries arrive at two replicas
  → Redis SETNX ensures only one claims seat
  → Second replica returns DUPLICATE_RESOLVED

✔ Kafka publish fails after seat claim
  → Retry queue ensures event eventually delivered
  → GD Orchestrator / Match Engine not missed

✔ Agent pod restart (Kubernetes rolling deploy)
  → Redis holds all seat state
  → New pod resumes serving — no state lost

✔ Billing event attempted twice (retry after timeout)
  → Idempotency key on seat.claimed prevents double-charge
  → Billing service deduplicates on idempotency_key

✔ Late rejoin after GD session ends
  → Idempotency key TTL expired → 410 GONE
  → No ghost seat, no stale token, no orphaned billing event
```

Joins do not duplicate — they resolve.
Seats do not multiply — they are claimed once.
Tokens do not proliferate — they are cached and served.
Billing does not double-fire — idempotency governs.

---

## SECTION T — FINAL ENFORCEMENT

```
ECOSKILLER PHONE_JOIN_IDEMPOTENCY_AGENT
Status: SEALED · LOCKED · GOVERNED · FINAL

No system claiming production-ready room join management
may proceed unless:

✔ All Section F state machine transitions are implemented
✔ All Section G schemas are migrated
✔ All Section H API contracts are active and returning correct
  idempotent responses
✔ All Section I Kafka events are wired and deduplicated
✔ All 5 Section J algorithms are implemented and tested
✔ All Section K failure handlers are in place
✔ All Section L room-type rules are enforced per room_type
✔ All Section M security controls are active
✔ All Section N observability hooks are scraping
✔ All Section O Kubernetes specs are applied
✔ All Section P intern execution steps pass
✔ Kafka topic room.participant.joined emits ONCE per unique join
✔ Kafka topic room.participant.seat.claimed emits ONCE per seat
✔ PostgreSQL room_seats enforces UNIQUE (room_id, user_id)
✔ Redis SETNX seat claim tested under concurrent load
✔ Duplicate join test returns 200 with CACHE token — verified

Failure → STOP EXECUTION
       → REPORT JOIN_IDEMPOTENCY_AGENT_INCOMPLETE
       → NO DEPLOYMENT CLAIM PERMITTED
```

---

```
PHONE_JOIN_IDEMPOTENCY_AGENT v1.0.0
ECOSKILLER Session & Lifecycle — Antigravity Tier
Status: FINAL · LOCKED · SEALED
Last Updated: 2026-03-04
Governed by: ECOSKILLER MASTER EXECUTION PROMPT v12.0
Bound to:
  R10 (Security) · R23 (Service Wiring) · R25 (Budget Sizing)
  R26 (Intern Instructions) · R39 (Core Inbuilt Tools)
  R40 (Admin Console) · R44 (Runnable Backend)
  R45 (Deployment Artifacts) · R46 (Intern Execution)
  R49 (Contract Validator) · R59 (Offline-First)
Sibling Agent: PHONE_SESSION_STATE_SYNC_AGENT (ECSK-AGENT-SESSION-SYNC-001)
GD Authority: AUTOMATED VOICE GD SYSTEM — Jitsi Rule-Driven Orchestration Doc
```
