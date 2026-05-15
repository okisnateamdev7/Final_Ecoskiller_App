# 🔒 SEALED & LOCKED AGENT SPECIFICATION
# INTELLIGENCE GROWTH TIME-SERIES AGENT
## Ecoskiller Antigravity — Enterprise Intelligence Core
---

```
DOCUMENT_CLASS         = PRODUCTION AGENT SPECIFICATION
ARTIFACT_VERSION       = v1.0.0
MUTATION_POLICY        = ADD-ONLY (versioned bump required for any change)
EXECUTION_MODE         = DETERMINISTIC + VALIDATED
CREATIVE_INTERPRETATION = FORBIDDEN
ASSUMPTION_FILLING     = FORBIDDEN
DEFAULT_BEHAVIOR       = DENY
FAILURE_MODE           = HALT ON AMBIGUITY
SEAL_STATUS            = LOCKED
```

---

## 🧬 SECTION 1 — AGENT IDENTITY (NON-NEGOTIABLE)

```yaml
AGENT_NAME:            INTELLIGENCE_GROWTH_TIME_SERIES_AGENT
AGENT_ID:              ECO-AG-IGTS-001
AGENT_CLASS:           Time-Series ML Agent (Primary) + LLM Assist (Secondary)
SYSTEM_ROLE:           Longitudinal Intelligence Growth Measurement & Prediction Engine
PRIMARY_DOMAIN:        Intelligence Analytics | User Growth Modeling | Predictive Trajectory
EXECUTION_MODE:        Deterministic + Validated
DATA_SCOPE:            User intelligence scores, test events, skill deltas, behavioral signals,
                       session metadata — per-user, per-domain, per-intelligence-dimension
TENANT_SCOPE:          STRICT ISOLATION — cross-tenant queries FORBIDDEN
FAILURE_POLICY:        HALT ON AMBIGUITY — no silent failure, no partial output
PLATFORM:              Ecoskiller Antigravity
ARCHITECTURE:          Microservices + Event-Driven
ML_RATIO:              75% Traditional ML (Time-Series, Regression, Classification)
AI_RATIO:              25% LLM-Assisted (Narrative generation, anomaly explanation)
SECURITY_MODEL:        Zero-Trust Multi-Tenant
DATA_POLICY:           Append-Only Audit Trail
DOMAIN_TRACKS:         Arts | Commerce | Science | Technology | Administration
```

---

## 🎯 SECTION 2 — PURPOSE DECLARATION

### 2.1 Problem Solved

