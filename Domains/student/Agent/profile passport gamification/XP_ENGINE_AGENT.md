# 🔒 XP_ENGINE_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED ENTERPRISE AGENT SPECIFICATION
### Domains: Dojo · Growth Engine · School Ops (Institute ERP)
### Version: v1.0.0 | Mutation Policy: ADD-ONLY | Execution Mode: DETERMINISTIC

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║              SEAL BLOCK — DO NOT MODIFY WITHOUT VERSION BUMP                ║
║                                                                              ║
║  EXECUTION_MODE          = DETERMINISTIC + VALIDATED                        ║
║  MUTATION_POLICY         = ADD_ONLY                                         ║
║  CREATIVE_INTERPRETATION = FORBIDDEN                                        ║
║  ASSUMPTION_FILLING      = FORBIDDEN                                        ║
║  DEFAULT_BEHAVIOR        = DENY                                             ║
║  FAILURE_MODE            = HALT_ON_AMBIGUITY                               ║
║  CROSS_TENANT_ACCESS     = FORBIDDEN                                        ║
║  SILENT_FAILURES         = FORBIDDEN                                        ║
║  AUTO_PROMOTION          = FORBIDDEN                                        ║
║  HISTORY_MUTATION        = FORBIDDEN                                        ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY — LOCKED)

```yaml
AGENT_NAME:         XP_ENGINE_AGENT
AGENT_VERSION:      v1.0.0
SYSTEM_ROLE:        Experience Points Computation, Distribution, and Governance Engine
PRIMARY_DOMAIN:     Gamification / Growth / Engagement
EXECUTION_MODE:     Deterministic + Validated
DATA_SCOPE:         User XP ledger · Activity events · Belt eligibility signals ·
                    Achievement unlock checks · School Ops attendance/compliance XP ·
                    Dojo match/score events · Growth referral/social XP events
TENANT_SCOPE:       Strict Per-Tenant Isolation (Institute ≠ Company ≠ Platform)
FAILURE_POLICY:     HALT on ambiguity · LOG_INCIDENT · ESCALATE to GOVERNANCE_AGENT
MODEL_VERSION_REF:  xp_engine_v1.0.0
AUDIT_TRAIL:        Append-only, immutable
```

**This agent must never assume missing specifications.
All undefined inputs → REJECT + LOG. No default XP awarded without explicit event mapping.**

---

## 2️⃣ PURPOSE DECLARATION

### Problem Solved
The XP_ENGINE_AGENT is the single authoritative engine for awarding, validating, versioning, and auditing all Experience Points (XP) earned across the three owner-domains of Ecoskiller Antigravity:

| Domain | XP Surface |
|---|---|
| **Dojo** | Match participation, win/loss, belt promotion milestones, scenario completion, mentor evaluation, tournament placement, streak maintenance |
| **Growth Engine** | Referral completions, social sharing events, feature-unlock referral chains, login streaks, challenge completions, viral mechanic milestones |
| **School Ops (Institute ERP)** | Attendance compliance, assignment submission, institute-assigned task completion, cohort milestones, parent visibility events, compliance badges |

### What It Consumes
- Validated activity events from upstream agents (MATCH_ENGINE_AGENT, GROWTH_EVENT_AGENT, SCHOOL_OPS_AGENT)
- User identity context (tenant_id, domain_track, role, user_id)
- Active XP policy rules from XP_POLICY_STORE (versioned, immutable references)
- Belt eligibility thresholds from BELT_ENGINE_AGENT
- Anti-abuse flags from COLLUSION_DETECTION_AGENT

### What It Produces
- XP delta records (append-only ledger entries)
- Updated cumulative XP snapshots per user
- RANK_UPDATE_EVENT triggers to RANK_ENGINE_AGENT
- BELT_ELIGIBILITY_CHECK_EVENT triggers to BELT_ENGINE_AGENT
- ACHIEVEMENT_CHECK_EVENT triggers to ACHIEVEMENT_ENGINE_AGENT
- FEATURE_VECTOR emissions to FEATURE_STORE_AGENT (passive intelligence)
- Full audit trail per XP award with traceability UUID

### Downstream Agents Depending on This Agent
- `RANK_ENGINE_AGENT`
- `BELT_ENGINE_AGENT`
- `ACHIEVEMENT_ENGINE_AGENT`
- `LEADERBOARD_AGENT`
- `NOTIFICATION_ENGINE_AGENT`
- `ANALYTICS_AGENT` / `OBSERVABILITY_AGENT`
- `FEATURE_STORE_AGENT`

### Upstream Agents Feeding This Agent
- `MATCH_ENGINE_AGENT` (Dojo match result events)
- `SCORING_ENGINE_AGENT` (Validated score results)
- `GROWTH_EVENT_AGENT` (Referral, social, streak events)
- `SCHOOL_OPS_AGENT` (Attendance, assignment, compliance events)
- `COLLUSION_DETECTION_AGENT` (Abuse flags — suppresses XP if flagged)
- `TOURNAMENT_ENGINE_AGENT` (Tournament placement events)

---

## 3️⃣ INPUT CONTRACT (STRICT — LOCKED)

### INPUT_SCHEMA

