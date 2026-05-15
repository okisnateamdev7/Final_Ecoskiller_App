# 🔒 ECOSKILLER — ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
## INTER_UNIT_COMPETITION_AGENT
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
```

---

## SECTION IDENTITY

```
Agent Name:       INTER_UNIT_COMPETITION_AGENT
Agent Class:      ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
Layer:            Antigravity
Parent System:    ECOSKILLER MASTER EXECUTION PROMPT v12.0+
Depends On:       R2, R5, R10, R21, R28, R39, R40, R49, R50, R53, R87
Produces:         Competition events, scoring records, trust signals,
                  leaderboard deltas, audit proofs, XP cascades,
                  reward disbursements, behavioral enforcement logs
Consumed By:      Intelligence Engine, Billing Service, Admin Console,
                  Notification Service, Analytics Service, Reputation Engine
```

---

## SECTION I — AGENT PURPOSE & ANTIGRAVITY DEFINITION

### I.A — What Antigravity Means in This System

Antigravity is the force that prevents platform decay.

Without antigravity, the following failures occur naturally over time:

- Units (Institutions / Companies / Trainer Groups / Student Cohorts) stop performing after initial onboarding
- Engagement decays without external pressure
- Quality standards drift downward without consequence
- Trust scores inflate without challenge
- Users coast on historical reputation, blocking new entrants
- Marketplace liquidity collapses as dominant players crowd out competition

The INTER_UNIT_COMPETITION_AGENT is the antigravity engine.

It enforces competitive pressure between peer units, rewards excellence with systemic amplification, penalizes stagnation with visibility reduction, and continuously redistributes trust capital based on verified behavioral evidence — not tenure.

**Antigravity has no sentiment. It has only rules, enforcement, and logs.**

### I.B — Agent Mandate

```
MANDATE:
The INTER_UNIT_COMPETITION_AGENT must:

1. Define competitive units across all platform entity types
2. Assign each unit a live performance composite score
3. Run structured competition cycles with deterministic evaluation
4. Enforce ranking redistribution based solely on verified behavioral metrics
5. Amplify high-performing units through systemic privileges
6. Apply graduated consequences to stagnating or declining units
7. Produce immutable audit logs for every competitive action
8. Surface all results publicly through trust-infrastructure overlays
9. Never use subjective criteria, AI inference, or human judgment
   for scoring, ranking, or privilege assignment
10. Operate continuously — not episodically
```

### I.C — Non-Negotiable Constraints

```
FORBIDDEN:
- Subjective scoring by admins or moderators
- AI-generated performance labels or confidence scores
- Manual ranking overrides without audit trail
- Grace periods that bypass enforcement
- Retroactive score adjustments without signed event evidence
- Competition suppression for paying tenants

REQUIRED:
- Rule engine only (R28-1) for all scoring and ranking
- Immutable audit logs (R10, R39) on every competitive event
- Deterministic output: identical input → identical output
- Full intern-executable observability of all agent state
```

---

## SECTION II — COMPETITIVE UNIT TAXONOMY

### II.A — Unit Type Registry

Every competitive unit in the system must be registered before
it can participate in any competition cycle.

```
UNIT_TYPE_01: INSTITUTION
  Scope: Colleges, Universities, Schools
  Identifiers: institution_id, verified_domain
  Sub-units: Departments, Batches

UNIT_TYPE_02: ENTERPRISE
  Scope: Corporations, SMEs
  Identifiers: company_id, verified_domain
  Sub-units: Departments, Teams

UNIT_TYPE_03: TRAINER_GROUP
  Scope: Cohorts of verified trainers on same domain track
  Identifiers: trainer_group_id, domain_track
  Sub-units: Individual trainer profiles

UNIT_TYPE_04: STUDENT_COHORT
  Scope: Verified students grouped by batch + institution
  Identifiers: cohort_id, batch_year, institution_id
  Sub-units: Individual student profiles

UNIT_TYPE_05: DOJO_TEAM
  Scope: Registered team groups in Dojo match arenas
  Identifiers: team_id, domain_track
  Sub-units: Individual Dojo participants

UNIT_TYPE_06: RECRUITER_PANEL
  Scope: Verified recruiter groups operating within enterprise tenants
  Identifiers: panel_id, company_id
  Sub-units: Individual recruiter profiles
```

### II.B — Unit Registration Rules

```
REGISTRATION_RULES:

1. Every unit must be registered before the competition cycle starts
2. Registration requires verified domain or tenant binding
3. Unverified units cannot participate
4. Units with < minimum_member_threshold are ineligible
5. minimum_member_threshold is declared per unit type in
   CompetitionCycleConfig
6. Units may belong to exactly ONE competition tier at a time
7. Tier assignment is computed — not manual
```

---

## SECTION III — DATABASE SCHEMA (MANDATORY)

All tables below are non-negotiable. Absence of any table → STOP EXECUTION.

### III.A — Core Competition Tables

```sql
-- Competition cycle definition
competition_cycles (
  cycle_id          UUID PRIMARY KEY,
  cycle_name        TEXT NOT NULL,
  unit_type         TEXT NOT NULL,       -- references UNIT_TYPE_XX
  domain_track      TEXT,                -- NULL = cross-domain
  start_date        DATE NOT NULL,
  end_date          DATE NOT NULL,
  scoring_formula   JSONB NOT NULL,      -- immutable once cycle starts
  tier_count        INTEGER NOT NULL,
  status            TEXT NOT NULL,       -- PENDING | ACTIVE | CLOSED | ARCHIVED
  created_at        TIMESTAMP NOT NULL,
  locked_at         TIMESTAMP            -- set when cycle becomes ACTIVE
)

