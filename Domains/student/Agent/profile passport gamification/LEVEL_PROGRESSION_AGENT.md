# 🔒 LEVEL_PROGRESSION_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED ENTERPRISE AGENT SPECIFICATION
### Domains: Dojo · Growth Engine · School Ops (Institute ERP)
### Version: v1.0.0 | Mutation Policy: ADD-ONLY | Execution Mode: DETERMINISTIC + VALIDATED

---

```
╔══════════════════════════════════════════════════════════════════════════════════╗
║           MASTER SEAL BLOCK — DO NOT MODIFY WITHOUT VERSION BUMP               ║
║                                                                                  ║
║  AGENT_NAME                  = LEVEL_PROGRESSION_AGENT                         ║
║  EXECUTION_MODE              = DETERMINISTIC + VALIDATED                        ║
║  MUTATION_POLICY             = ADD_ONLY                                         ║
║  CREATIVE_INTERPRETATION     = FORBIDDEN                                        ║
║  ASSUMPTION_FILLING          = FORBIDDEN                                        ║
║  DEFAULT_BEHAVIOR            = DENY                                             ║
║  FAILURE_MODE                = HALT_ON_AMBIGUITY                               ║
║  CROSS_TENANT_ACCESS         = FORBIDDEN                                        ║
║  SILENT_FAILURES             = FORBIDDEN                                        ║
║  AUTO_BELT_PROMOTION         = FORBIDDEN (requires mentor confirmation)         ║
║  AUTO_LEVEL_REGRESSION       = FORBIDDEN (levels never decrease)                ║
║  HISTORY_MUTATION            = FORBIDDEN                                        ║
║  AI_IN_PROGRESSION_DECISIONS = FORBIDDEN                                        ║
║  CROSS_DOMAIN_MIXING         = FORBIDDEN                                        ║
╚══════════════════════════════════════════════════════════════════════════════════╝
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY — LOCKED)

```yaml
AGENT_NAME:          LEVEL_PROGRESSION_AGENT
AGENT_VERSION:       v1.0.0
SYSTEM_ROLE:         Authoritative engine for computing, validating, persisting, and
                     broadcasting all level progression state across Dojo Belt Ladder,
                     Growth Prestige Tiers, and School Ops Academic Levels for every
                     user on the Ecoskiller Antigravity platform.
PRIMARY_DOMAIN:      Level Progression / Gamification / Credentialing
EXECUTION_MODE:      Deterministic + Validated
DATA_SCOPE:          Dojo belt state, match count, avg score, scenario pass records,
                     mentor confirmation records, Growth prestige tier, XP thresholds,
                     skill level bars (per technology), School Ops academic grade levels,
                     cohort progression milestones, influence score, placement readiness
