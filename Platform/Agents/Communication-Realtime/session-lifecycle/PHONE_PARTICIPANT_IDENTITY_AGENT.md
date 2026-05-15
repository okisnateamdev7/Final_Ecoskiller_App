# 🔒 PHONE_PARTICIPANT_IDENTITY_AGENT
## ECOSKILLER ANTIGRAVITY — INTELLIGENCE & INNOVATION CORE
### Enterprise Agent Specification · v1.0.0 · SEALED · LOCKED · DETERMINISTIC

---

| FIELD | VALUE |
|---|---|
| **Agent ID** | PPIA-001 |
| **Platform** | Ecoskiller Antigravity |
| **Version** | v1.0.0 |
| **Status** | SEALED · LOCKED · PRODUCTION-READY |
| **Mutation Policy** | Add-Only · Versioned · Backward Compatible |
| **Interpretation Authority** | NONE PERMITTED |
| **Security Model** | Zero-Trust · Multi-Tenant Strict Isolation · No PII in Transit |
| **Data Policy** | Append-Only Audit Trail · Immutable · GDPR/DPDPA Compliant |
| **Scale Target** | 10M – 100M Users |
| **ML Distribution** | 70–80% Traditional ML · 20–30% LLM/Semantic |
| **Failure Policy** | HALT on Ambiguity · No Silent Failures · Deny on Uncertainty |
| **Classification** | INTERNAL RESTRICTED · PII-SENSITIVE |

---

## TABLE OF CONTENTS

| § | SECTION |
|---|---|
| 1 | Agent Identity |
| 2 | Purpose Declaration |
| 3 | System Context & Architectural Position |
| 4 | Phone Identity Verification Protocol |
| 5 | Participant Binding Architecture |
| 6 | Input Contract |
| 7 | Output Contract |
| 8 | ML / AI Logic Layer |
| 9 | OTP Lifecycle State Machine |
| 10 | Session & Device Binding |
| 11 | Multi-Channel Identity Resolution |
| 12 | GD Participant Identity Integration |
| 13 | Dojo & Championship Identity Integration |
| 14 | Cross-Agent Dependency Map |
| 15 | Passive Intelligence Compatibility |
| 16 | Innovation Economy Compatibility |
| 17 | Growth Engine Hook |
| 18 | Scalability Design |
| 19 | Security Enforcement |
| 20 | Audit & Traceability |
| 21 | Failure Policy |
| 22 | Performance Monitoring |
| 23 | Versioning Policy |
| 24 | Non-Negotiable Rules |

---

## §1 · AGENT IDENTITY

| PARAMETER | VALUE |
|---|---|
| **AGENT_NAME** | PHONE_PARTICIPANT_IDENTITY_AGENT |
| **AGENT_ID** | PPIA-001 |
| **SYSTEM_ROLE** | Phone Number Ownership Verifier · Participant Identity Binder · Session Trust Anchor · OTP Lifecycle Controller |
| **PRIMARY_DOMAIN** | Identity & Access Governance · Participant Verification · Trust Infrastructure |
| **EXECUTION_MODE** | Deterministic + Validated + Event-Driven + Real-Time |
| **DATA_SCOPE** | Phone Numbers (hashed), OTP Tokens, Device Fingerprints, Session Bindings, Participant Identity Records, Verification Audit Trails |
| **TENANT_SCOPE** | Strict Per-Tenant Isolation · No Cross-Tenant Phone Number Queries Permitted |
| **FAILURE_POLICY** | HALT on Ambiguity · DENY on Verification Failure · LOG_INCIDENT · ESCALATE |
| **CLASSIFICATION** | INTERNAL RESTRICTED · PII-SENSITIVE |
| **MUTATION_POLICY** | Add-Only · Versioned · Backward Compatible · Human-Authorized Only |
| **PII_POLICY** | Phone numbers stored as HMAC-SHA256 hashes in primary DB · Plaintext only in encrypted transit channel · Zero PII in logs, events, or feature vectors |

> 🔒 **SEALED RULE:** This agent must never store raw phone numbers in any log, event stream, audit record, or analytics pipeline. All phone number references outside the encrypted identity store are HMAC-SHA256 hashes keyed to the tenant's Vault-managed secret. This rule is absolute and cannot be overridden at runtime.

### 1.1 Agent Classification Within Antigravity

The PHONE_PARTICIPANT_IDENTITY_AGENT (PPIA) is a **Tier-1 Trust Infrastructure Agent** within the Ecoskiller Antigravity platform. It sits at the intersection of the **Trust, Identity & Safeguards layer (Category 24)** and the **Core Platform & Auth layer (Category 1)**, operating as the authoritative verifier for phone-number-based identity claims and the binding authority that links a verified phone identity to a participant session across all platform domains.

PPIA governs phone-based identity for participants in:

- Voice Group Discussion (GD) sessions — recruiter-less, deterministic participation verification
- Dojo live match rooms — participant role binding before LiveKit token issuance
- Championship and tournament entry gates — preventing duplicate or proxy identity entry
- Campus placement portals — student + parent phone verification chains
- Society Skill Hub offline → online identity bridge (Society Franchise Model)
- Royalty and innovation workflows — innovator identity anchoring for legal contracts
- Parent consent and child protection workflows — guardian phone ownership verification
- Billing and subscription events — phone as secondary identity factor for high-value transactions

---

## §2 · PURPOSE DECLARATION

### 2.1 Problem Statement

In a platform serving 10M–100M users across job portals, skill assessments, live competition rooms, innovation marketplaces, and campus placement systems, phone number identity represents the highest-trust, lowest-friction proof of human presence. However, phone-based identity creates specific risks at scale:

- **OTP fraud** — SIM swap attacks, OTP interception, and brute-force OTP enumeration
- **Proxy participation** — One person verifying identity for another in GD sessions or championship matches
- **Multi-account abuse** — Same phone number used to create duplicate accounts across tenants
- **Stale session exploitation** — Verified identity tokens reused beyond their validity window
- **Cross-domain identity leakage** — Phone numbers correlated across tenants, violating multi-tenant isolation
- **Unverified participant scoring** — Behavioral scores assigned to sessions where identity was never cryptographically bound
- **Child protection failures** — Minor accounts lacking verified guardian phone linkage
- **Rural / low-bandwidth participants** — Society Skill Hub and offline-first participants joining via SMS-only paths without app access

Without a governed, deterministic identity agent, phone verification becomes a patchwork of ad hoc OTP calls across services — creating audit gaps, security holes, and compliance failures across the entire trust chain.

### 2.2 What This Agent Solves

PPIA provides a **sealed, deterministic, privacy-preserving phone identity verification and participant binding service** that governs the complete lifecycle of a phone number claim — from OTP dispatch through verified session binding, continuous session trust scoring, and eventual identity record archival.

### 2.3 Input / Output Summary

| DIMENSION | DEFINITION |
|---|---|
| **Input Consumed** | Phone number verification requests, OTP submission events, device fingerprint payloads, session bind requests, participant join tokens, guardian consent requests, DigiLocker identity correlation signals |
| **Output Produced** | Verified identity tokens (VIT), participant binding records, session trust scores, OTP lifecycle events, identity audit packages, downstream participation grants or denials |
| **Downstream Dependents** | AUTH_SERVICE, VOICE_GD_ORCHESTRATOR, DOJO_MATCH_ENGINE, CHAMPIONSHIP_QUALIFICATION_FILTER, CONSENT_AND_PARENT_PERMISSION_AGENT, IDENTITY_ASSURANCE_AGENT, BILLING_SERVICE, ROYALTY_ACCOUNTING_ENGINE, NOTIFICATION_SERVICE |
| **Upstream Feeders** | User registration events, Jasmin SMS Gateway, Keycloak session events, device fingerprint collector, DigiLocker integration (DIGILOCKER_ANTIGRAVITY_SEALED), FRAUD_DETECTION_ENGINE, FEATURE_STORE_SERVICE |

---

## §3 · SYSTEM CONTEXT & ARCHITECTURAL POSITION

### 3.1 Platform Architecture Compliance

