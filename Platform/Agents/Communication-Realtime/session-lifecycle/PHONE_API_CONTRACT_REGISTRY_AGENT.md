# PHONE_API_CONTRACT_REGISTRY_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### ECOSKILLER — ANTIGRAVITY MODULE
**Status:** `SEALED · LOCKED · GOVERNED · DETERMINISTIC`  
**Version:** `v1.0`  
**Mutation Policy:** `ADD-ONLY via version bump`  
**Interpretation Authority:** `NONE`  
**Execution Authority:** `Human declaration only`  
**Default Behavior:** `DENY`  
**Failure Mode:** `STOP → REPORT → NO PARTIAL OUTPUT`

---

## SECTION 0 — ANTIGRAVITY DECLARATION

ANTIGRAVITY is the architectural law inside Ecoskiller that eliminates invisible bureaucratic gravity — the weight of untrusted data, unverified identities, and unaudited phone interactions that silently pulls systems toward fraud, abuse, and compliance failure.

The PHONE_API_CONTRACT_REGISTRY_AGENT is the enforcement organ of ANTIGRAVITY for all telephony surfaces. It does not facilitate phone calls. It does not dial. It does not speak. It **contracts, registers, governs, and enforces** every phone-API interaction that touches the Ecoskiller platform — across all tenants, all user roles, and all communication pathways.

This agent exists because:

- Phone numbers are the single highest-abuse trust vector in multi-tenant SaaS
- OTP fraud, SIM-swap attacks, and SMS pumping are structurally preventable with deterministic enforcement
- Every SMS sent, every OTP issued, every phone verified must be traceable, auditable, and contract-bound
- No phone event may occur outside a registered, validated, rate-governed contract

**Absence of this agent → STOP EXECUTION on all phone-dependent services.**

---

## SECTION 1 — AGENT IDENTITY

```
AGENT_NAME            : PHONE_API_CONTRACT_REGISTRY_AGENT
AGENT_CLASS           : Trust Infrastructure · Contract Governance
DOMAIN_MODULE         : ENTERPRISE_OPTIMIZATION
SUB_MODULE            : TRUST_INFRASTRUCTURE
ANTIGRAVITY_LAYER     : PHONE_SURFACE
SERVICE_NAMESPACE     : trust-infra
KUBERNETES_NAMESPACE  : ecoskiller-trust
DEPLOY_TARGET         : k3s / GCP Kubernetes
LANGUAGE_RUNTIME      : Spring Boot (Java 21) or Node.js 20 LTS
CONTRACT_FORMAT       : OpenAPI 3.1 + AsyncAPI 2.6
STORAGE_PRIMARY       : PostgreSQL (phone_contract_registry schema)
STORAGE_STATE         : Redis (rate limits · OTP windows · lock state)
EVENT_BUS             : Apache Kafka
AUDIT_STORE           : ClickHouse (phone_audit_events table)
OBSERVABILITY         : Prometheus · Loki · OpenTelemetry
SECURITY_LAYER        : Keycloak (JWT) · OPA (policy enforcement) · ModSecurity (WAF)
```

---

## SECTION 2 — SCOPE AND JURISDICTION

### 2.1 What This Agent Governs (LOCKED)

| Surface | Governed |
|---|---|
| OTP delivery via SMS | YES — all tenants |
| Phone number registration and verification | YES — all user roles |
| Parent phone trust-binding (student-parent layer) | YES — explicit consent contract required |
| WhatsApp Business API (future) | YES — contract registry enforced on registration |
| Recruiter-to-candidate call scheduling tokens | YES — slot-locked, expiry-enforced |
| Missed-call verification flows | YES — provider contract registered |
| Institute admin bulk SMS (placement notifications) | YES — campaign contract required |
| GD session voice infrastructure (Jitsi/WebRTC) | NO — governed by Voice GD Orchestrator |
| In-app push notifications | NO — governed by Notification Service |
| Email verification | NO — governed by Auth Service + Email Verification Engine |

### 2.2 What This Agent Does NOT Do (HARD BOUNDARY)

```
DOES NOT STORE raw phone numbers beyond verified hash + last-4
DOES NOT INITIATE calls
DOES NOT RECORD voice
DOES NOT EXPOSE phone numbers to frontend APIs without masking
DOES NOT GRANT phone verification status without completing contract validation
DOES NOT RETRY failed OTPs automatically beyond defined contract window
```

Violation of boundary → STOP EXECUTION → REPORT PHONE-BOUNDARY-BREACH

---

## SECTION 3 — CONTRACT TAXONOMY

Every phone API interaction is classified into exactly one contract type. Unclassified interactions are denied.

### 3.1 Contract Types (NON-NEGOTIABLE)

```
CONTRACT_TYPE_01  : OTP_DELIVERY
CONTRACT_TYPE_02  : PHONE_REGISTRATION
CONTRACT_TYPE_03  : PHONE_VERIFICATION
CONTRACT_TYPE_04  : TRUST_BINDING (Parent ↔ Student)
CONTRACT_TYPE_05  : CAMPAIGN_SMS (Bulk · Institute / Recruiter)
CONTRACT_TYPE_06  : TRANSACTIONAL_SMS (Billing · Invoice · Slot alerts)
CONTRACT_TYPE_07  : SCHEDULING_TOKEN (Interview slot + caller ID token)
CONTRACT_TYPE_08  : MISSED_CALL_VERIFY
CONTRACT_TYPE_09  : PROVIDER_HEALTH_POLL
CONTRACT_TYPE_10  : AUDIT_REPLAY_REQUEST
```

### 3.2 Contract State Machine

