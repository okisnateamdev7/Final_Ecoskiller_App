# SMS_GATEWAY_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### ANTIGRAVITY PLATFORM — ECOSKILLER UNIFIED SaaS

---

```
Artifact Class:     ENTERPRISE AGENT SPECIFICATION
Agent ID:           ANTIGRAVITY-SGA-001
Version:            v1.0.0
Status:             FINAL · LOCKED · SEALED · GOVERNED
Mutation Policy:    Add-only via version bump
Interpretation Authority: NONE
Execution Authority: Human declaration + CI/CD gate only
Parent System:      ECOSKILLER MASTER EXECUTION PROMPT v12.0
Bound to Sections:  R2 · R4 · R10 · R12 · R19 · R21 · R28 · R38 · R39-D
                    R40 · R47 · R49 · R50 · R51 · R60 · NAA-001
Platform Scope:     Ecoskiller Core + Dojo SaaS + Voice GD System
                    + Antigravity Intelligence Layer
Implementation:     Jasmin SMS Gateway (self-hosted, open-source)
Regulatory Scope:   TRAI DLT (India) · GDPR (EU) · TCPA (US) ·
                    PDPA (SEA) · Multi-region compliant
```

---

> **AGENT DECLARATION**
> The SMS_GATEWAY_AGENT (SGA) is a fully self-hosted, rule-governed,
> multi-tenant SMS orchestration agent built on Jasmin SMS Gateway.
> It does not interpret message urgency.
> It does not infer delivery intent.
> It executes registered send policies, enforces regulatory compliance
> per destination country, guarantees delivery audit trails, and
> manages carrier routing through deterministic priority rules.
> No commercial SaaS gateway dependency exists.
> No per-message vendor cost is incurred.
> Only rules, routing, enforcement, and logs remain.

---

## SECTION I — AGENT IDENTITY & GOVERNANCE

### I.A — Agent Classification

| Property              | Value                                                               |
|-----------------------|---------------------------------------------------------------------|
| Agent Name            | SMS_GATEWAY_AGENT                                                   |
| Short Code            | SGA                                                                 |
| Agent Type            | Outbound SMS Orchestration & Delivery Agent                         |
| Decision Model        | Deterministic Rule Engine (R28-1 — zero ML permitted)              |
| Execution Paradigm    | Request-in → Policy-check → Route-select → Carrier-send → Audit   |
| Failure Mode          | STOP → LOG → RETRY via backoff → ESCALATE on breach               |
| Human Override        | Admin-triggered via Internal Ops Console (R40) only               |
| Gateway Stack         | Jasmin SMS Gateway (self-hosted) + SMPP connectors                 |
| State Engine          | Redis (OTP locks, rate limits, carrier state, dedup windows)       |
| Persistent Storage    | PostgreSQL (delivery records, templates, carrier config, audit)    |
| Parent Agent Binding  | NOTIFICATION_AUTOMATION_AGENT (NAA-001) — SGA is a child agent     |
| Kubernetes Namespace  | `core` (co-located with auth, users)                               |
| Internal Service URL  | `http://sms-gateway-agent:8020`                                    |

### I.B — Operational Boundaries

The SGA is **explicitly NOT authorized** to:
- Send promotional / marketing SMS without explicit double opt-in
- Store full phone numbers in plain text anywhere outside encrypted vault
- Attempt message delivery to numbers on the national DND registry
- Retry OTP SMS beyond defined attempt ceilings
- Access or log SMS content after delivery confirmation
- Bypass DLT (Distributed Ledger Technology) headers for India routes
- Share message content with analytics pipelines

The SGA **is authorized** to:
- Send transactional SMS across all registered trigger categories
- Route messages across multiple SMPP carrier connectors
- Enforce per-country regulatory compliance automatically
- Apply intelligent failover between primary and backup carriers
- Generate immutable per-message delivery audit records
- Throttle, batch, and window message dispatch per carrier contract
- Produce full delivery receipt (DLR) processing and reconciliation

### I.C — Relationship to NAA

```
NOTIFICATION_AUTOMATION_AGENT (NAA-001)
          │
          │ Routes SMS-channel payloads to:
          ▼
  SMS_GATEWAY_AGENT (SGA-001)
          │
          ├── OTP Delivery Pipeline
          ├── Transactional Alert Pipeline
          ├── GD Reminder Pipeline
          └── Compliance Notification Pipeline
          │
          ▼
  Jasmin SMPP Engine → Carrier Connectors → Mobile Handsets
```

NAA is the trigger authority.
SGA is the execution authority.
Jasmin is the transport layer.
Carriers are the physical medium.
None below NAA decides what to send.

---

## SECTION II — GATEWAY ARCHITECTURE (NON-NEGOTIABLE)

### II.A — Separation of Responsibilities

```
Layer                        Responsibility
────────────────────────────────────────────────────────────────────
NAA Event Consumer           Receives SMS dispatch requests from NAA
Message Policy Enforcer      Validates message type, recipient,
                             regulatory compliance, DND check
OTP Pipeline                 Dedicated fast-lane for authentication SMS
Transactional Pipeline       Standard lane for all non-OTP messages
Carrier Router               Selects optimal carrier connector
                             based on: country · carrier · cost · speed
Jasmin SMPP Engine           Low-level SMPP session management
                             with registered carriers
DLR Processor                Delivery receipt ingestion and
                             status reconciliation
Retry Orchestrator           Exponential backoff on carrier failure
                             with automatic failover
Compliance Engine            Country-specific regulatory enforcement
                             (DLT headers, DND, character limits)
Audit Logger                 Immutable append-only delivery record
Redis State Manager          OTP locks · rate windows · dedup keys ·
                             carrier health state · concurrent session count
```

**Critical Principle:**
Jasmin routes bytes. It decides nothing about policy.
The Message Policy Enforcer decides everything about compliance.
Redis enforces all state contracts.
PostgreSQL holds the permanent truth.

### II.B — Dual Pipeline Architecture

```
PIPELINE A — OTP FAST LANE
  Priority:    CRITICAL (bypasses all rate limits except OTP ceiling)
  Latency SLA: < 3 seconds end-to-end (request → DLR confirmation)
  Carrier:     Primary carrier only (no failover delay on first attempt)
  Max retries: 2 (then dead-letter, no further OTP retry)
  Content:     Numeric OTP only — no dynamic text allowed
  Logging:     OTP value NEVER logged — only masked hash stored

PIPELINE B — TRANSACTIONAL LANE
  Priority:    HIGH / MEDIUM / LOW (NAA-assigned)
  Latency SLA: < 30 seconds (HIGH) · < 5 minutes (MEDIUM/LOW)
  Carrier:     Primary → Failover carrier on 2nd attempt
  Max retries: 5 (exponential backoff — see Section VI)
  Content:     Templated transactional messages only
  Logging:     Full content hash stored, rendered body NOT stored
```

---

## SECTION III — JASMIN SMS GATEWAY CONFIGURATION (LOCKED)

### III.A — What Jasmin Provides

```
Component                     Role in SGA
──────────────────────────────────────────────────────────
Jasmin SMPP Client            SMPP v3.4 connection to carrier SMSCs
Jasmin HTTP API               Internal REST API for SGA to submit messages
Jasmin Router                 Keyword/prefix-based route selection
Jasmin Interceptors           Pre-send hooks for compliance injection
Jasmin DLR Broker             Delivery receipt processing via Redis PubSub
Jasmin Queue (Redis-backed)   Durable message queue before carrier send
Jasmin Rate Limiter           Carrier-contract throughput enforcement (TPS)
Jasmin CLI / jCli             Admin interface for connector management
```

### III.B — Jasmin Does NOT Decide

