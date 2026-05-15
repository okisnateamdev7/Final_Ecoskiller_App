# 🔒 ANTIGRAVITY — PLACEMENT PROBABILITY MODEL
## ECOSKILLER × DOJO SAAS × INSTITUTE + HR PREDICTIVE SYSTEMS
### STUDENT PLACEMENT ML PREDICTION ENGINE — SEALED & LOCKED PRODUCTION ARTIFACT

```
ARTIFACT CLASS:       HR Predictive Intelligence Production Model
SYSTEM NAMESPACE:     ANTIGRAVITY
SUBSYSTEM NAME:       PLACEMENT PROBABILITY MODEL (PPM)
EXECUTION MODE:       Deterministic · Sealed · Immutable · Event-Driven
MUTATION POLICY:      Add-Only via Version Bump
INTERPRETATION AUTH:  NONE
CREATIVE FILLING:     FORBIDDEN
ASSUMPTION FILLING:   FORBIDDEN
DEFAULT BEHAVIOR:     DENY
FAILURE MODE:         STOP → REPORT → NO PARTIAL OUTPUT
AI AUTHORITY:         Advisory Only — Never autonomous hire/reject gate
VERSION:              v1.0.0-LOCKED
SEALED DATE:          2026-02-27
GOVERNANCE STATUS:    LOCKED — No Unauthorized Mutations Permitted
```

---

# ⚠️ SEAL DECLARATION

> This document is a **LOCKED PRODUCTION ARTIFACT** for the ANTIGRAVITY Placement Probability ML subsystem of the Ecoskiller SaaS platform.
> No scoring formula, ML model architecture, feature definition, weight schema, prediction pipeline, API contract, or data entity may be altered without a **formal version bump** and **governance board approval**.
> Architecture Interpretation Authority: **NONE.**
> Execution Authority: **Human Declaration Only.**
> AI is advisory — it predicts, surfaces, and explains. It **never autonomously approves, rejects, or routes** a hiring decision.

---

# SECTION A — SYSTEM IDENTITY

```
SUBSYSTEM NAME:           ANTIGRAVITY — Placement Probability Model (PPM)
SUBSYSTEM TYPE:           Student-level HR Predictive ML Engine
PARENT SYSTEM:            ECOSKILLER v12.0 Unified Talent Operating System
EXECUTION CONTEXT:        Institute + HR Predictive Intelligence Layer
PREDICTION SCOPE:         Individual student placement probability
                          + Institute-level batch placement forecasting
                          + Recruiter match probability (bidirectional)
                          + Time-to-placement prediction
                          + Salary band prediction
                          + Offer acceptance probability

ML MODEL CLASS:           Supervised Gradient Boosting + Neural Embedding Hybrid
INFERENCE MODE:           Real-time per student event + Batch nightly cohort sweep
EXPLAINABILITY:           Mandatory — SHAP values per prediction
HUMAN OVERRIDE:           Required for all final hiring decisions
BIAS AUDIT:               Quarterly mandatory
PREDICTION HORIZON:       30 · 60 · 90 · 180 · 365 day windows
```

---

# SECTION B — PPM CORE PHILOSOPHY

The Placement Probability Model exists to answer three grounded questions with evidence-based, multi-dimensional ML predictions:

**For the Student:** *"What is my probability of placement, into which role, at which salary band, within what time window — and what specific actions will improve it?"*

**For the Institute:** *"What is the predicted placement rate for my upcoming cohort, which students need intervention, and how does my placement performance compare?"*

**For the Recruiter/HR:** *"What is the probability this candidate will perform, stay, and succeed — and what is the match quality between this student and this specific job?"*

PPM rejects:
- Placement prediction based on resume text or self-declared skills alone
- Binary placed/not-placed output with no actionability
- Black-box scores with no explainability
- Predictions that bypass human hiring authority
- Predictions that encode demographic bias

PPM enforces:
- Multi-signal evidence-based prediction (verified skills + real outcomes + behavioral data)
- SHAP explainability on every prediction surface
- Bias detection and fairness auditing across all protected dimensions
- Time-windowed predictions with confidence decay
- Student-facing action recommendations linked to prediction gaps
- Institute-facing cohort intervention alerts
- Full immutable audit trail on every prediction

---

# SECTION C — PARALLEL EXECUTION LANES (LOCKED)

All PPM signal dimensions are computed simultaneously via event-driven parallel pipelines. Final probability scores are computed only after all active lanes complete.

```
Lane P1  — Verified Skill Strength Signal (VSSS)
Lane P2  — Dojo Performance & Belt Signal (DPBS)
Lane P3  — Academic & Intelligence Signal (AIS)
Lane P4  — Project & Real-Work Evidence Signal (PRWES)
Lane P5  — Behavioral Habit & Consistency Signal (BHCS)
Lane P6  — Championship & Competition Signal (CCS)
Lane P7  — Social Proof & Endorsement Signal (SPES)
Lane P8  — Job Market Demand Alignment Signal (JMDAS)
Lane P9  — Historical Cohort Outcome Signal (HCOS)
Lane P10 — Employer & Recruiter Interaction Signal (ERIS)

All 10 lanes feed into →
  STUDENT PLACEMENT PROBABILITY SCORE (SPPS)
  TIME-TO-PLACEMENT PREDICTION (TTP)
  SALARY BAND PREDICTION (SBP)
  OFFER ACCEPTANCE PROBABILITY (OAP)
  JOB-ROLE FIT PROBABILITY (JRFP) [per job per student]
```

---

# SECTION D — CONTRACT GATE SYSTEM

```
Student Eligibility Gate:
  student_verified = TRUE
  minimum_activity_window ≥ 14 days
  minimum_signal_lanes_active ≥ 5 out of 10
  anomaly_flag = FALSE

Prediction Confidence Gate:
  confidence_score ≥ 0.40 → prediction surfaced (LOW band)
  confidence_score ≥ 0.65 → prediction surfaced (MEDIUM band)
  confidence_score ≥ 0.80 → prediction surfaced (HIGH band)
  confidence_score < 0.40 → "Insufficient data" shown, no score published

Employer Feedback Validation Gate:
  employer_verified = TRUE required for HCOS lane to accept outcome signals
  Unverified employer data → excluded from model training and inference

Bias Audit Gate:
  Quarterly bias audit must pass before model redeployment
  Fairness delta > threshold on any protected attribute → HOLD → governance review

Anti-Gaming Gate:
  manipulation_score > threshold on any signal lane → INVALIDATE + FLAG
  Batch collusion detected → cohort scores HELD pending governance review

Explainability Gate:
  Every prediction surfaced to any user MUST have SHAP breakdown attached
  Prediction without explainability → SYSTEM FAILURE → STOP
```

---

# SECTION E — DATA MODEL (FROZEN)

Primary entities **cannot be renamed**. Fields may extend — not mutate.

## E1 — Student Placement Profile Entity

```
StudentPlacementProfile {
  profile_id                  UUID (PK)
  student_id                  UUID (FK → User)
  institute_id                UUID (FK → Institute)
  tenant_id                   UUID (FK → Tenant)
  verified_status             BOOLEAN
  active_job_seeking          BOOLEAN
  preferred_roles             JSONB           // [role_id, ...]
  preferred_locations         JSONB
  preferred_salary_min        INTEGER
  salary_expectation_verified BOOLEAN
  placement_status            ENUM [seeking | placed | not_seeking | deferred]
  placed_at                   TIMESTAMP
  placement_role_id           UUID
  placement_employer_id       UUID
  placement_salary_band       ENUM
  last_signal_update_at       TIMESTAMP
  created_at                  TIMESTAMP
}
```

