# 🔒 CALL_COST_CALCULATION_AGENT.md
## ECOSKILLER ANTIGRAVITY PLATFORM
### STATUS: SEALED · LOCKED · DETERMINISTIC · GOVERNED
### MUTATION POLICY: ADD-ONLY VIA VERSION BUMP
### CREATIVE INTERPRETATION: FORBIDDEN
### ASSUMPTION FILLING: FORBIDDEN
### DEFAULT BEHAVIOR: DENY
### FAILURE MODE: HALT ON AMBIGUITY → STOP → LOG → ESCALATE

---

```
╔══════════════════════════════════════════════════════════════════════╗
║         CALL_COST_CALCULATION_AGENT — ANTIGRAVITY SEALED PROMPT      ║
║  Platform : Ecoskiller Antigravity                                   ║
║  Version  : v1.0.0                                                   ║
║  Tier     : Enterprise SaaS · Multi-Tenant · Zero-Trust              ║
║  Scale    : 10M–100M Users                                           ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY — NON-NEGOTIABLE)

```yaml
AGENT_NAME        : CALL_COST_CALCULATION_AGENT
SYSTEM_ROLE       : Real-Time and Batch Communication Cost Computation Engine
PRIMARY_DOMAIN    : Billing · Subscription · Compliance · Communication Economics
EXECUTION_MODE    : Deterministic + Validated + Append-Only
DATA_SCOPE        : Per-Tenant · Per-Session · Per-User · Per-Feature-Gate
TENANT_SCOPE      : Strict Isolation — No cross-tenant data leakage permitted
FAILURE_POLICY    : HALT on ambiguity · LOG all anomalies · ESCALATE unresolved
VERSION           : v1.0.0
MUTATION_POLICY   : Add-Only via version bump — no in-place modification
AUDIT_POLICY      : Every computation emits immutable audit record (UUID-tagged)
INTERPRETATION    : FORBIDDEN — Agent executes declared rules only
```

> **This agent must never assume missing cost specifications.**
> **This agent must never silently absorb errors.**
> **This agent must never output unaudited cost decisions.**

---

## 2️⃣ PURPOSE DECLARATION

### 2.1 Problem This Agent Solves

The Ecoskiller Antigravity platform operates a complex, multi-channel real-time communication stack including:

- **Voice Group Discussions (GD)** — Self-hosted Jitsi + WebRTC sessions with forced mute/unmute orchestration
- **Dojo Match Sessions** — LiveKit-powered real-time skill battles and mentor interviews
- **Notification Channels** — Email (Postfix / Docker Mail Server), SMS (Jasmin SMS Gateway), Push (ntfy)
- **AI/LLM Inference Calls** — Embedding generation, semantic scoring, idea similarity, intelligence evaluation
- **TURN/STUN Relay** — coturn NAT traversal costs for mobile / restricted network candidates
- **API Gateway Overhead** — Kong + Envoy egress costs per-call

Each of these communication events generates measurable infrastructure cost. At 10M–100M user scale, unmonitored call cost accumulates into multi-million rupee/dollar liability. This agent calculates, attributes, audits, and governs every communication cost unit in real time and via batch reconciliation.

### 2.2 Input Consumed

- Communication session lifecycle events from all upstream services
- Infrastructure metering data from Prometheus, ClickHouse, and the event bus (Kafka/RabbitMQ)
- Tenant billing tier entitlements from the Billing & Subscription Service
- Feature flag state from Unleash (per-tenant communication feature gates)
- Model inference logs from the Embedding & Model Inference Service

### 2.3 Output Produced

- Per-session cost breakdown (tagged by tenant, user, session-type, channel)
- Per-tenant monthly communication cost ledger entry
- Real-time threshold breach alerts (when cost exceeds plan ceiling)
- Batch reconciliation reports for billing cycle finalization
- Royalty-safe cost attribution for innovator-generated content processed via AI calls
- Audit-ready cost evidence records for compliance review

### 2.4 Downstream Agents That Depend On This Agent

| Downstream Agent | Dependency |
|---|---|
| `BILLING_GOVERNANCE_AGENT` | Consumes cost ledger entries for invoice generation |
| `REVENUE_RECOGNITION_AGENT` | Uses attributed call cost for COGS computation |
| `AUDIT_COMPLIANCE_AGENT` | Consumes immutable cost audit records |
| `OBSERVABILITY_AGENT` | Receives cost anomaly signals |
| `DATA_GOVERNANCE_AGENT` | Verifies cost data append-only integrity |
| `ROYALTY_ACCOUNTING_ENGINE` | Deducts AI inference cost from innovator gross royalty |
| `FEATURE_STORE_SERVICE` | Receives per-user communication cost feature vector |
| `FRAUD_DETECTION_ENGINE` | Receives anomaly flags for cost abuse patterns |
| `TENANT_EXPORT_AGENT` | Exports cost ledger for tenant data portability |

### 2.5 Upstream Agents That Feed This Agent

| Upstream Agent | Data Provided |
|---|---|
| `Voice GD Orchestrator` | Session start/end events, participant count, speaking duration |
| `Dojo Match Engine` | Match session events, LiveKit room duration, media quality tier |
| `Notification Service` | SMS unit count, email send events, push delivery receipts |
| `Embedding & Model Inference Service` | Token counts, model version, inference latency |
| `Revenue Ingestion Gateway` | Business revenue reports (for royalty-proportional AI cost share) |
| `Stream Processing Service` | Real-time event aggregation from Kafka |
| `Billing & Subscription Service` | Plan entitlements, included unit pools, overage rates |
| `Auth Service` | Tenant + user identity validation for cost attribution |

---

## 3️⃣ INPUT CONTRACT (STRICT)

### 3.1 Primary Input Schema

```json
INPUT_SCHEMA: {
  "required_fields": [
    "event_id",
    "event_type",
    "tenant_id",
    "user_id",
    "session_id",
    "session_type",
    "timestamp_utc_start",
    "timestamp_utc_end",
    "duration_seconds",
    "channel_type",
    "unit_count",
    "infrastructure_layer",
    "plan_tier"
  ],
  "optional_fields": [
    "participant_count",
    "model_version",
    "token_count_input",
    "token_count_output",
    "media_quality_tier",
    "turn_relay_used",
    "sms_gateway_route",
    "email_template_id",
    "push_platform",
    "feature_flag_state",
    "royalty_session_flag",
    "ai_purpose_code"
  ],
  "validation_rules": [
    "event_id must be UUID v4 — reject if malformed",
    "tenant_id must resolve against Auth Service tenant registry — reject if unresolvable",
    "user_id must belong to declared tenant_id — reject cross-tenant user reference",
    "timestamp_utc_end must be >= timestamp_utc_start — reject negative duration",
    "duration_seconds must equal (timestamp_utc_end - timestamp_utc_start) ± 2 seconds tolerance — reject drift > 2s",
    "session_type must be one of ENUM: [GD_VOICE, DOJO_MATCH, MENTOR_INTERVIEW, AI_INFERENCE, SMS, EMAIL, PUSH, TURN_RELAY, API_EGRESS] — reject unknown types",
    "channel_type must be one of ENUM: [WEBRTC_JITSI, LIVEKIT, JASMIN_SMS, POSTFIX_EMAIL, NTFY_PUSH, COTURN, KONG_EGRESS, LLM_INFERENCE] — reject unknown channels",
    "unit_count must be integer > 0 — reject zero or negative",
    "plan_tier must match active subscription record in Billing Service — reject mismatched tiers",
    "if session_type = AI_INFERENCE: token_count_input and token_count_output are required — reject if absent",
    "if session_type = GD_VOICE or DOJO_MATCH: participant_count required — reject if absent",
    "if turn_relay_used = true: duration_seconds for relay must be provided as sub-field — reject if absent"
  ],
  "security_checks": [
    "Validate JWT bearer token of emitting service — reject unsigned or expired tokens",
    "Validate tenant_id isolation — cross-tenant event injection triggers SECURITY_HALT",
    "Validate event_id uniqueness within processing window — reject replay attacks",
    "Validate source service identity against internal service registry — reject unknown emitters",
    "Rate-limit ingestion per tenant: max 10,000 events/minute — throttle excess, log breach"
  ],
  "domain_checks": [
    "GD_VOICE events must originate from Voice GD Orchestrator namespace only",
    "AI_INFERENCE events must include model_version matching Model Registry — reject unregistered versions",
    "ROYALTY_SESSION_FLAG = true requires innovator_id to be present — reject missing attribution",
    "SMS events must include sms_gateway_route matching active Jasmin configuration",
    "PUSH events must include push_platform in ENUM: [FCM, APNS, NTFY] — reject unknown platforms"
  ]
}
```

### 3.2 Null Tolerance Policy

```yaml
NULL_TOLERANCE_POLICY:
  required_fields  : ZERO — reject immediately on null
  optional_fields  : Permitted with explicit default substitution documented in audit log
  default_values   :
    participant_count       : null → log as UNKNOWN_PARTICIPANT_COUNT, flag for reconciliation
    model_version           : null → HALT if session_type = AI_INFERENCE
    turn_relay_used         : null → default false, log assumption
    royalty_session_flag    : null → default false
    media_quality_tier      : null → default STANDARD
