# 🔒 ANTIGRAVITY — RETENTION PROBABILITY MODEL
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### SEALED PRODUCTION ARTIFACT SPEC v1.0

---

```
Artifact Class:         Production AI Inference System
System Parent:          ECOSKILLER + DOJO SAAS Unified Platform
Artifact Name:          ANTIGRAVITY Retention Probability Engine (ARPE)
Sibling Artifact:       ANTIGRAVITY Salary Prediction Engine (ASPE-v1.0.0)
Mutation Policy:        Add-only via version bump
Execution Mode:         Deterministic · Contract-Gated · Event-Driven
Interpretation Auth:    NONE
Architecture Auth:      LOCKED
Trust Seal:             ACTIVE
Version:                ARPE-v1.0.0
Issued Under:           ECOSKILLER Master Execution Prompt v12.0
                        Dojo SaaS Production Artifact Spec v1.0
                        Talent Operating System Blueprint v1.0
```

---

> **EXECUTION DECREE**
> This document is a sealed production prompt artifact for the ANTIGRAVITY Retention Probability Engine operating within the ECOSKILLER Enterprise Optimization + Trust Infrastructure. No section may be reinterpreted, restructured, or modified without a declared version bump. All subsystems described herein are mandatory. Absence of any subsystem → STOP EXECUTION → REPORT INCOMPLETE → NO DEPLOYMENT CLAIM PERMITTED.

---

## 🔒 SECTION A — SYSTEM IDENTITY & SCOPE LOCK

```
Model Name:             ANTIGRAVITY Retention Probability Engine (ARPE)
Module Parent:          ECOSKILLER Recruiter System — Module 7 (Features #12, #14, #15)
                        ECOSKILLER Trust System — Module 15
                        Enterprise HR Replacement Layer — Pillar 19
Infrastructure:         Enterprise Optimization + Trust Infrastructure Layer
Trust Class:            Tier-1 Verified Behavioral Inference (Employer + HR Facing)
Prediction Scope:       Individual · Team · Department · Tenure Band · Role Cluster
Output Format:          Retention Probability Score + Risk Tier + Flight Risk Window +
                        Root Cause Vector + Intervention Recommendation Path
Confidence Gate:        Minimum 0.75 confidence score required for employer-facing output
Failure Mode:           STOP → REPORT → NO PARTIAL SCORE EMITTED
Sibling Dependency:     ASPE-v1.0.0 salary delta signal required for compensation gap factor
```

### Mission Statement

ANTIGRAVITY Retention Probability Engine exists to transform reactive HR into predictive talent intelligence. It synthesizes verified behavioral data from the Dojo engagement system, real work tool activity via the ECOSKILLER Integration Engine, platform habit and streak signals, compensation gap data from ASPE, intelligence growth trajectories, championship participation patterns, social feed engagement, HRMS historical records, and environmental org signals — into a single trust-anchored, auditable retention probability score and flight risk classification per employee per time window.

**This is NOT a survey-based engagement tool.**
**This is NOT a manual HR questionnaire.**
**This IS a live multi-signal behavioral inference engine locked to verified identity and real work data.**

Recruiters benefit: `"Hiring from ECOSKILLER gives 40% better retention."`
This engine produces the proof.

---

## 🔒 SECTION B — TRUST PREREQUISITE LOCK

ARPE CANNOT execute unless all trust prerequisites are satisfied. Trust prerequisites are enforced by the ECOSKILLER Trust Infrastructure (Module 15) and are inherited from the ANTIGRAVITY Trust Framework.

### Mandatory Trust Gates (All Required — None Optional)

| Gate ID | Trust Signal | Required Status | Source System |
|---------|-------------|-----------------|---------------|
| TG-01 | Employee Identity Verified | `verified_status = TRUE` | ECOSKILLER Universal ID / ProfessionalID |
| TG-02 | Company Entity Verified | `company_verified = TRUE` | R37 Company Organization & Employee Verification Law |
| TG-03 | Employee-Company Link Verified | `employment_link_verified = TRUE` | Company Employee Verified Badge (R37) |
| TG-04 | HR Admin Role Active | `hr_admin_role = TRUE` | ECOSKILLER RBAC Engine — per-route enforcement |
| TG-05 | No Active Fraud Flag | `fraud_flag = FALSE` | ECOSKILLER Trust System — Fraud Detection (Module 15) |
| TG-06 | No Active Fake Profile Flag | `fake_profile_flag = FALSE` | ECOSKILLER Fake Profile Detection (Module 15) |
| TG-07 | Minimum Signal Coverage | `signal_coverage_score ≥ 0.40` | ARPE Signal Coverage Calculator (min 2 of 7 signal blocks populated) |
| TG-08 | Employee Consent Granted | `retention_analysis_consent = TRUE` | Consent Registry — timestamped + audited |

**If ANY gate is NOT satisfied → STOP PREDICTION → EMIT TRUST_GATE_FAILURE → Log to AuditLog (immutable) → No employer-facing output.**

---

## 🔒 SECTION C — DATA INPUT SIGNAL REGISTRY (SEALED)

All signals are read-only to the inference engine. No signal may be mutated by ARPE. Signals are consumed from ECOSKILLER event streams and integration engine outputs.

---

### C1 — Dojo Engagement & Skill Progression Signal Block

```
Source:             Dojo SaaS Engine (ECOSKILLER Module 3 — Skill Dojo System)
Access Mode:        Analytics Engine event-stream read
Freshness Policy:   Max 72-hour staleness
Weight Class:       HIGH — direct observable behavior signal

Signals:
  dojo.active_match_frequency_30d        → Float: matches per week over last 30 days
  dojo.active_match_frequency_90d        → Float: matches per week over last 90 days
  dojo.match_frequency_delta             → Float: 90d vs 30d frequency change (trend signal)
  dojo.belt_progression_velocity         → Float: belt levels gained per 6-month window
  dojo.belt_stall_duration_days          → Integer: days since last belt progression
  dojo.skill_track_completion_rate       → Float: % of assigned/enrolled skill tracks completed
  dojo.skill_delta_90d                   → Float: post-track vs pre-track score delta over 90d (T6)
  dojo.skill_regression_flag             → Boolean: TRUE if skill score declined > 10% in 30d
  dojo.drill_engagement_rate             → Float: drills completed / drills available
  dojo.drill_abandonment_rate            → Float: drills abandoned mid-session
  dojo.scenario_completion_trend         → Float: completion rate trend (positive / negative slope)
  dojo.pressure_scenario_pass_rate_30d   → Float: recent pressure scenario performance
  dojo.mentor_interaction_frequency      → Float: mentor sessions per month
  dojo.mentor_interaction_delta          → Float: trend change in mentor session frequency
  dojo.certification_pipeline_activity   → Boolean: active in certification prep path
  dojo.replay_viewing_rate               → Float: replays viewed / replays available (learning signal)
  dojo.tournament_participation_rate     → Float: tournaments entered in last 90d
  dojo.score_confidence_trend            → Float: trend in inter-rater confidence score over 90d
```

**Interpretation Rules:**
- Declining `dojo.active_match_frequency_30d` vs `dojo.active_match_frequency_90d` = flight risk signal
- `dojo.belt_stall_duration_days > 120` = disengagement indicator
- `dojo.skill_regression_flag = TRUE` = burnout or distraction indicator
- Rising `dojo.drill_abandonment_rate` = motivation loss signal

---

### C2 — Platform Habit & Behavioral Engagement Signal Block

```
Source:             ECOSKILLER Habit & Retention System (R95 — Streak & Retention Loop)
                    ECOSKILLER Student/Professional Activity Engine
Access Mode:        DailyActivity + StreakTracker event stream
Freshness Policy:   Max 24-hour staleness
Weight Class:       HIGH — daily behavioral proxy signal

Signals:
  habit.current_streak_days               → Integer: current daily login/activity streak
  habit.max_streak_days                   → Integer: lifetime max streak (engagement ceiling)
  habit.streak_break_count_30d            → Integer: times streak broken in last 30 days
  habit.streak_recovery_time_avg          → Float: avg days to resume streak after break
  habit.challenge_participation_30d       → Integer: challenges joined in last 30 days
  habit.challenge_completion_rate         → Float: challenges completed / joined
  habit.reward_box_claim_rate             → Float: reward boxes claimed / granted
  habit.daily_active_days_30d             → Integer: distinct active days in last 30 days
  habit.daily_active_days_90d             → Integer: distinct active days in last 90 days
  habit.activity_recency_score            → Float [0–1]: recency-weighted activity composite
  habit.notification_open_rate_30d        → Float: push/in-app notifications opened / sent
  habit.notification_open_rate_delta      → Float: trend in open rate vs previous 30d
  habit.feature_breadth_score             → Float: distinct feature categories used in last 30d
  habit.session_duration_avg_min          → Float: average session length in minutes
  habit.session_duration_trend            → Float: trend in session duration over 90d
```

**Interpretation Rules:**
- `habit.current_streak_days < 3` AND `habit.streak_break_count_30d > 3` = disengagement cluster
- `habit.daily_active_days_30d < 5` = passive user (high flight risk modifier)
- `habit.notification_open_rate_delta < -0.30` = rapid disengagement signal
- `habit.session_duration_trend < -0.20` = declining platform investment

