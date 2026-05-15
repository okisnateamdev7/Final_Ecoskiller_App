# 🔒 GD_POST_SESSION_ANALYTICS_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED PRODUCTION ARTIFACT
---

```
ARTIFACT_CLASS          = PRODUCTION SYSTEM AGENT BLUEPRINT
PLATFORM                = Ecoskiller Antigravity
MODULE                  = Dojo Engine → Group Discussion Subsystem →
                          Post-Session Analytics & Intelligence Layer
MUTATION_POLICY         = ADD-ONLY via version bump
EXECUTION_MODE          = DETERMINISTIC + VALIDATED
CREATIVE_INTERPRETATION = FORBIDDEN
ASSUMPTION_FILLING      = FORBIDDEN
DEFAULT_BEHAVIOR        = DENY
FAILURE_MODE            = STOP_EXECUTION
SCHEMA_VERSION          = v1.0.0
SEAL_DATE               = 2026-02-25
SEAL_AUTHORITY          = ECOSKILLER INTELLIGENCE & INNOVATION CORE
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY — NON-NEGOTIABLE)

```
AGENT_NAME              = GD_POST_SESSION_ANALYTICS_AGENT

SYSTEM_ROLE             = Post-Session Analytical Brain for Group Discussion sessions.
                          Computes participant-level scores, session-level quality metrics,
                          skill intelligence deltas, learning gain measurements, topic
                          difficulty signals, cohort benchmarks, and curriculum health
                          indicators. Produces all downstream analytics payloads that feed
                          the Passport Engine, Belt Engine, Scoring Service, Leaderboard,
                          Teacher Dashboard, Institute ERP, Parent Report, and the
                          Passive Intelligence Layer.

PRIMARY_DOMAIN          = Dojo Engine → Group Discussion → Post-Session Analytics Layer

EXECUTION_MODE          = Deterministic + Validated

DATA_SCOPE              = GD Session Records | Attendance Records | Phase Presence Data |
                          Message/Contribution Logs | Role Completion Records |
                          Teacher Score Inputs | Historical Session Scores |
                          Topic Difficulty History | Participant Skill Profiles |
                          Cohort Benchmarks | Rubric Versions | Model Versions |
                          Audit Trails | Feature Vectors

TENANT_SCOPE            = Strict Isolation (Institute | Enterprise | Platform)

FAILURE_POLICY          = Halt on Ambiguity

STACK_CONTEXT           = Flutter (operational runtime) + React (SEO web, read-only)

TRANSPORT_CONTEXT       = Kafka Event Bus (async) | PostgreSQL (persistent store) |
                          Redis (intermediate computation cache) |
                          Batch ML Pipeline (nightly + on-demand)
```

This agent is the **single analytical authority** for all post-session intelligence in the GD
subsystem. It does not manage sessions. It does not assign roles. It does not control timers.
It does not make belt promotion decisions autonomously.

Its only mandate: **take raw session data and transform it into validated, versioned,
auditable analytical payloads that downstream systems can consume with full trust.**

**Interpretation Authority: NONE**
**Architecture Authority: LOCKED**

---

## 2️⃣ PURPOSE DECLARATION

### The Problem Without This Agent

A completed GD session generates raw data: attendance records, message counts, idea counts,
role completion flags, phase presence maps, and an optional teacher score. None of these raw
signals are meaningful on their own to any downstream system.

The Belt Engine cannot determine if this session counts toward promotion without knowing the
validated composite score, difficulty-normalized score, and inter-rater adjusted score.

The Passport Engine cannot calculate the correct intelligence delta without knowing which
specific intelligence dimensions were exercised at what intensity across which phases.

The Teacher Dashboard cannot show a meaningful summary without a ranked participant breakdown,
a session quality score, and a topic difficulty flag.

The Institute ERP cannot produce cohort analytics without normalized, comparable scores across
sessions with different topics, difficulty levels, and group compositions.

The Curriculum Engine cannot detect whether a topic needs to be retired, difficulty-relabeled,
or modified without per-session difficulty feedback aggregated over time.

**This agent is the transformation layer** — raw session data in, validated analytical
intelligence out. Every downstream system is blocked until this agent completes its pipeline.

### What It Consumes (Input Sources)

- Session completion event from GD_SESSION_SCHEDULER_AGENT (trigger)
- Attendance records from GD_ATTENDANCE_TRACKING_AGENT (verified presence data)
- Phase presence map per participant (from gd_attendance_phase_presence table)
- Message/contribution event log (from gd_messages table, per participant per phase)
- Role completion records (per participant — from SCORING_SERVICE_AGENT confirmation)
- Teacher score input (optional, 1–5 rating from TEACHER_SCORE_INPUT_AGENT)
- Historical session scores for this user (from gd_analytics_participant_history)
- Topic record with rubric version (from topics table)
- Topic difficulty history (from gd_topic_difficulty_history)
- Tenant plan config and domain track config (from TENANT_PLAN_AGENT)
- Rubric definition + construct mapping (from RUBRIC_REGISTRY_AGENT)
- Cohort benchmark data (peer group historical averages)

### What It Produces (Output Targets)

- Participant composite score per session (normalized, difficulty-adjusted, confidence-rated)
- Per-dimension intelligence delta per participant (Interpersonal, Linguistic, Leadership,
  Logical, Creative, Intrapersonal)
- Session quality score (session-level health metric)
- Per-phase engagement scores per participant
- Topic difficulty signal (pass rate feedback to TOPIC_DIFFICULTY_CALIBRATION_AGENT)
- Session score distribution (for variance anomaly detection)
- Learning gain delta per participant (vs prior 5-session baseline)
- Cohort benchmark comparison per participant
- Belt eligibility signal (composite score vs belt threshold — advisory only)
- Leaderboard ranking input (ranked participant list with scores)
- Teacher dashboard session summary
- Collusion anomaly signals (for ANTI_COLLUSION_DETECTION_AGENT)
- Feature vectors for FEATURE_STORE_AGENT (Passive Intelligence Layer)
- Immutable append-only audit records per computation event

### Upstream Agent Dependencies

```
UPSTREAM_AGENTS:
  - GD_SESSION_SCHEDULER_AGENT          → Provides session_completed trigger
  - GD_ATTENDANCE_TRACKING_AGENT        → Provides attendance_records, phase_presence_maps
  - SCORING_SERVICE_AGENT               → Provides role_completion_records
  - TEACHER_SCORE_INPUT_AGENT           → Provides optional teacher_score (1–5)
  - RUBRIC_REGISTRY_AGENT               → Provides rubric_definition, construct_mapping, rubric_version
  - TOPIC_DIFFICULTY_CALIBRATION_AGENT  → Provides current_difficulty_label, historical_pass_rate
  - TENANT_PLAN_AGENT                   → Provides analytics_feature_flags per plan
  - DOMAIN_ISOLATION_AGENT              → Validates cross-domain access
  - ANTI_COLLUSION_DETECTION_AGENT      → Provides pre-computed collusion flags (if any)
  - OBSERVABILITY_AGENT                 → Receives all telemetry
```

### Downstream Agent Dependencies

```
DOWNSTREAM_AGENTS:
  - PASSPORT_UPDATE_AGENT               → Consumes intelligence_delta_payload
  - BELT_ENGINE_AGENT                   → Consumes belt_eligibility_signal (advisory)
  - LEADERBOARD_AGENT                   → Consumes ranked_participant_list
  - TEACHER_DASHBOARD_AGENT             → Consumes session_analytics_summary
  - INSTITUTE_ERP_AGENT                 → Consumes cohort_analytics_payload
  - PARENT_REPORT_AGENT                 → Consumes student_session_analytics (bounded)
  - TOPIC_DIFFICULTY_CALIBRATION_AGENT  → Consumes difficulty_feedback_signal
  - CURRICULUM_HEALTH_AGENT             → Consumes session_quality_signal
  - FEATURE_STORE_AGENT                 → Consumes behavioral_feature_vectors
  - ANTI_COLLUSION_DETECTION_AGENT      → Consumes variance_anomaly_signals, score_distribution
  - GROWTH_ENGINE_AGENT                 → Consumes top_performer events for share triggers
  - AUDIT_LOG_AGENT                     → Consumes all analytics computation audit records
  - OBSERVABILITY_AGENT                 → Consumes pipeline health metrics
```

---

## 3️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA — SESSION ANALYTICS TRIGGER:
{
  "required_fields": [
    "trigger_event_type",
    "session_id",
    "tenant_id",
    "group_id",
    "topic_id",
    "domain_track",
    "session_mode",
    "session_completed_utc",
    "rubric_version",
    "attendance_records_ready_flag",
    "gd_type"
  ],
  "optional_fields": [
    "teacher_score_available_flag",
    "teacher_score_value",
    "teacher_score_submitted_at_utc",
    "collusion_flags_from_attendance",
    "mentor_id",
    "tournament_id",
    "certification_context_flag",
    "offline_synced_flag"
  ],
  "validation_rules": [
    "trigger_event_type must be: SESSION_ANALYTICS_TRIGGER",
    "session_id must exist in gd_sessions with status = COMPLETED",
    "tenant_id must match session tenant_id (hard isolation)",
    "attendance_records_ready_flag must be true (block until GD_ATTENDANCE_TRACKING_AGENT confirms)",
    "rubric_version must exist in rubric_registry for this domain_track",
    "gd_type must be: TEACHER_CREATED | STUDENT_CREATED | SYSTEM_GENERATED",
    "domain_track must be: ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION",
    "session_mode must be: TEXT | VOICE | VIDEO",
    "teacher_score_value if present must be integer 1–5",
    "teacher_score_available_flag = true requires teacher_score_value to be present",
    "certification_context_flag = true requires mentor_id to be present",
    "analytics pipeline must NOT be triggered if session_status != COMPLETED"
  ],
  "security_checks": [
    "JWT token must be valid and not expired (system-to-system service JWT)",
    "tenant_id must match JWT tenant claim",
    "session_id must belong to tenant_id",
    "rubric_version must be approved and published (not DRAFT)",
    "cross-tenant session analytics trigger = SECURITY FAILURE"
  ],
  "domain_checks": [
    "rubric_version domain_track must match session domain_track",
    "all participant user_ids must belong to same domain_track as session",
    "no cross-domain analytics payload generation"
  ]
}

BLOCKING DEPENDENCY RULE:
  This agent MUST NOT begin analytics computation until:
  1. GD_ATTENDANCE_TRACKING_AGENT emits: ATTENDANCE_RECORDS_READY for this session_id
  2. Teacher score window has elapsed OR teacher_score_available_flag = true
     (Teacher score window = 24 hours from session_completed_utc)
  3. ANTI_COLLUSION_DETECTION_AGENT has processed ghost flags (if any were raised)
  Timeout policy: If any dependency not met within 48 hours of session_completed_utc:
    → Proceed with available data, mark missing inputs in analytics_record
    → LOG_INCIDENT with missing_dependency detail
```

