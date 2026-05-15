# CALL_RATE_LIMIT_AGENT
## Billing & Quota Division — Ecoskiller SaaS Platform

```
Artifact Class       : Production Billing & Quota Agent Specification
Agent ID             : BILL-QUOTA-CRLA-001
Status               : FINAL · SEALED · LOCKED · NON-NEGOTIABLE
Version              : v1.0
Mutation Policy      : Add-only via version bump
Interpretation Auth  : NONE
Execution Authority  : Human declaration only
Domain               : Billing & Quota → Call Rate Limit Enforcement
Parent System        : ECOSKILLER MASTER EXECUTION PROMPT v12.0
Applies To           : Voice GD · Dojo Match Engine · Interview Service
                       API Gateway · WebSocket · Event Bus · All Tenant Tiers
Classification       : ANTIGRAVITY — Sealed Prompt Artifact
Enforcement Stack    : Kong · Envoy · Redis · PostgreSQL · Kafka · ClickHouse
```

---

> **SEAL DECLARATION**
> This document is a locked system agent specification. No clause may be
> reinterpreted, softened, removed, or overridden without a formal version
> bump and explicit human-signed declaration. Any deviation from this
> specification is a BILLING INTEGRITY VIOLATION and must trigger
> STOP EXECUTION.

---

## SECTION 1 — AGENT IDENTITY & PURPOSE

### 1.1 Agent Name
`CALL_RATE_LIMIT_AGENT` (CRLA)

### 1.2 Classification
**Billing & Quota Agent** — enforces deterministic, plan-bound, per-tenant
rate limits on every call type emitted by or directed at Ecoskiller services,
covering API calls, voice session initiations, WebSocket command channels,
event bus publications, and all metered platform operations.

### 1.3 Problem Statement

Ecoskiller is a **multi-tenant SaaS** serving Students, Trainers, Evaluators,
Institutes, Enterprises, Recruiters, Admins, Parents, and Automation Agents
across five domain tracks (Arts, Commerce, Science, Technology, Administration).

Every tenant tier (Free, Starter, Professional, Enterprise, Custom) has
contracted **call and session quotas**. Without a hard enforcement agent:

- A free-tier tenant can exhaust shared GD infrastructure by creating
  unlimited voice sessions
- A Starter recruiter can make unlimited API calls bypassing plan limits
- A burst of concurrent dojo matches from one tenant can starve other tenants'
  session capacity
- An automation agent (non-human) can call APIs at machine speed with no
  throttling, generating fraudulent usage or denial-of-service conditions
- Revenue leakage occurs when metered events are not counted and invoiced

The CALL_RATE_LIMIT_AGENT is the permanent, deterministic enforcement agent
for call rate, quota, and usage metering across the entire platform.

### 1.4 Governing Principle

> **Every call — API, voice session, WebSocket, event, or background job —
> is plan-bound, tenant-scoped, metered, enforced in real time, and immutably
> audited. No call executes outside its quota. No call is billed without a
> meter record. No meter record exists without a tenant binding.**

No exception. No override. No workaround.

---

## SECTION 2 — CALL TAXONOMY (COMPLETE)

CRLA governs every call class produced or consumed within Ecoskiller:

| Call Class | Examples | Metered | Rate-Limited | Quota-Gated |
|---|---|---|---|---|
| REST API calls | CRUD, search, job post, application | ✅ | ✅ | ✅ |
| Voice GD session initiations | GD room create, batch create | ✅ | ✅ | ✅ |
| Voice GD participant joins | Token-based join per candidate | ✅ | ✅ | ✅ |
| Dojo match initiations | Match room create, scenario assign | ✅ | ✅ | ✅ |
| Interview session bookings | Slot lock, token issuance | ✅ | ✅ | ✅ |
| WebSocket connections | GD command channel, dojo control | ✅ | ✅ | ✅ |
| WebSocket message sends | Mute/unmute commands, timer events | ✅ | ✅ | ❌ |
| Event bus publications | Kafka / RabbitMQ emit | ✅ | ✅ | ❌ |
| Notification dispatches | Email, SMS, push per tenant | ✅ | ✅ | ✅ |
| Search queries | OpenSearch candidate / job queries | ✅ | ✅ | ✅ |
| File upload operations | MinIO resume / certificate / invoice | ✅ | ✅ | ✅ |
| Analytics queries | ClickHouse dashboard reads | ✅ | ✅ | ❌ |
| Webhook deliveries | Outbound webhook per subscription | ✅ | ✅ | ✅ |
| Background job submissions | Airflow DAG triggers, batch jobs | ✅ | ✅ | ✅ |
| AI / automation agent calls | Non-human API consumers | ✅ | ✅ | ✅ |
| Admin API calls | Tenant admin, platform admin ops | ✅ | ✅ | ✅ |

---

## SECTION 3 — PLAN TIER DEFINITIONS (LOCKED)

