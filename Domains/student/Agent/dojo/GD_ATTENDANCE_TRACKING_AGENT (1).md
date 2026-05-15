# 🔒 GD_ATTENDANCE_TRACKING_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED AGENT SPECIFICATION
### Artifact Class: Enterprise Production Agent Blueprint
### Version: v1.0.0
### Mutation Policy: Add-only via version bump
### Execution Mode: Deterministic + Validated
### Interpretation Authority: NONE
### Architecture Authority: LOCKED
### Seal Status: PERMANENTLY SEALED

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║         GD ATTENDANCE TRACKING AGENT — ANTIGRAVITY PLATFORM                ║
║         ECOSKILLER INTELLIGENCE & INNOVATION CORE                           ║
║         STATUS: LOCKED · SEALED · PRODUCTION-GRADE · ENFORCED              ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## 🔐 EXECUTION GOVERNANCE HEADER

```
AGENT_CLASS               = AUTONOMOUS_TRACKER
EXECUTION_MODE            = DETERMINISTIC + VALIDATED
MUTATION_POLICY           = ADD_ONLY_VERSIONED
CREATIVE_INTERPRETATION   = FORBIDDEN
ASSUMPTION_FILLING        = FORBIDDEN
DEFAULT_BEHAVIOR          = DENY_ON_AMBIGUITY
FAILURE_MODE              = HALT + LOG + ESCALATE
SECURITY_MODEL            = ZERO_TRUST_MULTI_TENANT
DATA_POLICY               = APPEND_ONLY_AUDIT_TRAIL
CROSS_TENANT_QUERY        = FORBIDDEN
SILENT_FAILURE            = FORBIDDEN
PARTIAL_OUTPUT            = FORBIDDEN
RETROACTIVE_MODIFICATION  = FORBIDDEN
AUTO_ATTENDANCE_GRANT     = FORBIDDEN
COLLUSION_TOLERANCE       = ZERO
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY — NON-NEGOTIABLE)

```yaml
AGENT_NAME:          GD_ATTENDANCE_TRACKING_AGENT
AGENT_ID:            EKAG-GDAT-002
VERSION:             v1.0.0
SYSTEM_ROLE:         Autonomous Real-Time Group Discussion Attendance
                     Recorder, Validator, Anomaly Detector, and
                     Compliance Reporter
PRIMARY_DOMAIN:      Dojo GD Engine — Ecoskiller Antigravity Platform
EXECUTION_MODE:      Deterministic + Validated
DATA_SCOPE:          GD session join/leave events, participant presence
                     windows, mentor presence validation, recording
                     consent acknowledgements, duration compliance,
                     anti-ghost detection signals, attendance certificates,
                     institute/enterprise attendance reports, ERP export data,
                     parent visibility records, streak/XP eligibility signals
TENANT_SCOPE:        Strict Isolation — No cross-tenant attendance data access
FAILURE_POLICY:      Halt on ambiguity — No partial execution permitted
DOMAIN_LOCK:         Arts | Commerce | Science | Technology | Administration
                     (Cross-domain attendance data mixing FORBIDDEN)
STACK_CONTEXT:       FastAPI microservice | PostgreSQL 15 | Redis 7 |
                     Redis Streams | Kafka Event Bus | WebSocket Game Events |
                     LiveKit metadata signals | Flutter client heartbeat |
                     Prometheus metrics
```

This agent must **never** assume missing specifications. If any required
field is absent, ambiguous, or unverifiable — execution STOPS immediately.
No attendance record is created from ambiguous signals.

---

## 2️⃣ PURPOSE DECLARATION

### Problem This Agent Solves

Group Discussion sessions on the Ecoskiller Antigravity Dojo platform require
a forensic-grade, real-time attendance tracking system. Without this agent:

- Participants could claim session credit without meaningful presence
- Ghost participants (joined but silent/inactive) would corrupt scoring inputs
- Mentor presence would be unverified at certification sessions, invalidating belts
- Institute ERP integrations would receive unvalidated attendance, breaking
  compliance reports for schools, colleges, and enterprise HR systems
- Parent visibility dashboards would show inaccurate child participation data
- Streak engines and XP engines would reward fraudulent session completions
- Belt promotion eligibility checks would be triggered from unvalidated attendance
- Anti-collusion detection would have no presence data to cross-reference
- Recording consent compliance would be unenforceable

This agent is the **single source of truth** for who was present, for how long,
in what quality, and under what conditions in every GD session on the platform.

### What Input It Consumes

- Real-time join/leave events from LiveKit room webhooks
- Participant heartbeat signals from Flutter client (every 30 seconds)
- WebSocket presence state from Game Events channel
- Session metadata from GD_SESSION_SCHEDULER_AGENT (via event)
- Mentor presence signals from LiveKit metadata
- Recording consent acknowledgement events from CONSENT_CAPTURE_AGENT
- Behavioral anomaly signals from COLLUSION_DETECTION_AGENT
- Session lifecycle events (SESSION_LIVE, SESSION_ENDED) from GD_SESSION_SCHEDULER_AGENT
- Tenant context and domain from TENANT_CONTEXT_AGENT
- Institute/Enterprise report configuration from TENANT_ERP_CONFIG_AGENT
- Manual attendance correction requests (ADMIN/MENTOR role only, requires audit)

### What Output It Produces

- Real-time participant presence state (present / absent / ghost / partial)
- Finalized attendance records per session per participant (immutable on ENDED)
- Attendance compliance reports (per tenant, institution, cohort, domain)
- Mentor presence certificates (for CERTIFICATION and RANKED sessions)
- ERP-compatible attendance export payloads (HRIS, LMS, LTI)
- Parent visibility records (read-only child session attendance)
- Streak eligibility signals to STREAK_ENGINE
- XP eligibility signals to GROWTH_ENGINE
- Belt eligibility qualification signals to CERTIFICATE_ENGINE
- Anti-ghost attestation signals to SCORING_ENGINE
- Anomaly flags to OBSERVABILITY_AGENT and COMPLIANCE_AGENT
- Audit records to AUDIT_LOG_SERVICE (append-only, immutable)
- Feature vectors to FEATURE_STORE_AGENT

### Downstream Agents That Depend on This Agent

- SCORING_ENGINE (requires anti-ghost attestation before scoring is valid)
- CERTIFICATE_ENGINE (requires qualified attendance for belt eligibility)
- GROWTH_ENGINE / STREAK_ENGINE (XP and streak signals)
- LEADERBOARD_UPDATE_AGENT (attendance-qualified session only)
- ANALYTICS_AGENT (attendance metrics pipeline)
- NOTIFICATION_AGENT (absence alerts, late-join warnings)
- PARENT_VISIBILITY_SERVICE (child attendance records)
- INSTITUTE_ERP_AGENT (attendance for school/college/university report cards)
- ENTERPRISE_ERP_AGENT (attendance for corporate training compliance)
- COLLUSION_DETECTION_AGENT (cross-references presence data with scoring patterns)
- OBSERVABILITY_AGENT (attendance anomaly metrics)
- COMPLIANCE_AGENT (certification session mentor presence records)
- FEATURE_STORE_AGENT (behavioral features for ML models)

### Upstream Agents That Feed This Agent

- GD_SESSION_SCHEDULER_AGENT (session context, participant roster, mentor assignment)
- LiveKit Webhook Service (room join/leave/metadata events)
- FLUTTER_CLIENT_HEARTBEAT_SERVICE (30-second client presence pings)
- WEBSOCKET_GAME_EVENT_SERVICE (presence state during live session)
- CONSENT_CAPTURE_AGENT (recording consent confirmation signals)
- COLLUSION_DETECTION_AGENT (anomaly enrichment signals)
- TENANT_CONTEXT_AGENT (tenant plan, domain, institute/enterprise type)
- TENANT_ERP_CONFIG_AGENT (report format preferences, attendance thresholds)

---

## 3️⃣ INPUT CONTRACT (STRICT — ZERO TOLERANCE)

### 3A — Real-Time Event Input (Streaming)

```json
REALTIME_EVENT_INPUT_SCHEMA: {
  "required_fields": [
    "event_id",
    "event_type",
    "session_id",
    "tenant_id",
    "participant_id",
    "timestamp_utc",
    "source_channel"
  ],
  "field_definitions": {
    "event_id":        "UUID — globally unique event identifier",
    "event_type":      "ENUM[PARTICIPANT_JOINED | PARTICIPANT_LEFT |
                        HEARTBEAT_RECEIVED | HEARTBEAT_MISSED |
                        MENTOR_JOINED | MENTOR_LEFT |
                        CONSENT_ACKNOWLEDGED | CONSENT_DECLINED |
                        SESSION_LIVE | SESSION_ENDED |
                        ANOMALY_SIGNAL | NETWORK_DISCONNECT |
                        NETWORK_RECONNECT | FORCED_EXIT]",
    "session_id":      "UUID — must match active session in gd_sessions",
    "tenant_id":       "UUID — must match session tenant",
    "participant_id":  "UUID — must be in session roster",
    "timestamp_utc":   "ISO8601 UTC — event occurrence time",
    "source_channel":  "ENUM[LIVEKIT_WEBHOOK | FLUTTER_HEARTBEAT |
                        WEBSOCKET_GAME_EVENTS | MENTOR_SOCKET |
                        SYSTEM_INTERNAL]"
  },
  "optional_event_fields": {
    "network_quality_score":     "float 0.0–1.0 — from LiveKit metadata",
    "room_participant_count":    "integer — total in room at event time",
    "reconnect_attempt_count":   "integer — for NETWORK_RECONNECT events",
    "forced_exit_reason":        "string — BEHAVIORAL_SAFETY | TIMEOUT | ADMIN_ACTION",
    "anomaly_type":              "string — from COLLUSION_DETECTION_AGENT",
    "device_fingerprint_hash":   "SHA256 — for multi-device detection"
  },
  "validation_rules": [
    "event_id must be globally unique — reject duplicate event_ids",
    "session_id must reference an ACTIVE session (status = LIVE or WAITING)",
    "tenant_id must match session.tenant_id — cross-tenant signals REJECTED",
    "participant_id must be in gd_session_participants roster for session_id",
    "timestamp_utc must not be in the future (max +5 second clock drift allowed)",
    "timestamp_utc must not predate session.scheduled_start_utc by more than 600 seconds",
    "source_channel must be declared — undeclared source REJECTED",
    "HEARTBEAT_RECEIVED events must be within 45 seconds of last heartbeat or JOINED event",
    "MENTOR events (MENTOR_JOINED, MENTOR_LEFT) require mentor_id validation against session"
  ],
  "security_checks": [
    "LiveKit webhook HMAC signature verification — reject unsigned webhooks",
    "Flutter heartbeat JWT token validation — reject unauthenticated pings",
    "WebSocket event channel authentication — session-scoped token required",
    "Rate limit: max 120 events per participant per minute — spike = anomaly flag",
    "device_fingerprint_hash: detect same participant joining from multiple devices simultaneously"
  ],
  "null_tolerance_policy": "ZERO — all required fields non-null. Missing = REJECT + LOG",
  "reject_policy":          "Invalid event → LOG → DROP (no crash) → emit EVENT_REJECTED_ANOMALY"
}
```

### 3B — Manual Correction Input (Privileged Operation)

```json
MANUAL_CORRECTION_INPUT_SCHEMA: {
  "required_fields": [
    "correction_id",
    "tenant_id",
    "session_id",
    "requesting_actor_id",
    "requesting_actor_role",
    "participant_id",
    "correction_type",
    "correction_reason",
    "supporting_evidence_reference"
  ],
  "field_definitions": {
    "correction_id":                  "UUID — idempotency key",
    "correction_type":                "ENUM[MARK_PRESENT | MARK_ABSENT |
                                       ADJUST_DURATION | WAIVE_GHOST_FLAG |
                                       OVERRIDE_NETWORK_DROPOUT]",
    "correction_reason":              "string — min 20 chars, max 500 chars",
    "supporting_evidence_reference":  "UUID — reference to dispute ticket or
                                       admin review record"
  },
  "authorization_rules": [
    "requesting_actor_role must be ADMIN or MENTOR",
    "STUDENT role may NEVER submit manual corrections — FORBIDDEN",
    "PARENT role may NEVER submit manual corrections — FORBIDDEN",
    "MARK_PRESENT corrections: require ADMIN role only",
    "WAIVE_GHOST_FLAG: require ADMIN role + GOVERNANCE_AGENT approval reference",
    "Corrections on ARCHIVED sessions: ADMIN only + compliance justification required",
    "All corrections logged in immutable audit trail with full before/after state"
  ],
  "correction_limits": [
    "Maximum 3 manual corrections per session — beyond this, GOVERNANCE_AGENT review required",
    "Bulk corrections (>5 participants in one request): FORBIDDEN — must be individual",
    "Retroactive correction window: ENDED sessions within 72 hours of end time only",
    "ARCHIVED sessions: correction window closed — read-only forever"
  ]
}
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

