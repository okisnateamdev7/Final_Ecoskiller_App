# 🔒 GD_POST_SESSION_ANALYTICS_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED AGENT SPECIFICATION
### Artifact Class: Enterprise Production Agent Blueprint
### Version: v1.0.0
### Mutation Policy: Add-only via version bump
### Execution Mode: Deterministic + Validated
### Interpretation Authority: NONE
### Architecture Authority: LOCKED
### Seal Status: PERMANENTLY SEALED

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║       GD POST-SESSION ANALYTICS AGENT — ANTIGRAVITY PLATFORM               ║
║       ECOSKILLER INTELLIGENCE & INNOVATION CORE                             ║
║       STATUS: LOCKED · SEALED · PRODUCTION-GRADE · ENFORCED                ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## 🔐 EXECUTION GOVERNANCE HEADER

```
AGENT_CLASS                    = AUTONOMOUS_ANALYTICS_PROCESSOR
EXECUTION_MODE                 = DETERMINISTIC + VALIDATED
MUTATION_POLICY                = ADD_ONLY_VERSIONED
CREATIVE_INTERPRETATION        = FORBIDDEN
ASSUMPTION_FILLING             = FORBIDDEN
DEFAULT_BEHAVIOR               = DENY_ON_AMBIGUITY
FAILURE_MODE                   = HALT + LOG + ESCALATE
SECURITY_MODEL                 = ZERO_TRUST_MULTI_TENANT
DATA_POLICY                    = APPEND_ONLY_AUDIT_TRAIL
CROSS_TENANT_QUERY             = FORBIDDEN
SILENT_FAILURE                 = FORBIDDEN
PARTIAL_OUTPUT                 = FORBIDDEN
RETROACTIVE_SCORE_MODIFICATION = FORBIDDEN
AI_DECISION_AUTONOMY           = FORBIDDEN
ANALYTICS_OVERRIDE_SCORING     = FORBIDDEN
LEARNING_SIGNAL_SUPPRESSION    = FORBIDDEN
DOMAIN_DATA_MIXING             = FORBIDDEN
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY — NON-NEGOTIABLE)

```yaml
AGENT_NAME:         GD_POST_SESSION_ANALYTICS_AGENT
AGENT_ID:           EKAG-GDPSA-003
VERSION:            v1.0.0
SYSTEM_ROLE:        Autonomous Post-Session Intelligence Engine —
                    Computes multi-dimensional performance analytics,
                    learning effectiveness signals, skill growth vectors,
                    scenario calibration data, mentor effectiveness metrics,
                    curriculum intelligence, and platform-wide behavioral
                    intelligence from finalized GD session data
PRIMARY_DOMAIN:     Dojo GD Engine — Ecoskiller Antigravity Platform
EXECUTION_MODE:     Deterministic + Validated
DATA_SCOPE:         Finalized scoring records, attendance records, session
                    metadata, participant skill vectors, scenario performance
                    history, mentor evaluation records, belt progression data,
                    learning path effectiveness, domain engagement metrics,
                    cohort comparative analytics, institute/enterprise
                    performance reports, intelligence profile enrichment signals,
                    platform health metrics, economic abuse detection signals,
                    curriculum calibration feedback, feature store enrichment
TENANT_SCOPE:       Strict Isolation — No cross-tenant analytics data mixing
FAILURE_POLICY:     Halt on ambiguity — No partial analytics pipeline output
DOMAIN_LOCK:        Arts | Commerce | Science | Technology | Administration
                    (Cross-domain analytics computation FORBIDDEN without
                     explicit platform-level grant)
STACK_CONTEXT:      FastAPI microservice | PostgreSQL 15 | Redis 7 |
                    Kafka Event Bus (consumer) | OpenSearch 2.x |
                    Prometheus + Grafana | MinIO (report storage) |
                    Celery + Redis (async job queue) | Python 3.11
PIPELINE_TRIGGER:   ATTENDANCE_FINALIZED_EVENT + SCORING_COMPLETED_EVENT
                    (both must be present before pipeline initiates)
```

This agent must **never** assume missing data. If prerequisite events
(attendance finalization + scoring completion) have not both arrived within
the defined SLA window, execution STOPS and escalates.

---

## 2️⃣ PURPOSE DECLARATION

### Problem This Agent Solves

After every Group Discussion session ends, raw data exists in three isolated
systems: scoring records (SCORING_ENGINE), attendance records
(GD_ATTENDANCE_TRACKING_AGENT), and session metadata
(GD_SESSION_SCHEDULER_AGENT). Without this agent, that data:

- Produces no learning signal — no participant knows if they improved
- Cannot calibrate scenario difficulty — rubrics stay author-declared forever
- Cannot detect mentor score drift — rater bias compounds silently
- Cannot measure curriculum effectiveness — skill tracks have no outcome data
- Cannot power belt promotion decisions with confidence — gut-based advancement
- Cannot generate institute/enterprise ROI reports — no proof of training value
- Cannot identify at-risk learners for early intervention
- Cannot detect collusion patterns through score+attendance cross-analysis
- Cannot enrich the Intelligence Profile (Gardner's 8 types) with real behavioral data
- Cannot feed the passive intelligence layer for platform-wide ML improvement
- Cannot power the talent marketplace with verified, evidence-backed skill vectors
- Cannot detect scenario retirement triggers (consistently failing scenarios)
- Cannot generate the "Career DNA" signal that differentiates Ecoskiller from
  every competitor on the market

This agent is the **intelligence transformation layer** — it converts raw
session events into structured knowledge that makes the entire platform
smarter with every session processed.

### What Input It Consumes

- ATTENDANCE_FINALIZED_EVENT from GD_ATTENDANCE_TRACKING_AGENT
- SCORING_COMPLETED_EVENT from SCORING_ENGINE
- Session metadata from GD_SESSION_SCHEDULER_AGENT (via event or query)
- Historical participant skill vectors from SKILL_VECTOR_STORE
- Scenario metadata and historical calibration data from SCENARIO_CATALOG_AGENT
- Mentor calibration records from MENTOR_CALIBRATION_ENGINE
- Belt progression history from CERTIFICATE_ENGINE
- Learning path context from LEARNING_PATH_AGENT
- Intelligence profile baseline from INTELLIGENCE_PROFILE_AGENT
- Cohort/institution context from TENANT_CONTEXT_AGENT
- Domain industry demand signals from INDUSTRY_INTELLIGENCE_AGENT
- Feature store historical vectors from FEATURE_STORE_AGENT

### What Output It Produces

- Per-participant performance analytics reports (multi-dimensional)
- Skill growth vectors (delta between pre- and post-session performance)
- Learning effectiveness signals (improvement / stagnation / regression)
- Scenario difficulty calibration updates (data-derived, not author-declared)
- Mentor effectiveness and score drift reports
- Cohort comparative performance analytics (institute/enterprise level)
- Intelligence profile enrichment signals (Gardner's 8 types — behavioral data)
- Career DNA vector updates (skill → career path signal enrichment)
- At-risk learner identification signals (early intervention triggers)
- Curriculum review flags (underperforming scenarios / skill tracks)
- Belt promotion confidence scores (evidence-backed eligibility signals)
- Collusion cross-reference signals (score + attendance pattern analysis)
- Platform health analytics (domain engagement, session quality trends)
- ERP-compatible performance reports (institute / enterprise)
- Feature vectors to FEATURE_STORE_AGENT
- Audit records to AUDIT_LOG_SERVICE (append-only, immutable)

### Downstream Agents That Depend on This Agent

- CERTIFICATE_ENGINE (belt promotion confidence scores)
- LEARNING_PATH_AGENT (skill gap signals, learning effectiveness)
- INTELLIGENCE_PROFILE_AGENT (behavioral enrichment of 8 intelligence types)
- CAREER_DNA_AGENT (skill → career vector updates)
- SCENARIO_CATALOG_AGENT (difficulty recalibration signals)
- MENTOR_CALIBRATION_ENGINE (score drift reports, bias alerts)
- CURRICULUM_GOVERNANCE_AGENT (curriculum review flags)
- COLLUSION_DETECTION_AGENT (score + attendance cross-analysis)
- GROWTH_ENGINE (performance-linked learning signals)
- TALENT_MARKETPLACE_AGENT (verified skill vectors, evidence trails)
- INSTITUTE_ERP_AGENT (cohort performance reports)
- ENTERPRISE_ERP_AGENT (training ROI analytics)
- PARENT_VISIBILITY_SERVICE (child learning progress summaries)
- NOTIFICATION_AGENT (performance insights, at-risk alerts)
- OBSERVABILITY_AGENT (platform health analytics)
- FEATURE_STORE_AGENT (enriched behavioral features)
- INDUSTRY_INTELLIGENCE_AGENT (skill-to-demand gap feedback)

### Upstream Agents That Feed This Agent

- GD_ATTENDANCE_TRACKING_AGENT (finalized attendance records)
- SCORING_ENGINE (finalized scoring records)
- GD_SESSION_SCHEDULER_AGENT (session context, fairness snapshot)
- SKILL_VECTOR_STORE (historical participant skill baselines)
- SCENARIO_CATALOG_AGENT (scenario metadata, version, rubric)
- MENTOR_CALIBRATION_ENGINE (mentor calibration state)
- CERTIFICATE_ENGINE (belt history per participant)
- LEARNING_PATH_AGENT (current enrolled learning path)
- INTELLIGENCE_PROFILE_AGENT (current intelligence baseline scores)
- FEATURE_STORE_AGENT (historical behavioral features)
- TENANT_CONTEXT_AGENT (tenant plan, domain, institution type)

---

## 3️⃣ INPUT CONTRACT (STRICT — ZERO TOLERANCE)

### 3A — Pipeline Trigger (Dual-Event Gate)

```json
PIPELINE_TRIGGER_SCHEMA: {
  "required_fields": [
    "trigger_id",
    "session_id",
    "tenant_id",
    "attendance_record_id",
    "scoring_record_id",
    "attendance_finalized_at_utc",
    "scoring_completed_at_utc",
    "schema_version"
  ],
  "field_definitions": {
    "trigger_id":                  "UUID — idempotency key for this pipeline run",
    "session_id":                  "UUID — must exist in gd_sessions",
    "tenant_id":                   "UUID — strict tenant isolation scope",
    "attendance_record_id":        "UUID — must exist in gd_attendance_records,
                                    status = FINALIZED (not LOW_CONFIDENCE,
                                    not PENDING_FINALIZATION)",
    "scoring_record_id":           "UUID — must exist in gd_scoring_records,
                                    status = COMPLETED (not DISPUTED,
                                    not PENDING_MENTOR_REVIEW)",
    "attendance_finalized_at_utc": "ISO8601 — when attendance was finalized",
    "scoring_completed_at_utc":    "ISO8601 — when scoring was completed",
    "schema_version":              "string — must match agent's declared version"
  },
  "dual_gate_rules": [
    "BOTH attendance_record_id AND scoring_record_id must be present — one alone is INSUFFICIENT",
    "attendance_record.status must be FINALIZED — LOW_CONFIDENCE blocks pipeline",
    "scoring_record.status must be COMPLETED — DISPUTED scores block pipeline",
    "scoring_record.all_participants_scored = TRUE — partial scoring blocks pipeline",
    "Gap between attendance_finalized_at_utc and scoring_completed_at_utc must be <= 3600s",
    "If gap > 3600s: LOG_INCIDENT + ESCALATE — do not proceed with stale pairing",
    "session_id must match between attendance_record and scoring_record",
    "tenant_id must match across all referenced records"
  ],
  "sla_window_rules": [
    "Pipeline must be triggered within 300s of BOTH events arriving",
    "If trigger not received within 600s of session end: DEAD_LETTER + ESCALATE",
    "Retry window: 3 attempts at 60s intervals before dead-lettering"
  ],
  "security_checks": [
    "Service-to-service mTLS authentication required",
    "trigger_id deduplication: reject duplicate trigger within 3600s window",
    "tenant_id isolation: pipeline runs in tenant-scoped compute context"
  ],
  "null_tolerance_policy": "ZERO — all fields required, non-null",
  "reject_policy":          "Invalid trigger → STOP → LOG → DEAD_LETTER → ESCALATE"
}
```

### 3B — Historical Context Queries (Internal — Agent-Initiated)

The agent proactively queries these on pipeline initiation:

```json
HISTORICAL_CONTEXT_QUERIES: {
  "participant_skill_history": {
    "source":   "SKILL_VECTOR_STORE",
    "query":    "Last 10 sessions per participant in same domain + session_type",
    "purpose":  "Pre-session skill baseline for delta computation"
  },
  "scenario_calibration_history": {
    "source":   "SCENARIO_CATALOG_AGENT",
    "query":    "All historical performance data for this scenario_id",
    "purpose":  "Difficulty recalibration + fairness audit"
  },
  "mentor_calibration_state": {
    "source":   "MENTOR_CALIBRATION_ENGINE",
    "query":    "Current calibration score + drift history for mentor_id",
    "purpose":  "Detect if mentor scoring is within calibration tolerance"
  },
  "belt_progression_history": {
    "source":   "CERTIFICATE_ENGINE",
    "query":    "Full belt history for each RANKED/CERTIFICATION participant",
    "purpose":  "Belt promotion confidence scoring"
  },
  "intelligence_profile_baseline": {
    "source":   "INTELLIGENCE_PROFILE_AGENT",
    "query":    "Current 8-type intelligence scores per participant",
    "purpose":  "Enrich with behavioral signals from this GD session"
  },
  "cohort_performance_baseline": {
    "source":   "FEATURE_STORE_AGENT",
    "query":    "Cohort performance distribution for same domain + session_type",
    "purpose":  "Normalization baseline for comparative analytics"
  },
  "failure_policy_on_query": "If critical query fails (participant_skill_history,
                               scenario_calibration_history): HALT pipeline.
                               If enrichment query fails (intelligence_profile,
                               cohort_baseline): CONTINUE with degraded_signal flag."
}
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

