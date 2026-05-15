# 🔒 SEALED & LOCKED AGENT SPECIFICATION
# REFLECTIVE DEPTH ANALYZER AGENT
## Ecoskiller Antigravity — Intrapersonal Intelligence & Metacognitive Measurement Core
---

```
DOCUMENT_CLASS                  = PRODUCTION AGENT SPECIFICATION
ARTIFACT_VERSION                = v1.0.0
MUTATION_POLICY                 = ADD-ONLY (versioned bump mandatory for any change)
EXECUTION_MODE                  = DETERMINISTIC + VALIDATED
CREATIVE_INTERPRETATION         = FORBIDDEN
ASSUMPTION_FILLING              = FORBIDDEN
DEFAULT_BEHAVIOR                = DENY
FAILURE_MODE                    = HALT ON AMBIGUITY
CROSS_TENANT_ACCESS             = FORBIDDEN
AUTONOMOUS_SCORE_AWARD          = FORBIDDEN
SILENT_FAILURE                  = FORBIDDEN
PARTIAL_OUTPUT                  = FORBIDDEN
SEAL_STATUS                     = LOCKED
```

---

## 🧬 SECTION 1 — AGENT IDENTITY (NON-NEGOTIABLE)

```yaml
AGENT_NAME:               REFLECTIVE_DEPTH_ANALYZER_AGENT
AGENT_ID:                 ECO-AG-RDA-003
AGENT_CLASS:              Multi-Modal NLP + Behavioral ML Agent (Primary)
                          + LLM Semantic Assist (Secondary, governed)
SYSTEM_ROLE:              Intrapersonal Intelligence Measurement Engine —
                          Deep Metacognition Analyzer, Self-Reflection Quality Scorer,
                          Reasoning Transparency Detector, and Cognitive Self-Model Tracker
PRIMARY_DOMAIN:           Intrapersonal Intelligence | Metacognitive Depth |
                          Self-Assessment Accuracy | Reflective Practice Quality |
                          Thought Articulation Analysis | Emotional Regulation Signals |
                          Cognitive Consistency Tracking
EXECUTION_MODE:           Deterministic + Validated
DATA_SCOPE:
  Primary:
    - Written reflection submissions (free-text, structured prompts, journals)
    - Verbal reflection transcripts (Dojo match post-match analysis, live reasoning narration)
    - Self-assessment scores submitted by user (vs. peer/mentor scores)
    - Goal-setting records (goal clarity, goal commitment, goal revision patterns)
    - Metacognitive annotations during assessment (think-aloud, strategy narration)
    - Reasoning logs from Dojo scenarios (structured argument construction)
    - Career decision logs (decision quality signals from Integration System)
  Secondary:
    - Longitudinal behavioral signals from FEATURE_STORE_AGENT
    - Session engagement metadata (depth of engagement vs. surface browsing)
    - Temporal consistency patterns (stated beliefs vs. observed actions)
TENANT_SCOPE:             STRICT ISOLATION — cross-tenant data access FORBIDDEN
FAILURE_POLICY:           HALT ON AMBIGUITY
                          — No inference from incomplete or ambiguous input
                          — No partial scoring on failed validation
PLATFORM:                 Ecoskiller Antigravity
ARCHITECTURE:             Microservices + Event-Driven
ML_RATIO:                 70% Traditional ML + NLP (classification, regression, clustering)
AI_RATIO:                 30% LLM-Assisted (semantic depth parsing, qualitative scoring,
                          narrative quality analysis — governed, no decision autonomy)
SECURITY_MODEL:           Zero-Trust Multi-Tenant
DATA_POLICY:              Append-Only Audit Trail
DOMAIN_TRACKS:            Arts | Commerce | Science | Technology | Administration
INTELLIGENCE_DIMENSION:   Intrapersonal (PRIMARY — this agent's exclusive domain)
                          — Cross-dimensional signals from Interpersonal permitted
                            as secondary contextual input only
```

---

## 🎯 SECTION 2 — PURPOSE DECLARATION

### 2.1 The Core Problem: Measuring the Invisible

Every intelligence dimension on Ecoskiller has a reasonably observable measurement pathway: Logical-Mathematical through problem-solving, Linguistic through writing quality, Spatial through design and visualization, Musical through compositional output. But **Intrapersonal Intelligence** — the capacity to understand oneself deeply, to think about one's own thinking, to accurately model one's own cognitive states, emotional patterns, strengths, and limitations — is the hardest of all nine dimensions to measure objectively.

A shallow system would measure this with a simple self-report questionnaire. Ecoskiller's architecture demands better. The **REFLECTIVE_DEPTH_ANALYZER_AGENT** is the engine that transforms written reflections, verbal reasoning transcripts, self-assessment accuracy data, and metacognitive behavioral patterns into a structured, longitudinally-tracked **Intrapersonal Intelligence Score** — one that is explainable, versioned, auditable, fair, and built for a population of 10M to 100M users.

This agent answers questions that no other agent in the ecosystem can answer:

- Is this user developing genuine self-insight, or only performing self-reflection?
- How accurately does this user's self-assessment align with external evaluations?
- Is the depth of this user's reasoning increasing over time across scenarios?
- Can this user articulate their own decision-making process in a structured way?
- Does this user demonstrate consistent cognitive self-models, or fragmented, inconsistent ones?
- Is this user's stated goal-setting behavior aligned with their actual learning behaviors?
- Is this user capable of identifying and naming their own cognitive biases under pressure?

### 2.2 The Intrapersonal Intelligence Construct (Measurement Foundation)

This agent measures Intrapersonal Intelligence along **eight observable sub-dimensions**. These sub-dimensions are locked for v1.0.0 and may only be extended (not mutated) via MINOR version bump:

```yaml
INTRAPERSONAL_SUB_DIMENSIONS:
  RD1 — SELF_ASSESSMENT_ACCURACY:
    Definition:   Calibration between user's self-scored ability and external
                  (mentor/peer/system) scored ability
    Observable:   Self-score vs. actual-score delta across multiple events
    Signal_type:  Quantitative (delta computation)

  RD2 — METACOGNITIVE_DEPTH:
    Definition:   Quality and sophistication of a user's reasoning about their
                  own reasoning processes — do they explain WHY, not just WHAT?
    Observable:   Reflection text analysis — presence of meta-level statements,
                  strategy discussion, process narration
    Signal_type:  NLP + LLM-governed qualitative scoring

  RD3 — REFLECTIVE_ELABORATION:
    Definition:   Degree to which the user constructs rich, nuanced, multi-perspective
                  explanations of their experiences versus surface-level summaries
    Observable:   Text depth analysis — sentence complexity, argument layering,
                  counter-argument acknowledgment
    Signal_type:  NLP + structural analysis

  RD4 — COGNITIVE_CONSISTENCY:
    Definition:   Degree to which stated beliefs, self-assessments, and goals
                  are consistent with observed behavioral patterns over time
    Observable:   Cross-signal temporal consistency (stated vs. enacted)
    Signal_type:  ML consistency model (behavioral vs. declarative alignment)

  RD5 — EMOTIONAL_REGULATION_SIGNAL:
    Definition:   Presence of stable, regulated emotional framing in reflection —
                  neither suppressed nor dysregulated; indicators of emotional insight
    Observable:   Affect markers in text (sentiment arc, regulation language patterns)
    Signal_type:  NLP sentiment + regulation vocabulary classifier
    Note:         This is NOT a mental health assessment tool — it is a cognitive
                  framing analysis only. Clinical interpretation is FORBIDDEN.

  RD6 — GOAL_CLARITY_AND_COMMITMENT:
    Definition:   Quality and specificity of goal-setting articulations —
                  measurability, timeframes, revisions, follow-through signals
    Observable:   Goal record text + goal achievement behavioral data
    Signal_type:  NLP specificity scoring + behavioral completion tracking

  RD7 — REASONING_TRANSPARENCY:
    Definition:   Degree to which the user can expose and explain their own
                  decision-making pathway in structured reasoning tasks
    Observable:   Dojo scenario argumentation logs, structured reasoning narrations
    Signal_type:  Argument structure analysis (claim → evidence → warrant detection)

  RD8 — SELF_MODEL_COHERENCE:
    Definition:   Longitudinal consistency and evolution of the user's self-concept —
                  does their self-understanding grow in structured, coherent ways,
                  or is it fragmented and contradictory?
    Observable:   Cross-session comparison of self-description language,
                  belief statements, and identity anchors
    Signal_type:  Embedding-based semantic consistency tracking (longitudinal)
```

### 2.3 What This Agent Does NOT Do

```yaml
THIS_AGENT_DOES_NOT:
  - Provide psychological diagnosis, mental health assessment, or clinical evaluation
  - Predict personality types or psychiatric categories
  - Access or analyze private communications not submitted as platform content
  - Override Dojo Scoring Engine scores
  - Award belts or certifications autonomously
  - Interpret cultural expression styles as intelligence deficits
  - Generate scores from users who have not provided analyzable content
  - Share individual reflection content with recruiters (aggregate only)
  - Expose raw reflection text to any downstream agent (processed vectors only)
```

### 2.4 Input Consumed

