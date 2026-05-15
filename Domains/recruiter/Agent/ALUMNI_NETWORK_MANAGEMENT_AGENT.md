# 🔒 ECOSKILLER — ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
## ALUMNI_NETWORK_MANAGEMENT_AGENT
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
Agent Name:         ALUMNI_NETWORK_MANAGEMENT_AGENT
Agent Class:        ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
Layer:              Antigravity
Parent System:      ECOSKILLER MASTER EXECUTION PROMPT v12.0+
Namespace (k8s):    core / society / analytics / realtime
Stack Lock:         Python 3.11 + FastAPI + PostgreSQL + Redis +
                    ClickHouse + Kafka + Qdrant + Temporal +
                    WebSocket + Prometheus + Grafana + Loki +
                    OpenTelemetry + Keycloak (RBAC extension)
Depends On:         R2, R5, R10, R21, R28, R34, R35, R37,
                    R39, R40, R46, R49, R50, R51, R52, R53,
                    R56, R58, R62, R68, R71, R72, R73, R74,
                    R75, R76, R79, R80, R86, R91, R94,
                    INTER_UNIT_COMPETITION_AGENT,
                    LEADERBOARD_MANAGEMENT_AGENT
Produces:           Alumni profiles, verified career records,
                    mentorship bonds, referral chains, placement
                    attribution proofs, network graph edges,
                    alumni contribution scores, trust signal
                    feeds, SEO alumni pages, skill trajectory
                    timelines, institutional reputation signals,
                    society alumni network rankings, immutable
                    career audit logs
Consumed By:        Intelligence Engine, Leaderboard Agent,
                    Competition Agent, Notification Service,
                    Analytics Service, Reputation Engine,
                    Job Service, Institution Service,
                    Society Domain Layer, Billing Service,
                    Admin Governance Service, SEO Hook
```

---

## SECTION I — AGENT PURPOSE & ANTIGRAVITY DEFINITION

### I.A — What This Agent Does

The ALUMNI_NETWORK_MANAGEMENT_AGENT governs the full lifecycle
of every user who has graduated beyond their enrollment state —
from students who received placements, to trainers who built
cohorts, to coaches in the society domain who delivered outcomes.

An alumni is not a historical artifact.
An alumni is a living trust signal.

This agent converts alumni presence into computable value:

- A verified alumni placement record raises the institution's
  trust score in the LEADERBOARD_MANAGEMENT_AGENT
- An alumni referral that converts creates a permanent attribution
  chain traceable to the originating institution
- An alumni mentor who helps a student pass GD receives scored
  contribution credit that feeds back into the Reputation Engine
- An alumni company that hires from their origin institution
  creates a bidirectional loyalty signal consumed by the
  Competition Agent
- An alumni who goes dark is detected, re-engagement triggered,
  and career gap risk flagged per R73

**The network is the product.
The alumni are the network.
This agent is the governance layer that makes the network
trustworthy, measurable, and antigravitational.**

### I.B — Five Antigravity Mechanisms

```
MECHANISM 01: NETWORK GRAVITY REVERSAL
  Without alumni governance, graduated users disappear.
  Platform loses the most valuable trust signals it has —
  verified placement outcomes tied to real career trajectories.
  This agent prevents dropout by creating structural incentives
  for alumni to remain engaged, contribute, and be measured.

MECHANISM 02: INSTITUTIONAL TRUST ANCHORING
  An institution's current rank is partly a function of
  what its alumni achieved after leaving.
  This creates a forward-looking trust signal:
  institutions compete not just on current student performance
  but on the verified long-term outcomes of prior cohorts.
  Decay without ongoing alumni records is automatic.

MECHANISM 03: REFERRAL CHAIN INTEGRITY
  Every hire that traces back to an alumni referral
  is permanently attributed, audited, and rewarded.
  This prevents ghost-referral inflation and ensures
  platform referral economics are self-reinforcing.

MECHANISM 04: CAREER TRAJECTORY AS SIGNAL
  An alumni's career progression is a continuous input
  into the platform's intelligence models.
  Skills added, companies joined, promotions verified —
  all feed back into placement probability models for
  current students in the same institution and domain track.

MECHANISM 05: SOCIETY DOMAIN SUCCESSION
  In offline franchise networks, coach alumni who graduated
  from the society workshop system become the next generation
  of coaches, coordinators, and franchise owners.
  This agent manages role succession, capability verification,
  and contribution attribution for the society offline layer.
```

### I.C — Non-Negotiable Constraints

```
FORBIDDEN:
- Manual alumni verification without declared verification source
- AI inference of career progression (R28-1 — rule engine only
  for verification; ML permitted only for prediction models)
- Alumni contribution scores set by admin without rule basis
- Suppression of alumni records without dual admin sign-off + audit
- Cross-institution alumni data leakage (RLS enforced)
- Society domain alumni data merged with online domain without
  explicit migration declaration
- Parent-view access to alumni records without student consent
  (carried forward from student stage privacy rules)
- Retroactive score adjustments without signed event evidence

REQUIRED:
- Career stage state machine (R71) governs every alumni record
- Skill passport (R72) is the source of truth for verified skills
- Alumni contribution scoring is deterministic rule engine (R28-1)
- Referral attribution chain is immutable once written
- All alumni verifications hash-chained in audit log
- Qdrant vector store maintained for alumni similarity scoring
  and talent recommendation (society architecture requirement)
- Temporal workflow engine governs alumni-to-coach succession
  in society domain (Temporal mandatory per society architecture)
- Offline alumni records (society coaches) synced via CouchDB
```

---

## SECTION II — ALUMNI CLASSIFICATION TAXONOMY

Every user who transitions out of an enrollment state
must be classified into an alumni type before the agent
can operate on their record.

### II.A — Alumni Type Registry

```
ALUMNI_TYPE_01: PLACED_GRADUATE
  Origin Stage:   Student (enrollment completed)
  Trigger:        Verified offer acceptance + joining confirmation
  Capabilities:   Mentor current students, provide referrals,
                  validate institutional reputation signals,
                  post verified career updates
  Trust Level:    VERIFIED (dual-confirmed placement)
  Society Scope:  NO (online domain only)

ALUMNI_TYPE_02: UNPLACED_GRADUATE
  Origin Stage:   Student (enrollment completed, no placement via portal)
  Trigger:        Graduation date passed + no verified offer on record
  Capabilities:   Maintain skill passport, contribute peer reviews,
                  re-engage via off-campus placement flow
  Trust Level:    BASIC (identity verified, placement unconfirmed)
  Society Scope:  NO
  Note:           Not penalized — platform continues serving them
                  Re-engagement campaign triggered automatically (R73)

ALUMNI_TYPE_03: CAREER_TRANSITIONED
  Origin Stage:   Placed Graduate (ALUMNI_TYPE_01)
  Trigger:        New company join event OR self-reported
                  + minimum one verification signal
  Capabilities:   All TYPE_01 capabilities + dual-company referral
                  potential (current employer + origin company)
  Trust Level:    VERIFIED (career update verified)
  Society Scope:  NO

ALUMNI_TYPE_04: MENTOR_ALUMNI
  Origin Stage:   Any graduate alumni type
  Trigger:        Mentor role assignment accepted + first mentee
                  session completed
  Capabilities:   All graduate capabilities + mentor contribution
                  scoring, session delivery, Dojo mentorship,
                  GD coaching sessions
  Trust Level:    ELEVATED (mentor performance tracked)
  Society Scope:  YES (can mentor society domain students)

ALUMNI_TYPE_05: TRAINER_ALUMNI
  Origin Stage:   Verified Trainer (completed ≥ 1 course cycle)
  Trigger:        Trainer role graduation — course delivery
                  completion + minimum student placement
                  contribution verified
  Capabilities:   Course creation, co-teaching, alumni referral
                  circuits, trainer reputation cascade
  Trust Level:    VERIFIED + TRAINER_BADGE
  Society Scope:  YES