### 3.1 Tenant Tier Registry

Six tiers govern all quota allocations. Tiers are assigned at tenant
onboarding and enforced by CRLA on every call:

| Tier ID | Tier Name | Target Persona |
|---|---|---|
| T0 | FREE | Student (self-learning, unverified) |
| T1 | STARTER | Small institute / early recruiter |
| T2 | PROFESSIONAL | Mid-size college / active enterprise |
| T3 | ENTERPRISE | Corporate / large institute |
| T4 | PLATFORM_ADMIN | Internal Ecoskiller platform operators |
| T5 | CUSTOM | Negotiated enterprise contract |

### 3.2 API Call Quotas (Per Tenant Per Day — Rolling 24h Window)

| Endpoint Category | T0 FREE | T1 STARTER | T2 PROFESSIONAL | T3 ENTERPRISE | T4 ADMIN |
|---|---|---|---|---|---|
| Auth (login, refresh, MFA) | 50 | 200 | 1,000 | 5,000 | Unlimited |
| User profile read/write | 100 | 500 | 2,000 | 10,000 | Unlimited |
| Job CRUD | 10 | 100 | 1,000 | 10,000 | Unlimited |
| Job search / candidate search | 50 | 500 | 5,000 | 50,000 | Unlimited |
| Application submission | 20 | 200 | 2,000 | 20,000 | Unlimited |
| Notification dispatch | 20 | 500 | 5,000 | 50,000 | Unlimited |
| File upload | 5 | 50 | 500 | 5,000 | Unlimited |
| Webhook delivery | 10 | 100 | 1,000 | 10,000 | Unlimited |
| Analytics / dashboard query | 10 | 100 | 1,000 | 10,000 | Unlimited |
| AI / automation agent calls | 0 | 50 | 500 | 5,000 | Unlimited |

**Absence of quota record for a tenant → DENY ALL CALLS → ALERT → REPORT**

### 3.3 Voice Session Quotas (Per Tenant Per Calendar Month)

| Session Type | T0 FREE | T1 STARTER | T2 PROFESSIONAL | T3 ENTERPRISE | T5 CUSTOM |
|---|---|---|---|---|---|
| GD sessions (room creates) | 2 | 20 | 200 | 2,000 | Negotiated |
| GD participants per session | 5 | 8 | 10 | 12 | Negotiated |
| GD batches per day | 1 | 5 | 50 | 500 | Negotiated |
| Dojo match sessions | 5 | 50 | 500 | 5,000 | Negotiated |
| Interview session bookings | 0 | 10 | 200 | 2,000 | Negotiated |
| Concurrent voice sessions | 1 | 3 | 20 | 100 | Negotiated |
| Max session duration (min) | 30 | 60 | 90 | 120 | Negotiated |

**Quota exhausted → DENY ROOM CREATION → EMIT quota.exhausted event → NOTIFY TENANT ADMIN**

### 3.4 Real-Time Rate Limits (Per Tenant — Sliding Window)

These are burst and sustained rate caps enforced at the API Gateway (Kong)
and service mesh (Envoy) layer, independent of monthly quota:

| Call Type | Window | T0 FREE | T1 STARTER | T2 PROFESSIONAL | T3 ENTERPRISE |
|---|---|---|---|---|---|
| REST API calls | 60 sec | 30 rpm | 120 rpm | 600 rpm | 3,000 rpm |
| REST API calls | 1 sec | 5 rps | 20 rps | 100 rps | 500 rps |
| Voice session create | 60 sec | 1 | 3 | 10 | 50 |
| WebSocket connect | 60 sec | 2 | 10 | 50 | 200 |
| WebSocket messages | 1 sec | 5 | 20 | 100 | 500 |
| Search queries | 60 sec | 10 | 60 | 300 | 1,500 |
| File uploads | 60 min | 1 | 5 | 30 | 200 |
| Notification dispatch | 60 min | 5 | 50 | 500 | 5,000 |
| Webhook deliveries | 60 sec | 2 | 10 | 100 | 1,000 |
| Event bus publications | 1 sec | 2 | 10 | 50 | 250 |

**Rate limit breach → HTTP 429 + Retry-After header → METER VIOLATION → AUDIT LOG**

---

## SECTION 4 — ENFORCEMENT ARCHITECTURE

### 4.1 Enforcement Layer Stack

CRLA enforcement operates at four distinct layers simultaneously:

