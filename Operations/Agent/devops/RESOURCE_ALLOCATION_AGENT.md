# RESOURCE_ALLOCATION_AGENT
## Ecoskiller · Unified Platform · Program & Event Management Layer
### Status: FINAL · LOCKED · SEALED · ANTIGRAVITY-ENFORCED
### Artifact Class: Production Agent Prompt — Deterministic Execution
### Mutation Policy: Add-only via version bump · Interpretation Authority: NONE
### Execution Engine: ANTIGRAVITY
### Version: 1.0 · March 2026 · Internal Use Only

---

## ⚠️ ANTIGRAVITY OPERATOR SEAL

```
AGENT_NAME                    = RESOURCE_ALLOCATION_AGENT
EXECUTION_ENGINE              = ANTIGRAVITY
PLATFORM                      = ECOSKILLER · UNIFIED PLATFORM
DOMAIN                        = Program & Event Management — Resource Allocation Layer
SUB_DOMAINS_SERVED            = Dojo SaaS · Society SaaS · Core Ecoskiller
STACK_LOCK                    = Flutter (operational) + React/Next.js (SEO/public)
NAMESPACES_SERVED             = core · realtime · society · ops
MUTATION_POLICY               = ADD-ONLY VIA VERSION BUMP
INTERPRETATION                = FORBIDDEN
ARCHITECTURE_AUTHORITY        = LOCKED — HUMAN DECLARATION ONLY
ANTIGRAVITY_CONFUSION         = IMPOSSIBLE
READY_FOR                     = ANTIGRAVITY_PRODUCTION
ANTIGRAVITY_SAFE              = TRUE
RESOURCE_POOL_ISOLATION       = MANDATORY — DOJO NEVER SHARES WITH CORE
DOMAIN_ISOLATION              = ARTS · COMMERCE · SCIENCE POOLS NEVER MERGED
PARTIAL_ISOLATION             = FORBIDDEN
UNLIMITED_SHARED_RESOURCES    = FORBIDDEN
OVERBOOKING                   = FORBIDDEN — HARD LOCK VIA etcd + Redis
DOUBLE_ASSIGNMENT             = FORBIDDEN — DISTRIBUTED MUTEX ENFORCED
```

> Antigravity MUST NOT assume shared resource pools across Dojo and Ecoskiller Core.
> Antigravity MUST NOT merge Arts, Commerce, or Science domain isolation pools.
> Antigravity MUST NOT infer tenancy boundaries or generate unlimited shared resources.
> Antigravity MUST NOT remove isolation for cost savings or simplification.
> Antigravity MUST treat every STOP signal as a hard execution halt — zero partial output.
> Antigravity MUST enforce distributed locking on every resource assignment operation.
> Antigravity MUST generate UI strictly within declared screen boundaries in Section 17.
> Every resource type declared in Section 3 is immutable. New types require a version bump.

---

## SECTION 1 — AGENT IDENTITY & SCOPE

### 1.1 Agent Purpose

The Resource Allocation Agent is the single authoritative service governing how every scarce, bookable, or assignable resource across the Ecoskiller unified platform is:

- Registered into a canonical resource registry with capacity, type, and ownership metadata
- Reserved atomically against concurrent demand using distributed locking (etcd + Redis)
- Allocated to a specific program event with idempotency enforcement
- Monitored for utilisation, saturation, and conflict in real time
- Released upon event completion, cancellation, or timeout
- Governed for fair access across tenants, domains, and user types
- Reported to analytics for capacity planning and demand forecasting

This agent does NOT own event creation, scoring, billing, or content delivery.
It owns exactly one thing: the lifecycle of resource reservations across all program types.

### 1.2 Resource Domain Coverage

```
DOMAIN: VIRTUAL RESOURCES (COMPUTE & MEDIA INFRASTRUCTURE)
  Jitsi GD rooms           — self-hosted Jitsi; per-batch voice rooms for GD sessions
  LiveKit match rooms      — SFU rooms for Dojo matches, interviews, mentor sessions
  WebSocket session slots  — realtime command channels per active event
  Redis state machine slots — GD orchestration state instances (per batch)

DOMAIN: HUMAN RESOURCES (PERSONNEL POOLS)
  Coach pool               — society-registered coaches; assigned to workshop batches
  Coordinator pool         — society coordinators; assigned to exposition/tournament events
  Judge pool               — accredited judges; assigned to exposition + tournament scoring
  Mentor pool              — Dojo-registered mentors; assigned to sessions + belt calibrations
  GD batch examiner pool   — voice GD batch assignment (recruiter-less, system-assigned)

DOMAIN: PHYSICAL RESOURCES (SOCIETY LAYER)
  Workshop venues          — physical spaces for society skill batches
  Exposition halls         — venues for craft fairs, innovation expos, skill showcases
  Tournament arenas        — physical/hybrid spaces for society tournaments
  Equipment sets           — projectors, QR scanners, sound systems (per event type)
  Stall positions          — numbered positions within exposition floor layouts

DOMAIN: LOGICAL RESOURCES (PLATFORM GOVERNANCE)
  Interview time slots     — locked calendar windows in Interview Service
  Dojo match slots         — rated + unrated match queue positions
  Tournament bracket slots — single/double elimination bracket node assignments
  Workshop batch seats     — per-batch enrollment capacity (with waitlist)
  Mentor booking slots     — per-mentor time windows; fee-gated
  Scheme batch quotas      — PMKVY/DDU maximum enrolment ceilings per government batch
```

### 1.3 What This Agent Does NOT Own

```
DOES NOT OWN:
  Event creation logic          — owned by source services
  Scoring or evaluation         — owned by Scoring Engine
  Billing or fee collection     — owned by Billing Service
  Notification delivery         — owned by Notification Service
  Session content or curriculum — owned by Workshop / Session Services
  Identity or auth              — owned by Keycloak / Auth Service
  Room media transport          — owned by LiveKit / Jitsi (this agent issues tokens only)
  Calendar display              — owned by Event Calendar Sync Agent
```

### 1.4 Agent Position in System Architecture

```
Ecoskiller Unified Platform
  └── Namespaces: core · realtime · society · ops
        └── RESOURCE_ALLOCATION_AGENT
              ├── resource-registry-service     (owned — canonical resource catalogue)
              ├── allocation-engine             (owned — reservation + assignment + release)
              ├── conflict-detector             (owned — overbooking + double-assign prevention)
              ├── capacity-planner              (owned — utilisation analytics, demand forecast)
              ├── etcd                          (distributed locking — strong consistency)
              ├── Redis                         (fast lock state + quota counters)
              ├── PostgreSQL                    (canonical allocation records + audit log)
              ├── ClickHouse                    (utilisation analytics + capacity time-series)
              ├── Kafka                         (event bus — event lifecycle consumer + publisher)
              ├── Temporal                      (durable allocation workflows for complex flows)
              └── Notification Service          (consumer — conflict + release alerts)
```

---

## SECTION 2 — SYSTEM IDENTITY LOCK

### 2.1 Naming Conventions (Immutable)

Primary entities cannot be renamed. Fields may extend — not mutate.

```
Resource             — a single bookable unit (room, slot, venue, person, seat, equipment)
ResourceType         — classification from the registry in Section 3
ResourcePool         — a named collection of Resources of the same type within a scope
ResourceQuota        — maximum concurrent allocations permitted per pool per scope
ResourceReservation  — a time-locked hold on a Resource prior to full allocation (TTL-gated)
ResourceAllocation   — a confirmed assignment of a Resource to a specific program event
AllocationConflict   — detected clash between two ResourceAllocations on the same Resource
ReleaseRecord        — immutable record of resource being freed (event completed/cancelled)
CapacitySnapshot     — point-in-time utilisation metric per pool (ClickHouse)
AllocationAuditLog   — immutable log of every reservation, allocation, conflict, release
WaitlistEntry        — a queued demand request when ResourcePool is at capacity
```

