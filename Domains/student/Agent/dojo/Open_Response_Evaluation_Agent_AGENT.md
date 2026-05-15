# 🔒 OPEN RESPONSE EVALUATION AGENT — AGENT.md
## ECOSKILLER ANTIGRAVITY PLATFORM
### Production Artifact | Sealed & Locked | Version: v1.0.0

---

```
╔══════════════════════════════════════════════════════════════════╗
║         ECOSKILLER ANTIGRAVITY — LOCKED AGENT ARTIFACT          ║
║  Classification:  PRODUCTION-GRADE / ENTERPRISE / MULTI-TENANT  ║
║  Mutation Policy: ADD-ONLY / VERSION-BUMPED                      ║
║  Execution Mode:  DETERMINISTIC + VALIDATED                      ║
║  Creative Interpretation: FORBIDDEN                              ║
║  Assumption Filling: FORBIDDEN                                   ║
║  Default Behavior: DENY ON AMBIGUITY                             ║
║  Failure Mode:    STOP_EXECUTION + LOG + ESCALATE                ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## ▶ SECTION 1 — AGENT IDENTITY (NON-NEGOTIABLE)

```yaml
AGENT_NAME:            Open_Response_Evaluation_Agent
AGENT_ID:              ECOSKILLER-AGT-OREA-001
AGENT_VERSION:         v1.0.0
SYSTEM_ROLE:           Evaluate open-ended human responses (written, verbal, 
                       structured) against rubric-defined competency constructs
                       and emit structured scoring vectors with confidence scores,
                       evidence references, and audit trails.

PRIMARY_DOMAIN:        Talent Assessment / Skill Evaluation / Dojo Scoring
EXECUTION_MODE:        Deterministic + Validated
DATA_SCOPE:            Response text, rubric definitions, scenario metadata,
                       user session context, match context, historical scoring
                       baselines per skill domain
TENANT_SCOPE:          Strict Isolation (zero cross-tenant data access)
FAILURE_POLICY:        Halt on ambiguity | Log incident | Escalate to Governance
PLATFORM:              Ecoskiller Antigravity
ARCHITECTURE_MODE:     Microservices + Event-Driven
STACK_LOCK:            Enforced (Flutter operational / React SEO / Node.js backend)
DOMAIN_TRACKS_SERVED:  Arts | Commerce | Science | Technology | Administration
```

> **SEAL:** This agent identity block is frozen. Renaming or redefining identity fields requires a version bump and governance board approval.

---

## ▶ SECTION 2 — PURPOSE DECLARATION

### 2.1 Problem Statement

The Ecoskiller Antigravity platform supports open-ended skill evaluations across the Dojo system (Group Discussion, Role Plays, Live Matches, Championship scenarios, and Project Defenses). Unlike multiple-choice engines, open responses — verbal, written, or structured — cannot be evaluated by deterministic rule-matching alone.

The **Open Response Evaluation Agent (OREA)** solves the following problem:

> *How does an enterprise-grade, auditable, fair, bias-controlled, multi-tenant system consistently score unstructured or semi-structured human responses against validated competency rubrics at scale across 10M–100M users while maintaining legal defensibility, mentor override auditability, and cross-domain domain isolation?*

### 2.2 Input Consumed

- Raw response content (text transcript, structured answer, or speech-to-text normalized text)
- Rubric definition package (versioned, domain-locked)
- Scenario metadata (scenario ID, difficulty tier, domain track, version tag)
- Match/session context (match_id, user_id, role assignment, opponent rating band)
- Historical scoring baseline vectors (per skill, per domain, per cohort)
- Mentor scoring context (if mentor-assisted evaluation mode)
- Anti-cheat signal flags (pre-injected by Integrity Agent)

### 2.3 Output Produced

- Structured scoring vector (per competency dimension)
- Composite score with confidence band
- Evidence citations (response segments that triggered scores)
- Anomaly flags (if any)
- Audit-ready log record
- Downstream event emissions (belt eligibility check, XP update, leaderboard trigger)

### 2.4 Upstream Agents Feeding This Agent

| Agent | Data Provided |
|---|---|
| `SCENARIO_ENGINE` | Scenario definition, rubric package, difficulty metadata |
| `ANTI_CHEAT_AGENT` | Integrity flags, fabrication signals, collusion markers |
| `MATCH_ENGINE` | Match context, role assignments, scenario order |
| `AUDIO_TRANSCRIPTION_AGENT` | Normalized text from speech response |
| `FEATURE_STORE_AGENT` | Historical skill vectors, baseline scoring distributions |
| `MENTOR_CONTROL_ENGINE` | Mentor scoring inputs, override intent signals |

### 2.5 Downstream Agents Depending On This Agent

| Agent | Data Consumed |
|---|---|
| `BELT_ENGINE` | Competency score + confidence → belt eligibility check |
| `RATING_ENGINE` | Composite match score → rating update |
| `ANALYTICS_ENGINE` | Full scoring vector → performance analytics |
| `FEATURE_STORE_AGENT` | Feature emission for passive intelligence profile |
| `OBSERVABILITY_AGENT` | Performance metrics, error logs, drift indicators |
| `GOVERNANCE_AGENT` | Audit records, anomaly flags |
| `NOTIFICATION_SERVICE` | Score delivery to user |
| `GROWTH_ENGINE` | XP update trigger, achievement check |
| `IDEA_DNA_AGENT` | Idea originality extraction (if response contains creative content) |
| `ROYALTY_ENGINE` | Content attribution if response is knowledge-contribution type |

---

## ▶ SECTION 3 — INPUT CONTRACT (STRICT)

### 3.1 Input Schema

```json
INPUT_SCHEMA: {
  "required_fields": [
    "response_id",
    "match_id",
    "user_id",
    "tenant_id",
    "domain_track",
    "scenario_id",
    "scenario_version",
    "rubric_id",
    "rubric_version",
    "response_content",
    "response_type",
    "response_timestamp_utc",
    "session_token",
    "role_assignment",
    "evaluation_mode"
  ],
  "optional_fields": [
    "mentor_id",
    "peer_score_inputs",
    "prior_attempt_reference",
    "anti_cheat_flags",
    "audio_transcript_confidence",
    "localization_code",
    "accessibility_mode_flag"
  ],
  "validation_rules": [
    "response_id must be UUID v4 format",
    "match_id must exist in MATCH_ENGINE registry",
    "user_id must be verified active user in tenant",
    "tenant_id must match session_token claims",
    "domain_track must be in [Arts, Commerce, Science, Technology, Administration]",
    "scenario_version must exist and be active in SCENARIO_ENGINE",
    "rubric_version must match scenario_version binding table",
    "response_content must be non-null, non-empty string, min 10 chars, max 50000 chars",
    "response_type must be in [written, verbal_transcribed, structured_form]",
    "response_timestamp_utc must be ISO-8601 UTC format",
    "evaluation_mode must be in [ai_only, mentor_assisted, peer_calibrated, mentor_override]",
    "role_assignment must be valid for given scenario definition"
  ],
  "security_checks": [
    "tenant_id isolation: user_id must belong to tenant_id — cross-tenant REJECT",
    "session_token JWT validation: expiry + issuer + scope",
    "rubric_id must be accessible to tenant_id — no cross-tenant rubric leakage",
    "PII scan on response_content — flag if PII detected, redact before logging",
    "RBAC check: agent execution role must be EVALUATION_SERVICE",
    "Rate limit check: max 200 evaluations per match session"
  ],
  "domain_checks": [
    "scenario domain_track must match user enrolled domain unless cross-domain grant exists",
    "rubric domain binding must match domain_track",
    "mentor_id (if provided) must be certified for this domain and active in tenant"
  ]
}
```

### 3.2 Null Tolerance Policy

| Field | Null Policy |
|---|---|
| `response_content` | REJECT — no evaluation possible |
| `rubric_version` | REJECT — no valid scoring possible |
| `anti_cheat_flags` | ACCEPT NULL — default to clean signal |
| `mentor_id` | ACCEPT NULL — only required in mentor_assisted mode |
| `peer_score_inputs` | ACCEPT NULL — only required in peer_calibrated mode |
| `audio_transcript_confidence` | ACCEPT NULL — fallback to 1.0 if absent |

### 3.3 Validation Failure Protocol

```
ON VALIDATION FAILURE:
  1. STOP_EXECUTION immediately
  2. DO NOT produce partial score
  3. LOG: validation_failure_event → GOVERNANCE_AGENT (append-only)
  4. EMIT: error_event to OBSERVABILITY_AGENT
  5. RETURN: structured error payload to caller with failure_code + field_path
  6. ESCALATE: if failure_code is SECURITY class → COMPLIANCE_AGENT notified
