# 🔒 ROYALTY\_WALLET\_AGENT — Sealed Agent Specification

> **Platform:** Ecoskiller Antigravity · **Version:** v1.0.0 · **Status:** PRODUCTION-LOCKED
> **Mutation Policy:** ADD-ONLY via version bump · **Interpretation Authority:** NONE
> **Creative Deviation:** FORBIDDEN · **Execution Authority:** Human Declaration Only

---

## 1️⃣ Agent Identity *(Mandatory — Non-Negotiable)*

| Field | Value |
|---|---|
| `AGENT_NAME` | `ROYALTY_WALLET_AGENT` |
| `SYSTEM_ROLE` | ML Intelligence + Safety Owner — Royalty Computation, Wallet Ledger Management, and Innovation Economy Enforcement |
| `PRIMARY_DOMAIN` | Innovation Economy · Royalty Attribution · Wallet Integrity |
| `EXECUTION_MODE` | Deterministic + Validated + Append-Only |
| `DATA_SCOPE` | Royalty events, idea transactions, wallet credits/debits, usage attribution records, originality scores |
| `TENANT_SCOPE` | Strict Multi-Tenant Isolation (No cross-tenant wallet reads) |
| `FAILURE_POLICY` | HALT on ambiguity · STOP on confidence below threshold · ESCALATE on fraud signal · NO silent failures |

### Platform Context *(Binding)*

| Field | Value |
|---|---|
| Platform | Ecoskiller Antigravity |
| Architecture | Microservices + Event-Driven (Redis Streams) |
| Scale Target | 10M–100M concurrent users |
| ML Ratio | 70–80% Traditional ML (XGBoost, Regression, Time-Series) |
| AI Ratio | 20–30% LLM-assisted (semantic scoring only, no decisions) |
| Security Model | Zero-Trust, Multi-Tenant Isolation |
| Data Policy | Append-Only Audit Trail (immutable) |
| Stack Lock | Python 3.11 · FastAPI · PostgreSQL 15 · Redis 7 · OpenSearch 2.x · Kubernetes · Keycloak · Kong OSS |
| Mutation Policy | Add-Only Versioned — No retroactive modification |

> ⛔ Absence of any mandatory section → **STOP EXECUTION**

---

## 2️⃣ Purpose Declaration

### Problem Solved

Ecoskiller Antigravity enables creators — trainers, course authors, idea contributors, content producers, and tool builders — to earn royalties when their intellectual assets are accessed, cloned, licensed, or monetized by other platform participants. Without a governed, ML-verified wallet agent, royalty attribution is susceptible to fraud, duplication errors, and cross-tenant contamination.

`ROYALTY_WALLET_AGENT` is the **singular authority** responsible for:

- **(a)** Computing royalty amounts using ML models with verified confidence
- **(b)** Writing credited earnings to tenant-isolated wallet ledgers
- **(c)** Detecting fraudulent or duplicate royalty claims via anomaly detection
- **(d)** Enforcing originality thresholds before any royalty is committed
- **(e)** Emitting downstream events to `ROYALTY_ENGINE`, `BILLING_AGENT`, and `INNOVATION_ECONOMY_ORCHESTRATOR`
- **(f)** Operating as the **Safety Owner** — blocking, flagging, and escalating any royalty transaction that fails integrity checks

### Input Consumed

- Idea usage events from `IDEA_DNA_AGENT`
- Similarity + originality scores from `COPY_DETECTION_ENGINE`
- Content view / license / clone trigger events from `MARKETPLACE_AGENT`
- Wallet credit/debit requests from `BILLING_AGENT`
- User role and tier context from `IDENTITY_SERVICE`
- Royalty policy rules from `POLICY_REGISTRY` (versioned)

### Output Produced

- Committed wallet credit records (append-only)
- Royalty computation result objects (with confidence + audit reference)
- Fraud/anomaly alert events
- Feature vectors emitted to `FEATURE_STORE_AGENT`
- Downstream trigger events to `BILLING_AGENT`, `RANK_ENGINE`, `XP_ENGINE`

### Agent Dependencies

| Direction | Agents |
|---|---|
| **Downstream Dependents** | `BILLING_AGENT` · `RANK_ENGINE` · `XP_ENGINE` · `INNOVATION_ECONOMY_ORCHESTRATOR` · `OBSERVABILITY_AGENT` · `COMPLIANCE_AUDIT_AGENT` |
| **Upstream Feeders** | `IDEA_DNA_AGENT` · `COPY_DETECTION_ENGINE` · `MARKETPLACE_AGENT` · `IDENTITY_SERVICE` · `POLICY_REGISTRY` · `FEATURE_STORE_AGENT` |

---

## 3️⃣ Input Contract *(Strict — No Null Tolerance)*

### Required Fields