```

### 3.3 Validation Failure Behavior

```yaml
ON_VALIDATION_FAILURE:
  ACTION           : REJECT_EVENT
  LOG              : Append to audit trail with rejection_reason, input_hash, timestamp_utc
  ALERT            : Emit VALIDATION_FAILURE_EVENT to Observability Agent
  RETRY            : NOT permitted — emitting service must resubmit corrected event
  PARTIAL_COMPUTE  : FORBIDDEN — no cost computed on invalid input
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

### 4.1 Primary Output Schema

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "cost_record_id"          : "UUID v4 — immutable identifier",
    "tenant_id"               : "string — isolated per tenant",
    "user_id"                 : "string — attributed user",
    "session_id"              : "string — source session reference",
    "session_type"            : "ENUM — type of communication event",
    "channel_type"            : "ENUM — infrastructure channel used",
    "duration_seconds"        : "integer — validated session duration",
    "unit_count"              : "integer — countable units (minutes, tokens, messages)",
    "unit_cost_inr"           : "decimal(12,6) — per-unit cost in INR",
    "total_cost_inr"          : "decimal(12,6) — total computed cost",
    "currency"                : "INR (default) | USD (enterprise export)",
    "plan_tier"               : "string — applicable subscription tier",
    "included_units_consumed" : "integer — units drawn from plan pool",
    "overage_units"           : "integer — units beyond plan pool",
    "overage_cost_inr"        : "decimal(12,6) — cost of overage units only",
    "billing_period"          : "YYYY-MM — billing month",
    "cost_category"           : "ENUM: [MEDIA, NOTIFICATION, AI_INFERENCE, RELAY, EGRESS]",
    "royalty_deduction_flag"  : "boolean — true if cost reduces royalty payout",
    "royalty_deduction_amount": "decimal(12,6) — amount deducted from royalty",
    "threshold_breach_flag"   : "boolean — true if tenant cost ceiling exceeded",
    "anomaly_flag"            : "boolean — true if cost pattern is statistically abnormal",
    "computed_at_utc"         : "ISO8601 timestamp",
    "next_trigger_event"      : ["BILLING_LEDGER_UPDATE", "THRESHOLD_ALERT", "ROYALTY_DEDUCTION"]
  },
  "confidence_score"  : "0.0–1.0 — computation confidence (1.0 for rule-based, <1.0 for estimated units)",
  "model_version"     : "string — cost model version used (e.g. COST_MODEL_v1.0.0)",
  "audit_reference"   : "UUID v4 — links to immutable audit log entry",
  "next_trigger_event": [
    "BILLING_LEDGER_APPEND_EVENT",
    "THRESHOLD_BREACH_ALERT (conditional)",
    "ROYALTY_DEDUCTION_TRIGGER (conditional)",
    "ANOMALY_ESCALATION_EVENT (conditional)"
  ]
}
```

### 4.2 Output Guarantees

```yaml
OUTPUT_GUARANTEES:
  - Every output includes audit_reference — outputs without UUID are invalid and rejected
  - Every output includes model_version — cost model must be pinned and versioned
  - Confidence score < 0.80 triggers HUMAN_REVIEW_QUEUE flag
  - All monetary values use decimal(12,6) precision — no floating point arithmetic
  - Outputs are append-only to cost ledger — no update or delete operations
  - Cross-tenant outputs are architecturally impossible — enforced at DB row-level security
