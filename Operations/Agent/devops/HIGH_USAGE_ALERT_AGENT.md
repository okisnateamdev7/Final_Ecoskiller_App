# 🔒 HIGH_USAGE_ALERT_AGENT.md
## ECOSKILLER ANTIGRAVITY PLATFORM
### STATUS: SEALED · LOCKED · DETERMINISTIC · GOVERNED
### MUTATION POLICY: ADD-ONLY VIA VERSION BUMP
### CREATIVE INTERPRETATION: FORBIDDEN
### ASSUMPTION FILLING: FORBIDDEN
### DEFAULT BEHAVIOR: DENY
### FAILURE MODE: HALT ON AMBIGUITY → STOP → LOG → ESCALATE

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║           HIGH_USAGE_ALERT_AGENT — ANTIGRAVITY SEALED PROMPT                ║
║  Platform   : Ecoskiller Antigravity                                         ║
║  Version    : v1.0.0                                                         ║
║  Tier       : Enterprise SaaS · Multi-Tenant · Zero-Trust · Append-Only     ║
║  Scale      : 10M–100M Users · All Plan Tiers · All Communication Channels  ║
║  Domain     : Usage Monitoring · Threshold Enforcement · Billing Protection  ║
║               Feature Gate Execution · Admin Notification · Fraud Signals   ║
║  Stack Lock : Prometheus · Kafka · Redis · Unleash · PostgreSQL · Grafana   ║
║               Notification Service · PagerDuty · Airflow · ClickHouse       ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY — NON-NEGOTIABLE)

```yaml
AGENT_NAME           : HIGH_USAGE_ALERT_AGENT
SYSTEM_ROLE          : Real-Time Platform Usage Threshold Monitor, Multi-Channel
                       Alert Dispatcher, Feature Gate Enforcement Coordinator,
                       and Tenant Usage Ceiling Guardian for all Ecoskiller
                       Antigravity communication and compute channels
PRIMARY_DOMAIN       : Usage Monitoring · Threshold Enforcement · Alert Orchestration ·
                       Feature Gate Control via Unleash · Billing Protection ·
                       Overage Prevention · Fraud Signal Emission · Tenant Health
EXECUTION_MODE       : Continuous Real-Time (event-driven) + Scheduled Batch (periodic) ·
                       Deterministic Threshold Evaluation · Append-Only Audit ·
                       Sub-30-second enforcement latency for hard ceiling actions
DATA_SCOPE           : Per-Tenant · Per-Channel · Per-Plan-Tier · Per-Billing-Period ·
                       Per-User · Per-Feature-Pool · Per-Threshold-Level
TENANT_SCOPE         : Strict Isolation — no cross-tenant usage data visible in any
                       single alert computation or feature gate operation
FAILURE_POLICY       : NEVER silently skip a threshold breach ·
                       NEVER delay a hard ceiling enforcement beyond 30 seconds ·
                       HALT and ESCALATE if feature gate cannot be updated ·
                       LOG every threshold evaluation regardless of outcome
VERSION              : ALERT_MODEL_v1.0.0
MUTATION_POLICY      : Add-only via version bump — no in-place modification of
                       thresholds, enforcement rules, or alert routing
AUDIT_POLICY         : Every threshold evaluation emits immutable UUID-tagged audit record ·
                       Every feature gate change is double-logged (agent audit + Unleash audit)
FINANCIAL_AUTHORITY  : This agent enforces the billing ceiling that protects tenants
                       from unintended overcharges and protects platform revenue integrity ·
                       Threshold levels are financial instruments — no override without
                       BILLING_GOVERNANCE_AGENT + PLATFORM_ADMIN countersignature
ENFORCEMENT_SLA      : Hard ceiling enforcement: < 30 seconds from breach detection ·
                       WARNING alert delivery: < 60 seconds from threshold cross ·
                       CRITICAL alert delivery: < 30 seconds from threshold cross
AI_USAGE             : FORBIDDEN for threshold computation or enforcement decisions ·
                       AI may assist human-readable alert message generation ONLY ·
                       All enforcement logic is deterministic rule engine exclusively
```

> **This agent is the last enforcement layer before a tenant accumulates unintended financial liability.**
> **It must never be late. It must never be wrong. It must never be silent.**
> **A missed hard ceiling enforcement at 100M users can generate lakhs of INR in unintended charges within minutes.**
> **Every alert, every gate, every audit record is non-optional.**

---

## 2️⃣ PURPOSE DECLARATION

### 2.1 The Core Problem This Agent Solves

**The Scale Reality:**

Ecoskiller Antigravity operates at 10M–100M concurrent users across five plan tiers (FREE, BASIC, PROFESSIONAL, ENTERPRISE, and custom SLAs), eight metered communication channels (Voice GD, Dojo Match, SMS, Email, Push Notifications, AI/LLM Inference, TURN Relay, API Egress), and multiple compute resource pools. Each tenant is a school, enterprise, coaching centre, or individual family unit — none of whom are expected to monitor their own per-minute consumption in real time.

**Without this agent, three catastrophic failure modes occur:**

**Failure Mode 1 — Silent Overage Accumulation**
A tenant on the FREE plan dispatches 10,000 OTP SMS messages due to a loop bug in their notification code. The CALL_COST_CALCULATION_AGENT computes and records each cost event correctly. But no one intercepts the cumulative pattern. The billing period closes. The tenant receives a ₹50,000 invoice they did not expect. They dispute it. Legal is involved. Trust is destroyed.

**Failure Mode 2 — Uncapped AI Inference Abuse**
A BASIC-tier tenant's AI integration runs a runaway inference loop — 50,000 token calls in 3 hours. Each call is individually valid. The CALL_COST_CALCULATION_AGENT prices each correctly. But no ceiling is enforced in real time. The platform absorbs ₹2 lakh in unrecoverable GPU inference costs before anyone notices.

**Failure Mode 3 — GD Session Cascade Without Ceiling**
A school with a PROFESSIONAL plan schedules 600 GD sessions in a month (limit: 500). The first 500 are included. The next 100 are overage at 1.5× rate. The school's admin never saw a warning. The invoice arrives. They cancel their subscription. The platform loses the account.

**This agent prevents all three failure modes by:**
1. Monitoring cumulative usage across all 8 channels in real time from Kafka cost event streams
2. Evaluating every cost event against the tenant's plan ceiling at each threshold level (50%, 70%, 80%, 90%, 95%, 100%)
3. Dispatching role-appropriate, channel-appropriate, actionable alerts to the right personas (Tenant Admin, Platform Admin, School Principal, Parent, Billing Governance) at the right time
4. Executing hard ceiling enforcement via Unleash feature gate updates within 30 seconds of breach
5. Emitting fraud signals for anomalous consumption patterns that exceed what plan-level human usage could produce
6. Maintaining an immutable audit trail of every threshold event for billing disputes, regulatory review, and platform health reporting

### 2.2 What Channels and Resources Are Monitored

This agent monitors usage and cost pools across all metered dimensions of the platform:

| Monitored Channel | Source Agent | Pool Unit | Plan Example (BASIC) |
|---|---|---|---|
| Voice GD Sessions (Jitsi+WebRTC) | CALL_COST_CALCULATION_AGENT | Participant-sessions/month | 50 sessions |
| Dojo Match Sessions (LiveKit) | CALL_COST_CALCULATION_AGENT | Match sessions/month | 50 sessions |
| SMS — OTP + Transactional (Jasmin) | CALL_COST_CALCULATION_AGENT | Messages/month | 500 messages |
| SMS — Promotional (Jasmin) | CALL_COST_CALCULATION_AGENT | Messages/month | Shared SMS pool |
| Email — Transactional (Postfix) | CALL_COST_CALCULATION_AGENT | Emails/month | Included |
| Email — Bulk Campaign (Postfix) | CALL_COST_CALCULATION_AGENT | Emails/month | Shared email pool |
| Push Notifications (ntfy/FCM/APNS) | CALL_COST_CALCULATION_AGENT | Notifications/month | Included |
| AI/LLM Inference (Internal LLM) | CALL_COST_CALCULATION_AGENT | Tokens/month (per 1K) | 1,000 AI calls |
| TURN Relay (coturn) | CALL_COST_CALCULATION_AGENT | Relay-minutes/month | Metered |
| API Egress (Kong/Envoy) | CALL_COST_CALCULATION_AGENT | API calls + GB/month | Metered |
| **AGGREGATE COST CEILING** | CALL_COST_CALCULATION_AGENT | **Total INR/month** | **Plan ceiling** |

Additionally monitored (non-communication):
- Storage consumption (MinIO per-tenant bucket size)
- Active user seat count (vs plan user limit)
- Concurrent API request rate (vs rate limit)
- Data export volume (regulatory: large exports signal GDPR/data concern)

### 2.3 The Four Threshold Levels

```
THRESHOLD ARCHITECTURE — FOUR LEVELS PER CHANNEL PER TENANT:

  LEVEL 1 — EARLY WARNING (50% + 70% — optional, configurable per tenant)
    Purpose : Proactive awareness — give tenant time to adjust before billing impact
    Default : Enabled for ENTERPRISE tenants only (configurable for others)
    Action  : Informational notification to Tenant Admin via dashboard + email
              No feature gate change. No billing impact.

  LEVEL 2 — WARNING (80% — mandatory for all tiers)
    Purpose : Meaningful signal that significant portion of plan consumed
    Action  : Alert dispatched to Tenant Admin (email + push + in-app)
              Billing Governance notified internally
              No feature gate change yet

  LEVEL 3 — CRITICAL (95% — mandatory for all tiers)
    Purpose : Imminent ceiling — final opportunity to take manual action
    Action  : Alert dispatched to Tenant Admin + School Principal/Owner (if institute)
              BILLING_GOVERNANCE_AGENT initiates upgrade outreach via CRM
              Platform Admin informed
              No feature gate change yet — human window preserved

  LEVEL 4 — HARD CEILING (100% — enforced for FREE/BASIC/PROFESSIONAL)
    Purpose : Absolute cost protection — tenant ceiling enforced
    Action  : Feature gates suspended via Unleash within 30 seconds
              Tenant Admin receives SUSPENSION notice (email + in-app + push)
              Platform Admin alerted
              BILLING_GOVERNANCE_AGENT initiates upgrade pipeline
              No new cost events accepted for suspended channels

  ENTERPRISE EXCEPTION:
    ENTERPRISE plans: LEVEL 4 = SOFT CEILING only (alert only — no auto-suspension)
    Reason: Enterprise tenants have negotiated SLAs and contractual protections
    Enforcement: Manual escalation to BILLING_GOVERNANCE_AGENT + PLATFORM_ADMIN
```

### 2.4 Downstream Agents That Depend On This Agent

