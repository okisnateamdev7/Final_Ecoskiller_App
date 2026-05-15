# 🔒 DROPOUT_RISK_PREDICTION_AGENT
## ECOSKILLER — ANTIGRAVITY AGENT SPECIFICATION
### Status: SEALED · LOCKED · GOVERNED · DETERMINISTIC
### Artifact Class: Production Agent Blueprint
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only
### Companion Agent: ATTENDANCE_BEHAVIOR_ANALYTICS_AGENT (ABAA)
### R28 Compliance: ENFORCED (Traditional ML — rule-based tiers respected)
### M5 Compliance: ENFORCED (Human executes training, model cannot self-claim deployment)

---

## ⚠️ SEAL DECLARATION

```
This document is SEALED.
No ambiguity is permitted.
No inference is permitted.
No implicit behavior is permitted.
No prediction label is issued without a defined, auditable rule or model formula.
No user is acted upon without a defined intervention protocol.
No score is emitted without a traceable feature computation chain.
Every label, score, tier, and intervention in this document executes exactly as written.
Deviation = STOP EXECUTION → REPORT VIOLATION → NO PARTIAL OUTPUT.
```

---

# SECTION I — AGENT IDENTITY

```
Agent Name:           DROPOUT_RISK_PREDICTION_AGENT (DRPA)
Agent Class:          Antigravity Predictive Intelligence Agent
Parent System:        ECOSKILLER — Unified Job + Skill + Project + Education SaaS
Agent Type:           ML-Powered Risk Classifier + Rule-Gated Intervention Router
Execution Mode:       Deterministic feature pipeline → probabilistic model → rule-gated output
R28 Tier:             Tier 2 — Traditional ML (Gradient Boosting classification)
M5 Compliance:        Model architecture declared; training pipeline human-executed
Bias Policy:          Fairness audit mandatory before every model version deployment
Human Override:       Required for all Tier-RED interventions; logged with audit trail
LLM Usage:            Permitted ONLY for human-readable explanation generation (R28-3)
```

**Agent Mission:**

Predict, classify, and route early dropout risk signals for every active learner across all Ecoskiller learning surfaces — Dojo, Voice GD, live courses, society batches, campus placement programs, and bootcamps — using behavioral signals from ABAA, engagement signals from the platform activity layer, and academic/career progression signals from the core data layer. Emit tiered risk scores. Trigger defined intervention protocols. Never issue a verdict. Never act without a defined rule. Never label a user without a traceable computation.

---

# SECTION II — R28 COMPLIANCE DECLARATION (MANDATORY)

Per **R28 — Intelligence Cost Optimization Law**, every intelligence feature must declare its model tier. This agent's full R28 compliance map is:

| Feature | R28 Tier | Model Class |
|---|---|---|
| Dropout risk score computation | Tier 2 | Gradient Boosting (e.g., XGBoost / LightGBM) |
| Risk tier classification (GREEN / AMBER / RED) | Tier 1 | Rule engine — threshold lookup |
| Intervention routing | Tier 1 | Rule engine — deterministic decision tree |
| Feature engineering (all signals) | Tier 1 | Rule engine — formula-based |
| Anomaly in risk trajectory | Tier 2 | Isolation Forest (anomaly detection) |
| Human-readable risk explanation | Tier 3 | LLM — explanation only, not decision |
| Notification content personalization | Tier 3 | LLM — content generation only |

```
NO LLM MAY MAKE OR INFLUENCE A RISK SCORE.
NO LLM MAY TRIGGER AN INTERVENTION.
LLM outputs are advisory text only.
Decision authority lives in Tier 1 rules and Tier 2 ML outputs exclusively.
```

## R28-5 Cost Declaration (Required)

| Component | Model | Est. Inference Cost / 1,000 Requests | Est. Monthly Cost at MVP (10K users) |
|---|---|---|---|
| Dropout risk score | XGBoost (self-hosted) | ~$0.001 | ~$0.30 |
| Anomaly detector | Isolation Forest (self-hosted) | ~$0.001 | ~$0.10 |
| LLM explanation | claude-haiku-4-5 (API) | ~$0.25 | ~$25.00 (triggered only on Tier-RED) |

```
Absence of R28 cost mapping → STOP EXECUTION
```

---

# SECTION III — M5 COMPLIANCE DECLARATION (MANDATORY)

Per **M5 — AI MODEL REALITY LAW**, this document declares the model architecture. Training, fairness evaluation, and deployment are **human-executed responsibilities**. This agent does NOT claim deployed model status. Deployment requires human execution logs.

**Human-executed responsibilities for DRPA:**

```
✗ Acquisition of real behavioral training data (from ABAA ClickHouse)
✗ Execution of XGBoost training pipeline (Python, scikit-learn / XGBoost)
✗ Fairness and bias evaluation (per Section XI of this document)
✗ Model retraining operations (per defined schedule in Section XII)
✗ Production deployment sign-off (requires senior ML engineer + platform admin)

AI MAY define:
✓ Model architecture (this document)
✓ Feature sets (Section VII)
✓ Evaluation metrics (Section X)
✓ Explainability format (Section IX)
✓ Retraining schedule (Section XII)
✓ Fairness criteria (Section XI)

DRPA MODEL DEPLOYMENT STATUS: NOT CLAIMED UNTIL HUMAN LOG ATTACHED.
```

