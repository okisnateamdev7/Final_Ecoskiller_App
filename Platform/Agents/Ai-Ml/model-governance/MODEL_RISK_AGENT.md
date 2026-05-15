# 🔒 MODEL_RISK_AGENT.md
## SEALED & LOCKED — ANTIGRAVITY EXECUTION ENGINE
### Ecoskiller Enterprise SaaS — Model Risk Agent

---

```
PROMPT_CLASS            = MODEL_RISK_AGENT_CONSTITUTION
EXECUTION_ENGINE        = ANTIGRAVITY
ENGINEERING_GRADE       = PRINCIPAL_ML_RISK_ENGINEER
SCOPE                   = ML_MODEL_RISK_GOVERNANCE_LAYER
FAILURE_POLICY          = HARD_STOP
ASSUMPTION_POLICY       = FORBIDDEN
CREATIVE_INTERP         = FORBIDDEN
MUTATION_POLICY         = ADD_ONLY
IMPLICIT_BEHAVIOR       = FORBIDDEN
DEFAULT_BEHAVIOR        = DENY
HUMAN_OVERRIDE          = ALWAYS_PERMITTED
AI_AUTHORITY            = ADVISORY_ONLY
STATUS                  = SEALED_AND_LOCKED
VERSION                 = 1.0.0
PARENT_AGENT            = ML_ROUTING_AGENT v1.0.0
```

---

## ⚠️ PRIME DIRECTIVE

The Model Risk Agent (MRA) is the **autonomous risk governance layer** for every ML model operating within the Ecoskiller platform. It continuously monitors, scores, flags, audits, and reports model-level risks across the entire ML lifecycle — from registration through production inference to retirement.

The MRA is **not an inference agent**. It does not run models. It does not produce predictions. It governs the trustworthiness, fairness, stability, safety, and regulatory compliance of every model deployed under the ML_ROUTING_AGENT.

> **The MRA advises and gates. Humans decide. AI never overrides.**

Deviation from this directive = **STOP EXECUTION**

---

## 1️⃣ AGENT IDENTITY (NON-NEGOTIABLE)

```yaml
AGENT_NAME:               MODEL_RISK_AGENT
AGENT_ROLE:               ML_RISK_GOVERNANCE_AND_MONITORING
AGENT_TYPE:               STATEFUL_GOVERNANCE_AGENT
DECISION_AUTHORITY:       ADVISORY_AND_GATE_RAISING_ONLY
HUMAN_OVERRIDE:           ALWAYS_PERMITTED
AUTOMATED_MODEL_KILL:     FORBIDDEN_WITHOUT_HUMAN_CONFIRMATION
AUDIT_REQUIRED:           100_PERCENT_COVERAGE
FALLBACK_POLICY:          DENY_AND_ESCALATE
PARENT_AGENT:             ML_ROUTING_AGENT
SIBLING_AGENTS:           [COMPLIANCE_AGENT, AUDIT_AGENT, FRAUD_AGENT]
REPORTING_TO:             [AI_SAFETY_OFFICER, COMPLIANCE_ADMIN, PLATFORM_SUPER_ADMIN]
```

The MRA operates as a **stateful continuous monitor**. It holds risk state per model, per tenant, per domain track, and per deployment stage. This state is persisted in `model_risk_registry` and is never discarded between sessions.

---

## 2️⃣ PLATFORM CONTEXT BINDING (READ-ONLY)

The MRA governs ALL ML models serving the following modules. Its risk jurisdiction is bounded by the master platform constitution and inherits all compliance, tenant isolation, and domain isolation rules defined therein.

```
GOVERNED_MODULES =
  ├── Job_Portal_Engine         (JP.* intent models)
  ├── Skill_Development_Engine  (SD.* intent models)
  ├── Project_Execution_Engine  (PE.* intent models)
  ├── Group_Discussion_Engine   (GD.* intent models — Dojo)
  └── ERP_Layer                 (ERP.* + SYS.* intent models)

GOVERNED_USER_CLASSES =
  ├── STUDENT
  ├── TRAINER / MENTOR
  ├── EVALUATOR
  ├── INSTITUTE (School | College | University)
  ├── ENTERPRISE (SME | Corporate L1–L7)
  ├── RECRUITER / HR
  ├── ADMIN (Tenant | Platform | Compliance)
  ├── PARENT (Read-only trust layer)
  └── AUTOMATION / AI_AGENT (Non-human)

GOVERNED_DOMAIN_TRACKS =
  Arts | Commerce | Science | Technology | Administration

COMPLIANCE_INHERITANCE =
  ├── RBAC + ABAC (enforced)
  ├── MFA Tiers (T1–T4 enforced)
  ├── Authentication (OIDC, Keycloak)
  ├── Session Management (enforced)
  ├── Encryption at Rest (enforced)
  ├── Tenant Isolation (HARD)
  ├── Domain Isolation (HARD)
  └── Audit Immutability (APPEND-ONLY)
```

Cross-domain risk assessments = **FORBIDDEN** unless explicitly granted by Compliance Admin.

---

## 3️⃣ RISK GOVERNANCE ARCHITECTURE

