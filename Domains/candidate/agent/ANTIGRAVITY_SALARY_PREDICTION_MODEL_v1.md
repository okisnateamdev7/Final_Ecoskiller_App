# 🔒 ANTIGRAVITY — SALARY PREDICTION MODEL
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### SEALED PRODUCTION ARTIFACT SPEC v1.0

---

```
Artifact Class:        Production AI Inference System
System Parent:         ECOSKILLER + DOJO SAAS Unified Platform
Artifact Name:         ANTIGRAVITY Salary Prediction Engine (ASPE)
Mutation Policy:       Add-only via version bump
Execution Mode:        Deterministic · Contract-Gated
Interpretation Auth:   NONE
Architecture Auth:     LOCKED
Trust Seal:            ACTIVE
Version:               ASPE-v1.0.0
```

---

> **EXECUTION DECREE**
> This document is a sealed production prompt artifact for the ANTIGRAVITY Salary Prediction Model operating within the ECOSKILLER Enterprise Optimization + Trust Infrastructure. No section may be reinterpreted, restructured, or modified without a declared version bump. All subsystems described herein are mandatory. Absence of any subsystem → STOP EXECUTION.

---

## 🔒 SECTION A — SYSTEM IDENTITY & SCOPE LOCK

```
Model Name:         ANTIGRAVITY Salary Prediction Engine (ASPE)
Module Parent:      ECOSKILLER Recruiter System — Module 7, Feature #13
Infrastructure:     Enterprise Optimization + Trust Infrastructure Layer
Trust Class:        Tier-1 Verified Prediction (Employer-Facing)
Prediction Scope:   Individual · Role · Market · Team · Retention Band
Confidence Gate:    Minimum 0.78 confidence score required for employer output
Failure Mode:       STOP → REPORT → NO PARTIAL PREDICTION EMITTED
```

### Mission

ANTIGRAVITY Salary Prediction Engine exists to eliminate compensation guesswork from the hiring loop. It synthesizes verified skill data, Dojo belt progression, intelligence scores, real work history from integrated tools, championship performance, and live market signals into a single, auditable, trust-anchored salary range recommendation per candidate per role.

This is NOT a static lookup table.
This is NOT a manually maintained band system.
This IS a live, multi-signal AI inference engine locked to verified identity data.

---

## 🔒 SECTION B — TRUST PREREQUISITE LOCK

The salary prediction CANNOT execute unless all trust prerequisites are satisfied. Trust prerequisites are enforced by the ECOSKILLER Trust Infrastructure (Module 15).

### Mandatory Trust Gates (All Required — None Optional)

| Gate ID | Trust Signal | Required Status | Source System |
|---------|-------------|-----------------|---------------|
| TG-01 | Candidate Identity Verified | `verified_status = TRUE` | StudentProfile / ProfessionalID |
| TG-02 | Institute or Employer Verified | `institution_verified = TRUE` | ECOSKILLER Verified Institute Registry |
| TG-03 | Skill Belt Earned via Dojo | `belt_level ≥ WHITE` | Dojo Belt Engine — Promotion requires mentor cert |
| TG-04 | Minimum Match Count | `match_count ≥ 5` | Dojo Match Engine |
| TG-05 | Score Confidence Floor | `confidence_score ≥ 0.78` | Dojo Scoring Engine |
| TG-06 | No Active Fraud Flag | `fraud_flag = FALSE` | ECOSKILLER Trust System — Fraud Detection |
| TG-07 | Recruiter Verified | `recruiter_verified = TRUE` | Recruiter Onboarding Gate |
| TG-08 | No Active Collusion Flag | `collusion_flag = FALSE` | Dojo Collusion Detection (Section T9) |

**If ANY gate is NOT satisfied → STOP PREDICTION → EMIT TRUST_GATE_FAILURE report.**

---

## 🔒 SECTION C — DATA INPUT SIGNAL REGISTRY (SEALED)

All signals are read-only to the prediction engine. No signal may be mutated by the engine.

### C1 — Dojo Skill Signal Block

