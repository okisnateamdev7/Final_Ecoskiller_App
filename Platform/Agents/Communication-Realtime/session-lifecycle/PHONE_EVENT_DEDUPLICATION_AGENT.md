# PHONE_EVENT_DEDUPLICATION_AGENT
## ECOSKILLER — Session & Lifecycle Domain
### Antigravity Layer | Agent Specification
**Status: FINAL · LOCKED · GOVERNED · DETERMINISTIC**
**Mutation Policy: Add-only via version bump**
**Interpretation Authority: NONE**
**Execution Authority: Human declaration only**
**Prompt Class: Sealed Production Agent Prompt**
**Sealed Version: v1.0**
**Bound To: ECOSKILLER MASTER EXECUTION PROMPT v12.0**

---

## ⚠️ SEAL DECLARATION

This document is a **sealed, locked, non-negotiable agent prompt**.

No clause within this document may be:
- Reinterpreted
- Paraphrased
- Partially applied
- Overridden by downstream agents or services
- Weakened by AI inference

Any agent receiving this prompt **MUST** execute exactly as declared.

Deviation → **STOP EXECUTION** → **REPORT AGENT VIOLATION**

---

---

# SECTION I — AGENT IDENTITY

**Agent Name:** `PHONE_EVENT_DEDUPLICATION_AGENT`
**Agent Class:** Session & Lifecycle → Antigravity Layer
**Agent Role:** Deterministic deduplication of phone-originated lifecycle events within the ECOSKILLER platform
**Trigger Domain:** Auth, Session, OTP, Device, Notification, GD, Dojo, Interview pipelines
**Execution Model:** Event-driven, stateless processing, Redis-backed deduplication window
**Failure Mode:** STOP → EMIT DEDUP_FAILURE_EVENT → NO PARTIAL WRITE

---

---

# SECTION II — PROBLEM STATEMENT (LOCKED)

## Why This Agent Exists

Mobile phone networks, Android/iOS OS-level event systems, and WebRTC reconnect loops produce **duplicate lifecycle events** that, without interception, cause:

| Duplicate Event | Consequence Without Agent |
|---|---|
| Double OTP request | SMS cost spike, brute-force surface |
| Double session creation | Ghost sessions in Redis, JWT collision |
| Double GD join | Participant counted twice in state machine |
| Double Dojo match join | Score split corruption |
| Double interview slot lock | Two users booking same slot |
| Double notification send | User receives same notification N times |
| Double device registration | Push token duplication |
| Double belt award | Certification integrity breach |

**This agent eliminates all of the above.**

---

---

# SECTION III — CORE DESIGN PHILOSOPHY (NON-NEGOTIABLE)

```
Replace tolerance with protocol.
Replace retry hope with idempotency enforcement.
Replace soft deduplication with deterministic key locking.
```

**The agent operates only on:**
- Event fingerprint
- Deduplication window
- Idempotency key registry

**The agent explicitly avoids:**
- Business logic
- Scoring decisions
- Session content inspection
- User profiling

**The agent does not attempt to "understand" events.**
**It enforces uniqueness under deterministic rules.**

---

---

# SECTION IV — SCOPE (BOUNDED & LOCKED)

## Covered Event Categories

```
PHONE_SESSION_EVENTS
  phone.session.created
  phone.session.resumed
  phone.session.terminated

PHONE_AUTH_EVENTS
  phone.otp.requested
  phone.otp.verified
  phone.login.initiated
  phone.login.completed
  phone.logout.triggered

PHONE_DEVICE_EVENTS
  phone.device.registered
  phone.device.token.refreshed
  phone.device.offline.detected
  phone.device.online.restored

PHONE_GD_EVENTS
  phone.gd.join.requested
  phone.gd.join.confirmed
  phone.gd.disconnect.detected
  phone.gd.rejoin.attempted

PHONE_DOJO_EVENTS
  phone.dojo.match.join.requested
  phone.dojo.match.score.submitted

PHONE_INTERVIEW_EVENTS
  phone.interview.slot.lock.requested
  phone.interview.join.requested

PHONE_NOTIFICATION_EVENTS
  phone.notification.push.triggered
  phone.notification.sms.triggered
  phone.notification.in_app.triggered

PHONE_BELT_EVENTS
  phone.belt.award.triggered
```

**Out of scope (explicitly excluded):**
- Web browser events (handled by WEB_EVENT_DEDUPLICATION_AGENT)
- Server-to-server internal events
- Analytics ingestion streams
- Payment webhooks (handled by PAYMENT_WEBHOOK_DEDUP_AGENT)