## E2 — Placement Probability Score Entity

```
PlacementProbabilityScore {
  score_id                    UUID (PK)
  student_id                  UUID (FK → Student)
  institute_id                UUID (FK → Institute)
  spps_30d                    FLOAT (0–1)     // placement prob in 30 days
  spps_60d                    FLOAT (0–1)
  spps_90d                    FLOAT (0–1)
  spps_180d                   FLOAT (0–1)
  spps_365d                   FLOAT (0–1)
  ttp_prediction_days         INTEGER         // predicted days to placement
  salary_band_prediction      ENUM [entry | mid | senior | lead | executive]
  salary_band_confidence      FLOAT
  offer_acceptance_prob       FLOAT (0–1)
  confidence_score            FLOAT (0–1)
  confidence_band             ENUM [low | medium | high | verified]
  lane_scores                 JSONB           // P1–P10 normalized scores
  shap_values                 JSONB           // SHAP per feature per lane
  action_recommendations      JSONB           // [{action, expected_delta, priority}]
  model_version               STRING
  computed_at                 TIMESTAMP
  anomaly_flag                BOOLEAN
  governance_hold             BOOLEAN
  audit_hash                  STRING
}
```

## E3 — Job-Role Fit Probability Entity

```
JobRoleFitProbability {
  fit_id                      UUID (PK)
  student_id                  UUID (FK → Student)
  job_id                      UUID (FK → Job)
  employer_id                 UUID (FK → Enterprise)
  fit_probability             FLOAT (0–1)
  fit_rank                    INTEGER         // rank among all candidates for this job
  fit_percentile              FLOAT
  skill_match_score           FLOAT
  culture_fit_signal          FLOAT
  growth_potential_score      FLOAT
  retention_probability       FLOAT (0–1)     // predicted 12-month retention
  performance_prediction      FLOAT (0–1)     // predicted performance band
  salary_expectation_match    BOOLEAN
  location_match              BOOLEAN
  shap_breakdown              JSONB
  model_version               STRING
  computed_at                 TIMESTAMP
  shown_to_recruiter_at       TIMESTAMP
  shown_to_student_at         TIMESTAMP
}
```

## E4 — Institute Cohort Placement Forecast Entity

```
InstituteCohortPlacementForecast {
  forecast_id                 UUID (PK)
  institute_id                UUID (FK → Institute)
  cohort_ref                  STRING          // e.g. "2026-Batch-CS"
  cohort_size                 INTEGER
  forecasted_placement_rate   FLOAT (0–1)     // % of cohort placed within 180d
  forecasted_avg_salary_band  ENUM
  at_risk_student_count       INTEGER         // students with SPPS_90d < 0.30
  intervention_required       BOOLEAN
  intervention_priority_list  JSONB           // [{student_id, spps_90d, gap_signals}]
  forecast_confidence         FLOAT
  forecast_window_days        INTEGER
  model_version               STRING
  computed_at                 TIMESTAMP
  published_at                TIMESTAMP
  governance_approved         BOOLEAN
}
```

## E5 — Placement Signal Event Entity (Append-Only)

```
PlacementSignalEvent {
  event_id                    UUID (PK)
  student_id                  UUID (FK → Student)
  event_type                  ENUM [skill_verified | belt_earned | project_completed |
                                    match_won | job_applied | interview_completed |
                                    offer_received | offer_accepted | offer_rejected |
                                    employer_feedback | endorsement_received |
                                    streak_milestone | championship_result |
                                    intelligence_score_updated]
  lane_code                   ENUM [P1..P10]
  event_payload               JSONB
  event_weight                FLOAT           // contribution to lane score
  occurred_at                 TIMESTAMP
  processed_at                TIMESTAMP
  source_system               STRING
  anomaly_detected            BOOLEAN
}
```

## E6 — Placement Prediction Audit Entity (Immutable)

```
PlacementPredictionAudit {
  audit_id                    UUID (PK)
  student_id                  UUID (FK → Student)
  prediction_type             ENUM [spps | ttp | salary_band | oap | jrfp | cohort_forecast]
  prediction_value            JSONB
  model_version               STRING
  features_snapshot           JSONB           // full feature vector at prediction time
  shap_snapshot               JSONB           // full SHAP values at prediction time
  shown_to_roles              JSONB           // [role, user_id, shown_at]
  bias_audit_ref              UUID
  computed_at                 TIMESTAMP
  immutable_hash              STRING
}
```

## E7 — Recruiter Prediction Interaction Entity

```
RecruiterPredictionInteraction {
  interaction_id              UUID (PK)
  recruiter_id                UUID (FK → User)
  student_id                  UUID (FK → Student)
  job_id                      UUID (FK → Job)
  fit_score_shown             FLOAT
  shortlisted                 BOOLEAN
  interviewed                 BOOLEAN
  offered                     BOOLEAN
  hired                       BOOLEAN
  outcome_feedback            JSONB           // actual outcome for model feedback loop
  interaction_at              TIMESTAMP
}
```

## E8 — Bias Audit Report Entity

```
BiasAuditReport {
  report_id                   UUID (PK)
  model_version               STRING
  audit_period_start          TIMESTAMP
  audit_period_end            TIMESTAMP
  dimensions_audited          JSONB           // [gender, region, institute_type, ...]
  fairness_metrics            JSONB           // {demographic_parity, eq_opportunity, ...}
  violations_detected         BOOLEAN
  violation_details           JSONB
  remediation_actions         JSONB
  approved_by                 UUID (FK → GovernanceBoard)
  approved_at                 TIMESTAMP
  model_cleared_for_deploy    BOOLEAN
}
```

---

# SECTION F — 10-LANE SIGNAL ARCHITECTURE

## LANE P1 — Verified Skill Strength Signal (VSSS)

**What it measures:** The quality and breadth of verified, platform-authenticated skills — not self-declared, not resume-stated.

**Data Sources:**
- Dojo skill certification records (belt-linked)
- Skill test scores per skill domain
- Mentor-certified skill assessments
- Tool-based skill tracking (GitHub integration, Figma, etc.)
- Ecoskiller Integration Engine (real work data: commits, PRs, Jira, Salesforce)

**Algorithm:**

```
VSSS_raw =
  (verified_skill_count × skill_market_demand_weight × 0.30) +
  (avg_skill_certification_score × 0.25) +
  (skill_depth_score [advanced vs. entry level mix] × 0.25) +
  (real_work_evidence_skill_score × 0.20)

Verification Tiers:
  Platform-certified (belt + mentor)     = weight 1.0
  Integration-verified (GitHub/Jira AI)  = weight 0.85
  Self-declared (resume, unverified)     = weight 0.10 (near-zero)

Market Demand Weight:
  Each skill carries a market_demand_index (refreshed weekly from job posting signals)
  High-demand skill = multiplier 1.5
  Stable-demand skill = multiplier 1.0
  Declining-demand skill = multiplier 0.7

Anti-Inflation Rule:
  IF skill_certification_achieved via flagged mentor → excluded
  IF skill count increased >200% in 30 days → anomaly_flag triggered
```

**Output:** VSSS_normalized (0–100)

---

## LANE P2 — Dojo Performance & Belt Signal (DPBS)

**What it measures:** Live performance in the Dojo skill arena — match results, belt level, mentor scores, scenario difficulty, consistency under pressure.

