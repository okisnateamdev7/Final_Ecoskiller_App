# 🔒 MODEL_DATA_VALIDATION_AGENT
## ECOSKILLER ANTIGRAVITY — INTELLIGENCE & INNOVATION CORE
### Status: SEALED · LOCKED · GOVERNED · DETERMINISTIC
### Mutation Policy: ADD-ONLY via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only
### Version: MDVA-AG-v1.0.0

---

## ⚠️ SEAL DECLARATION

```
THIS DOCUMENT IS SEALED.
NO AGENT, SYSTEM, OR HUMAN MAY:
  - Reinterpret any validation rule
  - Add creative assumptions to pass/fail logic
  - Skip any declared validation layer
  - Soften rejection thresholds
  - Override governance escalations
  - Allow partial model deployment on failed validation
  - Auto-approve model promotion without all gates passing

DEFAULT_BEHAVIOR            = DENY
CREATIVE_INTERPRETATION     = FORBIDDEN
ASSUMPTION_FILLING          = FORBIDDEN
THRESHOLD_NEGOTIATION       = FORBIDDEN
PARTIAL_DEPLOYMENT          = FORBIDDEN
FAILURE_MODE                = STOP_EXECUTION → LOG_INCIDENT → ESCALATE
SILENT_PASS_ON_FAILURE      = FORBIDDEN
```

---

## 1️⃣ AGENT IDENTITY

```yaml
AGENT_NAME:           MODEL_DATA_VALIDATION_AGENT
AGENT_ID:             MDVA-AG-001
SYSTEM_ROLE:          Cross-cutting ML/AI Quality Governance Gate — Validates all
                      training data, model artifacts, inference inputs, inference outputs,
                      and feature data quality across the entire Ecoskiller Antigravity
                      Intelligence Layer before any model is trained, promoted, or served
PRIMARY_DOMAIN:       ML Governance / Data Quality / Model Validation / Inference Safety
EXECUTION_MODE:       Deterministic + Rule-Based + Statistical + Append-Only Audit
DATA_SCOPE:           All training datasets, inference payloads, feature vectors,
                      model artifacts, and output predictions — scoped per
                      tenant_id + model_id + validation_run_id
TENANT_SCOPE:         STRICT_ISOLATION — zero cross-tenant data exposure during validation
FAILURE_POLICY:       HALT_ON_ANY_GATE_FAILURE → LOG_FULL_REPORT → BLOCK_PROMOTION
                      → ESCALATE_TO: MODEL_GOVERNANCE_AGENT
PLATFORM:             Ecoskiller Antigravity
ARCHITECTURE:         Microservices + Event-Driven (Redis Streams + PostgreSQL)
SCALE_TARGET:         Validates models serving 10M–100M users
ML_WEIGHT:            75% Traditional ML model governance (primary)
AI_WEIGHT:            25% LLM / Semantic model governance (secondary)
MUTATION_POLICY:      ADD-ONLY versioned — thresholds may only be tightened, not relaxed
SECURITY_MODEL:       Zero-trust · All validation runs tenant-isolated · Audit immutable
DATA_POLICY:          Append-only validation records — no retroactive pass reassignment
```

---

## 2️⃣ PURPOSE DECLARATION

### Problem This Agent Solves

Ecoskiller Antigravity operates an Intelligence Layer serving 10M–100M users across 300 user types, 5 domain tracks, and multiple tenant organizations. The system depends on ML models trained on behavioral, engagement, progression, social, and integrity feature vectors. Without rigorous, automated validation gates across the full ML lifecycle, the following failure classes would go undetected:

- Models trained on poisoned, imbalanced, or PII-contaminated training data
- Feature vectors with null flooding, schema drift, or distribution shift fed into inference
- Models with unacceptable accuracy, fairness violations, or cross-domain contamination promoted to production
- Inference outputs containing confidence-below-threshold predictions served silently to consumers
- Training data from one tenant leaking into another tenant's model training run
- AI/LLM outputs with hallucination, schema violation, or policy breach served to downstream agents

The `MODEL_DATA_VALIDATION_AGENT` is the **ML governance gate**. It intercepts every data and model artifact at the following checkpoints:

1. **Training Data Validation** — before any model training run is initiated
2. **Feature Vector Validation** — on every feature vector entering the Feature Store (from all producers)
3. **Model Artifact Validation** — before any trained model is registered in the Model Registry
4. **Pre-Inference Validation** — before any inference payload is processed by a live model
5. **Post-Inference Validation** — before any model prediction is delivered to a consumer agent
6. **Drift Detection** — continuous statistical monitoring of feature distributions and model accuracy
7. **Fairness Validation** — periodic audit of model outputs across user segments and domain tracks

### What Input It Consumes

Training datasets (Parquet/JSONL from MinIO), feature vector write requests (from all BSP and producer agents via Feature Store intercept), model artifact metadata (from MODEL_REGISTRY_AGENT), inference request payloads (from MATCHING, RANKING, RECOMMENDATION agents), inference response payloads (from all ML/AI agents), and drift signals (from FEATURE_STORE_AGENT and OBSERVABILITY_AGENT).

### What Output It Produces

Validation reports (structured JSON), gate pass/fail decisions, promotion approval or block events to MODEL_REGISTRY_AGENT, drift alerts to MODEL_GOVERNANCE_AGENT, fairness audit reports to COMPLIANCE_ADMIN, and immutable audit entries to AUDIT_LOG_AGENT.

### Upstream Agents (Feed This Agent)

```yaml
UPSTREAM_AGENTS:
  - FEATURE_STORE_AGENT                 # Feature vectors (validation intercept on write path)
  - BEHAVIOR_STREAM_PROCESSOR           # Raw behavioral event features (pre-store validation)
  - ASSESSMENT_ENGINE_AGENT             # Assessment feature vectors
  - PROCTORING_AGENT                    # Integrity feature vectors
  - SKILL_GRAPH_AGENT                   # Skill mastery feature vectors
  - MODEL_REGISTRY_AGENT                # Trained model artifacts awaiting validation
  - MATCHING_ENGINE_AGENT               # Inference requests + responses
  - RANKING_ENGINE_AGENT                # Inference requests + responses
  - RECOMMENDATION_AGENT                # Inference requests + responses
  - CAREER_PATH_AGENT                   # Inference requests + responses
  - SKILL_GAP_AGENT                     # Inference requests + responses
  - AI_CAREER_ADVISOR_AGENT             # LLM inference requests + responses
  - OBSERVABILITY_AGENT                 # Live drift signals, latency anomalies
```

### Downstream Agents (Depend On This Agent)

```yaml
DOWNSTREAM_AGENTS:
  - MODEL_REGISTRY_AGENT                # Receives promotion approval/block decisions
  - MODEL_GOVERNANCE_AGENT              # Receives drift alerts, fairness reports, degradation signals
  - FEATURE_STORE_AGENT                 # Receives feature rejection or low_quality flag
  - OBSERVABILITY_AGENT                 # Receives validation health metrics
  - AUDIT_LOG_AGENT                     # Receives all immutable validation records
  - SECURITY_AGENT                      # Receives PII contamination or cross-tenant violation events
  - COMPLIANCE_ADMIN (human)            # Receives fairness audit reports
  - PLATFORM_SUPER_ADMIN (human)        # Receives critical governance escalations
```

---

## 3️⃣ VALIDATION ARCHITECTURE — SEVEN GATE SYSTEM

MDVA enforces a **Seven Gate System**. Every gate is mandatory. A failure at any gate halts the pipeline and blocks promotion.

```
╔══════════════════════════════════════════════════════════════════════╗
║           MDVA SEVEN GATE VALIDATION PIPELINE                       ║
╠══════════════════════════════════════════════════════════════════════╣
║  GATE 1 → TRAINING DATA VALIDATION                                  ║
║  GATE 2 → FEATURE VECTOR QUALITY VALIDATION                         ║
║  GATE 3 → MODEL ARTIFACT STRUCTURAL VALIDATION                      ║
║  GATE 4 → MODEL PERFORMANCE VALIDATION                              ║
║  GATE 5 → FAIRNESS & BIAS VALIDATION                                ║
║  GATE 6 → PRE-INFERENCE PAYLOAD VALIDATION                          ║
║  GATE 7 → POST-INFERENCE OUTPUT VALIDATION                          ║
╠══════════════════════════════════════════════════════════════════════╣
║  CONTINUOUS → DRIFT DETECTION ENGINE                                 ║
║  CONTINUOUS → CONFIDENCE FLOOR MONITOR                              ║
║  CONTINUOUS → CROSS-TENANT CONTAMINATION MONITOR                    ║
╚══════════════════════════════════════════════════════════════════════╝

GATE DEPENDENCY:
  GATE 1 must PASS before model training begins
  GATE 2 runs continuously — failures flag features in Feature Store
  GATE 3 must PASS before model artifact is registered
  GATE 4 must PASS before model is promoted to STAGING
  GATE 5 must PASS before model is promoted to PRODUCTION
  GATE 6 runs on every inference request — failure blocks the request
  GATE 7 runs on every inference response — failure suppresses the response

FAILURE PROPAGATION:
  ANY gate FAIL → STOP pipeline at that gate
  → GENERATE validation_report (structured, immutable)
  → EMIT VALIDATION_FAILED_EVENT to MODEL_GOVERNANCE_AGENT
  → BLOCK promotion or inference as appropriate
  → LOG to AUDIT_LOG_AGENT
  → DO NOT proceed to next gate
```

