# 🔒 ANTIGRAVITY — INSTITUTE RANKING MODEL
## ECOSKILLER × DOJO SAAS × HR PREDICTIVE SYSTEMS
### INSTITUTE ML RANKING ENGINE — SEALED & LOCKED PRODUCTION ARTIFACT

```
ARTIFACT CLASS:       Institute Intelligence Production Model
SYSTEM NAMESPACE:     ANTIGRAVITY
EXECUTION MODE:       Deterministic · Sealed · Immutable
MUTATION POLICY:      Add-Only via Version Bump
INTERPRETATION AUTH:  NONE
CREATIVE FILLING:     FORBIDDEN
ASSUMPTION FILLING:   FORBIDDEN
DEFAULT BEHAVIOR:     DENY
FAILURE MODE:         STOP → REPORT → NO PARTIAL OUTPUT
VERSION:              v1.0.0-LOCKED
```

---

# ⚠️ SEAL DECLARATION

> This document is a **LOCKED PRODUCTION ARTIFACT** for the ANTIGRAVITY Institute Ranking subsystem of the Ecoskiller SaaS platform.
> No engine, interface, scoring formula, ML model weight schema, or entity definition may be altered without a **formal version bump** and **governance board approval**.
> Architecture Interpretation Authority: **NONE.**
> Execution Authority: **Human Declaration Only.**

---

# SECTION A — SYSTEM IDENTITY

```
SUBSYSTEM NAME:       ANTIGRAVITY
SUBSYSTEM TYPE:       Institute ML Ranking Engine
PARENT SYSTEM:        ECOSKILLER (Unified Talent Operating System)
EXECUTION CONTEXT:    Institute + HR Predictive Intelligence Layer
RANKING SCOPE:        Institutes (Schools · Colleges · Universities · Coaching Centers)
RANKING PURPOSE:      Intelligence-driven, multi-dimensional institute quality score
ML MODEL CLASS:       Supervised + Unsupervised Hybrid Predictive System
REAL-TIME MODE:       Event-driven continuous scoring (no static snapshots)
HUMAN OVERRIDE:       Required for governance-class decisions
AI AUTHORITY:         Advisory only — Never autonomous gate control
```

---

# SECTION B — ANTIGRAVITY CORE PHILOSOPHY

Antigravity is named for its mission: **to lift institutions that deserve to rise above gravity of reputation alone**. It measures **real outcomes, real skill production, real student achievement** — not legacy branding, not infrastructure optics.

Antigravity rejects:
- Ranking purely by infrastructure investment
- Ranking by historical reputation
- Ranking by any single metric
- Gaming via fee injection or recruiter volume manipulation

Antigravity enforces:
- Multi-dimensional evidence-based scoring
- Predictive outcome validity
- Temporal decay on stale performance
- Anti-collusion and anti-manipulation detection
- Full audit trail per institute per dimension

---

# SECTION C — PARALLEL EXECUTION LANES (LOCKED)

All scoring dimensions execute simultaneously via **event-driven parallel pipelines**. No dimension waits on another. Final composite score is computed only after all lanes complete.

```
Lane I1 — Student Intelligence Output Score (SIOS)
Lane I2 — Skill Production Quality Score (SPQS)
Lane I3 — Championship & Competition Performance Score (CCPS)
Lane I4 — Placement & Career Outcome Score (PCOS)
Lane I5 — Mentor & Educator Effectiveness Score (MEES)
Lane I6 — Curriculum Validity & Innovation Score (CVIS)
Lane I7 — Student Retention & Habit Strength Score (SRHS)
Lane I8 — Trust, Verification & Integrity Score (TVIS)
Lane I9 — Peer & Community Impact Score (PCIS)
Lane I10 — Employer Feedback & Predictive Validity Score (EFPVS)
```

All 10 lanes feed into → **ANTIGRAVITY COMPOSITE INSTITUTE SCORE (ACIS)**

---

# SECTION D — CONTRACT GATE SYSTEM

```
Data Contract Gate:      institute_verified = TRUE
                         student_count ≥ 5 (minimum sample for statistical validity)
                         activity_window ≥ 30 days

Event Gate:              lane_complete_signal from ALL 10 lanes required
                         before ACIS composite calculation begins

Governance Gate:         Rank publish requires audit_log_complete = TRUE
                         Any anomaly_flag → HOLD → human_review_required

Anti-Game Gate:          If manipulation_score > threshold → INVALIDATE → FLAG → REPORT
```

