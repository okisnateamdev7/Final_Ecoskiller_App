# PHONE_FEATURE_GATING_AGENT
## Billing & Quota Division — Ecoskiller SaaS Platform

```
Artifact Class       : Production Billing & Quota Agent Specification
Agent ID             : BILL-QUOTA-PFGA-002
Status               : FINAL · SEALED · LOCKED · NON-NEGOTIABLE
Version              : v1.0
Mutation Policy      : Add-only via version bump
Interpretation Auth  : NONE
Execution Authority  : Human declaration only
Domain               : Billing & Quota → Phone Feature Gating
Parent System        : ECOSKILLER MASTER EXECUTION PROMPT v12.0
Applies To           : All User Tiers · All Tenant Types · All Notification
                       Channels · Phone Verification · OTP · SMS · Push ·
                       WhatsApp · Voice Call Features · Society Offline Layer
Classification       : ANTIGRAVITY — Sealed Prompt Artifact
Enforcement Stack    : Kong · Redis · Keycloak · Jasmin SMS Gateway · ntfy ·
                       Postfix · FCM · PostgreSQL · Kafka · ClickHouse ·
                       HashiCorp Vault · Open Policy Agent
Related Agents       : CALL_RATE_LIMIT_AGENT (BILL-QUOTA-CRLA-001)
                       TENANT_AUDIO_OBJECT_ISOLATION_AGENT (SEC-COMP-TAOIA-001)
```

---

> **SEAL DECLARATION**
> This document is a locked system agent specification. No clause may be
> reinterpreted, softened, removed, or overridden without a formal version
> bump and explicit human-signed declaration. Any deviation from this
> specification is a BILLING INTEGRITY VIOLATION and must trigger
> STOP EXECUTION immediately.

---

## SECTION 1 — AGENT IDENTITY & PURPOSE

### 1.1 Agent Name
`PHONE_FEATURE_GATING_AGENT` (PFGA)

### 1.2 Classification
**Billing & Quota Agent** — enforces deterministic, plan-bound, per-tenant
gating of every phone-related feature across the Ecoskiller platform. This
includes SMS OTP delivery, push notifications, WhatsApp sharing channels,
voice-based interview reminders, phone number verification workflows,
notification channel entitlements, and mobile-number-dependent trust features.

### 1.3 Problem Statement

Ecoskiller is a **multi-tenant SaaS** serving nine actor classes:
Students, Trainers/Mentors, Evaluators, Institutes, Enterprises, Recruiters,
Admins, Parents (read-only trust layer), and Automation/AI Agents — across
five domain tracks (Arts, Commerce, Science, Technology, Administration).

The platform operates a **self-hosted phone channel stack**:
- **Jasmin SMS Gateway** — SMS delivery for OTPs and notifications
- **ntfy** — Lightweight push notifications (self-hosted)
- **FCM Gateway** — Firebase Cloud Messaging for mobile push
- **Postfix** — Email delivery (boundary of phone feature scope)
- **WhatsApp / SMS sharing** — Multi-channel referral and content sharing

Phone features are **costly per-dispatch, abuse-prone, and plan-tier-gated**.
Without a hard enforcement agent:

- A Free-tier student can trigger unlimited OTP retries exhausting Jasmin
  gateway capacity shared across all tenants
- A Starter institute can send bulk SMS notifications exceeding plan allocation
  without billing
- WhatsApp and SMS referral sharing on restricted tiers bypasses cost controls
- Phone number verification can be abused for account cycling, credential
  stuffing, and SIM-swap fraud without rate enforcement
- Non-verified phone numbers can receive sensitive GD session tokens,
  interview reminders, and score notifications — a security and privacy gap
- Parents and guardian accounts on the Society/offline layer receive SMS
  notifications for child events without plan-tier checks
- Push notification bursts from automation agents can exhaust ntfy/FCM
  delivery capacity for real tenants

The PHONE_FEATURE_GATING_AGENT is the permanent, deterministic enforcement
agent for every phone-related feature gate, quota, rate limit, and billing
meter across the entire platform.

### 1.4 Governing Principle

> **No phone-related feature — OTP, SMS, push, WhatsApp, phone verification,
> voice reminder, or mobile-number-dependent trust gate — activates without
> a valid plan entitlement, a tenant binding, a metered record, and an
> immutable audit trail. No phone dispatch is free. No phone feature is
> assumed. Every channel is earned by plan tier.**

No exception. No override. No workaround.

---

## SECTION 2 — PHONE FEATURE TAXONOMY (COMPLETE)

PFGA governs every phone-related feature class in Ecoskiller:

