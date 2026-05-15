# 🔒 DOJO_SESSION_ORCHESTRATION_AGENT.md
## ECOSKILLER ANTIGRAVITY — GOVERNANCE & CORE CONTROL LAYER
### Status: `SEALED` · `LOCKED` · `GOVERNED` · `DETERMINISTIC`
### Mutation Policy: `ADD-ONLY via version bump`
### Interpretation Authority: `NONE`
### Execution Authority: `Human declaration only`
### Document Version: `v1.0.0`
### Classification: `CORE GOVERNANCE — TIER 0 — REAL-TIME CRITICAL`

---

> ⚠️ **SEAL NOTICE**: This agent specification is sealed and locked. No field may be modified, interpreted, inferred, extended, or overridden without a formally declared versioned amendment. All prior versions remain permanently immutable. Any execution deviating from this specification MUST HALT and escalate immediately to COMPLIANCE_AGENT and SECURITY_AGENT. Architecture interpretation is FORBIDDEN.

---

## 1️⃣ AGENT IDENTITY (MANDATORY — NON-NEGOTIABLE)

```yaml
AGENT_NAME:            DOJO_SESSION_ORCHESTRATION_AGENT
AGENT_ID:              ECSK-AGT-GOVERN-0043
SYSTEM_ROLE:           Real-Time Dojo Session Lifecycle Orchestrator & Governance Enforcer
PRIMARY_DOMAIN:        Dojo Group Discussion Arena — All Domain Tracks
LAYER:                 Governance & Core Control
PLATFORM:              Ecoskiller Antigravity
EXECUTION_MODE:        Deterministic + Event-Driven + Real-Time Stateful
DATA_SCOPE:            >
  Dojo session lifecycle state (create → join → active → conclude → archive),
  participant role bindings, match event streams, scoring orchestration signals,
  mentor command streams, video room token lifecycle, belt eligibility gates,
  tournament state, replay pipeline triggers, audit trail.
  Write authority: SESSION_STATE_STORE, AUDIT_STORE, MATCH_STORE.
  Read authority: RBAC_REGISTRY, SCENARIO_REGISTRY, BELT_REGISTRY,
  MENTOR_CERTIFICATION_REGISTRY, BILLING_GATE, TENANT_CONFIG.
TENANT_SCOPE:          Strict Tenant Isolation — sessions never cross tenant boundaries
FAILURE_POLICY:        >
  HALT_ON_AMBIGUITY |
  SAFE_SESSION_TERMINATE_ON_CORRUPTION |
  ROLLBACK_ON_PARTIAL_STATE |
  LOG_ALL_DEVIATIONS |
  NO_SILENT_FAILURES
SECURITY_MODEL:        Zero-Trust Multi-Tenant + Short-Lived Token Model
AUDIT_POLICY:          Append-Only Immutable Audit Trail — every lifecycle event logged
VERSION:               v1.0.0
BACKWARD_COMPATIBLE:   TRUE
```

> **RULE**: This agent owns the authoritative session state machine for every Dojo match. No other agent, service, or human may mutate session state outside of this agent's declared state transitions. Any direct mutation attempt = SECURITY_VIOLATION.

---

## 2️⃣ PURPOSE DECLARATION

### Problem Solved

The Ecoskiller Dojo Arena is the platform's live skill evaluation engine — a real-time Group Discussion and scenario-based assessment environment operating across five domain tracks (Arts, Commerce, Science, Technology, Administration). At 10M–100M users, the Dojo must simultaneously orchestrate thousands of concurrent live sessions involving participants, mentors, evaluators, and observers — each with distinct roles, permissions, scoring rights, and time-bound responsibilities.

Without a dedicated, governance-grade orchestration agent, the following failures become inevitable:

- **Uncontrolled session state drift** — sessions left open, orphaned, or in partial states after crashes or disconnects
- **Role injection** — unauthorized participants claiming evaluator or mentor roles mid-session
- **Video token leakage** — expired or shared tokens granting access to wrong rooms
- **Scoring race conditions** — multiple scoring events submitted out of order or concurrently, corrupting match results
- **Belt gate bypass** — belt eligibility evaluated against corrupt or incomplete session data
- **Mentor command chaos** — unlogged, unordered, or replayed mentor commands altering session outcomes
- **Replay pipeline failures** — sessions concluding without triggering the recording finalize → analytics → belt eligibility chain
- **Tournament state corruption** — bracket and pairing state corrupted by partial session completions
- **Behavioral safety failures** — no enforcement point for harassment exits, cooldowns, or emergency interventions
- **Billing gate leakage** — sessions starting without confirmed billing authorization

The `DOJO_SESSION_ORCHESTRATION_AGENT` solves all of these by being the single authoritative state machine governing the full Dojo session lifecycle — from pre-session validation through live orchestration to post-session pipeline closure and audit archival.

### Input Consumed
- Session creation requests (validated user, scenario, role assignments)
- Participant join/leave events from WebSocket gateway
- Mentor command events (mute, unmute, pause, intervention, override)
- Scoring submission events from participants and evaluators
- Video room lifecycle events from LiveKit SFU provider
- Timer expiry events from Redis TTL
- Billing gate authorization signals
- RBAC role binding confirmations from IDENTITY_AGENT
- Anomaly alerts from ANOMALY_BEHAVIOR_DETECTION_AGENT
- Scenario + rubric version references from SCENARIO_REGISTRY
- Belt eligibility check results from BELT_ENGINE
- Tournament pairing signals from TOURNAMENT_ENGINE

### Output Produced
- Authoritative session state records in SESSION_STATE_STORE
- Video room creation commands to LiveKit (signed token issuance)
- WebSocket command events to participant clients (timer, mute, phase change)
- Scoring finalization commands to DOJO_SCORING_AGENT
- Match result records sealed and emitted to MATCH_STORE
- Belt eligibility triggers to BELT_ENGINE
- Replay pipeline triggers to REPLAY_ENGINE
- Analytics pipeline triggers to ANALYTICS_ENGINE
- XP and Rank update events to GROWTH_ENGINE
- Tournament result events to TOURNAMENT_ENGINE
- Audit records for every state transition
- Emergency session termination commands when behavioral safety breached
- Human review queue items for disputed or anomalous sessions

### Downstream Agents Depending on This Agent
```
DOJO_SCORING_AGENT             # Receives score finalization commands
BELT_ENGINE                    # Receives session completion + eligibility data
REPLAY_ENGINE                  # Receives recording finalize trigger
ANALYTICS_ENGINE               # Receives match analytics pipeline trigger
GROWTH_ENGINE                  # Receives XP/Rank update events
TOURNAMENT_ENGINE              # Receives match results, bracket update triggers
NOTIFICATION_AGENT             # Receives session lifecycle notifications to users
ANOMALY_BEHAVIOR_DETECTION_AGENT  # Receives session event stream for integrity monitoring
OBSERVABILITY_AGENT            # Receives session health and lifecycle metrics
COMPLIANCE_AGENT               # Receives all audit records + dispute triggers
FEATURE_STORE_AGENT            # Receives behavioral features from session
REPUTATION_SCORE_AGENT         # Receives session participation quality signals
MENTOR_CALIBRATION_AGENT       # Receives mentor scoring data for drift detection
```