**Null Tolerance Policy:** ZERO null tolerance on required fields. Immediate rejection +
LOG_INCIDENT. Null attendance_records_ready_flag = pipeline never starts.

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA — PARTICIPANT ANALYTICS RECORD (one per participant per session):
{
  "result_object": {
    "analytics_record_id": "UUID",
    "session_id": "UUID",
    "user_id": "UUID",
    "tenant_id": "UUID",
    "group_id": "UUID",
    "topic_id": "UUID",
    "domain_track": "ENUM",

    "raw_composite_score": "float 0.0–100.0",
    "difficulty_normalized_score": "float 0.0–100.0",
    "inter_rater_adjusted_score": "float 0.0–100.0",
    "final_composite_score": "float 0.0–100.0",

    "score_components": {
      "participation_score":      { "raw": "float", "weight": "float", "weighted": "float" },
      "role_completion_score":    { "raw": "float", "weight": "float", "weighted": "float" },
      "idea_contribution_score":  { "raw": "float", "weight": "float", "weighted": "float" },
      "phase_engagement_score":   { "raw": "float", "weight": "float", "weighted": "float" },
      "teacher_score_component":  { "raw": "float | null", "weight": "float", "weighted": "float" }
    },

    "per_phase_scores": {
      "PREPARATION":     "float 0.0–100.0",
      "OPENING":         "float 0.0–100.0",
      "IDEA_GENERATION": "float 0.0–100.0",
      "DEBATE":          "float 0.0–100.0",
      "SUMMARY":         "float 0.0–100.0"
    },

    "intelligence_deltas": {
      "interpersonal":  { "delta": "float", "new_score": "float", "direction": "UP|DOWN|STABLE" },
      "linguistic":     { "delta": "float", "new_score": "float", "direction": "UP|DOWN|STABLE" },
      "leadership":     { "delta": "float", "new_score": "float", "direction": "UP|DOWN|STABLE" },
      "logical":        { "delta": "float", "new_score": "float", "direction": "UP|DOWN|STABLE" },
      "creative":       { "delta": "float", "new_score": "float", "direction": "UP|DOWN|STABLE" },
      "intrapersonal":  { "delta": "float", "new_score": "float", "direction": "UP|DOWN|STABLE" }
    },

    "learning_gain": {
      "baseline_score_5session_avg": "float | null",
      "current_session_score":       "float",
      "learning_gain_delta":         "float",
      "trend":                       "IMPROVING | STABLE | REGRESSING",
      "regression_flag":             "boolean"
    },

    "cohort_benchmark": {
      "cohort_avg_score":       "float",
      "percentile_rank":        "float 0.0–100.0",
      "vs_cohort_delta":        "float",
      "benchmark_group_size":   "integer"
    },

    "belt_eligibility_signal": {
      "score_meets_threshold": "boolean",
      "threshold_version":     "string",
      "advisory_only":         "true (ALWAYS true — belt engine makes final decision)"
    },

    "top_performer_flag":        "boolean",
    "variance_anomaly_flag":     "boolean",
    "collusion_suspicion_flag":  "boolean",

    "analytics_data_completeness": "COMPLETE | PARTIAL_MISSING_TEACHER | PARTIAL_MISSING_PHASES",
    "rubric_version":              "string",
    "model_version":               "string"
  },
  "confidence_score":   "0.0–1.0",
  "model_version":      "GD_ANALYTICS_ML_v1.0.0",
  "audit_reference":    "UUID",
  "next_trigger_event": [
    "PASSPORT_UPDATE_AGENT::INTELLIGENCE_DELTA_READY",
    "BELT_ENGINE_AGENT::BELT_ELIGIBILITY_SIGNAL",
    "LEADERBOARD_AGENT::RANKED_SESSION_RESULT",
    "TEACHER_DASHBOARD_AGENT::SESSION_ANALYTICS_SUMMARY",
    "INSTITUTE_ERP_AGENT::COHORT_ANALYTICS_UPDATE",
    "TOPIC_DIFFICULTY_CALIBRATION_AGENT::DIFFICULTY_FEEDBACK",
    "FEATURE_STORE_AGENT::EMIT_BEHAVIORAL_VECTOR",
    "ANTI_COLLUSION_DETECTION_AGENT::SCORE_DISTRIBUTION_READY",
    "GROWTH_ENGINE_AGENT::TOP_PERFORMER_EVENT (if top_performer_flag = true)"
  ]
}

OUTPUT_SCHEMA — SESSION ANALYTICS SUMMARY (one per session):
{
  "session_analytics_summary_id": "UUID",
  "session_id": "UUID",
  "tenant_id": "UUID",
  "group_id": "UUID",
  "topic_id": "UUID",
  "domain_track": "ENUM",
  "participant_count": "integer",

  "session_quality_score":         "float 0.0–100.0",
  "avg_composite_score":           "float",
  "score_distribution":            { "min": "float", "max": "float", "median": "float",
                                     "stddev": "float", "p25": "float", "p75": "float" },
  "top_performer_user_id":         "UUID | null",
  "top_performer_score":           "float | null",
  "ranked_participants":           [ { "rank": "int", "user_id": "UUID", "score": "float" } ],

  "phase_engagement_summary": {
    "OPENING":         { "avg_engagement": "float", "low_engagement_flag": "boolean" },
    "IDEA_GENERATION": { "avg_engagement": "float", "low_engagement_flag": "boolean" },
    "DEBATE":          { "avg_engagement": "float", "low_engagement_flag": "boolean" },
    "SUMMARY":         { "avg_engagement": "float", "low_engagement_flag": "boolean" }
  },

  "variance_anomaly_detected":     "boolean",
  "collusion_suspicion_count":     "integer",
  "teacher_score_applied":         "boolean",
  "difficulty_feedback_signal":    { "pass_rate": "float", "avg_score": "float",
                                     "difficulty_drift_flag": "boolean" },

  "session_integrity_verdict":     "CLEAN | ANOMALY_DETECTED | COLLUSION_SUSPECTED",
  "model_version":                 "string",
  "audit_reference":               "UUID",
  "generated_at_utc":              "ISO-8601"
}
```

**All outputs MUST include:** confidence_score, model_version, audit_reference, next_trigger_event.
**Outputs without rubric_version are INVALID and must not be forwarded downstream.**
**Belt eligibility signal advisory_only field must ALWAYS be true — belt engine decides.**

---

## 5️⃣ SCORING COMPUTATION SPECIFICATION (LOCKED)

### 5.1 Score Components and Default Weights

The scoring formula is a **weighted composite model** as defined in Dojo For Arts
Section F (Scoring Governance Lock). Weights are rubric-versioned. Changes require
MAJOR version bump and rubric governance board approval.

```
SCORE COMPONENT         DEFAULT WEIGHT    SOURCE DATA
──────────────────────────────────────────────────────────────────────────
participation_score          25%          Attendance records (presence %)
role_completion_score        25%          Role engine completion flag
idea_contribution_score      20%          gd_messages: ideas submitted count
                                          + idea quality ML score
phase_engagement_score       15%          Phase presence map + per-phase
                                          contribution rate
teacher_score_component      15%          Teacher 1–5 rating (if submitted)
                                          → normalized to 0–100 scale
                                          → 0 weight if teacher_score absent
                                          → remaining weight redistributed
                                          proportionally among other components
──────────────────────────────────────────────────────────────────────────
TOTAL                       100%
```

**Weight redistribution when teacher score absent:**
- participation_score →  29.4%
- role_completion_score → 29.4%
- idea_contribution_score → 23.5%
- phase_engagement_score → 17.6%
- teacher_score_component → 0%

### 5.2 Participation Score Calculation

```
participation_score =
  (presence_percentage × 0.60)
  + (phase_coverage_ratio × 0.40)

WHERE:
  presence_percentage   = from GD_ATTENDANCE_TRACKING_AGENT (0.0–1.0)
  phase_coverage_ratio  = phases_present_count / total_phases_count (0.0–1.0)

Score range: 0–100 (multiply by 100)

LATE JOIN PENALTY:
  If late_join_flag = true AND late_join_delay_seconds > 600:
    participation_score × 0.85 (15% penalty)
  If late_join_flag = true AND late_join_delay_seconds <= 600:
    No penalty

GHOST PRESENCE HANDLING:
  If ghost_presence_flag = true:
    participation_score = 0.0 (irrespective of presence_percentage)
    Flagged in analytics_record
```

### 5.3 Role Completion Score Calculation

```
role_completion_score =
  (role_actions_completed_flag × 70)
  + (role_actions_quality_score × 30)

