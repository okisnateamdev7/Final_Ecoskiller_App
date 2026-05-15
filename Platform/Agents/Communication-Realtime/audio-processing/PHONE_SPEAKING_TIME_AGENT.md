# PHONE_SPEAKING_TIME_AGENT.md

```
╔══════════════════════════════════════════════════════════════════════════════════════════════╗
║            ECOSKILLER ANTIGRAVITY — PHONE SPEAKING TIME AGENT                              ║
║                         SEALED · LOCKED · GOVERNED · DETERMINISTIC                         ║
║                                                                                              ║
║  Agent ID           : AGPSTA-005                                                             ║
║  Mutation Policy    : Add-only via version bump                                              ║
║  Interpretation     : NONE PERMITTED                                                         ║
║  Execution Auth     : Human declaration only                                                 ║
║  Classification     : CRITICAL REAL-TIME INFRASTRUCTURE —                                   ║
║                       SPEAKING TIME MEASUREMENT, ENFORCEMENT & INTELLIGENCE FEED LAYER     ║
╚══════════════════════════════════════════════════════════════════════════════════════════════╝
```

---

## SECTION 0 — SEAL DECLARATION

This agent specification is **SEALED**.

> No component of this agent may be interpreted, rewritten, abbreviated, inferred,
> or executed in any manner inconsistent with what is explicitly declared below.
> Any ambiguity MUST halt execution and escalate to the Governance Authority immediately.
> No silent assumptions. No creative logic. No discretionary measurement.
> Every millisecond of speaking time is tracked, validated, logged, and immutable.

**GOVERNANCE AUTHORITY   :** `ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT`
**COMPLIANCE AUTHORITY   :** `AUDIT_COMPLIANCE_AGENT`
**SECURITY AUTHORITY     :** `ZERO_TRUST_AGENT`
**SESSION AUTHORITY      :** `VOICE_GD_ORCHESTRATOR_AGENT`
**SCORING AUTHORITY      :** `SCORING_ENGINE`
**INTELLIGENCE AUTHORITY :** `INTELLIGENCE_SCORING_ML_AGENT`

---

## SECTION 1 — AGENT IDENTITY (MANDATORY)

```yaml
AGENT_NAME             : PHONE_SPEAKING_TIME_AGENT
AGENT_ID               : AGPSTA-005
SYSTEM_ROLE            : Real-Time Speaking Time Tracker, Token Utilisation Measurer,
                         Dominance Detector, Silence Classifier, Turn Fairness Enforcer,
                         Intelligence Signal Emitter, and Score Input Producer
                         for all Voice Session Types across the Antigravity Platform
PRIMARY_DOMAIN         : Voice GD Sessions / Dojo Match Sessions /
                         Interview Sessions / Intelligence Test Sessions /
                         School GD Sessions / Dojo Group Discussion Framework
EXECUTION_MODE         : Real-Time Deterministic + Sub-Second Precision +
                         Event-Driven + Stateless per Turn
DATA_SCOPE             : Speaking time events per participant per turn (millisecond precision),
                         mic state change events (open/muted), silence windows,
                         token utilisation metrics, round-level aggregates,
                         session-level time distribution summaries,
                         role-weighted time expectations,
                         cross-turn fairness indices
                         — NO raw audio, NO speech content, NO biometric voice data
TENANT_SCOPE           : Strict Multi-Tenant Isolation
                         (Redis namespace + Kafka partition isolation by tenant_id)
FAILURE_POLICY         : HALT on ambiguity — STOP → LOG → ESCALATE
                         — NO silent time misattribution under any circumstance
MUTATION_POLICY        : Add-only versioned — no retroactive time record modification
AUDIT_POLICY           : Append-only immutable audit trail per turn per participant
SECURITY_MODEL         : Zero-trust — every time event validated against session state,
                         every measurement cryptographically timestamped
VERSION                : v1.0.0
STATUS                 : ACTIVE
```

---

## SECTION 2 — PURPOSE DECLARATION

### 2.1 The Problem This Agent Solves

The Ecoskiller Antigravity platform's Voice GD system is built on a **core design philosophy**:

> *Speaking is enforced, not requested.*
> *Order is computed, not negotiated.*
> *Evaluation is numeric, not interpretive.*
> *Bias is structurally eliminated through protocol design.*

At the heart of this philosophy is a single, irreducible truth: **speaking time is the primary measurable behavior** in a voice group discussion. Everything else — scores, intelligence signals, XP, passport updates, fairness indices — is derived from it.

However, measuring speaking time in a real-time, multi-participant, mobile-tolerant, WebRTC-based system at scale introduces a precise set of challenges:

```
CHALLENGE 1 — CLOCK PRECISION
  Jitsi mute/unmute events arrive via WebSocket with millisecond timestamps.
  Network jitter means events may arrive out of order or with clock drift.
  Speaking time must be computed with sub-second precision despite this.

CHALLENGE 2 — MULTI-ROUND COMPLEXITY
  A GD session has multiple structured rounds:
  INTRO (60s per speaker), REBUTTAL (30s), OPEN DISCUSSION (unstructured),
  CONCLUSION (45s). Each round has different time allowances, different
  fairness expectations, and different scoring weights.
  The Dojo GD framework adds roles (Moderator, Speaker, Idea Generator,
  Note Keeper, Questioner) with role-specific time expectations.
  The agent must track, aggregate, and classify time across all rounds
  simultaneously without confusion.

CHALLENGE 3 — OPEN DISCUSSION FAIRNESS
  During the open discussion round, all participants are simultaneously
  unmuted. There is no token — only measurement. Dominance must be
  detected and penalised numerically. Silence must be detected and logged.
  Fairness must be computed in real time, not post-session.

CHALLENGE 4 — BOT VETO INTERACTION
  If PHONE_BOT_VOICE_DETECTION_AGENT (AGVDA-001) issues a BOT_CONFIRMED veto
  for a participant, that participant's speaking time must be immediately
  excluded from all fair-distribution calculations and score inputs.
  The time happened — it must be logged — but it must be flagged as
  BOT_VETOED and excluded from peer fairness comparisons.

CHALLENGE 5 — INTELLIGENCE SIGNAL PRODUCTION
  GD trains interpersonal intelligence, linguistic intelligence, and logical
  intelligence (as defined in the Ecoskiller Intelligence Framework based on
  Howard Gardner's Multiple Intelligences Theory).
  Speaking time data — not in isolation, but in combination with turn
  utilisation patterns, silence ratios, and cross-participant fairness —
  is the primary behavioral signal for these intelligence dimensions.
  The agent must emit structured intelligence feature vectors compatible
  with INTELLIGENCE_SCORING_ML_AGENT and PASSIVE_INTELLIGENCE_ENGINE.

CHALLENGE 6 — REAL-TIME ENFORCEMENT VS POST-SESSION SCORING
  Score calculation happens post-session (in SCORING_ENGINE).
  But fairness enforcement (dominance penalty, silence penalty) must happen
  in real time so that the state machine (VOICE_GD_ORCHESTRATOR_AGENT)
  can adjust session behavior during the open discussion round.
  The agent must serve both real-time enforcement and post-session scoring
  from the same measurement pipeline.

CHALLENGE 7 — DOJO ROLE-WEIGHTED TIME EXPECTATIONS
  In the Dojo GD framework, different roles have different expected
  speaking time profiles. A Moderator is expected to speak in short bursts
  to introduce phases. A Speaker is expected to use the majority of their
  allocated time. A Questioner should speak briefly but at high frequency.
  The agent must compare actual speaking time against role-specific
  expected profiles, not a flat per-participant expectation.

CHALLENGE 8 — MOBILE NETWORK TOLERANCE
  The platform targets India — 40–60 kbps upload bandwidth on mobile networks.
  Network drops cause mic state events to be lost or delayed.
  The agent must detect network-drop windows and classify them correctly as
  NETWORK_DROP (neutral) rather than SILENCE (participant failure).
```

> The `PHONE_SPEAKING_TIME_AGENT` is the **real-time speaking time measurement, validation,
> classification, and distribution engine** that converts raw mic-state event streams into
> precise, role-aware, bot-clean, fairness-indexed, intelligence-compatible time records
> that are the definitive input to scoring, intelligence profiling, XP calculation,
> passport updates, and parent reports.

### 2.2 What This Agent Does NOT Do

```
✗  Does not grant or revoke speaking tokens — that is VOICE_GD_ORCHESTRATOR's responsibility
✗  Does not detect bots — that is PHONE_BOT_VOICE_DETECTION_AGENT's responsibility
✗  Does not calculate final scores — that is SCORING_ENGINE's responsibility
✗  Does not assemble session transcripts — that is TRANSCRIPT_AGGREGATION_AGENT's responsibility
✗  Does not store raw audio — prohibited by architecture contract
✗  Does not interpret speech content — content is never accessed
✗  Does not adjudicate disputes — that is ADMIN_GOVERNANCE_SERVICE's responsibility
✗  Does not update XP directly — it emits events; GROWTH_ENGINE updates XP
```

### 2.3 Upstream Agents