```

---

## 5️⃣ COST COMPUTATION LOGIC LAYER

### 5.1 Cost Model Architecture

```yaml
COST_MODEL_TYPE       : Rule-Based + ML Anomaly Layer
PRIMARY_ENGINE        : Deterministic Rule Engine (100% of billing computation)
ML_OVERLAY            : Anomaly Detection + Drift Monitoring ONLY
                        (ML does NOT compute cost — it validates cost patterns)
AI_USAGE              : FORBIDDEN for cost computation
                        (AI may assist cost EXPLANATION for dashboards only)
```

### 5.2 Communication Channel Cost Matrix

All costs below are per-unit rates. Rates are versioned and stored in the `COST_RATE_REGISTRY` (append-only, immutable per version).

#### 5.2.1 Voice GD (Jitsi + WebRTC + coturn)

```
CHANNEL              : GD_VOICE
INFRASTRUCTURE       : Self-hosted Jitsi + WebRTC (Jitsi Videobridge + Jicofo)
                       + coturn NAT traversal
COST_UNIT            : Per participant-minute
BILLING_FORMULA      :
  base_cost          = participant_count × duration_minutes × RATE_GD_PER_PARTICIPANT_MINUTE
  turn_relay_cost    = (turn_relay_used ? relay_duration_minutes × RATE_TURN_PER_MINUTE : 0)
  session_cost_total = base_cost + turn_relay_cost

RATE_GD_PER_PARTICIPANT_MINUTE : Loaded from COST_RATE_REGISTRY (infrastructure-metered)
RATE_TURN_PER_MINUTE           : Loaded from COST_RATE_REGISTRY (bandwidth-metered)

BANDWIDTH_PROFILE (per participant, Voice-Only):
  upload   : 40–60 kbps
  download : 150–250 kbps (5–6 participant session)

NOTES:
  - Jitsi is self-hosted → no per-minute SaaS licensing cost
  - Cost = infrastructure amortization only (VM, bandwidth, storage overhead)
  - coturn adds relay cost only when NAT traversal is required
  - Aborted sessions (join_window_violation, early_exit) billed for actual
    elapsed duration only — no minimum billing floor at GD scope
  - Spectator joins (late join → spectator mode per GD rules) billed at
    spectator_rate (lower tier, receive-only bandwidth profile)
```

#### 5.2.2 Dojo Match + Mentor Interview (LiveKit SFU)

```
CHANNEL              : LIVEKIT_DOJO
INFRASTRUCTURE       : LiveKit SFU
COST_UNIT            : Per participant-minute
BILLING_FORMULA      :
  base_cost          = participant_count × duration_minutes × RATE_LIVEKIT_PER_PARTICIPANT_MINUTE
  quality_multiplier = (media_quality_tier = HD ? 1.4 : 1.0)
  session_cost       = base_cost × quality_multiplier

MEDIA_TIERS:
  STANDARD : Audio + SD video (default Dojo)
  HD       : HD video (Mentor Interview premium tier)

NOTES:
  - LiveKit costs include SFU hosting amortization
  - Quality tier is locked at session creation from feature gate
  - Participant count validated against Dojo Match Engine room roster
```

#### 5.2.3 SMS Notifications (Jasmin SMS Gateway)

```
CHANNEL              : JASMIN_SMS
INFRASTRUCTURE       : Jasmin SMS Gateway → Telecom operator route
COST_UNIT            : Per SMS message delivered
BILLING_FORMULA      :
  sms_cost           = unit_count × RATE_SMS_PER_MESSAGE[sms_gateway_route]

ROUTE_TIERS:
  DLT_TRANSACTIONAL  : OTP, interview alerts (premium route)
  PROMOTIONAL        : Campaign bulk SMS (lower route)
  INTERNATIONAL      : Cross-country delivery (highest rate)

RATES                : Loaded from COST_RATE_REGISTRY keyed by route + operator

NOTES:
  - Undelivered SMS: logged, NOT billed (delivery receipt required)
  - Failed delivery attempt: logged as NOTIFICATION_FAILURE, no cost recorded
  - Bulk batch SMS metered per individual message, not per batch
```

#### 5.2.4 Email Notifications (Postfix / Docker Mail Server)

```
CHANNEL              : POSTFIX_EMAIL
INFRASTRUCTURE       : Self-hosted Postfix + Docker Mail Server
COST_UNIT            : Per email sent (per recipient)
BILLING_FORMULA      :
  email_cost         = unit_count × RATE_EMAIL_PER_MESSAGE

RATES:
  TRANSACTIONAL      : Verification, alerts, invoices
  BULK_CAMPAIGN      : Mass outreach (higher infrastructure load factor)

NOTES:
  - Email is self-hosted → cost = infrastructure amortization only
  - Bounce tracking: bounced emails billed at sent rate (infrastructure consumed)
  - SMTP relay to external providers (if used): rate loaded separately from registry
  - Attachment emails: size_multiplier applied if attachment > 1MB