| CONSTRAINT | PPIA COMPLIANCE |
|---|---|
| Event-Driven Microservices | All identity verification outcomes emitted as structured Kafka events |
| Zero-Trust Security | Every verification request is treated as untrusted until all gates pass |
| Append-Only Audit Trail | All OTP lifecycle and binding events written to immutable WORM archive |
| Multi-Tenant Strict Isolation | Phone hashes scoped per-tenant; no cross-tenant phone resolution |
| Add-Only Versioning | No in-place mutations; all identity state changes create new versioned records |
| Stateless Execution | All state in Redis (OTP lifecycle) + PostgreSQL (identity records); horizontally scalable |
| Deterministic Output | Identical input → identical verification decision output |
| No Silent Failures | Every failure triggers STOP + LOG + ESCALATE protocol |
| PII Minimization | Phone numbers never in logs, events, or analytics — hash-only references |

### 3.2 Position in the Identity Stack

```
KEYCLOAK (AuthN layer)
    ↑ issues JWT after PPIA verification_token confirmed
PPIA — PHONE_PARTICIPANT_IDENTITY_AGENT
    ↑ receives phone claim from registration / join flow
USER / PARTICIPANT (web, Flutter app, SMS-only path)
    ↓ phone number hash + device fingerprint
JASMIN SMS GATEWAY (OTP delivery)
    ↓ OTP dispatched via SMS
REDIS (OTP state machine — TTL-governed)
POSTGRESQL (verified identity records — append-only)
IMMUTABLE_ARCHIVE_SERVICE (audit + legal hold)
```

### 3.3 Traffic Class Assignment

Per the Antigravity architecture, PPIA operates across two traffic classes:

- **HTTP APIs** — OTP request, OTP verify, session bind, identity status query
- **Async Events** — Identity verification outcomes, session expiry events, fraud signals (via Kafka)

PPIA does **not** operate in the WebSocket (realtime) or media traffic classes. It is a **pre-session trust anchor** — identity must be verified before any realtime session token is issued.

---

## §4 · PHONE IDENTITY VERIFICATION PROTOCOL

### 4.1 Verification Modes

PPIA supports three distinct verification modes, each with different trust levels and use cases:

| MODE | CODE | DESCRIPTION | TRUST LEVEL | USE CASE |
|---|---|---|---|---|
| **Standard OTP** | VM-1 | 6-digit OTP via SMS; 5-minute TTL | Medium | Registration, login, GD join |
| **Enhanced OTP** | VM-2 | 6-digit OTP + device fingerprint match required | High | Championship entry, Dojo match, billing events |
| **Guardian Chain** | VM-3 | Parent/guardian phone verified first; child account linked to guardian VIT | High | Minor accounts, child protection, parent consent |
| **Silent Re-verify** | VM-4 | Re-verification using existing VIT + fresh device fingerprint — no new OTP | Medium | Session resume within 24h on same device |
| **SMS-Only Fallback** | VM-5 | For low-bandwidth / offline-first participants (Society Skill Hub); OTP via SMS, no app required | Low-Medium | Rural participants, offline event bridge |

### 4.2 OTP Specification

| PARAMETER | VALUE |
|---|---|
| **OTP Length** | 6 digits (numeric only) |
| **OTP Generation** | Cryptographically secure random (CSPRNG) — not sequential, not predictable |
| **OTP TTL** | 300 seconds (5 minutes) — hard enforced in Redis with TTL key |
| **OTP Attempts** | Maximum 3 attempts per OTP instance — 4th attempt triggers lockout |
| **Lockout Duration** | 30 minutes per phone number per tenant after 3 failed instances within 1 hour |
| **Resend Cooldown** | 60 seconds between resend requests |
| **Max Resends** | 3 resends per verification session |
| **OTP Transport** | Jasmin SMS Gateway (primary) · Postfix email OTP (secondary, for web-only accounts) |
| **OTP Storage** | Redis only — encrypted at rest — never written to PostgreSQL or any log |
| **OTP Invalidation** | On successful verification, on TTL expiry, on lockout, on explicit cancel |

### 4.3 Verified Identity Token (VIT) Specification

Upon successful OTP verification, PPIA issues a **Verified Identity Token (VIT)** — a signed, short-lived JWT that proves phone ownership at a point in time.

```json
VIT_STRUCTURE {
  token_id:              UUID (v4, immutable)
  phone_hash:            HMAC-SHA256(phone_number, tenant_vault_key)
  tenant_id:             UUID
  verification_mode:     enum [VM-1 | VM-2 | VM-3 | VM-4 | VM-5]
  device_fingerprint_id: UUID?   // Required for VM-2; optional for others
  issued_at_utc:         ISO-8601
  expires_at_utc:        ISO-8601 // VM-1: 24h | VM-2: 4h | VM-3: 7d | VM-4: 24h | VM-5: 2h
  scope:                 enum [REGISTRATION | SESSION | GD_JOIN | DOJO_JOIN | CHAMPIONSHIP | BILLING | GUARDIAN_CONSENT]
  signature:             HMAC-SHA256(payload, PPIA_signing_key_from_vault)
  audit_reference:       UUID   // Immutable audit log entry
}
```

> 🔒 **SEALED RULE:** VIT tokens are single-use per scope claim. A VIT issued for `GD_JOIN` cannot be reused for `CHAMPIONSHIP` entry. Scope is bound at issuance and cannot be elevated at runtime.

---

## §5 · PARTICIPANT BINDING ARCHITECTURE

### 5.1 Identity Binding Chain

Participant binding is the process of linking a verified phone identity to a specific platform session (GD room, Dojo match, championship slot, billing event). The binding chain is:

```
PHONE_NUMBER_CLAIM
    ↓ OTP verification (PPIA)
VERIFIED_IDENTITY_TOKEN (VIT)
    ↓ presented to session gate
PARTICIPANT_BINDING_RECORD (PBR)
    ↓ emitted to session orchestrator
SESSION_TOKEN (Jitsi join token / LiveKit token / championship slot)
    ↓ participant enters session
BEHAVIORAL_DATA_COLLECTION (scoring, GD metrics, match stats)
    ↓ all behavioral data linked to PBR.binding_id
AUDIT_CLOSURE (session end → binding archived)
```

### 5.2 Participant Binding Record (PBR)

```json
PARTICIPANT_BINDING_RECORD {
  binding_id:              UUID (v4, immutable)
  vit_id:                  UUID     // Reference to VIT used to create this binding
  phone_hash:              HMAC-SHA256(phone, tenant_key)
  user_id:                 UUID     // Platform user account ID
  tenant_id:               UUID
  session_type:            enum [GD_SESSION | DOJO_MATCH | CHAMPIONSHIP | BILLING | CONSENT | REGISTRATION]
  session_id:              UUID     // Room ID / match_id / transaction_id
  bound_at_utc:            ISO-8601
  binding_valid_until_utc: ISO-8601
  device_fingerprint_id:   UUID?
  ip_address_hash:         HMAC-SHA256(ip, tenant_key)  // Never raw IP in record
  binding_status:          enum [ACTIVE | EXPIRED | REVOKED | SUSPICIOUS]
  trust_score:             float [0.0–1.0]  // ML-computed at bind time
  anomaly_flags:           string[]
  audit_reference:         UUID
}
```

### 5.3 Binding Uniqueness Rules

```
BINDING_UNIQUENESS_ENFORCEMENT {

  GD_SESSION:
    → One phone_hash may bind to ONE GD session at a time per tenant
    → If phone_hash already bound to active GD session → REJECT new bind
    → Re-entry after network drop: resume existing binding (do not create new)

  DOJO_MATCH:
    → One phone_hash may bind to ONE active Dojo match room at a time
    → Simultaneous match entry from two devices → SUSPICIOUS flag → human review

  CHAMPIONSHIP:
    → phone_hash must be unique per championship_id
    → Duplicate phone_hash in same championship → BLOCK + LOG_INCIDENT

  BILLING:
    → phone_hash must match account's primary verified phone
    → Mismatch → BLOCK transaction + ESCALATE to FRAUD_DETECTION_ENGINE

  REGISTRATION:
    → phone_hash must be globally unique per tenant
    → Duplicate phone_hash on registration → BLOCK + merge-account flow trigger
}
```

---

