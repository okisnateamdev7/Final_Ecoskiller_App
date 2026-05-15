# TOURNAMENT_MANAGEMENT_AGENT
## Ecoskiller · Dojo SaaS · Program & Event Management Layer
### Status: FINAL · LOCKED · SEALED · ANTIGRAVITY-ENFORCED
### Artifact Class: Production Agent Prompt — Deterministic Execution
### Mutation Policy: Add-only via version bump · Interpretation Authority: NONE
### Execution Engine: ANTIGRAVITY
### Version: 1.0 · March 2026 · Internal Use Only

---

## ⚠️ ANTIGRAVITY OPERATOR SEAL

```
AGENT_NAME              = TOURNAMENT_MANAGEMENT_AGENT
EXECUTION_ENGINE        = ANTIGRAVITY
PLATFORM                = ECOSKILLER · DOJO SAAS
DOMAIN                  = Program & Event Management
STACK_LOCK              = Flutter (operational) + React/Next.js (SEO)
MUTATION_POLICY         = ADD-ONLY VIA VERSION BUMP
INTERPRETATION          = FORBIDDEN
ARCHITECTURE_AUTHORITY  = LOCKED — HUMAN DECLARATION ONLY
ANTIGRAVITY_CONFUSION   = IMPOSSIBLE
READY_FOR               = ANTIGRAVITY_PRODUCTION
ANTIGRAVITY_SAFE        = TRUE
```

> Antigravity MUST NOT reinterpret tournament architecture, match flow, pairing logic, billing entitlements, or scoring rules.
> Antigravity MUST generate UI strictly within declared module boundaries.
> Antigravity MUST NOT add, remove, or restructure phases, rounds, or states beyond what is declared below.
> Antigravity MUST treat every STOP signal as a hard execution halt with no partial output.

---

## SECTION 1 — AGENT IDENTITY & SCOPE

### 1.1 Agent Purpose

The Tournament Management Agent governs the complete lifecycle of competitive events within the Ecoskiller Dojo SaaS platform. It is the authoritative orchestrator for:

- Tournament creation, configuration, and scheduling
- Participant registration, seeding, and eligibility enforcement
- Bracket generation and round-by-round progression
- Match orchestration in coordination with the Dojo Match Engine
- Real-time leaderboard computation and publication
- Prize, entry fee, and reward processing via the Billing Engine
- Audit trail generation and governance enforcement
- Post-tournament analytics and belt eligibility triggers

### 1.2 Blast Domain Classification

This agent operates exclusively within:

```
BLAST DOMAIN: B — DOJO SAAS (LOGICALLY DEPARTED)
  Matches · Scenarios · Scoring
  Belts · Certifications
  Replays · Mentor Systems
  Tournaments & Analytics
```

Failure in the Tournament domain MUST NOT cascade into:
- Blast Domain A (Ecoskiller Core — Jobs, Skills, Marketplace)
- Blast Domain C (Shared Trust & Identity Layer)

### 1.3 Agent Position in System Architecture

```
Ecoskiller Platform
  └── Dojo SaaS Namespace: realtime
        └── TOURNAMENT_MANAGEMENT_AGENT
              ├── Dojo Match Engine (consumer)
              ├── Scoring Engine (consumer)
              ├── Belt Engine (trigger)
              ├── Analytics Engine (publisher)
              ├── Billing & Subscription Service (consumer)
              ├── Notification Service (publisher)
              └── Leaderboard Service (publisher)
```

---

## SECTION 2 — SYSTEM IDENTITY LOCK

### 2.1 Naming Conventions (Immutable)

Primary entities cannot be renamed. Fields may extend — not mutate.

```
Tournament         — top-level competitive event container
TournamentRound    — a single elimination/pool stage
TournamentBracket  — the full bracket tree for a Tournament
Match              — a single live skill duel within a round
Participant        — a registered, eligible entrant
Seed               — computed ranking position at bracket generation
Score              — immutable numeric output of a completed Match
Leaderboard        — live ranked standings within a Tournament
Prize              — reward assigned to finishing position
EntryFee           — billing charge attached to Tournament registration
AuditLog           — immutable record of every state transition
```

### 2.2 Tournament Types (Declared — Non-Extendable Without Version Bump)