| Input Source | Data Type | Trigger |
|---|---|---|
| REFLECTION_SUBMISSION_SERVICE | Structured + free-text reflection | On submission |
| DOJO_SCORING_AGENT | Post-match verbal analysis transcript | On match completion |
| ASSESSMENT_ENGINE_AGENT | Self-score vs. system-score delta | On assessment completion |
| GOAL_TRACKING_SERVICE | Goal records + achievement signals | On goal event |
| FEATURE_STORE_AGENT | Behavioral consistency vectors | Batch 5-min |
| METACOGNITION_PROMPT_ENGINE | Prompted metacognitive response | On prompt completion |
| BATCH_SCHEDULER | Longitudinal coherence recalculation | Nightly 03:00 UTC |

### 2.5 Upstream Agents

```
UPSTREAM_AGENTS:
  - REFLECTION_SUBMISSION_SERVICE        (primary text input)
  - DOJO_SCORING_AGENT                   (verbal reasoning transcripts)
  - ASSESSMENT_ENGINE_AGENT              (self-score deltas)
  - GOAL_TRACKING_SERVICE                (goal quality and follow-through)
  - FEATURE_STORE_AGENT                  (behavioral consistency signals)
  - METACOGNITION_PROMPT_ENGINE          (structured metacognitive prompts)
  - INTELLIGENCE_GROWTH_TIME_SERIES_AGENT (growth context for longitudinal model)
  - BATCH_SCHEDULER                      (nightly coherence recalculation)
```

### 2.6 Downstream Agents

```
DOWNSTREAM_AGENTS:
  - INTELLIGENCE_GROWTH_TIME_SERIES_AGENT  (consumes RDA score as input signal)
  - POPULATION_PERCENTILE_ENGINE_AGENT     (consumes RDA score for Intrapersonal percentile)
  - AI_MENTOR_AGENT                        (consumes depth profile + gap analysis)
  - PARENT_INSIGHT_AGENT                   (age-appropriate growth narrative)
  - INSTITUTE_ANALYTICS_AGENT              (cohort reflective depth distribution)
  - RECRUITER_INTELLIGENCE_AGENT           (aggregate intrapersonal signal — consent gated)
  - FEATURE_STORE_AGENT                    (receives emitted reflection feature vectors)
  - OBSERVABILITY_AGENT                    (anomaly + drift signals)
  - ALERT_SERVICE_AGENT                    (regression, stagnation, consistency collapse alerts)
  - RANK_ENGINE_AGENT                      (growth triggers → XP events)
  - TRUST_SYSTEM_AGENT                     (consistency collapse → fraud proximity signal)
```

---

## 📥 SECTION 3 — INPUT CONTRACT (STRICT)

### 3.1 Trigger Types

This agent accepts **four distinct trigger types**. Wrong trigger type against wrong schema = IMMEDIATE REJECTION.

---

#### TRIGGER TYPE 1 — REFLECTION TEXT SUBMISSION

```json
INPUT_SCHEMA_REFLECTION: {
  "required_fields": [
    "event_id",
    "trigger_type",
    "user_id",
    "tenant_id",
    "submission_type",
    "submission_text",
    "reflection_prompt_id",
    "submission_timestamp_utc",
    "domain_track",
    "source_agent_id",
    "language_code",
    "consent_for_analysis"
  ],
  "optional_fields": [
    "session_id",
    "match_id",
    "scenario_id",
    "assessment_id",
    "word_count_declared",
    "user_declared_confidence",
    "reflection_target",
    "parent_reflection_id"
  ],
  "validation_rules": [
    "trigger_type must equal 'REFLECTION_SUBMISSION'",
    "event_id must be UUID v4 — idempotency key",
    "user_id must be UUID v4",
    "tenant_id must match JWT claim",
    "submission_type must be in [POST_MATCH_ANALYSIS, DAILY_JOURNAL, GOAL_SETTING,\
     SKILL_REFLECTION, CAREER_REFLECTION, SCENARIO_DEBRIEF, MENTOR_PROMPTED,\
     FREE_FORM_REFLECTION, STRUCTURED_METACOGNITIVE_PROMPT]",
    "submission_text must be non-empty string",
    "submission_text length must be >= 50 characters (below = insufficient_content flag)",
    "submission_text length must be <= 15000 characters (above = truncation + flag)",
    "reflection_prompt_id must reference an existing, active prompt in PROMPT_REGISTRY",
    "submission_timestamp_utc must be ISO 8601",
    "submission_timestamp_utc must not be future-dated",
    "domain_track must be in [Arts, Commerce, Science, Technology, Administration]",
    "language_code must be valid BCP-47 language tag",
    "consent_for_analysis must be boolean true — reject if false",
    "Duplicate event_id: skip processing (idempotency guard)"
  ],
  "security_checks": [
    "Validate tenant_id against JWT",
    "Validate user_id belongs to tenant_id via row-level security",
    "Validate source_agent_id is registered upstream",
    "consent_for_analysis must be true — never process without explicit consent",
    "Scan submission_text for PII injection attempts (email, phone, Aadhaar patterns)",
    "Scan submission_text for XSS/injection payloads (sanitize before NLP)",
    "Reject if event_id already processed (Redis dedup, TTL 48h)"
  ],
  "domain_checks": [
    "domain_track must match user's registered domain or granted secondary domain",
    "reflection_prompt_id must be active and compatible with submission_type",
    "If match_id provided: verify match belongs to user and is finalized"
  ]
}
```

---

#### TRIGGER TYPE 2 — SELF-ASSESSMENT DELTA EVENT

```json
INPUT_SCHEMA_SELF_DELTA: {
  "required_fields": [
    "event_id",
    "trigger_type",
    "user_id",
    "tenant_id",
    "assessment_id",
    "dimension",
    "user_self_score",
    "system_actual_score",
    "assessment_timestamp_utc",
    "source_agent_id"
  ],
  "optional_fields": [
    "user_confidence_in_self_score",
    "assessment_type",
    "scenario_difficulty_level",
    "mentor_score",
    "peer_score_avg"
  ],
  "validation_rules": [
    "trigger_type must equal 'SELF_ASSESSMENT_DELTA'",
    "user_self_score must be Float in [0.0, 100.0]",
    "system_actual_score must be Float in [0.0, 100.0]",
    "dimension must be in the defined 9-dimension enum",
    "assessment_timestamp_utc must be ISO 8601 and not future-dated",
    "user_confidence_in_self_score: if provided, must be Float in [0.0, 1.0]"
  ],
  "security_checks": [
    "Validate tenant_id against JWT",
    "Validate user_id belongs to tenant_id",
    "Validate assessment_id references a completed assessment owned by user_id",
    "Reject if system_actual_score is not from a verified scoring agent"
  ],
  "domain_checks": [
    "dimension must be active and mapped to a valid construct",
    "If mentor_score provided: verify mentor is authorized and certified for this assessment"
  ]
}
```

---

#### TRIGGER TYPE 3 — GOAL QUALITY EVENT

```json
INPUT_SCHEMA_GOAL: {
  "required_fields": [
    "event_id",
    "trigger_type",
    "user_id",
    "tenant_id",
    "goal_id",
    "goal_event_type",
    "goal_text",
    "goal_timestamp_utc",
    "source_agent_id"
  ],
  "optional_fields": [
    "target_date",
    "target_metric",
    "target_dimension",
    "progress_percent",
    "completion_flag",
    "revision_count",
    "original_goal_id"
  ],
  "validation_rules": [
    "trigger_type must equal 'GOAL_EVENT'",
    "goal_event_type must be in [GOAL_CREATED, GOAL_REVISED, GOAL_ACHIEVED,\
     GOAL_ABANDONED, GOAL_PROGRESS_UPDATE]",
    "goal_text must be non-empty, length >= 20 characters",
    "goal_text must be <= 2000 characters",
    "goal_timestamp_utc must be ISO 8601 and not future-dated",
    "If GOAL_REVISED: original_goal_id must be provided and exist",
    "If GOAL_PROGRESS_UPDATE: progress_percent must be Float in [0.0, 100.0]"
  ],
  "security_checks": [
    "Validate tenant_id against JWT",
    "Validate user_id belongs to tenant_id",
    "Validate goal_id belongs to user_id (no cross-user goal inspection)",
    "Reject if event_id already processed (dedup)"
  ],
  "domain_checks": [
    "If target_dimension provided: must be in valid dimension enum"
  ]
}
```

---

#### TRIGGER TYPE 4 — BATCH LONGITUDINAL COHERENCE

```json
INPUT_SCHEMA_BATCH: {
  "required_fields": [
    "event_id",
    "trigger_type",
    "tenant_id",
    "batch_date_utc",
    "scope",
    "scheduler_auth_token"
  ],
  "optional_fields": [
    "user_id_list",
    "cohort_id_filter",
    "domain_track_filter"
  ],
  "validation_rules": [
    "trigger_type must equal 'BATCH_COHERENCE'",
    "scope must be in [FULL_TENANT, COHORT, USER_LIST]",
    "batch_date_utc must be ISO 8601 within 24h of trigger",
    "scheduler_auth_token must be cryptographically verified",
    "If USER_LIST scope: user_id_list must be non-empty array of valid UUIDs",
    "Maximum user_id_list size: 10,000 per batch job"
  ],
  "security_checks": [
    "scheduler_auth_token verified against internal scheduler signing key",
    "FULL_TENANT scope requires TENANT_ADMIN authorization",
    "USER_LIST scope: all user_ids must belong to tenant_id"
  ],
  "domain_checks": [
    "If domain_track_filter provided: must be valid domain enum"
  ]
}
```

### 3.2 Input Rejection Policy

