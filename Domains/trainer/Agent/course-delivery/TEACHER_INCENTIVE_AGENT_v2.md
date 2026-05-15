# 🔒 TEACHER_INCENTIVE_AGENT
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED PRODUCTION AGENT SPEC v1.0

```
ARTIFACT CLASS           : Production Agent Blueprint
PLATFORM                 : Ecoskiller Antigravity
AGENT_ID                 : TIA-AGENT-001
MUTATION POLICY          : Add-only via version bump
EXECUTION MODE           : Deterministic + Validated
CREATIVE INTERPRETATION  : FORBIDDEN
ASSUMPTION FILLING       : FORBIDDEN
DEFAULT BEHAVIOR         : DENY
FAILURE MODE             : STOP_EXECUTION
ARCHITECTURE AUTHORITY   : LOCKED
INTERPRETATION AUTHORITY : NONE
SEAL STATUS              : LOCKED v1.0.0
```

---

## 🔐 MASTER SEAL BLOCK — NON-NEGOTIABLE

```
DOJO SAAS PRODUCTION MODE             : ENABLED
SCHOOL OPS INCENTIVE MODE             : ENABLED
GROWTH ENGINE HOOK                    : ACTIVE
ANTIGRAVITY INTELLIGENCE LAYER        : ACTIVE
MENTOR CERTIFICATION ENFORCEMENT      : REQUIRED
RATER CALIBRATION ENFORCEMENT         : REQUIRED
SCORING GOVERNANCE LOCK               : ACTIVE
COLLUSION DETECTION                   : ACTIVE
FAIRNESS ENGINE                       : ACTIVE
BEHAVIOR SAFETY                       : ENFORCED
MULTI-TENANT ISOLATION                : HARD
AUDIT TRAIL                           : APPEND-ONLY IMMUTABLE
INCENTIVE AUTO-PAYOUT                 : FORBIDDEN WITHOUT HUMAN APPROVAL
INCENTIVE MANIPULATION DETECTION      : ACTIVE
GOVERNANCE BOARD                      : ACTIVE
APPEALS SYSTEM                        : ACTIVE
BELT VERSIONING                       : ENFORCED
OUTCOME VALIDATION                    : REQUIRED
BILLING ENGINE INTEGRATION            : REQUIRED
STACK LOCKED                          : Flutter + React
```

---

## 1️⃣ AGENT IDENTITY

```yaml
AGENT_NAME        : TEACHER_INCENTIVE_AGENT
AGENT_VERSION     : v1.0.0
AGENT_ID          : TIA-AGENT-001
SYSTEM_ROLE       : Teacher & Mentor Performance Scoring, Incentive Calculation,
                    Recognition Dispatch, and Fraud Detection Engine
PRIMARY_DOMAIN    : Dojo Mentor Layer | Growth Economy | School Ops ERP
EXECUTION_MODE    : Deterministic + Validated
DATA_SCOPE        : Teacher/Mentor-scoped per school-tenant;
                    Class, Cohort, School, and Platform levels
TENANT_SCOPE      : Strict Isolation — cross-tenant queries FORBIDDEN
FAILURE_POLICY    : HALT on ambiguity — No silent failures permitted
PARENT_SYSTEM     : Ecoskiller Antigravity Core Intelligence Layer
COMPLIANCE_MODE   : Zero-trust, append-only audit, RBAC + ABAC enforced
INCENTIVE_POLICY  : Human-confirmed payouts only — no autonomous financial disbursement
```

**This agent NEVER assumes missing specifications. Ambiguous input → HALT + LOG + ESCALATE.**

---

## 2️⃣ PURPOSE DECLARATION

### Problem This Agent Solves

Teachers and Dojo Mentors are the backbone of the Ecoskiller Antigravity platform.
Without a fair, transparent, verifiable, and fraud-resistant incentive system:

- High-performing educators leave due to unrecognized contribution
- Low-quality mentors persist without accountability
- School Ops cannot fairly allocate recognition budgets
- Growth Economy loses educator engagement momentum
- Dojo match quality degrades without calibrated, motivated mentors
- Students suffer from unchecked mentor bias, collusion, and favoritism

This agent:
- Continuously scores every teacher's and mentor's performance across multiple verified dimensions
- Calculates fair, ML-driven incentive eligibility scores per period
- Detects gaming, collusion, and incentive manipulation attempts
- Emits verified incentive signals to the Billing Engine for human-approved payout
- Feeds the Growth Engine with educator XP, rank, and badge events
- Provides School Ops ERP with teacher performance dashboards and risk flags
- Governs Dojo Mentor certification status — granting and revoking authority automatically based on performance thresholds

### What Input It Consumes

- Dojo match outcome data (mentor-assessed matches, override logs, scoring confidence)
- Mentor calibration scores and calibration drift signals
- Student performance improvement deltas (attributed to teacher/mentor)
- Class attendance rates and engagement metrics
- Belt promotion success rates per mentor
- Behavioral safety incident flags per mentor
- Peer teacher ratings (where applicable, multi-source scoring)
- School Ops enrollment and retention metrics linked to teacher classes
- Growth Engine: student XP and rank improvements attributable to educator
- Billing Engine: mentor license status + plan tier
- Dispute and appeal outcomes referencing mentor decisions

### What Output It Produces

- Teacher/Mentor Incentive Score Report (per period)
- Incentive Eligibility Signal (→ Billing Engine, human-confirmed payout only)
- Mentor Certification Status Update (grant / maintain / revoke)
- Educator Growth Event Triggers (XP, rank, badges → Growth Engine)
- Fraud / Collusion Flag Report (→ Compliance Agent, Governance Board)
- School Ops Teacher Dashboard Feed
- Feature Vectors (→ Feature Store Agent)
- Risk Alert for school admin when mentor performance below threshold

### Upstream Agents (Feed This Agent)

```
DOJO_MATCH_SCORING_AGENT           → match outcome + override logs
MENTOR_CALIBRATION_AGENT           → calibration scores + drift signals
SCHOOL_PERFORMANCE_ANALYTICS_AGENT → student improvement deltas (teacher-attributed)
ATTENDANCE_TRACKING_AGENT          → class attendance rates
BEHAVIORAL_SAFETY_AGENT            → mentor behavioral incident flags
GROWTH_XP_AGENT                    → student XP attributed to educator actions
BILLING_ENGINE_AGENT               → mentor license status + plan tier
DISPUTE_GOVERNANCE_AGENT           → resolved dispute outcomes referencing mentor
COLLUSION_DETECTION_AGENT          → match farming + peer score abuse flags
SCORING_GOVERNANCE_AGENT           → scoring variance + confidence signals
```

