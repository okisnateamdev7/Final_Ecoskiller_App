# 🔒 LEADERBOARD STABILITY MODEL — PREDICTIVE AI EDITION
## ANTIGRAVITY SEALED MASTER PROMPT v2.0
### ECOSKILLER + DOJO (ARTS DOMAIN) — CHAMPIONSHIP ADVANCED + PARENT PREDICTIVE AI SYSTEM

---

```
╔══════════════════════════════════════════════════════════════════════════╗
║  ARTIFACT CLASS      : Production Leaderboard + Predictive AI Blueprint  ║
║  ARTIFACT VERSION    : v2.0                                               ║
║  EXECUTION ENGINE    : ANTIGRAVITY                                        ║
║  EXECUTION MODE      : LOCKED · DETERMINISTIC · SEALED · ADD-ONLY        ║
║  MUTATION POLICY     : VERSION BUMP ONLY (no field renames, no merges)   ║
║  INTERPRETATION      : FORBIDDEN                                          ║
║  CREATIVE_FILL       : FORBIDDEN                                          ║
║  ASSUMPTION_FILL     : FORBIDDEN                                          ║
║  DEFAULT_BEHAVIOR    : DENY                                               ║
║  FAILURE MODE        : STOP → REPORT → NO PARTIAL OUTPUT                 ║
║  PARENT SYSTEM       : ECOSKILLER ENTERPRISE MULTI-TENANT SAAS           ║
║  SUB-SYSTEM          : DOJO ENGINE (ARTS DOMAIN)                         ║
║  DOMAIN SCOPE        : ARTS TRACK ONLY                                   ║
║  AI AUTHORITY        : ADVISORY ONLY — NEVER FINAL                       ║
║  HUMAN OVERRIDE      : ALWAYS PRESERVED                                  ║
╚══════════════════════════════════════════════════════════════════════════╝
```

---

> ### ⚠️ ANTIGRAVITY BINDING DIRECTIVE — MUST READ BEFORE EXECUTION
>
> This is a fully sealed, deterministic, production-grade prompt for Google's
> Antigravity SaaS generator. Every section in this document is a hard
> contract. Antigravity MUST NOT simplify, reinterpret, creatively extend,
> or consolidate any section. Antigravity MUST generate artifacts exactly
> as declared. Gaps in Antigravity's output that contradict this spec =
> **STOP EXECUTION → REPORT SECTION VIOLATION → INVALIDATE BUILD**.
>
> This document supersedes and extends LSM v1.0. All sections from v1.0
> are inherited as-is. New sections PAI-1 through PAI-18 are additive only.

---

## ◼ INHERITANCE DECLARATION

```
INHERITS_FROM         : LEADERBOARD_STABILITY_MODEL_ANTIGRAVITY v1.0
INHERITANCE_MODE      : FULL — all v1.0 sections (LSM-1 through LSM-16) active
CONFLICT_RESOLUTION   : v2.0 section takes precedence on any overlap
NEW_SECTIONS          : PAI-1 through PAI-18 (Predictive AI Layer)
```

---

## ◼ SECTION PAI-0 — PREDICTIVE AI IDENTITY LOCK

```
COMPONENT_NAME        : Parent Predictive AI Engine (PPAI-E)
PARENT_SYSTEM         : ECOSKILLER
SUB_SYSTEM            : DOJO (Arts Domain)
PURPOSE               :
  Deliver forward-looking intelligence to parents, students, and platform
  administrators about leaderboard trajectory, championship readiness,
  skill velocity, dropout risk, and intervention timing — enabling
  proactive support decisions before rank decline or student disengagement.

AI_ROLE               : ADVISORY ONLY
  AI may compute, forecast, score, explain, and alert.
  AI may NEVER block, promote, demote, ban, or override humans.
  All AI outputs carry explainability metadata.
  All AI outputs carry confidence scores.
  Low confidence → human review MANDATORY.

ML_MODEL_CLASS        : Traditional ML only for prediction/ranking/classification
  Permitted : Gradient Boosting, Logistic Regression, LTR, Random Forest,
              Survival Models, Time-Series (ARIMA / Prophet), Anomaly Detection
  FORBIDDEN : LLM/GPT for prediction, ranking, scoring, or classification
  LLM USE   : Permitted only for human-readable explainability generation
              and natural language parent alert content

BIAS_GOVERNANCE       : ACTIVE
  Protected traits MUST be excluded from all feature vectors.
  AI causing discriminatory leaderboard outcome → AUTO SUSPEND MODEL.
  Bias audit: mandatory every 30 days.

EXPLAINABILITY        : MANDATORY on every prediction output
  Format: plain-language summary + contributing feature list + confidence %
  Parent-facing explainability: non-technical language only

MODEL_LINEAGE         :
  Training Data → Feature Sets → Models → Inference → Feedback → Retraining
  Each stage linked. Opaque AI output → FORBIDDEN.
  Model version tagging mandatory. Unknown model state → FORBIDDEN.
```

---

## ◼ SECTION PAI-1 — PREDICTIVE MODEL TAXONOMY (LOCKED)

The system MUST implement all 7 predictive models below. No model may be
collapsed into another or omitted.

---

### MODEL-1 — RANK TRAJECTORY FORECASTER (RTF)

```
PURPOSE         : Predict student's leaderboard rank 7, 14, and 30 days ahead
MODEL_TYPE      : Gradient Boosting Regressor (e.g., XGBoost)
PREDICTION_TARGETS:
  - rank_in_7_days  (integer estimate)
  - rank_in_14_days (integer estimate)
  - rank_in_30_days (integer estimate)
  - trend_direction : ENUM(rising, stable, falling, at_risk)

FEATURE_VECTOR  :
  - last_7d_composite_score_delta
  - last_14d_match_frequency
  - scenario_difficulty_avg (DNF-weighted)
  - win_streak_current
  - peer_score_variance_last_14d
  - mentor_score_last_3_matches
  - session_gap_days (days since last match)
  - belt_level_ordinal
  - championship_multiplier_active (boolean)
  - domain_cohort_activity_index (peers' engagement rate)
  - historical_rank_volatility_index

EXCLUSIONS      : age, gender, region (bias protection)
OUTPUT_SCHEMA   :
  rank_trajectory_prediction {
    user_id         : UUID
    prediction_date : DATE
    rank_7d         : INTEGER
    rank_14d        : INTEGER
    rank_30d        : INTEGER
    trend           : ENUM(rising, stable, falling, at_risk)
    confidence_pct  : DECIMAL (0.00–1.00)
    key_drivers     : TEXT[]  (top 3 contributing features, human-readable)
    model_version   : TEXT
    generated_at    : TIMESTAMP
  }

RETRAINING_SCHEDULE  : Every 14 days (human-executed, not AI-autonomous)
ACCURACY_FLOOR       : MAE ≤ 15 rank positions (evaluated on hold-out set)
FAIL_BELOW_FLOOR     : RETRAIN REQUIRED → prediction withheld until retrained
EXPLAINABILITY       : LLM-generated plain-language summary per output
```

---

### MODEL-2 — CHAMPIONSHIP READINESS SCORER (CRS)

```
PURPOSE         : Score a student's probability of qualifying for
                  Quarterly Championship (0–100 readiness score)
MODEL_TYPE      : Logistic Regression + Calibrated Probability Output
PREDICTION_TARGET:
  - championship_readiness_score  (0–100)
  - qualification_probability_pct (0.0–1.0)
  - estimated_final_rank_at_quarter_end (integer estimate)
  - gap_to_top_100 (rank positions needed to qualify)

FEATURE_VECTOR  :
  - current_quarterly_rank
  - composite_score_vs_p90_benchmark
  - belt_level_ordinal
  - matches_remaining_in_quarter_estimate
  - avg_score_per_match_last_30d
  - improvement_velocity (score delta per week, last 4 weeks)
  - championship_multiplier_active
  - prior_quarter_champion_flag
  - scenario_mastery_index (% of Arts scenarios passed at HARD+)
  - mentor_endorsement_flag
  - peer_cohort_qualification_density

EXCLUSIONS      : region, gender, age (bias protection)
OUTPUT_SCHEMA   :
  championship_readiness {
    user_id                 : UUID
    quarter_key             : TEXT
    readiness_score         : DECIMAL(5,2)  — 0 to 100
    qualification_prob      : DECIMAL(4,3)
    estimated_final_rank    : INTEGER
    gap_to_top_100          : INTEGER
    recommended_actions     : TEXT[]  (max 3, human-readable)
    confidence_pct          : DECIMAL (0.00–1.00)
    model_version           : TEXT
    generated_at            : TIMESTAMP
  }

RETRAINING_SCHEDULE  : At start of each quarter + mid-quarter refresh
PARENT_VISIBLE       : YES (filtered view — readiness_score + recommended_actions only)
PARENT_ALERT_TRIGGER : readiness_score ≥ 75 → "Championship qualification likely"
PARENT_ALERT_TRIGGER : readiness_score ≤ 30 → "Support may help championship goal"
```