---

## 4️⃣ GATE 1 — TRAINING DATA VALIDATION

### Trigger

Automatically triggered when a training job is submitted to the ML pipeline. MDVA intercepts before any compute is allocated.

### Validation Checks

```yaml
SCHEMA_VALIDATION:
  - All declared feature columns must be present in training dataset
  - Column data types must match FEATURE_REGISTRY type declarations
  - No undeclared columns permitted (unknown columns = REJECT)
  - Schema version of dataset must match current FEATURE_REGISTRY version
  - FAIL CONDITION: Any schema mismatch → BLOCK + REPORT

NULL_AND_COMPLETENESS_CHECK:
  - Required feature columns: null rate MUST be < 2%
  - Optional feature columns: null rate flagged if > 20% (WARNING, not block)
  - Rows with > 30% missing values: EXCLUDED from training + counted + logged
  - If excluded rows > 10% of total dataset: BLOCK + REPORT
  - FAIL CONDITION: Required column null rate ≥ 2%, OR excluded rows > 10%

VOLUME_CHECK:
  - Minimum training samples per model type:
      Classification models:   ≥ 10,000 labeled samples per class
      Regression models:       ≥ 5,000 samples per target range decile
      Clustering models:       ≥ 50,000 total samples
      Time-series models:      ≥ 365 days of data OR ≥ 500,000 events
      Ranking models:          ≥ 100,000 query-candidate pairs
  - FAIL CONDITION: Volume below minimum threshold for declared model type

CLASS_IMBALANCE_CHECK:
  - For classification models: minority class ratio MUST be ≥ 5% of total
  - If minority class < 5%: WARN + auto-trigger SMOTE recommendation
  - If minority class < 1%: BLOCK + REPORT
  - FAIL CONDITION: Minority class ratio < 1%

DISTRIBUTION_BASELINE:
  - For each numerical feature: compute mean, std, min, max, P5, P95, P99
  - Store as TRAINING_DISTRIBUTION_BASELINE (used for future drift detection)
  - For categorical features: compute value frequency distribution
  - Missing baseline = BLOCK (baseline must be computed before training)
  - FAIL CONDITION: Baseline computation failure

TEMPORAL_INTEGRITY:
  - For time-series models: timestamps must be monotonically non-decreasing
  - For event-based features: computed_at_utc cannot be future-dated
  - For behavioral features: event timestamps cannot precede user account creation date
  - FAIL CONDITION: Any temporal integrity violation

PII_CONTAMINATION_CHECK:
  - Scan all text fields for patterns: email, phone, national ID, credit card, IP address
  - Any raw PII detected in training data → IMMEDIATE BLOCK + SECURITY_INCIDENT
  - Acceptable: UUID identifiers (hashed), anonymized tokens
  - Not acceptable: email addresses, full names in free text, raw phone numbers
  - FAIL CONDITION: Any PII pattern match → SECURITY_INCIDENT + ESCALATE_TO SECURITY_AGENT

CROSS_TENANT_CONTAMINATION_CHECK:
  - Every training sample must carry tenant_id
  - Training job must declare target_tenant_id
  - Any sample with tenant_id ≠ target_tenant_id → IMMEDIATE BLOCK + SECURITY_INCIDENT
  - FAIL CONDITION: Any cross-tenant record in training dataset

LABEL_QUALITY_CHECK (for supervised models):
  - Label completeness: labeled samples / total samples MUST be ≥ 95%
  - Label consistency: duplicate feature vectors with conflicting labels checked
  - If conflicting labels > 2%: WARN; if > 5%: BLOCK
  - FAIL CONDITION: Label completeness < 95% or conflicting labels > 5%

DOMAIN_TRACK_ISOLATION:
  - Training data for Arts domain models MUST NOT contain Technology domain features
  - Each model must declare its domain_track scope in training job metadata
  - Cross-domain feature contamination → BLOCK + REPORT
  - FAIL CONDITION: Cross-domain feature presence in training dataset

RECENCY_CHECK:
  - Training data MUST include samples from last 90 days minimum
  - If most recent sample > 90 days old: WARN + human review required
  - If most recent sample > 180 days old: BLOCK
  - FAIL CONDITION: Most recent sample older than 180 days
```

### Gate 1 Output

```json
{
  "gate": "GATE_1_TRAINING_DATA",
  "validation_run_id": "UUID",
  "model_id": "string",
  "tenant_id": "UUID",
  "dataset_id": "string",
  "dataset_version": "string",
  "status": "PASS | FAIL | WARN",
  "total_samples": "int",
  "excluded_samples": "int",
  "null_rates": { "feature_name": "float" },
  "class_distribution": { "class_label": "float" },
  "pii_detected": "boolean",
  "cross_tenant_detected": "boolean",
  "distribution_baseline_stored": "boolean",
  "checks_passed": ["list of passed check names"],
  "checks_failed": ["list of failed check names with reason"],
  "checks_warned": ["list of warned check names"],
  "timestamp_utc": "ISO8601",
  "agent_version": "MDVA-AG-v1.0.0"
}
```

---

## 5️⃣ GATE 2 — FEATURE VECTOR QUALITY VALIDATION

### Trigger

Runs on every feature vector write request intercepted from producer agents before persistence to Feature Store Tier 2. This gate operates **inline on the write path** — rejection prevents storage.

### Validation Checks

```yaml
SCHEMA_COMPLIANCE:
  - feature_name MUST exist in FEATURE_REGISTRY v1.0
  - feature_value data type MUST match FEATURE_REGISTRY declared type
  - feature_version MUST be current active version
  - producer_agent_id MUST be in REGISTERED_PRODUCERS list
  - FAIL CONDITION: Any schema violation → REJECT write

VALUE_RANGE_VALIDATION:
  - For float features: value MUST be within [value_range_min, value_range_max] declared in FEATURE_REGISTRY
  - For int features: value MUST be ≥ 0 unless negative values explicitly declared
  - For string_enum features: value MUST be in allowed_values list
  - For boolean features: value MUST be true or false — no null, no string "null"
  - FAIL CONDITION: Any out-of-range or invalid enum value → REJECT write

CONFIDENCE_FLOOR_CHECK:
  - confidence_score MUST be ≥ 0.0 and ≤ 1.0
  - confidence_score < 0.3: REJECT write + LOG_LOW_QUALITY_FEATURE
  - confidence_score 0.3–0.5: ACCEPT write + set low_confidence_flag=true + LOG
  - confidence_score ≥ 0.5: ACCEPT write — standard path
  - FAIL CONDITION: confidence_score < 0.3

TEMPORAL_FRESHNESS_CHECK:
  - computed_at_utc MUST NOT be future-dated (drift tolerance: +5 seconds)
  - computed_at_utc MUST NOT be older than 2× stale_threshold for that feature_name
  - Example: session_depth_score stale_threshold=86400s → reject if older than 172800s
  - FAIL CONDITION: Computed_at_utc exceeds 2× stale_threshold

STATISTICAL_ANOMALY_DETECTION:
  - For each feature_name: compare incoming value against rolling 30-day distribution baseline
  - Z-score > 4.0 (4 standard deviations from mean): FLAG as anomaly + set anomaly_flag=true
  - Z-score > 6.0: REJECT write + LOG_STATISTICAL_ANOMALY + ESCALATE to MODEL_GOVERNANCE_AGENT
  - Categorical features: new value not seen in training distribution → FLAG
  - FAIL CONDITION: Z-score > 6.0

DUPLICATE_WINDOW_CHECK:
  - feature_vector_id checked against 24-hour Redis dedup window
  - Duplicate: ACCEPT silently as DUPLICATE (idempotency — not a validation failure)

TENANT_DOMAIN_COHERENCE:
  - Feature's domain_track must be coherent with user's enrolled domain
  - Technology-domain feature written for an Arts-enrolled user: WARN + FLAG unless cross-domain RBAC granted
  - FAIL CONDITION: Hard domain mismatch with no RBAC grant

PRODUCER_HEALTH_CHECK:
  - Track rolling 5-minute rejection rate per producer_agent_id
  - If rejection rate > 20% for any producer: emit PRODUCER_DEGRADED_ALERT
  - If rejection rate > 50% for any producer: BLOCK all writes from that producer + ESCALATE
  - FAIL CONDITION: Not a per-vector failure — producer-level circuit breaker
```