```
VOICE_GD_ORCHESTRATOR_AGENT        → Token grant/revoke events, session config, round structure
JITSI_SESSION_LAYER                → Mute/unmute state change events (millisecond timestamped)
WEBRTC_TRANSPORT_LAYER             → Network telemetry per participant (jitter, drops)
PHONE_BOT_VOICE_DETECTION_AGENT    → BOT_CONFIRMED / BOT_SUSPECTED flags per participant
AUTH_SERVICE                       → Participant identity binding per session
GD_ATTENDANCE_TRACKING_AGENT       → Participant presence classification
DOJO_SESSION_ORCHESTRATION_AGENT   → Role assignments (Moderator, Speaker, etc.) for Dojo GD
```

### 2.4 Downstream Agents

```
SCORING_ENGINE                             → Receives speaking time record as scoring input
TRANSCRIPT_AGGREGATION_AGENT (AGTAA-002)   → Receives speaking time data per turn per participant
INTELLIGENCE_SCORING_ML_AGENT              → Receives intelligence feature vectors
PASSIVE_INTELLIGENCE_ENGINE                → Receives behavioral speaking signals
GD_POST_SESSION_ANALYTICS_AGENT           → Receives time distribution analytics payload
PARENT_LLM_INSIGHT_NARRATIVE_AGENT        → Receives speaking time summary for reports
PASSPORT_AGGREGATION_AGENT                → Receives communication skill signals
VOICE_GD_ORCHESTRATOR_AGENT               → Receives real-time fairness alerts (open discussion)
OBSERVABILITY_AGENT                        → Receives performance metrics
FEATURE_STORE_SERVICE                      → Receives emitted feature vectors
AUDIT_COMPLIANCE_AGENT                     → Receives immutable time audit records
```

---

## SECTION 3 — INPUT CONTRACT (STRICT)

### 3.1 Trigger Events

This agent operates in **two concurrent modes** simultaneously for every active session:

```yaml
MODE_A — REAL_TIME_STREAMING (per mic event, during session):
  trigger : Every mute/unmute state change event from Jitsi/WebRTC layer
  latency : Must be processed within 50ms of event arrival
  purpose : Live time accumulation, live fairness index, real-time enforcement signals

MODE_B — TURN_COMPLETION_SNAPSHOT (per turn end):
  trigger : turn.token_revoked OR turn.timer_expired event from VOICE_GD_ORCHESTRATOR
  latency : Must be computed and emitted within 200ms of turn end
  purpose : Immutable per-turn speaking time record for scoring and transcription
```

### 3.2 Mic State Event Schema (Real-Time Input)

```json
MIC_STATE_EVENT: {
  "event_id"              : "UUID v4",
  "session_id"            : "UUID",
  "participant_id"        : "UUID",
  "tenant_id"             : "UUID",
  "turn_id"               : "UUID | null (null during open discussion)",
  "round_type"            : "INTRO | REBUTTAL | OPEN | CONCLUSION | PREPARATION",
  "event_type"            : "MIC_OPENED | MIC_CLOSED | NETWORK_DROP | NETWORK_RESTORED",
  "event_timestamp_utc"   : "ISO-8601 with millisecond precision",
  "jitsi_server_clock_ms" : "integer — Jitsi server-side clock at event time (ms since epoch)",
  "webrtc_sequence_number": "integer — monotonically increasing per participant",
  "source"                : "JITSI_API | WEBRTC_SIGNALING | ORCHESTRATOR_FORCED",
  "forced_by_orchestrator": "boolean — true if mute/unmute was forced by state machine",
  "network_quality_flag"  : "STABLE | DEGRADED | DROP"
}
```

### 3.3 Session Config Input (Received on Session Start)

```json
SESSION_SPEAKING_CONFIG: {
  "session_id"            : "UUID",
  "tenant_id"             : "UUID",
  "session_type"          : "GD | DOJO_GD | INTERVIEW | INTELLIGENCE_TEST",
  "round_structure"       : [
    {
      "round_type"              : "INTRO",
      "round_sequence"          : 1,
      "allocated_seconds_per_turn" : 60,
      "speaking_mode"           : "TOKEN_BASED",
      "fairness_enforcement"    : false
    },
    {
      "round_type"              : "REBUTTAL",
      "round_sequence"          : 2,
      "allocated_seconds_per_turn" : 30,
      "speaking_mode"           : "TOKEN_BASED",
      "fairness_enforcement"    : false
    },
    {
      "round_type"              : "OPEN",
      "round_sequence"          : 3,
      "allocated_seconds_total" : 300,
      "speaking_mode"           : "FREE_WITH_MEASUREMENT",
      "fairness_enforcement"    : true,
      "dominance_threshold_pct" : 40,
      "silence_threshold_seconds" : 30
    },
    {
      "round_type"              : "CONCLUSION",
      "round_sequence"          : 4,
      "allocated_seconds_per_turn" : 45,
      "speaking_mode"           : "TOKEN_BASED",
      "fairness_enforcement"    : false
    }
  ],
  "participant_roles"     : [
    {
      "participant_id"          : "UUID",
      "role"                    : "MODERATOR | SPEAKER | IDEA_GENERATOR | NOTE_KEEPER | QUESTIONER | STANDARD",
      "role_time_expectation_profile" : "string — see Section 5.3"
    }
  ],
  "bot_veto_participants" : ["UUID array — participants with BOT_CONFIRMED at session start"],
  "total_participants"    : "integer",
  "fair_share_seconds"    : "float — total_open_seconds / total_participants"
}
```

### 3.4 Input Validation Rules

```json
INPUT_VALIDATION: {
  "required_fields": [
    "event_id", "session_id", "participant_id", "tenant_id",
    "event_type", "event_timestamp_utc", "jitsi_server_clock_ms",
    "webrtc_sequence_number", "round_type", "source"
  ],
  "validation_rules": [
    "event_id must be UUID v4",
    "session_id must be active in Redis session registry",
    "participant_id must be in session participant roster",
    "tenant_id must match authenticated session tenant",
    "event_type must be one of declared MIC_STATE_EVENT types",
    "event_timestamp_utc must have millisecond precision",
    "jitsi_server_clock_ms must be within ±5000ms of server UTC clock",
    "webrtc_sequence_number must be >= last seen sequence for this participant",
    "event_type MIC_OPENED cannot follow MIC_OPENED without intervening MIC_CLOSED",
    "event_type MIC_CLOSED cannot follow MIC_CLOSED without intervening MIC_OPENED"
  ],
  "security_checks": [
    "Verify event source is JITSI_SESSION_LAYER or WEBRTC_TRANSPORT_LAYER",
    "Verify tenant_id is consistent with session_id",
    "Reject events from participants not in session roster",
    "Reject events with sequence numbers lower than last processed (replay guard)"
  ],
  "domain_checks": [
    "Verify round_type matches current active round in Redis state machine",
    "Verify participant is not in BOT_CONFIRMED state (events still logged but flagged)",
    "Verify session is in ACTIVE state (not COMPLETED, PAUSED, or TERMINATED)"
  ]
}
```

### 3.5 Null Tolerance Policy

```
NULL TOLERANCE: ZERO for all required fields
Out-of-sequence event         → LOG as SEQUENCE_ANOMALY, apply best-effort ordering
Clock drift > 5000ms          → FLAG as CLOCK_ANOMALY, apply NTP correction
Cross-tenant event            → HALT + SECURITY_ALERT
Unrecognised participant_id   → REJECT + LOG
Bot-confirmed participant event → ACCEPT (log) + FLAG as BOT_VETOED (exclude from scores)
```

---

## SECTION 4 — SPEAKING TIME MEASUREMENT ENGINE

### 4.1 Core Time Accumulation Algorithm

This algorithm runs **per participant per session** in Redis, updated on every mic state event.

```
ALGORITHM: ACCUMULATE_SPEAKING_TIME(mic_state_event)

STEP 1 — CLOCK NORMALISATION
  Use jitsi_server_clock_ms as primary timestamp source
  Apply NTP drift correction from WebRTC telemetry baseline
  Produce: normalised_event_timestamp_ms (monotonically consistent per session)

STEP 2 — SEQUENCE VALIDATION
  Verify webrtc_sequence_number > last_processed_sequence[participant_id]
  If out-of-order: buffer event for 100ms, attempt reorder
  If still out-of-order after 100ms: apply best-effort insertion with SEQUENCE_ANOMALY flag

STEP 3 — STATE MACHINE UPDATE
  CURRENT_MIC_STATE[participant_id] ∈ {OPEN, CLOSED, NETWORK_DROP}
  Transition rules:
    MIC_OPENED   → if state was CLOSED or NETWORK_DROP: record mic_open_start_ms
    MIC_CLOSED   → if state was OPEN: compute interval, add to speaking_time_ms
                   interval = normalised_event_timestamp_ms - mic_open_start_ms
    NETWORK_DROP → if state was OPEN: record drop_start_ms
                   state transitions to NETWORK_DROP (not CLOSED — different semantics)
    NETWORK_RESTORED → if state was NETWORK_DROP:
                   compute drop_duration_ms = now - drop_start_ms
                   add to network_drop_time_ms (NOT speaking time, NOT silence)

STEP 4 — INTERVAL VALIDATION
  If computed interval_ms < 0 → REJECT (clock inconsistency) + LOG
  If computed interval_ms > allocated_turn_duration_ms + 500ms (grace) →
    CAP at allocated_turn_duration_ms, FLAG as OVERFLOW_CLIPPED
  If interval_ms < 50ms → classify as PHANTOM_OPEN (mic noise, not speech) → DO NOT COUNT

STEP 5 — CLASSIFICATION
  Classify each speaking window:
    duration_ms < 500ms     → MICRO_UTTERANCE (counted but flagged separately)
    duration_ms 500–3000ms  → SHORT_BURST
    duration_ms 3000–15000ms → NORMAL_SPEECH
    duration_ms > 15000ms   → EXTENDED_SPEECH (normal in long turns)

STEP 6 — RUNNING TOTALS UPDATE (Redis)
  Atomic increment on all relevant counters:
    speaking_time_ms[participant_id][round_type]
    speaking_time_ms[participant_id][SESSION_TOTAL]
    speech_burst_count[participant_id][round_type]
    micro_utterance_count[participant_id][round_type]
    current_token_speaking_ms[participant_id]  ← resets on token change

STEP 7 — EMIT REAL-TIME EVENT
  Emit: speaking.time.event_processed
  Payload: {session_id, participant_id, round_type, current_token_speaking_ms,
            session_total_speaking_ms, classification}
  Consumer: VOICE_GD_ORCHESTRATOR_AGENT (real-time enforcement)
```

