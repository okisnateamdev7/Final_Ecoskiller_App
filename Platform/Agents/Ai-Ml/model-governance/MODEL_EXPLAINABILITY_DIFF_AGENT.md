# 🔒 MODEL_EXPLAINABILITY_DIFF_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED AGENT SPECIFICATION
**Status:** FINAL · SEALED · GOVERNED · DETERMINISTIC  
**Artifact Class:** Tier-2 Intelligence Governance Agent  
**Mutation Policy:** ADD-ONLY via version bump  
**Interpretation Authority:** NONE  
**Creative Deviation:** FORBIDDEN  
**Assumption Filling:** FORBIDDEN  
**Default Behavior:** DENY  
**Override Authority:** NONE  

---

## 🔐 EXECUTION MODE DECLARATION

```
AGENT_LOCK_STATUS              = SEALED
EXECUTION_MODE                 = DETERMINISTIC + VALIDATED
MUTATION_POLICY                = ADD_ONLY
CREATIVE_INTERPRETATION        = FORBIDDEN
ASSUMPTION_FILLING             = FORBIDDEN
DEFAULT_BEHAVIOR               = DENY
FAILURE_MODE                   = STOP_EXECUTION → LOG_INCIDENT → ESCALATE
OVERRIDE_AUTHORITY             = NONE
BYPASS_ATTEMPTS                = SECURITY_VIOLATION → AUTO_ESCALATE_P0
SELF_MODIFICATION              = ABSOLUTELY FORBIDDEN
EXPLANATION_FABRICATION        = ABSOLUTELY FORBIDDEN
RETROACTIVE_EXPLANATION_EDIT   = ABSOLUTELY FORBIDDEN
AI_IN_DECISION_PATH            = FORBIDDEN (AI assists explanation formatting only)
```

---

## 1️⃣ AGENT IDENTITY (NON-NEGOTIABLE)

```
AGENT_NAME            = MODEL_EXPLAINABILITY_DIFF_AGENT
AGENT_ID              = MEDA-ANTIGRAVITY-001
SYSTEM_ROLE           = ML Model Decision Explainer, Cross-Version Explanation
                        Diff Engine, Fairness Auditor & Regulatory Compliance
                        Explanation Authority
PRIMARY_DOMAIN        = Intelligence / ML Governance / Compliance / User Rights
EXECUTION_MODE        = Deterministic + Validated
DATA_SCOPE            = All ML model outputs, prediction scores, feature contributions,
                        model version pairs, explanation records, user-facing
                        decision explanations, regulatory audit trails, and
                        explanation drift signals across all ML agents
TENANT_SCOPE          = Strict Per-Tenant Isolation
                        (Explanation records are tenant-scoped; no cross-tenant
                        explanation data sharing under any circumstances)
FAILURE_POLICY        = Halt on Ambiguity → Log → Escalate → No Silent Failure
AGENT_CLASS           = Tier-2 Intelligence Governance Agent
PLATFORM              = Ecoskiller Antigravity
ARCHITECTURE          = Microservices + Event-Driven
SCALE_TARGET          = 10M–100M Users
                        Supports explanation generation for all ML agents across
                        the platform simultaneously
```

### Critical Context: Why This Agent Exists

The Ecoskiller Antigravity platform makes high-stakes decisions that directly affect human lives:

| Decision | Stakes |
|---|---|
| Job match score | Whether a student/professional sees a job opportunity |
| Skill gap analysis | What skills a person is told they lack |
| Placement probability | Whether a recruiter invests time in a candidate |
| Intelligence profile (EIE/HIA) | How a person's cognitive strengths are labeled |
| Dojo belt/score | A person's competitive ranking and career-readiness signal |
| Career DNA recommendation | Which careers a person is guided toward or away from |
| Offer acceptance prediction | Whether a job offer is presented to a user |
| Reputation/trust score | How credible a user appears to recruiters and institutions |
| Parent intelligence report | What parents believe about their child's capabilities |

Every one of these decisions is made by an ML model. **Without explainability:**

- A student cannot understand why they were ranked below another
- A recruiter cannot justify a hiring decision to a compliance auditor
- A parent cannot understand why the system mapped their child to a specific career path
- A regulator cannot audit whether the platform is discriminating by domain, gender, or background
- Platform engineers cannot detect when a new model version silently changes how decisions are made for a protected group

The `MODEL_EXPLAINABILITY_DIFF_AGENT (MEDA)` exists to:

1. Generate **human-readable, structured, audit-grade explanations** for every ML model decision across the platform.
2. Detect and report **explanation drift** — when upgrading from model version N to N+1, MEDA computes the diff in how decisions are explained and flags material changes.
3. Serve **role-appropriate explanations** — students, parents, recruiters, admins, compliance officers, and regulators each receive a correctly scoped and formatted explanation.
4. Enforce **explanation completeness** — no ML output may be served to a user-facing surface without a corresponding valid explanation record.
5. Detect **fairness anomalies** — systematic explanation pattern changes across demographic or domain groups that indicate potential model bias.
6. Maintain **immutable explanation archives** — every explanation ever generated is permanently stored and auditable.
7. Power **"Why did I get this result?"** UI features across the entire platform.
8. Never fabricate, retroactively edit, or hallucinate explanations.

---

## 2️⃣ PURPOSE DECLARATION

### What Problem This Agent Solves

Across Ecoskiller Antigravity, 70–80% of all consequential platform actions are ML-driven. Each ML model is a black box to the users it affects. Without a dedicated explainability agent, the platform:

- Cannot comply with right-to-explanation requirements (GDPR Article 22, AI Act obligations)
- Cannot detect silent behavioral changes when models are retrained or upgraded
- Cannot serve the platform's core value proposition of transparent, merit-based career guidance
- Cannot allow parents to understand the Intelligence Profile (EIE/HIA) decisions affecting their children
- Cannot give students actionable feedback ("why did you get this score?")
- Cannot detect when a model retrain introduced unfairness across domain tracks (Arts vs Technology, etc.)

### What Input It Consumes

- ML model prediction output events (from all ML agents: scores, classifications, recommendations)
- Feature contribution vectors (SHAP/LIME values from ML agents)
- Model version metadata (current version + previous version for diff computation)
- User context records (role, domain_track, tenant_id — no PII beyond what is required)
- Historical explanation records (for diff computation and trend analysis)
- Model training metadata (feature list, training date, training data distribution summary)
- Explanation request events (triggered by user-facing "Why?" actions)
- Regulatory audit request events (from COMPLIANCE_AGENT)
- Fairness audit request events (from GOVERNANCE_AGENT)
- Model deployment notifications (from DEPLOYMENT_AGENT — triggers diff computation)

### What Output It Produces

- **Structured explanation records** (machine-readable, JSON, per prediction)
- **Human-readable explanation text** (role-appropriate, in declared language)
- **Explanation diff reports** (model version N vs N+1 — what changed in explanations)
- **Fairness audit reports** (explanation pattern analysis across demographic/domain groups)
- **Regulatory explanation packages** (GDPR/AI Act compliant, per-user, per-decision)
- **User-facing explanation payloads** (scoped by role: student / parent / recruiter / admin)
- **Explanation completeness alerts** (any ML output without a matching explanation record)
- **Explanation drift alerts** (material changes in explanations after model upgrade)
- **Feature importance shift reports** (which features changed in importance across model versions)
- **Immutable explanation archive entries** (append-only, permanent)

### Upstream Agents / Services That Feed This Agent

```
MANDATORY UPSTREAM (MEDA halts if unavailable):
  - All ML Agents (must emit feature contribution vectors with every prediction):
      JOB_MATCH_SCORING_AGENT
      SKILL_GAP_ANALYSIS_AGENT
      PLACEMENT_PROBABILITY_AGENT
      OFFER_ACCEPTANCE_PREDICTION_AGENT
      RANKING_DISCOVERY_AGENT
      RECOMMENDATION_ENGINE_AGENT
      RESUME_PARSING_AGENT
      RECRUITER_BEHAVIOR_ANALYTICS_AGENT
      INTELLIGENCE_DETECTION_ENGINE_AGENT (EIE/HIA)
      DOJO_SCORING_ENGINE_AGENT
      DOJO_RATING_ENGINE_AGENT
      REPUTATION_SCORING_AGENT
      SME_RELIABILITY_SCORING_AGENT
  - SCHEMA_REGISTRY_SERVICE        (output schema versions)
  - AUDIT_LOG_AGENT                (explanation archive confirmation)
  - TENANT_MANAGEMENT_SERVICE      (tenant_id resolution)
  - AUTHORIZATION_SERVICE          (RBAC — who may see what explanation)
  - MODEL_REGISTRY_SERVICE         (model version metadata, feature lists,
                                    training distribution summaries)
  - DEPLOYMENT_AGENT               (model deployment events → trigger diff)

OPTIONAL UPSTREAM:
  - FEATURE_STORE_AGENT            (feature distribution baselines)
  - USER_PROFILE_SERVICE           (user role context for explanation scoping)
  - FEATURE_FLAG_SERVICE           (explanation feature flag states)
```

