# 🔒 ANTIGRAVITY — INSTITUTE REVENUE FORECAST MODEL
## INSTITUTE + HR PREDICTIVE SYSTEMS · MACHINE LEARNING MODULE
### ECOSKILLER DOJO SAAS — SEALED PRODUCTION ARTIFACT v1.0

```
ARTIFACT CLASS          : ML Predictive System — Institute Revenue Intelligence
PARENT MODULE           : ANTIGRAVITY (Curriculum Gap Detector v1.0 — Sealed)
MUTATION POLICY         : Add-only via version bump
EXECUTION MODE          : Deterministic · Sealed · Governed
CREATIVE INTERPRETATION : FORBIDDEN
ASSUMPTION FILLING      : FORBIDDEN
DEFAULT BEHAVIOR        : DENY
FAILURE MODE            : STOP_EXECUTION
SYSTEM AUTHORITY        : LOCKED — Human declaration only
DEPENDENCY LOCK         : Requires ANTIGRAVITY Curriculum Gap Detector v1.0 ACTIVE
```

---

## ⚙️ SYSTEM IDENTITY BLOCK

```
MODULE NAME             : ANTIGRAVITY — Institute Revenue Forecast Model
MODULE CLASS            : Institute Financial Predictive Intelligence + ML Revenue Engine
PARENT PLATFORM         : ECOSKILLER (Unified Talent Operating System)
PARENT MODULE           : ANTIGRAVITY — Curriculum Gap Detector v1.0
DOJO INTEGRATION        : Full Dojo SaaS Engine Compliance Required
EXECUTION SCOPE         : Institute ERP + Billing Engine + HR Predictive + Revenue ML
DOMAIN SCOPE            : All Tracks — Arts · Commerce · Science · Technology · Administration
STACK LOCK              : Flutter (Operational) + React/Next.js (SEO) — INHERITED
AI ROLE                 : Advisory + Forecast Only · No Financial Decision Authority
                          No Override Authority · No Auto-Billing · No Auto-Contract
BILLING ENGINE          : Inherits ECOSKILLER Billing Engine (Section P2) — LOCKED
```

---

# 🔒 SECTION 1 — MODULE SEAL DECLARATION

```
ANTIGRAVITY INSTITUTE REVENUE FORECAST MODEL = ENABLED

Revenue Prediction Engine           = REQUIRED
Enrollment Forecast ML              = REQUIRED
Placement-Linked Revenue Tracking   = REQUIRED
Curriculum Gap → Revenue Impact     = REQUIRED
Cohort Monetization Modeling        = REQUIRED
Subscription + Fee Forecast         = REQUIRED
Churn Prediction Engine             = REQUIRED
Institute Tier Scoring              = REQUIRED
HR Demand → Revenue Signal          = REQUIRED
Governance Board Financial Reports  = REQUIRED
Audit Trail                         = IMMUTABLE
Auto-Billing Decision               = FORBIDDEN
Auto-Contract Generation            = FORBIDDEN
Cross-Tenant Revenue Exposure       = FORBIDDEN
Architecture Interpretation         = FORBIDDEN
Creative Financial Assumption       = FORBIDDEN
```

---

# 🔒 SECTION 2 — WHAT THIS MODULE DOES (SYSTEM DEFINITION)

**ANTIGRAVITY Institute Revenue Forecast Model** is the ML-powered financial intelligence layer embedded within ECOSKILLER's Institute + HR Predictive System — operating as a child module of the ANTIGRAVITY Curriculum Gap Detector.

Its singular operational mandate is:

> **Forecast, model, explain, and govern the revenue trajectory of every institute operating on the ECOSKILLER platform — by fusing curriculum gap intelligence, student enrollment signals, placement outcomes, Dojo belt performance, HR market demand, and billing lifecycle data into a unified, auditable, predictive revenue engine.**

This module answers three core revenue intelligence questions at all times:

```
QUESTION 1 — FUTURE REVENUE
  What will this institute earn from ECOSKILLER-linked activities
  in the next 30 / 60 / 90 / 180 / 365 days?

QUESTION 2 — REVENUE RISK
  Which curriculum gaps, churn signals, placement failures, or
  market demand shifts are creating revenue risk right now —
  and by how much?

QUESTION 3 — REVENUE OPPORTUNITY
  Which untapped enrollment pools, skill tracks, belt certifications,
  or HR hiring pipelines represent the highest-probability
  revenue growth paths for this institute?
```

---

# 🔒 SECTION 3 — REVENUE ARCHITECTURE: FIVE FORECAST PLANES

```
PLANE 1 — DIRECT TUITION & ENROLLMENT REVENUE
  Source: Course enrollments, program fees, seat purchases
  Forecast Driver: Enrollment velocity, cohort fill rate, dropout prediction

PLANE 2 — PLACEMENT & HIRING SUCCESS REVENUE
  Source: Placement fees, hiring pipeline commissions, recruiter access fees
  Forecast Driver: Student placement probability score, employer demand signal,
                   belt validity score from ANTIGRAVITY Curriculum Gap Detector

PLANE 3 — DOJO BELT & CERTIFICATION REVENUE
  Source: Belt certification fees, re-certification cycles, mentor licensing
  Forecast Driver: Match completion rates, belt eligibility queues, mentor capacity

PLANE 4 — PLATFORM SUBSCRIPTION & SEAT REVENUE
  Source: Institute subscription tier, seat-based pricing, feature-gated access
  Forecast Driver: Seat utilization rate, churn prediction, plan upgrade probability

PLANE 5 — CURRICULUM GAP CORRECTION REVENUE OPPORTUNITY
  Source: ANTIGRAVITY gap signals → remediation course sales, upskill track purchases,
          mentor session bookings triggered by gap detection
  Forecast Driver: Gap severity score, gap type, student count affected per gap
```

---

# 🔒 SECTION 4 — DATA INPUT CONTRACTS (MANDATORY)

All input data streams are locked. Absence of any stream → STOP EXECUTION.

## 4A. Enrollment & Fee Inputs

```
ENROLLMENT_REVENUE_LOG
  Fields:
    log_id
    institute_id
    student_id
    course_id
    program_track
    enrollment_date
    fee_amount
    fee_currency
    payment_status (PAID | PENDING | FAILED | REFUNDED)
    installment_flag (BOOLEAN)
    installment_schedule_json
    discount_applied
    coupon_id (nullable)

COURSE_PRICING_REGISTRY
  Fields:
    course_id
    institute_id
    base_fee
    currency
    pricing_model (FLAT | PER_SEAT | SUBSCRIPTION | OUTCOME_LINKED)
    outcome_linked_trigger (nullable) — e.g. "placement confirmed"
    last_updated_at
    version

COHORT_FILL_SNAPSHOT
  Fields:
    cohort_id
    institute_id
    course_id
    max_seats
    enrolled_count
    waitlist_count
    snapshot_date
    dropout_count_to_date
```

## 4B. Placement & Hiring Revenue Inputs

```
PLACEMENT_REVENUE_LOG
  Fields:
    placement_id
    institute_id
    student_id
    employer_id
    role_id
    placement_date
    placement_fee_type (FLAT | PERCENTAGE_CTC | DEFERRED)
    fee_amount
    fee_currency
    payment_received_date (nullable)
    payment_status
    ctc_offered (nullable)
    ctc_currency (nullable)

PLACEMENT_PROBABILITY_SCORE
  Fields:
    student_id
    institute_id
    score_date
    placement_probability (0.0–1.0)
    top_matching_roles[]
    expected_placement_window_days
    model_confidence

EMPLOYER_HIRING_PIPELINE
  Fields:
    pipeline_id
    employer_id
    institute_id
    open_roles_count
    skill_requirements[]
    expected_hire_date
    hiring_budget_range
    pipeline_status (ACTIVE | PAUSED | CLOSED)
```

## 4C. Dojo Belt & Certification Revenue Inputs

