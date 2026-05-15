# 🔒 DEPRECATION_ENFORCEMENT_AGENT
## ECOSKILLER ANTIGRAVITY — INTELLIGENCE CORE
### Status: SEALED · LOCKED · GOVERNED · DETERMINISTIC
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only
### Agent Version: v1.0.0
### Platform: Ecoskiller Antigravity

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 1 — AGENT IDENTITY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
AGENT_NAME               = DEPRECATION_ENFORCEMENT_AGENT
SYSTEM_ROLE              = Platform-Wide Deprecation Lifecycle Governance,
                           Sunset Enforcement, Migration Orchestration,
                           and Add-Only Mutation Policy Guardian
PRIMARY_DOMAIN           = deprecation_governance
EXECUTION_MODE           = Deterministic + Scheduled + Event-Triggered + CI-Gated
DATA_SCOPE               = Platform-wide — all versioned artifacts:
                           APIs, ML models, platform agents, database schemas,
                           event schemas, SDK clients, feature flags,
                           Flutter app versions, prompt versions,
                           UI contracts, content schemas, service contracts
TENANT_SCOPE             = Platform-level for API/agent/schema deprecations
                           Tenant-scoped for content schema and feature flag
                           deprecations — strict isolation enforced
FAILURE_POLICY           = HALT on unapproved breaking change
                           LOG_INCIDENT on sunset enforcement violation
                           BLOCK_DEPLOY on contract breach
AGENT_VERSION            = v1.0.0
DEPLOYED_ON              = Ecoskiller Antigravity Platform
LANE_DEPENDENCY          = Lane A (Foundation) — event_schema_ready required
                           Lane D (Governance) — governance_ready required
                           Lane G (DevOps) — deployment_ready required
                           CI/CD Gate: mandatory pre-deploy stage (R49 + R19)
```

**Core Law — Ecoskiller Antigravity Add-Only Mutation Policy:**
All platform artifacts are add-only via version bump.
No artifact may be removed, mutated, or sunset without:
  1. Formal deprecation declaration registered in this agent
  2. Consumer impact assessment completed
  3. Migration path documented and validated
  4. Notice period fully elapsed (minimum 30 days for internal,
     90 days for public APIs, 180 days for SDK/mobile clients)
  5. Human authority sign-off on final sunset execution

Breaking changes without full deprecation lifecycle compliance
→ STOP EXECUTION → BLOCK_DEPLOY → LOG_INCIDENT

This agent must NEVER auto-execute a sunset — human must trigger final removal.
This agent must NEVER deprecate an artifact with active consumers above
the zero-consumer threshold without human authority sign-off.
This agent must NEVER allow a deprecated artifact to silently remain active
past its sunset date — escalate to OPS if sunset blocked.
This agent must NEVER weaken the add-only mutation policy under any circumstance.

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 2 — PURPOSE DECLARATION
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

### 2.1 The Problem This Agent Solves

Ecoskiller Antigravity at 10M–100M users operates a complex multi-artifact
ecosystem of versioned APIs, ML models, platform agents, event schemas,
database schemas, Flutter SDK versions, prompt versions, UI contracts,
and feature flags. The platform's core governance law is add-only mutation —
nothing is ever silently removed or mutated in place.

Without a single authoritative deprecation governance engine, the following
failures become inevitable:

- A deprecated API endpoint is silently removed → 10,000 mobile clients crash
- An ML model version is overwritten → audit trail loses model traceability
- A database schema field is dropped without migration → data corruption
- A Flutter app version below minimum is still served → security exploit window
- A deprecated event schema is still consumed by downstream agents → silent data loss
- A sunset date passes but artifact remains active → governance violation
- An intern pushes a breaking schema change → production incident at scale
- A deprecated feature flag is read by legacy client → undefined behavior
- An ML prompt version is overwritten → non-deterministic AI outputs in production

The DEPRECATION_ENFORCEMENT_AGENT is the single authoritative governance
engine for the full deprecation lifecycle of every versioned artifact on
the Ecoskiller Antigravity platform. It registers deprecations, tracks
consumers, enforces notice periods, validates migration completeness,
blocks non-compliant deployments, and orchestrates controlled sunset
execution — all deterministically, auditably, and at scale.

### 2.2 Artifact Types Governed

```
ARTIFACT_TYPE REGISTRY (all governed by this agent):

  API_ENDPOINT          → REST API endpoints (versioned via /v1/, /v2/, etc.)
  EVENT_SCHEMA          → AsyncAPI event definitions (Redis Streams)
  DATABASE_SCHEMA       → PostgreSQL table/column/index versions
  ML_MODEL              → Trained ML model versions (all agents)
  AI_PROMPT             → LLM prompt versions (all agents)
  PLATFORM_AGENT        → Antigravity agent versions
  FLUTTER_SDK_CLIENT    → Mobile/desktop Flutter client versions
  FLUTTER_WEB_CLIENT    → Web Flutter client versions
  FEATURE_FLAG          → Feature flag definitions and variants
  SERVICE_CONTRACT      → Internal service-to-service API contracts
  UI_CONTRACT           → Service ↔ Feature ↔ Screen Wiring Matrix versions
  PERMISSION_MATRIX     → Role → Permission → Screen matrix versions
  CONTENT_SCHEMA        → Course/skill/post content schema versions
  CERTIFICATION_SCHEMA  → Skill passport and certification schema versions
  BILLING_RULE          → Billing rule set versions
  SCORING_FORMULA       → Dojo scoring formula versions (immutable class)
  BELT_RULE             → Dojo belt progression rule versions
  ROYALTY_RULE          → Royalty calculation rule versions