ALUMNI_TYPE_06: SOCIETY_COACH_ALUMNI
  Origin Stage:   Society COACH role (society domain)
  Trigger:        Batch completion ≥ 3 + coordinator sign-off
                  via Temporal workflow
  Capabilities:   Franchise mentoring, coordinator training,
                  rural workshop delivery, Govt/CSR scheme
                  liaison, district-level talent networking
  Trust Level:    ELEVATED (SOCIETY_ADMIN verified)
  Society Scope:  YES — primary
  Offline:        YES — records synced via CouchDB
  Note:           Role succession tracked by Temporal workflow
                  per society architecture requirement

ALUMNI_TYPE_07: ENTERPRISE_RETURNER
  Origin Stage:   Any alumni type
  Trigger:        Alumni joins a company that is also a
                  verified Enterprise tenant on the platform
  Capabilities:   Internal referral circuit activation,
                  company-side trust anchor,
                  recruiter collaboration flag
  Trust Level:    ELEVATED (both identity and employment verified)
  Society Scope:  NO
  Note:           Bidirectional trust: alumni trust → company trust
                  and company trust → alumni profile amplification

ALUMNI_TYPE_08: ROYALTY_INNOVATOR_ALUMNI
  Origin Stage:   Student innovator with verified idea in
                  Idea Registry (Innovation Engine)
  Trigger:        Student reaches age 18 + ownership transfer
                  confirmed by Innovation Trust Governance Service
  Capabilities:   Full IP ownership, royalty wallet management,
                  licensing contract execution, innovation
                  mentoring for current students
  Trust Level:    VERIFIED + LEGAL_GUARDIAN_CONFIRMED
  Society Scope:  YES (innovation outreach)
  Note:           Governed by Innovation Trust Governance Service
                  + Legal Document Generation Service
```

---

## SECTION III — DATABASE SCHEMA (MANDATORY)

Absence of any table → STOP EXECUTION.

### III.A — Core Alumni Tables

```sql
-- Master alumni profile (extends user record)
alumni_profiles (
  alumni_id           UUID PRIMARY KEY,
  user_id             UUID NOT NULL UNIQUE,       -- FK to users table
  alumni_type         TEXT NOT NULL,              -- references ALUMNI_TYPE registry
  origin_institution_id UUID,                     -- FK to institutions
  origin_cohort_id    UUID,                       -- FK to student_cohorts
  graduation_date     DATE NOT NULL,
  enrollment_end_reason TEXT NOT NULL,            -- PLACEMENT | GRADUATION | TRANSFER | OTHER
  career_stage        TEXT NOT NULL,              -- from R71 state machine
  trust_level         TEXT NOT NULL,              -- BASIC | VERIFIED | ELEVATED
  contribution_score  NUMERIC NOT NULL DEFAULT 0,
  network_depth       INTEGER NOT NULL DEFAULT 0, -- hop distance to platform core
  verified_at         TIMESTAMP,
  verification_source TEXT,
  suppressed          BOOLEAN NOT NULL DEFAULT FALSE,
  suppressed_reason   TEXT,
  society_domain      BOOLEAN NOT NULL DEFAULT FALSE,
  offline_sync_enabled BOOLEAN NOT NULL DEFAULT FALSE,
  created_at          TIMESTAMP NOT NULL,
  last_active_at      TIMESTAMP,
  profile_hash        TEXT NOT NULL
)

-- Career record timeline (immutable entries)
alumni_career_records (
  record_id           UUID PRIMARY KEY,
  alumni_id           UUID REFERENCES alumni_profiles,
  record_type         TEXT NOT NULL,              -- PLACEMENT | PROMOTION | TRANSITION
                                                  -- | FREELANCE | STARTUP | HIATUS
  company_id          UUID,                       -- FK to enterprises (if on-platform)
  company_name_raw    TEXT,                       -- for off-platform companies
  role_title          TEXT NOT NULL,
  department          TEXT,
  start_date          DATE NOT NULL,
  end_date            DATE,                       -- NULL if current
  salary_band_verified BOOLEAN NOT NULL DEFAULT FALSE,
  verification_source TEXT,                       -- COMPANY_CONFIRM | OFFER_HASH
                                                  -- | SELF_DECLARED | DOCUMENT_UPLOAD
  verification_hash   TEXT,
  is_current          BOOLEAN NOT NULL DEFAULT FALSE,
  created_at          TIMESTAMP NOT NULL,
  record_hash         TEXT NOT NULL               -- sha256 of record state
)

-- Alumni → student mentorship bonds
alumni_mentorship_bonds (
  bond_id             UUID PRIMARY KEY,
  mentor_alumni_id    UUID REFERENCES alumni_profiles,
  mentee_user_id      UUID NOT NULL,
  bond_type           TEXT NOT NULL,              -- GD_COACHING | INTERVIEW_PREP
                                                  -- | CAREER_GUIDANCE | DOJO_TRAINING
                                                  -- | PROJECT_REVIEW | SOCIETY_WORKSHOP
  initiated_at        TIMESTAMP NOT NULL,
  status              TEXT NOT NULL,              -- ACTIVE | COMPLETED | TERMINATED
  sessions_completed  INTEGER NOT NULL DEFAULT 0,
  mentee_outcome      TEXT,                       -- PLACED | IMPROVED_SCORE | BELT_UP | NULL
  outcome_verified    BOOLEAN NOT NULL DEFAULT FALSE,
  contribution_points_awarded INTEGER DEFAULT 0,
  closed_at           TIMESTAMP,
  bond_hash           TEXT NOT NULL
)

-- Referral attribution chain
alumni_referral_chains (
  chain_id            UUID PRIMARY KEY,
  referrer_alumni_id  UUID REFERENCES alumni_profiles,
  referred_user_id    UUID NOT NULL,
  referral_type       TEXT NOT NULL,              -- JOB_APPLICATION | PLATFORM_SIGNUP
                                                  -- | SOCIETY_ENROLLMENT | COURSE_ENROLL
  referral_code       TEXT NOT NULL,
  referred_at         TIMESTAMP NOT NULL,
  conversion_event    TEXT,                       -- OFFER_ACCEPTED | ENROLLED | PLACED
  conversion_at       TIMESTAMP,
  conversion_verified BOOLEAN NOT NULL DEFAULT FALSE,
  reward_triggered    BOOLEAN NOT NULL DEFAULT FALSE,
  reward_type         TEXT,
  chain_hash          TEXT NOT NULL
)

-- Alumni contribution score ledger
alumni_contribution_ledger (
  ledger_id           UUID PRIMARY KEY,
  alumni_id           UUID REFERENCES alumni_profiles,
  contribution_type   TEXT NOT NULL,              -- references CONTRIBUTION_TYPE registry
  points_awarded      INTEGER NOT NULL,
  source_event_id     UUID NOT NULL,
  source_service      TEXT NOT NULL,
  awarded_at          TIMESTAMP NOT NULL,
  expires_at          TIMESTAMP,                  -- NULL = permanent
  ledger_hash         TEXT NOT NULL
)

-- Institutional alumni stats (materialized, refreshed daily)
alumni_institution_stats (
  stat_id             UUID PRIMARY KEY,
  institution_id      UUID NOT NULL,
  cohort_year         INTEGER,                    -- NULL = all-time
  total_alumni        INTEGER NOT NULL DEFAULT 0,
  placed_alumni       INTEGER NOT NULL DEFAULT 0,
  active_mentors      INTEGER NOT NULL DEFAULT 0,
  verified_career_records INTEGER NOT NULL DEFAULT 0,
  avg_contribution_score NUMERIC,
  referral_conversion_rate NUMERIC,
  placement_attribution_count INTEGER NOT NULL DEFAULT 0,
  last_computed_at    TIMESTAMP NOT NULL,
  stat_hash           TEXT NOT NULL
)

