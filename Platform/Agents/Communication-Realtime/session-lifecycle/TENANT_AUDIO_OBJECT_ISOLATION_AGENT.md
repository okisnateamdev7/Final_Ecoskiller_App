# TENANT_AUDIO_OBJECT_ISOLATION_AGENT
## Security & Compliance Division — Ecoskiller SaaS Platform

```
Artifact Class  : Production Security Agent Specification
Agent ID        : SEC-COMP-TAOIA-001
Status          : FINAL · SEALED · LOCKED · NON-NEGOTIABLE
Version         : v1.0
Mutation Policy : Add-only via version bump
Interpretation Authority : NONE
Execution Authority : Human declaration only
Domain          : Security & Compliance → Audio Object Isolation
Parent System   : ECOSKILLER MASTER EXECUTION PROMPT v12.0
Applies To      : Voice GD System · Dojo Match Engine · Interview Service
Classification  : ANTIGRAVITY — Sealed Prompt Artifact
```

---

> **SEAL DECLARATION**
> This document is a locked system agent specification. No clause may be
> reinterpreted, softened, removed, or overridden without a formal version bump
> and explicit human-signed declaration. Any deviation from this specification
> is a SECURITY VIOLATION and must trigger STOP EXECUTION.

---

## SECTION 1 — AGENT IDENTITY & PURPOSE

### 1.1 Agent Name
`TENANT_AUDIO_OBJECT_ISOLATION_AGENT` (TAOIA)

### 1.2 Classification
**Security & Compliance Agent** — enforces hard cryptographic and logical
isolation between tenant audio session objects across all real-time voice
infrastructure within the Ecoskiller platform.

### 1.3 Problem Statement

Ecoskiller operates as a **multi-tenant SaaS** across:

- Voice Group Discussion (GD) rooms (automated, recruiter-less)
- Dojo match audio channels (LiveKit / Jitsi)
- Interview session voice transport

All three systems produce **audio session objects** — rooms, tokens, state
machine records, Redis locks, WebSocket channels, database rows, and MinIO
artifacts — that **must never cross tenant boundaries**, even under failure,
under load, or through misconfigured orchestration.

The TAOIA is the permanent enforcement agent for this guarantee.

### 1.4 Governing Principle

> **One tenant's audio session object must be cryptographically, logically,
> and operationally invisible to all other tenants at every layer of the stack.**

No exception. No override. No workaround.

---

## SECTION 2 — SCOPE OF ENFORCEMENT

TAOIA governs all objects generated or consumed during audio session lifecycle:

| Object Class | Examples | Governed |
|---|---|---|
| Room identifiers | `gd_banking_20240206_1234` | ✅ Yes |
| WebRTC tokens | Short-lived JWT join tokens | ✅ Yes |
| Redis state keys | GD state machine keys, turn locks | ✅ Yes |
| WebSocket channels | Backend → frontend mute/unmute commands | ✅ Yes |
| PostgreSQL rows | Session metadata, participant maps, scores | ✅ Yes |
| MinIO objects | Consent records, audit files | ✅ Yes |
| Kafka / RabbitMQ events | `gd.completed`, `match.scored` | ✅ Yes |
| ClickHouse metrics | Speaking time, interrupt counts | ✅ Yes |
| Jitsi room names | Auto-generated room strings | ✅ Yes |
| LiveKit room tokens | Dojo match & interview tokens | ✅ Yes |
| OTel traces | Distributed tracing spans | ✅ Yes |

---

## SECTION 3 — THREAT MODEL (WHAT THIS AGENT PREVENTS)

### 3.1 Cross-Tenant Room Bleed
A candidate from Tenant A joins an audio room belonging to Tenant B due to
shared room namespace or missing tenant prefix enforcement.

**Verdict: CRITICAL. Must be eliminated at room creation.**

### 3.2 Token Forging / Replay Across Tenants
A valid short-lived token issued to Tenant A is replayed against a Tenant B
room. Backend does not validate tenant binding on token verification.

**Verdict: CRITICAL. Token must carry tenant_id as non-mutable signed claim.**