### 4.2 Silence Detection Algorithm

Silence during a speaking token is a distinct behavioral signal from a network drop.

```yaml
SILENCE_DETECTION:

  TOKEN_BASED_ROUNDS (INTRO, REBUTTAL, CONCLUSION):
    definition   : Token is active (MIC_OPENED by orchestrator) but participant
                   has produced < 500ms of speech in any 10-second window
    detection    : continuous_silence_ms > SILENCE_THRESHOLD_MS (default: 10000ms)
    action       : Emit speaking.silence.detected event
                   Record silence_window: {start_ms, duration_ms, round_type}
                   DO NOT force close token — silence is a valid behavioral signal
    classify_as  : VOLUNTARY_SILENCE if mic was opened by orchestrator
                   TOKEN_UNUSED if participant spoke 0ms in entire turn

  OPEN_DISCUSSION_ROUND:
    definition   : Participant has mic open (free) but speaks < 500ms in
                   any 30-second window during the open round
    detection    : rolling_silence_ms > OPEN_SILENCE_THRESHOLD_MS (default: 30000ms)
    action       : Emit speaking.open_round.silence_detected event
                   This feeds into dominance fairness calculation (Section 4.4)
    classify_as  : PASSIVE_PARTICIPANT if silence > 60% of open round duration
```

### 4.3 Token Utilisation Computation

Computed at the end of every token (turn completion snapshot):

```json
TOKEN_UTILISATION_RECORD: {
  "record_id"                   : "UUID",
  "session_id"                  : "UUID",
  "participant_id"              : "UUID",
  "turn_id"                     : "UUID",
  "round_type"                  : "INTRO | REBUTTAL | CONCLUSION",
  "allocated_duration_ms"       : "integer",
  "actual_speaking_ms"          : "integer",
  "silence_ms"                  : "integer (allocated - speaking - network_drop)",
  "network_drop_ms"             : "integer",
  "bot_vetoed"                  : "boolean",
  "token_utilisation_pct"       : "float 0.0–100.0",
  "speech_burst_count"          : "integer",
  "micro_utterance_count"       : "integer",
  "speech_start_latency_ms"     : "integer (time from MIC_OPENED to first 500ms+ speech)",
  "longest_continuous_speech_ms": "integer",
  "silence_windows"             : [
    {"start_ms": "integer", "duration_ms": "integer", "classify": "VOLUNTARY | NETWORK"}
  ],
  "utilisation_class"           : "FULL (>= 80%) | PARTIAL (40–79%) | MINIMAL (10–39%) |
                                   UNUSED (< 10%) | NETWORK_AFFECTED | BOT_VETOED",
  "overflow_clipped"            : "boolean",
  "clock_anomaly_flag"          : "boolean",
  "sequence_anomaly_flag"       : "boolean",
  "model_version"               : "AGPSTA-005-v1.0.0",
  "audit_reference"             : "UUID"
}
```

### 4.4 Open Discussion Fairness Engine

During the OPEN round, all participants are simultaneously unmuted. This engine runs **continuously** during the open round, updating every 5 seconds.

```yaml
FAIRNESS_ENGINE:

  INPUTS:
    - speaking_time_ms[all_active_participants][OPEN_ROUND]  (running totals from Redis)
    - fair_share_seconds (from SESSION_SPEAKING_CONFIG)
    - total_open_round_elapsed_ms
    - bot_vetoed_participant_ids

  COMPUTATIONS:

    1. FAIR_SHARE_EXPECTED_MS:
       fair_share_expected_ms = (total_open_round_elapsed_ms / total_eligible_participants)
       eligible = participants with attendance_class = PRESENT AND NOT bot_vetoed

    2. DOMINANCE_RATIO per participant:
       dominance_ratio = speaking_ms[p] / total_speaking_ms_all_eligible_participants
       DOMINANT if dominance_ratio > DOMINANCE_THRESHOLD_PCT / 100 (default: 0.40)
       Example: if one participant has spoken > 40% of all spoken time → DOMINANT

    3. SHARE_DEVIATION per participant:
       share_deviation = speaking_ms[p] - fair_share_expected_ms
       ABOVE_FAIR if deviation > +20% of fair_share
       BELOW_FAIR if deviation < -20% of fair_share
       AT_FAIR    if within ±20%

    4. GINI_COEFFICIENT (session-level fairness index):
       Computed over all eligible participant speaking times
       GINI = 0.0 → perfect equality
       GINI > 0.6 → severely unequal session
       Updated every 5 seconds and stored in Redis

    5. DOMINANCE_ALERT:
       IF any participant's dominance_ratio > DOMINANCE_THRESHOLD:
         Emit: speaking.open_round.dominance_alert
         Payload: {session_id, dominant_participant_id, dominance_ratio,
                   current_gini, elapsed_seconds}
         Consumer: VOICE_GD_ORCHESTRATOR_AGENT
         Note: Orchestrator DECIDES whether to intervene — this agent only reports

  OUTPUTS:
    - FAIRNESS_SNAPSHOT every 5 seconds (stored in Redis, TTL = session + 30min)
    - DOMINANCE_ALERT events (real-time, to VOICE_GD_ORCHESTRATOR_AGENT)
    - FINAL_FAIRNESS_RECORD at session end (permanent, to SCORING_ENGINE)
```

### 4.5 Role-Weighted Time Expectation Profiles (Dojo GD Framework)

Used when `session_type = DOJO_GD` and participant roles are assigned.

```yaml
ROLE_TIME_PROFILES:

  MODERATOR:
    expected_speaking_pct_of_allocation : 20–40%
    expected_burst_pattern              : FREQUENT_SHORT (many bursts < 5s)
    expected_silence_tolerance          : HIGH (Moderator often silent while others speak)
    scoring_weight_time_utilisation     : 0.3 (lower — moderation is not about speaking volume)
    scoring_weight_burst_frequency      : 0.7 (Moderator scored on engagement frequency)

  SPEAKER:
    expected_speaking_pct_of_allocation : 70–100%
    expected_burst_pattern              : FEW_LONG (1–3 sustained speech segments)
    expected_silence_tolerance          : LOW
    scoring_weight_time_utilisation     : 0.8
    scoring_weight_burst_frequency      : 0.2

  IDEA_GENERATOR:
    expected_speaking_pct_of_allocation : 50–80%
    expected_burst_pattern              : MEDIUM_BURST (3–6 bursts, 5–15s each)
    expected_silence_tolerance          : MEDIUM
    scoring_weight_time_utilisation     : 0.5
    scoring_weight_burst_frequency      : 0.5

  NOTE_KEEPER:
    expected_speaking_pct_of_allocation : 10–30%
    expected_burst_pattern              : VERY_SHORT (summary utterances < 3s)
    expected_silence_tolerance          : VERY_HIGH
    scoring_weight_time_utilisation     : 0.1
    scoring_weight_burst_frequency      : 0.9 (Note Keeper scores on concise contributions)

  QUESTIONER:
    expected_speaking_pct_of_allocation : 20–40%
    expected_burst_pattern              : SHORT_FREQUENT (many bursts 3–8s each)
    expected_silence_tolerance          : HIGH
    scoring_weight_time_utilisation     : 0.4
    scoring_weight_burst_frequency      : 0.6

  STANDARD (no role assignment, standard GD):
    expected_speaking_pct_of_allocation : 70–100%
    expected_burst_pattern              : MIXED
    expected_silence_tolerance          : LOW
    scoring_weight_time_utilisation     : 0.6
    scoring_weight_burst_frequency      : 0.4
```

---

## SECTION 5 — SESSION-LEVEL TIME DISTRIBUTION RECORD

Produced once per session at session end. This is the **canonical time distribution record** consumed by all downstream agents.