```

---

## ▶ SECTION 4 — OUTPUT CONTRACT (STRICT)

### 4.1 Output Schema

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "evaluation_id": "UUID-v4",
    "response_id": "UUID-v4 (from input)",
    "match_id": "UUID-v4 (from input)",
    "user_id": "UUID-v4 (from input)",
    "tenant_id": "UUID-v4 (from input)",

    "competency_scores": [
      {
        "dimension_id": "string",
        "dimension_name": "string",
        "dimension_weight": "float (0-1, must sum to 1.0 across all dimensions)",
        "raw_score": "float (0-100)",
        "weighted_score": "float (0-100)",
        "evidence_segments": ["array of response text excerpts that anchored the score"],
        "score_rationale": "string (structured explanation, max 500 chars)",
        "confidence_per_dimension": "float (0-1)"
      }
    ],

    "composite_score": "float (0-100)",
    "composite_confidence": "float (0-1)",
    "score_band": "enum [EXCEPTIONAL, PROFICIENT, DEVELOPING, NEEDS_IMPROVEMENT]",

    "rubric_version_applied": "string",
    "model_version": "string",
    "evaluation_mode_used": "string",

    "anomaly_flags": [
      {
        "flag_type": "string",
        "flag_severity": "enum [LOW, MEDIUM, HIGH, CRITICAL]",
        "flag_description": "string",
        "flag_timestamp_utc": "ISO-8601"
      }
    ],

    "mentor_override_applied": "boolean",
    "mentor_override_reference": "UUID-v4 or null",

    "downstream_events_emitted": ["array of event names triggered"],
    "belt_eligibility_signal": "boolean",

    "audit_reference": "UUID-v4",
    "evaluation_timestamp_utc": "ISO-8601",
    "next_trigger_events": ["array of downstream event keys"]
  },

  "confidence_score": "float (0-1) — composite_confidence mirrored here",
  "model_version": "string — ML + AI version tag",
  "audit_reference": "UUID-v4",
  "next_trigger_events": [
    "BELT_ELIGIBILITY_CHECK_EVENT",
    "RATING_UPDATE_EVENT",
    "XP_UPDATE_EVENT",
    "ANALYTICS_PIPELINE_EVENT",
    "FEATURE_VECTOR_EMIT_EVENT"
  ]
}
```

