# SOCIETY_MAPPING_AGENT
## ECOSKILLER — ANTIGRAVITY ONBOARDING SYSTEM
**Agent Class:** Society Identity, Mapping & Offline Onboarding Agent
**Status:** FINAL · LOCKED · SEALED · DETERMINISTIC
**Mutation Policy:** Add-only via version bump
**Interpretation Authority:** NONE
**Execution Authority:** Human declaration only
**Agent Version:** v1.0
**Bound To:** ECOSKILLER MASTER EXECUTION PROMPT v12.0
**Depends On:** ROLE_ASSIGNMENT_AGENT v1.0 (must complete before this agent activates)
**Kubernetes Namespace:** society
**Architecture Mandate:** 100% open-source · fully self-hosted · offline-first · event-driven

---

## SECTION I — AGENT IDENTITY & PURPOSE

### 1.1 What This Agent Is

The SOCIETY_MAPPING_AGENT is the **structural backbone of the ECOSKILLER offline and semi-urban reach system**. It activates after the ROLE_ASSIGNMENT_AGENT has confirmed identity for any of the following roles:

```
ROLE_SOCIETY_ADMIN
ROLE_COACH
ROLE_COORDINATOR
ROLE_FRANCHISE_OWNER
ROLE_MASTER_ORGANIZER
```

It does not activate for any other role.

This agent maps every society-scoped entity — its geography, its operational structure, its human hierarchy, its batch pipeline, its commission chain, its government/CSR linkages, its tournament engine, and its offline sync posture — into a deterministic, auditable, enforceable registry.

### 1.2 Core Mandate

> "The SOCIETY_MAPPING_AGENT reads the confirmed society-role identity, resolves it against the Society Registry, initialises the society's operational structure, binds it to its geographic zone, activates the offline-first sync layer, wires the commission engine, and emits the society_ready event — in that exact order, with zero deviation, regardless of connectivity status."

### 1.3 What This Agent Is NOT

- It is NOT a generic user onboarding screen
- It is NOT a CRM tool
- It is NOT a chat or messaging layer
- It is NOT permitted to infer society boundaries from partial data
- It is NOT permitted to proceed past any gate without confirmed contracts

If geographic mapping is ambiguous → **STOP → EMIT mapping_ambiguity_flag → HUMAN RESOLUTION REQUIRED**

If offline sync layer is unavailable → **STOP → EMIT sync_layer_unavailable → HALT SOCIETY ACTIVATION**

---

## SECTION II — SYSTEM BINDING CONTRACTS

This agent is **contractually bound** to the following ECOSKILLER registries. Any of these missing → **STOP EXECUTION**.

| Contract | Source | Gate Produced |
|---|---|---|
| ROLE_ASSIGNMENT_AGENT v1.0 | Prerequisite agent | role_confirmed |
| R2 | Domain Data Models (Tenant, User) | identity_schema_ready |
| R4 | Event Schema Registry | event_schema_ready |
| R5 | Workflow State Machines | workflow_ready |
| R39-A | Identity & Access Tools | tooling_ready |
| R59 | Offline-First & Low-Bandwidth Law | offline_layer_ready |
| R60 | Long-Term Archival Law | archival_ready |
| R67 | Marketplace Liquidity Balancing | liquidity_ready |
| R74 | Institution Dependency System | institution_binding_ready |
| R75 | Company Dependency & Workforce | company_binding_ready |
| Society Architecture v1.0 | ecoskiller_society_architecture.docx | society_schema_ready |
| Keycloak Realm Extension | New society roles in multi-tenant realm | rbac_ready |
| CouchDB Offline Sync Layer | Apache CouchDB deployment | offline_sync_ready |
| Temporal Workflow Engine | Deployed in k3s society namespace | workflow_engine_ready |

---

## SECTION III — SOCIETY ENTITY REGISTRY

### 3.1 What a Society Is

In ECOSKILLER, a **Society** is the fundamental offline operational unit. It is a geographic, human-staffed, physically-anchored node of the platform that:

- Runs skill workshops in residential areas, towns, villages, or peri-urban zones
- Operates physical training batches under platform governance
- Holds local tournaments and certification events
- Collects fees and distributes commissions per platform rules
- Syncs to the central platform when connectivity permits
- Functions autonomously when offline

A society is **not** an institution. A society is **not** a company. It is a franchise-governed local operating unit.

### 3.2 Society Taxonomy

```
SOCIETY_TYPE_RESIDENTIAL_COLONY     → Urban/peri-urban gated communities or housing clusters
SOCIETY_TYPE_VILLAGE_PANCHAYAT      → Rural gram panchayat coverage zones
SOCIETY_TYPE_TOWN_WARD              → Municipal ward-level coverage
SOCIETY_TYPE_INDUSTRIAL_CLUSTER     → Industrial area / SEZ worker skill zones
SOCIETY_TYPE_TRIBAL_ZONE            → Tribal / forest-area reach zones (special offline rules)
SOCIETY_TYPE_CAMPUS_ADJACENT        → Near-campus communities (student residential zones)
SOCIETY_TYPE_CORPORATE_TOWNSHIP     → Company township residential areas
```

### 3.3 Society Status State Machine

```
DRAFT
  → society_admin_registered → PENDING_VERIFICATION
  → verification_documents_submitted → UNDER_REVIEW
  → platform_approval_granted → ACTIVE
  → franchise_agreement_signed → FRANCHISE_BOUND
  → operational_threshold_met → FULLY_OPERATIONAL
  → suspension_trigger → SUSPENDED
  → termination_initiated → GRACE_PERIOD (7-day Temporal workflow)
  → grace_period_expired → TERMINATED
  → reinstatement_approved → ACTIVE
```

### 3.4 Society Operational Threshold

A society may not be declared FULLY_OPERATIONAL until:

```
[ ] min_1_coach_assigned AND coach_verified = TRUE
[ ] min_1_coordinator_assigned AND coordinator_verified = TRUE
[ ] society_geographic_zone_mapped = TRUE
[ ] offline_sync_node_configured = TRUE
[ ] first_batch_created = TRUE
[ ] bank_account_linked_for_commission = TRUE
[ ] franchise_agreement_status = SIGNED
[ ] society_profile_completeness ≥ 80%
```

---

## SECTION IV — GEOGRAPHIC MAPPING ENGINE

### 4.1 Mapping Purpose

Every society must be **geographically anchored** before any operational action is permitted. Geographic mapping is not optional metadata. It is a hard dependency for:

- Commission zone determination
- Master Organizer territory assignment
- Conflict detection (overlapping franchise zones)
- Government scheme eligibility
- CSR program targeting
- Offline node routing

### 4.2 Geographic Data Model

```sql
CREATE TABLE society_geographic_zones (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  society_id        UUID NOT NULL REFERENCES societies(id),
  zone_type         TEXT NOT NULL,           -- maps to SOCIETY_TYPE_*
  country           TEXT NOT NULL DEFAULT 'IN',
  state_code        TEXT NOT NULL,
  district          TEXT NOT NULL,
  sub_district      TEXT,
  village_town_city TEXT NOT NULL,
  ward_number       TEXT,
  pin_code          TEXT NOT NULL,
  lat_center        DECIMAL(10, 8),
  lng_center        DECIMAL(11, 8),
  radius_km         DECIMAL(5, 2),           -- coverage radius
  boundary_geojson  JSONB,                   -- optional polygon boundary
  mapped_at         TIMESTAMP NOT NULL DEFAULT NOW(),
  mapped_by         UUID NOT NULL REFERENCES users(id),
  is_verified       BOOLEAN NOT NULL DEFAULT FALSE,
  verified_at       TIMESTAMP,
  UNIQUE (pin_code, ward_number)             -- prevents duplicate zone claims
);

CREATE TABLE society_zone_conflicts (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  society_a_id      UUID NOT NULL REFERENCES societies(id),
  society_b_id      UUID NOT NULL REFERENCES societies(id),
  overlap_type      TEXT NOT NULL,           -- PARTIAL / FULL / BOUNDARY
  detected_at       TIMESTAMP NOT NULL DEFAULT NOW(),
  resolution_status TEXT NOT NULL DEFAULT 'PENDING',
  resolved_by       UUID REFERENCES users(id),
  resolved_at       TIMESTAMP
);
```

### 4.3 Geographic Mapping Algorithm

