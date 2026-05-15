# 🔒 GD_ATTENDANCE_TRACKING_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED PRODUCTION ARTIFACT
---

```
ARTIFACT_CLASS          = PRODUCTION SYSTEM AGENT BLUEPRINT
PLATFORM                = Ecoskiller Antigravity
MODULE                  = Dojo Engine → Group Discussion Subsystem → Attendance Intelligence Layer
MUTATION_POLICY         = ADD-ONLY via version bump
EXECUTION_MODE          = DETERMINISTIC + VALIDATED
CREATIVE_INTERPRETATION = FORBIDDEN
ASSUMPTION_FILLING      = FORBIDDEN
DEFAULT_BEHAVIOR        = DENY
FAILURE_MODE            = STOP_EXECUTION
SCHEMA_VERSION          = v1.0.0
SEAL_DATE               = 2026-02-25
SEAL_AUTHORITY          = ECOSKILLER INTELLIGENCE & INNOVATION CORE
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY — NON-NEGOTIABLE)

```
AGENT_NAME              = GD_ATTENDANCE_TRACKING_AGENT
SYSTEM_ROLE             = Real-Time GD Session Attendance Recorder, Presence Validator,
                          Participation Verifier, and Compliance Reporter
PRIMARY_DOMAIN          = Dojo Engine → Group Discussion → Attendance Intelligence Layer
EXECUTION_MODE          = Deterministic + Validated
DATA_SCOPE              = Session Join Events | Exit Events | Reconnect Events |
                          Idle/Inactive Signals | Phase Presence Records |
                          Late Join Records | Early Exit Records | Role Completion Presence |
                          Teacher Score Linkage | Parent Report Feed |
                          ERP Compliance Feed | Audit Trails
TENANT_SCOPE            = Strict Isolation (Institute | Enterprise | Platform)
FAILURE_POLICY          = Halt on Ambiguity
STACK_CONTEXT           = Flutter (operational runtime) + React (SEO web, read-only)
TRANSPORT_CONTEXT       = WebSocket (event stream) | Event Bus (Kafka) | Offline Queue (SQLite)
```

This agent is the **sole source of truth** for who was present in a GD session, when they joined,
when they exited, how long they were active, which phases they were present for, and whether their
attendance qualifies for scoring, XP, belt progression, compliance reports, and parent visibility.

It does **not** score users. It does **not** assign roles. It does **not** manage timers.
It **records, validates, classifies, and reports attendance only.**

**Interpretation Authority: NONE**
**Architecture Authority: LOCKED**

---

## 2️⃣ PURPOSE DECLARATION

### Problem This Agent Solves

GD sessions in the Dojo engine are live, time-bounded, multi-participant events. Attendance in a GD
session is not binary (attended / not attended). It is multi-dimensional:

- **When** did a participant join (before prep, during prep, after discussion started)?
- **Which phases** were they present for (Opening, Idea Generation, Debate, Summary)?
- **Did they disconnect and reconnect?** How many times? For how long?
- **Were they idle/inactive** even while connected (ghost presence)?
- **Did they exit early?** At what phase?
- **Was their attendance valid** for scoring eligibility?
- **Was their attendance valid** for XP eligibility?
- **Was their attendance valid** for belt progression eligibility?

Without this agent, all downstream agents (Scoring, XP, Passport, ERP Reports, Parent Reports,
Belt Engine) would have no verified attendance record to act upon. Downstream decisions made
without verified attendance = integrity failure.

This agent produces an **immutable, per-participant, per-phase, per-session attendance ledger**
that all downstream systems consume as authoritative input.

### What It Consumes (Input Sources)

- Session lifecycle events from GD_SESSION_SCHEDULER_AGENT (session_id, phase sequence, timings)
- Participant join events from WebSocket gateway (real-time)
- Participant exit events from WebSocket gateway (real-time)
- Participant reconnect events from WebSocket gateway (real-time)
- Heartbeat signals from Flutter client (idle/active detection)
- Phase transition events from TIMER_SERVICE_AGENT
- Teacher manual override events (for attendance correction with full audit)
- Offline sync payloads from OFFLINE_SYNC_AGENT (school server mode)
- Tenant plan config from TENANT_PLAN_AGENT (attendance reporting features)

### What It Produces (Output Targets)

- Per-participant attendance status per session (PRESENT | LATE | PARTIAL | ABSENT | GHOST)
- Per-phase presence records (present/absent per discussion phase per user)
- Attendance eligibility verdicts for Scoring Service, XP Engine, Belt Engine
- Session attendance summary for Teacher Dashboard
- Class/cohort attendance aggregates for Institute ERP Layer
- Student attendance records for Parent Report Feed
- Monthly/periodic attendance compliance reports for Admin/Compliance ERP
- Behavioral feature vectors for Passive Intelligence Layer (FEATURE_STORE_AGENT)
- Immutable append-only audit log per attendance event

### Upstream Agent Dependencies

```
UPSTREAM_AGENTS:
  - GD_SESSION_SCHEDULER_AGENT      → Provides session_id, phase_schedule, group_manifest
  - TIMER_SERVICE_AGENT             → Provides phase_start / phase_end events
  - PARTICIPANT_POOL_AGENT          → Provides expected participant list per session
  - DOMAIN_ISOLATION_AGENT          → Validates cross-domain access rules
  - TENANT_PLAN_AGENT               → Provides reporting feature flags per plan
  - OFFLINE_SYNC_AGENT              → Provides queued attendance events from offline sessions
  - ANTI_COLLUSION_DETECTION_AGENT  → Receives ghost presence flags for audit
```

### Downstream Agent Dependencies

```
DOWNSTREAM_AGENTS:
  - SCORING_SERVICE_AGENT           → Consumes attendance_eligibility_verdict
  - XP_REWARD_AGENT                 → Consumes xp_eligibility_verdict per event
  - PASSPORT_UPDATE_AGENT           → Consumes attendance_record (presence duration, phases)
  - BELT_ENGINE_AGENT               → Consumes validated_session_attendance_flag
  - LEADERBOARD_AGENT               → Consumes qualified_participant list
  - TEACHER_DASHBOARD_AGENT         → Consumes session_attendance_summary
  - INSTITUTE_ERP_AGENT             → Consumes cohort_attendance_aggregate
  - PARENT_REPORT_AGENT             → Consumes student_attendance_record
  - COMPLIANCE_ADMIN_AGENT          → Consumes periodic_compliance_attendance_report
  - FEATURE_STORE_AGENT             → Consumes behavioral feature vectors
  - OBSERVABILITY_AGENT             → Consumes all health metrics
  - AUDIT_LOG_AGENT                 → Consumes immutable attendance event log entries
  - ANTI_COLLUSION_DETECTION_AGENT  → Consumes ghost_presence_flag and inactivity_signals