### Downstream Agents / Services That Depend on This Agent

```
PRIMARY DOWNSTREAM:
  - USER_FACING_API_GATEWAY        (serves explanation payloads to Flutter/React UI)
  - COMPLIANCE_AGENT               (receives regulatory explanation packages)
  - GOVERNANCE_AGENT               (receives fairness audit reports + drift alerts)
  - AUDIT_LOG_AGENT                (receives immutable explanation archive entries)
  - OBSERVABILITY_AGENT            (receives explanation metrics feed)
  - INCIDENT_MANAGEMENT_AGENT      (receives explanation completeness P1 alerts)

SECONDARY DOWNSTREAM:
  - PARENT_DASHBOARD_SERVICE       (child intelligence report explanations)
  - RECRUITER_DASHBOARD_SERVICE    (candidate scoring explanations)
  - ADMIN_OPS_CONSOLE              (model behavior explanation explorer)
  - NOTIFICATION_SERVICE           (triggers "Your explanation is ready" alerts)
  - FEATURE_STORE_AGENT            (explanation quality behavioral features)
  - ML_DRIFT_MONITOR_AGENT         (receives feature importance shift data)
```

---

## 3️⃣ INPUT CONTRACT (STRICT)

Zero null tolerance on required fields. Reject all malformed inputs. Log all validation failures.

```json
INPUT_SCHEMA: {
  "required_fields": [
    "request_id",
    "request_type",
    "source_agent_id",
    "source_model_id",
    "source_model_version",
    "prediction_id",
    "prediction_timestamp_utc",
    "tenant_id",
    "domain_track",
    "prediction_output",
    "feature_contribution_vector",
    "payload_schema_version",
    "environment"
  ],
  "optional_fields": [
    "user_id",
    "user_role",
    "requester_id",
    "requester_role",
    "explanation_language",
    "previous_model_version",
    "previous_prediction_id",
    "regulatory_request_id",
    "fairness_audit_scope",
    "parent_user_id",
    "explanation_depth",
    "feature_distribution_baseline_id"
  ],
  "request_type_enum": [
    "PREDICTION_EXPLANATION_REQUEST",
    "DIFF_COMPUTATION_REQUEST",
    "FAIRNESS_AUDIT_REQUEST",
    "REGULATORY_EXPLANATION_REQUEST",
    "EXPLANATION_ARCHIVE_QUERY",
    "EXPLANATION_COMPLETENESS_CHECK",
    "FEATURE_IMPORTANCE_REPORT_REQUEST"
  ],
  "validation_rules": [
    "request_id MUST be UUID v4 — reject all other formats",
    "source_agent_id MUST resolve in AGENT_REGISTRY_SERVICE — reject unknown agents",
    "source_model_id MUST resolve in MODEL_REGISTRY_SERVICE — reject unknown models",
    "source_model_version MUST be semver format — reject malformed",
    "prediction_id MUST be UUID v4 — reject malformed",
    "prediction_timestamp_utc MUST be ISO-8601 UTC — reject malformed",
    "prediction_timestamp_utc MUST NOT be older than 30 days for real-time requests — reject stale",
    "tenant_id MUST resolve to an active tenant — reject unresolved",
    "domain_track MUST be one of [Arts, Commerce, Science, Technology, Administration] — reject all others",
    "prediction_output MUST conform to declared model output schema — reject non-conformant",
    "feature_contribution_vector MUST be present and non-empty — reject missing vectors",
    "feature_contribution_vector values MUST sum to approximate prediction output value (SHAP additivity check ±0.01 tolerance) — reject non-additive vectors",
    "feature_contribution_vector feature names MUST all appear in registered model feature list — reject unregistered features",
    "payload_schema_version MUST match current or N-1 version — reject older",
    "environment MUST be one of [dev, test, staging, production]",
    "explanation_language MUST be ISO 639-1 code if provided — reject non-ISO codes",
    "explanation_depth MUST be one of [SUMMARY, STANDARD, DETAILED, REGULATORY] if provided",
    "user_role MUST be one of [STUDENT, TRAINER, EVALUATOR, INSTITUTE, ENTERPRISE, RECRUITER, ADMIN, PARENT, AGENT] if provided"
  ],
  "security_checks": [
    "Validate mTLS source certificate against registered agent identity",
    "Validate JWT bearer token against ML_GOVERNANCE_ROLE or EXPLANATION_SERVICE_ROLE authorization",
    "Validate tenant_id matches the tenant_id embedded in prediction_id provenance chain",
    "Reject any input where user_id resolves to a different tenant than tenant_id (cross-tenant isolation)",
    "Reject payload exceeding 2MB (feature vectors can be large — this is a hard ceiling)",
    "Validate TLS 1.3 minimum",
    "Scan feature_contribution_vector for PII patterns — reject and escalate if found",
    "Validate that requester is authorized to access explanation for the given user_id and prediction_id",
    "Parent access to child explanation: validate parent-child relationship record before serving"
  ],
  "domain_checks": [
    "Verify source_model_id is registered to operate in the declared domain_track",
    "Verify user_id (if present) belongs to the declared domain_track",
    "Cross-domain explanation requests without explicit governance grant = REJECT + SECURITY_FLAG",
    "DIFF requests must supply both model versions — partial diff requests = REJECT"
  ],
  "null_policy": {
    "required_fields":  "ZERO NULL TOLERANCE — reject and log immediately",
    "optional_fields":  "NULL allowed but must be explicitly declared null — omission forbidden",
    "feature_contribution_vector": "MUST be non-null and non-empty on PREDICTION_EXPLANATION_REQUEST — zero-vector treated as missing = REJECT",
    "action_on_rejection": "LOG_VALIDATION_FAILURE → EMIT_REJECTION_EVENT → NO_PARTIAL_PROCESSING"
  }
}
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

All outputs must include confidence, version reference, and full traceability. No partial outputs.

```json
OUTPUT_SCHEMA: {
  "result_object": {

    "PREDICTION_EXPLANATION_RECORD": {
      "explanation_id":              "UUID (immutable)",
      "prediction_id":               "UUID (source prediction reference)",
      "source_model_id":             "string",
      "source_model_version":        "string (semver)",
      "tenant_id":                   "string",
      "domain_track":                "string",
      "environment":                 "string",
      "explanation_timestamp_utc":   "ISO-8601",

      "structured_explanation": {
        "decision_summary":          "string (1-2 sentence plain language)",
        "primary_factors":           [
          {
            "feature_name":          "string",
            "display_name":          "string (human-readable)",
            "contribution_direction":"POSITIVE | NEGATIVE | NEUTRAL",
            "contribution_magnitude":"HIGH | MEDIUM | LOW",
            "contribution_value":    "float (SHAP value)",
            "rank":                  "integer (1 = highest contributing feature)"
          }
        ],
        "top_N_features":            "integer (N declared per model, default 5)",
        "baseline_prediction_value": "float",
        "actual_prediction_value":   "float",
        "prediction_label":          "string | null (for classification outputs)"
      },

      "role_scoped_explanations": {
        "STUDENT": {
          "text":     "string (plain language, no jargon, max 150 words)",
          "language": "ISO 639-1 code"
        },
        "PARENT": {
          "text":     "string (empathetic, child-focused language, max 200 words)",
          "language": "ISO 639-1 code"
        },
        "RECRUITER": {
          "text":     "string (professional, data-referenced, max 150 words)",
          "language": "ISO 639-1 code"
        },
        "ADMIN": {
          "text":     "string (technical detail, feature names, values, max 300 words)",
          "language": "ISO 639-1 code"
        },
        "REGULATORY": {
          "text":     "string (formal, complete, all features, full provenance)",
          "language": "ISO 639-1 code",
          "full_feature_list": "array of all feature contributions",
          "model_training_reference": "string (model training run ID)",
          "data_distribution_reference": "UUID (training distribution snapshot)"
        }
      },

      "fairness_flags": {
        "domain_track_anomaly":      "boolean",
        "requires_fairness_review":  "boolean",
        "anomaly_description":       "string | null"
      },

      "explanation_completeness":    "COMPLETE | PARTIAL | INCOMPLETE",
      "missing_features":            ["array of features with no contribution value"]
    },

    "DIFF_REPORT": {
      "diff_report_id":              "UUID (immutable)",
      "model_id":                    "string",
      "version_from":                "string (semver — previous version)",
      "version_to":                  "string (semver — new version)",
      "diff_timestamp_utc":          "ISO-8601",
      "tenant_id":                   "string",
      "domain_track":                "string",

      "feature_importance_diff": [
        {
          "feature_name":            "string",
          "importance_v_from":       "float (mean absolute SHAP — previous version)",
          "importance_v_to":         "float (mean absolute SHAP — new version)",
          "importance_delta":        "float",
          "importance_delta_pct":    "float",
          "change_classification":   "MATERIAL | MINOR | NEGLIGIBLE | NEW_FEATURE | REMOVED_FEATURE"
        }
      ],

      "decision_boundary_changes": {
        "threshold_changed":         "boolean",
        "old_threshold":             "float | null",
        "new_threshold":             "float | null"
      },

      "explanation_text_diff": {
        "sample_size":               "integer (N randomly sampled predictions compared)",
        "avg_semantic_similarity":   "float (0.0–1.0, cosine similarity of explanation texts)",
        "material_change_detected":  "boolean",
        "examples_of_changed_explanations": [
          {
            "prediction_id":         "UUID",
            "explanation_v_from":    "string",
            "explanation_v_to":      "string",
            "change_summary":        "string"
          }
        ]
      },

      "fairness_diff": {
        "domain_track_distribution_shift": "boolean",
        "demographic_impact_detected":     "boolean",
        "details":                         "string | null"
      },

      "overall_diff_severity":       "NEGLIGIBLE | MINOR | MATERIAL | BREAKING",
      "requires_governance_review":  "boolean",
      "requires_regulatory_notification": "boolean",
      "diff_summary":                "string (plain language, max 200 words)"
    }
  },

  "confidence_score":   "0.0–1.0 (float, two decimal places)",
  "model_version":      "MEDA-EXPLAINABILITY-ENGINE-v{major}.{minor}.{patch}",
  "audit_reference":    "UUID (immutable, append-only)",
  "next_trigger_event": [
    "EXPLANATION_RECORD_CREATED",
    "EXPLANATION_SERVED_TO_USER (conditional)",
    "DIFF_REPORT_GENERATED (conditional)",
    "MATERIAL_DIFF_DETECTED (conditional)",
    "FAIRNESS_ANOMALY_DETECTED (conditional)",
    "EXPLANATION_COMPLETENESS_VIOLATION (conditional)",
    "REGULATORY_PACKAGE_READY (conditional)",
    "GOVERNANCE_REVIEW_REQUIRED (conditional)"
  ]
}
```

**Confidence Score Interpretation:**
```
1.00       = Feature contribution vector complete, all features resolved,
             SHAP additivity check passed, full explanation generated