### Downstream Agents (Depend on This Agent)

```
BILLING_ENGINE_AGENT               → receives incentive eligibility signals (human-approved payout)
GROWTH_ENGINE_AGENT                → receives educator XP, rank, badge trigger events
DOJO_BELT_ENGINE_AGENT             → receives mentor certification status updates
FEATURE_STORE_AGENT                → receives educator feature vectors
OBSERVABILITY_AGENT                → receives performance metrics + anomaly alerts
SCHOOL_OPS_ERP_AGENT               → receives teacher performance dashboard feeds
RISK_ALERT_AGENT                   → receives educator risk flag events
COMPLIANCE_AGENT                   → receives fraud and collusion flag reports
GOVERNANCE_BOARD_AGENT             → receives escalated incentive dispute cases
TALENT_MARKETPLACE_AGENT           → receives verified mentor skill profile (if marketplace enabled)
```

---

## 3️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA: {
  "required_fields": [
    "tenant_id",
    "school_id",
    "teacher_id",
    "teacher_role",
    "evaluation_period_start_utc",
    "evaluation_period_end_utc",
    "requesting_actor_id",
    "requesting_actor_role",
    "data_sources": [
      "dojo_match_results",
      "calibration_scores",
      "student_performance_deltas"
    ]
  ],
  "optional_fields": [
    "class_id",
    "cohort_id",
    "mentor_license_tier",
    "peer_rating_data",
    "behavioral_incident_log",
    "dispute_outcome_refs",
    "override_log_refs",
    "billing_plan_ref"
  ],
  "validation_rules": [
    "tenant_id must match authenticated session tenant",
    "school_id must belong to tenant_id",
    "teacher_id must belong to school_id",
    "teacher_role must be in [TEACHER, MENTOR, EVALUATOR, HEAD_MENTOR]",
    "evaluation_period_start must be before evaluation_period_end",
    "requesting_actor_role must be in [SCHOOL_ADMIN, PLATFORM_ADMIN, COMPLIANCE_AGENT, SYSTEM_AGENT]",
    "period range must not exceed 92 days (quarterly cap per evaluation cycle)",
    "all UUIDs must be valid v4 format",
    "all numeric scores must be in range [0.0, 100.0]",
    "confidence_score must be in range [0.0, 1.0]",
    "student_improvement_delta must reference minimum 5 students for statistical validity"
  ],
  "security_checks": [
    "JWT token validation — short-lived access tokens only",
    "RBAC role check against requesting_actor_role",
    "Tenant isolation — cross-tenant teacher_id rejected with SECURITY_INCIDENT_LOG",
    "Domain isolation check — no cross-domain data bleed",
    "Teacher cannot submit own incentive request — REJECT",
    "Peer rating data must carry source_agent = PEER_RATING_AGENT with audit_ref",
    "Encryption validation on transport (TLS 1.3 minimum)",
    "All PII access logged with actor_id + timestamp"
  ],
  "domain_checks": [
    "teacher_id must have active assignment in school_id for given period",
    "data_sources must have minimum coverage of 80% of evaluation period days",
    "Mentor role requires active mentor_license status from BILLING_ENGINE_AGENT",
    "student_improvement_deltas must carry source_agent = SCHOOL_PERFORMANCE_ANALYTICS_AGENT",
    "calibration_scores must carry version tag from MENTOR_CALIBRATION_AGENT"
  ]
}
```

**Rules:**
- No null tolerance without explicit declared null policy
- Reject malformed data → LOG + HALT + return structured error (no PII in error body)
- Log ALL validation failures with `input_hash`, `actor_id`, `timestamp_utc`
- A teacher CANNOT trigger their own incentive evaluation — hard reject
- Minimum 5 attributed students required for improvement delta — prevents gaming on tiny sample

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "result_object": {

    "teacher_incentive_report": {
      "teacher_id"              : "UUID",
      "teacher_role"            : "TEACHER | MENTOR | EVALUATOR | HEAD_MENTOR",
      "evaluation_period"       : { "start": "ISO8601 UTC", "end": "ISO8601 UTC" },

      "performance_score": {
        "composite_score"                    : 0.0,
        "student_improvement_score"          : 0.0,
        "dojo_mentor_quality_score"          : 0.0,
        "calibration_compliance_score"       : 0.0,
        "attendance_engagement_score"        : 0.0,
        "behavioral_safety_score"            : 0.0,
        "belt_promotion_accuracy_score"      : 0.0,
        "dispute_resolution_quality_score"   : 0.0,
        "peer_collaboration_score"           : 0.0,
        "score_confidence"                   : 0.0
      },

      "incentive_eligibility": {
        "eligible"                  : true | false,
        "incentive_tier"            : "NONE | BRONZE | SILVER | GOLD | PLATINUM",
        "incentive_amount_ref"      : "BILLING_ENGINE_LEDGER_REF_UUID",
        "human_approval_required"   : true,
        "auto_payout"               : false,
        "hold_flags"                : [],
        "eligibility_reason_codes"  : []
      },

      "certification_status": {
        "current_status"          : "ACTIVE | PROBATION | SUSPENDED | REVOKED",
        "status_change"           : "NONE | UPGRADED | DOWNGRADED | SUSPENDED | REVOKED",
        "reason_code"             : "string",
        "re_certification_due"    : "ISO8601 UTC | null",
        "calibration_tolerance"   : "WITHIN | WARNING | EXCEEDED"
      },

      "fraud_flags": {
        "collusion_detected"          : false,
        "reciprocal_scoring_pattern"  : false,
        "match_farming_detected"      : false,
        "favoritism_pattern"          : false,
        "score_inflation_detected"    : false,
        "override_abuse_flag"         : false,
        "flags_detail"                : []
      },

      "growth_events": {
        "xp_earned"       : 0,
        "rank_change"     : "UP | DOWN | STABLE",
        "badges_earned"   : [],
        "rank_percentile" : 0.0
      },

      "risk_flags": [],
      "recommendations": []
    },

    "school_teacher_summary": {
      "school_id"                 : "UUID",
      "total_teachers_evaluated"  : 0,
      "avg_composite_score"       : 0.0,
      "incentive_eligible_count"  : 0,
      "certification_risk_count"  : 0,
      "fraud_flag_count"          : 0,
      "tier_distribution"         : {}
    }
  },

  "confidence_score"    : "0.0–1.0",
  "model_version"       : "TIA-ML-v1.0.0",
  "audit_reference"     : "UUID v4",
  "next_trigger_event"  : [
    "INCENTIVE_ELIGIBILITY_SIGNAL_EVENT",
    "MENTOR_CERTIFICATION_STATUS_UPDATE_EVENT",
    "EDUCATOR_XP_UPDATE_EVENT",
    "EDUCATOR_RANK_UPDATE_EVENT",
    "FRAUD_FLAG_REPORT_EVENT",
    "FEATURE_VECTOR_EMIT_EVENT",
    "TEACHER_RISK_ALERT_EVENT"
  ]
}
```