```
STATES:
  DRAFT       → Contract created, not yet validated
  VALIDATED   → Schema + provider + rate-limit checks passed
  ACTIVE      → Contract live and enforcing
  SUSPENDED   → Temporarily halted (abuse signal / provider fault)
  EXPIRED     → TTL elapsed or explicit termination
  REVOKED     → Hard termination, non-recoverable
  QUARANTINED → Under fraud investigation, all events blocked

TRANSITIONS:
  DRAFT      → VALIDATED  (via contract_validator service)
  VALIDATED  → ACTIVE     (via human approval for CAMPAIGN / TRUST_BINDING)
  ACTIVE     → SUSPENDED  (via abuse detector / rate breach)
  SUSPENDED  → ACTIVE     (via trust review + human approval)
  ACTIVE     → EXPIRED    (via TTL scheduler)
  ANY        → REVOKED    (via governance admin action only)
  ACTIVE     → QUARANTINED (via fraud_detection_engine signal)
  QUARANTINED → REVOKED   (via compliance officer action)
  QUARANTINED → ACTIVE    (via cleared fraud investigation + human sign-off)
```

---

## SECTION 4 — PROVIDER REGISTRY

### 4.1 Registered Providers (Locked Stack)

| Provider ID | Provider Name | Type | Stack Reference | Failover Priority |
|---|---|---|---|---|
| `PROV-01` | Jasmin SMS Gateway | Self-hosted · SMS · OTP | Ecoskiller Tech Stack (LOCKED) | PRIMARY |
| `PROV-02` | Postfix + Docker Mail Server | Self-hosted · Email fallback (OTP) | Ecoskiller Tech Stack | SECONDARY (email-OTP path only) |
| `PROV-03` | ntfy | Self-hosted · Push fallback (OTP) | Ecoskiller Tech Stack | TERTIARY (app-OTP path only) |
| `PROV-04` | RESERVED — WhatsApp Business API | External · Contractual approval required | Not yet active | QUARANTINED until licensed |

### 4.2 Provider Contract Enforcement Rules

```
RULE-P01: Every provider must have an active PROVIDER_CONTRACT record before routing
RULE-P02: Provider contract must declare: endpoint, auth_type, rate_limit_per_min,
          rate_limit_per_day, max_message_length, encoding (GSM7 / UCS2),
          DLT_registration (for India), estimated_delivery_sla_ms
RULE-P03: If primary provider health poll fails 3 consecutive times →
          SUSPEND primary → ACTIVATE secondary → EMIT provider.failover.triggered event
RULE-P04: Provider failover requires event log entry with timestamp, reason, and
          responsible circuit_breaker_id
RULE-P05: No provider may be added to registry without human-declared approval
          signed by Platform Admin RBAC role
RULE-P06: DLT (Distributed Ledger Technology) registration is MANDATORY for all SMS
          sent to Indian phone numbers — absence → STOP ALL SMS DELIVERY
```

---

## SECTION 5 — PHONE REGISTRY DATA MODEL

### 5.1 Core Tables (PostgreSQL — phone_contract_registry schema)

```sql
-- PHONE NUMBER MASTER RECORD
CREATE TABLE phone_registry (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id             UUID NOT NULL REFERENCES tenants(id),
  user_id               UUID NOT NULL REFERENCES users(id),
  phone_hash            TEXT NOT NULL,           -- SHA-256 of E.164 normalized number
  phone_last4           CHAR(4) NOT NULL,        -- Display purposes only
  country_code          CHAR(4) NOT NULL,        -- E.g. +91
  carrier_name          TEXT,                    -- Populated via HLR lookup (future)
  is_verified           BOOLEAN DEFAULT FALSE,
  is_dnd_registered     BOOLEAN DEFAULT FALSE,   -- India DND Registry check
  is_blacklisted        BOOLEAN DEFAULT FALSE,
  trust_score           SMALLINT DEFAULT 0 CHECK (trust_score BETWEEN 0 AND 100),
  verification_method   TEXT CHECK (verification_method IN ('OTP_SMS','OTP_PUSH','MISSED_CALL','MANUAL_KYC')),
  verified_at           TIMESTAMPTZ,
  last_activity_at      TIMESTAMPTZ,
  created_at            TIMESTAMPTZ DEFAULT now(),
  UNIQUE(tenant_id, phone_hash)
);

-- CONTRACT REGISTRY
CREATE TABLE phone_api_contracts (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  contract_type         TEXT NOT NULL,           -- Maps to CONTRACT_TYPE_XX
  contract_state        TEXT NOT NULL DEFAULT 'DRAFT',
  tenant_id             UUID REFERENCES tenants(id),
  provider_id           TEXT NOT NULL,           -- Maps to PROV-XX
  initiated_by_user_id  UUID REFERENCES users(id),
  approved_by_admin_id  UUID REFERENCES users(id),
  purpose               TEXT NOT NULL,
  rate_limit_per_min    INTEGER NOT NULL,
  rate_limit_per_hour   INTEGER NOT NULL,
  rate_limit_per_day    INTEGER NOT NULL,
  max_recipients        INTEGER DEFAULT 1,       -- >1 for CAMPAIGN contracts only
  ttl_hours             INTEGER NOT NULL,
  dlt_template_id       TEXT,                    -- India DLT requirement
  dlt_entity_id         TEXT,                    -- India DLT requirement
  message_template_hash TEXT NOT NULL,           -- SHA-256 of approved template
  activated_at          TIMESTAMPTZ,
  expires_at            TIMESTAMPTZ,
  revoked_at            TIMESTAMPTZ,
  revocation_reason     TEXT,
  audit_trail           JSONB DEFAULT '[]',
  created_at            TIMESTAMPTZ DEFAULT now(),
  updated_at            TIMESTAMPTZ DEFAULT now()
);

-- OTP LEDGER
CREATE TABLE otp_ledger (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  contract_id           UUID NOT NULL REFERENCES phone_api_contracts(id),
  phone_hash            TEXT NOT NULL,
  otp_hash              TEXT NOT NULL,           -- bcrypt of OTP — never stored plain
  purpose               TEXT NOT NULL,           -- LOGIN | REGISTER | TRUST_BIND | TX_CONFIRM
  issued_at             TIMESTAMPTZ NOT NULL DEFAULT now(),
  expires_at            TIMESTAMPTZ NOT NULL,
  verified_at           TIMESTAMPTZ,
  attempt_count         SMALLINT DEFAULT 0,
  max_attempts          SMALLINT DEFAULT 3,
  is_consumed           BOOLEAN DEFAULT FALSE,
  is_expired            BOOLEAN DEFAULT FALSE,
  ip_address            INET,
  device_fingerprint    TEXT,
  CONSTRAINT max_attempts_check CHECK (attempt_count <= max_attempts)
);

-- TRUST BINDING REGISTRY (Parent ↔ Student)
CREATE TABLE phone_trust_bindings (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  contract_id           UUID NOT NULL REFERENCES phone_api_contracts(id),
  parent_phone_hash     TEXT NOT NULL,
  student_user_id       UUID NOT NULL REFERENCES users(id),
  binding_type          TEXT NOT NULL CHECK (binding_type IN ('GUARDIAN','PARENT_PRIMARY','PARENT_SECONDARY')),
  consent_timestamp     TIMESTAMPTZ NOT NULL,
  consent_ip            INET NOT NULL,
  consent_device        TEXT,
  is_active             BOOLEAN DEFAULT TRUE,
  terminated_at         TIMESTAMPTZ,
  termination_reason    TEXT,
  created_at            TIMESTAMPTZ DEFAULT now()
);

-- ABUSE SIGNALS
CREATE TABLE phone_abuse_signals (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  phone_hash            TEXT NOT NULL,
  signal_type           TEXT NOT NULL,          -- OTP_FLOOD | SIM_SWAP_SUSPECT | PUMPING_PATTERN | VELOCITY_BREACH
  signal_source         TEXT NOT NULL,          -- RATE_LIMITER | FRAUD_ENGINE | MANUAL_FLAG | CARRIER_SIGNAL
  severity              TEXT NOT NULL CHECK (severity IN ('LOW','MEDIUM','HIGH','CRITICAL')),
  auto_action_taken     TEXT,                   -- SUSPENDED | QUARANTINED | BLOCKED | NONE
  resolved              BOOLEAN DEFAULT FALSE,
  resolved_by           UUID REFERENCES users(id),
  resolved_at           TIMESTAMPTZ,
  metadata              JSONB DEFAULT '{}',
  created_at            TIMESTAMPTZ DEFAULT now()
);
```