### Upstream Agents and Services Feeding This Agent
```
IDENTITY_AGENT                 # RBAC role validation and JWT verification
BILLING_GATE_AGENT             # Session billing authorization
SCENARIO_REGISTRY              # Scenario + rubric version resolution
MENTOR_CERTIFICATION_REGISTRY  # Mentor eligibility verification
BELT_REGISTRY                  # Belt eligibility rules and thresholds
ANOMALY_BEHAVIOR_DETECTION_AGENT  # Real-time integrity signals
API_GATEWAY_AGENT              # Session creation request entry point
WEBSOCKET_GATEWAY              # Participant event stream
LIVEKIT_SFU                    # Video room lifecycle events
REDIS_TTL_SERVICE              # Timer expiry events
TOURNAMENT_ENGINE              # Tournament pairing assignments
```

---

## 3️⃣ SESSION STATE MACHINE (AUTHORITATIVE)

```
STATES (LOCKED — NO CUSTOM STATES PERMITTED):

  PRE_SESSION
    └─ Substates: REQUESTED → VALIDATED → BILLING_AUTHORIZED →
                  ROLES_BOUND → SCENARIO_LOADED → ROOM_CREATED → READY

  ACTIVE_SESSION
    └─ Substates: WAITING_FOR_PARTICIPANTS → IN_PROGRESS →
                  PHASE_TRANSITION → MENTOR_INTERVENTION → CONCLUDING

  POST_SESSION
    └─ Substates: SCORING_FINALIZED → RECORDING_ARCHIVING →
                  ANALYTICS_PROCESSING → BELT_GATE_CHECK →
                  RESULTS_SEALED → ARCHIVED

  TERMINAL_STATES:
    └─ COMPLETED      — normal session completion
    └─ ABANDONED      — participant quorum lost, session void
    └─ EMERGENCY_TERMINATED — behavioral safety override
    └─ CORRUPTED      — data integrity failure, quarantined for review
    └─ DISPUTED       — result under compliance review

STATE_TRANSITION_RULES:
  - Transitions are strictly ordered — no skipping states
  - Backward transitions FORBIDDEN except under explicit ROLLBACK_POLICY
  - Every transition emits a STATE_TRANSITION_EVENT to audit trail
  - Invalid transition attempt = HALT + LOG_INVALID_TRANSITION + ESCALATE

QUORUM_RULES:
  minimum_participants:          2 (any session type)
  ranked_session_minimum:        3 (minimum for ranked evaluation)
  mentor_required_sessions:      Certification matches, Belt promotion matches
  evaluator_required_sessions:   Scored Dojo matches (minimum 1 evaluator per session)
  quorum_wait_window:            300 seconds (5 minutes) then AUTO_ABANDON
  mid_session_quorum_loss:       >50% participants disconnect → PAUSE → 120s recovery → ABANDON
```

---

## 4️⃣ INPUT CONTRACT (STRICT)

### SESSION_CREATION_REQUEST_SCHEMA

```json
{
  "required_fields": [
    "request_id",
    "tenant_id",
    "creator_user_id",
    "session_type",
    "domain_tag",
    "scenario_id",
    "scenario_version",
    "participant_user_ids",
    "role_assignments",
    "billing_authorization_token",
    "scheduled_start_utc",
    "timestamp_utc",
    "request_signature"
  ],
  "optional_fields": [
    "tournament_id",
    "tournament_round",
    "mentor_user_id",
    "observer_user_ids",
    "custom_timer_override",
    "recording_consent_tokens",
    "accessibility_flags",
    "language_preference",
    "parent_session_id"
  ],
  "validation_rules": [
    "request_id MUST be UUID v4 — globally unique",
    "tenant_id MUST match authenticated JWT claim",
    "creator_user_id MUST belong to tenant_id",
    "session_type MUST be one of [PRACTICE, RANKED, CERTIFICATION, TOURNAMENT, MENTOR_LED, INTELLIGENCE_ASSESSMENT]",
    "domain_tag MUST be one of [Arts, Commerce, Science, Technology, Administration]",
    "scenario_id MUST exist in SCENARIO_REGISTRY for this tenant and domain",
    "scenario_version MUST be the latest published version unless explicit override approved",
    "participant_user_ids MUST be an array of 2–10 verified user UUIDs",
    "role_assignments MUST cover all participant_user_ids with no gaps",
    "billing_authorization_token MUST be valid and unexpired",
    "scheduled_start_utc MUST be ISO 8601 UTC",
    "request_signature MUST be HMAC-SHA256 of canonical request payload"
  ],
  "security_checks": [
    "JWT tenant_id isolation verified on every request",
    "All participant_user_ids must belong to tenant_id — cross-tenant = REJECT + SECURITY_ALERT",
    "billing_authorization_token validated against BILLING_GATE_AGENT before session creation",
    "RBAC check: creator must have SESSION_CREATE permission in their role",
    "Mentor user_id (if present) must have valid MENTOR_CERTIFIED status in MENTOR_CERTIFICATION_REGISTRY",
    "Duplicate request_id within 3600s = idempotent response — no duplicate session created",
    "recording_consent_tokens required for all participants if session_type = CERTIFICATION or RANKED"
  ],
  "domain_checks": [
    "scenario_id domain must match domain_tag — cross-domain scenario injection = REJECT",
    "role_assignments must respect domain-specific role rules from SCENARIO_REGISTRY"
  ]
}
```

### PARTICIPANT_JOIN_EVENT_SCHEMA

```json
{
  "required_fields": [
    "session_id",
    "participant_user_id",
    "role",
    "join_token",
    "device_fingerprint_hash",
    "timestamp_utc"
  ],
  "validation_rules": [
    "session_id MUST exist in SESSION_STATE_STORE and be in READY or WAITING_FOR_PARTICIPANTS state",
    "participant_user_id MUST be in the pre-authorized participant_user_ids list",
    "role MUST match the pre-assigned role in role_assignments — role injection = REJECT + SECURITY_ALERT",
    "join_token MUST be the short-lived signed token issued by this agent for this participant",
    "join_token expiry window: 300 seconds from issuance",
    "device_fingerprint_hash MUST be SHA-256"
  ]
}
```

### NULL TOLERANCE POLICY

```yaml
null_tolerance:                ZERO on all required fields
missing_required_field:        REJECT → LOG → ESCALATE to COMPLIANCE_AGENT
null_optional_field:           ACCEPT → FLAG in audit record
invalid_role_assignment:       REJECT_SESSION_CREATION + SECURITY_ALERT
expired_billing_token:         REJECT_SESSION_CREATION + notify BILLING_GATE_AGENT
```

---

## 5️⃣ OUTPUT CONTRACT (STRICT)

### SESSION_LIFECYCLE_RECORD_SCHEMA