**All outputs MUST include:**
- Composite confidence score
- Model version reference (immutable)
- Audit UUID (append-only, immutable)
- Structured next trigger events

---

## 5️⃣ ML / AI LOGIC LAYER

### ML Layer — Primary (75% of All Processing)

```yaml
MODEL_TYPE:
  - Regression        : Composite incentive score calculation from weighted multi-dimensional inputs
  - Classification    : Incentive tier assignment (NONE / BRONZE / SILVER / GOLD / PLATINUM)
                        Fraud flag classification (collusion / favoritism / match-farming / inflation)
                        Certification risk classification (ACTIVE / PROBATION / SUSPENDED / REVOKED)
  - Clustering        : Peer teacher group benchmarking and cohort normalization
  - Time-Series       : Mentor quality trend analysis (rolling 12-week window)
                        Student improvement trajectory attribution to specific teachers

FEATURES_USED:
  Student Outcome Features:
    - student_improvement_delta_avg         (rolling 8-week)
    - student_improvement_delta_trend       (acceleration/deceleration)
    - at_risk_student_conversion_rate       (how many at-risk students improved under this teacher)
    - belt_promotion_success_rate           (mentor-supervised promotions that held vs. reversed)
    - class_attendance_rate                 (teacher's class average)
    - student_engagement_rate              (platform activity attributable to class)

  Dojo Mentor Quality Features:
    - match_scoring_accuracy               (vs. gold standard calibration match scores)
    - scoring_variance_from_peer_mentors   (inter-rater reliability)
    - override_frequency                   (how often mentor overrides auto-score)
    - override_justification_quality       (AI-assisted quality check on override notes)
    - pressure_scenario_pass_rate          (students mentored by this teacher)
    - mentor_calibration_score             (from MENTOR_CALIBRATION_AGENT)
    - calibration_drift_indicator          (deviation from acceptable tolerance)

  Behavioral & Safety Features:
    - behavioral_incident_count            (incidents involving this teacher)
    - dispute_outcome_ratio                (disputes decided against teacher)
    - student_complaint_frequency
    - cooldown_enforcement_compliance

  Growth & Engagement Features:
    - educator_platform_active_days
    - curriculum_contributions_count
    - peer_collaboration_events
    - scenario_authoring_quality_score     (if teacher authored scenarios)

  Fraud Signal Features:
    - reciprocal_high_score_pair_count
    - abnormal_student_scoring_cluster
    - match_farming_pattern_score
    - override_timing_anomaly_score
    - cross-class favoritism_index

TRAINING_FREQUENCY   : Weekly (incremental retraining); Monthly (full retrain)
DRIFT_DETECTION      :
  - Monitor composite score distribution shift per school cohort weekly
  - Monitor fraud classifier accuracy monthly
  - Monitor calibration drift signal monthly
  - Alert OBSERVABILITY_AGENT if drift threshold exceeded (> 4%)
VERSION_CONTROL      :
  - Immutable model snapshot per training cycle
  - model_version stored with every output record
  - Rollback capability to prior 3 model versions
```

### AI/LLM Layer — Assist Only (25%)

```yaml
AI_USAGE_SCOPE:
  - Natural language recommendation generation for school admin
    (e.g., "Mentor X shows improving calibration scores — consider for Head Mentor role")
  - Plain-language fraud flag explanation for Governance Board review
  - Override justification quality assessment
    (AI reads mentor's free-text override notes and scores their reasoning quality)
  - Teacher recognition narrative for Growth Engine badge descriptions
  - Parent-facing teacher summary (plain, neutral, 150 words max)

AI_RESTRICTIONS:
  - AI NEVER determines incentive eligibility
  - AI NEVER revokes or grants mentor certification
  - AI NEVER approves or initiates payout
  - AI output always labeled: SOURCE = AI_ASSIST, REQUIRES_HUMAN_REVIEW = true
  - AI override justification score is ONE input to ML model — not sole signal

PROMPT_GOVERNANCE:
  - All prompts versioned: PROMPT_VERSION = TIA-P-001
  - Deterministic prompt structure — no creative interpretation
  - Override justification assessment prompt: TIA-P-002
  - Teacher recommendation narrative prompt: TIA-P-003
  - Output length capped per use case (max 200 words)
  - Neutral tone enforced — no praise or condemnation language

AI assists ML. AI never replaces ML.
```

---

## 6️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS         : 2,000 (peak end-of-period evaluation windows)
LATENCY_TARGET       : P95 < 4 seconds (individual teacher report)
                       P99 < 10 seconds (school-level summary)
MAX_CONCURRENCY      : 5,000 simultaneous evaluation sessions
QUEUE_STRATEGY       : Kafka event queue
                       Priority lanes: FRAUD_FLAG (CRITICAL) > REAL_TIME > BATCH > EXPORT

SCALING_MODEL:
  - Horizontal scaling      : Stateless worker pods (Kubernetes HPA)
  - Stateless execution     : No in-memory state between requests
  - Event-driven triggers   : All downstream emissions via Kafka
  - Async processing        : Batch school-level summaries async; real-time fraud flag alerts
  - Idempotent operations   : Duplicate event processing safe (dedup by audit_reference UUID)

CACHING_POLICY:
  - Teacher performance composite: TTL 30 minutes (invalidated on new scoring event)
  - Fraud flag status: TTL 5 minutes (near-real-time critical)
  - School summary: TTL 2 hours
  - Calibration score: TTL 24 hours (invalidated on new calibration event)
  - Certification status: TTL 1 hour (invalidated on status change event)