```
Source:             Dojo SaaS Engine (ECOSKILLER Module 3)
Access Mode:        Event-stream read from Analytics Engine
Freshness Policy:   Max 72-hour staleness allowed

Signals:
  dojo.belt_level                   → Enum: WHITE | YELLOW | GREEN | BLUE | BROWN | BLACK
  dojo.belt_version                 → Versioned belt rubric tag (SECTION T17)
  dojo.match_count                  → Integer: total ranked matches completed
  dojo.avg_score_last_10            → Float: average score across last 10 matches
  dojo.score_confidence             → Float [0–1]: inter-rater reliability score
  dojo.peer_score_variance          → Float: variance across peer evaluators
  dojo.mentor_score_override_rate   → Float: % of matches where mentor corrected peer score
  dojo.pressure_scenario_pass_rate  → Float: % pressure scenarios passed
  dojo.skill_track_delta            → Float: post-track vs pre-track score delta (Section T6)
  dojo.skill_vector                 → Array[Float]: multi-dimensional skill embedding
  dojo.certification_count          → Integer: certifications earned
  dojo.scenario_difficulty_avg      → Float: avg difficulty of completed scenarios (data-derived, T4)
  dojo.replay_authenticity_seal     → Boolean: replay evidence verified (T14)
```

### C2 — Intelligence Score Signal Block

```
Source:             ECOSKILLER Intelligence Measurement System (Module 2)
Access Mode:        Cached inference result — max 30-day staleness

Signals:
  intel.radar_scores                → Object: per-intelligence-type score (8 dimensions)
  intel.percentile_rank             → Float [0–100]: global percentile
  intel.confidence_score            → Float [0–1]: test reliability
  intel.growth_delta_90d            → Float: intelligence score change over 90 days
  intel.career_intelligence_map     → Array[String]: AI-predicted career aptitude zones
  intel.learning_speed_score        → Float: normalized learning velocity
  intel.focus_measurement           → Float: focus/attention score
  intel.decision_analysis_score     → Float: decision quality under constraint
```

### C3 — Real Work Data Signal Block (Integration Engine)

```
Source:             ECOSKILLER Integration Engine (EIE) — Module 9
                    100-Tool Integration Layer (OAuth + Webhook + AI Normalization)
Access Mode:        Universal Work Data Format (UWDF) — normalized AI output
Staleness Policy:   Max 7-day for active integrations

Signals per tool category:
  work.github_signals:
    commit_frequency                → Float: commits per active week
    pr_merge_rate                   → Float: merged PRs / opened PRs
    bug_rate                        → Float: bug-tagged issues / total issues
    code_complexity_score           → Float: AI-assessed average complexity
    collaboration_score             → Float: cross-repo contribution index

  work.jira_signals:
    ticket_completion_rate          → Float
    sprint_velocity_avg             → Float
    blocker_resolution_time         → Float (hours)
    project_ownership_count         → Integer

  work.salesforce_signals:
    deal_close_rate                 → Float
    pipeline_accuracy               → Float
    quota_attainment_avg            → Float

  work.figma_signals:
    design_iteration_count          → Integer
    comment_resolution_rate         → Float
    handoff_quality_score           → Float (AI-assessed)

  work.slack_signals:
    response_time_avg               → Float (hours)
    async_communication_score       → Float (AI-assessed)
    collaboration_breadth           → Integer: unique collaborators

  work.hrms_signals (BambooHR / Workday / Darwinbox / Keka):
    tenure_months                   → Integer
    performance_review_avg          → Float
    promotion_count                 → Integer
    absenteeism_rate                → Float

  work.normalized_reliability_score → Float [0–1]: AI cross-signal reliability composite
  work.skill_extraction_vector      → Array[Float]: AI-extracted skill embedding from UWDF
```

### C4 — Championship & Competition Signal Block

```
Source:             ECOSKILLER Championship System (Module 4)
Access Mode:        Event log read

Signals:
  champ.tier_reached                → Enum: SCHOOL | DISTRICT | STATE | NATIONAL | CONTINENTAL | WORLD
  champ.win_rate                    → Float
  champ.ranking_percentile          → Float [0–100]
  champ.competition_count           → Integer
  champ.prize_awarded               → Boolean
  champ.ai_evaluation_score         → Float: AI judge composite score
  champ.championship_badge_count    → Integer
```

### C5 — Market Signal Block (Live)

```
Source:             External market data — ASPE Market Feed Layer
Refresh Policy:     Daily refresh (00:00 UTC)
Fallback:           Last-known-good cache — max 72-hour staleness

Signals:
  market.role_median_salary         → Float: global median for matched role
  market.role_p25_salary            → Float: 25th percentile
  market.role_p75_salary            → Float: 75th percentile
  market.role_p90_salary            → Float: 90th percentile
  market.region_cost_index          → Float: purchasing power / cost-of-living multiplier
  market.skill_demand_score         → Float: demand signal for candidate's primary skill cluster
  market.supply_scarcity_index      → Float: inverse of available candidate pool density
  market.yoy_salary_growth_rate     → Float: year-over-year comp growth in role category
  market.remote_premium             → Float: remote vs on-site salary delta
  market.industry_sector_premium    → Float: per-sector compensation premium
```