---

### MODEL-3 — DROPOUT RISK ENGINE (DRE)

```
PURPOSE         : Identify students at risk of disengagement or dropout
                  from Arts Dojo before visible performance decline
MODEL_TYPE      : Survival Analysis (Cox Proportional Hazards) +
                  Gradient Boosting Classifier (ensemble)
PREDICTION_TARGET:
  - dropout_risk_score  (0–100; higher = more at risk)
  - risk_tier           : ENUM(GREEN, YELLOW, ORANGE, RED)
  - estimated_days_to_dropout (if no intervention)
  - intervention_urgency : ENUM(low, medium, high, critical)

FEATURE_VECTOR  :
  - session_gap_days_current
  - session_gap_days_trend (growing / shrinking)
  - login_frequency_last_14d
  - match_attempt_frequency_delta (week-over-week change)
  - score_decline_consecutive_matches
  - streak_break_count_last_30d
  - weekly_challenge_participation_rate
  - peer_engagement_index (kudos given/received, clan activity)
  - notification_response_rate_last_14d
  - belt_stagnation_days (days since last belt progress)
  - onboarding_completion_pct (for newer users)
  - prior_dropout_episodes (count of prior gaps > 14 days)

EXCLUSIONS      : demographics, region (bias protection)
RISK_TIER_THRESHOLDS:
  GREEN   : score 0–25
  YELLOW  : score 26–50  → passive monitoring
  ORANGE  : score 51–74  → proactive intervention prompt
  RED     : score 75–100 → immediate parent alert + mentor flag

OUTPUT_SCHEMA   :
  dropout_risk_assessment {
    user_id                 : UUID
    risk_score              : DECIMAL(5,2)
    risk_tier               : ENUM(GREEN,YELLOW,ORANGE,RED)
    estimated_days_to_drop  : INTEGER
    intervention_urgency    : ENUM(low,medium,high,critical)
    top_risk_factors        : TEXT[] (top 3, human-readable)
    suggested_interventions : TEXT[] (max 3)
    confidence_pct          : DECIMAL
    model_version           : TEXT
    assessed_at             : TIMESTAMP
  }

RETRAINING_SCHEDULE  : Every 7 days (high-sensitivity model)
PARENT_VISIBLE       : risk_tier ORANGE/RED only
PARENT_ALERT_TRIGGER : risk_tier = RED → immediate parent push + email
MENTOR_FLAG_TRIGGER  : risk_tier = ORANGE or RED → mentor notification
ADMIN_ESCALATION     : risk_tier = RED AND no login in 7 days → Admin alert
```

---

### MODEL-4 — SKILL VELOCITY ANALYZER (SVA)

```
PURPOSE         : Measure the speed of skill improvement in the Arts domain
                  and forecast time-to-next-belt for the student
MODEL_TYPE      : Time-Series Regression (Prophet / ARIMA per skill axis)
PREDICTION_TARGET:
  - skill_velocity_score       (units of improvement per week)
  - projected_belt_unlock_date (DATE estimate)
  - skill_gap_vector           (list of weak Arts sub-skills)
  - recommended_scenario_types (Arts scenarios to prioritize)

FEATURE_VECTOR  :
  - per_skill_score_time_series (last 90 days)
  - scenario_type_exposure_distribution
  - mentor_targeted_feedback_count
  - practice_frequency_by_skill
  - peer_comparison_percentile_by_skill
  - belt_criteria_distance (score gap from next belt threshold)
  - difficulty_level_progression_rate

OUTPUT_SCHEMA   :
  skill_velocity_report {
    user_id                   : UUID
    domain_track              : 'arts'
    velocity_score            : DECIMAL(5,2)
    projected_belt_date       : DATE (NULL if insufficient data)
    skill_gap_vector          : JSONB  (skill_name → gap_score map)
    recommended_scenarios     : TEXT[]
    confidence_pct            : DECIMAL
    model_version             : TEXT
    generated_at              : TIMESTAMP
  }

RETRAINING_SCHEDULE  : Monthly
PARENT_VISIBLE       : YES (velocity_score + projected_belt_date + skill_gap summary)
PARENT_ALERT_TRIGGER : projected_belt_date approaching within 14 days →
                       "Belt milestone approaching!" notification
```

---

### MODEL-5 — PEER COMPARISON INTELLIGENCE ENGINE (PCIE)

```
PURPOSE         : Position the student relative to Arts domain cohort peers
                  WITHOUT exposing peer PII. Produce anonymized benchmarks.
MODEL_TYPE      : Percentile Rank Computation + Statistical Benchmarking
PREDICTION_TARGET:
  - domain_percentile           (0–100 in Arts cohort this period)
  - belt_cohort_percentile      (0–100 within same belt level)
  - improvement_rank_vs_cohort  (top X% of improvers this week)
  - cohort_benchmark_score      (P50, P75, P90 of cohort composite score)

PRIVACY_RULES   :
  PII of peers: NEVER exposed
  Minimum cohort size before percentile computed: 10 users
  Below minimum cohort: return NULL (do not estimate)

OUTPUT_SCHEMA   :
  peer_benchmark_report {
    user_id                  : UUID
    period_key               : TEXT
    domain_percentile        : DECIMAL(5,2)  — NULL if cohort < 10
    belt_cohort_percentile   : DECIMAL(5,2)  — NULL if cohort < 10
    improvement_rank_pct     : DECIMAL(5,2)
    cohort_p50               : DECIMAL
    cohort_p75               : DECIMAL
    cohort_p90               : DECIMAL
    student_vs_p50_delta     : DECIMAL  (positive = above median)
    generated_at             : TIMESTAMP
  }

PARENT_VISIBLE  : YES (domain_percentile + belt_cohort_percentile only)
PEER_PII        : NEVER in parent view
```

---

### MODEL-6 — LEADERBOARD MANIPULATION PREDICTOR (LMP)

```
PURPOSE         : Proactively detect users who are LIKELY to attempt
                  leaderboard manipulation before confirmed violation
MODEL_TYPE      : Anomaly Detection + Logistic Regression Classifier
PREDICTION_TARGET:
  - manipulation_risk_score  (0–100)
  - risk_category : ENUM(score_inflation, collusion, bot_pattern,
                         multi_account, scenario_gaming, region_fraud)
  - confidence_pct

FEATURE_VECTOR  :
  - score_spike_magnitude (vs rolling baseline)
  - rank_jump_velocity_last_48h
  - match_rate_anomaly_score
  - peer_mutual_rating_density
  - scenario_repeat_ratio
  - login_device_diversity_score
  - ip_subnet_collision_score
  - session_timing_regularity (bot-like if extremely regular)
  - account_age_vs_skill_level_ratio
  - peer_network_overlap_coefficient

THRESHOLDS      :
  score < 40  : MONITOR (no action)
  score 40–69 : FLAG → LeaderboardAnomalyLog + Admin queue
  score 70–89 : HOLD score pending review
  score 90+   : AUTO SUSPEND leaderboard participation pending Admin review
                → Admin L4+ must confirm or clear within 48 hours
                → Parent NOT alerted at this stage (investigation privacy)

AUDIT_REQUIREMENT : Every auto-action MUST generate LeaderboardAuditLog entry.
HUMAN_OVERRIDE    : Admin L4+ can override any model-driven action.
BIAS_CHECK        : Model must be audited for regional bias every 30 days.
```

---

