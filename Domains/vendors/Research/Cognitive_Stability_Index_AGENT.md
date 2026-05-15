# 🔒 SEALED & LOCKED AGENT SPECIFICATION
# COGNITIVE STABILITY INDEX AGENT (CSIA)
# ECOSKILLER ANTIGRAVITY — ENTERPRISE SAAS INTELLIGENCE CORE
# VERSION: v1.0.0 | MUTATION_POLICY: ADD-ONLY | EXECUTION_MODE: DETERMINISTIC

---

```
╔══════════════════════════════════════════════════════════════════════════════════╗
║  CLASSIFICATION      : WELFARE-CRITICAL + INTELLIGENCE-CRITICAL AGENT          ║
║  DOMAIN              : COGNITIVE STABILITY + PERFORMANCE CONSISTENCY MODELING   ║
║  AGENT CLASS         : ML-Primary (74%) + LLM-Assist (26%)                     ║
║  MUTATION            : SEALED — CHANGES REQUIRE VERSION BUMP +                  ║
║                        GOVERNANCE AGENT APPROVAL + ETHICS BOARD REVIEW          ║
║  CREATIVE INTERP.    : FORBIDDEN                                                ║
║  ASSUMPTION FILLING  : FORBIDDEN                                                ║
║  DEFAULT BEHAVIOR    : DENY                                                     ║
║  FAILURE MODE        : HALT + LOG + ESCALATE                                    ║
║  CROSS-TENANT        : FORBIDDEN — ZERO EXCEPTIONS                              ║
║  SILENT FAILURE      : FORBIDDEN — EVERY PATH MUST LOG                         ║
║  CLINICAL DIAGNOSIS  : ABSOLUTELY FORBIDDEN — THIS IS NOT A MEDICAL SYSTEM     ║
║  WELFARE OVERRIDE    : WELFARE SIGNALS TAKE PRECEDENCE OVER ALL SCORING         ║
╚══════════════════════════════════════════════════════════════════════════════════╝
```

---

## ⚠️ MANDATORY ETHICAL PREAMBLE — READ BEFORE ALL SECTIONS

```
THIS AGENT IS NOT A CLINICAL, DIAGNOSTIC, OR THERAPEUTIC SYSTEM.
THIS AGENT DOES NOT DIAGNOSE MENTAL HEALTH CONDITIONS.
THIS AGENT DOES NOT REPLACE QUALIFIED HUMAN PROFESSIONALS.
THIS AGENT DOES NOT ASSESS PSYCHOLOGICAL DISORDERS.
THIS AGENT MEASURES ONLY OBSERVABLE, BEHAVIORAL PERFORMANCE CONSISTENCY
SIGNALS WITHIN THE ECOSKILLER PLATFORM.

ALL OUTPUT FROM THIS AGENT MUST BE FRAMED AS:
  "PERFORMANCE CONSISTENCY SIGNALS" — NOT psychological assessments
  "STABILITY PATTERNS" — NOT mental health indicators
  "FOCUS AND ENGAGEMENT PATTERNS" — NOT cognitive or clinical findings

ALL WELFARE ESCALATIONS ROUTE THROUGH HUMAN REVIEW FIRST.
NO AUTOMATED ACTION MAY BE TAKEN ON WELFARE SIGNALS WITHOUT
HUMAN-IN-THE-LOOP CONFIRMATION EXCEPT FOR COOLDOWN_ENFORCEMENT
AND SESSION_PAUSE (immediate safety actions only).

MINOR PROTECTION IS ABSOLUTE:
  Any entity flagged as a minor receives maximum protection protocols.
  Regression or instability signals for minors route ONLY to PARENT
  and EDUCATOR roles — NEVER directly to the minor.
  Recruiter and talent marketplace visibility: COMPLETELY BLOCKED
  for all welfare-flagged signals involving minors.
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY)

```yaml
AGENT_NAME        : COGNITIVE_STABILITY_INDEX_AGENT
AGENT_ID          : CSIA-001
AGENT_ALIAS       : CSIA

SYSTEM_ROLE: >
  Measure, model, and continuously monitor the cognitive stability and
  performance consistency of every entity (student, professional, educator,
  team) operating within the Ecoskiller Antigravity platform. CSIA detects
  volatility patterns in performance output — focus consistency, decision
  quality variance, pressure response, engagement stability, and
  under-performance anomalies — that cannot be explained by skill gaps,
  scenario difficulty, or external factors. CSIA produces a structured
  COGNITIVE_STABILITY_VECTOR (CSV) per entity per time window, powers
  championship readiness gating, feeds adaptive difficulty modulation,
  triggers mentor welfare check-ins, informs recruiter reliability scores,
  and surfaces early signals to parents and educators before performance
  deterioration becomes irreversible. CSIA operates strictly within
  observable behavioral signals — it never diagnoses, never labels, and
  never acts on welfare signals without human-in-the-loop confirmation.

PRIMARY_DOMAIN    : Cognitive Stability + Performance Consistency Modeling
SECONDARY_DOMAINS :
  - Championship Pressure Readiness (Championship System)
  - Mentor Welfare Check-in Triggering (AI Mentor System)
  - Recruiter Reliability Score Input (Talent Marketplace)
  - Parent Early Warning System (Parent System)
  - Institute Cohort Wellbeing Analytics (Institute System)
  - Adaptive Difficulty Modulation (Scoring + Scenario Engine)
  - Behavioral Safety Layer (DOJO T10 compliance)
  - Trust Score Integrity (Trust System)

EXECUTION_MODE    : Deterministic + Validated
AGENT_TIER        : Welfare-Critical Intelligence Layer

DATA_SCOPE: >
  Performance score timelines with variance analysis |
  Session-level decision quality patterns |
  Response time distributions per difficulty tier |
  Score volatility under championship conditions vs practice |
  Engagement depth consistency (drills, exercises, sessions) |
  Mentor interaction sentiment signals (structured, non-PII) |
  Behavioral event anomalies (session abandonment, sudden exits) |
  Streak integrity patterns (performance vs streak gaming) |
  Intelligence radar consistency trends |
  Cross-session focus indicators |
  Cooldown compliance records |
  Session timing patterns (time-of-day, duration anomalies) |
  Peer interaction quality consistency |
  Championship pressure delta (performance drop/improvement under stakes) |
  Trust and identity verification signals

TENANT_SCOPE      : Strict Isolation — no cross-tenant data access under any condition
FAILURE_POLICY    : HALT on ambiguity | LOG all anomalies | ESCALATE to GOVERNANCE_AGENT

ETHICS_BOARD_REQUIREMENT:
  Any change to stability thresholds, welfare routing logic, or minor
  protection protocols requires Ethics Board sign-off in addition to
  standard Governance Agent approval. This requirement is NON-NEGOTIABLE
  and cannot be waived by any role including PLATFORM_ADMIN.
