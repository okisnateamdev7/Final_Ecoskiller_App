# TRAINER_ASSIGNMENT_AGENT
## Ecoskiller Platform · Antigravity Module · Program & Event Management Domain
**Artifact Class:** Production Agent Prompt — SEALED & LOCKED  
**Version:** v1.0  
**Status:** FINAL · LOCKED · GOVERNED · DETERMINISTIC  
**Mutation Policy:** Add-only via version bump. No inline edits permitted.  
**Interpretation Authority:** NONE  
**Execution Authority:** Human declaration only  
**Domain:** Program & Event Management → Trainer Assignment Subsystem  
**Module:** ANTIGRAVITY  
**Namespace (k8s):** `society`  
**Service Name:** `trainer-assignment-agent`  
**Parent System:** Ecoskiller Society Skill & Offline Franchise Model  

---

## ⚠️ SEAL DECLARATION

```
THIS PROMPT IS SEALED.
No AI system, developer, intern, or operator may:
  — Reinterpret any rule herein
  — Substitute any logic not explicitly defined
  — Infer missing behavior from context
  — Deviate from defined failure modes
  — Add soft logic where hard enforcement is specified

Violation → STOP EXECUTION → REPORT AGENT-SEAL-VIOLATION
```

---

## SECTION 1 — AGENT IDENTITY & ROLE

**Agent Name:** `TRAINER_ASSIGNMENT_AGENT`  
**Module:** ANTIGRAVITY  
**Role:** Deterministic, rule-driven orchestrator responsible for assigning certified trainers to Programs and Events within the Antigravity module of the Ecoskiller Society Skill & Offline Franchise Model.

**Agent Type:** Event-Driven Microservice Agent (not conversational AI)  
**Execution Model:** Stateless computation + Stateful Redis + PostgreSQL source of truth  
**Decision Model:** Rule-first. No heuristic. No inference. No ML guess.

**What this agent IS:**
- A deterministic assignment engine
- A constraint enforcer
- An audit log producer
- A conflict detector and blocker
- A notification trigger

**What this agent IS NOT:**
- A recommendation engine
- A preference guesser
- A negotiation layer
- An AI judgment system
- A human replacement for dispute resolution

> "Assignments are computed, not negotiated. Eligibility is enforced, not estimated. Output is logged, not interpreted."

---

## SECTION 2 — SYSTEM CONTEXT (LOCKED)

### 2.1 Platform Position

The TRAINER_ASSIGNMENT_AGENT operates inside the **Ecoskiller Society Skill & Offline Franchise Model** as a core microservice within the **Workshop & Batch Layer (Layer B)** of the Society Domain.

It is downstream of:
- `Society Admin Service` (creates Programs and Events)
- `Auth Service` / Keycloak (provides trainer identity, role, and certification status)
- `Billing & Subscription Service` (provides plan-gated assignment limits)
- `Scoring Engine` (provides trainer performance history)
- `Certification & Belt Engine` (provides trainer credential validity)

It is upstream of:
- `Notification Service` (triggers trainer alerts and confirmations)
- `Analytics Service` (receives assignment events for ClickHouse)
- `Admin Governance Service` (receives conflict and dispute flags)
- `Temporal Workflow Engine` (manages multi-step assignment confirmation flows)

### 2.2 Architecture Principles (Inherited — Non-Negotiable)

From Ecoskiller Master Execution Prompt v12.0:

```
Architecture Style:   Event-driven microservices
Stack Mandate:        100% open-source, self-hosted
State Backend:        Redis (deterministic state machine)
Database:             PostgreSQL (source of truth, RLS enforced)
Event Bus:            Apache Kafka
Identity:             Keycloak (JWT, RBAC)
Orchestration:        Kubernetes (k3s), namespace: society
Workflow Engine:      Temporal (assignment confirmation flows)
Observability:        Prometheus + Loki + OpenTelemetry
Tenant Isolation:     Row-Level Security on society_id + tenant_id
```

No paid SaaS. No Firebase. No managed cloud DBs. No Kong. No GCP Pub/Sub.  
Violation → STOP EXECUTION.

### 2.3 Antigravity Module Definition

**ANTIGRAVITY** is the Program & Event Management module within the Ecoskiller Society domain. It governs:

- Physical and hybrid skill programs run at society franchise locations
- Scheduled batch sessions, workshops, bootcamps, and certification events
- Offline trainer deployment to society nodes
- Real-time and scheduled program execution tracking
- Trainer availability, eligibility, and conflict management
- Commission attribution for trainers per program delivery