```
SINGLE_ELIMINATION   — one loss = exit
DOUBLE_ELIMINATION   — two losses = exit
ROUND_ROBIN          — all vs all within pool
SWISS                — paired by current standing, no elimination
LEAGUE               — multi-week scheduled fixtures
SKILL_SPRINT         — time-boxed solo performance tournament
```

### 2.3 Tournament Modes

```
RANKED         — affects participant Elo/Rating
UNRANKED       — practice; no rating change
CERTIFICATION  — belt eligibility trigger on completion
QUALIFIER      — feeds entrants into a parent Tournament
```

---

## SECTION 3 — TOURNAMENT LIFECYCLE STATE MACHINE

### 3.1 States (Immutable — State Machine is Deterministic)

```
DRAFT
  → PUBLISHED
      → REGISTRATION_OPEN
          → REGISTRATION_CLOSED
              → SEEDING
                  → BRACKET_GENERATED
                      → ROUND_ACTIVE
                          → ROUND_COMPLETE
                              → [loop ROUND_ACTIVE if rounds remain]
                      → TOURNAMENT_COMPLETE
                          → RESULTS_PUBLISHED
                              → ARCHIVED
```

### 3.2 State Transition Rules

| From State            | To State              | Trigger                                            | Authority         |
|-----------------------|-----------------------|----------------------------------------------------|-------------------|
| DRAFT                 | PUBLISHED             | Admin publish action                               | TOURNAMENT_ADMIN  |
| PUBLISHED             | REGISTRATION_OPEN     | Schedule datetime reached                          | SYSTEM            |
| REGISTRATION_OPEN     | REGISTRATION_CLOSED   | Max participants reached OR schedule closed        | SYSTEM            |
| REGISTRATION_CLOSED   | SEEDING               | Admin confirms participant list                    | TOURNAMENT_ADMIN  |
| SEEDING               | BRACKET_GENERATED     | Seeding algorithm completes                        | SYSTEM            |
| BRACKET_GENERATED     | ROUND_ACTIVE          | Admin starts round OR scheduled round_start_time   | SYSTEM/ADMIN      |
| ROUND_ACTIVE          | ROUND_COMPLETE        | All matches in round reach MATCH_COMPLETE          | SYSTEM            |
| ROUND_COMPLETE        | ROUND_ACTIVE          | Next round exists AND conditions pass              | SYSTEM            |
| ROUND_COMPLETE        | TOURNAMENT_COMPLETE   | Final round complete                               | SYSTEM            |
| TOURNAMENT_COMPLETE   | RESULTS_PUBLISHED     | Prize disbursement verified, audit complete        | TOURNAMENT_ADMIN  |
| RESULTS_PUBLISHED     | ARCHIVED              | Retention period elapsed                           | SYSTEM            |

No state may be skipped.
No state may be reversed without an OVERRIDE audit entry.
OVERRIDE requires PLATFORM_SUPER_ADMIN authority + written justification captured in AuditLog.

---

## SECTION 4 — PARTICIPANT REGISTRATION ENGINE

### 4.1 Registration Rules (Non-Negotiable)

```
ELIGIBILITY_CHECK_ORDER:
  1. Identity verified (Keycloak auth confirmed)
  2. Skill belt level meets tournament minimum_belt_requirement
  3. Account not suspended or under dispute review
  4. Entry fee paid (if tournament.entry_fee > 0)
  5. Participant count < tournament.max_participants
  6. Registration window is OPEN (state = REGISTRATION_OPEN)
  7. Duplicate registration check passed
```

Failure at any eligibility step → registration DENIED with reason code.
No manual override of eligibility steps 1, 3, or 4.
Steps 2, 5, 6 may be overridden by TOURNAMENT_ADMIN with AuditLog entry.

### 4.2 Registration Data Captured

```
participant_id         — FK to User
tournament_id          — FK to Tournament
registered_at          — UTC timestamp
entry_fee_paid         — boolean
payment_reference      — FK to Billing transaction (nullable if free)
belt_at_registration   — snapshot of participant belt level at time of registration
seed_position          — null until SEEDING state
status                 — REGISTERED | WITHDRAWN | DISQUALIFIED | CHECKED_IN
checked_in_at          — nullable UTC timestamp
```

### 4.3 Withdrawal Rules

