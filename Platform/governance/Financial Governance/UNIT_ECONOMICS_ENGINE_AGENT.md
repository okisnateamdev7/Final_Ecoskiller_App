# 🔒 UNIT_ECONOMICS_ENGINE_AGENT
## ECOSKILLER ANTIGRAVITY — ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### Status: SEALED · LOCKED · GOVERNED · DETERMINISTIC
```
EXECUTION_MODE         = DETERMINISTIC + VALIDATED
MUTATION_POLICY        = ADD-ONLY VERSIONED
CREATIVE_INTERPRETATION = FORBIDDEN
ASSUMPTION_FILLING     = FORBIDDEN
DEFAULT_BEHAVIOR       = DENY
FAILURE_MODE           = STOP_EXECUTION → LOG_INCIDENT → ESCALATE
DOCUMENT_VERSION       = v1.0.0
CLASSIFICATION         = ENTERPRISE INTERNAL — RESTRICTED
```

---

## 1️⃣ AGENT IDENTITY

```yaml
AGENT_NAME        : UNIT_ECONOMICS_ENGINE_AGENT
SYSTEM_ROLE       : Real-time Unit Economics Computation, Trust-Weighted Financial
                    Intelligence, and Multi-Tenant Revenue Health Governance
PRIMARY_DOMAIN    : ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
EXECUTION_MODE    : Deterministic + Validated
DATA_SCOPE        : Financial Transactions · Subscription Ledger · Revenue Events ·
                    Cohort Behavioral Data · Tenant Cost Attribution · Royalty Flows
TENANT_SCOPE      : STRICT ISOLATION — No cross-tenant financial data permitted
FAILURE_POLICY    : HALT ON AMBIGUITY — No partial output, no silent failures
AGENT_CLASS       : ML-Primary (75%) + AI-Assisted (25%)
LAYER             : ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
ANTIGRAVITY_NODE  : UNIT_ECONOMICS_ENGINE
```

> ⚠️ This agent must NEVER assume missing specifications. Every undefined field is treated as an invalid input. Execution halts immediately.

---

## 2️⃣ PURPOSE DECLARATION

### Problem Solved
Ecoskiller Antigravity operates across 10M–100M users spanning multiple tenant types: **Institutes, Enterprises/SMEs, Recruiters, Trainers, Students, and Platform Admins**. Each tenant type generates, consumes, and destroys economic value differently. Without a sealed, compliant, per-tenant unit economics engine, the platform cannot:

- Price subscriptions rationally across tenant tiers
- Identify cohorts approaching churn before revenue is lost
- Compute real-time LTV to weight trust and priority scores
- Attribute platform costs fairly per tenant, domain, and feature
- Feed the Innovation Economy royalty engine with verified revenue signals
- Protect enterprise clients from cross-tenant financial data leakage

### What This Agent Solves
The `UNIT_ECONOMICS_ENGINE_AGENT` computes, validates, caches, and distributes **real-time and cohort-level unit economics** for every tenant, user segment, plan tier, domain track, and revenue stream on the Ecoskiller Antigravity platform. It operates as the **financial nervous system** of the ENTERPRISE OPTIMIZATION layer.

### Input Consumed
- Raw billing events from `BILLING_SERVICE`
- Subscription lifecycle events from `SUBSCRIPTION_STATE_MACHINE`
- User activity signals from `FEATURE_STORE_AGENT`
- Placement completion events from `JOB_PORTAL_ENGINE`
- Marketplace transaction events from `MARKETPLACE_SERVICE`
- Royalty trigger events from `ROYALTY_ENGINE`
- Infrastructure cost allocation feeds from `OBSERVABILITY_AGENT`
- Cohort enrollment data from `SKILL_DEVELOPMENT_ENGINE`
- Dojo session billing events from `DOJO_ENGINE`

### Output Produced
- Per-tenant unit economics snapshot (LTV, CAC, ARPU, Churn, MRR, ARR, Payback Period, Gross Margin)
- Per-cohort economic health vectors
- Trust-weighted revenue signals for `TRUST_INFRASTRUCTURE_LAYER`
- Churn risk scores for `RETENTION_TRIGGER_AGENT`
- Economic health events for `GROWTH_ENGINE`
- Feature profitability scores for `PRODUCT_GOVERNANCE_AGENT`
- Revenue attribution maps for `ROYALTY_ENGINE`
- Anomaly alerts for `OBSERVABILITY_AGENT`

### Downstream Agents Depending On This Agent
| Downstream Agent | Dependency |
|---|---|
| `TRUST_INFRASTRUCTURE_AGENT` | LTV score for trust weighting |
| `RETENTION_TRIGGER_AGENT` | Churn probability + LTV decay signal |
| `GROWTH_ENGINE_AGENT` | ARPU delta + cohort health |
| `ROYALTY_ENGINE` | Revenue attribution vector per idea/content |
| `PRODUCT_GOVERNANCE_AGENT` | Feature profitability + cost-per-activation |
| `RANK_ENGINE_AGENT` | Economic reliability score for recruiter/trainer ranking |
| `OBSERVABILITY_AGENT` | Anomaly + cost-drift alerts |
| `ADMIN_GOVERNANCE_AGENT` | Compliance financial health report |