---

# SECTION E — DATA MODEL (FROZEN)

Primary entities **cannot be renamed**. Fields may extend — not mutate.

## E1 — Institute Entity

```
Institute {
  institute_id          UUID (PK)
  tenant_id             UUID (FK → Tenant)
  name                  STRING
  type                  ENUM [school | college | university | coaching]
  region                STRING
  verified_status       BOOLEAN
  verification_tier     ENUM [basic | standard | platinum]
  created_at            TIMESTAMP
  active_student_count  INTEGER
  acis_score            FLOAT (0–1000)
  acis_rank             INTEGER
  acis_percentile       FLOAT
  acis_version          STRING
  last_computed_at      TIMESTAMP
  anomaly_flag          BOOLEAN
  governance_hold       BOOLEAN
}
```

## E2 — Lane Score Entity

```
InstituteLaneScore {
  score_id              UUID (PK)
  institute_id          UUID (FK → Institute)
  lane_code             ENUM [I1..I10]
  raw_score             FLOAT
  normalized_score      FLOAT (0–100)
  confidence_score      FLOAT (0–1)
  decay_factor          FLOAT
  sample_size           INTEGER
  computed_at           TIMESTAMP
  model_version         STRING
  anomaly_detected      BOOLEAN
  audit_hash            STRING
}
```

## E3 — ACIS Composite Score Entity

```
ACISComposite {
  composite_id          UUID (PK)
  institute_id          UUID (FK → Institute)
  acis_score            FLOAT (0–1000)
  rank_global           INTEGER
  rank_by_type          INTEGER
  rank_by_region        INTEGER
  percentile            FLOAT
  score_breakdown       JSONB (lane weights + raw scores)
  confidence_band       STRING [low | medium | high | verified]
  computed_at           TIMESTAMP
  published_at          TIMESTAMP
  model_version         STRING
  governance_approved   BOOLEAN
  audit_trail_ref       UUID
}
```

## E4 — Employer Feedback Signal Entity

```
EmployerFeedbackSignal {
  signal_id             UUID (PK)
  institute_id          UUID (FK → Institute)
  employer_id           UUID (FK → Enterprise)
  hire_cohort_ref       UUID
  performance_rating    FLOAT (1–5)
  retention_months      INTEGER
  skill_gap_reported    BOOLEAN
  job_ready_score       FLOAT
  feedback_at           TIMESTAMP
  verified              BOOLEAN
}
```

## E5 — Institute Anomaly Log Entity

```
InstituteAnomalyLog {
  log_id                UUID (PK)
  institute_id          UUID (FK)
  lane_code             ENUM [I1..I10 | ACIS]
  anomaly_type          ENUM [score_inflation | collusion | data_spoof | 
                               match_farming | rapid_spike | employer_fake]
  severity              ENUM [low | medium | high | critical]
  detected_at           TIMESTAMP
  resolved_at           TIMESTAMP
  resolution_action     STRING
  governance_ref        UUID
}
```

---

# SECTION F — 10-LANE SCORING ARCHITECTURE

## LANE I1 — Student Intelligence Output Score (SIOS)

**What it measures:** Quality and growth of student intelligence (multiple intelligences framework) produced by the institute over time.

**Data Sources:**
- Ecoskiller Intelligence Test results per student (per institute)
- Pre-test vs post-test delta scores
- Intelligence radar chart distribution
- Longitudinal growth tracking

**Algorithm:**

```
SIOS_raw = 
  (avg_intelligence_delta × 0.40) +
  (intelligence_growth_consistency × 0.25) +
  (top_percentile_student_density × 0.20) +
  (multi-intelligence_spread_score × 0.15)

Temporal Decay:
  results older than 180 days → decay factor = 0.85
  results older than 365 days → decay factor = 0.65
  results older than 730 days → decay factor = 0.40

Confidence Modifier:
  IF sample_size < 10 → confidence = LOW → weight reduction applied
  IF sample_size ≥ 50 → confidence = HIGH → full weight applied
```

**Output:** SIOS_normalized (0–100) · Confidence band

---

## LANE I2 — Skill Production Quality Score (SPQS)

**What it measures:** Quality of verified skill production — are students of this institute actually developing market-relevant, dojo-verified skills?

**Data Sources:**
- Dojo Belt progression per student cohort (institute-bound)
- Skill certification audit records
- Mentor certification scores
- Match win rates and scoring quality
- Skill track completion rates

**Algorithm:**