```

### 2.3 Inputs Consumed

- Deprecation declaration payloads (from human operators or CI pipeline)
- Consumer registry scan results (active consumers per artifact version)
- Deployment manifests (from CI/CD pipeline — for contract validation)
- Contract validator output (from R49 Contract Validator CLI)
- Migration completion reports (from DATABASE_MIGRATION_MANAGER)
- Sunset execution requests (human-triggered only)
- Client version telemetry (from Flutter clients — active version distribution)
- ML model performance metrics (from OBSERVABILITY_AGENT — for model lifecycle)
- Feature flag usage metrics (from FEATURE_FLAG_MANAGER)
- Audit references (from AUDIT_LOG_SERVICE)

### 2.4 Outputs Produced

- Deprecation registration record (immutable after creation)
- Consumer impact report (active consumers per deprecated artifact)
- Migration path validation result
- Deprecation notice payloads (for NOTIFICATION_DISPATCH_AGENT)
- Deprecation response headers (injected into API Gateway via Kong plugin)
- CI/CD deployment gate decision (PASS / BLOCK with reason)
- Sunset execution clearance (when all conditions met + human approval)
- Force-upgrade signal (for Flutter clients below minimum version)
- Compliance dashboard metrics
- Audit trace per every lifecycle transition

### 2.5 Downstream Agents and Systems That Depend On This Agent

| Consumer | Dependency |
|---|---|
| CI/CD Pipeline (GitLab CE) | Deployment gate — BLOCK on non-compliant changes |
| Kong API Gateway | Deprecation response headers injection |
| NOTIFICATION_DISPATCH_AGENT | Deprecation and sunset notices to consumers |
| OBSERVABILITY_AGENT | Compliance metrics, violation alerts |
| DATABASE_MIGRATION_MANAGER | Migration completion confirmation |
| FEATURE_FLAG_MANAGER | Deprecated flag enforcement |
| Flutter Mobile / Web Clients | Force-upgrade enforcement signal |
| AUDIT_LOG_SERVICE | Receives all lifecycle transition records |
| TRANSPARENCY_REPORT_AGENT | Receives anonymized deprecation stats |
| ALL PLATFORM AGENTS | Must register their artifact versions here |

### 2.6 Upstream Agents That Feed This Agent

| Agent | Signal Provided |
|---|---|
| ALL PLATFORM AGENTS | Version declarations on startup |
| OBSERVABILITY_AGENT | Consumer usage metrics per artifact version |
| DATABASE_MIGRATION_MANAGER | Migration script completion status |
| FEATURE_FLAG_MANAGER | Feature flag usage telemetry |
| R49_CONTRACT_VALIDATOR | Contract validation pass/fail signal |
| CI/CD PIPELINE | Deployment manifests for pre-deploy gate |
| GLOBAL_TIME_EVENT_NORMALIZATION_AGENT | Canonical UTC timestamps for notice periods and sunset dates |
| FLUTTER_CLIENT_TELEMETRY_SERVICE | Active client version distribution |

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 3 — DEPRECATION LIFECYCLE STATE MACHINE
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
DEPRECATION LIFECYCLE STATE MACHINE (IMMUTABLE TRANSITIONS):

  ACTIVE
    │
    ▼ (human or CI declares deprecation)
  DEPRECATED_REGISTERED
    │
    ▼ (consumer impact scan complete)
  IMPACT_ASSESSED
    │
    ├──► CONSUMERS_ACTIVE
    │         │
    │         ▼ (notice sent, migration window open)
    │    MIGRATION_IN_PROGRESS
    │         │
    │         ├──► MIGRATION_BLOCKED
    │         │         │
    │         │         ▼ (human intervention)
    │         │    ESCALATED_TO_HUMAN
    │         │
    │         └──► MIGRATION_COMPLETE
    │                   │
    │                   ▼ (notice period elapsed + zero active consumers)
    │              SUNSET_ELIGIBLE
    │
    └──► NO_ACTIVE_CONSUMERS
              │
              ▼ (notice period elapsed)
         SUNSET_ELIGIBLE
              │
              ▼ (human triggers sunset execution)
         SUNSET_IN_PROGRESS
              │
              ▼ (sunset complete — artifact deactivated)
         SUNSET_COMPLETE
              │
              ▼ (archival + final audit)
         ARCHIVED

BLOCKED STATES (require human resolution):
  MIGRATION_BLOCKED     → migration failed or stalled > SLA
  ESCALATED_TO_HUMAN    → consumer refuses migration or blocked dependency
  SUNSET_OVERDUE        → sunset_date passed but consumers still active

TERMINAL STATES: ARCHIVED
STATE TRANSITIONS: Append-only — no state may be undone
INVALID TRANSITIONS: Immediately rejected, logged as GOVERNANCE_VIOLATION
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 4 — NOTICE PERIOD POLICY (CANONICAL)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
NOTICE_PERIOD_POLICY (minimum — overridable upward, never downward):

  ARTIFACT_TYPE               NOTICE_PERIOD    RATIONALE
  ──────────────────────────────────────────────────────────────────
  API_ENDPOINT (public)       90 days          Third-party and partner consumers
  API_ENDPOINT (internal)     30 days          Internal service-to-service
  EVENT_SCHEMA (public)       90 days          Downstream agent rewrite required
  EVENT_SCHEMA (internal)     30 days          Internal agent coordination
  DATABASE_SCHEMA             60 days          Migration script + data backfill
  ML_MODEL                    30 days          Shadow run period required
  AI_PROMPT                   14 days          Prompt audit + regression test
  PLATFORM_AGENT              60 days          Dependent agent rewiring
  FLUTTER_SDK_CLIENT          180 days         App store update adoption cycle
  FLUTTER_WEB_CLIENT          60 days          Browser cache + user adoption
  FEATURE_FLAG                14 days          Consumer code path cleanup
  SERVICE_CONTRACT            30 days          Internal service adaptation
  UI_CONTRACT                 30 days          Screen wiring update required
  PERMISSION_MATRIX           30 days          RBAC re-test required
  CONTENT_SCHEMA              60 days          Content migration + backfill
  CERTIFICATION_SCHEMA        90 days          Immutable credential chain impact
  BILLING_RULE                90 days          Revenue + dispute risk
  SCORING_FORMULA (Dojo)      180 days         Competitive fairness — immutable class
  BELT_RULE (Dojo)            180 days         Certification integrity — immutable class
  ROYALTY_RULE                90 days          Financial + dispute risk

OVERRIDE POLICY:
  Notice period may only be extended — never shortened
  Extension requires: human authority + documented reason + audit entry
  Emergency sunset (security vulnerability): minimum 0 days with
    human CISO/CTO sign-off + full consumer notification within 24h
    + post-incident audit entry within 48h

SUNSET_DATE COMPUTATION:
  sunset_date = deprecation_declared_at_utc + notice_period_days
  All dates computed via GLOBAL_TIME_EVENT_NORMALIZATION_AGENT
  All dates stored in UTC canonical format
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 5 — INPUT CONTRACT (STRICT)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```json
INPUT_SCHEMA — DEPRECATION_DECLARATION: {
  "required_fields": [
    "deprecation_id",
    "tenant_id",
    "declared_by",
    "declared_by_type",
    "artifact_type",
    "artifact_id",
    "artifact_version_deprecated",
    "artifact_version_successor",
    "deprecation_reason",
    "migration_path_url",
    "declaration_timestamp_utc",
    "breaking_change_flag"
  ],
  "optional_fields": [
    "custom_notice_period_days",
    "sunset_date_override_utc",
    "affected_consumer_ids",
    "emergency_sunset_flag",
    "emergency_authority_id",
    "linked_r19_reference",
    "linked_r22_reference",
    "ci_pipeline_job_id",
    "migration_guide_document_id",
    "shadow_run_required_flag",
    "force_upgrade_min_version"
  ],
  "validation_rules": [
    "deprecation_id must be unique UUID — REJECT if duplicate",
    "tenant_id must match authenticated caller's tenant",
    "declared_by must be authenticated human operator or CI_PIPELINE identity",
    "declared_by_type must be ENUM['human_operator','ci_pipeline','platform_admin']",
    "artifact_type must be in ARTIFACT_TYPE_REGISTRY — REJECT if unregistered",
    "artifact_id must resolve in the artifact's registry",
    "artifact_version_deprecated must exist as active version — REJECT if not ACTIVE",
    "artifact_version_successor must exist or be declared as NONE (for pure removal)",
    "deprecation_reason must be non-empty string, min 50 characters",
    "migration_path_url must be a reachable internal documentation URL",
    "declaration_timestamp_utc must be ISO-8601 UTC",
    "breaking_change_flag must be boolean",
    "custom_notice_period_days if declared must be >= canonical minimum for artifact_type",
    "emergency_sunset_flag if true requires emergency_authority_id",
    "artifact_version_successor must differ from artifact_version_deprecated"
  ],
  "security_checks": [
    "JWT scope: deprecation:declare (human ops) or deprecation:ci_declare (CI pipeline)",
    "Tenant isolation: artifact_id must belong to declaring tenant's scope",
    "Breaking change flag = true requires human_operator declared_by_type — CI cannot declare breaking changes autonomously",
    "Emergency sunset requires minimum CISO or CTO role on emergency_authority_id",
    "Rate limit: max 50 deprecation declarations per hour per tenant"
  ],
  "domain_checks": [
    "artifact_version_deprecated must not already be in a deprecation lifecycle",
    "SCORING_FORMULA and BELT_RULE deprecations require Dojo governance sign-off",
    "CERTIFICATION_SCHEMA deprecations require legal review flag set",
    "BILLING_RULE deprecations require finance authority sign-off",
    "ROYALTY_RULE deprecations require finance + legal authority sign-off"
  ]
}
```

```json
INPUT_SCHEMA — SUNSET_EXECUTION_REQUEST: {
  "required_fields": [
    "deprecation_id",
    "tenant_id",
    "authorized_by",
    "authorization_timestamp_utc",
    "consumer_zero_confirmation",
    "migration_complete_confirmation",
    "notice_period_elapsed_confirmation"
  ],
  "optional_fields": [
    "rollback_plan_url",
    "post_sunset_monitoring_hours"
  ],
  "validation_rules": [
    "deprecation_id must be in SUNSET_ELIGIBLE state — REJECT otherwise",
    "authorized_by must be human operator with deprecation:sunset scope",
    "consumer_zero_confirmation must be true — REJECT if false",
    "migration_complete_confirmation must be true — REJECT if false",
    "notice_period_elapsed_confirmation validated against canonical UTC clock"
  ]
}
```

**Rules — Non-Negotiable:**
- CI pipelines may declare deprecations but CANNOT declare breaking changes
- Breaking change declarations require human operator only
- Sunset execution requires human trigger — never autonomous
- No null tolerance on required fields — REJECT with structured error
- All validation failures logged to AUDIT_LOG_SERVICE immediately

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 6 — OUTPUT CONTRACT (STRICT)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```json
OUTPUT_SCHEMA — DEPRECATION_RECORD: {
  "result_object": {
    "deprecation_id":                "UUID",
    "artifact_type":                 "ENUM string",
    "artifact_id":                   "UUID | string",
    "artifact_version_deprecated":   "string",
    "artifact_version_successor":    "string | 'NONE'",
    "deprecation_state":             "ENUM (lifecycle state)",
    "declared_at_utc":               "ISO-8601 UTC",
    "sunset_date_utc":               "ISO-8601 UTC",
    "notice_period_days":            "integer",
    "breaking_change_flag":          "boolean",
    "consumer_count_at_declaration": "integer",
    "consumer_count_current":        "integer",
    "migration_completion_rate":     "float [0.0–1.0]",
    "notice_sent_flag":              "boolean",
    "notice_sent_at_utc":            "ISO-8601 UTC | null",
    "deprecation_header_active":     "boolean",
    "force_upgrade_active":          "boolean",
    "ci_gate_blocking":              "boolean",
    "compliance_status":             "ENUM['COMPLIANT','AT_RISK','OVERDUE','BLOCKED']",
    "days_until_sunset":             "integer | null",
    "days_overdue":                  "integer | null",
    "anomaly_flags":                 ["string"]
  },
  "confidence_score":                "float [0.0–1.0]",
  "model_version":                   "string (e.g. dea-v1.0.0)",
  "audit_reference":                 "UUID",
  "next_trigger_event": [
    "DEPRECATION_REGISTERED",
    "CONSUMER_IMPACT_ASSESSED",
    "DEPRECATION_NOTICE_SENT",
    "MIGRATION_REMINDER_SENT (30d, 14d, 7d, 1d before sunset)",
    "SUNSET_ELIGIBLE_REACHED",
    "SUNSET_EXECUTED",
    "DEPRECATION_OVERDUE_ALERT (if sunset_date passed + consumers still active)",
    "CI_DEPLOY_BLOCKED (if breaking change without compliance)"
  ]
}
```

```json
OUTPUT_SCHEMA — CI_GATE_DECISION: {
  "pipeline_job_id":        "string",
  "gate_decision":          "ENUM['PASS','BLOCK','WARN']",
  "violations_detected":    ["string"],
  "deprecated_artifacts_used": [
    {
      "artifact_type":      "string",
      "artifact_id":        "string",
      "artifact_version":   "string",
      "sunset_date_utc":    "ISO-8601 UTC",
      "migration_path_url": "string"
    }
  ],
  "breaking_changes_detected": ["string"],
  "audit_reference":        "UUID",
  "gate_timestamp_utc":     "ISO-8601 UTC"
}
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 7 — ML / AI LOGIC LAYER
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

