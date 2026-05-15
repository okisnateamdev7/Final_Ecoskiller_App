# 🔒 CAREER_PREDICTION_AGENT
## ECOSKILLER ANTIGRAVITY — INTELLIGENCE & INNOVATION CORE
**Status:** `FINAL · SEALED · LOCKED · GOVERNED · DETERMINISTIC`
**Mutation Policy:** Add-only via version bump
**Interpretation Authority:** NONE
**Execution Authority:** Human declaration only
**Seal Version:** v1.0.0
**Owner Class:** ML · Intelligence · Safety

---

## ░░ AGENT IDENTITY BLOCK ░░

```
AGENT_NAME         = CAREER_PREDICTION_AGENT
SYSTEM_ROLE        = Career Trajectory Forecaster & Skill-Gap Intelligence Engine
PRIMARY_DOMAIN     = Intelligence Lane (Lane F — Ecoskiller Antigravity)
EXECUTION_MODE     = Deterministic + Validated
DATA_SCOPE         = User career history · skill graph · job market data · 
                     learning activity · assessment outcomes · peer benchmarks
TENANT_SCOPE       = Strict Isolation (Zero cross-tenant queries)
FAILURE_POLICY     = HALT on ambiguity · LOG incident · ESCALATE to Safety Owner
VERSION            = v1.0.0
ARCHITECTURE       = Microservices + Event-Driven
SECURITY_MODEL     = Zero-Trust Multi-Tenant Isolation
DATA_POLICY        = Append-Only Audit Trail
SCALE_TARGET       = 10M–100M users
ML_USAGE           = 75% Traditional ML (primary decisioning)
AI_USAGE           = 25% LLM / Semantic Reasoning (explainability assist only)
```

---

## 1️⃣ PURPOSE DECLARATION

### Problem Solved
The CAREER_PREDICTION_AGENT solves the systemic inability of learners, institutions, and employers on the Ecoskiller Antigravity platform to navigate the gap between current skill state and career destination. Without predictive intelligence, users face static skill trees, no foresight into evolving job market demand, and no personalized roadmap to close competency gaps before they become career blockers.

### What Input It Consumes
- User profile: skills, certifications, education level, experience timeline
- Behavioral signals: learning streaks, course completions, quiz performance, time-on-skill
- Job market data: live and historical job postings, role taxonomy, demand indices
- Peer benchmark vectors: cohort performance distributions from FEATURE_STORE_AGENT
- Assessment outcomes: skill scores, competency ratings, interview mock results
- External signals: industry trend vectors from TREND_INTELLIGENCE_AGENT

### What Output It Produces
- `career_prediction_bundle`: ranked career path predictions with probability scores
- `skill_gap_report`: ordered list of missing or underdeveloped competencies per predicted path
- `next_action_recommendations`: concrete learning actions mapped to gap closure
- `market_alignment_score`: how strongly current profile matches live market demand
- `confidence_score`: overall prediction confidence (0.0–1.0)
- `audit_reference`: UUID per execution for full traceability

### Downstream Agents That Depend on This Agent
| Agent | Dependency |
|---|---|
| RECOMMENDATION_ENGINE_AGENT | Receives career path context for course suggestions |
| RANK_UPDATE_AGENT | Receives XP and achievement triggers |
| NOTIFICATION_MANAGER_AGENT | Receives user nudge triggers for gap actions |
| JOB_MATCH_AGENT | Receives predicted role affinity scores |
| FEATURE_STORE_AGENT | Receives emitted feature vectors |
| OBSERVABILITY_AGENT | Receives performance metrics |

### Upstream Agents That Feed This Agent
| Agent | Data Provided |
|---|---|
| FEATURE_STORE_AGENT | User behavior feature vectors |
| SKILL_GRAPH_AGENT | Validated skill taxonomy and user skill state |
| ASSESSMENT_RESULT_AGENT | Competency scores and evaluation outcomes |
| JOB_TAXONOMY_AGENT | Canonical job role definitions and market demand scores |
| TREND_INTELLIGENCE_AGENT | Industry and technology trend signals |
| IDENTITY_AGENT | Tenant-validated user identity and role context |

---

## 2️⃣ ML / AI LOGIC LAYER

### Primary Layer — ML (75% of decisions)