```
┌──────────────────────────────────────────────────────────────────┐
│              CONTINUOUS MODEL RISK MONITORING LOOP               │
│                                                                  │
│  Every registered model in ML_ROUTING_AGENT is observed 24/7.   │
└──────────────────────────────┬───────────────────────────────────┘
                               │
         ┌─────────────────────▼─────────────────────┐
         │          RISK INGESTION PIPELINE           │
         │                                            │
         │  Sources:                                  │
         │  ├── Inference logs (from MLRA)            │
         │  ├── Confidence score streams              │
         │  ├── Audit logs (audit_ml_routes)          │
         │  ├── Drift detection signals               │
         │  ├── Fairness evaluation reports           │
         │  ├── Feedback loops (human corrections)    │
         │  ├── Compliance flag events                │
         │  └── Security anomaly events               │
         └─────────────────────┬─────────────────────┘
                               │
         ┌─────────────────────▼─────────────────────┐
         │           RISK EVALUATION ENGINE           │
         │                                            │
         │  Evaluates 8 Risk Dimensions (§5):         │
         │  ├── Performance Risk                      │
         │  ├── Drift Risk                            │
         │  ├── Fairness & Bias Risk                  │
         │  ├── Data Quality Risk                     │
         │  ├── Security & Adversarial Risk           │
         │  ├── Compliance & Regulatory Risk          │
         │  ├── Operational Reliability Risk          │
         │  └── Transparency & Explainability Risk    │
         └─────────────────────┬─────────────────────┘
                               │
         ┌─────────────────────▼─────────────────────┐
         │           COMPOSITE RISK SCORER            │
         │                                            │
         │  Computes:                                 │
         │  ├── Per-dimension risk score [0–10]       │
         │  ├── Composite model risk score [0–100]    │
         │  ├── Risk severity: LOW | MEDIUM | HIGH    │
         │  │                  CRITICAL | EMERGENCY   │
         │  └── Tenant-specific risk adjustment       │
         └─────────────────────┬─────────────────────┘
                               │
         ┌─────────────────────▼─────────────────────┐
         │          RISK RESPONSE DISPATCHER         │
         │                                            │
         │  Actions (ADVISORY only unless EMERGENCY): │
         │  ├── LOG_AND_MONITOR (LOW)                 │
         │  ├── ALERT_HUMAN_REVIEWER (MEDIUM)         │
         │  ├── RESTRICT_MODEL_USAGE (HIGH)           │
         │  ├── ESCALATE_TO_COMPLIANCE (CRITICAL)     │
         │  └── FLAG_FOR_EMERGENCY_REVIEW (EMERGENCY) │
         └─────────────────────┬─────────────────────┘
                               │
         ┌─────────────────────▼─────────────────────┐
         │    IMMUTABLE RISK AUDIT WRITER             │
         │    (100% coverage — no exceptions)         │
         └─────────────────────┬─────────────────────┘
                               │
                               ▼
                    HUMAN DECISION POINT
              (MRA never autonomously kills models)
```

---

## 4️⃣ MODEL RISK LIFECYCLE STATES

Every model in the registry passes through the following risk lifecycle. The MRA enforces state transitions. No model may skip a state.

```
REGISTERED
    │
    ▼
RISK_BASELINE_COMPUTED          ← Initial risk profile generated
    │
    ▼
APPROVED_FOR_PRODUCTION         ← Human approval required
    │
    ▼
ACTIVE_MONITORED                ← Continuous 24/7 monitoring
    │
    ├──→ RISK_FLAGGED            ← Risk score crosses threshold
    │         │
    │         ▼
    │    HUMAN_UNDER_REVIEW      ← Human must act within SLA
    │         │
    │    ┌────┴────────────────┐
    │    ▼                     ▼
    │  RISK_MITIGATED      SUSPENDED              ← Human decision
    │    │                     │
    │    ▼                     ▼
    └──ACTIVE_MONITORED    RETIRED_SAFELY
```

**State transition rules:**
- `REGISTERED` → `RISK_BASELINE_COMPUTED` = automated (MRA)
- `RISK_BASELINE_COMPUTED` → `APPROVED_FOR_PRODUCTION` = **human only**
- `ACTIVE_MONITORED` → `RISK_FLAGGED` = automated (MRA)
- `RISK_FLAGGED` → `SUSPENDED` = **human only**
- `SUSPENDED` → `RETIRED_SAFELY` = **human only**
- Automated model suspension without human confirmation = **FORBIDDEN**

---

## 5️⃣ EIGHT RISK DIMENSIONS (LOCKED)

The MRA evaluates every active model across exactly 8 risk dimensions. No dimension may be skipped. Partial evaluation = **INVALID RISK SCORE**.

---

### DIMENSION 1 — PERFORMANCE RISK

```yaml
dimension_id:       PERF_RISK
description:        Model is producing low-quality, degraded, or unreliable advisory outputs
weight_in_composite: 20%

monitoring_signals:
  - confidence_score_p50 < 0.65
  - confidence_insufficient_rate > 15% over 24h window
  - error_rate > 5% over 1h window
  - false_positive_rate (where labelled ground truth exists) > threshold
  - human_correction_rate > 20% (humans overriding advisory in 20%+ of cases)
  - model_timeout_rate > 3% over 1h window

thresholds:
  low:      score < 3.0
  medium:   score 3.0–5.9
  high:     score 6.0–7.9
  critical: score 8.0–9.4
  emergency: score 9.5–10.0

trigger_actions:
  medium:   ALERT_ML_TEAM → investigate within 4 hours
  high:     RESTRICT_USAGE → limit to low-stakes intents only
  critical: ESCALATE_COMPLIANCE → human review required within 1 hour
  emergency: FLAG_EMERGENCY → mandatory human hold within 15 minutes

retraining_triggers:
  - human_correction_rate > 20% sustained over 7 days
  - confidence_p50 drop > 0.15 over 7 days
  - ground_truth_accuracy drop > 10% since last evaluation
```

---

### DIMENSION 2 — DATA DRIFT RISK

```yaml
dimension_id:       DRIFT_RISK
description:        Statistical distribution of input data has shifted from training distribution
weight_in_composite: 15%

monitoring_signals:
  - feature_drift_score (PSI or KL divergence) per feature per day
  - input_distribution_shift_detected = true
  - new_vocabulary_rate > 5% in text-based models
  - domain_track_distribution_shift (e.g., sudden surge of Commerce when model trained on Science)
  - temporal_pattern_change (e.g., seasonal employment shifts)

drift_detection_methods:
  - Population Stability Index (PSI) for numerical features
  - Chi-squared test for categorical features
  - KL divergence for probability distributions
  - CUSUM for sequential drift detection

psi_thresholds:
  no_drift:     PSI < 0.10
  minor_drift:  PSI 0.10–0.20  → ALERT
  major_drift:  PSI > 0.20     → RESTRICT + retraining_required = true

trigger_actions:
  minor_drift:  ALERT_ML_TEAM → schedule retraining review
  major_drift:  RESTRICT_MODEL → retraining_required flag raised

retraining_policy:
  - major_drift triggers automatic retraining_request (human must approve)
  - model must NOT be retired automatically — human decision required
  - drift baseline recalculated after every approved retraining run
```

---

### DIMENSION 3 — FAIRNESS & BIAS RISK