```json
{
  "required_fields": [
    "event_id",            // UUID — globally unique royalty trigger event
    "tenant_id",           // UUID — strict tenant scope (hard boundary)
    "actor_id",            // UUID — creator / rights holder
    "consumer_id",         // UUID — user or org consuming the asset
    "asset_id",            // UUID — course, idea, tool, badge, content block
    "asset_type",          // ENUM: [COURSE | IDEA | TOOL | BADGE | CONTENT | TEMPLATE | PLUGIN]
    "trigger_type",        // ENUM: [VIEW | LICENSE | CLONE | EMBED | RESELL | FORK | CITE]
    "originality_score",   // FLOAT 0.0–1.0 — from COPY_DETECTION_ENGINE
    "similarity_hash",     // STRING — fingerprint from IDEA_DNA_AGENT
    "policy_version",      // STRING — e.g. "royalty_policy_v3.2"
    "event_timestamp_utc", // ISO 8601
    "domain_track"         // ENUM: [ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION]
  ]
}
```

### Optional Fields

```json
{
  "optional_fields": [
    "co_creator_ids",          // ARRAY[UUID] — for shared asset royalties
    "license_tier",            // ENUM: [FREE | BASIC | PRO | ENTERPRISE]
    "transaction_amount_inr",  // DECIMAL — if monetary transaction linked
    "parent_asset_id",         // UUID — if asset is a fork/derivative
    "xp_multiplier",           // FLOAT — growth engine modifier
    "campaign_id"              // UUID — if triggered under promotional event
  ]
}
```

### Validation Rules

- `event_id` must be UUID v4
- `tenant_id` must match `actor_id`'s registered tenant
- `consumer_id` must differ from `actor_id` (no self-royalty)
- `originality_score` range: `0.0 ≤ x ≤ 1.0`
- `event_timestamp_utc` must not be future-dated beyond +60 seconds
- `policy_version` must exist in `POLICY_REGISTRY`
- `domain_track` must match `actor_id`'s registered domain
- `asset_type` must match `asset_id` record in `ASSET_REGISTRY`

### Security Checks

- JWT validation via Keycloak (actor + consumer roles verified)
- Tenant boundary: `tenant_id` on actor must equal `tenant_id` on consumer OR explicit cross-tenant license exists
- Rate limit: max 500 royalty events per actor per minute
- IP + device fingerprint anomaly check (flag if > 3 std deviations from baseline)
- Replay attack prevention: `event_id` idempotency key (Redis TTL 24h)

### Domain Checks

- Cross-domain asset use requires explicit `DOMAIN_CROSS_LICENSE` record
- Arts domain assets: additional content moderation clearance required
- Government/NGO tier: royalty rate override from `POLICY_REGISTRY` only

### Rejection Rules

| Condition | Action |
|---|---|
| Any missing required field | `REJECT` + `LOG_VALIDATION_FAILURE` + `EMIT_ALERT` |
| Null in required field | `REJECT` (no null tolerance) |
| Malformed UUID | `REJECT` |
| Self-royalty attempt | `REJECT` + `FLAG_FRAUD_ATTEMPT` |
| Future-dated event | `REJECT` + `FLAG_ANOMALY` |
| Unknown `policy_version` | `HALT` + `ESCALATE_TO = COMPLIANCE_AUDIT_AGENT` |

---

## 4️⃣ Output Contract *(Strict)*

```json
{
  "result_object": {
    "wallet_credit_id":        "UUID — immutable ledger record",
    "actor_wallet_id":         "UUID — recipient wallet",
    "royalty_amount_computed": "DECIMAL — computed by ML royalty model",
    "royalty_currency":        "STRING — default: INR | configurable per tenant",
    "royalty_tier":            "ENUM: [MICRO | STANDARD | PREMIUM | ENTERPRISE]",
    "co_creator_splits": [
      {
        "co_creator_id":  "UUID",
        "split_percent":  "FLOAT",
        "split_amount":   "DECIMAL"
      }
    ],
    "wallet_status":           "ENUM: [CREDITED | HELD_FOR_REVIEW | BLOCKED | ESCROW]",
    "originality_gate_passed": "BOOLEAN",
    "fraud_flag":              "BOOLEAN",
    "fraud_reason":            "STRING | null",
    "policy_applied":          "STRING — version used",
    "computation_path":        "ARRAY[STRING] — model feature contributions (explainability)"
  },
  "confidence_score":          "FLOAT 0.0–1.0",
  "model_version":             "STRING — e.g. royalty_model_v4.1",
  "audit_reference":           "UUID — immutable audit trail ID",
  "next_trigger_events": [
    "WALLET_CREDITED_EVENT",
    "RANK_UPDATE_EVENT",
    "XP_UPDATE_EVENT",
    "BILLING_SYNC_EVENT",
    "ROYALTY_STATEMENT_UPDATE"
  ]
}
```

### Mandatory Output Guarantees

- Every output includes: `confidence_score` + `model_version` + `audit_reference`
- `confidence_score < 0.70` → `wallet_status = HELD_FOR_REVIEW`, ESCALATE
- `confidence_score < 0.50` → `STOP_EXECUTION`, `LOG_INCIDENT`, **NO CREDIT WRITTEN**
- `fraud_flag = TRUE` → `wallet_status = BLOCKED`, emit `FRAUD_DETECTED_EVENT`
- ALL outputs are append-only to ledger — no updates, no deletes

