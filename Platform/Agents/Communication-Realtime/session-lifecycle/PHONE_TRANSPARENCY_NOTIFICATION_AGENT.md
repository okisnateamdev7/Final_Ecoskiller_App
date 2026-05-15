# PHONE_TRANSPARENCY_NOTIFICATION_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### ECOSKILLER — ANTIGRAVITY LAYER · LOCKED & SEALED SPECIFICATION v1.0

---

```
Artifact Class:       Production System Blueprint
Domain:               ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
Codename:             ANTIGRAVITY
Mutation Policy:      Add-only via version bump
Interpretation Authority: NONE
Execution Authority:  Human declaration only
Status:               FINAL · LOCKED · GOVERNED · DETERMINISTIC
Determinism Rule:     Identical input → Identical output
Failure Mode:         STOP → REPORT → NO PARTIAL OUTPUT
```

---

## SECTION I — PURPOSE AND ABSOLUTE SCOPE

### 1.1 What This Agent Is

The `PHONE_TRANSPARENCY_NOTIFICATION_AGENT` (PTNA) is a **dedicated enterprise trust microservice** within the Ecoskiller Antigravity Layer.

It governs, audits, rate-controls, and publicly accounts for **every phone-channel communication** dispatched by the platform — including SMS, OTP, push notifications, automated voice alerts, and missed call triggers — across all roles:

- Students
- Parents / Guardians
- Recruiters
- Mentors
- Admins
- Platform-level system actors

### 1.2 What This Agent Is NOT

| NOT This | Reason |
|---|---|
| NOT a notification sender | Sending is owned by `Notification Service` |
| NOT an SMS gateway | Transport is owned by `Jasmin SMS Gateway` |
| NOT a push delivery engine | Push delivery is owned by `ntfy` |
| NOT an AI classifier | Zero inference, zero learning, zero interpretation |
| NOT a user-facing chat agent | No dialogue, no conversation |

### 1.3 Why Antigravity

Antigravity is the **trust layer that counteracts platform gravity** — the natural tendency of large SaaS platforms to over-communicate, under-report, and under-justify their communication behaviour to end users.

PTNA is the core enforcement engine of Antigravity.

---

## SECTION II — SYSTEM POSITION

### 2.1 Architecture Layer

```
┌─────────────────────────────────────────────────────────────┐
│                  ECOSKILLER PLATFORM                        │
│                                                             │
│  Auth · User · Job · GD Orchestrator · Dojo · Billing       │
│                       │                                     │
│              [Notification Service]                         │
│                       │                                     │
│         ┌─────────────▼──────────────┐                      │
│         │  PHONE_TRANSPARENCY_       │  ◄── ANTIGRAVITY     │
│         │  NOTIFICATION_AGENT (PTNA) │      TRUST LAYER     │
│         └──────┬──────────────┬──────┘                      │
│                │              │                             │
│         [Jasmin SMS]       [ntfy Push]                      │
│         [coturn Voice]     [Postfix]                        │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Traffic Position

PTNA sits **between** the `Notification Service` and **all phone-channel delivery transports**.

- Every outbound phone communication MUST pass through PTNA intercept.
- PTNA does not block — it records, validates, rates, and publishes.
- PTNA emits decisions to Kafka before forwarding to transports.

### 2.3 Dependency Map

| Dependency | Role | Direction |
|---|---|---|
| Kafka / RabbitMQ | Event bus for notification events | Consumes + Publishes |
| Redis | Rate state machine, OTP counters, delivery locks | Read/Write |
| PostgreSQL | Permanent audit log, consent records, delivery history | Write (append-only) |
| ClickHouse | Aggregated analytics, delivery metrics, anomaly data | Write |
| Keycloak | Identity resolution for user lookup | Read |
| Jasmin SMS Gateway | SMS / OTP transport | Downstream |
| ntfy | Push notification transport | Downstream |
| Open Policy Agent | Policy-as-code authorization for notification dispatch | Read |
| Wazuh | SIEM feed for abuse pattern detection | Publish |

---

## SECTION III — CORE DESIGN PHILOSOPHY

### 3.1 The Three Laws of PTNA

```
LAW 1 — EVERY PHONE COMMUNICATION IS A LEDGER ENTRY.
         No message leaves the platform without an immutable record.

LAW 2 — EVERY USER HAS THE RIGHT TO KNOW WHY THEY WERE CONTACTED.
         Every notification must carry a justification_code, trigger_source,
         and human-readable reason. No silent messages.

LAW 3 — ENTERPRISE RATE DISCIPLINE IS NON-NEGOTIABLE.
         No role, no urgency claim, no admin override bypasses
         the rate enforcement state machine.