```json
{
  "result_object": {
    "session_id": "UUID v4 — globally unique, assigned at creation",
    "session_state": "current state in state machine",
    "session_type": "PRACTICE | RANKED | CERTIFICATION | TOURNAMENT | MENTOR_LED | INTELLIGENCE_ASSESSMENT",
    "domain_tag": "Arts | Commerce | Science | Technology | Administration",
    "tenant_id": "tenant reference",
    "scenario_ref": {
      "scenario_id": "UUID",
      "scenario_version": "semantic version string",
      "rubric_version": "semantic version string"
    },
    "participant_roster": [
      {
        "user_id": "pseudonymised user reference",
        "role": "PARTICIPANT | EVALUATOR | MENTOR | OBSERVER",
        "join_time_utc": "ISO 8601",
        "leave_time_utc": "ISO 8601 or null",
        "connection_quality_score": "float [0.0 – 1.0]"
      }
    ],
    "session_timeline": {
      "requested_at_utc": "ISO 8601",
      "started_at_utc": "ISO 8601",
      "concluded_at_utc": "ISO 8601 or null",
      "total_duration_seconds": "integer"
    },
    "scoring_summary": {
      "finalized": "boolean",
      "scoring_agent_ref": "DOJO_SCORING_AGENT execution_trace_id",
      "score_confidence_score": "float — from INTELLIGENCE_CONFIDENCE_INTERVAL_AGENT",
      "mentor_override_applied": "boolean",
      "override_audit_ref": "UUID or null"
    },
    "belt_gate_result": {
      "evaluated": "boolean",
      "outcome": "ELIGIBLE | NOT_ELIGIBLE | DEFERRED_REVIEW | N_A",
      "belt_engine_ref": "BELT_ENGINE execution_trace_id or null"
    },
    "downstream_pipeline_status": {
      "replay_triggered": "boolean",
      "analytics_triggered": "boolean",
      "xp_update_triggered": "boolean",
      "rank_update_triggered": "boolean",
      "tournament_result_emitted": "boolean"
    },
    "integrity_status": "CLEAN | ANOMALY_FLAGGED | UNDER_REVIEW | DISPUTED",
    "terminal_state": "COMPLETED | ABANDONED | EMERGENCY_TERMINATED | CORRUPTED | DISPUTED"
  },
  "confidence_score": "float [0.0 – 1.0] — session data integrity confidence",
  "model_version": "DSOA_v1.0.0",
  "audit_reference": "UUID v4 — globally unique per session lifecycle",
  "next_trigger_event": [
    "SESSION_CREATED_EVENT",
    "SESSION_STARTED_EVENT",
    "SESSION_CONCLUDED_EVENT",
    "SESSION_ABANDONED_EVENT",
    "SESSION_EMERGENCY_TERMINATED_EVENT",
    "SCORING_FINALIZE_COMMAND",
    "REPLAY_PIPELINE_TRIGGER_EVENT",
    "ANALYTICS_PIPELINE_TRIGGER_EVENT",
    "BELT_GATE_TRIGGER_EVENT",
    "XP_UPDATE_EVENT",
    "RANK_UPDATE_EVENT",
    "TOURNAMENT_RESULT_EVENT",
    "DISPUTE_QUEUE_EVENT",
    "SESSION_AUDIT_WRITTEN_EVENT"
  ]
}
```

---

## 6️⃣ REAL-TIME PROTOCOL LAYER (LOCKED — ARCHITECTURE FROZEN)

```yaml
TRANSPORT_SEPARATION_RULE:
  # Per Dojo Spec Section E — NO MIXING OF CHANNELS
  VIDEO_TRANSPORT:          WebRTC SFU model via LiveKit
  GAME_EVENT_TRANSPORT:     WebSocket (bidirectional, session-scoped)
  MENTOR_COMMAND_TRANSPORT: Dedicated Mentor Socket channel (separate from participant socket)
  ANALYTICS_TRANSPORT:      Apache Kafka Event Bus (async, non-blocking)

  RULE: These four channels must NEVER merge.
        Any request to merge channels = ARCHITECTURE_VIOLATION + HALT.

VIDEO_ROOM_MANAGEMENT:
  provider:                 LiveKit SFU (provider swappable, architecture is not)
  room_naming:              room_id = session_id (no other naming scheme permitted)
  room_metadata:            role bindings embedded as signed metadata in room token
  token_model:              Short-lived signed JWT tokens — issued per participant per session
  token_lifetime:           900 seconds (15 minutes) — renewable on active connection
  token_issuance_authority: DOJO_SESSION_ORCHESTRATION_AGENT only
  token_revocation:         Immediate on participant leave, role violation, or session termination
  room_creation:            This agent creates rooms — no other service may create Dojo rooms
  room_destruction:         Triggered only by this agent on session terminal state
  coturn_integration:       TRUE — NAT traversal for reliable WebRTC connectivity
  srtp_encryption:          MANDATORY — all media encrypted in transit
  recording_activation:     Only if recording_consent_tokens present for all participants
  signed_recording_urls:    Time-limited, role-based — issued by this agent after session

WEBSOCKET_COMMAND_PROTOCOL:
  session_scoped:           TRUE — socket namespace = session_id
  command_types:
    - TIMER_START
    - TIMER_PAUSE
    - TIMER_EXTEND         # Mentor authority only
    - PHASE_CHANGE         # This agent only
    - MUTE_PARTICIPANT     # Mentor authority + logged
    - UNMUTE_PARTICIPANT   # Mentor authority + logged
    - PARTICIPANT_KICK     # CRITICAL authority — triggers ANOMALY_BEHAVIOR_DETECTION_AGENT
    - SESSION_PAUSE        # Mentor + behavioral safety override
    - SESSION_RESUME
    - EMERGENCY_STOP       # Behavioral safety — immediate, no debate
    - SCORE_WINDOW_OPEN    # This agent only — opens scoring period
    - SCORE_WINDOW_CLOSE   # This agent only — closes scoring period
  command_ordering:         Strictly monotonic sequence numbers — out-of-order commands rejected
  command_idempotency:      TRUE — duplicate sequence numbers deduplicated
  command_audit:            Every mentor command logged to AUDIT_STORE immediately

REDIS_STATE_MODEL:
  session_hot_state:        Redis Hash per session_id — TTL = session_max_duration + 3600s buffer
  participant_presence:     Redis Set per session_id — presence heartbeat every 10 seconds
  timer_state:              Redis TTL key per phase timer
  score_submission_lock:    Redis distributed lock — prevents concurrent score submission race
  deterministic_state_machine: Redis Lua scripts for atomic state transitions (as per tech stack)
```

---

## 7️⃣ SESSION LIFECYCLE — DETAILED PHASE EXECUTION

### PHASE 1 — PRE-SESSION VALIDATION PIPELINE

```yaml
STEP_1_REQUEST_VALIDATION:
  action: Validate all fields per INPUT_CONTRACT Section 4
  failure: REJECT + LOG + escalate to COMPLIANCE_AGENT
  output: Validated session_request object

STEP_2_BILLING_GATE_CHECK:
  action: Verify billing_authorization_token with BILLING_GATE_AGENT
  failure: REJECT_SESSION + notify requester via NOTIFICATION_AGENT
  note: No feature access without billing check — non-negotiable per Dojo Spec P2
  output: BILLING_CONFIRMED signal

STEP_3_RBAC_ROLE_BINDING:
  action: Verify all role_assignments against RBAC_REGISTRY via IDENTITY_AGENT
  checks:
    - Every participant has SESSION_JOIN permission
    - Mentor (if present) has MENTOR_CERTIFIED status in MENTOR_CERTIFICATION_REGISTRY
    - Evaluator (if present) has EVALUATOR role in this domain
    - No participant holds a role they are not authorized for
  failure: REJECT_SESSION + SECURITY_ALERT
  output: ROLES_BOUND confirmation

STEP_4_SCENARIO_RESOLUTION:
  action: Resolve scenario_id + scenario_version from SCENARIO_REGISTRY
  checks:
    - Scenario exists and is published (not draft, not retired)
    - Scenario version is current or explicitly approved legacy version
    - Rubric version resolved and attached
    - Scenario domain matches domain_tag
    - Scenario difficulty label is data-derived (not author-declared — per T4)
  failure: REJECT_SESSION + LOG_SCENARIO_RESOLUTION_FAILURE
  output: SCENARIO_LOADED with rubric reference

STEP_5_MENTOR_CERTIFICATION_CHECK:
  action: If session_type = CERTIFICATION or RANKED, verify mentor certification
  rule: Uncertified mentors CANNOT run RANKED or CERTIFICATION sessions — per T7
  failure: REJECT_SESSION + notify MENTOR_CALIBRATION_AGENT
  output: MENTOR_ELIGIBLE or session_type downgraded to PRACTICE (with requester notification)

STEP_6_RECORDING_CONSENT_CHECK:
  action: If session_type requires recording, verify consent tokens for all participants
  rule: Recording activation requires consent capture — per Dojo Spec P9 + Section H
  failure: DISABLE_RECORDING flag set; session proceeds without recording
  output: RECORDING_CONSENT_STATUS (ENABLED | DISABLED)

STEP_7_ROOM_CREATION:
  action: Create LiveKit SFU room with room_id = session_id
  action: Issue short-lived signed video tokens per participant (role-embedded metadata)
  action: Create WebSocket session namespace
  action: Initialize Redis hot state for session
  failure: HALT + ROLLBACK_ALL_PRIOR_STEPS + LOG_ROOM_CREATION_FAILURE
  output: SESSION_ROOM_READY + join tokens distributed to participants

STEP_8_SESSION_READY:
  action: Emit SESSION_CREATED_EVENT to event bus
  action: Write initial audit record to AUDIT_STORE
  action: Notify all participants via NOTIFICATION_AGENT
  state_transition: PRE_SESSION → READY
```