-- Society coach succession records (Temporal-governed)
alumni_society_succession (
  succession_id       UUID PRIMARY KEY,
  alumni_id           UUID REFERENCES alumni_profiles,
  society_id          UUID NOT NULL,
  previous_role       TEXT NOT NULL,              -- STUDENT | PARTICIPANT
  new_role            TEXT NOT NULL,              -- COACH | COORDINATOR | FRANCHISE_OWNER
  temporal_workflow_id TEXT NOT NULL,             -- Temporal workflow reference
  coordinator_sign_off_by UUID,
  society_admin_sign_off_by UUID,
  succession_date     DATE NOT NULL,
  batches_completed   INTEGER NOT NULL,           -- prerequisite count
  succession_hash     TEXT NOT NULL
)

-- Qdrant vector index tracking
alumni_vector_index (
  index_id            UUID PRIMARY KEY,
  alumni_id           UUID REFERENCES alumni_profiles,
  vector_type         TEXT NOT NULL,              -- SKILL_PORTFOLIO | CAREER_TRAJECTORY
                                                  -- | MENTOR_PROFILE | SOCIETY_ALUMNI
  qdrant_collection   TEXT NOT NULL,
  qdrant_point_id     TEXT NOT NULL,
  last_embedded_at    TIMESTAMP NOT NULL,
  embedding_version   TEXT NOT NULL,
  index_hash          TEXT NOT NULL
)

-- Alumni network graph edges (for network depth computation)
alumni_network_edges (
  edge_id             UUID PRIMARY KEY,
  from_alumni_id      UUID REFERENCES alumni_profiles,
  to_entity_id        UUID NOT NULL,
  to_entity_type      TEXT NOT NULL,              -- ALUMNI | STUDENT | INSTITUTION
                                                  -- | ENTERPRISE | SOCIETY_UNIT
  edge_type           TEXT NOT NULL,              -- MENTORED | REFERRED | CO_TRAINED
                                                  -- | CO_WORKED | SUCCEEDED_FROM
  created_at          TIMESTAMP NOT NULL,
  weight              NUMERIC NOT NULL DEFAULT 1.0,
  verified            BOOLEAN NOT NULL DEFAULT FALSE,
  edge_hash           TEXT NOT NULL
)

-- Alumni offline sync queue (society domain)
alumni_offline_sync_queue (
  sync_id             UUID PRIMARY KEY,
  alumni_id           UUID,
  record_type         TEXT NOT NULL,
  record_payload      JSONB NOT NULL,
  originated_at       TIMESTAMP NOT NULL,
  synced_at           TIMESTAMP,
  sync_status         TEXT NOT NULL DEFAULT 'PENDING',
  conflict_resolution TEXT
)

-- Immutable audit chain
alumni_audit_log (
  audit_id            UUID PRIMARY KEY,
  alumni_id           UUID,
  agent_action        TEXT NOT NULL,
  action_input        JSONB NOT NULL,
  action_output       JSONB NOT NULL,
  action_timestamp    TIMESTAMP NOT NULL,
  actor               TEXT NOT NULL,              -- AGENT | ADMIN | SYSTEM | TEMPORAL
  admin_id            UUID,
  previous_audit_hash TEXT,                       -- chain link
  audit_hash          TEXT NOT NULL               -- sha256(previous_hash + data)
)
```

---

## SECTION IV — CONTRIBUTION TYPE REGISTRY

All contribution types that award points to alumni
must be declared here. Undeclared types are rejected.

### IV.A — Mentorship Contributions

```
CONTRIB_01: MENTORSHIP_SESSION_DELIVERED
  Description: Alumni delivered a verified mentorship session
               (GD coaching, interview prep, career guidance)
  Points:      15 per session
  Max/Month:   150 (cap: 10 sessions × 15)
  Verification: Session completion logged by mentee + system timer
  Source:      Alumni Mentorship Bond record
  Decay:       YES — 90-day rolling window for active mentor status

CONTRIB_02: MENTORSHIP_OUTCOME_ACHIEVED
  Description: Mentee achieved a verified outcome traceable
               to the mentorship bond (placement, belt-up,
               GD score improvement ≥ 10 points)
  Points:      50 per verified outcome
  Max/Month:   Uncapped (outcome-driven, not volume-driven)
  Verification: Dual-verified — mentee outcome + platform record
  Source:      alumni_mentorship_bonds.outcome_verified = TRUE
  Decay:       NO — permanent record

CONTRIB_03: DOJO_COACHING_DELIVERED
  Description: Alumni delivered Dojo match coaching session
               to current student
  Points:      20 per verified match coaching session
  Verification: Dojo Match Engine session log
  Decay:       YES — 90-day rolling window
```

### IV.B — Referral Contributions

```
CONTRIB_04: REFERRAL_PLATFORM_SIGNUP_CONVERTED
  Description: Alumni referral resulted in a new verified
               user completing full profile + verification
  Points:      10 per conversion
  Verification: User profile completion + verification event
  Fraud Check: R52 referral fraud detection applied
  Decay:       NO

CONTRIB_05: REFERRAL_JOB_APPLICATION_SUBMITTED
  Description: Alumni referral resulted in a referred candidate
               submitting a verified job application
  Points:      20 per application
  Verification: Application Service application_submitted event
  Decay:       YES — 180-day window from referral date
  Max/Month:   200 (10 applications × 20)

CONTRIB_06: REFERRAL_PLACEMENT_CONVERTED
  Description: Alumni referral resulted in a verified offer
               acceptance and joining confirmation
  Points:      100 per verified placement
  Verification: Dual-verified — company offer hash + candidate accept
  Decay:       NO — permanent attribution record
  Note:        This is the highest single-event contribution.
               It permanently links alumni → institution →
               enterprise in the trust graph.
```

### IV.C — Knowledge Contributions

```
CONTRIB_07: COURSE_CONTENT_CONTRIBUTION
  Description: Alumni contributed verified course content
               (module, case study, scenario) used in
               active course delivery
  Points:      30 per accepted contribution
  Verification: Trainer + Education Service acceptance event
  Decay:       YES — content relevance score applied at 180 days

CONTRIB_08: SUCCESS_STORY_PUBLISHED
  Description: Alumni career milestone auto-generated as
               success story (R80) and published as SEO page
  Points:      25 per published story
  Verification: SEO publication event from R80 system
  Decay:       NO

CONTRIB_09: PEER_SKILL_ENDORSEMENT_GIVEN
  Description: Alumni provided verified skill endorsement
               to a current student or another alumni
  Points:      5 per verified endorsement
  Max/Month:   50 (cap: 10 endorsements × 5)
  Verification: R79 / R91 endorsement system — verified identity
  Decay:       YES — 90-day rolling window
```

### IV.D — Society Domain Contributions

```
CONTRIB_10: SOCIETY_WORKSHOP_DELIVERED
  Description: Society coach alumni delivered a verified
               offline workshop batch
  Points:      40 per completed batch
  Verification: Coordinator sign-off via Temporal workflow
  Offline:     YES — synced via CouchDB
  Decay:       NO

CONTRIB_11: SOCIETY_STUDENT_ENROLLED
  Description: Society coach alumni's direct referral resulted
               in a new verified student enrollment in
               society domain
  Points:      15 per verified enrollment
  Verification: Society Domain Layer enrollment event
  Offline:     YES
  Decay:       NO

CONTRIB_12: SOCIETY_TOURNAMENT_ORGANIZED
  Description: Society coach alumni organized or co-organized
               a verified society tournament
  Points:      35 per completed tournament
  Verification: Tournament Engine Layer completion event +
               Master Organizer sign-off
  Offline:     YES
  Decay:       NO

CONTRIB_13: GOVT_CSR_SCHEME_FACILITATED
  Description: Alumni facilitated verified student enrollment
               in Govt or CSR scheme through society network
  Points:      60 per scheme documentation approval
  Verification: Govt + CSR Layer + scheme documentation
               approval via Temporal workflow
  Offline:     YES
  Decay:       NO
