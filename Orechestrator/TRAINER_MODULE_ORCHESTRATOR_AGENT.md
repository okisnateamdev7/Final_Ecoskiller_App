# TRAINER_MODULE_ORCHESTRATOR_AGENT
## Ecoskiller Platform · Trainer Domain · Master Orchestration Layer
**Artifact Class:** Production Agent Prompt — SEALED & LOCKED
**Version:** v1.0
**Status:** FINAL · LOCKED · GOVERNED · DETERMINISTIC
**Mutation Policy:** Add-only via version bump. No inline edits permitted.
**Interpretation Authority:** NONE
**Execution Authority:** Human declaration only
**Domain:** Trainer Domain → Master Orchestrator
**Module:** TRAINER
**Namespace (k8s):** `trainer`
**Service Name:** `trainer-module-orchestrator`
**Parent System:** Ecoskiller Antigravity Platform

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
  — Route events to sub-agents outside the defined contracts

Violation → STOP EXECUTION → REPORT AGENT-SEAL-VIOLATION
```

---

## SECTION 1 — AGENT IDENTITY & ROLE

**Agent Name:** `TRAINER_MODULE_ORCHESTRATOR_AGENT`
**Module:** TRAINER
**Role:** Master event-driven orchestrator responsible for coordinating all sub-agents,
microservices, and workflows within the Ecoskiller Trainer Domain. It governs trainer
lifecycle from onboarding through revenue, reputation, and career growth — spanning
15 feature domains and all downstream analytics.

**Agent Type:** Event-Driven Orchestrator Microservice (not conversational AI)
**Execution Model:** Stateless computation + Stateful Redis + PostgreSQL source of truth
**Decision Model:** Rule-first. No heuristic. No inference. No ML guess.

**What this agent IS:**
- The single routing authority for all Trainer Domain events
- A domain boundary enforcer between Trainer and other platform domains
- A cross-agent dependency coordinator (sequencing and gating)
- An audit log aggregator for all trainer lifecycle events
- A conflict detector, blocker, and escalation trigger
- A notification dispatch coordinator

**What this agent IS NOT:**
- A recommendation engine
- A preference guesser or negotiation layer
- A replacement for sub-agent decision logic
- A human judgment substitute for dispute resolution
- A business logic executor (logic lives in sub-agents)

> "The orchestrator routes. Sub-agents decide. The audit log remembers everything."

---

## SECTION 2 — SYSTEM CONTEXT (LOCKED)

### 2.1 Platform Position

The `TRAINER_MODULE_ORCHESTRATOR_AGENT` operates at the apex of the **Ecoskiller Trainer
Domain**, sitting between the platform's API Gateway and all specialized Trainer sub-agents.

**It is downstream of:**
- `API Gateway` (receives all inbound Trainer Domain events via Kafka)
- `Auth Service / Keycloak` (validates JWT, RBAC roles for all trainer events)
- `Billing & Subscription Service` (provides plan-gated feature access for trainers)
- `Tenant Config Service` (provides tenant-specific orchestration rules)
- `Society Domain / Antigravity Module` (feeds program/event assignment triggers)

**It is upstream of:**
- All 15 Trainer Sub-Agents (see Section 4)
- `Notification Service` (receives consolidated notification dispatch events)
- `Analytics Service / ClickHouse` (receives orchestration-level telemetry)
- `Admin Governance Service` (receives conflict, escalation, and dispute flags)
- `Temporal Workflow Engine` (manages multi-step, long-running trainer workflows)
- `AUDIT_LOG_AGENT` (receives all orchestration decisions for immutable storage)

### 2.2 Architecture Principles (Inherited — Non-Negotiable)

From Ecoskiller Master Execution Prompt v12.0:

```
Architecture Style:   Event-driven microservices
Stack Mandate:        100% open-source, self-hosted
State Backend:        Redis (deterministic state machine + distributed locking)
Database:             PostgreSQL (source of truth, RLS enforced)
Event Bus:            Apache Kafka (all inter-agent communication)
Identity:             Keycloak (JWT, RBAC)
Orchestration:        Kubernetes (k3s), namespace: trainer
Workflow Engine:      Temporal (multi-step trainer lifecycle workflows)
Observability:        Prometheus + Loki + OpenTelemetry
Tenant Isolation:     Row-Level Security on society_id + tenant_id
Search:               Elasticsearch (trainer discovery, marketplace search)
```

No paid SaaS. No Firebase. No managed cloud DBs. No Kong. No GCP Pub/Sub.
Violation → STOP EXECUTION.

### 2.3 Trainer Module Definition

The **TRAINER MODULE** is the end-to-end trainer lifecycle management domain within
Ecoskiller Antigravity. It governs:

- Trainer onboarding, verification, and profile management
- Trainer marketplace discovery and booking
- Training program creation and content management
- Training delivery (online, offline, hybrid)
- Student assessment, progress tracking, and performance analytics
- Mentorship programs and industrial visit facilitation
- Trainer revenue, monetization, and commission management
- Trainer reputation scoring and review management
- Trainer growth, certifications, and career development
- Governance, compliance, and audit management
- Collaboration workflows between trainers and institutions

The `TRAINER_MODULE_ORCHESTRATOR_AGENT` is the **sole authorized routing layer**
for all events that cross sub-agent boundaries within this domain. No sub-agent
may call another sub-agent directly. All cross-agent communication must pass through
this orchestrator via Kafka.

No human, no other service, and no direct API call may bypass this orchestrator to
trigger sub-agent workflows without a valid Kafka event contract.
Violation → STOP EXECUTION → FLAG as `UNAUTHORIZED_ORCHESTRATION_BYPASS`.

---

## SECTION 3 — ENTITY DEFINITIONS (FROZEN)

Primary entities cannot be renamed. Fields may extend — not mutate.

```
Trainer           — A verified Ecoskiller coach/educator (role: COACH in Keycloak)
TrainerProfile    — Trainer's complete eligibility and skills vector
TrainerOnboarding — Verification workflow record for a new trainer applicant
Program           — A structured training delivery unit (batch, course, workshop)
Session           — A single scheduled instance within a Program
Assignment        — Trainer ↔ Program/Session binding record
Assessment        — A skill evaluation unit created by or assigned to a trainer
Mentorship        — A structured mentor-mentee pairing and program record
IndustrialVisit   — An employer-site experiential learning event
Revenue           — Trainer earnings record (commissions, fees, royalties)
Reputation        — Aggregate trainer quality score (reviews + analytics)
Certification     — A validated trainer credential or belt level record
CareerTrack       — Trainer's professional development pathway record
Collaboration     — A joint program or content co-creation record
GovernanceFlag    — A compliance or policy violation record
AuditLog          — Immutable append-only record of all orchestration actions
```

---

## SECTION 4 — SUB-AGENT REGISTRY (FROZEN)

All sub-agents are owned by the Trainer Domain. The orchestrator routes events to
these agents only. No agent outside this registry may be called without a version
bump and governance sign-off.

```
ID    AGENT NAME                            DOMAIN                       STATUS
──────────────────────────────────────────────────────────────────────────────────
A01   TRAINER_ONBOARDING_VERIFICATION_AGENT Onboarding & Verification    ACTIVE
A02   TRAINER_PROFILE_MANAGEMENT_AGENT      Profile Management           ACTIVE
A03   TRAINER_MARKETPLACE_AGENT             Marketplace Discovery        ACTIVE
A04   TRAINING_PROGRAM_CREATION_AGENT       Program Creation             ACTIVE
A05   TRAINING_DELIVERY_AGENT              Training Delivery            ACTIVE
A06   STUDENT_ASSESSMENT_AGENT             Assessment System            ACTIVE
A07   STUDENT_PERFORMANCE_ANALYTICS_AGENT  Performance Analytics        ACTIVE
A08   MENTORSHIP_SYSTEM_AGENT              Mentorship Management        ACTIVE
A09   INDUSTRIAL_VISIT_AGENT               Industrial Visit             ACTIVE
A10   TRAINER_REVENUE_AGENT                Revenue & Monetization       ACTIVE
A11   TRAINER_REPUTATION_AGENT             Reputation System            ACTIVE
A12   TRAINER_GROWTH_CAREER_AGENT          Growth & Career Development  ACTIVE
A13   TRAINER_CERTIFICATION_AGENT          Certification & Validation   ACTIVE
A14   GOVERNANCE_COMPLIANCE_AGENT          Governance & Compliance      ACTIVE
A15   COLLABORATION_AGENT                  Trainer Collaboration        ACTIVE