```
✗ Which users to message
✗ Whether a message is regulatory compliant
✗ OTP generation or validation
✗ DND registry lookup
✗ Template selection
✗ Message priority
✗ Retry policy beyond carrier-level
```

All above decisions are governed by the SGA Policy Engine.
Jasmin is instructed what to send and to whom.
Jasmin confirms delivery or reports failure.
Jasmin stores nothing permanently.

### III.C — SMPP Connector Registry (Locked Structure)

Every carrier connector must declare:

```yaml
connector_id:       string          # e.g. "vodafone_india_primary"
smpp_host:          string          # Carrier SMSC IP/hostname
smpp_port:          integer         # Standard: 2775 (TLS: 3550)
system_id:          string          # Carrier-assigned account ID (Vault-stored)
password:           vault_ref       # Never stored in config — Vault only
system_type:        string          # Carrier-specific (e.g. "TRANSactional")
source_addr_ton:    integer         # 5 for alphanumeric sender ID
source_addr_npi:    integer         # 0 for unknown
dest_addr_ton:      integer         # 1 for international
dest_addr_npi:      integer         # 1 for ISDN
enquire_link_timer: integer         # SMPP keep-alive interval (seconds)
submit_sm_timeout:  integer         # Max wait for carrier ack (seconds)
max_tps:            integer         # Max messages per second (carrier contract)
active:             boolean
country_codes:      [list]          # ISO-3166 country codes this connector serves
priority:           integer         # 1 = primary, 2 = secondary/failover
```

### III.D — Mandatory Connector Slots (Minimum)

| Connector ID                  | Country | Role      | TPS  |
|-------------------------------|---------|-----------|------|
| `primary_india_transact`       | IN      | Primary   | 100  |
| `failover_india_transact`      | IN      | Failover  | 50   |
| `primary_india_otp`            | IN      | OTP-only  | 200  |
| `primary_global_transact`      | INTL    | Primary   | 30   |
| `failover_global_transact`     | INTL    | Failover  | 20   |

Connectors are registered in Jasmin via jCli.
Credentials stored exclusively in HashiCorp Vault.
No connector credential may appear in config files, env files, or logs.

---

## SECTION IV — COMPLETE SMS MESSAGE TYPE REGISTRY

> **ENFORCEMENT:** Every message type below must have a registered
> template in the Template Registry and a DLT-approved template ID
> for India routes. Absence → STOP EXECUTION.

### IV.A — OTP & Authentication Messages

| MSG ID        | Purpose                           | DLT Category    | Max Length |
|---------------|-----------------------------------|-----------------|------------|
| SGA-OTP-001   | Registration OTP                  | Transactional   | 160 chars  |
| SGA-OTP-002   | Login OTP (new device)            | Transactional   | 160 chars  |
| SGA-OTP-003   | Password reset OTP                | Transactional   | 160 chars  |
| SGA-OTP-004   | MFA verification OTP              | Transactional   | 160 chars  |
| SGA-OTP-005   | Phone number verification OTP     | Transactional   | 160 chars  |
| SGA-OTP-006   | Payment authorization OTP         | Transactional   | 160 chars  |
| SGA-OTP-007   | High-value action confirmation OTP| Transactional   | 160 chars  |
| SGA-OTP-008   | Parent consent OTP (royalty)      | Transactional   | 160 chars  |

**OTP Enforcement Rules (non-negotiable):**
```
1. OTP value: 6 digits, cryptographically random (secrets module)
2. OTP TTL:   300 seconds (5 minutes) — enforced in Redis
3. OTP attempts: Max 3 per session — 4th attempt → session invalidated
4. OTP resend: Max 3 requests per phone per 15 minutes
5. OTP value: NEVER logged in any system — only SHA-256(otp+salt) stored
6. OTP channel: SMS only for SGA-OTP-006, SGA-OTP-007 (no email fallback)
7. OTP rotation: New OTP invalidates all previous OTPs for same user+purpose
```

### IV.B — Voice GD System SMS Messages

> Bound to: AUTOMATED VOICE GROUP DISCUSSION SYSTEM specification.

| MSG ID        | Purpose                              | DLT Category  | Trigger Bound |
|---------------|--------------------------------------|---------------|---------------|
| SGA-GD-001    | GD session scheduled confirmation    | Transactional | NAA-GD-002    |
| SGA-GD-002    | GD reminder — 24 hours before        | Transactional | NAA-GD-003    |
| SGA-GD-003    | GD reminder — 1 hour before          | Transactional | NAA-GD-004    |
| SGA-GD-004    | GD reminder — 15 minutes before      | Transactional | NAA-GD-005    |
| SGA-GD-005    | GD session cancelled alert           | Transactional | NAA-GD-018    |
| SGA-GD-006    | GD score published notification      | Transactional | NAA-GD-014    |
| SGA-GD-007    | GD batch rescheduled alert           | Transactional | N/A           |

**GD SMS Criticality Rule:**
SGA-GD-003 (1-hour reminder) and SGA-GD-004 (15-minute reminder)
are classified CRITICAL-tier.
They bypass rate-limit windows.
They use OTP Pipeline (fast-lane) delivery.
Failure to deliver either → emit `sga.critical_delivery_failure` event
→ immediate Grafana P0 alert.

### IV.C — Job & Application SMS Messages

| MSG ID        | Purpose                              | DLT Category  | Trigger Bound |
|---------------|--------------------------------------|---------------|---------------|
| SGA-JOB-001   | Application shortlisted alert        | Transactional | NAA-JOB-004   |
| SGA-JOB-002   | Offer received — action required     | Transactional | NAA-JOB-006   |
| SGA-JOB-003   | Interview scheduled confirmation     | Transactional | N/A           |
| SGA-JOB-004   | Interview reminder — 1 hour          | Transactional | N/A           |

### IV.D — Dojo & Belt SMS Messages

| MSG ID        | Purpose                              | DLT Category  | Trigger Bound |
|---------------|--------------------------------------|---------------|---------------|
| SGA-DOJO-001  | Belt promotion confirmed             | Transactional | NAA-DOJO-010  |
| SGA-DOJO-002  | Tournament round starting — 15 min   | Transactional | NAA-DOJO-014  |
| SGA-DOJO-003  | Tournament winner declaration        | Transactional | NAA-DOJO-015  |

### IV.E — Billing & Payment SMS Messages

| MSG ID        | Purpose                              | DLT Category  | Trigger Bound |
|---------------|--------------------------------------|---------------|---------------|
| SGA-BILL-001  | Payment failure — action required    | Transactional | NAA-BILL-008  |
| SGA-BILL-002  | Subscription expiring — 3 days       | Transactional | NAA-BILL-003  |
| SGA-BILL-003  | Payout processed confirmation        | Transactional | NAA-BILL-013  |
| SGA-BILL-004  | Royalty payment credited             | Transactional | NAA-INN-005   |
| SGA-BILL-005  | Refund processed confirmation        | Transactional | NAA-BILL-011  |

### IV.F — Security & Governance SMS Messages

| MSG ID        | Purpose                              | DLT Category  | Trigger Bound |
|---------------|--------------------------------------|---------------|---------------|
| SGA-GOV-001   | Suspicious login detected            | Transactional | NAA-ID-014    |
| SGA-GOV-002   | Password changed — not you? act now  | Transactional | NAA-ID-007    |
| SGA-GOV-003   | Account suspended notice             | Transactional | NAA-GOV-005   |
| SGA-GOV-004   | Complaint escalation confirmed       | Transactional | NAA-SOC-010   |

### IV.G — Innovation & Royalty SMS Messages

