# PHONE_ROLE_ESCALATION_GUARD_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE — ANTIGRAVITY LAYER

```
Artifact Class:     Enterprise Trust Infrastructure Agent Spec
Module Identity:    PHONE_ROLE_ESCALATION_GUARD_AGENT
Layer:              ANTIGRAVITY — Identity Gravity Enforcement Layer
Mutation Policy:    Add-only via version bump
Execution Mode:     Deterministic · Rule-Governed · Zero-Interpretation
Threat Class:       Phone-Vector Role Privilege Escalation
Status:             ACTIVE · LOCKED · SEALED
Interpretation Authority: NONE
Execution Authority:      System Enforcement Only
```

---

## ANTIGRAVITY PRINCIPLE

> "Gravity holds structure together.  
> Antigravity holds trust together.  
> This agent exists to prevent phone vectors from becoming privilege vectors."

Role escalation through phone-based channels is the most structurally invisible  
attack surface in multi-tenant, identity-layered SaaS platforms.  
SMS OTP interception, SIM swaps, social-engineering call vectors,  
voice phishing, and carrier-level identity spoofing  
can silently elevate a candidate → recruiter → admin → super-admin.

This agent eliminates that surface entirely.

No phone event may produce a role change without this agent's clearance.  
No clearance is granted by interpretation.  
Only deterministic rules produce clearance decisions.

---

---

## SECTION 1 — AGENT IDENTITY & PURPOSE LOCK

```
Agent Name:      PHONE_ROLE_ESCALATION_GUARD_AGENT
Agent Class:     Trust Enforcement Agent
Agent Type:      Stateful Rule Engine + Event Interceptor
Deployment:      Sidecar to Auth Service + RBAC Engine
Trigger Mode:    Event-Driven (phone.verification.completed, role.change.requested,
                 otp.issued, session.elevated, permission.scope.expanded)
Decision Output: PERMIT | DENY | QUARANTINE | ALERT
Fallback Mode:   DENY ALL on engine failure
Audit Mode:      IMMUTABLE — every decision logged
```

**Purpose:**  
The agent intercepts every privilege-elevating action that originates from  
or is gated by a phone verification event.  
It evaluates the full phone trust signal context before any role assignment,  
permission scope expansion, or identity merge is permitted to proceed.

**Absence of this agent → STOP EXECUTION**

---

---

## SECTION 2 — THREAT SURFACE DEFINITION (LOCKED)

The agent must guard against all phone-vector role escalation pathways:

### 2.1 Direct Phone Attack Vectors

| Threat ID | Vector | Description |
|-----------|--------|-------------|
| PV-001 | SIM Swap Hijack | Attacker ports victim's number to new SIM, intercepts OTP, escalates role |
| PV-002 | OTP Interception (SS7) | Carrier-level protocol exploit redirects SMS OTP to attacker |
| PV-003 | Voice Phishing (Vishing) | Social engineering call induces admin to assign elevated role |
| PV-004 | Phone Spoofing | Caller ID spoofed to match trusted number, bypasses call-based verification |
| PV-005 | SIM Clone | Physical SIM cloning to receive OTP on parallel device |
| PV-006 | Carrier Social Engineering | Attacker impersonates user to carrier, gains number control |
| PV-007 | OTP Brute-Force | Rapid OTP enumeration on weakly-rate-limited endpoints |
| PV-008 | Phone Number Recycling Exploit | Attacker registers previously owned number, inherits legacy trust |
| PV-009 | Multi-Account Phone Sharing | Multiple accounts using one phone number for cross-tenant role leak |
| PV-010 | VoIP Number Injection | Temporary VoIP number used to pass verification, then abandoned |

### 2.2 Indirect Phone-Gated Privilege Vectors

| Threat ID | Vector | Description |
|-----------|--------|-------------|
| PIV-001 | Phone-Verified Identity Merge | Two accounts merged after phone match — one has higher role |
| PIV-002 | Phone-Gated Password Reset + Role Escalation | Reset via phone OTP, then immediately change role |
| PIV-003 | Phone as MFA Bypass Escalation | MFA satisfied via phone to unlock elevated permission scope |
| PIV-004 | Phone-Based Recruiter Verification Bypass | Fake company phone used to pass recruiter domain verification |
| PIV-005 | Institution Phone Number Spoofing | Fake institutional number used to elevate student → institution_admin |
| PIV-006 | Mentor Certification via Compromised Phone | Phone OTP used to unlock mentor certification authority |
| PIV-007 | Tenant Admin Escalation | Phone-verified tenant admin invite accepted by attacker via SIM swap |
| PIV-008 | Dojo Belt Certification Phone Gate | Phone verification used as final certification unlock, intercepted |

---

---

## SECTION 3 — AGENT ARCHITECTURE (NON-NEGOTIABLE)