---

---

# SECTION V — SYSTEM ARCHITECTURE (LOCKED)

## Component Map

```
┌─────────────────────────────────────────────────────┐
│                PHONE CLIENT (Flutter)               │
│  Emits lifecycle events via API + WebSocket         │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│               API GATEWAY (Kong)                    │
│  Passes X-Idempotency-Key header enforcement        │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│     PHONE_EVENT_DEDUPLICATION_AGENT                 │
│  ┌───────────────┐  ┌──────────────────────────┐   │
│  │ Fingerprint   │  │ Dedup Window Registry    │   │
│  │ Generator     │  │ (Redis SETNX TTL-based)  │   │
│  └───────┬───────┘  └──────────┬───────────────┘   │
│          └──────────┬──────────┘                   │
│                     ▼                               │
│          ┌──────────────────────┐                   │
│          │  DEDUP DECISION      │                   │
│          │  UNIQUE → PASS       │                   │
│          │  DUPLICATE → REJECT  │                   │
│          └──────────────────────┘                   │
└────────────────────┬────────────────────────────────┘
                     │
          ┌──────────┴──────────┐
          ▼                     ▼
   DOWNSTREAM SERVICE     DEDUP_AUDIT_LOG
   (processes event)      (immutable record)
```

---

---

# SECTION VI — DEDUPLICATION KEY SCHEMA (LOCKED)

## Master Key Formula

```
dedup_key = SHA256(
  agent_name      +
  event_type      +
  user_id         +
  device_id       +
  idempotency_key +
  floor(unix_timestamp / window_seconds)
)
```

**Key prefix namespace:** `dedup:phone:`

**Full Redis key format:**
```
dedup:phone:{event_type}:{dedup_key_hash}
```

**Example:**
```
dedup:phone:phone.otp.requested:a3f92c1d...
dedup:phone:phone.gd.join.requested:b7e01f44...
```

---

## Per-Event TTL Window Table (LOCKED)

| Event Type | Dedup Window (TTL) | Reason |
|---|---|---|
| `phone.otp.requested` | 60 seconds | OTP rate limiting |
| `phone.otp.verified` | 30 seconds | Replay prevention |
| `phone.login.initiated` | 10 seconds | Network retry guard |
| `phone.login.completed` | 10 seconds | Double-session guard |
| `phone.logout.triggered` | 5 seconds | Idempotent logout |
| `phone.session.created` | 15 seconds | WebSocket reconnect loop |
| `phone.session.resumed` | 10 seconds | App foreground restoration |
| `phone.session.terminated` | 5 seconds | Background kill duplicate |
| `phone.device.registered` | 300 seconds | Boot/app reinstall |
| `phone.device.token.refreshed` | 60 seconds | FCM token churn |
| `phone.device.offline.detected` | 30 seconds | Network flap |
| `phone.device.online.restored` | 10 seconds | Network flap |
| `phone.gd.join.requested` | 20 seconds | WebRTC join retry |
| `phone.gd.join.confirmed` | 30 seconds | Session lock |
| `phone.gd.disconnect.detected` | 15 seconds | Reconnect trigger |
| `phone.gd.rejoin.attempted` | 30 seconds | Rejoin spam guard |
| `phone.dojo.match.join.requested` | 20 seconds | Match entry guard |
| `phone.dojo.match.score.submitted` | 120 seconds | Score double-write |
| `phone.interview.slot.lock.requested` | 30 seconds | Slot collision guard |
| `phone.interview.join.requested` | 20 seconds | Join retry guard |
| `phone.notification.push.triggered` | 60 seconds | Duplicate push |
| `phone.notification.sms.triggered` | 120 seconds | SMS cost guard |
| `phone.notification.in_app.triggered` | 30 seconds | UI noise guard |
| `phone.belt.award.triggered` | 3600 seconds | Certification integrity |

**TTL values are NON-NEGOTIABLE.**
Modification requires version bump + governance review.

---

---

# SECTION VII — AGENT EXECUTION FLOW (DETERMINISTIC)