The TRAINER_ASSIGNMENT_AGENT is the **sole authorized system** for creating, updating, rejecting, and logging trainer-to-program/event bindings within Antigravity.

No human, no other service, and no manual DB write may create an assignment record without going through this agent.  
Violation → STOP EXECUTION → FLAG as `UNAUTHORIZED_ASSIGNMENT_ATTEMPT`.

---

## SECTION 3 — ENTITY DEFINITIONS (FROZEN)

Primary entities cannot be renamed. Fields may extend — not mutate.

```
Program           — A structured skill delivery unit (e.g., 6-week batch)
Event             — A single scheduled occurrence within or standalone from a Program
Trainer           — A certified Ecoskiller coach (role: COACH in Keycloak)
Assignment        — A binding record: Trainer ↔ Event/Program (with metadata)
Slot              — A time-bound window for a trainer at a location
Society Node      — Physical franchise location (society_id)
Batch             — A cohort of learners assigned to a Program/Event
Conflict          — Any condition preventing a valid Assignment
AssignmentLog     — Immutable audit record of every assignment action
TrainerProfile    — Trainer's eligibility vector (certs, skills, history, load)
```

---

## SECTION 4 — TRAINER ELIGIBILITY RULES (ENFORCED, NOT SUGGESTED)

### 4.1 Hard Eligibility Gates

ALL conditions below must be TRUE for assignment to proceed.  
If ANY condition is FALSE → REJECT assignment → LOG reason → NOTIFY admin.

```
RULE E1 — ROLE CHECK
  trainer.keycloak_role MUST INCLUDE 'COACH'
  Failure → REJECT: ROLE_INVALID

RULE E2 — CERTIFICATION VALIDITY
  trainer.certification_status == 'ACTIVE'
  trainer.certification_expiry > event.scheduled_date
  Failure → REJECT: CERTIFICATION_EXPIRED or CERTIFICATION_INACTIVE

RULE E3 — SKILL DOMAIN MATCH
  trainer.skill_domains INTERSECT program.required_skill_domains ≠ EMPTY
  Failure → REJECT: SKILL_DOMAIN_MISMATCH

RULE E4 — BELT LEVEL SUFFICIENCY
  trainer.belt_level >= program.minimum_trainer_belt
  Failure → REJECT: BELT_LEVEL_INSUFFICIENT

RULE E5 — CONFLICT-FREE SLOT
  No existing Assignment for trainer on same date/time window
  Checked via Redis slot lock + PostgreSQL constraint
  Failure → REJECT: SLOT_CONFLICT

RULE E6 — SOCIETY NODE ELIGIBILITY
  trainer.approved_society_nodes INCLUDES event.society_node_id
  OR trainer.assignment_scope == 'GLOBAL'
  Failure → REJECT: NODE_NOT_AUTHORIZED

RULE E7 — LOAD LIMIT
  trainer.active_assignments_this_week < trainer.max_weekly_load
  max_weekly_load derived from trainer plan tier (default: 5)
  Failure → REJECT: TRAINER_OVERLOADED

RULE E8 — SUSPENSION CHECK
  trainer.account_status NOT IN ['SUSPENDED', 'UNDER_REVIEW', 'BANNED']
  Failure → REJECT: TRAINER_SUSPENDED

RULE E9 — MISCONDUCT FLAG CHECK
  trainer.open_misconduct_flags == 0
  Failure → REJECT: OPEN_MISCONDUCT_FLAG

RULE E10 — MENTOR CERTIFICATION (for Ranked/Certification Events)
  IF event.type IN ['RANKED', 'CERTIFICATION', 'BELT_EXAM']:
    trainer.mentor_certified == true
    trainer.mentor_certification_expiry > event.scheduled_date
  Failure → REJECT: MENTOR_CERT_REQUIRED
```

### 4.2 Soft Advisory Signals (Non-Blocking — Logged Only)

These signals are logged to ClickHouse for admin awareness. They do NOT block assignment.

```
ADVISORY A1 — PERFORMANCE HISTORY
  trainer.avg_session_score < 75 → LOG: LOW_PERFORMANCE_HISTORY

ADVISORY A2 — RECENCY
  trainer.last_active_session > 30 days ago → LOG: TRAINER_INACTIVE_RECENTLY

ADVISORY A3 — REPEAT ASSIGNMENT
  Same trainer assigned to same batch cohort >3 times → LOG: HIGH_REPEAT_RATIO

ADVISORY A4 — TRAVEL DISTANCE
  trainer.base_location distance to event.location > 50km → LOG: HIGH_TRAVEL_DISTANCE
```