### Upstream Agents Feeding This Agent
| Upstream Agent | Data Provided |
|---|---|
| `BILLING_SERVICE` | Raw payment events |
| `SUBSCRIPTION_STATE_MACHINE` | Plan lifecycle states |
| `FEATURE_STORE_AGENT` | User behavioral feature vectors |
| `JOB_PORTAL_ENGINE` | Placement revenue events |
| `MARKETPLACE_SERVICE` | Transaction records |
| `ROYALTY_ENGINE` | Outbound royalty cost records |
| `DOJO_ENGINE` | Session-based billing events |
| `OBSERVABILITY_AGENT` | Infrastructure cost-per-tenant metrics |

---

## 3️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA: {
  "required_fields": [
    "tenant_id",
    "event_type",
    "event_timestamp_utc",
    "source_agent",
    "payload_version",
    "actor_id",
    "domain_track",
    "tenant_type"
  ],
  "optional_fields": [
    "user_id",
    "plan_tier",
    "revenue_amount",
    "currency_code",
    "cohort_id",
    "session_id",
    "feature_id",
    "royalty_reference_id",
    "placement_id",
    "marketplace_item_id",
    "infrastructure_cost_usd"
  ],
  "validation_rules": [
    "tenant_id must be a valid UUID registered in TENANT_REGISTRY",
    "event_timestamp_utc must be ISO 8601 format, not future-dated, not older than 90 days",
    "event_type must match ALLOWED_EVENT_TYPE_ENUM",
    "revenue_amount if present must be positive DECIMAL(12,4), never negative",
    "currency_code must be ISO 4217 compliant",
    "domain_track must be in [Arts, Commerce, Science, Technology, Administration]",
    "tenant_type must be in [INSTITUTE, ENTERPRISE, SME, RECRUITER, TRAINER, INDIVIDUAL, PLATFORM]",
    "source_agent must match registered agent manifest in AGENT_REGISTRY",
    "payload_version must be a valid semver string"
  ],
  "security_checks": [
    "JWT bearer token of source agent must be valid and non-expired",
    "tenant_id in token must match tenant_id in payload",
    "Cross-tenant field injection detection: reject any payload referencing foreign tenant_ids",
    "actor_id must exist in USER_SERVICE registry for given tenant",
    "Rate limit: max 10,000 events per minute per tenant — excess triggers THROTTLE_FLAG"
  ],
  "domain_checks": [
    "domain_track must match actor_id's registered domain in USER_SERVICE",
    "Billing events for INSTITUTE domain must not contain ENTERPRISE plan tiers",
    "Royalty events require royalty_reference_id to be resolvable in ROYALTY_ENGINE"
  ]
}
```

### Input Rules — Non-Negotiable
- **No null tolerance** on required fields without explicit declared null policy per field
- **Reject malformed data immediately** — do not attempt auto-correction
- **Log all validation failures** to `AUDIT_LOG_SERVICE` before rejection
- **Dual-hash check**: Input payload hash must match header `X-Payload-Hash` (SHA-256)

### Allowed Event Type Enum
```
SUBSCRIPTION_CREATED
SUBSCRIPTION_RENEWED
SUBSCRIPTION_CANCELLED
SUBSCRIPTION_UPGRADED
SUBSCRIPTION_DOWNGRADED
PAYMENT_SUCCESS
PAYMENT_FAILED
PLACEMENT_COMPLETED
MARKETPLACE_TRANSACTION
ROYALTY_DISBURSEMENT
DOJO_SESSION_BILLED
REFUND_ISSUED
TRIAL_STARTED
TRIAL_CONVERTED
TRIAL_EXPIRED
INFRASTRUCTURE_COST_ALLOCATED
FEATURE_ACTIVATION_BILLED
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "tenant_id": "UUID",
    "computation_scope": "TENANT | COHORT | USER | PLATFORM",
    "snapshot_timestamp_utc": "ISO8601",
    "unit_economics": {
      "mrr_usd": "DECIMAL(12,4)",
      "arr_usd": "DECIMAL(12,4)",
      "arpu_usd": "DECIMAL(12,4)",
      "cac_usd": "DECIMAL(12,4)",
      "ltv_usd": "DECIMAL(12,4)",
      "ltv_cac_ratio": "DECIMAL(6,4)",
      "gross_margin_pct": "DECIMAL(5,4)",
      "payback_period_months": "DECIMAL(6,2)",
      "churn_rate_monthly_pct": "DECIMAL(5,4)",
      "net_revenue_retention_pct": "DECIMAL(5,4)",
      "expansion_revenue_usd": "DECIMAL(12,4)",
      "contraction_revenue_usd": "DECIMAL(12,4)",
      "new_mrr_usd": "DECIMAL(12,4)",
      "churned_mrr_usd": "DECIMAL(12,4)",
      "feature_profitability_map": {
        "feature_id": "DECIMAL(12,4)"
      },
      "domain_revenue_breakdown": {
        "Arts": "DECIMAL(12,4)",
        "Commerce": "DECIMAL(12,4)",
        "Science": "DECIMAL(12,4)",
        "Technology": "DECIMAL(12,4)",
        "Administration": "DECIMAL(12,4)"
      },
      "trust_economic_score": "DECIMAL(5,4)",
      "royalty_cost_usd": "DECIMAL(12,4)",
      "infrastructure_cost_usd": "DECIMAL(12,4)",
      "contribution_margin_usd": "DECIMAL(12,4)"
    },
    "churn_risk_cohorts": [
      {
        "cohort_id": "UUID",
        "churn_probability": "DECIMAL(4,4)",
        "ltv_at_risk_usd": "DECIMAL(12,4)"
      }
    ],
    "anomaly_flags": ["string"],
    "next_trigger_events": ["string"]
  },
  "confidence_score": "DECIMAL(4,4) — range 0.0000 to 1.0000",
  "model_version": "string — semver reference to active ML model",
  "audit_reference": "UUID — immutable log reference",
  "next_trigger_event": [
    "CHURN_RISK_DETECTED",
    "LTV_THRESHOLD_BREACH",
    "ANOMALY_DETECTED",
    "TRUST_SCORE_UPDATE",
    "GROWTH_ENGINE_SIGNAL",
    "RANK_UPDATE_EVENT"
  ]
}
```

### Output Guarantees
- All outputs **must include** `confidence_score`, `model_version`, and `audit_reference`
- Outputs with `confidence_score < 0.72` trigger `ESCALATE_TO_HUMAN_REVIEW` before downstream dispatch
- No partial outputs permitted — full schema or `STOP_EXECUTION`
- All monetary values returned in **USD** with `currency_conversion_reference` UUID attached

---

## 5️⃣ ML / AI LOGIC LAYER

### ML Layer (Primary — 75% of Computations)

```yaml
MODEL_TYPE:
  - Time-Series Forecasting (MRR, ARR, Churn trajectory)
  - Classification (Churn risk tier: LOW / MEDIUM / HIGH / CRITICAL)
  - Regression (LTV prediction, Payback period estimation)
  - Clustering (Cohort segmentation by economic behavior)