---

# SECTION IV — SCOPE LOCK

## 4.1 In-Scope Learner Surfaces

| Surface | Signal Source | Risk Window |
|---|---|---|
| Voice GD Sessions | ABAA → gd.* events | Per-session + rolling 30-day |
| Dojo Match Rooms | ABAA → dojo.* events | Per-match + rolling 30-day |
| Live Course Sessions | ABAA → course.* events | Per-session + rolling 30-day |
| Society / Offline Batches | ABAA → society.* events + CouchDB sync | Per-batch + rolling 30-day |
| Campus Placement Assessments | ABAA → assessment.* events | Per-assessment |
| Interview Sessions | ABAA → interview.* events | Per-slot |
| Platform Engagement Layer | ecoskiller.user.activity.* events | Rolling 7 / 14 / 30-day windows |
| Streak Engine (R57/R95) | ecoskiller.streak.* events | Daily + rolling 7-day |

## 4.2 In-Scope User Types (from 300-type master list)

**Primary targets — learners with scheduled progression:**
```
Class A — Students & Learners (Types 1–40): Full risk scoring + intervention routing
Class B — Trainers & Educators (Types 41–75): At-risk flagging only (inactivity / platform churn)
Class C — Institute Staff (Types 76–110): Not scored for dropout; receive learner risk dashboards
Class D — Employers (Types 111–160): Not scored; receive batch-level aggregate risk reports for their GD/interview cohorts
Class G — Platform Ops (Types 231–270): Full visibility, no personal scoring
Class H — Future Roles (Types 271–300): Scoped under digital twin / metaverse tracks when live
```

**Out of scope for personal dropout scoring:**
```
Government and NGO officials (Class F — Types 201–230): aggregate-only analytics
Freelancers not enrolled in tracked learning programs (Class E — Types 161–200)
```

## 4.3 Out-of-Scope (Forbidden)

```
FORBIDDEN: Scoring users on surfaces they did not consent to track (see Section XIII)
FORBIDDEN: Issuing dropout risk score for users with < 3 sessions on record
FORBIDDEN: Auto-removing any user from any program based on risk score
FORBIDDEN: Sharing individual dropout risk scores with employers without explicit user consent
FORBIDDEN: Using dropout risk score as a hiring signal
FORBIDDEN: Issuing risk scores to minors without parent/guardian consent in record
FORBIDDEN: Any LLM involvement in computing or adjusting the numeric risk score
FORBIDDEN: Generating a risk score when ABAA feed has not synced within last 48 hours (stale data)
```

---

# SECTION V — ARCHITECTURE PLACEMENT

## 5.1 System Position

```
DRPA sits in:
  Kubernetes namespace: analytics
  Co-located with: ATTENDANCE_BEHAVIOR_ANALYTICS_AGENT (ABAA)
  Dependency direction:
    Consumes FROM: ABAA (behavioral signals), Core Services (profile, streak, LBI)
    Produces TO: Notification Service, Admin Governance, Institute Dashboards

DRPA is a READER and EMITTER. It does not modify session state, scores, or profiles.
DRPA never writes to ClickHouse directly.
DRPA emits Kafka events → consumed by Notification, Governance, and Dashboard services.
```

## 5.2 Infrastructure Dependencies (Locked)

| Component | Role | DRPA Usage |
|---|---|---|
| Apache Kafka | Event bus | Signal ingestion + risk event emission |
| ClickHouse | Analytics store | Feature query for rolling window aggregations |
| PostgreSQL | Source of truth | User profile, enrollment, belt/certification state |
| Redis | State cache | Active risk tier per user (TTL: 24 hours) |
| Feature Store Service | ML feature storage | Centralized feature vectors for model inference |
| Model Registry Service | ML model versioning | Versioned XGBoost model artifact storage |
| Embedding & Model Inference Service | Model serving | XGBoost inference endpoint (self-hosted) |
| Vector Database Service (Qdrant) | Similarity search | Peer cohort comparison for anomaly context |
| Stream Processing Service | Real-time pipeline | Rolling window feature computation via Kafka Streams |
| Grafana | Dashboard rendering | Risk dashboards for institutes, platform ops |
| MinIO | Object storage | Risk audit archives, model artifacts |
| OpenTelemetry | Tracing | Full trace per risk computation |
| Prometheus + Loki | Metrics + Logs | Agent health, computation audit logs |

## 5.3 Data Flow (Non-Negotiable)

```
[ABAA Kafka Events]  +  [Platform Activity Events]  +  [Streak/Profile Events]
              ↓
     [Stream Processing Service]
     (rolling window feature computation — Kafka Streams)
              ↓
     [Feature Store Service]
     (feature vector stored per user per computation window)
              ↓
     [Model Inference Service]
     (XGBoost inference → raw risk probability 0.0–1.0)
              ↓
     [Risk Tier Classifier — RULE ENGINE (Tier 1)]
     (probability → GREEN / AMBER / RED tier)
              ↓
     [Risk Score Store — Redis (TTL: 24h) + ClickHouse (permanent)]
              ↓
     [Intervention Router — RULE ENGINE (Tier 1)]
     (tier × user_type × context → defined intervention action)
              ↓
     [Kafka: ecoskiller.drpa.risk_scored / ecoskiller.drpa.intervention_triggered]
              ↓
     [Notification Service] + [Admin Governance] + [Institute Dashboard API]
              ↓ (Tier-RED only, on-demand)
     [LLM Explanation Generator — Tier 3]
     (human-readable explanation text — NOT a decision input)
```