```json
SESSION_TIME_DISTRIBUTION: {
  "distribution_id"             : "UUID",
  "session_id"                  : "UUID",
  "tenant_id"                   : "UUID",
  "session_type"                : "GD | DOJO_GD | INTERVIEW | INTELLIGENCE_TEST",
  "assembled_at_utc"            : "ISO-8601",
  "model_version"               : "AGPSTA-005-v1.0.0",
  "audit_reference"             : "UUID",

  "session_totals": {
    "total_session_duration_ms" : "integer",
    "total_eligible_speaking_ms": "integer (sum across eligible, non-bot participants)",
    "total_silence_ms"          : "integer (sum of all TOKEN_UNUSED + VOLUNTARY windows)",
    "total_network_drop_ms"     : "integer",
    "session_speaking_ratio"    : "float 0.0–1.0 (speaking / total_session_duration)",
    "gini_coefficient_final"    : "float 0.0–1.0",
    "fairness_class"            : "EXCELLENT (<0.2) | GOOD (0.2–0.35) | MODERATE (0.35–0.5) |
                                   POOR (0.5–0.65) | SEVERELY_UNEQUAL (>0.65)"
  },

  "participant_summaries"       : [
    {
      "participant_id"              : "UUID",
      "role"                        : "MODERATOR | SPEAKER | ... | STANDARD",
      "bot_status"                  : "HUMAN | BOT_SUSPECTED | BOT_CONFIRMED",
      "attendance_class"            : "PRESENT | LATE | DROPPED",

      "by_round"                    : {
        "INTRO"     : {
          "allocated_ms"            : 60000,
          "speaking_ms"             : 52300,
          "silence_ms"              : 5200,
          "network_drop_ms"         : 2500,
          "utilisation_pct"         : 87.2,
          "utilisation_class"       : "FULL",
          "speech_burst_count"      : 3,
          "speech_start_latency_ms" : 620
        },
        "REBUTTAL"  : {"...": "same structure"},
        "OPEN"      : {
          "allocated_ms"            : "null (no per-participant allocation)",
          "speaking_ms"             : 48200,
          "dominance_ratio"         : 0.22,
          "share_deviation_ms"      : 8200,
          "share_class"             : "ABOVE_FAIR",
          "silence_ms"              : 12000,
          "speech_burst_count"      : 14,
          "dominant_alerts_issued"  : 0
        },
        "CONCLUSION": {"...": "same structure as INTRO"}
      },

      "session_totals"              : {
        "total_speaking_ms"         : "integer",
        "total_silence_ms"          : "integer",
        "total_network_drop_ms"     : "integer",
        "total_utilisation_pct"     : "float",
        "all_turns_completed"       : "boolean",
        "any_turn_fully_unused"     : "boolean",
        "role_expectation_match"    : "EXCEEDS | MEETS | BELOW | NOT_APPLICABLE",
        "interrupt_attempts"        : "integer (from VOICE_GD_ORCHESTRATOR events)",
        "dominant_flag"             : "boolean"
      },

      "score_inputs"                : {
        "used_all_turns"            : "boolean",
        "spoke_full_time"           : "boolean (>= 80% utilisation across token rounds)",
        "interrupt_attempts"        : "integer",
        "open_round_share_class"    : "ABOVE_FAIR | AT_FAIR | BELOW_FAIR | PASSIVE",
        "dominance_penalty_applies" : "boolean",
        "silence_penalty_applies"   : "boolean",
        "role_time_compliance"      : "float 0.0–1.0 (Dojo GD only)"
      },

      "intelligence_signals"        : {
        "interpersonal_signal"      : "float 0.0–1.0 — see Section 6.2",
        "linguistic_signal"         : "float 0.0–1.0 — see Section 6.2",
        "logical_signal"            : "float 0.0–1.0 — see Section 6.2"
      },

      "anomaly_flags"               : ["CLOCK_ANOMALY", "SEQUENCE_ANOMALY", "OVERFLOW_CLIPPED",
                                       "BOT_VETOED", "NETWORK_AFFECTED"]
    }
  ],

  "fairness_timeline"           : [
    {
      "snapshot_elapsed_seconds"  : 30,
      "gini_coefficient"          : 0.21,
      "dominant_participant_count": 0,
      "passive_participant_count" : 1
    }
  ],

  "dominance_events"            : [
    {
      "event_timestamp_utc"       : "ISO-8601",
      "participant_id"            : "UUID",
      "dominance_ratio_at_event"  : 0.43,
      "orchestrator_intervened"   : "boolean"
    }
  ]
}
```

---

## SECTION 6 — ML / AI LOGIC LAYER

### 6.1 Architecture Overview

```
AI MUST ASSIST ML — AI MUST NOT REPLACE ML
All speaking time measurement is rule-based and deterministic.
ML layer handles intelligence signal derivation and anomaly detection.
AI layer is used ONLY for narrative generation (parent reports, anomaly summaries).
```

### 6.2 ML Layer — Intelligence Signal Derivation

Speaking time patterns are the primary behavioral input for three of Howard Gardner's intelligence dimensions in the Ecoskiller Intelligence Framework.

```yaml
MODEL_TYPE          : Gradient Boosted Regression (per intelligence dimension)
TRAINING_TARGET     : Intelligence dimension score (0–100) validated against
                      DOJO_INTELLIGENCE_TESTING_SERVICE ground truth labels

─────────────────────────────────────────────────────────────────────────────
INTERPERSONAL_INTELLIGENCE_SIGNAL:
  Definition       : Measures ability to engage others, listen, respond in turn,
                     and maintain equitable group dynamics
  Features         :
    - open_round_share_class           (ABOVE | AT | BELOW | PASSIVE)
    - dominant_flag                    (boolean — dominance damages interpersonal score)
    - speech_start_latency_ms          (lower latency = better listener responsiveness)
    - burst_frequency_vs_role_profile  (role-appropriate engagement frequency)
    - share_deviation_ms               (distance from fair share)
    - gini_contribution                (per-participant contribution to session Gini)
    - turns_completed_ratio            (completed all assigned turns = engagement)
  Output           : interpersonal_signal (0.0–1.0)
  Weight in profile: 0.35 of GD session intelligence contribution to Interpersonal dimension

─────────────────────────────────────────────────────────────────────────────
LINGUISTIC_INTELLIGENCE_SIGNAL:
  Definition       : Measures command of structured expression — ability to use
                     allocated speaking time effectively and with intentional structure
  Features         :
    - token_utilisation_pct            (effective use of allocated speaking time)
    - longest_continuous_speech_ms     (sustained coherent expression)
    - speech_start_latency_ms          (preparedness and confidence in speaking)
    - silence_ratio_in_token           (silence within own token = hesitation)
    - micro_utterance_ratio            (micro-utterances = fragmented expression)
    - intro_utilisation_pct            (first impression — opening statement quality)
    - conclusion_utilisation_pct       (structured closing)
  Output           : linguistic_signal (0.0–1.0)
  Weight in profile: 0.40 of GD session intelligence contribution to Linguistic dimension

─────────────────────────────────────────────────────────────────────────────
LOGICAL_INTELLIGENCE_SIGNAL:
  Definition       : Measures structured engagement — evidence of sequencing,
                     cause-effect patterns in participation timing, and role compliance
  Features         :
    - role_time_compliance             (Dojo GD only: did participant follow role pattern?)
    - burst_pattern_vs_role_profile    (did burst pattern match role expectation?)
    - rebuttal_utilisation_pct         (logical response to prior speakers)
    - interrupt_attempt_ratio          (interruption = impulsive, not logical)
    - utilisation_consistency          (consistent performance across all rounds)
  Output           : logical_signal (0.0–1.0)
  Weight in profile: 0.25 of GD session intelligence contribution to Logical dimension

─────────────────────────────────────────────────────────────────────────────
DRIFT_DETECTION     : Monitor feature distribution shift monthly (KS test)
                      Alert if distribution shifts > 0.08
TRAINING_FREQUENCY  : Monthly retrain on labeled session data
                      (labels from INTELLIGENCE_SCORING_ML_AGENT post-session scores)
VERSION_CONTROL     : model_version stored in every SESSION_TIME_DISTRIBUTION output
```

### 6.3 ML Layer — Speaking Time Anomaly Detector

Detects abnormal speaking time patterns that may indicate gaming, bot behaviour not caught by AGVDA-001, or technical issues requiring investigation.

```yaml
MODEL_TYPE          : Isolation Forest (unsupervised, per-session)
FEATURES_USED       :
  - token_utilisation_pct vs session historical baseline (z-score)
  - speech_start_latency_ms vs participant baseline (z-score)
  - burst_pattern vs role_profile deviation
  - silence_ratio vs session-type baseline
  - open_round_dominance_ratio vs session average
  - sequence_anomaly_flag_count
  - clock_anomaly_flag_count

OUTPUT              : anomaly_score (0.0–1.0) per participant per session
THRESHOLD:
  ALERT             : >= 0.80 → FLAG + emit to PHONE_BOT_VOICE_DETECTION_AGENT
                      (cross-reference bot detection results)
  SUSPICIOUS        : 0.55–0.79 → FLAG in SESSION_TIME_DISTRIBUTION anomaly_flags
  NORMAL            : < 0.55 → proceed cleanly

TRAINING_FREQUENCY  : Monthly retrain
VERSION_CONTROL     : Model version stored in audit records
```

### 6.4 AI Assist Layer (Scoped — 20%)

```yaml
AI_USAGE_SCOPE:
  - Generate speaking time narrative summary for PARENT_LLM_INSIGHT_NARRATIVE_AGENT
    Example: "Your child spoke for 52 seconds of their allocated 60 seconds in the
             opening round, using their time effectively to introduce their point of view."
  - Generate anomaly description text for ADMIN_GOVERNANCE_SERVICE review queue
  - Summarise fairness metrics for session analytics reports

PROMPT_GOVERNANCE:
  - All prompts versioned (AGPSTA-AI-PROMPT-vX.Y.Z)
  - AI output is advisory and narrative only
  - AI output NEVER modifies any structured time record field
  - AI output tagged with ai_generated: true
  - No participant PII in AI prompts — participant roles and anonymised IDs only

AI MUST NOT:
  - Reclassify speaking time measurements
  - Override silence or dominance classifications
  - Modify intelligence signal values
  - Access cross-tenant session data
  - Generate content about specific named participants
  - Autonomously trigger fairness enforcement actions
```