```
INPUT: PhoneEvent {
  event_type,
  user_id,
  device_id,
  idempotency_key,
  timestamp,
  payload
}

STEP 1: VALIDATE INPUT
  IF any required field null OR empty
    → EMIT phone.event.invalid
    → STOP

STEP 2: GENERATE FINGERPRINT
  dedup_key = SHA256(
    "PHONE_EVENT_DEDUPLICATION_AGENT" +
    event_type +
    user_id +
    device_id +
    idempotency_key +
    floor(timestamp / window_ttl[event_type])
  )
  redis_key = "dedup:phone:" + event_type + ":" + dedup_key

STEP 3: REDIS SETNX CHECK
  result = REDIS.SET(redis_key, "1", NX, EX window_ttl[event_type])

  IF result == NULL (key already exists)
    → IS DUPLICATE
    → EMIT phone.event.deduplicated {
        event_type,
        user_id,
        device_id,
        dedup_key,
        timestamp
      }
    → WRITE to DEDUP_AUDIT_LOG
    → RETURN HTTP 200 (ACK without processing)
    → STOP

  IF result == "OK" (key did not exist, now set)
    → IS UNIQUE
    → PASS event to downstream service
    → WRITE to DEDUP_AUDIT_LOG as PASSED
    → RETURN downstream service response

STEP 4: FAILURE HANDLING
  IF Redis unreachable
    → EMIT phone.event.dedup.redis_failure
    → FALLBACK: PASS event (fail-open mode)
    → LOG critical alert
    → DO NOT block user action
```

---

---

# SECTION VIII — IDEMPOTENCY KEY PROTOCOL (CLIENT CONTRACT)

## Flutter Client MUST Implement

Every phone-originated event API call **MUST** include the header:

```
X-Idempotency-Key: {uuid_v4}
```

**Generation Rule (Flutter):**
```dart
// Generated once per user-initiated action
// Persisted in local action queue
// NOT regenerated on network retry
final idempotencyKey = const Uuid().v4();
```

**Key Lifecycle:**
- Generated before network call
- Persisted in `PendingActionQueue` (see R59 Offline-First Law)
- Re-sent with identical value on network retry
- Cleared after server ACK received
- **Never regenerated during retry window**

**Absence of X-Idempotency-Key header:**
- API Gateway (Kong) **MUST** reject with `HTTP 400 MISSING_IDEMPOTENCY_KEY`
- Event **MUST NOT** reach downstream services without it

---

---

# SECTION IX — DATABASE SCHEMA (LOCKED)

## Table: `phone_event_dedup_audit_log`

```sql
CREATE TABLE phone_event_dedup_audit_log (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  event_type        TEXT NOT NULL,
  user_id           UUID NOT NULL REFERENCES users(id),
  device_id         TEXT NOT NULL,
  idempotency_key   TEXT NOT NULL,
  dedup_key_hash    TEXT NOT NULL,
  decision          TEXT NOT NULL CHECK (decision IN ('PASSED', 'DEDUPLICATED', 'INVALID', 'REDIS_FAILURE')),
  window_ttl_ms     INTEGER NOT NULL,
  raw_payload       JSONB,
  processed_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  tenant_id         UUID NOT NULL REFERENCES tenants(id)
);

-- Indexes
CREATE INDEX idx_dedup_audit_user      ON phone_event_dedup_audit_log(user_id, processed_at);
CREATE INDEX idx_dedup_audit_event     ON phone_event_dedup_audit_log(event_type, processed_at);
CREATE INDEX idx_dedup_audit_decision  ON phone_event_dedup_audit_log(decision, processed_at);
CREATE INDEX idx_dedup_audit_key_hash  ON phone_event_dedup_audit_log(dedup_key_hash);
```

**Immutability rule:**
- No UPDATE permitted on this table
- No DELETE permitted on this table
- Governed under R60 (Long-Term Archival Law)

---

## Table: `phone_event_dedup_stats` (ClickHouse — Analytics)

```sql
-- ClickHouse schema
CREATE TABLE phone_event_dedup_stats (
  event_date        Date,
  event_hour        UInt8,
  event_type        String,
  tenant_id         UUID,
  total_received    UInt64,
  total_passed      UInt64,
  total_deduplicated UInt64,
  total_invalid     UInt64,
  redis_failure_count UInt64
) ENGINE = SummingMergeTree()
PARTITION BY event_date
ORDER BY (event_date, event_hour, event_type, tenant_id);
```

---

---

# SECTION X — REDIS KEY ARCHITECTURE (LOCKED)

## Namespace Isolation

```
dedup:phone:{event_type}:{dedup_key_hash}
```

**Redis cluster key rules:**
- All dedup keys use `{dedup_key_hash}` as hash slot tag for clustering
- TTL is **always** set atomically with SETNX via SET NX EX
- No manual key deletion permitted
- Keys expire automatically — no cron cleanup required

## Redis Failure Mode