| Feature Class | Description | Metered | Gated | Rate-Limited |
|---|---|---|---|---|
| **OTP — Login / MFA** | SMS OTP for auth, MFA enforcement | ✅ | ✅ | ✅ |
| **OTP — Phone Verification** | One-time phone number ownership proof | ✅ | ✅ | ✅ |
| **OTP — Action Confirmation** | High-value action OTP (payment, cert issue) | ✅ | ✅ | ✅ |
| **SMS Notification — System** | GD schedule, interview slot, score alert | ✅ | ✅ | ✅ |
| **SMS Notification — Bulk** | Institute batch alerts, recruiter broadcast | ✅ | ✅ | ✅ |
| **SMS Notification — Transactional** | Invoice, payment confirm, refund alert | ✅ | ✅ | ✅ |
| **SMS Referral Share** | Multi-channel referral via SMS | ✅ | ✅ | ✅ |
| **WhatsApp Share** | Referral, content, offer letter sharing | ✅ | ✅ | ✅ |
| **Push — In-App Alert** | ntfy / FCM push to Flutter app | ✅ | ✅ | ✅ |
| **Push — Background Sync** | Silent push for offline Society layer sync | ✅ | ✅ | ✅ |
| **Push — Match / GD Alert** | Dojo match ready, GD starting soon | ✅ | ✅ | ✅ |
| **Push — Bulk Broadcast** | Tenant-wide push (platform events, policy) | ✅ | ✅ | ✅ |
| **Phone Number Storage** | Storing verified phone on user record | ✅ | ✅ | ❌ |
| **Phone Number Update** | Changing verified phone on profile | ✅ | ✅ | ✅ |
| **Phone-Based Login** | Phone + OTP as primary auth method | ✅ | ✅ | ✅ |
| **Voice Reminder Call** | Automated call for interview / GD (future) | ✅ | ✅ | ✅ |
| **Parent / Guardian SMS** | Society-layer: child event alert to guardian | ✅ | ✅ | ✅ |
| **Society Offline SMS** | Rural QR attendance confirm, cert dispatch | ✅ | ✅ | ✅ |
| **Webhook Phone Trigger** | Outbound webhook carrying phone metadata | ✅ | ✅ | ✅ |
| **Phone Trust Score Gate** | Features unlocked only post phone verify | ❌ | ✅ | ❌ |

---

## SECTION 3 — PLAN TIER ENTITLEMENTS (LOCKED)

### 3.1 Tenant Tier Registry

Six tiers govern all phone feature entitlements. Tiers are assigned at
tenant onboarding and enforced by PFGA on every phone-related call:

| Tier ID | Tier Name | Primary Persona |
|---|---|---|
| T0 | FREE | Student, unverified self-learning |
| T1 | STARTER | Small institute, early recruiter |
| T2 | PROFESSIONAL | Mid-size college, active enterprise |
| T3 | ENTERPRISE | Corporate, large institute, franchise owner |
| T4 | PLATFORM_ADMIN | Internal Ecoskiller operators |
| T5 | CUSTOM | Negotiated enterprise / Society franchise contract |

### 3.2 OTP Entitlement Matrix (Per User Per Rolling Window)

| OTP Type | T0 FREE | T1 STARTER | T2 PROFESSIONAL | T3 ENTERPRISE |
|---|---|---|---|---|
| Login OTP (per hour) | 3 | 5 | 10 | 20 |
| Login OTP (per day) | 5 | 15 | 50 | 200 |
| Phone verify OTP (per day) | 2 | 5 | 10 | 20 |
| Phone verify OTP (per phone, lifetime) | 5 | 10 | 20 | 50 |
| Action confirm OTP (per day) | 2 | 5 | 20 | 100 |
| OTP retry window (seconds) | 60 | 45 | 30 | 20 |
| OTP validity window (seconds) | 300 | 300 | 300 | 300 |
| OTP digit length | 6 | 6 | 6 | 6 |

**OTP limit breached → DENY dispatch → HTTP 429 + retry_after → AUDIT LOG**
**OTP lifetime phone limit breached → DENY → FLAG for human review → ALERT**

### 3.3 SMS Notification Entitlement Matrix (Per Tenant Per Calendar Month)

| SMS Type | T0 FREE | T1 STARTER | T2 PROFESSIONAL | T3 ENTERPRISE | T5 CUSTOM |
|---|---|---|---|---|---|
| System SMS (GD, interview, score) | 10 | 100 | 1,000 | 10,000 | Negotiated |
| Bulk SMS (broadcast to users) | 0 | 50 | 500 | 5,000 | Negotiated |
| Transactional SMS (invoice, payment) | 5 | 50 | 500 | 5,000 | Negotiated |
| Referral SMS per user per day | 2 | 5 | 10 | 20 | Negotiated |
| Parent / guardian SMS | 0 | 0 | 50 | 500 | Negotiated |
| Society offline SMS | 0 | 0 | 0 | 1,000 | Negotiated |

**Monthly SMS quota exhausted → DENY dispatch → emit sms.quota.exhausted → NOTIFY tenant admin**
**Bulk SMS on T0/T1 → HTTP 402 Feature Gated → UPGRADE PROMPT**

### 3.4 Push Notification Entitlement Matrix (Per Tenant Per Calendar Month)

| Push Type | T0 FREE | T1 STARTER | T2 PROFESSIONAL | T3 ENTERPRISE |
|---|---|---|---|---|
| In-app alert push | 50 | 500 | 5,000 | 50,000 |
| Match / GD alert push | 20 | 200 | 2,000 | 20,000 |
| Background sync push (silent) | 10 | 100 | 1,000 | 10,000 |
| Bulk broadcast push | 0 | 0 | 500 | 5,000 |
| Push per user per day | 5 | 10 | 20 | 50 |

**Push quota exhausted → Queue with delay → NOTIFY tenant admin (do not drop silently)**

### 3.5 WhatsApp Share Entitlement Matrix (Per User Per Day)

| WhatsApp Feature | T0 FREE | T1 STARTER | T2 PROFESSIONAL | T3 ENTERPRISE |
|---|---|---|---|---|
| Referral share via WhatsApp | 3 | 10 | 30 | 100 |
| Content share (job, cert, offer) | 2 | 5 | 20 | 100 |
| Bulk WhatsApp (future) | 0 | 0 | 0 | Negotiated |

**WhatsApp share on restricted tier → HTTP 402 → UPGRADE PROMPT → AUDIT LOG**

### 3.6 Real-Time Rate Limits (Sliding Window — All Tiers)