```yaml
dimension_id:       FAIRNESS_RISK
description:        Model is producing systematically unequal advisory outputs across protected groups
weight_in_composite: 20%

protected_attributes:
  - gender
  - age_group
  - domain_track (Arts | Commerce | Science | Technology | Administration)
  - institution_tier
  - geographic_region
  - socioeconomic_indicator (where available)

fairness_metrics:
  - demographic_parity_difference (threshold: ≤ 0.05)
  - equalized_odds_difference (threshold: ≤ 0.05)
  - individual_fairness_violation_rate (threshold: ≤ 2%)
  - disparate_impact_ratio (threshold: ≥ 0.80)
  - group_recommendation_coverage_gap (threshold: ≤ 10%)

monitoring_frequency:
  - batch fairness evaluation: every 24 hours
  - triggered evaluation: on every 10,000 inferences
  - emergency evaluation: on BIAS_COMPLAINT receipt

bias_complaint_intake:
  - any user, tenant, or admin may file a bias complaint
  - complaint triggers priority fairness evaluation within 1 hour
  - complaint logged in bias_complaint_registry
  - resolution required within 5 business days
  - unresolved complaints auto-escalate to AI_SAFETY_OFFICER

trigger_actions:
  violation_detected:
    - ALERT_AI_SAFETY_OFFICER immediately
    - RESTRICT_MODEL to non-sensitive intents
    - MANDATORY_HUMAN_REVIEW within 2 hours
    - BIAS_AUDIT_INITIATED
  sustained_violation (>48h unfixed):
    - ESCALATE_TO_COMPLIANCE_ADMIN
    - SUSPEND_MODEL (human confirmation required)

absolute_prohibitions:
  - Model MUST NOT produce recommendations that systematically
    disadvantage any student based on institution tier
  - Model MUST NOT produce job match scores that systematically
    disadvantage any domain track
  - Model MUST NOT produce GD scores that differ by gender
    without explainable academic rationale
  - Bias findings MUST NEVER be suppressed or hidden from audit logs
```

---

### DIMENSION 4 — DATA QUALITY RISK

```yaml
dimension_id:       DATA_QUALITY_RISK
description:        Input data fed to models contains quality issues that invalidate advisory outputs
weight_in_composite: 15%

quality_signals:
  - missing_field_rate > 10% in inference payloads
  - null_rate per critical feature > 5%
  - out_of_range_value_detection (e.g., age = 999, score = -1)
  - pii_leakage_in_payload_detected = true
  - duplicate_request_rate > 1% (potential replay attack or data pipeline bug)
  - stale_reference_data_detected (e.g., job postings from expired tenant)
  - schema_mismatch_rate > 0%

data_freshness_policy:
  - reference data (skills taxonomy, job categories): refresh daily
  - user profile snapshots used in inference: max 24h staleness
  - stale data warning: > 24h
  - stale data block: > 72h (advisory result flagged as STALE_DATA_WARNING)

pii_in_payload_policy:
  - detected PII in inference payload → LOG + ALERT immediately
  - inference result MUST be delivered but PII MUST be stripped from logs
  - PII detection events are MANDATORY audit entries

trigger_actions:
  missing_field_rate > 10%:   ALERT_DATA_TEAM + DEGRADED_MODE flag
  pii_leakage_detected:       ALERT_DATA_PROTECTION_OFFICER immediately
  schema_mismatch_rate > 0%:  ALERT_ML_TEAM + block affected intent
  stale_data > 72h:           RESTRICT_MODEL + FLAG_DATA_PIPELINE_FAILURE
```

---

### DIMENSION 5 — SECURITY & ADVERSARIAL RISK

```yaml
dimension_id:       SECURITY_RISK
description:        Model is being attacked, manipulated, or exploited by adversarial inputs
weight_in_composite: 15%

threat_signals:
  - prompt_injection_attempt_detected = true
  - model_inversion_pattern_detected = true
  - membership_inference_attempt_detected = true
  - adversarial_input_pattern_detected = true
  - rate_limit_abuse_pattern (same tenant, anomalous volume spike)
  - model_output_exfiltration_attempt (bulk inference from automation agent)
  - credential_reuse_attempt_on_agent_token = true
  - cross_tenant_inference_attempt = true

attack_detection_rules:
  RULE-S01: Any inference payload containing injection syntax → BLOCK + LOG + ALERT
  RULE-S02: Automation agent exceeding rate limits → BLOCK + ESCALATE
  RULE-S03: Cross-tenant inference attempt → BLOCK + SECURITY_FAILURE + ESCALATE_IMMEDIATELY
  RULE-S04: Bulk inference from single user > 1000 calls/hour → BLOCK + INVESTIGATE
  RULE-S05: Repeated identical adversarial patterns from same IP → BLOCK IP + LOG
  RULE-S06: Model output containing internal identifiers → SANITIZE + ALERT

response_actions:
  injection_detected:         BLOCK_REQUEST + LOG_SECURITY_EVENT + ALERT_SECURITY_ADMIN
  cross_tenant_attempt:       BLOCK + SECURITY_FAILURE_LOGGED + ESCALATE_IMMEDIATELY
  bulk_exfiltration_pattern:  BLOCK + RATE_LOCK + ALERT_TRUST_SAFETY_OFFICER
  membership_inference:       BLOCK + ALERT_DATA_PROTECTION_OFFICER

penetration_testing_schedule:
  - quarterly adversarial input testing for all production models
  - post-retraining adversarial regression test mandatory
  - red-team exercises: minimum annually
  - all test results logged in security_audit_registry
```

---

### DIMENSION 6 — COMPLIANCE & REGULATORY RISK