### 3.3 Redis Key Namespace Collision
Two tenants' GD state machines share Redis key patterns due to missing tenant
prefix. Turn control and mute/unmute state bleed across sessions.

**Verdict: CRITICAL. All Redis keys must be namespaced by tenant_id.**

### 3.4 WebSocket Channel Cross-Contamination
A WebSocket server broadcasts mute/unmute commands to the wrong room due to
missing room-scope filtering. Candidate from Tenant A receives control signals
from Tenant B's orchestrator.

**Verdict: CRITICAL. WebSocket channels must be scoped to session_id +
tenant_id composite.**

### 3.5 PostgreSQL Row-Level Isolation Failure
Tenant A's admin queries participant records for Tenant B due to missing
Row-Level Security (RLS) policy on audio session tables.

**Verdict: CRITICAL. RLS must be enforced on all audio session tables.**

### 3.6 Object Storage Path Traversal
Tenant A constructs a MinIO path that resolves to Tenant B's audio consent or
audit files by traversing the prefix structure.

**Verdict: HIGH. All MinIO paths must include tenant_id as first path segment.**

### 3.7 Analytics / ClickHouse Tenant Leakage
An analytics query issued by Tenant A's admin panel returns speaking-time or
scoring metrics from Tenant B's GD sessions.

**Verdict: HIGH. All ClickHouse writes must embed tenant_id. All reads must
filter on tenant_id. No unscoped query permitted.**

### 3.8 Event Bus Routing Leak
A Kafka or RabbitMQ consumer in Tenant A's service receives events published
by Tenant B's GD Orchestrator due to missing topic namespace or shared
consumer group.

**Verdict: HIGH. All async events must carry tenant_id in envelope header.**

### 3.9 Jitsi Room Name Guessing
Jitsi rooms are named with predictable patterns. A candidate from Tenant A
guesses a Tenant B room name and joins without a valid token.

**Verdict: HIGH. Room names must include a cryptographic entropy component
bound to tenant_id. Rooms must require token validation.**

---

## SECTION 4 — ENFORCEMENT ARCHITECTURE

### 4.1 Tenant Isolation Model

```
ISOLATION MODEL: HARD CRYPTOGRAPHIC + LOGICAL SEPARATION

tenant_id  →  namespace prefix on ALL objects
           →  signed claim in ALL tokens
           →  RLS policy on ALL database tables
           →  partition key on ALL analytics writes
           →  topic prefix on ALL event bus messages
           →  path prefix on ALL object storage operations
           →  channel scope on ALL WebSocket connections
```

### 4.2 Room Naming Protocol (LOCKED)

**Format:**

```
{tenant_id}_{domain}_{topic_slug}_{date}_{entropy}

Example:
  t_acme_gd_banking_20240206_a3f9e2b1
  t_xyz_dojo_scenario17_20240206_c7d12f4a
  t_abc_interview_slotid92_20240206_b8e6a3f2
```

**Rules:**

- `tenant_id` is always first segment — non-negotiable
- `entropy` is 8-byte cryptographically random hex — generated server-side only
- Room name is **never exposed to the client** before token issuance
- Room name is **never reused** — it is created and destroyed per session
- Backend Orchestrator owns room name generation — no client input accepted

**Absence of entropy → STOP EXECUTION**
**Absence of tenant_id prefix → STOP EXECUTION**

### 4.3 Token Issuance Protocol (LOCKED)

All audio join tokens (Jitsi, LiveKit) must carry the following signed claims:

```json
{
  "tenant_id": "<immutable>",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "room_name": "<tenant-prefixed>",
  "role": "candidate | mentor | observer",
  "exp": "<unix timestamp — max 300 seconds from issuance>",
  "iat": "<unix timestamp>",
  "jti": "<unique token ID — for replay prevention>"
}
```

**Enforcement rules:**

- Token signed with HMAC-SHA256 using `HashiCorp Vault`-managed secret
- `tenant_id` claim is non-mutable — server validates on every join attempt
- Token TTL: **300 seconds maximum** — no extension permitted
- `jti` stored in Redis for replay detection — TTL = token TTL
- Token rejected if: tenant_id mismatch, room_name prefix mismatch, expired, replayed
- Backend **never** generates a token without validating room ownership by tenant