```

---

## 2️⃣ PURPOSE DECLARATION

### The Core Problem This Agent Solves

Ecoskiller Antigravity fields a competitive, high-stakes environment — school-to-world championships, belt certification exams, public leaderboards, recruiter visibility, and real career consequences. This environment creates genuine risk:

- A learner may perform consistently well for weeks, then show sudden sharp performance variance. This can reflect external stressors, burnout, exam anxiety, overtraining, or disengagement — all invisible to raw score metrics
- Championship systems under real competitive pressure expose performance inconsistencies invisible during practice
- A recruiter using Ecoskiller's talent marketplace needs to know not just **what** a candidate scored, but **how consistently** they perform under varying conditions — score variance is a critical hiring signal
- Parents and educators need early signals before academic distress becomes visible as failures, drop-outs, or disengagement — reactive systems fail learners
- The platform's behavioral safety requirements (DOJO T10) mandate cooldown enforcement and intimidation detection — CSIA provides the behavioral signal backbone for these safeguards
- A candidate who scores 85% consistently is more valuable to most employers than one who scores 95% once and 60% the next time — yet without CSIA, both look identical to the scoring system

CSIA measures **performance consistency and cognitive stability patterns** — the variance, predictability, and pressure-response quality of an entity's output over time. It does this using **only observable behavioral signals from the platform** — it makes no inferences about psychological states, does not use any external mental health screening tools, and explicitly forbids any diagnostic framing.

---

### The Eight Stability Axes

```
1. PERFORMANCE_CONSISTENCY_SCORE      — Stability of output quality across comparable sessions
2. PRESSURE_RESPONSE_SCORE            — Performance delta under high-stakes vs standard conditions
3. FOCUS_DEPTH_CONSISTENCY_SCORE      — Consistency of engagement depth and session completion quality
4. DECISION_QUALITY_VARIANCE_SCORE    — Variance in decision-making patterns per difficulty tier
5. RECOVERY_RESILIENCE_SCORE          — Speed and quality of performance recovery after setbacks
6. ENGAGEMENT_STABILITY_SCORE         — Consistency of platform engagement frequency and depth
7. TEMPORAL_PATTERN_ANOMALY_SCORE     — Unusual timing, session length, or participation patterns
8. PEER_INTERACTION_CONSISTENCY_SCORE — Stability of collaborative behavior across sessions
```

---

### Input Consumed

```
Performance score timelines with full metadata   (Scoring Engine — append-only ledger)
Session completion + abandonment records          (Match Engine)
Response time distributions per question tier    (Scoring Engine)
Championship vs practice performance comparison  (Tournament Engine + Scoring Engine)
Engagement depth logs (drill completion, depth)  (Skill Development Engine)
Mentor interaction records (structured metadata) (AI Mentor System — no content)
Behavioral safety event logs                     (DOJO T10 — cooldown, exit events)
Streak records + streak-score correlation        (Passive Intelligence Agent)
Intelligence radar consistency vectors           (Intelligence Measurement System)
Peer scoring behavior consistency                (Scoring Engine — peer matrix history)
Session timing metadata (hour, day, duration)    (Match Engine — no content)
Prior CSV records                                (Feature Store Agent)
Trust + identity verification signals            (Trust Verification Agent)
Minor flag + guardian linkage                    (Trust Verification Agent)
Learning velocity signals                        (LVMA — feeds pressure_response context)
Collaboration efficiency signals                 (ACEMA — feeds peer_interaction axis)
```

### Output Produced

```
COGNITIVE_STABILITY_VECTOR (CSV)          — 8-axis scored per entity per window
STABILITY_CLASS                           — STABLE | VARIABLE | VOLATILE | CONCERNING | CRITICAL
CHAMPIONSHIP_READINESS_SIGNAL             — READY | CONDITIONAL | NOT_READY
RELIABILITY_SCORE                         — recruiter-facing composite (0–100)
ADAPTIVE_DIFFICULTY_SIGNAL                — structured recommendation for Scenario Engine
WELFARE_CHECK_SIGNAL                      — structured mentor/parent routing (HUMAN-IN-LOOP)
STABILITY_NARRATIVE                       — AI-assisted structured explanation (versioned)
COHORT_STABILITY_BENCHMARK                — entity vs cohort comparison (anonymized)
AUDIT_REFERENCE                           — UUID with full immutable trace
```

### Downstream Agents That Depend on This Agent

```
AI_MENTOR_SYSTEM_AGENT          — Uses WELFARE_CHECK_SIGNAL for mentor check-in routing
CHAMPIONSHIP_SEEDING_AGENT      — Uses CHAMPIONSHIP_READINESS_SIGNAL for bracket decisions
SCORING_ENGINE                  — Uses ADAPTIVE_DIFFICULTY_SIGNAL for real-time modulation
ENTREPRENEURIAL_RISK_MODEL_AGENT— Uses CSV axes in reliability_risk computation
HIRING_MATCH_AGENT              — Uses RELIABILITY_SCORE as recruiter-facing talent signal
TALENT_MARKETPLACE_AGENT        — Includes RELIABILITY_SCORE in verified profile
PARENT_INTELLIGENCE_AGENT       — Uses WELFARE_CHECK_SIGNAL + narrative (minors only)
INSTITUTE_ANALYTICS_AGENT       — Uses cohort CSV for wellbeing analytics
TRUST_VERIFICATION_AGENT        — Uses engagement_stability and temporal anomalies
RANK_UPDATE_AGENT               — Championship readiness signal gates XP multiplier
GOVERNANCE_AGENT                — Receives CRITICAL welfare escalation events
OBSERVABILITY_AGENT             — Receives performance metrics from CSIA
BEHAVIORAL_SAFETY_AGENT         — Uses temporal_anomaly + cooldown compliance signals
```

### Upstream Agents That Feed This Agent

```
SCORING_ENGINE                  — Score timelines, response times, confidence per score
MATCH_ENGINE                    — Session metadata, completion/abandonment records
TOURNAMENT_ENGINE               — Championship match data, pressure conditions
INTELLIGENCE_MEASUREMENT_AGENT  — Radar consistency trends
AI_MENTOR_SYSTEM_AGENT          — Interaction metadata (no content) — frequency, depth
PASSIVE_INTELLIGENCE_AGENT      — Streak data, engagement frequency, idle gaps
FEATURE_STORE_AGENT             — Normalized behavioral features, prior CSV
TRUST_VERIFICATION_AGENT        — Identity verification, minor flag, guardian links
LEARNING_VELOCITY_MODEL_AGENT   — Velocity signals feeding pressure response context
ACEMA                           — Peer interaction consistency from collaboration sessions
BEHAVIORAL_SAFETY_AGENT         — Cooldown events, forced exits, abuse flags
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
    "trigger_event",
    "timestamp_utc",
    "request_id"
  ],

  "optional_fields": [
    "performance_score_timeline",
    "championship_performance_record",
    "session_completion_log",
    "response_time_distribution",
    "engagement_depth_log",
    "mentor_interaction_metadata",
    "behavioral_safety_event_log",
    "streak_correlation_data",
    "intelligence_radar_consistency",
    "peer_scoring_behavior_log",
    "session_timing_metadata",
    "prior_csv_id",
    "lvma_velocity_context",
    "acema_peer_signal",
    "stability_window_days",
    "force_recompute_flag",
    "championship_context_flag",
    "is_minor"
  ],

  "field_definitions": {
    "entity_id"        : "UUID v4",
    "entity_type"      : "enum: STUDENT | PROFESSIONAL | EDUCATOR | TEAM | COHORT",
    "tenant_id"        : "UUID — must exist in tenant registry",
    "domain_track"     : "enum: ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION",
    "trigger_event"    : "enum: SESSION_COMPLETE | CHAMPIONSHIP_PRE_CHECK | ASSESSMENT_COMPLETE | BEHAVIORAL_ANOMALY_DETECTED | SCHEDULED_REFRESH | MENTOR_WELFARE_REQUEST | PARENT_REPORT_REQUEST | INSTITUTE_REPORT_REQUEST | RECRUITER_RELIABILITY_REQUEST | IDLE_GAP_EXCEEDED | COOLDOWN_TRIGGERED",
    "timestamp_utc"    : "ISO 8601",
    "request_id"       : "UUID — unique within 24h rolling window",
    "stability_window_days": "integer [14–180] — default 60 if not specified",
    "is_minor"         : "boolean — if true, maximum welfare protection protocols activated; sourced from Trust Verification Agent"
  },

  "performance_score_timeline_schema": [
    {
      "session_id"           : "UUID",
      "score"                : "float [0.0–1.0]",
      "confidence"           : "float [0.0–1.0]",
      "difficulty_tier"      : "integer [1–10]",
      "session_type"         : "enum: PRACTICE | RANKED | CHAMPIONSHIP | CERTIFICATION | RETENTION_CHECK",
      "completion_status"    : "enum: COMPLETED | ABANDONED | TIMED_OUT | FORCE_EXITED",
      "response_time_avg_ms" : "integer",
      "response_time_std_ms" : "integer",
      "timestamp_utc"        : "ISO 8601"
    }
  ],

  "validation_rules": [
    "entity_id must be UUID v4 — reject malformed",
    "entity_type must be from approved enum — reject unknown types",
    "tenant_id must exist in tenant registry — reject if unknown",
    "domain_track must be one of 5 approved tracks — reject unknown",
    "trigger_event must be from approved enum — reject unknown events",
    "timestamp_utc must be ISO 8601 — reject malformed or future-dated beyond +60s",
    "request_id must be unique within 24h — return cached result for duplicates",
    "stability_window_days must be integer [14–180] — reject out-of-range; default 60",
    "performance_score_timeline: scores must be float [0.0–1.0] — reject out-of-range",
    "performance_score_timeline: difficulty_tier integer [1–10] — reject out-of-range",
    "performance_score_timeline: entries must be chronologically ordered — reject if not",
    "performance_score_timeline: completion_status must be from approved enum",
    "response_time values must be positive integers — reject zero or negative",
    "is_minor field: if true, trigger_event must not be RECRUITER_RELIABILITY_REQUEST — reject",
    "CHAMPIONSHIP_PRE_CHECK trigger: performance_score_timeline mandatory — reject if absent",
    "RECRUITER_RELIABILITY_REQUEST trigger: entity_type must be PROFESSIONAL — reject STUDENT",
    "minimum sessions: entity must have ≥ 5 scored sessions in window else INSUFFICIENT_DATA response",
    "championship_context_flag: must be boolean if present — reject non-boolean",
    "BEHAVIORAL_ANOMALY_DETECTED trigger: behavioral_safety_event_log mandatory — reject if absent"
  ],

  "security_checks": [
    "Validate tenant_id matches actor JWT claims — HALT on cross-tenant attempt",
    "Validate entity_id belongs to calling tenant's namespace",
    "Verify request signed with CSIA-authorized agent-level API key",
    "Rate limit: max 200 CSV requests per tenant per minute",
    "Sanitize all string fields — reject injection patterns",
    "RECRUITER_RELIABILITY_REQUEST: verify requesting actor has RECRUITER or HIRING role",
    "PARENT_REPORT_REQUEST: verify entity_id is linked child of requesting parent — reject unlinked",
    "is_minor = true: block RECRUITER_RELIABILITY_REQUEST at validation layer — reject with 403",
    "WELFARE_CHECK_SIGNAL outputs: verify recipient agent has WELFARE_ROUTING authorization",
    "Welfare data: additional encryption layer — AES-256-GCM with welfare-specific key",
    "Welfare escalation logs: separate audit partition with COMPLIANCE_ADMIN-only access"
  ],

  "domain_checks": [
    "Performance timeline sessions must belong to entity's registered domain_track",
    "Championship data must match entity's registered competition domain",
    "Cross-domain stability data: allowed only with CROSS_DOMAIN_GRANT in tenant config",
    "Peer interaction data: verify peer IDs belong to same tenant"
  ],

  "null_tolerance_policy": {
    "entity_id"                   : "REJECT — mandatory",
    "entity_type"                 : "REJECT — mandatory",
    "tenant_id"                   : "REJECT — mandatory",
    "domain_track"                : "REJECT — mandatory",
    "trigger_event"               : "REJECT — mandatory",
    "performance_score_timeline"  : "ALLOW NULL — severe confidence reduction; sets data_sparse_flag = true; min 5 sessions required",
    "championship_performance_record": "ALLOW NULL — pressure_response_score unavailable; flagged in missing_axes",
    "response_time_distribution"  : "ALLOW NULL — decision_quality_variance reduced to score-only signals",
    "engagement_depth_log"        : "ALLOW NULL — engagement_stability scored from session frequency only",
    "mentor_interaction_metadata" : "ALLOW NULL — excluded from metacognitive proxies",
    "behavioral_safety_event_log" : "ALLOW NULL for non-BEHAVIORAL_ANOMALY triggers; REJECT for BEHAVIORAL_ANOMALY trigger",
    "session_timing_metadata"     : "ALLOW NULL — temporal_pattern_anomaly axis unavailable; flagged",
    "is_minor"                    : "DEFAULT false if null — minor flag sourced from Trust Verification Agent independently",
    "stability_window_days"       : "DEFAULT 60 days if null — never reject",
    "prior_csv_id"                : "ALLOW NULL — trajectory delta disabled; first-measurement flag set"
  }
}
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

### Primary Output Object

```json
OUTPUT_SCHEMA: {

  "entity_csv": {
    "entity_id"                     : "UUID",
    "entity_type"                   : "STUDENT | PROFESSIONAL | EDUCATOR | TEAM | COHORT",
    "domain_track"                  : "enum",
    "stability_window_days"         : "integer",
    "is_minor"                      : "boolean",

    "cognitive_stability_vector": {
      "performance_consistency_score"      : "float [0.00–1.00]",
      "pressure_response_score"            : "float [0.00–1.00] — higher = better under pressure",
      "focus_depth_consistency_score"      : "float [0.00–1.00]",
      "decision_quality_variance_score"    : "float [0.00–1.00] — higher = lower variance (more stable)",
      "recovery_resilience_score"          : "float [0.00–1.00]",
      "engagement_stability_score"         : "float [0.00–1.00]",
      "temporal_pattern_anomaly_score"     : "float [0.00–1.00] — higher = more anomalous",
      "peer_interaction_consistency_score" : "float [0.00–1.00]"
    },

    "stability_composite_score"     : "integer [0–1000]",
    "stability_class"               : "enum: STABLE | VARIABLE | VOLATILE | CONCERNING | CRITICAL",
    "confidence_score"              : "float [0.00–1.00]",
    "missing_axes"                  : ["list of axes excluded due to absent input"],
    "data_sparse_flag"              : "boolean",
    "computation_path"              : "enum: FULL | PARTIAL | CACHED | DEGRADED | INSUFFICIENT_DATA"
  },

  "championship_readiness_signal": {
    "readiness_decision"            : "enum: READY | CONDITIONAL | NOT_READY",
    "readiness_confidence"          : "float [0.00–1.00]",
    "blocking_axes"                 : ["axes that are below championship threshold"],
    "conditional_requirements"      : "string — structured path to CONDITIONAL → READY",
    "pressure_delta"                : "float — performance change from practice to championship conditions"
  },

  "reliability_score": {
    "score"                         : "integer [0–100]",
    "reliability_class"             : "enum: HIGHLY_RELIABLE | RELIABLE | MODERATE | INCONSISTENT | UNPREDICTABLE",
    "score_available_to_recruiter"  : "boolean — false if is_minor OR stability_class = CRITICAL AND welfare_hold = true",
    "recruiter_facing_narrative"    : "string — 1 sentence factual statement (AI-assisted) — no welfare framing"
  },

  "adaptive_difficulty_signal": {
    "recommendation"                : "enum: INCREASE_DIFFICULTY | MAINTAIN | REDUCE_DIFFICULTY | PAUSE_COMPETITIVE | ENFORCE_COOLDOWN",
    "target_difficulty_tier"        : "integer [1–10] — null if PAUSE or COOLDOWN",
    "reason_code"                   : "string — structured code",
    "urgency"                       : "enum: NONE | LOW | MEDIUM | HIGH | IMMEDIATE"
  },

  "welfare_check_signal": {
    "welfare_flag_active"           : "boolean",
    "welfare_severity"              : "enum: NONE | MONITOR | CHECK_IN | URGENT | CRITICAL",
    "routing"                       : {
      "mentor_alert"                : "boolean",
      "parent_alert"                : "boolean — always true if is_minor AND welfare_flag_active",
      "educator_alert"              : "boolean",
      "institute_admin_alert"       : "boolean — URGENT and CRITICAL only",
      "governance_escalation"       : "boolean — CRITICAL only"
    },
    "welfare_signal_type"           : "enum: PERFORMANCE_DECLINE | ENGAGEMENT_DROP | PRESSURE_OVERLOAD | TEMPORAL_ANOMALY | BEHAVIORAL_SAFETY | STREAK_GAMING_CONCERN | NONE",
    "welfare_note"                  : "string — structured non-diagnostic observation for human reviewer",
    "human_review_required"         : "boolean — always true for CHECK_IN, URGENT, CRITICAL",
    "automated_action_taken"        : "enum: NONE | COOLDOWN_ENFORCED | SESSION_PAUSED",
    "welfare_data_visible_to"       : ["MENTOR | PARENT | EDUCATOR | INSTITUTE_ADMIN | GOVERNANCE — role list"]
  },

  "stability_narrative": {
    "summary"                       : "string — 2–3 sentences (AI-assisted, versioned prompt)",
    "consistency_strengths"         : ["top 3 positive stability signals"],
    "consistency_concerns"          : ["top 3 signals showing instability — non-diagnostic language only"],
    "performance_framing"           : "string — context for stability class (e.g. 'performing consistently within expected range')",
    "parent_facing_summary"         : "string — simplified, constructive — minors only",
    "educator_facing_note"          : "string — pedagogical context for stability signals",
    "recruiter_reliability_note"    : "string — factual, 1 sentence — PROFESSIONAL entity only"
  },

  "cohort_stability_benchmark": {
    "cohort_percentile"             : "integer [0–100] — null if cohort_benchmark absent",
    "cohort_stability_class_distribution": {
      "STABLE"      : "float — % of cohort",
      "VARIABLE"    : "float",
      "VOLATILE"    : "float",
      "CONCERNING"  : "float",
      "CRITICAL"    : "float"
    }
  },

  "model_version"                   : "string — e.g. CSIA-ML-v2.3.0",
  "prompt_version"                  : "string — e.g. CSIA-PROMPT-v1.5.0",
  "audit_reference"                 : "UUID v4",
  "next_trigger_event"              : [
    "CSV_UPDATED_EVENT",
    "WELFARE_CHECK_ROUTED (conditional)",
    "CHAMPIONSHIP_GATE_SIGNAL_EMITTED (conditional)",
    "ADAPTIVE_DIFFICULTY_EMITTED",
    "RELIABILITY_SCORE_UPDATED (conditional)"
  ],
  "timestamp_utc"                   : "ISO 8601"
}
```