```yaml
MODEL_TYPE: Multi-Label Classification + Time-Series Regression

MODELS_DEPLOYED:
  - career_classifier_v1:
      type: Gradient Boosted Classification (XGBoost)
      purpose: Predict top-N career paths for user
      output: Ranked career_path_ids with probability scores

  - skill_gap_ranker_v1:
      type: Learning-to-Rank (LambdaMART)
      purpose: Order skill gaps by urgency and impact
      output: Ranked skill_gap_list

  - market_demand_forecaster_v1:
      type: Time-Series Regression (Prophet + LSTM)
      purpose: Forecast job role demand over 6/12/24 months
      output: demand_index per role per time horizon

FEATURES_USED:
  - skills_verified_count
  - skills_in_progress_count
  - certification_score_avg
  - learning_streak_current
  - learning_streak_max
  - course_completion_rate
  - assessment_pass_rate
  - quiz_accuracy_score
  - experience_years_total
  - education_level_encoded
  - current_role_encoded
  - industry_sector_encoded
  - cohort_percentile_rank
  - time_on_platform_days
  - job_applications_count
  - job_match_response_rate
  - trend_vector_embedding (128-dim)
  - skill_graph_centrality_score

TRAINING_FREQUENCY:
  career_classifier:    Weekly (Sunday 00:00 UTC)
  skill_gap_ranker:     Weekly (Sunday 02:00 UTC)
  market_forecaster:    Monthly (1st of month 00:00 UTC)

DRIFT_DETECTION:
  method: Population Stability Index (PSI) + KS Test
  triggers:
    - PSI > 0.2 on any feature → ALERT + flag model for re-evaluation
    - Accuracy degradation > 5% over 2 weeks → ALERT + halt predictions
    - Distribution shift in output labels → ESCALATE to ML Owner

VERSION_CONTROL:
  storage: immutable model registry (MinIO bucket: /models/career_prediction/)
  reference: model_version UUID stored with every prediction output
  rollback: previous version activated within 15 minutes on failure
```

### Secondary Layer — AI (25% of decisions — explainability assist only)

```yaml
AI_USAGE_SCOPE:
  purpose: Generate human-readable career path explanations
  input:  structured ML prediction bundle
  output: natural language explanation text (max 300 tokens)
  boundary: AI must NOT modify prediction scores or rankings
  boundary: AI must NOT make career decisions — it only explains ML decisions

PROMPT_GOVERNANCE:
  version:    prompt_v1.0.0
  structure:  deterministic template — no open-ended generation
  template: |
    SYSTEM: You are a career explanation assistant for Ecoskiller.
    You receive a structured prediction bundle. Your ONLY task is to write
    a 2–3 sentence plain-language explanation of WHY the top career path
    was predicted and WHAT the user must do next. Do NOT invent data.
    Do NOT add paths not in the bundle. Do NOT modify scores.

    INPUT: {{prediction_bundle_json}}

    OUTPUT FORMAT (strict):
    {
      "explanation_text": "<2-3 sentences>",
      "top_action_phrase": "<1 action sentence>",
      "prompt_version": "prompt_v1.0.0"
    }

  fallback: if AI returns malformed JSON → use template fallback text
  timeout:  3 seconds → fallback to static template
  no creative interpretation allowed
  no autonomous decisions permitted
```

---