TENANT_SCOPE:        Strict Per-Tenant Isolation (Institute ≠ Company ≠ Platform)
FAILURE_POLICY:      HALT on ambiguity · LOG_INCIDENT · ESCALATE to GOVERNANCE_AGENT
MODEL_VERSION_REF:   level_progression_engine_v1.0.0
AUDIT_TRAIL:         Append-only, immutable, every state transition logged
```

**This agent must never assume missing specifications.
All undefined inputs → REJECT + LOG. No level transition executed without explicit,
validated eligibility confirmation from ALL required upstream dependencies.**

---

## 2️⃣ PURPOSE DECLARATION

### The Problem This Agent Solves

Across three owner-domains (Dojo, Growth, School Ops), Ecoskiller Antigravity users progress through distinct, structured level systems. Without a single authoritative agent to own eligibility computation, state transition, broadcast, and audit, level state becomes inconsistent, gameable, and non-auditable at enterprise scale (10M–100M users).

The LEVEL_PROGRESSION_AGENT is the **single source of truth** for all level state. It:
- Computes eligibility for every level transition across all three domains
- Enforces multi-gate conditions before any transition is executed
- Persists the transition as an immutable append-only record
- Broadcasts structured events to downstream agents (achievements, notifications, leaderboards, XP, certifications, feature unlocks)
- Blocks any invalid, low-confidence, or mentor-unconfirmed transition at the gate
- Emits passive feature vectors for ML-driven downstream intelligence

### Three-Domain Level Architecture

| Domain | Level System | Governing Logic |
|---|---|---|
| **Dojo** | Belt Ladder: White → Yellow → Green → Blue → Black | Multi-gate: match count + avg score + pressure scenario pass + mentor confirmation + audit record |
| **Growth Engine** | Prestige Tiers: Explorer → Challenger → Contender → Veteran → Elite + Skill Level Bars: Beginner → Intermediate → Advanced per technology | XP threshold gate + engagement pattern validation |
| **School Ops** | Academic Grade Levels: School Grade 6–8 → 9–10 → 11–12 + Post-Secondary Tiers: Diploma → UG → PG · Cohort Milestone Levels · Placement Readiness Level | Institute-verified academic progression + cohort milestone gates + placement officer confirmation |

### What It Consumes
- `LEVEL_TRIGGER_EVENT` from XP_ENGINE_AGENT (XP threshold crossed or belt eligibility check triggered)
- Validated match count + avg score + scenario pass records from SCORING_ENGINE_AGENT
- Mentor confirmation signals from MENTOR_CONTROL_ENGINE_AGENT
- Anti-abuse / collusion flags from COLLUSION_DETECTION_AGENT
- Academic record events from SCHOOL_OPS_AGENT
- Placement readiness confirmation from PLACEMENT_OFFICER (role-gated)
- Skill technology XP aggregates from ANALYTICS_AGENT

### What It Produces
- `LEVEL_UP_EVENT` — structured broadcast on successful level transition
- `LEVEL_PROGRESSION_BLOCKED_EVENT` — structured broadcast when gate fails
- Updated level state records in the LEVEL_STATE_STORE (append-only)
- Belt promotion eligibility clearance or rejection to CERTIFICATION_BELT_ENGINE_AGENT
- `ACHIEVEMENT_CHECK_EVENT` to ACHIEVEMENT_ENGINE_AGENT
- `RANK_UPDATE_EVENT` to RANK_ENGINE_AGENT
- `NOTIFICATION_TRIGGER_EVENT` to NOTIFICATION_ENGINE_AGENT
- `FEATURE_UNLOCK_EVENT` to FEATURE_GATE_AGENT
- `SHARE_TRIGGER_EVENT` to GROWTH_EVENT_AGENT (on prestige milestones)
- `CERTIFICATE_GENERATION_EVENT` to CERTIFICATION_BELT_ENGINE_AGENT (on belt promotion)
- Passive feature vectors to FEATURE_STORE_AGENT

### Downstream Agents Depending on This Agent
- `CERTIFICATION_BELT_ENGINE_AGENT` — awaits level clearance for certificate generation
- `ACHIEVEMENT_ENGINE_AGENT` — consumes level transitions for badge evaluation
- `RANK_ENGINE_AGENT` — recalculates rank on level change
- `LEADERBOARD_AGENT` — reflects belt/tier/grade level on multi-tier leaderboards
- `NOTIFICATION_ENGINE_AGENT` — sends congrats + next-level guidance
- `FEATURE_GATE_AGENT` — unlocks features on level milestone
- `GROWTH_EVENT_AGENT` — consumes share triggers on level milestones
- `ANALYTICS_AGENT` — tracks progression velocity, drop-off, and curriculum signals
- `OBSERVABILITY_AGENT` — monitors progression pipeline health

### Upstream Agents Feeding This Agent
- `XP_ENGINE_AGENT` — primary trigger source (XP threshold crossing, belt eligibility check)
- `SCORING_ENGINE_AGENT` — validated match scores + avg score computation
- `MATCH_ENGINE_AGENT` — match count validation for Dojo belt gates
- `MENTOR_CONTROL_ENGINE_AGENT` — mentor confirmation signals
- `COLLUSION_DETECTION_AGENT` — abuse/fraud flags that block transitions
- `SCHOOL_OPS_AGENT` — academic record events, attendance, assignment completion
- `ANALYTICS_AGENT` — skill technology XP aggregates, scenario pass records
- `AUTH_SERVICE` — JWT validation on every request

---

## 3️⃣ INPUT CONTRACT (STRICT — LOCKED)

### INPUT_SCHEMA

```json
{
  "required_fields": {
    "trigger_event_id":      "UUID — globally unique trigger event (idempotency key)",
    "trigger_type":          "Enum — see TRIGGER_TYPE_REGISTRY below",
    "user_id":               "UUID — must exist in USER_SERVICE, tenant-scoped",
    "tenant_id":             "UUID — tenant isolation key",
    "domain":                "Enum: DOJO | GROWTH | SCHOOL_OPS",
    "domain_track":          "Enum: ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION",
    "actor_role":            "Enum: STUDENT | TRAINER | EVALUATOR | ADMIN | AUTOMATION | PLACEMENT_OFFICER",
    "current_level_id":      "String — current level identifier from LEVEL_DEFINITION_REGISTRY",
    "target_level_id":       "String — proposed next level identifier from LEVEL_DEFINITION_REGISTRY",
    "timestamp_utc":         "ISO-8601 UTC timestamp",
    "policy_version":        "Semver — must match active LEVEL_POLICY_VERSION"
  },
  "optional_fields": {
    "match_count":           "Integer ≥ 0 — required for Dojo belt trigger_types",
    "avg_score_pct":         "Float 0.0–100.0 — required for Dojo belt trigger_types",
    "pressure_scenario_passed": "Boolean — required for Dojo belt trigger_types",
    "mentor_confirmation_id": "UUID — required for Dojo belt trigger_types (mentor board confirmation record)",
    "confidence_score":      "Float 0.0–1.0 — from SCORING_ENGINE, required for Dojo triggers",
    "xp_cumulative":         "Integer — required for GROWTH prestige tier triggers",
    "skill_tag":             "String — required for SKILL_LEVEL_BAR triggers (e.g. 'react', 'python')",
    "skill_xp":              "Integer — required for SKILL_LEVEL_BAR triggers",
    "academic_grade":        "String — required for SCHOOL_OPS academic level triggers",
    "institution_id":        "UUID — required for SCHOOL_OPS triggers",
    "cohort_id":             "UUID — required for SCHOOL_OPS cohort milestone triggers",
    "placement_officer_id":  "UUID — required for PLACEMENT_READINESS triggers",
    "collusion_flag":        "Boolean — if true, block all transitions and escalate",
    "mentor_board_review_id":"UUID — populated when transition was PENDING_MENTOR_BOARD"
  },
  "validation_rules": [
    "trigger_event_id must be globally unique — reject duplicate (idempotency)",
    "timestamp_utc must be within ±5 minute drift window of server UTC",
    "policy_version must exactly match active LEVEL_POLICY_VERSION",
    "user_id must belong to tenant_id — cross-tenant injection = SECURITY_FAILURE",
    "current_level_id must match user's actual current level in LEVEL_STATE_STORE",
    "target_level_id must be exactly one level above current_level_id in the domain's sequence — skipping levels = REJECT",
    "actor_role must be authorized for trigger_type per ROLE_TRIGGER_AUTHORIZATION_MATRIX",
    "Dojo belt trigger: match_count, avg_score_pct, pressure_scenario_passed, mentor_confirmation_id, confidence_score are ALL required — any null = REJECT",
    "Growth prestige tier trigger: xp_cumulative required — null = REJECT",
    "Skill level bar trigger: skill_tag + skill_xp required — null = REJECT",
    "School Ops academic level trigger: academic_grade + institution_id required",
    "Placement readiness trigger: placement_officer_id required — actor_role must be PLACEMENT_OFFICER",
    "collusion_flag = true → IMMEDIATE BLOCK, no transition, escalate to GOVERNANCE_AGENT",
    "confidence_score (Dojo) < 0.75 → PENDING_MENTOR_BOARD, transition suspended"
  ],
  "security_checks": [
    "JWT bearer token validation on every request",
    "Tenant isolation: user_id.tenant_id == request.tenant_id (hard check)",
    "Domain isolation: user domain_track authorized for trigger_type",
    "Role authorization: actor_role in permitted_roles for trigger_type (RBAC matrix)",
    "Service account token required for AUTOMATION actor_role",
    "Rate-limit: max 10 level trigger events per user per hour (abuse prevention)",
    "Replay attack prevention: trigger_event_id idempotency key checked (Redis TTL 48h)"
  ],
  "domain_checks": [
    "DOJO triggers must originate from MATCH_ENGINE_AGENT or SCORING_ENGINE_AGENT events",
    "SCHOOL_OPS triggers must originate from institute tenant, not company tenant",
    "GROWTH triggers must originate from XP_ENGINE_AGENT LEVEL_TRIGGER_EVENT",
    "Cross-domain level mapping is FORBIDDEN — Dojo belt does not affect School Ops level",
    "Mentor confirmation_id must be verified in MENTOR_CONFIRMATION_STORE before use"
  ]
}
```

### Null Tolerance Policy
```
NULL_TOLERANCE              = ZERO for all required fields
NULL in required field      → REJECT immediately, return 400 with field-level error
NULL in optional field      → Allowed only where domain logic does not require it
All validation failures     → Logged to AUDIT_TRAIL with full input hash
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT — LOCKED)

### OUTPUT_SCHEMA

```json
{
  "result_object": {
    "progression_transaction_id":  "UUID — unique per transition attempt",
    "user_id":                     "UUID",
    "tenant_id":                   "UUID",
    "domain":                      "Enum: DOJO | GROWTH | SCHOOL_OPS",
    "domain_track":                "Enum",
    "trigger_type":                "Enum — from TRIGGER_TYPE_REGISTRY",
    "current_level_id":            "String — level before this transaction",
    "target_level_id":             "String — level attempted",
    "transition_status":           "Enum: PROMOTED | BLOCKED | PENDING_MENTOR_BOARD | REJECTED | ALREADY_AT_LEVEL | COLLUSION_SUPPRESSED",
    "block_reason":                "String | null — populated if transition_status ≠ PROMOTED",
    "gates_evaluated": {
      "gate_id":                   "String",
      "gate_name":                 "String",
      "required_value":            "Any",
      "actual_value":              "Any",
      "gate_passed":               "Boolean"
    },
    "new_level_id":                "String | null — populated only if PROMOTED",
    "new_level_label":             "String | null — human-readable level name",
    "privileges_unlocked":         "Array of privilege_id strings | []",
    "certificate_generation_triggered": "Boolean",
    "policy_version":              "Semver — policy used for this evaluation",
    "mentor_confirmation_required": "Boolean"
  },
  "confidence_score":   "Float 0.0–1.0 — engine confidence in transition decision",
  "model_version":      "String — level_progression_engine_v1.0.0",
  "audit_reference":    "UUID — links to immutable audit log entry",
  "next_trigger_events": [
    "LEVEL_UP_EVENT",
    "ACHIEVEMENT_CHECK_EVENT",
    "RANK_UPDATE_EVENT",
    "NOTIFICATION_TRIGGER_EVENT",
    "FEATURE_UNLOCK_EVENT",
    "SHARE_TRIGGER_EVENT",
    "CERTIFICATE_GENERATION_EVENT",
    "FEATURE_VECTOR_EMIT_EVENT"
  ]
}
```

