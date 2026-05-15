# 🔒 SEALED & LOCKED AGENT SPECIFICATION
# LEARNING VELOCITY MODEL AGENT (LVMA)
# ECOSKILLER ANTIGRAVITY — ENTERPRISE SAAS INTELLIGENCE CORE
# VERSION: v1.0.0 | MUTATION_POLICY: ADD-ONLY | EXECUTION_MODE: DETERMINISTIC

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║  CLASSIFICATION      : INTELLIGENCE-CRITICAL + CURRICULUM-GOVERNANCE AGENT  ║
║  DOMAIN              : LEARNING SCIENCE + ADAPTIVE SKILL VELOCITY MODELING   ║
║  AGENT CLASS         : ML-Primary (76%) + LLM-Assist (24%)                  ║
║  MUTATION            : SEALED — CHANGES REQUIRE VERSION BUMP +               ║
║                        GOVERNANCE AGENT APPROVAL                             ║
║  CREATIVE INTERP.    : FORBIDDEN                                             ║
║  ASSUMPTION FILLING  : FORBIDDEN                                             ║
║  DEFAULT BEHAVIOR    : DENY                                                  ║
║  FAILURE MODE        : HALT + LOG + ESCALATE                                 ║
║  CROSS-TENANT        : FORBIDDEN — ZERO EXCEPTIONS                           ║
║  SILENT FAILURE      : FORBIDDEN — EVERY PATH MUST LOG                      ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY)

```yaml
AGENT_NAME        : LEARNING_VELOCITY_MODEL_AGENT
AGENT_ID          : LVMA-001
AGENT_ALIAS       : LVMA

SYSTEM_ROLE: >
  Continuously measure, model, predict, and optimise the learning velocity
  of every entity (student, professional, teacher, cohort, institute) on
  the Ecoskiller Antigravity platform. LVMA quantifies HOW FAST and
  HOW DURABLY an entity acquires, retains, and transfers skill across the
  Dojo belt ladder, adaptive intelligence tracks, project execution cycles,
  championship preparation arcs, and AI-mentor guided learning paths.
  LVMA produces a structured LEARNING_VELOCITY_VECTOR (LVV) per entity per
  skill domain, feeds adaptive content sequencing, powers belt promotion
  gating, drives curriculum review, and surfaces learning regression signals
  before they become irreversible drop-offs. LVMA is the platform's memory
  of HOW every learner learns — not just what they know.

PRIMARY_DOMAIN    : Learning Science + Adaptive Skill Velocity Modeling
SECONDARY_DOMAINS :
  - Belt Promotion Gating (Dojo Engine)
  - Adaptive Content Sequencing (AI Mentor System)
  - Curriculum Effectiveness Analytics (Institute + Creator Systems)
  - Intelligence Growth Tracking (Intelligence Measurement System)
  - Retention & Regression Detection (Skill Reliability System)
  - Parent Progress Reporting (Parent System)
  - Cohort Benchmarking (Institute Analytics)
  - Career Readiness Velocity (Talent Marketplace)

EXECUTION_MODE    : Deterministic + Validated
AGENT_TIER        : Core Intelligence Layer — Entity-bound + Track-bound

DATA_SCOPE: >
  Skill assessment scores + timelines | Belt progression history |
  Session completion rates | Drill engagement depth | Pre/post assessment
  deltas | Scenario difficulty exposure | Retention check outcomes |
  Intelligence radar growth | AI Mentor interaction logs |
  External tool learning signals (EIE — GitHub, Jira, LMS, etc.) |
  Championship preparation trajectories | Idle gap detection |
  Curriculum path traversal sequences | Parent-reported study hours |
  Cohort normative benchmarks | Migration-imported historical learning data

TENANT_SCOPE      : Strict Isolation — no cross-tenant data access under any condition
FAILURE_POLICY    : HALT on ambiguity | LOG all anomalies | ESCALATE to GOVERNANCE_AGENT
```

---

## 2️⃣ PURPOSE DECLARATION

### The Core Problem This Agent Solves

Ecoskiller Antigravity tracks **what** people know (skills, belt levels, intelligence scores). But the platform's deepest intelligence edge — and its most defensible moat — is knowing **HOW** people learn. Without a dedicated Learning Velocity layer, the platform cannot:

- Distinguish a learner who scored a Belt-3 in 30 days from one who took 18 months — they look identical to recruiters, but represent completely different talent signals
- Detect when a learner is plateauing before they disengage and churn
- Identify learning regression (a previously mastered skill decaying) before it affects championship or job performance
- Provide the AI Mentor System with the right adaptive content at the right moment — without velocity data, all recommendations are generic
- Give institutes measurable proof that their curriculum is actually accelerating learner progress (not just passing scores)
- Validate belt certifications with a velocity confidence seal — a belt earned with high velocity + high retention is far more valuable than one earned slowly with multiple retakes
- Feed parent reports with meaningful growth data, not just current scores
- Give recruiters a **velocity-adjusted talent signal** — fast learners with moderate current skills often outperform slow learners with high current skills in new-hire contexts
- Score creator/educator effectiveness not by student ratings but by measurable learning acceleration caused by their content

LVMA closes this gap by producing a **multi-dimensional Learning Velocity Vector (LVV)** per entity, per skill domain, per time window — continuously updated as new learning events arrive.

---

### The Seven Velocity Axes

```
1. ACQUISITION_VELOCITY      — How fast new skill knowledge is absorbed (raw learning speed)
2. RETENTION_DURABILITY      — How well acquired knowledge persists across time gaps
3. TRANSFER_EFFICIENCY       — How well learning in one context applies to new scenarios
4. REGRESSION_RISK           — Probability that a previously mastered area is decaying
5. PLATEAU_DETECTION_SCORE   — Likelihood that current trajectory will stall before next milestone
6. DIFFICULTY_CLIMB_RATE     — Speed at which learner successfully handles increasing challenge
7. METACOGNITIVE_SIGNAL      — Evidence that learner is self-correcting, strategy-adapting, seeking feedback
```

---

### Input Consumed

```
Skill assessment score timelines         (Scoring Engine — append-only ledger)
Belt progression history + timestamps    (Belt Engine)
Session + drill completion records       (Match Engine + Project Execution Engine)
Pre-assessment & post-assessment pairs   (Scoring Engine + DOJO T1/T2 framework)
Retention check match outcomes           (Scoring Engine — scheduled repeats)
Scenario difficulty calibration data     (Scenario Engine — DOJO T4 framework)
Intelligence radar growth vectors        (Intelligence Measurement System)
AI Mentor interaction logs               (AI Mentor System)
External LMS + work tool signals         (EIE — GitHub, Jira, Moodle, Google Classroom, etc.)
Championship preparation trajectories   (Tournament Engine)
Idle gap events                          (Passive Intelligence Agent — inactivity signals)
Curriculum path traversal sequences     (Skill Development Engine)
Cohort normative benchmarks              (Institute Analytics Agent — anonymized)
Prior LVV records                        (Feature Store Agent)
Trust + identity verification signals   (Trust Verification Agent)
```

### Output Produced

```
LEARNING_VELOCITY_VECTOR (LVV)           — 7-axis scored per entity per skill domain
VELOCITY_CLASS                           — ACCELERATING | STEADY | PLATEAUING | REGRESSING | STALLED
PREDICTED_BELT_READINESS_DATE            — ML-computed date for next belt promotion eligibility
ADAPTIVE_CONTENT_SIGNAL                  — Structured recommendation payload for AI Mentor System
CURRICULUM_EFFECTIVENESS_SCORE          — Per track per creator/educator
REGRESSION_ALERT                         — Structured alert if regression detected above threshold
VELOCITY_NARRATIVE                       — AI-assisted structured explanation (versioned prompt)
COHORT_VELOCITY_BENCHMARK                — Entity's velocity relative to cohort (anonymized)
AUDIT_REFERENCE                          — UUID with full trace
```

### Downstream Agents That Depend on This Agent

```
AI_MENTOR_SYSTEM_AGENT          — Primary consumer — uses ADAPTIVE_CONTENT_SIGNAL for
                                   personalized learning path sequencing
BELT_ENGINE                     — Uses PREDICTED_BELT_READINESS + LVV confidence before
                                   allowing belt promotion gate to open
ENTREPRENEURIAL_RISK_MODEL_AGENT— Uses velocity signals in reliability_risk axis
ACEMA (Collaboration Agent)     — Uses metacognitive_signal in session profiling
SCORING_ENGINE                  — Uses difficulty_climb_rate for scenario assignment
RANK_UPDATE_AGENT               — Uses velocity milestones for XP events
PARENT_INTELLIGENCE_AGENT       — Uses LVV summary for child progress reports
INSTITUTE_ANALYTICS_AGENT       — Uses cohort LVV for curriculum effectiveness reports
CREATOR_EFFECTIVENESS_AGENT     — Uses curriculum_effectiveness_score per content piece
HIRING_MATCH_AGENT              — Uses velocity_class as talent signal modifier
TALENT_MARKETPLACE_AGENT        — Includes velocity-adjusted certification confidence
CHAMPIONSHIP_SEEDING_AGENT      — Uses difficulty_climb_rate for tournament seeding
OBSERVABILITY_AGENT             — Receives performance metrics from LVMA
GOVERNANCE_AGENT                — Receives escalation events (regression alerts, drift)
CURRICULUM_REVIEW_AGENT         — Receives CURRICULUM_EFFECTIVENESS_SCORE below threshold
```

### Upstream Agents That Feed This Agent

```
SCORING_ENGINE                  — Score timelines, pre/post deltas, confidence per score
BELT_ENGINE                     — Promotion history, attempt counts, timestamps
MATCH_ENGINE                    — Session structures, scenario types, difficulty levels
SCENARIO_ENGINE                 — Difficulty calibration data (dynamic — DOJO T4 compliant)
INTELLIGENCE_MEASUREMENT_AGENT  — Radar growth vectors, cognitive style signals
AI_MENTOR_SYSTEM_AGENT          — Content consumed, engagement depth, time-on-content
PASSIVE_INTELLIGENCE_AGENT      — Idle gap events, streak data, engagement patterns
FEATURE_STORE_AGENT             — Normalized behavioral feature vectors, prior LVV
INTEGRATION_HUB_AGENT (EIE)     — External LMS signals (Moodle, Google Classroom),
                                   work tool learning signals (GitHub, Jira)
PROJECT_EXECUTION_ENGINE        — Milestone delivery timelines (learning-by-doing signal)
TRUST_VERIFICATION_AGENT        — Identity verification, anti-farming signals
TOURNAMENT_ENGINE               — Championship preparation trajectories
INSTITUTE_ANALYTICS_AGENT       — Cohort normative benchmarks (anonymized aggregates only)
```

---

