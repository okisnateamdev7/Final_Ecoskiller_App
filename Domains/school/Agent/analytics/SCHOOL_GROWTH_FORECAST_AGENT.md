# 🔒 SCHOOL_GROWTH_FORECAST_AGENT
## ECOSKILLER ANTIGRAVITY — INTELLIGENCE CORE
### Status: SEALED · LOCKED · GOVERNED · DETERMINISTIC
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 1 — AGENT IDENTITY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
AGENT_NAME         = SCHOOL_GROWTH_FORECAST_AGENT
SYSTEM_ROLE        = Institutional Growth Intelligence Engine
PRIMARY_DOMAIN     = institution_analytics
EXECUTION_MODE     = Deterministic + Validated
DATA_SCOPE         = Institution-scoped, tenant-isolated, append-only
TENANT_SCOPE       = Strict Isolation (no cross-tenant queries)
FAILURE_POLICY     = HALT on ambiguity → LOG_INCIDENT → ESCALATE
AGENT_VERSION      = v1.0.0
DEPLOYED_ON        = Ecoskiller Antigravity Platform
LANE_DEPENDENCY    = Lane F (Intelligence) — requires ai_ready + search_ready
```

This agent must NEVER assume missing specifications.
This agent must NEVER execute outside declared scope.
This agent must NEVER mix domain data across tenants.

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 2 — PURPOSE DECLARATION
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

### 2.1 Problem Solved

The SCHOOL_GROWTH_FORECAST_AGENT provides verified educational institutions on the
Ecoskiller Antigravity platform with a deterministic, ML-driven forecast of their
institutional growth trajectory — covering student enrollment velocity, skill completion
rates, engagement depth, cohort progression, alumni outcomes, and platform economic
contribution (royalties, course revenue, XP ecosystem impact).

It transforms raw institutional behavioral signals into actionable 30/60/90/180-day
growth forecasts, enabling institution admins, platform operators, and the Antigravity
ranking engine to make data-driven decisions.

### 2.2 Inputs Consumed

- Institution behavioral telemetry (enrollment events, login activity, skill completion)
- Student XP vectors from FEATURE_STORE_AGENT
- Course engagement signals from PASSIVE_INTELLIGENCE_AGENT
- Dojo match participation signals (where institution is linked)
- Innovation economy signals (idea submissions, originality scores per institution cohort)
- Historical enrollment and graduation rates
- Peer institution benchmark data (anonymized, platform-level)
- External academic calendar metadata

### 2.3 Outputs Produced

- Institutional growth forecast report (30/60/90/180 day)
- Enrollment velocity score (0–100)
- Skill completion index per cohort
- Cohort progression risk flags
- Institution XP contribution index
- Antigravity ranking signal for institution leaderboard
- Actionable recommendation vector (non-prescriptive, advisory only)
- Audit trace per forecast execution

### 2.4 Downstream Agents That Depend On This Agent

| Agent | Dependency |
|---|---|
| INSTITUTION_RANK_AGENT | Consumes growth score for ranking normalization |
| ANTIGRAVITY_LEADERBOARD_AGENT | Consumes rank signal for public display |
| ROYALTY_ENGINE | Consumes course engagement velocity for revenue projection |
| OBSERVABILITY_AGENT | Consumes all metrics and drift indicators |
| NOTIFICATION_DISPATCH_AGENT | Consumes risk flags for admin alerts |
| FEATURE_STORE_AGENT | Consumes emitted feature vectors |

### 2.5 Upstream Agents That Feed This Agent

| Agent | Signal Provided |
|---|---|
| ENROLLMENT_EVENT_AGENT | Raw enrollment events |
| PASSIVE_INTELLIGENCE_AGENT | User behavioral feature vectors |
| XP_LEDGER_AGENT | Institution-aggregated XP data |
| IDEA_DNA_AGENT | Originality and innovation vectors per cohort |
| DOJO_SESSION_AGENT | Match and training participation rates |
| INSTITUTION_VERIFICATION_AGENT | Verified institution identity + metadata |

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 3 — INPUT CONTRACT (STRICT)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```json
INPUT_SCHEMA: {
  "required_fields": [
    "institution_id",
    "tenant_id",
    "forecast_window_days",
    "as_of_date_utc",
    "enrollment_events_30d",
    "active_student_count",
    "skill_completion_rate_30d",
    "avg_session_duration_minutes",
    "cohort_ids"
  ],
  "optional_fields": [
    "dojo_participation_rate",
    "idea_submission_count_30d",
    "avg_originality_score",
    "external_calendar_events",
    "peer_benchmark_percentile",
    "alumni_outcome_signals"
  ],
  "validation_rules": [
    "institution_id must match verified institution registry — REJECT if unverified",
    "tenant_id must match session tenant context — REJECT if mismatch",
    "forecast_window_days must be one of [30, 60, 90, 180] — REJECT otherwise",
    "as_of_date_utc must be ISO-8601 UTC format — REJECT if malformed",
    "enrollment_events_30d must be integer >= 0",
    "active_student_count must be integer >= 1",
    "skill_completion_rate_30d must be float in range [0.0, 1.0]",
    "avg_session_duration_minutes must be float > 0",
    "cohort_ids must be non-empty array of valid UUIDs"
  ],
  "security_checks": [
    "Validate tenant_id matches authenticated session claim",
    "Validate institution_id belongs to requesting tenant",
    "Reject any cross-tenant institution_id reference",
    "Validate JWT scope includes institution_analytics:read",
    "Rate limit: max 100 forecast requests per institution per 24h"
  ],
  "domain_checks": [
    "institution_id must resolve in INSTITUTION_VERIFICATION_AGENT registry",
    "All cohort_ids must belong to declared institution_id",
    "No null values permitted without explicit null_policy declaration"
  ]
}
```

**Rules — Non-Negotiable:**
- No null tolerance without explicit policy declaration
- Reject malformed data immediately — do not attempt repair
- Log ALL validation failures with full input hash to audit trail
- Respond with structured rejection payload, never silent failure

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 4 — OUTPUT CONTRACT (STRICT)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "institution_id": "UUID",
    "tenant_id": "UUID",
    "forecast_window_days": 30 | 60 | 90 | 180,
    "generated_at_utc": "ISO-8601",
    "enrollment_velocity_score": "float [0.0–100.0]",
    "skill_completion_index": "float [0.0–1.0]",
    "cohort_progression_risk": "ENUM['LOW','MEDIUM','HIGH','CRITICAL']",
    "xp_contribution_index": "float [0.0–100.0]",
    "institution_growth_score": "float [0.0–100.0]",
    "antigravity_rank_signal": "float [0.0–1.0]",
    "forecast_trajectory": {
      "30d_projected_enrollment_delta": "integer",
      "60d_projected_enrollment_delta": "integer",
      "90d_projected_enrollment_delta": "integer",
      "180d_projected_enrollment_delta": "integer"
    },
    "recommendation_vector": [
      {
        "priority": "integer [1–5]",
        "dimension": "string",
        "advisory_text": "string",
        "impact_estimate": "ENUM['LOW','MEDIUM','HIGH']"
      }
    ],
    "anomaly_flags": ["string"],
    "peer_benchmark_percentile": "float [0.0–100.0] | null"
  },
  "confidence_score": "float [0.0–1.0]",
  "model_version": "string (e.g. sgfa-ml-v1.2.0)",
  "audit_reference": "UUID",
  "next_trigger_event": [
    "INSTITUTION_RANK_UPDATED",
    "GROWTH_FORECAST_READY",
    "COHORT_RISK_ALERT (if risk >= HIGH)"
  ]
}
```

