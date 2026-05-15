# 🔒 GD_SESSION_SCHEDULER_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED PRODUCTION ARTIFACT
---

```
ARTIFACT_CLASS        = PRODUCTION SYSTEM AGENT BLUEPRINT
PLATFORM              = Ecoskiller Antigravity
MUTATION_POLICY       = ADD-ONLY via version bump
EXECUTION_MODE        = DETERMINISTIC + VALIDATED
CREATIVE_INTERPRETATION = FORBIDDEN
ASSUMPTION_FILLING    = FORBIDDEN
DEFAULT_BEHAVIOR      = DENY
FAILURE_MODE          = STOP_EXECUTION
SCHEMA_VERSION        = v1.0.0
SEAL_DATE             = 2026-02-25
SEAL_AUTHORITY        = ECOSKILLER INTELLIGENCE & INNOVATION CORE
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY — NON-NEGOTIABLE)

```
AGENT_NAME            = GD_SESSION_SCHEDULER_AGENT
SYSTEM_ROLE           = Autonomous Group Discussion Session Lifecycle Orchestrator
PRIMARY_DOMAIN        = Dojo Engine → Group Discussion Subsystem
EXECUTION_MODE        = Deterministic + Validated
DATA_SCOPE            = GD Topics | Session Slots | Participant Pools | Role Assignments |
                        Timers | Phase Transitions | Score Triggers | Audit Trails
TENANT_SCOPE          = Strict Isolation (Institute | Enterprise | Platform)
FAILURE_POLICY        = Halt on Ambiguity
STACK_CONTEXT         = Flutter (operational runtime) + React (SEO web, read-only)
TRANSPORT_CONTEXT     = WebSocket (game events) | WebRTC (voice/video) | Event Bus (analytics)
```

This agent is the **sole authority** for scheduling, triggering, and lifecycle-managing GD sessions inside the Dojo engine. It does not make educational content decisions. It does not evaluate users. It schedules, assembles, and transitions.

**Interpretation Authority: NONE**
**Architecture Authority: LOCKED**

---

## 2️⃣ PURPOSE DECLARATION

### Problem This Agent Solves

Inside the Ecoskiller Antigravity Dojo engine, Group Discussion (GD) sessions are multi-participant, time-bound, role-assigned events requiring strict orchestration. Without this agent, GD sessions would require manual teacher intervention to launch, role assignment would be ad-hoc, preparation timers would be uncontrolled, and phase transitions would be inconsistent, breaking the deterministic Dojo evaluation model.

This agent eliminates all manual management from session creation through session teardown.

### What It Consumes (Input Sources)

- Topic records created by Teacher, Student, or Dojo System Generator
- Participant join events from enrolled users (via Participant Pool)
- Tenant plan configuration (GD quota: 30/50/70/100 per month per plan)
- Timer configuration per topic record
- Role rotation state from previous sessions (for fairness engine)
- Domain lock config (Arts / Commerce / Science / Technology / Administration)

### What It Produces (Output Targets)

- Scheduled session slots with confirmed participant groups
- Role assignment manifests (deterministic, auditable)
- Phase transition commands dispatched to Timer Service and Dojo Match Engine
- Session room creation events consumed by Video/WebSocket infrastructure
- Completion events triggering Scoring Service, Passport Update, XP Engine, and Analytics
- Full append-only audit records per session lifecycle event

### Upstream Agent Dependencies

```
UPSTREAM_AGENTS:
  - TOPIC_CREATION_AGENT          → Provides topic records
  - PARTICIPANT_POOL_AGENT        → Provides enrolled user queues
  - TENANT_PLAN_AGENT             → Provides GD quota + plan metadata
  - ROLE_ROTATION_STATE_AGENT     → Provides prior role history per user
  - DOMAIN_ISOLATION_AGENT        → Validates cross-domain access rules
  - BILLING_CHECK_MIDDLEWARE      → Validates GD quota not exceeded
```

### Downstream Agent Dependencies

```
DOWNSTREAM_AGENTS:
  - TIMER_SERVICE_AGENT           → Receives phase timer commands
  - DOJO_MATCH_ENGINE_AGENT       → Receives session start trigger
  - VIDEO_ROOM_AGENT              → Receives room creation event (voice/video GDs)
  - SCORING_SERVICE_AGENT         → Receives session_end event for evaluation
  - PASSPORT_UPDATE_AGENT         → Receives intelligence delta update trigger
  - XP_REWARD_AGENT               → Receives XP award events per role action
  - LEADERBOARD_AGENT             → Receives GD completion signal
  - FEATURE_STORE_AGENT           → Receives behavioral feature vector
  - OBSERVABILITY_AGENT           → Receives all health metrics
  - AUDIT_LOG_AGENT               → Receives immutable event log entries
  - OFFLINE_SYNC_AGENT            → Receives queued events for offline school mode
```

---

## 3️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA:
{
  "required_fields": [
    "topic_id",
    "tenant_id",
    "domain_track",
    "group_size",
    "prep_time_seconds",
    "discussion_time_seconds",
    "gd_type",
    "creator_id",
    "creator_role",
    "session_mode"
  ],
  "optional_fields": [
    "scheduled_start_utc",
    "custom_phase_config",
    "offline_mode_flag",
    "force_role_assignment",
    "mentor_id",
    "tournament_id",
    "certification_context_flag"
  ],
  "validation_rules": [
    "topic_id must exist in topics table for same tenant_id",
    "group_size must be integer between 2 and 10 (inclusive)",
    "prep_time_seconds must be integer between 300 and 1800",
    "discussion_time_seconds must be integer between 600 and 5400",
    "gd_type must be enum: TEACHER_CREATED | STUDENT_CREATED | SYSTEM_GENERATED",
    "creator_role must be enum: TEACHER | STUDENT | SYSTEM | ADMIN",
    "session_mode must be enum: TEXT | VOICE | VIDEO",
    "domain_track must be one of: ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION",
    "tenant GD quota for billing_period must not be exceeded (check BILLING_CHECK_MIDDLEWARE)",
    "scheduled_start_utc if present must be ISO-8601 UTC and >= now + 60 seconds",
    "offline_mode_flag if true requires session_mode = TEXT only"
  ],
  "security_checks": [
    "JWT token must be valid and not expired",
    "creator_id must belong to tenant_id (tenant isolation hard check)",
    "creator_role permissions must allow GD creation for gd_type",
    "domain_track must match creator's enrolled domain (no cross-domain creation without explicit grant)",
    "session_mode = VIDEO requires tenant plan to include video GD feature flag",
    "certification_context_flag = true requires mentor_id to be present and certified"
  ],
  "domain_checks": [
    "topic_id domain tag must match domain_track field",
    "all participant pool members must be enrolled in same domain_track",
    "no cross-tenant participant allowed"
  ]
}
```