## 3️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA: {

  "required_fields": [
    "entity_id",
    "entity_type",
    "tenant_id",
    "domain_track",
    "skill_id",
    "trigger_event",
    "timestamp_utc",
    "request_id"
  ],

  "optional_fields": [
    "assessment_score_timeline",
    "belt_progression_record",
    "session_completion_log",
    "pre_post_assessment_pair",
    "retention_check_outcomes",
    "scenario_difficulty_exposure_log",
    "intelligence_radar_growth_vector",
    "mentor_interaction_log",
    "external_tool_learning_signals",
    "championship_prep_trajectory",
    "idle_gap_events",
    "curriculum_path_sequence",
    "cohort_benchmark_ref_id",
    "prior_lvv_id",
    "force_recompute_flag",
    "velocity_window_days"
  ],

  "field_definitions": {
    "entity_id"       : "UUID v4 — learner, educator, or cohort identifier",
    "entity_type"     : "enum: STUDENT | PROFESSIONAL | EDUCATOR | COHORT | INSTITUTE",
    "tenant_id"       : "UUID — must exist in tenant registry",
    "domain_track"    : "enum: ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION",
    "skill_id"        : "UUID — specific skill being measured, or 'ALL' for full domain sweep",
    "trigger_event"   : "enum: ASSESSMENT_COMPLETE | BELT_PROMOTION_GATE | RETENTION_CHECK_COMPLETE | SESSION_COMPLETE | IDLE_GAP_EXCEEDED | SCHEDULED_REFRESH | AI_MENTOR_REQUEST | CHAMPIONSHIP_PREP_START | INSTITUTE_REPORT_REQUEST | PARENT_REPORT_REQUEST",
    "timestamp_utc"   : "ISO 8601",
    "request_id"      : "UUID — unique within 24h rolling window",

    "assessment_score_timeline": [
      {
        "assessment_id"  : "UUID",
        "skill_id"       : "UUID",
        "score"          : "float [0.0–1.0]",
        "confidence"     : "float [0.0–1.0]",
        "difficulty_tier": "integer [1–10]",
        "timestamp_utc"  : "ISO 8601",
        "attempt_number" : "integer ≥ 1"
      }
    ],

    "pre_post_assessment_pair": {
      "pre_assessment_id"   : "UUID",
      "post_assessment_id"  : "UUID",
      "skill_id"            : "UUID",
      "pre_score"           : "float [0.0–1.0]",
      "post_score"          : "float [0.0–1.0]",
      "gap_days"            : "integer — days between pre and post",
      "curriculum_track_id" : "UUID — which track was traversed between assessments"
    },

    "retention_check_outcomes": [
      {
        "original_assessment_id" : "UUID",
        "retention_check_id"     : "UUID",
        "skill_id"               : "UUID",
        "original_score"         : "float",
        "retention_score"        : "float",
        "gap_days"               : "integer — days since original mastery declared"
      }
    ],

    "idle_gap_events": [
      {
        "skill_id"        : "UUID",
        "gap_start_utc"   : "ISO 8601",
        "gap_end_utc"     : "ISO 8601 or null if ongoing",
        "gap_days"        : "integer"
      }
    ],

    "velocity_window_days": "integer [7–365] — default 90 if not specified"
  },

  "validation_rules": [
    "entity_id must be UUID v4 — reject malformed",
    "entity_type must be from approved enum — reject unknown types",
    "tenant_id must exist in tenant registry — reject if unknown",
    "domain_track must be one of 5 approved tracks — reject unknown",
    "skill_id must be UUID v4 OR literal string 'ALL' — reject malformed",
    "trigger_event must be from approved enum — reject unknown events",
    "timestamp_utc must be ISO 8601 — reject malformed or future-dated beyond +60s",
    "request_id must be unique within 24h — return cached result for duplicates",
    "assessment_score_timeline: scores must be float [0.0–1.0] — reject out-of-range",
    "assessment_score_timeline: difficulty_tier must be integer [1–10] — reject out-of-range",
    "assessment_score_timeline: attempt_number must be ≥ 1 — reject zero or negative",
    "assessment_score_timeline: entries must be chronologically ordered — reject if not",
    "pre_post_assessment_pair: post_timestamp must be after pre_timestamp — reject inversion",
    "pre_post_assessment_pair: gap_days must be ≥ 0 — reject negative",
    "retention_check_outcomes: retention_check timestamp must be after original — reject inversion",
    "idle_gap_events: gap_end_utc must be after gap_start_utc if not null — reject inversion",
    "velocity_window_days: must be integer [7–365] — reject out-of-range",
    "COHORT entity_type: cohort_benchmark_ref_id must be present — reject if absent",
    "EDUCATOR entity_type: curriculum_path_sequence must be present — reject if absent",
    "BELT_PROMOTION_GATE trigger: assessment_score_timeline and belt_progression_record are mandatory — reject if absent",
    "minimum data requirement: entity must have at least 3 scored events in velocity_window — else return INSUFFICIENT_DATA response (not error)"
  ],

  "security_checks": [
    "Validate tenant_id matches actor JWT claims — HALT on cross-tenant attempt",
    "Validate entity_id belongs to calling tenant's namespace",
    "Verify request signed with LVMA-authorized agent-level API key",
    "Rate limit: max 300 LVV requests per tenant per minute — queue excess",
    "Sanitize all string fields — reject injection patterns",
    "Verify skill_id exists in skill registry for specified domain_track — reject foreign skills",
    "External tool signals: verify source matches approved EIE provider list",
    "COHORT entity_type: verify requesting actor has INSTITUTE_ANALYTICS or ADMIN role",
    "PARENT_REPORT_REQUEST trigger: verify entity_id is linked child of requesting parent",
    "Retention check data: verify original_assessment_id exists in Scoring Engine ledger"
  ],

  "domain_checks": [
    "skill_id must belong to domain_track — reject cross-domain skill measurement",
    "curriculum_track_id must belong to domain_track — reject cross-domain path data",
    "Scenario difficulty data must come from domain-calibrated scenario pool",
    "External LMS signals: verify LMS content is tagged to correct domain_track",
    "Cross-domain skill transfers allowed only with explicit CROSS_DOMAIN_LEARNING_GRANT"
  ],

  "null_tolerance_policy": {
    "entity_id"                      : "REJECT — mandatory",
    "entity_type"                    : "REJECT — mandatory",
    "tenant_id"                      : "REJECT — mandatory",
    "domain_track"                   : "REJECT — mandatory",
    "skill_id"                       : "REJECT — mandatory",
    "trigger_event"                  : "REJECT — mandatory",
    "assessment_score_timeline"      : "ALLOW NULL — reduces confidence; sets data_sparse_flag = true; min 3 events required for any computation",
    "belt_progression_record"        : "ALLOW NULL — belt velocity axis unavailable; flagged in missing_axes",
    "pre_post_assessment_pair"       : "ALLOW NULL — acquisition_velocity computed from timeline only; narrative flags absence",
    "retention_check_outcomes"       : "ALLOW NULL — retention_durability axis unavailable; flags missing_axes",
    "idle_gap_events"                : "ALLOW NULL — regression_risk computed without explicit gap data; lower confidence",
    "scenario_difficulty_exposure_log": "ALLOW NULL — difficulty_climb_rate unavailable; flagged in missing_axes",
    "mentor_interaction_log"         : "ALLOW NULL — metacognitive_signal reduced to behavioral proxies only",
    "external_tool_learning_signals" : "ALLOW NULL — reduces integration_evidence_score; no penalty if tools unconnected",
    "cohort_benchmark_ref_id"        : "ALLOW NULL — cohort comparison disabled; relative percentile not computed",
    "prior_lvv_id"                   : "ALLOW NULL — trajectory delta disabled; first-measurement flag set",
    "velocity_window_days"           : "DEFAULT 90 days if null — never reject"
  }
}
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

### Primary Output Object

```json
OUTPUT_SCHEMA: {

  "entity_lvv": {
    "entity_id"                     : "UUID",
    "entity_type"                   : "STUDENT | PROFESSIONAL | EDUCATOR | COHORT | INSTITUTE",
    "domain_track"                  : "ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION",
    "skill_id"                      : "UUID or 'ALL'",
    "velocity_window_days"          : "integer — window used for this computation",

    "learning_velocity_vector": {
      "acquisition_velocity_score"      : "float [0.00–1.00]",
      "retention_durability_score"      : "float [0.00–1.00]",
      "transfer_efficiency_score"       : "float [0.00–1.00]",
      "regression_risk_score"           : "float [0.00–1.00] — higher = more risk",
      "plateau_detection_score"         : "float [0.00–1.00] — higher = more likely to plateau",
      "difficulty_climb_rate_score"     : "float [0.00–1.00]",
      "metacognitive_signal_score"      : "float [0.00–1.00]"
    },

    "velocity_composite_score"      : "integer [0–1000]",
    "velocity_class"                : "enum: ACCELERATING | STEADY | PLATEAUING | REGRESSING | STALLED",
    "confidence_score"              : "float [0.00–1.00]",
    "missing_axes"                  : ["list of LVV axes excluded due to absent input data"],
    "data_sparse_flag"              : "boolean",
    "computation_path"              : "enum: FULL | PARTIAL | CACHED | DEGRADED | INSUFFICIENT_DATA"
  },

  "predictive_outputs": {
    "predicted_belt_readiness_date"           : "ISO 8601 date — null if insufficient data",
    "predicted_belt_readiness_confidence"     : "float [0.00–1.00]",
    "predicted_next_plateau_date"             : "ISO 8601 date — null if velocity_class = ACCELERATING",
    "predicted_regression_trigger_date"       : "ISO 8601 date — null if regression_risk_score < 0.40",
    "career_readiness_velocity_percentile"    : "integer [0–100] — relative to domain cohort",
    "learning_potential_ceiling_score"        : "float [0.00–1.00] — upper bound estimate based on trajectory"
  },

  "adaptive_content_signal": {
    "recommended_content_type"      : "enum: DRILL | SCENARIO | CHALLENGE | RETENTION_CHECK | THEORY | MENTORING | REST",
    "recommended_difficulty_tier"   : "integer [1–10]",
    "recommended_skill_focus"       : ["ordered list of skill_ids — highest priority first"],
    "pacing_recommendation"         : "enum: ACCELERATE | MAINTAIN | SLOW_DOWN | PAUSE_AND_CONSOLIDATE",
    "urgency_signal"                : "enum: NONE | LOW | MEDIUM | HIGH | CRITICAL",
    "intervention_type"             : "enum: NONE | MENTOR_SESSION | PEER_STUDY | CONTENT_REMIX | GAMIFIED_CHALLENGE | PARENT_ALERT",
    "intervention_reason"           : "string — structured reason code"
  },

  "regression_alert": {
    "alert_active"                  : "boolean",
    "affected_skill_ids"            : ["list of UUID — skills showing regression signal"],
    "regression_severity"           : "enum: MILD | MODERATE | SEVERE | CRITICAL",
    "days_since_last_mastery_event" : "integer",
    "recommended_recovery_action"   : "string — structured action code"
  },

  "curriculum_effectiveness_score"  : "float [0.00–1.00] — populated only for EDUCATOR entity_type or INSTITUTE_REPORT_REQUEST",

  "cohort_velocity_benchmark"       : {
    "cohort_percentile"             : "integer [0–100] — null if cohort_benchmark_ref_id absent",
    "cohort_velocity_class_distribution": {
      "ACCELERATING": "float — % of cohort",
      "STEADY"       : "float",
      "PLATEAUING"   : "float",
      "REGRESSING"   : "float",
      "STALLED"      : "float"
    },
    "cohort_avg_velocity_composite" : "integer — cohort average for comparison"
  },

  "velocity_narrative": {
    "summary"                       : "string — 2–3 sentences (AI-assisted, versioned prompt)",
    "velocity_drivers"              : ["ordered list — top 3 positive velocity signals"],
    "velocity_blockers"             : ["ordered list — top 3 signals suppressing velocity"],
    "learning_style_signal"         : "string — observed pattern (e.g. 'spaced retrieval strength', 'difficulty-avoidant')",
    "parent_facing_summary"         : "string — simplified version for PARENT_REPORT_REQUEST only",
    "educator_facing_recommendation": "string — curriculum adjustment note for EDUCATOR entity_type"
  },

  "model_version"                   : "string — e.g. LVMA-ML-v3.1.0",
  "prompt_version"                  : "string — e.g. LVMA-PROMPT-v2.0.0",
  "audit_reference"                 : "UUID v4",
  "next_trigger_event"              : [
    "LVV_UPDATED_EVENT",
    "ADAPTIVE_CONTENT_SIGNAL_EMITTED",
    "REGRESSION_ALERT_EMITTED (conditional)",
    "BELT_GATE_SIGNAL_EMITTED (conditional)",
    "CURRICULUM_FLAG_EMITTED (conditional)"
  ],
  "timestamp_utc"                   : "ISO 8601"
}
```