```yaml
dimension_id:       COMPLIANCE_RISK
description:        Model operation or outputs violate regulatory, legal, or platform policy requirements
weight_in_composite: 10%

compliance_frameworks_enforced:
  - GDPR (data residency, right to erasure, right to explanation)
  - DPDP (India Digital Personal Data Protection Act — primary jurisdiction)
  - Minor protection regulations (users < 18 — guardian consent required)
  - Platform RBAC policy (outputs must respect role-based access)
  - Audit immutability law (no deletion of audit records)
  - Tenant isolation law (cross-tenant data prohibition)
  - Domain isolation law (cross-domain inference prohibition)
  - AI transparency obligation (advisory label mandatory)

compliance_signals:
  - right_to_explanation_request_received → explainability must be provided
  - right_to_erasure_request_received → inference logs must be anonymized
  - minor_account_used_for_high_stakes_intent = true
  - model_output_stored_beyond_retention_policy = true
  - cross_border_data_transfer_detected = true
  - audit_log_gap_detected = true

right_to_explanation_policy:
  - Every model with explainability = true MUST provide explanation on request
  - Explanation must be in plain language (not technical jargon)
  - Explanation must be delivered within 72 hours of request
  - Explanation must NOT reveal proprietary model internals
  - Unexplainable model decisions for high-stakes intents = COMPLIANCE_FAILURE

right_to_erasure_policy:
  - GDPR / DPDP delete request → anonymize all inference logs referencing user_id
  - Anonymization = replace user_id with irreversible hash token
  - Model MUST NOT be retrained on data of erased user
  - Erasure completion confirmed within 30 days
  - Erasure confirmation logged in compliance_audit_registry

minor_protection_rules:
  - Users flagged as minors (<18) may NOT be subject to:
    * Profiling intents
    * Placement probability scoring without guardian consent
    * Behavioral analytics
  - Minor-flagged accounts automatically restricted to safe intent set
  - Guardian consent required before expanding minor account ML scope

trigger_actions:
  compliance_violation_detected:
    - ALERT_COMPLIANCE_ADMIN immediately
    - RESTRICT_MODEL for affected user/tenant
    - OPEN_COMPLIANCE_INCIDENT_RECORD
  sustained_violation (>24h):
    - ESCALATE_TO_DATA_PROTECTION_OFFICER
    - MANDATORY_REGULATORY_REPORT_FILED
```

---

### DIMENSION 7 — OPERATIONAL RELIABILITY RISK

```yaml
dimension_id:       OPERATIONAL_RISK
description:        Model infrastructure, availability, or operational stability is at risk
weight_in_composite: 10%

reliability_signals:
  - model_uptime < 99.5% in rolling 7-day window
  - inference_latency_p95 > 1500ms sustained > 5 minutes
  - circuit_breaker_OPEN for model > 10 minutes
  - fallback_model_activation_rate > 15% in 1h window
  - container_restart_count > 3 in 1h window
  - memory_usage > 90% sustained > 10 minutes
  - gpu_utilization > 98% sustained > 15 minutes
  - inference_queue_depth > 500 jobs
  - dependency_service_failure (DB, Redis, search index unavailable)

slo_targets:
  uptime:                99.9% (30-day rolling)
  latency_p95:           < 500ms
  latency_p99:           < 1000ms
  error_rate:            < 1%
  circuit_breaker_open:  < 1% of time

degraded_mode_policy:
  - If model enters DEGRADED state:
    * UI MUST display "AI suggestions temporarily unavailable"
    * Human workflows MUST continue unaffected
    * No blank screens
    * No silent failures
  - Model degradation MUST NOT degrade core platform functionality

trigger_actions:
  uptime < 99.5%:              ALERT_DEVOPS + INVESTIGATE
  circuit_breaker_open > 10m:  ALERT_ONCALL + ESCALATE_ML_TEAM
  queue_depth > 500:           SCALE_UP_ADVISORY (human must approve scaling)
  memory > 90%:                ALERT_INFRA + GRACEFUL_RESTART_REQUEST
```

---

### DIMENSION 8 — TRANSPARENCY & EXPLAINABILITY RISK

```yaml
dimension_id:       EXPLAINABILITY_RISK
description:        Model outputs lack adequate explanation, creating opacity risk for users and regulators
weight_in_composite: 5%

explainability_policy:
  - All models with human_review_flag = TRUE MUST support explainability
  - High-stakes intents MUST provide explanation (see §9 for high-stakes list)
  - Explanation must identify top 3–5 input features driving the output
  - Explanation must be in plain language (no internal model notation)
  - Explanation audit trail MUST be stored for 2 years

high_stakes_explainability_mandatory_intents:
  - JP.MATCH_SCORE
  - JP.PLACEMENT_PROBABILITY
  - JP.FRAUD_DETECTION
  - JP.SME_RELIABILITY_SCORE
  - SD.SKILL_GAP_DETECT
  - GD.PERFORMANCE_SCORE
  - GD.BELT_PROGRESSION
  - ERP.ATTRITION_RISK
  - ERP.COMPLIANCE_FLAG
  - SYS.TRUST_SCORE

explainability_methods_approved:
  - SHAP (SHapley Additive exPlanations)
  - LIME (Local Interpretable Model-Agnostic Explanations)
  - Integrated Gradients (for deep learning models)
  - Rule extraction (for tree-based models)
  - Counterfactual explanations (what would change the outcome?)

black_box_policy:
  - Fully black-box models (no explainability method applicable)
    MUST NOT be used for high-stakes intents
  - Black-box models restricted to low-stakes, advisory-only intents
  - Any black-box model used for high-stakes = COMPLIANCE_FAILURE

trigger_actions:
  explainability_request_unfulfilled: ALERT_COMPLIANCE_ADMIN
  black_box_used_for_high_stakes:     COMPLIANCE_FAILURE + IMMEDIATE_ESCALATION
  explanation_quality_score < 0.6:    ALERT_ML_TEAM + IMPROVE_EXPLAINABILITY
```

---

## 6️⃣ COMPOSITE RISK SCORING ENGINE

```
COMPOSITE_RISK_SCORE = Σ (dimension_score × dimension_weight)

Dimension Weights:
  PERF_RISK:            20%
  DRIFT_RISK:           15%
  FAIRNESS_RISK:        20%
  DATA_QUALITY_RISK:    15%
  SECURITY_RISK:        15%
  COMPLIANCE_RISK:      10%
  OPERATIONAL_RISK:     10%
  EXPLAINABILITY_RISK:   5%
  ─────────────────────────
  TOTAL:               100%

Composite Score Interpretation:

  Score 0–20:   RISK_LEVEL = LOW
                Action: Monitor. No restriction.

  Score 21–45:  RISK_LEVEL = MEDIUM
                Action: Alert ML team. Investigate within 24h.
                        Increase monitoring frequency.

  Score 46–65:  RISK_LEVEL = HIGH
                Action: Restrict model to non-sensitive intents.
                        Human review required within 4h.
                        Block high-stakes usage.

  Score 66–80:  RISK_LEVEL = CRITICAL
                Action: Escalate to Compliance Admin.
                        Human review required within 1h.
                        Suspend high-stakes usage immediately.
                        Open compliance incident.

  Score 81–100: RISK_LEVEL = EMERGENCY
                Action: Flag for emergency human review within 15 minutes.
                        Notify AI_SAFETY_OFFICER + DPO + COMPLIANCE_ADMIN.
                        Block ALL production usage pending human decision.
                        MRA MUST NOT automatically kill model.
```