```
SPQS_raw = 
  (avg_belt_level_per_cohort × 0.30) +
  (skill_certification_pass_rate × 0.25) +
  (mentor_calibration_score_of_institute_mentors × 0.20) +
  (skill_market_relevance_index × 0.15) +
  (skill_diversity_breadth × 0.10)

Anti-Gaming Rule:
  IF rapid_belt_promotion_cluster_detected → SIOS_penalty applied
  IF mentor_collusion_score > 0.7 → LANE INVALIDATED → anomaly_flag = TRUE
```

**Output:** SPQS_normalized (0–100)

---

## LANE I3 — Championship & Competition Performance Score (CCPS)

**What it measures:** Institute performance in Ecoskiller championships (school, district, state, national, global).

**Data Sources:**
- Championship match records (tournament engine)
- Medal and rank records per institute cohort
- Championship tier (local → global weighted differently)
- Student representation rate (breadth vs. elite cherry-picking)

**Algorithm:**

```
CCPS_raw = 
  (championship_rank_weighted_average × 0.35) +
  (participation_breadth_ratio × 0.25) +    // anti cherry-pick
  (championship_tier_multiplier × 0.25) +   // global > national > state > local
  (repeat_champion_consistency × 0.15)

Tier Multipliers:
  Global Championship = 3.0×
  National = 2.0×
  State = 1.5×
  District = 1.2×
  Local = 1.0×

Anti-Cherry-Pick Rule:
  participation_breadth_ratio = (students_who_competed / total_students) × 100
  IF ratio < 5% → CCPS_score capped at 60/100 regardless of medals
```

**Output:** CCPS_normalized (0–100)

---

## LANE I4 — Placement & Career Outcome Score (PCOS)

**What it measures:** Real career outcomes of students — offers, salary bands, retention, employer satisfaction.

**Data Sources:**
- Job offer records tied to institute (via Job Portal Engine)
- Employer feedback signals (Lane I10 feeds into this)
- Salary band distributions of placed students
- Time-to-placement after graduation
- Return offer rates

**Algorithm:**

```
PCOS_raw = 
  (placement_rate_within_6_months × 0.30) +
  (salary_band_quality_score × 0.25) +
  (employer_return_hire_rate × 0.20) +
  (job_skill_match_score × 0.15) +          // AI matching relevance
  (placement_consistency_over_3_years × 0.10)

Salary Band Scoring:
  salary > 2× local_median = 1.0
  salary = 1.5–2× = 0.85
  salary = 1–1.5× = 0.65
  salary < local_median = 0.30

Dependency Note:
  PCOS requires employer_verified = TRUE
  Unverified employer data → excluded from computation
```

**Output:** PCOS_normalized (0–100)

---

## LANE I5 — Mentor & Educator Effectiveness Score (MEES)

**What it measures:** Quality of teachers and mentors at the institute — calibrated via Dojo's mentor calibration system.

**Data Sources:**
- Mentor calibration scores (Section T3 of Dojo LOCK)
- Student improvement delta correlated to specific mentors
- Mentor certification levels and recertification records
- Override ethics audit scores
- Teaching effectiveness AI signals

**Algorithm:**

```
MEES_raw =
  (avg_mentor_calibration_score × 0.35) +
  (student_improvement_correlation_per_mentor × 0.30) +
  (mentor_certification_rate × 0.20) +
  (override_ethics_compliance_score × 0.15)

Hard Rule:
  IF any_mentor_outside_calibration_tolerance AND not_flagged → MEES_penalty = -10
  IF mentor_recertification_missed → individual mentor excluded from score pool
  IF >30% of institute mentors are uncertified → MEES_cap = 50/100
```

**Output:** MEES_normalized (0–100)

---

## LANE I6 — Curriculum Validity & Innovation Score (CVIS)

**What it measures:** How well the institute's curriculum tracks real-world skill demand, stays updated, and is governed correctly.

**Data Sources:**
- Skill demand index (Ecoskiller market intelligence)
- Curriculum version history (Section T8 content governance)
- Scenario difficulty calibration scores (Section T4)
- Curriculum approval workflow compliance
- Content gap analysis (AI-driven)

**Algorithm:**

```
CVIS_raw =
  (market_relevance_alignment_score × 0.35) +
  (curriculum_recency_score × 0.25) +       // version update frequency
  (scenario_difficulty_validity_score × 0.20) +
  (content_governance_compliance_score × 0.20)

Market Relevance:
  Computed against real-time job posting demand signals
  Curricula with >18-month-old topics score < 0.5 in relevance

Governance Penalty:
  Silent rubric changes (violating T8) → CVIS_invalidated → audit_hold
```