### Velocity Class Thresholds (Immutable)

```
velocity_composite_score mapping:
  850–1000  → VELOCITY_CLASS = ACCELERATING   → pacing_recommendation = ACCELERATE
  650–849   → VELOCITY_CLASS = STEADY         → pacing_recommendation = MAINTAIN
  450–649   → VELOCITY_CLASS = PLATEAUING     → pacing_recommendation = SLOW_DOWN + CONTENT_REMIX
  250–449   → VELOCITY_CLASS = REGRESSING     → pacing_recommendation = PAUSE_AND_CONSOLIDATE + MENTOR_SESSION
  0–249     → VELOCITY_CLASS = STALLED        → urgency_signal = CRITICAL + ESCALATION

Override rules:
  IF regression_risk_score >= 0.75              → VELOCITY_CLASS forced minimum to REGRESSING
                                                   regardless of composite score
  IF plateau_detection_score >= 0.80            → VELOCITY_CLASS forced minimum to PLATEAUING
  IF confidence_score < 0.45                    → pacing_recommendation demoted one level
                                                   conservative + low_confidence_flag = true
  IF idle_gap > 21 days with no events          → regression_risk_score floor set to 0.60
                                                   regardless of model output
  IF retention_check delta < -0.20              → SEVERITY = SEVERE regression alert regardless
                                                   of overall composite score
  IF BELT_PROMOTION_GATE trigger AND confidence_score < 0.60
                                                → Belt promotion BLOCKED until full data available
                                                   (compliant with DOJO T2 framework)
```

---

## 5️⃣ ML / AI LOGIC LAYER

### ML Layer — 76% of Computation

```yaml
MODEL_ARCHITECTURE: Multi-Stage Temporal Pipeline

STAGE_1 — TEMPORAL_SIGNAL_EXTRACTOR:
  MODEL_TYPE: Feature Engineering Pipeline (statistical + time-series preprocessing)
  PURPOSE: >
    Transform raw assessment score timelines, session logs, retention checks,
    and idle gap events into structured temporal feature tensors.
    Handles irregular time intervals, missing observations, and
    multi-skill concurrent trajectories.
  KEY_OPERATIONS:
    - Score curve smoothing (LOWESS) — reduces noise while preserving trend
    - Learning curve parameterization (power law fit per skill per entity)
    - Forgetting curve estimation (Ebbinghaus-derived model per skill)
    - Attempt normalization — scores normalized by difficulty_tier
    - Gap imputation — structured approach for missing observations (not random fill)
  OUTPUT: Normalized temporal feature tensor per entity per skill per window

STAGE_2 — AXIS_VELOCITY_SCORERS (7 independent models):
  MODEL_TYPE: Gradient Boosted Regressor (LightGBM) — one per LVV axis
  PURPOSE: Score each of the 7 LVV axes independently and in isolation
  CALIBRATION: Isotonic regression calibration per axis output
  INTERPRETABILITY: SHAP values computed per prediction — stored in audit record

STAGE_3 — VELOCITY_COMPOSITOR:
  MODEL_TYPE: Weighted Ensemble (Stacking — Ridge meta-learner)
  PURPOSE: >
    Combine 7 axis scores into velocity_composite_score.
    Learns that acquisition_velocity without retention_durability is
    fragile velocity — should not score as highly as balanced vectors.
    Axis interaction weights are domain-specific (ARTS weights
    metacognitive_signal higher; TECHNOLOGY weights difficulty_climb_rate higher).
  DOMAIN_WEIGHT_MATRICES: Separate learned weight matrix per domain_track
  IMMUTABILITY: Domain weight matrices immutable between MINOR version bumps

STAGE_4 — PREDICTIVE_FORECASTER:
  MODEL_TYPE: Probabilistic Time-Series (Prophet + Bayesian structural time series)
  PURPOSE: >
    Forecast predicted_belt_readiness_date, predicted_next_plateau_date,
    predicted_regression_trigger_date. Outputs probability distributions
    with confidence intervals — not point estimates only.
  UNCERTAINTY_QUANTIFICATION: Required — output includes 80% credible interval
  CALIBRATION: Coverage probability calibrated against holdout set quarterly

STAGE_5 — REGRESSION_DETECTOR:
  MODEL_TYPE: Isolation Forest + Exponentially Weighted Moving Average (EWMA) control charts
  PURPOSE: >
    Detect knowledge regression — a SCORED_WELL-in-the-past skill whose
    recent scores or retention check results show statistically significant decay.
    Two detection methods run in parallel — structural (IF) + statistical (EWMA).
    Both must agree for SEVERE or CRITICAL regression alert.
  SENSITIVITY: High recall priority — false positive regression alert tolerated
               over false negative (missing regression is more dangerous)

STAGE_6 — CURRICULUM_EFFECTIVENESS_SCORER:
  MODEL_TYPE: Causal Inference Model (Difference-in-Differences) + Random Forest
  PURPOSE: >
    Isolate the curriculum contribution to learning velocity changes.
    Controls for entity prior velocity, peer effects, and external signals.
    Produces curriculum_effectiveness_score per track per educator per content piece.
  CAUSAL_IDENTIFICATION: >
    Uses natural experiments — learners who traversed the same curriculum at
    different times form control/treatment groups. Requires minimum 100 learners
    per curriculum_track_id before score is valid.
  MINIMUM_SAMPLE: 100 learners — below threshold, curriculum_effectiveness_score = null
                  with insufficient_curriculum_data_flag = true
```

### Complete Feature Registry (76 features)

```yaml
FEATURES_USED:

  Acquisition Speed Features:
    - score_improvement_per_attempt_avg          # raw learning rate
    - score_improvement_per_session_hour         # time-normalized learning rate
    - first_attempt_score_by_difficulty_tier     # learning efficiency at each difficulty
    - attempts_to_mastery_count                  # inverse efficiency signal
    - time_to_mastery_hours                      # absolute time to declared mastery
    - score_curve_slope_7d                       # recent learning momentum
    - score_curve_slope_30d                      # medium-term learning momentum
    - score_curve_slope_90d                      # long-term learning momentum
    - power_law_learning_rate_parameter          # individual learning rate constant (Stage 1)

  Retention & Forgetting Features:
    - retention_check_score_delta                # score drop from original mastery
    - retention_half_life_days                   # estimated days to 50% decay (Ebbinghaus fit)
    - spacing_effect_evidence_score              # benefit gained from spaced practice
    - retention_check_completion_rate            # did entity actually do retention checks?
    - avg_days_between_skill_touches             # practice frequency
    - max_idle_gap_days_90d                      # longest skill gap in window
    - retention_check_score_trend                # improving or worsening over time

  Transfer Efficiency Features:
    - cross_scenario_type_score_consistency      # same skill, different scenario formats
    - cross_difficulty_transfer_score            # applying easy-context learning to hard context
    - novel_scenario_first_attempt_score         # unseen scenario performance
    - knowledge_application_in_project_score     # work product quality from Project Engine
    - external_tool_performance_corr_with_score  # GitHub/Jira signal aligns with assessment score

  Plateau Detection Features:
    - score_variance_last_10_sessions            # decreasing variance = plateau signal
    - score_range_contraction_rate               # range narrowing over time
    - new_difficulty_tier_attempt_rate           # is entity stretching to harder content?
    - challenge_avoidance_index                  # opting for safe difficulty vs stretch
    - scenario_repeat_rate                       # re-doing known scenarios instead of new
    - days_since_last_difficulty_tier_increase   # stagnation signal
    - peer_percentile_rank_stability_30d         # rank not changing = plateau proxy

  Regression Risk Features:
    - ewma_score_control_chart_signal            # statistical out-of-control flag
    - retention_check_score_vs_mastery_threshold # below-mastery on retention check
    - idle_gap_regression_probability            # Ebbinghaus-derived decay probability
    - skill_usage_frequency_decay_rate           # decreasing engagement with skill
    - assessment_difficulty_retreat_flag         # entity choosing easier content
    - recent_session_score_below_historical_avg  # score regression vs own baseline
    - error_pattern_recurrence_rate              # same mistakes reappearing

  Difficulty Climb Features:
    - difficulty_tier_progression_rate           # avg tier increase per 30d
    - success_rate_at_stretch_tier               # performance 1–2 tiers above current comfort
    - first_attempt_pass_rate_at_new_tier        # mastery of new difficulty levels
    - championship_prep_difficulty_growth_rate   # difficulty progression in competition prep
    - belt_promotion_attempt_difficulty_readiness# difficulty level at promotion attempt

  Metacognitive Signal Features:
    - mentor_feedback_seek_rate                  # voluntary mentor interaction frequency
    - self_correction_event_count                # identifying own errors before mentor flags
    - drill_strategy_variety_score               # using multiple practice methods
    - error_analysis_engagement_rate             # engaging with wrong-answer explanations
    - goal_setting_completion_rate               # setting and tracking own learning goals
    - learning_plan_adherence_score              # following AI mentor recommendations
    - retry_strategy_change_flag                 # changing approach on retries (not just repeating)

  Context & External Features:
    - external_lms_completion_rate               # Moodle, Google Classroom completion
    - external_tool_skill_signal_alignment       # EIE work signal matches declared skill
    - championship_performance_vs_training_delta # competition score vs training score
    - cohort_relative_velocity_percentile        # relative to peers
    - parent_reported_study_hours_correlation    # if available
    - institute_curriculum_quality_score         # from Stage 6 curriculum scorer

TRAINING_FREQUENCY:
  FULL_RETRAIN            : Monthly (accumulated session + assessment data)
  INCREMENTAL_UPDATE      : Weekly (online updates for Stage 2 axis scorers)
  FORECASTING_MODELS      : Retrained per entity on each new observation (online Bayesian)
  REGRESSION_DETECTOR     : Continuous — streaming updates per new event
  CURRICULUM_SCORER       : Quarterly (requires accumulated cohort data)

TRAINING_DATA_REQUIREMENTS:
  Minimum labeled observations per entity for full LVV: 10 assessed events
  Minimum events for forecasting: 15 time-ordered scored events
  Minimum cohort size for curriculum effectiveness: 100 learners per track

DRIFT_DETECTION:
  FEATURE_DRIFT:
    Method: Population Stability Index (PSI) per feature — weekly
    Threshold: PSI > 0.20 triggers MODEL_REVIEW_EVENT to GOVERNANCE_AGENT
  PREDICTION_DRIFT:
    Method: Score distribution KS-test — weekly per axis
    Threshold: p < 0.05 on any primary axis triggers alert
  FORECASTING_CALIBRATION:
    Method: Coverage probability check — monthly against realized outcomes
    Threshold: Coverage < 70% on 80% credible intervals triggers recalibration
  REGRESSION_DETECTOR_RECALL:
    Method: Recall on labeled regression events — monthly
    Threshold: Recall < 0.85 triggers immediate model review (CRITICAL)

VERSION_CONTROL:
  All model artifacts in Model Registry (append-only, versioned)
  model_version tag: LVMA-ML-vMAJOR.MINOR.PATCH
  Feature schema versioned separately: LVMA-FEATURES-vX.Y
  Domain weight matrices versioned: LVMA-WEIGHTS-{DOMAIN}-vX.Y
  Shadow mode: New versions run in parallel 14 days before promotion
  Rollback: Prior version restorable within 15 minutes
```

