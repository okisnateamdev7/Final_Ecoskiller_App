# 🔒 INTELLIGENCE_BIAS_DETECTION_AGENT
## ECOSKILLER ANTIGRAVITY — INTELLIGENCE & INNOVATION CORE
**Status:** `FINAL · SEALED · LOCKED · GOVERNED · DETERMINISTIC`
**Mutation Policy:** Add-only via version bump · Safety Owner sign-off required
**Interpretation Authority:** NONE
**Execution Authority:** Human declaration only
**Seal Version:** v1.0.0
**Owner Class:** ML · Intelligence · Safety
**Classification:** SAFETY-CRITICAL · HIGHEST GOVERNANCE TIER

---

> ⚠️ **SAFETY-CRITICAL DECLARATION**
> This agent operates at the highest governance tier on the Ecoskiller Antigravity platform.
> It is the primary guardian of fairness, equity, and non-discrimination across all ML/AI
> models, ranking systems, recommendation engines, career predictions, job matching pipelines,
> and scoring algorithms. No ML model may be deployed or remain active if this agent raises
> an unresolved CRITICAL bias flag. Violation → STOP EXECUTION → ESCALATE → HUMAN REVIEW.

---

## ░░ AGENT IDENTITY BLOCK ░░

```
AGENT_NAME              = INTELLIGENCE_BIAS_DETECTION_AGENT
SYSTEM_ROLE             = Platform-Wide ML Fairness Auditor & Bias Detection Engine
PRIMARY_DOMAIN          = Intelligence Lane (Lane F) + Governance Lane (Lane D)
                          Dual-lane agent — Intelligence produces, Governance enforces
EXECUTION_MODE          = Deterministic + Validated + Continuous Background Monitor
DATA_SCOPE              = All ML/AI model outputs platform-wide ·
                          User demographic signal proxies · Prediction distributions ·
                          Ranking distributions · Score distributions · Audit logs ·
                          Feedback loops · Training data manifests
TENANT_SCOPE            = Strict Isolation + Platform-Level Cross-Tenant
                          Aggregate (anonymized, non-PII only)
FAILURE_POLICY          = HALT affected model on CRITICAL bias detection ·
                          LOG all findings · ESCALATE to Safety Owner ·
                          Block model redeployment until clearance
VERSION                 = v1.0.0
ARCHITECTURE            = Microservices + Event-Driven + Scheduled Batch + Streaming
SECURITY_MODEL          = Zero-Trust Multi-Tenant Isolation
DATA_POLICY             = Append-Only Audit Trail · No PII in bias computation
SCALE_TARGET            = 10M–100M users · Covers all 300 user types
ML_USAGE                = 80% Traditional ML (bias metrics, drift, distribution analysis)
AI_USAGE                = 20% LLM / Semantic Reasoning (explanation generation only)
MONITORING_MODE         = Continuous streaming + Daily batch + On-demand audit
GOVERNANCE_TIER         = TIER-1 SAFETY CRITICAL
```

---

## 1️⃣ PURPOSE DECLARATION

### Problem Solved

Ecoskiller Antigravity serves 10M–100M users across 300 user types spanning students from school to PhD, trainers, institutes, enterprises, government bodies, and freelancers. Across its platform, dozens of ML models make consequential decisions: career path predictions, job matching scores, skill gap rankings, trainer quality scores, campus leaderboard rankings, content recommendation orders, fraud risk scores, and more.

Without a dedicated, always-on bias detection layer, the following failure modes are guaranteed at scale:

- **Demographic bias:** Career predictions systematically under-ranking women, rural users, or students from tier-3 institutions into lower-opportunity paths
- **Socioeconomic proxy bias:** Engagement-based features (streak counts, device type, time-on-platform) acting as proxies for economic privilege and distorting ML rankings
- **Domain bias:** Arts, Commerce, and Humanities students receiving systematically lower confidence scores than STEM students due to training data imbalance
- **Feedback loop amplification:** Biased recommendations generating biased behavioral data that retrains models to become more biased over time
- **Trainer bias:** Automated quality scores systematically disadvantaging trainers from non-metro geographies or regional-language instructors
- **Recruiter bias leakage:** Hiring preference signals from biased employers contaminating job-match scoring models
- **Geographic bias:** Job matching systematically surfacing urban-only opportunities to rural students, narrowing perceived career paths

The INTELLIGENCE_BIAS_DETECTION_AGENT is the single authoritative guardian against all the above. It is non-optional, always active, and has the authority to flag, halt, and block any model operating on the platform.

### What Input It Consumes

**Streaming (real-time):**
- Model prediction events from all ML agents (scores, rankings, confidence values)
- User interaction feedback signals (clicks, applications, acceptances, rejections)
- Demographic proxy feature vectors (age band, geography tier, education level, domain track)
- Agent output events from: CAREER_PREDICTION_AGENT, JOB_MATCH_AGENT, RECOMMENDATION_ENGINE_AGENT, RANK_UPDATE_AGENT, ASSESSMENT_RESULT_AGENT, TRAINER_QUALITY_AGENT, FRAUD_DETECTION_AGENT

**Batch (daily/weekly):**
- Full prediction distribution snapshots per model per demographic segment
- Training data manifests from MODEL_REGISTRY_AGENT
- Historical audit logs from AUDIT_STORE
- Feedback completion rates by user segment

**On-Demand:**
- Full model audit requests triggered by Safety Owner
- Incident investigation payloads
- Pre-deployment model clearance requests

### What Output It Produces

- `bias_audit_report`: structured per-model bias finding with severity, affected segments, metric values
- `bias_alert_event`: real-time alert emitted to OBSERVABILITY_AGENT and Safety Owner when threshold breached
- `model_clearance_certificate`: issued to MODEL_REGISTRY_AGENT when a model passes bias audit (required before deployment)
- `bias_explanation_text`: AI-generated plain-language summary of detected bias pattern (governance-validated)
- `remediation_recommendation`: structured list of corrective actions for ML Owner
- `platform_fairness_scorecard`: aggregated platform-wide fairness health score (0.0–1.0) emitted daily
- `audit_reference`: UUID per execution for full traceability

### Downstream Agents That Depend on This Agent