### 2.2 Roles Served (Keycloak RBAC — Immutable)

```
SOCIETY_ADMIN        — manages physical resource pools (venues, equipment, stall positions)
MASTER_ORGANIZER     — cross-society resource governance; override authority
COORDINATOR          — views resource allocation for assigned events; marks equipment handover
COACH                — views own batch assignment; flags resource issues
JUDGE                — views own assignment schedule; no resource management access
FRANCHISE_OWNER      — reads resource utilisation reports for owned society units
PLATFORM_SUPER_ADMIN — full resource registry access; quota override; conflict resolution
SYSTEM               — automated allocation operations (Kafka consumers, Airflow DAGs)
```

---

## SECTION 3 — CANONICAL RESOURCE TYPE REGISTRY (LOCKED)

All resource types are declared here. No unlisted type may be allocated without a version bump.

### 3.1 Virtual Resource Types

| resource_type_key       | Owner Service             | Isolation Domain         | Lock Mechanism     | Max Concurrent (default) |
|-------------------------|---------------------------|--------------------------|--------------------|--------------------------|
| JITSI_GD_ROOM           | Voice GD Orchestrator     | Per-batch; Dojo-isolated | Redis distributed  | Platform config          |
| LIVEKIT_MATCH_ROOM      | Dojo Match Engine         | Dojo-isolated; per-match | Redis distributed  | Platform config          |
| LIVEKIT_INTERVIEW_ROOM  | Interview Service         | Core-isolated            | Redis distributed  | Platform config          |
| LIVEKIT_MENTOR_ROOM     | Interview/Session Service | Core-isolated            | Redis distributed  | Per-mentor: 1            |
| LIVEKIT_LIVE_CLASS_ROOM | Session Service           | Core-isolated            | Redis distributed  | Platform config          |
| WEBSOCKET_SESSION_SLOT  | Realtime namespace        | Per-event                | Redis counter      | Platform config          |
| REDIS_GD_STATE_MACHINE  | Voice GD Orchestrator     | Per-batch; Dojo-isolated | etcd mutex         | 1 per GD batch           |

### 3.2 Human Resource Types

| resource_type_key      | Owner Pool          | Scope              | Lock Mechanism    | Conflict Rule                          |
|------------------------|---------------------|--------------------|-------------------|----------------------------------------|
| COACH                  | coach-service pool  | Society            | Redis mutex       | 1 coach per batch per time window      |
| COORDINATOR            | coordinator-service | Society            | Redis mutex       | 1 coordinator per event per time window|
| JUDGE                  | Judge pool (expo/tournament) | Society   | Redis mutex       | 1 judge per N projects; declared limit |
| MENTOR                 | Dojo mentor pool    | Tenant             | Redis mutex       | 1 mentor per session slot              |
| GD_EXAMINER            | System-assigned     | Core/Dojo          | SYSTEM only       | Auto-assigned; no human selection      |

### 3.3 Physical Resource Types

| resource_type_key     | Owner Service                   | Scope   | Lock Mechanism | Conflict Rule                           |
|-----------------------|---------------------------------|---------|----------------|-----------------------------------------|
| WORKSHOP_VENUE        | workshop-service (society)      | Society | etcd mutex     | 1 venue per batch per time window       |
| EXPOSITION_HALL       | expo-service (society)          | Society | etcd mutex     | 1 hall per exposition                   |
| TOURNAMENT_ARENA      | tournament-service (society)    | Society | etcd mutex     | 1 arena per tournament per round        |
| EQUIPMENT_SET         | expo/workshop-service           | Society | Redis counter  | Max sets per event declared at creation |
| STALL_POSITION        | expo-service (society)          | Society | Redis mutex    | 1 position per stall registration       |

### 3.4 Logical Resource Types

| resource_type_key       | Owner Service          | Scope       | Lock Mechanism     | Conflict Rule                              |
|-------------------------|------------------------|-------------|--------------------|--------------------------------------------|
| INTERVIEW_SLOT          | Interview Service      | Core        | Redis mutex        | 1 slot per recruiter per candidate pair    |
| DOJO_MATCH_SLOT         | Dojo Match Engine      | Dojo        | Redis mutex        | 1 slot per match_id                        |
| TOURNAMENT_BRACKET_SLOT | Tournament Mgmt Agent  | Dojo/Society| etcd mutex         | 1 bracket node per round per match         |
| WORKSHOP_BATCH_SEAT     | workshop-service       | Society     | Redis counter      | Count ≤ batch.max_participants             |
| MENTOR_BOOKING_SLOT     | Interview Service      | Core        | Redis mutex        | 1 slot per mentor per time window          |
| SCHEME_BATCH_QUOTA      | scheme-service         | Society     | etcd mutex         | Count ≤ government-declared ceiling        |

---

## SECTION 4 — RESOURCE ISOLATION GOVERNANCE (NON-NEGOTIABLE)

### 4.1 Domain Isolation (Locked — Cannot Be Merged)

```
DOJO RESOURCE POOL:
  Fully isolated from Ecoskiller Core resource pools
  Independent compute, memory, network limits (k3s namespace: realtime)
  Per-room and per-group quotas enforced
  A Dojo overload MUST NEVER degrade Ecoskiller Core services
  Dojo pools MUST NOT share execution with Ecoskiller Core

ARTS DOMAIN POOL:
  Soft isolation permitted
  Resource bursting allowed within declared caps
  Graceful contention handling acceptable

COMMERCE DOMAIN POOL:
  Strong isolation mandatory
  No shared mutable resources
  Predictable capacity guarantees required

SCIENCE DOMAIN POOL:
  Deterministic isolation required
  No noisy-neighbor tolerance
  Reproducible resource envelopes mandatory

SOCIETY POOL:
  Isolated from Core and Dojo
  CouchDB sync layer does not affect Core databases
  Physical resource tracking is Society-only
  No Society resource type may be allocated to Core or Dojo events
```

### 4.2 Tenant Isolation (Mandatory)

```
RULES:
  Each tenant has isolated resource quotas
  Cross-tenant resource sharing is FORBIDDEN
  One tenant's demand spike MUST NOT affect another tenant's allocations
  Resource pool namespace = {resource_type}:{tenant_id}:{scope_id}
  PLATFORM_SUPER_ADMIN may view cross-tenant utilisation for governance only
  No allocation record is visible across tenant boundaries
```

### 4.3 User-Type Priority Lanes (Immutable)

```
PRIORITY_LANE_1 (highest): SYSTEM-critical operations (Kafka consumers, state machines)
PRIORITY_LANE_2:           ADMIN operations (PLATFORM_SUPER_ADMIN, MASTER_ORGANIZER)
PRIORITY_LANE_3:           GOVERNMENT-scheme batches (PMKVY/DDU ceiling enforcement)
PRIORITY_LANE_4:           MENTORS + EDUCATORS (stable session resources guaranteed)
PRIORITY_LANE_5:           MINORS (conservative, sandboxed resource limits; child safety)
PRIORITY_LANE_6:           STUDENTS / CANDIDATES (protected from noisy peers)
PRIORITY_LANE_7:           INFORMATIONAL / ANALYTICS (lowest priority; never preempts above)

Priority lane determines queue position when ResourcePool is at capacity.
Priority NEVER compromises isolation strength.
```

---

## SECTION 5 — RESOURCE LIFECYCLE STATE MACHINE

### 5.1 States (Deterministic — Immutable)

```
AVAILABLE
  → RESERVED           (time-locked hold; TTL-gated; pending payment or confirmation)
      → ALLOCATED       (confirmed assignment; event actively using resource)
          → IN_USE      (event has started; resource actively consumed)
              → RELEASED (event completed, cancelled, or timed out; resource freed)
      → EXPIRED         (reservation TTL elapsed without confirmation → back to AVAILABLE)
      → RESERVATION_FAILED (concurrent lock conflict; reservation rolled back → AVAILABLE)
  → BLOCKED             (resource taken offline: maintenance, fault, admin action)
      → AVAILABLE       (admin un-blocks resource)
  → DECOMMISSIONED      (resource permanently retired from pool)
```