─── INHERITED / SHARED AGENTS (cross-domain, read-only from Trainer Domain) ────
B01   TRAINER_ASSIGNMENT_AGENT             Antigravity / Society Domain SHARED
B02   GD_POST_SESSION_ANALYTICS_AGENT      Dojo Engine Domain           SHARED
B03   SCHOOL_PERFORMANCE_ANALYTICS_AGENT   Analytics Domain             SHARED
B04   MODEL_TRAINING_PIPELINE_AGENT        ML Intelligence Domain       SHARED
B05   TEACHER_INCENTIVE_AGENT              Incentive Engine Domain      SHARED
```

**RULE:** The orchestrator PRODUCES events for A01–A15.
For B01–B05, the orchestrator CONSUMES their output events and routes the
relevant payloads to A01–A15. It does NOT call B-series agents directly.
Violation → STOP EXECUTION.

---

## SECTION 5 — TRAINER LIFECYCLE STATE MACHINE (LOCKED)

All trainer lifecycle state transitions are governed by a deterministic Redis state machine
backed by PostgreSQL. The orchestrator owns and enforces this state machine.

```
TRAINER LIFECYCLE STATES:
  APPLICANT           — Trainer has submitted onboarding application
  UNDER_REVIEW        — Documents verified, background check in progress
  PROFILE_INCOMPLETE  — Onboarding passed, profile setup required
  ACTIVE              — Fully verified, active on platform
  SUSPENDED           — Temporarily suspended (compliance or misconduct)
  UNDER_INVESTIGATION — Formal governance investigation in progress
  DEACTIVATED         — Account deactivated (trainer-initiated or admin)
  BANNED              — Permanent ban (serious policy violation)
  RE_ONBOARDING       — Previously deactivated trainer re-applying
```

### 5.1 Valid State Transitions (LOCKED)

```
APPLICANT           → UNDER_REVIEW          (on onboarding_submitted event)
APPLICANT           → APPLICANT             (on resubmission of incomplete docs)
UNDER_REVIEW        → PROFILE_INCOMPLETE    (on onboarding_approved event)
UNDER_REVIEW        → APPLICANT             (on onboarding_rejected — with reason)
PROFILE_INCOMPLETE  → ACTIVE               (on profile_setup_completed event)
ACTIVE              → SUSPENDED            (on suspension_triggered event)
ACTIVE              → UNDER_INVESTIGATION  (on governance_flag_escalated event)
ACTIVE              → DEACTIVATED          (on trainer_deactivation_requested event)
SUSPENDED           → ACTIVE               (on suspension_lifted event)
SUSPENDED           → UNDER_INVESTIGATION  (on investigation_escalated event)
SUSPENDED           → BANNED              (on ban_decision event)
UNDER_INVESTIGATION → ACTIVE              (on investigation_cleared event)
UNDER_INVESTIGATION → SUSPENDED           (on investigation_penalty event)
UNDER_INVESTIGATION → BANNED             (on investigation_ban_decision event)
DEACTIVATED         → RE_ONBOARDING       (on reactivation_request event)
RE_ONBOARDING       → UNDER_REVIEW        (on re_onboarding_submitted event)
BANNED              → [NO VALID TRANSITION] (Permanent. Requires PLATFORM_ADMIN override + audit)
```

Invalid transitions → STOP → LOG: `INVALID_LIFECYCLE_TRANSITION` → ALERT admin.

### 5.2 Orchestration Rules Per State

```
State: APPLICANT
  Active Agents: A01 (TRAINER_ONBOARDING_VERIFICATION_AGENT)
  Blocked Agents: A03–A15 (no marketplace, delivery, or revenue activity)
  Notifications: Confirmation email sent to trainer applicant

State: UNDER_REVIEW
  Active Agents: A01, A14 (GOVERNANCE_COMPLIANCE_AGENT for background check)
  Blocked Agents: A03–A13, A15
  Notifications: Admin notified of pending review

State: PROFILE_INCOMPLETE
  Active Agents: A02 (TRAINER_PROFILE_MANAGEMENT_AGENT)
  Blocked Agents: A03–A15 (no assignments or marketplace until profile complete)
  Notifications: Trainer guided to complete profile (onboarding checklist)

State: ACTIVE
  Active Agents: ALL A01–A15 (within plan-gated limits)
  Blocked Agents: NONE (subject to feature flags)
  Notifications: Welcome email + marketplace listing activated

State: SUSPENDED
  Active Agents: A14 (compliance review only), A10 (revenue hold processing)
  Blocked Agents: A03–A09, A11–A13, A15
  Active Assignments: A01 cancels all CONFIRMED/PENDING assignments via TRAINER_ASSIGNMENT_AGENT
  Revenue: A10 holds all pending payouts until suspension resolved
  Notifications: Trainer notified with reason + appeal process

State: UNDER_INVESTIGATION
  Active Agents: A14 (investigation authority)
  Blocked Agents: ALL non-governance agents
  Notifications: PLATFORM_ADMIN + MASTER_ORGANIZER alerted (HIGH priority)

State: DEACTIVATED
  Active Agents: A10 (final settlement), A14 (data retention compliance)
  Blocked Agents: ALL active workflow agents
  Data: Profile hidden from marketplace; analytics retained per compliance policy
  Revenue: Final payout processed within 30 days

State: BANNED
  Active Agents: A14 (record freeze), A10 (forfeiture processing)
  Blocked Agents: ALL
  Data: Profile permanently deactivated; audit record retained 7 years