### 4A — Master Analytics Report (Per Session)

```json
SESSION_ANALYTICS_REPORT: {
  "result_object": {
    "analytics_report_id":     "UUID — primary immutable identifier",
    "session_id":              "UUID",
    "tenant_id":               "UUID",
    "domain_track":            "string",
    "session_type":            "string",
    "generated_at_utc":        "ISO8601",
    "pipeline_duration_ms":    "integer",
    "agent_version":           "string",

    "session_summary": {
      "total_participants":              "integer",
      "qualified_attendance_count":      "integer",
      "scored_participants_count":       "integer",
      "session_completion_rate":         "float",
      "average_session_score":           "float",
      "score_distribution":             "object — p10/p25/p50/p75/p90/p95",
      "mentor_present":                  "boolean",
      "scenario_id":                     "UUID",
      "scenario_version":                "string",
      "difficulty_label_used":           "string",
      "fairness_compliance_passed":      "boolean",
      "collusion_flags_raised":          "integer",
      "overall_session_quality_score":   "float 0.0–1.0"
    },

    "participant_analytics": [
      {
        "participant_id":                "UUID",
        "performance_vector": {
          "total_score":                 "float",
          "score_by_dimension":          "Map<metric_name, score>",
          "score_percentile_in_session": "float — rank vs. peers this session",
          "score_percentile_in_cohort":  "float — rank vs. historical cohort",
          "score_percentile_in_domain":  "float — rank vs. all domain participants"
        },
        "skill_delta": {
          "pre_session_skill_score":     "float — from historical baseline",
          "post_session_skill_score":    "float — updated after this session",
          "delta":                       "float — signed (+ improvement / - regression)",
          "delta_percentile":            "float — how strong is this delta vs. cohort",
          "learning_trajectory":         "ENUM[IMPROVING | STABLE | DECLINING | BREAKTHROUGH]",
          "breakthrough_flag":           "boolean — delta > 1.5 standard deviations above mean"
        },
        "learning_effectiveness": {
          "matches_since_last_belt":     "integer",
          "improvement_rate_last_5":     "float — avg delta over last 5 sessions",
          "retention_signal":            "float — performance on previously mastered skills",
          "regression_detected":         "boolean",
          "regression_dimensions":       "Array<metric_name>",
          "at_risk_flag":                "boolean — persistent decline >= 3 sessions",
          "at_risk_reason":              "string | null"
        },
        "belt_promotion_signal": {
          "eligible_for_review":         "boolean",
          "promotion_confidence_score":  "float 0.0–1.0",
          "sessions_completed":          "integer",
          "threshold_sessions_met":      "boolean",
          "score_threshold_met":         "boolean",
          "pressure_scenario_passed":    "boolean",
          "mentor_certification_present":"boolean",
          "evidence_summary":            "Array<evidence_item>"
        },
        "intelligence_enrichment": {
          "session_contributed_to":      "Array<intelligence_type>",
          "linguistic_signal":           "float | null",
          "logical_signal":              "float | null",
          "interpersonal_signal":        "float | null",
          "intrapersonal_signal":        "float | null",
          "spatial_signal":              "float | null",
          "naturalistic_signal":         "float | null",
          "musical_signal":              "float | null",
          "bodily_signal":               "float | null"
        },
        "career_dna_signal": {
          "skill_dimension_updated":     "Array<skill_dimension>",
          "career_alignment_delta":      "Map<career_path, delta_score>",
          "top_career_alignment":        "Array<career_path> — top 3"
        },
        "comparative_context": {
          "vs_session_average":          "string — ABOVE | AT | BELOW",
          "vs_domain_average":           "string",
          "vs_cohort_average":           "string",
          "vs_personal_average":         "string",
          "rank_in_session":             "integer",
          "rank_in_institution":         "integer | null"
        }
      }
    ],

    "scenario_calibration_update": {
      "scenario_id":                     "UUID",
      "scenario_version":                "string",
      "this_session_pass_rate":          "float",
      "cumulative_pass_rate":            "float — updated",
      "score_distribution_this_session": "object",
      "avg_completion_time_seconds":     "integer",
      "abandonment_count":               "integer",
      "failure_clustering":              "Array<failing_dimension>",
      "difficulty_reclassification": {
        "current_label":                 "string",
        "recommended_label":             "string | null — if change warranted",
        "reclassification_triggered":    "boolean",
        "data_basis":                    "object — supporting statistics"
      },
      "retirement_signal": {
        "retirement_triggered":          "boolean",
        "retirement_reason":             "string | null",
        "sessions_evaluated":            "integer"
      },
      "fairness_audit": {
        "cultural_neutrality_flag":      "boolean",
        "domain_bias_detected":          "boolean",
        "regional_performance_variance": "float"
      }
    },

    "mentor_effectiveness_report": {
      "mentor_id":                           "UUID | null",
      "present":                             "boolean",
      "calibration_score_pre_session":       "float | null",
      "score_alignment_with_peers":          "float | null",
      "score_variance_vs_calibration":       "float | null",
      "drift_detected":                      "boolean",
      "drift_severity":                      "ENUM[NONE | MILD | MODERATE | SEVERE] | null",
      "override_count":                      "integer",
      "override_pattern_anomaly":            "boolean",
      "favoritism_signal":                   "boolean",
      "recalibration_recommended":           "boolean",
      "recertification_trigger":             "boolean"
    },

    "collusion_cross_reference": {
      "score_attendance_anomaly":            "boolean",
      "suspicious_score_pairs":              "Array<participant_id_pair>",
      "reciprocal_high_scoring_detected":    "boolean",
      "farming_pattern_score":               "float",
      "cross_agent_investigation_required":  "boolean"
    },

    "curriculum_intelligence": {
      "skill_track_id":                      "UUID | null",
      "skill_track_effectiveness_delta":     "float | null",
      "drill_effectiveness_scores":          "Map<drill_id, score> | null",
      "drop_off_signals":                    "Array<drop_off_point>",
      "curriculum_review_flag":              "boolean",
      "curriculum_review_reason":            "string | null"
    }
  },

  "confidence_score":    "float 0.0–1.0 — analytics computation completeness",
  "degraded_signals":    "Array<signal_name> — enrichments that ran in fallback mode",
  "model_version":       "string",
  "audit_reference":     "UUID",
  "next_trigger_events": [
    "ANALYTICS_REPORT_READY_EVENT",
    "SKILL_VECTOR_UPDATE_EVENT",
    "INTELLIGENCE_PROFILE_UPDATE_EVENT",
    "CAREER_DNA_UPDATE_EVENT",
    "SCENARIO_CALIBRATION_UPDATE_EVENT",
    "MENTOR_DRIFT_ALERT_EVENT (if drift_detected)",
    "AT_RISK_LEARNER_ALERT_EVENT (if at_risk_flag)",
    "BELT_PROMOTION_SIGNAL_EVENT (if eligible_for_review)",
    "CURRICULUM_REVIEW_FLAG_EVENT (if curriculum_review_flag)",
    "COLLUSION_INVESTIGATION_EVENT (if cross_agent_investigation_required)"
  ]
}
```

All analytics reports are **immutable on creation**. Subsequent sessions
produce new reports — old reports are never mutated.

---

## 5️⃣ ML / AI LOGIC LAYER

### ML Usage (Primary — ~75% of all intelligence computations)