```

### IV.E — Career Verification Contributions

```
CONTRIB_14: CAREER_RECORD_VERIFIED
  Description: Alumni career record verified by either
               their employing enterprise (if on-platform)
               or via offer document hash match
  Points:      20 per verified record
  Verification: COMPANY_CONFIRM or OFFER_HASH source
  Decay:       NO — permanent verified record

CONTRIB_15: SKILL_PASSPORT_UPDATED_VERIFIED
  Description: Alumni added new verified skill to
               lifetime skill passport (R72)
  Points:      10 per verified skill addition
  Verification: Certification & Belt Engine or
               trainer confirmation
  Decay:       NO

CONTRIB_16: ROYALTY_INNOVATOR_ACTIVATION
  Description: Alumni ROYALTY_INNOVATOR_ALUMNI type
               activated full IP ownership upon turning 18
  Points:      75 one-time activation bonus
  Verification: Innovation Trust Governance Service
               + Legal Document Generation Service
  Decay:       NO
```

---

## SECTION V — CONTRIBUTION SCORE ENGINE

### V.A — Architecture Constraint

```
RULE: ALL contribution score computation is deterministic
      rule engine only (R28-1).
      No ML inference for score assignment.
      No admin override without dual sign-off + audit log.
      Identical input → Identical output — always.
```

### V.B — Score Computation Formula

```
contribution_score(alumni) =
  Σ [ points(contribution_type[i])
      × recency_weight(awarded_at[i])
      × trust_multiplier(alumni.trust_level) ]
  − decay_loss(alumni)

WHERE:

recency_weight(t) = e^(−λ_contrib × days_since_award(t))
  λ_contrib = 0.02 (slow decay — contribution history is valued)
  Applied only to contribution types with Decay: YES

trust_multiplier(trust_level):
  BASIC    → 0.80
  VERIFIED → 1.00
  ELEVATED → 1.20

decay_loss(alumni) =
  IF alumni.last_active_at is NULL
    OR days_since(alumni.last_active_at) > 180
  → apply inactivity_penalty = 10 points per 30 days of silence
     capped at total_penalty = 100 points max
```

### V.C — Network Depth Computation

```
Alumni network depth measures how deeply an alumni
is woven into the platform's trust graph.

network_depth(alumni) = count of distinct verified
  alumni_network_edges where from_alumni_id = alumni.alumni_id
  AND verified = TRUE

Tier mapping:
  0 edges   → ISOLATED (no network contribution)
  1–3 edges → CONNECTED
  4–8 edges → EMBEDDED
  9–15 edges → INFLUENTIAL
  16+ edges → NETWORK_ANCHOR

Network depth tier is surfaced on:
  - Alumni public profile (SCREEN_P02)
  - Leaderboard dimension DIM_ALUMNI (fed to Leaderboard Agent)
  - Institution stats (alumni_institution_stats)
```

### V.D — Qdrant Embedding Refresh Rules

```
Qdrant collections used by this agent:
  alumni_skill_portfolio  — cosine similarity for talent rec
  alumni_career_trajectory — L2 distance for career path matching
  alumni_mentor_profile   — cosine similarity for mentor matching
  alumni_society_coaches  — cosine similarity for franchise succession

Embedding refresh triggers:
  1. On alumni_career_records INSERT (new career record)
  2. On alumni_profiles career_stage update
  3. On alumni_mentorship_bonds status = COMPLETED
  4. On alumni skill passport update (R72 event)
  5. Daily batch refresh for records older than 30 days

Embedding model: declared in Feature Store Service (R28-3 permitted
  — LLM for text understanding of role titles and skill descriptions)
  Inference cost: declared per 1,000 requests in Model Registry Service
```

---

## SECTION VI — CAREER STAGE STATE MACHINE

All alumni must exist within the R71 Career Lifecycle
state machine. The alumni agent extends the R71 machine
with alumni-specific states.

### VI.A — Alumni Career States

```
States (extends R71):

ENROLLED              → Pre-alumni (current student/trainer)
TRANSITIONING         → Grace period (graduation detected,
                         classification pending — max 30 days)
PLACED_ACTIVE         → Verified placement, current employment
CAREER_GAP            → R73 inactivity detected (180+ days)
CAREER_TRANSITIONED   → New verified employment after gap
MENTOR_ACTIVE         → Active open mentorship bond
SOCIETY_COACH_ACTIVE  → Active society coach (Temporal-governed)
ROYALTY_OWNER_ACTIVE  → IP ownership transferred (TYPE_08)
RETIRED               → Voluntary retirement declared by alumni
DORMANT               → 365+ days no signal — auto-classified

Transitions:

ENROLLED            → TRANSITIONING       on graduation_date + 1 day
TRANSITIONING       → PLACED_ACTIVE       on offer_acceptance verified
TRANSITIONING       → CAREER_GAP          on 30-day expiry with no
                                           placement verified
PLACED_ACTIVE       → CAREER_GAP          on R73 inactivity trigger
PLACED_ACTIVE       → CAREER_TRANSITIONED on new career record verified
PLACED_ACTIVE       → MENTOR_ACTIVE       on first mentorship session
CAREER_GAP          → PLACED_ACTIVE       on new career record verified
CAREER_TRANSITIONED → MENTOR_ACTIVE       on first mentorship session
MENTOR_ACTIVE       → PLACED_ACTIVE       on all bonds closed
SOCIETY_COACH_ACTIVE → CAREER_GAP         on batch delivery stop (180+ days)
any                 → DORMANT             on 365 days no signal
DORMANT             → TRANSITIONING       on re-engagement event

All transitions emit: alumni.career_stage_changed
Transition rules evaluated daily by Temporal workflow.
```

### VI.B — Re-engagement Trigger Rules (R73 Extension)

```
TRIGGER RULE 01: CAREER_GAP detection
  Condition: last_active_at > 180 days ago
             AND career_stage NOT IN (RETIRED, DORMANT)
  Action:    → career_stage → CAREER_GAP
             → Emit: alumni.career_gap_detected
             → Notification: personalized re-engagement via R73
             → Skill obsolescence check initiated

TRIGGER RULE 02: DORMANT classification
  Condition: last_active_at > 365 days ago
             AND career_stage = CAREER_GAP
  Action:    → career_stage → DORMANT
             → contribution_score decay accelerated (λ = 0.10)
             → Network edges weight halved
             → Emit: alumni.dormant_classified

TRIGGER RULE 03: Skill obsolescence flag
  Condition: Any skill in alumni skill passport
             with last_used_at > 180 days
  Action:    → Emit: alumni.skill_obsolescence_detected
             → Notification sent with upgrade recommendation
             → Skill passport age flag set on public profile
```

---

## SECTION VII — INSTITUTIONAL TRUST ATTRIBUTION

### VII.A — How Alumni Feed Institutional Reputation

```
Alumni records are the primary long-term trust signal for
institutions in the LEADERBOARD_MANAGEMENT_AGENT.

The following alumni events directly update
alumni_institution_stats and emit signals to the
Leaderboard Agent's signal ingestion gateway:

EVENT → LEADERBOARD SIGNAL EMITTED:

alumni.placement_verified
  → SIG_INST_PLACEMENT_VERIFIED (DIM_02: Institution Unit)
  → SIG_COH_PLACEMENT_RATE (DIM_09: Student Cohort)
  Weight: HIGH

alumni.mentor_outcome_achieved
  → SIG_INST_MENTOR_CONTRIBUTION (DIM_02)
  Weight: MEDIUM

alumni.referral_placement_converted
  → SIG_INST_ALUMNI_REFERRAL_CONVERSION (DIM_02)
  Weight: HIGH (long-term institutional gravity signal)

alumni.career_record_verified (1 year+ after placement)
  → SIG_INST_POST_PLACEMENT_RETENTION (DIM_02)
  Description: Alumni who remain employed 1+ year post-placement
               signal strong institutional preparation quality
  Weight: HIGH