| Downstream Agent | Dependency |
|---|---|
| `BILLING_GOVERNANCE_AGENT` | Receives threshold events to initiate upgrade CRM workflows + invoice generation |
| `CALL_COST_CALCULATION_AGENT` | Receives suspension status — stops computing cost for suspended channels |
| `Notification Service` | Receives alert dispatch instructions (email, SMS, push, in-app) |
| `AUDIT_COMPLIANCE_AGENT` | Receives immutable threshold + gate enforcement audit records |
| `OBSERVABILITY_AGENT` | Receives platform-wide usage health metrics and anomaly signals |
| `FRAUD_DETECTION_ENGINE` | Receives anomalous consumption signals (machine-speed usage patterns) |
| `ADMIN_GOVERNANCE_SERVICE` | Receives escalation events for hard ceiling + enterprise soft ceiling breaches |
| `GEO_COMPLIANCE_AGENT` | Receives data export volume alerts (cross-border data concern signals) |
| `FEATURE_STORE_SERVICE` | Receives per-tenant usage intensity vectors for ML prediction models |
| `CRM_SERVICE` | Receives upgrade trigger events for sales outreach pipeline |
| `DATA_GOVERNANCE_AGENT` | Receives storage ceiling alerts |

### 2.5 Upstream Sources That Feed This Agent

| Upstream Source | Data Provided |
|---|---|
| `CALL_COST_CALCULATION_AGENT` | cost.computed.event (per-channel cost + billing units, real-time via Kafka) |
| `TELECOM_USAGE_RECONCILIATION_AGENT` | Reconciled usage corrections (override signals for corrected counts) |
| `Billing & Subscription Service` | Plan entitlements, pool sizes, ceiling values, billing period boundaries |
| `Auth Service` | Tenant identity validation, role resolution for alert routing |
| `Redis (Pool State)` | Real-time pool balance per tenant per channel (maintained by CALL_COST_CALCULATION_AGENT) |
| `Prometheus` | Infrastructure metrics (storage, API rate, concurrent users) |
| `Unleash` | Current feature gate state per tenant (to verify gate is set correctly) |
| `ClickHouse` | Historical usage aggregates for anomaly baseline computation |

---

## 3️⃣ PLAN TIER DEFINITIONS AND CEILING REGISTRY

> This section defines the authoritative plan configuration that governs all
> threshold evaluations. This registry is loaded at agent startup and versioned.
> No threshold can be evaluated without a registered plan ceiling.

### 3.1 Canonical Plan Tier Registry

```yaml
PLAN_TIER_REGISTRY_v1.0:

  FREE_TIER:
    plan_code              : FREE
    monthly_cost_ceiling_inr: 0   (included — no monetary ceiling; unit pool IS the ceiling)
    unit_pools:
      gd_sessions          : 5    per month
      dojo_match_sessions  : 5    per month
      sms_messages         : 50   per month (OTP + TRANSACTIONAL combined)
      promotional_sms      : 0    (NOT available on FREE tier)
      email_sends          : 100  per month
      push_notifications   : 500  per month
      ai_inference_calls   : 100  per month (token-based, per 1K tokens)
      turn_relay_minutes   : 30   per month
      api_egress_calls     : 1000 per month
      storage_gb           : 1    GB
      active_user_seats    : 5    users
    overage_policy         : BLOCKED (FREE tier has NO overage — hard ceiling only)
    suspension_mode        : HARD (feature gate enforced at 100%)
    enterprise_exception   : false
    early_warning_enabled  : false
    alert_channels         : [IN_APP, EMAIL]

  BASIC:
    plan_code              : BASIC
    monthly_cost_ceiling_inr: 999 INR (indicative platform cap)
    unit_pools:
      gd_sessions          : 50   per month
      dojo_match_sessions  : 50   per month
      sms_messages         : 500  per month (OTP + TRANSACTIONAL combined)
      promotional_sms      : 200  per month (separate pool)
      email_sends          : 2000 per month
      push_notifications   : 5000 per month
      ai_inference_calls   : 1000 per month
      turn_relay_minutes   : 300  per month
      api_egress_calls     : 10000 per month
      storage_gb           : 10   GB
      active_user_seats    : 50   users
    overage_policy         : BILLED_AT_1.5X (overage charged at 1.5× base rate)
    overage_ceiling_inr    : 499  INR (max overage before suspension kicks in)
    suspension_mode        : HARD (feature gate enforced at hard ceiling)
    enterprise_exception   : false
    early_warning_enabled  : false (configurable via PLATFORM_ADMIN)
    alert_channels         : [IN_APP, EMAIL, PUSH]

  PROFESSIONAL:
    plan_code              : PROFESSIONAL
    monthly_cost_ceiling_inr: 4999 INR (indicative platform cap)
    unit_pools:
      gd_sessions          : 500  per month
      dojo_match_sessions  : 500  per month
      sms_messages         : 5000 per month
      promotional_sms      : 2000 per month
      email_sends          : 20000 per month
      push_notifications   : 50000 per month
      ai_inference_calls   : 10000 per month
      turn_relay_minutes   : 3000  per month
      api_egress_calls     : 100000 per month
      storage_gb           : 100  GB
      active_user_seats    : 500  users
    overage_policy         : BILLED_AT_1.5X
    overage_ceiling_inr    : 1999 INR
    suspension_mode        : HARD
    enterprise_exception   : false
    early_warning_enabled  : true (50% + 70% alerts enabled by default)
    alert_channels         : [IN_APP, EMAIL, PUSH, SMS_OTP_FALLBACK]

  ENTERPRISE:
    plan_code              : ENTERPRISE
    monthly_cost_ceiling_inr: CUSTOM (loaded from contract_sla_registry per tenant)
    unit_pools             : CUSTOM (per contract — loaded from contract_sla_registry)
    overage_policy         : PER_CONTRACT
    suspension_mode        : SOFT (alert only — no auto-suspension)
    enterprise_exception   : true
    early_warning_enabled  : true (all levels: 50%, 70%, 80%, 90%, 95%, 100%)
    alert_channels         : [IN_APP, EMAIL, PUSH, SMS, DEDICATED_ACCOUNT_MANAGER]
    custom_sla_fields:
      - dedicated_account_manager_id
      - contract_start_date
      - contract_end_date
      - contract_rate_version
      - soft_ceiling_override_approver_role

PLAN_VERSION             : PLAN_REGISTRY_v1.0
MUTATION_POLICY          : Plans are add-only. Existing plan parameters immutable.
                           Rate changes require PLAN_REGISTRY_v1.1+
CUSTOM_ENTERPRISE_LOADING: Loaded from contract_sla_registry on tenant activation
                           Validated by BILLING_GOVERNANCE_AGENT on load
```

### 3.2 Threshold Percentage Table — Per Pool, Per Plan

```
╔══════════════════════════════════════════════════════════════════════════════════╗
║                    THRESHOLD EVALUATION TABLE                                   ║
╠══════════════╦══════════════╦═══════════════╦══════════════╦════════════════════╣
║ Level        ║ % of Pool    ║ FREE          ║ BASIC/PROF   ║ ENTERPRISE         ║
╠══════════════╬══════════════╬═══════════════╬══════════════╬════════════════════╣
║ EARLY_50     ║ 50%          ║ Disabled      ║ Configurable ║ Enabled (default)  ║
║ EARLY_70     ║ 70%          ║ Disabled      ║ Configurable ║ Enabled (default)  ║
║ WARNING      ║ 80%          ║ Enabled       ║ Enabled      ║ Enabled            ║
║ CRITICAL     ║ 95%          ║ Enabled       ║ Enabled      ║ Enabled            ║
║ CEILING      ║ 100%         ║ HARD SUSPEND  ║ HARD SUSPEND ║ SOFT ALERT ONLY    ║
║ OVERAGE      ║ > 100%       ║ BLOCKED       ║ BILLED 1.5×  ║ PER CONTRACT       ║
╚══════════════╩══════════════╩═══════════════╩══════════════╩════════════════════╝

EVALUATION_FREQUENCY:
  Real-time stream  : Evaluated on EVERY cost.computed.event from Kafka
  Scheduled batch   : Evaluated every 5 minutes for all active tenants (drift protection)
  Period boundary   : Evaluated at billing period start (reset all pools) and end (final state)

HYSTERESIS_RULE:
  Once a threshold level is crossed, re-alert suppression = 24 hours
  (prevent alert fatigue from repeated boundary crossings)
  EXCEPTION: CEILING (100%) — no re-alert suppression, enforce on every event post-ceiling
  EXCEPTION: FRAUD_PATTERN — no suppression regardless of prior alerts
```

---

## 4️⃣ INPUT CONTRACT (STRICT)

### 4.1 Primary Input — Cost Event Stream from Kafka

```json
COST_EVENT_INPUT_SCHEMA: {
  "required_fields": [
    "event_id",
    "tenant_id",
    "user_id",
    "session_id",
    "channel_type",
    "billing_unit_count",
    "billing_unit_cost_inr",
    "total_cost_inr",
    "billing_period",
    "plan_tier",
    "pool_balance_before",
    "pool_balance_after",
    "threshold_status_before",
    "computed_at_utc"
  ],
  "optional_fields": [
    "session_type",
    "anomaly_flag",
    "royalty_session_flag",
    "participant_count",
    "overage_units",
    "overage_cost_inr",
    "cost_record_id"
  ],
  "field_definitions": {
    "event_id"              : "UUID v4 — idempotency key for this agent's processing",
    "tenant_id"             : "string — must resolve in Auth Service",
    "user_id"               : "string — triggering user",
    "channel_type"          : "ENUM [GD_VOICE, DOJO_MATCH, SMS_OTP, SMS_TRANSACTIONAL, SMS_PROMOTIONAL, EMAIL_TRANSACTIONAL, EMAIL_BULK, PUSH_FCM, PUSH_APNS, PUSH_NTFY, AI_INFERENCE, TURN_RELAY, API_EGRESS, STORAGE, USER_SEATS]",
    "billing_unit_count"    : "integer > 0 — units consumed in this event",
    "billing_unit_cost_inr" : "decimal(12,6) — per-unit cost",
    "total_cost_inr"        : "decimal(12,6) — total cost of this event",
    "billing_period"        : "YYYY-MM — must match active billing period",
    "plan_tier"             : "ENUM [FREE, BASIC, PROFESSIONAL, ENTERPRISE] — current tenant plan",
    "pool_balance_before"   : "integer — pool units before this event was consumed",
    "pool_balance_after"    : "integer — pool units after this event was consumed",
    "threshold_status_before": "ENUM [NORMAL, EARLY_50, EARLY_70, WARNING, CRITICAL, CEILING_HIT] — prior state"
  },
  "validation_rules": [
    "event_id must be UUID v4 — reject malformed",
    "tenant_id must resolve in Auth Service — reject unknown tenants",
    "channel_type must match ENUM exactly — reject unknown channels",
    "billing_unit_count must be integer > 0 — reject zero or negative",
    "total_cost_inr must be decimal(12,6) — reject negative values",
    "billing_period must be YYYY-MM and match active period for tenant",
    "plan_tier must match tenant's current active plan in Billing Service",
    "pool_balance_after must be <= pool_balance_before (consumption cannot be negative)",
    "pool_balance_after must be >= 0 (pool cannot go below zero in this model)",
    "threshold_status_before must match ENUM — reject unknown states",
    "computed_at_utc must be within current billing period + 48h tolerance"
  ],
  "security_checks": [
    "Reject events not originating from CALL_COST_CALCULATION_AGENT (service identity check)",
    "Rate limit: max 100,000 cost events per minute per tenant — throttle excess",
    "event_id dedup window: 60 seconds — duplicate event_ids ignored (idempotency)",
    "Cross-tenant: tenant_id isolation enforced — no cross-tenant pool aggregation permitted"
  ]
}
```