```
WITHDRAWAL_WINDOW      — open until SEEDING state begins
LATE_WITHDRAWAL        — allowed until ROUND_ACTIVE with penalty flag
DISQUALIFICATION       — triggered by: no-show, behavioral flag, fraud detection
```

Entry fee refund on withdrawal follows Billing Service refund policy.
Entry fee is NON-REFUNDABLE after BRACKET_GENERATED state.

### 4.4 Check-In Enforcement

```
CHECK_IN_WINDOW        — opens 30 minutes before ROUND_ACTIVE
CHECK_IN_DEADLINE      — 10 minutes before ROUND_ACTIVE
NO_SHOW_ACTION         — participant status → DISQUALIFIED; bracket adjusted
```

Late joiners after CHECK_IN_DEADLINE → spectator only. No exceptions.

---

## SECTION 5 — SEEDING ENGINE

### 5.1 Seeding Algorithm (Deterministic)

```
PRIMARY_SORT    → participant Elo/Rating (descending)
TIEBREAK_1      → belt level (descending)
TIEBREAK_2      → total ranked matches played (descending)
TIEBREAK_3      → registration timestamp (ascending — earlier = better seed)
```

Seeding is computed once. Seeding cannot be re-run after BRACKET_GENERATED.
Manual seed override requires TOURNAMENT_ADMIN authority + AuditLog entry.

### 5.2 Bye Assignment

```
BYE_RULE       → assigned to lowest seeds first (fill bracket to power-of-2)
BYE_LIMIT      → maximum 1 bye per participant per tournament
BYE_MATCH      → recorded as WALKOVER with opponent_id = NULL
```

---

## SECTION 6 — BRACKET ENGINE

### 6.1 Bracket Generation Rules

```
INPUT           → seeded participant list
OUTPUT          → deterministic bracket tree
ALGORITHM       → standard single or double elimination pairing
                  (1 vs N, 2 vs N-1, etc.)
ROUND_ROBIN     → round-robin fixture matrix (all pairs, each plays once)
SWISS           → Swiss-system pairing by current standing per round
```

Bracket generation is idempotent — same seeds always produce same bracket.
Bracket structure is immutable after BRACKET_GENERATED state.

### 6.2 Bracket Data Model

```
TournamentBracket
  └── rounds[]
        └── TournamentRound
              ├── round_number
              ├── round_type (WINNER | LOSER | FINAL | POOL)
              ├── scheduled_start
              ├── state (PENDING | ACTIVE | COMPLETE)
              └── matches[]
                    └── Match
                          ├── match_id (FK to Dojo Match Engine)
                          ├── participant_a_id
                          ├── participant_b_id (nullable = BYE)
                          ├── winner_id (null until MATCH_COMPLETE)
                          ├── score_a
                          ├── score_b
                          ├── scenario_id
                          └── match_state
```

---

## SECTION 7 — MATCH ORCHESTRATION CONTRACT

### 7.1 Match Engine Interface (Locked)

The Tournament Management Agent does NOT own match execution.
It issues commands to and receives events from the Dojo Match Engine.

```
COMMANDS ISSUED BY TOURNAMENT_AGENT → MATCH ENGINE:
  CREATE_MATCH(tournament_id, round_id, participant_a, participant_b, scenario_id)
  START_MATCH(match_id)
  CANCEL_MATCH(match_id, reason)
  EXTEND_MATCH(match_id, duration_seconds)   [TOURNAMENT_ADMIN only]

EVENTS RECEIVED FROM MATCH ENGINE → TOURNAMENT_AGENT:
  MATCH_STARTED(match_id)
  MATCH_COMPLETED(match_id, winner_id, score_a, score_b)
  MATCH_ABANDONED(match_id, reason)
  MATCH_DISPUTED(match_id)
```

### 7.2 Match Failure Handling

| Event                 | Tournament Agent Action                                         |
|-----------------------|-----------------------------------------------------------------|
| MATCH_ABANDONED       | Mark match as VOID; trigger re-match if round allows; else BYE |
| MATCH_DISPUTED        | Freeze round progression; escalate to Admin dispute queue       |
| No-show (participant) | WALKOVER awarded to present participant; DQ logged              |
| Technical failure     | Retry once; if second failure → WALKOVER + incident log         |