| Agent | Dependency |
|---|---|
| MODEL_REGISTRY_AGENT | Requires model_clearance_certificate before any model goes live |
| CAREER_PREDICTION_AGENT | Receives bias flags; halts on CRITICAL |
| JOB_MATCH_AGENT | Receives bias flags; halts on CRITICAL |
| RECOMMENDATION_ENGINE_AGENT | Receives bias flags; halts on CRITICAL |
| RANK_UPDATE_AGENT | Receives ranking bias flags |
| TRAINER_QUALITY_AGENT | Receives quality score bias flags |
| OBSERVABILITY_AGENT | Receives all bias alerts and fairness scorecards |
| NOTIFICATION_MANAGER_AGENT | Receives Safety Owner alert triggers |
| COMPLIANCE_AUDIT_AGENT | Receives full bias audit reports |
| FEATURE_STORE_AGENT | Receives instructions to quarantine biased feature vectors |

### Upstream Agents That Feed This Agent

| Agent | Data Provided |
|---|---|
| CAREER_PREDICTION_AGENT | Prediction bundles with confidence scores per user |
| JOB_MATCH_AGENT | Match scores and ranking outputs |
| RECOMMENDATION_ENGINE_AGENT | Content recommendation orders and scores |
| RANK_UPDATE_AGENT | XP and leaderboard position changes |
| ASSESSMENT_RESULT_AGENT | Competency and evaluation score distributions |
| TRAINER_QUALITY_AGENT | Trainer quality scores and ranking distributions |
| FRAUD_DETECTION_AGENT | Risk scores and flag distributions |
| FEATURE_STORE_AGENT | Feature vector distributions for drift and proxy analysis |
| MODEL_REGISTRY_AGENT | Training data manifests and model metadata |
| IDENTITY_AGENT | Tenant-validated user context (no raw PII) |
| AUDIT_STORE | Historical prediction and interaction logs |

---

## 2️⃣ BIAS TAXONOMY (PLATFORM-WIDE — SEALED)

This agent monitors and detects the following bias classes. This taxonomy is sealed. New classes require Safety Owner approval and version bump.

```yaml
BIAS_CLASS_REGISTRY:

  BC-01: DEMOGRAPHIC_BIAS
    definition: Systematic score or ranking differences correlated with
                gender, age band, religion, caste, or ethnicity proxies
    severity_potential: CRITICAL
    applicable_models: [CAREER_PREDICTION, JOB_MATCH, ASSESSMENT_RESULT,
                        RANK_UPDATE, RECOMMENDATION_ENGINE]
    primary_metric: Demographic Parity Difference (DPD)
    secondary_metric: Equalized Odds Difference (EOD)

  BC-02: SOCIOECONOMIC_PROXY_BIAS
    definition: Features acting as proxies for economic privilege (device type,
                session length, platform engagement rate, streak count) causing
                systematically lower scores for economically disadvantaged users
    severity_potential: CRITICAL
    applicable_models: [CAREER_PREDICTION, JOB_MATCH, RANK_UPDATE,
                        RECOMMENDATION_ENGINE]
    primary_metric: Proxy Correlation Coefficient (PCC) with outcome variable
    secondary_metric: Score gap between economic tier proxies

  BC-03: GEOGRAPHIC_BIAS
    definition: Systematic disadvantage to rural, tier-2, tier-3 city users
                or non-metro trainers in scoring or matching outcomes
    severity_potential: HIGH
    applicable_models: [JOB_MATCH, CAREER_PREDICTION, TRAINER_QUALITY,
                        RECOMMENDATION_ENGINE]
    primary_metric: Geographic Parity Difference (GPD)
    secondary_metric: Opportunity set diversity score by geography

  BC-04: DOMAIN_TRACK_BIAS
    definition: Arts, Commerce, or Humanities users receiving systematically
                lower confidence scores, fewer recommendations, or lower
                career path quality than Science/Technology users
    severity_potential: HIGH
    applicable_models: [CAREER_PREDICTION, RECOMMENDATION_ENGINE, JOB_MATCH]
    primary_metric: Inter-Domain Score Variance (IDSV)
    secondary_metric: Training data representation ratio per domain track

  BC-05: FEEDBACK_LOOP_AMPLIFICATION_BIAS
    definition: Biased model outputs generating biased behavioral training data
                that causes models to become progressively more biased over
                successive retraining cycles
    severity_potential: CRITICAL
    applicable_models: ALL models with continuous retraining
    primary_metric: Bias Drift Velocity (BDV) — rate of bias metric change
                    across consecutive model versions
    secondary_metric: Training data demographic shift index

  BC-06: TRAINER_QUALITY_BIAS
    definition: Trainer quality scores systematically disadvantaging trainers
                from non-metro geographies, regional-language instructors,
                or female trainers independent of actual performance quality
    severity_potential: HIGH
    applicable_models: [TRAINER_QUALITY_AGENT]
    primary_metric: Trainer Demographic Parity Score (TDPS)
    secondary_metric: Quality score variance by geography tier

  BC-07: RECRUITER_PREFERENCE_LEAKAGE_BIAS
    definition: Hiring preference signals from historically biased employers
                contaminating job-match scoring models with proxy preferences
    severity_potential: CRITICAL
    applicable_models: [JOB_MATCH_AGENT]
    primary_metric: Employer Bias Contamination Index (EBCI)
    secondary_metric: Application success rate gap by user segment

  BC-08: ASSESSMENT_GRADING_BIAS
    definition: AI-assisted or ML-scored assessments producing systematically
                different outcomes for users based on language, geography,
                or education institution tier
    severity_potential: HIGH
    applicable_models: [ASSESSMENT_RESULT_AGENT]
    primary_metric: Assessment Score Parity Gap (ASPG)
    secondary_metric: Score distribution by institution tier

  BC-09: LANGUAGE_BIAS
    definition: Users communicating in regional languages or non-standard
                English receiving lower NLP-based scores, lower semantic
                match scores, or reduced recommendation quality
    severity_potential: HIGH
    applicable_models: [JOB_MATCH, RECOMMENDATION_ENGINE,
                        ASSESSMENT_RESULT_AGENT]
    primary_metric: Language Parity Score (LPS)
    secondary_metric: NLP confidence score by input language

  BC-10: CONFIRMATION_BIAS_LOCK-IN
    definition: Recommendation engines presenting users only with content
                confirming their existing skill profile, locking them into
                narrow career paths and preventing discovery of adjacent
                opportunities
    severity_potential: MODERATE
    applicable_models: [RECOMMENDATION_ENGINE_AGENT, CAREER_PREDICTION_AGENT]
    primary_metric: Recommendation Diversity Index (RDI)
    secondary_metric: Career path spread score per user cohort
```