### Stability Class Thresholds (Immutable)

```
stability_composite_score mapping:
  800–1000  → STABILITY_CLASS = STABLE       → reliability_class = HIGHLY_RELIABLE or RELIABLE
  600–799   → STABILITY_CLASS = VARIABLE     → reliability_class = RELIABLE or MODERATE
  400–599   → STABILITY_CLASS = VOLATILE     → reliability_class = MODERATE or INCONSISTENT
  200–399   → STABILITY_CLASS = CONCERNING   → reliability_class = INCONSISTENT
  0–199     → STABILITY_CLASS = CRITICAL     → reliability_class = UNPREDICTABLE
                                               → welfare_flag_active = true
                                               → governance_escalation = true

Override rules:
  IF temporal_pattern_anomaly_score >= 0.80
                        → welfare_flag_active = true regardless of composite
                          welfare_severity = CHECK_IN minimum
  IF engagement_stability_score < 0.20 for > 14 days
                        → welfare_severity minimum = URGENT
  IF championship_context_flag = true AND pressure_response_score < 0.35
                        → championship_readiness = NOT_READY (hard block)
  IF behavioral_safety_event_log contains FORCE_EXIT or HARASSMENT events
                        → welfare_severity = URGENT minimum
                          automated_action_taken = COOLDOWN_ENFORCED
  IF confidence_score < 0.45
                        → stability_class demoted one level more conservative
  IF is_minor = true AND welfare_flag_active = true
                        → parent_alert = true (unconditional, no override)
                          recruiter_score_available = false (unconditional)
  IF is_minor = true AND welfare_severity >= URGENT
                        → governance_escalation = true
                          institute_admin_alert = true

Championship Readiness Thresholds:
  READY:
    performance_consistency_score  ≥ 0.65
    pressure_response_score        ≥ 0.55
    recovery_resilience_score      ≥ 0.55
    engagement_stability_score     ≥ 0.60
    stability_class                ∈ {STABLE, VARIABLE}
    welfare_flag_active            = false

  CONDITIONAL:
    All READY thresholds not fully met but:
    pressure_response_score        ≥ 0.40
    stability_class                ∈ {STABLE, VARIABLE, VOLATILE}
    welfare_severity               ∈ {NONE, MONITOR}
    Requires: mentor_confirmation_flag = true before participation

  NOT_READY:
    pressure_response_score        < 0.35
    OR stability_class             ∈ {CONCERNING, CRITICAL}
    OR welfare_flag_active         = true with severity ≥ CHECK_IN
    OR is_minor with URGENT welfare
```

---

## 5️⃣ ML / AI LOGIC LAYER

### ML Layer — 74% of Computation

```yaml
MODEL_ARCHITECTURE: Multi-Stage Behavioral Signal Pipeline

STAGE_1 — TEMPORAL_SIGNAL_NORMALIZER:
  MODEL_TYPE: Statistical preprocessing pipeline
  PURPOSE: >
    Transform raw performance timelines into normalized, difficulty-adjusted,
    session-type-stratified signal streams. Separates PRACTICE from RANKED
    from CHAMPIONSHIP performance to enable valid cross-condition comparison.
    Applies context normalization — a score of 0.70 on difficulty tier 8
    in a championship is not equivalent to 0.70 in a practice drill.
  KEY_OPERATIONS:
    - Difficulty normalization per session score
    - Session-type stratification (practice vs ranked vs championship)
    - Response time z-scoring per difficulty tier
    - Temporal windowing (rolling 14d, 30d, 60d, 90d per axis)
    - Outlier detection — single anomalous session flagged, not used to anchor scores
    - Incomplete session imputation policy:
        ABANDONED → treated as low-score signal (not discarded)
        TIMED_OUT → partial credit scoring (difficulty-normalized)
        FORCE_EXITED → behavioral safety event flagged separately

STAGE_2 — AXIS_CONSISTENCY_SCORERS (8 independent models):
  MODEL_TYPE: Gradient Boosted Regressor (XGBoost) per axis
  CALIBRATION: Platt scaling per axis
  INTERPRETABILITY: SHAP values computed per prediction — stored in audit
  AXIS_MODEL_DETAILS:

    PERFORMANCE_CONSISTENCY_MODEL:
      Features: score_coefficient_of_variation, score_range_contraction,
                interquartile_range_per_difficulty, session_streak_score_delta,
                score_vs_own_historical_mean_deviation, rank_stability_30d
      Logic: High CV = high instability. Model also checks that HIGH scores
             are not explained solely by easy content selection.

    PRESSURE_RESPONSE_MODEL:
      Features: championship_vs_practice_score_delta,
                knockout_round_vs_qualification_delta,
                high_stakes_session_abandonment_rate,
                response_time_degradation_under_stakes,
                score_trend_in_finals_vs_preliminaries,
                peer_observation_effect_on_score
      Logic: Positive delta (better under pressure) = strong positive signal.
             Negative delta + increasing abandonment = pressure_overload indicator.

    FOCUS_DEPTH_CONSISTENCY_MODEL:
      Features: drill_completion_rate_variance,
                session_depth_engagement_score_variance,
                early_exit_rate_per_session_type,
                question_skip_rate_per_difficulty,
                time_per_question_consistency,
                partial_completion_clustering
      Logic: High variance in drill depth = focus inconsistency. Not the same
             as low performance — an entity can be consistently shallow OR
             consistently deep. Stability, not depth, is measured here.

    DECISION_QUALITY_VARIANCE_MODEL:
      Features: response_time_to_accuracy_correlation,
                rapid_guessing_event_rate,
                answer_change_reversal_rate,
                decision_pattern_shift_between_sessions,
                difficulty_tier_accuracy_consistency,
                structured_thinking_pattern_stability
      Logic: High reversal rate + rapid guessing = decision instability.
             Consistent methodical patterns = high stability regardless of absolute score.

    RECOVERY_RESILIENCE_MODEL:
      Features: post_setback_session_score_recovery_rate,
                sessions_to_return_to_baseline_after_low_score,
                streak_restart_velocity,
                performance_after_criticism_event,
                post_cooldown_session_quality,
                belt_demotion_response_score
      Logic: Measures time and quality of recovery. Fast recovery = high resilience.
             Prolonged depression after single failure = low resilience.

    ENGAGEMENT_STABILITY_MODEL:
      Features: session_frequency_coefficient_of_variation,
                active_days_per_week_consistency,
                idle_gap_frequency_30d,
                mentor_interaction_frequency_trend,
                platform_return_rate_after_gaps,
                streak_gaming_vs_genuine_engagement_index
      Logic: Streak gaming detection is critical — consistent daily 30-second
             logins to maintain streak ≠ genuine engagement. Both are measured.

    TEMPORAL_PATTERN_ANOMALY_MODEL:
      MODEL_TYPE: Isolation Forest + DBSCAN (anomaly detection)
      Features: session_hour_of_day_entropy,
                session_duration_anomaly_score,
                late_night_session_frequency_spike,
                weekend_vs_weekday_engagement_ratio_shift,
                sudden_duration_collapse (sessions < 5 min frequently),
                burst_session_pattern (many sessions in very short time),
                complete_engagement_cessation_flag
      Logic: Unusual temporal patterns may indicate stress, life disruption, or
             gaming behavior. Flagged for human review — not acted on automatically
             except COOLDOWN_ENFORCED for extreme burst patterns.
      WELFARE_SENSITIVITY: HIGH — temporal anomalies route to welfare check signal

    PEER_INTERACTION_CONSISTENCY_MODEL:
      Features: peer_score_giving_variance_across_sessions,
                collaboration_depth_consistency (from ACEMA),
                peer_conflict_event_frequency,
                reciprocal_score_inflation_flag,
                peer_group_composition_change_impact,
                social_withdrawal_signal
      Logic: Sudden change in peer interaction patterns can be behavioral signal.
             Social withdrawal from previously active collaborator is flagged.

STAGE_3 — STABILITY_COMPOSITOR:
  MODEL_TYPE: Stacking Ensemble (Ridge meta-learner with domain-specific weights)
  PURPOSE: >
    Combine 8 axis scores into stability_composite_score. Learns non-linear
    interactions — e.g. high performance_consistency but very low engagement_stability
    is a compound risk signal, not just two independent scores.
  DOMAIN_WEIGHT_MATRICES: Per domain (5 matrices) — IMMUTABLE between MINOR versions

STAGE_4 — PRESSURE_RESPONSE_FORECASTER:
  MODEL_TYPE: Logistic Regression + Bayesian Update
  PURPOSE: >
    Predict how entity will perform under next championship condition
    given current stability pattern. Outputs probability distribution
    with 80% credible interval.
  OUTPUT: championship_readiness_confidence + predicted_pressure_delta

STAGE_5 — WELFARE_SIGNAL_CLASSIFIER:
  MODEL_TYPE: Gradient Boosted Classifier (conservative recall bias)
  PURPOSE: >
    Classify welfare_severity from combined behavioral signals.
    HIGH RECALL priority — false positive welfare check-in preferred
    over false negative (missing a genuine welfare concern is worse than
    an unnecessary check-in).
  RECALL_TARGET: >= 0.90 on URGENT and CRITICAL classes
  PRECISION_FLOOR: >= 0.70 on MONITOR class (avoid alert fatigue)
  WELFARE_SENSITIVITY_NOTE: >
    Model output is advisory only. All MONITOR-and-above classifications
    route to human reviewer. No automated welfare action except
    COOLDOWN_ENFORCED and SESSION_PAUSED (immediate safety only).

STAGE_6 — RELIABILITY_SCORE_CALIBRATOR:
  MODEL_TYPE: Platt-calibrated weighted composite
  PURPOSE: >
    Convert stability_composite_score + pressure_response_score +
    recovery_resilience_score into a recruiter-facing RELIABILITY_SCORE [0–100].
    Calibrated so that score of 80+ corresponds to demonstrated sustained
    consistent performance validated against hiring outcome data
    (longitudinal validation per DOJO T13 framework).
  LONGITUDINAL_VALIDATION: Quarterly correlation with employer feedback signals
  MINIMUM_VALIDATION_COHORT: 500 hired candidates per domain per quarter
```

