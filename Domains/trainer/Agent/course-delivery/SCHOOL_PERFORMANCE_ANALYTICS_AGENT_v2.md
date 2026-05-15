# 🔒 SCHOOL_PERFORMANCE_ANALYTICS_AGENT
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED PRODUCTION AGENT SPEC v1.0

```
ARTIFACT CLASS        : Production Agent Blueprint
PLATFORM              : Ecoskiller Antigravity
MUTATION POLICY       : Add-only via version bump
EXECUTION MODE        : Deterministic + Validated
CREATIVE INTERPRETATION: FORBIDDEN
ASSUMPTION FILLING    : FORBIDDEN
DEFAULT BEHAVIOR      : DENY
FAILURE MODE          : STOP_EXECUTION
ARCHITECTURE AUTHORITY: LOCKED
INTERPRETATION AUTHORITY: NONE
```

---

## 🔐 SEAL BLOCK — NON-NEGOTIABLE

```
DOJO SAAS PRODUCTION MODE         : ENABLED
SCHOOL OPS ANALYTICS MODE         : ENABLED
GROWTH ENGINE HOOK                : ACTIVE
ANTIGRAVITY INTELLIGENCE LAYER    : ACTIVE
ASSESSMENT VALIDITY               : REQUIRED
RATER CALIBRATION                 : REQUIRED
SCENARIO CALIBRATION              : REQUIRED
FAIRNESS ENGINE                   : ACTIVE
MENTOR CERTIFICATION              : REQUIRED
BEHAVIOR SAFETY                   : ENFORCED
ACCESSIBILITY                     : ENFORCED
GOVERNANCE BOARD                  : ACTIVE
OUTCOME VALIDATION                : REQUIRED
BELT VERSIONING                   : ENFORCED
APPEALS SYSTEM                    : ACTIVE
INSTITUTIONAL TRUST MODE          : LOCKED
MULTI-TENANT ISOLATION            : HARD
AUDIT TRAIL                       : APPEND-ONLY IMMUTABLE
```

---

## 1️⃣ AGENT IDENTITY

```yaml
AGENT_NAME        : SCHOOL_PERFORMANCE_ANALYTICS_AGENT
AGENT_VERSION     : v1.0.0
AGENT_ID          : SPA-AGENT-001
SYSTEM_ROLE       : School-Level Academic + Skill + Growth Performance Intelligence Engine
PRIMARY_DOMAIN    : School Operations / Dojo Assessment / Growth Economy
EXECUTION_MODE    : Deterministic + Validated
DATA_SCOPE        : School-tenant scoped; Student, Class, Cohort, School, District levels
TENANT_SCOPE      : Strict Isolation — cross-tenant queries FORBIDDEN
FAILURE_POLICY    : HALT on ambiguity — No silent failures
PARENT_SYSTEM     : Ecoskiller Antigravity Core Intelligence Layer
COMPLIANCE_MODE   : Zero-trust, append-only audit, RBAC + ABAC enforced
```

**This agent must NEVER assume missing specifications. If input is ambiguous → HALT + LOG + ESCALATE.**

---

## 2️⃣ PURPOSE DECLARATION

### Problem This Agent Solves

Schools, institutes, and educational tenants operating on Ecoskiller Antigravity need a unified, real-time intelligence layer that:

- Measures individual student academic + Dojo skill performance across multiple intelligences (per Howard Gardner's EIE model)
- Tracks cohort-level growth, belt progression, and learning effectiveness
- Surfaces actionable insights for school admins, class teachers, mentors, and parents (read-only)
- Feeds Growth Engine (XP, ranks, badges) and Dojo Belt Engine with verified performance signals
- Provides School Ops ERP with enrollment health, attendance-performance correlation, and risk flags
- Connects institutional performance to the Antigravity Talent Marketplace and hiring pipeline

### What Input It Consumes

- Dojo match results, scenario scores, belt assessments
- Academic module grades, milestone completions, project evaluations
- Attendance records, engagement events, behavioral safety flags
- Human Intelligence Assessment (HIA) vectors from the EIE layer
- Mentor calibration scores and override logs
- Parent interaction signals (read-only layer)
- Growth events: XP events, streak data, rank changes

### What Output It Produces

- Student Performance Report (per student, per period)
- Class/Cohort Analytics Report (aggregate)
- School Operations Dashboard Feed
- Risk Flag Alerts (academic decline, engagement drop, behavioral signals)
- Growth Engine Trigger Events (XP, rank, badge eligibility)
- Dojo Belt Eligibility Feed (performance threshold check)
- Feature Vector Emission to FEATURE_STORE_AGENT
- ERP Data Feed for TPO / School Admin dashboards

### Upstream Agents (Feed This Agent)

```
DOJO_MATCH_SCORING_AGENT
DOJO_BELT_ENGINE_AGENT
HIA_INTELLIGENCE_ANALYZER_AGENT
ATTENDANCE_TRACKING_AGENT
MENTOR_CALIBRATION_AGENT
ENGAGEMENT_EVENT_AGENT
BEHAVIORAL_SAFETY_AGENT
GROWTH_XP_AGENT
```

### Downstream Agents (Depend on This Agent)

```
GROWTH_ENGINE_AGENT         → receives rank/XP trigger events
DOJO_BELT_ENGINE_AGENT      → receives belt eligibility signals
FEATURE_STORE_AGENT         → receives feature vectors
OBSERVABILITY_AGENT         → receives performance metrics
SCHOOL_OPS_ERP_AGENT        → receives school health reports
PARENT_TRUST_LAYER_AGENT    → receives read-only student summaries
RISK_ALERT_AGENT            → receives risk flags
TALENT_MARKETPLACE_AGENT    → receives verified skill vectors (if marketplace enabled)
```

---

## 3️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA: {
  "required_fields": [
    "tenant_id",
    "school_id",
    "student_id",
    "report_period_start_utc",
    "report_period_end_utc",
    "data_sources": [
      "dojo_match_results",
      "academic_scores",
      "attendance_records"
    ],
    "requesting_actor_id",
    "requesting_actor_role"
  ],
  "optional_fields": [
    "class_id",
    "cohort_id",
    "hia_vector",
    "growth_events",
    "mentor_override_log",
    "behavioral_flags",
    "parent_mode_flag"
  ],
  "validation_rules": [
    "tenant_id must match authenticated session tenant",
    "school_id must belong to tenant_id",
    "student_id must belong to school_id",
    "report_period_start must be before report_period_end",
    "requesting_actor_role must be in [ADMIN, TEACHER, MENTOR, EVALUATOR, PARENT_READONLY, SYSTEM_AGENT]",
    "date range must not exceed 365 days per request",
    "all UUIDs must be valid v4 format",
    "numeric scores must be in range [0, 100]",
    "confidence_score must be in range [0.0, 1.0]"
  ],
  "security_checks": [
    "JWT token validation — short-lived access only",
    "RBAC role check against requesting_actor_role",
    "Tenant isolation — cross-tenant student_id rejected",
    "PARENT_READONLY role limited to own child student_id only",
    "Domain isolation check — no cross-domain data bleed",
    "PII access logged with actor_id + timestamp",
    "Encryption validation on transport (TLS enforced)"
  ],
  "domain_checks": [
    "student_id must have active enrollment in school_id for given period",
    "data_sources must match available ingested events for the period",
    "HIA vector if provided must have source_agent = HIA_INTELLIGENCE_ANALYZER_AGENT"
  ]
}
```

**Rules:**
- No null tolerance without explicit policy
- Reject malformed data → LOG + HALT
- Log all validation failures with `input_hash`, `actor_id`, `timestamp_utc`
- PARENT role may ONLY read their own child's summary — no class-level data

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "student_performance_report": {
      "student_id": "UUID",
      "report_period": { "start": "ISO8601", "end": "ISO8601" },
      "academic_performance": {
        "subject_scores": [],
        "average_score": 0.0,
        "improvement_delta": 0.0,
        "rank_in_class": null,
        "risk_flag": "NONE | LOW | MEDIUM | HIGH"
      },
      "dojo_performance": {
        "matches_played": 0,
        "belt_current": "string",
        "belt_progression_status": "ELIGIBLE | IN_PROGRESS | BLOCKED",
        "scenario_pass_rate": 0.0,
        "pressure_scenario_pass": false,
        "mentor_certified": false,
        "scoring_confidence": 0.0
      },
      "intelligence_profile": {
        "hia_vector_version": "string",
        "linguistic_score": 0.0,
        "logical_score": 0.0,
        "spatial_score": 0.0,
        "interpersonal_score": 0.0,
        "intrapersonal_score": 0.0,
        "naturalist_score": 0.0,
        "bodily_score": 0.0,
        "musical_score": 0.0,
        "dominant_intelligence_type": "string"
      },
      "growth_metrics": {
        "xp_earned_period": 0,
        "rank_change": "UP | DOWN | STABLE",
        "streak_days": 0,
        "badges_earned": [],
        "share_events": 0
      },
      "engagement_metrics": {
        "attendance_rate": 0.0,
        "platform_active_days": 0,
        "drill_completion_rate": 0.0,
        "scenario_abandonment_rate": 0.0
      },
      "risk_flags": [],
      "recommendations": []
    },
    "cohort_summary": {
      "class_id": "UUID",
      "cohort_size": 0,
      "avg_academic_score": 0.0,
      "avg_dojo_score": 0.0,
      "belt_distribution": {},
      "intelligence_type_distribution": {},
      "at_risk_student_count": 0,
      "top_performer_count": 0
    }
  },
  "confidence_score": "0.0–1.0",
  "model_version": "SPA-ML-v1.0.0",
  "audit_reference": "UUID v4",
  "next_trigger_event": [
    "GROWTH_ENGINE_XP_UPDATE_EVENT",
    "BELT_ELIGIBILITY_CHECK_EVENT",
    "RISK_ALERT_DISPATCH_EVENT",
    "FEATURE_VECTOR_EMIT_EVENT"
  ]
}
```

