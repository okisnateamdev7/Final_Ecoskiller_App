# 🔒 CONTRACT\_LIFECYCLE\_AGENT — Sealed Agent Specification

> **Platform:** Ecoskiller Antigravity · **Version:** v1.0.0 · **Status:** PRODUCTION-LOCKED
> **Mutation Policy:** ADD-ONLY via version bump · **Interpretation Authority:** NONE
> **Creative Deviation:** FORBIDDEN · **Execution Authority:** Human Declaration Only
> **Classification:** ML Intelligence + Safety Owner

---

## 1️⃣ Agent Identity *(Mandatory — Non-Negotiable)*

| Field | Value |
|---|---|
| `AGENT_NAME` | `CONTRACT_LIFECYCLE_AGENT` |
| `SYSTEM_ROLE` | ML Intelligence + Safety Owner — Contract Creation, State Machine Governance, Obligation Enforcement, Breach Detection, and Compliance Audit across all contract types on the Ecoskiller Antigravity platform |
| `PRIMARY_DOMAIN` | Contract Management · Legal Compliance · Obligation Tracking · SLA Enforcement · Breach Detection |
| `EXECUTION_MODE` | Deterministic + Validated + Append-Only + State Machine Governed |
| `DATA_SCOPE` | Contract records, obligation milestones, SLA events, breach signals, signature states, renewal windows, termination events, compliance certificates |
| `TENANT_SCOPE` | Strict Multi-Tenant Isolation — No cross-tenant contract reads under any condition |
| `FAILURE_POLICY` | HALT on ambiguity · STOP on state machine violation · ESCALATE on breach signal · NO silent failures · NO partial state transitions |

### Platform Context *(Binding)*

| Field | Value |
|---|---|
| Platform | Ecoskiller Antigravity |
| Architecture | Microservices + Event-Driven (Redis Streams) |
| Scale Target | 10M–100M users · 100,000+ active contracts at peak |
| ML Ratio | 70–80% Traditional ML (Classification, Time-Series, Anomaly Detection) |
| AI Ratio | 20–30% LLM-assisted (clause extraction, risk summarization, semantic matching — advisory only) |
| Security Model | Zero-Trust · Multi-Tenant · Role-Gated · Domain-Isolated |
| Data Policy | Append-Only Audit Trail · Immutable contract versions · Legal retention enforced |
| Stack Lock | Python 3.11 · FastAPI · PostgreSQL 15 · Redis 7 · OpenSearch 2.x · Kubernetes · Keycloak · Kong OSS · MinIO |
| Mutation Policy | Add-Only Versioned — No retroactive modification of any contract record |

> ⛔ Absence of any mandatory section → **STOP EXECUTION**

---

## 2️⃣ Purpose Declaration

### Problem Solved

Ecoskiller Antigravity operates a multi-party ecosystem involving trainers, students, institutes, enterprises, freelancers, government bodies, and AI agents — all of whom enter into legally binding and platform-binding agreements. These contracts span: trainer–institute service agreements, student internship contracts, employer hiring contracts, course licensing agreements, project execution contracts, SLA-bound API/tool vendor agreements, and innovation economy royalty licensing.

Without a governed, ML-verified contract lifecycle agent, the platform is exposed to:
- Unsigned or improperly executed contracts triggering downstream financial flows prematurely
- Obligation milestone breaches going undetected until financial loss occurs
- Contract renewal windows lapsing silently, disrupting active services
- Cross-tenant contract data leaking due to absent isolation enforcement
- Fraudulent backdating or tampering of contract records

`CONTRACT_LIFECYCLE_AGENT` is the **singular authority** responsible for:

- **(a)** Creating, versioning, and storing all contract records across 12 contract types
- **(b)** Governing state machine transitions: DRAFT → REVIEW → SIGNED → ACTIVE → SUSPENDED → TERMINATED / RENEWED / EXPIRED
- **(c)** Monitoring obligation milestones and emitting alerts when deadlines approach or breach
- **(d)** Detecting SLA violations using ML time-series models and escalating to enforcement agents
- **(e)** Enforcing digital signature integrity via multi-party signature verification
- **(f)** Triggering downstream financial, ranking, and compliance events at each lifecycle transition
- **(g)** Operating as the **Safety Owner** — blocking any state transition that violates contractual rules, tenant isolation, or compliance policy

### Input Consumed

- Contract creation requests from `MARKETPLACE_AGENT`, `HIRING_AGENT`, `PROJECT_AGENT`, `BILLING_AGENT`
- Signature events from `ESIGN_SERVICE` (Keycloak-verified digital signatures)
- Obligation completion signals from `MILESTONE_TRACKER_AGENT`
- SLA performance data from `OBSERVABILITY_AGENT` and `SERVICE_DELIVERY_AGENT`
- Policy and legal clause templates from `POLICY_REGISTRY`
- User identity and role context from `IDENTITY_SERVICE`
- Renewal eligibility signals from `SUBSCRIPTION_AGENT`

### Output Produced

- Immutable contract version records (append-only)
- State transition events with full audit trail
- Obligation deadline alerts and breach notifications
- SLA violation reports to `COMPLIANCE_AUDIT_AGENT`
- Contract health scores per active agreement
- Feature vectors to `FEATURE_STORE_AGENT`
- Trigger events to `BILLING_AGENT`, `ROYALTY_WALLET_AGENT`, `RANK_ENGINE`

### Agent Dependencies

| Direction | Agents |
|---|---|
| **Downstream Dependents** | `BILLING_AGENT` · `ROYALTY_WALLET_AGENT` · `COMPLIANCE_AUDIT_AGENT` · `TRUST_SAFETY_AGENT` · `RANK_ENGINE` · `XP_ENGINE` · `NOTIFICATION_AGENT` · `OBSERVABILITY_AGENT` |
| **Upstream Feeders** | `MARKETPLACE_AGENT` · `HIRING_AGENT` · `PROJECT_AGENT` · `ESIGN_SERVICE` · `MILESTONE_TRACKER_AGENT` · `POLICY_REGISTRY` · `IDENTITY_SERVICE` · `SUBSCRIPTION_AGENT` · `BILLING_AGENT` |

---

## 3️⃣ Input Contract *(Strict — No Null Tolerance)*

### 3A — Contract Creation Request Schema