0.85–0.99  = Minor feature gaps (≤ 2 features with null contributions),
             explanation valid but flagged for ML team review
0.70–0.84  = Significant feature gaps, explanation marked PARTIAL,
             downstream systems must display uncertainty indicator
< 0.70     = INSUFFICIENT EXPLANATION DATA — do not serve to user-facing surfaces
             Emit EXPLANATION_INCOMPLETE_EVENT, escalate to ML_OPS_TEAM
             Regulatory explanations require confidence ≥ 0.95 — below this = HALT
```

---

## 5️⃣ ML / AI LOGIC LAYER

### Explainability Engine (Primary — 90%)

```
ENGINE_TYPE         = SHAP-based Feature Attribution Engine
                      (SHapley Additive exPlanations — model-agnostic)

SHAP_IMPLEMENTATION:
  Tree-based models (Gradient Boosting, Random Forest, XGBoost):
    → TreeExplainer (exact SHAP — O(T·L·D) complexity)
  Linear models (Logistic Regression, Ridge):
    → LinearExplainer (exact SHAP)
  Neural network or complex models:
    → DeepExplainer or KernelExplainer (approximate SHAP)
    → Flag approximate explanations in output: approximate_shap = true
  Unknown model type:
    → KernelExplainer as fallback
    → Flag in output: explainer_fallback = true

SHAP_ADDITIVITY_VALIDATION:
  Every feature contribution vector MUST satisfy:
  sum(SHAP values) ≈ (prediction_output - baseline_value) ± 0.01
  Failure = REJECT INPUT (non-additive vector cannot produce valid explanation)

BASELINE_VALUE:
  The expected value (mean prediction across training data distribution).
  Stored in MODEL_REGISTRY_SERVICE per model version.
  Used as the reference point for all SHAP explanations.
  MUST NOT be hardcoded — always retrieved from MODEL_REGISTRY_SERVICE.

FEATURE_IMPORTANCE_AGGREGATION:
  For DIFF computation: mean |SHAP| across N=1000 randomly sampled predictions
  (stored in MODEL_REGISTRY_SERVICE — computed at model deployment time)
```

### Natural Language Generation Layer (Secondary — 10%)

This is the ONLY location where controlled LLM assistance is permitted. It is used exclusively for converting structured SHAP explanations into natural language text. It has zero decision-making authority.

```
AI_USAGE_SCOPE        = STRICTLY LIMITED to:
                        Converting structured explanation objects into
                        natural language text for role-scoped explanations
                        (STUDENT, PARENT, RECRUITER, ADMIN, REGULATORY).
                        AI has ZERO authority over:
                        - SHAP computation
                        - Confidence scores
                        - Diff severity classification
                        - Fairness anomaly detection
                        - Any decision about the underlying ML prediction

PROMPT_GOVERNANCE:
  All NLG prompts are:
  - Versioned (NLG_PROMPT_v{major}.{minor}.{patch})
  - Deterministic structured templates (not open-ended)
  - Role-specific (separate prompt template per audience role)
  - Input-bounded (only structured explanation fields are injected)
  - Output-bounded (max token limits enforced per role)
  - Forbidden from introducing information not present in structured explanation
  - Forbidden from softening, hardening, or editorializing explanations
  - Required to use only declared display_name values for features
    (no raw feature names in user-facing text — PRIVACY ENFORCEMENT)

NLG_PROMPT_VERSION_REGISTRY:
  Location:    SCHEMA_REGISTRY_SERVICE / nlg_prompts /
  Format:      Versioned JSON template
  Change rule: ADD-ONLY, governance-approved, backward-compatible
  Old prompts: Retained minimum 2 years (regulatory requirement)

FALLBACK (NLG unavailable):
  → Use pre-built template strings from EXPLANATION_TEMPLATE_REGISTRY
  → Mark explanation: nlg_generated = false, template_generated = true
  → Do NOT block explanation delivery due to NLG unavailability
  → Log NLG unavailability incident
```

### Diff Engine (Deterministic — 100% rules-based)

```
DIFF_ENGINE_TYPE      = Deterministic Feature Importance Comparator

DIFF_COMPUTATION_TRIGGER:
  Triggered by DEPLOYMENT_AGENT on every model version deployment.
  Also triggerable on-demand by GOVERNANCE_AGENT or COMPLIANCE_AGENT.

DIFF_ALGORITHM:
  Step 1: Retrieve feature importance baseline (mean |SHAP|) for version_from
          from MODEL_REGISTRY_SERVICE.
  Step 2: Retrieve feature importance baseline for version_to from
          MODEL_REGISTRY_SERVICE.
  Step 3: For each feature in union(feature_list_from, feature_list_to):
          Compute importance_delta = importance_v_to - importance_v_from
          Compute importance_delta_pct = delta / max(importance_v_from, 0.001)
          Classify change:
            |delta_pct| < 5%    → NEGLIGIBLE
            5% ≤ |delta_pct| < 20% → MINOR
            20% ≤ |delta_pct| < 50% → MATERIAL
            |delta_pct| ≥ 50%   → BREAKING
            Feature in v_to but not v_from → NEW_FEATURE
            Feature in v_from but not v_to → REMOVED_FEATURE
  Step 4: Overall severity = max severity across all features
  Step 5: Requires governance review if any MATERIAL or BREAKING change
  Step 6: Requires regulatory notification if any BREAKING change in
          a model that affects user-facing decisions (career, job, score)

