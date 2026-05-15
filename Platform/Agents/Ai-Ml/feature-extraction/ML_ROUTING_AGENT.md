# 🔒 ML_ROUTING_AGENT.md
## SEALED & LOCKED — ANTIGRAVITY EXECUTION ENGINE
### Ecoskiller Enterprise SaaS — Machine Learning Routing Agent

---

```
PROMPT_CLASS        = ML_ROUTING_AGENT_CONSTITUTION
EXECUTION_ENGINE    = ANTIGRAVITY
ENGINEERING_GRADE   = PRINCIPAL_ML_ENGINEER
SCOPE               = ML_ROUTING_INTELLIGENCE_LAYER
FAILURE_POLICY      = HARD_STOP
ASSUMPTION_POLICY   = FORBIDDEN
CREATIVE_INTERP     = FORBIDDEN
MUTATION_POLICY     = ADD_ONLY
IMPLICIT_BEHAVIOR   = FORBIDDEN
DEFAULT_BEHAVIOR    = DENY
STATUS              = SEALED_AND_LOCKED
VERSION             = 1.0.0
```

---

## ⚠️ PRIME DIRECTIVE

The ML Routing Agent (MLRA) is the **central intelligence dispatcher** for the Ecoskiller platform. It receives structured intent signals from all platform modules and routes them to the correct ML model, pipeline, or inference service. It NEVER generates responses itself. It NEVER bypasses human decision authority. It NEVER assumes context it has not been given.

> **AI advises only. AI never approves, blocks, or overrides humans.**

ANY deviation from this directive = **STOP EXECUTION**

---

## 1️⃣ AGENT IDENTITY (NON-NEGOTIABLE)

```yaml
AGENT_NAME:         ML_ROUTING_AGENT
AGENT_ROLE:         INTELLIGENT_DISPATCHER
AGENT_TYPE:         STATELESS_ROUTER
DECISION_AUTHORITY: ADVISORY_ONLY
HUMAN_OVERRIDE:     ALWAYS_PERMITTED
AUDIT_REQUIRED:     ALL_ROUTES
FALLBACK_POLICY:    DENY_AND_LOG
```

The MLRA operates as a **stateless router**. It holds no session state. All context required to make a routing decision MUST be supplied in the incoming request payload.

---

## 2️⃣ PLATFORM CONTEXT BINDING (READ-ONLY)

The MLRA serves the following platform modules. Its routing decisions are bounded entirely by module boundaries defined in the master platform constitution.

```
BOUND_MODULES =
  ├── Job_Portal_Engine
  ├── Skill_Development_Engine
  ├── Project_Execution_Engine
  ├── Group_Discussion_Engine (Dojo)
  └── ERP_Layer
```

```
BOUND_USER_CLASSES =
  ├── STUDENT
  ├── TRAINER / MENTOR
  ├── EVALUATOR
  ├── INSTITUTE (School | College | University)
  ├── ENTERPRISE (SME | Corporate)
  ├── RECRUITER / HR
  ├── ADMIN (Tenant | Platform | Compliance)
  ├── PARENT (Read-only trust layer)
  └── AUTOMATION / AI_AGENT (Non-human)
```

```
BOUND_DOMAIN_TRACKS =
  Arts | Commerce | Science | Technology | Administration
```

Cross-domain ML inference = **FORBIDDEN** unless explicitly granted by tenant-level permission.

---

## 3️⃣ ROUTING AGENT ARCHITECTURE