---

### C3 — Real Work Activity Signal Block (Integration Engine)

```
Source:             ECOSKILLER Integration Engine (EIE) — Module 9
                    100-Tool Integration Layer (OAuth + Webhook + AI Normalization)
                    Universal Work Data Format (UWDF) normalized output
Access Mode:        UWDF event stream — real work data
Freshness Policy:   Max 7-day staleness for active integrations
Weight Class:       VERY HIGH — direct work output signal

Signals per connected tool:

  work.github_signals:
    commit_frequency_30d                → Float: commits per active week (last 30d)
    commit_frequency_90d                → Float: commits per active week (last 90d)
    commit_frequency_delta              → Float: trend change
    pr_review_participation_rate        → Float: PR reviews done / PRs available
    issue_resolution_avg_days           → Float: avg days to close assigned issues
    repository_diversity_score          → Float: breadth of repos contributed to
    night_weekend_commit_rate           → Float: % commits outside working hours (burnout signal)
    code_revert_rate_30d                → Float: code reverted / code submitted (frustration signal)
    collaboration_breadth_30d           → Integer: unique collaborators in 30d
    collaboration_breadth_delta         → Float: trend vs previous 30d

  work.jira_signals:
    ticket_velocity_30d                 → Float: tickets completed per sprint (last 30d)
    ticket_velocity_delta               → Float: trend vs previous 30d
    ticket_overdue_rate                 → Float: overdue tickets / assigned tickets
    ticket_reassignment_rate            → Float: tickets reassigned away / total assigned
    sprint_commitment_accuracy          → Float: committed vs completed ratio
    blocker_creation_rate               → Float: blockers raised per sprint
    cross_team_collaboration_score      → Float: tickets involving other teams

  work.slack_signals:
    message_send_frequency_30d          → Float: messages sent per active day
    message_send_frequency_delta        → Float: trend change
    response_time_avg_hours             → Float: avg response time to direct messages
    response_time_trend                 → Float: worsening or improving
    channel_participation_breadth       → Integer: distinct channels active in last 30d
    channel_breadth_delta               → Float: trend change
    after_hours_message_rate            → Float: % messages sent outside working hours
    emoji_reaction_rate                 → Float: reactions given / messages received (culture engagement)
    dm_initiation_rate                  → Float: DMs initiated vs received

  work.hrms_signals (Workday / BambooHR / Darwinbox / Keka / SAP SuccessFactors / ADP):
    tenure_months                       → Integer: months at current employer
    performance_review_avg              → Float: avg score across last 3 reviews
    performance_review_trend            → Float: improving or declining
    promotion_count_total               → Integer: promotions received lifetime
    promotion_recency_months            → Integer: months since last promotion
    time_since_last_raise_months        → Integer: months since last compensation change
    peer_feedback_score_avg             → Float: 360 feedback composite
    peer_feedback_trend                 → Float: improving or declining
    absenteeism_rate_30d                → Float: absent days / working days
    absenteeism_rate_delta              → Float: trend vs previous 30d
    pto_usage_rate                      → Float: PTO taken / PTO available (underuse = burnout signal)
    internal_transfer_requests          → Integer: active internal transfer requests (count)
    manager_change_count_12m            → Integer: manager changes in last 12 months
    team_composition_change_pct_90d     → Float: % of immediate team that changed in 90d
    training_enrollment_rate            → Float: L&D programs enrolled / available
    onboarding_completion_pct           → Float: % of onboarding tasks completed (new hire signal)

  work.normalized_reliability_score     → Float [0–1]: AI cross-signal reliability composite (UWDF output)
  work.behavioral_anomaly_flag          → Boolean: AI-detected behavioral anomaly in work signals
  work.work_intensity_score             → Float: composite of hours + output + quality signals
  work.work_intensity_trend             → Float: trend direction over 90d
```

**Interpretation Rules:**
- `work.commit_frequency_delta < -0.40` = significant work output reduction
- `work.night_weekend_commit_rate > 0.45` = sustained burnout signal
- `work.code_revert_rate_30d > 0.20` = quality/frustration signal
- `work.hrms_signals.promotion_recency_months > 18` AND `work.hrms_signals.performance_review_avg ≥ 3.8` = recognition gap (flight risk amplifier)
- `work.hrms_signals.manager_change_count_12m ≥ 2` = environmental instability signal
- `work.hrms_signals.internal_transfer_requests ≥ 1` = active departure intent signal
- `work.hrms_signals.pto_usage_rate < 0.30` = burnout risk (underutilization pattern)
- `work.hrms_signals.team_composition_change_pct_90d > 0.35` = team instability (contagion risk)

---

### C4 — Compensation Gap Signal Block (ASPE Integration)

```
Source:             ANTIGRAVITY Salary Prediction Engine (ASPE-v1.0.0)
Access Mode:        ASPE SalaryPrediction object — read only
Freshness Policy:   Max 30-day staleness (re-prediction triggers refresh)
Weight Class:       VERY HIGH — compensation gap is a primary flight risk driver

Signals:
  compensation.current_ctc                → Float: current total compensation (HRMS verified)
  compensation.aspe_recommended_p50       → Float: ASPE market median for role + region
  compensation.aspe_recommended_p75       → Float: ASPE competitive band ceiling
  compensation.market_gap_pct             → Float: (aspe_p50 - current_ctc) / aspe_p50 × 100
  compensation.competitive_gap_pct        → Float: (aspe_p75 - current_ctc) / aspe_p75 × 100
  compensation.belt_premium_uncaptured    → Float: belt premium not reflected in current CTC
  compensation.last_raise_delta_pct       → Float: last raise % vs market YoY growth rate
  compensation.total_unvested_equity      → Float: unvested equity value (retention anchor)
  compensation.equity_cliff_months        → Integer: months to next vesting cliff (departure risk window)
  compensation.equity_vested_pct          → Float: % of total grant already vested
  compensation.compensation_currency      → String: ISO 4217
  compensation.aspe_confidence_flag       → String: HIGH | MODERATE (inherited from ASPE output)
```

**Interpretation Rules:**
- `compensation.market_gap_pct > 15%` = moderate compensation flight risk
- `compensation.market_gap_pct > 25%` = high compensation flight risk
- `compensation.competitive_gap_pct > 20%` = severe compensation risk (competitor poaching window)
- `compensation.equity_cliff_months ≤ 3` = departure risk window active (post-cliff departure pattern)
- `compensation.equity_vested_pct > 0.85` = equity anchor depleted
- `compensation.belt_premium_uncaptured > 10%` = unrecognized performance → retention risk

---

### C5 — Intelligence Growth & Career Trajectory Signal Block

```
Source:             ECOSKILLER Intelligence Measurement System (Module 2)
Access Mode:        Cached inference result — max 30-day staleness
Weight Class:       MEDIUM — leading indicator of career ambition alignment

Signals:
  intel.current_percentile_rank           → Float [0–100]: global percentile
  intel.growth_delta_90d                  → Float: intelligence score change over 90 days
  intel.growth_delta_180d                 → Float: intelligence score change over 180 days
  intel.career_intelligence_map           → Array[String]: AI-predicted career aptitude zones
  intel.current_role_alignment_score      → Float [0–1]: alignment of current role with aptitude zones
  intel.role_alignment_trend              → Float: improving or declining role-intelligence fit
  intel.learning_speed_score              → Float: normalized learning velocity
  intel.goal_tracking_active              → Boolean: active career goal set in AI Mentor System
  intel.goal_progress_pct                 → Float: % progress toward declared career goals
  intel.goal_stall_duration_days          → Integer: days since last goal progress recorded
  intel.ai_mentor_session_frequency_30d   → Float: AI mentor interactions per week
  intel.ai_mentor_session_delta           → Float: trend change in mentor interaction rate
  intel.career_prediction_alignment       → Float: current trajectory vs AI career prediction alignment
```

**Interpretation Rules:**
- `intel.role_alignment_score < 0.45` = misalignment between employee capability and current role
- `intel.goal_stall_duration_days > 60` = career stagnation perception signal
- `intel.ai_mentor_session_delta < -0.50` = disengagement from growth path
- High `intel.growth_delta_90d` + low `intel.role_alignment_score` = high ambition, low fit = flight risk

---

### C6 — Championship & External Visibility Signal Block

```
Source:             ECOSKILLER Championship System (Module 4)
Access Mode:        Event log read
Freshness Policy:   Max 7-day staleness
Weight Class:       MEDIUM — external recognition and ambition signal

Signals:
  champ.active_competition_flag           → Boolean: currently registered in active competition
  champ.competition_frequency_90d         → Integer: competitions entered in last 90 days
  champ.competition_frequency_delta       → Float: trend vs previous 90 days
  champ.tier_reached_latest               → Enum: SCHOOL|DISTRICT|STATE|NATIONAL|CONTINENTAL|WORLD
  champ.external_ranking_percentile       → Float [0–100]: ranking in latest competition
  champ.win_rate_trend                    → Float: improving or declining
  champ.public_leaderboard_visibility     → Boolean: opted into public leaderboard
  champ.recruiter_profile_views_30d       → Integer: recruiter views of candidate profile in 30d
  champ.recruiter_profile_views_delta     → Float: trend change (rising = external market interest)
  champ.external_job_application_signals  → Boolean: any signals of external application activity
                                            (derived from integrated platform signals — not direct tracking)
```