---

## 5️⃣ ML / AI Logic Layer *(Core Intelligence)*

### A. ML Layer — 70% · Primary Decision Engine

#### MODEL 1: `ROYALTY_COMPUTATION_MODEL`

| Property | Value |
|---|---|
| Model Type | Gradient Boosted Regression (XGBoost) |
| Purpose | Compute fair royalty amount per trigger event |
| Output | `royalty_amount_computed` (DECIMAL, INR) |
| Training Frequency | Monthly — last 90 days of committed transactions |
| Drift Detection | PSI (Population Stability Index) monitored weekly; PSI > 0.25 → model freeze + escalate |
| Minimum Samples | 1,000 transactions per domain per training cycle |
| Version Control | Immutable artifact: `royalty_model_v{major}.{minor}` stored in MinIO |

**Features Used:**

| Feature | Description |
|---|---|
| `asset_quality_score` | 0.0–1.0, from `CONTENT_QUALITY_AGENT` |
| `originality_score` | 0.0–1.0, from `COPY_DETECTION_ENGINE` |
| `asset_engagement_rate` | views\_per\_day / total\_subscribers |
| `creator_reputation_score` | from `REPUTATION_ENGINE`, L1–L7 scale |
| `trigger_type_weight` | VIEW=0.1, LICENSE=1.0, CLONE=0.5, RESELL=2.0 |
| `license_tier_multiplier` | FREE=0.0, BASIC=0.3, PRO=1.0, ENTERPRISE=3.0 |
| `domain_base_rate` | per domain from `POLICY_REGISTRY` |
| `co_creator_count` | integer |
| `asset_age_days` | integer — decay function applied |
| `campaign_active_flag` | 0/1 — promotional period boost |

---

#### MODEL 2: `FRAUD_ANOMALY_DETECTION_MODEL`

| Property | Value |
|---|---|
| Model Type | Isolation Forest + Rule-Based Ensemble |
| Purpose | Detect fraudulent royalty inflation, fake trigger floods, coordinated self-referral rings |
| Output | `fraud_flag` (BOOLEAN) + `anomaly_score` (0.0–1.0) |
| Training Frequency | Weekly (fraud patterns evolve rapidly) |
| Drift Detection | F1-score on labeled holdout set; F1 < 0.80 → model freeze + escalate |

**Features Used:**

| Feature | Description |
|---|---|
| `events_per_actor_last_1hr` | integer |
| `events_per_actor_last_24hr` | integer |
| `unique_consumers_per_asset` | integer |
| `consumer_ip_diversity_score` | 0.0–1.0 |
| `actor_consumer_graph_density` | social graph metric — ring detection |
| `event_time_clustering_score` | irregular burst patterns |
| `royalty_velocity_zscore` | z-score vs. rolling 30-day actor baseline |
| `new_account_flag` | account age < 7 days = elevated risk |

**Fraud Hard Rules** *(enforced before ML score)*:

| Condition | Action |
|---|---|
| `self_royalty_attempt` | INSTANT BLOCK + FRAUD\_FLAG |
| `event_id` replay | INSTANT BLOCK |
| `events_per_actor_last_1hr > 300` | HOLD + ALERT |
| Consumer + actor: same tenant + same device + same IP | FLAG |
| `originality_score < 0.30` AND `trigger_type = LICENSE` | BLOCK |

---

#### MODEL 3: `ROYALTY_SPLIT_MODEL`

| Property | Value |
|---|---|
| Model Type | Rule-Based Classifier + Multi-Label Assignment |
| Purpose | Compute equitable royalty split across co-creators |
| Output | `co_creator_splits[]` (split\_percent per UUID) |

**Split Rules** *(ordered priority)*:

1. Explicit split declaration by asset owner → USE AS-IS (if sum = 100%)
2. Verified contribution weights from `PROJECT_CONTRIBUTION_AGENT`
3. Default equal split if no declaration exists
4. Maximum **10 co-creators** per asset for royalty split
5. Minimum split threshold: **1%** per co-creator (below = merged to primary)

---

### B. AI Layer — 20–30% · Semantic Assist Only

**AI Usage Scope:**
- Semantic disambiguation of ambiguous `asset_type` classifications
- Natural language explanation of royalty computation for user dashboard
- Detecting subtle semantic plagiarism beyond cosine similarity *(escalates to `COPY_DETECTION_ENGINE` — AI does NOT block directly)*

**AI Must NOT:**
- Make final royalty credit decisions autonomously
- Override ML fraud scores
- Modify wallet records
- Execute outside its declared scope

**Prompt Governance:**

| Rule | Value |
|---|---|
| Prompt versioning | `prompt_royalty_v{n}` |
| Temperature | `0.0` (deterministic) |
| `top_p` | `0.95` |
| Creative interpretation | FORBIDDEN |
| Decision authority | ML models are authoritative — LLM is advisory only |
| Latency budget | 3,000ms max → timeout = fallback to ML-only path |

---

## 6️⃣ Scalability Design