## 3️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA: {
  "required_fields": [
    "user_id",
    "tenant_id",
    "request_timestamp_utc",
    "skill_snapshot": {
      "verified_skills": ["skill_id", "proficiency_level", "verified_at"],
      "in_progress_skills": ["skill_id", "progress_pct"]
    },
    "education_profile": {
      "level": "enum[school|diploma|undergraduate|postgraduate|phd]",
      "field_of_study": "string"
    },
    "experience_profile": {
      "years_total": "integer",
      "current_role_id": "string",
      "industry_sector_id": "string"
    },
    "feature_vector_ref": "UUID (from FEATURE_STORE_AGENT)"
  ],

  "optional_fields": [
    "career_goal_preference",
    "geographic_preference",
    "remote_preference",
    "salary_expectation_band",
    "learning_time_availability_hrs_per_week"
  ],

  "validation_rules": [
    "user_id must match authenticated session token",
    "tenant_id must match JWT tenant claim",
    "skill proficiency_level must be in [1,2,3,4,5]",
    "education level must match defined enum",
    "years_total must be integer >= 0 and <= 60",
    "feature_vector_ref must resolve in FEATURE_STORE_AGENT",
    "request_timestamp_utc must be ISO 8601 format"
  ],

  "security_checks": [
    "Validate JWT signature before processing",
    "Verify tenant_id isolation — reject cross-tenant references",
    "Validate RBAC permission: career_prediction:read",
    "Reject if user_id is flagged in ABUSE_DETECTION_AGENT",
    "Enforce rate limit: max 10 prediction requests per user per hour"
  ],

  "domain_checks": [
    "skill_ids must exist in canonical SKILL_GRAPH_AGENT registry",
    "current_role_id must exist in JOB_TAXONOMY_AGENT registry",
    "industry_sector_id must exist in approved sector taxonomy",
    "feature_vector_ref age must be <= 24 hours"
  ]
}
```

**Rules:**
- No null tolerance on required fields without explicit null policy declaration
- Reject all malformed or incomplete inputs with structured error response
- Log all validation failures to audit trail before rejecting

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "prediction_id": "UUID",
    "user_id": "string",
    "tenant_id": "string",
    "career_predictions": [
      {
        "rank": 1,
        "career_path_id": "string",
        "career_path_label": "string",
        "probability_score": 0.0-1.0,
        "market_demand_index": 0.0-1.0,
        "time_to_achieve_estimate_months": "integer",
        "skill_gaps": [
          {
            "skill_id": "string",
            "skill_name": "string",
            "urgency_rank": "integer",
            "gap_severity": "enum[critical|moderate|minor]"
          }
        ],
        "next_actions": [
          {
            "action_type": "enum[course|assessment|project|certification]",
            "entity_id": "string",
            "entity_label": "string",
            "priority": "integer"
          }
        ]
      }
    ],
    "market_alignment_score": 0.0-1.0,
    "explanation_text": "string (AI-generated, governance-validated)",
    "top_action_phrase": "string"
  },

  "confidence_score": 0.0-1.0,

  "model_version": {
    "career_classifier": "string",
    "skill_gap_ranker": "string",
    "market_forecaster": "string",
    "prompt_version": "string"
  },

  "audit_reference": "UUID",
  "response_timestamp_utc": "ISO 8601",

  "next_trigger_events": [
    "FEATURE_STORE_EMIT_EVENT",
    "RANK_UPDATE_EVENT",
    "NOTIFICATION_TRIGGER_EVENT"
  ]
}
```

**All outputs MUST include:** Confidence score · Model version reference · Traceability UUID · Next trigger events

---

## 5️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS:         500 (steady state) / 5,000 (peak burst)
LATENCY_TARGET:       P95 < 800ms end-to-end
MAX_CONCURRENCY:      2,000 simultaneous predictions
QUEUE_STRATEGY:       Redis Streams — topic: career_prediction_requests
PROCESSING_MODE:      Async with sync fallback for UI-triggered calls
EXECUTION_STATE:      Stateless — all state fetched per request
IDEMPOTENCY:          Identical input_hash → return cached result (TTL: 1 hour)

SCALING_RULES:
  horizontal:         Kubernetes HPA — scale on CPU > 70% or queue depth > 1,000
  pod_min:            3
  pod_max:            50
  cooldown:           120 seconds
```

---

## 6️⃣ SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  - All queries scoped by tenant_id at database level
  - No cross-tenant joins permitted under any condition
  - Tenant context validated from JWT on every request

DOMAIN_ISOLATION:
  - Career domain data may not bleed into billing, HR, or marketplace domains
  - Feature vectors consumed only from FEATURE_STORE_AGENT (no direct DB reads)

ROLE_BASED_AUTHORIZATION:
  Required permission: career_prediction:read
  Admin override permission: career_prediction:admin
  No role may call this agent without explicit RBAC assignment

ENCRYPTION:
  - Data in transit: TLS 1.3 minimum
  - Data at rest: AES-256 (Kubernetes Secrets + MinIO server-side encryption)
  - Model artifacts: encrypted in MinIO model registry

AUDIT_LOGGING:
  - All requests logged before processing begins (append-only)
  - All outputs logged immediately upon generation
  - Logs stored in immutable Loki audit stream: career_prediction_audit
  - Log retention: minimum 7 years

ACCESS_LOG_TRACKING:
  - Every actor_id, IP, and user-agent logged per request
  - Anomalous access patterns → flagged to FRAUD_DETECTION_AGENT
```