```

#### 5.2.5 Push Notifications (ntfy)

```
CHANNEL              : NTFY_PUSH
INFRASTRUCTURE       : Self-hosted ntfy + FCM/APNS relay
COST_UNIT            : Per push notification delivered per device
BILLING_FORMULA      :
  push_cost          = unit_count × RATE_PUSH_PER_NOTIFICATION[push_platform]

PLATFORM_RATES:
  NTFY_SELF          : Lowest (self-hosted relay)
  FCM_ANDROID        : Firebase relay cost (Google Cloud pricing)
  APNS_IOS           : Apple Push relay cost

NOTES:
  - Multi-device delivery: each device counts as one unit
  - Silent push (background sync): billed at reduced rate
  - Rich notification (image payload): size_multiplier applied
```

#### 5.2.6 AI / LLM Inference Calls

```
CHANNEL              : LLM_INFERENCE
INFRASTRUCTURE       : Internal LLM endpoint (20–30% AI usage tier)
COST_UNIT            : Per 1,000 tokens (input + output separately metered)
BILLING_FORMULA      :
  input_cost         = (token_count_input / 1000) × RATE_TOKEN_INPUT_PER_1K
  output_cost        = (token_count_output / 1000) × RATE_TOKEN_OUTPUT_PER_1K
  inference_cost     = input_cost + output_cost

AI_PURPOSE_CODES (ENUM — required field):
  IDEA_SIMILARITY         : Idea DNA Fingerprint Engine similarity scoring
  INTELLIGENCE_EVAL       : Dojo intelligence test open-response evaluation
  EMBEDDING_GENERATION    : Semantic vector generation for search/matching
  ROYALTY_NARRATIVE       : Parent LLM insight narrative generation
  CAREER_GUIDANCE         : Career probability AI report generation
  PARENT_INSIGHT          : Parent advisory LLM output
  FRAUD_SIGNAL            : Fraud pattern LLM reasoning assist
  ADMIN_GOVERNANCE        : Platform admin dispute reasoning assist

NOTES:
  - AI inference must NOT be used for billing computation — rule engine only
  - Token counts validated against Model Registry inference log
  - model_version is MANDATORY — reject inference events without version tag
  - ROYALTY_SESSION_FLAG = true: inference cost is deducted from innovator
    royalty gross payout via ROYALTY_ACCOUNTING_ENGINE
  - Prompt governance is enforced upstream — this agent receives only
    metered token counts, not prompt content
  - Confidence score for AI inference cost = 1.0 (token counts are deterministic)
```

#### 5.2.7 TURN/STUN Relay (coturn)

```
CHANNEL              : COTURN_RELAY
INFRASTRUCTURE       : Self-hosted coturn
COST_UNIT            : Per relay-minute (per participant stream using relay)
BILLING_FORMULA      :
  relay_cost         = relay_participant_minutes × RATE_TURN_PER_RELAY_MINUTE

NOTES:
  - STUN (IP resolution only): zero marginal cost, not billed
  - TURN (active relay): billed per relay-minute (bandwidth-intensive)
  - Relay usage flagged by WebRTC ICE candidate type in session metadata
  - Mobile network users statistically higher relay probability — tracked
    for infrastructure capacity planning signals
```

#### 5.2.8 API Gateway Egress (Kong + Envoy)

```
CHANNEL              : API_EGRESS_KONG
INFRASTRUCTURE       : Kong API Gateway + Envoy proxy
COST_UNIT            : Per 10,000 API calls or per GB egress (whichever applies)
BILLING_FORMULA      :
  call_cost          = (api_call_count / 10000) × RATE_API_PER_10K_CALLS
  egress_cost        = egress_gb × RATE_EGRESS_PER_GB
  total_egress_cost  = call_cost + egress_cost

NOTES:
  - Internal service-to-service calls: zero egress cost (intra-cluster)
  - External webhook deliveries: billed as egress
  - Rate-limited calls (blocked by Envoy): not billed — logged as RATE_LIMIT_EVENT
  - Integration Hub external API calls (LinkedIn, GitHub): billed as egress
```

### 5.3 Plan Tier Pool Deduction Logic

```yaml
PLAN_POOL_DEDUCTION:
  Step 1 : Load active subscription for tenant_id from Billing Service
  Step 2 : Identify included_unit_pool for session_type from plan entitlement
  Step 3 : Check remaining pool balance
    IF remaining_pool >= unit_count:
      included_units_consumed = unit_count
      overage_units = 0
      overage_cost_inr = 0.00
    IF remaining_pool < unit_count:
      included_units_consumed = remaining_pool
      overage_units = unit_count - remaining_pool
      overage_cost_inr = overage_units × OVERAGE_RATE[session_type]
  Step 4 : Deduct from pool (atomic operation — Redis distributed lock enforced)
  Step 5 : Emit pool balance update event

POOL_TYPES_BY_PLAN:
  FREE_TIER     : GD sessions: 5/month, SMS: 50/month, AI calls: 100/month
  BASIC         : GD sessions: 50/month, SMS: 500/month, AI calls: 1,000/month
  PROFESSIONAL  : GD sessions: 500/month, SMS: 5,000/month, AI calls: 10,000/month
  ENTERPRISE    : Custom pools negotiated per tenant SLA

OVERAGE_POLICY   : Overage billed at 1.5× base rate unless custom enterprise SLA defines otherwise
POOL_RESET       : Monthly on billing_period boundary — never mid-period
```

### 5.4 Threshold & Ceiling Enforcement

```yaml
THRESHOLD_POLICY:
  WARNING_THRESHOLD  : 80% of monthly plan cost ceiling → emit WARNING_ALERT
  CRITICAL_THRESHOLD : 95% of monthly plan cost ceiling → emit CRITICAL_ALERT + notify admin
  HARD_CEILING       : 100% of plan ceiling → SUSPEND_COMMUNICATION_FEATURES
                       (feature gate updated via Unleash within 30 seconds)
  ENTERPRISE_CEILING : Soft ceiling only — alert but no auto-suspension
                       (contractual override required per SLA)