### AI / LLM Layer — 24% — Narrative, Interpretation, Adaptive Recommendation Text

```yaml
AI_USAGE_SCOPE:
  PERMITTED:
    - Generate velocity_narrative.summary (max 3 sentences)
    - Generate velocity_narrative.velocity_drivers (ordered list, max 3 items)
    - Generate velocity_narrative.velocity_blockers (ordered list, max 3 items)
    - Generate velocity_narrative.learning_style_signal (max 1 sentence — observed pattern only)
    - Generate velocity_narrative.parent_facing_summary (simplified, max 2 sentences — PARENT trigger only)
    - Generate velocity_narrative.educator_facing_recommendation (curriculum note, max 5 sentences)
    - Generate adaptive_content_signal.intervention_reason (1 sentence structured reason)
    - Generate regression_alert.recommended_recovery_action description (max 3 sentences)

  STRICTLY FORBIDDEN:
    - AI must NOT compute any LVV axis score
    - AI must NOT determine velocity_class
    - AI must NOT set predicted_belt_readiness_date
    - AI must NOT trigger regression_alert
    - AI must NOT set pacing_recommendation or urgency_signal
    - AI must NOT access raw assessment scores or timelines
    - AI must NOT access participant PII — receives only entity_id + aggregated scores + velocity_class
    - AI must NOT infer learning style without structured evidence from ML features
    - AI must NOT generate motivational or emotional content — factual structured narrative only
    - AI must NOT override recommended_content_type or difficulty_tier recommendations
    - AI must NOT fill missing axes with assumptions

PROMPT_GOVERNANCE:
  PROMPT_REGISTRY       : All prompts versioned — LVMA-PROMPT-vMAJOR.MINOR.PATCH
  PROMPT_STRUCTURE      : Deterministic template with variable injection only
  INPUT_TO_LLM          : Structured JSON: velocity_class + axis scores (no raw data) +
                          top SHAP features (anonymized labels only) + entity_type
  OUTPUT_VALIDATION     : LLM output validated against strict JSON schema before use
  FALLBACK              : If LLM unavailable or output fails schema →
                          static template narrative per velocity_class
  LATENCY_BUDGET        : LLM call max 2,500ms — timeout triggers static fallback
  PARENT_PROMPT_VERSION : Separate child-appropriate language version
                          LVMA-PROMPT-PARENT-vX.Y — simplified vocabulary, no jargon
  EDUCATOR_PROMPT_VERSION: Separate curriculum-focused version
                          LVMA-PROMPT-EDUCATOR-vX.Y — pedagogical vocabulary

DOMAIN_SPECIFIC_PROMPTS:
  LVMA-PROMPT-ARTS-vX.Y
  LVMA-PROMPT-COMMERCE-vX.Y
  LVMA-PROMPT-SCIENCE-vX.Y
  LVMA-PROMPT-TECHNOLOGY-vX.Y
  LVMA-PROMPT-ADMINISTRATION-vX.Y

AI_ASSIST_RULE:
  AI narrates what ML measured and predicted.
  AI does NOT replace, modify, or reinterpret ML outputs.
  All scores, classes, dates, and recommendations trace to ML computation.
```

---

## 6️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS:
  Steady State              : 10,000 requests/second
  Peak (post-assessment surge): 50,000 requests/second
  Peak (institute report batch): 25,000 requests/second
  Peak (championship prep wave): 30,000 requests/second

LATENCY_TARGET:
  p50                       : < 250ms
  p95                       : < 700ms
  p99                       : < 1,600ms
  REAL_TIME_PATH (belt gate + AI mentor request): < 400ms enforced SLA
  FORECASTING_PATH (predictive outputs): < 800ms (async acceptable if non-blocking)
  PARENT_REPORT_REQUEST: < 1,000ms (user-facing — acceptable for richness)

MAX_CONCURRENCY: 100,000 simultaneous entity LVV computations

QUEUE_STRATEGY:
  LANE_P0 (CRITICAL — Direct Compute, no queue):
    - BELT_PROMOTION_GATE trigger
    - AI_MENTOR_REQUEST (real-time content sequencing)
    - CHAMPIONSHIP_PREP_START (seeding input)
    Max queue depth: 0 — reject with 429 if ML inference overloaded

  LANE_P1 (HIGH — Priority Queue):
    - ASSESSMENT_COMPLETE (post-exam velocity update)
    - RETENTION_CHECK_COMPLETE
    - REGRESSION urgency_signal = CRITICAL
    Kafka topic: lvma.priority.velocity
    SLA: process within 5 seconds

  LANE_P2 (NORMAL — Standard Queue):
    - SESSION_COMPLETE (standard Dojo sessions)
    - IDLE_GAP_EXCEEDED (passive monitoring)
    - PARENT_REPORT_REQUEST
    - INSTITUTE_REPORT_REQUEST (single entity)
    Kafka topic: lvma.standard.velocity
    SLA: process within 60 seconds

  LANE_P3 (LOW — Batch Processing):
    - SCHEDULED_REFRESH (30d rolling)
    - INSTITUTE bulk cohort reports
    - Curriculum effectiveness recomputation
    - Historical LVV recomputation on model version upgrade
    Kafka topic: lvma.batch.velocity
    Processing window: off-peak hours only

STATELESS_EXECUTION:
  No local mutable state between requests
  Feature tensors cached in Redis (TTL = 4h per entity per skill)
  Prior LVV cached per entity (TTL = 48h — invalidated on new assessment event)
  Forgetting curve parameters cached per entity (TTL = 7d — slow-changing)
  Cohort benchmarks cached per domain per tenant (TTL = 6h)

IDEMPOTENCY:
  Duplicate request_ids within 24h return cached LVV — no recomputation
  Idempotency key: SHA-256(entity_id + skill_id + trigger_event + timestamp_floor_1h)

INFRASTRUCTURE:
  COMPUTE      : Kubernetes autoscaling pods (Node.js orchestrator + Python ML sidecar)
  MESSAGE_BUS  : Kafka (multi-lane input + output events)
  CACHE        : Redis Cluster (feature tensors + LVV results + forgetting curve params)
  STORAGE      : PostgreSQL append-only (audit + LVV history)
  ML_SERVING   : Python FastAPI sidecar per pod (LightGBM + Prophet + IsolationForest)
  FORECASTING  : Prophet models serialized per entity — lazy loaded per request
  TRANSPORT    : Kafka async | gRPC P0 synchronous path
```

---

## 7️⃣ SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  All entity_ids and skill_ids namespaced by tenant_id
  tenant_id validated against JWT claims on every request
  Cross-tenant entity_id access: IMMEDIATE HALT + SECURITY_VIOLATION log
  Database RLS: tenant_id enforced at PostgreSQL level as second defense
  LVV records: tenant_id included in every stored record — no orphaned records

DOMAIN_ISOLATION:
  skill_id validated against domain registry for specified domain_track
  Cross-domain velocity computation blocked without CROSS_DOMAIN_LEARNING_GRANT
  Curriculum path data must be domain-tagged — reject untagged imports
  Domain leak classified as SECURITY_FAILURE → GOVERNANCE + SECURITY_AGENT alert

ROLE_BASED_AUTHORIZATION:
  Who may request LVV computation:
    SYSTEM_AGENTS           : AI_MENTOR_SYSTEM, BELT_ENGINE, SCORING_ENGINE,
                              HIRING_MATCH_AGENT, CHAMPIONSHIP_SEEDING_AGENT
    EDUCATOR / MENTOR role  : Own assigned students only
    INSTITUTE_ADMIN         : All entities within own tenant
    RECRUITER role          : Velocity class + percentile only (no raw axis scores)
    TENANT_ADMIN            : Full scope within own tenant
    COMPLIANCE_ADMIN        : Read-only full tenant scope
    PARENT role             : Own linked children only — parent_facing_summary only
    STUDENT role            : Own LVV only — summarized view (no raw axis scores)

DATA_CLASSIFICATION:
  Raw assessment timelines  : SENSITIVE — never in output, internal only
  LVV axis scores           : INTERNAL — visible to EDUCATOR, MENTOR, ADMIN roles
  Velocity class            : SEMI-PUBLIC — visible in profile to authorized viewers
  Predicted readiness date  : INTERNAL — not exposed to RECRUITER role
  Regression alerts         : SENSITIVE — MENTOR and ADMIN roles only; PARENT simplified version
  Curriculum effectiveness  : INTERNAL — INSTITUTE_ADMIN and CREATOR roles only
  Parent-facing summary     : RESTRICTED — parent-authorized only

ENCRYPTION:
  In transit: TLS 1.3 minimum
  At rest: AES-256 for all LVV data, assessment timelines, narratives
  Model artifacts: Encrypted in Model Registry
  Forgetting curve parameters: Encrypted (contain behavioral fingerprints)
  Audit tables: Encrypted + append-only — no delete

AUDIT_LOGGING:
  Every computation logged before output is returned
  Audit partition: append-only PostgreSQL — INSERT only
  Log retention: 7 years minimum
  Access log: every API call logged — actor, tenant, entity, action, timestamp

MINOR_USER_PROTECTION:
  For STUDENT entity_type where age < 18 (flagged by Trust Verification Agent):
    - Narrative simplified — no deficit framing
    - Regression alerts NOT sent directly to student — sent to PARENT and EDUCATOR only
    - No velocity percentile shown to student directly
    - Parent-facing summary mandatory for all reports involving minors
```

