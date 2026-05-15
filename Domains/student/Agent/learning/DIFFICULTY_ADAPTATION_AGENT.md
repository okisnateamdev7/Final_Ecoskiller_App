# 🔒 DIFFICULTY_ADAPTATION_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED ENTERPRISE AGENT SPECIFICATION
### Domains: Dojo · Growth Engine · School Ops (Institute ERP)
### Version: v1.0.0 | Mutation Policy: ADD-ONLY | Execution Mode: DETERMINISTIC + VALIDATED

---

```
╔═════════════════════════════════════════════════════════════════════════════════════╗
║            MASTER SEAL BLOCK — DO NOT MODIFY WITHOUT VERSION BUMP                  ║
║                                                                                     ║
║  AGENT_NAME                        = DIFFICULTY_ADAPTATION_AGENT                   ║
║  EXECUTION_MODE                    = DETERMINISTIC + VALIDATED                     ║
║  MUTATION_POLICY                   = ADD_ONLY                                      ║
║  CREATIVE_INTERPRETATION           = FORBIDDEN                                     ║
║  ASSUMPTION_FILLING                = FORBIDDEN                                     ║
║  DEFAULT_BEHAVIOR                  = DENY                                          ║
║  FAILURE_MODE                      = HALT_ON_AMBIGUITY                            ║
║  CROSS_TENANT_ACCESS               = FORBIDDEN                                     ║
║  SILENT_FAILURES                   = FORBIDDEN                                     ║
║  AUTHOR_DECLARED_DIFFICULTY        = FORBIDDEN (data-derived only)                 ║
║  AI_IN_DIFFICULTY_DECISIONS        = FORBIDDEN (ML-governed only)                  ║
║  STATIC_DIFFICULTY_LABELS          = FORBIDDEN (T4 mandate: must be dynamic)       ║
║  CROSS_DOMAIN_DIFFICULTY_MIXING    = FORBIDDEN                                     ║
║  SILENT_SCENARIO_RETIREMENT        = FORBIDDEN (audit trail required)              ║
║  HISTORY_MUTATION                  = FORBIDDEN                                     ║
╚═════════════════════════════════════════════════════════════════════════════════════╝
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY — LOCKED)

```yaml
AGENT_NAME:           DIFFICULTY_ADAPTATION_AGENT
AGENT_VERSION:        v1.0.0
SYSTEM_ROLE:          Authoritative engine for continuous, data-driven computation,
                      reclassification, retirement, and governance of scenario/content
                      difficulty levels across Dojo, Growth, and School Ops domains.
                      Also owns adaptive difficulty pathway assignment per user,
                      learning regression detection, skill gap signal emission, and
                      curriculum health flagging across all three owner-domains.
PRIMARY_DOMAIN:       Difficulty Adaptation / Curriculum Intelligence / Learning Effectiveness
EXECUTION_MODE:       Deterministic + Validated (ML-governed, not AI-governed)
DATA_SCOPE:           Scenario pass rates, score distributions, completion times,
                      failure clustering, abandonment rates, per-user performance
                      history, skill gap signals, difficulty normalization factors,
                      curriculum health flags, scenario retirement audit records,
                      learning gain delta signals, retention check outcomes,
                      drill effectiveness scores, inactivity re-engagement triggers,
                      School Ops assignment difficulty records, Growth challenge calibration
TENANT_SCOPE:         Strict Per-Tenant Isolation (Institute ≠ Company ≠ Platform)
FAILURE_POLICY:       HALT on ambiguity · LOG_INCIDENT · ESCALATE to GOVERNANCE_AGENT
MODEL_VERSION_REF:    difficulty_adaptation_engine_v1.0.0
AUDIT_TRAIL:          Append-only, immutable — every reclassification, retirement,
                      pathway assignment, and regression flag is logged
```

**This agent must never assume missing specifications.
Difficulty labels are ALWAYS data-derived. Author-declared difficulty is NEVER accepted
as a final label. Any undefined input → REJECT + LOG. No difficulty decision executes
without validated statistical evidence meeting minimum sample thresholds.**

---

## 2️⃣ PURPOSE DECLARATION

### The Problem This Agent Solves

Across Ecoskiller Antigravity's three domains, content difficulty is the most critical and most commonly corrupted variable in any learning or assessment system. Static, author-declared difficulty labels become invalid the moment real users interact with content. A scenario declared "Hard" by its creator may be trivially solved by most users; one declared "Easy" may become an inadvertent gate-blocker. Both outcomes corrupt belt eligibility signals, XP multiplier fairness, and learning path quality.

The DIFFICULTY_ADAPTATION_AGENT enforces Section T4 of Dojo For Arts: **"Difficulty labels must be data-derived, not author-declared."** It is the single authoritative engine for:

- Continuously computing scenario/content difficulty from live performance data
- Reclassifying difficulty tiers when statistical evidence warrants
- Retiring scenarios that fail fairness or stagnation thresholds
- Assigning adaptive difficulty pathways to individual users based on their progression state
- Detecting learning regression, skill stagnation, and performance variance
- Flagging curriculum weakness to CURRICULUM_GOVERNANCE_AGENT
- Emitting normalised difficulty factors to SCORING_ENGINE_AGENT for fair XP multiplier computation
- Signalling career/skill obsolescence re-engagement for School Ops and Growth domains

### Three-Domain Difficulty Architecture

| Domain | Difficulty Surface | Adaptation Mechanism |
|---|---|---|
| **Dojo** | Scenario difficulty tiers (EASY → MEDIUM → HARD → PRESSURE) · Per-user difficulty pathway · Skill tree unlock gates | Statistical reclassification from pass rate, score distribution, completion time, abandonment, failure clustering. User pathway adapted from belt level + recent performance trend |
| **Growth Engine** | Weekly challenge difficulty · Time Attack mode calibration · Goal difficulty scaling · Skill Level Bar advancement gating | XP-weighted performance trend, engagement drop-off signals, plateau detection per technology tag |
| **School Ops** | Assignment difficulty tiers · Cohort challenge levels · Practice drill calibration · Re-engagement triggers for inactivity / skill obsolescence | Institute-scoped performance aggregates, assignment pass rates, cohort percentile distribution, inactivity threshold breach |

### What It Consumes
- Match completion events from MATCH_ENGINE_AGENT (scenario_id, score, time, outcome)
- Score events from SCORING_ENGINE_AGENT (scenario score, role, confidence)
- Abandonment events from MATCH_ENGINE_AGENT (scenario_id, abandoned_at, user_id)
- XP events from XP_ENGINE_AGENT (per-user skill tag XP delta)
- Assignment completion events from SCHOOL_OPS_AGENT (assignment_id, score, on_time)
- Inactivity signals from AUTH_SERVICE / SCHOOL_OPS_AGENT (last_active_at)
- Drill engagement events from ANALYTICS_AGENT
- Belt state from LEVEL_PROGRESSION_AGENT (for pathway computation)
- Collusion flags from COLLUSION_DETECTION_AGENT (exclude flagged matches from calibration)

### What It Produces
- Updated difficulty classification records (append-only, versioned)
- Scenario retirement recommendations to CURRICULUM_GOVERNANCE_AGENT
- Per-user adaptive difficulty pathway assignments (next-scenario difficulty tier)
- Learning regression alerts to NOTIFICATION_ENGINE_AGENT
- Skill gap signals to FEATURE_STORE_AGENT + SKILL_GAP_ANALYSIS_AGENT
- Difficulty normalization factors to SCORING_ENGINE_AGENT (per scenario, per tier)
- Curriculum health flags to CURRICULUM_GOVERNANCE_AGENT
- Inactivity re-engagement signals to NOTIFICATION_ENGINE_AGENT
- Passive feature vectors to FEATURE_STORE_AGENT

### Downstream Agents Depending on This Agent
- `SCORING_ENGINE_AGENT` — consumes difficulty normalization factor per scenario for fair XP
- `CURRICULUM_GOVERNANCE_AGENT` — receives retirement recommendations + curriculum flags
- `MATCH_ENGINE_AGENT` — consumes user's adaptive difficulty pathway for scenario selection
- `SKILL_GAP_ANALYSIS_AGENT` — consumes skill gap signals per user per technology tag
- `NOTIFICATION_ENGINE_AGENT` — receives regression alerts + inactivity re-engagement triggers
- `FEATURE_STORE_AGENT` — receives passive feature vectors + skill gap signals
- `LEVEL_PROGRESSION_AGENT` — receives difficulty normalization signals (belt gate fairness)
- `OBSERVABILITY_AGENT` — monitors difficulty pipeline health + curriculum health metrics
- `XP_ENGINE_AGENT` — consumes difficulty tier (for DIFFICULTY_* multiplier application)

### Upstream Agents Feeding This Agent
- `MATCH_ENGINE_AGENT` — match completion + abandonment events
- `SCORING_ENGINE_AGENT` — validated scores per scenario per user
- `ANALYTICS_AGENT` — aggregated drill engagement, learning gain deltas
- `SCHOOL_OPS_AGENT` — assignment completion records, attendance patterns
- `XP_ENGINE_AGENT` — per-user skill XP per technology tag
- `LEVEL_PROGRESSION_AGENT` — current belt state per user (for pathway computation)
- `COLLUSION_DETECTION_AGENT` — flagged match IDs (excluded from calibration)
- `AUTH_SERVICE` — inactivity signals (last login timestamps)

---

## 3️⃣ INPUT CONTRACT (STRICT — LOCKED)

### INPUT_SCHEMA

```json
{
  "required_fields": {
    "event_id":             "UUID — globally unique (idempotency key)",
    "event_type":           "Enum — see EVENT_TYPE_REGISTRY below",
    "source_agent":         "Enum — agent emitting this event",
    "tenant_id":            "UUID — tenant isolation key",
    "domain":               "Enum: DOJO | GROWTH | SCHOOL_OPS",
    "domain_track":         "Enum: ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION",
    "timestamp_utc":        "ISO-8601 UTC timestamp",
    "policy_version":       "Semver — must match active DIFFICULTY_POLICY_VERSION"
  },
  "optional_fields": {
    "scenario_id":          "UUID — required for all DOJO scenario events",
    "assignment_id":        "UUID — required for SCHOOL_OPS assignment events",
    "challenge_id":         "UUID — required for GROWTH challenge events",
    "user_id":              "UUID — required for per-user pathway + regression events",
    "match_id":             "UUID — required for DOJO match-level events",
    "score_pct":            "Float 0.0–100.0 — user score on completion",
    "completion_time_sec":  "Integer — seconds to complete (0 if abandoned)",
    "outcome":              "Enum: COMPLETED | ABANDONED | TIMED_OUT | PARTIAL",
    "collusion_flagged":    "Boolean — if true, exclude from calibration pool",
    "current_belt_level":   "String — required for per-user pathway events",
    "skill_tag":            "String — required for GROWTH skill-level events",
    "skill_xp":             "Integer — required for GROWTH skill-level events",
    "inactivity_days":      "Integer — required for inactivity re-engagement triggers",
    "last_score_pct":       "Float — required for regression detection events",
    "rolling_avg_score_7d": "Float — required for regression detection events",
    "confidence_score":     "Float 0.0–1.0 — from SCORING_ENGINE"
  },
  "validation_rules": [
    "event_id must be globally unique — reject duplicate (idempotency)",
    "timestamp_utc must be within ±10 minute drift window of server UTC",
    "policy_version must exactly match active DIFFICULTY_POLICY_VERSION",
    "tenant_id in payload must match JWT claim — cross-tenant injection = SECURITY_FAILURE",
    "domain + domain_track must match scenario/assignment metadata in content store",
    "collusion_flagged = true → exclude from all calibration pools, still log for audit",
    "score_pct, if provided, must be Float 0.0–100.0 inclusive",
    "completion_time_sec, if provided, must be Integer ≥ 0",
    "outcome enum must be one of the defined values — unknown values = REJECT",
    "For SCENARIO_CALIBRATION events: scenario_id is required",
    "For USER_PATHWAY events: user_id + current_belt_level are required",
    "For REGRESSION_DETECTION events: user_id + last_score_pct + rolling_avg_score_7d are required",
    "For INACTIVITY events: user_id + inactivity_days are required"
  ],
  "security_checks": [
    "JWT bearer token validation on every request",
    "Tenant isolation: payload.tenant_id == JWT.tenant_id (hard check)",
    "Domain isolation: domain_track matches user's authorized domain in profile",
    "Role-based authorization: source_agent must be in PERMITTED_SOURCE_AGENTS for event_type",
    "Rate-limit: max 1,000 calibration events per scenario per hour (abuse prevention)",
    "Replay attack prevention: event_id idempotency key (Redis TTL 72h)"
  ],
  "domain_checks": [
    "DOJO events must reference scenario_ids existing in SCENARIO_CONTENT_STORE",
    "SCHOOL_OPS events must originate from institute tenant type — company tenant REJECT",
    "GROWTH events must reference challenge_ids or skill_tags existing in GROWTH_CONTENT_STORE",
    "Collusion-flagged match data must be excluded from calibration pool and logged separately",
    "Scenario/assignment records marked RETIRED must not receive new calibration events — REJECT and flag"
  ]
}
```

### Minimum Sample Threshold Policy (ANTI-NOISE GATE)

```
MINIMUM_SAMPLE_POLICY:
  No difficulty reclassification may execute with fewer than:
    DOJO scenarios:        MIN_SAMPLE = 50 unique completions (excluding collusion-flagged)
    GROWTH challenges:     MIN_SAMPLE = 30 unique completions
    SCHOOL_OPS assignments: MIN_SAMPLE = 20 unique submissions (per cohort)

  Below threshold → accumulate data, no reclassification, log INSUFFICIENT_SAMPLE
  Threshold breach → trigger CALIBRATION_EVALUATION pipeline
  Sample counts must exclude collusion-flagged matches — always
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT — LOCKED)

