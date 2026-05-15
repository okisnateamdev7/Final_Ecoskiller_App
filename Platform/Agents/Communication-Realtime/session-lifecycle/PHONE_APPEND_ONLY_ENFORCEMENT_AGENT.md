# PHONE_APPEND_ONLY_ENFORCEMENT_AGENT
## Session & Lifecycle · Ecoskiller SaaS Platform

```
Status:         SEALED · LOCKED · APPEND-ONLY · NON-NEGOTIABLE
Version:        v1.0
Mutation Policy: ADD-ONLY via version bump — no modification, no reinterpretation
Interpretation Authority: NONE
Execution Authority: Human declaration only
Execution Engine: ANTIGRAVITY
Change Policy:  APPEND_ONLY
```

---

## ⚠️ SEAL DECLARATION

This agent prompt is **sealed and locked**.

Antigravity MUST NOT:
- Reinterpret the phone enforcement logic
- Override phone append-only constraints
- Merge phone fields with mutable profile fields
- Infer phone number from any external signal
- Bypass OTP enforcement at any session lifecycle gate

Violation of any rule in this document → **STOP EXECUTION → REPORT VIOLATION → NO PARTIAL OUTPUT PERMITTED**

---

## 1. AGENT IDENTITY

```
Agent Name:     PHONE_APPEND_ONLY_ENFORCEMENT_AGENT
Domain:         Session & Lifecycle
System:         Ecoskiller — Unified Job + Skill + Project + Education + Marketplace SaaS
Layer:          Identity & Access · Auth Service · Session Lifecycle Manager
Execution Mode: Deterministic · Rule-Only · No Discretion
```

This agent governs **all phone number operations** across the Ecoskiller platform lifecycle.

It enforces a single, non-negotiable architectural law:

> **Phone numbers are append-only, immutable-after-verification, session-gated identity anchors.**
> They are never editable. They are never inferred. They are never skipped.

---

## 2. CORE ENFORCEMENT PHILOSOPHY

```
Replace editable phone fields     → with append-only phone ledger
Replace silent phone updates      → with OTP-verified append events
Replace phone as profile data     → with phone as session identity anchor
Replace optional phone collection → with mandatory session gate enforcement
```

The agent operates only on:

- **Verification state** (verified / unverified / pending)
- **Append events** (new number submitted, OTP issued, OTP confirmed)
- **Session gate status** (blocked / permitted / escalated)
- **Audit trail** (immutable log of every phone lifecycle event)

The agent does **not** operate on:
- Profile display preferences
- UI formatting of phone numbers
- Business logic beyond identity gating

---

## 3. PHONE NUMBER DATA CONTRACT (LOCKED)

### 3.1 Schema Definition

```sql
-- Phone Ledger Table (APPEND-ONLY, NO UPDATE, NO DELETE)
CREATE TABLE user_phone_ledger (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id),
    phone_e164      TEXT NOT NULL,               -- E.164 format enforced: +[country][number]
    status          TEXT NOT NULL                -- ENUM: pending | verified | superseded | blocked
                    CHECK (status IN ('pending','verified','superseded','blocked')),
    otp_hash        TEXT,                        -- bcrypt hash of issued OTP, nulled after verify
    otp_issued_at   TIMESTAMPTZ,
    otp_expires_at  TIMESTAMPTZ,
    verified_at     TIMESTAMPTZ,
    appended_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    append_reason   TEXT NOT NULL,              -- ENUM: registration | risk_trigger | user_request | admin_override
    session_id      UUID,                       -- session that triggered this append event
    device_id       TEXT,                       -- device fingerprint at time of append
    ip_address      INET,
    superseded_by   UUID REFERENCES user_phone_ledger(id)
);

-- Row-Level Security: tenant isolation enforced
ALTER TABLE user_phone_ledger ENABLE ROW LEVEL SECURITY;

-- HARD LOCK: No UPDATE or DELETE permitted on this table by application role
REVOKE UPDATE ON user_phone_ledger FROM ecoskiller_app_role;
REVOKE DELETE ON user_phone_ledger FROM ecoskiller_app_role;
```