```
FUNCTION map_society_zone(society_id, zone_payload):

  STEP 1: VALIDATE_ZONE_PAYLOAD
    → check required fields: state_code, district, village_town_city, pin_code
    → if missing → EMIT zone_validation_failed → return HTTP 422 + field errors

  STEP 2: CONFLICT_DETECTION
    → query society_geographic_zones WHERE pin_code = payload.pin_code
    → if existing active society found:
      → compute overlap_type
      → if overlap_type = FULL → EMIT zone_conflict_detected → STOP → escalate to MASTER_ORGANIZER
      → if overlap_type = PARTIAL → EMIT zone_overlap_warning → log to society_zone_conflicts → allow with flag
      → if overlap_type = BOUNDARY → log only → proceed

  STEP 3: WRITE_ZONE_RECORD
    → insert into society_geographic_zones
    → set is_verified = FALSE (pending platform review)
    → emit society.zone_mapped event

  STEP 4: MASTER_ORGANIZER_TERRITORY_BINDING
    → query master_organizer_territories WHERE zone intersects territory
    → if match found → bind society to MASTER_ORGANIZER
    → if no match → assign to platform_default_territory → flag for territory assignment

  STEP 5: GOVERNMENT_SCHEME_ELIGIBILITY_CHECK
    → query govt_scheme_eligibility_rules WHERE state_code + zone_type match
    → if eligible schemes found → pre-populate society_govt_scheme_applications
    → emit society.scheme_eligibility_detected

  STEP 6: EMIT_COMPLETE
    → emit society.zone_mapping_complete { society_id, zone_id, conflict_status }
    → return zone_id + conflict_flags
```

---

## SECTION V — SOCIETY HUMAN HIERARCHY

### 5.1 Hierarchy Definition

Every society has a fixed, role-governed human hierarchy. Roles are pulled from the ROLE_ASSIGNMENT_AGENT registry. Hierarchy rules are enforced by this agent.

```
MASTER_ORGANIZER (platform-level, governs territory of multiple societies)
  └── FRANCHISE_OWNER (society-level owner, financial & legal accountability)
        └── SOCIETY_ADMIN (operational manager, day-to-day governance)
              ├── COACH (skill delivery — teaches batches)
              └── COORDINATOR (logistics — manages enrollments, attendance, fees)
```

### 5.2 Hierarchy Binding Rules

```
RULE-SH-01: Every society must have exactly ONE active FRANCHISE_OWNER
RULE-SH-02: Every society must have exactly ONE active SOCIETY_ADMIN
            (FRANCHISE_OWNER may also hold SOCIETY_ADMIN if approved by MASTER_ORGANIZER)
RULE-SH-03: A society must have minimum ONE COACH before first batch creation
RULE-SH-04: A society must have minimum ONE COORDINATOR before first enrollment
RULE-SH-05: MASTER_ORGANIZER governs a territory — may oversee 5–50 societies
RULE-SH-06: A COACH may not manage their own enrollment records (coordinator separation)
RULE-SH-07: FRANCHISE_OWNER financial records are read-only to COACH and COORDINATOR
RULE-SH-08: Hierarchy changes (role reassignment within a society) require MASTER_ORGANIZER approval
RULE-SH-09: A person may hold COACH + COORDINATOR in a society only if society population < 3 people
            AND MASTER_ORGANIZER explicitly grants dual-role exception
RULE-SH-10: If FRANCHISE_OWNER exits, society enters SUSPENDED state for 7-day Temporal grace workflow
```

### 5.3 Hierarchy Database Schema

```sql
CREATE TABLE societies (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id             UUID NOT NULL REFERENCES tenants(id),
  name                  TEXT NOT NULL,
  society_type          TEXT NOT NULL,
  status                TEXT NOT NULL DEFAULT 'DRAFT',
  franchise_owner_id    UUID REFERENCES users(id),
  society_admin_id      UUID REFERENCES users(id),
  master_organizer_id   UUID REFERENCES users(id),
  profile_completeness  INT NOT NULL DEFAULT 0,
  created_at            TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at            TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE society_members (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  society_id      UUID NOT NULL REFERENCES societies(id),
  user_id         UUID NOT NULL REFERENCES users(id),
  role_name       TEXT NOT NULL,           -- COACH / COORDINATOR / etc.
  assigned_by     UUID NOT NULL REFERENCES users(id),
  assigned_at     TIMESTAMP NOT NULL DEFAULT NOW(),
  is_active       BOOLEAN NOT NULL DEFAULT TRUE,
  deactivated_at  TIMESTAMP,
  UNIQUE (society_id, user_id, role_name)
);

CREATE TABLE master_organizer_territories (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  master_organizer_id   UUID NOT NULL REFERENCES users(id),
  territory_name        TEXT NOT NULL,
  state_codes           TEXT[] NOT NULL,
  district_codes        TEXT[],
  max_societies         INT NOT NULL DEFAULT 50,
  current_society_count INT NOT NULL DEFAULT 0,
  created_at            TIMESTAMP NOT NULL DEFAULT NOW()
);
```

---

## SECTION VI — BATCH & WORKSHOP PIPELINE

### 6.1 Batch Definition

A **Batch** is the atomic unit of skill delivery in a society. Every batch:

- Has a defined skill track (mapped to ECOSKILLER Skill Registry)
- Has a coach (ROLE_COACH) assigned
- Has a coordinator (ROLE_COORDINATOR) managing it
- Has a defined schedule (days, times, duration)
- Has a venue (physical address within the society zone)
- Has a fee structure (per enrollment, per session, or subscription)
- Operates offline-first with sync to central platform

### 6.2 Batch State Machine

```
DRAFT
  → coach_assigned + venue_confirmed → SCHEDULED
  → enrollment_open → ENROLLMENT_OPEN
  → min_enrollment_threshold_met → CONFIRMED
  → first_session_started → IN_PROGRESS
  → all_sessions_completed → COMPLETED
  → assessments_done → PENDING_CERTIFICATION
  → certifications_issued → CLOSED
  → low_enrollment_before_start → CANCELLED
  → coach_unavailable → SUSPENDED → reassignment → IN_PROGRESS
```

### 6.3 Batch Database Schema

```sql
CREATE TABLE batches (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  society_id          UUID NOT NULL REFERENCES societies(id),
  skill_track_id      UUID NOT NULL,               -- FK to platform skill registry
  batch_name          TEXT NOT NULL,
  coach_id            UUID NOT NULL REFERENCES users(id),
  coordinator_id      UUID NOT NULL REFERENCES users(id),
  venue_address       TEXT NOT NULL,
  venue_lat           DECIMAL(10, 8),
  venue_lng           DECIMAL(11, 8),
  max_capacity        INT NOT NULL DEFAULT 30,
  current_enrollment  INT NOT NULL DEFAULT 0,
  min_enrollment      INT NOT NULL DEFAULT 5,
  fee_amount          DECIMAL(10, 2) NOT NULL,
  fee_type            TEXT NOT NULL,               -- PER_SESSION / FULL_BATCH / SUBSCRIPTION
  schedule_days       TEXT[] NOT NULL,             -- ['MON','WED','FRI']
  session_time        TIME NOT NULL,
  session_duration_min INT NOT NULL DEFAULT 90,
  total_sessions      INT NOT NULL,
  start_date          DATE NOT NULL,
  end_date            DATE,
  status              TEXT NOT NULL DEFAULT 'DRAFT',
  created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE batch_enrollments (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  batch_id        UUID NOT NULL REFERENCES batches(id),
  student_id      UUID NOT NULL REFERENCES users(id),
  enrolled_at     TIMESTAMP NOT NULL DEFAULT NOW(),
  fee_paid        BOOLEAN NOT NULL DEFAULT FALSE,
  fee_paid_at     TIMESTAMP,
  payment_method  TEXT,                            -- CASH / UPI / ONLINE
  status          TEXT NOT NULL DEFAULT 'ACTIVE',  -- ACTIVE / DROPPED / COMPLETED
  UNIQUE (batch_id, student_id)
);

CREATE TABLE batch_sessions (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  batch_id        UUID NOT NULL REFERENCES batches(id),
  session_number  INT NOT NULL,
  session_date    DATE NOT NULL,
  start_time      TIME NOT NULL,
  end_time        TIME,
  venue_confirmed TEXT,
  coach_confirmed BOOLEAN NOT NULL DEFAULT FALSE,
  status          TEXT NOT NULL DEFAULT 'SCHEDULED',
  offline_logged  BOOLEAN NOT NULL DEFAULT FALSE,   -- was this logged offline?
  synced_at       TIMESTAMP,                        -- when CouchDB sync completed
  notes           TEXT
);

CREATE TABLE batch_attendance (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  session_id      UUID NOT NULL REFERENCES batch_sessions(id),
  student_id      UUID NOT NULL REFERENCES users(id),
  status          TEXT NOT NULL,                   -- PRESENT / ABSENT / LATE
  marked_by       UUID NOT NULL REFERENCES users(id),
  marked_at       TIMESTAMP NOT NULL DEFAULT NOW(),
  offline_queued  BOOLEAN NOT NULL DEFAULT FALSE,
  synced_at       TIMESTAMP,
  UNIQUE (session_id, student_id)
);
```

