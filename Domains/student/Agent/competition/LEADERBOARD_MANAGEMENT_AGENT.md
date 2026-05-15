# 🔒 ECOSKILLER — ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
## LEADERBOARD_MANAGEMENT_AGENT
### Antigravity Layer · Sealed · Locked · Versioned · Deterministic

```
Status:           ACTIVE · ENFORCED · ADD-ONLY
Mutation Policy:  Add-only via version bump
Interpretation:   FORBIDDEN
Assumption Fill:  FORBIDDEN
Default Behavior: DENY
Failure Mode:     STOP_EXECUTION
Execution Mode:   DETERMINISTIC
Version:          v1.0 — SEALED
Document Class:   ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
```

---

## AGENT IDENTITY BLOCK

```
Agent Name:         LEADERBOARD_MANAGEMENT_AGENT
Agent Class:        ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
Layer:              Antigravity
Parent System:      ECOSKILLER MASTER EXECUTION PROMPT v12.0+
Namespace (k8s):    society / realtime / analytics
Stack Lock:         Python 3.11 + FastAPI + Redis + PostgreSQL +
                    ClickHouse + Kafka + WebSocket + Prometheus +
                    Grafana + Loki + OpenTelemetry
Depends On:         R2, R5, R10, R21, R28, R38, R39, R40, R49,
                    R50, R51, R53, R57, R61, R68, R79, R87,
                    INTER_UNIT_COMPETITION_AGENT
Produces:           Leaderboard snapshots, rank records, trust-weighted
                    positions, badge grants, privilege triggers,
                    manipulation flags, public SEO rank pages,
                    real-time WebSocket rank feeds, ClickHouse
                    analytics rows, immutable audit hash chain
Consumed By:        Intelligence Engine, Billing Service,
                    Admin Governance Service, Notification Service,
                    Analytics Service, Reputation Engine,
                    SEO Regeneration Hook, Competition Agent,
                    Society Domain Layer, Franchise Engine
```

---

## SECTION I — AGENT PURPOSE & ANTIGRAVITY DEFINITION

### I.A — What This Agent Does

The LEADERBOARD_MANAGEMENT_AGENT is the **public face of competitive truth**
inside ECOSKILLER.

Where the INTER_UNIT_COMPETITION_AGENT computes scores in the dark,
this agent decides what is shown, to whom, at what resolution,
with what trust weighting, and in real time.

It is not a display service.
It is an enforcement surface.

**The leaderboard is not a vanity board.
It is a trust signal that redistributes attention, opportunity, and
platform resources based on verified behavioral evidence.**

The agent governs:

- What leaderboard dimensions exist across the full entity spectrum
- How ranks are computed, weighted, and displayed per audience type
- How real-time rank feeds are delivered to live interfaces
- How trust signals inflate or deflate visible rank positions
- How manipulation attempts are detected and suppressed
- How public SEO leaderboard pages are generated and invalidated
- How historical rank trajectories are stored and surfaced
- How offline and society-domain franchise networks are ranked separately

### I.B — Antigravity Mechanism

Without a governed leaderboard agent, the following decay patterns occur:

```
DECAY PATTERN 01: Rank Inflation
  Inactive high-history entities retain top positions
  New high-performers cannot displace them
  Platform appears rigged to new entrants → churn

DECAY PATTERN 02: Trust Score Decoupling
  Raw activity score ≠ trust-weighted quality signal
  Manipulation gaming creates false top positions
  Recruiters, students, and institutions lose confidence

DECAY PATTERN 03: Visibility Stagnation
  Same entities occupy visible positions permanently
  Virality collapses — no "rising" narrative
  External sharing drops — no leaderboard-induced social proof

DECAY PATTERN 04: Audience Blindness
  Recruiter view = student view = admin view
  Context-free rankings confuse all audiences
  Decision quality degrades for all stakeholders

DECAY PATTERN 05: Offline Network Invisibility
  Society domain and franchise offline units are unranked
  Offline participants have no competitive incentive
  Society-tier adoption stalls
```

**The LEADERBOARD_MANAGEMENT_AGENT eliminates all five decay patterns
through deterministic rank governance, trust weighting, audience
segmentation, real-time feed delivery, and offline domain inclusion.**

### I.C — Non-Negotiable Constraints

```
FORBIDDEN:
- Manual rank adjustments without full audit trail + dual admin sign-off
- AI-inferred rank positions or confidence-scored placements
- Algorithmic boosting for paying tenants without declared rule basis
- Suppression of rank positions without consequence audit log
- Cross-leaderboard contamination (each dimension is isolated)
- Public display of unverified entities
- Real-time feed delivery without Redis state validation
- Leaderboard snapshots without hash verification

REQUIRED:
- Rule engine only (R28-1) for all rank computation
- Trust weighting applied as declared formula — not heuristic
- Every rank change emits an event (leaderboard.rank_changed)
- Every manipulation flag emits an event (leaderboard.manipulation_detected)
- Public pages governed by R31 canonical + ISR rules
- Real-time delivery via WebSocket — not polling
- All audit entries hash-chained (tamper-evident)
- Offline / society domain leaderboards in isolated dimension
- Intern-executable local test environment (R46)
```

---

## SECTION II — LEADERBOARD DIMENSION REGISTRY

Each dimension is a fully isolated leaderboard universe.
Dimensions do not share rank pools.
Cross-dimension aggregates are computed separately and labeled as such.

### II.A — Primary Dimensions