```
════════════════════════════════════════════════════════════════════════
MODEL_1: SKILL_DELTA_CALCULATOR
════════════════════════════════════════════════════════════════════════
Type:             Regression + Time-Series Analysis
Purpose:          Compute the true learning delta for each participant
                  across each scored skill dimension, controlling for
                  session difficulty, opponent quality, scenario variance,
                  and random performance noise.
Input Features:
  - current_session_score_by_dimension (from SCORING_ENGINE)
  - historical_session_scores_last_10 (per dimension per participant)
  - scenario_difficulty_weight (from SCENARIO_CATALOG calibration)
  - opponent_average_rating (fairness snapshot from SCHEDULER)
  - session_type_weight (RANKED > PRACTICE for delta signal strength)
  - attendance_percentage (ghost/partial attendance degrades delta signal)
  - mentor_calibration_score (uncalibrated mentor scores penalized)
  - time_since_last_session (recency weight — recent sessions matter more)
  - pre_assessment_score_if_available (from LEARNING_PATH_AGENT)
Output:
  - skill_delta per dimension (signed float)
  - learning_trajectory classification
  - breakthrough_flag (delta > 1.5σ above cohort mean)
  - delta_confidence_score (data quality metric)
Training Freq:    Weekly (rolling 180-day window)
Drift Detection:
  - Monitor delta distribution shift by domain monthly
  - Monitor breakthrough_flag rate (target 5–8% of sessions — alert if > 15%)
  - Monitor trajectory classification stability
Version Control:  Immutable model version in every skill_delta output

════════════════════════════════════════════════════════════════════════
MODEL_2: LEARNING_EFFECTIVENESS_PREDICTOR
════════════════════════════════════════════════════════════════════════
Type:             Multi-Label Classification + Regression
Purpose:          Predict a participant's learning trajectory, detect
                  early regression, identify at-risk learners before
                  they disengage, and signal curriculum effectiveness.
Input Features:
  - skill_delta_last_5_sessions (rolling window per dimension)
  - session_completion_rate (attendance quality signal)
  - time_between_sessions (gap analysis — too long = retention loss)
  - score_variance_over_time (inconsistency signal)
  - improvement_rate_slope (linear regression on historical scores)
  - drill_engagement_rate (from LEARNING_PATH_AGENT)
  - belt_progression_velocity (time per belt level)
  - peer_interaction_quality_score (from SCORING_ENGINE)
  - mentor_feedback_frequency (sessions with mentor feedback vs. without)
  - domain_difficulty_exposure (are they practicing the right difficulty?)
Output:
  - learning_trajectory: ENUM[IMPROVING | STABLE | DECLINING | BREAKTHROUGH | AT_RISK]
  - at_risk_flag: boolean (3+ consecutive declining sessions)
  - at_risk_reason: categorical string
  - recommended_intervention_type: ENUM[DRILL_ASSIGNMENT | MENTOR_REVIEW |
                                        DIFFICULTY_ADJUSTMENT | PEER_MATCHING |
                                        LEARNING_PATH_REVISION | NONE]
  - retention_signal: float (how well are mastered skills being retained?)
  - regression_detected: boolean
  - regression_dimensions: Array<metric_name>
Training Freq:    Weekly
Drift Detection:  Monitor at_risk_flag rate monthly (target 10–15% of users)
                  Alert if > 20% — may indicate platform-wide issue

════════════════════════════════════════════════════════════════════════
MODEL_3: SCENARIO_DIFFICULTY_RECALIBRATOR
════════════════════════════════════════════════════════════════════════
Type:             Supervised Classification + Statistical Process Control
Purpose:          Continuously recalibrate scenario difficulty labels
                  based on actual performance data. Difficulty labels
                  MUST be data-derived — never static author declarations.
                  Also triggers scenario retirement when warranted.
Input Features:
  - cumulative_pass_rate (all sessions using this scenario_id)
  - score_distribution_spread (narrow = easy; wide = well-calibrated)
  - avg_completion_time_seconds (vs. expected for difficulty label)
  - abandonment_rate (users quitting mid-scenario)
  - failure_clustering_pattern (which dimensions fail consistently)
  - regional_performance_variance (cultural fairness signal)
  - belt_level_distribution_of_takers (is the right audience taking it?)
  - recalibration_history (how many times has label changed?)
  - sessions_since_last_calibration (recency weight)
Output:
  - current_difficulty_label: string
  - recommended_difficulty_label: string (may be same = no change)
  - reclassification_confidence: float
  - reclassification_triggered: boolean (triggers SCENARIO_CATALOG update)
  - retirement_signal: boolean (pass_rate < 15% over 50+ sessions)
  - retirement_reason: string | null
  - fairness_audit_flag: boolean (regional variance > 25%)
Reclassification Rules:
  - BEGINNER → INTERMEDIATE: pass_rate < 70% over 30+ sessions
  - INTERMEDIATE → ADVANCED: pass_rate < 55% over 30+ sessions
  - ADVANCED → PRESSURE: pass_rate < 35% over 30+ sessions
  - Any label → easier: pass_rate > 92% over 30+ sessions
  - Retirement trigger: pass_rate < 15% over 50+ sessions
Training Freq:    Monthly
Drift Detection:  Statistical process control charts per scenario
                  Western Electric Rules for out-of-control detection

════════════════════════════════════════════════════════════════════════
MODEL_4: MENTOR_SCORE_DRIFT_DETECTOR
════════════════════════════════════════════════════════════════════════
Type:             Statistical Anomaly Detection + Time-Series
Purpose:          Detect when a mentor's scoring patterns are drifting
                  from calibration standards — identifying leniency bias,
                  severity bias, halo effects, and favoritism patterns.
Input Features:
  - mentor_session_scores_last_30_days (per dimension)
  - gold_standard_calibration_scores (known-correct benchmark)
  - inter-rater_reliability_coefficient (vs. peer mentors, same scenarios)
  - score_variance_by_participant_group (favoritism detection)
  - override_frequency (excessive overrides = red flag)
  - score_distribution_shape (bimodal distribution = bias)
  - calibration_score_trajectory (improving / degrading over time)
  - time_since_last_calibration_session
Output:
  - calibration_score: float (current calibration quality)
  - drift_detected: boolean
  - drift_severity: ENUM[NONE | MILD | MODERATE | SEVERE]
  - drift_type: Array<ENUM[LENIENCY | SEVERITY | HALO_EFFECT | FAVORITISM |
                             DIMENSION_BLINDNESS | CULTURAL_BIAS]>
  - recalibration_recommended: boolean
  - recertification_trigger: boolean (SEVERE drift = automatic)
  - certification_authority_at_risk: boolean
Escalation:       SEVERE drift → MENTOR_CALIBRATION_ENGINE +
                  GOVERNANCE_AGENT + TENANT_ADMIN
Training Freq:    Monthly (calibration benchmark updated with new gold standards)
Drift Detection:  Runs on itself — monitor mentor_calibration score trends
                  Alert if > 10% of active mentors drift simultaneously

════════════════════════════════════════════════════════════════════════
MODEL_5: INTELLIGENCE_PROFILE_ENRICHER
════════════════════════════════════════════════════════════════════════
Type:             Multi-Output Regression
Purpose:          Map GD session behavioral signals to Gardner's 8
                  intelligence dimensions, enriching each participant's
                  permanent Intelligence Profile with real performance data.
                  This is Ecoskiller's core differentiator — no competitor
                  does this at scale.
Input Features:
  Per dimension:
  LINGUISTIC:
    - verbal_articulation_score (from SCORING_ENGINE rubric)
    - argument_structure_score
    - vocabulary_richness_signal (if text-based GD)
    - persuasion_effectiveness_score
  LOGICAL:
    - problem_structuring_score
    - evidence_usage_score
    - logical_flow_score
    - counter-argument_quality_score
  INTERPERSONAL:
    - peer_engagement_score (how well they engaged others)
    - conflict_navigation_score
    - leadership_signal (others directed attention to them)
    - collaboration_score
  INTRAPERSONAL:
    - self-correction_observed (mentor evaluation signal)
    - emotional_regulation_score (behavioral safety signals)
    - goal_orientation_signal (did they stay on topic?)
  SPATIAL: (relevant for TECHNOLOGY / design-domain GDs)
    - visual_reasoning_score (scenario-specific)
  NATURALISTIC: (relevant for SCIENCE / ARTS domain GDs)
    - pattern_recognition_score
    - systems_thinking_score
  (MUSICAL and BODILY: indirect signals only from specific scenario types)
Output:           8-dimensional enrichment vector (float per type, 0.0–1.0)
                  intelligence_profile_update_delta per dimension
                  signal_confidence per dimension (some GD types enrich
                  more dimensions than others)
Training Freq:    Monthly
Drift Detection:  Monitor signal distribution per domain type monthly
                  Ensure domain→intelligence mapping remains valid

════════════════════════════════════════════════════════════════════════
MODEL_6: BELT_PROMOTION_CONFIDENCE_SCORER
════════════════════════════════════════════════════════════════════════
Type:             Logistic Regression + Rule Overlay
Purpose:          Compute a confidence score for belt promotion eligibility
                  that combines all evidence: session history, score
                  thresholds, pressure scenario performance, mentor
                  certification, attendance quality, and trajectory.
                  This score supports (never replaces) mentor board review.
Input Features:
  - sessions_completed_at_current_belt (vs. required minimum)
  - score_threshold_sessions_met_count (sessions scoring above threshold)
  - pressure_scenario_pass_count (hardest scenario type — mandatory)
  - mentor_certified_sessions_count (certification-supervised sessions)
  - skill_delta_trend_last_10 (trajectory at promotion point)
  - attendance_quality_rate (ghost/partial sessions penalize)
  - score_consistency_score (variance — consistent > erratic)
  - peer_comparison_percentile (vs. same-belt cohort)
  - time_at_current_belt (too fast = suspicious)
  - belt_level (higher belts require higher confidence threshold)
Output:
  - promotion_confidence_score: float 0.0–1.0
  - evidence_summary: Array<{evidence_type, value, weight}>
  - recommendation: ENUM[READY | BORDERLINE | NOT_READY]
  - blocking_factors: Array<string> — what's holding them back
  - sessions_remaining_estimate: integer | null
Thresholds:       >= 0.80: READY for mentor board review
                  0.60–0.79: BORDERLINE (mentor discretion)
                  < 0.60: NOT_READY (no board referral)
NOTE:             Auto-promotion is FORBIDDEN — this model only informs
                  the mentor board. Human decision authority is non-negotiable.
Training Freq:    Monthly
Drift Detection:  Monitor promotion rate per belt level (target: not runaway)

════════════════════════════════════════════════════════════════════════
MODEL_7: CAREER_DNA_VECTOR_UPDATER
════════════════════════════════════════════════════════════════════════
Type:             Multi-label Classification + Embedding Update
Purpose:          Update each participant's Career DNA vector based on
                  their demonstrated skills in this session. Maps
                  performance dimensions to career path affinities.
                  This is the "Intelligence → Skill → Career" pipeline
                  that makes Ecoskiller larger than LinkedIn + Udemy.
Input Features:
  - skill_delta (per dimension from MODEL_1)
  - intelligence_enrichment_vector (from MODEL_5)
  - domain_track (Arts / Commerce / Science / Tech / Admin)
  - performance_strength_dimensions (top 3 scored dimensions this session)
  - historical_career_dna_vector (current state from CAREER_DNA_AGENT)
  - industry_demand_mapping (from INDUSTRY_INTELLIGENCE_AGENT)
  - belt_level (skill maturity signal)
Output:
  - career_dna_delta: Map<career_path, delta_score>
  - updated_top_career_alignments: Array<career_path> — top 5
  - skill_to_career_evidence: Array<{skill, career_path, confidence}>
  - career_mismatch_flag: boolean (enrolled in wrong domain for their DNA?)
Training Freq:    Monthly (with industry demand signal refresh)
Drift Detection:  Monitor career alignment distribution shifts
                  Alert if career_mismatch_flag > 15% of active users

════════════════════════════════════════════════════════════════════════
MODEL_8: COLLUSION_SCORE_ATTENDANCE_CROSS_ANALYZER
════════════════════════════════════════════════════════════════════════
Type:             Anomaly Detection (Isolation Forest)
Purpose:          Cross-reference scoring patterns with attendance
                  patterns to detect collusion rings that may evade
                  either detector alone. Ghost detection + score fraud
                  together reveal patterns neither can catch solo.
Input Features:
  - ghost_detection_score per participant (from ATTENDANCE_AGENT)
  - peer_score_given per participant pair (from SCORING_ENGINE)
  - reciprocal_high_scoring_flag (A scored B high AND B scored A high)
  - attendance_synchrony_score (from ATTENDANCE_AGENT fraud detector)
  - score_distribution_among_peers (outlier high scores = suspicious)
  - historical_pair_session_count (same pair appearing often)
  - score_given_vs_received_correlation (strong correlation = farming)
  - device_fingerprint_clustering (from ATTENDANCE_AGENT)
Output:
  - cross_agent_anomaly_score: float 0.0–1.0
  - collusion_ring_hypothesis: Array<participant_id> — suspected ring
  - investigation_required: boolean (score >= 0.60)
  - fraud_evidence_package: structured evidence for COMPLIANCE_AGENT
Escalation:       >= 0.60 → COLLUSION_DETECTION_AGENT + COMPLIANCE_AGENT
                  All affected sessions flagged; scoring suspended pending review
Training Freq:    Monthly (adversarial refresh)

FEATURES_USED:    Explicitly defined per model above
VERSION_CONTROL:  All model versions stored immutably; referenced in
                  every analytics report; previous versions retained for
                  audit replay and outcome validation
```