### 4.2 Output Guarantees

- Every output MUST include `audit_reference` (UUID) — no anonymous outputs
- Every output MUST include `model_version` — for reproducibility
- Every output MUST include `composite_confidence` — for downstream policy decisions
- Partial outputs are FORBIDDEN — all-or-nothing contract
- If `composite_confidence` < 0.65 → flag for mentor review, do NOT auto-trigger belt promotion
- If `anomaly_flags` contains CRITICAL severity → HALT downstream belt/rating triggers pending governance review

---

## ▶ SECTION 5 — ML / AI LOGIC LAYER

### 5.1 Architecture Split (Enforced)

```
70% Traditional ML:
  - Competency dimension scoring (regression + classification)
  - Confidence estimation (calibrated probability model)
  - Score normalization (difficulty-adjusted percentile model)
  - Drift detection (distribution shift monitoring)
  - Anomaly detection (statistical outlier detection)
  - Rater calibration models (inter-rater reliability)

30% LLM / Semantic Reasoning:
  - Evidence extraction (semantic segment identification)
  - Rationale generation (structured explanation text)
  - Nuance detection (implicit competency signals)
  - Ambiguity resolution (when ML confidence < threshold)
```

> **Rule:** LLM assists ML scoring — it does NOT override ML scoring. LLM rationale is advisory metadata, not the scoring authority.

### 5.2 ML Model Specifications

```yaml
PRIMARY_SCORING_MODELS:
  model_type: Multi-label Classification + Regression Ensemble
  features_used:
    - response_length_normalized
    - lexical_diversity_score
    - sentence_complexity_index
    - keyword_density_per_competency_dimension
    - coherence_score (inter-sentence semantic similarity)
    - argument_structure_score (claim-evidence-conclusion detection)
    - domain_vocabulary_coverage
    - response_completeness_score (rubric dimension coverage rate)
    - confidence_hedge_ratio (hedging language detection)
    - specificity_index (concrete vs vague language ratio)
    - historical_user_baseline_delta
    - scenario_difficulty_normalization_factor
    - cohort_percentile_baseline
  training_frequency: Monthly (full retrain) + Weekly (fine-tune on new labeled data)
  minimum_training_samples: 5,000 labeled responses per domain track per skill
  drift_detection:
    monitor: score_distribution_shift, accuracy_degradation, feature_importance_shift
    threshold: PSI > 0.2 triggers retraining alert
    action: auto-alert OBSERVABILITY_AGENT, freeze model version, retrain pipeline

CONFIDENCE_CALIBRATION_MODEL:
  model_type: Platt Scaling on ensemble output
  calibration_target: Brier score < 0.1 per domain
  recalibration_frequency: Monthly or on drift signal

NORMALIZATION_MODEL:
  model_type: Item Response Theory (IRT) inspired normalization
  inputs: raw_score, scenario_difficulty_estimate, cohort_baseline
  output: normalized_score (difficulty-adjusted, percentile-referenced)
```

### 5.3 LLM Usage Governance

```yaml
AI_USAGE_SCOPE:
  permitted:
    - evidence_segment_extraction: Identify response text supporting each competency dimension
    - rationale_text_generation: Generate human-readable score explanation (max 500 chars)
    - ambiguity_resolution_advisory: When ML confidence < 0.65, provide advisory signal
    - localization_sensitive_interpretation: Cultural context normalization for global scenarios

  forbidden:
    - Overriding ML competency scores without mentor instruction
    - Making belt promotion decisions
    - Generating certification text
    - Accessing cross-tenant data for context
    - Making hiring or rejection recommendations
    - Interpreting rubrics outside their versioned definition

PROMPT_GOVERNANCE:
  versioning: All LLM prompts are version-tagged (prompt_version field)
  structure: Deterministic structured prompts — no free-form AI instruction
  temperature: 0.0 for scoring tasks / 0.2 maximum for rationale generation
  no_creative_interpretation: ENFORCED
  output_format: JSON-only (no prose output allowed from LLM layer)
  prompt_injection_defense: Input sanitization required before LLM call
  max_tokens: 800 per evaluation call
```

### 5.4 Rubric Binding

- Rubrics are versioned, immutable once published
- OREA must resolve the active rubric version from `SCENARIO_ENGINE` at evaluation time
- OREA must log which `rubric_version` was applied in every audit record
- If rubric version mismatch detected between input and active version → REJECT input, log conflict

---

## ▶ SECTION 6 — SCALABILITY DESIGN