**Data Sources:**
- Dojo Match Engine records
- Belt progression history
- Scoring Engine results (peer + mentor + self merge)
- Pressure scenario pass rates (Section F, Dojo LOCK)
- Match win/loss rate over time
- Rating Engine Elo-equivalent rating

**Algorithm:**

```
DPBS_raw =
  (current_belt_level_normalized × 0.30) +
  (dojo_rating_percentile × 0.25) +
  (pressure_scenario_pass_rate × 0.20) +
  (scoring_consistency_score [low variance = good] × 0.15) +
  (match_frequency_recency_weighted × 0.10)

Belt Level Normalization:
  White  (0)  = 0.10
  Yellow (1)  = 0.25
  Orange (2)  = 0.40
  Green  (3)  = 0.55
  Blue   (4)  = 0.70
  Purple (5)  = 0.82
  Brown  (6)  = 0.91
  Black  (7)  = 1.00

Temporal Decay:
  Dojo activity > 90 days ago → decay_factor = 0.85
  Dojo activity > 180 days ago → decay_factor = 0.65

Pressure Scenario Rule:
  Pressure scenario is a hard gate:
  IF pressure_scenario_pass_rate < 0.30 → DPBS_cap = 50/100
```

**Output:** DPBS_normalized (0–100)

---

## LANE P3 — Academic & Intelligence Signal (AIS)

**What it measures:** Intelligence test outcomes, academic performance metrics, cognitive signal breadth — not GPA alone, but multi-dimensional intelligence profile.

**Data Sources:**
- Ecoskiller Intelligence Test scores (multiple intelligences framework)
- Intelligence growth delta (pre vs. post track)
- Academic record (if migrated / verified by institute)
- Intelligence percentile rank
- Cognitive breadth index (spread across intelligence types)

**Algorithm:**

```
AIS_raw =
  (intelligence_composite_score_normalized × 0.35) +
  (intelligence_growth_delta × 0.25) +         // improvement signal
  (cognitive_breadth_index × 0.20) +           // multi-intelligence spread
  (academic_performance_verified × 0.20)        // verified academic record

Intelligence Composite:
  Weighted average across:
  Linguistic, Logical-Mathematical, Spatial, Musical,
  Bodily-Kinesthetic, Interpersonal, Intrapersonal,
  Naturalist intelligences
  → weighted by job-role relevance per target role cluster

Academic Record Verification:
  Institute-issued (verified via Ecoskiller ERP) = weight 1.0
  Student-uploaded (unverified) = weight 0.25
  No record = weight 0.0 for this sub-component only

Confidence Note:
  IF intelligence_test_attempts < 2 → confidence_penalty applied
  IF intelligence_test_date > 365 days → decay_factor = 0.75
```

**Output:** AIS_normalized (0–100)

---

## LANE P4 — Project & Real-Work Evidence Signal (PRWES)

**What it measures:** Quality and quantity of real-world work evidence — project completions, portfolio quality, mentor-evaluated deliverables, integration-verified work artifacts.

**Data Sources:**
- Project Execution Engine records
- Portfolio auto-generated items (Section R, Ecoskiller)
- Mentor milestone evaluation scores
- GitHub integration (commits, PR quality, repo complexity)
- Figma / design tool integration (design artifact quality)
- Jira / ClickUp integration (task completion, delivery consistency)
- AI skill extraction from real work data

**Algorithm:**

```
PRWES_raw =
  (project_count_quality_weighted × 0.25) +
  (mentor_milestone_evaluation_avg × 0.25) +
  (real_work_integration_evidence_score × 0.25) +
  (portfolio_completeness_score × 0.15) +
  (work_delivery_consistency_score × 0.10)

Project Quality Weighting:
  Mentor-evaluated + platform-hosted = 1.0
  Peer-evaluated + platform-hosted   = 0.75
  Self-posted, no evaluation         = 0.20

Real Work Integration Score:
  GitHub: commit_frequency × code_complexity × bug_rate_inverse
  Jira: task_completion_rate × on_time_rate × complexity_avg
  Figma: project_count × review_score (if available)
  Salesforce: pipeline_managed × close_rate (if available)

Delivery Consistency:
  Measures: started vs. completed projects ratio
  Abandoned projects = negative signal
```

**Output:** PRWES_normalized (0–100)

---

## LANE P5 — Behavioral Habit & Consistency Signal (BHCS)

**What it measures:** Long-term learning consistency, daily activity patterns, streak data, platform engagement quality — proxy for professional reliability.

**Data Sources:**
- Daily activity logs (Section R95, Ecoskiller)
- Streak tracker (current and max streaks)
- Course completion rates
- Study room participation
- Challenge completion rate
- Peer quiz engagement
- Login recency and frequency pattern

**Algorithm:**

```
BHCS_raw =
  (streak_score × 0.30) +
  (course_completion_rate × 0.25) +
  (daily_active_rate_30d × 0.20) +
  (peer_learning_participation × 0.15) +
  (challenge_completion_rate × 0.10)

Streak Scoring:
  current_streak ≥ 30 days = 1.0
  current_streak 14–29 = 0.80
  current_streak 7–13  = 0.60
  current_streak 3–6   = 0.35
  current_streak < 3   = 0.10

Recency Weighting:
  Activity in last 7 days  = weight 2.0
  Activity in last 30 days = weight 1.5
  Activity in last 90 days = weight 1.0
  Activity > 90 days       = weight 0.5

Professional Reliability Proxy Rationale:
  Behavioral consistency on platform → correlated with
  job task completion consistency (validated via employer feedback loop)

Anti-Padding:
  Bot-pattern activity detected → lane invalidated + anomaly_flag
```

**Output:** BHCS_normalized (0–100)

---

## LANE P6 — Championship & Competition Signal (CCS)

**What it measures:** Student performance in competitive arenas — championships, tournaments, ranked matches — proxy for performance under pressure and competitive drive.

**Data Sources:**
- Championship records (Tournament Engine)
- Dojo ranked match history
- Competition tier (local → global)
- Participation breadth vs. specialization
- Final round reach rate

**Algorithm:**

```
CCS_raw =
  (championship_rank_score × 0.35) +
  (competition_tier_multiplier_weighted × 0.30) +
  (finals_reach_rate × 0.20) +
  (participation_consistency × 0.15)

Tier Multipliers:
  Global Championship medal    = 3.0×
  National Championship medal  = 2.0×
  State Championship medal     = 1.5×
  District / Local medal       = 1.0×

Rank Score:
  1st place = 1.0
  2nd–3rd   = 0.85
  Top 10%   = 0.70
  Top 25%   = 0.55
  Participated = 0.20

Null State:
  No competition history → CCS = NULL
  Weight redistributed to other active lanes
  (Student not penalized for non-participation)
```

**Output:** CCS_normalized (0–100) or NULL

---

## LANE P7 — Social Proof & Endorsement Signal (SPES)

**What it measures:** Peer endorsements, mentor recommendations, verifiable social credibility on the platform — quality-weighted, not volume-gamed.

**Data Sources:**
- Student endorsement records (Section R91)
- Mentor recommendation letters / ratings
- Peer project co-leader ratings
- Campus community leadership signals
- Verified endorsement quality score (endorser credibility weighted)

**Algorithm:**

```
SPES_raw =
  (endorsement_quality_weighted_score × 0.40) +
  (mentor_recommendation_score × 0.35) +
  (community_leadership_score × 0.15) +
  (peer_project_rating × 0.10)

Endorsement Quality Weighting:
  Endorser belt level × endorser_credibility_score × skill_relevance

Anti-Gaming Rule:
  Reciprocal endorsement detection (mutual inflation clusters)
  IF reciprocal_pair_score > threshold → both endorsements discounted 80%
  IF endorsement_velocity > 10/week → anomaly_flag + review hold

Mentor Recommendation Weight:
  Certified mentor = 1.0
  Uncertified mentor = 0.0 (excluded from this lane entirely)
```