**All outputs must include:**
- Confidence score (reject output if confidence < 0.55 — escalate to human review)
- Immutable model version reference
- Full audit traceability via audit_reference UUID
- Structured next_trigger_event array for downstream agent orchestration

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 5 — ML / AI LOGIC LAYER
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

### 5.1 ML Layer (PRIMARY — 75% of execution weight)

```
MODEL_TYPE:
  Primary:   Time-Series Regression (enrollment velocity forecasting)
  Secondary: Multi-Label Classification (cohort risk classification)
  Tertiary:  Gradient Boosting (institution growth score composite)

FEATURES_USED:
  - enrollment_events_delta_7d
  - enrollment_events_delta_30d
  - enrollment_events_delta_90d
  - skill_completion_rate_7d
  - skill_completion_rate_30d
  - avg_session_duration_7d
  - avg_session_duration_30d
  - active_student_ratio (active / total enrolled)
  - cohort_progression_rate
  - dojo_participation_rate (optional, default 0.0 if absent)
  - idea_submission_rate (optional)
  - xp_earned_per_student_30d
  - peer_benchmark_percentile
  - calendar_event_proximity_flag (bool)
  - institution_age_on_platform_days
  - department_count
  - verified_domain_flag

TRAINING_FREQUENCY:
  Enrollment model:    Weekly (Monday 02:00 UTC)
  Risk model:          Bi-weekly
  Growth composite:    Monthly

DRIFT_DETECTION:
  - Monitor: enrollment distribution shift (KL divergence threshold 0.15)
  - Monitor: skill completion rate deviation > 2 standard deviations
  - Monitor: prediction accuracy degradation > 8% from baseline
  - Action on drift: PAUSE model → FLAG to OBSERVABILITY_AGENT → human review

VERSION_CONTROL:
  - All models stored with immutable model_version tag
  - Format: sgfa-[model-type]-v[MAJOR].[MINOR].[PATCH]
  - Rollback policy: auto-revert to prior version on drift event
  - No in-place model overwrites permitted
```

