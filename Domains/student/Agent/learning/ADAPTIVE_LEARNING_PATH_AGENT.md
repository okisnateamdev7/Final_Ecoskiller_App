# 🔒 ADAPTIVE_LEARNING_PATH_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### ANTIGRAVITY MODULE — ECOSKILLER PLATFORM

---

```
Artifact Class       : PRODUCTION SYSTEM BLUEPRINT
Module Code          : ECOSKILLER-ALPA-ANTIGRAVITY-v1.0
Status               : SEALED · LOCKED · GOVERNED · DETERMINISTIC
Mutation Policy      : ADD-ONLY VIA VERSION BUMP
Interpretation Auth  : NONE
Execution Auth       : HUMAN DECLARATION ONLY
Creative Deviation   : FORBIDDEN
Assumption Filling   : FORBIDDEN
Default Behavior     : DENY
Failure Mode         : STOP_EXECUTION → REPORT → NO PARTIAL OUTPUT
```

---

## ⚠️ ANTIGRAVITY — ALPA DEFINITION (NON-NEGOTIABLE)

> **ANTIGRAVITY in learning** is the computed force that continuously reshapes a user's learning trajectory to escape skill stagnation, competency plateaus, and career irrelevance — in real time, without human intervention.
>
> The ADAPTIVE_LEARNING_PATH_AGENT (ALPA) is the execution engine that constructs, mutates, enforces, and audits personalized learning paths for every user in the ecosystem. A learning path is not a suggestion — it is a prescribed, sequenced, time-boxed curriculum derived from intelligence vectors, belt progression state, recruiter demand signals, dojo match outcomes, and real-time behavioral compliance.
>
> ALPA does not assign courses. It engineers trajectories.
> ALPA does not track progress. It enforces advancement.
> ALPA does not guess. It computes — from verified signal truth.

---

## SECTION 0 — SYSTEM POSITION IN ECOSKILLER ARCHITECTURE

```
LANE F — INTELLIGENCE
  └── ALPA (ADAPTIVE_LEARNING_PATH_AGENT)
        ├── Sibling: ASRA (AI_BASED_SKILL_RECOMMENDATION_AGENT)
        ├── ASRA feeds ALPA: recommendation_contract_ready → path seed input
        ├── Depends on: Lane A (identity_ready, rbac_ready)
        ├── Depends on: Lane B (db_ready, search_ready)
        ├── Depends on: Lane C (api_contract_ready)
        ├── Depends on: Intelligence Profile Service (Section XI)
        ├── Depends on: Dojo Match Engine (Section V.8)
        ├── Depends on: Scoring Engine (Section V.9)
        ├── Depends on: Belt & Certification Engine (Section V.10)
        ├── Depends on: Education Service (Courses, Study Rooms)
        ├── Depends on: Voice GD Orchestrator (Section V.7)
        └── Produces: alpa_contract_ready, path_graph_ready
```

**Dependency Chain Rule:**
ASRA `recommendation_contract_ready` must be active before ALPA path construction begins.
ALPA `path_graph_ready` must be verified before Student Dashboard Lane E widget renders learning path UI.

Absence of ALPA → Lane F contract gate CANNOT close → STOP EXECUTION

---

## SECTION I — AGENT IDENTITY & EXECUTION LAWS

### I.1 Agent Identity
```
Agent Name    : ADAPTIVE_LEARNING_PATH_AGENT
Agent Alias   : ALPA
Module Class  : Enterprise Optimization + Trust Infrastructure
Layer         : Intelligence Layer (Lane F)
Execution     : Continuous adaptive loop + event-triggered mutation
Trigger Types : [scheduled, event-driven, on-demand, belt-gate-triggered]
```

### I.2 Non-Negotiable Execution Laws

```
LAW-01 : Every learning path must be seeded by a verified ASRA recommendation (min 1)
LAW-02 : No path node may reference a skill not present in the Skill Registry
LAW-03 : No path may skip a belt prerequisite node
LAW-04 : Path mutation is triggered only by verified behavioral events — never by user request alone
LAW-05 : Every path mutation must produce a PathMutationRecord with before/after diff
LAW-06 : Completion of a path node must be verified by an external engine (Scoring, Mentor, System)
LAW-07 : Path staleness > 14 days without progress → STALE_FLAG raised, user notified
LAW-08 : Domain isolation enforced — path nodes from foreign domains forbidden unless DomainAccessGrant exists
LAW-09 : Tenant isolation enforced — path logic never crosses tenant boundaries
LAW-10 : Parent-linked students (age < 18) — path summaries delivered to parent dashboard (read-only)
LAW-11 : Path confidence score must be ≥ 0.60 for path to be surfaced
LAW-12 : No path may be auto-completed — only verified completion events from authorized engines are accepted
LAW-13 : Immutable audit log entry required for every path event (create, mutate, complete, expire, abort)
LAW-14 : Path Velocity Index must be computed per user per 30-day window and fed back to Intelligence Profile Service
LAW-15 : Zero hallucination tolerance — all path steps must resolve to real, active content nodes
```

Violation of any LAW → STOP EXECUTION → REPORT LAW_VIOLATION_CODE [ALPA-LAW-XX]

---

## SECTION II — PATH SIGNAL ARCHITECTURE (INPUT LAYER)

ALPA consumes ten verified signal classes. All signals are READ-ONLY from source services. No signal mutation is permitted within ALPA.

### II.1 Signal Class Registry

| Signal ID | Signal Class | Source Service | Refresh Rate | Criticality |
|-----------|-------------|----------------|--------------|-------------|
| PSIG-01 | ASRA Recommendation Vector (top-5 skills, SGS scores) | ASRA Service | On recommendation.generated event | CRITICAL |
| PSIG-02 | Intelligence DNA Vector (8-type profile) | Intelligence Profile Service | On recalculation event | CRITICAL |
| PSIG-03 | Dojo Belt State (current belt, pending belt, failed attempts) | Belt & Certification Engine | On belt.eligible / match.scored | CRITICAL |
| PSIG-04 | Dojo Match Performance (scenario accuracy, pressure pass/fail) | Scoring Engine | On match.scored | HIGH |
| PSIG-05 | Course Completion State (completed, in-progress, abandoned) | Education Service | On enrollment + completion events | HIGH |
| PSIG-06 | GD Participation Score (turn compliance, interrupt rate, silence ratio) | Voice GD Orchestrator | On gd.completed | MEDIUM |
| PSIG-07 | Recruiter Demand Index (from ASRA market demand endpoint) | ASRA → OpenSearch | ≤ 24 hours | HIGH |
| PSIG-08 | Application Pipeline Outcomes (shortlisted, rejected, offer stage) | Application Service | On application event | HIGH |
| PSIG-09 | Streak & Habit Compliance (daily activity, streak count, challenges) | Streak Tracker / Education | Daily | MEDIUM |
| PSIG-10 | Peer Benchmark Delta (leaderboard position change month-over-month) | Analytics Service | Weekly | LOW |

### II.2 Signal Ingestion Contract

```
PathSignalIngestionEvent {
  signal_id        : UUID
  signal_class     : PSIG-01..PSIG-10
  user_id          : UUID (tenant-scoped)
  tenant_id        : UUID
  domain           : [Arts | Commerce | Science | Technology | Administration]
  raw_value        : JSON (schema locked per signal_class)
  ingested_at      : ISO8601 UTC
  source_service   : String
  integrity_hash   : SHA-256 of raw_value
  signal_version   : Integer (monotonic increment per user per signal_class)
}
```