```json
{
  "required_fields": [
    "request_id",           // UUID v4 — idempotency key
    "tenant_id",            // UUID — strict tenant boundary
    "contract_type",        // ENUM (see Section 3C)
    "party_a_id",           // UUID — initiating party (creator / employer / institute)
    "party_b_id",           // UUID — accepting party (trainer / student / freelancer)
    "domain_track",         // ENUM: [ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION]
    "template_id",          // UUID — from POLICY_REGISTRY (validated clause template)
    "effective_date",       // ISO 8601 date — must be >= today
    "expiry_date",          // ISO 8601 date — must be > effective_date
    "obligations",          // ARRAY of ObligationObject (min 1)
    "sla_terms",            // ARRAY of SLATermObject (may be empty for personal contracts)
    "governing_law",        // STRING — e.g. "India — Information Technology Act 2000"
    "initiated_by_role"     // ENUM: EMPLOYER | INSTITUTE | TRAINER | PLATFORM | GOVERNMENT
  ],
  "optional_fields": [
    "co_parties",           // ARRAY[UUID] — additional signatories beyond party_a and party_b
    "financial_value_inr",  // DECIMAL — total contract value if monetary
    "royalty_rate",         // FLOAT 0.0–1.0 — for licensing contracts only
    "renewal_policy",       // ENUM: [AUTO | MANUAL | NONE]
    "renewal_notice_days",  // INTEGER — days before expiry to trigger renewal alert
    "confidentiality_level",// ENUM: [PUBLIC | INTERNAL | CONFIDENTIAL | TOP_SECRET]
    "parent_contract_id",   // UUID — if this is an amendment or sub-contract
    "intelligence_profile_id", // UUID — from DOJO_INTELLIGENCE_ENGINE (for training contracts)
    "geographic_scope"      // STRING — e.g. "India", "Global", "Maharashtra"
  ],
  "validation_rules": [
    "request_id must be UUID v4",
    "tenant_id must match both party_a and party_b registered tenants OR cross-tenant license must exist",
    "effective_date must not be past-dated beyond -24 hours",
    "expiry_date must be > effective_date",
    "obligations array must contain at least 1 valid ObligationObject",
    "template_id must exist and be ACTIVE in POLICY_REGISTRY",
    "domain_track must match party_a's registered domain OR be explicitly cross-domain licensed",
    "contract_type must be a valid ENUM value (see Section 3C)",
    "governing_law must match a record in LEGAL_JURISDICTION_REGISTRY",
    "financial_value_inr if present: must be DECIMAL ≥ 0.0",
    "royalty_rate if present: must be 0.0 ≤ x ≤ 1.0",
    "renewal_notice_days if present: must be INTEGER 1–365"
  ],
  "security_checks": [
    "JWT validation via Keycloak — both party_a and initiated_by_role verified",
    "Tenant boundary enforcement — party_a and party_b in same tenant OR CROSS_TENANT_AGREEMENT exists",
    "Rate limit: max 100 contract creation requests per tenant per minute",
    "Role authorization: initiated_by_role must match RBAC permissions of requesting user",
    "Template integrity: template_id hash verified against POLICY_REGISTRY signed record",
    "Replay prevention: request_id idempotency check (Redis TTL 72h)"
  ],
  "domain_checks": [
    "Arts domain contracts: require additional content clause verification",
    "Government contracts: require REGULATORY_APPROVAL_FLAG from COMPLIANCE_AUDIT_AGENT",
    "Cross-domain contracts: require explicit DOMAIN_CROSS_LICENSE in POLICY_REGISTRY",
    "Technology domain: NDA clause enforced by default for all IP-containing contracts"
  ]
}
```

### 3B — Obligation Object Schema

```json
{
  "obligation_id":      "UUID",
  "obligation_type":    "ENUM: [DELIVERY | PAYMENT | ATTENDANCE | COMPLETION | REPORTING | CERTIFICATION | REVIEW]",
  "description":        "STRING — max 500 chars",
  "due_date":           "ISO 8601",
  "responsible_party":  "ENUM: [PARTY_A | PARTY_B | BOTH | PLATFORM]",
  "verification_method":"ENUM: [MILESTONE_SIGNAL | MANUAL_APPROVAL | ML_DETECTION | PLATFORM_EVENT]",
  "penalty_clause":     "STRING | null — consequence of non-fulfillment",
  "weight":             "FLOAT 0.0–1.0 — relative importance for contract health score"
}
```

### 3C — Supported Contract Types

| Code | Contract Type | Parties |
|---|---|---|
| `TRAINER_SERVICE` | Trainer–Institute Service Agreement | Trainer ↔ Institute |
| `INTERNSHIP` | Student Internship Contract | Student ↔ Employer |
| `EMPLOYMENT` | Full/Part-time Employment Contract | Employer ↔ Candidate |
| `COURSE_LICENSE` | Course Content Licensing | Creator ↔ Institute/Platform |
| `PROJECT_EXECUTION` | Freelance Project Contract | Freelancer ↔ Client |
| `MENTORSHIP` | Mentorship Agreement | Mentor ↔ Student/Professional |
| `VENDOR_SLA` | Tool/API Vendor SLA | Vendor ↔ Platform |
| `GOVT_SKILL_PROGRAM` | Government Skill Program MOU | Govt Body ↔ Platform/Institute |
| `INNOVATION_LICENSE` | IP/Innovation Licensing | Creator ↔ Licensee |
| `ASSESSMENT_PARTNER` | Assessment/Certification Partnership | Examiner ↔ Institute |
| `FRANCHISE` | Franchise Institute Agreement | Platform ↔ Franchise Institute |
| `NDA` | Non-Disclosure Agreement | Any Party ↔ Any Party |

### 3D — Rejection Rules

| Condition | Action |
|---|---|
| Any missing required field | `REJECT` + `LOG_VALIDATION_FAILURE` + `EMIT_ALERT` |
| Null in required field | `REJECT` (no null tolerance) |
| Malformed UUID | `REJECT` |
| Past-dated effective_date | `REJECT` + `FLAG_ANOMALY` |
| Template not found in POLICY_REGISTRY | `HALT` + `ESCALATE_TO = COMPLIANCE_AUDIT_AGENT` |
| Tenant boundary violation | `REJECT` + `SECURITY_ALERT_EVENT` + `ESCALATE_TO = SECURITY_ADMIN` |
| Government contract without REGULATORY_APPROVAL_FLAG | `HALT` + `ESCALATE_TO = COMPLIANCE_AUDIT_AGENT` |
| Obligations array empty | `REJECT` — minimum 1 obligation enforced |

---

## 4️⃣ Output Contract *(Strict)*

```json
{
  "result_object": {
    "contract_id":             "UUID — globally unique immutable contract record",
    "contract_version":        "INTEGER — starts at 1, increments on each amendment",
    "contract_type":           "ENUM — from Section 3C",
    "state":                   "ENUM: [DRAFT | REVIEW | PENDING_SIGNATURE | SIGNED | ACTIVE | SUSPENDED | BREACHED | TERMINATED | EXPIRED | RENEWED]",
    "party_a_id":              "UUID",
    "party_b_id":              "UUID",
    "tenant_id":               "UUID",
    "domain_track":            "STRING",
    "effective_date":          "ISO 8601",
    "expiry_date":             "ISO 8601",
    "obligations_summary": [
      {
        "obligation_id":       "UUID",
        "status":              "ENUM: [PENDING | IN_PROGRESS | FULFILLED | BREACHED | WAIVED]",
        "due_date":            "ISO 8601",
        "days_remaining":      "INTEGER"
      }
    ],
    "contract_health_score":   "FLOAT 0.0–1.0 — ML-computed overall contract health",
    "breach_risk_score":       "FLOAT 0.0–1.0 — predictive ML breach probability",
    "sla_compliance_rate":     "FLOAT 0.0–1.0",
    "signature_status": {
      "party_a_signed":        "BOOLEAN",
      "party_b_signed":        "BOOLEAN",
      "platform_witnessed":    "BOOLEAN",
      "signed_at_utc":         "ISO 8601 | null"
    },
    "next_review_date":        "ISO 8601 | null",
    "renewal_eligible":        "BOOLEAN",
    "financial_obligations_met": "BOOLEAN"
  },
  "confidence_score":          "FLOAT 0.0–1.0",
  "model_version":             "STRING — e.g. contract_risk_model_v2.3",
  "audit_reference":           "UUID — immutable audit trail ID",
  "next_trigger_events": [
    "CONTRACT_CREATED_EVENT",
    "CONTRACT_SIGNED_EVENT",
    "CONTRACT_ACTIVATED_EVENT",
    "OBLIGATION_DUE_ALERT",
    "BILLING_TRIGGER_EVENT",
    "RANK_UPDATE_EVENT"
  ]
}
```

### Mandatory Output Guarantees

- Every output includes: `confidence_score` + `model_version` + `audit_reference`
- `confidence_score < 0.70` → state transition to `REVIEW` state; `ESCALATE_TO = COMPLIANCE_AUDIT_AGENT`
- `confidence_score < 0.50` → `STOP_EXECUTION`, `LOG_INCIDENT`, **NO STATE TRANSITION COMMITTED**
- `breach_risk_score > 0.75` → emit `BREACH_RISK_ALERT_EVENT` to `TRUST_SAFETY_AGENT`
- ALL outputs are append-only to contract store — no updates, no deletes to historical records

---

## 5️⃣ ML / AI Logic Layer *(Core Intelligence)*

### A. ML Layer — 70% · Primary Decision Engine