FEATURES_USED:
  Revenue Features:
    - days_since_last_payment
    - subscription_age_days
    - plan_tier_numeric
    - payment_failure_count_90d
    - upgrade_count_lifetime
    - downgrade_count_lifetime
    - refund_count_90d
    - expansion_revenue_delta_30d
  Behavioral Features (from FEATURE_STORE_AGENT):
    - dojo_session_count_30d
    - job_application_count_30d
    - skill_course_completion_rate
    - marketplace_transaction_count_30d
    - login_frequency_score
    - feature_activation_breadth_score
    - collaboration_intensity_score
  Contextual Features:
    - tenant_type_encoded
    - domain_track_encoded
    - cohort_age_days
    - user_count_in_tenant
    - industry_sector_encoded
    - geographic_region_encoded

TRAINING_FREQUENCY:
  Churn Classifier:          Weekly (Sunday 02:00 UTC)
  LTV Regression:            Monthly (1st of month, 01:00 UTC)
  MRR Time-Series:           Weekly
  Cohort Clustering:         Monthly
  Feature Profitability:     Monthly

DRIFT_DETECTION:
  - Monitor revenue distribution shift (PSI > 0.2 → ALERT)
  - Monitor churn model accuracy degradation (AUC drop > 3% → RETRAIN_FLAG)
  - Monitor LTV prediction MAPE (> 12% → RETRAIN_FLAG)
  - Monitor feature distribution (KL-divergence threshold = 0.15)

VERSION_CONTROL:
  - All models stored with immutable model_version (semver)
  - Model registry: MLflow (self-hosted)
  - Shadow mode testing: New model must run parallel for 7 days before promotion
  - Rollback: Immediate to previous model_version on RETRAIN_FLAG
```

### AI Layer (Assist — 25% of Operations)

```yaml
AI_USAGE_SCOPE:
  - Natural language explanation of unit economics anomalies
    (for Admin dashboards — READ ONLY, no decision authority)
  - Semantic parsing of unstructured refund reason notes
    → Classified into structured REFUND_REASON_CODE
  - Trust narrative generation per tenant economic health
    (displayed in Admin ERP — advisory only)
  - Anomaly description generation for OBSERVABILITY_AGENT alerts

AI_USAGE_PROHIBITED_FROM:
  - Making financial decisions
  - Approving or blocking payments
  - Modifying any financial record
  - Overriding ML churn classifications
  - Accessing raw PII fields

PROMPT_GOVERNANCE:
  - All prompts versioned in PROMPT_REGISTRY
  - Deterministic structure (temperature = 0, top_p = 1.0)
  - No creative interpretation permitted
  - Prompt version embedded in audit_reference output
  - Max token budget per invocation: 800 tokens
  - AI response must pass SCHEMA_VALIDATOR before use