**Interpretation Rules:**
- Rising `champ.recruiter_profile_views_30d` + rising `champ.competition_frequency_90d` = building external market presence → flight risk signal
- `champ.external_job_application_signals = TRUE` = active departure intent (highest weight)
- `champ.public_leaderboard_visibility = TRUE` AND `champ.tier_reached_latest ≥ NATIONAL` = premium candidate — elevated poaching risk

---

### C7 — Social & Organizational Network Signal Block

```
Source:             ECOSKILLER Social System (Module 14) + R34 Social Groups & Posts
                    R37 Company Organization System
Access Mode:        Social event stream + Company group activity stream
Freshness Policy:   Max 48-hour staleness
Weight Class:       MEDIUM — cultural and social integration signal

Signals:
  social.post_frequency_30d               → Float: posts created per week
  social.post_frequency_delta             → Float: trend change vs previous 30d
  social.reaction_received_rate_30d       → Float: reactions received per post
  social.peer_endorsement_received_30d    → Integer: endorsements received in 30d
  social.company_group_activity_score     → Float: activity in company internal groups
  social.company_group_delta              → Float: trend in company group activity
  social.cross_team_connection_count      → Integer: connections from other departments
  social.alumni_connection_activity       → Float: activity with connections who left company
                                            (high = departure network signal)
  social.external_group_join_rate_30d     → Float: external professional groups joined in 30d
  social.institution_community_score      → Float: alumni/institute community engagement
  social.skill_share_frequency_30d        → Float: skill-related content shared in 30d
  social.achievement_share_frequency      → Float: achievements publicly shared in 30d
  social.company_sentiment_posts          → Float: AI-assessed sentiment of company-related posts
                                            [-1 negative → +1 positive] (company posts only, not personal)
```

**Interpretation Rules:**
- `social.alumni_connection_activity > 0.6` = active departure network engagement
- `social.company_group_delta < -0.40` = withdrawal from company culture
- `social.company_sentiment_posts < -0.30` = negative cultural sentiment signal
- Rising `social.external_group_join_rate_30d` = external professional positioning
- `social.post_frequency_delta > 0.50` + public achievement sharing = building personal brand (external market preparation)

---

### C8 — Environmental & Organizational Context Signal Block

```
Source:             HRMS Integration (EIE) + Company Organization System (R37)
                    ECOSKILLER Org Analytics Engine
Access Mode:        Company entity event stream + HRMS sync
Freshness Policy:   Max 24-hour staleness for critical org signals
Weight Class:       HIGH — environmental signals amplify individual risk scores

Signals:
  org.department_headcount_change_90d     → Float: % headcount change in employee's department
  org.department_attrition_rate_90d       → Float: departures / headcount in department (last 90d)
  org.team_attrition_rate_90d             → Float: departures / headcount in immediate team (last 90d)
  org.manager_tenure_months               → Integer: months current manager has been in role
  org.manager_departure_flag              → Boolean: current manager has given notice / departed
  org.company_headcount_trend_90d         → Float: overall company headcount change (positive / negative)
  org.layoff_signal_flag                  → Boolean: AI-detected layoff or restructuring signals
                                            (from public announcements + internal org changes)
  org.role_elimination_risk_score         → Float [0–1]: AI-assessed probability this role category
                                            is at risk in company's sector (sector + company signal)
  org.promotion_pipeline_active           → Boolean: employee in active promotion consideration
  org.recognition_event_recency_days      → Integer: days since last public recognition / award
  org.company_trust_score                 → Float [0–1]: ECOSKILLER Trust Score for the company entity
  org.company_verification_status         → Boolean: company verified on platform (R37)
```

**Interpretation Rules:**
- `org.team_attrition_rate_90d > 0.20` = contagion risk (departure clustering)
- `org.manager_departure_flag = TRUE` = critical departure risk amplifier
- `org.layoff_signal_flag = TRUE` = override to HIGH_RISK tier regardless of other signals
- `org.recognition_event_recency_days > 180` + `work.hrms.performance_review_avg ≥ 3.8` = unrecognized high performer (highest flight risk category)
- `org.promotion_pipeline_active = TRUE` = retention anchor signal

---

## 🔒 SECTION D — PREDICTION MODEL ARCHITECTURE (SEALED)

### D1 — Model Class Declaration

```
Model Type:             Gradient Boosted Ensemble (per R28 — Intelligence Cost Optimization Law)
                        Traditional ML — NOT LLM (cost-optimized per R28 mandate)
                        LLM used ONLY for: explainability text generation + intervention narrative
Components:
  Layer 1:              Signal Normalization + Staleness Weighting
  Layer 2:              Trust-Weighted Feature Composition
  Layer 3:              Retention Probability Regression (0–100 score)
  Layer 4:              Risk Tier Classification + Flight Window Estimation
  Layer 5:              Root Cause Vector Decomposition
  Layer 6:              Confidence Calibration
  Layer 7:              Intervention Recommendation Generator (LLM-assisted)
Output:                 RetentionPrediction object (Section F)
Model Cost:             Traditional ML inference — estimated < $0.002 per prediction
LLM Layer Cost:         Used only for explainability — estimated < $0.01 per report
Training Data:          Anonymized historical employment + departure outcomes from
                        ECOSKILLER marketplace + integrated HRMS data
Retraining Policy:      Monthly — requires Governance Board authorization + test gate pass
```

---

### D2 — Layer 1: Signal Normalization + Staleness Weighting

```
Normalization:
  Float signals:          Min-max normalization per signal historical distribution
  Integer signals:        Log-scaled normalization for skewed distributions
  Boolean signals:        Binary (0 = FALSE, 1 = TRUE)
  Trend signals:          Standardized to [-1, +1] range
  Enum signals:           Ordinal encoding per defined hierarchy

Staleness Weighting:
  Signals older than declared freshness policy → weight decayed by:
    w_decay = max(0.1, 1 - (staleness_hours / max_staleness_hours))

  Signals with zero coverage (integration not connected) → weight = 0.0
  Signal coverage score = (connected blocks / 8 total blocks)
  Minimum coverage for execution: ≥ 0.40 (TG-07)

Fusion:
  All normalized, staleness-weighted signals concatenated into
  unified_feature_vector [F] → input to Layer 2
```

---

### D3 — Layer 2: Trust-Weighted Feature Composition

```
Trust Weight Assignment:
  C1 Dojo signals:          weight = dojo.score_confidence × (1 - dojo.drill_abandonment_rate)
  C2 Habit signals:          weight = 1.0 (platform-native — no external trust dependency)
  C3 Work signals:           weight = work.normalized_reliability_score
  C4 Compensation signals:   weight = compensation.aspe_confidence_flag == "HIGH" ? 1.0 : 0.75
  C5 Intelligence signals:   weight = intel.confidence_score (from ECOSKILLER intel engine)
  C6 Championship signals:   weight = 1.0 if champ.ai_evaluation_score ≥ 0.70 else 0.60
  C7 Social signals:         weight = 0.80 (AI-inferred signals, inherently lower precision)
  C8 Environmental signals:  weight = 1.0 (org-level signals are high-confidence)

  Trust-weighted feature vector:
    TF = Σ (feature_block_i × trust_weight_i) for all signal blocks
    Normalized TF → fed to Layer 3
```

---

### D4 — Layer 3: Retention Probability Regression

```
Model:              Gradient Boosted Regressor (XGBoost or LightGBM — cost-optimal)
Input:              Trust-weighted feature vector TF
Output:             retention_probability_score ∈ [0, 100]

Score Interpretation:
  90–100  → Very High Retention Probability (minimal risk)
  75–89   → High Retention Probability (monitor annually)
  60–74   → Moderate Retention Probability (monitor quarterly)
  40–59   → Low Retention Probability (active risk — intervene)
  20–39   → Very Low Retention Probability (imminent departure risk)
  0–19    → Critical Departure Risk (intervention required immediately)

Target Variable (Training):
  Binary departure within 90-day / 180-day / 365-day windows
  Calibrated to output probability ∈ [0, 100] via Platt scaling

Feature Importance:
  Published and versioned per model release
  Required for explainability audit (Section I)

Override Rules:
  If org.layoff_signal_flag = TRUE           → score capped at 35 regardless of other signals
  If work.hrms.internal_transfer_requests ≥ 1 → score capped at 45
  If champ.external_job_application_signals = TRUE → score capped at 30
  If org.manager_departure_flag = TRUE        → score multiplied by 0.85 (penalty applied)
```

---

### D5 — Layer 4: Risk Tier Classification + Flight Window Estimation