Signal missing `integrity_hash` → REJECTED → LOGGED → NO path mutation triggered
Duplicate `signal_version` → REJECTED → IDEMPOTENCY VIOLATION logged

---

## SECTION III — PATH CONSTRUCTION ARCHITECTURE

### III.1 Path Graph Model

A learning path is modeled as a **Directed Acyclic Graph (DAG)** — not a linear list. This allows concurrent learning streams, prerequisite branching, and conditional advancement.

```
PathGraph {
  path_id          : UUID
  user_id          : UUID
  tenant_id        : UUID
  domain           : String
  root_skill_node  : UUID (anchored from ASRA PSIG-01)
  graph_nodes      : PathNode[]
  graph_edges      : PathEdge[]
  critical_path    : UUID[] (ordered fastest completion route)
  parallel_streams : UUID[][] (nodes executable concurrently)
  status           : ENUM(ACTIVE, STALE, COMPLETED, ABORTED, SUSPENDED)
  confidence_score : DECIMAL(5,4)
  path_velocity    : DECIMAL(5,4) (nodes completed per week, rolling avg)
  generated_at     : TIMESTAMPTZ
  last_mutated_at  : TIMESTAMPTZ
  expires_at       : TIMESTAMPTZ (90-day rolling window, reset on activity)
}
```

### III.2 PathNode Schema (LOCKED)

```
PathNode {
  node_id              : UUID PRIMARY KEY
  path_id              : UUID NOT NULL
  node_type            : ENUM(
                           COURSE,
                           DOJO_MATCH,
                           DOJO_SCENARIO,
                           GD_ROUND,
                           CERTIFICATION_EXAM,
                           MENTORED_SESSION,
                           PEER_QUIZ,
                           PROJECT_SUBMISSION,
                           BELT_GATE
                         ) NOT NULL
  content_ref_id       : UUID NOT NULL → FK to node_type source service
  content_title        : VARCHAR(255) NOT NULL
  skill_node_id        : UUID NOT NULL → FK Skill Registry
  belt_prerequisite    : VARCHAR(50)
  belt_advancement     : VARCHAR(50)  ← belt unlocked on completion
  estimated_hours      : INTEGER NOT NULL
  deadline_days        : INTEGER       ← relative to path activation
  sequence_order       : INTEGER NOT NULL
  parallel_group_id    : UUID          ← null if sequential
  completion_verifier  : ENUM(SCORING_ENGINE, MENTOR, SYSTEM, PEER) NOT NULL
  completion_criteria  : JSONB NOT NULL ← locked rules for verification
  status               : ENUM(PENDING, IN_PROGRESS, COMPLETED, SKIPPED, FAILED) DEFAULT PENDING
  completed_at         : TIMESTAMPTZ
  verified_by          : UUID          ← verifier_id (engine or mentor)
  node_confidence      : DECIMAL(5,4)
  mutation_generation  : INTEGER DEFAULT 0  ← increments on each mutation
}
```

### III.3 PathEdge Schema

```
PathEdge {
  edge_id       : UUID PRIMARY KEY
  path_id       : UUID NOT NULL
  from_node_id  : UUID NOT NULL → FK PathNode
  to_node_id    : UUID NOT NULL → FK PathNode
  edge_type     : ENUM(SEQUENTIAL, PREREQUISITE, PARALLEL, CONDITIONAL) NOT NULL
  condition     : JSONB  ← null unless CONDITIONAL
                         ← example: { "if": "node_X.status == FAILED", "then": "activate_node_Y" }
}
```

### III.4 Path Construction Algorithm (LOCKED)

```
PATH_CONSTRUCTION_PIPELINE(user_id):

STEP 1 → SIGNAL AGGREGATION
  Fetch PSIG-01..PSIG-10 from Redis signal cache for user_id
  Validate integrity_hash per signal
  Stale signals (PSIG-01 > 72h, PSIG-02 > 72h, PSIG-03 > 48h) → refresh from source

STEP 2 → ROOT SKILL SELECTION
  From PSIG-01 (ASRA): select top-SGS skill not yet in active path
  Root skill anchors entire path DAG
  If PSIG-01 empty → STOP, raise PATH_CONSTRUCTION_BLOCKED_NO_RECOMMENDATION

STEP 3 → BELT GATE INSERTION (MANDATORY)
  From PSIG-03 (Belt State):
    If current_belt < skill.belt_prerequisite:
      Insert BELT_GATE node as path entry point
      All path nodes requiring higher belt positioned AFTER BELT_GATE
      Belt gate completion_verifier = MENTOR (never SYSTEM alone)

STEP 4 → INTELLIGENCE-ALIGNED NODE SELECTION
  From PSIG-02 (Intelligence DNA):
    Map dominant intelligence types to preferred learning modalities:
      Logical-Mathematical → weight DOJO_MATCH + DOJO_SCENARIO nodes higher
      Linguistic → weight COURSE + GD_ROUND nodes higher
      Interpersonal → weight MENTORED_SESSION + PEER_QUIZ higher
      Kinesthetic → weight PROJECT_SUBMISSION + DOJO_SCENARIO higher
      [All 8 intelligence types mapped to node_type weight matrix — see Appendix A]
    Generate weighted candidate node pool from Skill Registry + Content Services

STEP 5 → RECRUITER DEMAND ALIGNMENT
  From PSIG-07:
    Sort node candidates by recruiter_demand_rank ascending
    Nodes with demand_rank ≤ 10 elevated to critical_path
    Nodes with demand_rank > 30 placed in parallel_streams only

STEP 6 → DOJO PERFORMANCE INTEGRATION
  From PSIG-04:
    If match accuracy < 0.60 on skill_X → insert DOJO_SCENARIO remediation node before DOJO_MATCH
    If pressure scenario failed → insert pressure_simulation node (mandatory, not skippable)
    If match accuracy ≥ 0.85 → skip foundational course nodes (earned advancement)

STEP 7 → DAG ASSEMBLY
  Build graph: root node → prerequisite nodes → core nodes → belt gate → advanced nodes
  Apply parallel_group_id for concurrently executable nodes
  Assign sequence_order per sequential dependency chain
  Compute critical_path = shortest valid completion route through DAG

STEP 8 → CONFIDENCE SCORING
  path_confidence = weighted average of:
    (signal_freshness_composite × 0.35) +
    (node_coverage_of_skill_gap × 0.30) +
    (recruiter_demand_alignment × 0.20) +
    (belt_feasibility_score × 0.15)
  If path_confidence < 0.60 → SUPPRESS path, log PATH_CONFIDENCE_BELOW_FLOOR

STEP 9 → TRUST GATE VALIDATION (see Section V)
  Run all 9 path trust gates
  Any hard-fail gate → abort path construction

STEP 10 → PERSIST & EMIT
  Write PathGraph + PathNodes + PathEdges to PostgreSQL
  Write PathAuditLog entry: event_type = CREATED
  Emit Kafka: path.created { path_id, user_id, tenant_id, node_count, confidence_score }
  Push to Flutter dashboard via WebSocket: path.ready event
```

---

## SECTION IV — ADAPTIVE MUTATION ENGINE

The defining feature of ALPA is that paths mutate — not on a schedule, not on user request, but on verified behavioral truth. Every mutation produces a full diff audit record.

### IV.1 Mutation Trigger Registry