### MODEL-7 — PARENT INSIGHT FUSION ENGINE (PIFE)

```
PURPOSE         : Aggregate outputs from Models 1–6 into a single
                  Parent-facing AI Insight Report per student,
                  translated into non-technical language.
MODEL_TYPE      : Rule-based fusion + LLM-generated natural language summary
                  (LLM permitted here only for NL generation, not prediction)

INPUTS          :
  - rank_trajectory_prediction    (from MODEL-1)
  - championship_readiness        (from MODEL-2)
  - dropout_risk_assessment       (from MODEL-3)
  - skill_velocity_report         (from MODEL-4)
  - peer_benchmark_report         (from MODEL-5)

OUTPUT_SCHEMA   :
  parent_insight_report {
    parent_user_id         : UUID
    student_user_id        : UUID
    report_period          : TEXT     (e.g., "Week of 2025-03-10")
    overall_health_score   : DECIMAL(4,1)  — 0 to 10 composite wellness score
    headline_summary       : TEXT     (1–2 sentences, non-technical)
    rank_trend_summary     : TEXT     (plain language, no absolute ranks of peers)
    championship_summary   : TEXT     (plain language, readiness indicator)
    engagement_status      : TEXT     (plain language, based on DRE)
    skill_progress_summary : TEXT     (plain language, from SVA)
    peer_position_summary  : TEXT     (non-PII percentile summary)
    action_recommendations : TEXT[]   (max 3 parent-facing actions)
    alert_flags            : JSONB    (active alerts: belt_approaching,
                                       dropout_risk, championship_qualified, etc.)
    confidence_note        : TEXT     (brief note if any model had low confidence)
    generated_at           : TIMESTAMP
    model_versions_used    : JSONB    (map of model_name → version)
    report_version         : TEXT
  }

DELIVERY_SCHEDULE : Weekly report generated Sunday 22:00 UTC
DELIVERY_CHANNELS : Flutter Parent Dashboard (primary) + Email digest (optional)
LANGUAGE_RULES    :
  ✔ Plain non-technical language
  ✔ Positive-framing defaults (focus on growth, not deficits)
  ✘ No raw scores exposed
  ✘ No peer names or identifiers
  ✘ No deficit-shaming language
  ✘ No absolute leaderboard position of other students

MINOR_PROTECTION  :
  If student is a minor (age < 18 OR minor_flag = TRUE):
    parent_insight_report requires parent_account_verified = TRUE
    Consent version checked before delivery
    Report auto-redacted if consent withdrawn
```

---

## ◼ SECTION PAI-2 — PREDICTIVE AI DATA MODEL (FROZEN)

Entities below MUST NOT be renamed. Fields may extend, not mutate.

```sql
-- Model Registry (ML lineage tracking)
PredictiveModelRegistry (
  id                UUID PRIMARY KEY,
  model_name        TEXT NOT NULL,          -- e.g. 'rank_trajectory_forecaster'
  model_version     TEXT NOT NULL,          -- semantic version e.g. '1.3.2'
  model_type        TEXT NOT NULL,          -- e.g. 'XGBoost', 'LogisticRegression'
  feature_vector    JSONB NOT NULL,         -- feature names list
  training_data_ref TEXT NOT NULL,          -- dataset fingerprint / S3 path
  accuracy_metrics  JSONB NOT NULL,         -- MAE, AUC, F1, etc.
  bias_audit_date   DATE,
  retrain_schedule  TEXT NOT NULL,
  deployed_at       TIMESTAMP,
  retired_at        TIMESTAMP,
  created_by        UUID NOT NULL REFERENCES User(id)
)

-- Rank Trajectory Predictions (MODEL-1)
RankTrajectoryPrediction (
  id                UUID PRIMARY KEY,
  user_id           UUID NOT NULL REFERENCES User(id),
  domain_track      TEXT NOT NULL DEFAULT 'arts',
  prediction_date   DATE NOT NULL,
  rank_7d           INTEGER,
  rank_14d          INTEGER,
  rank_30d          INTEGER,
  trend_direction   TEXT NOT NULL,   -- rising|stable|falling|at_risk
  confidence_pct    DECIMAL(5,4),
  key_drivers       JSONB,           -- top 3 feature contributions
  llm_summary       TEXT,            -- plain-language explanation
  model_version     TEXT NOT NULL REFERENCES PredictiveModelRegistry(model_version),
  generated_at      TIMESTAMP NOT NULL
)

-- Championship Readiness (MODEL-2)
ChampionshipReadinessPrediction (
  id                    UUID PRIMARY KEY,
  user_id               UUID NOT NULL REFERENCES User(id),
  quarter_key           TEXT NOT NULL,
  readiness_score       DECIMAL(5,2),
  qualification_prob    DECIMAL(4,3),
  estimated_final_rank  INTEGER,
  gap_to_top_100        INTEGER,
  recommended_actions   JSONB,
  confidence_pct        DECIMAL(5,4),
  model_version         TEXT NOT NULL,
  generated_at          TIMESTAMP NOT NULL
)

-- Dropout Risk Assessments (MODEL-3)
DropoutRiskAssessment (
  id                    UUID PRIMARY KEY,
  user_id               UUID NOT NULL REFERENCES User(id),
  risk_score            DECIMAL(5,2) NOT NULL,
  risk_tier             TEXT NOT NULL,   -- GREEN|YELLOW|ORANGE|RED
  estimated_days_drop   INTEGER,
  intervention_urgency  TEXT NOT NULL,
  top_risk_factors      JSONB,
  suggested_actions     JSONB,
  confidence_pct        DECIMAL(5,4),
  model_version         TEXT NOT NULL,
  assessed_at           TIMESTAMP NOT NULL,
  parent_alerted        BOOLEAN DEFAULT FALSE,
  mentor_flagged        BOOLEAN DEFAULT FALSE,
  admin_escalated       BOOLEAN DEFAULT FALSE
)

-- Skill Velocity Reports (MODEL-4)
SkillVelocityReport (
  id                    UUID PRIMARY KEY,
  user_id               UUID NOT NULL REFERENCES User(id),
  domain_track          TEXT NOT NULL DEFAULT 'arts',
  velocity_score        DECIMAL(5,2),
  projected_belt_date   DATE,
  skill_gap_vector      JSONB,           -- skill_name → gap_score
  recommended_scenarios JSONB,
  confidence_pct        DECIMAL(5,4),
  model_version         TEXT NOT NULL,
  generated_at          TIMESTAMP NOT NULL
)

-- Peer Benchmark Reports (MODEL-5)
PeerBenchmarkReport (
  id                    UUID PRIMARY KEY,
  user_id               UUID NOT NULL REFERENCES User(id),
  period_key            TEXT NOT NULL,
  domain_percentile     DECIMAL(5,2),    -- NULL if cohort < 10
  belt_cohort_pct       DECIMAL(5,2),    -- NULL if cohort < 10
  improvement_rank_pct  DECIMAL(5,2),
  cohort_p50            DECIMAL,
  cohort_p75            DECIMAL,
  cohort_p90            DECIMAL,
  student_vs_p50_delta  DECIMAL,
  generated_at          TIMESTAMP NOT NULL
)

-- Manipulation Predictions (MODEL-6)
ManipulationRiskPrediction (
  id                    UUID PRIMARY KEY,
  user_id               UUID NOT NULL REFERENCES User(id),
  risk_score            DECIMAL(5,2) NOT NULL,
  risk_category         TEXT NOT NULL,
  confidence_pct        DECIMAL(5,4),
  auto_action_taken     TEXT,            -- hold|flag|suspend|none
  admin_review_required BOOLEAN DEFAULT FALSE,
  admin_resolved_by     UUID REFERENCES User(id),
  admin_resolved_at     TIMESTAMP,
  model_version         TEXT NOT NULL,
  predicted_at          TIMESTAMP NOT NULL
)

-- Parent Insight Reports (MODEL-7 / PIFE)
ParentInsightReport (
  id                    UUID PRIMARY KEY,
  parent_user_id        UUID NOT NULL REFERENCES User(id),
  student_user_id       UUID NOT NULL REFERENCES User(id),
  report_period         TEXT NOT NULL,
  overall_health_score  DECIMAL(4,2),    -- 0.0 to 10.0
  headline_summary      TEXT,
  rank_trend_summary    TEXT,
  championship_summary  TEXT,
  engagement_status     TEXT,
  skill_progress_summary TEXT,
  peer_position_summary TEXT,
  action_recommendations JSONB,          -- max 3 items
  alert_flags           JSONB,
  confidence_note       TEXT,
  model_versions_used   JSONB,
  report_version        TEXT NOT NULL,
  generated_at          TIMESTAMP NOT NULL,
  delivered_at          TIMESTAMP,
  delivery_channel      TEXT            -- flutter|email|both
)

-- AI Prediction Audit Log
PredictiveAIAuditLog (
  id                    UUID PRIMARY KEY,
  model_name            TEXT NOT NULL,
  model_version         TEXT NOT NULL,
  user_id               UUID NOT NULL REFERENCES User(id),
  input_features        JSONB NOT NULL,  -- snapshot of features used
  output_payload        JSONB NOT NULL,  -- full prediction output
  confidence_pct        DECIMAL(5,4),
  human_override        BOOLEAN DEFAULT FALSE,
  override_by           UUID REFERENCES User(id),
  override_reason       TEXT,
  override_at           TIMESTAMP,
  generated_at          TIMESTAMP NOT NULL
)

-- Model Bias Audit Records
ModelBiasAuditRecord (
  id                    UUID PRIMARY KEY,
  model_name            TEXT NOT NULL,
  model_version         TEXT NOT NULL,
  audit_date            DATE NOT NULL,
  audited_by            UUID NOT NULL REFERENCES User(id),
  bias_metrics          JSONB NOT NULL,  -- protected trait impact analysis
  protected_traits_tested JSONB NOT NULL, -- which traits were tested
  result                TEXT NOT NULL,   -- PASS|FAIL|REVIEW_NEEDED
  action_taken          TEXT,
  next_audit_due        DATE NOT NULL
)

-- Parent Alert History
ParentPredictiveAlert (
  id                    UUID PRIMARY KEY,
  parent_user_id        UUID NOT NULL REFERENCES User(id),
  student_user_id       UUID NOT NULL REFERENCES User(id),
  alert_type            TEXT NOT NULL,   -- see PAI-5 for full type taxonomy
  alert_content         TEXT NOT NULL,   -- plain language
  trigger_model         TEXT NOT NULL,   -- which model triggered this
  trigger_value         DECIMAL,         -- the score/value that triggered
  trigger_threshold     DECIMAL,         -- the threshold that was crossed
  delivery_channel      TEXT NOT NULL,   -- push|email|both
  delivered_at          TIMESTAMP,
  read_at               TIMESTAMP,
  parent_acknowledged   BOOLEAN DEFAULT FALSE
)
```