```
BELT_CERTIFICATION_REVENUE_LOG
  Fields:
    cert_revenue_id
    institute_id
    student_id
    belt_id
    belt_level
    certification_fee
    currency
    payment_date
    payment_status
    re_certification_flag (BOOLEAN)

BELT_ELIGIBILITY_QUEUE
  Fields:
    queue_id
    institute_id
    student_id
    belt_id
    eligibility_date
    projected_certification_date
    fee_expected
    governance_block_flag (from ANTIGRAVITY Gap Detector)

MENTOR_LICENSE_REVENUE_LOG
  Fields:
    license_id
    institute_id
    mentor_id
    license_tier
    license_fee
    billing_cycle (MONTHLY | ANNUAL)
    renewal_date
    status (ACTIVE | LAPSED | SUSPENDED)
```

## 4D. Platform Subscription Revenue Inputs

```
INSTITUTE_SUBSCRIPTION_LOG
  Fields:
    subscription_id
    institute_id
    plan_tier (STARTER | GROWTH | ENTERPRISE | CUSTOM)
    seat_count
    monthly_fee
    annual_fee
    billing_cycle
    start_date
    renewal_date
    auto_renew_flag
    churn_risk_score (0.0–1.0)
    last_login_activity_date
    feature_utilization_rate (0.0–1.0)

SEAT_UTILIZATION_SNAPSHOT
  Fields:
    snapshot_id
    institute_id
    subscription_id
    allocated_seats
    active_seats
    dormant_seats
    snapshot_date

PLAN_UPGRADE_EVENT_LOG
  Fields:
    event_id
    institute_id
    from_plan
    to_plan
    upgrade_date
    revenue_delta
    trigger_reason
```

## 4E. ANTIGRAVITY Gap Signal → Revenue Impact Inputs

```
GAP_REVENUE_IMPACT_SIGNAL
  Fields:
    signal_id
    gap_item_id (FK → curriculum_gap_item from Gap Detector)
    institute_id
    affected_student_count
    gap_severity
    gap_type
    estimated_remediation_revenue_opportunity
    estimated_revenue_at_risk
    signal_date

REMEDIATION_PURCHASE_LOG
  Fields:
    purchase_id
    institute_id
    student_id
    remediation_type (COURSE | DRILL_PACK | MENTOR_SESSION | TRACK_UPGRADE)
    triggered_by_gap_id (FK → gap_item_id)
    purchase_amount
    purchase_date
    payment_status
```

## 4F. HR Market Demand Revenue Inputs

```
MARKET_DEMAND_REVENUE_SIGNAL
  Fields:
    signal_id
    skill_id
    demand_weight
    market_velocity (RISING | STABLE | DECLINING | OBSOLETE)
    employer_count_hiring
    avg_salary_offered
    region
    signal_date

RECRUITER_ACCESS_REVENUE_LOG
  Fields:
    access_id
    recruiter_id
    employer_id
    institute_id
    access_tier (BASIC | PREMIUM | ENTERPRISE)
    access_fee
    billing_cycle
    start_date
    renewal_date
    candidates_viewed_count
    hires_made_count
```

## 4G. Churn & Retention Inputs

```
CHURN_SIGNAL_LOG
  Fields:
    signal_id
    institute_id
    signal_type (LOGIN_DROP | SEAT_DECLINE | FEATURE_ABANDONMENT |
                 PLACEMENT_FAILURE | CURRICULUM_DISSATISFACTION |
                 COMPETITOR_MIGRATION | BILLING_DISPUTE)
    signal_strength (0.0–1.0)
    signal_date
    revenue_at_risk

RETENTION_INTERVENTION_LOG
  Fields:
    intervention_id
    institute_id
    intervention_type (OUTREACH | DISCOUNT_OFFERED | PLAN_DOWNGRADE |
                       FEATURE_UNLOCK | SUCCESS_MANAGER_ASSIGNED)
    triggered_at
    outcome (RETAINED | CHURNED | PENDING)
    revenue_saved (nullable)
```

---

# 🔒 SECTION 5 — ML MODEL ARCHITECTURE (LOCKED)

## 5A. Revenue Forecast Models

```
MODEL_1: ENROLLMENT_REVENUE_FORECASTER
  Type          : Time-series Regression (Prophet + XGBoost ensemble)
  Input         : ENROLLMENT_REVENUE_LOG · COHORT_FILL_SNAPSHOT ·
                  COURSE_PRICING_REGISTRY · market_seasonality_signals
  Output        : enrollment_revenue_forecast_30d
                  enrollment_revenue_forecast_90d
                  enrollment_revenue_forecast_180d
                  enrollment_revenue_forecast_365d
                  confidence_interval_lower
                  confidence_interval_upper
                  cohort_fill_probability (0.0–1.0)
                  dropout_revenue_risk_amount
  Trigger       : Run weekly per institute + on-demand
  Retrain       : Monthly or on 500+ new enrollment events

MODEL_2: PLACEMENT_REVENUE_FORECASTER
  Type          : Probabilistic Regression (Monte Carlo simulation layer
                  over XGBoost placement score)
  Input         : PLACEMENT_PROBABILITY_SCORE · EMPLOYER_HIRING_PIPELINE ·
                  PLACEMENT_REVENUE_LOG · MARKET_DEMAND_REVENUE_SIGNAL ·
                  belt_validity_score (from ANTIGRAVITY Gap Detector MODEL_4)
  Output        : expected_placement_revenue_90d
                  expected_placement_revenue_180d
                  placement_revenue_probability_distribution
                  at_risk_placements_count
                  at_risk_revenue_amount
                  high_confidence_placements_count
  Trigger       : Run weekly per institute
  Retrain       : Quarterly (tied to HIRING_OUTCOME_LOG refresh)

MODEL_3: BELT_CERTIFICATION_REVENUE_FORECASTER
  Type          : Survival Analysis (Kaplan-Meier + Cox Proportional Hazards)
  Input         : BELT_ELIGIBILITY_QUEUE · BELT_CERTIFICATION_REVENUE_LOG ·
                  governance_block_flag · match_completion_velocity ·
                  scenario_pass_rate (from ANTIGRAVITY Gap Detector)
  Output        : belt_certification_revenue_30d
                  belt_certification_revenue_90d
                  governance_blocked_revenue (amount frozen due to auto-block)
                  re_certification_cycle_revenue
                  mentor_license_renewal_revenue
  Trigger       : Run weekly
  Retrain       : Monthly

MODEL_4: SUBSCRIPTION_CHURN_PREDICTOR
  Type          : Binary + Multi-class Classification
                  (Gradient Boosted Trees — LightGBM)
  Input         : INSTITUTE_SUBSCRIPTION_LOG · SEAT_UTILIZATION_SNAPSHOT ·
                  CHURN_SIGNAL_LOG · feature_utilization_rate ·
                  curriculum_health_score (from ANTIGRAVITY Gap Detector) ·
                  placement_success_rate
  Output        : churn_probability_30d (0.0–1.0)
                  churn_probability_90d (0.0–1.0)
                  churn_reason_prediction (top 3 ranked)
                  revenue_at_risk_from_churn
                  recommended_retention_action
                  upgrade_probability (0.0–1.0)
  Trigger       : Run daily per institute
  Retrain       : Monthly

MODEL_5: CURRICULUM_GAP_REVENUE_IMPACT_QUANTIFIER
  Type          : Causal Inference Model
                  (DoWhy framework — Average Treatment Effect estimation)
  Input         : GAP_REVENUE_IMPACT_SIGNAL · ANTIGRAVITY gap_items[] ·
                  gap_severity · affected_student_count ·
                  REMEDIATION_PURCHASE_LOG · PLACEMENT_REVENUE_LOG
  Output        : revenue_loss_attributable_to_gap (per gap_item)
                  remediation_revenue_opportunity (per gap_item)
                  gap_correction_ROI_estimate
                  total_revenue_at_risk_from_curriculum (institute level)
                  total_remediation_opportunity (institute level)
  Trigger       : Run on every new GAP_REPORT_GENERATED event
  Retrain       : Quarterly

MODEL_6: MARKET_DEMAND_REVENUE_OPPORTUNITY_RANKER
  Type          : Multi-objective Ranking Model (LambdaMART)
  Input         : MARKET_DEMAND_REVENUE_SIGNAL · RECRUITER_ACCESS_REVENUE_LOG ·
                  institute skill track inventory · EMPLOYER_HIRING_PIPELINE ·
                  avg_salary_offered · employer_count_hiring
  Output        : ranked_skill_revenue_opportunities[] (top 10 per institute)
                  opportunity_score (0.0–1.0 per skill)
                  expected_revenue_if_track_launched
                  time_to_revenue_estimate_days
                  market_window_urgency (CRITICAL | HIGH | MEDIUM | LOW)
  Trigger       : Run weekly + on MARKET_DRIFT_CRITICAL event
  Retrain       : Monthly

MODEL_7: INSTITUTE_REVENUE_HEALTH_SCORER
  Type          : Composite Weighted Scoring Model
                  (Normalized multi-dimensional index)
  Input         : Outputs of MODEL_1 through MODEL_6 ·
                  curriculum_health_score · mentor_quality_score ·
                  belt_validity_score · hiring_outcome_correlation
  Output        : institute_revenue_health_score (0–100)
                  revenue_health_tier (THRIVING | STABLE | AT_RISK | CRITICAL)
                  top_3_revenue_risks[]
                  top_3_revenue_opportunities[]
                  recommended_priority_action
  Trigger       : Run weekly per institute · Triggered on health score change > 10 pts
  Retrain       : Continuous weighted update (no full retrain needed)

MODEL_8: COHORT_LIFETIME_VALUE_PREDICTOR
  Type          : Regression (Neural Network — 3-layer MLP)
  Input         : student enrollment history · belt progression velocity ·
                  placement outcome · remediation purchase behavior ·
                  social engagement score · streak_count ·
                  endorsement_count · referral_count
  Output        : student_lifetime_value_estimate (in currency)
                  cohort_lifetime_value_estimate
                  high_value_student_segments[]
                  churn_adjusted_CLV
  Trigger       : Run monthly per cohort
  Retrain       : Quarterly

MODEL_9: REVENUE_ANOMALY_DETECTOR
  Type          : Unsupervised Anomaly Detection (Isolation Forest + CUSUM)
  Input         : All revenue log streams (real-time sliding window)
  Output        : revenue_anomaly_flag (BOOLEAN)
                  anomaly_type (SPIKE | DROP | PLATEAU | BILLING_FRAUD_SIGNAL |
                               REFUND_ABUSE | ENROLLMENT_MANIPULATION)
                  anomaly_severity (CRITICAL | HIGH | MEDIUM | LOW)
                  affected_revenue_amount
                  suggested_investigation_action
  Trigger       : Continuous real-time monitoring
  Retrain       : Monthly
```