### 6.4 Batch Creation Algorithm

```
FUNCTION create_batch(society_id, batch_payload):

  STEP 1: AUTHORITY_CHECK
    → verify caller has ROLE_SOCIETY_ADMIN or ROLE_FRANCHISE_OWNER in society
    → if not → EMIT unauthorised_batch_creation → return HTTP 403

  STEP 2: SOCIETY_READINESS_CHECK
    → verify society.status = ACTIVE or FULLY_OPERATIONAL
    → verify min_1_coach_assigned = TRUE
    → verify min_1_coordinator_assigned = TRUE
    → if any fail → return HTTP 409 + specific failure reason

  STEP 3: SKILL_TRACK_VALIDATION
    → validate skill_track_id exists in platform skill registry
    → if invalid → return HTTP 422 + suggest valid tracks

  STEP 4: SCHEDULE_CONFLICT_CHECK
    → for coach_id: query existing batches with overlapping schedule
    → if conflict → EMIT coach_schedule_conflict → return HTTP 409 + conflict details

  STEP 5: FEE_VALIDATION
    → validate fee_amount > 0
    → validate fee_type is in allowed enum
    → compute platform_commission_split (see Section VIII)

  STEP 6: WRITE_BATCH
    → insert into batches with status = DRAFT
    → emit batch.created event

  STEP 7: ENROLLMENT_OPEN_TRIGGER
    → if batch.start_date > today + 7 days → auto set enrollment_open = TRUE
    → emit batch.enrollment_opened event
    → trigger notification to society local feed

  ON ANY STEP FAILURE:
    → ROLLBACK → EMIT batch_creation_failed { step, reason }
```

---

## SECTION VII — TOURNAMENT ENGINE (SOCIETY-SCOPED)

### 7.1 Tournament Purpose

Society-level tournaments are **competitive skill-validation events** run physically within the society zone. They are the primary mechanism for:

- Identifying top performers for platform-wide visibility
- Generating platform gravity (non-members attend to watch → register)
- Driving coach and coordinator reputation scores
- Triggering belt/certification advancement review

### 7.2 Tournament State Machine

```
DRAFT
  → registration_open → REGISTRATION_OPEN
  → min_participants_registered → CONFIRMED
  → tournament_day_started → IN_PROGRESS
  → all_rounds_complete → SCORING
  → scores_validated → RESULTS_PUBLISHED
  → certifications_dispatched → CLOSED
  → insufficient_registrations → CANCELLED
```

### 7.3 Tournament Database Schema

```sql
CREATE TABLE society_tournaments (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  society_id          UUID NOT NULL REFERENCES societies(id),
  skill_track_id      UUID NOT NULL,
  tournament_name     TEXT NOT NULL,
  tournament_type     TEXT NOT NULL,             -- OPEN / BATCH_ONLY / INTER_SOCIETY
  organised_by        UUID NOT NULL REFERENCES users(id),
  venue_address       TEXT NOT NULL,
  tournament_date     DATE NOT NULL,
  registration_fee    DECIMAL(10,2) NOT NULL DEFAULT 0,
  prize_structure     JSONB,
  min_participants    INT NOT NULL DEFAULT 10,
  max_participants    INT,
  status              TEXT NOT NULL DEFAULT 'DRAFT',
  scoring_schema_id   UUID,                       -- FK to platform scoring engine
  created_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE tournament_registrations (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tournament_id     UUID NOT NULL REFERENCES society_tournaments(id),
  participant_id    UUID NOT NULL REFERENCES users(id),
  registered_at     TIMESTAMP NOT NULL DEFAULT NOW(),
  fee_paid          BOOLEAN NOT NULL DEFAULT FALSE,
  is_external       BOOLEAN NOT NULL DEFAULT FALSE,  -- non-platform user attending
  UNIQUE (tournament_id, participant_id)
);

CREATE TABLE tournament_results (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tournament_id     UUID NOT NULL REFERENCES society_tournaments(id),
  participant_id    UUID NOT NULL REFERENCES users(id),
  rank_position     INT NOT NULL,
  score             DECIMAL(10, 4) NOT NULL,
  judge_id          UUID REFERENCES users(id),
  result_locked     BOOLEAN NOT NULL DEFAULT FALSE,
  locked_at         TIMESTAMP,
  dispute_raised    BOOLEAN NOT NULL DEFAULT FALSE
);
```

---

## SECTION VIII — COMMISSION & FINANCE ENGINE

### 8.1 Commission Architecture

The commission engine is the financial backbone of the society model. It is **deterministic, rule-based, and auditable**. No ML. No inference. Rules only.

### 8.2 Commission Split Registry (LOCKED)

```
COMMISSION_SPLIT_RULES:

  For every fee collected in a society batch or tournament:

  PLATFORM_CUT:         15%   → credited to ECOSKILLER platform wallet
  MASTER_ORGANIZER_CUT:  5%   → credited to territory master organizer wallet
  FRANCHISE_OWNER_CUT:  30%   → credited to franchise owner wallet
  COACH_CUT:            35%   → credited to coach wallet (performance-weighted)
  COORDINATOR_CUT:      15%   → credited to coordinator wallet

  TOTAL:               100%

  RULES:
  RULE-CF-01: Platform cut deducted first before any distribution
  RULE-CF-02: Coach cut is base 35% — reduced by absence_penalty_rate if session skipped
  RULE-CF-03: Coordinator cut subject to enrollment_accuracy_score (≥ 90% = full cut)
  RULE-CF-04: No payout disbursed until 7-day hold window expires (Temporal workflow)
  RULE-CF-05: Cash payments logged by coordinator → flagged for FRANCHISE_OWNER confirmation
  RULE-CF-06: All commission ledger entries are immutable once written
  RULE-CF-07: Disputed entries freeze the commission chain — MASTER_ORGANIZER resolves
  RULE-CF-08: GST/VAT deduction applied before split per R13 (Billing & Subscription Service)
  RULE-CF-09: Minimum payout threshold: ₹100 — below this, amount held to next cycle
  RULE-CF-10: Monthly settlement cycle — Temporal scheduler triggers on 1st of each month
```

### 8.3 Commission Database Schema

```sql
CREATE TABLE society_commission_ledger (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  society_id          UUID NOT NULL REFERENCES societies(id),
  source_type         TEXT NOT NULL,             -- BATCH_FEE / TOURNAMENT_FEE / WORKSHOP_FEE
  source_id           UUID NOT NULL,             -- FK to source event
  gross_amount        DECIMAL(12, 4) NOT NULL,
  platform_cut        DECIMAL(12, 4) NOT NULL,
  master_org_cut      DECIMAL(12, 4) NOT NULL,
  franchise_cut       DECIMAL(12, 4) NOT NULL,
  coach_cut           DECIMAL(12, 4) NOT NULL,
  coordinator_cut     DECIMAL(12, 4) NOT NULL,
  gst_deducted        DECIMAL(12, 4) NOT NULL DEFAULT 0,
  transaction_date    TIMESTAMP NOT NULL DEFAULT NOW(),
  hold_expires_at     TIMESTAMP NOT NULL,        -- 7-day hold
  is_disputed         BOOLEAN NOT NULL DEFAULT FALSE,
  is_settled          BOOLEAN NOT NULL DEFAULT FALSE,
  settled_at          TIMESTAMP,
  entry_hash          TEXT NOT NULL              -- SHA-256 of entry for immutability
);

CREATE TABLE society_wallet_balances (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  owner_id        UUID NOT NULL REFERENCES users(id),
  society_id      UUID NOT NULL REFERENCES societies(id),
  available_balance  DECIMAL(12, 4) NOT NULL DEFAULT 0,
  held_balance       DECIMAL(12, 4) NOT NULL DEFAULT 0,
  lifetime_earned    DECIMAL(12, 4) NOT NULL DEFAULT 0,
  last_updated    TIMESTAMP NOT NULL DEFAULT NOW(),
  UNIQUE (owner_id, society_id)
);

CREATE TABLE society_payout_requests (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  owner_id        UUID NOT NULL REFERENCES users(id),
  society_id      UUID NOT NULL REFERENCES societies(id),
  amount_requested DECIMAL(12, 4) NOT NULL,
  bank_account_ref TEXT NOT NULL,
  upi_id          TEXT,
  status          TEXT NOT NULL DEFAULT 'PENDING',
  requested_at    TIMESTAMP NOT NULL DEFAULT NOW(),
  processed_at    TIMESTAMP,
  temporal_workflow_id TEXT                      -- Temporal run ID
);
```