| Phone Feature | Window | T0 FREE | T1 STARTER | T2 PROFESSIONAL | T3 ENTERPRISE |
|---|---|---|---|---|---|
| OTP dispatch | 60 sec | 1 | 2 | 5 | 10 |
| OTP dispatch | 1 hour | 3 | 5 | 10 | 20 |
| SMS dispatch (any type) | 60 sec | 2 | 5 | 20 | 100 |
| SMS dispatch (bulk) | 60 min | 0 | 10 | 100 | 1,000 |
| Push dispatch | 60 sec | 5 | 20 | 100 | 500 |
| Push dispatch (per user) | 60 sec | 1 | 2 | 5 | 10 |
| WhatsApp share | 60 min | 3 | 10 | 30 | 100 |
| Phone number update | 24 hour | 1 | 2 | 3 | 5 |
| Phone verify OTP request | 60 min | 1 | 2 | 3 | 5 |

---

## SECTION 4 — PHONE NUMBER VERIFICATION PROTOCOL (LOCKED)

### 4.1 Verification Principle

Phone number verification is a **trust gate** — not merely an account step.
A verified phone number unlocks plan-tier features. An unverified phone
number is treated as absent for all gating purposes.

```
VERIFICATION STATES:
  UNVERIFIED   → phone stored, OTP not yet confirmed
  VERIFIED     → OTP confirmed, trust gate open
  SUSPENDED    → verified but flagged (fraud, SIM-swap, abuse)
  BLACKLISTED  → phone number permanently barred from platform
```

### 4.2 Verification Flow (Deterministic)

```
1. User submits phone number → PFGA validates:
     a. E.164 format enforced (+91XXXXXXXXXX, etc.)
     b. Carrier prefix not in BLACKLIST registry
     c. Phone not already VERIFIED on another active account (same tenant)
     d. Phone not in BLACKLISTED state
     e. Daily verify OTP request limit not exceeded (Section 3.2)
     f. Lifetime phone verify attempts not exceeded

2. If validation passes:
     → Generate 6-digit cryptographic OTP
     → Store OTP hash in Redis: pfga:{tenant_id}:otp:{phone_hash}:{otp_hash}
     → TTL = 300 seconds
     → Dispatch SMS via Jasmin Gateway
     → Write meter record (otp_dispatched)
     → Start retry window timer: pfga:{tenant_id}:otp_retry:{phone_hash}

3. User submits OTP:
     → Retrieve OTP hash from Redis
     → Constant-time comparison (timing attack prevention)
     → If match: mark phone VERIFIED in PostgreSQL user record
     → DELETE OTP key from Redis
     → Emit phone.verified Kafka event
     → Write audit record

4. On OTP failure:
     → Increment failure counter: pfga:{tenant_id}:otp_fail:{phone_hash}
     → If failures >= 3 → SUSPEND phone number → require human review
     → Write audit record for each failure
```

### 4.3 Phone Number Uniqueness Rules

```
Within a tenant:
  One phone number → One active VERIFIED account (enforced by DB unique constraint
  + PFGA pre-check before OTP dispatch)

Cross-tenant:
  Same phone number CAN exist on multiple tenants (enterprise multi-tenant model)
  PFGA does NOT enforce cross-tenant phone uniqueness

Platform-level blacklist:
  Phone numbers on the platform BLACKLIST are barred across ALL tenants
  PFGA checks blacklist before ANY OTP dispatch
```

### 4.4 SIM-Swap & Fraud Detection

PFGA monitors for SIM-swap indicators:

```
Trigger: Phone number verified on Account A
         Same phone OTP attempt on Account B (same tenant) within 24h
Response:
  → DENY second verification
  → FLAG both accounts for review
  → emit phone.simswap_suspected Kafka event
  → Wazuh SIEM alert: HIGH severity

Trigger: >3 OTP failures on same phone in 1 hour
Response:
  → SUSPEND phone number (PFGA state: SUSPENDED)
  → DENY all further OTP for that phone for 24 hours
  → emit phone.abuse_detected Kafka event
  → Wazuh SIEM alert: MEDIUM severity

Trigger: Phone verified then immediate bulk SMS dispatch attempt
Response:
  → Apply 1-hour cool-down before bulk SMS allowed post-verify
  → This prevents "verify and spam" pattern
```

---

## SECTION 5 — OTP SECURITY PROTOCOL (LOCKED)

### 5.1 OTP Generation

```
Algorithm   : TOTP-compatible 6-digit numeric
Entropy     : cryptographically random (crypto.randomInt — Node.js / SecureRandom — Java)
Storage     : HASH stored in Redis (HMAC-SHA256 of OTP + phone_hash + tenant_id)
              NEVER stored in plaintext
Comparison  : Constant-time hash comparison only (prevents timing attacks)
Transmission: Via Jasmin SMS Gateway only — never in API response body
              NEVER in logs, traces, or error messages
```

### 5.2 Redis OTP Key Schema (LOCKED)

```
All OTP keys must follow this namespace:

pfga:{tenant_id}:otp:{phone_hash}              → OTP hash value
pfga:{tenant_id}:otp_ttl:{phone_hash}          → TTL tracker (300s)
pfga:{tenant_id}:otp_fail:{phone_hash}         → Failure counter
pfga:{tenant_id}:otp_retry:{phone_hash}        → Retry window lock
pfga:{tenant_id}:otp_daily:{phone_hash}        → Daily dispatch counter
pfga:{tenant_id}:otp_lifetime:{phone_hash}     → Lifetime verify counter
pfga:{tenant_id}:phone_suspend:{phone_hash}    → Suspension flag

Rules:
  - phone_hash = HMAC-SHA256(E.164_phone_number, platform_secret)
                 Platform secret stored in HashiCorp Vault
  - Raw phone number NEVER appears in Redis key
  - All keys are tenant-scoped (pfga:{tenant_id}: prefix mandatory)
  - Unscoped OTP key write → STOP EXECUTION → ALERT
```