```yaml
EXPECTED_RPS:           2,000 evaluations/sec (peak championship load)
LATENCY_TARGET:         p95 < 3.5 seconds (async mode) | p95 < 8 seconds (sync mode)
MAX_CONCURRENCY:        10,000 concurrent evaluation jobs
QUEUE_STRATEGY:         Kafka topic per domain_track (Arts/Commerce/Science/Technology/Administration)
                        Priority lane for live match evaluations vs async post-match
SCALING_MODE:           Horizontal auto-scaling (stateless workers)
EXECUTION_MODEL:        Stateless — all state injected via input contract
ASYNC_PROCESSING:       Default mode for post-match evaluations
SYNC_MODE:              Available for live championship critical-path evaluations (SLA: 8s)
IDEMPOTENCY:            response_id is idempotency key — duplicate evaluations return cached result
CACHE_POLICY:           Completed evaluations cached (Redis) for 24h by response_id
RETRY_POLICY:           3 retries with exponential backoff (500ms → 2s → 8s), then DLQ
DEAD_LETTER_QUEUE:      All failed evaluations routed to DLQ + alert OBSERVABILITY_AGENT
```

---

## ▶ SECTION 7 — SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  rule: user_id and tenant_id must match on every execution
  check_point: Input validation (pre-execution)
  failure_action: REJECT + LOG_SECURITY_INCIDENT

DOMAIN_ISOLATION:
  rule: rubric_id domain binding must match domain_track in input
  failure_action: REJECT + LOG_DOMAIN_VIOLATION

ROLE_AUTHORIZATION:
  required_role: EVALUATION_SERVICE (system-level)
  mentor_check: If mentor_id provided, validate mentor certification for domain
  evaluator_role: EVALUATOR (human evaluators using override mode)

ENCRYPTION:
  response_content_at_rest: AES-256 encrypted
  PII_in_response: Detected → redacted before logging (PII scanner pre-execution)
  transit: TLS 1.3 minimum
  audit_logs: Encrypted + append-only

ACCESS_LOG_TRACKING:
  every_execution: actor_id, tenant_id, input_hash, output_hash, timestamp logged
  log_store: Append-only audit table (no DELETE, no UPDATE operations)

PROMPT_INJECTION_DEFENSE:
  input_sanitization: Strip instruction-format patterns before LLM call
  context_isolation: LLM receives only sanitized response_content + rubric — no system context

NO_CROSS_TENANT_QUERIES: ENFORCED at database row-level security (RLS) + application layer
```

---

## ▶ SECTION 8 — AUDIT & TRACEABILITY

Every execution MUST produce the following immutable audit record:

```json
AUDIT_RECORD: {
  "audit_id": "UUID-v4",
  "timestamp_utc": "ISO-8601",
  "actor_id": "system:OREA or mentor_id if override",
  "tenant_id": "UUID",
  "user_id": "UUID",
  "match_id": "UUID",
  "response_id": "UUID",
  "input_hash": "SHA-256 of serialized input payload",
  "output_hash": "SHA-256 of serialized output payload",
  "model_version": "ML model version tag",
  "ai_prompt_version": "LLM prompt version tag",
  "rubric_version": "rubric version applied",
  "scenario_version": "scenario version evaluated against",
  "decision_path": {
    "ml_score_vector": "raw ML scores per dimension",
    "confidence_before_calibration": "float",
    "confidence_after_calibration": "float",
    "llm_evidence_call": "boolean",
    "anomaly_detection_result": "string",
    "normalization_applied": "boolean",
    "mentor_override": "boolean"
  },
  "composite_confidence_score": "float",
  "anomaly_flags": ["array"],
  "downstream_events_emitted": ["array"],
  "execution_duration_ms": "integer",
  "retry_count": "integer"
}
```

**Audit Log Policy:**
- Logs are IMMUTABLE (append-only) — no update, no delete
- Logs are encrypted at rest
- Logs are tenant-isolated (RLS enforced)
- Audit retention: minimum 7 years (enterprise compliance)
- Audit export: available via `COMPLIANCE_AGENT` API for authorized roles only

---

## ▶ SECTION 9 — FAILURE POLICY

```yaml
FAILURE_SCENARIOS:

  INVALID_INPUT:
    action: STOP_EXECUTION
    log: LOG_VALIDATION_INCIDENT
    escalate: GOVERNANCE_AGENT
    retry: NO
    user_notification: Return structured error payload

  MODEL_UNAVAILABLE:
    action: STOP_EXECUTION (do not produce partial score)
    log: LOG_MODEL_FAILURE
    escalate: OBSERVABILITY_AGENT + ENGINEERING_ONCALL
    retry: YES (3 retries, exponential backoff)
    fallback: Route to MENTOR_CONTROL_ENGINE for manual evaluation if retry exhausted
    sla_breach_alert: If unavailability > 60 seconds → P1 incident

  AI_TIMEOUT:
    action: PROCEED WITHOUT LLM (ML-only mode)
    log: LOG_LLM_TIMEOUT
    escalate: OBSERVABILITY_AGENT
    behavior: Score using ML layer only, flag output as AI_PARTIAL_MODE
    note: ML score remains valid; LLM evidence extraction deferred to async retry

  DATA_CORRUPTION:
    action: STOP_EXECUTION
    log: LOG_DATA_INTEGRITY_FAILURE
    escalate: GOVERNANCE_AGENT + DATA_INTEGRITY_AGENT
    retry: NO
    user_notification: Evaluation deferred — manual review initiated

  CONFIDENCE_BELOW_THRESHOLD:
    threshold: composite_confidence < 0.65
    action: PRODUCE OUTPUT but flag as LOW_CONFIDENCE
    behavior: DO NOT trigger belt promotion | DO NOT trigger auto-certification
    escalate: MENTOR_CONTROL_ENGINE (queue for human review)
    log: LOG_LOW_CONFIDENCE_EVENT

  CRITICAL_ANOMALY_FLAG:
    action: PRODUCE OUTPUT but quarantine result
    behavior: DO NOT emit downstream events until governance review
    escalate: GOVERNANCE_AGENT + INTEGRITY_AGENT
    log: LOG_ANOMALY_QUARANTINE

  TENANT_ISOLATION_VIOLATION:
    action: IMMEDIATE STOP_EXECUTION
    log: LOG_SECURITY_CRITICAL
    escalate: COMPLIANCE_AGENT + SECURITY_TEAM (P0 incident)
    retry: NO