### Complete Feature Registry (68 features)

```yaml
FEATURES_USED:

  Performance Consistency Features:
    - score_coefficient_of_variation_30d
    - score_interquartile_range_per_difficulty_tier
    - score_vs_own_historical_mean_deviation
    - score_range_contraction_rate_90d
    - rank_stability_30d
    - session_streak_score_delta                    # does streak continuation improve or not affect score?
    - high_score_difficulty_tier_correlation        # are high scores only at low difficulty?
    - performance_consistency_trend_direction       # improving, stable, or degrading consistency

  Pressure Response Features:
    - championship_vs_practice_score_delta
    - knockout_round_vs_qualification_score_delta
    - high_stakes_session_abandonment_rate
    - response_time_degradation_championship_pct
    - score_trend_finals_vs_preliminaries
    - peer_spectator_effect_on_score                # does being watched help or hurt?
    - time_pressure_score_delta                     # performance in timed vs untimed sessions
    - championship_attempt_count_before_success     # indirect pressure tolerance proxy

  Focus Depth Consistency Features:
    - drill_completion_rate_variance_30d
    - session_depth_score_coefficient_of_variation
    - early_exit_rate_per_session_type
    - question_skip_rate_per_difficulty_tier
    - time_per_question_std_dev
    - partial_completion_clustering_score           # are abandonments random or clustered?
    - content_revisit_pattern_depth                 # surface vs deep revisit behavior

  Decision Quality Variance Features:
    - response_time_to_accuracy_correlation
    - rapid_guessing_event_rate                     # responses < 3 seconds regardless of difficulty
    - answer_change_reversal_rate                   # second-guessing frequency
    - decision_pattern_shift_session_over_session
    - difficulty_tier_accuracy_consistency_score
    - structured_thinking_sequence_consistency      # consistent problem-solving approach

  Recovery Resilience Features:
    - post_low_score_session_recovery_rate          # next session score after below-mean session
    - sessions_to_baseline_after_setback
    - streak_restart_velocity                       # days to restart after streak break
    - performance_post_criticism_event              # score quality after mentor critical feedback
    - post_cooldown_first_session_quality
    - belt_demotion_response_trajectory             # how entity responds to belt challenge

  Engagement Stability Features:
    - session_frequency_coefficient_of_variation_30d
    - active_days_per_week_consistency_score
    - idle_gap_frequency_count_30d
    - mentor_interaction_frequency_trend
    - platform_return_rate_after_gap
    - streak_gaming_index                           # genuine vs performative streak behavior
    - content_diversity_engagement_score            # breadth of engagement types

  Temporal Pattern Anomaly Features:
    - session_hour_of_day_entropy                   # unusual concentration in specific hours
    - session_duration_anomaly_z_score
    - late_night_session_frequency_change_rate      # spike in late sessions
    - weekend_vs_weekday_ratio_shift_30d
    - micro_session_burst_count                     # many < 5-minute sessions in short window
    - complete_cessation_duration_days              # longest consecutive zero-activity period
    - session_start_time_std_dev                    # regularity of practice timing

  Peer Interaction Consistency Features:
    - peer_score_giving_coefficient_of_variation
    - collaboration_depth_consistency_from_acema
    - peer_conflict_event_frequency_30d
    - reciprocal_inflation_pair_flag
    - peer_group_change_impact_on_performance       # new group = performance drop/increase
    - social_withdrawal_velocity                    # rate of peer interaction decrease

TRAINING_FREQUENCY:
  FULL_RETRAIN              : Monthly
  INCREMENTAL_UPDATE        : Weekly (Stage 2 axis scorers)
  WELFARE_CLASSIFIER        : Bi-weekly (higher frequency due to welfare sensitivity)
  ANOMALY_TEMPORAL_DETECTOR : Continuous streaming updates
  RELIABILITY_CALIBRATOR    : Quarterly (requires employer feedback batch)
  PRESSURE_FORECASTER       : Monthly (requires championship outcome data)

DRIFT_DETECTION:
  FEATURE_DRIFT:
    Method: PSI per feature — weekly
    Threshold: PSI > 0.20 → MODEL_REVIEW_EVENT
  WELFARE_CLASSIFIER_RECALL:
    Method: Recall on labeled welfare events — bi-weekly
    Threshold: Recall < 0.88 on URGENT/CRITICAL → IMMEDIATE model review
  RELIABILITY_CALIBRATION:
    Method: Coverage probability vs employer outcome correlation — quarterly
    Threshold: < 0.70 correlation → RELIABILITY_CALIBRATOR retraining triggered
  PRESSURE_FORECASTING_CALIBRATION:
    Method: Coverage probability check vs championship outcomes — monthly
    Threshold: < 65% → PRESSURE_FORECASTER recalibration

VERSION_CONTROL:
  All model artifacts in Model Registry (append-only, encrypted)
  model_version: CSIA-ML-vMAJOR.MINOR.PATCH
  Welfare classifier versioned separately: CSIA-WELFARE-ML-vX.Y.Z
  Domain weight matrices: CSIA-WEIGHTS-{DOMAIN}-vX.Y
  Shadow mode: 21 days minimum before promotion
  Rollback: Prior version restorable within 15 minutes
  Ethics board review: mandatory for MAJOR version bumps
```

### AI / LLM Layer — 26% — Narrative, Context, Role-Specific Communication

```yaml
AI_USAGE_SCOPE:
  PERMITTED:
    - Generate stability_narrative.summary (max 3 sentences — non-diagnostic, behavioral only)
    - Generate stability_narrative.consistency_strengths (max 3 items)
    - Generate stability_narrative.consistency_concerns (max 3 items — non-diagnostic language enforced)
    - Generate stability_narrative.performance_framing (1 sentence — contextualizing class)
    - Generate stability_narrative.parent_facing_summary (minors only — constructive, hopeful, age-appropriate)
    - Generate stability_narrative.educator_facing_note (pedagogical framing — max 3 sentences)
    - Generate stability_narrative.recruiter_reliability_note (PROFESSIONAL only — 1 factual sentence)
    - Generate welfare_check_signal.welfare_note (structured non-diagnostic observation — max 3 sentences)
    - Generate championship_readiness_signal.conditional_requirements (structured path description)

  STRICTLY FORBIDDEN:
    - AI must NOT use any clinical, psychological, or diagnostic language
    - AI must NOT reference mental health conditions, disorders, or syndromes
    - AI must NOT suggest the entity "needs help" or "is struggling emotionally"
    - AI must NOT compute any CSV axis score
    - AI must NOT determine stability_class
    - AI must NOT set welfare_severity
    - AI must NOT make championship_readiness decisions
    - AI must NOT generate motivational or therapeutic content
    - AI must NOT access raw performance data or PII
    - AI must NOT override welfare routing decisions
    - AI must NOT produce deficit-framing narratives for minors

FORBIDDEN_LANGUAGE_LIST (enforced via output validation):
  Forbidden terms (must not appear in any output string):
    - "anxiety", "depression", "disorder", "mental health", "psychological",
    - "burnout" (clinical framing), "trauma", "stress disorder", "cognitive decline",
    - "emotional instability", "mental breakdown", "struggling emotionally",
    - "needs therapy", "clinical intervention", "at-risk mentally"
  Replacement framing examples (enforced by prompt template):
    - Instead of "showing anxiety signs" → "performance consistency below baseline"
    - Instead of "burnout detected" → "engagement pattern shift detected"
    - Instead of "struggling emotionally" → "recent performance variance warrants a check-in"

PROMPT_GOVERNANCE:
  PROMPT_REGISTRY       : All prompts versioned — CSIA-PROMPT-vMAJOR.MINOR.PATCH
  PROMPT_STRUCTURE      : Deterministic template with variable injection only
  INPUT_TO_LLM          : Structured JSON: stability_class + axis scores + welfare_severity
                          + top SHAP feature names + entity_type + is_minor
                          NO raw performance data | NO PII | NO welfare narrative content
  OUTPUT_VALIDATION     : Schema + forbidden language scan before use
  FORBIDDEN_LANGUAGE_SCAN: Automated regex scan on all AI output before passing downstream
                           Any forbidden term detected → fallback to static template
  FALLBACK              : Static templates per stability_class + is_minor + entity_type
  LATENCY_BUDGET        : LLM max 2,500ms → timeout triggers static fallback

PROMPT_VARIANTS:
  CSIA-PROMPT-GENERAL-vX.Y          → Standard performance framing
  CSIA-PROMPT-MINOR-vX.Y            → Child-appropriate, constructive framing
  CSIA-PROMPT-PARENT-vX.Y           → Warm, informative, non-alarming parent framing
  CSIA-PROMPT-EDUCATOR-vX.Y         → Pedagogical, context-aware educator framing
  CSIA-PROMPT-RECRUITER-vX.Y        → Factual, performance-only recruiter framing
  CSIA-PROMPT-WELFARE-vX.Y          → Non-diagnostic welfare note (for human reviewers)
  CSIA-PROMPT-{DOMAIN}-vX.Y         → 5 domain-specific variants (Arts, Commerce, etc.)

AI_ASSIST_RULE:
  AI narrates observable behavioral patterns detected by ML.
  AI does NOT interpret those patterns as psychological states.
  AI does NOT suggest causation for instability signals.
  All welfare routing decisions are made by ML + human reviewer — not AI.
```

---

## 6️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS:
  Steady State              : 7,000 requests/second
  Peak (championship season): 35,000 requests/second
  Peak (scheduled refresh)  : 20,000 requests/second

LATENCY_TARGET:
  p50                       : < 200ms
  p95                       : < 650ms
  p99                       : < 1,500ms
  CHAMPIONSHIP_PRE_CHECK (P0): < 350ms enforced SLA
  WELFARE_SIGNAL routing    : < 500ms — welfare alerts must not be delayed
  BEHAVIORAL_ANOMALY trigger: < 300ms — safety-first response

MAX_CONCURRENCY: 70,000 simultaneous entity CSV computations

QUEUE_STRATEGY:
  LANE_P0 (CRITICAL — Direct Compute, no queue):
    - CHAMPIONSHIP_PRE_CHECK trigger (seeding gate)
    - BEHAVIORAL_ANOMALY_DETECTED trigger (safety priority)
    - COOLDOWN_TRIGGERED trigger
    Max queue depth: 0 — reject with 429 if overloaded

  LANE_P1 (HIGH — Priority Queue):
    - MENTOR_WELFARE_REQUEST
    - ASSESSMENT_COMPLETE (championship prep window)
    - RECRUITER_RELIABILITY_REQUEST (real-time hiring flow)
    Kafka topic: csia.priority.stability
    SLA: process within 4 seconds

  LANE_P2 (NORMAL — Standard Queue):
    - SESSION_COMPLETE (standard sessions)
    - IDLE_GAP_EXCEEDED
    - PARENT_REPORT_REQUEST
    - INSTITUTE_REPORT_REQUEST
    Kafka topic: csia.standard.stability
    SLA: process within 45 seconds

  LANE_P3 (LOW — Batch):
    - SCHEDULED_REFRESH (30d rolling)
    - Cohort stability report generation
    - Reliability score batch recalibration
    Kafka topic: csia.batch.stability
    Processing: Off-peak hours only