```

> **AI must assist ML — never replace it.** The ML model produces the economic computation. AI produces only human-readable explanation for governance dashboards.

---

## 6️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS          : 8,000 events/sec (peak: 25,000 during billing cycles)
LATENCY_TARGET        : p50 < 80ms, p95 < 250ms, p99 < 600ms
MAX_CONCURRENCY       : 512 parallel compute workers per deployment pod
QUEUE_STRATEGY        : Redis Streams — consumer group per tenant_type
                        Dead Letter Queue: after 3 retry failures → DLQ with alert

HORIZONTAL_SCALING    : Kubernetes HPA — scale on CPU > 65% + queue depth > 5,000
STATELESS_EXECUTION   : YES — no in-memory state between executions
IDEMPOTENT_OPERATIONS : YES — event_id deduplication enforced (Redis SET NX, TTL 48h)
ASYNC_PROCESSING      : YES — all non-real-time cohort computations via async workers

CACHING_STRATEGY:
  - Per-tenant economic snapshot: Redis cache, TTL = 900 seconds
  - Churn risk score: Redis cache, TTL = 3600 seconds
  - Feature profitability map: Redis cache, TTL = 86400 seconds
  - Cache invalidation on any PAYMENT_SUCCESS or SUBSCRIPTION event

BATCH_JOBS:
  - Monthly ARR/MRR reconciliation: Nightly batch at 03:00 UTC
  - Cohort LTV re-computation: Weekly Sunday 04:00 UTC
  - Feature profitability map rebuild: Monthly 1st, 05:00 UTC
```

---

## 7️⃣ SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  - Every database query must include tenant_id WHERE clause (enforced at ORM layer)
  - Row-Level Security (RLS) enabled on all financial tables in PostgreSQL
  - No aggregate queries across tenant boundaries permitted
  - Tenant isolation verified on every request via TENANT_ISOLATION_MIDDLEWARE

DOMAIN_ISOLATION:
  - domain_track filter enforced on all domain-segmented computations
  - Cross-domain revenue aggregation requires explicit PLATFORM_ADMIN role
  - Domain leaks classified as SECURITY_FAILURE → immediate STOP_EXECUTION

ROLE-BASED AUTHORIZATION:
  ROLES_PERMITTED:
    - PLATFORM_ADMIN        → Full read + governance override
    - TENANT_ADMIN          → Read own tenant economics only
    - FINANCE_AGENT         → Read + export own tenant data
    - COMPLIANCE_AGENT      → Read audit trails + anomaly reports
    - ML_PIPELINE           → Write model outputs to result store
    - OBSERVABILITY_AGENT   → Read metrics + anomaly feed
  ROLES_FORBIDDEN_FROM_WRITE:
    - All non-ML_PIPELINE roles

ENCRYPTION:
  - Data at rest: AES-256 (PostgreSQL TDE via pgcrypto)
  - Data in transit: TLS 1.3 minimum — TLS 1.2 rejected
  - Model artefacts: Encrypted at rest in MinIO (SSE-KMS)
  - All secrets via HashiCorp Vault — no env-variable secrets in code

AUDIT_LOGGING:
  - All reads of tenant financial data logged to AUDIT_LOG_SERVICE
  - Append-only: no UPDATE or DELETE permitted on audit tables
  - Log access pattern: actor_id, tenant_id, data_scope, timestamp, action