---

# SECTION VI — EVENT SCHEMA (SEALED)

## 6.1 Kafka Topics Consumed from ABAA

```
ecoskiller.session.joined
ecoskiller.session.left
ecoskiller.gd.turn_completed
ecoskiller.gd.turn_skipped
ecoskiller.gd.mic_active_duration
ecoskiller.gd.interrupt_attempt
ecoskiller.dojo.match_joined
ecoskiller.dojo.match_left
ecoskiller.dojo.scenario_response_time
ecoskiller.course.session_joined
ecoskiller.course.session_left
ecoskiller.assessment.submitted
ecoskiller.assessment.idle_timeout
ecoskiller.society.batch_checkin
ecoskiller.society.batch_checkout
ecoskiller.society.late_arrival
```

## 6.2 Kafka Topics Consumed from Platform Activity Layer

```
ecoskiller.user.login
ecoskiller.user.profile_updated
ecoskiller.streak.updated
ecoskiller.streak.reset
ecoskiller.belt.promoted
ecoskiller.belt.eligible
ecoskiller.skill.added
ecoskiller.skill.obsolete_detected       (from R73 Career Gap Detection)
ecoskiller.course.enrolled
ecoskiller.course.completed
ecoskiller.certification.issued
ecoskiller.challenge.joined
ecoskiller.challenge.completed
ecoskiller.job.applied
ecoskiller.interview.completed
```

## 6.3 Kafka Topics Emitted by DRPA

```
ecoskiller.drpa.risk_scored
ecoskiller.drpa.tier_changed
ecoskiller.drpa.intervention_triggered
ecoskiller.drpa.risk_resolved
ecoskiller.drpa.model_anomaly_detected
ecoskiller.drpa.stale_data_warning
```

## 6.4 Canonical Risk Scored Event Envelope (Sealed)

```json
{
  "event_id": "uuid-v4",
  "event_type": "ecoskiller.drpa.risk_scored",
  "user_id": "uuid-v4",
  "tenant_id": "uuid-v4",
  "user_type_code": "integer (1–300)",
  "timestamp_utc": "ISO-8601",
  "risk_probability": "float 0.0–1.0",
  "risk_tier": "GREEN | AMBER | RED",
  "previous_tier": "GREEN | AMBER | RED | null",
  "tier_changed": "boolean",
  "feature_snapshot_id": "uuid-v4",
  "model_version": "string",
  "computation_window_days": "integer",
  "intervention_triggered": "boolean",
  "explanation_available": "boolean",
  "schema_version": "1.0"
}
```

**Schema violations → event discarded → dead-letter topic → alert → no partial emission.**

---

# SECTION VII — FEATURE CATALOG (SEALED)

All features are computed by the Stream Processing Service from ABAA events and platform activity events. No feature may be computed outside this catalog without a version bump.

## 7.1 Attendance & Participation Features (from ABAA)

| Feature Name | Computation | Window |
|---|---|---|
| `f_attendance_rate` | sessions_attended / sessions_scheduled | Rolling 30 days |
| `f_attendance_trend` | attendance_rate(last_7d) − attendance_rate(prior_7d) | 14 days |
| `f_consecutive_absences` | count of consecutive missed sessions | Since last attendance |
| `f_punctuality_rate` | on_time_joins / sessions_attended | Rolling 30 days |
| `f_early_exit_rate` | early_exits / sessions_attended | Rolling 30 days |
| `f_gd_turns_completed_rate` | turns_completed / total_turns (GD sessions) | Rolling 30 days |
| `f_gd_silence_ratio_avg` | mean silence_ratio across GD sessions | Rolling 30 days |
| `f_session_duration_ratio` | mean (active_duration / session_duration) | Rolling 30 days |

## 7.2 Platform Engagement Features

| Feature Name | Computation | Window |
|---|---|---|
| `f_days_since_last_login` | today − last login timestamp | Absolute |
| `f_days_since_last_session` | today − last ABAA session event | Absolute |
| `f_login_frequency_7d` | count of login events | Rolling 7 days |
| `f_login_frequency_30d` | count of login events | Rolling 30 days |
| `f_login_trend` | login_frequency_7d − login_frequency_prior_7d | 14 days |
| `f_current_streak` | streak_count from StreakTracker | Current |
| `f_streak_resets_30d` | count of streak_reset events | Rolling 30 days |
| `f_challenges_joined_30d` | count of challenge_joined events | Rolling 30 days |
| `f_profile_completeness` | profile completeness score 0–100 (from User Service) | Current |

## 7.3 Learning Progression Features