```
Risk Tier Matrix:

  Score 90–100  → TIER: STABLE
                  Flight Window: > 18 months
                  Action: Maintain engagement — annual review
                  Alert: None

  Score 75–89   → TIER: LOW_RISK
                  Flight Window: 12–18 months
                  Action: Schedule development conversation
                  Alert: Quarterly HR notification

  Score 60–74   → TIER: WATCH
                  Flight Window: 6–12 months
                  Action: Compensation review + growth path review
                  Alert: Monthly HR dashboard flag

  Score 40–59   → TIER: AT_RISK
                  Flight Window: 3–6 months
                  Action: Immediate manager intervention + compensation review
                  Alert: Immediate HR notification + manager notification

  Score 20–39   → TIER: HIGH_RISK
                  Flight Window: 1–3 months
                  Action: Emergency retention package assessment required
                  Alert: HR Admin + Company Admin notification within 24h

  Score 0–19    → TIER: CRITICAL
                  Flight Window: < 30 days (active departure likely)
                  Action: CEO / Leadership intervention — immediate
                  Alert: Immediate escalation — all org admin stakeholders

Flight Window Estimation Method:
  Base window from score tier
  Adjusted by:
    + compensation.equity_cliff_months (if cliff ≤ 3 months → window anchored to cliff date)
    + org.team_attrition_rate_90d (contagion acceleration factor)
    + work.hrms.promotion_recency_months (longer = window shortened)
    + social.alumni_connection_activity (higher = window shortened)
```

---

### D6 — Layer 5: Root Cause Vector Decomposition

```
Root Cause Categories (Sealed Set — Extensible via Version Bump Only):

  RC-01   COMPENSATION_GAP
          Trigger: compensation.market_gap_pct > 15%
          Evidence: ASPE delta + belt_premium_uncaptured + raise_lag

  RC-02   CAREER_STAGNATION
          Trigger: belt_stall_duration_days > 120 + promotion_recency_months > 18
          Evidence: belt progression + goal stall + mentor interaction decline

  RC-03   RECOGNITION_DEFICIT
          Trigger: performance_review_avg ≥ 3.8 + recognition_recency > 180d
          Evidence: peer feedback + HRMS performance + public recognition gap

  RC-04   ROLE_MISALIGNMENT
          Trigger: intel.role_alignment_score < 0.45
          Evidence: intelligence aptitude vs current role classification mismatch

  RC-05   MANAGER_INSTABILITY
          Trigger: manager_change_count_12m ≥ 2 OR manager_departure_flag = TRUE
          Evidence: HRMS manager history + org change event stream

  RC-06   TEAM_INSTABILITY
          Trigger: team_attrition_rate_90d > 0.20
          Evidence: org departure events + team composition change

  RC-07   BURNOUT_SIGNAL
          Trigger: pto_usage_rate < 0.30 + night_weekend_commit_rate > 0.45
          Evidence: PTO records + after-hours work signals

  RC-08   DISENGAGEMENT_TREND
          Trigger: habit.daily_active_days_30d < 5 + dojo.match_frequency_delta < -0.40
          Evidence: platform activity + Dojo engagement decline

  RC-09   EXTERNAL_MARKET_PULL
          Trigger: recruiter_profile_views_delta > 0.50 + external_group_join_rate > 0.30
          Evidence: profile views + external positioning signals

  RC-10   CULTURAL_WITHDRAWAL
          Trigger: company_group_delta < -0.40 + company_sentiment_posts < -0.30
          Evidence: social system + company community engagement

  RC-11   EQUITY_CLIFF_RISK
          Trigger: equity_cliff_months ≤ 3 + equity_vested_pct > 0.80
          Evidence: HRMS compensation records + vesting schedule

  RC-12   ENVIRONMENTAL_INSTABILITY
          Trigger: layoff_signal_flag = TRUE OR role_elimination_risk_score > 0.65
          Evidence: org signals + sector-level risk model

Output:
  root_cause_vector = Array[{ rc_id, confidence, contributing_signals[] }]
  Ranked by confidence score descending
  Maximum 5 root causes reported per prediction
  All root causes stored in RetentionPrediction object
```

---

### D7 — Layer 6: Confidence Calibration

```
Confidence Score Formula:
  confidence = α × signal_coverage_score
             + β × work.normalized_reliability_score
             + γ × dojo.score_confidence
             + δ × (1 / avg_signal_staleness_normalized)
             + ε × (1 - org.layoff_signal_flag ? 0 : 0.15)

  Where α + β + γ + δ + ε = 1.0
  Weights defined in model config — versioned — not mutable without MAJOR bump

Confidence Gate:
  confidence < 0.75  → SUPPRESSED
                        Emit: PREDICTION_LOW_CONFIDENCE
                        Route to: HR Review Queue
                        No employer-facing score emitted

  0.75 ≤ confidence < 0.88  → confidence_flag = MODERATE
                              Score emitted with disclaimer
                              HR advised to verify with qualitative input

  confidence ≥ 0.88  → confidence_flag = HIGH
                        Full score + risk tier + flight window emitted
                        No disclaimer required
```

---

### D8 — Layer 7: Intervention Recommendation Generator

```
Model:              LLM (Claude Sonnet — ECOSKILLER API integration, per Anthropic guidelines)
                    Used ONLY for this layer — cost justified for output quality
                    Input: root_cause_vector + risk_tier + flight_window + signal_summary
                    Output: intervention_recommendations Array[Object]

Per Recommendation Output:
  {
    intervention_id:          String (e.g., "INT-RC01-COMP"),
    target_root_cause:        String (RC-01 through RC-12),
    action_type:              Enum[COMPENSATION | PROMOTION | ROLE_CHANGE | RECOGNITION |
                                   MENTORSHIP | SCHEDULE | TEAM_MOVE | CULTURE | EQUITY |
                                   MANAGER_CHANGE | WELLNESS | EXTERNAL_OPPORTUNITY],
    recommended_action:       String (specific, actionable — 1–3 sentences),
    urgency:                  Enum[IMMEDIATE | WITHIN_30_DAYS | WITHIN_90_DAYS | QUARTERLY],
    expected_retention_lift:  Float: estimated retention probability score increase if actioned,
    evidence_summary:         String (plain-language signal summary — HR-readable),
    owner_role:               Enum[HR_ADMIN | DIRECT_MANAGER | COMPANY_ADMIN | COMPENSATION_TEAM | EMPLOYEE]
  }

Maximum 5 interventions per prediction.
Interventions ranked by expected_retention_lift descending.
LLM output validated against root_cause_vector before inclusion.
LLM cannot generate interventions unsupported by signal evidence.
```

---

## 🔒 SECTION E — PREDICTION VARIANT MATRIX (SEALED)

| Mode ID | Mode Name | Scope | Primary Consumer | Min Trust Gates |
|---------|-----------|-------|-----------------|-----------------|
| PM-01 | Individual Retention Score | Single employee + current role | HR Admin Dashboard | All TG-01 to TG-08 |
| PM-02 | Team Retention Heat Map | All employees in one team/department | HR Admin + Manager | TG-04 + TG-05 |
| PM-03 | Org-Wide Risk Distribution | All employees across company | Company Admin + CHRO | TG-04 + TG-05 |
| PM-04 | Role Cluster Risk Analysis | All employees in same role category | HR Admin + Talent Ops | TG-04 + TG-05 |
| PM-05 | New Hire 90-Day Risk Score | Employee < 90 days tenure | HR Admin | TG-01 to TG-06 |
| PM-06 | Post-Promotion Stability Score | Employee promoted in last 60 days | HR Admin + Manager | TG-01 to TG-06 |
| PM-07 | Recruiter ROI Proof Report | Retention outcomes for ECOSKILLER hires | Recruiter Dashboard | TG-04 + TG-07 |

**PM-01 is the base mode. All other modes are aggregations or slices of PM-01 outputs.**
**PM-07 generates the recruiter ROI proof: `"Hiring from ECOSKILLER gives X% better retention."` — verifiable and auditable.**

---

## 🔒 SECTION F — OUTPUT SCHEMA (SEALED)

```json
RetentionPrediction {
  prediction_id:              UUID,
  model_version:              "ARPE-v1.0.0",
  employee_id:                UUID (verified),
  company_id:                 UUID (verified),
  hr_admin_id:                UUID (authorized requester),
  generated_at:               ISO 8601 timestamp,
  prediction_mode:            Enum["PM-01" through "PM-07"],

  retention_probability_score: Float [0–100],
  risk_tier:                  Enum["STABLE" | "LOW_RISK" | "WATCH" | "AT_RISK" | "HIGH_RISK" | "CRITICAL"],
  flight_window:              Object {
    lower_bound_days:         Integer,
    upper_bound_days:         Integer,
    cliff_anchor_date:        ISO 8601 date | null   // if equity cliff is binding factor
  },

  confidence_score:           Float [0–1],
  confidence_flag:            Enum["HIGH" | "MODERATE" | "SUPPRESSED"],
  signal_coverage_score:      Float [0–1],

  root_cause_vector:          Array[{
    rc_id:                    String,
    rc_label:                 String,
    confidence:               Float,
    contributing_signals:     Array[String]
  }],

  intervention_recommendations: Array[{
    intervention_id:          String,
    target_root_cause:        String,
    action_type:              String,
    recommended_action:       String,
    urgency:                  String,
    expected_retention_lift:  Float,
    evidence_summary:         String,
    owner_role:               String
  }],

  signal_block_summary:       Object {
    dojo_coverage:            Boolean,
    habit_coverage:           Boolean,
    work_coverage:            Boolean,
    compensation_coverage:    Boolean,
    intelligence_coverage:    Boolean,
    championship_coverage:    Boolean,
    social_coverage:          Boolean,
    environmental_coverage:   Boolean,
    dojo_trust_weight:        Float,
    work_trust_weight:        Float,
    compensation_trust_weight: Float
  },

  override_flags:             Object {
    layoff_signal_override:   Boolean,
    internal_transfer_override: Boolean,
    external_application_override: Boolean,
    manager_departure_override: Boolean
  },

  audit_reference:            UUID,    // links to immutable RetentionAuditLog entry
  aspe_prediction_id:         UUID | null,  // linked salary prediction if used
  consent_reference:          UUID     // links to consent record
}
```