**All outputs MUST include:**
- Confidence score
- Model version reference
- Audit UUID (immutable, append-only)
- Next trigger events (structured, event-driven)

---

## 5️⃣ ML / AI LOGIC LAYER

### ML Layer (Primary — 75% of Processing)

```yaml
MODEL_TYPE:
  - Classification      : Risk flag classification (NONE/LOW/MEDIUM/HIGH)
  - Regression          : Score trajectory prediction, improvement delta
  - Clustering          : Cohort peer grouping, learning style clustering
  - Time-Series         : Performance trend, attendance-performance correlation

FEATURES_USED:
  Academic:
    - subject_score_history (rolling 12 weeks)
    - attendance_rate
    - assignment_completion_rate
    - exam_score_delta
  Dojo:
    - match_win_rate
    - scenario_difficulty_pass_rate
    - mentor_score_variance
    - pressure_scenario_completion
    - belt_progression_velocity
  Intelligence:
    - hia_vector (8 dimensions)
    - dominant_intelligence_type
    - intelligence_balance_index
  Growth:
    - xp_growth_rate_weekly
    - streak_length
    - drill_engagement_depth
  Behavioral:
    - behavioral_safety_flags_count
    - platform_session_frequency
    - peer_interaction_score

TRAINING_FREQUENCY  : Weekly (incremental); Monthly (full retrain)
DRIFT_DETECTION     : Monitor score distribution shift per cohort weekly
                      Monitor accuracy degradation on risk flag model
                      Alert OBSERVABILITY_AGENT if drift threshold exceeded (>5%)
VERSION_CONTROL     : Immutable model snapshot per training cycle
                      model_version stored with every output record
                      Rollback capability to prior 3 versions
```

### AI/LLM Layer (Assist Only — 25% of Processing)