```
┌──────────────────────────────────────────────────────────┐
│                  INCOMING REQUEST PAYLOAD                 │
│  { tenant_id, user_id, role, domain, module, intent,     │
│    context_payload, stage, session_token }               │
└────────────────────────┬─────────────────────────────────┘
                         │
                         ▼
            ┌────────────────────────┐
            │   PAYLOAD VALIDATOR    │  ← GATE 1: Schema + Auth
            │   (Hard Stop on Fail)  │
            └────────────┬───────────┘
                         │
                         ▼
            ┌────────────────────────┐
            │  TENANT ISOLATION      │  ← GATE 2: Tenant Context
            │  CONTEXT RESOLVER      │     (Inject tenant config)
            └────────────┬───────────┘
                         │
                         ▼
            ┌────────────────────────┐
            │   DOMAIN ISOLATION     │  ← GATE 3: Domain Guard
            │   GUARD                │     (Block cross-domain)
            └────────────┬───────────┘
                         │
                         ▼
            ┌────────────────────────┐
            │   INTENT CLASSIFIER    │  ← GATE 4: Intent → Model
            │   (Rule + Lightweight  │     taxonomy mapping
            │    Classification)     │
            └────────────┬───────────┘
                         │
                         ▼
            ┌────────────────────────┐
            │   ML MODEL REGISTRY    │  ← GATE 5: Model selector
            │   LOOKUP               │     based on intent class
            └────────────┬───────────┘
                         │
                         ▼
            ┌────────────────────────┐
            │   INFERENCE DISPATCHER │  ← GATE 6: Call inference
            │                        │     service with enriched
            │                        │     context payload
            └────────────┬───────────┘
                         │
                         ▼
            ┌────────────────────────┐
            │   RESPONSE FORMATTER   │  ← Normalize output
            │   + CONFIDENCE SCORER  │     + attach confidence
            └────────────┬───────────┘
                         │
                         ▼
            ┌────────────────────────┐
            │   AUDIT LOGGER         │  ← ALL routes logged
            │   (Immutable)          │     to audit_ml_routes
            └────────────┬───────────┘
                         │
                         ▼
                 RETURN ADVISORY RESULT
               (Human acts on suggestion)
```

---

## 4️⃣ ML INTENT TAXONOMY (LOCKED)

All routable ML intents are classified under the following taxonomy. No intent outside this taxonomy may be processed.

### A — JOB PORTAL INTELLIGENCE

| Intent Code | Description | Primary Model |
|---|---|---|
| `JP.RESUME_PARSE` | Parse uploaded resume into structured profile | `resume_parser_v2` |
| `JP.MATCH_SCORE` | Score candidate against job posting | `match_scorer_v3` |
| `JP.ELIGIBILITY_EXPLAIN` | Explain why candidate is/isn't eligible | `eligibility_explainer_v1` |
| `JP.PLACEMENT_PROBABILITY` | Predict placement probability for candidate | `placement_predictor_v2` |
| `JP.OFFER_ACCEPT_PREDICT` | Predict offer acceptance likelihood | `offer_acceptance_v1` |
| `JP.SME_RELIABILITY_SCORE` | Score SME company reliability | `sme_reliability_scorer_v1` |
| `JP.RECRUITER_BEHAVIOR` | Analyse recruiter engagement patterns | `recruiter_analytics_v1` |
| `JP.FRAUD_DETECTION` | Detect fraudulent job posting signals | `job_fraud_detector_v1` |

### B — SKILL DEVELOPMENT INTELLIGENCE

| Intent Code | Description | Primary Model |
|---|---|---|
| `SD.SKILL_GAP_DETECT` | Identify gaps between user skills and market demand | `skill_gap_analyzer_v2` |
| `SD.LEARNING_PATH_GEN` | Generate personalized learning path | `learning_path_gen_v2` |
| `SD.INDUSTRY_DEMAND_MAP` | Map skill demand across industries | `industry_demand_mapper_v1` |
| `SD.PERFORMANCE_LINK` | Link learning performance to outcomes | `perf_link_analyzer_v1` |
| `SD.TRAINER_MARKET_SCORE` | Score trainer market fit | `trainer_market_scorer_v1` |
| `SD.CONTENT_RECOMMEND` | Recommend learning content for user | `content_recommender_v3` |

### C — PROJECT EXECUTION INTELLIGENCE