```

---

## 3️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA — PARTICIPANT JOIN EVENT:
{
  "required_fields": [
    "event_type",
    "session_id",
    "user_id",
    "tenant_id",
    "join_timestamp_utc",
    "client_type",
    "network_mode"
  ],
  "optional_fields": [
    "device_id",
    "reconnect_flag",
    "reconnect_attempt_number",
    "offline_queued_flag",
    "offline_queued_at_utc"
  ],
  "validation_rules": [
    "event_type must be: PARTICIPANT_JOIN | PARTICIPANT_EXIT | PARTICIPANT_RECONNECT | HEARTBEAT | IDLE_DETECTED | TEACHER_OVERRIDE",
    "session_id must exist in gd_sessions table for same tenant_id with status IN (PREPARATION, DISCUSSION, CLOSING)",
    "user_id must be in group_members for this session_id",
    "join_timestamp_utc must be ISO-8601 UTC and >= session prep_start_utc",
    "join_timestamp_utc must be <= session discussion_end_utc + 60 seconds grace",
    "client_type must be: FLUTTER_MOBILE | FLUTTER_DESKTOP | FLUTTER_TABLET",
    "network_mode must be: ONLINE | OFFLINE_LOCAL",
    "reconnect_flag if true requires reconnect_attempt_number >= 1",
    "offline_queued_flag if true requires offline_queued_at_utc to be present",
    "TEACHER_OVERRIDE event_type requires teacher_id, override_reason, and override_type fields"
  ],
  "security_checks": [
    "JWT token must be valid and not expired",
    "user_id must match JWT sub claim exactly",
    "tenant_id must match JWT tenant claim exactly",
    "session_id must belong to tenant_id (tenant isolation hard check)",
    "user_id must be in group_members for this session_id (no uninvited joins)",
    "TEACHER_OVERRIDE requires teacher_role JWT claim and mentor certification check"
  ],
  "domain_checks": [
    "user domain_track must match session domain_track",
    "no cross-tenant attendance event allowed"
  ]
}

INPUT_SCHEMA — HEARTBEAT EVENT:
{
  "required_fields": [
    "event_type",
    "session_id",
    "user_id",
    "tenant_id",
    "heartbeat_timestamp_utc",
    "activity_signal"
  ],
  "optional_fields": [
    "last_action_timestamp_utc",
    "messages_sent_since_last_heartbeat",
    "ideas_submitted_since_last_heartbeat"
  ],
  "validation_rules": [
    "event_type must be: HEARTBEAT",
    "activity_signal must be: ACTIVE | IDLE",
    "heartbeat_timestamp_utc must be within 60 seconds of server receive time",
    "HEARTBEAT must only be accepted for sessions with status IN (PREPARATION, DISCUSSION)",
    "heartbeat_interval from client: every 30 seconds (server enforced)"
  ]
}

INPUT_SCHEMA — TEACHER OVERRIDE EVENT:
{
  "required_fields": [
    "event_type",
    "session_id",
    "tenant_id",
    "teacher_id",
    "target_user_id",
    "override_type",
    "override_reason",
    "override_timestamp_utc"
  ],
  "optional_fields": [
    "override_notes"
  ],
  "validation_rules": [
    "override_type must be: MARK_PRESENT | MARK_ABSENT | MARK_LATE | MARK_PARTIAL | EXCUSE_ABSENCE",
    "override_reason must be non-empty string (min 10 chars)",
    "teacher_id must have TEACHER or MENTOR role in this tenant and session domain",
    "target_user_id must be in group_members for this session_id",
    "override is only permitted after session status = COMPLETED or CLOSING",
    "no override permitted on SYSTEM_GENERATED attendance within 24h of session (requires ADMIN)"
  ]
}
```

**Null Tolerance Policy:** ZERO null tolerance on required fields. Immediate rejection + LOG_INCIDENT.

**Heartbeat Loss Policy:** If no heartbeat received for > 90 seconds during active session → emit IDLE_SUSPECTED event → ML classifier evaluates ghost presence. Do not mark absent immediately.

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA — ATTENDANCE RECORD (per participant per session):
{
  "result_object": {
    "attendance_record_id": "UUID",
    "session_id": "UUID",
    "user_id": "UUID",
    "tenant_id": "UUID",
    "group_id": "UUID",
    "attendance_status": "ENUM: PRESENT | LATE | PARTIAL | ABSENT | GHOST | EXCUSED",
    "join_timestamp_utc": "ISO-8601 | null",
    "exit_timestamp_utc": "ISO-8601 | null",
    "total_presence_seconds": "integer",
    "presence_percentage": "float (0.0–1.0)",
    "reconnect_count": "integer",
    "total_disconnect_seconds": "integer",
    "idle_periods": [
      {
        "idle_start_utc": "ISO-8601",
        "idle_end_utc": "ISO-8601",
        "idle_duration_seconds": "integer"
      }
    ],
    "phase_presence": {
      "PREPARATION": "PRESENT | ABSENT | PARTIAL",
      "OPENING": "PRESENT | ABSENT | PARTIAL",
      "IDEA_GENERATION": "PRESENT | ABSENT | PARTIAL",
      "DEBATE": "PRESENT | ABSENT | PARTIAL",
      "SUMMARY": "PRESENT | ABSENT | PARTIAL"
    },
    "late_join_flag": "boolean",
    "late_join_delay_seconds": "integer | null",
    "early_exit_flag": "boolean",
    "early_exit_phase": "ENUM | null",
    "ghost_presence_flag": "boolean",
    "ghost_confidence_score": "float 0.0–1.0",
    "teacher_override_applied": "boolean",
    "teacher_override_type": "ENUM | null",
    "teacher_override_reason": "string | null",
    "scoring_eligibility": "ELIGIBLE | INELIGIBLE | PARTIAL_ELIGIBLE",
    "xp_eligibility": "ELIGIBLE | INELIGIBLE",
    "belt_session_credit": "CREDITED | NOT_CREDITED",
    "attendance_source": "ONLINE | OFFLINE_SYNCED | TEACHER_OVERRIDE"
  },
  "confidence_score": "0.0–1.0 (attendance classification confidence)",
  "model_version": "GD_ATTENDANCE_ML_v1.0.0",
  "audit_reference": "UUID",
  "next_trigger_event": [
    "SCORING_SERVICE_AGENT::ATTENDANCE_READY",
    "XP_REWARD_AGENT::XP_ELIGIBILITY_SIGNAL",
    "BELT_ENGINE_AGENT::SESSION_CREDIT_SIGNAL",
    "PARENT_REPORT_AGENT::ATTENDANCE_UPDATE",
    "TEACHER_DASHBOARD_AGENT::SESSION_SUMMARY_UPDATE",
    "FEATURE_STORE_AGENT::EMIT_BEHAVIORAL_VECTOR",
    "ANTI_COLLUSION_DETECTION_AGENT::GHOST_FLAG_IF_SET"
  ]
}

OUTPUT_SCHEMA — SESSION ATTENDANCE SUMMARY (per session, for Teacher/ERP):
{
  "session_id": "UUID",
  "tenant_id": "UUID",
  "group_id": "UUID",
  "total_expected_participants": "integer",
  "present_count": "integer",
  "late_count": "integer",
  "partial_count": "integer",
  "absent_count": "integer",
  "ghost_count": "integer",
  "excused_count": "integer",
  "attendance_rate": "float (0.0–1.0)",
  "avg_presence_percentage": "float",
  "phase_attendance_breakdown": {
    "PREPARATION": { "present": "integer", "absent": "integer" },
    "OPENING": { "present": "integer", "absent": "integer" },
    "IDEA_GENERATION": { "present": "integer", "absent": "integer" },
    "DEBATE": { "present": "integer", "absent": "integer" },
    "SUMMARY": { "present": "integer", "absent": "integer" }
  },
  "session_integrity_flag": "CLEAN | ANOMALY_DETECTED",
  "model_version": "GD_ATTENDANCE_ML_v1.0.0",
  "audit_reference": "UUID"
}
```

**All outputs MUST include:** confidence_score, model_version, audit_reference, next_trigger_event.

---

## 5️⃣ ATTENDANCE STATUS CLASSIFICATION DEFINITIONS (LOCKED)

These definitions are **immutable**. Changes require a MAJOR version bump.

```
PRESENT:
  - Joined before or during PREPARATION phase
  - Presence percentage >= 80% of total session duration
  - No ghost_presence_flag
  - No early_exit_flag during IDEA_GENERATION or DEBATE phase

LATE:
  - Joined after PREPARATION phase ended (i.e., during OPENING or later)
  - Joined within first 25% of discussion_time
  - Presence percentage >= 70% of remaining session from join point
  - No ghost_presence_flag

PARTIAL:
  - Joined at any point
  - Presence percentage between 40% and 79% of total session duration
  - OR: joined on time but exited before SUMMARY phase
  - OR: reconnect_count >= 3 with total_disconnect_seconds > 20% of session duration