### 5.3 OTP Delivery Rules

```
Delivery channel priority:
  1. SMS via Jasmin (primary — always attempted first)
  2. In-app notification via ntfy (fallback if SMS delivery fails)
  3. Email via Postfix (final fallback for OTP — only if SMS and push both fail)

OTP in email:
  → Allowed ONLY as last-resort fallback
  → Flagged in audit as FALLBACK_DELIVERY
  → Email OTP validity: 180 seconds (shorter than SMS, higher risk channel)

OTP in API response body:
  → FORBIDDEN under all conditions
  → Any code path that returns OTP in response body → STOP EXECUTION

OTP in logs / traces:
  → FORBIDDEN under all conditions
  → OTP fields must be masked in all OpenTelemetry spans
  → Log sanitization enforced at PFGA middleware layer
```

---

## SECTION 6 — SMS CHANNEL ENFORCEMENT ARCHITECTURE

### 6.1 Jasmin Gateway Integration

Jasmin SMS Gateway (`jookies/jasmin`, Apache 2.0) is the sole self-hosted
SMS delivery engine. PFGA is the only service permitted to dispatch to Jasmin.
No other microservice may call Jasmin directly.

```
All SMS dispatch paths:
  [Any Service] → Notification Service → PFGA SMS Gate → Jasmin → Carrier

PFGA SMS Gate responsibilities:
  1. Validate tenant_id and plan tier
  2. Check monthly SMS quota (Redis)
  3. Check real-time rate limit (Redis sliding window)
  4. Check phone VERIFIED status
  5. Check phone not SUSPENDED or BLACKLISTED
  6. Check message template is pre-approved (no free-text SMS)
  7. Dispatch to Jasmin
  8. Write meter record
  9. Handle Jasmin delivery receipt and update meter status
```

### 6.2 SMS Template Registry (LOCKED)

All SMS messages must use pre-approved templates. No free-text SMS dispatch
is permitted. Template content is immutable once approved.

```
Template ID     | Purpose                   | Tenant Tiers Allowed
─────────────── | ────────────────────────── | ─────────────────────
SMS_OTP_LOGIN   | Login OTP delivery         | T0, T1, T2, T3, T4, T5
SMS_OTP_VERIFY  | Phone verification OTP     | T0, T1, T2, T3, T4, T5
SMS_OTP_ACTION  | High-value action OTP      | T1, T2, T3, T4, T5
SMS_GD_REMINDER | GD session reminder        | T1, T2, T3, T5
SMS_INTERVIEW   | Interview slot reminder    | T1, T2, T3, T5
SMS_SCORE_ALERT | GD/Dojo score available    | T1, T2, T3, T5
SMS_INVOICE     | Invoice generated notice   | T1, T2, T3, T5
SMS_PAYMENT     | Payment confirmation       | T1, T2, T3, T5
SMS_REFERRAL    | Referral invite link       | T0, T1, T2, T3
SMS_BULK_ALERT  | Institute broadcast        | T2, T3, T5
SMS_PARENT      | Child event alert          | T2, T3, T5
SMS_SOCIETY_QR  | Society attendance confirm | T3, T5
SMS_CERT        | Certificate ready notice   | T1, T2, T3, T5
```

**Free-text SMS dispatch attempt → DENY → STOP → ALERT → AUDIT**
**Template used on non-entitled tier → HTTP 402 → UPGRADE PROMPT**

### 6.3 Bulk SMS Throttle Protocol

Bulk SMS (SMS_BULK_ALERT) requires additional pre-authorization:

```
1. Tenant admin submits bulk SMS job:
     - recipient_list: max 5,000 per batch
     - template_id: must be SMS_BULK_ALERT
     - scheduled_at: must be ≥ 5 minutes future

2. PFGA validates:
     - Plan tier allows bulk SMS (T2+ only)
     - Monthly quota sufficient for full recipient count
     - Rate limit window allows batch

3. If authorized:
     - Issue bulk dispatch token (UUID, 24h TTL)
     - Schedule via Apache Airflow DAG
     - Dispatch in chunks of 100 per minute (Jasmin gateway protection)
     - Write meter record per chunk dispatched

4. If quota exhaustion during batch:
     - STOP remaining dispatch
     - Write partial dispatch audit record
     - Notify tenant admin of partial send + quota exhaustion
     - emit sms.bulk.partial_exhaustion Kafka event
```

---

## SECTION 7 — PUSH NOTIFICATION ENFORCEMENT ARCHITECTURE

### 7.1 Push Channel Stack

Two push channels are self-hosted and PFGA-governed:

```
ntfy (binwiederhier/ntfy, Apache 2.0):
  → Lightweight in-app alerts
  → Society offline layer silent sync
  → Low-latency operational notifications (GD starting, match ready)

FCM Gateway (Firebase Cloud Messaging — via self-hosted proxy):
  → Mobile push to Flutter Android/iOS app
  → High-delivery-rate critical alerts
  → Background sync for offline-capable mobile nodes
```

### 7.2 Push Dispatch Gate

```
All push dispatch paths:
  [Any Service] → Notification Service → PFGA Push Gate → ntfy / FCM

PFGA Push Gate responsibilities:
  1. Validate tenant_id and plan tier
  2. Check monthly push quota (Redis counter)
  3. Check per-user daily push limit (Redis counter)
  4. Check real-time burst rate limit (Redis sliding window)
  5. Check feature entitlement (bulk push: T2+ only)
  6. Dispatch to ntfy or FCM based on channel routing rules
  7. Write meter record
  8. Handle delivery receipt and update meter status
  9. On delivery failure: apply retry policy and write retry audit
```