---

## 8️⃣ AUDIT & TRACEABILITY

Every execution MUST produce and persist the following record **before** output is returned:

```json
AUDIT_RECORD: {
  "audit_reference"                 : "UUID v4 — globally unique, immutable",
  "timestamp_utc"                   : "ISO 8601",
  "actor_id"                        : "agent_id or user_id triggering request",
  "tenant_id"                       : "tenant namespace",
  "entity_id"                       : "entity being measured",
  "entity_type"                     : "STUDENT | PROFESSIONAL | EDUCATOR | COHORT | INSTITUTE",
  "domain_track"                    : "domain track",
  "skill_id"                        : "UUID or 'ALL'",
  "trigger_event"                   : "event that caused computation",
  "velocity_window_days"            : "integer",
  "input_hash"                      : "SHA-256 of normalized input payload",
  "output_hash"                     : "SHA-256 of LVV output payload",
  "model_version"                   : "LVMA-ML-vX.Y.Z",
  "prompt_version"                  : "LVMA-PROMPT-vX.Y.Z",
  "feature_schema_version"          : "LVMA-FEATURES-vX.Y",
  "computation_path"                : "FULL | PARTIAL | CACHED | DEGRADED | INSUFFICIENT_DATA",
  "velocity_class"                  : "ACCELERATING | STEADY | PLATEAUING | REGRESSING | STALLED",
  "velocity_composite_score"        : "integer",
  "confidence_score"                : "float",
  "regression_alert_triggered"      : "boolean",
  "belt_gate_signal_emitted"        : "boolean",
  "curriculum_flag_emitted"         : "boolean",
  "anomaly_flags"                   : ["list of anomalies detected"],
  "missing_axes"                    : ["list of axes excluded"],
  "data_sparse_flag"                : "boolean",
  "shap_top_features"               : ["top 5 SHAP feature names — no values, names only"],
  "llm_call_made"                   : "boolean",
  "llm_fallback_used"               : "boolean",
  "processing_time_ms"              : "integer",
  "escalation_triggered"            : "boolean",
  "escalated_to"                    : ["agent IDs escalated to — null if none"]
}
```

**LVV Record Immutability:**
Computed LVV records are append-only. Recomputation creates a new record linked via `prior_lvv_id`. Prior records are never modified or deleted. The full velocity history of every entity is permanently preserved and traceable.

**SHAP Explainability:**
SHAP values are computed per prediction and stored in the audit record (feature names only — no raw values). This ensures every velocity score is explainable to governance reviewers without exposing raw model internals.

---

## 9️⃣ FAILURE POLICY