SEMANTIC_SIMILARITY_CHECK:
  Sample N=100 predictions from last 24h (or from prediction archive).
  Generate explanation for each using version_from and version_to.
  Compute cosine similarity of explanation texts.
  avg_semantic_similarity < 0.80 → material_change_detected = true
```

### Fairness Analysis Engine (Deterministic — 100% rules-based)

```
FAIRNESS_SCOPE:
  MEDA monitors for systematic explanation pattern differences across:
  - domain_track groups (Arts vs Commerce vs Science vs Technology vs Administration)
  - user_role groups (STUDENT vs RECRUITER perspective explanations)
  - tenant_id groups (for multi-tenant fairness — no tenant should receive
    systematically worse explanations than another)

FAIRNESS_METRICS:
  Metric 1: Feature rank consistency across groups
    → If feature_rank for a given feature differs by > 2 positions between groups = FLAG
  Metric 2: Prediction score distribution shift across groups
    → KL divergence of prediction score distributions across domain_tracks
    → Threshold: KL > 0.1 = FLAG
  Metric 3: Explanation confidence score distribution
    → Mean confidence score should not differ by > 0.10 across groups
    → Larger gap = FLAG
  Metric 4: "REMOVED_FEATURE" or "NEW_FEATURE" disproportionately
    affecting one domain_track group = FLAG

FAIRNESS_COMPUTATION_FREQUENCY:
  Real-time: Flag individual predictions with domain_track_anomaly
  Batch:     Weekly fairness audit report across all models
  Trigger:   Always after a MATERIAL or BREAKING diff
```

---

## 6️⃣ SCALABILITY DESIGN

```
EXPECTED_RPS           = 5,000–50,000 explanation requests/sec at peak
                         (every ML prediction across the platform triggers
                          an explanation generation request)
LATENCY_TARGET:
  Real-time explanation (user-facing "Why?" button):
    P99 < 800ms (includes SHAP retrieval + NLG generation)
  Async explanation (stored with prediction, retrieved on demand):
    P99 < 200ms (SHAP retrieval + structured explanation generation)
    NLG generation is async — stored explanation served immediately
  Diff computation (triggered on deployment):
    P99 < 60 seconds (batch operation, non-real-time)
  Regulatory package (on-demand):
    P99 < 10 seconds

MAX_CONCURRENCY        = 2,000 parallel explanation processors
QUEUE_STRATEGY         = Kafka-backed ingestion (topic: meda.explanation.requests)
                         Priority queue for user-facing requests: meda.explanation.priority
                         Diff computation queue: meda.diff.requests
                         Regulatory queue: meda.regulatory.requests
                         Dead Letter Queue: meda.explanation.dlq

SCALING_MODEL          = Horizontal auto-scaling (Kubernetes HPA)
                         Stateless execution per MEDA instance
                         SHAP computation is CPU-intensive — HPA scales on CPU utilization

EXECUTION_STATE        = Stateless (all state in Redis + PostgreSQL + explanation archive)
IDEMPOTENCY            = All explanation requests idempotent
                         (Idempotency key: prediction_id + source_model_version + tenant_id)
                         Re-requesting explanation for same prediction_id returns
                         archived record — does NOT recompute

ASYNC_ARCHITECTURE:
  ALL predictions automatically receive async explanation generation.
  Explanation is stored alongside the prediction record.
  User-facing "Why?" request retrieves the stored explanation.
  If no stored explanation exists (race condition): real-time fallback generation.
  NLG generation is always async (stored, not blocking the prediction path).

SHAP_CACHING:
  Background value (baseline) per model version: cached in Redis (TTL: model version lifetime)
  Feature display name mappings: cached in Redis (TTL: 1 hour)
  Model feature list: cached in Redis (TTL: model version lifetime)
```

---

## 7️⃣ EXPLANATION SCOPE — ALL ML MODELS COVERED

This section declares every ML model across the platform for which MEDA generates explanations. No ML model may operate on the platform without a registered explanation contract in MEDA.

### 7A — Job & Career Intelligence Models

```
MODEL: JOB_MATCH_SCORING_MODEL
  Output explained:   Match score (0–100) per job per user
  Key features:       skill_overlap_score, experience_relevance_score,
                      domain_track_alignment, location_match,
                      salary_expectation_gap, certifications_match,
                      application_success_rate_history
  STUDENT explanation:
    "Your match score is {score}/100. Your strongest alignment is
    your {top_feature_display_name}. To improve this score, focus
    on {bottom_feature_display_name}."
  RECRUITER explanation:
    "Candidate scored {score}/100. Primary factors: {top_3_features}.
    {qualification_gaps_if_any}."
  Confidence floor:   0.75 (career decision — elevated floor)
  Diff severity alert: MATERIAL or above → governance review mandatory

MODEL: PLACEMENT_PROBABILITY_MODEL
  Output explained:   Probability (0–1) of successful placement in 90 days
  Key features:       skill_verified_count, dojo_performance_score,
                      portfolio_project_count, domain_demand_index,
                      application_activity_score, interview_pass_rate,
                      recency_of_skill_evidence
  Confidence floor:   0.75
  Parent visibility:  Yes — simplified explanation only

MODEL: OFFER_ACCEPTANCE_PREDICTION_MODEL
  Output explained:   Probability (0–1) candidate will accept offer
  Key features:       salary_match_score, company_fit_index,
                      location_preference_match, role_level_alignment,
                      response_time_history, engagement_depth_score
  RECRUITER explanation only (not shown to candidates)
  Confidence floor:   0.70

MODEL: RESUME_PARSING_MODEL
  Output explained:   Parsed skill tags, experience segments, education level
  Key features:       text_section_confidence, skill_keyword_density,
                      format_quality_score, completeness_score
  STUDENT explanation:
    "We extracted {N} skills from your resume. Sections identified:
    {section_list}. Tips to improve parsing: {improvement_tips}."
  Confidence floor:   0.65
```

### 7B — Skill & Learning Intelligence Models

```
MODEL: SKILL_GAP_ANALYSIS_MODEL
  Output explained:   List of skill gaps ranked by career impact
  Key features:       target_role_skill_requirements,
                      user_verified_skills, industry_demand_index,
                      peer_benchmark_score, learning_velocity_score
  STUDENT explanation:
    "To reach your target role, your most important gap is
    {top_gap_skill}. Based on your learning history, this
    could take approximately {estimated_weeks} weeks to address."
  Confidence floor:   0.70

MODEL: LEARNING_PATH_RECOMMENDATION_MODEL
  Output explained:   Ordered list of recommended learning modules
  Key features:       current_skill_level, target_role_distance,
                      learning_style_indicators, peer_completion_rates,
                      time_commitment_compatibility
  STUDENT explanation: Max 100 words, action-oriented
  Confidence floor:   0.65

MODEL: INDUSTRY_DEMAND_MAPPING_MODEL
  Output explained:   Demand score (0–100) for skill in a domain/region
  Key features:       job_posting_frequency, hiring_trend_30d,
                      salary_premium_indicator, domain_track_weight
  ADMIN/RECRUITER explanation only
  Confidence floor:   0.60