## §6 · INPUT CONTRACT (STRICT)

> 🔒 **SEALED RULE:** No null tolerance without explicit policy declaration. Reject all malformed input. Log every validation failure. Phone numbers are never logged — only their HMAC hash is referenced in any log or audit record.

### 6.1 OTP Request Schema

```json
INPUT_SCHEMA: OTPRequest {

  // REQUIRED FIELDS
  request_id:          UUID (client-generated, idempotency key)
  phone_hash:          HMAC-SHA256(phone_number, client_derived_key)
  phone_e164_encrypted: AES-256-GCM(phone_number, session_public_key)  // Encrypted plaintext for OTP dispatch only
  tenant_id:           UUID
  verification_mode:   enum [VM-1 | VM-2 | VM-3 | VM-4 | VM-5]
  scope:               enum [REGISTRATION | SESSION | GD_JOIN | DOJO_JOIN | CHAMPIONSHIP | BILLING | GUARDIAN_CONSENT]
  request_timestamp_utc: ISO-8601
  device_fingerprint:  DeviceFingerprintPayload  // Required for VM-2; recommended for all

  // OPTIONAL FIELDS
  guardian_user_id:    UUID?   // Required for VM-3 (Guardian Chain)
  session_id:          UUID?   // If scope is GD_JOIN or DOJO_JOIN
  resend_token:        UUID?   // Required for resend requests

  // VALIDATION RULES
  phone_e164_encrypted → decrypt and validate E.164 format: +[country_code][number]
  phone number length → 7–15 digits (ITU-T E.164)
  tenant_id → must exist in TENANT_REGISTRY
  scope → must be consistent with verification_mode
  rate_limit → max 5 OTP requests per phone_hash per hour per tenant
  blacklist_check → phone_hash must not appear in FRAUD_BLACKLIST
  country_code → must be in tenant's allowed country list

  // SECURITY CHECKS
  request_id deduplication → idempotent within 5-minute window
  IP rate limit → max 20 OTP requests per IP per hour
  Device fingerprint velocity → flag if > 3 phone_hashes from same device in 1 hour
  SIM swap signal check → query FRAUD_DETECTION_ENGINE for recent SIM swap alerts

  // DOMAIN CHECKS
  GD_JOIN scope → session_id required; session must be in OPEN state
  CHAMPIONSHIP scope → championship_id required; registration window must be open
  VM-3 → guardian_user_id required; guardian must have verified VM-1 or VM-2 on record
}
```

### 6.2 OTP Submission Schema

```json
INPUT_SCHEMA: OTPSubmission {
  submission_id:       UUID (client-generated)
  request_id:          UUID   // Must match the original OTPRequest.request_id
  phone_hash:          HMAC-SHA256(phone_number, client_derived_key)
  tenant_id:           UUID
  otp_value:           string [6 numeric digits]
  submission_timestamp_utc: ISO-8601
  device_fingerprint:  DeviceFingerprintPayload

  // VALIDATION RULES
  otp_value must be exactly 6 digits
  submission must arrive within OTP TTL window (300s)
  attempt_count for this request_id must be < 3
  phone_hash must match the phone_hash on the original OTPRequest
  device_fingerprint.device_id must match DeviceFingerprintPayload on OTPRequest (for VM-2)
}
```

### 6.3 Device Fingerprint Payload Schema

```json
DeviceFingerprintPayload {
  device_id:            UUID   // Client-generated stable device identifier
  platform:             enum [FLUTTER_ANDROID | FLUTTER_IOS | FLUTTER_WEB | REACT_WEB | SMS_ONLY]
  os_version_hash:      SHA-256(os_name + os_version)   // No raw OS string in transit
  screen_resolution:    string  // e.g. "1080x2340"
  timezone_offset:      integer // UTC offset in minutes
  language_code:        string  // BCP-47
  network_type:         enum [WIFI | MOBILE_4G | MOBILE_3G | MOBILE_2G | UNKNOWN]
  app_version:          string? // Flutter app version if applicable
  collected_at_utc:     ISO-8601
}
```

---

## §7 · OUTPUT CONTRACT (STRICT)

> 🔒 **SEALED RULE:** All outputs must include verification status, audit reference, and scope. No VIT issued without full traceability. Phone hash is the only phone-related field permitted in any output record.

### 7.1 OTP Dispatch Response

```json
OUTPUT_SCHEMA: OTPDispatchResponse {
  request_id:           UUID   // Echoes input
  dispatch_status:      enum [DISPATCHED | RATE_LIMITED | BLACKLISTED | INVALID_PHONE | SERVICE_ERROR]
  dispatch_timestamp_utc: ISO-8601
  expires_at_utc:       ISO-8601  // OTP expiry time
  resend_available_at_utc: ISO-8601  // When resend is permitted
  attempts_remaining:   integer [0–3]
  audit_reference:      UUID
  // NOTE: OTP value is NEVER included in any response
}
```

### 7.2 OTP Verification Response

```json
OUTPUT_SCHEMA: OTPVerificationResponse {
  submission_id:        UUID   // Echoes input
  verification_status:  enum [SUCCESS | INVALID_OTP | EXPIRED | MAX_ATTEMPTS_EXCEEDED | LOCKED | SUSPICIOUS]
  vit_token:            string?  // Signed JWT — only present if verification_status = SUCCESS
  vit_id:               UUID?
  vit_expires_at_utc:   ISO-8601?
  phone_hash:           HMAC-SHA256(phone, tenant_key)
  tenant_id:            UUID
  scope:                string
  trust_score:          float?   // Present on SUCCESS
  audit_reference:      UUID

  next_trigger_events: [
    IDENTITY_VERIFIED_EVENT,        // On SUCCESS
    PARTICIPANT_BIND_READY_EVENT,   // If scope is session-type
    FRAUD_SIGNAL_EVENT,             // If verification_status = SUSPICIOUS
    LOCKOUT_INITIATED_EVENT         // If MAX_ATTEMPTS_EXCEEDED
  ]
}
```

### 7.3 Participant Binding Response

```json
OUTPUT_SCHEMA: ParticipantBindingResponse {
  binding_id:           UUID (immutable)
  vit_id:               UUID
  user_id:              UUID
  tenant_id:            UUID
  session_type:         string
  session_id:           UUID
  session_token:        string?  // Short-lived Jitsi/LiveKit join token — only if binding approved
  binding_status:       enum [BOUND | REJECTED | SUSPICIOUS | DUPLICATE]
  trust_score:          float [0.0–1.0]
  bound_at_utc:         ISO-8601
  valid_until_utc:      ISO-8601
  audit_reference:      UUID
  confidence_score:     float [0.0–1.0]
  model_version_ref:    string  // PPIA version + ML model version
}
```

---

## §8 · ML / AI LOGIC LAYER

### 8.1 ML-Based Logic (Primary — ~75%)

#### 8.1.1 Identity Trust Scorer

| PARAMETER | SPECIFICATION |
|---|---|
| **MODEL_TYPE** | Regression — outputs trust_score 0.0–1.0 |
| **ALGORITHM** | Gradient Boosted Trees (XGBoost) — deterministic seed enforced |
| **FEATURES_USED** | `account_age_days`, `previous_verified_sessions_count`, `device_consistency_score`, `phone_hash_velocity_7d`, `otp_attempt_pattern` (clean / partial / brute), `sim_swap_signal_present`, `ip_country_match_phone_country`, `time_of_request_hour`, `network_type_enum`, `tenant_risk_tier` |
| **TRAINING_FREQUENCY** | Weekly — Airflow scheduled job |
| **DRIFT_DETECTION** | PSI on all features; threshold = 0.2 → WATCH; threshold = 0.4 → CRITICAL |
| **VERSION_CONTROL** | MODEL_REGISTRY_SERVICE — immutable reference required |
| **CONFIDENCE_THRESHOLD** | Trust score < 0.50 → SUSPICIOUS flag; trust score < 0.30 → BLOCK + escalate |
| **OUTPUT** | `trust_score: float`, `risk_flags: string[]`, `recommended_action: enum [ALLOW \| CHALLENGE \| BLOCK]` |

#### 8.1.2 SIM Swap & Proxy Detection Classifier