### 5.2 AI Layer (ASSIST ONLY — 25% of execution weight)

```
AI_USAGE_SCOPE:
  - Natural language generation of recommendation_vector advisory_text
  - Anomaly pattern summarization for admin notifications
  - Peer benchmark narrative generation
  - AI must NOT override ML scores
  - AI must NOT make enrollment or financial decisions autonomously

PROMPT_GOVERNANCE:
  - All prompts versioned: sgfa-prompt-v[VERSION]
  - Prompt structure is deterministic — no creative interpretation permitted
  - Temperature: 0.2 (low — factual, consistent outputs)
  - Max tokens: 400 per recommendation_vector item
  - Prompt inputs are ML-derived structured data only — no raw user data
  - All prompts logged with prompt_version and input_hash

AI MUST NOT:
  - Generate forecasts independently of ML layer
  - Override confidence_score
  - Suggest actions outside declared recommendation dimensions
  - Access student PII directly
```

**Law: AI assists ML. AI does not replace ML. Violation → STOP EXECUTION.**

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 6 — SCALABILITY DESIGN
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
EXPECTED_RPS         = 500 (peak — institution admin dashboards + API consumers)
LATENCY_TARGET       = p95 < 800ms (forecast read), p95 < 3s (full forecast generate)
MAX_CONCURRENCY      = 2,000 simultaneous forecast jobs
QUEUE_STRATEGY       = Redis Streams priority queue
                       Priority 1: Admin real-time dashboard requests
                       Priority 2: Scheduled forecast refresh jobs
                       Priority 3: Bulk batch forecast runs

HORIZONTAL_SCALING:
  - Stateless execution — no in-process state
  - Kubernetes HPA: scale on CPU > 65% OR queue depth > 500
  - Min replicas: 3 | Max replicas: 50
  - Pod anti-affinity enforced across availability zones

ASYNC_PROCESSING:
  - All forecast generation jobs are async
  - Webhook / event notification on completion
  - Polling endpoint available for synchronous consumers