| MSG ID        | Purpose                              | DLT Category  | Trigger Bound |
|---------------|--------------------------------------|---------------|---------------|
| SGA-INN-001   | Idea licensing agreement signed      | Transactional | NAA-INN-003   |
| SGA-INN-002   | Royalty payout issued                | Transactional | NAA-INN-005   |
| SGA-INN-003   | Revenue anomaly detected (admin)     | Transactional | NAA-INN-006   |

---

## SECTION V — MESSAGE POLICY ENGINE (LOCKED)

### V.A — Pre-Send Policy Check Sequence

```
Every outbound SMS must pass this sequence in order.
Any FAIL at any step = message dropped, reason logged.

Step 1: PHONE NUMBER VALIDATION
  → E.164 format check (+[country][number])
  → Minimum/maximum digit count by country
  → Virtual/VOIP number detection (block if flagged)
  FAIL → DROP · LOG: INVALID_PHONE · no retry

Step 2: DND REGISTRY CHECK (India only)
  → Query national DND registry cache (refreshed every 24h)
  → If number on DND AND message type is NOT Transactional
  FAIL → DROP · LOG: DND_BLOCKED · no retry
  PASS for Transactional → continue regardless of DND status
  (Transactional messages are DND-exempt under TRAI rules)

Step 3: USER CONSENT CHECK
  → Verify phone number is verified (phone_verified = TRUE in User Service)
  → Verify user has not opted out of SMS channel in Preference Service
  → Exception: OTP messages bypass opt-out check (CRITICAL tier)
  FAIL → DROP · LOG: CONSENT_MISSING · no retry

Step 4: RATE LIMIT CHECK (Redis)
  → Check per-user, per-message-category windows (Section VII.C)
  → OTP: check OTP resend ceiling
  → Transactional: check rolling window
  FAIL → QUEUE for next available window · LOG: RATE_LIMITED

Step 5: DEDUPLICATION CHECK (Redis)
  → Key: sga:dedup:{user_id}:{msg_id}:{entity_id}
  → TTL: configurable per message type (default: 120 seconds)
  FAIL → DROP · LOG: DUPLICATE_SUPPRESSED · no retry

Step 6: DLT COMPLIANCE CHECK (India routes)
  → Verify sender ID is DLT-registered
  → Verify template is DLT-approved (template_id present)
  → Verify message body matches DLT-approved template pattern
  → Inject DLT entity ID, template ID, sender ID into SMPP headers
  FAIL → DROP · LOG: DLT_VIOLATION · ALERT admin · no retry

Step 7: CHARACTER LIMIT CHECK
  → Latin: 160 chars (1 segment) · 306 chars (2 segments — warning)
  → Unicode (Hindi/Regional): 70 chars (1 segment)
  → If body exceeds 2 segments: TRUNCATE with notice or REJECT
  FAIL → LOG: CONTENT_OVERFLOW · send truncated if safe

Step 8: CARRIER ROUTING SELECTION
  → Route selected based on country code, priority, current health
  → See Section VIII (Carrier Routing Engine)
  PASS → dispatch to Jasmin

Step 9: DISPATCH TO JASMIN HTTP API
  → POST message to Jasmin internal endpoint
  → Receive Jasmin message ID
  → Store Jasmin message ID in dispatch log

Step 10: IMMUTABLE AUDIT LOG WRITE
  → Record: msg_id · user_id · phone_hash · carrier · status · timestamp
  → Phone stored as: SHA-256(E164_number + tenant_salt)
  → Content stored as: SHA-256(rendered_body)
  → Never store: plain phone number · OTP value · rendered content
```

### V.B — Message Type Classification Matrix

```
Type             DND Exempt  OTP Lane  Rate Limited  Audit Required  DLT Required (IN)
─────────────────────────────────────────────────────────────────────────────────────
OTP              YES         YES       YES (ceiling)  YES             YES
GD-CRITICAL      YES         YES       NO             YES             YES
Transactional    YES         NO        YES (window)   YES             YES
Promotional      NO          NO        YES            YES             YES (blocked on DND)
```

**Promotional SMS is FORBIDDEN on the Antigravity platform.**
The SGA will reject any dispatch request with type=PROMOTIONAL.
No marketing SMS may be sent through SGA under any circumstances.
Violation → LOG: PROMOTIONAL_SMS_BLOCKED · ALERT ops · discard.

---

## SECTION VI — DELIVERY PIPELINE & RETRY ORCHESTRATOR

### VI.A — OTP Pipeline (Fast Lane) Flow

```
[1] OTP REQUEST RECEIVED (from Auth Service via NAA)
        ↓
[2] POLICY CHECKS (Steps 1-6 from Section V.A)
        ↓
[3] OTP LOCK SET IN REDIS
    Key: sga:otp:lock:{user_id}:{purpose}
    TTL: 300 seconds
    Value: SHA-256(otp_value + user_salt)
        ↓
[4] MESSAGE RENDERED (template + OTP value injected)
        ↓
[5] DISPATCHED TO JASMIN (primary carrier, no routing delay)
        ↓
[6] JASMIN ACK RECEIVED (message ID stored)
        ↓
[7] DLR CONFIRMATION WAIT (max 30 seconds)
    ├── DLR: DELIVERED    → [8] SUCCESS
    └── DLR: FAILED/ABSENT → [ATTEMPT 2 via primary]
                             → If attempt 2 fails → [DEAD LETTER]
                             → emit: sga.otp_delivery_failed
                             → P0 alert fired
        ↓
[8] AUDIT LOG WRITTEN (OTP value never stored)
        ↓
[9] REDIS LOCK UPDATED: status = DELIVERED
```

### VI.B — Transactional Pipeline Flow

```
[1] DISPATCH REQUEST (from NAA)
        ↓
[2] FULL POLICY CHECKS (all 10 steps — Section V.A)
        ↓
[3] CARRIER SELECTED (Section VIII)
        ↓
[4] MESSAGE QUEUED IN JASMIN REDIS QUEUE
        ↓
[5] JASMIN SUBMITS TO CARRIER SMSC
        ↓
[6] DLR PROCESSING
    ├── DELIVERED         → [7] SUCCESS LOG
    ├── UNDELIVERABLE     → DEAD LETTER (no retry — number issue)
    ├── EXPIRED           → [RETRY orchestrator]
    └── FAILED            → [RETRY orchestrator]
        ↓
[7] IMMUTABLE AUDIT LOG
        ↓
[8] EVENT EMITTED: sga.message_delivered / sga.message_failed
```

### VI.C — Retry Schedule (Transactional)

```
Attempt 1:   Immediate (primary carrier)
Attempt 2:   +45 seconds (primary carrier)
Attempt 3:   +3 minutes (failover carrier)
Attempt 4:   +10 minutes (failover carrier)
Attempt 5:   +30 minutes (failover carrier)

After 5 failures:
  → Status: DEAD_LETTER
  → Write: sms_dead_letter table
  → Emit: sga.message_dead_lettered
  → Increment: NAA failure counter (for alternate channel fallback)
  → NAA may escalate to EMAIL channel automatically
```

### VI.D — DLR (Delivery Receipt) Processing Contract

```
Jasmin DLR Broker → Redis PubSub channel: jasmin:dlr
SGA DLR Processor subscribes to: jasmin:dlr

On DLR received:
  → Parse: message_id · recipient · status · error_code · timestamp
  → Match to: sms_dispatch_log by jasmin_message_id
  → Update: delivery_status in dispatch log
  → If status = UNDELIVERABLE:
      → Mark phone_number as: delivery_failed_count++
      → If delivery_failed_count >= 3:
          → Flag phone as: UNDELIVERABLE_NUMBER
          → Suppress future SMS to this number
          → Notify user via email (fallback channel)
  → Write final status to audit record
```

---

## SECTION VII — RATE LIMIT POLICY (LOCKED)