| Parameter | Value |
|---|---|
| `EXPECTED_RPS` | 5,000/sec (peak: 50,000 during campaign events) |
| `LATENCY_TARGET` | p50 ≤ 150ms · p95 ≤ 400ms · p99 ≤ 800ms |
| `MAX_CONCURRENCY` | 10,000 in-flight computations |
| `QUEUE_STRATEGY` | Redis Streams (`ROYALTY_EVENT_STREAM`) — 5 consumer groups per domain track |
| `PROCESSING_MODE` | Async event-driven; synchronous fast-path for `trigger_type = LICENSE` |
| `STATELESS` | TRUE — no local state; all state in PostgreSQL + Redis |
| `IDEMPOTENCY` | `event_id` = idempotency key (Redis 24h TTL) |
| `HORIZONTAL_SCALING` | Kubernetes HPA — min 3 pods, max 50; Scale: CPU > 70% OR queue > 10,000 |
| `CIRCUIT_BREAKER` | Resilience4j pattern — 5 failures/10s → open |
| `RATE_LIMITING` | Kong OSS: 500 events/actor/min · 10,000 events/tenant/min |

---

## 7️⃣ Security Enforcement *(Zero-Trust — Non-Negotiable)*

### Tenant Isolation
- Every database query carries `tenant_id` filter — enforced at ORM layer
- No cross-tenant wallet queries permitted **under any condition**
- `tenant_id` mismatch → IMMEDIATE REJECT + `SECURITY_ALERT_EVENT` emitted
- All tenant data encrypted at rest (AES-256) and in transit (TLS 1.3)

### Domain Isolation
- Arts / Commerce / Science / Technology / Administration domains have **separate wallet ledger partitions**
- Cross-domain royalty only permitted via explicit `DOMAIN_CROSS_LICENSE` record

### Role-Based Authorization *(Keycloak RBAC)*

| Action | Permitted Roles |
|---|---|
| Trigger royalty event | `MARKETPLACE_AGENT` · `IDEA_DNA_AGENT` · `BILLING_AGENT` (service accounts only) |
| Read wallet balance | Asset owner (self) · Platform Super Admin (read-only + audit) · Compliance Admin (read-only + audit) |
| **Write wallet ledger** | **`ROYALTY_WALLET_AGENT` ONLY — no human write access** |
| Forbidden | Users reading other users' wallets · Any human writing to ledger · Any agent bypassing validation |

### Encryption
- Wallet balance fields: encrypted at column level (PostgreSQL `pgcrypto`)
- `royalty_amount_computed` in transit: encrypted payload (not just TLS)
- Model artifacts: signed + hash-verified on load

---

## 8️⃣ Audit & Traceability *(Append-Only — Immutable)*

```json
{
  "log_id":            "UUID (primary key)",
  "timestamp_utc":     "ISO 8601 — nanosecond precision",
  "tenant_id":         "UUID",
  "actor_id":          "UUID — creator receiving royalty",
  "consumer_id":       "UUID — entity triggering royalty",
  "asset_id":          "UUID",
  "event_id":          "UUID — original trigger event",
  "input_hash":        "SHA-256 of full input payload",
  "output_hash":       "SHA-256 of full output payload",
  "model_version":     "STRING",
  "prompt_version":    "STRING | null (if AI path used)",
  "decision_path":     "ARRAY[STRING] — feature contributions ranked",
  "royalty_amount":    "DECIMAL",
  "confidence_score":  "FLOAT",
  "fraud_flag":        "BOOLEAN",
  "anomaly_flags":     "ARRAY[STRING]",
  "wallet_status":     "ENUM",
  "execution_time_ms": "INTEGER",
  "agent_pod_id":      "STRING — Kubernetes pod identifier",
  "ip_address":        "STRING — consumer IP (hashed for privacy)",
  "compliance_check":  "ENUM: [PASSED | FLAGGED | BLOCKED]"
}
```

### Immutability Enforcement
- Table uses PostgreSQL INSERT-ONLY policy (no UPDATE, no DELETE)
- Row-level security: no user role can DELETE from this table
- Nightly hash-chain checkpoint published to `AUDIT_CHAIN_STORE` for tamper detection
- **Retention: 7 years** (regulatory compliance for financial records)

---

## 9️⃣ Failure Policy *(No Silent Failures — Non-Negotiable)*