---

# 🔒 SECTION 6 — REVENUE FORECAST OUTPUT PAYLOAD (LOCKED CONTRACT)

```
ANTIGRAVITY_REVENUE_FORECAST_PAYLOAD
  Fields:
    forecast_id                     UUID
    institute_id                    UUID
    generated_at                    TIMESTAMPTZ
    forecast_period_days            [30, 60, 90, 180, 365]
    currency                        TEXT

    -- PLANE 1: Enrollment Revenue
    enrollment_revenue_30d          DECIMAL
    enrollment_revenue_90d          DECIMAL
    enrollment_revenue_180d         DECIMAL
    enrollment_revenue_365d         DECIMAL
    enrollment_confidence_interval  JSONB  -- {lower, upper} per period
    cohort_fill_probability         DECIMAL(4,3)
    dropout_revenue_risk            DECIMAL

    -- PLANE 2: Placement Revenue
    placement_revenue_90d           DECIMAL
    placement_revenue_180d          DECIMAL
    at_risk_placement_revenue       DECIMAL
    high_confidence_placement_revenue DECIMAL
    placement_revenue_distribution  JSONB  -- probability histogram

    -- PLANE 3: Belt & Certification Revenue
    belt_cert_revenue_30d           DECIMAL
    belt_cert_revenue_90d           DECIMAL
    governance_blocked_revenue      DECIMAL
    re_cert_cycle_revenue           DECIMAL
    mentor_license_revenue          DECIMAL

    -- PLANE 4: Subscription Revenue
    subscription_revenue_30d        DECIMAL
    subscription_revenue_90d        DECIMAL
    churn_revenue_at_risk_30d       DECIMAL
    churn_revenue_at_risk_90d       DECIMAL
    upgrade_revenue_opportunity     DECIMAL

    -- PLANE 5: Curriculum Gap Revenue Impact
    gap_attributed_revenue_loss     DECIMAL
    remediation_revenue_opportunity DECIMAL
    gap_correction_ROI_estimate     DECIMAL

    -- AGGREGATE SCORES
    total_forecast_revenue_90d      DECIMAL
    total_forecast_revenue_365d     DECIMAL
    total_revenue_at_risk           DECIMAL
    total_revenue_opportunity       DECIMAL
    institute_revenue_health_score  DECIMAL(5,2)
    revenue_health_tier             TEXT
    cohort_lifetime_value           DECIMAL
    churn_adjusted_CLV              DECIMAL

    -- RANKED OPPORTUNITIES
    top_skill_revenue_opportunities JSONB[]  -- ranked list from MODEL_6
    top_3_revenue_risks             JSONB[]
    top_3_revenue_opportunities     JSONB[]
    recommended_priority_action     TEXT

    -- ANOMALY STATUS
    anomaly_flag                    BOOLEAN
    anomaly_summary                 JSONB  -- nullable

    -- GOVERNANCE
    governance_blocked_amount       DECIMAL
    auto_review_flags               JSONB[]
    payload_version                 TEXT
```

---

# 🔒 SECTION 7 — REVENUE RULE ENGINE (IMMUTABLE)