### 5.2 State Transition Rules

| From State          | To State             | Trigger                                            | Authority         |
|---------------------|----------------------|----------------------------------------------------|-------------------|
| AVAILABLE           | RESERVED             | Reservation request + distributed lock acquired    | SYSTEM / Admin    |
| RESERVED            | ALLOCATED            | Source service confirms event; payment cleared     | SYSTEM            |
| RESERVED            | EXPIRED              | TTL elapsed without confirmation                   | SYSTEM (auto)     |
| RESERVED            | RESERVATION_FAILED   | Lock conflict (race condition detected)            | SYSTEM (auto)     |
| ALLOCATED           | IN_USE               | Event state machine reaches LIVE/ACTIVE            | SYSTEM            |
| IN_USE              | RELEASED             | Event COMPLETED, CANCELLED, or timed out           | SYSTEM            |
| ALLOCATED           | RELEASED             | Event cancelled before going LIVE                  | SYSTEM            |
| AVAILABLE           | BLOCKED              | Admin maintenance action                           | PLATFORM_SUPER_ADMIN / SOCIETY_ADMIN |
| BLOCKED             | AVAILABLE            | Admin clearance action                             | PLATFORM_SUPER_ADMIN / SOCIETY_ADMIN |
| RELEASED            | AVAILABLE            | Release processing complete; pool counter updated  | SYSTEM (auto)     |
| ANY                 | DECOMMISSIONED       | Admin permanent retirement action                  | PLATFORM_SUPER_ADMIN |

No state may be skipped.
SYSTEM-triggered transitions are automated and require no human approval.
Every transition produces an immutable AllocationAuditLog entry.

---

## SECTION 6 — DISTRIBUTED LOCKING ENGINE (NON-NEGOTIABLE)

### 6.1 Lock Technology Stack

```
PRIMARY LOCK:    etcd (strong-consistency distributed locking)
                 Used for: physical resources, scheme quotas, tournament bracket slots
                 Lock key format: resource:{resource_type}:{resource_id}:{tenant_id}
                 TTL: per resource type (see Section 6.3)
                 Consistency: linearizable (etcd guarantees)

FAST LOCK:       Redis (high-throughput distributed locks for virtual resources)
                 Used for: Jitsi rooms, LiveKit rooms, mentor slots, interview slots
                 Lock key format: lock:{resource_type}:{resource_id}:{event_id}
                 TTL: per resource type (see Section 6.3)
                 Algorithm: Redlock-compatible with single Redis cluster

COUNTER LOCK:    Redis atomic counters (INCR/DECR)
                 Used for: workshop batch seats, equipment sets, WebSocket slots
                 Key format: quota:{resource_type}:{pool_id}:{tenant_id}
                 Overflow protection: INCR checked against ResourceQuota.max_concurrent
```

### 6.2 Distributed Mutex Rules (Non-Negotiable)

```
ACQUISITION:
  1. Attempt lock acquisition with TTL
  2. If acquired → proceed with reservation
  3. If NOT acquired (lock held by another) → return RESERVATION_CONFLICT (409)
     No retry at application level — caller handles conflict
     No silent retry loops permitted

IDEMPOTENCY:
  Every reservation request carries idempotency_key = {event_id}:{resource_type}:{requester_id}
  Duplicate reservation with same idempotency_key → return existing reservation (no re-lock)
  Idempotency keys stored in Redis (TTL = 24 hours)

LOCK RELEASE:
  Lock released immediately on: ALLOCATED confirmed, EXPIRED, RESERVATION_FAILED, RELEASED
  Lock NEVER held beyond event.end_datetime_utc + 30 minutes (hard ceiling)
  Stale lock detection: Airflow DAG scans for locks > TTL ceiling → force-release + AuditLog

DOUBLE ASSIGNMENT PREVENTION:
  Before any allocation write to PostgreSQL:
    CHECK: resource_id NOT in active allocations for overlapping time window
    CHECK: etcd/Redis lock held by this transaction
  If either check fails → ROLLBACK + ALLOCATION_CONFLICT AuditLog entry
  No partial writes permitted
```

### 6.3 Lock TTL Definitions Per Resource Type

| resource_type_key       | Reservation TTL | Allocation Max Duration      | Notes                             |
|-------------------------|-----------------|------------------------------|-----------------------------------|
| JITSI_GD_ROOM           | 15 minutes      | GD session duration + 30 min | Auto-released on gd.completed     |
| LIVEKIT_MATCH_ROOM      | 10 minutes      | Match duration + 15 min      | Auto-released on match.completed  |
| LIVEKIT_INTERVIEW_ROOM  | 30 minutes      | Interview duration + 30 min  | Auto-released on interview.completed |
| LIVEKIT_MENTOR_ROOM     | 15 minutes      | Session duration + 15 min    | Auto-released on session.completed |
| LIVEKIT_LIVE_CLASS_ROOM | 30 minutes      | Session duration + 30 min    | Auto-released on session.completed |
| COACH                   | 48 hours        | Batch duration               | Released on batch.completed       |
| COORDINATOR             | 48 hours        | Event duration               | Released on event.archived        |
| JUDGE                   | 72 hours        | Event judging window         | Released on judging.closed        |
| MENTOR                  | 30 minutes      | Booking slot duration        | Released on session.completed     |
| WORKSHOP_VENUE          | 72 hours        | Batch duration               | etcd; released on batch.completed |
| EXPOSITION_HALL         | 7 days          | Exposition duration          | etcd; released on exposition.archived |
| TOURNAMENT_ARENA        | 48 hours        | Tournament + 4 hours         | etcd; released on tournament.archived |
| STALL_POSITION          | 30 minutes      | Exposition duration          | Released on exposition.archived   |
| INTERVIEW_SLOT          | 30 minutes      | Interview duration           | Released on interview.completed   |
| MENTOR_BOOKING_SLOT     | 30 minutes      | Booking slot duration        | Released on session.completed     |
| SCHEME_BATCH_QUOTA      | 7 days          | Batch duration               | etcd; government ceiling enforced |
| WORKSHOP_BATCH_SEAT     | 30 minutes      | Batch duration               | Counter-based; released on completion |
| TOURNAMENT_BRACKET_SLOT | 24 hours        | Round duration               | etcd; released on round.completed |

---

## SECTION 7 — RESERVATION ENGINE

### 7.1 Reservation Request Flow (Deterministic)

```
STEP 1: Validate request
  → resource_type exists in registry (Section 3)
  → requester has RBAC permission for this resource_type
  → tenant_id and scope_id match request context
  → time window does not violate ResourceQuota.max_concurrent
  → idempotency_key not already active
  Failure at any step → REJECT with reason_code; no lock attempted

STEP 2: Check pool capacity
  → Query ResourcePool.current_utilisation against ResourceQuota.max_concurrent
  → If at capacity → queue WaitlistEntry; return WAITLISTED (202)
  → If capacity available → proceed to Step 3

STEP 3: Acquire distributed lock
  → Attempt etcd or Redis lock (per resource_type)
  → If lock acquired → proceed to Step 4
  → If lock not acquired (conflict) → return RESERVATION_CONFLICT (409)
  No wait; no queue at lock level; conflict is immediate

STEP 4: Double-assignment check
  → SELECT existing allocations where resource_id overlaps time window
  → If overlap found → release lock → return ALLOCATION_CONFLICT (409)
  → If no overlap → proceed to Step 5

STEP 5: Write ResourceReservation to PostgreSQL
  → status = RESERVED
  → reserved_until = NOW() + TTL
  → idempotency_key stored

STEP 6: Update Redis quota counter
  → INCR quota:{resource_type}:{pool_id}:{tenant_id}

STEP 7: Publish event
  → resource.reserved (Kafka) → notifies source service
  → AuditLog entry written

STEP 8: Return ResourceReservation to caller
  → reservation_id, resource_id, reserved_until
```