```
DIM_01: STUDENT_INDIVIDUAL
  Entity Type:    Individual Student
  Scope:          Platform-wide + Institution-scoped + Cohort-scoped
  Update Cycle:   Every 6 hours + on-demand on trigger events
  Audience:       Student (self), Institution Admin, Recruiter, Public
  Trust Weighted: YES
  SEO Indexed:    YES (institution-scoped public page)
  Offline:        NO

DIM_02: INSTITUTION_UNIT
  Entity Type:    Verified Educational Institution
  Scope:          Platform-wide + Domain-track-scoped + State-scoped
  Update Cycle:   Daily (02:00 UTC)
  Audience:       Institution Admin, Recruiter, Platform Admin, Public
  Trust Weighted: YES
  SEO Indexed:    YES
  Offline:        NO

DIM_03: ENTERPRISE_HIRING
  Entity Type:    Verified Enterprise (hiring quality)
  Scope:          Platform-wide + Industry-sector-scoped
  Update Cycle:   Daily (02:00 UTC)
  Audience:       Candidate, Recruiter Admin, Platform Admin, Public
  Trust Weighted: YES
  SEO Indexed:    YES
  Offline:        NO

DIM_04: TRAINER_INDIVIDUAL
  Entity Type:    Verified Trainer / Mentor
  Scope:          Platform-wide + Domain-track-scoped
  Update Cycle:   Every 6 hours
  Audience:       Student, Institution Admin, Platform Admin, Public
  Trust Weighted: YES
  SEO Indexed:    YES
  Offline:        NO

DIM_05: TRAINER_GROUP
  Entity Type:    Registered Trainer Group / Cohort
  Scope:          Platform-wide + Domain-track-scoped
  Update Cycle:   Daily (02:00 UTC)
  Audience:       Institution Admin, Student, Platform Admin, Public
  Trust Weighted: YES
  SEO Indexed:    YES
  Offline:        NO

DIM_06: DOJO_INDIVIDUAL
  Entity Type:    Individual participant (Dojo matches)
  Scope:          Platform-wide + Domain-track-scoped + Belt-tier-scoped
  Update Cycle:   Real-time on match close
  Audience:       All authenticated users, Public (top 100)
  Trust Weighted: YES
  SEO Indexed:    YES (top 100 public page)
  Offline:        NO

DIM_07: DOJO_TEAM
  Entity Type:    Registered Dojo Team
  Scope:          Platform-wide + Tournament-scoped
  Update Cycle:   Real-time on match close
  Audience:       All authenticated users, Public
  Trust Weighted: YES
  SEO Indexed:    YES
  Offline:        NO

DIM_08: GD_COMPLIANCE
  Entity Type:    Individual candidate (Voice GD sessions)
  Scope:          Institution-scoped + Batch-scoped
  Update Cycle:   After every GD session close
  Audience:       Institution Admin, Recruiter, Candidate (self)
  Trust Weighted: YES
  SEO Indexed:    NO (private — candidate data)
  Offline:        NO

DIM_09: STUDENT_COHORT
  Entity Type:    Student batch (institution + year)
  Scope:          Platform-wide + Institution-scoped
  Update Cycle:   Daily (02:00 UTC)
  Audience:       Institution Admin, Recruiter, Public
  Trust Weighted: YES
  SEO Indexed:    YES
  Offline:        NO

DIM_10: SOCIETY_FRANCHISE
  Entity Type:    Offline Society Franchise Unit
  Scope:          Society-domain + State-scoped + District-scoped
  Update Cycle:   Daily (03:00 UTC, after society sync)
  Audience:       Franchise Owner, Society Admin, Master Organizer, Public
  Trust Weighted: YES
  SEO Indexed:    YES
  Offline:        YES — synced via CouchDB replication layer
  Note:           Governed under society k8s namespace

DIM_11: COACH_INDIVIDUAL
  Entity Type:    Verified Society-domain Coach
  Scope:          Society-domain + District-scoped
  Update Cycle:   Daily (03:00 UTC)
  Audience:       Franchise Owner, Society Admin, Public
  Trust Weighted: YES
  SEO Indexed:    YES
  Offline:        YES

DIM_12: INNOVATION_CONTRIBUTOR
  Entity Type:    Individual innovator (Idea Registry)
  Scope:          Platform-wide + Domain-category-scoped
  Update Cycle:   Weekly (Sunday 00:00 UTC)
  Audience:       All authenticated users, Business Discovery, Public
  Trust Weighted: YES
  SEO Indexed:    YES
  Offline:        NO
```

### II.B — Composite / Aggregate Dimensions

```
DIM_AGG_01: PLATFORM_TRUST_INDEX
  Description:    Cross-entity trust signal aggregate
  Entity Types:   Institution + Enterprise + Trainer Group (merged view)
  Scope:          Platform-wide
  Update Cycle:   Daily
  SEO Indexed:    YES — public transparency page (R62)
  Note:           Labeled explicitly as AGGREGATE — not primary rank

DIM_AGG_02: PLACEMENT_ECOSYSTEM_RANK
  Description:    Combined placement quality signal
  Entity Types:   Institution + Enterprise (placement chain performance)
  Scope:          State-scoped + National
  Update Cycle:   Weekly
  SEO Indexed:    YES
  Note:           Consumed by Public Transparency Report (R62)
```

---

## SECTION III — DATABASE SCHEMA (MANDATORY)

Absence of any table → STOP EXECUTION.

### III.A — Core Leaderboard Tables