Ecoskiller captures intelligence measurements across multiple assessment events (adaptive tests, dojo matches, championship rounds, project milestones, mentor certifications). Without longitudinal analysis, these are isolated data points. The **IGTS Agent** transforms scattered snapshots into a **continuous, verified growth curve per user across all nine intelligence dimensions** (per Gardner's Multiple Intelligences framework as scoped in the Ecoskiller system), enabling:

- Accurate **intelligence trajectory prediction** (3-month, 6-month, 12-month)
- Early detection of **plateau**, **regression**, or **acceleration** patterns
- **Career readiness signals** for the Recruiter System and AI Mentor System
- **Parental insights** on a child's developmental pace vs. age-cohort
- **Institute benchmarking** — cohort-level intelligence growth vs. national percentile

### 2.2 Input Consumed

| Input Source | Data Type | Frequency |
|---|---|---|
| Intelligence Assessment Events | Scored test result per dimension | On-demand (event-driven) |
| Dojo Match Scoring | Skill + behavioral score packages | Per match completion |
| Championship Results | Competitive performance data | Per championship event |
| Project Milestone Completions | Evidence-based skill signals | Per milestone gate |
| Passive Behavioral Signals (FEATURE_STORE_AGENT) | Feature vectors | Continuous stream |
| User Session Metadata | Engagement time, frequency, platform | Per session close |

### 2.3 Output Produced

| Output | Consumer | Format |
|---|---|---|
| Intelligence Growth Curve | User Dashboard, Parent Dashboard | Time-series vector |
| Intelligence Trajectory Score | AI Mentor Agent, Recruiter Agent | Float 0–100 per dimension |
| Growth Velocity Metric | Institute Dashboard, Analytics Engine | Delta per 30-day window |
| Regression / Plateau Alert | Alert Service, Mentor System | Structured event |
| Intelligence Prediction Vector | Recruiter Agent, Placement Engine | Predicted score + CI |
| Anomaly Flag | Observability Agent, Compliance Agent | Flag object |
| Rank Update Trigger | Growth Engine / Rank Agent | Structured event |

### 2.4 Upstream Agents

```
UPSTREAM_AGENTS:
  - ASSESSMENT_ENGINE_AGENT          (delivers scored events)
  - DOJO_SCORING_AGENT               (delivers match score packages)
  - CHAMPIONSHIP_RESULT_AGENT        (delivers competitive outcome vectors)
  - FEATURE_STORE_AGENT              (delivers behavioral feature vectors)
  - PROJECT_MILESTONE_AGENT          (delivers evidence-based completion signals)
  - SESSION_ANALYTICS_AGENT          (delivers engagement metadata)
```

### 2.5 Downstream Agents

```
DOWNSTREAM_AGENTS:
  - AI_MENTOR_AGENT                  (consumes trajectory + recommendations)
  - RECRUITER_INTELLIGENCE_AGENT     (consumes prediction vectors)
  - PARENT_INSIGHT_AGENT             (consumes growth summary, alerts)
  - INSTITUTE_ANALYTICS_AGENT        (consumes cohort intelligence curves)
  - OBSERVABILITY_AGENT              (consumes anomaly + drift metrics)
  - RANK_ENGINE_AGENT                (consumes XP + rank update triggers)
  - ALERT_SERVICE_AGENT              (consumes regression / plateau events)
  - FEATURE_STORE_AGENT              (receives emitted feature vectors back for feedback loop)
```

---

## 📥 SECTION 3 — INPUT CONTRACT (STRICT)

### 3.1 Primary Input Schema

```json
INPUT_SCHEMA: {
  "required_fields": [
    "user_id",
    "tenant_id",
    "domain_track",
    "intelligence_dimension",
    "assessment_score",
    "assessment_timestamp_utc",
    "source_agent_id",
    "event_type",
    "model_version_source"
  ],
  "optional_fields": [
    "confidence_score_source",
    "session_duration_seconds",
    "attempt_number",
    "scenario_id",
    "difficulty_level",
    "peer_score_component",
    "mentor_score_component",
    "self_score_component",
    "cohort_id",
    "championship_round_id"
  ],
  "validation_rules": [
    "user_id must be UUID v4 — reject if malformed",
    "tenant_id must match session JWT claim — reject if mismatch",
    "domain_track must be in [Arts, Commerce, Science, Technology, Administration]",
    "intelligence_dimension must be in [Linguistic, Logical-Mathematical, Spatial, Musical, Bodily-Kinesthetic, Interpersonal, Intrapersonal, Naturalist, Existential]",
    "assessment_score must be Float in range [0.0, 100.0] — reject if outside",
    "assessment_timestamp_utc must be ISO 8601 — reject if not parseable",
    "event_type must be in [ADAPTIVE_TEST, DOJO_MATCH, CHAMPIONSHIP, PROJECT_MILESTONE, BEHAVIORAL_SIGNAL]",
    "model_version_source must be a valid semver string",
    "No null values permitted in required_fields without explicit null_policy declaration"
  ],
  "security_checks": [
    "Validate tenant_id against session JWT",
    "Validate user_id belongs to tenant_id (row-level security check)",
    "Validate source_agent_id is a registered upstream agent",
    "Validate domain isolation: domain_track must match user's registered domain",
    "Reject any input containing cross-tenant user references"
  ],
  "domain_checks": [
    "domain_track must match the user's registered primary or secondary domain",
    "intelligence_dimension must be mapped to an active construct in SKILL_VALIDITY_FRAMEWORK",
    "If assessment origin is DOJO: require scoring_governance_version reference",
    "If assessment origin is CHAMPIONSHIP: require championship_id and round_id"
  ]
}
```

### 3.2 Input Rejection Policy

```yaml
REJECT_ON:
  - Malformed UUID
  - Score out of bounds
  - Unknown event_type
  - Missing required_fields
  - Tenant mismatch
  - Domain isolation violation
  - Unknown intelligence_dimension
  - Unregistered source_agent_id

ON_REJECT:
  - LOG_VALIDATION_FAILURE (append-only audit log)
  - EMIT_REJECTION_EVENT to Observability Agent
  - RETURN structured rejection object (see below)
  - DO NOT process partial input
  - DO NOT assume missing values

REJECTION_OBJECT: {
  "rejection_id": "UUID",
  "timestamp_utc": "ISO 8601",
  "input_hash": "SHA-256 of rejected payload",
  "rejection_reason": "string",
  "field_violations": ["list of fields failed"],
  "source_agent": "string",
  "audit_reference": "UUID"
}
```

---

## 📤 SECTION 4 — OUTPUT CONTRACT (STRICT)

### 4.1 Primary Output Schema

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "user_id": "UUID",
    "tenant_id": "UUID",
    "intelligence_dimension": "string",
    "domain_track": "string",
    "growth_curve": [
      {
        "timestamp_utc": "ISO 8601",
        "score": "Float [0-100]",
        "source_event_type": "string",
        "confidence": "Float [0-1]"
      }
    ],
    "current_score": "Float [0-100]",
    "growth_velocity_30d": "Float (delta per 30 days)",
    "growth_velocity_90d": "Float (delta per 90 days)",
    "trajectory_classification": "ACCELERATING | STEADY | PLATEAU | REGRESSING",
    "predicted_score_90d": "Float [0-100]",
    "predicted_score_180d": "Float [0-100]",
    "predicted_score_365d": "Float [0-100]",
    "prediction_confidence_interval": {
      "lower_bound": "Float",
      "upper_bound": "Float",
      "ci_level": "0.95"
    },
    "percentile_rank": "Float [0-100]",
    "cohort_comparison_delta": "Float",
    "anomaly_flags": ["list of AnomalyFlag objects"],
    "alerts": ["list of Alert objects"],
    "rank_trigger": "RANK_UPDATE_EVENT | null",
    "xp_trigger": "XP_UPDATE_EVENT | null"
  },
  "confidence_score": "Float [0-1]",
  "model_version": "semver string",
  "audit_reference": "UUID",
  "next_trigger_events": [
    "RANK_UPDATE_EVENT",
    "XP_UPDATE_EVENT",
    "SHARE_TRIGGER_EVENT",
    "ALERT_EVENT",
    "MENTOR_RECOMMENDATION_EVENT"
  ],
  "processing_metadata": {
    "data_points_used": "int",
    "time_window_days": "int",
    "drift_flag": "boolean",
    "model_health": "HEALTHY | DEGRADED | UNAVAILABLE"
  }
}
```

### 4.2 Output Guarantees

```yaml
ALL_OUTPUTS_MUST_INCLUDE:
  - confidence_score (Float 0-1)
  - model_version (semver, immutable reference)
  - audit_reference (UUID, append-only log entry)
  - next_trigger_events (may be empty array, never null)