alumni.society_workshop_delivered (TYPE_06)
  → SIG_SOC_ALUMNI_COACH_CONTRIBUTION (DIM_10: Society Franchise)
  Weight: HIGH (offline domain)

alumni.career_gap_detected
  → NEGATIVE signal: SIG_INST_ALUMNI_GAP_RATE (DIM_02)
  Description: High alumni gap rate signals weak career
               preparation by institution
  Weight: MEDIUM (inverted — lower = better)
```

### VII.B — Enterprise Bidirectional Trust Signal

```
When ALUMNI_TYPE_07 (ENTERPRISE_RETURNER) is created:

Both directions of trust are updated:

DIRECTION 01: Alumni → Enterprise trust
  Alumni is employed at verified enterprise tenant
  → Enterprise trust_level for this alumni: ELEVATED
  → Alumni profile shows employer trust badge
  → Alumni referrals from this employer get 1.3× attribution weight

DIRECTION 02: Enterprise → Alumni trust (bidirectional)
  Enterprise employment verification confirms alumni career record
  → alumni_career_records.verification_source = COMPANY_CONFIRM
  → Emit: alumni.career_record_verified
  → CONTRIB_14 awarded to alumni
  → Institution gains SIG_INST_POST_PLACEMENT_RETENTION signal
```

---

## SECTION VIII — API CONTRACT REGISTRY

All endpoints mandatory. Absence → STOP EXECUTION.

```
POST   /alumni/profiles/create
  Auth:      INTERNAL_SERVICE | SYSTEM (auto-triggered on graduation)
  Body:      AlumniProfileCreateRequest
  Response:  AlumniProfileResponse
  Audit:     YES

GET    /alumni/profiles/{alumni_id}
  Auth:      ALUMNI_SELF | INSTITUTION_ADMIN | RECRUITER
             | PLATFORM_ADMIN
  Response:  AlumniProfileDetailResponse
             (trust_level, career_stage, contribution_score,
              network_depth, active_bond_count)

GET    /alumni/profiles/{alumni_id}/public
  Auth:      PUBLIC
  Response:  AlumniPublicProfileResponse
             (display_name, alumni_type, career_stage,
              contribution_tier, skill_passport_summary,
              network_depth_tier, verified_badge)
  SEO:       YES — canonical URL, ISR 3600s

POST   /alumni/career-records/add
  Auth:      ALUMNI_SELF | ENTERPRISE_ADMIN (company confirmation)
  Body:      CareerRecordRequest
  Response:  CareerRecordResponse
  Audit:     YES

POST   /alumni/career-records/{record_id}/verify
  Auth:      ENTERPRISE_ADMIN | INTERNAL_DOCUMENT_SERVICE
  Body:      CareerVerificationRequest
  Response:  CareerVerificationResponse
  Audit:     YES

POST   /alumni/mentorship/create-bond
  Auth:      ALUMNI_MENTOR (MENTOR_ALUMNI type required)
  Body:      MentorshipBondRequest
  Response:  MentorshipBondResponse
  Audit:     YES

POST   /alumni/mentorship/{bond_id}/session-complete
  Auth:      ALUMNI_MENTOR + MENTEE (dual confirmation)
  Body:      SessionCompletionRequest
  Response:  SessionCompletionResponse
  Audit:     YES

POST   /alumni/mentorship/{bond_id}/close
  Auth:      ALUMNI_MENTOR | PLATFORM_ADMIN
  Body:      BondCloseRequest
  Response:  BondCloseResponse
  Audit:     YES

POST   /alumni/referral/generate-code
  Auth:      ALUMNI (any verified type)
  Response:  ReferralCodeResponse

POST   /alumni/referral/conversion-confirm
  Auth:      INTERNAL_SERVICE (signed JWT)
  Body:      ReferralConversionRequest
  Response:  ReferralConversionResponse
  Audit:     YES

GET    /alumni/contribution/ledger/{alumni_id}
  Auth:      ALUMNI_SELF | INSTITUTION_ADMIN | PLATFORM_ADMIN
  Response:  ContributionLedgerResponse (paginated)

GET    /alumni/network/graph/{alumni_id}
  Auth:      ALUMNI_SELF | PLATFORM_ADMIN
  Response:  NetworkGraphResponse
             (edges, network_depth, depth_tier)

GET    /alumni/institution/{institution_id}/stats
  Auth:      INSTITUTION_ADMIN | RECRUITER | PUBLIC (summary only)
  Response:  InstitutionAlumniStatsResponse

GET    /alumni/institution/{institution_id}/directory
  Auth:      INSTITUTION_ADMIN | RECRUITER (verified)
  Query:     alumni_type, career_stage, domain_track, graduation_year
  Response:  AlumniDirectoryResponse (paginated)

GET    /alumni/search/similar/{alumni_id}
  Auth:      INSTITUTION_ADMIN | RECRUITER | PLATFORM_ADMIN
  Query:     vector_type, limit
  Response:  SimilarAlumniResponse (Qdrant-powered)

POST   /alumni/society/succession/create
  Auth:      SOCIETY_ADMIN (Temporal workflow trigger)
  Body:      SuccessionRequest
  Response:  SuccessionWorkflowResponse (Temporal workflow_id)
  Audit:     YES

GET    /alumni/society/succession/{succession_id}
  Auth:      SOCIETY_ADMIN | MASTER_ORGANIZER | PLATFORM_ADMIN
  Response:  SuccessionStatusResponse (Temporal workflow state)

GET    /alumni/offline/sync-queue
  Auth:      SOCIETY_ADMIN | PLATFORM_ADMIN
  Response:  OfflineSyncQueueResponse

GET    /alumni/audit/{alumni_id}
  Auth:      PLATFORM_ADMIN
  Response:  AuditLogResponse (paginated, hash-chained)
```

---

## SECTION IX — ASYNC EVENT PUBLISHING

### IX.A — Events Emitted by This Agent

```
alumni.profile_created
  Payload: alumni_id, user_id, alumni_type, origin_institution_id,
           graduation_date, trust_level

alumni.type_upgraded
  Payload: alumni_id, previous_type, new_type, trigger_event

alumni.career_stage_changed
  Payload: alumni_id, previous_stage, new_stage, trigger_reason

alumni.placement_verified
  Payload: alumni_id, institution_id, cohort_id, company_id,
           verification_source, career_record_id

alumni.career_record_verified
  Payload: alumni_id, record_id, record_type, company_id,
           verification_source

alumni.career_gap_detected
  Payload: alumni_id, days_since_active, institution_id,
           skills_at_risk_count

alumni.skill_obsolescence_detected
  Payload: alumni_id, skill_ids, days_since_used

alumni.dormant_classified
  Payload: alumni_id, days_since_active, contribution_score_at_dormancy

alumni.mentorship_bond_created
  Payload: bond_id, mentor_alumni_id, mentee_user_id, bond_type

alumni.mentorship_session_completed
  Payload: bond_id, mentor_alumni_id, mentee_user_id,
           sessions_completed, contribution_points_awarded

alumni.mentorship_outcome_achieved
  Payload: bond_id, mentor_alumni_id, mentee_user_id,
           outcome_type, contribution_points_awarded

alumni.referral_converted
  Payload: chain_id, referrer_alumni_id, referred_user_id,
           conversion_event, institution_id

alumni.referral_placement_converted
  Payload: chain_id, referrer_alumni_id, referred_user_id,
           company_id, institution_id, contribution_points_awarded

alumni.contribution_score_updated
  Payload: alumni_id, previous_score, new_score, delta,
           trigger_contribution_type

alumni.network_depth_changed
  Payload: alumni_id, previous_depth, new_depth, new_depth_tier

alumni.society_succession_completed
  Payload: succession_id, alumni_id, new_role, society_id,
           temporal_workflow_id