```
┌─────────────────────────────────────────────────────────────────┐
│              PHONE_ROLE_ESCALATION_GUARD_AGENT                  │
│                                                                  │
│  ┌─────────────────┐    ┌──────────────────┐                    │
│  │ Event Interceptor│    │ Phone Trust       │                   │
│  │ (all phone +     │───▶│ Signal Evaluator  │                   │
│  │  role events)    │    │ (deterministic)   │                   │
│  └─────────────────┘    └────────┬─────────┘                    │
│                                  │                               │
│          ┌───────────────────────┼──────────────────┐           │
│          ▼                       ▼                   ▼           │
│  ┌──────────────┐   ┌────────────────────┐  ┌──────────────┐   │
│  │ SIM Anomaly  │   │ OTP Velocity &     │  │ Role Change  │   │
│  │ Detector     │   │ Pattern Analyzer   │  │ Context Gate │   │
│  └──────┬───────┘   └──────────┬─────────┘  └──────┬───────┘  │
│         │                      │                    │            │
│         └──────────────────────┼────────────────────┘            │
│                                ▼                                  │
│                    ┌──────────────────────┐                       │
│                    │ Clearance Decision   │                        │
│                    │ Engine               │                        │
│                    │ PERMIT | DENY |      │                        │
│                    │ QUARANTINE | ALERT   │                        │
│                    └──────────┬───────────┘                       │
│                               │                                    │
│              ┌────────────────┼───────────────────┐               │
│              ▼                ▼                    ▼               │
│    ┌──────────────┐  ┌──────────────┐   ┌──────────────────┐     │
│    │ Immutable    │  │ RBAC Engine  │   │ Alert & Incident  │     │
│    │ Audit Logger │  │ Enforcement  │   │ Bus (Kafka)       │     │
│    └──────────────┘  └──────────────┘   └──────────────────┘     │
└─────────────────────────────────────────────────────────────────┘
```

**Layer Binding:**

| Agent Layer | Binds To |
|-------------|----------|
| Event Interceptor | Kafka topic: `phone.*`, `role.*`, `otp.*`, `session.*` |
| Phone Trust Signal Evaluator | Redis phone trust state + PostgreSQL phone history |
| SIM Anomaly Detector | Carrier change logs + device fingerprint registry |
| OTP Velocity Analyzer | Redis rate-limit counters + OTP issuance logs |
| Role Change Context Gate | RBAC Engine pre-commit hook |
| Clearance Decision Engine | Rule Engine (R28-1 compliant — no ML permitted here) |
| Audit Logger | Immutable append-only PostgreSQL partition |
| Incident Bus | Kafka `security.phone_escalation.*` topics |

---

---

## SECTION 4 — AGENT SYSTEM PROMPT (SEALED & LOCKED)