### 7.2 Reservation Confirmation Flow

```
TRIGGER: Source service confirms event (e.g., gd.scheduled, match.created, batch.started)
  → Load ResourceReservation by reservation_id
  → Verify status = RESERVED and reserved_until > NOW()
  → Update status → ALLOCATED
  → Publish resource.allocated (Kafka)
  → AuditLog entry
```

### 7.3 Reservation Expiry (Automatic)

```
Airflow DAG: reservation_expiry_dag (runs @every_5_minutes)
  T1: SELECT reservations WHERE status = RESERVED AND reserved_until < NOW()
  T2: For each expired reservation:
        → Release etcd/Redis lock
        → Update status → EXPIRED
        → DECR Redis quota counter
        → Publish resource.expired (Kafka)
        → Check WaitlistEntry → promote first waiter (if any)
        → AuditLog entry
```

---

## SECTION 8 — RESOURCE ALLOCATION ENGINE

### 8.1 Kafka Events Consumed

```
Topic: interview.events
  interview.scheduled    → reserve INTERVIEW_SLOT + LIVEKIT_INTERVIEW_ROOM
  interview.completed    → release INTERVIEW_SLOT + LIVEKIT_INTERVIEW_ROOM
  interview.cancelled    → release all held resources for interview_id

Topic: gd.events
  gd.scheduled           → reserve JITSI_GD_ROOM + REDIS_GD_STATE_MACHINE + WEBSOCKET_SESSION_SLOT
  gd.completed           → release all held resources for gd_batch_id
  gd.cancelled           → release all held resources for gd_batch_id

Topic: match.events
  match.scheduled        → reserve LIVEKIT_MATCH_ROOM + DOJO_MATCH_SLOT + WEBSOCKET_SESSION_SLOT
  match.completed        → release LIVEKIT_MATCH_ROOM + DOJO_MATCH_SLOT + WEBSOCKET_SESSION_SLOT
  match.cancelled        → release all held resources for match_id

Topic: tournament.events
  tournament.round.scheduled  → reserve TOURNAMENT_BRACKET_SLOT per match in round
  tournament.round.completed  → release TOURNAMENT_BRACKET_SLOT for completed round
  tournament.scheduled (society) → reserve TOURNAMENT_ARENA + JUDGE pool slots

Topic: session.events
  session.scheduled      → reserve LIVEKIT_MENTOR_ROOM or LIVEKIT_LIVE_CLASS_ROOM
  session.completed      → release held room resource
  session.cancelled      → release held room resource

Topic: exposition.events
  exposition.published   → reserve EXPOSITION_HALL
  exposition.stall.allocated → reserve STALL_POSITION
  exposition.archived    → release EXPOSITION_HALL + all STALL_POSITIONs + JUDGE slots

Topic: workshop.events (society)
  batch.started          → reserve WORKSHOP_VENUE + COACH + EQUIPMENT_SET + SCHEME_BATCH_QUOTA (if tagged)
  batch.completed        → release all held resources for batch_id
  batch.cancelled        → release all held resources for batch_id

Topic: certification.events
  certification.exam.scheduled → reserve LIVEKIT_INTERVIEW_ROOM or WORKSHOP_VENUE

Topic: billing.events
  PAYMENT_CONFIRMED      → unblock RESERVED resource awaiting payment gate
  PAYMENT_FAILED         → release RESERVED resource; notify waitlist
```

### 8.2 Kafka Events Published by This Agent

```
Topic: resource.events
  resource.reserved           { reservation_id, resource_type, resource_id, event_id, reserved_until }
  resource.allocated          { allocation_id, resource_type, resource_id, event_id, allocated_at }
  resource.released           { allocation_id, resource_type, resource_id, released_at, release_reason }
  resource.expired            { reservation_id, resource_type, resource_id, expired_at }
  resource.conflict.detected  { resource_type, resource_id, conflicting_event_ids[], detected_at }
  resource.pool.saturated     { pool_id, resource_type, utilisation_pct, tenant_id, detected_at }
  resource.pool.cleared       { pool_id, resource_type, utilisation_pct, tenant_id, cleared_at }
  resource.waitlist.promoted  { waitlist_entry_id, resource_type, resource_id, promoted_at }
  resource.blocked            { resource_id, resource_type, reason, blocked_by, blocked_at }
  resource.released_from_block { resource_id, resource_type, unblocked_by, unblocked_at }
```

---

## SECTION 9 — HUMAN RESOURCE POOL MANAGEMENT

### 9.1 Coach Pool (Society Layer)

```
POOL_SCOPE:       Per society_id
REGISTRATION:     coach-service handles coach onboarding + demo evaluation
                  This agent reads coach availability and assigns to batches
ASSIGNMENT_RULES:
  1 coach per workshop batch per time window (hard limit)
  Coach must be registered in the same society_id
  Coach trust_index must be ≥ declared threshold at assignment time
  Coach reputation_score tracked; performance_tracking via coach-service
  Assignment locked via Redis mutex for batch time window

ASSIGNMENT_FLOW:
  1. workshop-service requests coach assignment for batch_id
  2. allocation-engine queries available coaches in society pool
  3. Selects by: availability → trust_index → reputation_score (descending)
  4. Acquires Redis mutex on coach_id for batch time window
  5. Writes ResourceAllocation (type=COACH)
  6. Publishes resource.allocated
  7. Notifies coach via Notification Service

CONFLICT_RULE:
  Coach already allocated to overlapping batch → ALLOCATION_CONFLICT
  Coach on BLOCKED status (misconduct review) → not eligible for assignment
  assignment_locked = true while ALLOCATED; no reassignment without SOCIETY_ADMIN override + AuditLog
```

### 9.2 Judge Pool (Society Layer)

```
POOL_SCOPE:       Per society_id; per exposition_id or tournament_id
ASSIGNMENT_RULES:
  Minimum 2 judges per project category (exposition)
  Maximum projects per judge declared at event creation
  Conflict-of-interest declaration required before assignment
  Judge credentials verified in judge-service profile
  Judge cannot be assigned to event where related participant is enrolled

ASSIGNMENT_FLOW:
  1. expo-service or tournament-service requests judge assignment
  2. allocation-engine queries eligible judges in society pool
  3. Filters: no conflict-of-interest → credential match → availability
  4. Acquires Redis mutex on judge_id for judging time window
  5. Writes ResourceAllocation (type=JUDGE) per category assigned
  6. Publishes resource.allocated
  7. Notifies judge via Notification Service (ASSESSMENT class)

REALLOCATION:
  Allowed before JUDGING_ACTIVE state
  Requires SOCIETY_ADMIN authority + AuditLog entry
  Lock released for previous judge; new lock acquired for replacement
```

### 9.3 Mentor Pool (Dojo Layer)

```
POOL_SCOPE:       Per tenant_id; tenant mentor pools isolated (no cross-tenant sharing)
NO_SHARED_MENTOR_POOLS_WITHOUT_AUDIT (platform rule)
ASSIGNMENT_RULES:
  1 mentor per session slot (hard limit; Redis mutex on mentor_id + slot_time)
  Mentor availability declared by mentor in their schedule
  Billing gate: mentor booking requires payment confirmation before slot locked
  Mentor booking slot: 30-minute TTL reservation pending payment
  Revenue split: 80% mentor, 20% platform (Billing Service handles split)

SLOT_ABUSE_PREVENTION:
  A user cannot repeatedly join/leave mentor rooms to block slots
  Violation trigger: 3+ cancellations within 24h → auto cooldown (10–30 min) applied
  Cooldown enforced by allocation-engine; logged to AuditLog

MENTOR_CAPACITY_RULE:
  Mentor cannot be double-booked across overlapping sessions
  etcd lock prevents concurrent bookings on same mentor_id
```