```
RULE_R001 — GOVERNANCE BLOCK REVENUE FREEZE RULE
  IF governance_block_flag = True (from ANTIGRAVITY Gap Detector)
  ON any belt in BELT_ELIGIBILITY_QUEUE
  THEN governance_blocked_revenue += belt_certification_fee
       belt_cert_revenue forecast EXCLUDES blocked belts
       governance_blocked_revenue reported SEPARATELY
  → Cannot be overridden by institute admin.
  → Only Governance Board can release freeze.

RULE_R002 — CHURN CRITICAL REVENUE ALERT RULE
  IF churn_probability_30d > 0.70
  THEN revenue_health_tier CANNOT be THRIVING or STABLE
       revenue_at_risk += total subscription revenue of institute
       platform_admin_alert = True
       success_manager_assignment_trigger = True
  → Alert must be acknowledged within 48 hours.

RULE_R003 — CURRICULUM GAP REVENUE RISK RULE
  IF curriculum_health_score < 60 (from ANTIGRAVITY Gap Detector)
  THEN placement_revenue forecast DISCOUNTED by gap_risk_multiplier
       gap_risk_multiplier = 1 - (curriculum_health_score / 100)
  → Placement revenue forecast reflects actual curriculum risk.
  → Institute cannot claim full placement forecast without gap resolution.

RULE_R004 — PHANTOM SKILL REVENUE AUDIT RULE
  IF gap_type = PHANTOM_SKILL (from ANTIGRAVITY Gap Detector)
  AND placement_revenue logged for skills under phantom classification
  THEN placement_revenue_legitimacy_flag = UNDER_REVIEW
       governance_flag = True
       audit_investigation_trigger = True
  → Placement fees tied to phantom skills quarantined pending audit.

RULE_R005 — BELT VALIDITY REVENUE PROTECTION RULE
  IF BELT_PREDICTIVE_VALIDITY_SCORE < 0.5 (from ANTIGRAVITY Gap Detector)
  THEN belt_certification_revenue for affected belt = FROZEN
       employer trust signal = DEGRADED
       placement_revenue tied to belt = AT_RISK_TAGGED
  → Revenue only reinstated after Governance Board belt reinstatement.

RULE_R006 — MARKET DRIFT OPPORTUNITY URGENCY RULE
  IF market_window_urgency = CRITICAL (from MODEL_6)
  AND institute does NOT have matching skill track
  THEN opportunity_expiry_flag = True
       opportunity_window_days forecasted and displayed
       revenue_opportunity MARKED as TIME-LIMITED
  → Institute must act within opportunity_window_days or signal expires.

RULE_R007 — REVENUE ANOMALY ESCALATION RULE
  IF anomaly_type = BILLING_FRAUD_SIGNAL OR ENROLLMENT_MANIPULATION
  THEN revenue_anomaly_flag = True
       anomaly_severity = CRITICAL
       platform_admin_alert = True (immediate)
       compliance_officer_alert = True (immediate)
       affected_revenue QUARANTINED pending investigation
  → No forecasting of quarantined revenue during investigation.

RULE_R008 — MINIMUM FORECAST CONFIDENCE RULE
  IF forecast confidence_interval width > 40% of point estimate
  THEN forecast_confidence_label = LOW
       forecast CANNOT be presented as HIGH CONFIDENCE to institute
       disclaimer REQUIRED on all dashboard surfaces
  → Prevents institute from making financial decisions on low-confidence data.

RULE_R009 — COHORT LIFETIME VALUE FLOOR RULE
  IF cohort_lifetime_value < platform_defined_CLV_floor
  THEN institute flagged for REVENUE SUSTAINABILITY REVIEW
       platform_admin_notified = True
  → CLV floor is platform-configurable per region and tier. Not hardcoded.

RULE_R010 — REMEDIATION OPPORTUNITY REPORTING RULE
  IF remediation_revenue_opportunity > 0
  AND gap_correction_ROI_estimate > 1.5 (i.e. 150% ROI)
  THEN opportunity presented in dashboard as HIGH PRIORITY action
       institute curriculum team notified
  → Advisory only. No auto-purchase triggered. Human decision required.

RULE_R011 — CROSS-TENANT REVENUE ISOLATION RULE
  Institute revenue data is STRICTLY TENANT ISOLATED.
  No revenue signal, benchmark comparison, or forecast may expose
  individual institute financial data to another institute.
  Aggregate anonymized benchmarks allowed only at platform level.
  Violation → STOP EXECUTION · SECURITY INCIDENT LOGGED

RULE_R012 — NO AUTO-BILLING FROM FORECAST RULE
  ANTIGRAVITY Revenue Forecast Model is ADVISORY ONLY.
  No forecast output may trigger:
    - Auto invoice generation
    - Auto contract extension
    - Auto subscription upgrade
    - Auto payment collection
  All billing actions require human-initiated trigger via Billing Engine.
  Violation → STOP EXECUTION · GOVERNANCE ESCALATION
```

---

# 🔒 SECTION 8 — API CONTRACT REGISTRY (LOCKED)

```
POST   /antigravity/revenue-forecast/run
         Auth   : Institute Admin · Platform Admin
         Body   : { institute_id, forecast_periods[], trigger_reason }
         Returns: ANTIGRAVITY_REVENUE_FORECAST_PAYLOAD
         SLA    : < 60 seconds for single institute

GET    /antigravity/revenue-forecast/{forecast_id}
         Auth   : Institute Admin · Platform Admin · Governance Board
         Returns: Full forecast payload with confidence intervals

GET    /antigravity/institute/{institute_id}/revenue-health
         Auth   : Institute Admin · Platform Admin
         Returns: {
                    revenue_health_score,
                    revenue_health_tier,
                    churn_probability_30d,
                    total_revenue_at_risk,
                    governance_blocked_revenue,
                    top_3_risks[],
                    top_3_opportunities[]
                  }

GET    /antigravity/institute/{institute_id}/churn-signal
         Auth   : Platform Admin · Success Manager
         Returns: {
                    churn_probability_30d,
                    churn_probability_90d,
                    churn_reason_prediction[],
                    revenue_at_risk,
                    recommended_retention_action,
                    upgrade_probability
                  }

GET    /antigravity/institute/{institute_id}/placement-revenue-forecast
         Auth   : Institute Admin · Platform Admin
         Returns: {
                    placement_revenue_90d,
                    placement_revenue_180d,
                    at_risk_placement_revenue,
                    placement_revenue_distribution,
                    curriculum_gap_discount_applied (boolean + amount)
                  }

GET    /antigravity/institute/{institute_id}/belt-revenue-forecast
         Auth   : Institute Admin · Platform Admin · Governance Board
         Returns: {
                    belt_cert_revenue_30d,
                    belt_cert_revenue_90d,
                    governance_blocked_revenue,
                    re_cert_cycle_revenue,
                    blocked_belt_list[]
                  }

GET    /antigravity/institute/{institute_id}/gap-revenue-impact
         Auth   : Institute Admin · Governance Board
         Returns: {
                    gap_attributed_revenue_loss,
                    remediation_revenue_opportunity,
                    gap_correction_ROI_estimate,
                    gap_items_with_revenue_impact[] (FK to Gap Detector report)
                  }

GET    /antigravity/institute/{institute_id}/skill-opportunities
         Auth   : Institute Admin · Curriculum Team · Platform Admin
         Returns: ranked_skill_revenue_opportunities[] (top 10)
                  { skill_id, opportunity_score, expected_revenue,
                    time_to_revenue_days, market_window_urgency }

GET    /antigravity/institute/{institute_id}/cohort-lifetime-value
         Auth   : Institute Admin · Platform Admin
         Returns: {
                    cohort_lifetime_value,
                    churn_adjusted_CLV,
                    high_value_student_segments[],
                    CLV_trend_90d
                  }

GET    /antigravity/revenue-anomaly/active
         Auth   : Platform Admin · Compliance Officer
         Returns: active_anomalies[] {
                    institute_id, anomaly_type, anomaly_severity,
                    affected_revenue_amount, detected_at,
                    quarantine_status
                  }

POST   /antigravity/revenue-anomaly/{anomaly_id}/resolve
         Auth   : Compliance Officer · Platform Admin (dual approval required)
         Body   : { resolution_action, justification, evidence_refs[] }

GET    /antigravity/platform/revenue-aggregate
         Auth   : Platform Admin only
         Returns: Anonymized aggregate revenue health across all institutes
                  No individual institute data exposed
                  Benchmark distributions only

POST   /antigravity/revenue-forecast/retention-intervention/log
         Auth   : Success Manager · Platform Admin
         Body   : { institute_id, intervention_type, outcome, revenue_saved }
```

---

# 🔒 SECTION 9 — DATABASE SCHEMA (IMMUTABLE ENTITIES)

