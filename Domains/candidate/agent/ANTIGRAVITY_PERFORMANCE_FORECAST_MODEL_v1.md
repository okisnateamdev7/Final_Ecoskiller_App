# 🔒 ANTIGRAVITY — PERFORMANCE FORECAST MODEL
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### SEALED PRODUCTION ARTIFACT SPEC v1.0

---

```
Artifact Class:         Production AI Inference System
System Parent:          ECOSKILLER + DOJO SAAS Unified Platform
Artifact Name:          ANTIGRAVITY Performance Forecast Engine (APFE)
Sibling Artifacts:      ANTIGRAVITY Salary Prediction Engine     (ASPE-v1.0.0)
                        ANTIGRAVITY Retention Probability Engine  (ARPE-v1.0.0)
Mutation Policy:        Add-only via version bump
Execution Mode:         Deterministic · Contract-Gated · Event-Driven
Interpretation Auth:    NONE
Architecture Authority: LOCKED
Trust Seal:             ACTIVE
Version:                APFE-v1.0.0
Issued Under:           ECOSKILLER Master Execution Prompt v12.0
                        Dojo SaaS Production Artifact Spec v1.0
                        Talent Operating System Blueprint v1.0
                        R28 — Intelligence Cost Optimization Law (ENFORCED)
```

---

> **EXECUTION DECREE**
> This document is a sealed production prompt artifact for the ANTIGRAVITY Performance Forecast Engine operating within the ECOSKILLER Enterprise Optimization + Trust Infrastructure. It is the third pillar of the ANTIGRAVITY intelligence triad alongside ASPE and ARPE. No section may be reinterpreted, restructured, or modified without a declared version bump. All subsystems described herein are mandatory. Absence of any subsystem → STOP EXECUTION → REPORT APFE-INCOMPLETE → NO DEPLOYMENT CLAIM PERMITTED.

---

## 🔒 SECTION A — SYSTEM IDENTITY & SCOPE LOCK

```
Model Name:             ANTIGRAVITY Performance Forecast Engine (APFE)
Module Parent:          ECOSKILLER Recruiter System — Module 7
                          Feature #11: Candidate Predictions
                          Feature #14: Performance Predictions
                          Feature #15: Reliability Scores
                          Feature #21: Digital Twin Testing
                          Feature #22: Simulation Hiring
                          Feature #23: Team Hiring AI
                          Feature #24: Hiring AI Agents
                          Feature #25: Hiring Forecasts
                        Enterprise HR Replacement Layer — Pillar 19
                          (Performance Tracking · Promotion Prediction · Skill Tracking)
Infrastructure:         Enterprise Optimization + Trust Infrastructure Layer
Trust Class:            Tier-1 Verified Behavioral Forecast (Recruiter + HR + Manager Facing)
Prediction Scope:       Individual · Role-Fit · Team Composition · Promotion Readiness ·
                        Skill Ceiling · Peak Performance Window · Reliability Band
Forecast Horizons:      30-day · 90-day · 180-day · 365-day · Role-Tenure
Confidence Gate:        Minimum 0.76 confidence score required for employer-facing output
Failure Mode:           STOP → REPORT → NO PARTIAL FORECAST EMITTED
Triad Dependencies:     ASPE-v1.0.0 (compensation context signal — required)
                        ARPE-v1.0.0 (retention context signal — required)
                        Both must be operational before APFE-v1.0.0 deployment
```

### Mission Statement

ANTIGRAVITY Performance Forecast Engine eliminates gut-feel from hiring, promotion, and workforce planning decisions. It synthesizes verified Dojo skill trajectories, intelligence measurement vectors, real work output data from 100 integrated tools, championship performance history, learning velocity, behavioral reliability signals, peer and mentor assessment data, role-fit intelligence scores, compensation alignment from ASPE, and retention stability from ARPE — into auditable, multi-horizon performance forecasts per candidate and per employee.

**This is NOT a subjective manager rating system.**
**This is NOT a static competency framework checklist.**
**This IS a live, multi-signal, multi-horizon AI performance forecasting engine tied to verified behavioral evidence — deterministic, explainable, and trust-anchored.**

**Primary Value Propositions:**
- Recruiters know *before hire* how a candidate will perform at 30 / 90 / 180 / 365 days.
- HR knows *before promotion* whether an employee is ready to perform at the next level.
- Team Managers know *before restructuring* which team composition maximises collective performance.
- Hiring AI Agents run Digital Twin simulations before offers are extended.

---

## 🔒 SECTION B — TRUST PREREQUISITE LOCK

APFE CANNOT execute unless all trust prerequisites are satisfied. Trust prerequisites are enforced by the ECOSKILLER Trust Infrastructure (Module 15) and the ANTIGRAVITY Trust Framework.

### Mandatory Trust Gates (All Required — None Optional)

| Gate ID | Trust Signal | Required Status | Source System |
|---------|-------------|-----------------|---------------|
| TG-01 | Candidate / Employee Identity Verified | `verified_status = TRUE` | ECOSKILLER Universal ID / ProfessionalID |
| TG-02 | Requesting Entity Verified | `requester_entity_verified = TRUE` | R37 Company Verification OR Recruiter Verification |
| TG-03 | Requester Role Authorized | `role ∈ [recruiter_verified, hr_admin, company_admin, manager]` | ECOSKILLER RBAC Engine |
| TG-04 | No Active Fraud Flag | `fraud_flag = FALSE` | Module 15 Fraud Detection |
| TG-05 | No Active Fake Profile Flag | `fake_profile_flag = FALSE` | Module 15 Fake Profile Detection |
| TG-06 | No Active Collusion Flag | `collusion_flag = FALSE` | Dojo Collusion Detection (Section T9) |
| TG-07 | Dojo Authenticity Seal Present | `dojo.replay_authenticity_seal = TRUE` | Dojo Analytics Engine — Talent Marketplace Trust Lock (T14) |
| TG-08 | Minimum Signal Coverage | `signal_coverage_score ≥ 0.45` | APFE Signal Coverage Calculator (min 3 of 9 signal blocks populated) |
| TG-09 | Subject Consent Granted | `performance_forecast_consent = TRUE` | APFE Consent Registry — timestamped + audited |
| TG-10 | Minimum Dojo Match Count | `dojo.match_count ≥ 3` | Dojo Match Engine — minimum evidence threshold |

**If ANY gate is NOT satisfied → STOP FORECAST → EMIT TRUST_GATE_FAILURE report → Log to AuditLog (immutable) → No employer-facing output emitted.**

---

## 🔒 SECTION C — DATA INPUT SIGNAL REGISTRY (SEALED)

All signals are read-only to the inference engine. No signal may be mutated by APFE. All signals consumed from ECOSKILLER event streams, Dojo analytics, and the ECOSKILLER Integration Engine.

---

### C1 — Dojo Skill Performance Signal Block

```
Source:             Dojo SaaS Engine (ECOSKILLER Module 3 — Skill Dojo System)
Access Mode:        Analytics Engine event-stream read + Replay Engine data
Freshness Policy:   Max 48-hour staleness
Weight Class:       VERY HIGH — primary verified skill performance signal

Signals:
  dojo.belt_level                           → Enum: WHITE|YELLOW|GREEN|BLUE|BROWN|BLACK
  dojo.belt_version                         → Versioned rubric tag (Section T17)
  dojo.match_count_total                    → Integer: all ranked matches completed
  dojo.match_count_last_30d                 → Integer: matches in last 30 days
  dojo.avg_score_all_time                   → Float: career average match score
  dojo.avg_score_last_10                    → Float: recent-form score average
  dojo.avg_score_last_30d                   → Float: last 30 days score average
  dojo.score_trajectory_90d                 → Float: slope of score trend over 90 days
                                              (positive = improving, negative = declining)
  dojo.score_trajectory_180d                → Float: slope over 180 days
  dojo.score_peak                           → Float: all-time peak score achieved
  dojo.score_peak_recency_days              → Integer: days since peak score (recency of best form)
  dojo.score_variance                       → Float: score consistency measure
  dojo.score_confidence                     → Float [0–1]: inter-rater reliability composite
  dojo.peer_score_variance                  → Float: spread across peer evaluators
  dojo.mentor_override_rate                 → Float: % matches where mentor corrected peer score
  dojo.mentor_override_direction            → Float: net direction of overrides (upward bias / downward)
  dojo.pressure_scenario_pass_rate          → Float: % high-pressure scenarios passed
  dojo.pressure_scenario_pass_rate_30d      → Float: recent pressure performance
  dojo.pressure_scenario_score_delta        → Float: pressure vs non-pressure score gap (resilience signal)
  dojo.skill_track_completion_rate          → Float: % enrolled tracks completed
  dojo.skill_track_mastery_rate             → Float: % tracks completed at mastery threshold
  dojo.skill_delta_pre_post                 → Float: score improvement from track start to completion
  dojo.skill_delta_trend_90d               → Float: improvement velocity over 90 days
  dojo.drill_accuracy_rate                  → Float: correct drill responses / total drill attempts
  dojo.drill_first_attempt_accuracy         → Float: accuracy on first attempt (no retry)
  dojo.scenario_difficulty_avg              → Float: avg difficulty of attempted scenarios (data-derived T4)
  dojo.scenario_difficulty_ceiling          → Float: highest difficulty consistently passed (ceiling signal)
  dojo.scenario_difficulty_trend            → Float: trend in attempted difficulty level
  dojo.belt_progression_velocity            → Float: belt levels per 6-month window
  dojo.belt_stall_duration_days             → Integer: days since last belt advancement
  dojo.certification_earned_count           → Integer: verified certifications
  dojo.replay_authenticity_seal             → Boolean: replay evidence verified (T14)
  dojo.skill_vector                         → Array[Float]: multi-dimensional skill embedding (512-dim)
  dojo.rater_calibration_compliance         → Boolean: evaluated by calibration-compliant mentors (T3)
  dojo.tournament_win_rate                  → Float: tournament wins / tournaments entered
  dojo.tournament_performance_trend         → Float: win rate improvement over time
```

**Forecast Signal Interpretation:**
- `dojo.score_trajectory_90d > 0.05` = active improvement trend → performance ceiling not yet reached
- `dojo.score_trajectory_90d < -0.03` = declining form → investigate cause before forecast
- `dojo.pressure_scenario_score_delta < -15` = poor performance under pressure → high-stakes role risk
- `dojo.scenario_difficulty_ceiling` is the primary **skill ceiling estimator**
- `dojo.drill_first_attempt_accuracy > 0.80` = strong independent problem-solving signal
- `dojo.belt_stall_duration_days > 90` AND `dojo.score_trajectory_90d > 0.0` = approaching natural ceiling
- `dojo.mentor_override_direction > 0` = consistently underscored by peers → humility indicator / team culture fit signal

---

### C2 — Intelligence Measurement Signal Block