| Trigger ID | Event | Mutation Type | Priority |
|-----------|-------|--------------|---------|
| MT-01 | `match.scored` with accuracy < 0.60 | Insert remediation node before failed skill's DOJO_MATCH | IMMEDIATE |
| MT-02 | `match.scored` with accuracy ≥ 0.85 | Remove foundational course node (earned skip) | IMMEDIATE |
| MT-03 | `belt.eligible` confirmed | Unlock next belt-gated nodes, activate BELT_GATE node | IMMEDIATE |
| MT-04 | `gd.completed` with interrupt_attempts > 3 | Insert GD_ROUND discipline node into path | HIGH |
| MT-05 | `recommendation.generated` (new ASRA PSIG-01) | Evaluate new SGS skill vs current path; insert if SGS delta > 0.15 | SCHEDULED (daily) |
| MT-06 | Course node abandoned (> 7 days no progress) | Replace with lighter-weight PEER_QUIZ equivalent | NEXT-CYCLE |
| MT-07 | PSIG-08: application rejection citing skill gap | Elevate corresponding path node to PRIORITY_CRITICAL | IMMEDIATE |
| MT-08 | PSIG-09: streak broken (> 3 day gap) | Insert re-engagement micro-node (30-min challenge) | NEXT-CYCLE |
| MT-09 | PSIG-10: peer benchmark rank drops > 5 positions | Insert peer-gap correction sprint (3-node accelerated sequence) | SCHEDULED |
| MT-10 | Path node completed ahead of deadline by > 30% | Pull forward next sequential node, compress estimated timeline | IMMEDIATE |
| MT-11 | Intelligence DNA vector recalculated (PSIG-02 update) | Re-weight node type preferences, swap modality if dominant type shifts | SCHEDULED |
| MT-12 | Tenant admin overrides domain priority | Recompute node sequencing per new domain weight configuration | ADMIN_TRIGGERED |

### IV.2 Mutation Execution Protocol

```
MUTATION_PIPELINE(trigger_id, user_id, trigger_payload):

  STEP 1 → FETCH ACTIVE PATH
    Load PathGraph for user_id where status = ACTIVE
    If no active path → trigger PATH_CONSTRUCTION_PIPELINE instead

  STEP 2 → SNAPSHOT PRE-MUTATION STATE
    Serialize current PathGraph (nodes, edges, sequence_orders) → before_snapshot JSONB

  STEP 3 → APPLY MUTATION RULE (per trigger_id)
    Execute mutation logic for MT-XX
    Validate: mutation does not violate any of LAW-01..LAW-15
    Validate: mutated path still forms valid DAG (no cycles, no orphan nodes)
    Compute new path_confidence post-mutation
    If new path_confidence < 0.60 → ABORT mutation, retain previous path, log MUTATION_ABORTED

  STEP 4 → SNAPSHOT POST-MUTATION STATE
    Serialize updated PathGraph → after_snapshot JSONB

  STEP 5 → WRITE MUTATION RECORD
    Write PathMutationRecord (see IV.3)
    Write PathAuditLog entry: event_type = MUTATED

  STEP 6 → EMIT EVENT
    Kafka: path.mutated { path_id, user_id, mutation_trigger, nodes_added, nodes_removed }
    WebSocket: path.updated event → Flutter live update

  STEP 7 → NOTIFY
    If mutation changes critical_path → push notification to user
    If mutation adds PRIORITY_CRITICAL node → push notification + parent notification (student < 18)
```

### IV.3 PathMutationRecord Schema

```sql
PathMutationRecord {
  mutation_id          UUID PRIMARY KEY
  path_id              UUID NOT NULL → FK PathGraph
  user_id              UUID NOT NULL
  tenant_id            UUID NOT NULL
  trigger_id           VARCHAR(10) NOT NULL  ← MT-01..MT-12
  trigger_event_ref    UUID                  ← Kafka event_id that triggered mutation
  before_snapshot      JSONB NOT NULL
  after_snapshot       JSONB NOT NULL
  nodes_added          UUID[]
  nodes_removed        UUID[]
  nodes_resequenced    UUID[]
  confidence_before    DECIMAL(5,4) NOT NULL
  confidence_after     DECIMAL(5,4) NOT NULL
  mutation_valid       BOOLEAN NOT NULL
  abort_reason         TEXT
  mutated_at           TIMESTAMPTZ NOT NULL
  mutation_hash        VARCHAR(64) NOT NULL  ← SHA-256 of before+after snapshots
  audit_log_ref        UUID NOT NULL → FK PathAuditLog
}
```

Every mutation is fully reversible from `before_snapshot`. Rollback capability is mandatory for admin governance.

---

## SECTION V — TRUST INFRASTRUCTURE (PATH TRUST LAYER)

All 9 trust gates are mandatory. Partial gate implementation → STOP EXECUTION.

### V.1 Path Trust Gate Registry

```
PATH-GATE-1   SKILL NODE EXISTENCE
              Every path node's skill_node_id must exist in Skill Registry with status = ACTIVE
              Failure → HARD REJECT node, log GATE1_VIOLATION

PATH-GATE-2   CONTENT NODE RESOLUTION
              Every node's content_ref_id must resolve to an active content entity in the source service
              (Course, Dojo Scenario, GD Room config, Certification, etc.)
              Failure → HARD REJECT node, log GATE2_VIOLATION

PATH-GATE-3   BELT PREREQUISITE ENFORCEMENT
              Nodes requiring belt_prerequisite ≥ user.current_belt must be blocked behind BELT_GATE node
              Direct access to gated nodes forbidden
              Failure → RESTRUCTURE path (not reject), insert BELT_GATE automatically, log GATE3_RESTRUCTURE

PATH-GATE-4   DOMAIN ISOLATION
              All path nodes must match user.domain
              Cross-domain nodes require DomainAccessGrant row for user_id
              Failure → HARD REJECT cross-domain node, log GATE4_VIOLATION + ALERT

PATH-GATE-5   TENANT ISOLATION
              path.tenant_id must match user.tenant_id on every write and read
              Failure → SECURITY ALERT → STOP CONSTRUCTION → ESCALATE to Wazuh

PATH-GATE-6   PATH CONFIDENCE FLOOR
              Final path_confidence must be ≥ 0.60 before materialization
              Failure → SUPPRESS path, raise PATH_CONFIDENCE_SUPPRESSED event

PATH-GATE-7   DAG VALIDITY
              Path graph must form valid DAG: no cycles, no orphan nodes, no disconnected subgraphs
              Every leaf node must have completion_verifier defined
              Failure → HARD REJECT path, log GATE7_DAG_INVALID

PATH-GATE-8   COMPLETION VERIFIER AUTHORITY
              BELT_GATE nodes → completion_verifier = MENTOR mandatory (SYSTEM alone forbidden)
              CERTIFICATION_EXAM nodes → completion_verifier = SYSTEM (Scoring Engine)
              MENTORED_SESSION nodes → completion_verifier = MENTOR
              DOJO_MATCH / DOJO_SCENARIO → completion_verifier = SCORING_ENGINE
              COURSE / PEER_QUIZ → completion_verifier = SYSTEM or PEER
              Violation → HARD REJECT node with wrong verifier

PATH-GATE-9   PARENT CONSENT (STUDENT < 18)
              If user.role == STUDENT and user.age < 18:
                path.parent_visibility must = TRUE
                parent_notification must be queued
              Failure → SOFT BLOCK (path created, not surfaced until parent notification sent)
```

Gate-5 failure is the only gate that triggers complete system halt. All other gates log and either reject/restructure without halting the full system.

### V.2 Immutable Path Audit Log

Every path lifecycle event produces an immutable, hash-chained audit record.