**Token forging attempt → LOG + ALERT + DENY + AUDIT RECORD**

### 4.4 Redis Key Namespace Protocol (LOCKED)

All Redis keys for audio session state must follow this schema:

```
taoia:{tenant_id}:{session_type}:{session_id}:{object_type}

Examples:
  taoia:t_acme:gd:sess_abc123:state_machine
  taoia:t_acme:gd:sess_abc123:turn_lock
  taoia:t_acme:gd:sess_abc123:participant_queue
  taoia:t_acme:gd:sess_abc123:timer
  taoia:t_xyz:dojo:match_def456:state
  taoia:t_xyz:interview:slot_789:lock
```

**Rules:**

- No Redis key for audio objects may exist without `taoia:{tenant_id}:` prefix
- All Redis TTLs for session state must be set at write time — no eternal keys
- Redis SCAN operations must always include the tenant prefix pattern
- Cross-tenant key access is structurally impossible when prefix is enforced
- Keyspace notifications, if enabled, must be scoped per tenant

**Unnamespaced key write → STOP EXECUTION → AUDIT LOG**

### 4.5 WebSocket Channel Isolation Protocol (LOCKED)

The WebSocket command channel (backend → frontend for mute/unmute, timer,
session control) must be isolated per session and per tenant:

```
Channel Scope: ws://backend/taoia/{tenant_id}/{session_id}

Rules:
- Server must validate tenant_id + session_id on every connection upgrade
- Connection refused if participant's token tenant_id ≠ channel tenant_id
- No broadcast permitted across session boundaries
- No shared channel multiplexed across tenants
- Disconnection on token expiry — no reconnect with same token
```

**Cross-tenant WebSocket message delivery → CRITICAL SECURITY VIOLATION**

### 4.6 PostgreSQL Row-Level Security Protocol (LOCKED)

All tables containing audio session data must enforce RLS:

**Affected tables (non-exhaustive):**

```sql
-- Audio session tables requiring RLS
gd_sessions
gd_participants
gd_turn_logs
gd_scores
gd_interrupt_logs
interview_sessions
interview_participants
dojo_match_sessions
dojo_match_participants
audio_session_audit_logs
```

**RLS Policy Template:**

```sql
-- Enable RLS on every audio session table
ALTER TABLE gd_sessions ENABLE ROW LEVEL SECURITY;

-- Policy: tenant can only see their own rows
CREATE POLICY tenant_isolation_policy ON gd_sessions
  USING (tenant_id = current_setting('app.current_tenant_id')::uuid);

-- current_setting must be set at the start of every DB transaction
-- by the application connection pool — NOT by the query itself
```

**Rules:**

- RLS must be enabled on table creation — not retrofitted
- Application must set `app.current_tenant_id` on every connection before query
- Superuser queries (admin, migrations) must be explicitly audited
- No `SECURITY DEFINER` functions that bypass RLS on audio session tables
- Flyway migrations must not disable RLS during schema changes

**Absent RLS policy on audio table → STOP EXECUTION**

### 4.7 MinIO Object Storage Isolation Protocol (LOCKED)

All audio-related object storage paths must follow:

```
/{tenant_id}/audio-sessions/{session_type}/{session_id}/{artifact_type}/{filename}

Examples:
  /t_acme/audio-sessions/gd/sess_abc123/consent/consent_record.json
  /t_acme/audio-sessions/gd/sess_abc123/audit/turn_log.json
  /t_xyz/audio-sessions/interview/slot_789/audit/session_meta.json
```

**Rules:**

- `tenant_id` is always root path segment — no exception
- Presigned URLs must include `tenant_id` path validation server-side
- MinIO bucket policies must deny cross-path access
- No shared bucket paths across tenants
- All object writes must be server-side only — no client-direct uploads for
  audio session artifacts
- Path traversal attempts (`../`, `%2F%2E%2E`) → LOG + DENY + ALERT

### 4.8 Event Bus (Kafka / RabbitMQ) Isolation Protocol (LOCKED)