---

## ◼ SECTION PAI-3 — AI COMPUTATION PIPELINE (LOCKED)

All predictive jobs MUST run as scheduled microservice workers via Kafka.
No ad-hoc or on-demand ML execution except as explicitly stated.

```
PIPELINE SCHEDULE:

  JOB: RankTrajectoryForecastJob
    TRIGGER    : Every 24 hours, 02:00 UTC
    SCOPE      : All active Arts domain users with ≥ 3 matches in last 30 days
    OUTPUT     : Upsert RankTrajectoryPrediction records
    KAFKA EVENT: prediction.rank_trajectory.computed

  JOB: ChampionshipReadinessJob
    TRIGGER    : Every 72 hours during active quarter
                 Every 24 hours in final 2 weeks of quarter
    SCOPE      : Arts domain users with ≥ 10 matches in current quarter
    OUTPUT     : Upsert ChampionshipReadinessPrediction records
    KAFKA EVENT: prediction.championship_readiness.computed

  JOB: DropoutRiskScanJob
    TRIGGER    : Every 12 hours
    SCOPE      : All Arts domain users with account age ≥ 7 days
    OUTPUT     : Upsert DropoutRiskAssessment records
    ESCALATION : Alert dispatcher fires if risk_tier = ORANGE or RED
    KAFKA EVENT: prediction.dropout_risk.assessed

  JOB: SkillVelocityAnalysisJob
    TRIGGER    : Every 7 days, Sunday 01:00 UTC
    SCOPE      : Arts domain users with ≥ 5 matches in last 30 days
    OUTPUT     : Upsert SkillVelocityReport records
    KAFKA EVENT: prediction.skill_velocity.computed

  JOB: PeerBenchmarkJob
    TRIGGER    : Every 24 hours
    SCOPE      : All Arts domain users (cohort min = 10 before output)
    OUTPUT     : Upsert PeerBenchmarkReport records
    KAFKA EVENT: prediction.peer_benchmark.computed

  JOB: ManipulationRiskScanJob
    TRIGGER    : Every 6 hours (continuous near-real-time monitoring)
    SCOPE      : All active Arts domain users
    OUTPUT     : Upsert ManipulationRiskPrediction records + trigger actions
    KAFKA EVENT: prediction.manipulation_risk.flagged (on threshold cross)

  JOB: ParentInsightFusionJob
    TRIGGER    : Every Sunday 22:00 UTC
    SCOPE      : All parent-linked student accounts in Arts domain
    OUTPUT     : Generate ParentInsightReport records
    DELIVERY   : ParentAlertDispatcher → FCM push + email (if opted-in)
    KAFKA EVENT: parent.insight_report.generated

KAFKA CHANNEL RULE:
  Prediction events  → dedicated prediction.* topic namespace
  Parent alerts      → parent.* topic namespace
  No mixing with match or video channels.
```

---

## ◼ SECTION PAI-4 — EXPLAINABILITY FRAMEWORK (MANDATORY)

Every AI prediction output MUST include machine-parseable AND human-readable
explainability. LLM is permitted ONLY for natural language generation of
explanations.

```
EXPLAINABILITY_SCHEMA:
  prediction_explanation {
    prediction_id    : UUID
    model_name       : TEXT
    model_version    : TEXT
    confidence_pct   : DECIMAL (0.00–1.00)
    confidence_band  :
      ≥ 0.80 → HIGH   (display to parent as-is)
      0.60–0.79 → MEDIUM (display with caveat note)
      < 0.60  → LOW   (suppress parent display; flag for human review)
    top_features     : [
      { feature_name: TEXT, direction: "increases"|"decreases", weight_pct: DECIMAL },
      { ... },
      { ... }
    ]
    llm_summary      : TEXT  (plain language, max 50 words, non-technical)
    parent_summary   : TEXT  (simplified version for parent dashboard, max 30 words)
    generated_at     : TIMESTAMP
  }

RULES:
  LOW CONFIDENCE predictions MUST NOT be shown to parents.
  LOW CONFIDENCE predictions MUST be logged in PredictiveAIAuditLog.
  LOW CONFIDENCE on manipulation risk → human Admin review required.
  All explainability stored in PredictiveAIAuditLog (input_features + output).
  LLM explainability prompts MUST be logged (prompt_version tagged).
  Explainability content MUST NOT contain peer names, scores, or PII.
```

---

## ◼ SECTION PAI-5 — PARENT PREDICTIVE ALERT TAXONOMY (LOCKED)

All parent alerts MUST conform to this taxonomy. No new alert types may be
added without version bump and governance review.