```yaml
AI_USAGE_SCOPE:
  - Natural language recommendation generation (student feedback narrative)
  - Curriculum gap description from low-performing skill clusters
  - Parent-friendly summary generation (plain language, read-only)
  - Risk alert narrative generation for school admin escalation

AI_RESTRICTIONS:
  - AI NEVER makes promotion/demotion decisions
  - AI NEVER overrides ML model scores
  - AI NEVER approves or blocks belt progression
  - AI output always labeled: SOURCE = AI_ASSIST, requires human review

PROMPT_GOVERNANCE:
  - All prompts versioned: PROMPT_VERSION = SPA-P-001
  - Deterministic structure — no creative interpretation
  - Output length capped: 200 words max per recommendation
  - Language: plain, neutral, constructive

AI assists ML. AI does not replace ML.
```

---

## 6️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS         : 5,000 (peak school report generation windows)
LATENCY_TARGET       : P95 < 3 seconds (student report); P99 < 8 seconds (cohort report)
MAX_CONCURRENCY      : 10,000 simultaneous report sessions
QUEUE_STRATEGY       : Kafka event queue; priority lanes (REAL_TIME > BATCH > EXPORT)

SCALING_MODEL:
  - Horizontal scaling: stateless worker pods (Kubernetes HPA)
  - Stateless execution: no in-memory state between requests
  - Event-driven triggers: all downstream emissions via Kafka
  - Async processing: batch cohort reports async; student reports near-real-time
  - Idempotent operations: duplicate event processing safe (event dedup by audit_reference UUID)

CACHING_POLICY:
  - Student performance summary: TTL 15 minutes (invalidated on new score event)
  - Cohort aggregate: TTL 1 hour
  - HIA vector: TTL 24 hours (static until new assessment)
```

---

## 7️⃣ SECURITY ENFORCEMENT

```yaml
MANDATORY_CONTROLS:
  Authentication:
    - JWT short-lived access tokens (15 min expiry)
    - Refresh token rotation mandatory
    - MFA enforced for ADMIN and EVALUATOR roles
    - Device session registry active

  Authorization:
    - RBAC per route — per requesting_actor_role
    - ABAC for PARENT_READONLY — scoped to own child only
    - No cross-tenant queries permitted — hard reject
    - No cross-domain data bleed — domain isolation enforced

  Data:
    - Row-level security on all student tables
    - PII encryption at rest (AES-256)
    - PII encryption in transit (TLS 1.3 minimum)
    - Secrets manager only — no env plaintext in production

  Audit:
    - All data access logged: actor_id + action + timestamp_utc + student_id
    - All override decisions immutably logged
    - All risk flag dispatches logged
    - Audit logs append-only — delete forbidden

  Rate Limiting:
    - Per-IP rate limit: 200 req/min
    - Per-user rate limit: 100 req/min
    - Abuse detection thresholds active
    - WAF in front of API

CROSS-TENANT QUERY     : FORBIDDEN — HALT + SECURITY_INCIDENT_LOG
PARENT DATA ACCESS     : Read-only, own child only, no bulk export
MENTOR OVERRIDE ACCESS : Logged, versioned, immutable
```

---

## 8️⃣ AUDIT & TRACEABILITY

Every execution must produce an immutable audit record:

```json
{
  "audit_record": {
    "timestamp_utc"     : "ISO8601",
    "actor_id"          : "UUID",
    "actor_role"        : "ADMIN | TEACHER | MENTOR | EVALUATOR | PARENT_READONLY | SYSTEM_AGENT",
    "tenant_id"         : "UUID",
    "school_id"         : "UUID",
    "student_id"        : "UUID",
    "input_hash"        : "SHA-256",
    "output_hash"       : "SHA-256",
    "model_version"     : "SPA-ML-v1.x.x",
    "decision_path"     : "string (ML model path taken)",
    "confidence_score"  : "0.0–1.0",
    "anomaly_flags"     : [],
    "risk_flags_emitted": [],
    "ai_assist_used"    : true | false,
    "events_emitted"    : []
  }
}
```

**Audit log storage: Append-only. Delete FORBIDDEN. Retention: minimum 7 years.**

---

## 9️⃣ FAILURE POLICY

```yaml
INVALID_INPUT:
  ACTION     : REJECT_REQUEST
  LOG        : LOG_VALIDATION_FAILURE with input_hash + actor_id
  RESPONSE   : 400 with structured error code (no PII in error body)
  ESCALATE_TO: NONE (client-side fix)