| Feature Name | Computation | Window |
|---|---|---|
| `f_courses_completed_30d` | count of course.completed events | Rolling 30 days |
| `f_belt_level` | current belt level integer (0 = none, 1–N) | Current |
| `f_days_since_last_belt_promotion` | today − last belt_promoted event | Absolute |
| `f_skills_added_30d` | count of skill.added events | Rolling 30 days |
| `f_skills_obsolete_count` | count of active skill.obsolete_detected flags | Current |
| `f_certifications_count` | total certifications issued | Cumulative |
| `f_lbi_current` | Learner Behavior Index from ABAA | Current |
| `f_lbi_trend` | lbi_current − lbi_14d_ago | 14 days |
| `f_assessments_submitted_rate` | assessments_submitted / assessments_assigned | Rolling 30 days |

## 7.4 Social & Network Features

| Feature Name | Computation | Window |
|---|---|---|
| `f_endorsements_received_30d` | count of StudentEndorsement events (to user) | Rolling 30 days |
| `f_peer_projects_joined_30d` | count of project join events | Rolling 30 days |
| `f_influence_score` | StudentProfile.influence_score (from R91) | Current |

## 7.5 Derived Risk Indicator Features (Rule-Computed, Tier 1)

These are binary flags computed by the rule engine before ML inference. They serve as high-signal inputs.

| Feature Name | Rule |
|---|---|
| `f_flag_critical_absence` | f_consecutive_absences >= 3 |
| `f_flag_ghost_user` | f_days_since_last_login >= 14 AND f_login_frequency_7d == 0 |
| `f_flag_skill_decay` | f_skills_obsolete_count >= 2 AND f_skills_added_30d == 0 |
| `f_flag_streak_collapse` | f_streak_resets_30d >= 3 |
| `f_flag_lbi_freefall` | f_lbi_trend <= -20 |
| `f_flag_assessment_abandonment` | f_assessments_submitted_rate < 0.3 |
| `f_flag_passive_attendance` | f_attendance_rate > 0.7 AND f_session_duration_ratio < 0.4 |

---

# SECTION VIII — RISK SCORING MODEL (SEALED)

## 8.1 Model Declaration (R12 Compliant)

```
Model Type:           XGBoost Binary Classifier (or LightGBM — team decision at training time)
Problem:              Binary classification — Dropout Risk (1 = at risk, 0 = not at risk)
Output:               Probability score 0.0–1.0 (risk_probability)
Input:                Feature vector from Section VII (26 features + 7 derived flags = 33 total)
Training Data:        Historical behavioral events from ClickHouse (ABAA output)
                      Minimum 90 days of data before first training run
                      Label definition: user who missed ≥ 50% of sessions in a 30-day window
                      following this feature snapshot
Evaluation Metrics:   AUC-ROC (primary), Precision@Recall-0.8, F1-Score
Explainability:       SHAP values computed for every inference (stored in Feature Store)
Retraining:           See Section XII
Deployment:           Human-gated (M5 compliance)
```

## 8.2 Risk Tier Classification (Tier 1 Rule Engine — NOT ML)

```
risk_probability → risk_tier mapping (immutable thresholds):

  GREEN:  risk_probability < 0.40
  AMBER:  risk_probability >= 0.40 AND < 0.70
  RED:    risk_probability >= 0.70

Threshold change = version bump + human declaration + fairness re-audit.
No threshold may be changed in production without all three.
```

## 8.3 Fast-Path Rule Override (Tier 1 — Applied BEFORE ML Inference)

When any of the following conditions are met, the risk tier is set by rule, bypassing the ML model. The ML score is still computed and stored for audit, but the tier is overridden.

```
OVERRIDE TO RED (immediate, no ML required):
  Condition A: f_consecutive_absences >= 5
  Condition B: f_days_since_last_login >= 21
  Condition C: f_attendance_rate < 0.20 AND f_lbi_current < 30
  Condition D: f_flag_ghost_user = TRUE AND f_flag_streak_collapse = TRUE
  Condition E: user has zero ABAA events in last 14 calendar days

OVERRIDE TO AMBER (fast-path, ML still runs):
  Condition F: f_consecutive_absences == 3 OR 4
  Condition G: f_lbi_trend <= -15 AND f_login_trend < 0
  Condition H: f_flag_assessment_abandonment = TRUE AND f_flag_skill_decay = TRUE

OVERRIDE REASON must be logged with the emitted risk event.
```

## 8.4 Minimum Data Guard

```
IF session_count < 3 for the user in the computation window:
  → DO NOT run inference
  → DO NOT emit risk_scored event
  → Log: DRPA_INSUFFICIENT_DATA
  → No risk tier assigned
  → No intervention triggered

Risk scoring begins only after 3 confirmed sessions.
```

---

# SECTION IX — EXPLAINABILITY FORMAT (SEALED)

## 9.1 SHAP-Based Feature Attribution

Every inference produces a SHAP value vector stored in the Feature Store with the feature_snapshot_id. SHAP values are human-readable only — they do not re-enter the scoring pipeline.

## 9.2 Human-Readable Explanation (Tier 3 — LLM, Tier-RED Only)

```
Trigger:     risk_tier == RED AND explanation_requested == true
Model:       claude-haiku-4-5 (cost-optimized, R28-3 compliant)
Input:       Top 5 SHAP features by absolute value + feature values + user_type_label
Output:      2–3 sentence plain-language explanation of contributing factors

CONSTRAINTS ON LLM EXPLANATION:
  MUST NOT mention the numeric risk_probability score
  MUST NOT use labels like "likely to drop out", "will fail", "at risk of quitting"
  MUST use constructive framing: "This learner may benefit from..."
  MUST NOT make recommendations that require human judgment (referrals, assessments)
  MUST be stored alongside SHAP snapshot — never displayed without it
  MUST be marked: explanation_type: LLM_GENERATED, NOT_A_DECISION
```