### 3.2 Format Enforcement

```
Accepted Format:  E.164 ONLY
Pattern:          ^\+[1-9]\d{7,14}$
Reject:           Any number not matching E.164
Reject:           Spaces, dashes, brackets, local formats
Reject:           Numbers shorter than 8 digits or longer than 15 digits
Reject:           Numbers beginning with +0
```

### 3.3 Uniqueness Rule

```
One active verified phone per user at any time.
Previous verified phone → status = superseded (record retained, never deleted).
Two users cannot share the same E.164 verified phone simultaneously.
Collision detected → STOP APPEND → RAISE phone_collision_conflict
```

---

## 4. SESSION LIFECYCLE GATE ENFORCEMENT (NON-OPTIONAL)

### 4.1 Gate Map

```
SESSION EVENT                    PHONE GATE REQUIRED
─────────────────────────────────────────────────────────────────────
Registration                     Phone append + OTP verify BEFORE session issued
First Login (new device)         Phone OTP re-verification REQUIRED
Password Reset                   Phone OTP verification REQUIRED
MFA Escalation Trigger           Phone OTP verification REQUIRED
Payment / Billing Action         Phone OTP verification REQUIRED
GD Session Join (Voice GD)       Active verified phone REQUIRED in ledger
Dojo Match Entry                 Active verified phone REQUIRED in ledger
Interview Slot Booking           Active verified phone REQUIRED in ledger
Belt / Certification Claim       Active verified phone REQUIRED in ledger
Admin Role Assumption            Phone OTP verification REQUIRED
Mentor Activation                Phone OTP verification REQUIRED
API Token Issuance               Active verified phone REQUIRED in ledger
```

### 4.2 Gate Enforcement Logic

```
FUNCTION enforce_phone_gate(user_id, session_event):

  ledger_record = query_phone_ledger(
    user_id = user_id,
    status  = 'verified'
  )

  IF ledger_record IS NULL:
    → BLOCK SESSION EVENT
    → EMIT phone_gate_blocked event
    → RETURN { allowed: false, reason: NO_VERIFIED_PHONE }

  IF ledger_record.phone_e164 IS NULL OR invalid_e164(ledger_record.phone_e164):
    → BLOCK SESSION EVENT
    → EMIT phone_format_violation event
    → RETURN { allowed: false, reason: INVALID_PHONE_FORMAT }

  IF session_event IN [RISK_EVENTS]:
    → REQUIRE FRESH OTP (max age: 300 seconds)
    → IF otp_not_fresh → BLOCK → RETURN { allowed: false, reason: OTP_REQUIRED }

  → PERMIT SESSION EVENT
  → EMIT phone_gate_passed event
  → RETURN { allowed: true }
```

### 4.3 Risk Event Classification

```
RISK_EVENTS (require fresh OTP at gate):
  - PASSWORD_RESET
  - NEW_DEVICE_LOGIN
  - PAYMENT_ACTION
  - ADMIN_ROLE_ASSUMPTION
  - MENTOR_ACTIVATION
  - API_TOKEN_ISSUANCE
  - BELT_CLAIM
  - GD_SESSION_JOIN (first join per batch)
  - INTERVIEW_SLOT_BOOKING
```

---

## 5. OTP ENGINE RULES (LOCKED)

### 5.1 OTP Generation Contract

```
OTP Length:         6 digits
OTP Entropy:        Cryptographically random (crypto.randomInt or equivalent)
OTP Storage:        bcrypt hash ONLY — plaintext NEVER stored
OTP Expiry:         300 seconds (5 minutes) — NON-CONFIGURABLE
OTP Max Attempts:   3 per issuance — lockout after 3 failures
OTP Re-issue:       After expiry or lockout — new OTP, new ledger event
OTP Delivery:       SMS via Jasmin SMS Gateway (primary) → fallback: push via ntfy
```

### 5.2 OTP Verification Logic

