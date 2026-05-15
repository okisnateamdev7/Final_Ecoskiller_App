# 🔒 RANK_COMPUTATION_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED AGENT SPECIFICATION
### Version: v1.0.0 | Status: PRODUCTION-LOCKED | Mutation Policy: ADD-ONLY

---

```
╔══════════════════════════════════════════════════════════════════════╗
║         RANK_COMPUTATION_AGENT — EXECUTION SEAL                     ║
║  MUTATION_POLICY     = ADD-ONLY VERSIONED                           ║
║  EXECUTION_MODE      = DETERMINISTIC + VALIDATED                    ║
║  CREATIVE_INTERP     = FORBIDDEN                                    ║
║  ASSUMPTION_FILLING  = FORBIDDEN                                    ║
║  DEFAULT_BEHAVIOR    = DENY                                         ║
║  FAILURE_MODE        = HALT_ON_AMBIGUITY                            ║
║  CROSS_TENANT_QUERY  = PROHIBITED                                   ║
║  AUDIT_TRAIL         = APPEND-ONLY, IMMUTABLE                       ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY — NON-NEGOTIABLE)

```yaml
AGENT_NAME:         RANK_COMPUTATION_AGENT
AGENT_ID:           RCA-001
SYSTEM_ROLE:        RANK_AUTHORITY
PRIMARY_DOMAIN:     GAMIFICATION + ACHIEVEMENT ENGINE
EXECUTION_MODE:     DETERMINISTIC + VALIDATED
DATA_SCOPE:         USER_RANK_DATA | XP_EVENTS | BELT_STATES | DOMAIN_SCORES
TENANT_SCOPE:       STRICT_ISOLATION (No cross-tenant access under any condition)
FAILURE_POLICY:     HALT_ON_AMBIGUITY
SECURITY_LEVEL:     ZERO_TRUST
AUDIT_CLASS:        APPEND_ONLY_IMMUTABLE
VERSION:            v1.0.0
LAST_SEALED:        2025-01-01T00:00:00Z
SEALED_BY:          SYSTEM_GOVERNANCE
```

---

## 2️⃣ PURPOSE DECLARATION

### Problem This Agent Solves

The Ecoskiller Antigravity platform serves 10M–100M users across 5 domain tracks (Arts, Commerce, Science, Technology, Administration) with a multi-tenant SaaS architecture. Without a deterministic, isolated, auditable rank computation engine, rank manipulation, cross-tenant data contamination, and non-reproducible ranking outcomes become critical risks.

`RANK_COMPUTATION_AGENT` is the **single authoritative source of truth** for all user ranking, XP computation, belt progression, and domain-level standing across the Ecoskiller Antigravity platform. It enforces mathematical determinism — identical inputs must produce identical outputs across every execution, every tenant, every domain.

### What Input It Consumes

- Raw XP events emitted from Activity Agents (match completions, session scores, achievements, streaks, referrals)
- Belt evaluation results from `BELT_ENGINE_AGENT`
- Domain assessment scores from `SCORING_AGENT`
- Peer rating data from `PEER_EVALUATION_AGENT`
- Challenge completion events from `CHALLENGE_ENGINE_AGENT`
- Cheating flags and penalty events from `ANTI_CHEAT_AGENT`
- Time-decay parameters from `DRIFT_MONITOR_AGENT`

### What Output It Produces

- Authoritative `RANK_UPDATE_EVENT` (consumed by UI and leaderboard layers)
- `XP_UPDATE_EVENT` (consumed by progression systems)
- `BELT_ELIGIBILITY_SIGNAL` (consumed by `BELT_ENGINE_AGENT`)
- `FEATURE_VECTOR` (consumed by `FEATURE_STORE_AGENT` for passive intelligence)
- Ranked percentile position per domain, per tenant, per user
- Confidence-scored rank computation with full audit reference

### Downstream Agents That Depend On It

```
LEADERBOARD_DISPLAY_AGENT
BELT_ENGINE_AGENT
NOTIFICATION_AGENT
SHARE_TRIGGER_AGENT
ANALYTICS_AGENT
OBSERVABILITY_AGENT
FEATURE_STORE_AGENT
ROYALTY_ENGINE (if user is a Trainer/Mentor)
RECRUITER_MATCH_AGENT (rank signal for placement probability)
```

### Upstream Agents That Feed It

```
ACTIVITY_TRACKER_AGENT
SCORING_AGENT
PEER_EVALUATION_AGENT
ANTI_CHEAT_AGENT
CHALLENGE_ENGINE_AGENT
BELT_ENGINE_AGENT
STREAK_AGENT
REFERRAL_ENGINE_AGENT
```

---

## 3️⃣ INPUT CONTRACT (STRICT)

### INPUT_SCHEMA

```json
{
  "required_fields": {
    "event_id":         "UUID — globally unique, used for idempotency check",
    "tenant_id":        "UUID — strict tenant scope, validated before processing",
    "user_id":          "UUID — must belong to declared tenant_id",
    "domain_track":     "ENUM[Arts|Commerce|Science|Technology|Administration]",
    "event_type":       "ENUM[XP_EARNED|BELT_EVALUATED|MATCH_COMPLETED|PENALTY_APPLIED|STREAK_MILESTONE|CHALLENGE_COMPLETED|PEER_RATING_RECEIVED|SESSION_SCORED]",
    "raw_xp_delta":     "INTEGER — signed (+/-), range [-10000, +10000] per single event",
    "source_agent":     "STRING — must be in APPROVED_UPSTREAM_AGENTS registry",
    "timestamp_utc":    "ISO8601 UTC — must not be future-dated beyond +60s drift allowance",
    "schema_version":   "STRING — must match current or N-1 version"
  },
  "optional_fields": {
    "belt_level":            "ENUM[White|Yellow|Orange|Green|Blue|Purple|Brown|Red|Black] — present if event_type=BELT_EVALUATED",
    "match_id":              "UUID — present if event_type=MATCH_COMPLETED",
    "session_id":            "UUID — present if event_type=SESSION_SCORED",
    "peer_rating_value":     "FLOAT[1.0–5.0] — present if event_type=PEER_RATING_RECEIVED",
    "challenge_id":          "UUID — present if event_type=CHALLENGE_COMPLETED",
    "penalty_reason":        "STRING — present if event_type=PENALTY_APPLIED",
    "multiplier_context":    "OBJECT — win streak, daily bonus, sponsored challenge bonus",
    "streak_count":          "INTEGER[0–∞] — active streak at time of event",
    "anti_cheat_clearance":  "BOOLEAN — must be TRUE for XP events from Dojo/Match sessions"
  },
  "validation_rules": [
    "event_id MUST be unique — duplicate event_id = REJECT (idempotency guard)",
    "tenant_id MUST match user_id's registered tenant from IDENTITY_SERVICE",
    "domain_track MUST match user's enrolled domain from USER_PROFILE_AGENT",
    "raw_xp_delta MUST NOT exceed tier-maximum defined in RANK_CONFIG_v{n}",
    "timestamp_utc MUST be within [event_created - 30s, event_created + 60s] drift window",
    "source_agent MUST be registered in APPROVED_UPSTREAM_AGENTS registry",
    "schema_version MUST be current or N-1 (N-2 and older = REJECT)",
    "anti_cheat_clearance MUST be TRUE for all match and session XP events",
    "multiplier_context values MUST not exceed configured caps per RANK_CONFIG",
    "PENALTY events MUST have penalty_reason — empty string = REJECT"
  ],
  "security_checks": [
    "JWT token tenant_id claim MUST equal input tenant_id",
    "Role-based authorization: only APPROVED_UPSTREAM_AGENTS may write to this agent's input queue",
    "No PII fields allowed in input payload beyond user_id (UUID only)",
    "Payload hash must match declared checksum in event envelope",
    "Rate limit: max 500 XP events per user per 60-second window — excess = QUEUE_BACKPRESSURE"
  ],
  "domain_checks": [
    "user_id MUST be enrolled in declared domain_track",
    "Domain track MUST be active (not suspended/archived) for this tenant",
    "If domain_track = Arts → belt scale and scoring rubric = ARTS_RUBRIC_v{n}",
    "If domain_track = Technology → belt scale and scoring rubric = TECH_RUBRIC_v{n}",
    "Each domain maintains isolated rank pools — cross-domain rank aggregation = FORBIDDEN unless user has multi-domain enrollment explicitly granted"
  ]
}
```

### Null Tolerance Policy

```
NULL_TOLERANCE:
  required_fields:  ZERO — any null in required = REJECT + LOG_VALIDATION_FAILURE
  optional_fields:  ALLOWED — treated as absent, never defaulted silently
  SILENT_DEFAULTS:  FORBIDDEN — no field may be assumed or inferred
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