| PARAMETER | SPECIFICATION |
|---|---|
| **MODEL_TYPE** | Binary Classification — SIM_SWAP_RISK: YES / NO |
| **ALGORITHM** | Random Forest — 200 trees, deterministic |
| **FEATURES_USED** | `carrier_change_signal` (from FRAUD_DETECTION_ENGINE), `last_port_date_delta_days`, `account_login_location_change_km`, `new_device_after_port`, `high_value_scope_request` (championship / billing), `phone_age_on_carrier_days` |
| **TRAINING_FREQUENCY** | Monthly |
| **DRIFT_DETECTION** | Residual monitoring — alert if false negative rate increases > 5% over 30 days |
| **OUTPUT** | `sim_swap_risk: bool`, `sim_swap_confidence: float`, `carrier_anomaly_flag: bool` |

#### 8.1.3 Duplicate Account Detector

| PARAMETER | SPECIFICATION |
|---|---|
| **MODEL_TYPE** | Similarity scoring — cross-account phone hash clustering |
| **ALGORITHM** | MinHash LSH (Locality-Sensitive Hashing) on device fingerprint + behavioral vectors |
| **FEATURES_USED** | `device_fingerprint_similarity`, `registration_ip_hash_cluster`, `behavioral_pattern_overlap_score`, `timing_pattern_similarity` |
| **TRAINING_FREQUENCY** | Weekly |
| **OUTPUT** | `duplicate_probability: float`, `candidate_duplicate_user_ids: UUID[]`, `merge_recommended: bool` |

### 8.2 AI-Based Logic (Supplementary — ~25%)

> 🔒 **SEALED RULE:** AI usage is strictly bounded. AI is advisory only. No AI output may directly grant or deny a verification. All verification decisions are produced by the ML pipeline and rule engine. AI outputs are informational artifacts appended after the decision.

#### 8.2.1 Fraud Pattern Narrative Generator

| PARAMETER | SPECIFICATION |
|---|---|
| **AI_USAGE_SCOPE** | Generate human-readable fraud risk summary from structured ML risk signals — for human reviewer dashboard only |
| **PROMPT_GOVERNANCE** | Versioned prompt v1.0.0; no creative interpretation; output is deterministic summary of ML feature values |
| **INPUT** | Structured ML risk object (`trust_score`, `risk_flags`, `sim_swap_risk`, `duplicate_probability`) |
| **OUTPUT** | `fraud_narrative: string` — appended to SUSPICIOUS incident record for human review |
| **FALLBACK** | If AI timeout > 2s → use templated string builder |

#### 8.2.2 Verification Denial Explanation Composer

| PARAMETER | SPECIFICATION |
|---|---|
| **AI_USAGE_SCOPE** | Compose user-facing denial explanation message — plain language, no technical details exposed |
| **PROMPT_GOVERNANCE** | Versioned template; output validated to ensure no security-sensitive detail is disclosed |
| **FALLBACK** | Static template string if AI unavailable |

---

## §9 · OTP LIFECYCLE STATE MACHINE

PPIA enforces a deterministic **7-state lifecycle** for every OTP instance. All state is held in Redis with enforced TTLs. No state may skip. Redis key format: `otp:{tenant_id}:{phone_hash}:{request_id}`.

### 9.1 OTP State Definitions

| STATE | CODE | DESCRIPTION | TTL / EXIT |
|---|---|---|---|
| **PENDING_DISPATCH** | O1 | OTP generated, queued for SMS delivery | 30s — fail if not dispatched |
| **DISPATCHED** | O2 | OTP sent via Jasmin SMS Gateway | 300s TTL from dispatch |
| **ATTEMPT_1** | O3 | First OTP submission received — incorrect | 300s remaining TTL |
| **ATTEMPT_2** | O4 | Second OTP submission received — incorrect | 300s remaining TTL |
| **VERIFIED** | O5 | Correct OTP submitted within TTL | Terminal — VIT issued |
| **EXPIRED** | O6 | TTL elapsed without successful verification | Terminal — no VIT |
| **LOCKED** | O7 | 3 failed attempts; phone_hash locked for 30 minutes | Terminal for 30min — then resets |

### 9.2 OTP State Transitions

```
OTP_STATE_MACHINE_TRANSITIONS {

  O1 → O2:  Jasmin dispatch_ack received within 30s
  O1 → O6:  Jasmin dispatch_ack NOT received within 30s → EXPIRED + LOG_INCIDENT
  O2 → O3:  Incorrect OTP submitted; attempts_remaining = 2
  O2 → O5:  Correct OTP submitted within TTL → VERIFIED
  O2 → O6:  TTL expires before any submission → EXPIRED
  O3 → O4:  Incorrect OTP submitted; attempts_remaining = 1
  O3 → O5:  Correct OTP submitted within TTL → VERIFIED
  O3 → O6:  TTL expires → EXPIRED
  O4 → O7:  Incorrect OTP submitted; attempts_remaining = 0 → LOCKED
  O4 → O5:  Correct OTP submitted within TTL → VERIFIED
  O4 → O6:  TTL expires → EXPIRED
  O5 → *:   Terminal — no further transitions
  O6 → *:   Terminal — no further transitions
  O7 → O1:  After lockout_duration (1800s) expires → phone_hash released for new OTP request

  ILLEGAL TRANSITIONS (HALT on attempt):
  O5 → ANY  // Verified OTP cannot be re-used
  O6 → O2   // Expired OTP cannot be re-dispatched on same request_id
  O7 → O2   // Locked phone_hash cannot receive new OTP until lockout expires
}
```

> 🔒 **SEALED RULE:** OTP values are stored in Redis using AES-256 encryption with a per-tenant key from HashiCorp Vault. The OTP value is **never** written to PostgreSQL, any log file, or any event stream. On state transition to O5, O6, or O7, the encrypted OTP value is immediately deleted from Redis.

---

## §10 · SESSION & DEVICE BINDING

### 10.1 Device Fingerprint Binding Rules

| SCENARIO | BINDING RULE | ACTION ON VIOLATION |
|---|---|---|
| Same device, same phone_hash, within 24h | Silent re-verify (VM-4) permitted | None |
| New device, same phone_hash, within 1h | Require fresh OTP (VM-1 minimum) | Block session token until re-verified |
| 3+ devices for same phone_hash within 24h | Velocity flag → SUSPICIOUS | Flag for human review; high-risk scopes blocked |
| Different phone_hash from same device_id within 1h | Velocity flag → SUSPICIOUS | FRAUD_DETECTION_ENGINE alert |
| VM-2 scope: device fingerprint mismatch | Hard block — VIT invalid | BLOCK + LOG_INCIDENT |

### 10.2 Session Token Issuance Gate

PPIA is the **sole issuer authority** that authorizes downstream session token generation. No session token (Jitsi join token, LiveKit token, championship slot token) may be issued without a valid, unexpired `ParticipantBindingRecord` from PPIA.

```
SESSION_TOKEN_ISSUANCE_GATE {

  PRECONDITIONS (all must be true):
  ✓ vit_id is valid and unexpired
  ✓ binding_id exists for the requested session_id
  ✓ binding_status = ACTIVE
  ✓ trust_score >= 0.50
  ✓ No SUSPICIOUS or higher anomaly_flags unresolved
  ✓ session_id is in the correct pre-join state (OPEN for GD, PENDING for Dojo)
  ✓ phone_hash is unique for this session_id (no duplicate binding)

  ON FAILURE OF ANY PRECONDITION:
  → DENY session token issuance
  → EMIT identity_gate_blocked_event → Kafka
  → LOG_INCIDENT with full context
  → Return structured denial with denial_code (no sensitive detail exposed to client)
}
```

### 10.3 Continuous Session Trust

For GD and Dojo sessions, PPIA monitors binding health throughout the session duration:

- **Heartbeat**: Every 60 seconds, PPIA checks `binding_status` and `vit_expiry` in Redis
- **On VIT expiry during session**: Emit `vit_expiry_warning_event` at -5 min; session orchestrator decides whether to force re-verify or allow session completion
- **On SUSPICIOUS escalation during session**: Emit `session_trust_degraded_event`; session orchestrator freezes scoring for affected participant pending human review
- **On binding_status = REVOKED**: Session orchestrator immediately removes participant from active session; behavioral data from that participant is quarantined pending review