---

#### MODEL 1: `CONTRACT_HEALTH_SCORE_MODEL`

| Property | Value |
|---|---|
| Model Type | Gradient Boosted Classification (XGBoost) |
| Purpose | Compute real-time contract health score reflecting obligation fulfillment probability |
| Output | `contract_health_score` (FLOAT 0.0–1.0) + health tier classification |
| Training Frequency | Monthly — on last 90 days of completed/terminated contracts |
| Minimum Samples | 500 completed contracts per domain per training cycle |
| Drift Detection | PSI monitored weekly; PSI > 0.25 → model freeze + escalate to `OBSERVABILITY_AGENT` |
| Version Control | Immutable: `contract_health_model_v{major}.{minor}` in MinIO |

**Features Used:**

| Feature | Description |
|---|---|
| `obligations_fulfilled_ratio` | fulfilled / total obligations (0.0–1.0) |
| `days_to_expiry` | integer — remaining contract days |
| `overdue_obligations_count` | integer |
| `sla_compliance_rate` | rolling 30-day SLA rate (0.0–1.0) |
| `party_a_reputation_score` | from `REPUTATION_ENGINE` (L1–L7) |
| `party_b_reputation_score` | from `REPUTATION_ENGINE` (L1–L7) |
| `financial_payment_on_time_rate` | historical payment timeliness (0.0–1.0) |
| `contract_type_risk_weight` | static risk multiplier per contract type |
| `domain_track_risk_score` | per-domain historical default rate |
| `amendment_count` | number of contract versions (high count = risk signal) |
| `cross_tenant_flag` | boolean — cross-tenant contracts have higher risk weight |
| `intelligence_profile_match_score` | from `DOJO_INTELLIGENCE_ENGINE` (for training contracts) |

---

#### MODEL 2: `BREACH_PREDICTION_MODEL`

| Property | Value |
|---|---|
| Model Type | Random Forest Classifier + Time-Series LSTM for deadline prediction |
| Purpose | Predict probability of obligation breach before it occurs; enable proactive intervention |
| Output | `breach_risk_score` (FLOAT 0.0–1.0) per obligation per contract |
| Training Frequency | Weekly — fraud/breach patterns evolve rapidly |
| Drift Detection | F1-score on labeled breach holdout set; F1 < 0.80 → freeze + escalate |

**Features Used:**

| Feature | Description |
|---|---|
| `days_since_last_obligation_activity` | integer — staleness signal |
| `communication_response_rate` | platform messages replied / sent (0.0–1.0) |
| `milestone_completion_velocity` | rate of milestone completions per week |
| `historical_breach_count` | party's past breach count on platform |
| `login_activity_decline_score` | 7-day rolling login trend (dropout signal) |
| `financial_stress_signal` | late payment flags from `BILLING_AGENT` (0/1) |
| `contract_complexity_score` | number of obligations × amendment count |
| `seasonal_risk_factor` | exam season, holiday patterns (time-series) |
| `party_account_age_days` | new accounts have higher breach risk |
| `dojo_engagement_drop_flag` | from `FEATURE_STORE_AGENT` — disengagement signal |

**Breach Alert Thresholds:**

| Score Range | Action |
|---|---|
| `0.00 – 0.40` | ✅ No action — healthy |
| `0.41 – 0.60` | ⚠️ `BREACH_RISK_MONITORING_EVENT` — soft alert to parties |
| `0.61 – 0.75` | 🔶 `BREACH_RISK_ALERT_EVENT` — formal notice triggered |
| `> 0.75` | ⛔ `BREACH_IMMINENT_EVENT` → escalate to `TRUST_SAFETY_AGENT` + `COMPLIANCE_AUDIT_AGENT` |

---

#### MODEL 3: `SLA_VIOLATION_DETECTION_MODEL`

| Property | Value |
|---|---|
| Model Type | Anomaly Detection (Isolation Forest) + Rule Engine |
| Purpose | Detect SLA violations in vendor agreements, trainer service contracts, and government skill programs |
| Output | `sla_violation_flag` (BOOLEAN) + `sla_violation_severity` (ENUM: LOW, MEDIUM, HIGH, CRITICAL) |
| Training Frequency | Weekly |
| Drift Detection | Precision/recall monitored on labeled SLA events; degradation → freeze + escalate |

**SLA Hard Rules** *(enforced before ML score)*:

| Condition | Action |
|---|---|
| Service uptime < agreed SLA % | INSTANT `SLA_VIOLATION_EVENT` |
| Delivery deadline missed by > 24h | `SLA_BREACH_RECORDED` |
| API response time > SLA threshold for 5+ consecutive minutes | `VENDOR_SLA_ALERT` |
| Trainer attendance below contracted rate | `TRAINER_SLA_BREACH` |
| Student completion rate below MOU targets (govt contracts) | `GOVT_MOU_ALERT` |

---

#### MODEL 4: `CONTRACT_RENEWAL_ELIGIBILITY_MODEL`

| Property | Value |
|---|---|
| Model Type | Logistic Regression |
| Purpose | Predict renewal likelihood and compute optimal renewal offer terms |
| Output | `renewal_eligible` (BOOLEAN) + `renewal_probability` (FLOAT 0.0–1.0) + `recommended_renewal_terms` |
| Training Frequency | Monthly |

**Features Used:** `contract_health_score`, `sla_compliance_rate`, `party_satisfaction_signals`, `financial_value_trend`, `platform_engagement_score`, `domain_market_demand_index`

---

#### MODEL 5: `CONTRACT_RISK_CLASSIFICATION_MODEL`

| Property | Value |
|---|---|
| Model Type | Multi-Class Classification (LightGBM) |
| Purpose | Classify incoming contract creation requests by risk tier before DRAFT state is committed |
| Output | `risk_tier` (ENUM: LOW · MEDIUM · HIGH · CRITICAL) + `risk_reason_flags` |
| Training Frequency | Monthly |

**Risk Tier Actions:**

| Tier | Action |
|---|---|
| LOW | Auto-proceed to DRAFT |
| MEDIUM | Proceed to DRAFT + flag for periodic monitoring |
| HIGH | Proceed to DRAFT + mandatory `COMPLIANCE_AUDIT_AGENT` review before SIGNED state |
| CRITICAL | HALT + `ESCALATE_TO = COMPLIANCE_AUDIT_AGENT` + `TRUST_SAFETY_AGENT` |

---

### B. AI Layer — 20–30% · Semantic Assist Only

**AI Usage Scope:**
- Semantic extraction and summarization of obligation clauses from uploaded contract documents
- Detecting clause anomalies or missing standard protections via NLP comparison against template
- Generating plain-language contract summaries for student and non-technical user dashboards
- Semantic matching of contract parties' intelligence profiles (from `DOJO_INTELLIGENCE_ENGINE`) to contract role fit assessment

**AI Must NOT:**
- Make final state transition decisions
- Override ML breach predictions or health scores
- Modify contract records
- Execute legal compliance decisions autonomously
- Generate novel contract clauses not present in `POLICY_REGISTRY` templates

**Prompt Governance:**

| Rule | Value |
|---|---|
| Prompt versioning | `prompt_contract_v{n}` |
| Temperature | `0.0` (fully deterministic) |
| Creative interpretation | FORBIDDEN |
| Decision authority | ML models are authoritative — LLM is advisory and explanatory only |
| Latency budget | 4,000ms max → timeout = fallback to template-only path |
| Max output length | 1,000 tokens — no essay-length AI outputs in transactional paths |

---

## 6️⃣ Scalability Design