### 8.4 Payout Workflow (Temporal)

```
PAYOUT_WORKFLOW (7-day enforcement):

  TRIGGER: payout_request.created
  ENGINE: Temporal Workflow Engine

  DAY 0:  Request received → validate bank_account_ref
  DAY 1:  Hold check — confirm no active disputes
  DAY 3:  Intermediate audit check — flag anomalies to MASTER_ORGANIZER
  DAY 7:  Hold expires → initiate bank transfer
          → update society_wallet_balances
          → emit payout.completed event
          → notify owner via push + SMS

  IF dispute_raised between DAY 0 and DAY 7:
    → pause workflow
    → emit payout.frozen { dispute_id }
    → resume only when dispute.resolved event received

  IF bank_transfer_failed:
    → retry 3 times (exponential backoff)
    → on 3rd failure → escalate to SUPER_ADMIN
    → emit payout.failed { reason }
```

---

## SECTION IX — GOVERNMENT & CSR INTEGRATION ENGINE

### 9.1 Purpose

Society nodes are the **last-mile delivery mechanism** for government skill schemes and CSR programs. This engine maps eligible societies to active schemes and manages documentation, compliance, and disbursement tracking.

### 9.2 Government Scheme Registry

```sql
CREATE TABLE govt_skill_schemes (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  scheme_name       TEXT NOT NULL,
  scheme_code       TEXT UNIQUE NOT NULL,         -- e.g. PMKVY_4.0, DDUGKY
  governing_body    TEXT NOT NULL,                -- MSDE, NSDC, State Govt
  eligible_states   TEXT[] NOT NULL,
  eligible_zone_types TEXT[] NOT NULL,            -- maps to SOCIETY_TYPE_*
  funding_per_student DECIMAL(10, 2),
  certification_body TEXT,
  scheme_start_date DATE NOT NULL,
  scheme_end_date   DATE,
  is_active         BOOLEAN NOT NULL DEFAULT TRUE,
  documentation_required TEXT[] NOT NULL,         -- list of required docs
  created_at        TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE society_scheme_applications (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  society_id        UUID NOT NULL REFERENCES societies(id),
  scheme_id         UUID NOT NULL REFERENCES govt_skill_schemes(id),
  application_status TEXT NOT NULL DEFAULT 'DRAFT',
  submitted_at      TIMESTAMP,
  approved_at       TIMESTAMP,
  documents         JSONB,                         -- MinIO refs
  batch_id          UUID REFERENCES batches(id),   -- linked batch
  students_enrolled INT NOT NULL DEFAULT 0,
  funding_received  DECIMAL(12, 2) NOT NULL DEFAULT 0,
  temporal_workflow_id TEXT,
  UNIQUE (society_id, scheme_id, batch_id)
);

CREATE TABLE csr_programs (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  company_id        UUID NOT NULL,                 -- FK to company tenant
  program_name      TEXT NOT NULL,
  focus_skill_tracks TEXT[] NOT NULL,
  target_zone_types TEXT[] NOT NULL,
  funding_per_student DECIMAL(10, 2),
  total_budget      DECIMAL(14, 2),
  program_start_date DATE NOT NULL,
  program_end_date  DATE,
  is_active         BOOLEAN NOT NULL DEFAULT TRUE,
  created_at        TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE society_csr_linkages (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  society_id        UUID NOT NULL REFERENCES societies(id),
  csr_program_id    UUID NOT NULL REFERENCES csr_programs(id),
  linked_batch_id   UUID REFERENCES batches(id),
  status            TEXT NOT NULL DEFAULT 'PENDING',
  students_covered  INT NOT NULL DEFAULT 0,
  funding_disbursed DECIMAL(12, 2) NOT NULL DEFAULT 0,
  UNIQUE (society_id, csr_program_id)
);
```

### 9.3 Scheme Eligibility Algorithm

```
FUNCTION check_scheme_eligibility(society_id):

  STEP 1: LOAD_SOCIETY_ZONE
    → fetch society_geographic_zones WHERE society_id = ?
    → if zone not mapped → return INELIGIBLE (reason: zone_not_mapped)

  STEP 2: QUERY_ACTIVE_SCHEMES
    → SELECT schemes WHERE state_code IN society.state_codes
      AND zone_type = society.society_type
      AND scheme_end_date > TODAY
      AND is_active = TRUE

  STEP 3: FOR EACH eligible scheme:
    → check if society_scheme_application already exists
    → if not → auto-create DRAFT application
    → pre-populate available fields from society profile
    → emit scheme.eligibility_detected { society_id, scheme_id }

  STEP 4: CSR_MATCH
    → SELECT csr_programs WHERE focus_skill_tracks OVERLAP society.active_skill_tracks
      AND target_zone_types CONTAINS society.society_type
      AND is_active = TRUE
    → auto-create society_csr_linkages DRAFT records for matches
    → emit csr.match_detected { society_id, csr_program_id }

  RETURN: { eligible_schemes[], csr_matches[], total_potential_funding }
```

---

## SECTION X — OFFLINE SYNC ARCHITECTURE

### 10.1 Offline Mandate

ECOSKILLER societies operate in zones with **intermittent, low-bandwidth, or zero connectivity**. The offline sync layer is therefore not an enhancement — it is a **structural requirement**. Any society-scoped operation must function fully offline and sync correctly when connectivity resumes.

This is governed by **R59 (Offline-First & Low-Bandwidth Law)** and the society architecture's CouchDB mandate.

### 10.2 Sync Stack

```
PRIMARY ENGINE:  Apache CouchDB (strict offline-first for rural/zero-connectivity zones)
ALTERNATIVE:     PostgreSQL logical replication to edge nodes (semi-connected zones)
SELECTION RULE:  IF society_type IN [VILLAGE_PANCHAYAT, TRIBAL_ZONE] → USE CouchDB
                 IF society_type IN [RESIDENTIAL_COLONY, CAMPUS_ADJACENT] → USE PostgreSQL replica
                 DEFAULT: CouchDB (safer)
```

### 10.3 Offline-Capable Operations (LOCKED)

The following operations MUST function fully offline:

```
OFFLINE_CAPABLE:
  ✔ batch_attendance_marking
  ✔ enrollment_recording (cash payment)
  ✔ session_notes_logging
  ✔ student_profile_viewing (cached)
  ✔ batch_schedule_viewing (cached)
  ✔ tournament_score_entry
  ✔ expense_logging
  ✔ offline_quiz_delivery

OFFLINE_RESTRICTED (require connectivity confirmation):
  ✗ commission_disbursement
  ✗ government_scheme_submission
  ✗ certification_issuance
  ✗ new_society_registration
  ✗ franchise_agreement_signing
  ✗ bank_account_linking
```

### 10.4 Sync Protocol

```
SYNC_TRIGGER_CONDITIONS:
  1. Network detected after offline period
  2. Manual sync triggered by COORDINATOR
  3. Scheduled background sync (every 6 hours if online)

SYNC_SEQUENCE:
  STEP 1: CONNECT → verify CouchDB replication channel
  STEP 2: PUSH local changes → pending_action_queue FIFO
  STEP 3: PULL central changes → overwrite cache
  STEP 4: CONFLICT_RESOLUTION:
    → for attendance: last_writer_wins with timestamp comparison
    → for fee_records: COORDINATOR record takes precedence over COACH record
    → for scores: higher authority (COACH > COORDINATOR) wins
    → for disputed records: FREEZE → emit sync_conflict_detected → MASTER_ORGANIZER resolution
  STEP 5: EMIT sync.completed { records_pushed, records_pulled, conflicts_detected }
  STEP 6: UPDATE sync_checkpoint_log

OFFLINE_INDICATOR_RULES:
  → App bar banner: "OFFLINE MODE" (red) when disconnected
  → App bar banner: "SYNCING..." (amber) during sync
  → App bar banner: "SYNCED" (green, 3s) on success
  → All offline-restricted actions show: "This action requires internet connection"
```

### 10.5 Sync Database Schema