---

## 3️⃣ ML / AI LOGIC LAYER

### Primary Layer — ML (80% of decisions)

```yaml
MODELS_DEPLOYED:

  bias_metric_calculator_v1:
    type: Statistical Computation Engine (not learned — deterministic math)
    purpose: Compute all bias metrics per model per demographic segment
    methods:
      - Demographic Parity Difference: |P(Y=1|A=0) - P(Y=1|A=1)|
      - Equalized Odds Difference: max(|TPR_gap|, |FPR_gap|)
      - Individual Fairness Score: Lipschitz condition approximation
      - Calibration Error by Group: ECE per demographic segment
      - Proxy Correlation: Pearson/Spearman r between sensitive proxies
                           and prediction outcomes
      - Distribution KS Test: Two-sample KS test per segment pair
    output: bias_metric_bundle per model per execution window

  bias_drift_tracker_v1:
    type: Time-Series Anomaly Detection (Isolation Forest + CUSUM)
    purpose: Detect when bias metrics are worsening over time across
             model retraining cycles
    features_used:
      - bias_metric_history_vector (last 12 measurement windows)
      - model_retraining_event_flag
      - training_data_composition_delta
      - user_segment_population_shift
    output: bias_drift_score (0.0–1.0) + trend_direction (improving|stable|degrading)
    alert_trigger: drift_score > 0.3 over 3 consecutive windows

  proxy_feature_detector_v1:
    type: Correlation Analysis + Mutual Information Scorer
    purpose: Identify features in any model that act as illegal proxies for
             protected attributes (e.g., session_duration as wealth proxy,
             institution_name as caste proxy)
    features_monitored:
      - All features from all registered models (fetched from MODEL_REGISTRY_AGENT)
    methods:
      - Pearson correlation between feature and sensitive attribute proxies
      - Mutual information scoring
      - Partial correlation controlling for outcome variable
    output: proxy_feature_risk_map per model

  feedback_loop_monitor_v1:
    type: Causal Graph Analysis + Time-Series Regression
    purpose: Detect when biased model outputs are feeding back into
             training data causing feedback loop amplification
    signals_monitored:
      - Output distribution shift per model version
      - Training data demographic composition delta
      - Bias metric change across consecutive model versions
    output: feedback_loop_risk_score (0.0–1.0) + amplification_direction

TRAINING_FREQUENCY:
  bias_metric_calculator:   Continuous streaming (5-minute windows) + Daily batch
  bias_drift_tracker:       Weekly recalibration on new drift data
  proxy_feature_detector:   On every new model registration + Weekly full scan
  feedback_loop_monitor:    After every model retraining event

DRIFT_DETECTION (for the detector itself):
  The bias detection models must themselves be monitored for statistical drift.
  method: Bootstrap sampling comparison of metric distributions
  trigger: If bias_metric_calculator produces implausible outputs
           (e.g., all metrics = 0.0 for 48 hours) → ALERT Safety Owner
  This prevents the detector from silently failing.

VERSION_CONTROL:
  storage: immutable model registry (MinIO: /models/bias_detection/)
  reference: detector_version UUID in every audit output
  rollback: previous version activated within 10 minutes on failure
```

### Secondary Layer — AI (20% — explanation assist only)

```yaml
AI_USAGE_SCOPE:
  purpose: Generate human-readable explanations of detected bias patterns
           for Safety Owner review and remediation guidance
  input:  structured bias_audit_report bundle from ML layer
  output: plain-language explanation + recommended remediation text
  boundary: AI must NOT compute, modify, or override bias metric values
  boundary: AI must NOT determine severity levels — ML layer decides severity
  boundary: AI must NOT recommend halting a model — that is deterministic rule logic

PROMPT_GOVERNANCE:
  version: bias_explain_prompt_v1.0.0
  structure: deterministic template — no open-ended generation
  template: |
    SYSTEM: You are a bias explanation assistant for Ecoskiller Antigravity.
    You receive a structured bias audit bundle from the ML bias detection system.
    Your ONLY tasks are:
    1. Write a 3–4 sentence plain-language explanation of what bias was detected,
       which user segments are affected, and what the probable cause is.
    2. List 3 concrete remediation steps the ML Owner should take.
    Do NOT invent data. Do NOT modify metric values. Do NOT suggest halting
    or clearing models — that is decided by deterministic rules, not you.
    Output ONLY valid JSON in the specified schema.

    INPUT: {{bias_audit_bundle_json}}

    OUTPUT FORMAT (strict):
    {
      "bias_explanation": "<3-4 sentences>",
      "probable_cause": "<1-2 sentences>",
      "remediation_steps": ["step 1", "step 2", "step 3"],
      "prompt_version": "bias_explain_prompt_v1.0.0"
    }

  fallback: AI timeout (3s) → use static template with metric values only
  no creative interpretation permitted
  no autonomous severity decisions permitted
```

---

## 4️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA_STREAMING: {
  "required_fields": [
    "source_agent_id",
    "source_model_id",
    "source_model_version",
    "tenant_id",
    "prediction_event_id",
    "timestamp_utc",
    "output_payload": {
      "score_or_rank": "float",
      "user_segment_vector": {
        "geography_tier": "enum[metro|tier2|tier3|rural|international]",
        "education_level": "enum[school|diploma|ug|pg|phd|other]",
        "domain_track": "enum[science|technology|commerce|arts|administration]",
        "experience_band": "enum[0-1yr|1-3yr|3-5yr|5-10yr|10yr+]",
        "gender_proxy_flag": "boolean (true if any gender proxy present in features)",
        "language_of_interaction": "ISO 639-1 code"
      },
      "confidence_score": "float 0.0-1.0",
      "prediction_type": "enum[career_path|job_match|recommendation|rank|quality_score|risk_score]"
    }
  ],

  "optional_fields": [
    "feedback_signal",
    "interaction_outcome",
    "prior_prediction_id"
  ],

  "validation_rules": [
    "source_agent_id must be in approved AGENT_REGISTRY",
    "source_model_id must be registered in MODEL_REGISTRY_AGENT",
    "tenant_id must match JWT tenant claim of source agent",
    "timestamp_utc must be ISO 8601 and within 60 seconds of receipt",
    "score_or_rank must be numeric — reject strings",
    "prediction_type must match defined enum",
    "user_segment_vector fields must match defined enums",
    "All enum values must be from approved taxonomies — reject unknowns"
  ],

  "security_checks": [
    "Validate agent JWT signature before processing",
    "Reject cross-tenant payload references",
    "Validate RBAC: source agent must have bias_data:write permission",
    "No raw PII permitted in any field — reject if PII detected",
    "Rate limit: max 10,000 events per minute per source agent"
  ],

  "domain_checks": [
    "source_model_id must exist in MODEL_REGISTRY_AGENT with active status",
    "prediction_type must match source_agent_id's declared output type",
    "user_segment_vector must not contain identifying information"
  ]
}