### OUTPUT_SCHEMA

```json
{
  "result_object": {
    "calibration_transaction_id":  "UUID — unique per calibration decision",
    "event_type":                  "Enum — from EVENT_TYPE_REGISTRY",
    "domain":                      "Enum: DOJO | GROWTH | SCHOOL_OPS",
    "domain_track":                "Enum",
    "tenant_id":                   "UUID",
    "subject_id":                  "UUID — scenario_id | assignment_id | challenge_id | user_id",
    "subject_type":                "Enum: SCENARIO | ASSIGNMENT | CHALLENGE | USER_PATHWAY",
    "action_taken":                "Enum: RECLASSIFIED | RETIRED | PATHWAY_ASSIGNED | REGRESSION_FLAGGED | CURRICULUM_FLAGGED | REENGAGEMENT_TRIGGERED | NORMALIZATION_UPDATED | NO_ACTION | INSUFFICIENT_SAMPLE",
    "previous_difficulty_tier":    "Enum | null",
    "new_difficulty_tier":         "Enum | null",
    "normalization_factor":        "Float | null — updated difficulty normalization for SCORING_ENGINE",
    "calibration_evidence": {
      "sample_count":              "Integer",
      "pass_rate_pct":             "Float",
      "avg_score_pct":             "Float",
      "avg_completion_time_sec":   "Float",
      "abandonment_rate_pct":      "Float",
      "score_std_deviation":       "Float",
      "failure_cluster_detected":  "Boolean",
      "p10_score":                 "Float",
      "p90_score":                 "Float"
    },
    "retirement_recommendation":   "Boolean",
    "retirement_reason":           "String | null",
    "curriculum_flag_emitted":     "Boolean",
    "pathway_assigned":            "Enum | null — new difficulty tier for user's next scenario",
    "regression_severity":         "Enum: NONE | MILD | MODERATE | SEVERE | null",
    "policy_version":              "Semver"
  },
  "confidence_score":    "Float 0.0–1.0 — engine confidence in calibration decision",
  "model_version":       "String — difficulty_adaptation_engine_v1.0.0",
  "audit_reference":     "UUID — links to immutable audit log entry",
  "next_trigger_events": [
    "DIFFICULTY_NORMALIZATION_UPDATE_EVENT",
    "SCENARIO_RETIREMENT_RECOMMENDATION_EVENT",
    "CURRICULUM_FLAG_EVENT",
    "USER_PATHWAY_UPDATE_EVENT",
    "REGRESSION_ALERT_EVENT",
    "REENGAGEMENT_TRIGGER_EVENT",
    "SKILL_GAP_SIGNAL_EVENT",
    "FEATURE_VECTOR_EMIT_EVENT"
  ]
}
```

### Output Rules
- `action_taken = NO_ACTION` when statistical evidence does not meet reclassification thresholds
- `action_taken = INSUFFICIENT_SAMPLE` when sample count below minimum — no further computation
- `normalization_factor` must be computed and emitted on every RECLASSIFIED action
- `retirement_recommendation = true` triggers a structured event to CURRICULUM_GOVERNANCE_AGENT — this agent does NOT execute retirement; CURRICULUM_GOVERNANCE_AGENT owns that decision
- All calibration evidence fields must be populated on RECLASSIFIED, RETIRED, and CURRICULUM_FLAGGED actions
- `pathway_assigned` is only populated for USER_PATHWAY subject_type
- Collusion-excluded data must NEVER appear in `calibration_evidence.sample_count`

---

## 5️⃣ DIFFICULTY TIER REGISTRY (LOCKED v1.0.0)

### DOJO Difficulty Tiers

```
TIER SEQUENCE (immutable label set):
  DOJO_EASY → DOJO_MEDIUM → DOJO_HARD → DOJO_PRESSURE

New tiers: require MAJOR version bump + backward compat documentation.
```

| Tier | XP Multiplier | Default Normalization Factor | Initial Author Classification | Runtime Classification Authority |
|---|---|---|---|---|
| `DOJO_EASY` | 0.8× | 0.80 | Allowed as initial seed only | DIFFICULTY_ADAPTATION_AGENT (data-derived) |
| `DOJO_MEDIUM` | 1.0× | 1.00 | Allowed as initial seed only | DIFFICULTY_ADAPTATION_AGENT (data-derived) |
| `DOJO_HARD` | 1.5× | 1.50 | Allowed as initial seed only | DIFFICULTY_ADAPTATION_AGENT (data-derived) |
| `DOJO_PRESSURE` | 2.0× | 2.00 | Allowed as initial seed only | DIFFICULTY_ADAPTATION_AGENT (data-derived) |

**Author classification is accepted ONLY as an initial seed value before minimum sample threshold is reached. Once minimum sample is achieved, the data-derived label permanently overrides the author label. Author labels cannot be restored after first data-driven classification.**

### GROWTH Difficulty Tiers

```
WEEKLY_CHALLENGE_EASY | WEEKLY_CHALLENGE_MEDIUM | WEEKLY_CHALLENGE_HARD
TIME_ATTACK_STANDARD | TIME_ATTACK_ADVANCED | TIME_ATTACK_ELITE
SKILL_GOAL_STARTER | SKILL_GOAL_INTERMEDIATE | SKILL_GOAL_ADVANCED
```