```yaml
REJECT_ON:
  - Any required field missing or null
  - Malformed UUID (any field)
  - submission_text empty or < 50 characters (INSUFFICIENT_CONTENT — not a hard reject,
    returns structured flag instead of processing)
  - consent_for_analysis = false (HARD REJECT — never process without consent)
  - Unknown trigger_type
  - Future-dated timestamps
  - Duplicate event_id (idempotency guard)
  - Unregistered source_agent_id
  - Unverified scheduler token (batch trigger)
  - Domain isolation violation
  - PII injection pattern detected in submission_text
  - XSS payload detected in submission_text

ON_REJECT:
  ACTION:              STOP_EXECUTION immediately
  LOG:                 LOG_VALIDATION_FAILURE (immutable audit)
  EMIT:                Structured rejection event to OBSERVABILITY_AGENT + source agent
  RESPONSE:            Structured rejection object (no partial output)
  PII_DETECTION:       Log PII detection event → trigger PII_SCRUB_SERVICE
                       Do NOT store original text containing PII

REJECTION_OBJECT: {
  "rejection_id":       "UUID",
  "timestamp_utc":      "ISO 8601",
  "event_id":           "original event_id",
  "input_hash":         "SHA-256 of input payload",
  "trigger_type":       "string",
  "rejection_reason":   "string",
  "field_violations":   ["list"],
  "pii_detected":       "boolean",
  "consent_missing":    "boolean",
  "audit_reference":    "UUID"
}
```

---

## 📤 SECTION 4 — OUTPUT CONTRACT (STRICT)

### 4.1 Primary Score Output (per submission or batch user)

```json
OUTPUT_SCHEMA_SCORE: {
  "result_object": {
    "user_id":                         "UUID",
    "tenant_id":                       "UUID",
    "domain_track":                    "string",
    "intelligence_dimension":          "Intrapersonal",
    "analysis_source":                 "string (trigger_type that produced this output)",
    "event_id_source":                 "UUID",

    "sub_dimension_scores": {
      "RD1_self_assessment_accuracy":  "Float [0-100] | null",
      "RD2_metacognitive_depth":       "Float [0-100] | null",
      "RD3_reflective_elaboration":    "Float [0-100] | null",
      "RD4_cognitive_consistency":     "Float [0-100] | null",
      "RD5_emotional_regulation":      "Float [0-100] | null",
      "RD6_goal_clarity_commitment":   "Float [0-100] | null",
      "RD7_reasoning_transparency":    "Float [0-100] | null",
      "RD8_self_model_coherence":      "Float [0-100] | null"
    },

    "composite_intrapersonal_score":   "Float [0-100]",
    "composite_confidence":            "Float [0-1]",

    "score_basis": {
      "sub_dimensions_computed":       "int (how many of 8 had sufficient data)",
      "minimum_required":              "int (4 required for composite — else INSUFFICIENT)",
      "composite_method":              "WEIGHTED_AVERAGE | INSUFFICIENT_DATA",
      "weights_version":               "semver (scoring weight config version)"
    },

    "depth_classification":            "SURFACE | DEVELOPING | SUBSTANTIVE | DEEP | TRANSFORMATIVE",
    "depth_classification_previous":   "string | null",
    "depth_changed":                   "boolean",

    "calibration_profile": {
      "over_estimation_tendency":      "Float (positive = over-estimates self)",
      "under_estimation_tendency":     "Float (positive = under-estimates self)",
      "calibration_stability_30d":     "Float (0=unstable, 1=stable)"
    },

    "longitudinal_signals": {
      "reflection_velocity_30d":       "Float (submissions per week)",
      "depth_trend_30d":               "IMPROVING | STABLE | DECLINING",
      "consistency_trend_90d":         "IMPROVING | STABLE | DECLINING",
      "goal_achievement_rate_90d":     "Float [0-1] | null"
    },

    "anomaly_flags":                   ["list of AnomalyFlag objects"],
    "quality_warnings":                ["list of QualityWarning objects"],

    "gap_analysis": {
      "weakest_sub_dimension":         "string (RD code)",
      "weakest_score":                 "Float",
      "recommended_focus":             "string (RD label)",
      "gap_to_next_depth_band":        "Float"
    },

    "privacy_controls": {
      "raw_text_stored":               "boolean (true only if consent_for_storage=true)",
      "raw_text_accessible_to":        ["list of authorized roles"],
      "vector_representation_only":    "boolean",
      "recruiter_accessible":          "boolean (false unless candidate consent)"
    },

    "rank_trigger":                    "RANK_UPDATE_EVENT | null",
    "xp_trigger":                      "XP_UPDATE_EVENT | null"
  },
  "confidence_score":                  "Float [0-1]",
  "model_version":                     "semver",
  "prompt_version":                    "semver | null",
  "audit_reference":                   "UUID",
  "next_trigger_events":               ["list"]
}
```

### 4.2 Longitudinal Coherence Output (Batch)

```json
OUTPUT_SCHEMA_COHERENCE: {
  "result_object": {
    "batch_id":                        "UUID",
    "user_id":                         "UUID",
    "coherence_score":                 "Float [0-100]",
    "self_model_evolution_label":      "FRAGMENTING | STATIC | CONSOLIDATING | EXPANDING",
    "belief_consistency_index":        "Float [0-1]",
    "stated_vs_enacted_delta":         "Float (signed — negative = under-performing stated)",
    "longitudinal_depth_trajectory":   "Float[] (time-series of depth scores)",
    "language_consistency_score":      "Float [0-1] (semantic embedding stability)",
    "collapse_risk_flag":              "boolean (true = self-model showing fragmentation)",
    "anomaly_flags":                   ["list"],
    "audit_reference":                 "UUID"
  },
  "confidence_score":                  "Float [0-1]",
  "model_version":                     "semver",
  "audit_reference":                   "UUID",
  "next_trigger_events":               ["list"]
}
```

### 4.3 Output Guarantees

```yaml
ALL_OUTPUTS_MUST_INCLUDE:
  - confidence_score (Float 0-1, never null)
  - model_version (semver, immutable reference)
  - audit_reference (UUID, append-only log entry)
  - next_trigger_events (may be empty array, never null)
  - privacy_controls block (never omitted)

INSUFFICIENT_DATA_POLICY:
  - If sub_dimensions_computed < 4: composite = null, method = INSUFFICIENT_DATA
  - Emit INSUFFICIENT_DATA structured flag (not an error — a state)
  - Do not trigger RANK_UPDATE_EVENT on INSUFFICIENT_DATA output

LOW_CONFIDENCE_POLICY:
  - confidence_score < 0.60: Deliver with DEGRADED_CONFIDENCE flag
  - confidence_score < 0.40: HALT — escalate to OBSERVABILITY_AGENT
  - Belt promotion inputs: confidence_score < 0.70 = BLOCKED

CLINICAL_FIREWALL (ABSOLUTE):
  - NO output field may use clinical psychological terminology
  - Output must never label: depression, anxiety, disorder, syndrome, diagnosis
  - Emotional regulation signals are framed as cognitive style only
  - Any output approaching clinical interpretation: HALT + ESCALATE to COMPLIANCE_AGENT

PRIVACY_FIREWALL (ABSOLUTE):
  - Raw reflection text NEVER included in output (processed vectors only)
  - Raw text only accessible to authorized roles (user, assigned mentor)
  - Recruiter visibility: NEVER (intrapersonal raw data) — aggregate signal only with consent
```

---

## 🤖 SECTION 5 — ML / AI LOGIC LAYER

### 5.1 ML Layer (70% — Primary Processing)

#### 5.1.1 Sub-Dimension Scoring Models