### 9.4 Coordinator Pool (Society Layer)

```
POOL_SCOPE:       Per society_id
ASSIGNMENT_RULES:
  1 coordinator per event per time window (hard limit)
  Coordinator must be onboarded in same society_id
  trust_index ≥ threshold required at assignment
  Coordinator cannot be assigned to overlapping events

SCOPE_OF_RESPONSIBILITIES (tracked via allocation):
  Workshop batches
  Exposition floor management
  Tournament event management
  Surprise audit scheduling (admin-directed)
```

---

## SECTION 10 — PHYSICAL RESOURCE MANAGEMENT (SOCIETY LAYER)

### 10.1 Venue Registration

```
Venues are registered in resource-registry-service by SOCIETY_ADMIN before any allocation.

VENUE_REGISTRATION_FIELDS:
  venue_id
  venue_name
  venue_type           — WORKSHOP_SPACE | EXPOSITION_HALL | TOURNAMENT_ARENA | MULTI_PURPOSE
  society_id           — owner society
  address              — full physical address (encrypted at rest)
  capacity             — max concurrent occupants (integer)
  accessibility_compliant — boolean (mandatory field; cannot be null)
  equipment_available[]  — FK list to registered EquipmentSets
  geo_coordinates      — lat/long (for territory mapping)
  status               — AVAILABLE | BLOCKED | DECOMMISSIONED
  registered_at        — UTC timestamp

VENUE_REGISTRATION_AUTHORITY:
  SOCIETY_ADMIN for own society venues
  MASTER_ORGANIZER for cross-society venue sharing (explicit approval required)

CROSS_SOCIETY_VENUE_SHARING:
  Forbidden without MASTER_ORGANIZER approval and explicit AuditLog entry
  Shared venue allocation is scoped to sharing_agreement_id
```

### 10.2 Equipment Set Management

```
EQUIPMENT_SET_TYPES (declared — not extendable without version bump):
  PROJECTOR_SET        — projector + screen + cables
  QR_SCANNER_SET       — handheld + fixed QR scanners for attendance
  SOUND_SYSTEM         — microphone + speaker + mixer
  DISPLAY_BOARD_SET    — banners, display boards, tables for exposition stalls
  DOCUMENTATION_KIT    — camera, notebook, pens for audits and events

ALLOCATION_RULES:
  Equipment allocated per event; not shared mid-event across events
  Coordinator confirms equipment handover (COORDINATOR marks EQUIPMENT_RECEIVED in UI)
  Equipment marked RETURNED at event completion (COORDINATOR marks EQUIPMENT_RETURNED)
  Missing or damaged equipment → incident flag → AuditLog → SOCIETY_ADMIN notified

EQUIPMENT_TRACKING:
  Status transitions: AVAILABLE → ALLOCATED (to event) → IN_USE → RETURNED → AVAILABLE
  Damaged equipment → BLOCKED (admin maintenance) → AVAILABLE after repair
```

### 10.3 Territory & Geo-Boundary Enforcement

```
TERRITORY_LOCKING (franchise-service governs; this agent enforces during allocation):
  Every venue has a geo_coordinates tag
  Every society_id has declared geo_boundary (from franchise-service territory map)
  A venue CANNOT be allocated to an event from a different society's territory
    unless cross-boundary_allocation_approved = true (MASTER_ORGANIZER approval)
  Geo-boundary validation runs on every physical venue allocation request
  Violation → REJECT allocation; AuditLog entry; alert FRANCHISE_OWNER

VILLAGE_UNIT_BINDING:
  Workshop batches and society events are bound to village_unit_id
  Resources allocated must belong to same village_unit or have explicit cross-unit approval
```

---

## SECTION 11 — LOGICAL RESOURCE MANAGEMENT

### 11.1 Workshop Batch Seat Management

```
SEAT_COUNTER:
  Redis counter: quota:WORKSHOP_BATCH_SEAT:{batch_id}:{tenant_id}
  Initialised at batch creation with max_participants value
  INCR on each enrollment confirmation
  DECR on withdrawal or cancellation

WAITLIST:
  Activated when counter = max_participants
  WaitlistEntry created per waiting participant
  On withdrawal → first WaitlistEntry promoted → INCR counter → notify

SCHEME_CEILING_ENFORCEMENT:
  If batch tagged with scheme_enrollment_id:
    SCHEME_BATCH_QUOTA counter is parallel constraint (etcd-locked)
    INCR must pass BOTH seat counter AND scheme quota
    Scheme quota = government-declared maximum (from scheme-service)
    Scheme ceiling breach → allocation REJECTED regardless of available seats
```

### 11.2 Interview Slot Management

```
SLOT_CREATION:
  Interview Service creates slot definitions → published to Kafka → this agent registers as ResourceReservation candidates

SLOT_LOCK_ON_BOOKING:
  Candidate selects slot → Redis mutex acquired on {slot_id}:{recruiter_id}
  30-minute TTL; confirmed on payment/confirmation → ALLOCATED
  Expired → AVAILABLE again → next waitlisted candidate promoted

SLOT_ABUSE_PREVENTION:
  A user cannot repeatedly book/cancel the same recruiter's slots
  3+ cancellations with same recruiter within 7 days → flag to Admin Governance Service
```

### 11.3 Tournament Bracket Slot Management

```
BRACKET_SLOT_RESERVATION:
  At tournament.round.scheduled event → etcd mutex per bracket_node_id
  Participants assigned to nodes by Tournament Management Agent
  This agent locks the node; conflict = immediate REJECT (no retry)
  Bracket integrity: a bracket node with allocation conflict → tournament PAUSED; admin alerted
```

---

## SECTION 12 — WAITLIST ENGINE

### 12.1 Waitlist Rules

```
WAITLIST_TRIGGER:   Resource pool at ResourceQuota.max_concurrent
WAITLIST_ENTRY:
  waitlist_entry_id
  resource_type
  pool_id
  requesting_event_id
  requesting_user_id (nullable for system requests)
  priority_lane        — from Section 4.3; higher priority → promoted first
  requested_at         — UTC timestamp
  expiry_at            — 24 hours from requested_at (configurable per resource_type)
  status               — WAITING | PROMOTED | EXPIRED | CANCELLED

PROMOTION_RULE:
  On resource.released event → sort WaitlistEntries by priority_lane ASC, requested_at ASC
  Promote first eligible entry → attempt reservation (per Section 7.1)
  If reservation fails (race) → promote next entry

EXPIRY:
  Airflow DAG: waitlist_expiry_dag (@every_5_minutes)
  WaitlistEntries where expiry_at < NOW() → status = EXPIRED; notify requester

WAITLIST_VISIBILITY:
  Participant sees their waitlist position in Flutter UI (rank in queue)
  Waitlist position updated in real-time via WebSocket push
```

---

## SECTION 13 — CAPACITY PLANNING ENGINE

### 13.1 Utilisation Metrics Published to ClickHouse

```
METRICS CAPTURED (per pool, per tenant, per resource_type):
  current_utilisation_pct     — (allocated / max_concurrent) × 100
  peak_utilisation_pct        — rolling 24h peak
  reservation_conflict_rate   — conflicts / reservation_attempts per hour
  waitlist_depth              — current queue length per pool
  average_reservation_ttl     — average time from RESERVED to ALLOCATED
  release_latency_ms          — time from event completion to resource RELEASED
  allocation_duration_minutes — actual usage vs declared duration

SATURATION_ALERTS:
  Pool utilisation > 80% → resource.pool.saturated event published
  Pool utilisation > 95% → CRITICAL alert; ops on-call paged
  Pool utilisation drops below 60% after saturation → resource.pool.cleared event

DEMAND_FORECAST:
  Airflow DAG: capacity_forecast_dag (@daily, 00:00 UTC)
  Reads ClickHouse: 90-day rolling utilisation history per pool
  Outputs: projected demand for next 30 days per resource_type per tenant
  Stores forecast in PostgreSQL (capacity_forecasts table)
  Alert if forecast shows > 85% utilisation in next 7 days → notify MASTER_ORGANIZER
```