```

---

## SECTION 6 — EVENT ROUTING CONTRACTS (LOCKED)

The orchestrator consumes inbound Kafka events and routes them to the appropriate
sub-agent. Each route is deterministic. No route may be inferred or modified without
a version bump.

### 6.1 Inbound Event → Sub-Agent Routing Map

```
INBOUND KAFKA TOPIC                          ROUTE TO AGENT          CONDITION
──────────────────────────────────────────────────────────────────────────────────────
trainer.onboarding.submitted                 A01                     Always
trainer.onboarding.documents_uploaded        A01                     Always
trainer.onboarding.background_check_req      A01 + A14               Always
trainer.onboarding.approved                  A01 → A02               On approval
trainer.onboarding.rejected                  A01                     On rejection
trainer.profile.update_requested             A02                     state = ACTIVE
trainer.profile.media_uploaded               A02                     state = ACTIVE
trainer.profile.skill_tag_updated            A02 → A11               state = ACTIVE
trainer.marketplace.search_request           A03                     state = ACTIVE
trainer.marketplace.booking_initiated        A03 → A01(verify) → A05 state = ACTIVE
trainer.marketplace.listing_update_req       A03                     state = ACTIVE
trainer.program.create_request               A04                     state = ACTIVE
trainer.program.curriculum_upload            A04                     state = ACTIVE
trainer.program.publish_request              A04 → A14(compliance)   state = ACTIVE
trainer.session.schedule_request             A05                     state = ACTIVE
trainer.session.start_event                  A05                     state = ACTIVE
trainer.session.end_event                    A05 → A07 → A11         state = ACTIVE
trainer.session.attendance_logged            A05 → A06               state = ACTIVE
trainer.assessment.create_request           A06                     state = ACTIVE
trainer.assessment.publish_request          A06 → A14               state = ACTIVE
trainer.assessment.grading_completed        A06 → A07               state = ACTIVE
trainer.assessment.result_released          A06 → A12               state = ACTIVE
trainer.analytics.performance_trigger       A07                     state = ACTIVE
trainer.analytics.cohort_report_request     A07                     state = ACTIVE
trainer.mentorship.program_create_req        A08                     state = ACTIVE
trainer.mentorship.pairing_request           A08 → A02               state = ACTIVE
trainer.mentorship.session_completed        A08 → A07 → A12         state = ACTIVE
trainer.industrial_visit.plan_submitted      A09                     state = ACTIVE
trainer.industrial_visit.approved           A09 → A05               state = ACTIVE
trainer.industrial_visit.completed          A09 → A07 → A10         state = ACTIVE
trainer.revenue.commission_trigger          A10                     state = ACTIVE
trainer.revenue.payout_request              A10 → A14               state = ACTIVE
trainer.revenue.invoice_generated           A10                     state = ACTIVE
trainer.reputation.review_submitted          A11                     state = ACTIVE
trainer.reputation.score_recalc_trigger     A11 → A02               state = ACTIVE
trainer.reputation.dispute_raised           A11 → A14               state = ACTIVE
trainer.growth.goal_set                      A12                     state = ACTIVE
trainer.growth.milestone_achieved           A12 → A11 → A10         state = ACTIVE
trainer.growth.learning_path_request        A12 → A13               state = ACTIVE
trainer.certification.apply_request         A13 → A14               state = ACTIVE
trainer.certification.exam_scheduled        A13                     state = ACTIVE
trainer.certification.result_published      A13 → A02 → A11         state = ACTIVE
trainer.certification.revoked               A13 → A01(re-verify)    state != BANNED
trainer.governance.flag_raised              A14                     Always
trainer.governance.investigation_started    A14 (+ state → UNDER_INVESTIGATION)
trainer.governance.suspension_decision      A14 (+ state → SUSPENDED)
trainer.governance.ban_decision             A14 (+ state → BANNED)
trainer.collaboration.request_sent          A15                     state = ACTIVE
trainer.collaboration.content_co_authored   A15 → A04               state = ACTIVE
trainer.collaboration.review_completed      A15 → A11               state = ACTIVE

─── CROSS-DOMAIN INBOUND (from B-series agents) ─────────────────────────────────
antigravity.assignment.confirmed             → A05 (session scheduling update)
antigravity.assignment.completed             → A10 (commission trigger)
antigravity.assignment.cancelled             → A05 (slot release)
gd.analytics.records_ready                  → A07 (performance analytics update)
teacher_incentive.eligibility_computed       → A10 (incentive payout trigger)
school_performance.report_generated         → A07 (cohort benchmark update)
```

### 6.2 Multi-Agent Chain Contracts (Sequential Workflows)

Some events require sequential multi-agent processing. The orchestrator enforces order.

```
CHAIN: TRAINER_ONBOARDING_COMPLETE
  Step 1: A01 → TRAINER_ONBOARDING_VERIFICATION_AGENT (document review + approval)
  Step 2: A02 → TRAINER_PROFILE_MANAGEMENT_AGENT (profile creation prompt)
  Step 3: A14 → GOVERNANCE_COMPLIANCE_AGENT (background check + compliance gate)
  Step 4: A03 → TRAINER_MARKETPLACE_AGENT (listing activation on profile complete)
  Step 5: A12 → TRAINER_GROWTH_CAREER_AGENT (initial career track creation)
  Timeout: Each step max 72 hours. Timeout → escalate to SOCIETY_ADMIN.
  On failure: STOP chain at failing step. Prior steps preserved. Human review required.

CHAIN: SESSION_COMPLETED
  Step 1: A05 → TRAINING_DELIVERY_AGENT (session close + attendance finalization)
  Step 2: A06 → STUDENT_ASSESSMENT_AGENT (grade submission + result release)
  Step 3: A07 → STUDENT_PERFORMANCE_ANALYTICS_AGENT (analytics pipeline trigger)
  Step 4: A11 → TRAINER_REPUTATION_AGENT (reputation score recalculation)
  Step 5: A10 → TRAINER_REVENUE_AGENT (commission attribution trigger)
  Step 6: A12 → TRAINER_GROWTH_CAREER_AGENT (milestone check + XP award)
  Dependency: Step 2 requires Step 1 complete. Steps 3–6 can run in parallel after Step 2.
  Timeout: Full chain must complete within 30 minutes of session_end event.

CHAIN: TRAINER_CERTIFICATION_AWARDED
  Step 1: A13 → TRAINER_CERTIFICATION_AGENT (result published)
  Step 2: A02 → TRAINER_PROFILE_MANAGEMENT_AGENT (profile credential updated)
  Step 3: A11 → TRAINER_REPUTATION_AGENT (trust score updated)
  Step 4: A03 → TRAINER_MARKETPLACE_AGENT (listing badge updated)
  Step 5: A12 → TRAINER_GROWTH_CAREER_AGENT (career milestone recorded)
  Dependency: All steps sequential. Each step awaits prior confirmation.

CHAIN: TRAINER_SUSPENSION
  Step 1: A14 → GOVERNANCE_COMPLIANCE_AGENT (suspension decision)
  Step 2: ORCHESTRATOR → state machine transition to SUSPENDED
  Step 3: B01 → TRAINER_ASSIGNMENT_AGENT (cancel all active/pending assignments)
  Step 4: A03 → TRAINER_MARKETPLACE_AGENT (delist from all search results)
  Step 5: A10 → TRAINER_REVENUE_AGENT (hold all pending payouts)
  Step 6: NOTIFICATION SERVICE → Trainer + Admin notification
  Priority: HIGH. Full chain must complete within 5 minutes of suspension_decision.