```
IF REDIS.ping() FAILS
  → Log CRITICAL: "DEDUP_REDIS_UNREACHABLE"
  → Emit: phone.event.dedup.redis_failure
  → MODE: FAIL-OPEN (pass event through)
  → Alert: Prometheus alert fires within 30 seconds
  → Human intervention required
```

**Fail-open is intentional** to preserve user experience.
Fail-open window must be monitored and cannot exceed 5 minutes.

---

---

# SECTION XI — EVENT BUS CONTRACTS (LOCKED)

## Events Emitted by This Agent

### `phone.event.deduplicated`
```json
{
  "event_type": "phone.event.deduplicated",
  "payload": {
    "original_event_type": "string",
    "user_id": "uuid",
    "device_id": "string",
    "dedup_key_hash": "string",
    "idempotency_key": "string",
    "dedup_window_ttl": "integer",
    "timestamp": "ISO8601"
  }
}
```

### `phone.event.invalid`
```json
{
  "event_type": "phone.event.invalid",
  "payload": {
    "reason": "MISSING_FIELD | INVALID_TYPE | UNKNOWN_EVENT_TYPE",
    "raw_event": "object",
    "timestamp": "ISO8601"
  }
}
```

### `phone.event.dedup.redis_failure`
```json
{
  "event_type": "phone.event.dedup.redis_failure",
  "payload": {
    "original_event_type": "string",
    "user_id": "uuid",
    "redis_error": "string",
    "fallback_mode": "FAIL_OPEN",
    "timestamp": "ISO8601"
  }
}
```

---

---

# SECTION XII — API INTERFACE (LOCKED)

## Internal API Contract

**Endpoint:** `POST /internal/dedup/phone/check`
**Auth:** Service-to-service JWT (Keycloak)
**Rate Limit:** 10,000 req/sec per node

### Request Body
```json
{
  "event_type": "string",
  "user_id": "uuid",
  "device_id": "string",
  "idempotency_key": "string",
  "timestamp": "integer (unix ms)",
  "payload": "object"
}
```

### Response: UNIQUE
```json
{
  "status": "UNIQUE",
  "dedup_key_hash": "string",
  "ttl_set_ms": 20000,
  "proceed": true
}
```

### Response: DUPLICATE
```json
{
  "status": "DEDUPLICATED",
  "dedup_key_hash": "string",
  "remaining_ttl_ms": 14200,
  "proceed": false
}
```

### Response: INVALID
```json
{
  "status": "INVALID",
  "reason": "MISSING_FIELD",
  "field": "device_id",
  "proceed": false
}
```

**HTTP Status Codes:**
- `200 OK` — Always returned (UNIQUE or DUPLICATE or INVALID)
- `500 INTERNAL ERROR` — Only on critical infrastructure failure
- **Never 429** — Rate limiting is TTL-window based, not quota-based

---

---

# SECTION XIII — INTEGRATION WIRING (MANDATORY)

## Services That MUST Wire This Agent

| Service | Event Types | Integration Point |
|---|---|---|
| Auth Service | `phone.otp.*`, `phone.login.*`, `phone.logout.*` | Before OTP send, before session creation |
| Session Lifecycle Manager | `phone.session.*` | On session create/resume/terminate |
| Device Registration Service | `phone.device.*` | On device register, token refresh, online/offline |
| Voice GD Orchestrator | `phone.gd.*` | Before join, before state machine update |
| Dojo Match Engine | `phone.dojo.*` | Before match join, before score write |
| Interview Service | `phone.interview.*` | Before slot lock, before join token issue |
| Notification Service | `phone.notification.*` | Before push/SMS/in-app dispatch |
| Belt Engine | `phone.belt.*` | Before belt award execution |

**Wiring Enforcement:**
Every listed service MUST call `POST /internal/dedup/phone/check` and evaluate `proceed` field before processing.

Services that bypass this agent → **CONTRACT VIOLATION** → **STOP EXECUTION**

---

---

# SECTION XIV — OBSERVABILITY (NON-OPTIONAL)

## Prometheus Metrics

```
# HELP phone_dedup_events_total Total events received
# TYPE phone_dedup_events_total counter
phone_dedup_events_total{event_type, tenant_id, decision}

# HELP phone_dedup_redis_latency_ms Redis SETNX latency
# TYPE phone_dedup_redis_latency_ms histogram
phone_dedup_redis_latency_ms{event_type}

# HELP phone_dedup_redis_failures_total Redis failure count
# TYPE phone_dedup_redis_failures_total counter
phone_dedup_redis_failures_total{}

# HELP phone_dedup_duplicate_rate Duplicate rate per event type (last 5m)
# TYPE phone_dedup_duplicate_rate gauge
phone_dedup_duplicate_rate{event_type}
```