```sql
CREATE TABLE society_sync_checkpoints (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  society_id        UUID NOT NULL REFERENCES societies(id),
  device_id         TEXT NOT NULL,
  last_sync_at      TIMESTAMP,
  records_pushed    INT NOT NULL DEFAULT 0,
  records_pulled    INT NOT NULL DEFAULT 0,
  conflicts_count   INT NOT NULL DEFAULT 0,
  sync_status       TEXT NOT NULL DEFAULT 'NEVER_SYNCED',
  couchdb_seq       TEXT                           -- CouchDB sequence token
);

CREATE TABLE pending_action_queue (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  society_id      UUID NOT NULL REFERENCES societies(id),
  device_id       TEXT NOT NULL,
  action_type     TEXT NOT NULL,
  action_payload  JSONB NOT NULL,
  queued_at       TIMESTAMP NOT NULL DEFAULT NOW(),
  synced_at       TIMESTAMP,
  sync_status     TEXT NOT NULL DEFAULT 'PENDING',
  retry_count     INT NOT NULL DEFAULT 0,
  error_log       TEXT
);

CREATE TABLE sync_conflict_log (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  society_id      UUID NOT NULL REFERENCES societies(id),
  entity_type     TEXT NOT NULL,
  entity_id       UUID NOT NULL,
  local_value     JSONB NOT NULL,
  central_value   JSONB NOT NULL,
  resolution      TEXT,
  resolved_by     UUID REFERENCES users(id),
  resolved_at     TIMESTAMP,
  created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);
```

---

## SECTION XI — SOCIETY MAPPING ALGORITHM (MASTER — LOCKED)

This is the master algorithm executed by the SOCIETY_MAPPING_AGENT. It is called once per society per status transition.

```
FUNCTION society_mapping_pipeline(user_id, signal):

  PRECONDITION:
    → verify ROLE_ASSIGNMENT_AGENT has emitted role.assigned for this user
    → verify role IS IN [ROLE_SOCIETY_ADMIN, ROLE_FRANCHISE_OWNER,
                         ROLE_MASTER_ORGANIZER, ROLE_COACH, ROLE_COORDINATOR]
    → if precondition fails → HALT → return error: prerequisite_agent_incomplete

  STEP 1: LOAD_ROLE_CONTEXT
    → fetch user_role_assignments for user_id
    → determine primary_society_role
    → determine if society already exists or is being created

  STEP 2: SOCIETY_RECORD_RESOLUTION
    IF new society creation:
      → create_society_record()
      → status = DRAFT
      → emit society.created
    IF joining existing society:
      → validate invitation_token
      → add to society_members
      → emit society.member_joined

  STEP 3: GEOGRAPHIC_MAPPING
    → invoke map_society_zone() (Section IV)
    → wait for zone_mapping_complete event
    → if mapping failed → HALT → report zone_mapping_failure

  STEP 4: HIERARCHY_VALIDATION
    → invoke validate_hierarchy_rules() (Section V)
    → check RULE-SH-01 through RULE-SH-10
    → if violation → emit hierarchy_violation { rule_code } → return 409

  STEP 5: OFFLINE_NODE_PROVISIONING
    → determine sync_engine (CouchDB vs PostgreSQL replica) from zone_type
    → provision society CouchDB database OR configure replication slot
    → write to society_sync_checkpoints with sync_status = CONFIGURED
    → emit offline_node.provisioned

  STEP 6: FRANCHISE_AGREEMENT_CHECK
    → if role = FRANCHISE_OWNER AND franchise_agreement_status != SIGNED:
      → dispatch FRANCHISE_AGREEMENT_TEMPORAL_WORKFLOW
      → set society.status = PENDING_FRANCHISE_AGREEMENT
      → emit franchise.agreement_required

  STEP 7: SCHEME_ELIGIBILITY_SCAN
    → invoke check_scheme_eligibility() (Section IX)
    → populate eligible scheme drafts
    → emit scheme.eligibility_report { society_id, schemes[], csr_matches[] }

  STEP 8: COMMISSION_ENGINE_INIT
    → load COMMISSION_SPLIT_RULES for society
    → create commission wallet records for all active society_members
    → emit commission_engine.initialized

  STEP 9: ONBOARDING_TRACK_DISPATCH
    → dispatch SocietyOnboardingTrack[primary_society_role] (Section XII)
    → emit society.onboarding_initiated

  STEP 10: SOCIETY_READY_GATE
    → if all above steps pass → emit society.ready { society_id, zone_id, sync_node_id }
    → update society.status = ACTIVE

  ON ANY STEP FAILURE:
    → ROLLBACK partial writes
    → EMIT pipeline_failure { step, reason, society_id }
    → SET society.status = MAPPING_FAILED
    → LOG to admin ops console (R40)
    → NO PARTIAL ACTIVE STATE PERMITTED
```

---

## SECTION XII — SOCIETY ONBOARDING TRACKS (BY ROLE)

### 12.1 Track Registry

```
TRACK[ROLE_FRANCHISE_OWNER]:
  Step 1: Society profile setup
          (name, type, description, coverage zone declaration)
  Step 2: Legal entity information
          (proprietor/company name, GST, PAN, business address)
  Step 3: Bank account linking for commission disbursement
  Step 4: Franchise agreement review + digital signature
          (via Digital Signature Service, R15 Legal Document Generation)
  Step 5: Geographic zone mapping (interactive map or PIN code entry)
  Step 6: First SOCIETY_ADMIN assignment (may be self)
  Step 7: First COACH invitation (email + phone)
  Step 8: First COORDINATOR invitation
  Step 9: First batch creation walkthrough
  Step 10: Offline sync node test (one mandatory test sync)
  Completion Gate:
    franchise_agreement_signed = TRUE
    AND zone_mapped = TRUE
    AND min_1_coach_invited = TRUE
    AND offline_sync_test_passed = TRUE
  Emit: society.onboarding_complete { role: ROLE_FRANCHISE_OWNER }

TRACK[ROLE_SOCIETY_ADMIN]:
  Step 1: Admin profile setup (if not FRANCHISE_OWNER)
  Step 2: Society operational overview
          (review existing society structure, batches, members)
  Step 3: Batch management walkthrough
  Step 4: Enrollment management walkthrough
  Step 5: Offline mode training (mandatory demo run)
  Step 6: Commission dashboard orientation
  Completion Gate:
    society_operational_overview_confirmed = TRUE
    AND offline_mode_demo_completed = TRUE
  Emit: society.onboarding_complete { role: ROLE_SOCIETY_ADMIN }

TRACK[ROLE_COACH]:
  Step 1: Coach profile setup
          (specialisation, teaching experience, skill certifications)
  Step 2: Skill track declaration (minimum 1 primary track)
  Step 3: Availability schedule setup
  Step 4: Batch assignment acceptance
  Step 5: Attendance marking tool walkthrough (offline demo mandatory)
  Step 6: Assessment delivery walkthrough
  Completion Gate:
    skill_track_declared = TRUE
    AND availability_set = TRUE
    AND offline_attendance_demo_completed = TRUE
  Emit: society.onboarding_complete { role: ROLE_COACH }

TRACK[ROLE_COORDINATOR]:
  Step 1: Coordinator profile setup
  Step 2: Enrollment management walkthrough
  Step 3: Fee collection tool orientation (cash + UPI + online)
  Step 4: Attendance sync tool walkthrough
  Step 5: Communication tools walkthrough (student notifications)
  Step 6: Offline mode training (mandatory demo run)
  Completion Gate:
    fee_collection_demo_completed = TRUE
    AND offline_mode_demo_completed = TRUE
  Emit: society.onboarding_complete { role: ROLE_COORDINATOR }

TRACK[ROLE_MASTER_ORGANIZER]:
  Step 1: Territory profile setup
          (state coverage, district coverage, max_societies)
  Step 2: Society portfolio review (existing active societies in territory)
  Step 3: Commission oversight orientation
  Step 4: Dispute resolution tool walkthrough
  Step 5: Payout governance review
  Step 6: Government scheme dashboard orientation
  Completion Gate:
    territory_profile_complete = TRUE
    AND society_portfolio_reviewed = TRUE
  Emit: society.onboarding_complete { role: ROLE_MASTER_ORGANIZER }
```

---

## SECTION XIII — API CONTRACT (SOCIETY MAPPING SERVICE)

All endpoints must conform to R3 (OpenAPI 3.1). These endpoints are **mandatory**.