**Output:** SPES_normalized (0–100)

---

## LANE P8 — Job Market Demand Alignment Signal (JMDAS)

**What it measures:** How well the student's verified skill set, target roles, and location preferences align with real-time job market demand signals.

**Data Sources:**
- Job posting demand index (Ecoskiller Job Portal Engine + external signals)
- Student's verified skill set vs. most demanded skills
- Target role cluster alignment
- Location preference vs. hiring activity heatmap
- Salary expectation vs. market band for target role

**Algorithm:**

```
JMDAS_raw =
  (skill_to_demand_match_score × 0.35) +
  (role_cluster_demand_score × 0.30) +
  (location_demand_alignment × 0.20) +
  (salary_expectation_market_fit × 0.15)

Skill-to-Demand Match:
  For each verified skill:
    market_demand_index × skill_certification_score
  Aggregate across top-5 skills by demand

Role Cluster Demand Score:
  Demand trend for target role cluster (30d / 90d / 365d)
  Rising demand = 1.0 multiplier
  Stable demand = 0.85
  Declining demand = 0.60

Salary Expectation Market Fit:
  IF expectation within ±15% of market median = 1.0
  IF expectation < market = 1.0 (more hireable)
  IF expectation > market +30% = 0.50

Market Index Refresh:
  Updated weekly from job posting signals
  Stale index (> 14 days) → weight reduced
```

**Output:** JMDAS_normalized (0–100)

---

## LANE P9 — Historical Cohort Outcome Signal (HCOS)

**What it measures:** Placement outcomes of past students from the same institute, same cohort profile, similar skill clusters — statistical base rate signal.

**Data Sources:**
- Historical placement records (Section R, Job Portal Engine)
- Institute cohort placement history (verified employer outcomes)
- Segment: same institute + same domain + similar SPPS profile
- Employer retention history for cohort graduates

**Algorithm:**

```
HCOS_raw =
  (institute_historical_placement_rate × 0.35) +
  (cohort_profile_match_placement_rate × 0.35) +
  (employer_retention_rate_for_cohort × 0.20) +
  (placement_trend_direction × 0.10)      // improving vs declining

Cohort Profile Match:
  Find students from same institute in past 3 years with:
  - similar VSSS range ±10
  - similar DPBS range ±10
  - same domain track
  → Compute their actual placement rate and outcome distribution

Minimum Cohort Size for Signal:
  Minimum 10 historical students in matched cohort
  IF < 10 → HCOS = NULL → weight redistributed

Institute Trend Direction:
  Comparing placement rate: current 12m vs prior 12m
  Improving = 1.0 bonus factor
  Declining = 0.85 penalty factor

Employer Verified Requirement:
  Only employer-verified placements count
  Unverified self-reported placements excluded
```

**Output:** HCOS_normalized (0–100) or NULL

---

## LANE P10 — Employer & Recruiter Interaction Signal (ERIS)

**What it measures:** Real recruiter and employer interaction signals — shortlists, interview invites, application outcomes — the most direct real-world hiring demand signal.

**Data Sources:**
- Job application lifecycle records (Applied → Shortlisted → Interviewed → Hired)
- Recruiter interaction records (RecruiterPredictionInteraction entity)
- Interview satisfaction ratings (employer side)
- Offer receipt rate (without placement)
- Application-to-shortlist conversion rate
- Response rate on recruiter-initiated contact

**Algorithm:**

```
ERIS_raw =
  (shortlist_rate_last_90d × 0.30) +
  (interview_invitation_rate × 0.25) +
  (offer_receipt_rate × 0.25) +
  (recruiter_initiated_contact_rate × 0.20)

Recency Weighting:
  Events in last 30 days = weight 2.5
  Events in last 90 days = weight 1.5
  Events in last 180 days = weight 1.0
  Events > 180 days       = weight 0.4

Null State:
  IF no applications submitted in last 180 days → ERIS = NULL
  Weight redistributed to other active lanes
  (Passive students not penalized — signal simply absent)

Positive Signal Amplifier:
  IF recruiter_initiated (not student-applied) → weight × 1.3
  (Recruiter reaching out unprompted = strong demand signal)
```

**Output:** ERIS_normalized (0–100) or NULL

---

# SECTION G — PLACEMENT PROBABILITY COMPOSITE FORMULAS (IMMUTABLE)

## G1 — Student Placement Probability Score (SPPS)

```
SPPS(t) = Σ [Lane_Score(i) × Lane_Weight(i) × Confidence(i) × Decay(i)]
          for active lanes i ∈ {P1..P10}

Lane Weights (LOCKED — Cannot change without version bump):

  P1  VSSS  — Verified Skill Strength                18%
  P2  DPBS  — Dojo Performance & Belt                15%
  P3  AIS   — Academic & Intelligence                10%
  P4  PRWES — Project & Real-Work Evidence           14%
  P5  BHCS  — Behavioral Habit & Consistency         10%
  P6  CCS   — Championship & Competition              6%  [NULL-redistributable]
  P7  SPES  — Social Proof & Endorsement              7%
  P8  JMDAS — Job Market Demand Alignment            10%
  P9  HCOS  — Historical Cohort Outcome               6%  [NULL-redistributable]
  P10 ERIS  — Employer & Recruiter Interaction        4%  [NULL-redistributable]
                                              Total 100%

NULL Lane Weight Redistribution:
  IF lane = NULL → weight proportionally distributed across other active lanes
  IF active_lanes < 5 → SPPS not computed → "Insufficient Data"

Output Scale:
  SPPS expressed as probability (0.00 – 1.00)
  Display format: percentage with confidence band

Time-Windowed Outputs:
  SPPS_30d  — placement probability within 30 days
  SPPS_60d  — placement probability within 60 days
  SPPS_90d  — placement probability within 90 days
  SPPS_180d — placement probability within 180 days
  SPPS_365d — placement probability within 365 days

  Each window uses time-decay weighting:
  Short-window (30d) → recent signals weighted 3× more heavily
  Long-window (365d) → broader signal history included
```

## G2 — Time-to-Placement Prediction (TTP)

```
TTP = Regression model trained on:
  Input:  SPPS components + market demand index + historical cohort TTP
  Output: predicted_days_to_placement

  Distribution:
    Point estimate: median predicted days
    Confidence interval: 10th–90th percentile range
    E.g.: "43 days (range: 28–72 days)"

  Recalibration:
    Model updated nightly with new placement outcome signals
```

## G3 — Salary Band Prediction (SBP)

```
SBP = Multi-class classifier trained on:
  Input:  Skill signals + Belt level + Intelligence score + Location + 
          Role cluster demand + Historical cohort salary outcomes
  Output: Predicted salary band (entry | mid | senior | lead | executive)
          + Probability distribution across bands
          + Confidence score

  Regional Normalization:
    Salary bands defined per region (India, UAE, US, etc.)
    Bands updated quarterly from verified outcome data
```

## G4 — Offer Acceptance Probability (OAP)

```
OAP = Binary classifier trained on:
  Input:  Salary expectation vs. market band + Location preference match +
          Role fit score + Student behavioral signals (engagement rate) +
          Historical acceptance/rejection patterns for similar profiles
  Output: P(accept offer | offer received) per job_id

  Use: Recruiter-facing signal to prioritize outreach
       Institute-facing to understand student conversion
```