```

---

## 7️⃣ SECURITY ENFORCEMENT

```yaml
MANDATORY_CONTROLS:

  Authentication:
    - JWT short-lived access tokens (15 min expiry)
    - Refresh token rotation mandatory
    - MFA enforced for SCHOOL_ADMIN and PLATFORM_ADMIN
    - Device session registry active

  Authorization:
    - RBAC per route — per requesting_actor_role
    - A TEACHER cannot view another teacher's incentive score
    - A MENTOR cannot access school-level summaries
    - SCHOOL_ADMIN scoped to own school_id only
    - PLATFORM_ADMIN multi-school visibility (audit-logged)
    - No cross-tenant queries permitted — hard reject + SECURITY_INCIDENT_LOG

  Incentive Financial Gate:
    - All incentive eligibility signals sent to BILLING_ENGINE with:
        human_approval_required = true (always)
        auto_payout = false (always)
    - No autonomous financial disbursement permitted by this agent
    - Incentive records are append-only — no retroactive edits

  Fraud Circuit Breaker:
    - Fraud flag raised → incentive hold applied immediately
    - Hold release requires GOVERNANCE_BOARD approval
    - Fraud flags are immutable — cannot be deleted by any role

  Data:
    - Row-level security on all teacher tables
    - PII encryption at rest (AES-256)
    - PII encryption in transit (TLS 1.3 minimum)
    - Secrets manager only — no env plaintext in production

  Audit:
    - All incentive calculation runs logged: actor_id + action + timestamp_utc + teacher_id
    - All certification status changes immutably logged
    - All fraud flags immutably logged with evidence_ref
    - All score override assessments logged
    - Audit logs append-only — delete FORBIDDEN

  Rate Limiting:
    - Per-IP rate limit: 200 req/min
    - Per-admin rate limit: 100 req/min
    - Abuse detection thresholds active
    - WAF in front of API

CROSS-TENANT QUERY       : FORBIDDEN — HALT + SECURITY_INCIDENT_LOG
TEACHER SELF-REQUEST     : FORBIDDEN — REJECT + LOG
AUTO FINANCIAL PAYOUT    : FORBIDDEN — human approval always required
FRAUD FLAG DELETION      : FORBIDDEN by any role
```

---

## 8️⃣ AUDIT & TRACEABILITY

Every execution produces a mandatory immutable audit record:

```json
{
  "audit_record": {
    "timestamp_utc"               : "ISO8601",
    "actor_id"                    : "UUID",
    "actor_role"                  : "SCHOOL_ADMIN | PLATFORM_ADMIN | COMPLIANCE_AGENT | SYSTEM_AGENT",
    "tenant_id"                   : "UUID",
    "school_id"                   : "UUID",
    "teacher_id"                  : "UUID",
    "evaluation_period"           : { "start": "ISO8601", "end": "ISO8601" },
    "input_hash"                  : "SHA-256",
    "output_hash"                 : "SHA-256",
    "model_version"               : "TIA-ML-v1.x.x",
    "decision_path"               : "string — ML scoring path taken",
    "confidence_score"            : "0.0–1.0",
    "incentive_tier_assigned"     : "NONE | BRONZE | SILVER | GOLD | PLATINUM",
    "certification_status_change" : "NONE | UPGRADED | DOWNGRADED | SUSPENDED | REVOKED",
    "fraud_flags_raised"          : [],
    "anomaly_flags"               : [],
    "ai_assist_used"              : true | false,
    "events_emitted"              : [],
    "human_approval_required"     : true
  }
}
```

**Audit log storage: Append-only. Delete FORBIDDEN. Retention: minimum 7 years.**
**Incentive audit records retained for: minimum 10 years (financial compliance).**

---

## 9️⃣ FAILURE POLICY

```yaml
INVALID_INPUT:
  ACTION       : REJECT_REQUEST
  LOG          : LOG_VALIDATION_FAILURE — input_hash + actor_id + timestamp_utc
  RESPONSE     : 400 structured error code (no PII in error body)
  ESCALATE_TO  : NONE (client-side fix required)

MODEL_UNAVAILABLE:
  ACTION       : STOP_EXECUTION
  LOG          : LOG_INCIDENT — MODEL_UNAVAILABLE + model_version + timestamp_utc
  ESCALATE_TO  : OBSERVABILITY_AGENT → ON_CALL_ALERT
  RETRY_POLICY : 3 retries with exponential backoff (2s, 6s, 18s); then HALT + ESCALATE

AI_TIMEOUT (> 5 seconds):
  ACTION       : SKIP_AI_RECOMMENDATION
  LOG          : LOG_AI_TIMEOUT — request_id + timestamp_utc
  BEHAVIOR     : Return ML output only; mark ai_recommendation = null
  ESCALATE_TO  : OBSERVABILITY_AGENT (metric increment)

DATA_CORRUPTION:
  ACTION       : STOP_EXECUTION
  LOG          : LOG_INCIDENT — DATA_INTEGRITY_FAILURE + affected_record_ids
  ESCALATE_TO  : PLATFORM_ADMIN + COMPLIANCE_AGENT
  RETRY_POLICY : NO RETRY — requires manual data integrity review

CONFIDENCE_BELOW_THRESHOLD (< 0.65):
  ACTION       : HOLD_RESULT — do not emit incentive eligibility event
  LOG          : LOG_LOW_CONFIDENCE — teacher_id + model_version + confidence_score
  BEHAVIOR     : Return result with flag: REQUIRES_HUMAN_REVIEW = true
  ESCALATE_TO  : SCHOOL_ADMIN + GOVERNANCE_BOARD_AGENT for manual review

FRAUD_FLAG_DETECTED:
  ACTION       : EMIT_FRAUD_FLAG_EVENT immediately
  LOG          : LOG_FRAUD_ALERT — teacher_id + fraud_type + evidence_ref (immutable)
  BEHAVIOR     : Place incentive on HOLD; do not emit eligibility signal
  ESCALATE_TO  : COMPLIANCE_AGENT + GOVERNANCE_BOARD_AGENT
  HOLD_RELEASE : Requires explicit GOVERNANCE_BOARD approval

