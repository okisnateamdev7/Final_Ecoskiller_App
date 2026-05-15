# 🏆 Championship ML (6) Prize Allocation Optimizer — Agent
## SKILL & COMPETITION CORE · ANTIGRAVITY SYSTEM
### ECOSKILLER DOJO SaaS v1.0 — LOCKED · SEALED · DETERMINISTIC

---

```
ARTIFACT CLASS         : Production Agent Specification
SYSTEM DOMAIN          : Antigravity · Skill & Competition Core
MODULE                 : Championship ML (6) — Prize Allocation Optimizer
ARTIFACT VERSION       : v1.0.0
MUTATION POLICY        : Add-Only via version bump
INTERPRETATION AUTH    : NONE
EXECUTION AUTHORITY    : Human Declaration Only
DETERMINISM RULE       : Identical input → Identical output
FAILURE MODE           : STOP → REPORT → NO PARTIAL OUTPUT
STATUS                 : LOCKED · SEALED · ENFORCED
```

---

## 🔒 SECTION 1 — AGENT IDENTITY & SCOPE

### Agent Name
**Championship ML Prize Allocation Optimizer (CMPAO)**

### System Context
This agent operates within the **Antigravity Skill & Competition Core** of the ECOSKILLER unified platform. It is a governed, deterministic ML agent responsible for all prize allocation decisions across championship tiers — School → District → State → National → Continental → World.

### Agent Classification
| Property | Value |
|---|---|
| Agent Type | ML Decision Engine — Prize Governance |
| Trigger Mode | Event-Driven (championship_result_finalized) |
| Execution Lane | Lane F — Intelligence (Matching, Ranking, Discovery) |
| Gate Dependency | `ai_ready` + `api_contract_ready` + `governance_ready` |
| Output Contract | `prize_allocation_record` (immutable, auditable) |
| Override Authority | Human Board Only (Level L3+) |
| Auto-Distribution | FORBIDDEN without audit gate pass |

---

## 🔒 SECTION 2 — SYSTEM LOCK DECLARATIONS

### 2.1 Stack Lock (Non-Negotiable)

```
Backend Language    : Python 3.11
ML Framework        : Scikit-learn + XGBoost + Optuna (HPO)
API Runtime         : FastAPI + OpenAPI 3.1
Event Broker        : Redis Streams
Database            : PostgreSQL 15 (primary store)
Audit Store         : Immutable append-only PostgreSQL partition
Cache               : Redis 7
Search              : OpenSearch 2.x (ranking index)
Auth Protocol       : OAuth2 + OIDC + JWT
Infra IaC           : OpenTofu (Terraform OSS)
CI/CD               : GitLab CE
Container Runtime   : Docker + Kubernetes
Monitoring          : Prometheus + Grafana
Logging             : Loki + Promtail
Tracing             : Jaeger (trace per allocation decision)
```

### 2.2 Interface Freeze

The following interfaces are IMMUTABLE. Internal improvements allowed. No signature change permitted without version bump:

```
prize_pool_intake_contract_v1
championship_result_event_v1
participant_score_vector_v1
allocation_output_record_v1
audit_log_schema_v1
override_request_schema_v1
```

### 2.3 Entity Freeze

Primary entities cannot be renamed. Fields may extend — not mutate:

```
Championship
ParticipantResult
PrizePool
AllocationRecord
AllocationRule
AuditLog
OverrideRequest
SponsorContract
DisqualificationFlag
```

---

## 🔒 SECTION 3 — COMPETITION TIER ARCHITECTURE

The agent governs prize allocation across **6 championship tiers** defined within Antigravity:

| Tier | Level | Scope | Prize Pool Source |
|---|---|---|---|
| T1 | School | Campus | Institute + Platform Fee |
| T2 | District | Multi-School | Sponsor + Platform |
| T3 | State | Regional | Government + Sponsor + Platform |
| T4 | National | Country-wide | Corporate Sponsor + Platform |
| T5 | Continental | Multi-Nation | Enterprise Sponsor + Global Fund |
| T6 | World | Global | World Fund + Mega Sponsor + Platform |