**Output:** CVIS_normalized (0–100)

---

## LANE I7 — Student Retention & Habit Strength Score (SRHS)

**What it measures:** Student engagement consistency — are students staying active, building habits, and returning to the platform?

**Data Sources:**
- Daily activity logs (Section R95)
- Streak data per student cohort (institute-bound)
- Course completion rates
- Study room activity
- Challenge participation rates

**Algorithm:**

```
SRHS_raw =
  (avg_streak_duration_per_cohort × 0.30) +
  (daily_active_return_rate × 0.30) +
  (course_completion_rate × 0.20) +
  (peer_learning_participation × 0.20)

Streak Scoring:
  avg streak ≥ 21 days = 1.0
  avg streak 14–21 = 0.85
  avg streak 7–14 = 0.65
  avg streak < 7 = 0.30

Anti-Padding Rule:
  IF activity_logs show bot-pattern clustering → SRHS_investigation → FLAG
```

**Output:** SRHS_normalized (0–100)

---

## LANE I8 — Trust, Verification & Integrity Score (TVIS)

**What it measures:** How trustworthy and fraud-free the institute's data and operations are.

**Data Sources:**
- Institute verification tier (basic / standard / platinum)
- Fraud detection signals (Trust System Module 15)
- Anomaly log history
- Collusion detection results (Section T9 Dojo LOCK)
- Economic abuse signals (Section T15)
- Dispute frequency and resolution quality

**Algorithm:**

```
TVIS_raw =
  (verification_tier_score × 0.30) +
  (anomaly_free_history_score × 0.30) +
  (dispute_resolution_quality × 0.20) +
  (economic_abuse_clean_score × 0.20)

Verification Tier Base Scores:
  Platinum = 1.0
  Standard = 0.75
  Basic    = 0.50
  Unverified → ACIS computation BLOCKED

Hard Penalties:
  critical anomaly in last 90 days → TVIS = 0 → ACIS governance_hold = TRUE
  unresolved high-severity anomaly → ACIS_publish blocked
```

**Output:** TVIS_normalized (0–100)

---

## LANE I9 — Peer & Community Impact Score (PCIS)

**What it measures:** The peer-to-peer, social, and community value the institute creates on the Ecoskiller platform.

**Data Sources:**
- Student endorsement volume and quality (R91)
- Study room creation and peer learning activity (R92)
- Social feed engagement and campus group activity (R93)
- Peer project collaboration rate (R94)
- Peer quiz challenge rates

**Algorithm:**

```
PCIS_raw =
  (endorsement_network_density × 0.25) +
  (study_room_active_rate × 0.25) +
  (social_feed_quality_engagement × 0.20) +
  (peer_project_completion_rate × 0.20) +
  (campus_community_health_score × 0.10)

Community Health Score:
  behavioral_safety_flag_rate (lower = better)
  harassment_report_density (lower = better)
  positive_reaction_ratio (higher = better)
```

**Output:** PCIS_normalized (0–100)

---

## LANE I10 — Employer Feedback & Predictive Validity Score (EFPVS)

**What it measures:** Real employer satisfaction with hires from the institute — the ultimate ground truth of institute quality.

**Data Sources:**
- Employer feedback signals (entity E4 above)
- Job performance correlation data
- Retention rate at employer level
- Skill gap reported rates
- Longitudinal job performance tracking (Section T13)

**Algorithm:**

```
EFPVS_raw =
  (avg_employer_performance_rating × 0.35) +
  (retention_at_12_months_rate × 0.25) +
  (skill_gap_absent_rate × 0.20) +
  (job_ready_score_avg × 0.20)

Validity Gate:
  Minimum 3 verified employer signals required for lane to activate
  IF < 3 signals → EFPVS = NULL → weight redistributed to other lanes
  IF employer_verified = FALSE → signal excluded

Longitudinal Bonus:
  3-year consistent employer satisfaction → +5 bonus points
```

**Output:** EFPVS_normalized (0–100)

---

# SECTION G — ACIS COMPOSITE FORMULA (IMMUTABLE)