| Parameter | Value |
|---|---|
| `EXPECTED_RPS` | 2,000 contract events/sec (peak: 20,000 during semester start / hiring season) |
| `LATENCY_TARGET` | p50 ≤ 200ms · p95 ≤ 500ms · p99 ≤ 1,000ms |
| `MAX_CONCURRENCY` | 5,000 in-flight contract state machine evaluations |
| `QUEUE_STRATEGY` | Redis Streams (`CONTRACT_EVENT_STREAM`) — 5 consumer groups per domain track |
| `STATE_MACHINE_ENGINE` | Deterministic FSM with event-sourcing; all transitions are events, not mutations |
| `PROCESSING_MODE` | Async for monitoring/health scoring; Synchronous fast-path for signature and ACTIVE state transitions |
| `STATELESS` | TRUE — all state in PostgreSQL (contracts) + Redis (locks + idempotency) |
| `IDEMPOTENCY` | `request_id` / `event_id` as idempotency key (Redis TTL 72h) |
| `HORIZONTAL_SCALING` | Kubernetes HPA — min 3 pods, max 40 pods; Scale: CPU > 70% OR queue depth > 8,000 |
| `CIRCUIT_BREAKER` | Resilience4j — 5 failures/10s → open |
| `RATE_LIMITING` | Kong OSS: 100 contract creations/tenant/min · 1,000 state events/tenant/min |
| `DISTRIBUTED_LOCK` | Redis `SETNX` lock per `contract_id` during state transitions (TTL 30s) |

---

## 7️⃣ Contract State Machine *(FSM — Sealed)*

```
                        ┌──────────────────────────────────────────┐
                        │       CONTRACT STATE MACHINE v1.0        │
                        │    Sealed · Deterministic · Add-Only     │
                        └──────────────────────────────────────────┘

  [CREATE REQUEST]
        │
        ▼
   ┌─────────┐   ML risk=CRITICAL     ┌──────────────┐
   │  DRAFT  │ ──────────────────────►│   REJECTED   │ (terminal)
   └─────────┘                        └──────────────┘
        │
        │  submit_for_review
        ▼
   ┌────────┐   compliance fail       ┌──────────────┐
   │ REVIEW │ ──────────────────────►│   REJECTED   │ (terminal)
   └────────┘                        └──────────────┘
        │
        │  review_approved
        ▼
   ┌──────────────────┐
   │ PENDING_SIGNATURE │
   └──────────────────┘
        │
        │  all_parties_signed (within signing_deadline)
        ▼
   ┌────────┐                         ┌──────────────────┐
   │ SIGNED │ ──(effective_date)────►│     ACTIVE       │
   └────────┘                        └──────────────────┘
                                             │
               ┌─────────────────────────────┼────────────────────────────┐
               │                             │                            │
        platform_action               breach_detected                expiry_date
               │                             │                            │
               ▼                             ▼                            ▼
        ┌───────────┐               ┌──────────────┐              ┌─────────┐
        │ SUSPENDED │               │   BREACHED   │              │ EXPIRED │
        └───────────┘               └──────────────┘              └─────────┘
               │                             │                            │
       resolved_or_waived            resolved / terminated        renewal=AUTO
               │                             │                            │
               ▼                             ▼                            ▼
           ACTIVE                    TERMINATED                    ┌─────────┐
                                    (terminal)                     │ RENEWED │
                                                                   └─────────┘
```

**FSM Rules (Non-Negotiable):**

| Rule |
|---|
| No state can skip intermediate states |
| State transitions require `confidence_score ≥ 0.70` |
| BREACHED state requires ML `breach_risk_score > 0.75` OR manual compliance declaration |
| TERMINATED is terminal — no re-activation permitted |
| REJECTED is terminal — new contract must be created |
| Distributed Redis lock required during any state transition |
| All transitions logged to immutable `contract_state_log` before downstream events are emitted |

---

## 8️⃣ Security Enforcement *(Zero-Trust — Non-Negotiable)*

### Tenant Isolation
- Every database query carries `tenant_id` filter — enforced at ORM layer
- No cross-tenant contract reads permitted under **any condition**
- `tenant_id` mismatch on any party → IMMEDIATE REJECT + `SECURITY_ALERT_EVENT`
- All contract data encrypted at rest (AES-256) and in transit (TLS 1.3)

### Domain Isolation
- Each domain track (Arts / Commerce / Science / Technology / Administration) has **isolated contract partitions**
- Contract clauses and templates are domain-scoped in `POLICY_REGISTRY`
- Cross-domain contracts require explicit `DOMAIN_CROSS_LICENSE` record + compliance approval

### Role-Based Authorization *(Keycloak RBAC)*

| Action | Permitted Roles |
|---|---|
| Create contract request | `MARKETPLACE_AGENT` · `HIRING_AGENT` · `PROJECT_AGENT` · `BILLING_AGENT` (service accounts) OR authenticated user with CONTRACT_CREATE permission |
| Sign contract | Registered parties only — verified via `ESIGN_SERVICE` |
| **Read contract full content** | Contract parties only · Platform Super Admin (audit, read-only) · Compliance Admin (read-only + audit log entry) |
| **Write / mutate contract state** | `CONTRACT_LIFECYCLE_AGENT` ONLY — no human writes to state machine |
| Suspend contract | Platform Admin OR `COMPLIANCE_AUDIT_AGENT` with reason |
| Terminate contract | Compliance Admin OR Platform Super Admin (human-supervised, irreversible) |
| Audit contract logs | Compliance Admin · Audit Admin · Platform Super Admin |

### Encryption
- Contract content fields: encrypted at column level (PostgreSQL `pgcrypto`)
- Digital signature payloads: asymmetric encryption (RSA-4096 via `ESIGN_SERVICE`)
- Model artifacts: signed + hash-verified on load from MinIO

### Contract Document Storage
- All contract PDFs/documents stored in MinIO with tenant-namespaced paths
- Object-level access control — no public URLs
- Hash of stored document appended to `contract_document_hash` field at upload time

---

## 9️⃣ Audit & Traceability *(Append-Only — Immutable)*

### Primary Audit Log Schema

```json
{
  "log_id":                "UUID (primary key)",
  "timestamp_utc":         "ISO 8601 — nanosecond precision",
  "tenant_id":             "UUID",
  "contract_id":           "UUID",
  "contract_version":      "INTEGER",
  "event_type":            "ENUM: [CREATED | STATE_TRANSITION | OBLIGATION_UPDATE | SLA_EVENT | BREACH_DETECTED | SIGNED | TERMINATED | RENEWED | AMENDED]",
  "from_state":            "ENUM | null",
  "to_state":              "ENUM",
  "actor_id":              "UUID — user or agent triggering event",
  "actor_role":            "STRING",
  "input_hash":            "SHA-256 of full input payload",
  "output_hash":           "SHA-256 of full output payload",
  "model_version":         "STRING",
  "confidence_score":      "FLOAT",
  "breach_risk_score":     "FLOAT | null",
  "contract_health_score": "FLOAT | null",
  "decision_path":         "ARRAY[STRING] — key ML features that drove the decision",
  "anomaly_flags":         "ARRAY[STRING]",
  "compliance_check":      "ENUM: [PASSED | FLAGGED | BLOCKED]",
  "execution_time_ms":     "INTEGER",
  "agent_pod_id":          "STRING — Kubernetes pod ID",
  "ip_address":            "STRING — hashed for privacy"
}
```

### Immutability Enforcement
- Table uses PostgreSQL INSERT-ONLY policy (no UPDATE, no DELETE for any role)
- Nightly hash-chain checkpoint published to `AUDIT_CHAIN_STORE` (tamper detection)
- **Retention: 10 years** (regulatory compliance — contracts have legal retention requirements exceeding standard 7-year financial rule)
- Cold storage replication to MinIO within 5 minutes of each log entry

---

## 🔟 Failure Policy *(No Silent Failures — Non-Negotiable)*