Advisories are NEVER used to block. They are NEVER surfaced to the trainer. They are ONLY available to SOCIETY_ADMIN and MASTER_ORGANIZER roles.

---

## SECTION 5 — ASSIGNMENT LIFECYCLE (STATE MACHINE)

All assignment state transitions are governed by a deterministic Redis state machine backed by PostgreSQL.

```
States:
  DRAFT           — Assignment initiated, eligibility check pending
  ELIGIBLE        — All E1–E10 rules passed
  SLOT_LOCKED     — Redis distributed lock acquired on trainer time slot
  PENDING_CONFIRM — Notification sent to trainer, awaiting confirmation
  CONFIRMED       — Trainer accepted within confirmation window
  ACTIVE          — Event date reached, assignment live
  COMPLETED       — Event ended, assignment closed
  CANCELLED       — Assignment cancelled (pre-event)
  REJECTED        — Trainer rejected or eligibility failed
  EXPIRED         — Trainer did not confirm within confirmation window
  DISPUTED        — Admin flagged for review
```

### 5.1 Valid Transitions (LOCKED)

```
DRAFT           → ELIGIBLE         (on eligibility pass)
DRAFT           → REJECTED         (on eligibility fail)
ELIGIBLE        → SLOT_LOCKED      (on Redis lock acquire)
ELIGIBLE        → REJECTED         (on Redis lock contention — slot taken)
SLOT_LOCKED     → PENDING_CONFIRM  (on notification sent)
PENDING_CONFIRM → CONFIRMED        (on trainer acceptance within window)
PENDING_CONFIRM → EXPIRED          (on timeout — default: 24 hours)
PENDING_CONFIRM → REJECTED         (on trainer decline)
CONFIRMED       → ACTIVE           (on event start datetime reached)
CONFIRMED       → CANCELLED        (on admin cancellation before event start)
ACTIVE          → COMPLETED        (on event end datetime + attendance logged)
ACTIVE          → DISPUTED         (on admin flag during or after event)
COMPLETED       → DISPUTED         (on post-event dispute within 72 hours)
DISPUTED        → COMPLETED        (on dispute resolved — no change)
DISPUTED        → CANCELLED        (on dispute resolved — assignment voided)
```

Invalid transitions → STOP → LOG: `INVALID_STATE_TRANSITION` → ALERT admin.

### 5.2 Slot Lock Protocol

```
Lock Key:   trainer_slot:{trainer_id}:{date}:{time_block}
Lock TTL:   300 seconds (5 minutes — sufficient for eligibility + notify trigger)
Lock Tool:  Redis SET NX PX (atomic)
On acquire: proceed to SLOT_LOCKED
On fail:    REJECT with SLOT_CONFLICT, release any partial state
```

No two assignments may share a trainer slot. Lock is acquired BEFORE assignment record is written to PostgreSQL.  
Failure to acquire → REJECT immediately. No retry. No queue. Admin must re-initiate.

---

## SECTION 6 — ASSIGNMENT TRIGGER MODES

### Mode 1: ADMIN-INITIATED (Primary)

Triggered by SOCIETY_ADMIN or MASTER_ORGANIZER via API:

```
POST /api/v1/antigravity/assignments
Authorization: Bearer {JWT with role SOCIETY_ADMIN or MASTER_ORGANIZER}
Body: {
  event_id:    UUID,
  trainer_id:  UUID,
  assigned_by: UUID (admin user),
  notes:       string (optional, max 500 chars)
}
```

Agent response (synchronous):

```
On success:
{
  assignment_id: UUID,
  state: "PENDING_CONFIRM",
  confirmation_deadline: ISO8601,
  eligibility_report: { rules_checked: 10, all_passed: true },
  advisories: [ ...A1–A4 flags if any ]
}

On failure:
{
  assignment_id: null,
  state: "REJECTED",
  rejection_code: "RULE_CODE",
  rejection_reason: "Human-readable description",
  eligibility_report: { rules_checked: N, failed_at: "E_RULE_CODE" }
}
```

### Mode 2: EVENT-TRIGGERED (Automated)

Kafka consumer. Triggered when:
- `program.batch_scheduled` event is published with `trainer_required: true` and `auto_assign: true`
- Agent queries eligible trainer pool and applies deterministic selection algorithm (Section 7)

### Mode 3: REASSIGNMENT (Admin Only)

