# PHONE_SESSION_STATE_SYNC_AGENT
## Session & Lifecycle — Antigravity Layer

```
ECOSKILLER — PHONE_SESSION_STATE_SYNC_AGENT
Status: FINAL · LOCKED · GOVERNED · DETERMINISTIC
Mutation Policy: Add-only via version bump
Interpretation Authority: NONE
Execution Authority: Human declaration only
Agent Class: Session & Lifecycle / Antigravity Tier
```

---

## SECTION A — AGENT IDENTITY

```
Agent Name:         PHONE_SESSION_STATE_SYNC_AGENT
Agent Code:         ECSK-AGENT-SESSION-SYNC-001
Domain:             Session & Lifecycle
Tier:               Antigravity
System Context:     ECOSKILLER Unified SaaS Platform
Execution Mode:     Event-Driven · Stateless Agent · Stateful Infrastructure
Determinism Rule:   Identical session event → Identical state transition
Failure Mode:       STOP → LOG → EMIT DEAD_LETTER_EVENT → NO PARTIAL STATE WRITE
Version:            1.0.0 SEALED
```

---

## SECTION B — AGENT PURPOSE

The `PHONE_SESSION_STATE_SYNC_AGENT` is the **sole authority** for
synchronizing user session state across all phone-connected clients
(Flutter Mobile, PWA, and Desktop App) within the ECOSKILLER platform.

It enforces that:

- Every session lifecycle event (create, refresh, suspend, resume,
  expire, terminate) is captured deterministically.
- Session state is consistent across simultaneous phone sessions,
  browser tabs, and device switches.
- No phantom session, duplicate session, or orphaned token ever persists
  in the system.
- All session transitions are immutably audit-logged.
- The antigravity tier ensures sessions survive network disruptions,
  background kills, and OS memory pressure without data loss.

**Human judgment is removed.**
**AI inference is removed.**
**Only rules, state machines, and enforcement remain.**

---

## SECTION C — NON-NEGOTIABLE OPERATING RULES

```
RULE-1:  Agent never writes session state directly to primary DB.
         All writes flow through Redis state machine ONLY.

RULE-2:  Every state transition must emit a corresponding Kafka event.
         No silent transitions permitted.

RULE-3:  Agent is stateless. All state lives in Redis + PostgreSQL.
         No in-memory session caching inside agent process.

RULE-4:  Conflict resolution is LAST-WRITE-WINS by server_timestamp.
         Client timestamps are advisory only, never authoritative.

RULE-5:  Session token rotation is mandatory on every resume from
         background or network reconnect.

RULE-6:  Multi-device session limit is enforced per tenant policy.
         Excess sessions are terminated oldest-first.

RULE-7:  Expired sessions are never reactivated. A new session must be
         created via Auth Service.

RULE-8:  All PII in session payloads must be encrypted at rest
         (AES-256) and in transit (TLS 1.3+).

RULE-9:  Agent must never expose raw session tokens in logs.
         Only session_id (UUID) is permitted in log output.

RULE-10: Agent failure triggers automatic fallback to offline-safe
         session cache per R59 (Offline-First Law).
```

Violation of any rule → `STOP AGENT` → `EMIT SESSION_SYNC_FAILURE EVENT`

---

## SECTION D — SYSTEM SHAPE (NON-NEGOTIABLE)

### Architecture Placement

```
Flutter App / PWA / Desktop Client
        │
        ▼
  WebSocket Channel  ←→  Kong API Gateway
        │
        ▼
  PHONE_SESSION_STATE_SYNC_AGENT   ← THIS AGENT
        │
        ├──▶ Redis (State Machine · Timers · Locks · OTPs)
        ├──▶ PostgreSQL (Session Metadata · Audit Logs)
        ├──▶ Kafka (Event Bus → user.session.* events)
        └──▶ Auth Service (Token Validation · Rotation)
```

### Traffic Class

```
Primary:  WebSocket commands (session ping, resume, terminate, sync)
Secondary: HTTP REST (session create, token refresh)
Async:    Kafka events (session.created, session.expired, etc.)
```

### Forbidden Interactions

```
✗  Agent may NOT call Job Service, Skill Service, or Billing Service.
✗  Agent may NOT read from ClickHouse or OpenSearch.
✗  Agent may NOT issue JWT tokens (Auth Service owns this).
✗  Agent may NOT store media or files (MinIO scope excluded).
```