```sql
-- Core Revenue Forecast Registry
CREATE TABLE revenue_forecast_report (
    forecast_id                     UUID PRIMARY KEY,
    institute_id                    UUID NOT NULL,
    generated_at                    TIMESTAMPTZ NOT NULL,
    trigger_reason                  TEXT NOT NULL,
    forecast_period_days            INT[] NOT NULL,
    currency                        TEXT NOT NULL DEFAULT 'INR',
    payload_version                 TEXT NOT NULL,

    -- Plane 1: Enrollment
    enrollment_revenue_30d          DECIMAL(18,2),
    enrollment_revenue_90d          DECIMAL(18,2),
    enrollment_revenue_180d         DECIMAL(18,2),
    enrollment_revenue_365d         DECIMAL(18,2),
    enrollment_confidence_interval  JSONB,
    cohort_fill_probability         DECIMAL(4,3),
    dropout_revenue_risk            DECIMAL(18,2),

    -- Plane 2: Placement
    placement_revenue_90d           DECIMAL(18,2),
    placement_revenue_180d          DECIMAL(18,2),
    at_risk_placement_revenue       DECIMAL(18,2),
    high_confidence_placement_revenue DECIMAL(18,2),
    placement_revenue_distribution  JSONB,

    -- Plane 3: Belt & Certification
    belt_cert_revenue_30d           DECIMAL(18,2),
    belt_cert_revenue_90d           DECIMAL(18,2),
    governance_blocked_revenue      DECIMAL(18,2),
    re_cert_cycle_revenue           DECIMAL(18,2),
    mentor_license_revenue          DECIMAL(18,2),

    -- Plane 4: Subscription
    subscription_revenue_30d        DECIMAL(18,2),
    subscription_revenue_90d        DECIMAL(18,2),
    churn_revenue_at_risk_30d       DECIMAL(18,2),
    churn_revenue_at_risk_90d       DECIMAL(18,2),
    upgrade_revenue_opportunity     DECIMAL(18,2),

    -- Plane 5: Gap Impact
    gap_attributed_revenue_loss     DECIMAL(18,2),
    remediation_revenue_opportunity DECIMAL(18,2),
    gap_correction_roi_estimate     DECIMAL(6,3),

    -- Aggregates
    total_forecast_revenue_90d      DECIMAL(18,2),
    total_forecast_revenue_365d     DECIMAL(18,2),
    total_revenue_at_risk           DECIMAL(18,2),
    total_revenue_opportunity       DECIMAL(18,2),
    institute_revenue_health_score  DECIMAL(5,2),
    revenue_health_tier             TEXT CHECK(revenue_health_tier IN (
                                      'THRIVING','STABLE','AT_RISK','CRITICAL')),
    cohort_lifetime_value           DECIMAL(18,2),
    churn_adjusted_clv              DECIMAL(18,2),

    -- Insights
    top_skill_revenue_opportunities JSONB,
    top_3_revenue_risks             JSONB,
    top_3_revenue_opportunities     JSONB,
    recommended_priority_action     TEXT,

    -- Anomaly
    anomaly_flag                    BOOLEAN DEFAULT FALSE,
    anomaly_summary                 JSONB,

    -- Governance
    governance_blocked_amount       DECIMAL(18,2) DEFAULT 0,
    auto_review_flags               JSONB,
    forecast_confidence_label       TEXT CHECK(forecast_confidence_label IN (
                                      'HIGH','MEDIUM','LOW'))
);

-- Churn Signal Log
CREATE TABLE institute_churn_signal (
    signal_id           UUID PRIMARY KEY,
    institute_id        UUID NOT NULL,
    signal_type         TEXT CHECK(signal_type IN (
                          'LOGIN_DROP','SEAT_DECLINE','FEATURE_ABANDONMENT',
                          'PLACEMENT_FAILURE','CURRICULUM_DISSATISFACTION',
                          'COMPETITOR_MIGRATION','BILLING_DISPUTE')),
    signal_strength     DECIMAL(4,3) NOT NULL,
    signal_date         DATE NOT NULL,
    revenue_at_risk     DECIMAL(18,2),
    acknowledged        BOOLEAN DEFAULT FALSE,
    acknowledged_by     UUID,
    acknowledged_at     TIMESTAMPTZ
);

-- Retention Intervention Log
CREATE TABLE retention_intervention (
    intervention_id     UUID PRIMARY KEY,
    institute_id        UUID NOT NULL,
    churn_signal_id     UUID REFERENCES institute_churn_signal(signal_id),
    intervention_type   TEXT CHECK(intervention_type IN (
                          'OUTREACH','DISCOUNT_OFFERED','PLAN_DOWNGRADE',
                          'FEATURE_UNLOCK','SUCCESS_MANAGER_ASSIGNED')),
    triggered_at        TIMESTAMPTZ DEFAULT NOW(),
    triggered_by        UUID NOT NULL,
    outcome             TEXT CHECK(outcome IN ('RETAINED','CHURNED','PENDING')),
    revenue_saved       DECIMAL(18,2),
    resolved_at         TIMESTAMPTZ
);

-- Revenue Anomaly Registry
CREATE TABLE revenue_anomaly (
    anomaly_id          UUID PRIMARY KEY,
    institute_id        UUID NOT NULL,
    anomaly_type        TEXT CHECK(anomaly_type IN (
                          'SPIKE','DROP','PLATEAU','BILLING_FRAUD_SIGNAL',
                          'REFUND_ABUSE','ENROLLMENT_MANIPULATION')),
    anomaly_severity    TEXT CHECK(anomaly_severity IN (
                          'CRITICAL','HIGH','MEDIUM','LOW')),
    affected_revenue    DECIMAL(18,2),
    detected_at         TIMESTAMPTZ DEFAULT NOW(),
    quarantine_status   BOOLEAN DEFAULT FALSE,
    quarantined_at      TIMESTAMPTZ,
    resolved_at         TIMESTAMPTZ,
    resolved_by         UUID,
    resolution_action   TEXT,
    resolution_evidence JSONB
);

-- Gap Revenue Impact Registry
CREATE TABLE gap_revenue_impact (
    impact_id                       UUID PRIMARY KEY,
    gap_item_id                     UUID NOT NULL,  -- FK to curriculum_gap_item
    institute_id                    UUID NOT NULL,
    affected_student_count          INT,
    gap_severity                    TEXT,
    estimated_revenue_loss          DECIMAL(18,2),
    estimated_remediation_revenue   DECIMAL(18,2),
    gap_correction_roi              DECIMAL(6,3),
    signal_date                     TIMESTAMPTZ DEFAULT NOW(),
    remediation_purchased           BOOLEAN DEFAULT FALSE,
    remediation_revenue_realized    DECIMAL(18,2)
);

-- Skill Revenue Opportunity Registry
CREATE TABLE skill_revenue_opportunity (
    opportunity_id          UUID PRIMARY KEY,
    institute_id            UUID NOT NULL,
    skill_id                UUID NOT NULL,
    opportunity_score       DECIMAL(4,3),
    expected_revenue        DECIMAL(18,2),
    time_to_revenue_days    INT,
    market_window_urgency   TEXT CHECK(market_window_urgency IN (
                              'CRITICAL','HIGH','MEDIUM','LOW')),
    expiry_flag             BOOLEAN DEFAULT FALSE,
    opportunity_window_days INT,
    created_at              TIMESTAMPTZ DEFAULT NOW(),
    actioned_at             TIMESTAMPTZ,
    actioned_by             UUID
);

-- Cohort LTV Registry
CREATE TABLE cohort_lifetime_value (
    ltv_id                      UUID PRIMARY KEY,
    institute_id                UUID NOT NULL,
    cohort_id                   UUID NOT NULL,
    calculated_at               TIMESTAMPTZ DEFAULT NOW(),
    cohort_ltv_estimate         DECIMAL(18,2),
    churn_adjusted_ltv          DECIMAL(18,2),
    high_value_segments         JSONB,
    ltv_trend_90d               DECIMAL(18,2),
    model_confidence            DECIMAL(4,3)
);

-- Enrollment Revenue Log (normalized)
CREATE TABLE enrollment_revenue_log (
    log_id                  UUID PRIMARY KEY,
    institute_id            UUID NOT NULL,
    student_id              UUID NOT NULL,
    course_id               UUID NOT NULL,
    program_track           TEXT NOT NULL,
    enrollment_date         DATE NOT NULL,
    fee_amount              DECIMAL(18,2) NOT NULL,
    fee_currency            TEXT NOT NULL,
    payment_status          TEXT CHECK(payment_status IN (
                              'PAID','PENDING','FAILED','REFUNDED')),
    installment_flag        BOOLEAN DEFAULT FALSE,
    installment_schedule    JSONB,
    discount_applied        DECIMAL(18,2) DEFAULT 0,
    coupon_id               UUID,
    created_at              TIMESTAMPTZ DEFAULT NOW()
);

-- Placement Revenue Log
CREATE TABLE placement_revenue_log (
    placement_id            UUID PRIMARY KEY,
    institute_id            UUID NOT NULL,
    student_id              UUID NOT NULL,
    employer_id             UUID NOT NULL,
    role_id                 UUID,
    placement_date          DATE NOT NULL,
    fee_type                TEXT CHECK(fee_type IN (
                              'FLAT','PERCENTAGE_CTC','DEFERRED')),
    fee_amount              DECIMAL(18,2),
    fee_currency            TEXT,
    payment_received_date   DATE,
    payment_status          TEXT CHECK(payment_status IN (
                              'PAID','PENDING','FAILED','DISPUTED')),
    ctc_offered             DECIMAL(18,2),
    legitimacy_flag         TEXT CHECK(legitimacy_flag IN (
                              'VERIFIED','UNDER_REVIEW','QUARANTINED'))
                            DEFAULT 'VERIFIED',
    created_at              TIMESTAMPTZ DEFAULT NOW()
);
```