| Failure Condition | Policy |
|---|---|
| Invalid / malformed input | `REJECT` → `LOG_VALIDATION_FAILURE` → Emit `CONTRACT_INPUT_REJECTED_EVENT` |
| ML model unavailable | `STOP_EXECUTION` → `LOG_INCIDENT` → `ESCALATE_TO = OBSERVABILITY_AGENT` → Retry: 5s, 30s, 120s (max 3) |
| AI (LLM) timeout > 4,000ms | `FALLBACK` to template-only path → `LOG_AI_TIMEOUT` → Continue (non-blocking) |
| `confidence_score < 0.50` | `STOP_EXECUTION` → `LOG_LOW_CONFIDENCE` → **NO state transition committed** → `ESCALATE_TO = COMPLIANCE_AUDIT_AGENT` |
| `confidence_score` 0.50–0.70 | State transition suspended → contract placed in `REVIEW` state → `NOTIFY` compliance team within 4 hours |
| Breach risk score > 0.75 | `BREACH_IMMINENT_EVENT` emitted → `ESCALATE_TO = TRUST_SAFETY_AGENT` + `COMPLIANCE_AUDIT_AGENT` |
| State machine violation attempt | `HALT_STATE_TRANSITION` → `LOG_FSM_VIOLATION` → `ESCALATE_TO = SECURITY_ADMIN` |
| Distributed lock timeout (> 30s) | `ABORT_TRANSITION` → `LOG_LOCK_TIMEOUT` → Retry once → if failure: dead-letter |
| Signature verification failure | `REJECT_SIGNATURE` → `LOG_SIGNATURE_FAILURE` → `ESCALATE_TO = ESIGN_SERVICE` + `COMPLIANCE_AUDIT_AGENT` |
| Data corruption (`input_hash` mismatch) | `HALT_ALL_CONTRACT_WRITES` → `ESCALATE_TO = PLATFORM_SUPER_ADMIN` → `CRITICAL_DATA_INTEGRITY_ALERT` |
| Tenant isolation violation | `IMMEDIATE HALT` → `SECURITY_ALERT_EVENT` → `ESCALATE_TO = SECURITY_ADMIN` → Terminate session |
| Template not found in POLICY_REGISTRY | `HALT` → `ESCALATE_TO = COMPLIANCE_ADMIN` → No contract created until resolved |
| Queue overflow (> 80k events) | `CIRCUIT_BREAKER OPEN` → Shed non-urgent health-score recompute events → Alert `DEVOPS_AGENT` |
| TERMINATED state re-activation attempt | `IMMEDIATE REJECT` + `SECURITY_ALERT_EVENT` — terminal state is immutable |

**Escalation Targets:**

| Agent | Handles |
|---|---|
| `COMPLIANCE_AUDIT_AGENT` | Confidence failures, policy and template violations |
| `TRUST_SAFETY_AGENT` | Breach imminent, fraud signals |
| `OBSERVABILITY_AGENT` | Model and infrastructure failures |
| `SECURITY_ADMIN` | Tenant/domain isolation violations, FSM violations |
| `PLATFORM_SUPER_ADMIN` | Data integrity and catastrophic failures |
| `ESIGN_SERVICE` | Signature verification failures |

**Retry Policy:** `MAX_RETRIES = 3` · Backoff = Exponential (5s → 30s → 120s)
After 3 failures → `DEAD_LETTER_QUEUE` (`contract_dlq`) + P1 escalation ticket

---

## 1️⃣1️⃣ Inter-Agent Dependency Map

### Upstream Agents

| Agent | Provides |
|---|---|
| `MARKETPLACE_AGENT` | Course license and innovation contract creation triggers |
| `HIRING_AGENT` | Employment and internship contract creation triggers |
| `PROJECT_AGENT` | Freelance project contract creation triggers |
| `ESIGN_SERVICE` | Digital signature events with Keycloak-verified identity |
| `MILESTONE_TRACKER_AGENT` | Obligation completion signals |
| `SERVICE_DELIVERY_AGENT` | SLA performance metrics |
| `POLICY_REGISTRY` | Contract templates, clause libraries, domain rate rules |
| `IDENTITY_SERVICE` | Party roles, tenant assignments, RBAC context |
| `SUBSCRIPTION_AGENT` | Renewal eligibility and subscription status signals |
| `BILLING_AGENT` | Payment obligation status, financial stress signals |
| `FEATURE_STORE_AGENT` | Historical engagement and behavioral feature vectors |
| `DOJO_INTELLIGENCE_ENGINE` | Intelligence profile match scores for training contracts |

### Downstream Agents

| Agent | Receives |
|---|---|
| `BILLING_AGENT` | `CONTRACT_PAYMENT_TRIGGER_EVENT` (on ACTIVE state) |
| `ROYALTY_WALLET_AGENT` | `LICENSING_CONTRACT_ACTIVE_EVENT` (for royalty computation) |
| `COMPLIANCE_AUDIT_AGENT` | `BREACH_IMMINENT_EVENT`, `SLA_VIOLATION_EVENT`, `HIGH_RISK_CONTRACT_EVENT` |
| `TRUST_SAFETY_AGENT` | `BREACH_IMMINENT_EVENT`, `FRAUDULENT_SIGNATURE_ATTEMPT_EVENT` |
| `RANK_ENGINE` | `CONTRACT_COMPLETED_EVENT` → ranking signal for both parties |
| `XP_ENGINE` | `CONTRACT_COMPLETED_EVENT` → XP award |
| `NOTIFICATION_AGENT` | All state change events, obligation due alerts, renewal alerts |
| `OBSERVABILITY_AGENT` | All health, drift, and latency metrics |
| `FEATURE_STORE_AGENT` | Feature vectors from contract execution (Section 12) |

### Event Triggers Emitted

| Event | Topic |
|---|---|
| `CONTRACT_CREATED_EVENT` | `contract.lifecycle.created` |
| `CONTRACT_SIGNED_EVENT` | `contract.lifecycle.signed` |
| `CONTRACT_ACTIVATED_EVENT` | `contract.lifecycle.activated` |
| `CONTRACT_SUSPENDED_EVENT` | `contract.lifecycle.suspended` |
| `CONTRACT_BREACHED_EVENT` | `contract.lifecycle.breached` |
| `CONTRACT_TERMINATED_EVENT` | `contract.lifecycle.terminated` |
| `CONTRACT_RENEWED_EVENT` | `contract.lifecycle.renewed` |
| `OBLIGATION_DUE_ALERT_EVENT` | `contract.obligation.due` |
| `BREACH_RISK_ALERT_EVENT` | `contract.breach.risk` |
| `BREACH_IMMINENT_EVENT` | `contract.breach.imminent` |
| `SLA_VIOLATION_EVENT` | `contract.sla.violation` |
| `CONTRACT_INPUT_REJECTED_EVENT` | `contract.input.rejected` |
| `CRITICAL_DATA_INTEGRITY_ALERT` | `platform.integrity.critical` |

> All events schema-registered in `EVENT_SCHEMA_REGISTRY` (Avro format, versioned)

---

## 1️⃣2️⃣ Passive Intelligence Compatibility *(Feature Store Emission)*

On every significant contract lifecycle event, emit to `FEATURE_STORE_AGENT`:

```json
{
  "user_id":          "party_a_id OR party_b_id",
  "feature_name":     "contract_lifecycle_event",
  "feature_value": {
    "event_type":     "ENUM (CREATED | SIGNED | ACTIVE | BREACHED | COMPLETED)",
    "contract_type":  "contract_type",
    "health_score":   "contract_health_score",
    "breach_risk":    "breach_risk_score",
    "domain_track":   "domain_track",
    "duration_days":  "contract_duration_in_days"
  },
  "timestamp":        "event_timestamp_utc",
  "source_agent":     "CONTRACT_LIFECYCLE_AGENT"
}
```

**Additional Feature Signals:**

- `contract_completion_rate` — rolling ratio of completed vs. breached contracts per party
- `avg_obligation_fulfillment_days` — average days before due date obligations are completed (earliness signal)
- `contract_reliability_score` — composite: health scores averaged across last 5 contracts
- `sla_track_record` — rolling SLA compliance rate per vendor/trainer party

---