Triggered only when existing assignment is in state `CANCELLED` or `EXPIRED`.  
Original assignment record is NOT deleted. New assignment is created with `parent_assignment_id` linkage.  
Reassignment count per event is capped at 3. Beyond cap → flag to MASTER_ORGANIZER for manual review.

---

## SECTION 7 — AUTO-SELECTION ALGORITHM (MODE 2 ONLY)

When `auto_assign: true`, the agent selects a trainer from the eligible pool using a **weighted deterministic ranking**, not ML inference.

```
Score Calculation (deterministic):

  base_score = 0

  + 30  if trainer.skill_domain_match_score == 100%  (exact match)
  + 20  if trainer.skill_domain_match_score >= 80%   (partial match)
  + 20  if trainer.society_node_id == event.society_node_id  (local trainer)
  + 15  if trainer.avg_session_score >= 85
  + 10  if trainer.avg_session_score >= 75
  + 10  if trainer.last_active_session <= 7 days ago
  + 5   if trainer.total_completed_sessions >= 20
  - 10  if trainer.active_assignments_this_week >= (max_weekly_load - 1)  (near load limit)
  - 20  if trainer.open_advisory_flags > 2

  Final rank: ORDER BY base_score DESC, trainer.seniority_months DESC
```

**Tiebreaker:** Longest time since last assignment (most rested trainer first).

Selection picks **rank #1** trainer. If rank #1 declines or expires, picks rank #2. Maximum auto-selection attempts: 3. Beyond 3 → escalate to SOCIETY_ADMIN with full eligible pool list.

**This algorithm cannot be changed without a version bump and human sign-off.**

---

## SECTION 8 — CONFIRMATION FLOW (TEMPORAL WORKFLOW)

On reaching state `PENDING_CONFIRM`, a Temporal workflow is initiated:

```
Workflow ID:   assignment_confirm_{assignment_id}
Timeout:       24 hours (configurable per program type, min: 4h, max: 72h)

Steps:
  1. Send confirmation notification to trainer (push + email + SMS)
  2. Wait for trainer response signal (ACCEPT or DECLINE)
     → On ACCEPT within window: transition to CONFIRMED
     → On DECLINE: transition to REJECTED, trigger re-selection if auto_assign
     → On timeout: transition to EXPIRED, trigger re-selection if auto_assign

  Reminder schedule:
    T+4h:  Push notification reminder
    T+12h: Email reminder
    T+20h: SMS + Admin alert ("Trainer has not responded")
```

Temporal ensures durable execution. Crash-safe. No cron job. No manual follow-up required.

---

## SECTION 9 — KAFKA EVENT CONTRACTS (LOCKED)

All events published to Kafka use the schema registry. Schema changes require version bump.

### Published Events (Agent → Bus)

```
Topic: antigravity.assignment.created
Payload: {
  assignment_id, event_id, trainer_id, society_node_id,
  state: "PENDING_CONFIRM", assigned_by, timestamp
}

Topic: antigravity.assignment.confirmed
Payload: {
  assignment_id, event_id, trainer_id, confirmed_at, timestamp
}

Topic: antigravity.assignment.rejected
Payload: {
  assignment_id, event_id, rejection_code, rejection_reason,
  attempted_trainer_id, timestamp
}

Topic: antigravity.assignment.cancelled
Payload: {
  assignment_id, event_id, cancelled_by, cancellation_reason, timestamp
}

Topic: antigravity.assignment.completed
Payload: {
  assignment_id, event_id, trainer_id, attendance_logged,
  session_score, timestamp
}

Topic: antigravity.assignment.disputed
Payload: {
  assignment_id, event_id, dispute_raised_by, dispute_reason, timestamp
}
```

### Consumed Events (Bus → Agent)

```
Topic: antigravity.program.batch_scheduled
  → Triggers Mode 2 auto-assignment if trainer_required: true

Topic: antigravity.event.cancelled
  → Triggers auto-cancel of all CONFIRMED/PENDING_CONFIRM assignments for that event

Topic: trainer.certification.revoked
  → Cancels all CONFIRMED/PENDING_CONFIRM assignments for that trainer
  → Flags all ACTIVE assignments for immediate admin review

Topic: trainer.account.suspended
  → Same handling as certification.revoked

Topic: society.node.deactivated
  → Cancels all future assignments at that node
```

---

## SECTION 10 — DATA MODEL (POSTGRESQL — FROZEN)

Schema: `society` namespace. RLS enforced on `society_id` and `tenant_id`.

### Table: `trainer_assignments`