### 7.3 Push Delivery Priority Routing

```
Priority: CRITICAL (GD start, match ready, payment confirmed)
  → Channel: FCM (guaranteed delivery attempt)
  → Retry: 3 attempts, 30-second intervals
  → Fallback: SMS if FCM fails after all retries

Priority: HIGH (score released, interview in 1 hour)
  → Channel: FCM + ntfy (dual dispatch)
  → Retry: 2 attempts, 60-second intervals
  → Fallback: In-app on next app open

Priority: NORMAL (badge update, content notification)
  → Channel: ntfy (primary)
  → Retry: 1 attempt, 300-second interval
  → Fallback: None

Priority: SILENT (background sync, cache warm)
  → Channel: FCM silent push
  → Retry: 0 (fire and forget)
  → Fallback: None
```

---

## SECTION 8 — PHONE TRUST SCORE GATE (LOCKED)

### 8.1 Trust Gate Principle

Phone verification is a prerequisite gate for plan-specific features.
PFGA enforces this gate at every feature-access checkpoint.

```
PHONE TRUST GATE MATRIX:

Feature                          | Unverified Phone | Verified Phone
──────────────────────────────── | ──────────────── | ───────────────
Login via phone + OTP            | ❌ DENIED         | ✅ ALLOWED
GD session join (voice)          | ❌ DENIED         | ✅ ALLOWED (T1+)
Interview slot booking           | ❌ DENIED         | ✅ ALLOWED (T1+)
Dojo match join                  | ⚠️ ALLOWED (T0)   | ✅ ALLOWED
Bulk action submission           | ❌ DENIED         | ✅ ALLOWED
Referral SMS/WhatsApp share      | ❌ DENIED         | ✅ ALLOWED
Recruiter profile approval       | ❌ DENIED         | ✅ ALLOWED
Certification issuance           | ❌ DENIED         | ✅ ALLOWED
Parent account visibility        | ❌ DENIED         | ✅ ALLOWED
Society coordinator role         | ❌ DENIED         | ✅ ALLOWED
Automation agent API key issue   | ❌ DENIED         | ✅ ALLOWED (T2+)
```

### 8.2 Trust Score Modulation

Phone verification state feeds directly into the platform-wide trust score:

```
Phone VERIFIED:          trust_score += 15 points
Phone SUSPENDED:         trust_score -= 30 points
Phone BLACKLISTED:       trust_score = 0 (forced minimum)
Phone never provided:    trust_score += 0 (neutral — email path)
OTP failure × 3:         trust_score -= 10 points
SIM-swap suspected:      trust_score -= 25 points
```

Trust score impact is computed by the Reputation Service and fed back to
PFGA for rate limit modulation (progressive unlocking — see CRLA §9.2).

---

## SECTION 9 — SOCIETY & OFFLINE LAYER PHONE GOVERNANCE

### 9.1 Society Domain Context

The Ecoskiller Society/Offline Franchise layer operates in rural and
semi-urban zones with limited connectivity. Phone-based features in this
layer serve:

- QR attendance confirmation SMS to student/parent
- Certificate dispatch notification to guardian phone
- Coach and coordinator OTP authentication (low-bandwidth)
- Child welfare incident alerts to guardian phone
- Offline sync trigger via silent push to coach device

### 9.2 Society-Specific Phone Rules

```
Society SMS (SMS_SOCIETY_QR, SMS_PARENT, SMS_CERT):
  - Available on T3 ENTERPRISE and T5 CUSTOM only
  - Must carry guardian_consent = true in dispatch metadata
  - Minor recipient (<18) SMS → guardian phone only, not student phone
  - Guardian phone must be VERIFIED before any child event SMS dispatched
  - Consent record stored in MinIO: /society/{tenant_id}/guardian_consent/
  - Every society SMS requires compliance_tag in meter record

Society Push (silent background sync):
  - Coach device must be enrolled via FCM (device token registered)
  - Offline sync push dispatched max every 10 minutes per device
  - Rate enforced by PFGA Redis: pfga:{tenant_id}:society_sync:{device_token}

Child Safety Lock:
  - Any SMS or push containing a child's full name requires:
      guardian_consent = true in dispatch context
      minor_safe_mode = true (name replaced with initials in message body)
  - Absence of minor_safe_mode on child SMS → DENY → AUDIT → ALERT
```

---

## SECTION 10 — METERING ENGINE

### 10.1 Metering Principle

Every phone feature dispatch — executed or denied — must produce a
meter record before action execution or denial return.

```
METERING RULE: Meter first, then dispatch or deny.
               An unmetered phone dispatch is a billing gap.
               An unmetered denial is an audit gap.
               Neither is permitted.
```

### 10.2 Meter Record Schema

```json
{
  "meter_id": "<uuid>",
  "agent": "PHONE_FEATURE_GATING_AGENT",
  "tenant_id": "<uuid>",
  "actor_id": "<user_id or agent_id>",
  "actor_type": "human | automation_agent | system",
  "feature_class": "otp_login | otp_verify | otp_action | sms_system | sms_bulk | sms_transactional | sms_referral | push_alert | push_silent | push_bulk | whatsapp_share | phone_verify | society_sms | parent_sms",
  "template_id": "<SMS_OTP_LOGIN | SMS_GD_REMINDER | ...>",
  "channel": "sms | push_fcm | push_ntfy | whatsapp",
  "recipient_phone_hash": "<HMAC-SHA256 of E.164 phone>",
  "plan_tier": "T0 | T1 | T2 | T3 | T4 | T5",
  "status": "DISPATCHED | DENIED | RATE_LIMITED | QUOTA_EXHAUSTED | FEATURE_GATED | DELIVERY_FAILED | FALLBACK_DELIVERED",
  "denial_reason": "<null or reason code>",
  "units_consumed": 1,
  "billable": true,
  "delivery_receipt": "DELIVERED | FAILED | PENDING | null",
  "jasmin_message_id": "<null or Jasmin msg ID>",
  "compliance_tag": "<null | society | minor_safe | guardian_consent>",
  "timestamp": "<ISO 8601>",
  "trace_id": "<OpenTelemetry trace ID>"
}
```