HARD_CEILING_BEHAVIOR:
  - Jitsi GD rooms: new room creation blocked, active rooms allowed to complete
  - AI inference: blocked immediately for non-critical purposes
  - SMS: blocked except OTP transactional (safety exemption)
  - Email: blocked except account security emails (safety exemption)
  - Push: blocked except critical system alerts (safety exemption)
```

---

## 6️⃣ ML / ANOMALY DETECTION LAYER

```yaml
ML_ROLE: Cost Pattern Anomaly Detection ONLY
         (Not for cost computation — rule engine is exclusive for computation)

MODEL_TYPE         : Isolation Forest + Time-Series Anomaly Detection
FEATURES_USED      :
  - session_cost_inr (rolling 24h, 7d, 30d per tenant)
  - unit_count per session type (rolling windows)
  - participant_count vs expected_cohort_size
  - token_count deviation from ai_purpose_code baseline
  - overage_rate (frequency of overage events)
  - cost_per_user ratio vs plan tier

TRAINING_FREQUENCY : Weekly retrain on ClickHouse cost data
DRIFT_DETECTION    :
  - Monitor cost distribution shift per tenant tier (daily)
  - Monitor token count baseline per AI purpose code (weekly)
  - Alert if mean session cost shifts > 2 standard deviations from 30-day window

ANOMALY_FLAGS:
  COST_SPIKE        : Single session cost > 10× tenant's 30-day median session cost
  TOKEN_ABUSE       : AI inference token count > 5× purpose_code baseline
  RELAY_ANOMALY     : TURN relay used for > 60% of participants (network infrastructure alert)
  POOL_DRAIN        : Pool consumed > 50% within first 7 days of billing period
  CROSS_SESSION     : Same user_id generating cost events across conflicting session types simultaneously

VERSION_CONTROL    : model_version stored per anomaly detection run
                     Predictions are advisory — do not modify computed cost
```

---

## 7️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS       : 50,000 cost events/second at peak (10M concurrent users)
LATENCY_TARGET     :
  Real-time path   : < 50ms per event (P99)
  Batch path       : < 5 minutes for full billing period reconciliation
MAX_CONCURRENCY    : Horizontal — unlimited pod scaling via Kubernetes HPA
EXECUTION_STATE    : Stateless — all state in Redis + PostgreSQL
QUEUE_STRATEGY     :
  Real-time events : Kafka topic — cost.events.raw (partitioned by tenant_id)
  Batch jobs       : Apache Airflow DAG — nightly + end-of-period reconciliation
  Threshold alerts : Dedicated Kafka topic — cost.threshold.alerts (priority queue)

IDEMPOTENCY        :
  - event_id is idempotency key
  - Duplicate event_id within processing window → reject + log (no double billing)
  - Exactly-once delivery enforced via Kafka transactional producer

HORIZONTAL_SCALING :
  - Cost compute pods: stateless, auto-scale 2→200 pods
  - Redis cluster: cost pool state — sharded by tenant_id
  - ClickHouse: write-optimized cost ledger — columnar append-only
  - PostgreSQL: audit log + cost record metadata — row-level tenant isolation
```

---

## 8️⃣ SECURITY ENFORCEMENT

```yaml
SECURITY_CONTROLS:

  TENANT_ISOLATION:
    - All DB queries include tenant_id predicate (enforced at ORM layer)
    - Row-level security enabled on PostgreSQL cost tables
    - No aggregate query spans multiple tenants
    - Cross-tenant cost comparison: FORBIDDEN (analytics must use anonymized aggregates)

  DOMAIN_ISOLATION:
    - Cost events from GD domain cannot reference Royalty domain user_ids directly
    - AI inference cost events cannot expose prompt content — token counts only
    - Notification costs cannot expose message content — recipient count only

  ROLE-BASED_AUTHORIZATION:
    - PLATFORM_ADMIN  : Read all tenant cost data (audit role)
    - TENANT_ADMIN    : Read own tenant cost data only
    - BILLING_SERVICE : Write cost ledger entries (service-to-service JWT)
    - AGENT_SERVICE   : Emit cost events (service identity validation)
    - CANDIDATE/USER  : NO access to raw cost records
    - PARENT          : NO access to cost records

  ENCRYPTION:
    - All cost data at rest: AES-256 (managed by HashiCorp Vault)
    - All cost event transport: TLS 1.3 minimum
    - Monetary values stored as encrypted decimal — decrypted only in billing context

  AUDIT_LOGGING:
    - Every cost computation event appended to immutable audit log
    - Audit logs stored in MinIO WORM-style bucket (15+ year retention)
    - Log entries cannot be modified or deleted — append-only enforced at storage layer
    - Wazuh SIEM monitors audit log integrity (tampering detection)

  ACCESS_TRACKING:
    - Every read of cost record logged with accessor identity + timestamp
    - Anomalous access patterns (bulk reads, off-hours access) flagged to SIEM
```

---

## 9️⃣ AUDIT & TRACEABILITY

Every cost computation execution emits the following immutable audit record:

```json
AUDIT_LOG_SCHEMA: {
  "audit_id"          : "UUID v4 — globally unique",
  "timestamp_utc"     : "ISO8601",
  "actor_id"          : "service identity of emitting agent",
  "tenant_id"         : "string",
  "user_id"           : "string",
  "session_id"        : "string",
  "session_type"      : "ENUM",
  "input_hash"        : "SHA-256 of raw input event payload",
  "output_hash"       : "SHA-256 of computed cost record",
  "model_version"     : "COST_MODEL_vX.Y.Z",
  "decision_path"     : [
    "VALIDATION_PASSED",
    "POOL_DEDUCTION_APPLIED",
    "OVERAGE_COMPUTED",
    "THRESHOLD_CHECKED",
    "ANOMALY_CHECKED",
    "LEDGER_APPENDED"
  ],
  "confidence_score"  : "0.0–1.0",
  "anomaly_flags"     : ["COST_SPIKE", "TOKEN_ABUSE", ...],
  "billing_period"    : "YYYY-MM",
  "total_cost_inr"    : "decimal(12,6)",
  "pool_balance_after": "integer",
  "threshold_status"  : "NORMAL | WARNING | CRITICAL | CEILING_HIT",
  "immutable"         : true
}
```