-- Unit enrollment in a cycle
competition_enrollments (
  enrollment_id     UUID PRIMARY KEY,
  cycle_id          UUID REFERENCES competition_cycles,
  unit_id           UUID NOT NULL,
  unit_type         TEXT NOT NULL,
  tier_assigned     INTEGER NOT NULL,
  enrolled_at       TIMESTAMP NOT NULL,
  enrollment_hash   TEXT NOT NULL        -- sha256 of enrollment state
)

-- Raw behavioral events fed into scoring
competition_events (
  event_id          UUID PRIMARY KEY,
  cycle_id          UUID REFERENCES competition_cycles,
  unit_id           UUID NOT NULL,
  unit_type         TEXT NOT NULL,
  event_type        TEXT NOT NULL,       -- references EVENT_TYPE registry
  event_value       NUMERIC NOT NULL,
  event_source      TEXT NOT NULL,       -- service that emitted event
  event_timestamp   TIMESTAMP NOT NULL,
  verified          BOOLEAN NOT NULL DEFAULT FALSE,
  verification_hash TEXT
)

-- Computed score snapshots (immutable after write)
competition_scores (
  score_id          UUID PRIMARY KEY,
  cycle_id          UUID REFERENCES competition_cycles,
  unit_id           UUID NOT NULL,
  unit_type         TEXT NOT NULL,
  snapshot_date     DATE NOT NULL,
  raw_score         NUMERIC NOT NULL,
  normalized_score  NUMERIC NOT NULL,
  tier_rank         INTEGER NOT NULL,
  global_rank       INTEGER NOT NULL,
  score_hash        TEXT NOT NULL,       -- sha256 of score state
  computed_at       TIMESTAMP NOT NULL
)

-- Leaderboard state (materialized, refreshed on scoring run)
competition_leaderboard (
  leaderboard_id    UUID PRIMARY KEY,
  cycle_id          UUID REFERENCES competition_cycles,
  unit_type         TEXT NOT NULL,
  tier              INTEGER NOT NULL,
  unit_id           UUID NOT NULL,
  display_name      TEXT NOT NULL,
  current_rank      INTEGER NOT NULL,
  previous_rank     INTEGER,
  rank_delta        INTEGER,             -- positive = improved
  composite_score   NUMERIC NOT NULL,
  badge_flags       JSONB,
  last_updated      TIMESTAMP NOT NULL
)

-- Privilege grants resulting from competition outcomes
competition_privilege_grants (
  grant_id          UUID PRIMARY KEY,
  cycle_id          UUID REFERENCES competition_cycles,
  unit_id           UUID NOT NULL,
  privilege_type    TEXT NOT NULL,       -- references PRIVILEGE_TYPE registry
  granted_at        TIMESTAMP NOT NULL,
  expires_at        TIMESTAMP,
  revoked           BOOLEAN DEFAULT FALSE,
  revoked_at        TIMESTAMP,
  grant_hash        TEXT NOT NULL
)

-- Consequence applications for bottom-tier units
competition_consequences (
  consequence_id    UUID PRIMARY KEY,
  cycle_id          UUID REFERENCES competition_cycles,
  unit_id           UUID NOT NULL,
  consequence_type  TEXT NOT NULL,       -- references CONSEQUENCE_TYPE registry
  severity_level    INTEGER NOT NULL,    -- 1-5
  applied_at        TIMESTAMP NOT NULL,
  lifted_at         TIMESTAMP,
  reason_code       TEXT NOT NULL,
  consequence_hash  TEXT NOT NULL
)

-- Immutable audit log (every agent action)
competition_audit_log (
  audit_id          UUID PRIMARY KEY,
  cycle_id          UUID,
  unit_id           UUID,
  agent_action      TEXT NOT NULL,
  action_input      JSONB NOT NULL,
  action_output     JSONB NOT NULL,
  action_timestamp  TIMESTAMP NOT NULL,
  actor             TEXT NOT NULL,       -- 'AGENT' | 'SYSTEM' | 'ADMIN'
  admin_id          UUID,               -- populated only when actor = ADMIN
  audit_hash        TEXT NOT NULL        -- sha256 chain link
)
```

---

## SECTION IV — EVENT TYPE REGISTRY

All events consumed by the scoring engine must be declared here.
Undeclared event types are rejected by the event ingestion gateway.

### IV.A — Institution Events

```
EVT_INST_PLACEMENT_VERIFIED
  Description: A student placement was verified by both company and student
  Source:       Application Service
  Value Unit:   Count
  Weight Class: HIGH

EVT_INST_COURSE_COMPLETION_RATE
  Description: Percentage of enrolled students completing a course
  Source:       Education Service
  Value Unit:   Percentage (0-100)
  Weight Class: HIGH

EVT_INST_GD_PARTICIPATION_RATE
  Description: Percentage of students participating in scheduled GD sessions
  Source:       Voice GD Orchestrator
  Value Unit:   Percentage (0-100)
  Weight Class: MEDIUM

EVT_INST_COMPLAINT_RESOLUTION_RATE
  Description: Percentage of anonymous complaints resolved within SLA
  Source:       Admin Governance Service
  Value Unit:   Percentage (0-100)
  Weight Class: HIGH