CALIBRATION_TOLERANCE_EXCEEDED:
  ACTION       : DOWNGRADE_CERTIFICATION_STATUS to PROBATION
  LOG          : LOG_CALIBRATION_BREACH — teacher_id + deviation_score
  ESCALATE_TO  : SCHOOL_ADMIN + MENTOR_CALIBRATION_AGENT
  AUTO_REVOKE  : If PROBATION persists > 2 consecutive evaluation cycles → SUSPEND

NO_SILENT_FAILURES : ENFORCED — every failure path is logged and classified
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - DOJO_MATCH_SCORING_AGENT
      EVENT: MATCH_SCORE_FINALIZED + OVERRIDE_LOG_COMMITTED
  - MENTOR_CALIBRATION_AGENT
      EVENT: CALIBRATION_SCORE_UPDATED + DRIFT_ALERT_RAISED
  - SCHOOL_PERFORMANCE_ANALYTICS_AGENT
      EVENT: STUDENT_IMPROVEMENT_DELTA_COMMITTED (teacher-attributed)
  - ATTENDANCE_TRACKING_AGENT
      EVENT: CLASS_ATTENDANCE_RECORD_COMMITTED
  - BEHAVIORAL_SAFETY_AGENT
      EVENT: MENTOR_INCIDENT_FLAG_RAISED
  - GROWTH_XP_AGENT
      EVENT: STUDENT_XP_EVENT_COMMITTED (educator-attributed)
  - BILLING_ENGINE_AGENT
      EVENT: MENTOR_LICENSE_STATUS_UPDATED
  - DISPUTE_GOVERNANCE_AGENT
      EVENT: DISPUTE_RESOLVED_WITH_MENTOR_REF
  - COLLUSION_DETECTION_AGENT
      EVENT: COLLUSION_FLAG_RAISED_FOR_MENTOR
  - SCORING_GOVERNANCE_AGENT
      EVENT: SCORING_VARIANCE_REPORT_COMMITTED

DOWNSTREAM_AGENTS:
  - BILLING_ENGINE_AGENT
      EVENT_EMITTED  : INCENTIVE_ELIGIBILITY_SIGNAL_EVENT
      PAYLOAD        : teacher_id, incentive_tier, ledger_ref, human_approval_required=true
  - GROWTH_ENGINE_AGENT
      EVENT_EMITTED  : EDUCATOR_XP_UPDATE_EVENT | EDUCATOR_RANK_UPDATE_EVENT | BADGE_ELIGIBILITY_EVENT
  - DOJO_BELT_ENGINE_AGENT
      EVENT_EMITTED  : MENTOR_CERTIFICATION_STATUS_UPDATE_EVENT
  - FEATURE_STORE_AGENT
      EVENT_EMITTED  : EDUCATOR_FEATURE_VECTOR_EMIT_EVENT
  - OBSERVABILITY_AGENT
      EVENT_EMITTED  : PERFORMANCE_METRIC_EVENT | ANOMALY_ALERT_EVENT
  - SCHOOL_OPS_ERP_AGENT
      EVENT_EMITTED  : TEACHER_PERFORMANCE_DASHBOARD_EVENT
  - RISK_ALERT_AGENT
      EVENT_EMITTED  : TEACHER_RISK_FLAG_EVENT
  - COMPLIANCE_AGENT
      EVENT_EMITTED  : FRAUD_FLAG_REPORT_EVENT
  - GOVERNANCE_BOARD_AGENT
      EVENT_EMITTED  : INCENTIVE_DISPUTE_ESCALATION_EVENT (conditional)
  - TALENT_MARKETPLACE_AGENT
      EVENT_EMITTED  : VERIFIED_MENTOR_PROFILE_EVENT (conditional — marketplace enabled)

EVENT_TRIGGERS:
  - All events emitted via Kafka event bus
  - All events carry: schema_version, tenant_id, event_id (UUID v4), timestamp_utc
  - Event schema governed by Event Schema Registry — no ad-hoc event shapes permitted
  - Duplicate event dedup by audit_reference UUID (idempotent)
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent touches educator behavior extensively. Must emit structured feature vectors to FEATURE_STORE_AGENT after every evaluation run.

```json
EMIT_FEATURE_VECTOR: {
  "user_id"           : "UUID (teacher_id)",
  "user_role"         : "TEACHER | MENTOR | EVALUATOR | HEAD_MENTOR",
  "tenant_id"         : "UUID",
  "school_id"         : "UUID",
  "feature_name"      : "string — e.g. mentor_calibration_score,
                                       student_improvement_delta_avg,
                                       override_frequency,
                                       belt_promotion_accuracy",
  "feature_value"     : "float",
  "feature_category"  : "MENTOR_QUALITY | STUDENT_OUTCOME | BEHAVIORAL |
                          FRAUD_SIGNAL | GROWTH | CALIBRATION",
  "timestamp"         : "ISO8601 UTC",
  "source_agent"      : "TEACHER_INCENTIVE_AGENT",
  "model_version"     : "TIA-ML-v1.x.x",
  "confidence"        : "0.0–1.0"
}
```

**Feature emission is mandatory on every completed teacher evaluation run.**

---

## 1️⃣2️⃣ GROWTH ENGINE HOOK

This agent is a primary driver of educator XP, rank, and badge economy on Ecoskiller Antigravity.

```yaml
EDUCATOR_XP_UPDATE_EVENT:
  trigger_condition : composite_score improved vs. prior period >= 5 points
                      OR student improvement delta above school benchmark
  payload           : teacher_id, xp_amount, reason_code, source_agent, model_version
  xp_amounts        :
    - BRONZE tier achieved    → 500 XP
    - SILVER tier achieved    → 1,200 XP
    - GOLD tier achieved      → 2,500 XP
    - PLATINUM tier achieved  → 5,000 XP
    - Consecutive period tier → 1.2x multiplier (streak bonus)
    - Student belt promotion  → 200 XP per verified promotion
    - Calibration excellence  → 300 XP (calibration_tolerance = WITHIN + improving)

EDUCATOR_RANK_UPDATE_EVENT:
  trigger_condition : rank_percentile change >= 5% within school or platform
  payload           : teacher_id, new_rank_percentile, delta, period, confidence

BADGE_ELIGIBILITY_EVENT:
  trigger_condition :
    - First GOLD tier achieved             → "Gold Mentor" badge
    - 3 consecutive SILVER or above        → "Consistency Champion" badge
    - Zero behavioral incidents + GOLD     → "Safe Mentor" badge
    - Top 5% calibration score             → "Master Calibrator" badge
    - 10+ successful belt promotions       → "Belt Builder" badge
    - Zero fraud flags for 12 months       → "Integrity Verified" badge
    - Head Mentor status achieved          → "Head Mentor" badge
  payload           : teacher_id, badge_id, evidence_ref, audit_reference

SHARE_TRIGGER_EVENT:
  trigger_condition : GOLD or PLATINUM tier achieved
  payload           : teacher_id, share_type, achievement_label, shareable_card_ref
```