**Recipient phone number NEVER appears in meter record — only HMAC hash**

### 10.3 Meter Storage Architecture

```
Path 1 — Real-time (Redis): quota counter INCR on every dispatch
Path 2 — Durable (ClickHouse via Kafka):
  Kafka topic: ecoskiller.{tenant_id}.metering.phone.{feature_class}
  → Analytics Service consumer → ClickHouse phone_meters table

ClickHouse Schema:
CREATE TABLE phone_meters (
  meter_id            UUID,
  tenant_id           UUID NOT NULL,
  feature_class       LowCardinality(String),
  channel             LowCardinality(String),
  plan_tier           LowCardinality(String),
  status              LowCardinality(String),
  units_consumed      UInt32,
  billable            UInt8,
  delivery_receipt    LowCardinality(String),
  compliance_tag      Nullable(String),
  event_ts            DateTime
) ENGINE = MergeTree()
PARTITION BY (tenant_id, toYYYYMM(event_ts))
ORDER BY (tenant_id, feature_class, event_ts);
```

**All ClickHouse reads MUST include `WHERE tenant_id = ?`**
**Raw phone numbers are NEVER written to ClickHouse**

---

## SECTION 11 — FEATURE GATE RESPONSE CONTRACTS

### 11.1 Denied — Feature Not in Plan

```json
HTTP 402 Payment Required
{
  "allowed": false,
  "agent": "PHONE_FEATURE_GATING_AGENT",
  "gate_reason": "PLAN_FEATURE_GATED",
  "feature_class": "sms_bulk",
  "template_id": "SMS_BULK_ALERT",
  "current_tier": "T1",
  "required_tier": "T2",
  "upgrade_url": "/billing/upgrade",
  "upgrade_prompt": true
}
```

### 11.2 Denied — Quota Exhausted

```json
HTTP 429 Too Many Requests
{
  "allowed": false,
  "agent": "PHONE_FEATURE_GATING_AGENT",
  "gate_reason": "MONTHLY_QUOTA_EXHAUSTED",
  "feature_class": "sms_system",
  "quota_used": 1000,
  "quota_limit": 1000,
  "reset_at": "<ISO 8601 — start of next calendar month>",
  "upgrade_url": "/billing/upgrade",
  "upgrade_prompt": true
}
```

### 11.3 Denied — Rate Limited

```json
HTTP 429 Too Many Requests
Retry-After: 58
{
  "allowed": false,
  "agent": "PHONE_FEATURE_GATING_AGENT",
  "gate_reason": "RATE_LIMIT_BREACHED",
  "feature_class": "otp_login",
  "window": "60s",
  "limit": 1,
  "retry_after_seconds": 58
}
```

### 11.4 Denied — Phone Not Verified

```json
HTTP 403 Forbidden
{
  "allowed": false,
  "agent": "PHONE_FEATURE_GATING_AGENT",
  "gate_reason": "PHONE_NOT_VERIFIED",
  "feature_class": "gd_session_join",
  "verification_required": true,
  "verify_url": "/profile/phone/verify"
}
```

### 11.5 Denied — Phone Suspended or Blacklisted

```json
HTTP 403 Forbidden
{
  "allowed": false,
  "agent": "PHONE_FEATURE_GATING_AGENT",
  "gate_reason": "PHONE_SUSPENDED | PHONE_BLACKLISTED",
  "feature_class": "otp_login",
  "support_url": "/support/account"
}
```

---

## SECTION 12 — AGENT OPERATIONAL LIFECYCLE

### 12.1 Phone Feature Request Intake

```
TRIGGER: Any phone-related feature call reaches Notification Service
         or PFGA middleware (via Kong plugin)

PFGA Actions:
  1. Extract tenant_id from JWT (Keycloak-validated)
  2. Extract actor_id and actor_type
  3. Fetch plan tier from PFGA Policy Cache (Redis, TTL 60s)
  4. Evaluate Feature Gate — deny if feature not in plan tier (→ HTTP 402)
  5. Evaluate Phone Verification State — deny if phone not verified for
     gated features (→ HTTP 403)
  6. Check phone SUSPENDED / BLACKLISTED state (→ HTTP 403)
  7. Check template authorization for this tier (→ HTTP 402)
  8. Evaluate monthly quota (Redis monthly counter) (→ HTTP 429)
  9. Evaluate real-time rate limit (Redis sliding window) (→ HTTP 429)
  10. If all pass: write meter record (DISPATCHED) → dispatch to channel
  11. Update Redis quota counter (atomic INCR)
  12. Publish meter event to Kafka
  13. Await delivery receipt → update meter record status
```

### 12.2 OTP Lifecycle

```
REQUEST → validate → generate → hash → Redis store (300s TTL)
        → Jasmin dispatch → meter record
VERIFY  → Redis lookup → constant-time compare → if match:
        → mark VERIFIED in PostgreSQL → DELETE Redis key → emit event
EXPIRE  → Redis TTL auto-deletes hash → no cleanup needed
FAIL    → increment fail counter → if ≥ 3: SUSPEND phone → ALERT
```

### 12.3 Quota Reset Cycle (Airflow-Scheduled)