### 4.2 Secondary Input — Scheduled Batch Evaluation Request

```json
BATCH_EVALUATION_REQUEST: {
  "required_fields": [
    "evaluation_id",
    "evaluation_type",
    "billing_period",
    "triggered_by"
  ],
  "evaluation_types": [
    "PERIODIC_5MIN      : Triggered by Airflow every 5 minutes — all active tenants",
    "PERIOD_BOUNDARY    : Triggered at billing period start/end",
    "ADHOC_TENANT       : Triggered by BILLING_GOVERNANCE_AGENT for specific tenant",
    "POST_RECONCILIATION: Triggered by TELECOM_USAGE_RECONCILIATION_AGENT after correction"
  ]
}
```

### 4.3 Tertiary Input — Manual Override Request (PLATFORM_ADMIN only)

```json
MANUAL_OVERRIDE_REQUEST: {
  "required_fields": [
    "override_id",
    "admin_user_id",
    "admin_jwt",
    "tenant_id",
    "override_type",
    "override_reason",
    "approval_reference"
  ],
  "override_types": [
    "EXTEND_CEILING       : Temporarily increase a tenant's pool ceiling",
    "LIFT_SUSPENSION      : Restore suspended features (post-payment or error correction)",
    "SUPPRESS_ALERT_LEVEL : Suppress a specific threshold level for N hours",
    "FORCE_SUSPENSION     : Manually enforce suspension (fraud response)"
  ],
  "security_rules": [
    "override_type EXTEND_CEILING: requires PLATFORM_ADMIN + BILLING_GOVERNANCE_AGENT countersignature",
    "override_type LIFT_SUSPENSION: requires PLATFORM_ADMIN + payment_confirmation_id",
    "override_type FORCE_SUSPENSION: requires PLATFORM_ADMIN only (fraud response — no countersignature delay)",
    "All overrides: logged in immutable audit trail with full approval chain",
    "All overrides: emit to AUDIT_COMPLIANCE_AGENT immediately"
  ]
}
```

### 4.4 Null Tolerance Policy

```yaml
NULL_TOLERANCE_POLICY:
  Cost event required fields  : ZERO — reject immediately on null
  Optional fields             :
    anomaly_flag              : null → default false
    royalty_session_flag      : null → default false
    participant_count         : null → log assumption, not required for alert evaluation
    overage_units             : null → computed from pool_balance_after if needed
  DEFAULT_LOG                 : All substituted defaults logged in processing audit record
  REJECTION_ACTION            : Emit to kafka dead-letter + LOG_VALIDATION_FAILURE
                                Do NOT silently drop invalid events — every rejection is recorded
```

---

## 5️⃣ OUTPUT CONTRACT (STRICT)

### 5.1 Threshold Evaluation Result Schema

```json
THRESHOLD_EVALUATION_RESULT: {
  "alert_record": {
    "alert_id"                    : "UUID v4 — immutable",
    "evaluation_id"               : "UUID — links to triggering cost event or batch run",
    "tenant_id"                   : "string",
    "billing_period"              : "YYYY-MM",
    "channel_type"                : "ENUM",
    "plan_tier"                   : "ENUM",

    "pool_total"                  : "integer — total pool units for this channel this period",
    "pool_consumed"               : "integer — units consumed so far",
    "pool_remaining"              : "integer — units remaining",
    "consumption_percentage"      : "decimal(6,4) — consumed / total × 100",
    "overage_units"               : "integer — units consumed beyond pool (0 if within pool)",
    "overage_cost_inr"            : "decimal(12,6) — accumulated overage cost (0 if within pool)",

    "threshold_level_crossed"     : "ENUM: [NONE, EARLY_50, EARLY_70, WARNING, CRITICAL, CEILING_HIT]",
    "threshold_level_prior"       : "ENUM — level before this evaluation",
    "threshold_newly_crossed"     : "boolean — true if level changed in this evaluation",
    "alert_suppressed_by_hysteresis": "boolean — true if within 24h re-alert window",

    "feature_gate_action"         : "ENUM: [NO_ACTION, GATE_SUSPENDED, GATE_RESTORED, GATE_ALREADY_SUSPENDED]",
    "unleash_flag_name"           : "string or null — Unleash feature flag updated",
    "unleash_update_status"       : "ENUM: [NOT_REQUIRED, SUCCESS, FAILED, PENDING_RETRY] or null",
    "unleash_update_latency_ms"   : "integer or null",
    "enforcement_sla_met"         : "boolean — true if gate updated within 30s of breach",

    "alerts_dispatched"           : [
      {
        "recipient_role"          : "ENUM: [TENANT_ADMIN, PLATFORM_ADMIN, SCHOOL_PRINCIPAL, BILLING_GOVERNANCE, CRM, ACCOUNT_MANAGER]",
        "recipient_id"            : "string",
        "channel"                 : "ENUM: [EMAIL, PUSH, IN_APP, SMS_OTP, DEDICATED_CHANNEL]",
        "alert_template_id"       : "string",
        "dispatch_status"         : "ENUM: [SENT, FAILED, SUPPRESSED_HYSTERESIS, QUEUED]",
        "dispatched_at_utc"       : "ISO8601 or null"
      }
    ],
    "downstream_events_emitted"   : [
      "BILLING_GOVERNANCE_NOTIFIED",
      "FRAUD_SIGNAL_EMITTED (conditional)",
      "CRM_UPGRADE_TRIGGER_FIRED (conditional)",
      "FEATURE_STORE_UPDATED"
    ],
    "fraud_signal_emitted"        : "boolean",
    "fraud_signal_reason"         : "ENUM or null",

    "evaluated_at_utc"            : "ISO8601",
    "model_version"               : "ALERT_MODEL_v1.0.0",
    "audit_reference"             : "UUID v4",
    "confidence_score"            : "1.0 — deterministic rule evaluation"
  }
}
```

### 5.2 Output Guarantees

```yaml
GUARANTEES:
  confidence_score          : Always 1.0 — threshold evaluation is deterministic
  alert_id                  : Always present — every evaluation has immutable UUID
  audit_reference           : Always present — every evaluation linked to audit record
  threshold_level_crossed   : Always populated — never null or "unknown"
  feature_gate_action       : Always populated — even if NO_ACTION, declared explicitly
  enforcement_sla_met       : Always populated — SLA accountability is non-negotiable
  alerts_dispatched         : Always an array — empty array if no alerts sent (with reason)
  cross_tenant_isolation    : Architecturally impossible — tenant_id predicate on all ops
  MONETARY_PRECISION        : decimal(12,6) — no floating point arithmetic anywhere
  IDEMPOTENCY               : Same event_id processed twice → same result, one audit record
```

---

## 6️⃣ THRESHOLD EVALUATION ENGINE

### 6.1 Core Evaluation Algorithm

```
THRESHOLD_EVALUATION_ALGORITHM:
─────────────────────────────────────────────────────────────────────

INPUT:
  tenant_id, channel_type, billing_period, pool_balance_after,
  plan_tier, pool_total (from plan registry)

STEP 1 — Compute Consumption Percentage
  consumed         = pool_total - pool_balance_after
  consumed_pct     = (consumed / pool_total) × 100
  IF pool_total == 0:
    consumed_pct = 100 (zero-pool = ceiling already hit — applies to PROMOTIONAL on FREE)

STEP 2 — Determine Current Threshold Level
  IF consumed_pct >= 100  : current_level = CEILING_HIT
  ELSE IF consumed_pct >= 95 : current_level = CRITICAL
  ELSE IF consumed_pct >= 80 : current_level = WARNING
  ELSE IF consumed_pct >= 70 : current_level = EARLY_70
  ELSE IF consumed_pct >= 50 : current_level = EARLY_50
  ELSE                        : current_level = NORMAL

STEP 3 — Detect Level Transition
  prior_level     = redis.get(threshold_state_key(tenant_id, channel_type, billing_period))
  IF prior_level == null: prior_level = NORMAL
  newly_crossed   = (current_level > prior_level)
    // Level ordering: NORMAL < EARLY_50 < EARLY_70 < WARNING < CRITICAL < CEILING_HIT

STEP 4 — Check Hysteresis Suppression
  IF newly_crossed AND current_level != CEILING_HIT:
    last_alert_key  = hysteresis_key(tenant_id, channel_type, current_level)
    last_alert_time = redis.get(last_alert_key)
    IF last_alert_time AND (now - last_alert_time) < 24 HOURS:
      suppressed_by_hysteresis = true
    ELSE:
      suppressed_by_hysteresis = false
  IF current_level == CEILING_HIT:
    suppressed_by_hysteresis = false  // CEILING NEVER SUPPRESSED

STEP 5 — Check Early Warning Eligibility
  IF current_level IN [EARLY_50, EARLY_70]:
    IF plan_tier == FREE: SKIP (early warnings disabled on FREE)
    IF plan_tier IN [BASIC, PROFESSIONAL]: CHECK tenant setting (early_warning_enabled flag)
    IF plan_tier == ENTERPRISE: PROCEED (always enabled)

STEP 6 — Execute Enforcement Action
  IF current_level == CEILING_HIT AND newly_crossed:
    IF plan_tier IN [FREE, BASIC, PROFESSIONAL]:
      EXECUTE feature_gate_suspension(tenant_id, channel_type)
        → UPDATE Unleash flag: tenant_{id}_{channel}_enabled = false
        → SLA: within 30 seconds
        → Emit: CALL_COST_CALCULATION_AGENT suspension signal
    IF plan_tier == ENTERPRISE:
      RECORD soft_ceiling_breach (no gate suspension)
      ESCALATE to ADMIN_GOVERNANCE_SERVICE + ACCOUNT_MANAGER

STEP 7 — Dispatch Alerts (if newly_crossed AND not suppressed)
  FOR each alert_recipient in ALERT_ROUTING_TABLE[plan_tier][current_level]:
    RESOLVE recipient_id from tenant contact registry
    SELECT alert_template from ALERT_TEMPLATE_REGISTRY[channel_type][current_level]
    RENDER alert content (interpolate usage numbers, plan tier, upgrade link)
    DISPATCH via Notification Service (async — does not block enforcement)
    RECORD dispatch result in alert_record

STEP 8 — Emit Downstream Events
  IF current_level IN [WARNING, CRITICAL, CEILING_HIT]:
    EMIT billing.threshold.event → BILLING_GOVERNANCE_AGENT
  IF current_level == CEILING_HIT:
    EMIT crm.upgrade.trigger → CRM_SERVICE (except ENTERPRISE)
  IF fraud_pattern_detected (Section 6.4):
    EMIT fraud.consumption.signal → FRAUD_DETECTION_ENGINE

STEP 9 — Persist State and Audit
  redis.set(threshold_state_key, current_level, TTL = billing_period_end + 7 days)
  IF newly_crossed AND NOT suppressed:
    redis.set(hysteresis_key, now, TTL = 24 HOURS)
  APPEND immutable audit record (Section 10)
  EMIT alert.record.appended → AUDIT_COMPLIANCE_AGENT
```