### 5.2 Redis State Keys (Deterministic Namespace)

```
phone:otp:window:{phone_hash}            → TTL = OTP expiry (default 300s)
phone:rate:min:{phone_hash}              → TTL = 60s · counter
phone:rate:hour:{phone_hash}             → TTL = 3600s · counter
phone:rate:day:{phone_hash}              → TTL = 86400s · counter
phone:contract:active:{contract_id}      → TTL = contract TTL
phone:blacklist:{phone_hash}             → Permanent until cleared
phone:otp:attempt:{otp_ledger_id}        → TTL = OTP expiry · attempt counter
phone:trust:pending:{binding_id}         → TTL = 24h consent window
campaign:rate:{contract_id}              → TTL = campaign window · send counter
provider:health:{provider_id}            → TTL = 30s · health status
provider:circuit:{provider_id}           → TTL = 300s · circuit breaker state
```

---

## SECTION 6 — OTP GOVERNANCE (SEALED)

### 6.1 OTP Rules (NON-NEGOTIABLE)

```
RULE-OTP-01  : OTP length = 6 digits minimum · 8 digits for financial transactions
RULE-OTP-02  : OTP TTL = 300 seconds (5 minutes) — no exceptions
RULE-OTP-03  : Max attempts = 3 per OTP window · 4th attempt = OTP invalidated + abuse signal
RULE-OTP-04  : OTP stored as bcrypt hash only — plaintext never persisted or logged
RULE-OTP-05  : New OTP cannot be issued to same phone within 60 seconds of last issue
               (anti-flooding lock enforced via Redis phone:otp:window key)
RULE-OTP-06  : Max 5 OTP issuances per phone per 24-hour window
               (6th attempt → phone suspended for 24h · abuse signal emitted)
RULE-OTP-07  : OTP delivery failure (provider timeout > 10s) → log event + return
               DELIVERY_FAILED status to caller — caller must retry via contract
RULE-OTP-08  : OTP for TRUST_BINDING (Parent consent) requires human-readable
               consent message prepended to OTP SMS body — template hash enforced
RULE-OTP-09  : Financial-transaction OTP (CONTRACT_TYPE_06) requires additional
               device fingerprint match from prior verified session
RULE-OTP-10  : Every OTP event writes to otp_ledger AND emits otp.issued Kafka event
               Absence of either → STOP OTP FLOW
```

### 6.2 OTP Flow (Deterministic State Machine)

```
REQUEST RECEIVED
       ↓
[01] Contract validation → is contract ACTIVE? → NO → REJECT (403)
       ↓
[02] Rate limit check (Redis) → breach? → YES → REJECT (429) + log
       ↓
[03] Blacklist check (Redis) → listed? → YES → REJECT (403) + log
       ↓
[04] DND check (India) → registered + non-transactional? → YES → REJECT (451)
       ↓
[05] OTP window check → active window exists? → YES → REJECT (429) + return TTL remaining
       ↓
[06] Generate OTP (CSPRNG · 6-8 digits per contract type)
       ↓
[07] Hash OTP (bcrypt, cost factor 10)
       ↓
[08] Persist to otp_ledger (phone_hash, otp_hash, expires_at, contract_id)
       ↓
[09] Set Redis OTP window key (TTL = 300s)
       ↓
[10] Dispatch to provider via Jasmin (primary) → await ACK (timeout = 10s)
       ↓
[11] Emit otp.issued event to Kafka → topic: phone.events
       ↓
[12] Return {reference_id, expires_in_seconds} to caller
       (OTP value NEVER returned in API response)
```

---

## SECTION 7 — TRUST BINDING GOVERNANCE (Parent ↔ Student Layer)

### 7.1 Trust Binding Rules (SEALED)