### SCHOOL OPS Difficulty Tiers

```
ASSIGNMENT_FOUNDATIONAL | ASSIGNMENT_STANDARD | ASSIGNMENT_CHALLENGING | ASSIGNMENT_ADVANCED
DRILL_BASIC | DRILL_INTERMEDIATE | DRILL_ADVANCED
COHORT_CHALLENGE_INTRODUCTORY | COHORT_CHALLENGE_STANDARD | COHORT_CHALLENGE_CAPSTONE
```

---

## 6️⃣ CALIBRATION ALGORITHM (LOCKED v1.0.0)

### CORE DIFFICULTY RECLASSIFICATION ALGORITHM

```
DIFFICULTY_RECLASSIFICATION_PIPELINE (v1.0.0):

TRIGGER: Scheduled batch (every 6 hours for DOJO, every 24 hours for GROWTH/SCHOOL_OPS)
         OR on-demand when scenario passes minimum sample threshold for first time.

FOR EACH subject (scenario | assignment | challenge):

  STEP 1 — DATA COLLECTION WINDOW
    Collect all completion/abandonment events for subject in rolling 30-day window.
    Exclude: collusion-flagged matches, duplicate event_ids, beta/test users.
    Compute: sample_count (post-exclusion)

  STEP 2 — MINIMUM SAMPLE GATE
    IF sample_count < MIN_SAMPLE_THRESHOLD[domain]:
      action_taken = INSUFFICIENT_SAMPLE
      LOG and EXIT — no reclassification

  STEP 3 — COMPUTE CALIBRATION SIGNALS

    pass_rate_pct      = (completed_with_score ≥ PASS_THRESHOLD) / sample_count × 100
    avg_score_pct      = MEAN(all scores in window, post-exclusion)
    avg_completion_time_sec = MEAN(completion_time_sec for COMPLETED outcomes)
    abandonment_rate_pct    = (ABANDONED + TIMED_OUT outcomes) / sample_count × 100
    score_std_deviation     = STD_DEV(all scores in window)
    p10_score               = 10th PERCENTILE of score distribution
    p90_score               = 90th PERCENTILE of score distribution
    failure_cluster_detected = TRUE if ≥ 30% of scores cluster in [0–40%] range

  STEP 4 — RECLASSIFICATION DECISION ENGINE

    Apply RECLASSIFICATION_RULES[domain][current_tier] (see Rule Tables below).
    Compute proposed_tier from rule evaluation.
    IF proposed_tier ≠ current_tier:
      action_taken = RECLASSIFIED
      previous_difficulty_tier = current_tier
      new_difficulty_tier = proposed_tier
      Compute new normalization_factor (see Normalization section)
    ELSE:
      action_taken = NO_ACTION
      Update normalization_factor if statistical drift detected (≥5% shift in avg_score)

  STEP 5 — RETIREMENT EVALUATION
    Apply RETIREMENT_RULES (see Retirement section).
    IF retirement criteria met:
      retirement_recommendation = TRUE
      retirement_reason = [applicable rule]
      EMIT SCENARIO_RETIREMENT_RECOMMENDATION_EVENT to CURRICULUM_GOVERNANCE_AGENT
      (Do NOT retire — CURRICULUM_GOVERNANCE_AGENT owns the retirement decision)

  STEP 6 — FAIRNESS AUDIT CHECK
    Apply FAIRNESS_AUDIT_RULES (see Fairness section).
    IF fairness flag triggered:
      curriculum_flag_emitted = TRUE
      EMIT CURRICULUM_FLAG_EVENT to CURRICULUM_GOVERNANCE_AGENT with evidence

  STEP 7 — WRITE CALIBRATION RECORD
    Write to DIFFICULTY_CALIBRATION_LEDGER (append-only)
    Update DIFFICULTY_CURRENT_STATE table (mutable snapshot)

  STEP 8 — EMIT DOWNSTREAM EVENTS
    EMIT DIFFICULTY_NORMALIZATION_UPDATE_EVENT to SCORING_ENGINE_AGENT
    EMIT DIFFICULTY_NORMALIZATION_UPDATE_EVENT to XP_ENGINE_AGENT
    If RECLASSIFIED: EMIT to CURRICULUM_GOVERNANCE_AGENT
    EMIT FEATURE_VECTOR_EMIT_EVENT to FEATURE_STORE_AGENT
    WRITE immutable AUDIT LOG record

  RETURN calibration output
```

---

### DOJO RECLASSIFICATION RULE TABLE (v1.0.0)

```
Rules evaluated in order. First matching rule wins.

── Scenarios with current tier = DOJO_EASY ──────────────────────────────────────────
RULE DE-1: IF pass_rate_pct ≥ 85 AND avg_score_pct ≥ 80 AND abandonment_rate_pct < 5
           → tier remains DOJO_EASY (well-calibrated easy)
RULE DE-2: IF pass_rate_pct ≥ 90 AND avg_completion_time_sec < (BENCHMARK_EASY × 0.5)
           → RECLASSIFY to TRIVIAL_FLAG (emit curriculum flag — scenario too easy,
             candidate for content enrichment — NOT a valid tier, triggers curriculum review)
RULE DE-3: IF pass_rate_pct < 60 OR abandonment_rate_pct > 20
           → RECLASSIFY to DOJO_MEDIUM

── Scenarios with current tier = DOJO_MEDIUM ─────────────────────────────────────────
RULE DM-1: IF pass_rate_pct ≥ 70 AND avg_score_pct BETWEEN 60–80 AND abandonment_rate_pct < 15
           → tier remains DOJO_MEDIUM (well-calibrated medium)
RULE DM-2: IF pass_rate_pct ≥ 85 AND avg_score_pct ≥ 80 AND abandonment_rate_pct < 8
           → RECLASSIFY to DOJO_EASY
RULE DM-3: IF pass_rate_pct < 45 OR abandonment_rate_pct > 30 OR failure_cluster_detected = TRUE
           → RECLASSIFY to DOJO_HARD
RULE DM-4: IF score_std_deviation > 25 (high variance — inconsistent difficulty)
           → curriculum_flag_emitted = TRUE (fairness concern, inconsistent scenario)

── Scenarios with current tier = DOJO_HARD ──────────────────────────────────────────
RULE DH-1: IF pass_rate_pct BETWEEN 30–65 AND avg_score_pct BETWEEN 55–75
           → tier remains DOJO_HARD (well-calibrated hard)
RULE DH-2: IF pass_rate_pct ≥ 70 AND avg_score_pct ≥ 75 AND abandonment_rate_pct < 20
           → RECLASSIFY to DOJO_MEDIUM
RULE DH-3: IF pass_rate_pct < 20 OR abandonment_rate_pct > 50 OR failure_cluster_detected = TRUE
           → RECLASSIFY to DOJO_PRESSURE
RULE DH-4: IF p90_score - p10_score > 50 (extreme score spread)
           → curriculum_flag_emitted = TRUE (scenario fairness concern)

── Scenarios with current tier = DOJO_PRESSURE ──────────────────────────────────────
RULE DP-1: IF pass_rate_pct BETWEEN 10–35 AND avg_score_pct BETWEEN 40–65
           → tier remains DOJO_PRESSURE (well-calibrated pressure)
RULE DP-2: IF pass_rate_pct ≥ 40 AND avg_score_pct ≥ 70
           → RECLASSIFY to DOJO_HARD
RULE DP-3: IF pass_rate_pct < 5 AND abandonment_rate_pct > 70
           → retirement_recommendation = TRUE (reason: UNATTEMPTABLE — prevents all learning)
RULE DP-4: IF sample_count ≥ 100 AND avg_score_pct < 20 AND abandonment_rate_pct > 60
           → retirement_recommendation = TRUE (reason: EXCESSIVE_DIFFICULTY_DISTRESS)
```

### PASS_THRESHOLD by Domain

```
DOJO:       score_pct ≥ 60% = PASS (aligned to White Belt gate minimum)
GROWTH:     score_pct ≥ 70% = PASS
SCHOOL_OPS: score_pct ≥ 50% = PASS (configurable per institute, default 50%)
```

---

## 7️⃣ DIFFICULTY NORMALIZATION FACTOR (LOCKED)

The normalization factor is the per-scenario difficulty weight emitted to SCORING_ENGINE_AGENT and XP_ENGINE_AGENT. It ensures that a user who solves a statistically harder scenario receives proportionally fair XP credit.

### Normalization Formula (v1.0.0)

```
normalization_factor = BASE_FACTOR[current_tier] × ADJUSTMENT_COEFFICIENT

Where:
  BASE_FACTOR = { DOJO_EASY: 0.80, DOJO_MEDIUM: 1.00, DOJO_HARD: 1.50, DOJO_PRESSURE: 2.00 }

  ADJUSTMENT_COEFFICIENT is computed from:
    raw_adj = 1.0
    IF pass_rate_pct < 30:   raw_adj += 0.15
    IF pass_rate_pct < 15:   raw_adj += 0.10  (additive — very hard earns extra)
    IF pass_rate_pct > 80:   raw_adj -= 0.10  (easier than tier implies)
    IF abandonment_rate_pct > 40: raw_adj += 0.05  (distress signal = harder)
    IF avg_completion_time_sec > BENCHMARK_TIER × 1.5: raw_adj += 0.05
    ADJUSTMENT_COEFFICIENT = CLAMP(raw_adj, 0.70, 2.50)  (hard floor + ceiling)

  FINAL normalization_factor = ROUND(BASE_FACTOR × ADJUSTMENT_COEFFICIENT, 2)

CLAMP bounds prevent normalization inflation attacks or runaway deflation.
normalization_factor is updated on every reclassification AND when avg_score drift ≥ 5%.
normalization_factor is versioned per calibration_transaction_id.
SCORING_ENGINE_AGENT always reads normalization_factor from DIFFICULTY_CURRENT_STATE.
```