### 4A — Real-Time Presence State (Live)

```json
REALTIME_PRESENCE_OUTPUT: {
  "session_id":           "UUID",
  "tenant_id":            "UUID",
  "snapshot_timestamp":   "ISO8601",
  "participants": [
    {
      "participant_id":         "UUID",
      "presence_status":        "ENUM[PRESENT | ABSENT | GHOST | PARTIAL | DISCONNECTED | FORCED_EXIT]",
      "time_in_session_seconds":"integer — cumulative present seconds",
      "last_signal_utc":        "ISO8601",
      "network_quality_avg":    "float 0.0–1.0",
      "heartbeat_miss_count":   "integer",
      "reconnect_count":        "integer",
      "consent_status":         "ENUM[ACKNOWLEDGED | DECLINED | PENDING]",
      "anomaly_flags":          "Array<string>"
    }
  ],
  "mentor_presence": {
    "mentor_id":            "UUID | null",
    "present":              "boolean",
    "joined_at_utc":        "ISO8601 | null",
    "total_present_seconds":"integer"
  },
  "session_quorum_met":   "boolean — minimum required participants present",
  "model_version":        "string",
  "audit_reference":      "UUID"
}
```

### 4B — Finalized Attendance Record (Post-Session — IMMUTABLE ON CREATION)

```json
FINALIZED_ATTENDANCE_OUTPUT: {
  "result_object": {
    "attendance_record_id":       "UUID — primary immutable identifier",
    "session_id":                 "UUID",
    "tenant_id":                  "UUID",
    "domain_track":               "string",
    "session_type":               "string",
    "finalized_at_utc":           "ISO8601",
    "session_duration_minutes":   "integer — scheduled",
    "actual_duration_minutes":    "integer — actual",

    "participant_attendance_records": [
      {
        "participant_id":            "UUID",
        "tenant_id":                 "UUID",
        "final_attendance_status":   "ENUM[QUALIFIED | PARTIAL | ABSENT | GHOST | DISQUALIFIED]",
        "present_seconds":           "integer — total verified presence",
        "present_percentage":        "float — present_seconds / actual_session_seconds * 100",
        "attendance_threshold_met":  "boolean — >= configured threshold (default 80%)",
        "join_time_utc":             "ISO8601 | null",
        "leave_time_utc":            "ISO8601 | null",
        "late_join_seconds":         "integer — seconds after session start",
        "early_leave_seconds":       "integer — seconds before session end",
        "network_dropout_count":     "integer",
        "total_dropout_seconds":     "integer",
        "heartbeat_miss_count":      "integer",
        "consent_status":            "ENUM[ACKNOWLEDGED | DECLINED | NOT_REQUIRED]",
        "ghost_detection_score":     "float 0.0–1.0 — ML model output",
        "ghost_flagged":             "boolean",
        "anomaly_flags":             "Array<anomaly_type>",
        "scoring_eligible":          "boolean — anti-ghost attestation for SCORING_ENGINE",
        "xp_eligible":               "boolean",
        "streak_eligible":           "boolean",
        "belt_eligible":             "boolean — RANKED/CERTIFICATION only",
        "correction_applied":        "boolean",
        "correction_reference":      "UUID | null"
      }
    ],

    "mentor_attendance_record": {
      "mentor_id":                 "UUID | null",
      "present":                   "boolean",
      "present_seconds":           "integer",
      "present_percentage":        "float",
      "certification_authority_valid": "boolean — present >= 90% for CERTIFICATION sessions",
      "override_events_count":     "integer"
    },

    "session_quorum_analysis": {
      "minimum_required":          "integer",
      "actual_qualified":          "integer",
      "quorum_met":                "boolean",
      "quorum_met_at_utc":         "ISO8601 | null"
    },

    "anti_collusion_snapshot": {
      "collusion_flags_raised":    "integer",
      "flagged_participant_pairs": "Array<UUID_pair>",
      "investigation_required":    "boolean"
    },

    "compliance_tags": {
      "institute_report_ready":    "boolean",
      "enterprise_report_ready":   "boolean",
      "parent_visibility_ready":   "boolean",
      "erp_export_ready":          "boolean"
    }
  },

  "confidence_score":    "float 0.0–1.0 — attendance data completeness confidence",
  "model_version":       "string",
  "audit_reference":     "UUID",
  "next_trigger_event":  [
    "ATTENDANCE_FINALIZED_EVENT",
    "SCORING_ATTESTATION_EVENT",
    "XP_ELIGIBILITY_EVENT",
    "BELT_ELIGIBILITY_CHECK_EVENT",
    "ERP_EXPORT_READY_EVENT",
    "PARENT_RECORD_READY_EVENT",
    "STREAK_UPDATE_EVENT"
  ]
}
```

All finalized attendance records are **immutable on creation**. No field may
be modified after finalization except via the controlled manual correction
pathway (Section 3B), which creates a new correction overlay record — it does
**not** mutate the original.

---

## 5️⃣ ML / AI LOGIC LAYER

### ML Usage (Primary — ~78% of intelligence decisions)