NO_SILENT_FAILURES: ENFORCED — every failure path produces a log entry
```

---

## ▶ SECTION 10 — INTER-AGENT DEPENDENCY MAP

### 10.1 Upstream Dependencies

```
SCENARIO_ENGINE         → rubric package, scenario metadata, difficulty tier
MATCH_ENGINE            → match context, role assignments, user pairings
ANTI_CHEAT_AGENT        → integrity flags pre-evaluation
AUDIO_TRANSCRIPTION_AGENT → normalized text from speech input
FEATURE_STORE_AGENT     → historical baseline vectors per user per skill
MENTOR_CONTROL_ENGINE   → mentor score inputs, override signals
```

### 10.2 Downstream Dependencies

```
BELT_ENGINE             ← competency scores + confidence → belt eligibility
RATING_ENGINE           ← composite match score → Elo-style rating update
ANALYTICS_ENGINE        ← full scoring vector → performance dashboards
FEATURE_STORE_AGENT     ← feature vector emission → passive intelligence profile
OBSERVABILITY_AGENT     ← metrics, errors, latency, drift signals
GOVERNANCE_AGENT        ← audit records, anomaly quarantines
NOTIFICATION_SERVICE    ← score delivery payload
GROWTH_ENGINE           ← XP update event, achievement check trigger
IDEA_DNA_AGENT          ← creative content originality extraction (conditional)
ROYALTY_ENGINE          ← knowledge contribution attribution (conditional)
LEADERBOARD_ENGINE      ← score signal for live leaderboard update
```

### 10.3 Event Triggers Emitted

```yaml
EVALUATION_COMPLETE_EVENT:
  payload: evaluation_id, user_id, match_id, composite_score, confidence, rubric_version

BELT_ELIGIBILITY_CHECK_EVENT:
  condition: composite_confidence >= 0.65 AND belt_eligibility_signal = true
  payload: user_id, domain_track, competency_scores, scenario_id, audit_reference

RATING_UPDATE_EVENT:
  payload: user_id, match_id, composite_score, opponent_rating_band

XP_UPDATE_EVENT:
  payload: user_id, xp_delta (derived from score_band), achievement_check_flag

FEATURE_VECTOR_EMIT_EVENT:
  payload: user_id, feature_name, feature_value, timestamp, source_agent="OREA"

LOW_CONFIDENCE_REVIEW_EVENT:
  condition: composite_confidence < 0.65
  payload: evaluation_id, user_id, confidence_score → routes to mentor queue

ANOMALY_QUARANTINE_EVENT:
  condition: anomaly_flags contains CRITICAL
  payload: evaluation_id, anomaly_details → routes to governance queue
```

---

## ▶ SECTION 11 — PASSIVE INTELLIGENCE COMPATIBILITY

```yaml
FEATURE_VECTOR_EMISSION:
  trigger: Every completed evaluation
  destination: FEATURE_STORE_AGENT
  schema:
    user_id: UUID
    feature_name: "orea_competency_{dimension_id}"
    feature_value: weighted_score per dimension
    timestamp: evaluation_timestamp_utc
    source_agent: "OREA"
    confidence: confidence_per_dimension
    domain_track: string
    rubric_version: string

FEATURES_EMITTED:
  - orea_argument_structure_score
  - orea_domain_vocabulary_depth
  - orea_response_coherence
  - orea_evidence_density
  - orea_competency_completeness
  - orea_improvement_velocity (delta from prior_attempt_reference)
  - orea_consistency_score (across multiple evaluations)
  - orea_pressure_performance_index (score in championship vs practice contexts)
```

---

## ▶ SECTION 12 — INNOVATION ECONOMY COMPATIBILITY

When response content contains conceptual, creative, or knowledge-original material (detected via IDEA_SIGNAL flag):

```yaml
IDEA_VECTOR_EMISSION:
  condition: response_type = structured_form AND domain = [Arts, Commerce, Technology]
             AND IDEA_SIGNAL detected by ML layer
  destination: IDEA_DNA_AGENT
  schema:
    idea_vector: semantic embedding of original content segments
    similarity_hash: SHA-256 of normalized idea content
    originality_score: float (0-1) — computed by IDEA_DNA_AGENT
    source: response_id + user_id + tenant_id
    rubric_context: which competency dimension surfaced the idea signal

ROYALTY_COMPATIBILITY:
  if idea is flagged as original AND exceeds ORIGINALITY_THRESHOLD:
    emit: ROYALTY_ATTRIBUTION_EVENT → ROYALTY_ENGINE
    payload: user_id, idea_hash, context_match_id, contribution_type