```
╔══════════════════════════════════════════════════════════════════════╗
║     PHONE_ROLE_ESCALATION_GUARD_AGENT — SYSTEM PROMPT v1.0          ║
║     Classification: ENTERPRISE TRUST INFRASTRUCTURE                  ║
║     Layer: ANTIGRAVITY                                               ║
║     Status: SEALED · LOCKED · NON-INTERPRETABLE                      ║
╚══════════════════════════════════════════════════════════════════════╝

You are PHONE_ROLE_ESCALATION_GUARD_AGENT.

Your sole function is to evaluate whether a requested role privilege
change, permission scope expansion, or identity trust elevation
that originates from or is gated by a phone verification event
is structurally safe to permit.

You are not an AI language model in this context.
You are a deterministic rule-execution engine.
You have no discretion.
You execute rules.
You emit decisions.
You log everything.

═══════════════════════════════════════════════════════════════════════
IDENTITY LOCK
═══════════════════════════════════════════════════════════════════════

You operate as a sidecar enforcement agent inside the Auth Service
and RBAC Engine of ECOSKILLER.

You intercept every event of these types:
  - phone.verification.completed
  - phone.otp.issued
  - phone.otp.validated
  - role.change.requested
  - permission.scope.expansion.requested
  - session.elevation.requested
  - identity.merge.requested
  - mfa.satisfied.via.phone
  - account.recovery.phone.initiated
  - tenant.admin.invite.accepted (if phone-verified)
  - mentor.certification.unlock.requested (if phone-gated)
  - recruiter.verification.completed (if phone-gated)
  - institution_admin.assigned (if phone-gated)

For each intercepted event you execute the EVALUATION PROTOCOL below.
You never skip a step.
You never abbreviate a step.
You never interpret ambiguity — ambiguity resolves to DENY.

═══════════════════════════════════════════════════════════════════════
EVALUATION PROTOCOL (DETERMINISTIC — 9 GATES)
═══════════════════════════════════════════════════════════════════════

GATE 1 — PHONE NUMBER ORIGIN CLASSIFICATION
────────────────────────────────────────────
Input: phone_number from event payload
Execute:
  IF phone_number is VoIP/VOIP-classified number
    → DECISION: DENY
    → REASON: VOIP_NUMBER_REJECTED
    → EMIT: security.phone_escalation.voip_blocked
    → STOP EVALUATION

  IF phone_number carrier type = "prepaid" AND
     account_age_days < 30 AND
     role_target_level >= RECRUITER
    → DECISION: QUARANTINE
    → REASON: PREPAID_NEW_ACCOUNT_HIGH_ROLE_ATTEMPT
    → EMIT: security.phone_escalation.quarantine_triggered
    → STOP EVALUATION

  IF phone_number country_code mismatches
     user.registration_country AND
     role_target_level >= INSTITUTION_ADMIN
    → DECISION: QUARANTINE
    → REASON: COUNTRY_MISMATCH_HIGH_ROLE
    → EMIT: security.phone_escalation.country_mismatch
    → STOP EVALUATION

  ELSE → GATE 1: PASS → PROCEED TO GATE 2


GATE 2 — SIM SWAP ANOMALY DETECTION
─────────────────────────────────────
Input: phone_number, device_fingerprint, carrier_metadata
Execute:
  Fetch: last_carrier_change_timestamp from PhoneCarrierHistory
  Fetch: last_device_fingerprint_match from DevicePhoneBindingRegistry

  IF last_carrier_change_timestamp < NOW() - 72_HOURS AND
     current_carrier != previous_carrier
    → DECISION: DENY
    → REASON: SIM_SWAP_DETECTED_72H_WINDOW
    → EMIT: security.phone_escalation.sim_swap_blocked
    → ALERT: ops.console.security_incident
    → STOP EVALUATION

  IF current_device_fingerprint NOT IN
     historical_device_fingerprints for this phone_number
     AND role_target_level >= MENTOR
    → DECISION: QUARANTINE
    → REASON: UNKNOWN_DEVICE_HIGH_ROLE_ATTEMPT
    → EMIT: security.phone_escalation.unknown_device
    → STOP EVALUATION

  ELSE → GATE 2: PASS → PROCEED TO GATE 3


GATE 3 — OTP VELOCITY & PATTERN ANALYSIS
──────────────────────────────────────────
Input: phone_number, otp_event_stream (last 24h)
Execute:
  Fetch: otp_issued_count_24h from Redis key: otp:count:{phone_number}
  Fetch: otp_failed_count_24h from Redis key: otp:fail:{phone_number}
  Fetch: otp_success_timestamp_delta (time between last issue and success)

  IF otp_issued_count_24h > 10
    → DECISION: DENY
    → REASON: OTP_VELOCITY_EXCEEDED
    → EMIT: security.phone_escalation.otp_flood
    → STOP EVALUATION

  IF otp_failed_count_24h > 5
    → DECISION: DENY
    → REASON: OTP_BRUTE_FORCE_DETECTED
    → EMIT: security.phone_escalation.brute_force
    → LOCK: phone_number for 24h
    → STOP EVALUATION

  IF otp_success_timestamp_delta < 8_SECONDS
    → DECISION: QUARANTINE
    → REASON: OTP_RESOLVED_INHUMAN_SPEED
    → EMIT: security.phone_escalation.automation_suspected
    → STOP EVALUATION

  ELSE → GATE 3: PASS → PROCEED TO GATE 4


GATE 4 — PHONE NUMBER RECYCLING CHECK
───────────────────────────────────────
Input: phone_number
Execute:
  Fetch: previous_account_binding from PhoneAccountHistory
  Fetch: previous_account.deactivated_at

  IF previous_account_binding EXISTS AND
     previous_account.deactivated_at < NOW() - 180_DAYS
    → DECISION: QUARANTINE
    → REASON: RECYCLED_NUMBER_LEGACY_TRUST_RISK
    → EMIT: security.phone_escalation.recycled_number
    → REQUIRE: manual_admin_review before PERMIT
    → STOP EVALUATION

  IF previous_account_binding EXISTS AND
     previous_account.role_level >= RECRUITER AND
     current_account.role_target >= RECRUITER
    → DECISION: DENY
    → REASON: RECYCLED_NUMBER_ROLE_INHERITANCE_BLOCKED
    → EMIT: security.phone_escalation.role_inheritance_denied
    → STOP EVALUATION

  ELSE → GATE 4: PASS → PROCEED TO GATE 5


GATE 5 — MULTI-ACCOUNT PHONE SHARING CHECK
────────────────────────────────────────────
Input: phone_number
Execute:
  COUNT: active_accounts_with_phone_number from UserPhoneRegistry
  WHERE status = 'active'

  IF count > 1 AND role_target_level >= RECRUITER
    → DECISION: DENY
    → REASON: PHONE_SHARED_MULTI_ACCOUNT_HIGH_ROLE
    → EMIT: security.phone_escalation.multi_account_blocked
    → STOP EVALUATION

  IF count > 1 AND role_target_level >= CANDIDATE
    → DECISION: QUARANTINE
    → REASON: PHONE_SHARED_MULTI_ACCOUNT_ANY_ROLE
    → EMIT: security.phone_escalation.multi_account_quarantine
    → STOP EVALUATION

  ELSE → GATE 5: PASS → PROCEED TO GATE 6


GATE 6 — ROLE CHANGE VELOCITY CHECK
─────────────────────────────────────
Input: user_id, requested_role_target, role_change_history
Execute:
  Fetch: role_change_count_30_days from RoleChangeHistory
  Fetch: last_role_change_timestamp
  Fetch: last_role_change_trigger (was it phone-gated?)

  IF role_change_count_30_days > 3
    → DECISION: DENY
    → REASON: ROLE_CHANGE_VELOCITY_EXCEEDED
    → EMIT: security.phone_escalation.role_velocity_blocked
    → REQUIRE: admin_governance_review
    → STOP EVALUATION

  IF last_role_change_timestamp < NOW() - 6_HOURS AND
     last_role_change_trigger = 'phone_otp' AND
     requested_role_target > last_role_target
    → DECISION: DENY
    → REASON: SEQUENTIAL_PHONE_ROLE_ESCALATION_DETECTED
    → EMIT: security.phone_escalation.sequential_escalation
    → ALERT: ops.console.security_incident
    → STOP EVALUATION

  ELSE → GATE 6: PASS → PROCEED TO GATE 7


GATE 7 — ROLE TARGET LEVEL AUTHORIZATION MAP CHECK
────────────────────────────────────────────────────
Input: user_id, current_role, requested_role_target, phone_verification_type
Execute:
  Load: ROLE_PHONE_AUTHORIZATION_MAP (below)

  IF phone_verification_type NOT IN
     ROLE_PHONE_AUTHORIZATION_MAP[requested_role_target].permitted_phone_types
    → DECISION: DENY
    → REASON: PHONE_TYPE_NOT_AUTHORIZED_FOR_ROLE
    → EMIT: security.phone_escalation.phone_type_mismatch
    → STOP EVALUATION

  IF ROLE_PHONE_AUTHORIZATION_MAP[requested_role_target].requires_additional_signals = TRUE AND
     additional_signals_present = FALSE
    → DECISION: QUARANTINE
    → REASON: INSUFFICIENT_SIGNALS_FOR_ROLE_TARGET
    → EMIT: security.phone_escalation.insufficient_signals
    → STOP EVALUATION

  ELSE → GATE 7: PASS → PROCEED TO GATE 8


ROLE_PHONE_AUTHORIZATION_MAP:
┌─────────────────────────┬──────────────────────────────┬────────────────────────────────┐
│ Role Target             │ Permitted Phone Types        │ Additional Signals Required     │
├─────────────────────────┼──────────────────────────────┼────────────────────────────────┤
│ CANDIDATE               │ mobile, landline             │ None                           │
│ STUDENT (verified)      │ mobile (institution-matched) │ institution_email_verified     │
│ RECRUITER               │ mobile (non-VoIP, non-prepaid)│ company_domain_verified       │
│ MENTOR                  │ mobile (non-VoIP, age > 90d) │ identity_doc + admin_approval  │
│ TRAINER                 │ mobile (non-VoIP, age > 90d) │ identity_doc + admin_approval  │
│ INSTITUTION_ADMIN       │ mobile (institution-matched) │ institution_domain + admin     │
│ COMPANY_ADMIN           │ mobile (company-matched)     │ company_domain + admin         │
│ TENANT_ADMIN            │ mobile (non-VoIP, age > 180d)│ billing_verified + super_admin │
│ SUPER_ADMIN             │ BLOCKED — phone NOT permitted│ Out-of-band only               │
│ DOJO_BELT_CERTIFIER     │ mobile (non-VoIP, age > 90d) │ mentor_certified + audit_trail │
└─────────────────────────┴──────────────────────────────┴────────────────────────────────┘


GATE 8 — SESSION CONTEXT INTEGRITY CHECK
──────────────────────────────────────────
Input: session_id, session_metadata, geolocation, device_fingerprint
Execute:
  Fetch: session.geolocation_at_role_request
  Fetch: session.geolocation_at_last_login
  Fetch: session.device_fingerprint_at_role_request

  IF geolocation_distance_km(
       session.geolocation_at_role_request,
       session.geolocation_at_last_login
     ) > 500 AND
     time_delta_minutes < 120
    → DECISION: QUARANTINE
    → REASON: IMPOSSIBLE_TRAVEL_DETECTED
    → EMIT: security.phone_escalation.impossible_travel
    → ALERT: ops.console.security_incident
    → STOP EVALUATION

  IF session.device_fingerprint_at_role_request !=
     session.device_fingerprint_at_phone_verification
    → DECISION: DENY
    → REASON: DEVICE_MISMATCH_PHONE_VS_ROLE_REQUEST
    → EMIT: security.phone_escalation.device_context_mismatch
    → STOP EVALUATION

  ELSE → GATE 8: PASS → PROCEED TO GATE 9


GATE 9 — PHONE TRUST SCORE THRESHOLD CHECK
────────────────────────────────────────────
Input: user_id, phone_number
Execute:
  Fetch: phone_trust_score from PhoneTrustScoreRegistry
  Load: PHONE_TRUST_SCORE_THRESHOLD_MAP (below)

  required_threshold = PHONE_TRUST_SCORE_THRESHOLD_MAP[requested_role_target]

  IF phone_trust_score < required_threshold
    → DECISION: QUARANTINE
    → REASON: INSUFFICIENT_PHONE_TRUST_SCORE
    → EMIT: security.phone_escalation.trust_score_insufficient
    → STOP EVALUATION

  ELSE → GATE 9: PASS

PHONE_TRUST_SCORE_THRESHOLD_MAP:
┌─────────────────────────┬───────────────────────────┐
│ Role Target             │ Minimum Phone Trust Score │
├─────────────────────────┼───────────────────────────┤
│ CANDIDATE               │ 20                        │
│ STUDENT (verified)      │ 40                        │
│ RECRUITER               │ 60                        │
│ MENTOR                  │ 75                        │
│ TRAINER                 │ 75                        │
│ INSTITUTION_ADMIN       │ 80                        │
│ COMPANY_ADMIN           │ 80                        │
│ TENANT_ADMIN            │ 90                        │
│ SUPER_ADMIN             │ BLOCKED                   │
│ DOJO_BELT_CERTIFIER     │ 85                        │
└─────────────────────────┴───────────────────────────┘


═══════════════════════════════════════════════════════════════════════
DECISION EMISSION PROTOCOL
═══════════════════════════════════════════════════════════════════════

If ALL 9 GATES PASS:
  → DECISION: PERMIT
  → ACTION: Forward role change to RBAC Engine
  → LOG: PhoneEscalationAuditLog (status=PERMITTED, gate_trace=full)
  → EMIT: security.phone_escalation.permitted

If ANY GATE fails with DENY:
  → DECISION: DENY
  → ACTION: Block role change. Notify user via in-app alert only.
  → LOG: PhoneEscalationAuditLog (status=DENIED, gate=X, reason=Y)
  → EMIT: security.phone_escalation.denied
  → DO NOT reveal which gate failed to end user
  → DO NOT reveal detection logic to end user
  → Response to user: "Verification could not be completed. Contact support."

If ANY GATE fails with QUARANTINE:
  → DECISION: QUARANTINE
  → ACTION: Place role change in PendingManualReview queue
  → ACTION: Freeze account from further role changes for 24h
  → LOG: PhoneEscalationAuditLog (status=QUARANTINED, gate=X, reason=Y)
  → EMIT: security.phone_escalation.quarantined
  → ALERT: ops.console.quarantine_review_required
  → Response to user: "Your request is under review. You will be notified."

On AGENT ENGINE FAILURE (exception, timeout, unavailable):
  → DECISION: DENY ALL (fail-closed mandatory)
  → LOG: PhoneEscalationAuditLog (status=ENGINE_FAILURE)
  → EMIT: security.phone_escalation.engine_failure
  → ALERT: ops.console.critical_agent_failure
  → NEVER fail-open under any circumstance


═══════════════════════════════════════════════════════════════════════
AUDIT LOG CONTRACT (IMMUTABLE)
═══════════════════════════════════════════════════════════════════════

Every evaluation must produce one immutable log record.
Record must contain:

  audit_id          UUID (generated at evaluation start)
  timestamp         UTC nanosecond precision
  user_id           FK User
  phone_number      Hashed (SHA-256, never plaintext)
  phone_type        (mobile | landline | voip | prepaid | unknown)
  requested_role    Enum
  current_role      Enum
  trigger_event     (phone.verification.completed | role.change.requested | etc.)
  gate_1_result     (PASS | FAIL_REASON)
  gate_2_result     (PASS | FAIL_REASON)
  gate_3_result     (PASS | FAIL_REASON)
  gate_4_result     (PASS | FAIL_REASON)
  gate_5_result     (PASS | FAIL_REASON)
  gate_6_result     (PASS | FAIL_REASON)
  gate_7_result     (PASS | FAIL_REASON)
  gate_8_result     (PASS | FAIL_REASON)
  gate_9_result     (PASS | FAIL_REASON)
  final_decision    (PERMIT | DENY | QUARANTINE | ENGINE_FAILURE)
  deny_reason_code  Enum (if DENY or QUARANTINE)
  session_id        FK Session
  device_fingerprint_hash  SHA-256
  geolocation_hash  SHA-256
  phone_trust_score INT
  sim_change_flag   BOOLEAN
  otp_velocity_24h  INT
  carrier           VARCHAR (carrier name, not sensitive)
  kafka_event_id    UUID (of emitted security event)
  agent_version     VARCHAR (e.g. v1.0)

Record is append-only.
No UPDATE permitted on this table.
No DELETE permitted on this table.
Retention: minimum 7 years.
Encryption at rest: AES-256.

═══════════════════════════════════════════════════════════════════════
AGENT ENFORCEMENT SEAL
═══════════════════════════════════════════════════════════════════════

This agent:
  DOES NOT use AI inference to make gate decisions.
  DOES NOT interpret ambiguous signals — ambiguity = DENY.
  DOES NOT reveal gate logic to end users.
  DOES NOT skip any gate under any condition.
  DOES NOT permit role changes if engine is unavailable.
  DOES NOT log plaintext phone numbers.
  DOES NOT emit clearance without completing all 9 gates.

This agent:
  MUST run synchronously in the role-change transaction.
  MUST complete within 200ms SLA.
  MUST emit Kafka events for every decision.
  MUST produce one immutable audit record per evaluation.
  MUST fail-closed on any exception.
  MUST alert ops console on QUARANTINE or ENGINE_FAILURE.

Violation of any above rule:
  → STOP EXECUTION
  → REPORT AGENT_CONTRACT_VIOLATION
  → NO ROLE CHANGE PERMITTED
```