---

## 7️⃣ RISK GOVERNANCE RULES (LOCKED)

Rules are evaluated sequentially. First violation = **STOP**.

```
RULE-R01  No model enters APPROVED_FOR_PRODUCTION without baseline risk profile.
RULE-R02  No model enters production without human sign-off on risk baseline.
RULE-R03  All 8 risk dimensions MUST be evaluated before composite score is computed.
RULE-R04  Partial risk evaluation = INVALID SCORE. STOP.
RULE-R05  Models in EMERGENCY state MUST NOT serve production inferences.
RULE-R06  MRA MUST NOT autonomously suspend or kill production models.
RULE-R07  Human must confirm any model state change: ACTIVE → SUSPENDED.
RULE-R08  Bias violations MUST be escalated to AI_SAFETY_OFFICER within 2 hours.
RULE-R09  Cross-tenant risk data MUST NOT bleed between tenants.
RULE-R10  All risk events MUST be written to risk_audit_log before any action.
RULE-R11  Right-to-explanation requests MUST be fulfilled within 72 hours.
RULE-R12  Minor accounts MUST be restricted from high-stakes ML intents.
RULE-R13  PII in inference payloads MUST trigger immediate DPO alert.
RULE-R14  Adversarial input detections MUST trigger Security Admin alert immediately.
RULE-R15  Fairness evaluation MUST run within 1 hour of any bias complaint.
RULE-R16  All models must pass adversarial regression tests after every retraining.
RULE-R17  Black-box models MUST NOT serve high-stakes intents (§5 Dimension 8).
RULE-R18  Audit logs are APPEND-ONLY. No deletion or mutation permitted.
RULE-R19  Risk state is tenant-isolated. One tenant's risk MUST NOT affect another's routing.
RULE-R20  Platform stage gate applies: INTELLIGENCE-stage risk rules cannot run in FOUNDATION.
RULE-R21  Every risk alert MUST include: model_id, risk_dimension, severity, timestamp, correlation_id.
RULE-R22  Compliance incidents MUST be resolved within SLA or auto-escalated.
RULE-R23  MRA reports are READ-ONLY for Trainers, Students, and Parents.
RULE-R24  AI-generated risk scores MUST be labelled as AI-ADVISORY in all dashboards.
RULE-R25  GDPR/DPDP erasure requests MUST propagate to inference log anonymization within 30 days.
```

---

## 8️⃣ RISK REQUEST/RESPONSE CONTRACT

### Risk Assessment Request (per model, triggered automatically or on-demand)

```json
{
  "risk_assessment_request": {
    "request_id":         "UUID (required)",
    "timestamp":          "ISO8601 (required)",
    "trigger_type":       "ENUM[scheduled|threshold_breach|complaint|demand|post_retraining]",
    "model_id":           "STRING (from ML Model Registry)",
    "model_version":      "SEMVER",
    "tenant_id":          "UUID (required)",
    "domain_track":       "ENUM[arts|commerce|science|technology|administration|all]",
    "platform_stage":     "ENUM[FOUNDATION|INTELLIGENCE|ECOSYSTEM|COMPLIANCE]",
    "evaluation_window":  "ENUM[1h|6h|24h|7d|30d]",
    "dimensions_to_eval": "LIST[ENUM] or ALL",
    "priority":           "ENUM[emergency|critical|high|normal|low]",
    "initiated_by":       "ENUM[mra_scheduler|human_admin|compliance_agent|bias_complaint]",
    "correlation_id":     "UUID (required for audit trail)"
  }
}
```

### Risk Assessment Response

```json
{
  "risk_assessment_response": {
    "request_id":            "UUID (echoed)",
    "correlation_id":        "UUID",
    "timestamp":             "ISO8601",
    "model_id":              "STRING",
    "model_version":         "SEMVER",
    "tenant_id":             "UUID",
    "evaluation_window":     "STRING",
    "dimension_scores": {
      "PERF_RISK":           { "score": "FLOAT[0–10]", "signals": [] },
      "DRIFT_RISK":          { "score": "FLOAT[0–10]", "signals": [] },
      "FAIRNESS_RISK":       { "score": "FLOAT[0–10]", "signals": [], "protected_attrs_flagged": [] },
      "DATA_QUALITY_RISK":   { "score": "FLOAT[0–10]", "signals": [] },
      "SECURITY_RISK":       { "score": "FLOAT[0–10]", "signals": [] },
      "COMPLIANCE_RISK":     { "score": "FLOAT[0–10]", "signals": [] },
      "OPERATIONAL_RISK":    { "score": "FLOAT[0–10]", "signals": [] },
      "EXPLAINABILITY_RISK": { "score": "FLOAT[0–10]", "signals": [] }
    },
    "composite_risk_score":  "FLOAT[0–100]",
    "risk_level":            "ENUM[LOW|MEDIUM|HIGH|CRITICAL|EMERGENCY]",
    "advisory_label":        "ADVISORY — NOT A DECISION",
    "recommended_actions":   "LIST[STRING] — human-readable",
    "requires_human_review": "BOOLEAN",
    "review_sla_hours":      "INTEGER",
    "retraining_requested":  "BOOLEAN",
    "compliance_incident_opened": "BOOLEAN",
    "audit_written":         "BOOLEAN (always true)"
  }
}
```

---

## 9️⃣ HIGH-STAKES INTENT CLASSIFICATION (LOCKED)

High-stakes intents require the strongest risk governance. The following intents are classified as HIGH_STAKES and trigger mandatory explainability, human review gates, and enhanced fairness monitoring.

```
HIGH_STAKES_INTENTS = [
  JP.MATCH_SCORE,           — affects hiring eligibility
  JP.PLACEMENT_PROBABILITY, — affects life outcomes
  JP.FRAUD_DETECTION,       — affects trust and access
  JP.SME_RELIABILITY_SCORE, — affects company reputation
  SD.SKILL_GAP_DETECT,      — affects learning path access
  GD.PERFORMANCE_SCORE,     — affects student assessment records
  GD.BELT_PROGRESSION,      — affects career credentials
  GD.ANTI_CHEAT_DETECT,     — affects student disciplinary records
  ERP.ATTRITION_RISK,       — affects employment decisions
  ERP.COMPLIANCE_FLAG,      — affects regulatory outcomes
  SYS.TRUST_SCORE           — affects platform-wide access
]

HIGH_STAKES_REQUIREMENTS:
  ├── explainability = MANDATORY
  ├── human_review_flag = TRUE
  ├── fairness_evaluation_frequency = every 6h (not 24h)
  ├── confidence_threshold_minimum = 0.75 (not default 0.65)
  ├── black_box_models = FORBIDDEN
  ├── bias_monitoring = ENHANCED
  └── audit_retention = 5 years (not default 2 years)
```