```
RULE-TB-01  : Trust binding can only be initiated by authenticated student
              (RBAC role = STUDENT · verified phone required)
RULE-TB-02  : Parent phone must be distinct from student phone — same number → REJECT
RULE-TB-03  : Consent OTP sent to parent phone — parent must enter OTP
              within 24-hour window
RULE-TB-04  : Consent SMS must use DLT-registered template with explicit consent
              language — template hash enforced on send
RULE-TB-05  : Maximum 2 trust bindings per student (PRIMARY + SECONDARY guardian)
RULE-TB-06  : Trust binding activation emits trust_binding.activated Kafka event
              consumed by: Parent Dashboard Service, Notification Service,
              Admin Governance Service
RULE-TB-07  : Trust binding termination requires either:
              (a) Student explicit revocation (authenticated), OR
              (b) Admin Governance action (misconduct / dispute)
              Auto-expiry is NOT permitted
RULE-TB-08  : Parent phone visibility in student profile = MASKED (last-4 only)
              Full number accessible only via Admin Governance with audit log entry
RULE-TB-09  : Parent consent record is IMMUTABLE post-activation — stored in
              Immutable Archive Service (WORM, 15+ year retention per Legal requirement)
RULE-TB-10  : Phone trust score of student increases +15 points upon successful
              trust binding activation
```

---

## SECTION 8 — CAMPAIGN SMS GOVERNANCE

### 8.1 Campaign Contract Requirements

Campaign SMS (bulk messaging) is available ONLY to: Institute Admins (placement drives), Recruiter Admins (job alerts), and Platform Admins (system announcements).

```
PRE-CONDITIONS (all must pass):
  ✓ Contract type = CONTRACT_TYPE_05 (CAMPAIGN_SMS)
  ✓ RBAC role = INSTITUTE_ADMIN | RECRUITER_ADMIN | PLATFORM_ADMIN
  ✓ Tenant billing status = ACTIVE (no outstanding invoices)
  ✓ DLT Entity ID registered
  ✓ DLT Template ID approved for each message template
  ✓ Recipient list = platform-internal user IDs only (no external uploads)
  ✓ Human admin approval of campaign contract
  ✓ Rate limit declared: max 500 messages/hour per tenant default
```

```
ENFORCEMENT RULES:
  RULE-C01  : Recipient resolution happens server-side only
               — caller submits filter criteria (role, batch, institute_id)
               — agent resolves actual phone hashes internally
               — caller never receives resolved phone list
  RULE-C02  : Campaign delivery is asynchronous — Kafka-driven batch dispatch
  RULE-C03  : Opt-out records are checked per phone_hash before dispatch
               — opted-out phones silently skipped + counted in delivery report
  RULE-C04  : Campaign delivery report (sent_count, failed_count, skipped_count)
               stored in ClickHouse — no individual success/fail per phone exposed
  RULE-C05  : Campaign contract auto-expires after 48 hours or delivery completion
  RULE-C06  : DND-registered phones (India) receive campaign SMS only if template
               is classified as TRANSACTIONAL on DLT — PROMOTIONAL blocked
```

---

## SECTION 9 — ANTI-ABUSE ENFORCEMENT ENGINE

### 9.1 Abuse Signals and Automated Actions

| Signal Type | Detection Trigger | Auto Action | Escalation |
|---|---|---|---|
| `OTP_FLOOD` | >5 OTP requests in 24h from same phone | SUSPEND phone 24h | Emit `phone.abuse.otp_flood` |
| `VELOCITY_BREACH` | >20 registration attempts from same IP in 1h | BLOCK IP (Redis, 24h) | Emit `phone.abuse.velocity` + Wazuh alert |
| `SIM_SWAP_SUSPECT` | Verified phone re-verifies with different device fingerprint within 1h | QUARANTINE phone + force re-KYC | Emit `phone.abuse.sim_swap` + Admin alert |
| `PUMPING_PATTERN` | Provider reports >10 failed delivery acks to same prefix in 10min | SUSPEND number range + notify Platform Admin | Emit `phone.abuse.pumping` |
| `TEMPLATE_TAMPER` | Message body hash ≠ registered DLT template hash | BLOCK send + REVOKE contract | Emit `phone.abuse.template_tamper` + CRITICAL alert |
| `BLACKLIST_HIT` | Phone hash matches global blacklist | REJECT all interactions | Log only |
| `TRUST_BIND_MISMATCH` | Parent phone = student phone | REJECT + Emit `phone.trust.mismatch` | Log |

### 9.2 Fraud Detection Integration

```
Integration Point: Fraud Detection Engine (Ecoskiller AI Layer)
Protocol         : Kafka consumer on topic phone.events
Events consumed  :
  - otp.issued
  - otp.failed
  - phone.verified
  - trust_binding.initiated
  - campaign.dispatched
  - provider.failover.triggered

Fraud signals emitted back to this agent via topic: fraud.signals.phone
Agent response to fraud signal:
  SEVERITY=LOW     → log + increment phone trust_score penalty
  SEVERITY=MEDIUM  → SUSPEND phone · emit abuse signal
  SEVERITY=HIGH    → QUARANTINE phone · notify Admin Governance
  SEVERITY=CRITICAL → REVOKE all contracts for phone · notify Compliance Officer
```

---

## SECTION 10 — API CONTRACT (OpenAPI 3.1)