```sql
-- Dimension configuration registry
leaderboard_dimensions (
  dimension_id        TEXT PRIMARY KEY,         -- e.g. DIM_01
  dimension_name      TEXT NOT NULL,
  entity_type         TEXT NOT NULL,
  scope_type          TEXT NOT NULL,            -- GLOBAL | INSTITUTION | DOMAIN | SOCIETY
  update_cycle        TEXT NOT NULL,            -- REALTIME | 6H | DAILY | WEEKLY
  trust_weighted      BOOLEAN NOT NULL DEFAULT TRUE,
  seo_indexed         BOOLEAN NOT NULL DEFAULT FALSE,
  offline_enabled     BOOLEAN NOT NULL DEFAULT FALSE,
  audience_flags      JSONB NOT NULL,           -- {student, institution, recruiter, public, admin}
  active              BOOLEAN NOT NULL DEFAULT TRUE,
  created_at          TIMESTAMP NOT NULL
)

-- Scope registry for scoped leaderboards
leaderboard_scopes (
  scope_id            UUID PRIMARY KEY,
  dimension_id        TEXT REFERENCES leaderboard_dimensions,
  scope_type          TEXT NOT NULL,            -- INSTITUTION | DOMAIN_TRACK | STATE | DISTRICT | BELT | TOURNAMENT
  scope_reference_id  UUID NOT NULL,            -- FK to the scoping entity
  scope_label         TEXT NOT NULL,
  active              BOOLEAN NOT NULL DEFAULT TRUE
)

-- Entity registration per dimension+scope
leaderboard_entries (
  entry_id            UUID PRIMARY KEY,
  dimension_id        TEXT REFERENCES leaderboard_dimensions,
  scope_id            UUID REFERENCES leaderboard_scopes,
  entity_id           UUID NOT NULL,
  entity_type         TEXT NOT NULL,
  display_name        TEXT NOT NULL,
  verified_status     BOOLEAN NOT NULL DEFAULT FALSE,
  enrolled_at         TIMESTAMP NOT NULL,
  last_active_at      TIMESTAMP,
  suppressed          BOOLEAN NOT NULL DEFAULT FALSE,
  suppressed_reason   TEXT,
  entry_hash          TEXT NOT NULL
)

-- Raw score signals feeding rank computation
leaderboard_signals (
  signal_id           UUID PRIMARY KEY,
  dimension_id        TEXT REFERENCES leaderboard_dimensions,
  entity_id           UUID NOT NULL,
  signal_type         TEXT NOT NULL,            -- references SIGNAL_TYPE registry
  signal_value        NUMERIC NOT NULL,
  signal_source       TEXT NOT NULL,
  signal_timestamp    TIMESTAMP NOT NULL,
  trust_verified      BOOLEAN NOT NULL DEFAULT FALSE,
  trust_verifier      TEXT,
  signal_hash         TEXT NOT NULL
)

-- Computed rank snapshots (immutable after write)
leaderboard_snapshots (
  snapshot_id         UUID PRIMARY KEY,
  dimension_id        TEXT REFERENCES leaderboard_dimensions,
  scope_id            UUID REFERENCES leaderboard_scopes,
  entity_id           UUID NOT NULL,
  snapshot_date       DATE NOT NULL,
  raw_score           NUMERIC NOT NULL,
  trust_score         NUMERIC NOT NULL,         -- trust-weighted final score
  rank_position       INTEGER NOT NULL,
  previous_rank       INTEGER,
  rank_delta          INTEGER,                  -- negative = improved rank
  percentile          NUMERIC(5,2),
  tier_label          TEXT,                     -- ELITE | TOP | MID | RISING | BOTTOM
  snapshot_hash       TEXT NOT NULL,
  computed_at         TIMESTAMP NOT NULL,
  UNIQUE (dimension_id, scope_id, entity_id, snapshot_date)
)

-- Live leaderboard state (materialized, refreshed on snapshot write)
leaderboard_live (
  live_id             UUID PRIMARY KEY,
  dimension_id        TEXT REFERENCES leaderboard_dimensions,
  scope_id            UUID REFERENCES leaderboard_scopes,
  entity_id           UUID NOT NULL,
  entity_type         TEXT NOT NULL,
  display_name        TEXT NOT NULL,
  rank_position       INTEGER NOT NULL,
  previous_rank       INTEGER,
  rank_delta          INTEGER,
  trust_score         NUMERIC NOT NULL,
  tier_label          TEXT NOT NULL,
  rising_flag         BOOLEAN NOT NULL DEFAULT FALSE,  -- top 10% rank_delta improvement
  badge_flags         JSONB,
  streak_days         INTEGER DEFAULT 0,
  last_snapshot_date  DATE NOT NULL,
  last_updated        TIMESTAMP NOT NULL,
  UNIQUE (dimension_id, scope_id, entity_id)
)

-- Trust weight registry per signal type
leaderboard_trust_weights (
  weight_id           UUID PRIMARY KEY,
  dimension_id        TEXT REFERENCES leaderboard_dimensions,
  signal_type         TEXT NOT NULL,
  base_weight         NUMERIC NOT NULL,
  trust_multiplier    NUMERIC NOT NULL DEFAULT 1.0,  -- applied when trust_verified = TRUE
  decay_lambda        NUMERIC NOT NULL DEFAULT 0.05, -- recency decay constant
  active              BOOLEAN NOT NULL DEFAULT TRUE
)

-- Manipulation detection log
leaderboard_manipulation_log (
  log_id              UUID PRIMARY KEY,
  dimension_id        TEXT NOT NULL,
  entity_id           UUID NOT NULL,
  detection_rule      TEXT NOT NULL,
  signal_ids_flagged  UUID[] NOT NULL,
  severity            INTEGER NOT NULL,          -- 1-5
  action_taken        TEXT NOT NULL,             -- FLAGGED | SUPPRESSED | SIGNALS_EXCLUDED
  detected_at         TIMESTAMP NOT NULL,
  resolved            BOOLEAN NOT NULL DEFAULT FALSE,
  resolved_at         TIMESTAMP,
  log_hash            TEXT NOT NULL
)

-- Immutable audit chain
leaderboard_audit_log (
  audit_id            UUID PRIMARY KEY,
  dimension_id        TEXT,
  scope_id            UUID,
  entity_id           UUID,
  agent_action        TEXT NOT NULL,
  action_input        JSONB NOT NULL,
  action_output       JSONB NOT NULL,
  action_timestamp    TIMESTAMP NOT NULL,
  actor               TEXT NOT NULL,             -- AGENT | ADMIN | SYSTEM
  admin_id            UUID,
  previous_audit_hash TEXT,                      -- chain link
  audit_hash          TEXT NOT NULL              -- sha256(previous_hash + action_data)
)

-- Public SEO page generation log
leaderboard_seo_pages (
  page_id             UUID PRIMARY KEY,
  dimension_id        TEXT REFERENCES leaderboard_dimensions,
  scope_id            UUID,
  page_url            TEXT NOT NULL,
  last_generated_at   TIMESTAMP NOT NULL,
  last_invalidated_at TIMESTAMP,
  entity_count        INTEGER NOT NULL,
  page_hash           TEXT NOT NULL
)

-- Offline sync queue (society domain)
leaderboard_offline_sync_queue (
  sync_id             UUID PRIMARY KEY,
  dimension_id        TEXT REFERENCES leaderboard_dimensions,
  entity_id           UUID NOT NULL,
  signal_payload      JSONB NOT NULL,
  originated_at       TIMESTAMP NOT NULL,        -- offline timestamp
  synced_at           TIMESTAMP,
  sync_status         TEXT NOT NULL DEFAULT 'PENDING', -- PENDING | SYNCED | CONFLICT
  conflict_resolution TEXT
)
```

---

## SECTION IV — SIGNAL TYPE REGISTRY

All signals consumed by the rank computation engine must be declared here.
Undeclared signal types are rejected at ingestion.

### IV.A — Student Individual Signals (DIM_01)

```
SIG_STU_GD_COMPLIANCE_SCORE
  Source:   Voice GD Orchestrator
  Unit:     Score 0–100
  Weight:   HIGH (3.0)
  Trust:    Auto-verified (deterministic GD protocol)
  Decay:    YES (λ=0.05)

SIG_STU_DOJO_MATCH_WIN_RATE
  Source:   Dojo Match Engine
  Unit:     Percentage 0–100
  Weight:   HIGH (3.0)
  Trust:    Auto-verified (Dojo scoring engine)
  Decay:    YES (λ=0.05)

SIG_STU_SKILL_PASSPORT_COUNT
  Source:   Certification & Belt Engine
  Unit:     Count
  Weight:   MEDIUM (2.0)
  Trust:    Auto-verified (mentor confirmation required)
  Decay:    NO (permanent record)

SIG_STU_PROJECT_DELIVERY_VERIFIED
  Source:   Project Execution Engine
  Unit:     Count
  Weight:   HIGH (3.0)
  Trust:    Requires mentor + peer verification dual sign
  Decay:    NO

SIG_STU_PLACEMENT_OFFER_RECEIVED
  Source:   Application Service
  Unit:     Count
  Weight:   HIGH (3.0)
  Trust:    Requires company + student dual acceptance
  Decay:    NO

SIG_STU_STREAK_LENGTH
  Source:   Streak Engine (R57 / R95)
  Unit:     Days
  Weight:   LOW (1.0)
  Trust:    Auto-verified
  Decay:    YES (λ=0.10, fast decay to prevent stale streaks)

SIG_STU_PEER_ENDORSEMENT_COUNT
  Source:   Student Reputation Engine (R79, R91)
  Unit:     Count
  Weight:   MEDIUM (2.0)
  Trust:    Requires verified student endorser
  Decay:    YES (λ=0.03)

SIG_STU_COURSE_COMPLETION_COUNT
  Source:   Education Service
  Unit:     Count
  Weight:   MEDIUM (2.0)
  Trust:    Auto-verified
  Decay:    YES (λ=0.02)

SIG_STU_INNOVATION_SUBMISSION_SCORE
  Source:   Innovation Scoring Engine
  Unit:     Score 0–100
  Weight:   HIGH (3.0)
  Trust:    Requires Idea DNA verification pass
  Decay:    NO
```

### IV.B — Institution Unit Signals (DIM_02)