### 6.2 Aggregate Cost Ceiling Evaluation

In addition to per-channel pool evaluation, this agent evaluates the **aggregate INR spending ceiling** for BASIC and PROFESSIONAL tenants:

```yaml
AGGREGATE_CEILING_EVALUATION:

  WHAT_IT_IS:
    The sum of all channel costs (∑ total_cost_inr across all channels) for a tenant
    within the billing_period, measured against the plan's monthly_cost_ceiling_inr.

  FORMULA:
    aggregate_consumed_inr = SUM(total_cost_inr) for tenant_id WHERE billing_period = current
    aggregate_pct          = (aggregate_consumed_inr / monthly_cost_ceiling_inr) × 100
    threshold_level        = determined by same percentage table as per-channel

  WHEN_TRIGGERED:
    Evaluated on every cost.computed.event (all channels contribute to aggregate)
    Evaluated in 5-minute batch sweep for drift protection

  OVERAGE_CEILING_ENFORCEMENT:
    IF plan_tier IN [BASIC, PROFESSIONAL]:
      overage_accumulated_inr = aggregate_consumed_inr - pool_value_inr
      IF overage_accumulated_inr >= overage_ceiling_inr:
        // Overage ceiling is a SECOND hard ceiling — prevents runaway overage spend
        EXECUTE feature_gate_suspension for all metered channels
        EMIT OVERAGE_CEILING_BREACH event (distinct from pool ceiling breach)
        NOTIFY Tenant Admin + Platform Admin + BILLING_GOVERNANCE_AGENT

  FREE_TIER_RULE:
    FREE tier has NO monetary ceiling — only unit pool ceilings
    Aggregate INR evaluation: NOT APPLICABLE for FREE tier
```

### 6.3 Per-Channel Feature Gate Suspension Map

When a channel reaches CEILING_HIT, the following Unleash feature flags are updated:

```yaml
FEATURE_GATE_MAP:
  // Format: channel_type → Unleash flag name pattern → suspension behavior

  GD_VOICE:
    unleash_flag    : tenant_{id}_gd_voice_enabled
    suspension      : New GD room creation BLOCKED
    active_sessions : Allowed to complete (in-progress rooms not interrupted)
    affected_calls  : Jitsi room creation API, GD session scheduler

  DOJO_MATCH:
    unleash_flag    : tenant_{id}_dojo_match_enabled
    suspension      : New Dojo match creation BLOCKED
    active_matches  : Allowed to complete
    affected_calls  : Dojo Match Engine, LiveKit room creation

  SMS_OTP:
    unleash_flag    : tenant_{id}_sms_otp_enabled
    suspension      : EXEMPT from suspension
    reason          : OTP is authentication-critical — suspension creates account lockout
                      OTP SMS is NEVER suspended regardless of ceiling
    exception_logged: Every OTP sent post-ceiling logged as EXEMPT_SAFETY_CHANNEL

  SMS_TRANSACTIONAL:
    unleash_flag    : tenant_{id}_sms_transactional_enabled
    suspension      : BLOCKED post-ceiling (non-safety transactional)
    active_sends    : In-flight sends complete (Jasmin queue not flushed)

  SMS_PROMOTIONAL:
    unleash_flag    : tenant_{id}_sms_promotional_enabled
    suspension      : BLOCKED post-ceiling (first channel to suspend)
    immediate       : true (no in-flight exemption)

  EMAIL_TRANSACTIONAL:
    unleash_flag    : tenant_{id}_email_transactional_enabled
    suspension      : PARTIAL EXEMPT — security-critical emails continue
    exempted_types  : [ACCOUNT_SECURITY, PASSWORD_RESET, PAYMENT_CONFIRMATION]
    non_exempt      : All other transactional emails BLOCKED

  EMAIL_BULK:
    unleash_flag    : tenant_{id}_email_bulk_enabled
    suspension      : BLOCKED immediately

  PUSH_FCM:
    unleash_flag    : tenant_{id}_push_fcm_enabled
    suspension      : PARTIAL EXEMPT — critical system pushes continue
    exempted_types  : [SYSTEM_ALERT, ACCOUNT_SECURITY]
    non_exempt      : All engagement/notification pushes BLOCKED

  PUSH_APNS:
    unleash_flag    : tenant_{id}_push_apns_enabled
    suspension      : Same as PUSH_FCM

  PUSH_NTFY:
    unleash_flag    : tenant_{id}_push_ntfy_enabled
    suspension      : BLOCKED (internal notifications only — no safety exemption)

  AI_INFERENCE:
    unleash_flag    : tenant_{id}_ai_inference_enabled
    suspension      : BLOCKED for non-critical AI purposes
    exempted_purposes: [FRAUD_SIGNAL, ADMIN_GOVERNANCE] (platform-safety AI)
    non_exempt      : IDEA_SIMILARITY, INTELLIGENCE_EVAL, EMBEDDING, ROYALTY_NARRATIVE,
                      CAREER_GUIDANCE, PARENT_INSIGHT — all BLOCKED

  TURN_RELAY:
    unleash_flag    : tenant_{id}_turn_relay_enabled
    suspension      : BLOCKED — new relay sessions not permitted
    active_sessions : Allowed to complete (TURN disconnection = call drop = user harm)

  API_EGRESS:
    unleash_flag    : tenant_{id}_api_egress_enabled
    suspension      : Rate-limited to 10% of normal limit (not full block)
    reason          : Full API block would break tenant's integration — managed throttle

SUSPENSION_EXECUTION_PROTOCOL:
  1. Compute list of flags to update for tenant_id + channel_type
  2. Call Unleash Admin API: PATCH /api/admin/projects/default/features/{flag_name}
     body: { "enabled": false, "strategies": [{ "name": "tenantId", "parameters": { "tenantIds": "{tenant_id}" }}] }
  3. Verify flag state after update: GET /api/admin/projects/default/features/{flag_name}
  4. IF verification fails: RETRY 3× with 5s exponential backoff
  5. IF all retries fail: HALT_AND_ESCALATE → ADMIN_GOVERNANCE_SERVICE (SEV-1)
  6. Record unleash_update_status + unleash_update_latency_ms in alert_record
  7. Emit FEATURE_GATE_UPDATED event to AUDIT_COMPLIANCE_AGENT

SUSPENSION_SLA_ENFORCEMENT:
  Hard ceiling breach to gate update: < 30 seconds (SLA)
  IF enforcement_sla_met = false: Emit SLA_BREACH_EVENT to PLATFORM_ADMIN + OBSERVABILITY_AGENT
  SLA breaches are tracked and reported in monthly platform health reports
```

### 6.4 Fraud Pattern Detection

This agent detects usage patterns that cannot be explained by human interaction and emits signals to FRAUD_DETECTION_ENGINE:

```yaml
FRAUD_PATTERN_DETECTION:
  ROLE: Pattern anomaly detection for consumption abuse ONLY
        (Does NOT modify thresholds or enforcement — advisory signal only)

  PATTERN_1 — MACHINE_SPEED_CONSUMPTION:
    Trigger  : > 1000 SMS events in 60 seconds for a single tenant
    Signal   : MACHINE_SPEED_SMS_ABUSE
    Reason   : Human users cannot trigger 1000 SMS sends in 60 seconds
    Action   : Emit fraud.consumption.signal → FRAUD_DETECTION_ENGINE immediately
               Do NOT suppress threshold alerts — continue normal enforcement

  PATTERN_2 — POOL_DRAIN_VELOCITY:
    Trigger  : > 50% of monthly pool consumed in < 24 hours
    Signal   : POOL_DRAIN_VELOCITY_ANOMALY
    Reason   : Healthy usage distributes across the month — front-loading = likely abuse or bug
    Action   : Emit signal + emit WARNING threshold alert immediately (skip EARLY levels)
               Notify TENANT_ADMIN and PLATFORM_ADMIN even if suppression window active

  PATTERN_3 — AI_INFERENCE_SPIKE:
    Trigger  : > 5× 7-day rolling average for AI inference calls in a single hour
    Signal   : AI_INFERENCE_ABUSE_SUSPECTED
    Reason   : Legitimate AI usage follows session patterns — spikes indicate runaway loops
    Action   : Emit signal + escalate to PLATFORM_ADMIN + OBSERVABILITY_AGENT
               Consider immediate AI_INFERENCE gate suspension (human decision required)

  PATTERN_4 — CROSS_CHANNEL_SIMULTANEOUS_SPIKE:
    Trigger  : > 80% threshold crossed simultaneously on 3+ channels within 5 minutes
    Signal   : COORDINATED_USAGE_SPIKE
    Reason   : Legitimate tenant activity rarely spikes all channels simultaneously
    Possible causes: DDoS reflected traffic, integration bug, compromised tenant credentials
    Action   : Emit signal → FRAUD_DETECTION_ENGINE + ZERO_TRUST_AGENT immediately

  PATTERN_5 — POST_CEILING_OTP_VOLUME:
    Trigger  : > 100 OTP SMS sent post-ceiling suspension (OTP is exempt from suspension)
    Signal   : OTP_ABUSE_POST_CEILING
    Reason   : OTP exemption is for authentication safety — if OTP volume is still high
               post-ceiling, it may indicate the tenant's OTP trigger itself is looping
    Action   : Emit signal → FRAUD_DETECTION_ENGINE
               Notify TENANT_ADMIN with specific OTP loop warning

  FRAUD_SIGNAL_SCHEMA:
    {
      signal_id       : UUID,
      tenant_id       : string,
      signal_type     : ENUM (from patterns above),
      trigger_metric  : string — what measurement triggered the signal,
      trigger_value   : decimal — actual value that breached the pattern threshold,
      baseline_value  : decimal — expected/normal value,
      detected_at_utc : ISO8601,
      channel_types   : [list of affected channels],
      alert_id        : UUID — links to triggering threshold evaluation
    }
```

---

## 7️⃣ ALERT CONTENT AND ROUTING ENGINE

### 7.1 Alert Recipient Routing Table