CHAIN: PROGRAM_PUBLICATION
  Step 1: A04 → TRAINING_PROGRAM_CREATION_AGENT (content validation)
  Step 2: A14 → GOVERNANCE_COMPLIANCE_AGENT (compliance review)
  Step 3: A03 → TRAINER_MARKETPLACE_AGENT (listing created/updated)
  Step 4: A12 → TRAINER_GROWTH_CAREER_AGENT (XP awarded for program creation)
  Dependency: Step 3 requires both Step 1 + Step 2 complete and approved.

CHAIN: INDUSTRIAL_VISIT_COMPLETED
  Step 1: A09 → INDUSTRIAL_VISIT_AGENT (visit closure + attendance finalized)
  Step 2: A07 → STUDENT_PERFORMANCE_ANALYTICS_AGENT (outcome analytics)
  Step 3: A10 → TRAINER_REVENUE_AGENT (facilitation fee attribution)
  Step 4: A11 → TRAINER_REPUTATION_AGENT (employer feedback → reputation update)
  Step 5: A12 → TRAINER_GROWTH_CAREER_AGENT (industry engagement milestone)

CHAIN: MENTORSHIP_SESSION_COMPLETED
  Step 1: A08 → MENTORSHIP_SYSTEM_AGENT (session closure + notes finalized)
  Step 2: A07 → STUDENT_PERFORMANCE_ANALYTICS_AGENT (mentee progress update)
  Step 3: A10 → TRAINER_REVENUE_AGENT (mentorship fee attribution)
  Step 4: A12 → TRAINER_GROWTH_CAREER_AGENT (mentorship milestone recorded)
```

---

## SECTION 7 — ORCHESTRATION STATE MACHINE (REDIS — LOCKED)

Each orchestration workflow instance is tracked via Redis with a deterministic state machine.

```
ORCHESTRATION STATES (per workflow instance):
  INITIATED         — Trigger event received and validated
  ROUTING           — Sub-agent target identified, event being dispatched
  AWAITING_STEP     — Waiting for a step to complete in a multi-agent chain
  STEP_COMPLETED    — A chain step has returned success confirmation
  CHAIN_COMPLETED   — All steps in a multi-agent chain completed
  ESCALATED         — A step failed or timed out; escalated to admin
  FAILED            — Unrecoverable orchestration failure
  CANCELLED         — Workflow cancelled by admin or system event
```

### State Transition Rules

```
INITIATED           → ROUTING          (on event validation pass)
INITIATED           → FAILED           (on event validation fail)
ROUTING             → AWAITING_STEP    (for multi-step chains)
ROUTING             → CHAIN_COMPLETED  (for single-step routes)
AWAITING_STEP       → STEP_COMPLETED   (on sub-agent confirmation received)
STEP_COMPLETED      → AWAITING_STEP    (if more steps remain in chain)
STEP_COMPLETED      → CHAIN_COMPLETED  (if final step)
AWAITING_STEP       → ESCALATED        (on timeout — per-step timeout defined in chain)
ESCALATED           → AWAITING_STEP    (on admin override — retry)
ESCALATED           → CANCELLED        (on admin cancellation)
CHAIN_COMPLETED     → [terminal]       (success — audit record finalized)
FAILED              → [terminal]       (failure — incident record created)
CANCELLED           → [terminal]       (admin action)
```

### Redis Key Format

```
Workflow Instance:    trainer_orch:{workflow_type}:{trainer_id}:{event_id}
TTL:                  Per workflow type (range: 5 minutes for instant routes,
                      7 days for onboarding chains)
Lock Key:             trainer_orch_lock:{trainer_id}:{workflow_type}
Lock TTL:             300 seconds
```

---

## SECTION 8 — FEATURE FLAG ENFORCEMENT (PLAN-GATED)

Not all features are available to all trainers. Feature access is gated by the
trainer's subscription plan. The orchestrator enforces this BEFORE routing any event.

```
FEATURE                              FREE      STANDARD   PREMIUM    ENTERPRISE
─────────────────────────────────────────────────────────────────────────────────
Trainer Marketplace Listing          ✓ (basic) ✓          ✓          ✓
Training Program Creation            ✓ (1)     ✓ (5)      ✓ (20)     ✓ (unlimited)
Live Session Delivery (Online)       ✓ (2/mo)  ✓ (10/mo)  ✓ (50/mo)  ✓ (unlimited)
Student Assessment Tools             ✓ (basic) ✓          ✓          ✓ (advanced)
Performance Analytics Dashboard      ✗         ✓ (basic)  ✓ (full)   ✓ (custom)
Mentorship Program Management        ✗         ✓          ✓          ✓
Industrial Visit Facilitation        ✗         ✗          ✓          ✓
Revenue Dashboard & Payouts          ✓ (70%)   ✓ (75%)    ✓ (80%)    ✓ (custom rate)
Reputation Score Display             ✓         ✓          ✓          ✓
Certification Fast-Track             ✗         ✗          ✓          ✓
Growth & Career Development Tools    ✓ (basic) ✓          ✓          ✓ (dedicated CSM)
Collaboration Workspace              ✗         ✓ (2)      ✓ (10)     ✓ (unlimited)
Governance Dashboard Access          ✗         ✓          ✓          ✓
Advanced Compliance Reporting        ✗         ✗          ✓          ✓
```

**Enforcement Rule:** If a trainer's plan does not include the requested feature,
the orchestrator:
1. REJECTS the event with `PLAN_FEATURE_NOT_AVAILABLE`
2. Publishes an upsell notification via the Notification Service
3. Logs the rejection with plan_id and feature_code to ClickHouse
4. Does NOT forward the event to the sub-agent

---

## SECTION 9 — API CONTRACTS (LOCKED)

All routes require JWT Bearer token. RBAC enforced per route.
Orchestrator exposes only meta-routing and status endpoints.
Business logic endpoints are owned by individual sub-agents.

```
POST   /api/v1/trainer/events
       Roles: SYSTEM (internal service-to-service only)
       → Receive and route inbound trainer domain events
       → Not exposed to external clients

GET    /api/v1/trainer/{trainer_id}/status
       Roles: COACH (own only), SOCIETY_ADMIN, MASTER_ORGANIZER, PLATFORM_ADMIN
       → Returns trainer lifecycle state + active workflow summary

GET    /api/v1/trainer/{trainer_id}/workflow/{workflow_id}
       Roles: SOCIETY_ADMIN, MASTER_ORGANIZER, PLATFORM_ADMIN
       → Returns full workflow state + chain step status

POST   /api/v1/trainer/{trainer_id}/workflow/{workflow_id}/retry
       Roles: SOCIETY_ADMIN, MASTER_ORGANIZER
       → Admin retry for ESCALATED workflow
       → Body: { step_id: string, override_reason: string }

DELETE /api/v1/trainer/{trainer_id}/workflow/{workflow_id}
       Roles: MASTER_ORGANIZER, PLATFORM_ADMIN
       → Cancel an in-progress workflow
       → Body: { cancellation_reason: string }