WHERE:
  role_actions_completed_flag:
    MODERATOR:       completed opening + any phase intervention = 1.0 | else 0.0
    SPEAKER:         completed summary phase with content = 1.0 | else 0.0
    IDEA_GENERATOR:  submitted >= 1 idea in IDEA_GENERATION phase = 1.0 | else 0.0
    NOTE_KEEPER:     sent >= 2 messages in any phase = 1.0 | else 0.0
    QUESTIONER:      sent >= 1 message in DEBATE phase = 1.0 | else 0.0

  role_actions_quality_score:
    ML-classified quality of role-specific actions (0–100)
    Uses: message_count, idea_count, phase_timing, contribution_pattern
    Model: Random Forest classifier (see ML section)

ROLE HYBRID (groups < 5):
  Combined roles scored as average of individual role criteria
```

### 5.4 Idea Contribution Score Calculation

```
idea_contribution_score =
  (ideas_submitted_count_score × 0.50)
  + (idea_quality_ml_score × 0.50)

WHERE:
  ideas_submitted_count_score:
    0 ideas  = 0
    1 idea   = 50
    2 ideas  = 75
    3+ ideas = 100
    (Capped at 3 to prevent quantity gaming)

  idea_quality_ml_score:
    ML model scores each submitted idea (0–100) on:
      - relevance_to_topic (topic embedding similarity)
      - clarity_score (linguistic ML classifier)
      - originality_score (within-session uniqueness — TF-IDF + cosine similarity)
    Final = average across all submitted ideas

  TEXT GD: Full idea_contribution_score applied
  VOICE GD: ideas_submitted_count_score only (no text ideas)
             idea_quality_ml_score = 0, weight redistributed to role_completion
  VIDEO GD: Same as VOICE
```

### 5.5 Phase Engagement Score Calculation

```
phase_engagement_score =
  weighted average of per-phase engagement across all phases present

per_phase_engagement =
  (messages_in_phase / avg_messages_per_participant_in_phase) × 100
  — capped at 100

PHASE WEIGHTS (within phase_engagement_score):
  OPENING:          10%
  IDEA_GENERATION:  40%
  DEBATE:           30%
  SUMMARY:          20%

If participant absent from a phase:
  That phase contribution = 0
  Other phases' weights scaled proportionally

CONTRIBUTION RATE ANOMALY:
  If messages_in_phase = 0 for IDEA_GENERATION AND DEBATE:
    phase_engagement_score penalty × 0.80
    Emit to variance_anomaly detection
```

### 5.6 Teacher Score Normalization

```
Teacher input: integer 1–5

Normalization to 0–100 scale:
  1 → 20
  2 → 40
  3 → 60
  4 → 80
  5 → 100

Teacher score influence capping:
  Even with teacher_score = 5 (normalized 100):
    Maximum contribution to final_composite_score = 15%
    Teacher score alone CANNOT push final_composite_score above 75 without
    strong ML-scored components

Teacher score override rule (from Dojo Section F):
  All teacher score overrides logged to immutable audit
  Variance between teacher_score and ML-computed score:
    Difference > 30 points (on 0–100 scale): emit SCORER_VARIANCE_FLAG
    SCORER_VARIANCE_FLAG → feed to RATER_CALIBRATION_AGENT
```

### 5.7 Difficulty Normalization

```
raw_composite_score → difficulty_normalized_score

DIFFICULTY NORMALIZATION FORMULA:
  difficulty_normalized_score =
    raw_composite_score × difficulty_normalization_factor

WHERE:
  difficulty_normalization_factor =
    (topic_historical_avg_score_across_all_sessions / 70.0)

    Capped: 0.80 ≤ difficulty_normalization_factor ≤ 1.20

  PURPOSE:
    Harder topics (lower historical avg) → score normalized upward
    Easier topics (higher historical avg) → score normalized downward
    Creates comparable scores across sessions of different difficulty

  If topic has < 10 historical sessions:
    difficulty_normalization_factor = 1.0 (no normalization yet)
    Flagged as INSUFFICIENT_CALIBRATION_HISTORY in analytics_record

  Topic difficulty derived from TOPIC_DIFFICULTY_CALIBRATION_AGENT:
    data-derived difficulty label (T4 — Scenario Difficulty Calibration Lock)
    NOT author-declared
```

### 5.8 Inter-Rater Adjustment

```
difficulty_normalized_score → inter_rater_adjusted_score

PURPOSE:
  Account for teacher scoring bias / drift across different teachers
  (Implements T2 Scoring Validity & Reliability Lock +
   T3 Rater Calibration Lock from Dojo For Arts)

FORMULA:
  inter_rater_adjusted_score =
    difficulty_normalized_score + teacher_bias_correction_offset