All events emitted by audio session services must carry tenant_id in the
event envelope:

**Event Envelope Schema:**

```json
{
  "event_id": "<uuid>",
  "event_type": "gd.completed | match.scored | interview.completed",
  "tenant_id": "<required — non-mutable>",
  "session_id": "<uuid>",
  "emitted_at": "<ISO 8601>",
  "payload": { }
}
```

**Kafka Topic Naming:**

```
ecoskiller.{tenant_id}.{domain}.{event_type}

Examples:
  ecoskiller.t_acme.gd.completed
  ecoskiller.t_xyz.dojo.match_scored
  ecoskiller.t_abc.interview.completed
```

**Consumer Rules:**

- Consumers must validate `tenant_id` in envelope before processing
- No consumer may subscribe to a wildcard topic pattern that includes
  multiple tenants' events
- Dead letter queue must also be tenant-scoped
- Event replay must enforce tenant_id filtering

### 4.9 ClickHouse Analytics Isolation Protocol (LOCKED)

All analytics writes for audio sessions must embed `tenant_id` as a partition
column:

```sql
-- Example GD metrics table
CREATE TABLE gd_metrics (
  tenant_id       UUID NOT NULL,
  session_id      UUID NOT NULL,
  participant_id  UUID NOT NULL,
  mic_open_ms     UInt64,
  turns_completed UInt8,
  interrupt_count UInt8,
  event_ts        DateTime
) ENGINE = MergeTree()
PARTITION BY (tenant_id, toYYYYMM(event_ts))
ORDER BY (tenant_id, session_id, event_ts);
```

**Query Rules:**

- Every analytics query MUST include `WHERE tenant_id = ?` as a mandatory
  filter — no exception
- Dashboard API must inject tenant_id from authenticated session context —
  never from client request parameter
- No unscoped analytical views or materialized views across tenants

---

## SECTION 5 — AGENT OPERATIONAL LIFECYCLE

### 5.1 Session Birth (Room Creation)

```
TRIGGER: GD Orchestrator / Dojo Match Engine / Interview Service requests room

TAOIA Actions:
  1. Validate request carries authenticated tenant_id from auth context
  2. Generate room_name with tenant prefix + entropy
  3. Register room in PostgreSQL with tenant_id binding (RLS-protected row)
  4. Create Redis state machine key with taoia:{tenant_id}: namespace
  5. Issue short-lived join tokens (≤ 300s) with tenant_id claim
  6. Return room_name and tokens to orchestrator — never to client directly
  7. Write audit log: room_created, tenant_id, session_id, timestamp
```

### 5.2 Session Active (Participant In Room)

```
TRIGGER: Participant joins via token

TAOIA Actions:
  1. Validate token signature
  2. Validate tenant_id claim matches room tenant_id prefix
  3. Validate jti not in Redis replay cache
  4. Validate room_name claim matches actual room
  5. Upgrade WebSocket connection to scoped channel: taoia/{tenant_id}/{session_id}
  6. Log join event to PostgreSQL (RLS-protected)
  7. All mute/unmute commands delivered only within channel scope
  8. All timer, turn, and state events scoped to session_id
```

### 5.3 Session Termination

```
TRIGGER: Orchestrator closes session OR timeout OR all participants exit

TAOIA Actions:
  1. Force-mute all participants via Jitsi/LiveKit API
  2. Destroy WebSocket channel scope
  3. Delete Redis state machine keys (all taoia:{tenant_id}:{session_id}:* keys)
  4. Mark PostgreSQL session row as terminated (RLS-protected)
  5. Write final audit record to MinIO (tenant-prefixed path)
  6. Emit gd.completed / match.scored / interview.completed event with tenant_id envelope
  7. Invalidate all outstanding tokens for session (Redis jti cache)
  8. Room name retired — never reused
```

### 5.4 Failure Handling