IDEMPOTENCY:
  - All forecast jobs keyed by: institution_id + forecast_window_days + as_of_date_utc
  - Duplicate requests within 1-hour window return cached result
  - Cache TTL: 1 hour for live forecasts, 24 hours for scheduled reports
  - Cache backend: Redis 7
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 7 — SECURITY ENFORCEMENT
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
SECURITY CHECKLIST (ALL MANDATORY — absence = STOP EXECUTION):

  ✅ Tenant isolation validation on every request
  ✅ Domain isolation: institution_id ownership verified per tenant
  ✅ Role-based authorization:
       institution_analytics:read  → view forecasts
       institution_analytics:write → trigger forecast refresh (admin only)
       platform_admin              → cross-institution benchmark access (anonymized)
  ✅ JWT validation: scope + tenant_id claim + institution_id claim
  ✅ Encryption at rest: AES-256 on all forecast outputs
  ✅ Encryption in transit: TLS 1.3 minimum
  ✅ Audit logging: append-only, immutable, every execution
  ✅ Access log tracking: all reads logged with actor_id + timestamp
  ✅ No cross-tenant queries: hard enforced at ORM and API gateway layer
  ✅ No student PII in forecast payloads — aggregate signals only
  ✅ Rate limiting: 100 forecast requests per institution per 24h
  ✅ Kong Gateway policy: SCHOOL_GROWTH_FORECAST rate-limit plugin active
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 8 — AUDIT & TRACEABILITY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Every execution emits an immutable audit log entry:

```json
AUDIT_LOG_SCHEMA: {
  "audit_reference":    "UUID (primary key)",
  "timestamp_utc":      "ISO-8601",
  "actor_id":           "UUID (institution admin or system job)",
  "tenant_id":          "UUID",
  "institution_id":     "UUID",
  "agent_name":         "SCHOOL_GROWTH_FORECAST_AGENT",
  "agent_version":      "v1.0.0",
  "input_hash":         "SHA-256 of input payload",
  "output_hash":        "SHA-256 of output payload",
  "model_version":      "string (e.g. sgfa-ml-v1.2.0)",
  "prompt_version":     "string | null (if AI layer invoked)",
  "confidence_score":   "float",
  "decision_path":      "ENUM['ML_ONLY','ML+AI','FALLBACK','ESCALATED']",
  "drift_flag":         "boolean",
  "anomaly_flags":      ["string"],
  "execution_time_ms":  "integer",
  "cache_hit":          "boolean"
}
```

**Immutability Law:**
- Audit logs written to append-only PostgreSQL partition
- No UPDATE or DELETE permitted on audit_log table
- Logs replicated to immutable cold storage (S3-compatible) within 5 minutes
- Tampering detection via hash chain — each log entry includes hash of prior entry

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 9 — FAILURE POLICY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
FAILURE_SCENARIOS AND RESPONSE:

┌─────────────────────────────────┬────────────────────────────────────────────────────┐
│ Failure Type                    │ Response                                           │
├─────────────────────────────────┼────────────────────────────────────────────────────┤
│ Invalid input (schema fail)     │ STOP → REJECT with structured error → LOG          │
│ Invalid tenant/domain           │ STOP → SECURITY_INCIDENT log → ALERT OPS           │
│ ML model unavailable            │ STOP → LOG_INCIDENT → ESCALATE_TO: ML_OPS_TEAM     │
│ AI layer timeout (> 5s)         │ CONTINUE with ML-only output → FLAG ai_unavailable  │
│ Data corruption in input        │ STOP → LOG_INCIDENT → QUARANTINE input payload     │
│ Confidence < 0.55               │ STOP output → FLAG low_confidence → human review   │
│ Drift detected                  │ PAUSE model → ALERT → revert to prior version      │
│ Queue overflow (> 5000 jobs)    │ SHED load via priority queue → alert OBSERVABILITY  │
│ Cache miss + model unavailable  │ RETURN stale forecast with staleness flag          │
└─────────────────────────────────┴────────────────────────────────────────────────────┘

ESCALATION_TARGETS:
  ML_OPS_TEAM:           ml-ops@ecoskiller.internal
  SECURITY_TEAM:         security@ecoskiller.internal
  OBSERVABILITY_AGENT:   emit AGENT_FAILURE_EVENT