### OUTPUT_SCHEMA

```json
{
  "result_object": {
    "user_id":                "UUID",
    "tenant_id":              "UUID",
    "domain_track":           "ENUM[Arts|Commerce|Science|Technology|Administration]",
    "rank_before":            "INTEGER — rank position before this computation",
    "rank_after":             "INTEGER — rank position after this computation",
    "xp_before":              "INTEGER — total XP before event",
    "xp_after":               "INTEGER — total XP after event",
    "xp_delta_applied":       "INTEGER — actual delta after caps, penalties, multipliers",
    "belt_level_current":     "ENUM — current belt after evaluation",
    "belt_changed":           "BOOLEAN",
    "percentile_position":    "FLOAT[0.0–100.0] — user's position in domain rank pool",
    "rank_tier":              "ENUM[Aspirant|Practitioner|Specialist|Expert|Master|GrandMaster]",
    "rank_velocity":          "FLOAT — rank change rate over last 7 days",
    "leaderboard_eligible":   "BOOLEAN — false if penalty flag, anti-cheat hold, or probation",
    "next_belt_xp_required":  "INTEGER — XP gap to next belt threshold",
    "computation_breakdown":  {
      "base_xp":              "INTEGER",
      "streak_multiplier":    "FLOAT",
      "domain_weight":        "FLOAT",
      "peer_rating_factor":   "FLOAT",
      "belt_bonus":           "INTEGER",
      "penalty_deduction":    "INTEGER",
      "cap_applied":          "BOOLEAN",
      "final_xp_delta":       "INTEGER"
    }
  },
  "confidence_score":      "FLOAT[0.0–1.0] — computation confidence (below 0.75 = FLAG)",
  "model_version":         "STRING — e.g., RCA-ML-v2.3.1",
  "audit_reference":       "UUID — immutable pointer to audit log entry",
  "next_trigger_event": [
    "RANK_UPDATE_EVENT",
    "XP_UPDATE_EVENT",
    "BELT_ELIGIBILITY_CHECK (if belt_changed=true)",
    "SHARE_TRIGGER_EVENT (if rank milestone reached)",
    "FEATURE_VECTOR_EMIT"
  ]
}
```