SPECIAL_ROUTING — WELFARE_ESCALATION_FAST_PATH:
  IF welfare_severity = URGENT or CRITICAL:
    Bypass all queues — direct execution + direct notification emit
    Welfare alert routed to recipients within 60 seconds of trigger
  This path is ALWAYS active regardless of system load.

STATELESS_EXECUTION:
  No local mutable state between requests
  Stability feature tensors: Redis (TTL = 6h per entity)
  Prior CSV: Redis cache (TTL = 48h — invalidated on new event)
  Welfare state: Redis (TTL = 7d — welfare flags persist longer)
  Championship readiness cache: TTL = 24h (refreshed pre-event)
  Forgetting/recovery curves: TTL = 7d (slow-changing)

IDEMPOTENCY:
  Duplicate request_ids return cached CSV — no recomputation
  Idempotency key: SHA-256(entity_id + trigger_event + timestamp_floor_1h)
  Exception: BEHAVIORAL_ANOMALY trigger — always recomputes, never cached

INFRASTRUCTURE:
  COMPUTE     : Kubernetes autoscaling pods
  MESSAGE_BUS : Kafka (multi-lane + welfare fast path dedicated topic)
  CACHE       : Redis Cluster (with welfare-specific namespace)
  STORAGE     : PostgreSQL append-only (audit + CSV history + welfare logs)
  WELFARE_STORAGE: Separate encrypted PostgreSQL partition — welfare logs isolated
  ML_SERVING  : Python FastAPI sidecar per pod
  TRANSPORT   : Kafka async | gRPC P0 | dedicated welfare WebSocket for real-time alerts
```

---

## 7️⃣ SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  All entity_ids namespaced by tenant_id
  tenant_id validated on every request against JWT claims
  Cross-tenant access: IMMEDIATE HALT + SECURITY_VIOLATION log
  Database RLS at PostgreSQL layer as defense-in-depth

DOMAIN_ISOLATION:
  Performance data validated against entity's domain_track
  Cross-domain analysis: requires CROSS_DOMAIN_GRANT

ROLE_BASED_AUTHORIZATION:
  SYSTEM_AGENTS           : AI_MENTOR_SYSTEM, CHAMPIONSHIP_SEEDING_AGENT,
                            ERMA, HIRING_MATCH_AGENT (authorized system roles)
  MENTOR role             : Own assigned students — welfare signals + CSV summary
  EDUCATOR role           : Assigned students — stability class + educator_facing_note
  INSTITUTE_ADMIN         : Cohort-level data (anonymized) + institution-level trends
  RECRUITER role          : RELIABILITY_SCORE + recruiter_reliability_note ONLY
                            (No raw CSV axes | No welfare signals | No minor data)
  PARENT role             : Own linked children — parent_facing_summary + welfare check routing
  TENANT_ADMIN            : Full scope within own tenant
  COMPLIANCE_ADMIN        : Read-only — welfare partition requires separate key
  STUDENT role            : Own stability_class + consistency_strengths ONLY
                            (No raw axis scores | No welfare severity | No pressure scores)

WELFARE_DATA_ACCESS_CONTROLS:
  Welfare signals classified as SENSITIVE_WELFARE
  Separate encryption key: AES-256-GCM (WELFARE_DATA_KEY — rotated quarterly)
  Welfare audit partition: separate from main audit — COMPLIANCE_ADMIN only
  Welfare signals NEVER visible to RECRUITER role — hard enforcement at API gateway
  Welfare signals for minors: additional access control layer
  Welfare log retention: 10 years (extended from standard 7y — welfare data has regulatory longevity)

MINOR_DATA_PROTECTION:
  is_minor = true triggers maximum protection mode:
    - RECRUITER_RELIABILITY_REQUEST: rejected at API gateway (403) — not just suppressed
    - Welfare signals: PARENT notification unconditional
    - CSV raw axes: not exposed to any external API consumer
    - Talent marketplace visibility: blocked for all CSIA signals
    - GDPR/child data protection: explicit consent model for any data use
    - Data retention for minors: follows applicable jurisdiction rules (COPPA, GDPR-K)

ENCRYPTION:
  All CSV data at rest: AES-256
  Welfare data: AES-256-GCM with separate key
  In transit: TLS 1.3 minimum
  Model artifacts: encrypted in Model Registry
  Audit tables: encrypted + append-only — no delete path

AUDIT_LOGGING:
  Every computation: logged before output returned
  Welfare events: logged to separate encrypted partition
  Welfare log access: logged separately (who accessed welfare data, when)
  All logs: INSERT-ONLY — no UPDATE, no DELETE
  Retention: 7 years standard | 10 years welfare partition
```

---

## 8️⃣ AUDIT & TRACEABILITY

Every execution MUST produce and persist the following record **before** output is returned:

```json
AUDIT_RECORD: {
  "audit_reference"               : "UUID v4 — globally unique, immutable",
  "timestamp_utc"                 : "ISO 8601",
  "actor_id"                      : "requesting agent or user",
  "tenant_id"                     : "tenant namespace",
  "entity_id"                     : "entity evaluated",
  "entity_type"                   : "enum",
  "domain_track"                  : "enum",
  "is_minor"                      : "boolean",
  "trigger_event"                 : "enum",
  "stability_window_days"         : "integer",
  "input_hash"                    : "SHA-256 of normalized input payload",
  "output_hash"                   : "SHA-256 of CSV output payload",
  "model_version"                 : "CSIA-ML-vX.Y.Z",
  "welfare_classifier_version"    : "CSIA-WELFARE-ML-vX.Y.Z",
  "prompt_version"                : "CSIA-PROMPT-vX.Y.Z",
  "computation_path"              : "enum",
  "stability_class"               : "enum",
  "stability_composite_score"     : "integer",
  "confidence_score"              : "float",
  "welfare_flag_active"           : "boolean",
  "welfare_severity"              : "enum",
  "welfare_routing_triggered"     : "boolean",
  "automated_action_taken"        : "enum",
  "championship_readiness"        : "enum — null if not championship trigger",
  "reliability_score_computed"    : "boolean",
  "reliability_score_available"   : "boolean — false if minor or welfare hold",
  "anomaly_flags"                 : ["list of anomalies detected"],
  "missing_axes"                  : ["list of axes excluded"],
  "data_sparse_flag"              : "boolean",
  "shap_top_features"             : ["top 5 SHAP feature names — no values"],
  "forbidden_language_scan_passed": "boolean — AI output scan result",
  "llm_call_made"                 : "boolean",
  "llm_fallback_used"             : "boolean",
  "processing_time_ms"            : "integer",
  "escalation_triggered"          : "boolean",
  "escalated_to"                  : ["list of agents/roles notified"]
}
```

**Welfare Audit Record (separate partition, additional fields):**
```json
WELFARE_AUDIT_RECORD: {
  "welfare_audit_reference"       : "UUID v4 — linked to main audit_reference",
  "welfare_severity"              : "enum",
  "welfare_signal_type"           : "enum",
  "routing_recipients"            : ["roles notified"],
  "human_review_status"           : "enum: PENDING | REVIEWED | RESOLVED | ESCALATED",
  "human_reviewer_id"             : "UUID — populated after human review",
  "resolution_timestamp_utc"      : "ISO 8601 — populated after resolution",
  "automated_action"              : "enum",
  "minor_involved"                : "boolean",
  "welfare_data_access_log"       : [
    {
      "accessor_id"   : "UUID",
      "accessor_role" : "enum",
      "access_time"   : "ISO 8601",
      "access_purpose": "string"
    }
  ]
}
```

**CSV Record Immutability:** All CSV records are append-only. Recomputation creates new record linked via `prior_csv_id`. Prior records never modified or deleted. Full stability history preserved indefinitely.

---

## 9️⃣ FAILURE POLICY