GET    /api/v1/trainer/{trainer_id}/audit
       Roles: SOCIETY_ADMIN, MASTER_ORGANIZER, PLATFORM_ADMIN
       → Returns full orchestration audit log for this trainer

POST   /api/v1/trainer/{trainer_id}/suspend
       Roles: SOCIETY_ADMIN, MASTER_ORGANIZER
       → Initiates TRAINER_SUSPENSION chain
       → Body: { reason: string, severity: "WARNING|SUSPENSION|INVESTIGATION" }

POST   /api/v1/trainer/{trainer_id}/reinstate
       Roles: MASTER_ORGANIZER, PLATFORM_ADMIN
       → Initiates suspension lift workflow
       → Body: { justification: string }

GET    /api/v1/trainer/orchestrator/health
       Roles: PLATFORM_ADMIN
       → Returns orchestrator health, active workflow count, queue depths
```

---

## SECTION 10 — KAFKA EVENT CONTRACTS (LOCKED)

### 10.1 Inbound Topics Consumed by Orchestrator

```
Topic Pattern:        trainer.{domain}.{action}
Consumer Group:       trainer-orchestrator
Partition Strategy:   Partitioned by trainer_id (consistent routing per trainer)
Offset Strategy:      Commit after successful routing + audit log write
Dead Letter Topic:    trainer.orchestrator.dlq
```

### 10.2 Outbound Topics Published by Orchestrator

```
Topic: trainer.orchestrator.workflow_initiated
Payload: {
  workflow_id, trainer_id, workflow_type, tenant_id,
  chain_steps: [...], initiated_at, state: "INITIATED"
}

Topic: trainer.orchestrator.step_dispatched
Payload: {
  workflow_id, trainer_id, step_id, target_agent, event_payload,
  dispatched_at, expected_timeout_seconds
}

Topic: trainer.orchestrator.workflow_completed
Payload: {
  workflow_id, trainer_id, workflow_type, tenant_id,
  total_steps, completed_at, duration_ms
}

Topic: trainer.orchestrator.workflow_escalated
Payload: {
  workflow_id, trainer_id, workflow_type, failing_step_id,
  failure_reason, escalated_to, escalated_at
}

Topic: trainer.orchestrator.state_transition
Payload: {
  trainer_id, tenant_id, from_state, to_state,
  triggered_by, workflow_id, transitioned_at
}

Topic: trainer.orchestrator.feature_blocked
Payload: {
  trainer_id, tenant_id, feature_code, plan_id,
  requested_at, rejection_reason: "PLAN_FEATURE_NOT_AVAILABLE"
}

Topic: trainer.orchestrator.security_violation
Payload: {
  trainer_id, tenant_id, violation_type, source_ip,
  attempted_action, detected_at
}
```

### 10.3 Cross-Domain Event Contracts

```
CONSUMED FROM ANTIGRAVITY DOMAIN:
  Topic: antigravity.assignment.confirmed    → Route: A05 (session scheduling sync)
  Topic: antigravity.assignment.completed    → Route: A10 (commission trigger)
  Topic: antigravity.assignment.cancelled    → Route: A05 (slot release)
  Topic: antigravity.commission.trigger      → Route: A10 (revenue processing)

CONSUMED FROM DOJO DOMAIN:
  Topic: gd.analytics.records_ready          → Route: A07 (analytics ingestion)
  Topic: gd.analytics.intelligence_deltas    → Route: A12 (career growth signal)

CONSUMED FROM INCENTIVE DOMAIN:
  Topic: teacher_incentive.eligibility_computed → Route: A10 (payout execution)

PUBLISHED TO CROSS-DOMAIN:
  Topic: trainer.domain.profile_updated      → Consumed by: Marketplace Search Engine
  Topic: trainer.domain.certification_awarded→ Consumed by: TRAINER_ASSIGNMENT_AGENT (B01)
  Topic: trainer.domain.suspended            → Consumed by: TRAINER_ASSIGNMENT_AGENT (B01)
  Topic: trainer.domain.reputation_updated   → Consumed by: SCHOOL_PERFORMANCE_ANALYTICS_AGENT (B03)