## G5 — Job-Role Fit Probability (JRFP)

```
JRFP(student, job) = 
  (skill_match_score × 0.35) +
  (experience_evidence_match × 0.25) +
  (intelligence_role_alignment × 0.15) +
  (cultural_fit_signal × 0.10) +
  (salary_expectation_match × 0.10) +
  (location_match_binary × 0.05)

  Output: Fit probability (0–1) + fit_percentile among all candidates
  Computed per (student, job) pair at job application event
  Updated if student signals change materially (>5% shift in SPPS)
```

---

# SECTION H — ML MODEL SPECIFICATION

## H1 — Model Architecture Stack

```
PRIMARY MODEL:          Gradient Boosting (XGBoost / LightGBM)
                        → For SPPS, TTP, JRFP computation
                        → Proven on tabular recruitment data
                        → Interpretable with SHAP

EMBEDDING LAYER:        Neural embedding model
                        → Encodes skill vectors, role vectors, JD vectors
                        → Powers JRFP semantic matching
                        → Embeddings frozen per version

SEQUENCE MODEL:         LSTM / Transformer-lite
                        → Behavioral sequence (habit, engagement pattern)
                        → Powers BHCS lane
                        → Temporal signal modeling

ANOMALY LAYER:          Isolation Forest + Statistical Process Control
                        → Powers anti-gaming detection per signal lane

EXPLAINABILITY:         SHAP (SHapley Additive exPlanations)
                        → Mandatory on every prediction surface
                        → Feature-level and lane-level attribution

BIAS DETECTION:         Fairlearn + custom fairness metrics
                        → Mandatory quarterly audit
```

## H2 — Feature Engineering (Locked Baseline)

```
Global Student Features:
  f_verified_skill_count
  f_avg_skill_cert_score
  f_top_skill_market_demand_index
  f_real_work_evidence_score
  f_belt_level_normalized
  f_dojo_rating_percentile
  f_pressure_scenario_pass_rate
  f_intelligence_composite
  f_intelligence_growth_delta
  f_cognitive_breadth_index
  f_academic_record_verified
  f_project_count_quality_weighted
  f_mentor_milestone_avg
  f_portfolio_completeness
  f_streak_current
  f_course_completion_rate
  f_daily_active_rate_30d
  f_endorsement_quality_score
  f_mentor_recommendation_score
  f_skill_demand_alignment
  f_salary_expectation_market_fit
  f_historical_cohort_placement_rate
  f_shortlist_rate_90d
  f_offer_receipt_rate
  f_recruiter_initiated_rate
  f_application_to_shortlist_conversion
  f_time_since_last_placement_signal
  f_activity_recency_weighted

Job-Specific Features (for JRFP):
  j_required_skills_vector
  j_salary_band
  j_location
  j_seniority_level
  j_company_size
  j_industry_domain
  j_job_posting_recency
  j_applicant_count_log

Feature Normalization:
  Continuous → Min-Max per feature across training population
  Categorical → Target encoding (validated for leakage)
  Temporal → Rolling windows: 7d / 30d / 90d / 180d / 365d aggregates
  Embedding → L2-normalized dense vectors

Feature Drift Monitoring:
  PSI (Population Stability Index) computed weekly per feature
  PSI > 0.25 → model retraining triggered immediately
  PSI > 0.10 → drift alert issued
```

## H3 — Model Training Governance

```
Training Data Requirements:
  Minimum training records: 1,000 students with verified placement outcomes
  Label definition: placement_within_Nd = 1 if placed within window, else 0
  Ground truth source: employer-verified placement records only
  Data freshness: last 36 months, recency-weighted

Train / Validation / Test Split:
  70% training
  15% validation (tuning)
  15% test (temporal holdout — most recent 90 days, NO leakage)

Model Versioning:
  Each trained model receives version hash
  Version pinned to prediction output forever
  Rollback to prior version must be achievable in < 30 minutes

Retraining Schedule:
  Nightly:  Incremental update with new placement outcome signals
  Weekly:   Feature drift check → retrain if PSI threshold exceeded
  Monthly:  Full retraining on complete sliding window dataset
  Quarterly: Architecture review + bias audit + performance benchmark

Minimum Performance Gates (must pass before deploy):
  SPPS AUC-ROC ≥ 0.82 (for 90d window)
  SPPS Precision@Top10% ≥ 0.75
  TTP prediction MAE < 15 days
  JRFP AUC-ROC ≥ 0.78
  SBP accuracy (correct band) ≥ 0.70
  SHAP completeness: 100% of predictions have SHAP values
  Bias audit: demographic_parity_delta < 0.05 on all protected attributes
  
  Failure on any gate → STOP DEPLOYMENT → governance review required
```

## H4 — SHAP Explainability Implementation (Mandatory)

```
SHAP computed: Per prediction, per student, per time window

Student-facing explanation format:
  "Your placement probability is X%"
  Top 3 positive factors (green):
    e.g., "Strong verified skills in Python (+12%)"
    e.g., "Consistent daily activity streak (+8%)"
    e.g., "High Dojo belt level — Purple (+9%)"
  Top 3 improvement opportunities (amber):
    e.g., "No real-world project evidence (−14%)"
    e.g., "Salary expectation above market band (−7%)"
    e.g., "Low market demand for target skills (−9%)"

Institute-facing explanation format:
  Per-student SHAP breakdown for at-risk students
  Aggregate SHAP attribution for cohort forecast
  "Primary risk factor driving low cohort prediction: [factor]"

Recruiter-facing explanation format:
  JRFP breakdown per student per job
  Top 3 fit factors (positive + negative)
  Retention probability explanation

SHAP Storage:
  Full SHAP vector stored in PlacementPredictionAudit per prediction
  Queryable for governance and dispute resolution
  Immutable after storage
```

## H5 — Bias Detection & Fairness Framework

```
Protected Attributes Monitored:
  gender
  region / state / country
  institute_type (school / college / coaching)
  institute_tier (private / public / rural / urban)
  family_income_band (if collected — optional)
  age_cohort

Fairness Metrics Computed Quarterly:
  Demographic Parity Difference (DPD)
    = |P(predict_placed | group A) - P(predict_placed | group B)|
    Threshold: DPD < 0.05

  Equal Opportunity Difference (EOD)
    = |P(predict_placed | placed=1, group A) - P(predict_placed | placed=1, group B)|
    Threshold: EOD < 0.05

  Calibration by Group
    = Model should be equally well-calibrated across all groups

Bias Remediation Actions (if violated):
  Reweighting training samples
  Fairness constraints during training (in-processing)
  Post-processing threshold adjustment per group
  Governance board review required before any remediation deploy

Audit Trail:
  Every bias audit → BiasAuditReport entity created
  model_cleared_for_deploy = FALSE until bias audit passes
  All audit reports immutable and governance-signed
```

---

# SECTION I — PREDICTION OUTPUT SURFACES

## I1 — Student-Facing Prediction Dashboard

```
Shown to: Student (authenticated Flutter app only)
Content:
  SPPS_30d / 60d / 90d / 180d display (probability bar with confidence band)
  TTP prediction ("Estimated placement: 43–72 days")
  SBP prediction ("Expected salary band: Mid-level ₹6–10L")
  OAP for active job applications
  SHAP-powered Top 3 strengths + Top 3 improvement gaps
  Action Recommendations (ranked by expected SPPS delta)
    Example: "Complete a project in your target domain → +12% SPPS_90d"
    Example: "Earn Blue Belt → +9% SPPS_90d"
    Example: "Align salary expectation to market → +7% SPPS_90d"
  Historical SPPS trend chart (90-day history)
  Confidence band indicator

Access Control: Student sees only own prediction. Always.
```