```yaml
openapi: 3.1.0
info:
  title: PHONE_API_CONTRACT_REGISTRY_AGENT
  version: "1.0"
  description: |
    Sealed contract registry for all phone API interactions in Ecoskiller.
    ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE — ANTIGRAVITY MODULE.

servers:
  - url: https://api.ecoskiller.com/trust/phone
    description: Production
  - url: https://api.ecoskiller.staging/trust/phone
    description: Staging

security:
  - bearerAuth: []

paths:

  /contracts:
    post:
      summary: Register a new phone API contract
      operationId: createPhoneContract
      x-rbac-roles: [PLATFORM_ADMIN, INSTITUTE_ADMIN, RECRUITER_ADMIN, AUTH_SERVICE_INTERNAL]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/CreateContractRequest'
      responses:
        "201":
          description: Contract created in DRAFT state
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ContractResponse'
        "400": { description: Invalid contract schema }
        "403": { description: RBAC role not permitted }
        "409": { description: Duplicate active contract for same purpose }

  /contracts/{contract_id}/activate:
    post:
      summary: Activate a validated contract (human approval gate)
      operationId: activateContract
      x-rbac-roles: [PLATFORM_ADMIN]
      parameters:
        - name: contract_id
          in: path
          required: true
          schema: { type: string, format: uuid }
      responses:
        "200": { description: Contract activated }
        "400": { description: Contract not in VALIDATED state }
        "403": { description: Not authorized }

  /otp/issue:
    post:
      summary: Issue OTP under an active contract
      operationId: issueOtp
      x-rbac-roles: [AUTH_SERVICE_INTERNAL, NOTIFICATION_SERVICE_INTERNAL]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/IssueOtpRequest'
      responses:
        "200":
          description: OTP issued — reference_id returned, OTP value never returned
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/IssueOtpResponse'
        "429": { description: Rate limit or OTP window active }
        "403": { description: Phone blacklisted or contract invalid }
        "451": { description: DND registered — non-transactional blocked }

  /otp/verify:
    post:
      summary: Verify submitted OTP against ledger
      operationId: verifyOtp
      x-rbac-roles: [AUTH_SERVICE_INTERNAL]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/VerifyOtpRequest'
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/VerifyOtpResponse'
        "400": { description: OTP invalid or expired }
        "429": { description: Max attempts exceeded }

  /phone/register:
    post:
      summary: Register and normalize a phone number for a user
      operationId: registerPhone
      x-rbac-roles: [AUTH_SERVICE_INTERNAL, USER_SERVICE_INTERNAL]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/RegisterPhoneRequest'
      responses:
        "201": { description: Phone registered (unverified) }
        "409": { description: Phone already registered to this tenant }

  /phone/verify:
    post:
      summary: Mark phone as verified post-OTP confirmation
      operationId: verifyPhone
      x-rbac-roles: [AUTH_SERVICE_INTERNAL]
      responses:
        "200": { description: Phone trust record updated, trust_score recalculated }

  /trust-bindings:
    post:
      summary: Initiate parent-student trust binding
      operationId: initiateTrustBinding
      x-rbac-roles: [STUDENT]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/InitiateTrustBindingRequest'
      responses:
        "202": { description: Consent OTP dispatched to parent phone }
        "400": { description: Parent phone = student phone — rejected }
        "409": { description: Max trust bindings reached (2) }

  /trust-bindings/{binding_id}/confirm:
    post:
      summary: Parent confirms trust binding via OTP
      operationId: confirmTrustBinding
      x-rbac-roles: [PUBLIC_WITH_OTP_REFERENCE]
      responses:
        "200": { description: Trust binding activated — immutable record created }
        "400": { description: OTP invalid or consent window expired }

  /providers/{provider_id}/health:
    get:
      summary: Get provider health status
      operationId: getProviderHealth
      x-rbac-roles: [PLATFORM_ADMIN, OPS_INTERNAL]
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ProviderHealthResponse'

  /audit/phone/{phone_hash}:
    get:
      summary: Retrieve audit trail for a phone hash (Admin Governance only)
      operationId: getPhoneAudit
      x-rbac-roles: [PLATFORM_ADMIN, COMPLIANCE_OFFICER]
      parameters:
        - name: phone_hash
          in: path
          required: true
          schema: { type: string }
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/PhoneAuditResponse'
        "403": { description: Insufficient RBAC — audit access logged }

components:
  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT

  schemas:
    CreateContractRequest:
      type: object
      required: [contract_type, provider_id, purpose, rate_limit_per_min,
                 rate_limit_per_hour, rate_limit_per_day, ttl_hours, message_template_hash]
      properties:
        contract_type: { type: string, enum: [OTP_DELIVERY, PHONE_REGISTRATION,
          PHONE_VERIFICATION, TRUST_BINDING, CAMPAIGN_SMS, TRANSACTIONAL_SMS,
          SCHEDULING_TOKEN, MISSED_CALL_VERIFY, PROVIDER_HEALTH_POLL, AUDIT_REPLAY_REQUEST] }
        provider_id: { type: string, enum: [PROV-01, PROV-02, PROV-03] }
        purpose: { type: string, maxLength: 255 }
        rate_limit_per_min: { type: integer, minimum: 1, maximum: 60 }
        rate_limit_per_hour: { type: integer, minimum: 1, maximum: 500 }
        rate_limit_per_day: { type: integer, minimum: 1, maximum: 5000 }
        ttl_hours: { type: integer, minimum: 1, maximum: 720 }
        message_template_hash: { type: string, description: SHA-256 of approved template }
        dlt_template_id: { type: string }
        dlt_entity_id: { type: string }
        max_recipients: { type: integer, default: 1 }

    IssueOtpRequest:
      type: object
      required: [contract_id, phone_hash, purpose, device_fingerprint, ip_address]
      properties:
        contract_id: { type: string, format: uuid }
        phone_hash: { type: string, description: SHA-256 of E.164 normalized phone }
        purpose: { type: string, enum: [LOGIN, REGISTER, TRUST_BIND, TX_CONFIRM, RESET] }
        device_fingerprint: { type: string }
        ip_address: { type: string, format: ipv4 }

    IssueOtpResponse:
      type: object
      properties:
        reference_id: { type: string, format: uuid }
        expires_in_seconds: { type: integer }
        masked_destination: { type: string, example: "+91****9876" }

    VerifyOtpRequest:
      type: object
      required: [reference_id, otp_value, device_fingerprint]
      properties:
        reference_id: { type: string, format: uuid }
        otp_value: { type: string, minLength: 6, maxLength: 8 }
        device_fingerprint: { type: string }

    VerifyOtpResponse:
      type: object
      properties:
        verified: { type: boolean }
        attempts_remaining: { type: integer }
        trust_score_delta: { type: integer, description: Score change applied to phone record }
```

---

## SECTION 11 — EVENT SCHEMA (AsyncAPI 2.6)