```yaml
/societies/create:
  POST:
    summary: Create new society record
    auth: jwt [ROLE_FRANCHISE_OWNER | ROLE_SUPER_ADMIN]
    body: { name, society_type, description }
    responses:
      201: { society_id, status: DRAFT }
      403: { error: INSUFFICIENT_ROLE }

/societies/{id}/zone/map:
  POST:
    summary: Map geographic zone to society
    auth: jwt [ROLE_FRANCHISE_OWNER | ROLE_SOCIETY_ADMIN]
    body: { state_code, district, village_town_city, pin_code, lat_center?, lng_center? }
    responses:
      200: { zone_id, conflict_status, master_organizer_assigned }
      409: { error: ZONE_CONFLICT_FULL, conflict_details }

/societies/{id}/members/add:
  POST:
    summary: Add member to society with role
    auth: jwt [ROLE_FRANCHISE_OWNER | ROLE_SOCIETY_ADMIN]
    body: { user_id, role_name }
    responses:
      200: { member_id, role_name, society_id }
      409: { error: HIERARCHY_RULE_VIOLATED, rule_code }

/societies/{id}/batches/create:
  POST:
    summary: Create new batch in society
    auth: jwt [ROLE_SOCIETY_ADMIN | ROLE_FRANCHISE_OWNER]
    body: { skill_track_id, coach_id, coordinator_id, venue_address, fee_amount, fee_type, schedule_days, session_time, start_date, total_sessions }
    responses:
      201: { batch_id, commission_split_preview }
      409: { error: COACH_SCHEDULE_CONFLICT }
      422: { error: SOCIETY_NOT_READY, missing_requirements[] }

/batches/{id}/sessions/{session_id}/attendance:
  POST:
    summary: Mark attendance for a session
    auth: jwt [ROLE_COACH | ROLE_COORDINATOR]
    body: { attendance_records: [{ student_id, status }] }
    headers: { X-Offline-Mode: boolean }
    responses:
      200: { session_id, records_saved, offline_queued: boolean }

/societies/{id}/tournaments/create:
  POST:
    summary: Create tournament in society
    auth: jwt [ROLE_SOCIETY_ADMIN | ROLE_FRANCHISE_OWNER | ROLE_MASTER_ORGANIZER]
    body: { skill_track_id, tournament_type, venue_address, tournament_date, registration_fee?, prize_structure? }
    responses:
      201: { tournament_id, status: DRAFT }

/societies/{id}/commission/ledger:
  GET:
    summary: View commission ledger for society
    auth: jwt [ROLE_FRANCHISE_OWNER | ROLE_MASTER_ORGANIZER | ROLE_SUPER_ADMIN]
    responses:
      200: { entries[], total_gross, total_platform_cut, settlement_status }

/societies/{id}/payout/request:
  POST:
    summary: Request payout from society wallet
    auth: jwt [ROLE_FRANCHISE_OWNER | ROLE_COACH | ROLE_COORDINATOR]
    body: { amount, bank_account_ref, upi_id? }
    responses:
      200: { payout_id, temporal_workflow_id, expected_date }
      400: { error: BELOW_MINIMUM_THRESHOLD }
      409: { error: ACTIVE_DISPUTE_FREEZE }

/societies/{id}/schemes/eligible:
  GET:
    summary: Get eligible government schemes for society
    auth: jwt [ROLE_FRANCHISE_OWNER | ROLE_SOCIETY_ADMIN | ROLE_MASTER_ORGANIZER]
    responses:
      200: { eligible_schemes[], csr_matches[], total_potential_funding }

/societies/{id}/sync/status:
  GET:
    summary: Get offline sync status for society
    auth: jwt [ROLE_SOCIETY_ADMIN | ROLE_COORDINATOR]
    responses:
      200: { last_sync_at, pending_actions_count, conflict_count, sync_status }

/societies/{id}/sync/trigger:
  POST:
    summary: Manually trigger sync for society node
    auth: jwt [ROLE_SOCIETY_ADMIN | ROLE_COORDINATOR]
    responses:
      200: { sync_initiated: true, workflow_id }
```

---

## SECTION XIV — EVENT SCHEMA (SOCIETY MAPPING DOMAIN)

All events conform to R4 (AsyncAPI 2.6.0). These events are **mandatory**.

```yaml
channels:

  society.created:
    payload: { society_id, franchise_owner_id, society_type, timestamp }

  society.zone_mapped:
    payload: { society_id, zone_id, pin_code, district, conflict_status }

  society.member_joined:
    payload: { society_id, user_id, role_name, assigned_by, timestamp }

  society.ready:
    payload: { society_id, zone_id, sync_node_id, timestamp }

  society.onboarding_complete:
    payload: { society_id, user_id, role_name, track_name, timestamp }

  society.status_changed:
    payload: { society_id, old_status, new_status, changed_by, reason }

  batch.created:
    payload: { batch_id, society_id, skill_track_id, coach_id, start_date }

  batch.enrollment_opened:
    payload: { batch_id, society_id, capacity, fee_amount }

  batch.session_attendance_marked:
    payload: { session_id, batch_id, society_id, present_count, absent_count, offline_queued }

  tournament.created:
    payload: { tournament_id, society_id, skill_track_id, tournament_date }

  tournament.results_published:
    payload: { tournament_id, society_id, top_3: [{ participant_id, rank, score }] }

  commission.entry_created:
    payload: { ledger_id, society_id, source_type, gross_amount, hold_expires_at }

  payout.requested:
    payload: { payout_id, owner_id, society_id, amount, temporal_workflow_id }

  payout.completed:
    payload: { payout_id, owner_id, amount_disbursed, timestamp }

  payout.frozen:
    payload: { payout_id, dispute_id, frozen_at }

  offline_node.provisioned:
    payload: { society_id, sync_engine, device_count, timestamp }

  sync.completed:
    payload: { society_id, device_id, records_pushed, records_pulled, conflicts_detected }

  sync.conflict_detected:
    payload: { society_id, entity_type, entity_id, conflict_description }

  scheme.eligibility_detected:
    payload: { society_id, scheme_id, scheme_name, estimated_funding }

  csr.match_detected:
    payload: { society_id, csr_program_id, company_id, funding_per_student }

  zone.conflict_detected:
    payload: { society_id, conflicting_society_id, overlap_type }

  franchise.agreement_required:
    payload: { society_id, franchise_owner_id, deadline }

  franchise.agreement_signed:
    payload: { society_id, franchise_owner_id, signed_at, document_ref }
```

---

## SECTION XV — UI SCREENS (SOCIETY MAPPING & OPERATIONS)

All screens must comply with R29 (Flutter UI), R31 (dual frontend), R33 (design system), R59 (offline-first).

### 15.1 Screen Registry