### Benchmark Completion Times by Tier (v1.0.0 defaults)

```
DOJO_EASY:      BENCHMARK = 300 seconds  (5 minutes)
DOJO_MEDIUM:    BENCHMARK = 600 seconds  (10 minutes)
DOJO_HARD:      BENCHMARK = 900 seconds  (15 minutes)
DOJO_PRESSURE:  BENCHMARK = 1200 seconds (20 minutes)

Benchmarks are data-updated quarterly from median completion times per tier.
Benchmark changes require minor version bump.
```

---

## 8️⃣ SCENARIO RETIREMENT RULES (LOCKED v1.0.0)

Retirement is a **recommendation only** from this agent. The CURRICULUM_GOVERNANCE_AGENT executes the actual retirement decision after human review. No scenario is retired without a formal audit record and human confirmation.

### Retirement Triggers

| Rule ID | Condition | Retirement Reason Code |
|---|---|---|
| `RET-01` | pass_rate_pct < 5 AND abandonment_rate_pct > 70 (DOJO_PRESSURE) | `UNATTEMPTABLE` |
| `RET-02` | avg_score_pct < 20 AND abandonment_rate_pct > 60 AND sample_count ≥ 100 | `EXCESSIVE_DIFFICULTY_DISTRESS` |
| `RET-03` | pass_rate_pct > 95 AND avg_completion_time_sec < (BENCHMARK_EASY × 0.3) AND sample_count ≥ 200 | `TRIVIALLY_EASY_DEGENERATE` |
| `RET-04` | sample_count < 10 in last 180 days (stagnant, no engagement) | `ENGAGEMENT_STAGNATION` |
| `RET-05` | score_std_deviation > 40 (extreme inconsistency) AND sample_count ≥ 100 | `PSYCHOMETRIC_INVALIDITY` |
| `RET-06` | failure_cluster_detected AND p10_score < 10 AND p90_score > 90 (bimodal split) | `BIMODAL_VALIDITY_FAILURE` |
| `RET-07` | CURRICULUM_GOVERNANCE_AGENT manually flags for retirement via admin review | `ADMIN_GOVERNANCE_RETIREMENT` |

### Retirement Workflow

```
DIFFICULTY_ADAPTATION_AGENT     → Emits SCENARIO_RETIREMENT_RECOMMENDATION_EVENT
CURRICULUM_GOVERNANCE_AGENT     → Human review + approval workflow
                                → If approved: executes SCENARIO_RETIRED state change
                                → Writes retirement audit record (immutable)
                                → Notifies content team
                                → Triggers replacement scenario commission workflow

This agent NEVER directly retires a scenario.
Retirement_recommendation = TRUE is a signal, not an execution.
```

---

## 9️⃣ PER-USER ADAPTIVE DIFFICULTY PATHWAY (LOCKED v1.0.0)

The DIFFICULTY_ADAPTATION_AGENT computes an **individual difficulty pathway** for each user — the recommended difficulty tier for their next scenario within a domain and domain_track. This is consumed by MATCH_ENGINE_AGENT when selecting scenarios for practice sessions and challenges.

### USER PATHWAY ALGORITHM

```
PATHWAY_COMPUTATION (v1.0.0):

TRIGGER: On every match completion event for the user (DOJO) OR weekly for GROWTH/SCHOOL_OPS.

INPUTS:
  - user.current_belt_level (from LEVEL_PROGRESSION_AGENT)
  - user.rolling_avg_score_7d (last 7 days, domain-specific)
  - user.rolling_avg_score_30d (last 30 days)
  - user.last_10_match_outcomes (W/L/A = win/loss/abandoned)
  - user.current_difficulty_pathway (current assigned tier)
  - user.consecutive_wins_at_tier (count)
  - user.consecutive_losses_at_tier (count)
  - user.abandonment_count_7d (last 7 days)

STEP 1 — PERFORMANCE TREND CLASSIFICATION

  trend = CLASSIFY(rolling_avg_score_7d, rolling_avg_score_30d, last_10_match_outcomes):
    ACCELERATING:  7d_avg > 30d_avg BY ≥10 AND ≥7/10 last matches WON
    STEADY:        |7d_avg - 30d_avg| < 10 AND abandon_count_7d < 3
    STRUGGLING:    7d_avg < 30d_avg BY ≥10 OR ≥6/10 last matches LOST OR abandon_count_7d ≥ 3
    REGRESSING:    7d_avg < 30d_avg BY ≥20 AND ≥7/10 last matches LOST (severe)

STEP 2 — PATHWAY DECISION TABLE

  IF trend = ACCELERATING AND consecutive_wins_at_tier ≥ 5:
    → pathway_assigned = ONE TIER UP (increment difficulty)
    → emit PATHWAY_INCREASE_EVENT

  IF trend = STEADY:
    → pathway_assigned = CURRENT TIER (no change)
    → no event emitted

  IF trend = STRUGGLING AND consecutive_losses_at_tier ≥ 5:
    → pathway_assigned = ONE TIER DOWN (reduce difficulty to rebuild confidence)
    → emit PATHWAY_DECREASE_EVENT
    → emit REGRESSION_ALERT_EVENT (severity = MILD or MODERATE based on magnitude)

  IF trend = REGRESSING:
    → pathway_assigned = ONE TIER DOWN (mandatory)
    → emit PATHWAY_DECREASE_EVENT
    → emit REGRESSION_ALERT_EVENT (severity = SEVERE)
    → emit SKILL_GAP_SIGNAL_EVENT to SKILL_GAP_ANALYSIS_AGENT

STEP 3 — BELT-TIER FLOOR CONSTRAINT
  Pathway cannot drop below the MINIMUM_TIER_FOR_BELT[user.current_belt_level]:
    BELT_NONE / BELT_WHITE:  floor = DOJO_EASY
    BELT_YELLOW:             floor = DOJO_EASY (can still practice easy for warmup)
    BELT_GREEN:              floor = DOJO_MEDIUM
    BELT_BLUE:               floor = DOJO_HARD
    BELT_BLACK:              floor = DOJO_HARD

  Pathway cannot exceed the MAXIMUM_TIER_FOR_BELT:
    BELT_NONE:    ceiling = DOJO_EASY
    BELT_WHITE:   ceiling = DOJO_MEDIUM
    BELT_YELLOW:  ceiling = DOJO_HARD
    BELT_GREEN+:  ceiling = DOJO_PRESSURE (no ceiling above Green)

STEP 4 — DIFFICULTY UNLOCK GATE (from SECTION 24 mechanics)
  New users: Only DOJO_EASY available until 25 EASY completions
  After 25 EASY completions:   DOJO_MEDIUM unlocked
  After 50 MEDIUM completions: DOJO_HARD unlocked
  Unlock state tracked in DIFFICULTY_UNLOCK_STORE per user

  Pathway assignment must respect unlock state — cannot assign tier user has not unlocked.

STEP 5 — WRITE PATHWAY RECORD + EMIT
  Write to USER_PATHWAY_STORE (latest pathway per user, per domain, per domain_track)
  Emit USER_PATHWAY_UPDATE_EVENT to MATCH_ENGINE_AGENT
  Emit FEATURE_VECTOR to FEATURE_STORE_AGENT
  Write AUDIT record
```

---

## 🔟 LEARNING REGRESSION & SKILL GAP DETECTION (LOCKED v1.0.0)

### Regression Detection Algorithm

```
REGRESSION_DETECTION_PIPELINE:
  Runs weekly per user, per domain, per skill_tag (where applicable).

  INPUTS: rolling_avg_score_7d, rolling_avg_score_30d, rolling_avg_score_90d

  REGRESSION_SEVERITY:
    NONE:     |7d_avg - 30d_avg| < 5 (within normal variance)
    MILD:     7d_avg < 30d_avg BY 5–14 (slight dip)
    MODERATE: 7d_avg < 30d_avg BY 15–24 (performance alert)
    SEVERE:   7d_avg < 30d_avg BY ≥25 OR 7d_avg < 90d_avg BY ≥30

  On MODERATE or SEVERE:
    → EMIT REGRESSION_ALERT_EVENT to NOTIFICATION_ENGINE_AGENT
      with: user_id, severity, affected_domain, affected_skill_tag (if GROWTH),
            recommended_scenarios (top 3 by skill_gap signal),
            mentor_session_flag (if SEVERE + Green Belt+)
    → EMIT SKILL_GAP_SIGNAL_EVENT to SKILL_GAP_ANALYSIS_AGENT
    → EMIT FEATURE_VECTOR to FEATURE_STORE_AGENT

  Aligns with System §18: "Performance Variance Alerts — if current avg < historical avg
  by >15%, notify user and recommend specific weak scenarios."
```

### Skill Gap Signal Schema

```json
{
  "skill_gap_signal_id":    "UUID",
  "user_id":                "UUID",
  "tenant_id":              "UUID",
  "domain":                 "Enum",
  "domain_track":           "Enum",
  "skill_tag":              "String | null",
  "gap_severity":           "Enum: MILD | MODERATE | SEVERE",
  "current_avg_score_pct":  "Float",
  "baseline_avg_score_pct": "Float",
  "delta_pct":              "Float (negative = regression)",
  "recommended_scenario_ids": ["UUID", "UUID", "UUID"],
  "recommendation_basis":   "String — which calibration rule generated recommendations",
  "timestamp_utc":          "ISO-8601",
  "source_agent":           "DIFFICULTY_ADAPTATION_AGENT"
}
```