```
ANTIGRAVITY COMPOSITE INSTITUTE SCORE (ACIS)

ACIS = Σ (Lane_Score × Lane_Weight × Confidence_Factor × Decay_Factor)

Lane Weights (LOCKED — Cannot change without version bump):

  I1  SIOS   — Student Intelligence Output          12%
  I2  SPQS   — Skill Production Quality             14%
  I3  CCPS   — Championship & Competition           10%
  I4  PCOS   — Placement & Career Outcomes          16%
  I5  MEES   — Mentor & Educator Effectiveness      12%
  I6  CVIS   — Curriculum Validity & Innovation      8%
  I7  SRHS   — Student Retention & Habit Strength    8%
  I8  TVIS   — Trust, Verification & Integrity      10%
  I9  PCIS   — Peer & Community Impact               5%
  I10 EFPVS  — Employer Feedback & Predictive Val.  15%
                                              Total 100%

Scale: 0 – 1000
  
  ACIS = (Σ weighted_lane_scores / 100) × 1000

Confidence Band Rules:
  All lanes HIGH confidence     → VERIFIED badge
  ≥7 lanes HIGH, rest MEDIUM   → HIGH
  ≥5 lanes MEDIUM+             → MEDIUM
  <5 lanes active              → LOW (no public rank publish allowed)

Governance Hold Triggers (auto):
  anomaly_flag = TRUE on any lane        → HOLD
  TVIS = 0                               → HOLD
  governance_approved = FALSE            → HOLD
  institute_verified = FALSE             → BLOCK (no score computed)
```

---

# SECTION H — ML MODEL SPECIFICATION

## H1 — Model Architecture

```
MODEL TYPE:           Multi-output Regression + Anomaly Detection Ensemble
TRAINING PARADIGM:    Supervised (outcome-labeled) + Unsupervised (anomaly)
INFERENCE MODE:       Streaming (event-triggered) + Batch (nightly recalibration)
```

## H2 — Feature Engineering

```
Per Lane Input Features (example for PCOS Lane I4):

  f1  placement_rate_30d
  f2  placement_rate_180d
  f3  placement_rate_365d
  f4  avg_salary_ratio_to_median
  f5  employer_return_hire_binary
  f6  job_skill_match_score_ecoskiller_ai
  f7  offer_acceptance_rate
  f8  time_to_placement_days
  f9  industry_diversity_entropy
  f10 placement_consistency_3y_variance

Feature Normalization:
  All continuous features → Min-Max normalization per lane
  Categorical → One-hot encoding
  Temporal → Rolling 90/180/365 window aggregations
```

## H3 — Model Training Rules

```
Training Data:
  Minimum 6 months of institute activity per training record
  Label source: Employer feedback signals (ground truth for outcome lanes)
  
Model Retraining:
  Nightly: Weight recalibration via online learning (incremental)
  Monthly: Full model retraining on fresh cohort data
  Quarterly: Architecture review + performance benchmarking

Validation:
  Train / Validation / Test split: 70 / 15 / 15
  Holdout = most recent 90 days (temporal holdout — no leakage)

Performance Metrics:
  RMSE per lane
  Rank correlation (Spearman) at composite level
  Calibration score (confidence band accuracy)
  Anomaly precision / recall
  
Minimum Performance Gates (must pass before deploy):
  Lane RMSE < 8.0 (on 0–100 scale)
  Spearman rank correlation ≥ 0.82
  Anomaly precision ≥ 0.88, recall ≥ 0.80
  Confidence calibration error < 0.05
```

## H4 — Anomaly & Anti-Gaming ML Layer

```
MODEL TYPE:        Isolation Forest + LSTM Sequence Anomaly Detector

Detects:
  - Score inflation via coordinated peer scoring (reciprocal cluster)
  - Match farming patterns (abnormal volume spikes)
  - Rating manipulation (rapid unnatural score climbs)
  - Employer signal spoofing (fake feedback injection)
  - Streak bot-padding
  - Collusion clusters between mentors and students

Action on Detection:
  LOW anomaly     → flag + monitor
  MEDIUM anomaly  → lane hold + human review queue
  HIGH anomaly    → ACIS governance_hold + institute alert
  CRITICAL        → ACIS_publish blocked + governance board escalation
```

---

# SECTION I — RANKING OUTPUT ARCHITECTURE

## I1 — Ranking Dimensions (Simultaneous — Not Mutually Exclusive)