### PHASE 2 — ACTIVE SESSION ORCHESTRATION

```yaml
JOIN_WINDOW:
  duration: 300 seconds from SESSION_CREATED_EVENT
  action: Accept participant joins, validate join tokens per JOIN_EVENT_SCHEMA
  quorum_check: Every join event recalculates quorum status
  quorum_met: STATE → IN_PROGRESS
  quorum_timeout: STATE → ABANDONED + cleanup + notify + audit

PHASE_MANAGEMENT:
  phases_defined_by: SCENARIO_REGISTRY (per scenario rubric)
  phase_timer_engine: Redis TTL keys — deterministic expiry
  phase_transitions:
    - Automatic on timer expiry (standard flow)
    - Manual by MENTOR command PHASE_CHANGE (logged + audited)
  score_window: Opened by this agent at defined phase checkpoints
                Closed by this agent — scoring outside window = REJECTED

MENTOR_COMMAND_EXECUTION:
  authority: Only certified mentors (verified in STEP_5) may issue commands
  command_chain:
    receive → verify_mentor_role → sequence_number_check →
    execute → log_to_AUDIT_STORE → emit_to_WebSocket_clients
  override_commands (MUTE, KICK, PAUSE, EMERGENCY_STOP):
    require: Mentor authentication re-verification (short-lived nonce)
    logged:  Immediately and immutably per Dojo Spec Section H + P1
  mentor_command_audit: All commands stored in AUDIT_STORE — immutable
  mentor_score_override: Permitted with mandatory audit reference — per Scoring Governance Lock

BEHAVIORAL_SAFETY_ENFORCEMENT:
  # Per Dojo Spec T10 — Safety overrides scoring
  emergency_stop_authority:
    - Mentor (via EMERGENCY_STOP command)
    - ANOMALY_BEHAVIOR_DETECTION_AGENT (via QUARANTINE signal)
    - This agent (on detecting irrecoverable state corruption)
  emergency_stop_effect:
    - Immediate session state → EMERGENCY_TERMINATED
    - All video tokens revoked immediately
    - WebSocket connections closed
    - Session result VOID (does not count toward belts)
    - Evidence package emitted to COMPLIANCE_AGENT
  harassment_reporting: Participant HARASSMENT_REPORT event routed to MODERATION_AGENT
  forced_exit_option: Any participant may exit; if quorum breached → session PAUSE then possible ABANDON
  cooldown_enforcement: Post-emergency-stop, affected participants enter cooldown managed by IDENTITY_AGENT

ANOMALY_MONITORING_INTEGRATION:
  stream: All session WebSocket events forwarded to ANOMALY_BEHAVIOR_DETECTION_AGENT in real-time
  response_to_anomaly_alert:
    CRITICAL: Execute EMERGENCY_STOP immediately
    HIGH:     PAUSE session + notify mentor + await mentor decision (max 120s then EMERGENCY_STOP)
    MEDIUM:   FLAG session + continue + human review triggered post-session
    LOW:      LOG + continue
  scoring_anomaly:
    source: ANOMALY_BEHAVIOR_DETECTION_AGENT DOJO_INTEGRITY signals
    action: Flag session + hold belt gate check pending COMPLIANCE_AGENT review

DISCONNECT_AND_RECONNECT_HANDLING:
  single_participant_disconnect:
    wait_window: 120 seconds
    reconnect: Re-issue join token; participant rejoins in current phase
    timeout: Participant marked as DROPPED; quorum recalculated
  majority_disconnect:
    action: PAUSE session immediately; 120s recovery window
    recovery_fails: STATE → ABANDONED
  mentor_disconnect:
    action: PAUSE session immediately
    mentor_led_sessions: No substitute mentor — session ABANDONED after 300s
    practice_sessions: Continue without mentor commands
```

### PHASE 3 — POST-SESSION PIPELINE (MANDATORY — NO MANUAL SYNC)

```yaml
# Per Dojo Spec Section P6 — all wiring is mandatory, event-driven only

STEP_1_SCORE_FINALIZATION:
  action: Close all score windows; emit SCORING_FINALIZE_COMMAND to DOJO_SCORING_AGENT
  wait: Await SCORING_COMPLETE_EVENT from DOJO_SCORING_AGENT (timeout: 120s)
  timeout_action: Log + escalate + session marked DISPUTED
  scoring_governance:
    formula: Weighted metric model — Peer + Mentor + Self merge (per Scoring Governance Lock)
    variance_check: DOJO_SCORING_AGENT checks inter-rater reliability (per T2)
    low_confidence: Scores below confidence threshold → DEFERRED_REVIEW; no belt promotion
    mentor_override: Permitted with mandatory audit reference; triggers rating recompute
  output: SCORING_FINALIZED signal + score record sealed in MATCH_STORE

STEP_2_RECORDING_FINALIZATION:
  action: Emit RECORDING_FINALIZE_TRIGGER to REPLAY_ENGINE
  condition: Only if RECORDING_CONSENT_STATUS = ENABLED
  output: Recording archived + signed time-limited replay URLs generated (role-based access)
  replay_access_rule: Role-based — Mentors, Evaluators, and session participants only (per P1 Media Security)

STEP_3_ANALYTICS_PIPELINE:
  action: Emit ANALYTICS_PIPELINE_TRIGGER to ANALYTICS_ENGINE
  analytics_payload:
    - session_id, domain_tag, scenario_id, scenario_version
    - participant count, duration, phase timings
    - score distribution, variance metrics
    - disconnect events, reconnect events
    - mentor command frequency
  output: Analytics record written; product analytics metrics updated (per T19)

STEP_4_BELT_GATE_CHECK:
  action: Emit BELT_GATE_TRIGGER to BELT_ENGINE with sealed match result
  prerequisites:
    - scoring_finalized = TRUE
    - integrity_status = CLEAN (ANOMALY_FLAGGED or DISPUTED sessions = DEFERRED)
    - session_type in [RANKED, CERTIFICATION, TOURNAMENT] (PRACTICE excluded)
    - mentor_certified = TRUE for CERTIFICATION and RANKED sessions (per T7)
  belt_promotion_rules (ALL must pass — per Dojo Spec Section G + T2):
    - match_count threshold met
    - score_threshold met
    - pressure_scenario_pass = TRUE
    - mentor_certification confirmed
    - confidence_score >= 0.80 (from INTELLIGENCE_CONFIDENCE_INTERVAL_AGENT)
    - integrity_status = CLEAN
    - no active MEDIUM+ anomaly from ANOMALY_BEHAVIOR_DETECTION_AGENT
    - low_confidence_flag = FALSE
  AUTO_PROMOTION: FORBIDDEN — mentor confirmation required always
  output: BELT_GATE_RESULT (ELIGIBLE | NOT_ELIGIBLE | DEFERRED_REVIEW)

STEP_5_GROWTH_ENGINE_UPDATE:
  condition: session_type != PRACTICE AND integrity_status = CLEAN
  emit:
    XP_UPDATE_EVENT:    XP delta computed by GROWTH_ENGINE based on match result + confidence
    RANK_UPDATE_EVENT:  Rank recalculated by RANK_UPDATE_AGENT
    SHARE_TRIGGER_EVENT: Only if result is outstanding (confidence >= 0.90 + integrity CLEAN)
  anomaly_guard: No XP/Rank events if session has unresolved HIGH+ anomaly flag

STEP_6_TOURNAMENT_RESULT:
  condition: tournament_id present in session record
  action: Emit TOURNAMENT_RESULT_EVENT to TOURNAMENT_ENGINE
  payload: match_result, participant scores, belt_gate_result, session_id
  output: Tournament bracket updated by TOURNAMENT_ENGINE

STEP_7_FEATURE_STORE_EMISSION:
  action: Emit behavioral feature vector to FEATURE_STORE_AGENT
  features:
    - session_completion_rate (per user, rolling 30d)
    - average_score_per_domain
    - mentor_command_received_frequency
    - disconnect_frequency
    - scoring_variance_contribution
    - session_type_distribution

STEP_8_RESULT_SEAL_AND_ARCHIVE:
  action: Write final SESSION_LIFECYCLE_RECORD to MATCH_STORE (immutable)
  action: Write terminal audit record to AUDIT_STORE
  action: Emit SESSION_AUDIT_WRITTEN_EVENT
  action: Release Redis hot state (after 3600s retention window for late event processing)
  state_transition: → COMPLETED (or appropriate terminal state)
  output: SESSION_CONCLUDED_EVENT emitted to event bus
```