### 13.2 Capacity Planning Reports

```
REPORT TYPES:
  WEEKLY_UTILISATION_REPORT   — per tenant, per resource_type, per pool
  PEAK_DEMAND_ANALYSIS        — top 10 peak days per resource_type
  CONFLICT_RATE_REPORT        — resource types with highest conflict rates
  WAITLIST_TREND_REPORT       — waitlist depth trend per pool
  FORECAST_ACCURACY_REPORT    — predicted vs actual utilisation

REPORT_DELIVERY:
  Stored in MinIO (ecoskiller-ops-reports bucket)
  Sent to MASTER_ORGANIZER + FRANCHISE_OWNER via Notification Service
  Available in Flutter Admin Panel (resource utilisation dashboards)
```

---

## SECTION 14 — SECURITY CONTROLS

### 14.1 Auth & RBAC (Mandatory)

```
ALL resource allocation API calls → JWT auth enforced via Keycloak
  SOCIETY_ADMIN          — manage physical resource pools for own society
  MASTER_ORGANIZER       — cross-society resource governance; view all pools in tenant
  COORDINATOR            — view own event resource allocations; confirm equipment handover
  COACH                  — view own batch assignment
  JUDGE                  — view own judging assignment schedule
  FRANCHISE_OWNER        — read-only utilisation reports for own society units
  PLATFORM_SUPER_ADMIN   — full registry access; override; conflict resolution
  SYSTEM                 — automated allocation (Kafka consumers; Airflow DAGs)

No PARTICIPANT role may view or interact with resource allocation APIs.
Resource pool metadata (capacity numbers, pool IDs) are INTERNAL — never exposed to participants.
```

### 14.2 Data Security

```
Venue physical addresses → encrypted at rest (AES-256)
Human resource profiles (coach, coordinator, judge) → row-level security on society_id
Cross-society data access → FORBIDDEN without explicit sharing_agreement_id
etcd lock keys → never contain PII (use IDs only)
Redis lock keys → never contain PII
AllocationAuditLog → immutable; no delete or update permitted
Capacity forecast data → tenant-scoped; no cross-tenant read
```

### 14.3 Audit Immutability

```
EVERY reservation attempt (success or failure) → AllocationAuditLog entry
EVERY allocation confirmation                  → AllocationAuditLog entry
EVERY release                                   → AllocationAuditLog entry
EVERY allocation conflict detected              → AllocationAuditLog entry
EVERY lock acquisition failure                  → AllocationAuditLog entry
EVERY override by admin                         → AllocationAuditLog entry with actor + justification
EVERY equipment handover/return confirmation    → AllocationAuditLog entry
EVERY territory boundary enforcement action     → AllocationAuditLog entry
EVERY waitlist promotion                         → AllocationAuditLog entry
```

---

## SECTION 15 — FAILURE HANDLING (DETERMINISTIC)

| Failure Condition                               | Agent Action                                                                          |
|-------------------------------------------------|---------------------------------------------------------------------------------------|
| etcd unavailable at lock time                   | REJECT reservation; return 503; alert ops; no allocation proceeds                     |
| Redis unavailable at quota counter              | REJECT reservation; return 503; alert ops                                             |
| PostgreSQL write failure on reservation         | Release lock; rollback; return 500; AuditLog entry; no partial state                 |
| Lock TTL expired mid-event                      | Auto-extend lock if event still IN_USE; publish resource.lock_extended event          |
| Coach assigned to batch goes on misconduct hold | Release allocation; find next eligible coach; notify SOCIETY_ADMIN; AuditLog          |
| Venue marked BLOCKED during active allocation   | Allocation held; SOCIETY_ADMIN + COORDINATOR alerted; admin must resolve              |
| Kafka consumer lag > 120 seconds                | Agent enters DEGRADED mode; reservations still served from PostgreSQL; alert ops      |
| Scheme quota ceiling breach                     | Hard REJECT; etcd lock released; return 403; AuditLog; scheme-service notified        |
| Territory boundary violation detected           | Hard REJECT; allocation rolled back; FRANCHISE_OWNER + MASTER_ORGANIZER alerted      |
| Waitlist promotion race condition               | Single winner via Redis atomic INCR; other promotions fail gracefully; next in queue  |
| Equipment MISSING after event                   | Incident flag created; COORDINATOR + SOCIETY_ADMIN notified; AuditLog; equipment BLOCKED |
| Double assignment detected at DB check          | Rollback; release lock; ALLOCATION_CONFLICT AuditLog; return 409; no partial write   |

No partial outputs. No silent failures. Every failure produces an AuditLog entry and an observability event.

---

## SECTION 16 — OBSERVABILITY REQUIREMENTS

### 16.1 Metrics (Prometheus)

```
resource_reservations_total          (by resource_type, tenant_id, outcome: SUCCESS|CONFLICT|EXPIRED)
resource_allocations_total           (by resource_type, tenant_id)
resource_releases_total              (by resource_type, release_reason)
resource_conflicts_total             (by resource_type, tenant_id)
resource_pool_utilisation_pct        (gauge, by resource_type, pool_id, tenant_id)
resource_waitlist_depth              (gauge, by resource_type, pool_id)
resource_lock_acquisition_latency_ms (histogram, by lock_type: etcd|redis)
resource_lock_failure_total          (by resource_type, lock_type)
resource_allocation_duration_minutes (histogram, by resource_type)
resource_territory_violations_total  (counter, by society_id)
resource_equipment_incidents_total   (counter, by society_id)
resource_scheme_quota_breaches_total (counter, by scheme_id)
```

### 16.2 Dashboards (Grafana)

```
Resource Allocation Health Dashboard:
  → Pool utilisation heat map (all resource_types, all tenants)
  → Reservation conflict rate trend (last 24h, 7d)
  → Lock acquisition latency percentiles (etcd vs Redis)
  → Waitlist depth per pool (live)
  → Top 5 most-contested resource_types
  → Equipment incident count (last 30 days)
  → Scheme quota utilisation per batch
  → Territory boundary violation count (last 30 days)
  → Capacity forecast vs actuals (rolling 30-day)

Resource Allocation Admin Dashboard (MASTER_ORGANIZER / PLATFORM_SUPER_ADMIN):
  → Active allocations table (filterable by resource_type, tenant, event)
  → BLOCKED resources list
  → Conflict log (last 7 days)
  → Human resource pool availability (Coach, Coordinator, Judge, Mentor)
```

### 16.3 Alerting

```
resource.pool.utilisation > 80%           → alert MASTER_ORGANIZER
resource.pool.utilisation > 95%           → CRITICAL alert; page on-call
resource.conflict_rate > 10% in 5 min    → alert ops
resource.etcd_lock_failure_rate > 1%      → CRITICAL alert; infrastructure review
resource.scheme_quota_breach              → alert SOCIETY_ADMIN + scheme-service immediately
resource.territory_violation_detected     → alert FRANCHISE_OWNER + MASTER_ORGANIZER immediately
resource.equipment_missing_post_event     → alert COORDINATOR + SOCIETY_ADMIN
resource.kafka_consumer_lag > 120s        → alert ops; agent degraded mode
resource.stale_lock_detected              → alert ops; auto-force-release initiated
```

---

## SECTION 17 — UI GENERATION RULES (ANTIGRAVITY)

### 17.1 Flutter App — Resource Allocation UI Responsibilities

Antigravity MUST generate the following screens. No extras. No omissions.