| Failure Condition | Policy |
|---|---|
| Invalid / malformed input | `REJECT` → `LOG_VALIDATION_FAILURE` → Emit `ROYALTY_INPUT_REJECTED_EVENT` |
| ML model unavailable | `STOP_EXECUTION` → `LOG_INCIDENT` → `ESCALATE_TO = OBSERVABILITY_AGENT` → Retry: 5s, 30s, 120s (max 3) |
| AI (LLM) timeout > 3,000ms | `FALLBACK` to ML-only path → `LOG_AI_TIMEOUT` → Continue (non-blocking) |
| `confidence_score < 0.50` | `STOP_EXECUTION` → `LOG_LOW_CONFIDENCE` → `wallet_status = HELD_FOR_REVIEW` → `ESCALATE_TO = COMPLIANCE_AUDIT_AGENT` |
| `confidence_score` 0.50–0.70 | `wallet_status = HELD_FOR_REVIEW` → Notify creator → Human review within 24h |
| Fraud flag raised | `STOP_EXECUTION` → `wallet_status = BLOCKED` → `ESCALATE_TO = TRUST_SAFETY_AGENT` → `LOG_FRAUD_INCIDENT` (immutable) |
| Data corruption (`input_hash` mismatch) | `HALT_ALL_WALLET_WRITES` → `ESCALATE_TO = PLATFORM_SUPER_ADMIN` → Emit `CRITICAL_DATA_INTEGRITY_ALERT` |
| Tenant isolation violation | `IMMEDIATE HALT` → `SECURITY_ALERT_EVENT` → `ESCALATE_TO = SECURITY_ADMIN` → Terminate session |
| `policy_version` not found in registry | `HALT` → `ESCALATE_TO = COMPLIANCE_ADMIN` → No royalty written until resolved |
| Queue overflow (> 100k events) | `CIRCUIT_BREAKER OPEN` → Shed non-urgent events → Alert `DEVOPS_AGENT` |

**Escalation Targets:**

| Agent | Handles |
|---|---|
| `COMPLIANCE_AUDIT_AGENT` | Policy and confidence failures |
| `TRUST_SAFETY_AGENT` | Fraud and abuse flags |
| `OBSERVABILITY_AGENT` | Model and infrastructure failures |
| `SECURITY_ADMIN` | Tenant/domain isolation violations |
| `PLATFORM_SUPER_ADMIN` | Data integrity and catastrophic failures |

**Retry Policy:** `MAX_RETRIES = 3` · Backoff = Exponential (5s → 30s → 120s)
After 3 failures → `DEAD_LETTER_QUEUE` (`royalty_dlq`) + manual escalation ticket

---

## 🔟 Inter-Agent Dependency Map

### Upstream Agents

| Agent | Provides |
|---|---|
| `IDEA_DNA_AGENT` | `IDEA_VECTOR` + `SIMILARITY_HASH` + `ORIGINALITY_SCORE` |
| `COPY_DETECTION_ENGINE` | `originality_score` + `plagiarism_flag` |
| `MARKETPLACE_AGENT` | Asset trigger events (view / license / clone) |
| `BILLING_AGENT` | `license_tier` + `transaction_amount_inr` |
| `IDENTITY_SERVICE` | Actor role + tenant + `domain_track` |
| `POLICY_REGISTRY` | Royalty rates + domain base rates |
| `FEATURE_STORE_AGENT` | Historical feature vectors for ML inference |
| `CONTENT_QUALITY_AGENT` | `asset_quality_score` |

### Downstream Agents

| Agent | Receives |
|---|---|
| `BILLING_AGENT` | `BILLING_SYNC_EVENT` (wallet → payout pipeline) |
| `RANK_ENGINE` | `RANK_UPDATE_EVENT` |
| `XP_ENGINE` | `XP_UPDATE_EVENT` |
| `INNOVATION_ECONOMY_ORCHESTRATOR` | `ROYALTY_COMMITTED_EVENT` |
| `OBSERVABILITY_AGENT` | All health + drift metrics |
| `COMPLIANCE_AUDIT_AGENT` | `HELD_FOR_REVIEW` + policy escalations |
| `TRUST_SAFETY_AGENT` | `FRAUD_DETECTED_EVENT` |
| `FEATURE_STORE_AGENT` | Feature vectors from this agent's execution |

### Event Triggers Emitted

| Event | Topic |
|---|---|
| `WALLET_CREDITED_EVENT` | `royalty.wallet.credited` |
| `WALLET_BLOCKED_EVENT` | `royalty.wallet.blocked` |
| `ROYALTY_HELD_EVENT` | `royalty.wallet.held` |
| `FRAUD_DETECTED_EVENT` | `royalty.fraud.detected` |
| `ROYALTY_INPUT_REJECTED_EVENT` | `royalty.input.rejected` |
| `CRITICAL_DATA_INTEGRITY_ALERT` | `platform.integrity.critical` |

> All events schema-registered in `EVENT_SCHEMA_REGISTRY` (Avro format, versioned)

---

## 1️⃣1️⃣ Passive Intelligence Compatibility *(Feature Store Emission)*

On every successful royalty computation, emit to `FEATURE_STORE_AGENT`:

```json
{
  "user_id":        "actor_id",
  "feature_name":   "royalty_earned_event",
  "feature_value": {
    "amount":       "royalty_amount_computed",
    "trigger_type": "trigger_type",
    "asset_type":   "asset_type",
    "confidence":   "confidence_score",
    "domain_track": "domain_track",
    "tier":         "royalty_tier"
  },
  "timestamp":      "event_timestamp_utc",
  "source_agent":   "ROYALTY_WALLET_AGENT"
}
```

**Additional Feature Signals:**
- `royalty_velocity_30d` — rolling sum per creator, per domain
- `fraud_attempt_flag` — binary, used in `REPUTATION_ENGINE`
- `wallet_health_score` — composite: earnings regularity + no blocks