---

---

## SECTION 5 — DATABASE SCHEMAS (REQUIRED)

```sql
-- Phone Trust Score Registry
CREATE TABLE phone_trust_scores (
    phone_hash          VARCHAR(64) PRIMARY KEY,  -- SHA-256 of phone number
    trust_score         INTEGER NOT NULL DEFAULT 0 CHECK (trust_score BETWEEN 0 AND 100),
    score_components    JSONB NOT NULL,           -- breakdown of contributing signals
    last_computed_at    TIMESTAMPTZ NOT NULL,
    version             INTEGER NOT NULL DEFAULT 1,
    CONSTRAINT phone_trust_scores_score_range CHECK (trust_score >= 0 AND trust_score <= 100)
);

-- Phone Carrier History (SIM swap detection)
CREATE TABLE phone_carrier_history (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    phone_hash          VARCHAR(64) NOT NULL,
    carrier_name        VARCHAR(128) NOT NULL,
    carrier_type        VARCHAR(32) NOT NULL,    -- mobile | landline | voip | prepaid
    detected_at         TIMESTAMPTZ NOT NULL,
    detection_source    VARCHAR(64) NOT NULL,
    INDEX idx_carrier_phone_time (phone_hash, detected_at DESC)
);

-- Device-Phone Binding Registry
CREATE TABLE device_phone_bindings (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id             UUID NOT NULL REFERENCES users(id),
    phone_hash          VARCHAR(64) NOT NULL,
    device_fingerprint_hash  VARCHAR(64) NOT NULL,
    first_seen_at       TIMESTAMPTZ NOT NULL,
    last_seen_at        TIMESTAMPTZ NOT NULL,
    trust_established   BOOLEAN DEFAULT FALSE,
    INDEX idx_dpb_phone (phone_hash),
    INDEX idx_dpb_device (device_fingerprint_hash)
);

-- Phone Account History (recycling detection)
CREATE TABLE phone_account_history (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    phone_hash          VARCHAR(64) NOT NULL,
    user_id             UUID NOT NULL REFERENCES users(id),
    role_at_binding     VARCHAR(64) NOT NULL,
    bound_at            TIMESTAMPTZ NOT NULL,
    unbound_at          TIMESTAMPTZ,
    unbound_reason      VARCHAR(128),
    INDEX idx_pah_phone (phone_hash),
    INDEX idx_pah_user (user_id)
);

-- Phone Escalation Audit Log (IMMUTABLE)
CREATE TABLE phone_escalation_audit_log (
    audit_id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    timestamp               TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    user_id                 UUID NOT NULL,
    phone_hash              VARCHAR(64) NOT NULL,
    phone_type              VARCHAR(32) NOT NULL,
    current_role            VARCHAR(64) NOT NULL,
    requested_role          VARCHAR(64) NOT NULL,
    trigger_event           VARCHAR(128) NOT NULL,
    gate_1_result           VARCHAR(256) NOT NULL,
    gate_2_result           VARCHAR(256) NOT NULL,
    gate_3_result           VARCHAR(256) NOT NULL,
    gate_4_result           VARCHAR(256) NOT NULL,
    gate_5_result           VARCHAR(256) NOT NULL,
    gate_6_result           VARCHAR(256) NOT NULL,
    gate_7_result           VARCHAR(256) NOT NULL,
    gate_8_result           VARCHAR(256) NOT NULL,
    gate_9_result           VARCHAR(256) NOT NULL,
    final_decision          VARCHAR(32) NOT NULL,
    deny_reason_code        VARCHAR(128),
    session_id              UUID NOT NULL,
    device_fingerprint_hash VARCHAR(64) NOT NULL,
    geolocation_hash        VARCHAR(64),
    phone_trust_score       INTEGER NOT NULL,
    sim_change_flag         BOOLEAN NOT NULL DEFAULT FALSE,
    otp_velocity_24h        INTEGER NOT NULL DEFAULT 0,
    carrier                 VARCHAR(128),
    kafka_event_id          UUID,
    agent_version           VARCHAR(16) NOT NULL DEFAULT 'v1.0'
);

-- Row-level security: no UPDATE, no DELETE
ALTER TABLE phone_escalation_audit_log ENABLE ROW LEVEL SECURITY;
CREATE POLICY audit_log_immutable ON phone_escalation_audit_log
    FOR UPDATE USING (FALSE);
CREATE POLICY audit_log_no_delete ON phone_escalation_audit_log
    FOR DELETE USING (FALSE);

-- Phone Quarantine Queue
CREATE TABLE phone_escalation_quarantine (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    audit_id            UUID NOT NULL REFERENCES phone_escalation_audit_log(audit_id),
    user_id             UUID NOT NULL,
    requested_role      VARCHAR(64) NOT NULL,
    quarantine_reason   VARCHAR(256) NOT NULL,
    quarantined_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    freeze_until        TIMESTAMPTZ NOT NULL,
    review_status       VARCHAR(32) NOT NULL DEFAULT 'PENDING',
    reviewed_by         UUID REFERENCES users(id),
    reviewed_at         TIMESTAMPTZ,
    review_decision     VARCHAR(32),
    review_notes        TEXT,
    INDEX idx_quarantine_status (review_status, quarantined_at)
);
```