```json
{
  "required_fields": {
    "event_id":         "UUID — globally unique event identifier (idempotency key)",
    "event_type":       "Enum — see EVENT_TYPE_REGISTRY below",
    "event_source":     "Enum: DOJO | GROWTH | SCHOOL_OPS",
    "user_id":          "UUID — must exist in USER_SERVICE, tenant-scoped",
    "tenant_id":        "UUID — tenant isolation key",
    "domain_track":     "Enum: ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION",
    "actor_role":       "Enum: STUDENT | TRAINER | EVALUATOR | ADMIN | AUTOMATION",
    "timestamp_utc":    "ISO-8601 UTC timestamp",
    "xp_policy_version":"Semver string — e.g. v1.0.0 — must match active policy"
  },
  "optional_fields": {
    "match_id":         "UUID — required when event_source = DOJO",
    "tournament_id":    "UUID — required for tournament events",
    "scenario_id":      "UUID — required for scenario completion events",
    "referral_code":    "String — required for referral XP events",
    "streak_day":       "Integer ≥ 1 — required for login/activity streak events",
    "school_task_id":   "UUID — required for SCHOOL_OPS events",
    "confidence_score": "Float 0.0–1.0 — from SCORING_ENGINE, required for match events",
    "mentor_override":  "Boolean — flags XP requiring mentor board review",
    "collusion_flag":   "Boolean — if true, suppress XP and escalate"
  },
  "validation_rules": [
    "event_id must be globally unique — reject duplicate event_id (idempotency enforcement)",
    "timestamp_utc must be within accepted drift window of ±5 minutes server UTC",
    "xp_policy_version must exactly match the currently active policy version",
    "user_id must belong to tenant_id — cross-tenant injection = SECURITY_FAILURE",
    "actor_role must be authorized to generate event_type — role-event matrix enforced",
    "confidence_score, when provided, must be Float 0.0–1.0 inclusive",
    "streak_day must be Integer ≥ 1 when provided",
    "collusion_flag = true → IMMEDIATE SUPPRESSION, no XP awarded, escalate to GOVERNANCE_AGENT"
  ],
  "security_checks": [
    "JWT bearer token validation on every request",
    "Tenant isolation check: user_id.tenant == tenant_id (hard check)",
    "Domain isolation check: user domain_track authorized for event_type",
    "Role-based authorization: actor_role permitted to submit event_type",
    "Rate-limit enforcement: per user_id and per IP",
    "Replay attack prevention: event_id idempotency key checked against dedup store (Redis TTL 24h)"
  ],
  "domain_checks": [
    "Cross-domain XP events only permitted with explicit CROSS_DOMAIN_GRANT in policy store",
    "Domain_track mismatch between user profile and event → REJECT",
    "School Ops events must originate from verified institute tenant — not company tenant",
    "Dojo events must reference valid match_id from MATCH_ENGINE — no synthetic match events"
  ]
}
```