```
SIG_INST_PLACEMENT_RATE
  Source:   Application Service
  Unit:     Percentage 0–100
  Weight:   HIGH (3.0)
  Trust:    Dual-verified (company + student accept)
  Decay:    YES (λ=0.03)

SIG_INST_GD_PARTICIPATION_RATE
  Source:   Voice GD Orchestrator
  Unit:     Percentage 0–100
  Weight:   MEDIUM (2.0)
  Trust:    Auto-verified
  Decay:    YES (λ=0.05)

SIG_INST_COMPLAINT_RESOLUTION_RATE
  Source:   Admin Governance Service
  Unit:     Percentage 0–100
  Weight:   HIGH (3.0)
  Trust:    Auto-verified (anonymous complaint system R36)
  Decay:    YES (λ=0.07, complaints age out faster)

SIG_INST_SKILL_PASSPORT_ISSUANCE_RATE
  Source:   Certification Engine
  Unit:     Percentage 0–100
  Weight:   MEDIUM (2.0)
  Trust:    Auto-verified
  Decay:    YES (λ=0.02)

SIG_INST_RECRUITER_ENGAGEMENT_COUNT
  Source:   Job Service + Application Service
  Unit:     Count
  Weight:   HIGH (3.0)
  Trust:    Verified recruiter identity required
  Decay:    YES (λ=0.04)

SIG_INST_STUDENT_RETENTION_RATE
  Source:   User Service
  Unit:     Percentage 0–100
  Weight:   MEDIUM (2.0)
  Trust:    Auto-verified
  Decay:    YES (λ=0.03)
```

### IV.C — Enterprise Hiring Signals (DIM_03)

```
SIG_ENT_GD_FAIRNESS_SCORE
  Source:   Voice GD Orchestrator
  Unit:     Score 0–100
  Weight:   HIGH (3.0)
  Trust:    Auto-verified (deterministic GD protocol)
  Decay:    YES (λ=0.07)

SIG_ENT_OFFER_ACCEPTANCE_RATE
  Source:   Application Service
  Unit:     Percentage 0–100
  Weight:   HIGH (3.0)
  Trust:    Dual-verified
  Decay:    YES (λ=0.05)

SIG_ENT_CANDIDATE_FEEDBACK_SCORE
  Source:   Application Service
  Unit:     Score 0–100
  Weight:   HIGH (3.0)
  Trust:    Verified candidate identity required
  Decay:    YES (λ=0.06)

SIG_ENT_TIME_TO_HIRE_NORMALIZED
  Source:   Job Service
  Unit:     Score 0–100 (lower days = higher score)
  Weight:   MEDIUM (2.0)
  Trust:    Auto-verified
  Decay:    YES (λ=0.05)

SIG_ENT_DISPUTE_RESOLUTION_RATE
  Source:   Admin Governance Service
  Unit:     Percentage 0–100
  Weight:   HIGH (3.0)
  Trust:    Auto-verified
  Decay:    YES (λ=0.07)
```

### IV.D — Dojo Individual Signals (DIM_06)

```
SIG_DOJO_WIN_RATE
  Source:   Dojo Match Engine + Scoring Engine
  Unit:     Percentage 0–100
  Weight:   HIGH (3.0)
  Trust:    Auto-verified (immutable match result)
  Decay:    YES (λ=0.04)

SIG_DOJO_BELT_LEVEL
  Source:   Certification & Belt Engine
  Unit:     Ordinal (mapped 1–10 per belt level)
  Weight:   HIGH (3.0)
  Trust:    Mentor confirmation required
  Decay:    NO (permanent credential)

SIG_DOJO_SCENARIO_DIFFICULTY_AVG
  Source:   Dojo Match Engine
  Unit:     Score 0–100
  Weight:   MEDIUM (2.0)
  Trust:    Auto-verified
  Decay:    YES (λ=0.05)

SIG_DOJO_PEER_SCORE_AVG
  Source:   Scoring Engine (peer + mentor merge)
  Unit:     Score 0–100
  Weight:   HIGH (3.0)
  Trust:    Variance anomaly detection passed
  Decay:    YES (λ=0.04)
```

### IV.E — Society Franchise Signals (DIM_10)

```
SIG_SOC_BATCH_COMPLETION_RATE
  Source:   Workshop & Batch Layer (society namespace)
  Unit:     Percentage 0–100
  Weight:   HIGH (3.0)
  Trust:    Coordinator verification required
  Decay:    YES (λ=0.03)
  Offline:  YES — synced via CouchDB replication

SIG_SOC_TOURNAMENT_PARTICIPATION_RATE
  Source:   Tournament Engine Layer (society namespace)
  Unit:     Percentage 0–100
  Weight:   HIGH (3.0)
  Trust:    Master Organizer sign-off required
  Decay:    YES (λ=0.04)
  Offline:  YES

SIG_SOC_COMMISSION_COMPLIANCE_RATE
  Source:   Commission & Finance Layer (society namespace)
  Unit:     Percentage 0–100
  Weight:   HIGH (3.0)
  Trust:    Auto-verified (Temporal payout workflow)
  Decay:    YES (λ=0.06)
  Offline:  NO (finance signals require online sync before scoring)

SIG_SOC_GOVT_CSR_SCHEME_UPTAKE
  Source:   Govt + CSR Layer (society namespace)
  Unit:     Count
  Weight:   MEDIUM (2.0)
  Trust:    Scheme documentation approval required
  Decay:    NO
  Offline:  YES

SIG_SOC_STUDENT_ENROLLMENT_COUNT
  Source:   Society Domain Layer
  Unit:     Count
  Weight:   MEDIUM (2.0)
  Trust:    Auto-verified (Keycloak SOCIETY_ADMIN role binding)
  Decay:    YES (λ=0.02)
  Offline:  YES
```

---

## SECTION V — RANK COMPUTATION ENGINE

### V.A — Architecture Constraint

```
RULE: ALL rank computation is deterministic rule-engine only.
      No ML inference. No LLM scoring. No confidence scores.
      No admin-defined manual overrides outside declared formula.
      Identical input → Identical output — always.
      Violation → STOP EXECUTION (R28-1)
```

### V.B — Base Score Formula

```
base_score(entity, dimension, scope, date) =
  Σ [ normalize(signal_value[i]) × weight[i] × recency_decay(signal_timestamp[i]) × trust_multiplier(trust_verified[i]) ]

WHERE:

normalize(v) = (v − min_v) / (max_v − min_v) × 100
  min_v, max_v = percentile 5th and 95th of same dimension + scope
  (Winsorized normalization to suppress outlier distortion)

weight[i] = base_weight from leaderboard_trust_weights
  HIGH   = 3.0
  MEDIUM = 2.0
  LOW    = 1.0

recency_decay(t) = e^(−λ × days_between(t, computation_date))
  λ = per-signal decay constant from leaderboard_trust_weights

trust_multiplier(verified) =
  IF trust_verified = TRUE  → trust_multiplier from registry (default 1.5)
  IF trust_verified = FALSE → 1.0
  (Unverified signals count at face value; verified signals amplified)
```

### V.C — Trust Penalty Adjustment