alumni.society_workshop_delivered
  Payload: alumni_id, society_id, batch_id, contribution_points_awarded

alumni.qdrant_embedding_refreshed
  Payload: alumni_id, vector_type, qdrant_collection, embedding_version

alumni.suppressed
  Payload: alumni_id, suppressed_by, audit_id

alumni.unsuppressed
  Payload: alumni_id, unsuppressed_by, audit_id
```

### IX.B — Consuming Services

```
Leaderboard Agent        → alumni.placement_verified
                           alumni.career_record_verified
                           alumni.mentorship_outcome_achieved
                           alumni.referral_placement_converted
                           alumni.society_workshop_delivered
                           alumni.career_gap_detected (negative signal)

Competition Agent        → alumni.placement_verified (institution signal)
                           alumni.referral_placement_converted
                           alumni.society_succession_completed

Notification Service     → alumni.career_gap_detected
                           alumni.skill_obsolescence_detected
                           alumni.dormant_classified
                           alumni.mentorship_outcome_achieved
                           alumni.referral_placement_converted
                           alumni.society_succession_completed

Intelligence Engine      → alumni.placement_verified
                           alumni.career_record_verified
                           alumni.career_stage_changed
                           (feeds placement probability models
                            for current students)

Reputation Engine (R68)  → alumni.contribution_score_updated
                           alumni.network_depth_changed
                           alumni.career_record_verified

Analytics Service        → ALL events → ClickHouse

SEO Regeneration Hook    → alumni.placement_verified
                           alumni.career_record_verified
                           alumni.type_upgraded
                           (triggers ISR revalidation of alumni
                            public pages and institution pages)

Innovation Trust Service → alumni.type_upgraded (TYPE_08 trigger)

Society Domain Layer     → alumni.society_succession_completed
                           alumni.society_workshop_delivered

Admin Governance Service → alumni.suppressed
                           alumni.unsuppressed

Billing Service          → alumni.referral_placement_converted
                           (commission attribution for referral
                            reward payout)
```

---

## SECTION X — UI SCREENS (MANDATORY)

Absence of any screen → STOP EXECUTION.

### X.A — Public SEO Screens (Next.js — R31 Governed)

```
SCREEN_P01: ALUMNI PUBLIC PROFILE PAGE
  URL:         /alumni/{alumni_id}
  Visibility:  Public
  Content:     Display name, alumni type badge, career stage,
               contribution tier (CORE | ACTIVE | STANDARD | DORMANT),
               network depth tier, verified career summary
               (company names but not salaries),
               skill passport highlights (public skills only),
               mentorship availability status,
               success story card (if R80 published),
               endorsement count, institution origin
  SEO Meta:    Auto-generated (name + alumni type + institution)
  Canonical:   YES
  ISR:         Revalidate on alumni.career_record_verified
               and alumni.type_upgraded events
  Schema.org:  Person structured data

SCREEN_P02: INSTITUTION ALUMNI DIRECTORY PAGE
  URL:         /institutions/{institution_id}/alumni
  Visibility:  Public (summary stats only)
               Full directory: INSTITUTION_ADMIN | RECRUITER
  Content (Public):
               Total alumni count, placed alumni percentage,
               active mentors count, industries represented,
               contribution score distribution chart,
               notable alumni (top 5 by contribution score),
               graduation cohort timeline
  Content (Authenticated):
               Full searchable alumni directory with filters
  SEO Meta:    "[Institution Name] Alumni Network | EcoSkiller"
  Canonical:   YES
  ISR:         Daily revalidation
```

### X.B — Authenticated Screens (Flutter — R43 Governed)

```
SCREEN_A01: ALUMNI DASHBOARD (Self)
  Access:      ALUMNI (any type)
  Content:     Career stage indicator + transition history
               Contribution score gauge + breakdown by type
               Network depth ring chart
               Active mentorship bonds (open + completed count)
               Referral chain tracker (active chains + conversions)
               Skill passport status (verified vs unverified skills)
               Career record timeline (all entries)
               Re-engagement notifications (if CAREER_GAP active)
               Society succession status (if TYPE_06)

SCREEN_A02: MENTORSHIP MANAGEMENT
  Access:      ALUMNI_MENTOR (MENTOR_ALUMNI or TRAINER_ALUMNI type)
  Content:     All active mentorship bonds list
               Create new bond flow (mentee search + bond type select)
               Session log per bond (date, duration, outcome note)
               Session completion confirmation widget
               Outcome declaration form (mentee outcome type)
               Bond close + outcome verification flow
               Contribution points earned this month
               Mentee outcome history

SCREEN_A03: REFERRAL HUB
  Access:      ALUMNI (any verified type)
  Content:     Personal referral code + QR code
               One-click share links (job, course, signup, society)
               Active referral chains list
                 (referred_user display + conversion status)
               Conversion event tracker
               Reward wallet (reward_triggered = TRUE entries)
               Referral leaderboard position (within institution cohort)
               Fraud flag notice (if R52 flag active)

SCREEN_A04: CAREER RECORD MANAGER
  Access:      ALUMNI (any type)
  Content:     Full career timeline view
               Add new career record form
                 (company, role, dates, salary band toggle)
               Verification request button
                 (triggers company confirmation flow)
               Document upload for offer hash verification
               Skill passport sync from career record
               Verification status per record
                 (COMPANY_CONFIRM | OFFER_HASH | SELF_DECLARED)

SCREEN_A05: TALENT SIMILARITY EXPLORER
  Access:      INSTITUTION_ADMIN | RECRUITER (verified)
  Content:     Search alumni by skill portfolio similarity
                 (Qdrant-powered cosine search)
               Career trajectory matching
                 (find alumni whose path matches target profile)
               Filter: alumni_type, career_stage, domain_track,
                       graduation_year, network_depth_tier
               Export to CSV
               Save search criteria as alert

SCREEN_A06: INSTITUTION ALUMNI CONTROL PANEL
  Access:      INSTITUTION_ADMIN
  Content:     Full alumni directory with advanced filters
               Contribution score leaderboard (this institution)
               Cohort-year placement attribution chart
               Alumni career gap rate monitor
               Mentor availability map
               Referral conversion funnel (by graduation cohort)
               Request alumni re-engagement campaign trigger
               Export alumni stats report

SCREEN_A07: SOCIETY ALUMNI & SUCCESSION CONSOLE
  Access:      SOCIETY_ADMIN | MASTER_ORGANIZER
  Content:     Society coach alumni list (TYPE_06)
               Succession pipeline (STUDENT → COACH ready candidates)
               Temporal workflow status per succession (live)
               Batch completion requirements progress bars
               Coordinator sign-off queue
               Offline sync status per society alumni
               District-level alumni network map
               Workshop delivery record per coach alumni

SCREEN_A08: ALUMNI ADMIN GOVERNANCE CONSOLE
  Access:      PLATFORM_ADMIN
  Content:     All alumni profiles with suppression status
               Suppression / unsuppression dual sign-off workflow
               Manipulation flag triage for contribution fraud
               Career record verification dispute resolution
               Audit log explorer (hash chain viewer)
               Qdrant embedding health dashboard
               Offline sync queue management
               Alumni type reclassification tool (with audit trail)
```

---

## SECTION XI — SOCIETY DOMAIN OFFLINE ARCHITECTURE

### XI.A — Offline Alumni Data Flows

```
OFFLINE STACK (society namespace):
  CouchDB (edge nodes at franchise locations)
  Temporal workflow engine (succession + payout governance)
  PostgreSQL logical replication (semi-connected fallback)

OFFLINE-ELIGIBLE RECORD TYPES:
  - alumni_mentorship_bonds (society workshop sessions)
  - alumni_society_succession (role transitions)
  - alumni_contribution_ledger (society domain entries)
  - alumni_network_edges (society edges)
  - CONTRIB_10, CONTRIB_11, CONTRIB_12, CONTRIB_13 payloads