## I2 — Institute-Facing Cohort Forecast Dashboard

```
Shown to: Institute Admin (Flutter app — institute module)
Content:
  Institute-wide cohort placement rate prediction
  At-risk student list (SPPS_90d < threshold, e.g., 0.30)
    With: primary gap signals per student (SHAP-driven)
    With: recommended institute intervention per student
  Cohort SPPS distribution histogram
  Cohort TTP distribution
  Cohort SBP distribution
  Comparison vs. historical cohort performance
  Comparison vs. peer institutes (anonymized benchmark)
  Month-over-month SPPS trend per cohort
  Intervention priority queue (sorted by impact × urgency)

Access Control:
  Institute sees only own students
  Cross-institute student data: FORBIDDEN
  Individual student identifiers visible only to institute admin for own students
```

## I3 — Recruiter-Facing Candidate Fit Dashboard

```
Shown to: Verified Recruiter (Flutter app — recruiter module)
Content:
  Per job: ranked candidate list by JRFP
  Per candidate:
    JRFP score + percentile among all applicants
    Retention probability (12-month)
    Performance prediction band
    Salary expectation match
    Top 3 fit factors (SHAP-driven)
    Verified skill match breakdown
    Dojo belt + match history summary
    OAP (probability candidate accepts if offered)

Access Control:
  Recruiter sees only candidates who applied to their job
    OR candidates who opted into recruiter discovery
  Student must opt-in to recruiter discovery mode
  Recruiter cannot see SPPS_overall — only JRFP for their specific job
  Prediction audit logged every time recruiter views a score
```

## I4 — Platform Admin Monitoring Dashboard

```
Shown to: Platform Admin only
Content:
  Global SPPS distribution health
  Model performance metrics (AUC, MAE, calibration)
  Feature drift dashboard (PSI per feature)
  Bias audit status and metrics
  Anomaly detection log
  Governance holds active
  At-risk institute cohort alerts
  Prediction volume and latency metrics
  Model version deployment status
```

---

# SECTION J — ACTION RECOMMENDATION ENGINE

```
For every student with SPPS < 0.70, generate ranked action recommendations:

Recommendation Generation:
  For each improvement gap identified by SHAP:
    Map gap to actionable platform feature
    Estimate expected SPPS_90d delta from completing action
    Assign priority score = expected_delta × student_urgency × market_timing

Recommendation Categories:
  Skill Actions:
    "Earn [Skill] certification in Dojo → +X% SPPS"
    "Complete [Real-world project type] → +X% SPPS"
  
  Belt Actions:
    "Advance to [Next Belt] in [Skill Domain] → +X% SPPS"
    "Pass [Pressure Scenario] → +X% SPPS"
  
  Behavioral Actions:
    "Maintain 14-day streak → +X% SPPS"
    "Complete [Course] → +X% SPPS"
  
  Market Alignment Actions:
    "Update salary expectation to [range] → +X% SPPS"
    "Add [High-demand skill] to profile → +X% SPPS"
  
  Social Proof Actions:
    "Request mentor recommendation → +X% SPPS"
    "Join peer project in [domain] → +X% SPPS"

Recommendation Delivery:
  In-app notification (Flutter push)
  Dashboard top panel (high priority)
  Weekly summary email
  AI Mentor interface integration

Recommendation Tracking:
  Track: recommendation shown → action taken → SPPS delta observed
  Feedback loop into recommendation model accuracy
```

---

# SECTION K — API CONTRACT REGISTRY (LOCKED)

```
Student Placement Predictions:
  GET  /students/{id}/placement-probability
    → Returns: SPPS_{30d,60d,90d,180d,365d} + confidence + TTP + SBP
    → Auth: Student (own only) | Institute Admin (own students) | Platform Admin

  GET  /students/{id}/placement-probability/explanation
    → Returns: Full SHAP breakdown + action recommendations
    → Auth: Student (own only) | Institute Admin (own students)

  GET  /students/{id}/placement-probability/history
    → Returns: 90-day SPPS trend
    → Auth: Student (own only) | Institute Admin

Job-Role Fit:
  GET  /jobs/{job_id}/candidates/ranked
    → Returns: Ranked candidate list with JRFP + explanations
    → Auth: Verified Recruiter (own jobs only)

  GET  /students/{id}/jobs/{job_id}/fit-probability
    → Returns: JRFP for specific student-job pair + SHAP + OAP
    → Auth: Recruiter (for applied/discovered candidates) | Student (own)

Institute Cohort:
  GET  /institutes/{id}/cohort-placement-forecast
    → Returns: InstituteCohortPlacementForecast
    → Auth: Institute Admin (own institute only) | Platform Admin

  GET  /institutes/{id}/at-risk-students
    → Returns: Students with SPPS_90d < threshold + gap signals
    → Auth: Institute Admin (own institute only)

Employer Feedback:
  POST /employer-feedback/submit
    → Input: EmployerFeedbackSignal (verified employer only)
    → Triggers: HCOS + ERIS lane update → SPPS recompute

Model Governance:
  GET  /ppm/model-performance
    → Returns: AUC, MAE, calibration, drift metrics
    → Auth: Platform Admin only

  GET  /ppm/bias-audit/latest
    → Returns: BiasAuditReport
    → Auth: Platform Admin | Governance Board

  POST /ppm/governance/hold-release/{student_id}
    → Releases prediction hold after review
    → Auth: Governance Board role only

  GET  /ppm/audit/{prediction_id}
    → Returns: Full PlacementPredictionAudit
    → Auth: Platform Admin | Governance Board
```

---

# SECTION L — UI SCREENS (LOCKED)

```
Student Screens (Flutter — operational interface):
  L1  — Placement Probability Card (dashboard widget)
  L2  — Full SPPS Detail Screen (time windows + confidence)
  L3  — SHAP Explanation Screen ("Why this score?")
  L4  — Action Recommendations Screen (ranked list + expected delta)
  L5  — SPPS History Chart Screen (90-day trend)
  L6  — TTP Prediction Widget
  L7  — Salary Band Prediction Screen
  L8  — Job Fit Score (per application)

Institute Admin Screens (Flutter — institute module):
  L9  — Cohort Placement Forecast Dashboard
  L10 — At-Risk Students List (with intervention priority)
  L11 — Student SPPS Drilldown (per student gap signals)
  L12 — Cohort SPPS Distribution Histogram
  L13 — Cohort TTP Distribution
  L14 — Historical vs. Predicted Placement Rate Comparison
  L15 — Intervention Alert Feed

Recruiter Screens (Flutter — recruiter module):
  L16 — Ranked Candidate List (by JRFP per job)
  L17 — Candidate Fit Card (JRFP + top 3 factors)
  L18 — Retention Probability Indicator
  L19 — Salary Expectation Match Badge
  L20 — OAP Indicator (offer acceptance likelihood)

Public SEO Screens (React/Next.js — read-only, indexable):
  L21 — Institute Predicted Placement Rate (public figure, aggregated)
  L22 — Program-level Placement Forecast (anonymous cohort data)

Platform Admin Screens (Flutter — admin module):
  L23 — Model Performance Dashboard
  L24 — Feature Drift Monitor
  L25 — Bias Audit Dashboard
  L26 — Governance Hold Queue
  L27 — Anomaly Detection Log
  L28 — Prediction Volume & Latency Monitor
```

---