```yaml
FAILURE_SCENARIOS:

  INVALID_INPUT:
    CONDITION: Required field missing, malformed, security check failed,
               or domain check failed
    POLICY: STOP_EXECUTION
    ACTION: Return structured 400-class error with explicit rejection per field
    LOG: Write VALIDATION_FAILURE audit record
    ESCALATE_TO: None — caller responsible for fix
    RETRY: Not applicable

  INSUFFICIENT_DATA:
    CONDITION: entity has fewer than 3 scored events in velocity_window
               (not an error — legitimate learning state)
    POLICY: RETURN_INSUFFICIENT_DATA (not an error response)
    ACTION: Return structured response with computation_path = INSUFFICIENT_DATA
            Include minimum_events_required and events_available counts
            Return pacing_recommendation = MAINTAIN (conservative default)
    LOG: Write INSUFFICIENT_DATA informational record
    ESCALATE_TO: None
    RETRY: Not applicable — platform should generate more assessment events first

  MODEL_UNAVAILABLE:
    CONDITION: ML inference sidecar unresponsive after 3 retries
    POLICY: DEGRADED_MODE
    ACTION: >
      Check Redis cache for prior LVV (< 48h old) → return with computation_path = CACHED
      If cache absent or expired → return STEADY velocity_class with confidence = 0.10
      Set computation_path = DEGRADED, pacing_recommendation = MAINTAIN (safe default)
    LOG: Write MODEL_UNAVAILABLE incident to OBSERVABILITY_AGENT
    ESCALATE_TO: OBSERVABILITY_AGENT (critical) + ON_CALL_ENGINEER
    RETRY: Exponential backoff: 500ms → 1.5s → 4.5s before DEGRADED fallback

  FORECASTING_MODEL_FAILURE:
    CONDITION: Prophet or BSTS forecasting model fails or returns invalid distribution
    POLICY: PARTIAL_EXECUTION
    ACTION: >
      Return LVV axis scores and velocity_class (Stage 2/3 only)
      Set all predictive_outputs fields to null
      Set computation_path = PARTIAL
    LOG: Write FORECASTING_FAILURE warning record
    ESCALATE_TO: OBSERVABILITY_AGENT (warning level)
    RETRY: Single retry after 2s — if fails, partial output returned

  LLM_NARRATIVE_TIMEOUT:
    CONDITION: LLM call exceeds 2,500ms or returns schema-invalid output
    POLICY: PARTIAL_EXECUTION
    ACTION: Return full ML-computed LVV with static template narrative per velocity_class
            Set llm_fallback_used = true
    LOG: Write LLM_TIMEOUT warning record
    ESCALATE_TO: OBSERVABILITY_AGENT (warning)
    RETRY: Single retry at 1,200ms — if fails, static fallback used

  DATA_CORRUPTION:
    CONDITION: Input hash mismatch detected after processing
    POLICY: STOP_EXECUTION — IMMEDIATE
    ACTION: Discard all computation, return 500-class structured error
    LOG: Write DATA_CORRUPTION critical incident
    ESCALATE_TO: GOVERNANCE_AGENT + SECURITY_AGENT — immediate
    RETRY: FORBIDDEN — requires manual investigation

  REGRESSION_DETECTED_CRITICAL:
    CONDITION: regression_risk_score >= 0.85 OR retention check delta < -0.35
    POLICY: EXECUTE + FORCE_ESCALATE
    ACTION: >
      Complete full computation
      Set regression_alert.alert_active = true
      Set regression_alert.regression_severity = CRITICAL
      Set urgency_signal = CRITICAL
      Emit REGRESSION_ALERT_EVENT to AI_MENTOR_SYSTEM + PARENT_INTELLIGENCE_AGENT
        (if entity is minor) + EDUCATOR
    LOG: Write CRITICAL_REGRESSION_EVENT record
    ESCALATE_TO: AI_MENTOR_SYSTEM_AGENT + GOVERNANCE_AGENT (if entity is at risk of belt demotion)
    RETRY: Not applicable — decision final until new assessment data

  BELT_GATE_LOW_CONFIDENCE:
    CONDITION: BELT_PROMOTION_GATE trigger AND confidence_score < 0.60
    POLICY: BLOCK_PROMOTION + FLAG
    ACTION: >
      Complete computation
      Set belt_gate_signal = BLOCK
      Return structured explanation with missing_axes and data_needed
      (Compliant with DOJO T2: "Low confidence scores cannot trigger belt
      promotion without mentor board review")
    LOG: Write BELT_GATE_LOW_CONFIDENCE record
    ESCALATE_TO: BELT_ENGINE + MENTOR_ASSIGNMENT_AGENT (for manual review)
    RETRY: Not applicable — requires new assessment data

  TENANT_ISOLATION_VIOLATION:
    CONDITION: Request attempts to access entity outside requesting tenant
    POLICY: IMMEDIATE_HALT
    ACTION: Return 403 — zero details disclosed
    LOG: Write SECURITY_VIOLATION critical record
    ESCALATE_TO: SECURITY_AGENT + GOVERNANCE_AGENT + PLATFORM_ADMIN
    RETRY: FORBIDDEN

  CURRICULUM_EFFECTIVENESS_MINIMUM_NOT_MET:
    CONDITION: < 100 learners traversed curriculum_track for STAGE_6 computation
    POLICY: RETURN_INSUFFICIENT_CURRICULUM_DATA (not error)
    ACTION: Return LVV without curriculum_effectiveness_score
            Set insufficient_curriculum_data_flag = true
            Include learners_available count in response
    LOG: Write CURRICULUM_INSUFFICIENT_DATA informational record
    ESCALATE_TO: CURRICULUM_REVIEW_AGENT (informational — track needs more learners)

NO_SILENT_FAILURES: Every failure path writes an audit log and returns
                    a structured response. Raw exceptions never exposed to callers.
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS (agents that feed LVMA):
  SCORING_ENGINE:
    Provides: Assessment score timelines, pre/post pairs, retention check results,
              confidence per score, difficulty tier per assessment
    Protocol: Kafka stream (lvma.scoring.events) + gRPC pull for P0 requests

  BELT_ENGINE:
    Provides: Belt promotion history, attempt counts, timestamps, belt levels
    Protocol: gRPC pull at request time

  MATCH_ENGINE:
    Provides: Session structures, scenario types, role assignments
    Protocol: gRPC pull at request time

  SCENARIO_ENGINE:
    Provides: Difficulty calibration data (dynamic — DOJO T4 compliant),
              pass rates, score distributions per scenario
    Protocol: gRPC pull at request time

  INTELLIGENCE_MEASUREMENT_AGENT:
    Provides: Intelligence radar growth vectors, cognitive style classifications
    Protocol: gRPC pull at request time

  AI_MENTOR_SYSTEM_AGENT:
    Provides: Content consumption logs, engagement depth, time-on-content,
              recommendation adherence rates
    Protocol: Kafka stream (lvma.mentor.interactions)

  PASSIVE_INTELLIGENCE_AGENT:
    Provides: Idle gap events, streak data, session frequency signals
    Protocol: Kafka stream (lvma.passive.signals)

  FEATURE_STORE_AGENT:
    Provides: Normalized behavioral feature vectors, prior LVV references
    Protocol: gRPC pull at request time + Kafka subscription

  INTEGRATION_HUB_AGENT (EIE):
    Provides: External LMS signals (Moodle, Google Classroom completion rates),
              work tool learning signals (GitHub, Jira project delivery)
    Protocol: Kafka stream (lvma.external.learning)

  PROJECT_EXECUTION_ENGINE:
    Provides: Milestone delivery timelines, task completion quality
    Protocol: gRPC pull for project sessions

  TRUST_VERIFICATION_AGENT:
    Provides: Identity verification status, minor flag, anti-farming signals
    Protocol: gRPC pull — mandatory before computation (especially for belt gate)

  TOURNAMENT_ENGINE:
    Provides: Championship preparation trajectories, competition performance history
    Protocol: gRPC pull for CHAMPIONSHIP_PREP_START triggers

  INSTITUTE_ANALYTICS_AGENT:
    Provides: Anonymized cohort normative benchmarks (aggregate only)
    Protocol: gRPC pull for cohort comparison features

DOWNSTREAM_AGENTS (agents that consume LVMA output):
  AI_MENTOR_SYSTEM_AGENT:
    Consumes: ADAPTIVE_CONTENT_SIGNAL (full payload)
    Use: Real-time personalized content sequencing, difficulty targeting
    Priority: P0 — must be sub-400ms

  BELT_ENGINE:
    Consumes: LVV confidence + velocity_class + predicted_belt_readiness_date
    Use: Belt promotion gate decision (DOJO T2 compliant)
    Rule: Belt promotion blocked if LVMA returns BLOCK signal

  ENTREPRENEURIAL_RISK_MODEL_AGENT (ERMA):
    Consumes: velocity_class, retention_durability_score, metacognitive_signal_score
    Use: Feeds reliability_risk axis

  ACEMA (Collaboration Agent):
    Consumes: metacognitive_signal_score, difficulty_climb_rate_score
    Use: Refines individual collaboration profile

  SCORING_ENGINE:
    Consumes: difficulty_climb_rate_score
    Use: Adaptive scenario assignment — next challenge difficulty

  RANK_UPDATE_AGENT:
    Consumes: Velocity milestone events (ACCELERATING class achieved, plateau broken)
    Use: XP and achievement calculations

  PARENT_INTELLIGENCE_AGENT:
    Consumes: velocity_narrative.parent_facing_summary, regression_alert (summarized)
    Use: Child progress reports (parent dashboard)

  INSTITUTE_ANALYTICS_AGENT:
    Consumes: Cohort LVV distributions, curriculum_effectiveness_score
    Use: Institute curriculum health reports, cohort benchmarking

  CREATOR_EFFECTIVENESS_AGENT:
    Consumes: curriculum_effectiveness_score per content piece per creator
    Use: Creator performance analytics, content quality ranking

  HIRING_MATCH_AGENT:
    Consumes: velocity_class, difficulty_climb_rate_score, career_readiness_velocity_percentile
    Use: Velocity-adjusted talent signal for recruiter candidate surfacing

  TALENT_MARKETPLACE_AGENT:
    Consumes: velocity_composite_score, predicted_belt_readiness_date confidence
    Use: Velocity-adjusted certification confidence seal

  CHAMPIONSHIP_SEEDING_AGENT:
    Consumes: difficulty_climb_rate_score, velocity_class
    Use: Fair championship bracket seeding

  CURRICULUM_REVIEW_AGENT:
    Consumes: curriculum_effectiveness_score when below threshold
    Use: Flags content for editorial review (DOJO T6 compliant)

  OBSERVABILITY_AGENT:
    Consumes: All performance metrics, drift alerts, anomaly events
    Use: LVMA health dashboards

  GOVERNANCE_AGENT:
    Consumes: Critical regression escalations, belt gate blocks, model drift alerts
    Use: Compliance oversight

EVENT_TRIGGERS:

  INPUT_TRIGGERS (events that cause LVMA to execute):
    ASSESSMENT_COMPLETE             → Full LVV update for affected skill
    BELT_PROMOTION_GATE             → Full LVV + confidence gate check (P0)
    RETENTION_CHECK_COMPLETE        → Retention axis update + regression check
    SESSION_COMPLETE                → Partial LVV update (session-level features)
    IDLE_GAP_EXCEEDED               → Regression risk recompute (threshold: 14 days)
    SCHEDULED_REFRESH               → Full 30d LVV refresh for all entities
    AI_MENTOR_REQUEST               → Real-time ADAPTIVE_CONTENT_SIGNAL (P0)
    CHAMPIONSHIP_PREP_START         → Full LVV + difficulty seeding signal
    INSTITUTE_REPORT_REQUEST        → Cohort LVV + curriculum effectiveness
    PARENT_REPORT_REQUEST           → Entity LVV with parent_facing_summary
    EXTERNAL_LMS_SYNC_COMPLETE      → Update external learning features
    CURRICULUM_VERSION_UPDATED      → Recompute curriculum_effectiveness_score

  OUTPUT_EVENTS (events LVMA emits):
    LVV_UPDATED_EVENT               → Kafka (lvma.lvv.results) after every computation
    VELOCITY_CLASS_CHANGED          → Emitted when class transitions
    REGRESSION_ALERT_EVENT          → Emitted to AI_MENTOR + PARENT + EDUCATOR
    BELT_GATE_SIGNAL_EVENT          → Emitted to BELT_ENGINE (ALLOW or BLOCK)
    PLATEAU_DETECTED_EVENT          → Emitted to AI_MENTOR_SYSTEM for intervention
    CURRICULUM_EFFECTIVENESS_BELOW_THRESHOLD → Emitted to CURRICULUM_REVIEW_AGENT
    VELOCITY_MILESTONE_EVENT        → Emitted to RANK_UPDATE_AGENT (XP trigger)
    LVMA_MODEL_DRIFT_ALERT          → Emitted to OBSERVABILITY_AGENT
    ACCELERATION_MILESTONE_ACHIEVED → Emitted to GROWTH_ENGINE (share trigger)
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

LVMA both **consumes from** and **emits to** the Feature Store.

**Consume from Feature Store:**
```json
CONSUME_FEATURE_VECTOR: {
  "entity_id"     : "UUID",
  "feature_name"  : "e.g. prior_lvv_acquisition_velocity_score",
  "feature_value" : "float",
  "timestamp"     : "ISO 8601",
  "source_agent"  : "SCORING_ENGINE | BELT_ENGINE | LVMA | etc."
}
```

**Complete Emission List to Feature Store (per entity per skill per window):**

```
acquisition_velocity_score
retention_durability_score
transfer_efficiency_score
regression_risk_score
plateau_detection_score
difficulty_climb_rate_score
metacognitive_signal_score
velocity_composite_score
velocity_class_encoded               (ACCELERATING=4, STEADY=3, PLATEAUING=2, REGRESSING=1, STALLED=0)
predicted_belt_readiness_days        (days from now — integer)
learning_potential_ceiling_score
retention_half_life_days
challenge_avoidance_index
career_readiness_velocity_percentile
curriculum_effectiveness_score       (EDUCATOR/COHORT only)
```

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY

LVMA does not directly manage idea vectors. However, when `trigger_event = CHAMPIONSHIP_PREP_START` or `entity_type = PROFESSIONAL` working in innovation contexts:

```json
EMIT_VELOCITY_SIGNAL_TO_IDEA_DNA: {
  "entity_id"                     : "UUID",
  "metacognitive_signal_score"    : "float — proxy for idea origination quality",
  "transfer_efficiency_score"     : "float — ability to apply learning across domains",
  "difficulty_climb_rate_score"   : "float — readiness for complex innovation challenges",
  "velocity_class"                : "enum",
  "audit_reference"               : "UUID",
  "source_agent"                  : "LEARNING_VELOCITY_MODEL_AGENT"
}
```

**Use by IDEA_DNA_AGENT:**
- `metacognitive_signal_score` contributes to idea originality confidence (self-directed learners produce more original ideas)
- `transfer_efficiency_score` contributes to cross-domain idea quality assessment
- Signals are advisory only — IDEA_DNA_AGENT makes all originality decisions

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

```yaml
GROWTH_ENGINE_TRIGGERS:

  VELOCITY_CLASS_UPGRADE_MILESTONE:
    CONDITION: velocity_class transitions from STALLED/REGRESSING → STEADY or higher
    EMIT: XP_UPDATE_EVENT
      payload:
        entity_id: UUID
        xp_action: "VELOCITY_CLASS_RECOVERY"
        prior_class: enum
        new_class: enum
        audit_reference: UUID
    RECIPIENT: RANK_UPDATE_AGENT

  ACCELERATION_MILESTONE:
    CONDITION: velocity_class = ACCELERATING for first time in 90d window
    EMIT: SHARE_TRIGGER_EVENT
      payload:
        entity_id: UUID
        share_type: "LEARNING_ACCELERATION_MILESTONE"
        velocity_composite_score: integer
        domain_track: enum
        audit_reference: UUID
    RECIPIENT: SOCIAL_ENGINE → generates shareable velocity achievement card

  RETENTION_EXCELLENCE:
    CONDITION: retention_durability_score >= 0.90 sustained over 30d
    EMIT: XP_UPDATE_EVENT
      payload:
        entity_id: UUID
        xp_action: "RETENTION_EXCELLENCE_RECOGNIZED"
        retention_durability_score: float
        audit_reference: UUID
    RECIPIENT: RANK_UPDATE_AGENT

  DIFFICULTY_CLIMB_ACHIEVEMENT:
    CONDITION: difficulty_climb_rate_score >= 0.85 AND new difficulty tier reached
    EMIT: RANK_UPDATE_EVENT + XP_UPDATE_EVENT
      payload:
        entity_id: UUID
        xp_action: "DIFFICULTY_FRONTIER_REACHED"
        new_difficulty_tier: integer
        audit_reference: UUID
    RECIPIENT: RANK_UPDATE_AGENT + CHAMPIONSHIP_SEEDING_AGENT (re-seed signal)

  STREAK_VELOCITY_BONUS:
    CONDITION: ACCELERATING class maintained for 7+ consecutive days
    EMIT: XP_UPDATE_EVENT
      payload:
        entity_id: UUID
        xp_action: "SUSTAINED_ACCELERATION_STREAK"
        streak_days: integer
        audit_reference: UUID
    RECIPIENT: RANK_UPDATE_AGENT

THIS AGENT DOES NOT:
  - Compute XP values directly — delegated to RANK_UPDATE_AGENT
  - Award badges directly — delegated to ACHIEVEMENT_ENGINE
  - Post to social channels — delegated to SOCIAL_ENGINE
  - Adjust leaderboard positions — delegated to RANK_UPDATE_AGENT
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