### Gate 2 Output (Per Vector)

```json
{
  "gate": "GATE_2_FEATURE_VECTOR",
  "validation_run_id": "UUID",
  "feature_vector_id": "UUID",
  "feature_name": "string",
  "producer_agent_id": "string",
  "tenant_id": "UUID",
  "user_id": "UUID",
  "status": "PASS | FAIL | WARN | DUPLICATE",
  "rejection_reason": "string | null",
  "anomaly_flag": "boolean",
  "z_score": "float | null",
  "confidence_score": "float",
  "low_confidence_flag": "boolean",
  "timestamp_utc": "ISO8601",
  "agent_version": "MDVA-AG-v1.0.0"
}
```

---

## 6️⃣ GATE 3 — MODEL ARTIFACT STRUCTURAL VALIDATION

### Trigger

Triggered when a trained model artifact is submitted to MODEL_REGISTRY_AGENT for registration. Runs before any staging deployment.

### Validation Checks

```yaml
ARTIFACT_INTEGRITY:
  - Model artifact checksum (SHA-256) verified at submission against training job declaration
  - Artifact must not be corrupted (deserialization test — attempt load without inference)
  - Artifact file size within declared bounds (anomaly if > 3× median for model type)
  - FAIL CONDITION: Checksum mismatch, deserialization failure, or extreme size anomaly

MODEL_METADATA_COMPLETENESS:
  - Required metadata fields:
      model_id, model_type, model_version, training_job_id, training_dataset_id,
      training_dataset_version, feature_names_used, target_variable, domain_track,
      tenant_id, training_completed_at_utc, framework_name, framework_version,
      python_version, training_distribution_baseline_ref
  - Any missing field → BLOCK
  - FAIL CONDITION: Any required metadata field absent

FEATURE_CONTRACT_COMPLIANCE:
  - All features declared in model metadata must exist in FEATURE_REGISTRY v1.0
  - Feature versions used in training must match versions currently active in Feature Store
  - If feature version mismatch > 0: WARN + require human sign-off for promotion
  - If feature no longer exists in FEATURE_REGISTRY: BLOCK
  - FAIL CONDITION: Undefined feature or hard version incompatibility

FRAMEWORK_VERSION_LOCK:
  - Scikit-learn: must be declared version in R1 Technology Stack Lock
  - XGBoost / LightGBM: must be declared version
  - PyTorch / TensorFlow: must be declared version
  - LLM base models: must reference approved model from AI_MODEL_APPROVED_LIST
  - Unapproved framework version → BLOCK
  - FAIL CONDITION: Framework version not in approved stack

EXPLAINABILITY_ARTIFACT:
  - For all classification and regression models: SHAP or LIME explainability artifact required
  - Explainability artifact must cover top-10 most influential features
  - Absence of explainability artifact → BLOCK for PRODUCTION promotion
    (allowed in STAGING with WARNING)
  - FAIL CONDITION (PRODUCTION): Missing explainability artifact

TRAINING_GATE1_REFERENCE:
  - Model artifact must reference a GATE_1 validation_run_id with status=PASS
  - If referenced Gate 1 run is absent or FAIL: BLOCK
  - FAIL CONDITION: No passed Gate 1 reference in model metadata

TENANT_SCOPE_DECLARATION:
  - Model must declare exact tenant_id scope or GLOBAL (platform-wide models only)
  - GLOBAL models require PLATFORM_SUPER_ADMIN sign-off
  - FAIL CONDITION: Missing tenant_scope declaration
```

### Gate 3 Output

```json
{
  "gate": "GATE_3_MODEL_ARTIFACT",
  "validation_run_id": "UUID",
  "model_id": "string",
  "model_version": "string",
  "tenant_id": "UUID | GLOBAL",
  "artifact_checksum": "SHA-256",
  "checksum_verified": "boolean",
  "metadata_complete": "boolean",
  "feature_contract_compatible": "boolean",
  "framework_approved": "boolean",
  "explainability_present": "boolean",
  "gate1_reference_valid": "boolean",
  "status": "PASS | FAIL | WARN",
  "blocks": ["list of blocking issues"],
  "warnings": ["list of non-blocking warnings"],
  "timestamp_utc": "ISO8601",
  "agent_version": "MDVA-AG-v1.0.0"
}
```

---

## 7️⃣ GATE 4 — MODEL PERFORMANCE VALIDATION

### Trigger

Triggered automatically after Gate 3 PASS. Runs model against held-out validation dataset before staging deployment.

### Validation Thresholds by Model Type

```yaml
CLASSIFICATION_MODELS:
  # Applied to: MATCHING_ENGINE, RANKING_ENGINE, FRAUD_DETECTION, CAREER_STAGE_CLASSIFIER
  MINIMUM_THRESHOLDS:
    accuracy:           ≥ 0.80
    f1_score_macro:     ≥ 0.75
    precision_macro:    ≥ 0.70
    recall_macro:       ≥ 0.70
    auc_roc:            ≥ 0.85
  HARD_BLOCK_BELOW:
    accuracy:           < 0.65
    auc_roc:            < 0.70
  VALIDATION_DATASET:
    Size:               ≥ 20% held-out split from Gate 1 validated training data
    Stratified:         REQUIRED — class distribution must match training distribution

REGRESSION_MODELS:
  # Applied to: ENGAGEMENT_SCORE, REPUTATION_SCORE, INFLUENCE_SCORE
  MINIMUM_THRESHOLDS:
    r2_score:           ≥ 0.70
    mae_relative:       ≤ 15% of target range
    rmse_relative:      ≤ 20% of target range
  HARD_BLOCK_BELOW:
    r2_score:           < 0.50
    mae_relative:       > 30% of target range

CLUSTERING_MODELS:
  # Applied to: USER_COHORT_SEGMENTATION
  MINIMUM_THRESHOLDS:
    silhouette_score:   ≥ 0.40
    davies_bouldin:     ≤ 1.5
    inertia_delta:      < 5% change from prior model version (stability check)
  HARD_BLOCK_BELOW:
    silhouette_score:   < 0.25

RANKING_MODELS:
  # Applied to: SEARCH_RANKING, LEADERBOARD_RANKING, DISCOVERY_RANKING
  MINIMUM_THRESHOLDS:
    ndcg_at_10:         ≥ 0.75
    map_at_10:          ≥ 0.70
    mrr:                ≥ 0.65
  HARD_BLOCK_BELOW:
    ndcg_at_10:         < 0.60

TIME_SERIES_MODELS:
  # Applied to: CHURN_PREDICTION, STREAK_CONTINUATION
  MINIMUM_THRESHOLDS:
    mape:               ≤ 15%
    smape:              ≤ 12%
    direction_accuracy: ≥ 0.70
  HARD_BLOCK_BELOW:
    mape:               > 30%

RECOMMENDATION_MODELS:
  # Applied to: RECOMMENDATION_AGENT
  MINIMUM_THRESHOLDS:
    precision_at_10:    ≥ 0.30
    recall_at_10:       ≥ 0.25
    coverage:           ≥ 0.50 (fraction of item catalog recommendable)
    novelty_score:      ≥ 0.40 (not just popular items)
  HARD_BLOCK_BELOW:
    precision_at_10:    < 0.15
    coverage:           < 0.20

LLM_AI_MODELS:
  # Applied to: AI_CAREER_ADVISOR, semantic enrichment components
  MINIMUM_THRESHOLDS:
    schema_compliance_rate:    ≥ 0.98   (structured output schema adherence)
    hallucination_rate:        ≤ 0.02   (fact-checked against knowledge base)
    response_completeness:     ≥ 0.95   (required output fields present)
    toxicity_score:            ≤ 0.01   (Perspective API or equivalent)
    policy_compliance_rate:    ≥ 0.99   (no forbidden content categories)
  HARD_BLOCK_BELOW:
    schema_compliance_rate:    < 0.90
    hallucination_rate:        > 0.05
    toxicity_score:            > 0.03
```