**Null Tolerance Policy:** ZERO null tolerance on required fields. Any null in required fields triggers immediate rejection with LOG_INCIDENT.

**Malformed Data Policy:** Reject immediately. Log validation failure to AUDIT_LOG_AGENT with input_hash, failure_reason, actor_id, timestamp_utc.

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA:
{
  "result_object": {
    "session_id": "UUID",
    "topic_id": "UUID",
    "tenant_id": "UUID",
    "group_id": "UUID",
    "session_status": "ENUM: SCHEDULED | ASSEMBLING | PREPARATION | DISCUSSION | CLOSING | COMPLETED | FAILED | CANCELLED",
    "phase_schedule": {
      "prep_start_utc": "ISO-8601",
      "prep_end_utc": "ISO-8601",
      "discussion_start_utc": "ISO-8601",
      "discussion_end_utc": "ISO-8601",
      "phase_sequence": ["OPENING", "IDEA_GENERATION", "DEBATE", "SUMMARY"]
    },
    "role_manifest": [
      {
        "user_id": "UUID",
        "assigned_role": "ENUM: MODERATOR | SPEAKER | IDEA_GENERATOR | NOTE_KEEPER | QUESTIONER",
        "role_rotation_index": "integer"
      }
    ],
    "gd_mode": "TEXT | VOICE | VIDEO",
    "room_token": "string (signed, time-limited — only for VOICE/VIDEO sessions)",
    "next_trigger_event": ["PREP_TIMER_START", "ROOM_CREATE_IF_AV", "PARTICIPANT_NOTIFY"]
  },
  "confidence_score": "0.0–1.0 (scheduling feasibility score; below 0.75 = escalate)",
  "model_version": "GD_SCHEDULER_ML_v1.0.0",
  "audit_reference": "UUID",
  "next_trigger_event": [
    "TIMER_SERVICE::START_PREP_TIMER",
    "DOJO_MATCH_ENGINE::SESSION_READY",
    "VIDEO_ROOM_AGENT::CREATE_ROOM (if VIDEO/VOICE)",
    "PARTICIPANT_NOTIFY_AGENT::SEND_JOIN_SIGNAL",
    "FEATURE_STORE_AGENT::EMIT_BEHAVIORAL_VECTOR"
  ]
}
```

**All outputs MUST include:** confidence_score, model_version, audit_reference, next_trigger_event array.

**Outputs without audit_reference are invalid and must not be forwarded downstream.**

---

## 5️⃣ ML / AI LOGIC LAYER

This agent is **ML-primary (70–80% ML logic)** with **AI-assist (20–30% scope-bounded)**.

### ML Logic (Primary)

```
MODEL_TYPE:
  - Classification: Session feasibility scoring (can session be scheduled given quota, group size, availability)
  - Time-Series: Optimal scheduling slot prediction based on historical participation patterns
  - Clustering: Participant grouping fairness model (balance by rating band, history, role gap)

FEATURES_USED:
  - tenant_id (encoded)
  - domain_track (encoded)
  - gd_type (encoded)
  - group_size
  - historical_participation_rate per user in pool
  - time_of_day (UTC hour)
  - day_of_week
  - tenant_gd_quota_remaining
  - prior_role_assignments (from ROLE_ROTATION_STATE_AGENT)
  - session_mode (TEXT=0 / VOICE=1 / VIDEO=2)
  - participant_rating_band (average and variance)
  - repeat_opponent_flag (bool per pair)

TRAINING_FREQUENCY:
  - Feasibility classifier: Weekly retrain on prior 30-day session data
  - Slot prediction model: Monthly retrain
  - Grouping model: Monthly retrain

DRIFT_DETECTION:
  - Monitor distribution shift on: participation_rate, session_completion_rate, group_size distribution
  - Monitor accuracy degradation: feasibility_prediction vs actual_session_success
  - Alert threshold: >5% accuracy drop over 7-day rolling window → ESCALATE to OBSERVABILITY_AGENT

VERSION_CONTROL:
  - All models stored with immutable model_version tag
  - Model reference embedded in every OUTPUT_SCHEMA response
  - Rollback-safe: prior model version retained for 60 days
```

### AI Logic (Bounded Scope Only)

```
AI_USAGE_SCOPE:
  - Scope 1: Conflict resolution prompt — when grouping ML returns ambiguous result
              (confidence < 0.75), AI assists with a structured tie-breaking suggestion
              (e.g., which participant swap improves role rotation fairness most).
              AI produces a ranked option list. Human (MENTOR or ADMIN) selects.
  - Scope 2: Session title / description enrichment for SYSTEM_GENERATED GD topics
              (text summarization, domain-safe).
  - Scope 3: Anomaly explanation — when ANTI_COLLUSION_DETECTION flags a session,
              AI generates a structured anomaly report for governance review.