## 1️⃣3️⃣ Innovation Economy Compatibility

`CONTRACT_LIFECYCLE_AGENT` governs `INNOVATION_LICENSE` contract type — it is the **legal enforcement arm** of the Innovation Economy.

### On INNOVATION\_LICENSE Contract ACTIVE event, emit:

```json
{
  "contract_id":        "UUID",
  "asset_id":           "UUID — licensed IP asset",
  "licensor_id":        "party_a_id (creator)",
  "licensee_id":        "party_b_id",
  "royalty_rate":       "FLOAT from contract terms",
  "license_scope":      "STRING — geographic + usage scope",
  "effective_date":     "ISO 8601",
  "expiry_date":        "ISO 8601",
  "economic_signal":    "financial_value_inr"
}
```

**Compatibility Chain:**
- `ROYALTY_WALLET_AGENT` only computes royalties for assets covered by an **ACTIVE** `INNOVATION_LICENSE` contract
- `COPY_DETECTION_ENGINE` checks `originality_score` — below 0.30 blocks license contract ACTIVE state
- `IDEA_DNA_AGENT` provides `SIMILARITY_HASH` used in contract de-duplication validation

### Originality Gate on INNOVATION\_LICENSE *(Hard Enforcement)*

| Originality Score | Contract State Action |
|---|---|
| `≥ 0.80` | ✅ Proceeds to SIGNED → ACTIVE normally |
| `0.60 – 0.79` | ⚠️ Flagged in REVIEW state; compliance review required before ACTIVE |
| `0.30 – 0.59` | 🔶 HELD in REVIEW; `COPY_DISPUTE_EVENT` emitted; manual resolution required |
| `< 0.30` | ⛔ **REJECTED** — contract creation blocked; `COPY_DETECTED_BLOCK_EVENT` emitted |

---

## 1️⃣4️⃣ Growth Engine Hook

On `CONTRACT_COMPLETED_EVENT` (when a contract reaches TERMINATED with all obligations FULFILLED or reaches EXPIRED with `sla_compliance_rate ≥ 0.90`):

### `RANK_UPDATE_EVENT`
```json
{
  "actor_id":       "party_a_id AND party_b_id (both rewarded)",
  "rank_signal":    "contract_health_score × financial_value_weight",
  "rank_domain":    "domain_track",
  "trigger_source": "CONTRACT_LIFECYCLE_AGENT"
}
```

### `XP_UPDATE_EVENT`
```json
{
  "actor_id":     "party_a_id OR party_b_id",
  "xp_delta":     "contract_type XP table (see below)",
  "xp_source":    "CONTRACT_COMPLETED"
}
```

**XP Award Table by Contract Type:**

| Contract Type | XP Award |
|---|---|
| `INTERNSHIP` | +150 XP per party |
| `EMPLOYMENT` | +300 XP |
| `TRAINER_SERVICE` | +200 XP |
| `COURSE_LICENSE` | +100 XP |
| `PROJECT_EXECUTION` | +120 XP |
| `GOVT_SKILL_PROGRAM` | +500 XP (institutional impact weight) |
| `INNOVATION_LICENSE` | +250 XP |
| `FRANCHISE` | +400 XP |

### `SHARE_TRIGGER_EVENT` *(Conditional)*
Triggered only if: `financial_value_inr > SHARE_THRESHOLD` (from `POLICY_REGISTRY`) AND party has `SHARE_CONSENT = TRUE`

---

## 1️⃣5️⃣ Performance Monitoring

### Metrics Exposed *(Prometheus + Grafana)*

| Metric | Type | Labels |
|---|---|---|
| `contract_events_total` | Counter | tenant, domain, contract\_type, event\_type |
| `contract_state_transitions_total` | Counter | from\_state, to\_state |
| `contract_health_score_avg` | Gauge | rolling 1-hour avg per domain |
| `breach_risk_score_avg` | Gauge | rolling 1-hour avg |
| `active_contracts_count` | Gauge | per tenant, per domain |
| `obligation_overdue_count` | Gauge | per tenant |
| `sla_violation_rate` | Gauge | violations / total SLA contracts |
| `contract_lifecycle_latency_ms` | Histogram | p50, p95, p99 |
| `contract_model_drift_psi` | Gauge | PSI from drift monitor |
| `contract_dlq_depth` | Gauge | dead letter queue |
| `contract_write_errors` | Counter | — |
| `pending_signature_age_hours_max` | Gauge | longest unsigned contract age |

### Alerting Rules

| Condition | Priority | Escalate To |
|---|---|---|
| `breach_risk_score_avg > 0.60` sustained 10 min | **P1** | `TRUST_SAFETY_AGENT` |
| `sla_violation_rate > 0.10` | **P1** | `COMPLIANCE_AUDIT_AGENT` |
| `contract_model_drift_psi > 0.25` | P2 | `OBSERVABILITY_AGENT` |
| p99 latency > 1,000ms for 5 consecutive minutes | P2 | `DEVOPS_AGENT` |
| `contract_dlq_depth > 200` | **P1** | `PLATFORM_SUPER_ADMIN` |
| `pending_signature_age_hours_max > 72` | P2 | `NOTIFICATION_AGENT` |
| `contract_health_score_avg < 0.55` | P2 | `COMPLIANCE_AUDIT_AGENT` |

**Integration:** `OBSERVABILITY_AGENT` · Jaeger trace ID on every contract event span · Loki stream: `contract_lifecycle_agent_logs`

---

## 1️⃣6️⃣ Database Schema *(Locked — Add-Only Migrations)*