### Output Guarantees

```
EVERY output MUST include:
  ✅ confidence_score
  ✅ model_version
  ✅ audit_reference (UUID, immutable)
  ✅ next_trigger_event list (may be empty array, never null)
  ✅ full computation_breakdown (no black-box outputs)

FORBIDDEN in output:
  ❌ Null audit_reference
  ❌ Confidence score omission
  ❌ Partial computation_breakdown
  ❌ Output without rank_before/rank_after pair
```

---

## 5️⃣ ML / AI LOGIC LAYER

### ML Layer (PRIMARY — 75% of computation)

```yaml
MODEL_TYPE:
  PRIMARY:      Gradient Boosted Ranking Model (LightGBM)
  SECONDARY:    Time-Series Decay Model (ARIMA-variant for rank velocity)
  TERTIARY:     Anomaly Detection Classifier (Isolation Forest — for XP spike detection)

FEATURES_USED:
  # User Performance Features
  - cumulative_xp_total
  - xp_7d_rolling_sum
  - xp_30d_rolling_sum
  - match_win_rate_30d
  - session_completion_rate
  - peer_rating_avg_30d
  - streak_current_length
  - streak_max_lifetime
  - belt_level_ordinal
  - domain_track_encoded
  - challenge_completion_rate
  
  # Contextual Features
  - tenant_cohort_size
  - domain_active_user_count
  - days_since_account_creation
  - days_since_last_active
  - premium_tier_flag
  
  # Anti-Gaming Features
  - xp_variance_24h
  - session_frequency_anomaly_score
  - peer_rating_consistency_score
  - anti_cheat_flag_count_30d

TRAINING_FREQUENCY:
  RANK_MODEL:       Weekly (Sunday 02:00 UTC)
  DECAY_MODEL:      Monthly (1st of month 03:00 UTC)
  ANOMALY_MODEL:    Weekly (Monday 02:00 UTC)

DRIFT_DETECTION:
  XP_DISTRIBUTION:  Monitor with KS-test weekly — alert if p < 0.05
  RANK_ACCURACY:    Monitor Spearman correlation vs. manual audit weekly
  FEATURE_DRIFT:    Monitor PSI (Population Stability Index) per feature
  ALERT_THRESHOLD:  PSI > 0.2 = WARNING | PSI > 0.25 = RETRAIN_TRIGGER
  ACCURACY_FLOOR:   Spearman ρ < 0.90 = RETRAIN_TRIGGER + ESCALATE

VERSION_CONTROL:
  MODEL_REGISTRY:   MLflow (internal)
  VERSIONING:       Semantic — MAJOR.MINOR.PATCH
  IMMUTABILITY:     Deployed model artifacts = READ_ONLY, never overwritten
  ROLLBACK:         N-1 version maintained hot — auto-rollback if drift breach
  SHADOW_TESTING:   New model runs in shadow for 72h before promotion

PREDICTION_OUTPUT:
  RANK_SCORE:       FLOAT[0.0–1.0] normalized ranking signal
  VELOCITY_SIGNAL:  FLOAT — predicted rank change rate
  ANOMALY_FLAG:     BOOLEAN — triggers hold on XP application if TRUE
```

### AI Layer (SUPPLEMENTAL — 25% of computation)