PROMPT_GOVERNANCE:
  - All AI prompts versioned (PROMPT_VERSION = GD_SCHEDULER_PROMPT_v1.0.0)
  - Deterministic structure enforced; no open-ended generation allowed
  - AI output treated as ADVISORY only; no binding decision without human or ML confirmation
  - AI must NEVER auto-assign roles, auto-launch sessions, or auto-cancel sessions

AI MUST NOT:
  - Replace ML scheduling decisions
  - Auto-approve, auto-block, or auto-cancel any session
  - Override governance agents
  - Operate outside defined scope above
```

---

## 6️⃣ SCALABILITY DESIGN

```
EXPECTED_RPS:
  - 1M users:  ~280 session create requests/hour peak → ~0.08 RPS average, burst ~50 RPS
  - 10M users: ~2800 session create requests/hour peak → ~0.8 RPS average, burst ~500 RPS
  - 100M users: ~28000 session create requests/hour peak → ~8 RPS average, burst ~5000 RPS

LATENCY_TARGET:
  - Session schedule confirmation: p95 < 500ms
  - Role manifest generation: p95 < 200ms
  - Phase transition dispatch: p95 < 100ms

MAX_CONCURRENCY:
  - 10,000 simultaneous active sessions per deployment cluster

QUEUE_STRATEGY:
  - Kafka topic: gd.session.schedule.requests (partitioned by tenant_id)
  - Kafka topic: gd.session.lifecycle.events (partitioned by session_id)
  - Dead letter queue: gd.session.schedule.dlq (retry-3 then escalate)
  - Consumer group: gd-scheduler-consumer-group

HORIZONTAL_SCALING:
  - Stateless execution enforced — no local session state in agent memory
  - All state persisted to PostgreSQL + Redis (session phase cache)
  - Kubernetes HPA on CPU + Kafka consumer lag

IDEMPOTENCY:
  - All schedule requests include idempotency_key (topic_id + creator_id + scheduled_start_utc hash)
  - Duplicate requests within 60s window are deduplicated and return prior result
  - Idempotency cache: Redis with 5-minute TTL

ASYNC_PROCESSING:
  - Session creation: Async via Kafka
  - Phase transitions: Async via Timer Service event
  - Score triggers: Async via session_end event
  - Synchronous: Only health-check and validation endpoints
```

---

## 7️⃣ SECURITY ENFORCEMENT

```
TENANT ISOLATION:
  - All DB queries scoped by tenant_id at row-level security (PostgreSQL RLS enforced)
  - Cross-tenant participant join = SECURITY FAILURE → STOP_EXECUTION + LOG_INCIDENT
  - tenant_id extracted from JWT claims only (never from request body)

DOMAIN ISOLATION:
  - Participant domain_track verified against topic domain_track
  - Cross-domain access forbidden unless explicit grant in tenant policy table
  - Domain leaks classified as SECURITY FAILURE

ROLE-BASED AUTHORIZATION:
  - TEACHER: can create TEACHER_CREATED GDs within own domain
  - STUDENT: can create STUDENT_CREATED GDs within own domain (if plan permits)
  - SYSTEM: can create SYSTEM_GENERATED GDs for any tenant domain
  - ADMIN (Tenant): can create any GD type within tenant
  - ADMIN (Platform): full access
  - EVALUATOR: read-only on session manifests
  - PARENT: read-only visibility on child's sessions (no creation)
  - RECRUITER/HR: no GD scheduling access (Dojo internal only)

JWT ENFORCEMENT:
  - Short-lived access tokens (15-minute expiry)
  - Refresh token rotation mandatory
  - Video room tokens: signed, separate TTL = session duration + 10 minutes
  - Token scope: must include `dojo:gd:schedule` claim

ENCRYPTION:
  - All PII at rest: AES-256
  - All inter-service communication: TLS 1.3
  - Room tokens: HMAC-SHA256 signed
  - No plaintext secrets in environment (Secret Manager only)

AUDIT LOGGING:
  - All session lifecycle events: append-only immutable log
  - Every role assignment: logged with rotation_index and actor
  - Every phase transition: logged with UTC timestamp and trigger source
  - Every failed validation: logged with input_hash and failure_reason
  - WAF enforced at API gateway level
  - Rate limiting: 100 session create requests per user per hour

ANTI-CHEAT:
  - match farming detection via ANTI_COLLUSION_DETECTION_AGENT
  - Flagged sessions held from scoring until audit review
  - Behavioral anomaly signals emitted to FEATURE_STORE_AGENT
```

---

## 8️⃣ AUDIT & TRACEABILITY

Every execution event must produce an immutable audit record:

```json
AUDIT_RECORD:
{
  "timestamp_utc": "ISO-8601",
  "actor_id": "UUID (user_id or SYSTEM)",
  "actor_role": "ENUM",
  "tenant_id": "UUID",
  "session_id": "UUID",
  "event_type": "ENUM: SESSION_CREATED | PARTICIPANT_JOINED | ROLES_ASSIGNED | PREP_STARTED |
                        PREP_ENDED | DISCUSSION_STARTED | PHASE_TRANSITION | DISCUSSION_ENDED |
                        SESSION_COMPLETED | SESSION_FAILED | SESSION_CANCELLED | VALIDATION_FAILED |
                        SECURITY_VIOLATION | QUOTA_EXCEEDED",
  "input_hash": "SHA-256 of canonical input payload",
  "output_hash": "SHA-256 of canonical output payload",
  "model_version": "GD_SCHEDULER_ML_v1.0.0",
  "decision_path": "string (e.g., ML_FEASIBILITY_PASS → ROLE_ENGINE_ASSIGN → TIMER_DISPATCH)",
  "confidence_score": "0.0–1.0",
  "anomaly_flags": ["ARRAY of flag strings, empty if none"],
  "audit_reference": "UUID"
}
```

**Logs must be:**
- Appended to append-only audit store (no update, no delete)
- Replicated to secondary audit sink within 30 seconds
- Retained per tenant compliance policy (minimum 90 days)
- Exportable for enterprise LMS / HRIS audit export (SECTION T18)

---

## 9️⃣ FAILURE POLICY

```
FAILURE: INVALID INPUT
  ACTION: STOP_EXECUTION
  RESPONSE: HTTP 422 with structured validation error
  LOG: LOG_INCIDENT to AUDIT_LOG_AGENT
  ESCALATE_TO: none (caller responsible)
  RETRY_POLICY: no retry (client must fix input)