LOW_CONFIDENCE_POLICY:
  - If confidence_score < 0.60: EMIT alert, flag output, DO NOT trigger belt promotion
  - If confidence_score < 0.40: HALT output, escalate to OBSERVABILITY_AGENT
  - Mentor board review required before any certification action on low-confidence output
```

---

## 🤖 SECTION 5 — ML / AI LOGIC LAYER

### 5.1 ML Layer (75% — Primary Intelligence)

```yaml
MODEL_TYPE:
  Primary:   Time-Series Forecasting (Prophet / LSTM hybrid)
  Secondary: Regression (Gradient Boosting — XGBoost / LightGBM)
  Tertiary:  Classification (Trajectory State — Random Forest)

FEATURES_USED:
  Temporal:
    - score_at_t_minus_7d
    - score_at_t_minus_30d
    - score_at_t_minus_90d
    - rolling_mean_score_30d
    - rolling_std_score_30d
    - assessment_frequency_30d
    - days_since_last_assessment
  
  Behavioral:
    - session_engagement_score
    - streak_length_days
    - dojo_participation_frequency
    - championship_attempt_count
    - self_initiated_vs_prompted_ratio
  
  Contextual:
    - difficulty_level_distribution
    - domain_track_encoded
    - intelligence_dimension_encoded
    - cohort_age_percentile
    - platform_tenure_days
    - mentor_interaction_count
  
  Scoring Provenance:
    - peer_score_weight
    - mentor_score_weight
    - self_score_weight
    - scoring_confidence_avg
    - inter_rater_reliability_index

TRAINING_FREQUENCY:
  Time-Series Model:    Weekly (Sunday 02:00 UTC)
  Regression Model:     Monthly (1st of month, 03:00 UTC)
  Classification Model: Monthly (1st of month, 04:00 UTC)
  
  Retrain trigger: Drift detection threshold exceeded (see Section 5.3)

PREDICTION_HORIZONS:
  - 30-day forward score
  - 90-day forward score
  - 180-day forward score
  - 365-day forward score
  (All with 95% CI bounds)

MINIMUM_DATA_POINTS:
  - Minimum 3 assessment events required to generate curve
  - Minimum 7 events required to generate trajectory classification
  - Minimum 14 events required for prediction with CI < 15 points width
  - Below minimums: emit INSUFFICIENT_DATA flag, do not generate prediction