```
RESOURCE_DASHBOARD_SCREEN (SOCIETY_ADMIN / MASTER_ORGANIZER)
  → Pool utilisation summary cards per resource_type
  → Colour-coded status: GREEN (<70%) | YELLOW (70–85%) | RED (>85%)
  → Active allocations list (current events with resource assignments)
  → BLOCKED resources list with reason and blocked-by
  → Quick action: BLOCK / UNBLOCK resource
  → Drill-down per resource_type → pool detail view

RESOURCE_POOL_DETAIL_SCREEN (SOCIETY_ADMIN / MASTER_ORGANIZER)
  → All Resources of selected type in society scope
  → Per-resource: status, current allocation (event name, time window), allocated_to
  → Utilisation timeline (hourly; last 7 days)
  → Waitlist count per pool
  → Add resource button (for physical resources: venue, equipment)

VENUE_REGISTRATION_SCREEN (SOCIETY_ADMIN)
  → Form: name, type, capacity, address, accessibility_compliant toggle, equipment list
  → Geo-coordinate input (map picker or manual lat/long)
  → Submit → creates venue in resource-registry-service
  → Validation: accessibility_compliant cannot be null; capacity must be integer > 0

EQUIPMENT_MANAGEMENT_SCREEN (SOCIETY_ADMIN / COORDINATOR)
  → Equipment set list: type, status, last allocated event
  → Mark EQUIPMENT_RECEIVED (Coordinator confirms handover)
  → Mark EQUIPMENT_RETURNED (Coordinator confirms return)
  → Flag as DAMAGED → incident created; equipment BLOCKED pending admin action
  → SOCIETY_ADMIN: BLOCK / UNBLOCK equipment for maintenance

HUMAN_RESOURCE_POOL_SCREEN (SOCIETY_ADMIN / MASTER_ORGANIZER)
  → Tabs: COACH | COORDINATOR | JUDGE | MENTOR
  → Per human resource: name, status (AVAILABLE / ALLOCATED / BLOCKED), trust_index, reputation_score
  → Current allocation: event name, time window
  → Allocate button (triggers allocation flow for specific event)
  → View allocation history per person

ALLOCATION_ASSIGNMENT_SCREEN (SOCIETY_ADMIN — for manual assignments)
  → Select resource_type (dropdown from registry)
  → Select resource from available pool list
  → Select event (from active events in society scope)
  → Confirm assignment → triggers reservation flow
  → Shows: conflict warning if overlap detected (pre-validation before submit)
  → Override option (PLATFORM_SUPER_ADMIN only; requires justification text field)

CONFLICT_ALERT_SCREEN (SOCIETY_ADMIN / MASTER_ORGANIZER)
  → List of AllocationConflicts (last 30 days)
  → Per conflict: resource_type, resource_id, conflicting_event_ids, detected_at, resolution_status
  → Resolution status: PENDING | RESOLVED | OVERRIDDEN
  → Admin can mark RESOLVED with resolution note (appended to AuditLog)

WAITLIST_MANAGEMENT_SCREEN (SOCIETY_ADMIN)
  → Active waitlists per resource_type per pool
  → Per entry: event name, priority_lane, requested_at, expiry_at, position in queue
  → Manual promote button (skips queue; SOCIETY_ADMIN authority; AuditLog required)
  → Cancel waitlist entry button

CAPACITY_FORECAST_SCREEN (MASTER_ORGANIZER / FRANCHISE_OWNER)
  → 30-day demand forecast chart per resource_type
  → Predicted utilisation vs current capacity line chart
  → Saturation risk flag: resource_types forecasted to exceed 85% in next 7 days
  → Export forecast as CSV

ALLOCATION_AUDIT_LOG_SCREEN (SOCIETY_ADMIN / PLATFORM_SUPER_ADMIN)
  → Chronological list of AuditLog entries (filterable by resource_type, event_id, actor)
  → Per entry: timestamp, resource_type, resource_id, action, actor, outcome, justification
  → Read-only; no edit; no delete
  → Paginated; exportable

MY_RESOURCE_ASSIGNMENTS_SCREEN (COACH / COORDINATOR / JUDGE / MENTOR)
  → Personal view: own resource assignments (upcoming + completed)
  → Per assignment: event name, role, date/time (user timezone), venue or room details
  → For COACH: batch details, workshop venue name
  → For JUDGE: event name, assigned categories, judging window
  → For COORDINATOR: event name, equipment assigned to them
  → For MENTOR: session slot, student name (only if session confirmed)
  → No pool management controls
```

### 17.2 React / Next.js SEO Layer

```
NO React pages for Resource Allocation.
Resource allocation is entirely internal operational infrastructure.
Zero public-facing pages.
React layer MUST NOT expose:
  - Pool utilisation data
  - Venue capacity numbers
  - Human resource pool information
  - Conflict or waitlist data
  - Any internal allocation metadata
```

### 17.3 Antigravity UI Limits

```
✅ ALLOWED:
  Generate screens listed in 17.1
  Display utilisation using colour-coded status (GREEN | YELLOW | RED)
  Implement real-time pool utilisation updates via WebSocket push
  Implement conflict pre-validation before assignment confirmation
  Show waitlist position updates via WebSocket push
  Display capacity forecast charts (ClickHouse-backed; read-only)

🚫 FORBIDDEN:
  Generating any public-facing Resource Allocation pages
  Exposing resource pool IDs, capacity numbers, or allocation records to PARTICIPANT role
  Implementing distributed lock logic in Flutter UI layer
  Showing other tenants' resource pools to non-PLATFORM_SUPER_ADMIN users
  Implementing quota override without PLATFORM_SUPER_ADMIN role check
  Adding resource_type beyond the registry in Section 3 without version bump
  Displaying venue physical addresses to non-admin roles
  Building allocation flows that bypass distributed lock (etcd/Redis) sequence
  Allowing PARTICIPANT or STUDENT roles to initiate resource reservations
```

---

## SECTION 18 — TEMPORAL WORKFLOW INTEGRATION

### 18.1 Durable Allocation Workflows (Complex Cases)

Temporal is used where allocation involves multi-step coordination that must survive process restarts.

```
WORKFLOW: coach_assignment_workflow
  Triggered by: batch.started event
  Steps:
    1. Query coach pool availability
    2. Acquire Redis mutex (with Temporal retry + timeout)
    3. Write ResourceAllocation
    4. Notify coach (Notification Service)
    5. Await COACH_CONFIRMED signal (max 24h)
    6. If not confirmed → release allocation; find next coach; retry from Step 1
    7. If confirmed → allocation finalised; update coach-service
  Handles: process crash at any step → resume from last checkpoint

WORKFLOW: physical_venue_allocation_workflow
  Triggered by: exposition.published or batch.started (with venue required)
  Steps:
    1. Validate territory boundary
    2. Acquire etcd mutex on venue_id + time_window
    3. Write ResourceAllocation
    4. Notify COORDINATOR + SOCIETY_ADMIN
    5. Await VENUE_CONFIRMED signal (max 48h)
    6. If not confirmed → release; find alternative venue; retry
  Handles: venue BLOCKED mid-workflow → compensation action triggered

WORKFLOW: scheme_quota_allocation_workflow
  Triggered by: enrollment confirmed + scheme_enrollment_id present
  Steps:
    1. Acquire etcd mutex on scheme_id + batch_id
    2. Read current scheme quota count
    3. If count < ceiling → INCR; write ResourceAllocation; release lock
    4. If count = ceiling → REJECT; trigger WaitlistEntry; release lock
  Handles: Kafka duplicate events via idempotency_key check at Step 1
```

---

## SECTION 19 — DATA RETENTION & BACKUP

```
ResourceAllocation records     → 3-year retention
AllocationAuditLog             → immutable, permanent (WORM-style)
ResourceReservation records    → 30-day post-expiry retention
WaitlistEntry records          → 90-day post-completion retention
CapacitySnapshot (ClickHouse)  → 2-year retention (time-series analytics)
CapacityForecast records       → 1-year retention
ResourcePool records           → retained for venue/resource lifetime + 1 year
Equipment incident records     → 3-year retention (compliance)
Territory boundary violation   → 3-year retention (compliance)
Scheme quota records           → 7-year retention (government compliance)
```