ABSENT:
  - Never joined the session
  - OR: joined but presence percentage < 40% of total session duration
  - OR: joined but immediately disconnected (presence < 120 seconds)

GHOST:
  - Connected (WebSocket active) but idle_duration_seconds > 60% of their total connected duration
  - ML ghost_confidence_score >= 0.80
  - No messages sent, no ideas submitted, no role actions taken across all phases
  - System flags but does NOT auto-mark absent — escalates to Teacher for review

EXCUSED:
  - Teacher override applied with override_type = EXCUSE_ABSENCE
  - Requires override_reason (minimum 10 chars)
  - Treated as PARTIAL for scoring purposes unless teacher marks PRESENT override
```

---

## 6️⃣ ML / AI LOGIC LAYER

This agent is **ML-primary (70–80%)** with **AI-assist (20–30%, strictly bounded)**.

### ML Logic (Primary)

```
MODEL 1 — ATTENDANCE STATUS CLASSIFIER
  Model Type:   Random Forest / Gradient Boosting (XGBoost)
  Purpose:      Classify final attendance status per participant
  Features:
    - join_delay_seconds (from prep_start)
    - total_presence_seconds
    - presence_percentage
    - reconnect_count
    - total_disconnect_seconds
    - idle_periods_count
    - total_idle_seconds
    - idle_percentage
    - messages_sent (count)
    - ideas_submitted (count)
    - role_actions_taken (count)
    - phases_present (bitmask: PREPARATION|OPENING|IDEA_GENERATION|DEBATE|SUMMARY)
    - early_exit_phase_index (int: 0=no exit, 1-5=phase exited)
    - session_mode (TEXT=0 / VOICE=1 / VIDEO=2)
    - network_mode (ONLINE=0 / OFFLINE=1)
    - reconnect_pattern_variance (float)
  Output:       PRESENT | LATE | PARTIAL | ABSENT | GHOST (confidence 0.0–1.0)
  Threshold:    confidence < 0.75 → escalate to TEACHER for review

MODEL 2 — GHOST PRESENCE DETECTOR
  Model Type:   Logistic Regression + Anomaly Score (Isolation Forest)
  Purpose:      Detect participants connected but behaviorally absent
  Features:
    - idle_percentage (total_idle_seconds / total_connected_seconds)
    - messages_sent_per_minute
    - ideas_submitted (binary: 0/1)
    - role_actions_completed (binary: 0/1)
    - heartbeat_regularity_score (variance in heartbeat intervals)
    - response_lag_average_ms
    - activity_burst_pattern (sparse vs distributed)
  Output:       ghost_presence_flag (boolean), ghost_confidence_score (0.0–1.0)
  Alert Threshold: ghost_confidence_score >= 0.80

MODEL 3 — LATE JOIN IMPACT CLASSIFIER
  Model Type:   Decision Tree / Rule Engine (lightweight)
  Purpose:      Determine if late join affects scoring/XP eligibility
  Features:
    - late_join_delay_seconds
    - phases_missed (count from bitmask)
    - presence_percentage_from_join
    - role_completed (boolean)
  Output:       scoring_eligibility (ELIGIBLE / INELIGIBLE / PARTIAL_ELIGIBLE)

MODEL 4 — RETENTION PREDICTION (PASSIVE)
  Model Type:   Logistic Regression / Time-Series
  Purpose:      Predict if participant will complete session (early dropout risk)
  Timing:       Evaluated at 25% and 50% of discussion_time
  Features:
    - messages_sent_so_far
    - phase_completion_rate_so_far
    - idle_count_so_far
    - reconnect_count_so_far
    - historical_gd_dropout_rate (user history)
  Output:       dropout_risk_score (0.0–1.0) — emitted to FEATURE_STORE_AGENT only

TRAINING FREQUENCY:
  - Attendance Status Classifier:  Weekly retrain on prior 30-day session data
  - Ghost Presence Detector:       Weekly retrain
  - Late Join Classifier:          Monthly (rule-stable)
  - Retention Prediction:          Monthly retrain

DRIFT DETECTION:
  - Monitor: distribution shift on presence_percentage, idle_percentage, reconnect_count
  - Monitor: accuracy of attendance_status vs teacher_override_ground_truth
  - Alert: >5% accuracy degradation over 7-day rolling window → ESCALATE to OBSERVABILITY_AGENT

VERSION CONTROL:
  - All models: immutable model_version tag = GD_ATTENDANCE_ML_v{MAJOR}.{MINOR}.{PATCH}
  - Embedded in every output record
  - Rollback-safe: prior version retained 60 days