### Regression Testing vs. Production Baseline

```yaml
REGRESSION_CHECK:
  - Every new model version compared against current PRODUCTION model metrics
  - Performance regression threshold: primary metric MAY NOT degrade > 3% from production baseline
  - If degradation 1–3%: WARN + require human sign-off for promotion
  - If degradation > 3%: BLOCK + REPORT
  - FAIL CONDITION: Primary metric degrades > 3% from production baseline

LATENCY_BUDGET_CHECK:
  - P99 inference latency MUST be validated on STAGING hardware before promotion
  - Classification/Regression: P99 < 100ms
  - Ranking models: P99 < 150ms
  - Recommendation models: P99 < 200ms
  - LLM models: P99 < 3000ms
  - FAIL CONDITION: Latency exceeds declared budget on staging benchmark
```

### Gate 4 Output

```json
{
  "gate": "GATE_4_MODEL_PERFORMANCE",
  "validation_run_id": "UUID",
  "model_id": "string",
  "model_version": "string",
  "model_type": "string",
  "tenant_id": "UUID | GLOBAL",
  "validation_dataset_id": "string",
  "metrics": { "metric_name": "float" },
  "thresholds_met": { "metric_name": "boolean" },
  "regression_vs_production": { "metric_name": "delta_float" },
  "regression_blocked": "boolean",
  "latency_p99_ms": "float",
  "latency_budget_met": "boolean",
  "status": "PASS | FAIL | WARN",
  "blocks": ["list"],
  "warnings": ["list"],
  "timestamp_utc": "ISO8601",
  "agent_version": "MDVA-AG-v1.0.0"
}
```

---

## 8️⃣ GATE 5 — FAIRNESS & BIAS VALIDATION

### Trigger

Runs after Gate 4 PASS. Mandatory before PRODUCTION promotion. May be run on STAGING for early detection.

### Fairness Checks

```yaml
DEMOGRAPHIC_PARITY:
  - Model predictions must not show systematic bias across:
      domain_track (Arts | Commerce | Science | Technology | Administration)
      user_role_category (8 declared categories)
      geo_region_code (if available)
  - Disparate impact ratio: favorable prediction rate for any group / overall rate MUST be ≥ 0.80
  - If ratio < 0.80 for any group: WARN + human review required before production
  - If ratio < 0.70 for any group: BLOCK
  - FAIL CONDITION: Disparate impact ratio < 0.70 for any declared group

EQUAL_OPPORTUNITY:
  - True positive rate (recall) across groups must not differ by > 10%
  - Max TPR − Min TPR across all groups ≤ 0.10
  - FAIL CONDITION: TPR difference > 10% across groups

RECOMMENDATION_DIVERSITY:
  # For RECOMMENDATION_AGENT specifically
  - Recommendations must not over-represent any single domain, skill category, or employer
  - No single entity_id may appear in > 30% of recommendations for a user cohort
  - FAIL CONDITION: Single entity over-representation > 30%

SKILL_GAP_FAIRNESS:
  # For SKILL_GAP_AGENT specifically
  - Skill gap suggestions must not systematically channel domain-track users
    toward lower-paying or lower-prestige career paths based on group membership
  - Statistical test: chi-square on career path distribution across groups
  - p-value < 0.05 indicating systematic channeling: WARN + review
  - FAIL CONDITION: Confirmed systematic adverse channeling (p < 0.001)

MATCHING_FAIRNESS:
  # For MATCHING_ENGINE_AGENT
  - Job match scores for equally qualified candidates from different groups
    must not differ by > 15% on average
  - FAIL CONDITION: Match score disparity > 15% for equally-qualified candidates

TRAINING_REPRESENTATION_CHECK:
  - Training data must contain samples from all 5 domain tracks
  - No domain track may comprise < 5% of training samples if that domain is in model scope
  - FAIL CONDITION: Any in-scope domain track has < 5% representation

TEMPORAL_FAIRNESS:
  - Model performance must not systematically degrade for recent users vs. established users
  - New user cohort (< 30 days) prediction quality vs. established cohort (> 180 days)
  - Degradation > 15% for new users: WARN; > 25%: BLOCK
  - FAIL CONDITION: New user performance degradation > 25%
```

### Gate 5 Output

```json
{
  "gate": "GATE_5_FAIRNESS_BIAS",
  "validation_run_id": "UUID",
  "model_id": "string",
  "model_version": "string",
  "tenant_id": "UUID | GLOBAL",
  "disparate_impact_by_group": { "group_name": "float" },
  "tpr_by_group": { "group_name": "float" },
  "tpr_max_delta": "float",
  "recommendation_diversity_score": "float | null",
  "matching_fairness_score": "float | null",
  "training_domain_representation": { "domain_track": "float" },
  "new_user_performance_delta": "float | null",
  "status": "PASS | FAIL | WARN",
  "requires_human_review": "boolean",
  "blocks": ["list"],
  "warnings": ["list"],
  "timestamp_utc": "ISO8601",
  "agent_version": "MDVA-AG-v1.0.0"
}
```

---

## 9️⃣ GATE 6 — PRE-INFERENCE PAYLOAD VALIDATION

### Trigger

Runs **inline on every inference request** before any model processes the input. This is a real-time, low-latency gate (budget: < 5ms per request).

### Validation Checks

```yaml
SCHEMA_VALIDATION:
  - Inference payload must match declared INPUT_SCHEMA for the target model_id + model_version
  - Required fields: all features declared in model's feature_names_used
  - Any missing required feature: BLOCK inference request
  - FAIL CONDITION: Any required feature absent from payload

FEATURE_VERSION_CHECK:
  - Every feature in payload must carry feature_version matching model's training feature_version
  - Mismatched feature version: BLOCK + LOG_VERSION_MISMATCH
  - FAIL CONDITION: Feature version mismatch

STALE_FEATURE_CHECK:
  - Each feature in payload carries computed_at_utc
  - Stale features (age > stale_threshold for that feature): FLAG + pass with stale_flag=true
  - If > 30% of payload features are stale: WARN consumer + downgrade confidence
  - If > 60% of payload features are stale: BLOCK inference (result would be unreliable)
  - FAIL CONDITION: > 60% of payload features stale

NULL_RATE_CHECK:
  - Null values in required features: BLOCK
  - Null rate in optional features > 50%: WARN + proceed with degraded confidence
  - FAIL CONDITION: Any null in required feature

CONFIDENCE_FLOOR_CHECK:
  - Payload features with low_confidence_flag: counted
  - If > 40% of payload features are low_confidence: WARN consumer
  - If > 70% of payload features are low_confidence: BLOCK (inference unreliable)
  - FAIL CONDITION: > 70% low_confidence features

TENANT_CONTEXT_CHECK:
  - Inference request tenant_id must match model's declared tenant_id scope
  - FAIL CONDITION: Tenant mismatch → BLOCK + SECURITY_INCIDENT

RATE_LIMIT_CHECK:
  - Per consumer agent: maximum inference request rate per model declared in model metadata
  - Exceeded: THROTTLE + LOG (not a hard block for rate limit alone)
  - FAIL CONDITION: Not a hard gate failure — throttle only
```

### Gate 6 Output (inline, low-latency)

```json
{
  "gate": "GATE_6_PRE_INFERENCE",
  "validation_run_id": "UUID",
  "model_id": "string",
  "inference_request_id": "UUID",
  "tenant_id": "UUID",
  "status": "PASS | FAIL | WARN",
  "stale_feature_rate": "float",
  "low_confidence_feature_rate": "float",
  "null_rate_required_features": "float",
  "rejection_reason": "string | null",
  "degraded_confidence": "boolean",
  "timestamp_utc": "ISO8601",
  "processing_duration_ms": "float",
  "agent_version": "MDVA-AG-v1.0.0"
}
```

---

## 🔟 GATE 7 — POST-INFERENCE OUTPUT VALIDATION

### Trigger

Runs **inline on every inference response** before it is delivered to the consumer agent. Budget: < 5ms per response.

### Validation Checks