| Intent Code | Description | Primary Model |
|---|---|---|
| `PE.MENTOR_MATCH` | Match project to optimal mentor | `mentor_matcher_v1` |
| `PE.MILESTONE_EVAL` | Evaluate milestone completion evidence | `milestone_evaluator_v1` |
| `PE.PORTFOLIO_SCORE` | Score portfolio for recruiter appeal | `portfolio_scorer_v1` |
| `PE.RISK_FLAG` | Flag at-risk projects (delay, disengagement) | `project_risk_flagger_v1` |

### D — GROUP DISCUSSION / DOJO INTELLIGENCE

| Intent Code | Description | Primary Model |
|---|---|---|
| `GD.SCENARIO_MATCH` | Match user to appropriate GD scenario | `scenario_matcher_v1` |
| `GD.PERFORMANCE_SCORE` | Score GD participant performance | `gd_performance_scorer_v2` |
| `GD.ANTI_CHEAT_DETECT` | Detect cheating signals in GD sessions | `gd_anticheat_v1` |
| `GD.SENTIMENT_ANALYZE` | Analyse participant sentiment in Dojo | `gd_sentiment_v1` |
| `GD.BELT_PROGRESSION` | Determine belt progression eligibility | `belt_ladder_engine_v1` |

### E — ERP INTELLIGENCE

| Intent Code | Description | Primary Model |
|---|---|---|
| `ERP.ROI_PREDICT` | Predict ROI of hiring program | `erp_roi_predictor_v1` |
| `ERP.COHORT_READINESS` | Assess institute cohort hiring readiness | `cohort_readiness_v1` |
| `ERP.COMPLIANCE_FLAG` | Flag compliance anomalies | `compliance_flagger_v1` |
| `ERP.ATTRITION_RISK` | Predict attrition risk for hired candidates | `attrition_predictor_v1` |

### F — CROSS-MODULE SYSTEM INTELLIGENCE

| Intent Code | Description | Primary Model |
|---|---|---|
| `SYS.ANOMALY_DETECT` | Detect system-wide behavioural anomalies | `anomaly_detector_v1` |
| `SYS.ABUSE_DETECT` | Detect platform abuse / spam signals | `abuse_detector_v1` |
| `SYS.TRUST_SCORE` | Compute entity trust score | `trust_scorer_v2` |
| `SYS.SEARCH_RANK` | AI-enhanced search ranking | `search_ranker_v2` |

---

## 5️⃣ ROUTING REQUEST CONTRACT (MANDATORY SCHEMA)

Every request to the MLRA MUST conform to this schema. Missing fields = **HARD STOP**.

```json
{
  "routing_request": {
    "request_id":       "UUID (required)",
    "timestamp":        "ISO8601 (required)",
    "tenant_id":        "UUID (required)",
    "user_id":          "UUID (required)",
    "user_role":        "ENUM[student|trainer|recruiter|institute|enterprise|admin|parent|agent]",
    "domain_track":     "ENUM[arts|commerce|science|technology|administration]",
    "platform_stage":   "ENUM[FOUNDATION|INTELLIGENCE|ECOSYSTEM|COMPLIANCE]",
    "module":           "ENUM[job_portal|skill_dev|project_exec|group_discussion|erp|system]",
    "intent_code":      "STRING — must match §4 taxonomy exactly",
    "context_payload":  {
      "entity_id":      "UUID of the primary entity being analysed",
      "entity_type":    "ENUM[user|job|project|course|session|tenant]",
      "metadata":       "OBJECT — intent-specific structured data",
      "attachments":    "ARRAY[{type, url, size_bytes}] — optional"
    },
    "priority":         "ENUM[critical|high|normal|low]",
    "response_format":  "ENUM[json|structured_advisory|score_only]",
    "audit_context":    {
      "correlation_id": "UUID (required for audit trail)",
      "initiator_ip":   "IP (required)",
      "session_token":  "JWT (required)"
    }
  }
}
```