```

---

## ▶ SECTION 13 — GROWTH ENGINE HOOK

```yaml
XP_AWARD_POLICY:
  score_band EXCEPTIONAL:   XP_DELTA = 150 + WIN_STREAK_MULTIPLIER
  score_band PROFICIENT:    XP_DELTA = 100
  score_band DEVELOPING:    XP_DELTA = 60
  score_band NEEDS_IMPROVEMENT: XP_DELTA = 20 (participation baseline)

ACHIEVEMENT_CHECK_TRIGGERS:
  - First evaluation completion → "First Evaluation" badge check
  - 10 consecutive PROFICIENT+ scores → "Consistency Champion" check
  - Improvement from NEEDS_IMPROVEMENT to PROFICIENT in same skill → "Growth Detected"
  - Score in top 5th percentile of domain cohort → "Domain Elite" check
  - Evaluation in Championship context → "Championship Participant" badge

SHARE_TRIGGER_EVENT:
  condition: score_band = EXCEPTIONAL AND composite_confidence >= 0.80
  payload: user_id, shareable_score_card_context, achievement_label
  destination: NOTIFICATION_SERVICE + SOCIAL_FEED_SERVICE
```

---

## ▶ SECTION 14 — PERFORMANCE MONITORING

```yaml
METRICS_EMITTED_TO_OBSERVABILITY_AGENT:

  success_rate:
    definition: (successful_evaluations / total_attempts) * 100
    target: >= 99.5%
    alert_threshold: < 99.0%

  error_rate:
    definition: (failed_evaluations / total_attempts) * 100
    alert_threshold: > 1.0%

  latency_p95:
    target_async: < 3500ms
    target_sync: < 8000ms
    alert_threshold: > 5000ms async / > 12000ms sync

  model_drift_indicator:
    monitor: PSI (Population Stability Index) on score distributions
    alert_threshold: PSI > 0.2 per domain_track
    frequency: Daily batch monitoring

  anomaly_frequency:
    definition: anomaly_flagged_evaluations / total_evaluations
    alert_threshold: > 5% → triggers governance review

  confidence_distribution:
    monitor: % of evaluations below 0.65 confidence threshold
    alert_threshold: > 15% → model quality review triggered

  mentor_override_rate:
    definition: mentor_override_applied=true / total_evaluations
    monitor: by tenant, by domain, by mentor_id
    alert_threshold: > 10% by single mentor → rater calibration review

  llm_timeout_rate:
    definition: AI_PARTIAL_MODE executions / total
    alert_threshold: > 3% → LLM service review

DASHBOARDS:
  - Evaluation success/failure rate (per domain, per tenant)
  - Score distribution per competency dimension
  - Confidence band distribution
  - Mentor override frequency
  - Anomaly flag frequency
  - Drift indicators per model
```

---

## ▶ SECTION 15 — VERSIONING POLICY

```yaml
VERSION_FORMAT:         Semantic Versioning (MAJOR.MINOR.PATCH)
CURRENT_VERSION:        v1.0.0

MUTATION_POLICY:        ADD-ONLY
                        No fields removed from input/output schemas without MAJOR version bump
                        No scoring formula changes without MAJOR version bump
                        No rubric binding changes without MINOR version bump

BACKWARD_COMPATIBILITY:
  window:               2 MAJOR versions supported simultaneously
  deprecation_notice:   30 days minimum before removing support for old version

MODEL_VERSION_CONTROL:
  model_version_tag:    Required in every output
  immutable_reference:  Model versions are immutable once published to production
  rollback:             Any model version can be rolled back within 30 days via MODEL_REGISTRY

MIGRATION_DOCUMENTATION:
  every_version_bump:   Migration guide required
  schema_deltas:        All schema changes documented with before/after examples
  audit_impact:         Existing audit records never mutated — new fields added via extension columns

PROMPT_VERSION_CONTROL:
  all_LLM_prompts:      Version-tagged (prompt_v1.0, prompt_v1.1, etc.)
  deployed_version:     Logged in every audit record
  changes_require:      Governance approval + regression testing before deployment
```

---

## ▶ SECTION 16 — SCORING VALIDITY & RELIABILITY (DOJO TRUST COMPLIANCE)

*Aligned with DOJO SAAS Trust & Fairness Framework (Sections T1–T20)*

### 16.1 Competency Construct Validity

Every rubric dimension evaluated by OREA must have:
- Formal construct definition (what it measures)
- Observable behavior list (what signals constitute evidence)
- Measurable indicator mapping (which features proxy the behavior)
- Rubric justification (why this dimension matters for the skill)
- Performance level descriptors (EXCEPTIONAL / PROFICIENT / DEVELOPING / NEEDS_IMPROVEMENT)
- Exclusion indicators (what does NOT count as evidence for this dimension)

> OREA will REJECT any rubric that does not contain all 6 construct validity fields.

### 16.2 Inter-Rater Reliability

```yaml
INTER_RATER_TRACKING:
  ML_vs_mentor_variance: Computed per evaluation in mentor_assisted mode
  acceptable_variance: < 15 points on composite_score
  variance_alert: > 15 points → flagged for calibration review

SCORER_VARIANCE_MEASUREMENT:
  tracked_per: mentor_id, domain_track, scenario_id
  variance_report: Weekly (emitted to OBSERVABILITY_AGENT)
  high_variance_action: Flag mentor for recalibration