## 9.3 Explanation Delivery Rules

```
Student self-view:   Receives constructive summary only — NO risk tier label shown
                     ("You may benefit from more consistent session attendance")
Institute Admin:     Receives risk tier + top 3 contributing factor labels (feature names, not values)
Platform Ops:        Receives full SHAP breakdown + LLM explanation
Parent view:         NO risk information displayed without student consent (age 18+)
                     For minors: simplified "engagement summary" only — no risk labels
Employer:            NO dropout risk information shared under any circumstance
```

---

# SECTION X — EVALUATION METRICS (R12 COMPLIANT)

## 10.1 Model Quality Metrics

| Metric | Minimum Acceptable Threshold | Measurement Frequency |
|---|---|---|
| AUC-ROC | ≥ 0.78 | Per training run |
| Precision at Recall=0.80 | ≥ 0.65 | Per training run |
| F1-Score | ≥ 0.70 | Per training run |
| False Positive Rate (GREEN falsely flagged RED) | ≤ 0.05 | Per training run |

```
Model below any threshold → REJECTED → human review required before deployment
```

## 10.2 Operational Performance Metrics (Prometheus)

| Metric | SLA |
|---|---|
| Feature computation lag (event → Feature Store) | < 10 seconds |
| Model inference latency (p99) | < 500 ms |
| Risk event emission lag (inference → Kafka) | < 2 seconds |
| Daily risk scoring coverage (% of active users scored) | ≥ 95% |
| Stale data rate (users skipped due to ABAA gap > 48h) | < 5% |

---

# SECTION XI — FAIRNESS & BIAS GOVERNANCE (MANDATORY)

## 11.1 Protected Attributes (Never Used as Features)

```
FORBIDDEN as features — direct or proxy:
  Gender
  Religion
  Caste / Social category
  Region / State of origin
  Language
  College tier (direct input)
  Family income
  First-generation learner status
  Disability status

Any feature found to be a proxy for a protected attribute after SHAP analysis
→ REMOVE from feature catalog → version bump → retrain → re-audit
```

## 11.2 Fairness Metrics (Human-Evaluated Pre-Deployment)

| Fairness Check | Method | Threshold |
|---|---|---|
| Demographic parity across gender | AUC-ROC disparity | ≤ 0.05 gap |
| Equalized odds across user_type_code groups | TPR/FPR comparison | ≤ 0.05 gap |
| Calibration across tenant_id | Mean predicted vs actual dropout rate | ≤ 0.05 deviation |
| SHAP feature audit for proxy discrimination | Manual review by human | No protected attribute proxy in top 10 SHAP features |

```
Fairness audit failing any threshold → MODEL NOT DEPLOYED.
Fairness audit is human responsibility (M5 compliance).
```

## 11.3 Disparate Impact Log

```
Every batch scoring run produces a disparate_impact_log stored in MinIO.
Log contains: user_type_code distribution of RED-tier assignments,
              per-tenant RED rate, and SHAP feature dominance summary.
Log retained: 3 years (compliance requirement).
Log reviewed: quarterly by platform governance team.
```

---

# SECTION XII — RETRAINING SCHEDULE (SEALED)

## 12.1 Scheduled Retraining

```
Frequency:    Monthly (first Sunday of each month — Airflow DAG)
Data window:  Last 180 days of ABAA behavioral events from ClickHouse
Label window: 30-day forward window from feature snapshot date
Trigger:      Apache Airflow scheduled DAG: drpa_monthly_retrain
Human gate:   Model diff report must be reviewed and approved before swap
Rollback:     Previous model version retained in Model Registry for 90 days
```

## 12.2 Drift-Triggered Retraining

```
Trigger conditions (any one):
  - AUC-ROC on holdout set drops below 0.75 for 3 consecutive daily checks
  - RED tier assignment rate changes by > 15% week-over-week (population drift)
  - Mean feature value shift > 2 standard deviations in any top-5 SHAP feature
  - New major platform feature that generates new Kafka event types

Action:
  → Emit ecoskiller.drpa.model_anomaly_detected
  → Alert: ML Engineer + Platform Admin
  → Human initiates emergency retraining
  → Model frozen at current version until new model passes evaluation
```

## 12.3 Model Version Registry

```
Model artifact storage: MinIO bucket: drpa-models/
Version naming: drpa_v{MAJOR}.{MINOR}_{YYYY-MM-DD}
Each version stores:
  - Trained model binary (.pkl or .ubj)
  - Feature schema version
  - Evaluation metrics JSON
  - Fairness audit report
  - SHAP summary plot
  - Human sign-off log entry
Versions retained: Last 5 production versions minimum.
```

---

# SECTION XIII — INTERVENTION PROTOCOL (DETERMINISTIC)

## 13.1 Tier Classification → Intervention Matrix

This matrix is the sole authority for what happens when a risk tier is assigned or changes. No action outside this matrix is permitted.