---

## §11 · MULTI-CHANNEL IDENTITY RESOLUTION

### 11.1 Resolution Priority Chain

When a user presents multiple identity signals, PPIA resolves them according to this priority chain (highest trust first):

```
IDENTITY_RESOLUTION_PRIORITY {

  Priority 1 (HIGHEST): DigiLocker-verified phone (Aadhaar-linked)
    → Trust multiplier: +0.25 on trust_score
    → Source: DIGILOCKER_ANTIGRAVITY_SEALED agent

  Priority 2: VM-2 Enhanced OTP (phone + device fingerprint)
    → Base trust_score from ML model
    → Scope: Championship, Billing, Dojo

  Priority 3: VM-1 Standard OTP (phone only)
    → Base trust_score from ML model
    → Scope: Registration, GD_JOIN, SESSION

  Priority 4: VM-4 Silent Re-verify (VIT + device match)
    → Trust_score = previous trust_score * 0.95 decay factor
    → Only valid within 24h of original VM-1/VM-2 verification

  Priority 5 (LOWEST): VM-5 SMS-Only (Society Skill Hub / offline bridge)
    → Trust_score capped at 0.65 regardless of ML output
    → Scope: limited — registration and basic session only
    → NOT permitted for Championship or Billing scopes
}
```

### 11.2 Guardian Chain Identity (Minor Accounts)

For accounts flagged as minors (under-18) or where the CONSENT_AND_PARENT_PERMISSION_AGENT has flagged a guardian requirement:

```
GUARDIAN_CHAIN_PROTOCOL {

  Step 1: Guardian phone_hash must be VM-1 or VM-2 verified first
  Step 2: Guardian VIT issued with scope = GUARDIAN_CONSENT
  Step 3: Child account linked to guardian VIT via guardian_binding_id
  Step 4: Child account sessions require guardian_binding_status = ACTIVE
  Step 5: Guardian can revoke child session access via guardian_revoke_event

  Guardian VIT expiry:
    → VM-3 VIT TTL = 7 days
    → On expiry: child account moves to RESTRICTED mode
    → CONSENT_AND_PARENT_PERMISSION_AGENT is notified
    → Child cannot join championship, billing, or GD_ASSESSMENT scopes until guardian re-verifies

  CHILD_PROTECTION_EVIDENCE_AGENT is notified of all Guardian Chain events.
}
```

---

## §12 · GD PARTICIPANT IDENTITY INTEGRATION

### 12.1 GD Join Identity Gate

The Voice GD Orchestrator (which replaces the human moderator entirely using deterministic protocol) **must** receive a valid `ParticipantBindingRecord` from PPIA before issuing a Jitsi room join token. The integration flow:

```
GD_PARTICIPANT_IDENTITY_FLOW {

  T-0:  Candidate requests GD batch assignment (Job Portal)
  T-1:  PPIA.OTPRequest (scope = GD_JOIN, session_id = gd_batch_id)
  T-2:  Jasmin dispatches OTP via SMS to candidate's registered phone
  T-3:  Candidate submits OTP (within 5 min)
  T-4:  PPIA verifies OTP → issues VIT (scope = GD_JOIN)
  T-5:  PPIA creates ParticipantBindingRecord for gd_batch_id
  T-6:  GD Orchestrator queries PPIA: GET /binding/{gd_batch_id}/{user_id}
  T-7:  PPIA returns binding_status = ACTIVE + trust_score
  T-8:  GD Orchestrator issues Jitsi join token (short-lived, signed)
  T-9:  Candidate joins Jitsi room (audio-only, muted by default)
  T-10: GD session begins — Redis state machine takes control

  JOIN WINDOW ENFORCEMENT (from GD system design):
  → Candidate who fails PPIA verification before join_window_close_utc
    becomes SPECTATOR_ONLY (cannot speak, cannot score)
  → Late joiners denied PPIA binding for that session
  → No exceptions — enforced by deterministic protocol
}
```

### 12.2 GD Scoring Identity Linkage

All GD behavioral data (mic_open_duration, turns_completed, interrupt_attempts, silence_duration) captured by the GD Orchestrator is linked to the `binding_id` from PPIA — not directly to `user_id`. This ensures:

- If a binding is later flagged as SUSPICIOUS, the associated behavioral score is quarantined
- Score audit trail has cryptographic linkage to verified phone identity
- No score can be produced for an unbound participant

---

## §13 · DOJO & CHAMPIONSHIP IDENTITY INTEGRATION

### 13.1 Dojo Match Identity Gate

For Dojo live match rooms (LiveKit SFU), PPIA enforces **VM-2 Enhanced OTP** (phone + device fingerprint) for all match-type sessions. This prevents proxy participation where one person verifies for another.

```
DOJO_PARTICIPANT_IDENTITY_RULES {

  Verification Mode Required: VM-2 (phone + device fingerprint)
  Binding Scope:              DOJO_JOIN
  Trust Score Minimum:        0.65 to receive LiveKit token
  Device Lock:                Participant must join from the same device
                              used during VM-2 verification
  Re-entry After Disconnect:  VM-4 silent re-verify (same device, within session TTL)
  Mentor Participants:        VM-2 required; separate RBAC role binding via Keycloak
  Score Quarantine Trigger:   If binding_status degrades to SUSPICIOUS during match
                              → match scoring frozen for that participant
                              → DOJO_MATCH_ENGINE notified
                              → Human review required before score published
}
```

### 13.2 Championship Identity Gate

Championship entry represents the highest-stakes identity scenario on the platform. PPIA enforces maximum identity assurance:

```
CHAMPIONSHIP_IDENTITY_RULES {

  Verification Mode Required: VM-2 (mandatory) + DigiLocker signal (recommended)
  Binding Scope:              CHAMPIONSHIP
  Trust Score Minimum:        0.75 to receive championship slot token
  Phone Hash Uniqueness:      Global per championship_id (not just per-tenant)
                              → Prevents cross-tenant duplicate entry
  Re-verification Window:     VM-2 must be completed within 2 hours of match start
  SIM Swap Check:             Mandatory — if sim_swap_risk = true → BLOCK + human review
  Duplicate Account Check:    Mandatory — if duplicate_probability > 0.80 → BLOCK + incident
  Championship_HOLD_EVENT:    If SMDA has frozen championship scoring models,
                              PPIA must check SCORING_MODEL_DEPRECATION_AGENT
                              status before issuing championship binding
}
```

---

## §14 · CROSS-AGENT DEPENDENCY MAP

### 14.1 Upstream Agents (Feeders)

| AGENT | FEEDS TO PPIA | EVENT / SIGNAL |
|---|---|---|
| USER_SERVICE | User account creation events, phone number claims | `user.registered_event` |
| KEYCLOAK | Session events, JWT issuance, MFA triggers | `keycloak.session_event` |
| JASMIN_SMS_GATEWAY | OTP delivery acknowledgements, delivery failures | `sms.dispatch_ack_event` |
| FRAUD_DETECTION_ENGINE | SIM swap alerts, blacklist updates, fraud flags | `fraud.signal_event` |
| DIGILOCKER_ANTIGRAVITY_SEALED | DigiLocker phone-Aadhaar correlation signals | `digilocker.verification_event` |
| FEATURE_STORE_SERVICE | ML feature snapshots for trust scoring | `feature.snapshot_event` |
| CONSENT_AND_PARENT_PERMISSION_AGENT | Guardian requirement flags | `consent.guardian_required_event` |
| CHILD_PROTECTION_EVIDENCE_AGENT | Minor account flags | `child_protection.flag_event` |
| SCORING_MODEL_DEPRECATION_AGENT | Championship scoring model status | `scoring.model.status_event` |

### 14.2 Downstream Agents (Dependents)