### AI Usage (Supplementary — ~25% of output generation)

```
AI_USAGE_SCOPE:

  AI_1: PARTICIPANT_PERFORMANCE_INSIGHT_GENERATOR
    Purpose:     Generate human-readable, personalized performance
                 narrative for each participant's analytics dashboard —
                 translating ML vectors into actionable insights.
    Trigger:     ANALYTICS_REPORT_READY_EVENT — per participant
    Constraint:  Structured data → narrative only.
                 Template-driven with slot filling.
                 Temperature = 0.1 (minimal variation, no hallucination)
                 Max output: 300 words per participant
    Governance:  Prompt versioned in PROMPT_GOVERNANCE_REGISTRY v1.0+
    Rule:        AI never declares "you should be promoted" or makes
                 authoritative skill judgements — advisory tone only
    Validation:  All factual claims validated against ML output before
                 rendering — AI cannot introduce new factual claims

  AI_2: AT_RISK_INTERVENTION_RECOMMENDER
    Purpose:     Generate personalized intervention recommendation
                 message for at-risk learners and their mentors/admins
    Trigger:     at_risk_flag = TRUE from MODEL_2
    Constraint:  Structured intervention_type from ML → narrative guidance
                 Temperature = 0
                 Must not include alarming language to the learner
                 Mentor version and learner version are different prompts
    Governance:  Versioned prompts; learner-facing output reviewed for
                 emotional safety compliance

  AI_3: INSTITUTE_COHORT_REPORT_NARRATIVE_GENERATOR
    Purpose:     Generate executive summary for institute/enterprise
                 ERP cohort performance reports (principal, TPO, HR)
    Trigger:     Weekly batch ERP report generation
    Constraint:  Data pre-computed by ML; AI generates narrative summary only
                 Max 500 words; structured JSON data → prose paragraph
    Governance:  No PII in prompt — anonymized IDs only; de-anonymized on render

  AI_4: CURRICULUM_REVIEW_RECOMMENDATION_GENERATOR
    Purpose:     Generate curriculum review recommendation when
                 curriculum_review_flag = TRUE
    Trigger:     curriculum_review_flag = TRUE from analytics pipeline
    Constraint:  ML-derived data → structured recommendation narrative
                 Submitted to CURRICULUM_GOVERNANCE_AGENT for human review
    Rule:        AI recommendation is advisory only — no auto-curriculum change

  AI_5: SCENARIO_RETIREMENT_JUSTIFICATION_GENERATOR
    Purpose:     Generate formal retirement justification report when
                 scenario retirement is triggered by MODEL_3
    Trigger:     retirement_signal = TRUE from SCENARIO_DIFFICULTY_RECALIBRATOR
    Constraint:  Statistical data → formal written justification
                 Sent to SCENARIO_CATALOG_AGENT + GOVERNANCE_AGENT

AI GOVERNANCE RULES:
  - AI NEVER determines skill levels, belt eligibility, or career paths
  - AI NEVER overrides ML-computed scores or classifications
  - AI ONLY generates human-readable outputs from pre-computed ML data
  - All AI prompts versioned in PROMPT_GOVERNANCE_REGISTRY
  - AI outputs validated against source ML data before delivery
  - No AI creative interpretation of performance data — FORBIDDEN
  - Emotional safety review mandatory for all learner-facing AI outputs
```

---

## 6️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_THROUGHPUT:
  Sessions to process:      10,000+ sessions/day at platform scale
  Peak burst:               500 sessions completing within 30-minute window
  Events per session:       1 pipeline trigger per session
  Context queries per run:  6–8 historical queries per pipeline execution

LATENCY_TARGETS:
  Pipeline initiation:      p99 < 30s after dual-gate trigger received
  Core ML computation:      p99 < 120s (8 models, some parallelized)
  Full report generation:   p99 < 300s (5 minutes end-to-end)
  Insight delivery:         p99 < 60s after report generation
  ERP batch report:         p99 < 600s (async — non-blocking)
  Intelligence profile:     p99 < 60s after report generation

MAX_CONCURRENCY:
  Pipeline workers:         200 concurrent session analytics pipelines
  ML model inference:       Shared model server pool (GPU-backed for
                            large feature sets)
  AI generation workers:    50 concurrent AI generation jobs

QUEUE_STRATEGY:
  Primary pipeline queue:   Kafka topic — gd_analytics_pipeline_queue
                            Partitioned by tenant_id
                            Consumer groups per domain for isolation
  AI generation queue:      Celery + Redis — gd_analytics_ai_queue
                            Priority: AT_RISK alerts > standard insights
  ERP batch queue:          Celery + Redis — gd_erp_analytics_queue (low priority)
  Dead letter queue:        gd_analytics_pipeline_dlq (retry 3x then park)

COMPUTE_ARCHITECTURE:
  - Stateless pipeline workers — all state in PostgreSQL + Redis
  - ML model serving: shared FastAPI model server (no cold start on workers)
  - Feature retrieval: Redis cache (L1) → PostgreSQL (L2) — 95% cache hit target
  - Historical context: pre-fetched in parallel at pipeline start
  - AI generation: async, non-blocking — does not delay core analytics
  - Report storage: PostgreSQL (structured) + MinIO (PDF/export artifacts)

IDEMPOTENCY:
  Pipeline run key:  SHA256(session_id + attendance_record_id + scoring_record_id)
  Duplicate trigger: Return existing report_id — do not re-run
  Partial run recovery: Each pipeline stage checkpointed; resume from last
                        successful checkpoint on retry

ASYNC_OPERATIONS (non-blocking):
  - AI insight generation
  - ERP report generation
  - Parent visibility summary generation
  - Feature vector emission to FEATURE_STORE_AGENT
  - Career DNA vector emission
  - Intelligence profile enrichment emission

SYNC_OPERATIONS (blocking — pipeline halts if these fail):
  - Core ML model computation (Models 1–4, 6, 8)
  - Attendance + scoring data validation
  - Audit record creation
  - Skill vector update commitment
```

---

## 7️⃣ SECURITY ENFORCEMENT

```
SECURITY MODEL: ZERO-TRUST MULTI-TENANT

TENANT ISOLATION:
  ✓ All analytics computations scoped to tenant_id — RLS enforced
  ✓ Cohort comparisons: only within same tenant + institution
  ✓ Cross-tenant benchmarking: platform-level aggregated, anonymized only
  ✓ Kafka partitioned by tenant_id — no cross-tenant event consumption
  ✓ ML feature queries: tenant-scoped WHERE clauses enforced at ORM level
  ✓ No platform-wide participant rankings visible to tenants (competitive risk)

ROLE-BASED AUTHORIZATION (RBAC):
  Operation                           | STUDENT | MENTOR | EVALUATOR | ADMIN | PARENT | SYSTEM
  ─────────────────────────────────── |─────────|────────|───────────|───────|────────|───────
  View own performance analytics      |  ✓      |  ✓     |  ✓        |  ✓    |  ✗     |  ✓
  View own intelligence profile delta |  ✓      |  ✓     |  ✓        |  ✓    |  ✗     |  ✓
  View own career DNA signal          |  ✓      |  ✓     |  ✓        |  ✓    |  ✗     |  ✓
  View session aggregate analytics    |  ✗      |  ✓     |  ✓        |  ✓    |  ✗     |  ✓
  View mentor effectiveness report    |  ✗      |  ✗     |  ✓        |  ✓    |  ✗     |  ✓
  View cohort performance report      |  ✗      |  ✓     |  ✓        |  ✓    |  ✗     |  ✓
  View collusion cross-reference      |  ✗      |  ✗     |  ✗        |  ✓    |  ✗     |  ✓
  View child's learning progress      |  ✗      |  ✗     |  ✗        |  ✓    |  ✓*    |  ✓
  Access ERP analytics export         |  ✗      |  ✗     |  ✗        |  ✓    |  ✗     |  ✓
  View scenario calibration data      |  ✗      |  ✗     |  ✗        |  ✓    |  ✗     |  ✓
  View platform health analytics      |  ✗      |  ✗     |  ✗        |  ✓    |  ✗     |  ✓
  ─────────────────────────────────────────────────────────────────────────────────────────────
  * PARENT: child's learning progress only — no skill vectors, no scores, no career DNA

DATA PROTECTION:
  ✓ Participant skill vectors encrypted at rest (AES-256)
  ✓ Intelligence profile data: sensitive PII category — encrypted + access-logged
  ✓ Career DNA vector: participant-owned, view-gated by role
  ✓ ML model feature data: no raw PII in feature vectors — UUID references only
  ✓ ERP exports: encrypted in transit (TLS 1.3), encrypted at rest in MinIO
  ✓ AI prompts: anonymized participant context — no real names in prompt payloads

ANALYTICS ABUSE PREVENTION:
  ✓ Bulk export rate limit: max 100 participant records per ERP export request
  ✓ Analytics API rate limit: 1000 requests/minute per tenant
  ✓ Cross-participant data fishing: query filters enforce participant_id scope
    unless ADMIN role with explicit audit log entry
  ✓ Inference attack prevention: cohort analytics suppress results where
    cohort_size < 5 (small group identity inference risk)
```

---

## 8️⃣ AUDIT & TRACEABILITY

```json
AUDIT_LOG_SCHEMA: {
  "audit_id":                    "UUID — primary key",
  "timestamp_utc":               "ISO8601",
  "agent_id":                    "GD_POST_SESSION_ANALYTICS_AGENT",
  "agent_version":               "string",
  "operation_type":              "ENUM[PIPELINE_INITIATED | PIPELINE_COMPLETED |
                                   PIPELINE_FAILED | MODEL_COMPUTED |
                                   INSIGHT_GENERATED | REPORT_DELIVERED |
                                   SCENARIO_RECALIBRATION_TRIGGERED |
                                   MENTOR_DRIFT_ALERT_EMITTED |
                                   AT_RISK_ALERT_EMITTED |
                                   BELT_SIGNAL_EMITTED |
                                   COLLUSION_ESCALATED |
                                   CURRICULUM_FLAG_EMITTED |
                                   ERP_EXPORT_GENERATED |
                                   DUPLICATE_PIPELINE_REJECTED]",
  "tenant_id":                   "UUID",
  "session_id":                  "UUID",
  "analytics_report_id":         "UUID | null",
  "actor_id":                    "UUID — system agent",
  "input_hash":                  "SHA256 — trigger payload hash",
  "output_hash":                 "SHA256 — analytics report hash",
  "ml_models_invoked":           "Array<{model_id, model_version, runtime_ms,
                                          output_summary}>",
  "ai_components_invoked":       "Array<{component_id, prompt_version,
                                          runtime_ms, output_validated}>",
  "pipeline_stage_checkpoints":  "Array<{stage_name, started_at, completed_at,
                                          status, error_if_any}>",
  "confidence_score":            "float",
  "degraded_signals":            "Array<signal_name>",
  "downstream_events_emitted":   "Array<event_type>",
  "failure_reason":              "string | null",
  "escalations_raised":          "Array<{target, reason, severity}>"
}