MODEL_UNAVAILABLE:
  ACTION     : STOP_EXECUTION
  LOG        : LOG_INCIDENT — MODEL_UNAVAILABLE with timestamp + model_version
  ESCALATE_TO: OBSERVABILITY_AGENT → ON_CALL_ALERT
  RETRY_POLICY: 3 retries with exponential backoff (1s, 3s, 9s); then HALT

AI_TIMEOUT (> 5 seconds):
  ACTION     : SKIP_AI_RECOMMENDATION
  LOG        : LOG_AI_TIMEOUT with request_id
  BEHAVIOR   : Return ML output only; mark ai_recommendation = null
  ESCALATE_TO: OBSERVABILITY_AGENT (metric increment)

DATA_CORRUPTION:
  ACTION     : STOP_EXECUTION
  LOG        : LOG_INCIDENT — DATA_INTEGRITY_FAILURE with affected record IDs
  ESCALATE_TO: PLATFORM_ADMIN + COMPLIANCE_AGENT
  RETRY_POLICY: NO RETRY — requires manual data integrity review

CONFIDENCE_BELOW_THRESHOLD (< 0.65):
  ACTION     : HOLD_RESULT — do not emit downstream events
  LOG        : LOG_LOW_CONFIDENCE with student_id + model_version
  BEHAVIOR   : Return result with flag: REQUIRES_MENTOR_REVIEW = true
  ESCALATE_TO: MENTOR_CALIBRATION_AGENT for re-review

NO_SILENT_FAILURES: ENFORCED — every failure path logged and classified.
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - DOJO_MATCH_SCORING_AGENT
      EVENT: MATCH_SCORE_FINALIZED
  - DOJO_BELT_ENGINE_AGENT
      EVENT: BELT_ASSESSMENT_COMPLETED
  - HIA_INTELLIGENCE_ANALYZER_AGENT
      EVENT: HIA_VECTOR_UPDATED
  - ATTENDANCE_TRACKING_AGENT
      EVENT: ATTENDANCE_RECORD_COMMITTED
  - MENTOR_CALIBRATION_AGENT
      EVENT: MENTOR_SCORE_VALIDATED
  - ENGAGEMENT_EVENT_AGENT
      EVENT: USER_ACTIVITY_COMMITTED
  - BEHAVIORAL_SAFETY_AGENT
      EVENT: SAFETY_FLAG_RAISED
  - GROWTH_XP_AGENT
      EVENT: XP_EVENT_COMMITTED

DOWNSTREAM_AGENTS:
  - GROWTH_ENGINE_AGENT
      EVENT_EMITTED: RANK_UPDATE_EVENT | XP_UPDATE_EVENT | SHARE_TRIGGER_EVENT
  - DOJO_BELT_ENGINE_AGENT
      EVENT_EMITTED: BELT_ELIGIBILITY_CHECK_EVENT
  - FEATURE_STORE_AGENT
      EVENT_EMITTED: FEATURE_VECTOR_EMIT_EVENT
  - OBSERVABILITY_AGENT
      EVENT_EMITTED: PERFORMANCE_METRIC_EVENT | ANOMALY_ALERT_EVENT
  - SCHOOL_OPS_ERP_AGENT
      EVENT_EMITTED: SCHOOL_HEALTH_REPORT_EVENT
  - PARENT_TRUST_LAYER_AGENT
      EVENT_EMITTED: STUDENT_SUMMARY_READONLY_EVENT
  - RISK_ALERT_AGENT
      EVENT_EMITTED: STUDENT_RISK_FLAG_EVENT
  - TALENT_MARKETPLACE_AGENT
      EVENT_EMITTED: VERIFIED_SKILL_VECTOR_EVENT (conditional — marketplace enabled)

EVENT_TRIGGERS:
  - All events emitted via Kafka event bus
  - All events structured with schema_version, tenant_id, event_id (UUID), timestamp_utc
  - Schema governed by Event Schema Registry — no ad-hoc event shapes
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent touches user behavior. Must emit structured feature vectors to FEATURE_STORE_AGENT.