### Null Tolerance Policy
```
NULL_TOLERANCE = ZERO for required fields
NULL in required field → REJECT immediately
NULL in optional field → allowed only where policy explicitly permits default null
Log all validation failures to AUDIT_TRAIL
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT — LOCKED)

### OUTPUT_SCHEMA

```json
{
  "result_object": {
    "xp_transaction_id":    "UUID — unique per award event",
    "user_id":              "UUID",
    "tenant_id":            "UUID",
    "domain_track":         "Enum: ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION",
    "event_type":           "Enum — from EVENT_TYPE_REGISTRY",
    "event_source":         "Enum: DOJO | GROWTH | SCHOOL_OPS",
    "xp_delta":             "Integer — XP awarded this transaction (never negative from this agent)",
    "xp_cumulative":        "Integer — total XP post-award (read from ledger)",
    "xp_policy_version":    "Semver — policy used to compute award",
    "multiplier_applied":   "Float — active multiplier at time of award (1.0 default)",
    "multiplier_reason":    "String | null — e.g. WIN_STREAK_3X, FIRST_EVER_MATCH",
    "award_status":         "Enum: AWARDED | SUPPRESSED | PENDING_REVIEW | REJECTED",
    "suppression_reason":   "String | null — populated if award_status ≠ AWARDED",
    "belt_eligibility_check_triggered": "Boolean",
    "achievement_check_triggered":      "Boolean",
    "rank_update_triggered":            "Boolean"
  },
  "confidence_score":   "Float 0.0–1.0 — XP engine confidence in award correctness",
  "model_version":      "String — xp_engine_v1.0.0",
  "audit_reference":    "UUID — links to immutable audit log entry",
  "next_trigger_events": [
    "RANK_UPDATE_EVENT",
    "BELT_ELIGIBILITY_CHECK_EVENT",
    "ACHIEVEMENT_CHECK_EVENT",
    "FEATURE_VECTOR_EMIT_EVENT"
  ]
}
```

### Output Rules
- Every output must carry `confidence_score`, `model_version`, `audit_reference`
- XP delta must NEVER be negative in output (deductions are handled by XP_DEDUCTION_AGENT — separate agent, separate audit trail)
- `award_status = SUPPRESSED` if collusion_flag = true OR if COLLUSION_DETECTION_AGENT has flagged user
- `award_status = PENDING_REVIEW` if confidence_score < CONFIDENCE_THRESHOLD (0.75) and event involves belt-adjacent XP
- `award_status = REJECTED` if any validation or security check fails
- Downstream events must be emitted regardless of SUPPRESSED/PENDING status (emit with suppression context so downstream agents can react)

---

## 5️⃣ XP POLICY LAYER (ML + RULE-BASED HYBRID)

### XP Policy Architecture
```
PRIMARY_APPROACH = Rule-Based (deterministic, auditable)
ML_USAGE = 20% — anomaly detection, multiplier calibration, engagement prediction
AI_USAGE = FORBIDDEN in XP award decisions
AI may assist analytics layer only — never award or deny XP
```

### XP_POLICY_STORE (Versioned, Immutable References)

All XP values are stored in the XP_POLICY_STORE, versioned per `xp_policy_version`. Policies are immutable once activated. New policies require version bump and backward-compatibility documentation.

#### DOJO DOMAIN — XP EVENT TABLE (Base Values, v1.0.0)

| Event Type | Base XP | Multiplier Eligible | Notes |
|---|---|---|---|
| `DOJO_MATCH_PARTICIPATED` | 50 | No | Participation floor, regardless of outcome |
| `DOJO_MATCH_WON` | 150 | Yes — WIN_STREAK | Added to participation XP |
| `DOJO_MATCH_LOST` | 20 | No | Learning credit |
| `DOJO_SCENARIO_COMPLETED` | 75 | Yes — DIFFICULTY_FACTOR | Multiplied by scenario difficulty tier |
| `DOJO_BELT_PROMOTION_ATTEMPTED` | 30 | No | Attempt credit only |
| `DOJO_BELT_PROMOTED` | 500 | No | Fixed flat award — no multiplier |
| `DOJO_TOURNAMENT_PARTICIPATED` | 100 | No | Base tournament presence |
| `DOJO_TOURNAMENT_ROUND_WIN` | 200 | Yes — TOURNAMENT_ROUND_MULTIPLIER | Scales per round depth |
| `DOJO_TOURNAMENT_CHAMPION` | 1000 | No | Fixed prestigious award |
| `DOJO_DRILL_COMPLETED` | 25 | No | Per drill |
| `DOJO_MENTOR_EVALUATION_RECEIVED` | 40 | No | For student evaluated by mentor |
| `DOJO_PEER_GG_RECEIVED` | 10 | No | Respect/GG mechanic, cap 50/day |
| `DOJO_WIN_STREAK_3` | 100 | N/A — streak bonus | Bonus at 3-match win streak |
| `DOJO_WIN_STREAK_5` | 250 | N/A — streak bonus | Bonus at 5-match win streak |
| `DOJO_WIN_STREAK_10` | 600 | N/A — streak bonus | Bonus at 10-match win streak |
| `DOJO_FIRST_MATCH_EVER` | 200 | N/A — one-time | Awarded only once per user lifetime |
| `DOJO_FIRST_BELT` | 300 | N/A — one-time | Awarded only once per user lifetime |

#### GROWTH ENGINE DOMAIN — XP EVENT TABLE (Base Values, v1.0.0)

| Event Type | Base XP | Multiplier Eligible | Notes |
|---|---|---|---|
| `GROWTH_DAILY_LOGIN` | 10 | Yes — LOGIN_STREAK | Base per login |
| `GROWTH_LOGIN_STREAK_7` | 100 | N/A — streak bonus | 7-day consecutive login |
| `GROWTH_LOGIN_STREAK_30` | 500 | N/A — streak bonus | 30-day consecutive login |
| `GROWTH_LOGIN_STREAK_100` | 2000 | N/A — streak bonus | 100-day consecutive login |
| `GROWTH_REFERRAL_SIGNUP` | 300 | No | Referral target completed registration |
| `GROWTH_REFERRAL_FIRST_ACTIVITY` | 200 | No | Referral completed first qualifying activity |
| `GROWTH_REFERRAL_5_COMPLETED` | 500 | N/A — milestone | Unlocks premium trial trigger |
| `GROWTH_PROFILE_COMPLETION_50PCT` | 100 | N/A — one-time | Profile 50% complete |
| `GROWTH_PROFILE_COMPLETION_100PCT` | 250 | N/A — one-time | Profile 100% complete |
| `GROWTH_SOCIAL_SHARE_ACHIEVEMENT` | 30 | No | Cap 3 awards/day |
| `GROWTH_WEEKLY_CHALLENGE_COMPLETED` | 150 | No | Per challenge completed |
| `GROWTH_FEATURE_UNLOCK_EARNED` | 50 | No | Viral feature-unlock mechanic completed |
| `GROWTH_BUG_REPORT_VERIFIED` | 500 | N/A — admin verified | Admin must mark bug as valid |
| `GROWTH_FEATURE_REQUEST_IMPLEMENTED` | 400 | N/A — admin verified | Admin must confirm implementation |

#### SCHOOL OPS DOMAIN — XP EVENT TABLE (Base Values, v1.0.0)

| Event Type | Base XP | Multiplier Eligible | Notes |
|---|---|---|---|
| `SCHOOL_ATTENDANCE_DAILY` | 15 | Yes — ATTENDANCE_STREAK | Per verified attendance |
| `SCHOOL_ATTENDANCE_STREAK_7` | 75 | N/A — streak bonus | 7 consecutive days |
| `SCHOOL_ATTENDANCE_STREAK_30` | 300 | N/A — streak bonus | 30 consecutive days |
| `SCHOOL_ASSIGNMENT_SUBMITTED_ONTIME` | 60 | No | Submitted before deadline |
| `SCHOOL_ASSIGNMENT_SUBMITTED_LATE` | 20 | No | Late but submitted |
| `SCHOOL_MILESTONE_COHORT_COMPLETED` | 400 | N/A — milestone | Institute-defined cohort milestone |
| `SCHOOL_COMPLIANCE_BADGE_EARNED` | 100 | N/A — one-time | Per badge type, admin verified |
| `SCHOOL_INSTITUTE_CHALLENGE_COMPLETED`| 200 | No | Institute-scoped challenge |
| `SCHOOL_PLACEMENT_READY_VERIFIED` | 1000 | N/A — admin verified | Placement officer marks student ready |

---

### MULTIPLIER REGISTRY (v1.0.0)

| Multiplier ID | Value | Condition | Max Stack |
|---|---|---|---|
| `WIN_STREAK_2X` | 2.0× | Win streak = 3–4 matches | Not stackable with other win streak |
| `WIN_STREAK_3X` | 3.0× | Win streak = 5–9 matches | Not stackable with other win streak |
| `WIN_STREAK_5X` | 5.0× | Win streak ≥ 10 matches | Not stackable with other win streak |
| `DIFFICULTY_EASY` | 0.8× | Scenario difficulty = EASY | Applied to scenario XP |
| `DIFFICULTY_MEDIUM` | 1.0× | Scenario difficulty = MEDIUM | Baseline |
| `DIFFICULTY_HARD` | 1.5× | Scenario difficulty = HARD | Applied to scenario XP |
| `DIFFICULTY_PRESSURE` | 2.0× | Scenario difficulty = PRESSURE | Applied to scenario XP |
| `TOURNAMENT_ROUND_2` | 1.2× | Round 2 win | Applied to tournament round win XP |
| `TOURNAMENT_ROUND_3` | 1.5× | Round 3 win | Applied to tournament round win XP |
| `TOURNAMENT_SEMIFINAL` | 2.0× | Semifinal win | Applied to tournament round win XP |
| `TOURNAMENT_FINAL` | 3.0× | Final win | Applied to tournament round win XP |
| `LOGIN_STREAK_7DAY` | 1.5× | Active 7-day login streak | Applied to daily login XP |
| `LOGIN_STREAK_30DAY` | 2.0× | Active 30-day login streak | Applied to daily login XP |
| `ATTENDANCE_STREAK_7DAY` | 1.5× | Active 7-day attendance streak | Applied to daily attendance XP |

**Multiplier Stacking Rule:** Win streak multipliers are mutually exclusive. Difficulty and tournament multipliers apply per event type only. Login streak multiplier applies to daily login XP only. No cross-domain multiplier stacking permitted.

---

### DAILY / PERIOD XP CAPS (Abuse Prevention)

| Cap Type | Limit | Scope |
|---|---|---|
| `DAILY_DOJO_XP_CAP` | 2,000 XP | Per user, per tenant, per calendar day UTC |
| `DAILY_GROWTH_XP_CAP` | 500 XP | Per user, per tenant, per calendar day UTC |
| `DAILY_SCHOOL_XP_CAP` | 800 XP | Per user, per tenant, per calendar day UTC |
| `PEER_GG_XP_DAILY_CAP` | 50 XP | Per user, per tenant — GG mechanic |
| `SOCIAL_SHARE_XP_DAILY_CAP` | 90 XP (3×30) | Per user, per tenant |
| `REFERRAL_XP_MONTHLY_CAP` | 5,000 XP | Per user, per tenant, per calendar month |

XP awarded beyond caps is silently dropped (no award_status = AWARDED). The transaction is still logged with `award_status = CAP_EXCEEDED` for audit purposes.

---

## 6️⃣ ML / AI LOGIC LAYER

### ML Usage (Rule-Governed Assist)

```yaml
MODEL_TYPE: Classification + Anomaly Detection
PURPOSE:    XP anomaly detection, multiplier abuse pattern detection,
            engagement prediction (read-only signal)