```yaml
AI_USAGE_SCOPE:
  - Narrative rank summary generation (for user-facing rank card)
  - Outlier explanation (when anomaly_flag=TRUE, AI generates human-readable reason)
  - Belt recommendation rationale (contextual, non-binding)

AI_DECISION_AUTONOMY:    NONE
  # AI NEVER approves or denies rank changes
  # AI NEVER overrides ML model outputs
  # AI output is informational ONLY — never stored as authoritative data

PROMPT_GOVERNANCE:
  VERSIONING:           Prompt templates versioned alongside model_version
  TEMPLATE_ID:          RANK_NARRATIVE_PROMPT_v{n}
  DETERMINISM:          Temperature = 0.0 (zero-temperature, no creative drift)
  MAX_TOKENS:           150 (narrative summary only)
  FORBIDDEN_OUTPUTS:    Specific rank promises | Career guarantees | Comparison to named users
  PII_FILTER:           Strip all user PII before prompt construction
  AUDIT:                Prompt hash + response hash logged per invocation
```

---

## 6️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS:
  BASELINE:     5,000 RPS (normal load)
  PEAK:         50,000 RPS (post-match burst, tournament end events)
  DESIGN_FOR:   100,000 RPS (10M–100M user headroom)

LATENCY_TARGET:
  P50:    < 50ms
  P95:    < 200ms
  P99:    < 500ms
  P99.9:  < 2,000ms (burst tolerance)

MAX_CONCURRENCY:     10,000 concurrent computation threads

QUEUE_STRATEGY:
  TECHNOLOGY:         Apache Kafka
  TOPIC:              rank.computation.events.{tenant_id}
  PARTITIONING:       By user_id hash (consistent hashing — same user = same partition)
  CONSUMER_GROUP:     rank-computation-cg-{tenant_id}
  RETENTION:          7 days (for replay on failure)
  DLQ:                rank.computation.dlq.{tenant_id} (failed events for manual review)

EXECUTION_MODEL:
  STATELESS:          TRUE — no local state stored in agent instance
  IDEMPOTENCY:        event_id deduplication via Redis SET NX with 24h TTL
  ASYNC_PROCESSING:   TRUE — event-driven via Kafka, no synchronous blocking
  HORIZONTAL_SCALING: TRUE — Kubernetes HPA on CPU + queue lag metric
  CACHE:
    USER_XP_SNAPSHOT:       Redis, TTL=300s (5 min)
    RANK_CONFIG:            Redis, TTL=3600s (1hr) — invalidated on config push
    DOMAIN_POOL_SIZE:       Redis, TTL=60s — refreshed by STATS_AGENT
```

---

## 7️⃣ SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  ENFORCEMENT:      Hard — tenant_id validated at ingress, computation, and output
  ISOLATION_LEVEL:  Row-level security enforced at database layer (PostgreSQL RLS)
  CROSS_TENANT:     FORBIDDEN — any cross-tenant query = SECURITY_VIOLATION event
  LOGGING:          Every rejected cross-tenant attempt logged to SECURITY_AUDIT_LOG

DOMAIN_ISOLATION:
  ENFORCEMENT:      Domain rank pools are physically separate keys in Redis and separate table partitions in PostgreSQL
  CROSS_DOMAIN:     Forbidden unless user has explicit multi-domain grant in USER_PROFILE
  VIOLATION:        Trigger SECURITY_VIOLATION + halt computation

AUTHORIZATION:
  INBOUND_CALLERS:  Only agents in APPROVED_UPSTREAM_AGENTS registry (validated via service mesh JWT)
  OUTPUT_CONSUMERS: Only agents in APPROVED_DOWNSTREAM_AGENTS registry
  HUMAN_ACCESS:     No direct human access to computation input queue
  ADMIN_OVERRIDE:   FORBIDDEN — admins may VIEW audit logs only, never mutate rank records

ENCRYPTION:
  IN_TRANSIT:       TLS 1.3 mandatory (TLS 1.2 = REJECT)
  AT_REST:          AES-256-GCM for PostgreSQL + Redis persistent data
  KAFKA_MESSAGES:   Payload encrypted before publish, decrypted by authorized consumers only

AUDIT_LOGGING:
  TYPE:             Append-only — no UPDATE or DELETE on audit records
  STORAGE:          Separate write-once audit database (PostgreSQL with triggers blocking UPDATE/DELETE)
  ACCESS:           COMPLIANCE_ADMIN role only (read), GOVERNANCE_AGENT (write via agent only)

PII_HANDLING:
  IN_COMPUTATION:   user_id (UUID) only — no name, email, or personal data in computation pipeline
  IN_AUDIT:         user_id + audit_reference only
  IN_AI_PROMPTS:    PII stripped before prompt construction — enforced by PRE_PROMPT_SANITIZER
```

---

## 8️⃣ AUDIT & TRACEABILITY

Every execution emits an immutable audit record:

```json
{
  "audit_id":           "UUID — primary key, globally unique",
  "timestamp_utc":      "ISO8601 UTC",
  "actor_id":           "UUID — user_id of subject",
  "agent_id":           "RCA-001",
  "tenant_id":          "UUID",
  "domain_track":       "ENUM",
  "input_hash":         "SHA-256 of full input payload",
  "output_hash":        "SHA-256 of full output payload",
  "model_version":      "STRING — e.g., RCA-ML-v2.3.1",
  "prompt_version":     "STRING — if AI layer invoked, e.g., RANK_NARRATIVE_PROMPT_v3",
  "decision_path":      {
    "ml_rank_score":        "FLOAT",
    "anomaly_flag":         "BOOLEAN",
    "multipliers_applied":  "ARRAY",
    "caps_triggered":       "BOOLEAN",
    "belt_check_triggered": "BOOLEAN",
    "penalty_applied":      "BOOLEAN"
  },
  "confidence_score":   "FLOAT",
  "anomaly_flags":      "ARRAY[STRING] — list of triggered anomaly rules, empty if none",
  "processing_ms":      "INTEGER — computation duration in milliseconds",
  "schema_version":     "STRING"
}
```

### Audit Integrity Rules

```
IMMUTABILITY:         Audit records CANNOT be updated or deleted after write
REPLAY_CAPABILITY:    Given input_hash, computation is fully reproducible
RETENTION:            Minimum 7 years (compliance requirement)
TAMPERING_DETECTION:  Audit chain hash — each record includes hash of previous record
EXPORT:               Permitted only via COMPLIANCE_EXPORT_AGENT with GDPR-aware anonymization
```

---

## 9️⃣ FAILURE POLICY

### Failure Scenarios & Mandatory Responses

```yaml
INVALID_INPUT:
  ACTION:         REJECT_EVENT
  RESPONSE:       Return structured error with validation_failure_code
  LOG:            LOG_VALIDATION_FAILURE to audit log
  DLQ:            Route event to DLQ with failure reason
  RETRY:          NOT APPLICABLE — invalid input must be fixed at source
  ESCALATE_TO:    SOURCE_AGENT + OBSERVABILITY_AGENT

MODEL_UNAVAILABLE:
  ACTION:         STOP_EXECUTION
  RESPONSE:       Emit MODEL_UNAVAILABLE event to OBSERVABILITY_AGENT
  LOG:            LOG_INCIDENT with model_version + failure_type
  RETRY_POLICY:   3 retries with exponential backoff (1s, 4s, 16s), then HALT
  ESCALATE_TO:    PLATFORM_OPS_ALERT (PagerDuty)
  FALLBACK:       NO FALLBACK — rank cannot be computed without model
  USER_IMPACT:    XP event queued for deferred processing (max 15 min queue TTL)

AI_TIMEOUT:
  ACTION:         CONTINUE_WITHOUT_AI (ML result is authoritative)
  LOG:            LOG_AI_TIMEOUT with prompt_version
  NARRATIVE:      Skip narrative generation — rank update proceeds
  ESCALATE_TO:    OBSERVABILITY_AGENT
  RETRY:          1 retry after 2s, then skip AI layer for this execution

DATA_CORRUPTION:
  ACTION:         HALT_EXECUTION
  LOG:            LOG_CRITICAL — data corruption detected
  ESCALATE_TO:    PLATFORM_OPS_ALERT (P0 incident)
  QUARANTINE:     Affected user rank record LOCKED for manual review
  AUDIT:          Full forensic audit record created immediately

CONFIDENCE_BELOW_THRESHOLD:
  THRESHOLD:      < 0.75
  ACTION:         FLAG_FOR_REVIEW — result NOT applied to leaderboard
  LOG:            LOG_LOW_CONFIDENCE with full decision_path
  ESCALATE_TO:    ANALYTICS_AGENT + OBSERVABILITY_AGENT
  REVIEW:         COMPLIANCE_ADMIN notified within 30 minutes

ANTI_CHEAT_HOLD:
  ACTION:         REJECT_XP_CREDIT — rank NOT updated
  LOG:            LOG_ANTI_CHEAT_HOLD with anti_cheat_clearance=false evidence
  ESCALATE_TO:    ANTI_CHEAT_AGENT + COMPLIANCE_ADMIN
  USER_NOTICE:    Trigger notification via NOTIFICATION_AGENT (appeal process explained)

GENERAL_RULE:
  SILENT_FAILURES: FORBIDDEN — every failure must produce a logged, structured record
  DEFAULT_ON_UNKNOWN_ERROR: HALT_EXECUTION + LOG_UNKNOWN_FAILURE + ESCALATE
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP

### Upstream Agents (Feeds Into RCA)

```
ACTIVITY_TRACKER_AGENT    → XP_EARNED events
SCORING_AGENT             → SESSION_SCORED events
PEER_EVALUATION_AGENT     → PEER_RATING_RECEIVED events
ANTI_CHEAT_AGENT          → anti_cheat_clearance flag on match/session events
CHALLENGE_ENGINE_AGENT    → CHALLENGE_COMPLETED events
BELT_ENGINE_AGENT         → BELT_EVALUATED events (post-belt assessment)
STREAK_AGENT              → STREAK_MILESTONE events + active streak_count
REFERRAL_ENGINE_AGENT     → XP_EARNED events (referral bonuses)
DRIFT_MONITOR_AGENT       → Time-decay parameters (monthly push to RANK_CONFIG)
```

### Downstream Agents (Consumes From RCA)

```
LEADERBOARD_DISPLAY_AGENT → Consumes RANK_UPDATE_EVENT
BELT_ENGINE_AGENT         → Consumes BELT_ELIGIBILITY_SIGNAL
NOTIFICATION_AGENT        → Consumes RANK_UPDATE_EVENT (triggers rank milestone push)
SHARE_TRIGGER_AGENT       → Consumes SHARE_TRIGGER_EVENT (social card generation)
ANALYTICS_AGENT           → Consumes full rank computation payload for aggregate analysis
OBSERVABILITY_AGENT       → Consumes all failure events + performance metrics
FEATURE_STORE_AGENT       → Consumes FEATURE_VECTOR_EMIT
RECRUITER_MATCH_AGENT     → Consumes rank tier + percentile as placement signal
ROYALTY_ENGINE            → Consumes rank signals for Trainer/Mentor compensation weighting
```

### Event Topology

```
[Upstream Agents]
      │
      ▼