```

---

## 8️⃣ AUDIT & TRACEABILITY

Every execution logs the following immutable record to `AUDIT_LOG_SERVICE`:

```json
{
  "audit_id"          : "UUID — globally unique, immutable",
  "timestamp_utc"     : "ISO 8601",
  "actor_id"          : "UUID of calling agent or human actor",
  "tenant_id"         : "UUID",
  "domain_track"      : "Arts | Commerce | Science | Technology | Administration",
  "input_hash"        : "SHA-256 of full input payload",
  "output_hash"       : "SHA-256 of full output payload",
  "model_version"     : "semver of active ML model",
  "prompt_version"    : "semver of AI prompt used (if applicable)",
  "decision_path"     : "ML_PRIMARY | AI_ASSIST | HYBRID",
  "confidence_score"  : "DECIMAL(4,4)",
  "computation_scope" : "TENANT | COHORT | USER | PLATFORM",
  "anomaly_flags"     : ["string array"],
  "escalation_flag"   : "BOOLEAN",
  "execution_duration_ms" : "INTEGER",
  "kafka_offset"      : "INTEGER — event stream position for replay"
}
```

> **Logs are immutable. No UPDATE. No DELETE. No override. Enforced at database layer via INSERT-ONLY policy on `financial_audit_log` table.**

---

## 9️⃣ FAILURE POLICY

### Failure Scenarios & Responses

| Failure Type | Action | Escalation |
|---|---|---|
| Invalid input (schema violation) | STOP · LOG · REJECT | None — client must fix |
| Tenant not found in TENANT_REGISTRY | STOP · LOG · ALERT | ADMIN_GOVERNANCE_AGENT |
| ML model unavailable | STOP · LOG · FALLBACK to cached snapshot (max 4h) | ML_OPS_TEAM via PagerDuty |
| AI timeout (> 5000ms) | CONTINUE without AI narrative · LOG warning | OBSERVABILITY_AGENT |
| Data corruption detected (hash mismatch) | STOP · LOG · QUARANTINE input | SECURITY_AGENT + COMPLIANCE_AGENT |
| Confidence score < 0.72 | HOLD output · LOG · ESCALATE for human review | FINANCE_GOVERNANCE_AGENT |
| Cross-tenant data detected | STOP · LOG · SECURITY_INCIDENT | SECURITY_AGENT (P0 alert) |
| Infrastructure cost feed missing | CONTINUE partial compute · FLAG incomplete · LOG | OBSERVABILITY_AGENT |
| Queue depth > 50,000 (overload) | THROTTLE per-tenant · LOG · SCALE_TRIGGER | INFRA_OPS |

```yaml
STOP_EXECUTION  : YES — enforced on all critical failure types
LOG_INCIDENT    : YES — to AUDIT_LOG_SERVICE and INCIDENT_REGISTRY
ESCALATE_TO:
  - P0 (Security)     : SECURITY_AGENT → CISO_ONCALL (PagerDuty)
  - P1 (Data)         : COMPLIANCE_AGENT → FINANCE_GOVERNANCE_AGENT
  - P2 (Model)        : ML_OPS_TEAM → Slack #ml-ops-alerts
  - P3 (Performance)  : OBSERVABILITY_AGENT → Slack #infra-alerts

RETRY_POLICY:
  - Transient failures: Exponential backoff — 500ms, 2s, 8s (max 3 retries)
  - Persistent failures: DLQ after 3 retries, human review required
  - Model failures: No retry — serve cached snapshot, flag as STALE

SILENT_FAILURES : FORBIDDEN — every failure must produce an audit log entry
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - BILLING_SERVICE              # Raw payment + subscription events
  - SUBSCRIPTION_STATE_MACHINE   # Plan lifecycle transitions
  - FEATURE_STORE_AGENT          # User behavioral feature vectors
  - JOB_PORTAL_ENGINE            # Placement revenue events
  - MARKETPLACE_SERVICE          # Transaction records
  - ROYALTY_ENGINE               # Outbound royalty cost records
  - DOJO_ENGINE                  # Session billing events
  - OBSERVABILITY_AGENT          # Infrastructure cost feed

DOWNSTREAM_AGENTS:
  - TRUST_INFRASTRUCTURE_AGENT   # LTV + trust_economic_score
  - RETENTION_TRIGGER_AGENT      # Churn risk + LTV decay
  - GROWTH_ENGINE_AGENT          # ARPU delta + cohort health
  - ROYALTY_ENGINE               # Revenue attribution map
  - PRODUCT_GOVERNANCE_AGENT     # Feature profitability scores
  - RANK_ENGINE_AGENT            # Economic reliability scores
  - OBSERVABILITY_AGENT          # Anomaly + cost-drift alerts
  - ADMIN_GOVERNANCE_AGENT       # Compliance financial health

EVENT_TRIGGERS_EMITTED:
  - unit_economics.snapshot.computed
  - unit_economics.churn_risk.detected
  - unit_economics.ltv_threshold.breached
  - unit_economics.anomaly.flagged
  - unit_economics.trust_score.updated
  - unit_economics.model.drift_detected
  - unit_economics.feature.profitability_updated
  - unit_economics.royalty.attribution_ready

EVENT_TRIGGERS_CONSUMED:
  - billing.payment.success
  - billing.payment.failed
  - subscription.created
  - subscription.renewed
  - subscription.cancelled
  - subscription.upgraded
  - subscription.downgraded
  - placement.completed
  - marketplace.transaction.completed
  - royalty.disbursed
  - dojo.session.billed
  - infrastructure.cost.allocated
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent emits the following feature vectors to `FEATURE_STORE_AGENT` after each computation:

```json
EMIT_FEATURE_VECTOR: [
  {
    "user_id"        : "UUID (if user-scoped)",
    "feature_name"   : "unit_econ.ltv_usd",
    "feature_value"  : "DECIMAL",
    "timestamp"      : "ISO8601",
    "source_agent"   : "UNIT_ECONOMICS_ENGINE_AGENT"
  },
  {
    "user_id"        : "UUID",
    "feature_name"   : "unit_econ.churn_probability",
    "feature_value"  : "DECIMAL(4,4)",
    "timestamp"      : "ISO8601",
    "source_agent"   : "UNIT_ECONOMICS_ENGINE_AGENT"
  },
  {
    "user_id"        : "UUID",
    "feature_name"   : "unit_econ.payback_period_months",
    "feature_value"  : "DECIMAL",
    "timestamp"      : "ISO8601",
    "source_agent"   : "UNIT_ECONOMICS_ENGINE_AGENT"
  },
  {
    "user_id"        : "UUID",
    "feature_name"   : "unit_econ.arpu_usd",
    "feature_value"  : "DECIMAL",
    "timestamp"      : "ISO8601",
    "source_agent"   : "UNIT_ECONOMICS_ENGINE_AGENT"
  },
  {
    "user_id"        : "UUID",
    "feature_name"   : "unit_econ.trust_economic_score",
    "feature_value"  : "DECIMAL(4,4)",
    "timestamp"      : "ISO8601",
    "source_agent"   : "UNIT_ECONOMICS_ENGINE_AGENT"
  }
]
```

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY

When this agent computes revenue attribution that touches idea-originated content (Marketplace items, Dojo training packs, course content):

```json
EMIT_TO_ROYALTY_ENGINE: {
  "idea_id"              : "UUID",
  "revenue_attributed_usd": "DECIMAL(12,4)",
  "attribution_period"   : "ISO8601 date range",
  "tenant_id"            : "UUID",
  "computation_basis"    : "DIRECT | DOWNSTREAM_INFLUENCE",
  "confidence_score"     : "DECIMAL(4,4)",
  "audit_reference"      : "UUID"
}

COMPATIBLE_AGENTS:
  - IDEA_DNA_AGENT          # Idea originality reference for revenue weighting
  - ROYALTY_ENGINE          # Receives attribution vector for disbursement
  - COPY_DETECTION_ENGINE   # Validates attribution uniqueness
```

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

When this agent detects economic signals affecting ranking or achievement:

```yaml
TRIGGER_ON:
  - LTV_CAC_RATIO > 3.0       → EMIT RANK_UPDATE_EVENT (recruiter/trainer tier elevation)
  - CHURN_PROBABILITY > 0.65  → EMIT RETENTION_TRIGGER_EVENT
  - ARPU > PLAN_TIER_CEILING  → EMIT UPGRADE_SUGGESTION_EVENT
  - NRR > 1.10                → EMIT XP_UPDATE_EVENT (enterprise account health badge)
  - TRIAL_CONVERTED           → EMIT SHARE_TRIGGER_EVENT (conversion celebration card)

EVENTS_EMITTED:
  - RANK_UPDATE_EVENT         → RANK_ENGINE_AGENT
  - XP_UPDATE_EVENT           → GAMIFICATION_ENGINE_AGENT
  - SHARE_TRIGGER_EVENT       → SOCIAL_DISTRIBUTION_AGENT
  - RETENTION_TRIGGER_EVENT   → RETENTION_TRIGGER_AGENT
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

```yaml
METRICS_EMITTED_TO_OBSERVABILITY_AGENT:
  - unit_econ.success_rate              # Target: > 99.5%
  - unit_econ.error_rate                # Target: < 0.5%
  - unit_econ.latency_p50_ms            # Target: < 80ms
  - unit_econ.latency_p95_ms            # Target: < 250ms
  - unit_econ.latency_p99_ms            # Target: < 600ms
  - unit_econ.ml_drift_indicator        # Target: PSI < 0.2
  - unit_econ.anomaly_frequency         # Target: < 0.1% of executions
  - unit_econ.confidence_below_threshold_rate  # Target: < 2%
  - unit_econ.cache_hit_rate            # Target: > 85%
  - unit_econ.dlq_depth                 # Target: 0

ALERTING_THRESHOLDS:
  - error_rate > 1%            → P2 Alert → Slack #unit-econ-alerts
  - latency_p99 > 1000ms       → P2 Alert
  - ml_drift_indicator > 0.2   → P1 Alert → ML_OPS_TEAM
  - cross_tenant_attempt > 0   → P0 Alert → SECURITY_AGENT
  - dlq_depth > 100            → P1 Alert

DASHBOARD:
  - Grafana: unit-economics-engine-agent board
  - Panels: Success rate, Latency heatmap, Churn risk distribution,
            LTV trend by tenant type, Feature profitability treemap,
            Drift indicators, DLQ depth
```

---

## 1️⃣5️⃣ VERSIONING POLICY

```yaml
VERSION_STRATEGY         : Semantic Versioning (MAJOR.MINOR.PATCH)
MUTATION_POLICY          : ADD-ONLY — no field removal, no schema breaking changes
BACKWARD_COMPATIBILITY   : Mandatory — old consumers must continue to function
MIGRATION_DOCUMENTATION  : Required for every MINOR or MAJOR version bump

VERSION_ARTIFACTS:
  - Agent Specification:  Versioned in AGENT_REGISTRY (this document)
  - ML Models:            Versioned in MLflow model registry
  - AI Prompts:           Versioned in PROMPT_REGISTRY
  - API Contracts:        Versioned in OpenAPI 3.1 registry
  - Event Schemas:        Versioned in AsyncAPI registry
  - Database Migrations:  Alembic migration files, append-only