```

### 3.2 Trust Primitives

| Primitive | Description |
|---|---|
| `justification_code` | Machine-readable reason for the notification |
| `trigger_source` | Which service / event caused this notification |
| `consent_ref` | Reference to user's active consent record |
| `policy_ref` | OPA policy rule that authorized dispatch |
| `delivery_token` | Unique, signed, single-use dispatch token |
| `transparency_uri` | Per-user URL showing full notification history |

---

## SECTION IV — FUNCTIONAL SPECIFICATION

### 4.1 Core Functions (NON-NEGOTIABLE)

#### F-01 — Notification Intercept Gateway

Every notification request from `Notification Service` must arrive at PTNA via a signed internal POST to `/api/v1/ptna/dispatch-request`.

PTNA performs the following in strict sequence:

```
1. Validate dispatch token (signed JWT from Notification Service)
2. Resolve user identity via Keycloak
3. Verify active consent record for channel (SMS / Push / Voice)
4. Evaluate rate limit state from Redis
5. Evaluate OPA policy: is this notification type authorized for this role?
6. Assign justification_code + trigger_source from event metadata
7. Write ledger entry to PostgreSQL (status = PENDING)
8. Emit notification.intercepted event to Kafka
9. Forward to transport (Jasmin / ntfy) OR hold with RATE_HELD status
10. Update ledger entry (status = DISPATCHED / HELD / FAILED)
11. Emit notification.dispatched / notification.held / notification.failed to Kafka
```

**FAILURE AT ANY STEP = STOP. LOG. DO NOT DISPATCH.**

---

#### F-02 — Immutable Notification Ledger

Every dispatched, held, or failed notification produces one immutable ledger record.

**Schema: `notification_ledger`**

```sql
CREATE TABLE notification_ledger (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id             UUID NOT NULL,
    tenant_id           UUID NOT NULL,
    role                VARCHAR(32) NOT NULL,           -- student | recruiter | mentor | parent | admin
    channel             VARCHAR(16) NOT NULL,           -- SMS | PUSH | VOICE | OTP
    notification_type   VARCHAR(64) NOT NULL,           -- OTP_LOGIN | GD_REMINDER | BELT_AWARD | etc.
    trigger_service     VARCHAR(64) NOT NULL,           -- gd_orchestrator | billing | dojo | auth | etc.
    trigger_event       VARCHAR(128) NOT NULL,          -- Kafka event name that caused this
    justification_code  VARCHAR(64) NOT NULL,           -- machine-readable reason
    justification_text  TEXT NOT NULL,                  -- human-readable reason (EN)
    consent_ref         UUID,                           -- FK to consent_records
    policy_ref          VARCHAR(128) NOT NULL,          -- OPA rule path
    delivery_token      VARCHAR(256) NOT NULL UNIQUE,
    recipient_phone     VARCHAR(20),                    -- E.164 format, encrypted at rest
    recipient_push_id   VARCHAR(256),                   -- encrypted at rest
    status              VARCHAR(16) NOT NULL,           -- PENDING | DISPATCHED | HELD | FAILED | EXPIRED
    hold_reason         VARCHAR(128),                   -- if HELD: rate_limit | consent_missing | policy_denied
    dispatch_attempted_at TIMESTAMPTZ,
    dispatch_confirmed_at TIMESTAMPTZ,
    gateway_response    JSONB,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    is_immutable        BOOLEAN NOT NULL DEFAULT TRUE   -- UPDATE forbidden after DISPATCHED
);
```

**Enforcement rules:**
- No DELETE on this table. Ever.
- No UPDATE after `status = DISPATCHED`.
- Row-level security enforced by tenant_id.
- PII columns (phone, push_id) encrypted at rest using Vault-managed keys.

---

#### F-03 — Rate Enforcement State Machine

Rate limits are enforced deterministically via Redis. No exceptions. No admin bypass.

**Rate Limit Policy Table (v1.0 — Locked)**

| Channel | Role | Window | Max Dispatches | Cooldown on Breach |
|---|---|---|---|---|
| SMS / OTP | Student | 15 minutes | 3 | 30 minutes hard block |
| SMS / OTP | Student | 24 hours | 10 | 12 hours hard block |
| SMS / OTP | Recruiter | 24 hours | 20 | 4 hours hard block |
| SMS / OTP | Parent | 24 hours | 5 | 24 hours hard block |
| Push Notification | Student | 1 hour | 6 | 1 hour hard block |
| Push Notification | Student | 24 hours | 20 | 6 hours hard block |
| Push Notification | Recruiter | 24 hours | 30 | 2 hours hard block |
| Platform Broadcast | Admin | 1 hour | 1 | 2 hours hard block |
| Voice Alert | Any | 24 hours | 2 | 48 hours hard block |
| GD Reminder | Student | Per GD session | 3 | No more for session |

**Redis Key Pattern:**
```
ptna:rate:{tenant_id}:{user_id}:{channel}:{window}  → counter (TTL = window duration)
ptna:cooldown:{tenant_id}:{user_id}:{channel}       → lock (TTL = cooldown duration)
```

**State Machine Transitions:**
```
IDLE → CHECK_COUNTER → [UNDER_LIMIT] → DISPATCH
                    → [AT_LIMIT]    → HOLD → LOG_RATE_HELD → EMIT_HELD_EVENT
                    → [COOLDOWN]    → REJECT → LOG_COOLDOWN_BLOCKED → EMIT_BLOCKED_EVENT