```
LAYER 1 — Kong API Gateway (Ingress — first line)
  → IP-level rate limiting (per IP, sliding window)
  → Tenant-level rate limiting (per tenant_id from JWT)
  → Plan-tier enforcement via Kong plugin + CRLA policy feed
  → Returns HTTP 429 with Retry-After before request hits service

LAYER 2 — Envoy Service Mesh (Inter-service — second line)
  → Rate limiting between microservices (circuit breaking)
  → Prevents a single service from hammering another
  → Per-service call budget enforcement
  → Retries and backpressure management

LAYER 3 — CRLA Middleware (Application — third line)
  → Business-logic quota checks (monthly session quota)
  → Feature gating based on plan tier
  → Pre-authorization of voice session creation
  → Metering record write before action execution

LAYER 4 — Redis State Machine (Atomic counters — fourth line)
  → Atomic increment of all rate-limit counters
  → Sliding window calculations using ZADD + ZRANGEBYSCORE
  → Token bucket state for burst allowances
  → All counter keys namespaced by tenant_id (CRLA prefix)
```

### 4.2 Redis Counter Namespace Protocol (LOCKED)

All rate-limit counters in Redis must follow this schema:

```
crla:{tenant_id}:{call_class}:{window_type}:{window_id}

Examples:
  crla:t_acme:api_call:sliding:1704067200
  crla:t_acme:gd_session_create:monthly:2024-02
  crla:t_acme:websocket_connect:sliding:1704067200
  crla:t_acme:notification_dispatch:hourly:1704067200
  crla:t_acme:file_upload:hourly:1704067200
  crla:t_xyz:dojo_match:monthly:2024-02
  crla:t_abc:interview_booking:monthly:2024-02
```

**Counter rules:**

- All Redis keys for CRLA must be prefixed `crla:{tenant_id}:`
- Sliding window implemented using Redis sorted sets (ZADD / ZRANGEBYSCORE)
- Fixed window implemented using Redis INCR with TTL set at key creation
- Token bucket implemented using Redis hash with `tokens`, `last_refill`
- All counter writes are atomic (Redis MULTI/EXEC or Lua script)
- No counter key exists without `tenant_id` — unscoped keys: STOP EXECUTION

**Key TTL rules:**

| Window Type | TTL |
|---|---|
| Per-second (1s) | 10 seconds |
| Per-minute (60s) | 300 seconds |
| Per-hour | 7,200 seconds |
| Per-day | 172,800 seconds |
| Per-month | 33 days (rolls to next month safely) |

### 4.3 Kong Plugin Configuration (LOCKED)

Kong enforces CRLA policies via the `rate-limiting-advanced` plugin
configured per tenant using the plan tier fetched from CRLA policy store:

```yaml
plugins:
  - name: rate-limiting-advanced
    config:
      identifier: consumer          # consumer = authenticated tenant_id
      limit:
        - 30                        # T0: 30 per minute (resolved at runtime)
      window_size:
        - 60
      window_type: sliding
      retry_after_jitter_max: 0
      error_code: 429
      error_message: "CRLA: Call rate limit exceeded. Upgrade plan or retry after window."
      namespace: crla               # Isolates counters from other Redis namespaces
      sync_rate: 0.25               # Sync every 250ms for accuracy
      strategy: redis
      redis:
        host: redis-crla
        port: 6379
        database: 1                 # Dedicated Redis DB for CRLA counters
```

**Dynamic plan injection:**
Kong fetches the plan tier for each `tenant_id` from CRLA Policy Cache
(Redis DB 0) on each request. Plan cache TTL: 60 seconds.
Plan change takes effect within 60 seconds — no pod restart required.

### 4.4 Pre-Authorization Gate for Voice Sessions (LOCKED)

Before the Voice GD Orchestrator, Dojo Match Engine, or Interview Service
creates any session, they MUST call the CRLA Pre-Authorization API:

```
POST /crla/v1/authorize-session

Request:
{
  "tenant_id": "<uuid>",
  "session_type": "gd | dojo | interview",
  "participant_count": <integer>,
  "requested_duration_minutes": <integer>,
  "plan_tier": "<T0|T1|T2|T3|T5>"
}

Response (ALLOWED):
{
  "authorized": true,
  "authorization_token": "<short-lived 60s token>",
  "quota_remaining": {
    "monthly_sessions": 47,
    "concurrent_sessions": 3,
    "participants_allowed": 10
  }
}

Response (DENIED):
{
  "authorized": false,
  "denial_reason": "MONTHLY_QUOTA_EXHAUSTED | CONCURRENT_LIMIT_REACHED | PLAN_FEATURE_GATED | PARTICIPANT_LIMIT_EXCEEDED | DURATION_LIMIT_EXCEEDED",
  "quota_remaining": { ... },
  "upgrade_prompt": true
}
```

**Rules:**

- Session creation WITHOUT a valid authorization token → STOP SESSION → ALERT
- Authorization token is single-use and expires in 60 seconds
- Orchestrator must pass authorization token in session creation header
- CRLA validates token on session creation before room provisioning
- Denial triggers `quota.exhausted` or `rate.limit.breached` event on Kafka

### 4.5 Concurrent Session Enforcement (LOCKED)

Concurrent session limits are enforced via Redis atomic counters:

```
crla:{tenant_id}:concurrent:active_sessions

Rules:
  - INCR on session creation (after authorization)
  - DECR on session termination (guaranteed via TTL fallback)
  - If INCR result > plan concurrent limit → DENY + ROLLBACK INCR
  - TTL fallback: max_session_duration + 300 seconds (orphan protection)
  - Counter is inspected in CRLA pre-authorization gate (Section 4.4)
```

**Concurrent limit exceeded → DENY → emit quota.concurrent_limit event → NOTIFY ADMIN**

---

## SECTION 5 — METERING ENGINE

### 5.1 Metering Principle

Every call that succeeds MUST produce a meter record. Every call that is
denied MUST also produce a meter record (with status: DENIED). No billing
event exists without a corresponding meter record.

```
METERING RULE: Meter first, then execute.
               Meter the denial, then return 429.
               No unmetered calls. No exceptions.
```

### 5.2 Meter Record Schema

```json
{
  "meter_id": "<uuid>",
  "tenant_id": "<uuid>",
  "call_class": "api_call | gd_session | dojo_session | interview_session | websocket | notification | file_upload | search | webhook | event_publish | background_job",
  "call_subtype": "<specific endpoint or action>",
  "plan_tier": "T0 | T1 | T2 | T3 | T4 | T5",
  "actor_id": "<user_id or automation_agent_id>",
  "actor_type": "human | automation_agent | system",
  "session_id": "<uuid — null if not a session call>",
  "status": "EXECUTED | DENIED | RATE_LIMITED | QUOTA_EXHAUSTED",
  "denial_reason": "<null or reason code>",
  "units_consumed": "<integer — 1 for calls, minutes for voice sessions>",
  "billable": true,
  "timestamp": "<ISO 8601>",
  "window_id": "<rolling window identifier>",
  "trace_id": "<OpenTelemetry trace ID>",
  "kong_request_id": "<X-Request-ID from Kong>"
}
```

### 5.3 Meter Storage Architecture

Meter records flow through two paths simultaneously:

**Path 1 — Real-time (Redis, for rate calculation):**
```
CRLA middleware → Redis atomic counter INCR → window state updated
```

**Path 2 — Durable (ClickHouse, for billing and analytics):**
```
CRLA middleware → Kafka topic: ecoskiller.{tenant_id}.metering.{call_class}
               → Analytics Service consumer → ClickHouse metering table
```

**ClickHouse Metering Table Schema:**

```sql
CREATE TABLE call_meters (
  meter_id        UUID,
  tenant_id       UUID NOT NULL,
  call_class      LowCardinality(String),
  call_subtype    String,
  plan_tier       LowCardinality(String),
  actor_id        UUID,
  actor_type      LowCardinality(String),
  session_id      Nullable(UUID),
  status          LowCardinality(String),
  denial_reason   Nullable(String),
  units_consumed  UInt32,
  billable        UInt8,
  event_ts        DateTime
) ENGINE = MergeTree()
PARTITION BY (tenant_id, toYYYYMM(event_ts))
ORDER BY (tenant_id, call_class, event_ts);
```

**All ClickHouse reads MUST include `WHERE tenant_id = ?`**
**All ClickHouse writes MUST include `tenant_id` — absence: STOP EXECUTION**

### 5.4 Voice Session Metering Units

Voice sessions are metered in **session-minutes** per participant:

```
billed_units = participant_count × actual_duration_minutes

Example:
  GD session: 8 participants × 45 minutes = 360 session-minutes
  Dojo match: 2 participants × 30 minutes = 60 session-minutes
  Interview:  2 participants × 60 minutes = 120 session-minutes
```

Session-minutes are written to ClickHouse on session termination via the
`gd.completed`, `match.scored`, and `interview.completed` Kafka events,
which CRLA consumes to finalize metering records.

---

## SECTION 6 — FEATURE GATING MIDDLEWARE

### 6.1 Gating Principle

Feature availability is plan-tier-gated at the middleware layer before
any service logic executes. CRLA Feature Gate is invoked by Kong as a
pre-request plugin.

### 6.2 Feature Gate Matrix (LOCKED)

| Feature | T0 FREE | T1 STARTER | T2 PROFESSIONAL | T3 ENTERPRISE |
|---|---|---|---|---|
| Voice GD sessions | ✅ (2/mo) | ✅ | ✅ | ✅ |
| Automated GD batches | ❌ | ✅ | ✅ | ✅ |
| Dojo match sessions | ✅ (5/mo) | ✅ | ✅ | ✅ |
| Interview scheduling | ❌ | ✅ | ✅ | ✅ |
| Recruiter search access | ❌ | ✅ | ✅ | ✅ |
| Analytics dashboards | ❌ | Limited | Full | Full + Export |
| AI / automation agents | ❌ | ❌ | ✅ | ✅ |
| Webhook subscriptions | ❌ | ❌ | ✅ | ✅ |
| Custom scoring rules | ❌ | ❌ | ❌ | ✅ |
| Multi-domain GD rooms | ❌ | ❌ | ✅ | ✅ |
| SLA guarantee | ❌ | ❌ | ❌ | ✅ |
| Priority support API | ❌ | ❌ | ❌ | ✅ |
| CRLA policy override | ❌ | ❌ | ❌ | ❌ (T5 only) |