SYNC FLOW:
  1. Coach delivers workshop offline
  2. Coordinator records session in CouchDB edge node
  3. On connectivity:
     → CouchDB replication → central cluster
     → Kafka topic: alumni.offline.events
  4. Alumni agent consumes Kafka topic
  5. Validates: coordinator Keycloak role confirmed
  6. Validates: originated_at not stale (max 7 days)
  7. Inserts alumni_offline_sync_queue record
  8. Processes through contribution engine after sync confirmation

TEMPORAL WORKFLOW GATES:
  SUCCESSION flow requires Temporal durable workflow:
    - Step 1: Batch completion count verified
    - Step 2: Coordinator sign-off captured (Temporal signal)
    - Step 3: SOCIETY_ADMIN sign-off captured (Temporal signal)
    - Step 4: New Keycloak role assigned
    - Step 5: alumni_society_succession record written
    - Step 6: alumni.society_succession_completed emitted
  If any step fails → Temporal retries per backoff policy
  Workflow state visible in SCREEN_A07

CONFLICT RESOLUTION (offline):
  IF two edge nodes submit conflicting session records
  for same coach + same date window:
  → Conservative rule: lower contribution value accepted
  → Conflict logged to alumni_offline_sync_queue
     with sync_status = CONFLICT
  → SOCIETY_ADMIN alerted
  → Manual resolution via SCREEN_A07
```

---

## SECTION XII — ANTI-MANIPULATION CONTROLS

All controls are deterministic rules — no heuristics.

```
CONTROL AM01: SELF-REFERRAL REJECTION
  Rule:     Alumni cannot submit referral for their own
            secondary account (same device_fingerprint
            OR same ip_subnet within 48h of signup)
  Action:   Referral chain rejected
            R52 ReferralFraudFlag created
            Contribution points BLOCKED for this chain
            Severity 3 logged to alumni_audit_log

CONTROL AM02: MENTORSHIP SESSION INFLATION
  Rule:     If alumni creates > 5 new mentorship bonds in 7 days
            OR marks > 3 sessions complete in a single 24h window
  Action:   All sessions beyond threshold flagged as PENDING_REVIEW
            Contribution points held pending ops review
            Severity 2 logged

CONTROL AM03: CAREER RECORD BACKDATING
  Rule:     Career record start_date cannot be more than
            90 days before submission date unless accompanied
            by offer_hash verification
  Action:   Record accepted as SELF_DECLARED only
            verification_source locked to SELF_DECLARED
            Cannot be upgraded to COMPANY_CONFIRM after 90 days

CONTROL AM04: CONTRIBUTION VELOCITY CAP
  Rule:     Alumni contribution_score cannot increase by
            more than 200 points in a single 30-day window
  Action:   Excess points held in pending ledger
            Released in next 30-day window if no flags
            Severity 1 logged

CONTROL AM05: SOCIETY OFFLINE TIMESTAMP MANIPULATION
  Rule:     Offline record originated_at cannot predate
            device's last confirmed online sync by > 7 days
  Action:   Record rejected with STALE_OFFLINE_RECORD error
            Severity 2 logged to alumni_audit_log

CONTROL AM06: ENDORSEMENT CLUSTER DETECTION
  Rule:     > 5 peer endorsements from alumni sharing same
            institution_id in < 48h window
  Action:   Endorsements beyond 5 flagged as unverified
            CONTRIB_09 points blocked for flagged endorsements
            Severity 2 logged

CONTROL AM07: DORMANT ALUMNI REFERRAL BLOCKING
  Rule:     Alumni in DORMANT career_stage cannot trigger
            new referral chains
  Action:   Referral code generation blocked
            Existing chains still tracked for historical record
            Alumni notified: "Re-activate profile to generate referrals"
```

---

## SECTION XIII — OBSERVABILITY REQUIREMENTS

Absence of any metric → STOP EXECUTION.

```
METRICS (Prometheus):
alumni_profiles_total                    gauge    (label: alumni_type, trust_level)
alumni_career_stage_distribution         gauge    (label: career_stage)
alumni_career_records_verified_total     counter
alumni_mentorship_bonds_active           gauge
alumni_mentorship_sessions_total         counter
alumni_referral_chains_active            gauge
alumni_referral_conversions_total        counter  (label: conversion_event)
alumni_contribution_score_updates_total  counter
alumni_career_gap_detections_total       counter
alumni_dormant_classifications_total     counter
alumni_society_successions_total         counter
alumni_offline_sync_queue_depth          gauge    (label: society_id)
alumni_qdrant_embeddings_total           gauge    (label: vector_type)
alumni_manipulation_flags_total          counter  (label: control_id, severity)

LOGS (Loki):
- Every alumni profile creation with alumni_type
- Every career stage transition with trigger_reason
- Every career record verification (source + alumni_id)
- Every mentorship bond creation and outcome
- Every referral conversion event
- Every manipulation flag creation
- Every suppression / unsuppression with admin_ids
- Every Temporal workflow step for society succession
- Every offline sync conflict with details
- Every audit hash mismatch [CRITICAL]

TRACING (OpenTelemetry / Grafana Tempo):
- Alumni profile creation span
- Career record verification span
- Contribution score computation span
- Qdrant embedding refresh span (vector_type attribute)
- Society succession Temporal workflow span

DASHBOARDS (Grafana):
Board 01: ALUMNI NETWORK HEALTH
  · Active alumni by type distribution (pie)
  · Career stage distribution (bar over time)
  · Mentorship bond creation rate (7-day rolling)
  · Referral conversion funnel (signup → application → placement)
  · Contribution score distribution histogram

Board 02: INSTITUTION ALUMNI TRUST SIGNALS
  · Placement attribution rate by institution (top 20)
  · Alumni career gap rate by institution
  · Post-placement retention signal trend
  · Alumni mentor density by institution

Board 03: SOCIETY DOMAIN ALUMNI OPS
  · Coach alumni batch delivery rate (offline + online)
  · Succession pipeline status (Temporal workflow states)
  · Offline sync queue depth per society
  · Govt/CSR scheme facilitation count trend

Board 04: MANIPULATION & TRUST INTEGRITY
  · Manipulation flag rate by control_id
  · Suppressed alumni count over time
  · Self-referral rejection rate
  · Contribution velocity cap triggers per day

ALERTS:
alumni_audit_hash_mismatch               → PagerDuty CRITICAL
alumni_offline_sync_queue_depth > 200    → Ops WARN
alumni_manipulation_flags_total
  > 10 per alumni in 24h                 → Ops WARN
Temporal succession workflow stuck > 48h → Ops WARN
qdrant embedding refresh lag > 72h      → Ops INFO
```

---

## SECTION XIV — INTERN EXECUTION MAPPING

All steps intern-executable without senior DevOps.
Absence → STOP EXECUTION.

### XIV.A — Run Alumni Agent Locally

```
Step 1: Navigate to service
  cd /backend/services/alumni_agent/

Step 2: Create virtual environment
  python -m venv venv && source venv/bin/activate

Step 3: Install dependencies
  pip install -r requirements.txt

Step 4: Copy env template
  cp .env.example .env
  # Set: DB_URL, REDIS_URL, KAFKA_BROKER,
  #      COUCHDB_URL (society dims),
  #      QDRANT_URL, QDRANT_API_KEY,
  #      TEMPORAL_HOST

Step 5: Run migrations
  alembic upgrade head

Step 6: Seed classification registries
  python seed_alumni_types.py
  python seed_contribution_types.py

Step 7: Start agent
  uvicorn main:app --reload --port 8032

Expected Output:
  ALUMNI NETWORK AGENT ONLINE
  Alumni types loaded: 8
  Contribution types loaded: 16
  Qdrant collections: 4
  Docs: http://localhost:8032/docs
```

### XIV.B — Test Core Alumni Flow

```
Step 1: Simulate graduation event
  python tools/simulate_graduation.py --user_id=TEST_001
  Expected: alumni profile created (TYPE_01 or TYPE_02)