INPUT_SCHEMA_BATCH_AUDIT: {
  "required_fields": [
    "audit_request_id",
    "requesting_actor_id",
    "requesting_actor_role",
    "target_model_id",
    "target_model_version",
    "audit_window_start_utc",
    "audit_window_end_utc",
    "audit_scope": "enum[full|demographic|geographic|domain|proxy|feedback_loop]",
    "tenant_id"
  ],

  "validation_rules": [
    "requesting_actor_role must be in [safety_owner, platform_admin, compliance_admin]",
    "audit_window must not exceed 90 days",
    "target_model_id must be in MODEL_REGISTRY_AGENT",
    "audit_scope must match defined enum"
  ]
}
```

**Rules:** No null tolerance. Reject all malformed inputs with structured 422 error. Log all validation failures.

---

## 5️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA_BIAS_AUDIT_REPORT: {
  "audit_id": "UUID",
  "audit_timestamp_utc": "ISO 8601",
  "target_model_id": "string",
  "target_model_version": "string",
  "audit_window": { "start": "ISO 8601", "end": "ISO 8601" },
  "tenant_id": "string",
  "detector_version": "string",

  "bias_findings": [
    {
      "bias_class_id": "BC-01 through BC-10",
      "bias_class_label": "string",
      "severity": "enum[CRITICAL|HIGH|MODERATE|LOW|PASS]",
      "affected_segments": ["segment descriptions"],
      "metric_name": "string",
      "metric_value": "float",
      "threshold_breached": "float",
      "direction": "enum[privileged_over_disadvantaged|disadvantaged_over_privileged|bidirectional]",
      "sample_count": "integer",
      "statistical_significance": { "p_value": "float", "confidence_interval": [] }
    }
  ],

  "overall_model_fairness_status": "enum[CLEARED|FLAGGED|HALTED|UNDER_REVIEW]",
  "highest_severity_found": "enum[CRITICAL|HIGH|MODERATE|LOW|PASS]",
  "model_clearance_issued": "boolean",

  "bias_drift_assessment": {
    "drift_score": "float 0.0-1.0",
    "trend_direction": "enum[improving|stable|degrading]",
    "consecutive_degrading_windows": "integer"
  },

  "proxy_feature_risks": [
    {
      "feature_name": "string",
      "proxy_correlation_score": "float",
      "probable_sensitive_attribute": "string",
      "risk_level": "enum[HIGH|MODERATE|LOW]"
    }
  ],

  "feedback_loop_risk": {
    "risk_score": "float 0.0-1.0",
    "amplification_direction": "string",
    "retraining_contamination_flag": "boolean"
  },

  "bias_explanation": "string (AI-generated, governance-validated)",
  "probable_cause": "string (AI-generated)",
  "remediation_steps": ["string"],

  "confidence_score": "float 0.0-1.0",
  "detector_version": "string",
  "audit_reference": "UUID",

  "next_trigger_events": [
    "MODEL_HALT_EVENT (if CRITICAL)",
    "SAFETY_OWNER_ALERT_EVENT",
    "COMPLIANCE_AUDIT_EVENT",
    "OBSERVABILITY_METRIC_EMIT_EVENT"
  ]
}

OUTPUT_SCHEMA_MODEL_CLEARANCE_CERTIFICATE: {
  "certificate_id": "UUID",
  "issued_at_utc": "ISO 8601",
  "model_id": "string",
  "model_version": "string",
  "clearance_status": "enum[CLEARED|CONDITIONAL|DENIED]",
  "conditions": ["string (if CONDITIONAL)"],
  "valid_until_utc": "ISO 8601 (clearance expires — re-audit required)",
  "issuing_agent": "INTELLIGENCE_BIAS_DETECTION_AGENT",
  "detector_version": "string",
  "audit_reference": "UUID",
  "safety_owner_required_sign_off": "boolean (true if CONDITIONAL)"
}
```

---

## 6️⃣ BIAS SEVERITY THRESHOLDS & ENFORCEMENT MATRIX