```
Failure Type               → TAOIA Response
────────────────────────── → ──────────────────────────────────────
Token tenant mismatch      → DENY + ALERT + AUDIT_LOG + STOP SESSION
Room name no tenant prefix → STOP EXECUTION + ALERT
Redis key cross-namespace  → STOP EXECUTION + ALERT
RLS policy violation       → DENY + ALERT + AUDIT_LOG
WebSocket cross-channel    → DENY + DISCONNECT + ALERT
MinIO path traversal       → DENY + ALERT + AUDIT_LOG
Event missing tenant_id    → DISCARD + ALERT + AUDIT_LOG
Analytics unscoped query   → DENY + ALERT
Replay token detected      → DENY + BAN jti + ALERT + AUDIT_LOG
```

---

## SECTION 6 — AUDIT & OBSERVABILITY

### 6.1 Immutable Audit Log Schema

Every TAOIA enforcement action must produce an immutable audit record:

```json
{
  "audit_id": "<uuid>",
  "agent": "TENANT_AUDIO_OBJECT_ISOLATION_AGENT",
  "tenant_id": "<uuid>",
  "session_id": "<uuid>",
  "action": "room_created | token_issued | join_validated | join_denied | session_terminated | violation_detected",
  "actor_id": "<participant_id or system>",
  "resource_type": "room | token | redis_key | websocket | db_row | storage_object | event",
  "resource_id": "<identifier>",
  "result": "ALLOWED | DENIED | VIOLATION",
  "violation_type": "<null or threat category from Section 3>",
  "timestamp": "<ISO 8601>",
  "trace_id": "<OpenTelemetry trace ID>"
}
```

Audit records must be written to:
- PostgreSQL `audio_session_audit_logs` table (RLS-protected)
- MinIO `/{tenant_id}/audio-sessions/audits/` (for long-term retention)

### 6.2 Wazuh SIEM Integration

TAOIA must emit structured security events to Wazuh for:
- Token rejection (any reason)
- Cross-tenant access attempt
- Replay token detection
- Unnamespaced Redis key write
- RLS violation
- MinIO path traversal attempt

**Alert severity levels:**
- `CRITICAL` — Any cross-tenant access attempt
- `HIGH` — Token replay, path traversal
- `MEDIUM` — Policy validation failures
- `LOW` — Audit log write failures (triggers separate alert)

### 6.3 Prometheus Metrics

TAOIA must expose the following metrics to Prometheus:

```
taoia_sessions_active{tenant_id}
taoia_tokens_issued_total{tenant_id, session_type}
taoia_tokens_rejected_total{tenant_id, rejection_reason}
taoia_violations_total{tenant_id, violation_type}
taoia_audit_writes_total{tenant_id}
taoia_redis_keys_active{tenant_id}
```

### 6.4 Grafana Dashboards (Mandatory)

The following dashboards must exist for TAOIA:

- Cross-tenant violation rate (by tenant, by violation type)
- Token rejection rate (by reason)
- Active session count (by tenant)
- Redis key namespace compliance rate
- Audit log write success rate
- SIEM alert volume over time

---

## SECTION 7 — INTEGRATION CONTRACTS

### 7.1 Dependency Contract: Auth Service

TAOIA **requires** the Auth Service to:
- Inject authenticated `tenant_id` into every request context
- Never permit a request to reach the GD Orchestrator, Dojo Engine, or
  Interview Service without a valid `tenant_id` in JWT

**Broken contract → STOP SESSION CREATION**

### 7.2 Dependency Contract: Voice GD Orchestrator

GD Orchestrator must:
- Call TAOIA room creation API — never generate room names independently
- Use TAOIA-issued tokens only — never construct tokens internally
- Route all state writes through TAOIA-namespaced Redis keys
- Never bypass TAOIA for any audio session object lifecycle operation

### 7.3 Dependency Contract: Dojo Match Engine

Same as 7.2 — applies identically to LiveKit-based dojo match rooms.

### 7.4 Dependency Contract: Interview Service

Same as 7.2 — applies identically to interview session voice rooms.

### 7.5 Dependency Contract: HashiCorp Vault

- All token signing secrets stored in Vault — never in environment variables
- Vault lease for signing key: 24 hours with auto-rotation
- TAOIA must retrieve signing key from Vault on each service startup

### 7.6 Dependency Contract: Open Policy Agent (OPA)