EVT_INST_SKILL_PASSPORT_ISSUANCE
  Description: Number of verified skill passports issued to students
  Source:       Certification & Belt Engine
  Value Unit:   Count
  Weight Class: MEDIUM

EVT_INST_RECRUITER_ENGAGEMENT
  Description: Number of unique verified recruiters who engaged with
               students from this institution
  Source:       Job Service + Application Service
  Value Unit:   Count
  Weight Class: HIGH

EVT_INST_STUDENT_RETENTION_RATE
  Description: Percentage of enrolled students still active at cycle end
  Source:       User Service
  Value Unit:   Percentage (0-100)
  Weight Class: MEDIUM
```

### IV.B — Enterprise Events

```
EVT_ENT_OFFER_ACCEPTANCE_RATE
  Description: Percentage of extended offers accepted by candidates
  Source:       Application Service
  Value Unit:   Percentage (0-100)
  Weight Class: HIGH

EVT_ENT_TIME_TO_HIRE
  Description: Average days from job post to offer acceptance
  Source:       Job Service
  Value Unit:   Days (lower = better, inverted in scoring)
  Weight Class: HIGH

EVT_ENT_GD_FAIRNESS_SCORE
  Description: Deterministic GD fairness score from Voice GD Orchestrator
  Source:       Voice GD Orchestrator
  Value Unit:   Score (0-100)
  Weight Class: HIGH

EVT_ENT_CANDIDATE_FEEDBACK_SCORE
  Description: Average candidate feedback score for hiring process
  Source:       Application Service
  Value Unit:   Score (0-100)
  Weight Class: MEDIUM

EVT_ENT_DISPUTE_RESOLUTION_RATE
  Description: Percentage of hiring disputes resolved within SLA
  Source:       Admin Governance Service
  Value Unit:   Percentage (0-100)
  Weight Class: HIGH

EVT_ENT_VERIFIED_JOB_POST_RATE
  Description: Percentage of job posts that passed verification without rejection
  Source:       Job Service
  Value Unit:   Percentage (0-100)
  Weight Class: MEDIUM
```

### IV.C — Trainer Group Events

```
EVT_TRN_STUDENT_COMPLETION_RATE
  Description: Average course completion rate across all trainer group courses
  Source:       Education Service
  Value Unit:   Percentage (0-100)
  Weight Class: HIGH

EVT_TRN_PLACEMENT_CONTRIBUTION
  Description: Number of students from group courses who received verified placements
  Source:       Application Service
  Value Unit:   Count
  Weight Class: HIGH

EVT_TRN_CONTENT_QUALITY_SCORE
  Description: Aggregated student feedback score on course content quality
  Source:       Education Service
  Value Unit:   Score (0-100)
  Weight Class: HIGH

EVT_TRN_PEER_ENDORSEMENT_COUNT
  Description: Number of verified peer endorsements received by group members
  Source:       R81 Trainer Reputation Engine
  Value Unit:   Count
  Weight Class: MEDIUM

EVT_TRN_LIVE_SESSION_ATTENDANCE
  Description: Average attendance rate across all live sessions in cycle
  Source:       Live Teaching System (R83)
  Value Unit:   Percentage (0-100)
  Weight Class: MEDIUM

EVT_TRN_COMPLAINT_RATE
  Description: Number of substantiated complaints filed against group members
  Source:       Admin Governance Service
  Value Unit:   Count (inverted in scoring — lower = better)
  Weight Class: HIGH
```

### IV.D — Student Cohort Events

```
EVT_COH_DOJO_MATCH_PARTICIPATION
  Description: Percentage of cohort members participating in Dojo matches
  Source:       Dojo Match Engine
  Value Unit:   Percentage (0-100)
  Weight Class: HIGH

EVT_COH_GD_COMPLIANCE_SCORE
  Description: Average GD compliance score from Voice GD Orchestrator
  Source:       Voice GD Orchestrator
  Value Unit:   Score (0-100)
  Weight Class: HIGH

EVT_COH_SKILL_PASSPORT_RATE
  Description: Percentage of cohort members with at least one skill passport
  Source:       Certification & Belt Engine
  Value Unit:   Percentage (0-100)
  Weight Class: MEDIUM

EVT_COH_PEER_PROJECT_DELIVERY
  Description: Number of verified peer projects delivered and published
  Source:       Project Execution Engine
  Value Unit:   Count
  Weight Class: HIGH

EVT_COH_STREAK_MAINTENANCE_RATE
  Description: Percentage of cohort maintaining ≥7-day activity streak
  Source:       R57 / R95 Streak Engine
  Value Unit:   Percentage (0-100)
  Weight Class: MEDIUM

EVT_COH_PLACEMENT_RATE
  Description: Percentage of eligible cohort members with verified placements
  Source:       Application Service
  Value Unit:   Percentage (0-100)
  Weight Class: HIGH
```

---

## SECTION V — SCORING FORMULA ENGINE

### V.A — Formula Architecture

```
RULE: ALL scoring must use deterministic rule-engine computation only.
      No ML inference. No AI scoring. No human judgment.
      Violation → STOP EXECUTION
```

### V.B — Composite Score Formula (Universal)

```
composite_score = Σ (event_value[i] × weight[i] × recency_decay[i])
                  − Σ (penalty_value[j] × penalty_weight[j])

WHERE:

event_value[i]    = normalized value of event type i (0-100 scale)
weight[i]         = event type weight from EVENT_TYPE registry
                    HIGH   = 3.0
                    MEDIUM = 2.0
                    LOW    = 1.0
recency_decay[i]  = e^(−λ × days_since_event)
                    λ = 0.05 (standard decay constant)
                    Applied to ensure recent behavior weighted more heavily

penalty_value[j]  = raw penalty magnitude from consequence event j
penalty_weight[j] = severity_level of consequence (1-5 mapped to 1.0-5.0)
```

### V.C — Normalization Rule

```
normalized_value = (raw_value − min_value) / (max_value − min_value) × 100

WHERE:
min_value and max_value are computed from the CURRENT CYCLE cohort only.
No cross-cycle normalization.
No absolute benchmark normalization.
Ranking is always relative to enrolled peers in same tier.
```

### V.D — Tier Assignment Algorithm

```
TIER ASSIGNMENT runs after each scoring snapshot.

1. Sort all enrolled units by composite_score DESCENDING
2. Divide ranked list into tier_count equal segments
   Tier 1 = top segment (highest performers)
   Tier N = bottom segment (lowest performers)
3. If unit count is not divisible evenly:
   Remainder units go to middle tier(s)
4. Tie-breaking rule:
   Same score → alphabetical unit_id sort (deterministic)
5. Write new tier_assigned to competition_enrollments
6. Compute rank_delta = previous_rank - current_rank
7. Write leaderboard snapshot to competition_leaderboard
```

---

## SECTION VI — PRIVILEGE TYPE REGISTRY

Top-performing units receive systemic privileges.
All privileges must be declared in this registry.
Undeclared privilege types cannot be granted.

```
PRIVILEGE_01: FEATURED_LISTING_BOOST
  Description: Unit appears in featured placement zones on public pages
  Applies To:  Institution, Enterprise, Trainer Group
  Duration:    Remainder of competition cycle
  Visibility:  Public
  Trigger:     Tier 1 rank AND composite_score ≥ 80

PRIVILEGE_02: VERIFIED_EXCELLENCE_BADGE
  Description: Public badge displayed on unit profile and all member profiles
  Applies To:  All unit types
  Duration:    90 days post cycle close
  Visibility:  Public + SEO indexed
  Trigger:     Tier 1 completion

PRIVILEGE_03: EXPANDED_JOB_POST_QUOTA
  Description: Enterprise job post quota increased by 50%
  Applies To:  Enterprise
  Duration:    Next billing cycle
  Visibility:  Internal
  Trigger:     Tier 1 AND EVT_ENT_GD_FAIRNESS_SCORE ≥ 85

PRIVILEGE_04: PRIORITY_CANDIDATE_DISCOVERY
  Description: Enterprise's open roles surfaced first in candidate search
  Applies To:  Enterprise
  Duration:    Remainder of competition cycle
  Visibility:  Public
  Trigger:     Tier 1 rank

PRIVILEGE_05: TRAINER_AMPLIFIED_FEED_VISIBILITY
  Description: Trainer group content ranked higher in student feeds
  Applies To:  Trainer Group
  Duration:    30 days
  Visibility:  Internal algorithm
  Trigger:     Tier 1 AND EVT_TRN_CONTENT_QUALITY_SCORE ≥ 80

PRIVILEGE_06: COHORT_GD_PRIORITY_SCHEDULING
  Description: Student cohort receives priority time slots for GD sessions
  Applies To:  Student Cohort
  Duration:    Next GD scheduling cycle
  Visibility:  Internal
  Trigger:     EVT_COH_GD_COMPLIANCE_SCORE ≥ 90

PRIVILEGE_07: INSTITUTION_PLACEMENT_SPOTLIGHT
  Description: Institution featured in placement success pages and
               distributed to recruiter discovery feed
  Applies To:  Institution
  Duration:    Cycle duration + 30 days
  Visibility:  Public + SEO indexed
  Trigger:     EVT_INST_PLACEMENT_VERIFIED top quartile

PRIVILEGE_08: REDUCED_PLATFORM_COMMISSION
  Description: Platform commission on marketplace transactions reduced
               by 5% for cycle duration
  Applies To:  Institution, Enterprise, Trainer Group
  Duration:    Remainder of competition cycle
  Visibility:  Internal (billing ledger)
  Trigger:     Tier 1 completion for two consecutive cycles
```

---

## SECTION VII — CONSEQUENCE TYPE REGISTRY

Bottom-tier units receive structured consequences.
All consequences must be declared here.
Consequences are graduated by severity and cycle history.

```
CONSEQUENCE_01: VISIBILITY_REDUCTION
  Description: Unit's public listing deprioritized in discovery rankings
  Severity:    1
  Applies To:  All unit types
  Duration:    Remainder of current cycle
  Visibility:  Internal algorithm only (not disclosed to unit)
  Trigger:     Bottom tier (Tier N) for first time in cycle

CONSEQUENCE_02: BADGE_SUPPRESSION
  Description: Existing excellence badges hidden from public view
  Severity:    2
  Applies To:  All unit types
  Duration:    Until tier improvement
  Visibility:  Public (badge disappears silently)
  Trigger:     Bottom tier (Tier N) for two consecutive scoring snapshots