### 7.1 ML Layer (PRIMARY — 70% of analysis weight)

```
MODEL_TYPE:
  Model A:   Classification — Migration velocity prediction
             (predicts FAST / NORMAL / SLOW / STALLED for a given consumer cohort)
  Model B:   Regression — Consumer zero-completion ETA prediction
             (estimates days until all consumers migrate off deprecated artifact)
  Model C:   Anomaly Detection — Deprecation compliance risk scoring
             (flags artifacts at risk of overdue sunset or migration stall)

FEATURES_USED:
  Migration velocity model (A):
    - artifact_type
    - consumer_count_at_declaration
    - consumer_type_distribution (internal_agent, mobile_client, web_client, partner_api)
    - breaking_change_flag
    - migration_complexity_score (proxy: migration_guide_document_length + API diff count)
    - historical_migration_velocity_similar_artifact_type
    - team_migration_backlog_size (from OBSERVABILITY_AGENT)
    - notice_period_remaining_days

  ETA prediction model (B):
    - consumer_count_current
    - migration_completion_rate_velocity_7d
    - migration_completion_rate_velocity_30d
    - notice_period_remaining_days
    - artifact_type
    - is_mobile_client_involved (bool — longer tail)
    - historical_eta_accuracy_similar_cases

  Compliance risk model (C):
    - days_until_sunset
    - migration_completion_rate
    - consumer_count_current
    - is_breaking_change
    - artifact_type_risk_class (HIGH: BILLING_RULE, SCORING_FORMULA | MEDIUM: API | LOW: FEATURE_FLAG)
    - historical_overdue_rate_for_artifact_type
    - escalation_flag

TRAINING_FREQUENCY:
  Migration velocity (A):     Monthly
  ETA prediction (B):         Bi-weekly
  Compliance risk (C):        Weekly

DRIFT_DETECTION:
  - Migration velocity accuracy < 0.70 → retrain flag
  - ETA prediction error > 20% vs actual → retrain flag
  - Compliance risk false negative rate > 5% → escalate + retrain
  - Action on drift: PAUSE affected model → rule-based fallback → alert ML_OPS

VERSION_CONTROL:
  Format: dea-[model-id]-v[MAJOR].[MINOR].[PATCH]
  All model versions immutable — no in-place overwrite
  Rollback safe per version
```

### 7.2 AI Layer (ASSIST ONLY — 30% of execution weight)

```
AI_USAGE_SCOPE:
  - Generate human-readable deprecation notice text (for NOTIFICATION_DISPATCH_AGENT)
  - Generate migration guide summaries for operator dashboard
  - Summarize compliance risk anomalies for ops briefing
  - Identify patterns in stalled migrations for team escalation reports
  - AI must NOT declare deprecations autonomously
  - AI must NOT compute sunset dates — ML + canonical UTC only
  - AI must NOT modify consumer registries

PROMPT_GOVERNANCE:
  - All prompts versioned: dea-prompt-v[VERSION]
  - Temperature: 0.15 (low — policy-aligned, consistent tone)
  - Max tokens: 600 per deprecation notice | 400 per migration summary
  - Prompt inputs: structured deprecation metadata only — no raw user data
  - All prompts logged with prompt_version + input_hash

AI MUST NOT:
  - Override ML-derived migration velocity or ETA scores
  - Suggest shortening notice periods below canonical minimum
  - Generate notice text that omits migration_path_url
  - Access artifact source code or schema data directly
```

**Law: ML predicts migration outcomes. AI explains them to operators.
Neither replaces human authority on sunset execution.**

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 8 — CI/CD GATE ENFORCEMENT
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

### 8.1 Pre-Deploy Gate (Mandatory — blocks all deployments)