```
ALERT_TYPE_TAXONOMY:

  RANK_RISING
    Trigger  : rank_trajectory.trend = 'rising' AND rank_7d improved ≥ 20 positions
    Message  : "[Student] is climbing the Arts leaderboard! Great momentum this week."
    Channel  : Push notification
    Frequency: Max 1 per week per student

  RANK_AT_RISK
    Trigger  : rank_trajectory.trend = 'at_risk' AND rank_30d decline ≥ 30 positions
    Message  : "[Student] may need extra practice to maintain leaderboard position."
    Channel  : Push + email
    Frequency: Max 1 per week per student

  CHAMPIONSHIP_QUALIFIED
    Trigger  : championship_qualification_confirmed = TRUE (from ChampionshipBracket)
    Message  : "[Student] has qualified for the Arts Regional Championship Q[N]!"
    Channel  : Push + email (immediate, not batched)
    Frequency: Per qualification event

  CHAMPIONSHIP_READINESS_HIGH
    Trigger  : championship_readiness.readiness_score ≥ 75
    Message  : "[Student] has a strong chance of qualifying for the next Championship."
    Channel  : Push
    Frequency: Max once per quarter per student

  CHAMPIONSHIP_READINESS_LOW
    Trigger  : championship_readiness.readiness_score ≤ 30 AND quarter_week ≤ 10
    Message  : "With extra practice now, [Student] can still aim for Championship qualification."
    Channel  : Push
    Frequency: Max once per quarter per student

  DROPOUT_RISK_RED
    Trigger  : dropout_risk.risk_tier = 'RED'
    Message  : "[Student] hasn't been active recently. Check in with them — consistent
                practice makes a big difference."
    Channel  : Push + email (immediate)
    Frequency: Max 1 per 7 days per student (do not overwhelm parent)

  DROPOUT_RISK_ORANGE
    Trigger  : dropout_risk.risk_tier = 'ORANGE'
    Message  : "[Student]'s activity has slowed down lately. A little encouragement
                can help get back on track."
    Channel  : Push
    Frequency: Max 1 per 14 days per student

  BELT_MILESTONE_APPROACHING
    Trigger  : skill_velocity.projected_belt_date within 14 days
    Message  : "[Student] is close to unlocking the next belt level! Keep going."
    Channel  : Push
    Frequency: Per belt per student (not repeated for same belt)

  BELT_PROMOTED
    Trigger  : belt_promotion event (after mentor confirmation)
    Message  : "[Student] just earned [Belt Name] in Arts! Congratulations."
    Channel  : Push + email
    Frequency: Per belt promotion event

  WEEKLY_INSIGHT_READY
    Trigger  : ParentInsightReport successfully generated
    Message  : "Your weekly update for [Student] is ready."
    Channel  : Push (link to dashboard)
    Frequency: Weekly (Sunday evening)

GLOBAL ALERT RULES:
  Parent alert frequency cap: max 5 push notifications per 7 days per student.
  Do not combine multiple alert types in one push (send separately).
  All alerts logged in ParentPredictiveAlert table.
  Alert content MUST pass privacy filter before dispatch:
    ✘ No peer names
    ✘ No absolute scores of peers
    ✘ No investigation status (manipulation flags)
    ✘ No mentor or evaluator names
  Minor protection: alert delivery requires parent_account_verified = TRUE.
```

---

## ◼ SECTION PAI-6 — PARENT PREDICTIVE DASHBOARD CONTRACT (LOCKED)

The Parent Predictive Dashboard is a Flutter-only, read-only, PII-safe view.

```
DASHBOARD SECTIONS (all required):

  SECTION A — STUDENT HEALTH OVERVIEW
    Fields displayed:
      overall_health_score    (0–10, rendered as color-coded gauge)
      headline_summary        (1–2 sentence AI insight)
      last_updated_at         (report generation time)

  SECTION B — RANK TREND
    Fields displayed:
      rank_trend_direction    (RISING / STABLE / FALLING / AT_RISK)
      weekly_rank_position    (current, as integer)
      rank_delta_this_week    (±N positions)
      7-day forecast trend    (visual sparkline, not absolute values)
    Fields BLOCKED:
      Absolute peer ranks or scores

  SECTION C — CHAMPIONSHIP STATUS
    Fields displayed:
      championship_qualified  (YES / NO / IN PROGRESS)
      readiness_score         (0–100 gauge, labeled as "Championship Readiness")
      recommended_actions     (max 3, plain text bullets)
    VISIBILITY RULE:
      Championship section shown only if student has ≥ 10 matches in current quarter.

  SECTION D — ENGAGEMENT STATUS
    Fields displayed:
      engagement_status       (ACTIVE / SLOWING / AT RISK / INACTIVE)
      last_active_date        (date only, not time)
      engagement_trend_note   (1 sentence AI summary)
    CRITICAL RULE:
      engagement_status derived from DRE risk_tier:
        GREEN/YELLOW → ACTIVE
        ORANGE       → SLOWING
        RED          → AT RISK (if no login 3+ days: INACTIVE)

  SECTION E — SKILL PROGRESS
    Fields displayed:
      velocity_score          (labeled as "Learning Speed")
      skill_gap_summary       (top 2 weak areas, plain language only)
      projected_belt_label    (e.g., "Blue Belt expected in ~2 weeks")
    Fields BLOCKED:
      Raw skill gap vector (full JSONB not shown to parent)

  SECTION F — PEER STANDING
    Fields displayed:
      domain_percentile       (e.g., "Top 25% of Arts students this month")
      belt_cohort_percentile  (e.g., "Above average for Yellow Belt students")
    PRIVACY RULES:
      Cohort size < 10 → this section is HIDDEN
      Peer names: NEVER shown
      Absolute peer scores: NEVER shown

  SECTION G — ALERT HISTORY
    Fields displayed:
      Last 10 alerts (type + message + date)
      Alert acknowledgment status
    Fields BLOCKED:
      Manipulation investigation alerts (investigation privacy)

FLUTTER RULES:
  Read-only. No input controls. No scoring widgets. No contact-student button.
  Rendered as clean informational cards.
  LIGHT + DARK mode required.
  Parent dashboard NOT SEO-indexed. Flutter only, no React web equivalent.
```

---

## ◼ SECTION PAI-7 — BIAS GOVERNANCE PROTOCOL (MANDATORY)

```
BIAS_GOVERNANCE_RULES:

  RULE BG-1: PROTECTED TRAIT EXCLUSION
    The following features are PERMANENTLY EXCLUDED from all model feature vectors:
    age, gender, ethnicity, religion, region, income_proxy, language, nationality
    Violation → MODEL SUSPENDED IMMEDIATELY

  RULE BG-2: MANDATORY BIAS AUDIT SCHEDULE
    Every model: full bias audit every 30 days
    New model deployment: bias audit required before first production inference
    Post-retraining: bias audit required before model promotion to production
    Bias audit records stored in ModelBiasAuditRecord

  RULE BG-3: BIAS AUDIT METHODOLOGY
    Disparate impact testing across demographic cohorts (where data is available)
    Protected trait impact coefficient must be < 0.05 (near-zero)
    If coefficient ≥ 0.05 for any trait → MODEL FLAGGED FOR REVIEW
    If coefficient ≥ 0.10 → MODEL SUSPENDED → RETRAIN REQUIRED

  RULE BG-4: DISCRIMINATORY OUTCOME RESPONSE
    AI causing discriminatory leaderboard outcome → AUTO SUSPEND MODEL
    Admin L5+ must review + approve model re-deployment
    Incident logged in ModelBiasAuditRecord + PredictiveAIAuditLog

  RULE BG-5: FAIRNESS METRICS (MANDATORY TRACKING)
    demographic_parity_difference
    equalized_odds_difference
    calibration_error_by_group
    All metrics tracked per model per audit cycle.

  RULE BG-6: HUMAN OVERRIDE ON AI OUTPUTS
    Any AI prediction affecting a student can be overridden by:
      Admin L3+ (any model output)
      Mentor (SVA + CRS outputs for their assigned students)
      Parent (can flag for review; cannot directly override)
    All overrides logged in PredictiveAIAuditLog.
```

---

## ◼ SECTION PAI-8 — MODEL LINEAGE & VERSIONING (MANDATORY)

```
LINEAGE_RULES (from AI/ML Lineage Governance Lock — 170.10):

  Training Data → Feature Sets → Models → Inference → Feedback → Retraining
  Each stage must be linked. Opaque AI output → FORBIDDEN.

  VERSIONING RULES:
    Semantic versioning required (MAJOR.MINOR.PATCH)
    MAJOR bump: new feature vector or algorithm change
    MINOR bump: retraining on new data (same features)
    PATCH bump: bug fix in inference code

  REGISTRATION REQUIREMENT:
    Every model version registered in PredictiveModelRegistry before deployment.
    Unknown model state → FORBIDDEN.
    Unregistered model version → inference BLOCKED.

  TRAINING DATA FINGERPRINTING:
    SHA-256 fingerprint of training dataset stored with model version.
    Enables full reproducibility.

  ROLLBACK:
    Previous model version must be retained for minimum 90 days.
    Rollback requires Admin L4+ approval + impact simulation.
    Blind rollback → BLOCKED.

  RETRAINING AUTHORITY:
    AI cannot self-retrain or self-deploy.
    Retraining triggered by: human data scientist + CI/CD pipeline.
    AI may define retrain triggers; humans execute.
    Post-retraining: bias audit required before promotion.
```