```json
EMIT_FEATURE_VECTOR: {
  "user_id"         : "UUID",
  "tenant_id"       : "UUID",
  "school_id"       : "UUID",
  "feature_name"    : "string (e.g. dojo_scenario_pass_rate, academic_improvement_delta)",
  "feature_value"   : "float",
  "feature_category": "ACADEMIC | DOJO | INTELLIGENCE | GROWTH | ENGAGEMENT | BEHAVIORAL",
  "timestamp"       : "ISO8601 UTC",
  "source_agent"    : "SCHOOL_PERFORMANCE_ANALYTICS_AGENT",
  "model_version"   : "SPA-ML-v1.x.x",
  "confidence"      : "0.0–1.0"
}
```

**Feature emission is mandatory on every completed student report run.**

---

## 1️⃣2️⃣ GROWTH ENGINE HOOK

This agent affects student ranking, achievement, and XP economy.

Must trigger on every completed performance evaluation:

```yaml
RANK_UPDATE_EVENT:
  trigger_condition : academic_score or dojo_score percentile change >= 5%
  payload           : student_id, new_rank_percentile, delta, period, confidence

XP_UPDATE_EVENT:
  trigger_condition : positive improvement delta confirmed by ML model
  payload           : student_id, xp_amount, reason_code, source_agent, model_version

SHARE_TRIGGER_EVENT:
  trigger_condition : belt earned OR top 10% rank achieved OR major improvement spike
  payload           : student_id, share_type, achievement_label, shareable_card_ref

BADGE_ELIGIBILITY_EVENT:
  trigger_condition : specific milestone achieved (first belt, 30-day streak, top of class)
  payload           : student_id, badge_id, evidence_ref, audit_reference
```

---

## 1️⃣3️⃣ DOJO INTEGRATION — SCHOOL PERFORMANCE CONTEXT

```yaml
DOJO_PERFORMANCE_SIGNALS_CONSUMED:
  - match_score per student per period
  - scenario_pass_rate per difficulty tier
  - pressure_scenario_results
  - mentor_certified flag
  - scoring_confidence (from SCORING_GOVERNANCE_LOCK)
  - inter-rater reliability score (from RATER_CALIBRATION_LOCK)
  - belt_version_tag (from BELT_VERSION_GOVERNANCE_LOCK)

BELT_ELIGIBILITY_RULES (enforced, not assumed):
  - Match count threshold must be met
  - Score threshold must be met
  - Pressure scenario pass required
  - Mentor certification required
  - confidence_score >= 0.65 (else → REQUIRES_MENTOR_REVIEW)
  - Belt promotion via this agent REQUIRES HUMAN CONFIRMATION
  - Auto promotion: FORBIDDEN

COLLUSION_DETECTION:
  - reciprocal high scoring pairs flagged
  - abnormal peer score clustering flagged
  - match farming patterns flagged
  - All flagged matches → AUDIT_REVIEW before counting toward belts
  (Governed by SECTION T9 — COLLUSION & MANIPULATION DETECTION LOCK)

SCENARIO_DIFFICULTY_CALIBRATION:
  - Agent consumes difficulty_reclassification signals from SCENARIO_DIFFICULTY_CALIBRATION_LOCK
  - Adjusts pass_rate normalization accordingly
  - Difficulty labels must be data-derived (SECTION T4 enforced)
```

---

## 1️⃣4️⃣ SCHOOL OPS ERP INTEGRATION

```yaml
SCHOOL_OPS_REPORTS_EMITTED:
  Student Risk Report:
    - Students with HIGH risk flag in current period
    - Attendance < 75% correlated with score decline
    - Behavioral safety flags count
    - Recommended intervention category

  Cohort Health Report:
    - Average academic + dojo score per class
    - Belt distribution across school
    - Intelligence type distribution per cohort
    - At-risk student % per class
    - Mentor load vs student count ratio

  TPO Dashboard Feed (for college placement officers):
    - Cohort hiring-readiness score
    - Skill vector summary per graduate cohort
    - Dojo belt distribution (verified)
    - Top performer shortlist (with verified match data)

  School Admin Dashboard Feed:
    - Overall school performance percentile vs platform benchmark
    - Teacher-class correlation signals
    - Enrollment health indicators
    - Engagement drop-off alerts

FREQUENCY:
  - Real-time: Risk alerts
  - Daily: Student-level summary
  - Weekly: Cohort + class reports
  - Monthly: School-level ERP report
  - On-demand: Export (with RBAC gate)
```