```sql
CREATE TABLE society.trainer_assignments (
  assignment_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id              UUID NOT NULL,
  society_node_id        UUID NOT NULL,
  event_id               UUID NOT NULL REFERENCES society.events(event_id),
  program_id             UUID REFERENCES society.programs(program_id),
  trainer_id             UUID NOT NULL REFERENCES users.trainers(user_id),
  assigned_by            UUID NOT NULL REFERENCES users.users(user_id),
  state                  VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
  parent_assignment_id   UUID REFERENCES society.trainer_assignments(assignment_id),
  reassignment_count     INT NOT NULL DEFAULT 0,
  rejection_code         VARCHAR(50),
  rejection_reason       TEXT,
  auto_assigned          BOOLEAN NOT NULL DEFAULT FALSE,
  selection_rank         INT,
  notes                  VARCHAR(500),
  confirmation_deadline  TIMESTAMPTZ,
  confirmed_at           TIMESTAMPTZ,
  cancelled_at           TIMESTAMPTZ,
  cancelled_by           UUID,
  completed_at           TIMESTAMPTZ,
  session_score          NUMERIC(5,2),
  attendance_logged      BOOLEAN DEFAULT FALSE,
  dispute_flag           BOOLEAN DEFAULT FALSE,
  created_at             TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at             TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Constraints
ALTER TABLE society.trainer_assignments
  ADD CONSTRAINT uq_trainer_event UNIQUE (trainer_id, event_id)
  DEFERRABLE INITIALLY DEFERRED;

-- RLS
ALTER TABLE society.trainer_assignments ENABLE ROW LEVEL SECURITY;
CREATE POLICY rls_tenant ON society.trainer_assignments
  USING (tenant_id = current_setting('app.current_tenant_id')::UUID);
CREATE POLICY rls_society ON society.trainer_assignments
  USING (society_node_id = current_setting('app.current_society_id')::UUID
         OR current_setting('app.role') IN ('MASTER_ORGANIZER', 'PLATFORM_ADMIN'));
```

### Table: `assignment_audit_log`

```sql
CREATE TABLE society.assignment_audit_log (
  log_id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  assignment_id    UUID NOT NULL,
  event_id         UUID NOT NULL,
  trainer_id       UUID NOT NULL,
  action           VARCHAR(50) NOT NULL,  -- e.g., CREATED, STATE_CHANGE, REJECTED, CANCELLED
  from_state       VARCHAR(30),
  to_state         VARCHAR(30),
  actor_id         UUID,                  -- who triggered it (system or user)
  actor_type       VARCHAR(20),           -- 'SYSTEM', 'ADMIN', 'TRAINER'
  rule_code        VARCHAR(50),
  reason           TEXT,
  metadata         JSONB,
  timestamp        TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- IMMUTABLE: No UPDATE or DELETE permitted on this table.
-- Enforced via trigger + application-level policy.
-- Audit log is append-only.
```

---

## SECTION 11 — API CONTRACTS (LOCKED)

All routes require JWT Bearer token. RBAC enforced per route.

```
POST   /api/v1/antigravity/assignments
       Roles: SOCIETY_ADMIN, MASTER_ORGANIZER
       → Initiate assignment (Mode 1)

GET    /api/v1/antigravity/assignments/{assignment_id}
       Roles: SOCIETY_ADMIN, MASTER_ORGANIZER, COORDINATOR, COACH (own only)
       → Fetch assignment detail + current state

GET    /api/v1/antigravity/assignments?event_id={id}&state={state}
       Roles: SOCIETY_ADMIN, MASTER_ORGANIZER, COORDINATOR
       → List assignments for an event

PUT    /api/v1/antigravity/assignments/{assignment_id}/confirm
       Roles: COACH (self only)
       → Trainer accepts assignment
       → Body: { response: "ACCEPT" | "DECLINE", decline_reason?: string }

DELETE /api/v1/antigravity/assignments/{assignment_id}
       Roles: SOCIETY_ADMIN, MASTER_ORGANIZER
       → Cancel assignment (only if state in [CONFIRMED, PENDING_CONFIRM])
       → Body: { reason: string }

GET    /api/v1/antigravity/trainers/eligible?event_id={id}
       Roles: SOCIETY_ADMIN, MASTER_ORGANIZER
       → Returns ranked eligible trainer pool for an event (for manual selection)
       → Does NOT auto-assign

GET    /api/v1/antigravity/assignments/{assignment_id}/audit
       Roles: SOCIETY_ADMIN, MASTER_ORGANIZER, PLATFORM_ADMIN
       → Returns full immutable audit log for assignment

POST   /api/v1/antigravity/assignments/{assignment_id}/dispute
       Roles: SOCIETY_ADMIN, MASTER_ORGANIZER, COORDINATOR
       → Flag assignment for dispute review
       → Body: { reason: string }
```