---

## 8️⃣ SCORING GOVERNANCE ENFORCEMENT

```yaml
# Per Dojo Spec Section F — Scoring formula immutable class
SCORING_FORMULA_CLASS:          IMMUTABLE — changes require MAJOR version bump
FORMULA_COMPONENTS:
  weighted_metric_model:        TRUE
  score_merge:                  Peer + Mentor + Self — weights defined in SCENARIO_REGISTRY rubric
  audit_log_on_override:        MANDATORY — every override creates immutable audit record
  variance_anomaly_detection:   Enforced via DOJO_SCORING_AGENT + ANOMALY_BEHAVIOR_DETECTION_AGENT

SCORE_SUBMISSION_RULES:
  window:                       Open only during designated scoring phases (controlled by this agent)
  outside_window_submission:    REJECTED — logged as anomaly signal
  duplicate_submission:         Idempotent — first submission wins; duplicates discarded + logged
  race_condition_prevention:    Redis distributed lock per participant per scoring window
  self_scoring_weight_cap:      Cannot exceed 30% of total score (defined in rubric)
  peer_scoring_minimum:         At least 2 peer scores required for score to be valid
  evaluator_override_authority: Evaluator may adjust scores within rubric-defined variance bounds
  mentor_override_authority:    Mentor may override any score — requires audit nonce + reason

INTER_RATER_RELIABILITY:
  tracking:                     Per-session, per-evaluator (per T2)
  drift_detection:              Mentor/evaluator scoring patterns forwarded to MENTOR_CALIBRATION_AGENT
  calibration_tolerance:        Mentors outside tolerance = certification authority suspended (per T3)
  low_reliability_sessions:     Flagged; belt gate check deferred to COMPLIANCE_AGENT review

CONFIDENCE_SCORE_GATE:
  source:                       INTELLIGENCE_CONFIDENCE_INTERVAL_AGENT
  minimum_for_belt_promotion:   0.80
  below_threshold:              LOW_CONFIDENCE_FLAG set; mentor board review required (per T2)
```

---

## 9️⃣ ML / AI LOGIC LAYER

### Session Health Scoring (ML-Based — Primary)

```yaml
MODEL_TYPE:           Multi-variate Regression — Session Integrity Confidence Scorer
PURPOSE: >
  Computes a session-level confidence score [0.0–1.0] representing
  the integrity and completeness of session data before sealing
  the match result and triggering the belt gate.

FEATURES_USED:
  - participant_quorum_maintained_ratio    # % of session duration with full quorum
  - score_submission_completeness          # % of expected scores actually submitted
  - scoring_window_compliance_rate         # % of scores submitted in valid window
  - inter_rater_variance_score             # Lower = more reliable scoring
  - disconnect_event_count                 # Excessive disconnects signal instability
  - mentor_override_count                  # High overrides signal anomalous session
  - phase_timer_deviation                  # Actual vs expected phase duration
  - anomaly_flag_count_from_abda           # Flags from ANOMALY_BEHAVIOR_DETECTION_AGENT
  - recording_integrity_flag               # Recording completed without corruption

CONFIDENCE_THRESHOLDS:
  CLEAN_SESSION:          >= 0.80
  REVIEW_REQUIRED:        0.60 – 0.79
  DISPUTED:               0.40 – 0.59
  VOID_SESSION:           < 0.40 — session result cannot be used for belt or ranking

TRAINING_FREQUENCY:     Weekly (Sunday 03:00 UTC)
DRIFT_DETECTION:        PSI per feature per domain, checked every 5000 sessions
VERSION_CONTROL:        MODEL_REGISTRY — immutable reference, semantic versioning
```

### AI-Assisted Layer (LLM-Based — Scoped)

```yaml
AI_USAGE_SCOPE: >
  Strictly limited to:
  1. Generating human-readable session summaries for dispute review panels
  2. Generating mentor calibration anomaly explanations
  3. Scenario rubric fairness review assistance (weekly batch, not real-time)

  AI MUST NOT:
  - Override session confidence scores
  - Modify state machine transitions
  - Issue video tokens or WebSocket commands
  - Access unmasked participant data
  - Award or block belt promotions

PROMPT_GOVERNANCE:
  versioned: TRUE
  deterministic_structure: ENFORCED
  creative_interpretation: FORBIDDEN
  prompt_registry: PROMPT_REGISTRY_AGENT
  prompt_immutability: ADD-ONLY
```

---

## 🔟 SCALABILITY DESIGN

```yaml
EXPECTED_CONCURRENT_SESSIONS:   10,000 – 100,000 simultaneous live sessions
EXPECTED_RPS:                   200,000 – 2,000,000 WebSocket events/sec at peak
LATENCY_TARGETS:
  state_transition:             p99 < 50ms (Redis Lua atomic transitions)
  websocket_command_delivery:   p99 < 30ms (client-perceived)
  video_token_issuance:         p99 < 100ms
  post_session_pipeline_start:  p99 < 5s from session_concluded signal
MAX_CONCURRENCY:                100,000 simultaneous session state machines
QUEUE_STRATEGY:                 >
  CERTIFICATION/TOURNAMENT sessions: Priority queue — dedicated worker pool
  RANKED sessions:                   Standard priority queue
  PRACTICE sessions:                 Bulk queue — burst-tolerant

HORIZONTAL_SCALING:             TRUE — stateless orchestration pods, Kubernetes HPA
STATELESS:                      TRUE — all session state in Redis (hot) + PostgreSQL (cold)
STATEFUL_STREAM_PROCESSING:     Apache Flink for event stream processing
IDEMPOTENT:                     TRUE — duplicate events deduplicated via Redis distributed locks
SESSION_SHARD_STRATEGY:         Shard by tenant_id + session_id hash
```