```
MODEL_TYPE: Classification + Anomaly Detection + Time-Series Analysis

════════════════════════════════════════════════════════════════
MODEL_1: GHOST_PARTICIPANT_DETECTOR
════════════════════════════════════════════════════════════════
Type:             Binary Classification
Purpose:          Detect participants who are technically "joined" (room
                  connection alive) but not meaningfully participating —
                  the "ghost" pattern that inflates attendance fraudulently.
Input Features:
  - heartbeat_miss_rate (last 5 minutes rolling window)
  - audio_activity_signal (from LiveKit metadata — not content, just presence)
  - video_activity_signal (camera on/off pattern)
  - websocket_interaction_event_count (game event channel activity)
  - time_since_last_active_interaction (seconds)
  - network_quality_score (avg over window)
  - reconnect_spike_pattern (many reconnects without activity)
  - session_duration_ratio (ghost behavior often peaks mid-session)
  - historical_ghost_score_per_user (rolling 90-day average)
  - peer_interaction_count (did others direct events at this participant)
Output:           ghost_detection_score (float 0.0–1.0)
                  ghost_flagged = TRUE if score >= 0.72
Threshold:        0.72 (configurable per tenant, min 0.60)
Training Freq:    Weekly (rolling 90-day window)
Drift Detection:
  - Monitor false positive rate monthly (target < 5%)
  - Monitor false negative rate monthly (target < 3%)
  - Alert if ghost_flag_rate changes > 15% month-over-month per tenant
Version Control:  model_id + model_version stored in every attendance record
Human Override:   ADMIN role may WAIVE ghost flag with GOVERNANCE_AGENT
                  approval — waiver logged immutably

════════════════════════════════════════════════════════════════
MODEL_2: NETWORK_DROPOUT_CLASSIFIER
════════════════════════════════════════════════════════════════
Type:             Multi-Class Classification
Purpose:          Distinguish genuine network failure (attendance should
                  be preserved) from deliberate disconnect (attendance
                  should be penalized) vs. device crash (grace period).
Input Features:
  - disconnect_duration_seconds
  - reconnect_attempted (boolean)
  - reconnect_success_rate (historical per user)
  - time_of_dropout_in_session (early/mid/late — behavioral difference)
  - prior_dropout_pattern (rolling 30-day)
  - device_type (mobile dropouts differ from desktop)
  - network_quality_score_before_dropout
  - concurrent_platform_activity (device active elsewhere during dropout)
Output:           dropout_classification:
                  ENUM[GENUINE_NETWORK | DELIBERATE_DISCONNECT | DEVICE_CRASH | AMBIGUOUS]
                  attended_seconds_to_credit (adjusted by classification)
Training Freq:    Monthly
Drift Detection:  Monitor DELIBERATE_DISCONNECT misclassification monthly

════════════════════════════════════════════════════════════════
MODEL_3: ATTENDANCE_FRAUD_PATTERN_DETECTOR
════════════════════════════════════════════════════════════════
Type:             Anomaly Detection (Isolation Forest + Rule Overlay)
Purpose:          Detect coordinated attendance fraud patterns —
                  multi-account farming, shared device fraud, VPN-masked
                  presence, scheduled ghost rings.
Input Features:
  - device_fingerprint_hash collision rate (same device, multiple accounts)
  - IP address clustering (same IP block, multiple participants)
  - join_time_synchrony (participants joining within seconds of each other, repeatedly)
  - leave_time_synchrony (all leave simultaneously — unnatural)
  - heartbeat_pattern_similarity (identical heartbeat intervals across participants)
  - session_creation_to_join_latency (too fast = scripted)
  - historical_pair_session_count (same pair appearing repeatedly)
  - geographic_impossibility_flag (device location inconsistency)
Output:           fraud_risk_score (float 0.0–1.0)
                  fraud_pattern_type (string — pattern label)
                  investigation_required (boolean — if score >= 0.65)
Training Freq:    Monthly (adversarial refresh — fraud evolves)
Escalation:       Scores >= 0.65 → COLLUSION_DETECTION_AGENT + COMPLIANCE_AGENT

════════════════════════════════════════════════════════════════
MODEL_4: ATTENDANCE_THRESHOLD_PREDICTOR
════════════════════════════════════════════════════════════════
Type:             Regression
Purpose:          Predict at session start whether each participant is
                  likely to meet the attendance threshold — enables proactive
                  warnings to mentors and participants.
Input Features:
  - historical_session_completion_rate per user
  - current_day_activity_level (device active?)
  - prior_no_show_rate (rolling 30-day)
  - session_type (RANKED/CERTIFICATION = higher completion motivation)
  - time_of_day (user's historical engagement by hour)
  - days_since_last_session
Output:           predicted_completion_probability (float 0.0–1.0)
                  early_warning = TRUE if < 0.60
Trigger:          Fired at SESSION_LIVE event, per participant
Use:              Feeds NOTIFICATION_AGENT (mentor/admin early warning)
Training Freq:    Weekly
Drift Detection:  Monitor prediction accuracy vs. actual completion weekly

FEATURES_USED:    See model definitions above — explicit per model
VERSION_CONTROL:  model_version stored in every attendance record output;
                  immutable reference; previous versions retained for audit
```

### AI Usage (Supplementary — ~22% of decisions)

```
AI_USAGE_SCOPE:

  AI_1: ATTENDANCE_ANOMALY_EXPLANATION_GENERATOR
    Purpose:     Generate human-readable explanation for anomaly flags
                 shown to ADMIN/MENTOR on session review dashboards
    Trigger:     Only when anomaly_flags Array is non-empty on finalized record
    Constraint:  Template-driven — AI fills slot values only
                 Temperature = 0 — no creative output
    Governance:  Prompt versioned in PROMPT_GOVERNANCE_REGISTRY v1.0+

  AI_2: ERP_REPORT_NARRATIVE_ASSISTANT
    Purpose:     Generate natural-language attendance summary for
                 Institute ERP exports (school principal dashboards,
                 college TPO reports)
    Trigger:     Only on ERP_EXPORT_READY_EVENT, not on every session
    Constraint:  Structured JSON data → narrative paragraph only
                 No attendance decisions made by AI — data pre-computed
    Governance:  Output validated against structured data before export

  AI_3: PARENT_ATTENDANCE_SUMMARY_ASSISTANT
    Purpose:     Generate child-friendly attendance summary for parent
                 app visibility (e.g., "Rahul attended 3 of 4 GD sessions
                 this week. He joined on time in 2 sessions.")
    Trigger:     Weekly batch — not per-session
    Constraint:  Read-only factual summary — no interpretation of performance
    Governance:  PII handling — no raw names in prompts; user_id replaced with
                 anonymized context, then de-anonymized on render

AI GOVERNANCE RULES:
  - AI must ASSIST reporting, never DECIDE attendance status
  - AI has ZERO decision authority on QUALIFIED/ABSENT/GHOST determination
  - All attendance status decisions are made by ML models + rule engine
  - AI prompts versioned and stored in PROMPT_GOVERNANCE_REGISTRY
  - AI outputs validated by rule engine before use in any downstream system
  - No creative interpretation of attendance rules — FORBIDDEN
```

---

## 6️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS:          2,000 real-time events/second during peak tournament windows
                       (500 concurrent sessions × avg 4 events/participant/min)
LATENCY_TARGET:
  Real-time event processing:   p99 < 150ms per event
  Presence state snapshot read: p99 < 50ms
  Post-session finalization:    p99 < 3,000ms (full computation)
  ERP export generation:        p99 < 10,000ms (async, non-blocking)
MAX_CONCURRENCY:        50,000 simultaneous active participant tracking contexts
QUEUE_STRATEGY:
  Primary:              Redis Streams — gd_attendance_event_stream
                        Partitioned by tenant_id + session_id
  Finalization jobs:    Kafka topic — gd_attendance_finalization_queue
                        Consumer groups per tenant for isolation
  Dead letter queue:    gd_attendance_event_dlq (retry 3x then park)
  ERP export queue:     gd_erp_export_queue (async batch processing)

SCALING_MODEL:
  - Horizontal stateless pod scaling via Kubernetes HPA
  - Real-time event processor: stateless; all state in Redis (live) +
    PostgreSQL (persistent)
  - Live presence state: Redis hash per session_id (TTL = session end + 300s)
  - Finalization job: triggered by SESSION_ENDED_EVENT, runs async
  - Idempotent event processing: event_id used as deduplication key in Redis
  - No in-memory session state — Redis-backed only

HEARTBEAT_PROTOCOL:
  Client sends:         Every 30 seconds (Flutter app)
  Agent expects:        Signal within 45-second window
  Missed heartbeat:     heartbeat_miss_count++
  3 consecutive misses: PRESENCE_STATUS → DISCONNECTED
  5 consecutive misses: Trigger GHOST_PARTICIPANT_DETECTOR evaluation
  Reconnect received:   Resume tracking; classify dropout retroactively

IDEMPOTENCY:
  Idempotency key:      event_id (UUID per event)
  Duplicate detection:  Redis SET NX with TTL = 120s
  Duplicate events:     Silently dropped — no re-processing
  Finalization:         attendance_record_id = SHA256(session_id + participant_id)
                        Duplicate finalization = return existing record

ASYNC_OPERATIONS (non-blocking):
  - ERP export generation
  - Parent visibility record publishing
  - Feature vector emission to FEATURE_STORE_AGENT
  - XP/Streak eligibility signals to GROWTH_ENGINE
  - Notification dispatch

SYNC_OPERATIONS (blocking — failure halts finalization):
  - Ghost detection score computation
  - Anti-collusion cross-reference
  - Attendance threshold compliance check
  - Scoring eligibility attestation
  - Audit record creation
```

---

## 7️⃣ SECURITY ENFORCEMENT

```
SECURITY MODEL: ZERO-TRUST MULTI-TENANT

TENANT ISOLATION:
  ✓ All attendance records scoped by tenant_id (PostgreSQL RLS enforced)
  ✓ Redis keys namespaced: attendance:{tenant_id}:{session_id}:{participant_id}
  ✓ No cross-tenant event processing — tenant_id mismatch = REJECT + ANOMALY_FLAG
  ✓ ERP exports scoped to requesting tenant's institution only
  ✓ Kafka consumer groups partitioned by tenant_id