No match result is accepted without a MATCH_COMPLETED event from the Match Engine.
The Tournament Agent cannot self-assign winners.

### 7.3 Scenario Assignment

```
ASSIGNMENT_RULE     → scenario randomly selected from tournament.allowed_scenarios[]
REPEAT_RULE         → same scenario cannot repeat for same participant pair within tournament
DIFFICULTY_RULE     → scenario difficulty must be within tournament.difficulty_band
FAIRNESS_RULE       → scenario order randomized per round
```

Scenario assignment is logged and immutable per match.

---

## SECTION 8 — SCORING & LEADERBOARD ENGINE

### 8.1 Score Ingestion (Locked)

```
SOURCE              → Scoring Engine (only) via MATCH_COMPLETED event
FORMAT              → numeric, weighted, audit-stamped
OVERRIDE_POLICY     → only TOURNAMENT_ADMIN can flag for review
                      PLATFORM_SUPER_ADMIN required for actual score mutation
IMMUTABILITY        → scores written once; corrections create new audit record
```

### 8.2 Leaderboard Computation Rules

```
TOURNAMENT_COMPLETE / ROUND_ROBIN / SWISS:
  Points:
    WIN     = 3 points
    DRAW    = 1 point (if applicable)
    LOSS    = 0 points
    BYE     = 1 point
    WALKOVER_WIN = 3 points
    WALKOVER_LOSS = 0 points + DQ flag

  Tiebreak order:
    1. Head-to-head result (if applicable)
    2. Average score margin
    3. Opponent cumulative score (strength of schedule)
    4. Seed position (lower seed number = better tiebreak)
```

### 8.3 Leaderboard Publication

```
PUBLICATION_TRIGGER   → after each ROUND_COMPLETE event
VISIBILITY            → PARTICIPANTS: full standings
                        PUBLIC (React SEO layer): read-only, no PII
CACHE_LAYER           → Redis (TTL = 60 seconds during active round)
PERSISTENCE           → PostgreSQL (immutable snapshot per round)
```

---

## SECTION 9 — PRIZE, BILLING & ENTRY FEE ENGINE

### 9.1 Entry Fee Processing

```
PAYMENT_GATE         → entry fee collected at registration
PROVIDER             → Billing & Subscription Service (abstracted Stripe layer)
FAILURE_ACTION       → registration denied; no provisional entry
REFUND_WINDOW        → before BRACKET_GENERATED state only
GST_VAT              → enforced per Billing Service regional rules
INVOICE              → auto-generated on payment confirmation
```

### 9.2 Prize Pool Accounting

```
PRIZE_POOL_SOURCES:
  entry_fee_pool     — sum of all entry fees collected
  platform_addition  — optional platform-funded prize addition
  sponsor_addition   — optional sponsor-funded prize addition

PRIZE_DISTRIBUTION:
  Positions and percentages declared at DRAFT state
  Distribution locked at REGISTRATION_CLOSED state
  No prize structure change after REGISTRATION_CLOSED

DISBURSEMENT_TRIGGER  → RESULTS_PUBLISHED state
DISBURSEMENT_METHOD   → Royalty Wallet Service credit OR bank payout
DISBURSEMENT_AUDIT    → double-entry ledger in Royalty Accounting Engine
```

### 9.3 Fraud & Abuse Controls

```
DETECT:
  fake tournament loops (repeated entry farming)
  multi-account entry (same user, multiple accounts)
  collusion ring detection (suspicious win/loss patterns)
  refund abuse patterns

ACTION ON FLAG:
  freeze prize disbursement
  escalate to Admin Governance Service
  no auto-resolution
```

---

## SECTION 10 — FAIRNESS ENGINE

### 10.1 Match Fairness Controls (Mandatory)

```
OPPONENT_BAND_LIMIT       → max Elo/Rating gap per round (configurable per tournament)
REPEAT_OPPONENT_AVOIDANCE → same pair cannot face each other twice in same round
ROLE_ROTATION_FAIRNESS    → attacker/defender roles rotated per match
FIRST_SPEAKER_ROTATION    → not applicable (dojo skill duels, not GD)
SCENARIO_RANDOMIZATION    → seeded random per match, logged
CONSTRAINT_BALANCE        → scenario constraints distributed evenly across participants
```

### 10.2 Collusion Detection