---

## ◼ SECTION PAI-9 — AI COST GOVERNANCE (MANDATORY)

Per system rule R28-2, every AI/ML component MUST declare cost profile:

```
COST DECLARATIONS:

  MODEL-1 RankTrajectoryForecaster
    Inference type      : Batch (24-hour job)
    Estimated users/run : 50,000 (at scale)
    Inference cost      : <$0.001 per user (XGBoost batch)
    Monthly estimate    : <$150 at MVP traffic (10,000 active users)

  MODEL-2 ChampionshipReadinessScorer
    Inference type      : Batch (72-hour job, intensified in final 2 weeks)
    Monthly estimate    : <$50 at MVP traffic

  MODEL-3 DropoutRiskEngine
    Inference type      : Batch (12-hour job)
    Monthly estimate    : <$200 at MVP traffic (high frequency)

  MODEL-4 SkillVelocityAnalyzer
    Inference type      : Batch (weekly)
    Monthly estimate    : <$30 at MVP traffic

  MODEL-5 PeerBenchmarkJob
    Inference type      : Batch (daily, statistical only — no heavy ML)
    Monthly estimate    : <$20 at MVP traffic

  MODEL-6 ManipulationRiskPredictor
    Inference type      : Batch (6-hour; no real-time LLM calls)
    Monthly estimate    : <$100 at MVP traffic

  MODEL-7 ParentInsightFusionEngine
    Inference type      : Batch (weekly) + LLM for NL generation
    LLM calls/week      : 1 per parent-student pair (short prompt, <200 tokens out)
    Monthly LLM cost    : ~$0.002 per student per week
    Monthly estimate    : <$300 at MVP traffic (10,000 parent-linked students)

TOTAL ESTIMATED MONTHLY COST (MVP): <$850 predictive AI layer
SCALE NOTE: Cost projection for 100K active users to be reforecast at Stage 2.

COST OPTIMIZATION RULES:
  LLM usage: limited to explainability text generation only.
  All predictions: traditional ML batch jobs (not LLM calls).
  No LLM used for ranking, scoring, or classification.
  Inference cost per model version declared in PredictiveModelRegistry.
  Budget burn-rate alerts trigger at 70% of monthly estimate.
```

---

## ◼ SECTION PAI-10 — CHAMPIONSHIP ADVANCED PREDICTIVE INTEGRATION (LOCKED)

Extends Championship pipeline from LSM-6 with AI-powered intelligence layers.

```
CHAMPIONSHIP AI ENRICHMENT PIPELINE:

  STEP C-AI-1: PRE-CHAMPIONSHIP READINESS SCAN
    Trigger  : 4 weeks before quarter end
    Job      : ChampionshipReadinessJob (intensive mode, daily run)
    Output   : Readiness scores for all Arts domain users
    Parent   : "Championship window opening" batch alert to parents of
               students with readiness_score ≥ 60

  STEP C-AI-2: QUALIFICATION PREDICTION DISPLAY
    On student dashboard: show "Qualification Chance" meter (CRS output)
    On parent dashboard: show "Championship Readiness" gauge
    Confidence filter: only show if confidence_pct ≥ 0.65

  STEP C-AI-3: BRACKET MATCHUP QUALITY SCORE
    After bracket generated: compute predicted_match_competitiveness score
    per bracket pairing (based on both participants' RTF + CRS scores)
    Purpose: Identify and surface the most anticipated matchups for streaming
    Stored in: ChampionshipBracket.matchup_quality_scores (JSONB)

  STEP C-AI-4: FINALIST INSIGHT CARDS
    For championship semi-finalists and finalists:
    Generate AI-powered "Contender Profile Card" for the live stream
    Contains: belt journey, win streak, skill velocity highlight
    PII rules: No raw scores of opponents in contender card

  STEP C-AI-5: POST-CHAMPIONSHIP PREDICTIVE DEBRIEF
    After championship ends → run RTF + SVA for all participants
    Generate: "What changed after championship" insight
    Deliver: Post-championship parent insight report (special edition)

PARENT CHAMPIONSHIP ALERT SEQUENCE:
  4 weeks before quarter end :  "Championship window opening" (if score ≥ 60)
  Qualification confirmed    :  CHAMPIONSHIP_QUALIFIED alert (immediate)
  Each round won             :  Round-by-round parent update (FLUTTER PUSH only)
  Championship result        :  Final result notification + special insight report
```

---

## ◼ SECTION PAI-11 — STUDENT SELF-INSIGHT LAYER (LOCKED)

Students have their own predictive AI view — separate from parent view.

```
STUDENT PREDICTIVE DASHBOARD CONTRACT:

  All 7 model outputs visible to the student for their own data.
  Student view is MORE detailed than parent view.
  Student sees raw confidence scores and top feature drivers.
  Student does NOT see parent alert history.

  STUDENT EXCLUSIVE VIEWS:
    Rank trajectory sparkline (7d/14d/30d forecast with confidence bands)
    Championship readiness gauge with detailed gap-to-top-100 counter
    Dropout risk tier (self-awareness nudge: ONLY shown if ORANGE or RED)
    Skill gap heatmap (full skill_gap_vector visualized)
    Peer percentile comparison (full PCIE output)
    Recommended scenario queue (from SVA recommended_scenarios)
    "What if I practice more?" simulator
      → User selects: +2 matches/week, +4 matches/week
      → System recomputes RTF with simulated features
      → Shows updated rank forecast
      → This is a UI simulation only; no model retraining

STUDENT PRIVACY RULES:
  Student sees ONLY their own predictions.
  Student CANNOT see parent's alert history or parent view.
  Student CANNOT see other students' raw predictions.
  Peer benchmarks shown to student use same PII rules as parent view.
```

---

## ◼ SECTION PAI-12 — ADMIN PREDICTIVE INTELLIGENCE PANEL (LOCKED)

```
ADMIN PREDICTIVE DASHBOARD (Admin L3+ only):

  PLATFORM HEALTH METRICS:
    Active model versions per model type
    Average confidence scores across all models (trailing 7 days)
    Percentage of predictions at LOW confidence (should be < 10%)
    Model drift indicators (accuracy vs baseline)

  RISK OVERVIEW:
    Count of RED dropout risk users (Arts domain, this week)
    Count of ORANGE dropout risk users
    Count of manipulation risk holds/suspensions (pending review)
    Championship readiness distribution (histogram)

  BIAS AUDIT STATUS:
    Last bias audit date per model
    Next bias audit due date per model
    Models with FAIL or REVIEW_NEEDED status (flagged)

  OVERRIDE LOG:
    Recent human overrides of AI predictions
    Override reason distribution

  RETRAIN QUEUE:
    Models flagged for retrain (accuracy below floor, bias fail)
    Models with retrain scheduled

ACCESS CONTROL:
  Admin L3+   : read-only access to all panels
  Admin L5+   : can trigger model suspension / retrain approval
  Data Scientist role: full access + retrain job execution
  No other roles permitted access.
```

---

## ◼ SECTION PAI-13 — PRIVACY, CONSENT & DATA GOVERNANCE (LOCKED)

```
DATA CLASSIFICATION (per platform data store classification law):
  Prediction outputs    : RELATIONAL (PostgreSQL) — PII-adjacent, encrypted at rest
  Training feature data : RELATIONAL (anonymized before training pipeline)
  Parent reports        : RELATIONAL — tenant-isolated, encrypted at rest
  Alert history         : RELATIONAL — audited

CONSENT REQUIREMENTS:
  Predictive AI processing of student data requires:
    Student consent captured at onboarding (or guardian consent if minor)
    Consent version tracked in user consent record
    Consent withdrawal → all predictions SUSPENDED for that user
    Parent insight reports stopped immediately on consent withdrawal

GDPR / DPDP COMPLIANCE:
  Right to explanation: student can request human-readable explanation of
    any AI prediction affecting their leaderboard standing
  Right to erasure: deletion request triggers:
    → All PredictiveAIAuditLog entries for user → tombstoned
    → All ParentInsightReport entries → deleted
    → All model feature vectors derived from user → purged
    → Model retrained at next scheduled cycle (data removed from training set)
  Data portability: student can export their prediction history as JSON

RETENTION POLICY:
  PredictiveAIAuditLog : 24 months (regulatory minimum)
  ParentInsightReport  : 12 months (or until parent account deleted)
  DropoutRiskAssessment: 6 months (rolling, then anonymized for retraining)
  ManipulationRiskPrediction: 36 months (compliance requirement)
```