---

## SECTION E — SESSION LIFECYCLE STATE MACHINE

### States

```
INITIALIZING → ACTIVE → IDLE → SUSPENDED → RESUMED → EXPIRED → TERMINATED
                  │                              │
                  └──────── FORCE_TERMINATED ────┘
```

### Transition Table

| From          | Event                     | To               | Action                                   |
|---------------|---------------------------|------------------|------------------------------------------|
| —             | session.create            | INITIALIZING     | Allocate session_id, write Redis TTL     |
| INITIALIZING  | auth.token.validated      | ACTIVE           | Emit session.created, set heartbeat      |
| ACTIVE        | heartbeat.missed (×3)     | IDLE             | Emit session.idle, start idle timer      |
| IDLE          | client.ping               | ACTIVE           | Reset heartbeat, emit session.resumed    |
| IDLE          | idle.timeout              | SUSPENDED        | Emit session.suspended, retain Redis key |
| SUSPENDED     | client.reconnect          | RESUMED          | Rotate token, emit session.resumed       |
| RESUMED       | auth.token.validated      | ACTIVE           | Full session restore, emit session.active|
| ACTIVE        | session.expire_ttl        | EXPIRED          | Delete Redis key, emit session.expired   |
| ACTIVE        | user.logout               | TERMINATED       | Wipe Redis + DB record, emit terminated  |
| ANY           | admin.force_terminate     | FORCE_TERMINATED | Immediate wipe, emit force_terminated    |
| EXPIRED       | ANY                       | REJECTED         | Return 401. New login required.          |

### Redis State Machine Keys

```
Key Pattern:   session:{session_id}:state
TTL:           Configurable per tenant (default: 7 days inactive → EXPIRED)
Lock Key:      session:{session_id}:lock  (TTL: 5s, prevent race conditions)
Heartbeat Key: session:{session_id}:heartbeat  (TTL: 30s rolling)
Device Map:    user:{user_id}:sessions  (sorted set, score = last_active_ts)
```

---

## SECTION F — DATA LAYER CONTRACTS

### PostgreSQL Schema

```sql
-- Session Record (persistent audit source)
CREATE TABLE phone_sessions (
    session_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id          UUID NOT NULL REFERENCES users(id),
    tenant_id        UUID NOT NULL REFERENCES tenants(id),
    device_id        TEXT NOT NULL,
    device_platform  TEXT CHECK (device_platform IN ('android','ios','pwa','desktop')),
    ip_address       INET,
    user_agent       TEXT,
    state            TEXT NOT NULL DEFAULT 'INITIALIZING',
    token_hash       TEXT NOT NULL,        -- SHA-256 of JWT, never raw token
    created_at       TIMESTAMPTZ DEFAULT now(),
    last_active_at   TIMESTAMPTZ DEFAULT now(),
    expires_at       TIMESTAMPTZ NOT NULL,
    terminated_at    TIMESTAMPTZ,
    termination_reason TEXT
);

-- Immutable Session Audit Log
CREATE TABLE session_audit_log (
    log_id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id       UUID NOT NULL REFERENCES phone_sessions(session_id),
    user_id          UUID NOT NULL,
    event_type       TEXT NOT NULL,
    from_state       TEXT,
    to_state         TEXT,
    actor            TEXT NOT NULL,   -- 'user' | 'system' | 'admin'
    metadata         JSONB,
    occurred_at      TIMESTAMPTZ DEFAULT now()
);

-- Indexes
CREATE INDEX idx_phone_sessions_user_id ON phone_sessions(user_id);
CREATE INDEX idx_phone_sessions_state   ON phone_sessions(state);
CREATE INDEX idx_session_audit_session  ON session_audit_log(session_id);
```

### Redis Key Contracts

```
Type:    HASH
Key:     session:{session_id}:state
Fields:
  - state            TEXT
  - user_id          UUID
  - tenant_id        UUID
  - device_id        TEXT
  - device_platform  TEXT
  - last_active_ts   EPOCH_MS
  - token_hash       TEXT
  - sync_vector      INT        (monotonic counter, prevents replay)

Type:    STRING
Key:     session:{session_id}:heartbeat
Value:   "1"
TTL:     30 seconds (rolling refresh on each client ping)

Type:    ZSET
Key:     user:{user_id}:sessions
Member:  session_id
Score:   last_active_ts (EPOCH_MS)
```