**Feature gated for current plan tier → HTTP 402 + upgrade_prompt: true**
**No feature bypass exists at any tier below T5 without human declaration**

### 6.3 Feature Gate Response Schema

```json
{
  "allowed": false,
  "gate_reason": "PLAN_FEATURE_GATED",
  "feature_id": "interview_scheduling",
  "current_tier": "T1",
  "required_tier": "T2",
  "upgrade_url": "/billing/upgrade",
  "upgrade_prompt": true
}
```

---

## SECTION 7 — QUOTA EXHAUSTION HANDLING

### 7.1 Exhaustion Response Matrix

| Quota Type | Enforcement Action | Tenant Notification | Admin Alert |
|---|---|---|---|
| Monthly session quota | DENY session create | Email + In-app | ✅ |
| Daily API quota | HTTP 429 + Retry-After | In-app banner | ✅ |
| Per-minute rate limit | HTTP 429 | None (transient) | ❌ |
| Per-second burst limit | HTTP 429 | None (transient) | ❌ |
| Concurrent session limit | DENY + 503 response | In-app + Email | ✅ |
| Participant count limit | DENY join token issue | In-app | ❌ |
| Storage quota | DENY upload | In-app + Email | ✅ |
| Notification quota | Queue with delay | In-app warning | ❌ |

### 7.2 Quota Exhaustion Event Schema (Kafka)

```json
{
  "event_type": "quota.exhausted",
  "tenant_id": "<uuid>",
  "quota_type": "monthly_gd_sessions | daily_api_calls | concurrent_sessions | storage_bytes",
  "plan_tier": "T1",
  "quota_limit": 20,
  "quota_used": 20,
  "exhausted_at": "<ISO 8601>",
  "renewal_at": "<ISO 8601 — next quota reset>",
  "upgrade_eligible": true
}
```

Event consumed by:
- Notification Service → send email + push to tenant admin
- Billing Service → log exhaustion event, flag for upsell workflow
- Admin Governance Service → visibility for platform admins

### 7.3 Soft Warning Thresholds

CRLA emits `quota.warning` events at the following thresholds:

| Threshold | Action |
|---|---|
| 70% of monthly quota consumed | In-app warning banner |
| 85% of monthly quota consumed | Email to tenant admin |
| 95% of monthly quota consumed | Email + push + in-app urgent banner |
| 100% (exhaustion) | DENY + full notification cascade |

---

## SECTION 8 — AUTOMATION AGENT CALL GOVERNANCE

### 8.1 Non-Human Actor Detection

All API calls must carry an actor type claim in the JWT:

```json
{
  "actor_type": "human | automation_agent",
  "agent_id": "<uuid — required if automation_agent>",
  "agent_name": "<string>",
  "tenant_id": "<uuid>"
}
```

**Automation agents are:**
- Permitted only on T2, T3, T5 plan tiers (Feature Gate — Section 6.2)
- Subject to stricter rate limits (dedicated `ai_agent_calls` counter)
- Required to carry `agent_id` — missing agent_id on automation actor: DENY
- Metered separately from human calls for billing transparency
- Subject to anomaly detection (Section 9.3)

### 8.2 Automation Agent Rate Limits (Per Agent Per Tenant)

| Call Type | Per Second | Per Minute | Per Day |
|---|---|---|---|
| REST API calls | 10 rps | 200 rpm | 5,000 rpd |
| Search queries | 5 rps | 100 rpm | 2,000 rpd |
| Session creates | 1 rps | 5 rpm | 50 rpd |
| Webhook triggers | 2 rps | 20 rpm | 500 rpd |

**Machine-speed call burst without agent_id → DENY + ALERT + AUDIT**
**Automation agent on T0/T1 plan → DENY + emit plan.violation event**

---

## SECTION 9 — ABUSE & ANOMALY DETECTION

### 9.1 Detection Triggers

CRLA monitors for the following abuse patterns:

| Pattern | Detection Method | Response |
|---|---|---|
| IP-level burst | Kong IP rate limit (500/min) | Block IP + SIEM alert |
| Credential stuffing | Auth failure rate > 10/min per IP | Adaptive CAPTCHA + block |
| Quota cycling | Monthly quota exhausted within 24h of reset | Flag for human review |
| Session flooding | >3x normal concurrent session attempts | DENY + alert |
| Token replay on CRLA auth tokens | Redis jti check | DENY + AUDIT + ALERT |
| Agent impersonation | human actor_type but machine call pattern | FLAG + ALERT |
| Cross-tenant quota probing | Calls to other tenants' quota API | DENY + AUDIT + SIEM |
| Webhook flood | Outbound webhook failures > 50% rate | Circuit break + alert |