---

## 1️⃣1️⃣ CURRICULUM HEALTH FLAGS (LOCKED v1.0.0)

When a scenario or content item exhibits statistical patterns that indicate curriculum quality problems, DIFFICULTY_ADAPTATION_AGENT emits a structured `CURRICULUM_FLAG_EVENT` to CURRICULUM_GOVERNANCE_AGENT.

### Curriculum Flag Trigger Table

| Flag Code | Condition | Meaning |
|---|---|---|
| `CF-TRIVIAL` | pass_rate ≥ 95 AND avg_time < 30% of tier benchmark | Scenario too easy — content enrichment needed |
| `CF-INCONSISTENT` | score_std_deviation > 25 for MEDIUM or HARD tier | Scenario inconsistently hard — rubric or scenario design issue |
| `CF-BIMODAL` | p90 − p10 > 50 AND failure_cluster_detected | Bimodal pass/fail — scenario discriminates on external factor (not skill) |
| `CF-STAGNANT` | sample_count < 10 in 180 days | Low engagement — scenario may be irrelevant or poor quality |
| `CF-DISTRESS` | abandonment > 40% AND avg_score < 30 (any tier) | Scenario causing user distress — safety concern (links to T10) |
| `CF-LEARNING_GAP` | Skill track without measurable improvement signal over 90 days | Curriculum not driving learning gain (T6 mandate) |
| `CF-CULTURAL_BIAS` | Score variance > 2σ between cohorts of different domain_track | Potential cultural/domain bias in scenario content (T12 mandate) |
| `CF-NO_CONSTRUCT` | Scenario lacks a mapped construct definition in SKILL_VALIDITY_STORE | T1 compliance violation — scenario cannot contribute to belt eligibility |

---

## 1️⃣2️⃣ SCHOOL OPS DOMAIN — SPECIFIC ADAPTATIONS (LOCKED v1.0.0)

### Assignment Difficulty Calibration

```
SCHOOL_OPS calibration follows same pipeline structure but with:
  - MIN_SAMPLE_THRESHOLD = 20 (per cohort, not platform-wide)
  - Calibration window = 60 days (slower cycle, academic pace)
  - PASS_THRESHOLD = 50% (institute-configurable, default 50%)
  - Retirement trigger: assignment not attempted by ≥ 80% of cohort in 30 days
    → retirement_reason = COHORT_DISENGAGEMENT

SCHOOL_OPS inactivity re-engagement (R73 mandate):
  Inactivity threshold by user segment:
    School Grade 6–8:     7 days inactivity → MILD_REENGAGEMENT_TRIGGER
    School Grade 9–12:   10 days inactivity → MODERATE_REENGAGEMENT_TRIGGER
    Post-secondary:      14 days inactivity → MODERATE_REENGAGEMENT_TRIGGER
    Working professional: 21 days inactivity → SEVERE_REENGAGEMENT_TRIGGER

  On threshold breach:
    → EMIT REENGAGEMENT_TRIGGER_EVENT to NOTIFICATION_ENGINE_AGENT
    → Include: top 3 scenarios/assignments at user's current difficulty pathway
    → Flag skill_obsolete_detected if inactivity > 60 days AND skill_last_used_at > 60d
    → EMIT skill_obsolete_detected event to SKILL_GAP_ANALYSIS_AGENT

Institute Scope Isolation:
  School Ops calibration is ALWAYS scoped to institution_id + cohort_id.
  Platform-level calibration does NOT override institution-specific calibration.
  Institution admins can view but NOT modify calibration data.
  No cross-institution calibration sharing without explicit data-sharing agreement.
```

### School Ops Drill Calibration

```
Drill effectiveness is measured per drill_id per cohort:
  drill_completion_rate  = completions / assigned × 100
  drill_avg_score        = MEAN(scores)
  drill_improvement_delta = post_drill_avg_score - pre_drill_avg_score

  If drill_improvement_delta < 5 over 3 cycles:
    → CF-LEARNING_GAP flag emitted to CURRICULUM_GOVERNANCE_AGENT
    → Drill flagged for content review
    → Recommendation: replace or redesign drill

  Drill effectiveness signals are emitted to FEATURE_STORE_AGENT as:
    feature: drill_effectiveness_score
    value: drill_improvement_delta (float)
```

---

## 1️⃣3️⃣ GROWTH ENGINE DOMAIN — SPECIFIC ADAPTATIONS (LOCKED v1.0.0)

### Weekly Challenge Calibration

```
GROWTH weekly challenge calibration:
  MIN_SAMPLE_THRESHOLD = 30 completions
  Calibration window = 14 days (faster cycle, weekly cadence)
  Pass threshold = 70%
  Challenge difficulty updated before each new weekly cycle

  Plateau detection per user:
    IF user completes same-difficulty challenge 3+ consecutive weeks
    AND avg_score_7d ≥ 80%:
      → pathway_assigned = difficulty UP for next challenge
      → emit USER_PATHWAY_UPDATE_EVENT

  Technology skill-tag plateau detection:
    IF skill_xp increment < 50 XP per week for 3 consecutive weeks
    AND user is at SKILL_LEVEL_ADVANCED:
      → emit CF-LEARNING_GAP flag
      → recommend advanced-tier scenarios for that skill_tag
      → emit SKILL_GAP_SIGNAL_EVENT
```

### Time Attack Calibration

```
Time Attack benchmark times are calibrated separately from standard mode.
  Calibration uses only TIME_ATTACK outcomes (mode-isolated).
  
  TIME_ATTACK_STANDARD benchmark: median completion time for top 50th percentile
  TIME_ATTACK_ADVANCED benchmark: median for top 25th percentile
  TIME_ATTACK_ELITE benchmark:    median for top 10th percentile

  User assigned to TIME_ATTACK tier based on:
    Their completion_time_sec relative to tier benchmarks in last 5 attempts
    Below TIME_ATTACK_STANDARD benchmark → upgrade tier
    Cannot complete TIME_ATTACK_STANDARD → downgrade tier
```

---

## 1️⃣4️⃣ ML / AI LOGIC LAYER

### ML Usage (Primary Decision Engine for Calibration)

```yaml
MODEL_TYPE:        Regression + Classification + Time-Series + Anomaly Detection

MODELS:
  1. DIFFICULTY_CLASSIFIER (Regression + Classification)
     Purpose: Predicts optimal difficulty tier from statistical evidence
     Training: Supervised on historical calibration outcomes (labeled by curriculum experts)
     Features: pass_rate_pct, avg_score_pct, score_std_deviation, abandonment_rate_pct,
               avg_completion_time_sec, failure_cluster, p10_score, p90_score,
               sample_count, domain_track_encoded, scenario_type_encoded
     Output: Proposed tier label + confidence score
     Note: ML model SUGGESTS — rule engine in Section 6 is authoritative
     ML confidence < 0.70 → rule engine output takes precedence

  2. USER_PATHWAY_PREDICTOR (Time-Series + Classification)
     Purpose: Predicts optimal next-difficulty for user given performance trend
     Training: Historical pathway assignment outcomes (did user improve after assignment?)
     Features: rolling_avg_score_7d, rolling_avg_score_30d, current_belt_encoded,
               consecutive_wins, consecutive_losses, abandon_count_7d,
               last_10_match_outcome_vector, time_since_last_match_hours
     Output: Pathway trend classification (ACCELERATING | STEADY | STRUGGLING | REGRESSING)

  3. REGRESSION_DETECTOR (Anomaly Detection + Time-Series)
     Purpose: Early detection of performance degradation before severity is obvious
     Training: Time-series user performance sequences, labeled regression events
     Features: score_time_series_30d, outcome_sequence, abandon_frequency_trend,
               completion_time_trend, skill_tag_score_deltas

  4. CURRICULUM_HEALTH_PREDICTOR (Classification)
     Purpose: Predicts which scenarios are approaching flag conditions before threshold is hit
     Training: Historical curriculum flag outcomes + scenario lifecycle data
     Output: Proactive early-warning signal (advisory only, does not trigger flags)

TRAINING_FREQUENCY:   Monthly (all models)
DRIFT_DETECTION:
  - Monitor distribution shift in calibration outcomes vs rule-engine baseline
  - Monitor pathway predictor accuracy (did user improve after assignment? feedback loop)
  - Monitor regression detector recall (missing regressions = dangerous drift)
  - Monitor inter-cohort calibration variance (bias detection)
  - Trigger GOVERNANCE_AGENT alert if any model drifts > 2 sigma from baseline

VERSION_CONTROL:
  - model_version stored in every audit record and calibration output
  - Immutable model snapshot per version tag
  - Rollback to previous model_version if drift alarm fires
  - Retrain triggers log in MODEL_RETRAIN_AUDIT_LOG (append-only)
```

### AI Usage (Strictly Advisory — No Decisional Authority)

```yaml
AI_USAGE_SCOPE:
  - Permitted: Natural language description of calibration change for curriculum admin dashboard
  - Permitted: Summarizing difficulty shift evidence into human-readable report for
               CURRICULUM_GOVERNANCE_AGENT review workflows
  - Permitted: Generating recommended learning path narrative for re-engagement notification
  - FORBIDDEN: AI may never make a difficulty reclassification decision
  - FORBIDDEN: AI may never trigger or approve scenario retirement
  - FORBIDDEN: AI may never compute normalization factors
  - FORBIDDEN: Creative interpretation of calibration rules

PROMPT_GOVERNANCE:
  - Versioned prompt templates only (stored in PROMPT_REGISTRY, immutable)
  - Deterministic structured prompts — no open-ended generation in decision paths
  - AI outputs are flagged as ADVISORY_NARRATIVE in audit records
  - AI called post-decision only — never in the decision pipeline

AI_ASSISTS_ML_NOT_REPLACES_IT = ENFORCED
```