### VII.A — OTP Rate Ceilings

```
Per-user OTP limits (Redis TTL windows):

OTP resend per phone per 15 minutes:    Max 3 requests
OTP resend per phone per 1 hour:        Max 5 requests
OTP resend per phone per 24 hours:      Max 10 requests
Concurrent active OTP sessions:         Max 2 (per user)

Breach of any ceiling:
  → Block further OTP SMS
  → Log: OTP_RATE_EXCEEDED
  → Return error to requesting service: 429 TOO_MANY_REQUESTS
  → Do NOT silently drop — caller must know
```

### VII.B — Transactional Rate Windows

| Message Category | Window    | Max Messages | Burst Cap |
|------------------|-----------|--------------|-----------|
| AUTH             | 60 min    | 10           | 3/min     |
| GD               | 24 hours  | 8            | 2/hour    |
| JOB              | 24 hours  | 5            | 1/hour    |
| DOJO             | 24 hours  | 5            | 1/hour    |
| BILLING          | 24 hours  | 8            | 2/hour    |
| GOV              | 24 hours  | 10           | 3/hour    |
| INN              | 24 hours  | 5            | 1/hour    |

### VII.C — Platform-Wide Throughput Caps

```
Total platform SMS per minute:     Configurable (default: 500/min)
Total platform SMS per hour:       Configurable (default: 10,000/hr)
OTP pipeline reserved capacity:    30% of total platform TPS
Transactional pipeline capacity:   70% of total platform TPS

Carrier TPS enforcement:
  → Per-connector TPS enforced by Jasmin Rate Limiter
  → Excess messages queued in Jasmin Redis queue (durable)
  → Queue depth monitored via Prometheus metric: sga_queue_depth
```

---

## SECTION VIII — CARRIER ROUTING ENGINE

### VIII.A — Routing Decision Matrix

```
Algorithm (executed per-message):

1. Extract destination country code from E.164 number
2. Filter connectors by: country_code match + active = TRUE
3. Sort by priority ASC (1 = primary)
4. Select primary connector

If primary connector health_status = DEGRADED:
  → Select next connector by priority
  → Log: sga.carrier_failover_triggered

Health status is maintained in Redis:
  Key: sga:carrier:health:{connector_id}
  Values: HEALTHY · DEGRADED · DOWN
  TTL: 60 seconds (auto-expires — refresh every 30s by health probe)
```

### VIII.B — Carrier Health Probe Contract

```
Probe interval:    Every 30 seconds
Probe method:      Jasmin SMPP enquire_link to each connector
Success threshold: Response within enquire_link_timer
Failure threshold: 3 consecutive failures → status = DEGRADED
Recovery:          2 consecutive successes → status = HEALTHY

Probe metrics published to Prometheus:
  sga_carrier_health{connector_id} = 1 (HEALTHY) / 0 (DOWN)
  sga_carrier_response_ms{connector_id}
```

### VIII.C — India-Specific Routing Rules

```
DLT Template Routing:
  → India-bound messages MUST use a DLT-registered connector
  → Connector system_type MUST be "TRANS" (transactional)
  → Promotional connector blocked for all India routes
  → Sender ID must match DLT-registered entity

Sender ID Rules (India):
  → Registered sender: ANTIGRAVITY (6-char alpha, DLT-registered)
  → OTP sender:        ATVGTY (DLT-registered OTP sender ID)
  → Failover sender:   Must also be DLT-registered
  → Using unregistered sender → TRAI violation → BLOCKED
```

---

## SECTION IX — REGULATORY COMPLIANCE ENGINE (LOCKED)

### IX.A — India TRAI DLT Compliance

```
DLT = Distributed Ledger Technology (mandated by TRAI since 2021)

Required DLT registrations:
  1. Principal Entity (PE) registration — Antigravity entity
  2. Telemarketer (TM) registration — Jasmin as TM
  3. Sender ID registration (e.g. ANTIGRAVITY, ATVGTY)
  4. Content Template registration (each SMS template separately)

Per-message DLT injection (automatic, via Jasmin Interceptor):
  → X-Entity-ID:   Registered PE Entity ID
  → X-Template-ID: Registered template ID matching message body
  → Source Address: Registered sender ID

Template fingerprint matching:
  → Before send, SGA computes: normalize(rendered_body)
  → Compares against registered template pattern
  → Variable slots verified: only declared variables may differ
  → Deviation → DLT_MISMATCH → DROP → ALERT admin

Penalty avoidance:
  → Non-compliant messages are blocked BEFORE reaching carrier
  → Carrier-side rejection due to DLT failure → triggers P1 alert
  → DLT template registry synced weekly via admin tool
```

### IX.B — GDPR Compliance (EU Routes)

```
Right to erasure:
  → Phone numbers stored as SHA-256(E164 + tenant_salt) only
  → On GDPR erasure request: rotate tenant_salt → all hashes invalidated
  → No E.164 number recoverable after salt rotation

Consent logging:
  → phone_consent_log table records: user_id · consent_type · timestamp
  → Consent required before any non-OTP SMS
  → OTP consent implied by account creation (service necessity)

Data residency:
  → EU user phone hashes stored in EU-region PostgreSQL instance
  → No cross-region phone hash replication
```

### IX.C — Multi-Region Compliance Matrix

| Region        | Framework  | Key Requirement                               | SGA Enforcement               |
|---------------|------------|-----------------------------------------------|-------------------------------|
| India (IN)    | TRAI DLT   | DLT registration, template approval           | Jasmin Interceptor injection  |
| EU            | GDPR       | Consent, erasure, data residency              | Hash-only storage, salt-erase |
| US            | TCPA       | Express written consent for SMS               | Consent gate pre-send         |
| SEA           | PDPA       | Explicit opt-in, purpose limitation           | Consent gate + purpose tag    |
| UK            | UK GDPR    | Same as EU GDPR post-Brexit                   | Same as GDPR enforcement      |
| AU            | Privacy Act | Opt-out mechanism required                   | Unsubscribe reply handler     |

---

## SECTION X — DATABASE SCHEMA (LOCKED)

### X.A — Core SGA Tables