```

---

#### F-04 — Consent Verification Engine

Before dispatching any phone communication, PTNA validates active consent.

**Consent Record Schema: `consent_records`**

```sql
CREATE TABLE consent_records (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL,
    tenant_id       UUID NOT NULL,
    channel         VARCHAR(16) NOT NULL,           -- SMS | PUSH | VOICE | OTP
    consent_type    VARCHAR(32) NOT NULL,           -- explicit | implied | guardian_delegated
    granted_at      TIMESTAMPTZ NOT NULL,
    revoked_at      TIMESTAMPTZ,
    revoked_by      UUID,
    legal_basis     VARCHAR(64) NOT NULL,           -- DPDP_2023 | GDPR | TRAI_DND | COPPA
    parent_user_id  UUID,                           -- if minor: guardian who granted
    version         INTEGER NOT NULL DEFAULT 1,
    is_active       BOOLEAN GENERATED ALWAYS AS (revoked_at IS NULL) STORED
);
```

**Consent Resolution Logic:**
```
IF user is minor (age < 18 from profile):
    REQUIRE parent_user_id consent record
    IF parent_user_id consent missing → HOLD with CONSENT_GUARDIAN_MISSING
    IF parent_user_id consent revoked → HOLD with CONSENT_GUARDIAN_REVOKED

IF user is adult:
    IF no active consent record for channel → HOLD with CONSENT_MISSING
    IF consent revoked → HOLD with CONSENT_REVOKED

IF channel = OTP (authentication critical):
    BYPASS consent check (OTP is security-critical, TRAI-exempt)
    LOG bypass_reason = OTP_SECURITY_EXEMPT
    AUDIT trail required