WHERE:
  teacher_bias_correction_offset =
    from RATER_CALIBRATION_AGENT for this teacher_id
    (teacher's historical scoring bias relative to ML baseline)
    Range: -10.0 to +10.0 points

  If teacher_score not submitted:
    inter_rater_adjusted_score = difficulty_normalized_score
    (no adjustment needed, no rater)

  If teacher_id has no calibration data:
    inter_rater_adjusted_score = difficulty_normalized_score
    Emit: RATER_CALIBRATION_PENDING flag

final_composite_score = inter_rater_adjusted_score
  Capped: 0.0 ≤ final_composite_score ≤ 100.0
```

---

## 6️⃣ INTELLIGENCE DELTA COMPUTATION (LOCKED)

### 6.1 Intelligence Dimension Mapping to GD Roles and Phases

This mapping is rubric-governed. Changes require rubric version bump + construct
validity review (T1 — Skill Validity Framework Lock).

```
INTELLIGENCE DIMENSION    PRIMARY SIGNAL SOURCE              PHASES WEIGHTED
─────────────────────────────────────────────────────────────────────────────
Interpersonal             Role completion (all roles)        All phases (equal)
                          Phase engagement score
                          Peer interaction count

Linguistic                Messages sent total                All phases
                          Idea clarity score                 IDEA_GENERATION (2×)
                          Summary quality (SPEAKER role)     SUMMARY (2×)
                          Vocabulary diversity index (NLP)

Leadership                MODERATOR role completion          OPENING (3×)
                          Phase transition management        All phases
                          Conflict navigation signals

Logical                   QUESTIONER role completion         DEBATE (3×)
                          Idea coherence score               IDEA_GENERATION (2×)
                          Challenge quality (NLP)

Creative                  Idea originality score             IDEA_GENERATION (3×)
                          Ideas submitted count
                          Novel solution signals (NLP)

Intrapersonal             Preparation time usage ratio       PREPARATION (3×)
                          Self-contribution consistency
                          Note quality (NOTE_KEEPER role)    All phases
─────────────────────────────────────────────────────────────────────────────
```

### 6.2 Delta Calculation Formula

```
intelligence_delta[dimension] =
  (dimension_contribution_score × dimension_weight_factor × attendance_multiplier)
  - regression_penalty

WHERE:
  dimension_contribution_score:
    Computed per dimension using signals above (0–100)

  dimension_weight_factor:
    Based on role assigned (role amplifies certain dimensions):
      MODERATOR:       leadership × 1.5, interpersonal × 1.2
      SPEAKER:         linguistic × 1.5, leadership × 1.1
      IDEA_GENERATOR:  creative × 1.5, logical × 1.2
      NOTE_KEEPER:     intrapersonal × 1.5, linguistic × 1.1
      QUESTIONER:      logical × 1.5, interpersonal × 1.1

  attendance_multiplier:
      PRESENT:          1.00
      LATE:             0.85
      PARTIAL:          0.60
      ABSENT/GHOST:     0.00 (no delta awarded)

  regression_penalty:
    If final_composite_score < (user's 5-session baseline × 0.80):
      regression_penalty = 2.0 points per dimension
      Regression flag set in learning_gain record

Delta range per session: -5.0 to +5.0 points per dimension
Passport accumulates these deltas over time (handled by PASSPORT_UPDATE_AGENT)
```

### 6.3 Passport Update Payload

```json
PASSPORT_UPDATE_PAYLOAD:
{
  "user_id": "UUID",
  "session_id": "UUID",
  "analytics_record_id": "UUID",
  "rubric_version": "string",
  "intelligence_deltas": {
    "interpersonal":  { "delta": "float", "direction": "UP|DOWN|STABLE" },
    "linguistic":     { "delta": "float", "direction": "UP|DOWN|STABLE" },
    "leadership":     { "delta": "float", "direction": "UP|DOWN|STABLE" },
    "logical":        { "delta": "float", "direction": "UP|DOWN|STABLE" },
    "creative":       { "delta": "float", "direction": "UP|DOWN|STABLE" },
    "intrapersonal":  { "delta": "float", "direction": "UP|DOWN|STABLE" }
  },
  "source": "GD_SESSION",
  "domain_track": "ENUM",
  "timestamp_utc": "ISO-8601",
  "confidence_score": "float",
  "model_version": "string"
}
```

---

## 7️⃣ ML / AI LOGIC LAYER

This agent is **ML-primary (70–80%)** with **AI-assist (20–30%, strictly bounded)**.

### ML Logic (Primary)

```
MODEL 1 — ROLE ACTION QUALITY CLASSIFIER
  Model Type:   Random Forest / XGBoost
  Purpose:      Score quality of role-specific actions per participant
  Features:
    - assigned_role (encoded)
    - messages_in_prep_phase
    - messages_in_opening_phase
    - messages_in_idea_generation_phase
    - messages_in_debate_phase
    - messages_in_summary_phase
    - ideas_submitted_count
    - avg_message_length
    - message_timing_pattern (early/late within phase)
    - contribution_burst_score (concentrated vs distributed)
    - role_phase_alignment_score (did they contribute in their primary phase)
    - attendance_status (encoded)
    - presence_percentage
  Output:       role_actions_quality_score (0–100), confidence (0.0–1.0)
  Training:     Weekly retrain on prior 30-day sessions with teacher labels

MODEL 2 — IDEA QUALITY SCORER
  Model Type:   TF-IDF + Cosine Similarity + Gradient Boosting (TEXT GD only)
  Purpose:      Score each submitted idea on relevance, clarity, originality
  Features:
    - idea_text_vector (TF-IDF embedding, max 512 tokens)
    - topic_description_vector (cosine similarity to topic)
    - session_idea_vectors (uniqueness within session — originality)
    - vocabulary_diversity_index (type-token ratio)
    - sentence_count
    - avg_sentence_length
    - keyword_overlap_with_topic
  Output:       { relevance: float, clarity: float, originality: float,
                  idea_quality_ml_score: float }  (all 0–100)
  Training:     Monthly retrain on labeled idea dataset

MODEL 3 — VOCABULARY DIVERSITY & LINGUISTIC CLASSIFIER
  Model Type:   Statistical NLP + Logistic Regression
  Purpose:      Score linguistic intelligence signal from message text
  Features:
    - type_token_ratio (vocabulary diversity)
    - avg_word_length
    - rare_word_count (words not in top 1000 frequency list)
    - sentence_structure_variety
    - explanation_length (IDEA_GENERATOR and SPEAKER phases)
    - domain_vocabulary_usage (domain-specific terms for domain_track)
  Output:       linguistic_signal_score (0–100), confidence
  Training:     Monthly retrain
  NOTE:         TEXT GD only. VOICE GD uses message_count + phase presence instead.

MODEL 4 — SESSION QUALITY SCORER
  Model Type:   Linear Regression
  Purpose:      Compute session-level health and quality
  Features:
    - avg_composite_score_all_participants
    - score_stddev (distribution spread)
    - attendance_rate
    - avg_phase_engagement_all_phases
    - ghost_participant_rate
    - role_completion_rate (% of participants who completed role)
    - teacher_score_applied (boolean)
    - teacher_vs_ml_variance_score
    - ideas_submitted_per_participant
    - avg_message_count_per_participant
    - session_mode (encoded)
    - topic_difficulty_label (encoded)
  Output:       session_quality_score (0–100)
  Training:     Monthly retrain

MODEL 5 — LEARNING GAIN & REGRESSION DETECTOR
  Model Type:   Time-Series / Moving Average + Threshold Classification
  Purpose:      Measure individual improvement or regression across sessions
  Features:
    - current_session_final_composite_score
    - prior_5_session_scores (time-ordered)
    - domain_track (encoded)
    - gd_type (encoded)
    - role_assigned_in_prior_sessions (diversity of roles)
    - attendance_rate_prior_5_sessions
  Output:       learning_gain_delta, trend (IMPROVING|STABLE|REGRESSING),
                regression_flag (boolean)
  Training:     Weekly retrain

MODEL 6 — VARIANCE ANOMALY DETECTOR
  Model Type:   Isolation Forest + Statistical Z-Score
  Purpose:      Detect abnormal score distributions within a session
              (T9 — Collusion & Manipulation Detection Lock)
  Features:
    - score_distribution (all participants in session)
    - teacher_score_vs_ml_delta per participant
    - peer_contribution_pattern (reciprocal high scores)
    - ghost_flag_count in session
    - session_mode (encoded)
    - historical_score_distribution for same topic
  Output:       variance_anomaly_flag (boolean), anomaly_severity (LOW|MEDIUM|HIGH),
                collusion_suspicion_flag (boolean)
  Training:     Monthly retrain + real-time anomaly threshold

TRAINING GOVERNANCE:
  - All models: Weekly or Monthly (see per-model above)
  - Training data: tenant-isolated; cross-tenant training FORBIDDEN
  - Model version: immutable tag embedded in every output record
  - Rollback-safe: prior versions retained 60 days
  - Drift detection: monitor accuracy vs teacher_label ground truth (rolling 30-day)
  - Alert threshold: >5% accuracy degradation → PagerDuty P2 via OBSERVABILITY_AGENT

DRIFT DETECTION METRICS (monitored continuously):
  - role_quality_classifier accuracy vs teacher override labels
  - idea_quality_scorer distribution shift
  - session_quality_score distribution shift
  - learning_gain regression_flag false positive rate
  - variance_anomaly_flag precision/recall vs confirmed collusion cases
```

### AI Logic (Bounded Scope — 20–30%)

```
AI_USAGE_SCOPE:

  Scope 1 — Idea Semantic Analysis (TEXT GD only)
    When: idea_quality_ml_score needs deep semantic evaluation
    What: LLM embedding-based semantic similarity check
          (Topic relevance beyond keyword matching;
           detects rephrased duplicates within session)
    Output: semantic_relevance_score (0–100), semantic_duplicate_flag
    Constraint: AI output is ONE input into idea_quality_ml_score formula.
                NOT the sole determinant.
    Prompt version: GD_ANALYTICS_IDEA_PROMPT_v1.0.0

  Scope 2 — Session Narrative Summary (for Teacher Dashboard)
    What: AI generates a 3–5 sentence natural language summary of session:
          "Session focused on [topic]. Strongest participation in IDEA_GENERATION.
          [User_alias] led the discussion with [score]. Phase engagement dropped
          in DEBATE phase — 2 of 5 participants were absent."
    Constraint: Summary is DISPLAY-ONLY. Not stored in analytics record.
                Not a decision input. Purely informational.
    Prompt version: GD_ANALYTICS_SUMMARY_PROMPT_v1.0.0

  Scope 3 — Curriculum Health Narrative (for CURRICULUM_HEALTH_AGENT)
    What: If session_quality_score < 50 for same topic across 3+ consecutive sessions,
          AI generates a structured flagging narrative explaining likely causes.
    Output: curriculum_flag_narrative (string, max 500 chars)
    Constraint: ADVISORY. Curriculum board makes final decision.
    Prompt version: GD_ANALYTICS_CURRICULUM_PROMPT_v1.0.0

  Scope 4 — Anomaly Explanation (for ANTI_COLLUSION_DETECTION_AGENT)
    What: When variance_anomaly_flag = true, AI generates a structured
          description of the anomaly pattern for governance board review.
    Constraint: ADVISORY TEXT ONLY. No enforcement decisions.
    Prompt version: GD_ANALYTICS_ANOMALY_PROMPT_v1.0.0

PROMPT GOVERNANCE (ALL SCOPES):
  - All prompts versioned with immutable version tags
  - All prompts deterministic: fixed structure, bounded output length
  - No open-ended generation
  - No decision authority
  - AI timeout: 5000ms → fallback to ML result only, log incident
  - AI output never replaces ML model output in score computation
  - AI outputs stored separately from ML outputs in analytics record
  - AI advisory text clearly marked as AI_GENERATED in output payload

AI MUST NOT:
  - Compute or modify any score component
  - Determine intelligence deltas
  - Set belt_eligibility_signal
  - Determine variance_anomaly_flag (ML-only)
  - Generate final_composite_score
  - Override teacher scores
  - Access data outside current session scope
```

---

## 8️⃣ SCALABILITY DESIGN

```
EXPECTED PROCESSING VOLUME:
  1M sessions/month   → ~1M analytics pipeline runs/month
                      → ~33,000/day → ~1,400/hour → ~23/minute
  10M sessions/month  → ~230/minute
  100M sessions/month → ~2,300/minute

  NOTE: Analytics pipeline is post-session (not real-time).
  Acceptable processing SLA: within 5 minutes of SESSION_ANALYTICS_TRIGGER.

LATENCY TARGETS:
  - Trigger acknowledgment (Kafka consume):      p95 < 200ms
  - Full analytics pipeline completion:          p95 < 5 minutes per session
  - Passport delta payload delivery:             p95 < 6 minutes post-session
  - Teacher dashboard summary delivery:          p95 < 6 minutes post-session
  - ERP cohort aggregate update:                 p95 < 30 minutes (batch acceptable)
  - Belt eligibility signal delivery:            p95 < 6 minutes post-session

MAX_CONCURRENCY:
  - 500 concurrent analytics pipeline runs per cluster
  - ML inference: GPU-backed batch inference for idea scoring (TEXT GD)
  - CPU-based inference for all other models

QUEUE_STRATEGY:
  - Kafka topic: gd.analytics.session_trigger      (partitioned by tenant_id)
  - Kafka topic: gd.analytics.records_ready        (output, partitioned by tenant_id)
  - Kafka topic: gd.analytics.intelligence_deltas  (partitioned by user_id)
  - Kafka topic: gd.analytics.session_summary      (partitioned by tenant_id)
  - Kafka topic: gd.analytics.difficulty_feedback  (partitioned by topic_id)
  - Dead letter queue: gd.analytics.pipeline.dlq   (retry-3 then escalate)

STATELESS EXECUTION: ENFORCED
  - No local pipeline state in agent memory
  - Intermediate computation stored in Redis (TTL = 2 hours)
  - All final records persisted to PostgreSQL

BATCH ML PROCESSING:
  - Nightly batch: learning_gain model update (per user rolling 5-session baseline)
  - On-demand: triggered per session completion for immediate pipeline
  - Avoid real-time ML for large models (idea quality scorer GPU batch)

IDEMPOTENCY:
  - Pipeline trigger: idempotency_key = session_id + session_completed_utc hash
  - Duplicate triggers within 60-minute window: deduplicated, return prior result
  - Idempotency cache: Redis with 2-hour TTL

TEXT GD vs VOICE/VIDEO GD PIPELINE DIFFERENCES:
  TEXT GD:
    Full pipeline including NLP, idea quality, linguistic analysis
    Estimated processing: 30s–120s per session

  VOICE/VIDEO GD:
    No NLP text analysis (no text content)
    No idea quality scorer
    Reduced pipeline: 10s–30s per session
    intelligence_deltas for linguistic/creative reduced (no text signal)
    idea_contribution_score = 0 (zero weight, redistributed)
```

---

## 9️⃣ SECURITY ENFORCEMENT

```
TENANT ISOLATION:
  - All DB queries scoped by tenant_id at PostgreSQL RLS
  - Analytics records never cross tenant boundaries
  - Cross-tenant session analytics = SECURITY FAILURE → STOP_EXECUTION + LOG_INCIDENT
  - tenant_id extracted from service JWT claims only (never from payload)

DOMAIN ISOLATION:
  - Rubric version must match domain_track of session
  - Cohort benchmark computation: only includes participants from same domain_track + tenant
  - Cross-domain analytics aggregation = FORBIDDEN without explicit platform-level grant

ROLE-BASED AUTHORIZATION:
  - STUDENT:         can view own analytics record (via Student Dashboard)
  - TEACHER/MENTOR:  can view session_analytics_summary for own sessions + participants
  - INSTITUTE_ADMIN: can view cohort aggregates for own tenant
  - PLATFORM_ADMIN:  full read access to all analytics (audit mode only)
  - PARENT:          bounded view (student_session_analytics — bounded schema, see below)
  - RECRUITER/HR:    access only to verified skill proof reports via TALENT_MARKETPLACE layer;
                     raw session analytics NOT exposed directly
  - EVALUATOR:       read-only session_analytics_summary in audit context

STUDENT DATA EXPOSURE RULES:
  - Individual scores never exposed between students in a session
  - Ranked participant list for Teacher Dashboard: user_ids visible to teacher only
  - Student-facing: own score + own percentile rank (no peer scores shown)
  - No raw message content included in analytics output payloads (content privacy)

PARENT-BOUNDED ANALYTICS VIEW:
  Exposed to parent:
    - session date, topic title
    - attendance_status (human-readable)
    - session_performance: "Above Average" | "Average" | "Needs Improvement"
      (bucketed from percentile_rank — no raw score)
    - learning_gain trend: "Improving" | "Stable" | "Needs Attention"
  NOT exposed to parent:
    - raw final_composite_score
    - individual score components
    - intelligence_delta raw values
    - variance_anomaly_flag
    - collusion_suspicion_flag
    - teacher_score_component
    - model confidence scores

SCORE DATA SECURITY:
  - All PII and score data at rest: AES-256
  - All inter-service: TLS 1.3
  - Analytics records: PostgreSQL RLS enforced
  - No plaintext secrets (Secret Manager only)
  - ML model artifacts stored in encrypted model registry (access via model_version key only)

RUBRIC VERSION SECURITY:
  - Only APPROVED + PUBLISHED rubric versions accepted
  - DRAFT rubric versions cannot be used for live session scoring
  - Rubric access: read-only for analytics agent (no rubric modification authority)
```

---

## 🔟 AUDIT & TRACEABILITY

Every analytics computation event produces an immutable audit record:

```json
AUDIT_RECORD:
{
  "audit_id": "UUID",
  "timestamp_utc": "ISO-8601",
  "actor_id": "SYSTEM (GD_POST_SESSION_ANALYTICS_AGENT)",
  "tenant_id": "UUID",
  "session_id": "UUID",
  "user_id": "UUID | null (null for session-level events)",
  "event_type": "ENUM:
    ANALYTICS_PIPELINE_TRIGGERED |
    DEPENDENCY_WAIT_TIMEOUT |
    SCORING_COMPUTED |
    INTELLIGENCE_DELTA_COMPUTED |
    SESSION_QUALITY_SCORED |
    LEARNING_GAIN_COMPUTED |
    COHORT_BENCHMARK_COMPUTED |
    BELT_ELIGIBILITY_SIGNAL_EMITTED |
    VARIANCE_ANOMALY_DETECTED |
    COLLUSION_SUSPICION_FLAGGED |
    DIFFICULTY_FEEDBACK_EMITTED |
    TEACHER_VARIANCE_FLAGGED |
    ANALYTICS_RECORD_FINALIZED |
    DOWNSTREAM_PAYLOAD_EMITTED |
    VALIDATION_FAILED |
    SECURITY_VIOLATION |
    PIPELINE_FAILED",
  "input_hash": "SHA-256 of canonical input payload",
  "output_hash": "SHA-256 of canonical output record",
  "model_version": "GD_ANALYTICS_ML_v1.0.0",
  "rubric_version": "string",
  "decision_path": "string (e.g., ML_SCORING → DIFFICULTY_NORM → INTER_RATER_ADJ → DELTA_CALC → EMIT)",
  "confidence_score": "0.0–1.0",
  "anomaly_flags": ["ARRAY of flag strings"]
}
```

**Audit Log Rules:**
- Append-only, immutable store
- Replicated to secondary sink within 30 seconds
- Retained per tenant compliance policy (minimum 90 days)
- Exportable for ERP, LMS, HRIS compliance
- Score computation chain fully traceable: every formula step logged via decision_path
- Teacher score overrides produce separate audit record (original ML baseline preserved)
- Rubric version change automatically triggers re-audit flag for prior sessions
  (DOES NOT recompute — just flags for manual review)

---

## 1️⃣1️⃣ FAILURE POLICY

```
FAILURE: ATTENDANCE_RECORDS NOT READY (blocking dependency)
  ACTION: HOLD pipeline (do not start)
  MAX WAIT: 48 hours from session_completed_utc
  AFTER TIMEOUT: Proceed with available data, mark PARTIAL_MISSING_ATTENDANCE
  LOG: LOG_INCIDENT
  ESCALATE_TO: GD_ATTENDANCE_TRACKING_AGENT + OBSERVABILITY_AGENT

FAILURE: RUBRIC VERSION NOT FOUND or DRAFT
  ACTION: STOP_EXECUTION
  LOG: LOG_INCIDENT (configuration error)
  ESCALATE_TO: RUBRIC_REGISTRY_AGENT + TENANT_ADMIN
  RETRY_POLICY: Retry every 30 minutes for 6 hours; then escalate to PLATFORM_ADMIN

FAILURE: ML MODEL UNAVAILABLE
  ACTION: STOP_EXECUTION of analytics pipeline
  FALLBACK: Queue session for retry when model restored
  LOG: LOG_INCIDENT
  ESCALATE_TO: OBSERVABILITY_AGENT → PagerDuty P2
  RETRY_POLICY: 3 retries with exponential backoff; then DLQ

FAILURE: AI SCOPE TIMEOUT (AI inference > 5000ms)
  ACTION: Proceed without AI component
  ACTION: Mark AI_SCOPE_SKIPPED in analytics_record
  LOG: LOG_INCIDENT (non-critical)
  ESCALATE_TO: OBSERVABILITY_AGENT (non-paging alert)
  RETRY_POLICY: No retry on AI; ML result used only

FAILURE: CONFIDENCE BELOW THRESHOLD (confidence_score < 0.75)
  ACTION: Complete analytics computation
  ACTION: Set confidence_score in output, flag LOW_CONFIDENCE
  ACTION: Belt eligibility signal marked ADVISORY_LOW_CONFIDENCE
  LOG: LOG_INCIDENT
  ESCALATE_TO: TEACHER_REVIEW_QUEUE (if teacher exists) else TENANT_ADMIN
  NOTE: Low confidence analytics STILL emitted to downstream — all must check confidence

FAILURE: VARIANCE ANOMALY HIGH SEVERITY
  ACTION: Complete analytics computation
  ACTION: Set session_integrity_verdict = ANOMALY_DETECTED
  ACTION: Emit to ANTI_COLLUSION_DETECTION_AGENT immediately
  ACTION: Belt credits for all participants in session HELD pending review
  LOG: LOG_INCIDENT
  ESCALATE_TO: ANTI_COLLUSION_DETECTION_AGENT + GOVERNANCE_BOARD

FAILURE: DATA CORRUPTION (inconsistent session state)
  ACTION: STOP_EXECUTION
  ACTION: Mark session analytics as FAILED
  LOG: LOG_INCIDENT with full state snapshot
  ESCALATE_TO: PLATFORM_ADMIN + OBSERVABILITY_AGENT
  RETRY_POLICY: No auto-retry; manual investigation required

FAILURE: SECURITY VIOLATION (cross-tenant access attempt)
  ACTION: STOP_EXECUTION immediately
  LOG: LOG_INCIDENT (SECURITY_VIOLATION)
  ESCALATE_TO: PLATFORM_COMPLIANCE_ADMIN immediately (PagerDuty P1)
  RETRY_POLICY: No retry. Access suspended.
```

**No silent failures. Every failure produces an immutable audit record.**

---

## 1️⃣2️⃣ INTER-AGENT DEPENDENCY MAP

### Event Flow: Analytics Pipeline Trigger

```
GD_SESSION_SCHEDULER_AGENT
  → emits: SESSION_COMPLETED (session_id, tenant_id, group_id, topic_id)
  → GD_ATTENDANCE_TRACKING_AGENT processes and emits: ATTENDANCE_RECORDS_READY
  → TEACHER_SCORE_INPUT_AGENT: 24h window opens for optional teacher score
  → After 24h OR teacher_score_submitted → GD_POST_SESSION_ANALYTICS_AGENT triggered
  → Consumes: ATTENDANCE_RECORDS_READY + optional teacher_score

GD_POST_SESSION_ANALYTICS_AGENT dependency checks:
  1. ATTENDANCE_RECORDS_READY = true           → proceed
  2. Teacher score window elapsed OR submitted  → proceed
  3. ANTI_COLLUSION flags processed            → proceed
```

### Event Flow: Analytics Output Dispatch

```
GD_POST_SESSION_ANALYTICS_AGENT
  → emits to PASSPORT_UPDATE_AGENT:
      INTELLIGENCE_DELTA_PAYLOAD (per user)
  → emits to BELT_ENGINE_AGENT:
      BELT_ELIGIBILITY_SIGNAL (advisory, per user)
  → emits to LEADERBOARD_AGENT:
      RANKED_SESSION_RESULT (session summary + ranked list)
  → emits to TEACHER_DASHBOARD_AGENT:
      SESSION_ANALYTICS_SUMMARY (full summary + AI narrative)
  → emits to INSTITUTE_ERP_AGENT:
      COHORT_ANALYTICS_UPDATE (aggregate)
  → emits to PARENT_REPORT_AGENT:
      STUDENT_SESSION_ANALYTICS (bounded schema per child)
  → emits to TOPIC_DIFFICULTY_CALIBRATION_AGENT:
      DIFFICULTY_FEEDBACK_SIGNAL (pass_rate, avg_score, difficulty_drift_flag)
  → emits to CURRICULUM_HEALTH_AGENT:
      SESSION_QUALITY_SIGNAL (session_quality_score, topic_id)
  → emits to FEATURE_STORE_AGENT:
      BEHAVIORAL_FEATURE_VECTORS (per user, see section 13)
  → emits to ANTI_COLLUSION_DETECTION_AGENT:
      SCORE_DISTRIBUTION_PAYLOAD + VARIANCE_ANOMALY_FLAG
  → emits to GROWTH_ENGINE_AGENT:
      TOP_PERFORMER_EVENT (if top_performer_flag = true)
  → emits to RATER_CALIBRATION_AGENT:
      SCORER_VARIANCE_FLAG (if teacher vs ML delta > 30 points)
  → emits to AUDIT_LOG_AGENT:
      ANALYTICS_RECORD_FINALIZED audit records (all)
```

### EVENT_TRIGGERS (Kafka Topics)

```
INBOUND TOPICS:
  - gd.analytics.session_trigger          (from SESSION_SCHEDULER + ATTENDANCE_AGENT)
  - gd.analytics.teacher_score_ready      (from TEACHER_SCORE_INPUT_AGENT)

OUTBOUND TOPICS:
  - gd.analytics.records_ready            (per-participant analytics)
  - gd.analytics.session_summary          (per-session summary)
  - gd.analytics.intelligence_deltas      (per user, per session)
  - gd.analytics.belt_eligibility         (per user, advisory)
  - gd.analytics.difficulty_feedback      (per topic)
  - gd.analytics.anomaly_detected         (if variance anomaly)
  - gd.analytics.top_performer            (if top_performer_flag)
  - gd.analytics.pipeline_failed          (on unrecoverable error)
  - gd.analytics.security_violation
```

---

## 1️⃣3️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent **must** emit structured feature vectors to FEATURE_STORE_AGENT for every
completed participant analytics record:

```json
EMIT_FEATURE_VECTOR (per user per session):
{
  "user_id": "UUID",
  "session_id": "UUID",
  "tenant_id": "UUID",
  "source_agent": "GD_POST_SESSION_ANALYTICS_AGENT",
  "domain_track": "ENUM",
  "timestamp_utc": "ISO-8601",
  "features": {
    "gd_final_composite_score":        "float 0.0–100.0",
    "gd_difficulty_normalized_score":  "float 0.0–100.0",
    "gd_participation_score":          "float",
    "gd_role_completion_score":        "float",
    "gd_idea_contribution_score":      "float",
    "gd_phase_engagement_score":       "float",
    "gd_learning_gain_delta":          "float",
    "gd_regression_flag":              "integer (0|1)",
    "gd_percentile_rank":              "float 0.0–100.0",
    "gd_top_performer_flag":           "integer (0|1)",
    "gd_ideas_submitted_count":        "integer",
    "gd_messages_sent_total":          "integer",
    "gd_role_assigned":                "integer (encoded 0–4)",
    "gd_session_mode":                 "integer (TEXT=0|VOICE=1|VIDEO=2)",
    "gd_interpersonal_delta":          "float",
    "gd_linguistic_delta":             "float",
    "gd_leadership_delta":             "float",
    "gd_logical_delta":                "float",
    "gd_creative_delta":               "float",
    "gd_intrapersonal_delta":          "float"
  }
}
```

These features feed:
- Passive Intelligence Engine (PIE) — real behavior intelligence signal
- Skill Gap Analysis Engine — score vs benchmark gap identification
- Retention Prediction Engine — learning trajectory signals
- User Growth Prediction Engine (ML Engine 3) — talent development forecasting
- Ranking Engine (ML Engine 5) — XP and innovation rank computation

---

## 1️⃣4️⃣ TOPIC DIFFICULTY CALIBRATION FEEDBACK

This agent feeds the TOPIC_DIFFICULTY_CALIBRATION_AGENT after every session:
(Implements T4 — Scenario Difficulty Calibration Lock from Dojo For Arts)

```json
DIFFICULTY_FEEDBACK_SIGNAL:
{
  "topic_id": "UUID",
  "session_id": "UUID",
  "tenant_id": "UUID",
  "domain_track": "ENUM",
  "session_pass_rate": "float (participants scoring >= 60 / total participants)",
  "session_avg_score": "float",
  "session_score_stddev": "float",
  "score_distribution_p25": "float",
  "score_distribution_p75": "float",
  "dropout_rate_in_session": "float (from attendance records)",
  "avg_phase_engagement": "float",
  "low_engagement_phases": ["ARRAY of phase names with avg_engagement < 40"],
  "difficulty_drift_flag": "boolean (true if pass_rate deviates >15% from historical avg)",
  "session_count_for_topic": "integer (total sessions this topic has run)",
  "model_version": "string",
  "timestamp_utc": "ISO-8601"
}
```

**TOPIC_DIFFICULTY_CALIBRATION_AGENT uses this signal to:**
- Automatically reclassify topic difficulty label (data-derived, not author-declared)
- Flag topic for retirement if pass_rate consistently < 20% over 10+ sessions
- Flag topic for fairness audit if score_stddev > 25
- Reduce topic difficulty weight in difficulty_normalization_factor

---

## 1️⃣5️⃣ LEARNING EFFECTIVENESS LOOP (T6 LOCK IMPLEMENTATION)

This agent implements the learning effectiveness measurement required by T6 of Dojo For Arts:

```
PRE-ASSESSMENT BASELINE:
  - Captured from user's prior 5 sessions in same domain_track
  - Stored in: gd_analytics_participant_history table
  - baseline_score_5session_avg = avg(last_5_final_composite_scores)

POST-SESSION SCORE:
  - current_session_final_composite_score (computed by this agent)

DELTA IMPROVEMENT:
  - learning_gain_delta = current_score - baseline_score_5session_avg
  - Positive delta = improvement
  - Zero delta = stable
  - Negative delta = regression

RETENTION CHECK MATCHES:
  - Agent tracks score trend across all sessions (stored in history table)
  - Detects regression: if last 3 sessions trend downward
  - Regression detection → CURRICULUM_HEALTH_AGENT flag

DRILL EFFECTIVENESS SCORES:
  - Not computed by this agent (computed by CURRICULUM_HEALTH_AGENT from aggregated signals)
  - This agent provides the per-session input signal

SKILL TRACKS WITHOUT MEASURABLE IMPROVEMENT:
  - If avg learning_gain_delta for same topic across all users < 2.0 over 10 sessions:
    → CURRICULUM_HEALTH_AGENT receives SESSION_QUALITY_SIGNAL with low_improvement_flag
    → Curriculum board flagged for review
```

---

## 1️⃣6️⃣ RATER CALIBRATION INTEGRATION (T2 + T3 LOCK IMPLEMENTATION)

This agent participates in rater calibration as required by T2 and T3 of Dojo For Arts:

```
INTER-RATER RELIABILITY TRACKING:
  - After every session with teacher_score submitted:
    Emit SCORER_VARIANCE_EVENT to RATER_CALIBRATION_AGENT:
    {
      teacher_id, session_id, topic_id, domain_track,
      teacher_score_normalized: float,
      ml_composite_score: float,
      variance_delta: float,
      variance_flag: boolean (delta > 30 points)
    }

RATER CALIBRATION AGENT uses this to:
  - Compute teacher_bias_correction_offset (used by this agent in inter-rater adjustment)
  - Track scorer_drift over time
  - Generate mentor_bias_reports
  - Trigger mentor re-certification if drift > tolerance

THIS AGENT uses RATER_CALIBRATION_AGENT output:
  - teacher_bias_correction_offset fed back into inter_rater_adjusted_score formula
  - Creates closed feedback loop between analytics and calibration

LOW CONFIDENCE SCORES AND BELT PROMOTION:
  - As required by T2: "Low confidence scores cannot trigger belt promotion
    without mentor board review"
  - This agent enforces: if confidence_score < 0.75:
    belt_eligibility_signal.advisory_note = "LOW_CONFIDENCE_MENTOR_REVIEW_REQUIRED"
  - BELT_ENGINE_AGENT must check this field before any promotion decision
```

---

## 1️⃣7️⃣ COLLUSION & MANIPULATION DETECTION INTEGRATION (T9 LOCK)

This agent implements T9 (Collusion & Manipulation Detection Lock) signals:

```
DETECTION SIGNALS COMPUTED BY THIS AGENT:

1. RECIPROCAL HIGH SCORING PAIRS:
   - If 2+ participants in session both have top_performer signals
     and both have ghost_presence_flag = false
     and their per_phase_scores show unusually aligned patterns:
     → emit RECIPROCAL_PATTERN signal to ANTI_COLLUSION_DETECTION_AGENT

2. ABNORMAL PEER SCORE CLUSTERING:
   - If score_stddev < 5.0 for session with >= 5 participants:
     (all participants scored nearly identically)
     → variance_anomaly_flag = true, anomaly_severity = MEDIUM

3. MATCH FARMING PATTERNS:
   - Not computed by this agent (requires cross-session history)
   - This agent provides per-session score to ANTI_COLLUSION_DETECTION_AGENT
     which detects farming patterns across sessions

4. RATING INFLATION CLUSTERS:
   - If avg teacher_score = 5 (max) AND ml_composite_score_avg < 60:
     → SCORER_VARIANCE_FLAG + COLLUSION_SUSPICION_FLAG

5. MENTOR FAVORITISM:
   - If same teacher_id gives 5/5 to same user_id in 3+ consecutive sessions:
     → RATER_CALIBRATION_AGENT receives FAVORITISM_PATTERN_SIGNAL

FLAGGED SESSIONS PROTOCOL:
  - session_integrity_verdict = ANOMALY_DETECTED or COLLUSION_SUSPECTED:
    → belt_session_credit held for ALL participants pending review
    → ANTI_COLLUSION_DETECTION_AGENT is decision authority for clearance
    → This agent DOES NOT clear held credits — only flags them
```

---

## 1️⃣8️⃣ GROWTH ENGINE HOOK

```
TOP_PERFORMER_EVENT (to GROWTH_ENGINE_AGENT):
  Triggered when: top_performer_flag = true AND attendance_status = PRESENT
  {
    user_id, session_id, tenant_id, domain_track,
    final_composite_score, percentile_rank,
    achievement_type: "GD_TOP_PERFORMER",
    share_card_template: "GD_TOP_PERFORMER_CARD",
    timestamp_utc
  }

MILESTONE_ANALYTICS_EVENT (to GROWTH_ENGINE_AGENT):
  Triggered when: learning_gain_delta > 15.0 (major improvement)
  {
    user_id, session_id,
    achievement_type: "GD_MAJOR_IMPROVEMENT",
    learning_gain_delta, timestamp_utc
  }

RANK_UPDATE_EVENT (to LEADERBOARD_AGENT):
  All participants → ranked by final_composite_score
  Leaderboard entry created per session per domain_track

XP signals handled by XP_REWARD_AGENT (consumes BELT_ELIGIBILITY_SIGNAL)
```

---

## 1️⃣9️⃣ PERFORMANCE MONITORING

```
METRICS (emitted to OBSERVABILITY_AGENT):
  - pipeline_trigger_to_completion_latency_p95  (target: < 5 minutes)
  - pipeline_success_rate                        (target: > 99.5%)
  - pipeline_failure_rate                        (alert: > 0.5%)
  - ml_inference_latency_p95_per_model           (per model, targets vary)
  - ai_scope_timeout_rate                        (alert: > 5%)
  - dependency_wait_timeout_rate                 (alert: > 1%)
  - model_drift_accuracy_delta                   (rolling 30-day, alert: > 5%)
  - confidence_below_threshold_rate              (alert: > 10%)
  - variance_anomaly_detection_rate              (monitor trend)
  - teacher_variance_flag_rate                   (monitor trend — high rate = rater calibration issue)
  - collusion_suspicion_flag_rate                (zero tolerance trend monitoring)
  - security_violation_count                     (zero tolerance)
  - dlq_depth                                    (alert: > 100)
  - downstream_payload_delivery_success_rate     (per downstream agent)

DASHBOARDS (mandatory on OBSERVABILITY_AGENT):
  - pipeline_completion_latency_distribution
  - ml_model_confidence_distribution_per_model
  - teacher_vs_ml_variance_distribution
  - variance_anomaly_frequency_by_domain_track
  - learning_gain_trend_by_cohort
  - topic_difficulty_drift_alerts
  - session_quality_score_distribution

ALERTS:
  - Pipeline failure rate > 0.5% over 10min      → PagerDuty P2
  - Security violation detected                   → PagerDuty P1 (immediate)
  - ML model drift > 5% accuracy drop            → PagerDuty P2
  - AI timeout rate > 10% over 1 hour            → PagerDuty P3
  - DLQ depth > 100                              → PagerDuty P2
  - Belt credits held for > 10 sessions (collusion backlog) → PagerDuty P2
```

---

## 2️⃣0️⃣ DATABASE SCHEMA (LOCKED — ADD-ONLY MUTATION)

```sql
-- gd_analytics_participant_records
-- Core analytics record per participant per session
CREATE TABLE gd_analytics_participant_records (
  analytics_record_id           UUID PRIMARY KEY,
  session_id                    UUID NOT NULL REFERENCES gd_sessions(session_id),
  user_id                       UUID NOT NULL,
  tenant_id                     UUID NOT NULL,
  group_id                      UUID NOT NULL,
  topic_id                      UUID NOT NULL,
  domain_track                  VARCHAR(50) NOT NULL,

  raw_composite_score           DECIMAL(6,3),
  difficulty_normalized_score   DECIMAL(6,3),
  inter_rater_adjusted_score    DECIMAL(6,3),
  final_composite_score         DECIMAL(6,3),

  participation_score_raw       DECIMAL(6,3),
  participation_score_weighted  DECIMAL(6,3),
  role_completion_score_raw     DECIMAL(6,3),
  role_completion_score_weighted DECIMAL(6,3),
  idea_contribution_score_raw   DECIMAL(6,3),
  idea_contribution_score_weighted DECIMAL(6,3),
  phase_engagement_score_raw    DECIMAL(6,3),
  phase_engagement_score_weighted DECIMAL(6,3),
  teacher_score_raw             DECIMAL(6,3),
  teacher_score_weighted        DECIMAL(6,3),

  phase_score_preparation       DECIMAL(6,3),
  phase_score_opening           DECIMAL(6,3),
  phase_score_idea_generation   DECIMAL(6,3),
  phase_score_debate            DECIMAL(6,3),
  phase_score_summary           DECIMAL(6,3),

  delta_interpersonal           DECIMAL(6,3),
  delta_linguistic              DECIMAL(6,3),
  delta_leadership              DECIMAL(6,3),
  delta_logical                 DECIMAL(6,3),
  delta_creative                DECIMAL(6,3),
  delta_intrapersonal           DECIMAL(6,3),

  baseline_score_5session_avg   DECIMAL(6,3),
  learning_gain_delta           DECIMAL(6,3),
  learning_trend                VARCHAR(20),
  regression_flag               BOOLEAN NOT NULL DEFAULT FALSE,

  cohort_avg_score              DECIMAL(6,3),
  percentile_rank               DECIMAL(6,3),
  vs_cohort_delta               DECIMAL(6,3),
  benchmark_group_size          INTEGER,

  top_performer_flag            BOOLEAN NOT NULL DEFAULT FALSE,
  variance_anomaly_flag         BOOLEAN NOT NULL DEFAULT FALSE,
  collusion_suspicion_flag      BOOLEAN NOT NULL DEFAULT FALSE,
  belt_eligibility_score_meets  BOOLEAN,

  analytics_data_completeness   VARCHAR(50) NOT NULL DEFAULT 'COMPLETE',
  rubric_version                VARCHAR(100) NOT NULL,
  model_version                 VARCHAR(100) NOT NULL,
  ml_confidence_score           DECIMAL(4,3),
  audit_reference               UUID NOT NULL,
  generated_at_utc              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version                       INTEGER NOT NULL DEFAULT 1
);

-- gd_analytics_session_summary
CREATE TABLE gd_analytics_session_summary (
  summary_id                    UUID PRIMARY KEY,
  session_id                    UUID NOT NULL REFERENCES gd_sessions(session_id),
  tenant_id                     UUID NOT NULL,
  group_id                      UUID NOT NULL,
  topic_id                      UUID NOT NULL,
  domain_track                  VARCHAR(50) NOT NULL,
  participant_count             INTEGER NOT NULL,

  session_quality_score         DECIMAL(6,3),
  avg_composite_score           DECIMAL(6,3),
  score_min                     DECIMAL(6,3),
  score_max                     DECIMAL(6,3),
  score_median                  DECIMAL(6,3),
  score_stddev                  DECIMAL(6,3),
  score_p25                     DECIMAL(6,3),
  score_p75                     DECIMAL(6,3),

  top_performer_user_id         UUID,
  top_performer_score           DECIMAL(6,3),

  phase_engagement_opening_avg  DECIMAL(6,3),
  phase_engagement_idea_avg     DECIMAL(6,3),
  phase_engagement_debate_avg   DECIMAL(6,3),
  phase_engagement_summary_avg  DECIMAL(6,3),

  variance_anomaly_detected     BOOLEAN NOT NULL DEFAULT FALSE,
  collusion_suspicion_count     INTEGER NOT NULL DEFAULT 0,
  teacher_score_applied         BOOLEAN NOT NULL DEFAULT FALSE,
  session_integrity_verdict     VARCHAR(30) NOT NULL DEFAULT 'CLEAN',

  difficulty_pass_rate          DECIMAL(5,4),
  difficulty_drift_flag         BOOLEAN NOT NULL DEFAULT FALSE,

  rubric_version                VARCHAR(100) NOT NULL,
  model_version                 VARCHAR(100) NOT NULL,
  audit_reference               UUID NOT NULL,
  generated_at_utc              TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- gd_analytics_participant_history
-- Rolling history for learning gain computation
CREATE TABLE gd_analytics_participant_history (
  id                            UUID PRIMARY KEY,
  user_id                       UUID NOT NULL,
  tenant_id                     UUID NOT NULL,
  domain_track                  VARCHAR(50) NOT NULL,
  session_id                    UUID NOT NULL,
  final_composite_score         DECIMAL(6,3) NOT NULL,
  session_completed_utc         TIMESTAMPTZ NOT NULL,
  created_at                    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
-- Index for fast 5-session baseline query:
CREATE INDEX idx_participant_history_user_domain
  ON gd_analytics_participant_history(user_id, domain_track, session_completed_utc DESC);

-- gd_idea_quality_scores (TEXT GD only)
CREATE TABLE gd_idea_quality_scores (
  id                            UUID PRIMARY KEY,
  session_id                    UUID NOT NULL,
  user_id                       UUID NOT NULL,
  tenant_id                     UUID NOT NULL,
  message_id                    UUID NOT NULL REFERENCES gd_messages(message_id),
  relevance_score               DECIMAL(6,3),
  clarity_score                 DECIMAL(6,3),
  originality_score             DECIMAL(6,3),
  semantic_relevance_score      DECIMAL(6,3),
  semantic_duplicate_flag       BOOLEAN NOT NULL DEFAULT FALSE,
  idea_quality_ml_score         DECIMAL(6,3) NOT NULL,
  ai_advisory_used              BOOLEAN NOT NULL DEFAULT FALSE,
  model_version                 VARCHAR(100) NOT NULL,
  created_at                    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- gd_analytics_audit_log (APPEND-ONLY)
CREATE TABLE gd_analytics_audit_log (
  audit_id                      UUID PRIMARY KEY,
  timestamp_utc                 TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  actor_id                      VARCHAR(100) NOT NULL, -- SYSTEM agent name
  tenant_id                     UUID NOT NULL,
  session_id                    UUID,
  user_id                       UUID,
  event_type                    VARCHAR(60) NOT NULL,
  input_hash                    VARCHAR(64),
  output_hash                   VARCHAR(64),
  model_version                 VARCHAR(100),
  rubric_version                VARCHAR(100),
  decision_path                 TEXT,
  confidence_score              DECIMAL(4,3),
  anomaly_flags                 TEXT[]
);

-- ROW LEVEL SECURITY
ALTER TABLE gd_analytics_participant_records     ENABLE ROW LEVEL SECURITY;
ALTER TABLE gd_analytics_session_summary         ENABLE ROW LEVEL SECURITY;
ALTER TABLE gd_analytics_participant_history     ENABLE ROW LEVEL SECURITY;
ALTER TABLE gd_idea_quality_scores               ENABLE ROW LEVEL SECURITY;
-- gd_analytics_audit_log: no RLS (platform-wide compliance access controlled at API layer)
```

---

## 2️⃣1️⃣ MICROSERVICES REQUIRED

```
SERVICE                           RESPONSIBILITY
Analytics Trigger Service         Consume session_completed events; manage dependency
                                  checks (attendance ready, teacher score window);
                                  dispatch pipeline jobs

Scoring Computation Service       Execute weighted composite scoring formula;
                                  difficulty normalization; inter-rater adjustment;
                                  produce final_composite_score per participant

Intelligence Delta Service        Map scores to intelligence dimensions;
                                  compute deltas per dimension per participant;
                                  produce passport_update_payload

Session Quality Service           Compute session-level quality score;
                                  score distribution; variance anomaly detection;
                                  produce session_analytics_summary

Learning Gain Service             Rolling 5-session baseline management;
                                  learning_gain_delta; trend classification;
                                  regression detection; curriculum health signal

NLP Analysis Service              TEXT GD only: idea quality scoring (relevance,
                                  clarity, originality); linguistic classifier;
                                  vocabulary diversity; AI semantic scope (bounded)
```

Additional services consumed (not owned):
- Rubric Registry Service (rubric version lookup)
- Rater Calibration Service (teacher_bias_correction_offset)
- Topic Difficulty Calibration Service (current difficulty data)
- Audit Log Service (append-only sink)
- Feature Store Service (behavioral vector ingest)

---

## 2️⃣2️⃣ VERSIONING POLICY

```
VERSION FORMAT: GD_ANALYTICS_ML_v{MAJOR}.{MINOR}.{PATCH}
CURRENT VERSION: v1.0.0

CHANGES REQUIRING MAJOR VERSION BUMP:
  - Any score component weight change
  - Scoring formula structural change
  - Intelligence dimension mapping change
  - Delta calculation formula change
  - Teacher score normalization change
  - Difficulty normalization formula change
  - Inter-rater adjustment formula change
  - Belt eligibility threshold change
  - Learning gain baseline window change (currently 5 sessions)
  - DB schema field rename or removal (FORBIDDEN without major + migration)

CHANGES REQUIRING MINOR VERSION BUMP:
  - New score component added
  - New intelligence dimension added
  - New ML model feature added
  - New output field added
  - New downstream payload field added

PATCH VERSION:
  - Bug fixes, performance improvements
  - No formula or interface change

RUBRIC VERSION INDEPENDENCE:
  - model_version and rubric_version are tracked SEPARATELY
  - Rubric version change does not force model version change
  - But rubric change triggers: re-validation of intelligence dimension mapping
  - Analytics records must store BOTH versions independently

BACKWARD COMPATIBILITY WINDOW: 60 days minimum
PRIOR MODEL VERSIONS: Retained 60 days for rollback
HISTORICAL RECORDS: Never recomputed on model change — flagged for review only
```

---

## 2️⃣3️⃣ NON-NEGOTIABLE RULES

This agent must **NOT**:

```
❌ Begin analytics pipeline before ATTENDANCE_RECORDS_READY = true
❌ Begin analytics pipeline before teacher score window has elapsed (24h)
❌ Modify or overwrite historical analytics records
❌ Auto-delete any audit log entry
❌ Make belt promotion decisions (advisory signal only — BELT_ENGINE decides)
❌ Override GOVERNANCE agents, ANTI_COLLUSION_DETECTION_AGENT, or CURRICULUM_HEALTH_AGENT
❌ Use DRAFT rubric versions for score computation
❌ Accept cross-tenant analytics pipeline triggers
❌ Compute analytics for sessions with status != COMPLETED
❌ Use AI to compute any score component or intelligence delta
❌ Use AI output as sole determinant of any output field
❌ Clear held belt credits (ANTI_COLLUSION_DETECTION_AGENT is the clearance authority)
❌ Expose raw peer scores to students (only own score + percentile rank visible)
❌ Expose ghost_presence_flag, ML confidence scores, or variance_anomaly_flag to parents
❌ Include raw message content in any analytics output payload
❌ Mix domain data from different domain_tracks in cohort benchmarks
❌ Mix tenant data in any computation or aggregate
❌ Emit analytics records without rubric_version + model_version + audit_reference
❌ Proceed after SECURITY_VIOLATION — always STOP_EXECUTION
❌ Auto-recompute historical analytics on model version change
❌ Apply difficulty normalization to topics with < 10 historical sessions
   (must use factor = 1.0 and flag INSUFFICIENT_CALIBRATION_HISTORY)
❌ Award top_performer_flag to participants with ABSENT, GHOST, or PARTIAL attendance
❌ Execute NLP pipeline on VOICE or VIDEO GD message content
   (voice sessions have no text idea submissions)
```

---

## 🔒 FINAL GOVERNANCE SEAL

```
GD_POST_SESSION_ANALYTICS_AGENT PRODUCTION MODE ENABLED

✅ Tenant Isolation:               HARD ENFORCED (PostgreSQL RLS + JWT)
✅ Domain Isolation:               HARD ENFORCED (rubric + cohort + benchmark scoping)
✅ Security Model:                 ZERO-TRUST MULTI-TENANT
✅ Execution Mode:                 DETERMINISTIC + VALIDATED
✅ Mutation Policy:                ADD-ONLY VERSIONED
✅ Audit Trail:                    APPEND-ONLY IMMUTABLE (all computation steps logged)
✅ ML Primary:                     70–80% (6 ML models — scoring, quality, anomaly, learning)
✅ AI Bounded Scope:               20–30% (4 scopes — idea semantic, summary, curriculum,
                                   anomaly narrative — ALL advisory only)
✅ Scoring Formula:                WEIGHTED COMPOSITE — IMMUTABLE (rubric-versioned)
✅ Difficulty Normalization:       DATA-DERIVED (not author-declared — T4 compliance)
✅ Inter-Rater Adjustment:         RATER CALIBRATION AGENT FEED (T2 + T3 compliance)
✅ Intelligence Delta:             RUBRIC-GOVERNED DIMENSION MAPPING (T1 compliance)
✅ Learning Gain Measurement:      5-SESSION ROLLING BASELINE (T6 compliance)
✅ Variance Anomaly Detection:     ISOLATION FOREST (T9 compliance)
✅ Belt Eligibility:               ADVISORY ONLY — BELT ENGINE DECIDES ALWAYS
✅ Low Confidence:                 FLAGGED — MENTOR BOARD REVIEW REQUIRED (T2 compliance)
✅ Teacher Score:                  OPTIONAL — WEIGHT REDISTRIBUTED IF ABSENT
✅ Teacher Variance:               FLAGGED TO RATER CALIBRATION AGENT (T3 compliance)
✅ Dependency Gate:                ATTENDANCE_READY + TEACHER_WINDOW ENFORCED
✅ Student Score Privacy:          OWN SCORE + PERCENTILE ONLY — NO PEER SCORES
✅ Parent Bounded View:            BUCKETED PERFORMANCE ONLY — NO RAW SCORES
✅ Collusion Integration:          SIGNALS TO ANTI_COLLUSION_DETECTION_AGENT ONLY
✅ Topic Difficulty Feedback:      EMITTED AFTER EVERY SESSION (T4 compliance)
✅ Curriculum Health Feed:         SESSION_QUALITY_SIGNAL EMITTED (T6 compliance)
✅ Passive Intelligence Feed:      20 FEATURE VECTORS PER USER PER SESSION
✅ TEXT / VOICE / VIDEO PARITY:    SEPARATE PIPELINE PATHS — NO MODE CONFUSION
✅ Offline Session Support:        ANALYTICS RUNS AFTER OFFLINE SYNC COMPLETES
✅ Failure Policy:                 HALT ON AMBIGUITY — NO SILENT FAILURES
✅ Observability:                  FULL TELEMETRY TO OBSERVABILITY_AGENT
✅ Scalability:                    STATELESS | KAFKA-ASYNC | BATCH ML | REDIS CACHE
✅ Backward Compatibility:         60-DAY WINDOW MANDATORY
✅ Historical Records:             NEVER RECOMPUTED — FLAGGED FOR REVIEW ON MODEL CHANGE
✅ Interpretation Authority:       NONE
✅ Architecture Authority:         LOCKED

SEALED & LOCKED
ARTIFACT VERSION: v1.0.0
PLATFORM: ECOSKILLER ANTIGRAVITY
DOMAIN: DOJO ENGINE → GROUP DISCUSSION → POST-SESSION ANALYTICS & INTELLIGENCE LAYER
SEAL DATE: 2026-02-25
```

---

*This document is a sealed production artifact. All scoring formula weights, intelligence
dimension mappings, delta calculation formulas, and eligibility thresholds are immutable
without a MAJOR version bump, migration documentation, and rubric governance board approval.
No field may be renamed or removed. No ML model may replace the scoring formula. AI is
advisory only and must never compute scores. Creative interpretation is forbidden.*