No route may bypass eligibility enforcement. No route may write directly to `trainer_assignments` without passing through the agent's rule engine. Violation → STOP EXECUTION.

---

## SECTION 12 — NOTIFICATION CONTRACTS

The agent publishes to the `Notification Service` via Kafka. It does NOT call the notification service directly.

```
Notification Triggers:

  ON: PENDING_CONFIRM
    → To: Trainer (push + email + SMS)
    → Template: assignment_confirmation_request
    → Data: event_name, date, time, location, society_node, confirmation_deadline

  ON: CONFIRMED
    → To: Trainer (push + email)
    → Template: assignment_confirmed
    → To: SOCIETY_ADMIN / COORDINATOR (push)
    → Template: admin_assignment_confirmed

  ON: REJECTED (eligibility failure)
    → To: SOCIETY_ADMIN only
    → Template: admin_assignment_rejected
    → Data: trainer_name, rejection_code, rejection_reason

  ON: EXPIRED (no trainer response)
    → To: SOCIETY_ADMIN + MASTER_ORGANIZER
    → Template: admin_assignment_expired
    → Data: event details, trainer_name, time_elapsed

  ON: CANCELLED
    → To: Trainer (push + email + SMS)
    → Template: assignment_cancelled
    → Data: event_name, cancellation_reason
    → To: SOCIETY_ADMIN
    → Template: admin_assignment_cancelled

  ON: COMPLETED
    → To: Trainer (push)
    → Template: session_completed_thankyou
    → Internal: Trigger commission attribution event

  ON: DISPUTED
    → To: SOCIETY_ADMIN + MASTER_ORGANIZER + PLATFORM_ADMIN
    → Template: admin_assignment_disputed
    → Priority: HIGH
```

Notification service owns templates and delivery. This agent only publishes the trigger event with required data payload.

---

## SECTION 13 — COMMISSION ATTRIBUTION HOOK

On `COMPLETED` state, the agent publishes:

```
Topic: antigravity.commission.trigger
Payload: {
  assignment_id,
  trainer_id,
  event_id,
  program_id,
  society_node_id,
  session_score,
  attendance_count,
  completion_timestamp
}
```

Commission calculation is owned by the **Commission & Finance Layer** (Layer D in Society Architecture). This agent does NOT calculate commissions. It only triggers the event.

Commission trigger is only fired when:
- State transitions to `COMPLETED`
- `attendance_logged == true`
- `dispute_flag == false`

If dispute is active, commission trigger is held until dispute resolution.

---

## SECTION 14 — ANALYTICS EVENT CONTRACTS

All state transitions are published to the Analytics Service via Kafka → ClickHouse.

```
Topic: analytics.antigravity.assignment_events
Payload (all transitions): {
  assignment_id, event_id, trainer_id, society_node_id, tenant_id,
  from_state, to_state, transition_reason, auto_assigned,
  selection_rank, rejection_code, timestamp
}
```

ClickHouse dashboards must track:
- Assignment success rate per society node
- Average time to confirm per trainer
- Rejection rate by rule code
- Auto-assignment success ratio
- Trainer load distribution
- Reassignment frequency per event type
- Dispute rate per program type

---

## SECTION 15 — FAILURE HANDLING (NO DISCRETION)

```
Failure Condition                    System Action
────────────────────────────────────────────────────────────────
Eligibility rule fails               REJECT, LOG, NOTIFY admin
Redis slot lock fails (contention)   REJECT immediately, LOG SLOT_CONFLICT
PostgreSQL write failure             ROLLBACK, ALERT via Prometheus, LOG
Temporal workflow start failure      RETRY x3 (exponential backoff), then ALERT
Kafka publish failure                RETRY x5 (exponential backoff), then LOG to dead-letter topic
Trainer confirmation timeout         TRANSITION to EXPIRED, trigger auto re-select (Mode 2) or notify admin (Mode 1)
Invalid state transition attempt     STOP, LOG: INVALID_STATE_TRANSITION, ALERT admin
Certification revoked mid-assignment CANCEL all active/pending, FLAG to admin
Society node deactivated             CANCEL all future assignments at node, NOTIFY impacted trainers
Reassignment cap exceeded (>3)       STOP auto-reassign, ESCALATE to MASTER_ORGANIZER
Unauthorized assignment write        REJECT 403, LOG: UNAUTHORIZED_ASSIGNMENT_ATTEMPT, ALERT PLATFORM_ADMIN
```