---

# 🔒 SECTION 10 — UI SCREENS (FLUTTER — OPERATIONAL LAYER)

```
SCREEN_R1: INSTITUTE REVENUE HEALTH DASHBOARD
  Access  : Institute Admin · Platform Admin
  Shows   :
    - Revenue Health Score gauge (0–100) with tier badge
    - Total forecast revenue 30d / 90d / 365d cards
    - Revenue at risk summary card
    - Governance blocked revenue indicator
    - Plane-by-plane revenue breakdown (5 planes, chart)
    - Top 3 risks + top 3 opportunities action cards
    - Anomaly alert banner (if active)
    - Churn risk meter with 30d probability
  Actions :
    - Tap risk card → drill into risk detail
    - Tap opportunity card → launch skill track wizard (advisory, not auto-execute)

SCREEN_R2: ENROLLMENT REVENUE FORECAST VIEW
  Access  : Institute Admin · Platform Admin
  Shows   :
    - Forecast line chart: 30 / 90 / 180 / 365d with confidence bands
    - Cohort fill probability gauge
    - Dropout revenue risk card
    - Course-level enrollment revenue breakdown (table + bar chart)
    - Installment schedule impact view
    - Discount impact analysis
  Actions :
    - Export forecast as PDF or CSV

SCREEN_R3: PLACEMENT REVENUE TRACKER
  Access  : Institute Admin · Platform Admin
  Shows   :
    - Placement revenue pipeline (Kanban: Probable → Confirmed → Paid)
    - At-risk placements list with revenue and reason tags
    - Curriculum gap discount applied indicator (from RULE_R003)
    - Phantom skill audit flag warning (from RULE_R004)
    - Employer hiring pipeline view (open roles × expected revenue)
    - Placement probability distribution chart (Monte Carlo output)
  Actions :
    - Flag disputed placement
    - View student placement probability score

SCREEN_R4: BELT & CERTIFICATION REVENUE PANEL
  Access  : Institute Admin · Platform Admin · Governance Board
  Shows   :
    - Belt certification revenue 30d / 90d forecast
    - Governance-blocked belt queue (count + frozen revenue amount)
    - Re-certification cycle revenue estimate
    - Mentor license revenue tracker
    - Belt validity score widget (cross-linked to Gap Detector)
  Actions :
    - View blocked belt detail → navigate to Governance Board

SCREEN_R5: SUBSCRIPTION & CHURN INTELLIGENCE PANEL
  Access  : Platform Admin · Success Manager
  Shows   :
    - Institute subscription tier + seat utilization gauge
    - Churn probability 30d / 90d risk meter
    - Churn reason prediction (top 3, confidence-weighted)
    - Revenue at risk from churn card
    - Upgrade probability indicator
    - Retention intervention history timeline
    - Recommended retention action card
  Actions :
    - Log retention intervention
    - Assign success manager (Platform Admin only)
    - Trigger outreach workflow (human-initiated only)

SCREEN_R6: CURRICULUM GAP REVENUE IMPACT VIEW
  Access  : Institute Admin · Governance Board
  Shows   :
    - Total revenue loss attributed to gaps (from MODEL_5)
    - Remediation revenue opportunity total
    - Gap correction ROI estimate gauge
    - Per-gap revenue impact table:
        gap_item_id | gap_type | severity | revenue_loss | opportunity | ROI
    - Link to Gap Detector report (cross-module navigation)
    - High-ROI remediation opportunities sorted by priority
  Actions :
    - Initiate remediation course purchase (human-triggered, advisory flow)
    - Navigate to Gap Detector report for evidence

SCREEN_R7: SKILL REVENUE OPPORTUNITY EXPLORER
  Access  : Institute Admin · Curriculum Team · Platform Admin
  Shows   :
    - Top 10 skill revenue opportunities ranked by opportunity score
    - Market window urgency badge per skill
    - Expected revenue estimate card per skill
    - Time-to-revenue estimate (days)
    - Expiry flag alert for CRITICAL urgency windows
    - Market demand sparkline (from Gap Detector MARKET_DRIFT feed)
  Actions :
    - Mark opportunity as actioned (human decision log)
    - Request skill track creation (routes to Curriculum Team workflow)

SCREEN_R8: COHORT LIFETIME VALUE DASHBOARD
  Access  : Institute Admin · Platform Admin
  Shows   :
    - Cohort LTV estimate vs churn-adjusted LTV (comparison widget)
    - High-value student segment breakdown (donut chart)
    - LTV trend 90d sparkline
    - Model confidence indicator
    - CLV floor breach alert (if below platform threshold)
  Note    : Student-level LTV data never surfaced to institute without role gate

SCREEN_R9: REVENUE ANOMALY MONITOR
  Access  : Platform Admin · Compliance Officer only
  Shows   :
    - Active anomalies list with severity badge
    - Quarantined revenue amount indicator
    - Anomaly type breakdown (bar chart)
    - Investigation status per anomaly
    - Dual-approval resolution workflow panel
  Actions :
    - Initiate investigation
    - Submit resolution (requires dual approval — Compliance + Platform Admin)

SCREEN_R10: PLATFORM AGGREGATE REVENUE INTELLIGENCE
  Access  : Platform Admin only
  Shows   :
    - Anonymized aggregate revenue health distribution across institutes
    - Revenue health tier distribution (THRIVING / STABLE / AT_RISK / CRITICAL)
    - Platform-wide churn risk trend
    - Aggregate skill opportunity heatmap
    - Revenue anomaly count by type (no institute-identifying data)
  Rule    : No individual institute data exposed on this screen
```

---

# 🔒 SECTION 11 — REACT/NEXT.JS SEO LAYER (READ-ONLY)

```
PUBLIC SEO PAGES (React/Next.js — Static/SSR):

  /institute/{institute_id}/outcomes
    → Public placement rate summary (anonymized aggregate)
    → Belt certification count (published public stats only)
    → No revenue figures exposed publicly
    → Structured data: schema.org/EducationalOrganization

  /institute/{institute_id}/hiring-partners
    → Active employer hiring pipeline (public tier only)
    → Industries hiring from this institute
    → No financial pipeline data exposed

  /skills/{skill_id}/career-value
    → Aggregated salary range signal for skill
    → Market demand indicator
    → No institute-specific revenue data

STRICT RULE: No financial forecast, revenue figure, churn data, or
institute-specific subscription data is ever surfaced in the React
SEO public layer. React layer = discovery only.
```

---

# 🔒 SECTION 12 — EVENT ARCHITECTURE (MANDATORY WIRING)