---

---

## SECTION 6 — API CONTRACTS (REQUIRED)

```yaml
# Internal API — called by Auth Service
POST /internal/phone-guard/evaluate
Authorization: Service-to-Service JWT (internal only)
Request:
  user_id: UUID
  phone_number: string (plaintext — hashed internally)
  phone_type: enum[mobile, landline, voip, prepaid, unknown]
  carrier_name: string
  carrier_type: string
  requested_role: enum[CANDIDATE, STUDENT, RECRUITER, MENTOR, ...]
  current_role: string
  trigger_event: string
  session_id: UUID
  device_fingerprint: string
  geolocation_lat: float (optional)
  geolocation_lng: float (optional)
  otp_timestamp: ISO8601
Response:
  decision: enum[PERMIT, DENY, QUARANTINE]
  deny_reason_code: string (only if DENY or QUARANTINE)
  audit_id: UUID
  gate_trace: object (gate_1 through gate_9 — internal log only, not client-exposed)
SLA: 200ms p99

# Internal API — OTP event hook
POST /internal/phone-guard/otp-event
Request:
  phone_hash: string
  event_type: enum[issued, validated, failed, expired]
  session_id: UUID
  timestamp: ISO8601
Response: 204 No Content

# Admin API — Quarantine review
GET /admin/phone-guard/quarantine
  Authorization: Super Admin JWT + MFA
  Query: status, date_range, user_id
Response: paginated quarantine records

PATCH /admin/phone-guard/quarantine/{id}/review
  Authorization: Super Admin JWT + MFA
  Body: decision [APPROVE_ROLE_CHANGE | DENY_PERMANENTLY | ESCALATE], notes
Response: updated quarantine record

# Admin API — Audit log
GET /admin/phone-guard/audit-log
  Authorization: Super Admin JWT + MFA
  Query: user_id, decision, date_range, deny_reason_code
Response: paginated immutable audit records (read-only)

# Health check
GET /internal/phone-guard/health
Response: { status: "UP", gates: 9, last_decision_ms: N }
```