```yaml
OUTPUT_SCHEMA_COMPLIANCE:
  - Inference response must match declared OUTPUT_SCHEMA for model_id + model_version
  - All required output fields must be present
  - Data types must match declared output schema
  - FAIL CONDITION: Schema violation → SUPPRESS response + LOG + emit INFERENCE_FAILED_EVENT

CONFIDENCE_OUTPUT_CHECK:
  - Every inference response MUST carry a confidence_score (0.0–1.0)
  - confidence_score < 0.40: SUPPRESS response + emit LOW_CONFIDENCE_RESPONSE_EVENT
  - confidence_score 0.40–0.60: FLAG + deliver with low_confidence_flag=true
  - confidence_score ≥ 0.60: deliver normally
  - FAIL CONDITION: confidence_score < 0.40 OR absent

PREDICTION_RANGE_VALIDATION:
  - Classification: predicted class must be in declared class list for that model
  - Regression: predicted value must be within [min, max] of training target distribution ± 20%
  - Ranking: rank scores must be bounded [0.0, 1.0] and non-negative
  - Recommendation: all recommended entity_ids must exist in entity registry
  - FAIL CONDITION: Out-of-bounds prediction → SUPPRESS + LOG_PREDICTION_ANOMALY

CROSS_TENANT_OUTPUT_CHECK:
  - Inference response must not contain entity references from a different tenant
  - Detected cross-tenant entity in output: IMMEDIATE SUPPRESS + SECURITY_INCIDENT
  - FAIL CONDITION: Cross-tenant entity in response

LLM_OUTPUT_SPECIFIC_CHECKS:
  # Applied to AI_CAREER_ADVISOR_AGENT and all LLM-backed agents
  SCHEMA_COMPLIANCE:
    - LLM output must parse successfully as declared JSON schema
    - Parse failure: SUPPRESS + LOG_LLM_SCHEMA_VIOLATION
  CONTENT_POLICY_CHECK:
    - Output scanned against Ecoskiller content policy rules:
        No career discrimination language
        No gender/caste/religion bias in recommendations
        No salary anchoring below regional minimum standards
        No content violating Ecoskiller moderation policies
    - Violation detected: SUPPRESS + LOG_CONTENT_POLICY_VIOLATION + ESCALATE_TO TRUST_SAFETY_AGENT
  HALLUCINATION_CHECK:
    - Named entities (company names, skill names, certification names) validated against
      ENTITY_REGISTRY before delivery
    - Unresolvable entity: FLAG + strip from response + LOG_HALLUCINATION_DETECTED
  TOXICITY_CHECK:
    - Toxicity score MUST be ≤ 0.01 before delivery
    - Toxicity > 0.01: SUPPRESS + LOG_TOXICITY_VIOLATION + ESCALATE_TO TRUST_SAFETY_AGENT
    - FAIL CONDITION: Toxicity > 0.01

RESPONSE_COMPLETENESS:
  - Required output fields missing: SUPPRESS
  - Optional fields missing: WARN + deliver with completeness_flag
  - FAIL CONDITION: Required output field absent
```

### Gate 7 Output (inline, low-latency)

```json
{
  "gate": "GATE_7_POST_INFERENCE",
  "validation_run_id": "UUID",
  "model_id": "string",
  "inference_request_id": "UUID",
  "tenant_id": "UUID",
  "status": "PASS | FAIL | WARN | SUPPRESSED",
  "confidence_score": "float",
  "low_confidence_flag": "boolean",
  "prediction_in_range": "boolean",
  "cross_tenant_entity_detected": "boolean",
  "llm_schema_compliant": "boolean | null",
  "content_policy_compliant": "boolean | null",
  "hallucination_entities_stripped": "int | null",
  "toxicity_score": "float | null",
  "suppression_reason": "string | null",
  "timestamp_utc": "ISO8601",
  "processing_duration_ms": "float",
  "agent_version": "MDVA-AG-v1.0.0"
}
```

---

## 1️⃣1️⃣ CONTINUOUS DRIFT DETECTION ENGINE

### Trigger

Runs continuously — event-driven (triggered by FEATURE_STORED_EVENT from Feature Store) and scheduled (daily full distribution analysis).

### Drift Detection Methods

```yaml
POPULATION_STABILITY_INDEX (PSI):
  FORMULA: PSI = Σ (Actual% − Expected%) × ln(Actual% / Expected%)
  REFERENCE_DISTRIBUTION: Training baseline from Gate 1 TRAINING_DISTRIBUTION_BASELINE
  MONITORING_WINDOW: Rolling 7-day window vs. training baseline
  THRESHOLDS:
    PSI < 0.10:   STABLE — no action
    PSI 0.10–0.20: MODERATE DRIFT — WARN + log to MODEL_GOVERNANCE_AGENT
    PSI > 0.20:   SIGNIFICANT DRIFT — CRITICAL ALERT + recommend model retraining
    PSI > 0.40:   SEVERE DRIFT — FREEZE model predictions + MANDATORY retrain
  FAIL CONDITION: PSI > 0.40 → model frozen until retrained and promoted

KOLMOGOROV-SMIRNOV TEST (KS Test):
  APPLIES TO: All continuous numerical feature distributions
  MONITORING_WINDOW: Rolling 14-day vs. training baseline
  THRESHOLD: KS statistic > 0.15 with p < 0.01 → DRIFT ALERT
  FAIL CONDITION: KS > 0.15 sustained for > 3 consecutive days → ESCALATE

CHI-SQUARE TEST:
  APPLIES TO: All categorical feature distributions
  MONITORING_WINDOW: Rolling 7-day vs. training baseline
  THRESHOLD: p < 0.01 → CATEGORICAL DISTRIBUTION SHIFT ALERT
  FAIL CONDITION: p < 0.01 sustained for > 3 consecutive days

MODEL_ACCURACY_DECAY_MONITOR:
  METHOD: Compare live prediction quality against labeled holdout samples (where available)
  MONITORING_WINDOW: Rolling 14-day accuracy estimate
  THRESHOLDS:
    Primary metric decay < 2%:     STABLE
    Primary metric decay 2–5%:     WARN + flag for review
    Primary metric decay 5–10%:    ALERT + schedule retrain
    Primary metric decay > 10%:    FREEZE model + MANDATORY retrain
  FAIL CONDITION: Accuracy decay > 10%

CONCEPT_DRIFT_DETECTION:
  METHOD: Page-Hinkley test on rolling prediction error stream
  APPLIES TO: All regression and time-series models
  SENSITIVITY: Page-Hinkley delta = 0.005, lambda = 50
  TRIGGER: Page-Hinkley alarm → emit CONCEPT_DRIFT_DETECTED_EVENT
  FAIL CONDITION: Consecutive Page-Hinkley alarms > 3 → FREEZE model

FEATURE_IMPORTANCE_DRIFT:
  METHOD: Compare SHAP value distribution for top-10 features vs. training baseline
  MONITORING_WINDOW: Weekly SHAP re-computation
  THRESHOLD: Any top-3 feature SHAP importance changes by > 30%: ALERT
  FAIL CONDITION: Not a hard freeze — escalate to MODEL_GOVERNANCE_AGENT
```

### Drift Detection Output (Event)

```json
{
  "event_type": "DRIFT_DETECTION_REPORT",
  "drift_run_id": "UUID",
  "model_id": "string",
  "model_version": "string",
  "tenant_id": "UUID | GLOBAL",
  "feature_name": "string | null",
  "drift_type": "PSI | KS | CHI_SQUARE | ACCURACY_DECAY | CONCEPT_DRIFT | FEATURE_IMPORTANCE",
  "drift_score": "float",
  "threshold_breached": "string",
  "action_taken": "STABLE | WARN | ALERT | FREEZE",
  "model_frozen": "boolean",
  "retrain_recommended": "boolean",
  "timestamp_utc": "ISO8601",
  "agent_version": "MDVA-AG-v1.0.0"
}
```

---

## 1️⃣2️⃣ INPUT CONTRACT (STRICT)

### Validation Request Schema (All Gates)

```json
VALIDATION_REQUEST_SCHEMA: {
  "required_fields": [
    "validation_request_id",
    "gate_type",
    "requestor_agent_id",
    "tenant_id",
    "model_id",
    "model_version",
    "payload_ref",
    "timestamp_utc"
  ],
  "optional_fields": [
    "training_job_id",
    "dataset_id",
    "inference_request_id",
    "feature_vector_id",
    "domain_track",
    "experiment_id",
    "priority_level"
  ],
  "validation_rules": [
    "validation_request_id MUST be UUID v4",
    "gate_type MUST be one of: GATE_1 | GATE_2 | GATE_3 | GATE_4 | GATE_5 | GATE_6 | GATE_7 | DRIFT",
    "requestor_agent_id MUST be in REGISTERED_REQUESTORS list",
    "tenant_id MUST be valid and active in IDENTITY_AGENT",
    "payload_ref MUST resolve to actual artifact in MinIO or Redis",
    "timestamp_utc MUST be ISO 8601 UTC — drift > 30s = REJECT"
  ],
  "security_checks": [
    "mTLS certificate verification for all requestor connections",
    "Requestor agent_id authorization validated against IDENTITY_AGENT",
    "Tenant isolation: all validation artifacts scoped to declared tenant_id",
    "No cross-tenant payload references permitted"
  ]
}
```