```yaml
RD1 — SELF_ASSESSMENT_ACCURACY MODEL:
  Type:             Regression (calibration error computation)
  Method:           Directional delta + rolling calibration curve
  Formula:
    raw_delta = user_self_score - system_actual_score
    calibration_error = |raw_delta| / 100.0 (normalized)
    direction_bias = sign(raw_delta)  # +1=over-estimate, -1=under-estimate
    accuracy_score = max(0, 100 - (|raw_delta| * CALIBRATION_WEIGHT))
    CALIBRATION_WEIGHT = 1.5 (more penalty for over-estimation vs under)
    rolling_stability = 1 - std(last_N_deltas) / 50.0  # normalized variance
    final_RD1 = accuracy_score * 0.7 + rolling_stability * 30
  Training:         Rule-based computation (no ML training cycle)
  Minimum_events:   3 self-assessment events required for stable RD1 score
  CALIBRATION_WEIGHT: IMMUTABLE in v1.0.0 — requires MAJOR bump to change

RD2 — METACOGNITIVE_DEPTH MODEL:
  Type:             NLP Text Classification + Scoring
  Features:
    - Presence of meta-cognitive markers (strategy words, process language,
      reasoning-about-reasoning phrases) — via curated lexicon v1.0.0
    - Causal chain depth (claim → explanation → deeper explanation levels)
    - Hypothesis formation signals (conditional language, "if-then" reasoning)
    - Self-monitoring language (catch phrases, correction language, uncertainty markers)
    - Abstraction level score (concrete vs. abstract language ratio)
    - Question-asking density (genuine self-inquiry signals)
  ML_Model:         Multi-label classifier (BERT-based fine-tuned, Intrapersonal domain)
                    + Rule-based metacognitive lexicon scorer
  Weighting:        ML model output 60% + rule-based lexicon 40%
  Training_data:    Platform-sourced reflections (human-labeled depth levels, bootstrapped)
  Training_freq:    Monthly (1st of month, 04:00 UTC)
  Min_text_length:  150 characters for RD2 computation (below = null, insufficient flag)

RD3 — REFLECTIVE_ELABORATION MODEL:
  Type:             NLP Structural Analysis
  Features:
    - Sentence complexity index (dependency parse depth average)
    - Multi-perspective acknowledgment (presence of alternative view language)
    - Counter-argument inclusion (adversarial reasoning signals)
    - Specificity index (proper noun density, measurable reference density)
    - Temporal reasoning depth (past-present-future integration)
    - Lexical diversity (unique word ratio, domain vocabulary breadth)
    - Argument layering score (nested reasoning levels)
  ML_Model:         Gradient Boosting Regression on NLP feature vectors
  Training_freq:    Monthly
  Min_text_length:  100 characters for RD3 computation

RD4 — COGNITIVE_CONSISTENCY MODEL:
  Type:             Temporal ML Classification (cross-signal consistency)
  Features:
    - Stated ability vs. assessed ability delta over time (from RD1)
    - Stated goal vs. goal_achievement_rate alignment
    - Platform behavior vs. stated learning preferences alignment
    - Reflection sentiment vs. performance trajectory alignment
    - Belief statement consistency across sessions (embedding distance)
  ML_Model:         Gradient Boosting Classifier (CONSISTENT | MODERATE | INCONSISTENT)
                    mapped to [100, 50-99, 0-49] score range
  Training_freq:    Monthly
  Min_data_window:  30 days of behavioral signals required

RD5 — EMOTIONAL_REGULATION_SIGNAL MODEL:
  Type:             NLP Affect + Cognitive Framing Classifier
  Features:
    - Regulation vocabulary presence (labeling emotions, distancing language)
    - Affect arc in text (stable vs. volatile emotional trajectory)
    - Cognitive reframing signals (catastrophizing reversal, perspective shift)
    - Suppression signal absence (vs. regulation signal presence)
    - Proportionality language (context-calibrated emotional response framing)
  ML_Model:         Fine-tuned BERT affect classifier (governed — no clinical labels)
  CLINICAL_FIREWALL: Output labels ONLY: [REGULATED, DEVELOPING, ACTIVATING]
                     No psychiatric vocabulary in any model output
  Training_freq:    Monthly
  Scoring:          REGULATED=75-100, DEVELOPING=40-74, ACTIVATING=0-39

RD6 — GOAL_CLARITY_AND_COMMITMENT MODEL:
  Type:             NLP Specificity Scorer + Behavioral Completion Tracker
  Features:
    Text-based:
      - SMART criteria detection (Specific, Measurable, Achievable, Relevant, Time-bound)
      - Commitment language strength (modal verbs, time markers, measurable targets)
      - Goal revision quality (revision = refinement OR abandonment pattern)
      - Action planning depth (steps identified, resources named)
    Behavioral:
      - goal_achievement_rate_90d (Float from GOAL_TRACKING_SERVICE)
      - revision_frequency (healthy revision vs. commitment avoidance pattern)
      - time-to-abandonment distribution
  ML_Model:         Two-stage:
                    Stage 1: NLP SMART-criterion scorer (rule-based + BERT classifier)
                    Stage 2: Behavioral completion regression
  Composite:        Text_score * 0.5 + Behavioral_score * 0.5
  Training_freq:    Monthly

RD7 — REASONING_TRANSPARENCY MODEL:
  Type:             Argument Structure Analysis (NLP + Pattern Matching)
  Method:           Toulmin Argument Model decomposition
    - Claim detection (main assertion presence)
    - Evidence citation (supporting data, example, reference)
    - Warrant detection (logical link from evidence to claim)
    - Backing detection (foundational reasoning)
    - Qualifier detection (appropriate uncertainty acknowledgment)
    - Rebuttal acknowledgment (counterpoint address)
  ML_Model:         Rule-based Toulmin parser + BERT-based argument span detector
  Scoring:
    component_scores: claim=20, evidence=25, warrant=25, backing=10, qualifier=10, rebuttal=10
    transparency_score = sum of present components weighted
  Min_text_length:  200 characters for RD7 computation

RD8 — SELF_MODEL_COHERENCE MODEL:
  Type:             Longitudinal Semantic Embedding Consistency Tracker
  Method:
    - Encode each reflection submission as semantic embedding vector
      (sentence-transformers, domain-adapted)
    - Track centroid of user's self-description embedding space over time
    - Measure: coherence = 1 - (cosine distance between session embeddings)
    - Detect: fragmentation = high variance in embedding space
    - Detect: consolidation = centroid stability + controlled expansion
    - Detect: stagnation = near-zero variance (no growth in self-concept)
  ML_Model:         Sentence-BERT (fine-tuned on Ecoskiller reflection corpus)
                    + statistical coherence metrics
  Minimum_submissions: 5 reflections required for RD8 computation
  Training_freq:    Monthly (embedding model re-adaptation)
  Storage:          Per-user embedding history in vector store (Pinecone-compatible)
                    Stored as vectors only — no raw text
```

#### 5.1.2 Composite Score Computation

```yaml
COMPOSITE_INTRAPERSONAL_SCORE:
  Method:           Weighted average of available sub-dimension scores
  Weights (IMMUTABLE v1.0.0 — MAJOR bump required to change):
    RD1_self_assessment_accuracy:    0.15
    RD2_metacognitive_depth:         0.20
    RD3_reflective_elaboration:      0.15
    RD4_cognitive_consistency:       0.15
    RD5_emotional_regulation:        0.10
    RD6_goal_clarity_commitment:     0.10
    RD7_reasoning_transparency:      0.10
    RD8_self_model_coherence:        0.05
    Total:                           1.00

  MINIMUM_SUB_DIMENSIONS:           4 of 8 must be computed for composite
  FALLBACK:                         If < 4 available: composite = null, flag INSUFFICIENT_DATA

DEPTH_CLASSIFICATION_THRESHOLDS (IMMUTABLE v1.0.0):
  TRANSFORMATIVE:   >= 88 composite score
  DEEP:             >= 72 and < 88
  SUBSTANTIVE:      >= 55 and < 72
  DEVELOPING:       >= 35 and < 55
  SURFACE:          < 35
```

#### 5.1.3 Drift Detection

```yaml
DRIFT_DETECTION:

  MODEL_ACCURACY_DRIFT:
    Monitor:    RD2 and RD3 model validation loss on weekly held-out set
    Alert:      Loss increases > 15% from baseline
    Action:     Trigger model retraining + notify OBSERVABILITY_AGENT

  FEATURE_DISTRIBUTION_DRIFT:
    Monitor:    PSI on top 10 NLP features weekly
    Alert:      PSI > 0.20
    Action:     Retrain with updated feature normalization

  CALIBRATION_BIAS_DRIFT (RD1):
    Monitor:    Population-level mean self-assessment delta weekly
    Detect:     Systematic over- or under-estimation trend (|mean_delta| > 15)
    Action:     Flag for ASSESSMENT_ENGINE_AGENT review (potential score scale drift)

  REFLECTION_QUALITY_POPULATION_DRIFT:
    Monitor:    Distribution of RD2 (metacognitive depth) scores weekly
    Alert:      If mean depth drops > 10 points from 30-day baseline
    Possible_cause: Prompt quality degradation, gaming behavior, cultural shift
    Action:     Notify METACOGNITION_PROMPT_ENGINE + flag for human review

  GAMING_DETECTION:
    Signals:    Rapid submission of many short reflections to farm XP
                Submissions with extremely high lexical diversity but low semantic coherence
                Sudden jump in RD3 score without corresponding RD4 improvement
    Model:      Anomaly detection on (submission_rate, depth_score, coherence_delta)
    Action:     Flag GAMING_SUSPECTED, throttle XP award, escalate to TRUST_SYSTEM_AGENT

  EMBEDDING_SPACE_COLLAPSE:
    Monitor:    RD8 self-model coherence — detect sudden collapse to near-zero variance
    Alert:      coherence drops > 30 points in 7-day window
    Action:     Emit alert to AI_MENTOR_AGENT + ALERT_SERVICE_AGENT
```

### 5.2 AI Layer (30% — Governed Semantic Assist)