ROLE-BASED AUTHORIZATION (RBAC):
  Operation                    | STUDENT | MENTOR | EVALUATOR | ADMIN | PARENT | SYSTEM
  ─────────────────────────────|─────────|────────|───────────|───────|────────|───────
  View own attendance record   |  ✓      |  ✓     |  ✓        |  ✓    |  ✗*    |  ✓
  View session attendance list |  ✗      |  ✓     |  ✓        |  ✓    |  ✗     |  ✓
  View child's attendance      |  ✗      |  ✗     |  ✗        |  ✓    |  ✓*    |  ✓
  Submit manual correction     |  ✗      |  ✓**   |  ✗        |  ✓    |  ✗     |  ✗
  Export ERP report            |  ✗      |  ✗     |  ✗        |  ✓    |  ✗     |  ✓
  View audit trail             |  ✗      |  ✗     |  ✗        |  ✓    |  ✗     |  ✓
  Waive ghost flag             |  ✗      |  ✗     |  ✗        |  ✓    |  ✗     |  ✗
  ─────────────────────────────────────────────────────────────────────────────────────
  * PARENT: read-only, child's records only, within their tenant + institution scope
  ** MENTOR: limited corrections (ADJUST_DURATION, OVERRIDE_NETWORK_DROPOUT only)
             MARK_PRESENT and WAIVE_GHOST_FLAG require ADMIN

EVENT CHANNEL AUTHENTICATION:
  ✓ LiveKit webhooks: HMAC-SHA256 signature verified on every event
  ✓ Flutter heartbeat: Session-scoped JWT (RS256, 15-min refresh)
  ✓ WebSocket events: Session-scoped token, role-bound
  ✓ Internal system events: Service-to-service mTLS

DATA ENCRYPTION:
  ✓ All attendance records encrypted at rest (AES-256)
  ✓ PII (participant_id, mentor_id) referenced by UUID only in logs
  ✓ device_fingerprint_hash stored as SHA256 — no raw device data
  ✓ ERP export payloads: encrypted in transit (TLS 1.3 minimum)
  ✓ Parent visibility records: scoped access tokens, time-limited

ABUSE DETECTION:
  ✓ Event rate spike per participant: > 120/min = ANOMALY_FLAG
  ✓ Multi-device simultaneous join: device_fingerprint_hash collision = FLAG
  ✓ IP block clustering in same session: >= 4 participants same /24 = FLAG
  ✓ Synchronized join/leave (< 2 seconds apart, repeatedly): FRAUD_PATTERN flag
  ✓ VPN/proxy detection signals from network layer fed as anomaly enrichment
  ✓ Manual correction abuse: > 3 corrections per session by same actor = escalate

RECORDING CONSENT COMPLIANCE:
  ✓ No attendance record marked QUALIFIED without consent_status = ACKNOWLEDGED
    for sessions where recording_enabled = TRUE
  ✓ CONSENT_DECLINED participants: attendance tracked but recording excluded
  ✓ CONSENT_PENDING at session end: attendance marked PARTIAL pending resolution
  ✓ Consent records stored immutably — no retroactive consent modification
```

---

## 8️⃣ AUDIT & TRACEABILITY

Every operation of this agent — event processing, finalization, correction —
must emit an immutable audit log entry. Append-only. No exceptions.

```json
AUDIT_LOG_SCHEMA: {
  "audit_id":               "UUID — primary key",
  "timestamp_utc":          "ISO8601",
  "agent_id":               "GD_ATTENDANCE_TRACKING_AGENT",
  "agent_version":          "string — semver",
  "operation_type":         "ENUM[EVENT_PROCESSED | EVENT_REJECTED |
                             ATTENDANCE_FINALIZED | MANUAL_CORRECTION_APPLIED |
                             GHOST_FLAG_RAISED | GHOST_FLAG_WAIVED |
                             FRAUD_PATTERN_DETECTED | ERP_EXPORT_GENERATED |
                             ANOMALY_ESCALATED | HEARTBEAT_MISS_LOGGED]",
  "tenant_id":              "UUID",
  "session_id":             "UUID",
  "actor_id":               "UUID — system agent or human actor",
  "actor_role":             "string",
  "participant_id":         "UUID | null — per-participant ops",
  "input_hash":             "SHA256 — hash of input event or request payload",
  "output_hash":            "SHA256 — hash of output record",
  "ml_models_invoked":      "Array<{model_id, model_version, output_score}>",
  "decision_path":          "Array<{step_name, input_snapshot, result, rule_applied}>",
  "confidence_score":       "float 0.0–1.0",
  "anomaly_flags":          "Array<{flag_type, flag_reason, severity}>",
  "correction_before":      "object | null — previous state before correction",
  "correction_after":       "object | null — new state after correction",
  "correction_reference":   "UUID | null",
  "failure_reason":         "string | null",
  "escalation_target":      "string | null"
}

AUDIT ENFORCEMENT:
  ✓ INSERT-ONLY — no UPDATE or DELETE on audit table ever
  ✓ Encrypted at rest
  ✓ Minimum 7-year retention (compliance requirement for certification records)
  ✓ Partitioned by tenant_id + month for query performance
  ✓ Exportable via /audit/export API (ADMIN role only, rate-limited)
  ✓ Correction audit: always captures BEFORE and AFTER state
  ✓ Ghost flag waiver: always references GOVERNANCE_AGENT approval UUID
```

---

## 9️⃣ FAILURE POLICY

```
FAILURE SCENARIOS AND MANDATORY RESPONSES:

  ════════════════════════════════════════════════════════
  SCENARIO: INVALID OR MALFORMED REAL-TIME EVENT
  ════════════════════════════════════════════════════════
  Response:
    → DROP event (no crash — stream must remain alive)
    → LOG_INCIDENT (event_validation_failure)
    → Emit EVENT_REJECTED_ANOMALY to OBSERVABILITY_AGENT
    → Increment invalid_event_rate counter
    → If invalid_event_rate > 5% in 60s window: ALERT + escalate to INFRA

  ════════════════════════════════════════════════════════
  SCENARIO: PARTICIPANT NOT IN SESSION ROSTER (Unknown actor)
  ════════════════════════════════════════════════════════
  Response:
    → REJECT event immediately
    → LOG_INCIDENT (unauthorized_participant_signal — SECURITY category)
    → Emit SECURITY_ANOMALY to SECURITY_INCIDENT_RESPONSE_AGENT
    → Do NOT create any presence record for unknown actor

  ════════════════════════════════════════════════════════
  SCENARIO: HEARTBEAT STREAM INTERRUPTED (No signal > 45s)
  ════════════════════════════════════════════════════════
  Response:
    → heartbeat_miss_count++
    → After 3 consecutive misses: PRESENCE_STATUS → DISCONNECTED
    → Log missed heartbeat with timestamp
    → NOTIFY NOTIFICATION_AGENT (participant connectivity warning)
    → After 5 consecutive misses: trigger GHOST_PARTICIPANT_DETECTOR
    → If reconnect arrives: resume tracking; classify dropout retroactively
    → If no reconnect by session end: NETWORK_DROPOUT_CLASSIFIER applied

  ════════════════════════════════════════════════════════
  SCENARIO: GHOST_PARTICIPANT_DETECTOR RETURNS SCORE >= 0.72
  ════════════════════════════════════════════════════════
  Response:
    → Set ghost_flagged = TRUE on live presence state
    → Continue tracking (do not force-remove participant)
    → On finalization: final_attendance_status = GHOST
    → scoring_eligible = FALSE
    → xp_eligible = FALSE
    → belt_eligible = FALSE
    → Emit GHOST_DETECTED_EVENT to SCORING_ENGINE + COLLUSION_DETECTION_AGENT
    → LOG_INCIDENT immutably
    → Notify MENTOR (if present) via NOTIFICATION_AGENT

  ════════════════════════════════════════════════════════
  SCENARIO: FRAUD_PATTERN_DETECTOR SCORE >= 0.65
  ════════════════════════════════════════════════════════
  Response:
    → Set investigation_required = TRUE in session fraud snapshot
    → Continue session (do not interrupt live session)
    → On finalization: flag all implicated participants' records
    → Emit FRAUD_PATTERN_EVENT to COLLUSION_DETECTION_AGENT + COMPLIANCE_AGENT
    → LOG_INCIDENT (P1 priority)
    → ESCALATE_TO: COMPLIANCE_AGENT + TENANT_ADMIN
    → Scoring and belt eligibility suspended pending investigation

  ════════════════════════════════════════════════════════
  SCENARIO: FINALIZATION JOB FAILS (DB error, model timeout)
  ════════════════════════════════════════════════════════
  Response:
    → RETRY_POLICY: 3 retries, exponential backoff (1s, 2s, 4s)
    → If all retries fail:
        → Park session in gd_attendance_finalization_failed queue
        → LOG_INCIDENT (finalization_failure — P1)
        → ESCALATE_TO: INFRASTRUCTURE_ONCALL_AGENT
        → Set attendance_record status = PENDING_FINALIZATION
        → Do NOT emit downstream triggers until finalization succeeds
        → Notify OBSERVABILITY_AGENT

  ════════════════════════════════════════════════════════
  SCENARIO: ATTENDANCE CONFIDENCE SCORE < 0.60
  ════════════════════════════════════════════════════════
  Response:
    → Set attendance_record status = LOW_CONFIDENCE
    → LOG_INCIDENT (low_confidence_attendance)
    → Suppress scoring_eligible, xp_eligible, belt_eligible
    → ESCALATE_TO: HUMAN_REVIEW_QUEUE
    → Notify requesting tenant ADMIN
    → Attendance triggers released only after human review confirmation

  ════════════════════════════════════════════════════════
  SCENARIO: LIVKIT WEBHOOK UNAVAILABLE (No room events > 60s during LIVE session)
  ════════════════════════════════════════════════════════
  Response:
    → Switch to HEARTBEAT_ONLY mode
    → Log LIVEKIT_SIGNAL_LOSS incident
    → Continue attendance tracking using Flutter heartbeat + WebSocket only
    → Flag session as DEGRADED_SIGNAL in attendance record
    → Alert OBSERVABILITY_AGENT
    → On session end: ghost detection uses available signals only
    → Flag confidence_score penalty: -0.15 on overall record

  ════════════════════════════════════════════════════════
  SCENARIO: MENTOR ABSENT FROM CERTIFICATION SESSION
  ════════════════════════════════════════════════════════
  Response:
    → Continue tracking attendance (session may still proceed as PRACTICE)
    → Set mentor_attendance_record.certification_authority_valid = FALSE
    → Emit MENTOR_ABSENT_CERTIFICATION_SESSION_EVENT to CERTIFICATE_ENGINE
    → Belt eligibility from this session: SUSPENDED (requires governance review)
    → LOG_INCIDENT (mentor_absence_certification — P2)
    → Notify ADMIN via NOTIFICATION_AGENT immediately

  ════════════════════════════════════════════════════════
  SCENARIO: MANUAL CORRECTION UNAUTHORIZED (role violation)
  ════════════════════════════════════════════════════════
  Response:
    → REJECT correction immediately
    → LOG_INCIDENT (unauthorized_correction_attempt — SECURITY category)
    → Emit SECURITY_ANOMALY to SECURITY_INCIDENT_RESPONSE_AGENT
    → Do NOT apply any change
    → Return 403 Forbidden with audit reference