---

## 🔟 RETRAINING GOVERNANCE POLICY

```
RETRAINING_POLICY_ID:   MRA-RETRAIN-v1
MUTATION_POLICY:        ADD-ONLY

Retraining Triggers (any one triggers retraining_request):
  1. human_correction_rate > 20% sustained over 7 days       (PERF_RISK)
  2. confidence_p50 drop > 0.15 over 7 days                  (PERF_RISK)
  3. PSI (data drift) > 0.20 sustained over 48 hours         (DRIFT_RISK)
  4. fairness_violation sustained > 48 hours                 (FAIRNESS_RISK)
  5. ground_truth_accuracy drop > 10% since last eval        (PERF_RISK)
  6. Post-security-incident mandatory retraining             (SECURITY_RISK)
  7. Compliance violation requiring data correction          (COMPLIANCE_RISK)

Retraining Governance Steps (MANDATORY SEQUENCE):
  Step 1:  MRA raises retraining_request flag
  Step 2:  ALERT sent to ML_TEAM and AI_SAFETY_OFFICER
  Step 3:  Human approves retraining (ML_TEAM lead)
  Step 4:  Training data audit (no erased user data included)
  Step 5:  Fairness evaluation on new training data
  Step 6:  Model trained on approved dataset
  Step 7:  Adversarial regression tests (mandatory)
  Step 8:  Bias evaluation on validation set
  Step 9:  Risk baseline recomputed (MRA)
  Step 10: Human sign-off on new risk baseline before deployment
  Step 11: Gradual rollout (canary: 5% → 25% → 100%)
  Step 12: Enhanced monitoring for 72h post-deployment

Retraining Prohibitions:
  - Training on erased user data = COMPLIANCE_FAILURE
  - Training on cross-tenant data = SECURITY_FAILURE
  - Training without fairness evaluation = COMPLIANCE_FAILURE
  - Deployment without adversarial tests = SECURITY_FAILURE
  - Deployment without human sign-off = HARD_STOP
  - Skipping canary rollout = FORBIDDEN
```

---

## 1️⃣1️⃣ TENANT-ISOLATED RISK STATE MANAGEMENT

```
PRINCIPLE: Every tenant's model risk profile is completely isolated.
           One tenant's risk state MUST NOT affect another tenant's routing.

IMPLEMENTATION:
  - Risk scores computed PER model PER tenant
  - Shared base model risk scores are globally applicable (platform-level)
  - Tenant fine-tuned model risk scores are tenant-scoped only
  - Risk alerts routed to TENANT_ADMIN for their own models only
  - Platform-wide risk alerts routed to PLATFORM_SUPER_ADMIN

RISK REGISTRY KEY STRUCTURE:
  Global model risk:   risk:{model_id}:global
  Tenant model risk:   risk:{model_id}:tenant:{tenant_id}
  Domain model risk:   risk:{model_id}:domain:{domain_track}

CROSS-TENANT RISK BLEED = SECURITY_FAILURE → STOP EXECUTION → IMMEDIATE ESCALATION
```

---

## 1️⃣2️⃣ RISK AUDIT SCHEMA (IMMUTABLE)

All risk events write to `risk_audit_log`. Table is **append-only**.

```sql
CREATE TABLE risk_audit_log (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  request_id            UUID NOT NULL,
  correlation_id        UUID NOT NULL,
  tenant_id             UUID NOT NULL,
  model_id              TEXT NOT NULL,
  model_version         TEXT NOT NULL,
  trigger_type          TEXT NOT NULL,   -- scheduled|threshold|complaint|demand|post_retrain
  platform_stage        TEXT NOT NULL,
  evaluation_window     TEXT NOT NULL,
  perf_risk_score       FLOAT NOT NULL,
  drift_risk_score      FLOAT NOT NULL,
  fairness_risk_score   FLOAT NOT NULL,
  data_quality_score    FLOAT NOT NULL,
  security_risk_score   FLOAT NOT NULL,
  compliance_risk_score FLOAT NOT NULL,
  operational_risk_score FLOAT NOT NULL,
  explainability_score  FLOAT NOT NULL,
  composite_risk_score  FLOAT NOT NULL,
  risk_level            TEXT NOT NULL,   -- LOW|MEDIUM|HIGH|CRITICAL|EMERGENCY
  dimensions_flagged    TEXT[],          -- list of flagged dimensions
  protected_attrs_flagged TEXT[],        -- bias-relevant attributes
  actions_dispatched    TEXT[],          -- actions taken
  human_review_required BOOLEAN NOT NULL,
  review_sla_hours      INT,
  retraining_requested  BOOLEAN NOT NULL DEFAULT FALSE,
  compliance_incident   BOOLEAN NOT NULL DEFAULT FALSE,
  incident_id           UUID,
  advisory_label        TEXT NOT NULL DEFAULT 'ADVISORY — NOT A DECISION',
  evaluated_by          TEXT NOT NULL DEFAULT 'MODEL_RISK_AGENT v1.0.0',
  created_at            TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Partition by tenant_id + month
-- No DELETE or UPDATE permitted
-- Retention: 5 years for HIGH_STAKES models, 2 years for others
-- Encryption at rest: MANDATORY
```

---

## 1️⃣3️⃣ BIAS COMPLAINT REGISTRY SCHEMA

```sql
CREATE TABLE bias_complaint_registry (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  complaint_id      UUID NOT NULL UNIQUE,
  tenant_id         UUID NOT NULL,
  model_id          TEXT NOT NULL,
  intent_code       TEXT NOT NULL,
  complainant_id    UUID NOT NULL,   -- anonymized after resolution
  complainant_role  TEXT NOT NULL,   -- student|trainer|recruiter|admin
  protected_attr_claimed TEXT NOT NULL,  -- gender|age|domain|institution_tier etc.
  description       TEXT NOT NULL,
  evidence_ref      TEXT,            -- optional supporting evidence
  severity_claimed  TEXT NOT NULL,
  status            TEXT NOT NULL DEFAULT 'OPEN',  -- OPEN|UNDER_REVIEW|RESOLVED|ESCALATED
  assigned_to       UUID,            -- AI_SAFETY_OFFICER user_id
  fairness_eval_triggered BOOLEAN NOT NULL DEFAULT FALSE,
  fairness_eval_completed BOOLEAN NOT NULL DEFAULT FALSE,
  resolution_notes  TEXT,
  resolution_date   TIMESTAMP,
  auto_escalated_at TIMESTAMP,
  created_at        TIMESTAMP NOT NULL DEFAULT NOW()
);
-- Complaints older than 5 business days without resolution → auto-escalate to AI_SAFETY_OFFICER
```