```

### 5.2 AI Layer (25% — Assist Only)

```yaml
AI_USAGE_SCOPE:
  PERMITTED:
    - Generate natural-language growth narrative for Parent Dashboard
    - Generate natural-language anomaly explanation for Mentor Alert
    - Generate natural-language career readiness summary for Recruiter Agent
    - Explain trajectory classification in human-readable form
  
  FORBIDDEN:
    - AI may NOT override ML score calculation
    - AI may NOT generate intelligence scores
    - AI may NOT make belt promotion decisions
    - AI may NOT modify audit records
    - AI may NOT autonomously trigger rank updates
    - AI interpretation beyond defined scope FORBIDDEN

PROMPT_GOVERNANCE:
  - All LLM prompts are versioned (semver)
  - Prompts are deterministic in structure — no open-ended generation
  - Prompt version stored in audit log per execution
  - LLM output is post-processed and validated before delivery
  - Hallucination guard: numeric values in LLM output must match ML output exactly
  - If LLM output contradicts ML output: DISCARD LLM output, deliver ML output only

LLM_MODEL_SCOPE:
  - Used for text generation assistance only
  - No decision authority
  - No data access beyond the structured ML result object passed to it
```

### 5.3 Drift Detection

```yaml
DRIFT_DETECTION:
  Score_Distribution_Drift:
    - Monitor: KS-test on score distributions weekly
    - Alert threshold: p-value < 0.05
    - Action: Flag model for retraining, notify OBSERVABILITY_AGENT

  Prediction_Accuracy_Drift:
    - Monitor: MAE of 30-day predictions vs actuals, rolling 4-week window
    - Alert threshold: MAE degrades > 20% from baseline
    - Action: Trigger emergency retraining, hold predictions flagged as DEGRADED

  Feature_Distribution_Drift:
    - Monitor: Population Stability Index (PSI) on top 10 features weekly
    - Alert threshold: PSI > 0.20
    - Action: Retrain with updated feature normalization

  Rater_Bias_Drift (from Dojo Scoring):
    - Monitor: Mentor score variance from DOJO_SCORING_AGENT calibration reports
    - Alert threshold: Mentor score drift > 2σ from calibration baseline
    - Action: Flag affected scores, request mentor re-calibration before including in curve

VERSION_CONTROL:
  - Each trained model version tagged with semver
  - Model artifact stored immutably (append-only model registry)
  - model_version referenced in every output
  - Rollback to previous version supported
  - No model version deletion
```

---

## ⚡ SECTION 6 — SCALABILITY DESIGN

```yaml
EXPECTED_RPS:            5,000 RPS (current); 50,000 RPS (100M user scale)
LATENCY_TARGET:          p50 < 120ms | p95 < 400ms | p99 < 800ms
MAX_CONCURRENCY:         10,000 parallel evaluation jobs
QUEUE_STRATEGY:          Kafka topic per domain_track; partitioned by user_id hash
PROCESSING_MODEL:        Async (event-driven); no synchronous blocking calls
SCALING_MODEL:           Horizontal — stateless execution pods
IDEMPOTENCY:             All operations idempotent; duplicate events safely skipped via event_id dedup
STATE_STORAGE:           TimescaleDB (time-series scores); Redis (ephemeral feature cache, TTL 5min)
BATCH_PROCESSING:        Nightly batch recalculation for cohort percentile ranks (02:00 UTC)
REALTIME_PROCESSING:     On-event curve update triggered within 2s of upstream event arrival
CACHE_STRATEGY:
  - User growth curve: cached in Redis, TTL 10min, invalidated on new score event
  - Cohort percentile: cached in Redis, TTL 4h, refreshed nightly batch
```

---

## 🔐 SECTION 7 — SECURITY ENFORCEMENT

```yaml
MANDATORY_CONTROLS:
  Tenant Isolation:
    - All queries scoped to tenant_id from JWT
    - Row-level security enforced at DB layer (TimescaleDB RLS)
    - Cross-tenant query = IMMEDIATE HALT + SECURITY_INCIDENT log

  Domain Isolation:
    - domain_track validated against user's registered domain
    - Cross-domain data access forbidden without explicit ABAC grant
    - Domain leaks classified as SECURITY_FAILURE

  Authorization:
    - RBAC + ABAC enforced per route
    - Role hierarchy: Platform Admin > Tenant Admin > Institute > Recruiter > Mentor > Parent > Student
    - Parent role: Read-only access to child's data only
    - Recruiter role: Aggregate scores only, no raw assessment detail without candidate consent

  Encryption:
    - All data at rest: AES-256
    - All data in transit: TLS 1.3
    - PII fields: field-level encryption
    - Secrets via Secret Manager only (no env plaintext in production)

  Audit Logging:
    - Append-only audit log (immutable)
    - Every execution logged (see Section 8)
    - Access log per user_id, per agent call
    - Log tamper detection via SHA-256 chaining

  API Security:
    - JWT short-lived access tokens
    - Rate limiting: per IP + per user_id
    - WAF in front of all API surfaces
    - Abuse detection thresholds active