```sql
PathAuditLog {
  audit_id              UUID PRIMARY KEY
  path_id               UUID NOT NULL
  user_id               UUID NOT NULL
  tenant_id             UUID NOT NULL
  event_type            ENUM(
                          PATH_CREATED,
                          PATH_MUTATED,
                          NODE_COMPLETED,
                          NODE_FAILED,
                          NODE_SKIPPED,
                          BELT_GATE_UNLOCKED,
                          PATH_STALE_FLAGGED,
                          PATH_COMPLETED,
                          PATH_ABORTED,
                          PATH_EXPIRED,
                          TRUST_GATE_VIOLATED,
                          MUTATION_ABORTED,
                          PARENT_NOTIFIED,
                          ROLLBACK_EXECUTED
                        ) NOT NULL
  event_payload         JSONB NOT NULL
  signal_snapshot       JSONB NOT NULL  ← exact PSIG values at event time
  path_confidence_snap  DECIMAL(5,4)
  performed_by          ENUM(SYSTEM, USER, MENTOR, ADMIN, SCORING_ENGINE) NOT NULL
  performed_at          TIMESTAMPTZ NOT NULL
  immutable_hash        VARCHAR(64) NOT NULL  ← SHA-256 of event + path state
  previous_audit_hash   VARCHAR(64)           ← hash chain for tamper detection
}
```

**Tamper Detection:** Hash chain verification on every read.
Chain break → SECURITY_ALERT → Wazuh escalation → Admin Governance Service flagged.

### V.3 Path Explainability Contract

Every surfaced path must carry a human-readable construction narrative. Black-box output is FORBIDDEN.

```
path_narrative: "Your learning path was built because:
  - ASRA identified [Skill Name] as your highest-priority gap (SGS: 0.87) [PSIG-01]
  - Your Intelligence Profile shows [TYPE] dominance — matched to [node_types] learning modes [PSIG-02]
  - Your current [Belt Level] means you must complete the [Belt Gate] before advancing [PSIG-03]
  - Recruiters searched [Skill Name] [N] times in [Domain] this week [PSIG-07]
  - Your application to [Company] was rejected citing this skill gap [PSIG-08]
  - Completing this path advances your Dojo belt from [Current] → [Next]"

Minimum 2 signal references required in narrative.
Single-signal narrative → REJECTED, path not surfaced.
```

### V.4 Path Trust Score (PTS)

```
PTS = (
  (signal_freshness_composite × 0.35) +
  (gate_pass_rate × 0.25) +
  (node_resolution_rate × 0.25) +
  (historical_path_completion_rate × 0.15)
)

PTS Display:
  0.85 - 1.00 → "High Confidence"  (🟢)
  0.65 - 0.84 → "Moderate"         (🟡)
  < 0.65      → "Indicative"       (🟠)
```

PTS must appear on every path card and path detail screen. Absence → UI CONTRACT VIOLATION.

---

## SECTION VI — PATH VELOCITY INDEX (PVI)

The Path Velocity Index is a per-user metric tracking how fast a user advances through learning paths. It feeds directly back into the Intelligence Profile Service for long-term intelligence evolution scoring.

```
PVI(user_id, window_days=30) = (
  nodes_completed_in_window ÷ nodes_scheduled_in_window
) × path_confidence_average

PVI Scale:
  PVI ≥ 0.80 → ACCELERATED    → trigger MT-10 (pull forward timeline)
  PVI 0.50-0.79 → ON_TRACK    → no mutation triggered
  PVI 0.30-0.49 → LAGGING     → trigger MT-08/MT-09 re-engagement
  PVI < 0.30   → STALLED      → PATH_STALE_FLAG raised + mentor notification

PVI emitted as event: pvi.computed { user_id, pvi_score, window, computed_at }
Consumed by: Intelligence Evolution Timeline Service (Section XI)
```

---

## SECTION VII — BELT GATE ENGINE (CRITICAL SUB-COMPONENT)

Belt gates are the highest-trust nodes in any path. They represent promotion decision points and are governed by the strictest rules in ALPA.

### VII.1 Belt Gate Rules (NON-NEGOTIABLE)

```
RULE-BG-1  BELT_GATE node may only be inserted — never removed — once a path is active
RULE-BG-2  completion_verifier for BELT_GATE = MENTOR (mandatory, not overridable)
RULE-BG-3  Auto-promotion is FORBIDDEN (inherited from Dojo SECTION G law)
RULE-BG-4  BELT_GATE completion requires:
             - match_count threshold met (from Belt Engine config)
             - score_threshold met (from Belt Engine config)
             - pressure_scenario_pass = TRUE
             - mentor_confirmation = TRUE (async, mentor must act)
             - audit_record_written = TRUE
RULE-BG-5  If mentor does not confirm within 7 days → BELT_GATE status = PENDING_MENTOR
             → Notification escalation chain: D1, D3, D7 reminders
             → After D14: escalate to Admin Governance Service
RULE-BG-6  All path nodes requiring the next belt level remain in LOCKED state until BELT_GATE completed
RULE-BG-7  BELT_GATE completion event triggers: belt.eligible event → Belt & Certification Engine
             Belt Engine handles actual belt promotion — ALPA does NOT promote belts directly
```

### VII.2 Belt Gate Node States

```
State Machine:
  LOCKED → AVAILABLE (when prerequisites met)
  AVAILABLE → IN_PROGRESS (when user initiates gate activities)
  IN_PROGRESS → PENDING_MENTOR (when system criteria met, awaiting mentor)
  PENDING_MENTOR → COMPLETED (mentor confirms)
  PENDING_MENTOR → FAILED (mentor denies, or 14-day escalation unresolved)
  FAILED → IN_PROGRESS (user may retry after cooldown_days from Belt Engine config)
  COMPLETED → terminal (immutable)
```

---

## SECTION VIII — DATABASE SCHEMA (LOCKED)

### VIII.1 Core Tables

```sql
-- Path Graph (one active path per user per domain)
PathGraph {
  path_id              UUID PRIMARY KEY
  user_id              UUID NOT NULL
  tenant_id            UUID NOT NULL
  domain               VARCHAR(50) NOT NULL
  root_skill_node_id   UUID NOT NULL
  status               ENUM(ACTIVE, STALE, COMPLETED, ABORTED, SUSPENDED) DEFAULT ACTIVE
  confidence_score     DECIMAL(5,4) NOT NULL
  path_velocity        DECIMAL(5,4) DEFAULT 0
  path_narrative       TEXT NOT NULL
  pts_score            DECIMAL(5,4)
  generated_at         TIMESTAMPTZ NOT NULL
  last_mutated_at      TIMESTAMPTZ
  last_activity_at     TIMESTAMPTZ
  expires_at           TIMESTAMPTZ NOT NULL
  parent_visibility    BOOLEAN DEFAULT FALSE
  construction_hash    VARCHAR(64) NOT NULL
}

-- Path Nodes
PathNode { ... }          -- See Section III.2

-- Path Edges
PathEdge { ... }          -- See Section III.3

-- Path Mutation Records
PathMutationRecord { ... } -- See Section IV.3

-- Path Audit Log
PathAuditLog { ... }       -- See Section V.2

-- Intelligence-to-NodeType Weight Matrix (tenant-overridable within ±10% band)
IntelligenceNodeTypeWeight {
  weight_id            UUID PRIMARY KEY
  tenant_id            UUID NOT NULL
  intelligence_type    VARCHAR(50) NOT NULL  ← 8 types from Intelligence Profile Service
  node_type            VARCHAR(50) NOT NULL  ← ENUM matches PathNode.node_type
  weight               DECIMAL(4,3) NOT NULL
  override_applied     BOOLEAN DEFAULT FALSE
  overridden_by        UUID               ← admin_id
  overridden_at        TIMESTAMPTZ
}

-- Path Completion Registry (summary table for analytics)
PathCompletionRegistry {
  completion_id        UUID PRIMARY KEY
  path_id              UUID NOT NULL
  user_id              UUID NOT NULL
  tenant_id            UUID NOT NULL
  domain               VARCHAR(50) NOT NULL
  root_skill_node_id   UUID NOT NULL
  total_nodes          INTEGER NOT NULL
  completed_nodes      INTEGER NOT NULL
  failed_nodes         INTEGER NOT NULL
  skipped_nodes        INTEGER NOT NULL
  total_hours_spent    DECIMAL(8,2)
  time_to_complete_days INTEGER
  final_pvi            DECIMAL(5,4)
  belt_advanced_to     VARCHAR(50)
  completed_at         TIMESTAMPTZ NOT NULL
}

-- User Path Lock (prevents concurrent path construction)
UserPathLock {
  lock_id              UUID PRIMARY KEY
  user_id              UUID NOT NULL
  tenant_id            UUID NOT NULL
  domain               VARCHAR(50) NOT NULL
  locked_at            TIMESTAMPTZ NOT NULL
  lock_ttl_seconds     INTEGER DEFAULT 30
  UNIQUE (user_id, tenant_id, domain)
}
```