```
trust_score(entity) = base_score(entity)
  − Σ [ manipulation_penalty(log) × severity_weight(log.severity) ]

WHERE:

manipulation_penalty(log) =
  IF log.action_taken = SIGNALS_EXCLUDED → 0 (already excluded from base)
  IF log.action_taken = FLAGGED → 5 points per unresolved flag
  IF log.action_taken = SUPPRESSED → entity removed from public leaderboard

severity_weight(s) = s × 2.0
  (Severity 5 = 10.0 point multiplier per unresolved flag)
```

### V.D — Tier Label Assignment

```
After ranking all entities in a dimension + scope:

Percentile 91–100 → ELITE
Percentile 71–90  → TOP
Percentile 41–70  → MID
Percentile 21–40  → RISING
Percentile 1–20   → BOTTOM

RISING FLAG:
IF rank_delta ≤ -5 (improved by 5+ positions in one cycle)
  AND current tier IN (RISING, MID)
  → rising_flag = TRUE

ELITE BADGE:
IF entity remains in ELITE tier for ≥ 3 consecutive snapshots
  → Emit leaderboard.elite_sustained event
  → Trigger PRIVILEGE_02: VERIFIED_EXCELLENCE_BADGE (via Competition Agent)
```

### V.E — Offline Signal Integration Rule

```
FOR dimensions with offline_enabled = TRUE (DIM_10, DIM_11):

1. Offline signals arrive via CouchDB → Kafka replication topic
2. Signal ingestion gateway validates signal timestamp
3. IF signal_timestamp > last_online_sync_timestamp
   → Mark as offline_originated = TRUE
4. IF signal_timestamp outside current scoring window by > 7 days
   → Reject with STALE_OFFLINE_SIGNAL error
5. Finance-category signals (SIG_SOC_COMMISSION_COMPLIANCE_RATE)
   → ALWAYS require online sync confirmation before acceptance
   → Offline finance signals held in leaderboard_offline_sync_queue
      until Temporal payout workflow confirms sync
6. All offline signals included in next scheduled scoring run
7. Offline-originated signals flagged in snapshot with offline_fraction metric
```

---

## SECTION VI — REAL-TIME RANK FEED SYSTEM

### VI.A — Feed Architecture

```
REAL-TIME FEED STACK:
  WebSocket server (per dimension)
  Redis Pub/Sub (rank change broadcast channel)
  Redis sorted set (live rank state per dimension+scope)
  Kafka consumer (leaderboard.rank_changed events)

FLOW:
  1. Snapshot write completes
  2. Agent emits leaderboard.rank_changed to Kafka
  3. WebSocket service consumes event
  4. Updates Redis sorted set for dimension+scope
  5. Broadcasts delta to all subscribed WebSocket clients
  6. Client applies rank delta animation

SUBSCRIPTIONS:
  /ws/leaderboard/{dimension_id}/{scope_id}
  Authentication: JWT required
  Scope access: validated against RBAC (audience_flags)
```

### VI.B — Redis State Structure

```
KEY PATTERN: leaderboard:{dimension_id}:{scope_id}:live
TYPE:        Redis Sorted Set
MEMBER:      entity_id
SCORE:       trust_score (float, higher = better rank)

KEY PATTERN: leaderboard:{dimension_id}:{scope_id}:meta:{entity_id}
TYPE:        Redis Hash
FIELDS:
  display_name, rank_position, previous_rank, rank_delta,
  tier_label, rising_flag, badge_flags, last_updated

KEY PATTERN: leaderboard:{dimension_id}:{scope_id}:snapshot_date
TYPE:        Redis String
VALUE:       ISO date of last snapshot

TTL:         leaderboard live keys → 48 hours (refreshed on each snapshot)
```

### VI.C — Feed Delivery Rules

```
RULE 01: Delta-only delivery
  WebSocket push contains ONLY changed entities in each broadcast
  Not full leaderboard — rank deltas only
  Client reconciles against local state

RULE 02: Burst protection
  If > 50 rank changes in single snapshot run for one scope:
  → Batch broadcast as single delta message
  → Label: BULK_UPDATE
  → Client shows "Leaderboard updated" toast, reloads full state

RULE 03: Dojo real-time exception
  DIM_06 and DIM_07 (Dojo) feeds are updated on EVERY match close
  Not batched to scheduled snapshot cycle
  Match close event → immediate Redis update → immediate broadcast

RULE 04: Offline dimension feeds
  DIM_10 and DIM_11 do NOT have real-time WebSocket feeds
  Refreshed only after daily sync completion
  Offline units do not see live updates
```

---

## SECTION VII — ANTI-MANIPULATION CONTROLS

### VII.A — Detection Rules (Deterministic — No Heuristics)

```
RULE M01: SIGNAL SPIKE DETECTION
  Trigger:   Any entity submits signal_type with value > 3×
             its own 30-day rolling average in a single 24h window
  Action:    signals flagged as trust_verified = FALSE
             → excluded from base_score for current snapshot
             → logged to leaderboard_manipulation_log with severity 2
             → emit leaderboard.manipulation_detected

RULE M02: VELOCITY CAP ENFORCEMENT
  Trigger:   trust_score increases by > 20 points in a single snapshot
  Action:    Score capped at previous + 20
             Overflow logged to audit trail
             Carry-forward evaluated in next snapshot

RULE M03: ENDORSEMENT CLUSTER DETECTION
  Trigger:   > 5 endorsement signals from entities sharing
             the same institution_id or ip_subnet in < 48 hours
  Action:    All endorsement signals from that cluster
             flagged as trust_verified = FALSE for current window
             Severity 3 logged

RULE M04: SELF-SERVICE SIGNAL REJECTION
  Trigger:   Signal source JWT belongs to same tenant as scored entity
             AND signal_type requires external verification
  Action:    Signal rejected at ingestion gateway
             Error: SELF_SERVICE_SIGNAL_REJECTED
             Severity 4 logged if repeated (> 3 attempts)

RULE M05: OFFLINE TIMESTAMP MANIPULATION
  Trigger:   Offline signal timestamp predates device's
             last known online sync by > 7 days
  Action:    Signal rejected
             Error: STALE_OFFLINE_SIGNAL
             Logged to manipulation log severity 1

RULE M06: CROSS-SCOPE SIGNAL FLOODING
  Trigger:   Same entity_id submits signals across > 10 distinct
             scope_ids in a single 24h window
  Action:    Signals beyond top 10 by trust_score are excluded
             Severity 2 logged — ops alert triggered
```

### VII.B — Suppression Rules

```
SUPPRESSION TRIGGER:
  IF entity has ≥ 3 unresolved manipulation_log entries
     of severity ≥ 3 within a 30-day window:
  → leaderboard_entries.suppressed = TRUE
  → Entity removed from leaderboard_live table
  → Entity NOT visible on any public leaderboard page
  → Entity CAN still be seen by UNIT_ADMIN in private dashboard
  → Suppression record written to audit log
  → Event emitted: leaderboard.entity_suppressed

SUPPRESSION LIFT:
  Requires ops console action (R40)
  Requires dual admin sign-off
  Produces audit log entry with both admin IDs
  Entity re-enters leaderboard at rank computed from
  last valid snapshot (no position memory of suppressed period)
```

---

## SECTION VIII — API CONTRACT REGISTRY