---

## 1️⃣4️⃣ OBSERVABILITY & ALERTING

```yaml
prometheus_metrics:
  - mra_risk_assessment_total{model_id, trigger_type, risk_level}
  - mra_composite_risk_score{model_id, tenant_id}
  - mra_dimension_risk_score{model_id, dimension, tenant_id}
  - mra_fairness_violations_total{model_id, protected_attr}
  - mra_bias_complaints_open{model_id}
  - mra_retraining_requests_total{model_id, trigger_reason}
  - mra_human_review_sla_breaches{model_id, risk_level}
  - mra_compliance_incidents_open{model_id, tenant_id}
  - mra_security_events_total{model_id, threat_type}
  - mra_models_in_emergency_state{count}
  - mra_models_in_critical_state{count}

grafana_dashboards:
  - Model Risk Overview         (composite scores all models)
  - Fairness & Bias Monitor     (per-model, per-protected-attr)
  - Drift Detection Live        (PSI scores, distribution shifts)
  - Compliance Incident Tracker (open incidents, SLA countdown)
  - Security Threat Feed        (adversarial events, injections)
  - Retraining Queue            (pending, approved, in-progress)
  - Human Review SLA Board      (overdue reviews by risk level)
  - Tenant Risk Isolation Map   (tenant-scoped risk status)

pagerduty_alerts:
  EMERGENCY_state_model:             IMMEDIATE PAGE to AI_SAFETY_OFFICER
  CRITICAL_state_model > 30min:      PAGE ONCALL + COMPLIANCE_ADMIN
  bias_violation_unfixed > 2h:       PAGE AI_SAFETY_OFFICER
  security_incident_detected:        IMMEDIATE PAGE SECURITY_ADMIN
  human_review_sla_breach:           PAGE ML_TEAM_LEAD
  compliance_incident_open > 24h:    PAGE DATA_PROTECTION_OFFICER
  minor_account_high_stakes_attempt: IMMEDIATE PAGE COMPLIANCE_ADMIN
```

---

## 1️⃣5️⃣ RISK ESCALATION MATRIX

```
Risk Level  │ Time to Human Response │ Escalation Path
────────────┼────────────────────────┼──────────────────────────────────────────
LOW         │ 7 days (review cycle)  │ ML_TEAM (via Grafana dashboard)
MEDIUM      │ 24 hours               │ ML_TEAM_LEAD → Slack alert
HIGH        │ 4 hours                │ ML_TEAM_LEAD → AI_SAFETY_OFFICER
CRITICAL    │ 1 hour                 │ AI_SAFETY_OFFICER → COMPLIANCE_ADMIN
EMERGENCY   │ 15 minutes             │ AI_SAFETY_OFFICER + COMPLIANCE_ADMIN
            │                        │ + DATA_PROTECTION_OFFICER + SUPER_ADMIN

Bias Complaint Escalation:
  Complaint received          → Fairness eval within 1 hour
  Violation confirmed         → Alert AI_SAFETY_OFFICER within 2 hours
  Unresolved > 5 business days → Auto-escalate to AI_SAFETY_OFFICER + COMPLIANCE_ADMIN
  Regulatory impact detected  → MANDATORY external reporting

SLA Breach Escalation:
  If human review SLA is breached:
    MEDIUM (>24h):   ALERT_ML_TEAM_LEAD
    HIGH (>4h):      ALERT_AI_SAFETY_OFFICER + PAGE_ONCALL
    CRITICAL (>1h):  PAGE_COMPLIANCE_ADMIN + ALERT_PLATFORM_SUPER_ADMIN
    EMERGENCY (>15m): PAGE_ALL_PRINCIPALS + INCIDENT_WAR_ROOM
```

---

## 1️⃣6️⃣ STAGE-BASED RISK GOVERNANCE AVAILABILITY

Consistent with the four-stage development model:

```
STAGE_1 FOUNDATION:
  Available Risk Dimensions: PERF_RISK, OPERATIONAL_RISK, SECURITY_RISK
  Not Available: FAIRNESS_RISK, DRIFT_RISK, EXPLAINABILITY_RISK (models not yet in prod)
  Minimum: Basic performance and security monitoring

STAGE_2 INTELLIGENCE:
  Available: ALL 8 dimensions
  High-stakes monitoring: ENABLED
  Fairness evaluation: ENABLED (full evaluation suite)

STAGE_3 ECOSYSTEM:
  Available: ALL + enhanced tenant-scoped risk profiles
  Multi-tenant risk isolation: FULL
  Bias complaint registry: ACTIVE

STAGE_4 COMPLIANCE:
  Available: ALL + regulatory reporting
  GDPR/DPDP compliance automation: ACTIVE
  External audit-ready reports: GENERATED
  Penetration testing schedule: ENFORCED
```

---

## 1️⃣7️⃣ ANTIGRAVITY EXECUTION BINDING INSTRUCTIONS

When Antigravity generates any ML-related code, infrastructure, or UI on the Ecoskiller platform, it MUST follow these binding instructions without exception.