```
SIGNALS_MONITORED:
  reciprocal high scoring between same pair across multiple tournaments
  abnormal peer score clustering
  match farming patterns (high win rate vs specific opponents)
  mentor favoritism patterns

ACTION:
  flag match for audit review
  flagged matches DO NOT count toward belt eligibility until cleared
```

### 10.3 Tournament Fairness Audit

```
MANDATORY AFTER: every TOURNAMENT_COMPLETE
AUDIT CHECKS:
  seeding integrity
  bracket balance verification
  scenario distribution fairness
  score variance anomaly check
  prize pool disbursement accuracy
AUDIT STORED: immutable AuditLog, linked to tournament_id
```

---

## SECTION 11 — REALTIME PROTOCOL LOCK

### 11.1 Transport Separation (Non-Negotiable)

```
Video (match rooms)  → WebRTC via LiveKit SFU (Dojo Match Engine owns)
Game Events          → WebSocket command channel
Tournament Commands  → Mentor/Admin WebSocket
Analytics Events     → Kafka event bus
Leaderboard updates  → WebSocket push to Flutter client
```

No channel mixing permitted.
Tournament Management Agent NEVER touches WebRTC or media streams.

### 11.2 WebSocket Event Contracts (Tournament Layer)

```
EVENTS PUBLISHED BY TOURNAMENT_AGENT → CLIENT (Flutter):
  tournament.registration.confirmed
  tournament.registration.denied   { reason_code }
  tournament.bracket.published     { bracket_url }
  tournament.round.started         { round_number, match_assignments[] }
  tournament.match.assigned        { match_id, opponent_id, room_token }
  tournament.round.completed       { standings_snapshot }
  tournament.results.published     { final_standings, prizes[] }
  tournament.leaderboard.updated   { leaderboard_snapshot }
  tournament.disqualified          { participant_id, reason }

EVENTS RECEIVED BY TOURNAMENT_AGENT ← ADMIN SOCKET:
  tournament.admin.start_round     { round_id }
  tournament.admin.force_walkover  { match_id, winner_id, reason }
  tournament.admin.pause           { tournament_id, reason }
  tournament.admin.resume          { tournament_id }
  tournament.admin.disqualify      { participant_id, reason }
```

---

## SECTION 12 — BELT & CERTIFICATION TRIGGER CONTRACT

### 12.1 Tournament → Belt Engine Event

```
EVENT: tournament.completed → Belt Engine
PAYLOAD:
  participant_id
  tournament_id
  tournament_mode         (must be CERTIFICATION or RANKED)
  final_position          (integer rank)
  match_count             (total matches played)
  average_score           (numeric)
  scenario_difficulty_avg (numeric)
  pressure_scenario_passed (boolean)
```

### 12.2 Belt Promotion Rules (Belt Engine governs — not this agent)

```
This agent SUPPLIES data.
Belt Engine DECIDES promotion.
Auto-promotion FORBIDDEN.
Mentor confirmation REQUIRED before belt award.
AI cannot award belts directly.
```

---

## SECTION 13 — SECURITY CONTROLS

### 13.1 Auth & Access (Mandatory)

```
ALL TOURNAMENT ADMIN ACTIONS → RBAC enforced via Keycloak
  TOURNAMENT_ADMIN    → create, publish, manage tournament
  MASTER_ORGANIZER    → cross-tenant tournament governance
  PLATFORM_SUPER_ADMIN → override, audit, force-state transitions
  PARTICIPANT         → register, withdraw, view own match info
  PUBLIC              → read-only leaderboard (React layer, no auth)

JWT short-lived access tokens on all API calls
Room tokens signed and time-limited (Match Engine issues, not this agent)
```

### 13.2 Data Security

```
Row-level security enforced on tournament_id and tenant_id
Cross-tenant tournament data access FORBIDDEN
PII encrypted at rest (participant profiles)
Prize and billing records encrypted
```

### 13.3 Audit Immutability

```
EVERY state transition          → AuditLog entry (immutable)
EVERY admin override            → AuditLog entry with actor + reason
EVERY prize disbursement        → double-entry ledger record
EVERY disqualification          → AuditLog + Participant record update
EVERY score override            → AuditLog with original + new value
```

---

## SECTION 14 — BEHAVIORAL SAFETY LOCK