```yaml
SEVERITY_THRESHOLDS:

  CRITICAL:
    trigger_conditions:
      - Demographic Parity Difference (DPD) > 0.10
      - Equalized Odds Difference (EOD) > 0.10
      - Proxy Correlation Coefficient (PCC) > 0.50 (feature clearly proxying protected attr)
      - Employer Bias Contamination Index (EBCI) > 0.15
      - Feedback Loop Amplification: bias_drift_velocity > 0.05 per retraining cycle
        for 3 consecutive cycles
    enforcement:
      - IMMEDIATE model halt event emitted to MODEL_REGISTRY_AGENT
      - Model predictions suspended within 60 seconds of detection
      - Safety Owner paged immediately (PagerDuty — P1 incident)
      - ML Owner notified immediately
      - No redeployment permitted without full audit clearance + Safety Owner sign-off
      - Incident record created in COMPLIANCE_AUDIT_AGENT
    response_sla:
      - Safety Owner acknowledgement: 2 hours
      - Root cause analysis: 48 hours
      - Remediation plan: 72 hours
      - Re-audit clearance: defined per remediation scope

  HIGH:
    trigger_conditions:
      - DPD between 0.05 and 0.10
      - EOD between 0.05 and 0.10
      - Geographic Parity Difference (GPD) > 0.10
      - Inter-Domain Score Variance (IDSV) > 0.12
      - Trainer Demographic Parity Score gap > 0.10
      - Assessment Score Parity Gap (ASPG) > 0.08
      - Language Parity Score gap > 0.10
    enforcement:
      - Bias flag attached to all model outputs (consumers must display flag)
      - Safety Owner notified (PagerDuty — P2 incident)
      - ML Owner notified with remediation recommendation
      - Model may continue operating with flag for max 7 days before mandatory re-audit
      - Increased monitoring frequency: 1-minute windows instead of 5-minute
    response_sla:
      - Safety Owner acknowledgement: 8 hours
      - Remediation plan: 7 days
      - Re-audit: within 14 days

  MODERATE:
    trigger_conditions:
      - DPD between 0.02 and 0.05
      - Recommendation Diversity Index (RDI) < 0.60
      - Proxy Correlation Coefficient between 0.25 and 0.50
      - Bias drift: degrading trend for 2 consecutive windows
    enforcement:
      - Bias flag logged in audit trail
      - ML Owner notified via async notification (no pager)
      - Model continues operating
      - Remediation tracked in next scheduled sprint
    response_sla:
      - Remediation plan: 30 days

  LOW:
    trigger_conditions:
      - DPD between 0.01 and 0.02
      - Minor proxy correlations (PCC 0.10–0.25)
    enforcement:
      - Logged in audit trail
      - Included in monthly fairness report
      - No immediate action required

  PASS:
    trigger_conditions:
      - All metric values within acceptable thresholds
    enforcement:
      - Model clearance certificate issued (valid 30 days)
      - Logged in audit trail
      - Included in platform fairness scorecard
```

---

## 7️⃣ SCALABILITY DESIGN

```yaml
STREAMING_PROCESSING:
  EXPECTED_EVENTS_PER_SECOND:   2,000 (steady) / 20,000 (peak)
  LATENCY_TARGET:               P95 < 300ms for streaming bias metric update
  MAX_CONCURRENCY:              5,000 simultaneous event processing goroutines
  QUEUE_STRATEGY:               Redis Streams — topic: bias_detection_events
  PROCESSING_MODE:              Async streaming with 5-minute tumbling windows
  IDEMPOTENCY:                  prediction_event_id deduplication (TTL: 24 hours)

BATCH_PROCESSING:
  SCHEDULE:                     Daily 02:00 UTC full platform bias audit
  WINDOW_SIZE:                  24 hours
  PARALLELISM:                  Parallel per model per tenant (Kubernetes Job)
  ESTIMATED_DURATION:           < 2 hours for 10M user platform

SCALING_RULES:
  horizontal:   Kubernetes HPA — scale on queue depth > 5,000 or CPU > 65%
  pod_min:      5 (always-on — safety critical)
  pod_max:      100
  cooldown:     60 seconds
  priority:     HIGHEST — this agent gets Kubernetes PriorityClass: system-critical

STATELESS: All state fetched per window — no in-process state
```

---

## 8️⃣ SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  - All user segment vectors scoped by tenant_id
  - Cross-tenant aggregate stats: anonymized, aggregated only — no user-level data
  - No raw user_id in bias computation — only anonymized cohort identifiers
  - Tenant bias reports visible only to that tenant's Safety Owner

DOMAIN_ISOLATION:
  - Bias data does not bleed into billing, marketplace, or ERP domains
  - Model prediction payloads consumed read-only — no writes back to source

PII_PROTECTION:
  - This agent NEVER stores or processes raw PII
  - All user segment vectors use categorical buckets (e.g., "tier2_city" not city name)
  - Gender signals: binary proxy flag only (feature_contains_gender_proxy: true/false)
  - If PII is detected in any incoming payload: REJECT + LOG_SECURITY_INCIDENT

ROLE_BASED_AUTHORIZATION:
  Required to submit prediction events:   bias_data:write (source agents only)
  Required to read bias audit reports:    bias_audit:read (safety_owner, compliance_admin)
  Required to request model clearance:    model_clearance:request (ml_owner)
  Required to issue halt events:          model_halt:write (this agent only)

ENCRYPTION:
  - TLS 1.3 on all event streams
  - AES-256 at rest for all audit reports
  - Model clearance certificates: signed with platform private key

AUDIT_LOGGING:
  - All detection events logged append-only to: bias_detection_audit Loki stream
  - All model halt events logged to: safety_incident_log (immutable)
  - Log retention: minimum 10 years (regulatory compliance)
  - Tamper-evidence: SHA-256 chaining of consecutive log entries