### Output Rules
- `transition_status = PROMOTED` only when ALL gates pass AND mentor confirmation present (Dojo) AND collusion_flag = false
- `transition_status = PENDING_MENTOR_BOARD` when confidence < 0.75 or mentor_confirmation_id absent — transition is held, not rejected
- `transition_status = BLOCKED` when any hard gate fails (match count, score threshold, XP threshold, academic record)
- `transition_status = COLLUSION_SUPPRESSED` when collusion_flag = true
- All gate evaluations must be included in output (passed AND failed), for full transparency
- `new_level_id` and `privileges_unlocked` are null for any status ≠ PROMOTED
- Every output emits downstream events regardless of transition_status (downstream agents must react to all statuses)
- `certificate_generation_triggered = true` only on Dojo belt PROMOTED transitions

---

## 5️⃣ LEVEL DEFINITION REGISTRY (LOCKED v1.0.0)

### DOMAIN A: DOJO — BELT LADDER

```
BELT SEQUENCE (immutable order):
  DOJO_BELT_NONE → DOJO_BELT_WHITE → DOJO_BELT_YELLOW →
  DOJO_BELT_GREEN → DOJO_BELT_BLUE → DOJO_BELT_BLACK

Level skipping: FORBIDDEN. Transitions must be sequential, one belt at a time.
Auto-promotion: FORBIDDEN. Mentor confirmation is mandatory at every promotion gate.
Belt regression: FORBIDDEN. Once awarded, a belt cannot be revoked by this agent
                 (revocation is GOVERNANCE_AGENT domain with separate audit trail).
```

#### Belt Gate Requirements (Multi-Gate, ALL must pass)

| Belt Level | Gate 1: Min Match Count | Gate 2: Min Avg Score | Gate 3: Pressure Scenario Pass | Gate 4: Mentor Confirmation | Gate 5: Confidence Score |
|---|---|---|---|---|---|
| `DOJO_BELT_WHITE` | 10 matches | 60% avg | Not required | Required | ≥ 0.75 |
| `DOJO_BELT_YELLOW` | 25 matches | 70% avg | Not required | Required | ≥ 0.75 |
| `DOJO_BELT_GREEN` | 50 matches | 75% avg | Required (1 pass) | Required | ≥ 0.80 |
| `DOJO_BELT_BLUE` | 100 matches | 80% avg | Required (1 pass) | Required | ≥ 0.80 |
| `DOJO_BELT_BLACK` | 200 matches | 85% avg | Required (2 passes) | Required (Mentor Board, not single mentor) | ≥ 0.85 |

**Gate Evaluation Order:** Gate 1 → Gate 2 → Gate 3 → Gate 4 → Gate 5
If any gate fails: BLOCKED immediately. Remaining gates are not evaluated.
Belt gate values are immutable in v1.0.0. Any change requires major version bump.

#### Belt Domain-Track Isolation
Each domain_track (ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION) maintains **independent belt progression**. A user's Technology Black Belt has no bearing on their Arts belt state.

#### Belt Privileges Unlocked on Promotion

| Belt | Privileges Unlocked |
|---|---|
| `DOJO_BELT_WHITE` | Access to Intermediate Scenario Pool · Belt badge on profile |
| `DOJO_BELT_YELLOW` | Access to Advanced Scenario Pool · Tournament entry eligibility |
| `DOJO_BELT_GREEN` | Mentor-Eligible status unlocked · Priority arena matchmaking |
| `DOJO_BELT_BLUE` | Sponsored Challenge eligibility · Expert leaderboard tier |
| `DOJO_BELT_BLACK` | Mentor Board eligibility · NFT Certificate mint option · "Dojo Master" public badge |

---

### DOMAIN B: GROWTH ENGINE — PRESTIGE TIERS

```
PRESTIGE TIER SEQUENCE (immutable order):
  GROWTH_TIER_NONE → GROWTH_TIER_EXPLORER → GROWTH_TIER_CHALLENGER →
  GROWTH_TIER_CONTENDER → GROWTH_TIER_VETERAN → GROWTH_TIER_ELITE

Tier regression: FORBIDDEN. Prestige tiers are permanent once achieved.
Tier skipping: FORBIDDEN. Sequential only.
```

#### Prestige Tier XP Thresholds (cumulative total XP across all domains)

| Tier Level | Min Cumulative XP | Additional Engagement Gate | Privileges Unlocked |
|---|---|---|---|
| `GROWTH_TIER_EXPLORER` | 0 XP | Account verified | Public profile visible · Basic arena access |
| `GROWTH_TIER_CHALLENGER` | 1,000 XP | ≥ 10 activities completed | Referral system enabled · City leaderboard entry |
| `GROWTH_TIER_CONTENDER` | 5,000 XP | ≥ 1 belt (any Dojo domain) OR ≥ 30-day login streak | College leaderboard entry · Weekly challenge access |
| `GROWTH_TIER_VETERAN` | 15,000 XP | ≥ Green Belt (any domain) OR ≥ 3 referral activations | Global leaderboard entry · Advanced analytics dashboard · Profile boost eligibility |
| `GROWTH_TIER_ELITE` | 50,000 XP | ≥ Blue Belt (any domain) + ≥ 100-day login streak | "Elite" public badge · Sponsored challenge priority · Early access to new features · Creator amplification boost |

---

### DOMAIN B2: GROWTH ENGINE — SKILL LEVEL BARS (Per Technology)

```
SKILL LEVEL SEQUENCE (per technology tag):
  SKILL_LEVEL_BEGINNER → SKILL_LEVEL_INTERMEDIATE → SKILL_LEVEL_ADVANCED

Applied per skill_tag. Each technology is an independent level track.
Regression: FORBIDDEN (bars can only advance).
```

#### Skill Level XP Thresholds

| Skill Level | Min Skill XP (per tag) | Trigger |
|---|---|---|
| `SKILL_LEVEL_BEGINNER` | 0–300 XP | Automatic on first tagged scenario completion |
| `SKILL_LEVEL_INTERMEDIATE` | 301–1,000 XP | XP threshold gate, no manual approval needed |
| `SKILL_LEVEL_ADVANCED` | 1,001+ XP | XP threshold gate, no manual approval needed |

Skill level transitions are automatic (no mentor confirmation required). They represent cumulative evidence of scenario performance tagged to a technology, not a certified credential.

---

### DOMAIN C: SCHOOL OPS — ACADEMIC LEVELS

```
ACADEMIC LEVEL HIERARCHY (Institute-defined, not platform-defined):
  School Segment:
    SCHOOL_GRADE_6_8 → SCHOOL_GRADE_9_10 → SCHOOL_GRADE_11_12_SCIENCE
    SCHOOL_GRADE_11_12_COMMERCE → SCHOOL_GRADE_11_12_ARTS

  Post-Secondary Segment:
    DIPLOMA → ITI → POLYTECHNIC → UNDERGRADUATE → POSTGRADUATE → PHD

  Cohort Milestone Levels (Institute-defined per cohort):
    COHORT_MILESTONE_1 → COHORT_MILESTONE_2 → COHORT_MILESTONE_N

  Placement Readiness Level:
    PLACEMENT_NOT_READY → PLACEMENT_ELIGIBLE → PLACEMENT_READY_VERIFIED

Level progression authority: SCHOOL_OPS_AGENT + institute_admin role ONLY.
Platform engine does NOT auto-promote school academic levels.
```