Backup per Ecoskiller infrastructure standards:
- PostgreSQL daily backup (resource-registry schema + allocations schema)
- ClickHouse analytics backup (capacity snapshots)
- Redis snapshot backup (quota counters + active lock state)
- etcd snapshot backup (distributed lock metadata)
- Velero cluster backup includes all namespaces served by this agent

---

## SECTION 20 — MULTI-TENANT ISOLATION

```
TENANT_ISOLATION_RULE:
  All resource pools scoped to tenant_id
  All ResourceAllocations scoped to tenant_id
  Row-level security enforced at PostgreSQL on tenant_id AND society_id
  Cross-tenant resource sharing: FORBIDDEN (no exceptions)
  Cross-society resource sharing within tenant: requires MASTER_ORGANIZER approval
    + explicit sharing_agreement_id + AuditLog entry
  FRANCHISE_OWNER sees only society units owned by them
  MASTER_ORGANIZER sees all societies within tenant (read + manage)
  PLATFORM_SUPER_ADMIN sees all tenants (read-only for governance; override requires justification)
  Resource pool namespace format: {resource_type}:{tenant_id}:{scope_id}
  No implicit namespace inheritance across tenants
```

---

## SECTION 21 — ANTIGRAVITY RUN COMMAND

Paste this block into the Antigravity master prompt to activate this agent:

```
BEGIN LOCKED AGENT ARTIFACT — RESOURCE_ALLOCATION_AGENT v1.0

System Role             : Resource Allocation Agent · Ecoskiller Unified Platform
Execution Engine        : ANTIGRAVITY
Domain                  : Program & Event Management — Resource Allocation Layer
Sub-Domains Served      : Dojo SaaS · Society SaaS · Core Ecoskiller
Stack                   : Flutter (admin operational UI) + NO React public pages
Services Owned          : resource-registry-service · allocation-engine ·
                          conflict-detector · capacity-planner
Lock Infrastructure     : etcd (strong consistency — physical + scheme + bracket resources)
                          Redis (fast lock — virtual rooms + human slots + counters)
Distributed Mutex       : ENFORCED on every resource assignment — no exceptions
Double Assignment       : FORBIDDEN — DB-level check + lock held through write
Overbooking             : FORBIDDEN — quota counters atomic; scheme ceiling hard-rejected
Idempotency             : idempotency_key on every reservation request
Dojo Isolation          : Dojo resource pools NEVER shared with Ecoskiller Core
Domain Isolation        : Arts · Commerce · Science pools NEVER merged
Tenant Isolation        : Row-level security on tenant_id + society_id at PostgreSQL
Territory Enforcement   : Geo-boundary validated on every physical venue allocation
Partial Isolation       : FORBIDDEN
Unlimited Shared Pools  : FORBIDDEN
Temporal Workflows      : Used for multi-step allocations requiring durability
                          (coach assignment · venue allocation · scheme quota)
Human Pools             : Coach · Coordinator · Judge · Mentor — all mutex-locked per time window
Physical Resources      : Venue · Equipment · Stall Position — etcd-locked
Logical Resources       : Interview slots · Batch seats · Bracket nodes · Scheme quota
Waitlist                : Priority-lane sorted; atomic Redis INCR on promotion
Capacity Planning       : ClickHouse time-series; Airflow 30-day forecast DAG
Kafka Events            : Consumes all event.scheduled topics; publishes resource.events topic
Observability           : Prometheus metrics · Grafana dashboards · Alerting declared
AuditLog                : Immutable — every allocation, conflict, release, override logged
Failure Mode            : STOP → ROLLBACK LOCK → LOG → ALERT → NO PARTIAL STATE
Interpretation          : FORBIDDEN
Architecture Change     : ADD-ONLY VIA VERSION BUMP
UI Boundaries           : Section 17 — 10 Flutter admin screens; NO React public pages
React Boundary          : NONE — zero public-facing resource allocation pages

ANTIGRAVITY_SAFE                 = TRUE
ANTIGRAVITY_CONFUSION            = IMPOSSIBLE
READY_FOR                        = ANTIGRAVITY_PRODUCTION
RESOURCE_POOL_ISOLATION          = MANDATORY
DOMAIN_ISOLATION                 = MANDATORY
PARTIAL_ISOLATION                = FORBIDDEN
UNLIMITED_SHARED_RESOURCES       = FORBIDDEN
OVERBOOKING                      = FORBIDDEN
DOUBLE_ASSIGNMENT                = FORBIDDEN

END LOCKED AGENT ARTIFACT — RESOURCE_ALLOCATION_AGENT v1.0
```

---

## SECTION 22 — COMPLIANCE SEAL

```
RESOURCE_ALLOCATION_AGENT COMPLIANCE SEAL

✔ Distributed Locking Enforced (etcd + Redis per resource_type)
✔ Double Assignment Prevention (DB check + lock held through write)
✔ Overbooking Prevention (atomic quota counters; hard ceiling)
✔ Idempotency Enforced (idempotency_key on every reservation)
✔ Dojo Resource Pool Isolated from Ecoskiller Core (never shared)
✔ Arts / Commerce / Science Domain Pools Never Merged
✔ Tenant Isolation (tenant_id row-level security at PostgreSQL)
✔ Society Isolation (society_id row-level security)
✔ Partial Isolation Forbidden
✔ Unlimited Shared Resources Forbidden
✔ Territory Geo-Boundary Enforcement (physical venue allocations)
✔ Cross-Society Venue Sharing Requires MASTER_ORGANIZER Approval
✔ Coach Pool: Trust Index + Reputation Score Gated Assignment
✔ Judge Pool: Conflict-of-Interest Declaration Required
✔ Mentor Pool: No Cross-Tenant Sharing Without Audit
✔ Coordinator Pool: Trust Index Gated
✔ Equipment Handover/Return Tracked (Coordinator confirmation)
✔ Equipment Incident → BLOCKED + AuditLog + Admin Alert
✔ Scheme Quota Ceiling Hard-Rejected (etcd mutex; government compliance)
✔ Slot Abuse Prevention (repeated join/leave → auto cooldown)
✔ Waitlist Priority-Lane Sorted (government > minors > students)
✔ Lock TTL Defined Per Resource Type (Section 6.3)
✔ Stale Lock Detection + Force-Release (Airflow DAG)
✔ Temporal Durable Workflows (coach assignment, venue, scheme quota)
✔ Kafka Consumption: All event.scheduled topics
✔ Kafka Publication: resource.events topic
✔ Capacity Planning: ClickHouse 90-day rolling; 30-day forecast
✔ Saturation Alerts at 80% and 95% utilisation
✔ AuditLog Immutable (every reservation, conflict, release, override)
✔ Data Retention Declared (3-year allocation; 7-year scheme; permanent AuditLog)
✔ Backup Declared (PostgreSQL + ClickHouse + Redis + etcd + Velero)
✔ Antigravity UI Boundary: 10 Flutter admin screens; ZERO React public pages
✔ PARTICIPANT Role: No access to resource allocation APIs
✔ Zero Paid SaaS Dependencies (self-hosted etcd, Redis, Kafka, Temporal, Airflow)
✔ GDPR / India DPDP: venue address encrypted; human resource PII scoped
✔ Observability Non-Optional

MUTATION POLICY   : ADD-ONLY VIA VERSION BUMP
INTERPRETATION    : FORBIDDEN
EXECUTION ENGINE  : ANTIGRAVITY
STATUS            : FINAL · LOCKED · SEALED
```

---

*Ecoskiller Platform · Unified Program & Event Management · Resource Allocation Agent · v1.0 · March 2026 · Internal Use Only · Confidential*