# SECTION M — SECURITY & GOVERNANCE LOCK

```
Data Access Isolation:
  Student prediction → student sees own only
  Institute admin → own students only, cross-institute FORBIDDEN
  Recruiter → applied/opted-in students only, own jobs only
  Employer feedback → own hire outcomes only
  Prediction audit log → Governance Board + Platform Admin only

Prediction Override:
  SPPS scores CANNOT be manually overridden
  JRFP scores CANNOT be manually overridden
  Only governance_hold status can be changed (by Governance Board)
  All hold releases immutably logged

Privacy Controls:
  Student may opt-out of recruiter discovery (OAP hidden from recruiter)
  Student may download own prediction history (data portability)
  Student may request prediction explanation audit
  PII excluded from model training feature vectors

Audit:
  Every prediction computation → PlacementPredictionAudit record created
  Every recruiter view of a candidate score → logged
  Every bias audit → BiasAuditReport created + governance-signed
  Every governance hold/release → immutable audit log entry
  Audit records: immutable, append-only, hash-verified

Anti-Gaming Enforcement:
  Signal manipulation detection per lane (Isolation Forest)
  Endorsement reciprocal pair detection (SPES Lane)
  Activity bot-pattern detection (BHCS Lane)
  Employer signal spoofing detection (HCOS Lane)
  Anomaly detected → HOLD → human review → resolve or invalidate

Regulatory Compliance:
  GDPR-compliant data handling (EU student opt-out right)
  Right to explanation enforced (SHAP mandatory)
  Data retention policy: predictions retained 7 years (audit purposes)
  PII deletion on student account closure (model features anonymized)
```

---

# SECTION N — INTEGRATION GLUE (EVENT-DRIVEN ONLY)

```
Signal Events → PPM Lane Triggers:

  Dojo Match Engine      → match_result_event      → P2 (DPBS) update
  Dojo Belt Engine       → belt_promotion_event     → P2 (DPBS) update
  Skill Certification    → skill_certified_event    → P1 (VSSS) update
  Project Engine         → milestone_completed_event → P4 (PRWES) update
  Integration Hub        → work_artifact_event       → P1 + P4 update
  Activity Logger        → daily_activity_event      → P5 (BHCS) update
  Championship Engine    → match_result_event        → P6 (CCS) update
  Endorsement Service    → endorsement_event         → P7 (SPES) update
  Job Portal Engine      → application_event         → P10 (ERIS) update
  Job Portal Engine      → shortlist_event           → P10 (ERIS) update
  Job Portal Engine      → offer_event               → P10 (ERIS) update
  Employer Feedback API  → feedback_submitted_event  → P9 (HCOS) update
  Intelligence System    → test_completed_event      → P3 (AIS) update
  Market Demand Service  → demand_refresh_event      → P8 (JMDAS) update

Lane Complete → SPPS Composite:
  10 lane_complete_signals (or null + weight redistribution) →
  SPPS computation job fired →
  PlacementProbabilityScore entity created →
  PlacementPredictionAudit record created →
  Student notification fired (if significant change ≥ 5%) →
  Institute notification fired (if at-risk threshold crossed)

SPPS → Downstream:
  SPPS updated → ACIS Institute Ranking Model (I4 PCOS Lane) signal fired
  SPPS updated → Cohort Placement Forecast recomputed (nightly batch)
  JRFP updated → Recruiter candidate ranking refreshed
  Employer feedback received → HCOS signal updated → SPPS recomputed

No manual sync. Event-driven only. No polling allowed.
```

---

# SECTION O — DEVOPS & DEPLOYMENT LOCK

```
PPM runs as isolated microservice cluster:

  Services:
    ppm-lane-scorer (×10 parallel workers, one per lane)
    ppm-spps-compositor
    ppm-ml-inference-engine (XGBoost + LSTM + embedding models)
    ppm-explainability-engine (SHAP computation)
    ppm-recommendation-engine (action recommendation generation)
    ppm-anomaly-detector
    ppm-bias-auditor (scheduled quarterly + on-demand)
    ppm-api-gateway
    ppm-cohort-forecaster (nightly batch)

  Infrastructure:
    Kubernetes namespace: ecoskiller-ppm
    Separate resource quotas per lane worker
    HPA on lane workers and inference engine
    GPU-backed node pool for embedding + LSTM inference
    Redis Streams for event bus
    PostgreSQL (dedicated schema: ppm)
    MinIO for ML model artifact storage
    Vault for model secrets and API keys

  CI/CD:
    All performance gates must pass before deploy
    Blue/green deploy with automatic rollback if AUC degrades >3%
    Model version pinned per SPPS composite version
    SHAP completeness check in CI (100% required)
    Bias audit must have passed within last 90 days before major deploy

  Monitoring:
    Lane compute latency (alert if >3 min)
    SPPS composite latency (alert if >20 min after trigger)
    SHAP computation latency (alert if >5 min per prediction)
    Inference engine error rate (alert if >0.1%)
    Model drift PSI alerts
    Bias metric live dashboard
    Anomaly detection latency (alert if >60 sec)
```

---

# SECTION P — TEST & QA LOCK

```
Required Test Gates (no production deploy without all passing):

ML Model Tests:
  AUC-ROC per window (30d / 60d / 90d / 180d / 365d)
  Precision@Top10% test
  TTP MAE test
  JRFP AUC test
  SBP accuracy test
  Calibration curve test
  Bias audit fairness delta test

SHAP Tests:
  SHAP completeness: 100% of test set predictions have SHAP
  SHAP sign consistency: positive features increase score
  SHAP sum = model output (Shapley consistency check)

Score Integrity Tests:
  Weight sum = 1.0 for active lanes verification
  NULL lane weight redistribution correctness
  Confidence band assignment correctness
  Temporal decay application test

Anti-Gaming Tests:
  Reciprocal endorsement injection → SPES lane must discount
  Bot-pattern activity injection → BHCS lane must flag
  Employer signal spoofing → HCOS lane must reject

API Contract Tests:
  All endpoints return correct schema
  Student cannot access other student predictions
  Recruiter cannot access non-applied student predictions
  Rate limit enforcement

Event Pipeline Tests:
  Correct lane fires on correct event type
  SPPS only fires after lane signals resolve
  Audit record created for every prediction
  Governance hold blocks prediction publish
```

---

# SECTION Q — OBSERVABILITY LOCK

```
Required Dashboards:

  PPM Lane Health Dashboard:
    Lane compute success rate per lane (×10)
    Avg lane compute latency
    Signal event volume per lane
    Lane NULL rate (when weight redistribution kicks in)

  SPPS Composite Dashboard:
    SPPS computation success rate
    Time from trigger event to SPPS update
    SPPS distribution across student population
    At-risk student count trends

  ML Model Dashboard:
    Live AUC-ROC (rolling 30-day validation)
    Feature PSI heatmap (drift detection)
    TTP MAE rolling average
    SHAP top contributors (population-level)

  Bias & Fairness Dashboard:
    Demographic Parity Difference per group (live)
    Equal Opportunity Difference per group (live)
    Calibration by group
    Last bias audit date + result

  Anomaly & Integrity Dashboard:
    Anomalies detected by type + lane
    Governance holds active
    Resolution queue

  Recruiter Interaction Accuracy:
    Predicted JRFP vs. actual shortlist/hire outcomes
    OAP prediction accuracy (rolling)

Alerts:
  Lane compute failure alert
  SPPS composite failure alert
  Model AUC degradation alert (>3% drop)
  Feature drift PSI > 0.10 alert
  Feature drift PSI > 0.25 → immediate retrain trigger
  Bias metric violation alert
  SHAP computation failure alert
  Anomaly detector down alert
  Governance hold unresolved > 72h alert
  At-risk cohort spike alert (if institute at-risk student count +20% in 7 days)
```