---

## 1️⃣5️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS:         3,000 (peak — end of tournament wave, simultaneous match completions)
LATENCY_TARGET:       Per-event ingestion P99 < 100ms
                      Per-user pathway computation P99 < 200ms
                      Batch calibration cycle: complete within 30 minutes for all scenarios
MAX_CONCURRENCY:      60,000 simultaneous event ingest connections
QUEUE_STRATEGY:       Kafka topics:
                        difficulty.dojo.events
                        difficulty.growth.events
                        difficulty.school_ops.events
                        difficulty.calibration.scheduled   (batch trigger topic)
                      Consumer groups per domain
                      DLQ per topic with alerting on depth > 500
                      Idempotency via event_id dedup key (Redis, TTL 72h)

HORIZONTAL_SCALING:   Stateless event ingest layer — deploy N replicas
STATELESS:            All persistent state in PostgreSQL + ClickHouse (historical)
                      Redis for hot caches (current pathway per user, normalization factors)
EVENT_DRIVEN:         Consumed from Kafka, all downstream triggers emitted to Kafka
ASYNC_PROCESSING:     All batch calibration runs are async (Airflow-triggered)
                      Per-user pathway updates are real-time (sub-200ms)
IDEMPOTENT:           event_id is idempotency key — duplicate events do not double-count
                      in calibration pools

BATCH_SCHEDULE:
  DOJO calibration cycle:       Every 6 hours (Airflow)
  GROWTH calibration cycle:     Every 24 hours (Airflow, pre-weekly challenge reset)
  SCHOOL_OPS calibration cycle: Every 24 hours (Airflow)
  Regression detection run:     Weekly per user (Airflow)
  Inactivity detection run:     Daily (Airflow, per domain segment thresholds)
```

---

## 1️⃣6️⃣ SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  - Every calibration query filters by tenant_id at query layer
  - No cross-tenant scenario performance data sharing
  - No cross-institution calibration data sharing (SCHOOL_OPS)
  - Row-level security enforced at PostgreSQL layer

DOMAIN_ISOLATION:
  - domain_track verified against scenario/assignment metadata
  - SCHOOL_OPS calibration data never accessible by Company tenant type
  - Cross-domain scenario comparison FORBIDDEN

ROLE_BASED_AUTHORIZATION:
  - Only AUTOMATION agents (from permitted source list) may emit calibration events
  - Curriculum admin may VIEW calibration data — not modify
  - GOVERNANCE_AGENT may escalate retirement decisions — not this agent's scope
  - No student or trainer direct access to calibration internals

CALIBRATION_DATA_PROTECTION:
  - Calibration records are read-only after write (append-only ledger)
  - Normalization factors versioned — old values retained, never overwritten
  - Difficulty label history fully reconstructible from ledger

ENCRYPTION:
  - Calibration data encrypted at rest (AES-256)
  - User performance data referenced by UUID only — no PII in calibration records
  - Transit encryption: TLS 1.3 minimum

AUDIT_LOGGING:
  - Every calibration decision + every pathway assignment logged immutably
  - Schema constraint: no DELETE or UPDATE on audit tables
  - Audit failure halts calibration write — consistency over availability
```

---

## 1️⃣7️⃣ AUDIT & TRACEABILITY

Every calibration evaluation emits this immutable audit record:

```json
{
  "audit_id":                       "UUID",
  "calibration_transaction_id":     "UUID",
  "timestamp_utc":                  "ISO-8601",
  "actor_id":                       "UUID (service_account_id)",
  "tenant_id":                      "UUID",
  "institution_id":                 "UUID | null",
  "subject_id":                     "UUID — scenario | assignment | challenge | user",
  "subject_type":                   "Enum",
  "domain":                         "Enum",
  "domain_track":                   "Enum",
  "input_hash":                     "SHA-256 of canonicalized calibration input",
  "output_hash":                    "SHA-256 of canonicalized calibration output",
  "model_version":                  "difficulty_adaptation_engine_v1.0.0",
  "policy_version":                 "v1.0.0",
  "action_taken":                   "Enum",
  "previous_difficulty_tier":       "Enum | null",
  "new_difficulty_tier":            "Enum | null",
  "normalization_factor_previous":  "Float | null",
  "normalization_factor_new":       "Float | null",
  "calibration_evidence_snapshot":  "JSONB — full evidence at time of decision",
  "rule_matched":                   "String — rule ID that triggered decision",
  "ml_model_suggestion":            "Enum | null — ML model's suggested tier",
  "ml_confidence":                  "Float | null",
  "rule_engine_overrode_ml":        "Boolean",
  "retirement_recommended":         "Boolean",
  "curriculum_flag_code":           "String | null",
  "anomaly_flags":                  "Array | []",
  "collusion_excluded_count":       "Integer — how many events excluded from pool",
  "downstream_events_emitted":      "Array of event_id strings",
  "decision_path":                  "Array of step IDs executed"
}
```

**Audit Tables:** `difficulty_calibration_audit_log` + `user_pathway_audit_log` — append-only. No DELETE or UPDATE. Verified by GOVERNANCE_AGENT quarterly.

---

## 1️⃣8️⃣ FAILURE POLICY

| Failure Condition | Behavior |
|---|---|
| **Invalid / missing required field** | REJECT → 400 with field error · Log to audit (action_taken = REJECTED) |
| **Duplicate event_id** | REJECT idempotently → 200 with action_taken = ALREADY_PROCESSED · Log replay attempt |
| **Below minimum sample threshold** | action_taken = INSUFFICIENT_SAMPLE · No reclassification · Log · Accumulate |
| **DIFFICULTY_POLICY_STORE unavailable** | STOP_EXECUTION · LOG_INCIDENT · ESCALATE to GOVERNANCE_AGENT + OPS_TEAM · RETRY 3× (1s, 2s, 4s) → DLQ |
| **SCORING_ENGINE_AGENT normalization update fails** | Retain previous normalization_factor · Log incident · RETRY 3× · Alert OBSERVABILITY_AGENT |
| **Calibration DB write failure** | STOP_EXECUTION · Do NOT emit downstream events · LOG_INCIDENT · RETRY 3× with circuit breaker |
| **Kafka emit failure** | Retry 3× → DLQ · Alert OBSERVABILITY_AGENT · Calibration record still written |
| **ML model unavailable** | Fall back to rule-engine only · Log ML_FALLBACK · Continue · Alert OBSERVABILITY_AGENT |
| **Collusion flag detected on event** | Exclude from calibration pool · Log COLLUSION_EXCLUDED · Continue pipeline |
| **Tenant isolation violation** | STOP_EXECUTION immediately · LOG_SECURITY_INCIDENT (CRITICAL) · ESCALATE to SECURITY_AGENT + GOVERNANCE_AGENT |
| **Scenario already RETIRED receives event** | REJECT event · Log RETIRED_SCENARIO_EVENT · Alert CURRICULUM_GOVERNANCE_AGENT |

**No silent failures permitted under any condition.**

---

## 1️⃣9️⃣ INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - MATCH_ENGINE_AGENT           # Match completion + abandonment events
  - SCORING_ENGINE_AGENT         # Validated scores per match per scenario
  - ANALYTICS_AGENT              # Drill engagement, learning gain aggregates
  - SCHOOL_OPS_AGENT             # Assignment completion events
  - XP_ENGINE_AGENT              # Skill tag XP delta events
  - LEVEL_PROGRESSION_AGENT      # User's current belt (for pathway floor/ceiling)
  - COLLUSION_DETECTION_AGENT    # Flagged match IDs (excluded from calibration pool)
  - AUTH_SERVICE                 # Inactivity signals (last login)

DOWNSTREAM_AGENTS:
  - SCORING_ENGINE_AGENT         # Receives normalization_factor updates per scenario
  - XP_ENGINE_AGENT              # Receives difficulty tier updates (for multiplier)
  - MATCH_ENGINE_AGENT           # Receives user pathway assignments (scenario selection)
  - CURRICULUM_GOVERNANCE_AGENT  # Receives retirement recommendations + curriculum flags
  - SKILL_GAP_ANALYSIS_AGENT     # Receives skill gap signals per user per skill_tag
  - NOTIFICATION_ENGINE_AGENT    # Receives regression alerts + inactivity triggers
  - FEATURE_STORE_AGENT          # Receives passive feature vectors
  - OBSERVABILITY_AGENT          # Monitors calibration pipeline health