> **Audit logs are write-once. No update. No delete. No override.
> Stored in MinIO + replicated to backup (Velero). Access requires PLATFORM_ADMIN JWT.**

---

## 🔟 FAILURE POLICY

```yaml
FAILURE_SCENARIOS:

  INVALID_INPUT:
    ACTION         : REJECT_EVENT
    LOG            : Append rejection record to audit trail
    EMIT           : VALIDATION_FAILURE_EVENT → Observability Agent
    RETRY          : Forbidden — emitting service must resubmit
    PARTIAL_OUTPUT : Forbidden

  MODEL_UNAVAILABLE (Cost Rate Registry unreachable):
    ACTION         : HALT_COMPUTATION
    LOG            : LOG_INCIDENT with error_code = RATE_REGISTRY_UNAVAILABLE
    ESCALATE_TO    : OBSERVABILITY_AGENT + BILLING_GOVERNANCE_AGENT
    RETRY_POLICY   : 3 retries with 500ms exponential backoff
                     After 3 failures → HALT + queue event for manual reconciliation
    FALLBACK       : No cost computed — event held in dead-letter queue

  AI_INFERENCE_TIMEOUT (token count source unavailable):
    ACTION         : HALT_AI_COST_COMPUTATION
    LOG            : LOG_INCIDENT with session_id + timeout details
    ESCALATE_TO    : OBSERVABILITY_AGENT
    RETRY_POLICY   : 2 retries — if unresolved, mark event PENDING_RECONCILIATION
    BILLING_IMPACT : AI inference cost not computed — flagged for batch reconciliation

  DATA_CORRUPTION (input_hash mismatch on re-validation):
    ACTION         : SECURITY_HALT — full stop
    LOG            : LOG_INCIDENT with corruption_flag = true
    ESCALATE_TO    : FORENSICS_AGENT + AUDIT_COMPLIANCE_AGENT + ZERO_TRUST_AGENT
    RETRY_POLICY   : NONE — requires human review before any restart
    ALERT_CHANNEL  : Immediate Wazuh SIEM alert + Grafana dashboard critical flag

  CONFIDENCE_BELOW_THRESHOLD (< 0.80):
    ACTION         : COMPUTE_AND_FLAG
    LOG            : Output generated but tagged with LOW_CONFIDENCE_FLAG
    ESCALATE_TO    : HUMAN_REVIEW_QUEUE
    BILLING_IMPACT : Cost held as PROVISIONAL — not finalized until human review

  POOL_STATE_LOCK_FAILURE (Redis lock cannot be acquired):
    ACTION         : RETRY with 100ms jitter × 5 attempts
                     After 5 failures → HALT + LOG_INCIDENT
    ESCALATE_TO    : OBSERVABILITY_AGENT
    BILLING_IMPACT : Event queued — no cost applied until lock succeeds

  THRESHOLD_HARD_CEILING_HIT:
    ACTION         : SUSPEND_FEATURE_GATE via Unleash API (within 30 seconds)
    LOG            : LOG_INCIDENT with threshold_status = CEILING_HIT
    NOTIFY         : TENANT_ADMIN + PLATFORM_ADMIN
    BILLING_IMPACT : Subsequent events blocked — no cost computed post-suspension

GLOBAL_RULES:
  - NO silent failures permitted
  - NO partial cost outputs without full audit trail
  - NO cost record modification after commit
  - ALL failure events must emit structured events to Kafka dead-letter topic
```

---

## 1️⃣1️⃣ INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - Voice GD Orchestrator          → gd.session.completed event
  - Dojo Match Engine              → dojo.match.completed event
  - Notification Service           → notification.sent event
  - Embedding & Model Inference    → inference.completed event
  - Revenue Ingestion Gateway      → revenue.report.submitted event
  - Stream Processing Service      → aggregated feature streams
  - Billing & Subscription Service → plan.entitlement.query response
  - Auth Service                   → identity.validation response

DOWNSTREAM_AGENTS:
  - Billing Governance Agent       → cost.ledger.entry event
  - Royalty Accounting Engine      → royalty.deduction.trigger event
  - Audit Compliance Agent         → audit.record.appended event
  - Observability Agent            → cost.anomaly.signal event
  - Fraud Detection Engine         → cost.abuse.flag event
  - Feature Store Service          → user.cost.feature.vector event

EVENT_TRIGGERS_EMITTED:
  - cost.computed.event            : Every successful cost computation
  - cost.threshold.warning         : 80% ceiling reached
  - cost.threshold.critical        : 95% ceiling reached
  - cost.ceiling.hit               : 100% — triggers feature suspension
  - cost.anomaly.detected          : ML anomaly flag raised
  - cost.validation.failed         : Input rejected
  - cost.reconciliation.required   : Manual review needed
  - cost.pool.depleted             : Included unit pool exhausted

ALL_EVENTS_SCHEMA:
  {
    event_id        : UUID v4,
    event_type      : string (ENUM above),
    tenant_id       : string,
    payload         : object,
    timestamp_utc   : ISO8601,
    source_agent    : "CALL_COST_CALCULATION_AGENT",
    schema_version  : "v1.0.0"
  }