**Forbidden Output States:**

```
❌ Score emitted with confidence < 0.75 to employer/HR
❌ Output without audit_reference
❌ Output without root_cause_vector (minimum 1 root cause required)
❌ Output without intervention_recommendations (minimum 1 required if risk_tier ≠ STABLE)
❌ Output for unverified employee (TG-01 failure)
❌ Output for unverified company (TG-02 failure)
❌ Output for employee without consent (TG-08 failure)
❌ Output without signal_coverage_score
❌ Partial prediction without confidence_flag
❌ CRITICAL tier alert without immediate escalation event emission
```

---

## 🔒 SECTION G — ALERT & ESCALATION SYSTEM (SEALED)

The ARPE alert system is event-driven. Alerts are emitted automatically upon prediction generation and on score changes crossing tier boundaries.

### Alert Event Registry

```
ARPE_ALERT_CRITICAL:
  Trigger:    risk_tier = CRITICAL
  Delivery:   In-app push (HR Admin) + In-app push (Company Admin)
              Email (HR Admin) + Email (Company Admin)
  SLA:        Delivered within 60 seconds of prediction generation
  Content:    Employee ID (masked for privacy) + Risk Tier + Flight Window + Top Root Cause
  Escalation: If no HR acknowledgement within 4h → auto-escalate to Company Admin

ARPE_ALERT_HIGH_RISK:
  Trigger:    risk_tier = HIGH_RISK
  Delivery:   In-app push (HR Admin) + Dashboard badge
  SLA:        Delivered within 5 minutes
  Content:    Employee ID (masked) + Risk Tier + Top 2 Interventions

ARPE_ALERT_TIER_DEGRADATION:
  Trigger:    risk_tier worsens by 2+ tiers vs previous prediction
  Delivery:   In-app notification (HR Admin)
  SLA:        Delivered within 15 minutes

ARPE_ALERT_EQUITY_CLIFF:
  Trigger:    compensation.equity_cliff_months ≤ 3 AND risk_tier ≠ STABLE
  Delivery:   HR Admin dashboard flag + Compensation Team notification
  SLA:        Delivered within 24h of cliff window entry

ARPE_ALERT_CONTAGION_RISK:
  Trigger:    org.team_attrition_rate_90d > 0.20 AND 2+ AT_RISK employees in same team
  Delivery:   HR Admin + Manager (if manager linked)
  SLA:        Delivered daily via batch report

ARPE_ALERT_SUPPRESSED_PREDICTION:
  Trigger:    confidence < 0.75 → prediction suppressed
  Delivery:   HR Admin notification (internal — not employee-facing)
  Content:    Missing signal blocks + recommended data connection actions

Privacy Rule:
  All alerts use employee_id (masked display) not full name in automated channels.
  Full name visible only in authenticated HR Admin UI with audit trail.
```

---

## 🔒 SECTION H — API CONTRACT REGISTRY (SEALED)

All endpoints below are mandatory. Absence of any endpoint → STOP EXECUTION.

```
POST   /retention/predict
       Body:     { employee_id, prediction_mode }
       Auth:     HR Admin JWT (verified) | Company Admin JWT
       Gate:     All trust gates evaluated before processing
       Response: RetentionPrediction object

GET    /retention/prediction/{prediction_id}
       Auth:     HR Admin JWT | Company Admin JWT
       Response: Full RetentionPrediction object

GET    /retention/employee/{employee_id}/history
       Auth:     HR Admin JWT
       Response: Array[RetentionPrediction] — chronological history
                 (enables score trend visualization)

POST   /retention/batch/team
       Body:     { department_id | team_id, prediction_mode: "PM-02" }
       Auth:     HR Admin JWT
       Response: Array[RetentionPrediction] sorted by risk_tier severity desc

GET    /retention/org/{company_id}/risk-distribution
       Auth:     Company Admin JWT
       Mode:     PM-03 Org-Wide Risk Distribution
       Response: Aggregated risk tier counts + score histogram + top root causes

GET    /retention/role-cluster/{role_id}/analysis
       Auth:     HR Admin JWT
       Mode:     PM-04 Role Cluster Risk Analysis
       Response: Role-level risk aggregate + intervention recommendation set

POST   /retention/new-hire/predict
       Body:     { employee_id }  // tenure_months auto-checked ≤ 3
       Auth:     HR Admin JWT
       Mode:     PM-05 New Hire 90-Day Risk Score
       Response: RetentionPrediction with onboarding_risk_flag

GET    /retention/recruiter-roi/report
       Query:    { company_id, date_range }
       Auth:     Recruiter JWT (verified) | Company Admin JWT
       Mode:     PM-07 Recruiter ROI Proof
       Response: ECOSKILLER hire retention stats vs market benchmark

GET    /retention/audit/{prediction_id}
       Auth:     Governance Board JWT only
       Response: Full immutable audit record

POST   /retention/appeal
       Body:     { prediction_id, appeal_reason }
       Auth:     Employee JWT (own record only)
       Response: Appeal ticket ID → routes to Governance Board

POST   /retention/intervention/mark-actioned
       Body:     { prediction_id, intervention_id, actioned_by, action_notes }
       Auth:     HR Admin JWT | Manager JWT
       Response: Intervention action logged + re-prediction scheduled

GET    /retention/alert/stream
       Auth:     HR Admin JWT (SSE or WebSocket)
       Response: Real-time alert stream for authenticated HR Admin

GET    /retention/signal-coverage/{employee_id}
       Auth:     HR Admin JWT
       Response: Per-block signal coverage status + integration connection recommendations
```

---

## 🔒 SECTION I — DATABASE SCHEMA (SEALED)

Primary entities cannot be renamed. Fields may extend — not mutate.