---

## 1️⃣3️⃣ OUTPUT CONTRACT (STRICT)

### Unified Validation Report Schema

```json
VALIDATION_REPORT_SCHEMA: {
  "result_object": {
    "validation_run_id": "UUID",
    "gate_type": "string",
    "model_id": "string",
    "model_version": "string",
    "tenant_id": "UUID | GLOBAL",
    "requestor_agent_id": "string",
    "status": "PASS | FAIL | WARN | SUPPRESSED | BLOCKED",
    "gate_decisions": {
      "GATE_1": "PASS | FAIL | WARN | NOT_RUN",
      "GATE_2": "PASS | FAIL | WARN | NOT_RUN",
      "GATE_3": "PASS | FAIL | WARN | NOT_RUN",
      "GATE_4": "PASS | FAIL | WARN | NOT_RUN",
      "GATE_5": "PASS | FAIL | WARN | NOT_RUN",
      "GATE_6": "PASS | FAIL | WARN | NOT_RUN",
      "GATE_7": "PASS | FAIL | WARN | NOT_RUN | SUPPRESSED"
    },
    "promotion_approved": "boolean",
    "promotion_blocked_reason": "string | null",
    "requires_human_review": "boolean",
    "drift_alerts": [],
    "fairness_flags": [],
    "security_incidents": [],
    "processing_duration_ms": "int",
    "report_generated_at_utc": "ISO8601"
  },
  "confidence_score": "float (0.0–1.0) — MDVA confidence in validation completeness",
  "model_version": "MDVA-AG-v1.0.0",
  "audit_reference": "UUID",
  "next_trigger_events": [
    "MODEL_PROMOTED_EVENT (on full PASS)",
    "MODEL_BLOCKED_EVENT (on any FAIL)",
    "HUMAN_REVIEW_REQUIRED_EVENT (on WARN requiring sign-off)",
    "DRIFT_ALERT_EVENT (on drift detection)",
    "SECURITY_INCIDENT_EVENT (on cross-tenant or PII violations)"
  ]
}
```

---

## 1️⃣4️⃣ DATABASE SCHEMA (POSTGRESQL 15)

### Core Table: `validation_runs`

```sql
CREATE TABLE validation_runs (
    id                          UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    validation_run_id           UUID            NOT NULL UNIQUE,
    gate_type                   VARCHAR(32)     NOT NULL,
    requestor_agent_id          VARCHAR(64)     NOT NULL,
    tenant_id                   UUID            NOT NULL,
    model_id                    VARCHAR(128)    NOT NULL,
    model_version               VARCHAR(64)     NOT NULL,
    training_job_id             UUID,
    dataset_id                  VARCHAR(256),
    inference_request_id        UUID,
    feature_vector_id           UUID,
    domain_track                VARCHAR(32),
    status                      VARCHAR(32)     NOT NULL,
    promotion_approved          BOOLEAN,
    promotion_blocked_reason    TEXT,
    requires_human_review       BOOLEAN         NOT NULL DEFAULT FALSE,
    human_review_completed      BOOLEAN,
    human_reviewer_id           UUID,
    human_review_decision       VARCHAR(32),
    human_review_at_utc         TIMESTAMPTZ,
    gate_1_status               VARCHAR(16),
    gate_2_status               VARCHAR(16),
    gate_3_status               VARCHAR(16),
    gate_4_status               VARCHAR(16),
    gate_5_status               VARCHAR(16),
    gate_6_status               VARCHAR(16),
    gate_7_status               VARCHAR(16),
    metrics_snapshot            JSONB,
    fairness_snapshot           JSONB,
    drift_snapshot              JSONB,
    security_incident_flag      BOOLEAN         NOT NULL DEFAULT FALSE,
    processing_duration_ms      INTEGER,
    report_payload              JSONB           NOT NULL,
    created_at_utc              TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    agent_version               VARCHAR(64)     NOT NULL DEFAULT 'MDVA-AG-v1.0.0'
    -- APPEND-ONLY: no UPDATE permitted on completed validation records
);

CREATE INDEX idx_vr_model        ON validation_runs (model_id, model_version, gate_type);
CREATE INDEX idx_vr_tenant       ON validation_runs (tenant_id, created_at_utc DESC);
CREATE INDEX idx_vr_security     ON validation_runs (security_incident_flag, created_at_utc DESC)
    WHERE security_incident_flag = TRUE;
CREATE INDEX idx_vr_human_review ON validation_runs (requires_human_review, human_review_completed)
    WHERE requires_human_review = TRUE;
```

### Drift Log Table: `drift_detection_logs`

```sql
CREATE TABLE drift_detection_logs (
    id                      UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    drift_run_id            UUID            NOT NULL UNIQUE,
    model_id                VARCHAR(128)    NOT NULL,
    model_version           VARCHAR(64)     NOT NULL,
    tenant_id               UUID            NOT NULL,
    feature_name            VARCHAR(128),
    drift_type              VARCHAR(32)     NOT NULL,
    drift_score             DOUBLE PRECISION NOT NULL,
    threshold_name          VARCHAR(64),
    threshold_value         DOUBLE PRECISION,
    action_taken            VARCHAR(32)     NOT NULL,
    model_frozen            BOOLEAN         NOT NULL DEFAULT FALSE,
    retrain_recommended     BOOLEAN         NOT NULL DEFAULT FALSE,
    resolved_at_utc         TIMESTAMPTZ,
    resolution_note         TEXT,
    detected_at_utc         TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    agent_version           VARCHAR(64)     NOT NULL DEFAULT 'MDVA-AG-v1.0.0'
);
```

### Fairness Audit Table: `fairness_audit_records`

```sql
CREATE TABLE fairness_audit_records (
    id                          UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    validation_run_id           UUID            NOT NULL REFERENCES validation_runs(validation_run_id),
    model_id                    VARCHAR(128)    NOT NULL,
    model_version               VARCHAR(64)     NOT NULL,
    tenant_id                   UUID            NOT NULL,
    group_type                  VARCHAR(64)     NOT NULL,   -- domain_track | role_category | geo_region
    group_value                 VARCHAR(128)    NOT NULL,
    disparate_impact_ratio      DOUBLE PRECISION,
    true_positive_rate          DOUBLE PRECISION,
    false_positive_rate         DOUBLE PRECISION,
    prediction_count            INTEGER,
    fairness_status             VARCHAR(32)     NOT NULL,   -- PASS | WARN | FAIL
    audited_at_utc              TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    agent_version               VARCHAR(64)     NOT NULL DEFAULT 'MDVA-AG-v1.0.0'
);
```

---

## 1️⃣5️⃣ ML / AI LOGIC WITHIN MDVA

### MDVA's Own ML Components

```yaml
STATISTICAL_ANOMALY_DETECTOR:
  MODEL_TYPE: Z-score + IQR-based anomaly detection (non-parametric)
  PURPOSE: Gate 2 feature value range checking
  UPDATES: Daily recalibration of rolling distribution statistics
  NO_TRAINING_REQUIRED: Rule-based statistical method — deterministic

DISTRIBUTION_COMPARATOR:
  MODEL_TYPE: PSI + KS Test + Chi-Square (statistical tests, not learned models)
  PURPOSE: Drift Detection Engine
  REFERENCE: Training baseline distributions from Gate 1
  UPDATES: Baselines refreshed on every approved model retrain

LLM_OUTPUT_EVALUATOR:
  MODEL_TYPE: Classifier (traditional ML, 80%) + Rule Engine (20%)
  PURPOSE: Gate 7 hallucination and content policy checks
  FEATURES_USED: entity_recognition_score, policy_keyword_match, schema_parse_success
  TRAINING: Supervised on labeled LLM output quality dataset
  FREQUENCY: Monthly retrain as LLM model updates occur
  NOTE: MDVA's internal ML must itself pass GATE 3 and GATE 4 validation before deployment

FAIRNESS_AUDITOR:
  MODEL_TYPE: Statistical tests (disparate impact, chi-square, TPR comparison)
  PURPOSE: Gate 5 bias detection
  NO_TRAINING_REQUIRED: Fully statistical — deterministic computation
```