FAILURE: MODEL UNAVAILABLE (ML service down)
  ACTION: STOP_EXECUTION (do not fall back to random assignment)
  LOG: LOG_INCIDENT
  ESCALATE_TO: OBSERVABILITY_AGENT → PagerDuty alert
  RETRY_POLICY: 3 retries with 2s exponential backoff, then DLQ

FAILURE: AI TIMEOUT (AI assist call exceeds 5000ms)
  ACTION: Proceed without AI assist (ML decision only)
  LOG: LOG_INCIDENT with latency metric
  ESCALATE_TO: OBSERVABILITY_AGENT (non-critical alert)
  RETRY_POLICY: No retry on AI; ML result used as primary

FAILURE: DATA CORRUPTION (session state inconsistency detected)
  ACTION: STOP_EXECUTION immediately
  LOG: LOG_INCIDENT with session_id, inconsistency description
  ESCALATE_TO: PLATFORM_ADMIN + OBSERVABILITY_AGENT
  RETRY_POLICY: No auto-retry; requires manual investigation

FAILURE: CONFIDENCE BELOW THRESHOLD (confidence_score < 0.75)
  ACTION: STOP_EXECUTION of automatic session launch
  RESPONSE: Return advisory result with escalation flag set
  LOG: LOG_INCIDENT
  ESCALATE_TO: MENTOR (if present) or TENANT_ADMIN
  RETRY_POLICY: Human review required before resubmit

FAILURE: QUOTA EXCEEDED (tenant GD quota for billing period)
  ACTION: STOP_EXECUTION
  RESPONSE: HTTP 402 with quota_exceeded error and current usage
  LOG: LOG_INCIDENT (non-security)
  ESCALATE_TO: BILLING_SERVICE_AGENT for upgrade prompt
  RETRY_POLICY: No retry until quota resets or plan upgraded

FAILURE: SECURITY VIOLATION (cross-tenant, domain leak)
  ACTION: STOP_EXECUTION immediately
  RESPONSE: HTTP 403 — no detail exposed to caller
  LOG: LOG_INCIDENT (SECURITY_VIOLATION event_type, full detail)
  ESCALATE_TO: PLATFORM_COMPLIANCE_ADMIN
  RETRY_POLICY: No retry; access suspended pending review
```

**No silent failures. Every failure must produce an audit record.**

---

## 🔟 INTER-AGENT DEPENDENCY MAP

### Event Flow: Session Creation

```
TOPIC_CREATION_AGENT
  → emits: TOPIC_READY event (topic_id, tenant_id, domain_track)
  → consumed by: GD_SESSION_SCHEDULER_AGENT

PARTICIPANT_POOL_AGENT
  → emits: PARTICIPANT_JOIN event (user_id, topic_id, tenant_id)
  → consumed by: GD_SESSION_SCHEDULER_AGENT

BILLING_CHECK_MIDDLEWARE
  → consumed by: GD_SESSION_SCHEDULER_AGENT (quota check on schedule request)

TENANT_PLAN_AGENT
  → emits: PLAN_CONFIG (gd_quota, mode_permissions)
  → consumed by: GD_SESSION_SCHEDULER_AGENT
```

### Event Flow: Session Lifecycle Dispatch

```
GD_SESSION_SCHEDULER_AGENT
  → emits to TIMER_SERVICE_AGENT:         PREP_TIMER_START (session_id, duration_seconds)
  → emits to DOJO_MATCH_ENGINE_AGENT:     SESSION_READY (session_id, group_id, role_manifest)
  → emits to VIDEO_ROOM_AGENT:            CREATE_ROOM (session_id, mode, participant_tokens) [VOICE/VIDEO only]
  → emits to PARTICIPANT_NOTIFY_AGENT:    JOIN_SIGNAL (session_id, user_id, role, prep_end_utc)
  → emits to FEATURE_STORE_AGENT:         EMIT_BEHAVIORAL_VECTOR (below)
  → emits to AUDIT_LOG_AGENT:             SESSION_CREATED audit record
```

### Event Flow: Phase Transitions

```
TIMER_SERVICE_AGENT
  → emits: PREP_TIMER_EXPIRED (session_id)
  → consumed by: GD_SESSION_SCHEDULER_AGENT
  → GD_SESSION_SCHEDULER_AGENT emits: DISCUSSION_START to DOJO_MATCH_ENGINE_AGENT

DOJO_MATCH_ENGINE_AGENT
  → emits: PHASE_TRANSITION_COMMAND (session_id, next_phase)
  → consumed by: GD_SESSION_SCHEDULER_AGENT (for phase audit logging)

DOJO_MATCH_ENGINE_AGENT
  → emits: SESSION_END (session_id, completion_status)
  → consumed by: GD_SESSION_SCHEDULER_AGENT
  → GD_SESSION_SCHEDULER_AGENT emits: SESSION_COMPLETED to:
      SCORING_SERVICE_AGENT
      PASSPORT_UPDATE_AGENT
      XP_REWARD_AGENT
      LEADERBOARD_AGENT
      ANALYTICS_ENGINE_AGENT
      AUDIT_LOG_AGENT
```

### EVENT_TRIGGERS (Kafka Topics)

```
INBOUND TOPICS:
  - gd.topic.ready
  - gd.participant.join
  - gd.timer.prep_expired
  - gd.timer.discussion_expired
  - gd.match.session_end
  - gd.billing.quota_check_response