All endpoints mandatory. Absence → STOP EXECUTION.

```
GET    /leaderboard/{dimension_id}/global
  Auth:      AUTHENTICATED
  Query:     page, page_size, tier_filter
  Response:  LeaderboardPageResponse
  Cache:     Redis 5 minutes

GET    /leaderboard/{dimension_id}/scope/{scope_id}
  Auth:      AUTHENTICATED (scope access validated)
  Query:     page, page_size, tier_filter, rising_only
  Response:  ScopedLeaderboardResponse
  Cache:     Redis 5 minutes

GET    /leaderboard/{dimension_id}/scope/{scope_id}/public
  Auth:      PUBLIC
  Response:  PublicLeaderboardResponse
             (top 100 only; display_name + rank + tier_label + badge_flags)
  SEO:       YES — canonical URL, ISR revalidate 3600s

GET    /leaderboard/entity/{entity_id}/rank
  Auth:      UNIT_MEMBER | UNIT_ADMIN | PLATFORM_ADMIN
  Query:     dimension_id, scope_id
  Response:  EntityRankDetailResponse
             (full score breakdown + signal contributions + rank history)

GET    /leaderboard/entity/{entity_id}/history
  Auth:      UNIT_MEMBER | UNIT_ADMIN | PLATFORM_ADMIN
  Query:     dimension_id, scope_id, from_date, to_date
  Response:  RankHistoryResponse (paginated snapshots)

GET    /leaderboard/entity/{entity_id}/signals
  Auth:      UNIT_ADMIN | PLATFORM_ADMIN
  Query:     dimension_id, from_date, to_date
  Response:  SignalFeedResponse (all inbound signals with trust status)

POST   /leaderboard/signals/ingest
  Auth:      INTERNAL_SERVICE (signed JWT)
  Body:      SignalBatch (max 1000 per request)
  Response:  IngestionAcknowledgement
  Rate:      500 signals/second per source service

GET    /leaderboard/dimensions
  Auth:      AUTHENTICATED
  Response:  DimensionListResponse (active dimensions + audience access)

GET    /leaderboard/manipulation/flags
  Auth:      PLATFORM_ADMIN
  Query:     dimension_id, severity, resolved, from_date, to_date
  Response:  ManipulationFlagListResponse (paginated)

POST   /leaderboard/manipulation/resolve/{log_id}
  Auth:      PLATFORM_ADMIN (dual sign-off required)
  Body:      ResolutionRequest (resolution_note, action_taken)
  Response:  ResolutionResponse
  Audit:     YES — both admin IDs written

GET    /leaderboard/audit/{dimension_id}
  Auth:      PLATFORM_ADMIN
  Query:     entity_id, from_date, to_date
  Response:  AuditLogResponse (paginated, hash-chained)

GET    /leaderboard/offline/sync-queue
  Auth:      SOCIETY_ADMIN | PLATFORM_ADMIN
  Query:     sync_status, from_date
  Response:  OfflineSyncQueueResponse

POST   /leaderboard/offline/sync-confirm/{sync_id}
  Auth:      SOCIETY_ADMIN (Temporal workflow confirmation)
  Response:  SyncConfirmResponse
  Audit:     YES

GET    /leaderboard/seo/pages
  Auth:      PLATFORM_ADMIN
  Response:  SEOPageStatusResponse (all generated pages + last_invalidated)

POST   /leaderboard/seo/invalidate/{dimension_id}
  Auth:      PLATFORM_ADMIN | INTERNAL_SEO_HOOK
  Response:  InvalidationResponse
```

---

## SECTION IX — ASYNC EVENT PUBLISHING

### IX.A — Events Emitted by This Agent

```
leaderboard.snapshot_completed
  Payload: dimension_id, scope_id, snapshot_date,
           entities_ranked, computation_duration_ms

leaderboard.rank_changed
  Payload: dimension_id, scope_id, entity_id, entity_type,
           previous_rank, new_rank, rank_delta, tier_label,
           rising_flag, trust_score

leaderboard.tier_changed
  Payload: dimension_id, scope_id, entity_id,
           previous_tier, new_tier

leaderboard.elite_sustained
  Payload: dimension_id, scope_id, entity_id,
           consecutive_elite_snapshots

leaderboard.entity_suppressed
  Payload: dimension_id, entity_id, reason, severity,
           suppression_audit_id

leaderboard.entity_unsuppressed
  Payload: dimension_id, entity_id, reinstated_rank,
           resolution_audit_id

leaderboard.manipulation_detected
  Payload: dimension_id, entity_id, detection_rule,
           flagged_signal_count, severity

leaderboard.seo_page_invalidated
  Payload: dimension_id, scope_id, page_url

leaderboard.offline_sync_pending
  Payload: dimension_id, entity_id, signal_count, oldest_signal_at

leaderboard.snapshot_failed
  Payload: dimension_id, scope_id, failure_reason, failed_at
```

### IX.B — Consuming Services

```
Notification Service     → leaderboard.rank_changed (tier_changed only)
                           leaderboard.elite_sustained
                           leaderboard.entity_suppressed
                           leaderboard.manipulation_detected (severity ≥ 3)

Competition Agent        → leaderboard.elite_sustained
                           leaderboard.tier_changed
                           leaderboard.entity_suppressed

Admin Governance Service → leaderboard.manipulation_detected (severity ≥ 4)
                           leaderboard.entity_suppressed
                           leaderboard.snapshot_failed

Analytics Service        → ALL events → ClickHouse

Reputation Engine (R68)  → leaderboard.rank_changed
                           leaderboard.tier_changed

SEO Regeneration Hook    → leaderboard.snapshot_completed (SEO-indexed dims)
                           leaderboard.tier_changed

Billing Service          → leaderboard.elite_sustained
                           (triggers PRIVILEGE_08 reduced commission)

Society Domain Layer     → leaderboard.offline_sync_pending
                           leaderboard.snapshot_completed (DIM_10, DIM_11 only)
```

---

## SECTION X — UI SCREENS (MANDATORY)

Absence of any screen → STOP EXECUTION.

### X.A — Public SEO Screens (Next.js — R31 Governed)

```
SCREEN_P01: GLOBAL LEADERBOARD PAGE
  URL:         /leaderboard/{dimension_id}
  Visibility:  Public
  Content:     Top 100 entities — rank, display_name, tier badge,
               rising indicator, trust score (normalized display)
               Domain track filter, scope selector
               "How this is scored" transparency link
  SEO Meta:    Auto-generated per dimension
  Canonical:   YES (R43 governed)
  ISR:         Revalidate every 3600 seconds
  Schema.org:  ItemList structured data

SCREEN_P02: SCOPED LEADERBOARD PAGE
  URL:         /leaderboard/{dimension_id}/{scope_type}/{scope_id}
  Examples:    /leaderboard/institutions/state/maharashtra
               /leaderboard/students/institution/iit-bombay
  Visibility:  Public
  Content:     Scope-specific top 100 + scope description
  SEO Meta:    Auto-generated per scope
  Canonical:   YES
  ISR:         Revalidate every 3600 seconds

SCREEN_P03: ENTITY RANK PUBLIC PROFILE EMBED
  URL:         Embedded widget in /units/{unit_id}/competition
  Content:     Current rank, tier badge, rising indicator,
               dimension selector, historical chart (public view)
  Visibility:  Public
```