**Path Construction Concurrency Rule:** UserPathLock must be acquired (via Redis distributed lock backed by etcd) before any path construction or mutation. Lock TTL = 30 seconds. Failure to acquire → queue and retry × 3, then fail with PATH_LOCK_TIMEOUT.

---

## SECTION IX — API CONTRACT REGISTRY (LOCKED)

All APIs: REST + OpenAPI 3.1. No deviation permitted.

### IX.1 Core Endpoints

```
GET    /alpa/path/{user_id}
       → Returns active PathGraph with all nodes and edges
       → Auth: JWT (user | admin)
       → Response: PathGraph + PathNode[] + PathEdge[] + path_narrative + pts_score

POST   /alpa/path/construct
       → Triggers on-demand path construction for user
       → Auth: JWT (system | admin role only)
       → Body: { user_id, domain, force_reconstruct: boolean }
       → Side effect: acquires UserPathLock
       → Response: { job_id, status: QUEUED }

GET    /alpa/path/{user_id}/status
       → Returns path status, PVI, progress summary
       → Auth: JWT (user | parent read-only | admin)
       → Response: { status, confidence_score, pts_score, pvi, nodes_completed, nodes_total, next_node }

GET    /alpa/path/{user_id}/next-node
       → Returns the next prescribed action node for the user
       → Auth: JWT (user role)
       → Response: PathNode + completion_criteria + estimated_hours + deadline

POST   /alpa/path/node/{node_id}/complete
       → Records node completion (called by authorized engines only)
       → Auth: JWT (SCORING_ENGINE | MENTOR | SYSTEM service role)
       → Body: { verified_by, verification_payload: JSONB, completed_at }
       → Side effect: triggers mutation evaluation, emits node.completed event

POST   /alpa/path/node/{node_id}/fail
       → Records node failure (called by authorized engines only)
       → Auth: JWT (SCORING_ENGINE | MENTOR service role)
       → Body: { verified_by, failure_reason, retry_eligible: boolean }
       → Side effect: triggers MT-01 mutation evaluation

GET    /alpa/path/{user_id}/history
       → Returns all PathGraphs (active, completed, aborted, expired)
       → Auth: JWT (user | parent | admin)
       → Pagination: cursor-based

GET    /alpa/path/{user_id}/belt-gate
       → Returns current BELT_GATE node status and criteria
       → Auth: JWT (user | mentor | admin)
       → Response: PathNode (BELT_GATE type) + completion_criteria + mentor_status

POST   /alpa/path/{path_id}/belt-gate/mentor-confirm
       → Mentor confirms belt gate completion
       → Auth: JWT (MENTOR role, linked to user's domain)
       → Body: { mentor_id, confirmation_notes, confirmed: boolean }
       → Side effect: triggers belt.eligible event, unlocks gated nodes

GET    /alpa/path/{user_id}/mutations
       → Returns full mutation history for user's active path
       → Auth: JWT (user | admin)
       → Response: PathMutationRecord[] ordered by mutated_at desc

GET    /alpa/path/{user_id}/velocity
       → Returns PVI for user over configurable window
       → Auth: JWT (user | mentor | admin)
       → Response: { pvi_score, pvi_class, window_days, nodes_completed, nodes_scheduled }

GET    /alpa/analytics/domain/{domain}/completion-rate
       → Returns path completion rates per domain per tenant
       → Auth: JWT (ADMIN | ANALYTICS role)
       → Response: { domain, completion_rate, avg_pvi, avg_time_days, belt_advancement_rate }

POST   /alpa/admin/intelligence-weights/override
       → Admin override of IntelligenceNodeTypeWeight within ±10% band
       → Auth: JWT (TENANT_ADMIN + MFA re-auth)
       → Body: { overrides[], justification }
       → Writes to PathAuditLog before applying

GET    /alpa/parent/{student_id}/path-summary
       → Parent-visible path summary (read-only, no signal data)
       → Auth: JWT (PARENT role, linked to student)
       → Response: { current_node, progress_pct, next_milestone, pts_label, belt_status }
```

All endpoints require tenant_id resolved from JWT. Missing tenant context → 403 FORBIDDEN.

---

## SECTION X — EVENT BUS CONTRACTS (KAFKA)

### X.1 Produced Events (ALPA → Bus)

```
Topic: path.created
Payload: { path_id, user_id, tenant_id, domain, root_skill_node_id,
           node_count, confidence_score, generated_at }

Topic: path.mutated
Payload: { path_id, user_id, tenant_id, trigger_id, nodes_added,
           nodes_removed, confidence_before, confidence_after, mutated_at }

Topic: path.node.completed
Payload: { path_id, node_id, user_id, tenant_id, node_type,
           skill_node_id, verified_by, completed_at }

Topic: path.node.failed
Payload: { path_id, node_id, user_id, tenant_id, failure_reason,
           retry_eligible, failed_at }

Topic: path.belt_gate.unlocked
Payload: { path_id, user_id, tenant_id, belt_advanced_to,
           mentor_id, unlocked_at }

Topic: path.stale_flagged
Payload: { path_id, user_id, tenant_id, last_activity_at,
           stale_since_days, flagged_at }

Topic: path.completed
Payload: { path_id, user_id, tenant_id, total_nodes, completed_nodes,
           time_to_complete_days, final_pvi, belt_advanced_to, completed_at }

Topic: pvi.computed
Payload: { user_id, tenant_id, pvi_score, pvi_class, window_days,
           computed_at }
```

### X.2 Consumed Events (Bus → ALPA)

```
Topic: recommendation.generated  → triggers MT-05 evaluation
Topic: match.scored               → triggers MT-01 / MT-02 evaluation
Topic: belt.eligible              → triggers MT-03 (unlock belt-gated nodes)
Topic: gd.completed               → triggers MT-04 evaluation (interrupt_attempts check)
Topic: job.applied + rejected     → triggers MT-07 (application rejection signal)
Topic: user.created               → initializes path signal cache for new user
Topic: intelligence.recalculated  → triggers MT-11 (re-weight node type preferences)
```

No synchronous chaining across domain events. All consumption is async.

---

## SECTION XI — MICROSERVICE SPECIFICATION