---

## SECTION G — API CONTRACTS

### REST Endpoints

```yaml
# SESSION CREATE
POST /api/v1/sessions
Authorization: Bearer <access_token>
Body:
  device_id: string (required)
  device_platform: android | ios | pwa | desktop (required)
  ip_address: string (optional, server-inferred preferred)
Response 201:
  session_id: UUID
  expires_at: ISO8601
  sync_vector: int

# SESSION HEARTBEAT (keep-alive)
POST /api/v1/sessions/{session_id}/ping
Authorization: Bearer <access_token>
Response 200:
  state: string
  sync_vector: int
  server_ts: ISO8601

# SESSION TERMINATE
DELETE /api/v1/sessions/{session_id}
Authorization: Bearer <access_token>
Response 204: (no body)

# MULTI-DEVICE SESSION LIST
GET /api/v1/sessions
Authorization: Bearer <access_token>
Response 200:
  sessions: [ { session_id, device_platform, last_active_at, state } ]

# FORCE-TERMINATE OTHER SESSIONS
DELETE /api/v1/sessions/others
Authorization: Bearer <access_token>
Response 200:
  terminated_count: int
```

### WebSocket Commands (Backend → Client)

```json
// Mute/Unmute command pattern (matches GD orchestrator model from Jitsi stack)
{ "cmd": "session.state_update", "session_id": "...", "new_state": "ACTIVE",  "sync_vector": 42 }
{ "cmd": "session.token_rotate", "session_id": "...", "new_token": "...",     "sync_vector": 43 }
{ "cmd": "session.force_terminate", "session_id": "...", "reason": "...",     "sync_vector": 44 }
{ "cmd": "session.multi_device_alert", "active_sessions": 3,                 "sync_vector": 45 }
```

---

## SECTION H — KAFKA EVENT SCHEMA (ASYNCAPI)

```yaml
asyncapi: 2.6.0
info:
  title: PHONE_SESSION_STATE_SYNC_AGENT Events
  version: 1.0.0

channels:

  user.session.created:
    publish:
      message:
        payload:
          session_id: { type: string, format: uuid }
          user_id:    { type: string, format: uuid }
          tenant_id:  { type: string, format: uuid }
          device_platform: { type: string }
          occurred_at: { type: string, format: date-time }

  user.session.active:
    publish:
      message:
        payload:
          session_id:   { type: string }
          sync_vector:  { type: integer }
          occurred_at:  { type: string }

  user.session.idle:
    publish:
      message:
        payload:
          session_id:   { type: string }
          idle_since:   { type: string }

  user.session.suspended:
    publish:
      message:
        payload:
          session_id:   { type: string }
          suspend_reason: { type: string }

  user.session.resumed:
    publish:
      message:
        payload:
          session_id:    { type: string }
          resumed_from:  { type: string }  # SUSPENDED | IDLE
          token_rotated: { type: boolean }

  user.session.expired:
    publish:
      message:
        payload:
          session_id:  { type: string }
          user_id:     { type: string }
          expired_at:  { type: string }

  user.session.terminated:
    publish:
      message:
        payload:
          session_id:  { type: string }
          user_id:     { type: string }
          actor:       { type: string }  # user | admin | system
          reason:      { type: string }

  user.session.force_terminated:
    publish:
      message:
        payload:
          session_id:      { type: string }
          admin_actor_id:  { type: string }
          reason:          { type: string }
          occurred_at:     { type: string }

  user.session.sync_failure:
    publish:
      message:
        payload:
          session_id:  { type: string }
          error_code:  { type: string }
          error_detail:{ type: string }
          occurred_at: { type: string }
```

---

## SECTION I — CORE ALGORITHMS

### Algorithm 1 — Sync Vector Conflict Resolution

```
FUNCTION resolve_sync_conflict(incoming_vector, stored_vector):
  IF incoming_vector > stored_vector:
    ACCEPT incoming event
    UPDATE stored_vector = incoming_vector
    RETURN ACCEPTED
  ELSE IF incoming_vector == stored_vector:
    RETURN DUPLICATE (idempotent ignore)
  ELSE:
    RETURN STALE (reject, emit session.sync_stale_rejected)
```

### Algorithm 2 — Multi-Device Limit Enforcer