### MDVA Does Not Use AI for Gate Decisions

```
AI/LLM components within MDVA are limited to:
  - LLM output content policy scanning (Gate 7 only)
  - Validation report natural language summary generation (advisory only)

AI MUST NOT:
  - Override statistical gate pass/fail decisions
  - Soften rejection thresholds
  - Interpret ambiguous validation results favorably
  - Make model promotion decisions
```

---

## 1️⃣6️⃣ SCALABILITY DESIGN

```yaml
GATE_2_INLINE_LATENCY_BUDGET:   < 5ms per feature vector (inline on Feature Store write path)
GATE_6_INLINE_LATENCY_BUDGET:   < 5ms per inference request
GATE_7_INLINE_LATENCY_BUDGET:   < 5ms per inference response
GATE_1_BATCH_LATENCY_TARGET:    < 30 minutes for datasets up to 10M rows
GATE_3_LATENCY_TARGET:          < 5 minutes per model artifact
GATE_4_LATENCY_TARGET:          < 2 hours for full performance evaluation
GATE_5_LATENCY_TARGET:          < 4 hours for full fairness audit

HORIZONTAL_SCALING:
  - Gate 2, 6, 7 (inline): Stateless workers, Kubernetes HPA on CPU > 70%
    min 5 pods, max 100 pods
  - Gate 1, 3, 4, 5 (batch): Kubernetes Jobs — isolated per validation run
  - Drift Engine: Dedicated worker pool, min 3, max 20 pods

QUEUE_STRATEGY:
  - Inline gate requests: Synchronous (Gates 2, 6, 7) — no queue, direct intercept
  - Batch validation requests: Redis Streams queue, consumer group per gate type
  - Dead letter queue: DLQ_VALIDATION for failed batch runs (TTL 72hrs)

IDEMPOTENCY:
  - validation_run_id deduplication — 48-hour window via Redis SET NX
  - Re-submitted validation request with same validation_run_id returns cached result
```

---

## 1️⃣7️⃣ SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  - Every validation run carries tenant_id — all data access scoped to that tenant
  - Training datasets accessed via tenant-namespaced MinIO paths only
  - Cross-tenant reference in any artifact: IMMEDIATE HALT + SECURITY_INCIDENT

PII_DETECTION_ENFORCEMENT:
  - PII scanner runs on ALL training data (Gate 1) and ALL LLM outputs (Gate 7)
  - PII patterns: email regex, phone regex, Aadhaar/PAN patterns (India locale),
    credit card Luhn patterns, IPv4/IPv6 address patterns, full name in free text
  - Detection in training data: IMMEDIATE BLOCK + SECURITY_INCIDENT + ESCALATE_TO SECURITY_AGENT + CISO_ALERT
  - Detection in LLM output: SUPPRESS response + SECURITY_INCIDENT + ESCALATE

VALIDATION_RESULT_IMMUTABILITY:
  - All validation_runs records are append-only
  - No retroactive promotion assignment permitted
  - Human review decisions logged as new events (not updates to existing records)
  - Audit trail immutable — MDVA holds INSERT-ONLY on audit tables

AGENT_AUTHORIZATION:
  - mTLS required for all requestor connections
  - Requestor agents must be in REGISTERED_REQUESTORS list
  - Unregistered requestor: REJECT + LOG + ESCALATE_TO SECURITY_AGENT

THRESHOLD_PROTECTION:
  - All validation thresholds are declared in this sealed document
  - Runtime threshold override: FORBIDDEN
  - Configuration file thresholds are version-locked and hash-verified at startup
  - Any threshold value deviation from sealed spec: HALT + LOG_GOVERNANCE_VIOLATION
```

---

## 1️⃣8️⃣ FAILURE POLICY

```yaml
GATE_1_FAIL:
  ACTION: BLOCK training job from starting
  LOG: LOG_GATE1_FAILURE (training_job_id, dataset_id, failed_checks)
  ESCALATE_TO: MODEL_GOVERNANCE_AGENT (immediate)
  HUMAN_REQUIRED: Yes — must review and resubmit corrected dataset
  RETRY_POLICY: Manual resubmission after issue correction only

GATE_2_FAIL (per vector):
  ACTION: REJECT feature vector write
  LOG: LOG_GATE2_REJECTION (feature_vector_id, producer_agent_id, rejection_reason)
  ESCALATE_TO: OBSERVABILITY_AGENT (batched 60s); if PII: SECURITY_AGENT (immediate)
  RETRY_POLICY: NONE — producer must fix and resubmit

GATE_3_FAIL:
  ACTION: BLOCK model registration in MODEL_REGISTRY
  LOG: LOG_GATE3_FAILURE (model_id, model_version, blocks)
  ESCALATE_TO: MODEL_GOVERNANCE_AGENT (immediate)
  RETRY_POLICY: Fix artifact or metadata and resubmit

GATE_4_FAIL:
  ACTION: BLOCK staging promotion
  LOG: LOG_GATE4_FAILURE (model_id, model_version, metrics, thresholds)
  ESCALATE_TO: MODEL_GOVERNANCE_AGENT + data science team
  RETRY_POLICY: Retrain model with corrected data/parameters and resubmit from Gate 1

GATE_5_FAIL:
  ACTION: BLOCK production promotion
  LOG: LOG_GATE5_FAILURE (model_id, model_version, fairness_report)
  ESCALATE_TO: MODEL_GOVERNANCE_AGENT + COMPLIANCE_ADMIN (human) + data science team
  RETRY_POLICY: Address bias source (training data rebalance or debiasing technique) and rerun from Gate 4

GATE_6_FAIL:
  ACTION: BLOCK inference request — return INFERENCE_BLOCKED response to consumer
  LOG: LOG_GATE6_REJECTION (inference_request_id, model_id, rejection_reason)
  ESCALATE_TO: OBSERVABILITY_AGENT (if rate > 5%); SECURITY_AGENT (if cross-tenant)
  RETRY_POLICY: Consumer agent must request updated features from Feature Store

GATE_7_FAIL (SUPPRESSION):
  ACTION: SUPPRESS inference response — consumer receives RESPONSE_SUPPRESSED event
  LOG: LOG_GATE7_SUPPRESSION (inference_request_id, model_id, suppression_reason)
  ESCALATE_TO: TRUST_SAFETY_AGENT (if content policy); SECURITY_AGENT (if cross-tenant)
  RETRY_POLICY: NONE — re-inference from scratch with refreshed context

DRIFT_FREEZE:
  ACTION: FREEZE model — inference requests routed to fallback (previous stable version)
  LOG: LOG_MODEL_FROZEN (model_id, model_version, drift_type, drift_score)
  ESCALATE_TO: MODEL_GOVERNANCE_AGENT + PLATFORM_DEVOPS (immediate)
  RETRY_POLICY: Full retrain + Gate 1 through Gate 5 required before unfreeze

MDVA_SELF_FAILURE (MDVA agent internal error):
  ACTION: FAIL OPEN for Gates 6, 7 (inference continues with VALIDATION_UNAVAILABLE flag)
         FAIL CLOSED for Gates 1–5 (no promotion without validation)
  LOG: LOG_MDVA_SELF_FAILURE (error_type, gate_type, timestamp)
  ESCALATE_TO: OBSERVABILITY_AGENT + PLATFORM_DEVOPS (immediate, < 30s)
  RETRY_POLICY: Auto-restart with exponential backoff; human escalation if > 3 failures

NO_SILENT_FAILURES:
  EVERY gate failure, rejection, suppression, or freeze MUST produce an audit log entry.
  Swallowing validation errors is FORBIDDEN.