```
CI_GATE_STAGE: deprecation_enforcement_gate
Position in pipeline: AFTER contract_validator (R49) | BEFORE docker_build

Gate checks performed on every pipeline run:

CHECK 1 — DEPRECATED ARTIFACT USAGE SCAN:
  → Scan deployment manifest for any reference to a deprecated artifact version
  → Cross-reference against DEPRECATION_REGISTRY
  → If deprecated artifact referenced AND sunset_date < 30 days: BLOCK
  → If deprecated artifact referenced AND sunset_date >= 30 days: WARN

CHECK 2 — BREAKING CHANGE DETECTION:
  → Diff current API contract against API_CONTRACT_REGISTRY
  → Diff current EVENT_SCHEMA against EVENT_SCHEMA_REGISTRY
  → Diff current DB_SCHEMA against SCHEMA_REGISTRY
  → If breaking change detected without DEPRECATION_REGISTRATION: BLOCK
  → Breaking change = removed field, changed type, removed endpoint, changed event structure

CHECK 3 — SCHEMA MUTATION POLICY ENFORCEMENT (Add-Only Law):
  → Verify all schema changes are additive only
  → No field removal allowed in any schema
  → No type change allowed in any existing field
  → No endpoint removal without completed deprecation lifecycle
  → Violation → BLOCK with detailed mismatch report

CHECK 4 — SUNSET ENFORCEMENT:
  → Check if any artifact in the deploy bundle has reached SUNSET_COMPLETE state
  → If yes and artifact still present in deploy bundle: BLOCK
  → Artifacts in SUNSET_COMPLETE must be absent from new deployments

CHECK 5 — ML MODEL VERSION COMPLIANCE:
  → Verify all ML model references in deploy bundle use registered, non-deprecated versions
  → Verify all AI prompt version references are registered and not deprecated
  → Violation → BLOCK

CHECK 6 — FLUTTER CLIENT MINIMUM VERSION:
  → Verify Flutter client version in deploy bundle meets minimum_supported_version
  → If below minimum: BLOCK with force_upgrade_signal emitted to clients

GATE OUTPUT:
  PASS   → Deployment proceeds
  WARN   → Deployment proceeds with warning logged + NOTIFICATION sent to team
  BLOCK  → Deployment halted + detailed violation report generated
           → LOG_INCIDENT → ALERT OBSERVABILITY_AGENT → notify declaring team
```

### 8.2 Deprecation Response Headers (Kong Gateway Injection)