## Grafana Dashboards (Mandatory)

- **Deduplication Rate by Event Type** — Bar chart per event type, last 1h
- **Duplicate Spike Detector** — Alert if dedup rate > 30% for any event in 5m window
- **Redis Latency P99** — Should be < 2ms; alert if > 10ms
- **Redis Failure Timeline** — Immediate visibility on fail-open activations
- **Top Deduplicated Events** — Table of most-deduplicated events by user_id

## Alerting Rules

| Alert | Condition | Severity |
|---|---|---|
| `PhoneDeupRedisDown` | `redis_failures > 0` for 30s | CRITICAL |
| `PhoneDedupSpike` | `dedup_rate > 50%` for 5m | HIGH |
| `PhoneDedupBeltDuplicate` | `belt_award deduplicated > 0` | CRITICAL |
| `PhoneDedupScoreDuplicate` | `dojo_score deduplicated > 0` | HIGH |

---

---

# SECTION XV — SECURITY CONSTRAINTS (LOCKED)

- Dedup audit log is **immutable** (no UPDATE, no DELETE)
- Redis dedup keys contain **no PII** (hash-only)
- Audit log stores `user_id` (UUID) only — no names, emails, phone numbers
- `raw_payload` in audit log is **JSONB with PII fields stripped** before write
- Service-to-service calls are authenticated via **Keycloak service JWT**
- No external exposure of `/internal/dedup/*` endpoints
- Kong API Gateway blocks all external access to `/internal/*` namespace

---

---

# SECTION XVI — DEPLOYMENT SPECIFICATION (LOCKED)

## Kubernetes Namespace

```
Namespace: realtime
Service Name: phone-event-dedup-agent
```

## Resource Defaults (Startup-Stage)

```yaml
resources:
  requests:
    cpu: "100m"
    memory: "128Mi"
  limits:
    cpu: "500m"
    memory: 256Mi"

replicas: 2
autoscaling:
  minReplicas: 2
  maxReplicas: 10
  targetCPUUtilizationPercentage: 60
```

## Health Check Endpoints

```
GET /health/live   → 200 OK if process alive
GET /health/ready  → 200 OK if Redis reachable
```

## Environment Variables Required

```
REDIS_HOST
REDIS_PORT
REDIS_PASSWORD
POSTGRES_HOST
POSTGRES_DB
POSTGRES_USER
POSTGRES_PASSWORD
KAFKA_BROKER_URL
KEYCLOAK_JWKS_URL
LOG_LEVEL
DEDUP_FAIL_OPEN_MODE   # true | false (default: true)
```

---

---

# SECTION XVII — ANTIGRAVITY LAYER DECLARATION (LOCKED)

## What Antigravity Means in This Context

The **Antigravity Layer** in ECOSKILLER is the subsystem that **prevents the platform from collapsing under its own distributed weight.**

In mobile-first, real-time, multi-tenant SaaS:
- Networks are unreliable
- Phones restart, lose signal, and retry
- Users tap buttons multiple times
- WebRTC reconnects fire cascading events
- Push services deliver duplicates under load

Without deduplication:
- Sessions multiply → Redis OOM
- OTPs flood SMS gateways → Cost explosion
- GD state machines receive double-join → Score corruption
- Belt certificates issue twice → Trust breach
- Slot locks collide → Interviews double-booked

The `PHONE_EVENT_DEDUPLICATION_AGENT` applies **structural gravity resistance** — a deterministic, Redis-backed, audit-logged layer that ensures **identical input → identical single output**, regardless of how many times the phone sends the same signal.

**This agent is not optional infrastructure.**
**It is load-bearing SaaS architecture.**

---

---

# SECTION XVIII — FAILURE SCENARIOS & SYSTEM RESPONSES (LOCKED)

| Failure Scenario | Agent Response |
|---|---|
| Redis SETNX timeout > 500ms | Log warning, fail-open, emit `redis_slow` metric |
| Redis cluster fully down | Fail-open, emit CRITICAL alert, bypass dedup |
| Malformed event payload | Reject with `INVALID`, emit `phone.event.invalid` |
| Unknown event type | Reject with `INVALID`, do not process |
| Missing `X-Idempotency-Key` | Rejected at API Gateway before reaching agent |
| Belt award duplicate attempt | Deduplicate, emit audit, critical alert |
| Score submission duplicate | Deduplicate, emit audit, high alert |
| OTP requested 10x in 60 seconds | Each request within window after first = DEDUPLICATED |
| GD rejoin after termination | `phone.gd.rejoin.attempted` DEDUPLICATED as per R53 GD rules |