```

---

## 1️⃣2️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

```yaml
FEATURE_EMISSION:
  This agent emits per-user communication cost feature vectors to FEATURE_STORE_SERVICE
  for use in:
    - Intelligence Prediction Engine (communication investment as growth signal)
    - Skill Demand Forecast Agent (correlation between communication spend and career outcomes)
    - Career Probability Model (AI inference usage as learning intensity signal)

EMIT_FEATURE_VECTOR:
  {
    user_id          : string,
    feature_name     : "communication_cost_30d_inr",
    feature_value    : decimal — rolling 30-day total cost for user,
    feature_name_2   : "gd_session_participation_rate",
    feature_value_2  : decimal — GD sessions / available GD slots ratio,
    feature_name_3   : "ai_inference_usage_index",
    feature_value_3  : decimal — AI call count vs plan tier baseline,
    timestamp        : ISO8601,
    source_agent     : "CALL_COST_CALCULATION_AGENT"
  }

EMISSION_FREQUENCY : After every billing period close + on-demand for real-time ML pipelines
PRIVACY_RULE       : Feature vectors contain aggregated cost metrics only — no raw session data
                     No message content, no voice data, no AI prompt content
```

---

## 1️⃣3️⃣ INNOVATION ECONOMY COMPATIBILITY

```yaml
ROYALTY_COST_DEDUCTION:
  When ROYALTY_SESSION_FLAG = true:
    - AI inference cost for idea evaluation is attributable to innovator session
    - ROYALTY_ACCOUNTING_ENGINE receives deduction trigger with:
        innovator_id        : string
        deduction_amount    : decimal(12,6) in INR
        deduction_reason    : "AI_INFERENCE_COST_FOR_IDEA_EVALUATION"
        audit_reference     : UUID (links to cost record)
        billing_period      : YYYY-MM
    - Deduction is transparent — innovator dashboard shows gross royalty - cost deductions

IDEA_VECTOR_COMPATIBILITY:
  - This agent does NOT compute IDEA_VECTOR or SIMILARITY_HASH
  - It meters the cost of AI calls that produce those vectors
  - IDEA_DNA_AGENT and COPY_DETECTION_ENGINE consume AI call tokens — those tokens
    are metered by this agent and billed appropriately

ROYALTY_AUDIT_TRAIL:
  - Every royalty deduction generated by this agent is linked via audit_reference
  - ROYALTY_AUDIT_COMPLIANCE_SERVICE can query full deduction history per innovator
  - All deductions are immutable and append-only
```

---

## 1️⃣4️⃣ GROWTH ENGINE HOOK

```yaml
GROWTH_EVENTS:
  This agent does NOT directly emit XP or rank events.
  It feeds signals that the Growth Engine uses for:

  COST_AS_ENGAGEMENT_SIGNAL:
    - High GD participation (GD session cost events) → signals active platform engagement
    - Emitted to Feature Store → consumed by Skill XP Calibration Agent

  PLATFORM_HEALTH_SIGNAL:
    - Rising per-tenant cost with rising user activity = healthy growth signal
    - Emitted to ERP Analytics Agent for board-level reporting

  THRESHOLD_EVENTS_AS_UPGRADE_TRIGGERS:
    - cost.ceiling.hit event → triggers BILLING_GOVERNANCE_AGENT to initiate
      plan upgrade outreach via CRM
    - This creates a deterministic upsell pipeline driven by cost data
```

---

## 1️⃣5️⃣ PERFORMANCE MONITORING

```yaml
METRICS_EMITTED_TO_PROMETHEUS:
  - call_cost_agent_events_processed_total     (counter, by tenant, session_type)
  - call_cost_agent_computation_latency_ms     (histogram, P50/P95/P99)
  - call_cost_agent_validation_failures_total  (counter, by failure_reason)
  - call_cost_agent_anomalies_detected_total   (counter, by anomaly_type)
  - call_cost_agent_pool_depletions_total      (counter, by tenant, session_type)
  - call_cost_agent_threshold_breaches_total   (counter, by level: WARNING/CRITICAL/CEILING)
  - call_cost_agent_queue_depth                (gauge, dead-letter queue size)
  - call_cost_agent_confidence_score_avg       (gauge, rolling 1h average)

GRAFANA_DASHBOARDS:
  - Real-time cost event throughput per tenant
  - Pool balance heatmap (all tenants, all session types)
  - Anomaly detection timeline
  - Threshold breach history
  - Dead-letter queue depth trend
  - AI inference cost by purpose code

ALERTING_RULES:
  - computation_latency P99 > 100ms → PagerDuty alert
  - validation_failure_rate > 5% in 5-minute window → critical alert
  - dead_letter_queue_depth > 1,000 → immediate escalation
  - confidence_score_avg < 0.85 in 1h window → human review trigger

INTEGRATION:
  - Prometheus: metrics scrape every 15 seconds
  - Loki: log aggregation (structured JSON logs only)
  - OpenTelemetry: distributed trace per cost computation chain
  - Wazuh: security event correlation for access anomalies
```

---

## 1️⃣6️⃣ VERSIONING POLICY

```yaml
VERSIONING_RULES:
  CURRENT_VERSION        : COST_MODEL_v1.0.0
  MUTATION_POLICY        : Add-only — new cost rules added as new version
                           Existing rules immutable once version is published
  VERSION_ACTIVATION     : New version requires explicit platform admin activation
                           Old version remains active for in-flight billing periods
  BACKWARD_COMPATIBILITY : All outputs include model_version — historical records
                           can be audited against the version that produced them
  ROLLBACK_POLICY        : Prior version remains available for 90 days post-migration
                           Rollback requires PLATFORM_ADMIN approval + audit log entry

COST_RATE_REGISTRY_VERSIONING:
  - Rate changes are new COST_RATE_REGISTRY versions
  - No retroactive rate application — new rates apply from activation timestamp only
  - Tenants on fixed-term contracts: rate version locked to contract start date
  - All rate versions stored indefinitely (append-only, never purged)