AUDIT ENFORCEMENT:
  ✓ INSERT-ONLY — no UPDATE or DELETE on audit table ever
  ✓ Encrypted at rest
  ✓ Minimum 7-year retention (outcome validation requires long history)
  ✓ Every ML model invocation logged with version and output hash
  ✓ Every AI generation logged with prompt version
  ✓ Pipeline stage checkpoints enable replay-from-checkpoint on failure
  ✓ All downstream events emitted recorded for dependency tracing
```

---

## 9️⃣ FAILURE POLICY

```
FAILURE SCENARIOS AND MANDATORY RESPONSES:

  ════════════════════════════════════════════════════════
  SCENARIO: DUAL-GATE CONDITION NOT MET (only one event arrived)
  ════════════════════════════════════════════════════════
  Response:
    → WAIT for missing event up to SLA window (300s)
    → If missing event arrives within window: proceed normally
    → If window expires without both events:
        → LOG_INCIDENT (pipeline_gate_timeout — P2)
        → DEAD_LETTER session to gd_analytics_pipeline_dlq
        → ESCALATE_TO: INFRASTRUCTURE_ONCALL_AGENT
        → Notify OBSERVABILITY_AGENT
        → Do NOT proceed with incomplete data

  ════════════════════════════════════════════════════════
  SCENARIO: ATTENDANCE RECORD STATUS = LOW_CONFIDENCE
  ════════════════════════════════════════════════════════
  Response:
    → BLOCK pipeline — do not process low-confidence attendance
    → LOG_INCIDENT (pipeline_blocked_low_confidence_attendance)
    → Queue for retry after human review confirmation arrives
    → Alert TENANT_ADMIN + HUMAN_REVIEW_QUEUE
    → SLA: retry window = 72 hours; after that DEAD_LETTER

  ════════════════════════════════════════════════════════
  SCENARIO: SCORING RECORD STATUS = DISPUTED
  ════════════════════════════════════════════════════════
  Response:
    → BLOCK pipeline — disputed scores cannot feed analytics
    → LOG_INCIDENT (pipeline_blocked_disputed_scoring)
    → Queue for retry after dispute resolution
    → Emit ANALYTICS_DELAYED_EVENT to NOTIFICATION_AGENT
    → No analytics output until scoring is resolved and COMPLETED

  ════════════════════════════════════════════════════════
  SCENARIO: CRITICAL HISTORICAL CONTEXT QUERY FAILS
  (participant_skill_history OR scenario_calibration_history unavailable)
  ════════════════════════════════════════════════════════
  Response:
    → RETRY_POLICY: 3 retries, 2s exponential backoff
    → If all retries fail:
        → HALT pipeline for that session
        → LOG_INCIDENT (critical_context_unavailable — P1)
        → ESCALATE_TO: INFRASTRUCTURE_ONCALL_AGENT
        → DEAD_LETTER with full context for manual review
        → Do NOT produce analytics report without this data

  ════════════════════════════════════════════════════════
  SCENARIO: ENRICHMENT CONTEXT QUERY FAILS
  (intelligence_profile, career_DNA, cohort_baseline unavailable)
  ════════════════════════════════════════════════════════
  Response:
    → CONTINUE pipeline with degraded_signals flag for that dimension
    → Log degraded signal in report: degraded_signals Array
    → Set confidence_score penalty: -0.10 per degraded enrichment source
    → Complete core analytics; omit affected enrichment sections
    → Retry enrichment async after core report delivered
    → Patch report with enrichment data once available (new record version)

  ════════════════════════════════════════════════════════
  SCENARIO: ML MODEL INFERENCE TIMEOUT (> 30s per model)
  ════════════════════════════════════════════════════════
  Response (CORE models — 1, 2, 3, 4, 6, 8):
    → RETRY_POLICY: 2 retries, immediate
    → If still failing: HALT pipeline → LOG → ESCALATE
  Response (ENRICHMENT models — 5, 7):
    → Fall back to previous session's vector (no delta)
    → Flag as degraded_signal
    → Continue pipeline
    → Retry enrichment async

  ════════════════════════════════════════════════════════
  SCENARIO: BELT_PROMOTION_CONFIDENCE >= 0.80 (READY)
  ════════════════════════════════════════════════════════
  Response:
    → Emit BELT_PROMOTION_SIGNAL_EVENT to CERTIFICATE_ENGINE
    → Include full evidence_summary
    → Notify participant + mentor via NOTIFICATION_AGENT
    → LOG_INCIDENT (belt_promotion_signal_emitted — INFO)
    → NOTE: This agent ONLY signals. Auto-promotion FORBIDDEN.
             Mentor board review MANDATORY before any belt change.

  ════════════════════════════════════════════════════════
  SCENARIO: AT_RISK_FLAG = TRUE (3+ consecutive declining sessions)
  ════════════════════════════════════════════════════════
  Response:
    → Emit AT_RISK_LEARNER_ALERT_EVENT to NOTIFICATION_AGENT
    → Trigger AI_2 (intervention recommendation generation)
    → Notify assigned mentor (if any) with mentor-version recommendation
    → Notify LEARNING_PATH_AGENT for immediate path review
    → LOG_INCIDENT (at_risk_learner_identified — P2)
    → Do NOT notify the learner of "at risk" label directly —
      use positive intervention framing only

  ════════════════════════════════════════════════════════
  SCENARIO: MENTOR DRIFT SEVERITY = SEVERE
  ════════════════════════════════════════════════════════
  Response:
    → Emit MENTOR_DRIFT_ALERT_EVENT (SEVERE) immediately
    → Trigger recertification_trigger = TRUE in mentor record
    → Notify MENTOR_CALIBRATION_ENGINE for immediate recalibration
    → Notify GOVERNANCE_AGENT
    → Notify TENANT_ADMIN
    → Certification authority suspended pending recalibration
    → LOG_INCIDENT (severe_mentor_drift — P1)
    → All sessions scored by this mentor in last 30 days flagged for review

  ════════════════════════════════════════════════════════
  SCENARIO: COLLUSION CROSS-ANALYSIS SCORE >= 0.60
  ════════════════════════════════════════════════════════
  Response:
    → Generate fraud_evidence_package
    → Emit COLLUSION_INVESTIGATION_EVENT to COLLUSION_DETECTION_AGENT
    → Emit to COMPLIANCE_AGENT
    → Suspend all pending downstream eligibility signals for implicated
      participants (XP, belt, career DNA) — pending investigation
    → LOG_INCIDENT (collusion_detected — P1)
    → Do NOT alert participants directly — silent investigation

  ════════════════════════════════════════════════════════
  SCENARIO: ANALYTICS CONFIDENCE SCORE < 0.60
  ════════════════════════════════════════════════════════
  Response:
    → Mark report status = LOW_CONFIDENCE
    → Suppress belt_promotion_signal and at_risk_alert
    → Deliver basic analytics (session summary only)
    → ESCALATE_TO: HUMAN_REVIEW_QUEUE
    → LOG_INCIDENT (low_confidence_analytics_report)
    → Full eligibility signals released only after human review

  ════════════════════════════════════════════════════════
  SCENARIO: PIPELINE WORKER CRASH (mid-execution)
  ════════════════════════════════════════════════════════
  Response:
    → Resume from last checkpointed pipeline stage on next worker pick-up
    → If same trigger_id re-consumed: idempotency check → skip completed stages
    → Maximum 3 resume attempts before DEAD_LETTER
    → All completed stages preserved — no recomputation of clean stages
    → LOG_INCIDENT (pipeline_worker_crash — P1)

RETRY_POLICY (general):
  Max retries:     3 for transient failures; 2 for model timeouts
  Backoff:         Exponential — 2s, 4s, 8s
  Non-retryable:   Data integrity failures, security violations,
                   duplicate trigger (idempotency)
  DEAD_LETTER:     After all retries exhausted → park in DLQ +
                   ESCALATE + NOTIFY OBSERVABILITY_AGENT

ESCALATION_TARGETS:
  Gate timeout:               INFRASTRUCTURE_ONCALL_AGENT (P2)
  Critical query failure:     INFRASTRUCTURE_ONCALL_AGENT (P1)
  Severe mentor drift:        MENTOR_CALIBRATION_ENGINE + GOVERNANCE_AGENT (P1)
  Collusion detected:         COLLUSION_DETECTION_AGENT + COMPLIANCE_AGENT (P1)
  Pipeline crash:             INFRASTRUCTURE_ONCALL_AGENT (P1)
  Data integrity failure:     SECURITY_INCIDENT_RESPONSE_AGENT + PLATFORM_ADMIN (P0)
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP

```
UPSTREAM DEPENDENCIES (this agent consumes from):

  GD_ATTENDANCE_TRACKING_AGENT
    Event:    ATTENDANCE_FINALIZED_EVENT
    Data:     Finalized attendance records, ghost flags, fraud signals,
              anomaly flags, per-participant qualification status
    Failure:  BLOCK pipeline if attendance not finalized

  SCORING_ENGINE
    Event:    SCORING_COMPLETED_EVENT
    Data:     Per-participant per-dimension scores, peer scores,
              mentor override records, score confidence
    Failure:  BLOCK pipeline if scoring disputed or incomplete

  GD_SESSION_SCHEDULER_AGENT
    Query:    Session metadata, fairness snapshot, scenario assignment,
              mentor assignment, participant roster
    Failure:  HALT pipeline — no context without session record

  SKILL_VECTOR_STORE
    Query:    Last 10 sessions per participant per domain
    Purpose:  Pre-session skill baseline for delta computation
    Failure:  HALT pipeline (critical context)

  SCENARIO_CATALOG_AGENT
    Query:    Scenario metadata, all historical calibration data,
              rubric version, difficulty history
    Purpose:  Difficulty recalibration, fairness audit
    Failure:  HALT pipeline (critical context)

  MENTOR_CALIBRATION_ENGINE
    Query:    Current calibration score, drift history, last session
    Purpose:  Mentor effectiveness computation
    Failure:  CONTINUE with mentor_calibration = UNAVAILABLE flag

  CERTIFICATE_ENGINE
    Query:    Belt progression history per participant
    Purpose:  Belt promotion confidence scoring
    Failure:  CONTINUE with belt_signal = DEGRADED flag

  INTELLIGENCE_PROFILE_AGENT
    Query:    Current 8-type baseline per participant
    Purpose:  Intelligence enrichment delta computation
    Failure:  CONTINUE with enrichment = DEGRADED flag

  FEATURE_STORE_AGENT
    Query:    Historical behavioral features per participant
    Purpose:  Cohort baseline normalization
    Failure:  CONTINUE with cohort_comparison = UNAVAILABLE flag

  TENANT_CONTEXT_AGENT
    Query:    Tenant plan, institution type, report preferences
    Purpose:  Report format and scope configuration
    Failure:  Use platform defaults; flag as DEFAULT_CONFIG

DOWNSTREAM TRIGGERS (events this agent emits):

  SKILL_VECTOR_STORE
    Event:    SKILL_VECTOR_UPDATE_EVENT
    Payload:  Per-participant skill delta + new post-session vector
    Rule:     Committed synchronously — core pipeline output

  INTELLIGENCE_PROFILE_AGENT
    Event:    INTELLIGENCE_PROFILE_UPDATE_EVENT
    Payload:  8-dimension enrichment vector per participant
    Rule:     Async — non-blocking

  CAREER_DNA_AGENT
    Event:    CAREER_DNA_UPDATE_EVENT
    Payload:  Career alignment delta per participant
    Rule:     Async — non-blocking

  SCENARIO_CATALOG_AGENT
    Event:    SCENARIO_CALIBRATION_UPDATE_EVENT
    Payload:  Recalibration recommendation, retirement signal,
              fairness audit flag
    Rule:     Sync if reclassification_triggered — async otherwise

  MENTOR_CALIBRATION_ENGINE
    Event:    MENTOR_DRIFT_ALERT_EVENT (if drift_detected)
    Payload:  Drift severity, drift type, recalibration recommendation
    Rule:     P1 escalation path if SEVERE

  CERTIFICATE_ENGINE
    Event:    BELT_PROMOTION_SIGNAL_EVENT (if eligible_for_review)
    Payload:  Promotion confidence score, evidence summary, blocking factors
    Rule:     Sync — belt eligibility must be committed before session report delivered

  LEARNING_PATH_AGENT
    Event:    SKILL_GAP_UPDATE_EVENT + AT_RISK_SIGNAL_EVENT (if at_risk)
    Payload:  Skill gaps identified, recommended interventions

  COLLUSION_DETECTION_AGENT
    Event:    COLLUSION_INVESTIGATION_EVENT (if score >= 0.60)
    Payload:  fraud_evidence_package, suspected ring participants

  CURRICULUM_GOVERNANCE_AGENT
    Event:    CURRICULUM_REVIEW_FLAG_EVENT (if curriculum_review_flag)
    Payload:  Underperforming dimensions, drop-off signals, AI recommendation

  TALENT_MARKETPLACE_AGENT
    Event:    SKILL_EVIDENCE_UPDATE_EVENT
    Payload:  Verified skill performance evidence (attestation signature)
    Rule:     Only for RANKED/CERTIFICATION sessions

  GROWTH_ENGINE
    Event:    PERFORMANCE_LINKED_LEARNING_EVENT
    Payload:  Skill delta, breakthrough_flag, learning trajectory

  INSTITUTE_ERP_AGENT / ENTERPRISE_ERP_AGENT
    Event:    ERP_ANALYTICS_READY_EVENT
    Payload:  Cohort performance summary, individual performance summary

  PARENT_VISIBILITY_SERVICE
    Event:    CHILD_PROGRESS_UPDATE_EVENT
    Payload:  Learning trajectory (no scores — progress language only)

  NOTIFICATION_AGENT
    Events:   PERFORMANCE_INSIGHT_READY (per participant),
              AT_RISK_ALERT (mentor/admin version),
              BELT_PROMOTION_SIGNAL (participant + mentor),
              MENTOR_DRIFT_ALERT (admin + governance),
              CURRICULUM_REVIEW_NEEDED (admin)

  OBSERVABILITY_AGENT
    Events:   All pipeline lifecycle events, all model performance metrics,
              all anomaly and escalation events

  FEATURE_STORE_AGENT
    Event:    ANALYTICS_FEATURE_VECTOR_EVENT (see Section 11)

EVENT_SCHEMA (all emitted events):
  {
    "event_id":              "UUID",
    "event_type":            "string",
    "schema_version":        "string",
    "tenant_id":             "UUID",
    "session_id":            "UUID",
    "analytics_report_id":   "UUID",
    "emitted_by":            "GD_POST_SESSION_ANALYTICS_AGENT",
    "agent_version":         "string",
    "timestamp_utc":         "ISO8601",
    "payload":               "object — event-type specific",
    "audit_reference":       "UUID"
  }
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent is the **primary enrichment source** for the FEATURE_STORE_AGENT.
Every completed analytics pipeline emits dense, multi-dimensional feature
vectors that train every ML model on the platform.

```json
EMIT_FEATURE_VECTOR: {
  "user_id":        "UUID — per participant",
  "feature_name":   "gd_post_session_performance_profile",
  "feature_value": {
    "session_type":                   "string",
    "domain_track":                   "string",
    "session_count_total":            "integer — lifetime",
    "session_count_domain":           "integer — this domain",
    "total_score":                    "float",
    "score_by_dimension":             "Map<dimension, score>",
    "score_percentile_cohort":        "float",
    "skill_delta":                    "float — signed",
    "skill_delta_by_dimension":       "Map<dimension, delta>",
    "learning_trajectory":            "string",
    "breakthrough_flag":              "boolean",
    "at_risk_flag":                   "boolean",
    "regression_detected":            "boolean",
    "improvement_rate_last_5":        "float",
    "belt_level_current":             "string",
    "belt_promotion_confidence":      "float",
    "intelligence_enrichment_vector": "Map<type, score_delta>",
    "career_dna_top_alignment":       "Array<career_path>",
    "mentor_present":                 "boolean",
    "scenario_difficulty":            "string",
    "collusion_flag":                 "boolean",
    "session_quality_score":          "float"
  },
  "timestamp_utc":  "ISO8601",
  "source_agent":   "GD_POST_SESSION_ANALYTICS_AGENT",
  "schema_version": "v1.0.0"
}
```

These vectors feed:
- SKILL_DELTA_CALCULATOR retraining (Model 1)
- LEARNING_EFFECTIVENESS_PREDICTOR retraining (Model 2)
- BELT_PROMOTION_CONFIDENCE_SCORER retraining (Model 6)
- CAREER_DNA_VECTOR_UPDATER retraining (Model 7)
- JOB PORTAL matching engine (skill vector → job match score)
- PLACEMENT_PROBABILITY_MODEL (offer acceptance prediction)
- RECRUITER_BEHAVIOR_ANALYTICS (which skill signals attract recruiters)
- TALENT_MARKETPLACE_AGENT (evidence-backed skill claims)

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY

This agent generates the **primary input** for the Innovation Economy layer when
GD sessions are of innovation scenario type. It must emit structured vectors
that enable IDEA_DNA_AGENT, ROYALTY_ENGINE, and COPY_DETECTION_ENGINE to function.

```json
INNOVATION_SESSION_ANALYTICS_EMIT: {
  "session_id":               "UUID",
  "tenant_id":                "UUID",
  "participant_id":           "UUID",
  "domain_track":             "string",
  "scenario_type":            "INNOVATION",
  "originality_signal":       "float 0.0–1.0 — derived from interpersonal +
                                logical + linguistic score combination",
  "idea_quality_vector": {
    "novelty_score":          "float — how different from prior ideas in same domain",
    "feasibility_signal":     "float — logical structure quality proxy",
    "articulation_quality":   "float — linguistic score proxy",
    "peer_engagement_response":"float — how peers reacted to their ideas"
  },
  "idea_vector_eligible":     "boolean — qualified attendance + non-ghost + non-disputed",
  "timestamp_utc":            "ISO8601",
  "trigger_type":             "INNOVATION_SESSION_ANALYTICS_READY",
  "source_agent":             "GD_POST_SESSION_ANALYTICS_AGENT"
}
```

Downstream: IDEA_DNA_AGENT computes IDEA_VECTOR, SIMILARITY_HASH, and
ORIGINALITY_SCORE from this payload. ROYALTY_ENGINE credits innovators using
this as evidence. COPY_DETECTION_ENGINE uses idea_vector for similarity
detection across the platform.

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

This agent feeds the most intelligence-rich signals into the Growth Engine —
connecting real learning performance to platform progression mechanics.

```json
GROWTH_ENGINE_TRIGGERS: {

  "PERFORMANCE_LINKED_LEARNING_EVENT": {
    "participant_id":         "UUID",
    "session_id":             "UUID",
    "tenant_id":              "UUID",
    "skill_delta":            "float",
    "learning_trajectory":    "string",
    "breakthrough_flag":      "boolean",
    "xp_multiplier_signal":   "float — 1.0 base; 1.3 IMPROVING; 2.0 BREAKTHROUGH",
    "performance_event_type": "ENUM[BREAKTHROUGH | IMPROVING | STABLE |
                               DECLINING | FIRST_DOMAIN_SESSION |
                               BELT_THRESHOLD_REACHED]"
  },

  "RANK_UPDATE_EVENT": {
    "participant_id":          "UUID",
    "session_id":              "UUID",
    "tenant_id":               "UUID",
    "new_score_percentile":    "float",
    "rank_change_direction":   "ENUM[UP | DOWN | STABLE]",
    "domain_track":            "string",
    "session_type":            "string — RANKED/TOURNAMENT only"
  },

  "SHARE_TRIGGER_EVENT": {
    "participant_id":          "UUID",
    "session_id":              "UUID",
    "tenant_id":               "UUID",
    "share_prompt_type":       "ENUM[BREAKTHROUGH_PERFORMANCE |
                                SKILL_MILESTONE | BELT_SIGNAL_REACHED |
                                INTELLIGENCE_LEVEL_UP |
                                CAREER_ALIGNMENT_DISCOVERED]"
  }
}

RULES:
  - ghost_flagged participants: NO growth signals emitted
  - collusion_investigation_required: ALL growth signals SUSPENDED
  - BREAKTHROUGH_FLAG = TRUE: 2.0x XP multiplier signal (maximum)
  - DECLINING trajectory: no XP multiplier; growth engine may reduce streak
  - AT_RISK: notification to growth engine to reduce difficulty (not penalize)
  - This agent fires triggers only — GROWTH_ENGINE computes final values
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

```yaml
METRICS (emitted to OBSERVABILITY_AGENT — Prometheus + Grafana):

  Pipeline Health:
    - analytics_pipeline_success_rate               (target: > 99.5%)
    - analytics_pipeline_latency_p50_ms
    - analytics_pipeline_latency_p99_ms             (target: < 300,000ms)
    - pipeline_gate_timeout_rate                    (alert: > 0.5%)
    - pipeline_dead_letter_queue_depth              (alert: > 0)
    - pipeline_retry_rate                           (alert: > 1%)

  ML Model Performance:
    - skill_delta_model_accuracy                    (drift: < 80% target)
    - learning_effectiveness_precision              (at_risk rate target: 10–15%)
    - scenario_recalibration_rate                   (target: < 5% per month)
    - mentor_drift_detection_rate                   (alert: > 10% of mentors drifting)
    - belt_confidence_score_distribution
    - career_dna_update_latency_p99_ms
    - collusion_cross_analysis_flag_rate            (alert: any sustained increase)

  Output Quality:
    - analytics_report_confidence_score_avg         (target: > 0.85)
    - low_confidence_report_rate                    (alert: > 2%)
    - degraded_signal_frequency_by_source
    - at_risk_alert_rate                            (target: 10–15% of users)
    - belt_promotion_signal_rate                    (target: natural distribution)
    - breakthrough_flag_rate                        (target: 5–8% of sessions)

  Downstream Delivery:
    - skill_vector_update_latency_p99_ms            (target: < 60,000ms)
    - intelligence_profile_update_latency_p99_ms
    - erp_report_generation_success_rate            (target: > 99.9%)
    - insight_delivery_latency_p99_ms               (target: < 60,000ms)

  Platform Intelligence:
    - scenario_retirement_triggers_per_month
    - curriculum_review_flags_per_month
    - mentor_recertification_triggers_per_month
    - career_dna_mismatch_flag_rate                 (alert: > 15%)

DASHBOARDS REQUIRED:
  - Analytics Pipeline Health (real-time)
  - Learning Effectiveness Trends (by domain/tenant)
  - At-Risk Learner Volume Over Time
  - Scenario Calibration Status Board
  - Mentor Drift Monitor
  - Belt Promotion Pipeline Health
  - Career DNA Alignment Distribution
  - Collusion Investigation Queue
  - Intelligence Profile Enrichment Coverage

ALERTING:
  - analytics_pipeline_success_rate < 99%          → PagerDuty P2
  - pipeline_dead_letter_queue_depth > 0            → PagerDuty P2
  - severe_mentor_drift detected                    → PagerDuty P1 + Compliance
  - collusion_investigation triggered               → PagerDuty P1 + Compliance
  - at_risk_rate > 20%                              → Slack Curriculum Alert (P2)
  - data_integrity_failure                          → PagerDuty P0
  - scenario_retirement_trigger                     → Slack Curriculum Alert
  - career_dna_mismatch_rate > 15%                  → Slack Product Alert
```