---

## 1️⃣1️⃣ SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  validation:               JWT claim match on every API call and event
  session_cross_tenant:     FORBIDDEN — hard block at session router
  violation_action:         HALT + SECURITY_INCIDENT + ALERT_SECURITY_AGENT

ROLE_INJECTION_PREVENTION:
  rule: Role assignments are sealed at session creation — IMMUTABLE
  mid_session_role_change: FORBIDDEN for all except this agent's own state transitions
  attempt_detected: REJECT + SECURITY_ALERT + ANOMALY_BEHAVIOR_DETECTION_AGENT notified

VIDEO_TOKEN_SECURITY:
  issuance_authority:       This agent only
  token_lifetime:           900 seconds (renewable on active heartbeat)
  token_revocation:         Immediate on leave, kick, or emergency stop
  sharing_detection:        Multiple simultaneous uses of same token = REVOKE + SECURITY_ALERT
  signed_recording_urls:    Time-limited (3600s) + role-scoped + access-logged on every read

MENTOR_COMMAND_SECURITY:
  authentication:           Mentor commands require short-lived nonce (re-verified per command)
  audit:                    Immutable — every command in AUDIT_STORE
  override_ethics:          Mentor override of scores requires declared reason code
  replay_prevention:        Sequence number monotonicity enforced

ENCRYPTION:
  video_transport:          SRTP (WebRTC mandatory)
  websocket_transport:      WSS (TLS 1.3)
  api_layer:                TLS 1.3
  data_at_rest:             AES-256
  PII:                      Masked before AUDIT_STORE write
  session_recordings:       Encrypted at rest; signed time-limited URLs only

ROW_LEVEL_SECURITY:
  all_session_data:         RLS enforced at PostgreSQL layer — per Dojo Spec P1
  no_cross_tenant_rows:     ENFORCED at DB query level
```

---

## 1️⃣2️⃣ AUDIT & TRACEABILITY

Every session lifecycle event writes an immutable audit record:

```json
{
  "timestamp_utc": "ISO 8601 UTC",
  "actor_id": "user_id (pseudonymised) + tenant_id composite",
  "session_id": "UUID v4",
  "execution_trace_id": "UUID v4 — per state transition",
  "event_type": "state transition | mentor command | scoring event | emergency action",
  "previous_state": "state machine previous state",
  "new_state": "state machine new state",
  "input_hash": "SHA-256 of triggering event payload",
  "output_hash": "SHA-256 of resulting state record",
  "model_version": "DSOA_v1.0.0",
  "decision_path": "trigger → validation → transition → downstream_emission",
  "confidence_score": "session integrity confidence at this point",
  "anomaly_flags": ["ROLE_INJECTION_ATTEMPT", "QUORUM_BREACH", "SCORE_OUTSIDE_WINDOW", "DISCONNECT_STORM"],
  "mentor_command_ref": "UUID or null",
  "belt_gate_ref": "UUID or null",
  "tenant_id": "masked reference",
  "domain_tag": "Arts | Commerce | Science | Technology | Administration",
  "recording_consent_status": "ENABLED | DISABLED | PENDING",
  "audit_schema_version": "AUDIT_SCHEMA_v3"
}
```

> **IMMUTABILITY RULE**: Audit records are written once. No UPDATE, DELETE, or PATCH permitted. Any attempt = SECURITY_INCIDENT. All audit records HMAC-signed at write time.

---

## 1️⃣3️⃣ FAILURE POLICY

```yaml
FAILURE_SCENARIOS:

  INVALID_SESSION_REQUEST:
    action: REJECT + LOG_VALIDATION_FAILURE
    escalate_to: COMPLIANCE_AGENT
    retry_policy: NO_RETRY without correction

  BILLING_GATE_FAILURE:
    action: REJECT_SESSION + notify requester via NOTIFICATION_AGENT
    escalate_to: BILLING_GATE_AGENT
    retry_policy: NO_RETRY — billing must be resolved externally

  ROOM_CREATION_FAILURE (LiveKit unavailable):
    action: ROLLBACK all pre-session steps + HALT
    log: LOG_ROOM_CREATION_FAILURE
    escalate_to: OBSERVABILITY_AGENT + infrastructure team
    retry_policy: RETRY_3x with 1s exponential backoff; after 3 failures → HALT + notify users

  STATE_CORRUPTION (Redis state inconsistency detected):
    action: PAUSE session + quarantine state
    log: LOG_STATE_CORRUPTION with session_id + snapshot
    escalate_to: DATA_INTEGRITY_AGENT + COMPLIANCE_AGENT
    retry_policy: NO_RETRY — manual forensic review required; session → CORRUPTED terminal state

  SCORING_AGENT_TIMEOUT (120s without SCORING_COMPLETE_EVENT):
    action: Mark session as DISPUTED + emit DISPUTE_QUEUE_EVENT
    log: LOG_SCORING_TIMEOUT
    escalate_to: COMPLIANCE_AGENT + TRUST_REVIEW_PANEL
    retry_policy: RETRY_1x after 60s; persistent timeout → DISPUTED

  QUORUM_LOSS_MID_SESSION:
    action: PAUSE immediately; 120s recovery window; then ABANDON
    log: LOG_QUORUM_LOSS
    notify: All participants via NOTIFICATION_AGENT
    belt_gate: Session does NOT count toward belt metrics

  MENTOR_DISCONNECT_IN_REQUIRED_SESSION:
    action: PAUSE; 300s mentor reconnect window
    timeout_action: SESSION_ABANDONED
    log: LOG_MENTOR_DISCONNECT

  ANOMALY_CRITICAL_DURING_SESSION:
    action: EMERGENCY_STOP immediately — no delay, no debate
    log: LOG_EMERGENCY_STOP + EVIDENCE_PACKAGE
    escalate_to: SECURITY_AGENT + COMPLIANCE_AGENT
    session_result: VOID — does not count toward belts or XP

  MODEL_UNAVAILABLE (Session integrity scorer):
    action: Continue session; use rule-based fallback confidence scoring
    flag: MODEL_DEGRADED_FLAG on session record
    escalate_to: MODEL_GOVERNANCE_AGENT + OBSERVABILITY_AGENT
    belt_gate: Deferred to COMPLIANCE_AGENT review if MODEL_DEGRADED_FLAG set

  AI_ANNOTATION_TIMEOUT:
    action: Skip AI annotation — emit ML-only session record
    note: NEVER blocks session conclusion or pipeline triggers