```

---

#### F-05 — User Transparency Portal

Every user gets a dedicated, authenticated URL:

```
GET /api/v1/ptna/transparency/{user_id}
```

Returns:

```json
{
  "user_id": "uuid",
  "total_notifications_sent": 42,
  "last_30_days": {
    "SMS": 8,
    "PUSH": 23,
    "OTP": 11
  },
  "recent_notifications": [
    {
      "id": "uuid",
      "sent_at": "2026-03-04T10:15:00Z",
      "channel": "SMS",
      "type": "GD_REMINDER",
      "reason": "You have a Group Discussion session starting in 30 minutes for batch GD_BANKING_20260304_1234",
      "trigger": "gd_orchestrator",
      "status": "DISPATCHED"
    }
  ],
  "held_notifications": [...],
  "rate_status": {
    "SMS": { "used_today": 3, "limit_today": 10, "cooldown_active": false },
    "PUSH": { "used_today": 7, "limit_today": 20, "cooldown_active": false }
  },
  "consent_status": {
    "SMS": "active",
    "PUSH": "active",
    "VOICE": "revoked"
  }
}
```

**Transparency Portal Rules:**
- Available to authenticated user only (JWT required, Keycloak validated)
- For minors: also visible to guardian account
- For recruiters: also visible to tenant admin
- Data retention: 24 months rolling window
- No sensitive content stored (notification body is hashed, justification_text preserved)

---

#### F-06 — Parent / Guardian Phone Transparency

For users identified as minors (age < 18 in user profile):

1. All phone communications require guardian consent ref.
2. Guardian receives a **mirror notification** (push only) informing that their ward received a phone message. SMS mirror: optional, consent-gated.
3. Guardian has full read access to ward's notification transparency portal.
4. Guardian can revoke any channel consent — takes effect within 60 seconds (Redis TTL-based).
5. Guardian revocation emits `consent.guardian_revoked` event to Kafka.
6. All services consuming `consent.guardian_revoked` must halt further dispatch immediately.

**Integration with Royalty & Kids System (Ecoskiller XIII):**
- When a child innovator (kid in royalty system) is notified about licensing events, royalty payouts, or contract changes — PTNA enforces guardian mirror with `justification_code = ROYALTY_EVENT_CHILD`.
- These notifications are escalated to high-priority audit tier automatically.

---

#### F-07 — OPA Policy Integration

PTNA queries Open Policy Agent before every dispatch.

**Policy Input:**
```json
{
  "user_id": "uuid",
  "role": "student",
  "channel": "SMS",
  "notification_type": "GD_REMINDER",
  "trigger_service": "gd_orchestrator",
  "tenant_id": "uuid",
  "user_plan": "free",
  "time_utc": "2026-03-04T10:00:00Z"
}
```

**Policy Output:**
```json
{
  "allow": true,
  "policy_ref": "ecoskiller/ptna/notification/student_sms_gd",
  "reason": "Student SMS for GD session is authorized under free plan"
}
```

**If `allow = false`:** Notification is POLICY_DENIED. Ledger entry written. No dispatch. Event emitted.

**Locked policy categories:**
```
ecoskiller/ptna/notification/student_sms_*
ecoskiller/ptna/notification/student_push_*
ecoskiller/ptna/notification/recruiter_sms_*
ecoskiller/ptna/notification/recruiter_push_*
ecoskiller/ptna/notification/parent_*
ecoskiller/ptna/notification/admin_broadcast_*
ecoskiller/ptna/notification/otp_security_exempt
ecoskiller/ptna/notification/royalty_child_escalated
```

---

#### F-08 — Abuse Detection & SIEM Integration

PTNA monitors for anomalous phone notification patterns and reports to Wazuh.

**Abuse Signals:**

| Signal | Threshold | Action |
|---|---|---|
| OTP flood — same user | 5 OTPs in 10 minutes | Auto-lock user + Wazuh alert |
| SMS flood — same tenant | 500 SMS in 1 hour | Tenant rate-hold + Wazuh critical |
| Bulk broadcast without OPA pass | 1 attempt | Immediate Wazuh critical + STOP |
| Repeated failed delivery (same number) | 10 failures / hour | Quarantine number + log |
| Consent-revoked user still receiving | 1 occurrence | Wazuh critical + immediate investigation flag |
| Unknown trigger_service in dispatch request | Any | Reject + Wazuh alert |

**Wazuh Event Schema:**
```json
{
  "source": "PTNA",
  "event_type": "abuse_signal",
  "severity": "critical | high | medium",
  "signal_code": "OTP_FLOOD | SMS_FLOOD | CONSENT_BREACH | UNKNOWN_TRIGGER",
  "tenant_id": "uuid",
  "user_id": "uuid",
  "detail": "...",
  "timestamp": "ISO8601"
}
```

---

#### F-09 — Analytics Feed (ClickHouse)

PTNA writes aggregated metrics to ClickHouse for enterprise dashboards.

**ClickHouse Table: `ptna_metrics`**

```sql
CREATE TABLE ptna_metrics (
    date            Date,
    tenant_id       UUID,
    channel         LowCardinality(String),
    notification_type LowCardinality(String),
    trigger_service LowCardinality(String),
    status          LowCardinality(String),
    hold_reason     LowCardinality(String),
    count           UInt64,
    updated_at      DateTime
) ENGINE = SummingMergeTree()
ORDER BY (date, tenant_id, channel, notification_type, trigger_service, status);
```

**Grafana Dashboard Panels (Required):**
- Daily dispatch volume by channel and tenant
- Hold rate by channel (rate limit vs. consent vs. policy)
- OTP success / failure rate
- GD notification delivery success rate (tied to Voice GD Orchestrator)
- Parent guardian mirror delivery rate
- Abuse signal frequency by tenant
- Consent revocation trend (daily, 30-day rolling)

---

#### F-10 — Notification Type Registry (Locked)

All valid `notification_type` values must be pre-registered. Unknown types are rejected.

**Registry: `notification_type_registry`**

```sql
CREATE TABLE notification_type_registry (
    type_code           VARCHAR(64) PRIMARY KEY,
    description         TEXT NOT NULL,
    allowed_channels    VARCHAR[] NOT NULL,           -- {'SMS','PUSH','VOICE','OTP'}
    requires_consent    BOOLEAN NOT NULL DEFAULT TRUE,
    otp_exempt          BOOLEAN NOT NULL DEFAULT FALSE,
    allowed_roles       VARCHAR[] NOT NULL,
    max_per_session     INTEGER,                      -- NULL = no session limit
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deprecated_at       TIMESTAMPTZ
);
```

**Locked Notification Types (v1.0):**

| Type Code | Channels | Roles | Consent Required | Notes |
|---|---|---|---|---|
| `OTP_LOGIN` | OTP | All | No (exempt) | Auth critical |
| `OTP_RESET_PASSWORD` | OTP | All | No (exempt) | Auth critical |
| `OTP_CONSENT_GRANT` | OTP | Parent | No (exempt) | Trust bootstrap |
| `GD_SESSION_SCHEDULED` | SMS, PUSH | Student | Yes | Pre-GD entry |
| `GD_SESSION_REMINDER_30M` | SMS, PUSH | Student | Yes | T-30 min |
| `GD_SESSION_REMINDER_10M` | PUSH | Student | Yes | T-10 min |
| `GD_SESSION_STARTED` | PUSH | Student | Yes | Real-time |
| `GD_SCORE_PUBLISHED` | SMS, PUSH | Student | Yes | Post-session |
| `INTERVIEW_SLOT_BOOKED` | SMS, PUSH | Student | Yes | Slot confirmation |
| `INTERVIEW_REMINDER_24H` | SMS, PUSH | Student | Yes | Day-before |
| `INTERVIEW_REMINDER_1H` | PUSH | Student | Yes | Hour-before |
| `JOB_APPLICATION_STATUS` | PUSH | Student | Yes | Pipeline update |
| `OFFER_LETTER_ISSUED` | SMS, PUSH | Student | Yes | High-priority |
| `BELT_AWARDED` | SMS, PUSH | Student | Yes | Dojo milestone |
| `BELT_ELIGIBLE_ALERT` | PUSH | Student | Yes | Dojo nudge |
| `DOJO_MATCH_STARTING` | PUSH | Student, Mentor | Yes | Realtime |
| `ROYALTY_PAYOUT_CREDIT` | SMS, PUSH | Parent | Yes | Financial event |
| `ROYALTY_CONTRACT_CHANGE` | SMS | Parent | Yes | Legal event |
| `ROYALTY_CHILD_LICENSED` | SMS, PUSH | Parent | Yes | Child IP event |
| `BILLING_INVOICE_GENERATED` | SMS, PUSH | Recruiter | Yes | Financial |
| `BILLING_PAYMENT_FAILED` | SMS, PUSH | Recruiter | Yes | Urgent |
| `CONSENT_REVOCATION_CONFIRM` | SMS | Any | No (confirmation) | Trust loop |
| `PLATFORM_MAINTENANCE` | PUSH | All | Yes | Broadcast |
| `ADMIN_SECURITY_ALERT` | SMS, PUSH | Admin | Yes | Security |
| `GUARDIAN_WARD_ACTIVITY` | PUSH | Parent | Yes | Mirror notification |

**Adding new types:** Version bump required. PR with OPA policy addition. Registry insert via migration. No runtime injection.

---

## SECTION V — API CONTRACT

### 5.1 Endpoint Specifications

#### POST `/api/v1/ptna/dispatch-request`
Internal-only. Called by `Notification Service`.

**Request:**
```json
{
  "dispatch_token": "signed_jwt_from_notification_service",
  "user_id": "uuid",
  "tenant_id": "uuid",
  "channel": "SMS",
  "notification_type": "GD_SESSION_REMINDER_30M",
  "trigger_service": "gd_orchestrator",
  "trigger_event": "gd.session.t_minus_30",
  "trigger_ref": "session_uuid",
  "payload": {
    "recipient_phone": "+919876543210",
    "template_id": "gd_reminder_30m_en",
    "variables": { "session_id": "GD_BANKING_20260304_1234", "time": "10:30 AM" }
  }
}
```

**Response:**
```json
{
  "ledger_id": "uuid",
  "status": "DISPATCHED | HELD | FAILED | POLICY_DENIED | CONSENT_MISSING",
  "hold_reason": null,
  "transparency_uri": "/api/v1/ptna/transparency/{user_id}",
  "delivery_token": "token"
}
```

---

#### GET `/api/v1/ptna/transparency/{user_id}`
Authenticated. User, guardian (for minors), or tenant admin.

Returns full transparency record (see F-05).

---

#### GET `/api/v1/ptna/ledger/{ledger_id}`
Internal audit use. Admin + compliance roles only.

Returns single ledger record with full metadata.

---

#### POST `/api/v1/ptna/consent/revoke`
User-authenticated. Revokes channel consent immediately.

```json
{
  "user_id": "uuid",
  "channel": "SMS",
  "revoked_by": "uuid",
  "reason": "user_requested | guardian_requested | legal_hold"
}
```

---

#### GET `/api/v1/ptna/admin/dashboard`
Tenant admin. Returns aggregate metrics summary.

---

#### GET `/api/v1/ptna/health`
Public health check endpoint. Returns `{ "status": "ok" }`.

---

## SECTION VI — EVENT CONTRACTS (KAFKA)

### 6.1 Events Published by PTNA

| Event | Topic | Payload Keys |
|---|---|---|
| `notification.intercepted` | `ptna.events` | ledger_id, user_id, channel, type, status |
| `notification.dispatched` | `ptna.events` | ledger_id, user_id, channel, gateway_ref |
| `notification.held` | `ptna.events` | ledger_id, user_id, hold_reason, retry_after |
| `notification.failed` | `ptna.events` | ledger_id, user_id, failure_code |
| `notification.policy_denied` | `ptna.events` | ledger_id, user_id, policy_ref, deny_reason |
| `consent.revoked` | `ptna.consent` | user_id, channel, revoked_by, effective_at |
| `consent.guardian_revoked` | `ptna.consent` | user_id, guardian_id, channel, effective_at |
| `ptna.abuse_signal` | `ptna.security` | signal_code, user_id, tenant_id, severity |
| `ptna.rate_breach` | `ptna.security` | user_id, channel, window, count |

### 6.2 Events Consumed by PTNA

| Event | Source Service | PTNA Action |
|---|---|---|
| `user.consent_updated` | User Service | Refresh consent cache in Redis |
| `user.minor_status_changed` | User Service | Trigger guardian consent re-check |
| `gd.batch.created` | GD Orchestrator | Pre-register expected notification volume |
| `billing.invoice.generated` | Billing Service | Validate recruiter phone consent |
| `royalty.payout.triggered` | Royalty Accounting | Enforce child escalation policy |
| `auth.otp.requested` | Auth Service | Bypass consent, apply OTP rate machine |

---

## SECTION VII — FAILURE HANDLING

### 7.1 PTNA Failure Matrix

| Failure Scenario | PTNA Response | Downstream Effect |
|---|---|---|
| Redis unavailable | STOP dispatch. Log to Postgres. Emit `ptna.redis_failure`. Alert Grafana. | No notifications until Redis restored |
| PostgreSQL write failure | STOP dispatch. Retry 3x with exponential backoff. After 3 failures: emit critical alert. | Notification blocked until write succeeds |
| OPA unreachable | STOP dispatch. POLICY_UNKNOWN status. No notification exits without policy verdict. | Zero notifications until OPA restored |
| Keycloak unavailable | STOP dispatch. IDENTITY_UNRESOLVABLE status. | No notifications |
| Jasmin SMS Gateway failure | Log GATEWAY_FAILED. Update ledger. Emit `notification.failed`. No retry by PTNA (retry is Jasmin's domain). | SMS fails. Push may still proceed. |
| ntfy failure | Log PUSH_FAILED. Update ledger. Emit `notification.failed`. | Push fails. SMS may still proceed. |
| Unknown notification_type received | REJECT immediately. Log TYPE_NOT_REGISTERED. Emit `ptna.abuse_signal`. | Blocked entirely |
| dispatch_token invalid / expired | REJECT immediately. Log AUTH_FAILURE. | Blocked entirely |

### 7.2 PTNA Self-Recovery Rules

- PTNA does NOT restart failed notifications autonomously.
- Retry responsibility lies with the originating service based on `notification.failed` event.
- PTNA only enforces — it does not retry.
- All failures are observable in Grafana (Loki + Prometheus).

---

## SECTION VIII — DEPLOYMENT SPECIFICATION

### 8.1 Kubernetes Namespace

```yaml
namespace: core   # PTNA lives in core namespace alongside auth and user services
```

### 8.2 Deployment Manifest Requirements

```yaml
# Required for all environments
resources:
  requests:
    cpu: "250m"
    memory: "256Mi"
  limits:
    cpu: "1000m"
    memory: "512Mi"