---

## 1️⃣5️⃣ VERSIONING POLICY

```
Current Version:     v1.0.0
Scheme:              MAJOR.MINOR.PATCH

MAJOR bump required when:
  - Pipeline trigger schema changes
  - Analytics report schema fields added or removed
  - Any ML model type or architecture change
  - New downstream agent dependency added
  - Belt promotion confidence threshold changes
  - Skill delta computation formula changes
  - Intelligence profile dimension mapping changes

MINOR bump required when:
  - New optional report fields added
  - New ML model version deployed (same architecture, new training)
  - New enrichment signal added (non-breaking)
  - New ERP export format added
  - New AI component added

PATCH bump required when:
  - Bug fixes with no behavioral change
  - Performance optimizations
  - Metric additions only
  - Logging improvements

BACKWARD COMPATIBILITY:
  - Analytics reports from old versions always readable
  - Skill vector history preserved across model versions
  - Intelligence profile: additive only — no dimension removal
  - Career DNA vector: additive only — no career path removal

ML MODEL VERSIONING:
  - All model versions retained in model registry forever
  - model_version referenced in every report (8 entries for 8 models)
  - Model swap requires deployment changelog entry
  - No silent model version changes — declared in version bump notes
  - Outcome validation requires historical model versions for long-term tracking

BACKWARD COMPATIBILITY WINDOW:
  - Previous MAJOR version: 90-day support window
  - Analytics consumers must upgrade within window
  - Deprecation notices via DEPRECATION_NOTICE_EVENT at MAJOR release

ROLLBACK SAFETY:
  - Pipeline stage checkpoints enable partial rollback
  - Skill vector updates can be reverted via correction overlay
  - Intelligence profile deltas can be reversed on rollback
  - No analytics reports deleted on rollback — old reports retained
```

---

## 1️⃣6️⃣ NON-NEGOTIABLE RULES

```
❌ Processing pipeline without BOTH attendance AND scoring records finalized
❌ Computing skill deltas from ghost-flagged or disputed session data
❌ Allowing AI components to determine skill levels, trajectories, or career paths
❌ Triggering automatic belt promotion — signal only, mentor board decides always
❌ Modifying historical analytics reports directly — new reports only
❌ Deleting or archiving audit log entries under any condition
❌ Emitting growth/belt signals for collusion-investigated participants
❌ Running cohort comparisons with group size < 5 (identity inference risk)
❌ Cross-tenant feature query or analytics computation under any condition
❌ Domain data mixing in intelligence profile enrichment
❌ Suppressing at-risk alerts to protect mentor/admin from difficult conversations
❌ Suppressing collusion escalations for any business reason
❌ Using undeclared ML models or algorithms not specified in this document
❌ Allowing mentor drift to persist without escalation when SEVERE
❌ Allowing scenario difficulty labels to be author-declared without data validation
❌ Retiring scenarios without formal justification report to GOVERNANCE_AGENT
❌ Emitting intelligence profile data to unauthorized role (non-participant)
❌ Using raw PII in AI prompt payloads — UUID references and anonymized context only
❌ Producing partial analytics reports — all-or-nothing per confidence level
❌ Operating outside its declared scope (post-session analytics only)
❌ Emitting talent marketplace skill evidence for PRACTICE sessions
   (RANKED / CERTIFICATION only carry marketplace-grade evidence)
```

---

## 🔒 DATABASE ENTITIES OWNED BY THIS AGENT

```sql
-- ─────────────────────────────────────────────────────────────────
-- MASTER SESSION ANALYTICS REPORTS (IMMUTABLE ON CREATION)
-- ─────────────────────────────────────────────────────────────────
gd_session_analytics_reports (
  analytics_report_id        UUID PRIMARY KEY,
  session_id                 UUID NOT NULL,
  tenant_id                  UUID NOT NULL,
  domain_track               VARCHAR(32) NOT NULL,
  session_type               VARCHAR(32) NOT NULL,
  generated_at_utc           TIMESTAMPTZ NOT NULL,
  pipeline_duration_ms       INTEGER,
  agent_version              VARCHAR(32) NOT NULL,
  ml_models_snapshot         JSONB NOT NULL,
  session_summary            JSONB NOT NULL,
  scenario_calibration_data  JSONB NOT NULL,
  mentor_effectiveness_data  JSONB,
  collusion_cross_reference  JSONB NOT NULL,
  curriculum_intelligence    JSONB,
  confidence_score           FLOAT NOT NULL,
  degraded_signals           JSONB,
  status                     VARCHAR(32) NOT NULL DEFAULT 'COMPLETE',
  created_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW()
  -- INSERT-ONLY — no UPDATE permitted after creation
  -- RLS: WHERE tenant_id = current_tenant()
)

-- ─────────────────────────────────────────────────────────────────
-- PER-PARTICIPANT ANALYTICS RECORDS (IMMUTABLE ON CREATION)
-- ─────────────────────────────────────────────────────────────────
gd_participant_analytics (
  id                          UUID PRIMARY KEY,
  analytics_report_id         UUID NOT NULL REFERENCES gd_session_analytics_reports,
  session_id                  UUID NOT NULL,
  tenant_id                   UUID NOT NULL,
  participant_id              UUID NOT NULL,
  performance_vector          JSONB NOT NULL,
  skill_delta                 JSONB NOT NULL,
  learning_effectiveness      JSONB NOT NULL,
  belt_promotion_signal       JSONB NOT NULL,
  intelligence_enrichment     JSONB,
  career_dna_signal           JSONB,
  comparative_context         JSONB NOT NULL,
  agent_version               VARCHAR(32) NOT NULL,
  created_at                  TIMESTAMPTZ NOT NULL DEFAULT NOW()
  -- INSERT-ONLY
  -- RLS: WHERE tenant_id = current_tenant()
)

-- ─────────────────────────────────────────────────────────────────
-- SKILL VECTOR HISTORY (APPEND-ONLY — each session adds a record)
-- ─────────────────────────────────────────────────────────────────
gd_skill_vectors (
  id                          UUID PRIMARY KEY,
  participant_id              UUID NOT NULL,
  tenant_id                   UUID NOT NULL,
  domain_track                VARCHAR(32) NOT NULL,
  session_id                  UUID NOT NULL,
  analytics_report_id         UUID NOT NULL,
  pre_session_vector          JSONB NOT NULL,
  post_session_vector         JSONB NOT NULL,
  delta_vector                JSONB NOT NULL,
  learning_trajectory         VARCHAR(32) NOT NULL,
  breakthrough_flag           BOOLEAN NOT NULL DEFAULT FALSE,
  model_version               VARCHAR(32) NOT NULL,
  created_at                  TIMESTAMPTZ NOT NULL DEFAULT NOW()
  -- INSERT-ONLY — each session appends; history never deleted
)

-- ─────────────────────────────────────────────────────────────────
-- SCENARIO CALIBRATION HISTORY (APPEND-ONLY PER SESSION)
-- ─────────────────────────────────────────────────────────────────
gd_scenario_calibration_history (
  id                            UUID PRIMARY KEY,
  scenario_id                   UUID NOT NULL,
  tenant_id                     UUID NOT NULL,
  session_id                    UUID NOT NULL,
  analytics_report_id           UUID NOT NULL,
  this_session_pass_rate        FLOAT NOT NULL,
  cumulative_pass_rate          FLOAT NOT NULL,
  score_distribution            JSONB NOT NULL,
  avg_completion_time_seconds   INTEGER,
  abandonment_count             INTEGER NOT NULL DEFAULT 0,
  failure_clustering            JSONB,
  reclassification_triggered    BOOLEAN NOT NULL DEFAULT FALSE,
  recommended_label             VARCHAR(32),
  retirement_signal             BOOLEAN NOT NULL DEFAULT FALSE,
  fairness_audit_flag           BOOLEAN NOT NULL DEFAULT FALSE,
  model_version                 VARCHAR(32) NOT NULL,
  created_at                    TIMESTAMPTZ NOT NULL DEFAULT NOW()
  -- INSERT-ONLY
)

-- ─────────────────────────────────────────────────────────────────
-- MENTOR EFFECTIVENESS RECORDS (APPEND-ONLY PER SESSION)
-- ─────────────────────────────────────────────────────────────────
gd_mentor_effectiveness_records (
  id                              UUID PRIMARY KEY,
  mentor_id                       UUID NOT NULL,
  tenant_id                       UUID NOT NULL,
  session_id                      UUID NOT NULL,
  analytics_report_id             UUID NOT NULL,
  calibration_score               FLOAT,
  score_alignment_with_peers      FLOAT,
  drift_detected                  BOOLEAN NOT NULL DEFAULT FALSE,
  drift_severity                  VARCHAR(32),
  drift_type                      JSONB,
  override_count                  INTEGER NOT NULL DEFAULT 0,
  override_pattern_anomaly        BOOLEAN NOT NULL DEFAULT FALSE,
  favoritism_signal               BOOLEAN NOT NULL DEFAULT FALSE,
  recalibration_recommended       BOOLEAN NOT NULL DEFAULT FALSE,
  recertification_trigger         BOOLEAN NOT NULL DEFAULT FALSE,
  model_version                   VARCHAR(32) NOT NULL,
  created_at                      TIMESTAMPTZ NOT NULL DEFAULT NOW()
  -- INSERT-ONLY
)

-- ─────────────────────────────────────────────────────────────────
-- AT-RISK LEARNER RECORDS
-- ─────────────────────────────────────────────────────────────────
gd_at_risk_records (
  id                           UUID PRIMARY KEY,
  participant_id               UUID NOT NULL,
  tenant_id                    UUID NOT NULL,
  session_id                   UUID NOT NULL,
  analytics_report_id          UUID NOT NULL,
  at_risk_reason               TEXT NOT NULL,
  consecutive_declining_count  INTEGER NOT NULL,
  recommended_intervention     VARCHAR(64) NOT NULL,
  intervention_delivered       BOOLEAN NOT NULL DEFAULT FALSE,
  resolved                     BOOLEAN NOT NULL DEFAULT FALSE,
  resolved_at_utc              TIMESTAMPTZ,
  created_at                   TIMESTAMPTZ NOT NULL DEFAULT NOW()
  -- Updateable ONLY for intervention_delivered and resolved fields
)

-- ─────────────────────────────────────────────────────────────────
-- IMMUTABLE ANALYTICS AUDIT LOG
-- ─────────────────────────────────────────────────────────────────
gd_analytics_audit_log (
  audit_id                     UUID PRIMARY KEY,
  timestamp_utc                TIMESTAMPTZ NOT NULL,
  agent_id                     VARCHAR(64) NOT NULL,
  agent_version                VARCHAR(32) NOT NULL,
  operation_type               VARCHAR(64) NOT NULL,
  tenant_id                    UUID NOT NULL,
  session_id                   UUID,
  analytics_report_id          UUID,
  actor_id                     UUID NOT NULL,
  input_hash                   VARCHAR(64) NOT NULL,
  output_hash                  VARCHAR(64),
  ml_models_invoked            JSONB NOT NULL,
  ai_components_invoked        JSONB,
  pipeline_stage_checkpoints   JSONB NOT NULL,
  confidence_score             FLOAT,
  degraded_signals             JSONB,
  downstream_events_emitted    JSONB,
  failure_reason               TEXT,
  escalations_raised           JSONB
  -- INSERT-ONLY — no UPDATE or DELETE ever
  -- Encrypted at rest
  -- Partitioned by tenant_id + created_month
)
```