### Tier Cascade Rules
- A participant's prize record at T1 accumulates toward eligibility signals at T2.
- No tier may distribute prizes until `championship_result_finalized` event is consumed from Redis Streams.
- Cross-tier prize stacking is permitted (configurable per SponsorContract).
- Tier skipping is FORBIDDEN. Qualification must be sequential.

---

## 🔒 SECTION 4 — ML ENGINE SPECIFICATION

### 4.1 Agent Model Architecture

```
Model Type              : Gradient-Boosted Ensemble (XGBoost primary)
Fallback                : Rule-Based Deterministic Engine
HPO Framework           : Optuna (Bayesian optimization)
Feature Store           : Redis-backed live feature cache
Training Cadence        : Post each championship cycle
Inference Mode          : Real-time (< 500ms P99 per allocation batch)
Explainability          : SHAP values mandatory per decision
Audit Output            : Full feature vector + SHAP + decision log
```

### 4.2 Input Feature Vector

Every participant result produces a **ParticipantScoreVector** before prize allocation runs:

| Feature | Source | Type |
|---|---|---|
| `raw_score` | ScoringEngine | Float |
| `normalized_score` | ScoringEngine normalized curve | Float |
| `rank_position` | LeaderboardEngine | Integer |
| `confidence_score` | MatchEngine | Float |
| `peer_score_component` | PeerScoring merge | Float |
| `mentor_score_component` | MentorScoring | Float |
| `self_score_component` | SelfAssessment | Float |
| `variance_flag` | AnomalyDetector | Boolean |
| `disqualification_flag` | IntegrityEngine | Boolean |
| `match_count` | MatchHistory | Integer |
| `belt_level` | BeltEngine | Categorical |
| `streak_bonus` | RetentionEngine | Float |
| `tier_weight` | TierConfig | Float |
| `sponsor_priority_modifier` | SponsorContract | Float |
| `region_normalization_factor` | RegionConfig | Float |
| `recency_weight` | TimeDecayConfig | Float |
| `reliability_score` | TrustEngine | Float |
| `collusion_risk_score` | IntegrityML | Float |

### 4.3 Prize Allocation Algorithm — Sealed Logic

```python
# SEALED — DO NOT MODIFY WITHOUT VERSION BUMP AND GOVERNANCE BOARD APPROVAL

def compute_prize_allocation(
    participant_vector: ParticipantScoreVector,
    prize_pool: PrizePool,
    allocation_rules: AllocationRule,
    tier_config: TierConfig
) -> AllocationRecord:

    # Gate 1 — Integrity Pre-check
    if participant_vector.disqualification_flag:
        return AllocationRecord(amount=0.0, reason="DISQUALIFIED", auditable=True)

    if participant_vector.collusion_risk_score > COLLUSION_THRESHOLD:
        return AllocationRecord(amount=0.0, reason="COLLUSION_HOLD", auditable=True, review_required=True)

    # Gate 2 — Confidence Check
    if participant_vector.confidence_score < CONFIDENCE_FLOOR:
        return AllocationRecord(amount=0.0, reason="LOW_CONFIDENCE_HOLD", review_required=True)

    # Gate 3 — ML Score Computation
    adjusted_score = (
        (participant_vector.normalized_score * allocation_rules.score_weight)
        + (participant_vector.streak_bonus * allocation_rules.streak_weight)
        + (participant_vector.reliability_score * allocation_rules.reliability_weight)
        + (participant_vector.sponsor_priority_modifier)
    ) * tier_config.tier_weight * participant_vector.region_normalization_factor

    # Gate 4 — Rank-Based Bracket Assignment
    bracket = allocation_rules.rank_to_bracket(participant_vector.rank_position)

    # Gate 5 — Prize Calculation
    prize_amount = prize_pool.bracket_allocations[bracket] * adjusted_score

    # Gate 6 — Cap Enforcement
    prize_amount = min(prize_amount, prize_pool.per_participant_cap)

    # Gate 7 — Audit Log Generation (MANDATORY)
    return AllocationRecord(
        participant_id=participant_vector.participant_id,
        championship_id=participant_vector.championship_id,
        tier=tier_config.tier_level,
        bracket=bracket,
        raw_prize=prize_amount,
        adjusted_prize=apply_tax_rules(prize_amount),
        shap_values=compute_shap(participant_vector),
        feature_snapshot=participant_vector.to_dict(),
        decision_timestamp=utcnow(),
        auditable=True,
        override_eligible=True
    )
```