FEATURES_USED:
  - xp_delta_per_session
  - events_per_hour
  - win_rate_last_30_days
  - streak_age_days
  - referral_activation_rate
  - scenario_completion_speed_percentile
  - peer_score_variance
  - collusion_detection_agent_flag_history

TRAINING_FREQUENCY: Monthly
DRIFT_DETECTION:
  - Monitor distribution shift in xp_delta_per_session
  - Monitor anomaly flag rate (baseline ± 2 sigma triggers review)
  - Monitor accuracy degradation on labeled abuse cases

VERSION_CONTROL:
  - Store model_version in every audit record
  - Immutable model snapshot per version
  - Rollback to previous model_version on drift alarm
```

### AI Usage (Strictly Scoped)

```yaml
AI_USAGE_SCOPE:
  - Permitted: Natural language explanation of XP award for user-facing notifications
  - Permitted: Anomaly pattern summarization for GOVERNANCE_AGENT dashboard
  - FORBIDDEN: AI may never award, suppress, or modify XP decisions
  - FORBIDDEN: Creative interpretation of XP policies

PROMPT_GOVERNANCE:
  - Versioned prompt templates only
  - Deterministic structured prompts
  - No open-ended generation in decision paths
  - AI output reviewed by rule engine before any action taken

AI_ASSISTS_ML_NOT_REPLACES_IT = ENFORCED
```

---

## 7️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS:       5,000 (peak Dojo tournament events)
LATENCY_TARGET:     XP award pipeline P99 < 200ms
                    XP ledger write P99 < 100ms
MAX_CONCURRENCY:    50,000 simultaneous users (event-driven burst)
QUEUE_STRATEGY:     Kafka topic per domain (xp.dojo, xp.growth, xp.school_ops)
                    Consumer group per domain with dead-letter queue (DLQ)
                    Idempotency via event_id dedup key (Redis, 24h TTL)

HORIZONTAL_SCALING: Stateless agent — deploy N replicas behind load balancer
STATELESS:          All state in PostgreSQL (XP ledger) + Redis (dedup + cap tracking)
EVENT_DRIVEN:       Consumed from Kafka, emitted to Kafka
ASYNC_PROCESSING:   All downstream triggers emitted async (fire-and-check-DLQ)
IDEMPOTENT:         event_id is idempotency key — duplicate events produce no double-award
```