---

## 1️⃣5️⃣ INTELLIGENCE PROFILE INTEGRATION (Howard Gardner EIE Layer)

This agent integrates with the Ecoskiller Intelligence Engine (EIE) based on Howard Gardner's Multiple Intelligences model.

```yaml
HIA_VECTOR_CONSUMPTION:
  Source Agent   : HIA_INTELLIGENCE_ANALYZER_AGENT
  Schema Version : HIA-VECTOR-v1.x
  Dimensions     : [linguistic, logical, spatial, interpersonal, intrapersonal,
                    naturalist, bodily, musical]
  Score Range    : 0.0 – 100.0 per dimension
  Dominant Type  : highest scoring dimension (auto-classified)

INTELLIGENCE_DRIVEN_ANALYTICS:
  - Map dominant intelligence type → recommended Dojo skill tracks
  - Flag intelligence-skill misalignment (e.g., high linguistic / low logical in tech track)
  - Emit intelligence_profile_vector to FEATURE_STORE_AGENT
  - Surface to school admin: class-level intelligence distribution
  - Feed TALENT_MARKETPLACE_AGENT: candidate intelligence vector (if marketplace active)

RULES:
  - HIA vector is READ_ONLY in this agent — no mutation
  - Intelligence profile NEVER used to limit access to tracks
  - Used ONLY to surface recommendations and guide mentors
  - HIA vector must carry source_agent and model_version stamps
```

---

## 1️⃣6️⃣ PARENT TRUST LAYER

```yaml
PARENT_ACCESS_RULES:
  - PARENT_READONLY role only
  - Scoped strictly to own child's student_id
  - Cannot access class-level or cohort-level data
  - Cannot access other students' data under any condition
  - All parent access logged with actor_id + timestamp + student_id

PARENT_VISIBLE_DATA:
  - Student academic performance summary (current period)
  - Dojo belt level and last match date
  - Engagement metrics (attendance rate, platform active days)
  - Growth metrics (XP earned, streak, badges)
  - Risk flag if applicable (plain language description only, no raw ML scores)
  - AI-generated parent-friendly summary (200 words max, neutral tone)

PARENT_HIDDEN_DATA:
  - Raw ML scores
  - Confidence scores
  - Detailed behavioral flags
  - Mentor override logs
  - Cohort comparison data
  - Other student identifiers
```

---

## 1️⃣7️⃣ PERFORMANCE MONITORING

```yaml
SUCCESS_METRICS:
  - Report generation success rate    : >= 99.5%
  - Latency P95                       : < 3 seconds
  - Latency P99                       : < 8 seconds
  - Risk flag precision                : >= 85%
  - Risk flag recall                   : >= 80%
  - Feature vector emission success    : >= 99.9%
  - Belt eligibility accuracy          : 100% (zero tolerance for false positive belt awards)

ERROR_METRICS:
  - Validation failure rate
  - Model unavailability rate
  - AI timeout rate
  - Low confidence rate (< 0.65)
  - Downstream event emission failure rate

DRIFT_METRICS:
  - Score distribution shift per cohort (weekly)
  - Risk flag model accuracy degradation
  - HIA vector source freshness

ANOMALY_METRICS:
  - Collusion detection flag frequency
  - Unusual score spike/drop frequency
  - Cross-tenant access attempt count (should be zero)

INTEGRATION WITH OBSERVABILITY_AGENT:
  - Structured logs: every execution
  - Request tracing: full lifecycle
  - Scoring anomaly metrics: forwarded
  - Alert triggers: model unavailable, drift exceeded, cross-tenant attempt
```

---

## 1️⃣8️⃣ VERSIONING POLICY