```
Service Name         : alpa-service
Language             : Node.js (TypeScript) or Spring Boot
Namespace            : ecoskiller-intelligence (Lane F)
Replicas             : Min 2, Max 12 (HPA on CPU + Kafka consumer lag)
Database             : PostgreSQL (path schema, RLS enforced)
Distributed Lock     : Redis (UserPathLock) + etcd (strong consistency)
Cache                : Redis (signal cache, path status cache, TTL-managed)
Search               : OpenSearch (skill registry + content node queries)
Event Bus            : Kafka (produced topics per X.1 + consumed per X.2)
WebSocket Channel    : Outbound to Notification Service for real-time path updates
Exposed via          : Kong API Gateway (rate limited: 60 req/min/user)
Internal Calls       : Intelligence Profile Service, Belt Engine, Scoring Engine, ASRA
Auth                 : Keycloak JWT (RBAC enforced per endpoint per role)
```

### XI.1 Kubernetes Deployment Contract

```yaml
Namespace    : ecoskiller-intelligence
Deployment   : alpa-service
  Replicas   : 2
  Resources  :
    requests : cpu=500m, memory=1Gi
    limits   : cpu=2000m, memory=4Gi
  Probes     :
    liveness  : GET /health → 200 OK
    readiness : GET /ready → 200 OK
                (includes Redis, PG, etcd, Kafka connectivity)
HPA          :
  minReplicas: 2
  maxReplicas: 12
  metrics    :
    - CPU utilization > 70%
    - Kafka consumer lag > 500 messages
Service      : ClusterIP (internal only except Kong-exposed endpoints)
Ingress      : Kong routes /alpa/** → alpa-service
ConfigMap    : belt_gate_config, mutation_trigger_registry, pvi_thresholds,
               intelligence_weight_matrix_defaults, path_confidence_floors
Secrets      : DB credentials via HashiCorp Vault injection
               Kafka credentials via Vault
```

---

## SECTION XII — OBSERVABILITY (NON-OPTIONAL)

```
Metrics (Prometheus):
  alpa_paths_constructed_total           (counter, per tenant, per domain)
  alpa_mutations_triggered_total         (counter, per mutation trigger_id, per tenant)
  alpa_trust_gate_violations_total       (counter, per gate number)
  alpa_path_construction_duration_ms     (histogram)
  alpa_mutation_duration_ms              (histogram)
  alpa_pvi_score_distribution            (histogram, per domain)
  alpa_belt_gate_pending_count           (gauge, per tenant)
  alpa_stale_paths_count                 (gauge, per tenant)
  alpa_path_completion_rate_30d          (gauge, per tenant, per domain)
  alpa_node_type_distribution            (counter, per node_type, per domain)

Logs (Loki):
  Every path event → structured JSON (path_id, user_id, tenant_id, event_type)
  Every trust gate violation → structured JSON with gate_id + violation_detail
  Every mutation → before/after node count + trigger_id
  Every belt gate state change → full state + mentor_id

Traces (OpenTelemetry):
  Full trace per path construction pipeline (Step 1 → Step 10)
  Span per trust gate check (9 spans per construction)
  Span per mutation (per MT-XX)
  Span per belt gate state transition

Dashboards (Grafana):
  Panel: Path Construction Rate (per tenant, per domain)
  Panel: Mutation Trigger Heatmap (by MT-XX frequency)
  Panel: PVI Distribution Curve (per domain)
  Panel: Belt Gate Funnel (available → in_progress → pending_mentor → completed)
  Panel: Trust Gate Violation Breakdown (by gate number)
  Panel: Path Stale Rate (rolling 7-day)
  Panel: ALPA ANTIGRAVITY Effectiveness Index
         → % users whose PVI ≥ 0.50 (on-track or above) over rolling 30 days
  Panel: Kafka Consumer Lag (alpa consumer group)
```

Absence of any metric → OBSERVABILITY CONTRACT VIOLATION → STOP DEPLOYMENT

---

## SECTION XIII — UI / FLUTTER CONTRACT

### XIII.1 Mandatory Screens (5 required)

```
Screen 1 : ALPA Path Dashboard Widget (embedded in Student/User Dashboard)
  Content:
    - Active path title + root skill
    - PVI progress ring (visual velocity indicator)
    - Current node card: node_type icon, title, estimated_hours, deadline
    - Path progress bar: X of Y nodes completed
    - PTS score badge (🟢/🟡/🟠)
    - Belt gate status indicator (if BELT_GATE node is next)
    - "Continue Path" CTA → deep link to next node

Screen 2 : Full Path View (DAG Visualization)
  Content:
    - Interactive path graph: nodes as cards, edges as connecting lines
    - Node states visualized: PENDING (grey), IN_PROGRESS (blue), COMPLETED (green),
      FAILED (red), LOCKED (padlock icon for belt-gated)
    - Parallel streams shown as branching lanes
    - Critical path highlighted in accent color
    - Tap any node → Node Detail Panel
    - Path narrative (construction explanation, min 2 signal citations)
    - PTS score + signal freshness status
    - Mutation history button → Path Mutation Timeline screen

Screen 3 : Node Detail Panel (bottom sheet or dedicated screen)
  Content:
    - Node type, title, skill
    - Completion criteria (human-readable, not raw JSONB)
    - Estimated hours + deadline
    - Completion verifier type (Scoring Engine / Mentor / System / Peer)
    - For BELT_GATE: full belt criteria checklist + mentor confirmation status
    - "Start" / "Resume" / "View in [source service]" actions
    - Belt advancement badge (if completion unlocks next belt)

Screen 4 : Path Mutation Timeline
  Content:
    - Ordered list of all mutations (newest first)
    - Per mutation: trigger event, nodes added/removed, confidence delta
    - Human-readable mutation reason (e.g., "Match performance below 60% triggered remediation node")
    - "See before/after" diff toggle per mutation

Screen 5 : Parent Path Summary (read-only)
  Content:
    - Child's current path progress (% complete)
    - Current node (no signal data exposed)
    - PTS label only
    - Belt status (current → next)
    - Upcoming milestone (next 30 days)
    - PVI class label (Accelerated / On Track / Lagging / Stalled)
    - "Message Mentor" action
```

No screen may expose raw PSIG signal values, SGS scores, or internal path computation weights to non-admin roles.

### XIII.2 Real-Time Delivery Contract

```
WebSocket Events → Flutter:
  path.ready        → Animate path widget appearance on dashboard
  path.updated      → Refresh DAG visualization (mutation applied)
  node.completed    → Animate node to completed state + celebration micro-interaction
  node.failed       → Animate node to failed state + remediation node appears
  belt_gate.unlocked → Full-screen belt advancement celebration + notify parent
  path.stale        → Banner: "Your path needs attention — last activity X days ago"

Push Notifications (FCM):
  Path construction complete      → "Your personalized learning path is ready"
  PRIORITY_CRITICAL node inserted → "[Skill] is now critical based on recruiter demand"
  Belt gate unlocked              → "You've advanced to [Belt Level]! 🥋"
  Path stale (D7)                → "Your learning path is waiting — pick up where you left off"
  Mentor confirmation needed      → "[User] is ready for belt assessment" (to mentor)
```

---

## SECTION XIV — SECURITY BASELINE (LOCKED)