```
Source:             ECOSKILLER Intelligence Measurement System (Module 2 — EIMS)
                    4-Layer measurement: Psychometric + Behavioral + Real Task + Longitudinal
Access Mode:        Cached inference result — max 21-day staleness
Weight Class:       VERY HIGH — foundational capability signal

Signals:
  intel.radar_scores                        → Object: per-intelligence-type score (8 dimensions)
    intel.logical_score                     → Float [0–1000]: logical-mathematical intelligence
    intel.linguistic_score                  → Float [0–1000]: linguistic intelligence
    intel.spatial_score                     → Float [0–1000]: spatial intelligence
    intel.interpersonal_score               → Float [0–1000]: interpersonal intelligence
    intel.intrapersonal_score               → Float [0–1000]: intrapersonal / self-awareness
    intel.naturalistic_score                → Float [0–1000]: naturalistic intelligence
    intel.musical_score                     → Float [0–1000]: musical intelligence
    intel.kinesthetic_score                 → Float [0–1000]: kinesthetic intelligence
  intel.dominant_intelligence_type          → String: highest scoring intelligence dimension
  intel.secondary_intelligence_type         → String: second highest scoring dimension
  intel.global_percentile_rank              → Float [0–100]: global intelligence percentile
  intel.confidence_score                    → Float [0–1]: test reliability and consistency score
  intel.growth_delta_90d                    → Float: intelligence score change over 90 days
  intel.growth_delta_180d                   → Float: intelligence score change over 180 days
  intel.learning_speed_score                → Float: normalized learning velocity
  intel.focus_measurement                   → Float: focus and sustained attention score
  intel.decision_quality_score              → Float: decision analysis score under constraint
  intel.decision_quality_trend              → Float: improving or declining decision quality
  intel.creativity_score                    → Float: AI-assessed creative problem-solving
  intel.reliability_score                   → Float [0–1]: consistency across repeat assessments
  intel.career_intelligence_map             → Array[String]: AI-predicted career aptitude zones
  intel.role_fit_intelligence_score         → Float [0–1]: alignment of intelligence profile
                                              to requested role's intelligence demand model
  intel.role_fit_intelligence_trend         → Float: fit improving or declining over time
  intel.adaptive_test_ceiling               → Float: highest adaptive test difficulty consistently passed
  intel.behavioral_intelligence_signals     → Object: behavior-derived intelligence indicators
    intel.competition_decision_quality      → Float: decision quality in championship scenarios
    intel.collaboration_leadership_score    → Float: leadership emergence in team challenges
    intel.deadline_reliability_score        → Float: consistency of output quality under time pressure
```

**Forecast Signal Interpretation:**
- `intel.role_fit_intelligence_score < 0.50` = fundamental capability mismatch for target role
- `intel.learning_speed_score > 0.80` = fast-learner modifier → 90-day ramp faster than baseline
- `intel.decision_quality_trend > 0` = improving executive function → promotion readiness signal
- `intel.deadline_reliability_score > 0.85` = critical for high-delivery roles (engineering, sales, ops)
- `intel.adaptive_test_ceiling` feeds directly into skill ceiling estimation in Layer 3

---

### C3 — Real Work Output Signal Block (Integration Engine)

```
Source:             ECOSKILLER Integration Engine (EIE) — Module 9
                    100-Tool Integration Layer: OAuth + Webhooks + AI Normalization
                    Universal Work Data Format (UWDF) — AI normalized output
Access Mode:        UWDF event stream
Freshness Policy:   Max 7-day staleness for active integrations
Weight Class:       VERY HIGH — most direct evidence of real-world performance

Signals per tool category (UWDF normalized):

  work.github_signals (Developer Tools):
    output_velocity_30d                   → Float: merged PRs + commits per active week (last 30d)
    output_velocity_90d                   → Float: 90-day average
    output_velocity_trend                 → Float: improvement slope
    code_quality_score                    → Float: AI-assessed code quality composite
    code_quality_trend                    → Float: improving or declining
    test_coverage_contribution            → Float: tests written per feature delivered
    architecture_contribution_score       → Float: AI-detected architectural impact of contributions
    review_quality_score                  → Float: quality of code reviews given
    cross_repo_breadth                    → Float: diversity of technical domains contributed to
    bug_introduction_rate                 → Float: bugs per feature (inverse quality signal)
    bug_introduction_trend                → Float: improving or worsening

  work.jira_signals (Project Management):
    delivery_rate                         → Float: % committed tickets delivered on time
    delivery_rate_trend                   → Float: improving or declining
    estimation_accuracy                   → Float: estimated vs actual effort accuracy
    initiative_score                      → Float: self-initiated tickets / assigned tickets
    cross_team_contribution               → Float: contributions to other teams' epics
    complexity_handling_score             → Float: avg story points of completed tickets

  work.salesforce_signals (CRM — Sales Performance):
    quota_attainment_avg                  → Float: % quota achieved (last 3 periods)
    quota_attainment_trend                → Float: improving or declining
    pipeline_creation_rate                → Float: new opportunities created per period
    deal_velocity_avg                     → Float: avg days to close a deal
    deal_velocity_trend                   → Float: faster or slower closing
    forecast_accuracy                     → Float: predicted pipeline vs actual close rate
    customer_retention_rate               → Float: accounts retained / accounts managed
    upsell_expansion_rate                 → Float: revenue expansion in existing accounts

  work.figma_signals (Design Performance):
    design_output_rate                    → Float: screens / components delivered per week
    design_iteration_quality              → Float: approved-on-first-review rate
    cross_functional_collaboration        → Float: design handoff quality score
    design_system_contribution            → Float: components added to shared design system

  work.slack_signals (Communication Performance):
    async_quality_score                   → Float: AI-assessed quality of written communication
    response_reliability_rate             → Float: % responses within SLA
    knowledge_sharing_score               → Float: informational messages sent to channels
    conflict_resolution_signals           → Float: AI-detected constructive resolution patterns

  work.hrms_signals (Workday / BambooHR / Darwinbox / Keka / SAP / ADP):
    performance_review_avg                → Float: avg rating across last 3 formal reviews
    performance_review_trend              → Float: improving or declining per review cycle
    performance_review_count              → Integer: number of formal reviews completed
    peer_feedback_score_avg               → Float: 360 peer feedback composite
    peer_feedback_trend                   → Float: improving or declining
    manager_feedback_score_avg            → Float: direct manager performance rating
    manager_feedback_trend                → Float: improving or declining
    promotion_history_count               → Integer: total promotions received
    promotion_velocity                    → Float: avg months between promotions
    onboarding_completion_speed_days      → Integer: days to complete onboarding (new hire signal)
    l_and_d_completion_rate               → Float: L&D programs completed / assigned
    training_effectiveness_score          → Float: post-training performance delta (if tracked)
    goal_attainment_rate                  → Float: declared OKRs/KPIs met / total declared
    goal_attainment_trend                 → Float: improving or declining

  work.normalized_reliability_score       → Float [0–1]: AI cross-signal reliability composite
  work.performance_extraction_vector      → Array[Float]: AI-extracted performance embedding (UWDF output)
  work.output_intensity_score             → Float: composite of volume + quality + speed signals
  work.output_intensity_trend             → Float: trend direction over 90 days
```

**Forecast Signal Interpretation:**
- `work.github.output_velocity_trend > 0.15` = accelerating developer — strong 90-day trajectory
- `work.salesforce.quota_attainment_trend > 0` AND `work.salesforce.forecast_accuracy > 0.80` = reliable sales performer
- `work.hrms.goal_attainment_rate > 0.85` = KPI disciplined → dependable output signal
- `work.hrms.performance_review_trend > 0` + `work.hrms.peer_feedback_trend > 0` = multi-rater improving consensus → promotion readiness
- `work.hrms.onboarding_completion_speed_days` feeds directly into 30-day ramp forecast
- `work.normalized_reliability_score` is the master trust weight for this entire block

---

### C4 — Championship & Competition Performance Signal Block

```
Source:             ECOSKILLER Championship System (Module 4)
Access Mode:        Event log read
Freshness Policy:   Max 14-day staleness
Weight Class:       HIGH — proven high-pressure performance signal

Signals:
  champ.tier_reached_highest                → Enum: SCHOOL|DISTRICT|STATE|NATIONAL|CONTINENTAL|WORLD
  champ.tier_reached_recent                 → Enum: most recent championship tier
  champ.win_rate_all_time                   → Float: wins / competitions entered
  champ.win_rate_last_3                     → Float: recent form win rate
  champ.win_rate_trend                      → Float: improving or declining
  champ.score_percentile_avg                → Float: avg placement percentile across competitions
  champ.score_under_pressure_avg            → Float: performance score in elimination rounds specifically
  champ.team_competition_win_rate           → Float: win rate in team format competitions
  champ.individual_competition_win_rate     → Float: win rate in individual format
  champ.competition_frequency_90d           → Integer: competitions entered in 90 days
  champ.ai_evaluation_score_avg             → Float: avg AI judge composite score
  champ.ai_evaluation_trend                 → Float: AI-assessed performance trend
  champ.cross_skill_competition_count       → Integer: competitions across multiple skill domains
  champ.highest_rank_achieved               → Integer: best competitive rank number (lower = better)
  champ.comeback_rate                       → Float: % of deficit positions reversed in elimination rounds
                                              (resilience and pressure performance signal)
```

**Forecast Signal Interpretation:**
- `champ.tier_reached_highest = NATIONAL|CONTINENTAL|WORLD` = verified elite-level performance signal
- `champ.score_under_pressure_avg > champ.avg_score_all_time` = performs better under pressure
- `champ.comeback_rate > 0.40` = high resilience and pressure adaptability
- `champ.team_competition_win_rate >> champ.individual_competition_win_rate` = team amplifier role
- `champ.individual_competition_win_rate >> champ.team_competition_win_rate` = independent performer role

---

### C5 — Learning Velocity & Growth Trajectory Signal Block

```
Source:             ECOSKILLER Intelligence System (Module 2) + Dojo Skill Engine (Module 3)
                    Student Habit & Retention System (R95)
Access Mode:        Multi-system composite — max 48-hour staleness
Weight Class:       HIGH — forward-looking performance trajectory signal

Signals:
  growth.skill_acquisition_speed            → Float: avg time to reach YELLOW belt per new skill domain
  growth.skill_transfer_rate                → Float: performance gain in adjacent skill domains
                                              after mastering primary domain (transfer learning)
  growth.learning_curve_slope               → Float: score improvement rate per 10 matches
                                              in first 30 matches of a new skill
  growth.plateau_duration_avg               → Float: avg days spent in plateau before breakthrough
  growth.plateau_breakthrough_rate          → Float: % plateaus eventually broken through
  growth.concept_mastery_speed              → Float: avg sessions to achieve scenario difficulty ceiling
  growth.error_recovery_rate                → Float: performance recovery speed after a bad match
  growth.error_recovery_trend               → Float: improving or declining recovery speed
  growth.self_correction_rate               → Float: % errors self-identified before mentor feedback
  growth.mentor_feedback_absorption         → Float: score improvement per mentor feedback session
  growth.knowledge_retention_score          → Float: retention test scores vs initial learning scores
                                              (from Dojo retention check matches — Section T6)
  growth.regression_recovery_count          → Integer: times skill regression reversed (perseverance signal)
  growth.daily_learning_days_90d            → Integer: distinct active learning days in 90 days
  growth.daily_learning_consistency         → Float: regularity coefficient of learning activity
  growth.challenge_attempt_rate             → Float: % available challenges attempted
  growth.challenge_completion_rate          → Float: % attempted challenges completed
  growth.stretch_goal_attempt_rate          → Float: % of attempts at scenarios above current belt difficulty
```