```
Global Rank           — All institutes ranked globally
Type Rank             — Ranked within type [school | college | university | coaching]
Region Rank           — Ranked within geographic region
Intelligence Rank     — Ranked by I1 SIOS score only
Skill Rank            — Ranked by I2 SPQS score only
Placement Rank        — Ranked by I4 PCOS score only
Rising Star Rank      — Fastest ACIS improvement in 90 days (momentum metric)
Integrity Rank        — Ranked by I8 TVIS score (trust leaderboard)
```

## I2 — Rank Publication Rules

```
Public Rank:
  ACIS confidence_band ≥ MEDIUM → public rank visible
  ACIS confidence_band = LOW    → "Insufficient Data" shown publicly

Institute Admin View:
  Full breakdown per lane visible at all times to institute admin
  Anomaly flags visible to institute admin only

Platform Admin View:
  Full detail + anomaly logs + governance hold status
  
Rank Update Frequency:
  ACIS computed: Real-time (event-triggered, within 5 min of qualifying event)
  Public rank published: Every 24 hours (nightly batch publish)
  Championship / major event outcomes: Immediate rank update trigger
```

## I3 — Rising Star Algorithm

```
Rising_Star_Score = 
  (ACIS_today - ACIS_90days_ago) / ACIS_90days_ago × 100

Eligibility:
  institute_age ≥ 90 days
  ACIS_confidence ≥ MEDIUM
  anomaly_flag = FALSE
  
Rising Star Badge awarded if:
  Rising_Star_Score ≥ 15% in 90 days
  Sustained for ≥ 30 days (no gaming spike)
```

---

# SECTION J — API CONTRACT REGISTRY (LOCKED)

```
GET  /institutes/{id}/acis-score
  → Returns: ACIS composite + confidence band + rank positions

GET  /institutes/{id}/acis-breakdown
  → Returns: All 10 lane scores + weights + confidence + anomaly flags
  → Access: Institute admin + Platform admin only

GET  /rankings/global
  → Returns: Top-N global institute rankings (public)
  → Filters: type, region, min_confidence

GET  /rankings/by-dimension/{lane_code}
  → Returns: Dimension-specific ranking (e.g., placement, skill)

GET  /institutes/{id}/rising-star
  → Returns: Momentum score + Rising Star badge status

POST /institutes/{id}/employer-signal
  → Input: EmployerFeedbackSignal payload
  → Triggers: EFPVS lane recalculation
  → Auth: Verified employer only

GET  /institutes/{id}/anomaly-log
  → Access: Platform admin + Governance board only

POST /governance/acis-hold-release/{institute_id}
  → Releases ACIS governance hold after review
  → Auth: Governance board role only

GET  /institutes/{id}/acis-history
  → Returns: 365-day ACIS score history (graph data)
```

---

# SECTION K — UI SCREENS (LOCKED)

```
Institute Dashboard (Flutter — operational):
  K1 — ACIS Score Card (live, with confidence band)
  K2 — 10-Lane Radar Chart (all dimension scores)
  K3 — ACIS History Graph (365-day trend)
  K4 — Rank Position Badges (global / type / region)
  K5 — Rising Star Momentum Panel
  K6 — Anomaly Alert Banner (if flagged)
  K7 — Employer Feedback Summary Panel

Public SEO Page (React/Next.js — SEO surface):
  K8 — Public Institute Profile (ACIS score + public rank)
  K9 — Public Ranking Leaderboard (filterable)
  K10 — Dimension Rank Tables (Placement / Skill / Intelligence)
  K11 — Rising Star Showcase
  K12 — Verified Institute Badge Display
  K13 — Structured data schema (schema.org/EducationalOrganization)

Platform Admin (Flutter):
  K14 — Full ACIS Control Panel
  K15 — Governance Hold Management
  K16 — Anomaly Investigation Dashboard
  K17 — Model Performance Monitor (RMSE, calibration)
  K18 — Employer Signal Verification Queue
```

---

# SECTION L — SECURITY & GOVERNANCE LOCK

```
ACIS Score Access:
  Public score → Open
  Lane breakdown → Institute admin + Platform admin (JWT + RBAC)
  Anomaly logs → Governance board role only
  Model internals → Platform engineering only

Score Override:
  ACIS score cannot be manually overridden
  Lane scores cannot be manually overridden
  Only governance_hold can be released (by board)
  All hold releases immutably logged

Anti-Manipulation Hardening:
  Rate limiting on employer signal submissions
  Employer verification mandatory before signal counts
  ML anomaly scanner runs within 60 seconds of any lane score event
  All score events → immutable append-only event log

Audit:
  Every ACIS computation → audit_hash generated
  Every lane score → audit_hash generated
  Every rank publish → timestamped + version-tagged
  Every governance action → immutable log with board member identity
```