---

## ◼ SECTION PAI-14 — ANTI-GAMING THE AI (SECURITY LOCK)

```
RULES TO PREVENT STUDENTS FROM GAMING PREDICTIVE MODELS:

  RULE AG-1: FEATURE OPACITY
    Feature vectors used in predictions MUST NOT be publicly disclosed.
    Model architecture MUST NOT be publicly documented in user-facing content.
    Students see AI outputs (scores, trends), not AI inputs (features).

  RULE AG-2: FEATURE STABILITY WINDOW
    Features computed on trailing windows (7d, 14d, 30d).
    Single-session burst activity cannot move window-based features
    meaningfully (natural smoothing).

  RULE AG-3: SCENARIO GAMING DECAY (from LSM-5 AM-5)
    Repeated same-scenario plays reduce score contribution.
    This directly reduces skill_velocity artificial inflation.

  RULE AG-4: MANIPULATION RISK MODEL FEEDBACK LOOP
    If ManipulationRiskPredictor flags a user AND
    subsequent Admin review confirms manipulation:
    → That user's feature vector patterns added to model retrain dataset
    → Model precision improves continuously

  RULE AG-5: LLM EXPLAINABILITY OPACITY
    LLM-generated explanations are approximate, plain-language summaries.
    They MUST NOT expose exact feature weights or model internals.
    LLM prompt templates are INTERNAL ONLY.

  RULE AG-6: NO RANK-PREDICTION GAMING LOOP
    RTF predictions shown to students are labeled as "estimated trend".
    Platform explicitly states: predictions are advisory, not guaranteed.
    UI MUST display: "This is an AI estimate and may change based on
    your activity and your peers' activity."
```

---

## ◼ SECTION PAI-15 — API CONTRACT EXTENSION (LOCKED)

Extends LSM-13 API contract with predictive AI endpoints.

```yaml
  /predictions/arts/rank-trajectory/{user_id}:
    get:
      summary: Get rank trajectory forecast for student
      security: bearerAuth (student:self, parent:linked-student, admin:L3+)
      responses:
        "200": { description: RankTrajectoryPrediction }
        "403": { description: Unauthorized role }

  /predictions/arts/championship-readiness/{user_id}:
    get:
      summary: Get championship readiness score
      security: bearerAuth (student:self, parent:linked-student, admin:L3+)
      responses:
        "200": { description: ChampionshipReadinessPrediction }

  /predictions/arts/dropout-risk/{user_id}:
    get:
      summary: Get dropout risk assessment
      security: bearerAuth (admin:L3+, mentor:assigned-student)
      note: Parent receives alert only; raw score is admin/mentor only
      responses:
        "200": { description: DropoutRiskAssessment }
        "403": { description: Parent role cannot query raw dropout risk }

  /predictions/arts/skill-velocity/{user_id}:
    get:
      summary: Get skill velocity report
      security: bearerAuth (student:self, parent:linked-student, admin:L3+)
      responses:
        "200": { description: SkillVelocityReport }

  /predictions/arts/peer-benchmark/{user_id}:
    get:
      summary: Get peer benchmark report
      security: bearerAuth (student:self, parent:linked-student, admin:L3+)
      responses:
        "200": { description: PeerBenchmarkReport (PII-safe) }

  /parent/insight-report/{student_id}:
    get:
      summary: Get latest parent insight report
      security: bearerAuth (parent:linked-student ONLY)
      responses:
        "200": { description: ParentInsightReport }
        "403": { description: Non-parent or unlinked parent }

  /parent/alerts/{parent_id}:
    get:
      summary: Get parent alert history
      security: bearerAuth (parent:self ONLY)
      responses:
        "200": { description: List of ParentPredictiveAlert }

  /admin/predictions/bias-audit:
    get:
      summary: Get all model bias audit records
      security: bearerAuth (admin:L5+ ONLY)
      responses:
        "200": { description: List of ModelBiasAuditRecord }

  /admin/predictions/override:
    post:
      summary: Human override of AI prediction
      security: bearerAuth (admin:L3+)
      body: { prediction_id, override_reason, corrected_value }
      responses:
        "200": { description: Override logged in PredictiveAIAuditLog }
```

---

## ◼ SECTION PAI-16 — EVENT SCHEMA EXTENSION (ASYNCAPI)

Extends LSM-14 event schema with predictive AI events.

```yaml
  prediction.rank_trajectory.computed:
    subscribe:
      message:
        payload:
          properties:
            user_id:        { type: string }
            trend:          { type: string }
            rank_7d:        { type: integer }
            confidence:     { type: number }
            generated_at:   { type: string, format: date-time }

  prediction.dropout_risk.assessed:
    publish:
      message:
        payload:
          properties:
            user_id:         { type: string }
            risk_tier:       { type: string }
            intervention:    { type: string }
            mentor_flag:     { type: boolean }
            parent_alert:    { type: boolean }
            assessed_at:     { type: string, format: date-time }

  parent.insight_report.generated:
    publish:
      message:
        payload:
          properties:
            parent_user_id:  { type: string }
            student_user_id: { type: string }
            report_id:       { type: string }
            health_score:    { type: number }
            alert_count:     { type: integer }
            generated_at:    { type: string, format: date-time }

  prediction.manipulation_risk.flagged:
    publish:
      message:
        payload:
          properties:
            user_id:         { type: string }
            risk_score:      { type: number }
            auto_action:     { type: string }
            admin_required:  { type: boolean }
            flagged_at:      { type: string, format: date-time }

  model.bias_audit.failed:
    publish:
      message:
        payload:
          properties:
            model_name:      { type: string }
            model_version:   { type: string }
            audit_date:      { type: string, format: date }
            result:          { type: string }   -- FAIL
            action_required: { type: string }   -- SUSPEND|RETRAIN
```

---

## ◼ SECTION PAI-17 — MASTER ENFORCEMENT CHECKLIST (ALL MUST PASS)