---

# SECTION R — VERSIONING & BACKWARD COMPATIBILITY

```
PPM Model Version Schema: vMAJOR.MINOR.PATCH

MAJOR bump required:
  Any change to SPPS lane weights
  Any change to ML model architecture
  Any change to SPPS formula structure
  Any change to lane definitions or algorithms
  Any change to SHAP methodology
  Any change to bias fairness thresholds

MINOR bump required:
  Adding new sub-features to an existing lane
  Adding new anomaly detection pattern
  Adding new recommendation action type
  Adding new prediction output (new window or score type)

PATCH:
  Bug fixes
  Performance optimizations
  No behavioral change

Backward Compatibility:
  All predictions stamped with model_version
  Historical predictions remain accessible at model_version of computation
  Re-computation of historical predictions NOT automatic
  Institutes notified 30 days before MAJOR version deploy
  Students notified in-app of model version changes (plain language)

Audit Permanence:
  PlacementPredictionAudit records are PERMANENT
  Cannot be deleted — only archived
  Available for dispute resolution indefinitely
```

---

# SECTION S — ANTIGRAVITY PPM MASTER PROMPT INSERT BLOCK

> Paste this sealed block into the Ecoskiller master execution prompt when activating the ANTIGRAVITY Placement Probability Model subsystem:

```
═══════════════════════════════════════════════════════════════════════
BEGIN ANTIGRAVITY PPM LOCKED ARTIFACT — v1.0.0
═══════════════════════════════════════════════════════════════════════

SUBSYSTEM:             ANTIGRAVITY — Placement Probability Model (PPM)
PARENT SYSTEM:         ECOSKILLER v12.0 UNIFIED + ANTIGRAVITY ACIS v1.0.0
EXECUTION MODE:        Deterministic · Event-Driven · Sealed
MUTATION POLICY:       Add-Only via Major/Minor/Patch version bump

PREDICTION OUTPUTS:
  SPPS     Student Placement Probability Score (30/60/90/180/365d windows)
  TTP      Time-to-Placement Prediction (days, with confidence interval)
  SBP      Salary Band Prediction (multi-class, regional)
  OAP      Offer Acceptance Probability (per student per job)
  JRFP     Job-Role Fit Probability (per student per job)
  COHORT   Institute Cohort Placement Forecast

LANE ARCHITECTURE:     10 Parallel Signal Lanes (P1–P10)
ML MODEL:              XGBoost + LSTM + Neural Embeddings Hybrid
INFERENCE MODE:        Real-time (event-triggered) + Nightly Batch
EXPLAINABILITY:        SHAP mandatory on every prediction surface
BIAS AUDIT:            Quarterly mandatory — model blocked if violated
RETRAINING:            Nightly (incremental) · Weekly (drift-triggered) · Monthly (full)

PREDICTION AUTHORITY:  ADVISORY ONLY
                       AI never approves, rejects, or routes hiring decisions
                       Human authority required for all final decisions

SECURITY:              JWT + RBAC + Row-level student isolation
PRIVACY:               Student opt-out of recruiter discovery respected
                       PII excluded from model feature vectors
                       Right to explanation enforced (SHAP)
GOVERNANCE:            Bias audit gate enforced before deploy
                       Governance board controls hold releases
                       Immutable prediction audit trail mandatory

ANTI-GAMING:           Isolation Forest + SPC per signal lane
                       Reciprocal endorsement detection active
                       Bot-pattern activity detection active
                       Employer signal spoofing detection active

STACK:                 Kubernetes · Redis Streams · PostgreSQL ·
                       MinIO · GPU node pool for inference
UI:                    Flutter (operational) · React/Next.js (public SEO)
API:                   REST + OpenAPI 3.1 — all contracts frozen

SEALED CONTROLS:
  ✔ 10-Lane Parallel Signal Architecture
  ✔ 5-Window Probability Prediction (30/60/90/180/365d)
  ✔ Time-to-Placement Prediction
  ✔ Salary Band Prediction (regional)
  ✔ Offer Acceptance Probability
  ✔ Job-Role Fit Probability (bidirectional)
  ✔ Institute Cohort Placement Forecast
  ✔ At-Risk Student Intervention Alerts
  ✔ SHAP Explainability (mandatory, every surface)
  ✔ Action Recommendations (SHAP-driven, delta-ranked)
  ✔ Bias Detection & Fairness Framework (Fairlearn)
  ✔ Quarterly Bias Audit Gate (model blocked if violated)
  ✔ Feature Drift Monitoring (PSI weekly)
  ✔ NULL Lane Weight Redistribution
  ✔ Confidence Band System (low/medium/high/verified)
  ✔ Temporal Decay Enforcement per signal lane
  ✔ Anti-Gaming Detection (Isolation Forest + SPC)
  ✔ Employer Signal Verification Gate
  ✔ Immutable Prediction Audit Trail
  ✔ Governance Hold System
  ✔ Student Data Isolation (sees own predictions only)
  ✔ Recruiter Isolation (sees only applied/opted-in students)
  ✔ Minimum Performance Gates Before Deploy
  ✔ Full DevOps Automation (Kubernetes + GPU pool)
  ✔ Observability Dashboards Required
  ✔ Test Gates Mandatory Before Release
  ✔ ACIS Integration (PPM feeds I4 PCOS lane of Institute Ranking)

END ANTIGRAVITY PPM LOCKED ARTIFACT
═══════════════════════════════════════════════════════════════════════
```

---

# SECTION T — FINAL GOVERNANCE SEAL

```
ANTIGRAVITY PLACEMENT PROBABILITY MODEL ENABLED
Evidence-Based Prediction Required (No Resume-Only Scoring)
SHAP Explainability Mandatory on Every Prediction Surface
Bias Audit Gate Required Before Model Deploy
Demographic Parity Delta < 0.05 Enforced
Student Data Isolation Enforced (own predictions only)
Recruiter Isolation Enforced (applied/opted-in only)
AI Authority: Advisory Only — Never Autonomous Hiring Gate
Human Authority Required for All Final Hiring Decisions
Employer Signal Verification Required (unverified excluded)
Anti-Gaming Detection Active on All 10 Signal Lanes
Temporal Decay Enforced per Lane
NULL Lane Weight Redistribution Active
Confidence Band System Active
Governance Hold System Active
Immutable Prediction Audit Trail Mandatory
Minimum ML Performance Gates Enforced Before Deploy
Feature Drift Monitoring Active (PSI weekly)
Automatic Retrain Trigger on PSI > 0.25
Stack Locked: Flutter + React + Kubernetes + GPU Pool
Mutation: Add-Only Versioned
Architecture Interpretation: FORBIDDEN
Bias Remediation: Governance Board Approval Required
ACIS Integration: Active (PPM feeds ACIS I4 PCOS Lane)
```

---

*Document Class: ANTIGRAVITY Placement Probability Model — Sealed Production Artifact*
*Model Version: v1.0.0-LOCKED*
*Parent System: ECOSKILLER DOJO SAAS v12.0 + ANTIGRAVITY ACIS v1.0.0*
*Issued Under: Institute + HR Predictive Systems Intelligence Layer*
*Sealed Date: 2026-02-27*
*Governance Status: LOCKED — No Unauthorized Mutations Permitted*
*Interpretation Authority: NONE*