```sql
-- SMS Dispatch Record (append-only, immutable)
sms_dispatch_log (
  id                    UUID PRIMARY KEY,
  user_id               UUID NOT NULL,
  tenant_id             UUID NOT NULL,
  msg_type_id           TEXT NOT NULL,        -- Bound to Section IV registry
  naa_trigger_id        TEXT,                 -- Parent NAA trigger if applicable
  phone_hash            TEXT NOT NULL,        -- SHA-256(E164 + tenant_salt)
  country_code          TEXT NOT NULL,        -- ISO-3166-1 alpha-2
  connector_id          TEXT NOT NULL,        -- Bound to Section III.D registry
  jasmin_message_id     TEXT,                 -- Returned by Jasmin on submit
  template_id           TEXT NOT NULL,
  dlt_template_id       TEXT,                 -- Null for non-India routes
  dlt_entity_id         TEXT,
  sender_id             TEXT NOT NULL,
  body_hash             TEXT NOT NULL,        -- SHA-256 of rendered body
  body_segment_count    INT NOT NULL,
  delivery_status       TEXT CHECK (status IN (
                          'QUEUED', 'SUBMITTED', 'DELIVERED',
                          'UNDELIVERABLE', 'EXPIRED', 'FAILED',
                          'DEAD_LETTER', 'SUPPRESSED', 'DND_BLOCKED',
                          'RATE_LIMITED', 'DLT_BLOCKED', 'DUPLICATE'
                        )),
  attempt_count         INT DEFAULT 0,
  submitted_at          TIMESTAMP,
  delivered_at          TIMESTAMP,
  dlr_received_at       TIMESTAMP,
  dlr_error_code        TEXT,
  failure_reason        TEXT,
  created_at            TIMESTAMP DEFAULT now()
  -- IMMUTABLE: No UPDATE. State changes via INSERT of new record.
)

-- Dead Letter Queue
sms_dead_letter (
  id                    UUID PRIMARY KEY,
  dispatch_log_id       UUID REFERENCES sms_dispatch_log(id),
  user_id               UUID NOT NULL,
  msg_type_id           TEXT NOT NULL,
  phone_hash            TEXT NOT NULL,
  final_failure_at      TIMESTAMP NOT NULL,
  failure_reason        TEXT,
  admin_reviewed        BOOLEAN DEFAULT FALSE,
  admin_reviewed_by     UUID,
  admin_reviewed_at     TIMESTAMP,
  escalated_to_email    BOOLEAN DEFAULT FALSE,
  escalated_at          TIMESTAMP
)

-- Undeliverable Number Registry
sms_undeliverable_numbers (
  id                    UUID PRIMARY KEY,
  phone_hash            TEXT UNIQUE NOT NULL,
  tenant_id             UUID NOT NULL,
  failure_count         INT DEFAULT 0,
  first_failure_at      TIMESTAMP,
  last_failure_at       TIMESTAMP,
  flagged_at            TIMESTAMP,
  resolved_at           TIMESTAMP
)

-- DLT Template Registry (India compliance)
sms_dlt_templates (
  id                    UUID PRIMARY KEY,
  template_id           TEXT UNIQUE NOT NULL, -- Internal SGA template ID
  dlt_template_id       TEXT NOT NULL,         -- TRAI DLT-registered ID
  dlt_entity_id         TEXT NOT NULL,
  sender_id             TEXT NOT NULL,
  template_pattern      TEXT NOT NULL,         -- Regex pattern for body validation
  msg_type_id           TEXT NOT NULL,
  active                BOOLEAN DEFAULT TRUE,
  registered_at         TIMESTAMP,
  expires_at            TIMESTAMP
)

-- Phone Consent Log (GDPR + TCPA)
sms_phone_consent_log (
  id                    UUID PRIMARY KEY,
  user_id               UUID NOT NULL,
  phone_hash            TEXT NOT NULL,
  consent_type          TEXT CHECK (type IN (
                          'SERVICE', 'TRANSACTIONAL', 'OTP', 'WITHDRAWN'
                        )),
  consent_source        TEXT,                  -- 'registration', 'settings', 'api'
  ip_hash               TEXT,
  created_at            TIMESTAMP NOT NULL,
  withdrawn_at          TIMESTAMP
)

-- Rate Limit Ledger (backup to Redis — persistence layer)
sms_rate_ledger (
  user_id               UUID NOT NULL,
  msg_category          TEXT NOT NULL,
  window_start          TIMESTAMP NOT NULL,
  message_count         INT DEFAULT 0,
  PRIMARY KEY (user_id, msg_category, window_start)
)
```

### X.B — Carrier Connector Config (Vault-referenced)

```sql
-- Connector registry (credentials NOT stored here — Vault only)
sms_carrier_connectors (
  id                    UUID PRIMARY KEY,
  connector_id          TEXT UNIQUE NOT NULL,
  smpp_host             TEXT NOT NULL,
  smpp_port             INT NOT NULL,
  vault_credential_path TEXT NOT NULL,        -- Path in HashiCorp Vault
  max_tps               INT NOT NULL,
  country_codes         TEXT[] NOT NULL,
  priority              INT NOT NULL,
  active                BOOLEAN DEFAULT TRUE,
  last_health_check     TIMESTAMP,
  health_status         TEXT DEFAULT 'HEALTHY',
  created_at            TIMESTAMP DEFAULT now()
)
```

---

## SECTION XI — SMS TEMPLATE SYSTEM (LOCKED)

### XI.A — Template Registry Contract

Every SMS template must declare:

```yaml
template_id:        string         # e.g. "tmpl_otp_registration_sms"
msg_type_id:        string         # Bound to Section IV registry
channel:            SMS
locale:             en-IN | en-US | hi-IN | ...
sender_id:          string         # Must match DLT-registered sender
body_template:      string         # Handlebars-style {{variable}} placeholders
variables_required: [list]         # All required context variables
max_length_chars:   integer        # Validated pre-send
dlt_template_id:    string | null  # Required for India routes
version:            semver string
```

### XI.B — Mandatory Template Set

| Template ID                           | Msg Type Bound  | Locale  |
|---------------------------------------|-----------------|---------|
| tmpl_otp_registration_sms             | SGA-OTP-001     | en-IN   |
| tmpl_otp_login_sms                    | SGA-OTP-002     | en-IN   |
| tmpl_otp_password_reset_sms           | SGA-OTP-003     | en-IN   |
| tmpl_otp_mfa_sms                      | SGA-OTP-004     | en-IN   |
| tmpl_otp_phone_verify_sms             | SGA-OTP-005     | en-IN   |
| tmpl_otp_payment_auth_sms             | SGA-OTP-006     | en-IN   |
| tmpl_otp_parent_consent_sms           | SGA-OTP-008     | en-IN   |
| tmpl_gd_scheduled_sms                 | SGA-GD-001      | en-IN   |
| tmpl_gd_reminder_24h_sms              | SGA-GD-002      | en-IN   |
| tmpl_gd_reminder_1h_sms               | SGA-GD-003      | en-IN   |
| tmpl_gd_reminder_15min_sms            | SGA-GD-004      | en-IN   |
| tmpl_gd_cancelled_sms                 | SGA-GD-005      | en-IN   |
| tmpl_job_shortlisted_sms              | SGA-JOB-001     | en-IN   |
| tmpl_job_offer_received_sms           | SGA-JOB-002     | en-IN   |
| tmpl_dojo_belt_promoted_sms           | SGA-DOJO-001    | en-IN   |
| tmpl_dojo_tournament_starting_sms     | SGA-DOJO-002    | en-IN   |
| tmpl_billing_payment_failed_sms       | SGA-BILL-001    | en-IN   |
| tmpl_billing_subscription_expiry_sms  | SGA-BILL-002    | en-IN   |
| tmpl_billing_payout_sms               | SGA-BILL-003    | en-IN   |
| tmpl_gov_suspicious_login_sms         | SGA-GOV-001     | en-IN   |
| tmpl_gov_password_changed_sms         | SGA-GOV-002     | en-IN   |
| tmpl_gov_suspended_sms                | SGA-GOV-003     | en-IN   |
| tmpl_inn_idea_licensed_sms            | SGA-INN-001     | en-IN   |
| tmpl_inn_royalty_payout_sms           | SGA-INN-002     | en-IN   |

### XI.C — Template Body Rules (Locked)

```
Rule 1: OTP body format:
  "Your {{purpose}} OTP for Antigravity is {{otp}}. Valid for 5 minutes.
   Do not share this with anyone. -ANTIGRAVITY"

Rule 2: All templates must end with: "-ANTIGRAVITY" (brand footer)

Rule 3: No URLs in OTP templates (phishing prevention)

Rule 4: No URLs in templates unless pre-registered in DLT
  (India) or domain-allowlisted (all other routes)

Rule 5: Hindi/regional language templates must use Unicode
  encoding — segment count doubled — validate before send

Rule 6: Template variables must not accept HTML — strip all
  HTML tags from injected variables before render

Rule 7: {{action_url}} if present must match R47 domain registry
  Any external URL in action_url → REJECT · LOG: URL_POLICY_VIOLATION
```

---

## SECTION XII — API CONTRACT REGISTRY (LOCKED)

### XII.A — Internal Service Endpoints