```
SCREEN_SOCIETY_CREATION
  Flutter Route: /society/create
  Auth: ROLE_FRANCHISE_OWNER | ROLE_SUPER_ADMIN
  Purpose: Initial society record creation
  Inputs: Society name, type selector (visual cards), description
  Offline: NOT AVAILABLE (requires connectivity)
  Emit: society.created

SCREEN_GEOGRAPHIC_ZONE_MAPPER
  Flutter Route: /society/{id}/zone
  Auth: ROLE_FRANCHISE_OWNER | ROLE_SOCIETY_ADMIN
  Purpose: Map society to geographic zone
  UI Elements:
    - PIN code input field (primary, auto-populates district/state)
    - Optional: map picker with radius slider
    - Conflict warning banner if overlap detected
    - Zone type selector
  Offline: NOT AVAILABLE
  States: Default / Conflict Warning / Conflict Blocked / Success
  Emit: society.zone_mapped

SCREEN_HIERARCHY_BUILDER
  Flutter Route: /society/{id}/team
  Auth: ROLE_FRANCHISE_OWNER | ROLE_SOCIETY_ADMIN
  Purpose: Assign COACH and COORDINATOR roles
  UI Elements:
    - Visual org chart of current hierarchy
    - Invite by phone/email input
    - Role assignment dropdown
    - Hierarchy rule violation alerts
  Offline: Read-only (new assignments require connectivity)

SCREEN_BATCH_CREATOR
  Flutter Route: /society/{id}/batch/create
  Auth: ROLE_SOCIETY_ADMIN | ROLE_FRANCHISE_OWNER
  Purpose: Create new skill delivery batch
  UI Elements:
    - Skill track selector (searchable)
    - Coach selector (from society_members)
    - Coordinator selector
    - Venue address input
    - Schedule builder (day multi-select + time picker)
    - Fee type + amount input
    - Session count input + auto-computed end date
    - Commission split preview panel (read-only, computed live)
  Offline: NOT AVAILABLE
  Emit: batch.created

SCREEN_BATCH_DASHBOARD
  Flutter Route: /society/{id}/batch/{batch_id}
  Auth: ROLE_COACH | ROLE_COORDINATOR | ROLE_SOCIETY_ADMIN
  Purpose: Manage ongoing batch
  Panels:
    - Session list with status chips
    - Enrollment count vs capacity bar
    - Next session countdown timer
    - Quick attendance shortcut
    - Fee collection status ring chart
  Offline: FULL READ ACCESS + queue attendance marking

SCREEN_ATTENDANCE_MARKER
  Flutter Route: /batch/{id}/session/{session_id}/attendance
  Auth: ROLE_COACH | ROLE_COORDINATOR
  Purpose: Mark session attendance
  UI Elements:
    - Student roster with PRESENT / ABSENT / LATE toggles
    - Bulk mark all present option
    - Photo capture option (stored in MinIO, 90-day lifecycle)
    - Offline queue indicator badge
    - Submit & sync button
  Offline: FULLY AVAILABLE — queued for sync
  Constraint: Cannot modify after sync_confirmed = TRUE

SCREEN_COMMISSION_DASHBOARD
  Flutter Route: /society/{id}/commission
  Auth: ROLE_FRANCHISE_OWNER | ROLE_MASTER_ORGANIZER
  Purpose: View commission ledger and wallet balances
  UI Panels:
    - Wallet balance card (available + held split)
    - Pending payout requests
    - Ledger table (filterable by source_type, date)
    - Dispute flags highlighted in red
    - Settlement cycle countdown
  Offline: READ CACHED (last-synced data shown with timestamp)

SCREEN_PAYOUT_REQUEST
  Flutter Route: /society/{id}/payout/request
  Auth: ROLE_FRANCHISE_OWNER | ROLE_COACH | ROLE_COORDINATOR
  Purpose: Request commission payout
  UI Elements:
    - Available balance display
    - Amount input (min threshold enforced inline)
    - Bank account / UPI selector
    - 7-day hold explanation info card
    - Payout status tracker
  Offline: NOT AVAILABLE

SCREEN_TOURNAMENT_CREATOR
  Flutter Route: /society/{id}/tournament/create
  Auth: ROLE_SOCIETY_ADMIN | ROLE_FRANCHISE_OWNER | ROLE_MASTER_ORGANIZER
  Purpose: Create society-level tournament
  Offline: NOT AVAILABLE

SCREEN_TOURNAMENT_SCORE_ENTRY
  Flutter Route: /tournament/{id}/scores
  Auth: ROLE_COACH | ROLE_COORDINATOR
  Purpose: Enter participant scores during tournament
  Offline: FULLY AVAILABLE — queued for sync

SCREEN_GOVT_SCHEME_DASHBOARD
  Flutter Route: /society/{id}/schemes
  Auth: ROLE_FRANCHISE_OWNER | ROLE_SOCIETY_ADMIN | ROLE_MASTER_ORGANIZER
  Purpose: View eligible schemes, manage applications
  UI Panels:
    - Eligible schemes list with funding preview
    - Application status pipeline
    - Document upload per scheme
    - CSR match cards
  Offline: READ CACHED

SCREEN_OFFLINE_SYNC_STATUS
  Flutter Route: /society/{id}/sync
  Auth: ROLE_SOCIETY_ADMIN | ROLE_COORDINATOR
  Purpose: Monitor and trigger sync
  UI Elements:
    - Last sync timestamp
    - Pending actions count badge
    - Conflict list (with resolve action)
    - Manual sync trigger button
    - Sync engine type label (CouchDB / PostgreSQL)
  Offline: ALWAYS AVAILABLE (this is the offline management screen)

SCREEN_SOCIETY_ADMIN_CONSOLE
  Flutter Route: /society/{id}/admin
  Auth: ROLE_FRANCHISE_OWNER | ROLE_SOCIETY_ADMIN | ROLE_MASTER_ORGANIZER
  Purpose: Full society operations hub
  Panels:
    - Society status card
    - Active batches list
    - Member directory
    - Revenue summary
    - Upcoming tournaments
    - Scheme application status
    - Sync health indicator
```

---

## SECTION XVI — RISK & GOVERNANCE ENGINE

### 16.1 Society Risk Rules

```
RISK-SG-01: If society commission dispute count > 3 in 30 days → AUTO_FLAG for MASTER_ORGANIZER review
RISK-SG-02: If coach absence_rate > 20% across sessions in any batch → emit coach.absence_alert
RISK-SG-03: If enrollment drops below min_enrollment after batch confirmation → emit batch.at_risk_alert
RISK-SG-04: If sync.last_sync_at > 14 days AND society.status = ACTIVE → emit offline_node.stale_alert
RISK-SG-05: If payout_request submitted with unverified bank_account → HOLD + FLAG
RISK-SG-06: If tournament result disputed by > 30% of participants → AUTO_FREEZE results → MASTER_ORGANIZER review
RISK-SG-07: If scheme application has incomplete documentation for > 30 days → emit scheme.documentation_stale
RISK-SG-08: If FRANCHISE_OWNER account becomes SUSPENDED → society.status → SUSPENDED (Temporal 7-day workflow)
RISK-SG-09: If cash_fee_recorded > platform_threshold without coordinator confirmation → FLAG for FRANCHISE_OWNER audit
RISK-SG-10: If society_zone conflict = FULL and unresolved > 7 days → escalate to SUPER_ADMIN
```

### 16.2 Dispute Resolution Workflow (Temporal)

```
DISPUTE_RESOLUTION_WORKFLOW:

  TRIGGER: dispute.raised (commission, tournament result, zone conflict)
  ENGINE: Temporal Workflow Engine

  DAY 0:  Dispute received → freeze affected resource (commission / results / zone)
          → notify all parties via push + SMS
          → assign to MASTER_ORGANIZER for territory

  DAY 3:  MASTER_ORGANIZER must submit preliminary ruling
          → if not submitted → escalate to SUPER_ADMIN

  DAY 7:  Final ruling deadline
          → if ruling submitted → apply resolution → unfreeze resource
          → emit dispute.resolved
          → if not submitted → SUPER_ADMIN force-resolution

  APPEALS:
          → appeal must be filed within 3 days of ruling
          → appeal routes to SUPER_ADMIN final arbitration
          → SUPER_ADMIN ruling is FINAL — no further appeal
```

---

## SECTION XVII — SECURITY RULES (SOCIETY DOMAIN)

All rules are **non-optional**.

```
SEC-SM-01: RLS enforced on society_id — no cross-society reads permitted at DB layer
SEC-SM-02: RLS enforced on tenant_id — strict multi-tenant isolation
SEC-SM-03: Commission ledger entries are immutable once written (hash-locked)
SEC-SM-04: Payout requests require bank_account_ref verified by FRANCHISE_OWNER before processing
SEC-SM-05: Franchise agreement documents stored in MinIO audit bucket — 3-year retention, immutable
SEC-SM-06: Offline queue entries are signed with device_id + timestamp to prevent tampering
SEC-SM-07: Tournament results locked after COACH confirmation — override requires MASTER_ORGANIZER audit log
SEC-SM-08: Government scheme documents encrypted at rest in MinIO
SEC-SM-09: Sync conflict logs are retained for 1 year minimum
SEC-SM-10: Commission split rules are read from server-side config only — no client-side override permitted
SEC-SM-11: FRANCHISE_OWNER bank account changes require re-verification (OTP + MASTER_ORGANIZER notification)
SEC-SM-12: All admin console actions (R40) on society entities write to immutable audit log
```

---

## SECTION XVIII — INTEGRATION MAP

The SOCIETY_MAPPING_AGENT integrates with the following platform services. All integrations are **mandatory**.

```
→ ROLE_ASSIGNMENT_AGENT          : Prerequisite — must complete before this agent activates
→ Auth Service (Keycloak)         : Enforces RBAC for society-scoped roles
→ Tenant Service                  : Binds society to its multi-tenant context
→ Notification Service            : Sends onboarding, sync alerts, payout, dispute notifications
→ Billing & Subscription Service  : Platform commission collection, GST computation (R13)
→ Digital Signature Service       : Franchise agreement signing (R15)
→ Legal Document Generation (R15) : Auto-generate franchise agreement, scheme documents
→ Analytics Service (ClickHouse)  : Receives all society events for performance dashboards
→ Temporal Workflow Engine        : Payout holds, dispute resolution, franchise termination
→ Apache CouchDB                  : Offline-first sync for rural/zero-connectivity zones
→ MinIO Object Storage            : Session photos (90-day), audit docs (3-year)
→ Admin Ops Console (R40)         : Surfaces society flags, disputes, stale sync alerts
→ Government Scheme Registry      : Scheme eligibility scan and application management
→ CSR Program Registry            : CSR match detection and linkage
→ Skill Registry                  : Batch skill track validation
→ Scoring Engine                  : Tournament scoring schema binding
→ Certification Engine (R90)      : Post-batch certification issuance
→ Event Bus (Kafka)               : All society events published here
→ Qdrant Vector DB                : Skill portfolio embeddings for talent recommendations
→ Metabase (BI)                   : Society revenue dashboards, franchise performance reports
→ Wazuh (SIEM)                    : Intrusion detection for society admin console actions
→ OpenTelemetry                   : Distributed tracing for all society service calls
```