ROLLBACK_POLICY:
  - Model rollback: Immediate, no approval required (ML_OPS_TEAM authority)
  - Agent rollback: Requires FINANCE_GOVERNANCE_AGENT approval
  - Schema rollback: FORBIDDEN — use forward-only migrations
```

---

## 1️⃣6️⃣ CORE METRICS COMPUTED

### Revenue Metric Definitions (Locked)

| Metric | Formula | Computation Frequency |
|---|---|---|
| **MRR** | Sum of all active recurring subscription revenue in current month | Real-time on every SUBSCRIPTION event |
| **ARR** | MRR × 12 | Derived from MRR |
| **ARPU** | MRR ÷ Active paying users | Real-time |
| **CAC** | Total sales + marketing cost ÷ New customers acquired | Monthly batch |
| **LTV** | ARPU × Gross Margin % ÷ Monthly Churn Rate | Weekly |
| **LTV:CAC Ratio** | LTV ÷ CAC | Derived |
| **Payback Period** | CAC ÷ (ARPU × Gross Margin %) | Derived |
| **Gross Margin %** | (Revenue − COGS) ÷ Revenue | Monthly batch |
| **Monthly Churn Rate** | Churned customers this month ÷ Customers at start of month | Monthly |
| **NRR (Net Revenue Retention)** | (Starting MRR + Expansion − Contraction − Churn) ÷ Starting MRR | Monthly |
| **Contribution Margin** | Revenue − Variable Costs (infra + royalty + per-transaction) | Per billing cycle |
| **Trust Economic Score** | Composite: LTV_normalized × NRR_normalized × PaybackPeriod_inverse | Weekly |

---

## 1️⃣7️⃣ TENANT TYPE ECONOMIC PROFILES

Each tenant type has distinct unit economic computation rules — **no cross-type aggregation permitted without PLATFORM_ADMIN scope**.

```yaml
INSTITUTE:
  Revenue streams: [Subscription, Student seat fees, Placement success fee]
  CAC basis: Direct sales + TPO partnership cost
  LTV driver: Cohort retention, placement rate, re-enrollment
  Trust weight: Placement completion rate × financial health score

ENTERPRISE / SME:
  Revenue streams: [Subscription, Job posting credits, Hiring ERP seat fees]
  CAC basis: Sales team cost + enterprise marketing
  LTV driver: Hiring volume, seat expansion, contract renewal
  Trust weight: Payment reliability × hiring volume × contract age

RECRUITER:
  Revenue streams: [Application credit packs, featured posting fees]
  CAC basis: Platform marketing allocation
  LTV driver: Hire success rate, repeat posting behavior
  Trust weight: Hire success rate × fraud-free history

TRAINER:
  Revenue streams: [Course listing revenue share, Dojo session fees]
  CAC basis: Onboarding cost allocation
  LTV driver: Student completion rate, course rating, repeat sessions
  Trust weight: Course rating × royalty compliance × completion rate

INDIVIDUAL (Student):
  Revenue streams: [Premium subscription, Dojo battle credits, Marketplace purchases]
  CAC basis: Digital channel acquisition cost
  LTV driver: Skill progression, placement success, community engagement
  Trust weight: Engagement score × payment history × verification status
```

---

## 1️⃣8️⃣ DATABASE SCHEMA (LOCKED)

```sql
-- APPEND-ONLY. NO UPDATE. NO DELETE.

CREATE TABLE unit_economics_snapshots (
  id                          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id                   UUID NOT NULL REFERENCES tenants(id),
  computation_scope           TEXT NOT NULL CHECK (computation_scope IN ('TENANT','COHORT','USER','PLATFORM')),
  snapshot_timestamp_utc      TIMESTAMP NOT NULL,
  mrr_usd                     NUMERIC(12,4),
  arr_usd                     NUMERIC(12,4),
  arpu_usd                    NUMERIC(12,4),
  cac_usd                     NUMERIC(12,4),
  ltv_usd                     NUMERIC(12,4),
  ltv_cac_ratio               NUMERIC(6,4),
  gross_margin_pct            NUMERIC(5,4),
  payback_period_months       NUMERIC(6,2),
  churn_rate_monthly_pct      NUMERIC(5,4),
  net_revenue_retention_pct   NUMERIC(5,4),
  expansion_revenue_usd       NUMERIC(12,4),
  contraction_revenue_usd     NUMERIC(12,4),
  churned_mrr_usd             NUMERIC(12,4),
  new_mrr_usd                 NUMERIC(12,4),
  royalty_cost_usd            NUMERIC(12,4),
  infrastructure_cost_usd     NUMERIC(12,4),
  contribution_margin_usd     NUMERIC(12,4),
  trust_economic_score        NUMERIC(5,4),
  confidence_score            NUMERIC(4,4),
  model_version               TEXT NOT NULL,
  audit_reference             UUID NOT NULL,
  anomaly_flags               JSONB,
  domain_revenue_breakdown    JSONB,
  feature_profitability_map   JSONB,
  created_at                  TIMESTAMP DEFAULT NOW()
);