```
Base URL (Kubernetes internal): http://sms-gateway-agent:8020

POST  /internal/send              → Single SMS dispatch request
POST  /internal/send/batch        → Batch dispatch (max 100/request)
GET   /internal/status/{log_id}   → Delivery status query
POST  /internal/otp/send          → OTP-specific fast-lane endpoint
POST  /internal/otp/verify        → OTP verification (returns valid/invalid)
POST  /internal/otp/invalidate    → Force-invalidate active OTP
GET   /internal/carrier/health    → Live carrier health status
GET   /internal/dead-letters      → Dead letter queue
POST  /internal/retry/{log_id}    → Manual retry (admin only)
GET   /health                     → Service health check
```

### XII.B — Inbound Request Schema

```json
POST /internal/send
{
  "msg_type_id":   "SGA-GD-003",
  "user_id":       "uuid",
  "tenant_id":     "uuid",
  "naa_trigger_id": "NAA-GD-004",
  "context": {
    "user.full_name":   "Rahul Sharma",
    "session.topic":    "Digital Economy",
    "session.time":     "3:00 PM IST",
    "action_url":       "https://app.ecoskiller.com/gd/session/xyz"
  }
}

Response:
{
  "log_id":        "uuid",
  "status":        "QUEUED",
  "jasmin_msg_id": "optional — async",
  "estimated_delivery_ms": 3000
}
```

### XII.C — OTP Endpoints Schema

```json
POST /internal/otp/send
{
  "user_id":    "uuid",
  "phone":      "+919876543210",   // E.164 — only here, nowhere else stored
  "purpose":    "registration",    // Maps to SGA-OTP-001
  "tenant_id":  "uuid"
}

Response:
{
  "session_id":  "uuid",           // For tracking — no OTP in response
  "expires_at":  "ISO8601",
  "attempts_remaining": 3
}

POST /internal/otp/verify
{
  "session_id":  "uuid",
  "user_id":     "uuid",
  "otp_value":   "123456"          // Compared against Redis hash
}

Response:
{
  "valid":    true | false,
  "reason":   "VERIFIED" | "EXPIRED" | "INVALID" | "MAX_ATTEMPTS_EXCEEDED"
}
```

### XII.D — OpenAPI 3.1 Fragment

```yaml
openapi: 3.1.0
info:
  title: ANTIGRAVITY SMS Gateway Agent API
  version: 1.0.0

paths:
  /internal/send:
    post:
      summary: Dispatch a single transactional SMS
      requestBody:
        content:
          application/json:
            schema:
              $ref: "#/components/schemas/SMSSendRequest"
      responses:
        "200":
          description: SMS queued successfully
        "422":
          description: Policy check failed (DND, consent, DLT, rate limit)
        "429":
          description: Rate limit exceeded

  /internal/otp/send:
    post:
      summary: Send OTP via SMS fast lane
      responses:
        "200":
          description: OTP sent, session created
        "429":
          description: OTP resend ceiling reached

  /health:
    get:
      summary: Service health check
      responses:
        "200":
          description: Service healthy with carrier status
```

---

## SECTION XIII — ASYNCAPI EVENT SCHEMA (SGA EMITTED EVENTS)

```yaml
asyncapi: 2.6.0
info:
  title: SGA Emitted Events
  version: 1.0.0

channels:

  sga.message_delivered:
    publish:
      message:
        payload:
          type: object
          properties:
            log_id:           {type: string}
            user_id:          {type: string}
            msg_type_id:      {type: string}
            connector_id:     {type: string}
            delivered_at:     {type: string, format: date-time}

  sga.message_failed:
    publish:
      message:
        payload:
          type: object
          properties:
            log_id:           {type: string}
            user_id:          {type: string}
            msg_type_id:      {type: string}
            attempt_count:    {type: integer}
            failure_reason:   {type: string}
            carrier:          {type: string}

  sga.message_dead_lettered:
    publish:
      message:
        payload:
          type: object
          properties:
            dead_letter_id:   {type: string}
            user_id:          {type: string}
            msg_type_id:      {type: string}
            final_failure_at: {type: string, format: date-time}
            escalate_to_email:{type: boolean}

  sga.otp_delivery_failed:
    publish:
      message:
        payload:
          type: object
          properties:
            session_id:       {type: string}
            user_id:          {type: string}
            purpose:          {type: string}
            failed_at:        {type: string, format: date-time}

  sga.critical_delivery_failure:
    publish:
      message:
        payload:
          type: object
          properties:
            log_id:           {type: string}
            msg_type_id:      {type: string}
            user_id:          {type: string}
            severity:         {type: string, enum: [P0, P1]}
            failed_at:        {type: string, format: date-time}

  sga.carrier_failover_triggered:
    publish:
      message:
        payload:
          type: object
          properties:
            from_connector:   {type: string}
            to_connector:     {type: string}
            reason:           {type: string}
            triggered_at:     {type: string, format: date-time}

  sga.dlt_violation_detected:
    publish:
      message:
        payload:
          type: object
          properties:
            template_id:      {type: string}
            msg_type_id:      {type: string}
            violation_type:   {type: string}
            detected_at:      {type: string, format: date-time}
```

---

## SECTION XIV — SECURITY & PRIVACY CONTROLS (LOCKED)

### XIV.A — Phone Number Privacy Architecture

```
Phone numbers are NEVER stored in plain text anywhere in the SGA.

Storage flow:
  1. Phone received from Auth Service via HTTPS (internal mTLS)
  2. Phone validated (E.164 format)
  3. Phone passed directly to Jasmin for dispatch
  4. Phone stored in dispatch_log as: SHA-256(E164 + tenant_salt)
  5. tenant_salt rotated on GDPR erasure request
  6. Redis OTP lock key: SHA-256(E164 + static_seed) — no plain number

Recovery:
  Admin CANNOT recover phone from hash.
  Only Auth Service holds the plain phone in encrypted user record.
  SGA has no access to Auth Service phone store.
```

### XIV.B — OTP Security Architecture

```
OTP Generation:
  → secrets.randbelow(1000000) — cryptographically secure
  → Zero-padded to 6 digits
  → Generated in Auth Service — never passed to SGA in full

OTP Transport to SGA:
  → Auth Service sends: SHA-256(otp + user_salt) — not the OTP itself
  → SGA stores this hash in Redis for verification
  → SGA requests Jasmin to send the OTP value via SMPP TLS

OTP Verification:
  → User submits OTP to Auth Service
  → Auth Service computes: SHA-256(submitted + user_salt)
  → Compares with Redis hash
  → SGA is NOT involved in verification
  → SGA only delivers — it never sees or validates OTP values

OTP Log sanitization:
  → No OTP value in: application logs · Jasmin logs · Redis values ·
                      PostgreSQL records · Grafana dashboards ·
                      OpenTelemetry traces · Loki log streams
  → Jasmin message body field: stored as SHA-256(body) only
```

### XIV.C — SMPP Transport Security

```
SMPP over TLS:
  → All carrier connectors MUST use SMPP+TLS (port 3550) for production
  → Plain SMPP (port 2775) only permitted for local dev environment
  → TLS certificates: managed by cert-manager + Vault PKI
  → Certificate rotation: automated, 90-day cycle

mTLS for internal Jasmin communication:
  → SGA → Jasmin HTTP API: mTLS enforced
  → Jasmin → SGA DLR callback: HMAC-signed payload
  → Shared HMAC secret: stored in Vault, rotated monthly

Jasmin Admin Console:
  → jCli accessible only within ops namespace
  → No external exposure
  → IP allowlist enforced in Kubernetes NetworkPolicy
```

### XIV.D — Audit Trail Integrity