Kafka Topic: rank.computation.events.{tenant_id}
      │
      ▼
[RANK_COMPUTATION_AGENT]
      │
      ├──▶ Kafka Topic: rank.updates.{tenant_id}           → LEADERBOARD + NOTIFICATION
      ├──▶ Kafka Topic: xp.updates.{tenant_id}             → BELT_ENGINE
      ├──▶ Kafka Topic: belt.eligibility.{tenant_id}       → BELT_ENGINE_AGENT
      ├──▶ Kafka Topic: share.triggers.{tenant_id}         → SHARE_TRIGGER_AGENT
      ├──▶ Kafka Topic: feature.vectors.{tenant_id}        → FEATURE_STORE_AGENT
      └──▶ Kafka Topic: rank.audit.{tenant_id}             → AUDIT_DATABASE (append-only)
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent emits structured feature vectors for all user rank interactions:

```json
{
  "EMIT_FEATURE_VECTOR": {
    "user_id":            "UUID",
    "feature_name":       "STRING — e.g., rank_percentile_domain_technology",
    "feature_value":      "FLOAT",
    "feature_type":       "ENUM[rank_position|xp_delta|velocity|belt_ordinal|percentile]",
    "domain_track":       "ENUM",
    "tenant_id":          "UUID",
    "timestamp":          "ISO8601 UTC",
    "source_agent":       "RANK_COMPUTATION_AGENT",
    "model_version":      "STRING"
  }
}
```

Feature vectors emitted per computation:

```
rank_percentile_{domain}           — FLOAT[0.0–100.0]
xp_delta_applied                   — INTEGER
rank_velocity_7d                   — FLOAT
belt_ordinal_current               — INTEGER[0–8]
streak_multiplier_applied          — FLOAT
leaderboard_eligible_flag          — BOOLEAN (as 0/1)
confidence_score                   — FLOAT
```

Destination: `FEATURE_STORE_AGENT` (consumed by SKILL_GAP_AGENT, PLACEMENT_PROBABILITY_MODEL, RECRUITER_MATCH_AGENT)

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY

Not directly applicable — this agent does not process ideas or content.

However, if a user earns XP through an Innovator Badge event (from `FEATURE_REQUEST_AGENT` when a feature suggestion is implemented), this agent MUST:

```
ACCEPT:  XP_EARNED event with event_type=INNOVATOR_BADGE_XP
PROCESS: Standard computation pipeline applies
EMIT:    RANK_UPDATE_EVENT + XP_UPDATE_EVENT as normal
```

Direct `IDEA_VECTOR` or `SIMILARITY_HASH` computation: **OUT OF SCOPE — DELEGATED TO IDEA_DNA_AGENT**

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

This agent is the **authoritative trigger** for Growth Engine events:

```yaml
RANK_UPDATE_EVENT:
  TRIGGER:      Every successful rank computation
  PAYLOAD:      rank_before, rank_after, domain_track, belt_level, percentile, tenant_id, user_id
  CONSUMER:     LEADERBOARD_DISPLAY_AGENT, NOTIFICATION_AGENT

XP_UPDATE_EVENT:
  TRIGGER:      Every successful XP delta application
  PAYLOAD:      xp_before, xp_after, xp_delta_applied, computation_breakdown, tenant_id, user_id
  CONSUMER:     BELT_ENGINE_AGENT, ANALYTICS_AGENT

SHARE_TRIGGER_EVENT:
  TRIGGER:      When rank milestone is crossed (defined in RANK_MILESTONE_CONFIG)
  CONDITIONS:   Belt upgrade | Top 10% percentile entry | Rank tier promotion | Win streak ≥ 5
  PAYLOAD:      milestone_type, rank_after, belt_level_current, domain_track, user_id, share_card_template_id
  CONSUMER:     SHARE_TRIGGER_AGENT
```