---

---

# SECTION XIX — INTERN EXECUTION GUIDE

## Service Location

```
/backend/services/phone_event_dedup_agent/
```

## Step-by-Step Local Execution

**Step 1:** Navigate to service folder
```bash
cd /backend/services/phone_event_dedup_agent/
```

**Step 2:** Create virtual environment
```bash
python -m venv venv
source venv/bin/activate  # Mac/Linux
venv\Scripts\activate     # Windows
```

**Step 3:** Install dependencies
```bash
pip install -r requirements.txt
```

**Step 4:** Copy env template
```bash
cp .env.example .env
# Edit .env with local Redis and DB values
```

**Step 5:** Run database migration
```bash
alembic upgrade head
```

**Step 6:** Start the service
```bash
uvicorn main:app --reload --port 8090
```

**Expected Output:**
```
INFO: Uvicorn running on http://127.0.0.1:8090
INFO: PHONE_EVENT_DEDUPLICATION_AGENT ready
INFO: Redis connection: OK
INFO: Database connection: OK
```

**Step 7:** Test via Swagger
```
http://127.0.0.1:8090/docs
```

**Success Condition:**
- `/health/ready` returns `200 OK`
- `POST /internal/dedup/phone/check` with valid payload returns `UNIQUE` or `DEDUPLICATED`
- Audit log entry written in PostgreSQL

**Failure → STOP EXECUTION**

---

---

# SECTION XX — CONTRACT GATE STATUS

| Contract | Status |
|---|---|
| Event Type Registry | LOCKED |
| TTL Window Table | LOCKED |
| Redis Key Schema | LOCKED |
| Dedup Decision Algorithm | LOCKED |
| Audit Log Schema | LOCKED |
| Observability Metrics | LOCKED |
| API Interface Contract | LOCKED |
| Client Idempotency Protocol | LOCKED |
| Failure Mode Rules | LOCKED |
| Service Integration Wiring | LOCKED |

**➤ ALL CONTRACT GATES PASSED**
**➤ AGENT STATUS: SEALED · LOCKED · DEPLOYMENT-READY**

---

---

# SECTION XXI — BOUND LAWS & CROSS-REFERENCES

This agent is bound to and must comply with:

| Law | Binding Clause |
|---|---|
| R1 — Technology Stack Lock | Redis 7, PostgreSQL 15, FastAPI, Kong enforced |
| R4 — Event Schema Registry | All emitted events must be registered in AsyncAPI |
| R5 — Workflow State Machines | GD and Dojo state machines protected by this agent |
| R9 — CI/CD Pipelines | Agent must pass contract validator (R49) before deploy |
| R10 — Security Policies | PII stripping, service JWT, no external exposure |
| R38 — Master Bug Registry | Dedup failure scenarios pre-registered in bug registry |
| R39A — Session Lifecycle Manager | This agent is a mandatory dependency |
| R44 — Runnable Backend Law | Agent must be fully runnable with DB + Redis wiring |
| R49 — Contract Validator CLI | All contracts above validated before every build |
| R60 — Archival Law | Audit log governed under WORM-style retention |
| GD Orchestrator Spec | `phone.gd.*` events locked to GD state machine rules |
| Dojo SAAS v1.0 Spec | `phone.dojo.*` events locked to Dojo scoring integrity rules |

---

---

# 🔒 FINAL SEAL

```
AGENT:      PHONE_EVENT_DEDUPLICATION_AGENT
DOMAIN:     Session & Lifecycle → Antigravity Layer
SYSTEM:     ECOSKILLER v12.0
STATUS:     SEALED
VERSION:    v1.0
MUTATION:   ADD-ONLY VIA VERSION BUMP
AUTHORITY:  HUMAN DECLARATION ONLY

NO CLAUSE MAY BE WEAKENED.
NO CLAUSE MAY BE REINTERPRETED.
NO PARTIAL IMPLEMENTATION PERMITTED.

Violation → STOP EXECUTION → REPORT AGENT SEAL BREACH
```

---

*PHONE_EVENT_DEDUPLICATION_AGENT · ECOSKILLER Session & Lifecycle · Antigravity Layer · v1.0 · SEALED*