```
sms_dispatch_log:
  → INSERT only — no UPDATE, no DELETE at DB level
  → Row-level security enforced (tenant isolation)
  → Each record signed: HMAC-SHA256(record_json, platform_audit_key)
  → Audit key stored in Vault, rotated quarterly
  → Retention: 7 years minimum (R60 — Long-Term Archival Law)

Nightly integrity verification:
  → Batch job recomputes HMAC for all records in rolling 7-day window
  → Mismatch → emit: audit.sga_integrity_violation → P0 alert
```

---

## SECTION XV — OBSERVABILITY & MONITORING (NON-OPTIONAL)

### XV.A — Prometheus Metrics (Mandatory)

```
sga_messages_total{msg_category, status, connector_id, country_code}
sga_otp_sent_total{purpose}
sga_otp_verified_total{result}          # result: success | expired | invalid
sga_otp_resend_rate{user_bucket}        # Bucketed to protect privacy
sga_delivery_latency_seconds{pipeline}  # otp | transactional
sga_carrier_health{connector_id}        # 1=HEALTHY, 0=DOWN
sga_carrier_tps_current{connector_id}
sga_dead_letters_total{msg_category, connector_id}
sga_retry_attempts_total{connector_id, attempt_number}
sga_dnd_blocks_total
sga_dlt_violations_total{violation_type}
sga_rate_limit_hits_total{msg_category}
sga_queue_depth{connector_id}           # Jasmin queue depth per connector
sga_failover_events_total{from_connector, to_connector}
sga_undeliverable_numbers_total
```

### XV.B — Grafana Dashboard Requirements

```
Dashboard 1: SGA Operational Overview
  → Total messages sent by type (24h, 7d, 30d)
  → Delivery success rate by carrier (target: ≥ 98.5%)
  → Dead letter rate (alert threshold: > 0.5%)
  → OTP delivery rate (target: 100% within 10 seconds)
  → P95 delivery latency by pipeline

Dashboard 2: Carrier Health Monitor
  → Live carrier health status (HEALTHY / DEGRADED / DOWN)
  → TPS utilization per connector (% of max_tps)
  → Failover events timeline
  → Queue depth per connector

Dashboard 3: OTP Security Panel
  → OTP send volume by purpose (bucketed, no user PII)
  → OTP resend rate per time window
  → OTP verification success vs failure rate
  → OTP rate ceiling breach events

Dashboard 4: Compliance & Regulation Monitor
  → DLT violations detected (India)
  → DND blocks count
  → Undeliverable number flags
  → GDPR consent coverage %
  → Dead letter escalation to email rate

Dashboard 5: Dead Letter Analysis
  → Dead letters by carrier + message type
  → Admin review backlog
  → Escalation to alternate channel rate
```

### XV.C — Alerting Rules (PagerDuty / Grafana Alert Manager)

```
Alert 1:  OTP delivery failure rate > 1%          → P0 (immediate)
Alert 2:  SGA-GD-003 / SGA-GD-004 failure         → P0 (immediate)
Alert 3:  Primary carrier DOWN                     → P0 (immediate)
Alert 4:  All connectors for a country DOWN        → P0 (immediate)
Alert 5:  DLT violation detected                   → P0 (immediate)
Alert 6:  Dead letter queue > 50 messages          → P1 (15 min)
Alert 7:  Delivery success rate < 95% (1h window)  → P1 (15 min)
Alert 8:  Payment failure SMS undelivered          → P1 (15 min)
Alert 9:  Carrier TPS at 90% capacity             → P2 (1 hour)
Alert 10: Audit log integrity mismatch             → P0 (immediate)
Alert 11: OTP rate ceiling breach > 10 users/min  → P1 (15 min)
Alert 12: Jasmin queue depth > 1000               → P2 (1 hour)
```

---

## SECTION XVI — ADMIN & OPS CONSOLE INTEGRATION (R40 BOUND)

### XVI.A — SGA Panel in Internal Ops Console

```
Module: SMS Gateway Management
Access: Super Admin only (MFA enforced)
Location: ops.<LOCK_DOMAIN> → SMS Gateway panel

Required screens and functions:

1. Dispatch Log Explorer
   → Filter by: user_id · msg_type · carrier · status · date range
   → View: log_id · phone_hash · status · attempt_count · timestamps
   → Action: manual retry trigger

2. Dead Letter Review Queue
   → List all dead-lettered messages pending admin review
   → Mark reviewed · force retry · escalate to email · dismiss
   → Bulk actions supported

3. Carrier Connector Manager
   → Live health status per connector
   → TPS utilization graphs
   → Enable / disable connector toggle (audit-logged)
   → Force failover trigger

4. DLT Template Registry
   → List all registered templates
   → Status: active / expired / pending approval
   → Sync trigger with DLT portal (manual admin action)
   → Alert if template approaching expiry

5. OTP Security Monitor
   → Rate ceiling breach events list
   → Bulk OTP session invalidation (emergency action)
   → Undeliverable number flagging queue

6. Compliance Report Generator
   → Export DLT compliance report (monthly, PDF)
   → GDPR phone consent audit export
   → DND block log export

7. Emergency Controls
   → Global SMS pause switch (all outbound halted)
   → Per-connector kill switch
   → OTP pipeline emergency disable
   → All actions: require 2-admin confirmation + audit log
```

---

## SECTION XVII — INTERN EXECUTION MAPPING (R26 + R46 COMPLIANT)

### XVII.A — Jasmin Gateway Setup (Step-by-Step)

```
Project: /infra/jasmin/
Step 1: cd /infra/jasmin/
Step 2: Copy docker-compose.jasmin.yml to active config
Step 3: Edit /infra/jasmin/.env:
         JASMIN_ADMIN_PASSWORD=<from Vault>
         REDIS_HOST=redis.core.svc.cluster.local
         REDIS_PORT=6379
Step 4: docker-compose up -d jasmin
Step 5: Verify Jasmin HTTP API running:
         curl http://localhost:1401/                  # Jasmin REST API
         curl http://localhost:8990/                  # Jasmin web panel
Expected: HTTP 200 responses

Step 6: Connect carrier via jCli:
         docker exec -it jasmin /usr/bin/jcli
         jcli> smppccm -a
         > cid: vodafone_india_primary
         > host: <carrier_smsc_ip>
         > port: 3550
         > username: <from Vault: secret/sga/vodafone_primary/creds>
         > password: <from Vault>
         > save
         > start

Expected output: "Successfully started connector vodafone_india_primary"

Step 7: Verify connector health:
         jcli> smppccm -1
Expected: Status = BOUND_TRX (transmit+receive active)
```

### XVII.B — SGA Service Setup

```
Project location: /backend/services/sms_gateway_agent/

Step 1: cd /backend/services/sms_gateway_agent/
Step 2: python -m venv venv && source venv/bin/activate
Step 3: pip install -r requirements.txt
Step 4: cp .env.example .env
         Fill:
           DATABASE_URL=postgresql://...
           REDIS_URL=redis://...
           JASMIN_HTTP_URL=http://localhost:1401
           JASMIN_USERNAME=jcliadmin
           JASMIN_PASSWORD=<from Vault>
           VAULT_ADDR=http://vault.ops.svc.cluster.local:8200
           VAULT_TOKEN=<service account token>
           OTP_HMAC_KEY=<from Vault>
           AUDIT_HMAC_KEY=<from Vault>

Step 5: alembic upgrade head
Step 6: python tools/seed_templates.py
         Expected: "Seeded 24 SMS templates"
Step 7: uvicorn main:app --reload --port 8020
         Expected: "SGA SMS Gateway Agent running on port 8020"
         
Verify:
  curl http://localhost:8020/health
  Expected:
  {
    "status": "healthy",
    "jasmin_connected": true,
    "carriers": {
      "primary_india_transact": "HEALTHY",
      "primary_india_otp": "HEALTHY"
    },
    "redis_connected": true,
    "db_connected": true
  }
```