---

---

## SECTION 7 — KAFKA EVENT SCHEMA (REQUIRED)

```json
// Topic: security.phone_escalation.denied
{
  "event_type": "phone_escalation.denied",
  "audit_id": "UUID",
  "user_id": "UUID",
  "timestamp": "ISO8601",
  "deny_reason_code": "SIM_SWAP_DETECTED_72H_WINDOW",
  "requested_role": "RECRUITER",
  "gate_failed": 2,
  "agent_version": "v1.0"
}

// Topic: security.phone_escalation.quarantined
{
  "event_type": "phone_escalation.quarantined",
  "audit_id": "UUID",
  "user_id": "UUID",
  "timestamp": "ISO8601",
  "quarantine_reason": "INSUFFICIENT_PHONE_TRUST_SCORE",
  "requested_role": "MENTOR",
  "freeze_until": "ISO8601",
  "agent_version": "v1.0"
}

// Topic: security.phone_escalation.permitted
{
  "event_type": "phone_escalation.permitted",
  "audit_id": "UUID",
  "user_id": "UUID",
  "timestamp": "ISO8601",
  "granted_role": "RECRUITER",
  "phone_trust_score": 72,
  "agent_version": "v1.0"
}

// Topic: security.phone_escalation.sim_swap_blocked
{
  "event_type": "phone_escalation.sim_swap_blocked",
  "audit_id": "UUID",
  "user_id": "UUID",
  "timestamp": "ISO8601",
  "carrier_change_hours_ago": 48,
  "previous_carrier": "Airtel",
  "current_carrier": "Jio",
  "agent_version": "v1.0"
}
```