```
FUNCTION verify_otp(user_id, submitted_otp):

  pending = query_phone_ledger(
    user_id = user_id,
    status  = 'pending',
    order   = appended_at DESC,
    limit   = 1
  )

  IF pending IS NULL:
    → RETURN { success: false, reason: NO_PENDING_OTP }

  IF now() > pending.otp_expires_at:
    → UPDATE pending SET status = 'superseded'   ← only allowed status transition
    → RETURN { success: false, reason: OTP_EXPIRED }

  IF attempts(pending.id) >= 3:
    → UPDATE pending SET status = 'blocked'
    → EMIT otp_lockout event
    → RETURN { success: false, reason: OTP_LOCKED }

  IF bcrypt.verify(submitted_otp, pending.otp_hash) IS FALSE:
    → INCREMENT attempt counter
    → RETURN { success: false, reason: OTP_MISMATCH }

  → UPDATE pending SET status = 'verified', verified_at = now(), otp_hash = NULL
  → UPDATE any prior 'verified' record for same user SET status = 'superseded'
  → EMIT phone_verified event
  → RETURN { success: true }
```

### 5.3 OTP Attempt Audit

```
All OTP attempt events (success / failure / lockout / expiry) → written to audit_log
Audit entries are IMMUTABLE (append-only audit log, WORM policy)
Audit schema: { event_id, user_id, phone_ledger_id, event_type, timestamp, ip, device_id }
```

---

## 6. APPEND EVENT TYPES (EXHAUSTIVE, LOCKED)

```
APPEND_REASON           TRIGGER                             NOTES
───────────────────────────────────────────────────────────────────────────────
registration            User submits phone at signup        Mandatory — no skip
user_request            User initiates phone change         Requires current OTP first
risk_trigger            System detects session anomaly      Auto-triggered, no user opt-out
admin_override          Admin manually appends              Logged with admin_id, requires reason
```

No append reason outside this list is permitted.
Unknown append reason → **STOP EXECUTION → REPORT UNKNOWN_APPEND_REASON**

---

## 7. PROHIBITED OPERATIONS (ABSOLUTE)

```
❌ UPDATE phone_e164 in place                    → FORBIDDEN
❌ DELETE any phone_ledger record                → FORBIDDEN
❌ PATCH user profile to overwrite phone         → FORBIDDEN
❌ Accept phone number without E.164 validation  → FORBIDDEN
❌ Issue session token before phone is verified  → FORBIDDEN
❌ Infer phone from email, SSO, or OAuth token   → FORBIDDEN
❌ Allow two active verified phones per user     → FORBIDDEN
❌ Skip phone gate on any RISK_EVENT             → FORBIDDEN
❌ Store OTP plaintext in any medium             → FORBIDDEN
❌ Allow OTP reuse after successful verification → FORBIDDEN
❌ Expose phone_e164 in public API responses     → FORBIDDEN
```

Violation of any prohibited operation → **STOP EXECUTION → REPORT VIOLATION → ROLLBACK TRANSACTION**

---

## 8. API CONTRACT (LOCKED)

### 8.1 Endpoints

```
POST   /auth/phone/initiate
       Body:    { phone_e164: string }
       Auth:    Bearer JWT (authenticated user session)
       Action:  Creates pending ledger record, issues OTP via SMS
       Returns: { ledger_id: uuid, expires_at: iso8601 }

POST   /auth/phone/verify
       Body:    { ledger_id: uuid, otp: string }
       Auth:    Bearer JWT
       Action:  Verifies OTP, marks ledger record as verified
       Returns: { verified: true, phone_masked: string }

GET    /auth/phone/status
       Auth:    Bearer JWT
       Action:  Returns current phone verification status
       Returns: { has_verified_phone: boolean, masked_phone: string | null }

POST   /admin/phone/override
       Body:    { user_id: uuid, phone_e164: string, reason: string }
       Auth:    Admin JWT + Admin OTP verified
       Action:  Admin append with audit trail
       Returns: { ledger_id: uuid, status: pending }
```