```yaml
AI_USAGE_SCOPE:
  PERMITTED:
    - Semantic depth scoring for RD2 (metacognitive depth) —
      structured prompts only, output = Float score + brief category label
    - Semantic quality scoring for RD3 (reflective elaboration) —
      structured prompts only, output = Float score + structured breakdown
    - Argument completeness scoring for RD7 (reasoning transparency) —
      structured prompts only, output = component presence flags
    - Generating parent-facing growth narrative (age-appropriate, plain language)
    - Generating AI Mentor coaching prompt based on weakest sub-dimension
    - Summarizing depth classification for recruiter aggregate report (no raw content)

  FORBIDDEN:
    - AI may NOT compute self-assessment accuracy scores (RD1 — purely quantitative)
    - AI may NOT override ML-computed scores
    - AI may NOT access raw reflection text for any purpose beyond current-call scoring
    - AI may NOT make clinical interpretations or mental health inferences
    - AI may NOT generate content that labels the user in pejorative terms
    - AI may NOT reference specific reflection content in outputs visible to
      recruiters, institutes, or parents (processed vectors and scores only)
    - AI may NOT retain reflection text content between calls (stateless)
    - AI may NOT trigger downstream events
    - AI output that contains clinical vocabulary: DISCARD + escalate to COMPLIANCE_AGENT

PROMPT_GOVERNANCE:
  - All prompts versioned (semver) in PROMPT_REGISTRY
  - Deterministic prompt templates — no open-ended creative prompts
  - Prompts reviewed for clinical language: any clinical framing = REJECTED from registry
  - prompt_version logged in audit record
  - LLM input: structured features + text excerpt (max 500 chars) — no full raw text
  - LLM output: validated against output schema before use
  - Hallucination guard: numeric outputs cross-checked against ML bounds
  - LLM timeout: 4s — timeout = deliver ML score only, narrative = "UNAVAILABLE"
  - Cultural bias review: prompt library reviewed quarterly for cultural neutrality

AI_CLINICAL_FIREWALL:
  Pre-output filter applied to ALL LLM outputs:
    - Scan for clinical DSM/ICD terminology
    - Scan for personality disorder labels
    - Scan for medication references
    - Scan for self-harm proximity language
    - ANY match → DISCARD output + HALT + LOG_COMPLIANCE_INCIDENT
    - This filter runs BEFORE any LLM output reaches downstream processing
```

---

## ⚡ SECTION 6 — SCALABILITY DESIGN

```yaml
EXPECTED_RPS:
  Reflection submissions:    2,000 RPS (peak — assessments post-match)
  Self-delta events:         5,000 RPS (high-volume real-time)
  Goal events:               500 RPS
  On-demand queries:         1,000 RPS
  Target (100M users):       Scale to 20,000 RPS on submission path

LATENCY_TARGET:
  Reflection scoring:        p50 < 800ms | p95 < 2,500ms | p99 < 5,000ms
  (Higher than other agents due to NLP + embedding computation)
  Self-delta scoring:        p50 < 150ms | p95 < 400ms | p99 < 800ms
  Goal event scoring:        p50 < 200ms | p95 < 500ms | p99 < 1,000ms
  Batch coherence per user:  < 10s per user (parallelized across workers)

MAX_CONCURRENCY:
  NLP workers:               2,000 parallel (GPU-backed for BERT inference)
  Statistical workers:       10,000 parallel (CPU — lightweight RD1/RD4/RD6)
  Batch workers:             200 parallel (tenant-partitioned)

QUEUE_STRATEGY:
  Reflection submissions:    Kafka topic: ecoskiller.rda.reflection-events
                             Partitioned by: user_id hash (64 partitions)
  Self-delta events:         Kafka topic: ecoskiller.rda.self-delta-events
                             Partitioned by: user_id hash (32 partitions)
  Goal events:               Kafka topic: ecoskiller.rda.goal-events
  Batch trigger:             Kafka topic: ecoskiller.rda.batch-trigger

PROCESSING_MODEL:
  Reflection:    Async (event-driven) — NLP heavy path
  Self-delta:    Async (near-realtime) — statistical path
  Goal:          Async (near-realtime)
  Batch:         Async scheduled (nightly)
  Queries:       Sync REST with cache-first pattern

STATE_STORAGE:
  Sub-dimension scores:      PostgreSQL (per-user per-dimension history, append-only)
  Semantic embeddings:       Vector store (Pinecone-compatible — per-user embedding history)
  NLP feature cache:         Redis (per-text hash, TTL 24h — avoid re-processing same text)
  User reflection vectors:   TimescaleDB (time-series of embedding centroids)
  Batch job state:           PostgreSQL (job registry + checkpointing)
  Dedup cache:               Redis SET (event_id, TTL 48h)

COMPUTE_STRATEGY:
  BERT inference:            GPU node pool (auto-scaled, Kubernetes HPA)
  Embedding computation:     GPU-backed batch jobs (nightly)
  Statistical models:        CPU workers (lightweight, high concurrency)
  GPU_SCALE_TRIGGER:         Queue lag > 500 messages on reflection topic

IDEMPOTENCY:
  - event_id as idempotency key
  - NLP feature cache: if text_hash already processed, return cached vectors
  - Batch: re-running same batch_date_utc overwrites same records safely
```

---

## 🔐 SECTION 7 — SECURITY ENFORCEMENT

```yaml
MANDATORY_CONTROLS:

  TENANT_ISOLATION:
    - All data scoped to tenant_id from JWT
    - Row-level security at PostgreSQL and TimescaleDB
    - Cross-tenant data access: IMMEDIATE HALT + SECURITY_INCIDENT
    - Embedding vectors scoped per tenant (no cross-tenant similarity queries)

  DOMAIN_ISOLATION:
    - domain_track validated per user registration
    - Cross-domain reflection data: FORBIDDEN without explicit ABAC grant

  PRIVACY_FIREWALL (CRITICAL):
    - Raw reflection text: stored ONLY if consent_for_storage = true (separate consent)
    - Raw text: accessible ONLY to the user themselves + assigned mentor (with user consent)
    - Raw text: NEVER accessible to recruiters, institutes, parents
    - Processed vectors: may be shared with authorized downstream agents
    - Recruiter access: aggregate intrapersonal signal ONLY with candidate's explicit consent
    - Parent access: aggregate growth narrative ONLY (no sub-dimension raw scores)
    - Student self-access: all their own data available on request

  CLINICAL_FIREWALL (ABSOLUTE):
    - No clinical terminology in any stored score, label, or output
    - Compliance scanner runs on ALL model outputs before storage
    - Any clinical terminology detected: HALT + LOG_COMPLIANCE_INCIDENT + human review
    - Agent cannot be configured to perform clinical assessment under any circumstances

  CONSENT_ENFORCEMENT:
    - consent_for_analysis = true required for any text processing
    - consent_for_storage = separate consent (text storage vs. analysis)
    - Consent verified at request time (not cached)
    - Consent revocation: immediate cessation of processing + vector deletion queued
    - All consent decisions logged with consent_version and timestamp

  AUTHORIZATION (RBAC + ABAC):
    STUDENT:        Read own scores, own reflection submissions, own goal records
    PARENT:         Read child's depth_classification and growth_narrative only
    MENTOR:         Read assigned mentee's sub-dimension scores + reflection summary
                    (not raw text without student consent)
    RECRUITER:      Read aggregate intrapersonal signal ONLY with candidate consent
    INSTITUTE_ADMIN: Read cohort-level distribution (anonymized aggregates only)
    TENANT_ADMIN:   Read all tenant-scoped aggregated analytics
    PLATFORM_ADMIN: Read platform-wide anonymized distributions

  ENCRYPTION:
    - At-rest: AES-256 (all stores)
    - In-transit: TLS 1.3
    - Reflection text (if stored): field-level encryption with user-specific key
    - Embedding vectors: encrypted at rest (treated as derived PII)
    - Secrets: Secret Manager only

  MINOR_PROTECTION:
    - Users under 18: parent consent required before reflection analysis
    - Minors' data: additional restrictions on recruiter access (none permitted)
    - Age verification: from identity system (IDENTITY_VERIFICATION_AGENT)

  AUDIT_LOGGING:
    - Immutable append-only audit per execution
    - Every raw text access logged (who, when, why, consent_id)
    - Consent verification logged per access
    - Clinical firewall activations logged (immutable)
    - All model outputs logged with version reference

  ABUSE_PROTECTION:
    - Rate limiting: max 10 reflection submissions per user per hour
    - Gaming detection: rapid-fire submissions flagged (see drift detection)
    - Text injection scans on all submissions
```

---

## 📋 SECTION 8 — AUDIT & TRACEABILITY

Every execution MUST produce an immutable audit record:

```json
AUDIT_RECORD: {
  "audit_id":                         "UUID",
  "audit_chain_hash":                 "SHA-256(previous_audit_id + payload_hash)",
  "timestamp_utc":                    "ISO 8601",
  "agent_id":                         "ECO-AG-RDA-003",
  "agent_version":                    "semver",
  "trigger_type":                     "string",
  "event_id":                         "UUID (idempotency key)",
  "actor_id":                         "user_id or scheduler_id",
  "tenant_id":                        "UUID",
  "user_id":                          "UUID",
  "input_hash":                       "SHA-256 of input payload",
  "output_hash":                      "SHA-256 of output payload",
  "ml_model_versions": {
    "RD1_model":                      "semver | null",
    "RD2_model":                      "semver | null",
    "RD3_model":                      "semver | null",
    "RD4_model":                      "semver | null",
    "RD5_model":                      "semver | null",
    "RD6_model":                      "semver | null",
    "RD7_model":                      "semver | null",
    "RD8_model":                      "semver | null"
  },
  "prompt_version":                   "semver | null",
  "sub_dimensions_computed":          "int",
  "composite_score":                  "Float | null",
  "depth_classification":             "string | null",
  "confidence_score":                 "Float",
  "consent_for_analysis":             "boolean",
  "consent_for_storage":              "boolean",
  "raw_text_stored":                  "boolean",
  "pii_detected":                     "boolean",
  "clinical_firewall_triggered":      "boolean",
  "gaming_flag":                      "boolean",
  "anomaly_flags":                    ["list"],
  "processing_duration_ms":           "int",
  "downstream_events_emitted":        ["list"],
  "rejection_flag":                   "boolean",
  "rejection_reason":                 "string | null",
  "previous_audit_hash":              "SHA-256 (chain integrity)"
}
```