---

## 1️⃣2️⃣ Innovation Economy Compatibility

`ROYALTY_WALLET_AGENT` is the **execution arm** of the Innovation Economy.

### Emit on Every Asset Royalty Computation

```json
{
  "asset_id":       "UUID",
  "idea_vector":    "FLOAT[] — semantic embedding (from IDEA_DNA_AGENT)",
  "royalty_event":  "trigger_type",
  "economic_signal":"royalty_amount_computed",
  "timestamp_utc":  "ISO 8601"
}
```

- `SIMILARITY_HASH` — passed through from `COPY_DETECTION_ENGINE`, not modified
- `ORIGINALITY_SCORE` — consumed as gating input, emitted as part of audit record

### Originality Gate *(Hard Enforcement)*

| Score Range | Action |
|---|---|
| `≥ 0.80` | ✅ Full royalty rate applied |
| `0.60 – 0.79` | ⚠️ 70% of computed rate |
| `0.30 – 0.59` | 🔶 30% of computed rate + `REVIEW_FLAG` |
| `< 0.30` | ⛔ **ROYALTY BLOCKED** + `COPY_DISPUTE_EVENT` emitted |

---

## 1️⃣3️⃣ Growth Engine Hook

On every confirmed `WALLET_CREDITED_EVENT`, trigger:

### `RANK_UPDATE_EVENT`
```json
{
  "actor_id":       "UUID",
  "rank_signal":    "royalty_amount_computed",
  "rank_domain":    "domain_track",
  "trigger_source": "ROYALTY_WALLET_AGENT"
}
```

### `XP_UPDATE_EVENT`
```json
{
  "actor_id":     "UUID",
  "xp_delta":     "royalty_tier → XP_MULTIPLIER applied",
  "xp_source":    "ROYALTY_EARNED",
  "xp_multiplier":"optional from input OR policy default"
}
```

**XP Multiplier Table** *(from `POLICY_REGISTRY`)*:

| Tier | XP per Event |
|---|---|
| MICRO | +5 XP |
| STANDARD | +20 XP |
| PREMIUM | +75 XP |
| ENTERPRISE | +200 XP |

### `SHARE_TRIGGER_EVENT` *(Conditional)*
Triggered only if `royalty_amount_computed > SHARE_THRESHOLD` AND `actor.SHARE_CONSENT = TRUE`

---

## 1️⃣4️⃣ Performance Monitoring

### Metrics Exposed *(Prometheus + Grafana)*

| Metric | Type | Labels |
|---|---|---|
| `royalty_events_total` | Counter | tenant, domain, trigger\_type |
| `royalty_amount_total_inr` | Sum Gauge | tenant, domain |
| `royalty_computation_latency_ms` | Histogram | p50, p95, p99 |
| `royalty_confidence_score_avg` | Gauge | rolling 1-hour average |
| `royalty_fraud_flag_rate` | Gauge | fraud events / total events |
| `royalty_held_queue_depth` | Gauge | events in HELD\_FOR\_REVIEW |
| `royalty_model_drift_psi` | Gauge | PSI score from drift monitor |
| `royalty_wallet_write_errors` | Counter | — |
| `royalty_dlq_depth` | Gauge | dead letter queue depth |

### Alerting Rules

| Condition | Priority | Escalate To |
|---|---|---|
| `royalty_fraud_flag_rate > 0.05` | **P1** | `TRUST_SAFETY_AGENT` |
| `royalty_model_drift_psi > 0.25` | P2 | `OBSERVABILITY_AGENT` |
| p99 latency > 800ms for 5 consecutive minutes | P2 | `DEVOPS_AGENT` |
| `royalty_dlq_depth > 500` | **P1** | `PLATFORM_SUPER_ADMIN` |
| `royalty_confidence_score_avg < 0.65` | P2 | `COMPLIANCE_AUDIT_AGENT` |

**Integration:** `OBSERVABILITY_AGENT` · Jaeger trace on every computation span · Loki stream: `royalty_wallet_agent_logs`

---

## 1️⃣5️⃣ Database Schema *(Locked — Add-Only Migrations)*