```sql
-- TABLE: contracts (APPEND-ONLY versioned records)
CREATE TABLE contracts (
  contract_id          UUID NOT NULL,
  contract_version     INTEGER NOT NULL DEFAULT 1,
  tenant_id            UUID NOT NULL,            -- PARTITION KEY
  contract_type        VARCHAR(30) NOT NULL,
  state                VARCHAR(30) NOT NULL,
  party_a_id           UUID NOT NULL,
  party_b_id           UUID NOT NULL,
  domain_track         VARCHAR(30) NOT NULL,
  template_id          UUID NOT NULL,
  effective_date       DATE NOT NULL,
  expiry_date          DATE NOT NULL,
  governing_law        VARCHAR(200) NOT NULL,
  financial_value_inr  DECIMAL(18,4),
  royalty_rate         DECIMAL(5,4),
  renewal_policy       VARCHAR(20) NOT NULL DEFAULT 'NONE',
  renewal_notice_days  INTEGER,
  confidentiality_level VARCHAR(20) NOT NULL DEFAULT 'INTERNAL',
  parent_contract_id   UUID,
  contract_health_score DECIMAL(5,4),
  breach_risk_score    DECIMAL(5,4),
  sla_compliance_rate  DECIMAL(5,4),
  risk_tier            VARCHAR(20),
  model_version        VARCHAR(50) NOT NULL,
  audit_reference      UUID NOT NULL,
  document_hash        VARCHAR(64),              -- SHA-256 of stored document
  created_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  PRIMARY KEY (contract_id, contract_version)   -- versioned PK
  -- NO UPDATE COLUMN — append-only by design
);

-- TABLE: contract_obligations
CREATE TABLE contract_obligations (
  obligation_id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  contract_id          UUID NOT NULL,
  contract_version     INTEGER NOT NULL,
  tenant_id            UUID NOT NULL,
  obligation_type      VARCHAR(30) NOT NULL,
  description          TEXT NOT NULL,
  due_date             DATE NOT NULL,
  responsible_party    VARCHAR(20) NOT NULL,
  status               VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  verification_method  VARCHAR(30) NOT NULL,
  weight               DECIMAL(5,4) NOT NULL,
  fulfilled_at         TIMESTAMPTZ,
  fulfilled_by         UUID,
  created_at           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- TABLE: contract_signatures
CREATE TABLE contract_signatures (
  signature_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  contract_id          UUID NOT NULL,
  contract_version     INTEGER NOT NULL,
  tenant_id            UUID NOT NULL,
  signer_id            UUID NOT NULL,
  signer_role          VARCHAR(50) NOT NULL,
  signature_hash       VARCHAR(512) NOT NULL,    -- RSA-4096 signature
  signed_at_utc        TIMESTAMPTZ NOT NULL,
  ip_address_hash      VARCHAR(64) NOT NULL,
  device_fingerprint   VARCHAR(128),
  verified             BOOLEAN NOT NULL DEFAULT FALSE,
  verified_at          TIMESTAMPTZ
);

-- TABLE: contract_state_log (IMMUTABLE)
CREATE TABLE contract_state_log (
  log_id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  contract_id          UUID NOT NULL,
  tenant_id            UUID NOT NULL,
  from_state           VARCHAR(30),
  to_state             VARCHAR(30) NOT NULL,
  transition_reason    TEXT,
  actor_id             UUID NOT NULL,
  model_version        VARCHAR(50) NOT NULL,
  confidence_score     DECIMAL(5,4),
  breach_risk_score    DECIMAL(5,4),
  anomaly_flags        TEXT[],
  audit_reference      UUID NOT NULL,
  created_at           TIMESTAMPTZ NOT NULL DEFAULT NOW()
  -- Row Level Security: NO DELETE, NO UPDATE for any role
);

-- TABLE: contract_sla_events
CREATE TABLE contract_sla_events (
  sla_event_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  contract_id          UUID NOT NULL,
  tenant_id            UUID NOT NULL,
  sla_term_id          UUID NOT NULL,
  violation_flag       BOOLEAN NOT NULL DEFAULT FALSE,
  violation_severity   VARCHAR(20),
  measured_value       DECIMAL(18,4),
  threshold_value      DECIMAL(18,4),
  measured_at          TIMESTAMPTZ NOT NULL,
  created_at           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

**Indexes:**
```sql
CREATE INDEX ON contracts(tenant_id, state, effective_date);
CREATE INDEX ON contracts(contract_id, contract_version);
CREATE INDEX ON contracts(party_a_id, tenant_id);
CREATE INDEX ON contracts(party_b_id, tenant_id);
CREATE INDEX ON contracts(state) WHERE state IN ('ACTIVE','SUSPENDED','BREACHED');
CREATE INDEX ON contract_obligations(contract_id, status, due_date);
CREATE INDEX ON contract_state_log(contract_id, created_at);
CREATE INDEX ON contract_sla_events(contract_id, violation_flag, measured_at);
```

**Partitioning:**
- `contracts` partitioned by RANGE(`effective_date`) — quarterly partitions
- `contract_state_log` partitioned by HASH(`tenant_id`) — 16 hash buckets
- `contract_sla_events` partitioned by RANGE(`measured_at`) — monthly partitions

---

## 1️⃣7️⃣ API Contract *(Locked — REST + OpenAPI 3.1)*

> All endpoints are **internal service-to-service only** — not exposed via Kong to public.

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| `POST` | `/internal/contracts/create` | Create new contract (DRAFT state) | Service JWT + `Idempotency-Key` header |
| `POST` | `/internal/contracts/{id}/submit-review` | DRAFT → REVIEW | JWT — initiating party role |
| `POST` | `/internal/contracts/{id}/approve` | REVIEW → PENDING\_SIGNATURE | JWT — `COMPLIANCE_AUDIT_AGENT` or Compliance Admin |
| `POST` | `/internal/contracts/{id}/sign` | Record party signature | JWT — party identity + `ESIGN_SERVICE` token |
| `GET` | `/internal/contracts/{id}?tenant_id={tid}` | Read contract record (current version) | JWT — contract party OR compliance role |
| `GET` | `/internal/contracts/{id}/history` | Full version history | JWT — compliance role only |
| `GET` | `/internal/contracts/{id}/obligations` | Obligation status list | JWT — contract party |
| `POST` | `/internal/contracts/{id}/suspend` | ACTIVE → SUSPENDED | JWT — Platform Admin OR `COMPLIANCE_AUDIT_AGENT` + reason required |
| `POST` | `/internal/contracts/{id}/terminate` | Any state → TERMINATED | JWT — Compliance Admin OR Platform Super Admin (human-supervised) |
| `POST` | `/internal/contracts/{id}/renew` | EXPIRED → RENEWED | JWT — initiating party + renewal policy check |
| `GET` | `/internal/contracts/{id}/health` | Real-time health + breach risk scores | JWT — contract party |
| `GET` | `/internal/contracts/health` | Agent liveness + readiness | None (internal cluster only) |

**All API Calls:**
- Traced via Jaeger (`trace_id` in headers)
- Rate limited at Kong (Section 6)
- Logged in `contract_state_log`
- Response time SLA: **500ms p95**
- Distributed Redis lock required on all state-mutating calls

---

## 1️⃣8️⃣ Versioning Policy *(Add-Only — Backward Compatible)*

### Agent Version Scheme: `CONTRACT_LIFECYCLE_AGENT_v{MAJOR}.{MINOR}.{PATCH}`

| Bump | Trigger |
|---|---|
| MAJOR | Breaking change in FSM states, input/output schema, or contract type ENUM (requires 90-day dual-version support) |
| MINOR | New optional field, new ML model, new contract type, new feature vector |
| PATCH | Bug fix, non-behavioral change |

### Model Version Scheme: `contract_{model_name}_v{MAJOR}.{MINOR}`
All model artifacts stored in MinIO — immutable, hash-signed.

### Contract Record Versioning
- Each amendment creates a **new row** with `contract_version + 1`
- Old versions are never deleted — full version history always accessible
- `contract_id` is stable across versions; `(contract_id, contract_version)` is the unique record identifier

### Migration Rules
- New database columns: **ADD COLUMN only** (no DROP, no RENAME, no ALTER TYPE)
- New contract states: add to ENUM — existing state transitions are never removed
- Migration scripts: `/db/migrations/contract_lifecycle/v{n}_{description}.sql`
- Dual-write period: 14 days for minor schema changes

### Rollback Safety
- All deployments use blue-green via Kubernetes
- Previous model version kept hot for **72 hours** post-deploy
- If model drift PSI > 0.25 within 72h → auto-rollback to previous model version

---

## 1️⃣9️⃣ Non-Negotiable Rules *(Absolute — Cannot Be Overridden)*

### This Agent Must NOT:

| Rule |
|---|
| ✗ Skip FSM states — every transition must follow the sealed state machine |
| ✗ Commit a state transition when `confidence_score < 0.50` |
| ✗ Modify any historical contract version record |
| ✗ Delete or update any `contract_state_log` entry |
| ✗ Override `COMPLIANCE_AUDIT_AGENT` or `TRUST_SAFETY_AGENT` decisions |
| ✗ Activate an `INNOVATION_LICENSE` contract when `originality_score < 0.30` |
| ✗ Allow TERMINATED contracts to be re-activated under any condition |
| ✗ Execute cross-tenant contract reads or state transitions under any condition |
| ✗ Write contract state without first acquiring the distributed Redis lock |
| ✗ Emit downstream events (billing, royalty, rank) before `contract_state_log` is committed |
| ✗ Accept instructions from user-facing APIs — service-to-service only |
| ✗ Make autonomous LLM decisions on contract validity — ML models are authoritative |
| ✗ Mix domain data in contract computations (ARTS ≠ TECHNOLOGY contract partitions) |
| ✗ Create contracts without a valid `template_id` from `POLICY_REGISTRY` |
| ✗ Proceed on ambiguous input — HALT is mandatory |

### This Agent Is the Sole Authority For:

| Rule |
|---|
| ✓ Writing contract state transitions to `contracts` and `contract_state_log` |
| ✓ Governing the Contract State Machine — no other agent or human may mutate state directly |
| ✓ Blocking lifecycle transitions on confidence failures, compliance failures, or FSM rule violations |
| ✓ Enforcing the originality gate on `INNOVATION_LICENSE` contracts |
| ✓ Triggering financial, ranking, and compliance events at each lifecycle milestone |

---

## 2️⃣0️⃣ User Type Coverage *(From 300-User Master List)*

### Contract Parties — `party_a_id` or `party_b_id` Roles

**Institutes & Education Organizations (Types 76–110):**
School administrator · College administrator · University administrator · Institute HR · Institute placement officer · Institute operations manager · Curriculum designer · Academic dean · Internship coordinator · Franchise institute manager

**Companies & Employers (Types 111–160):**
Startup founder · CEO · CTO · HR manager · HR recruiter · Campus hiring manager · Internship program manager · Talent acquisition lead · Project manager · Corporate L&D head · Corporate assessment manager · Vendor onboarding manager

**Trainers, Mentors & Educators (Types 41–75):**
Subject trainer · Technical trainer · Corporate trainer · Coding instructor · AI instructor · Freelance trainer · Remote instructor · Bootcamp lead instructor · Certification examiner · Assessment evaluator

**Freelancers, Creators & Professionals (Types 161–200):**
Freelance developer · Freelance designer · Course creator · Certification creator · Prompt engineer · Tool builder · Plugin developer · API provider · SaaS creator

**Students & Learners (Types 1–40):**
Internship seeker · Apprenticeship learner · Bootcamp student — as `party_b_id` in INTERNSHIP contracts

**Government, NGOs & Policy Bodies (Types 201–230):**
Government skill department officer · NSDC official · AICTE official · Skill mission officer · NGO founder — as parties in `GOVT_SKILL_PROGRAM` and `ASSESSMENT_PARTNER` contracts

### Governance — Compliance Readers Only
Platform super admin · Compliance admin · Audit admin · Data protection officer · AI compliance auditor · AI safety officer · Compliance inspector · Education auditor

---

## 2️⃣1️⃣ Dojo Intelligence Engine Integration *(Unique to This Agent)*

`CONTRACT_LIFECYCLE_AGENT` is the **first agent** on the Ecoskiller platform to integrate with the `DOJO_INTELLIGENCE_ENGINE` (Howard Gardner's 8-Intelligence profiling system):

### For Training, Mentorship & Educator Contracts

When `contract_type ∈ [TRAINER_SERVICE | MENTORSHIP | COURSE_LICENSE]`, the agent optionally requests an **intelligence profile match score** from `DOJO_INTELLIGENCE_ENGINE`:

```json
{
  "trainer_intelligence_profile_id": "UUID — trainer's 8-intelligence scores",
  "student_domain_intelligence_needs": {
    "linguistic": 0.0–1.0,
    "logical": 0.0–1.0,
    "spatial": 0.0–1.0,
    "interpersonal": 0.0–1.0,
    "intrapersonal": 0.0–1.0,
    "naturalistic": 0.0–1.0
  },
  "match_score": "FLOAT 0.0–1.0 — compatibility of trainer intelligence to student needs"
}
```

**Usage in ML Models:**
- `intelligence_profile_match_score` is a **feature** in `CONTRACT_HEALTH_SCORE_MODEL`
- Higher match scores are associated with lower breach probability (obligation fulfillment improves when trainer–student intelligence alignment is high)
- Used in `CONTRACT_RENEWAL_ELIGIBILITY_MODEL` — high match score increases renewal probability

**Emitted to `FEATURE_STORE_AGENT`:**
```json
{
  "feature_name": "intelligence_contract_match",
  "feature_value": "intelligence_profile_match_score",
  "contract_type": "TRAINER_SERVICE",
  "domain_track":  "domain_track"
}
```

---

## 2️⃣2️⃣ Deployment Specification

```yaml
namespace:      ecoskiller-intelligence
service_name:   contract-lifecycle-agent
image:          ecoskiller/contract-lifecycle-agent:v1.0.0
replicas:       min: 3  max: 40  (HPA)
resources:
  cpu:          request: 500m  limit: 2000m
  memory:       request: 512Mi limit: 2Gi