```
Monthly Reset (1st of each month, midnight UTC):
  1. Archive all phone meter counters to ClickHouse
  2. DELETE Redis monthly phone quota keys for all tenants
  3. Write reset audit record to PostgreSQL
  4. emit phone.quota.reset.monthly Kafka event
  5. Trigger invoice enrichment for phone usage in billing period

Daily Reset (midnight UTC):
  1. DELETE Redis daily OTP dispatch counters
  2. DELETE Redis daily WhatsApp share counters
  3. Write daily audit summary to PostgreSQL
```

### 12.4 Failure Handling

```
Failure Type                         → PFGA Response
──────────────────────────────────── → ──────────────────────────────────────
Redis unavailable                    → FAIL OPEN: 1 OTP/hour emergency cap
                                       + CRITICAL alert + AUDIT
Jasmin Gateway unreachable           → Retry 3× (15s, 30s, 60s intervals)
                                       → Fallback to ntfy / email
                                       → emit sms.delivery.failed event
                                       → ALERT on 3rd failure
FCM unreachable                      → Fallback to ntfy → SMS if critical
                                       → emit push.delivery.failed event
Kafka unavailable                    → Buffer meter records in PostgreSQL
                                       fallback table → ALERT
Phone hash collision (theoretical)   → Log → Flag → human review → ALERT
Guardian consent missing on society  → DENY → AUDIT → ALERT
                                       child SMS → STOP DISPATCH
Minor safety mode absent on child    → DENY → AUDIT → CRITICAL ALERT
SMS dispatch
OTP in log stream detected           → STOP EXECUTION → CRITICAL ALERT
                                       → Sanitize log → Security incident
```

---

## SECTION 13 — AUDIT & OBSERVABILITY

### 13.1 Immutable Audit Log Schema

```json
{
  "audit_id": "<uuid>",
  "agent": "PHONE_FEATURE_GATING_AGENT",
  "tenant_id": "<uuid>",
  "actor_id": "<uuid>",
  "actor_type": "human | automation_agent | system",
  "feature_class": "<phone feature type>",
  "channel": "sms | push_fcm | push_ntfy | whatsapp",
  "template_id": "<template>",
  "recipient_phone_hash": "<HMAC hash — never raw phone>",
  "decision": "ALLOWED | DENIED | RATE_LIMITED | QUOTA_EXHAUSTED | FEATURE_GATED | SUSPENDED | BLACKLISTED",
  "denial_reason": "<null or code>",
  "plan_tier": "<T0-T5>",
  "quota_snapshot": {
    "monthly_used": 847,
    "monthly_limit": 1000,
    "daily_otp_used": 3,
    "daily_otp_limit": 5
  },
  "delivery_receipt": "DELIVERED | FAILED | PENDING | null",
  "compliance_tags": ["society", "minor_safe", "guardian_consent"],
  "timestamp": "<ISO 8601>",
  "trace_id": "<OpenTelemetry trace ID>"
}
```

**Raw phone numbers are NEVER written to audit logs**
**OTP values are NEVER written to audit logs under any circumstance**

### 13.2 Wazuh SIEM Integration

PFGA must emit structured security events to Wazuh for:

| Event | Severity |
|---|---|
| OTP value detected in any log stream | CRITICAL |
| SIM-swap pattern detected | HIGH |
| Phone BLACKLIST probe attempt | HIGH |
| OTP failure × 3 on same phone | MEDIUM |
| Bulk SMS on restricted tier attempt | MEDIUM |
| Society child SMS without guardian consent | CRITICAL |
| Minor safety mode absent on child dispatch | CRITICAL |
| Free-text SMS dispatch attempt | HIGH |
| Cross-tenant phone quota probe | HIGH |
| Phone verify lifetime limit exceeded | MEDIUM |

### 13.3 Prometheus Metrics (Mandatory)

```
pfga_otp_dispatched_total{tenant_id, otp_type, channel}
pfga_otp_verified_total{tenant_id}
pfga_otp_failed_total{tenant_id, failure_reason}
pfga_otp_suspended_phones_total{tenant_id}
pfga_sms_dispatched_total{tenant_id, template_id, status}
pfga_sms_delivery_failed_total{tenant_id, template_id}
pfga_push_dispatched_total{tenant_id, channel, priority}
pfga_push_delivery_failed_total{tenant_id, channel}
pfga_whatsapp_shares_total{tenant_id, feature_class}
pfga_quota_exhausted_total{tenant_id, feature_class}
pfga_feature_gate_denials_total{tenant_id, feature_class, plan_tier}
pfga_phone_trust_gate_denials_total{tenant_id, feature_class}
pfga_society_sms_total{tenant_id, compliance_tag}
pfga_minor_safe_mode_enforced_total{tenant_id}
```

### 13.4 Grafana Dashboards (Mandatory)

- OTP dispatch volume and failure rate by tenant
- SMS quota burn-down (monthly) by tenant and template
- Push dispatch volume by channel and priority
- Phone verification funnel (submitted → verified → suspended)
- Feature gate denial breakdown (by plan tier, by feature class)
- Society SMS compliance rate (guardian consent coverage)
- Jasmin gateway health (delivery success %, latency)
- FCM/ntfy delivery rate by priority class
- Blacklisted/suspended phone count over time

---

## SECTION 14 — INTEGRATION CONTRACTS

### 14.1 Auth Service (Keycloak)
Must inject `tenant_id`, `actor_type`, `plan_tier`, `phone_verified` into JWT.
**Missing `phone_verified` claim → PFGA treats phone as UNVERIFIED**