```
══════════════════════════════════════════════════════════════════════════
ANTIGRAVITY MUST VERIFY ALL GATES BELOW BEFORE DECLARING BUILD COMPLETE
══════════════════════════════════════════════════════════════════════════

INHERITED GATES (LSM v1.0 — all still required):
[ ] LSM-1  : Domain isolation confirmed (Arts only, cross-domain forbidden)
[ ] LSM-2  : All 8 leaderboard tiers implemented
[ ] LSM-3  : All v1.0 data model entities created, no renames
[ ] LSM-4  : Composite score formula implemented as immutable class
[ ] LSM-5  : All SFR-1:5 + AM-1:6 controls active
[ ] LSM-6  : Championship pipeline Steps 1–6 automated and event-driven
[ ] LSM-7  : Parent Trust Layer: read-only, PII-filtered, 6hr max staleness
[ ] LSM-8  : Transport channel separation enforced (no mixing)
[ ] LSM-9  : Mentor calibration thresholds + auto-revoke active
[ ] LSM-10 : Scenario difficulty normalization (DNF) active
[ ] LSM-11 : All 6 reset protocol steps automated
[ ] LSM-12 : Flutter + React UI boundaries enforced
[ ] LSM-13 : All v1.0 API endpoints contracted and security-gated
[ ] LSM-14 : All v1.0 Kafka events registered
[ ] LSM-15 : Audit trail active for all leaderboard mutations
[ ] LSM-16 : Antigravity run command acknowledged

NEW GATES (v2.0 Predictive AI):
[ ] PAI-0  : PPAI-E identity declared; AI advisory-only enforced
[ ] PAI-1  : All 7 predictive models declared with feature vectors and output schemas
[ ] PAI-2  : All PAI data model entities created:
              PredictiveModelRegistry, RankTrajectoryPrediction,
              ChampionshipReadinessPrediction, DropoutRiskAssessment,
              SkillVelocityReport, PeerBenchmarkReport,
              ManipulationRiskPrediction, ParentInsightReport,
              PredictiveAIAuditLog, ModelBiasAuditRecord, ParentPredictiveAlert
[ ] PAI-3  : All 7 prediction pipeline jobs implemented with Kafka events
[ ] PAI-4  : Explainability schema enforced on all model outputs;
              LOW confidence outputs suppressed from parents
[ ] PAI-5  : All 10 parent alert types implemented per taxonomy; frequency cap active
[ ] PAI-6  : Parent Predictive Dashboard (7 sections) implemented in Flutter;
              all PII filters active
[ ] PAI-7  : Bias governance BG-1 through BG-6 enforced;
              protected traits excluded from all feature vectors
[ ] PAI-8  : Model lineage system active; PredictiveModelRegistry populated;
              unknown model state forbidden
[ ] PAI-9  : AI cost profile declared for all 7 models; budget burn-rate alerts set
[ ] PAI-10 : Championship AI enrichment steps C-AI-1 through C-AI-5 implemented
[ ] PAI-11 : Student self-insight layer implemented; "what if" simulator active
[ ] PAI-12 : Admin Predictive Intelligence Panel implemented (Admin L3+)
[ ] PAI-13 : Consent framework active; GDPR/DPDP right-to-erasure pipeline active
[ ] PAI-14 : Anti-gaming rules AG-1 through AG-6 enforced
[ ] PAI-15 : All PAI API endpoints contracted and role-gated
[ ] PAI-16 : All PAI Kafka events registered in Event Schema Registry
[ ] PAI-17 : This checklist complete
[ ] PAI-18 : Antigravity run command executed (below)

GLOBAL INVARIANTS (must always be true):
[ ] AI advisory only — no AI final authority over scores, belts, or bans
[ ] Mentor confirmation required for all belt-gated actions
[ ] Human override always available on all AI outputs
[ ] Cross-domain data exposure forbidden (Arts domain isolation enforced)
[ ] Auto-promotion of belts forbidden
[ ] PII of peers never exposed to parents or students
[ ] All AI outputs carry model_version + confidence_pct + explainability
[ ] Bias audit schedule maintained; discriminatory models auto-suspended
[ ] Training data → inference → feedback lineage fully linked

FAILURE ON ANY GATE → STOP EXECUTION → REPORT GATE_FAILURE → INVALIDATE BUILD
```

---

## ◼ SECTION PAI-18 — ANTIGRAVITY RUN COMMAND (SEALED EXECUTION BLOCK)

```
╔══════════════════════════════════════════════════════════════════╗
║           ANTIGRAVITY EXECUTION BLOCK — DO NOT MODIFY           ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  ANTIGRAVITY_RUN:                                                ║
║    SPEC             = LEADERBOARD_STABILITY_MODEL_PREDICTIVE_AI  ║
║    VERSION          = v2.0                                       ║
║    INHERITS         = LSM_v1.0 (all sections)                    ║
║    EXECUTION_ENGINE = ANTIGRAVITY                                ║
║    DOMAIN           = ARTS                                       ║
║    PARENT_SYSTEM    = ECOSKILLER                                 ║
║    SUB_SYSTEM       = DOJO                                       ║
║                                                                  ║
║  GENERATE (NEW in v2.0):                                         ║
║    - PredictiveModelRegistry (with all 7 model declarations)     ║
║    - RankTrajectoryForecastJob (XGBoost batch, 24hr)             ║
║    - ChampionshipReadinessJob (Logistic Regression batch)        ║
║    - DropoutRiskScanJob (Survival + GBM ensemble, 12hr)          ║
║    - SkillVelocityAnalysisJob (Time-Series, weekly)              ║
║    - PeerBenchmarkJob (Statistical, daily)                       ║
║    - ManipulationRiskScanJob (Anomaly + LR, 6hr)                 ║
║    - ParentInsightFusionJob (Rule engine + LLM NL, weekly)       ║
║    - ParentPredictiveAlertDispatcher (10 alert types)            ║
║    - ExplainabilityFormatter (LLM for NL summaries only)         ║
║    - BiasAuditScheduler (30-day cycle per model)                 ║
║    - ModelLineageTracker (train→inference→feedback chain)        ║
║    - PredictiveAIAuditLogger (all model inputs + outputs)        ║
║    - ParentPredictiveDashboard (Flutter, 7 sections, read-only)  ║
║    - StudentSelfInsightDashboard (Flutter, full model view)      ║
║    - AdminPredictiveIntelligencePanel (Flutter/Web, L3+)         ║
║    - ConsentWithdrawalPredictionPurgeJob (GDPR/DPDP)             ║
║    - ChampionshipAIEnrichmentPipeline (5 steps, C-AI-1:5)        ║
║    - PAI API endpoints (PAI-15)                                  ║
║    - PAI Kafka event publishers (PAI-16)                         ║
║    - PAI data model migrations (PAI-2)                           ║
║                                                                  ║
║  ENFORCE:                                                        ║
║    - All PAI gates (PAI-0 through PAI-18)                        ║
║    - All LSM gates (LSM-1 through LSM-16)                        ║
║    - Protected trait exclusion from ALL feature vectors          ║
║    - Bias audit on every model before production deployment      ║
║    - AI advisory only — zero final authority                     ║
║    - Parent dashboard: 7 sections, all PII filters active        ║
║    - Explainability on every prediction output                   ║
║    - LOW confidence suppression from parent view                 ║
║    - Human override always available                             ║
║    - Model lineage end-to-end linked                             ║
║    - Consent framework before any parent report delivery         ║
║    - Minor protection: parent_account_verified required          ║
║    - Alert frequency caps enforced                               ║
║    - Anti-gaming rules AG-1:6 active                             ║
║                                                                  ║
║  FORBIDDEN:                                                      ║
║    - Creative interpretation of any section                      ║
║    - Architecture simplification                                 ║
║    - LLM for scoring, ranking, or classification                 ║
║    - AI final authority on scores, belts, or bans                ║
║    - Cross-domain data exposure (Arts isolation enforced)        ║
║    - Auto-promotion of belts                                     ║
║    - Peer PII in parent or student views                         ║
║    - Protected traits in any feature vector                      ║
║    - Deployment without bias audit passing                       ║
║    - Skipping any PAI or LSM section                             ║
║    - Model deployment without PredictiveModelRegistry entry      ║
║                                                                  ║
║  FAILURE MODE = STOP → REPORT → NO PARTIAL OUTPUT               ║
║                                                                  ║
╠══════════════════════════════════════════════════════════════════╣
║  ANTIGRAVITY_SEAL: LSM-PPAI-v2.0-ARTS-LOCKED ✅                 ║
╚══════════════════════════════════════════════════════════════════╝
```

---

```
╔══════════════════════════════════════════════════════════════════════════╗
║  🔒 DOCUMENT SEAL                                                        ║
║  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━   ║
║  ARTIFACT   : LEADERBOARD_STABILITY_MODEL_PREDICTIVE_AI_ANTIGRAVITY.md  ║
║  VERSION    : v2.0                                                       ║
║  INHERITS   : LSM v1.0 (fully)                                           ║
║  DOMAIN     : ARTS (Dojo SaaS)                                           ║
║  PLATFORM   : ECOSKILLER                                                 ║
║  ENGINE     : ANTIGRAVITY                                                ║
║  SECTIONS   : LSM-1:16 (inherited) + PAI-0:18 (new — 19 sections)       ║
║  MODELS     : 7 Predictive ML Models declared                           ║
║  DATA MODEL : 11 new entities declared                                  ║
║  API        : 9 new endpoints declared                                  ║
║  EVENTS     : 5 new Kafka events declared                               ║
║  STATUS     : SEALED · LOCKED · PRODUCTION-READY SPEC                  ║
║  MUTATION   : ADD-ONLY VIA VERSION BUMP                                 ║
║  SIGNED     : ECOSKILLER PLATFORM GOVERNANCE                            ║
║  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━   ║
╚══════════════════════════════════════════════════════════════════════════╝
```