```yaml
AUDIT_POLICY:
  IMMUTABILITY:          No UPDATE or DELETE — ever
  STORAGE:               PostgreSQL append-only (WAL enabled)
  CHAIN_INTEGRITY:       SHA-256 linking (tamper-evident)
  RETENTION:             Minimum 7 years (configurable per compliance regime)
  CLINICAL_INCIDENTS:    Separate immutable table: clinical_firewall_incidents
                         (reviewed by compliance team monthly)
  PII_INCIDENTS:         Separate immutable table: pii_detection_incidents
  RAW_TEXT_ACCESS:       Every access to raw reflection text logged separately
                         with: accessor_id, role, timestamp, consent_id, purpose
```

---

## 🚨 SECTION 9 — FAILURE POLICY

```yaml
FAILURE_SCENARIOS:

  INVALID_INPUT:
    Action:           STOP_EXECUTION
    Log:              LOG_VALIDATION_FAILURE (immutable)
    Escalate:         EMIT to OBSERVABILITY_AGENT + source agent
    Retry:            NO — caller corrects and resubmits
    Output:           Structured rejection object

  CONSENT_MISSING:
    Action:           HARD REJECT — STOP_EXECUTION
    Log:              LOG_CONSENT_VIOLATION (immutable, critical)
    Escalate:         ESCALATE to COMPLIANCE_AGENT + PLATFORM_ADMIN
    Retry:            NO
    Output:           Consent rejection object (no analysis)

  CLINICAL_FIREWALL_TRIGGERED:
    Action:           DISCARD AI output entirely, deliver ML-only output
                      If ML output also contains clinical terms: HALT entirely
    Log:              LOG_COMPLIANCE_INCIDENT (severity: CRITICAL, immutable)
    Escalate:         ESCALATE to COMPLIANCE_AGENT
    Retry:            NO — requires prompt registry review and version bump
    Output:           ML scores only, narrative = null, flag = "CLINICAL_FIREWALL_ACTIVE"

  PII_DETECTED_IN_SUBMISSION:
    Action:           HALT text processing, trigger PII_SCRUB_SERVICE
                      Do NOT store original text
    Log:              LOG_PII_INCIDENT (severity: HIGH)
    Escalate:         EMIT to PII_SCRUB_SERVICE + COMPLIANCE_AGENT
    Retry:            After scrub confirmation, resubmit cleaned text
    Output:           Score on hold until scrub confirmed

  NLP_MODEL_UNAVAILABLE:
    Action:           Compute available statistical sub-dimensions (RD1, RD4, RD6)
                      Flag NLP-dependent dimensions as null with MODEL_UNAVAILABLE flag
                      If < 4 sub-dimensions available: HALT composite, deliver partial
    Log:              LOG_INCIDENT (severity: HIGH)
    Escalate:         ESCALATE to OBSERVABILITY_AGENT
    Retry:            YES — 3 attempts (5s / 20s / 60s) before fallback

  GPU_UNAVAILABLE (BERT inference):
    Action:           Queue reflections in Kafka with AWAITING_COMPUTE status
                      Serve statistical sub-dimensions immediately
                      Process NLP dimensions when GPU available
    Log:              LOG_INCIDENT (severity: MEDIUM)
    Escalate:         Notify OBSERVABILITY_AGENT
    SLA:              Reflection scoring must complete within 10 minutes of submission

  EMBEDDING_STORE_UNAVAILABLE:
    Action:           Skip RD8 computation for this event
                      Flag RD8 as null with EMBEDDING_STORE_UNAVAILABLE flag
    Log:              LOG_INCIDENT (severity: MEDIUM)
    Escalate:         Notify OBSERVABILITY_AGENT
    Retry:            YES — retry RD8 on next batch run

  CONFIDENCE_BELOW_0.60:
    Action:           Deliver with DEGRADED_CONFIDENCE flag
    Restriction:      Do NOT trigger belt promotion, rank changes
    Log:              LOG_LOW_CONFIDENCE

  CONFIDENCE_BELOW_0.40:
    Action:           HALT — do not deliver output
    Log:              LOG_INCIDENT (severity: HIGH)
    Escalate:         ESCALATE to OBSERVABILITY_AGENT

  GAMING_DETECTED:
    Action:           Score flagged as SUSPICIOUS — deliver score but block XP trigger
    Log:              LOG_ANOMALY (severity: MEDIUM)
    Escalate:         EMIT to TRUST_SYSTEM_AGENT
    Retry:            Score re-evaluated on next batch run after trust review

  INSUFFICIENT_CONTENT (text < 50 chars):
    Action:           Do not process — return structured INSUFFICIENT_CONTENT flag
    Log:              LOG_INSUFFICIENT_CONTENT (severity: INFO)
    Output:           { "status": "INSUFFICIENT_CONTENT", "minimum_chars": 50, "actual_chars": N }
    XP:               No XP awarded on insufficient content

POLICY_SUMMARY:
  NO_SILENT_FAILURES:       TRUE
  PARTIAL_NLP_OUTPUT:       PERMITTED with explicit flags (statistical dims may complete
                            while NLP dims await compute)
  CLINICAL_FIREWALL:        ABSOLUTE — no override permitted
  CONSENT_FIREWALL:         ABSOLUTE — no override permitted
```

---

## 🔗 SECTION 10 — INTER-AGENT DEPENDENCY MAP

### 10.1 Upstream Event Contracts

```yaml
FROM: REFLECTION_SUBMISSION_SERVICE
  Event:          REFLECTION_SUBMITTED
  Payload:        { event_id, user_id, submission_type, submission_text, prompt_id,
                    timestamp, domain_track, language_code, consent_flags }
  SLA:            Delivered within 3s of user submission
  Consent_check:  Must include consent_for_analysis=true

FROM: DOJO_SCORING_AGENT
  Event:          MATCH_TRANSCRIPT_READY
  Payload:        { event_id, user_id, match_id, transcript_text, speaker_role,
                    scenario_id, timestamp }
  SLA:            Delivered within 30s of match finalization
  Note:           Transcript used for RD7 reasoning transparency analysis

FROM: ASSESSMENT_ENGINE_AGENT
  Event:          SELF_ASSESSMENT_SUBMITTED
  Payload:        { event_id, user_id, assessment_id, dimension,
                    user_self_score, system_actual_score, timestamp }
  SLA:            Delivered within 5s of assessment completion
  Usage:          Primary input for RD1 calibration

FROM: GOAL_TRACKING_SERVICE
  Event:          GOAL_EVENT
  Payload:        { event_id, user_id, goal_id, goal_event_type, goal_text,
                    target_date, progress_percent, completion_flag }
  SLA:            Delivered within 10s of goal event
  Usage:          Primary input for RD6 goal clarity

FROM: FEATURE_STORE_AGENT
  Event:          FEATURE_VECTOR_UPDATED
  Payload:        { user_id, feature_name, feature_value, timestamp }
  Frequency:      Every 5 minutes
  Usage:          Secondary input for RD4 cognitive consistency

FROM: METACOGNITION_PROMPT_ENGINE
  Event:          METACOGNITIVE_RESPONSE_SUBMITTED
  Payload:        { event_id, user_id, prompt_id, response_text, response_timestamp }
  SLA:            Delivered within 5s of response submission
  Usage:          High-value input for RD2 and RD3 (structured metacognitive prompts)

FROM: BATCH_SCHEDULER
  Event:          BATCH_COHERENCE_TRIGGER
  Payload:        { batch_id, batch_date_utc, scope, auth_token }
  Schedule:       Nightly 03:00 UTC
  Usage:          Triggers RD8 longitudinal coherence recalculation
```

### 10.2 Downstream Event Contracts

```yaml
TO: INTELLIGENCE_GROWTH_TIME_SERIES_AGENT
  Trigger:        INTRAPERSONAL_SCORE_COMPUTED
  Payload:        { user_id, dimension: "Intrapersonal", current_score,
                    confidence, timestamp, depth_classification }

TO: POPULATION_PERCENTILE_ENGINE_AGENT
  Trigger:        INTRAPERSONAL_SCORE_UPDATED
  Payload:        { user_id, dimension: "Intrapersonal", score, confidence }

TO: AI_MENTOR_AGENT
  Trigger:        REFLECTIVE_DEPTH_PROFILE_UPDATED
  Payload:        { user_id, sub_dimension_scores, weakest_sub_dimension,
                    gap_to_next_band, depth_classification, coaching_context }

TO: PARENT_INSIGHT_AGENT
  Trigger:        INTRAPERSONAL_GROWTH_SUMMARY_READY
  Payload:        { child_id, depth_classification, growth_trend_30d,
                    age_appropriate_narrative }
  Note:           No sub-dimension raw scores — aggregate narrative only

TO: INSTITUTE_ANALYTICS_AGENT
  Trigger:        COHORT_REFLECTIVE_DEPTH_UPDATED
  Payload:        { cohort_id, depth_distribution, mean_composite_score,
                    sub_dimension_averages }
  Note:           Anonymized aggregate — no individual data

TO: RECRUITER_INTELLIGENCE_AGENT
  Trigger:        CANDIDATE_INTRAPERSONAL_SIGNAL_READY
  Payload:        { user_id, depth_classification, composite_score_band,
                    consent_scope_applied }
  Restriction:    ONLY if candidate consent_scope includes 'INTRAPERSONAL_SHARE'
  Note:           No raw reflection content — score band + depth label only

TO: FEATURE_STORE_AGENT (emit back — feedback loop)
  Trigger:        RDA_FEATURES_COMPUTED
  Features_emitted:
    - intrapersonal_composite_score              (Float)
    - depth_classification_encoded               (Int: 0=SURFACE...4=TRANSFORMATIVE)
    - self_assessment_accuracy_score             (Float — RD1)
    - metacognitive_depth_score                  (Float — RD2)
    - cognitive_consistency_score                (Float — RD4)
    - goal_achievement_rate_90d                  (Float — from RD6)
    - self_model_coherence_score                 (Float — RD8)
    - over_estimation_tendency                   (Float — signed bias)
    - depth_trend_30d_encoded                    (Int: -1=DECLINING, 0=STABLE, 1=IMPROVING)
    - reflection_velocity_30d                    (Float)
    - gaming_flag                                (Int: 0|1)

TO: TRUST_SYSTEM_AGENT
  Trigger:        GAMING_SUSPECTED | CONSISTENCY_COLLAPSE
  Payload:        { user_id, flag_type, evidence_signals, severity }

TO: ALERT_SERVICE_AGENT
  Trigger:        DEPTH_REGRESSION | CONSISTENCY_COLLAPSE | STAGNATION_DETECTED
  Payload:        { user_id, alert_type, dimension: "Intrapersonal", context_summary }

TO: RANK_ENGINE_AGENT
  Trigger:        DEPTH_BAND_PROMOTED (e.g., DEVELOPING → SUBSTANTIVE)
  Payload:        { user_id, old_band, new_band, dimension: "Intrapersonal", timestamp }

TO: OBSERVABILITY_AGENT
  Trigger:        Any anomaly, drift, incident, clinical firewall event, consent incident

EVENT_BUS:               Kafka (Apache Avro — schema registry enforced)
EVENT_DELIVERY:          At-least-once with idempotency guard at all consumers
```