---

## 🔒 API CONTRACT

```yaml
API ENDPOINTS (FastAPI — versioned):

  GET  /api/v1/analytics/sessions/{session_id}/report
    Auth:      JWT Bearer (MENTOR, EVALUATOR, ADMIN)
    Purpose:   Full session analytics report
    Output:    SESSION_ANALYTICS_REPORT

  GET  /api/v1/analytics/participants/{participant_id}/performance
    Auth:      JWT Bearer (own record: any role | others: MENTOR/ADMIN)
    Query:     domain_track, session_type, from_utc, to_utc, page, limit
    Output:    Array<PARTICIPANT_ANALYTICS_SUMMARY>

  GET  /api/v1/analytics/participants/{participant_id}/skill-vector
    Auth:      JWT Bearer (own: any | others: ADMIN only)
    Query:     domain_track, from_utc, to_utc
    Output:    SKILL_VECTOR_HISTORY

  GET  /api/v1/analytics/participants/{participant_id}/intelligence-profile
    Auth:      JWT Bearer (own: any | others: ADMIN only)
    Output:    INTELLIGENCE_PROFILE_DELTA_HISTORY

  GET  /api/v1/analytics/participants/{participant_id}/career-dna
    Auth:      JWT Bearer (own: any | others: ADMIN only)
    Output:    CAREER_DNA_VECTOR_HISTORY

  GET  /api/v1/analytics/cohorts/{cohort_id}/performance
    Auth:      JWT Bearer (MENTOR, ADMIN roles — tenant-scoped)
    Output:    COHORT_PERFORMANCE_ANALYTICS

  GET  /api/v1/analytics/scenarios/{scenario_id}/calibration
    Auth:      JWT Bearer (ADMIN role only)
    Output:    SCENARIO_CALIBRATION_HISTORY

  GET  /api/v1/analytics/mentors/{mentor_id}/effectiveness
    Auth:      JWT Bearer (EVALUATOR, ADMIN roles only)
    Output:    MENTOR_EFFECTIVENESS_HISTORY

  GET  /api/v1/analytics/sessions/{session_id}/audit
    Auth:      JWT Bearer (ADMIN role only)
    Output:    Array<ANALYTICS_AUDIT_LOG_ENTRY>

  POST /api/v1/analytics/exports/erp
    Auth:      JWT Bearer (ADMIN role only)
    Input:     {tenant_id, institution_id?, cohort_id?, from_utc, to_utc,
                report_type: ENUM[PERFORMANCE | LEARNING | CAREER], format}
    Output:    ERP_EXPORT_JOB_REFERENCE (async)

  GET  /api/v1/analytics/platform/health
    Auth:      JWT Bearer (PLATFORM_ADMIN role only)
    Purpose:   Platform-wide analytics health (aggregated, no PII)
    Output:    PLATFORM_ANALYTICS_HEALTH_SUMMARY

RATE LIMITS:
  GET  /sessions/*/report:         1000 req/min per tenant
  GET  /participants/*/performance: 2000 req/min per tenant
  GET  /cohorts/*/performance:      200 req/min per tenant
  GET  /scenarios/*/calibration:    100 req/min per tenant
  POST /exports/erp:                10 req/hour per tenant
  GET  /platform/health:            60 req/hour (platform admin)
```

---

## 🔒 OUTCOME VALIDATION LOCK (SECTION T13 COMPLIANCE)

Per Dojo SaaS Spec Section T13, skill belts must have predictive validity
tracking. This agent is the **primary engine** for that validation:

```
OUTCOME VALIDATION REQUIREMENTS:

  employer_feedback_signals:
    Source:     TALENT_MARKETPLACE_AGENT (employer ratings post-hire)
    Stored in:  gd_participant_analytics (career_dna_signal.career_alignment_delta)
    Purpose:    Validate that belt level correlates with employer satisfaction

  job_performance_correlation:
    Source:     ENTERPRISE_ERP_AGENT (6-month, 12-month performance reviews)
    Computation: Correlation between belt_level at hire + 6-month review score
    Purpose:    Prove belt → real-world performance validity

  hiring_outcome_mapping:
    Source:     JOB_PORTAL_ENGINE (offer_accepted, position_secured)
    Stored in:  gd_skill_vectors (post_session_vector → career_dna_top_alignment)
    Purpose:    Validate that Career DNA predictions match actual hiring outcomes

  longitudinal_skill_performance:
    Source:     gd_skill_vectors history (rolling 2-year window per participant)
    Computation: Regression on skill_delta trajectory → career outcome
    Purpose:    Validate learning effectiveness model over time

BELT VALIDITY TRACKING:
  - Every belt awarded → outcome_tracking_record created
  - 6-month review: compare belt_holder performance vs. non-belt holders
  - 12-month review: longitudinal performance validation
  - Results feed back into BELT_PROMOTION_CONFIDENCE_SCORER training
  - Annual validity report generated for GOVERNANCE_BOARD
```

---

## 🔒 DOJO INTEGRATION LOCKS (INHERITED)

```
✓ Scoring governance: Analytics NEVER modifies scores —
  reads from SCORING_ENGINE only, immutably
✓ Belt promotion: AUTO-PROMOTION FOREVER FORBIDDEN —
  this agent emits signal only; mentor board decides
✓ Scenario difficulty: MUST be data-derived from this agent's calibration —
  author-declared labels forbidden after 30+ sessions of data
✓ Rater calibration: MENTOR_CALIBRATION_ENGINE is authoritative —
  this agent feeds drift data; calibration actions owned by that engine
✓ Content governance: Scenario retirement recommendations go through
  CURRICULUM_GOVERNANCE_AGENT — not executed directly by this agent
✓ Assessment validity: All constructs validated by T1–T2 framework
  (skill construct mapping → observable behavior → measurable indicator)
✓ Collusion/manipulation detection: Cross-referenced per T9 framework
  (reciprocal scoring, farming patterns, rating inflation)
✓ Behavioral safety: FORCED_EXIT sessions excluded from performance analytics
  unless ADMIN review confirms data validity
✓ Accessibility: Intelligence profile enrichment signals available in
  accessible format (screen reader compatible) for T11 compliance
✓ Localization: Regional performance variance flagged in scenario calibration
  for T12 cultural fairness compliance
```

---

## ✅ FINAL GOVERNANCE SEAL

```
╔══════════════════════════════════════════════════════════════════════════════╗
║             GD POST-SESSION ANALYTICS AGENT                                 ║
║             ECOSKILLER ANTIGRAVITY v1.0.0                                   ║
║                                                                             ║
║  GOVERNANCE STATUS: FULLY SEALED                                            ║
║                                                                             ║
║  ✓ Agent Identity Locked              ✓ Purpose Declaration Complete        ║
║  ✓ Input Contract Sealed (Dual-Gate)  ✓ Output Contract Sealed (Master+Sub) ║
║  ✓ 8 ML Models Governed               ✓ 5 AI Components Governed            ║
║  ✓ Scalability Design Defined         ✓ Security Zero-Trust Enforced        ║
║  ✓ Audit Trail Immutable              ✓ Failure Policy (12 scenarios)       ║
║  ✓ Inter-Agent Map Complete (16 up)   ✓ Passive Intelligence Compatible     ║
║  ✓ Innovation Economy Compatible      ✓ Growth Engine Hooks Active          ║
║  ✓ Performance Monitoring Defined     ✓ Versioning Policy Locked            ║
║  ✓ 20 Non-Negotiable Rules Sealed     ✓ Database Schema Owned (7 tables)    ║
║  ✓ API Contract Defined (11 endpoints)✓ Outcome Validation Locked (T13)     ║
║  ✓ Dojo Integration Locks Inherited   ✓ Career DNA Pipeline Active          ║
║  ✓ Intelligence Profile Engine Active ✓ Scenario Calibration Engine Active  ║
║  ✓ Mentor Drift Detection Active      ✓ Collusion Cross-Analysis Active     ║
║  ✓ Curriculum Intelligence Active     ✓ Belt Confidence Scorer Active       ║
║  ✓ At-Risk Learner Detection Active   ✓ ERP Analytics Ready                 ║
║  ✓ Talent Marketplace Compatible      ✓ Parent Visibility Compliant         ║
║                                                                             ║
║  DOJO SAAS PRODUCTION MODE: ENABLED                                        ║
║  ECOSKILLER ANTIGRAVITY: ACTIVE                                             ║
║  ZERO-TRUST SECURITY: ENFORCED                                              ║
║  APPEND-ONLY AUDIT: ENFORCED                                                ║
║  DUAL-GATE PIPELINE TRIGGER: ENFORCED                                       ║
║  AUTO-BELT-PROMOTION: PERMANENTLY FORBIDDEN                                 ║
║  SCENARIO DIFFICULTY: DATA-DERIVED ONLY                                     ║
║  AI DECISION AUTONOMY: FORBIDDEN                                            ║
║  MUTATION POLICY: ADD-ONLY VERSIONED                                        ║
║  INTERPRETATION AUTHORITY: NONE                                             ║
║  ARCHITECTURE AUTHORITY: LOCKED                                             ║
║                                                                             ║
║  Missing prerequisite events → BLOCK pipeline                              ║
║  Ghost/disputed data in pipeline → HALT                                    ║
║  Auto-promotion signal → FORBIDDEN                                         ║
║  Silent failure → FORBIDDEN                                                 ║
║  Partial output → FORBIDDEN                                                 ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

*Document Version: v1.0.0 | Platform: Ecoskiller Antigravity*
*Agent: GD_POST_SESSION_ANALYTICS_AGENT (EKAG-GDPSA-003)*
*Hard Dependencies: GD_ATTENDANCE_TRACKING_AGENT v1.0.0+ | SCORING_ENGINE v1.0.0+*
*Soft Dependencies: SKILL_VECTOR_STORE | SCENARIO_CATALOG_AGENT | MENTOR_CALIBRATION_ENGINE*
*Mutation Policy: Add-only via version bump | Seal Date: 2026-02-27*
*Governed by: ECOSKILLER INTELLIGENCE & INNOVATION CORE*