### X.B — Authenticated Screens (Flutter — R43 Governed)

```
SCREEN_A01: LEADERBOARD EXPLORER
  Access:      ALL AUTHENTICATED USERS
  Content:     Browse all dimensions available to user's role
               Filter by scope, tier, domain track
               Search entity by name
               Toggle: GLOBAL | MY INSTITUTION | MY DOMAIN
               Rising entities section (top rank_delta improvers)
               Live rank feed indicator (WebSocket connected)

SCREEN_A02: MY RANK DASHBOARD
  Access:      UNIT_MEMBER (personal ranks across all dimensions)
  Content:     Rank cards per dimension (current rank, tier, delta)
               Score breakdown radar chart (per signal category)
               Historical rank trajectory line chart (90 days)
               Active badges from rank-driven rewards
               "What moves my rank" explainer panel
               Next milestone: signals needed to reach next tier
               Manipulation flags panel (if any active — unit admin only)

SCREEN_A03: ENTITY RANK DEEP DIVE
  Access:      UNIT_ADMIN | PLATFORM_ADMIN
  Content:     Full signal feed (all inbound signals with trust status)
               Score computation breakdown (each signal contribution)
               Trust weight visualization
               Manipulation flag history
               Rank snapshot table (30 days)
               Export to CSV

SCREEN_A04: SOCIETY FRANCHISE LEADERBOARD
  Access:      FRANCHISE_OWNER | SOCIETY_ADMIN | MASTER_ORGANIZER
  Content:     DIM_10 and DIM_11 leaderboards scoped to society domain
               State-level + district-level toggle
               Offline sync status indicator per franchise
               Pending sync queue count
               Coach individual rankings (DIM_11)
               Commission compliance rank signal breakdown

SCREEN_A05: LEADERBOARD ADMIN CONSOLE
  Access:      PLATFORM_ADMIN
  Content:     All dimension status + last snapshot timestamps
               Failed snapshot alerts
               Manipulation flag triage queue
               Suppressed entity management
               Offline sync queue management
               Signal type weight editor (audit-logged)
               SEO page generation status + manual invalidate
               Audit log explorer (hash chain viewer)
               Dual sign-off suppression lift workflow
```

---

## SECTION XI — OBSERVABILITY REQUIREMENTS

Absence of any metric/log requirement → STOP EXECUTION.

```
METRICS (Prometheus):
leaderboard_snapshot_runs_total          counter  (label: dimension_id, status)
leaderboard_snapshot_duration_ms         histogram (label: dimension_id)
leaderboard_signals_ingested_total       counter  (label: dimension_id, signal_type)
leaderboard_signals_rejected_total       counter  (label: reason)
leaderboard_rank_changes_total           counter  (label: dimension_id, direction: up|down)
leaderboard_tier_changes_total           counter  (label: dimension_id, from_tier, to_tier)
leaderboard_manipulation_flags_total     counter  (label: detection_rule, severity)
leaderboard_entities_suppressed_active   gauge    (label: dimension_id)
leaderboard_websocket_connections_active gauge    (label: dimension_id)
leaderboard_offline_sync_queue_depth     gauge    (label: dimension_id)
leaderboard_seo_pages_stale_count        gauge    (label: dimension_id)

LOGS (Loki):
- Every snapshot run start / end (dimension, scope, entity_count, duration)
- Every signal rejection with reason + entity_id
- Every rank change of tier_label transition
- Every manipulation flag creation
- Every suppression / unsuppression with admin_ids
- Every audit hash mismatch [CRITICAL]
- Every failed offline sync

TRACING (OpenTelemetry / Grafana Tempo):
- Snapshot computation span per dimension (entity_count attribute)
- Signal ingestion batch span (batch_size attribute)
- WebSocket broadcast span (scope_id, delta_count attributes)
- Offline sync span (sync_id, signal_count attributes)

DASHBOARDS (Grafana — Required):
Board 01: LEADERBOARD SYSTEM HEALTH
  · Snapshot success rate per dimension (24h rolling)
  · Signal ingestion rate (per minute)
  · Rank changes per hour heatmap
  · WebSocket active connections
  · Manipulation flag rate trend

Board 02: TRUST INTEGRITY MONITOR
  · Manipulation flag breakdown by detection rule
  · Suppressed entities count over time
  · Trust_verified vs unverified signal ratio per dimension
  · Offline sync queue depth trend

Board 03: SOCIETY DOMAIN LEADERBOARD OPS
  · Offline sync lag (hours since last sync per franchise)
  · Pending finance signal queue depth
  · Franchise rank distribution histogram

ALERTS:
leaderboard.snapshot_failed              → PagerDuty CRITICAL
leaderboard_audit_hash_mismatch          → PagerDuty CRITICAL
leaderboard_manipulation_flags_total
  > 15 per hour for same entity          → Ops WARN
leaderboard_offline_sync_queue_depth
  > 100 for any dimension                → Ops WARN
leaderboard_seo_pages_stale_count
  > 5 for SEO-indexed dimensions         → Ops INFO
```

---

## SECTION XII — OFFLINE DOMAIN ARCHITECTURE

### XII.A — Society / Franchise Offline Sync Design

```
OFFLINE STACK (society namespace, k8s):
  CouchDB (primary offline replication store)
    → Deployed at franchise edge nodes in rural zones
    → PostgreSQL logical replication for semi-connected areas

SYNC FLOW:
  1. Franchise coordinator records signals offline
     → Written to local CouchDB instance
  2. When connectivity restored:
     → CouchDB replication syncs to central CouchDB cluster
  3. Kafka consumer on central CouchDB change feed:
     → Transforms CouchDB documents → leaderboard signal format
     → Publishes to Kafka topic: leaderboard.offline.signals
  4. Signal ingestion gateway consumes Kafka topic
     → Validates offline_timestamp (max 7-day staleness rule)
     → Validates coordinator Keycloak role (COORDINATOR | SOCIETY_ADMIN)
     → Inserts to leaderboard_signals with offline_originated = TRUE
     → Updates leaderboard_offline_sync_queue status → SYNCED
  5. Finance signals additionally routed through Temporal workflow
     → Payout workflow confirms revenue data integrity
     → Only after Temporal confirmation → finance signal accepted

CONFLICT RESOLUTION:
  IF two offline nodes submit conflicting values for same
  entity + signal_type + time window:
  → Lower value is accepted (conservative scoring)
  → Conflict logged to leaderboard_offline_sync_queue
     with sync_status = CONFLICT
  → Society Admin alerted via Notification Service
  → Manual resolution via ops console (SCREEN_A05)
```

---

## SECTION XIII — SEO PAGE GOVERNANCE

### XIII.A — Generation Rules