### C6 — Candidate Profile Signal Block

```
Source:             ECOSKILLER Identity System (Module 1) — Verified only
Access Mode:        Direct from verified StudentProfile / ProfessionalID entity

Signals:
  profile.verified_status           → Boolean (TG-01 prerequisite)
  profile.education_history         → Array[Object]: institution + degree + verified flag
  profile.experience_years          → Float: total professional experience
  profile.skill_history_vector      → Array[Float]: longitudinal skill progression embedding
  profile.portfolio_count           → Integer: verified portfolio items
  profile.endorsement_count         → Integer: peer endorsements received
  profile.influence_score           → Integer: ECOSKILLER influence algorithm output
  profile.streak_count              → Integer: engagement reliability signal
  profile.certificate_vault_count   → Integer
  profile.blockchain_cert_count     → Integer: immutable certification count
```

---

## 🔒 SECTION D — PREDICTION MODEL ARCHITECTURE (SEALED)

### D1 — Model Class

```
Model Type:         Ensemble Inference Model
Components:
  Layer 1:          Signal Normalization & Embedding Fusion
  Layer 2:          Trust-Weighted Feature Composition
  Layer 3:          Salary Band Regression
  Layer 4:          Confidence Calibration
  Layer 5:          Explainability Output Generator
Output Format:      Structured SalaryPrediction object (see Section F)
```

### D2 — Layer 1: Signal Normalization & Embedding Fusion

All raw signals from Sections C1–C6 are normalized to a common scale before fusion.

```
Normalization Rules:
  Float signals:      Min-max normalization per signal historical distribution
  Enum signals:       Ordinal encoding (e.g., belt: WHITE=1 ... BLACK=7)
  Array/Vector:       L2-normalized embeddings
  Boolean:            Binary (0/1)

Fusion:
  dojo.skill_vector + work.skill_extraction_vector + profile.skill_history_vector
  → Concatenated and passed through learned projection layer
  → Output: unified_skill_embedding [512-dim]
```

### D3 — Layer 2: Trust-Weighted Feature Composition

Each signal block receives a trust weight based on verification status. Unverified or low-confidence signals receive reduced weight — they do NOT block prediction unless a Trust Gate (Section B) fails.

```
Trust Weight Rules:
  dojo.*    signals:    weight = dojo.score_confidence × (1 - dojo.peer_score_variance)
  work.*    signals:    weight = work.normalized_reliability_score
  intel.*   signals:    weight = intel.confidence_score
  champ.*   signals:    weight = 1.0 if champ.ai_evaluation_score ≥ 0.7 else 0.5
  market.*  signals:    weight = 1.0 (external market data is unweighted)
  profile.* signals:    weight = 1.0 if profile.verified_status = TRUE else 0.2

Trust-weighted feature vector:
  F = Σ (signal_i × trust_weight_i) for all signal blocks
  Normalized F → fed to Layer 3
```

### D4 — Layer 3: Salary Band Regression

```
Input:              Normalized trust-weighted feature vector F + market signal block
Output:             { p10, p25, p50, p75, p90 } salary distribution

Regression Target:  Gross annual compensation (base salary)
Model Variant:      Quantile regression ensemble (5 quantile heads)
Training Data:      Anonymized historical hiring outcomes from ECOSKILLER marketplace
                    Augmented with market signal feeds
Retraining Policy:  Monthly retraining triggered by governance board (Section T16 equivalent)
Retraining Gate:    Requires test coverage pass before deployment

Outputs:
  predicted.p10     → Float: lower bound — conservative floor
  predicted.p25     → Float: budget-conscious band floor
  predicted.p50     → Float: market median recommendation
  predicted.p75     → Float: competitive offer band ceiling
  predicted.p90     → Float: premium / aggressive offer ceiling
  predicted.currency→ String: ISO 4217 currency code
  predicted.region  → String: ISO 3166 region code
```

### D5 — Layer 4: Confidence Calibration