EVENT_TRIGGERS_EMITTED:

  DIFFICULTY_NORMALIZATION_UPDATE_EVENT:
    topic:   difficulty.normalization.update
    payload: { scenario_id, tenant_id, domain, normalization_factor, tier,
               calibration_transaction_id, policy_version, timestamp_utc }
    condition: RECLASSIFIED or normalization drift ≥ 5%

  SCENARIO_RETIREMENT_RECOMMENDATION_EVENT:
    topic:   curriculum.retirement.recommendation
    payload: { scenario_id, tenant_id, domain, retirement_reason, calibration_evidence,
               calibration_transaction_id, timestamp_utc }
    condition: retirement_recommendation = TRUE

  CURRICULUM_FLAG_EVENT:
    topic:   curriculum.flag
    payload: { subject_id, subject_type, tenant_id, domain, flag_code, evidence,
               calibration_transaction_id, timestamp_utc }
    condition: curriculum_flag_emitted = TRUE

  USER_PATHWAY_UPDATE_EVENT:
    topic:   difficulty.pathway.update
    payload: { user_id, tenant_id, domain, domain_track, new_pathway_tier,
               previous_pathway_tier, trend_classification, calibration_transaction_id, timestamp_utc }
    condition: pathway_assigned != previous

  REGRESSION_ALERT_EVENT:
    topic:   learning.regression.alert
    payload: { user_id, tenant_id, domain, severity, delta_score, recommended_scenario_ids,
               mentor_session_flag, timestamp_utc }
    condition: regression_severity in [MODERATE, SEVERE]

  REENGAGEMENT_TRIGGER_EVENT:
    topic:   engagement.reengagement.trigger
    payload: { user_id, tenant_id, domain, inactivity_days, trigger_level,
               recommended_scenario_ids, skill_obsolete_detected, timestamp_utc }
    condition: inactivity threshold breached

  SKILL_GAP_SIGNAL_EVENT:
    topic:   skill.gap.signal
    payload: { skill_gap_signal schema — see Section 10 }
    condition: regression_severity = SEVERE or skill_obsolete_detected

  FEATURE_VECTOR_EMIT_EVENT:
    topic:   feature_store.ingest
    payload:
      user_id:              UUID (for USER_PATHWAY events) | null (for scenario calibration)
      feature_name:         "difficulty_calibration_signal"
      feature_value:        { domain, action_taken, tier_change_direction, normalization_factor,
                              regression_severity, pathway_tier }
      timestamp:            ISO-8601
      source_agent:         "DIFFICULTY_ADAPTATION_AGENT"
```

---

## 2️⃣0️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

```yaml
FEATURE_VECTORS_EMITTED:

  Per scenario calibration:
    - difficulty_tier_encoded:          integer (EASY=1, MEDIUM=2, HARD=3, PRESSURE=4)
    - normalization_factor:             float
    - pass_rate_pct:                    float
    - abandonment_rate_pct:             float
    - avg_score_pct:                    float
    - score_std_deviation:              float
    - failure_cluster_detected:         boolean (0/1)
    - curriculum_flag_active:           boolean (0/1)
    - retirement_recommended:           boolean (0/1)

  Per user pathway:
    - current_pathway_tier_encoded:     integer
    - trend_classification_encoded:     integer (ACCELERATING=4, STEADY=3, STRUGGLING=2, REGRESSING=1)
    - consecutive_wins_at_tier:         integer
    - consecutive_losses_at_tier:       integer
    - regression_severity_encoded:      integer (NONE=0, MILD=1, MODERATE=2, SEVERE=3)
    - days_since_difficulty_change:     integer

COMPATIBILITY:
  FEATURE_STORE_AGENT:       ✅ (primary passive consumer)
  SKILL_GAP_ANALYSIS_AGENT:  ✅ (gap signal consumer)
  IDEA_DNA_AGENT:            N/A
  ROYALTY_ENGINE:            N/A
  GROWTH_ENGINE:             ✅ (re-engagement signal consumer)
```

---

## 2️⃣1️⃣ PERFORMANCE MONITORING

```yaml
METRICS:
  calibration_run_completion_rate:   % of scheduled runs completing within time window
  reclassification_rate:             RECLASSIFIED / total evaluations (per domain)
  insufficient_sample_rate:          INSUFFICIENT_SAMPLE / total evaluations
  retirement_recommendation_rate:    % of scenarios receiving retirement recommendation
  curriculum_flag_rate:              % of evaluations emitting curriculum flags
  pathway_change_rate:               % of user pathway evaluations triggering a change
  regression_detection_recall:       % of confirmed regressions detected early (monthly)
  normalization_factor_drift:        average delta between consecutive normalization updates
  collusion_exclusion_rate:          % of events excluded from calibration pools
  latency_batch_p99:                 P99 batch calibration cycle time
  latency_pathway_p99:               P99 per-user pathway computation time
  dl_queue_depth:                    DLQ depth per topic (alert if > 500)
  ml_fallback_rate:                  % of calibrations using rule-engine fallback (ML unavailable)

DASHBOARDS_REQUIRED:
  - Calibration Pipeline Health (completion rate, latency, DLQ depth per domain)
  - Difficulty Distribution Map (% of scenarios per tier, per domain_track)
  - Curriculum Health Board (active flags, pending retirements, fairness audit status)
  - User Pathway Distribution (% of users per tier per domain — regression risk)
  - Normalization Factor Trend (per-tier average normalization over time)
  - Regression Detection Funnel (detected / alerted / acted upon)
  - Assessment Integrity Dashboard (difficulty_shift, fairness_index — from O7 mandate)

ALERTING:
  calibration_run_not_completed          → WARN (P2 SLA: 1 hour)
  calibration_batch_p99 > 90 min        → WARN
  retirement_recommendation_rate > 5%   → CRITICAL (mass curriculum quality failure)
  curriculum_flag_rate > 10%            → WARN
  regression_detection_recall < 80%     → WARN (model drift)
  audit_log_write_failure               → CRITICAL (data integrity)
  kafka_dlq_depth > 500                 → CRITICAL

INTEGRATED_WITH: OBSERVABILITY_AGENT
```

---

## 2️⃣2️⃣ DATABASE SCHEMA (LOCKED v1.0.0)

```sql
-- Difficulty Current State: mutable snapshot (latest per scenario/assignment/challenge)
CREATE TABLE difficulty_current_state (
  state_id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  subject_id              UUID NOT NULL,
  subject_type            TEXT NOT NULL,
  tenant_id               UUID NOT NULL,
  domain                  TEXT NOT NULL,
  domain_track            TEXT NOT NULL,
  current_difficulty_tier TEXT NOT NULL,
  normalization_factor    NUMERIC(5,2) NOT NULL DEFAULT 1.00,
  author_seed_tier        TEXT NOT NULL,         -- original author classification (seed)
  data_derived            BOOLEAN NOT NULL DEFAULT FALSE,
  sample_count            INTEGER NOT NULL DEFAULT 0,
  last_calibrated_at      TIMESTAMPTZ,
  calibration_tx_id       UUID,
  policy_version          TEXT NOT NULL,
  retirement_recommended  BOOLEAN NOT NULL DEFAULT FALSE,
  is_retired              BOOLEAN NOT NULL DEFAULT FALSE,
  retired_at              TIMESTAMPTZ,
  created_at              TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at              TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE (subject_id, tenant_id, domain, domain_track)
);

-- Difficulty Calibration Ledger: append-only history of all calibration decisions
CREATE TABLE difficulty_calibration_ledger (
  calibration_transaction_id  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  event_id                    UUID NOT NULL UNIQUE,  -- idempotency key
  subject_id                  UUID NOT NULL,
  subject_type                TEXT NOT NULL,
  tenant_id                   UUID NOT NULL,
  domain                      TEXT NOT NULL,
  domain_track                TEXT NOT NULL,
  action_taken                TEXT NOT NULL,
  previous_difficulty_tier    TEXT,
  new_difficulty_tier         TEXT,
  normalization_factor_prev   NUMERIC(5,2),
  normalization_factor_new    NUMERIC(5,2),
  calibration_evidence        JSONB NOT NULL,
  rule_matched                TEXT,
  ml_suggestion               TEXT,
  ml_confidence               NUMERIC(4,3),
  rule_engine_overrode_ml     BOOLEAN NOT NULL DEFAULT FALSE,
  retirement_recommended      BOOLEAN NOT NULL DEFAULT FALSE,
  curriculum_flag_code        TEXT,
  collusion_excluded_count    INTEGER NOT NULL DEFAULT 0,
  policy_version              TEXT NOT NULL,
  model_version               TEXT NOT NULL,
  audit_reference             UUID NOT NULL,
  created_at                  TIMESTAMPTZ NOT NULL DEFAULT now()
);
-- IMMUTABLE: no UPDATE or DELETE

-- User Pathway Store: latest pathway per user per domain per domain_track
CREATE TABLE user_pathway_store (
  pathway_id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id                 UUID NOT NULL,
  tenant_id               UUID NOT NULL,
  domain                  TEXT NOT NULL,
  domain_track            TEXT NOT NULL,
  skill_tag               TEXT,                -- for GROWTH skill-tag pathways
  current_pathway_tier    TEXT NOT NULL,
  trend_classification    TEXT NOT NULL,
  consecutive_wins        INTEGER NOT NULL DEFAULT 0,
  consecutive_losses      INTEGER NOT NULL DEFAULT 0,
  last_computed_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
  calibration_tx_id       UUID,
  policy_version          TEXT NOT NULL,
  UNIQUE (user_id, tenant_id, domain, domain_track, skill_tag)
);

-- Difficulty Unlock Store: per-user unlock gates
CREATE TABLE difficulty_unlock_store (
  unlock_id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id                 UUID NOT NULL,
  tenant_id               UUID NOT NULL,
  domain                  TEXT NOT NULL,
  domain_track            TEXT NOT NULL,
  easy_completions        INTEGER NOT NULL DEFAULT 0,
  medium_unlocked         BOOLEAN NOT NULL DEFAULT FALSE,
  medium_completions      INTEGER NOT NULL DEFAULT 0,
  hard_unlocked           BOOLEAN NOT NULL DEFAULT FALSE,
  hard_unlocked_at        TIMESTAMPTZ,
  medium_unlocked_at      TIMESTAMPTZ,
  last_updated_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE (user_id, tenant_id, domain, domain_track)
);