---

# SECTION M — INTEGRATION GLUE (EVENT-DRIVEN ONLY)

```
Dojo Match Engine → score events → Lane I2 (SPQS) trigger
Dojo Belt Promotion → certification events → Lane I2 (SPQS) trigger
Tournament Engine → result events → Lane I3 (CCPS) trigger
Job Portal Engine → offer/placement events → Lane I4 (PCOS) trigger
Mentor Certification → calibration events → Lane I5 (MEES) trigger
Curriculum CMS → content update events → Lane I6 (CVIS) trigger
Activity Log Service → habit events → Lane I7 (SRHS) trigger
Trust Engine → verification events → Lane I8 (TVIS) trigger
Social Feed Service → engagement events → Lane I9 (PCIS) trigger
Employer Feedback API → signal events → Lane I10 (EFPVS) trigger

ACIS Composite Trigger:
  All 10 lane_complete_signals received → ACIS computation job fired
  ACIS result → Institute Ranking Service → Public rank updated (nightly)
  ACIS result → Notification Service → Institute admin alerted (if rank change ≥5)
  ACIS result → Analytics Engine → historical record appended

No manual sync. Event-driven only. No polling allowed.
```

---

# SECTION N — DEVOPS & DEPLOYMENT LOCK

```
ANTIGRAVITY runs as isolated microservice cluster:

  Services:
    antigravity-lane-scorer (×10 parallel workers, one per lane)
    antigravity-acis-compositor
    antigravity-ml-inference-engine
    antigravity-anomaly-detector
    antigravity-rank-publisher
    antigravity-api-gateway

  Infrastructure:
    Kubernetes namespace: ecoskiller-antigravity
    Separate resource quotas per lane worker
    HPA (Horizontal Pod Autoscaler) on lane workers
    Redis Streams for event bus
    PostgreSQL (dedicated schema: antigravity)
    MinIO for ML model artifact storage

  CI/CD:
    Automated tests gate before deploy (RMSE + precision gates)
    Blue/green deploy with automatic rollback if RMSE degrades >5%
    Model version pinned per ACIS composite version

  Monitoring:
    Lane compute latency (alert if >5 min)
    ACIS composite latency (alert if >30 min after trigger)
    Anomaly detection latency (alert if >60 sec)
    Model drift detection (weekly automated report)
    Rank publish failure alerts
```

---

# SECTION O — TEST & QA LOCK

```
Required Test Gates (no production deploy without passing):

ML Model Tests:
  - RMSE per lane test
  - Rank correlation test
  - Confidence calibration test
  - Anomaly precision/recall test

Score Integrity Tests:
  - ACIS formula correctness test (known-answer test)
  - Weight sum = 1.0 verification
  - Temporal decay application test
  - Confidence band assignment test

Anti-Gaming Tests:
  - Reciprocal scoring cluster injection test → must detect
  - Employer signal spoofing test → must flag
  - Rapid spike simulation test → must trigger anomaly

API Contract Tests:
  - All endpoints return correct schema
  - RBAC enforcement per endpoint
  - Rate limit enforcement test

Event Pipeline Tests:
  - Lane trigger on correct event types
  - ACIS only fires when all 10 lanes complete
  - Governance hold blocks publish
```

---

# SECTION P — OBSERVABILITY LOCK

```
Required Dashboards:
  ANTIGRAVITY Lane Health Dashboard
    - Lane compute success rate per lane
    - Avg lane compute time
    - Confidence distribution

  ACIS Composite Dashboard
    - ACIS computation success rate
    - Time from event → ACIS update
    - Rank publish success rate

  ML Model Dashboard
    - Live RMSE per lane (rolling 7 days)
    - Model drift indicators
    - Retraining trigger log

  Anomaly Dashboard
    - Anomalies detected (by type, severity, institute)
    - Resolution time
    - Governance holds active

Alerts:
  Lane compute failure alert
  ACIS composite failure alert
  Anomaly detection service down alert
  Model RMSE degradation alert (>5% vs baseline)
  Governance hold unresolved >72h alert
```

---

# SECTION Q — VERSIONING & BACKWARD COMPATIBILITY