#### School Ops Gate Requirements

| Transition Type | Required Gates |
|---|---|
| School Grade transition | Institute admin confirmation + academic record from SCHOOL_OPS_AGENT |
| Post-Secondary tier | Institute admin confirmation + enrollment record |
| Cohort Milestone | Institute-defined criteria met (tracked by SCHOOL_OPS_AGENT) |
| `PLACEMENT_ELIGIBLE` | Cohort milestone completion + placement readiness criteria from institute |
| `PLACEMENT_READY_VERIFIED` | Explicit PLACEMENT_OFFICER role confirmation — no automation permitted |

---

## 6️⃣ MULTI-GATE EVALUATION ENGINE (LOCKED ALGORITHM)

```
LEVEL_PROGRESSION_EVALUATION_ALGORITHM (v1.0.0):

INPUT: validated trigger event

STEP 1 — IDEMPOTENCY CHECK
  IF trigger_event_id already in PROCESSED_EVENTS_STORE (Redis, TTL 48h):
    RETURN transition_status = ALREADY_PROCESSED (200, no duplicate transition)
  ELSE: continue

STEP 2 — COLLUSION FLAG CHECK
  IF collusion_flag = true:
    BLOCK immediately
    transition_status = COLLUSION_SUPPRESSED
    LOG_INCIDENT (CRITICAL)
    ESCALATE_TO = GOVERNANCE_AGENT
    RETURN output (no further evaluation)

STEP 3 — LEVEL SEQUENCE VALIDATION
  IF target_level_id is not the direct next level above current_level_id:
    REJECT (level skipping forbidden)
    transition_status = REJECTED
    block_reason = "LEVEL_SKIP_FORBIDDEN"
    RETURN output

STEP 4 — CURRENT LEVEL STATE VERIFICATION
  Query LEVEL_STATE_STORE for user's authoritative current level
  IF input.current_level_id ≠ store.current_level_id:
    REJECT (stale trigger, state mismatch)
    transition_status = REJECTED
    block_reason = "STALE_LEVEL_STATE"
    RETURN output

STEP 5 — DOMAIN-SPECIFIC GATE EVALUATION
  Load gate_requirements from LEVEL_POLICY_STORE (versioned, immutable reference)
  FOR EACH gate in gate_requirements[target_level_id]:
    evaluate gate condition against input payload
    record gate_id, gate_name, required_value, actual_value, gate_passed
    IF gate_passed = false:
      transition_status = BLOCKED
      block_reason = "GATE_{gate_id}_FAILED"
      STOP gate evaluation
      RETURN output (gates_evaluated includes all evaluated up to failure)

STEP 6 — CONFIDENCE SCORE CHECK (Dojo only)
  IF domain = DOJO AND confidence_score < confidence_threshold[target_level_id]:
    transition_status = PENDING_MENTOR_BOARD
    block_reason = "LOW_CONFIDENCE_SCORE"
    EMIT PENDING_MENTOR_BOARD_EVENT
    RETURN output (no transition, held for mentor review)

STEP 7 — MENTOR CONFIRMATION CHECK (Dojo only)
  IF domain = DOJO:
    Verify mentor_confirmation_id exists in MENTOR_CONFIRMATION_STORE
    Verify mentor_confirmation_id.user_id = input.user_id
    Verify mentor_confirmation_id.target_belt = input.target_level_id
    Verify mentor is certified (MENTOR_CERTIFICATION_STORE check)
    IF Black Belt: verify Mentor Board quorum (≥ 3 certified mentors)
    IF any verification fails:
      transition_status = PENDING_MENTOR_BOARD
      block_reason = "MENTOR_CONFIRMATION_INVALID_OR_ABSENT"
      RETURN output

STEP 8 — EXECUTE TRANSITION
  Write to LEVEL_STATE_STORE (append-only):
    { user_id, tenant_id, domain, from_level, to_level, timestamp_utc,
      progression_transaction_id, gate_results, mentor_confirmation_id,
      policy_version, model_version }
  transition_status = PROMOTED
  new_level_id = target_level_id
  privileges_unlocked = PRIVILEGE_REGISTRY[target_level_id]

STEP 9 — EMIT DOWNSTREAM EVENTS
  EMIT LEVEL_UP_EVENT (Kafka topic: level.progression)
  EMIT ACHIEVEMENT_CHECK_EVENT
  EMIT RANK_UPDATE_EVENT
  EMIT NOTIFICATION_TRIGGER_EVENT
  EMIT FEATURE_UNLOCK_EVENT (for each privilege in privileges_unlocked)
  IF domain = DOJO: EMIT CERTIFICATE_GENERATION_EVENT
  IF milestone belt/tier: EMIT SHARE_TRIGGER_EVENT
  EMIT FEATURE_VECTOR_EMIT_EVENT to FEATURE_STORE_AGENT

STEP 10 — WRITE AUDIT LOG
  Write immutable record to LEVEL_AUDIT_LOG
  Include: full input_hash, output_hash, gates_evaluated, decision_path,
           model_version, policy_version, all event IDs emitted
  RETURN output
```

---

## 7️⃣ ML / AI LOGIC LAYER

### ML Usage (Governance-Assist, Non-Decisional)

```yaml
MODEL_TYPE:        Classification + Time-Series + Anomaly Detection
PURPOSE:           Detect abnormal progression velocity (farming), compute progression
                   health signals, predict drop-off risk for early intervention

FEATURES_USED:
  - matches_per_day (7d rolling avg)
  - avg_score_trend (14d rolling)
  - time_to_belt_promotion_days
  - scenario_difficulty_distribution
  - peer_score_variance_per_match
  - mentor_override_frequency
  - progression_velocity_percentile (vs cohort)
  - collusion_flag_history_30d
  - login_streak_at_time_of_promotion
  - referral_count_at_tier_change
  - school_attendance_rate_at_promotion

TRAINING_FREQUENCY:   Monthly
DRIFT_DETECTION:
  - Monitor distribution shift in time_to_belt_promotion_days
  - Monitor anomaly flag rate (baseline ± 2 sigma triggers review)
  - Monitor prediction accuracy on labeled farming cases (monthly)

VERSION_CONTROL:
  - model_version stored in every audit record
  - Immutable model snapshot per version tag
  - Rollback to previous version on drift alarm
  - ML output is advisory signal only — never overrides gate decisions

ML_OUTPUT_USAGE:
  - High anomaly score → flag to GOVERNANCE_AGENT for review (does not block transition)
  - Drop-off risk score → emitted to NOTIFICATION_ENGINE_AGENT for proactive nudge
  - Progression velocity signal → emitted to ANALYTICS_AGENT for curriculum review
```

### AI Usage (Strictly Scoped — Advisory Only)