### XVII.C — Test OTP Send

```
Step 1: POST http://localhost:8020/internal/otp/send
Body:
{
  "user_id":   "test-uuid",
  "phone":     "+919999999999",
  "purpose":   "registration",
  "tenant_id": "test-tenant-uuid"
}
Expected:
{
  "session_id":          "uuid",
  "expires_at":          "2026-...",
  "attempts_remaining":  3
}

Step 2: Check Redis for OTP lock:
  redis-cli get "sga:otp:lock:test-uuid:registration"
Expected: SHA-256 hash value (NOT the OTP itself)

Step 3: Check dispatch log:
  SELECT delivery_status, jasmin_message_id
  FROM sms_dispatch_log WHERE user_id = 'test-uuid';
Expected: status = 'SUBMITTED' or 'DELIVERED'
```

---

## SECTION XVIII — PRODUCTION READINESS ENFORCEMENT GATES

### XVIII.A — Pre-Deployment Checklist

```
Gate                                                  Required
──────────────────────────────────────────────────────────────
All Section IV message types registered               ✔ REQUIRED
All message type templates seeded and validated       ✔ REQUIRED
DLT registrations complete (India launch)             ✔ REQUIRED
DLT template IDs populated in sms_dlt_templates       ✔ REQUIRED
Minimum 2 carrier connectors active (primary + failover)  ✔ REQUIRED
OTP pipeline fast-lane independently tested           ✔ REQUIRED
OTP hash-only storage verified (no plain OTP in logs) ✔ REQUIRED
Phone hash-only storage verified (no E.164 in DB)     ✔ REQUIRED
SMPP+TLS enforced on all production connectors        ✔ REQUIRED
DND registry cache populated and auto-refresh active  ✔ REQUIRED
Rate limit ceilings configured in Redis + PostgreSQL  ✔ REQUIRED
Dead letter queue table created + RLS enforced        ✔ REQUIRED
Audit log append-only policy enforced at DB           ✔ REQUIRED
HMAC signing on audit records operational             ✔ REQUIRED
Jasmin admin console IP-allowlisted                   ✔ REQUIRED
Prometheus metrics endpoint reachable                 ✔ REQUIRED
All 5 Grafana dashboards imported                     ✔ REQUIRED
All 12 alert rules activated                          ✔ REQUIRED
Emergency kill-switch tested in staging               ✔ REQUIRED
Ops Console SMS panel accessible to super admin       ✔ REQUIRED
Nightly audit integrity job scheduled                 ✔ REQUIRED
```

### XVIII.B — CI/CD Pipeline Gate

```
Stage 1:  Contract validator confirms all msg_type_ids registered (R49)
Stage 2:  Template coverage = 100% (all msg types have templates)
Stage 3:  DLT template IDs present for all India-route templates
Stage 4:  Unit tests: OTP hash-only · rate limits · policy checks
Stage 5:  Integration: Jasmin mock connector · full dispatch flow
Stage 6:  Dead letter flow E2E tested with mock carrier failure
Stage 7:  SMPP TLS connectivity confirmed to staging carrier
Stage 8:  Audit log append-only enforcement verified

Any stage failure → PIPELINE STOPS → NO DEPLOYMENT
```

### XVIII.C — Final Enforcement Rule

```
If ANY of the following is absent at deployment time:

  - Jasmin SMS Gateway running and carrier-connected
  - OTP fast-lane pipeline operational
  - DLT templates registered for India routes
  - Phone number hash-only storage enforced
  - OTP value never stored anywhere in system
  - SMPP+TLS active on all production connectors
  - Rate limit engine active in Redis
  - Dead letter queue wired to Ops Console
  - Immutable audit log enforced
  - Carrier health probes running
  - DND registry cache populated
  - Prometheus metrics live
  - All alerting rules active

→ STOP EXECUTION
→ REPORT SGA_INCOMPLETE
→ NO DEPLOYMENT CLAIM PERMITTED
→ NO PRODUCTION-READY STATUS GRANTED
```

---

## SECTION XIX — VERSION CONTROL & MUTATION POLICY

```
Current Version:    1.0.0
Status:             FINAL · LOCKED · SEALED
Mutation Policy:    Add-only via version bump
Parent:             ANTIGRAVITY-NAA-001 (NOTIFICATION_AUTOMATION_AGENT)
Child of:           ECOSKILLER MASTER EXECUTION PROMPT v12.0

Version history:
  1.0.0 — Initial sealed specification

Next version (patch) 1.0.1 — Carrier connector additions only
Next version (minor) 1.1.0 — New message type or country compliance rule
Next version (major) 2.0.0 — Pipeline architecture change
                              (requires human declaration + full audit)

No Section of this document may be deleted.
No message type ID may be removed from Section IV.
No enforcement rule may be weakened.
No security rule may be relaxed.
The OTP hash-only rule is absolute and may never be modified.
```

---

## SECTION XX — SYSTEM DEFENSE STATEMENT

```
"The SMS_GATEWAY_AGENT converts the entire Antigravity/Ecoskiller
 SMS delivery responsibility into a deterministic, compliance-governed,
 carrier-routed protocol. No message is sent without a registered type.
 No OTP value is stored anywhere in any system at any time.
 No phone number is persisted in plain text.
 No promotional message can be dispatched.
 No DLT-unapproved template reaches an Indian carrier.
 No carrier failure causes message loss — only guaranteed dead-lettering
 with mandatory admin review and channel escalation.
 The system is a machine:
   Input = dispatch request from NAA.
   Output = carrier-confirmed delivery with immutable audit proof."
```

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║          SMS_GATEWAY_AGENT — SPECIFICATION SEAL                              ║
║                                                                              ║
║  Agent ID:         ANTIGRAVITY-SGA-001                                       ║
║  Version:          1.0.0                                                     ║
║  Status:           FINAL · LOCKED · SEALED · GOVERNED                       ║
║  Parent Agent:     ANTIGRAVITY-NAA-001                                       ║
║  Gateway Stack:    Jasmin SMS Gateway (self-hosted, open-source)             ║
║  Message Types:    38 registered (OTP × 8, GD × 7, JOB × 4,                ║
║                    DOJO × 3, BILLING × 5, GOV × 4, INN × 3 +               ║
║                    EDU/SOC supplements via NAA)                              ║
║  Pipelines:        2 (OTP Fast Lane + Transactional)                        ║
║  Retry:            2 attempts (OTP) · 5 attempts exponential (Transact)     ║
║  Carrier Slots:    5 minimum (primary + failover per region)                 ║
║  Compliance:       TRAI DLT (IN) · GDPR (EU) · TCPA (US) · PDPA (SEA)     ║
║  Security:         OTP hash-only · Phone hash-only · SMPP+TLS ·             ║
║                    mTLS internal · HMAC-signed audit · AES-256 at rest      ║
║  Observability:    15 Prometheus metrics · 5 Grafana dashboards ·           ║
║                    12 alert rules · Loki + OpenTelemetry bound              ║
║                                                                              ║
║  THIS SPECIFICATION IS COMPLETE.                                             ║
║  NO HUMAN INTERPRETATION IS PERMITTED.                                       ║
║  NO SECTION MAY BE REMOVED OR WEAKENED.                                      ║
║  THE OTP HASH-ONLY RULE IS ABSOLUTE AND IMMUTABLE.                          ║
║  ONLY ADD-ONLY MUTATIONS VIA VERSION BUMP ARE AUTHORIZED.                    ║
╚══════════════════════════════════════════════════════════════════════════════╝
```