```yaml
FAILURE_SCENARIOS:

  INVALID_INPUT:
    CONDITION: Required field missing, malformed, security check failed
    POLICY: STOP_EXECUTION
    ACTION: Return structured 400-class error with explicit rejection per field
    LOG: Write VALIDATION_FAILURE audit record
    ESCALATE_TO: None
    RETRY: Not applicable

  INSUFFICIENT_DATA:
    CONDITION: entity has < 5 scored sessions in stability_window (not an error)
    POLICY: RETURN_INSUFFICIENT_DATA (structured, not error)
    ACTION: Return computation_path = INSUFFICIENT_DATA
            Include sessions_available and sessions_required
            Return adaptive_difficulty_signal = MAINTAIN (safe default)
            Welfare classifier still runs if behavioral_safety_event_log present
    LOG: Write INSUFFICIENT_DATA informational record
    ESCALATE_TO: None — note to AI_MENTOR to generate initial assessment sessions

  MODEL_UNAVAILABLE:
    CONDITION: ML inference sidecar unresponsive after 3 retries
    POLICY: DEGRADED_MODE
    ACTION: >
      Check Redis cache for prior CSV (< 48h) → return with computation_path = CACHED
      If no cache → return VARIABLE stability_class with confidence = 0.10
      Set computation_path = DEGRADED
      WELFARE EXCEPTION: If trigger was BEHAVIORAL_ANOMALY → route to MENTOR immediately
      regardless of model state (welfare safety overrides degraded mode)
    LOG: Write MODEL_UNAVAILABLE incident
    ESCALATE_TO: OBSERVABILITY_AGENT + ON_CALL_ENGINEER
    RETRY: Exponential backoff: 500ms → 1.5s → 4.5s

  WELFARE_CLASSIFIER_UNAVAILABLE:
    CONDITION: Stage 5 welfare classifier fails
    POLICY: CONSERVATIVE_DEFAULT + HUMAN_ESCALATION
    ACTION: >
      Set welfare_severity = CHECK_IN (conservative default)
      Set human_review_required = true
      Route to MENTOR for human check
      Complete all other computation stages normally
    LOG: Write WELFARE_CLASSIFIER_UNAVAILABLE critical record
    ESCALATE_TO: GOVERNANCE_AGENT + ON_CALL_ENGINEER immediately

  LLM_TIMEOUT or FORBIDDEN_LANGUAGE_DETECTED:
    CONDITION: LLM exceeds 2,500ms OR forbidden language scan fails
    POLICY: PARTIAL_EXECUTION — static template
    ACTION: Return full ML CSV with static template narrative per stability_class
            Set llm_fallback_used = true
    LOG: Write LLM_TIMEOUT or FORBIDDEN_LANGUAGE_DETECTED warning
    ESCALATE_TO: OBSERVABILITY_AGENT (warning) | GOVERNANCE for repeated language failures

  DATA_CORRUPTION:
    CONDITION: Input hash mismatch during processing
    POLICY: STOP_EXECUTION — IMMEDIATE
    ACTION: Discard computation, return 500-class structured error
    LOG: Write DATA_CORRUPTION critical incident
    ESCALATE_TO: GOVERNANCE_AGENT + SECURITY_AGENT immediately
    RETRY: FORBIDDEN

  WELFARE_CRITICAL_DETECTED:
    CONDITION: stability_class = CRITICAL OR welfare_severity = CRITICAL
    POLICY: EXECUTE + FORCE_WELFARE_ESCALATION
    ACTION: >
      Complete computation
      Emit WELFARE_CRITICAL_EVENT — fast path (< 60 seconds)
      Set governance_escalation = true
      If is_minor: institute_admin_alert = true + parent_alert = true
      automated_action_taken = COOLDOWN_ENFORCED if championship_context_flag = true
    LOG: Write WELFARE_CRITICAL_EVENT record (separate partition)
    ESCALATE_TO: GOVERNANCE_AGENT + AI_MENTOR + PARENT (if minor) + EDUCATOR
    RETRY: Not applicable — escalation is final until human resolution

  MINOR_RECRUITER_REQUEST_ATTEMPTED:
    CONDITION: RECRUITER_RELIABILITY_REQUEST trigger AND is_minor = true
    POLICY: IMMEDIATE_REJECT
    ACTION: Return 403 — no details about minor's data disclosed
    LOG: Write MINOR_PROTECTION_VIOLATION record (critical — potential data misuse)
    ESCALATE_TO: SECURITY_AGENT + GOVERNANCE_AGENT + COMPLIANCE_ADMIN
    RETRY: FORBIDDEN — requires investigation

  TENANT_ISOLATION_VIOLATION:
    CONDITION: Cross-tenant access attempt
    POLICY: IMMEDIATE_HALT
    ACTION: Return 403 — zero output
    LOG: Write SECURITY_VIOLATION critical record
    ESCALATE_TO: SECURITY_AGENT + GOVERNANCE_AGENT + PLATFORM_ADMIN
    RETRY: FORBIDDEN

  TEMPORAL_ANOMALY_BURST_DETECTED:
    CONDITION: temporal_pattern_anomaly_score >= 0.85
    POLICY: COMPUTE + WELFARE_ROUTE + SAFETY_ACTION
    ACTION: >
      Complete computation
      Set welfare_severity minimum = CHECK_IN
      If burst_session_pattern confirmed: automated_action = COOLDOWN_ENFORCED
      Route welfare_check_signal immediately to MENTOR
    LOG: Write TEMPORAL_ANOMALY_DETECTED record (welfare partition)
    ESCALATE_TO: AI_MENTOR_SYSTEM_AGENT (immediate welfare routing)

NO_SILENT_FAILURES: Every failure path writes audit. Every welfare condition
                    writes to welfare audit partition. Raw exceptions never exposed.
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  SCORING_ENGINE:
    Provides: Score timelines, response times, confidence, session types, completion status
    Protocol: Kafka stream (csia.scoring.events) + gRPC pull for P0

  MATCH_ENGINE:
    Provides: Session metadata, role assignments, completion/abandonment records
    Protocol: gRPC pull

  TOURNAMENT_ENGINE:
    Provides: Championship vs practice performance records, knockout round data
    Protocol: gRPC pull for CHAMPIONSHIP_PRE_CHECK triggers

  INTELLIGENCE_MEASUREMENT_AGENT:
    Provides: Radar consistency trends, cognitive style stability
    Protocol: gRPC pull

  AI_MENTOR_SYSTEM_AGENT:
    Provides: Interaction metadata (frequency, depth — no content), feedback response signals
    Protocol: Kafka stream (csia.mentor.metadata)

  PASSIVE_INTELLIGENCE_AGENT:
    Provides: Streak data, idle gap events, engagement frequency signals
    Protocol: Kafka stream (csia.passive.signals)

  FEATURE_STORE_AGENT:
    Provides: Normalized behavioral feature vectors, prior CSV references
    Protocol: gRPC pull + Kafka subscription

  TRUST_VERIFICATION_AGENT:
    Provides: Identity verification, is_minor flag, guardian links, fraud signals
    Protocol: gRPC pull — mandatory before welfare signal generation
    CRITICAL: is_minor flag sourced ONLY from Trust Verification Agent — not self-reported

  LEARNING_VELOCITY_MODEL_AGENT (LVMA):
    Provides: velocity_class, regression_risk, plateau signals (pressure_response context)
    Protocol: gRPC pull

  ACEMA:
    Provides: Peer interaction consistency signals, collaboration stability
    Protocol: gRPC pull

  BEHAVIORAL_SAFETY_AGENT:
    Provides: Cooldown events, forced exits, abuse/harassment flag records
    Protocol: Kafka stream (csia.safety.events) — highest priority consumer

DOWNSTREAM_AGENTS:
  AI_MENTOR_SYSTEM_AGENT:
    Consumes: WELFARE_CHECK_SIGNAL, adaptive_difficulty_signal
    Use: Mentor session routing, check-in scheduling, content modulation
    Priority: P0 for welfare routing

  CHAMPIONSHIP_SEEDING_AGENT:
    Consumes: championship_readiness_signal, pressure_response_score
    Use: Fair bracket construction + participant readiness gating

  SCORING_ENGINE:
    Consumes: adaptive_difficulty_signal
    Use: Real-time scenario difficulty adjustment during session

  ENTREPRENEURIAL_RISK_MODEL_AGENT (ERMA):
    Consumes: performance_consistency_score, pressure_response_score, recovery_resilience_score
    Use: reliability_risk axis computation (bidirectional interface)

  HIRING_MATCH_AGENT:
    Consumes: reliability_score (PROFESSIONAL entities only)
    Use: Candidate reliability signal for recruiter surfacing
    Constraint: Never receives welfare signals or minor data

  TALENT_MARKETPLACE_AGENT:
    Consumes: reliability_score + recruiter_reliability_note (PROFESSIONAL only)
    Use: Verified reliability badge in candidate profile
    Constraint: Welfare data completely excluded from this pathway

  PARENT_INTELLIGENCE_AGENT:
    Consumes: welfare_check_signal (minor entities) + parent_facing_summary
    Use: Parent dashboard alerts, child wellbeing reports

  INSTITUTE_ANALYTICS_AGENT:
    Consumes: Cohort stability distributions (anonymized aggregates)
    Use: Institute wellbeing analytics, cohort-level trends

  TRUST_VERIFICATION_AGENT:
    Consumes: engagement_stability_score, temporal_pattern_anomaly_score
    Use: Trust score computation + fraud signal enrichment

  RANK_UPDATE_AGENT:
    Consumes: Championship readiness signal (gates XP multiplier for championship)
    Use: NOT_READY blocks championship XP bonus

  GOVERNANCE_AGENT:
    Consumes: WELFARE_CRITICAL events, minor protection violations, model drift alerts
    Use: Compliance review + escalation management

  OBSERVABILITY_AGENT:
    Consumes: All performance metrics, welfare classifier drift, anomaly rates
    Use: CSIA health dashboards

EVENT_TRIGGERS:

  INPUT_TRIGGERS:
    SESSION_COMPLETE                → Standard CSV update (P2)
    CHAMPIONSHIP_PRE_CHECK          → Full CSV + readiness gate (P0)
    ASSESSMENT_COMPLETE             → Partial CSV update for performance axes (P1/P2)
    BEHAVIORAL_ANOMALY_DETECTED     → Full CSV + immediate welfare route (P0)
    SCHEDULED_REFRESH               → 30d rolling full recompute (P3)
    MENTOR_WELFARE_REQUEST          → Full CSV with welfare focus (P1)
    PARENT_REPORT_REQUEST           → Full CSV with parent_facing_summary (P2)
    INSTITUTE_REPORT_REQUEST        → Cohort CSV aggregation (P2/P3)
    RECRUITER_RELIABILITY_REQUEST   → Full CSV for PROFESSIONAL only (P1)
    IDLE_GAP_EXCEEDED               → Engagement + temporal anomaly recompute (P2)
    COOLDOWN_TRIGGERED              → Temporal anomaly + welfare check (P0)

  OUTPUT_EVENTS:
    CSV_UPDATED_EVENT               → Kafka (csia.csv.results)
    STABILITY_CLASS_CHANGED         → Emitted when class transitions
    WELFARE_CHECK_ROUTED_EVENT      → Emitted to welfare routing recipients
    WELFARE_CRITICAL_EVENT          → Emitted to GOVERNANCE + MENTOR + PARENT
    CHAMPIONSHIP_GATE_SIGNAL_EVENT  → Emitted to CHAMPIONSHIP_SEEDING_AGENT
    RELIABILITY_SCORE_UPDATED_EVENT → Emitted to HIRING_MATCH_AGENT
    ADAPTIVE_DIFFICULTY_EMITTED     → Emitted to SCORING_ENGINE
    COOLDOWN_ENFORCE_EVENT          → Emitted to BEHAVIORAL_SAFETY_AGENT
    CSIA_MODEL_DRIFT_ALERT          → Emitted to OBSERVABILITY_AGENT
    MINOR_STABILITY_ALERT_EVENT     → Emitted to PARENT_INTELLIGENCE_AGENT
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

```json
EMIT_FEATURE_VECTOR_TO_FEATURE_STORE: [
  { "feature_name": "cognitive_stability_composite",          "type": "float [0.00–1.00]" },
  { "feature_name": "performance_consistency_score",          "type": "float [0.00–1.00]" },
  { "feature_name": "pressure_response_score",                "type": "float [0.00–1.00]" },
  { "feature_name": "focus_depth_consistency_score",          "type": "float [0.00–1.00]" },
  { "feature_name": "decision_quality_variance_score",        "type": "float [0.00–1.00]" },
  { "feature_name": "recovery_resilience_score",              "type": "float [0.00–1.00]" },
  { "feature_name": "engagement_stability_score",             "type": "float [0.00–1.00]" },
  { "feature_name": "temporal_pattern_anomaly_score",         "type": "float [0.00–1.00]" },
  { "feature_name": "peer_interaction_consistency_score",     "type": "float [0.00–1.00]" },
  { "feature_name": "stability_class_encoded",                "type": "integer [0–4]" },
  { "feature_name": "reliability_score",                      "type": "integer [0–100]" },
  { "feature_name": "championship_readiness_encoded",         "type": "integer [0–2]" },
  { "feature_name": "welfare_flag_active",                    "type": "boolean encoded 0/1" },
  { "feature_name": "pressure_delta",                         "type": "float signed" },
  { "feature_name": "recovery_velocity_days",                 "type": "integer" }
]
```

**Welfare features are NOT emitted to the Feature Store.** Welfare signals are isolated in the welfare partition and routed only through authorized welfare channels. No welfare data enters the general feature pipeline.

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY

CSIA does not directly manage idea vectors. However, stability signals contribute to innovation context:

```json
EMIT_TO_IDEA_DNA_AGENT (conditional — PROFESSIONAL entity type only):
  {
    "entity_id"                         : "UUID",
    "cognitive_stability_composite"     : "float — stability at time of idea submission",
    "decision_quality_variance_score"   : "float — decision quality proxy for idea clarity",
    "focus_depth_consistency_score"     : "float — sustained focus proxy for idea depth",
    "stability_class"                   : "enum",
    "source_agent"                      : "COGNITIVE_STABILITY_INDEX_AGENT",
    "audit_reference"                   : "UUID"
  }