```yaml
AI_USAGE_SCOPE:
  - Permitted: Natural language generation of level-up congratulations message
                (structured template, versioned prompt, deterministic structure)
  - Permitted: Summarizing gate failure reason into user-friendly explanation
               for NOTIFICATION_ENGINE_AGENT consumption
  - FORBIDDEN: AI may never make or override a level transition decision
  - FORBIDDEN: Creative interpretation of gate conditions
  - FORBIDDEN: AI may never confirm or deny mentor board outcomes

PROMPT_GOVERNANCE:
  - Versioned prompt templates only (stored in PROMPT_REGISTRY, immutable)
  - Deterministic structured prompts — no open-ended generation
  - AI output reviewed by template validator before emitting to downstream
  - AI is called AFTER transition decision is made — never before

AI_ASSISTS_ML_NOT_REPLACES_IT = ENFORCED
```

---

## 8️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS:        2,000 (sustained) / 8,000 (peak tournament end wave)
LATENCY_TARGET:      Level evaluation pipeline P99 < 300ms
                     Level state write P99 < 100ms
                     Event emission (Kafka) P99 < 50ms
MAX_CONCURRENCY:     40,000 simultaneous evaluation requests
QUEUE_STRATEGY:      Kafka topic per domain:
                       level.dojo.trigger
                       level.growth.trigger
                       level.school_ops.trigger
                     Consumer group per domain with DLQ
                     Idempotency via trigger_event_id dedup key (Redis, TTL 48h)

HORIZONTAL_SCALING:  Stateless agent — deploy N replicas behind load balancer
STATELESS:           All state in PostgreSQL (LEVEL_STATE_STORE) +
                     Redis (dedup + current-level cache per user)
EVENT_DRIVEN:        Consumed from Kafka, all downstream triggers emitted to Kafka
ASYNC_PROCESSING:    All downstream events emitted async with DLQ monitoring
IDEMPOTENT:          trigger_event_id is idempotency key — duplicate events produce
                     no duplicate transitions
CACHE_STRATEGY:      Current level per user cached in Redis (TTL: 1 hour)
                     Cache invalidated on every PROMOTED transition
                     Cache miss → authoritative read from PostgreSQL LEVEL_STATE_STORE
```

---

## 9️⃣ SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  - Every query includes tenant_id filter at query layer
  - No cross-tenant level state reads or writes — ever
  - tenant_id validated against JWT claim on every request
  - Row-level security enforced at PostgreSQL layer

DOMAIN_ISOLATION:
  - domain_track checked against user profile on every trigger
  - Cross-domain progression mapping forbidden
  - SCHOOL_OPS triggers rejected from Company tenant types
  - Dojo belt events rejected if not sourced from MATCH_ENGINE events

ROLE_BASED_AUTHORIZATION:
  - actor_role must be in ROLE_TRIGGER_AUTHORIZATION_MATRIX for trigger_type
  - PLACEMENT_OFFICER role required for PLACEMENT_READY_VERIFIED transition
  - MENTOR / EVALUATOR role required for mentor confirmation origin
  - AUTOMATION role requires signed service-account JWT

ENCRYPTION:
  - Level state data encrypted at rest (AES-256)
  - All user_id references are UUIDs — no PII in level records
  - Transit encryption: TLS 1.3 minimum
  - Secret management via secrets manager — no plaintext config in production

AUDIT_LOGGING:
  - Every evaluation attempt logged to immutable LEVEL_AUDIT_LOG
  - Includes full gate trace, input_hash, output_hash, decision_path
  - Schema constraint: no DELETE or UPDATE permitted on audit table
  - Audit log failures halt the transaction — consistency over availability

ACCESS_LOG_TRACKING:
  - All reads of LEVEL_STATE_STORE logged
  - Admin overrides logged separately with actor_id, reason, timestamp
  - Mentor confirmation usage logged with mentor_id + certification check result
```

---

## 🔟 AUDIT & TRACEABILITY

Every level evaluation attempt — successful or not — emits this immutable audit record:

```json
{
  "audit_id":                      "UUID",
  "progression_transaction_id":    "UUID",
  "timestamp_utc":                 "ISO-8601",
  "actor_id":                      "UUID (user_id or service_account_id)",
  "tenant_id":                     "UUID",
  "institution_id":                "UUID | null (School Ops only)",
  "input_hash":                    "SHA-256 of canonicalized input payload",
  "output_hash":                   "SHA-256 of canonicalized output payload",
  "model_version":                 "level_progression_engine_v1.0.0",
  "policy_version":                "v1.0.0",
  "domain":                        "DOJO | GROWTH | SCHOOL_OPS",
  "domain_track":                  "ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION",
  "current_level_id":              "String",
  "target_level_id":               "String",
  "transition_status":             "Enum",
  "gates_evaluated": [
    {
      "gate_id":         "String",
      "gate_name":       "String",
      "required_value":  "Any",
      "actual_value":    "Any",
      "gate_passed":     "Boolean"
    }
  ],
  "decision_path":  ["IDEMPOTENCY_CHECK", "COLLUSION_CHECK", "SEQUENCE_VALIDATION",
                     "STATE_VERIFICATION", "GATE_EVALUATION", "CONFIDENCE_CHECK",
                     "MENTOR_CHECK", "TRANSITION_EXECUTED", "EVENTS_EMITTED"],
  "confidence_score":              "Float | null",
  "mentor_confirmation_id":        "UUID | null",
  "anomaly_flags":                 "Array | []",
  "downstream_events_emitted":     "Array of event_id strings"
}
```

**Audit Table:** `level_progression_audit_log` — append-only. No DELETE or UPDATE permitted. Schema-enforced. Verified by GOVERNANCE_AGENT quarterly.

---

## 1️⃣1️⃣ FAILURE POLICY

### Failure Scenarios and Required Behavior

| Failure Condition | Behavior |
|---|---|
| **Invalid / missing required field** | REJECT → 400 with field-level validation error · Log to audit (transition_status = REJECTED) · Do NOT halt downstream pipeline |
| **Duplicate trigger_event_id** | REJECT idempotently → 200 with transition_status = ALREADY_PROCESSED · Log replay attempt with anomaly flag · No transition executed |
| **Collusion flag present** | BLOCK immediately · transition_status = COLLUSION_SUPPRESSED · LOG_INCIDENT · ESCALATE to GOVERNANCE_AGENT |
| **LEVEL_POLICY_STORE unavailable** | STOP_EXECUTION · LOG_INCIDENT · ESCALATE to GOVERNANCE_AGENT + OPS_TEAM · RETRY_POLICY = 3× exponential backoff (1s, 2s, 4s) → DLQ |
| **MENTOR_CONFIRMATION_STORE unavailable** | STOP_EXECUTION for Dojo triggers · LOG_INCIDENT · RETRY_POLICY = 3× backoff → DLQ · Non-Dojo triggers continue (no mentor gate) |
| **LEVEL_STATE_STORE write failure** | STOP_EXECUTION · Do NOT emit downstream events (consistency rule) · LOG_INCIDENT · ESCALATE to OPS_TEAM · RETRY_POLICY = 3× with circuit breaker |
| **Kafka emit failure (downstream events)** | Retry 3× · If all fail → write to DLQ with full context · Alert OBSERVABILITY_AGENT · Transaction is still recorded in LEVEL_STATE_STORE |
| **Confidence score < threshold (Dojo)** | transition_status = PENDING_MENTOR_BOARD · Hold transition · Emit PENDING_MENTOR_BOARD_EVENT · Do NOT block user experience — inform via NOTIFICATION_ENGINE_AGENT |
| **Mentor confirmation absent or invalid (Dojo)** | transition_status = PENDING_MENTOR_BOARD · Hold transition · Emit escalation to MENTOR_CONTROL_ENGINE_AGENT |
| **Tenant isolation violation detected** | STOP_EXECUTION immediately · LOG_SECURITY_INCIDENT (CRITICAL) · ESCALATE to SECURITY_AGENT + GOVERNANCE_AGENT · Block all further requests from actor_id until reviewed |
| **Level state mismatch (stale trigger)** | REJECT → transition_status = REJECTED · Log with anomaly flag STALE_TRIGGER · No transition |
| **Level skip attempted** | REJECT → transition_status = REJECTED · Log with anomaly flag SKIP_ATTEMPT |