| Risk Tier | User Type | Condition | Intervention Action | Actor |
|---|---|---|---|---|
| GREEN | Any learner | Stable | No action. Daily risk score updated silently. | System |
| AMBER | Student (Types 1–40) | First AMBER | Push notification: engagement nudge | Notification Service |
| AMBER | Student (Types 1–40) | AMBER for 3 consecutive scoring cycles | Email: personalized re-engagement message + streak recovery tip | Notification Service |
| AMBER | Student (Types 1–40) | AMBER for 7 consecutive cycles | Alert to Institute Counselor (Type 95) | Admin Governance → Counselor Dashboard |
| AMBER | Trainer (Types 41–75) | AMBER | Email: platform inactivity notice | Notification Service |
| RED | Student (Types 1–40) | First RED | Push + Email: re-engagement message + session reminder | Notification Service |
| RED | Student (Types 1–40) | RED + f_days_since_last_login >= 7 | Alert: Institute Placement Officer (Type 95) + Student Counselor | Admin Governance |
| RED | Student (Types 1–40) | RED + f_consecutive_absences >= 5 | Alert: Institute Admin (Type 77) flagged for human outreach | Admin Governance |
| RED | Minor student | RED (any condition) | Alert: Parent trust layer (daily digest, constructive framing only) | Notification Service → Parent |
| RED | Any | RED persists for 14 days without improvement | Escalation to Platform Ops (Type 232) + Tenant Admin | Admin Governance |
| RESOLVED | Any | Tier changes from RED/AMBER → GREEN | Emit risk_resolved event. Close open alerts. | System |

## 13.2 Intervention Content Rules

```
All notification content for AMBER and RED interventions:
  MUST use constructive, encouraging framing
  MUST NOT disclose the numeric risk probability to the user
  MUST NOT use the words "dropout", "risk", "failure", "at risk", "flagged"
  MUST include a clear, low-friction re-engagement call-to-action
  MUST be generated by Notification Service using defined templates
  MAY be personalized by LLM (Tier 3) at Tier-RED — see Section IX

All administrative alerts to Institute Admin / Counselor:
  MUST include: user_id, risk_tier, top 3 contributing feature labels (no raw values)
  MUST NOT include: risk_probability score, LLM explanation text
  MUST include: suggested human action (defined template — not LLM-generated)
  MUST be delivered via Admin Governance Service Dashboard, not raw notification
```

## 13.3 Intervention Outcome Tracking

```
Every triggered intervention is logged:
  - intervention_id (UUID)
  - user_id
  - risk_tier_at_trigger
  - intervention_type
  - delivered_at
  - opened_at (push/email open event, if available)
  - tier_30d_after_intervention (populated asynchronously)
  - resolved: boolean

Outcome data feeds into model retraining (as behavioral label enrichment).
Outcome log retained: 3 years.
```

---

# SECTION XIV — DASHBOARD SPECIFICATIONS

## 14.1 Role-Based Risk Dashboard Matrix

| Dashboard | Visible To | Content |
|---|---|---|
| Student Self-View | Student (self only) | Engagement health indicators (no risk labels), streak, LBI trend, re-engagement prompts |
| Parent Trust View | Parent (linked minor only) | "Engagement summary" only — no risk tier, no probability |
| Institute Counselor | Student Counselor (Type 95) | AMBER + RED students in their institution, top contributing factors, open interventions |
| Institute Admin | Institute Admin (Type 77), Placement Officer (Type 95) | Cohort risk heatmap, RED count, intervention log, trend over time |
| Employer (Batch Only) | Employer Hiring Manager (Type 122) | Aggregate completion rate for their GD/interview batches only — no individual risk data |
| Platform Ops | Analytics Admin (Types 258, 259), Platform Admin (Type 232) | All tenants, model health, RED rate by tenant, fairness metrics, retraining status |

## 14.2 Mandatory Dashboard Panels

**Institute Admin Risk Dashboard must include:**
```
- Cohort risk heatmap: student × risk_tier (current week)
- RED students count + trend (last 30 days)
- AMBER-to-RED conversion rate (intervention effectiveness signal)
- At-risk by program type (GD, Dojo, Course, Batch)
- Open intervention log (counselor actions pending)
- Export: CSV of AMBER + RED students (GDPR/DPDP compliant, anonymized option)
```

**Platform Ops Dashboard must include:**
```
- Daily scoring coverage %
- RED rate by tenant (anomaly detection)
- Intervention delivery success rate (push open, email open)
- Model AUC-ROC on daily holdout (drift monitor)
- Stale data rate (ABAA sync gap > 48h)
- Fairness metric last audit date + pass/fail
- Retraining pipeline last run + next scheduled
```

---

# SECTION XV — DATA GOVERNANCE (SEALED)

## 15.1 Retention Policy

| Data Type | Retention | Storage |
|---|---|---|
| Feature vectors (per scoring run) | 90 days | Feature Store (hot) |
| Risk scores (all tiers) | 1 year | ClickHouse |
| SHAP value snapshots | 1 year | Feature Store |
| Intervention logs | 3 years | PostgreSQL |
| Fairness audit reports | 3 years | MinIO |
| Model versions | 5 production versions minimum | MinIO (drpa-models/) |
| Disparate impact logs | 3 years | MinIO |
| LLM-generated explanations | 90 days | PostgreSQL (text field) |
| Minor user risk records | 90 days maximum unless consent extended | Flagged partition |