SILENT_FAILURE_POLICY: ABSOLUTE ZERO TOLERANCE
```

---

## 1️⃣4️⃣ INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - IDENTITY_AGENT                          # RBAC + JWT validation
  - BILLING_GATE_AGENT                      # Session billing authorization
  - SCENARIO_REGISTRY                       # Scenario + rubric resolution
  - MENTOR_CERTIFICATION_REGISTRY           # Mentor eligibility
  - BELT_REGISTRY                           # Belt rules and thresholds
  - ANOMALY_BEHAVIOR_DETECTION_AGENT        # Real-time integrity signals
  - API_GATEWAY_AGENT                       # Session creation entry point
  - WEBSOCKET_GATEWAY                       # Participant event stream
  - LIVEKIT_SFU                             # Video room lifecycle
  - REDIS_TTL_SERVICE                       # Timer expiry
  - TOURNAMENT_ENGINE                       # Tournament pairing

DOWNSTREAM_AGENTS:
  - DOJO_SCORING_AGENT                      # Score finalization
  - BELT_ENGINE                             # Belt eligibility gate
  - REPLAY_ENGINE                           # Recording finalize trigger
  - ANALYTICS_ENGINE                        # Match analytics pipeline
  - GROWTH_ENGINE                           # XP + Rank updates
  - TOURNAMENT_ENGINE                       # Result + bracket update
  - NOTIFICATION_AGENT                      # User notifications
  - ANOMALY_BEHAVIOR_DETECTION_AGENT        # Session event stream feed
  - OBSERVABILITY_AGENT                     # Health + lifecycle metrics
  - COMPLIANCE_AGENT                        # Audit + dispute records
  - FEATURE_STORE_AGENT                     # Behavioral feature emission
  - REPUTATION_SCORE_AGENT                  # Participation quality signals
  - MENTOR_CALIBRATION_AGENT                # Mentor scoring data
  - INTELLIGENCE_CONFIDENCE_INTERVAL_AGENT  # CI validation on scoring output

EVENT_TRIGGERS:
  EMITS:
    - SESSION_CREATED_EVENT
    - SESSION_STARTED_EVENT
    - SESSION_PHASE_CHANGED_EVENT
    - SESSION_CONCLUDED_EVENT
    - SESSION_ABANDONED_EVENT
    - SESSION_EMERGENCY_TERMINATED_EVENT
    - SCORING_FINALIZE_COMMAND
    - REPLAY_PIPELINE_TRIGGER_EVENT
    - ANALYTICS_PIPELINE_TRIGGER_EVENT
    - BELT_GATE_TRIGGER_EVENT
    - XP_UPDATE_EVENT
    - RANK_UPDATE_EVENT
    - TOURNAMENT_RESULT_EVENT
    - DISPUTE_QUEUE_EVENT
    - SESSION_AUDIT_WRITTEN_EVENT
    - MENTOR_COMMAND_LOGGED_EVENT
    - EMERGENCY_STOP_EVENT

  CONSUMES:
    - SESSION_CREATE_REQUEST
    - PARTICIPANT_JOIN_EVENT
    - PARTICIPANT_LEAVE_EVENT
    - MENTOR_COMMAND_EVENT
    - SCORE_SUBMISSION_EVENT
    - TIMER_EXPIRY_EVENT
    - SCORING_COMPLETE_EVENT
    - LIVEKIT_ROOM_EVENT
    - ANOMALY_ALERT_EVENT
    - BILLING_AUTHORIZED_EVENT
    - TOURNAMENT_PAIRING_EVENT
    - RECONNECT_REQUEST_EVENT
```

---

## 1️⃣5️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

```json
{
  "EMIT_FEATURE_VECTOR": {
    "user_id": "pseudonymised_user_token",
    "tenant_id": "tenant_scoped_id",
    "feature_name": "dojo_session_behavioral_profile",
    "feature_value": "composite float [0.0 – 1.0]",
    "feature_sub_values": {
      "session_completion_rate_30d": "float",
      "average_score_percentile": "float",
      "mentor_intervention_frequency": "float",
      "disconnect_frequency_30d": "float",
      "domain_tag": "string",
      "session_type_distribution": "object",
      "scoring_consistency_score": "float",
      "belt_gate_pass_rate": "float",
      "intelligence_type_signal": "object — from session exercises (Linguistic, Logical, etc.)"
    },
    "timestamp": "ISO 8601 UTC",
    "source_agent": "DOJO_SESSION_ORCHESTRATION_AGENT"
  }
}
```

> **Intelligence Layer Integration**: Session exercise performance is mapped to Howard Gardner's Multiple Intelligences profile (Linguistic, Logical-Mathematical, Spatial, Bodily-Kinesthetic, Musical, Interpersonal, Intrapersonal, Naturalistic) and emitted to FEATURE_STORE_AGENT for enrichment of the `user_intelligence_profile` table.

---

## 1️⃣6️⃣ GROWTH ENGINE HOOK

```yaml
EMIT_ON_SESSION_COMPLETED_AND_CLEAN:
  XP_UPDATE_EVENT:
    conditions: session_type != PRACTICE AND integrity_status = CLEAN
    payload:
      user_id: "pseudonymised per participant"
      xp_delta: "computed by GROWTH_ENGINE from match result + confidence_score"
      session_id: "audit reference"

  RANK_UPDATE_EVENT:
    conditions: session_type in [RANKED, CERTIFICATION, TOURNAMENT] AND integrity_status = CLEAN
    payload:
      user_id: "per participant"
      match_result: "sealed result reference"

  SHARE_TRIGGER_EVENT:
    conditions: confidence_score >= 0.90 AND integrity_status = CLEAN AND belt_gate_result = ELIGIBLE

SUPPRESS_ALL_GROWTH_EVENTS:
  conditions:
    - integrity_status IN [ANOMALY_FLAGGED, DISPUTED, CORRUPTED]
    - terminal_state IN [ABANDONED, EMERGENCY_TERMINATED, CORRUPTED, DISPUTED]
    - active unresolved HIGH+ anomaly from ANOMALY_BEHAVIOR_DETECTION_AGENT

BELT_PROMOTION_GUARD:
  auto_promotion: FORBIDDEN — always requires mentor confirmation
  anomaly_hold: Any unresolved MEDIUM+ anomaly freezes belt gate check
  dojo_spec_lock: Belt promotion rules from Dojo Spec Section G are immutable
```

---

## 1️⃣7️⃣ DOJO MULTI-TENANT RULES

```yaml
# Per Dojo Spec Section P11
TENANT_ISOLATION:
  sessions:               Per-tenant — no cross-tenant session creation or visibility
  scenario_catalogs:      Per-tenant — tenant_skill_catalog isolation enforced
  mentor_pools:           Per-tenant — tenant_mentor_pool isolation enforced
  billing:                Per-tenant — tenant_billing isolation enforced
  branding:               Per-tenant — tenant branding in session UI

TENANT_ADMIN_AUTHORITY:
  can_do:
    - Create sessions within their tenant
    - Define tenant skill catalog
    - Manage tenant mentor pool
    - View tenant-scoped analytics
    - Set tenant-scoped timer and phase configurations
  cannot_do:
    - Modify scoring formula (global immutable class)
    - Modify belt promotion rules (global immutable class)
    - Access another tenant's sessions or data
    - Override ANOMALY_BEHAVIOR_DETECTION_AGENT decisions
```

---

## 1️⃣8️⃣ PERFORMANCE MONITORING

```yaml
METRICS:
  session_creation_success_rate:   target > 99.9%
  session_start_latency:           p99 < 3s (from READY to IN_PROGRESS)
  websocket_command_latency:       p99 < 30ms
  post_session_pipeline_latency:   p99 < 5s from SESSION_CONCLUDED to all triggers emitted
  room_creation_success_rate:      target > 99.95%
  score_finalization_success_rate: target > 99.9%
  belt_gate_trigger_latency:       p99 < 10s from SCORING_FINALIZED
  emergency_stop_execution_time:   p99 < 500ms from trigger to all tokens revoked

  session_health_distribution:
    CLEAN:          target > 90% of completed sessions
    REVIEW:         < 7%
    DISPUTED:       < 2%
    VOID:           < 1%

  mentor_override_frequency:
    alert_threshold: > 20% of sessions per mentor in 7-day window
    action: Notify MENTOR_CALIBRATION_AGENT

OBSERVABILITY_INTEGRATION:
  agent: OBSERVABILITY_AGENT
  dashboards:
    - DOJO_SESSION_LIFECYCLE_DASHBOARD
    - DOJO_SCORING_INTEGRITY_DASHBOARD
    - DOJO_MENTOR_ACTIVITY_DASHBOARD
  prometheus_metrics: TRUE
  match_lifecycle_tracing: Full distributed trace per session (per P5)
  video_qos_metrics: TRUE — per session, per participant
  socket_disconnect_metrics: TRUE
  scoring_anomaly_metrics: TRUE
```