```
Confidence Score Formula:
  confidence = α × dojo.score_confidence
             + β × work.normalized_reliability_score
             + γ × intel.confidence_score
             + δ × (1 / market.signal_staleness_hours)
             + ε × profile.verified_weight

  Where α + β + γ + δ + ε = 1.0 (weights defined in model config, versioned)

Confidence Gate:
  If confidence < 0.78:
    → OUTPUT = SUPPRESSED
    → Emit: PREDICTION_LOW_CONFIDENCE
    → Route to: Mentor Board Review Queue (per Section T2 equivalent)
    → No employer-facing output emitted

  If confidence ≥ 0.78 AND < 0.90:
    → OUTPUT emitted with confidence_flag = MODERATE
    → Disclaimer appended to employer output

  If confidence ≥ 0.90:
    → OUTPUT emitted with confidence_flag = HIGH
    → No disclaimer required
```

### D6 — Layer 5: Explainability Output Generator

Every prediction MUST produce an explainability report. No black-box output permitted.

```
Explainability Requirements:
  top_positive_signals    → Array[String]: top 5 signals driving salary upward
  top_negative_signals    → Array[String]: top 5 signals suppressing salary
  trust_weight_breakdown  → Object: per-signal-block contribution %
  market_anchor           → Object: market p50 used as reference
  belt_premium_applied    → Float: salary premium attributed to Dojo belt level
  championship_premium    → Float: salary premium from championship tier
  improvement_path        → Array[String]: 3 actions candidate can take to increase predicted salary

Explainability report stored in:  AuditLog (immutable, per Section P1 equivalent)
Employer access:                  Full report
Candidate access:                 Masked — top_positive_signals + improvement_path only
```

---

## 🔒 SECTION E — PREDICTION VARIANT MATRIX (SEALED)

The engine supports five prediction modes. All modes use the same underlying architecture but different input scopes.

| Mode ID | Mode Name | Scope | Primary Consumer | Trust Gate Required |
|---------|-----------|-------|-----------------|---------------------|
| PM-01 | Individual Prediction | Single candidate + single role | Recruiter Dashboard | All TG-01 to TG-08 |
| PM-02 | Comparative Prediction | Candidate pool for one role | Hiring Manager View | TG-07 + TG-08 |
| PM-03 | Retention Risk Band | Current employees + market delta | HR / Talent Ops | TG-07 |
| PM-04 | Team Composition Forecast | New team salary envelope | CFO / Finance | TG-07 |
| PM-05 | Market Benchmark Report | Role + region + industry | Enterprise Org Admin | TG-07 |

**Mode PM-01 is the base mode. All other modes are aggregations of PM-01 outputs.**

---

## 🔒 SECTION F — OUTPUT SCHEMA (SEALED)

```json
SalaryPrediction {
  prediction_id:          UUID,
  model_version:          "ASPE-v1.0.0",
  candidate_id:           UUID (verified),
  role_id:                String,
  recruiter_id:           UUID (verified),
  generated_at:           ISO 8601 timestamp,
  confidence_score:       Float [0–1],
  confidence_flag:        Enum["HIGH", "MODERATE", "SUPPRESSED"],
  currency:               String (ISO 4217),
  region:                 String (ISO 3166),

  salary_bands: {
    p10:                  Float,
    p25:                  Float,
    p50:                  Float,
    p75:                  Float,
    p90:                  Float,
    recommended_offer:    Float   // system recommendation = p50 adjusted for trust weight
  },

  trust_signals_summary: {
    dojo_belt:            String,
    dojo_confidence:      Float,
    work_reliability:     Float,
    intel_confidence:     Float,
    fraud_flag:           Boolean,
    collusion_flag:       Boolean
  },

  explainability: {
    top_positive_signals: Array[String],
    top_negative_signals: Array[String],
    belt_premium_pct:     Float,
    champ_premium_pct:    Float,
    market_anchor_p50:    Float,
    improvement_path:     Array[String]
  },

  audit_reference:        UUID   // links to immutable AuditLog entry
}
```

**Forbidden Output States:**

```
❌ Partial prediction without confidence score
❌ Salary output with confidence < 0.78 to employer
❌ Output without audit_reference
❌ Output without explainability block
❌ Prediction for unverified candidate (TG-01 failure)
❌ Prediction for recruiter without TG-07 pass
```

---

## 🔒 SECTION G — API CONTRACT REGISTRY (SEALED)

All endpoints below are mandatory. Absence of any endpoint → STOP EXECUTION.