```yaml
asyncapi: 2.6.0
info:
  title: PHONE_API_CONTRACT_REGISTRY_AGENT Events
  version: "1.0"

channels:

  phone.events:
    description: All phone interaction events (consumed by Fraud Detection, Analytics)
    publish:
      message:
        oneOf:
          - $ref: '#/components/messages/OtpIssued'
          - $ref: '#/components/messages/OtpVerified'
          - $ref: '#/components/messages/OtpFailed'
          - $ref: '#/components/messages/PhoneVerified'
          - $ref: '#/components/messages/TrustBindingActivated'
          - $ref: '#/components/messages/TrustBindingTerminated'
          - $ref: '#/components/messages/PhoneAbuse'
          - $ref: '#/components/messages/ProviderFailover'
          - $ref: '#/components/messages/ContractStateChanged'

  fraud.signals.phone:
    description: Inbound fraud signals from Fraud Detection Engine
    subscribe:
      message:
        $ref: '#/components/messages/FraudSignal'

components:
  messages:

    OtpIssued:
      payload:
        type: object
        required: [event_type, reference_id, phone_hash, purpose, contract_id,
                   provider_id, issued_at, expires_at, tenant_id]
        properties:
          event_type: { const: otp.issued }
          reference_id: { type: string, format: uuid }
          phone_hash: { type: string }
          purpose: { type: string }
          contract_id: { type: string, format: uuid }
          provider_id: { type: string }
          issued_at: { type: string, format: date-time }
          expires_at: { type: string, format: date-time }
          tenant_id: { type: string, format: uuid }
          ip_address: { type: string }
          device_fingerprint: { type: string }

    OtpVerified:
      payload:
        type: object
        required: [event_type, reference_id, phone_hash, verified_at, tenant_id]
        properties:
          event_type: { const: otp.verified }
          reference_id: { type: string, format: uuid }
          phone_hash: { type: string }
          verified_at: { type: string, format: date-time }
          tenant_id: { type: string, format: uuid }
          attempt_count: { type: integer }

    OtpFailed:
      payload:
        type: object
        required: [event_type, reference_id, phone_hash, reason, attempt_count]
        properties:
          event_type: { const: otp.failed }
          reference_id: { type: string, format: uuid }
          phone_hash: { type: string }
          reason: { type: string, enum: [INVALID_OTP, EXPIRED, MAX_ATTEMPTS_EXCEEDED, CONTRACT_INVALID] }
          attempt_count: { type: integer }

    TrustBindingActivated:
      payload:
        type: object
        required: [event_type, binding_id, student_user_id, parent_phone_hash,
                   binding_type, activated_at, consent_timestamp, tenant_id]
        properties:
          event_type: { const: trust_binding.activated }
          binding_id: { type: string, format: uuid }
          student_user_id: { type: string, format: uuid }
          parent_phone_hash: { type: string }
          binding_type: { type: string }
          activated_at: { type: string, format: date-time }
          consent_timestamp: { type: string, format: date-time }
          tenant_id: { type: string, format: uuid }

    PhoneAbuse:
      payload:
        type: object
        required: [event_type, phone_hash, signal_type, severity, auto_action_taken, detected_at]
        properties:
          event_type: { const: phone.abuse }
          phone_hash: { type: string }
          signal_type: { type: string }
          severity: { type: string, enum: [LOW, MEDIUM, HIGH, CRITICAL] }
          auto_action_taken: { type: string }
          detected_at: { type: string, format: date-time }
          metadata: { type: object }

    ProviderFailover:
      payload:
        type: object
        required: [event_type, from_provider, to_provider, reason, failed_at]
        properties:
          event_type: { const: provider.failover.triggered }
          from_provider: { type: string }
          to_provider: { type: string }
          reason: { type: string }
          failed_at: { type: string, format: date-time }
          circuit_breaker_id: { type: string }

    FraudSignal:
      payload:
        type: object
        required: [signal_id, phone_hash, severity, signal_source, detected_at]
        properties:
          signal_id: { type: string, format: uuid }
          phone_hash: { type: string }
          severity: { type: string, enum: [LOW, MEDIUM, HIGH, CRITICAL] }
          signal_source: { type: string }
          detected_at: { type: string, format: date-time }
          recommended_action: { type: string }
```

---

## SECTION 12 — SERVICE DEPENDENCIES (CONTRACT GATES)

### 12.1 Upstream Dependencies (This Agent Requires)

| Service | Dependency Type | Gate Requirement |
|---|---|---|
| Auth Service | Internal JWT validation | `identity_ready` MUST be ACTIVE |
| Keycloak | RBAC enforcement | `rbac_ready` MUST be ACTIVE |
| Redis | Rate limiting · OTP windows · circuit breakers | Redis cluster MUST be healthy |
| PostgreSQL | Contract registry · OTP ledger · trust bindings | `db_ready` MUST be ACTIVE |
| Jasmin SMS Gateway | Primary OTP / SMS delivery | Provider health poll MUST pass |
| Kafka | Event emission · fraud signal consumption | Event bus MUST be reachable |
| OPA | Policy enforcement on contract creation | `policy_engine_ready` MUST be ACTIVE |
| Wazuh | SIEM alert push (abuse events) | Security bus MUST be available |

### 12.2 Downstream Consumers (This Agent Produces)

| Service | Consumes | Via |
|---|---|---|
| Fraud Detection Engine | `phone.events` | Kafka |
| Analytics Service | `phone.events` → ClickHouse | Kafka |
| Admin Governance Service | Abuse signals + contract state changes | Kafka |
| Parent Dashboard Service | `trust_binding.activated` | Kafka |
| Notification Service | Delivery receipts and campaign reports | Kafka |
| Immutable Archive Service | Trust binding consent records | Direct write (WORM API) |
| Auth Service | Phone verification status | REST (internal) |

### 12.3 Contract Gate Output

On successful agent startup and health verification, this agent produces the gate signal:

```
phone_trust_ready = TRUE
```

Services that require phone trust before activation:
- Auth Service (phone verification flows)
- User Service (phone registration)
- Parent Dashboard Service
- Notification Service (SMS channel)
- Billing Service (transaction confirmation OTP)