```
FUNCTION enforce_device_limit(user_id, tenant_id):
  max_sessions = tenant_policy.max_concurrent_sessions(tenant_id)
  current = ZCARD user:{user_id}:sessions

  IF current >= max_sessions:
    oldest_session = ZRANGE user:{user_id}:sessions 0 0  // lowest score
    EXECUTE force_terminate(oldest_session, reason="device_limit_exceeded")
    EMIT user.session.force_terminated

  RETURN ALLOWED
```

### Algorithm 3 — Offline Antigravity Cache Write-Back

```
FUNCTION antigravity_writeback(device_queue):
  // Called on reconnect from SUSPENDED state (R59 — Offline-First Law)

  SORT device_queue BY event.client_ts ASC
  FOR each event IN device_queue:
    TRY:
      resolved = resolve_sync_conflict(event.sync_vector, redis.sync_vector)
      IF resolved == ACCEPTED:
        apply_state_transition(event)
        EMIT corresponding Kafka event
      IF resolved == STALE:
        LOG stale_event_discarded
    CATCH conflict:
      APPLY last_write_wins by server_ts
      EMIT user.session.sync_conflict_resolved

  RETURN writeback_complete
```

### Algorithm 4 — Token Rotation on Resume

```
FUNCTION rotate_token_on_resume(session_id):
  ACQUIRE session:{session_id}:lock (TTL=5s)

  old_token_hash = HGET session:{session_id}:state token_hash
  new_token = AuthService.issue_short_lived_token(session_id)
  new_token_hash = SHA256(new_token)

  HSET session:{session_id}:state token_hash new_token_hash
  UPDATE phone_sessions SET token_hash = new_token_hash WHERE session_id = session_id

  EMIT user.session.resumed { token_rotated: true }
  RELEASE session:{session_id}:lock

  RETURN new_token  // returned to client over encrypted WebSocket only
```

---

## SECTION J — FAILURE HANDLING

| Failure Condition                   | Agent Action                                             |
|-------------------------------------|----------------------------------------------------------|
| Redis unavailable                   | Fallback read from PostgreSQL · Emit DEGRADED_MODE alert |
| Kafka publish failure               | Retry ×3 with backoff · Dead-letter queue on 3rd fail    |
| Auth Service timeout (token rotate) | Retain old token for 60s grace · Emit token_rotate_warn  |
| WebSocket disconnect mid-transition | Persist last known state in Redis · Resume on reconnect  |
| Sync vector rollback attack         | Reject · Log security event · Emit session.attack_detect |
| DB write failure                    | Redis as source-of-truth temporarily · Alert Ops console |
| Device limit exceeded               | Terminate oldest session · Notify user via push (R12)    |
| Agent process crash                 | Kubernetes restarts agent · Redis preserves all state    |

No retries on:
- EXPIRED sessions (401 forced)
- FORCE_TERMINATED sessions (permanent)
- Stale event replays (discarded silently with log)

---

## SECTION K — SECURITY BASELINE

```
✔  Session tokens: never stored raw (SHA-256 hash only in DB/Redis)
✔  WebSocket channel: TLS 1.3 + WSS only
✔  All session payloads: AES-256-GCM encrypted at rest
✔  IP mismatch detection: flag and emit session.ip_change_detected
✔  User-Agent mismatch detection: flag on suspicious device pivot
✔  Redis keys: ACL-restricted to session sync agent namespace only
✔  Admin force_terminate: requires Super Admin MFA (R40 enforcement)
✔  Rate limit: max 10 session.create requests per user per hour
✔  Replay protection: sync_vector monotonic counter (Section I, Algo 1)
✔  Audit log: immutable, append-only (PostgreSQL row-level security)
✔  PII fields: never written to Loki/ELK logs (only session_id UUIDs)
```

---

## SECTION L — OBSERVABILITY HOOKS

### Prometheus Metrics

```
ecoskiller_session_active_total{tenant_id, device_platform}
ecoskiller_session_created_total{tenant_id}
ecoskiller_session_expired_total{tenant_id}
ecoskiller_session_force_terminated_total{reason}
ecoskiller_session_sync_conflict_total
ecoskiller_session_writeback_latency_ms (histogram)
ecoskiller_session_heartbeat_miss_total
ecoskiller_session_token_rotate_total
ecoskiller_session_redis_error_total
```

### Grafana Dashboards (Required)