---

## 8️⃣ SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  - Every query includes tenant_id filter
  - No cross-tenant XP ledger reads or writes
  - Tenant_id validated against JWT claim on every request

DOMAIN_ISOLATION:
  - user domain_track checked against event domain on every award
  - Cross-domain events require explicit CROSS_DOMAIN_GRANT in policy store

ROLE_BASED_AUTHORIZATION:
  - actor_role must be in permitted_roles for event_type (enforced via RBAC matrix)
  - AUTOMATION agent events require signed service-account token

ENCRYPTION:
  - XP ledger data encrypted at rest (AES-256)
  - PII references (user_id) are UUID references — no PII in XP records
  - Transit encryption: TLS 1.3 minimum

AUDIT_LOGGING:
  - Every XP award attempt logged to immutable append-only audit table
  - Includes: timestamp_utc, actor_id, tenant_id, input_hash, output_hash,
              model_version, award_status, xp_delta, event_type
  - Logs cannot be deleted or modified — violations trigger GOVERNANCE_AGENT alert

ACCESS_LOG_TRACKING:
  - All reads of XP ledger logged
  - Admin access to XP records logged separately
```

---

## 9️⃣ AUDIT & TRACEABILITY

Every XP transaction execution emits the following immutable audit record:

```json
{
  "audit_id":           "UUID",
  "xp_transaction_id":  "UUID",
  "timestamp_utc":      "ISO-8601",
  "actor_id":           "UUID (user_id or service_account_id)",
  "tenant_id":          "UUID",
  "input_hash":         "SHA-256 of canonicalized input payload",
  "output_hash":        "SHA-256 of canonicalized output payload",
  "model_version":      "xp_engine_v1.0.0",
  "xp_policy_version":  "v1.0.0",
  "event_type":         "Enum",
  "event_source":       "Enum: DOJO | GROWTH | SCHOOL_OPS",
  "xp_delta":           "Integer",
  "award_status":       "Enum",
  "decision_path":      "Array of rule IDs applied: [XP_POLICY_LOOKUP, CAP_CHECK, COLLUSION_CHECK, MULTIPLIER_APPLY, LEDGER_WRITE]",
  "confidence_score":   "Float",
  "anomaly_flags":      "Array of anomaly code strings | []"
}
```

**Audit Table:** `xp_audit_log` — append-only, no DELETE or UPDATE permitted by schema constraint. Verified by GOVERNANCE_AGENT quarterly.

---

## 🔟 FAILURE POLICY

### Failure Scenarios and Required Behavior

| Failure Condition | Behavior |
|---|---|
| **Invalid input (missing required field)** | REJECT immediately · Return 400 with validation error · Log to audit with `award_status = REJECTED` · Do NOT halt downstream pipeline |
| **Duplicate event_id (replay attack)** | REJECT silently (idempotent) · Return 200 with `award_status = ALREADY_PROCESSED` · Log replay attempt with anomaly flag |
| **XP_POLICY_STORE unavailable** | STOP_EXECUTION · LOG_INCIDENT · ESCALATE_TO = GOVERNANCE_AGENT · RETRY_POLICY = 3× exponential backoff (1s, 2s, 4s) then DLQ |
| **Kafka publish failure (downstream event emit)** | Retry 3× · If all fail, write to DLQ with full context · Alert OBSERVABILITY_AGENT |
| **Collusion flag present** | SUPPRESS XP · LOG_INCIDENT · ESCALATE_TO = GOVERNANCE_AGENT · Return `award_status = SUPPRESSED` |
| **Confidence score < 0.75 (belt-adjacent event)** | HOLD in PENDING_REVIEW state · Escalate to MENTOR_BOARD queue · XP not awarded until review clears |
| **Database write failure (XP ledger)** | STOP_EXECUTION · Do NOT emit downstream events (consistency rule) · LOG_INCIDENT · ESCALATE_TO = OPS_TEAM · RETRY_POLICY = 3× with circuit breaker |
| **Daily XP cap exceeded** | AWARD_STATUS = CAP_EXCEEDED · Drop award silently · Log in audit · Continue pipeline normally |
| **Tenant isolation violation detected** | STOP_EXECUTION immediately · LOG_SECURITY_INCIDENT (CRITICAL) · ESCALATE_TO = SECURITY_AGENT + GOVERNANCE_AGENT · Block all further requests from actor until review |

**No silent failures permitted under any condition.**

---

## 1️⃣1️⃣ INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - MATCH_ENGINE_AGENT         # Dojo match results
  - SCORING_ENGINE_AGENT       # Validated scores + confidence
  - GROWTH_EVENT_AGENT         # Referral, streak, social events
  - SCHOOL_OPS_AGENT           # Attendance, assignment, compliance
  - TOURNAMENT_ENGINE_AGENT    # Tournament placement events
  - COLLUSION_DETECTION_AGENT  # Abuse/fraud flags (suppresses XP)
  - AUTH_SERVICE               # JWT validation

DOWNSTREAM_AGENTS:
  - RANK_ENGINE_AGENT          # Triggered on every XP award
  - BELT_ENGINE_AGENT          # Triggered on Dojo XP events above threshold
  - ACHIEVEMENT_ENGINE_AGENT   # Triggered on milestone events
  - LEADERBOARD_AGENT          # Triggered for XP leaderboard refresh
  - NOTIFICATION_ENGINE_AGENT  # Triggered to notify user of XP award
  - ANALYTICS_AGENT            # Receives XP event for platform analytics
  - FEATURE_STORE_AGENT        # Receives passive feature vectors
  - GOVERNANCE_AGENT           # Receives escalations and anomaly events

EVENT_TRIGGERS_EMITTED:
  RANK_UPDATE_EVENT:
    topic: rank.update
    payload: { user_id, tenant_id, xp_delta, xp_cumulative, timestamp_utc }

  BELT_ELIGIBILITY_CHECK_EVENT:
    topic: belt.eligibility_check
    payload: { user_id, tenant_id, domain_track, xp_cumulative, match_count, timestamp_utc }
    condition: event_source = DOJO and xp_delta > 0 and award_status = AWARDED

  ACHIEVEMENT_CHECK_EVENT:
    topic: achievement.check
    payload: { user_id, tenant_id, event_type, xp_cumulative, timestamp_utc }

  XP_FEATURE_VECTOR_EVENT:
    topic: feature_store.ingest
    payload:
      user_id:       UUID
      feature_name:  "xp_daily_delta"
      feature_value: <xp_delta>
      timestamp:     ISO-8601
      source_agent:  "XP_ENGINE_AGENT"
```