---

## SECTION 7 — SCORE INPUT CONTRACT

This agent produces `score_inputs` per participant which are the **mandatory input** to the `SCORING_ENGINE`. The SCORING_ENGINE may not compute a speaking-time-based score without receiving a validated `SESSION_TIME_DISTRIBUTION` from this agent.

### 7.1 Score Input Fields and Scoring Formula Guidance

```yaml
SCORE_INPUTS (per participant):

  used_all_turns:
    type    : boolean
    meaning : Completed every assigned turn in token-based rounds
    guidance: +10 points in base scoring formula

  spoke_full_time:
    type    : boolean
    meaning : Average token_utilisation_pct >= 80% across all token-based rounds
    guidance: +10 points in base scoring formula

  interrupt_attempts:
    type    : integer
    meaning : Number of confirmed interrupt attempts logged by VOICE_GD_ORCHESTRATOR
    guidance: -2 points per interrupt in base scoring formula

  open_round_share_class:
    type    : enum (ABOVE_FAIR | AT_FAIR | BELOW_FAIR | PASSIVE)
    meaning : Participation equity in open discussion round
    guidance:
      ABOVE_FAIR → +5 points (active, but check dominant_flag — if dominant, apply penalty)
      AT_FAIR    → +8 points (ideal — equitable participation)
      BELOW_FAIR → +2 points
      PASSIVE    → 0 points

  dominance_penalty_applies:
    type    : boolean
    meaning : Participant dominated open round (dominance_ratio > threshold)
    guidance: -5 points deducted from open round score
              (Dominance penalised numerically — system design principle)

  silence_penalty_applies:
    type    : boolean
    meaning : TOKEN_UNUSED in any token-based turn
    guidance: -3 points per unused token

  role_time_compliance (Dojo GD only):
    type    : float 0.0–1.0
    meaning : Alignment of actual speaking pattern with role expectation profile
    guidance: Multiplier (0.5–1.0) on participation score for Dojo GD sessions
              role_time_compliance = 1.0 → full multiplier (×1.0)
              role_time_compliance = 0.5 → half multiplier (×0.75)

BASE_FORMULA (Rule-Based — SCORING_ENGINE implements this):
  score =
    + (used_all_turns ? 10 : 0)
    + (spoke_full_time ? 10 : 0)
    - (interrupt_attempts * 2)
    + (open_round_points based on open_round_share_class)
    - (dominance_penalty_applies ? 5 : 0)
    - (silence_penalty_applies ? token_unused_count * 3 : 0)
    × (role_time_compliance_multiplier if DOJO_GD else 1.0)
  
  MAX_POSSIBLE: 33 points (from this agent's inputs)
  NOTE: Additional score components from SCORING_ENGINE
        (e.g., content quality if AI-assisted assessment) are added separately.
        This agent's inputs are the PARTICIPATION score only.
```

---

## SECTION 8 — REAL-TIME ENFORCEMENT PROTOCOL

### 8.1 Events Emitted to VOICE_GD_ORCHESTRATOR_AGENT (Real-Time)

This agent emits enforcement-relevant signals in real time. The VOICE_GD_ORCHESTRATOR_AGENT receives these signals and **decides whether to act**. This agent does not take enforcement actions directly.

```yaml
REAL_TIME_EVENTS:

  speaking.token.utilisation_update (every 5 seconds during active token):
    payload : {session_id, participant_id, turn_id, current_speaking_ms,
               allocated_ms, utilisation_pct, remaining_ms}
    purpose : Powers countdown timer UI on participant device

  speaking.silence.detected (during token-based rounds):
    payload : {session_id, participant_id, turn_id, silence_duration_ms,
               threshold_ms, silence_class}
    purpose : Orchestrator may flash UI warning if participant is silent for too long
    note    : THIS AGENT DOES NOT FORCE MUTE — Orchestrator decides

  speaking.open_round.dominance_alert (during open round):
    payload : {session_id, dominant_participant_id, dominance_ratio,
               gini_coefficient, elapsed_seconds, fair_share_ms}
    purpose : Orchestrator may choose to log, display public fairness meter, or intervene
    note    : DOMINANCE PENALISED IN SCORE — Orchestrator does not silence dominance
              (dominance is a valid behavioral signal; it is penalised, not blocked)

  speaking.open_round.passive_alert (during open round):
    payload : {session_id, passive_participant_id, silence_duration_ms,
               threshold_ms, share_deviation_ms}
    purpose : Orchestrator may display private nudge to passive participant's device

  speaking.open_round.fairness_snapshot (every 5 seconds):
    payload : {session_id, gini_coefficient, fairness_class,
               per_participant_share_class_map}
    purpose : Powers live fairness meter on facilitator or admin dashboard

  speaking.turn.completion_snapshot (immediately on turn end):
    payload : TOKEN_UTILISATION_RECORD (Section 4.3)
    purpose : TRANSCRIPT_AGGREGATION_AGENT receives turn data in real time,
              not waiting for session end
```

### 8.2 Open Discussion Round — Measurement-Under-Freedom Protocol

The open discussion round embodies the system's core principle: *Freedom exists only under measurement.*

```
ALL PARTICIPANTS UNMUTED SIMULTANEOUSLY.
NO TOKEN GRANTS. NO TURN ORDER.
FULL MEASUREMENT ACTIVE.

What is measured:
  ✓  Every MIC_OPENED event (who starts speaking and when)
  ✓  Speaking duration per utterance
  ✓  Inter-utterance gaps (silence between bursts)
  ✓  Interrupt pattern (rapid mic opens when others are speaking = interruption attempt)
  ✓  Dominance ratio (updated every 5 seconds)
  ✓  Gini coefficient (updated every 5 seconds)
  ✓  Total speaking time vs fair share

What is NOT done:
  ✗  No forced mute during open round (freedom is genuine)
  ✗  No content analysis
  ✗  No leadership labelling
  ✗  No personality inference

Interrupt Detection in Open Round:
  An "interrupt attempt" is detected when:
    Participant A's MIC_OPENED event occurs within 500ms of
    Participant B's mic being open AND while Participant B has
    been speaking continuously for >= 2000ms
  Result: increment interrupt_attempts[Participant_A]
          classify as INTERRUPT_ATTEMPT in event log
```

---

## SECTION 9 — SCALABILITY DESIGN

```yaml
EXPECTED_EVENT_RPS      : 50,000 mic state events/second
                          (10,000 concurrent sessions × 5 participants × 1 event/s avg)
LATENCY_TARGET          :
  Event processing      : < 50ms from event receipt to Redis update (p99)
  Turn completion snap  : < 200ms from turn end to TOKEN_UTILISATION_RECORD emit (p99)
  Session distribution  : < 1000ms from session end to SESSION_TIME_DISTRIBUTION emit (p95)
MAX_CONCURRENCY         : 10,000 concurrent session time-trackers
QUEUE_STRATEGY          :
  MIC_STATE_EVENTS      : Kafka topic — speaking.time.events
                          (partitioned by session_id for strict ordering per session)
  TURN_SNAPSHOTS        : Kafka topic — speaking.turn.completed
  SESSION_DISTRIBUTIONS : Kafka topic — speaking.session.completed

HORIZONTAL_SCALING      : Stateless workers per session partition — Kubernetes HPA
STATELESS_EXECUTION     : All running totals in Redis — no local state in workers
IDEMPOTENT_EVENTS       : webrtc_sequence_number used as idempotency key
                          duplicate events ignored if sequence already processed
SESSION_AFFINITY        :
  All events for a session_id route to the same Kafka partition
  (Kafka partition key = session_id)
  Ensures strict ordering of mic state events per session

REDIS_STRUCTURE         :
  speaking:session:{session_id}:participant:{participant_id}:
    state               : OPEN | CLOSED | NETWORK_DROP
    mic_open_start_ms   : integer
    speaking_ms:{round} : integer (per round running total)
    speaking_ms:total   : integer
    burst_count:{round} : integer
    silence_windows     : list
    last_seq            : integer (sequence number guard)
  TTL: session duration + 1 hour (cleanup after transcript assembly)

CACHING                 :
  FAIRNESS_SNAPSHOT     : Redis, updated every 5s, TTL = session + 30min
  SESSION_CONFIG        : Redis, loaded on session start, TTL = session duration
  BOT_VETO_LIST         : Redis, updated by AGVDA-001 in real time
```

---

## SECTION 10 — SECURITY ENFORCEMENT