```yaml
ALERT_ROUTING_TABLE:

  FREE_TIER:
    EARLY_50     : NOT_DISPATCHED (disabled on FREE)
    EARLY_70     : NOT_DISPATCHED (disabled on FREE)
    WARNING_80   : [TENANT_ADMIN → IN_APP + EMAIL]
    CRITICAL_95  : [TENANT_ADMIN → IN_APP + EMAIL + PUSH]
    CEILING_100  : [TENANT_ADMIN → IN_APP + EMAIL + PUSH,
                   PLATFORM_ADMIN → IN_APP (internal)]

  BASIC:
    EARLY_50     : [TENANT_ADMIN → IN_APP] (if enabled)
    EARLY_70     : [TENANT_ADMIN → IN_APP + EMAIL] (if enabled)
    WARNING_80   : [TENANT_ADMIN → IN_APP + EMAIL + PUSH]
    CRITICAL_95  : [TENANT_ADMIN → IN_APP + EMAIL + PUSH,
                   BILLING_GOVERNANCE_AGENT → internal event]
    CEILING_100  : [TENANT_ADMIN → IN_APP + EMAIL + PUSH,
                   PLATFORM_ADMIN → IN_APP,
                   BILLING_GOVERNANCE_AGENT → upgrade trigger]

  PROFESSIONAL:
    EARLY_50     : [TENANT_ADMIN → IN_APP + EMAIL]
    EARLY_70     : [TENANT_ADMIN → IN_APP + EMAIL + PUSH]
    WARNING_80   : [TENANT_ADMIN → IN_APP + EMAIL + PUSH,
                   SCHOOL_PRINCIPAL/OWNER → EMAIL (if institute tenant)]
    CRITICAL_95  : [TENANT_ADMIN → IN_APP + EMAIL + PUSH + SMS_OTP_FALLBACK,
                   SCHOOL_PRINCIPAL/OWNER → IN_APP + EMAIL,
                   BILLING_GOVERNANCE_AGENT → upgrade trigger]
    CEILING_100  : [TENANT_ADMIN → IN_APP + EMAIL + PUSH + SMS_OTP_FALLBACK,
                   SCHOOL_PRINCIPAL/OWNER → IN_APP + EMAIL,
                   PLATFORM_ADMIN → IN_APP + internal escalation,
                   BILLING_GOVERNANCE_AGENT → urgent upgrade trigger]

  ENTERPRISE:
    EARLY_50     : [TENANT_ADMIN → IN_APP + EMAIL,
                   ACCOUNT_MANAGER → DEDICATED_CHANNEL]
    EARLY_70     : [TENANT_ADMIN → IN_APP + EMAIL + PUSH,
                   ACCOUNT_MANAGER → DEDICATED_CHANNEL + EMAIL]
    WARNING_80   : [TENANT_ADMIN → IN_APP + EMAIL + PUSH,
                   ACCOUNT_MANAGER → DEDICATED_CHANNEL + EMAIL + SMS]
    CRITICAL_95  : [TENANT_ADMIN → IN_APP + EMAIL + PUSH + SMS,
                   ACCOUNT_MANAGER → DEDICATED_CHANNEL + EMAIL + SMS + PHONE_FLAG,
                   BILLING_GOVERNANCE_AGENT → contract review trigger]
    CEILING_100  : [TENANT_ADMIN → IN_APP + EMAIL + PUSH + SMS,
                   ACCOUNT_MANAGER → DEDICATED_CHANNEL + EMAIL + SMS + PHONE_FLAG,
                   ADMIN_GOVERNANCE_SERVICE → soft ceiling escalation,
                   BILLING_GOVERNANCE_AGENT → contract amendment trigger]

RECIPIENT_RESOLUTION:
  TENANT_ADMIN         : tenant.contacts.primary_admin_user_id (from tenant registry)
  SCHOOL_PRINCIPAL     : tenant.institute_profile.principal_contact_id (if institute type)
  PLATFORM_ADMIN       : platform.admin.on_call_id (from Admin Governance Service)
  ACCOUNT_MANAGER      : enterprise_sla_registry.dedicated_account_manager_id
  BILLING_GOVERNANCE   : Internal Kafka event (not human — agent-to-agent)
  CRM_SERVICE          : Internal Kafka event (not human — trigger only)

RECIPIENT_RESOLUTION_FAILURE:
  IF recipient_id cannot be resolved: LOG_INCIDENT
  FALLBACK: platform.admin.default_id for unresolved recipients
  NEVER silently drop an alert due to unresolved recipient
```

### 7.2 Alert Template Definitions

```yaml
ALERT_TEMPLATE_REGISTRY:

  TEMPLATE: WARNING_80_CHANNEL
    ID         : HUAA_WARN_CHANNEL_v1
    Subject    : "⚠️ {{channel_name}} Usage at {{consumption_pct}}% — {{tenant_name}}"
    Body_EN    : |
      Dear {{recipient_name}},

      Your {{channel_name}} usage has reached {{consumption_pct}}% of your
      monthly {{plan_tier}} plan allocation.

      Current Status:
        Channel      : {{channel_name}}
        Used         : {{pool_consumed}} of {{pool_total}} {{unit_label}}
        Remaining    : {{pool_remaining}} {{unit_label}}
        Billing Period: {{billing_period}}
        Plan Tier    : {{plan_tier}}

      At this rate, your {{channel_name}} allocation will be fully consumed
      on approximately {{estimated_exhaustion_date}}.

      What happens at 100%:
        {{suspension_behavior_description_for_channel}}

      To continue uninterrupted service, consider:
        → Upgrading your plan: {{upgrade_link}}
        → Reviewing usage patterns in your dashboard: {{dashboard_link}}

      This is an automated alert from Ecoskiller Antigravity.
      Platform: {{platform_url}}

    Channels   : EMAIL + IN_APP
    Variables  : [tenant_name, recipient_name, channel_name, consumption_pct,
                 pool_consumed, pool_total, unit_label, pool_remaining,
                 billing_period, plan_tier, estimated_exhaustion_date,
                 suspension_behavior_description_for_channel, upgrade_link, dashboard_link]

  TEMPLATE: CRITICAL_95_CHANNEL
    ID         : HUAA_CRIT_CHANNEL_v1
    Subject    : "🚨 CRITICAL: {{channel_name}} at {{consumption_pct}}% — Action Required"
    Body_EN    : |
      URGENT: Your {{channel_name}} allocation is almost exhausted.

      {{consumption_pct}}% consumed — only {{pool_remaining}} {{unit_label}} remaining
      out of {{pool_total}} {{unit_label}} for {{billing_period}}.

      {{channel_name}} will be automatically suspended when 100% is reached.
      {{suspension_behavior_description_for_channel}}

      Immediate Options:
        1. Upgrade your plan now: {{upgrade_link}} (instant activation)
        2. Review and reduce usage: {{dashboard_link}}
        3. Contact support: {{support_link}}

      Estimated exhaustion: {{estimated_exhaustion_date}} at current consumption rate.

    Channels   : EMAIL + IN_APP + PUSH

  TEMPLATE: CEILING_HIT_SUSPENSION
    ID         : HUAA_CEIL_SUSPEND_v1
    Subject    : "🔴 {{channel_name}} SUSPENDED — Monthly Limit Reached"
    Body_EN    : |
      {{channel_name}} has been suspended for your {{plan_tier}} plan account.

      Your monthly allocation of {{pool_total}} {{unit_label}} has been fully consumed.

      Suspended as of: {{suspension_timestamp}}
      Billing Period  : {{billing_period}}

      What is still available:
        {{exempt_channels_description}}

      What is suspended:
        {{suspended_channels_description}}

      To restore service immediately:
        → Upgrade your plan: {{upgrade_link}}
        → Contact billing: {{billing_contact}}

      Your service will automatically restore at the start of the next billing
      period ({{next_period_start}}) if you remain on your current plan.

    Channels   : EMAIL + IN_APP + PUSH (+ SMS_OTP_FALLBACK for PROFESSIONAL)

  TEMPLATE: CEILING_HIT_ENTERPRISE_SOFT
    ID         : HUAA_CEIL_ENT_SOFT_v1
    Subject    : "📊 Contract Usage Ceiling Reached — {{tenant_name}}"
    Body_EN    : |
      Your usage of {{channel_name}} has reached the contracted monthly allocation.

      Per your Enterprise SLA agreement, service continues uninterrupted.
      Your dedicated account manager {{account_manager_name}} has been notified.

      Current Status:
        Consumed   : {{pool_consumed}} {{unit_label}} ({{consumption_pct}}% of contract)
        Contract   : {{pool_total}} {{unit_label}} for {{billing_period}}
        Overage    : {{overage_units}} {{unit_label}} (billed per contract terms)

      Your account manager will contact you within {{account_manager_sla}} business hours
      to discuss contract amendment options if needed.

    Channels   : EMAIL + IN_APP + PUSH + SMS (Enterprise)

  TEMPLATE: AGGREGATE_COST_CEILING_WARNING
    ID         : HUAA_AGG_WARN_v1
    Subject    : "⚠️ Total Monthly Spend at {{consumption_pct}}% of Budget — {{tenant_name}}"
    Body_EN    : |
      Your total platform spending across all channels has reached {{consumption_pct}}%
      of your monthly plan budget.

      Aggregate Spending:
        Spent this month : ₹{{aggregate_consumed_inr}}
        Monthly budget   : ₹{{monthly_cost_ceiling_inr}}
        Remaining        : ₹{{remaining_inr}}
        Billing period   : {{billing_period}}

      Top spending channels this period:
        {{top_channels_breakdown}}

      {{call_to_action}}

    Channels   : EMAIL + IN_APP

TEMPLATE_VARIABLES_RESOLUTION:
  estimated_exhaustion_date : Computed from current 7-day rolling consumption rate
                              Formula: days_remaining = pool_remaining / avg_daily_consumption
                              remaining_date = today + days_remaining
                              IF avg_daily_consumption == 0: "at current rate, no exhaustion"
  suspension_behavior_description: Loaded from FEATURE_GATE_MAP suspension descriptions
  exempt_channels_description: Generated from FEATURE_GATE_MAP exempt channels for plan tier
  top_channels_breakdown      : Top 3 channels by cost contribution this period

TEMPLATE_RENDERING_SECURITY:
  Template variable injection: sanitize all interpolated values (strip HTML, no script injection)
  Recipient name: loaded from platform user profile only — not from tenant-provided input
  Links: all links are platform-generated (upgrade_link, dashboard_link) — no external redirect injection
```

---

## 8️⃣ BILLING PERIOD LIFECYCLE MANAGEMENT