**Forecast Signal Interpretation:**
- `growth.skill_acquisition_speed` + `growth.learning_curve_slope` = primary **onboarding ramp predictor**
- `growth.plateau_breakthrough_rate > 0.75` = high grit and persistence → long-tenure high performer signal
- `growth.error_recovery_rate` feeds directly into 30-day performance ramp confidence
- `growth.stretch_goal_attempt_rate > 0.60` = proactive capability growth → ceiling not self-limiting
- `growth.mentor_feedback_absorption > 0.80` = high coaching potential → manageable and developable

---

### C6 — Behavioral Reliability Signal Block

```
Source:             Dojo Scoring Engine + Platform Habit System (R95) + EIE HRMS signals
Access Mode:        Multi-source composite — max 72-hour staleness
Weight Class:       HIGH — predicts consistency and dependability of output

Signals:
  reliability.match_commitment_rate         → Float: % of registered matches actually completed
  reliability.match_abandonment_rate        → Float: % of started matches abandoned
  reliability.scoring_consistency_score     → Float: intra-subject score variance (low = consistent)
  reliability.deadline_adherence_rate       → Float: work deliverables on time (from HRMS + Jira)
  reliability.deadline_adherence_trend      → Float: improving or declining
  reliability.commitment_follow_through     → Float: % pledged tasks completed (cross-signal)
  reliability.attendance_consistency        → Float: scheduled activity attendance rate
  reliability.communication_reliability     → Float: % requests responded to within SLA
  reliability.estimation_reliability        → Float: declared effort vs actual effort accuracy
  reliability.streak_max_days               → Integer: lifetime max platform activity streak
  reliability.streak_current_days           → Integer: current streak
  reliability.streak_consistency_score      → Float: streak regularity — not just length
  reliability.challenge_completion_rate     → Float: % challenges completed once started
  reliability.platform_reliability_score    → Float [0–1]: ECOSKILLER composite reliability signal
                                              (cross-validated against platform behavior + work data)
```

**Forecast Signal Interpretation:**
- `reliability.match_abandonment_rate > 0.20` = unreliable under adversity → high-stakes role risk
- `reliability.deadline_adherence_rate > 0.90` = consistently delivers → core performance predictor
- `reliability.scoring_consistency_score` is the primary **performance variance predictor**
- Low variance + high average = reliable high performer
- High average + high variance = volatile performer (risk in client-facing or deadline-critical roles)
- `reliability.platform_reliability_score` is the master gate for Reliability Band output

---

### C7 — Role Intelligence Fit Signal Block

```
Source:             ECOSKILLER Intelligence System (EIMS) + Role Intelligence Demand Model
                    Role demand models are pre-built for standard role categories and are versioned
Access Mode:        Computed on-demand using intel.radar_scores + role_demand_vector
Freshness Policy:   Max 24-hour staleness (role demand models refreshed weekly)
Weight Class:       VERY HIGH — determines role-specific performance ceiling

Signals:
  role_fit.target_role_id                   → String: role being forecast against
  role_fit.role_intelligence_demand_vector  → Array[Float]: weighted intelligence requirements
                                              for the target role (from Role Intelligence Demand Model)
  role_fit.intelligence_fit_score           → Float [0–1]: dot product match of
                                              candidate intel.radar_scores vs role demand vector
  role_fit.intelligence_fit_gap_vector      → Array[{dimension, gap_pct}]: per-dimension gap analysis
  role_fit.dominant_fit_dimensions          → Array[String]: candidate's strongest matching intel types
  role_fit.critical_gap_dimensions          → Array[String]: intel dimensions where gap > 25%
  role_fit.role_complexity_tier             → Enum: ENTRY|MID|SENIOR|LEAD|EXECUTIVE
  role_fit.role_pressure_intensity          → Float [0–1]: how pressure-intensive the role is
  role_fit.role_collaboration_intensity     → Float [0–1]: collaboration requirement of the role
  role_fit.role_independence_score          → Float [0–1]: independent execution requirement
  role_fit.skill_domain_match_score         → Float [0–1]: Dojo skill vector vs role skill requirement
  role_fit.dojo_scenario_role_match         → Float [0–1]: similarity of completed scenarios
                                              to the scenarios typical of the target role
  role_fit.historical_role_performance_avg  → Float: avg performance of past candidates
                                              with similar profiles in the same role
                                              (anonymized cohort benchmark from ECOSKILLER marketplace)
```

**Forecast Signal Interpretation:**
- `role_fit.intelligence_fit_score < 0.50` → performance ceiling in this role is LOW → forecast suppressed with LOW_FIT warning
- `role_fit.critical_gap_dimensions.length > 2` → multiple critical gaps → forecast requires caveat
- `role_fit.role_pressure_intensity > 0.70` AND `dojo.pressure_scenario_score_delta < -15` → high risk of underperformance under role pressure
- `role_fit.dojo_scenario_role_match > 0.80` → candidate has been tested in scenarios matching this role → highest confidence forecast

---

### C8 — Compensation Alignment Signal Block (ASPE Integration)

```
Source:             ANTIGRAVITY Salary Prediction Engine (ASPE-v1.0.0) — required dependency
Access Mode:        ASPE SalaryPrediction object — read only
Freshness Policy:   Max 30-day staleness
Weight Class:       MEDIUM — compensation alignment affects motivation and performance trajectory

Signals:
  comp.market_gap_pct                       → Float: current / offered CTC vs ASPE p50 (from ARPE C4)
  comp.belt_premium_alignment               → Float: whether offered CTC reflects belt premium
  comp.compensation_satisfaction_proxy      → Float: inverse of market_gap_pct (higher gap = lower satisfaction)
  comp.unvested_equity_value                → Float: retention anchor affecting motivation horizon
  comp.equity_cliff_months                  → Integer: from ARPE signal — performance motivation window
  comp.last_raise_market_lag_pct            → Float: last raise vs market YoY growth delta
  comp.total_comp_vs_role_benchmark         → Float: total comp vs ECOSKILLER role benchmark for role_id
  comp.aspe_confidence_flag                 → String: HIGH | MODERATE (inherited confidence)
```

**Forecast Signal Interpretation:**
- `comp.market_gap_pct > 20%` = compensation dissatisfaction likely → performance motivation risk
- `comp.belt_premium_alignment = FALSE` = unrecognized performer → motivation gap affects output quality
- `comp.equity_cliff_months ≤ 3` = peak performance window before equity depletion (may inflate short-term output)
- `comp.total_comp_vs_role_benchmark < 0.85` = role under-compensation → sustained underperformance risk

---

### C9 — Retention Stability Signal Block (ARPE Integration)

```
Source:             ANTIGRAVITY Retention Probability Engine (ARPE-v1.0.0) — required dependency
Access Mode:        ARPE RetentionPrediction object — read only
Freshness Policy:   Max 14-day staleness
Weight Class:       HIGH — retention probability directly modifies performance forecast horizon

Signals:
  retention.retention_probability_score     → Float [0–100]: from ARPE (if subject is existing employee)
  retention.risk_tier                       → Enum: STABLE|LOW_RISK|WATCH|AT_RISK|HIGH_RISK|CRITICAL
  retention.flight_window_upper_days        → Integer: from ARPE — bounds performance forecast horizon
  retention.top_root_causes                 → Array[String]: ARPE root cause codes (RC-01 to RC-12)
  retention.manager_instability_flag        → Boolean: RC-05 active
  retention.team_instability_flag           → Boolean: RC-06 active
  retention.burnout_signal_flag             → Boolean: RC-07 active (burnout detected)
  retention.disengagement_trend_flag        → Boolean: RC-08 active
  retention.confidence_flag                 → String: HIGH | MODERATE | SUPPRESSED
```

**Forecast Signal Interpretation (Employee Mode Only):**
- `retention.risk_tier = CRITICAL` → forecast horizon capped at `retention.flight_window_upper_days`
- `retention.burnout_signal_flag = TRUE` → performance forecast suppressed for next 30d; burnout modifier applied
- `retention.disengagement_trend_flag = TRUE` → performance trajectory modifier: multiply by 0.80
- `retention.team_instability_flag = TRUE` → team composition performance forecast adjusted downward
- For **candidate mode (pre-hire)**: ARPE not yet available → this block is zero-weighted for new candidates

---

## 🔒 SECTION D — PREDICTION MODEL ARCHITECTURE (SEALED)

### D1 — Model Class Declaration

```
Model Type:             Gradient Boosted Ensemble (per R28 — Intelligence Cost Optimization Law)
                        Traditional ML — NOT LLM for scoring layer
                        LLM used ONLY for: explainability text + improvement narrative generation
Sub-Models:
  Sub-Model A:          30-Day Performance Ramp Regressor
  Sub-Model B:          90-Day Performance Stabilization Regressor
  Sub-Model C:          180-Day Performance Ceiling Classifier
  Sub-Model D:          365-Day High Performance Sustainability Classifier
  Sub-Model E:          Promotion Readiness Classifier (ENTRY→MID, MID→SENIOR, SENIOR→LEAD, LEAD→EXEC)
  Sub-Model F:          Reliability Band Regressor (performance variance estimator)
  Sub-Model G:          Skill Ceiling Estimator (role-specific maximum performance predictor)
  Sub-Model H:          Team Composition Performance Optimizer (team-level forecast)

Output:                 PerformanceForecast object (Section F)
Model Cost:             Traditional ML inference — estimated < $0.003 per prediction
LLM Layer Cost:         Explainability only — estimated < $0.012 per forecast report
Training Data:          Anonymized historical performance outcomes from ECOSKILLER marketplace
                        + HRMS historical performance review data
                        + Championship performance outcomes
                        + Post-hire performance tracking from enterprise clients
Retraining Policy:      Monthly — Governance Board authorization + test gate pass required
```

---

### D2 — Layer 1: Signal Normalization + Staleness Weighting

```
Normalization Rules:
  Float signals:          Min-max normalization per signal historical distribution
  Integer signals:        Log-scaled normalization for skewed count distributions
  Enum signals:           Ordinal encoding per defined hierarchy
  Trend signals:          Standardized to [-2.0, +2.0] range (wider to preserve gradient signals)
  Vector signals:         L2-normalized embeddings (dojo.skill_vector, work.performance_extraction_vector)
  Boolean flags:          Binary penalty applied (0 = FALSE, -1 = TRUE for risk flags)

Staleness Decay:
  w_decay = max(0.05, 1 - (staleness_hours / max_staleness_hours))
  Zero-coverage blocks: weight = 0.0 (not penalized — just excluded)

Signal Coverage Score:
  coverage = (populated_blocks / 9 total blocks)
  TG-08 minimum: ≥ 0.45 (minimum 3 blocks with non-zero signals)
```

---

### D3 — Layer 2: Trust-Weighted Feature Composition