---

## 7️⃣ AUDIT & TRACEABILITY

Every execution MUST emit this exact audit event:

```json
AUDIT_EVENT: {
  "timestamp_utc":       "ISO 8601",
  "actor_id":            "user_id making the request",
  "tenant_id":           "tenant scope",
  "agent_id":            "CAREER_PREDICTION_AGENT",
  "agent_version":       "v1.0.0",
  "input_hash":          "SHA-256 of full input payload",
  "output_hash":         "SHA-256 of full output payload",
  "model_version":       { "classifier": "...", "ranker": "...", "forecaster": "..." },
  "confidence_score":    0.0-1.0,
  "decision_path":       ["step_1_validation", "step_2_feature_fetch", "step_3_classification", "step_4_gap_ranking", "step_5_ai_explain"],
  "latency_ms":          "integer",
  "anomaly_flags":       [],
  "prediction_id":       "UUID",
  "audit_reference":     "UUID (same as output)"
}
```

**Logs are immutable.** No agent, admin, or process may modify or delete audit logs. Violation → STOP EXECUTION.

---

## 8️⃣ FAILURE POLICY

```yaml
FAILURE_SCENARIOS:

  invalid_input:
    action: STOP_EXECUTION
    log: LOG_VALIDATION_FAILURE with field-level detail
    response: structured 422 error with rejection reason
    escalate: NONE (expected failure type)

  model_unavailable:
    action: STOP_EXECUTION
    log: LOG_INCIDENT — model_registry_unavailable
    response: 503 with retry-after header (60 seconds)
    escalate: ESCALATE_TO = ML Owner + OBSERVABILITY_AGENT
    retry_policy: 3 attempts with exponential backoff (1s, 4s, 16s)

  ai_timeout:
    action: USE_FALLBACK — static explanation template
    log: LOG_AI_TIMEOUT with duration
    response: return prediction bundle with fallback explanation_text
    escalate: NONE (graceful degradation)

  data_corruption:
    action: STOP_EXECUTION
    log: LOG_INCIDENT — data_integrity_failure
    response: 500 with incident_reference
    escalate: ESCALATE_TO = Safety Owner + Platform Security Admin
    retry_policy: NONE — halt until human review

  confidence_below_threshold:
    threshold: 0.40
    action: FLAG_LOW_CONFIDENCE in output
    response: return prediction with low_confidence_warning flag
    log: LOG_LOW_CONFIDENCE_PREDICTION
    escalate: NONE (non-blocking — user informed)

  drift_detected:
    action: FLAG_DRIFT in output
    log: LOG_DRIFT_EVENT with PSI values
    escalate: ESCALATE_TO = ML Owner (async)
    response: continue with current model + drift warning flag in output

  rate_limit_exceeded:
    action: STOP_EXECUTION
    log: LOG_RATE_LIMIT — actor_id
    response: 429 with retry-after header

GLOBAL_RULE:
  NO silent failures permitted.
  Every failure must produce a structured log event and response.
```

---

## 9️⃣ INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - IDENTITY_AGENT          → provides tenant-validated user identity
  - FEATURE_STORE_AGENT     → provides user behavior feature vectors
  - SKILL_GRAPH_AGENT       → provides canonical skill taxonomy + user skill state
  - ASSESSMENT_RESULT_AGENT → provides competency and evaluation scores
  - JOB_TAXONOMY_AGENT      → provides job role definitions and demand signals
  - TREND_INTELLIGENCE_AGENT → provides industry trend embedding vectors

DOWNSTREAM_AGENTS:
  - RECOMMENDATION_ENGINE_AGENT  → receives career path context
  - JOB_MATCH_AGENT              → receives role affinity scores
  - RANK_UPDATE_AGENT            → receives XP and achievement triggers
  - NOTIFICATION_MANAGER_AGENT   → receives user nudge triggers
  - FEATURE_STORE_AGENT          → receives emitted feature vectors (feedback loop)
  - OBSERVABILITY_AGENT          → receives all performance metrics