```yaml
BILLING_PERIOD_LIFECYCLE:

  PERIOD_START_ACTIONS (triggered at 00:00:01 UTC of billing period start):
    1. Reset all pool balances in Redis for all active tenants
       (Atomic: SET pool_balance_{tenant}_{channel} = pool_total with new billing_period key)
    2. Clear all threshold_state_key entries for prior period
    3. Clear all hysteresis keys (new period = fresh alert slate)
    4. Emit BILLING_PERIOD_RESET_COMPLETE → CALL_COST_CALCULATION_AGENT
    5. Verify Unleash gates: all suspended gates from prior period RESTORED at period start
       (Tenant who hit ceiling last month gets fresh start — gates restored automatically)
    6. Log PERIOD_START audit record (immutable)
    7. Send "Fresh Start" notification to tenants who were suspended in prior period

  PERIOD_END_ACTIONS (triggered at 23:59:00 UTC of billing period last day):
    1. Compute final period summary for all active tenants
    2. Emit PERIOD_END_USAGE_SUMMARY → BILLING_GOVERNANCE_AGENT (for invoice generation)
    3. Compute overage charges for BASIC/PROFESSIONAL tenants
    4. Emit OVERAGE_CHARGES → BILLING_GOVERNANCE_AGENT
    5. Emit USAGE_PATTERN_SUMMARY → FEATURE_STORE_SERVICE (ML feature update)
    6. Archive period usage data to ClickHouse (immutable period snapshot)
    7. Log PERIOD_END audit record

  MID_PERIOD_PLAN_UPGRADE (tenant upgrades plan during active period):
    1. Receive PLAN_CHANGED event from Billing & Subscription Service
    2. Update pool_total for all channels to new plan's pool sizes
    3. Re-evaluate all thresholds against new pool sizes
    4. If prior suspended gate now within ceiling: RESTORE gate via Unleash
    5. Send "Plan Upgraded" confirmation to Tenant Admin with new limits
    6. Log PLAN_UPGRADE_MID_PERIOD audit record

  MID_PERIOD_PLAN_DOWNGRADE:
    1. Receive PLAN_CHANGED event (downgrade)
    2. Update pool_total to new plan's smaller pools
    3. Re-evaluate thresholds — if current consumption exceeds new pool: immediate ceiling
    4. If immediate ceiling triggered: enforce suspension as per normal ceiling protocol
    5. Send "Plan Downgraded — Usage Review Required" alert to Tenant Admin
    6. Log PLAN_DOWNGRADE_MID_PERIOD audit record

BILLING_PERIOD_RESET_GATE_RESTORATION:
  Policy: Suspended gates are AUTOMATICALLY RESTORED at billing period reset
  Exception: FORCE_SUSPENSION (fraud response) — NOT auto-restored
             Requires PLATFORM_ADMIN manual lift with payment_confirmation_id
  Restoration SLA: < 60 seconds from period boundary
  Restoration audit: logged in immutable audit trail + Unleash audit
```

---

## 9️⃣ SCALABILITY DESIGN

```yaml
THROUGHPUT_REQUIREMENTS:
  Cost events processed/second: 50,000 (at 100M user peak)
  Tenants monitored           : Up to 1,000,000 active tenants
  Threshold evaluations/second: 50,000 (one per cost event)
  Unleash gate updates/minute : Up to 10,000 (ceiling enforcement bursts)
  Alert dispatches/minute     : Up to 100,000

ARCHITECTURE:
  Event processing  : Kafka consumer group (partitioned by tenant_id for ordered processing)
                      Each partition processed by one consumer instance (ordering guarantee)
  State management  : Redis Cluster (pool balances, threshold states, hysteresis keys)
                      Sharded by tenant_id hash
  Batch evaluation  : Apache Airflow DAG (5-minute sweep + period boundary jobs)
  Alert dispatch    : Async Kafka producer → Notification Service
                      (Does NOT block threshold evaluation — fire and observe)
  Unleash updates   : Direct HTTP to Unleash Admin API with retry logic
                      Parallel updates for multi-channel ceiling events

LATENCY_TARGETS:
  Threshold evaluation     : < 10ms P99 (event-driven path)
  Unleash gate update      : < 30s from breach (SLA) — target < 5s actual
  Alert dispatch submission: < 100ms P99 (async — submission to Notification Service)
  Batch sweep completion   : < 120 seconds for full tenant sweep (5-minute Airflow cadence)

IDEMPOTENCY:
  event_id dedup: Redis SETNX with 60s TTL — same event processed only once
  Threshold level transitions: persisted in Redis — re-processing same event returns same result
  Gate updates: Unleash API is idempotent — duplicate updates safe
  Alert dispatch: alert_id dedup — same alert_id never dispatched twice

REDIS_DATA_MODEL:
  threshold_state:{tenant_id}:{channel}:{billing_period}   → ENUM level, TTL = period_end + 7d
  hysteresis:{tenant_id}:{channel}:{level}                 → timestamp, TTL = 24h
  pool_balance:{tenant_id}:{channel}:{billing_period}      → integer (maintained by CALL_COST)
  dedup:{event_id}                                         → processed flag, TTL = 60s
  suspension_state:{tenant_id}:{channel}                   → boolean, TTL = period_end + 7d

HORIZONTAL_SCALING:
  Consumer pods       : 3 minimum, 100 maximum (HPA on Kafka consumer lag)
  Batch evaluation    : Airflow worker pool (isolated from real-time consumers)
  Alert dispatch      : Separate worker pool (Notification Service handles delivery scale)
  Redis Cluster       : Minimum 6 shards (3 primary + 3 replica) — auto-failover

KAFKA_CONSUMER_CONFIGURATION:
  Topic              : cost.events.raw (consumed) + cost.threshold.alerts (produced)
  Consumer_group     : high-usage-alert-agent-cg
  Partition_count    : 100 (matches peak tenant concurrency)
  Offset_commit      : After successful processing + Redis state update (at-least-once)
  Retry_policy       : 3 retries with 500ms backoff, then dead-letter
  Dead_letter_topic  : cost.alert.failed
```

---

## 🔟 SECURITY ENFORCEMENT

```yaml
SECURITY_CONTROLS:

  TENANT_ISOLATION:
    All pool balance reads include tenant_id key (Redis key includes tenant_id)
    All threshold evaluations scoped to single tenant_id
    No cross-tenant usage aggregation in any query
    Kafka partition key = tenant_id (consumer per partition = natural tenant isolation)

  FEATURE_GATE_AUTHORITY:
    Unleash Admin API called only by this agent for tenant-level suspensions
    Human PLATFORM_ADMIN can override via Manual Override Request (Section 4.3)
    All Unleash mutations logged in: (a) this agent's audit trail, (b) Unleash native audit
    Unauthorized Unleash calls (not from this agent or PLATFORM_ADMIN): SECURITY_ALERT

  ALERT_CONTENT_SECURITY:
    Template variable injection: all interpolated values HTML-escaped before rendering
    No raw user-input content in alert bodies (all from platform data registry only)
    Recipient identity: loaded from platform registry, not from request payload
    Upgrade links: platform-generated, HTTPS only, no query string injection

  OVERRIDE_SECURITY:
    Manual overrides require PLATFORM_ADMIN role JWT (validated with Auth Service)
    EXTEND_CEILING overrides: dual approval (PLATFORM_ADMIN + BILLING_GOVERNANCE)
    All overrides: immutable audit record + immediate AUDIT_COMPLIANCE_AGENT notification
    Override_reason field: required and logged — no unexplained ceiling extensions

  ROLE_BASED_ACCESS:
    PLATFORM_ADMIN   : Full read + manual override + force suspension
    BILLING_GOVERNANCE: Read threshold events + upgrade trigger (no gate control)
    TENANT_ADMIN     : Read own tenant threshold status + alert history
    OBSERVABILITY    : Read metrics and aggregate health dashboards
    STUDENTS/PARENTS : NO access to billing or threshold data

  AUDIT_INTEGRITY:
    All audit records: append-only PostgreSQL + MinIO WORM backup
    Wazuh SIEM: monitors for threshold record tampering
    Alert records: cryptographic hash (SHA-256) of tenant_id + channel + threshold + timestamp
    Retention: threshold audit = 7 years; fraud signals = 15 years
```

---

## 1️⃣1️⃣ AUDIT & TRACEABILITY

Every threshold evaluation and enforcement action emits the following immutable audit record:

```json
THRESHOLD_AUDIT_LOG: {
  "audit_id"                      : "UUID v4",
  "timestamp_utc"                 : "ISO8601",
  "actor_id"                      : "HIGH_USAGE_ALERT_AGENT (or PLATFORM_ADMIN user_id for overrides)",
  "tenant_id"                     : "string",
  "billing_period"                : "YYYY-MM",
  "channel_type"                  : "ENUM",
  "plan_tier"                     : "ENUM",

  "pool_total"                    : "integer",
  "pool_consumed"                 : "integer",
  "pool_remaining"                : "integer",
  "consumption_percentage"        : "decimal(6,4)",

  "threshold_level_prior"         : "ENUM",
  "threshold_level_current"       : "ENUM",
  "threshold_newly_crossed"       : "boolean",
  "alert_suppressed_by_hysteresis": "boolean",

  "feature_gate_action"           : "ENUM",
  "unleash_flag_name"             : "string or null",
  "unleash_update_status"         : "ENUM or null",
  "enforcement_sla_met"           : "boolean",
  "enforcement_latency_ms"        : "integer or null",

  "alerts_dispatched_count"       : "integer",
  "alerts_failed_count"           : "integer",
  "fraud_signal_emitted"          : "boolean",
  "fraud_signal_type"             : "ENUM or null",

  "triggering_event_id"           : "UUID — links to cost event",
  "override_id"                   : "UUID or null — populated if manual override",

  "model_version"                 : "ALERT_MODEL_v1.0.0",
  "confidence_score"              : "1.0",
  "record_hash"                   : "SHA-256 of core fields",
  "immutable"                     : true
}
```

---

## 1️⃣2️⃣ FAILURE POLICY