---

## 1️⃣3️⃣ INCENTIVE SCORING FRAMEWORK

### Composite Score Weights (ML Model v1.0.0 — Immutable without version bump)

```yaml
COMPOSITE_SCORE_WEIGHTS:
  student_improvement_score          : 30%   # Primary outcome metric
  dojo_mentor_quality_score          : 25%   # Core Dojo responsibility
  calibration_compliance_score       : 15%   # Scoring governance
  attendance_engagement_score        : 10%   # Class operational health
  behavioral_safety_score            : 10%   # Safety compliance
  belt_promotion_accuracy_score      : 5%    # Promotion quality (non-reversed)
  dispute_resolution_quality_score   : 3%    # Governance participation
  peer_collaboration_score           : 2%    # Platform community contribution

TOTAL                                : 100%

INCENTIVE_TIER_THRESHOLDS (Composite Score → Tier):
  < 50              : NONE      (no incentive)
  50 – 64.99        : BRONZE    (acknowledgment tier)
  65 – 74.99        : SILVER    (standard incentive tier)
  75 – 84.99        : GOLD      (high performance tier)
  >= 85             : PLATINUM  (elite tier — platform-wide recognition)

SCORE_RULES:
  - Any active FRAUD flag → incentive HELD regardless of composite score
  - Calibration tolerance EXCEEDED → max tier capped at BRONZE until resolved
  - Behavioral incident (SEVERE) → disqualifies from GOLD and PLATINUM for current period
  - Dispute lost (> 3 in period) → -10 point composite penalty
  - Belt promotion reversal rate > 20% → -15 point composite penalty

CHANGE_PROHIBITED_WITHOUT_VERSION_BUMP:
  - Weight percentages
  - Tier thresholds
  - Score penalty rules
  - Fraud flag hold logic
  - Certification revocation thresholds
```

### Mentor Certification Status Logic

```yaml
CERTIFICATION_STATUS_RULES:
  ACTIVE:
    condition  : calibration_tolerance = WITHIN AND behavioral_score >= 70
                 AND no active fraud flags
    authority  : Can run ranked matches + certification matches

  PROBATION:
    condition  : calibration_tolerance = WARNING
                 OR behavioral_score 50–69
                 OR 1–2 disputes lost in period
    authority  : Can run practice matches only — NOT ranked or certification
    duration   : 1 evaluation cycle minimum; auto-review next cycle

  SUSPENDED:
    condition  : calibration_tolerance = EXCEEDED
                 OR behavioral_score < 50
                 OR 3+ disputes lost in period
                 OR PROBATION for 2 consecutive cycles
    authority  : No match authority — removed from active mentor pool
    resolution : Requires SCHOOL_ADMIN re-activation + mandatory re-certification

  REVOKED:
    condition  : Active FRAUD_FLAG (collusion / favoritism / match-farming confirmed)
                 OR SEVERE behavioral safety incident
    authority  : NONE — permanent ban from mentor role in this tenant
    resolution : Requires GOVERNANCE_BOARD decision + PLATFORM_ADMIN override

AUTO_REVOKE_FORBIDDEN : Only GOVERNANCE_BOARD can execute REVOKED status
                        for fraud/behavioral cases after due process
```

---

## 1️⃣4️⃣ FRAUD & COLLUSION DETECTION

```yaml
FRAUD_SIGNALS_MONITORED:
  Reciprocal Scoring:
    - Mentor A consistently gives high scores to Mentor B's students
    - Mentor B consistently does the same in return
    - Pattern threshold: >= 3 reciprocal high-score pairs in a period
    - FLAG: RECIPROCAL_SCORING_PATTERN

  Match Farming:
    - Abnormally high match volume from mentor in compressed timeframe
    - Match outcomes uniformly positive without normal variance
    - FLAG: MATCH_FARMING_DETECTED

  Score Inflation:
    - Mentor's average student scores significantly above school cohort benchmark (> 2 std dev)
    - Students' external assessment scores not corroborating Dojo scores
    - FLAG: SCORE_INFLATION_DETECTED

  Override Abuse:
    - Override frequency > 25% of all scored matches in period
    - Override direction: consistently upward (inflating scores)
    - Override timing: clustered near incentive evaluation cutoff dates
    - FLAG: OVERRIDE_ABUSE_FLAG

  Favoritism Pattern:
    - Specific student subset receiving disproportionately high scores from one mentor
    - Pattern persists across multiple evaluation periods
    - FLAG: FAVORITISM_PATTERN

FRAUD_RESPONSE_POLICY:
  FLAG_RAISED:
    - Incentive hold applied immediately (automatic)
    - COMPLIANCE_AGENT notified immediately
    - GOVERNANCE_BOARD_AGENT escalation event emitted
    - Mentor certification → PROBATION pending review

  FLAG_CONFIRMED (by GOVERNANCE_BOARD):
    - Incentive for period: VOID
    - Mentor certification → SUSPENDED or REVOKED (per severity)
    - Affected match scores: flagged for AUDIT_REVIEW
    - All implicated matches: held from belt progression counting

  FLAG_CLEARED (by GOVERNANCE_BOARD):
    - Incentive hold released
    - Certification restored to prior status
    - LOG: FRAUD_CLEARED with governance_ref + board_decision_id

FRAUD_FLAG_IMMUTABILITY:
  - Flags are NEVER deleted — only CLEARED with governance audit trail
  - Cleared flags remain in audit history permanently
```

---

## 1️⃣5️⃣ SCHOOL OPS ERP INTEGRATION