### 9.2 Progressive Rate Unlocking

Trust score (from Ecoskiller reputation system) modulates rate limits:

```
trust_score >= 90  →  +20% rate limit bonus on all buckets
trust_score 70-89  →  Standard plan limits
trust_score 50-69  →  -20% rate limit penalty
trust_score < 50   →  Shadow-limiting activated (calls succeed but
                       responses are delayed 2-5 seconds to discourage abuse)
trust_score < 20   →  DENY ALL non-read calls → Force human review
```

Trust score is fetched from the Reputation Service at CRLA startup and
cached in Redis with 5-minute TTL per tenant.

### 9.3 Circuit Breaker (Envoy — Inter-Service)

CRLA configures Envoy circuit breakers to protect internal services from
cascade overload:

```yaml
circuit_breakers:
  thresholds:
    - priority: DEFAULT
      max_connections: 1000
      max_pending_requests: 500
      max_requests: 1000
      max_retries: 3
      track_remaining: true
```

When a circuit trips:
- Downstream service returns HTTP 503
- CRLA logs `service.circuit_open` event
- Grafana alert fires within 30 seconds
- Circuit re-evaluation after 30-second cooldown

---

## SECTION 10 — BILLING INTEGRATION

### 10.1 Billing Service Contract

CRLA is the **sole source of truth** for usage data fed to the Billing &
Subscription Service. No billing record may be generated without a
corresponding CRLA meter record.

```
CRLA → Kafka: ecoskiller.{tenant_id}.metering.* (all call classes)
     → Billing Service consumer
     → Billing Ledger (PostgreSQL, double-entry)
     → Invoice Generator (Airflow-scheduled, monthly)
```

### 10.2 Billable Event Types

| Event | Billing Unit | Billable From Tier |
|---|---|---|
| GD session-minutes | Per session-minute per participant | T1+ |
| Dojo session-minutes | Per session-minute per participant | T0+ (T0 free allowance) |
| Interview session-minutes | Per session-minute per participant | T1+ |
| Overage API calls | Per 1,000 calls above plan quota | T1+ |
| Notification overages | Per 100 notifications above plan | T1+ |
| Storage overages | Per GB above plan allocation | T1+ |
| AI agent calls | Per 100 calls above plan quota | T2+ |
| Webhook deliveries | Per 1,000 deliveries above plan | T2+ |

### 10.3 Overage Handling Protocol

When a tenant exceeds plan quota but has overage billing enabled:

```
1. CRLA detects quota threshold crossed
2. CRLA checks overage_enabled flag in Billing Service (cached in Redis, 60s TTL)
3. If overage_enabled = true:
   → Allow call to proceed
   → Meter with status: EXECUTED_OVERAGE
   → Billing Service accrues overage charge
   → In-app overage notification sent
4. If overage_enabled = false:
   → DENY call
   → Emit quota.exhausted event
   → Return HTTP 402 with upgrade_prompt
```

### 10.4 Invoice Audit Trail

Every invoice generated by the Billing Service must reference:
- CRLA meter record IDs for all billed events
- ClickHouse query ID proving data source
- Tenant_id binding on every line item
- Plan tier in effect during billing period
- Any overage multiplier applied

**Invoice without CRLA meter references → INVALID → STOP INVOICE GENERATION**

---

## SECTION 11 — AGENT OPERATIONAL LIFECYCLE

### 11.1 Call Intake (Every Inbound Call)

```
TRIGGER: Any API, WebSocket, or session initiation call arrives at Kong

CRLA Actions:
  1. Extract tenant_id from JWT (validated by Keycloak / Auth Service)
  2. Extract actor_type and actor_id
  3. Fetch plan tier from CRLA Policy Cache (Redis DB 0, TTL 60s)
  4. Evaluate Feature Gate (Section 6) — deny if feature not in plan
  5. Evaluate real-time rate limit (Redis sliding window ZADD/ZRANGEBYSCORE)
  6. If rate limit breached → HTTP 429 + Retry-After + meter record (RATE_LIMITED)
  7. If feature gated → HTTP 402 + upgrade_prompt + meter record (DENIED)
  8. If allowed → write meter record (EXECUTED) → pass to service
  9. Update Redis counter (atomic INCR)
  10. Publish meter event to Kafka
```

### 11.2 Voice Session Pre-Authorization

```
TRIGGER: GD Orchestrator / Dojo Engine / Interview Service requests session create

CRLA Actions:
  1. Validate request carries authenticated tenant_id
  2. Check monthly session quota (Redis monthly counter)
  3. Check concurrent session counter (Redis atomic)
  4. Check participant count vs plan limit
  5. Check requested duration vs plan maximum
  6. If all pass → issue single-use authorization token (60s TTL, stored in Redis)
  7. Write pre-auth meter record
  8. Return authorization decision with quota snapshot
  9. On session completion → DECR concurrent counter + write final meter record
```