```sql
-- TABLE: wallet_ledger (APPEND-ONLY)
CREATE TABLE wallet_ledger (
  wallet_credit_id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id         UUID NOT NULL,          -- PARTITION KEY
  actor_id          UUID NOT NULL,          -- creator receiving royalty
  asset_id          UUID NOT NULL,
  event_id          UUID NOT NULL UNIQUE,   -- idempotency key
  trigger_type      VARCHAR(20) NOT NULL,
  royalty_amount    DECIMAL(18,4) NOT NULL,
  royalty_currency  VARCHAR(10) NOT NULL DEFAULT 'INR',
  royalty_tier      VARCHAR(20) NOT NULL,
  wallet_status     VARCHAR(30) NOT NULL,
  originality_score DECIMAL(5,4) NOT NULL,
  fraud_flag        BOOLEAN NOT NULL DEFAULT FALSE,
  confidence_score  DECIMAL(5,4) NOT NULL,
  model_version     VARCHAR(50) NOT NULL,
  audit_reference   UUID NOT NULL,
  policy_version    VARCHAR(50) NOT NULL,
  domain_track      VARCHAR(30) NOT NULL,
  created_at        TIMESTAMPTZ NOT NULL DEFAULT NOW()
  -- NO UPDATE COLUMN — append-only by design
);

-- TABLE: wallet_co_creator_splits
CREATE TABLE wallet_co_creator_splits (
  split_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  wallet_credit_id  UUID NOT NULL REFERENCES wallet_ledger(wallet_credit_id),
  tenant_id         UUID NOT NULL,
  co_creator_id     UUID NOT NULL,
  split_percent     DECIMAL(5,2) NOT NULL,
  split_amount      DECIMAL(18,4) NOT NULL,
  created_at        TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- TABLE: royalty_fraud_incidents
CREATE TABLE royalty_fraud_incidents (
  incident_id    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id      UUID NOT NULL,
  actor_id       UUID NOT NULL,
  event_id       UUID NOT NULL,
  fraud_reason   TEXT NOT NULL,
  anomaly_score  DECIMAL(5,4) NOT NULL,
  resolved       BOOLEAN NOT NULL DEFAULT FALSE,
  escalated_to   VARCHAR(100),
  created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

**Indexes:**
```sql
CREATE INDEX ON wallet_ledger(tenant_id, actor_id, created_at);
CREATE INDEX ON wallet_ledger(event_id);
CREATE INDEX ON wallet_ledger(wallet_status) WHERE wallet_status IN ('HELD_FOR_REVIEW','BLOCKED');
CREATE INDEX ON royalty_audit_log(tenant_id, timestamp_utc);
CREATE INDEX ON royalty_fraud_incidents(tenant_id, actor_id, resolved);
```

**Partitioning:**
- `wallet_ledger` — RANGE by `created_at` (monthly partitions)
- `royalty_audit_log` — HASH by `tenant_id` (16 hash buckets)

---

## 1️⃣6️⃣ API Contract *(Locked — REST + OpenAPI 3.1)*

> All endpoints are **internal service-to-service only** — not exposed via Kong to public.

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| `POST` | `/internal/royalty/compute` | Compute and commit royalty for a trigger event | Service JWT (agent scope) + `Idempotency-Key` header |
| `GET` | `/internal/royalty/wallet/{actor_id}?tenant_id={tid}` | Read wallet summary | Bearer JWT — actor self OR compliance role |
| `GET` | `/internal/royalty/wallet/{actor_id}/transactions` | Paginated wallet transaction history | Bearer JWT — actor self OR compliance role |
| `POST` | `/internal/royalty/fraud/resolve/{incident_id}` | Resolve fraud incident (human action only) | `TRUST_SAFETY_AGENT` service account OR Platform Admin |
| `GET` | `/internal/royalty/health` | Liveness + readiness check | None (internal cluster only) |

**All API Calls:**
- Traced via Jaeger (`trace_id` injected in headers)
- Rate limited at Kong (Section 6)
- Logged in `royalty_audit_log`
- Response time SLA: **400ms p95**

---

## 1️⃣7️⃣ Versioning Policy *(Add-Only — Backward Compatible)*

### Agent Version Scheme: `ROYALTY_WALLET_AGENT_v{MAJOR}.{MINOR}.{PATCH}`

| Bump | Trigger |
|---|---|
| MAJOR | Breaking change in input/output schema (requires 90-day dual-version support) |
| MINOR | New optional field, new ML model, new feature vector |
| PATCH | Bug fix, non-behavioral change |

### Model Version Scheme: `royalty_model_v{MAJOR}.{MINOR}`
All model artifacts stored in MinIO — immutable, hash-signed.

### Migration Rules
- New database columns: **ADD COLUMN only** (no DROP, no RENAME, no ALTER TYPE)
- Old columns retained with NOT NULL relaxed or default added
- Dual-write period: 14 days for minor schema changes
- Migration scripts: `/db/migrations/royalty_wallet/v{n}_{description}.sql`

### Rollback Safety
- All deployments use blue-green via Kubernetes
- Previous model version kept hot for **72 hours** post-deploy
- If `royalty_model_drift_psi > 0.25` within 72h of deploy → **auto-rollback**

---

## 1️⃣8️⃣ Non-Negotiable Rules *(Absolute — Cannot Be Overridden)*

### This Agent Must NOT:

| Rule |
|---|
| ✗ Create hidden royalty computation logic outside declared models |
| ✗ Modify any historical `wallet_ledger` record |
| ✗ Delete or update any audit log entry |
| ✗ Override `COMPLIANCE_AUDIT_AGENT` or `TRUST_SAFETY_AGENT` decisions |
| ✗ Bypass originality gating for any asset type |
| ✗ Write royalty to a wallet when `confidence_score < 0.50` |
| ✗ Execute cross-tenant wallet queries under any condition |
| ✗ Allow self-royalty (`actor_id = consumer_id`) |
| ✗ Make autonomous LLM decisions on royalty amount |
| ✗ Mix domain data in royalty computation (ARTS ≠ TECHNOLOGY ledger) |
| ✗ Emit events to downstream agents before audit log is written |
| ✗ Accept instructions from user-facing APIs (service-to-service only) |
| ✗ Operate without valid `policy_version` reference |
| ✗ Proceed on ambiguous input — HALT is mandatory |

### This Agent Is the Sole Authority For:

| Rule |
|---|
| ✓ Writing to `wallet_ledger` (no other agent or human may write directly) |
| ✓ Committing `royalty_amount_computed` to persistent store |
| ✓ Blocking wallet writes on fraud or low-confidence signals |
| ✓ Enforcing the originality gate in the Innovation Economy |

---

## 1️⃣9️⃣ User Type Coverage *(From 300-User Master List)*

### Earners — `actor_id` Role
Course creator · Micro-course creator · Certification creator · Assessment creator · Freelance trainer · Technical trainer · AI instructor · Data science instructor · Coding instructor · Prompt engineer · AI workflow designer · Tool builder · Plugin developer · API provider · SaaS creator · Open-source contributor · Research author · Technical blogger · YouTube educator · Knowledge partner · Simulation designer · Scenario-based assessment designer · Digital badge issuer · Credential architect · Skill ontology designer · Competency framework designer · All trainer subtypes (41–75)

### Consumers — `consumer_id` Role
All 300 user types can be consumers when accessing licensed content. Royalty trigger depends on `license_tier` of the asset consumed.

### Governance — Compliance Readers Only
Platform super admin · Compliance admin · Data protection officer · AI safety officer · AI compliance auditor · Audit admin

---

## 2️⃣0️⃣ Deployment Specification

```yaml
namespace:      ecoskiller-intelligence
service_name:   royalty-wallet-agent
image:          ecoskiller/royalty-wallet-agent:v1.0.0
replicas:       min: 3  max: 50  (HPA)
resources:
  cpu:          request: 500m  limit: 2000m
  memory:       request: 512Mi limit: 2Gi