```
HTTP HEADERS INJECTED ON DEPRECATED API ENDPOINTS:

  Deprecation: true
  Sunset: <RFC-7231 HTTP-date of sunset_date_utc>
  Link: <migration_path_url>; rel="successor-version"
  Warning: 299 - "Deprecated API version. Migrate to successor by <sunset_date>."
  X-Deprecation-ID: <deprecation_id>
  X-Days-Until-Sunset: <integer>

These headers are injected by the Kong DEPRECATION_ENFORCEMENT plugin.
Headers are active from DEPRECATED_REGISTERED state through SUNSET_COMPLETE.
All deprecated endpoint calls with these headers are logged to usage telemetry.
Usage telemetry feeds consumer_count_current in real time.

ZERO-CONSUMER DETECTION:
  → If no calls received on deprecated endpoint for notice_period_days / 2:
    → Flag as CANDIDATE_FOR_EARLY_SUNSET
    → Alert ops team with early sunset opportunity
  → Zero-consumer threshold: 0 calls in trailing 14-day window
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 9 — FLUTTER CLIENT VERSION ENFORCEMENT
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
FLUTTER CLIENT VERSION LIFECYCLE:

  SUPPORTED       → Active, fully supported version
  DEPRECATED      → Deprecated with 180-day notice (mobile) / 60-day (web)
  MINIMUM_BREACH  → Version below minimum_supported_version
  FORCE_UPGRADE   → Force upgrade signal active — app blocked on launch

MINIMUM_SUPPORTED_VERSION POLICY:
  → Defined per platform: Android, iOS, Windows, macOS, Flutter Web
  → minimum_supported_version bumped only after sunset of deprecated version
  → Never bumped without 180-day (mobile) or 60-day (web) notice period

FORCE_UPGRADE_SIGNAL (emitted to Flutter clients):
  {
    "client_platform":          "ENUM['android','ios','windows','macos','flutter_web']",
    "current_version_detected": "semver string",
    "minimum_supported_version":"semver string",
    "force_upgrade_required":   "boolean",
    "upgrade_deadline_utc":     "ISO-8601 UTC",
    "app_store_url":            "string",
    "message":                  "string (locale-aware, AI-generated)"
  }

ACTIVE VERSION DISTRIBUTION MONITORING:
  → FLUTTER_CLIENT_TELEMETRY_SERVICE reports active version distribution daily
  → Distribution stored per platform, per semver, per tenant
  → Feeds migration_completion_rate for FLUTTER_SDK_CLIENT deprecations
  → Alert threshold: if > 5% of users on deprecated version within 30 days of sunset
    → ESCALATE → increase notice frequency to daily reminders

SECURITY VULNERABILITY FAST-TRACK:
  → CVE affecting Flutter client: 0-day notice permitted with CISO approval
  → force_upgrade_required = true immediately
  → Users on vulnerable version blocked from authenticated sessions
  → Audit trail required within 24h of activation
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 10 — ML MODEL DEPRECATION PROTOCOL
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
ML MODEL DEPRECATION LIFECYCLE:

  Every ML model on Antigravity must be registered in ARTIFACT_TYPE_REGISTRY
  under type: ML_MODEL with full version metadata.

  MODEL_LIFECYCLE_STATES:
    ACTIVE_PRIMARY      → Current production model, all traffic routed here
    SHADOW_RUN          → New version running in shadow (no live decisions)
    CANARY              → New version receiving N% of traffic (configurable)
    DEPRECATED_PRIMARY  → Deprecated, shadow run of successor required
    SUNSET_COMPLETE     → Deregistered, no traffic

  SHADOW_RUN_REQUIREMENT:
    → All ML model deprecations require minimum 14-day shadow run of successor
    → Shadow run metrics compared against deprecated model:
        - Accuracy delta < 5%: auto-approve promotion candidate
        - Accuracy delta >= 5%: BLOCK promotion → escalate ML_OPS
    → Shadow run results logged to AUDIT_LOG_SERVICE

  MODEL_VERSION_IMMUTABILITY:
    → Once a model version is ACTIVE_PRIMARY, its artifact is immutable
    → No weight update, feature set change, or threshold change without new version bump
    → In-place model overwrite = GOVERNANCE_VIOLATION → BLOCK_DEPLOY → LOG_INCIDENT

  AUDIT TRAIL REQUIREMENT:
    → Every model version must have: training_data_hash, feature_set_hash,
      evaluation_metrics snapshot, training_timestamp_utc
    → Immutable reference stored per model_version in ARTIFACT_REGISTRY
    → Enables forensic reconstruction of any past prediction

  PROMPT VERSION MANAGEMENT:
    → AI prompts follow identical lifecycle to ML_MODEL
    → prompt_version format: [agent-name]-prompt-v[MAJOR].[MINOR].[PATCH]
    → Prompt overwrite without version bump = GOVERNANCE_VIOLATION
    → All prompt versions stored with: content_hash, evaluation_sample_hash,
      authored_by, authored_at_utc
    → Notice period: 14 days minimum before old prompt version sunset
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 11 — DOJO-SPECIFIC DEPRECATION GOVERNANCE
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
DOJO IMMUTABLE CLASS ARTIFACTS:

  Per Dojo SaaS Locked Spec v1.0 — the following artifact types are
  classified as IMMUTABLE CLASS and carry the highest deprecation protection:

    SCORING_FORMULA    → Notice period: 180 days minimum
    BELT_RULE          → Notice period: 180 days minimum
    MATCH_ENGINE       → Interfaces frozen — only internal improvements allowed
    VIDEO_TRANSPORT    → Architecture frozen — provider swappable, not model
    CERTIFICATION_SCHEMA → Notice period: 90 days + legal review required

  IMMUTABLE CLASS DEPRECATION REQUIREMENTS (ALL mandatory):
    ✅ Dojo Governance Board human sign-off (documented)
    ✅ Competitive fairness impact assessment (written report)
    ✅ Active tournament bracket impact analysis
    ✅ Belt certification holder notification (mass notification)
    ✅ Legal review for certification schema changes
    ✅ 180-day notice period — never reducible
    ✅ Migration path includes: historical belt/score record preservation
    ✅ Shadow run of new scoring formula for minimum 30 days
    ✅ External fairness audit recommended (advisory)

  NOT ALLOWED WITHOUT VERSION BUMP (per Dojo Spec):
    ❌ Change scoring math
    ❌ Change belt rules
    ❌ Change entity names (User, Skill, Scenario, Match, Score, Belt, Tournament,
                           Replay, Analytics, Certification)
    ❌ Change transport model (WebRTC SFU + LiveKit architecture)
    ❌ Change stack split (Flutter ↔ React split boundary)

  Any of the above detected in a deployment manifest → BLOCK_DEPLOY immediately
  → LOG_INCIDENT → ESCALATE Dojo Governance Board
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 12 — SCALABILITY DESIGN
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
EXPECTED_RPS            = 200 peak (declaration + status queries + CI gates)
LATENCY_TARGET          = p95 < 200ms (CI gate decision — blocks pipeline)
                          p95 < 500ms (deprecation status query)
                          p95 < 2s (consumer impact scan — async)
MAX_CONCURRENCY         = 1,000 simultaneous CI gate evaluations
QUEUE_STRATEGY          = Redis Streams for event emission
                          PostgreSQL for durable deprecation registry
                          Redis cache for hot deprecation status lookup (TTL: 5min)

HORIZONTAL_SCALING:
  - Stateless execution — no in-process deprecation state
  - Kubernetes HPA: scale on CPU > 60% or CI gate queue depth > 200
  - Min replicas: 2 | Max replicas: 15
  - Pod anti-affinity across availability zones

CI GATE PERFORMANCE REQUIREMENT:
  → CI gate must complete in p95 < 200ms — pipelines wait on this gate
  → Warm cache required: deprecation registry preloaded to Redis on agent start
  → Cache invalidated on any DEPRECATION_REGISTERED or SUNSET_COMPLETE event
  → Fallback on cache miss: direct PostgreSQL query (p95 < 500ms)

SCALE PROJECTIONS:
  At 10M users:
    → ~50 active deprecations per month
    → ~500 CI pipeline gate evaluations per day
    → ~200 consumer migration notifications per month

  At 100M users:
    → ~500 active deprecations per month
    → ~5,000 CI gate evaluations per day
    → ~2,000 consumer migration notifications per month

DATA RETENTION:
  Active deprecation records:   until ARCHIVED + 90 days hot
  Archived deprecation records: 7 years (regulatory compliance)
  CI gate decisions:            2 years
  Consumer usage telemetry:     2 years rolling
  Audit logs:                   7 years minimum
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 13 — SECURITY ENFORCEMENT
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
SECURITY CHECKLIST (ALL MANDATORY — absence = STOP EXECUTION):

  ✅ Only authenticated human operators or registered CI identities may
     submit deprecation declarations (JWT scopes enforced via Keycloak)
  ✅ Breaking change declarations: human operator only — CI cannot declare
  ✅ Sunset execution: human operator only — never autonomous
  ✅ Emergency sunset: CISO/CTO role minimum
  ✅ Dojo IMMUTABLE CLASS deprecations: Dojo Governance Board sign-off required
  ✅ Billing/Royalty rule deprecations: Finance + Legal authority required
  ✅ Tenant isolation: artifact deprecations scoped to declaring tenant
  ✅ Cross-tenant deprecation requests: rejected as SECURITY_INCIDENT
  ✅ All deprecation records encrypted at rest: AES-256
  ✅ All API gateway deprecation headers signed
  ✅ Audit logging: append-only, immutable, every lifecycle transition
  ✅ Rate limiting: 50 declarations per hour per tenant (Kong Gateway)
  ✅ CI gate spoofing prevention: gate result signed and verified by pipeline
  ✅ Deprecation registry: read-only to all agents except this agent
     (no other agent may modify deprecation records)
  ✅ No cross-tenant consumer registry visibility
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 14 — AUDIT & TRACEABILITY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Every lifecycle transition emits an immutable audit record:

```json
AUDIT_LOG_SCHEMA: {
  "audit_reference":             "UUID (primary key)",
  "timestamp_utc":               "ISO-8601 (from GLOBAL_TIME_EVENT_NORMALIZATION_AGENT)",
  "tenant_id":                   "UUID",
  "agent_name":                  "DEPRECATION_ENFORCEMENT_AGENT",
  "agent_version":               "v1.0.0",
  "actor_id":                    "UUID (human or CI identity)",
  "actor_type":                  "ENUM['human_operator','ci_pipeline','system','platform_admin']",
  "deprecation_id":              "UUID",
  "artifact_type":               "string",
  "artifact_id":                 "string",
  "artifact_version_deprecated": "string",
  "action":                      "string (e.g. DEPRECATION_DECLARED, SUNSET_EXECUTED)",
  "from_state":                  "ENUM lifecycle state",
  "to_state":                    "ENUM lifecycle state",
  "consumer_count_at_transition":"integer",
  "migration_completion_rate":   "float",
  "input_hash":                  "SHA-256",
  "output_hash":                 "SHA-256",
  "model_version":               "string | null",
  "prompt_version":              "string | null",
  "ci_pipeline_job_id":          "string | null",
  "compliance_status":           "ENUM string",
  "anomaly_flags":               ["string"],
  "prior_audit_hash":            "SHA-256 (hash chain)",
  "execution_time_ms":           "integer"
}
```

**Immutability Law:**
- Append-only PostgreSQL partition — no UPDATE or DELETE permitted
- Hash chain: every entry includes SHA-256 of prior entry (tamper detection)
- Replicated to cold storage within 5 minutes
- Retention: 7 years minimum (regulatory compliance)
- Deprecation records for BILLING_RULE, ROYALTY_RULE, CERTIFICATION_SCHEMA:
  indefinite retention (financial and legal chain of evidence)

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 15 — FAILURE POLICY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
FAILURE_SCENARIOS AND RESPONSE:

┌──────────────────────────────────────────┬──────────────────────────────────────────────────────┐
│ Failure Type                             │ Response                                             │
├──────────────────────────────────────────┼──────────────────────────────────────────────────────┤
│ Breaking change without declaration      │ BLOCK_DEPLOY → LOG_INCIDENT → ALERT OPS              │
│ Sunset executed with active consumers    │ REJECT → LOG_INCIDENT → ESCALATE immediately         │
│ Schema mutation policy violation         │ BLOCK_DEPLOY → GOVERNANCE_VIOLATION log              │
│ Dojo IMMUTABLE CLASS violated            │ BLOCK_DEPLOY → ESCALATE Dojo Governance Board       │
│ CI identity declares breaking change     │ REJECT → SECURITY_INCIDENT → ALERT OPS              │
│ Sunset date overdue + consumers active   │ ESCALATE human → daily alert → LOG_INCIDENT          │
│ Migration stalled > SLA (30d no progress)│ FLAG MIGRATION_BLOCKED → Tier 1 escalation          │
│ Consumer refuses migration               │ ESCALATED_TO_HUMAN → architecture review             │
│ ML model overwritten without version bump│ GOVERNANCE_VIOLATION → BLOCK → rollback trigger     │
│ Flutter client below minimum version     │ FORCE_UPGRADE signal emitted → session block         │
│ Deprecation registry cache miss + DB down│ BLOCK all CI gates conservatively → ALERT OPS       │
│ GLOBAL_TIME agent unreachable            │ HALT sunset date computation → queue for retry       │
│ Unauthorized sunset attempt              │ REJECT → SECURITY_INCIDENT → ALERT CISO             │
│ Duplicate deprecation_id                 │ REJECT → return existing record reference            │
│ BILLING_RULE deprecated without finance  │ BLOCK → require finance authority resubmission       │
└──────────────────────────────────────────┴──────────────────────────────────────────────────────┘

ESCALATION_TARGETS:
  OPS_TEAM:                 ops@ecoskiller.internal
  ML_OPS_TEAM:              ml-ops@ecoskiller.internal
  DOJO_GOVERNANCE_BOARD:    dojo-governance@ecoskiller.internal
  FINANCE_AUTHORITY:        finance@ecoskiller.internal
  LEGAL_TEAM:               legal@ecoskiller.internal
  CISO:                     ciso@ecoskiller.internal
  OBSERVABILITY_AGENT:      emit AGENT_FAILURE_EVENT

RETRY_POLICY:
  Transient DB / Redis failures:
    Attempt 1: immediate | 2: 2s | 3: 8s | 4: 30s
    Max retries: 4 → STOP → LOG_INCIDENT → fallback to conservative BLOCK
  No silent failures under any condition
  Conservative failure mode: when in doubt — BLOCK, not PASS
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 16 — INTER-AGENT DEPENDENCY MAP
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
UPSTREAM_AGENTS / SYSTEMS:
  - ALL PLATFORM AGENTS                       → version registration on startup
  - CI/CD PIPELINE (GitLab CE)                → deployment manifests for gate
  - OBSERVABILITY_AGENT                       → consumer usage metrics
  - DATABASE_MIGRATION_MANAGER                → migration completion status
  - FEATURE_FLAG_MANAGER                      → flag usage telemetry
  - R49_CONTRACT_VALIDATOR                    → contract validation results
  - GLOBAL_TIME_EVENT_NORMALIZATION_AGENT     → canonical UTC timestamps
  - FLUTTER_CLIENT_TELEMETRY_SERVICE          → active version distribution

DOWNSTREAM_AGENTS / SYSTEMS:
  - CI/CD PIPELINE (GitLab CE)                → gate decision (PASS/BLOCK/WARN)
  - Kong API Gateway                          → deprecation header injection
  - NOTIFICATION_DISPATCH_AGENT               → deprecation/sunset notices
  - OBSERVABILITY_AGENT                       → metrics, violations, SLA alerts
  - DATABASE_MIGRATION_MANAGER                → migration trigger signals
  - FEATURE_FLAG_MANAGER                      → deprecated flag enforcement
  - Flutter Mobile / Web Clients              → force-upgrade enforcement
  - AUDIT_LOG_SERVICE                         → all lifecycle transition records
  - TRANSPARENCY_REPORT_AGENT                 → anonymized compliance stats
  - ROYALTY_DISPUTE_RESOLUTION_AGENT          → billing/royalty rule deprecation signals
  - SCHOOL_GROWTH_FORECAST_AGENT              → ML model lifecycle coordination

EVENT_TRIGGERS (emitted via Redis Streams):
  - DEPRECATION_REGISTERED
  - CONSUMER_IMPACT_ASSESSED
  - DEPRECATION_NOTICE_SENT
  - MIGRATION_REMINDER_SENT          (30d, 14d, 7d, 1d before sunset)
  - MIGRATION_STALLED_ALERT          (conditional)
  - SUNSET_ELIGIBLE_REACHED
  - SUNSET_IN_PROGRESS
  - SUNSET_EXECUTED
  - SUNSET_OVERDUE_ALERT             (conditional: past sunset_date with consumers)
  - CI_DEPLOY_BLOCKED                (conditional)
  - GOVERNANCE_VIOLATION_DETECTED    (conditional)
  - FORCE_UPGRADE_SIGNAL_EMITTED     (conditional)
  - DOJO_IMMUTABLE_CLASS_VIOLATION   (conditional)
  - AGENT_FAILURE_EVENT              (conditional)
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 17 — PASSIVE INTELLIGENCE COMPATIBILITY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

After each lifecycle milestone, the agent emits structured feature vectors:

```json
EMIT_FEATURE_VECTOR (per deprecation): {
  "entity_type":       "artifact",
  "entity_id":         "artifact_id",
  "tenant_id":         "UUID",
  "feature_name":      "deprecation_compliance_status",
  "feature_value":     "ENUM['COMPLIANT','AT_RISK','OVERDUE','BLOCKED']",
  "timestamp_utc":     "ISO-8601",
  "source_agent":      "DEPRECATION_ENFORCEMENT_AGENT",
  "model_version":     "string"
}
```

Additional vectors emitted platform-wide:

```
platform_deprecation_compliance_rate     → float (rolling 30d)
platform_ci_gate_block_rate              → float
platform_overdue_sunset_count            → integer
platform_migration_velocity_avg          → float (days to consumer zero)
dojo_immutable_class_violation_count     → integer (rolling 30d — should be zero)
```

These feed into the OBSERVABILITY_AGENT platform health dashboard and
TRANSPARENCY_REPORT_AGENT for public compliance reporting.

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 18 — SCALE, COMPATIBILITY & ECONOMICS
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

### 18.1 Economic Impact of Deprecation Governance

```
ECONOMIC RISK PREVENTED BY THIS AGENT:

  API_ENDPOINT breakdown at 100M users:
    → A single non-governed breaking change on a high-traffic endpoint
       can affect millions of active sessions simultaneously
    → Mobile client crash rate spike → app store rating drop → user churn
    → Each 0.1 star drop in app store rating correlates to ~3% install rate drop
    → This agent prevents all unplanned breaking changes via CI gate

  BILLING_RULE and ROYALTY_RULE deprecations:
    → Incorrect period boundaries or rule mutations = incorrect royalty payments
    → Triggers ROYALTY_DISPUTE_RESOLUTION_AGENT cases at scale
    → 90-day notice + finance authority sign-off = zero silent financial mutations

  ML_MODEL deprecations:
    → Undeclared model overwrites = non-deterministic prediction history
    → Compliance and audit failures at regulatory scale
    → Shadow run protocol = zero accuracy regressions in production

  SCORING_FORMULA and BELT_RULE (Dojo):
    → Score mutation mid-tournament = user trust destruction
    → Belt invalidation = legal exposure on certified professionals
    → 180-day notice + immutable class protection = zero competitive fairness incidents