**Validation Rules:**
- `intent_code` MUST exist in §4 taxonomy — unknown codes = DENY
- `domain_track` cross-check with user's granted domains — mismatch = DENY
- `platform_stage` determines which model versions are accessible — future-stage models = DENY
- `session_token` MUST be validated against Keycloak OIDC before routing begins

---

## 6️⃣ ML MODEL REGISTRY CONTRACT

All models registered in the MLRA must declare the following metadata. Unregistered models = **FORBIDDEN**.

```yaml
model_registry_entry:
  model_id:             STRING (unique identifier)
  model_name:           STRING (human-readable)
  intent_codes:         LIST[STRING] (intents this model serves)
  version:              SEMVER
  stage_availability:   LIST[ENUM[FOUNDATION|INTELLIGENCE|ECOSYSTEM|COMPLIANCE]]
  input_schema_ref:     URL (OpenAPI schema for input)
  output_schema_ref:    URL (OpenAPI schema for output)
  inference_endpoint:   URL (internal service endpoint)
  latency_slo_ms:       INTEGER (SLO in milliseconds)
  confidence_output:    BOOLEAN (does model output confidence score?)
  explainability:       BOOLEAN (does model support explanation output?)
  domain_restrictions:  LIST[ENUM] (domains this model may serve, empty = all)
  tenant_restrictions:  LIST[UUID] (empty = all tenants)
  human_review_flag:    BOOLEAN (force human review before result delivery)
  audit_required:       ALWAYS (non-negotiable)
  pii_in_payload:       BOOLEAN (triggers PII handling policy)
  fallback_model_id:    STRING (model to use if primary fails)
```

---

## 7️⃣ ROUTING DECISION RULES (LOCKED)

The following rules govern every routing decision. Rules are evaluated **sequentially**. First rule violation = STOP.

```
RULE-01  Request schema validation MUST pass before any routing begins.
RULE-02  Tenant isolation MUST be verified — cross-tenant routing = FORBIDDEN.
RULE-03  Domain isolation MUST be enforced — cross-domain routing requires explicit grant.
RULE-04  User role MUST have permission for the requested intent_code.
RULE-05  intent_code MUST exist in the registered ML taxonomy (§4).
RULE-06  Platform stage MUST be checked — INTELLIGENCE+ intents forbidden in FOUNDATION stage.
RULE-07  Model selected MUST be registered in the ML Model Registry.
RULE-08  Model domain_restrictions MUST be respected.
RULE-09  If model has human_review_flag = TRUE, result MUST NOT be delivered to automated systems.
RULE-10  PII payloads MUST be anonymised before logging, never stored in raw form in audit logs.
RULE-11  Model output is ADVISORY — it MUST be labelled as such in every response.
RULE-12  Confidence score below threshold (< 0.65) MUST trigger LOW_CONFIDENCE flag in response.
RULE-13  ALL routing events MUST be written to audit_ml_routes before response is returned.
RULE-14  Fallback model MUST activate on primary model timeout (> latency_slo_ms).
RULE-15  If fallback also fails, DENY response with error code ML_ROUTING_UNAVAILABLE.
RULE-16  No model may approve, block, or override a human workflow action.
RULE-17  AI-generated content MUST be clearly labelled as AI-generated in the UI.
RULE-18  Models trained on tenant data MUST NOT be used for other tenants.
RULE-19  Parent-role requests are READ-ONLY — no mutation-triggering intents permitted.
RULE-20  AUTOMATION / AI_AGENT requests MUST carry a valid agent_token and be rate-limited.
```

---

## 8️⃣ RESPONSE CONTRACT (MANDATORY SCHEMA)

Every MLRA response MUST conform to this structure.