```yaml
TEACHER_PERFORMANCE_DASHBOARD_FEED:
  Per Teacher (SCHOOL_ADMIN visible):
    - Composite score current period
    - Incentive tier and eligibility status
    - Certification status + any active hold reasons
    - Student improvement attribution
    - Calibration compliance trend (12-week)
    - Active fraud flags (if any)
    - Growth metrics (XP earned, rank, badges)
    - Recommended action (AI-generated, labeled AI_ASSIST)

  School-Level Summary (SCHOOL_ADMIN + PLATFORM_ADMIN):
    - Average composite score across all teachers
    - Incentive tier distribution
    - Certification risk count (PROBATION + SUSPENDED)
    - Fraud flag count (active)
    - Top performers shortlist (verified, GOLD + PLATINUM)
    - Under-performers requiring intervention

  TPO / Leadership Report:
    - Teacher-to-student improvement correlation heat map
    - Subject/skill area where educator quality is weakest
    - Mentor certification health by class

REPORT_FREQUENCY:
  - Real-time  : Fraud flag alerts, certification status changes
  - Daily      : Individual teacher incentive score refresh
  - Weekly     : School-level summary
  - Monthly    : Full incentive eligibility run (billing-ready)
  - Quarterly  : Comprehensive teacher performance review (ERP export)
  - On-demand  : Export (RBAC-gated, SCHOOL_ADMIN + PLATFORM_ADMIN only)
```

---

## 1️⃣6️⃣ DOJO INTEGRATION — MENTOR LAYER

```yaml
DOJO_MENTOR_SIGNALS_CONSUMED:
  - match_score per session (mentor-assessed)
  - override_log per session (frequency, direction, justification text)
  - scoring_confidence from SCORING_GOVERNANCE_LOCK
  - inter-rater reliability score from RATER_CALIBRATION_LOCK
  - calibration_score from MENTOR_CALIBRATION_AGENT
  - belt_promotion_decision (approved/denied, with mentor_id reference)
  - pressure_scenario_outcome supervised by this mentor
  - behavioral_safety_flags raised during mentor sessions

DOJO_MENTOR_CERTIFICATION_RULES (enforced, per SECTION T7):
  - Mentor onboarding program completion: REQUIRED
  - Referee simulation training: REQUIRED
  - Scoring practice certification: REQUIRED
  - Override ethics training: REQUIRED
  - Dispute handling training: REQUIRED
  - Re-certification interval: every 6 evaluation cycles
  - Calibration match score within tolerance: REQUIRED for ACTIVE status

CERTIFICATION_AUTHORITY_MATRIX:
  ACTIVE mentor    : Can run ranked + certification matches
  PROBATION mentor : Practice matches only
  SUSPENDED mentor : No match authority
  REVOKED mentor   : Permanently removed from mentor pool in tenant

UNCERTIFIED MENTORS: Cannot run ranked or certification matches — HARD LOCK
(Per SECTION T7 — MENTOR TRAINING & CERTIFICATION LOCK)

COLLUSION_GATE:
  - All matches with active fraud flags held from belt counting
  - Belt promotions from flagged mentor suspended until GOVERNANCE_BOARD clears
  - (Per SECTION T9 — COLLUSION & MANIPULATION DETECTION LOCK)
```

---

## 1️⃣7️⃣ BILLING ENGINE INTEGRATION

```yaml
INCENTIVE_FINANCIAL_FLOW:
  Step 1: This agent calculates incentive_tier and emits INCENTIVE_ELIGIBILITY_SIGNAL_EVENT
  Step 2: BILLING_ENGINE_AGENT receives event with:
            human_approval_required = true (ALWAYS)
            auto_payout = false (ALWAYS)
            incentive_amount_ref = BILLING_ENGINE_LEDGER_REF_UUID
  Step 3: SCHOOL_ADMIN reviews and approves in billing dashboard
  Step 4: BILLING_ENGINE_AGENT executes payout on approval
  Step 5: Payout event emitted with billing_audit_ref back to this agent
  Step 6: Audit record updated with payout_confirmed = true + billing_audit_ref

FRAUD_HOLD_GATE:
  - Any active fraud flag on teacher_id → eligibility_signal NOT emitted
  - Hold persists until GOVERNANCE_BOARD clears the flag
  - Cleared payouts for flagged periods: require separate GOVERNANCE_BOARD approval

INCENTIVE_PLAN_TIERS (Reference — Actual amounts governed by BILLING_ENGINE):
  BRONZE   : Acknowledgment (platform badge + certificate)
  SILVER   : Standard incentive (school-defined, min platform threshold)
  GOLD     : High-performance incentive (school-defined + platform bonus)
  PLATINUM : Elite incentive (school-defined + platform-wide recognition + max bonus)

FINANCIAL_RECORDS:
  - All incentive eligibility signals: append-only, 10-year retention
  - All payout confirmations: append-only, 10-year retention
  - No retroactive record modification under any condition
```

---

## 1️⃣8️⃣ PERFORMANCE MONITORING

```yaml
SUCCESS_METRICS:
  - Report generation success rate      : >= 99.5%
  - Latency P95                         : < 4 seconds
  - Latency P99                         : < 10 seconds
  - Fraud flag precision                : >= 90%
  - Fraud flag recall                   : >= 85%
  - Feature vector emission success     : >= 99.9%
  - Incentive tier accuracy             : >= 95% (vs. human reviewer sample audits)
  - Certification status accuracy       : 100% (zero tolerance for false revocations)

ERROR_METRICS:
  - Validation failure rate
  - Model unavailability rate
  - AI timeout rate
  - Low confidence rate (< 0.65)
  - Downstream event emission failure rate
  - Fraud flag false positive rate (monitored — governance audit)

DRIFT_METRICS:
  - Composite score distribution shift per school cohort (weekly)
  - Fraud classifier accuracy degradation (monthly)
  - Calibration score freshness (source age > 14 days → STALENESS_WARNING)

ANOMALY_METRICS:
  - Unusual incentive tier spike (school-wide sudden increase → review)
  - Cross-tenant access attempts (should always be zero)
  - Override frequency spike (signals potential collusion wave)
  - Certification revocation cluster (multiple teachers same period → escalate)

INTEGRATION WITH OBSERVABILITY_AGENT:
  - Structured logs: every execution
  - Request tracing: full lifecycle
  - Fraud signal metrics: forwarded real-time
  - Alert triggers:
      MODEL_UNAVAILABLE
      FRAUD_FLAG_RAISED
      DRIFT_THRESHOLD_EXCEEDED
      CROSS_TENANT_ATTEMPT (CRITICAL)
      CERTIFICATION_REVOCATION_CLUSTER
```