**Absence of `phone_trust_ready` → STOP EXECUTION on all listed services.**

---

## SECTION 13 — OBSERVABILITY (NON-OPTIONAL)

### 13.1 Prometheus Metrics (All Required)

```
phone_otp_issued_total{tenant_id, provider_id, purpose}
phone_otp_verified_total{tenant_id, purpose}
phone_otp_failed_total{tenant_id, reason}
phone_otp_expired_total{tenant_id}
phone_contract_active_count{contract_type, tenant_id}
phone_abuse_signal_total{signal_type, severity, auto_action}
phone_provider_failover_total{from_provider, to_provider}
phone_provider_health_status{provider_id}            → Gauge: 1=UP, 0=DOWN
phone_trust_binding_active_count{tenant_id}
phone_rate_limit_breach_total{phone_hash_prefix, limit_type}
phone_campaign_sent_total{tenant_id, contract_id}
phone_campaign_failed_total{tenant_id, contract_id}
```

### 13.2 Alerting Rules

```
ALERT PhoneProviderDown
  expr: phone_provider_health_status{provider_id="PROV-01"} == 0
  for: 90s
  severity: CRITICAL
  action: Page Platform Admin · Auto-failover to PROV-02

ALERT OtpFloodDetected
  expr: rate(phone_abuse_signal_total{signal_type="OTP_FLOOD"}[5m]) > 5
  severity: HIGH
  action: Trigger Wazuh incident

ALERT ContractSuspendedUnexpected
  expr: increase(phone_contract_state_change_total{new_state="SUSPENDED"}[10m]) > 3
  severity: HIGH
  action: Notify Platform Admin

ALERT DltTemplateViolation
  expr: phone_abuse_signal_total{signal_type="TEMPLATE_TAMPER"} > 0
  severity: CRITICAL
  action: Page Compliance Officer · HALT all campaign sends for tenant
```

### 13.3 Loki Log Labels (Mandatory)

```
{app="phone-contract-registry", tenant_id="...", contract_id="...", 
 phone_hash_prefix="...", event_type="..."}
```

Raw phone numbers MUST NOT appear in logs. Violation = security incident.

### 13.4 OpenTelemetry Traces

```
Trace spans required for:
  - contract_create (root)
  - otp_issue (root)
      └── rate_limit_check
      └── blacklist_check
      └── otp_generate
      └── provider_dispatch
      └── kafka_emit
  - otp_verify (root)
      └── hash_compare
      └── attempt_update
  - trust_binding_initiate (root)
  - trust_binding_confirm (root)
      └── immutable_archive_write
```

---

## SECTION 14 — SECURITY ENFORCEMENT

```
SEC-01  : All phone numbers normalized to E.164 format on ingress — rejected if invalid
SEC-02  : Phone hash = SHA-256(E.164_normalized) — computed before any storage or comparison
SEC-03  : OTP stored as bcrypt (cost=10) — never logged, never returned in API response
SEC-04  : All API endpoints protected by JWT (Keycloak) + OPA policy check
SEC-05  : Rate limits enforced at: WAF (ModSecurity) → Kong Gateway → Redis → Service layer
          (3-layer defense · any layer can block independently)
SEC-06  : Inter-service calls authenticated via short-lived Vault-issued service tokens
SEC-07  : All audit log writes are append-only — no UPDATE or DELETE permitted on audit tables
SEC-08  : Trust binding consent records written to Immutable Archive Service (WORM)
          within 5 seconds of activation — failure = STOP binding activation
SEC-09  : Phone masking enforced in all API responses — full number never returned
          Format: country_code + ****last4 (e.g., +91****9876)
SEC-10  : Admin access to full phone number requires:
          (a) COMPLIANCE_OFFICER or PLATFORM_ADMIN role
          (b) Active audit log entry created before number is decrypted
          (c) Reason string mandatory
          Absence of any condition → ACCESS DENIED
SEC-11  : PII encryption at rest — phone numbers encrypted with AES-256 in PostgreSQL
          using Vault-managed encryption keys
SEC-12  : All Kafka events containing phone data use phone_hash only — never E.164
```

---

## SECTION 15 — FAILURE HANDLING (DETERMINISTIC)

| Failure Scenario | Agent Action | Recovery Path |
|---|---|---|
| Jasmin (PROV-01) unreachable >10s | SUSPEND PROV-01 · activate PROV-02 · emit `provider.failover` | Auto-restore when PROV-01 health poll passes 3x |
| All providers unreachable | HALT all OTP delivery · return 503 with `phone_trust_degraded=true` | Manual admin intervention required |
| Redis unreachable | HALT OTP issuance (no rate state) · return 503 · alert | Auto-retry 30s · escalate after 2 min |
| Kafka unreachable | OTP events buffered locally (5 min max) · emit backpressure signal | Auto-reconnect · emit buffered events on restore |
| PostgreSQL unreachable | HALT all contract reads/writes · return 503 | Database HA failover (k3s persistent volume) |
| OPA policy engine unreachable | DENY ALL contract operations (fail-closed) | Alert + await OPA recovery |
| Immutable Archive write failure | ROLLBACK trust binding activation · return 500 | Retry 3x · escalate to Compliance Officer |
| DLT registration expired | HALT all SMS delivery for tenant · notify Institute Admin | Admin must re-register DLT entity |

**Partial failure is NOT permitted. Fail-closed in all cases.**

---

## SECTION 16 — INDIA-SPECIFIC COMPLIANCE (DLT ENFORCEMENT)

All SMS sent to Indian phone numbers (country code +91) must comply with TRAI regulations enforced via the DLT (Distributed Ledger Technology) platform.