## 15.2 Privacy Constraints (Non-Negotiable)

```
PII (name, email, phone) NEVER stored in Feature Store or ClickHouse.
All stores reference user_id (UUID) only.
Name resolution at display time via User Service join only.
Risk probability scores NEVER visible to employers.
Risk labels NEVER shared to third parties without explicit user consent.
Cross-tenant risk data access: FORBIDDEN at database level (RLS enforced on tenant_id).
```

## 15.3 Consent Architecture

```
Consent required before:
  - Emitting risk data to Institute Admin (student consent or institutional enrollment agreement)
  - Emitting risk data to employer (explicit per-batch consent)
  - Storing minor user risk records (guardian consent)
  - Using student risk data in model retraining (terms of service consent, batch consent)

Consent registry in PostgreSQL (shared with ABAA):
  consent_type: RISK_SCORING | INSTITUTE_VISIBILITY | EMPLOYER_SHARE | MODEL_TRAINING

Consent missing → score computed internally for operational use
                → NOT emitted to any external surface
                → Logged as DRPA_CONSENT_GATED
```

---

# SECTION XVI — INTEGRATION CONTRACTS

## 16.1 Upstream Services (Signal Producers)

| Service | Signal Type | Contract Status |
|---|---|---|
| ABAA (Attendance Behavior Analytics Agent) | All behavioral signals | LOCKED |
| User Service | Profile completeness, user_type_code | LOCKED |
| Streak Engine (R57 / R95) | streak_count, streak_reset events | LOCKED |
| Scoring Engine | LBI score | LOCKED |
| Belt & Certification Engine | belt_level, certification events | LOCKED |
| Career Gap & Skill Obsolescence Service (R73) | skill.obsolete_detected events | LOCKED |
| Student Identity & Portfolio Service (R91) | influence_score, endorsement events | LOCKED |

## 16.2 Downstream Services (Consumers of DRPA output)

| Service | Kafka Topic Consumed | Purpose |
|---|---|---|
| Notification Service | drpa.intervention_triggered | Student/trainer nudges |
| Admin Governance Service | drpa.risk_scored, drpa.intervention_triggered | Institute/counselor alerts |
| Billing Service | drpa.risk_scored | Usage metering (risk computation as billable event) |
| Analytics Service | drpa.risk_scored | Platform-level dropout analytics |
| Model Registry Service | (internal) | Model artifact versioning |

## 16.3 API Contract (Sealed)

```
GET  /api/v1/drpa/risk/{user_id}                  — Current risk tier + top factors (role-gated)
GET  /api/v1/drpa/cohort/{batch_id}/summary        — Aggregate risk summary for batch
GET  /api/v1/drpa/institution/{tenant_id}/heatmap  — Risk heatmap data
GET  /api/v1/drpa/interventions/{user_id}          — Intervention history (role-gated)
GET  /api/v1/drpa/model/health                     — Model AUC, last retrain, fairness status
POST /api/v1/drpa/export/at-risk                   — CSV export (INSTITUTE_ADMIN+ only)

All endpoints:
  - JWT required
  - RBAC enforced via OPA
  - Tenant-scoped: cross-tenant request → 403 Forbidden
  - Rate limited: 100 req/min per user
  - risk_probability field: NEVER returned to student-role or employer-role callers
  - Response schema versioned
```

---

# SECTION XVII — DEVOPS & OBSERVABILITY

## 17.1 Kubernetes Deployment

```
Namespace: analytics (shared with ABAA)
Services:
  - drpa-feature-pipeline     (Kafka Streams processor, stateless, scalable)
  - drpa-inference-server     (XGBoost model server, stateless, horizontal)
  - drpa-intervention-router  (rule engine, stateless)
  - drpa-api-server           (REST API, stateless)
  - drpa-scheduler            (Airflow DAG runner — retraining, fairness checks)

All services: Docker containers, Helm charts, blue/green deploy.
Model swap: zero-downtime via model version toggle in Model Registry.
No manual production deploys permitted.
```

## 17.2 Required Observability

```
Metrics (Prometheus):
  drpa_features_computed_total          (counter, by user_type_code)
  drpa_inference_duration_seconds       (histogram, p50/p95/p99)
  drpa_risk_tier_distribution           (gauge, by tier × tenant_id)
  drpa_interventions_triggered_total    (counter, by intervention_type)
  drpa_stale_data_users_total           (gauge — users skipped due to ABAA gap)
  drpa_model_auc_current                (gauge — updated daily)
  drpa_scoring_coverage_percent         (gauge — % of active users scored daily)

Alerts:
  - drpa_model_auc_current < 0.75 → PagerDuty (CRITICAL)
  - drpa_stale_data_users_total > 5% of active users → Slack alert
  - drpa_risk_tier_distribution RED rate > 30% in any tenant → CRITICAL alert
  - drpa_inference_duration_seconds p99 > 1s → performance alert
  - Retraining DAG failure → PagerDuty (ML Engineer + Platform Admin)
```

---

# SECTION XVIII — FAILURE HANDLING (DETERMINISTIC)