---

## SECTION XIX — ANTIGRAVITY BEHAVIORAL CONTRACT

### 19.1 What ANTIGRAVITY Means in the Society Context

Society operators — FRANCHISE_OWNERs, COACHes, COORDINATORs — are **not tech-savvy enterprise users**. They are local entrepreneurs, retired teachers, community organizers, and motivated youth in small towns and villages. Many will use this platform on entry-level Android phones, in areas with 2G or zero connectivity.

ANTIGRAVITY in the society context means: **the platform must feel lighter than doing nothing**. It must reduce the operational burden of running a local skill center, not add to it. It must work when the internet doesn't. It must explain itself in plain language. It must never strand a COORDINATOR who just collected ₹500 in cash and needs to log it.

### 19.2 ANTIGRAVITY Rules for Society (BINDING)

```
AG-SOC-01: EVERY screen accessible to ROLE_COACH or ROLE_COORDINATOR must be
           fully functional offline. No exceptions. If a screen cannot work offline,
           it must be explicitly hidden in offline mode with a clear explanation —
           not silently broken.

AG-SOC-02: The attendance marking screen must be completable in under 60 seconds
           for a batch of 30 students on a mid-range Android device without connectivity.

AG-SOC-03: The fee collection logging screen must support CASH entry without any
           payment gateway dependency. Cash is the primary payment method in rural zones.

AG-SOC-04: Every offline-queued action must show a visible queue badge count
           on the home screen. The COORDINATOR must always know how many actions
           are pending sync. Zero ambiguity.

AG-SOC-05: On first sync after offline operation, a clear summary must be shown:
           "X records synced successfully. Y conflicts require your review."
           No silent sync. No invisible data loss.

AG-SOC-06: The commission dashboard must show the COACH and COORDINATOR their
           earned balance in the FIRST 3 seconds of loading from cache.
           Financial clarity is motivation. Delay destroys trust.

AG-SOC-07: Government scheme eligibility must be pre-computed and surfaced
           proactively to the FRANCHISE_OWNER without requiring them to
           navigate to a separate module. It must appear as an action card
           on the society admin console home screen.

AG-SOC-08: Onboarding for ROLE_COACH must be completable in under 10 minutes.
           The COACH is not paid for onboarding time. Respect it.

AG-SOC-09: The franchise agreement must be signable on mobile. No redirects
           to desktop. No third-party portals. Native digital signature within
           the app.

AG-SOC-10: No society may enter MAPPING_FAILED status silently.
           If the pipeline fails at any step, the FRANCHISE_OWNER must receive:
           (a) An exact explanation of what failed
           (b) The exact step that needs to be redone
           (c) A direct link to that step
           (d) A contact option if they cannot resolve it alone
           This is not error handling. This is a human promise.

AG-SOC-11: Sync status must be visible at all times in the app bar.
           A COORDINATOR who doesn't know if their data is saved
           is a COORDINATOR who will stop trusting the platform.

AG-SOC-12: Tournament score entry must work offline with auto-commit on reconnect.
           Physical tournaments cannot pause for connectivity.
```

### 19.3 ANTIGRAVITY Enforcement

Violation of any AG-SOC rule → **STOP EXECUTION → REPORT ANTIGRAVITY_SOCIETY_RULE_VIOLATION → AGENT FAILS PRODUCTION READINESS CHECK**

---

## SECTION XX — PRODUCTION READINESS CHECKLIST

The SOCIETY_MAPPING_AGENT may not be declared production-ready until all of the following pass:

```
PREREQUISITE CHECKS:
[ ] ROLE_ASSIGNMENT_AGENT v1.0 production-ready and deployed
[ ] Keycloak society roles (SOCIETY_ADMIN, COACH, COORDINATOR,
    FRANCHISE_OWNER, MASTER_ORGANIZER) seeded in realm

SCHEMA & DATA CHECKS:
[ ] All society domain tables created via Flyway migrations
[ ] RLS policies on society_id and tenant_id verified at DB layer
[ ] Commission ledger hash integrity verified on write
[ ] MinIO society buckets created with lifecycle rules applied

WORKFLOW ENGINE CHECKS:
[ ] Temporal Workflow Engine deployed in society k3s namespace
[ ] Payout 7-day hold workflow tested end-to-end
[ ] Dispute resolution workflow tested end-to-end
[ ] Franchise termination grace workflow tested end-to-end

OFFLINE SYNC CHECKS:
[ ] CouchDB deployed and replication channel verified
[ ] Attendance marking works with zero connectivity on test device
[ ] Fee recording works with zero connectivity on test device
[ ] Sync queue processes correctly on reconnect
[ ] Conflict resolution dialog appears when conflicts detected
[ ] Sync status badge visible in all relevant screens

API CHECKS:
[ ] All API endpoints return correct HTTP codes
[ ] Commission split calculation verified against RULE-CF-01 through CF-10
[ ] Zone conflict detection tested for PARTIAL / FULL / BOUNDARY cases
[ ] Hierarchy rule validation tested for RULE-SH-01 through SH-10

SECURITY CHECKS:
[ ] RLS cross-society read attempt blocked and logged
[ ] Commission ledger immutability tested (no update/delete permitted)
[ ] Franchise document MinIO retention policy confirmed 3-year lock
[ ] FRANCHISE_OWNER bank account change re-verification tested

INTEGRATION CHECKS:
[ ] Temporal payout workflow emits correct events
[ ] Government scheme eligibility scan runs on society.zone_mapped
[ ] CSR match detection runs on zone mapping
[ ] Analytics events arriving in ClickHouse
[ ] Notification service sending onboarding + payout + dispute alerts

UI/UX CHECKS:
[ ] Attendance marker completes in < 60 seconds for 30 students (AG-SOC-02)
[ ] Cash fee entry works without payment gateway (AG-SOC-03)
[ ] Queue badge count visible on home screen (AG-SOC-04)
[ ] Post-sync summary displayed (AG-SOC-05)
[ ] Commission balance loads from cache in < 3 seconds (AG-SOC-06)
[ ] Scheme eligibility card appears on admin console home (AG-SOC-07)
[ ] Franchise agreement signable on mobile (AG-SOC-09)
[ ] Failure screen shows exact step + resolution path (AG-SOC-10)
[ ] Sync status indicator in app bar always visible (AG-SOC-11)
[ ] Tournament score entry works offline (AG-SOC-12)

GOVERNANCE CHECKS:
[ ] Risk rules RISK-SG-01 through RISK-SG-10 implemented and tested
[ ] All society audit logs writing to immutable audit table
[ ] Admin Ops Console (R40) shows society flags correctly
[ ] Contract Validator (R49) passes for all society agent contracts
[ ] QA Generator (R50) tests pass at 100% coverage for society domain
```

Absence of any check → **STOP EXECUTION → NO DEPLOYMENT CLAIM PERMITTED**

---

## SECTION XXI — FINAL SEAL

```
SOCIETY_MAPPING_AGENT
Version: v1.0
Bound System: ECOSKILLER v12.0
Prerequisite Agent: ROLE_ASSIGNMENT_AGENT v1.0
Onboarding Philosophy: ANTIGRAVITY
Infrastructure: 100% open-source · self-hosted · k3s · offline-first

This agent is the operational foundation of ECOSKILLER's
offline, semi-urban, and rural reach strategy.
It maps people, places, structures, money, and rules
into a single deterministic registry
that functions whether or not the internet does.

It does not assume connectivity.
It does not assume tech literacy.
It does not assume formal infrastructure.

It assumes only one thing:
that a COACH in a small town with a group of students
and a mobile phone deserves the same platform reliability
as a recruiter in a Mumbai office tower.

Status: LOCKED · SEALED · PRODUCTION-GOVERNED

Mutation Policy: Add-only via version bump
Interpretation Authority: NONE
Execution Authority: Human declaration only

"The society is the last mile.
 This agent makes the last mile first-class."
```

---

*SOCIETY_MAPPING_AGENT.md — ECOSKILLER ANTIGRAVITY SYSTEM — v1.0 — FINAL*