```
Events REVENUE FORECAST MODEL produces (→ consumed by):

REVENUE_FORECAST_GENERATED          → Notification Service, Institute Admin,
                                        Platform Admin Dashboard
CHURN_RISK_CRITICAL                 → Success Manager Alert, Platform Admin,
                                        Retention Intervention Engine
REVENUE_ANOMALY_DETECTED            → Compliance Officer, Platform Admin (IMMEDIATE),
                                        Revenue Quarantine Engine
GAP_REVENUE_IMPACT_CALCULATED       → Institute Admin, Curriculum Team Alert,
                                        Remediation Recommendation Engine
SKILL_OPPORTUNITY_CRITICAL_WINDOW   → Institute Admin, Curriculum Team,
                                        Market Intelligence Dashboard
GOVERNANCE_BLOCKED_REVENUE_UPDATED  → Governance Board, Institute Admin,
                                        Belt Revenue Forecast (recompute)
CLV_FLOOR_BREACH                    → Platform Admin, Revenue Review Queue
BELT_REVENUE_FROZEN                 → Belt Engine (confirm freeze), Institute Admin,
                                        Governance Board
HIGH_ROI_REMEDIATION_AVAILABLE      → Institute Curriculum Team,
                                        Institute Admin Notification

Events REVENUE FORECAST MODEL consumes (← produced by):

GAP_REPORT_GENERATED                ← ANTIGRAVITY Curriculum Gap Detector
  → Triggers: MODEL_5 (Gap Revenue Impact) run
  → Updates: gap_revenue_impact table

BELT_AUTO_BLOCKED                   ← ANTIGRAVITY Curriculum Gap Detector
  → Triggers: RULE_R001 (Revenue Freeze), governance_blocked_revenue update
  → Updates: belt_revenue forecast recompute

MARKET_DRIFT_CRITICAL               ← ANTIGRAVITY Curriculum Gap Detector
  → Triggers: MODEL_6 (Opportunity Ranker) run
  → Updates: skill_revenue_opportunity table

MATCH_COMPLETED                     ← Dojo Match Engine
  → Feeds: Belt eligibility queue → MODEL_3 input

BELT_AWARDED                        ← Belt Engine
  → Logs: belt_certification_revenue_log entry

PLACEMENT_CONFIRMED                 ← Job Portal Engine
  → Logs: placement_revenue_log entry
  → Triggers: MODEL_2 update (recalibrate actual vs forecast)

SUBSCRIPTION_RENEWED                ← Billing Engine
  → Logs: institute_subscription_log update
  → Triggers: MODEL_4 recalibrate

SUBSCRIPTION_CANCELLED              ← Billing Engine
  → Triggers: CHURN_RISK_CRITICAL event
  → Updates: churn revenue at risk

EMPLOYER_FEEDBACK_RECEIVED          ← HR Integration Engine
  → Feeds: MODEL_2 (Placement Revenue Forecaster — outcome validation)

REFUND_PROCESSED                    ← Billing Engine
  → Feeds: MODEL_9 (Anomaly Detector — refund pattern monitoring)

MENTOR_SUSPENDED                    ← ANTIGRAVITY Curriculum Gap Detector
  → Triggers: mentor_license_revenue forecast adjustment
```

---

# 🔒 SECTION 13 — GOVERNANCE AUTHORITY MATRIX

```
ACTION                                         | WHO CAN AUTHORIZE
-----------------------------------------------|------------------------------------------
Run revenue forecast scan                      | System (automated) · Institute Admin · Platform Admin
Publish forecast report to institute           | Platform Admin (auto) on generation
Release governance-blocked belt revenue        | Governance Board only
Resolve revenue anomaly                        | Compliance Officer + Platform Admin (dual approval)
Dismiss churn risk alert                       | Success Manager (logged + justified)
Override forecast confidence label             | Platform Admin (with written justification)
Access cross-institute aggregate data          | Platform Admin only
Log retention intervention outcome             | Success Manager · Platform Admin
Mark skill opportunity as actioned             | Institute Admin · Curriculum Team
Initiate remediation purchase workflow         | Institute Admin (human-triggered, advisory only)
Export revenue forecast data                   | Institute Admin (own data only) · Platform Admin
Access raw anomaly investigation data          | Compliance Officer · Platform Admin only
```

---

# 🔒 SECTION 14 — COMPLIANCE, AUDIT & RETENTION LOCK

```
IMMUTABILITY RULES:
  All revenue forecast reports            → Immutable once generated
  All placement revenue log entries       → Immutable · Append-only
  All anomaly records                     → Immutable (resolution is additive, not overwrite)
  All churn signal logs                   → Immutable
  All retention intervention logs         → Immutable

EVERY GOVERNANCE ACTION CARRIES:
    actor_id
    action_type
    justification_text
    timestamp
    digital_signature_hash

DATA RETENTION POLICY:
    Revenue forecast reports              → 7 years minimum
    Enrollment revenue logs               → 10 years minimum (financial records)
    Placement revenue logs                → 10 years minimum
    Belt certification revenue logs       → Lifetime of platform
    Subscription logs                     → 7 years minimum
    Anomaly records                       → 10 years minimum
    Churn signal logs                     → 5 years minimum
    Cohort LTV records                    → 7 years minimum

FINANCIAL DATA CLASSIFICATION:
    Institute revenue figures             → CONFIDENTIAL
    Placement fee amounts                 → CONFIDENTIAL + PII-ADJACENT
    Student placement probability scores  → RESTRICTED
    Anomaly investigation records         → RESTRICTED
    Platform aggregate benchmarks         → INTERNAL

EXPORT CONTROLS:
    Institute admin: own institute revenue data only (PDF + CSV, encrypted)
    Platform admin: full access + aggregate reports
    Governance Board: audit trail export (signed + encrypted)
    Cross-institute export: FORBIDDEN
    Public revenue data: FORBIDDEN (only outcome summaries — see React SEO rules)

TAX & REGULATORY:
    GST/VAT handling: Inherited from ECOSKILLER Billing Engine (Section P2)
    Revenue recognition: Follows subscription period — not cash receipt
    Deferred revenue tracking: Required for installment and outcome-linked fees
```

---

# 🔒 SECTION 15 — PERFORMANCE & SCALABILITY LOCK

```
FORECAST SLA:
    Single institute forecast run          → Complete within 60 seconds
    Batch nightly forecast (all institutes)→ Complete within 3 hours
    Real-time anomaly detection            → < 5 second detection latency
    Churn signal update (daily)            → Complete within 30 minutes
    Gap revenue impact recalculation       → < 2 minutes after GAP_REPORT_GENERATED

ML MODEL RETRAINING CADENCE:
    MODEL_1 (Enrollment Forecaster)        → Monthly or 500+ new enrollment events
    MODEL_2 (Placement Forecaster)         → Quarterly (tied to hiring outcome refresh)
    MODEL_3 (Belt Revenue Forecaster)      → Monthly
    MODEL_4 (Churn Predictor)              → Monthly
    MODEL_5 (Gap Revenue Impact)           → Quarterly
    MODEL_6 (Opportunity Ranker)           → Monthly
    MODEL_7 (Revenue Health Scorer)        → Continuous weighted update
    MODEL_8 (CLV Predictor)               → Quarterly
    MODEL_9 (Anomaly Detector)            → Monthly

INFRA REQUIREMENTS:
    Compute         : GPU-capable node pool for ML training (Kubernetes)
    Columnar Store  : ClickHouse or BigQuery-compatible for revenue analytics
    Time-series DB  : InfluxDB or TimescaleDB for market signal streams
    Cache           : Redis for real-time health score serving (< 100ms)
    Queue           : Kafka / Redis Streams for event ingestion
    Scheduler       : Kubernetes CronJob for nightly batch forecasts
    Storage         : Encrypted object storage for forecast report archives
    Secrets         : All ML model weights and revenue keys in Secret Manager only
```

---

# 🔒 SECTION 16 — SECURITY LOCK (INHERITED + MODULE-SPECIFIC)