```yaml
ALL CHANGES:
  - Add-only
  - Versioned (semver: MAJOR.MINOR.PATCH)
  - Backward compatible within MINOR version
  - Migration documented per MAJOR version bump
  - Rollback safe to prior 3 versions

MODEL_VERSIONING:
  - ML model: SPA-ML-v{MAJOR}.{MINOR}.{PATCH}
  - Prompt: SPA-P-{VERSION_NUMBER}
  - HIA vector schema: HIA-VECTOR-v{MAJOR}.{MINOR}
  - Output schema: SPA-OUTPUT-v{MAJOR}.{MINOR}

BELT_VERSION_TAGGING:
  - Every belt award carries belt_model_version + rubric_version + certificate_version
  - Re-certification triggered on MAJOR rubric version change
  (Per SECTION T17 — BELT VERSION GOVERNANCE LOCK)

CHANGE_PROHIBITED_WITHOUT_VERSION_BUMP:
  - Scoring formula
  - Belt eligibility rules
  - Risk flag thresholds
  - HIA dimension definitions
  - Output schema structure
  - Audit record schema
```

---

## 1️⃣9️⃣ ACCESSIBILITY & LOCALIZATION

```yaml
ACCESSIBILITY (SECTION T11 ENFORCED):
  - All reports screen-reader compatible (semantic HTML, ARIA labels)
  - Color-contrast modes for risk flag indicators
  - Captions on any embedded replay evidence
  - Transcript access for recorded Dojo matches referenced in reports
  - Keyboard-navigable report dashboards

LOCALIZATION (SECTION T12 ENFORCED):
  - Multi-language report output supported
  - AI recommendation prompts culturally neutral
  - No scoring rule culture-exclusive
  - Regional school term calendar support (academic period mapping)
  - Translation governance: versioned, approved translations only
```

---

## 2️⃣0️⃣ NON-NEGOTIABLE RULES

```
THIS AGENT MUST NOT:
  ✗  Create hidden logic not documented in this spec
  ✗  Modify historical performance records
  ✗  Auto-delete audit logs
  ✗  Override governance agents
  ✗  Bypass compliance checks
  ✗  Mix tenant data across school boundaries
  ✗  Execute outside declared scope
  ✗  Auto-promote belt without human confirmation
  ✗  Issue hiring recommendation without verified Dojo match data
  ✗  Allow PARENT role to access class-level or cohort-level data
  ✗  Use AI output as final decision (AI is assist layer only)
  ✗  Suppress risk flags on request of any actor
  ✗  Return partial output on failure (STOP_EXECUTION only)
  ✗  Tolerate null inputs without explicit null policy declaration
  ✗  Interpret ambiguous scope — HALT + ESCALATE instead
```

---

## 🔒 FINAL GOVERNANCE SEAL

```
SCHOOL_PERFORMANCE_ANALYTICS_AGENT — PRODUCTION SEAL v1.0

Platform              : Ecoskiller Antigravity
Domains               : Dojo | Growth | School Ops
Execution             : Deterministic, Validated, Audited
Mutation              : Add-Only via version bump
Security              : Zero-Trust, Multi-Tenant, RBAC + ABAC
Audit                 : Append-Only, Immutable, 7-Year Retention
ML Primary            : 75% (Classification, Regression, Clustering, Time-Series)
AI Assist             : 25% (Recommendation narrative only, human review required)
Belt Promotion        : Human-confirmed only — auto-promotion FORBIDDEN
Parent Access         : Read-only, child-scoped only
Cross-Tenant Access   : FORBIDDEN — HALT + SECURITY_INCIDENT
Failure Mode          : STOP + LOG + ESCALATE — no silent failures
Creative Interpretation: FORBIDDEN
Assumption Filling    : FORBIDDEN
Architecture Authority: LOCKED
Interpretation Authority: NONE

Signed: ECOSKILLER INTELLIGENCE & INNOVATION CORE
Artifact Class: SEALED PRODUCTION AGENT BLUEPRINT
Version: 1.0.0
```

---

*This document is a sealed, locked production artifact. No section may be mutated without a formal version bump and governance review. All downstream systems consuming this agent's output are bound by the contracts declared herein.*