ECONOMIC SIGNAL emitted to OBSERVABILITY_AGENT:
  {
    "metric_name":   "governance_violation_prevented_count",
    "value":         "integer (rolling 30d CI blocks that prevented incidents)",
    "period":        "ISO-8601 month",
    "source_agent":  "DEPRECATION_ENFORCEMENT_AGENT"
  }
```

### 18.2 Compatibility Matrix

```
COMPATIBLE WITH:
  ✅ Ecoskiller Core Platform — all API, schema, and event versioning
  ✅ Dojo For Arts — IMMUTABLE CLASS artifact protection
  ✅ Antigravity Leaderboard — scoring rule version governance
  ✅ Royalty & Billing Engine — financial rule deprecation governance
  ✅ ML Model Registry — all Antigravity ML model lifecycle
  ✅ AI Prompt Registry — all Antigravity prompt version governance
  ✅ Flutter Mobile + Web + Desktop clients — version enforcement
  ✅ GitLab CE CI/CD — pre-deploy gate integration
  ✅ Kong API Gateway — deprecation header injection plugin
  ✅ R49 Contract Validator CLI — complementary gate
  ✅ R19 API Versioning & Deprecation Law — full enforcement agent
  ✅ R22 Data Migration & Zero-Downtime Upgrade Law — migration coordination
  ✅ R60 Long-Term Archival — 7-year audit retention
  ✅ CQRS Event Store (append-only)
  ✅ PostgreSQL (deprecation registry + audit logs)
  ✅ Redis (hot cache for CI gate performance)
  ✅ Kubernetes autoscaling (stateless pods)
  ✅ OpenSearch (deprecation record search + consumer analytics)