```
Trust Weight Assignment Per Block:
  C1 Dojo signals:          weight = dojo.score_confidence × dojo.rater_calibration_compliance_binary
  C2 Intelligence signals:  weight = intel.confidence_score × intel.reliability_score
  C3 Work signals:          weight = work.normalized_reliability_score
  C4 Championship signals:  weight = 1.0 if champ.ai_evaluation_score_avg ≥ 0.70 else 0.65
  C5 Growth signals:        weight = 0.90 (platform-computed — high internal consistency)
  C6 Reliability signals:   weight = reliability.platform_reliability_score
  C7 Role Fit signals:      weight = 1.0 (deterministic model-computed — no external trust dependency)
  C8 Compensation signals:  weight = comp.aspe_confidence_flag == "HIGH" ? 1.0 : 0.75
  C9 Retention signals:     weight = retention.confidence_flag == "HIGH" ? 1.0 :
                                     retention.confidence_flag == "MODERATE" ? 0.75 : 0.0

  Unified trust-weighted feature vector TF:
    TF = Σ (normalized_signal_block_i × trust_weight_i)
    TF → input to sub-models D4 through D11
```

---

### D4 — Sub-Model A: 30-Day Performance Ramp Forecast

```
Purpose:        Predict performance score at 30 days in role
Target:         Normalized performance score [0–100] at 30-day mark
Key Drivers:    growth.learning_curve_slope, intel.learning_speed_score,
                work.hrms.onboarding_completion_speed_days,
                dojo.drill_first_attempt_accuracy,
                growth.error_recovery_rate,
                role_fit.intelligence_fit_score

Output:
  ramp_30d.predicted_score          → Float [0–100]
  ramp_30d.confidence_interval      → Object { lower: Float, upper: Float }
  ramp_30d.ramp_speed_tier          → Enum: FAST | STANDARD | SLOW | AT_RISK
  ramp_30d.key_accelerators         → Array[String]: top 3 signals driving fast ramp
  ramp_30d.key_blockers             → Array[String]: top 3 signals risking slow ramp

Ramp Speed Tiers:
  FAST:       Predicted score ≥ 75 at 30 days (above role median)
  STANDARD:   Predicted score 55–74 at 30 days (at role median)
  SLOW:       Predicted score 35–54 at 30 days (below role median — needs support)
  AT_RISK:    Predicted score < 35 at 30 days (significant onboarding risk)
```

---

### D5 — Sub-Model B: 90-Day Performance Stabilization Forecast

```
Purpose:        Predict sustained performance level at 90-day stabilization point
Target:         Normalized performance score [0–100] at 90-day mark (role median = 50)
Key Drivers:    All C1–C7 signals weighted
                Additional: dojo.score_trajectory_90d, growth.plateau_breakthrough_rate,
                work.output_intensity_trend, intel.role_fit_intelligence_score

Output:
  stable_90d.predicted_score        → Float [0–100]
  stable_90d.confidence_interval    → Object { lower: Float, upper: Float }
  stable_90d.performance_tier       → Enum: EXCEPTIONAL|HIGH|SOLID|DEVELOPING|UNDERPERFORMING
  stable_90d.trajectory             → Enum: ASCENDING|STABLE|PLATEAUING|DECLINING

Performance Tiers at 90 Days (Role-Normalized):
  EXCEPTIONAL:    Score ≥ 85 (top 10% of role cohort)
  HIGH:           Score 70–84 (top 25% of role cohort)
  SOLID:          Score 55–69 (above role median)
  DEVELOPING:     Score 40–54 (below role median — coachable)
  UNDERPERFORMING:Score < 40 (significant intervention required)
```

---

### D6 — Sub-Model C: 180-Day Performance Ceiling Forecast

```
Purpose:        Predict the performance ceiling the candidate will reach in this role
                within 180 days — how high can they go?
Target:         Maximum achievable normalized score [0–100] in 180 days in target role
Key Drivers:    dojo.scenario_difficulty_ceiling, intel.adaptive_test_ceiling,
                growth.stretch_goal_attempt_rate, growth.plateau_breakthrough_rate,
                role_fit.intelligence_fit_score, intel.learning_speed_score

Output:
  ceiling_180d.predicted_ceiling    → Float [0–100]
  ceiling_180d.ceiling_tier         → Enum: ELITE|HIGH|MODERATE|LIMITED
  ceiling_180d.ceiling_limiters     → Array[String]: top signals constraining the ceiling
  ceiling_180d.ceiling_expanders    → Array[String]: top signals that could raise the ceiling

Ceiling Tiers:
  ELITE:          Ceiling ≥ 90 (role-transforming level — future leader material)
  HIGH:           Ceiling 75–89 (strong top-quartile performer)
  MODERATE:       Ceiling 55–74 (solid mid-level contributor)
  LIMITED:        Ceiling < 55 (structural role mismatch — reassignment may be needed)
```

---

### D7 — Sub-Model D: 365-Day High Performance Sustainability Forecast

```
Purpose:        Predict whether high performance will be sustained over a full year
Target:         Binary + probability: will the candidate maintain performance_tier ≥ HIGH
                for 10 of 12 months in the first year?
Key Drivers:    reliability.platform_reliability_score, dojo.score_variance,
                growth.daily_learning_consistency, intel.intrapersonal_score,
                champ.comeback_rate, retention.retention_probability_score (ARPE),
                comp.total_comp_vs_role_benchmark

Output:
  sustained_365d.probability        → Float [0–1]: probability of sustained high performance
  sustained_365d.sustainability_tier→ Enum: VERY_LIKELY|LIKELY|UNCERTAIN|UNLIKELY
  sustained_365d.risk_factors       → Array[String]: top risks to sustainability
  sustained_365d.anchor_factors     → Array[String]: top signals supporting sustainability
  sustained_365d.horizon_cap_days   → Integer: forecast horizon capped by ARPE retention window
                                      (if employee is AT_RISK or higher retention tier)
```

---

### D8 — Sub-Model E: Promotion Readiness Classifier

```
Purpose:        Predict whether a candidate / employee is ready for promotion
                to the next role complexity tier within 90 / 180 / 365 days
Target:         Multi-class: READY_NOW | READY_90D | READY_180D | READY_365D | NOT_YET
Key Drivers:    dojo.belt_progression_velocity, intel.decision_quality_trend,
                work.hrms.performance_review_trend, work.hrms.peer_feedback_trend,
                intel.role_fit_intelligence_score (for target tier role),
                growth.stretch_goal_attempt_rate, champ.tier_reached_highest,
                work.hrms.goal_attainment_rate, intel.interpersonal_score (for lead+)

Output:
  promotion_readiness.verdict               → Enum: READY_NOW|READY_90D|READY_180D|READY_365D|NOT_YET
  promotion_readiness.target_tier           → Enum: MID|SENIOR|LEAD|EXECUTIVE
  promotion_readiness.confidence            → Float [0–1]
  promotion_readiness.readiness_score       → Float [0–100]: composite readiness index
  promotion_readiness.gap_areas             → Array[{dimension, gap_description, development_action}]
  promotion_readiness.accelerators          → Array[String]: actions that would accelerate readiness
  promotion_readiness.dojo_belt_required    → Enum: minimum belt required for target tier promotion
                                              (BLUE for SENIOR, BROWN for LEAD, BLACK for EXECUTIVE)
                                              [Configurable per company — default values provided]

Belt-Promotion Coupling Rule (Sealed):
  Promotion to SENIOR requires:    dojo.belt_level ≥ BLUE (configurable)
  Promotion to LEAD requires:      dojo.belt_level ≥ BROWN (configurable)
  Promotion to EXECUTIVE requires: dojo.belt_level ≥ BLACK (configurable)
  Belt requirement may be overridden by governance board with audit log entry.
```

---

### D9 — Sub-Model F: Reliability Band Regressor

```
Purpose:        Forecast the performance variance band — how consistent will output be?
Target:         Performance standard deviation estimate [low variance = reliable]
Key Drivers:    reliability.scoring_consistency_score, dojo.score_variance,
                reliability.deadline_adherence_rate, intel.intrapersonal_score,
                growth.error_recovery_rate, champ.comeback_rate

Output:
  reliability_band.variance_score           → Float [0–1]: predicted output variance (0 = perfectly consistent)
  reliability_band.reliability_tier         → Enum: ROCK_SOLID|RELIABLE|VARIABLE|VOLATILE
  reliability_band.role_risk_assessment     → String: risk level for target role's tolerance for variance
  reliability_band.variance_triggers        → Array[String]: signals that drive variance up

Reliability Tiers:
  ROCK_SOLID:   Variance ≤ 0.08 (rare — near-perfect consistency)
  RELIABLE:     Variance 0.09–0.18 (dependable — suits client-facing, deadline-critical roles)
  VARIABLE:     Variance 0.19–0.30 (output fluctuates — needs structured management)
  VOLATILE:     Variance > 0.30 (high inconsistency risk — not suited for high-stakes delivery roles)
```

---

### D10 — Sub-Model G: Skill Ceiling Estimator

```
Purpose:        Forecast the maximum skill level achievable in this role domain
                within the next 12 months of active development
Target:         Predicted belt ceiling achievable in 12 months [ordinal 1–7]
Key Drivers:    dojo.belt_progression_velocity, growth.skill_acquisition_speed,
                intel.learning_speed_score, dojo.scenario_difficulty_ceiling,
                intel.adaptive_test_ceiling, growth.plateau_breakthrough_rate

Output:
  skill_ceiling.predicted_belt_12m          → Enum: WHITE|YELLOW|GREEN|BLUE|BROWN|BLACK
  skill_ceiling.predicted_belt_confidence   → Float [0–1]
  skill_ceiling.ceiling_limiters            → Array[String]: signals capping the ceiling
  skill_ceiling.development_acceleration    → Array[String]: actions to raise the ceiling
  skill_ceiling.time_to_next_belt_days      → Integer: estimated days to next belt level
  skill_ceiling.time_to_next_belt_range     → Object { min: Integer, max: Integer }
```

---

### D11 — Sub-Model H: Team Composition Performance Optimizer (Digital Twin Mode)

```
Purpose:        Forecast collective team performance given a proposed team composition.
                Enables Digital Twin Testing (Module 7, Feature #21) and
                Team Hiring AI (Module 7, Feature #23).
Input:          Array of candidate_ids + proposed role assignments + team_id (optional existing team)
Target:         Predicted team performance composite [0–100] + optimal composition recommendation

Key Drivers:    Aggregate of individual APFE outputs + team_fit_vectors
                + intelligence diversity score (teams with diverse intelligence profiles outperform)
                + dojo.interpersonal_score cross-member compatibility
                + skill complementarity matrix (overlap vs coverage)
                + communication style compatibility (from Slack / social signals)

Output:
  team_forecast.predicted_team_score        → Float [0–100]
  team_forecast.team_performance_tier       → Enum: EXCEPTIONAL|HIGH|SOLID|DEVELOPING|IMBALANCED
  team_forecast.composition_strengths       → Array[String]: collective strengths
  team_forecast.composition_gaps            → Array[String]: team-level gaps
  team_forecast.optimal_composition         → Array[{candidate_id, role_id, fit_rationale}]
  team_forecast.risk_compositions           → Array[{conflict_type, member_ids, mitigation}]
  team_forecast.intelligence_diversity_score→ Float [0–1]: team intelligence profile diversity
  team_forecast.skill_coverage_score        → Float [0–1]: % of required role skills covered by team
  team_forecast.digital_twin_simulation_id  → UUID: links this forecast to a simulation run record

Digital Twin Simulation:
  All team composition forecasts are stored as simulation runs.
  Multiple compositions can be compared before hire decision.
  Simulation stored in: PerformanceSimulationLog (append-only)
  Access: Recruiter JWT | HR Admin JWT | Company Admin JWT
```