```

**Use:** IDEA_DNA_AGENT uses cognitive stability context when scoring idea quality — a submission made during VOLATILE or CRITICAL stability class may have reduced originality confidence (the idea may be reactive or impulsive rather than deliberate). This is advisory only — IDEA_DNA_AGENT makes all originality decisions.

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

```yaml
GROWTH_ENGINE_TRIGGERS:

  STABILITY_CLASS_UPGRADE:
    CONDITION: stability_class transitions from VOLATILE/CONCERNING → VARIABLE or STABLE
    EMIT: XP_UPDATE_EVENT
      payload:
        entity_id: UUID
        xp_action: "STABILITY_RECOVERY_ACHIEVED"
        prior_class: enum
        new_class: enum
        audit_reference: UUID
    RECIPIENT: RANK_UPDATE_AGENT

  CHAMPIONSHIP_READINESS_ACHIEVED:
    CONDITION: championship_readiness_signal transitions to READY
    EMIT: XP_UPDATE_EVENT + optional SHARE_TRIGGER_EVENT
      payload:
        entity_id: UUID
        xp_action: "CHAMPIONSHIP_READY_STATUS_ACHIEVED"
        readiness_confidence: float
        audit_reference: UUID
    RECIPIENT: RANK_UPDATE_AGENT + CHAMPIONSHIP_SEEDING_AGENT

  PRESSURE_EXCELLENCE:
    CONDITION: pressure_response_score >= 0.80 sustained over 30d
    EMIT: XP_UPDATE_EVENT
      payload:
        entity_id: UUID
        xp_action: "PRESSURE_PERFORMANCE_EXCELLENCE"
        pressure_response_score: float
        audit_reference: UUID
    RECIPIENT: RANK_UPDATE_AGENT

  RECOVERY_RESILIENCE_MILESTONE:
    CONDITION: recovery_resilience_score crosses 0.75 for first time
    EMIT: SHARE_TRIGGER_EVENT
      payload:
        entity_id: UUID
        share_type: "RESILIENCE_MILESTONE"
        stability_class: enum
        audit_reference: UUID
    RECIPIENT: SOCIAL_ENGINE (generates shareable resilience achievement card)

GROWTH_ENGINE_RESTRICTIONS:
  Welfare-related events MUST NOT trigger share events or public achievements
  Stability CONCERNING or CRITICAL: XP bonuses suppressed (not penalized — just not boosted)
  Growth events for minors: parent notification triggered alongside any public share
  NOT-READY championship signal: XP championship multiplier suppressed

THIS AGENT DOES NOT:
  - Compute XP values directly — RANK_UPDATE_AGENT
  - Create share cards — SOCIAL_ENGINE
  - Award badges — ACHIEVEMENT_ENGINE
  - Suppress XP for instability — no penalty framing; suppression is silence not punishment
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

```yaml
METRICS_EMITTED_TO_OBSERVABILITY_AGENT:

  Success Rate:
    metric: csia_request_success_rate
    alert_threshold: < 98.5% → CRITICAL

  Error Rate:
    metric: csia_request_error_rate
    alert_threshold: > 1.5% → WARNING | > 5.0% → CRITICAL

  Latency:
    metric: csia_computation_latency_ms (p50, p95, p99)
    alert: p95 > 750ms → WARNING | p99 > 1,800ms → CRITICAL

  Welfare Fast Path Latency:
    metric: csia_welfare_routing_latency_ms
    alert: > 60 seconds end-to-end welfare alert → CRITICAL (safety SLA breach)

  Welfare Classifier Recall:
    metric: csia_welfare_recall_urgent_critical
    target: ≥ 0.90
    alert: < 0.88 → immediate WELFARE_CLASSIFIER_REVIEW

  Welfare Alert Rate:
    metric: csia_welfare_alerts_per_1000_entities
    alert: Spike > 5x rolling baseline → GOVERNANCE investigation
    alert: Sustained HIGH rate → curriculum/platform stress audit

  Minor Protection Violation Rate:
    metric: csia_minor_protection_violations
    target: 0 (any violation is critical)
    alert: Any violation → SECURITY + GOVERNANCE + COMPLIANCE immediate

  Championship NOT_READY Rate:
    metric: csia_championship_not_ready_rate
    monitor: High rate → flag for championship season preparation quality review

  Reliability Score Distribution:
    metric: csia_reliability_score_distribution
    monitor: Ensure calibration aligns with employer outcome correlation (quarterly)

  Feature Drift PSI:
    metric: csia_psi_per_feature
    alert: PSI > 0.20 → MODEL_REVIEW

  Forbidden Language Scan Results:
    metric: csia_forbidden_language_detection_rate
    target: 0 detections
    alert: Any detection → GOVERNANCE + immediate prompt review

  Temporal Anomaly Detection Rate:
    metric: csia_temporal_anomaly_rate
    monitor: Trend tracking — sudden spikes may indicate platform-level stress event

  Welfare Human Review Resolution Time:
    metric: csia_welfare_review_resolution_hours
    target: < 24h for CHECK_IN | < 4h for URGENT | < 1h for CRITICAL
    alert: Breach of target → GOVERNANCE + ON_CALL

REQUIRED_DASHBOARDS:
  - CSIA Real-time Performance Dashboard
  - CSIA Stability Class Distribution (by domain, by tenant — anonymized)
  - CSIA Welfare Alert Dashboard (trend + resolution status)
  - CSIA Minor Protection Dashboard (violation rate — always visible to COMPLIANCE)
  - CSIA Welfare Classifier Health Dashboard
  - CSIA Championship Readiness Rate Dashboard
  - CSIA Reliability Score Calibration Dashboard
  - CSIA Forbidden Language Detection Dashboard
  - CSIA Queue Health Dashboard (all lanes + welfare fast path)
```

---

## 1️⃣5️⃣ VERSIONING POLICY

```yaml
VERSION_FORMAT: MAJOR.MINOR.PATCH
  MAJOR: Output schema change (fields removed/renamed), stability_class threshold
         changed, welfare routing logic changed, minor protection policy changed,
         reliability score calibration methodology changed
  MINOR: New axis added, new prompt variant, new domain weight matrix,
         new entity_type supported, welfare classifier retrained
  PATCH: Bug fix, calibration adjustment, prompt refinement,
         forbidden language list updated

VERSIONED_ARTIFACTS:
  CSIA-ML-vX.Y.Z                     → Core ML ensemble
  CSIA-WELFARE-ML-vX.Y.Z             → Welfare classifier (separately versioned)
  CSIA-PROMPT-GENERAL-vX.Y           → General narrative prompt
  CSIA-PROMPT-MINOR-vX.Y             → Minor-specific prompt
  CSIA-PROMPT-PARENT-vX.Y            → Parent-facing prompt
  CSIA-PROMPT-EDUCATOR-vX.Y          → Educator-facing prompt
  CSIA-PROMPT-RECRUITER-vX.Y         → Recruiter-facing prompt
  CSIA-PROMPT-WELFARE-vX.Y           → Welfare note prompt
  CSIA-PROMPT-{DOMAIN}-vX.Y          → 5 domain-specific prompts
  CSIA-FEATURES-vX.Y                 → Feature schema
  CSIA-WEIGHTS-{DOMAIN}-vX.Y         → Domain compositor weight matrices
  CSIA-FORBIDDEN-LANGUAGE-LIST-vX.Y  → Forbidden language registry (separately versioned)

PROMOTION_REQUIREMENTS:
  Standard MINOR/PATCH: 21-day shadow mode + < 5% output variance
  MAJOR version: GOVERNANCE_AGENT approval + ETHICS_BOARD sign-off + 30-day notice
  Welfare classifier changes (any version): Ethics board review
  Forbidden language list changes: Compliance team review + Ethics board sign-off
  Minor protection policy changes: Legal + Ethics board + GOVERNANCE — mandatory

BACKWARD_COMPATIBILITY:
  CSV records retain their computation version — historical records never retroactively changed
  Downstream consumers tolerate new fields
  Welfare logs: version-tagged per record for regulatory traceability
  Deprecation window: 90 days with migration guide
```

---

## 1️⃣6️⃣ NON-NEGOTIABLE RULES

```
This agent MUST NOT:

  ✗  Use clinical, diagnostic, or therapeutic language in any output
  ✗  Diagnose, label, or infer psychological conditions from behavioral signals
  ✗  Take automated welfare action beyond COOLDOWN_ENFORCED and SESSION_PAUSED
  ✗  Surface welfare signals to RECRUITER role — ever, under any condition
  ✗  Process RECRUITER_RELIABILITY_REQUEST for any entity flagged as minor
  ✗  Modify historical CSV records — all records are append-only forever
  ✗  Delete audit logs or welfare logs under any condition
  ✗  Override GOVERNANCE_AGENT or ETHICS_BOARD decisions
  ✗  Mix domain data (ARTS entity scored using TECHNOLOGY stability rubric)
  ✗  Allow AI output to contain forbidden language — scan enforced before use
  ✗  Route welfare signals to unauthorized roles — strict role authorization
  ✗  Use self-reported is_minor flag — must source from TRUST_VERIFICATION_AGENT
  ✗  Generate motivational, therapeutic, or emotional content in narratives
  ✗  Suppress welfare escalation for high-performing entities — stability score
     does NOT reduce welfare protection
  ✗  Use peer observation or spectator data to infer psychological state
  ✗  Communicate directly with frontend — agent-to-agent via service mesh only
  ✗  Allow AI to set welfare_severity, championship_readiness, or reliability_score
  ✗  Process welfare data outside the encrypted welfare partition
  ✗  Share welfare audit records without COMPLIANCE_ADMIN authorization

This agent MUST:

  ✓  Write audit record + welfare audit record BEFORE returning any output
  ✓  Source is_minor flag from TRUST_VERIFICATION_AGENT — never input field
  ✓  Run forbidden language scan on ALL AI output before use
  ✓  Route ALL welfare signals URGENCY+ through human-in-the-loop review
  ✓  Apply parent_alert = true unconditionally for minors with active welfare flag
  ✓  Block recruiter access to minor data at API gateway layer (403)
  ✓  Apply welfare fast-path routing (< 60s) for URGENT and CRITICAL welfare events
  ✓  Enforce welfare hold on RELIABILITY_SCORE when stability_class = CRITICAL
  ✓  Use domain-specific compositor weight matrices per domain_track
  ✓  Compute SHAP values per prediction and store in audit
  ✓  Route BEHAVIORAL_ANOMALY triggers as P0 — never queue
  ✓  Operate statelessly — all context fetched per request
  ✓  Include model_version, welfare_classifier_version, and prompt_version in every output
  ✓  Maintain separate encrypted welfare partition with separate access controls
  ✓  Require ethics board sign-off for any change to welfare routing or minor protection
  ✓  Treat welfare classifier unavailability as a critical incident, not degraded mode
```

---

## 🔐 DOMAIN-SPECIFIC STABILITY PROFILES (LOCKED)

### Arts Domain Stability Signals

```yaml
ARTS_DOMAIN_CSV:
  Compositor weights (CSIA-WEIGHTS-ARTS-vX.Y):
    performance_consistency_score        : 0.18
    pressure_response_score              : 0.12  # arts has lower championship pressure emphasis
    focus_depth_consistency_score        : 0.22  # highest — sustained creative focus critical
    decision_quality_variance_score      : 0.14
    recovery_resilience_score            : 0.16  # important — rejection is part of arts
    engagement_stability_score           : 0.10
    temporal_pattern_anomaly_score       : 0.04  # inverted
    peer_interaction_consistency_score   : 0.04

  Domain-specific positive stability signals:
    - Iterative creative cycle completion consistency
    - Critique integration showing stable improvement patterns
    - Portfolio diversity maintained alongside quality
    - Sustained exploratory engagement (trying new styles/forms)

  Domain-specific risk signals:
    - Sudden stylistic collapse (reverting to earliest patterns only)
    - Peer feedback avoidance spike
    - Submission quality variance > 2 standard deviations
```

### Commerce Domain Stability Signals

```yaml
COMMERCE_DOMAIN_CSV:
  Compositor weights (CSIA-WEIGHTS-COMMERCE-vX.Y):
    performance_consistency_score        : 0.20
    pressure_response_score              : 0.20  # high — commerce = negotiation, pitch stakes
    focus_depth_consistency_score        : 0.14
    decision_quality_variance_score      : 0.18  # high — commercial decisions must be consistent
    recovery_resilience_score            : 0.14
    engagement_stability_score           : 0.08
    temporal_pattern_anomaly_score       : 0.04  # inverted
    peer_interaction_consistency_score   : 0.02

  Domain-specific positive stability signals:
    - Consistent negotiation outcome quality across scenario types
    - Stable decision-making approach under time pressure
    - CRM/Sales tool data aligning with platform stability signals

  Domain-specific risk signals:
    - High score variance across scenario complexity tiers
    - Decision reversal rate above domain baseline in commercial scenarios
    - Abandonment clustering in high-stakes pitch simulations
```