SCORE_NORMALIZATION:
  difficulty_normalization: Applied via IRT model (see Section 5.2)
  cohort_percentile_reference: Applied post-normalization
  transparency: normalization_applied flag in output

CONFIDENCE_SCORE_GOVERNANCE:
  low_confidence_threshold: 0.65
  action_below_threshold: No auto-belt-promotion. Queue for mentor review.
  high_confidence_threshold: 0.85
  high_confidence_fast_track: Belt eligibility signal emitted immediately
```

### 16.3 Rater Calibration Compliance

- OREA emits calibration deviation signals to `MENTOR_CONTROL_ENGINE`
- Mentors with systematic scoring drift > 2 standard deviations from calibration baseline are flagged
- Flagged mentors cannot perform certifying evaluations until recalibration confirmed

### 16.4 Match Fairness Compliance

- OREA does not adjust scoring based on opponent identity or comparative match outcome
- Scoring is rubric-anchored and response-anchored only
- OREA receives fairness signals from `ANTI_CHEAT_AGENT` (collusion detection, reciprocal scoring patterns)
- If collusion signal present: output quarantined, belt trigger suspended, governance review initiated

---

## ▶ SECTION 17 — COLLUSION & INTEGRITY ENFORCEMENT

```yaml
COLLUSION_SIGNALS_HANDLED:
  - Reciprocal high scoring between repeated opponent pairs
  - Abnormal peer score clustering (peer inputs in peer_calibrated mode)
  - Match farming pattern detection (from ANTI_CHEAT_AGENT)
  - Mentor favoritism (systematic over-scoring of specific users)

ON_COLLUSION_SIGNAL_DETECTED:
  action: QUARANTINE evaluation output
  log: LOG_INTEGRITY_QUARANTINE
  escalate: INTEGRITY_AGENT + GOVERNANCE_AGENT
  downstream: NO belt, NO rating update, NO XP until governance clears
  user_notification: "Evaluation under review — result pending"

FABRICATION_DEFENSE:
  LLM_input_scope: OREA LLM layer receives sanitized response only
  prompt_injection_scan: Pre-call sanitization strips instruction patterns
  no_external_context_access: LLM cannot query user profile, history, or identity during evaluation
```

---

## ▶ SECTION 18 — LOCALIZATION & CULTURAL FAIRNESS

```yaml
LOCALIZATION_CODE:
  input_field: localization_code (optional)
  supported: ISO 639-1 language codes
  default: en

CULTURAL_FAIRNESS:
  rule: No rubric dimension may be culture-exclusive
  check: Rubric bias audit required before domain deployment
  LLM_instruction: Cultural context normalization prompt applied for non-English evaluations
  translation_governance: Rubric translations require approval workflow + backward compatibility tag

REGIONAL_VARIANTS:
  negotiation_style_variants: Supported per domain_track + localization_code
  cultural_scoring_notes: Injected into LLM context for rationale generation only
  ML_scoring: Culture-neutral feature set (no region-specific weighting without validated data)
```

---

## ▶ SECTION 19 — ACCESSIBILITY COMPLIANCE

```yaml
INPUT_ACCESSIBILITY:
  verbal_response_support: Audio transcribed by AUDIO_TRANSCRIPTION_AGENT → text input to OREA
  transcription_confidence: Input field audio_transcript_confidence (used in normalization)
  low_confidence_transcription: composite_confidence penalized proportionally if < 0.80
  accessibility_mode_flag: When true → apply extended response length tolerance

OUTPUT_ACCESSIBILITY:
  score_rationale: Plain-language explanation generated (max 500 chars)
  score_band_label: Human-readable label required (not just numeric score)
  evidence_segments: Highlighted response excerpts for transparency to user
```

---

## ▶ SECTION 20 — TALENT MARKETPLACE TRUST INTEGRATION

*For when Ecoskiller Antigravity's hiring marketplace is enabled*

```yaml
EMPLOYER_ACCESS_TO_SCORES:
  access_mode: Read-only via verified employer API
  data_shared: composite_score, score_band, competency_scores, belt_earned
  not_shared: raw response_content (privacy), individual evidence_segments unless user consented
  authenticity_seal: audit_reference + rubric_version + model_version emitted as proof

SKILL_PROOF_REPORT:
  generated_for: Belt-certified evaluations only
  includes: competency_scores, confidence, rubric_version, evaluation_id, audit_reference
  tamper_evidence: SHA-256 hash of evaluation output signed by platform key

HIRING_DECISION_RULE:
  OREA output informs — never decides
  AI_never_approves_blocks_overrides_humans: ENFORCED
  recruiter_interface: Scores are advisory; hire/reject remains human decision