RETRY_POLICY (general):
  Max retries:          3
  Backoff:              Exponential — 1s, 2s, 4s
  Non-retryable:        Security violations, authorization failures, data integrity errors
  Retryable:            Network timeouts, DB transient errors, dependency 503s

ESCALATION_TARGETS:
  Ghost flag raised:    COLLUSION_DETECTION_AGENT + MENTOR (if present)
  Fraud pattern:        COMPLIANCE_AGENT + TENANT_ADMIN + GOVERNANCE_AGENT (P1)
  Finalization failure: INFRASTRUCTURE_ONCALL_AGENT (P1)
  Security anomaly:     SECURITY_INCIDENT_RESPONSE_AGENT (P0)
  Low confidence:       HUMAN_REVIEW_QUEUE + TENANT_ADMIN
  Data integrity:       SECURITY_INCIDENT_RESPONSE_AGENT + PLATFORM_ADMIN (P0)
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP

```
UPSTREAM DEPENDENCIES (this agent consumes from):

  GD_SESSION_SCHEDULER_AGENT
    Provides: session roster, session_type, domain_track, scheduled times,
              mentor_id, scenario, billing reference, fairness snapshot
    Trigger:  SESSION_SCHEDULED_EVENT → pre-warm tracking context
    Failure:  Cannot track attendance without session context → STOP

  LIVEKIT_WEBHOOK_SERVICE
    Provides: join/leave/disconnect/metadata events (real-time)
    Protocol: HMAC-signed HTTP POST webhooks
    Failure:  Switch to HEARTBEAT_ONLY mode + flag DEGRADED_SIGNAL

  FLUTTER_CLIENT_HEARTBEAT_SERVICE
    Provides: 30-second participant presence pings (JWT-authenticated)
    Failure:  heartbeat_miss_count tracking, DISCONNECTED after 3 misses

  WEBSOCKET_GAME_EVENT_SERVICE
    Provides: In-session interaction signals (game events channel)
    Purpose:  Enriches ghost detection with interaction activity signals
    Failure:  Degrade gracefully; ghost model uses remaining features

  CONSENT_CAPTURE_AGENT
    Provides: CONSENT_ACKNOWLEDGED / CONSENT_DECLINED events per participant
    Failure:  consent_status = PENDING; QUALIFIED status blocked until resolved

  COLLUSION_DETECTION_AGENT
    Provides: Anomaly enrichment signals, fraud pattern alerts
    Provides: Cross-session pair history for fraud pattern model
    Failure:  Continue tracking; flag fraud model as DEGRADED

  TENANT_CONTEXT_AGENT
    Provides: Tenant plan, domain access, institute/enterprise type,
              attendance threshold configuration per tenant
    Failure:  Use platform-default thresholds; flag record as DEFAULT_THRESHOLD

  TENANT_ERP_CONFIG_AGENT
    Provides: ERP export format preferences, report field mappings,
              institution-specific compliance requirements
    Failure:  Use standard ERP export format; flag as DEFAULT_FORMAT

DOWNSTREAM TRIGGERS (events this agent emits):

  SCORING_ENGINE
    Event: SCORING_ATTESTATION_EVENT
    Payload: {session_id, participant_id, scoring_eligible, ghost_flagged,
              present_percentage, anti_ghost_attestation_signature}
    Rule: Scoring engine MUST NOT process scores for ghost_flagged participants

  CERTIFICATE_ENGINE
    Event: BELT_ELIGIBILITY_CHECK_EVENT
    Payload: {session_id, participant_id, belt_eligible, attendance_threshold_met,
              session_type, mentor_certification_authority_valid}
    Rule: Belt eligibility check only if belt_eligible = TRUE

  GROWTH_ENGINE / STREAK_ENGINE
    Event: XP_ELIGIBILITY_EVENT + STREAK_UPDATE_EVENT
    Payload: {participant_id, session_id, xp_eligible, streak_eligible,
              attendance_qualified, session_type}

  LEADERBOARD_UPDATE_AGENT
    Event: ATTENDANCE_QUALIFIED_SESSION_EVENT
    Rule: Only qualified attendance records trigger leaderboard consideration

  ANALYTICS_AGENT
    Event: ATTENDANCE_FINALIZED_EVENT (full record payload)
    Purpose: Attendance pipeline for learning effectiveness analytics,
             scenario pass rate analysis, domain engagement metrics

  NOTIFICATION_AGENT
    Events: PARTICIPANT_LATE_JOIN_WARNING, HEARTBEAT_MISS_ALERT,
            GHOST_DETECTION_ALERT (to mentor), SESSION_COMPLETION_SUMMARY,
            ERP_EXPORT_READY, LOW_ATTENDANCE_ALERT (to admin)

  PARENT_VISIBILITY_SERVICE
    Event: PARENT_RECORD_READY_EVENT
    Payload: child attendance summary (anonymized for prompt, rendered with names)

  INSTITUTE_ERP_AGENT / ENTERPRISE_ERP_AGENT
    Event: ERP_EXPORT_READY_EVENT
    Payload: Structured attendance export (tenant-format specific)

  COLLUSION_DETECTION_AGENT
    Event: GHOST_DETECTED_EVENT, FRAUD_PATTERN_EVENT
    Payload: Full anomaly context for cross-session investigation

  OBSERVABILITY_AGENT
    Event: All lifecycle events, all anomaly flags, all performance metrics

  FEATURE_STORE_AGENT
    Event: ATTENDANCE_FEATURE_VECTOR_EVENT (see Section 11)

  COMPLIANCE_AGENT
    Event: CERTIFICATION_MENTOR_ABSENT_EVENT, FRAUD_PATTERN_EVENT

EVENT_SCHEMA (all events):
  {
    "event_id":           "UUID",
    "event_type":         "string",
    "schema_version":     "string",
    "tenant_id":          "UUID",
    "session_id":         "UUID",
    "participant_id":     "UUID | null",
    "emitted_by":         "GD_ATTENDANCE_TRACKING_AGENT",
    "agent_version":      "string",
    "timestamp_utc":      "ISO8601",
    "payload":            "object — event-type specific",
    "audit_reference":    "UUID"
  }
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent captures the richest behavioral signal set on the platform — every
participant's real presence pattern across every session. These signals must be
emitted to FEATURE_STORE_AGENT after every finalized session, per participant.

```json
EMIT_FEATURE_VECTOR: {
  "user_id":          "UUID — each participant individually",
  "feature_name":     "gd_attendance_behavioral_profile",
  "feature_value": {
    "session_type":                 "string",
    "domain_track":                 "string",
    "attendance_status":            "string — QUALIFIED|PARTIAL|ABSENT|GHOST",
    "present_percentage":           "float",
    "attendance_threshold_met":     "boolean",
    "late_join_seconds":            "integer",
    "early_leave_seconds":          "integer",
    "network_dropout_count":        "integer",
    "heartbeat_miss_count":         "integer",
    "ghost_detection_score":        "float",
    "ghost_flagged":                "boolean",
    "network_dropout_classification": "string",
    "fraud_risk_score":             "float",
    "time_of_day_slot":             "string",
    "day_of_week":                  "integer 1–7",
    "session_duration_minutes":     "integer",
    "mentor_present":               "boolean",
    "consecutive_sessions_attended":"integer — rolling streak count",
    "correction_applied":           "boolean"
  },
  "timestamp_utc":    "ISO8601",
  "source_agent":     "GD_ATTENDANCE_TRACKING_AGENT",
  "schema_version":   "v1.0.0"
}
```

These vectors feed:
- PARTICIPANT_AVAILABILITY_PREDICTOR (GD_SESSION_SCHEDULER_AGENT model)
- GHOST_PARTICIPANT_DETECTOR retraining
- ATTENDANCE_THRESHOLD_PREDICTOR retraining
- NETWORK_DROPOUT_CLASSIFIER retraining
- Personalized session time recommendation engine
- Engagement + churn prediction models
- Learning effectiveness analysis (ANALYTICS_AGENT)

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY

This agent does not directly handle idea vectors or innovation scoring.
However, attendance at innovation-type GD sessions (domain = ARTS or TECHNOLOGY,
scenario_type = INNOVATION) constitutes a qualifying signal for the Innovation
Economy layer. This agent emits:

```json
EMIT_IF_INNOVATION_SESSION_ATTENDED: {
  "session_id":           "UUID",
  "tenant_id":            "UUID",
  "participant_id":       "UUID",
  "domain_track":         "string",
  "scenario_type":        "INNOVATION | STANDARD",
  "attendance_qualified": "boolean",
  "present_percentage":   "float",
  "timestamp_utc":        "ISO8601",
  "trigger_type":         "INNOVATION_SESSION_ATTENDANCE_QUALIFIED",
  "source_agent":         "GD_ATTENDANCE_TRACKING_AGENT"
}
```

Downstream: IDEA_DNA_AGENT uses this to validate that idea submission came
from a participant with verified session presence — preventing ghost-attended
innovation fraud. ROYALTY_ENGINE requires qualified attendance before royalty
attribution is valid.

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

This agent drives three categories of growth signals, all conditional on
verified attendance quality:

```json
GROWTH_ENGINE_TRIGGERS: {

  "STREAK_UPDATE_EVENT": {
    "participant_id":       "UUID",
    "session_id":           "UUID",
    "tenant_id":            "UUID",
    "streak_eligible":      "boolean",
    "streak_event_type":    "ENUM[SESSION_ATTENDED | SESSION_MISSED | STREAK_BROKEN]",
    "consecutive_count":    "integer — current streak value"
  },

  "XP_UPDATE_EVENT": {
    "participant_id":       "UUID",
    "session_id":           "UUID",
    "tenant_id":            "UUID",
    "xp_eligible":          "boolean",
    "xp_event_type":        "ENUM[QUALIFIED_ATTENDANCE | PERFECT_ATTENDANCE |
                             ON_TIME_JOIN | FULL_SESSION_COMPLETED]",
    "xp_multiplier_signal": "float — 1.0 base, up to 1.5 for perfect attendance"
  },

  "SHARE_TRIGGER_EVENT": {
    "participant_id":       "UUID",
    "session_id":           "UUID",
    "tenant_id":            "UUID",
    "share_prompt_type":    "ENUM[ATTENDANCE_STREAK_MILESTONE |
                             PERFECT_ATTENDANCE_WEEK | CERTIFICATION_ATTENDED]"
  }
}