---

### D12 — Layer 6: Confidence Calibration

```
Confidence Score Formula:
  confidence = α × signal_coverage_score
             + β × dojo.score_confidence
             + γ × work.normalized_reliability_score
             + δ × intel.confidence_score
             + ε × intel.reliability_score
             + ζ × role_fit.intelligence_fit_score

  Where α + β + γ + δ + ε + ζ = 1.0
  Weights defined in versioned model config — not mutable without MAJOR version bump

Confidence Gate Enforcement:
  confidence < 0.76  → ALL sub-models SUPPRESSED
                        Emit: PREDICTION_LOW_CONFIDENCE
                        Route to: HR Review Queue
                        No employer-facing output emitted

  0.76 ≤ confidence < 0.88 → confidence_flag = MODERATE
                              Output emitted with MODERATE disclaimer
                              Recruiter advised to supplement with interview data

  confidence ≥ 0.88  → confidence_flag = HIGH
                        Full forecast output emitted
                        No disclaimer required

Per-Sub-Model Confidence:
  Each sub-model emits its own confidence score alongside its output.
  Sub-model confidence < 0.70 → that sub-model output flagged as LOW_CONFIDENCE individually.
  Low-confidence sub-models displayed with visual caveat in UI.
  They are NOT suppressed individually — only the full forecast is suppressed if overall < 0.76.
```

---

### D13 — Layer 7: Performance Explainability + Improvement Path Generator

```
Model:              LLM (Claude Sonnet — ECOSKILLER API integration, per R28 cost scope)
                    Used ONLY for this layer — cost-justified for output quality
Input:              All sub-model outputs + signal summaries + role_fit gap vector
Output:             PerformanceExplainability object

Per Forecast Output:
  explainability.top_performance_drivers    → Array[String]: top 5 signals driving performance up
  explainability.top_performance_risks      → Array[String]: top 5 signals suppressing performance
  explainability.ramp_narrative             → String: plain-language 30-day ramp prediction story
  explainability.ceiling_narrative          → String: plain-language skill ceiling explanation
  explainability.reliability_narrative      → String: plain-language variance explanation
  explainability.signal_contribution_map    → Object: per-block % contribution to overall forecast
  explainability.recruiter_brief            → String: 3-sentence recruiter-ready summary
  explainability.hiring_manager_brief       → String: 3-sentence manager onboarding brief
  explainability.improvement_path           → Array[{action, expected_impact, owner, timeline}]
                                              (top 5 actions to improve performance forecast)
  explainability.development_plan_outline   → String: 90-day suggested development focus areas
  explainability.digital_twin_comparison    → String | null: (present in Sub-Model H outputs only)
                                              narrative comparing team composition options

LLM Validation:
  Every LLM output validated against signal data before inclusion.
  LLM cannot generate claims unsupported by signal evidence.
  LLM output length: recruiter_brief ≤ 3 sentences, hiring_manager_brief ≤ 3 sentences.
  improvement_path: maximum 5 actions, each ≤ 2 sentences.
```

---

## 🔒 SECTION E — PREDICTION VARIANT MATRIX (SEALED)

| Mode ID | Mode Name | Scope | Primary Consumer | Min Trust Gates |
|---------|-----------|-------|-----------------|-----------------|
| PM-01 | Pre-Hire Candidate Forecast | Single candidate + target role | Recruiter Dashboard | TG-01 to TG-10 |
| PM-02 | Existing Employee Forecast | Single employee + current role | HR Admin + Manager | TG-01 to TG-10 |
| PM-03 | Promotion Readiness Assessment | Single employee + target tier | HR Admin + Manager | TG-01 to TG-09 |
| PM-04 | Team Composition Optimizer | Candidate pool + proposed team | Recruiter + HR Admin | TG-01 to TG-09 |
| PM-05 | Digital Twin Simulation | Multiple compositions compared | Recruiter + Company Admin | TG-01 to TG-09 |
| PM-06 | Skill Ceiling Audit | Single employee — development planning | HR Admin + Manager | TG-01 to TG-07 |
| PM-07 | Bulk Role Cohort Forecast | All employees in same role | Company Admin + CHRO | TG-02 to TG-05 |
| PM-08 | Hiring Forecast | Aggregate performance outlook for planned hires | CFO + Talent Ops | TG-02 to TG-04 |
| PM-09 | Reliability Bench Report | Team reliability variance analysis | Manager + HR Admin | TG-02 to TG-05 |

**PM-01 and PM-02 are the base modes. All other modes are aggregations or extensions of base outputs.**

---

## 🔒 SECTION F — OUTPUT SCHEMA (SEALED)

```json
PerformanceForecast {
  forecast_id:                    UUID,
  model_version:                  "APFE-v1.0.0",
  subject_id:                     UUID (verified candidate or employee),
  subject_mode:                   Enum["CANDIDATE" | "EMPLOYEE"],
  target_role_id:                 String,
  target_role_tier:               Enum["ENTRY"|"MID"|"SENIOR"|"LEAD"|"EXECUTIVE"],
  requester_id:                   UUID (recruiter | hr_admin | manager),
  requester_role:                 String,
  prediction_mode:                Enum["PM-01" through "PM-09"],
  generated_at:                   ISO 8601 timestamp,

  overall_confidence_score:       Float [0–1],
  confidence_flag:                Enum["HIGH" | "MODERATE" | "SUPPRESSED"],
  signal_coverage_score:          Float [0–1],

  ramp_30d: {
    predicted_score:              Float,
    confidence_interval:          { lower: Float, upper: Float },
    ramp_speed_tier:              String,
    sub_model_confidence:         Float,
    key_accelerators:             Array[String],
    key_blockers:                 Array[String]
  },

  stable_90d: {
    predicted_score:              Float,
    confidence_interval:          { lower: Float, upper: Float },
    performance_tier:             String,
    trajectory:                   String,
    sub_model_confidence:         Float
  },

  ceiling_180d: {
    predicted_ceiling:            Float,
    ceiling_tier:                 String,
    sub_model_confidence:         Float,
    ceiling_limiters:             Array[String],
    ceiling_expanders:            Array[String]
  },

  sustained_365d: {
    probability:                  Float,
    sustainability_tier:          String,
    sub_model_confidence:         Float,
    risk_factors:                 Array[String],
    anchor_factors:               Array[String],
    horizon_cap_days:             Integer | null
  },

  promotion_readiness: {
    verdict:                      String,
    target_tier:                  String,
    readiness_score:              Float,
    confidence:                   Float,
    gap_areas:                    Array[Object],
    accelerators:                 Array[String],
    dojo_belt_required:           String
  },

  reliability_band: {
    variance_score:               Float,
    reliability_tier:             String,
    role_risk_assessment:         String,
    variance_triggers:            Array[String]
  },

  skill_ceiling: {
    predicted_belt_12m:           String,
    confidence:                   Float,
    time_to_next_belt_days:       Integer,
    time_to_next_belt_range:      { min: Integer, max: Integer },
    ceiling_limiters:             Array[String],
    development_acceleration:     Array[String]
  },

  team_forecast:                  Object | null,  // present only in PM-04 and PM-05 modes

  role_fit_summary: {
    intelligence_fit_score:       Float,
    skill_domain_match_score:     Float,
    critical_gap_dimensions:      Array[String],
    dominant_fit_dimensions:      Array[String]
  },

  explainability: {
    top_performance_drivers:      Array[String],
    top_performance_risks:        Array[String],
    ramp_narrative:               String,
    ceiling_narrative:            String,
    reliability_narrative:        String,
    signal_contribution_map:      Object,
    recruiter_brief:              String,
    hiring_manager_brief:         String,
    improvement_path:             Array[Object],
    development_plan_outline:     String
  },

  triad_links: {
    aspe_prediction_id:           UUID | null,
    arpe_prediction_id:           UUID | null
  },

  override_flags: {
    retention_burnout_override:   Boolean,
    role_fit_low_ceiling_warning: Boolean,
    team_instability_modifier:    Boolean,
    compensation_motivation_risk: Boolean
  },

  audit_reference:                UUID,
  consent_reference:              UUID
}
```

**Forbidden Output States:**

```
❌ Forecast emitted with overall_confidence_score < 0.76 to employer
❌ Output without audit_reference
❌ Output without explainability block (minimum recruiter_brief required)
❌ Output without role_fit_summary
❌ Forecast for unverified candidate/employee (TG-01 failure)
❌ Forecast without consent (TG-09 failure)
❌ team_forecast output without PerformanceSimulationLog entry
❌ Promotion readiness verdict without dojo_belt_required declared
❌ Any sub-model output marked HIGH confidence without signal coverage ≥ 0.60
❌ Partial forecast object with missing required top-level fields
```

---

## 🔒 SECTION G — API CONTRACT REGISTRY (SEALED)

All endpoints mandatory. Absence of any endpoint → STOP EXECUTION.

```
POST   /performance/forecast
       Body:     { subject_id, target_role_id, prediction_mode, target_tier? }
       Auth:     Recruiter JWT (verified) | HR Admin JWT | Manager JWT
       Gate:     All trust gates evaluated before processing
       Response: PerformanceForecast object

GET    /performance/forecast/{forecast_id}
       Auth:     Recruiter JWT | HR Admin JWT | Manager JWT | Subject JWT (scoped)
       Response: Full object for authorized roles; improvement_path only for subject

GET    /performance/candidate/{subject_id}/history
       Auth:     Recruiter JWT | HR Admin JWT
       Response: Array[PerformanceForecast] — chronological history

POST   /performance/forecast/team
       Body:     { candidate_ids[], role_assignments[], team_id? }
       Auth:     Recruiter JWT | HR Admin JWT | Company Admin JWT
       Mode:     PM-04 Team Composition Optimizer
       Response: PerformanceForecast with team_forecast populated

POST   /performance/digital-twin/simulate
       Body:     { compositions: Array[{ candidate_ids[], role_assignments[] }] }
       Auth:     Recruiter JWT | Company Admin JWT
       Mode:     PM-05 Digital Twin Simulation
       Response: Array[PerformanceForecast] with simulation_comparison_summary

GET    /performance/promotion-readiness/{employee_id}
       Query:    { target_tier }
       Auth:     HR Admin JWT | Manager JWT
       Mode:     PM-03 Promotion Readiness Assessment
       Response: PerformanceForecast (promotion_readiness block emphasized)

GET    /performance/skill-ceiling/{employee_id}
       Auth:     HR Admin JWT | Manager JWT | Subject JWT
       Mode:     PM-06 Skill Ceiling Audit
       Response: PerformanceForecast (skill_ceiling + ceiling_180d blocks emphasized)

GET    /performance/role-cohort/{role_id}
       Auth:     Company Admin JWT
       Mode:     PM-07 Bulk Role Cohort Forecast
       Response: Cohort aggregate statistics + individual forecast summaries

POST   /performance/hiring-forecast
       Body:     { planned_roles[], headcount_targets[], time_horizon_days }
       Auth:     CFO JWT | Company Admin JWT
       Mode:     PM-08 Hiring Forecast
       Response: Aggregate performance outlook + recommended candidate profiles

GET    /performance/reliability-bench/{team_id}
       Auth:     Manager JWT | HR Admin JWT
       Mode:     PM-09 Reliability Bench Report
       Response: Team variance analysis + per-member reliability tier

GET    /performance/simulation/{simulation_id}
       Auth:     Recruiter JWT | HR Admin JWT | Company Admin JWT
       Response: Full PerformanceSimulationLog record + comparison summary

GET    /performance/audit/{forecast_id}
       Auth:     Governance Board JWT only
       Response: Full immutable audit record + signal snapshots

POST   /performance/appeal
       Body:     { forecast_id, appeal_reason }
       Auth:     Subject JWT (own record) | HR Admin JWT (on behalf)
       Response: Appeal ticket ID → routes to Governance Board

GET    /performance/role-intelligence-model/{role_id}
       Auth:     HR Admin JWT | Recruiter JWT
       Response: Role Intelligence Demand Model (role_fit signal block basis)
                 — which intelligence types this role prioritizes and at what weight
```