```

---

## SECTION 11 — DATA MODEL (POSTGRESQL — FROZEN)

Schema: `trainer` namespace. RLS enforced on `society_id` and `tenant_id`.

### Table: `trainer_lifecycle_states`

```sql
CREATE TABLE trainer.trainer_lifecycle_states (
  state_id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  trainer_id           UUID NOT NULL REFERENCES users.trainers(user_id),
  tenant_id            UUID NOT NULL,
  current_state        VARCHAR(30) NOT NULL DEFAULT 'APPLICANT',
  previous_state       VARCHAR(30),
  transition_reason    TEXT,
  transitioned_by      UUID,
  transitioned_by_type VARCHAR(20),  -- 'SYSTEM', 'ADMIN', 'TRAINER'
  workflow_id          UUID,
  created_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- RLS
ALTER TABLE trainer.trainer_lifecycle_states ENABLE ROW LEVEL SECURITY;
CREATE POLICY rls_tenant ON trainer.trainer_lifecycle_states
  USING (tenant_id = current_setting('app.current_tenant_id')::UUID);
```

### Table: `trainer_orchestration_workflows`

```sql
CREATE TABLE trainer.trainer_orchestration_workflows (
  workflow_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  trainer_id             UUID NOT NULL REFERENCES users.trainers(user_id),
  tenant_id              UUID NOT NULL,
  workflow_type          VARCHAR(60) NOT NULL,
  state                  VARCHAR(30) NOT NULL DEFAULT 'INITIATED',
  chain_definition       JSONB NOT NULL,
  current_step_index     INT NOT NULL DEFAULT 0,
  total_steps            INT NOT NULL,
  trigger_event_id       UUID,
  trigger_topic          VARCHAR(120),
  trigger_payload_hash   VARCHAR(64),
  initiated_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  completed_at           TIMESTAMPTZ,
  escalated_at           TIMESTAMPTZ,
  escalated_reason       TEXT,
  cancelled_at           TIMESTAMPTZ,
  cancelled_by           UUID,
  duration_ms            INT,
  retry_count            INT NOT NULL DEFAULT 0,
  max_retries            INT NOT NULL DEFAULT 3
);

ALTER TABLE trainer.trainer_orchestration_workflows ENABLE ROW LEVEL SECURITY;
CREATE POLICY rls_tenant ON trainer.trainer_orchestration_workflows
  USING (tenant_id = current_setting('app.current_tenant_id')::UUID);
```

### Table: `trainer_workflow_steps`

```sql
CREATE TABLE trainer.trainer_workflow_steps (
  step_id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  workflow_id          UUID NOT NULL REFERENCES trainer.trainer_orchestration_workflows(workflow_id),
  trainer_id           UUID NOT NULL,
  tenant_id            UUID NOT NULL,
  step_index           INT NOT NULL,
  target_agent         VARCHAR(60) NOT NULL,
  kafka_topic_sent     VARCHAR(120),
  kafka_topic_reply    VARCHAR(120),
  dispatched_at        TIMESTAMPTZ,
  completed_at         TIMESTAMPTZ,
  state                VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  result_payload       JSONB,
  failure_reason       TEXT,
  timeout_seconds      INT NOT NULL DEFAULT 300,
  retry_count          INT NOT NULL DEFAULT 0
);
```

### Table: `trainer_orchestration_audit_log` (APPEND-ONLY)

```sql
CREATE TABLE trainer.trainer_orchestration_audit_log (
  log_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  workflow_id     UUID,
  trainer_id      UUID,
  tenant_id       UUID NOT NULL,
  event_type      VARCHAR(60) NOT NULL,
  from_state      VARCHAR(30),
  to_state        VARCHAR(30),
  actor_id        UUID,
  actor_type      VARCHAR(20),
  target_agent    VARCHAR(60),
  kafka_topic     VARCHAR(120),
  payload_hash    VARCHAR(64),
  result_code     VARCHAR(50),
  reason          TEXT,
  metadata        JSONB,
  timestamp       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- IMMUTABLE: No UPDATE or DELETE permitted.
-- Enforced via trigger + application-level policy.
```

### Table: `trainer_feature_access_log`

```sql
CREATE TABLE trainer.trainer_feature_access_log (
  log_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  trainer_id      UUID NOT NULL,
  tenant_id       UUID NOT NULL,
  feature_code    VARCHAR(60) NOT NULL,
  plan_id         VARCHAR(40) NOT NULL,
  access_result   VARCHAR(20) NOT NULL,  -- 'ALLOWED' | 'BLOCKED'
  rejection_code  VARCHAR(60),
  requested_at    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

---

## SECTION 12 — NOTIFICATION CONTRACTS

The orchestrator publishes to the Notification Service via Kafka.
It does NOT call the notification service directly.

```
Notification Triggers:

  ON: Onboarding Application Received
    → To: Applicant Trainer (email + push)
    → Template: trainer_onboarding_received
    → To: SOCIETY_ADMIN (push)
    → Template: admin_new_trainer_application

  ON: Onboarding Approved
    → To: Trainer (email + push + SMS)
    → Template: trainer_onboarding_approved
    → Data: profile_setup_link, deadline (72 hours)

  ON: Onboarding Rejected
    → To: Trainer (email)
    → Template: trainer_onboarding_rejected
    → Data: rejection_reason, appeal_link

  ON: Profile Setup Reminder (T+24h, T+48h if incomplete)
    → To: Trainer (push + email)
    → Template: trainer_profile_incomplete_reminder

  ON: Trainer Goes ACTIVE
    → To: Trainer (email + push)
    → Template: trainer_welcome_active
    → Data: marketplace_link, dashboard_link, quick_start_guide

  ON: Certification Awarded
    → To: Trainer (push + email + SMS)
    → Template: trainer_certification_awarded
    → Data: certification_name, badge_link, validity_period

  ON: Certification Expiry Warning (T-30d, T-7d)
    → To: Trainer (push + email)
    → Template: trainer_certification_expiry_warning

  ON: Reputation Score Change > 5 points
    → To: Trainer (push)
    → Template: trainer_reputation_update
    → Data: new_score, change_direction, key_driver

  ON: Revenue Payout Processed
    → To: Trainer (push + email)
    → Template: trainer_payout_processed
    → Data: amount, period, breakdown_link

  ON: Suspension
    → To: Trainer (email + SMS) — Priority: HIGH
    → Template: trainer_suspension_notice
    → Data: reason, appeal_process_link, duration
    → To: SOCIETY_ADMIN (push) — Template: admin_trainer_suspended

  ON: Suspension Lifted
    → To: Trainer (email + push)
    → Template: trainer_suspension_lifted

  ON: Governance Investigation Started
    → To: SOCIETY_ADMIN + MASTER_ORGANIZER + PLATFORM_ADMIN (all)
    → Template: admin_investigation_started
    → Priority: HIGH

  ON: Workflow Step Timeout / Escalation
    → To: SOCIETY_ADMIN (push + email)
    → Template: admin_workflow_escalated
    → Data: workflow_type, trainer_id, failing_step, elapsed_time
```

---

## SECTION 13 — SECURITY CONTROLS (NON-OPTIONAL)

Inherited from Ecoskiller Security Baseline + Dojo SaaS Production Security Lock:

```
Auth:           JWT short-lived access tokens (15 min expiry)
                Refresh token rotation mandatory
                Device session registry enforced
                Service-to-service tokens: dedicated service accounts in Keycloak

RBAC:           Per-route role enforcement (Keycloak + OPA)
                COACH:             Read-only on own lifecycle state + workflow status
                COORDINATOR:       Read-only on trainer workflows within own society node
                SOCIETY_ADMIN:     Full CRUD on trainer lifecycle within own society
                MASTER_ORGANIZER:  Full CRUD on all trainers across nodes (same tenant)
                PLATFORM_ADMIN:    Full read + suspension/reinstate authority across all tenants
                SYSTEM agents:     Kafka-only inter-agent communication (no API access)

Tenant:         All queries scoped by tenant_id (RLS enforced)
                Cross-tenant trainer queries → 403 always
                Cross-tenant workflow reads → 403 always

Data:           PII fields (trainer contact, bank details) encrypted at rest (AES-256)
                All inter-service communication: TLS 1.3
                No plaintext secrets (Vault / Secret Manager only)
                Audit log cannot be modified, deleted, or overwritten

Rate Limits:    POST /trainer/events — 500 req/min (system-to-system only)
                GET /trainer/{id}/status — 120 req/min per SOCIETY_ADMIN
                POST /trainer/{id}/suspend — 10 req/min per MASTER_ORGANIZER
                Exceeded → 429, LOG abuse_signal → alert PLATFORM_ADMIN

Anti-replay:    All Kafka events carry idempotency_key (event_id + trainer_id hash)
                Duplicate events within 60-minute window: deduplicated, prior result returned
                Idempotency cache: Redis with 2-hour TTL
```

---

## SECTION 14 — FAILURE HANDLING (NO DISCRETION)

```
Failure Condition                        System Action
───────────────────────────────────────────────────────────────────────────────────
Event validation fails                   REJECT, LOG, notify sender agent
Plan feature gate blocks request         REJECT PLAN_FEATURE_NOT_AVAILABLE, upsell notify
Invalid lifecycle state transition       STOP, LOG: INVALID_LIFECYCLE_TRANSITION, ALERT admin
Sub-agent dispatch fails (Kafka)         RETRY x5 (exponential backoff), then DLQ
Sub-agent response timeout               TRANSITION workflow to ESCALATED, notify admin
Chain step failure                       STOP chain, preserve prior steps, ESCALATE
Redis state lock fails                   RETRY x3 (50ms backoff), then ALERT (race condition)
PostgreSQL write failure                 ROLLBACK, ALERT via Prometheus, LOG
Temporal workflow start failure          RETRY x3 (exp backoff), then ALERT
Audit log write failure                  PagerDuty: CRITICAL (immutability guarantee broken)
Cross-tenant event detected              STOP immediately, LOG: SECURITY_VIOLATION, ALERT PLATFORM_ADMIN (P1)
Unauthorized orchestration bypass        REJECT 403, LOG: UNAUTHORIZED_ORCHESTRATION_BYPASS, ALERT
Trainer certification revoked            Cancel all assignments (via B01), update profile (A02),
                                         recalculate reputation (A11), notify admin
Trainer account suspended mid-session    Cancel active assignments (B01), hold revenue (A10),
                                         delist from marketplace (A03), notify trainer + admin
Workflow retry cap exceeded (>3)         ESCALATE to MASTER_ORGANIZER with full workflow trace
```

No silent failures. Every failure produces an immutable audit record.
No partial state. All-or-nothing writes using PostgreSQL transactions.

---

## SECTION 15 — OBSERVABILITY (NON-OPTIONAL)

### Prometheus Metrics

```
trainer_orchestrator_workflows_total{workflow_type, state, tenant_id}
trainer_orchestrator_workflow_duration_ms{workflow_type, p50, p95, p99}
trainer_orchestrator_step_dispatch_latency_ms{target_agent}
trainer_orchestrator_chain_failures_total{workflow_type, failing_step}
trainer_orchestrator_escalations_total{workflow_type, reason}
trainer_orchestrator_feature_blocks_total{feature_code, plan_id}
trainer_orchestrator_state_transitions_total{from_state, to_state}
trainer_orchestrator_kafka_dlq_depth
trainer_orchestrator_security_violations_total{violation_type}
trainer_lifecycle_state_counts{state, tenant_id}
```

### Loki Logs (Structured JSON)

Every orchestration action produces a structured log entry:

```json
{
  "service": "trainer-module-orchestrator",
  "module": "trainer",
  "workflow_id": "uuid",
  "trainer_id": "uuid",
  "tenant_id": "uuid",
  "event_type": "WORKFLOW_STEP_DISPATCHED",
  "workflow_type": "SESSION_COMPLETED",
  "step_index": 2,
  "target_agent": "STUDENT_PERFORMANCE_ANALYTICS_AGENT",
  "state": "AWAITING_STEP",
  "actor_type": "SYSTEM",
  "result": "SUCCESS",
  "timestamp": "ISO8601"
}
```

### OpenTelemetry Traces

Full distributed trace from inbound Kafka event → state machine update → sub-agent
dispatch → Temporal workflow initiation → audit log write.
Trace ID propagated through all downstream sub-agents and cross-domain services.

### Alerting Rules

```
ALERT: workflow_failure_rate > 5% in 5min window             → PagerDuty: HIGH
ALERT: chain_escalation_rate > 10/hour for same workflow type → Slack: WARN
ALERT: audit_log_write_failure > 0                           → PagerDuty: CRITICAL
ALERT: security_violation detected                           → PagerDuty: P1 (immediate)
ALERT: kafka_dlq_depth > 50                                  → PagerDuty: HIGH
ALERT: trainer_suspension_chain duration > 5 min             → Slack: HIGH
ALERT: cross_tenant_event detected                           → PagerDuty: P1 (immediate)
ALERT: step_dispatch_latency_p95 > 2000ms                   → Slack: WARN
```

---

## SECTION 16 — DEPLOYMENT SPEC

```
Service Name:         trainer-module-orchestrator
K8s Namespace:        trainer
Image:                ecoskiller/trainer-module-orchestrator:v1.0.0
Replicas:             3 (min), 10 (max) — HPA on CPU + Kafka consumer lag
Resources:
  Request:  CPU 500m, Memory 1Gi
  Limit:    CPU 2000m, Memory 2Gi
Liveness:             /health/live  (30s interval)
Readiness:            /health/ready (10s interval)
Startup:              /health/start (90s timeout)

Environment (from Vault — NO plaintext in prod):
  DB_URL, DB_USER, DB_PASSWORD
  REDIS_URL, REDIS_PASSWORD
  KAFKA_BROKERS, KAFKA_CONSUMER_GROUP_ID
  KEYCLOAK_REALM, KEYCLOAK_CLIENT_ID, KEYCLOAK_SECRET
  TEMPORAL_HOST, TEMPORAL_NAMESPACE
  NOTIFICATION_TOPIC_PREFIX
  ANALYTICS_TOPIC_PREFIX
  VAULT_ADDR, VAULT_TOKEN

ConfigMap:
  WORKFLOW_LOCK_TTL_SECONDS=300
  ONBOARDING_CHAIN_TIMEOUT_HOURS=72
  SESSION_CHAIN_TIMEOUT_MINUTES=30
  SUSPENSION_CHAIN_TIMEOUT_MINUTES=5
  MAX_WORKFLOW_RETRIES=3
  KAFKA_DLQ_TOPIC=trainer.orchestrator.dlq
  FEATURE_FLAG_CACHE_TTL_SECONDS=300
  IDEMPOTENCY_CACHE_TTL_SECONDS=7200
```

---

## SECTION 17 — TEST REQUIREMENTS (NO DEPLOY WITHOUT PASS)

```
Unit Tests:
  - All lifecycle state transitions (valid + invalid)
  - All event-to-agent routing rules (each inbound topic)
  - Feature flag enforcement (plan gate: BLOCKED + ALLOWED for each feature)
  - Multi-agent chain orchestration (each defined chain type)
  - Failure handling: timeout, retry, escalation, DLQ
  - Cross-tenant event rejection
  - Idempotency deduplication

Integration Tests:
  - Full TRAINER_ONBOARDING_COMPLETE chain (all 5 steps)
  - Full SESSION_COMPLETED chain (all 6 steps, with parallel execution)
  - TRAINER_SUSPENSION chain (end-to-end within 5 minutes)
  - Cross-domain event consumption (from B01, B02, B03)
  - PostgreSQL RLS: cross-tenant access must fail (403)
  - Kafka DLQ: message lands in DLQ after retry cap exceeded
  - Audit log immutability (no UPDATE/DELETE succeeds on audit table)
  - Redis lock: concurrent workflow for same trainer → only one proceeds

Load Tests:
  - 1000 concurrent SESSION_COMPLETED workflow instances (different trainers)
  - 50 concurrent TRAINER_SUSPENSION events (across different tenants)
  - Kafka consumer lag: 10,000 queued events — processing within SLA

Chaos Tests:
  - Sub-agent unavailable mid-chain → escalation fires correctly
  - Redis failure during lock acquire → no data corruption
  - Kafka broker failure → message retained and reprocessed on recovery
  - PostgreSQL failover → workflows resume on reconnect

Coverage Threshold: 90% minimum before deploy.
```

---

## SECTION 18 — CHANGE GOVERNANCE

```
ALLOWED without version bump:
  - Add new notification template
  - Add new Prometheus metric
  - Add new advisory signal
  - Adjust non-breaking timeout values (increase only)
  - Add new Loki log field (non-breaking addition)

NOT ALLOWED without version bump + human sign-off:
  - Change any lifecycle state or state transition
  - Change any event routing rule (Section 6)
  - Change any multi-agent chain definition
  - Change Kafka topic names or payload schema
  - Change PostgreSQL table structure
  - Change API contract (routes, request/response shape)
  - Change Redis key format or TTL
  - Change audit log schema
  - Change RBAC role permissions
  - Change feature flag plan-gate table
  - Change failure handling behavior
  - Add or remove a sub-agent from the registry (Section 4)
  - Change cross-domain event contracts
```

Version bump format: `vMAJOR.MINOR.PATCH`
Breaking changes → MAJOR bump.
New non-breaking features → MINOR bump.
Fixes → PATCH bump.

---

## SECTION 19 — INTEGRATION WITH EXISTING AGENTS

### 19.1 TRAINER_ASSIGNMENT_AGENT (B01 — Antigravity Domain)

```
Relationship:   Cross-domain consumer of B01 events
Integration:    When trainer goes SUSPENDED or BANNED, orchestrator
                immediately triggers B01 (cancel assignments) via:
                  Topic: trainer.domain.suspended → consumed by B01
                When trainer certification is awarded/revoked, orchestrator
                notifies B01 via:
                  Topic: trainer.domain.certification_updated → consumed by B01
B01 reads:      Assignment confirmation → routes to A05
                Assignment completion → routes to A10 (commission)
```

### 19.2 GD_POST_SESSION_ANALYTICS_AGENT (B02 — Dojo Domain)

```
Relationship:   Cross-domain consumer of B02 output events
Integration:    Orchestrator subscribes to:
                  Topic: gd.analytics.records_ready → routes to A07
                  Topic: gd.analytics.intelligence_deltas → routes to A12
B02 output feeds trainer analytics pipeline without requiring direct coupling.
```

### 19.3 SCHOOL_PERFORMANCE_ANALYTICS_AGENT (B03 — Analytics Domain)

```
Relationship:   Cross-domain producer for B03
Integration:    Orchestrator publishes:
                  Topic: trainer.domain.reputation_updated → consumed by B03
                  Topic: trainer.domain.analytics_ready → consumed by B03
B03 receives consolidated trainer performance signals for institution-level
reporting without accessing Trainer Domain internals.
```

### 19.4 MODEL_TRAINING_PIPELINE_AGENT (B04 — ML Domain)

```
Relationship:   Feature vector producer for B04
Integration:    After SESSION_COMPLETED chain, A07 emits feature vectors to
                B04 via the FEATURE_STORE_AGENT (not directly).
                Orchestrator does not call B04 directly.
                B04 model updates flow back to A06 (assessment quality) and
                A07 (analytics models) via model version update events.
```

### 19.5 TEACHER_INCENTIVE_AGENT (B05 — Incentive Domain)

```
Relationship:   Cross-domain input to A10
Integration:    B05 computes incentive eligibility and emits:
                  Topic: teacher_incentive.eligibility_computed
                Orchestrator routes this to A10 (TRAINER_REVENUE_AGENT)
                for payout execution.
                Orchestrator publishes trainer performance signals:
                  Topic: trainer.domain.performance_snapshot → consumed by B05
```

---

## SECTION 20 — SUB-AGENT PLACEHOLDER DEFINITIONS

The following Trainer Domain agents are referenced in routing contracts and must be
implemented to spec. Their orchestration interface contracts are locked here.

### Course Delivery Agent (A05 interface — TRAINING_DELIVERY_AGENT)

```
Domain:         trainer/Agent/course-delivery/
Kafka Inbound:  trainer.session.schedule_request
                trainer.session.start_event
                trainer.session.end_event
                trainer.session.attendance_logged
Kafka Outbound: trainer.session.started (→ orchestrator acknowledges, routes to A06)
                trainer.session.completed (→ triggers SESSION_COMPLETED chain)
                trainer.session.attendance_finalized (→ A06, A07)
```

### Challenge Preparation Agent (A06 interface — STUDENT_ASSESSMENT_AGENT)

```
Domain:         trainer/Agent/challenge-preparation/
Kafka Inbound:  trainer.assessment.create_request
                trainer.assessment.grading_completed
Kafka Outbound: trainer.assessment.graded (→ A07 analytics pipeline)
                trainer.assessment.result_released (→ A12 growth signal)
```

### Workshop Management Agent (A08/A09 interface — MENTORSHIP + INDUSTRIAL_VISIT)

```
Domain:         trainer/Agent/workshop-management/
Kafka Inbound:  trainer.mentorship.program_create_req
                trainer.mentorship.session_completed
                trainer.industrial_visit.plan_submitted
                trainer.industrial_visit.completed
Kafka Outbound: trainer.mentorship.session_closed (→ A07, A10, A12)
                trainer.industrial_visit.closed (→ A07, A10, A11)
```

---

## SECTION 21 — FINAL GOVERNANCE SEAL

```
╔══════════════════════════════════════════════════════════════════════════╗
║         TRAINER_MODULE_ORCHESTRATOR_AGENT — TRAINER DOMAIN              ║
║                      FINAL GOVERNANCE SEAL                              ║
╠══════════════════════════════════════════════════════════════════════════╣
║  Lifecycle State Machine      LOCKED — 9 States, 17 Transitions         ║
║  Event Routing Map            LOCKED — 55 Inbound Topics, Deterministic  ║
║  Multi-Agent Chains           LOCKED — 6 Chain Types Defined             ║
║  Feature Flag Enforcement     LOCKED — Plan-Gated, Pre-Routing           ║
║  Sub-Agent Registry           LOCKED — A01–A15 + B01–B05                ║
║  Kafka Contracts              LOCKED — Schema Registry Enforced          ║
║  API Contracts                LOCKED — RBAC Per Route                   ║
║  Audit Log                    LOCKED — Append-Only, Immutable            ║
║  Cross-Domain Integration     LOCKED — B01–B05 Contracts Defined        ║
║  Security Baseline            LOCKED — JWT + OPA + RLS + TLS 1.3        ║
║  Tenant Isolation             ENFORCED (RLS tenant_id, anti-cross-tenant)║
║  Feature Gating               ENFORCED (plan-level, pre-routing check)   ║
║  Failure Handling             NON-OPTIONAL — No Silent Failures          ║
║  Observability                NON-OPTIONAL — Full Telemetry              ║
║  Test Gates                   NON-OPTIONAL (90% coverage threshold)      ║
║  Open Source Mandate          100% — No Paid SaaS                        ║
║  Interpretation Authority     NONE                                       ║
║  Architecture Authority       LOCKED                                     ║
╚══════════════════════════════════════════════════════════════════════════╝

ECOSKILLER ANTIGRAVITY — TRAINER MODULE ORCHESTRATOR
Status: PRODUCTION READY SPECIFICATION · SEALED · v1.0
Mutation: Add-only via version bump with human sign-off
Execution: Deterministic. Routes govern. Judgment is excluded.
Sub-Agents: 15 Domain Agents + 5 Cross-Domain Shared Agents
Features:   15 Feature Domains Governed
Chains:     6 Multi-Agent Workflow Chains
Namespace:  trainer (k8s)
Tenancy:    Strict Multi-Tenant Isolation (PostgreSQL RLS + Kafka partitioning)
```

---

*Ecoskiller Antigravity Platform · Trainer Domain · Master Orchestration Layer · Internal Use Only · March 2026*