---

## 1️⃣2️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

```yaml
FEATURE_VECTOR_EMISSIONS:
  XP_ENGINE_AGENT emits to FEATURE_STORE_AGENT on every award:

  features_emitted:
    - feature: xp_daily_delta
      value: integer XP awarded this transaction
    - feature: xp_cumulative
      value: total XP at time of award
    - feature: event_type_encoded
      value: integer encoding of event_type
    - feature: domain_track_encoded
      value: integer encoding of domain_track
    - feature: multiplier_applied
      value: float multiplier used
    - feature: award_status_encoded
      value: integer encoding (AWARDED=1, SUPPRESSED=0, PENDING=2, REJECTED=-1)
    - feature: daily_xp_cap_proximity
      value: float 0.0–1.0 (how close to daily cap)

COMPATIBILITY:
  - FEATURE_STORE_AGENT: ✅
  - IDEA_DNA_AGENT:       N/A (XP Engine does not touch ideas)
  - ROYALTY_ENGINE:       N/A
  - GROWTH_ENGINE:        ✅ (passive signal consumer)
```

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

```yaml
RANK_UPDATE_EVENT:     Emitted on every AWARDED XP transaction
XP_UPDATE_EVENT:       Emitted as part of every output (downstream consumers subscribe)
SHARE_TRIGGER_EVENT:   Emitted on milestone XP events (belt promotion, tournament win,
                       streak milestones) to GROWTH_EVENT_AGENT for optional social share prompt

Milestone thresholds triggering SHARE_TRIGGER_EVENT:
  - DOJO_BELT_PROMOTED
  - DOJO_TOURNAMENT_CHAMPION
  - DOJO_WIN_STREAK_10
  - GROWTH_LOGIN_STREAK_100
  - SCHOOL_PLACEMENT_READY_VERIFIED
  - Any FIRST_EVER event type
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

```yaml
METRICS:
  success_rate:         XP awards with award_status = AWARDED / total events processed
  error_rate:           award_status = REJECTED / total events processed
  latency_p50:          Median XP pipeline latency (ms)
  latency_p99:          P99 XP pipeline latency (ms) — target < 200ms
  cap_hit_rate:         award_status = CAP_EXCEEDED / total events
  suppression_rate:     award_status = SUPPRESSED / total events
  pending_review_rate:  award_status = PENDING_REVIEW / total events
  drift_indicator:      ML anomaly model confidence drift (monitored monthly)
  anomaly_frequency:    anomaly_flags array non-empty / total events

OBSERVABILITY_INTEGRATION:
  - Metrics published to Prometheus
  - Dashboard in Grafana: XP Engine Health
  - Alerts:
      xp_pipeline_p99 > 500ms           → WARN
      error_rate > 5%                   → CRITICAL
      suppression_rate spike > 3x avg   → CRITICAL (possible abuse wave)
      audit_log_write_failure           → CRITICAL (data integrity)
      kafka_dlq_depth > 100             → WARN

INTEGRATED_WITH: OBSERVABILITY_AGENT
```

---

## 1️⃣5️⃣ VERSIONING POLICY

```yaml
VERSIONING_RULES:
  - All policy changes: ADD_ONLY with version bump
  - XP base values: Immutable once activated in a policy version
  - New event types: Require minor version bump (v1.0.0 → v1.1.0)
  - New multiplier: Require minor version bump
  - Cap changes: Require minor version bump + backward compat note
  - Breaking changes to output schema: Require major version bump (v1.0.0 → v2.0.0)