replicas:
  dev: 1
  test: 1
  staging: 2
  production: 3    # minimum 3 for HA

livenessProbe:
  path: /api/v1/ptna/health
  initialDelaySeconds: 10
  periodSeconds: 15

readinessProbe:
  path: /api/v1/ptna/health
  initialDelaySeconds: 5
  periodSeconds: 10

autoscaling:
  enabled: true
  minReplicas: 3
  maxReplicas: 10
  targetCPUUtilizationPercentage: 70
```

### 8.3 Environment Variables Required

```
PTNA_REDIS_URL=
PTNA_POSTGRES_URL=
PTNA_KAFKA_BROKERS=
PTNA_OPA_URL=
PTNA_KEYCLOAK_URL=
PTNA_KEYCLOAK_REALM=
PTNA_VAULT_ADDR=
PTNA_JASMIN_INTERNAL_URL=
PTNA_NTFY_INTERNAL_URL=
PTNA_ENCRYPTION_KEY_REF=          # Vault path for PII encryption key
PTNA_DISPATCH_TOKEN_SECRET=       # Shared secret with Notification Service
PTNA_WAZUH_SIEM_URL=
PTNA_CLICKHOUSE_URL=
PTNA_ENVIRONMENT=                 # dev | test | staging | production
```

All secrets: sourced from HashiCorp Vault. Zero hardcoded credentials.

### 8.4 Implementation Stack

```
Language:     Node.js (TypeScript) or Spring Boot (Java 21)
              — matches GD Orchestrator stack for operational consistency