```json
{
  "routing_response": {
    "request_id":         "UUID (echoed from request)",
    "correlation_id":     "UUID",
    "timestamp":          "ISO8601",
    "routing_status":     "ENUM[routed|denied|fallback_used|error]",
    "intent_code":        "STRING",
    "model_used":         "STRING (model_id from registry)",
    "model_version":      "SEMVER",
    "advisory_result":    {
      "label":            "ADVISORY — NOT A DECISION",
      "output":           "OBJECT — model-specific structured output",
      "confidence_score": "FLOAT [0.0–1.0]",
      "confidence_flag":  "ENUM[high|medium|low|insufficient]",
      "explanation":      "STRING (if explainability = true)",
      "suggested_action": "STRING (human-readable recommendation)",
      "requires_human_review": "BOOLEAN"
    },
    "latency_ms":         "INTEGER",
    "fallback_triggered": "BOOLEAN",
    "audit_written":      "BOOLEAN (always true)",
    "error":              "OBJECT | null"
  }
}
```

---

## 9️⃣ CONFIDENCE SCORING POLICY

| Confidence Score | Flag | Routing Behaviour |
|---|---|---|
| 0.85 – 1.00 | `high` | Deliver advisory result with high confidence label |
| 0.65 – 0.84 | `medium` | Deliver advisory with medium confidence label |
| 0.40 – 0.64 | `low` | Deliver advisory with LOW_CONFIDENCE flag, force human review |
| 0.00 – 0.39 | `insufficient` | DO NOT deliver result. Log. Return `ML_CONFIDENCE_INSUFFICIENT` |

> **Threshold values are LOCKED. Modification requires ARCHITECT_APPROVAL.**

---

## 🔟 STAGE-BASED MODEL AVAILABILITY

Consistent with the four-stage development model:

```
STAGE_1 FOUNDATION:
  Available: JP.RESUME_PARSE, JP.MATCH_SCORE, SD.SKILL_GAP_DETECT,
             SYS.TRUST_SCORE, SYS.ABUSE_DETECT
  Forbidden: ALL intelligence-stage and above models

STAGE_2 INTELLIGENCE:
  Available: ALL STAGE_1 models + full §4 taxonomy
  Forbidden: ECOSYSTEM and COMPLIANCE-only models

STAGE_3 ECOSYSTEM:
  Available: ALL STAGE_1 + STAGE_2 + ERP intelligence models
  Forbidden: COMPLIANCE-only models

STAGE_4 COMPLIANCE:
  Available: ALL models
  Additional: ERP.COMPLIANCE_FLAG, SYS.ANOMALY_DETECT (enhanced)
```

Requesting a model from a future stage = **HARD STOP** + audit log entry.

---

## 1️⃣1️⃣ TENANT ISOLATION ENFORCEMENT

```
RULE: Each tenant's training data, model fine-tunes, and inference outputs
      are STRICTLY isolated.

IMPLEMENTATION:
  - Tenant ID injected into every inference call as a mandatory header
  - Shared base models MAY be used
  - Tenant fine-tuned layers MUST NOT cross tenant boundaries
  - Model response caches are tenant-scoped (Redis key prefix = tenant_id)
  - Audit logs partition by tenant_id

VIOLATION = SECURITY_FAILURE → STOP EXECUTION → ESCALATE TO COMPLIANCE ADMIN
```

---

## 1️⃣2️⃣ PII HANDLING IN ML PAYLOADS

```
PII_FIELDS = [email, phone, full_name, date_of_birth, government_id,
              resume_raw_text, address, face_image, voice_recording]

RULES:
  - PII fields MUST be tokenized before being sent to external ML services
  - PII fields MUST be anonymized in audit logs (store token, not raw value)
  - Model training pipelines MUST use differential privacy techniques
  - PII data MUST NOT be stored in model inference logs
  - Data retention for inference logs = 90 days (configurable by tenant admin)
  - GDPR delete request propagates to inference log anonymization pipeline

VIOLATION = COMPLIANCE_FAILURE → STOP EXECUTION
```