| AGENT | DEPENDENCY | NOTIFICATION TRIGGER |
|---|---|---|
| AUTH_SERVICE / KEYCLOAK | Receives VIT as pre-condition for JWT issuance | On every verified OTP (SUCCESS) |
| VOICE_GD_ORCHESTRATOR | Receives ParticipantBindingRecord before Jitsi token issuance | On GD_JOIN binding ACTIVE |
| DOJO_MATCH_ENGINE | Receives ParticipantBindingRecord before LiveKit token issuance | On DOJO_JOIN binding ACTIVE |
| CHAMPIONSHIP_QUALIFICATION_FILTER | Receives championship binding status | On CHAMPIONSHIP binding ACTIVE |
| IDENTITY_ASSURANCE_AGENT | Receives trust_score and anomaly_flags for platform-wide identity health | On every verification |
| FRAUD_DETECTION_ENGINE | Receives SUSPICIOUS and LOCKED events | On anomaly_flags elevation |
| BILLING_SERVICE | Receives BILLING scope VIT before high-value transaction approval | On BILLING scope request |
| NOTIFICATION_SERVICE | Receives OTP dispatch instructions | On every OTP request |
| ROYALTY_ACCOUNTING_ENGINE | Receives innovator identity binding for royalty contract anchoring | On REGISTRATION + innovation scope |
| IMMUTABLE_ARCHIVE_SERVICE | Receives full identity lifecycle audit packages | On account close / legal hold |
| OBSERVABILITY_AGENT | Metrics, trace IDs, anomaly frequency | Continuous |

### 14.3 Kafka Event Emission Map

| TRIGGER | KAFKA EVENT | CONSUMERS |
|---|---|---|
| OTP dispatched | `identity.otp.dispatched` | OBSERVABILITY_AGENT |
| OTP verified (SUCCESS) | `identity.phone.verified` | AUTH_SERVICE, relevant session orchestrator |
| OTP failed (EXPIRED / LOCKED) | `identity.otp.failed` | FRAUD_DETECTION_ENGINE, OBSERVABILITY_AGENT |
| Participant bound (ACTIVE) | `identity.participant.bound` | Session orchestrator (GD / Dojo / Championship) |
| Binding SUSPICIOUS | `identity.binding.suspicious` | FRAUD_DETECTION_ENGINE, HUMAN_REVIEW_QUEUE |
| Binding REVOKED | `identity.binding.revoked` | Session orchestrator, AUDIT_COMPLIANCE_AGENT |
| SIM swap detected | `identity.simswap.detected` | FRAUD_DETECTION_ENGINE, CISO_ALERT_CHANNEL |
| Duplicate account detected | `identity.duplicate.detected` | USER_SERVICE, ADMIN_GOVERNANCE_SERVICE |
| Guardian chain established | `identity.guardian.bound` | CONSENT_AND_PARENT_PERMISSION_AGENT |
| VIT expiry warning | `identity.vit.expiry_warning` | Session orchestrator |

---

## §15 · PASSIVE INTELLIGENCE COMPATIBILITY

When PPIA processes verification events that are associated with intelligence assessment sessions (Dojo intelligence tests, GD performance assessments, 8-type intelligence scoring activities), it must emit **aggregate behavioral signals** — never individual identity data — to the FEATURE_STORE_AGENT.

### 15.1 Feature Vector Emission

```json
EMIT_FEATURE_VECTOR: PhoneVerificationBehavioralSignal {
  user_id:              [NOT EMITTED — use binding_id reference only]
  feature_name:         "phone_verification_trust_signal"
  feature_value: {
    binding_id:           UUID     // Opaque reference — not linked to phone_hash in feature store
    verification_mode:    enum [VM-1 | VM-2 | VM-3 | VM-4 | VM-5]
    trust_score:          float
    otp_attempt_pattern:  enum [CLEAN | PARTIAL | BRUTE]  // No OTP value — pattern only
    device_consistency:   float
    session_type:         enum [GD_SESSION | DOJO_MATCH | CHAMPIONSHIP | REGISTRATION]
    verification_latency_ms: integer  // Time from OTP dispatch to verified submission
  }
  timestamp:            ISO-8601
  source_agent:         "PHONE_PARTICIPANT_IDENTITY_AGENT"
}
```

> 🔒 **SEALED RULE:** The `binding_id` in feature vectors is a one-way opaque reference. PPIA does **not** expose a lookup API that maps `binding_id` → `phone_hash` or `user_id` to the FEATURE_STORE_SERVICE. The mapping exists only in the encrypted PPIA identity store.

---

## §16 · INNOVATION ECONOMY COMPATIBILITY

When PPIA verifies the identity of an innovator (a user submitting ideas, licensing innovations, or receiving royalty payments), the following additional identity anchoring rules apply:

```json
INNOVATION_IDENTITY_ANCHOR {
  innovator_binding_id:    UUID   // PPIA-issued binding for this innovator
  verification_mode:       enum   // Must be VM-1 minimum; VM-2 for royalty events
  identity_anchor_hash:    HMAC-SHA256(phone_hash + user_id + tenant_id, vault_key)
  anchor_timestamp_utc:    ISO-8601
  legal_hold_flag:         boolean  // True if any active licensing contract
}
```

### 16.1 Innovation Economy Constraints

| ENGINE | PPIA CONSTRAINT | ENFORCEMENT |
|---|---|---|
| IDEA_REGISTRY_SERVICE | Idea submission requires active VM-1 minimum VIT | Before idea.submitted_event |
| ROYALTY_ACCOUNTING_ENGINE | Royalty payout requires VM-2 verified phone + active billing-scope VIT | Before royalty.payout_event |
| LICENSING_CONTRACT_SERVICE | Contract signing requires VM-2 verified phone; minor accounts require guardian VM-3 | Before contract.signed_event |
| INNOVATION_TRUST_GOVERNANCE | Parent consent records linked to guardian phone_hash via VM-3 | On guardian chain establishment |
| ROYALTY_WALLET_SERVICE | Wallet access requires continuous VIT validity | On every wallet access request |

> 🔒 **SEALED RULE:** Royalty payout operations are never executed if the innovator's `ParticipantBindingRecord.binding_status` is SUSPICIOUS or REVOKED. The ROYALTY_ACCOUNTING_ENGINE must query PPIA binding status before processing any payout. This is a hard dependency — no fallback.

---

## §17 · GROWTH ENGINE HOOK

When PPIA verification events affect user ranking, achievement, or XP eligibility, the following Growth Engine events must be triggered:

| TRIGGER CONDITION | REQUIRED EVENT | ENFORCEMENT |
|---|---|---|
| First phone verification (new account) | `EMIT: ACCOUNT_VERIFIED_XP_EVENT` | On VM-1 SUCCESS for REGISTRATION |
| VM-2 verification completed (enhanced trust) | `EMIT: TRUST_UPGRADE_XP_EVENT` | On VM-2 SUCCESS |
| Championship binding ACTIVE | `EMIT: CHAMPIONSHIP_ENTRY_CONFIRMED_EVENT` | On CHAMPIONSHIP binding ACTIVE |
| Guardian chain established for minor | `EMIT: GUARDIAN_LINKED_ACHIEVEMENT_EVENT` | On VM-3 SUCCESS |
| SIM swap detected → binding REVOKED | `EMIT: RANK_FREEZE_EVENT` for affected user | On SIM swap confirmation |
| Duplicate account confirmed | `EMIT: XP_AUDIT_FREEZE_EVENT` for both accounts | On duplicate_confirmed |

> ⚠️ **RULE:** PPIA must never directly modify XP or rank values. It only emits events. The SCORING ENGINE and XP systems act on those events. PPIA's role is identity signal emission — not score computation.

---

## §18 · SCALABILITY DESIGN