---

## 🔒 SECTION H — DATABASE SCHEMA (SEALED)

Primary entities cannot be renamed. Fields may extend — not mutate.

```sql
-- Core forecast record
PerformanceForecast (
  forecast_id                     UUID PRIMARY KEY,
  model_version                   VARCHAR NOT NULL,
  subject_id                      UUID NOT NULL REFERENCES User(id),
  subject_mode                    VARCHAR NOT NULL,        -- CANDIDATE | EMPLOYEE
  target_role_id                  VARCHAR NOT NULL,
  target_role_tier                VARCHAR NOT NULL,
  requester_id                    UUID NOT NULL REFERENCES User(id),
  requester_role                  VARCHAR NOT NULL,
  prediction_mode                 VARCHAR NOT NULL,
  generated_at                    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  overall_confidence_score        FLOAT NOT NULL,
  confidence_flag                 VARCHAR NOT NULL,
  signal_coverage_score           FLOAT NOT NULL,
  suppressed                      BOOLEAN DEFAULT FALSE,
  suppression_reason              VARCHAR,
  -- Sub-model outputs (JSONB for flexibility within schema lock)
  ramp_30d_output                 JSONB NOT NULL,
  stable_90d_output               JSONB NOT NULL,
  ceiling_180d_output             JSONB NOT NULL,
  sustained_365d_output           JSONB NOT NULL,
  promotion_readiness_output      JSONB NOT NULL,
  reliability_band_output         JSONB NOT NULL,
  skill_ceiling_output            JSONB NOT NULL,
  team_forecast_output            JSONB,                   -- null if not PM-04 / PM-05
  role_fit_summary                JSONB NOT NULL,
  override_flags                  JSONB NOT NULL,
  -- Triad links
  aspe_prediction_id              UUID REFERENCES SalaryPrediction(prediction_id),
  arpe_prediction_id              UUID REFERENCES RetentionPrediction(prediction_id),
  -- Governance
  consent_reference               UUID NOT NULL,
  audit_reference                 UUID NOT NULL REFERENCES PerformanceAuditLog(audit_id)
)

-- Explainability record (separate for size management)
PerformanceForecastExplainability (
  explainability_id               UUID PRIMARY KEY,
  forecast_id                     UUID NOT NULL REFERENCES PerformanceForecast(forecast_id),
  top_performance_drivers         JSONB NOT NULL,
  top_performance_risks           JSONB NOT NULL,
  ramp_narrative                  TEXT NOT NULL,
  ceiling_narrative               TEXT NOT NULL,
  reliability_narrative           TEXT NOT NULL,
  signal_contribution_map         JSONB NOT NULL,
  recruiter_brief                 TEXT NOT NULL,
  hiring_manager_brief            TEXT NOT NULL,
  improvement_path                JSONB NOT NULL,
  development_plan_outline        TEXT NOT NULL,
  digital_twin_comparison         TEXT            -- null if not PM-05
)

-- Signal snapshot (immutable — write-once)
PerformanceSignalSnapshot (
  snapshot_id                     UUID PRIMARY KEY,
  forecast_id                     UUID NOT NULL REFERENCES PerformanceForecast(forecast_id),
  signal_block                    VARCHAR NOT NULL,  -- 'dojo'|'intel'|'work'|'champ'|'growth'|
                                                     -- 'reliability'|'role_fit'|'comp'|'retention'
  signal_key                      VARCHAR NOT NULL,
  signal_value                    JSONB NOT NULL,
  trust_weight                    FLOAT NOT NULL,
  staleness_hours                 FLOAT NOT NULL,
  captured_at                     TIMESTAMPTZ NOT NULL
  -- NO UPDATE OR DELETE PERMITTED
)

-- Immutable audit log (append-only enforced at DB constraint level)
PerformanceAuditLog (
  audit_id                        UUID PRIMARY KEY,
  forecast_id                     UUID NOT NULL,
  event_type                      VARCHAR NOT NULL,
  actor_id                        UUID,
  actor_role                      VARCHAR,
  event_payload                   JSONB NOT NULL,
  created_at                      TIMESTAMPTZ NOT NULL DEFAULT NOW()
  -- APPEND-ONLY: NO UPDATE OR DELETE PERMITTED AT ANY ROLE LEVEL
)

-- Consent registry
PerformanceConsentRecord (
  consent_id                      UUID PRIMARY KEY,
  subject_id                      UUID NOT NULL REFERENCES User(id),
  requester_entity_id             UUID NOT NULL REFERENCES Tenant(id),
  consent_type                    VARCHAR DEFAULT 'performance_forecast',
  granted_at                      TIMESTAMPTZ NOT NULL,
  revoked_at                      TIMESTAMPTZ,
  active                          BOOLEAN NOT NULL DEFAULT TRUE,
  consent_version                 VARCHAR NOT NULL
)

-- Digital twin simulation log
PerformanceSimulationLog (
  simulation_id                   UUID PRIMARY KEY,
  simulation_type                 VARCHAR DEFAULT 'TEAM_COMPOSITION',
  requester_id                    UUID NOT NULL REFERENCES User(id),
  composition_inputs              JSONB NOT NULL,     -- all candidate_ids + role_assignments tried
  composition_outputs             JSONB NOT NULL,     -- all PerformanceForecast summaries
  optimal_composition_id          UUID REFERENCES PerformanceForecast(forecast_id),
  comparison_narrative            TEXT,
  created_at                      TIMESTAMPTZ NOT NULL DEFAULT NOW()
  -- APPEND-ONLY: NO UPDATE OR DELETE
)

-- Role Intelligence Demand Model (versioned)
RoleIntelligenceDemandModel (
  model_id                        UUID PRIMARY KEY,
  role_category                   VARCHAR NOT NULL,
  role_tier                       VARCHAR NOT NULL,
  model_version                   VARCHAR NOT NULL,
  intelligence_demand_vector      JSONB NOT NULL,     -- weights per intelligence dimension
  skill_domain_requirements       JSONB NOT NULL,     -- required Dojo skill domains + belt minimums
  pressure_intensity              FLOAT NOT NULL,
  collaboration_intensity         FLOAT NOT NULL,
  independence_score              FLOAT NOT NULL,
  created_at                      TIMESTAMPTZ NOT NULL,
  is_active                       BOOLEAN DEFAULT TRUE
)

-- Appeal records
PerformanceAppeal (
  appeal_id                       UUID PRIMARY KEY,
  forecast_id                     UUID NOT NULL REFERENCES PerformanceForecast(forecast_id),
  subject_id                      UUID NOT NULL,
  appeal_reason                   TEXT NOT NULL,
  status                          VARCHAR DEFAULT 'PENDING',
  governance_decision             TEXT,
  resolved_at                     TIMESTAMPTZ,
  created_at                      TIMESTAMPTZ NOT NULL DEFAULT NOW()
)
```

**Row-level security enforced on all tables.**
**Tenant isolation: no cross-company data access permitted at any role.**
**PerformanceAuditLog: append-only — DB constraint enforced. No UPDATE or DELETE.**
**PerformanceSignalSnapshot: write-once. No modification after forecast finalization.**
**PerformanceSimulationLog: append-only — simulation history preserved permanently.**

---

## 🔒 SECTION I — UI SCREEN CONTRACT (SEALED)

All screens mandatory. Absence → STOP EXECUTION.

### Flutter (Operational Interface)

```
PerformanceForecastGeneratorScreen (PM-01 / PM-02)
  - Subject selector: candidate OR employee (verified only)
  - Target role input + role tier selector
  - Prediction mode toggle (PM-01 to PM-09)
  - Signal block coverage panel: per-block connection status
  - Trust gate strip: TG-01 to TG-10 visual indicators (green/amber/red)
  - [ GENERATE FORECAST ] button → disabled if TG-08 coverage < 0.45 or consent absent
  - Loading: skeleton loaders with progress indicator (sub-models completing sequentially)

PerformanceForecastResultScreen
  - Overall confidence badge: HIGH / MODERATE / SUPPRESSED
  - Timeline view: 30d → 90d → 180d → 365d forecast arc (visual progression chart)
  - 30-Day Ramp Panel:
      Score gauge + ramp_speed_tier badge (FAST/STANDARD/SLOW/AT_RISK)
      Key accelerators list + key blockers list
  - 90-Day Stabilization Panel:
      Score gauge + performance_tier badge + trajectory arrow
      Confidence interval band displayed on gauge
  - 180-Day Ceiling Panel:
      Ceiling bar + ceiling_tier badge (ELITE/HIGH/MODERATE/LIMITED)
      Ceiling limiters + expanders toggle
  - 365-Day Sustainability Panel:
      Probability dial + sustainability_tier + horizon_cap indicator (if ARPE-bounded)
  - Promotion Readiness Panel:
      Verdict badge (READY_NOW/READY_90D/etc.) + readiness_score gauge
      Belt requirement badge + gap areas accordion
  - Reliability Band Panel:
      Variance visualization (narrow band = ROCK_SOLID, wide band = VOLATILE)
      Role risk assessment label
  - Skill Ceiling Panel:
      Belt progression bar: current belt → predicted 12m belt
      Time-to-next-belt range + confidence
  - Role Intelligence Fit Panel:
      Spider chart: candidate intel.radar_scores vs role demand vector
      Critical gap dimensions highlighted red
  - Explainability Panel:
      Recruiter brief (3 sentences, copyable)
      Hiring manager brief (3 sentences, copyable)
      Signal contribution map (horizontal bar chart)
      Improvement path: ranked actions with timeline + owner + expected impact
  - ANTIGRAVITY Triad Links:
      ASPE salary band link (if available)
      ARPE retention score link (if available — employee mode only)
  - [ DOWNLOAD PDF REPORT ] button
  - [ SAVE TO CANDIDATE FILE ] button (recruiter mode)
  - Appeal status banner (if active)

DigitalTwinSimulatorScreen (PM-05)
  - Composition builder: drag-and-drop candidate pool → role slots
  - Up to 5 compositions compared simultaneously
  - Per-composition: team_forecast.predicted_team_score + performance_tier
  - Side-by-side comparison table: intelligence diversity + skill coverage + reliability band
  - Optimal composition highlighted (auto-selected by APFE)
  - [ RUN SIMULATION ] button → triggers PM-05 for all compositions
  - Comparison narrative panel (LLM-generated)
  - [ SAVE SIMULATION ] button → creates PerformanceSimulationLog entry
  - Simulation history panel: past simulations for this role

TeamReliabilityBenchScreen (PM-09)
  - Team selector
  - Per-member reliability tier badge
  - Team variance heatmap: role vs reliability band
  - VOLATILE members flagged with intervention suggestion
  - [ GENERATE BENCH REPORT ] → downloadable PDF

PromotionReadinessBoardScreen (PM-03, HR Admin)
  - Department / team filter
  - Employee list: current tier → readiness verdict → readiness_score
  - Belt level column: current belt vs required belt for promotion
  - Sort by: readiness_score | readiness_verdict | performance_tier
  - [ RUN PROMOTION ASSESSMENT ] per employee
  - Bulk readiness export

HiringForecastPlannerScreen (PM-08, CFO / Talent Ops)
  - Role input grid + headcount targets + time horizon
  - Expected aggregate performance output: weighted team score
  - Skill coverage forecast: which capability gaps remain after planned hires
  - Budget alignment: links to ASPE team forecast for salary envelope
  - [ GENERATE HIRING PLAN ] → PerformanceForecast PM-08 output

SubjectPerformanceInsightScreen (Subject-Facing — Candidate / Employee)
  - Improvement path: top 5 actions with timeline (own improvement_path only)
  - Dojo progression: current belt → predicted 12m belt
  - Development plan outline: 90-day focus areas
  - Skill gap visualization: own intel profile vs role requirement
  - [ BOOK MENTOR SESSION ] shortcut → Dojo Mentor System
  - NO raw scores exposed to subject
  - NO performance_tier or ceiling_tier exposed to subject
  - Only improvement_path, development_plan_outline, and belt progression
```