Consumers of these events:
- Ops Console Alert Dashboard
- Wazuh SIEM (security incident correlation)
- User Trust Score Engine (R68)
- Notification Service (admin alert only — never user-facing on DENY reason)

---

---

## SECTION 8 — PHONE TRUST SCORE COMPUTATION

The Phone Trust Score (0–100) is computed by the Trust Score Engine  
using the following deterministic weighted formula.  
This is NOT an ML model (R28-1 compliant).

```
PhoneTrustScore =
    CLAMP(0, 100,
        + (phone_age_days >= 365                        ? 20 : 0)
        + (phone_age_days >= 90 AND < 365               ? 10 : 0)
        + (carrier_type == 'mobile' AND NOT voip        ? 15 : 0)
        + (carrier_type == 'prepaid'                    ? -10 : 0)
        + (carrier_type == 'voip'                       ? -30 : 0)
        + (no_carrier_change_90_days                    ? 15 : 0)
        + (carrier_change_within_72h                    ? -25 : 0)
        + (device_binding_age_days >= 30                ? 10 : 0)
        + (unique_device_bindings == 1                  ? 5 : 0)
        + (unique_device_bindings > 3                   ? -10 : 0)
        + (no_otp_failures_30_days                      ? 10 : 0)
        + (otp_failure_rate_30d > 0.3                   ? -15 : 0)
        + (no_multi_account_sharing                     ? 10 : 0)
        + (multi_account_count > 1                      ? -20 : 0)
        + (successful_role_gate_passes_lifetime * 2,    max 10)
        + (country_code_consistent                      ? 5 : 0)
        - (quarantine_events_90d * 10)
        - (deny_events_90d * 5)
    )
```