```
INDIA-01  : DLT Entity ID must be registered for every sending organization
INDIA-02  : Every SMS template must have an approved DLT Template ID
INDIA-03  : Template body must EXACTLY match the approved DLT template
            Hash mismatch → BLOCK SEND + TEMPLATE_TAMPER abuse signal
INDIA-04  : OTP SMS falls under TRANSACTIONAL category — always permitted (DND bypass)
INDIA-05  : Placement drive / job alert SMS falls under PROMOTIONAL category
            — blocked for DND-registered numbers
INDIA-06  : DLT Template ID stored in phone_api_contracts.dlt_template_id
INDIA-07  : DLT Entity ID stored in phone_api_contracts.dlt_entity_id
INDIA-08  : Absence of both fields for +91 numbers → REJECT contract creation
INDIA-09  : Sender ID (Header) must be 6-character alphanumeric as registered on DLT
            Mismatch → provider rejects delivery — logged as DELIVERY_FAILED
INDIA-10  : Telemarketer ID must be present in Jasmin route config for Indian SMS
```

---

## SECTION 17 — DEPLOYMENT SPECIFICATION

### 17.1 Kubernetes Manifest Requirements

```yaml
namespace: ecoskiller-trust

Deployment:
  replicas: 2 (minimum) · 5 (maximum, HPA)
  resources:
    requests: { cpu: 250m, memory: 512Mi }
    limits:   { cpu: 1000m, memory: 1Gi }
  readinessProbe:
    httpGet: { path: /actuator/health/readiness, port: 8080 }
    initialDelaySeconds: 15
    periodSeconds: 10
  livenessProbe:
    httpGet: { path: /actuator/health/liveness, port: 8080 }
    initialDelaySeconds: 30
    periodSeconds: 20
  env:
    - name: JASMIN_HTTP_ENDPOINT
      valueFrom: { secretKeyRef: { name: jasmin-secret, key: http_endpoint } }
    - name: REDIS_URL
      valueFrom: { secretKeyRef: { name: redis-secret, key: url } }
    - name: KAFKA_BOOTSTRAP
      valueFrom: { configMapKeyRef: { name: kafka-config, key: bootstrap_servers } }
    - name: OTP_BCRYPT_COST
      value: "10"
    - name: OTP_TTL_SECONDS
      value: "300"
    - name: PHONE_HASH_ALGORITHM
      value: "SHA-256"
```

### 17.2 Environment Variable Requirements (All Environments)

```
REQUIRED (Vault-injected — never hardcoded):
  JASMIN_HTTP_ENDPOINT
  JASMIN_USERNAME
  JASMIN_PASSWORD
  POSTGRES_URL
  POSTGRES_USERNAME
  POSTGRES_PASSWORD
  REDIS_URL
  REDIS_PASSWORD
  KAFKA_BOOTSTRAP_SERVERS
  VAULT_ADDR
  OPA_URL
  DLT_ENTITY_ID_DEFAULT (platform-level default)
  IMMUTABLE_ARCHIVE_URL
  WAZUH_WEBHOOK_URL

CONFIGURABLE (ConfigMap):
  OTP_TTL_SECONDS (default: 300)
  OTP_MAX_ATTEMPTS (default: 3)
  OTP_DAILY_LIMIT (default: 5)
  OTP_FLOOD_WINDOW_SECONDS (default: 60)
  PROVIDER_HEALTH_POLL_INTERVAL_SECONDS (default: 30)
  PROVIDER_FAILOVER_THRESHOLD (default: 3 consecutive failures)
  PHONE_TRUST_SCORE_VERIFY_BONUS (default: 15)
  PHONE_TRUST_SCORE_ABUSE_PENALTY (default: 25)
  CAMPAIGN_DEFAULT_RATE_PER_HOUR (default: 500)
```

---

## SECTION 18 — CONTRACT GATE CHECKLIST

All items must be TRUE before agent is declared `phone_trust_ready`.

```
[ ] phone_contract_registry schema migrated (Flyway)
[ ] otp_ledger table created and indexed
[ ] phone_trust_bindings table created
[ ] phone_abuse_signals table created
[ ] Redis connection verified · all key namespaces reachable
[ ] Jasmin SMS Gateway health poll passes (PROV-01)
[ ] Kafka producer connected · phone.events topic created
[ ] Kafka consumer connected · fraud.signals.phone topic subscribed
[ ] OPA policy bundle loaded · phone_contract policies active
[ ] Keycloak JWT validation config active
[ ] Vault service token injected · secrets decryptable
[ ] DLT credentials verified (India tenants)
[ ] Immutable Archive Service endpoint reachable
[ ] Wazuh webhook endpoint reachable
[ ] Prometheus metrics endpoint /metrics exposed
[ ] Loki log shipping active
[ ] OpenTelemetry collector reachable
[ ] All alerting rules deployed to Grafana/Alertmanager
[ ] Admin Governance Service subscribed to phone.events (Kafka)
[ ] Fraud Detection Engine subscribed to phone.events (Kafka)
```

**All 20 checks must pass → `phone_trust_ready = TRUE` emitted.**  
**Any failure → STOP EXECUTION → REPORT PHONE-TRUST-NOT-READY → block dependent services.**

---

## SECTION 19 — MUTATION POLICY

```
ALLOWED        : Adding new contract types (via version bump)
ALLOWED        : Adding new abuse signal types (via version bump)
ALLOWED        : Adding new provider registrations (human approval + version bump)
ALLOWED        : Extending schema with optional fields (backward compatible only)

FORBIDDEN      : Removing existing contract types
FORBIDDEN      : Changing OTP TTL below 60 seconds or above 600 seconds without compliance review
FORBIDDEN      : Removing DLT enforcement for India
FORBIDDEN      : Changing phone hash algorithm without full data migration plan
FORBIDDEN      : Adding plaintext phone storage anywhere in data model
FORBIDDEN      : Removing the fail-closed policy from OPA integration
FORBIDDEN      : Adding AI/ML judgment to OTP validation or contract approval
               (This agent is deterministic — human or rule-based only)
```

---

## SECTION 20 — VERSION HISTORY

| Version | Status | Change |
|---|---|---|
| v1.0 | SEALED · ACTIVE | Initial declaration — all sections locked |

---

**DOCUMENT END**  
**PHONE_API_CONTRACT_REGISTRY_AGENT v1.0**  
**ECOSKILLER — ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE — ANTIGRAVITY**  
`SEALED · LOCKED · GOVERNED · ADD-ONLY`