FORBIDDEN:
  - Cross-tenant queries
  - Plaintext PII in logs
  - Agent self-modifying audit records
  - Score override without audit trail
  - Any operation outside defined scope
```

---

## 📋 SECTION 8 — AUDIT & TRACEABILITY

Every execution MUST produce an immutable audit record:

```json
AUDIT_RECORD: {
  "audit_id":              "UUID (primary key)",
  "timestamp_utc":         "ISO 8601",
  "agent_id":              "ECO-AG-IGTS-001",
  "agent_version":         "semver",
  "actor_id":              "user_id or system_agent_id triggering the call",
  "tenant_id":             "UUID",
  "user_id":               "UUID (subject of analysis)",
  "input_hash":            "SHA-256 of input payload",
  "output_hash":           "SHA-256 of output payload",
  "model_version":         "semver of ML model used",
  "prompt_version":        "semver of LLM prompt if used (null if ML-only)",
  "decision_path":         "string — trajectory classification path taken",
  "confidence_score":      "Float [0-1]",
  "data_points_used":      "int",
  "drift_flag":            "boolean",
  "anomaly_flags":         ["list of anomaly codes"],
  "processing_duration_ms":"int",
  "next_trigger_events":   ["list of triggered events"],
  "rejection_flag":        "boolean",
  "rejection_reason":      "string | null",
  "previous_audit_hash":   "SHA-256 of previous record (chain integrity)"
}
```

```yaml
AUDIT_POLICY:
  - Logs are IMMUTABLE — no update, no delete
  - Stored in append-only audit table (PostgreSQL with WAL)
  - Chain integrity verified via SHA-256 linking
  - Audit export available to Tenant Admin and Compliance Agent
  - Retention: minimum 7 years (configurable by tenant compliance policy)
```

---

## 🚨 SECTION 9 — FAILURE POLICY

```yaml
FAILURE_SCENARIOS:

  INVALID_INPUT:
    Action:       STOP_EXECUTION
    Log:          LOG_VALIDATION_FAILURE (audit trail)
    Escalate:     EMIT rejection event to source agent + OBSERVABILITY_AGENT
    Retry:        NO — caller must fix and resubmit
    Output:       Structured rejection object (Section 3.2)

  MODEL_UNAVAILABLE:
    Action:       STOP_EXECUTION
    Log:          LOG_INCIDENT (severity: HIGH)
    Escalate:     ESCALATE_TO = OBSERVABILITY_AGENT + PLATFORM_ADMIN
    Retry:        YES — exponential backoff, max 3 attempts, 5s / 15s / 45s
    Output:       SERVICE_UNAVAILABLE event (no partial output)

  AI_TIMEOUT (LLM layer):
    Action:       CONTINUE — deliver ML output only, skip LLM narrative
    Log:          LOG_INCIDENT (severity: LOW)
    Escalate:     Notify OBSERVABILITY_AGENT
    Retry:        YES — 1 retry after 3s, then skip
    Output:       ML result delivered, LLM narrative field = null with flag "NARRATIVE_UNAVAILABLE"

  DATA_CORRUPTION (integrity check failure):
    Action:       STOP_EXECUTION
    Log:          LOG_INCIDENT (severity: CRITICAL)
    Escalate:     ESCALATE_TO = PLATFORM_ADMIN + COMPLIANCE_AGENT
    Retry:        NO — manual investigation required
    Output:       CORRUPTION_ALERT event

  CONFIDENCE_BELOW_THRESHOLD (< 0.60):
    Action:       DELIVER output with DEGRADED_CONFIDENCE flag
    Log:          LOG_LOW_CONFIDENCE
    Escalate:     EMIT alert to ALERT_SERVICE_AGENT + AI_MENTOR_AGENT
    Retry:        NO — wait for new assessment event
    Restriction:  DO NOT trigger belt promotion, rank change, or certification

  CONFIDENCE_CRITICALLY_LOW (< 0.40):
    Action:       STOP_EXECUTION, DO NOT deliver output
    Log:          LOG_INCIDENT (severity: HIGH)
    Escalate:     ESCALATE_TO = OBSERVABILITY_AGENT
    Retry:        NO

  INSUFFICIENT_DATA (< 3 assessment events):
    Action:       STOP_EXECUTION
    Log:          LOG_INSUFFICIENT_DATA
    Output:       STRUCTURED flag: { "status": "INSUFFICIENT_DATA", "minimum_required": 3, "current_count": N }
    Escalate:     EMIT to AI_MENTOR_AGENT to prompt user engagement