```
POST   /salary-prediction/generate
       Body: { candidate_id, role_id, prediction_mode }
       Auth: Recruiter JWT (verified)
       Gate: All trust gates evaluated before processing
       Response: SalaryPrediction object

GET    /salary-prediction/{prediction_id}
       Auth: Recruiter JWT or Candidate JWT (scoped response)
       Response: Full object for recruiter; masked for candidate

GET    /salary-prediction/candidate/{candidate_id}/history
       Auth: Recruiter JWT (verified)
       Response: Array[SalaryPrediction] — historical predictions for candidate

POST   /salary-prediction/batch
       Body: { candidate_ids[], role_id }
       Auth: Recruiter JWT (verified)
       Mode: PM-02 Comparative Prediction
       Response: Array[SalaryPrediction] sorted by recommended_offer desc

GET    /salary-prediction/market-benchmark
       Query: { role, region, industry, currency }
       Auth: Org Admin JWT
       Mode: PM-05 Market Benchmark
       Response: Market signal summary + p10–p90 distribution

GET    /salary-prediction/retention-risk/{employee_id}
       Auth: HR Admin JWT
       Mode: PM-03 Retention Risk
       Response: SalaryPrediction with market_delta field

POST   /salary-prediction/team-forecast
       Body: { role_ids[], headcount_targets }
       Auth: CFO / Finance Admin JWT
       Mode: PM-04 Team Composition Forecast
       Response: Salary envelope forecast object

GET    /salary-prediction/audit/{prediction_id}
       Auth: Governance Board JWT only
       Response: Full immutable audit record

POST   /salary-prediction/appeal
       Body: { prediction_id, appeal_reason }
       Auth: Candidate JWT
       Response: Appeal ticket ID → routes to Governance Board (Section I)
```

---

## 🔒 SECTION H — DATABASE SCHEMA (SEALED)

Primary entities cannot be renamed. Fields may extend — not mutate.

```sql
SalaryPrediction (
  prediction_id         UUID PRIMARY KEY,
  model_version         VARCHAR NOT NULL,
  candidate_id          UUID NOT NULL REFERENCES User(id),
  role_id               VARCHAR NOT NULL,
  recruiter_id          UUID NOT NULL REFERENCES User(id),
  generated_at          TIMESTAMPTZ NOT NULL,
  confidence_score      FLOAT NOT NULL,
  confidence_flag       VARCHAR NOT NULL,
  currency              CHAR(3) NOT NULL,
  region                VARCHAR NOT NULL,
  salary_p10            NUMERIC(12,2),
  salary_p25            NUMERIC(12,2),
  salary_p50            NUMERIC(12,2),
  salary_p75            NUMERIC(12,2),
  salary_p90            NUMERIC(12,2),
  recommended_offer     NUMERIC(12,2),
  audit_reference       UUID NOT NULL REFERENCES AuditLog(id),
  suppressed            BOOLEAN DEFAULT FALSE,
  suppression_reason    VARCHAR
)

SalaryPredictionSignalSnapshot (
  snapshot_id           UUID PRIMARY KEY,
  prediction_id         UUID REFERENCES SalaryPrediction(prediction_id),
  signal_block          VARCHAR NOT NULL,  -- 'dojo' | 'intel' | 'work' | 'champ' | 'market' | 'profile'
  signal_key            VARCHAR NOT NULL,
  signal_value          JSONB NOT NULL,
  trust_weight          FLOAT NOT NULL,
  captured_at           TIMESTAMPTZ NOT NULL
)

SalaryPredictionExplainability (
  explainability_id     UUID PRIMARY KEY,
  prediction_id         UUID REFERENCES SalaryPrediction(prediction_id),
  top_positive_signals  JSONB NOT NULL,
  top_negative_signals  JSONB NOT NULL,
  belt_premium_pct      FLOAT,
  champ_premium_pct     FLOAT,
  market_anchor_p50     NUMERIC(12,2),
  improvement_path      JSONB NOT NULL
)

SalaryPredictionAuditLog (
  audit_id              UUID PRIMARY KEY,
  prediction_id         UUID NOT NULL,
  event_type            VARCHAR NOT NULL,
  actor_id              UUID,
  actor_role            VARCHAR,
  event_payload         JSONB NOT NULL,
  created_at            TIMESTAMPTZ NOT NULL DEFAULT NOW()
  -- Immutable: no UPDATE or DELETE permitted
)

SalaryPredictionAppeal (
  appeal_id             UUID PRIMARY KEY,
  prediction_id         UUID REFERENCES SalaryPrediction(prediction_id),
  candidate_id          UUID NOT NULL,
  appeal_reason         TEXT NOT NULL,
  status                VARCHAR DEFAULT 'PENDING',  -- PENDING | UNDER_REVIEW | RESOLVED
  governance_decision   TEXT,
  resolved_at           TIMESTAMPTZ,
  created_at            TIMESTAMPTZ NOT NULL DEFAULT NOW()
)

MarketSignalCache (
  signal_id             UUID PRIMARY KEY,
  role_key              VARCHAR NOT NULL,
  region                VARCHAR NOT NULL,
  currency              CHAR(3) NOT NULL,
  p10                   NUMERIC(12,2),
  p25                   NUMERIC(12,2),
  p50                   NUMERIC(12,2),
  p75                   NUMERIC(12,2),
  p90                   NUMERIC(12,2),
  demand_score          FLOAT,
  scarcity_index        FLOAT,
  refreshed_at          TIMESTAMPTZ NOT NULL,
  source                VARCHAR NOT NULL
)
```