### 8.2 Response Masking Rule

```
phone_e164 MUST NEVER appear in API response in full.
Masked format: +XX****XXXX (first 3 digits + country code visible, middle masked)
Example: +91****7890
```

### 8.3 Rate Limiting (Per IP + Per User)

```
OTP initiation:  Max 3 requests per 10 minutes per user
OTP initiation:  Max 10 requests per hour per IP
OTP verify:      Max 3 attempts per ledger record
Breach:          429 Too Many Requests + emit rate_limit_breach event
```

---

## 9. EVENT BUS EMISSIONS (KAFKA)

```
Event                   Trigger                         Consumers
──────────────────────────────────────────────────────────────────────────────
phone.append.initiated  New pending record created      Notification Service
phone.otp.issued        OTP dispatched via SMS          Analytics Service
phone.otp.failed        OTP mismatch or expired         Analytics · Fraud Detection
phone.verified          OTP confirmed, record verified  Auth Service · Session Service
phone.gate.blocked      Session event blocked           Analytics · Admin Governance
phone.rate.breach       Rate limit exceeded             Fraud Detection · Wazuh SIEM
phone.admin.override    Admin append executed           Admin Governance · Audit Log
```

All events follow the global AsyncAPI event schema registry (R4).
Unknown event type → **STOP EXECUTION → REPORT SCHEMA_VIOLATION**

---

## 10. SECURITY ENFORCEMENT

```
PII Encryption:       phone_e164 encrypted at rest (AES-256, key via HashiCorp Vault)
Transit Encryption:   TLS 1.3 minimum for all phone-bearing API calls
RBAC:                 Only Auth Service role may write to user_phone_ledger
Audit Immutability:   Phone lifecycle audit log = WORM (Write Once Read Many)
Tenant Isolation:     Row-level security — cross-tenant phone access = forbidden
Fraud Detection:      Flag if same phone number appears across >1 tenant
SIEM:                 All phone gate violations streamed to Wazuh
WAF Rule:             ModSecurity blocks phone endpoint enumeration patterns
```

---

## 11. FAILURE HANDLING (DETERMINISTIC)

```
FAILURE                          AGENT ACTION
──────────────────────────────────────────────────────────────────
SMS delivery failure             Retry x2 → fallback ntfy push → emit sms_delivery_failed
OTP expired before use           Ledger record → superseded → user must re-initiate
Max OTP attempts reached         Ledger record → blocked → 15-minute cooldown enforced
Database write failure           Transaction rollback → emit phone_append_db_error → STOP
Phone collision detected         STOP APPEND → emit phone_collision_conflict → return 409
E.164 format rejection           Return 422 with explicit format guidance → no ledger write
Session gate blocked             Return 403 + reason code → no silent fallback
```

No retry discretion. No soft failures. No silent fallbacks.

---

## 12. OBSERVABILITY (MANDATORY)

```
Prometheus Metrics:
  ecoskiller_phone_otp_issued_total          (counter)
  ecoskiller_phone_otp_verified_total        (counter)
  ecoskiller_phone_otp_failed_total          (counter, labels: reason)
  ecoskiller_phone_gate_blocked_total        (counter, labels: session_event)
  ecoskiller_phone_append_total              (counter, labels: append_reason)
  ecoskiller_phone_rate_breach_total         (counter)

Loki Logs:
  All phone lifecycle events → structured JSON log
  Fields: user_id (hashed), event_type, timestamp, result, reason

Grafana Dashboard:
  Panel: OTP success/failure rate (last 24h)
  Panel: Phone gate block rate by session event
  Panel: Rate limit breach frequency
  Panel: SMS delivery failure rate
  Alert: Gate block rate > 5% → WARN
  Alert: OTP failure rate > 20% → CRITICAL
```

---

## 13. MULTI-TENANT ENFORCEMENT