```

---

## 9️⃣ AUDIT & TRACEABILITY

Every detection cycle MUST emit this exact audit event:

```json
AUDIT_EVENT: {
  "timestamp_utc":              "ISO 8601",
  "event_type":                 "BIAS_DETECTION_CYCLE_COMPLETE",
  "agent_id":                   "INTELLIGENCE_BIAS_DETECTION_AGENT",
  "agent_version":              "v1.0.0",
  "detector_version":           { "metric_calculator": "...", "drift_tracker": "...",
                                  "proxy_detector": "...", "feedback_monitor": "..." },
  "target_model_id":            "string",
  "target_model_version":       "string",
  "tenant_id":                  "string",
  "input_event_count":          "integer (events processed in window)",
  "input_hash":                 "SHA-256 of aggregated input set",
  "output_hash":                "SHA-256 of bias_audit_report",
  "bias_findings_count":        "integer",
  "highest_severity_detected":  "enum[CRITICAL|HIGH|MODERATE|LOW|PASS]",
  "model_halted":               "boolean",
  "clearance_issued":           "boolean",
  "decision_path":              ["window_aggregation", "metric_computation",
                                 "threshold_evaluation", "severity_classification",
                                 "action_enforcement", "ai_explanation", "event_emission"],
  "latency_ms":                 "integer",
  "anomaly_flags":              [],
  "audit_reference":            "UUID"
}
```

**Logs are immutable and tamper-evident. No agent, admin, or process may modify or delete.
Violation → STOP EXECUTION → ESCALATE TO PLATFORM SECURITY.**

---

## 🔟 FAILURE POLICY

```yaml
FAILURE_SCENARIOS:

  invalid_input_payload:
    action: REJECT + STOP_PROCESSING_THIS_EVENT
    log: LOG_VALIDATION_FAILURE with field-level detail + source_agent_id
    response: 422 structured error to source agent
    escalate: NONE for isolated events; if >5% of events invalid → ALERT ML Owner
    retry: NONE — source agent must fix payload

  model_detection_unavailable:
    action: STOP_BIAS_MONITORING for affected model
    log: LOG_INCIDENT — bias_detector_unavailable
    escalate: ESCALATE_TO = Safety Owner (P1 — monitoring down = safety gap)
    response: MODEL_MONITORING_SUSPENDED event to OBSERVABILITY_AGENT
    retry: 3 attempts with exponential backoff (2s, 8s, 32s)
    note: Platform should NOT continue model deployment if bias monitoring is down

  ai_explanation_timeout:
    action: USE_FALLBACK — static template with metric values
    log: LOG_AI_TIMEOUT with duration
    response: audit report returned with fallback explanation (clearly labeled)
    escalate: NONE if isolated; alert if >10% of audits timeout

  insufficient_sample_size:
    action: DEFER bias metric computation — mark as INSUFFICIENT_DATA
    log: LOG_INSUFFICIENT_SAMPLE with segment details
    response: audit report with INSUFFICIENT_DATA flag per affected metric
    threshold: minimum 100 predictions per segment per metric per window
    escalate: NONE — log for ML Owner review

  statistical_significance_failure:
    action: Include finding but mark as STATISTICALLY_UNCERTAIN
    log: LOG_LOW_SIGNIFICANCE with p-value
    response: include in report with p_value and confidence_interval
    escalate: NONE — inform Safety Owner in next daily report

  data_corruption_in_audit_log:
    action: STOP_EXECUTION of affected audit
    log: LOG_INCIDENT — audit_data_integrity_failure
    escalate: ESCALATE_TO = Safety Owner + Platform Security Admin
    retry: NONE — halt until human review
    note: Prior model clearance certificates may be revoked by Safety Owner

  bias_detector_self_failure:
    definition: Bias metrics show implausible uniformity (all 0.0 or 1.0)
                for > 48 hours — detector may have silently failed
    action: STOP_BIAS_MONITORING + ALERT Safety Owner (P1)
    log: LOG_INCIDENT — detector_self_check_failure
    escalate: ESCALATE_TO = Safety Owner + ML Owner
    note: Self-monitoring prevents silent failure of the safety layer

GLOBAL_RULE:
  NO silent failures.
  This is a safety-critical agent. Every failure must produce
  a structured log event, a response, and an escalation where appropriate.
```

---

## 1️⃣1️⃣ INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - CAREER_PREDICTION_AGENT      → streams prediction events
  - JOB_MATCH_AGENT              → streams match score events
  - RECOMMENDATION_ENGINE_AGENT  → streams recommendation ranking events
  - RANK_UPDATE_AGENT            → streams XP and leaderboard events
  - ASSESSMENT_RESULT_AGENT      → streams competency score events
  - TRAINER_QUALITY_AGENT        → streams quality score events
  - FRAUD_DETECTION_AGENT        → streams risk score events
  - FEATURE_STORE_AGENT          → provides feature distribution vectors
  - MODEL_REGISTRY_AGENT         → provides model manifests + training data metadata
  - AUDIT_STORE                  → provides historical logs for batch audits
  - IDENTITY_AGENT               → provides tenant-validated context

DOWNSTREAM_AGENTS:
  - MODEL_REGISTRY_AGENT         → receives model_clearance_certificate /
                                   model_halt_event
  - CAREER_PREDICTION_AGENT      → receives bias_flag (may halt)
  - JOB_MATCH_AGENT              → receives bias_flag (may halt)
  - RECOMMENDATION_ENGINE_AGENT  → receives bias_flag (may halt)
  - RANK_UPDATE_AGENT            → receives ranking bias flag
  - TRAINER_QUALITY_AGENT        → receives quality score bias flag
  - FEATURE_STORE_AGENT          → receives quarantine instruction for biased features
  - OBSERVABILITY_AGENT          → receives all bias alerts + fairness scorecard
  - NOTIFICATION_MANAGER_AGENT   → receives Safety Owner + ML Owner alert triggers
  - COMPLIANCE_AUDIT_AGENT       → receives full bias audit reports

EVENT_TRIGGERS_EMITTED:

  BIAS_ALERT_EVENT:
    trigger: severity >= HIGH detected
    schema:  { audit_id, model_id, model_version, bias_class_id, severity,
               affected_segments, metric_value, threshold_breached, timestamp_utc }

  MODEL_HALT_EVENT:
    trigger: severity = CRITICAL detected
    schema:  { audit_id, model_id, model_version, halt_reason, effective_timestamp_utc }

  MODEL_CLEARANCE_ISSUED_EVENT:
    trigger: model passes full audit (PASS status)
    schema:  { certificate_id, model_id, model_version, valid_until_utc, audit_reference }

  FEATURE_QUARANTINE_EVENT:
    trigger: proxy_feature_risk HIGH detected
    schema:  { feature_name, model_id, proxy_correlation_score, timestamp_utc }

  PLATFORM_FAIRNESS_SCORECARD_EVENT:
    trigger: daily batch audit complete
    schema:  { scorecard_date, platform_fairness_score, models_cleared, models_flagged,
               models_halted, highest_severity_active, audit_reference }

  SAFETY_OWNER_ALERT_EVENT:
    trigger: severity >= HIGH or detector self-failure
    schema:  { incident_type, severity, model_id, audit_id, recommended_action,
               sla_deadline_utc, timestamp_utc }
```

---

## 1️⃣2️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent emits feature vectors to FEATURE_STORE_AGENT after every audit cycle:

```json
EMIT_FEATURE_VECTORS: [
  {
    "feature_name":  "model_fairness_score",
    "feature_value": 0.0-1.0,
    "entity_type":   "model",
    "entity_id":     "model_id",
    "timestamp":     "ISO 8601",
    "source_agent":  "INTELLIGENCE_BIAS_DETECTION_AGENT"
  },
  {
    "feature_name":  "highest_active_bias_severity",
    "feature_value": "enum value encoded as integer [PASS=0,LOW=1,MOD=2,HIGH=3,CRIT=4]",
    "entity_type":   "model",
    "entity_id":     "model_id",
    "timestamp":     "ISO 8601",
    "source_agent":  "INTELLIGENCE_BIAS_DETECTION_AGENT"
  },
  {
    "feature_name":  "platform_fairness_health",
    "feature_value": 0.0-1.0,
    "entity_type":   "platform",
    "entity_id":     "tenant_id",
    "timestamp":     "ISO 8601",
    "source_agent":  "INTELLIGENCE_BIAS_DETECTION_AGENT"
  }
]
```