### 14.1 In-Tournament Safety Controls

```
Harassment reporting            → available during active match (Flutter UI)
Intimidation signal detection   → behavioral flag on repeated aggression reports
Forced match exit option        → participant can exit; DQ applied; safety logged
Mentor emergency intervention   → Mentor Socket command: EMERGENCY_PAUSE
Cooldown enforcement            → flagged participant cooling period before re-entry
Abuse flag tagging              → attached to participant record; visible to admins
```

Safety overrides scoring.
A safety-flagged match is FROZEN until Admin Governance reviews.
No score from a safety-flagged match counts until review is complete.

---

## SECTION 15 — UI GENERATION RULES (ANTIGRAVITY)

### 15.1 Flutter App — Tournament UI Responsibilities

Antigravity MUST generate the following screens. No extras. No omissions.

```
TOURNAMENT_BROWSE_SCREEN
  → list of open, upcoming, completed tournaments
  → filter by: skill, mode, type, entry_fee, belt_requirement
  → card: tournament name, skill, type, seats remaining, prize pool

TOURNAMENT_DETAIL_SCREEN
  → tournament metadata
  → registration button (state-gated: visible only if REGISTRATION_OPEN)
  → bracket preview (locked until BRACKET_GENERATED)
  → rules and scenario types
  → prize structure
  → countdown timer to registration close / round start

REGISTRATION_FLOW_SCREEN
  → eligibility check display (real-time, per 4.1 rules)
  → entry fee payment integration (Billing Service widget)
  → confirmation receipt

MY_TOURNAMENTS_SCREEN
  → registered tournaments (status, next match, current position)
  → completed tournaments (final position, prize won)

BRACKET_VIEWER_SCREEN
  → interactive bracket tree
  → match status per cell (PENDING / LIVE / COMPLETE)
  → winner highlighted
  → tap on match → match detail

MATCH_ASSIGNMENT_SCREEN
  → opponent name, belt, rating
  → scenario name (revealed at match start)
  → room join button (active only in match window)
  → countdown to match start

LEADERBOARD_SCREEN
  → live ranked standings (refreshed per WebSocket push)
  → own position highlighted
  → per-round standings toggle

RESULTS_SCREEN
  → final standings
  → prize claim status
  → belt eligibility notification (if triggered)
  → replay links (per Replay Engine)

TOURNAMENT_ADMIN_PANEL (admin role only)
  → tournament lifecycle controls (state transitions)
  → round start button
  → disqualify participant
  → force walkover
  → pause / resume
  → audit log viewer
```

### 15.2 React / Next.js SEO Layer — Tournament Pages

```
/tournaments                    → public tournament index (SSG)
/tournaments/[slug]             → public tournament detail (SSR)
/tournaments/[slug]/leaderboard → public read-only leaderboard (SSR, no PII)
/tournaments/[slug]/results     → public final results after RESULTS_PUBLISHED
```

React layer MUST NOT host:
- Registration flow
- Match room
- Admin panel
- Any authenticated action

### 15.3 Antigravity UI Limits

```
✅ ALLOWED:
  Generate screens listed in 15.1 and 15.2
  Apply Ecoskiller design system (Material 3, Flutter)
  Implement WebSocket listeners for live leaderboard
  Implement state-gated button visibility

🚫 FORBIDDEN:
  Adding screens not declared in 15.1 / 15.2
  Implementing scoring logic in UI layer
  Mixing Flutter and React responsibilities
  Accessing or displaying raw audit logs to non-admin roles
  Implementing bracket generation logic client-side
  Adding prize disbursement UI not connected to Billing Service events
```

---

## SECTION 16 — OBSERVABILITY REQUIREMENTS

### 16.1 Metrics (Prometheus)

```
tournament_registrations_total        (by tournament_id, status)
tournament_match_start_failures_total (by tournament_id, round)
tournament_round_completion_time_ms   (histogram)
tournament_no_show_rate               (gauge, by tournament_id)
tournament_prize_disbursement_errors  (counter)
tournament_leaderboard_update_lag_ms  (histogram)
tournament_fraud_flags_total          (counter)
```

### 16.2 Dashboards (Grafana)

```
Tournament Health Dashboard:
  → Active tournaments by state
  → Match failure rate per round
  → No-show rate by tournament type
  → Leaderboard update lag
  → Prize disbursement success rate
  → Fraud flags triggered (last 24h)
```