### 4.4 Prize Pool Distribution Model

```
Rank 1          : 30% of tier prize pool
Rank 2          : 20%
Rank 3          : 10%
Rank 4–10       : 5% shared (tier-configurable)
Rank 11–25      : Participation bonus pool (tier-configurable)
Remaining       : Platform reserve + rollover fund
```

All distribution percentages are stored in `AllocationRule` entity. No hardcoded values in agent runtime code. Rule updates require governance review and version bump.

### 4.5 Collusion & Manipulation Detection

The ML agent integrates IntegrityML sub-module:

```
Detection Targets:
  - Reciprocal high-scoring participant pairs
  - Abnormal peer score clustering
  - Match farming patterns
  - Rating inflation clusters
  - Mentor favoritism patterns

Detection Output:
  - collusion_risk_score (0.0–1.0)
  - flagged_match_ids[]
  - escalation_level (MONITOR | HOLD | BLOCK)

Flagged Participants:
  → Prize HELD pending audit review
  → Cannot receive prize until audit_cleared = TRUE
  → Human board notification triggered
```

---

## 🔒 SECTION 5 — DATABASE SCHEMA

### 5.1 Required Entities

```sql
-- Championship Result (source truth)
CREATE TABLE championship_result (
    result_id         UUID PRIMARY KEY,
    championship_id   UUID NOT NULL REFERENCES championship(id),
    participant_id    UUID NOT NULL REFERENCES user(id),
    tier_level        INT NOT NULL CHECK (tier_level BETWEEN 1 AND 6),
    raw_score         DECIMAL(10,4) NOT NULL,
    normalized_score  DECIMAL(10,4) NOT NULL,
    rank_position     INT NOT NULL,
    confidence_score  DECIMAL(5,4) NOT NULL,
    belt_level        VARCHAR(20) NOT NULL,
    disqualified      BOOLEAN DEFAULT FALSE,
    collusion_flag    BOOLEAN DEFAULT FALSE,
    variance_flag     BOOLEAN DEFAULT FALSE,
    finalized_at      TIMESTAMPTZ NOT NULL,
    created_at        TIMESTAMPTZ DEFAULT NOW()
);

-- Prize Pool
CREATE TABLE prize_pool (
    pool_id             UUID PRIMARY KEY,
    championship_id     UUID NOT NULL REFERENCES championship(id),
    tier_level          INT NOT NULL,
    total_amount        DECIMAL(14,2) NOT NULL,
    currency            VARCHAR(5) DEFAULT 'USD',
    per_participant_cap DECIMAL(14,2),
    sponsor_contract_id UUID REFERENCES sponsor_contract(id),
    locked_at           TIMESTAMPTZ,
    status              VARCHAR(20) DEFAULT 'PENDING',
    created_at          TIMESTAMPTZ DEFAULT NOW()
);

-- Allocation Rule (versioned)
CREATE TABLE allocation_rule (
    rule_id         UUID PRIMARY KEY,
    rule_version    VARCHAR(20) NOT NULL,
    tier_level      INT NOT NULL,
    score_weight    DECIMAL(5,4) NOT NULL,
    streak_weight   DECIMAL(5,4) NOT NULL,
    reliability_weight DECIMAL(5,4) NOT NULL,
    bracket_config  JSONB NOT NULL,
    effective_from  TIMESTAMPTZ NOT NULL,
    effective_to    TIMESTAMPTZ,
    approved_by     UUID NOT NULL,
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

-- Allocation Record (IMMUTABLE after write)
CREATE TABLE allocation_record (
    allocation_id     UUID PRIMARY KEY,
    championship_id   UUID NOT NULL,
    participant_id    UUID NOT NULL,
    tier_level        INT NOT NULL,
    bracket           VARCHAR(20) NOT NULL,
    raw_prize         DECIMAL(14,2) NOT NULL,
    adjusted_prize    DECIMAL(14,2) NOT NULL,
    currency          VARCHAR(5) DEFAULT 'USD',
    shap_values       JSONB NOT NULL,
    feature_snapshot  JSONB NOT NULL,
    rule_version      VARCHAR(20) NOT NULL,
    status            VARCHAR(20) DEFAULT 'PENDING',
    override_eligible BOOLEAN DEFAULT TRUE,
    audit_cleared     BOOLEAN DEFAULT FALSE,
    distributed_at    TIMESTAMPTZ,
    created_at        TIMESTAMPTZ DEFAULT NOW()
);

-- Override Request (human-only authority)
CREATE TABLE override_request (
    override_id       UUID PRIMARY KEY,
    allocation_id     UUID NOT NULL REFERENCES allocation_record(id),
    requested_by      UUID NOT NULL,
    requester_role    VARCHAR(50) NOT NULL CHECK (requester_role IN ('BOARD_L3','BOARD_L4','ADMIN')),
    original_amount   DECIMAL(14,2) NOT NULL,
    override_amount   DECIMAL(14,2) NOT NULL,
    justification     TEXT NOT NULL,
    approved_by       UUID,
    status            VARCHAR(20) DEFAULT 'PENDING',
    created_at        TIMESTAMPTZ DEFAULT NOW(),
    resolved_at       TIMESTAMPTZ
);

-- Audit Log (IMMUTABLE — append-only partition)
CREATE TABLE prize_audit_log (
    log_id            UUID PRIMARY KEY,
    event_type        VARCHAR(50) NOT NULL,
    allocation_id     UUID,
    championship_id   UUID,
    participant_id    UUID,
    actor_id          UUID,
    payload           JSONB NOT NULL,
    created_at        TIMESTAMPTZ DEFAULT NOW()
) PARTITION BY RANGE (created_at);
```