HTTP Server:  Fastify (Node) / Spring WebFlux (Java)
State:        Redis (ioredis / Lettuce)
DB Client:    Prisma / Drizzle (Node) or JOOQ (Java)
OPA Client:   OPA REST client with caching layer
Kafka:        kafkajs / Spring Kafka
Observability: OpenTelemetry SDK + Prometheus micrometer
```

---

## SECTION IX — OBSERVABILITY (NON-OPTIONAL)

### 9.1 Prometheus Metrics

```
ptna_dispatch_total{channel, type, status, tenant}          Counter
ptna_dispatch_duration_seconds{channel}                     Histogram
ptna_rate_holds_total{channel, window, tenant}              Counter
ptna_consent_check_duration_seconds                         Histogram
ptna_opa_evaluation_duration_seconds                        Histogram
ptna_redis_latency_seconds                                  Histogram
ptna_abuse_signals_total{signal_code, severity}             Counter
ptna_ledger_write_duration_seconds                          Histogram
ptna_active_cooldowns{channel, tenant}                      Gauge
```

### 9.2 Required Grafana Alerts

| Alert | Condition | Severity |
|---|---|---|
| PTNA Dispatch Rate Drop | dispatch_total drops >50% vs 5min average | Critical |
| Redis Unavailable | Redis connection failure for >30s | Critical |
| OPA Unavailable | OPA policy check failure for >30s | Critical |
| Abuse Signal Spike | abuse_signals_total > 5 in 1 minute | High |
| Consent Breach Detected | consent.revoked user receiving notifications | Critical |
| OTP Flood Active | OTP_FLOOD signal fired | High |
| Hold Rate > 30% | rate_holds/dispatch > 0.30 for 5 minutes | Warning |

### 9.3 Loki Log Labels

```
{service="ptna", env="production", tenant_id="...", channel="...", notification_type="..."}
```

Structured JSON logs only. No unstructured strings.

---

## SECTION X — SECURITY BASELINE

### 10.1 Security Controls

| Control | Implementation |
|---|---|
| PII Encryption at Rest | Phone numbers + push IDs encrypted via Vault-managed AES-256 key |
| Internal Auth | Mutual TLS between PTNA and Notification Service |
| dispatch_token Validation | HMAC-SHA256 signed JWT, 60-second expiry |
| Rate limiting on API endpoints | Kong rate limit plugin (external) + Redis (internal) |
| RBAC on transparency endpoints | Keycloak JWT validation + role check |
| Audit log immutability | PostgreSQL row-level trigger: UPDATE/DELETE blocked after DISPATCHED |
| Consent revocation TTL | Redis key: immediate effect within 60 seconds |
| SIEM Forwarding | All abuse signals and security events to Wazuh |

### 10.2 Data Residency

- All `notification_ledger` data is tenant-isolated via `tenant_id` column.
- Row-level security enabled in PostgreSQL.
- Cross-tenant queries are impossible by design.

---

## SECTION XI — LEGAL + COMPLIANCE BINDINGS

### 11.1 Regulatory Compliance

| Regulation | PTNA Implementation |
|---|---|
| India DPDP Act 2023 | Consent capture before phone contact. Revocation within 60s. Audit log 24-month retention. |
| TRAI DND Regulations | OTP classified as transactional (exempt). Promotional SMS require consent_type = explicit. |
| GDPR (EU users) | Consent basis recorded. Right-to-erasure: phone number erased from ledger (hash retained). |
| COPPA / Minor Protection | Guardian consent mandatory for users < 18. Mirror notifications to guardian. |
| Indian IT Act 2000 | Audit logs preserved for 180 days minimum (PTNA: 24 months). |

### 11.2 Consent Bootstrap Problem

For new users who have not yet granted consent:

```
Channel = OTP (account creation / login): EXEMPT — proceed
Channel = SMS / PUSH (onboarding messages): HOLD until consent wizard completed
Consent wizard triggered by: User Service on user.created event
PTNA holds all non-OTP notifications until consent_status = active
```

---

## SECTION XII — INTEGRATION WITH GD ORCHESTRATOR

The Voice GD Orchestrator is the highest-volume notification trigger in the system. PTNA enforces strict discipline on GD-related phone communications.

**GD Notification Sequence (PTNA-governed):**

```
T-24h:  GD_SESSION_SCHEDULED → SMS + PUSH (1 send, no repeat)
T-30m:  GD_SESSION_REMINDER_30M → SMS + PUSH (rate-protected, max 1)
T-10m:  GD_SESSION_REMINDER_10M → PUSH only (rate-protected, max 1)
T-0:    GD_SESSION_STARTED → PUSH only (max 1 per session)
T+1h:   GD_SCORE_PUBLISHED → SMS + PUSH (max 1 per session)
```

**Per-session enforcement:**
- `max_per_session` is enforced per `trigger_ref = session_id`.
- Redis key: `ptna:gd_session:{session_id}:{user_id}:{type}` → send count.
- If a candidate is retried (e.g., network failure), PTNA prevents duplicate dispatch using `delivery_token` uniqueness on ledger.

---

## SECTION XIII — ENTERPRISE OPTIMIZATION METRICS

PTNA contributes directly to enterprise-grade optimization signals:

| Metric | Business Value |
|---|---|
| `Notification Efficiency Rate` = dispatched / (dispatched + held + failed) | Measures how cleanly the platform communicates |
| `Consent Coverage Rate` = users with active consent / total active users | Platform trust health indicator |
| `OTP Success Rate` = OTP delivered / OTP requested | Auth funnel health |
| `GD Reminder Delivery Rate` = GD reminders delivered / GD sessions created | Attendance improvement signal |
| `Guardian Mirror Rate` = guardian mirrors sent / minor user notifications | Parental trust score |
| `Abuse Signal Rate` = abuse signals / total dispatches (should be < 0.001%) | Platform safety score |

These metrics feed into the **Ecoskiller Trust Score Dashboard** (Grafana).

---

## SECTION XIV — VERSION CONTROL AND MUTATION POLICY

```
Current Version:     1.0
Status:              LOCKED
Mutation Policy:     Add-only
                     New notification types → registry insert + OPA policy + version bump
                     New rate limits → Redis config update + version bump
                     New channels → full section addition + version bump
                     Schema changes → Flyway migration + version bump