### Next.js (SEO / Public-Facing — Read Only)

```
CandidatePerformanceInsightPublicPage
  - Candidate-facing: "Your performance readiness signal" (no raw scores)
  - Belt level public display + next belt estimate
  - Call to action: "Run more Dojo matches to improve your performance forecast accuracy"
  - NO performance_tier, ceiling_tier, promotion_readiness exposed publicly
  - NO employer data or role-specific forecasts exposed
```

---

## 🔒 SECTION J — INTEGRATION BINDING (SEALED)

No manual data sync permitted. Event-driven only. Per ECOSKILLER Section P6 + R37 mandate.

```
Dojo Match End Event                → triggers: APFE dojo signal block refresh
Dojo Belt Promotion Event           → triggers: APFE skill_ceiling recalculation + forecast re-flag
Dojo Scenario Difficulty Reclassified → triggers: APFE dojo.scenario_difficulty_ceiling update
Intel Assessment Complete           → triggers: APFE intel signal block refresh
Intel Score Updated                 → triggers: APFE role_fit.intelligence_fit_score recalculation
EIE Tool Sync Complete              → triggers: APFE work signal block refresh (per tool)
EIE HRMS Performance Review Synced → triggers: APFE work.hrms block refresh + promotion_readiness reflag
Championship Result Posted          → triggers: APFE champ signal block refresh
ASPE Prediction Generated           → triggers: APFE comp signal block refresh (C8)
ARPE Prediction Generated           → triggers: APFE retention signal block refresh (C9)
ARPE Risk Tier Changed              → triggers: APFE sustained_365d.horizon_cap_days recalculation
ARPE Burnout Flag Set               → triggers: APFE burnout_override flag activation
Role Intelligence Demand Model Updated → triggers: APFE role_fit block recalculation for affected roles
Collusion Flag Set                  → triggers: APFE TG-06 failure → forecast suppression
Fraud Flag Set                      → triggers: APFE TG-04 failure → forecast suppression
Consent Revoked                     → triggers: APFE forecast suppression + simulation log flagging
Appeal Resolved                     → triggers: PerformanceAuditLog append + subject notification
Simulation Saved                    → triggers: PerformanceSimulationLog creation (immutable)
```

---

## 🔒 SECTION K — PRIVACY, CONSENT & ETHICS LOCK

```
Consent Requirements:
  Subject must grant performance_forecast_consent before ANY forecast runs
  Consent is explicit, informed, and versioned
  Consent UI must display: what signals are analyzed, who sees the forecast, appeal rights,
                           how forecasts are used in hiring/promotion decisions
  Consent revocable at any time → immediate forecast suppression
  Revocation logged in PerformanceAuditLog (immutable)

Subject Visibility Rules:
  Recruiter:          Full forecast + all sub-models + signal block summaries + explainability
  HR Admin:           Full forecast + all sub-models + improvement path + development plan
  Direct Manager:     stable_90d + promotion_readiness + reliability_band + improvement_path
                      (No ceiling or ramp scores — manager focuses on development, not ranking)
  Company Admin:      Aggregated cohort forecasts + promotion pipeline + hiring forecasts
  Subject:            improvement_path + development_plan_outline + belt progression only
                      (NO raw scores | NO performance_tier | NO ceiling_tier | NO reliability_tier)
  CFO / Finance:      PM-08 hiring forecasts only — aggregate, no individual scores
  Governance Board:   Full audit record access — read only

Prohibited Uses:
  ❌ Forecast used as SOLE basis for termination
  ❌ Forecast used to reduce compensation
  ❌ Forecast shared with subject's current employer without consent (candidate mode)
  ❌ Demographic signals inferred from any signal block
  ❌ Cross-tenant forecast data sharing
  ❌ Subject raw scores shared publicly

Ethics Review:
  Monthly ethics audit by ANTIGRAVITY Governance Board
  Bias audit: forecast variance by role tenure + intelligence dimension + region
  Role Intelligence Demand Models reviewed for cultural bias quarterly (per T12)
  Bias threshold violation → immediate model suspension for affected role category
```

---

## 🔒 SECTION L — SECURITY LOCK

```
Auth:
  All APFE API routes require valid JWT
  Role enforcement: per-route RBAC
  HR Admin + Company Admin routes: MFA verification required
  Digital twin simulation: Recruiter JWT (verified) required
  Rate limiting: per IP + per user per endpoint
  WAF enforcement: inherited from ECOSKILLER P1

Data Protection:
  PerformanceForecast: PII-classified → encrypted at rest
  PerformanceSignalSnapshot: encrypted at rest + tenant-isolated
  PerformanceSimulationLog: encrypted at rest + tenant-isolated
  PerformanceAuditLog: append-only enforced at DB constraint level
  No cross-tenant data access under any role

Audit Trail (Immutable):
  Every forecast generation: logged
  Every simulation run: logged
  Every forecast view by recruiter/manager: logged (access audit)
  Every consent grant/revoke: logged
  Every appeal action: logged
  Every Role Intelligence Demand Model update: logged
  All logs → PerformanceAuditLog (append-only — no modification permitted)

Candidate Protection:
  Pre-hire candidate forecasts: encrypted in transit + at rest
  Forecast shared only with verified recruiter entity that initiated the request
  Candidate can view their own improvement_path via authenticated session only
```

---

## 🔒 SECTION M — TEST GATE LOCK

No deployment permitted without all test layers passing. (Per ECOSKILLER P4 + R14 + R50)

```
Unit Tests:
  - Confidence calibration formula: all weight permutations
  - Trust gate evaluation: all 10 gates, all failure paths, all edge combinations
  - Sub-model output boundary conditions: score 0, 100, null inputs
  - Role Intelligence Fit: dot product calculation correctness
  - Override flag logic: all 4 override conditions, interaction effects
  - Staleness decay: boundary values, zero coverage blocks
  - Belt-Promotion Coupling Rule: all tier transitions

Integration Tests:
  - End-to-end: subject_id + role_id → PerformanceForecast object (PM-01)
  - ASPE dependency: ASPE unavailable → C8 zero-weighted gracefully
  - ARPE dependency: ARPE unavailable → C9 zero-weighted gracefully
  - Trust gate TG-06 collusion failure → forecast suppressed → audit log entry confirmed
  - Team composition PM-04: 5 candidates → team_forecast populated correctly
  - Digital Twin PM-05: 3 compositions → PerformanceSimulationLog created
  - Promotion readiness PM-03: belt requirement gate enforced
  - Consent revoke → forecast suppression within 60 seconds

Regression Tests:
  - Known candidate profiles → forecasts within ±5% of APFE-v1.0.0 baseline
  - Override flags: burnout_override → performance modifier applied
  - retention.risk_tier = CRITICAL → horizon_cap_days applied correctly

Load Tests:
  - 500 concurrent PM-01 forecasts: < 4s p99 response
  - PM-05 Digital Twin: 5 compositions × 10 candidates: < 15s response
  - PM-07 bulk cohort: 5,000 employees → complete within 15 minutes
  - Sub-Model H (team optimizer): 20-person team combination analysis: < 8s

Fairness Tests (Mandatory — per T12):
  - Forecast variance: no role category systematically over/under-forecast
  - Role Intelligence Demand Models: no cultural exclusivity in demand vectors (T12)
  - Intelligence dimension bias: no single intelligence type systematically gated
  - Region-based forecast variance: must not exceed defined bias threshold
  - Monthly fairness audit report generated by Governance Board

Ethics Tests:
  - Subject JWT → cannot access raw performance_tier (verified blocked)
  - Manager JWT → cannot access ceiling_tier or ramp score (verified blocked)
  - Cross-tenant subject access: verified blocked at all role levels
  - Demographic proxy: no demographic variable derives from signal blocks (verified at schema level)

Coverage threshold: all test categories must pass before deployment authorization.
```

---

## 🔒 SECTION N — OBSERVABILITY LOCK

(Per ECOSKILLER P5 + R16 + R39)

```
Required Metrics:
  apfe.forecast.generated                   → Counter per prediction_mode
  apfe.forecast.suppressed                  → Counter (confidence < 0.76)
  apfe.trust_gate.failure                   → Counter per gate_id
  apfe.confidence_score.histogram           → Distribution
  apfe.sub_model.confidence.histogram       → Distribution per sub-model (A through H)
  apfe.signal_coverage.histogram            → Distribution
  apfe.performance_tier.distribution        → Gauge per tier (EXCEPTIONAL → UNDERPERFORMING)
  apfe.ceiling_tier.distribution            → Gauge per tier (ELITE → LIMITED)
  apfe.reliability_tier.distribution        → Gauge per tier (ROCK_SOLID → VOLATILE)
  apfe.promotion_readiness.distribution     → Gauge per verdict (READY_NOW → NOT_YET)
  apfe.digital_twin.simulations_run         → Counter
  apfe.role_fit.low_fit_rate                → Gauge: % forecasts with fit_score < 0.50
  apfe.model.inference_latency_ms           → Histogram per sub-model
  apfe.llm_layer.latency_ms                 → Histogram (explainability generation)
  apfe.appeal.open_count                    → Gauge
  apfe.consent.active_count                 → Gauge

Dashboards Required:
  - Forecast volume + suppression rate (daily)
  - Trust gate failure breakdown (per gate)
  - Sub-model confidence distribution heatmap
  - Performance tier distribution trends (role-level cohort view)
  - Promotion pipeline readiness (org-level monthly)
  - Digital twin simulation volume + avg team score improvement
  - Role intelligence fit distribution per role category
  - Model inference latency (p50 / p95 / p99 per sub-model)
  - Bias audit monthly summary

Operations Alerts:
  - apfe.forecast.suppressed rate > 20% → OPS ALERT
  - apfe.trust_gate.failure (TG-04 fraud) spike → IMMEDIATE OPS ALERT
  - apfe.model.inference_latency_ms p99 > 6000 → OPS ALERT
  - apfe.llm_layer.latency_ms p99 > 10000 → OPS WARNING
  - apfe.role_fit.low_fit_rate > 30% for any role → OPS REVIEW (role model recalibration)
  - Role Intelligence Demand Model staleness > 21 days → OPS WARNING
```