```

### 7C — Intelligence Detection Engine (EIE/HIA — Howard Gardner Model)

```
MODEL: INTELLIGENCE_DETECTION_ENGINE_MODEL (EIE/HIA)
  Output explained:   Intelligence profile scores across 8 dimensions:
                      Linguistic, Logical, Spatial, Bodily, Musical,
                      Interpersonal, Intrapersonal, Naturalistic

  CRITICAL EXPLANATION RULES FOR EIE/HIA:
    ① NEVER frame any intelligence type as "bad" or "low" in user-facing text.
       The platform philosophy is "every person is intelligent" — explanations
       must reflect this by describing strengths and growth areas only.
    ② NEVER map scores directly to career predictions without
       showing intermediate reasoning (skill → career path, not score → career).
    ③ PARENT explanation must be empathetic, positive, growth-focused.
       FORBIDDEN: "Your child scored low in Logical intelligence."
       REQUIRED:  "Your child shows strong Interpersonal and Spatial strengths.
                   Logical reasoning is an area where targeted Dojo exercises
                   can help them grow."
    ④ Confidence floor: 0.80 (highest on platform — affects human identity)
    ⑤ Any score below confidence floor must NOT be displayed to user.
       Explanation must instead say: "More activity data needed to generate
       your full Intelligence Profile. Complete {N} more Dojo exercises."

  Key features:
    Per intelligence type:
      behavioral_test_score_{type},
      dojo_exercise_performance_{type},
      consistency_score_{type},
      sample_size_{type} (number of data points)

  DIFF RULE: Any MATERIAL change in EIE/HIA diff
    → Mandatory governance review before new model version is served to users
    → Regulatory explanation package generated automatically
    → Parent notification: "Your child's Intelligence Profile may be updated
      when you next view it due to a model improvement."
```

### 7D — Dojo Competitive Engine Models

```
MODEL: DOJO_SCORING_ENGINE_MODEL
  Output explained:   Match score (0–100) for a completed Dojo session
  Key features:       argument_quality_score, response_time_distribution,
                      vocabulary_diversity_score, logical_coherence_score,
                      engagement_depth_score, peer_comparison_percentile
  Confidence floor:   0.80 (competitive integrity — elevated)
  STUDENT explanation:
    "Your score of {score} reflects: {top_3_factors}.
    Compared to your last session, {improvement_or_decline_text}."

MODEL: DOJO_RATING_ENGINE_MODEL
  Output explained:   ELO-style competitive rating change after session
  Key features:       opponent_rating, score_differential,
                      expected_outcome_probability,
                      performance_consistency_score
  STUDENT explanation:
    "Your rating changed from {old_rating} to {new_rating}.
    This reflects your performance relative to your opponent's
    expected skill level."
  Confidence floor:   0.75

MODEL: DOJO_BELT_ENGINE_MODEL
  Output explained:   Belt tier assignment and progression eligibility
  Key features:       cumulative_session_score, consistency_streak,
                      domain_mastery_score, peer_rank_percentile,
                      evaluator_score_weight
  STUDENT explanation: Plain language belt progression story
  ADMIN explanation: Full feature breakdown
  Confidence floor:   0.80
```

### 7E — Reputation & Trust Models

```
MODEL: REPUTATION_SCORING_MODEL
  Output explained:   Trust/reputation score (0–100) for user or entity
  Key features:       verified_achievement_count, peer_endorsement_score,
                      platform_activity_consistency, complaint_history_weight,
                      recruiter_feedback_score
  STUDENT/USER explanation:
    "Your reputation score of {score} is based on {top_3_factors}.
    Actions that can improve your score: {improvement_actions}."
  RECRUITER explanation: More detail on verification factors
  Confidence floor:   0.70

MODEL: SME_RELIABILITY_SCORING_MODEL
  Output explained:   Reliability score for SME job posters (0–100)
  Key features:       offer_completion_rate, payment_history,
                      candidate_feedback_score, response_time_average,
                      verified_business_status
  RECRUITER/ENTERPRISE explanation only
  Confidence floor:   0.70
```

---

## 8️⃣ EXPLANATION DIFF ENGINE — DETAILED SPECIFICATION

The diff engine is MEDA's most architecturally distinctive capability. It answers: **"When we upgraded model version N to N+1, how did explanations change — and did that change affect users materially?"**

### 8A — Diff Trigger Protocol

```
DIFF_TRIGGERS:

  Automatic (mandatory):
    - Every model version deployment (DEPLOYMENT_AGENT emits MODEL_DEPLOYED event)
    - MEDA receives event → Queues diff computation for model_id, version_from, version_to
    - Diff must complete within 60 seconds of deployment notification
    - Deployment may NOT be marked fully complete until diff report is generated
      (MEDA emits DIFF_READY_EVENT which DEPLOYMENT_AGENT waits for)

  Manual (on-demand):
    - GOVERNANCE_AGENT may request diff for any two registered model versions
    - COMPLIANCE_AGENT may request diff for regulatory audit purposes
    - No user role may directly trigger diff computation

  Post-Incident:
    - If explanation completeness violation is detected at scale (> 1% of
      predictions missing explanations), MEDA triggers self-audit diff
      between current and previous version
```

### 8B — Diff Severity Classification and Response Matrix

```
SEVERITY: NEGLIGIBLE
  Definition: All feature importance deltas < 5%, no new/removed features,
              semantic similarity > 0.95
  Response:   Log only. No governance review required.
              Archive diff report. Proceed with deployment.

SEVERITY: MINOR
  Definition: Some feature deltas 5–20%, or 1–2 features with LOW
              contribution added/removed, semantic similarity 0.85–0.95
  Response:   Log + notify ML_OPS_TEAM (non-urgent).
              Governance review optional (ML team discretion).
              Archive diff report.

SEVERITY: MATERIAL
  Definition: Any feature delta ≥ 20%, OR ≥ 3 features added/removed,
              OR semantic similarity < 0.85, OR fairness diff detected
  Response:   HALT deployment to production (staging OK).
              EMIT MATERIAL_DIFF_DETECTED_EVENT (P1).
              MANDATORY governance review by GOVERNANCE_AGENT.
              ML team must provide written justification for change.
              Governance approval required before production deployment.
              Regulatory notification prepared (not yet sent — pending review).

SEVERITY: BREAKING
  Definition: Any feature delta ≥ 50%, OR core decision feature removed,
              OR complete set of primary_factors changed,
              OR semantic similarity < 0.70,
              OR demographic fairness anomaly detected
  Response:   IMMEDIATE HALT — block production deployment.
              EMIT BREAKING_DIFF_DETECTED_EVENT (P0).
              ESCALATE_TO = GOVERNANCE_AGENT + COMPLIANCE_AGENT + ML_LEAD + PLATFORM_ADMIN.
              Mandatory full fairness audit before any deployment.
              Regulatory notification MUST be sent if model affects career decisions.
              Rollback to previous version remains available.
              Resolution requires governance sign-off at COMPLIANCE_ADMIN level.
```

### 8C — Explanation Archive for Diff Baseline

```
EXPLANATION_ARCHIVE:
  Location:      Immutable append-only object storage (WORM-compliant)
  Contents:      Every structured explanation record ever generated
                 (structured fields only — NOT NLG text in primary archive)
  Indexed by:    prediction_id, model_id, model_version, tenant_id,
                 domain_track, explanation_timestamp_utc
  Retention:     Minimum 7 years (regulatory compliance)
  Access:        Read-only via MEDA. No direct external access.
                 Regulatory queries served via MEDA regulatory endpoint only.

DIFF_BASELINE_STORAGE:
  Location:      MODEL_REGISTRY_SERVICE (per model version)
  Contents:      Feature importance baseline (mean |SHAP| per feature, N=1000)
                 Prediction score distribution summary (mean, std, percentiles)
                 Explanation confidence score distribution
                 Computed at model training time, stored before deployment
  MANDATORY:     Model version CANNOT be deployed without stored baseline.
                 DEPLOYMENT_AGENT checks baseline existence before proceeding.
```

---

## 9️⃣ ROLE-SCOPED EXPLANATION DELIVERY RULES

### Strict Audience Authorization Matrix

```
EXPLANATION ACCESS CONTROL:

Model: JOB_MATCH_SCORING
  STUDENT (own predictions only):     SUMMARY level ✅
  PARENT (student child only):        SUMMARY level ✅
  RECRUITER (candidate they reviewed):STANDARD level ✅
  INSTITUTE_ADMIN:                    STANDARD level ✅
  PLATFORM_ADMIN:                     DETAILED level ✅
  COMPLIANCE_AUDITOR:                 REGULATORY level ✅
  OTHER STUDENTS:                     FORBIDDEN ❌

Model: INTELLIGENCE_DETECTION_ENGINE (EIE/HIA)
  STUDENT (own profile only):         STANDARD level ✅
  PARENT (verified child only):       STANDARD level ✅ (empathetic template)
  RECRUITER:                          SUMMARY level ✅ (no raw scores — aggregate only)
  INSTITUTE_ADMIN:                    STANDARD level ✅ (cohort aggregate only — no individual)
  PLATFORM_ADMIN:                     DETAILED level ✅
  COMPLIANCE_AUDITOR:                 REGULATORY level ✅
  ANONYMOUS / UNVERIFIED:             FORBIDDEN ❌