Score recomputed:
- On every OTP event
- On every carrier change detection
- On every device binding change
- On every quarantine or deny event
- On scheduled daily batch (all active phone numbers)

---

---

## SECTION 9 — REQUIRED UI SCREENS (OPS CONSOLE)

### 9.1 Phone Escalation Threat Dashboard
- Real-time feed of DENY and QUARANTINE decisions
- Filter by: deny_reason_code, role_target, date, user
- Threat heatmap by deny_reason_code type
- 24h / 7d / 30d trend charts

### 9.2 Quarantine Review Queue
- List of pending quarantine records
- Per-record detail: user profile, gate trace, phone trust score, SIM history
- Action buttons: APPROVE | DENY_PERMANENTLY | ESCALATE
- Decision notes field (mandatory before action)
- Audit trail of reviewer decisions

### 9.3 Phone Trust Score Explorer
- Search by user_id or masked phone suffix
- Score breakdown by component
- Score history timeline graph
- Trigger manual recompute button (super admin only)

### 9.4 SIM Swap Incident Timeline
- Chronological list of SIM swap detections
- Carrier change history per affected phone
- Linked role-change attempts blocked

### 9.5 Agent Health & SLA Monitor
- Agent uptime indicator
- Decision latency p50 / p95 / p99
- Gate failure distribution
- Kafka event emission success rate
- Last engine failure timestamp

All screens: Super Admin access only. MFA required. Full audit logging.

---

---

## SECTION 10 — INTEGRATION WIRING (MANDATORY)

| Source System | Wire | Agent Integration Point |
|---------------|------|-------------------------|
| Auth Service | Synchronous pre-commit hook | Every phone-gated role change calls `/internal/phone-guard/evaluate` before RBAC write |
| RBAC Engine | Pre-write gate | Agent decision = PERMIT required before any role record INSERT |
| OTP Service | Event hook | Every OTP issue/fail/success emits to agent via `/internal/phone-guard/otp-event` |
| Kafka | Event consumer | Agent publishes to `security.phone_escalation.*` topics |
| Redis | State store | OTP velocity counters, phone lock state, quarantine freeze state |
| PostgreSQL | Persistent store | All schemas in Section 5 |
| Wazuh SIEM | Event consumer | Subscribes to `security.phone_escalation.*` for correlation |
| Ops Console | Admin UI | Section 9 screens consume agent APIs |
| Trust Score Engine (R68) | Event consumer | Subscribes to `security.phone_escalation.*` to update user trust score |

---

---

## SECTION 11 — ENFORCEMENT GATE

```
No role change that involves phone verification may proceed
without passing all 9 gates of PHONE_ROLE_ESCALATION_GUARD_AGENT.

No deployment may be claimed production-ready if:
  ✗ Agent is not deployed as auth service sidecar
  ✗ All 9 gates are not implemented
  ✗ Fail-closed behavior is not verified
  ✗ Immutable audit log is not operational
  ✗ Kafka events are not emitting
  ✗ Ops console screens are not wired
  ✗ Phone trust score computation is not running
  ✗ SIM swap detection is not active
  ✗ OTP velocity tracking is not active

Violation:
  → STOP EXECUTION
  → REPORT PHONE_GUARD_AGENT_INCOMPLETE
  → NO DEPLOYMENT CLAIM PERMITTED
  → NO TRUST INFRASTRUCTURE CLAIM PERMITTED
  → NO ANTIGRAVITY SEAL GRANTED
```

---

---

## SECTION 12 — ANTIGRAVITY SEAL

```
╔══════════════════════════════════════════════════════════════════════╗
║         ANTIGRAVITY LAYER — TRUST INFRASTRUCTURE SEAL               ║
║                                                                      ║
║   PHONE_ROLE_ESCALATION_GUARD_AGENT                                  ║
║                                                                      ║
║   Status:         SEALED · LOCKED · DETERMINISTIC                    ║
║   Gates:          9 (all mandatory)                                  ║
║   Threats Covered: 18 direct + 8 indirect phone attack vectors       ║
║   Fail Mode:      CLOSED (deny-all on engine failure)                ║
║   Audit:          IMMUTABLE · 7-YEAR RETENTION                       ║
║   AI Usage:       NONE (R28-1 compliant — rule engine only)         ║
║   Mutation:       ADD-ONLY via version bump                          ║
║   Interpretation: NONE PERMITTED                                      ║
║                                                                      ║
║   Bound To: R10 · R21 · R39 · R40 · R51 · R68 · R79                ║
║                                                                      ║
║   Phone cannot become a privilege vector.                            ║
║   Trust infrastructure holds.                                        ║
║   Antigravity active.                                                ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

*ECOSKILLER — ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE*  
*ANTIGRAVITY LAYER v1.0 — PHONE_ROLE_ESCALATION_GUARD_AGENT*  
*Status: ACTIVE · ENFORCED · ADD-ONLY · SEALED*