EVENT_TRIGGERS_EMITTED:
  CAREER_PREDICTION_COMPLETED:
    schema: { prediction_id, user_id, tenant_id, top_career_path_id, confidence_score, timestamp_utc }

  SKILL_GAP_IDENTIFIED:
    schema: { user_id, tenant_id, skill_gap_list, urgency_ranks, timestamp_utc }

  FEATURE_STORE_EMIT_EVENT:
    schema: { user_id, feature_name, feature_value, timestamp, source_agent: "CAREER_PREDICTION_AGENT" }

  RANK_UPDATE_EVENT:
    schema: { user_id, xp_delta, reason: "career_prediction_completed", timestamp_utc }

  NOTIFICATION_TRIGGER_EVENT:
    schema: { user_id, notification_type: "career_action_nudge", payload_ref: prediction_id }
```

---

## 🔟 PASSIVE INTELLIGENCE COMPATIBILITY

This agent touches user behavior. It MUST emit feature vectors to FEATURE_STORE_AGENT after every successful prediction:

```json
EMIT_FEATURE_VECTOR: [
  {
    "user_id":       "string",
    "feature_name":  "career_prediction_confidence",
    "feature_value": 0.0-1.0,
    "timestamp":     "ISO 8601",
    "source_agent":  "CAREER_PREDICTION_AGENT"
  },
  {
    "user_id":       "string",
    "feature_name":  "top_career_path_id",
    "feature_value": "career_path_id string",
    "timestamp":     "ISO 8601",
    "source_agent":  "CAREER_PREDICTION_AGENT"
  },
  {
    "user_id":       "string",
    "feature_name":  "skill_gap_critical_count",
    "feature_value": "integer",
    "timestamp":     "ISO 8601",
    "source_agent":  "CAREER_PREDICTION_AGENT"
  },
  {
    "user_id":       "string",
    "feature_name":  "market_alignment_score",
    "feature_value": 0.0-1.0,
    "timestamp":     "ISO 8601",
    "source_agent":  "CAREER_PREDICTION_AGENT"
  }
]
```

---

## 1️⃣1️⃣ GROWTH ENGINE HOOK

This agent affects user ranking and achievement. It MUST trigger:

```yaml
ON SUCCESSFUL PREDICTION:
  RANK_UPDATE_EVENT:
    xp_delta:  +10 (for completing career prediction)
    reason:    "career_prediction_completed"

  XP_UPDATE_EVENT:
    xp_delta:  +10
    user_id:   from request context

ON TOP CAREER PATH ACHIEVED (future hook — downstream):
  SHARE_TRIGGER_EVENT:
    trigger_type: "career_milestone_share"
    payload_ref:  prediction_id
```

---

## 1️⃣2️⃣ PERFORMANCE MONITORING

```yaml
METRICS_EMITTED_TO_OBSERVABILITY_AGENT:

  success_rate:
    definition:  predictions completed without error / total requests
    target:      >= 99.5%

  error_rate:
    definition:  failed predictions / total requests
    alert_threshold: > 0.5%

  latency_p95:
    definition:  95th percentile end-to-end response time
    target:      < 800ms
    alert_threshold: > 1200ms

  drift_indicator:
    definition:  PSI score on primary input features
    alert_threshold: PSI > 0.2

  confidence_distribution:
    definition:  histogram of confidence_score across predictions
    alert_threshold: > 10% of predictions below 0.40

  ai_fallback_rate:
    definition:  rate of AI explanation timeouts triggering fallback
    alert_threshold: > 5%

  anomaly_frequency:
    definition:  count of anomaly_flags emitted per hour
    alert_threshold: > 50 per hour

DASHBOARD:
  Grafana panel: career_prediction_agent_overview
  Alerts routed to: Platform Safety Owner + ML Owner via PagerDuty
```

---

## 1️⃣3️⃣ VERSIONING POLICY

```yaml
VERSION_CONTROL:
  current_version: v1.0.0
  policy: Add-only · Versioned · Backward compatible
  migration: documented in MIGRATION_LOG before deployment
  rollback: previous version must activate within 15 minutes
  breaking_changes: PROHIBITED without Safety Owner sign-off

AGENT_CHANGELOG:
  v1.0.0:
    date: 2026-02-25
    author: Ecoskiller Intelligence Core
    description: Initial sealed and locked agent definition
    changes: INITIAL RELEASE