| PARAMETER | SPECIFICATION |
|---|---|
| **EXPECTED_RPS** | 500–5,000 RPS peak (OTP requests spike during GD batch windows and championship registration opens) |
| **LATENCY_TARGET** | P95 < 200ms for OTP dispatch response; P95 < 100ms for OTP verification; P99 < 500ms end-to-end |
| **MAX_CONCURRENCY** | 5,000 concurrent OTP lifecycle instances across all tenants |
| **QUEUE_STRATEGY** | Redis partitioned by `tenant_id:{phone_hash}` — ensures per-phone serialization with cross-tenant parallelism; Kafka partitioned by `tenant_id` for event emission |
| **SCALING_MODEL** | Horizontal — fully stateless service; all OTP state in Redis with TTL; all identity records in PostgreSQL |
| **DEPLOYMENT** | Kubernetes namespace: `core` (collocated with auth, users) |
| **RESOURCE_CLASS** | 2 vCPU, 2GB RAM per pod; autoscale 3–20 pods based on OTP request queue depth |
| **IDEMPOTENCY** | All OTP requests keyed on `request_id` — idempotent within 5-minute window; duplicate requests return same dispatch response |
| **ASYNC_PROCESSING** | Trust scoring, duplicate detection, and fraud signal processing are async via Kafka consumers — do not block OTP dispatch path |
| **REDIS_TTL_STRATEGY** | OTP keys: 300s TTL; lockout keys: 1800s TTL; binding heartbeat: 60s TTL; VIT cache: matches VIT expiry |
| **SMS_GATEWAY_FALLBACK** | If Jasmin primary fails: retry via secondary Jasmin instance; if both fail: email OTP fallback (web accounts only); SMS-only accounts: LOG_INCIDENT + manual retry |

---

## §19 · SECURITY ENFORCEMENT

> 🔒 **SEALED RULE:** Zero-Trust at every layer. Phone numbers are PII. No raw phone number ever appears in any log, event, metric label, trace span, or audit record. Every phone reference outside the encrypted identity store is an HMAC-SHA256 hash.

| SECURITY CONTROL | IMPLEMENTATION | ENFORCEMENT POINT |
|---|---|---|
| Phone Number PII Protection | HMAC-SHA256 hash in all references; raw phone encrypted with AES-256-GCM for OTP dispatch only | Input validation + all output serializers |
| Tenant Isolation | phone_hash scoped per-tenant (different HMAC keys per tenant via Vault); no cross-tenant phone resolution | Input validation + HMAC key derivation |
| OTP Brute Force Prevention | 3 attempts max; 30-min lockout; Redis-enforced; no exponential hints on failure | OTP verification path |
| SIM Swap Detection | FRAUD_DETECTION_ENGINE query on every VM-2 and CHAMPIONSHIP scope request | Before OTP dispatch for high-risk scopes |
| Rate Limiting | 5 OTP requests / phone_hash / hour; 20 requests / IP / hour; enforced via Redis counters + Kong API gateway | API gateway + PPIA internal |
| JWT Signature on VIT | HMAC-SHA256 signed with PPIA signing key from HashiCorp Vault; key rotated monthly | VIT issuance |
| Device Fingerprint Binding | VM-2: device_id must match at OTP request and OTP submission; mismatch = hard block | OTP submission validation |
| Secret Management | No credentials, keys, or salts in code or config; all from HashiCorp Vault with dynamic secrets | Service startup |
| Encryption at Rest | All identity records in PostgreSQL encrypted with column-level AES-256; OTP values AES-256 in Redis | DB policy + Redis config |
| Encryption in Transit | TLS 1.3 enforced; mTLS between PPIA and internal services (Envoy) | Service mesh policy |
| Audit Log Immutability | Every PPIA action appended to immutable audit log; no update/delete permitted | All execution paths |
| WAF Protection | ModSecurity WAF at ingress; rate limit rules for /otp/request and /otp/verify endpoints | NGINX ingress |
| IP Velocity Monitoring | Wazuh SIEM monitors OTP endpoint IP patterns; PPIA consumes SIEM alerts | FRAUD_DETECTION_ENGINE integration |
| Access Log Tracking | All API calls logged with actor_id, tenant_id, phone_hash (never raw phone), timestamp | Observability layer |
| OWASP Top-10 | ModSecurity rules enforced on all PPIA HTTP endpoints | NGINX ingress layer |

---

## §20 · AUDIT & TRACEABILITY

### 20.1 Execution Audit Record

Every PPIA action must produce an immutable audit record:

```json
AUDIT_RECORD: PhoneIdentityAudit {
  audit_id:               UUID (v4, immutable)
  timestamp_utc:          ISO-8601 (millisecond precision)
  event_type:             enum [OTP_REQUESTED | OTP_DISPATCHED | OTP_VERIFIED | OTP_FAILED |
                                OTP_EXPIRED | OTP_LOCKED | VIT_ISSUED | BINDING_CREATED |
                                BINDING_REVOKED | SUSPICIOUS_FLAGGED | SIM_SWAP_DETECTED |
                                DUPLICATE_DETECTED | GUARDIAN_BOUND | SESSION_TOKEN_ISSUED |
                                SESSION_TOKEN_DENIED]
  phone_hash:             HMAC-SHA256(phone, tenant_key)   // NEVER raw phone
  tenant_id:              UUID
  user_id:                UUID?   // Null for pre-registration events
  request_id:             UUID?
  binding_id:             UUID?
  vit_id:                 UUID?
  device_fingerprint_id:  UUID?
  ip_address_hash:        HMAC-SHA256(ip, tenant_key)      // NEVER raw IP
  session_type:           string?
  session_id:             UUID?
  trust_score:            float?
  ml_model_version:       string   // Version of trust scorer model used
  agent_version:          string   // PPIA version
  decision:               string   // ALLOW / DENY / SUSPICIOUS / BLOCK
  decision_path:          string[] // Ordered rule/model chain
  anomaly_flags:          string[]
  confidence_score:       float
  worm_archive_ref:       UUID     // IMMUTABLE_ARCHIVE_SERVICE reference
}
```

> 🔒 **SEALED RULE:** Audit logs are immutable. No update, delete, or overwrite operation is permitted on any audit record by any agent, service, or human actor. Phone numbers and raw IP addresses are **never** present in any audit record. Violation triggers immediate CISO alert and forensic investigation.

### 20.2 Legal Retention Requirements

| RECORD TYPE | RETENTION PERIOD | STORAGE |
|---|---|---|
| OTP lifecycle audit records | 2 years | PostgreSQL (encrypted) → WORM after 90 days |
| Verified Identity Token records | 5 years | PostgreSQL (encrypted) → WORM after 1 year |
| Participant Binding Records | 7 years | IMMUTABLE_ARCHIVE_SERVICE |
| Guardian Chain records | 18 years from child's birth date OR 7 years from last activity (whichever is longer) | IMMUTABLE_ARCHIVE_SERVICE — LEGAL_HOLD |
| Fraud / SUSPICIOUS incident records | 10 years | IMMUTABLE_ARCHIVE_SERVICE — LEGAL_HOLD |
| Championship identity bindings | 10 years | IMMUTABLE_ARCHIVE_SERVICE |
| Royalty innovator identity anchors | 15 years | IMMUTABLE_ARCHIVE_SERVICE — WORM |

---

## §21 · FAILURE POLICY

> 🔒 **SEALED RULE:** No silent failures. On verification ambiguity, the default is **DENY**. The system must never grant access on an uncertain identity signal. Security > availability for identity decisions.

| FAILURE CONDITION | AGENT ACTION | ESCALATE TO | RETRY POLICY |
|---|---|---|---|
| Invalid input (schema violation) | `STOP → LOG_INCIDENT → REJECT 400` | Requesting service | No retry — fix input |
| Phone number fails E.164 validation | `REJECT 422 → LOG` | Requesting service | No retry |
| Jasmin SMS dispatch failure (primary) | `RETRY secondary Jasmin → LOG` | NOTIFICATION_SERVICE | Retry 3× on secondary; escalate on 3rd failure |
| Jasmin SMS dispatch failure (all instances) | `FALLBACK email OTP (web only) → LOG_INCIDENT` | OBSERVABILITY_AGENT | Manual retry for SMS-only accounts |
| Redis unavailable (OTP state) | `STOP_EXECUTION → LOG_CRITICAL → REJECT all OTP operations` | Engineering Lead + CISO | Retry 5× with 1s backoff; full halt if Redis down > 30s |
| ML trust model unavailable | `FALLBACK to rule-based trust scoring → LOG_INCIDENT` | ML_ROUTING_AGENT | Automatic — rule-based fallback activates; no escalation unless > 30 min |
| FRAUD_DETECTION_ENGINE unreachable | `USE cached blacklist (max 5min stale) → LOG_INCIDENT` | OBSERVABILITY_AGENT | Retry 3× with 5s backoff; proceed with cache on 3rd failure |
| VIT signing key unavailable (Vault) | `STOP_EXECUTION → LOG_CRITICAL → REJECT all VIT issuance` | CISO + Engineering Lead | Retry 10× with exponential backoff; escalate at 60s total |
| Duplicate binding attempt detected | `REJECT → LOG_INCIDENT → EMIT duplicate_event` | FRAUD_DETECTION_ENGINE, ADMIN_GOVERNANCE | No retry — human investigation required |
| SIM swap confirmed during session | `REVOKE binding → HALT session token → LOG_INCIDENT` | CISO + Session Orchestrator | No retry — human clearance required |
| Audit write failure | `STOP_EXECUTION → LOG to fallback channel → CRITICAL ALERT` | CISO + Engineering Lead | Retry 3×; full halt if all fail |
| Trust score below 0.30 | `BLOCK → FLAG for human review → LOG` | FRAUD_DETECTION_ENGINE | No retry — human must resolve |
| DigiLocker service unavailable | `CONTINUE without DigiLocker signal → LOG` | None (graceful degradation) | DigiLocker is optional signal; no escalation |