probes:
  liveness:     GET /internal/royalty/health → 200 OK  (interval: 30s)
  readiness:    GET /internal/royalty/health?ready=true (interval: 10s)
secrets:        injected via Kubernetes Secrets (no hardcoded credentials)
```

**Required Environment Variables:**
```
DB_HOST · DB_PORT · DB_NAME · DB_USER · DB_PASSWORD_SECRET
REDIS_HOST · REDIS_PORT · REDIS_STREAM_NAME
MODEL_REGISTRY_URL       (MinIO endpoint)
KEYCLOAK_ISSUER_URL
POLICY_REGISTRY_URL
OBSERVABILITY_AGENT_ENDPOINT
TRUST_SAFETY_AGENT_ENDPOINT
COMPLIANCE_AUDIT_AGENT_ENDPOINT
```

**Contract Gate Dependencies** *(must be satisfied before deployment)*:
- ✅ Lane A: `identity_ready` · `rbac_ready` · `event_schema_ready`
- ✅ Lane B: `db_ready` · `search_ready`
- ✅ Lane C: `api_contract_ready` (MARKETPLACE · BILLING · IDEA\_DNA)
- ✅ Lane F: `ai_ready` (COPY\_DETECTION · FEATURE\_STORE online)

---

## 🔒 Final Lock Declaration

| Checkpoint | Status |
|---|---|
| Agent identity declared and locked | ✔ |
| Input / output contracts strictly defined | ✔ |
| ML models specified (XGBoost Regression · Isolation Forest · Rule Classifier) | ✔ |
| AI scope bounded (semantic assist only, no decision authority) | ✔ |
| Fraud detection and originality gating enforced | ✔ |
| Tenant and domain isolation enforced at every layer | ✔ |
| Audit trail: append-only, immutable, 7-year retention | ✔ |
| Failure policies: no silent failures, all paths covered | ✔ |
| Inter-agent dependency map complete | ✔ |
| Passive intelligence (feature store) emission defined | ✔ |
| Innovation economy hooks (IDEA\_DNA · COPY\_DETECTION · ROYALTY\_ENGINE) wired | ✔ |
| Growth engine hooks (RANK · XP · SHARE triggers) defined | ✔ |
| Performance monitoring with alerting thresholds set | ✔ |
| Database schema locked with add-only migration policy | ✔ |
| API contracts defined (internal service-to-service only) | ✔ |
| Versioning and rollback policy defined | ✔ |
| Non-negotiable rules enumerated | ✔ |
| 300-user type coverage mapped | ✔ |
| Deployment specification complete | ✔ |
| Contract gate dependencies verified | ✔ |

> **Violation of any section above:**
> → `STOP EXECUTION`
> → `REPORT NON-COMPLIANT AGENT DEPLOYMENT`
> → `NO ROYALTY WRITES PERMITTED`

---

*ECOSKILLER ANTIGRAVITY · ROYALTY\_WALLET\_AGENT · v1.0.0 · SEALED*