---

## 🔒 SECTION O — GOVERNANCE & APPEALS LOCK

```
Appeals Authority:          ANTIGRAVITY Governance Board
Appeals Scope:              Subjects disputing performance forecast or promotion readiness verdict
Appeal Trigger:             Subject submits via POST /performance/appeal
Review SLA:                 72 hours from submission
Review Access:              Full PerformanceAuditLog + PerformanceSignalSnapshot + consent record
Decision Types:             UPHELD | MODIFIED | RETRACTED
Decision Storage:           Immutable append to PerformanceAuditLog

Role Intelligence Demand Model Governance:
  All Role Demand Models versioned and audited
  New role category model: requires Governance Board approval before activation
  Model update: MINOR version bump required
  Model retirement: 3-month parallel operation with successor model

Model Retraining Authority:
  Declared by:              ANTIGRAVITY Technical Governance Board only
  Triggers:
    - Monthly scheduled cycle
    - Outcome validation AUC < 0.72 (per T13 equivalent)
    - Role cohort bias detected in monthly fairness audit
    - > 20 disputed forecasts in 30-day window
    - Signal drift: > 20% delta from expected distribution in any signal block

Fairness Review Cadence:
  Monthly:    Bias audit across all active role categories
  Quarterly:  Role Intelligence Demand Model cultural fairness review (T12)
  Annually:   Full outcome validation study — forecast vs actual performance correlation
              (Per T13 — belts and forecasts must have predictive validity)
```

---

## 🔒 SECTION P — VERSIONING & MUTATION GOVERNANCE LOCK

```
Version Format:             APFE-vMAJOR.MINOR.PATCH

  MAJOR bump required:      Sub-model architecture change / Trust gate restructure /
                            Schema mutation (destructive) / Privacy visibility rule change /
                            Confidence formula weight change / Belt-promotion coupling rule change /
                            Role Intelligence Demand Model scoring algorithm change

  MINOR bump required:      New signal block added / New sub-model added /
                            New prediction mode / New role intelligence category /
                            Override flag addition / New performance tier definition

  PATCH bump permitted:     Bug fix / Monitoring update / LLM prompt update (explainability only) /
                            Non-structural schema field addition / Observability threshold change

Allowed without version bump:
  - Add new integration tool signal (additive to existing block)
  - Update observability dashboards
  - Expand improvement_path action library (LLM additive prompt change)
  - Add new role to Role Intelligence Demand Model library (new entry, no change to algorithm)
  - Update alert SLA thresholds (non-breaking)

NOT allowed without MAJOR version bump:
  - Change sub-model boundary values (tier thresholds)
  - Change trust gate logic or count
  - Change confidence formula weights
  - Change belt-promotion coupling requirements
  - Change output schema structure (destructive)
  - Change privacy visibility rules

Backward Compatibility Window:
  MAJOR version: 6-month parallel operation before retirement
  MINOR version: 3-month parallel operation
  PATCH version: Immediate cutover after test gate pass

Version Tags Required On:
  - Model artifact (all sub-models)
  - Every PerformanceForecast output (model_version field)
  - Every PerformanceAuditLog entry
  - Role Intelligence Demand Model (model_version per role category)
  - Confidence weight config file
  - Consent record (consent_version — tracks policy version agreed to)
```

---

## 🔒 SECTION Q — ANTIGRAVITY INTELLIGENCE TRIAD INTEGRATION SEAL

APFE is the third and final pillar of the ANTIGRAVITY intelligence triad. The triad operates as a unified system. Each model feeds the others.

```
ANTIGRAVITY INTELLIGENCE TRIAD — INTEGRATION MAP

  ASPE (Salary Prediction)
    ↓ compensation_gap signals → ARPE (C4)
    ↓ compensation_alignment signals → APFE (C8)
    ↓ belt_premium → both ARPE and APFE

  ARPE (Retention Probability)
    ↓ retention_probability_score → APFE (C9)
    ↓ flight_window_upper_days → APFE sustained_365d.horizon_cap_days
    ↓ burnout_signal_flag → APFE burnout_override modifier
    ↓ disengagement_trend_flag → APFE trajectory modifier

  APFE (Performance Forecast)
    ↓ performance_tier → ASPE (informs belt_premium justification)
    ↓ promotion_readiness.verdict → ARPE (promotion_pipeline_active = TRUE)
    ↓ ceiling_tier = LIMITED → ASPE (caps salary premium at current belt — no ceiling premium)
    ↓ sustainability_tier → ARPE (informs org.promotion_pipeline_active signal)

Triad Unified Output — PerformanceTalentCard:
  For any single candidate or employee, all three models may be requested together:
  POST /antigravity/talent-card/{subject_id}
  Response: {
    salary_prediction: SalaryPrediction (ASPE),
    retention_forecast: RetentionPrediction (ARPE),
    performance_forecast: PerformanceForecast (APFE),
    triad_narrative: String (LLM-generated holistic talent narrative — 5 sentences max),
    triad_risk_flags: Array[String] (cross-model risk signals surfaced together),
    triad_confidence: Float (harmonic mean of all three model confidence scores),
    audit_references: { aspe: UUID, arpe: UUID, apfe: UUID }
  }

  PerformanceTalentCard is the master recruiter artifact.
  It is the foundation of the Hiring AI Agent (Module 7, Feature #24).
  It is the evidence layer for Simulation Hiring (Module 7, Feature #22).
  Absence of any triad model → PerformanceTalentCard not generated.
```

---

## 🔒 FINAL GOVERNANCE SEAL BLOCK

```
═══════════════════════════════════════════════════════════════════════════════
ANTIGRAVITY PERFORMANCE FORECAST ENGINE — SEAL v1.0.0
═══════════════════════════════════════════════════════════════════════════════

ENTERPRISE OPTIMIZATION MODE:                         ACTIVE
TRUST INFRASTRUCTURE:                                 LOCKED
ECOSKILLER SIGNAL INTEGRATION (9 BLOCKS):             REQUIRED
DOJO SKILL PERFORMANCE SIGNAL (C1):                   REQUIRED
INTELLIGENCE MEASUREMENT SIGNAL (C2 — EIMS):          REQUIRED
REAL WORK OUTPUT SIGNAL (EIE — 100 TOOLS) (C3):       REQUIRED
CHAMPIONSHIP PERFORMANCE SIGNAL (C4):                 REQUIRED
LEARNING VELOCITY SIGNAL (C5):                        REQUIRED
BEHAVIORAL RELIABILITY SIGNAL (C6):                   REQUIRED
ROLE INTELLIGENCE FIT SIGNAL (C7):                    REQUIRED
COMPENSATION ALIGNMENT SIGNAL (ASPE C8):              REQUIRED
RETENTION STABILITY SIGNAL (ARPE C9):                 REQUIRED
GRADIENT BOOSTED MODEL (R28 COST-OPTIMIZED):          ENFORCED
8 SUB-MODELS (A THROUGH H):                           ALL REQUIRED
LLM LAYER (EXPLAINABILITY ONLY — R28 SCOPED):         ENFORCED
CONFIDENCE GATE (0.76):                               ENFORCED
8 FORECAST DIMENSIONS:                                ALL REQUIRED
DIGITAL TWIN SIMULATION (PM-05):                      REQUIRED
TEAM COMPOSITION OPTIMIZER (PM-04):                   REQUIRED
ROLE INTELLIGENCE DEMAND MODELS:                      VERSIONED AND REQUIRED
BELT-PROMOTION COUPLING RULE:                         ENFORCED
SUBJECT CONSENT (PREREQUISITE):                       ENFORCED
AUDIT LOG (IMMUTABLE — APPEND ONLY):                  REQUIRED
SIMULATION LOG (APPEND ONLY):                         REQUIRED
APPEALS SYSTEM:                                       ACTIVE
GOVERNANCE BOARD AUTHORITY:                           REQUIRED
FAIRNESS BIAS AUDIT (MONTHLY):                        MANDATORY
ROLE INTELLIGENCE MODEL CULTURAL REVIEW (QUARTERLY):  MANDATORY
OUTCOME VALIDATION STUDY (ANNUALLY):                  MANDATORY
PRIVACY VISIBILITY RULES (TIERED):                    LOCKED
PII ENCRYPTION:                                       REQUIRED
RBAC ENFORCEMENT (PER ROLE):                          REQUIRED
TENANT ISOLATION:                                     REQUIRED
TEST GATE (ALL LAYERS):                               REQUIRED BEFORE DEPLOY
VERSIONED MUTATION ONLY:                              ENFORCED
INTERPRETATION AUTHORITY:                             NONE
ARCHITECTURE AUTHORITY:                               LOCKED

TRIAD DEPENDENCIES:
  ASPE-v1.0.0 must be operational before APFE deployment
  ARPE-v1.0.0 must be operational before APFE deployment
  PerformanceTalentCard requires all three ANTIGRAVITY models active

Absence of ANY above component
→ STOP EXECUTION
→ REPORT APFE-INCOMPLETE
→ NO DEPLOYMENT CLAIM PERMITTED

═══════════════════════════════════════════════════════════════════════════════
ANTIGRAVITY APFE-v1.0.0 | ECOSKILLER UNIFIED PLATFORM | SEALED
Issued Under: ECOSKILLER Master Execution Prompt v12.0
              Dojo SaaS Production Artifact Spec v1.0
              Talent Operating System Blueprint v1.0
              R28 — Intelligence Cost Optimization Law
Siblings:     ASPE-v1.0.0 (Salary Prediction Model)
              ARPE-v1.0.0 (Retention Probability Model)
Triad Product: ANTIGRAVITY PerformanceTalentCard
═══════════════════════════════════════════════════════════════════════════════
```

---

*Document Class: Sealed Production Prompt Artifact*
*Mutation Policy: Add-Only via Version Bump*
*Interpretation Authority: NONE*
*Architecture Authority: LOCKED*
*Prepared For: ANTIGRAVITY Enterprise Build Team*
*Version: APFE-v1.0.0*
*Paired With: ASPE-v1.0.0 (Salary Prediction) · ARPE-v1.0.0 (Retention Probability)*
*Triad Completion: ANTIGRAVITY Intelligence Triad — ALL THREE MODELS SEALED*