```
- Session State Distribution (pie: ACTIVE / IDLE / SUSPENDED)
- Session Creation Rate (time series)
- Multi-Device Limit Violations (counter)
- Antigravity Write-Back Latency (p50 / p95 / p99)
- Token Rotation Rate (time series)
- Sync Conflict Rate (counter with alert threshold)
```

### Alert Rules

```yaml
alerts:
  - name: HighSyncConflictRate
    condition: rate(ecoskiller_session_sync_conflict_total[5m]) > 0.05
    severity: warning

  - name: SessionRedisErrorSpike
    condition: rate(ecoskiller_session_redis_error_total[1m]) > 0.01
    severity: critical
    action: page_on_call_engineer

  - name: MassSessionTermination
    condition: rate(ecoskiller_session_force_terminated_total[1m]) > 50
    severity: critical
    action: escalate_to_admin_console
```

---

## SECTION M — AGENT DEPLOYMENT SPEC

### Kubernetes Namespace

```
Namespace: realtime
Deployment: phone-session-sync-agent
Replicas: 3 (min) → 10 (max, HPA on WebSocket connection count)
```

### Resource Limits (Startup-Stage Budget — R25)

```yaml
resources:
  requests:
    cpu: "100m"
    memory: "128Mi"
  limits:
    cpu: "500m"
    memory: "512Mi"
```

### Environment Variables (Template — no hardcoded secrets)

```env
REDIS_URL=redis://redis-service:6379
POSTGRES_URL=postgresql://session_agent_user:${DB_PASS}@postgres-service:5432/ecoskiller
KAFKA_BROKERS=kafka-service:9092
AUTH_SERVICE_URL=http://auth-service:8000
WEBSOCKET_PORT=8080
SESSION_DEFAULT_TTL_DAYS=7
MAX_SESSIONS_DEFAULT=5
TOKEN_GRACE_PERIOD_SECONDS=60
HEARTBEAT_INTERVAL_SECONDS=30
HEARTBEAT_MISS_THRESHOLD=3
```

### Health Check Endpoints

```
GET /health        → 200 OK { status: "healthy", redis: "ok", kafka: "ok" }
GET /ready         → 200 OK when all dependencies reachable
GET /metrics       → Prometheus scrape endpoint (port 9090)
```

---

## SECTION N — INTERN EXECUTION INSTRUCTIONS (R26 · R46 COMPLIANCE)

### File Structure

```
/backend/services/phone_session_sync_agent/
├── main.py                        # Agent entrypoint (FastAPI + WebSocket)
├── state_machine.py               # Redis state machine logic
├── algorithms.py                  # Sync vector, multi-device, writeback
├── kafka_producer.py              # Kafka event emitter
├── db_models.py                   # SQLAlchemy models (phone_sessions, audit_log)
├── migrations/
│   └── 001_phone_sessions.sql    # DB migration script
├── config.py                      # Loads env vars
├── health.py                      # /health and /ready endpoints
├── Dockerfile
├── requirements.txt
└── README_INTERN.md               # Step-by-step intern guide
```

### Intern Step-by-Step Run

```bash
# Step 1 — Navigate to service folder
cd /backend/services/phone_session_sync_agent/

# Step 2 — Create virtual environment
python -m venv venv

# Step 3 — Activate
source venv/bin/activate        # Mac/Linux
venv\Scripts\activate           # Windows

# Step 4 — Install dependencies
pip install -r requirements.txt

# Step 5 — Copy environment template
cp .env.example .env
# Edit .env and fill in local values

# Step 6 — Run DB migration
psql -U ecoskiller -d ecoskiller -f migrations/001_phone_sessions.sql

# Step 7 — Start agent
uvicorn main:app --reload --port 8080

# Expected output:
# INFO:     PHONE_SESSION_STATE_SYNC_AGENT started on ws://0.0.0.0:8080
# INFO:     Redis connection: OK
# INFO:     Kafka connection: OK
# INFO:     PostgreSQL connection: OK
```

### Success Condition

```
✔ Agent running on port 8080
✔ GET /health returns 200
✔ WebSocket accepts connection at ws://localhost:8080/ws/session
✔ Redis HSET session:test:state visible via redis-cli
✔ Kafka topic user.session.created receives test event
✔ PostgreSQL phone_sessions table populated after create call
```

Failure at any step → STOP EXECUTION → REPORT INTERN_SETUP_FAILURE