---

## 🔒 SECTION 6 — API CONTRACT REGISTRY

All contracts below are FROZEN. Version bump required for any modification.

### 6.1 Core Allocation APIs

```yaml
POST /championships/{championship_id}/allocations/compute
  Description : Trigger ML prize allocation computation
  Auth        : JWT + ROLE:SYSTEM_AGENT
  Input       : { championship_id, tier_level, dry_run?: bool }
  Output      : AllocationBatch { records[], total_allocated, status }
  Gate        : championship_result_finalized = TRUE
  Failure     : STOP → REPORT → NO PARTIAL OUTPUT

GET /championships/{championship_id}/allocations
  Description : Retrieve all allocation records for a championship
  Auth        : JWT + ROLE:ADMIN | BOARD | FINANCE
  Output      : AllocationRecord[]

GET /allocations/{allocation_id}
  Description : Single allocation with SHAP values and feature snapshot
  Auth        : JWT + ROLE:ADMIN | participant (own record only)
  Output      : AllocationRecord + shap_values + feature_snapshot

POST /allocations/{allocation_id}/override
  Description : Request manual override (Human Authority Only)
  Auth        : JWT + ROLE:BOARD_L3+
  Input       : OverrideRequest { override_amount, justification }
  Output      : OverrideRequest { status: PENDING }

POST /allocations/{allocation_id}/distribute
  Description : Trigger prize distribution to wallet
  Auth        : JWT + ROLE:FINANCE
  Gate        : audit_cleared = TRUE + status = APPROVED
  Output      : DistributionRecord

GET /prize-pools/{championship_id}
  Description : Retrieve prize pool configuration
  Auth        : JWT + ROLE:ADMIN | FINANCE
  Output      : PrizePool

POST /prize-pools
  Description : Configure prize pool for championship
  Auth        : JWT + ROLE:ADMIN
  Input       : PrizePool { championship_id, tier_level, total_amount, ... }
  Output      : PrizePool { pool_id, status: LOCKED }

GET /allocation-rules/active
  Description : Get current active allocation rules per tier
  Auth        : JWT + ROLE:ADMIN
  Output      : AllocationRule[]

POST /allocation-rules
  Description : Create new allocation rule version (requires board approval)
  Auth        : JWT + ROLE:BOARD_L3+
  Input       : AllocationRule
  Output      : AllocationRule { rule_id, rule_version }
```