BACKWARD_COMPATIBILITY:
  - Old event_id references remain valid indefinitely
  - Older xp_policy_version events retained in audit log
  - Rollback: Deactivate new policy version, reactivate previous — no ledger mutation

MIGRATION_DOCUMENTATION:
  - Every version bump must include: what changed, what was added, backward compat impact
  - Stored in: XP_POLICY_CHANGELOG (append-only versioned document)
```

---

## 1️⃣6️⃣ NON-NEGOTIABLE RULES (SEALED — CANNOT BE OVERRIDDEN)

```
XP_ENGINE_AGENT MUST NOT:
  ❌ Create hidden XP logic outside of XP_POLICY_STORE
  ❌ Modify historical XP ledger records (append-only, ever)
  ❌ Auto-delete audit logs under any condition
  ❌ Override COLLUSION_DETECTION_AGENT suppression flags
  ❌ Bypass tenant isolation checks for any reason
  ❌ Mix domain_track data across tenant scopes
  ❌ Execute XP award decisions using AI/LLM output
  ❌ Award XP for events with collusion_flag = true
  ❌ Auto-promote belts (belt promotion is BELT_ENGINE_AGENT's domain)
  ❌ Award negative XP (deductions are XP_DEDUCTION_AGENT's domain)
  ❌ Operate outside scope of defined EVENT_TYPE_REGISTRY
  ❌ Skip audit log writes — audit failure = transaction failure
  ❌ Process events without xp_policy_version match
  ❌ Interpret ambiguous inputs — reject and log instead
```

---

## 🔒 DOMAIN-SPECIFIC DESIGN NOTES

### Dojo Domain — XP Specifics
- XP from Dojo matches is only awarded after `SCORING_ENGINE_AGENT` emits a validated score event. Raw match events without scoring validation are queued, not awarded.
- Belt promotion XP (`DOJO_BELT_PROMOTED`) requires prior confirmation from `BELT_ENGINE_AGENT` — the XP_ENGINE_AGENT does not unilaterally determine belt eligibility.
- Confidence score < 0.75 from SCORING_ENGINE on a match → `award_status = PENDING_REVIEW` for all match-derived XP until mentor board clears.
- Tournament XP rounds are awarded incrementally per round win, not retroactively on champion status.

### Growth Engine Domain — XP Specifics
- Login streak XP is computed daily at midnight UTC via a scheduled batch job trigger from `GROWTH_EVENT_AGENT`. Not triggered by user action directly.
- Referral XP for `GROWTH_REFERRAL_SIGNUP` is held in escrow for 48 hours to allow fraud detection. Released after COLLUSION_DETECTION_AGENT clears the referral pair.
- Social share XP (`GROWTH_SOCIAL_SHARE_ACHIEVEMENT`) requires verified share event from integration hub — self-reported shares are not accepted.
- Bug report and feature request XP require admin verification event from ADMIN_GOVERNANCE_SERVICE before award.

### School Ops Domain — XP Specifics
- Attendance XP requires verified attendance record from SCHOOL_OPS_AGENT (biometric, QR, or admin-verified — not self-reported).
- Assignment XP distinguishes on-time vs. late submission — deadline timestamp is immutable once set by institute.
- School Ops XP is tenant-scoped to Institute tenants only. Company tenants cannot emit School Ops events.
- Placement readiness XP (`SCHOOL_PLACEMENT_READY_VERIFIED`) is one-time per user and requires explicit signal from PLACEMENT_OFFICER role — not STUDENT or AUTOMATION.

---

## 🔒 EVENT_TYPE_REGISTRY (LOCKED v1.0.0)

All event types permitted in this agent version. Any event_type not in this registry → REJECT immediately.

```
DOJO DOMAIN:
  DOJO_MATCH_PARTICIPATED | DOJO_MATCH_WON | DOJO_MATCH_LOST
  DOJO_SCENARIO_COMPLETED | DOJO_BELT_PROMOTION_ATTEMPTED | DOJO_BELT_PROMOTED
  DOJO_TOURNAMENT_PARTICIPATED | DOJO_TOURNAMENT_ROUND_WIN | DOJO_TOURNAMENT_CHAMPION
  DOJO_DRILL_COMPLETED | DOJO_MENTOR_EVALUATION_RECEIVED | DOJO_PEER_GG_RECEIVED
  DOJO_WIN_STREAK_3 | DOJO_WIN_STREAK_5 | DOJO_WIN_STREAK_10
  DOJO_FIRST_MATCH_EVER | DOJO_FIRST_BELT

GROWTH DOMAIN:
  GROWTH_DAILY_LOGIN | GROWTH_LOGIN_STREAK_7 | GROWTH_LOGIN_STREAK_30 | GROWTH_LOGIN_STREAK_100
  GROWTH_REFERRAL_SIGNUP | GROWTH_REFERRAL_FIRST_ACTIVITY | GROWTH_REFERRAL_5_COMPLETED
  GROWTH_PROFILE_COMPLETION_50PCT | GROWTH_PROFILE_COMPLETION_100PCT
  GROWTH_SOCIAL_SHARE_ACHIEVEMENT | GROWTH_WEEKLY_CHALLENGE_COMPLETED
  GROWTH_FEATURE_UNLOCK_EARNED | GROWTH_BUG_REPORT_VERIFIED | GROWTH_FEATURE_REQUEST_IMPLEMENTED

SCHOOL OPS DOMAIN:
  SCHOOL_ATTENDANCE_DAILY | SCHOOL_ATTENDANCE_STREAK_7 | SCHOOL_ATTENDANCE_STREAK_30
  SCHOOL_ASSIGNMENT_SUBMITTED_ONTIME | SCHOOL_ASSIGNMENT_SUBMITTED_LATE
  SCHOOL_MILESTONE_COHORT_COMPLETED | SCHOOL_COMPLIANCE_BADGE_EARNED
  SCHOOL_INSTITUTE_CHALLENGE_COMPLETED | SCHOOL_PLACEMENT_READY_VERIFIED
```

---

## 🔒 DATABASE SCHEMA (LOCKED v1.0.0)

```sql
-- XP Ledger: immutable append-only transaction log
CREATE TABLE xp_ledger (
  xp_transaction_id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  event_id            UUID NOT NULL UNIQUE,           -- idempotency key
  user_id             UUID NOT NULL,
  tenant_id           UUID NOT NULL,
  domain_track        TEXT NOT NULL,
  event_type          TEXT NOT NULL,
  event_source        TEXT NOT NULL,
  xp_delta            INTEGER NOT NULL CHECK (xp_delta >= 0),
  xp_cumulative       INTEGER NOT NULL,
  xp_policy_version   TEXT NOT NULL,
  multiplier_applied  NUMERIC(4,2) NOT NULL DEFAULT 1.0,
  award_status        TEXT NOT NULL,
  suppression_reason  TEXT,
  confidence_score    NUMERIC(4,3),
  model_version       TEXT NOT NULL,
  audit_reference     UUID NOT NULL,
  created_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Immutable: no UPDATE or DELETE permitted (enforced by policy + row-level security)
-- Partitioned by tenant_id for isolation

-- XP User Snapshot: mutable current state (derived from ledger)
CREATE TABLE xp_user_snapshot (
  user_id             UUID PRIMARY KEY,
  tenant_id           UUID NOT NULL,
  xp_total            INTEGER NOT NULL DEFAULT 0,
  xp_dojo             INTEGER NOT NULL DEFAULT 0,
  xp_growth           INTEGER NOT NULL DEFAULT 0,
  xp_school           INTEGER NOT NULL DEFAULT 0,
  last_updated_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  snapshot_version    BIGINT NOT NULL DEFAULT 0
);

-- XP Daily Cap Tracker (Redis-backed, PostgreSQL fallback)
-- Key: xp_cap:{tenant_id}:{user_id}:{domain}:{YYYYMMDD}
-- Value: integer (XP awarded today in domain)
-- TTL: 25 hours (auto-expire after day boundary)

-- XP Audit Log
CREATE TABLE xp_audit_log (
  audit_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  xp_transaction_id   UUID NOT NULL,
  timestamp_utc       TIMESTAMPTZ NOT NULL,
  actor_id            UUID NOT NULL,
  tenant_id           UUID NOT NULL,
  input_hash          TEXT NOT NULL,
  output_hash         TEXT NOT NULL,
  model_version       TEXT NOT NULL,
  xp_policy_version   TEXT NOT NULL,
  decision_path       JSONB NOT NULL,
  anomaly_flags       JSONB NOT NULL DEFAULT '[]',
  award_status        TEXT NOT NULL,
  created_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);
-- Immutable: no UPDATE or DELETE
```

---

## 🔒 FINAL GOVERNANCE SEAL

```
╔══════════════════════════════════════════════════════════════════════════════╗
║                     XP_ENGINE_AGENT — GOVERNANCE SEAL                       ║
║                                                                              ║
║  XP COMPUTATION MODE          DETERMINISTIC RULE-BASED                      ║
║  AI IN XP DECISIONS           FORBIDDEN                                     ║
║  AUDIT TRAIL                  IMMUTABLE APPEND-ONLY                         ║
║  TENANT ISOLATION             HARD ENFORCED                                 ║
║  CROSS-DOMAIN MIXING          FORBIDDEN                                     ║
║  SILENT FAILURES              FORBIDDEN                                     ║
║  AUTO BELT PROMOTION          FORBIDDEN                                     ║
║  NEGATIVE XP FROM THIS AGENT  FORBIDDEN                                     ║
║  HISTORY MUTATION             FORBIDDEN                                     ║
║  ASSUMPTION FILLING           FORBIDDEN                                     ║
║  COLLUSION OVERRIDE           FORBIDDEN                                     ║
║  CREATIVE POLICY INTERPRETATION FORBIDDEN                                   ║
║                                                                              ║
║  DOJO OWNER:          XP_ENGINE_AGENT v1.0.0                                ║
║  GROWTH OWNER:        XP_ENGINE_AGENT v1.0.0                                ║
║  SCHOOL OPS OWNER:    XP_ENGINE_AGENT v1.0.0                                ║
║                                                                              ║
║  ARCHITECTURE:        SEALED                                                ║
║  INTERPRETATION AUTH: NONE                                                  ║
║  MUTATION AUTH:       VERSION BUMP ONLY                                     ║
║                                                                              ║
║  ECOSKILLER ANTIGRAVITY — ENTERPRISE SAAS GRADE                             ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---
*XP_ENGINE_AGENT.md — v1.0.0 | Ecoskiller Antigravity | Generated per Master Agent Creation Prompt | Sealed*