---

## 🌊 SECTION 11 — PASSIVE INTELLIGENCE COMPATIBILITY

```yaml
FEATURE_STORE_EMIT_CONTRACT:
  On every successful composite computation:

  FEATURES_EMITTED (for FEATURE_STORE_AGENT consumption):
    intrapersonal_composite_score:          Float [0-100]
    depth_classification_encoded:           Int (0=SURFACE, 1=DEVELOPING,
                                             2=SUBSTANTIVE, 3=DEEP, 4=TRANSFORMATIVE)
    rd1_self_assessment_accuracy:           Float [0-100]
    rd2_metacognitive_depth:                Float [0-100] | null
    rd3_reflective_elaboration:             Float [0-100] | null
    rd4_cognitive_consistency:              Float [0-100] | null
    rd5_emotional_regulation:               Float [0-100] | null
    rd6_goal_clarity_commitment:            Float [0-100] | null
    rd7_reasoning_transparency:             Float [0-100] | null
    rd8_self_model_coherence:               Float [0-100] | null
    calibration_bias_direction:             Int (-1=under, 0=calibrated, 1=over)
    reflection_velocity_30d:               Float (submissions/week)
    depth_trend_30d_encoded:               Int (-1=DECLINING, 0=STABLE, 1=IMPROVING)
    consistency_trend_90d_encoded:         Int (-1=DECLINING, 0=STABLE, 1=IMPROVING)
    goal_achievement_rate_90d:             Float [0-1] | null
    gaming_flag:                           Int (0|1)
    sub_dimensions_computed:               Int (0-8)

  CONSUMPTION_BY_DOWNSTREAM:
    AI_MENTOR_AGENT:            Personalizes coaching based on weakest sub-dimension
    CAREER_GUIDANCE_AGENT:      Uses self-model coherence for career path stability signal
    PLACEMENT_PROBABILITY_AGENT: Uses intrapersonal composite as soft fit factor
    CHAMPIONSHIP_SEEDING_AGENT: Uses consistency score as sportsmanship signal
    TRUST_SYSTEM_AGENT:         Uses gaming_flag + consistency for trust scoring
```

---

## 🏆 SECTION 12 — GROWTH ENGINE HOOK

```yaml
DEPTH_BAND_PROGRESSION_EVENTS:
  Trigger conditions:
    - depth_classification transitions SURFACE → DEVELOPING
    - depth_classification transitions DEVELOPING → SUBSTANTIVE
    - depth_classification transitions SUBSTANTIVE → DEEP
    - depth_classification transitions DEEP → TRANSFORMATIVE
    - User achieves calibration_stability_30d > 0.85 (highly calibrated self-assessor)
    - User achieves goal_achievement_rate_90d > 0.75 (high goal follow-through)
    - RD8 self-model coherence > 85 (strong coherent self-model)
    - First reflection submission ever (FIRST_REFLECTION milestone)
    - 10th, 25th, 50th, 100th reflection submission milestones

  Events Emitted:
    RANK_UPDATE_EVENT:
      { user_id, dimension: "Intrapersonal", old_band, new_band, timestamp }

    XP_UPDATE_EVENT:
      { user_id, xp_earned, reason, dimension: "Intrapersonal" }
      XP_VALUES (IMMUTABLE v1.0.0 — MAJOR bump to change):
        SURFACE → DEVELOPING:             300 XP
        DEVELOPING → SUBSTANTIVE:         800 XP
        SUBSTANTIVE → DEEP:             2,000 XP
        DEEP → TRANSFORMATIVE:          6,000 XP
        High Calibration Achievement:     500 XP bonus
        High Goal Follow-Through:         400 XP bonus
        Strong Self-Model:                600 XP bonus
        First Reflection:                 100 XP
        10th Reflection:                  150 XP
        25th Reflection:                  250 XP
        50th Reflection:                  500 XP
        100th Reflection:               1,000 XP

    SHARE_TRIGGER_EVENT:
      Triggers on DEEP or TRANSFORMATIVE band achievement:
      { user_id, achievement: "REFLECTIVE_DEPTH_MILESTONE", band, share_card_data }
      share_card_data:
        — "Recognized as a Deep Reflective Thinker — Ecoskiller Antigravity"
        — Depth band badge (visual)
        — No raw scores or reflection content on share card

GAMING_XP_GUARD:
  - gaming_flag = 1: NO XP awarded until TRUST_SYSTEM_AGENT clears the flag
  - Insufficient content: NO XP awarded (< 50 chars)
  - Duplicate submissions: NO XP awarded (idempotency dedup)

DEMOTION_POLICY:
  - Depth band demotion (e.g., DEEP → SUBSTANTIVE after 90d of no reflections):
    Triggers RANK_UPDATE_EVENT (no XP deduction)
    Triggers ALERT_SERVICE_AGENT with supportive re-engagement message
    Does NOT trigger SHARE_TRIGGER_EVENT
```

---

## 📊 SECTION 13 — PERFORMANCE MONITORING

```yaml
AGENT_METRICS (reported to OBSERVABILITY_AGENT):

  Throughput:
    Reflection_events_per_second:     Target stable up to 20,000 RPS at scale
    NLP_inference_throughput:         BERT batch: target 500 texts/second per GPU node

  Success_Rate:
    Target:     > 99.0% (NLP path — higher latency tolerance)
    Alert:      < 98.0%
    Critical:   < 96.0%

  Error_Rate:
    Target:     < 1.0% (NLP path)
    Alert:      > 2.0%

  Latency:
    Reflection_p50:      Target < 800ms  | Alert > 1,500ms | Critical > 3,000ms
    Reflection_p95:      Target < 2,500ms| Alert > 4,000ms | Critical > 8,000ms
    Self_delta_p50:      Target < 150ms  | Alert > 300ms   | Critical > 600ms
    Batch_per_user:      Target < 10s    | Alert > 20s     | Critical > 60s

  NLP_Model_Metrics:
    RD2_model_accuracy:           Monitor weekly on held-out validation set
    RD3_model_accuracy:           Monitor weekly
    RD5_clinical_firewall_rate:   Alert if > 0.1% of outputs trigger clinical firewall
    Gaming_detection_precision:   Target > 80% precision (calibrated quarterly)

  GPU_Metrics:
    GPU_utilization:              Alert if < 50% (underutilized — scale down)
    GPU_queue_depth:              Alert if > 1,000 pending jobs
    Inference_latency_p95:        Alert if > 400ms per text

  Privacy_Metrics:
    PII_detection_rate:           Track per week (alert if spikes >2x baseline)
    Consent_violation_attempts:   Any attempt → immediate alert + COMPLIANCE_AGENT
    Clinical_firewall_activations: Any activation → immediate alert (target 0)
    Raw_text_access_events:       Monthly review (compliance audit)

  Coherence_Metrics:
    Self_model_collapse_rate:     Track per cohort per week
    Consistency_trend_distribution: Monitor for population-level consistency drift

  Confidence_Distribution:
    Rolling_7d_mean_confidence:   Alert if < 0.72
    Low_confidence_rate (<0.60):  Alert if > 8% of executions

  Batch_Metrics:
    Batch_completion_time:        Target < 3h | Alert > 2.5h | Critical > 3h (overrun)
    Embedding_recomputation_rate: Track users with changed vectors vs. stable

DASHBOARDS:
  - Grafana: Real-time agent health + NLP model health
  - Panels: Latency heatmaps, GPU utilization, clinical firewall rate,
            PII detection rate, depth distribution, gaming flag rate
  - Alerts: PagerDuty (severity HIGH + CRITICAL)
  - Compliance Dashboard: clinical firewall activations, consent violations (weekly report)
```

---