```yaml
METRICS_EMITTED_TO_OBSERVABILITY_AGENT:

  Success Rate:
    metric: lvma_request_success_rate
    type: gauge (%)
    alert_threshold: < 98.5% over 5-minute window → CRITICAL

  Error Rate:
    metric: lvma_request_error_rate
    type: gauge (%)
    alert_threshold: > 1.5% → WARNING | > 5.0% → CRITICAL + ON_CALL

  Latency Percentiles:
    metric: lvma_computation_latency_ms
    types: p50, p95, p99
    alert_thresholds:
      p95 > 800ms   → WARNING
      p99 > 1,800ms → CRITICAL

  P0 Lane Latency (Belt Gate + AI Mentor):
    metric: lvma_p0_latency_ms
    alert_threshold: p95 > 450ms → CRITICAL (user-impacting)

  LLM Fallback Rate:
    metric: lvma_llm_fallback_rate
    type: gauge (%)
    alert_threshold: > 15% over 1h → WARNING

  Regression Alert Rate:
    metric: lvma_regression_alert_rate
    type: gauge (per 1,000 entities)
    alert: Sudden spike > 3x rolling baseline → GOVERNANCE_AGENT investigation

  Belt Gate Block Rate:
    metric: lvma_belt_gate_block_rate
    type: gauge (%)
    alert: > 30% blocks in any 1h window → MODEL_REVIEW (possible data quality issue)

  Forecasting Calibration Coverage:
    metric: lvma_forecast_coverage_probability
    type: gauge (%)
    target: ≥ 70% for 80% credible intervals
    alert: < 65% → FORECASTING_MODEL_REVIEW_EVENT

  Feature Drift PSI:
    metric: lvma_psi_per_feature
    type: gauge (PSI per key feature)
    alert: PSI > 0.20 on any primary feature → MODEL_REVIEW_EVENT

  Confidence Score Distribution:
    metric: lvma_confidence_distribution
    type: histogram (bucketed)
    alert: > 30% requests with confidence < 0.45 → MODEL_REVIEW

  Insufficient Data Rate:
    metric: lvma_insufficient_data_rate
    type: gauge (%)
    monitor: Trend — high rate indicates learners not being assessed frequently enough
    alert: > 25% over any 24h window → CURRICULUM_REVIEW_AGENT notification

  Curriculum Effectiveness Coverage:
    metric: lvma_curriculum_tracks_above_100_learners
    type: gauge (count)
    monitor: Ensures enough tracks qualify for Stage 6 scoring

  Cache Hit Rate:
    metric: lvma_redis_cache_hit_rate
    type: gauge (%)
    target: > 60% for scheduled refreshes, > 40% for real-time

  Queue Depth per Lane:
    metric: lvma_queue_depth_p0_p1_p2_p3
    alert:
      P0 queue > 0 for > 20 seconds → CRITICAL
      P1 queue > 1,000 → WARNING
      P2 queue > 10,000 → WARNING

REQUIRED_DASHBOARDS (in OBSERVABILITY_AGENT):
  - LVMA Real-time Performance Dashboard
  - LVMA Velocity Class Distribution (by domain, by tenant — anonymized)
  - LVMA Regression Alert Frequency Dashboard
  - LVMA Forecasting Calibration Dashboard
  - LVMA Model Drift Dashboard
  - LVMA Belt Gate Decision Dashboard (allow vs block ratio)
  - LVMA Curriculum Effectiveness Coverage Dashboard
  - LVMA Queue Health Dashboard (all 4 lanes)
  - LVMA LLM Health Dashboard (fallback rate + latency)
```

---

## 1️⃣5️⃣ VERSIONING POLICY

```yaml
VERSION_FORMAT: MAJOR.MINOR.PATCH
  MAJOR: Output schema field removed/renamed, LVV axis removed, velocity_class
         threshold changed, forecasting model architecture changed,
         security model changed
  MINOR: New LVV axis added, new feature added, new entity_type supported,
         new trigger_event supported, new prompt version, Stage 6 curriculum
         model updated
  PATCH: Bug fix, calibration adjustment, prompt refinement, feature weight tweak

VERSIONED_ARTIFACTS:
  LVMA-ML-vX.Y.Z                → Core ML ensemble
  LVMA-PROMPT-vX.Y.Z            → Global LLM prompt
  LVMA-PROMPT-PARENT-vX.Y       → Parent-facing prompt
  LVMA-PROMPT-EDUCATOR-vX.Y     → Educator-facing prompt
  LVMA-PROMPT-{DOMAIN}-vX.Y     → Domain-specific prompts (5 domains)
  LVMA-FEATURES-vX.Y            → Feature schema
  LVMA-WEIGHTS-{DOMAIN}-vX.Y    → Domain velocity compositor weight matrices
  LVMA-FORECAST-vX.Y            → Forecasting model config

MUTATION_RULES:
  LVV axes: ADD-ONLY — never remove or rename
  Velocity class values: ADD-ONLY — never remove existing values
  Output schema fields: ADD-ONLY — never remove or rename existing fields
  Velocity class thresholds: IMMUTABLE without MAJOR version + GOVERNANCE approval
  Forgetting curve parameters: IMMUTABLE per entity — re-estimated online, not batch-reset

PROMOTION_PROCESS:
  Shadow mode: 21 days minimum (longer than other agents — forecasting requires observation)
  Shadow comparison: LVV composite deviation < 5% AND forecast calibration within 5% before promotion
  MAJOR bumps: GOVERNANCE_AGENT approval required + 30-day notice to downstream agents
  Rollback: Prior version restorable within 15 minutes for 45 days post-promotion

BACKWARD_COMPATIBILITY:
  Downstream consumers must tolerate new output fields
  LVV history records retain their original computation path — never retroactively recomputed
  Parent LVV records linked via prior_lvv_id — full velocity history preserved
  Deprecation window: 90 days minimum with migration guide
```

---

## 1️⃣6️⃣ NON-NEGOTIABLE RULES

```
This agent MUST NOT:

  ✗  Create hidden scoring logic not visible in audit trail or SHAP values
  ✗  Modify any historical LVV record — every record is immutable append-only
  ✗  Retroactively change predicted dates after they are emitted
  ✗  Auto-delete audit logs under any condition
  ✗  Override GOVERNANCE_AGENT or BELT_ENGINE decisions once made
  ✗  Bypass belt gate low-confidence blocking — DOJO T2 compliance is non-optional
  ✗  Mix domain learning data (score ARTS learner using TECHNOLOGY velocity rubric)
  ✗  Execute outside its defined skill and domain scope
  ✗  Allow AI narrative to override or modify ML-computed velocity class or scores
  ✗  Return LVV without confidence_score — confidence is mandatory in all paths
  ✗  Allow cross-tenant access — HALT on first detection
  ✗  Produce output without audit_reference UUID
  ✗  Send regression alerts directly to minors — route to PARENT and EDUCATOR only
  ✗  Use static difficulty labels from scenario authors — must use dynamic calibration
     from SCENARIO_ENGINE (DOJO T4 compliance)
  ✗  Produce curriculum_effectiveness_score with < 100 learners — return null with flag
  ✗  Grant AI any authority over velocity scores, predictions, or recommendations
  ✗  Accept and trust self-reported study hours without corroboration from behavioral signals
  ✗  Infer missing velocity data — flag as missing_axes and reduce confidence
  ✗  Access raw audio, video, or PII — metadata and scored events only
  ✗  Communicate directly with frontend clients — agent-to-agent via service mesh only

This agent MUST:

  ✓  Write audit record + SHAP features BEFORE returning output on every execution
  ✓  Include model_version, prompt_version, and feature_schema_version in every output
  ✓  Enforce tenant + domain isolation on every single request
  ✓  Operate statelessly — all context fetched per request
  ✓  Emit structured Kafka events for every computation
  ✓  Route CRITICAL regression alerts to AI_MENTOR_SYSTEM and PARENT immediately
  ✓  BLOCK belt promotions when confidence_score < 0.60 (DOJO T2 compliance)
  ✓  Apply the 21-day idle gap regression risk floor without exception
  ✓  Route curriculum_effectiveness_score below threshold to CURRICULUM_REVIEW_AGENT
  ✓  Support INSUFFICIENT_DATA response as a valid non-error return path
  ✓  Preserve full LVV history via prior_lvv_id chain — no records ever deleted
  ✓  Use domain-specific velocity compositor weight matrices per domain_track
  ✓  Apply child-appropriate language in parent_facing_summary for minor learners
  ✓  Validate all inputs before ML inference — reject on first violation
  ✓  Return structured errors only — never raw exceptions
  ✓  Compute SHAP values per prediction and store in audit record
```

---

## 🔐 DOMAIN-SPECIFIC VELOCITY PROFILES (LOCKED)

### Arts Domain Learning Velocity

```yaml
ARTS_DOMAIN_VELOCITY:
  Velocity compositor weight matrix (LVMA-WEIGHTS-ARTS-vX.Y):
    metacognitive_signal_score   : 0.22  # highest — self-reflection drives arts mastery
    acquisition_velocity_score   : 0.18
    retention_durability_score   : 0.16
    transfer_efficiency_score    : 0.18  # high — arts requires contextual adaptation
    difficulty_climb_rate_score  : 0.12
    plateau_detection_score      : 0.08  # inverted in composite — plateau = risk
    regression_risk_score        : 0.06  # inverted in composite — risk = drag

  Domain-specific velocity signals:
    Positive:
      - Iterative improvement on critique-feedback cycles
      - Portfolio piece quality growth over time
      - Cross-media or cross-genre skill transfer evidence
      - Voluntary challenge seeking in Dojo Arts track
      - Peer recognition events (cited work in group sessions)
    Risk signals:
      - Repetitive work in same style without expansion
      - Mentor feedback not integrated in subsequent submissions
      - Abandonment of difficulty tier after single failure

  Retention model variant: Arts skills decay faster than declarative knowledge
    Retention half-life calibration: shorter baseline (adjusted quarterly per data)
```

### Commerce Domain Learning Velocity

```yaml
COMMERCE_DOMAIN_VELOCITY:
  Velocity compositor weight matrix (LVMA-WEIGHTS-COMMERCE-vX.Y):
    transfer_efficiency_score    : 0.22  # highest — commerce = applying principles to new contexts
    acquisition_velocity_score   : 0.18
    retention_durability_score   : 0.18
    difficulty_climb_rate_score  : 0.16
    metacognitive_signal_score   : 0.14
    plateau_detection_score      : 0.07
    regression_risk_score        : 0.05

  Domain-specific velocity signals:
    Positive:
      - Real CRM/Sales tool integration data aligning with assessed scores
      - Cross-scenario performance consistency (negotiation, case analysis, pitch)
      - Measurable improvement in scenario outcome metrics over time
    Risk signals:
      - High assessment scores without EIE integration evidence (unverified)
      - Narrow scenario type performance (only comfortable in one format)
```

### Science Domain Learning Velocity

```yaml
SCIENCE_DOMAIN_VELOCITY:
  Velocity compositor weight matrix (LVMA-WEIGHTS-SCIENCE-vX.Y):
    retention_durability_score   : 0.24  # highest — science builds on prior knowledge
    acquisition_velocity_score   : 0.20
    difficulty_climb_rate_score  : 0.18
    transfer_efficiency_score    : 0.16
    metacognitive_signal_score   : 0.12
    plateau_detection_score      : 0.06
    regression_risk_score        : 0.04

  Domain-specific velocity signals:
    Positive:
      - Foundation concept retention strong (enables higher-tier learning)
      - Lab/simulation milestone delivery quality improving
      - Conceptual question quality in mentor sessions
    Risk signals:
      - Foundation gaps blocking advanced content (score plateau at tier 3–4)
      - High forgetting rate on quantitative skills

  Retention model variant: Science — higher retention half-life for conceptual understanding,
    shorter for procedural calculation skills (separate half-life per skill sub-type)
```