### 14.2 Notification Service
Is the **only** caller permitted to reach PFGA SMS/Push Gate.
No microservice calls Jasmin, ntfy, or FCM directly.
**Direct Jasmin/FCM call without PFGA → STOP EXECUTION → ALERT**

### 14.3 Jasmin SMS Gateway
PFGA is sole authorized producer to Jasmin SMPP/HTTP interface.
Jasmin delivery receipts must be consumed by PFGA to update meter records.
**Delivery receipt not processed → meter record remains PENDING → Airflow reconciliation job**

### 14.4 Reputation Service
Consumes `phone.verified`, `phone.suspended`, `phone.simswap_suspected`
Kafka events from PFGA to adjust trust scores.
Trust score changes fed back to PFGA Redis cache (5-minute TTL).

### 14.5 Billing & Subscription Service
Consumes PFGA meter events from Kafka.
Phone feature usage is a line item in monthly invoice.
**Invoice without PFGA meter references for SMS/push usage → INVALID**

### 14.6 Society Compliance Service
Provides `guardian_consent` status to PFGA before society/parent SMS dispatch.
**Consent query failure → DENY dispatch → ALERT (do not fail open)**

### 14.7 Admin Governance Service
Receives all PFGA violation events.
Manages phone BLACKLIST registry (add/remove with audit).
Platform-level blacklist changes propagate to PFGA Redis within 60 seconds.

### 14.8 HashiCorp Vault
Stores:
- Platform phone hash secret (for HMAC-SHA256 of phone numbers)
- OTP signing secrets (if TOTP-based)
- Jasmin gateway credentials
- FCM service account key
**All secrets via Vault Agent Sidecar — no env vars, no ConfigMaps**

### 14.9 Open Policy Agent (OPA)
PFGA delegates complex authorization to OPA:
- "Can this actor send bulk SMS on this plan?"
- "Can this society coordinator dispatch parent SMS for this batch?"
- "Is this phone number pattern allowed in this geography?"

---

## SECTION 15 — DEPLOYMENT REQUIREMENTS

### 15.1 Kubernetes Namespace

```yaml
namespace: billing
labels:
  app: pfga
  tier: billing-quota
  criticality: critical
  data-sensitivity: high   # phone data handling
```

### 15.2 Resource Limits (Minimum)

```yaml
resources:
  requests:
    cpu: "300m"
    memory: "256Mi"
  limits:
    cpu: "1"
    memory: "512Mi"
```

### 15.3 Redis Dedicated Database Partitions

```yaml
redis:
  host: redis-pfga              # Dedicated Redis instance (not shared)
  database:
    0: plan_policy_cache         # TTL 60s
    1: otp_hashes_and_counters   # TTL per OTP window
    2: quota_counters            # Monthly / daily
    3: phone_state               # VERIFIED / SUSPENDED / BLACKLISTED
    4: rate_limit_windows        # Sliding window ZADD
  maxmemory-policy: noeviction  # MANDATORY — OTP state must never be evicted
```

### 15.4 Health Probes

Readiness must verify:
- Redis PFGA instance reachable, `noeviction` confirmed
- Jasmin Gateway reachable (HTTP health check)
- FCM proxy reachable
- HashiCorp Vault secret accessible (phone hash secret)
- Kafka producer connection healthy
- OPA policy loaded and responsive
- Guardian consent service (Society Compliance) reachable

---

## SECTION 16 — FINAL ENFORCEMENT DECLARATION

```
┌──────────────────────────────────────────────────────────────────┐
│  PHONE_FEATURE_GATING_AGENT — FINAL LAW                          │
│                                                                  │
│  1. No phone feature activates without plan-tier entitlement     │
│  2. No OTP is dispatched without a metered record                │
│  3. No OTP value appears in any log, trace, or API response      │
│  4. No raw phone number appears in Redis, logs, or audit trail   │
│  5. No SMS dispatch bypasses the Jasmin Gateway via PFGA         │
│  6. No free-text SMS template is permitted under any condition   │
│  7. No bulk SMS dispatches on T0 or T1 plan tiers                │
│  8. No child SMS dispatches without guardian consent confirmed   │
│  9. No minor's full name appears in any SMS or push message      │
│  10. No phone feature is available on suspended/blacklisted phone│
│  11. No society offline SMS dispatches without compliance_tag    │
│  12. Every phone feature denial produces an immutable audit log  │
│  13. Every quota exhaustion triggers tenant admin notification   │
│  14. Every SIM-swap pattern triggers a SIEM alert                │
│  15. Every OTP-in-log detection is a CRITICAL security incident  │
│                                                                  │
│  Violation of any rule above:                                    │
│  → DENY DISPATCH                                                 │
│  → WRITE METER RECORD (even for denied dispatches)              │
│  → WRITE AUDIT LOG                                               │
│  → FIRE SIEM ALERT (on security and compliance violations)       │
│  → REPORT TO ADMIN GOVERNANCE SERVICE                            │
│  → REPORT TO BILLING SERVICE                                     │
│                                                                  │
│  No exceptions. No overrides. No workarounds.                    │
│  T5 CUSTOM exemptions require human-signed contract only.        │
└──────────────────────────────────────────────────────────────────┘
```

---

## SECTION 17 — VERSION CONTROL

| Version | Date | Change | Authority |
|---|---|---|---|
| v1.0 | 2026-03-04 | Initial sealed specification | Human declaration |

**Next version: v1.1 — Add-only. No mutations to existing clauses.**

---

```
SEAL: PHONE_FEATURE_GATING_AGENT · BILL-QUOTA-PFGA-002 · v1.0
CLASSIFICATION: ANTIGRAVITY — SEALED PROMPT ARTIFACT
STATUS: FINAL · LOCKED · GOVERNED · NON-NEGOTIABLE
```