SCHEMA_MIGRATION_POLICY:
  - New fields added with optional status and null-safe defaults
  - No field removal or rename permitted
  - Schema changes documented in migration changelog
  - Flyway manages PostgreSQL schema versions for cost tables
```

---

## 1️⃣7️⃣ NON-NEGOTIABLE RULES

```yaml
THIS AGENT MUST NOT:

  ✗ Create hidden cost computation logic outside this declared contract
  ✗ Modify any historical cost record after commit
  ✗ Auto-delete any audit log entry for any reason
  ✗ Override governance or compliance agent decisions
  ✗ Bypass the COST_RATE_REGISTRY — hardcoded rates are forbidden
  ✗ Compute costs using AI/LLM reasoning — rule engine only
  ✗ Allow cross-tenant cost data to appear in any single query
  ✗ Execute outside the declared session_type ENUM scope
  ✗ Produce cost outputs without confidence_score and audit_reference
  ✗ Apply retroactive rate changes to closed billing periods
  ✗ Deduct royalty amounts without explicit ROYALTY_SESSION_FLAG = true
  ✗ Suppress threshold alerts regardless of business reason
  ✗ Accept cost events without validated tenant_id and user_id
  ✗ Process cost events from unregistered or unverified emitting services
  ✗ Mix infrastructure cost data across GD domain and Royalty domain
  ✗ Allow silent failure — every failure path emits a structured failure event
```

---

## 1️⃣8️⃣ DEPLOYMENT CONTEXT

```yaml
KUBERNETES_NAMESPACE     : billing
SERVICE_IDENTITY         : call-cost-calculation-agent
RESOURCE_PROFILE:
  CPU_REQUEST            : 500m
  CPU_LIMIT              : 2000m
  MEMORY_REQUEST         : 512Mi
  MEMORY_LIMIT           : 2Gi
  REPLICAS_MIN           : 3 (HA)
  REPLICAS_MAX           : 100 (HPA on event queue depth)

HEALTH_CHECKS:
  LIVENESS               : /health/live (200 = running)
  READINESS              : /health/ready (200 = rate registry loaded + Redis connected)
  STARTUP_PROBE          : 30s grace period for rate registry warm-up

DEPENDENCIES_AT_STARTUP:
  - PostgreSQL (cost tables — readiness check)
  - Redis (pool state — readiness check)
  - Kafka (consumer group registered — readiness check)
  - COST_RATE_REGISTRY (loaded into memory — startup probe)
  - Auth Service (JWT validation endpoint — readiness check)

GRACEFUL_SHUTDOWN:
  - Drain in-flight events before pod termination
  - Commit all pending pool deductions
  - Flush audit log buffer
  - Emit agent.shutdown event to Observability Agent
  - Shutdown timeout: 30 seconds (hard kill after)

CI_CD_PIPELINE:
  - GitHub Actions: build → unit test → integration test → security scan → image push
  - Helm chart deployment: blue/green with automated rollback on health check failure
  - No manual production deployments permitted
  - DB migrations via Flyway on deployment — rollback-safe scripts only
```

---

## 1️⃣9️⃣ TESTING CONTRACT

```yaml
REQUIRED_TEST_COVERAGE    : 90% minimum (enforced in CI — build fails below threshold)

UNIT_TESTS:
  - All cost formula paths (GD, Dojo, SMS, Email, Push, AI, TURN, Egress)
  - Pool deduction logic (normal, partial pool, zero pool, overage)
  - Threshold trigger logic (warning, critical, ceiling)
  - All validation rules (required fields, ENUM values, timestamp consistency)
  - All failure paths (halt, retry, dead-letter)

INTEGRATION_TESTS:
  - End-to-end cost event from Kafka → computation → PostgreSQL ledger
  - Redis pool state atomic deduction under concurrent load
  - Anomaly detection model integration
  - Billing Governance Agent ledger handoff

SECURITY_TESTS:
  - Cross-tenant event injection (must be rejected)
  - Replay attack (duplicate event_id must be rejected)
  - Unauthorized service identity (must be rejected)
  - Rate limit enforcement (10,000 events/min — excess must throttle)

PERFORMANCE_TESTS:
  - 50,000 events/second sustained throughput test
  - P99 latency < 50ms at peak load
  - Pool deduction under 10,000 concurrent requests (no double-spend)

CHAOS_TESTS:
  - Redis unavailability → agent halts gracefully, no cost computed
  - Rate Registry unavailability → dead-letter queue fills, no silent failure
  - Kafka partition rebalance → idempotency maintained, no duplicate billing
```

---

## 2️⃣0️⃣ AGENT SEAL DECLARATION

```
╔══════════════════════════════════════════════════════════════════════╗
║                         AGENT SEAL                                   ║
║                                                                      ║
║  AGENT   : CALL_COST_CALCULATION_AGENT                               ║
║  VERSION : v1.0.0                                                    ║
║  STATUS  : SEALED · LOCKED · PRODUCTION-READY                       ║
║                                                                      ║
║  This agent specification is add-only from this point forward.      ║
║  No clause may be removed or modified without:                       ║
║    (a) Version bump to v1.1.0 or higher                             ║
║    (b) PLATFORM_ADMIN written approval                              ║
║    (c) Audit log entry for the modification rationale               ║
║    (d) Migration documentation for backward compatibility           ║
║                                                                      ║
║  Any execution that deviates from this specification is a           ║
║  GOVERNANCE VIOLATION and must be halted and reported.              ║
║                                                                      ║
║  SEALED BY : ECOSKILLER ANTIGRAVITY INTELLIGENCE & INNOVATION CORE  ║
║  PLATFORM  : ECOSKILLER ANTIGRAVITY                                  ║
║  ARCH TYPE : Enterprise SaaS · Zero-Trust · Multi-Tenant            ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

*End of CALL_COST_CALCULATION_AGENT.md — v1.0.0 — SEALED*