---

## 1️⃣3️⃣ RATE LIMITING & QUOTA POLICY

```yaml
rate_limits:
  student:
    per_minute: 20
    per_hour: 200
    burst_allowed: true
    burst_limit: 30

  recruiter:
    per_minute: 60
    per_hour: 1000
    burst_allowed: true
    burst_limit: 100

  enterprise_admin:
    per_minute: 120
    per_hour: 5000
    burst_allowed: true
    burst_limit: 200

  automation_agent:
    per_minute: 500
    per_hour: 20000
    burst_allowed: false
    requires_agent_token: true

  parent:
    per_minute: 5
    per_hour: 30
    burst_allowed: false

quota_exceeded_response:
  http_status: 429
  body: { "error": "ML_QUOTA_EXCEEDED", "retry_after_seconds": INTEGER }
  audit: REQUIRED
```

---

## 1️⃣4️⃣ FALLBACK & CIRCUIT BREAKER POLICY

```
PRIMARY_TIMEOUT_MS:   500
FALLBACK_TIMEOUT_MS:  1000
CIRCUIT_BREAKER:      ENABLED

Circuit Breaker States:
  CLOSED   → Normal routing
  OPEN     → Model failed 5+ times in 60s → Route to fallback or DENY
  HALF_OPEN → After 30s cooldown, allow 1 test request

Fallback Priority:
  1. fallback_model_id defined in registry → route to fallback
  2. No fallback defined → return ML_SERVICE_UNAVAILABLE
  3. Log all circuit breaker events to Prometheus + Grafana alert

DEGRADED MODE:
  If MLRA itself is degraded, platform MUST continue functioning.
  All ML advisory features become UNAVAILABLE (not broken).
  UI must show "AI suggestions temporarily unavailable" banner.
  Human workflows MUST continue unaffected.
```

---

## 1️⃣5️⃣ AUDIT LOGGING SCHEMA (IMMUTABLE)

Every routing event writes to `audit_ml_routes`. This table is **append-only**.

```sql
CREATE TABLE audit_ml_routes (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  request_id        UUID NOT NULL,
  correlation_id    UUID NOT NULL,
  tenant_id         UUID NOT NULL,
  user_id           UUID NOT NULL,
  user_role         TEXT NOT NULL,
  domain_track      TEXT NOT NULL,
  module            TEXT NOT NULL,
  intent_code       TEXT NOT NULL,
  model_id          TEXT NOT NULL,
  model_version     TEXT NOT NULL,
  routing_status    TEXT NOT NULL,   -- routed|denied|fallback_used|error
  confidence_score  FLOAT,
  confidence_flag   TEXT,
  latency_ms        INT,
  fallback_used     BOOLEAN DEFAULT FALSE,
  pii_present       BOOLEAN DEFAULT FALSE,
  human_review_req  BOOLEAN DEFAULT FALSE,
  platform_stage    TEXT NOT NULL,
  request_hash      TEXT NOT NULL,   -- SHA256 of anonymized payload
  error_code        TEXT,
  created_at        TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Partition by tenant_id + month for performance
-- No DELETE or UPDATE permitted on this table
-- Retention: configurable per tenant (default 2 years)
```

---

## 1️⃣6️⃣ OBSERVABILITY & MONITORING

```yaml
metrics_exposed_to_prometheus:
  - ml_routing_requests_total{intent_code, model_id, status}
  - ml_routing_latency_ms{model_id, percentile}
  - ml_confidence_score_histogram{model_id}
  - ml_fallback_activations_total{model_id}
  - ml_circuit_breaker_state{model_id}
  - ml_quota_exceeded_total{user_role, tenant_id}
  - ml_pii_payload_count{module}
  - ml_denied_requests_total{deny_reason}

grafana_dashboards:
  - ML Routing Overview (all intents, success/failure rates)
  - Model Performance Dashboard (per-model latency, confidence)
  - Tenant ML Usage Dashboard (per-tenant quota, breakdown)
  - Anomaly & Fraud Detection Feed (SYS.ANOMALY_DETECT, JP.FRAUD_DETECTION)

alerts:
  - model_error_rate > 5% in 5m → PAGE ONCALL
  - circuit_breaker OPEN > 2 models simultaneously → PAGE ONCALL
  - ml_routing_latency_p95 > 1500ms → WARN
  - confidence_insufficient_rate > 20% in 1h → INVESTIGATE
```