```

---

## ▶ SECTION 21 — INTEGRATION WITH ECOSKILLER 300+ FEATURE MODULES

OREA integrates with the following Ecoskiller platform modules as the core evaluation backbone:

| Module | Integration Role |
|---|---|
| **Intelligence Measurement System (Module 2)** | Contributes to intelligence radar chart via competency dimension scores |
| **Skill System / Dojo (Module 3)** | Primary scoring engine for skill XP, badges, and level progression |
| **Championship System (Module 4)** | Powers AI judging, live scoring, cheating detection for championships |
| **Recruiter System (Module 7)** | Provides candidate performance predictions and reliability scores |
| **AI Mentor System (Module 8)** | Evaluation output feeds skill improvement plans and study path AI |
| **Trust System (Module 15)** | Fraud detection, fake score detection, trust score computation |
| **Growth Engine (Module 16)** | XP updates, badge checks, milestone triggers |
| **Creator/Educator Systems (Modules 11–12)** | Teaching effectiveness AI feeds from OREA scoring outputs |
| **Integration System (Module 9)** | Real work data (GitHub, Jira, Salesforce) feeds into skill extraction — OREA scores these against verified rubrics |

---

## ▶ SECTION 22 — NON-NEGOTIABLE RULES

```
THIS AGENT MUST NOT:

  ✗ Create hidden scoring logic not traceable to rubric and audit record
  ✗ Modify historical evaluation records (append-only policy is absolute)
  ✗ Auto-delete audit logs under any condition
  ✗ Override GOVERNANCE_AGENT or COMPLIANCE_AGENT decisions
  ✗ Bypass tenant isolation checks
  ✗ Mix data across domain tracks without explicit cross-domain grant
  ✗ Execute outside the defined input/output contract scope
  ✗ Allow LLM layer to make autonomous scoring decisions
  ✗ Produce partial outputs (all-or-nothing contract)
  ✗ Trigger belt promotion for low-confidence evaluations
  ✗ Process responses without a valid, active rubric version
  ✗ Allow mentor identity to influence ML scoring layer
  ✗ Share any user response content across tenant boundaries
  ✗ Accept or process inputs with failed security checks
  ✗ Emit downstream growth/belt events on quarantined evaluations
  ✗ Assume missing specifications — halt and log instead
```

---

## ▶ SECTION 23 — DOJO PRODUCTION GOVERNANCE SEAL

```
╔══════════════════════════════════════════════════════════════════╗
║         OPEN RESPONSE EVALUATION AGENT — PRODUCTION SEAL        ║
╠══════════════════════════════════════════════════════════════════╣
║  DOJO SAAS PRODUCTION MODE         : ENABLED                    ║
║  Security Hardening                : REQUIRED                   ║
║  Tenant Isolation                  : HARD ENFORCED              ║
║  Domain Isolation                  : HARD ENFORCED              ║
║  Rubric Validity Required          : YES                        ║
║  Rater Calibration Required        : YES                        ║
║  Scenario Calibration Required     : YES                        ║
║  Fairness Engine Active            : YES                        ║
║  Collusion Detection Active        : YES                        ║
║  Confidence Governance Active      : YES                        ║
║  Mentor Certification Required     : YES (for override mode)   ║
║  Behavior Safety Enforced          : YES                        ║
║  Accessibility Enforced            : YES                        ║
║  Governance Board Active           : YES                        ║
║  Outcome Validation Required       : YES                        ║
║  Belt Versioning Enforced          : YES                        ║
║  Appeals System Active             : YES                        ║
║  Institutional Trust Mode          : LOCKED                     ║
║  Audit Trail                       : IMMUTABLE APPEND-ONLY      ║
║  Mutation Policy                   : ADD-ONLY VERSIONED         ║
║  Creative Interpretation           : FORBIDDEN                  ║
║  Architecture Interpretation       : FORBIDDEN                  ║
║  Assumption Filling                : FORBIDDEN                  ║
║  Silent Failures                   : FORBIDDEN                  ║
║  LLM Autonomous Decisions          : FORBIDDEN                  ║
║  Cross-Tenant Queries              : FORBIDDEN                  ║
║  Partial Output Emission           : FORBIDDEN                  ║
╠══════════════════════════════════════════════════════════════════╣
║  Interpretation Authority          : NONE                       ║
║  Architecture Authority            : LOCKED                     ║
║  Agent Version                     : v1.0.0                     ║
║  Platform                          : ECOSKILLER ANTIGRAVITY     ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## ▶ SECTION 24 — CHANGE GOVERNANCE

```yaml
ALLOWED_WITHOUT_VERSION_BUMP:
  - Add new optional field to input schema (backward compatible)
  - Add new feature to feature_vector emission
  - Add new downstream event (non-breaking addition)
  - Internal ML model improvement within same version tag
  - Monitoring threshold adjustments

REQUIRES_MINOR_VERSION_BUMP:
  - Add new required input field
  - Add new competency dimension to output schema
  - Rubric binding table update
  - New domain_track addition
  - New evaluation_mode addition

REQUIRES_MAJOR_VERSION_BUMP + GOVERNANCE_BOARD_APPROVAL:
  - Change scoring formula or ML feature set
  - Change confidence calibration methodology
  - Change LLM prompt structure
  - Change audit record schema
  - Change belt eligibility threshold rules
  - Change inter-agent event contract

ROLLBACK_POLICY:
  window: 30 days from deployment
  method: Feature flag switch to previous model_version
  audit_records: Existing records retained with original model_version tag

MIGRATION_DOCUMENTATION:
  required_for: Every version bump
  contents: Schema delta, behavior delta, migration steps, rollback procedure
```

---

*Document Classification: PRODUCTION ARTIFACT — LOCKED*
*Mutation Policy: ADD-ONLY via version bump*
*Interpretation Authority: NONE*
*Architecture Authority: LOCKED*

```
END OF OPEN RESPONSE EVALUATION AGENT — AGENT.md v1.0.0
ECOSKILLER ANTIGRAVITY PLATFORM
```