### 16.3 Alerting

```
tournament.match_start_failure   → page on-call
tournament.round_stuck_30min     → alert (round has not progressed in 30 min)
tournament.prize_disbursement_failed → alert immediately
tournament.fraud_cluster_detected → alert + auto-freeze
```

---

## SECTION 17 — ASYNC EVENT CONTRACTS (KAFKA)

### 17.1 Events Published by Tournament Agent

```
Topic: tournament.events

Events:
  tournament.created           { tournament_id, config_snapshot }
  tournament.published         { tournament_id }
  tournament.registration.opened { tournament_id }
  tournament.registration.closed { tournament_id, participant_count }
  tournament.bracket.generated { tournament_id, bracket_snapshot }
  tournament.round.started     { tournament_id, round_id, match_ids[] }
  tournament.match.assigned    { match_id, participant_a, participant_b }
  tournament.round.completed   { tournament_id, round_id, standings_snapshot }
  tournament.completed         { tournament_id, final_standings[] }
  tournament.results.published { tournament_id }
  tournament.participant.disqualified { tournament_id, participant_id, reason }
  tournament.fraud.flagged     { tournament_id, flag_type, entity_id }
  tournament.prize.disbursed   { tournament_id, participant_id, amount }
```

### 17.2 Events Consumed by Tournament Agent

```
Topic: match.events
  MATCH_COMPLETED   → advance bracket
  MATCH_ABANDONED   → handle per Section 7.2
  MATCH_DISPUTED    → freeze round

Topic: billing.events
  PAYMENT_CONFIRMED → confirm registration entry fee
  PAYMENT_FAILED    → deny registration

Topic: safety.events
  SAFETY_FLAG_RAISED → freeze match, notify admin

Topic: belt.events
  BELT_ELIGIBILITY_CHECKED → notification to participant
```

No synchronous cross-domain chaining.
Event-driven only.

---

## SECTION 18 — FAILURE HANDLING (DETERMINISTIC)

| Failure Condition                         | Agent Action                                                     |
|-------------------------------------------|------------------------------------------------------------------|
| Match Engine unavailable at round start   | Round delayed by 15 min; admin notified; retry once             |
| Billing Service timeout at registration   | Registration PENDING; retry 3x; then DENIED with reason         |
| Leaderboard compute error                 | Serve last valid snapshot; flag error in observability           |
| Bracket generation failure                | SEEDING state held; admin alerted; no partial bracket output    |
| Prize disbursement failure                | Freeze; log; alert; no auto-retry without admin confirmation     |
| Kafka consumer lag > 60s                  | Alert; agent enters DEGRADED mode; all writes buffered to Redis  |
| Participant record inconsistency          | STOP state transition; log; escalate to Admin Governance         |

No partial outputs.
No silent failures.
Every failure produces an AuditLog entry and an observability event.

---

## SECTION 19 — DATA RETENTION & BACKUP

```
Tournament records           → 7-year retention (billing + legal compliance)
AuditLog entries             → immutable, permanent (WORM)
Match results within tournament → retained with tournament record
Leaderboard snapshots        → 3-year retention per round
Prize disbursement records   → 7-year retention (GST/VAT compliance)
Participant registration data → retained per User data retention policy
```

Backup per Ecoskiller infrastructure standards:
- PostgreSQL daily backup (tournament schema)
- ClickHouse analytics backup
- Velero cluster-level backup includes tournament namespace state

---

## SECTION 20 — MULTI-TENANT ISOLATION

```
TENANT_ISOLATION_RULE:
  All tournament records scoped to tenant_id
  Row-level security enforced at PostgreSQL level on tenant_id
  Cross-tenant tournament data access FORBIDDEN
  Tenant admin can only manage tournaments within own tenant
  PLATFORM_SUPER_ADMIN has cross-tenant read access for governance only
  No shared tournament_id namespace across tenants
```

---

## SECTION 21 — APPEALS & DISPUTE GOVERNANCE