| Failure | System Response |
|---|---|
| ABAA feed stale > 48 hours | Skip scoring → emit drpa.stale_data_warning → alert Platform Ops |
| Feature computation error | Log DRPA_FEATURE_FAILURE → skip user for this cycle → no tier assigned |
| Model inference timeout (3 retries) | Fall back to fast-path rule override (Section VIII.3) → log DRPA_ML_FALLBACK |
| Fast-path rule override conditions not met | Risk tier = previous tier (carry forward, max 3 cycles) → then alert |
| Model AUC below 0.75 (runtime check) | Freeze model at current version → alert ML engineer → human review required |
| Consent record missing | Score computed internally → NOT emitted → log DRPA_CONSENT_GATED |
| Minor protection violation | STOP → REPORT MINOR_PROTECTION_BREACH → escalate to DPO |
| Tenant isolation violation | STOP → REPORT TENANT_ISOLATION_BREACH → alert Security Admin |
| Intervention delivery failure (Notification Service down) | Retry 3x → log DRPA_INTERVENTION_UNDELIVERED → queue for next cycle |
| LLM explanation generation failure | Risk event emitted WITHOUT explanation → explanation_available: false → no block |

```
DRPA FAILURE PHILOSOPHY:
  A missing score is better than a wrong score.
  A held intervention is better than a misfired intervention.
  A degraded fallback is better than a silent failure.
  Every failure is logged before any other action.
  The model never auto-corrects. Humans correct.
```

---

# SECTION XIX — WHAT THIS AGENT NEVER DOES

```
NEVER labels a user as "likely to drop out" or "at risk of failure" in any user-facing surface
NEVER shares dropout risk scores with employers under any condition
NEVER acts on a risk score without a defined intervention rule
NEVER auto-removes, auto-suspends, or auto-restricts any user
NEVER uses protected demographic attributes as features (directly or as proxies)
NEVER deploys a new model version without human review and sign-off (M5)
NEVER uses an LLM to compute, adjust, or gate a risk score or intervention decision
NEVER scores a user with fewer than 3 sessions on record
NEVER operates on stale ABAA data older than 48 hours without emitting a warning
NEVER issues two conflicting risk tiers for the same user in the same 24-hour window
NEVER retains dropout risk scores beyond their defined retention window
NEVER claims a trained or deployed model without attached human execution logs (M5)
```

---

# SECTION XX — VERSION & CHANGE CONTROL

```
Document Version:     1.0
Status:               SEALED
Seal Date:            2026-03-04
Companion Document:   ATTENDANCE_BEHAVIOR_ANALYTICS_AGENT v1.0
Next Review:          Version bump required for any structural change

Allowed without version bump:
  - Add a new Kafka topic to consumed list (with new feature definition)
  - Add a new intervention row to Section XIII matrix (additive only)
  - Add a new dashboard panel (additive only)
  - Update R28-5 cost estimates (non-structural)

Requires version bump + human declaration + fairness re-audit:
  - Change any risk tier threshold in Section VIII.2
  - Change any fast-path rule override condition in Section VIII.3
  - Add or remove any feature from Section VII
  - Change model type or architecture
  - Change any fairness threshold in Section XI
  - Change any data retention period in Section XV
  - Change LLM model used for explanation generation
  - Add any new surface to Section IV scope

IMPLICIT BEHAVIOR = FORBIDDEN.
ALL behavior is declared.
ALL scoring is defined.
ALL features are catalogued.
ALL interventions are enumerated.
ALL outputs are auditable.
```

---

## ✅ SEAL CONFIRMATION

```
✔ Agent identity declared with R28 and M5 compliance
✔ R28 tier map complete — every feature and model classified
✔ M5 law respected — human training and deployment authority preserved
✔ Scope locked — in-scope surfaces, user types, and forbidden actions listed
✔ Architecture placement defined — consumer + emitter, never controller
✔ Kafka event schema sealed — canonical envelope specified
✔ Feature catalog locked — 33 features (26 computed + 7 derived flags), all formulas defined
✔ Risk scoring model declared — XGBoost, evaluation metrics, label definition
✔ Risk tier classification rule-based (Tier 1) — NOT ML
✔ Fast-path override rules enumerated — deterministic
✔ Minimum data guard enforced (< 3 sessions → no score)
✔ Explainability format defined — SHAP + LLM advisory text (Tier 3 only)
✔ Evaluation metrics complete (R12 compliant)
✔ Fairness governance sealed — protected attributes listed, metrics defined, human gate enforced
✔ Retraining schedule declared — monthly scheduled + drift-triggered (Airflow)
✔ Intervention matrix complete — all tier × user_type × condition rows enumerated
✔ Intervention content rules sealed — language constraints, framing rules
✔ Dashboard RBAC matrix complete
✔ Data governance sealed — retention, privacy, consent architecture
✔ Integration contracts locked — all upstream and downstream services named
✔ Observability requirements specified — Prometheus metrics + alert thresholds
✔ Failure handling deterministic — 11 failure modes with defined responses
✔ Prohibited behaviors explicitly listed — 13 hard constraints
✔ Change control enforced — version bump requirements defined

DRPA IS SEALED.
EXECUTION BEGINS ONLY ON HUMAN DECLARATION.
MODEL DEPLOYMENT STATUS: NOT CLAIMED UNTIL HUMAN EXECUTION LOG IS ATTACHED.
```