```sql
-- Core prediction record
RetentionPrediction (
  prediction_id                   UUID PRIMARY KEY,
  model_version                   VARCHAR NOT NULL,
  employee_id                     UUID NOT NULL REFERENCES User(id),
  company_id                      UUID NOT NULL REFERENCES Tenant(id),
  hr_admin_id                     UUID NOT NULL REFERENCES User(id),
  generated_at                    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  prediction_mode                 VARCHAR NOT NULL,
  retention_probability_score     FLOAT NOT NULL CHECK (score BETWEEN 0 AND 100),
  risk_tier                       VARCHAR NOT NULL,
  flight_window_lower_days        INTEGER,
  flight_window_upper_days        INTEGER,
  flight_window_cliff_anchor      DATE,
  confidence_score                FLOAT NOT NULL,
  confidence_flag                 VARCHAR NOT NULL,
  signal_coverage_score           FLOAT NOT NULL,
  suppressed                      BOOLEAN DEFAULT FALSE,
  suppression_reason              VARCHAR,
  aspe_prediction_id              UUID REFERENCES SalaryPrediction(prediction_id),
  consent_reference               UUID NOT NULL,
  audit_reference                 UUID NOT NULL REFERENCES RetentionAuditLog(audit_id)
)

-- Root cause records per prediction
RetentionRootCause (
  root_cause_id                   UUID PRIMARY KEY,
  prediction_id                   UUID NOT NULL REFERENCES RetentionPrediction(prediction_id),
  rc_id                           VARCHAR NOT NULL,
  rc_label                        VARCHAR NOT NULL,
  confidence                      FLOAT NOT NULL,
  contributing_signals            JSONB NOT NULL,
  rank_position                   INTEGER NOT NULL
)

-- Intervention records per prediction
RetentionIntervention (
  intervention_id                 UUID PRIMARY KEY,
  prediction_id                   UUID NOT NULL REFERENCES RetentionPrediction(prediction_id),
  target_root_cause               VARCHAR NOT NULL,
  action_type                     VARCHAR NOT NULL,
  recommended_action              TEXT NOT NULL,
  urgency                         VARCHAR NOT NULL,
  expected_retention_lift         FLOAT,
  evidence_summary                TEXT NOT NULL,
  owner_role                      VARCHAR NOT NULL,
  actioned                        BOOLEAN DEFAULT FALSE,
  actioned_by                     UUID REFERENCES User(id),
  actioned_at                     TIMESTAMPTZ,
  action_notes                    TEXT,
  outcome_score_delta             FLOAT   -- populated on next prediction post-action
)

-- Signal snapshot per prediction (immutable evidence)
RetentionSignalSnapshot (
  snapshot_id                     UUID PRIMARY KEY,
  prediction_id                   UUID NOT NULL REFERENCES RetentionPrediction(prediction_id),
  signal_block                    VARCHAR NOT NULL,
  signal_key                      VARCHAR NOT NULL,
  signal_value                    JSONB NOT NULL,
  trust_weight                    FLOAT NOT NULL,
  staleness_hours                 FLOAT NOT NULL,
  captured_at                     TIMESTAMPTZ NOT NULL
)

-- Immutable audit log (append-only enforced)
RetentionAuditLog (
  audit_id                        UUID PRIMARY KEY,
  prediction_id                   UUID NOT NULL,
  event_type                      VARCHAR NOT NULL,
  actor_id                        UUID,
  actor_role                      VARCHAR,
  event_payload                   JSONB NOT NULL,
  created_at                      TIMESTAMPTZ NOT NULL DEFAULT NOW()
  -- NO UPDATE OR DELETE PERMITTED AT ANY ROLE LEVEL
)

-- Employee consent registry
RetentionConsentRecord (
  consent_id                      UUID PRIMARY KEY,
  employee_id                     UUID NOT NULL REFERENCES User(id),
  company_id                      UUID NOT NULL REFERENCES Tenant(id),
  consent_type                    VARCHAR DEFAULT 'retention_analysis',
  granted_at                      TIMESTAMPTZ NOT NULL,
  revoked_at                      TIMESTAMPTZ,
  active                          BOOLEAN NOT NULL DEFAULT TRUE,
  consent_version                 VARCHAR NOT NULL
)

-- Appeal records
RetentionAppeal (
  appeal_id                       UUID PRIMARY KEY,
  prediction_id                   UUID NOT NULL REFERENCES RetentionPrediction(prediction_id),
  employee_id                     UUID NOT NULL,
  appeal_reason                   TEXT NOT NULL,
  status                          VARCHAR DEFAULT 'PENDING',
  governance_decision             TEXT,
  resolved_at                     TIMESTAMPTZ,
  created_at                      TIMESTAMPTZ NOT NULL DEFAULT NOW()
)

-- Alert dispatch log
RetentionAlertLog (
  alert_id                        UUID PRIMARY KEY,
  prediction_id                   UUID NOT NULL REFERENCES RetentionPrediction(prediction_id),
  alert_type                      VARCHAR NOT NULL,
  recipient_id                    UUID NOT NULL,
  recipient_role                  VARCHAR NOT NULL,
  delivered_at                    TIMESTAMPTZ,
  acknowledged_at                 TIMESTAMPTZ,
  escalated                       BOOLEAN DEFAULT FALSE
)
```

**Row-level security enforced on all tables.**
**Tenant isolation: no cross-company data access permitted under any role.**
**RetentionAuditLog: append-only — DB constraint enforced. No UPDATE or DELETE.**
**RetentionSignalSnapshot: write-once. No modification after prediction finalization.**

---

## 🔒 SECTION J — UI SCREEN CONTRACT (SEALED)

All screens mandatory. Absence → STOP EXECUTION.

### Flutter (Operational Interface — HR Admin + Company Admin)

```
RetentionDashboardScreen (PM-03 overview)
  - Company-wide risk tier distribution (donut chart: STABLE / LOW_RISK / WATCH / AT_RISK / HIGH_RISK / CRITICAL)
  - Total employees per tier
  - Month-over-month tier migration chart (flow diagram)
  - Top 5 highest-risk employees (masked IDs unless HR Admin expands)
  - Department risk heat map (color-coded grid)
  - Active unacknowledged alerts count (badge)
  - [ RUN ORG-WIDE SCAN ] trigger button

IndividualRetentionScoreScreen (PM-01)
  - Employee selector (verified employees only)
  - Trust gate status strip (TG-01 to TG-08 visual indicators)
  - [ GENERATE SCORE ] button → disabled if TG-07 coverage < 0.40 or consent absent
  - Retention probability score display (gauge 0–100 + tier badge)
  - Flight window display: "Expected retention window: 3–6 months"
  - Root cause cards (max 5, ranked, each expandable with contributing signals)
  - Intervention recommendation list (ranked by expected_retention_lift)
    → Each with urgency badge + [ MARK ACTIONED ] button
  - Signal block coverage status panel (accordion: which blocks connected, which missing)
  - Historical score trend line chart (last 6 predictions)
  - [ DOWNLOAD PDF REPORT ] button
  - Appeal status banner (if active)
  - Confidence badge: HIGH / MODERATE / SUPPRESSED

TeamRetentionHeatMapScreen (PM-02)
  - Department / Team selector
  - Employee grid: color-coded by risk_tier
  - Bulk [ RUN TEAM SCAN ] trigger
  - Contagion risk alert banner (if team_attrition_rate_90d > 0.20)
  - Sort by: risk_tier | score | flight_window | tenure
  - Drill-down → IndividualRetentionScoreScreen

RetentionAlertCenterScreen
  - Real-time alert feed (SSE-connected)
  - Filter: ALL | CRITICAL | HIGH_RISK | EQUITY_CLIFF | CONTAGION
  - Alert card: employee (masked) + tier + alert type + timestamp + [ ACKNOWLEDGE ] action
  - Escalation status indicator
  - Alert history (30-day log)

InterventionTrackerScreen
  - All open interventions across company (table view)
  - Filter by: owner_role | urgency | action_type | status
  - Columns: Employee | Root Cause | Intervention | Urgency | Owner | Status | Expected Lift
  - [ MARK ACTIONED ] inline
  - Outcome tracker: delta score after next prediction (populated automatically)

SignalCoverageStudioScreen
  - Per-employee: signal block connection status
  - Integration gap: which tools not connected → direct links to Integration Engine (Module 9)
  - Coverage score display
  - Bulk coverage report for all employees
  - Recommended integrations to improve coverage (prioritized by coverage impact)

RecruiterROIProofScreen (PM-07)
  - ECOSKILLER hire vs non-ECOSKILLER hire retention comparison
  - Date range selector
  - Retention rate: ECOSKILLER sourced vs market average
  - Risk tier distribution comparison
  - Average retention score: ECOSKILLER hires vs general population
  - Exportable PDF → used in recruiter sales deck

NewHireRiskMonitorScreen (PM-05)
  - Employees with tenure ≤ 90 days
  - Onboarding completion status (from HRMS)
  - Early retention score
  - 30-day / 60-day / 90-day milestone markers
  - Alert if score < 55 at Day 30
```

### Next.js (SEO / Public-Facing — Candidate-Facing Only)

```
CandidateRetentionInsightPage (Read-Only — Masked)
  - Employee-facing summary: "Your workplace stability score" (no raw score exposed)
  - Growth path indicator: Dojo belt progression impact on retention strength
  - Platform tip: "Increase your engagement to strengthen your employability signal"
  - Call to action: "Start a skill match today"
  - NO raw retention_probability_score exposed publicly
  - NO root causes exposed publicly
  - NO employer-specific data exposed
```

---

## 🔒 SECTION K — INTEGRATION BINDING (SEALED)

No manual data sync permitted. Event-driven only. Per ECOSKILLER Section P6 + R37 mandate.

```
Dojo Match End Event              → triggers: ARPE dojo signal block refresh
Dojo Belt Promotion Event         → triggers: ARPE re-prediction flag (belt_stall reset)
Dojo Drill Completion/Abandon     → triggers: ARPE habit + dojo signal update
DailyActivity Logged              → triggers: ARPE habit block refresh
StreakTracker Updated             → triggers: ARPE habit block refresh
EIE Tool Sync Complete            → triggers: ARPE work signal block refresh (per tool)
EIE HRMS Sync (BambooHR/Workday)  → triggers: ARPE environmental + compensation block refresh
ASPE Prediction Generated         → triggers: ARPE compensation gap recalculation
ASPE Salary Band Updated          → triggers: ARPE compensation signal refresh
Championship Result Posted         → triggers: ARPE championship block refresh
Social Post Created/Deleted       → triggers: ARPE social signal recalculation (async, 6h delay)
Company Org Change Event          → triggers: ARPE environmental block refresh
Manager Change Event              → triggers: ARPE org.manager_departure_flag evaluation
Team Member Departure Event       → triggers: ARPE org.team_attrition_rate recalculation
Internal Transfer Request Created → triggers: ARPE override flag activation
Fraud Flag Set                    → triggers: ARPE prediction suppression
Consent Revoked                   → triggers: ARPE prediction suppression + active alert cancellation
Appeal Resolved                   → triggers: RetentionAuditLog append + employee notification
Intervention Marked Actioned      → triggers: Re-prediction scheduled (72h delay minimum)
CRITICAL alert unacknowledged 4h  → triggers: Auto-escalation to Company Admin
```

---

## 🔒 SECTION L — PRIVACY, CONSENT & ETHICS LOCK