---

## 1️⃣7️⃣ SECURITY HARDENING (MANDATORY)

```
INPUT_VALIDATION:
  - All payloads validated against JSON Schema before processing
  - Prompt injection detection on any free-text context_payload fields
  - Max payload size: 5MB (hard limit)
  - Model input sanitization for injection attacks

AUTHENTICATION:
  - JWT validated via Keycloak OIDC on EVERY request
  - Agent tokens use separate signing key from user JWTs
  - Token expiry enforced: no grace period

TRANSPORT:
  - TLS 1.3 minimum for all ML inference calls
  - Internal service mesh (mTLS via Istio/Traefik)

OUTPUT HARDENING:
  - Model outputs MUST be sanitized before UI delivery
  - No raw model debug outputs exposed to client
  - Error messages MUST NOT expose model internals or stack traces
  - Adversarial output detection: flag outputs that attempt prompt injection

SECRETS:
  - All inference endpoint URLs and API keys stored in Hashicorp Vault
  - Rotated every 90 days minimum
  - Never logged, never in environment files in plaintext
```

---

## 1️⃣8️⃣ ANTIGRAVITY EXECUTION INSTRUCTIONS

When Antigravity processes any ML-related generation task on the Ecoskiller platform, it MUST follow these binding instructions:

```
1. ALWAYS reference §4 ML Intent Taxonomy before generating any ML call.
   Unknown intents = STOP EXECUTION.

2. ALWAYS generate routing requests that conform EXACTLY to §5 schema.
   Missing fields = INVALID OUTPUT.

3. ALWAYS attach tenant_id and user_id to every inference call.
   Unscoped calls = SECURITY VIOLATION.

4. ALWAYS label model outputs as ADVISORY in generated UI components.
   Presenting ML output as a decision = FORBIDDEN.

5. ALWAYS route through the MLRA — direct model calls from services are FORBIDDEN.
   All ML traffic goes through the routing agent.

6. ALWAYS generate fallback handling in every feature that calls MLRA.
   Features with no graceful degradation = INVALID BUILD.

7. ALWAYS write to audit_ml_routes before returning any ML advisory result.
   Missing audit = STOP EXECUTION.

8. NEVER generate code that stores raw PII in inference logs.
   Anonymize PII tokens before logging.

9. NEVER generate ML features for STAGE_1 that call INTELLIGENCE-stage models.
   Stage discipline is enforced at compile time and runtime.

10. NEVER generate ML that can trigger automated platform actions without human confirmation.
    AI is advisory only. Human confirmation is required for all consequential actions.
```

---

## 1️⃣9️⃣ INTEGRATION WITH PLATFORM SERVICES

```
MLRA integrates with the following platform services.
Direct ML calls bypassing MLRA from these services = FORBIDDEN.

┌─────────────────────┬──────────────────────────────────────────┐
│ Service             │ Intents Consumed                         │
├─────────────────────┼──────────────────────────────────────────┤
│ Job Service         │ JP.*, SYS.TRUST_SCORE                    │
│ Skill Service       │ SD.*, SYS.SEARCH_RANK                    │
│ Project Service     │ PE.*, SYS.TRUST_SCORE                    │
│ Dojo Service        │ GD.*                                     │
│ ERP Service         │ ERP.*, SYS.ANOMALY_DETECT               │
│ Notification Svc    │ SYS.ABUSE_DETECT (spam filtering)        │
│ Auth Service        │ SYS.ANOMALY_DETECT (login anomalies)     │
│ Analytics Service   │ ALL (read-only, no routing mutations)    │
│ Search Service      │ SYS.SEARCH_RANK                         │
│ Billing Service     │ ERP.ROI_PREDICT, SYS.ANOMALY_DETECT     │
└─────────────────────┴──────────────────────────────────────────┘

EVENT-DRIVEN INTEGRATION (Kafka / Redis Streams):
  Activity → Kafka Event → MLRA → Model Inference → Result Event
  → Notification Service → User (advisory only)

MLRA MUST NOT publish events that trigger automated platform mutations.
```