```yaml
ZERO_TRUST_POLICY:
  - Every mic state event validated: session_id + participant_id + tenant_id + sequence
  - Sequence number guard prevents replay and injection attacks
  - No trust carried between events — each event re-validated independently
  - Clock drift > 5000ms → FLAG + alert + apply correction

TENANT_ISOLATION:
  - All Redis keys namespaced by tenant_id
  - All Kafka topics partitioned with tenant_id in message headers
  - No cross-tenant session data in any Redis key or Kafka message
  - Cross-tenant event detection → HALT + SECURITY_ALERT + ZERO_TRUST_AGENT

EVENT_SOURCE_VALIDATION:
  - Only accept events from JITSI_SESSION_LAYER, WEBRTC_TRANSPORT_LAYER,
    VOICE_GD_ORCHESTRATOR_AGENT (forced mute/unmute)
  - Reject events from any other source — LOG + alert ZERO_TRUST_AGENT

BOT_VETO_ENFORCEMENT:
  - BOT_CONFIRMED participants' events are ACCEPTED and LOGGED
  - Their time records are FLAGGED as BOT_VETOED
  - Their time is EXCLUDED from all fairness calculations
  - Their time is EXCLUDED from score_inputs for scoring
  - Their time IS INCLUDED in session_totals with bot_vetoed flag
  - This ensures full audit trail while protecting session integrity

PII_POLICY:
  - participant_id (UUID) used throughout — no names stored
  - AI layer receives only role labels and anonymised participant IDs (P1, P2...)
  - Speaking time records do not contain voice content

ENCRYPTION:
  - All SESSION_TIME_DISTRIBUTION records encrypted at rest (AES-256)
  - All Kafka messages encrypted in transit (TLS 1.3)
  - Redis keys encrypted at rest (HashiCorp Vault-managed)
  - All time records HMAC-signed before audit write

ACCESS_CONTROL:
  READ_REAL_TIME      : VOICE_GD_ORCHESTRATOR_AGENT only
  READ_COMPLETED      : SCORING_ENGINE, TRANSCRIPT_AGGREGATION_AGENT,
                        INTELLIGENCE_SCORING_ML_AGENT, GD_POST_SESSION_ANALYTICS_AGENT,
                        PARENT_LLM_INSIGHT_NARRATIVE_AGENT, AUDIT_COMPLIANCE_AGENT
  WRITE               : This agent only
  ADMIN_READ          : ADMIN_GOVERNANCE_SERVICE (with access log)
```

---

## SECTION 11 — AUDIT & TRACEABILITY

Every turn completion and session distribution writes an immutable audit record:

```json
AUDIT_RECORD: {
  "timestamp_utc"              : "ISO-8601",
  "actor_id"                   : "PHONE_SPEAKING_TIME_AGENT",
  "agent_version"              : "AGPSTA-005-v1.0.0",
  "operation_type"             : "TURN_SNAPSHOT | SESSION_DISTRIBUTION |
                                  DOMINANCE_ALERT | SILENCE_ALERT |
                                  FAIRNESS_SNAPSHOT | BOT_VETO_APPLIED |
                                  ANOMALY_DETECTED | SEQUENCE_ANOMALY | CLOCK_ANOMALY",
  "session_id"                 : "UUID",
  "participant_id"             : "UUID | null (null for session-level records)",
  "tenant_id"                  : "UUID",
  "turn_id"                    : "UUID | null",
  "round_type"                 : "string | null",
  "input_event_count"          : "integer (mic events processed for this record)",
  "input_hash"                 : "SHA-256 of mic event stream for this turn",
  "output_hash"                : "SHA-256 of produced record",
  "speaking_ms_recorded"       : "integer",
  "utilisation_pct"            : "float | null",
  "anomaly_score"              : "float | null",
  "anomaly_class"              : "string | null",
  "bot_veto_applied"           : "boolean",
  "clock_anomaly_flag"         : "boolean",
  "sequence_anomaly_flag"      : "boolean",
  "fairness_gini_at_record"    : "float | null",
  "intelligence_signals_emitted": "boolean",
  "ai_assist_used"             : "boolean",
  "ai_prompt_version"          : "string | null",
  "model_version"              : "AGPSTA-005-v1.0.0",
  "operation_duration_ms"      : "integer"
}
```

**Audit store**: `IMMUTABLE_ARCHIVE_SERVICE` — append-only, WORM-style
**Retention**: 7 years (aligns with transcript retention policy)
**Integrity**: Every audit record HMAC-signed; tamper detection on read

---

## SECTION 12 — FAILURE POLICY

```yaml
INVALID_MIC_STATE_EVENT (validation failure):
  action      : REJECT + LOG with specific failure reason
  stop_exec   : NO (reject the event, continue processing others)
  log_to      : AUDIT_COMPLIANCE_AGENT
  escalate_to : VOICE_GD_ORCHESTRATOR_AGENT (notify event rejected)
  note        : Session continues; rejected event is a gap in the record

OUT_OF_ORDER_EVENTS (sequence anomaly):
  action      : Buffer for 100ms, attempt reorder
                After 100ms: apply best-effort + SEQUENCE_ANOMALY flag
  stop_exec   : NO
  log_to      : AUDIT record (sequence_anomaly_flag = true)
  escalate_to : OBSERVABILITY_AGENT (metric increment)

CLOCK_DRIFT_DETECTED (> 5000ms):
  action      : Apply NTP correction + CLOCK_ANOMALY flag
  stop_exec   : NO
  log_to      : AUDIT record + OBSERVABILITY_AGENT
  escalate_to : FORENSICS_AGENT if drift > 30000ms (potential manipulation)

REDIS_UNAVAILABLE:
  action      : HALT event processing for this session
  stop_exec   : YES for affected session
  log_to      : OBSERVABILITY_AGENT
  escalate_to : VOICE_GD_ORCHESTRATOR_AGENT (session may need to pause)
  retry       : Reconnect with 500ms exponential backoff (max 3 retries)
               After 3 failures → HALT + escalate to ADMIN_GOVERNANCE_SERVICE

SESSION_CONFIG_MISSING (on first event arrival):
  action      : Request config from VOICE_GD_ORCHESTRATOR_AGENT
  stop_exec   : YES (buffer events for 2000ms awaiting config)
  log_to      : OBSERVABILITY_AGENT
  retry       : Once after 1000ms
               After retry failure → apply DEFAULT_CONFIG + FLAG

TURN_SNAPSHOT_COMPUTE_FAILURE:
  action      : Emit partial snapshot with INCOMPLETE flag
  stop_exec   : NO (partial data better than no data for transcript)
  log_to      : AUDIT_COMPLIANCE_AGENT (INCOMPLETE flag)
  escalate_to : TRANSCRIPT_AGGREGATION_AGENT (notify of incomplete turn data)

BOT_VETO_RECEIVED_MID_SESSION:
  action      : Immediately update BOT_VETO_LIST in Redis
                Apply retroactive BOT_VETOED flag to all prior time records for this participant
                Exclude from all future fairness calculations
                Notify SCORING_ENGINE of score_input revision
  stop_exec   : NO
  log_to      : AUDIT record per affected turn

CROSS_TENANT_EVENT_DETECTED:
  action      : HALT + SECURITY_ALERT
  stop_exec   : YES
  log_to      : ZERO_TRUST_AGENT + AUDIT_COMPLIANCE_AGENT
  escalate_to : ADMIN_GOVERNANCE_SERVICE

NO_SILENT_FAILURES: ENFORCED — every failure path produces a structured log record
```

---

## SECTION 13 — INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  VOICE_GD_ORCHESTRATOR_AGENT        : session config, token grants/revokes, turn events
  JITSI_SESSION_LAYER                : mute/unmute state change events
  WEBRTC_TRANSPORT_LAYER             : network telemetry per participant
  PHONE_BOT_VOICE_DETECTION_AGENT    : BOT_CONFIRMED / BOT_SUSPECTED flags
  AUTH_SERVICE                       : participant identity binding
  GD_ATTENDANCE_TRACKING_AGENT       : presence classification
  DOJO_SESSION_ORCHESTRATION_AGENT   : role assignments for Dojo GD sessions

DOWNSTREAM_AGENTS:
  SCORING_ENGINE                     : score_inputs (mandatory — agent cannot score without this)
  TRANSCRIPT_AGGREGATION_AGENT       : turn-level time data per participant
  INTELLIGENCE_SCORING_ML_AGENT      : intelligence_signals per participant
  PASSIVE_INTELLIGENCE_ENGINE        : behavioral speaking feature vectors
  GD_POST_SESSION_ANALYTICS_AGENT   : SESSION_TIME_DISTRIBUTION for analytics
  PARENT_LLM_INSIGHT_NARRATIVE_AGENT : speaking time summary for parent reports
  PASSPORT_AGGREGATION_AGENT         : communication skill signals
  VOICE_GD_ORCHESTRATOR_AGENT        : real-time enforcement events (dominance, silence)
  OBSERVABILITY_AGENT                : performance and session health metrics
  FEATURE_STORE_SERVICE              : emitted speaking feature vectors
  AUDIT_COMPLIANCE_AGENT             : immutable time audit records

EVENT_TRIGGERS:
  CONSUMED:
    - speaking.time.events                    (Kafka — mic state event stream)
    - gd.turn.token_granted                   (from VOICE_GD_ORCHESTRATOR)
    - gd.turn.token_revoked                   (from VOICE_GD_ORCHESTRATOR)
    - gd.session.started                      (session config load trigger)
    - gd.session.completed                    (session distribution trigger)
    - voice.bot.detection_result              (from PHONE_BOT_VOICE_DETECTION_AGENT)
    - gd.open_round.started                   (enables fairness engine)
    - gd.open_round.ended                     (disables fairness engine, final snapshot)

  EMITTED:
    - speaking.token.utilisation_update       (real-time, per active token)
    - speaking.silence.detected               (real-time, to VOICE_GD_ORCHESTRATOR)
    - speaking.open_round.dominance_alert     (real-time, to VOICE_GD_ORCHESTRATOR)
    - speaking.open_round.passive_alert       (real-time, to VOICE_GD_ORCHESTRATOR)
    - speaking.open_round.fairness_snapshot   (every 5s during open round)
    - speaking.turn.completion_snapshot       (per turn end)
    - speaking.session.distribution_complete  (session end)
    - speaking.anomaly.detected               (to PHONE_BOT_VOICE_DETECTION_AGENT)
    - security.cross_tenant_violation         (to ZERO_TRUST_AGENT)