```
GENERATION TRIGGER:
  On event: leaderboard.snapshot_completed
  IF dimension_id.seo_indexed = TRUE
  → Generate / revalidate Next.js ISR page for:
     · Global dimension page
     · All active scope pages for dimension
     · All entity rank profile embeds for top 100 entities

GENERATION OUTPUT per page:
  · HTML-rendered leaderboard table
  · Schema.org ItemList structured data
  · Title: "[Dimension Label] Leaderboard — [Scope] | EcoSkiller"
  · Meta description: auto-generated from top 3 entity names + tier
  · Canonical URL (no scope duplication)
  · Sitemap.xml entry added
  · robots.txt: Allow for public dimensions
                Disallow for DIM_08 (GD_COMPLIANCE — private)

INVALIDATION TRIGGER:
  On event: leaderboard.entity_suppressed
  → Immediate ISR revalidation of all pages containing suppressed entity
  On event: leaderboard.manipulation_detected (severity ≥ 4)
  → Immediate revalidation of affected entity's public profile
```

---

## SECTION XIV — INTERN EXECUTION MAPPING

All steps intern-executable without senior DevOps. Absence → STOP EXECUTION.

### XIV.A — Run Agent Locally

```
Step 1: Navigate to service
  cd /backend/services/leaderboard_agent/

Step 2: Create virtual environment
  python -m venv venv && source venv/bin/activate

Step 3: Install dependencies
  pip install -r requirements.txt

Step 4: Copy env template
  cp .env.example .env
  # Set: DB_URL, REDIS_URL, KAFKA_BROKER, COUCHDB_URL (for society dims)

Step 5: Run migrations
  alembic upgrade head

Step 6: Seed dimension registry
  python seed_dimensions.py

Step 7: Start agent
  uvicorn main:app --reload --port 8031

Expected Output:
  LEADERBOARD AGENT ONLINE
  Dimensions loaded: 12
  WebSocket endpoint: ws://localhost:8031/ws/leaderboard/
  Docs: http://localhost:8031/docs
```

### XIV.B — Test a Snapshot Run

```
Step 1: Ingest test signals
  python tools/inject_test_signals.py --dimension=DIM_01 --entity_count=20

Step 2: Trigger snapshot
  python tools/trigger_snapshot.py --dimension=DIM_01 --scope=global

Step 3: View leaderboard
  GET http://localhost:8031/leaderboard/DIM_01/global

Expected Output:
  20 entities ranked
  Tier labels assigned
  trust_score computed per entity
  rising_flag set where applicable

Failure → STOP → REPORT SNAPSHOT_FAILURE
```

### XIV.C — Test Offline Sync (Society Domain)

```
Step 1: Start local CouchDB
  docker run -p 5984:5984 couchdb:3

Step 2: Insert offline signal document
  python tools/inject_offline_signal.py --dimension=DIM_10

Step 3: Trigger sync
  python tools/trigger_offline_sync.py

Step 4: Confirm signal accepted
  GET http://localhost:8031/leaderboard/offline/sync-queue

Expected Output:
  Signal status: SYNCED
  leaderboard_signals table updated
  offline_originated = TRUE
```

---

## SECTION XV — ENFORCEMENT SEAL

```
SECTION XV — LEADERBOARD_MANAGEMENT_AGENT ENFORCEMENT SEAL
Status: ACTIVE · FINAL · LOCKED

No system deployment may proceed unless all of the following
conditions are met and verified:

✔ All database tables in Section III exist with migrations
✔ All signal types in Section IV are registered in signal registry
✔ Rank computation formula in Section V is implemented as
  rule-engine only — no ML, no AI inference (R28-1)
✔ Trust weighting system in Section V.B is active per signal type
✔ Anti-manipulation rules in Section VII are all enforced
✔ All API endpoints in Section VIII are implemented and contracted
✔ Real-time WebSocket feed system in Section VI is operational
✔ Offline sync architecture in Section XII is deployed for
  society-namespace dimensions
✔ All async events in Section IX are published and consumed
✔ All UI screens in Section X are implemented and wired
✔ All observability requirements in Section XI are live
✔ All SEO governance rules in Section XIII are active
✔ All intern execution steps in Section XIV pass without error
✔ Audit hash chain is unbroken from first write
✔ Society domain offline-sync queue is monitored by Grafana

Failure in any → STOP EXECUTION
               → REPORT LEADERBOARD_MANAGEMENT_AGENT_INCOMPLETE
               → NO DEPLOYMENT CLAIM PERMITTED

This agent is bound to:
R2   — Domain Data Models
R5   — Workflow State Machines
R10  — Security Policies
R21  — Internal Operations Console
R28  — Intelligence Cost Optimization Law
R31  — Dual Frontend SEO + Application Architecture Law
R38  — Master Bug & Failure Registry Law
R39  — Core Inbuilt Platform Tools
R40  — Admin & Ops Console
R49  — Automatic Contract Validator
R50  — Automated QA Test Generator
R51  — Anti-Spam & Platform Abuse Prevention
R53  — Status, Badge & Level Progression
R57  — Daily Habit & Streak Engine
R62  — Public Transparency & Trust Report
R68  — Reputation & Trust Loop
R79  — Trust & Reputation Amplification
R87  — Trainer Competition & Gamification
INTER_UNIT_COMPETITION_AGENT

Violation → STOP → REPORT → NO CLAIM OF COMPLETION
```

---

## SECTION XVI — ANTIGRAVITY CONTRACT SUMMARY

```
The LEADERBOARD_MANAGEMENT_AGENT enforces antigravity
through six governing principles:

1. MULTI-DIMENSIONAL TRUTH
   A single entity is ranked across 12 dimensions simultaneously.
   There is no single score that can be gamed.
   Excellence must be demonstrated across verified behavioral axes.

2. TRUST AMPLIFICATION
   Verified signals count more than unverified signals.
   Platform resources flow toward entities whose performance
   is independently confirmed — not self-reported.

3. RECENCY SOVEREIGNTY
   Historical dominance decays continuously.
   Present behavior always outweighs past reputation
   at the rate declared by the decay constants.
   There is no coast mode.

4. AUDIENCE-SEGMENTED VISIBILITY
   Recruiter leaderboard ≠ Student leaderboard ≠ Admin leaderboard.
   Each audience sees what is relevant to their trust decision.
   No information pollution across audience types.

5. OFFLINE INCLUSION
   Society franchise and coaching networks are ranked
   with the same rigor as online-native entities.
   Offline participation creates competitive incentive
   for the hardest-to-reach adoption layer.

6. TRANSPARENT MECHANICS
   Every entity can see the exact signal breakdown
   that produced their rank.
   Trust is not a black box.
   It is a formula, with every input source declared
   and every computation logged.
```

---

```
DOCUMENT STATUS:    SEALED · LOCKED · PRODUCTION-READY SPECIFICATION
AGENT VERSION:      v1.0
MASTER PROMPT:      ECOSKILLER v12.0+
MUTATION POLICY:    ADD-ONLY via version bump
ISSUED:             2026-03-04
NEXT REVIEW:        Via version bump only — no silent edits permitted
BINDING:            INTER_UNIT_COMPETITION_AGENT v1.0 +
                    ECOSKILLER Society Architecture v1.0 +
                    ECOSKILLER Infrastructure v8 (k3s confirmed)
```