OUTBOUND TOPICS:
  - gd.session.created
  - gd.session.roles_assigned
  - gd.session.prep_started
  - gd.session.discussion_started
  - gd.session.phase_transition
  - gd.session.completed
  - gd.session.failed
  - gd.session.cancelled
  - gd.session.security_violation
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent touches user behavior. It **must** emit structured feature vectors to FEATURE_STORE_AGENT after every session lifecycle event:

```json
EMIT_FEATURE_VECTOR:
{
  "user_id": "UUID",
  "feature_name": "gd_session_participation | gd_role_completed | gd_prep_time_used_ratio | gd_session_mode",
  "feature_value": "numeric or encoded",
  "timestamp": "ISO-8601 UTC",
  "source_agent": "GD_SESSION_SCHEDULER_AGENT",
  "session_id": "UUID",
  "topic_id": "UUID",
  "domain_track": "ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION"
}
```

Features emitted per session per user:
- `gd_session_participation` (1 = joined, 0 = dropped)
- `gd_role_completed` (1 = role actions completed, 0 = incomplete)
- `gd_prep_time_used_ratio` (float: 0.0–1.0)
- `gd_session_mode` (TEXT=0 / VOICE=1 / VIDEO=2)
- `gd_role_assigned` (encoded: MODERATOR=0 / SPEAKER=1 / IDEA_GENERATOR=2 / NOTE_KEEPER=3 / QUESTIONER=4)
- `gd_discussion_phase_completion` (float: phases completed / total phases)

These features feed the Passive Intelligence Layer and Skill Gap Analysis Engine.

---

## 1️⃣2️⃣ GD SESSION SCHEDULER — CORE FUNCTIONAL SPECIFICATION

### 12.1 GD Types Supported

```
TYPE 1 — TEACHER_CREATED
  Creator: TEACHER role
  Authorization: Domain-matched tenant teacher
  Quota: Counts against tenant GD quota

TYPE 2 — STUDENT_CREATED
  Creator: STUDENT role
  Authorization: Plan must include student-creation flag
  Quota: Counts against tenant GD quota

TYPE 3 — SYSTEM_GENERATED
  Creator: SYSTEM (auto-triggered by Dojo curriculum engine)
  Authorization: Platform-level
  Quota: Counts against tenant GD quota
```

### 12.2 Topic Lifecycle Integration

```
TOPIC RECORD (topics table):
  topic_id          UUID PK
  tenant_id         UUID (FK, indexed)
  domain_track      ENUM
  title             VARCHAR(500)
  description       TEXT
  group_size        INTEGER (2–10)
  prep_time_seconds INTEGER (300–1800)
  discussion_time_seconds INTEGER (600–5400)
  gd_type           ENUM
  creator_id        UUID
  creator_role      ENUM
  session_mode      ENUM
  status            ENUM: DRAFT | ACTIVE | ARCHIVED
  created_at        TIMESTAMPTZ
  version           INTEGER (add-only mutation)
```

Topic must be ACTIVE status before scheduling. DRAFT topics cannot be scheduled. ARCHIVED topics cannot be scheduled.

### 12.3 Participant Pool Management

```
JOINING RULES:
  - User must be enrolled in same domain_track as topic
  - User must belong to same tenant
  - Pool capacity = group_size (strict maximum)
  - Waitlist supported (up to group_size additional users)
  - If waitlist participant replaces a dropout before prep_start: allowed
  - After prep_start: no new joins permitted

POOL STATUS TRANSITIONS:
  OPEN → FULL (when group_size reached) → LOCKED (when prep timer starts)
```

### 12.4 Role Assignment Engine

```
ROLE TYPES:
  MODERATOR       — Controls discussion flow
  SPEAKER         — Presents and summarizes ideas
  IDEA_GENERATOR  — Proposes solutions
  NOTE_KEEPER     — Records discussion points
  QUESTIONER      — Challenges ideas presented

ASSIGNMENT ALGORITHM:
  1. Retrieve role_rotation_index per user from ROLE_ROTATION_STATE_AGENT
  2. Sort users by role_rotation_index ascending (lowest index = highest priority for new role)
  3. Assign roles in fixed order: MODERATOR → SPEAKER → IDEA_GENERATOR → NOTE_KEEPER → QUESTIONER
  4. Increment role_rotation_index for all assigned users in ROLE_ROTATION_STATE_AGENT
  5. For groups < 5: roles merged (NOTE_KEEPER + QUESTIONER → QUESTIONER-KEEPER hybrid)
  6. For groups > 5: duplicate IDEA_GENERATOR / NOTE_KEEPER roles assigned

FAIRNESS CONSTRAINTS:
  - No user assigned MODERATOR in two consecutive sessions (enforced)
  - Repeat opponent pair avoidance: flag if same two users matched > 2 times in 30 days
  - First-speaker rotation across sessions

AUTO-PROMOTION: FORBIDDEN — no automatic role escalation during session
```

### 12.5 Timer & Phase Transition System

```
PHASE SEQUENCE (SYSTEM-ENFORCED — NOT USER-CONTROLLED):

  PREPARATION PHASE
    Duration: prep_time_seconds (configured per topic)
    Countdown visible to all participants
    Allowed actions: Write notes, save ideas, draw diagrams (stored locally)
    On expiry: AUTO → DISCUSSION_START (zero manual step)

  DISCUSSION PHASE
    Phase 1 — OPENING        (system_allocated: 10% of discussion_time)
      Actor: MODERATOR introduces topic
    Phase 2 — IDEA_GENERATION (system_allocated: 40% of discussion_time)
      Actor: IDEA_GENERATOR role primary
    Phase 3 — DEBATE          (system_allocated: 25% of discussion_time)
      Actor: QUESTIONER challenges
    Phase 4 — SUMMARY         (system_allocated: 25% of discussion_time)
      Actor: SPEAKER summarizes

  Phase transitions: AUTOMATIC via TIMER_SERVICE_AGENT
  Manual phase override: MENTOR ONLY, logged as immutable override audit record
  User phase control: FORBIDDEN

TIMER DISPATCH CONTRACT:
  Agent emits to TIMER_SERVICE_AGENT:
  {
    session_id,
    phase: "PREPARATION | OPENING | IDEA_GENERATION | DEBATE | SUMMARY",
    duration_seconds: integer,
    on_expiry_event: "ENUM of next event to emit"
  }
```