CONSEQUENCE_03: IMPROVEMENT_NOTICE
  Description: Formal improvement notice sent to unit admin
               Specifies which event metrics are below peer median
               Provides deterministic improvement criteria to exit notice
  Severity:    2
  Applies To:  All unit types
  Duration:    Until tier improvement or cycle close
  Visibility:  Unit admin only
  Trigger:     Bottom tier (Tier N) for two consecutive scoring snapshots

CONSEQUENCE_04: JOB_POST_QUOTA_RESTRICTION
  Description: Enterprise job post quota reduced by 25%
  Severity:    3
  Applies To:  Enterprise
  Duration:    Next billing cycle
  Visibility:  Internal
  Trigger:     Bottom tier (Tier N) for three consecutive scoring snapshots
               AND EVT_ENT_GD_FAIRNESS_SCORE < 50

CONSEQUENCE_05: GD_SESSION_AUDIT_REQUIREMENT
  Description: All future GD sessions flagged for orchestrator audit review
               No human reviewer — audit is automated rule inspection only
  Severity:    3
  Applies To:  Institution, Enterprise
  Duration:    Until compliance threshold restored
  Visibility:  Internal ops console
  Trigger:     EVT_ENT_GD_FAIRNESS_SCORE < 40 for two consecutive snapshots

CONSEQUENCE_06: PUBLIC_PERFORMANCE_DISCLOSURE
  Description: Unit's tier ranking and composite score made visible
               on public profile page
  Severity:    4
  Applies To:  Institution, Enterprise
  Duration:    Until tier improvement
  Visibility:  Public
  Trigger:     Bottom tier (Tier N) for four consecutive scoring snapshots

CONSEQUENCE_07: PLACEMENT_PRIVILEGE_SUSPENSION
  Description: Unit removed from featured placement zones
               Candidate matching deprioritized
  Severity:    4
  Applies To:  Enterprise
  Duration:    Until return to Tier 2 or above
  Visibility:  Internal
  Trigger:     EVT_ENT_CANDIDATE_FEEDBACK_SCORE < 30 sustained

CONSEQUENCE_08: PROBATIONARY_STATUS
  Description: Unit placed on probation
               Admin console flag set to PROBATION
               Internal review triggered
               No automatic remediation — ops must confirm resolution
  Severity:    5
  Applies To:  All unit types
  Duration:    Until ops confirmation of resolution
  Visibility:  Internal ops console + unit admin
  Trigger:     Bottom tier (Tier N) for five consecutive scoring snapshots
               AND composite_score < 20
```

---

## SECTION VIII — COMPETITION CYCLE LIFECYCLE

### VIII.A — State Machine

```
CYCLE STATES:

PENDING
  ↓ (admin_declares_start OR automated_schedule trigger)
ACTIVE
  ↓ (scoring runs every 24h UTC)
  Leaderboard updates continuously
  Privilege grants applied
  Consequences enforced
  ↓ (end_date reached)
CLOSED
  ↓ (archival job runs)
ARCHIVED

TRANSITIONS:
PENDING → ACTIVE:   Requires enrollment minimum met per tier
                    Requires scoring_formula locked (immutable)
ACTIVE → CLOSED:    Automatic on end_date UTC 00:00:00
CLOSED → ARCHIVED:  Automatic 30 days after close date

FORBIDDEN:
CLOSED → ACTIVE (no re-open)
ARCHIVED → any state
```

### VIII.B — Daily Scoring Run (Cron Job)

```
Schedule: Daily UTC 02:00:00 (off-peak)
Execution:

1. Load all ACTIVE cycles
2. For each cycle:
   a. Load all competition_events since last snapshot
   b. Apply scoring_formula from cycle config (immutable)
   c. Compute composite_score per unit
   d. Normalize scores within cycle cohort
   e. Compute tier assignments
   f. Compute rank_delta
   g. Write competition_scores snapshot (immutable)
   h. Update competition_leaderboard (mutable materialized view)
   i. Evaluate privilege grant conditions
      → Write competition_privilege_grants where triggered
      → Revoke expired grants
   j. Evaluate consequence conditions
      → Write competition_consequences where triggered
      → Lift consequences where conditions no longer met
   k. Emit events:
      competition.scored
      competition.privilege_granted (if applicable)
      competition.consequence_applied (if applicable)
      competition.tier_changed (if applicable)
   l. Write audit log entry for full scoring run

3. If any step fails:
   → STOP scoring run for that cycle
   → Log failure to competition_audit_log
   → Emit competition.scoring_failed event
   → Alert ops console
   → Previous snapshot remains valid — do not write partial state
```

---

## SECTION IX — API CONTRACT REGISTRY

All endpoints below are mandatory. Absence → STOP EXECUTION.

```
POST   /competition/cycles/create
  Auth:        PLATFORM_ADMIN
  Body:        CycleCreateRequest
  Response:    CycleCreatedResponse
  Audit:       YES

GET    /competition/cycles
  Auth:        AUTHENTICATED
  Query:       unit_type, status, domain_track
  Response:    CycleListResponse

GET    /competition/cycles/{cycle_id}
  Auth:        AUTHENTICATED
  Response:    CycleDetailResponse

POST   /competition/cycles/{cycle_id}/enroll
  Auth:        UNIT_ADMIN
  Body:        EnrollmentRequest
  Response:    EnrollmentResponse
  Audit:       YES

GET    /competition/leaderboard/{cycle_id}
  Auth:        AUTHENTICATED
  Query:       tier, unit_type
  Response:    LeaderboardResponse
  Cache:       60 seconds