```yaml
FAILURE_SCENARIOS:

  COST_EVENT_VALIDATION_FAILURE:
    ACTION         : REJECT event, log rejection with reason
    EMIT           : validation.failure.event → OBSERVABILITY_AGENT
    DEAD_LETTER    : Event pushed to cost.alert.failed Kafka topic
    RETRY          : NOT permitted — CALL_COST_CALCULATION_AGENT must resubmit corrected
    THRESHOLD_EVAL : NOT performed on invalid events

  REDIS_UNAVAILABLE (pool state unreachable):
    ACTION         : HALT real-time threshold evaluation
    LOG            : LOG_INCIDENT
    ESCALATE_TO    : OBSERVABILITY_AGENT + PLATFORM_ADMIN (SEV-2)
    RETRY_POLICY   : Attempt Redis reconnect every 5 seconds
    FALLBACK       : Switch to PostgreSQL read for pool balances (slower, always available)
                     Log FALLBACK_MODE_ACTIVE with latency impact warning
    RESUME         : Resume Redis-backed evaluation on reconnect

  UNLEASH_API_UNAVAILABLE (feature gate enforcement blocked):
    ACTION         : This is SEV-1 — ceiling enforcement cannot complete
    LOG            : LOG_INCIDENT with UNLEASH_UNREACHABLE + enforcement_sla_met = false
    ESCALATE_TO    : PLATFORM_ADMIN + ADMIN_GOVERNANCE_SERVICE (immediate, SEV-1)
    RETRY_POLICY   : 3 retries × 5s exponential backoff
    POST_RETRY     : Alert PLATFORM_ADMIN to manually enforce gate via Unleash UI
    AUDIT          : Record enforcement failure with full evidence chain
    NEVER          : Treat Unleash failure as "gate already suspended" — assume safe

  NOTIFICATION_SERVICE_UNREACHABLE (alert dispatch blocked):
    ACTION         : LOG_INCIDENT — enforcement (gate update) continues regardless
    RETRY_POLICY   : 3 retries with 30s backoff for alert dispatch
    FALLBACK       : Emit to alert.dispatch.failed Kafka topic for retry by Notification Service
    ENFORCEMENT    : NEVER blocked by alert dispatch failure
    SLA_NOTE       : Gate enforcement SLA is independent of alert delivery

  BILLING_SERVICE_UNREACHABLE (plan entitlement not retrievable):
    ACTION         : HALT threshold evaluation for affected tenant
    LOG            : LOG_INCIDENT
    RETRY_POLICY   : 3 retries × 10s backoff
    FALLBACK       : Use last-known-good plan from Redis plan cache (TTL = 1 hour)
    ESCALATE_TO    : OBSERVABILITY_AGENT if unreachable > 5 minutes

  POOL_BALANCE_INCONSISTENCY (Redis balance < 0 or > pool_total):
    ACTION         : LOG_INCIDENT with POOL_INCONSISTENCY flag
    ESCALATE_TO    : CALL_COST_CALCULATION_AGENT + DATA_GOVERNANCE_AGENT
    ENFORCEMENT    : Use conservative value (treat as fully consumed if < 0)
    HUMAN_REVIEW   : Flag for PLATFORM_ADMIN review — likely Redis write anomaly

  FRAUD_PATTERN_DETECTION_FAILURE:
    ACTION         : LOG_INCIDENT — continue normal threshold enforcement
    NOTE           : Fraud detection is advisory — its failure does not block enforcement
    ESCALATE_TO    : OBSERVABILITY_AGENT

  MANUAL_OVERRIDE_VALIDATION_FAILURE:
    ACTION         : REJECT override request, return error to requestor
    LOG            : LOG_INCIDENT with override_id + rejection_reason
    NOTIFY         : PLATFORM_ADMIN of failed override attempt (security signal)

GLOBAL_FAILURE_RULES:
  - NO silent failures — every failure emits structured Kafka event
  - Feature gate enforcement is NEVER blocked by auxiliary service failures
  - Alert dispatch failures do NOT constitute enforcement failure
  - Every failure path emits to dead-letter Kafka topic
  - SLA breach on enforcement: automatic SEV-1 escalation to PLATFORM_ADMIN
```

---

## 1️⃣3️⃣ INTER-AGENT DEPENDENCY MAP

```yaml
KAFKA_TOPICS_CONSUMED:
  - cost.events.raw               (primary: per-channel cost events from CALL_COST)
  - billing.plan.changed          (tenant plan upgrade/downgrade events)
  - billing.period.boundary       (period start/end triggers)
  - reconciliation.override.event (corrected unit counts from TELECOM_RECONCILIATION)

KAFKA_TOPICS_PRODUCED:
  - cost.threshold.alerts         (threshold breach events — consumed by BILLING_GOVERNANCE)
  - feature.gate.updated          (Unleash gate state changes — consumed by AUDIT_COMPLIANCE)
  - alert.dispatch.instructions   (consumed by Notification Service)
  - fraud.consumption.signal      (consumed by FRAUD_DETECTION_ENGINE)
  - platform.health.usage.summary (consumed by OBSERVABILITY_AGENT + FEATURE_STORE)
  - cost.alert.failed             (dead-letter — consumed by ops monitoring)
  - crm.upgrade.trigger           (consumed by CRM_SERVICE)

SYNCHRONOUS_DEPENDENCIES:
  Unleash Admin API    : PATCH/GET flag state (ceiling enforcement — blocking)
  Auth Service         : JWT validation + tenant/recipient resolution (blocking)
  Billing Service      : Plan entitlement query (blocking, with Redis cache fallback)
  Redis Cluster        : Pool state read/write (blocking, with PostgreSQL fallback)

ASYNC_DEPENDENCIES:
  Notification Service : Alert dispatch (non-blocking — fire and observe)
  ClickHouse           : Historical consumption query (batch evaluation only)
  PostgreSQL           : Audit record write (async batch flush)

INTER_AGENT_PROTOCOL:
  This agent consumes from CALL_COST_CALCULATION_AGENT — it does NOT call CALL_COST directly
  All enforcement state changes are broadcast via Kafka — pull model for downstream agents
  BILLING_GOVERNANCE_AGENT subscribes to cost.threshold.alerts independently
  No synchronous agent-to-agent HTTP calls except to Unleash and Auth Service
```

---

## 1️⃣4️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

```yaml
FEATURE_EMISSION_TO_FEATURE_STORE:
  After each billing period close, this agent emits per-tenant usage intensity vectors:

  FEATURE_VECTOR_SCHEMA:
    {
      tenant_id                           : string,
      billing_period                      : YYYY-MM,
      feature_name_1  : "usage_intensity_index",
      feature_value_1 : decimal — avg consumption % across all channels this period,
      feature_name_2  : "ceiling_hit_channel_count",
      feature_value_2 : integer — number of channels that hit ceiling this period,
      feature_name_3  : "alert_response_rate",
      feature_value_3 : decimal — % of warnings where tenant took upgrade action within 48h,
      feature_name_4  : "pool_drain_velocity_index",
      feature_value_4 : decimal — how quickly pools drain relative to period length,
      feature_name_5  : "upgrade_propensity_signal",
      feature_value_5 : decimal — composite: ceiling hits + critical alerts + response time,
      timestamp       : ISO8601,
      source_agent    : "HIGH_USAGE_ALERT_AGENT"
    }

INTELLIGENCE_USE_CASES:
  - CAREER_PREDICTION_AGENT: High GD session usage = high engagement = better outcome predictor
  - SCHOOL_GROWTH_FORECAST_AGENT: Ceiling hit frequency predicts school growth trajectory
  - UNIT_ECONOMICS_ENGINE_AGENT: Usage intensity feeds ARPU and LTV models
  - CRM_SERVICE: upgrade_propensity_signal drives outreach timing and offer personalization

PRIVACY_GUARANTEE:
  Feature vectors: tenant-level aggregates only — no individual user communication patterns
  No message content, no personal communication history in any feature vector
```

---

## 1️⃣5️⃣ OBSERVABILITY & PERFORMANCE MONITORING

```yaml
METRICS_EMITTED_TO_PROMETHEUS:
  - threshold_evaluations_total          (counter, by level crossed, channel, plan_tier)
  - threshold_level_distribution         (gauge: current tenant count at each level)
  - feature_gate_updates_total           (counter, by channel, status)
  - feature_gate_enforcement_latency_ms  (histogram P50/P95/P99 — SLA tracking)
  - enforcement_sla_breaches_total       (counter — critical operational metric)
  - alerts_dispatched_total              (counter, by level, channel, recipient_role)
  - alerts_failed_total                  (counter, by reason)
  - fraud_signals_emitted_total          (counter, by signal_type)
  - tenants_at_ceiling_active            (gauge — how many tenants currently suspended)
  - tenants_at_critical_active           (gauge — how many tenants at 95%)
  - overage_accumulated_inr_total        (gauge, by plan_tier — financial exposure metric)
  - evaluation_latency_ms                (histogram P50/P95/P99)
  - redis_pool_read_latency_ms           (histogram)
  - unleash_api_latency_ms               (histogram)

GRAFANA_DASHBOARDS:
  - Real-time tenant threshold status (heatmap: all tenants × all channels × threshold level)
  - Feature gate suspension timeline (which tenants suspended, when, which channels)
  - Enforcement SLA compliance (30s target — % met over rolling 7 days)
  - Alert delivery success rate (dispatched vs delivered by channel)
  - Fraud signal log (pattern × tenant × timestamp)
  - Plan tier distribution (usage intensity by tier — upgrade opportunity identification)
  - Monthly overage accumulation by tenant and plan tier
  - Pool drain velocity forecast (which tenants will hit ceiling before month end)

ALERTING_RULES:
  - enforcement_sla_breaches_total > 0 in 5min: CRITICAL — PagerDuty SEV-1
  - unleash_api_latency_ms P99 > 10,000: WARNING — Unleash degradation
  - tenants_at_ceiling_active > 1000: WARNING — mass ceiling event (platform-wide issue?)
  - alerts_failed_total > 1% of dispatched in 10min: WARNING — Notification Service issue
  - evaluation_latency_ms P99 > 50ms: WARNING — consumer lag building
  - redis_pool_read_latency_ms P99 > 20ms: WARNING — Redis pressure
  - fraud_signals_emitted_total > 100/hour: CRITICAL — potential platform abuse wave

INTEGRATION:
  Prometheus      : scrape every 15 seconds
  Loki            : structured JSON logs per evaluation
  OpenTelemetry   : trace ID propagated from triggering cost event through gate update
  PagerDuty       : SEV-1 for enforcement SLA breaches + Unleash unavailability
  Wazuh SIEM      : audit log integrity monitoring + unauthorized gate update detection
```

---

## 1️⃣6️⃣ VERSIONING POLICY

```yaml
CURRENT_VERSION          : ALERT_MODEL_v1.0.0
MUTATION_POLICY          : Add-only — new thresholds or channels added as new version
                           Existing thresholds immutable once published

WHAT_REQUIRES_VERSION_BUMP:
  - Any change to threshold percentages (80%, 95%, 100%)
  - Any change to enforcement SLA (currently 30 seconds)
  - Any change to plan tier pool sizes
  - Any change to feature gate suspension map (which channels are exempt)
  - Any change to alert routing table (who gets alerted at which level)
  - Any change to fraud pattern detection triggers

WHAT_DOES_NOT_REQUIRE_VERSION_BUMP:
  - Alert template content updates (wording, formatting) — template versioned separately
  - New tenant onboarding (plan registry add-only)
  - Grafana dashboard changes

PLAN_REGISTRY_VERSIONING:
  PLAN_REGISTRY_v1.0 is current
  New plans: add-only (PLAN_REGISTRY_v1.1+)
  Existing plan parameter changes: new version required
  Historical evaluations auditable against plan version active at evaluation time

BACKWARD_COMPATIBILITY:
  All audit records include model_version
  Historical threshold records auditable against the version that produced them
  No retroactive re-evaluation of prior period thresholds

SCHEMA_MIGRATION:
  New fields: optional with null-safe defaults
  No field removal or rename
  Flyway manages PostgreSQL audit schema versions
```

---

## 1️⃣7️⃣ NON-NEGOTIABLE RULES