### 11.3 Quota Reset Cycle (Airflow-Scheduled)

```
TRIGGER: Apache Airflow daily and monthly DAGs

Daily Reset (midnight UTC):
  1. Identify all tenants with daily quota counters
  2. Archive previous day counters to ClickHouse
  3. DELETE Redis daily counter keys for all tenants
  4. Write reset audit record
  5. Publish quota.reset event on Kafka

Monthly Reset (1st of each month, midnight UTC):
  1. Identify all tenants with monthly session quotas
  2. Archive previous month counters to ClickHouse
  3. DELETE Redis monthly counter keys for all tenants
  4. Write reset audit record to PostgreSQL
  5. Trigger Invoice Generator for billing period
  6. Publish quota.reset.monthly event on Kafka
```

### 11.4 Failure Handling

```
Failure Type                        → CRLA Response
─────────────────────────────────── → ──────────────────────────────────────────
Redis unavailable                   → FAIL OPEN with emergency 10 rpm cap for
                                      all tenants + CRITICAL alert
Kafka unavailable                   → Buffer meter records in PostgreSQL
                                      fallback table + ALERT
ClickHouse unavailable              → Buffer via Kafka + ALERT (no billing impact)
Plan cache stale > 5 min            → Re-fetch from Billing Service directly
                                      + ALERT
Authorization token missing on      → DENY session create + ALERT + AUDIT
voice session create
Concurrent counter orphan           → TTL fallback clears after max session
                                      duration + 300s
Meter write failure                 → Retry 3x → DLQ → ALERT → human review
Billing Service unreachable         → Queue meter events + retry + ALERT
```

---

## SECTION 12 — AUDIT & OBSERVABILITY

### 12.1 Immutable Audit Log Schema

Every CRLA enforcement decision produces an immutable audit record:

```json
{
  "audit_id": "<uuid>",
  "agent": "CALL_RATE_LIMIT_AGENT",
  "tenant_id": "<uuid>",
  "actor_id": "<uuid>",
  "actor_type": "human | automation_agent | system",
  "call_class": "<call type>",
  "call_subtype": "<endpoint or action>",
  "decision": "ALLOWED | DENIED | RATE_LIMITED | QUOTA_EXHAUSTED | FEATURE_GATED",
  "denial_reason": "<null or code>",
  "plan_tier": "<T0-T5>",
  "quota_snapshot": {
    "monthly_used": 18,
    "monthly_limit": 20,
    "concurrent_active": 3,
    "concurrent_limit": 20
  },
  "rate_window_counters": {
    "per_second": 47,
    "per_minute": 430
  },
  "timestamp": "<ISO 8601>",
  "trace_id": "<OpenTelemetry trace ID>",
  "kong_request_id": "<string>"
}
```

### 12.2 Wazuh SIEM Integration

CRLA must emit structured security events to Wazuh for:

- IP-level burst detection (>500 calls/minute from single IP)
- Automation agent on restricted tier attempt
- Quota cycling anomaly
- Cross-tenant quota probe attempt
- Rate limit override attempt
- Redis key namespace violation

**Alert severity:**
- `CRITICAL` — Cross-tenant probe, auth bypass attempt
- `HIGH` — Automation agent on restricted tier, quota cycling
- `MEDIUM` — Sustained rate limit breach (>5 minutes)
- `LOW` — Single quota exhaustion event

### 12.3 Prometheus Metrics (Mandatory)

```
crla_calls_total{tenant_id, call_class, status}
crla_calls_rate_limited_total{tenant_id, call_class}
crla_quota_exhausted_total{tenant_id, quota_type}
crla_sessions_authorized_total{tenant_id, session_type}
crla_sessions_denied_total{tenant_id, denial_reason}
crla_concurrent_sessions_active{tenant_id}
crla_overage_events_total{tenant_id, call_class}
crla_redis_counter_ops_total{operation, status}
crla_meter_write_latency_seconds{call_class}
crla_kafka_publish_latency_seconds{topic}
crla_feature_gate_denials_total{tenant_id, feature_id, plan_tier}
```

### 12.4 Grafana Dashboards (Mandatory)

The following dashboards must exist for CRLA:

- Call volume by tenant and call class (real-time)
- Rate limit breach rate by tenant and window type
- Quota utilization per tenant (monthly burn-down)
- Concurrent session heatmap by tenant
- Overage billing accrual by tenant
- Feature gate denial funnel (by feature, by plan tier)
- Automation agent call volume vs plan allocation
- Redis counter health (ops/sec, latency)
- Kafka meter event lag (consumer lag per topic)
- Top tenants by call volume (ranking table)

---

## SECTION 13 — DEPLOYMENT REQUIREMENTS

### 13.1 Kubernetes Namespace

CRLA runs in the `billing` namespace as a sidecar policy engine, with Kong
plugin integration from the `core` namespace.