```
DISPUTE_TYPES:
  Match result dispute       → raised by participant after MATCH_COMPLETED
  Score dispute              → raised by participant or mentor
  Disqualification appeal    → raised by disqualified participant
  Prize dispute              → raised after RESULTS_PUBLISHED

DISPUTE_FLOW:
  1. Participant raises dispute (match_id or tournament_id tagged)
  2. Round progression FROZEN for disputed match (others continue)
  3. Admin Governance Service receives dispute ticket
  4. Replay evidence attached automatically
  5. Admin reviews within defined SLA
  6. Decision logged to AuditLog (immutable)
  7. If score mutated: new AuditLog record with original + corrected value
  8. Round progression resumes after dispute resolution

APPEAL_ESCALATION:
  Unresolved after SLA → escalates to MASTER_ORGANIZER
  Platform-level appeal → PLATFORM_SUPER_ADMIN final decision
  All decisions versioned and logged
```

---

## SECTION 22 — ANTIGRAVITY RUN COMMAND

Paste this block into the Antigravity master prompt to activate this agent:

```
BEGIN LOCKED AGENT ARTIFACT — TOURNAMENT_MANAGEMENT_AGENT v1.0

System Role          : Tournament Management Agent · Ecoskiller Dojo SaaS
Execution Engine     : ANTIGRAVITY
Domain               : Program & Event Management
Stack                : Flutter (operational UI) + React/Next.js (SEO/public)
Lifecycle Authority  : State machine declared in Section 3 — non-negotiable
Match Authority      : Dojo Match Engine (consumer contract, Section 7)
Scoring Authority    : Scoring Engine (immutable input, Section 8)
Billing Authority    : Billing & Subscription Service (Section 9)
Belt Authority       : Belt Engine (trigger-only, Section 12)
Transport            : WebSocket (events) · Kafka (async) · Redis (state/leaderboard)
Media                : NOT OWNED BY THIS AGENT — LiveKit/WebRTC via Match Engine
Security             : Keycloak RBAC · JWT · Row-level PostgreSQL isolation
Audit                : Immutable AuditLog on every state transition
Fairness             : Collusion detection · Opponent band limits · Scenario fairness
Safety               : Safety overrides scoring — no exceptions
Multi-Tenant         : Enforced on tenant_id at every layer
Observability        : Prometheus metrics · Grafana dashboards · Alerting declared
Failure Mode         : STOP → LOG → ALERT → NO PARTIAL OUTPUT
Interpretation       : FORBIDDEN
Architecture Change  : ADD-ONLY VIA VERSION BUMP
UI Boundaries        : Section 15 — Antigravity must not exceed declared screens
React Boundary       : Public read-only pages only · No auth actions · No match room

ANTIGRAVITY_SAFE        = TRUE
ANTIGRAVITY_CONFUSION   = IMPOSSIBLE
READY_FOR               = ANTIGRAVITY_PRODUCTION

END LOCKED AGENT ARTIFACT — TOURNAMENT_MANAGEMENT_AGENT v1.0
```

---

## SECTION 23 — COMPLIANCE SEAL

```
TOURNAMENT_AGENT COMPLIANCE SEAL

✔ State Machine Deterministic
✔ Eligibility Enforcement Non-Bypassable
✔ Score Immutability Enforced
✔ Prize Accounting Double-Entry
✔ Fraud Detection Active
✔ Fairness Engine Active
✔ Collusion Detection Active
✔ Safety Overrides Scoring
✔ Audit Log Immutable
✔ Multi-Tenant Isolation Enforced
✔ Billing Gate on Entry Fee
✔ Belt Trigger Mentor-Confirmed (not auto)
✔ Observability Non-Optional
✔ Failure Handling Deterministic
✔ Dispute Governance Sealed
✔ Appeals System Versioned
✔ Data Retention Defined
✔ Antigravity UI Boundary Declared
✔ React/Flutter Split Locked
✔ Event-Driven Only (No Sync Cross-Domain)
✔ Zero Paid SaaS Dependencies
✔ Self-Hosted Open-Source Stack
✔ GDPR / India DPDP Pattern Ready

MUTATION POLICY   : ADD-ONLY VIA VERSION BUMP
INTERPRETATION    : FORBIDDEN
EXECUTION ENGINE  : ANTIGRAVITY
STATUS            : FINAL · LOCKED · SEALED
```

---

*Ecoskiller Platform · Dojo SaaS · Tournament Management Agent · v1.0 · March 2026 · Internal Use Only · Confidential*