### 6.2 Audit & Analytics APIs

```yaml
GET /championships/{championship_id}/allocations/audit
  Description : Full audit trail for all allocation decisions
  Auth        : JWT + ROLE:BOARD | COMPLIANCE
  Output      : AuditLog[]

GET /championships/{championship_id}/allocations/explain/{participant_id}
  Description : SHAP explainability report for participant's allocation
  Auth        : JWT + ROLE:ADMIN | participant (own)
  Output      : ExplainabilityReport { shap_values, feature_importances, decision_path }

GET /allocations/analytics/distribution-summary
  Description : Prize distribution summary across tiers
  Auth        : JWT + ROLE:ADMIN | FINANCE
  Output      : DistributionSummary { by_tier, by_bracket, totals }
```

---

## 🔒 SECTION 7 — EVENT SCHEMA REGISTRY

All events are registered in the ECOSKILLER Event Schema Registry. Absent → STOP EXECUTION.

```json
// championship_result_finalized
{
  "event": "championship_result_finalized",
  "version": "v1",
  "schema": {
    "championship_id": "uuid",
    "tier_level": "int",
    "result_count": "int",
    "finalized_at": "datetime",
    "prize_pool_id": "uuid"
  }
}

// prize_allocation_computed
{
  "event": "prize_allocation_computed",
  "version": "v1",
  "schema": {
    "championship_id": "uuid",
    "tier_level": "int",
    "allocation_batch_id": "uuid",
    "total_allocated": "decimal",
    "record_count": "int",
    "hold_count": "int"
  }
}

// prize_allocation_hold_triggered
{
  "event": "prize_allocation_hold_triggered",
  "version": "v1",
  "schema": {
    "allocation_id": "uuid",
    "participant_id": "uuid",
    "reason": "string [COLLUSION_HOLD|LOW_CONFIDENCE_HOLD|DISQUALIFIED]",
    "review_required": "bool"
  }
}

// prize_override_requested
{
  "event": "prize_override_requested",
  "version": "v1",
  "schema": {
    "override_id": "uuid",
    "allocation_id": "uuid",
    "requested_by": "uuid",
    "original_amount": "decimal",
    "override_amount": "decimal"
  }
}

// prize_distributed
{
  "event": "prize_distributed",
  "version": "v1",
  "schema": {
    "allocation_id": "uuid",
    "participant_id": "uuid",
    "amount": "decimal",
    "currency": "string",
    "wallet_ref": "string",
    "distributed_at": "datetime"
  }
}
```

---

## 🔒 SECTION 8 — ROLE → PERMISSION MATRIX

| Role | Compute Allocation | View Allocation | Override | Distribute | Audit View | Rule Config |
|---|---|---|---|---|---|---|
| SYSTEM_AGENT | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| PARTICIPANT | ❌ | Own only | ❌ | ❌ | ❌ | ❌ |
| ADMIN | ❌ | ✅ | ❌ | ❌ | ✅ | ❌ |
| FINANCE | ❌ | ✅ | ❌ | ✅ | ✅ | ❌ |
| BOARD_L3 | ❌ | ✅ | ✅ | ❌ | ✅ | ✅ |
| BOARD_L4 | ❌ | ✅ | ✅ | ✅ | ✅ | ✅ |
| COMPLIANCE | ❌ | ✅ | ❌ | ❌ | ✅ | ❌ |

---

## 🔒 SECTION 9 — INTEGRITY & TRUST ENGINE INTEGRATION

### 9.1 Pre-Allocation Integrity Gates

All 4 gates must pass before allocation record is created. Any gate failure = HOLD.