**No silent failures permitted under any condition.**

---

## 1️⃣2️⃣ INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - XP_ENGINE_AGENT                 # Primary trigger: XP threshold → belt eligibility check
  - SCORING_ENGINE_AGENT            # Match scores, avg score, confidence
  - MATCH_ENGINE_AGENT              # Match count validation
  - MENTOR_CONTROL_ENGINE_AGENT     # Mentor confirmation signals
  - COLLUSION_DETECTION_AGENT       # Abuse/fraud flags
  - SCHOOL_OPS_AGENT                # Academic records, attendance, assignment completion
  - ANALYTICS_AGENT                 # Skill XP aggregates per technology tag
  - TOURNAMENT_ENGINE_AGENT         # Tournament completion signals (can feed Dojo gate evidence)
  - AUTH_SERVICE                    # JWT validation

DOWNSTREAM_AGENTS:
  - CERTIFICATION_BELT_ENGINE_AGENT # Receives CERTIFICATE_GENERATION_EVENT on belt PROMOTED
  - ACHIEVEMENT_ENGINE_AGENT        # Checks for achievement badge conditions on level change
  - RANK_ENGINE_AGENT               # Recalculates rank on level change
  - LEADERBOARD_AGENT               # Refreshes multi-tier leaderboard (city/college/global)
  - NOTIFICATION_ENGINE_AGENT       # Sends level-up + privilege unlock notifications
  - FEATURE_GATE_AGENT              # Unlocks privileges defined in PRIVILEGE_REGISTRY
  - GROWTH_EVENT_AGENT              # Receives SHARE_TRIGGER_EVENT on prestige milestones
  - ANALYTICS_AGENT                 # Progression velocity, drop-off signals
  - OBSERVABILITY_AGENT             # Pipeline health, gate failure rates
  - FEATURE_STORE_AGENT             # Passive intelligence feature vectors

EVENT_TRIGGERS_EMITTED:

  LEVEL_UP_EVENT:
    topic: level.progression
    payload: { progression_transaction_id, user_id, tenant_id, domain, domain_track,
               from_level, to_level, privileges_unlocked, timestamp_utc, policy_version }
    condition: transition_status = PROMOTED

  LEVEL_PROGRESSION_BLOCKED_EVENT:
    topic: level.progression.blocked
    payload: { progression_transaction_id, user_id, tenant_id, domain, transition_status,
               block_reason, gates_evaluated, timestamp_utc }
    condition: transition_status ≠ PROMOTED and ≠ ALREADY_PROCESSED

  PENDING_MENTOR_BOARD_EVENT:
    topic: level.mentor_board.pending
    payload: { progression_transaction_id, user_id, tenant_id, target_level_id,
               confidence_score, mentor_confirmation_id, timestamp_utc }
    condition: transition_status = PENDING_MENTOR_BOARD

  CERTIFICATE_GENERATION_EVENT:
    topic: certification.generate
    payload: { user_id, tenant_id, belt_level, domain_track, progression_transaction_id,
               policy_version, timestamp_utc }
    condition: domain = DOJO and transition_status = PROMOTED

  FEATURE_UNLOCK_EVENT:
    topic: feature.unlock
    payload: { user_id, tenant_id, privilege_id, level_id, timestamp_utc }
    condition: transition_status = PROMOTED, emitted per privilege in privileges_unlocked

  SHARE_TRIGGER_EVENT:
    topic: growth.share_trigger
    payload: { user_id, tenant_id, milestone_type, level_id, timestamp_utc }
    condition: Prestige tier change OR belt BLACK/BLUE OR placement readiness verified

  FEATURE_VECTOR_EMIT_EVENT:
    topic: feature_store.ingest
    payload:
      user_id:              UUID
      feature_name:         "level_progression_event"
      feature_value:        { domain, from_level, to_level, transition_status, gates_passed }
      timestamp:            ISO-8601
      source_agent:         "LEVEL_PROGRESSION_AGENT"
```

---

## 1️⃣3️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

```yaml
FEATURE_VECTORS_EMITTED_PER_EVALUATION:
  - feature: current_dojo_belt_encoded
    value: integer (NONE=0, WHITE=1, YELLOW=2, GREEN=3, BLUE=4, BLACK=5)
  - feature: current_growth_prestige_tier_encoded
    value: integer (EXPLORER=1, CHALLENGER=2, CONTENDER=3, VETERAN=4, ELITE=5)
  - feature: current_school_level_encoded
    value: integer (domain-specific encoding per academic track)
  - feature: gates_passed_ratio
    value: float (gates_passed / gates_total — per evaluation attempt)
  - feature: time_since_last_promotion_days
    value: integer (days since last PROMOTED transition in domain)
  - feature: total_blocked_attempts_30d
    value: integer (count of BLOCKED transitions in last 30 days per domain)
  - feature: mentor_confirmation_lead_time_hours
    value: float (hours between mentor confirmation issued and transition triggered)
  - feature: progression_domain_encoded
    value: integer (DOJO=1, GROWTH=2, SCHOOL_OPS=3)
  - feature: confidence_score_at_promotion
    value: float | null

COMPATIBILITY:
  FEATURE_STORE_AGENT:       ✅
  IDEA_DNA_AGENT:            N/A (Level Progression does not touch ideas)
  ROYALTY_ENGINE:            N/A
  GROWTH_ENGINE:             ✅ (passive signal consumer)
  ANALYTICS_AGENT:           ✅ (curriculum + progression health signals)
```

---

## 1️⃣4️⃣ GROWTH ENGINE HOOK

```yaml
RANK_UPDATE_EVENT:      Emitted on every PROMOTED transition
XP_UPDATE_EVENT:        N/A — XP_ENGINE_AGENT owns this (LEVEL_PROGRESSION_AGENT is downstream)
SHARE_TRIGGER_EVENT:    Emitted on these milestone transitions:
  - DOJO_BELT_BLUE (promoted)
  - DOJO_BELT_BLACK (promoted)
  - GROWTH_TIER_VETERAN (promoted)
  - GROWTH_TIER_ELITE (promoted)
  - PLACEMENT_READY_VERIFIED (promoted)
  - Any DOJO_BELT_* first-ever promotion for the user (FIRST_BELT milestone)

LEADERBOARD_REFRESH:    Triggered on every domain PROMOTED transition
FEATURE_UNLOCK:         Triggered per privilege in PRIVILEGE_REGISTRY[target_level_id]
```

---

## 1️⃣5️⃣ PERFORMANCE MONITORING

```yaml
METRICS:
  success_rate:              PROMOTED transitions / total evaluations
  error_rate:                REJECTED transitions / total evaluations
  blocked_rate:              BLOCKED transitions / total evaluations
  pending_mentor_rate:       PENDING_MENTOR_BOARD / total Dojo evaluations
  collusion_suppression_rate: COLLUSION_SUPPRESSED / total evaluations
  latency_p50:               Median evaluation pipeline latency (ms)
  latency_p99:               P99 evaluation pipeline latency — target < 300ms
  gate_failure_distribution: Breakdown of which gate fails most frequently per belt/tier
  mentor_board_backlog:      Count of PENDING_MENTOR_BOARD awaiting resolution (alert if > 50)
  drift_indicator:           ML anomaly model confidence drift (monitored monthly)
  anomaly_frequency:         anomaly_flags non-empty / total evaluations
  dl_queue_depth:            DLQ message depth per domain topic (alert if > 100)