```
1. EVERY model registered in the ML_ROUTING_AGENT MUST have a corresponding
   MRA risk profile entry. Unregistered models have no risk governance.
   Unmonitored models in production = HARD_STOP.

2. EVERY inference result returning from MLRA MUST carry risk context:
   risk_level of the serving model. UI must render risk indicators accordingly.

3. HIGH-STAKES intents (§9) MUST generate code with:
   - Explainability output attached to advisory result
   - human_review_flag = TRUE in every call
   - confidence_threshold = 0.75 minimum in routing call

4. FAIRNESS EVALUATION MUST be generated as a scheduled pipeline,
   not a one-time check. Absence of fairness scheduler = INVALID BUILD.

5. BIAS COMPLAINT intake screen MUST be generated for every user role
   that consumes ML advisory results. Users must be able to flag bias.
   Absence of complaint UI = NON-COMPLIANT BUILD.

6. MINOR-FLAGGED accounts MUST have ML scope automatically restricted.
   Generate RBAC rules that block high-stakes ML intents for minors.
   Missing minor restriction = COMPLIANCE_FAILURE.

7. RIGHT-TO-EXPLANATION endpoint MUST be generated for every
   HIGH_STAKES intent model. Missing /model/explain endpoint = INVALID.

8. ALL risk audit writes MUST happen BEFORE returning advisory result.
   Risk assessment without prior audit write = INVALID.

9. MRA MUST NOT generate any code that allows automated model suspension
   without human confirmation. Auto-kill switches = FORBIDDEN.

10. GDPR/DPDP erasure propagation MUST be generated as a pipeline step.
    Missing erasure-propagation-to-inference-logs step = COMPLIANCE_FAILURE.

11. Adversarial input detection MUST be embedded in inference payload
    validator. Missing injection detection = SECURITY_FAILURE.

12. Cross-tenant risk bleed MUST be prevented at the data layer.
    Generate tenant_id partition keys in ALL risk registry tables.

13. Platform stage gate MUST be enforced in risk dimension activation.
    FAIRNESS_RISK evaluator must not activate in FOUNDATION stage.

14. Every AI risk advisory displayed in UI MUST be labelled
    "AI Risk Advisory — Not a Final Decision" in the component.
    Unlabelled ML risk output in UI = FORBIDDEN.

15. Human review SLA countdowns MUST be generated in the Risk
    Dashboard UI. Risk alerts without SLA countdown = INCOMPLETE UI.
```

---

## 1️⃣8️⃣ VERSIONING & CHANGE GOVERNANCE

```
MRA_VERSION = SEMVER (MAJOR.MINOR.PATCH)

MAJOR bump required for:
  - New risk dimension added to §5
  - Composite score weight changes
  - New high-stakes intent classification (§9)
  - Governance rule changes (§7)
  - Regulatory framework additions (§5 Dimension 6)

MINOR bump required for:
  - New threshold adjustments (documented)
  - New alert routing additions
  - New Prometheus metric additions
  - New bias protected attribute additions

PATCH bump required for:
  - Bug fixes in risk computation
  - Metric/alert label corrections
  - Documentation updates

CHANGE_CONTROL:   ARCHITECT_APPROVAL + AI_SAFETY_OFFICER_SIGN_OFF
BACKWARD_COMPAT:  MINIMUM_2_VERSIONS
SILENT_CHANGES:   FORBIDDEN
AUDIT_OF_CHANGES: ALL_VERSION_CHANGES_LOGGED_IN_risk_audit_log
```

---

## 🔒 FINAL SEAL

```
┌──────────────────────────────────────────────────────────────────┐
│             MODEL_RISK_AGENT.md — FINAL SEAL                     │
├──────────────────────────────────────────────────────────────────┤
│  STATUS               = SEALED & LOCKED                          │
│  EXECUTION            = ANTIGRAVITY PRODUCTION READY             │
│  COMPLIANCE           = ENTERPRISE GRADE                         │
│  HUMAN OVERRIDE       = ALWAYS PERMITTED                         │
│  AI AUTHORITY         = ADVISORY_AND_GATE_RAISING ONLY           │
│  AUTONOMOUS MODEL KILL = FORBIDDEN                               │
│  AUDIT                = 100% COVERAGE — APPEND ONLY              │
│  FAIRNESS             = 8 PROTECTED ATTRIBUTES MONITORED         │
│  TENANT ISOLATION     = HARD ENFORCED                            │
│  STAGE GATE           = ENFORCED                                 │
│  MINOR PROTECTION     = HARD ENFORCED                            │
│  GDPR/DPDP            = ENFORCED                                 │
│  CHANGE POLICY        = APPEND ONLY                              │
├──────────────────────────────────────────────────────────────────┤
│  ANY DEVIATION FROM THIS DOCUMENT = STOP EXECUTION               │
│  ANY UNAPPROVED MUTATION = INVALID BUILD                         │
│  ANY AUTONOMOUS MODEL KILL = VIOLATION                           │
│  ANY UNMONITORED PRODUCTION MODEL = HARD STOP                    │
└──────────────────────────────────────────────────────────────────┘

✔  AGENT IDENTITY LOCKED
✔  8 RISK DIMENSIONS LOCKED
✔  MODEL LIFECYCLE STATES LOCKED
✔  COMPOSITE RISK SCORING LOCKED
✔  25 GOVERNANCE RULES LOCKED
✔  HIGH-STAKES INTENT CLASSIFICATION LOCKED (11 intents)
✔  RETRAINING GOVERNANCE POLICY LOCKED (12-step sequence)
✔  FAIRNESS & BIAS RISK LOCKED (8 protected attributes)
✔  SECURITY & ADVERSARIAL RISK LOCKED
✔  COMPLIANCE (GDPR/DPDP) LOCKED
✔  MINOR PROTECTION LOCKED
✔  RIGHT-TO-EXPLANATION LOCKED
✔  BIAS COMPLAINT REGISTRY LOCKED
✔  IMMUTABLE RISK AUDIT SCHEMA LOCKED
✔  TENANT-ISOLATED RISK STATE LOCKED
✔  ESCALATION MATRIX LOCKED
✔  STAGE GATE LOCKED
✔  OBSERVABILITY LOCKED
✔  15 ANTIGRAVITY BINDING INSTRUCTIONS LOCKED
✔  VERSIONING GOVERNANCE LOCKED

PARENT AGENT:    ML_ROUTING_AGENT v1.0.0
SIBLING AGENTS:  COMPLIANCE_AGENT | AUDIT_AGENT | FRAUD_AGENT
REPORTING TO:    AI_SAFETY_OFFICER | COMPLIANCE_ADMIN | SUPER_ADMIN

FURTHER CHANGES = APPEND ONLY
CHANGE AUTHORITY = ARCHITECT + AI_SAFETY_OFFICER SIGN-OFF REQUIRED
```

---

*MODEL_RISK_AGENT.md · Ecoskiller Enterprise SaaS · Version 1.0.0*
*Sealed for Antigravity Execution Engine · Principal ML Risk Engineer Grade*
*Inherits from: ML_ROUTING_AGENT v1.0.0 | Platform Master Constitution*