```
GATE 1 — Disqualification Check
  Source    : IntegrityEngine
  Signal    : disqualification_flag
  Condition : flag = TRUE → status = DISQUALIFIED → no prize

GATE 2 — Collusion Risk Check
  Source    : IntegrityML
  Signal    : collusion_risk_score
  Threshold : > 0.75 → COLLUSION_HOLD
  Action    : Notify governance board, freeze allocation

GATE 3 — Confidence Score Check
  Source    : MatchEngine
  Signal    : confidence_score
  Threshold : < 0.60 → LOW_CONFIDENCE_HOLD
  Action    : Queue for mentor board review

GATE 4 — Variance Anomaly Check
  Source    : ScoringEngine AnomalyDetector
  Signal    : variance_flag
  Condition : flag = TRUE → flag for secondary audit review
```

### 9.2 Audit Immutability Law

Every allocation decision generates an **immutable audit record**. This record:
- Cannot be deleted
- Cannot be updated (append-only partition)
- Must include: full feature vector, SHAP values, rule version, decision timestamp, acting system agent ID
- Is permanently accessible to BOARD and COMPLIANCE roles
- Is exportable for regulatory compliance

---

## 🔒 SECTION 10 — UI SCREENS (Flutter + React)

### 10.1 Flutter Operational Screens

```
Championship Prize Dashboard
  - Real-time allocation status per tier
  - Hold queue with reason codes
  - Distribution status tracker
  - Override request panel (BOARD roles only)

Participant Prize Detail Screen
  - My prize allocation
  - Allocation bracket
  - Explainability panel (SHAP visualization)
  - Distribution status

Admin Allocation Console
  - Batch allocation status
  - Integrity hold queue
  - Manual override workflow
  - Audit log viewer

Finance Distribution Screen
  - Approved allocations queue
  - Distribute action (FINANCE role)
  - Distribution confirmation & receipt
  - Tax summary panel

Governance Board Screen
  - Override request inbox
  - Approve / Reject override
  - Audit export
  - Allocation rule versioning
```

### 10.2 React SEO Public Screens

```
Championship Leaderboard (Public — Read-Only)
  - Ranked participants
  - Prize tier display (no individual amounts until distributed)
  - Sponsor attribution

Championship Results Page (SEO indexed)
  - Tier overview
  - Winners announcement
  - Certificate links
```

---

## 🔒 SECTION 11 — NOTIFICATION POLICY REGISTRY

| Event | Recipient | Channel | Trigger |
|---|---|---|---|
| Allocation computed | Participant | In-App + Email | On compute |
| Allocation hold | Participant + Board | In-App + Email | On hold flag |
| Override requested | Board L3+ | In-App + Email | On request create |
| Override approved | Finance + Participant | In-App + Email | On approval |
| Prize distributed | Participant | In-App + Email + Push | On distribute |
| Collusion flagged | Compliance + Board | Email + Urgent | On flag |
| Allocation rule updated | Admin | In-App | On rule version bump |

---

## 🔒 SECTION 12 — BILLING INTEGRATION LOCK

The agent integrates with the ECOSKILLER Financial System (Module 13):

```
Escrow Hold         : Prize pool locked in escrow upon championship_start event
Escrow Release      : Released to allocation engine upon championship_result_finalized
Tax Computation     : Applied per jurisdiction rules (GST/VAT/TDS config)
Wallet Credit       : Post distribution → user.wallet credited
Payout Trigger      : User-initiated from wallet (per payout rules)
Sponsor Settlement  : Sponsor prize contributions tracked per SponsorContract
Refund Rules        : Disqualified participant refund to prize pool (configurable)
Financial Ledger    : Every allocation transaction → BillingLedgerSchema entry
```

---

## 🔒 SECTION 13 — DEVOPS & DEPLOYMENT LOCK

### 13.1 Environment Isolation

```
DEV       : Local ML model stubs + mock prize pools
TEST      : CI pipeline + unit/integration tests + contract validator
STAGING   : Full ML engine + real tier configs + synthetic championships
PRODUCTION: Live championships + real prize distribution + full audit
```

### 13.2 Required K8s Manifests