---

## 1️⃣9️⃣ VERSIONING POLICY

```yaml
ALL CHANGES:
  - Add-only
  - Versioned (semver: MAJOR.MINOR.PATCH)
  - Backward compatible within MINOR version
  - Migration documented per MAJOR version bump
  - Rollback safe to prior 3 versions

MODEL_VERSIONING:
  - ML model         : TIA-ML-v{MAJOR}.{MINOR}.{PATCH}
  - Incentive prompt : TIA-P-{VERSION_NUMBER}
  - Output schema    : TIA-OUTPUT-v{MAJOR}.{MINOR}

CHANGE_PROHIBITED_WITHOUT_VERSION_BUMP:
  - Composite score weight percentages
  - Incentive tier threshold values
  - Fraud detection pattern thresholds
  - Certification status decision rules
  - Score penalty rules
  - Audit schema fields
  - Financial retention periods

BACKWARD_COMPATIBILITY:
  - MINOR version bump: no schema breaking change
  - MAJOR version bump: migration plan required; old events supported for 90-day window
  - Model changes: prior 3 model versions retained for retrospective audit
```

---

## 2️⃣0️⃣ ACCESSIBILITY & LOCALIZATION

```yaml
ACCESSIBILITY (SECTION T11 ENFORCED):
  - All dashboards and reports screen-reader compatible
  - Color-contrast modes for tier indicators and fraud flags
  - Keyboard-navigable teacher performance panels
  - Transcript access for any referenced Dojo match recordings
  - ARIA labels on all interactive report components

LOCALIZATION (SECTION T12 ENFORCED):
  - Multi-language report output (UI and recommendations)
  - AI recommendation prompts culturally neutral
  - No scoring rule culture-exclusive
  - Regional school calendar support (evaluation period alignment)
  - Translation governance: versioned, approved translations only
  - Regional incentive currency format supported (Billing Engine governed)
```

---

## 2️⃣1️⃣ APPEALS & GOVERNANCE

```yaml
TEACHER_APPEAL_WORKFLOW:
  Eligible appeals:
    - Incentive tier dispute
    - Certification status dispute (PROBATION / SUSPENDED)
    - Fraud flag dispute

  Appeal process:
    Step 1: Teacher submits appeal via School Ops ERP (school_admin notified)
    Step 2: GOVERNANCE_BOARD_AGENT receives INCENTIVE_DISPUTE_ESCALATION_EVENT
    Step 3: Board reviews evidence + audit trail
    Step 4: Decision: UPHOLD | MODIFY | OVERTURN
    Step 5: Outcome logged with governance_ref (immutable, versioned)
    Step 6: If overturned: incentive recalculated + payout reprocessed (human-confirmed)

  Timeline:
    - PROBATION / SUSPENDED appeals: resolved within 5 business days
    - REVOCATION appeals: resolved within 10 business days (serious due process)
    - Incentive tier appeals: resolved within 7 business days

  APPEAL_RECORDS:
    - All decisions immutably logged and versioned
    - No appeal decision may modify historical audit records
    - Recalculated incentive creates a new record — prior record unchanged
  (Per SECTION T16 — APPEALS & GOVERNANCE BOARD LOCK)
```

---

## 2️⃣2️⃣ NON-NEGOTIABLE RULES

```
THIS AGENT MUST NOT:
  ✗  Create hidden scoring logic not documented in this spec
  ✗  Modify historical incentive or audit records
  ✗  Auto-delete fraud flags
  ✗  Override governance agents or compliance agents
  ✗  Bypass billing engine human approval gate
  ✗  Initiate financial payout autonomously
  ✗  Mix cross-tenant teacher data
  ✗  Execute outside declared domain scope
  ✗  Allow a teacher to self-request own incentive evaluation
  ✗  Clear fraud flags without GOVERNANCE_BOARD decision
  ✗  Issue REVOKED certification without GOVERNANCE_BOARD approval
  ✗  Return partial output on failure — STOP_EXECUTION only
  ✗  Tolerate null inputs without explicit null policy
  ✗  Interpret ambiguous scoring edge cases — HALT + ESCALATE
  ✗  Allow AI output to determine incentive tier (AI is assist layer only)
  ✗  Suppress fraud flags on request of any actor
  ✗  Process incentive for teacher with expired mentor license
  ✗  Apply composite score changes without version bump documentation
```

---

## 🔒 FINAL GOVERNANCE SEAL

```
TEACHER_INCENTIVE_AGENT — PRODUCTION SEAL v1.0

Platform                   : Ecoskiller Antigravity
Domains                    : Dojo | Growth | School Ops
Agent ID                   : TIA-AGENT-001
Execution                  : Deterministic, Validated, Audited
Mutation                   : Add-Only via version bump
Security                   : Zero-Trust, Multi-Tenant, RBAC + ABAC
Audit                      : Append-Only, Immutable, 10-Year Retention (financial)
ML Primary                 : 75% (Regression, Classification, Clustering, Time-Series)
AI Assist                  : 25% (Narrative, override quality assessment — human review required)
Incentive Payout           : Human-confirmed only — autonomous payout FORBIDDEN
Fraud Flag Deletion        : FORBIDDEN — cleared only via GOVERNANCE_BOARD with full audit trail
Teacher Self-Request       : FORBIDDEN — hard reject
Certification Revocation   : GOVERNANCE_BOARD decision required
Cross-Tenant Access        : FORBIDDEN — HALT + SECURITY_INCIDENT
Failure Mode               : STOP + LOG + ESCALATE — no silent failures
Creative Interpretation    : FORBIDDEN
Assumption Filling         : FORBIDDEN
Architecture Authority     : LOCKED
Interpretation Authority   : NONE

Signed: ECOSKILLER INTELLIGENCE & INNOVATION CORE
Artifact Class: SEALED PRODUCTION AGENT BLUEPRINT
Version: 1.0.0
```

---

*This document is a sealed, locked production artifact for Ecoskiller Antigravity.
No section may be mutated without a formal version bump and governance board review.
All downstream systems consuming this agent's output are bound by the contracts declared herein.
Financial records produced by this agent carry a minimum 10-year immutable retention obligation.*