### 12.6 Session Mode Handling

```
TEXT GD (55% of sessions — primary volume):
  - WebSocket only
  - No WebRTC
  - Offline school server mode supported (SQLite local + PostgreSQL sync)
  - Messages stored locally if offline; uploaded when internet available

VOICE GD (40% of sessions):
  - WebRTC audio via LiveKit
  - Signed room token (session_duration + 10min TTL)
  - Room = session_id
  - Participant metadata = role binding
  - Text fallback if WebRTC fails

VIDEO GD (5% of sessions — plan-gated):
  - WebRTC audio+video via LiveKit SFU model
  - Signed room token
  - Room = session_id
  - Recording consent must be captured before room creation
  - Recording trigger: → VIDEO_ROOM_AGENT on session start
  - Plan check: VIDEO mode requires tenant plan with video_gd_flag = true

TRANSPORT CHANNEL SEPARATION (IMMUTABLE):
  Video       → WebRTC
  Game Events → WebSocket
  Commands    → Mentor Socket
  Analytics   → Event Bus
  No channel mixing. Architecture locked.
```

### 12.7 Automatic Group Creation Contract

```
TRIGGER CONDITIONS (ALL must be true):
  1. Participant pool status = FULL (group_size reached)
  2. OR scheduled_start_utc reached (whichever first)
  3. Billing quota check passed
  4. Domain isolation check passed
  5. Security checks passed
  6. ML feasibility_score >= 0.75

ON TRIGGER:
  1. Create discussion_groups record
  2. Lock participant pool (no further joins)
  3. Generate role manifest (Role Assignment Engine)
  4. Persist group_members records with role assignments
  5. Emit SESSION_READY to DOJO_MATCH_ENGINE_AGENT
  6. Emit PREP_TIMER_START to TIMER_SERVICE_AGENT
  7. Emit CREATE_ROOM to VIDEO_ROOM_AGENT (if VOICE/VIDEO)
  8. Emit JOIN_SIGNAL to PARTICIPANT_NOTIFY_AGENT
  9. Append SESSION_CREATED audit record
  10. Emit BEHAVIORAL_FEATURE_VECTOR to FEATURE_STORE_AGENT

ZERO MANUAL STEP after trigger confirmation.
```

### 12.8 Structured Discussion Phase Guard

The agent must enforce phase guards:

```
PHASE GUARD RULES:
  - IDEA_GENERATOR role actions only accepted during IDEA_GENERATION phase
  - QUESTIONER role challenges only accepted during DEBATE phase
  - SPEAKER summary only accepted during SUMMARY phase
  - MODERATOR actions accepted in all phases (limited commands)
  - Out-of-phase actions: REJECTED with structured error, not silently dropped
  - Phase guard violations logged to AUDIT_LOG_AGENT
```

### 12.9 Session Completion & Downstream Triggers

```
ON SESSION_END (received from DOJO_MATCH_ENGINE_AGENT):
  1. Set session_status = COMPLETED (or FAILED if incomplete)
  2. Emit to SCORING_SERVICE_AGENT:
       { session_id, group_id, role_manifest, participation_metrics, mentor_score if present }
  3. Emit to PASSPORT_UPDATE_AGENT:
       { user_id[], intelligence_deltas: { interpersonal, linguistic, leadership } }
  4. Emit XP_UPDATE_EVENT to XP_REWARD_AGENT per user:
       { JOIN=10 XP, ROLE_COMPLETE=20 XP, TOP_PARTICIPANT=30 XP }
  5. Emit to LEADERBOARD_AGENT: SESSION_COMPLETED signal
  6. Emit RANK_UPDATE_EVENT if applicable
  7. Emit SHARE_TRIGGER_EVENT for social sharing card generation
  8. Append SESSION_COMPLETED audit record (immutable)
  9. Emit final BEHAVIORAL_FEATURE_VECTOR per participant

XP TABLE (IMMUTABLE — change requires version bump):
  Action                     XP
  Join session               10
  Complete assigned role     20
  Top participant (ML-ranked) 30
```

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

This agent affects ranking and achievement. It **must** trigger:

```
RANK_UPDATE_EVENT:
  { user_id, session_id, domain_track, timestamp_utc, source_agent: GD_SESSION_SCHEDULER_AGENT }

XP_UPDATE_EVENT:
  { user_id, xp_delta, reason: ENUM, session_id, timestamp_utc }

SHARE_TRIGGER_EVENT:
  { user_id, achievement_type: GD_COMPLETED | GD_TOP_PARTICIPANT | GD_ROLE_MASTERED,
    session_id, share_card_template: ENUM, timestamp_utc }
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

```
METRICS (all emitted to OBSERVABILITY_AGENT):
  - session_create_success_rate       (target: >99.5%)
  - session_create_error_rate         (alert: >0.5%)
  - session_create_latency_p95        (target: <500ms)
  - role_assignment_latency_p95       (target: <200ms)
  - phase_transition_latency_p95      (target: <100ms)
  - ml_feasibility_score_distribution (drift indicator)
  - quota_exceeded_rate_per_tenant    (billing health)
  - security_violation_count          (zero tolerance)
  - offline_sync_lag_seconds          (target: <300s)
  - anomaly_flag_frequency            (week-over-week)

DASHBOARDS (mandatory on OBSERVABILITY_AGENT):
  - session_failure_rate
  - room_crash_rate (VOICE/VIDEO)
  - role_assignment_errors
  - phase_guard_violation_frequency
  - DLQ depth (gd.session.schedule.dlq)