probes:
  liveness:     GET /internal/contracts/health → 200 OK  (interval: 30s)
  readiness:    GET /internal/contracts/health?ready=true (interval: 10s)
secrets:        injected via Kubernetes Secrets (no hardcoded credentials)
```

**Required Environment Variables:**
```
DB_HOST · DB_PORT · DB_NAME · DB_USER · DB_PASSWORD_SECRET
REDIS_HOST · REDIS_PORT · REDIS_STREAM_NAME
REDIS_LOCK_TTL_SECONDS          (default: 30)
MODEL_REGISTRY_URL               (MinIO endpoint)
KEYCLOAK_ISSUER_URL
POLICY_REGISTRY_URL
ESIGN_SERVICE_URL
DOJO_INTELLIGENCE_ENGINE_URL
OBSERVABILITY_AGENT_ENDPOINT
TRUST_SAFETY_AGENT_ENDPOINT
COMPLIANCE_AUDIT_AGENT_ENDPOINT
FEATURE_STORE_AGENT_ENDPOINT
NOTIFICATION_AGENT_ENDPOINT
MINIO_CONTRACTS_BUCKET           (tenant-namespaced)
```

**Contract Gate Dependencies** *(must be satisfied before deployment)*:
- ✅ Lane A: `identity_ready` · `rbac_ready` · `event_schema_ready`
- ✅ Lane B: `db_ready` · `search_ready`
- ✅ Lane C: `api_contract_ready` (MARKETPLACE · HIRING · PROJECT · BILLING · ESIGN)
- ✅ Lane D: `governance_ready` (COMPLIANCE_AUDIT_AGENT · NOTIFICATION_AGENT online)
- ✅ Lane F: `ai_ready` (FEATURE_STORE · DOJO_INTELLIGENCE_ENGINE online)

---

## 🔒 Final Lock Declaration

| Checkpoint | Status |
|---|---|
| Agent identity declared and locked | ✔ |
| 12 contract types defined and locked | ✔ |
| Input/output contracts strictly defined | ✔ |
| Contract State Machine sealed (9 states, deterministic FSM) | ✔ |
| 5 ML models specified with features, training policy, and drift detection | ✔ |
| AI scope bounded (semantic assist only, no decision authority) | ✔ |
| Breach prediction model with tiered alert thresholds | ✔ |
| SLA violation detection with hard rules | ✔ |
| Originality gate enforced for INNOVATION\_LICENSE contracts | ✔ |
| Tenant and domain isolation enforced at every layer | ✔ |
| Distributed lock enforcement on all state transitions | ✔ |
| Audit trail: append-only, immutable, 10-year retention | ✔ |
| Failure policies: no silent failures, all paths covered | ✔ |
| Inter-agent dependency map complete (13 upstream · 8 downstream) | ✔ |
| Passive intelligence (feature store) emission defined | ✔ |
| Innovation economy (ROYALTY\_WALLET, COPY\_DETECTION, IDEA\_DNA) wired | ✔ |
| Growth engine hooks (RANK · XP · SHARE) with contract-type XP table | ✔ |
| Performance monitoring with alerting thresholds set | ✔ |
| Database schema locked with add-only migration policy | ✔ |
| API contracts defined (internal service-to-service only) | ✔ |
| Versioning and rollback policy defined | ✔ |
| Non-negotiable rules enumerated | ✔ |
| 300-user type coverage mapped | ✔ |
| Dojo Intelligence Engine integration specified | ✔ |
| Deployment specification complete | ✔ |
| Contract gate dependencies verified | ✔ |

> **Violation of any section above:**
> → `STOP EXECUTION`
> → `REPORT NON-COMPLIANT AGENT DEPLOYMENT`
> → `NO CONTRACT STATE TRANSITIONS PERMITTED`

---

*ECOSKILLER ANTIGRAVITY · CONTRACT\_LIFECYCLE\_AGENT · v1.0.0 · SEALED*