---

## 1️⃣3️⃣ PRE-DEPLOYMENT MODEL CLEARANCE PROTOCOL

Every ML/AI model on Ecoskiller Antigravity MUST obtain a clearance certificate from this agent before being deployed to any environment beyond DEV. This is non-negotiable.

```yaml
CLEARANCE_PROTOCOL:

  STEP 1 — REGISTRATION:
    ML Owner registers new model in MODEL_REGISTRY_AGENT with:
      - model_id, model_version, model_type, training_data_manifest,
        feature_list, prediction_type, expected_user_segments
    MODEL_REGISTRY_AGENT emits: MODEL_REGISTERED_EVENT to this agent

  STEP 2 — TRAINING DATA AUDIT:
    This agent receives training data manifest and checks:
      - Demographic representation ratios across all training segments
      - Geographic distribution of training samples
      - Domain track distribution
      - Presence of any direct sensitive attribute features
      - Historical bias in labeled outcomes
    Output: training_data_bias_report (PASS/FLAG/REJECT)

  STEP 3 — SYNTHETIC PREDICTION AUDIT:
    ML Owner submits 1,000 synthetic test prediction sets covering
    all user segment combinations
    This agent runs full bias metric suite on synthetic outputs
    Output: synthetic_audit_report (PASS/FLAG/REJECT)

  STEP 4 — CLEARANCE DECISION:
    CLEARED:      Training data PASS + Synthetic PASS
                  Certificate issued. Valid 30 days.
    CONDITIONAL:  One layer PASS, one FLAG — model may deploy with
                  monitoring at 1-minute resolution + Safety Owner review
                  within 7 days. Certificate valid 14 days.
    DENIED:       Any REJECT — model must not deploy. ML Owner must
                  retrain and resubmit.

  STEP 5 — POST-DEPLOYMENT CONTINUOUS AUDIT:
    After deployment, this agent monitors live predictions continuously.
    Certificate is automatically suspended on CRITICAL detection.
    Reactivation requires new clearance (return to Step 3).
```

---

## 1️⃣4️⃣ QUARTERLY PLATFORM FAIRNESS AUDIT

```yaml
SCHEDULE: Quarterly (Jan 1, Apr 1, Jul 1, Oct 1 at 00:00 UTC)

SCOPE:
  - Full audit of all active models across all tenants (anonymized aggregate)
  - Training data composition review for all models retrained in quarter
  - Feedback loop amplification analysis (12-week retrospective)
  - User segment outcome distribution analysis (careers, jobs, rankings)
  - Trainer quality score demographic distribution analysis
  - Language and regional parity review

MANDATORY OUTPUTS:
  - Platform Quarterly Fairness Report (published to Safety Owner + Compliance Admin)
  - Model Fairness Rankings (ranked list of all models by fairness score)
  - Bias Trend Report (quarter-over-quarter change in all bias metrics)
  - Remediation Completion Report (were prior quarter remediations completed?)
  - Recommendations for next quarter (top 5 fairness improvement priorities)

SAFETY_OWNER_ACTIONS_REQUIRED:
  - Review and sign quarterly report within 14 days of generation
  - Approve or reject remediation plans for HIGH severity findings
  - Escalate CRITICAL patterns to Anthropic-equivalent governance body if unresolved

LEGAL_COMPLIANCE_NOTE:
  Quarterly reports must be retained for minimum 7 years.
  Reports constitute evidence of due diligence under AI fairness regulations.
  Reports must be made available to regulatory auditors on request.
```

---

## 1️⃣5️⃣ USER TYPE SCOPE

This agent serves all 300 user types from the Ecoskiller taxonomy by protecting their interests from algorithmic bias. It specifically protects:

**Most Vulnerable to Bias (primary protection focus):**
School students (Grades 6–12) — particularly Arts/Commerce stream students from tier-2/3 geographies; Dropout learners; Distance learning students; ITI/Polytechnic students; Rural users; Regional-language users; Female learners in STEM; Freelancers from non-metro regions; Government/NGO workers.

**Institutional Users Protected:**
Institute placement officers and counselors receive unbiased student analytics; Trainers protected from geographic/gender quality score bias.

**Employer Users Protected:**
HR managers and recruiters protected from receiving AI recommendations contaminated by historical hiring bias.

**Platform Operations:**
Safety Owners receive audit reports; Compliance admins receive fairness certifications; BI analysts receive fairness scorecards.

---

## 1️⃣6️⃣ PERFORMANCE MONITORING

```yaml
METRICS_EMITTED_TO_OBSERVABILITY_AGENT:

  detection_success_rate:
    definition:  audit cycles completed without error / total cycles scheduled
    target:      >= 99.9% (safety-critical — higher than standard agents)
    alert_threshold: < 99.5%

  critical_bias_detection_latency:
    definition:  time from prediction event receipt to CRITICAL alert emission
    target:      P95 < 60 seconds
    alert_threshold: > 120 seconds

  model_halt_activation_latency:
    definition:  time from CRITICAL detection to MODEL_HALT_EVENT emission
    target:      P99 < 60 seconds
    alert_threshold: > 120 seconds

  clearance_certificate_issuance_time:
    definition:  time from clearance request to certificate/denial
    target:      < 30 minutes for standard audit
    alert_threshold: > 60 minutes

  platform_fairness_score_trend:
    definition:  7-day rolling average of platform_fairness_score
    alert_threshold: score < 0.80 or declining > 0.05 in 7 days

  active_critical_flags:
    definition:  count of unresolved CRITICAL bias flags across all models
    target:      0
    alert_threshold: > 0 (any unresolved CRITICAL = Safety Owner P1 alert remains active)

  false_positive_rate:
    definition:  Safety Owner-confirmed false positives / total CRITICAL flags
    target:      < 5%
    review_frequency: Monthly (Safety Owner reviews and confirms/rejects flags)

DASHBOARD:
  Grafana panel: intelligence_bias_detection_overview
  Grafana panel: per_model_fairness_scores
  Grafana panel: active_bias_flags_heatmap
  Grafana panel: platform_fairness_trend_30day
  Alerts routed to: Safety Owner (P1/P2 PagerDuty) + ML Owner + Compliance Admin
```