ALERTS:
  - Session create failure rate > 1% over 5 min → PagerDuty P2
  - Security violation detected → PagerDuty P1 (immediate)
  - ML model drift > 5% accuracy drop → PagerDuty P2
  - DLQ depth > 100 → PagerDuty P2
  - Quota exceeded surge (>10 tenants simultaneously) → PagerDuty P2
```

---

## 1️⃣5️⃣ DATABASE SCHEMA (LOCKED — ADD-ONLY MUTATION)

```sql
-- CORE TABLES (entity names immutable per DATA MODEL FREEZE)

-- topics (extend only, never rename fields)
CREATE TABLE topics (
  topic_id              UUID PRIMARY KEY,
  tenant_id             UUID NOT NULL,
  domain_track          VARCHAR(50) NOT NULL,
  title                 VARCHAR(500) NOT NULL,
  description           TEXT,
  group_size            INTEGER NOT NULL CHECK (group_size BETWEEN 2 AND 10),
  prep_time_seconds     INTEGER NOT NULL CHECK (prep_time_seconds BETWEEN 300 AND 1800),
  discussion_time_seconds INTEGER NOT NULL CHECK (discussion_time_seconds BETWEEN 600 AND 5400),
  gd_type               VARCHAR(30) NOT NULL,
  creator_id            UUID NOT NULL,
  creator_role          VARCHAR(30) NOT NULL,
  session_mode          VARCHAR(10) NOT NULL,
  status                VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
  created_at            TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version               INTEGER NOT NULL DEFAULT 1
);

-- gd_sessions
CREATE TABLE gd_sessions (
  session_id            UUID PRIMARY KEY,
  topic_id              UUID NOT NULL REFERENCES topics(topic_id),
  tenant_id             UUID NOT NULL,
  group_id              UUID NOT NULL,
  session_status        VARCHAR(30) NOT NULL,
  scheduled_start_utc   TIMESTAMPTZ,
  prep_start_utc        TIMESTAMPTZ,
  prep_end_utc          TIMESTAMPTZ,
  discussion_start_utc  TIMESTAMPTZ,
  discussion_end_utc    TIMESTAMPTZ,
  current_phase         VARCHAR(30),
  ml_feasibility_score  DECIMAL(4,3),
  model_version         VARCHAR(100) NOT NULL,
  audit_reference       UUID NOT NULL,
  created_at            TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version               INTEGER NOT NULL DEFAULT 1
);