GET    /competition/leaderboard/{cycle_id}/public
  Auth:        PUBLIC (no auth required)
  Response:    PublicLeaderboardResponse (anonymized unit names for non-members)
  SEO:         YES — indexed page

GET    /competition/units/{unit_id}/score/{cycle_id}
  Auth:        UNIT_MEMBER | UNIT_ADMIN | PLATFORM_ADMIN
  Response:    UnitScoreDetailResponse

GET    /competition/units/{unit_id}/privileges
  Auth:        UNIT_ADMIN | PLATFORM_ADMIN
  Response:    PrivilegeGrantListResponse

GET    /competition/units/{unit_id}/consequences
  Auth:        UNIT_ADMIN | PLATFORM_ADMIN
  Response:    ConsequenceListResponse

POST   /competition/events/ingest
  Auth:        INTERNAL_SERVICE (signed JWT)
  Body:        CompetitionEventBatch
  Response:    IngestionAcknowledgement
  Rate Limit:  500 events/second per source service

GET    /competition/audit/{cycle_id}
  Auth:        PLATFORM_ADMIN
  Response:    AuditLogResponse (paginated)

GET    /competition/cycles/{cycle_id}/formula
  Auth:        AUTHENTICATED
  Response:    ScoringFormulaPublicView (weights visible, raw formula not)
```

---

## SECTION X — EVENT INGESTION GATEWAY

### X.A — Inbound Event Sources

```
Every platform service that produces behavioral events
must publish them to the Competition Event Bus.

SOURCE SERVICE → EVENT TYPES EMITTED:

Voice GD Orchestrator      → EVT_ENT_GD_FAIRNESS_SCORE
                              EVT_INST_GD_PARTICIPATION_RATE
                              EVT_COH_GD_COMPLIANCE_SCORE

Application Service        → EVT_ENT_OFFER_ACCEPTANCE_RATE
                              EVT_ENT_CANDIDATE_FEEDBACK_SCORE
                              EVT_INST_PLACEMENT_VERIFIED
                              EVT_COH_PLACEMENT_RATE
                              EVT_INST_RECRUITER_ENGAGEMENT

Job Service                → EVT_ENT_TIME_TO_HIRE
                              EVT_ENT_VERIFIED_JOB_POST_RATE

Education Service          → EVT_INST_COURSE_COMPLETION_RATE
                              EVT_TRN_STUDENT_COMPLETION_RATE
                              EVT_TRN_CONTENT_QUALITY_SCORE
                              EVT_TRN_LIVE_SESSION_ATTENDANCE

Dojo Match Engine          → EVT_COH_DOJO_MATCH_PARTICIPATION

Certification Engine       → EVT_INST_SKILL_PASSPORT_ISSUANCE
                              EVT_COH_SKILL_PASSPORT_RATE

Admin Governance Service   → EVT_INST_COMPLAINT_RESOLUTION_RATE
                              EVT_ENT_DISPUTE_RESOLUTION_RATE
                              EVT_TRN_COMPLAINT_RATE

Project Execution Engine   → EVT_COH_PEER_PROJECT_DELIVERY

Trainer Reputation Engine  → EVT_TRN_PEER_ENDORSEMENT_COUNT

Streak Engine              → EVT_COH_STREAK_MAINTENANCE_RATE

User Service               → EVT_INST_STUDENT_RETENTION_RATE
```

### X.B — Inbound Event Validation Rules

```
VALIDATION_RULES:

1. event_type must exist in EVENT_TYPE registry
   → Reject unknown event types with 400

2. unit_id must be enrolled in at least one ACTIVE cycle
   → Reject non-enrolled units with 422

3. event_source must match registered service identity in JWT
   → Reject mismatched sources with 403

4. event_timestamp must be within ACTIVE cycle date range
   → Reject out-of-range events with 422

5. event_value must be within declared range for event_type
   → Reject out-of-range values with 422

6. Duplicate event_id within same cycle → deduplicate silently
   Do NOT double-count

7. Batch size: max 1000 events per request
```

---

## SECTION XI — ANTI-MANIPULATION CONTROLS

The agent must enforce structural anti-gaming controls.
These are deterministic rules — not heuristic detections.

```
CONTROL_01: EVENT SPIKE DETECTION
  Rule:   If any unit submits more than 3× its 30-day rolling average
          of a single event type in one 24h window:
          → Flag events as UNVERIFIED
          → Exclude flagged events from scoring snapshot
          → Write MANIPULATION_SUSPECTED flag to audit log
          → Trigger ops console alert

CONTROL_02: SELF-REFERENTIAL EVENT REJECTION
  Rule:   Events emitted by a service owned by the same unit
          being scored are rejected unless dual-signed by
          an independent verification service

CONTROL_03: SCORE VELOCITY CAP
  Rule:   composite_score cannot increase by more than 25 points
          in a single 24h scoring run (relative to previous snapshot)
          If cap exceeded:
          → Score capped at previous + 25
          → Excess growth logged to audit trail
          → Reviewed in next cycle's scoring run for carry-forward

CONTROL_04: MINIMUM PARTICIPATION GATE
  Rule:   Units with fewer than minimum_member_threshold active members
          in cycle period are scored but ineligible for Tier 1 privileges
          until threshold is met

CONTROL_05: CROSS-CYCLE TRUST LOCK
  Rule:   Units demoted to PROBATIONARY_STATUS in current cycle
          cannot receive Tier 1 privileges in the NEXT cycle
          even if they achieve Tier 1 ranking
          Probation must be formally cleared by ops before privilege unlock