```
SEC-01  All API calls authenticated via Keycloak-issued JWT
SEC-02  Tenant isolation enforced at PostgreSQL RLS (row-level security)
SEC-03  UserPathLock prevents concurrent path construction race conditions
SEC-04  BELT_GATE completion requires MENTOR JWT (SYSTEM-only completion forbidden)
SEC-05  Admin intelligence weight override requires MFA re-authentication
SEC-06  All PathGraph and PathNode data encrypted at rest (AES-256)
SEC-07  PathAuditLog hash chain verified on every read by Compliance Service
SEC-08  ALPA has no write access to: User Profile, Scoring tables, Belt Engine state
         ALPA only reads from external services and writes to its own path schema
SEC-09  Kong rate limit: 60 req/min/user on path endpoints
SEC-10  etcd locking uses short-lived tokens (TTL 30s) — no persistent lock possible
SEC-11  Wazuh monitors: GATE-5 violations (cross-tenant), hash chain breaks,
         unusual mutation frequency (> 20 mutations/user/day → anomaly alert),
         belt gate mentor confirmation from unauthorized mentors
SEC-12  Parent access token scoped to read-only on child path summary only
         Parent cannot see: signal values, SGS scores, mutation raw diffs
```

Any SEC rule bypass → SECURITY BREACH → STOP EXECUTION → ALERT WAZUH → ESCALATE

---

## SECTION XV — FAILURE HANDLING (DETERMINISTIC)

| Failure Scenario | System Action |
|-----------------|--------------|
| ASRA PSIG-01 unavailable | PATH_CONSTRUCTION_BLOCKED — queue retry × 3 (60s intervals), alert |
| Redis signal cache miss | Fall back to PostgreSQL SignalCacheManifest |
| UserPathLock timeout (> 30s) | PATH_LOCK_TIMEOUT — fail gracefully, user notified, retry queued |
| Trust Gate GATE-5 violation | SECURITY_HALT — construction aborted, Wazuh alert raised |
| DAG cycle detected (GATE-7) | Path aborted, PATH_INVALID logged, reconstruction queued |
| Mentor 7-day non-response | Belt gate escalation chain: D7 re-notification, D14 Admin escalation |
| Mutation produces path_confidence < 0.60 | MUTATION_ABORTED — previous path retained, mutation logged as aborted |
| Kafka event delivery failure | Local retry buffer, exponential backoff, dead letter queue |
| PostgreSQL write failure | Retry × 3, then dead letter queue + alert |
| Content node no longer active (GATE-2) | Node removed from path, replacement node insertion attempted via MT-XX |
| Intelligence Profile Service down | Use last known PSIG-02 vector, flag PSIG-02_STALE in path metadata |
| Belt Engine unreachable | BELT_GATE node status frozen at current state, retry on reconnect |
| WebSocket channel down | Path updates queued in Redis, delivered on reconnect |
| etcd unreachable | Fall back to Redis-only locking, log LOCKING_DEGRADED warning |

No silent failures. All failures produce structured log with `error_code`, `affected_path_id`, `affected_user_id`.

---

## SECTION XVI — DEPLOYMENT SEQUENCE (INTERN-EXECUTABLE)

```
STEP 1  Apply DB migrations (Flyway)
        Tables: PathGraph, PathNode, PathEdge, PathMutationRecord,
                PathAuditLog, IntelligenceNodeTypeWeight,
                PathCompletionRegistry, UserPathLock
        Verify: flyway info → all migrations = SUCCESS

STEP 2  Seed IntelligenceNodeTypeWeight matrix
        Load 8 intelligence types × 9 node types = 72 default weight rows
        Per domain (5 domains) = 360 seed rows
        Verify: SELECT COUNT(*) FROM IntelligenceNodeTypeWeight → 360

STEP 3  Verify ASRA dependency
        GET /asra/recommendations/{test_user_id} → must return ≥ 1 active record
        If ASRA not ready → STOP, resolve ASRA first

STEP 4  Deploy alpa-service to ecoskiller-intelligence namespace
        kubectl apply -f infra/k8s/[env]/alpa/
        kubectl get pods -n ecoskiller-intelligence → alpa pods = Running

STEP 5  Register Kafka topics (8 produced + 7 consumed per Sections X.1, X.2)
        Verify: kafka-topics.sh --list → all topics present

STEP 6  Register Kong routes
        /alpa/** → alpa-service ClusterIP
        Rate limit: 60 req/min/user

STEP 7  Seed belt gate configuration in ConfigMap
        Match Belt Engine config: match counts, score thresholds, cooldown_days

STEP 8  Deploy Grafana dashboard
        Import: /infra/grafana/alpa-dashboard.json
        Verify: all 8 panels visible and returning data

STEP 9  Smoke test
        POST /alpa/path/construct { user_id: [test_user], domain: Technology, force_reconstruct: false }
        Poll: GET /alpa/path/[test_user]/status → status = ACTIVE
        Verify: GET /alpa/path/[test_user] → PathNode[] count ≥ 3
        Verify: path_narrative references ≥ 2 signal classes
        Verify: pts_score present and ≥ 0.60

STEP 10 Verify Audit Log
        SELECT * FROM PathAuditLog WHERE user_id = [test_user]
        Expected: PATH_CREATED event present, immutable_hash populated, chain valid

STEP 11 Verify Belt Gate (if test user has active belt)
        GET /alpa/path/[test_user]/belt-gate → BELT_GATE node present
        Verify: completion_verifier = MENTOR

STEP 12 Verify Mutation Trigger (integration test)
        Simulate match.scored Kafka event with accuracy = 0.45 for test_user
        Poll: GET /alpa/path/[test_user]/mutations → mutation with trigger_id = MT-01 present
```

Any step failure → STOP EXECUTION → REPORT DEPLOYMENT_STEP_[N]_FAILURE

---

## SECTION XVII — PRODUCTION READINESS GATE (FINAL CHECKLIST)

```
✅ All 12 DB tables migrated and verified
✅ IntelligenceNodeTypeWeight matrix seeded (360 rows)
✅ ASRA dependency verified and returning recommendations
✅ Belt Engine config synchronized to ALPA ConfigMap
✅ All 10 path signal classes wired and ingesting
✅ Path construction pipeline tested end-to-end
✅ DAG assembly validated (no cycles detected in test paths)
✅ All 9 Trust Gates active, tested, and logging
✅ BELT_GATE mentor requirement enforced (SYSTEM-only rejected in test)
✅ All 12 mutation triggers wired to Kafka consumers
✅ 3 mutation scenarios tested: MT-01 (match fail), MT-03 (belt unlock), MT-05 (ASRA update)
✅ Immutable PathAuditLog operational and hash-chain verified
✅ PathMutationRecord before/after diff verified
✅ All 14 API endpoints registered and tested
✅ Kafka: 8 produced topics confirmed, 7 consumed topics confirmed
✅ Redis UserPathLock tested (concurrent construction correctly serialized)
✅ etcd locking operational (fallback to Redis tested)
✅ Flutter screens (5 mandatory) deployed and rendering PathGraph
✅ DAG visualization tested with parallel streams + sequential nodes
✅ WebSocket real-time update tested (path.updated, node.completed)
✅ Belt gate celebration UI tested
✅ Push notification for PRIORITY_CRITICAL node insertion tested
✅ Parent summary endpoint tested (signal data confirmed NOT exposed)
✅ Prometheus metrics collecting all 10 defined metrics
✅ Grafana dashboard: all 8 panels visible
✅ Wazuh alert rules active: GATE-5, hash chain, mutation anomaly, unauthorized mentor
✅ Kong rate limits enforced (60 req/min/user verified)
✅ Tenant isolation verified (cross-tenant path construction rejected)
✅ Domain isolation verified (cross-domain nodes rejected without DomainAccessGrant)
✅ Path narrative verified (min 2 signal citations per narrative)
✅ PTS score visible on all path cards
✅ PVI computation verified over 30-day window
✅ pvi.computed events reaching Intelligence Evolution Timeline Service
✅ Path rollback from PathMutationRecord.before_snapshot tested
```