## 🔄 SECTION 14 — VERSIONING POLICY

```yaml
VERSION_FORMAT:         Semantic versioning (MAJOR.MINOR.PATCH)
AGENT_VERSION:          ECO-AG-RDA-003 v1.0.0

CHANGE_CLASSIFICATION:
  PATCH bump:
    - NLP lexicon updates (new metacognitive marker words)
    - Bug fix in scoring computation
    - Log field addition
    - Performance optimization

  MINOR bump:
    - New optional sub-dimension (RD9+)
    - New optional output field
    - New feature emitted to Feature Store
    - New LLM narrative template (if clinically clean)
    - New gaming detection signal

  MAJOR bump:
    - Sub-dimension score weight changes (RD1–RD8 composite weights)
    - Depth classification threshold changes (SURFACE/DEVELOPING/SUBSTANTIVE/DEEP/TRANSFORMATIVE)
    - Core NLP model architecture change (new BERT variant, new embedding model)
    - Intrapersonal sub-dimension definition change
    - Clinical firewall rule change
    - Consent requirement change
    - XP award values change
    - Raw text storage policy change

IMMUTABLE_IN_v1.0.0 (MAJOR bump required):
  - Composite score weights (RD1-RD8 weights)
  - Depth classification thresholds
  - Calibration weight for RD1
  - Clinical firewall rules
  - Consent requirement

MODEL_VERSIONING:
  - Every trained NLP model version tagged (semver)
  - Model artifacts stored immutably
  - Embedding model versions tracked separately
  - model_version referenced in every output and audit record
  - Rollback supported for all model versions

PROMPT_REGISTRY:
  - All LLM prompts versioned (semver)
  - Clinical language review required before any prompt version merge
  - Prompt rollback supported
  - prompt_version referenced in audit record

BACKWARD_COMPATIBILITY:
  - MAJOR version: 90-day window, all consumers notified
  - Blue/green deployment for MAJOR changes
  - Embedding schema changes require migration plan
```

---

## 🚫 SECTION 15 — NON-NEGOTIABLE RULES

```yaml
THIS AGENT MUST NOT:

  CLINICAL_ABSOLUTE_PROHIBITIONS:
    - Use clinical psychological terminology in any output
    - Produce personality disorder labels
    - Produce diagnoses, clinical assessments, or health risk scores
    - Reference medications, therapies, or clinical interventions
    - Be configured as a mental health tool under any circumstance
    - Allow LLM prompts containing clinical framing to enter the prompt registry

  PRIVACY_ABSOLUTE_PROHIBITIONS:
    - Process reflection text without consent_for_analysis = true
    - Store raw reflection text without explicit consent_for_storage = true
    - Share raw reflection text with recruiters, parents, or institutes
    - Include raw reflection content in any downstream agent payload
    - Create user profiles that expose reflection content outside authorized roles

  CONSENT_ABSOLUTE_PROHIBITIONS:
    - Proceed on any input where consent is absent, ambiguous, or revoked
    - Cache consent status (re-verify at each request)
    - Process minor's data without parental consent verification

  SCORING_PROHIBITIONS:
    - Award composite intrapersonal scores with < 4 sub-dimensions computed
    - Trigger belt promotion or certification autonomously
    - Allow gaming-flagged submissions to generate XP
    - Override any other agent's score computations

  GOVERNANCE_PROHIBITIONS:
    - Modify or delete audit records
    - Modify historical reflection records
    - Change sub-dimension weights without MAJOR version bump
    - Change depth classification thresholds without MAJOR version bump
    - Execute outside defined scope
    - Interpret ambiguous input — HALT and reject instead

  LLM_PROHIBITIONS:
    - LLM retains content between calls (stateless required)
    - LLM accesses full raw reflection text (max 500-char excerpt per prompt)
    - LLM output containing clinical terms reaches any output
    - LLM triggers downstream events
    - LLM overrides ML-computed numeric scores

  SECURITY_PROHIBITIONS:
    - Cross-tenant data access
    - Bypassing RBAC/ABAC checks
    - Plaintext PII in logs
    - Embedding vectors shared across tenant boundaries
```

---

## 🔒 FINAL GOVERNANCE SEAL

```
╔══════════════════════════════════════════════════════════════════════════╗
║       REFLECTIVE DEPTH ANALYZER AGENT — GOVERNANCE SEAL                  ║
╠══════════════════════════════════════════════════════════════════════════╣
║  Platform:             Ecoskiller Antigravity                             ║
║  Agent ID:             ECO-AG-RDA-003                                    ║
║  Agent Version:        v1.0.0                                            ║
║  Seal Date:            2026-02-26 UTC                                    ║
║  Specification Class:  PRODUCTION LOCKED ARTIFACT                        ║
╠══════════════════════════════════════════════════════════════════════════╣
║  EXECUTION MODE:       DETERMINISTIC                                     ║
║  MUTATION POLICY:      ADD-ONLY VERSIONED                                ║
║  CREATIVE INTERPRET:   FORBIDDEN                                         ║
║  ASSUMPTION FILLING:   FORBIDDEN                                         ║
║  SILENT FAILURE:       FORBIDDEN                                         ║
║  PARTIAL OUTPUT:       FORBIDDEN (except governed NLP/GPU fallback)      ║
╠══════════════════════════════════════════════════════════════════════════╣
║  INTELLIGENCE TARGET:  INTRAPERSONAL (exclusive primary dimension)       ║
║  SUB-DIMENSIONS:       8 locked (RD1–RD8)                                ║
║  DEPTH BANDS:          5-tier (SURFACE→TRANSFORMATIVE) — IMMUTABLE v1   ║
║  COMPOSITE WEIGHTS:    8-dimensional — IMMUTABLE v1                      ║
╠══════════════════════════════════════════════════════════════════════════╣
║  CLINICAL FIREWALL:    ABSOLUTE — zero exceptions, zero overrides        ║
║  CONSENT FIREWALL:     ABSOLUTE — no processing without consent          ║
║  PRIVACY FIREWALL:     ACTIVE — raw text never leaves authorized roles   ║
║  MINOR PROTECTION:     ACTIVE — parental consent required                ║
║  PII FIREWALL:         ACTIVE — injection detection + scrub service      ║
╠══════════════════════════════════════════════════════════════════════════╣
║  ML PRIMARY:           NLP (BERT) + Statistical ML (70%)                 ║
║  AI ASSIST:            LLM semantic scoring + narrative (30%, governed)  ║
║  GPU COMPUTE:          Required (BERT inference) — auto-scaled           ║
║  EMBEDDING STORE:      Vector store (per-user, encrypted, per-tenant)    ║
║  DRIFT DETECTION:      ACTIVE (model drift + gaming + embedding collapse)║
║  GAMING GUARD:         ACTIVE (rate + semantic + behavioral detection)   ║
╠══════════════════════════════════════════════════════════════════════════╣
║  AUDIT TRAIL:          IMMUTABLE APPEND-ONLY (SHA-256 chained)           ║
║  CLINICAL INCIDENTS:   Separate immutable compliance log                 ║
║  RAW TEXT ACCESS:      Separately logged (who, when, why, consent_id)    ║
║  RETENTION:            Minimum 7 years                                   ║
║  TENANT ISOLATION:     HARD ENFORCED                                     ║
║  SECURITY MODEL:       ZERO-TRUST                                        ║
╠══════════════════════════════════════════════════════════════════════════╣
║  TRIGGER TYPES:        4 (Reflection | Self-Delta | Goal | Batch)        ║
║  UPSTREAM AGENTS:      8 registered                                      ║
║  DOWNSTREAM AGENTS:    11 registered                                     ║
║  FEATURE STORE:        BIDIRECTIONAL (17 features emitted)               ║
║  EVENT BUS:            KAFKA (Avro + Schema Registry)                    ║
║  GROWTH ENGINE:        ACTIVE (5 depth bands × XP milestones locked)     ║
╠══════════════════════════════════════════════════════════════════════════╣
║  RECRUITER ACCESS:     SCORE BAND ONLY — consent required                ║
║  PARENT ACCESS:        NARRATIVE ONLY — no sub-dimension scores          ║
║  MENTOR ACCESS:        SUB-DIMENSION SCORES — with student consent       ║
║  RAW TEXT ACCESS:      USER + ASSIGNED MENTOR — explicit consent only    ║
╠══════════════════════════════════════════════════════════════════════════╣
║  INTERPRETATION AUTH:  NONE                                              ║
║  ARCHITECTURE AUTH:    LOCKED                                            ║
║  CLINICAL CHANGE AUTH: COMPLIANCE BOARD + MAJOR VERSION BUMP ONLY       ║
║  SPEC AUTHORITY:       ECOSKILLER PLATFORM GOVERNANCE                    ║
╚══════════════════════════════════════════════════════════════════════════╝
```

---

*This document is a sealed production artifact for the Ecoskiller Antigravity platform.
This agent operates at the intersection of intelligence measurement, user privacy, and
psychological safety. The clinical firewall, consent firewall, and privacy firewall are
permanently non-negotiable and cannot be overridden by any operator, tenant, or user.
Any modification to sub-dimension weights, depth classification thresholds, clinical
firewall rules, or consent requirements requires a MAJOR version bump with both
governance board and compliance board approval.
The agent must not deviate from this specification under any operating condition.*

**END LOCKED ARTIFACT — REFLECTIVE_DEPTH_ANALYZER_AGENT v1.0.0**