---

## 1️⃣7️⃣ VERSIONING POLICY

```yaml
VERSION_CONTROL:
  current_version: v1.0.0
  policy: Add-only · Versioned · Backward compatible
  migration: documented in MIGRATION_LOG before any change
  rollback: previous version activates within 10 minutes
  breaking_changes: PROHIBITED without Safety Owner sign-off

SPECIAL_RULE_FOR_THRESHOLDS:
  Bias severity thresholds (Section 6) are versioned separately.
  Any threshold change requires:
    1. Safety Owner written approval
    2. Compliance Admin written approval
    3. Version bump to threshold_policy_version
    4. 30-day notice before activation
  Thresholds may only be tightened (lower), never loosened, without
  external regulatory review.

AGENT_CHANGELOG:
  v1.0.0:
    date: 2026-02-25
    author: Ecoskiller Intelligence & Safety Core
    description: Initial sealed and locked agent definition
    changes: INITIAL RELEASE
    threshold_policy_version: tp-v1.0.0
```

---

## 1️⃣8️⃣ NON-NEGOTIABLE RULES — SEALED

This agent **MUST NOT:**

```
❌ Create hidden logic or undocumented detection paths
❌ Modify bias metric values (metrics are computed mathematically — no adjustment)
❌ Allow the AI layer to determine severity levels or issue halt decisions
❌ Suppress, delay, or soften a CRITICAL finding for any reason
❌ Issue a model clearance certificate to a model with active CRITICAL findings
❌ Allow a model with an expired or revoked clearance to continue serving predictions
❌ Process raw PII in any bias computation
❌ Allow cross-tenant user-level data in bias computation (only anonymized aggregates)
❌ Modify, delete, or suppress audit logs or safety incident records
❌ Allow any ML Owner, tenant admin, or operator to override a CRITICAL halt without Safety Owner written sign-off
❌ Tighten or loosen bias thresholds without versioned approval process
❌ Execute outside the bias detection and fairness auditing domain
❌ Allow bias detection monitoring to be paused or suspended for any period without Safety Owner authorization
❌ Allow a model to retrain on data that this agent has flagged as contaminated without quarantine clearance
❌ Mix tenant-level bias data in any cross-tenant computation without explicit anonymization
```

---

## 1️⃣9️⃣ SAFETY OWNER RESPONSIBILITIES

```yaml
SAFETY_OWNER_ROLE: ML · Intelligence · Safety Owner

RESPONSIBILITIES:

  REAL-TIME:
    - Acknowledge P1 CRITICAL bias alerts within 2 hours
    - Authorize or reject model halt within 2 hours of CRITICAL detection
    - Authorize or reject model redeployment after CRITICAL resolution

  WEEKLY:
    - Review all HIGH severity findings and remediation progress
    - Review bias drift trend reports
    - Review false positive analysis

  MONTHLY:
    - Review platform_fairness_scorecard trend
    - Review false positive rate and calibrate if needed (threshold change process)
    - Review proxy feature risk map for all active models

  QUARTERLY:
    - Sign quarterly platform fairness report (mandatory — no signature = non-compliant)
    - Approve/reject ML Owner remediation plans for HIGH findings
    - Commission external fairness audit (annual — every 4th quarter)

  PRE-DEPLOYMENT:
    - Review all CONDITIONAL clearance certificates
    - Sign-off on any model deployment with unresolved MODERATE flags
    - Final authority on any disputed clearance/denial decision

  THRESHOLD GOVERNANCE:
    - Sole authority to approve bias threshold changes (with Compliance Admin co-sign)
    - Responsible for regulatory alignment of thresholds

  AUTHORITY LIMITS:
    - Safety Owner CANNOT override a CRITICAL model halt without also notifying
      Platform Compliance Admin
    - Safety Owner CANNOT issue a clearance for a CRITICAL-flagged model without
      independent technical review from ML Owner confirming remediation
```

---

## 🔐 SEAL DECLARATION

```
═══════════════════════════════════════════════════════════════════════════════════
AGENT NAME:    INTELLIGENCE_BIAS_DETECTION_AGENT
VERSION:       v1.0.0
THRESHOLD_POLICY_VERSION: tp-v1.0.0
SEAL DATE:     2026-02-25
PLATFORM:      ECOSKILLER ANTIGRAVITY
LANE:          F (Intelligence) + D (Governance) — DUAL LANE
OWNER CLASS:   ML · Intelligence · Safety
GOVERNANCE TIER: TIER-1 SAFETY CRITICAL

THIS AGENT SPECIFICATION IS:
  ✔ SEALED         — No interpretation permitted
  ✔ LOCKED         — No undeclared modifications permitted
  ✔ GOVERNED       — Subject to Safety Owner oversight at highest tier
  ✔ DETERMINISTIC  — Bias metrics computed mathematically, not heuristically
  ✔ AUDITABLE      — Every detection cycle produces immutable audit trail
  ✔ VERSIONED      — All changes require version bump + Safety Owner approval
  ✔ ISOLATED       — No raw PII, strict tenant isolation in all computations
  ✔ ALWAYS-ON      — Monitoring cannot be paused without Safety Owner authorization
  ✔ SELF-MONITORED — Detector monitors its own failure to prevent silent gaps

AUTHORITY HIERARCHY:
  This agent's CRITICAL findings cannot be overridden by:
    — Platform admins
    — Tenant admins
    — ML Owners
    — Product managers
    — Any automated agent
  They may only be resolved by:
    — Safety Owner (with Compliance Admin co-notification)
    — After confirmed technical remediation by ML Owner

Mutation: Add-only via version bump
Threshold changes: require Safety Owner + Compliance Admin written co-approval + 30 days notice
Violation of any non-negotiable rule → STOP EXECUTION → REPORT → ESCALATE TO SAFETY OWNER

═══════════════════════════════════════════════════════════════════════════════════
```

---

*End of INTELLIGENCE_BIAS_DETECTION_AGENT v1.0.0 — SEALED AND LOCKED*