**Any unchecked item → PRODUCTION GATE FAILS → NO LAUNCH PERMITTED**

---

## SECTION XVIII — INTEGRATION WITH ASRA (ANTIGRAVITY COUPLING)

ALPA and ASRA are sibling agents. Their integration is the core of the ANTIGRAVITY engine.

```
INTEGRATION FLOW:

ASRA produces: recommendation.generated event (Kafka)
  → ALPA consumes: triggers MT-05 (new skill evaluation for path)
  → If SGS delta > 0.15 for recommended skill vs current path root:
    → New path node inserted OR new path constructed for the skill
  → Result: recommendation is not just surfaced — it becomes a concrete path step

ALPA produces: path.completed event
  → ASRA consumes: removes completed skill from recommendation pool
  → ASRA re-ranks remaining skills → new recommendation.generated
  → Result: continuous self-reinforcing ANTIGRAVITY loop

ALPA produces: pvi.computed event
  → Intelligence Evolution Timeline Service updates longitudinal graph
  → Passive Intelligence Engine updates user's intelligence score
  → ASRA re-reads updated PSIG-02 → re-weights recommendations
  → Result: faster learners (high PVI) receive harder, more advanced paths

SHARED TRUST LAYER:
  ASRA Trust Gates + ALPA Trust Gates share:
    - Domain isolation logic (DomainAccessGrant table)
    - Tenant isolation enforcement (PostgreSQL RLS)
    - Parent consent flow (student age < 18)
    - Immutable audit log schema (different tables, same hash-chain design)
```

---

## SECTION XIX — FEEDBACK LOOP & MODEL IMPROVEMENT

### XIX.1 Feedback Signal Collection

```
Signal: Path completed within 80% of estimated timeline
  → Node time estimates adjusted (confidence_calibration event)

Signal: Path abandoned (> 30 days stale, no revival)
  → Node type mix for that intelligence profile flagged for review

Signal: BELT_GATE failed repeatedly (> 2 fails)
  → Insert additional DOJO_SCENARIO preparatory nodes before next attempt

Signal: PVI consistently ≥ 0.85 for user
  → Trigger path acceleration: deadline_days compressed 20%

Signal: Mutation MT-01 triggered > 3 times on same skill
  → Skill flagged as "high difficulty cluster" → mentor notification
```

### XIX.2 Improvement Cycle (Non-Real-Time)

```
Trigger         : Apache Airflow scheduled job (bi-weekly)
Input           : PathCompletionRegistry + PathMutationRecord (rolling 90 days)
Output          : IntelligenceNodeTypeWeight recalibration report
Review Required : TENANT_ADMIN human approval before applying
Auto-apply      : FORBIDDEN
Audit on apply  : MANDATORY
```

---

## SECTION XX — ANTIGRAVITY CONTRIBUTION INDEX (ALPA-ACI)

ALPA's primary KPI contribution to the platform-level ANTIGRAVITY INDEX.

```
ALPA-ACI = (
  (Path Completion Rate 30-day) × 0.35 +
  (PVI ≥ 0.50 User Rate) × 0.30 +
  (Belt Advancement Rate per quarter) × 0.20 +
  (Mutation Relevance Rate: accepted mutations / total mutations) × 0.15
)

Target ALPA-ACI at launch  : ≥ 0.60
Target ALPA-ACI at 90 days : ≥ 0.72
Critical threshold          : ALPA-ACI < 0.45 → ALPA PERFORMANCE REVIEW TRIGGERED

ALPA-ACI feeds the platform-level ANTIGRAVITY INDEX alongside ASRA's AGI score.
Combined ANTIGRAVITY INDEX = (AGI × 0.50) + (ALPA-ACI × 0.50)
```

ALPA-ACI is computed weekly by Analytics Service and published to the Grafana ANTIGRAVITY Effectiveness dashboard.

---

## SECTION XXI — VERSIONING & MUTATION POLICY

```
Current Version        : v1.0
Mutation Policy        : ADD-ONLY
Allowed Changes        : New mutation triggers (MT-13+), new path node types,
                         new trust gates, new API endpoints, new signal classes
Forbidden Changes      : Modifying DAG construction algorithm core logic
                         Removing existing trust gates
                         Changing existing API response schemas
                         Altering audit log or mutation record schemas
                         Removing BELT_GATE mentor requirement (LAW-03, RULE-BG-2)
                         Enabling auto-promotion (RULE-BG-3 absolute)
Version Bump Required  : Any change to Section III, IV, V, VIII, IX
Approval Required      : PLATFORM_OWNER human declaration
Auto-apply             : FORBIDDEN
```

---

## APPENDIX A — INTELLIGENCE TYPE → NODE TYPE WEIGHT MATRIX (DEFAULT)

| Intelligence Type | COURSE | DOJO_MATCH | DOJO_SCENARIO | GD_ROUND | CERT_EXAM | MENTORED | PEER_QUIZ | PROJECT | BELT_GATE |
|------------------|--------|-----------|--------------|---------|----------|---------|---------|---------|----------|
| Logical-Math | 0.10 | 0.20 | 0.20 | 0.05 | 0.15 | 0.05 | 0.10 | 0.10 | 0.05 |
| Linguistic | 0.15 | 0.05 | 0.05 | 0.25 | 0.10 | 0.15 | 0.10 | 0.10 | 0.05 |
| Spatial | 0.10 | 0.10 | 0.15 | 0.05 | 0.10 | 0.10 | 0.05 | 0.25 | 0.10 |
| Musical | 0.20 | 0.05 | 0.05 | 0.15 | 0.10 | 0.20 | 0.10 | 0.10 | 0.05 |
| Bodily-Kinesthetic | 0.05 | 0.15 | 0.25 | 0.05 | 0.05 | 0.10 | 0.05 | 0.25 | 0.05 |
| Interpersonal | 0.10 | 0.10 | 0.05 | 0.20 | 0.05 | 0.25 | 0.15 | 0.05 | 0.05 |
| Intrapersonal | 0.20 | 0.10 | 0.10 | 0.05 | 0.15 | 0.15 | 0.10 | 0.10 | 0.05 |
| Naturalist | 0.15 | 0.05 | 0.10 | 0.10 | 0.15 | 0.10 | 0.10 | 0.20 | 0.05 |

Row sums = 1.0 per intelligence type. Deviation → STOP EXECUTION.
Tenant override: ±10% per cell, subject to row sum = 1.0 constraint.

---

## FINAL SYSTEM DEFENSE

> **"ALPA does not assign learning. It engineers trajectories — personalized, adaptive, trust-gated, and immutably audited. Every path is a force vector computed from intelligence truth, belt state, behavioral compliance, and market reality. Every mutation is a course correction triggered by verified events — never by guesswork. Every belt gate requires human confirmation. Every step is explainable. This is ANTIGRAVITY made operational."**

---

```
Document Status     : SEALED
Version             : v1.0
Module              : ADAPTIVE_LEARNING_PATH_AGENT (ALPA)
Platform            : ECOSKILLER
Layer               : ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
Module Alias        : ANTIGRAVITY PATH ENGINE
Sibling Module      : ASRA (AI_BASED_SKILL_RECOMMENDATION_AGENT)
Sealed By           : ECOSKILLER ARCHITECTURE AUTHORITY
Last Locked         : 2026-03-04
Next Revision Gate  : v2.0 (ADD-ONLY, HUMAN APPROVAL REQUIRED)
```

---
*End of sealed document. No section may be interpreted, abbreviated, or partially implemented.*
*Partial implementation = NO IMPLEMENTATION. STOP EXECUTION.*