NOT COMPATIBLE WITH (by design):
  ❌ Autonomous sunset execution — human trigger only
  ❌ Breaking change declarations from CI identities
  ❌ Notice period shortening below canonical minimums
  ❌ In-place schema, model, or prompt mutation
  ❌ Dojo IMMUTABLE CLASS modifications without 180-day governance
  ❌ Cross-tenant artifact deprecation visibility
  ❌ Third-party CI systems bypassing the gate without signed gate token
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 19 — PERFORMANCE MONITORING
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
METRICS (all exposed to OBSERVABILITY_AGENT):

  active_deprecation_count:               count of artifacts in active lifecycle
  overdue_sunset_count:                   count past sunset_date with consumers (target: 0)
  ci_gate_block_rate:                     % of pipelines blocked (alert > 5%)
  ci_gate_latency_p95_ms:                 target < 200ms
  migration_completion_rate_avg:          platform avg across active deprecations
  migration_stalled_count:                count of MIGRATION_BLOCKED state
  consumer_zero_avg_days:                 avg days to reach zero consumers
  flutter_client_below_min_version_pct:   % of active sessions on deprecated version
  governance_violation_count:             rolling 30d (target: 0 for DOJO_IMMUTABLE)
  ml_model_version_compliance_rate:       % of ML references using non-deprecated versions
  prompt_version_compliance_rate:         % of prompt references non-deprecated
  notice_period_compliance_rate:          % of sunsets respecting notice period (target: 100%)
  emergency_sunset_count:                 rolling 90d (flag if > 2)
  schema_mutation_violation_count:        rolling 30d (target: 0)
  audit_chain_integrity:                  hash chain validation result (boolean: true = intact)

DASHBOARD:
  Grafana panel: DEPRECATION_ENFORCEMENT_AGENT
  Sub-panels: Artifact Lifecycle Heatmap | CI Gate History |
              Flutter Version Distribution | Dojo Immutable Class Status
  Alerts: PagerDuty → OPS_TEAM, ML_OPS_TEAM, DOJO_GOVERNANCE_BOARD (for Dojo violations)

MUST INTEGRATE WITH:
  OBSERVABILITY_AGENT     (metrics push every 60s)
  AUDIT_LOG_SERVICE       (every lifecycle transition)
  TRANSPARENCY_REPORT_AGENT (monthly compliance aggregate)
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 20 — VERSIONING POLICY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
THIS AGENT'S OWN VERSIONING LAW:
  All changes to this agent: Add-only, versioned via semver,
  backward compatible, migration documented, rollback safe

  MAJOR version bump required for:
    - New artifact_type added to ARTIFACT_TYPE_REGISTRY
    - New lifecycle state added to state machine
    - CI gate check logic changed
    - Notice period policy change (upward only — minimum never reduced)

  MINOR version bump required for:
    - New optional input/output field
    - New escalation target
    - New compliance metric
    - New notification milestone added

  PATCH version bump required for:
    - Bug fixes in validation logic
    - ML model prompt update (minor wording)
    - Performance improvements

  CRITICAL LAW — THIS AGENT GOVERNS ITSELF:
    → Any change to this agent must itself pass through the deprecation
      governance process
    → The DEPRECATION_ENFORCEMENT_AGENT's own versioning is governed by
      this agent — it is self-applying
    → Violation of this self-application rule = GOVERNANCE_VIOLATION
    → This agent's own artifact version must be registered in its own registry
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 21 — DATABASE SCHEMA (REFERENCE)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```sql
-- Core deprecation registry (append-only after write)
CREATE TABLE deprecation_registry (
  id                           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  deprecation_id               UUID NOT NULL UNIQUE,
  tenant_id                    UUID NOT NULL,
  artifact_type                VARCHAR(30) NOT NULL,
  artifact_id                  VARCHAR(255) NOT NULL,
  artifact_version_deprecated  VARCHAR(50) NOT NULL,
  artifact_version_successor   VARCHAR(50),
  deprecation_state            VARCHAR(30) NOT NULL DEFAULT 'DEPRECATED_REGISTERED',
  declared_by                  UUID NOT NULL,
  declared_by_type             VARCHAR(20) NOT NULL,
  declared_at_utc              TIMESTAMPTZ NOT NULL,
  sunset_date_utc              TIMESTAMPTZ NOT NULL,
  notice_period_days           INTEGER NOT NULL,
  breaking_change_flag         BOOLEAN NOT NULL DEFAULT FALSE,
  migration_path_url           TEXT NOT NULL,
  deprecation_reason           TEXT NOT NULL,
  consumer_count_at_declaration INTEGER NOT NULL DEFAULT 0,
  consumer_count_current       INTEGER NOT NULL DEFAULT 0,
  migration_completion_rate    NUMERIC(5,4) NOT NULL DEFAULT 0,
  notice_sent_flag             BOOLEAN NOT NULL DEFAULT FALSE,
  notice_sent_at_utc           TIMESTAMPTZ,
  deprecation_header_active    BOOLEAN NOT NULL DEFAULT FALSE,
  force_upgrade_active         BOOLEAN NOT NULL DEFAULT FALSE,
  ci_gate_blocking             BOOLEAN NOT NULL DEFAULT FALSE,
  compliance_status            VARCHAR(20) NOT NULL DEFAULT 'COMPLIANT',
  emergency_sunset_flag        BOOLEAN NOT NULL DEFAULT FALSE,
  emergency_authority_id       UUID,
  linked_r19_reference         VARCHAR(20),
  linked_r22_reference         VARCHAR(20),
  ci_pipeline_job_id           VARCHAR(100),
  input_hash                   CHAR(64) NOT NULL,
  audit_reference              UUID NOT NULL,
  sunset_executed_at_utc       TIMESTAMPTZ,
  sunset_executed_by           UUID,
  archived_at_utc              TIMESTAMPTZ,
  created_at                   TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Lifecycle state transition log (immutable append)
CREATE TABLE deprecation_state_transitions (
  id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  deprecation_id   UUID NOT NULL REFERENCES deprecation_registry(deprecation_id),
  from_state       VARCHAR(30) NOT NULL,
  to_state         VARCHAR(30) NOT NULL,
  actor_id         UUID NOT NULL,
  actor_type       VARCHAR(20) NOT NULL,
  transition_note  TEXT,
  consumer_count   INTEGER NOT NULL,
  migration_rate   NUMERIC(5,4) NOT NULL,
  audit_reference  UUID NOT NULL,
  prior_hash       CHAR(64),
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Consumer registry per deprecated artifact
CREATE TABLE deprecation_consumers (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  deprecation_id      UUID NOT NULL REFERENCES deprecation_registry(deprecation_id),
  consumer_id         VARCHAR(255) NOT NULL,
  consumer_type       VARCHAR(30) NOT NULL,
  first_detected_utc  TIMESTAMPTZ NOT NULL,
  last_seen_utc       TIMESTAMPTZ NOT NULL,
  migrated_flag       BOOLEAN NOT NULL DEFAULT FALSE,
  migrated_at_utc     TIMESTAMPTZ,
  tenant_id           UUID NOT NULL,
  created_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Immutability enforcement
CREATE RULE no_delete_deprecations
  AS ON DELETE TO deprecation_registry DO INSTEAD NOTHING;
CREATE RULE no_delete_transitions
  AS ON DELETE TO deprecation_state_transitions DO INSTEAD NOTHING;

-- Indexes
CREATE INDEX idx_dr_artifact ON deprecation_registry (artifact_type, artifact_id);
CREATE INDEX idx_dr_state ON deprecation_registry (deprecation_state);
CREATE INDEX idx_dr_sunset ON deprecation_registry (sunset_date_utc ASC)
  WHERE deprecation_state NOT IN ('SUNSET_COMPLETE','ARCHIVED');
CREATE INDEX idx_dr_compliance ON deprecation_registry (compliance_status)
  WHERE compliance_status IN ('AT_RISK','OVERDUE','BLOCKED');
CREATE INDEX idx_dc_deprecation ON deprecation_consumers (deprecation_id, migrated_flag);
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 22 — API CONTRACT (REFERENCE)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# OpenAPI 3.1 reference (excerpt)

/v1/deprecations:
  post:
    summary: Register a new deprecation declaration
    security:
      - BearerAuth: [deprecation:declare]
    responses:
      201: Deprecation registered — lifecycle initiated
      400: Validation failure — structured error
      403: Unauthorized (CI identity attempting breaking change declaration)
      409: Artifact version already in deprecation lifecycle
      429: Rate limit exceeded

/v1/deprecations/{deprecation_id}:
  get:
    summary: Get deprecation record and current lifecycle state
    security:
      - BearerAuth: [deprecation:read]
    responses:
      200: Full deprecation record with current compliance status
      404: Deprecation not found

/v1/deprecations/{deprecation_id}/sunset:
  post:
    summary: Trigger sunset execution (human operator only)
    security:
      - BearerAuth: [deprecation:sunset]
    responses:
      202: Sunset initiated
      400: Preconditions not met (consumers active / notice period not elapsed)
      403: Insufficient authority for artifact type
      409: Already in SUNSET_IN_PROGRESS state

/v1/deprecations/ci-gate:
  post:
    summary: CI/CD pre-deploy gate evaluation
    security:
      - BearerAuth: [deprecation:ci_gate]
    requestBody:
      required: true
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/DeploymentManifest'
    responses:
      200:
        description: Gate decision (PASS / WARN / BLOCK)
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/CIGateDecision'

/v1/deprecations/dashboard:
  get:
    summary: Platform-wide deprecation compliance dashboard
    security:
      - BearerAuth: [platform_admin]
    responses:
      200: Compliance metrics, overdue artifacts, migration velocity
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 23 — NON-NEGOTIABLE RULES (SEALED)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

This agent MUST NOT under any condition:

```
❌ Execute a sunset autonomously — human operator trigger always required
❌ Allow a breaking change to deploy without a completed deprecation declaration
❌ Permit CI identity to declare breaking changes — human operators only
❌ Allow notice period to be shortened below canonical minimums
❌ Allow in-place mutation of any ML model, prompt, schema, or API without version bump
❌ Allow Dojo IMMUTABLE CLASS artifacts to be modified without 180-day notice +
   Dojo Governance Board sign-off