RULES:
  - GHOST_FLAGGED participants: NO streak, NO XP, NO share trigger
  - ABSENT participants: streak_event_type = SESSION_MISSED → streak counter reset
  - PARTIAL attendance (< threshold): XP reduced proportionally; no streak increment
  - QUALIFIED attendance: full XP + streak increment
  - Perfect attendance (100% present_percentage, on time): xp_multiplier_signal = 1.5
  - This agent fires triggers only — GROWTH_ENGINE computes actual values
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

```yaml
METRICS (emitted to OBSERVABILITY_AGENT — Prometheus format):

  Real-Time Tracking:
    - attendance_event_processing_rate          (events/sec — target stable)
    - attendance_event_processing_latency_p99   (target: < 150ms)
    - attendance_event_rejection_rate           (alert: > 1%)
    - heartbeat_miss_rate_per_session           (alert: > 20% of participants)
    - livekit_signal_loss_duration_seconds      (alert: > 30s)
    - websocket_presence_event_lag_ms           (alert: > 500ms)

  Attendance Quality:
    - ghost_detection_flag_rate                 (by tenant — alert: > 5%)
    - ghost_false_positive_rate                 (monthly — target: < 5%)
    - fraud_pattern_detection_rate              (alert: any spike)
    - attendance_threshold_met_rate             (by session_type, domain)
    - manual_correction_rate                    (alert: > 3% of sessions)
    - low_confidence_finalization_rate          (alert: > 2%)

  Finalization Pipeline:
    - attendance_finalization_success_rate      (target: > 99.8%)
    - attendance_finalization_latency_p99_ms    (target: < 3,000ms)
    - finalization_retry_rate                   (alert: > 0.5%)
    - finalization_failed_queue_depth           (alert: > 0)

  ERP & Reporting:
    - erp_export_generation_success_rate        (target: > 99.9%)
    - erp_export_latency_p99_ms                 (target: < 10,000ms)
    - parent_record_publish_latency_p99_ms      (target: < 5,000ms)

  ML Model Health:
    - ghost_detector_score_distribution_shift   (drift alert: > 15%)
    - fraud_detector_anomaly_rate               (drift alert: any sustained increase)
    - attendance_predictor_accuracy_weekly      (drift alert: < 80%)

DASHBOARDS REQUIRED:
  - Real-Time Session Attendance Heatmap (per tenant)
  - Ghost Detection Rate Trends
  - Fraud Pattern Investigation Queue
  - Manual Correction Frequency by Tenant/Actor
  - ERP Export Pipeline Health
  - ML Model Drift Monitor
  - Parent Visibility Record Lag

ALERTING:
  - attendance_event_rejection_rate > 1%         → Slack Engineering Alert
  - livekit_signal_loss > 30s in any session     → PagerDuty P2
  - fraud_pattern detected (any)                 → PagerDuty P1 + Compliance Queue
  - finalization_failed_queue_depth > 0          → PagerDuty P1
  - security_anomaly (unauthorized_participant)  → PagerDuty P0
  - data_integrity_failure                       → PagerDuty P0
  - mentor_absent_certification_session          → Slack Ops + Tenant Admin
```

---

## 1️⃣5️⃣ VERSIONING POLICY

```
VERSIONING RULES (NON-NEGOTIABLE):

  Current Version:     v1.0.0
  Scheme:              MAJOR.MINOR.PATCH (Semantic Versioning)

  MAJOR bump required when:
    - Any input event schema field added or removed
    - Any finalized attendance record schema field added or removed
    - Ghost detection threshold changes
    - New ML model introduced that changes eligibility decisions
    - New downstream agent dependency added
    - Attendance threshold formula changes

  MINOR bump required when:
    - New optional event fields added
    - New ML model version deployed (same model, new training)
    - New optional output fields added
    - New ERP export format added
    - New anomaly flag type added

  PATCH bump required when:
    - Bug fixes with no behavioral change
    - Performance optimizations
    - Logging/metric additions only
    - Documentation updates

  BACKWARD COMPATIBILITY:
    - Previous MAJOR version supported 90 days post new MAJOR release
    - Finalized attendance records from old versions remain readable forever
    - correction_reference always points to original record version

  ML MODEL VERSIONING:
    - All model versions stored immutably in model registry
    - model_version referenced in every attendance record and audit log
    - Model swap requires MINOR version bump minimum
    - No silent model version changes — declared in deployment changelog

  ROLLBACK SAFETY:
    - Blue/green deployment supported
    - Redis event state can be replayed from Kafka (48-hour retention)
    - DB migrations backward compatible for 1 MAJOR version
    - Finalized records never deleted on rollback — old finalization retained
```

---

## 1️⃣6️⃣ NON-NEGOTIABLE RULES

This agent is **permanently prohibited** from:

```
❌ Creating or modifying attendance records without verified event signals
❌ Marking any participant QUALIFIED based on join signal alone (presence duration required)
❌ Auto-granting belt eligibility without attendance_threshold_met = TRUE
❌ Retroactively modifying finalized attendance records directly — correction overlay only
❌ Deleting or archiving audit log entries under any condition
❌ Allowing ghost-flagged participants to receive XP, streak, or belt eligibility
❌ Allowing STUDENT or PARENT roles to submit manual attendance corrections
❌ Issuing scoring attestation for ghost-flagged participants to SCORING_ENGINE
❌ Processing events from participants not on the session roster
❌ Cross-tenant attendance data access in any query or operation
❌ Mixing domain attendance data across domain tracks
❌ Emitting attendance signals to GROWTH_ENGINE without finalized record completion
❌ Suppressing anomaly flags to OBSERVABILITY_AGENT for any reason
❌ Applying bulk attendance corrections > 5 participants in a single operation
❌ Granting ERP export access to non-ADMIN actors
❌ Treating CONSENT_DECLINED as CONSENT_ACKNOWLEDGED for QUALIFIED determination
❌ Executing ghost detection without minimum required signal data (< 3 signals = LOW_CONFIDENCE)
❌ Allowing AI components to determine attendance status — ML + rule engine only
❌ Operating outside its declared scope (attendance tracking only)
```

---

## 🔒 DATABASE ENTITIES OWNED BY THIS AGENT