Model: DOJO_SCORING_ENGINE
  STUDENT (own sessions only):        STANDARD level ✅
  OPPONENT (session they participated in): SUMMARY level ✅
  EVALUATOR (sessions they evaluated): STANDARD level ✅
  RECRUITER:                          SUMMARY level ✅
  PLATFORM_ADMIN:                     DETAILED level ✅
  COMPLIANCE_AUDITOR:                 REGULATORY level ✅
  UNRELATED USERS:                    FORBIDDEN ❌

Model: REPUTATION_SCORING
  STUDENT (own score only):           STANDARD level ✅
  RECRUITER (candidates they viewed): SUMMARY level ✅
  PLATFORM_ADMIN:                     DETAILED level ✅
  OTHER USERS (another user's score): FORBIDDEN ❌

Model: OFFER_ACCEPTANCE_PREDICTION
  RECRUITER (their own job posting):  STANDARD level ✅
  STUDENT (about themselves):         FORBIDDEN ❌
                                      (This model is recruiter-facing only)
  PLATFORM_ADMIN:                     DETAILED level ✅

RULE: Any access request that doesn't match this matrix = REJECT + LOG + NOTIFY COMPLIANCE
```

### Explanation Depth Definitions

```
SUMMARY:
  - Decision outcome (score, label, recommendation)
  - Top 3 contributing factors (display names, direction, magnitude)
  - 1 actionable improvement hint
  - Max 100 words of NLG text

STANDARD:
  - All SUMMARY content
  - Top 5 contributing factors with relative magnitude visualization data
  - Comparison to peer benchmark (anonymized)
  - 2–3 actionable improvement hints
  - Max 200 words of NLG text

DETAILED:
  - All STANDARD content
  - Full feature list with raw SHAP values
  - Baseline value and prediction value
  - Model version reference
  - Confidence score displayed
  - Max 400 words of NLG text

REGULATORY:
  - All DETAILED content
  - Complete feature contribution vector (all features, all values)
  - Model training run reference
  - Training data distribution reference
  - SHAP additivity verification confirmation
  - Explanation generation timestamp
  - All processing agent IDs in provenance chain
  - Explanation prompt version (NLG)
  - No word limit
```

---

## 🔟 EXPLANATION COMPLETENESS ENFORCEMENT

```
COMPLETENESS_CONTRACT:

Rule: No ML prediction result may be served to any user-facing surface
      without a corresponding valid explanation record in the explanation archive.

ENFORCEMENT_MECHANISM:
  MEDA runs explanation completeness checks:
    - Continuous (real-time): Every prediction event consumed from Kafka
      is checked against the explanation archive after TTL_COMPLETION_WINDOW
    - TTL_COMPLETION_WINDOW = 30 seconds (explanation must exist within 30s of prediction)
    - Batch (hourly): Scan last hour's predictions — any without explanation = violation

VIOLATION_THRESHOLDS:
  1 missing explanation:   LOG_WARNING (may be processing lag)
  > 10 missing in 1 minute: EMIT EXPLANATION_COMPLETENESS_WARNING (P2)
  > 1% of predictions missing in any 5-minute window: EMIT P1 + ESCALATE to ML_OPS_TEAM
  > 5% missing: EMIT P0 + ESCALATE to GOVERNANCE_AGENT + halt affected model's
                user-facing serving until resolved

USER_FACING_FALLBACK (if explanation not yet ready):
  Display: "We're preparing your explanation. It will be available in a few moments."
  Retry: Check every 5 seconds for up to 60 seconds.
  After 60 seconds with no explanation: Show minimal template explanation
  and emit EXPLANATION_DELAYED_EVENT for ops team.
  NEVER show "No explanation available" to users without a fallback message.
```

---

## 1️⃣1️⃣ SECURITY ENFORCEMENT

```
SECURITY_MODEL = Zero-Trust Multi-Tenant

ENFORCEMENT_RULES:
  ✅ mTLS on ALL agent communications
  ✅ TLS 1.3 minimum
  ✅ JWT validation with role-based scope on every explanation request
  ✅ Tenant isolation: explanation records are tenant-partitioned
     No explanation data from Tenant A is accessible from Tenant B
  ✅ User isolation: a user's explanation is only accessible to:
     - The user themselves
     - Authorized reviewers (per access control matrix above)
     - Compliance/governance agents with audit authority
  ✅ PII scan on all feature names in explanations:
     Feature display_names must be approved in FEATURE_DISPLAY_NAME_REGISTRY
     No raw database column names in user-facing explanations
  ✅ Explanation archive: WORM-compliant storage, no delete, no update
  ✅ Access logging: every explanation record read is logged with actor_id
  ✅ Audit trail: every explanation generation is logged with full provenance
  ✅ NLG prompt injection prevention:
     All NLG inputs are structured JSON — no free-text user input
     is ever injected into NLG prompts

FORBIDDEN:
  ❌ No cross-tenant explanation access under any circumstances
  ❌ No raw feature names (database column names) in user-facing text
  ❌ No PII in explanation records (user_id is stored but not in explanation text)
  ❌ No retroactive modification of archived explanation records
  ❌ No explanation serving without RBAC authorization check
  ❌ No fabricated explanations (AI may only convert structured SHAP data to text)
  ❌ No explanation completeness suppression (every missing explanation must be logged)
  ❌ No bypassing SHAP additivity check
```

---

## 1️⃣2️⃣ AUDIT & TRACEABILITY

Every explanation generation must produce an immutable audit entry.

```json
AUDIT_LOG_SCHEMA: {
  "audit_id":                        "UUID (immutable, system-generated)",
  "timestamp_utc":                   "ISO-8601 UTC",
  "meda_instance_id":                "MEDA pod instance ID",
  "request_id":                      "UUID",
  "request_type":                    "string",
  "prediction_id":                   "UUID",
  "source_model_id":                 "string",
  "source_model_version":            "string (semver)",
  "tenant_id":                       "string",
  "domain_track":                    "string",
  "environment":                     "string",
  "user_id_hash":                    "SHA-256 of user_id (not raw user_id)",
  "requester_role":                  "string",
  "input_hash":                      "SHA-256 of raw input payload",
  "output_hash":                     "SHA-256 of output explanation record",
  "explainability_engine_version":   "MEDA-EXPLAINABILITY-ENGINE-v{x}.{y}.{z}",
  "nlg_prompt_version":              "NLG_PROMPT_v{x}.{y}.{z} | TEMPLATE_FALLBACK | NOT_INVOKED",
  "shap_method":                     "TreeExplainer | LinearExplainer | DeepExplainer | KernelExplainer",
  "shap_approximate":                "boolean",
  "shap_additivity_check_passed":    "boolean",
  "explanation_depth":               "SUMMARY | STANDARD | DETAILED | REGULATORY",
  "confidence_score":                "float 0.00–1.00",
  "explanation_completeness":        "COMPLETE | PARTIAL | INCOMPLETE",
  "fairness_flag":                   "boolean",
  "diff_triggered":                  "boolean",
  "diff_severity":                   "NEGLIGIBLE | MINOR | MATERIAL | BREAKING | NOT_APPLICABLE",
  "processing_time_ms":              "integer",
  "served_to_user":                  "boolean",
  "access_role":                     "string | null"
}
```

**Audit Storage:** Append-only Kafka topic `audit.log.stream` → WORM object storage. Retention: minimum 7 years.

---

## 1️⃣3️⃣ FAILURE POLICY

No silent failures. No fabricated explanations. No partial outputs served to users.

```
FAILURE SCENARIOS AND RESPONSES:

[F-01] FEATURE CONTRIBUTION VECTOR MISSING:
  → REJECT input
  → LOG_VALIDATION_FAILURE
  → NOTIFY source_agent_id: "Feature contribution vector required"
  → Mark prediction as EXPLANATION_INCOMPLETE
  → EMIT EXPLANATION_INCOMPLETE_EVENT
  → RETRY_POLICY = Source agent must re-emit with vector

[F-02] SHAP ADDITIVITY CHECK FAILS (vector does not sum to prediction delta):
  → REJECT input
  → LOG_SHAP_INTEGRITY_FAILURE
  → NOTIFY source_agent_id + ML_OPS_TEAM
  → DO NOT generate explanation from non-additive vector
  → RETRY_POLICY = Source ML agent must recompute SHAP values

[F-03] MODEL NOT FOUND IN MODEL_REGISTRY_SERVICE:
  → REJECT input
  → LOG_UNKNOWN_MODEL_INCIDENT (security flag — unregistered model)
  → ESCALATE_TO = GOVERNANCE_AGENT + SECURITY_AGENT
  → RETRY_POLICY = NONE (manual investigation required)

[F-04] NLG SERVICE UNAVAILABLE:
  → FALLBACK to EXPLANATION_TEMPLATE_REGISTRY (pre-built templates)
  → Generate structured explanation (no NLG text)
  → Mark explanation: nlg_generated = false
  → LOG_NLG_UNAVAILABLE
  → NOTIFY ML_OPS_TEAM (non-urgent)
  → DO NOT block explanation delivery
  → RETRY_POLICY = Retry NLG every 60s (background), update explanation
    in archive when NLG becomes available

[F-05] EXPLANATION ARCHIVE WRITE FAILURE:
  → STOP_OUTPUT_EMISSION (explanation is not valid without archive storage)
  → LOG_ARCHIVE_WRITE_FAILURE (P0 — audit trail broken)
  → ESCALATE_TO = INFRA_OPS_TEAM + COMPLIANCE_AGENT
  → Buffer explanation in-memory (max 5 min TTL) and retry write
  → RETRY_POLICY = Exponential backoff (5s, 15s, 30s), max 5 retries
  → After 5 failures: HALT explanation serving + P0 escalation

[F-06] CONFIDENCE BELOW FLOOR (< model-specific floor):
  → GENERATE structured explanation (SHAP data)
  → DO NOT serve NLG text to user-facing surfaces
  → Store explanation with completeness = PARTIAL
  → Display to user: "More activity data needed to generate your full explanation."
  → LOG_LOW_CONFIDENCE_EXPLANATION
  → REGULATORY requests: confidence < 0.95 = HALT + ESCALATE to COMPLIANCE_AGENT

[F-07] DIFF COMPUTATION: BASELINE NOT FOUND:
  → HALT diff computation
  → LOG_DIFF_BASELINE_MISSING (P1)
  → ESCALATE_TO = ML_OPS_TEAM + DEPLOYMENT_AGENT
  → BLOCK deployment completion signal until baseline is provided
  → RETRY_POLICY = NONE (ML team must generate and register baseline)

[F-08] BREAKING DIFF DETECTED:
  → HALT production deployment
  → EMIT BREAKING_DIFF_DETECTED_EVENT (P0)
  → LOG_BREAKING_DIFF_INCIDENT (full evidence package)
  → ESCALATE_TO = GOVERNANCE_AGENT + COMPLIANCE_AGENT + ML_LEAD
  → RETRY_POLICY = NONE — governance resolution required

[F-09] UNAUTHORIZED EXPLANATION ACCESS ATTEMPT:
  → REJECT request
  → LOG_UNAUTHORIZED_ACCESS_INCIDENT (security)
  → ESCALATE_TO = SECURITY_AGENT
  → NOTIFY requesting user: "You are not authorized to view this explanation."
  → RETRY_POLICY = NONE

[F-10] EIE/HIA MODEL — CONFIDENCE < 0.80:
  → DO NOT generate explanation text (intelligence profile is identity-sensitive)
  → DO NOT show partial intelligence scores to user
  → Display: "Complete {N} more Dojo exercises to unlock your full
    Intelligence Profile."
  → LOG_EIE_INSUFFICIENT_DATA
  → RETRY_POLICY = Re-evaluate after user completes required exercises

[F-11] KAFKA BROKER UNAVAILABLE:
  → Buffer explanation requests in-memory (max 10,000, 5 min TTL)
  → LOG_KAFKA_UNAVAILABLE
  → ESCALATE_TO = INFRA_OPS_TEAM
  → RETRY_POLICY = Reconnect every 10s, flush buffer on reconnect

[F-12] REGULATORY EXPLANATION REQUEST — ANY FAILURE:
  → HALT regulatory package generation
  → LOG_REGULATORY_FAILURE (P0)
  → ESCALATE_TO = COMPLIANCE_AGENT + COMPLIANCE_ADMIN
  → DO NOT deliver incomplete regulatory package
  → RETRY_POLICY = Manual resolution required before retry
```

---

## 1️⃣4️⃣ INTER-AGENT DEPENDENCY MAP

```
UPSTREAM_AGENTS:
  MANDATORY:
    - All ML Agents (feature contribution vector emitters)
    - MODEL_REGISTRY_SERVICE
    - SCHEMA_REGISTRY_SERVICE
    - AUDIT_LOG_AGENT
    - TENANT_MANAGEMENT_SERVICE
    - AUTHORIZATION_SERVICE
    - DEPLOYMENT_AGENT

  OPTIONAL:
    - FEATURE_STORE_AGENT
    - USER_PROFILE_SERVICE
    - FEATURE_FLAG_SERVICE
    - NLG_SERVICE (fallback to templates if unavailable)

DOWNSTREAM_AGENTS:
  PRIMARY:
    - USER_FACING_API_GATEWAY      (serves explanations to Flutter/React UI)
    - COMPLIANCE_AGENT             (regulatory packages)
    - GOVERNANCE_AGENT             (diff alerts + fairness reports)
    - AUDIT_LOG_AGENT              (immutable explanation archive)
    - OBSERVABILITY_AGENT          (explanation metrics)
    - INCIDENT_MANAGEMENT_AGENT    (completeness + breaking diff P0 events)

  SECONDARY:
    - PARENT_DASHBOARD_SERVICE     (child intelligence report explanations)
    - FEATURE_STORE_AGENT          (explanation quality behavioral features)
    - ML_DRIFT_MONITOR_AGENT       (feature importance shift data)
    - NOTIFICATION_SERVICE         (explanation ready + diff governance alerts)

EVENTS_EMITTED:
  - EXPLANATION_RECORD_CREATED
  - EXPLANATION_SERVED_TO_USER
  - EXPLANATION_INCOMPLETE_DETECTED
  - EXPLANATION_COMPLETENESS_WARNING     (P2)
  - EXPLANATION_COMPLETENESS_VIOLATION   (P1/P0 by threshold)
  - DIFF_REPORT_GENERATED
  - DIFF_SEVERITY_NEGLIGIBLE
  - DIFF_SEVERITY_MINOR
  - MATERIAL_DIFF_DETECTED               (P1 — blocks production deployment)
  - BREAKING_DIFF_DETECTED               (P0 — halts deployment)
  - FAIRNESS_ANOMALY_DETECTED            (P1)
  - REGULATORY_PACKAGE_READY
  - GOVERNANCE_REVIEW_REQUIRED
  - SHAP_INTEGRITY_FAILURE
  - EIE_INSUFFICIENT_DATA
  - NLG_FALLBACK_ACTIVE
```

---

## 1️⃣5️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

```json
EMIT_FEATURE_VECTOR: {
  "user_id":        "string (hashed — no raw user_id)",
  "entity_type":    "EXPLANATION_RECORD",
  "entity_id":      "prediction_id",
  "feature_name":   "string (e.g., explanation_confidence_score,
                     top_feature_rank_1_name, explanation_depth,
                     explanation_completeness, nlg_generated)",
  "feature_value":  "float | string (categorical features as encoded floats)",
  "timestamp":      "ISO-8601 UTC",
  "source_agent":   "MODEL_EXPLAINABILITY_DIFF_AGENT",
  "domain_track":   "string"
}
```

These vectors power downstream ML models that predict explanation engagement ("Did the user understand their explanation?") and recommendation quality ("Are high-confidence explanations correlated with better user outcomes?").

---

## 1️⃣6️⃣ GROWTH ENGINE HOOK

```
EXPLANATION_ENGAGEMENT_TRIGGERS:

  When a user reads their explanation (tracked via API event):
  → EMIT XP_UPDATE_EVENT: +10 XP for "Understanding your results"
     (encourages users to read and engage with explanations — aligns with
      platform goal of transparent, learning-driven career growth)

  When a user acts on an explanation (e.g., enrolls in a recommended skill):
  → EMIT XP_UPDATE_EVENT: +25 XP for "Acting on your insights"

  Achievement unlocks (examples):
  → "Insight Seeker" badge: First time user reads 5 explanations
  → "Data-Driven" badge: User improves a specific metric that was flagged
     in their explanation

  RANK_UPDATE_EVENT = NOT DIRECTLY (explanations do not affect rank,
                      but actions taken based on explanations affect rank
                      through the normal activity pipeline)

  SHARE_TRIGGER_EVENT:
    User may share anonymized version of their skill gap explanation or
    intelligence profile summary. MEDA generates a shareable explanation
    summary that REMOVES all contribution values and shows only qualitative text.
    Raw SHAP values and feature names MUST NOT be included in shareable content.