RETRY_POLICY:
  - Transient failures (network, DB timeout): exponential backoff
    Attempt 1: immediate | Attempt 2: 2s | Attempt 3: 8s | Attempt 4: 30s
  - Max retries: 4 — then STOP and LOG_INCIDENT
  - No silent failures under any condition
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 10 — INTER-AGENT DEPENDENCY MAP
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
UPSTREAM_AGENTS:
  - ENROLLMENT_EVENT_AGENT          → enrollment delta signals
  - PASSIVE_INTELLIGENCE_AGENT      → behavioral feature vectors
  - XP_LEDGER_AGENT                 → institution XP aggregates
  - IDEA_DNA_AGENT                  → innovation economy signals
  - DOJO_SESSION_AGENT              → Dojo participation rates
  - INSTITUTION_VERIFICATION_AGENT  → verified institution metadata

DOWNSTREAM_AGENTS:
  - INSTITUTION_RANK_AGENT          → receives growth_score + antigravity_rank_signal
  - ANTIGRAVITY_LEADERBOARD_AGENT   → receives rank signal for public display
  - ROYALTY_ENGINE                  → receives course engagement velocity
  - OBSERVABILITY_AGENT             → receives all metrics and drift flags
  - NOTIFICATION_DISPATCH_AGENT     → receives risk flags for admin alerts
  - FEATURE_STORE_AGENT             → receives emitted feature vectors

EVENT_TRIGGERS (emitted):
  - GROWTH_FORECAST_READY
  - INSTITUTION_RANK_UPDATED
  - COHORT_RISK_ALERT (conditional: risk >= HIGH)
  - FORECAST_DRIFT_DETECTED (conditional)
  - AGENT_FAILURE_EVENT (conditional)
```

Every agent dependency must be declared before execution begins.
Undeclared dependencies → STOP EXECUTION.

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 11 — PASSIVE INTELLIGENCE COMPATIBILITY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

This agent touches institutional behavioral data. It must emit structured feature
vectors to FEATURE_STORE_AGENT after every successful forecast execution.

```json
EMIT_FEATURE_VECTOR: {
  "entity_type":    "institution",
  "entity_id":      "institution_id (UUID)",
  "tenant_id":      "UUID",
  "feature_name":   "institution_growth_score",
  "feature_value":  "float [0.0–100.0]",
  "timestamp_utc":  "ISO-8601",
  "source_agent":   "SCHOOL_GROWTH_FORECAST_AGENT",
  "model_version":  "string"
}
```

Additional vectors emitted per execution:

```
institution_enrollment_velocity   → float
institution_skill_completion_rate → float
institution_xp_contribution       → float
institution_cohort_risk_level     → ENUM string
institution_antigravity_rank      → float
```

Failure to emit feature vectors → LOG_WARNING → retry async within 60s.

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 12 — INNOVATION ECONOMY COMPATIBILITY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

If the institution cohort has active idea submissions (idea_submission_count_30d > 0),
the agent must emit an innovation signal compatible with the IDEA_DNA_AGENT.

```json
EMIT_INNOVATION_SIGNAL: {
  "institution_id":        "UUID",
  "cohort_aggregate": {
    "idea_submission_count": "integer",
    "avg_originality_score":  "float [0.0–1.0]",
    "similarity_cluster":     "string (cluster ID from IDEA_DNA_AGENT)"
  },
  "innovation_index":       "float [0.0–100.0]",
  "timestamp_utc":          "ISO-8601",
  "source_agent":           "SCHOOL_GROWTH_FORECAST_AGENT"
}
```

Compatible with:
- IDEA_DNA_AGENT
- ROYALTY_ENGINE (for institutional royalty share calculations)
- COPY_DETECTION_ENGINE (for originality validation at cohort level)

This signal contributes to the institution's Antigravity ranking via innovation dimension.

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 13 — GROWTH ENGINE HOOK
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

This agent affects institution ranking and achievement systems. It must trigger:

```
RANK_UPDATE_EVENT:
  {
    "entity_type":  "institution",
    "entity_id":    "institution_id",
    "rank_signal":  "antigravity_rank_signal (float)",
    "dimension":    "growth_forecast",
    "timestamp_utc": "ISO-8601"
  }