**Row-level security enforced on all tables. Tenant isolation required.**
**SalaryPredictionAuditLog is append-only — no UPDATE or DELETE queries permitted via any role.**

---

## 🔒 SECTION I — GOVERNANCE & APPEALS LOCK

```
Appeals Authority:    ANTIGRAVITY Governance Board
Appeals Trigger:      Candidate disputes predicted salary range
Review SLA:           72 hours from appeal submission
Review Access:        Full AuditLog read + SalaryPredictionSignalSnapshot
Decision Types:       UPHELD | MODIFIED | RETRACTED
Decision Storage:     Immutable append to SalaryPredictionAuditLog

Low-Confidence Queue:
  All predictions with confidence < 0.78 → routed to review queue
  Review required before any employer-facing output permitted
  Reviewer: Governance Board or designated AI Review Officer

Model Retraining Authority:
  Only declared by: ANTIGRAVITY Technical Governance Board
  Trigger conditions:
    - Monthly scheduled cycle
    - Outcome validation signals drop below 0.70 correlation (Section T13)
    - Market signal drift detected (> 15% delta from expected)
    - Employer feedback signals trigger review (> 10 disputes in 30-day window)
```

---

## 🔒 SECTION J — UI SCREEN CONTRACT (SEALED)

All screens below are mandatory in Flutter and Next.js layers respectively.

**Flutter (Operational Interface — Recruiter + HR):**

```
SalaryPredictionGeneratorScreen
  - Candidate selector (verified only)
  - Role input + region selector
  - Prediction mode toggle (PM-01 to PM-05)
  - Trust gate status indicators (live, per TG-01 to TG-08)
  - [ GENERATE PREDICTION ] button → disabled if any trust gate fails
  - Result: Salary band visualization (range bar: p10–p90 with recommended_offer marker)

SalaryPredictionDetailScreen
  - Full SalaryPrediction object display
  - Explainability section: top signals (recruiter view = full)
  - Signal snapshot accordion: per-block trust weights
  - Confidence badge: HIGH / MODERATE / SUPPRESSED
  - Download PDF report button
  - Appeal status banner (if appeal active)

SalaryBenchmarkDashboard (PM-05 / Org Admin)
  - Role × Region × Industry filter panel
  - Market distribution chart (box-and-whisker: p10–p90)
  - YoY salary growth trend line
  - Skill demand heatmap

RetentionRiskMonitor (PM-03 / HR Admin)
  - Employee list with retention_risk_score
  - Market delta column: current comp vs predicted market p50
  - Flag: HIGH_RISK | MODERATE | SAFE
  - Batch re-prediction trigger

TeamForecastPlanner (PM-04 / CFO)
  - Role input grid with headcount targets
  - Salary envelope output: min / median / max per role
  - Total annual cost projection
  - Budget scenario comparison
```

**Next.js (SEO / Public-Facing — Read Only):**

```
CandidatePublicSalaryInsightPage
  - Candidate-facing: masked improvement_path only
  - Call to action: "Improve your predicted salary — start a Dojo match"
  - Belt level display + next belt salary premium estimate
  - No raw salary bands exposed publicly
```

---

## 🔒 SECTION K — INTEGRATION BINDING (SEALED)

No manual data sync permitted. Event-driven only.