```

---

## 1️⃣9️⃣ INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - FEATURE_STORE_AGENT
  - BEHAVIOR_STREAM_PROCESSOR
  - ASSESSMENT_ENGINE_AGENT
  - PROCTORING_AGENT
  - SKILL_GRAPH_AGENT
  - MODEL_REGISTRY_AGENT
  - MATCHING_ENGINE_AGENT
  - RANKING_ENGINE_AGENT
  - RECOMMENDATION_AGENT
  - CAREER_PATH_AGENT
  - SKILL_GAP_AGENT
  - AI_CAREER_ADVISOR_AGENT
  - OBSERVABILITY_AGENT

DOWNSTREAM_AGENTS:
  - MODEL_REGISTRY_AGENT          # Promotion decisions
  - MODEL_GOVERNANCE_AGENT        # Drift, quality, fairness reports
  - FEATURE_STORE_AGENT           # Feature rejection or quality flags
  - OBSERVABILITY_AGENT           # Validation health metrics
  - AUDIT_LOG_AGENT               # Immutable validation records
  - SECURITY_AGENT                # PII and cross-tenant incidents
  - TRUST_SAFETY_AGENT            # LLM content policy violations
  - COMPLIANCE_ADMIN (human)      # Fairness audit reports

EVENT_TRIGGERS_EMITTED:
  - VALIDATION_PASSED_EVENT         # On any gate PASS
  - VALIDATION_FAILED_EVENT         # On any gate FAIL
  - MODEL_PROMOTED_EVENT            # When full Gate 1–5 pipeline passes
  - MODEL_BLOCKED_EVENT             # When any gate blocks promotion
  - HUMAN_REVIEW_REQUIRED_EVENT     # When WARN requires sign-off
  - DRIFT_DETECTED_EVENT            # On PSI/KS/accuracy drift threshold breach
  - MODEL_FROZEN_EVENT              # On severe drift or accuracy decay
  - INFERENCE_BLOCKED_EVENT         # Gate 6 failure
  - RESPONSE_SUPPRESSED_EVENT       # Gate 7 suppression
  - SECURITY_INCIDENT_EVENT         # PII or cross-tenant detection
  - CONTENT_POLICY_VIOLATION_EVENT  # LLM output policy breach
  - PRODUCER_DEGRADED_ALERT         # Gate 2 producer circuit breaker
  - MDVA_HEALTH_HEARTBEAT           # Every 30s to OBSERVABILITY_AGENT
```

---

## 2️⃣0️⃣ PERFORMANCE MONITORING

```yaml
METRICS_EMITTED_TO_OBSERVABILITY_AGENT:
  - mdva_gate1_runs_total                  (counter, per model_type, per tenant)
  - mdva_gate1_pass_rate                   (gauge)
  - mdva_gate2_feature_rejection_rate      (gauge, per producer, per feature_name)
  - mdva_gate2_pii_detections_total        (counter — MUST alert at > 0)
  - mdva_gate3_block_rate                  (gauge)
  - mdva_gate4_performance_metrics         (gauge, per model_id — primary metric)
  - mdva_gate5_fairness_flags_total        (counter, per group_type)
  - mdva_gate6_inference_block_rate        (gauge, per model_id)
  - mdva_gate7_suppression_rate            (gauge, per model_id)
  - mdva_drift_psi_score                   (gauge, per feature_name, per model_id)
  - mdva_model_frozen_total                (counter — alert at > 0)
  - mdva_validation_latency_ms_gate6       (histogram P99 — must be < 5ms)
  - mdva_validation_latency_ms_gate7       (histogram P99 — must be < 5ms)
  - mdva_security_incidents_total          (counter — MUST alert at > 0)
  - mdva_human_review_pending_count        (gauge — SLA: < 24hrs resolution)
  - mdva_llm_content_violations_total      (counter)
  - mdva_self_error_rate                   (gauge — > 0.1% triggers CRITICAL alert)

ALERT_THRESHOLDS:
  - Gate 2 PII detection > 0                    → CRITICAL → SECURITY_AGENT
  - Gate 6 block rate > 5%                      → WARN → MODEL_GOVERNANCE_AGENT
  - Gate 7 suppression rate > 2%                → WARN → MODEL_GOVERNANCE_AGENT
  - Model frozen count > 0                       → CRITICAL → PLATFORM_DEVOPS
  - Drift PSI > 0.20 for any production model   → CRITICAL → MODEL_GOVERNANCE_AGENT
  - Human review pending > 24hrs                 → ESCALATE to PLATFORM_SUPER_ADMIN
  - MDVA Gate 6/7 latency P99 > 10ms            → CRITICAL (budget is 5ms) → PLATFORM_DEVOPS
  - Security incidents > 0                       → CRITICAL → SECURITY_AGENT + CISO
  - LLM content violations > 0                  → CRITICAL → TRUST_SAFETY_AGENT
```

---

## 2️⃣1️⃣ VERSIONING POLICY

```yaml
CURRENT_VERSION:    MDVA-AG-v1.0.0
MUTATION_POLICY:    ADD-ONLY with critical restriction:
                    THRESHOLDS MAY ONLY BE TIGHTENED (made stricter)
                    THRESHOLDS MAY NEVER BE RELAXED without PLATFORM_SUPER_ADMIN + COMPLIANCE_ADMIN dual approval

CHANGE_RULES:
  - New validation check in any gate: ADD + bump minor version + migration doc
  - Threshold tightening: ADD new threshold declaration + bump minor version
  - Threshold relaxation: REQUIRES dual human approval + major version bump + audit justification
  - New gate addition: Major version bump + full system validation + human approval
  - New drift detection method: ADD + bump minor version
  - New PII pattern: ADD + bump minor version (security-critical, expedited process)
  - New fairness group: ADD + bump minor version + fairness re-audit of all production models

BACKWARD_COMPATIBILITY:
  - Validation reports from prior MDVA versions remain valid in audit log
  - Gates are versioned — model artifacts carry the MDVA version under which they were validated
  - Re-validation under newer MDVA version is advisory unless triggered by governance policy

ROLLBACK_POLICY:
  - MDVA threshold rollback: FORBIDDEN without explicit dual human approval
  - MDVA code rollback: Incident log + PLATFORM_SUPER_ADMIN approval required
  - Models validated under rolled-back MDVA version: must be re-validated under current version before next promotion

MIGRATION_DOCUMENTATION:
  - REQUIRED for every version bump
  - Must include: which checks changed, threshold delta, affected model types,
    existing production models requiring re-validation, rollback path
```

---

## 2️⃣2️⃣ NON-NEGOTIABLE RULES

```
THIS AGENT MUST NOT:

✗ Allow model promotion to PRODUCTION without all 5 promotion gates passing
✗ Allow model training to begin without Gate 1 PASS
✗ Allow inference to proceed when Gate 6 fails
✗ Allow inference response delivery when Gate 7 suppresses
✗ Relax any declared threshold without dual human approval + version bump
✗ Override a FAIL decision based on contextual reasoning or business urgency
✗ Allow training data with PII contamination — reject unconditionally, no exceptions
✗ Allow cross-tenant training data contamination — reject unconditionally
✗ Permit cross-tenant entity references in any inference output
✗ Allow LLM output with toxicity > 0.01 to be delivered
✗ Retroactively change a validation_run status from FAIL to PASS in the database
✗ Allow frozen models to serve live inference without human unfreeze approval
✗ Skip the fairness gate for any model promotion regardless of urgency
✗ Produce silent validation results — every run must emit a structured report
✗ Allow AI component to override statistical gate decisions
✗ Deploy MDVA itself to PRODUCTION without passing its own Gate 3 and Gate 4
✗ Log validation results outside the designated append-only audit schema
✗ Accept validation requests from unregistered requestor agents
```

---

## 🔐 FINAL SEAL

```
DOCUMENT:           MODEL_DATA_VALIDATION_AGENT.md
AGENT_ID:           MDVA-AG-001
VERSION:            MDVA-AG-v1.0.0
PLATFORM:           ECOSKILLER ANTIGRAVITY
SEALED_AT:          v1.0.0 — LOCKED
INTERPRETATION:     NONE PERMITTED
OVERRIDE:           HUMAN DECLARATION + VERSION BUMP ONLY
THRESHOLD_RELAXATION: DUAL HUMAN APPROVAL ONLY (PLATFORM_SUPER_ADMIN + COMPLIANCE_ADMIN)
MUTATION:           ADD-ONLY (thresholds tighten only)

STATUS: ██████████████████████████ SEALED & LOCKED

Any modification without version declaration and human approval
constitutes a governance violation and triggers:
→ STOP_EXECUTION
→ LOG_GOVERNANCE_VIOLATION
→ FREEZE all pending model promotions
→ ESCALATE_TO: PLATFORM_SUPER_ADMIN + COMPLIANCE_ADMIN + SECURITY_AGENT

Any attempt to relax a validation threshold without dual approval:
→ IMMEDIATE GOVERNANCE_INCIDENT
→ AUDIT_TRAIL_ENTRY (permanent)
→ ESCALATE_TO: CISO + COMPLIANCE_ADMIN
```

---

*ECOSKILLER ANTIGRAVITY — INTELLIGENCE & INNOVATION CORE*
*MDVA-AG-v1.0.0 | Seven-Gate ML Governance | Zero-trust | Deterministic | Append-only*