```sql
-- ─────────────────────────────────────────────────────────────────
-- LIVE PRESENCE STATE (Redis-backed during session — persistent backup)
-- ─────────────────────────────────────────────────────────────────
-- Primary store: Redis hash (TTL = session_end + 300s)
-- Backup: PostgreSQL gd_live_presence (upsert on each event)

gd_live_presence (
  presence_id               UUID PRIMARY KEY,
  session_id                UUID NOT NULL,
  tenant_id                 UUID NOT NULL,
  participant_id            UUID NOT NULL,
  presence_status           VARCHAR(32) NOT NULL,
  time_in_session_seconds   INTEGER NOT NULL DEFAULT 0,
  last_signal_utc           TIMESTAMPTZ NOT NULL,
  heartbeat_miss_count      INTEGER NOT NULL DEFAULT 0,
  reconnect_count           INTEGER NOT NULL DEFAULT 0,
  network_quality_avg       FLOAT,
  ghost_detection_score     FLOAT,
  ghost_flagged             BOOLEAN NOT NULL DEFAULT FALSE,
  consent_status            VARCHAR(32) NOT NULL DEFAULT 'PENDING',
  anomaly_flags             JSONB,
  updated_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  UNIQUE (session_id, participant_id)
  -- RLS: WHERE tenant_id = current_tenant()
  -- Updated in real-time; superseded by finalized record on session end
)

-- ─────────────────────────────────────────────────────────────────
-- FINALIZED ATTENDANCE RECORDS (IMMUTABLE ON CREATION)
-- ─────────────────────────────────────────────────────────────────
gd_attendance_records (
  attendance_record_id      UUID PRIMARY KEY,
  session_id                UUID NOT NULL,
  tenant_id                 UUID NOT NULL,
  domain_track              VARCHAR(32) NOT NULL,
  session_type              VARCHAR(32) NOT NULL,
  finalized_at_utc          TIMESTAMPTZ NOT NULL,
  actual_duration_minutes   INTEGER NOT NULL,
  agent_version             VARCHAR(32) NOT NULL,
  ml_models_snapshot        JSONB NOT NULL,
  confidence_score          FLOAT NOT NULL,
  quorum_met                BOOLEAN NOT NULL,
  collusion_investigation_required BOOLEAN NOT NULL DEFAULT FALSE,
  compliance_tags           JSONB,
  created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW()
  -- INSERT-ONLY — no UPDATE permitted after creation
  -- RLS: WHERE tenant_id = current_tenant()
)

-- ─────────────────────────────────────────────────────────────────
-- PER-PARTICIPANT ATTENDANCE DETAIL (IMMUTABLE ON CREATION)
-- ─────────────────────────────────────────────────────────────────
gd_participant_attendance (
  id                          UUID PRIMARY KEY,
  attendance_record_id        UUID NOT NULL REFERENCES gd_attendance_records,
  session_id                  UUID NOT NULL,
  tenant_id                   UUID NOT NULL,
  participant_id              UUID NOT NULL,
  final_attendance_status     VARCHAR(32) NOT NULL,
  present_seconds             INTEGER NOT NULL DEFAULT 0,
  present_percentage          FLOAT NOT NULL DEFAULT 0.0,
  attendance_threshold_met    BOOLEAN NOT NULL DEFAULT FALSE,
  join_time_utc               TIMESTAMPTZ,
  leave_time_utc              TIMESTAMPTZ,
  late_join_seconds           INTEGER NOT NULL DEFAULT 0,
  early_leave_seconds         INTEGER NOT NULL DEFAULT 0,
  network_dropout_count       INTEGER NOT NULL DEFAULT 0,
  total_dropout_seconds       INTEGER NOT NULL DEFAULT 0,
  heartbeat_miss_count        INTEGER NOT NULL DEFAULT 0,
  consent_status              VARCHAR(32) NOT NULL,
  ghost_detection_score       FLOAT,
  ghost_flagged               BOOLEAN NOT NULL DEFAULT FALSE,
  fraud_risk_score            FLOAT,
  anomaly_flags               JSONB,
  scoring_eligible            BOOLEAN NOT NULL DEFAULT FALSE,
  xp_eligible                 BOOLEAN NOT NULL DEFAULT FALSE,
  streak_eligible             BOOLEAN NOT NULL DEFAULT FALSE,
  belt_eligible               BOOLEAN NOT NULL DEFAULT FALSE,
  correction_applied          BOOLEAN NOT NULL DEFAULT FALSE,
  correction_reference        UUID,
  created_at                  TIMESTAMPTZ NOT NULL DEFAULT NOW()
  -- INSERT-ONLY — no UPDATE permitted after creation
  -- Corrections create new overlay records, not mutations
  -- RLS: WHERE tenant_id = current_tenant()
)

-- ─────────────────────────────────────────────────────────────────
-- MENTOR ATTENDANCE DETAIL (IMMUTABLE ON CREATION)
-- ─────────────────────────────────────────────────────────────────
gd_mentor_attendance (
  id                              UUID PRIMARY KEY,
  attendance_record_id            UUID NOT NULL REFERENCES gd_attendance_records,
  session_id                      UUID NOT NULL,
  tenant_id                       UUID NOT NULL,
  mentor_id                       UUID,
  present                         BOOLEAN NOT NULL DEFAULT FALSE,
  present_seconds                 INTEGER NOT NULL DEFAULT 0,
  present_percentage              FLOAT NOT NULL DEFAULT 0.0,
  certification_authority_valid   BOOLEAN NOT NULL DEFAULT FALSE,
  override_events_count           INTEGER NOT NULL DEFAULT 0,
  created_at                      TIMESTAMPTZ NOT NULL DEFAULT NOW()
  -- INSERT-ONLY
)

-- ─────────────────────────────────────────────────────────────────
-- MANUAL CORRECTION OVERLAY (ADD-ONLY — never mutates base records)
-- ─────────────────────────────────────────────────────────────────
gd_attendance_corrections (
  correction_id               UUID PRIMARY KEY,
  original_participant_att_id UUID NOT NULL REFERENCES gd_participant_attendance,
  session_id                  UUID NOT NULL,
  tenant_id                   UUID NOT NULL,
  participant_id              UUID NOT NULL,
  requested_by                UUID NOT NULL,
  requested_by_role           VARCHAR(32) NOT NULL,
  correction_type             VARCHAR(64) NOT NULL,
  correction_reason           TEXT NOT NULL,
  supporting_evidence_ref     UUID NOT NULL,
  governance_approval_ref     UUID,
  state_before                JSONB NOT NULL,
  state_after                 JSONB NOT NULL,
  applied_at_utc              TIMESTAMPTZ NOT NULL,
  agent_version               VARCHAR(32) NOT NULL
  -- INSERT-ONLY — corrections are overlays, not mutations
)

-- ─────────────────────────────────────────────────────────────────
-- IMMUTABLE AUDIT LOG
-- ─────────────────────────────────────────────────────────────────
gd_attendance_audit_log (
  audit_id              UUID PRIMARY KEY,
  timestamp_utc         TIMESTAMPTZ NOT NULL,
  agent_id              VARCHAR(64) NOT NULL,
  agent_version         VARCHAR(32) NOT NULL,
  operation_type        VARCHAR(64) NOT NULL,
  tenant_id             UUID NOT NULL,
  session_id            UUID,
  actor_id              UUID NOT NULL,
  actor_role            VARCHAR(32) NOT NULL,
  participant_id        UUID,
  input_hash            VARCHAR(64) NOT NULL,
  output_hash           VARCHAR(64),
  ml_models_invoked     JSONB,
  decision_path         JSONB NOT NULL,
  confidence_score      FLOAT,
  anomaly_flags         JSONB,
  correction_before     JSONB,
  correction_after      JSONB,
  correction_reference  UUID,
  failure_reason        TEXT,
  escalation_target     VARCHAR(128)
  -- INSERT-ONLY — no UPDATE or DELETE ever
  -- Encrypted at rest
  -- Partitioned by tenant_id + created_month
)

-- ─────────────────────────────────────────────────────────────────
-- ERP EXPORT LOG (audit of all exports delivered)
-- ─────────────────────────────────────────────────────────────────
gd_erp_exports (
  export_id             UUID PRIMARY KEY,
  tenant_id             UUID NOT NULL,
  institution_id        UUID,
  cohort_id             UUID,
  export_type           VARCHAR(32) NOT NULL,
  export_format         VARCHAR(32) NOT NULL,
  session_ids_included  UUID[] NOT NULL,
  requested_by          UUID NOT NULL,
  generated_at_utc      TIMESTAMPTZ NOT NULL,
  export_hash           VARCHAR(64) NOT NULL,
  delivered             BOOLEAN NOT NULL DEFAULT FALSE,
  delivered_at_utc      TIMESTAMPTZ,
  agent_version         VARCHAR(32) NOT NULL
  -- INSERT-ONLY
)
```

---

## 🔒 API CONTRACT