```
ANTIGRAVITY Model Version Schema: vMAJOR.MINOR.PATCH

MAJOR bump required:
  - Any change to ACIS lane weights
  - Any change to ML model architecture
  - Any change to scoring formula structure
  - Any change to lane definitions

MINOR bump required:
  - Adding new sub-features to an existing lane
  - Adding new anomaly detection pattern
  - Adding new API endpoint

PATCH:
  - Bug fixes
  - Performance optimizations
  - No behavioral change

Backward Compatibility:
  All ACIS scores stamped with model_version
  Historical scores remain accessible at version of computation
  Re-computation of historical scores NOT automatic (governance decision only)
  Institutes notified 30 days before MAJOR version deploy
```

---

# SECTION R — ANTIGRAVITY MASTER PROMPT INSERT BLOCK

> Paste this into the Ecoskiller master execution prompt when activating the ANTIGRAVITY subsystem:

```
═══════════════════════════════════════════════════════════════
BEGIN ANTIGRAVITY LOCKED ARTIFACT — v1.0.0
═══════════════════════════════════════════════════════════════

SUBSYSTEM:            ANTIGRAVITY — Institute Ranking ML Engine
PARENT:               ECOSKILLER v12.0 UNIFIED
EXECUTION MODE:       Deterministic · Event-Driven · Sealed
MUTATION POLICY:      Add-Only via Major/Minor/Patch version bump

LANE ARCHITECTURE:    10 Parallel Scoring Lanes
COMPOSITE SCORE:      ACIS (0–1000)
RANKING DIMENSIONS:   Global · Type · Region · Dimension-Specific

ML MODEL:             Supervised + Unsupervised Hybrid
INFERENCE MODE:       Streaming + Nightly Batch
RETRAINING CYCLE:     Nightly (incremental) · Monthly (full)

SECURITY:             JWT + RBAC + Immutable Audit Logs
GOVERNANCE:           Board-approval required for hold releases
ANTI-GAMING:          Isolation Forest + LSTM anomaly detection active
EMPLOYER VALIDATION:  Mandatory verified signal gate

STACK:                Kubernetes · Redis Streams · PostgreSQL · MinIO
UI:                   Flutter (admin) · React/Next.js (public SEO)
API:                  REST + OpenAPI 3.1 — all contracts frozen

SCORING OVERRIDE:     FORBIDDEN
MANUAL RANK SET:      FORBIDDEN
INTERPRETATION AUTH:  NONE
ARCHITECTURE AUTH:    LOCKED

SEALED CONTROLS:
  ✔ 10-Lane Parallel Scoring
  ✔ ACIS Composite (0–1000 scale)
  ✔ Confidence Band System
  ✔ Temporal Decay Enforcement
  ✔ Anomaly Detection Active
  ✔ Governance Hold System
  ✔ Employer Feedback Validation Gate
  ✔ Anti-Cherry-Pick Rules (CCPS)
  ✔ Anti-Collusion Detection (SPQS, TVIS)
  ✔ Rising Star Momentum Rank
  ✔ Multi-Dimension Public Leaderboard
  ✔ Immutable Audit Trail
  ✔ ML Model Gate Before Deploy
  ✔ Event-Driven Only (No Manual Sync)
  ✔ Full DevOps Automation (Kubernetes)
  ✔ Observability Dashboards Required
  ✔ Test Gates Mandatory Before Release

END ANTIGRAVITY LOCKED ARTIFACT
═══════════════════════════════════════════════════════════════
```

---

# SECTION S — FINAL GOVERNANCE SEAL

```
ANTIGRAVITY INSTITUTE RANKING MODE ENABLED
Evidence-Based Scoring Required
ML Model Gate Required Before Deploy
Employer Signal Validation Required
Anti-Gaming Detection Active
Temporal Decay Enforced
Confidence Band System Active
Governance Board Authority Enforced
Multi-Dimensional Ranking Active
Event-Driven Architecture Only
Immutable Audit Trail Active
No Manual Score Override Permitted
No Static Reputation Ranking Allowed
No Cherry-Pick Gaming Allowed
Institute Verification Required for Score
Stack Locked: Flutter + React + Kubernetes
Mutation: Add-Only Versioned
Architecture Interpretation: FORBIDDEN
```

---

*Document Class: ANTIGRAVITY Institute ML Ranking Model — Sealed Production Artifact*
*Model Version: v1.0.0-LOCKED*
*Parent System: ECOSKILLER DOJO SAAS v12.0*
*Issued Under: Institute + HR Predictive Systems Intelligence Layer*
*Sealed Date: 2026-02-27*
*Governance Status: LOCKED — No Unauthorized Mutations Permitted*