```

---

## SECTION XII — UI SCREENS (MANDATORY)

All screens below must be implemented. Absence → STOP EXECUTION.

### XII.A — Public Screens (SEO Indexed, Next.js)

```
SCREEN_P01: COMPETITION LEADERBOARD PUBLIC PAGE
  URL:             /competition/{cycle_id}/leaderboard
  Visibility:      Public
  Content:         Ranked units by tier
                   Badge indicators for excellence
                   Score trend sparklines (last 7 days)
                   Domain track filter
  SEO Metadata:    Auto-generated per cycle
  Canonical:       YES
  Refresh:         ISR — revalidate every 3600 seconds

SCREEN_P02: UNIT COMPETITION PROFILE PAGE
  URL:             /units/{unit_id}/competition
  Visibility:      Public
  Content:         Current cycle rank and tier
                   Historical cycle performance graph
                   Active badges and privileges
                   Active consequences (PUBLIC_PERFORMANCE_DISCLOSURE only)
  SEO Metadata:    YES
  Canonical:       YES
```

### XII.B — Authenticated Screens (Flutter)

```
SCREEN_A01: UNIT COMPETITION DASHBOARD
  Access:          UNIT_ADMIN | UNIT_MEMBER
  Content:         Current rank, tier, composite score
                   Score breakdown by event type
                   Rank delta indicators (↑↓ with magnitude)
                   Active privileges list
                   Active consequences list (if any)
                   Improvement actions panel (consequence remediation paths)
                   Score trend graph (full cycle)

SCREEN_A02: COMPETITION EVENTS FEED
  Access:          UNIT_ADMIN
  Content:         All inbound competition events for this unit
                   Event type, value, timestamp, verification status
                   Flagged events highlighted
                   Export function

SCREEN_A03: COMPETITION CYCLE BROWSER
  Access:          ALL AUTHENTICATED USERS
  Content:         All ACTIVE and UPCOMING cycles
                   Filter by unit type, domain track
                   Enrollment status indicator
                   Join / enroll action (for eligible units)

SCREEN_A04: PRIVILEGE WALLET
  Access:          UNIT_ADMIN
  Content:         Active privilege grants
                   Grant expiry timers
                   Historical granted privileges
                   Consequence suppression status (if any active)

SCREEN_A05: COMPETITION AUDIT EXPLORER
  Access:          PLATFORM_ADMIN
  Content:         Full audit log for any cycle
                   Filter by unit, event type, action type
                   Manipulation flags
                   Hash verification tool
                   Export as CSV
```

---

## SECTION XIII — ASYNC EVENT PUBLISHING

### XIII.A — Events Emitted by This Agent

```
competition.cycle.created
  Payload: cycle_id, unit_type, start_date, end_date, tier_count

competition.cycle.opened
  Payload: cycle_id, enrolled_unit_count

competition.cycle.closed
  Payload: cycle_id, final_leaderboard_snapshot_id

competition.unit.enrolled
  Payload: enrollment_id, cycle_id, unit_id, unit_type, tier_assigned

competition.scored
  Payload: cycle_id, snapshot_date, units_scored_count

competition.tier_changed
  Payload: cycle_id, unit_id, previous_tier, new_tier, rank_delta

competition.privilege_granted
  Payload: grant_id, cycle_id, unit_id, privilege_type, expires_at

competition.privilege_revoked
  Payload: grant_id, cycle_id, unit_id, privilege_type

competition.consequence_applied
  Payload: consequence_id, cycle_id, unit_id, consequence_type, severity_level

competition.consequence_lifted
  Payload: consequence_id, unit_id, lifted_at

competition.manipulation_suspected
  Payload: cycle_id, unit_id, event_type, flagged_event_count

competition.scoring_failed
  Payload: cycle_id, failure_reason, failed_at
```

### XIII.B — Consuming Services

```
Notification Service     → competition.tier_changed
                           competition.privilege_granted
                           competition.consequence_applied
                           competition.manipulation_suspected

Admin Governance Service → competition.manipulation_suspected
                           competition.consequence_applied (severity ≥ 4)
                           competition.scoring_failed

Billing Service          → competition.privilege_granted (REDUCED_PLATFORM_COMMISSION)
                           competition.privilege_revoked (REDUCED_PLATFORM_COMMISSION)

Intelligence Engine      → competition.scored (feed into reputation signals)

Analytics Service        → All events (written to ClickHouse)

SEO Regeneration Hook    → competition.scored (revalidate leaderboard pages)
                           competition.tier_changed (revalidate unit pages)
```

---

## SECTION XIV — OBSERVABILITY REQUIREMENTS

All metrics below must be emitted. Absence → STOP EXECUTION.

```
METRICS (Prometheus):
competition_scoring_runs_total          counter
competition_scoring_run_duration_ms     histogram
competition_events_ingested_total       counter
competition_events_rejected_total       counter  (label: reason)
competition_privilege_grants_active     gauge
competition_consequences_active         gauge    (label: severity)
competition_manipulation_flags_total    counter
competition_tier_changes_total          counter  (label: direction: up|down)

LOGS (Loki):
- Every scoring run start/end
- Every event rejection with reason
- Every privilege grant / revoke
- Every consequence apply / lift
- Every manipulation flag
- Every audit hash mismatch (CRITICAL alert)