---

## SECTION O — SERVICE WIRING MATRIX (R23 COMPLIANCE)

| Service               | Interaction Type | Direction       | Purpose                              |
|-----------------------|-----------------|-----------------|--------------------------------------|
| Auth Service          | HTTP REST        | Agent → Auth    | Token validation & rotation          |
| Redis                 | Redis Protocol   | Agent ↔ Redis   | State machine, heartbeat, locks      |
| PostgreSQL            | SQL              | Agent → DB      | Session records, audit logs          |
| Kafka                 | Publish          | Agent → Kafka   | All session lifecycle events         |
| Kong API Gateway      | WebSocket Proxy  | Client → Agent  | Incoming session commands            |
| Notification Service  | Kafka Subscribe  | Notif → Agent   | Push alerts on force-terminate       |
| Admin Console (R40)   | HTTP REST        | Admin → Agent   | Force-terminate, session inspection  |
| Observability Stack   | Prometheus       | Prometheus → Agent | Metrics scrape                    |

---

## SECTION P — CONTRACT GATE DECLARATIONS (R49 COMPLIANCE)

```
CONTRACT-001: Session State Machine (Section E) → LOCKED
CONTRACT-002: PostgreSQL Schema (Section F)     → LOCKED
CONTRACT-003: Redis Key Contracts (Section F)   → LOCKED
CONTRACT-004: REST API Contract (Section G)     → LOCKED
CONTRACT-005: WebSocket Command Schema (Section G) → LOCKED
CONTRACT-006: Kafka Event Schema (Section H)    → LOCKED
CONTRACT-007: Algorithm Definitions (Section I) → LOCKED
CONTRACT-008: Security Baseline (Section K)     → LOCKED
CONTRACT-009: Observability Metrics (Section L) → LOCKED
CONTRACT-010: Kubernetes Deployment Spec (Section M) → LOCKED
```

All contracts are:

```
✔ Declared
✔ Deterministic
✔ Machine-validatable via R49 Contract Validator CLI
✔ Add-only (no contract may be silently removed)
```

---

## SECTION Q — ANTIGRAVITY GUARANTEE

The **Antigravity** designation means this agent is immune to the
following platform failure conditions that would otherwise terminate
a session:

```
✔ Phone screen locked / backgrounded           → Heartbeat suspended, session SUSPENDED
✔ OS killed app due to memory pressure         → Redis retains state, resume on relaunch
✔ Network disconnected (flight mode, tunnel)   → Offline-safe cache per R59 writeback
✔ App crash mid-session                        → Reconnect flow restores to last ACTIVE state
✔ Multiple tabs open (PWA)                     → Sync vector arbitrates authoritative state
✔ Device switch mid-workflow                   → Token rotation, multi-device sync preserved
✔ Server rolling deploy / pod restart          → Redis-persisted state survives pod death
✔ Kafka broker transient failure               → Retry ×3 + dead-letter queue
```

Sessions do not fall — they suspend and resume.
Data does not drift — the sync vector governs.
Tokens do not leak — rotation enforces freshness.

---

## SECTION R — FINAL ENFORCEMENT

```
ECOSKILLER PHONE_SESSION_STATE_SYNC_AGENT
Status: SEALED · LOCKED · GOVERNED · FINAL

No system claiming production-ready Session & Lifecycle
management may proceed unless:

✔ All Section E state machine transitions are implemented
✔ All Section F schemas are migrated
✔ All Section G API contracts are active
✔ All Section H Kafka events are wired
✔ All Section I algorithms are implemented and tested
✔ All Section J failure handlers are in place
✔ All Section K security controls are active
✔ All Section L observability hooks are scraping
✔ All Section M Kubernetes specs are applied
✔ All Section N intern execution steps pass

Failure → STOP EXECUTION
       → REPORT SESSION_SYNC_AGENT_INCOMPLETE
       → NO DEPLOYMENT CLAIM PERMITTED
```

---

```
PHONE_SESSION_STATE_SYNC_AGENT v1.0.0
ECOSKILLER Session & Lifecycle — Antigravity Tier
Status: FINAL · LOCKED · SEALED
Last Updated: 2026-03-02
Governed by: ECOSKILLER MASTER EXECUTION PROMPT v12.0
Bound to: R10 · R18 · R19 · R39 · R40 · R44 · R45 · R59 · R60
```