No retry loops on business logic failures. Retries only on infrastructure failures (DB, Kafka, Temporal).  
No partial state. All-or-nothing writes using PostgreSQL transactions.

---

## SECTION 16 — SECURITY CONTROLS (NON-OPTIONAL)

Inherited from Ecoskiller Security Baseline + Dojo SaaS Production Security Lock:

```
Auth:           JWT short-lived access tokens (15 min expiry)
                Refresh token rotation mandatory
                Device session registry enforced

RBAC:           Per-route role enforcement (Keycloak + OPA)
                COACH may only confirm/decline own assignments
                COORDINATOR read-only on assignments
                SOCIETY_ADMIN full CRUD on own society_node
                MASTER_ORGANIZER full CRUD across all nodes (same tenant)
                PLATFORM_ADMIN read + dispute resolution only

Tenant:         All queries scoped by tenant_id (RLS enforced)
                Cross-tenant reads → 403 always

Data:           PII encrypted at rest (trainer contact details)
                Assignment notes field sanitized (XSS prevention)
                No raw audit log export without PLATFORM_ADMIN role

Audit:          All write actions logged to assignment_audit_log (immutable)
                Audit log cannot be modified, deleted, or overwritten
                Audit available to SOCIETY_ADMIN, MASTER_ORGANIZER, PLATFORM_ADMIN

Rate Limits:    POST /assignments — 30 req/min per SOCIETY_ADMIN
                GET /trainers/eligible — 60 req/min
                Exceeded → 429, LOG abuse signal
```

---

## SECTION 17 — OBSERVABILITY (NON-OPTIONAL)

### Prometheus Metrics

```
antigravity_assignment_total{state, society_node_id, tenant_id}
antigravity_assignment_rejection_total{rule_code, society_node_id}
antigravity_assignment_confirmation_latency_seconds{auto_assigned}
antigravity_slot_lock_contention_total{society_node_id}
antigravity_reassignment_total{event_type}
antigravity_trainer_overload_events_total{trainer_id}
antigravity_dispute_total{program_type}
```

### Loki Logs (Structured JSON)

Every agent action produces a structured log entry:

```json
{
  "service": "trainer-assignment-agent",
  "module": "antigravity",
  "assignment_id": "uuid",
  "event_id": "uuid",
  "trainer_id": "uuid",
  "society_node_id": "uuid",
  "action": "STATE_CHANGE",
  "from_state": "ELIGIBLE",
  "to_state": "SLOT_LOCKED",
  "rule_checked": null,
  "result": "SUCCESS",
  "actor_id": "uuid",
  "actor_type": "ADMIN",
  "timestamp": "ISO8601"
}
```

### OpenTelemetry Traces

Full distributed trace from API request → eligibility check → Redis lock → Temporal start → Kafka publish.  
Trace ID propagated through all downstream services.

### Alerting Rules

```
ALERT: assignment_failure_rate > 20% in 5min window          → PagerDuty: HIGH
ALERT: temporal_workflow_start_failure > 0                    → PagerDuty: CRITICAL
ALERT: slot_lock_contention_total spike > 50/min             → Slack: WARN
ALERT: trainer_overload_events_total > 10/hour               → Slack: WARN
ALERT: dispute_total > 5 in 24h for single society_node      → Slack + Email: HIGH
ALERT: audit_log_write_failure > 0                           → PagerDuty: CRITICAL
```

---

## SECTION 18 — DEPLOYMENT SPEC

```
Service Name:        trainer-assignment-agent
K8s Namespace:       society
Image:               ecoskiller/trainer-assignment-agent:v1.0.0
Replicas:            2 (min), 5 (max) — HPA on CPU + Kafka consumer lag
Resources:
  Request:  CPU 250m, Memory 512Mi
  Limit:    CPU 1000m, Memory 1Gi
Liveness:            /health/live  (30s interval)
Readiness:           /health/ready (10s interval)
Startup:             /health/start (60s timeout)

Environment (from Vault — NO plaintext in prod):
  DB_URL, DB_USER, DB_PASSWORD
  REDIS_URL, REDIS_PASSWORD
  KAFKA_BROKERS, KAFKA_GROUP_ID
  KEYCLOAK_REALM, KEYCLOAK_CLIENT_ID, KEYCLOAK_SECRET
  TEMPORAL_HOST, TEMPORAL_NAMESPACE
  NOTIFICATION_TOPIC
  ANALYTICS_TOPIC

ConfigMap:
  SLOT_LOCK_TTL_SECONDS=300
  CONFIRMATION_WINDOW_HOURS=24
  MAX_REASSIGNMENT_ATTEMPTS=3
  AUTO_ASSIGN_MAX_RANK_ATTEMPTS=3
  MAX_WEEKLY_LOAD_DEFAULT=5
```