```
/infra/k8s/dev/cmpao/
/infra/k8s/test/cmpao/
/infra/k8s/staging/cmpao/
/infra/k8s/production/cmpao/

Each containing:
  - Deployment (cmpao-engine)
  - Service
  - Ingress
  - ConfigMap (tier configs, allocation rules)
  - Secret templates (DB creds, wallet API keys)
  - HorizontalPodAutoscaler
  - CronJob (model retraining post-championship)
```

### 13.3 CI Pipeline Gates (GitLab CE)

```yaml
stages:
  - contract_validator
  - unit_tests
  - integration_tests
  - ml_model_tests
  - integrity_tests
  - load_tests
  - docker_build
  - deploy_staging
  - deploy_production

ml_model_tests:
  tests:
    - allocation_determinism_test
    - shap_output_completeness_test
    - collusion_threshold_test
    - confidence_gate_test
    - tier_weight_test
    - cap_enforcement_test

integrity_tests:
  tests:
    - disqualification_gate_test
    - collusion_hold_test
    - variance_flag_test
    - audit_immutability_test
    - override_authority_test
```

---

## 🔒 SECTION 14 — OBSERVABILITY & ALERTING LOCK

### 14.1 Required Metrics (Prometheus)

```
cmpao_allocations_computed_total        (counter, by tier)
cmpao_allocations_held_total            (counter, by reason)
cmpao_allocations_distributed_total     (counter, by tier)
cmpao_override_requests_total           (counter, by status)
cmpao_ml_inference_duration_ms          (histogram, P50/P95/P99)
cmpao_collusion_flags_total             (counter)
cmpao_prize_pool_utilization_ratio      (gauge, by tier)
cmpao_audit_log_write_latency_ms        (histogram)
```

### 14.2 Dashboards (Grafana)

```
Championship Prize Overview Dashboard
  - Allocations computed vs held vs distributed
  - Collusion flag rate
  - Override request rate
  - Prize pool utilization by tier

ML Engine Health Dashboard
  - Inference latency (P50/P95/P99)
  - Model confidence score distribution
  - SHAP value anomaly detector
  - Retraining job status

Integrity Monitor Dashboard
  - Disqualification rate by tier
  - Collusion hold queue depth
  - Audit log write throughput
```

### 14.3 Alerting Rules

```
CRITICAL: cmpao_ml_inference_duration_ms P99 > 1000ms
CRITICAL: audit_log_write_failure = any
CRITICAL: prize_distribution_failure = any
WARNING : collusion_flag_rate > 5% of championship cohort
WARNING : override_request_rate > 2% of allocations
WARNING : prize_pool_utilization_ratio > 100% (overspend risk)
```

---

## 🔒 SECTION 15 — APPEALS & GOVERNANCE LOCK

### 15.1 Appeal Workflow

```
Participant Appeal Trigger:
  → Participant views allocation detail
  → Disputes bracket or amount
  → Submits appeal with justification

Appeal Review:
  → ADMIN reviews feature snapshot + SHAP
  → If anomaly confirmed → escalate to BOARD_L3
  → BOARD_L3 issues override or upholds

Governance Board Decisions:
  → All decisions logged and versioned
  → Logged to prize_audit_log (immutable)
  → Participant notified within SLA (48 hours)
```

### 15.2 Override Authority Rules

```
Auto Override             : FORBIDDEN
BOARD_L3 Override         : Permitted (single approval)
BOARD_L4 Override         : Permitted for tier T5–T6 (dual approval required)
Finance Execution          : Only after BOARD approval
Override Amount Cap        : Max 150% of original computed allocation
Override Audit             : Mandatory — reason + SHAP delta logged
```

---

## 🔒 SECTION 16 — ML RETRAINING GOVERNANCE

```
Retraining Trigger      : Post-championship-cycle (automated CronJob)
Training Data Source    : championship_result + allocation_record (historical)
Label Source            : Employer feedback signals + post-hire outcome mapping
Validation Gate         : Hold-out set accuracy + bias audit before deploy
Retraining Approval     : BOARD_L3 sign-off required before production deploy
Model Registry          : Versioned (MLflow-compatible)
Rollback               : One-click to last approved model version
Drift Detection         : Prometheus alert on score distribution shift > 10%
```