TAOIA delegates authorization policy evaluation to OPA for:
- "Can this tenant_id create a room of this type?"
- "Can this participant_id join this session_id?"
- "Can this admin_id read audit logs for this tenant_id?"

OPA policies are version-controlled. Policy changes require explicit
human-signed approval.

---

## SECTION 8 — DEPLOYMENT REQUIREMENTS

### 8.1 Kubernetes Namespace

TAOIA runs in the `realtime` namespace alongside the GD Orchestrator,
Dojo Match Engine, and Interview Service.

```yaml
namespace: realtime
labels:
  app: taoia
  tier: security-compliance
  criticality: critical
```

### 8.2 Resource Limits (Minimum)

```yaml
resources:
  requests:
    cpu: "500m"
    memory: "512Mi"
  limits:
    cpu: "2"
    memory: "1Gi"
```

### 8.3 Health Probes

```yaml
livenessProbe:
  httpGet:
    path: /health/live
    port: 8080
readinessProbe:
  httpGet:
    path: /health/ready
    port: 8080
```

Readiness must verify:
- Redis connection with correct namespace prefix
- Vault connection and key availability
- PostgreSQL connection with RLS validation check
- OPA connection

### 8.4 Secrets Management

- All secrets injected via Vault Agent Sidecar
- No secrets in ConfigMaps, environment variables, or Helm values files
- Secret rotation must not require pod restart

---

## SECTION 9 — COMPLIANCE DECLARATIONS

### 9.1 GDPR / Data Privacy

- TAOIA does not store audio content — only behavioral metadata
- All PII in audit records is encrypted at rest (AES-256)
- Audit records for a tenant may be deleted on verified data erasure request
- Consent records stored in tenant-isolated MinIO paths

### 9.2 Zero-Trust Assertion

TAOIA operates on zero-trust principles:
- Every token verified on every request — no session-level trust inheritance
- Every Redis key access pattern validated at write time
- Every WebSocket connection re-authenticated on reconnect
- Every database query evaluated against RLS — no trusted internal bypass

### 9.3 Immutability Guarantee

- Audit logs are write-once — no UPDATE or DELETE permitted on audit tables
- MinIO audit objects stored with WORM policy (15-year retention minimum)
- Scoring records derived from TAOIA-enforced sessions are immutable

---

## SECTION 10 — FINAL ENFORCEMENT DECLARATION

```
┌─────────────────────────────────────────────────────────────┐
│  TENANT_AUDIO_OBJECT_ISOLATION_AGENT — FINAL LAW            │
│                                                             │
│  1. No audio session object exists without tenant_id        │
│  2. No token is valid across tenant boundaries              │
│  3. No Redis key exists without taoia:{tenant_id}: prefix   │
│  4. No database row is readable outside tenant RLS          │
│  5. No WebSocket channel crosses session or tenant scope    │
│  6. No storage path exists without tenant_id root segment   │
│  7. No event is processed without tenant_id envelope        │
│  8. No analytics query runs without tenant_id WHERE clause  │
│  9. Every violation produces an immutable audit record      │
│  10. Every violation triggers a SIEM alert                  │
│                                                             │
│  Violation of any rule above:                               │
│  → STOP SESSION                                             │
│  → DENY ACCESS                                              │
│  → WRITE AUDIT LOG                                          │
│  → FIRE SIEM ALERT                                          │
│  → REPORT TO ADMIN GOVERNANCE SERVICE                       │
│                                                             │
│  No exceptions. No overrides. No workarounds.               │
└─────────────────────────────────────────────────────────────┘
```

---

## SECTION 11 — VERSION CONTROL

| Version | Date | Change | Authority |
|---|---|---|---|
| v1.0 | 2026-03-04 | Initial sealed specification | Human declaration |

**Next version: v1.1 — Add-only. No mutations to existing clauses.**

---

```
SEAL: TENANT_AUDIO_OBJECT_ISOLATION_AGENT · SEC-COMP-TAOIA-001 · v1.0
CLASSIFICATION: ANTIGRAVITY — SEALED PROMPT ARTIFACT
STATUS: FINAL · LOCKED · GOVERNED · NON-NEGOTIABLE
```