```
INHERITED FROM ECOSKILLER MASTER PROMPT:
    JWT auth · RBAC + ABAC · MFA · Row-level security · Tenant isolation ·
    PII encryption at rest · Secret manager · WAF · Rate limiting · Audit logs

INHERITED FROM ANTIGRAVITY GAP DETECTOR v1.0:
    Gap signal classification (SENSITIVE_INTERNAL)
    Governance authority hierarchy
    Immutable audit trail structure

MODULE-SPECIFIC ADDITIONS:
    Revenue forecast data                 → CONFIDENTIAL classification
    Placement fee + CTC data              → CONFIDENTIAL + PII-ADJACENT
    Anomaly investigation data            → RESTRICTED
    ML model inference endpoints          → RBAC-gated, rate-limited
    Revenue forecast API                  → Institute-scoped JWT claims enforced
    Cross-tenant query guard              → Enforced at DB row-level and API gateway
    Financial log exports                 → Encrypted, signed, expiring download URLs
    Billing fraud signals                 → Dual-alert: Compliance + Platform Admin
    ML training jobs                      → Isolated compute namespace (no shared memory)
    Model version artifacts               → Signed + immutably stored in artifact registry
```

---

# 🔒 SECTION 17 — TEST GATE REQUIREMENTS

```
Unit Tests Required:
    - Each ML model output contract (mocked inputs → expected output type + range)
    - Rule engine (RULE_R001–RULE_R012) deterministic output tests
    - Governance blocked revenue freeze triggers
    - Churn critical alert threshold tests
    - No auto-billing assertion tests (RULE_R012)
    - Revenue isolation: zero cross-tenant data in any response
    - Forecast confidence label assignment logic
    - Anomaly type classification edge cases

Integration Tests Required:
    - Full forecast pipeline: input ingestion → ML model → rule engine → payload
    - Event chain: GAP_REPORT_GENERATED → MODEL_5 run → GAP_REVENUE_IMPACT_CALCULATED
    - Event chain: BELT_AUTO_BLOCKED → RULE_R001 → governance_blocked_revenue update
    - Event chain: SUBSCRIPTION_CANCELLED → CHURN_RISK_CRITICAL → success manager alert
    - Anomaly detection: BILLING_FRAUD_SIGNAL → dual alert → quarantine
    - Placement revenue: RULE_R003 gap discount applied correctly to forecast
    - Phantom skill: RULE_R004 placement fee quarantine triggered

Data Integrity Tests:
    - Quarantined revenue excluded from forecast totals
    - Governance-blocked belt revenue reported separately (never in forecast total)
    - Frozen anomaly revenue not surfaced in institute health score
    - No revenue data exposed in React SEO layer (automated scan)
    - Cross-tenant isolation: institute A cannot access institute B revenue data

Load Tests:
    - 1000 concurrent institute forecast requests within SLA
    - Nightly batch forecast for 10,000 institutes within 3-hour window
    - Real-time anomaly detector under high event throughput

No production deploy without all test gates PASSING.
Absence of passing test suite → STOP EXECUTION
```

---

# 🔒 SECTION 18 — ANTIGRAVITY REVENUE MODULE MASTER PROMPT INSERT BLOCK

Paste this block into the ECOSKILLER MASTER PROMPT (ANTIGRAVITY Extension Block):

```
BEGIN LOCKED ANTIGRAVITY REVENUE FORECAST MODULE ARTIFACT

Module Name         : ANTIGRAVITY — Institute Revenue Forecast Model
Module Type         : Institute Financial Predictive ML Intelligence System
Version             : 1.0
Status              : SEALED · LOCKED · GOVERNED · DETERMINISTIC
Parent Module       : ANTIGRAVITY Curriculum Gap Detector v1.0 (REQUIRED ACTIVE)

Forecast Planes:
  PLANE_1           : Enrollment & Tuition Revenue
  PLANE_2           : Placement & Hiring Success Revenue
  PLANE_3           : Dojo Belt & Certification Revenue
  PLANE_4           : Platform Subscription & Seat Revenue
  PLANE_5           : Curriculum Gap Correction Revenue Impact

ML Models           : 9 models (see Section 5) — architecture locked
Rule Engine         : Rules R001–R012 — immutable
API Contracts       : 13 endpoints — locked
DB Schema           : 10 entities — add-only
Events Produced     : 9 — event-driven only
Events Consumed     : 12 — from Gap Detector + Dojo + Billing + HR engines
UI Screens          : 10 Flutter operational + 3 React SEO public pages
Governance          : Governance Board + Compliance Officer dual-authority controls
Audit               : Immutable · Signed · Long-retention (7–lifetime)
Security            : Inherits ECOSKILLER master + Gap Detector + CONFIDENTIAL classification
Test Gates          : Unit + Integration + Data Integrity + Load — mandatory

AI Role             : ADVISORY + FORECAST ONLY
Auto-Billing        : FORBIDDEN
Auto-Contract       : FORBIDDEN
Auto-Promotion      : FORBIDDEN (inherited from Gap Detector)
Cross-Tenant Exposure: FORBIDDEN
Creative Assumption  : FORBIDDEN
Default             : DENY

Interpretation Authority  : NONE
Architecture Authority    : LOCKED

END LOCKED ANTIGRAVITY REVENUE FORECAST MODULE ARTIFACT
```

---

# 🔒 SECTION 19 — FINAL GOVERNANCE SEAL

```
ANTIGRAVITY INSTITUTE REVENUE FORECAST MODEL — PRODUCTION SEAL BLOCK

SYSTEM STATUS:
  ✔ Five revenue forecast planes defined and locked
  ✔ Nine ML models architected and sealed
  ✔ Twelve rule engine rules immutable (R001–R012)
  ✔ Thirteen API endpoints locked
  ✔ Ten database entities locked (add-only)
  ✔ Nine events produced (event-driven only, no manual sync)
  ✔ Twelve events consumed (cross-module wiring locked)
  ✔ Ten Flutter operational screens defined
  ✔ Three React public SEO pages defined (no revenue data exposed)
  ✔ Governance authority matrix locked
  ✔ Dual-approval anomaly resolution enforced
  ✔ Audit, compliance, and financial retention rules immutable
  ✔ Security inheritance + CONFIDENTIAL financial classification locked
  ✔ Test gates mandatory (unit + integration + data integrity + load)
  ✔ Performance SLAs defined per model and per API
  ✔ ML retraining cadence locked per model
  ✔ ANTIGRAVITY Gap Detector v1.0 declared as hard dependency

FORBIDDEN OPERATIONS:
  ✘ Auto-billing from forecast output
  ✘ Auto-contract extension from forecast
  ✘ Cross-tenant revenue data exposure
  ✘ Public surfacing of revenue figures
  ✘ Placement fee payment against phantom skill-linked placements (without audit)
  ✘ Belt revenue forecast inclusion of governance-blocked belts
  ✘ Silent rubric change revenue impact (inherited — audit escalation)
  ✘ Architecture interpretation by AI or intern
  ✘ Mutation of sealed entities without version bump

VIOLATION OF ANY ABOVE:
  → STOP EXECUTION
  → REPORT ANTIGRAVITY_REVENUE_INTEGRITY_VIOLATION
  → NO DEPLOYMENT CLAIM PERMITTED
  → COMPLIANCE ESCALATION MANDATORY
```

---

```
Document Version    : 1.0
Artifact Class      : Production ML Module — Sealed
Parent Artifact     : ANTIGRAVITY Curriculum Gap Detector v1.0
Sealed By           : ECOSKILLER DOJO SAAS Governance Authority
Mutation Policy     : Add-only via version bump
Last Locked         : v1.0 — Initial Seal
Next Review         : v2.0 bump required for any structural change
Dependency Lock     : ANTIGRAVITY Curriculum Gap Detector v1.0 MUST BE ACTIVE
```