Step 2: Add career record
  POST http://localhost:8032/alumni/career-records/add
  Body: test_career_record.json
  Expected: record created, verification_source=SELF_DECLARED

Step 3: Verify career record
  POST http://localhost:8032/alumni/career-records/{id}/verify
  Expected: verification_source=COMPANY_CONFIRM
           CONTRIB_14 awarded
           alumni.career_record_verified event emitted

Step 4: Create mentorship bond
  POST http://localhost:8032/alumni/mentorship/create-bond
  Expected: bond created, alumni_type upgraded to MENTOR_ALUMNI

Step 5: Complete session
  POST http://localhost:8032/alumni/mentorship/{bond_id}/session-complete
  Expected: CONTRIB_01 awarded, contribution_score updated

Step 6: Check contribution ledger
  GET http://localhost:8032/alumni/contribution/ledger/{alumni_id}
  Expected: 2 entries (CONTRIB_14 + CONTRIB_01)

Failure at any step → STOP → REPORT ALUMNI_AGENT_FAILURE
```

### XIV.C — Test Society Succession Flow

```
Step 1: Create test coach alumni (TYPE_06)
  python tools/create_society_coach.py --batches=3

Step 2: Trigger succession
  POST http://localhost:8032/alumni/society/succession/create
  Expected: Temporal workflow_id returned

Step 3: Check Temporal workflow status
  GET http://localhost:8032/alumni/society/succession/{id}
  Expected: workflow_state = AWAITING_COORDINATOR_SIGNOFF

Step 4: Submit coordinator sign-off (simulated)
  python tools/simulate_coordinator_signoff.py --workflow_id=X

Step 5: Submit society admin sign-off
  python tools/simulate_admin_signoff.py --workflow_id=X

Step 6: Confirm succession complete
  GET http://localhost:8032/alumni/society/succession/{id}
  Expected: workflow_state = COMPLETED
           new_role = COORDINATOR
           alumni.society_succession_completed emitted

Failure → STOP → REPORT SOCIETY_SUCCESSION_FAILURE
```

---

## SECTION XV — ENFORCEMENT SEAL

```
SECTION XV — ALUMNI_NETWORK_MANAGEMENT_AGENT ENFORCEMENT SEAL
Status: ACTIVE · FINAL · LOCKED

No system deployment may proceed unless all conditions below
are met and verified:

✔ All database tables in Section III exist with migrations
✔ All alumni types in Section II are registered in Keycloak
  and accessible via RBAC role binding
✔ All contribution types in Section IV are registered
  in contribution registry
✔ Contribution score formula in Section V is implemented
  as rule engine only — no ML, no AI inference (R28-1)
✔ Career stage state machine in Section VI is implemented
  with all declared transitions and Temporal integration
✔ R73 re-engagement triggers are active and emit events
✔ Institutional trust attribution signals in Section VII
  are wired to Leaderboard Agent signal ingestion gateway
✔ All API endpoints in Section VIII are implemented
  and contracted (R49 validator passes)
✔ All async events in Section IX are published and consumed
✔ All UI screens in Section X are implemented and wired
✔ Society offline architecture in Section XI is deployed
  with CouchDB + Temporal per society architecture doc
✔ All anti-manipulation controls in Section XII are enforced
✔ All observability requirements in Section XIII are live
  including all four Grafana dashboards
✔ All intern execution steps in Section XIV pass without error
✔ Qdrant vector collections are initialized and
  embedding refresh crons are scheduled
✔ Audit hash chain is unbroken from first write

Failure in any → STOP EXECUTION
               → REPORT ALUMNI_NETWORK_MANAGEMENT_AGENT_INCOMPLETE
               → NO DEPLOYMENT CLAIM PERMITTED

This agent is bound to:
R2   — Domain Data Models
R5   — Workflow State Machines
R10  — Security Policies
R21  — Internal Operations Console
R28  — Intelligence Cost Optimization Law
R34  — Social Groups & Posts System Law
R35  — Institution Organization System Law
R37  — Company Organization & Employee Verification Law
R39  — Core Inbuilt Platform Tools
R40  — Admin & Ops Console
R46  — Intern Execution Mapping
R49  — Automatic Contract Validator
R50  — Automated QA Test Generator
R51  — Anti-Spam & Platform Abuse Prevention
R52  — Viral Referral & Invitation Engine Law
R53  — Status, Badge & Level Progression Law
R56  — Real-World Outcome Showcase Law
R58  — Autonomous Network Effect Propagation Law
R62  — Public Transparency & Trust Report Law
R68  — Reputation & Trust Loop Law
R71  — Career Lifecycle Capture System Law
R72  — Lifetime Skill Passport Law
R73  — Career Gap & Skill Obsolescence Detection Law
R74  — Verified Institution Dependency System Law
R75  — Company Dependency & Workforce System Law
R76  — Public Shareable Career ID Law
R79  — Trust & Reputation Amplification Law
R80  — Continuous Success Story Generation Law
R86  — Trainer Student Growth & Referral System Law
R90  — Trainer Legacy & Archival System Law
R91  — Student Identity, Status & Portfolio System Law
R94  — Career & Peer Collaboration Viral Law
INTER_UNIT_COMPETITION_AGENT v1.0
LEADERBOARD_MANAGEMENT_AGENT v1.0
Society Architecture v1.0 (Qdrant + Temporal + CouchDB)

Violation → STOP → REPORT → NO CLAIM OF COMPLETION
```

---

## SECTION XVI — ANTIGRAVITY CONTRACT SUMMARY

```
The ALUMNI_NETWORK_MANAGEMENT_AGENT enforces antigravity
through seven governing principles:

1. GRADUATED USERS ARE NOT EXITS
   Every graduation event creates an alumni record.
   Alumni contribute to institutional trust for as long
   as they remain active on the platform.
   Decay without ongoing contribution is automatic.
   Departure without record is structurally blocked.

2. VERIFIED CAREER RECORDS AS TRUST CURRENCY
   Unverified career claims count at face value.
   Verified career records (company-confirmed or hash-matched)
   carry a 1.2× trust multiplier across all contribution types.
   The platform makes verification the economically rational choice.

3. MENTORSHIP AS COMPUTABLE VALUE
   Mentorship is not goodwill.
   It is a scored, audited, outcome-tracked contribution
   that directly feeds institutional reputation signals
   and alumni leaderboard positions.
   The platform makes mentorship economically rational.

4. REFERRAL CHAINS AS PERMANENT ATTRIBUTION
   Every referral that converts is permanently attributed
   to the originating alumni and their institution.
   This creates a long-term loyalty signal that
   institutions compete to maximize —
   and alumni compete to extend.

5. NETWORK DEPTH AS COMPUTABLE GRAVITY
   The more an alumni is woven into the platform graph —
   mentoring, referring, co-working, endorsing —
   the higher their network_depth and contribution score.
   High-depth alumni are the gravitational centers
   that make the platform irreplaceable to its ecosystem.

6. SOCIETY DOMAIN SUCCESSION AS STRUCTURED GROWTH
   Offline workshop coaches who demonstrate delivery capability
   are promoted through a Temporal-governed succession pipeline.
   This creates a self-replicating talent pipeline
   for the hardest-to-reach adoption layer —
   and makes every coach an alumni with ongoing platform stakes.

7. CAREER GAP AS EARLY WARNING SYSTEM
   Alumni who go silent are not ignored.
   Career gaps trigger re-engagement, skill obsolescence
   detection, and institutional notification.
   The platform treats inactivity as a signal to act on —
   not a state to accept.
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
                    LEADERBOARD_MANAGEMENT_AGENT v1.0 +
                    ECOSKILLER Society Architecture v1.0
                    (Qdrant + Temporal + CouchDB confirmed) +
                    ECOSKILLER Infrastructure v8 (k3s confirmed) +
                    Student & Parent Campus Portal Guide v1.0
```