---

## §22 · PERFORMANCE MONITORING

### 22.1 Core Metrics

| METRIC | TARGET | ALERT THRESHOLD | OWNER |
|---|---|---|---|
| OTP Dispatch Success Rate | > 99% | < 97% | NOTIFICATION_SERVICE + OBSERVABILITY_AGENT |
| OTP Dispatch Latency P95 | < 200ms (API response) | > 500ms | OBSERVABILITY_AGENT |
| OTP Verification Latency P95 | < 100ms | > 300ms | OBSERVABILITY_AGENT |
| VIT Issuance Success Rate | > 99.5% | < 98% | OBSERVABILITY_AGENT |
| Participant Binding Success Rate | > 99% | < 97% | OBSERVABILITY_AGENT |
| Session Token Issuance Rate | > 99% (of valid bindings) | < 97% | Session orchestrators |
| Fraudulent OTP Attempt Rate | < 2% of total attempts | > 5% | FRAUD_DETECTION_ENGINE |
| SIM Swap Detection Rate | > 95% of known swaps | < 90% | ML Lead + CISO |
| Trust Scorer F1 (fraud class) | > 0.88 | < 0.82 | ML Lead |
| Trust Scorer PSI (self-drift) | PSI < 0.2 | PSI > 0.4 | ML Lead |
| Redis OTP State Availability | > 99.99% | < 99.9% | Engineering Lead |
| Audit Write Success Rate | 100% | Any failure | CISO + Engineering |
| Lockout Rate (legitimate users) | < 0.5% | > 2% | Product + FRAUD team |

### 22.2 Observability Stack Integration

- **Prometheus:** All metrics on `/metrics` endpoint; 15s scrape; labels include `tenant_id_hash` (hashed — never raw), `verification_mode`, `session_type`
- **Grafana:** Dedicated PPIA dashboard — OTP funnel (requested → dispatched → verified → bound), trust score distribution, fraud signal frequency, latency percentiles
- **Loki:** All execution logs with structured JSON; phone fields always hashed; no PII in log fields
- **OpenTelemetry:** Full distributed trace from OTP request → SMS dispatch → OTP submit → VIT issue → binding create → session token issue; `trace_id` propagated to downstream session orchestrators
- **Wazuh SIEM:** OTP endpoint IP velocity alerts, lockout pattern analysis, SIM swap correlation

---

## §23 · VERSIONING POLICY

| VERSIONING RULE | SPECIFICATION |
|---|---|
| **Version Scheme** | Semantic versioning: MAJOR.MINOR.PATCH |
| **MAJOR increment** | Breaking change to VIT structure, input/output contract schema, OTP state machine, or security model |
| **MINOR increment** | New verification modes, new scope types, new ML model versions, new optional fields |
| **PATCH increment** | Bug fixes, threshold adjustments, OTP TTL changes, documentation updates |
| **Mutation Policy** | Add-Only — no removal of fields; existing fields never renamed or re-typed |
| **Backward Compatibility** | All previous VIT versions must be accepted during a 7-day overlap window after MINOR/MAJOR release |
| **Migration Documentation** | Every version bump requires a migration note documenting security impact and rollout sequence |
| **Rollback Safety** | Every version deployable alongside previous version; Redis key format must be version-namespaced |
| **ML Model Version Binding** | Each PPIA version declares exact trust scorer, SIM swap detector, and duplicate detector model version IDs |
| **Prompt Version Binding** | Each PPIA version declares exact AI prompt template version IDs (fraud narrative, denial composer) |
| **HMAC Key Rotation** | Phone hash HMAC keys rotated monthly via HashiCorp Vault; dual-key window of 48h during rotation; old keys retained for audit record verification only |
| **VIT Signing Key Rotation** | VIT signing keys rotated every 90 days; all active VITs re-signed during rotation window |

---

## §24 · NON-NEGOTIABLE RULES

> 🔒 **SEALED RULE:** The following rules are absolute. No exception, override, or runtime bypass is permitted under any circumstances. Violation of any rule below triggers immediate CISO alert and forensic review.

### 24.1 PPIA Must NEVER:

- Store, log, emit, or expose raw phone numbers in any system other than the encrypted identity store and the encrypted OTP dispatch channel
- Issue a VIT for a scope that was not declared in the original OTP request
- Allow a VIT to be reused across sessions of different `session_type` or `session_id`
- Issue a session token (Jitsi / LiveKit / championship) without a valid, active `ParticipantBindingRecord`
- Allow a user to join a GD session, Dojo match, or championship without a verified phone binding
- Bypass the 3-attempt OTP lockout under any circumstance — including admin override without human authorization
- Allow cross-tenant phone hash resolution — phone identities are scoped to a single tenant
- Emit `user_id` or `phone_hash` together in any feature vector, Kafka event, or analytics record (they are always separated)
- Process royalty payouts or licensing contract signatures when the innovator's binding_status is SUSPICIOUS or REVOKED
- Allow minor accounts to access Championship, Billing, or GD_ASSESSMENT scopes without an active Guardian Chain binding
- Auto-promote a trust score above the ML model's output — trust scores may only be increased by DigiLocker signal integration
- Execute OTP dispatch on phone numbers in the FRAUD_BLACKLIST — even if requested by a PLATFORM_GOVERNOR role
- Assume a device fingerprint is unchanged between sessions — always re-validate for VM-2 scopes

### 24.2 PPIA Must ALWAYS:

- Default to DENY on any verification ambiguity — never ALLOW on uncertainty
- Hash all phone number references using HMAC-SHA256 with the tenant's Vault-managed key before any storage, logging, or event emission
- Validate tenant isolation on every request — phone_hash HMAC key is tenant-specific and must never be shared
- Emit a structured Kafka event on every OTP lifecycle state transition
- Write an immutable audit record before and after every VIT issuance and binding creation
- Enforce minimum shadow periods and session token TTLs as specified in §4.2 and §10.1
- Coordinate with CONSENT_AND_PARENT_PERMISSION_AGENT before issuing any VIT for a minor account
- Query FRAUD_DETECTION_ENGINE for SIM swap risk before issuing VM-2 or CHAMPIONSHIP scope VITs
- Respect the session token issuance gate defined in §10.2 — all preconditions must be met
- Enforce Guardian Chain binding for all minor accounts in high-stakes scopes
- Rotate HMAC phone hash keys and VIT signing keys on the defined schedule via HashiCorp Vault
- Freeze royalty and innovation economy operations when the innovator's binding is SUSPICIOUS
- Maintain a complete, unbroken audit trail for every phone identity lifecycle

> 🔒 **SEALED RULE:** These rules cannot be modified at runtime. Any modification requires a new MAJOR version of PPIA with full governance review, CISO authorization, legal review (due to PII handling changes), human authorization, and audit record creation.

---

*— END OF SPECIFICATION —*

**PHONE_PARTICIPANT_IDENTITY_AGENT · v1.0.0 · SEALED · LOCKED**
*Ecoskiller Antigravity · Intelligence & Innovation Core · INTERNAL RESTRICTED · PII-SENSITIVE*