### Rank Milestone Configuration

```yaml
RANK_MILESTONES:
  - BELT_PROMOTION:         Any belt level increase
  - TOP_PERCENTILE:         Entry into top 10%, 5%, 1%
  - TIER_PROMOTION:         Aspirant→Practitioner→Specialist→Expert→Master→GrandMaster
  - WIN_STREAK_5:           5 consecutive match wins
  - WIN_STREAK_10:          10 consecutive match wins
  - XP_THRESHOLD:           Every 1,000 XP milestone (1000, 2000, 5000, 10000, ...)
  - DOMAIN_RANK_1:          Reaching rank #1 in tenant domain
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

### Metrics Emitted (to OBSERVABILITY_AGENT)

```yaml
OPERATIONAL_METRICS:
  success_rate:             events processed successfully / total events received
  error_rate:               events failed / total events received
  validation_failure_rate:  validation rejects / total events received
  dlq_rate:                 events routed to DLQ / total events received

LATENCY_METRICS:
  p50_latency_ms:           50th percentile end-to-end computation time
  p95_latency_ms:           95th percentile
  p99_latency_ms:           99th percentile
  queue_consumer_lag:       Kafka consumer lag per partition (alert if lag > 10,000 events)

ML_HEALTH_METRICS:
  confidence_score_avg:     Rolling 1hr average — alert if < 0.80
  low_confidence_rate:      % of computations below 0.75 threshold
  drift_indicator:          PSI per feature (published weekly)
  anomaly_frequency:        anomaly flags triggered / total computations

SECURITY_METRICS:
  cross_tenant_attempt_count:   Must = 0 at all times (non-zero = P0 alert)
  auth_rejection_count:         Unauthorized caller attempts
  anti_cheat_hold_count:        XP holds triggered by anti-cheat

INTEGRATION:
  TECHNOLOGY:   Prometheus metrics export
  DASHBOARD:    Grafana (internal ops only, not public)
  ALERTING:     PagerDuty integration via OBSERVABILITY_AGENT
  SLA_TARGET:   success_rate ≥ 99.9% | p99_latency ≤ 500ms
```

---

## 1️⃣5️⃣ VERSIONING POLICY

```yaml
VERSIONING_SCHEME:
  FORMAT:         MAJOR.MINOR.PATCH  (e.g., v1.0.0)
  AGENT_SPEC:     This document — versioned as RCA-SPEC-v{n}
  ML_MODEL:       Versioned as RCA-ML-v{n} in MLflow registry
  PROMPT_TEMPLATE: Versioned as RANK_NARRATIVE_PROMPT-v{n}
  RANK_CONFIG:    Versioned as RANK_CONFIG-v{n} (belt thresholds, XP caps, multipliers)

MUTATION_RULES:
  ADD_ONLY:       New fields may be added to INPUT_SCHEMA or OUTPUT_SCHEMA
  NO_REMOVAL:     Existing fields CANNOT be removed (consumers may depend on them)
  NO_RENAME:      Field names CANNOT change (backward compatibility enforced)
  DEPRECATION:    Fields must be marked deprecated for 2 full versions before removal consideration

BACKWARD_COMPATIBILITY:
  INPUT:          Agent MUST accept current schema version AND N-1
  OUTPUT:         Agent MUST produce current schema version
  CONSUMER_BREAK: Any change that breaks a downstream consumer = MAJOR version bump required

MIGRATION_PROTOCOL:
  DOCUMENT:       Every version change documented in CHANGELOG-RCA.md
  SHADOW_PERIOD:  New version runs in shadow (no production effect) for minimum 72 hours
  ROLLBACK:       N-1 version deployed alongside current — rollback within 5 minutes
  COMMUNICATION:  Downstream agent owners notified minimum 5 business days before MAJOR version

RANK_CONFIG_GOVERNANCE:
  EDIT_AUTHORITY: PLATFORM_GOVERNANCE_ADMIN only
  APPROVAL:       Requires 2-party approval (Governance + Engineering Lead)
  EFFECT:         Configuration changes take effect at next Kafka consumer restart
  AUDIT:          Every config change logged with timestamp + approver IDs