```yaml
THIS AGENT MUST NOT:

  ✗ Skip threshold evaluation for any cost event regardless of plan tier or channel type
  ✗ Delay hard ceiling enforcement beyond 30 seconds for FREE/BASIC/PROFESSIONAL tenants
  ✗ Auto-suspend an ENTERPRISE tenant — soft ceiling only (alert + escalation)
  ✗ Suppress a CEILING_HIT alert due to hysteresis — ceiling alerts are never suppressed
  ✗ Suspend OTP SMS for any tenant under any circumstance — OTP is always exempt
  ✗ Suspend account security emails or password reset emails — always exempt
  ✗ Use AI/LLM for threshold computation, gate enforcement, or fraud classification
  ✗ Allow cross-tenant pool data to appear in any single evaluation context
  ✗ Produce a threshold evaluation result without an audit_reference UUID
  ✗ Perform a Unleash gate update without immediately recording it in audit trail
  ✗ Process a manual override without dual approval (PLATFORM_ADMIN + BILLING_GOVERNANCE)
     — except FORCE_SUSPENSION (fraud response — single approval permitted)
  ✗ Treat Unleash unavailability as "gate is already suspended" — must halt and escalate
  ✗ Restore a FORCE_SUSPENSION gate without payment_confirmation_id
  ✗ Emit billing_unit_count = non-zero for events in a suspended channel
  ✗ Delete or modify any audit record — append-only is absolute
  ✗ Allow alert dispatch failure to block gate enforcement
  ✗ Allow pool_balance to go below 0 without triggering ceiling evaluation
  ✗ Ignore a reconciliation override event from TELECOM_RECONCILIATION_AGENT —
     corrected counts must trigger threshold re-evaluation
  ✗ Evaluate thresholds without first validating tenant_id against Auth Service
  ✗ Emit upgrade CRM triggers for ENTERPRISE tenants without Account Manager review
  ✗ Begin a new billing period without first confirming all prior-period suspended gates
     have been restored (gate restoration is mandatory at period boundary)
```

---

## 1️⃣8️⃣ DEPLOYMENT CONTEXT

```yaml
KUBERNETES_NAMESPACE       : billing
SERVICE_IDENTITY           : high-usage-alert-agent
RESOURCE_PROFILE:
  CPU_REQUEST              : 500m
  CPU_LIMIT                : 2000m
  MEMORY_REQUEST           : 512Mi
  MEMORY_LIMIT             : 2Gi
  REPLICAS_MIN             : 3 (HA — alert agent is availability-critical)
  REPLICAS_MAX             : 100 (HPA on Kafka consumer lag)
  HPA_METRIC               : kafka_consumer_lag (custom Prometheus metric)
  HPA_SCALE_UP_THRESHOLD   : consumer_lag > 10,000 events
  HPA_SCALE_DOWN_COOLDOWN  : 10 minutes (stability during alert bursts)

STARTUP_SEQUENCE:
  1. Connect to Redis Cluster (pool state store)
  2. Load plan tier registry into memory
  3. Load feature gate map into memory
  4. Load alert template registry into memory
  5. Connect to Kafka consumer group (cost.events.raw)
  6. Connect to Unleash Admin API (verify connectivity + auth)
  7. Validate Auth Service JWT signing key (for override request validation)
  8. Mark READY (readiness probe passes)
  Total startup target: < 15 seconds

HEALTH_CHECKS:
  LIVENESS   : /health/live (200 = process running)
  READINESS  : /health/ready (200 = Redis connected + Kafka consumer active
               + Unleash reachable + plan registry loaded)
  STARTUP    : 20s grace period

GRACEFUL_SHUTDOWN:
  - Drain in-flight cost event evaluations (max 10s drain window)
  - Commit Kafka consumer offsets for processed events
  - Flush pending audit log writes (PostgreSQL + MinIO)
  - Flush pending alert dispatch submissions to Notification Service
  - Emit agent.shutdown event to OBSERVABILITY_AGENT
  - Shutdown timeout: 30 seconds

CI_CD_PIPELINE:
  - GitHub Actions: build → unit test → integration test → security scan → image push
  - Helm chart: blue/green deployment, automated rollback on health check failure
  - Threshold percentage changes require 2-reviewer PR approval + test suite pass
  - Unleash flag name convention changes require PLATFORM_ADMIN approval
  - No manual production deployments
```

---

## 1️⃣9️⃣ TESTING CONTRACT

```yaml
REQUIRED_TEST_COVERAGE     : 95% minimum (financial enforcement agent — enforced in CI)

UNIT_TESTS — THRESHOLD MATH:
  - 0% consumed → level NORMAL
  - Exactly 50% → EARLY_50
  - 50.0001% → EARLY_50 (boundary)
  - Exactly 80% → WARNING (boundary)
  - 79.9999% → EARLY_70 (not yet WARNING)
  - Exactly 95% → CRITICAL (boundary)
  - Exactly 100% → CEILING_HIT (boundary)
  - 100.0001% → CEILING_HIT (overage case)
  - pool_total = 0 → consumed_pct = 100 (zero-pool rule)
  - FREE tier: EARLY_50 and EARLY_70 suppressed correctly
  - ENTERPRISE: CEILING_HIT → SOFT alert only (no gate suspension)

UNIT_TESTS — FEATURE GATE SUSPENSION:
  - GD_VOICE ceiling → tenant_{id}_gd_voice_enabled set false
  - SMS_OTP ceiling → NO gate suspension (safety exempt)
  - EMAIL_TRANSACTIONAL ceiling → security emails exempt, bulk blocked
  - AI_INFERENCE ceiling → non-critical purposes blocked, FRAUD_SIGNAL + ADMIN_GOVERNANCE exempt
  - TURN_RELAY ceiling → new relay blocked, active relay in-progress allowed
  - API_EGRESS ceiling → throttled to 10% (not full block)
  - Unleash API failure → halt + escalate (not "assume suspended")

UNIT_TESTS — ALERT ROUTING:
  - FREE tier WARNING_80 → Tenant Admin only (IN_APP + EMAIL)
  - PROFESSIONAL CRITICAL_95 → Tenant Admin + School Principal (if institute) + Billing Governance
  - ENTERPRISE CEILING_100 → soft alert + Account Manager + Admin Governance (no gate)
  - Hysteresis suppression: same level within 24h → suppressed (except CEILING)
  - Hysteresis on CEILING → never suppressed (always alert)

UNIT_TESTS — FRAUD PATTERNS:
  - 1000 SMS in 60 seconds → MACHINE_SPEED_SMS_ABUSE signal
  - 50% pool drain in 24 hours → POOL_DRAIN_VELOCITY_ANOMALY signal
  - 3+ channels at 80% simultaneously within 5 minutes → COORDINATED_USAGE_SPIKE
  - 100 OTP sent post-ceiling → OTP_ABUSE_POST_CEILING signal
  - Normal usage → no fraud signal

UNIT_TESTS — BILLING PERIOD LIFECYCLE:
  - Period reset: all pool balances restored, threshold states cleared, gates restored
  - Mid-period upgrade: new pool sizes applied, thresholds re-evaluated
  - Mid-period downgrade: consumption vs new pool triggers ceiling if exceeded
  - FORCE_SUSPENSION gate not auto-restored at period reset (fraud lock)

INTEGRATION_TESTS:
  - Full event flow: cost.computed.event → threshold evaluation → Unleash gate update → alert dispatch
  - Dual-approval override: EXTEND_CEILING with and without BILLING_GOVERNANCE countersignature
  - Redis unavailability: fallback to PostgreSQL pool read → evaluation continues
  - Unleash unavailability: halt + SEV-1 escalation → no silent "gate assumed suspended"
  - Batch 5-minute sweep: all active tenants evaluated within 120s
  - Period boundary: gate restoration for all prior-period suspended tenants

SECURITY_TESTS:
  - Cross-tenant pool injection attempt → rejected
  - Manual override without PLATFORM_ADMIN JWT → rejected
  - EXTEND_CEILING without BILLING_GOVERNANCE countersignature → rejected
  - Unauthorized Unleash gate read → detected and logged

PERFORMANCE_TESTS:
  - 50,000 cost events/second → P99 evaluation latency < 10ms
  - 1,000,000 tenant sweep in 5-minute batch → completes within 120 seconds
  - Unleash gate update under 10,000 concurrent ceiling breaches → P99 < 5s (SLA < 30s)
  - Redis read latency under peak load → P99 < 5ms
  - Alert dispatch under 100,000 simultaneous alerts → no event loss

CHAOS_TESTS:
  - Redis unavailable: fallback activates, evaluations continue with degraded latency
  - Unleash unreachable: SEV-1 fires, no silent failure, enforcement halted and escalated
  - Kafka partition rebalance during peak: idempotency maintained, no duplicate gate suspensions
  - Notification Service down: gate enforcement proceeds, alerts queued for retry
  - Pod crash during gate update: recovery on restart, Unleash state verified and corrected
```

---

## 2️⃣0️⃣ AGENT SEAL DECLARATION

```
╔══════════════════════════════════════════════════════════════════════════════╗
║                             AGENT SEAL                                       ║
║                                                                              ║
║  AGENT   : HIGH_USAGE_ALERT_AGENT                                            ║
║  VERSION : ALERT_MODEL_v1.0.0                                                ║
║  STATUS  : SEALED · LOCKED · PRODUCTION-READY · FINANCIALLY GOVERNED        ║
║                                                                              ║
║  This agent specification is add-only from this point forward.              ║
║  No threshold percentage, enforcement SLA, suspension rule, alert routing   ║
║  table, plan tier pool size, or feature gate exemption may be removed or    ║
║  modified without:                                                           ║
║    (a) Version bump to ALERT_MODEL_v1.1.0 or higher                         ║
║    (b) PLATFORM_ADMIN written approval                                       ║
║    (c) Audit log entry for modification rationale                           ║
║    (d) BILLING_GOVERNANCE_AGENT sign-off on any change affecting            ║
║        ceiling enforcement, overage policy, or plan pool sizes              ║
║    (e) GEO_COMPLIANCE_AGENT sign-off on any change affecting feature        ║
║        gate exemptions for safety-critical channels (OTP, security email)   ║
║    (f) LEGAL_POLICY_AGENT review for any change to ENTERPRISE soft          ║
║        ceiling policy (contractual obligation modification)                 ║
║    (g) Migration documentation for backward compatibility                   ║
║                                                                              ║
║  The 30-second enforcement SLA is a financial commitment to tenants.        ║
║  Any enforcement delay beyond 30 seconds is a platform liability event.     ║
║                                                                              ║
║  The OTP SMS exemption from suspension is a safety-critical rule.           ║
║  It may not be removed or modified without security team sign-off.          ║
║                                                                              ║
║  Any execution that deviates from this specification is a                   ║
║  GOVERNANCE VIOLATION and must be halted and reported immediately.          ║
║                                                                              ║
║  SEALED BY : ECOSKILLER ANTIGRAVITY INTELLIGENCE & INNOVATION CORE          ║
║  PLATFORM  : ECOSKILLER ANTIGRAVITY                                          ║
║  ARCH TYPE : Enterprise SaaS · Zero-Trust · Multi-Tenant · Governed         ║
║  STACK LOCK: Unleash · Kafka · Redis · Prometheus · Grafana · Airflow       ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

*End of HIGH_USAGE_ALERT_AGENT.md — ALERT_MODEL_v1.0.0 — SEALED*