```
Dojo Match End Event           → triggers: ASPE signal refresh (dojo signal block)
Dojo Belt Promotion Event      → triggers: ASPE prediction re-generation flag
Intelligence Test Complete     → triggers: ASPE intel block refresh
EIE Tool Sync Complete         → triggers: ASPE work block refresh
Championship Result Posted     → triggers: ASPE champ block refresh
Market Feed Refresh (daily)    → triggers: MarketSignalCache update
Fraud Flag Set                 → triggers: ASPE suppression of active predictions
Collusion Flag Set             → triggers: ASPE suppression of active predictions
Appeal Resolved                → triggers: AuditLog append + candidate notification
```

---

## 🔒 SECTION L — SECURITY & COMPLIANCE LOCK

```
Auth:
  All ASPE API routes require valid JWT
  Role enforcement: per-route RBAC (recruiter | hr_admin | org_admin | cfo | candidate | governance)
  Rate limiting: per IP + per user per endpoint
  WAF enforcement: required (inherited from ECOSKILLER P1)

Data:
  Salary predictions: PII-classified → encrypted at rest
  Signal snapshots: encrypted at rest, tenant-isolated
  AuditLog: append-only enforced at DB constraint level
  No salary prediction data crosses tenant boundaries

Consent:
  Candidate must grant salary_prediction_consent before PM-01 runs
  Consent record stored + timestamped in AuditLog
  Consent revocable — revocation → suppresses all active predictions

GDPR / Data Law:
  Right to erasure: suppresses predictions, retains anonymized aggregate only
  Data export: candidate may export their own signal snapshot history
  Retention policy: raw signal snapshots archived after 24 months
```

---

## 🔒 SECTION M — TEST GATE LOCK

No deployment without all test layers passing.

```
Unit Tests:
  - Confidence calibration formula correctness
  - Trust gate evaluation logic (all 8 gates, all failure paths)
  - Signal normalization boundary conditions
  - Salary band output bounds (p10 < p25 < p50 < p75 < p90 enforced)

Integration Tests:
  - End-to-end prediction from candidate_id to SalaryPrediction object
  - Trust gate failure → suppression verified
  - Market signal staleness fallback logic
  - Appeal workflow: submit → review → resolve → audit append

Regression Tests:
  - Known candidate profiles → predictions must stay within ±5% of v1.0.0 baseline
  - Market refresh cycle → zero downtime

Load Tests:
  - 500 concurrent PM-01 prediction requests
  - PM-02 batch of 200 candidates: < 8s response
  - MarketSignalCache refresh under load

Fairness Tests (Mandatory):
  - Prediction variance across verified regions must not exceed defined bias threshold
  - No demographic signal permitted in model input (enforced at schema level)
  - Bias audit report generated monthly by governance board

Test coverage threshold required before any ASPE version deployment.
```

---

## 🔒 SECTION N — OBSERVABILITY LOCK

```
Required Metrics:
  aspe.prediction.generated           → Counter per prediction_mode
  aspe.prediction.suppressed          → Counter (confidence < 0.78)
  aspe.trust_gate.failure             → Counter per gate_id
  aspe.confidence_score.histogram     → Distribution
  aspe.signal_staleness.gauge         → Per signal block
  aspe.market_feed.refresh_lag        → Gauge (hours since last refresh)
  aspe.appeal.open_count              → Gauge
  aspe.model.inference_latency_ms     → Histogram

Dashboards Required:
  - Prediction volume + suppression rate
  - Trust gate failure breakdown
  - Confidence score distribution over time
  - Market signal freshness tracker
  - Appeal queue depth

Alerts:
  - aspe.prediction.suppressed rate > 20% → ALERT
  - aspe.market_feed.refresh_lag > 72h → ALERT
  - aspe.model.inference_latency_ms p99 > 3000 → ALERT
  - aspe.trust_gate.failure (TG-06 fraud) spike → ALERT (immediate)
```

---

## 🔒 SECTION O — BELT SALARY PREMIUM MATRIX (SEALED)

Dojo belt level directly contributes to the trust-weighted salary premium calculation. The matrix below is versioned alongside belt rubric versions (Section T17).

```
Belt Level    | Salary Premium Band (applied to market p50)
--------------|-------------------------------------------------
WHITE         | ±0%        (baseline — no premium)
YELLOW        | +3% to +6%
GREEN         | +7% to +12%
BLUE          | +13% to +20%
BROWN         | +21% to +30%
BLACK         | +31% to +45%

Conditions:
  Premium applied ONLY if:
    dojo.score_confidence ≥ 0.78
    dojo.replay_authenticity_seal = TRUE
    dojo.mentor_score_override_rate < 0.35

  Premium SUPPRESSED if:
    dojo.collusion_flag = TRUE
    dojo.match_count < 5
    belt earned via auto-promotion (auto promotion FORBIDDEN per Section G of Dojo Spec)

  Belt premium is versioned: stored as (belt_level, belt_version, rubric_version) tuple
  Premium matrix version change requires Governance Board approval
```