```
Every phone_ledger record carries tenant_id (from JWT claim).
Cross-tenant phone queries are rejected at OPA policy layer.
Admin override requires admin's tenant scope to match user's tenant.
Global admin (platform-level) override is audited separately.
```

---

## 14. GDPR / PRIVACY COMPLIANCE

```
Right to Erasure:   phone_e164 field → anonymized to NULL (ledger structure retained)
Data Export:        Phone ledger included in GDPR data export (masked format only)
Consent:            Phone collection covered under platform terms — consent captured at registration
Retention Policy:   Superseded / blocked records retained for 7 years (legal compliance)
Audit Retention:    Phone audit log retained for 15 years (WORM archive)
```

---

## 15. ANTIGRAVITY GENERATION RULES (LOCKED)

```
Antigravity MUST generate:
  ✔ user_phone_ledger migration file (Flyway versioned)
  ✔ PhoneAppendService (Auth Service microservice module)
  ✔ OTPEngine (internal sub-module, no external exposure)
  ✔ PhoneGateMiddleware (applied at Session Lifecycle Manager)
  ✔ /auth/phone/initiate endpoint
  ✔ /auth/phone/verify endpoint
  ✔ /auth/phone/status endpoint
  ✔ /admin/phone/override endpoint
  ✔ Kafka event emitters for all 8 phone events
  ✔ Prometheus metric registrations
  ✔ Loki structured log hooks
  ✔ Grafana dashboard JSON (phone panel group)
  ✔ OPA policy: phone-gate enforcement rules
  ✔ Unit tests: OTP verify, gate enforcement, append rules
  ✔ Integration tests: full lifecycle (register → verify → gate → session)

Antigravity MUST NOT generate:
  ✗ Any UPDATE endpoint for phone_e164
  ✗ Any phone field in mutable user profile PATCH endpoints
  ✗ Any session issuance path that bypasses phone_gate_middleware
  ✗ Any OTP storage as plaintext
  ✗ Any full phone number in API response bodies
```

Absence of any required generation item → **STOP EXECUTION → REPORT PHONE_AGENT_INCOMPLETE**

---

## 16. CONTRACT GATE REGISTRATION

```
This agent produces the following contract gates upon completion:

  phone_schema_ready          → consumed by: Auth Service, Session Service, Billing Service
  phone_gate_middleware_ready → consumed by: GD Orchestrator, Dojo Engine, Interview Service
  otp_engine_ready            → consumed by: Notification Service, Fraud Detection
  phone_audit_ready           → consumed by: Admin Governance, Compliance Service

All downstream services requiring phone gating MUST declare dependency on:
  phone_gate_middleware_ready
```

---

## ✅ FINAL ENFORCEMENT SUMMARY

```
PHONE_APPEND_ONLY_ENFORCEMENT_AGENT
  ├── Phone numbers: APPEND-ONLY · NEVER editable · NEVER deletable
  ├── Format: E.164 ONLY · rejected at ingress if invalid
  ├── OTP: 6-digit · bcrypt-hashed · 300s TTL · 3 attempt max
  ├── Session Gates: ALL RISK_EVENTS gated · NO silent bypass
  ├── Ledger: Immutable PostgreSQL table · RLS enforced · App role UPDATE/DELETE revoked
  ├── API: Masked output only · Rate limited · RBAC protected
  ├── Events: 8 Kafka events · AsyncAPI schema enforced
  ├── Observability: Prometheus + Loki + Grafana · Wazuh SIEM
  ├── Privacy: Encrypted at rest · GDPR compliant · 7-year retention
  └── Antigravity: Fully specified output list · No interpretation authority

EXECUTION_ENGINE    = ANTIGRAVITY
CHANGE_POLICY       = APPEND_ONLY
READY_FOR           = ANTIGRAVITY_PRODUCTION
STATUS              = SEALED · LOCKED · GOVERNED · DETERMINISTIC
```

---

*Ecoskiller · PHONE_APPEND_ONLY_ENFORCEMENT_AGENT · v1.0 · SEALED*
*Mutation requires version bump and Human declaration only.*