---

## SECTION 19 — TEST REQUIREMENTS (NO DEPLOY WITHOUT PASS)

```
Unit Tests:
  - All 10 eligibility rules (E1–E10) — test pass and fail for each
  - State machine transitions — test all valid + all invalid
  - Auto-selection ranking algorithm — determinism test (same input = same output)
  - Slot lock contention handling
  - Commission trigger condition (dispute_flag check)

Integration Tests:
  - Full assignment lifecycle (Mode 1): DRAFT → COMPLETED
  - Full assignment lifecycle (Mode 2): auto-select → COMPLETED
  - Rejection flow with audit log validation
  - Temporal workflow: confirm, decline, timeout scenarios
  - Kafka publish verification for all event topics
  - Kafka consume: certification.revoked mid-assignment
  - PostgreSQL RLS: cross-tenant access must fail (403)

Load Tests:
  - 100 concurrent admin-initiated assignments (same node, different trainers)
  - Slot lock contention under 50 concurrent requests for same trainer slot

Integrity Tests:
  - Audit log immutability (no UPDATE/DELETE succeeds)
  - Unique trainer-event constraint enforcement
  - State transition guard (no skip states)

Coverage Threshold: 85% minimum before deploy.
```

---

## SECTION 20 — CHANGE GOVERNANCE

```
ALLOWED without version bump:
  - Add new advisory signal (A-series)
  - Add new Prometheus metric
  - Add new Loki log field
  - Adjust notification reminder schedule
  - Increase resource limits

NOT ALLOWED without version bump + human sign-off:
  - Change any eligibility rule (E1–E10)
  - Change state machine transitions
  - Change auto-selection algorithm weights
  - Change Kafka topic names or payload schema
  - Change PostgreSQL table structure
  - Change API contract (routes, request/response shape)
  - Change Redis lock key format or TTL
  - Change audit log schema
  - Change RBAC role permissions
  - Change commission trigger conditions
```

Version bump format: `vMAJOR.MINOR.PATCH`  
Breaking changes → MAJOR bump.  
New non-breaking features → MINOR bump.  
Fixes → PATCH bump.

---

## SECTION 21 — FINAL GOVERNANCE SEAL

```
╔══════════════════════════════════════════════════════════════════╗
║         TRAINER_ASSIGNMENT_AGENT — ANTIGRAVITY MODULE           ║
║                    FINAL GOVERNANCE SEAL                        ║
╠══════════════════════════════════════════════════════════════════╣
║  Eligibility Engine        LOCKED — 10 Hard Rules Enforced      ║
║  State Machine             LOCKED — Deterministic, No Drift     ║
║  Slot Locking              LOCKED — Redis Atomic, No Race       ║
║  Auto-Selection            LOCKED — Weighted Rank, No ML        ║
║  Confirmation Flow         LOCKED — Temporal Durable Workflow    ║
║  Kafka Contracts           LOCKED — Schema Registry Enforced    ║
║  API Contracts             LOCKED — RBAC Per Route              ║
║  Audit Log                 LOCKED — Append-Only, Immutable      ║
║  Commission Hook           LOCKED — Dispute-Gated               ║
║  Security Baseline         LOCKED — JWT + OPA + RLS             ║
║  Observability             NON-OPTIONAL                         ║
║  Test Gates                NON-OPTIONAL (85% threshold)         ║
║  Multi-Tenant Isolation    ENFORCED (RLS society_id+tenant_id)  ║
║  Open Source Mandate       100% — No Paid SaaS                  ║
║  Interpretation Authority  NONE                                 ║
║  Architecture Authority    LOCKED                               ║
╚══════════════════════════════════════════════════════════════════╝

ANTIGRAVITY PROGRAM & EVENT MANAGEMENT — TRAINER ASSIGNMENT AGENT
Status: PRODUCTION READY SPECIFICATION · SEALED · v1.0
Mutation: Add-only via version bump with human sign-off
Execution: Deterministic. Rules govern. Judgment is excluded.
```

---

*Ecoskiller Platform · Society Domain · Antigravity Module · Internal Use Only · March 2026*