```

---

## SECTION 14 — PASSIVE INTELLIGENCE COMPATIBILITY

This agent emits structured feature vectors to the Feature Store after every session.

```json
EMIT_FEATURE_VECTOR (per participant, per session): {
  "user_id"          : "UUID",
  "session_id"       : "UUID",
  "session_type"     : "GD | DOJO_GD | INTERVIEW",
  "feature_name"     : "gd_speaking_time_profile",
  "feature_value"    : 0.82,
  "feature_set"      : {
    "avg_token_utilisation_pct"       : 85.2,
    "open_round_share_class"          : "AT_FAIR",
    "dominant_flag"                   : false,
    "silence_ratio_token_rounds"      : 0.08,
    "speech_start_latency_mean_ms"    : 580,
    "burst_frequency_per_minute"      : 3.2,
    "interrupt_attempts"              : 1,
    "turns_completed_ratio"           : 1.0,
    "gini_contribution"               : 0.18,
    "role_time_compliance"            : 0.91,
    "interpersonal_signal"            : 0.78,
    "linguistic_signal"               : 0.84,
    "logical_signal"                  : 0.71
  },
  "timestamp"        : "ISO-8601",
  "source_agent"     : "PHONE_SPEAKING_TIME_AGENT"
}
```

These vectors feed:
- `PASSIVE_INTELLIGENCE_ENGINE` — continuous intelligence profiling without explicit tests
- `INTELLIGENCE_GROWTH_TIME_SERIES_AGENT` — longitudinal speaking growth tracking
- `BEHAVIOR_ANALYTICS_AGENT` — platform-wide communication skill analytics
- `CAREER_PROBABILITY_MODEL` — speaking behavior as career outcome predictor
- `SKILL_RANK_ENGINE` — communication skill dimension ranking
- `POPULATION_PERCENTILE_ENGINE` — student's speaking profile vs platform population

---

## SECTION 15 — GROWTH ENGINE HOOK

```yaml
ON session.distribution_complete WITH session_integrity = CLEAN:
  EMIT:
    XP_UPDATE_EVENT:
      base_xp = speaking_participation_score (from score_inputs)
      bonus_xp if role_time_compliance >= 0.85 (Dojo GD — role mastery bonus)
      bonus_xp if gini_contribution <= 0.15 (equitable participant bonus)

    RANK_UPDATE_EVENT:
      feeds SKILL_RANK_ENGINE for:
        - Communication skill dimension (all session types)
        - Interpersonal skill dimension (GD, Dojo GD)
        - Linguistic skill dimension (GD, Dojo GD)

    SHARE_TRIGGER_EVENT:
      if participant's total_utilisation_pct >= 90% AND all_turns_completed = true:
        emit achievement event: "Full Participation" badge eligible
      if open_round_share_class = AT_FAIR AND gini_contribution <= 0.15:
        emit achievement: "Balanced Contributor" badge eligible

ON session.distribution_complete WITH bot_confirmed_count > 0:
  - XP_UPDATE_EVENT: BLOCKED for bot_confirmed participants
  - RANK_UPDATE_EVENT: BLOCKED for bot_confirmed participants
  - Fair share recalculated excluding bot_confirmed participants before any XP emission
```

---

## SECTION 16 — PARENT REPORT INTEGRATION

This agent produces a structured speaking time summary specifically formatted for `PARENT_LLM_INSIGHT_NARRATIVE_AGENT` to generate age-appropriate, meaningful parent reports.

```json
PARENT_REPORT_SPEAKING_SUMMARY: {
  "session_id"                : "UUID",
  "participant_id_hash"       : "SHA-256 (not raw UUID — parent sees child, not system ID)",
  "session_date_utc"          : "ISO-8601",
  "session_type_label"        : "Group Discussion | Dojo Discussion | Interview Practice",
  "role_assigned"             : "Moderator | Speaker | ... | Standard Participant",
  "participation_summary"     : {
    "turns_completed_of_total"    : "e.g., 4 of 4",
    "average_time_used_pct"       : "e.g., 87%",
    "overall_participation_class" : "EXCELLENT | GOOD | MODERATE | NEEDS_IMPROVEMENT",
    "open_round_share_class"      : "ABOVE_FAIR | AT_FAIR | BELOW_FAIR | PASSIVE",
    "was_dominant"                : "boolean",
    "interrupt_attempts"          : "integer",
    "role_time_compliance_label"  : "Followed role well | Partially | Did not follow role"
  },
  "intelligence_signals"      : {
    "interpersonal_growth"    : "IMPROVING | STABLE | DECLINING",
    "linguistic_engagement"   : "HIGH | MEDIUM | LOW",
    "logical_structure"       : "HIGH | MEDIUM | LOW"
  },
  "highlight"                 : "string — single most positive behavioral observation",
  "growth_area"               : "string — single most impactful improvement opportunity"
}
```

The AI layer generates a natural language narrative from this structured summary. No PII beyond the summary structure is passed to the AI layer.

---

## SECTION 17 — PERFORMANCE MONITORING

```yaml
METRICS (via Prometheus → Grafana):

  event_processing_latency_p99_ms        : Target < 50ms
  turn_snapshot_latency_p99_ms           : Target < 200ms
  session_distribution_latency_p95_ms    : Target < 1000ms
  event_rejection_rate_pct               : Rejected events / total events
  sequence_anomaly_rate_pct              : Events with sequence flags
  clock_anomaly_rate_pct                 : Events with clock drift flags
  bot_veto_applied_per_session_avg       : Average bot vetoes per session
  dominance_alert_rate_per_session_avg   : Average dominance alerts per session
  gini_coefficient_avg_by_session_type   : Fairness health by session type
  token_utilisation_mean_pct             : Platform-wide mean participation
  open_round_passive_rate_pct            : Rate of passive participants in open rounds
  redis_operation_latency_p99_ms         : Target < 10ms
  kafka_consumer_lag_speaking_events     : Target < 500 messages
  intelligence_signal_emit_success_rate  : % of sessions with signals successfully emitted

DASHBOARDS (Grafana):
  - Real-Time Session Speaking Health (live sessions, per tenant)
  - Token Utilisation Distribution (by session type, by round type)
  - Open Round Fairness Distribution (Gini coefficient histogram)
  - Dominance Event Frequency (by session, by tenant)
  - Intelligence Signal Distribution (interpersonal, linguistic, logical)
  - Bot Veto Impact on Fairness (pre/post veto session Gini)
  - Parent Report Quality Metrics (narrative generation success rate)
  - Speaking Time Anomaly Monitor (anomaly score distribution)

ALERTS:
  - event_processing_latency_p99 > 100ms  → ALERT OBSERVABILITY_AGENT
  - kafka_consumer_lag > 5000 messages    → SCALE UP workers immediately
  - redis_operation_latency_p99 > 50ms    → ALERT infrastructure team
  - gini_coefficient_avg > 0.6 (24h avg) → ALERT GD_POST_SESSION_ANALYTICS_AGENT
    (systematic session design issue if sustained)
  - cross_tenant_violation_count > 0      → CRITICAL PAGE + HALT
  - intelligence_signal_emit_success_rate < 99% → ALERT INTELLIGENCE_SCORING_ML_AGENT
```

---

## SECTION 18 — VERSIONING POLICY

```yaml
AGENT_VERSION_FORMAT       : AGPSTA-005-vMAJOR.MINOR.PATCH
MUTATION_POLICY            : Add-only — no field removal from any output schema
BACKWARD_COMPAT            : All prior SESSION_TIME_DISTRIBUTION schemas remain readable
ROLLBACK_SAFETY            : Previous version retained 90 days post-rollout
MIGRATION_DOC              : Required for every MINOR or MAJOR version bump
CHANGELOG                  : Append-only CHANGELOG.md co-versioned with this spec
ML_MODEL_VERSION           : Versioned separately (AGPSTA-ML-v{intelligence_dim}-vX.Y.Z)
AI_PROMPT_VERSION          : AGPSTA-AI-PROMPT-vX.Y.Z
SCHEMA_REGISTRY            : All output schemas registered in
                             ECOSKILLER_ANTIGRAVITY_SCHEMA_COMPATIBILITY_AGENT
SELF_VERSION_IN_OUTPUTS    : model_version field mandatory in every
                             TOKEN_UTILISATION_RECORD, SESSION_TIME_DISTRIBUTION,
                             and AUDIT_RECORD
```

---

## SECTION 19 — NON-NEGOTIABLE RULES

This agent **MUST NOT**:

```
✗  Store or access raw audio at any layer under any circumstance
✗  Infer speech content, tone, emotion, accent, or personality from time data
✗  Label participants as "leaders", "confident", or any subjective trait
   (the platform explicitly avoids personality traits and leadership labels)
✗  Force mute or unmute any participant — only VOICE_GD_ORCHESTRATOR can do this
✗  Grant or revoke speaking tokens — only VOICE_GD_ORCHESTRATOR can do this
✗  Block a dominant participant during open discussion — dominance is penalised, not blocked
✗  Modify a committed TOKEN_UTILISATION_RECORD after it has been written to audit store
✗  Exclude a network drop window from speaking time without NETWORK_DROP classification
   (network drops must never be counted as silence — they are operationally distinct)