```

---

## 1️⃣7️⃣ PERFORMANCE MONITORING

```
METRICS_EMITTED (Prometheus):

  meda_explanation_requests_total          (counter, labels: request_type, domain_track)
  meda_explanations_generated_total        (counter, labels: model_id, completeness)
  meda_explanations_served_total           (counter, labels: model_id, role, depth)
  meda_explanation_rejected_total          (counter, labels: rejection_reason)
  meda_shap_additivity_failures_total      (counter, labels: model_id)
  meda_nlg_fallback_rate                   (gauge, labels: model_id)
  meda_explanation_completeness_gauge      (gauge: % predictions with complete explanations)
  meda_confidence_score_histogram          (histogram, labels: model_id)
  meda_processing_latency_ms               (histogram: p50, p95, p99, labels: request_type)
  meda_diff_reports_generated_total        (counter, labels: model_id, severity)
  meda_material_diffs_total                (counter, labels: model_id)
  meda_breaking_diffs_total                (counter, labels: model_id)
  meda_fairness_anomalies_total            (counter, labels: model_id, domain_track)
  meda_regulatory_packages_generated_total (counter)
  meda_archive_write_failures_total        (counter)
  meda_explanation_archive_size_bytes      (gauge)
  meda_unauthorized_access_attempts_total  (counter)