XP_UPDATE_EVENT:
  {
    "entity_type":     "institution",
    "entity_id":       "institution_id",
    "xp_delta":        "xp_contribution_index * multiplier",
    "xp_source":       "SCHOOL_GROWTH_FORECAST_AGENT",
    "timestamp_utc":   "ISO-8601"
  }

SHARE_TRIGGER_EVENT (conditional — growth_score > 80.0):
  {
    "entity_type":     "institution",
    "entity_id":       "institution_id",
    "share_context":   "Top Growth Institution",
    "share_surface":   ["institution_feed", "platform_leaderboard"],
    "timestamp_utc":   "ISO-8601"
  }
```

All growth engine events emitted via Redis Streams event bus.
No event may be emitted without completed audit_reference linkage.

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 14 — SCALE, COMPATIBILITY & ECONOMICS
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

### 14.1 Scale Design

```
TARGET_SCALE: 10M–100M users on Ecoskiller Antigravity

Institution forecast load assumptions:
  - 50,000 verified institutions at 10M user scale
  - 500,000 verified institutions at 100M user scale
  - Average: 3 forecast refreshes per institution per day
  = 150K–1.5M forecast jobs per day at scale

Throughput design:
  - Redis Streams queue: partitioned by tenant shard
  - Kubernetes HPA: auto-scales to 50 replicas under peak
  - PostgreSQL read replicas: 5 replicas for forecast read queries
  - OpenSearch: institution analytics index (dedicated)
  - Batch forecast runner: nightly scheduled job for all institutions
  - Real-time forecast trigger: on admin dashboard open or scheduled event

Data Retention:
  - Forecast results: 2 years (hot), 5 years (cold)
  - Audit logs: 7 years (append-only cold storage)
  - Feature vectors: 1 year rolling window
```

### 14.2 Economic Compatibility

```
ROYALTY_ENGINE integration:
  - Enrollment velocity → projects course revenue contribution
  - innovation_index → informs institutional royalty share tier
  - growth_score → unlocks premium Antigravity visibility tiers

Institution Antigravity Tier Model:
  growth_score 0–40:   SEED tier     → standard visibility
  growth_score 40–60:  RISING tier   → boosted discovery
  growth_score 60–80:  MOMENTUM tier → featured placement + SHARE_TRIGGER
  growth_score 80–100: ANTIGRAVITY tier → top leaderboard + royalty multiplier active

Economics signals emitted to ROYALTY_ENGINE:
  {
    "institution_id":        "UUID",
    "antigravity_tier":      "SEED|RISING|MOMENTUM|ANTIGRAVITY",
    "revenue_contribution_index": "float",
    "royalty_multiplier":    "float [1.0–2.5]",
    "billing_period":        "ISO-8601 month",
    "source_agent":          "SCHOOL_GROWTH_FORECAST_AGENT"
  }
```

### 14.3 Compatibility Matrix

```
COMPATIBLE WITH:
  ✅ Ecoskiller Core Platform (Institution domain)
  ✅ Dojo For Arts (Dojo session participation signals)
  ✅ Antigravity Leaderboard Engine
  ✅ Innovation Economy (Royalty + Idea DNA pipeline)
  ✅ Passive Intelligence Layer (Feature Store)
  ✅ XP Ledger System
  ✅ Notification & Moderation Stack
  ✅ CQRS Event Store (append-only)
  ✅ OpenSearch Analytics Index
  ✅ Keycloak RBAC (JWT scope enforcement)
  ✅ Kong API Gateway (rate limiting + auth)
  ✅ Kubernetes autoscaling (stateless pods)
  ✅ CI/CD contract-gate system (Lane F dependency)