POLICY_SUMMARY:
  NO_SILENT_FAILURES = TRUE
  PARTIAL_OUTPUT = FORBIDDEN
  ASSUMPTION_ON_FAILURE = FORBIDDEN
```

---

## 🔗 SECTION 10 — INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  ASSESSMENT_ENGINE_AGENT:
    Event:        ASSESSMENT_COMPLETED
    Payload:      { user_id, dimension, score, confidence, timestamp }
    SLA:          Delivered within 5s of assessment submission
  
  DOJO_SCORING_AGENT:
    Event:        MATCH_SCORED
    Payload:      { user_id, skill_dimension, weighted_score, peer/mentor/self components, match_id }
    SLA:          Delivered within 10s of match finalization
    Dependency:   Scoring must pass DOJO scoring_governance_version check
  
  CHAMPIONSHIP_RESULT_AGENT:
    Event:        CHAMPIONSHIP_ROUND_COMPLETE
    Payload:      { user_id, round_id, performance_vector, ranking }
    SLA:          Delivered within 30s of round finalization
  
  FEATURE_STORE_AGENT:
    Event:        FEATURE_VECTOR_UPDATED
    Payload:      { user_id, feature_name, feature_value, timestamp, source_agent }
    SLA:          Batch-delivered every 5 minutes
  
  PROJECT_MILESTONE_AGENT:
    Event:        MILESTONE_VERIFIED
    Payload:      { user_id, milestone_id, skill_signals, evidence_hash }
    SLA:          Delivered within 15s of verification
  
  SESSION_ANALYTICS_AGENT:
    Event:        SESSION_CLOSED
    Payload:      { user_id, duration_seconds, engagement_score, platform }
    SLA:          Delivered within 5s of session close

DOWNSTREAM_AGENTS:
  AI_MENTOR_AGENT:
    Trigger:      TRAJECTORY_COMPUTED
    Payload:      { trajectory_classification, velocity, predicted_scores, growth_narrative }
  
  RECRUITER_INTELLIGENCE_AGENT:
    Trigger:      INTELLIGENCE_PROFILE_UPDATED
    Payload:      { user_id, dimension_scores, prediction_vector, confidence }
    Restriction:  Aggregated — no raw test detail without consent
  
  PARENT_INSIGHT_AGENT:
    Trigger:      GROWTH_SUMMARY_READY
    Payload:      { child_id, growth_curve_summary, alerts, cohort_rank }
    Restriction:  Read-only, no mutation authority
  
  INSTITUTE_ANALYTICS_AGENT:
    Trigger:      COHORT_METRICS_UPDATED
    Payload:      { cohort_id, intelligence_distribution, growth_velocity_avg }
  
  OBSERVABILITY_AGENT:
    Trigger:      ANOMALY_DETECTED | DRIFT_DETECTED | INCIDENT_LOGGED
    Payload:      { agent_id, alert_type, severity, context }
  
  RANK_ENGINE_AGENT:
    Trigger:      RANK_UPDATE_EVENT
    Payload:      { user_id, dimension, new_percentile, previous_percentile }
  
  ALERT_SERVICE_AGENT:
    Trigger:      REGRESSION_DETECTED | PLATEAU_DETECTED | LOW_CONFIDENCE
    Payload:      { user_id, alert_type, dimension, context }

EVENT_BUS:               Kafka
EVENT_FORMAT:            Apache Avro (schema registry enforced)
EVENT_DELIVERY:          At-least-once with idempotency guard at consumer
```

---

## 🌊 SECTION 11 — PASSIVE INTELLIGENCE COMPATIBILITY

This agent both **consumes from** and **emits to** the FEATURE_STORE_AGENT:

```yaml
EMIT_FEATURE_VECTOR_ON_EACH_EXECUTION:
  {
    "user_id":        "UUID",
    "feature_name":   "intelligence_growth_velocity_30d | trajectory_class | predicted_score_90d | ...",
    "feature_value":  "Float | String (encoded)",
    "timestamp":      "ISO 8601",
    "source_agent":   "INTELLIGENCE_GROWTH_TIME_SERIES_AGENT"
  }

FEATURES_EMITTED:
  - intelligence_growth_velocity_30d    (Float)
  - intelligence_growth_velocity_90d    (Float)
  - trajectory_classification_encoded   (Int: 0=REGRESSING, 1=PLATEAU, 2=STEADY, 3=ACCELERATING)
  - predicted_score_90d                 (Float per dimension)
  - current_percentile_rank             (Float)
  - assessment_frequency_30d            (Int)
  - anomaly_flag_count_30d              (Int)

PURPOSE:
  These features feed into:
  - RECRUITER_INTELLIGENCE_AGENT (match scoring)
  - AI_MENTOR_AGENT (guidance personalization)
  - CHAMPIONSHIP_SEEDING_AGENT (fairness pairing)
  - PLACEMENT_PROBABILITY_AGENT (hiring predictions)
```

---

## 📈 SECTION 12 — GROWTH ENGINE HOOK

```yaml
ON_TRAJECTORY_MILESTONE:
  Conditions that trigger growth events:
    - User crosses percentile band (e.g., 50th → 60th → 75th → 90th)
    - trajectory_classification transitions to ACCELERATING
    - 90-day predicted score exceeds target (user-set goal)
    - User achieves top 10% in cohort for any dimension
    - Streak of 30+ days with positive growth velocity

  Events Emitted:
    RANK_UPDATE_EVENT:
      { user_id, dimension, old_rank, new_rank, timestamp }
    
    XP_UPDATE_EVENT:
      { user_id, xp_earned, reason: "INTELLIGENCE_GROWTH_MILESTONE", dimension }
    
    SHARE_TRIGGER_EVENT:
      { user_id, achievement_type: "INTELLIGENCE_GROWTH", share_card_data }

BELT_PROMOTION_POLICY:
  - This agent INFORMS the Belt Engine via structured output
  - This agent DOES NOT directly promote belts
  - Belt promotion requires: match_count + score_threshold + pressure_scenario_pass + mentor_certification
  - Low-confidence outputs (< 0.60) BLOCKED from informing belt promotion
  - Auto-promotion FORBIDDEN (per Dojo governance lock)
```

---

## 📊 SECTION 13 — PERFORMANCE MONITORING

```yaml
AGENT_METRICS (reported to OBSERVABILITY_AGENT):
  Success_Rate:
    Target:  > 99.5% (excluding caller-side validation failures)
    Alert:   < 98%
  
  Error_Rate:
    Target:  < 0.5%
    Alert:   > 1%
  
  Latency_P50:
    Target:  < 120ms
    Alert:   > 200ms
  
  Latency_P95:
    Target:  < 400ms
    Alert:   > 600ms
  
  Latency_P99:
    Target:  < 800ms
    Alert:   > 1200ms
  
  Prediction_MAE_30d:
    Target:  < 5 score points
    Alert:   > 8 score points
  
  Drift_Indicator:
    Monitor: Weekly — KS-test + PSI
    Alert:   Threshold breach as defined in Section 5.3
  
  Anomaly_Frequency:
    Monitor: Anomaly flag rate per 1000 executions
    Baseline: Established over first 30 days of production
    Alert:   > 2x baseline rate
  
  Confidence_Score_Distribution:
    Monitor: Rolling 7-day average confidence
    Alert:   Average drops below 0.70
  
  Queue_Lag:
    Target:  Kafka consumer lag < 500 messages
    Alert:   > 2000 messages lag
    Critical: > 10,000 messages lag

DASHBOARD:
  - Grafana: Real-time agent health dashboard
  - Panels: Success rate, latency percentiles, drift indicator, confidence distribution, queue lag
  - Alerts: PagerDuty integration for severity HIGH + CRITICAL
```

---

## 🔄 SECTION 14 — VERSIONING POLICY