---

## 🔒 SECTION 17 — SPONSOR CONTRACT INTEGRATION LOCK

```
SponsorContract Entity:
  - sponsor_id
  - championship_id
  - tier_levels[] (which tiers they fund)
  - contribution_amount
  - prize_priority_modifier (float, applied in ML feature vector)
  - special_winner_criteria (optional JSON rules)
  - attribution_required (boolean)
  - contract_locked_at

Rules:
  - Sponsor priority modifier CANNOT exceed 0.20 (20% boost cap)
  - Sponsor cannot dictate winner — only influence prize pool size
  - Sponsor attribution displayed on championship public page
  - Sponsor settlement post-distribution, not pre
  - Absence of SponsorContract → sponsor_priority_modifier = 0.0
```

---

## 🔒 SECTION 18 — ANTI-GRAVITY SYSTEM TRUST SEAL

This agent is governed under the **ECOSKILLER DOJO TRUST & FAIRNESS MODE**:

```
DOJO TRUST & FAIRNESS MODE              ENABLED
Assessment Validity                     REQUIRED
Rater Calibration                       REQUIRED
Scenario Calibration                    REQUIRED
Fairness Engine                         ACTIVE
Collusion Detection                     ACTIVE
Outcome Validation                      REQUIRED
Belt Versioning                         ENFORCED
Appeals System                          ACTIVE
Institutional Trust Mode                LOCKED
Prize Override Human Authority          ENFORCED
Audit Immutability                      ENFORCED
ML Explainability (SHAP)                MANDATORY
Auto-Distribution Without Audit         FORBIDDEN
```

---

## 🔒 SECTION 19 — PRODUCTION READINESS CHECKLIST

All items must be ✅ before any production deploy claim:

```
[ ] championship_result_finalized event contract registered
[ ] prize_pool_intake_contract_v1 registered
[ ] allocation_output_record_v1 registered
[ ] AllocationRule active version exists for all 6 tiers
[ ] PrizePool configured and locked for championship
[ ] IntegrityML collusion model deployed
[ ] SHAP explainability verified on sample batch
[ ] Audit log immutability tested (append-only confirmed)
[ ] Override authority RBAC tested (non-board roles blocked)
[ ] Auto-distribution gate tested (fails without audit_cleared)
[ ] K8s manifests for all 4 environments present
[ ] CI pipeline GREEN on ml_model_tests + integrity_tests
[ ] Prometheus metrics emitting
[ ] Grafana dashboards live
[ ] Alerting rules active
[ ] Audit log partition created (production)
[ ] Sponsor contract integration tested
[ ] Tax computation rules loaded per jurisdiction
[ ] Wallet credit integration tested
[ ] Multi-tier cascade logic verified (T1→T6)
[ ] Appeals workflow end-to-end tested
[ ] ML retraining CronJob scheduled
[ ] Model rollback tested
```

Absence of any item above:
```
→ STOP EXECUTION
→ REPORT CMPAO-PRODUCTION-READINESS-FAILURE
→ NO PRIZE DISTRIBUTION CLAIM PERMITTED
```

---

## 🔒 SECTION 20 — FINAL GOVERNANCE SEAL

```
ANTIGRAVITY CHAMPIONSHIP ML PRIZE ALLOCATION OPTIMIZER
Version             : v1.0.0
Status              : LOCKED · SEALED · ENFORCED
System              : ECOSKILLER Unified SaaS
Domain              : Skill & Competition Core
Mutation Policy     : Add-Only via version bump
Override Authority  : Human Board L3+ Only
Auto-Distribution   : FORBIDDEN without audit gate
Interpretation Auth : NONE
Architecture Auth   : LOCKED
Explainability      : SHAP mandatory per decision
Audit               : Immutable append-only
Failure Mode        : STOP → REPORT → NO PARTIAL OUTPUT
```

---

*End of Artifact — Championship ML (6) Prize Allocation Optimizer Agent — ANTIGRAVITY SKILL & COMPETITION CORE — ECOSKILLER v1.0*

```
LOCKED ████████████████████████████████████████ SEALED
```