-- Row-Level Security enforced
ALTER TABLE unit_economics_snapshots ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON unit_economics_snapshots
  USING (tenant_id = current_setting('app.current_tenant_id')::UUID);

CREATE TABLE churn_risk_registry (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id             UUID NOT NULL,
  cohort_id             UUID,
  user_id               UUID,
  churn_probability     NUMERIC(4,4) NOT NULL,
  ltv_at_risk_usd       NUMERIC(12,4),
  risk_tier             TEXT CHECK (risk_tier IN ('LOW','MEDIUM','HIGH','CRITICAL')),
  model_version         TEXT NOT NULL,
  computed_at_utc       TIMESTAMP NOT NULL,
  audit_reference       UUID NOT NULL,
  created_at            TIMESTAMP DEFAULT NOW()
);

CREATE TABLE financial_audit_log (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  timestamp_utc         TIMESTAMP NOT NULL,
  actor_id              UUID NOT NULL,
  tenant_id             UUID NOT NULL,
  domain_track          TEXT,
  input_hash            TEXT NOT NULL,
  output_hash           TEXT NOT NULL,
  model_version         TEXT NOT NULL,
  prompt_version        TEXT,
  decision_path         TEXT NOT NULL,
  confidence_score      NUMERIC(4,4),
  computation_scope     TEXT NOT NULL,
  anomaly_flags         JSONB,
  escalation_flag       BOOLEAN DEFAULT FALSE,
  execution_duration_ms INTEGER,
  kafka_offset          BIGINT,
  created_at            TIMESTAMP DEFAULT NOW()
  -- NO UPDATE TRIGGER ENFORCED AT APPLICATION LAYER
);
```

---

## 1️⃣9️⃣ NON-NEGOTIABLE RULES

```
THIS AGENT MUST NOT:
  ❌ Create hidden financial computation logic outside this specification
  ❌ Modify any historical financial record
  ❌ Auto-delete or archive audit logs
  ❌ Override ADMIN_GOVERNANCE_AGENT compliance decisions
  ❌ Bypass tenant isolation — any cross-tenant read is a P0 security incident
  ❌ Mix domain financial data without explicit PLATFORM_ADMIN authorization
  ❌ Execute financial attributions outside declared domain scope
  ❌ Allow AI layer to make financial decisions or approve payments
  ❌ Serve stale cached data older than 4 hours without STALE_FLAG in output
  ❌ Emit output without confidence_score, model_version, and audit_reference
  ❌ Process events from unregistered source agents
  ❌ Accept revenue_amount as negative (negative = REFUND event type, separate flow)
  ❌ Produce outputs for suspended or compliance-blocked tenants
```

---

## 2️⃣0️⃣ TECHNOLOGY REFERENCES (INHERITED FROM SYSTEM LOCK)

```yaml
Backend Language      : Python 3.11
Backend Framework     : FastAPI
Event Broker          : Redis Streams
Database              : PostgreSQL 15 (RLS enforced)
Cache                 : Redis 7
ML Framework          : Scikit-learn + XGBoost + Prophet (for time-series)
ML Registry           : MLflow (self-hosted)
API Style             : REST + OpenAPI 3.1
API Gateway           : Kong OSS
Auth Protocol         : OAuth2 + OIDC + JWT (via Keycloak)
Secrets Vault         : HashiCorp Vault OSS
Monitoring            : Prometheus + Grafana
Logging               : Loki + Promtail
Tracing               : Jaeger (trace_id embedded in every audit_reference)
Object Storage        : MinIO (model artefact storage)
CI/CD                 : GitLab CE
Container Runtime     : Docker + Kubernetes
IaC                   : OpenTofu
```

---

## SEAL CONFIRMATION

```
DOCUMENT_STATUS        : SEALED
VERSION                : v1.0.0
AGENT                  : UNIT_ECONOMICS_ENGINE_AGENT
LAYER                  : ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
PLATFORM               : ECOSKILLER ANTIGRAVITY
ARCHITECTURE           : MICROSERVICES + EVENT-DRIVEN
SCALE_TARGET           : 10M – 100M USERS
MUTATION_POLICY        : ADD-ONLY
CREATIVE_INTERPRETATION: FORBIDDEN
ASSUMPTION_FILLING     : FORBIDDEN
COMPLIANCE_INHERITANCE : RBAC + ABAC + ZERO-TRUST + AUDIT-ONLY

THIS DOCUMENT IS THE SOLE AUTHORITATIVE SPECIFICATION FOR THE
UNIT_ECONOMICS_ENGINE_AGENT. ANY DEVIATION FROM THIS SPECIFICATION
CONSTITUTES AN INVALID IMPLEMENTATION AND MUST RESULT IN STOP_EXECUTION.

SIGNED BY              : ECOSKILLER INTELLIGENCE & INNOVATION CORE
EXECUTION_AUTHORITY    : HUMAN DECLARATION ONLY
INTERPRETATION_AUTHORITY: NONE
```