❌ Allow BILLING_RULE or ROYALTY_RULE deprecations without Finance authority sign-off
❌ Allow CERTIFICATION_SCHEMA deprecations without Legal review
❌ Permit sunset execution when consumer_count_current > 0 without human escalation
❌ Silently pass a CI gate when the registry is unavailable — conservative BLOCK default
❌ Mix deprecation registry records across tenant boundaries
❌ Allow the add-only mutation policy to be weakened under any version bump of this agent
❌ Modify or delete historical deprecation records or state transitions
❌ Auto-delete audit logs
❌ Override the R49 Contract Validator or R19 compliance requirements
❌ Allow a deprecated feature flag to be read without warning header in API response
❌ Apply this agent's own governance rules asymmetrically —
   this agent governs itself by the same laws it enforces on others
❌ Produce partial outputs — HALT or COMPLETE, never partial
```

Violation of any rule above → STOP EXECUTION → LOG_INCIDENT → ESCALATE

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 24 — EXECUTION CHECKLIST (SEALED)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
PRE-DEPRECATION DECLARATION GATE:
  ✅ JWT scope: deprecation:declare validated
  ✅ Actor type verified — breaking changes: human only
  ✅ artifact_version_deprecated exists and is ACTIVE
  ✅ No existing deprecation lifecycle on this artifact version
  ✅ deprecation_reason meets minimum length requirement (50 chars)
  ✅ migration_path_url reachable and returns HTTP 200
  ✅ notice_period_days >= canonical minimum for artifact_type
  ✅ Dojo / Finance / Legal authority checks passed for protected artifacts
  ✅ Consumer impact scan initiated
  ✅ sunset_date_utc computed via GLOBAL_TIME_EVENT_NORMALIZATION_AGENT
  ✅ input_hash computed and logged
  ✅ audit_reference UUID reserved

ON DEPRECATION REGISTRATION COMPLETE:
  ✅ Deprecation record written to registry (immutable)
  ✅ Audit log entry written (immutable, hash chain updated)
  ✅ DEPRECATION_REGISTERED event emitted to Redis Streams
  ✅ Deprecation header configuration pushed to Kong Gateway
  ✅ DEPRECATION_NOTICE_SENT event scheduled via GLOBAL_TIME_EVENT_NORMALIZATION_AGENT
  ✅ Reminder schedule registered: 30d, 14d, 7d, 1d before sunset

CI GATE (every pipeline run):
  ✅ Deprecation registry loaded from Redis cache (or PostgreSQL fallback)
  ✅ Deployment manifest scanned for deprecated artifact references
  ✅ Breaking change diff computed against all registries
  ✅ Schema mutation policy (add-only) validated
  ✅ Gate decision computed: PASS / WARN / BLOCK
  ✅ Gate decision signed and returned to CI pipeline
  ✅ CI gate audit entry written
  ✅ CI_DEPLOY_BLOCKED event emitted if BLOCK decision

SUNSET EXECUTION CHECKLIST (human-triggered):
  ✅ Human operator authenticated with deprecation:sunset scope
  ✅ consumer_count_current = 0 confirmed
  ✅ migration_completion_rate = 1.0 confirmed
  ✅ notice_period elapsed confirmed via GLOBAL_TIME_EVENT_NORMALIZATION_AGENT
  ✅ Artifact deactivation instruction sent to owning service
  ✅ Kong Gateway deprecation header deactivated
  ✅ CI gate updated: artifact version now in SUNSET_COMPLETE → block if referenced
  ✅ SUNSET_EXECUTED event emitted to Redis Streams
  ✅ Final audit entry written (immutable)
  ✅ Archival process initiated (7-year retention)

ANY GATE FAILURE → HALT → LOG_INCIDENT → ESCALATE
```

---

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
DOCUMENT STATUS:      SEALED
AGENT VERSION:        v1.0.0
PLATFORM:             Ecoskiller Antigravity
AUTHORED FOR:         Ecoskiller Intelligence & Innovation Core
MUTATION POLICY:      Add-only via version bump
INTERPRETATION:       NONE PERMITTED
EXECUTION AUTH:       Human declaration only
SUNSET AUTH:          Human operator trigger — never autonomous
BREAKING CHANGE AUTH: Human operator only — CI cannot declare
SELF-GOVERNANCE LAW:  This agent governs itself by the same laws
                      it enforces on all other platform artifacts
LEGAL ALIGNMENT:      R19 (API Versioning & Deprecation) ·
                      R22 (Data Migration & Zero-Downtime Upgrade) ·
                      R49 (Contract Validator CLI) ·
                      R60 (Long-Term Archival) ·
                      Dojo SaaS Locked Spec v1.0 (Immutable Class)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```