What CANNOT Change Without Full Version Review:
  - Core Three Laws (Section III.1)
  - Ledger schema primary keys and immutability rules
  - OTP security exemption logic
  - Guardian consent enforcement for minors
  - Wazuh abuse signal threshold for CONSENT_BREACH (always 1 = critical)
```

---

## SECTION XV — ACCEPTANCE CRITERIA (NON-NEGOTIABLE)

The PTNA is considered production-ready **only when all of the following pass:**

```
✅ AC-01: Every dispatch request produces a ledger entry — zero silent sends
✅ AC-02: Rate limits enforced — 100 consecutive tests at limit return HELD
✅ AC-03: OTP dispatch succeeds without consent record
✅ AC-04: SMS blocked when consent_status = revoked
✅ AC-05: Guardian consent required for user with age < 18
✅ AC-06: OPA policy denial results in POLICY_DENIED ledger entry, zero dispatch
✅ AC-07: Redis unavailability blocks all non-OTP dispatch within 5 seconds
✅ AC-08: Abuse signal fires when OTP flood threshold crossed
✅ AC-09: Transparency endpoint returns correct history for authenticated user
✅ AC-10: Unknown notification_type rejected at intercept (not at gateway)
✅ AC-11: All Prometheus metrics populated after 100 test dispatches
✅ AC-12: Grafana dashboard loads with real data in staging environment
✅ AC-13: PII fields (phone) encrypted at rest — plaintext not visible in DB
✅ AC-14: duplicate delivery_token rejected (idempotency enforced)
✅ AC-15: Consent revocation takes effect within 60 seconds (Redis TTL test)
```

**Failure in any AC → STOP DEPLOYMENT → REPORT → NO PRODUCTION CLAIM PERMITTED**

---

## SECTION XVI — FINAL SYSTEM DEFENSE

```
"The PHONE_TRANSPARENCY_NOTIFICATION_AGENT converts every phone communication
 dispatched by the Ecoskiller platform into an auditable, consent-verified,
 rate-disciplined, policy-governed ledger entry — eliminating silent sends,
 notification abuse, consent violations, and enterprise opacity through
 deterministic enforcement, not interpretation."

WebRTC is the physics.
Jitsi is the engine.
The GD Orchestrator is the law for group discussions.
PTNA is the law for every phone that rings because of this platform.
```

---

```
Document ID:    ECOSKILLER-ANTIGRAVITY-PTNA-v1.0
Classification: ENTERPRISE TRUST INFRASTRUCTURE — INTERNAL
Locked By:      Human declaration
Mutation By:    Version bump only
Expiry:         None
```