---

## 1️⃣9️⃣ SESSION_STATE_STORE SCHEMA

```sql
CREATE TABLE sessions (
  session_id              UUID PRIMARY KEY,
  tenant_id               VARCHAR(128) NOT NULL,
  domain_tag              VARCHAR(64) NOT NULL,
  session_type            VARCHAR(32) NOT NULL,
  current_state           VARCHAR(64) NOT NULL,
  terminal_state          VARCHAR(32),
  scenario_id             UUID NOT NULL,
  scenario_version        VARCHAR(32) NOT NULL,
  rubric_version          VARCHAR(32) NOT NULL,
  billing_auth_ref        VARCHAR(256) NOT NULL,
  recording_enabled       BOOLEAN NOT NULL DEFAULT FALSE,
  integrity_status        VARCHAR(32) NOT NULL DEFAULT 'CLEAN',
  session_confidence_score NUMERIC(5,4),
  mentor_user_id          UUID,
  tournament_id           UUID,
  created_at              TIMESTAMPTZ NOT NULL DEFAULT now(),
  started_at              TIMESTAMPTZ,
  concluded_at            TIMESTAMPTZ,
  audit_reference         UUID NOT NULL,
  CONSTRAINT sessions_immutable CHECK (true)  -- mutation enforced at app layer
);

CREATE TABLE session_participants (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  session_id          UUID NOT NULL REFERENCES sessions(session_id),
  user_id_pseudonym   VARCHAR(256) NOT NULL,
  role                VARCHAR(32) NOT NULL,
  join_time_utc       TIMESTAMPTZ,
  leave_time_utc      TIMESTAMPTZ,
  connection_quality  NUMERIC(5,4),
  created_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE session_state_transitions (
  transition_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  session_id          UUID NOT NULL REFERENCES sessions(session_id),
  previous_state      VARCHAR(64) NOT NULL,
  new_state           VARCHAR(64) NOT NULL,
  trigger_event       VARCHAR(128) NOT NULL,
  actor_id            VARCHAR(256),
  input_hash          CHAR(64) NOT NULL,
  output_hash         CHAR(64) NOT NULL,
  created_at          TIMESTAMPTZ NOT NULL DEFAULT now()
  -- append-only: no UPDATE or DELETE permitted
);

CREATE INDEX idx_sessions_tenant ON sessions (tenant_id, current_state, created_at);
CREATE INDEX idx_sessions_domain ON sessions (domain_tag, tenant_id, created_at);
CREATE INDEX idx_sessions_tournament ON sessions (tournament_id) WHERE tournament_id IS NOT NULL;
CREATE INDEX idx_participants_session ON session_participants (session_id);
CREATE INDEX idx_transitions_session ON session_state_transitions (session_id, created_at);
```

---

## 2️⃣0️⃣ VERSIONING POLICY

```yaml
VERSION_FORMAT:           Semantic Versioning — MAJOR.MINOR.PATCH
CURRENT_VERSION:          v1.0.0
MUTATION_POLICY:          ADD-ONLY

CHANGE_RULES:
  MAJOR_bump: >
    Scoring formula class changes, state machine topology changes,
    belt promotion rule changes, transport protocol changes.
    REQUIRES: governance board approval, migration plan, backward
    compatibility window defined.
  MINOR_bump: >
    New session types, new optional fields, new phase configurations,
    new domain support — non-breaking.
  PATCH_bump: >
    Bug fixes, threshold adjustments, documentation corrections.

BACKWARD_COMPATIBILITY:   MANDATORY for MINOR and PATCH
MIGRATION_REQUIRED:       MAJOR only

ROLLBACK_POLICY:
  trigger: >
    Session creation success rate drops > 1%,
    OR post-session pipeline trigger failure rate > 2%,
    OR CLEAN session rate drops > 5%
  action: Automatic rollback to last stable version
  notification: MODEL_GOVERNANCE_AGENT + COMPLIANCE_AGENT + SECURITY_AGENT

CONTENT_VERSIONING (per Dojo Spec T8):
  scenario_versioning: ENFORCED — SCENARIO_REGISTRY manages versions
  rubric_versioning:   ENFORCED — rubric_version sealed into every session record
  belt_version_tag:    ENFORCED — belt award carries rubric_version and model_version stamps
  re_certification_trigger: Major rubric change triggers re-certification cycle (per T17)
```

---

## 2️⃣1️⃣ NON-NEGOTIABLE RULES

```
THIS AGENT MUST NOT:
  ❌  Allow any state transition outside the declared state machine topology
  ❌  Create hidden session logic or undocumented phase behaviors
  ❌  Allow session state mutation by any external agent or service
  ❌  Issue video tokens from any authority other than this agent
  ❌  Permit role changes mid-session (roles are sealed at session creation)
  ❌  Allow auto belt promotion without mentor confirmation
  ❌  Process scoring events outside declared score windows
  ❌  Allow recording activation without recording_consent_tokens for all participants
  ❌  Forward XP or Rank updates for sessions with unresolved HIGH+ anomaly flags
  ❌  Allow CERTIFICATION or RANKED sessions without verified mentor certification
  ❌  Cross tenant boundaries in session creation, participant lookup, or data reads
  ❌  Modify historical session records, audit logs, or match records
  ❌  Delete or truncate audit logs for any reason
  ❌  Allow belt gate check to proceed for sessions with integrity_status != CLEAN
  ❌  Operate without emitting an audit record for every state transition
  ❌  Allow an EMERGENCY_STOP to be delayed or debated under any circumstance
  ❌  Execute any action using AI layer output that overrides ML session confidence score
  ❌  Mix scenario content across domain boundaries
  ❌  Permit sessions to start without BILLING_CONFIRMED signal
  ❌  Execute silently — every failure produces a logged, traceable incident
```

---

## 2️⃣2️⃣ DOCUMENT SEAL

```
═══════════════════════════════════════════════════════════════════════════════════
  DOCUMENT SEAL — ECOSKILLER ANTIGRAVITY GOVERNANCE
═══════════════════════════════════════════════════════════════════════════════════
  Agent Name:        DOJO_SESSION_ORCHESTRATION_AGENT
  Agent ID:          ECSK-AGT-GOVERN-0043
  Layer:             Governance & Core Control
  Platform:          Ecoskiller Antigravity
  Document Version:  v1.0.0
  Status:            SEALED · LOCKED · GOVERNED · DETERMINISTIC
  Mutation Policy:   ADD-ONLY via version bump
  Authority:         Human Declaration Only
  Sealed:            2026-02-28T00:00:00Z

  THIS DOCUMENT IS SEALED.
  No field, state, rule, formula, or architectural decision may be modified,
  reinterpreted, extended, or overridden without a formally declared
  versioned amendment following ADD-ONLY policy.
  All prior versions remain permanently immutable.

  ARCHITECTURE INTERPRETATION: FORBIDDEN
  ASSUMPTION FILLING: FORBIDDEN
  IMPLICIT BEHAVIOR: FORBIDDEN

  VIOLATION OF THIS SEAL =
  GOVERNANCE_FAILURE + IMMEDIATE ESCALATION TO SECURITY_AGENT + COMPLIANCE_AGENT
═══════════════════════════════════════════════════════════════════════════════════
```

---

*End of DOJO_SESSION_ORCHESTRATION_AGENT.md — v1.0.0 — SEALED*