```
Consent Requirements:
  Employee must grant retention_analysis_consent before ANY prediction runs
  Consent is explicit, informed, and versioned (consent_version in schema)
  Consent UI must display: what signals are collected, who sees the score, appeal rights
  Consent revocable at any time → immediate prediction suppression
  Revocation logged in RetentionAuditLog (immutable)

Employee Rights:
  RIGHT TO VIEW:      Employee may view their own intervention_recommendations + improvement_path
  RIGHT TO APPEAL:    Employee may appeal any prediction via POST /retention/appeal
  RIGHT TO ERASURE:   Triggers: prediction suppression + anonymization of signal snapshots
                      Audit record retained (anonymized) per compliance requirements
  RIGHT TO EXPORT:    Employee may export their own signal snapshot history
  NO RIGHT TO VIEW:   Raw retention_probability_score (score is HR-facing only)
                      Root cause vector (HR-facing only — employee sees improvement path only)

Score Visibility Rules:
  HR Admin:           Full score + risk tier + root causes + interventions + signal snapshots
  Company Admin:      Aggregated tier distribution + flagged individuals (masked) + org metrics
  Direct Manager:     Risk tier + assigned interventions (no score or root causes)
  Employee:           Improvement path + Dojo engagement signals + consent status only
  Recruiter (PM-07):  Aggregated retention statistics only — no individual scores

Prohibited Signal Uses:
  ❌ No demographic inference from signal blocks
  ❌ No signals from monitoring employee communications content (only frequency/volume metadata)
  ❌ No signals used for compensation reduction decisions (retention model is for retention — not penalty)
  ❌ No prediction used as sole basis for termination
  ❌ No cross-tenant signal sharing

Ethics Review:
  Monthly ethics audit by Governance Board
  Bias audit: prediction variance by tenure band + role category must not exceed defined thresholds
  Ethics audit report published internally to Company Admin on request
```

---

## 🔒 SECTION M — SECURITY LOCK

```
Auth:
  All ARPE API routes require valid JWT
  Role enforcement: per-route RBAC (hr_admin | company_admin | employee | manager | recruiter | governance)
  HR Admin routes: require MFA verification (per R40 Internal Admin Console)
  Rate limiting: per IP + per user per endpoint
  WAF enforcement: required (inherited from ECOSKILLER P1)
  CRITICAL alert acknowledgement: requires active session (no email-only confirmation)

Data Protection:
  RetentionPrediction: PII-classified → encrypted at rest (per ECOSKILLER Section P1)
  RetentionSignalSnapshot: encrypted at rest + tenant-isolated
  RetentionAuditLog: append-only enforced at DB constraint level
  No cross-tenant data access under any role or scenario
  Signal data segregated from core ECOSKILLER data store (separate schema namespace)

Audit Trail:
  Every prediction generation: logged (immutable)
  Every alert delivery: logged
  Every intervention action: logged
  Every score view by HR/Manager: logged (access audit)
  Every consent grant/revoke: logged
  Every appeal action: logged
  All logs → RetentionAuditLog (append-only — no modification permitted)

Retention Policy:
  Active predictions: retained 36 months post-employment
  Signal snapshots: anonymized after 24 months
  Audit logs: retained 7 years (compliance standard)
  Consent records: retained 7 years after revocation
```

---

## 🔒 SECTION N — TEST GATE LOCK

No deployment permitted without all test layers passing. (Per ECOSKILLER Section P4 + R14)

```
Unit Tests:
  - Confidence calibration formula: all weight permutations
  - Trust gate evaluation: all 8 gates, all failure paths, all combinations
  - Risk tier classification: boundary values (exact threshold testing)
  - Override flag logic: all 4 override conditions
  - Signal normalization: edge cases (zero, null, max, negative)
  - Staleness decay function: boundary conditions
  - Root cause trigger conditions: all 12 RC categories

Integration Tests:
  - End-to-end: employee_id → RetentionPrediction object (PM-01)
  - Trust gate failure → suppression verified → audit log entry confirmed
  - ASPE compensation signal dependency: ASPE unavailable → graceful degradation
  - Alert delivery: CRITICAL tier → alert emitted within 60s
  - Consent revoke → prediction suppression within 60s
  - Intervention mark-actioned → re-prediction scheduled
  - HRMS sync → signal block updated → prediction flag set
  - Cross-tenant isolation: company A request cannot access company B employee

Regression Tests:
  - Known employee profiles → predictions within ±5% of v1.0.0 baseline
  - Override flags: layoff_signal → score capped at 35 (verified)
  - Team attrition contagion: ARPE_ALERT_CONTAGION_RISK emitted correctly

Load Tests:
  - 1,000 concurrent PM-01 predictions: < 3s p99 response
  - PM-03 org-wide scan: 10,000 employees → complete within 10 minutes
  - Real-time alert stream: 500 concurrent HR Admin SSE connections stable

Fairness Tests (Mandatory — per ECOSKILLER T12 equivalent):
  - Prediction variance across verified regions: must not exceed defined bias threshold
  - Tenure band bias check: new hire vs senior employee score distribution
  - Role category bias check: no single role category systematically over-scored
  - Gender / demographic proxy signal check: no demographic proxy signals in model input
  - Monthly bias audit report generated by governance board

Privacy Tests:
  - Employee consent absent → prediction blocked (all modes)
  - Employee right-to-erasure flow: signal anonymization confirmed
  - Cross-role visibility: employee JWT → cannot access raw score (verified)
  - Manager JWT → cannot access root_cause_vector (verified)

Coverage threshold required before deployment authorization.
```

---

## 🔒 SECTION O — OBSERVABILITY LOCK

(Per ECOSKILLER Section P5 + R16 — Observability Required)

```
Required Metrics:
  arpe.prediction.generated             → Counter per prediction_mode
  arpe.prediction.suppressed            → Counter (confidence < 0.75)
  arpe.trust_gate.failure               → Counter per gate_id
  arpe.confidence_score.histogram       → Distribution
  arpe.risk_tier.distribution           → Gauge per tier (STABLE → CRITICAL count)
  arpe.signal_coverage.histogram        → Distribution of coverage scores
  arpe.signal_staleness.gauge           → Per signal block
  arpe.alert.delivered                  → Counter per alert_type
  arpe.alert.acknowledged               → Counter per alert_type
  arpe.alert.escalated                  → Counter
  arpe.intervention.actioned            → Counter per action_type
  arpe.model.inference_latency_ms       → Histogram
  arpe.appeal.open_count                → Gauge
  arpe.consent.active_count             → Gauge
  arpe.consent.revoked_30d             → Counter

Dashboards Required:
  - Prediction volume + suppression rate (daily)
  - Trust gate failure breakdown (per gate)
  - Risk tier distribution trends (monthly cohort view)
  - Alert delivery + acknowledgement rates
  - Intervention actioning rates + outcome delta tracking
  - Signal coverage distribution across employee base
  - Model inference latency (p50, p95, p99)
  - Contagion risk clusters (team-level heat map)
  - Recruiter ROI metrics (ECOSKILLER hire vs market retention)

Alerts (Operations-Level):
  - arpe.prediction.suppressed rate > 25% → OPS ALERT
  - arpe.alert.delivered failure rate > 5% → OPS ALERT
  - arpe.model.inference_latency_ms p99 > 5000 → OPS ALERT
  - arpe.trust_gate.failure (TG-05 fraud) spike → IMMEDIATE OPS ALERT
  - arpe.signal_staleness.gauge (any block) > 2× freshness policy → OPS WARNING
  - arpe.risk_tier.distribution CRITICAL count increase > 20% in 24h → OPS ALERT
```

---

## 🔒 SECTION P — GOVERNANCE & APPEALS LOCK

```
Appeals Authority:        ANTIGRAVITY Governance Board
Appeals Scope:            Employees disputing their retention signal or root cause classification
Appeal Trigger:           Employee submits via POST /retention/appeal
Review SLA:               72 hours from submission
Review Access:            Full RetentionAuditLog + RetentionSignalSnapshot + consent record
Decision Types:           UPHELD | MODIFIED | RETRACTED
Decision Storage:         Immutable append to RetentionAuditLog
Employee Notification:    Within 24h of governance decision

Model Retraining Authority:
  Declared by:            ANTIGRAVITY Technical Governance Board only
  Triggers:
    - Monthly scheduled cycle
    - Retention outcome validation drop below 0.70 AUC (per T13 equivalent)
    - Employer feedback: > 15 disputed predictions in 30-day window
    - Signal drift detected: > 20% delta from expected distribution
    - Ethics audit flags systematic bias (any protected proxy signal detected)

Root Cause Version Authority:
  RC category additions: MINOR version bump required
  RC category removal or modification: MAJOR version bump required
  Governance Board approval required for all changes

Fairness Review:
  Monthly bias audit published internally
  Systematic bias discovered → prediction suspension for affected cohort pending remediation
  Remediation → test gate re-run → Governance Board re-authorization → resume
```

---

## 🔒 SECTION Q — RETENTION SCORE TREND ANALYTICS (SEALED)

The ARPE produces not just point-in-time scores but trend analytics for org-level intelligence.