DASHBOARDS_REQUIRED:
  - Level Progression Pipeline Health (success/block/error rates per domain)
  - Belt Gate Failure Heatmap (which gates fail most — signals curriculum issues)
  - Mentor Board Backlog (pending confirmations count + age)
  - Progression Velocity Distribution (time-to-belt per domain_track)
  - Anomaly Frequency Trend (farming detection signals)

ALERTING:
  evaluation_p99 > 600ms               → WARN
  error_rate > 3%                       → CRITICAL
  collusion_suppression_rate spike >3x avg → CRITICAL
  mentor_board_backlog > 50             → WARN
  mentor_board_backlog > 200            → CRITICAL
  audit_log_write_failure               → CRITICAL (data integrity)
  kafka_dlq_depth > 100                 → WARN

INTEGRATED_WITH: OBSERVABILITY_AGENT
```

---

## 1️⃣6️⃣ VERSIONING POLICY

```yaml
VERSIONING_RULES:
  ADD_ONLY:                 All changes are additive — no existing values altered
  NEW_BELT_LEVEL:           Requires major version bump (v1.0.0 → v2.0.0)
  GATE_VALUE_CHANGE:        Requires major version bump + full migration documentation
  NEW_PRESTIGE_TIER:        Requires minor version bump (v1.0.0 → v1.1.0)
  NEW_PRIVILEGE:            Requires minor version bump
  NEW_SKILL_LEVEL:          Requires minor version bump
  SCHOOL_OPS_LEVEL_ADD:     Requires minor version bump + institute admin confirmation
  ALGORITHM_CHANGE:         Requires major version bump + backward compat window
  PROMPT_TEMPLATE_CHANGE:   Requires minor version bump in PROMPT_REGISTRY

BACKWARD_COMPATIBILITY:
  - Old progression_transaction_id references valid indefinitely
  - Old policy_version audit records retained and readable
  - Users promoted under older policy retain their level — no retroactive gate re-evaluation

MIGRATION_DOCUMENTATION:
  - Every version bump must include: what changed, what was added, backward compat impact,
    effective date, and migration path for in-flight PENDING_MENTOR_BOARD records
  - Stored in: LEVEL_PROGRESSION_POLICY_CHANGELOG (append-only versioned document)

ROLLBACK:
  - Deactivate new policy version
  - Reactivate previous policy version
  - All PROMOTED transitions in new version remain valid (no regression)
  - In-flight evaluations re-routed to previous version on rollback
```

---

## 1️⃣7️⃣ DATABASE SCHEMA (LOCKED v1.0.0)

```sql
-- Level State Store: current level per user per domain (mutable snapshot)
CREATE TABLE level_user_state (
  state_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id             UUID NOT NULL,
  tenant_id           UUID NOT NULL,
  domain              TEXT NOT NULL CHECK (domain IN ('DOJO', 'GROWTH', 'SCHOOL_OPS')),
  domain_track        TEXT NOT NULL,
  skill_tag           TEXT,              -- NULL for belt/tier/academic; populated for SKILL_LEVEL_BAR
  current_level_id    TEXT NOT NULL,
  policy_version      TEXT NOT NULL,
  last_promoted_at    TIMESTAMPTZ,
  last_updated_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE (user_id, tenant_id, domain, domain_track, skill_tag)
);
-- Partitioned by tenant_id for isolation
-- Row-level security enforced

-- Level Progression Ledger: append-only transition history
CREATE TABLE level_progression_ledger (
  progression_transaction_id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  trigger_event_id            UUID NOT NULL UNIQUE,  -- idempotency key
  user_id                     UUID NOT NULL,
  tenant_id                   UUID NOT NULL,
  domain                      TEXT NOT NULL,
  domain_track                TEXT NOT NULL,
  skill_tag                   TEXT,
  from_level_id               TEXT NOT NULL,
  to_level_id                 TEXT NOT NULL,
  transition_status           TEXT NOT NULL,
  block_reason                TEXT,
  gates_evaluated             JSONB NOT NULL DEFAULT '[]',
  privileges_unlocked         JSONB NOT NULL DEFAULT '[]',
  mentor_confirmation_id      UUID,
  confidence_score            NUMERIC(4,3),
  policy_version              TEXT NOT NULL,
  model_version               TEXT NOT NULL,
  audit_reference             UUID NOT NULL,
  created_at                  TIMESTAMPTZ NOT NULL DEFAULT now()
);
-- IMMUTABLE: no UPDATE or DELETE permitted (schema constraint + policy)

-- Level Audit Log: append-only, full traceability
CREATE TABLE level_progression_audit_log (
  audit_id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  progression_transaction_id  UUID NOT NULL,
  timestamp_utc               TIMESTAMPTZ NOT NULL,
  actor_id                    UUID NOT NULL,
  tenant_id                   UUID NOT NULL,
  institution_id              UUID,
  input_hash                  TEXT NOT NULL,
  output_hash                 TEXT NOT NULL,
  model_version               TEXT NOT NULL,
  policy_version              TEXT NOT NULL,
  decision_path               JSONB NOT NULL,
  gates_evaluated             JSONB NOT NULL DEFAULT '[]',
  anomaly_flags               JSONB NOT NULL DEFAULT '[]',
  transition_status           TEXT NOT NULL,
  downstream_events_emitted   JSONB NOT NULL DEFAULT '[]',
  created_at                  TIMESTAMPTZ NOT NULL DEFAULT now()
);
-- IMMUTABLE: no UPDATE or DELETE permitted

-- Level Policy Store: versioned gate definitions (read-only at runtime)
CREATE TABLE level_policy_store (
  policy_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  policy_version    TEXT NOT NULL UNIQUE,
  domain            TEXT NOT NULL,
  level_id          TEXT NOT NULL,
  gate_definitions  JSONB NOT NULL,
  privileges        JSONB NOT NULL,
  is_active         BOOLEAN NOT NULL DEFAULT false,
  effective_from    TIMESTAMPTZ NOT NULL,
  effective_until   TIMESTAMPTZ,
  created_at        TIMESTAMPTZ NOT NULL DEFAULT now()
);
-- Only one policy_version active per domain at a time
-- ADD-ONLY: no existing policy rows mutated

-- Mentor Confirmation Store: verified mentor board decisions
CREATE TABLE mentor_confirmation_store (
  confirmation_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id             UUID NOT NULL,
  tenant_id           UUID NOT NULL,
  target_belt_level   TEXT NOT NULL,
  mentor_id           UUID NOT NULL,
  mentor_board_ids    JSONB,            -- populated for Black Belt (quorum)
  confirmed_at        TIMESTAMPTZ NOT NULL,
  expires_at          TIMESTAMPTZ NOT NULL,  -- confirmation valid window: 7 days
  consumed            BOOLEAN NOT NULL DEFAULT false,
  consumed_at         TIMESTAMPTZ,
  created_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);