```yaml
API ENDPOINTS (FastAPI — versioned):

  GET  /api/v1/attendance/sessions/{session_id}/live
    Auth:     JWT Bearer (MENTOR, EVALUATOR, ADMIN roles)
    Purpose:  Real-time presence snapshot for active session
    Output:   REALTIME_PRESENCE_OUTPUT

  GET  /api/v1/attendance/sessions/{session_id}/record
    Auth:     JWT Bearer (tenant-scoped, role-checked)
    Purpose:  Finalized attendance record for ended session
    Output:   FINALIZED_ATTENDANCE_OUTPUT

  GET  /api/v1/attendance/participants/{participant_id}/history
    Auth:     JWT Bearer (own record: any role | others: ADMIN/MENTOR)
    Query:    tenant_id, from_utc, to_utc, session_type, domain_track, page, limit
    Output:   Array<PARTICIPANT_ATTENDANCE_SUMMARY>

  GET  /api/v1/attendance/sessions/{session_id}/audit
    Auth:     JWT Bearer (ADMIN role only)
    Output:   Array<ATTENDANCE_AUDIT_LOG_ENTRY>

  POST /api/v1/attendance/corrections
    Auth:     JWT Bearer (ADMIN or MENTOR role only)
    Input:    MANUAL_CORRECTION_INPUT_SCHEMA
    Output:   CORRECTION_CONFIRMATION_RESPONSE
    Idempotency-Key: header required

  POST /api/v1/attendance/exports/erp
    Auth:     JWT Bearer (ADMIN role only)
    Input:    {tenant_id, institution_id?, cohort_id?, from_utc, to_utc,
               session_type?, export_format}
    Output:   ERP_EXPORT_JOB_REFERENCE (async — result delivered via webhook)

  GET  /api/v1/attendance/parents/{child_participant_id}/summary
    Auth:     JWT Bearer (PARENT role — own child only, tenant-scoped)
    Output:   PARENT_ATTENDANCE_SUMMARY (read-only, anonymized device data)

  GET  /api/v1/attendance/sessions/{session_id}/ghost-report
    Auth:     JWT Bearer (ADMIN role only)
    Output:   GHOST_DETECTION_REPORT per participant

RATE LIMITS:
  GET  /live:         500 requests/minute per tenant
  GET  /record:       2000 requests/minute per tenant
  GET  /history:      1000 requests/minute per actor
  POST /corrections:  20 requests/hour per actor
  POST /exports/erp:  10 requests/hour per tenant
```

---

## 🔒 ATTENDANCE STATUS DETERMINATION RULES (RULE ENGINE — LOCKED)

```
FINAL STATUS DETERMINATION LOGIC (evaluated at finalization):

  QUALIFIED:
    ✓ present_percentage >= attendance_threshold (default 80%, tenant-configurable min 70%)
    ✓ ghost_flagged = FALSE
    ✓ consent_status = ACKNOWLEDGED (if recording_enabled = TRUE)
    ✓ No active fraud investigation on participant
    ✓ confidence_score >= 0.60

  PARTIAL:
    ✓ present_percentage >= 50% AND < attendance_threshold
    ✓ ghost_flagged = FALSE
    ✓ No active fraud investigation
    → XP reduced proportionally; no streak increment; no belt eligibility

  GHOST:
    ✓ ghost_flagged = TRUE (ghost_detection_score >= 0.72)
    → scoring_eligible = FALSE; xp_eligible = FALSE;
       streak_eligible = FALSE; belt_eligible = FALSE
    → Automatically referred to COLLUSION_DETECTION_AGENT

  ABSENT:
    ✓ present_percentage < 50%
    ✓ OR participant never joined (join_time_utc = NULL)
    → streak_event_type = SESSION_MISSED; streak reset
    → No XP; no belt eligibility

  DISQUALIFIED:
    ✓ Active fraud investigation + fraud_risk_score >= 0.65
    ✓ OR forced_exit_reason = BEHAVIORAL_SAFETY
    ✓ OR CONSENT_DECLINED for required recording session
    → All eligibility flags FALSE; no exceptions

  LOW_CONFIDENCE:
    ✓ confidence_score < 0.60 (insufficient signal data)
    → Status = LOW_CONFIDENCE; all eligibility suspended pending human review

ATTENDANCE THRESHOLD CONFIGURATION:
  Platform default:         80% present_percentage
  Tenant minimum:           70% (cannot be set below this)
  Tenant maximum:           100%
  Certification sessions:   Tenant threshold OR 90%, whichever is HIGHER
  PRACTICE sessions:        Tenant threshold OR 60%, whichever is LOWER
  Threshold stored in:      tenant_erp_config, version-tagged
```

---

## 🔒 DOJO INTEGRATION LOCKS (INHERITED)

```
✓ Audio/video presence signal used for ghost detection ONLY —
  no content analysis, no transcription by this agent
✓ Transport channels strictly separated:
    Video → LiveKit (join/leave/metadata only — no content)
    Game Events → WebSocket (interaction signals only)
    Analytics → Event Bus
    Heartbeat → dedicated Flutter JWT channel
✓ Mentor override events logged immutably per Dojo spec
✓ Certification authority valid = mentor present >= 90% of session duration
✓ Belt promotion: this agent fires eligibility signal only —
  CERTIFICATE_ENGINE + mentor board make the decision
✓ Recording consent captured before session counted as QUALIFIED
✓ Scoring governance: SCORING_ENGINE never receives scores without
  this agent's anti-ghost attestation signature
✓ Safety overrides scoring: FORCED_EXIT_BEHAVIORAL_SAFETY = DISQUALIFIED
  regardless of present_percentage
✓ No silent rubric change: attendance threshold changes require version bump
```

---

## 🔒 INSTITUTE / ENTERPRISE ERP INTEGRATION (LOCKED)

```
INSTITUTE ERP (Schools, Colleges, Universities):
  Export fields mandatory:
    student_id, student_name_ref (UUID), institution_id, session_date,
    session_type, domain_track, attendance_status, present_minutes,
    attendance_percentage, mentor_present, ghost_flagged, report_version
  Formats supported: JSON, CSV, LTI-compatible XML
  Delivery: Webhook to institute's registered endpoint (TLS 1.3 required)
  Frequency: Per session (instant) + Weekly batch report
  Parent visibility: Subset of above — no ghost_flagged field visible to parents

ENTERPRISE ERP (Corporates, SMEs):
  Export fields mandatory:
    employee_id_ref (UUID), enterprise_id, department_id, session_date,
    session_type, domain_track, attendance_status, present_minutes,
    compliance_passed (boolean), report_version
  Formats supported: JSON, CSV, HRIS-compatible XML
  Delivery: Webhook or pull API (/api/v1/attendance/exports/erp)
  Integration: SSO enterprise mode — employee_id_ref linked to HRIS record

COACHING CENTRE ERP:
  Separate tenant scope — cannot access school student records
  Coach admin sees: student_id_ref, session_count, attendance_rate, ranking_eligible
  Cross-institution data: FORBIDDEN per Dojo spec data isolation rules

LTI COMPATIBILITY:
  LTI 1.3 result service support for LMS integration
  Attendance record maps to LTI completion + score passback
  Institution LMS administrator must be ADMIN role within tenant
```

---

## ✅ FINAL GOVERNANCE SEAL

```
╔══════════════════════════════════════════════════════════════════════════════╗
║               GD ATTENDANCE TRACKING AGENT                                  ║
║               ECOSKILLER ANTIGRAVITY v1.0.0                                 ║
║                                                                             ║
║  GOVERNANCE STATUS: FULLY SEALED                                            ║
║                                                                             ║
║  ✓ Agent Identity Locked              ✓ Input Contracts Sealed (2 modes)   ║
║  ✓ Output Contracts Sealed (2 modes)  ✓ ML/AI Logic Governed (4 models)    ║
║  ✓ Scalability Design Defined         ✓ Security Zero-Trust Enforced        ║
║  ✓ Audit Trail Immutable              ✓ Failure Policy Complete (10 cases)  ║
║  ✓ Inter-Agent Map Complete           ✓ Passive Intelligence Compatible     ║
║  ✓ Innovation Economy Compatible      ✓ Growth Engine Hooks Active          ║
║  ✓ Performance Monitoring Defined     ✓ Versioning Policy Locked            ║
║  ✓ Non-Negotiable Rules Sealed        ✓ Database Schema Owned (7 tables)    ║
║  ✓ API Contract Defined               ✓ Status Determination Rules Locked   ║
║  ✓ Dojo Integration Locks Inherited   ✓ ERP Integration Locked              ║
║  ✓ Multi-Tenant Compliance Active     ✓ Ghost Detection Governed            ║
║  ✓ Manual Correction Audit Locked     ✓ Parent Visibility Compliant         ║
║  ✓ Institute + Enterprise ERP Ready   ✓ LTI Compatibility Declared          ║
║                                                                             ║
║  DOJO SAAS PRODUCTION MODE: ENABLED                                        ║
║  ECOSKILLER ANTIGRAVITY: ACTIVE                                             ║
║  ZERO-TRUST SECURITY: ENFORCED                                              ║
║  APPEND-ONLY AUDIT: ENFORCED                                                ║
║  CORRECTION OVERLAY MODEL: ENFORCED (no base record mutation)               ║
║  GHOST DETECTION: ACTIVE (threshold 0.72)                                   ║
║  FRAUD DETECTION: ACTIVE (threshold 0.65)                                   ║
║  MUTATION POLICY: ADD-ONLY VERSIONED                                        ║
║  INTERPRETATION AUTHORITY: NONE                                             ║
║  ARCHITECTURE AUTHORITY: LOCKED                                             ║
║                                                                             ║
║  Absence of any sealed section → STOP EXECUTION                            ║
║  Silent failure → FORBIDDEN                                                 ║
║  Partial output → FORBIDDEN                                                 ║
║  Direct base record mutation → FORBIDDEN                                    ║
║  Ghost participant XP/Belt eligibility → FORBIDDEN                          ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

*Document Version: v1.0.0 | Platform: Ecoskiller Antigravity | Agent: GD_ATTENDANCE_TRACKING_AGENT*
*Dependency: GD_SESSION_SCHEDULER_AGENT v1.0.0+ required upstream*
*Mutation Policy: Add-only via version bump | Seal Date: 2026-02-27*
*Governed by: ECOSKILLER INTELLIGENCE & INNOVATION CORE*