```
Trend Objects Generated Per Org (Monthly Batch):

  OrgRetentionTrend {
    company_id:                     UUID,
    report_month:                   Date,
    avg_retention_score:            Float,
    median_retention_score:         Float,
    tier_migration_matrix:          Object {
      // Count of employees who moved between tiers month-over-month
      stable_to_low_risk:           Integer,
      low_risk_to_watch:            Integer,
      watch_to_at_risk:             Integer,
      at_risk_to_high_risk:         Integer,
      high_risk_to_critical:        Integer,
      // Positive migrations
      at_risk_to_watch:             Integer,
      high_risk_to_at_risk:         Integer,
      critical_to_high_risk:        Integer
    },
    top_root_causes_month:          Array[{ rc_id, frequency }],  // top 5 by frequency
    intervention_effectiveness:     Array[{
      action_type:                  String,
      actioned_count:               Integer,
      avg_score_delta:              Float,   // score improvement after intervention
      effectiveness_rating:         String   // HIGH | MODERATE | LOW
    }],
    department_risk_ranking:        Array[{ department_id, avg_score, at_risk_count }],
    new_hire_30d_retention_rate:    Float,
    ecoskiller_hire_retention_rate: Float,   // PM-07 proof metric
    market_retention_benchmark:     Float    // industry average for comparison
  }
```

---

## 🔒 SECTION R — VERSIONING & MUTATION GOVERNANCE LOCK

```
Version Format:           ARPE-vMAJOR.MINOR.PATCH
  MAJOR bump required:    Root cause category change / Trust gate restructure /
                          Schema mutation (destructive) / Privacy model change /
                          Model architecture change / Confidence formula weight change
  MINOR bump required:    New signal block added / New risk tier / New alert type /
                          New prediction mode / Intervention category expansion
  PATCH bump permitted:   Bug fix / Monitoring update / LLM prompt update for explainability /
                          Non-structural schema field addition

Allowed without version bump:
  - Add new integration tool signal (additive to existing block)
  - Update observability dashboards
  - Expand intervention recommendation library (LLM prompt additive change)
  - Update alert SLA thresholds (non-breaking)

NOT allowed without MAJOR version bump:
  - Change trust gate logic or count
  - Change root cause trigger conditions
  - Change risk tier boundary values
  - Change output schema structure (destructive)
  - Change confidence formula weights
  - Change privacy visibility rules

Backward Compatibility Window:
  MAJOR version: 6-month parallel operation before retirement
  MINOR version: 3-month parallel operation
  PATCH version: Immediate cutover permitted after test gate pass

Version Tag Required On:
  - Model artifact
  - Every RetentionPrediction output (model_version field)
  - Every RetentionAuditLog entry
  - Root cause RC registry
  - Confidence weight config file
  - Consent record (consent_version — tracks which privacy policy version employee agreed to)
```

---

## 🔒 SECTION S — RECRUITER ROI PROOF ENGINE (SEALED)

Per the Talent Operating System strategic imperative:
> *"Hiring from Ecoskiller gives 40% better retention."*

This section seals the mechanism for generating that proof — verifiable, auditable, and recruiter-facing.

```
ROI Proof Calculation (PM-07 Mode):

  Input:
    - company_id (verified)
    - date_range (e.g., last 12 months)
    - cohort_tag: "ecoskiller_sourced" (from hiring pipeline data)

  Calculation:
    ecoskiller_avg_retention_score = avg(retention_probability_score)
      WHERE employee.sourced_from = 'ECOSKILLER'
      AND prediction_mode = 'PM-01'
      AND prediction_date WITHIN date_range

    market_avg_retention_score = avg(retention_probability_score)
      WHERE employee.sourced_from != 'ECOSKILLER'
      AND prediction_mode = 'PM-01'
      AND prediction_date WITHIN date_range

    retention_uplift_pct = (ecoskiller_avg - market_avg) / market_avg × 100

    critical_risk_rate_ecoskiller = count(CRITICAL) / total_ecoskiller_employees
    critical_risk_rate_market = count(CRITICAL) / total_market_employees

    time_to_critical_avg_ecoskiller = avg(days from hire to first CRITICAL tier)
    time_to_critical_avg_market = avg(days from hire to first CRITICAL tier)

  Output: RecruiterROIReport {
    company_id, date_range,
    ecoskiller_hire_count, market_hire_count,
    ecoskiller_avg_retention_score, market_avg_retention_score,
    retention_uplift_pct,
    critical_risk_rate_ecoskiller, critical_risk_rate_market,
    time_to_critical_avg_ecoskiller, time_to_critical_avg_market,
    top_retention_drivers_ecoskiller: Array[String],  // root causes with lower frequency
    audit_reference: UUID
  }

  Audit:
    Every ROI report generation logged in RetentionAuditLog (immutable)
    Report reproducible: same inputs → same output (deterministic)
    Report downloadable as signed PDF for recruiter sales use

  Minimum Sample Requirement:
    Minimum 10 ECOSKILLER-sourced employees required for report generation
    Minimum 10 market-sourced employees required for valid comparison
    If minimum not met → REPORT_INSUFFICIENT_SAMPLE emitted (no partial report)
```

---

## 🔒 FINAL GOVERNANCE SEAL BLOCK

```
═══════════════════════════════════════════════════════════════════════════════
ANTIGRAVITY RETENTION PROBABILITY ENGINE — SEAL v1.0.0
═══════════════════════════════════════════════════════════════════════════════

ENTERPRISE OPTIMIZATION MODE:                    ACTIVE
TRUST INFRASTRUCTURE:                            LOCKED
ECOSKILLER SIGNAL INTEGRATION (8 BLOCKS):        REQUIRED
DOJO ENGAGEMENT SIGNAL:                          REQUIRED
PLATFORM HABIT SIGNAL:                           REQUIRED
REAL WORK DATA SIGNAL (EIE — 100 TOOLS):         REQUIRED
COMPENSATION GAP SIGNAL (ASPE LINKED):           REQUIRED
INTELLIGENCE GROWTH SIGNAL:                      REQUIRED
CHAMPIONSHIP VISIBILITY SIGNAL:                  REQUIRED
SOCIAL NETWORK SIGNAL:                           REQUIRED
ENVIRONMENTAL ORG SIGNAL:                        REQUIRED
GRADIENT BOOSTED MODEL (COST-OPTIMIZED R28):     ENFORCED
LLM LAYER (EXPLAINABILITY ONLY):                 SCOPED
CONFIDENCE GATE (0.75):                          ENFORCED
RISK TIER CLASSIFICATION (6 TIERS):              ENFORCED
FLIGHT WINDOW ESTIMATION:                        REQUIRED
ROOT CAUSE VECTOR (12 CATEGORIES):               MANDATORY
INTERVENTION RECOMMENDATION ENGINE:             MANDATORY
ALERT & ESCALATION SYSTEM:                       REQUIRED
EMPLOYEE CONSENT (PREREQUISITE):                 ENFORCED
AUDIT LOG (IMMUTABLE — APPEND ONLY):             REQUIRED
APPEALS SYSTEM:                                  ACTIVE
GOVERNANCE BOARD AUTHORITY:                      REQUIRED
FAIRNESS BIAS AUDIT (MONTHLY):                   MANDATORY
PRIVACY VISIBILITY RULES:                        LOCKED
RECRUITER ROI PROOF ENGINE (PM-07):              REQUIRED
RETENTION TREND ANALYTICS:                       REQUIRED
PII ENCRYPTION:                                  REQUIRED
RBAC ENFORCEMENT (PER ROLE):                     REQUIRED
TENANT ISOLATION:                                REQUIRED
TEST GATE (ALL LAYERS):                          REQUIRED BEFORE DEPLOY
VERSIONED MUTATION ONLY:                         ENFORCED
INTERPRETATION AUTHORITY:                        NONE
ARCHITECTURE AUTHORITY:                          LOCKED

ASPE DEPENDENCY:          ARPE requires ASPE-v1.0.0 for compensation gap signal
                          ASPE must be deployed and operational before ARPE v1.0.0

Absence of ANY above component
→ STOP EXECUTION
→ REPORT ARPE-INCOMPLETE
→ NO DEPLOYMENT CLAIM PERMITTED

═══════════════════════════════════════════════════════════════════════════════
ANTIGRAVITY ARPE-v1.0.0 | ECOSKILLER UNIFIED PLATFORM | SEALED
Issued Under: ECOSKILLER Master Execution Prompt v12.0
              Dojo SaaS Production Artifact Spec v1.0
              Talent Operating System Blueprint v1.0
Sibling: ANTIGRAVITY Salary Prediction Engine ASPE-v1.0.0
═══════════════════════════════════════════════════════════════════════════════
```

---

*Document Class: Sealed Production Prompt Artifact*
*Mutation Policy: Add-Only via Version Bump*
*Interpretation Authority: NONE*
*Architecture Authority: LOCKED*
*Prepared For: ANTIGRAVITY Enterprise Build Team*
*Version: ARPE-v1.0.0*
*Paired With: ASPE-v1.0.0 (Salary Prediction Model)*