-- discussion_groups
CREATE TABLE discussion_groups (
  group_id              UUID PRIMARY KEY,
  session_id            UUID NOT NULL REFERENCES gd_sessions(session_id),
  topic_id              UUID NOT NULL,
  tenant_id             UUID NOT NULL,
  created_at            TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- group_members
CREATE TABLE group_members (
  id                    UUID PRIMARY KEY,
  group_id              UUID NOT NULL REFERENCES discussion_groups(group_id),
  user_id               UUID NOT NULL,
  tenant_id             UUID NOT NULL,
  assigned_role         VARCHAR(30) NOT NULL,
  role_rotation_index   INTEGER NOT NULL,
  joined_at             TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- gd_messages
CREATE TABLE gd_messages (
  message_id            UUID PRIMARY KEY,
  session_id            UUID NOT NULL,
  group_id              UUID NOT NULL,
  user_id               UUID NOT NULL,
  tenant_id             UUID NOT NULL,
  phase                 VARCHAR(30) NOT NULL,
  message_type          VARCHAR(20) NOT NULL,
  content_hash          VARCHAR(64) NOT NULL,
  created_at            TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- gd_audit_log (APPEND-ONLY — no update, no delete)
CREATE TABLE gd_audit_log (
  audit_id              UUID PRIMARY KEY,
  timestamp_utc         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  actor_id              UUID NOT NULL,
  actor_role            VARCHAR(30) NOT NULL,
  tenant_id             UUID NOT NULL,
  session_id            UUID,
  event_type            VARCHAR(50) NOT NULL,
  input_hash            VARCHAR(64),
  output_hash           VARCHAR(64),
  model_version         VARCHAR(100),
  decision_path         TEXT,
  confidence_score      DECIMAL(4,3),
  anomaly_flags         TEXT[]
);

-- ROW LEVEL SECURITY (all tables)
ALTER TABLE topics ENABLE ROW LEVEL SECURITY;
ALTER TABLE gd_sessions ENABLE ROW LEVEL SECURITY;
ALTER TABLE discussion_groups ENABLE ROW LEVEL SECURITY;
ALTER TABLE group_members ENABLE ROW LEVEL SECURITY;
ALTER TABLE gd_messages ENABLE ROW LEVEL SECURITY;
-- gd_audit_log: no RLS (platform-wide compliance view), access controlled at API layer
```

**Offline Storage (School Server Mode):**
```
SQLite local (student device):
  - topics (local cache)
  - gd_messages (queued, pending sync)
  - group_members (local role reference)
  - sync_queue (pending uploads)

PostgreSQL lite (school server):
  - Mirror of above; syncs to cloud when internet available
  - OFFLINE_SYNC_AGENT handles upload queue
```

---

## 1️⃣6️⃣ MICROSERVICES REQUIRED (MINIMUM 5)

```
SERVICE                   RESPONSIBILITY
Discussion Service        Session lifecycle management, group creation, phase orchestration
Topic Service             Topic CRUD, validation, domain isolation
Timer Service             Phase timers, countdown events, expiry dispatch
Role Engine               Role assignment, rotation state, fairness enforcement
Scoring Service           Post-session evaluation trigger, ML scoring input
```

Additional services consumed (not owned by this agent):
- Video Room Service (LiveKit integration)
- Participant Notify Service (push notifications)
- Audit Log Service (append-only sink)
- Offline Sync Service (school mode)
- Billing Check Middleware

---

## 1️⃣7️⃣ VERSIONING POLICY

```
VERSION FORMAT: GD_SCHEDULER_ML_v{MAJOR}.{MINOR}.{PATCH}
CURRENT VERSION: v1.0.0

ALL CHANGES:
  - Add-only
  - Versioned with version bump
  - Backward compatible for minimum 60 days
  - Migration documented before deploy
  - Rollback-safe (prior version retained)

CHANGES REQUIRING MAJOR VERSION BUMP:
  - Role assignment algorithm change
  - Phase timing formula change
  - Quota enforcement logic change
  - Security model change
  - DB schema field rename or removal (FORBIDDEN without major bump + migration)

CHANGES REQUIRING MINOR VERSION BUMP:
  - New optional input field added
  - New output field added
  - New GD type added
  - New phase type added

PATCH VERSION:
  - Bug fixes, performance improvements
  - No interface change
```

---

## 1️⃣8️⃣ NON-NEGOTIABLE RULES

This agent must **NOT**:

```
❌ Create hidden scheduling logic not defined in this spec
❌ Modify historical session records in any table
❌ Auto-delete or auto-archive audit log entries
❌ Override GOVERNANCE agents, COMPLIANCE agents, or BILLING_CHECK_MIDDLEWARE
❌ Bypass domain isolation checks under any circumstance
❌ Allow cross-tenant participant joins
❌ Auto-launch sessions with ML confidence_score < 0.75 without human escalation
❌ Auto-promote or auto-demote belt/certification status
❌ Allow video GD without recording consent capture
❌ Accept session_mode = VIDEO if tenant plan video_gd_flag = false
❌ Allow role assignment without role_rotation_state check
❌ Execute outside GD scheduling scope
❌ Use AI to make binding scheduling decisions
❌ Mix domain data from different domain_tracks
❌ Mix tenant data in any query
❌ Operate in non-stateless execution (no local in-memory session state)
❌ Emit events without audit_reference
❌ Proceed after SECURITY_VIOLATION — always STOP_EXECUTION
```

---

## 1️⃣9️⃣ OFFLINE SCHOOL MODE COMPLIANCE

```
OFFLINE_MODE_FLAG = true is valid ONLY when session_mode = TEXT

OFFLINE BEHAVIOR:
  1. Session scheduled and roles assigned as normal (requires internet at creation)
  2. Students connect via school WiFi to school server (PostgreSQL lite)
  3. All discussion messages stored locally (SQLite on device)
  4. Timer service runs from school server clock (NTP-synced)
  5. Phase transitions dispatched from school server
  6. On internet restore: OFFLINE_SYNC_AGENT uploads all queued events
  7. Audit trail completed post-sync
  8. Scoring triggers after sync completes

OFFLINE FORBIDDEN ACTIONS:
  - New session creation (requires cloud billing check)
  - Role reassignment after offline sync
  - Video or Voice mode offline sessions
```

---

## 2️⃣0️⃣ PLAN TIER INTEGRATION (GD QUOTA LOCK)

```
PLAN          MONTHLY GD QUOTA    TEXT    VOICE   VIDEO
₹125/month    30 GD               ✅      ✅      ❌
₹199/month    50 GD               ✅      ✅      ❌
₹399/month    70 GD               ✅      ✅      ✅ (limited)
₹599/month    100 GD              ✅      ✅      ✅

GD SESSION DISTRIBUTION EXPECTATION:
  55% TEXT GD    (low infra cost)
  40% VOICE GD   (medium infra cost)
  5%  VIDEO GD   (high infra cost, plan-gated)

Quota enforcement is HARD. No soft limits. No grace quota.
Exceeding quota = STOP_EXECUTION + HTTP 402 response.
Quota resets on billing cycle date, not calendar month.
```

---

## 🔒 FINAL GOVERNANCE SEAL

```
GD_SESSION_SCHEDULER_AGENT PRODUCTION MODE ENABLED

✅ Tenant Isolation:          HARD ENFORCED
✅ Domain Isolation:          HARD ENFORCED
✅ Security Model:            ZERO-TRUST MULTI-TENANT
✅ Execution Mode:            DETERMINISTIC + VALIDATED
✅ Mutation Policy:           ADD-ONLY VERSIONED
✅ Audit Trail:               APPEND-ONLY IMMUTABLE
✅ ML Primary:                70–80% (FEASIBILITY + GROUPING + ROLE ENGINE)
✅ AI Bounded Scope:          20–30% (ADVISORY ONLY — no decision authority)
✅ Offline School Mode:       TEXT ONLY — SUPPORTED
✅ Phase Governance:          SYSTEM-CONTROLLED — USER OVERRIDE FORBIDDEN
✅ Role Rotation:             FAIRNESS ENGINE ACTIVE
✅ Quota Enforcement:         BILLING MIDDLEWARE GATED
✅ Video Consent:             REQUIRED BEFORE ROOM CREATION
✅ Failure Policy:            HALT ON AMBIGUITY — NO SILENT FAILURES
✅ Observability:             FULL TELEMETRY TO OBSERVABILITY_AGENT
✅ Scalability:               HORIZONTAL STATELESS KAFKA-DRIVEN
✅ Backward Compatibility:    60-DAY WINDOW MANDATORY
✅ Interpretation Authority:  NONE
✅ Architecture Authority:    LOCKED

SEALED & LOCKED
ARTIFACT VERSION: v1.0.0
PLATFORM: ECOSKILLER ANTIGRAVITY
DOMAIN: DOJO ENGINE → GROUP DISCUSSION SUBSYSTEM
SEAL DATE: 2026-02-25
```

---

*This document is a sealed production artifact. All modifications require a version bump, migration documentation, and backward compatibility verification. No field may be renamed or removed. No logic may be added without updating this document first. Creative interpretation is forbidden.*