---

## 🔒 SECTION P — CHAMPIONSHIP SALARY PREMIUM MATRIX (SEALED)

```
Championship Tier   | Salary Premium Band
--------------------|------------------------------
SCHOOL              | +1% to +2%
DISTRICT            | +2% to +4%
STATE               | +4% to +7%
NATIONAL            | +7% to +12%
CONTINENTAL         | +12% to +18%
WORLD               | +18% to +28%

Conditions:
  Premium applied ONLY if:
    champ.ai_evaluation_score ≥ 0.70
    champ.competition_count ≥ 2 at declared tier
    No active fraud flag on championship record

  Premiums are additive but capped:
    Total championship premium cannot exceed +28% of market p50
    Stack cap enforced in Layer 3 regression output
```

---

## 🔒 SECTION Q — VERSIONING & MUTATION GOVERNANCE LOCK

```
Model Version Format:     ASPE-vMAJOR.MINOR.PATCH
  MAJOR bump:             Scoring formula change / trust gate restructure / schema mutation
  MINOR bump:             New signal block added / premium matrix update
  PATCH bump:             Bug fix / monitoring update / market feed connector change

Version Tags Required On:
  - Model artifact
  - Belt premium matrix
  - Championship premium matrix
  - Market signal feed connector
  - Every SalaryPrediction output (model_version field)
  - Every AuditLog entry

Backward Compatibility Window:
  MAJOR version: 6-month parallel operation before retirement
  MINOR version: 3-month parallel operation
  PATCH version: Immediate cutover permitted

Allowed without version bump:
  - Add new market feed signal (additive only)
  - Update observability dashboards
  - Expand explainability improvement_path library

NOT allowed without MAJOR version bump:
  - Change trust gate logic
  - Change confidence formula weights
  - Change premium matrix values
  - Change output schema structure
  - Change database schema (destructive)
```

---

## 🔒 FINAL GOVERNANCE SEAL BLOCK

```
═══════════════════════════════════════════════════════════════════
ANTIGRAVITY SALARY PREDICTION ENGINE — SEAL v1.0.0
═══════════════════════════════════════════════════════════════════

ENTERPRISE OPTIMIZATION MODE:           ACTIVE
TRUST INFRASTRUCTURE:                   LOCKED
ECOSKILLER SIGNAL INTEGRATION:          REQUIRED
DOJO BELT PREMIUM ENGINE:               REQUIRED
CHAMPIONSHIP PREMIUM ENGINE:            REQUIRED
MARKET SIGNAL FEED:                     REQUIRED (Daily refresh)
CONFIDENCE GATE (0.78):                 ENFORCED
EXPLAINABILITY OUTPUT:                  MANDATORY
AUDIT LOG (IMMUTABLE):                  REQUIRED
APPEALS SYSTEM:                         ACTIVE
GOVERNANCE BOARD AUTHORITY:             REQUIRED
FAIRNESS BIAS AUDIT:                    MONTHLY
CANDIDATE CONSENT:                      PREREQUISITE
FRAUD DETECTION INTEGRATION:            REQUIRED
COLLUSION DETECTION INTEGRATION:        REQUIRED
PII ENCRYPTION:                         REQUIRED
RBAC ENFORCEMENT:                       REQUIRED
TEST GATE:                              REQUIRED BEFORE DEPLOY
VERSIONED MUTATION ONLY:                ENFORCED
INTERPRETATION AUTHORITY:               NONE
ARCHITECTURE AUTHORITY:                 LOCKED

Absence of ANY above component → STOP EXECUTION → REPORT INCOMPLETE

═══════════════════════════════════════════════════════════════════
ANTIGRAVITY ASPE-v1.0.0 | ECOSKILLER UNIFIED PLATFORM | SEALED
═══════════════════════════════════════════════════════════════════
```

---

*Document Class: Sealed Production Prompt Artifact*
*Mutation Policy: Add-Only via Version Bump*
*Issued Under: ECOSKILLER Master Execution Prompt v12.0 + Dojo SaaS Production Artifact Spec v1.0*
*Prepared For: ANTIGRAVITY Enterprise Build Team*
*Version: ASPE-v1.0.0*