DASHBOARDS (Grafana):
- Competition Health Dashboard
  · Active cycles count
  · Events ingested/hour
  · Scoring run success rate
  · Manipulation flag rate
  · Consequence distribution by type

ALERTS:
competition.scoring_failed             → PagerDuty CRITICAL
competition.audit_hash_mismatch        → PagerDuty CRITICAL
competition_manipulation_flags_total
  > 10 per hour for same unit          → Ops WARN

TRACING (OpenTelemetry):
- Scoring run spans with unit_count attribute
- Event ingestion spans with batch_size attribute
- Privilege grant decision spans
```

---

## SECTION XV — INTERN EXECUTION MAPPING

All steps below must be intern-executable without senior DevOps.

### XV.A — Run Competition Agent Locally

```
Step 1: Navigate to service folder
  cd /backend/services/competition_agent/

Step 2: Install dependencies
  python -m venv venv
  source venv/bin/activate
  pip install -r requirements.txt

Step 3: Copy env template
  cp .env.example .env

Step 4: Configure .env
  DB_URL=postgresql://localhost:5432/ecoskiller
  REDIS_URL=redis://localhost:6379
  KAFKA_BROKER=localhost:9092

Step 5: Run migrations
  alembic upgrade head

Step 6: Seed event type registry
  python seed_event_types.py

Step 7: Start agent
  uvicorn main:app --reload --port 8030

Expected Output:
  COMPETITION AGENT ONLINE
  Event Gateway: http://localhost:8030
  Docs: http://localhost:8030/docs
```

### XV.B — Trigger a Test Scoring Run

```
Step 1: Create a test cycle via API
  POST http://localhost:8030/competition/cycles/create
  Body: test_cycle_config.json

Step 2: Enroll a test unit
  POST http://localhost:8030/competition/cycles/{cycle_id}/enroll

Step 3: Ingest test events
  python tools/inject_test_events.py --cycle_id={cycle_id}

Step 4: Trigger manual scoring run
  python tools/trigger_scoring_run.py --cycle_id={cycle_id}

Expected Output:
  SCORING RUN COMPLETE
  Units scored: N
  Privilege grants written: N
  Consequences applied: N
  Audit log entries written: N

Failure → STOP → REPORT SCORING_RUN_FAILURE
```

---

## SECTION XVI — ENFORCEMENT SEAL

```
SECTION XVI — INTER_UNIT_COMPETITION_AGENT ENFORCEMENT SEAL
Status: ACTIVE · FINAL · LOCKED

No system deployment may proceed unless:

✔ All database tables in Section III exist and have migrations
✔ All event types in Section IV are registered in the event registry
✔ Scoring formula in Section V is implemented as rule engine only (R28-1)
✔ All privilege types in Section VI are declared and grantable
✔ All consequence types in Section VII are declared and enforceable
✔ Competition cycle state machine in Section VIII is implemented
✔ All API endpoints in Section IX are implemented and contracted
✔ Event ingestion gateway in Section X is operational
✔ All anti-manipulation controls in Section XI are active
✔ All UI screens in Section XII are implemented and wired
✔ All async events in Section XIII are published and consumed
✔ All observability requirements in Section XIV are live
✔ All intern execution steps in Section XV pass without error

Failure in any → STOP EXECUTION
               → REPORT INTER_UNIT_COMPETITION_AGENT_INCOMPLETE
               → NO DEPLOYMENT CLAIM PERMITTED

This agent is bound to:
R2   (Data Models)
R5   (Workflow State Machines)
R10  (Security Policies)
R21  (Internal Operations Console)
R28  (Intelligence Cost Optimization Law)
R39  (Core Inbuilt Platform Tools)
R40  (Admin & Ops Console)
R49  (Automatic Contract Validator)
R50  (Automated QA Test Generator)
R53  (Status, Badge & Level Progression)
R57  (Daily Habit & Streak Engine)
R87  (Trainer Competition & Gamification)

Violation → STOP → REPORT → NO CLAIM OF COMPLETION
```

---

## SECTION XVII — ANTIGRAVITY CONTRACT SUMMARY

```
The INTER_UNIT_COMPETITION_AGENT implements antigravity
through five structural mechanisms:

1. COMPETITIVE PRESSURE
   Every unit is always ranked relative to its peers.
   There is no neutral state.
   Stagnation is a declining rank.

2. CONTINUOUS MEASUREMENT
   The agent runs every 24 hours.
   There are no grace periods.
   There are no off-cycle rest periods.

3. PRIVILEGE REDISTRIBUTION
   Privileges flow to high performers.
   Privileges are revoked from declining performers.
   Platform resources follow performance — not tenure.

4. GRADUATED CONSEQUENCES
   Consequences escalate with duration of underperformance.
   They are lifted automatically when performance recovers.
   No human discretion in application or removal.

5. IMMUTABLE AUDIT CHAIN
   Every scoring run, every event, every privilege, every consequence
   is hash-chained in the audit log.
   No actor — human or system — can alter historical records.
   Trust is not assumed. Trust is proven by unbroken chain.
```

---

```
DOCUMENT STATUS:    SEALED · LOCKED · PRODUCTION-READY SPECIFICATION
AGENT VERSION:      v1.0
MASTER PROMPT:      ECOSKILLER v12.0+
MUTATION POLICY:    ADD-ONLY via version bump
ISSUED:             2026-03-04
NEXT REVIEW:        Via version bump only — no silent edits permitted
```