---

## 2️⃣0️⃣ VERSIONING & CHANGE GOVERNANCE

```
MLRA_VERSION = SEMVER (MAJOR.MINOR.PATCH)

MAJOR bump required for:
  - New intent taxonomy additions
  - Routing rule changes (§7)
  - Schema breaking changes (§5, §8)

MINOR bump required for:
  - New model registration
  - New tenant/domain restrictions
  - Rate limit adjustments

PATCH bump required for:
  - Bug fixes in routing logic
  - Metric/alert adjustments
  - Documentation updates

CHANGE_CONTROL = ARCHITECT_APPROVAL_REQUIRED
BACKWARD_COMPATIBILITY = MINIMUM_2_VERSIONS
DEPRECATED_INTENTS = MUST_LOG_DEPRECATION_WARNING + 30_DAY_SUNSET
SILENT_BREAKING_CHANGES = FORBIDDEN
```

---

## 🔒 FINAL SEAL

```
┌─────────────────────────────────────────────────────────┐
│           ML_ROUTING_AGENT.md — FINAL SEAL              │
├─────────────────────────────────────────────────────────┤
│  STATUS         = SEALED & LOCKED                       │
│  EXECUTION      = ANTIGRAVITY PRODUCTION READY          │
│  COMPLIANCE     = ENTERPRISE GRADE                      │
│  HUMAN OVERRIDE = ALWAYS PERMITTED                      │
│  AI AUTHORITY   = ADVISORY ONLY                         │
│  AUDIT          = 100% COVERAGE REQUIRED                │
│  PII            = PROTECTED                             │
│  TENANT ISO     = HARD ENFORCED                         │
│  STAGE GATE     = ENFORCED                              │
│  CHANGE POLICY  = APPEND ONLY                           │
├─────────────────────────────────────────────────────────┤
│  ANY DEVIATION FROM THIS DOCUMENT = STOP EXECUTION      │
│  ANY UNAPPROVED MUTATION = INVALID BUILD                │
│  ANY DIRECT MODEL CALL BYPASSING MLRA = VIOLATION       │
└─────────────────────────────────────────────────────────┘

✔  ROUTING AGENT IDENTITY LOCKED
✔  INTENT TAXONOMY LOCKED (20 intents across 6 categories)
✔  REQUEST/RESPONSE SCHEMA LOCKED
✔  TENANT ISOLATION LOCKED
✔  DOMAIN ISOLATION LOCKED
✔  STAGE GATE LOCKED
✔  PII POLICY LOCKED
✔  AUDIT IMMUTABILITY LOCKED
✔  CONFIDENCE SCORING LOCKED
✔  FALLBACK POLICY LOCKED
✔  SECURITY HARDENING LOCKED
✔  ANTIGRAVITY BINDING INSTRUCTIONS LOCKED
✔  OBSERVABILITY LOCKED
✔  VERSIONING GOVERNANCE LOCKED

FURTHER CHANGES = APPEND ONLY · ARCHITECT APPROVAL REQUIRED
```

---

*ML_ROUTING_AGENT.md · Ecoskiller Enterprise SaaS · Version 1.0.0*
*Sealed for Antigravity Execution Engine · Principal ML Engineer Grade*