```yaml
namespace: billing
labels:
  app: crla
  tier: billing-quota
  criticality: critical
```

### 13.2 Resource Limits (Minimum)

```yaml
resources:
  requests:
    cpu: "500m"
    memory: "512Mi"
  limits:
    cpu: "2"
    memory: "1Gi"
```

### 13.3 Redis Dedicated Instance

CRLA requires a **dedicated Redis instance** (not shared with TAOIA or
general application cache):

```yaml
redis:
  host: redis-crla
  port: 6379
  database:
    0: plan_policy_cache
    1: rate_limit_counters
    2: session_authorization_tokens
    3: jti_replay_cache
  maxmemory-policy: noeviction   # CRITICAL — counters must never be evicted
```

**`noeviction` is mandatory** — a rate-limit counter evicted under memory
pressure creates a quota bypass. This must be enforced at Redis config level.

### 13.4 Health Probes

Readiness must verify:
- Redis CRLA instance reachable and `noeviction` policy confirmed
- Kafka producer connection healthy
- ClickHouse write connection healthy
- Billing Service plan cache populated
- Kong plugin registration confirmed

---

## SECTION 14 — INTEGRATION CONTRACTS

### 14.1 Auth Service
Must inject `tenant_id`, `actor_type`, `plan_tier` into every JWT.
**Missing claims → CRLA denies all calls for that token**

### 14.2 Kong API Gateway
Must load CRLA plan policies on every request from Redis DB 0.
Must enforce `rate-limiting-advanced` plugin with CRLA namespace.
**Kong bypass attempt → SIEM ALERT + AUDIT**

### 14.3 Voice GD Orchestrator / Dojo Engine / Interview Service
Must call CRLA pre-authorization API before every session creation.
Must pass authorization token in `X-CRLA-Auth` header.
**Session without authorization token → STOP SESSION**

### 14.4 Billing & Subscription Service
Is the **sole consumer** of plan tier definitions.
CRLA fetches plan metadata from Billing Service, not from its own config.
Plan changes in Billing Service propagate to CRLA within 60 seconds.

### 14.5 Analytics Service / ClickHouse
Consumes CRLA meter events from Kafka.
Writes to `call_meters` ClickHouse table.
**Unscoped ClickHouse query → DENY**

### 14.6 Apache Airflow
Owns quota reset DAGs (daily and monthly).
Must alert on DAG failure — quota not reset = tenants blocked incorrectly.

### 14.7 Open Policy Agent (OPA)
CRLA delegates complex authorization decisions to OPA:
- "Can T1 tenant enable overage billing?"
- "Can this automation agent call this endpoint?"
- "Can this tenant's trust score unlock extended limits?"

---

## SECTION 15 — FINAL ENFORCEMENT DECLARATION

```
┌──────────────────────────────────────────────────────────────────┐
│  CALL_RATE_LIMIT_AGENT — FINAL LAW                               │
│                                                                  │
│  1. No call executes without a meter record                      │
│  2. No session is created without CRLA pre-authorization         │
│  3. No quota is enforced without a tenant_id binding             │
│  4. No feature is accessible above plan tier                     │
│  5. No Redis counter exists without crla:{tenant_id}: prefix     │
│  6. No billing record is generated without a CRLA meter source   │
│  7. No automation agent calls on restricted plan tiers           │
│  8. No overage bill exists without explicit overage_enabled flag │
│  9. Every rate limit breach produces an audit record             │
│  10. Every quota exhaustion triggers a tenant notification       │
│  11. Every abuse pattern triggers a SIEM alert                   │
│  12. Every quota reset is scheduled, logged, and auditable       │
│                                                                  │
│  Violation of any rule above:                                    │
│  → DENY CALL                                                     │
│  → WRITE METER RECORD (even for denied calls)                    │
│  → WRITE AUDIT LOG                                               │
│  → FIRE SIEM ALERT (on abuse or bypass attempt)                  │
│  → REPORT TO BILLING SERVICE                                     │
│  → REPORT TO ADMIN GOVERNANCE SERVICE                            │
│                                                                  │
│  No exceptions. No overrides. No workarounds.                    │
│  T5 CUSTOM tier overrides require human-signed contract only.    │
└──────────────────────────────────────────────────────────────────┘
```

---

## SECTION 16 — VERSION CONTROL

| Version | Date | Change | Authority |
|---|---|---|---|
| v1.0 | 2026-03-04 | Initial sealed specification | Human declaration |

**Next version: v1.1 — Add-only. No mutations to existing clauses.**

---

```
SEAL: CALL_RATE_LIMIT_AGENT · BILL-QUOTA-CRLA-001 · v1.0
CLASSIFICATION: ANTIGRAVITY — SEALED PROMPT ARTIFACT
STATUS: FINAL · LOCKED · GOVERNED · NON-NEGOTIABLE
```