```

---

## 1️⃣6️⃣ NON-NEGOTIABLE RULES

### This Agent MUST NOT:

```
❌  Create hidden logic — all computation must be fully traceable via computation_breakdown
❌  Modify historical records — rank history is append-only; corrections create NEW records
❌  Auto-delete audit logs — no automated deletion of any audit or computation record
❌  Override governance agents — ANTI_CHEAT_AGENT and COMPLIANCE_AGENT decisions are final
❌  Bypass compliance checks — no "fast path" skipping validation or security checks
❌  Mix domain data — Arts domain rank pool NEVER mixed with Technology pool under any condition
❌  Execute outside scope — this agent computes rank ONLY; it does not write to user profiles directly
❌  Accept inputs from unregistered agents — source_agent not in APPROVED_UPSTREAM_AGENTS = REJECT
❌  Grant rank without anti-cheat clearance — match/session XP requires anti_cheat_clearance=TRUE
❌  Produce output without audit_reference — every output without UUID audit pointer is INVALID
❌  Apply silent defaults — all computation parameters must come from RANK_CONFIG, never assumed
❌  Allow AI to override ML — AI layer is informational only, ML model is authoritative
❌  Compute rank cross-tenant — tenant_id isolation is absolute and non-negotiable
❌  Store user PII beyond UUID — no names, emails, or personal data in computation pipeline
❌  Allow rank manipulation by score injection — all XP events must originate from approved upstream agents
```

---

## APPENDIX A — RANK TIER DEFINITIONS (LOCKED)

```yaml
RANK_TIERS:
  Aspirant:       XP[0–999]       | Percentile: 0–40%
  Practitioner:   XP[1000–4999]   | Percentile: 40–65%
  Specialist:     XP[5000–14999]  | Percentile: 65–80%
  Expert:         XP[15000–39999] | Percentile: 80–92%
  Master:         XP[40000–99999] | Percentile: 92–99%
  GrandMaster:    XP[100000+]     | Percentile: Top 1%

NOTE: Percentile bands are tenant + domain scoped.
      A GrandMaster in Arts ≠ ranked against Technology pool.
      Absolute XP thresholds are defined per domain in RANK_CONFIG-v{n}.
```

## APPENDIX B — BELT → XP MAPPING (LOCKED)

```yaml
BELT_SYSTEM:
  White:    Base entry — no XP bonus
  Yellow:   500 XP milestone belt bonus
  Orange:   1,500 XP milestone belt bonus
  Green:    3,000 XP milestone belt bonus
  Blue:     6,000 XP milestone belt bonus
  Purple:   10,000 XP milestone belt bonus
  Brown:    20,000 XP milestone belt bonus
  Red:      40,000 XP milestone belt bonus
  Black:    100,000 XP milestone belt bonus

BELT_BONUS_XP_ON_PROMOTION:
  Each belt promotion grants ONE-TIME belt bonus XP (defined in RANK_CONFIG-v{n}).
  Bonus XP is processed as a synthetic XP_EARNED event from BELT_ENGINE_AGENT.
  Duplicate belt promotion bonuses are idempotency-rejected.
```

## APPENDIX C — XP MULTIPLIER CAPS (LOCKED)

```yaml
MULTIPLIER_CAPS:
  streak_multiplier:          MAX 3.0x (applied to base XP delta)
  sponsored_challenge_bonus:  MAX 2.0x (applied to base XP delta)
  daily_bonus_multiplier:     MAX 1.5x
  combined_cap:               MAX 4.0x (no combination of multipliers may exceed 4x)
  single_event_xp_cap:        MAX 2,000 XP per event (hard ceiling, any event type)
  daily_xp_cap_per_user:      MAX 10,000 XP per 24-hour rolling window

CAP_POLICY:
  If cap_applied=TRUE in output, the computation_breakdown MUST show pre-cap and post-cap values.
  Cap triggers are logged as anomaly_flags with flag_type=XP_CAP_TRIGGERED.
```

---

## SEAL RECORD

```
╔══════════════════════════════════════════════════════════════════════╗
║  DOCUMENT:       RANK_COMPUTATION_AGENT.md                          ║
║  AGENT_ID:       RCA-001                                            ║
║  SPEC_VERSION:   RCA-SPEC-v1.0.0                                    ║
║  PLATFORM:       ECOSKILLER ANTIGRAVITY                             ║
║  SEALED:         2025-01-01T00:00:00Z                               ║
║  STATUS:         PRODUCTION-LOCKED                                  ║
║  NEXT_REVIEW:    TRIGGERED BY VERSION BUMP ONLY                     ║
║  GOVERNANCE:     PLATFORM_GOVERNANCE_ADMIN + ENGINEERING_LEAD       ║
║                                                                     ║
║  THIS DOCUMENT CANNOT BE EDITED WITHOUT:                            ║
║  1. Formal version bump (RCA-SPEC-v1.x.x or v2.0.0)               ║
║  2. Two-party approval                                              ║
║  3. Downstream agent notification (min 5 business days)            ║
║  4. Shadow testing period (min 72 hours)                            ║
║  5. Changelog entry in CHANGELOG-RCA.md                            ║
╚══════════════════════════════════════════════════════════════════════╝
```