### Science Domain Stability Signals

```yaml
SCIENCE_DOMAIN_CSV:
  Compositor weights (CSIA-WEIGHTS-SCIENCE-vX.Y):
    performance_consistency_score        : 0.20
    pressure_response_score              : 0.16
    focus_depth_consistency_score        : 0.16
    decision_quality_variance_score      : 0.22  # highest — scientific method requires consistency
    recovery_resilience_score            : 0.12
    engagement_stability_score           : 0.08
    temporal_pattern_anomaly_score       : 0.04  # inverted
    peer_interaction_consistency_score   : 0.02

  Domain-specific positive stability signals:
    - Hypothesis formation consistency across sessions
    - Evidence evaluation approach stable over time
    - Lab/simulation problem-solving method reproducibility

  Domain-specific risk signals:
    - Structured thinking breakdown under time pressure
    - Calculation accuracy collapse in timed scenarios
    - Engagement drop in foundational content after advanced content attempted
```

### Technology Domain Stability Signals

```yaml
TECHNOLOGY_DOMAIN_CSV:
  Compositor weights (CSIA-WEIGHTS-TECHNOLOGY-vX.Y):
    performance_consistency_score        : 0.18
    pressure_response_score              : 0.18
    focus_depth_consistency_score        : 0.16
    decision_quality_variance_score      : 0.16
    recovery_resilience_score            : 0.14
    engagement_stability_score           : 0.10
    temporal_pattern_anomaly_score       : 0.06  # higher — tech over-engagement patterns notable
    peer_interaction_consistency_score   : 0.02

  Domain-specific positive stability signals:
    - Code quality consistency across project complexity tiers
    - PR review participation rate stable over time (from EIE)
    - Debugging approach consistency (structured vs random)
    - Sprint delivery consistency in project milestones

  Domain-specific risk signals:
    - Temporal anomaly: burst coding then complete cessation (overwork pattern)
    - Review quality collapse after difficulty spike
    - Sudden technology breadth contraction (comfort zone retreat)
```

### Administration Domain Stability Signals

```yaml
ADMINISTRATION_DOMAIN_CSV:
  Compositor weights (CSIA-WEIGHTS-ADMINISTRATION-vX.Y):
    performance_consistency_score        : 0.22  # highest — compliance requires consistent execution
    pressure_response_score              : 0.16
    focus_depth_consistency_score        : 0.14
    decision_quality_variance_score      : 0.20
    recovery_resilience_score            : 0.12
    engagement_stability_score           : 0.10
    temporal_pattern_anomaly_score       : 0.04  # inverted
    peer_interaction_consistency_score   : 0.02

  Domain-specific positive stability signals:
    - Policy application consistency across simulation types
    - Escalation protocol adherence rate stable
    - Formal process adherence score consistency

  Domain-specific risk signals:
    - Decision quality collapse in novel compliance scenarios
    - Inconsistent application of learned procedures
    - Engagement drop during foundational compliance modules
```

---

## 🔐 WELFARE CHECK-IN ROUTING PROTOCOL (HUMAN-IN-THE-LOOP)

```yaml
WELFARE_ROUTING_PROTOCOL:

  SEVERITY_NONE:
    Action: Standard CSV output — no welfare routing
    Automated action: NONE

  SEVERITY_MONITOR:
    Action: Flag in entity profile for next mentor interaction
    Routing: Mentor note (passive — added to mentor session context)
    Automated action: NONE
    Human review: Not required — mentor may review at own discretion
    Timeline: Flag visible for 14 days, then auto-expires

  SEVERITY_CHECK_IN:
    Action: Active mentor alert — scheduled check-in recommended
    Routing: MENTOR_SYSTEM_AGENT (scheduled check-in signal)
             PARENT_INTELLIGENCE_AGENT (if is_minor — immediate)
    Automated action: NONE
    Human review: REQUIRED — mentor must log outcome within 72 hours
    Welfare note: Non-diagnostic structured observation provided to mentor

  SEVERITY_URGENT:
    Action: Immediate mentor + educator alert
    Routing: MENTOR_SYSTEM_AGENT (immediate — welfare fast path)
             PARENT_INTELLIGENCE_AGENT (all entities, not only minors)
             EDUCATOR role (direct notification)
             INSTITUTE_ADMIN (if is_minor or enterprise context)
    Automated action: If championship_context_flag: COOLDOWN_ENFORCED
    Human review: REQUIRED — must log within 24 hours
    Governance: GOVERNANCE_AGENT awareness notification (not escalation)

  SEVERITY_CRITICAL:
    Action: Immediate full escalation
    Routing: All URGENT recipients +
             GOVERNANCE_AGENT (formal escalation) +
             PLATFORM_ADMIN notification
    Automated action: COOLDOWN_ENFORCED + SESSION_PAUSED (if active session)
                      championship eligibility suspended pending review
    Human review: REQUIRED — must log within 1 hour
    Welfare hold: RELIABILITY_SCORE suppressed from recruiter view
                  championship_readiness = NOT_READY (overrides all other signals)

  WELFARE_OUTCOME_RECORDING:
    Human reviewer must record:
      - outcome: RESOLVED | MONITORED | ESCALATED_EXTERNAL | FALSE_POSITIVE
      - action_taken: structured code
      - reviewer_notes: free text (stored in welfare partition only)
      - resolution_timestamp_utc
    Unresolved URGENT after 24h → automated reminder to GOVERNANCE_AGENT
    Unresolved CRITICAL after 1h → automated governance escalation (hard reminder)

  FALSE_POSITIVE_FEEDBACK_LOOP:
    Mentor-reported FALSE_POSITIVE outcomes fed back to welfare classifier training
    This data is critical for maintaining precision without sacrificing recall
    Minimum batch: 200 labeled false positives per domain before model update
```

---

## 🔐 FINAL GOVERNANCE SEAL

```
╔══════════════════════════════════════════════════════════════════════════════════╗
║  COGNITIVE STABILITY INDEX AGENT — PRODUCTION SEAL                              ║
║                                                                                  ║
║  AGENT ID          : CSIA-001                                                   ║
║  VERSION           : v1.0.0                                                     ║
║  PLATFORM          : ECOSKILLER ANTIGRAVITY                                     ║
║  CLASSIFICATION    : WELFARE-CRITICAL + INTELLIGENCE-CRITICAL                   ║
║                                                                                  ║
║  ✅  ML-Primary Computation (74%)                                               ║
║      XGBoost Axis Scoring (8 models) + Stacking Compositor +                    ║
║      Isolation Forest + DBSCAN Temporal Anomaly +                               ║
║      GBM Welfare Classifier + Logistic Pressure Forecaster +                    ║
║      Platt-Calibrated Reliability Scorer (6-stage pipeline)                     ║
║  ✅  AI-Assist Narrative (26%) — 11 versioned prompt variants                  ║
║      Forbidden language enforcement, zero clinical framing                      ║
║  ✅  8-Axis Cognitive Stability Vector — Immutable axis definitions             ║
║  ✅  68 ML Features — Full behavioral signal registry                          ║
║  ✅  5 Domain Stability Profiles — Domain-specific compositor weights           ║
║  ✅  Welfare Check-in Protocol — 5 severity levels, human-in-the-loop          ║
║  ✅  Welfare Fast Path — URGENT/CRITICAL routing < 60 seconds                  ║
║  ✅  Minor Protection — Absolute block on recruiter access, parent routing      ║
║  ✅  Forbidden Language Scanner — AI output validated before use                ║
║  ✅  Zero-Trust Security — Tenant + domain isolation hard enforced              ║
║  ✅  Dual Audit System — Standard + welfare encrypted partition                 ║
║  ✅  Welfare Audit Retention — 10 years (extended from 7y standard)            ║
║  ✅  Championship Readiness Protocol — 3-tier gate (READY/CONDITIONAL/NOT)     ║
║  ✅  Reliability Score — Recruiter-facing, welfare-isolated, calibrated         ║
║  ✅  Adaptive Difficulty Signal — Real-time modulation for Scenario Engine      ║
║  ✅  12 Failure Scenarios — Zero silent failures, safety overrides first        ║
║  ✅  Scalability — 35,000 RPS peak, welfare fast path always active             ║
║  ✅  Growth Engine — Recovery + resilience + championship hooks                 ║
║  ✅  Feature Store Emit — 15 stability features (welfare features excluded)     ║
║  ✅  Ethics Board Requirement — Mandatory for all welfare policy changes        ║
║  ✅  Versioned Add-Only — 13 versioned artifact types                          ║
║  ✅  Observability — 14 metrics, 9 dashboards, welfare classifier monitoring   ║
║                                                                                  ║
║  CLINICAL DIAGNOSIS                : ABSOLUTELY FORBIDDEN                       ║
║  THERAPEUTIC CONTENT               : FORBIDDEN                                  ║
║  FORBIDDEN LANGUAGE IN OUTPUT      : FORBIDDEN (scanner enforced)               ║
║  RECRUITER ACCESS TO WELFARE DATA  : FORBIDDEN — API GATEWAY HARD BLOCK        ║
║  RECRUITER ACCESS TO MINOR DATA    : FORBIDDEN — 403 AT GATEWAY                ║
║  AUTOMATED WELFARE ACTION          : FORBIDDEN EXCEPT COOLDOWN + SESSION_PAUSE  ║
║  WELFARE WITHOUT HUMAN REVIEW      : FORBIDDEN for CHECK_IN and above          ║
║  SILENT FAILURES                   : FORBIDDEN                                  ║
║  CROSS-TENANT ACCESS               : FORBIDDEN                                  ║
║  HISTORICAL CSV MUTATION           : FORBIDDEN                                  ║
║  WELFARE LOG DELETION              : FORBIDDEN                                  ║
║  SELF-REPORTED MINOR FLAG          : FORBIDDEN — must source from TRUST AGENT   ║
║  ETHICS BYPASS                     : FORBIDDEN FOR ANY REASON                   ║
║                                                                                  ║
║  INTERPRETATION AUTHORITY          : NONE                                       ║
║  ARCHITECTURE AUTHORITY            : LOCKED                                     ║
║  ETHICS BOARD REQUIREMENT          : ACTIVE — WELFARE CHANGES REQUIRE SIGN-OFF ║
║  SEALED BY: ECOSKILLER ANTIGRAVITY GOVERNANCE CORE                              ║
╚══════════════════════════════════════════════════════════════════════════════════╝
```

---

*Document Classification      : Enterprise SaaS System Specification — Welfare-Critical*
*Mutation Policy              : Add-only via version bump + Governance + Ethics Board approval*
*Parent System Document       : MASTER_AGENT_CREATION_PROMPT.md*
*Reference Artifacts          : DOJO_SAAS_SPEC_v1.0 (T10, T11, T12, T13) |*
*                               ECOSKILLER_COMPLETE_TECHS | TALENT_OS_BLUEPRINT |*
*                               ERMA_v1.0.0 | ACEMA_v1.0.0 | LVMA_v1.0.0*
*ERMA Integration Version     : CSIA-ERMA-INTERFACE-v1.0*
*LVMA Integration Version     : CSIA-LVMA-INTERFACE-v1.0*
*ACEMA Integration Version    : CSIA-ACEMA-INTERFACE-v1.0*
*Welfare Protocol Version     : CSIA-WELFARE-PROTOCOL-v1.0*
*Ethics Board Last Review     : Required before production deployment*
*Next Review                  : Triggered by MAJOR version bump or Ethics Board request*
```