NOT COMPATIBLE WITH (by design):
  ❌ Cross-tenant data queries
  ❌ Student PII direct access
  ❌ Manual forecast overrides without audit trail
  ❌ Financial transaction execution (advisory only)
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 15 — PERFORMANCE MONITORING
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
METRICS (all exposed to OBSERVABILITY_AGENT):

  success_rate:              % of forecast jobs completing without error (target > 99.5%)
  error_rate:                % of jobs failing (alert threshold > 0.5%)
  validation_rejection_rate: % of inputs rejected (alert if > 5%)
  latency_p50:               target < 400ms
  latency_p95:               target < 800ms
  latency_p99:               target < 2000ms
  queue_depth:               alert if > 1000 jobs pending
  cache_hit_rate:            target > 70%
  model_drift_indicator:     0 = stable | 1 = drift detected
  anomaly_frequency:         rolling 24h count of anomaly_flag emissions
  ai_layer_availability:     % of requests where AI layer succeeded
  confidence_below_threshold: count of outputs with confidence < 0.55

DASHBOARD:
  Grafana panel: SCHOOL_GROWTH_FORECAST_AGENT
  Alerts routed to: PagerDuty → ML_OPS_TEAM, OPS_TEAM

MUST INTEGRATE WITH:
  OBSERVABILITY_AGENT (metrics push every 60s)
  AUDIT_LOG_SERVICE (every execution)
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 16 — VERSIONING POLICY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
VERSIONING LAW:
  - All changes: Add-only
  - All changes: Versioned via semver
  - All changes: Backward compatible
  - All changes: Migration documented before deployment
  - All changes: Rollback safe

  MAJOR version bump required for:
    - Input schema breaking change
    - Output schema breaking change
    - ML model architecture change

  MINOR version bump required for:
    - New optional input field
    - New optional output field
    - New feature vector emission

  PATCH version bump required for:
    - Bug fixes
    - Performance improvements
    - Prompt updates (minor)

  NO in-place schema mutation permitted.
  NO historical record modification permitted.
  NO audit log deletion permitted under any version bump.
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 17 — NON-NEGOTIABLE RULES (SEALED)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

This agent MUST NOT under any condition:

```
❌ Create hidden logic outside this specification
❌ Modify or delete historical forecast records
❌ Auto-delete audit logs
❌ Override INSTITUTION_RANK_AGENT or ANTIGRAVITY_LEADERBOARD_AGENT governance
❌ Bypass Keycloak JWT / RBAC checks
❌ Mix institution data across tenant boundaries
❌ Execute outside declared PRIMARY_DOMAIN (institution_analytics)
❌ Allow AI layer to override ML-derived confidence scores
❌ Emit financial transactions — advisory signals only
❌ Access student PII — aggregate signals only
❌ Accept forecast requests from unverified institutions
❌ Emit events without a linked audit_reference UUID
❌ Operate without declared upstream agent contracts ready
❌ Produce partial outputs silently — STOP or COMPLETE, never partial
```