SUCCESS_RATE_TARGET    = > 99.9% explanation generation success
ERROR_RATE_THRESHOLD   = < 0.1%
COMPLETENESS_TARGET    = > 99.5% of predictions have explanation within 30s
LATENCY_SLA:
  Async explanation generation P99 = < 200ms
  User-facing explanation retrieval P99 = < 300ms (from archive)
  Real-time fallback explanation P99 = < 800ms
  Diff computation P99 = < 60 seconds

INTEGRATES_WITH:
  - OBSERVABILITY_AGENT (Prometheus + Grafana + Jaeger)
  - Alertmanager (PagerDuty / Opsgenie for P0/P1)
  - Distributed tracing (Jaeger — full trace on every explanation)
  - Kibana (structured JSON logs)
```

---

## 1️⃣8️⃣ VERSIONING POLICY

```
VERSIONING_RULES:
  ✅ All changes: ADD-ONLY
  ✅ Every change: Semantic version bump
  ✅ All explanation schema changes: Backward compatible + migration script
  ✅ All NLG prompt changes: Versioned independently — old prompts retained 2 years
  ✅ All diff algorithm changes: Require governance approval + diff-of-diff test
  ✅ All fairness metric changes: Require compliance approval + fairness re-audit
  ✅ Rollback: Safe to N-1 guaranteed
  ✅ Explanation archive: WORM — no version can modify archived explanations

CURRENT_VERSION: MEDA-v1.0.0
NLG_PROMPT_VERSION: NLG_PROMPT_v1.0.0

VERSION_HISTORY:
  v1.0.0 — Initial sealed specification (2025)
```

---

## 1️⃣9️⃣ DEPLOYMENT REQUIREMENTS

```
RUNTIME:               Kubernetes (containerized, HPA)
REPLICA_MIN:           3 (production), 2 (staging), 1 (test/dev)
REPLICA_MAX:           40 (auto-scale on CPU — SHAP is CPU-intensive)
RESOURCE_REQUESTS:     CPU: 1000m, Memory: 1Gi (per replica)
                       (SHAP computation is CPU-heavy — higher CPU floor)
RESOURCE_LIMITS:       CPU: 4000m, Memory: 4Gi (per replica)

HEALTH_CHECKS:
  Liveness:            /health/live (HTTP 200 = alive)
  Readiness:           /health/ready (HTTP 200 = explanation archive + model registry connected)
  Startup:             60s grace (model registry sync on startup)

CONFIG_MANAGEMENT:     Kubernetes ConfigMap + Vault Secrets (no hardcoded values)
SECRETS:               HashiCorp Vault Agent Sidecar injection
NETWORK_POLICY:        Strict whitelist-only
SERVICE_MESH:          Istio (mTLS + traffic observability)
TRACING:               Jaeger (100% sampling dev/test, 10% production,
                       100% for regulatory explanation requests always)
STORAGE:               Explanation archive: WORM-compliant object storage (MinIO or S3-compatible)
                       Explanation index: PostgreSQL (append-only, no UPDATE/DELETE)
                       Explanation cache (active sessions): Redis
```

---

## 2️⃣0️⃣ NON-NEGOTIABLE ABSOLUTE RULES

```
❌ NEVER fabricate an explanation (all explanations must derive from real SHAP values)
❌ NEVER retroactively edit or modify an archived explanation record
❌ NEVER serve a user-facing explanation without completing the authorization check
❌ NEVER include raw database column names in user-facing explanation text
   (only approved display_names from FEATURE_DISPLAY_NAME_REGISTRY)
❌ NEVER include another user's data in an explanation
❌ NEVER show EIE/HIA scores below confidence floor (0.80) to users
❌ NEVER frame intelligence scores as "low" or "bad" in user-facing text
   (empathetic, growth-oriented language REQUIRED per EIE/HIA rules)
❌ NEVER allow AI (NLG) to introduce information not present in SHAP output
❌ NEVER allow AI (NLG) to soften or harden a factual explanation (no editorial)
❌ NEVER suppress a SHAP additivity failure (reject and log — never silently accept)
❌ NEVER deploy a new model version without completing the diff computation
❌ NEVER deliver a regulatory explanation package with confidence < 0.95
❌ NEVER cross-correlate explanation data across tenants
❌ NEVER allow explanation archive to accept UPDATE or DELETE operations
❌ NEVER bypass RBAC authorization on explanation access
❌ NEVER share raw SHAP values in user-facing shareable explanation content
❌ NEVER modify historical records (immutable by design)
❌ NEVER override governance, compliance, or security agents
❌ NEVER execute outside declared scope
❌ NEVER create hidden explanation logic or undocumented evaluation paths
```

---

## 🔐 AGENT SEAL DECLARATION

```
╔════════════════════════════════════════════════════════════════════════╗
║         MODEL_EXPLAINABILITY_DIFF_AGENT — SEALED                       ║
║         Platform: Ecoskiller Antigravity                               ║
║         Version:  MEDA-v1.0.0                                          ║
║         NLG Prompt Version: NLG_PROMPT_v1.0.0                         ║
║         Status:   FINAL · LOCKED · GOVERNED                            ║
║                                                                         ║
║  This agent explains every ML decision on the platform.                ║
║  It detects when explanations change between model versions.           ║
║  It enforces that no ML decision is served without an explanation.     ║
║  It protects users' right to understand decisions that affect them.    ║
║                                                                         ║
║  Explanations are derived exclusively from SHAP feature attribution.  ║
║  AI assists in formatting explanations only — zero decision authority. ║
║  No fabrication. No retroactive edits. No completeness suppression.   ║
║                                                                         ║
║  This specification is COMPLETE and SEALED.                            ║
║  No interpretation beyond declared scope.                              ║
║  No assumption filling.                                                ║
║  No creative deviation.                                                ║
║  No mutation without version bump + governance approval.               ║
║                                                                         ║
║  Any deviation from this specification =                               ║
║  STOP EXECUTION + GOVERNANCE ESCALATION                                ║
╚════════════════════════════════════════════════════════════════════════╝
```

---

*Document generated under Ecoskiller Antigravity Master Agent Creation Framework. All sections comply with the Master Agent Creation Prompt v1.0, Ecoskiller Master Execution Prompt v12.0, and R12 (AI Model Specification) requirements. Mutation policy: Add-only. Authority: Human declaration only.*