-- consumed = true after used in a PROMOTED transition (one-use per confirmation_id)
```

---

## 1️⃣8️⃣ NON-NEGOTIABLE RULES (SEALED — CANNOT BE OVERRIDDEN)

```
LEVEL_PROGRESSION_AGENT MUST NOT:
  ❌ Allow level skipping — transitions must be sequential, one level at a time
  ❌ Execute auto-promotion for Dojo belts — mentor confirmation mandatory, always
  ❌ Allow belt regression — once a belt is awarded it cannot be revoked by this agent
  ❌ Allow prestige tier regression — tiers are permanent achievements
  ❌ Create hidden gate logic outside of LEVEL_POLICY_STORE
  ❌ Modify any historical level state records (append-only, ever)
  ❌ Auto-delete audit logs under any condition
  ❌ Override COLLUSION_DETECTION_AGENT suppression flags
  ❌ Bypass tenant isolation checks for any reason
  ❌ Mix domain-track level state across tenant scopes
  ❌ Use AI/LLM output to make level transition decisions
  ❌ Skip audit log writes — audit failure = transaction failure
  ❌ Process triggers with policy_version mismatch
  ❌ Execute transitions without matching all required upstream dependencies
  ❌ Override GOVERNANCE_AGENT escalation outcomes
  ❌ Promote School Ops academic levels without institute admin confirmation
  ❌ Promote PLACEMENT_READY_VERIFIED without explicit PLACEMENT_OFFICER role signal
  ❌ Execute outside the scope of the TRIGGER_TYPE_REGISTRY
  ❌ Execute outside scope — any undefined trigger_type → REJECT immediately
```

---

## 🔒 TRIGGER_TYPE_REGISTRY (LOCKED v1.0.0)

All trigger_type values permitted in this agent version. Any value not in this registry → REJECT immediately.

```
DOJO DOMAIN:
  DOJO_BELT_WHITE_ELIGIBILITY_CHECK
  DOJO_BELT_YELLOW_ELIGIBILITY_CHECK
  DOJO_BELT_GREEN_ELIGIBILITY_CHECK
  DOJO_BELT_BLUE_ELIGIBILITY_CHECK
  DOJO_BELT_BLACK_ELIGIBILITY_CHECK
  DOJO_BELT_MENTOR_BOARD_CLEARANCE     # Fired when mentor board confirms a pending promotion
  DOJO_SKILL_LEVEL_BAR_UPDATE          # Per technology tag skill XP threshold crossed

GROWTH DOMAIN:
  GROWTH_PRESTIGE_TIER_CHALLENGER_CHECK
  GROWTH_PRESTIGE_TIER_CONTENDER_CHECK
  GROWTH_PRESTIGE_TIER_VETERAN_CHECK
  GROWTH_PRESTIGE_TIER_ELITE_CHECK
  GROWTH_SKILL_LEVEL_INTERMEDIATE_CHECK
  GROWTH_SKILL_LEVEL_ADVANCED_CHECK

SCHOOL OPS DOMAIN:
  SCHOOL_GRADE_TRANSITION_CHECK
  SCHOOL_POST_SECONDARY_TRANSITION_CHECK
  SCHOOL_COHORT_MILESTONE_CHECK
  SCHOOL_PLACEMENT_ELIGIBLE_CHECK
  SCHOOL_PLACEMENT_READY_VERIFIED_CHECK
```

---

## 🔒 ROLE_TRIGGER_AUTHORIZATION_MATRIX (LOCKED v1.0.0)

| Trigger Type Group | Permitted Actor Roles |
|---|---|
| DOJO_BELT_*_ELIGIBILITY_CHECK | AUTOMATION (from SCORING_ENGINE), EVALUATOR |
| DOJO_BELT_MENTOR_BOARD_CLEARANCE | TRAINER, EVALUATOR, ADMIN |
| DOJO_SKILL_LEVEL_BAR_UPDATE | AUTOMATION (from ANALYTICS_AGENT) |
| GROWTH_PRESTIGE_TIER_* | AUTOMATION (from XP_ENGINE_AGENT) |
| GROWTH_SKILL_LEVEL_* | AUTOMATION (from ANALYTICS_AGENT) |
| SCHOOL_GRADE_TRANSITION_CHECK | ADMIN (Institute Tenant), AUTOMATION (from SCHOOL_OPS_AGENT) |
| SCHOOL_POST_SECONDARY_TRANSITION_CHECK | ADMIN (Institute Tenant) |
| SCHOOL_COHORT_MILESTONE_CHECK | AUTOMATION (from SCHOOL_OPS_AGENT), ADMIN |
| SCHOOL_PLACEMENT_ELIGIBLE_CHECK | AUTOMATION, ADMIN |
| SCHOOL_PLACEMENT_READY_VERIFIED_CHECK | PLACEMENT_OFFICER only |

---

## 🔒 FINAL GOVERNANCE SEAL

```
╔══════════════════════════════════════════════════════════════════════════════════╗
║               LEVEL_PROGRESSION_AGENT — FINAL GOVERNANCE SEAL                  ║
║                                                                                  ║
║  LEVEL TRANSITION AUTHORITY          MULTI-GATE DETERMINISTIC RULE ENGINE       ║
║  AI IN PROGRESSION DECISIONS         FORBIDDEN                                  ║
║  AUTO BELT PROMOTION                 FORBIDDEN — MENTOR CONFIRMATION MANDATORY  ║
║  LEVEL REGRESSION                    FORBIDDEN — APPEND-ONLY STATE              ║
║  LEVEL SKIPPING                      FORBIDDEN — SEQUENTIAL ONLY                ║
║  AUDIT TRAIL                         IMMUTABLE APPEND-ONLY                      ║
║  TENANT ISOLATION                    HARD ENFORCED AT QUERY + JWT LAYER         ║
║  CROSS-DOMAIN LEVEL MIXING           FORBIDDEN                                  ║
║  SILENT FAILURES                     FORBIDDEN                                  ║
║  HISTORY MUTATION                    FORBIDDEN                                  ║
║  ASSUMPTION FILLING                  FORBIDDEN                                  ║
║  CREATIVE POLICY INTERPRETATION      FORBIDDEN                                  ║
║  COLLUSION OVERRIDE                  FORBIDDEN                                  ║
║                                                                                  ║
║  DOJO BELT OWNER:           LEVEL_PROGRESSION_AGENT v1.0.0                     ║
║  GROWTH PRESTIGE OWNER:     LEVEL_PROGRESSION_AGENT v1.0.0                     ║
║  SCHOOL OPS LEVEL OWNER:    LEVEL_PROGRESSION_AGENT v1.0.0                     ║
║  SKILL LEVEL BAR OWNER:     LEVEL_PROGRESSION_AGENT v1.0.0                     ║
║                                                                                  ║
║  ARCHITECTURE:              SEALED                                              ║
║  INTERPRETATION AUTHORITY:  NONE                                                ║
║  MUTATION AUTHORITY:        VERSION BUMP ONLY — ADD-ONLY                       ║
║                                                                                  ║
║  ECOSKILLER ANTIGRAVITY — ENTERPRISE SAAS GRADE — 10M–100M USERS               ║
╚══════════════════════════════════════════════════════════════════════════════════╝
```

---
*LEVEL_PROGRESSION_AGENT.md — v1.0.0 | Ecoskiller Antigravity | Generated per Master Agent Creation Prompt | Sealed & Locked*