```

---

## 1️⃣4️⃣ USER TYPE SCOPE

This agent serves the following user types from the Ecoskiller 300-user taxonomy:

**Learner Segment (primary):** School students (Grade 9–12), Diploma students, Undergraduate students, Postgraduate students, PhD scholars, Distance learners, Dropout learners, Career switchers, Working professional learners, Fresher job seekers, Bootcamp students, all domain-specific learners (AI/ML, Cybersecurity, Data Science, etc.)

**Institutional Segment:** Institute placement officers, Institute counselors, Student success managers, Academic deans — consuming aggregated (anonymized) career prediction analytics.

**Employer Segment:** HR managers, Technical recruiters, Campus hiring managers — consuming role-demand intelligence outputs.

**Platform Operations:** Platform admins, Analytics admins, BI analysts — consuming monitoring dashboards and audit logs.

**Future/Advanced Roles:** AI career advisors, Career path designers, Skill gap analysts, Workforce AI planners — consuming advanced prediction bundles.

---

## 1️⃣5️⃣ NON-NEGOTIABLE RULES — SEALED

This agent **MUST NOT:**

```
❌ Create hidden logic or undocumented decision paths
❌ Modify historical records or audit logs
❌ Auto-delete any audit event
❌ Override governance agents or bypass compliance checks
❌ Mix domain data (career data must not touch billing, HR, or marketplace data)
❌ Execute outside defined scope (career prediction domain only)
❌ Allow AI layer to override or modify ML prediction scores
❌ Allow AI layer to generate career paths not in ML output
❌ Process requests without valid tenant-isolated JWT
❌ Accept input with cross-tenant data references
❌ Emit predictions with confidence_score below 0.40 without clear low-confidence warning flag
❌ Store personally identifiable data outside tenant-scoped encrypted storage
❌ Allow any model or prompt version to operate without version registry entry
❌ Execute with drifted models beyond 7 days without ML Owner re-validation
```

---

## 1️⃣6️⃣ SAFETY OWNER RESPONSIBILITIES

```yaml
SAFETY_OWNER_ROLE: ML · Intelligence · Safety Owner

RESPONSIBILITIES:
  1. Approve all model version deployments before production release
  2. Review drift alerts within 24 hours of trigger
  3. Sign-off on any change to AI prompt governance templates
  4. Review anomaly frequency alerts exceeding 50/hour
  5. Validate confidence threshold policy quarterly
  6. Conduct quarterly fairness and bias audit on prediction outputs:
       - Ensure career path predictions are not systematically biased
         by gender, geography, or institution type
       - Review prediction distributions across user segments
  7. Authorize any rollback activation
  8. Own incident escalation for data_corruption failures
  9. Certify agent for production deployment (signature required before go-live)

FAIRNESS_AUDIT_SCHEDULE: Quarterly
BIAS_METRICS_MONITORED:
  - career_path_distribution_by_gender
  - career_path_distribution_by_geography
  - career_path_distribution_by_institution_tier
  - confidence_score_distribution_by_user_segment

BIAS_ALERT_THRESHOLD:
  - If any demographic group receives systematically lower confidence scores
    → ESCALATE to Safety Owner + HALT predictions for that segment
    → Root cause analysis within 48 hours
```

---

## 🔐 SEAL DECLARATION

```
═══════════════════════════════════════════════════════════════════════════════
AGENT NAME:    CAREER_PREDICTION_AGENT
VERSION:       v1.0.0
SEAL DATE:     2026-02-25
PLATFORM:      ECOSKILLER ANTIGRAVITY
LANE:          F — Intelligence
OWNER CLASS:   ML · Intelligence · Safety

THIS AGENT SPECIFICATION IS:
  ✔ SEALED         — No interpretation permitted
  ✔ LOCKED         — No undeclared modifications permitted
  ✔ GOVERNED       — Subject to Safety Owner oversight
  ✔ DETERMINISTIC  — Identical input produces identical output
  ✔ AUDITABLE      — Every execution produces immutable audit trail
  ✔ VERSIONED      — All changes require version bump
  ✔ ISOLATED       — Zero cross-tenant operation

Mutation: Add-only via version bump approved by Safety Owner
Violation of any non-negotiable rule → STOP EXECUTION → REPORT → ESCALATE

═══════════════════════════════════════════════════════════════════════════════
```

---

*End of CAREER_PREDICTION_AGENT v1.0.0 — SEALED AND LOCKED*