```yaml
VERSIONING_RULES:
  Format:             Semantic versioning (MAJOR.MINOR.PATCH)
  Agent_Version:      ECO-AG-IGTS-001 v1.0.0
  Changes:
    PATCH bump:       Bug fix, performance optimization, log improvement
    MINOR bump:       New optional output field, new optional feature, new metric
    MAJOR bump:       Schema change, scoring logic change, model architecture change
  
  MUTATION_POLICY:    ADD-ONLY
    - New fields may be added to output schema
    - Existing fields may NOT be renamed or removed
    - Schema extensions must be backward-compatible
    - Consumers must be tolerant of unknown fields
  
  MODEL_VERSIONING:
    - Every trained model version is immutable and stored
    - model_version referenced in every output and audit record
    - No model deletion
    - Rollback to previous model version supported without data migration
  
  MIGRATION_POLICY:
    - All schema migrations documented
    - Migrations are versioned and reversible
    - Blue/green deployment required for MAJOR version changes
    - Backward compatibility window: minimum 90 days after MAJOR change
  
  PROMPT_VERSIONING (LLM):
    - Every LLM prompt version stored in prompt registry
    - prompt_version logged in audit record
    - Prompt changes require MINOR or MAJOR bump depending on scope
```

---

## 🚫 SECTION 15 — NON-NEGOTIABLE RULES

```yaml
THIS AGENT MUST NOT:
  - Create hidden scoring logic outside documented ML pipeline
  - Modify historical intelligence score records
  - Delete or modify audit log entries
  - Override decisions made by BELT_ENGINE or CERTIFICATION agents
  - Bypass RBAC/ABAC checks under any condition
  - Mix data across tenant boundaries
  - Execute outside the defined input/output contract
  - Generate intelligence scores using LLM (ML-only for scoring)
  - Promote belts or certifications autonomously
  - Produce output when input validation fails
  - Produce partial output on model failure
  - Assume missing input values
  - Interpret ambiguous input — HALT instead
  - Make domain-crossing queries without explicit ABAC grant
  - Store PII in unencrypted fields
  - Access external APIs not listed in approved upstream dependencies
  - Suppress anomaly flags from audit record
  - Execute creative LLM interpretation beyond defined narrative scope
```

---

## 🔒 FINAL GOVERNANCE SEAL

```
╔══════════════════════════════════════════════════════════════════╗
║    INTELLIGENCE GROWTH TIME-SERIES AGENT — GOVERNANCE SEAL       ║
╠══════════════════════════════════════════════════════════════════╣
║  Platform:              Ecoskiller Antigravity                    ║
║  Agent ID:              ECO-AG-IGTS-001                          ║
║  Version:               v1.0.0                                   ║
║  Seal Date:             2026-02-26 UTC                           ║
╠══════════════════════════════════════════════════════════════════╣
║  EXECUTION MODE:        DETERMINISTIC                            ║
║  MUTATION POLICY:       ADD-ONLY VERSIONED                       ║
║  CREATIVE INTERPRET:    FORBIDDEN                                ║
║  ASSUMPTION FILLING:    FORBIDDEN                                ║
║  SILENT FAILURE:        FORBIDDEN                                ║
║  CROSS-TENANT ACCESS:   FORBIDDEN                                ║
║  AUTO BELT PROMOTION:   FORBIDDEN                                ║
╠══════════════════════════════════════════════════════════════════╣
║  ML PRIMARY:            TIME-SERIES FORECASTING + REGRESSION     ║
║  AI ASSIST:             LLM NARRATIVE ONLY (no decision auth)    ║
║  DRIFT DETECTION:       ACTIVE                                   ║
║  AUDIT TRAIL:           IMMUTABLE APPEND-ONLY                    ║
║  TENANT ISOLATION:      HARD ENFORCED                            ║
║  DOMAIN ISOLATION:      HARD ENFORCED                            ║
║  SECURITY MODEL:        ZERO-TRUST                               ║
╠══════════════════════════════════════════════════════════════════╣
║  UPSTREAM COUNT:        6 registered agents                      ║
║  DOWNSTREAM COUNT:      8 registered agents                      ║
║  EVENT BUS:             KAFKA (Avro schema registry)             ║
║  FEATURE STORE:         BIDIRECTIONAL (consume + emit)           ║
║  GROWTH ENGINE HOOK:    ACTIVE                                   ║
║  OBSERVABILITY:         INTEGRATED (Grafana + PagerDuty)         ║
╠══════════════════════════════════════════════════════════════════╣
║  INTERPRETATION AUTH:   NONE                                     ║
║  ARCHITECTURE AUTH:     LOCKED                                   ║
║  SPEC AUTHORITY:        ECOSKILLER PLATFORM GOVERNANCE           ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*This document is a sealed production artifact. Any modification requires a version bump and governance board approval. The agent must not deviate from this specification under any operating condition.*

**END LOCKED ARTIFACT — INTELLIGENCE_GROWTH_TIME_SERIES_AGENT v1.0.0**