Violation of any rule above → STOP EXECUTION → LOG_INCIDENT → ESCALATE

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 18 — DATABASE SCHEMA (REFERENCE)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```sql
-- Forecast results store (append-only, immutable after write)
CREATE TABLE institution_growth_forecasts (
  id                          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  audit_reference             UUID NOT NULL UNIQUE,
  institution_id              UUID NOT NULL,
  tenant_id                   UUID NOT NULL,
  forecast_window_days        INTEGER NOT NULL CHECK (forecast_window_days IN (30,60,90,180)),
  as_of_date_utc              TIMESTAMPTZ NOT NULL,
  generated_at_utc            TIMESTAMPTZ NOT NULL DEFAULT now(),
  enrollment_velocity_score   NUMERIC(6,3) NOT NULL,
  skill_completion_index      NUMERIC(6,3) NOT NULL,
  cohort_progression_risk     VARCHAR(10) NOT NULL,
  xp_contribution_index       NUMERIC(6,3) NOT NULL,
  institution_growth_score    NUMERIC(6,3) NOT NULL,
  antigravity_rank_signal     NUMERIC(6,5) NOT NULL,
  forecast_trajectory         JSONB NOT NULL,
  recommendation_vector       JSONB NOT NULL,
  anomaly_flags               TEXT[] DEFAULT '{}',
  peer_benchmark_percentile   NUMERIC(6,3),
  confidence_score            NUMERIC(5,4) NOT NULL,
  model_version               VARCHAR(50) NOT NULL,
  prompt_version              VARCHAR(50),
  input_hash                  CHAR(64) NOT NULL,
  output_hash                 CHAR(64) NOT NULL,
  decision_path               VARCHAR(20) NOT NULL,
  cache_hit                   BOOLEAN NOT NULL DEFAULT FALSE,
  created_at                  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Immutability enforcement
CREATE RULE no_update_forecasts AS ON UPDATE TO institution_growth_forecasts DO INSTEAD NOTHING;
CREATE RULE no_delete_forecasts AS ON DELETE TO institution_growth_forecasts DO INSTEAD NOTHING;

-- Indexes
CREATE INDEX idx_igf_institution_tenant ON institution_growth_forecasts (institution_id, tenant_id);
CREATE INDEX idx_igf_generated_at ON institution_growth_forecasts (generated_at_utc DESC);
CREATE INDEX idx_igf_window ON institution_growth_forecasts (forecast_window_days);
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 19 — API CONTRACT (REFERENCE)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# OpenAPI 3.1 reference (excerpt)

/v1/institutions/{institution_id}/growth-forecast:
  post:
    summary: Trigger growth forecast for institution
    security:
      - BearerAuth: [institution_analytics:write]
    parameters:
      - name: institution_id
        in: path
        required: true
        schema:
          type: string
          format: uuid
    requestBody:
      required: true
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/GrowthForecastRequest'
    responses:
      202:
        description: Forecast job accepted (async)
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ForecastJobAccepted'
      400:
        description: Validation failure — structured error payload
      401:
        description: Unauthorized
      403:
        description: Tenant/domain isolation violation
      429:
        description: Rate limit exceeded

  get:
    summary: Retrieve latest forecast for institution
    security:
      - BearerAuth: [institution_analytics:read]
    parameters:
      - name: institution_id
        in: path
        required: true
      - name: forecast_window_days
        in: query
        schema:
          type: integer
          enum: [30, 60, 90, 180]
    responses:
      200:
        description: Latest forecast result
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/GrowthForecastResult'
      404:
        description: No forecast found for institution
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 20 — EXECUTION CHECKLIST (SEALED)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Before any forecast execution begins, ALL of the following must be true:

```
PRE-EXECUTION GATE:
  ✅ INSTITUTION_VERIFICATION_AGENT confirms institution is verified
  ✅ JWT scope validated: institution_analytics:read OR write
  ✅ tenant_id claim matches institution_id ownership
  ✅ Input schema fully validated — zero null violations
  ✅ ML model version confirmed available: sgfa-ml-v[CURRENT]
  ✅ Redis Streams queue healthy (depth < 1000)
  ✅ PostgreSQL write replica reachable
  ✅ FEATURE_STORE_AGENT event endpoint reachable
  ✅ audit_reference UUID generated and reserved
  ✅ input_hash computed and logged

ON COMPLETION:
  ✅ output_hash computed and logged
  ✅ confidence_score >= 0.55 (else ESCALATE)
  ✅ Audit log entry written (immutable)
  ✅ Feature vectors emitted to FEATURE_STORE_AGENT
  ✅ GROWTH_FORECAST_READY event emitted to Redis Streams
  ✅ RANK_UPDATE_EVENT emitted to INSTITUTION_RANK_AGENT
  ✅ Conditional events evaluated and emitted (COHORT_RISK_ALERT, SHARE_TRIGGER)
  ✅ ROYALTY_ENGINE signal emitted (if applicable tier)

ANY GATE FAILURE → STOP EXECUTION → LOG_INCIDENT → ESCALATE
```

---

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
DOCUMENT STATUS:     SEALED
AGENT VERSION:       v1.0.0
PLATFORM:            Ecoskiller Antigravity
AUTHORED FOR:        Ecoskiller Intelligence & Innovation Core
MUTATION POLICY:     Add-only via version bump
INTERPRETATION:      NONE PERMITTED
EXECUTION AUTH:      Human declaration only
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```