✗  Include BOT_CONFIRMED participant time in fairness calculations
✗  Exclude BOT_CONFIRMED participant time from audit logs
   (log everything — exclude only from fairness and score inputs)
✗  Process events from participants not in the session roster
✗  Serve real-time data to any agent not in the AUTHORIZED_REAL_TIME_CONSUMERS list
✗  Emit intelligence signals without model_version tag
✗  Allow the AI layer to modify any structured time or signal field
✗  Operate without a loaded SESSION_SPEAKING_CONFIG
✗  Mix time records from different sessions or tenants
✗  Count a MIC_OPENED event as speaking time without a subsequent MIC_CLOSED interval
   (open-ended intervals must be resolved at session end with best-effort close)
✗  Emit a SESSION_TIME_DISTRIBUTION without audit_reference
✗  Generate parent report content with participant PII
```

---

## SECTION 20 — DEPLOYMENT CONFIGURATION

```yaml
KUBERNETES_NAMESPACE    : realtime
SERVICE_TYPE            : Stateless microservice (Deployment)
REPLICA_MIN             : 5
REPLICA_MAX             : 200 (HPA on Kafka consumer lag for speaking.time.events)
HPA_TRIGGER             : Kafka consumer lag > 1000 messages
RESOURCE_REQUEST        : 250m CPU, 256Mi RAM (lightweight — primary logic is counter ops)
RESOURCE_LIMIT          : 1000m CPU, 1Gi RAM
LIVENESS_PROBE          : /health/live
READINESS_PROBE         : /health/ready (checks Redis + Kafka connectivity)
STARTUP_PROBE           : 10s grace

KAFKA_TOPIC_IN          :
  - speaking.time.events            (partitioned by session_id)
KAFKA_TOPIC_OUT         :
  - speaking.token.utilisation_update
  - speaking.silence.detected
  - speaking.open_round.dominance_alert
  - speaking.open_round.passive_alert
  - speaking.open_round.fairness_snapshot
  - speaking.turn.completion_snapshot
  - speaking.session.distribution_complete
  - speaking.anomaly.detected
  - security.cross_tenant_violation

REDIS_USAGE             :
  - Per-participant per-session time accumulators (see Section 9)
  - Bot veto list per session
  - Fairness snapshots (every 5s)
  - Session config cache
  - Sequence number guard per participant

SECRETS_MANAGED_BY      : HashiCorp Vault
CONFIG_MANAGED_BY       : Kubernetes ConfigMap (versioned, includes round structure defaults)
FEATURE_FLAGS           : Unleash
  - speaking.dominance_enforcement.enabled (per-tenant toggle for real-time alerts)
  - speaking.role_profiles.enabled         (Dojo GD role profiles — per-session type)
  - speaking.parent_report.enabled         (parent report generation per tenant)

ENVIRONMENTS            :
  - dev     : Simulated mic events, no Redis persistence, mock intelligence signals
  - staging : Full pipeline with real Jitsi test sessions
  - prod    : Full stack, Vault secrets, immutable audit, 7-year retention
```

---

## SECTION 21 — INTEGRATION ARCHITECTURE MAP

```
JITSI/WEBRTC TRANSPORT LAYER
  │ MIC_OPENED / MIC_CLOSED / NETWORK_DROP events (WebSocket + Kafka)
  ▼
PHONE_SPEAKING_TIME_AGENT (this agent — AGPSTA-005)
  │
  ├─── REAL-TIME PIPELINE (< 50ms per event) ────────────────────────────────────┐
  │    STEP 1  Clock normalisation (NTP drift correction)                         │
  │    STEP 2  Sequence validation (monotonic guard)                              │
  │    STEP 3  State machine update (OPEN→CLOSED→interval compute)                │
  │    STEP 4  Interval validation (< 0ms reject; overflow cap; phantom filter)   │
  │    STEP 5  Classification (MICRO | SHORT | NORMAL | EXTENDED)                 │
  │    STEP 6  Redis atomic counter update (speaking_ms per round per participant) │
  │    STEP 7  Emit real-time event → VOICE_GD_ORCHESTRATOR_AGENT                │
  │                                                                               │
  │    OPEN ROUND FAIRNESS ENGINE (every 5s during open round)                   │
  │    → Fair share computation → Gini coefficient → Dominance ratio              │
  │    → DOMINANCE_ALERT if threshold breached → VOICE_GD_ORCHESTRATOR_AGENT     │
  └────────────────────────────────────────────────────────────────────────────────┘
  │
  ├─── TURN COMPLETION SNAPSHOT (< 200ms per turn end) ──────────────────────────┐
  │    TOKEN_UTILISATION_RECORD assembled from Redis counters                     │
  │    → TRANSCRIPT_AGGREGATION_AGENT (AGTAA-002)                                │
  │    → SCORING_ENGINE (score_inputs per turn)                                   │
  │    → AUDIT_COMPLIANCE_AGENT (immutable audit record)                         │
  └────────────────────────────────────────────────────────────────────────────────┘
  │
  └─── SESSION DISTRIBUTION (< 1000ms after session.completed) ──────────────────┐
       SESSION_TIME_DISTRIBUTION assembled from all turn records + open round     │
       ML layer → Intelligence signals derived (interpersonal, linguistic, logical)│
       → SCORING_ENGINE (final score_inputs)                                      │
       → INTELLIGENCE_SCORING_ML_AGENT (intelligence feature vectors)             │
       → PARENT_LLM_INSIGHT_NARRATIVE_AGENT (parent report summary)              │
       → PASSPORT_AGGREGATION_AGENT (communication skill update)                  │
       → FEATURE_STORE_SERVICE (speaking time feature vectors)                    │
       → GD_POST_SESSION_ANALYTICS_AGENT (analytics payload)                     │
       → IMMUTABLE_ARCHIVE_SERVICE (WORM storage — 7 years)                      │
       └──────────────────────────────────────────────────────────────────────────┘
```

---

## SECTION 22 — ANTI-PATTERNS EXPLICITLY PROHIBITED

| Anti-Pattern | Why Prohibited |
|---|---|
| Counting network drop time as silence | Network drops are operationally neutral — silence is behavioral; conflating them produces unfair scores |
| Blocking dominant participants during open round | Freedom exists only under measurement — dominance is penalised in score, never blocked in session |
| Labelling participants with subjective traits | The platform's core design philosophy explicitly prohibits confidence scoring, personality labels, and leadership inference |
| Using raw audio for any measurement | Privacy-by-architecture — all measurement is at the mic-state event level only |
| Excluding BOT_CONFIRMED events from audit log | Audit completeness is non-negotiable — bot veto excludes from scores and fairness, not from logs |
| Serving real-time data to SCORING_ENGINE | Scoring is post-session — real-time stream only to VOICE_GD_ORCHESTRATOR |
| Computing intelligence signals without model_version | Untraceable signal — intelligence profile becomes unreproducible |
| Emitting XP directly from this agent | XP emission is GROWTH_ENGINE's responsibility — this agent emits events only |
| Flat fair-share comparison across all roles | Moderator and Speaker have different time expectations — role profiles must be applied |
| Retroactive modification of TOKEN_UTILISATION_RECORD | Committed turn records are immutable — disputes handled via TRANSCRIPT_VERSIONING_AGENT |
| Open-ended MIC_OPENED without close at session end | Must resolve all open intervals at session end with best-effort close and OPEN_ENDED flag |
| Missing sequence guard on event processing | Allows replay injection to inflate speaking time |
| Cross-session time accumulation | Redis key namespacing by session_id is mandatory — no global accumulators |

---

## SECTION 23 — FINAL LOCK DECLARATION

```
╔══════════════════════════════════════════════════════════════════════════════════════════════╗
║                          AGENT SPECIFICATION — SEALED                                        ║
║                                                                                              ║
║  This document is the authoritative, governing specification for the                        ║
║  PHONE_SPEAKING_TIME_AGENT operating within the Ecoskiller Antigravity platform.            ║
║                                                                                              ║
║  Any deviation, abbreviation, re-interpretation, or undocumented extension of this          ║
║  specification constitutes a compliance violation and MUST be escalated to                  ║
║  ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT before any implementation proceeds.               ║
║                                                                                              ║
║  AGENT_ID          : AGPSTA-005                                                              ║
║  VERSION           : v1.0.0                                                                  ║
║  SESSION_TYPES     : GD | DOJO_GD | INTERVIEW | INTELLIGENCE_TEST                           ║
║  INTELLIGENCE_DIMS : Interpersonal · Linguistic · Logical                                   ║
║  STORES            : Redis (real-time) · Kafka (events) · WORM (final records)              ║
║  SEALED BY         : ECOSKILLER INTELLIGENCE & INNOVATION CORE                              ║
║  GOVERNANCE        : ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT                               ║
║  SESSION_AUTHORITY : VOICE_GD_ORCHESTRATOR_AGENT                                            ║
║  SCORING_AUTHORITY : SCORING_ENGINE                                                          ║
║  MUTATION LOCK     : Add-only — version bump required for any change                        ║
║  STATUS            : ACTIVE · PRODUCTION-READY                                              ║
╚══════════════════════════════════════════════════════════════════════════════════════════════╝
```