-- Calibration Audit Log: full traceability (append-only)
CREATE TABLE difficulty_calibration_audit_log (
  audit_id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  calibration_transaction_id  UUID NOT NULL,
  timestamp_utc               TIMESTAMPTZ NOT NULL,
  actor_id                    UUID NOT NULL,
  tenant_id                   UUID NOT NULL,
  institution_id              UUID,
  input_hash                  TEXT NOT NULL,
  output_hash                 TEXT NOT NULL,
  model_version               TEXT NOT NULL,
  policy_version              TEXT NOT NULL,
  decision_path               JSONB NOT NULL,
  anomaly_flags               JSONB NOT NULL DEFAULT '[]',
  action_taken                TEXT NOT NULL,
  ml_fallback_used            BOOLEAN NOT NULL DEFAULT FALSE,
  downstream_events_emitted   JSONB NOT NULL DEFAULT '[]',
  created_at                  TIMESTAMPTZ NOT NULL DEFAULT now()
);
-- IMMUTABLE: no UPDATE or DELETE
```

---

## 2️⃣3️⃣ VERSIONING POLICY

```yaml
ADD_ONLY:                     All changes are additive
NEW_DIFFICULTY_TIER:          MAJOR version bump (v1.0.0 → v2.0.0) + full migration doc
RECLASSIFICATION_RULE_CHANGE: MAJOR version bump + existing classification preserved
NORMALIZATION_FORMULA_CHANGE: MAJOR version bump + recalibration audit for all scenarios
NEW_RETIREMENT_RULE:          MINOR version bump (v1.0.0 → v1.1.0)
NEW_CURRICULUM_FLAG_CODE:     MINOR version bump
BENCHMARK_TIME_CHANGE:        MINOR version bump (quarterly update cycle)
MIN_SAMPLE_THRESHOLD_CHANGE:  MINOR version bump + justification doc
NEW_DOMAIN_SUPPORT:           MINOR version bump

BACKWARD_COMPATIBILITY:
  - Old calibration_transaction_id references valid indefinitely
  - Scenarios calibrated under older policy retain their classification until recalibrated
  - normalization_factor version history fully preserved in ledger
  - User pathway history never deleted

ROLLBACK:
  - Deactivate new policy version, reactivate previous
  - In-flight batch calibration runs restart under previous policy
  - Difficulty states calibrated under new policy remain valid (no retroactive reversal)
  - Policy version stored on every record enables per-record rollback analysis
```

---

## 2️⃣4️⃣ NON-NEGOTIABLE RULES (SEALED — CANNOT BE OVERRIDDEN)

```
DIFFICULTY_ADAPTATION_AGENT MUST NOT:
  ❌ Accept author-declared difficulty labels as final — data-derived only (T4 mandate)
  ❌ Use static difficulty labels — all labels are subject to continuous re-evaluation
  ❌ Directly retire a scenario — retirement is CURRICULUM_GOVERNANCE_AGENT's decision
  ❌ Include collusion-flagged match data in any calibration pool
  ❌ Execute reclassification below MIN_SAMPLE_THRESHOLD
  ❌ Create hidden calibration logic outside of DIFFICULTY_POLICY_STORE
  ❌ Modify historical calibration records (append-only, ever)
  ❌ Auto-delete audit logs under any condition
  ❌ Override COLLUSION_DETECTION_AGENT exclusion flags
  ❌ Bypass tenant isolation checks for any reason
  ❌ Share calibration data cross-tenant or cross-institution
  ❌ Allow normalization_factor to exceed CLAMP ceiling of 2.50
  ❌ Allow normalization_factor to fall below CLAMP floor of 0.70
  ❌ Use AI/LLM to make difficulty classification decisions
  ❌ Allow difficulty tiers to be skipped in user pathways (must step up/down one tier)
  ❌ Assign a difficulty tier above the user's current DIFFICULTY_UNLOCK state
  ❌ Process events for scenarios marked as RETIRED
  ❌ Execute outside the scope of EVENT_TYPE_REGISTRY
  ❌ Emit curriculum flags without statistical evidence meeting threshold
  ❌ Allow ML model to override rule engine when ML confidence < 0.70
  ❌ Skip audit log writes — audit failure = calibration transaction failure
```

---

## 🔒 EVENT_TYPE_REGISTRY (LOCKED v1.0.0)

Any event_type not in this registry → REJECT immediately.

```
DOJO DOMAIN:
  DOJO_MATCH_COMPLETION_SIGNAL       # Per match: score, time, outcome, scenario_id
  DOJO_MATCH_ABANDONMENT_SIGNAL      # Per abandonment: scenario_id, abandoned_at
  DOJO_SCENARIO_CALIBRATION_TRIGGER  # Scheduled: run calibration for scenario_id
  DOJO_USER_PATHWAY_COMPUTE          # Per user: compute next difficulty pathway
  DOJO_REGRESSION_CHECK              # Per user: weekly regression detection
  DOJO_DIFFICULTY_UNLOCK_UPDATE      # Per user: update unlock gate counts
  DOJO_TIME_ATTACK_COMPLETION_SIGNAL # Time attack mode completions (isolated calibration)

GROWTH DOMAIN:
  GROWTH_CHALLENGE_COMPLETION_SIGNAL # Weekly challenge completion
  GROWTH_CHALLENGE_CALIBRATION_TRIGGER
  GROWTH_SKILL_TAG_XP_SIGNAL         # Per-user skill XP delta per tag
  GROWTH_PLATEAU_DETECTION_CHECK     # Weekly per user per skill_tag
  GROWTH_TIME_ATTACK_CALIBRATION_TRIGGER

SCHOOL OPS DOMAIN:
  SCHOOL_ASSIGNMENT_COMPLETION_SIGNAL
  SCHOOL_ASSIGNMENT_CALIBRATION_TRIGGER
  SCHOOL_DRILL_COMPLETION_SIGNAL
  SCHOOL_DRILL_EFFECTIVENESS_CHECK   # Per drill per cohort
  SCHOOL_INACTIVITY_CHECK            # Daily per user per segment threshold
  SCHOOL_COHORT_CALIBRATION_TRIGGER  # Per cohort, per assignment batch
```

---

## 🔒 FINAL GOVERNANCE SEAL

```
╔═════════════════════════════════════════════════════════════════════════════════════╗
║           DIFFICULTY_ADAPTATION_AGENT — FINAL GOVERNANCE SEAL                      ║
║                                                                                     ║
║  DIFFICULTY LABEL AUTHORITY     DATA-DERIVED, ML-GOVERNED RULE ENGINE              ║
║  AUTHOR-DECLARED LABELS         SEED VALUE ONLY — OVERRIDDEN ON FIRST CALIBRATION  ║
║  STATIC DIFFICULTY LABELS       FORBIDDEN — CONTINUOUS RE-EVALUATION MANDATORY     ║
║  SCENARIO RETIREMENT            RECOMMENDATION ONLY — CURRICULUM_GOVERNANCE OWNS   ║
║  AI IN CALIBRATION DECISIONS    FORBIDDEN                                          ║
║  COLLUSION DATA IN POOLS        FORBIDDEN                                          ║
║  AUDIT TRAIL                    IMMUTABLE APPEND-ONLY                              ║
║  TENANT ISOLATION               HARD ENFORCED AT QUERY + JWT LAYER                ║
║  CROSS-INSTITUTION DATA SHARE   FORBIDDEN (SCHOOL OPS)                             ║
║  NORMALIZATION FACTOR BOUNDS    CLAMPED 0.70–2.50 (non-negotiable)                ║
║  MINIMUM SAMPLE GATE            HARD GATE — NO EXCEPTIONS                         ║
║  ML OVERRIDE OF RULE ENGINE     FORBIDDEN WHEN ML CONFIDENCE < 0.70               ║
║  DIFFICULTY UNLOCK GATE         ENFORCED PER USER — TIERS CANNOT BE SKIPPED        ║
║  SILENT FAILURES                FORBIDDEN                                          ║
║  HISTORY MUTATION               FORBIDDEN                                          ║
║  ASSUMPTION FILLING             FORBIDDEN                                          ║
║                                                                                     ║
║  DOJO DIFFICULTY OWNER:         DIFFICULTY_ADAPTATION_AGENT v1.0.0                ║
║  GROWTH DIFFICULTY OWNER:       DIFFICULTY_ADAPTATION_AGENT v1.0.0                ║
║  SCHOOL OPS DIFFICULTY OWNER:   DIFFICULTY_ADAPTATION_AGENT v1.0.0                ║
║  NORMALIZATION FACTOR OWNER:    DIFFICULTY_ADAPTATION_AGENT v1.0.0                ║
║  USER PATHWAY OWNER:            DIFFICULTY_ADAPTATION_AGENT v1.0.0                ║
║  CURRICULUM HEALTH SIGNAL OWNER:DIFFICULTY_ADAPTATION_AGENT v1.0.0                ║
║                                                                                     ║
║  ARCHITECTURE:                  SEALED                                             ║
║  INTERPRETATION AUTHORITY:      NONE                                               ║
║  MUTATION AUTHORITY:            VERSION BUMP ONLY — ADD-ONLY                      ║
║                                                                                     ║
║  ECOSKILLER ANTIGRAVITY — ENTERPRISE SAAS GRADE — 10M–100M USERS                  ║
╚═════════════════════════════════════════════════════════════════════════════════════╝
```

---
*DIFFICULTY_ADAPTATION_AGENT.md — v1.0.0 | Ecoskiller Antigravity | Generated per Master Agent Creation Prompt | Sealed & Locked*