### Technology Domain Learning Velocity

```yaml
TECHNOLOGY_DOMAIN_VELOCITY:
  Velocity compositor weight matrix (LVMA-WEIGHTS-TECHNOLOGY-vX.Y):
    difficulty_climb_rate_score  : 0.24  # highest — tech = progressive complexity mastery
    acquisition_velocity_score   : 0.20
    transfer_efficiency_score    : 0.18
    retention_durability_score   : 0.16
    metacognitive_signal_score   : 0.12
    plateau_detection_score      : 0.06
    regression_risk_score        : 0.04

  Domain-specific velocity signals:
    Positive:
      - GitHub commit complexity growth over time
      - Jira ticket resolution rate and milestone delivery quality
      - Assessment score alignment with live code quality (EIE)
      - New technology adoption rate (GitHub language diversity)
      - Code review participation increasing over time
    Risk signals:
      - Assessment scores high without any GitHub/Jira integration evidence
      - Difficulty tier stagnation despite positive assessment scores
      - Narrowing language/framework diversity (comfort zone shrinking)
```

### Administration Domain Learning Velocity

```yaml
ADMINISTRATION_DOMAIN_VELOCITY:
  Velocity compositor weight matrix (LVMA-WEIGHTS-ADMINISTRATION-vX.Y):
    metacognitive_signal_score   : 0.20
    retention_durability_score   : 0.20
    transfer_efficiency_score    : 0.18
    acquisition_velocity_score   : 0.18
    difficulty_climb_rate_score  : 0.14
    plateau_detection_score      : 0.06
    regression_risk_score        : 0.04

  Domain-specific velocity signals:
    Positive:
      - Policy comprehension and application accuracy improving
      - Scenario escalation protocol execution quality
      - Cross-functional collaboration simulation performance
    Risk signals:
      - Procedural memory decay on compliance-critical scenarios
      - Narrow improvement limited to familiar scenario templates
```

---

## 🔐 BELT PROMOTION GATE PROTOCOL (DOJO T2 COMPLIANT)

```yaml
BELT_GATE_DECISION_RULES:
  TRIGGER: BELT_PROMOTION_GATE event from BELT_ENGINE

  REQUIRED_FOR_ALLOW:
    confidence_score              : >= 0.60 (DOJO T2: "Low confidence cannot trigger promotion")
    velocity_class                : STEADY or better (ACCELERATING, STEADY)
    regression_risk_score         : < 0.50
    retention_durability_score    : >= 0.55
    difficulty_climb_rate_score   : >= 0.50 (must have succeeded at belt-appropriate difficulty tier)
    minimum_assessment_events     : >= 10 in velocity_window

  CONDITIONAL_ALLOW (requires mentor board review):
    confidence_score              : 0.45–0.59 → MENTOR_BOARD_REVIEW required
    velocity_class                : PLATEAUING → mentor must confirm readiness
    retention_durability_score    : 0.45–0.54 → mentor retention assessment required

  BLOCK (hard block — no exceptions):
    confidence_score              : < 0.45
    velocity_class                : REGRESSING or STALLED
    regression_risk_score         : >= 0.75
    data_sparse_flag              : true with < 5 events in window

  GATE_SIGNAL_OUTPUT:
    gate_decision                 : enum: ALLOW | CONDITIONAL_ALLOW | BLOCK
    gate_reason                   : structured code + human-readable explanation
    blocking_axes                 : list of LVV axes that caused block
    remediation_recommendation    : structured path to resolve block
    audit_reference               : UUID — linked to Belt Engine promotion audit

  COMPLIANCE_NOTE:
    This gate protocol implements DOJO T2 (Scoring Validity & Reliability Lock).
    "Low confidence scores cannot trigger belt promotion without mentor board review."
    Mentor board review required for CONDITIONAL_ALLOW cases — not for ALLOW or BLOCK.
```

---

## 🔐 LEARNING REGRESSION RECOVERY PROTOCOL

```yaml
REGRESSION_RECOVERY_RECOMMENDATIONS:
  Severity: MILD (regression_risk_score 0.40–0.59):
    recommended_content_type: RETENTION_CHECK
    pacing: SLOW_DOWN
    intervention: AI_MENTOR_SYSTEM generates spaced repetition plan
    urgency_signal: LOW
    parent_alert: false

  Severity: MODERATE (regression_risk_score 0.60–0.74):
    recommended_content_type: DRILL (focused on decayed skill sub-areas)
    pacing: PAUSE_AND_CONSOLIDATE
    intervention: MENTOR_SESSION recommended
    urgency_signal: MEDIUM
    parent_alert: true (if minor entity)

  Severity: SEVERE (regression_risk_score 0.75–0.84 OR retention delta < -0.20):
    recommended_content_type: THEORY + DRILL sequence
    pacing: PAUSE_AND_CONSOLIDATE
    intervention: MENTOR_SESSION (mandatory before next belt attempt)
    urgency_signal: HIGH
    parent_alert: true (all entity types)
    belt_gate_impact: BELT_PROMOTION blocked until recovery confirmed

  Severity: CRITICAL (regression_risk_score >= 0.85 OR retention delta < -0.35):
    recommended_content_type: FULL_TRACK_RESTART for affected skill sub-areas
    pacing: PAUSE_AND_CONSOLIDATE
    intervention: MENTOR_SESSION (immediate) + curriculum pathway reassessment
    urgency_signal: CRITICAL
    parent_alert: true (all entity types) + EDUCATOR_ALERT
    belt_gate_impact: BELT_PROMOTION blocked + existing belt under review flag
    escalation: GOVERNANCE_AGENT if 3+ CRITICAL regression events in 30d
```

---

## 🔐 FINAL GOVERNANCE SEAL

```
╔══════════════════════════════════════════════════════════════════════════════╗
║  LEARNING VELOCITY MODEL AGENT — PRODUCTION SEAL                            ║
║                                                                              ║
║  AGENT ID          : LVMA-001                                               ║
║  VERSION           : v1.0.0                                                 ║
║  PLATFORM          : ECOSKILLER ANTIGRAVITY                                 ║
║  CLASSIFICATION    : INTELLIGENCE-CRITICAL + CURRICULUM-GOVERNANCE          ║
║                                                                              ║
║  ✅  ML-Primary Computation (76%)                                           ║
║      LightGBM Axis Scoring (7 models) + Stacking Velocity Compositor +      ║
║      Prophet Forecasting + BSTS + Isolation Forest Regression Detector +    ║
║      Causal Inference Curriculum Scorer (6-stage pipeline)                  ║
║  ✅  AI-Assist Narrative (24%) — 9 versioned prompts (global +              ║
║      domain-specific + parent + educator), zero decision authority          ║
║  ✅  7-Axis Learning Velocity Vector — Immutable axis definitions           ║
║  ✅  76 ML Features — Full temporal feature registry                        ║
║  ✅  5 Domain Velocity Profiles — Domain-specific compositor weight matrices║
║  ✅  Probabilistic Forecasting — Belt readiness, plateau, regression dates  ║
║  ✅  Ebbinghaus Forgetting Curve — Per-entity retention modeling            ║
║  ✅  Belt Promotion Gate Protocol — DOJO T2 compliant                       ║
║  ✅  Learning Regression Recovery Protocol — 4 severity levels              ║
║  ✅  Curriculum Effectiveness Scoring — Causal inference model              ║
║  ✅  SHAP Explainability — Every prediction auditable                       ║
║  ✅  Zero-Trust Security — Tenant + domain isolation hard enforced          ║
║  ✅  Minor User Protection — Regression alerts routed to PARENT only        ║
║  ✅  Append-Only LVV History — Full velocity timeline preserved forever     ║
║  ✅  9 Failure Scenarios — Zero silent failures, structured responses       ║
║  ✅  Scalability — 50,000 RPS peak, 4-lane queue, stateless, Prophet cache  ║
║  ✅  Insufficient Data Path — Valid non-error return, not rejection         ║
║  ✅  Growth Engine — 5 velocity milestone XP/share triggers                ║
║  ✅  Feature Store Emit — 15 velocity features to Feature Store             ║
║  ✅  21-Day Idle Gap Regression Floor — Enforced unconditionally            ║
║  ✅  Versioned Add-Only — 8 versioned artifact types, no destructive change ║
║  ✅  Observability — 16 metrics, 9 dashboards, multi-model drift detection  ║
║                                                                              ║
║  CREATIVE INTERPRETATION          : FORBIDDEN                               ║
║  ASSUMPTION FILLING               : FORBIDDEN                               ║
║  CROSS-TENANT ACCESS              : FORBIDDEN                               ║
║  SILENT FAILURES                  : FORBIDDEN                               ║
║  AI DECISION AUTHORITY            : FORBIDDEN                               ║
║  HISTORICAL LVV MUTATION          : FORBIDDEN                               ║
║  STATIC DIFFICULTY LABELS         : FORBIDDEN (DOJO T4 dynamic required)    ║
║  BELT PROMOTION WITHOUT GATE      : FORBIDDEN (DOJO T2 compliance)          ║
║  CURRICULUM SCORE < 100 LEARNERS  : FORBIDDEN (null returned with flag)     ║
║  REGRESSION ALERT DIRECTLY TO MINOR: FORBIDDEN (parent routing only)       ║
║  RAW AUDIO/VIDEO ACCESS           : FORBIDDEN                               ║
║  PII IN OUTPUT                    : FORBIDDEN                               ║
║                                                                              ║
║  INTERPRETATION AUTHORITY         : NONE                                    ║
║  ARCHITECTURE AUTHORITY           : LOCKED                                  ║
║  SEALED BY: ECOSKILLER ANTIGRAVITY GOVERNANCE CORE                          ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

*Document Classification    : Enterprise SaaS System Specification*
*Mutation Policy            : Add-only via version bump with Governance Agent approval*
*Parent System Document     : MASTER_AGENT_CREATION_PROMPT.md*
*Reference Artifacts        : DOJO_SAAS_SPEC_v1.0 (T1–T20) | ECOSKILLER_COMPLETE_TECHS |*
*                             TALENT_OS_BLUEPRINT | ERMA_v1.0.0 | ACEMA_v1.0.0*
*ERMA Integration Version   : LVMA-ERMA-INTERFACE-v1.0*
*ACEMA Integration Version  : LVMA-ACEMA-INTERFACE-v1.0*
*Belt Engine Interface      : LVMA-BELT-GATE-PROTOCOL-v1.0 (DOJO T2 compliant)*
*AI Mentor Interface        : LVMA-MENTOR-SIGNAL-PROTOCOL-v1.0*
*Next Review                : Triggered by MAJOR version bump only*