```

### AI Logic (Bounded Scope — 20–30%)

```
AI_USAGE_SCOPE:
  Scope 1: Ghost Presence Explanation
    When ghost_confidence_score >= 0.80, AI generates a structured human-readable
    explanation for the Teacher review queue (e.g., "Participant was connected for
    18 minutes but sent 0 messages and completed 0 role actions across 4 phases.")
    AI output = advisory text only. Human (Teacher) makes final decision.

  Scope 2: Anomaly Narrative for Compliance Reports
    When session_integrity_flag = ANOMALY_DETECTED, AI generates a structured
    anomaly description for the Compliance ERP report. Purely descriptive. No decisions.

  Scope 3: Attendance Pattern Summarization (Monthly Reports)
    AI summarizes cohort attendance trends into natural language for Institute ERP reports.
    (e.g., "Class 7A average attendance rate dropped 12% in March vs February, driven
    primarily by high dropout rates in DEBATE phase.")
    AI output = report narrative. Not a decision input.

PROMPT_GOVERNANCE:
  - All AI prompts versioned: ATTENDANCE_AI_PROMPT_v1.0.0
  - Deterministic structure. No open-ended generation.
  - AI output treated as ADVISORY only — no binding attendance decision via AI
  - AI must NEVER auto-mark attendance status
  - AI must NEVER override ML classification
  - AI must NEVER emit scoring_eligibility decisions
```

---

## 7️⃣ SCALABILITY DESIGN

```
EXPECTED EVENT VOLUME:
  1M users:   ~6M–7M participant-session events/month
              ~1,400 heartbeat events/second at peak (30s intervals, 42K active sessions)
  10M users:  ~14,000 heartbeat events/second at peak
  100M users: ~140,000 heartbeat events/second at peak

LATENCY TARGETS:
  - Join event acknowledgment:         p95 < 200ms
  - Heartbeat processing:              p95 < 100ms (non-blocking, async)
  - Attendance status classification:  p95 < 500ms post-session
  - Session summary generation:        p95 < 2s post-session
  - Parent report feed update:         p95 < 5s post-session
  - ERP cohort aggregate update:       p95 < 30s post-session (batch acceptable)

MAX_CONCURRENCY:
  - 10,000 simultaneous active sessions → ~80,000 concurrent tracked participants
  - Heartbeat processor: separate horizontally scaled pod cluster

QUEUE_STRATEGY:
  - Kafka topic: gd.attendance.join_events      (partitioned by session_id)
  - Kafka topic: gd.attendance.heartbeat        (partitioned by session_id, high throughput)
  - Kafka topic: gd.attendance.exit_events      (partitioned by session_id)
  - Kafka topic: gd.attendance.teacher_override (partitioned by tenant_id)
  - Kafka topic: gd.attendance.records_ready    (output, partitioned by tenant_id)
  - Dead letter queue: gd.attendance.dlq        (retry-3 then escalate)

STATELESS EXECUTION: ENFORCED
  - No local state in agent memory
  - All session-phase state in Redis (TTL = session_duration + 10 minutes)
  - All persistent records in PostgreSQL

IDEMPOTENCY:
  - Join events: idempotency_key = session_id + user_id + join_timestamp_utc (hash)
  - Duplicate join events within 30s window: deduplicated, return prior result
  - Heartbeat: idempotency not required (fire-and-forget, last-write-wins in Redis)

HEARTBEAT PROCESSING (OPTIMIZED):
  - Heartbeats do NOT hit PostgreSQL directly
  - Heartbeat state maintained in Redis (last_heartbeat_ts, activity_signal, idle_start_ts)
  - Redis state flushed to PostgreSQL in batch at phase transitions and session end
  - This prevents 140K writes/second to PostgreSQL at 100M scale

OFFLINE SESSION HANDLING:
  - Attendance events queued in SQLite on device / school server
  - OFFLINE_SYNC_AGENT uploads after internet restore
  - GD_ATTENDANCE_TRACKING_AGENT processes offline_queued events with
    offline_queued_at_utc as the authoritative timestamp
  - Offline events accepted up to 24 hours after session end
  - After 24 hours: TEACHER_OVERRIDE required to update attendance
```

---

## 8️⃣ SECURITY ENFORCEMENT

```
TENANT ISOLATION:
  - All DB queries scoped by tenant_id at PostgreSQL RLS (row-level security)
  - attendance_records table: RLS enforced on tenant_id
  - Cross-tenant attendance event = SECURITY FAILURE → STOP_EXECUTION + LOG_INCIDENT
  - tenant_id extracted from JWT claims only (never from event payload)

DOMAIN ISOLATION:
  - User domain_track verified at join event against session domain_track
  - Cross-domain attendance rejected (security failure, not validation error)

ROLE-BASED AUTHORIZATION:
  - STUDENT:       can emit own join/exit/heartbeat events
  - TEACHER/MENTOR: can view session attendance summary, submit teacher_override
  - INSTITUTE_ADMIN: can view cohort attendance aggregates for own tenant
  - PLATFORM_ADMIN: full read access, no write except compliance override
  - PARENT:        read-only visibility on own child's attendance records
  - RECRUITER/HR:  no attendance data access (Dojo internal only)
  - EVALUATOR:     read-only on session attendance summaries (audit context)

JWT ENFORCEMENT:
  - Short-lived access tokens (15-minute expiry)
  - Refresh token rotation mandatory
  - All attendance API endpoints require valid JWT
  - user_id in event payload must match JWT sub claim — mismatch = SECURITY FAILURE

HEARTBEAT SECURITY:
  - Heartbeat token = session-scoped short-lived token (session_duration + 5 min TTL)
  - Heartbeats without valid session token: rejected, logged
  - Heartbeat flood detection: >4 heartbeats in 30s window from same user = rate limited

TEACHER OVERRIDE SECURITY:
  - Override requires teacher_role JWT claim
  - Override logged as immutable record with teacher_id, timestamp, reason
  - Override cannot delete prior attendance records — it only APPENDS a correction record
  - Audit trail shows: original ML classification → teacher override → final status
  - Admin-level overrides require ADMIN JWT claim and 2-field reason

PARENT DATA ACCESS:
  - Parent can only view own child's attendance (child relationship must be verified in DB)
  - Parent report feed: attendance_status only (no raw event data exposed to parent)
  - No phase-level detail exposed to parent (summary only)

DATA SECURITY:
  - All PII at rest: AES-256
  - All inter-service: TLS 1.3
  - attendance_records: PostgreSQL RLS enforced
  - Heartbeat Redis store: no PII, session_id + user_id hash only
  - No plaintext secrets (Secret Manager only)

SCREENSHOT/SCREEN RECORDING:
  - Attendance UI on Flutter: sensitive screens disable screenshot capability
  - Teacher override form: screenshot disabled during input
```

---

## 9️⃣ AUDIT & TRACEABILITY

Every attendance event produces an immutable audit record:

```json
AUDIT_RECORD:
{
  "audit_id": "UUID",
  "timestamp_utc": "ISO-8601",
  "actor_id": "UUID (user_id or SYSTEM or teacher_id)",
  "actor_role": "ENUM",
  "tenant_id": "UUID",
  "session_id": "UUID",
  "user_id": "UUID (subject of attendance event)",
  "event_type": "ENUM: PARTICIPANT_JOIN | PARTICIPANT_EXIT | PARTICIPANT_RECONNECT |
                        HEARTBEAT_IDLE_DETECTED | GHOST_FLAGGED | ATTENDANCE_CLASSIFIED |
                        TEACHER_OVERRIDE_APPLIED | OFFLINE_SYNC_PROCESSED |
                        VALIDATION_FAILED | SECURITY_VIOLATION |
                        SCORING_ELIGIBILITY_EMITTED | XP_ELIGIBILITY_EMITTED |
                        BELT_CREDIT_EMITTED | REPORT_GENERATED",
  "input_hash": "SHA-256 of canonical input payload",
  "output_hash": "SHA-256 of canonical output record",
  "model_version": "GD_ATTENDANCE_ML_v1.0.0",
  "decision_path": "string (e.g., ML_GHOST_DETECTOR → GHOST_FLAG=true → TEACHER_REVIEW_QUEUED)",
  "confidence_score": "0.0–1.0",
  "anomaly_flags": ["ARRAY of flag strings"],
  "attendance_status_before_override": "ENUM | null",
  "attendance_status_final": "ENUM",
  "audit_reference": "UUID"
}
```

**Audit Log Rules:**
- Append-only immutable store. No updates. No deletes.
- Replicated to secondary sink within 30 seconds.
- Retained per tenant compliance policy (minimum 90 days).
- Exportable for ERP audit, HRIS, LMS compliance reporting.
- Teacher overrides produce a SEPARATE audit record (not mutation of original).
- The original ML classification record is never overwritten — both coexist in audit log.

---

## 🔟 FAILURE POLICY

```
FAILURE: INVALID JOIN EVENT (validation fail)
  ACTION: STOP_EXECUTION (reject event)
  RESPONSE: HTTP 422 with structured validation error
  LOG: LOG_INCIDENT to AUDIT_LOG_AGENT
  ESCALATE_TO: none (caller responsible)
  RETRY_POLICY: no retry (client must fix payload)

FAILURE: LATE JOIN BEYOND GRACE WINDOW (join_timestamp > discussion_end_utc + 60s)
  ACTION: REJECT JOIN, mark user ABSENT
  RESPONSE: HTTP 409 with SESSION_CLOSED error
  LOG: LOG_INCIDENT
  NOTE: Teacher override permitted post-session if extenuating circumstance

FAILURE: ML MODEL UNAVAILABLE (attendance classifier down)
  ACTION: STOP_EXECUTION of classification
  FALLBACK: Store raw events; defer classification until model restored
  LOG: LOG_INCIDENT
  ESCALATE_TO: OBSERVABILITY_AGENT → PagerDuty P2
  RETRY_POLICY: 3 retries with exponential backoff, then defer queue

FAILURE: HEARTBEAT STREAM BROKEN (>90s gap during active session)
  ACTION: DO NOT mark absent immediately
  ACTION: Emit IDLE_SUSPECTED event to GHOST_PRESENCE_DETECTOR
  ACTION: Continue recording all events received
  ACTION: If heartbeat resumes: update Redis state, clear IDLE_SUSPECTED
  ACTION: If heartbeat never resumes through session end: ML classifies based on available data
  LOG: LOG_INCIDENT (non-critical until session end)
  RETRY_POLICY: Flutter client retries heartbeat up to 3 times with 10s backoff

FAILURE: SECURITY VIOLATION (cross-tenant, JWT mismatch)
  ACTION: STOP_EXECUTION immediately
  RESPONSE: HTTP 403 — no detail exposed
  LOG: LOG_INCIDENT (SECURITY_VIOLATION)
  ESCALATE_TO: PLATFORM_COMPLIANCE_ADMIN immediately
  RETRY_POLICY: No retry. Access suspended pending investigation.

FAILURE: CONFIDENCE BELOW THRESHOLD (confidence_score < 0.75)
  ACTION: DO NOT finalize attendance_status automatically
  ACTION: Route to TEACHER_REVIEW_QUEUE with ML suggestion and confidence score
  LOG: LOG_INCIDENT
  ESCALATE_TO: TEACHER (if session had teacher) or TENANT_ADMIN (system GDs)
  RETRY_POLICY: Teacher review within 48 hours; else auto-classify as PARTIAL

FAILURE: OFFLINE SYNC REJECTED (event timestamp > 24h post session_end)
  ACTION: REJECT offline event
  ACTION: Flag record for TEACHER_OVERRIDE workflow
  LOG: LOG_INCIDENT
  ESCALATE_TO: TEACHER + TENANT_ADMIN notification
  RETRY_POLICY: No retry. Teacher override is the only remediation path.

FAILURE: DATA CORRUPTION (attendance state inconsistency)
  ACTION: STOP_EXECUTION
  ACTION: Freeze session attendance record in INCOMPLETE state
  LOG: LOG_INCIDENT with full state snapshot
  ESCALATE_TO: PLATFORM_ADMIN + OBSERVABILITY_AGENT
  RETRY_POLICY: No auto-retry. Manual investigation required.
```

**No silent failures. Every failure produces an audit record.**

---

## 1️⃣1️⃣ INTER-AGENT DEPENDENCY MAP

### Event Flow: Live Session Tracking

```
WebSocket Gateway
  → emits: PARTICIPANT_JOIN (session_id, user_id, timestamp)
  → consumed by: GD_ATTENDANCE_TRACKING_AGENT

Flutter Client (every 30s)
  → emits: HEARTBEAT (session_id, user_id, activity_signal)
  → consumed by: GD_ATTENDANCE_TRACKING_AGENT → Redis state update

TIMER_SERVICE_AGENT
  → emits: PHASE_TRANSITION (session_id, phase, timestamp)
  → consumed by: GD_ATTENDANCE_TRACKING_AGENT
  → triggers: phase_presence snapshot written to PostgreSQL

WebSocket Gateway
  → emits: PARTICIPANT_EXIT (session_id, user_id, timestamp)
  → consumed by: GD_ATTENDANCE_TRACKING_AGENT
```

### Event Flow: Post-Session Classification

```
GD_SESSION_SCHEDULER_AGENT
  → emits: SESSION_COMPLETED (session_id)
  → consumed by: GD_ATTENDANCE_TRACKING_AGENT
  → triggers: flush Redis → PostgreSQL, run ML classification, emit all downstream events

GD_ATTENDANCE_TRACKING_AGENT
  → emits to SCORING_SERVICE_AGENT:       ATTENDANCE_READY (attendance_eligibility_verdict per user)
  → emits to XP_REWARD_AGENT:             XP_ELIGIBILITY_SIGNAL (per user per XP trigger)
  → emits to BELT_ENGINE_AGENT:           SESSION_CREDIT_SIGNAL (per user)
  → emits to PASSPORT_UPDATE_AGENT:       ATTENDANCE_RECORD (presence_duration, phases_present)
  → emits to TEACHER_DASHBOARD_AGENT:     SESSION_SUMMARY (session_attendance_summary)
  → emits to INSTITUTE_ERP_AGENT:         COHORT_ATTENDANCE_UPDATE
  → emits to PARENT_REPORT_AGENT:         STUDENT_ATTENDANCE_RECORD (per child)
  → emits to FEATURE_STORE_AGENT:         BEHAVIORAL_FEATURE_VECTOR (per user)
  → emits to ANTI_COLLUSION_DETECTION_AGENT: GHOST_FLAG_EVENT (if ghost detected)
  → emits to AUDIT_LOG_AGENT:             ATTENDANCE_CLASSIFIED audit record
```

### EVENT_TRIGGERS (Kafka Topics)

```
INBOUND TOPICS:
  - gd.attendance.join_events
  - gd.attendance.heartbeat
  - gd.attendance.exit_events
  - gd.attendance.teacher_override
  - gd.session.phase_transition       (consumed from TIMER_SERVICE)
  - gd.session.completed              (consumed from GD_SESSION_SCHEDULER)
  - gd.offline.sync_payload           (consumed from OFFLINE_SYNC_AGENT)

OUTBOUND TOPICS:
  - gd.attendance.records_ready       (per-participant final record)
  - gd.attendance.session_summary     (per-session summary)
  - gd.attendance.ghost_flagged
  - gd.attendance.teacher_review_needed
  - gd.attendance.security_violation
  - gd.attendance.report_generated    (ERP, parent, compliance)
```

---

## 1️⃣2️⃣ ATTENDANCE ELIGIBILITY RULES (LOCKED — IMMUTABLE)

Changes to eligibility rules require MAJOR version bump + migration documentation.

### Scoring Eligibility

```
ELIGIBLE:
  - attendance_status IN (PRESENT, LATE)
  - AND presence_percentage >= 0.70
  - AND ghost_presence_flag = false
  - AND phases_present includes IDEA_GENERATION

PARTIAL_ELIGIBLE:
  - attendance_status = PARTIAL
  - AND presence_percentage >= 0.40
  - AND phases_present includes at least 2 of 5 phases
  - Scoring weight applied at 50% of normal weight

INELIGIBLE:
  - attendance_status IN (ABSENT, GHOST)
  - OR presence_percentage < 0.40
  - OR ghost_presence_flag = true with ghost_confidence_score >= 0.80
  - EXCUSED attendance = INELIGIBLE for scoring (no score from excused absence)
```

### XP Eligibility

```
JOIN XP (10 XP):
  - Awarded if: attendance_status IN (PRESENT, LATE, PARTIAL)
  - AND presence_percentage >= 0.40
  - NOT awarded for: ABSENT, GHOST, EXCUSED

ROLE_COMPLETE XP (20 XP):
  - Awarded if: role_actions_completed = true (from SCORING_SERVICE_AGENT confirmation)
  - AND attendance_status = ELIGIBLE for scoring
  - NOT awarded for: PARTIAL_ELIGIBLE or INELIGIBLE

TOP_PARTICIPANT XP (30 XP):
  - Awarded if: ML-ranked top participant by SCORING_SERVICE_AGENT
  - AND attendance_status = PRESENT
  - NOT awarded for: LATE, PARTIAL, GHOST, ABSENT, EXCUSED
```

### Belt Session Credit

```
CREDITED:
  - attendance_status = PRESENT
  - AND presence_percentage >= 0.80
  - AND ghost_presence_flag = false
  - AND session was RANKED type (not practice)
  - Counts toward session count for belt progression

NOT_CREDITED:
  - All other attendance statuses
  - GHOST sessions never credited even if teacher overrides to PRESENT
    (GHOST flag persists in audit record; belt credit requires clean attendance)
```

---

## 1️⃣3️⃣ TEACHER DASHBOARD INTEGRATION

This agent **must** feed the Teacher Dashboard with real-time and post-session attendance data:

### Real-Time Feed (During Session)

```
LIVE ATTENDANCE PANEL (Teacher Dashboard):
  - Shows: participant list with live status indicator (JOINED | NOT YET | DISCONNECTED | IDLE)
  - Updates: every 30 seconds (driven by heartbeat state from Redis)
  - Teacher can see: join time, idle indicators, reconnect count per participant
  - Teacher CAN: flag a participant for review (does NOT auto-change status)
  - Teacher CANNOT: change attendance status during live session (override only post-session)
  - Data source: Redis state (not PostgreSQL — real-time only)
```

### Post-Session Summary

```
SESSION ATTENDANCE SUMMARY (Teacher Dashboard — post session):
  Fields shown:
  - Participant name + attendance_status
  - Presence percentage
  - Phase presence breakdown (which phases attended)
  - Ghost flag (if any) with ML confidence
  - ML classification confidence (if < 0.75: "Review Recommended" badge)
  - Teacher override input: [override_type dropdown] [reason text] [submit]

TEACHER OVERRIDE WORKFLOW:
  1. Teacher sees ATTENDANCE_RECORD with low confidence or GHOST flag
  2. Teacher selects override_type and enters override_reason (min 10 chars)
  3. System validates teacher authorization
  4. Override submitted → TEACHER_OVERRIDE event → GD_ATTENDANCE_TRACKING_AGENT
  5. Agent appends correction record to audit log (original NOT deleted)
  6. Final status updated to override_type value
  7. Downstream agents receive corrected eligibility signals
  8. Audit shows: original ML result + teacher override + final status (three records)
```

---

## 1️⃣4️⃣ PARENT REPORT FEED

This agent feeds the Parent Report layer. Parent data access is strictly bounded:

```
PARENT REPORT FEED — WHAT IS SHARED:
  - session_date (not full UTC timestamp)
  - topic_title (non-sensitive)
  - attendance_status (human-readable: "Attended" | "Attended Late" | "Partial" | "Absent")
  - presence_percentage (shown as percentage bar, not raw float)
  - phases_summary: "Attended 4 of 5 discussion phases" (not raw phase breakdown)

PARENT REPORT FEED — WHAT IS NOT SHARED:
  - ghost_presence_flag (not exposed to parent — only to Teacher)
  - reconnect_count (internal signal, not parent-visible)
  - ML confidence scores (internal)
  - idle_periods detail (internal)
  - teacher_override details (teacher-facing only)
  - Raw event timestamps

PARENT REPORT DELIVERY:
  - In-app parent dashboard (Flutter, read-only)
  - Weekly summary push notification (if enabled)
  - Monthly summary report (auto-generated)
  - Parent CANNOT dispute attendance (must go through student → Teacher → override workflow)

GDPR-K / CHILD DATA COMPLIANCE:
  - Parent report data for minors treated under child data protection rules
  - Data minimization enforced (only necessary fields)
  - Retention: parent-visible data follows tenant data retention policy
```

---

## 1️⃣5️⃣ INSTITUTE ERP & COMPLIANCE REPORTING

This agent feeds the Institute ERP with cohort-level attendance analytics:

### Cohort Attendance Aggregates

```
COHORT_ATTENDANCE_AGGREGATE (per class / per domain / per time period):
  - class_id (institute internal grouping)
  - tenant_id
  - reporting_period (weekly | monthly | quarterly)
  - total_sessions
  - total_expected_participant_sessions
  - total_present_participant_sessions
  - average_attendance_rate (float)
  - late_rate (float)
  - partial_rate (float)
  - absent_rate (float)
  - ghost_rate (float)
  - average_presence_percentage (float)
  - phase_attendance_breakdown (per phase, average presence rate)
  - top_performers_list (top 5 by attendance rate — with user consent)
  - low_attendance_alerts (users below 60% attendance rate)
```

### Compliance Reports

```
COMPLIANCE_ATTENDANCE_REPORT (for Admin / Compliance ERP):
  - Triggered: Monthly (automatic) or on-demand
  - Scope: Tenant-wide, per domain, per class
  - Content:
    - Attendance rate trends (month-over-month)
    - Ghost presence frequency per session type
    - Teacher override frequency and reasons summary
    - Anomaly sessions (session_integrity_flag = ANOMALY_DETECTED)
    - Offline session attendance quality (online vs offline delta)
    - ML model confidence distribution (% of records below 0.75 threshold)
  - Format: Structured JSON (for API) + AI-generated narrative (for human reading)
  - Export: CSV / JSON for HRIS, LMS integration (SECTION T18 compliance)
  - Immutable: Report records are append-only, versioned
```

---

## 1️⃣6️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent touches user behavior. It **must** emit feature vectors to FEATURE_STORE_AGENT
after every session classification:

```json
EMIT_FEATURE_VECTOR (per user per session):
{
  "user_id": "UUID",
  "session_id": "UUID",
  "tenant_id": "UUID",
  "feature_name": "gd_attendance_status | gd_presence_percentage | gd_reconnect_count |
                   gd_idle_percentage | gd_phase_presence_count | gd_late_join_flag |
                   gd_early_exit_flag | gd_ghost_flag | gd_dropout_risk_score",
  "feature_value": "numeric or encoded",
  "timestamp": "ISO-8601 UTC",
  "source_agent": "GD_ATTENDANCE_TRACKING_AGENT",
  "domain_track": "ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION"
}
```

Features feed:
- Passive Intelligence Engine (PIE) — interpersonal, communication behavior signals
- Skill Gap Analysis Engine — engagement patterns
- Retention Prediction Engine — dropout risk modeling
- User Growth Prediction Engine — long-term skill trajectory

---

## 1️⃣7️⃣ ANTI-COLLUSION COMPATIBILITY

```
GHOST_FLAG_EVENT emitted to ANTI_COLLUSION_DETECTION_AGENT:
{
  "session_id": "UUID",
  "user_id": "UUID",
  "tenant_id": "UUID",
  "ghost_confidence_score": "float",
  "idle_percentage": "float",
  "messages_sent": "integer",
  "role_actions_taken": "integer",
  "reconnect_count": "integer",
  "session_mode": "TEXT | VOICE | VIDEO",
  "timestamp_utc": "ISO-8601"
}

ANTI_COLLUSION_DETECTION_AGENT uses this to:
  - Detect match farming patterns (users repeatedly ghost-attending for XP)
  - Detect reciprocal ghost tolerance (user pairs consistently not monitoring each other)
  - Flag sessions for audit review before scoring counts toward belt progression
  - Generate governance board review requests for repeat ghost offenders

GD_ATTENDANCE_TRACKING_AGENT does NOT make collusion decisions.
It emits signals only. ANTI_COLLUSION_DETECTION_AGENT is the decision authority.
```

---

## 1️⃣8️⃣ GROWTH ENGINE HOOK

This agent affects XP and achievement. It **must** trigger:

```
XP_UPDATE_EVENT (via XP_REWARD_AGENT):
  { user_id, xp_delta, reason: ENUM(JOIN_GD | COMPLETE_ROLE | TOP_PARTICIPANT),
    session_id, attendance_status, timestamp_utc }

RANK_UPDATE_EVENT (via LEADERBOARD_AGENT):
  { user_id, session_id, attendance_qualified: boolean, timestamp_utc }

SHARE_TRIGGER_EVENT (via GROWTH_ENGINE_AGENT — if top_participant):
  { user_id, achievement_type: GD_ATTENDED | GD_PERFECT_ATTENDANCE,
    session_id, share_card_template, timestamp_utc }

PERFECT_ATTENDANCE_MILESTONE:
  - Triggered when user achieves PRESENT status in 10 consecutive sessions
  - Emits: MILESTONE_ACHIEVED event → XP bonus + badge + share card
  - Tracked by: attendance_streak counter per user per domain_track
```

---

## 1️⃣9️⃣ PERFORMANCE MONITORING

```
METRICS (emitted to OBSERVABILITY_AGENT):
  - join_event_success_rate           (target: >99.9%)
  - join_event_latency_p95            (target: <200ms)
  - heartbeat_processing_latency_p95  (target: <100ms)
  - attendance_classification_latency_p95  (target: <500ms)
  - ghost_detection_accuracy          (vs teacher_override ground truth, rolling 30-day)
  - ml_confidence_below_threshold_rate (% below 0.75 — alert if >10%)
  - teacher_override_frequency        (per session — anomaly if >30% of participants)
  - offline_sync_success_rate         (target: >99%)
  - security_violation_count          (zero tolerance)
  - dlq_depth                         (gd.attendance.dlq — alert if >100)
  - redis_heartbeat_lag_ms            (real-time state freshness)

DASHBOARDS (mandatory on OBSERVABILITY_AGENT):
  - attendance_classification_failure_rate
  - ghost_flag_frequency_per_session
  - teacher_override_rate_trend
  - ml_confidence_distribution
  - offline_sync_lag_distribution
  - parent_report_delivery_success_rate

ALERTS:
  - Join event failure rate > 0.5% over 5min         → PagerDuty P2
  - Security violation detected                        → PagerDuty P1 (immediate)
  - ML model drift > 5% accuracy drop                  → PagerDuty P2
  - DLQ depth > 100                                    → PagerDuty P2
  - Redis heartbeat lag > 5s average                   → PagerDuty P2
  - Ghost detection rate spike (> 15% per session)     → PagerDuty P3 (investigation)
```

---

## 2️⃣0️⃣ DATABASE SCHEMA (LOCKED — ADD-ONLY MUTATION)

```sql
-- gd_attendance_records (core table — one record per participant per session)
CREATE TABLE gd_attendance_records (
  attendance_record_id      UUID PRIMARY KEY,
  session_id                UUID NOT NULL REFERENCES gd_sessions(session_id),
  user_id                   UUID NOT NULL,
  tenant_id                 UUID NOT NULL,
  group_id                  UUID NOT NULL,
  attendance_status         VARCHAR(20) NOT NULL,
  join_timestamp_utc        TIMESTAMPTZ,
  exit_timestamp_utc        TIMESTAMPTZ,
  total_presence_seconds    INTEGER NOT NULL DEFAULT 0,
  presence_percentage       DECIMAL(5,4),
  reconnect_count           INTEGER NOT NULL DEFAULT 0,
  total_disconnect_seconds  INTEGER NOT NULL DEFAULT 0,
  total_idle_seconds        INTEGER NOT NULL DEFAULT 0,
  idle_percentage           DECIMAL(5,4),
  late_join_flag            BOOLEAN NOT NULL DEFAULT FALSE,
  late_join_delay_seconds   INTEGER,
  early_exit_flag           BOOLEAN NOT NULL DEFAULT FALSE,
  early_exit_phase          VARCHAR(30),
  ghost_presence_flag       BOOLEAN NOT NULL DEFAULT FALSE,
  ghost_confidence_score    DECIMAL(4,3),
  scoring_eligibility       VARCHAR(20) NOT NULL,
  xp_eligibility            VARCHAR(20) NOT NULL,
  belt_session_credit       VARCHAR(20) NOT NULL,
  attendance_source         VARCHAR(20) NOT NULL,
  ml_confidence_score       DECIMAL(4,3),
  model_version             VARCHAR(100) NOT NULL,
  audit_reference           UUID NOT NULL,
  teacher_override_applied  BOOLEAN NOT NULL DEFAULT FALSE,
  created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version                   INTEGER NOT NULL DEFAULT 1
);

-- gd_attendance_phase_presence (per phase per user per session)
CREATE TABLE gd_attendance_phase_presence (
  id                        UUID PRIMARY KEY,
  attendance_record_id      UUID NOT NULL REFERENCES gd_attendance_records(attendance_record_id),
  session_id                UUID NOT NULL,
  user_id                   UUID NOT NULL,
  tenant_id                 UUID NOT NULL,
  phase_name                VARCHAR(30) NOT NULL,
  phase_status              VARCHAR(20) NOT NULL, -- PRESENT | ABSENT | PARTIAL
  phase_start_utc           TIMESTAMPTZ NOT NULL,
  phase_end_utc             TIMESTAMPTZ NOT NULL,
  user_join_in_phase_utc    TIMESTAMPTZ,
  user_exit_in_phase_utc    TIMESTAMPTZ,
  phase_presence_seconds    INTEGER NOT NULL DEFAULT 0,
  created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- gd_heartbeat_events (high-volume — partitioned by date)
CREATE TABLE gd_heartbeat_events (
  id                        UUID PRIMARY KEY,
  session_id                UUID NOT NULL,
  user_id                   UUID NOT NULL,
  tenant_id                 UUID NOT NULL,
  heartbeat_timestamp_utc   TIMESTAMPTZ NOT NULL,
  activity_signal           VARCHAR(10) NOT NULL, -- ACTIVE | IDLE
  messages_sent_delta       INTEGER NOT NULL DEFAULT 0,
  ideas_submitted_delta     INTEGER NOT NULL DEFAULT 0
) PARTITION BY RANGE (heartbeat_timestamp_utc);
-- Partitioned daily; partitions older than 30 days archived

-- gd_teacher_overrides (append-only override records)
CREATE TABLE gd_teacher_overrides (
  override_id               UUID PRIMARY KEY,
  attendance_record_id      UUID NOT NULL REFERENCES gd_attendance_records(attendance_record_id),
  session_id                UUID NOT NULL,
  tenant_id                 UUID NOT NULL,
  teacher_id                UUID NOT NULL,
  target_user_id            UUID NOT NULL,
  override_type             VARCHAR(30) NOT NULL,
  override_reason           TEXT NOT NULL,
  override_notes            TEXT,
  original_ml_status        VARCHAR(20) NOT NULL,
  override_status           VARCHAR(20) NOT NULL,
  override_timestamp_utc    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  audit_reference           UUID NOT NULL
);

-- gd_attendance_session_summary (per-session summary — post-session)
CREATE TABLE gd_attendance_session_summary (
  summary_id                UUID PRIMARY KEY,
  session_id                UUID NOT NULL REFERENCES gd_sessions(session_id),
  tenant_id                 UUID NOT NULL,
  group_id                  UUID NOT NULL,
  total_expected            INTEGER NOT NULL,
  present_count             INTEGER NOT NULL DEFAULT 0,
  late_count                INTEGER NOT NULL DEFAULT 0,
  partial_count             INTEGER NOT NULL DEFAULT 0,
  absent_count              INTEGER NOT NULL DEFAULT 0,
  ghost_count               INTEGER NOT NULL DEFAULT 0,
  excused_count             INTEGER NOT NULL DEFAULT 0,
  attendance_rate           DECIMAL(5,4),
  avg_presence_percentage   DECIMAL(5,4),
  session_integrity_flag    VARCHAR(20) NOT NULL DEFAULT 'CLEAN',
  model_version             VARCHAR(100) NOT NULL,
  audit_reference           UUID NOT NULL,
  generated_at              TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- gd_attendance_audit_log (append-only — no RLS on this table)
CREATE TABLE gd_attendance_audit_log (
  audit_id                  UUID PRIMARY KEY,
  timestamp_utc             TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  actor_id                  UUID NOT NULL,
  actor_role                VARCHAR(30) NOT NULL,
  tenant_id                 UUID NOT NULL,
  session_id                UUID,
  user_id                   UUID,
  event_type                VARCHAR(60) NOT NULL,
  input_hash                VARCHAR(64),
  output_hash               VARCHAR(64),
  model_version             VARCHAR(100),
  decision_path             TEXT,
  confidence_score          DECIMAL(4,3),
  anomaly_flags             TEXT[],
  attendance_status_before  VARCHAR(20),
  attendance_status_final   VARCHAR(20)
);

-- ROW LEVEL SECURITY
ALTER TABLE gd_attendance_records      ENABLE ROW LEVEL SECURITY;
ALTER TABLE gd_attendance_phase_presence ENABLE ROW LEVEL SECURITY;
ALTER TABLE gd_attendance_session_summary ENABLE ROW LEVEL SECURITY;
ALTER TABLE gd_teacher_overrides       ENABLE ROW LEVEL SECURITY;
-- gd_heartbeat_events: no RLS (high-volume partition, controlled at application layer)
-- gd_attendance_audit_log: no RLS (platform-wide compliance)

-- REDIS KEYS (Heartbeat State — ephemeral, TTL = session_duration + 10min)
-- Key: gd:attendance:{session_id}:{user_id}:heartbeat
-- Value: { last_ts, activity_signal, idle_start_ts, messages_delta, ideas_delta }
-- Key: gd:attendance:{session_id}:{user_id}:phase_state
-- Value: { current_phase, phase_join_ts, phase_presence_seconds }
```

---

## 2️⃣1️⃣ MICROSERVICES REQUIRED

```
SERVICE                          RESPONSIBILITY
Attendance Tracker Service       Core join/exit/heartbeat event processing; Redis state management
Ghost Detection Service          ML-powered idle/ghost presence classifier
Attendance Classifier Service    Post-session ML classification of attendance_status
Teacher Override Service         Override workflow; authorization; audit append
Attendance Report Service        Session summaries; ERP aggregates; parent reports; compliance
```

Additional services consumed (not owned by this agent):
- WebSocket Gateway (event source)
- Timer Service (phase transition events)
- Audit Log Service (append-only sink)
- Offline Sync Service (school mode events)
- Notification Service (teacher review alerts, parent report delivery)

---

## 2️⃣2️⃣ OFFLINE SCHOOL MODE COMPLIANCE

```
OFFLINE_MODE_FLAG = true SESSIONS (TEXT mode only):

ATTENDANCE TRACKING OFFLINE:
  1. Flutter client queues join/exit/heartbeat events to SQLite local store
  2. School server (PostgreSQL lite) receives events from student devices via school WiFi
  3. Events timestamped with offline_queued_at_utc (device clock, NTP-synced to school server)
  4. On internet restore: OFFLINE_SYNC_AGENT uploads queued events to cloud
  5. GD_ATTENDANCE_TRACKING_AGENT processes offline events using offline_queued_at_utc
     as authoritative timestamp (NOT cloud receive timestamp)
  6. Offline events accepted: within 24 hours of session_end_utc
  7. Attendance classification proceeds as normal after offline events ingested

OFFLINE GHOST DETECTION:
  - Heartbeat intervals in offline mode: 60 seconds (instead of 30s, to reduce school WiFi load)
  - Ghost detection thresholds relaxed: idle_percentage threshold = 0.70 (vs 0.60 online)
  - Lower threshold because offline network interruptions can cause false idle signals

OFFLINE REJECTION CASE:
  - Events arriving > 24h post session_end → rejected
  - Teacher override workflow triggered
  - OFFLINE_REJECTION event logged to audit

ACCURACY NOTE:
  - Offline attendance records flagged with attendance_source = OFFLINE_SYNCED
  - Compliance reports show online vs offline accuracy delta
```

---

## 2️⃣3️⃣ VERSIONING POLICY

```
VERSION FORMAT: GD_ATTENDANCE_ML_v{MAJOR}.{MINOR}.{PATCH}
CURRENT VERSION: v1.0.0

ALL CHANGES: Add-only, versioned, backward compatible, migration documented, rollback-safe.

CHANGES REQUIRING MAJOR VERSION BUMP:
  - Attendance status classification definition change (e.g., PRESENT threshold change)
  - Scoring eligibility rule change
  - XP eligibility rule change
  - Belt credit rule change
  - Ghost presence detection threshold change
  - DB schema field rename or removal (FORBIDDEN without major bump + migration)
  - Teacher override rule change
  - Heartbeat interval change

CHANGES REQUIRING MINOR VERSION BUMP:
  - New attendance_status category added
  - New optional input field added
  - New ML model feature added
  - New compliance report field added
  - New parent report field added

PATCH VERSION:
  - Bug fixes, performance improvements
  - No interface or rule change

BACKWARD COMPATIBILITY WINDOW: 60 days minimum.
PRIOR MODEL VERSIONS: Retained 60 days for rollback.
```

---

## 2️⃣4️⃣ NON-NEGOTIABLE RULES

This agent must **NOT**:

```
❌ Create hidden attendance logic not defined in this spec
❌ Modify historical attendance records in any table
❌ Allow teacher override to delete original ML classification record
❌ Auto-delete or auto-archive audit log entries
❌ Override GOVERNANCE agents, COMPLIANCE agents, or ANTI_COLLUSION_DETECTION_AGENT
❌ Allow cross-tenant attendance event processing
❌ Mark a participant ABSENT during a live session (only post-session classification)
❌ Auto-award XP for GHOST or ABSENT attendance status
❌ Credit belt session for GHOST, ABSENT, PARTIAL, or EXCUSED attendance
❌ Expose ghost_presence_flag or teacher_override details to PARENT report feed
❌ Expose raw event timestamps or ML confidence scores to PARENT report feed
❌ Allow parent to dispute attendance directly (must go through teacher workflow)
❌ Process offline events older than 24 hours post-session (must reject)
❌ Operate in non-stateless execution (no local session state in memory)
❌ Allow heartbeat from un-joined participant (must have prior JOIN event for session)
❌ Accept join event after session discussion_end_utc + 60 seconds grace
❌ Allow session attendance override without teacher_role JWT claim
❌ Emit scoring_eligibility verdicts via AI (ML-only decision)
❌ Mix domain data from different domain_tracks in any query
❌ Mix tenant data in any query or aggregate
❌ Emit events without audit_reference
❌ Proceed after SECURITY_VIOLATION — always STOP_EXECUTION
❌ Allow VIDEO or VOICE session attendance in OFFLINE mode
❌ Auto-finalize GHOST status without teacher review queue (teacher always gets override window)
```

---

## 🔒 FINAL GOVERNANCE SEAL

```
GD_ATTENDANCE_TRACKING_AGENT PRODUCTION MODE ENABLED

✅ Tenant Isolation:              HARD ENFORCED (PostgreSQL RLS + JWT claim)
✅ Domain Isolation:              HARD ENFORCED
✅ Security Model:                ZERO-TRUST MULTI-TENANT
✅ Execution Mode:                DETERMINISTIC + VALIDATED
✅ Mutation Policy:               ADD-ONLY VERSIONED
✅ Audit Trail:                   APPEND-ONLY IMMUTABLE (original records never mutated)
✅ ML Primary:                    70–80% (Status Classifier | Ghost Detector | Eligibility Engine)
✅ AI Bounded Scope:              20–30% (Ghost Explanation | Anomaly Narrative | Report Summary)
✅ Heartbeat Processing:          REDIS-FIRST (batch flush to PostgreSQL at phase transitions)
✅ Ghost Detection:               ML-POWERED — TEACHER REVIEW REQUIRED BEFORE FINAL STATUS
✅ Teacher Override:              AUDIT-APPENDED — ORIGINAL RECORD PRESERVED ALWAYS
✅ Parent Data Minimization:      STRICT — NO RAW EVENTS | NO GHOST FLAG | NO ML SCORES
✅ Offline School Mode:           TEXT ONLY | 24H SYNC WINDOW | OFFLINE_QUEUED_AT AUTHORITATIVE
✅ Scoring Eligibility:           ML-ONLY DECISION — AI ADVISORY ONLY
✅ XP Eligibility:                ATTENDANCE-GATED — NO XP FOR GHOST OR ABSENT
✅ Belt Credit:                   STRICT — PRESENT STATUS + 80% PRESENCE REQUIRED
✅ ERP Compliance Reporting:      COHORT AGGREGATES + TREND ANALYSIS + AI NARRATIVE
✅ Anti-Collusion Compatibility:  GHOST_FLAG_EVENTS EMITTED TO ANTI_COLLUSION_AGENT
✅ Failure Policy:                HALT ON AMBIGUITY — NO SILENT FAILURES
✅ Observability:                 FULL TELEMETRY TO OBSERVABILITY_AGENT
✅ Scalability:                   STATELESS | KAFKA-PARTITIONED | REDIS HEARTBEAT BUFFER
✅ Backward Compatibility:        60-DAY WINDOW MANDATORY
✅ Interpretation Authority:      NONE
✅ Architecture Authority:        LOCKED

SEALED & LOCKED
ARTIFACT VERSION: v1.0.0
PLATFORM: ECOSKILLER ANTIGRAVITY
DOMAIN: DOJO ENGINE → GROUP DISCUSSION → ATTENDANCE INTELLIGENCE LAYER
SEAL DATE: 2026-02-25
```

---

*This document is a sealed production artifact. All modifications require a version bump,
migration documentation, and backward compatibility verification. No field may be renamed
or removed. No eligibility rule may change without MAJOR version bump. No logic may be
added without updating this document first. Creative interpretation is forbidden.*
