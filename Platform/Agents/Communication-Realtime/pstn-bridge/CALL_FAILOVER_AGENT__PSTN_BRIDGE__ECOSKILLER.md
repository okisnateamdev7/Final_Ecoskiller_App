# 🔒 CALL_FAILOVER_AGENT — PSTN & BRIDGE LAYER
## ECOSKILLER UNIFIED SAAS PLATFORM
### Antigravity Edition — Sealed Production Prompt

---

```
Artifact Class:     Production Agent Prompt
System Domain:      Voice Communication Infrastructure
Layer:              PSTN / SIP Bridge / WebRTC Failover
Agent Name:         CALL_FAILOVER_AGENT
Status:             FINAL · LOCKED · SEALED · GOVERNED
Mutation Policy:    Add-only via version bump
Version:            v1.0-ANTIGRAVITY
Interpretation Authority:  NONE
Execution Authority:       Human declaration only
Antigravity Mode:          ENABLED — No call dropped, no voice lost
```

---

## ░░ AGENT IDENTITY ░░

```
Agent ID:           CALL_FAILOVER_AGENT
Full Name:          Call Failover & PSTN Bridge Intelligence Agent
Parent System:      ECOSKILLER — Voice GD Orchestrator (Service #7)
Scope:              All real-time voice sessions — GD, Dojo, Interviews
Operating Layer:    PSTN ↔ SIP ↔ WebRTC ↔ Jitsi ↔ LiveKit ↔ coturn
Primary Mission:    Zero call drop. Zero silent failure. Zero dead session.
```

---

## ░░ SECTION 1 — PURPOSE & AUTHORITY ░░

### 1.1 Mission Statement

```
The CALL_FAILOVER_AGENT governs the continuity of all real-time
voice and media sessions across the Ecoskiller platform.

It is the last line of defense between a live Group Discussion,
Dojo Match, or Interview session and total communication failure.

When every other layer fails — network, WebRTC, Jitsi, coturn —
this agent maintains session continuity, re-routes through PSTN
or SIP bridge, preserves scoring state, logs all events immutably,
and ensures no participant is silently dropped without a recovery path.
```

### 1.2 Authority Chain

```
CALL_FAILOVER_AGENT reports to:    Voice GD Orchestrator (Service #7)
Controlled by:                     Backend State Machine (Redis)
Monitored by:                      Prometheus + Grafana + Loki
Audited by:                        Immutable Audit Log (PostgreSQL)
Governed by:                       Admin Governance Service (Service #14)
```

### 1.3 Scope Boundary

```
IN SCOPE:
  ✔ WebRTC transport failure detection
  ✔ Jitsi SFU room crash handling
  ✔ coturn STUN/TURN failover
  ✔ PSTN bridge dial-in fallback
  ✔ SIP trunk rerouting
  ✔ Session state preservation during failover
  ✔ Participant reconnect window management
  ✔ Score state freeze and restore
  ✔ GD turn-engine pause and resume
  ✔ Dojo match hold and continuation
  ✔ Failover event audit logging
  ✔ Notification dispatch on failover

OUT OF SCOPE:
  ✗ Content evaluation or scoring decisions
  ✗ Belt or certification triggers
  ✗ Billing disputes
  ✗ AI or ML scoring inference
  ✗ Direct media processing (audio analysis)
  ✗ DNS or SSL configuration
```

---

## ░░ SECTION 2 — SYSTEM ARCHITECTURE POSITION ░░

### 2.1 Layer Map

```
┌──────────────────────────────────────────────────────────┐
│                  CANDIDATE / PARTICIPANT                 │
│              (Browser / Flutter App / PSTN)              │
└──────────────┬───────────────────────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────────────────────┐
│              KONG API GATEWAY + NGINX INGRESS            │
│              (ModSecurity WAF, Rate Limiting)            │
└──────────────┬───────────────────────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────────────────────┐
│           VOICE GD ORCHESTRATOR (Service #7)             │
│     Node.js / Spring Boot + Redis State Machine          │
│     WebSocket Command Channel — mute/unmute/timer        │
└──────────────┬───────────────────────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────────────────────┐
│             ██  CALL_FAILOVER_AGENT  ██                  │
│                                                          │
│   Detection │ Decision │ Rerouting │ Logging │ Recovery  │
└──────┬───────────────────────────┬──────────────────┬────┘
       │                           │                  │
       ▼                           ▼                  ▼
┌────────────┐           ┌─────────────────┐  ┌─────────────────┐
│  WebRTC    │           │  PSTN Bridge    │  │  SIP Trunk      │
│  + Jitsi  │           │  (Dial-In API)  │  │  (SIP Provider) │
│  + coturn  │           │                 │  │                 │
└────────────┘           └─────────────────┘  └─────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────────────┐
│        REDIS (State Machine) + PostgreSQL (Audit)        │
│        Prometheus (Metrics) + Loki (Logs)                │
└──────────────────────────────────────────────────────────┘
```

### 2.2 Transport Hierarchy (Priority Order)

```
PRIORITY 1 — WebRTC via Jitsi SFU
  Primary path. Browser-native. Encrypted (SRTP).
  Used when full connectivity confirmed.

PRIORITY 2 — WebRTC via coturn TURN relay
  Activated when direct peer connection fails.
  NAT traversal via TURN server.
  Higher latency — acceptable fallback.

PRIORITY 3 — SIP Bridge
  Activated when WebRTC path unstable or repeatedly failing.
  Session migrated to SIP trunk provider.
  Audio continues. UI retains control commands via WebSocket.

PRIORITY 4 — PSTN Bridge (Dial-In)
  Last resort. Activated on complete internet failure.
  Participant receives outbound call to registered phone number.
  Audio-only. Session state preserved in Redis.
  Scoring continues. Turn engine paused during transition.

PRIORITY 5 — Session Hold + Async Resume
  Activated when all audio paths fail.
  Session enters HOLD state.
  Re-entry window: configurable (default 120 seconds).
  If no reconnect within window → session terminated with audit log.
```

---

## ░░ SECTION 3 — FAILURE TAXONOMY ░░

### 3.1 Failure Classes

```
CLASS F1 — NETWORK DROPOUT (Participant-side)
  Definition:  Participant loses internet connectivity
  Detection:   WebSocket heartbeat timeout (threshold: 5s)
  Severity:    MEDIUM
  Action:      Hold turn. Attempt reconnect window. Log event.

CLASS F2 — WEBRTC ICE FAILURE
  Definition:  ICE negotiation fails or STUN unreachable
  Detection:   ICE connection state = failed
  Severity:    MEDIUM
  Action:      Force coturn TURN relay. Renegotiate.

CLASS F3 — JITSI ROOM CRASH
  Definition:  Jitsi Videobridge or Jicofo process failure
  Detection:   Room health API returns non-200 / timeout
  Severity:    HIGH
  Action:      Freeze session state. Attempt room recreation.
               If recreation fails → SIP bridge activation.

CLASS F4 — COTURN SERVER FAILURE
  Definition:  TURN relay unavailable
  Detection:   TURN health check failure
  Severity:    HIGH
  Action:      Immediate SIP bridge activation.

CLASS F5 — TOTAL MEDIA STACK FAILURE
  Definition:  WebRTC + Jitsi + coturn all unavailable
  Detection:   All health checks negative simultaneously
  Severity:    CRITICAL
  Action:      PSTN bridge activation for all participants.
               Session state preserved. Scoring frozen.

CLASS F6 — PARTICIPANT SILENT DROP
  Definition:  Participant audio gone but session still open
  Detection:   Mic open duration metric = 0 for > 10s during active turn
  Severity:    LOW-MEDIUM
  Action:      Log silence event. Skip turn if silence > threshold.
               Notify via WebSocket.

CLASS F7 — DUPLICATE SESSION COLLISION
  Definition:  Same participant joined in two rooms simultaneously
  Detection:   Redis participant_session_map conflict check
  Severity:    HIGH
  Action:      Terminate older session. Preserve latest.
               Audit both sessions.

CLASS F8 — BRIDGE PROVIDER TIMEOUT
  Definition:  PSTN or SIP provider API unresponsive
  Detection:   Outbound call API timeout > 8s
  Severity:    HIGH
  Action:      Retry once. If second failure → session HOLD.
               Notify participant via push + SMS.

CLASS F9 — SESSION STATE CORRUPTION
  Definition:  Redis state machine key corrupted or expired
  Detection:   State machine read returns invalid schema
  Severity:    CRITICAL
  Action:      Reconstruct state from PostgreSQL audit log.
               Emit state_recovery_completed event.

CLASS F10 — CLOCK DRIFT / TIMER DESYNC
  Definition:  Turn timer out of sync between participants
  Detection:   Frontend timer vs Redis timer delta > 2s
  Severity:    MEDIUM
  Action:      Force timer resync via WebSocket push.
               Log desync event.
```

---

## ░░ SECTION 4 — ANTIGRAVITY FAILOVER DECISION ENGINE ░░

### 4.1 Core Algorithm — Antigravity Protocol

```
ANTIGRAVITY MODE:
  Definition: No call falls without a catch.
              Every failure has a pre-defined landing path.
              No failure is handled with discretion.
              All paths are deterministic.

ALGORITHM:

on_failure_event(failure_class, session_id, participant_id):

  1. FREEZE_SESSION_STATE(session_id)
     → snapshot Redis state to PostgreSQL
     → timestamp: failover_initiated_at

  2. EVALUATE_FAILURE_CLASS(failure_class)
     → select recovery_path from FAILOVER_PRIORITY_TABLE

  3. EXECUTE_RECOVERY_PATH(recovery_path)
     → if RETRY_WEBRTC:
         attempt_reconnect(timeout=10s)
         if success → RESTORE_SESSION_STATE → RESUME_TURN_ENGINE
         if fail    → escalate to next priority

     → if ACTIVATE_TURN_RELAY:
         force_coturn_path(participant_id)
         if success → RESTORE_SESSION_STATE → RESUME_TURN_ENGINE
         if fail    → escalate to next priority

     → if ACTIVATE_SIP_BRIDGE:
         acquire_sip_token(session_id, participant_id)
         dial_participant_via_sip(participant_phone)
         if success → notify_participant_via_websocket → RESUME_TURN_ENGINE
         if fail    → escalate to next priority

     → if ACTIVATE_PSTN_BRIDGE:
         acquire_pstn_token(session_id, participant_id)
         outbound_call(participant_phone)
         play_ivr_prompt("You are rejoining session: {session_id}")
         if answered → bind_audio_to_session → RESUME_TURN_ENGINE
         if no answer within 30s → SESSION_HOLD

     → if SESSION_HOLD:
         set_session_state(HOLD)
         start_reentry_timer(120s)
         notify_all_participants(hold_message)
         await participant_reconnect OR timer_expiry

     → if TIMER_EXPIRED:
         terminate_session(reason=FAILOVER_EXHAUSTED)
         generate_partial_score(for_present_participants)
         emit gd.session.terminated.failover
         write IMMUTABLE_AUDIT_RECORD

  4. LOG_ALL_STEPS(immutable=true, audit_table=session_failover_log)

  5. EMIT_EVENT(failover.completed | failover.failed)
```

### 4.2 Failover Priority Table

```
FAILURE CLASS │ PRIORITY 1         │ PRIORITY 2         │ PRIORITY 3
──────────────┼────────────────────┼────────────────────┼─────────────────────
F1 (Network)  │ Wait + Reconnect   │ PSTN Outbound Call │ Session Hold
F2 (ICE Fail) │ coturn TURN relay  │ SIP Bridge         │ PSTN Bridge
F3 (Jitsi)    │ Room Recreation    │ SIP Bridge         │ PSTN Bridge
F4 (coturn)   │ SIP Bridge         │ PSTN Bridge        │ Session Hold
F5 (Critical) │ PSTN All Users     │ Session Hold       │ Terminate+AuditLog
F6 (Silent)   │ Log + Skip Turn    │ —                  │ —
F7 (Duplicate)│ Kill Old Session   │ —                  │ —
F8 (Bridge TO)│ Retry Once         │ Session Hold       │ SMS Notify
F9 (Corrupt)  │ State Reconstruct  │ Resume Session     │ Manual Review Flag
F10 (Drift)   │ Force Timer Resync │ —                  │ —
```

---

## ░░ SECTION 5 — PSTN BRIDGE SPECIFICATION ░░

### 5.1 PSTN Bridge Architecture

```
PSTN BRIDGE COMPONENTS:

┌─────────────────────────────────────────────────────────────┐
│                  PSTN BRIDGE CONTROLLER                     │
│                                                             │
│  Outbound Call API  │  IVR Handler  │  DTMF Processor       │
│  Audio Mixer Bind   │  Session Bind │  Participant Mapper    │
└─────────────┬───────────────────────────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────────────────────────┐
│              SIP TRUNK PROVIDER ABSTRACTION                 │
│    (Twilio Voice / Exotel / Plivo / MSG91 — swappable)      │
└─────────────────────────────────────────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────────────────────────┐
│               PUBLIC SWITCHED TELEPHONE NETWORK             │
│                    (Participant's Phone)                     │
└─────────────────────────────────────────────────────────────┘
```

### 5.2 PSTN Bridge Behavior Rules

```
RULE PB-1: OUTBOUND ONLY
  The system dials OUT to the participant's verified phone number.
  No dial-in numbers exposed publicly.
  Phone numbers stored encrypted (HashiCorp Vault).
  Numbers retrieved only during active failover event.

RULE PB-2: IVR PROMPT SEQUENCE
  Step 1: "You have been disconnected from your session."
  Step 2: "Press 1 to rejoin as audio-only participant."
  Step 3: "Press 2 to exit the session."
  Step 4: On press 1 → bind phone audio to Jitsi audio mixer.
  Step 5: On press 2 or no input (30s) → log exit, close call.

RULE PB-3: AUDIO BINDING
  PSTN audio stream routed through SIP ↔ WebRTC bridge.
  Jitsi receives PSTN participant as standard audio peer.
  Turn engine sees PSTN participant as active with reduced timeout.
  Mute/unmute commands relayed via DTMF (#1 = mute, #2 = unmute).

RULE PB-4: SCORING CONTINUITY
  PSTN participation counted as reduced-mode participation.
  Participation flag: pstn_bridge_mode = true.
  Metrics collected: turn_taken (yes/no), audio_duration.
  Silence detection not available in PSTN mode.
  Interrupt detection not available in PSTN mode.
  Score notes: [PSTN_MODE — limited metrics].

RULE PB-5: COST GOVERNANCE
  PSTN bridge calls logged per second.
  Cost event emitted: pstn.call.cost.incurred.
  Billing service receives usage event.
  Per-session PSTN cost cap enforced (configurable, default: 5 minutes).
  If cap reached → participant notified → session hold for that user.

RULE PB-6: CONSENT
  Outbound call dialed only if participant pre-consented
  at session enrollment (R9 — Legal & Compliance).
  Consent record stored: participant_pstn_consent table.
  If no consent → PSTN bridge skipped → SESSION HOLD for that user.
```

### 5.3 PSTN Bridge Database Schema

```sql
-- PSTN Bridge Consent
CREATE TABLE participant_pstn_consent (
  consent_id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id         UUID NOT NULL REFERENCES users(id),
  session_type    TEXT NOT NULL,  -- 'GD' | 'INTERVIEW' | 'DOJO'
  phone_verified  BOOLEAN NOT NULL DEFAULT FALSE,
  consented_at    TIMESTAMP NOT NULL,
  revoked_at      TIMESTAMP
);

-- PSTN Bridge Events
CREATE TABLE pstn_bridge_events (
  event_id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  session_id      UUID NOT NULL,
  participant_id  UUID NOT NULL,
  failure_class   TEXT NOT NULL,
  bridge_type     TEXT NOT NULL,  -- 'PSTN' | 'SIP' | 'TURN'
  initiated_at    TIMESTAMP NOT NULL,
  answered_at     TIMESTAMP,
  terminated_at   TIMESTAMP,
  duration_sec    INTEGER,
  outcome         TEXT NOT NULL,  -- 'CONNECTED' | 'NO_ANSWER' | 'FAILED'
  cost_units      DECIMAL(10,4),
  pstn_mode_score_note TEXT
);

-- Session Failover Log (Immutable)
CREATE TABLE session_failover_log (
  log_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  session_id      UUID NOT NULL,
  participant_id  UUID,
  failure_class   TEXT NOT NULL,
  failure_detail  JSONB NOT NULL,
  recovery_path   TEXT NOT NULL,
  state_snapshot  JSONB NOT NULL,
  initiated_at    TIMESTAMP NOT NULL DEFAULT NOW(),
  resolved_at     TIMESTAMP,
  resolution      TEXT,           -- 'RECOVERED' | 'HOLD' | 'TERMINATED'
  audit_hash      TEXT NOT NULL   -- SHA256 of log_id + session_id + initiated_at
);
-- IMMUTABILITY ENFORCED: NO UPDATE, NO DELETE on session_failover_log
```

---

## ░░ SECTION 6 — SIP BRIDGE SPECIFICATION ░░

### 6.1 SIP Bridge Architecture

```
SIP BRIDGE COMPONENTS:

Backend Token Issuer
  → Generates short-lived SIP room credentials
  → Session scoped (match_id | session_id)
  → Expires on session end

SIP Trunk Provider Layer
  → Provider abstraction interface
  → Supported: Twilio SIP / Exotel SIP / Asterisk self-hosted
  → Swappable via env config (SIP_PROVIDER_CLASS)

Audio Gateway
  → Bridges SIP audio into WebRTC SFU pipeline
  → Participant visible as standard audio peer in Jitsi
  → Mute/unmute commands forwarded via SIP INFO message

SIP ↔ WebRTC Transcoder
  → Codec negotiation (G.711 / OPUS)
  → Latency monitoring
  → Auto-reconnect on codec mismatch
```

### 6.2 SIP Bridge Rules

```
RULE SB-1: TOKEN-BASED SIP JOIN
  SIP participants join using session-scoped SIP URI:
  sip:{session_id}@sip.ecoskiller.com
  Token embedded in SIP INVITE message.
  Unauthenticated SIP joins rejected at gateway.

RULE SB-2: AUDIO ROUTING
  SIP audio stream forwarded to Jitsi Videobridge via bridge connector.
  SIP participant appears as named audio peer in session.
  Backend receives audio presence events: participant_audio_active.

RULE SB-3: COMMAND PROPAGATION
  Mute/unmute commands from backend state machine
  forwarded via SIP INFO to SIP bridge.
  SIP bridge enforces mute at gateway level.
  SIP participant cannot override mute.

RULE SB-4: QUALITY MONITORING
  Jitter, packet loss, MOS score tracked per SIP session.
  If MOS < 2.5 for > 15s → escalate to PSTN bridge.
  All QoS events emitted to Prometheus.
```

---

## ░░ SECTION 7 — STATE MACHINE INTEGRATION ░░

### 7.1 Redis State Keys (Agent-Owned)

```
KEY SCHEMA:

session:{session_id}:failover_state
  → Values: NORMAL | DETECTING | RECOVERING | HOLD | TERMINATED
  → TTL: session_duration + 600s

session:{session_id}:participant:{participant_id}:bridge_mode
  → Values: WEBRTC | TURN | SIP | PSTN | HOLD | DISCONNECTED
  → Updated on each failover step

session:{session_id}:turn_engine_state
  → Frozen during failover
  → Restored on recovery
  → Includes: current_speaker, queue, elapsed_time, scores_so_far

session:{session_id}:failover_lock
  → Distributed lock (etcd)
  → Prevents concurrent failover execution for same session
  → TTL: 30s
```

### 7.2 State Transition Map

```
                    NORMAL
                      │
           failure_detected
                      │
                      ▼
                  DETECTING
                      │
           ┌──────────┴──────────┐
     recovery_path           no_path
           │                    │
           ▼                    ▼
       RECOVERING             HOLD
           │                    │
    ┌──────┴──────┐     ┌───────┴───────┐
  success      fail   reconnect     timeout
    │             │       │              │
    ▼             ▼       ▼              ▼
  NORMAL         HOLD   NORMAL       TERMINATED
```

---

## ░░ SECTION 8 — EVENT BUS CONTRACTS ░░

### 8.1 Events Emitted by CALL_FAILOVER_AGENT

```yaml
# AsyncAPI Contracts

channels:
  call.failover.initiated:
    publish:
      message:
        payload:
          type: object
          properties:
            session_id:      { type: string }
            participant_id:  { type: string }
            failure_class:   { type: string }
            bridge_path:     { type: string }
            initiated_at:    { type: string, format: date-time }

  call.failover.recovered:
    publish:
      message:
        payload:
          type: object
          properties:
            session_id:      { type: string }
            participant_id:  { type: string }
            bridge_used:     { type: string }
            recovery_time_ms: { type: integer }

  call.failover.failed:
    publish:
      message:
        payload:
          type: object
          properties:
            session_id:      { type: string }
            participant_id:  { type: string }
            failure_chain:   { type: array, items: { type: string } }
            final_state:     { type: string }

  call.pstn.bridge.activated:
    publish:
      message:
        payload:
          type: object
          properties:
            session_id:      { type: string }
            participant_id:  { type: string }
            phone_masked:    { type: string }
            call_sid:        { type: string }

  call.session.hold:
    publish:
      message:
        payload:
          type: object
          properties:
            session_id:      { type: string }
            reason:          { type: string }
            hold_until:      { type: string, format: date-time }

  call.session.terminated.failover:
    publish:
      message:
        payload:
          type: object
          properties:
            session_id:      { type: string }
            cause:           { type: string }
            partial_scores:  { type: object }
            audit_log_id:    { type: string }
```

---

## ░░ SECTION 9 — API CONTRACTS ░░

### 9.1 Internal REST API (Backend Use Only)

```yaml
openapi: 3.1.0
info:
  title: CALL_FAILOVER_AGENT Internal API
  version: 1.0.0

paths:
  /failover/trigger:
    post:
      summary: Manually trigger failover for a session
      security: [bearerAuth: []]
      requestBody:
        content:
          application/json:
            schema:
              type: object
              required: [session_id, failure_class]
              properties:
                session_id:     { type: string }
                participant_id: { type: string }
                failure_class:  { type: string }
      responses:
        "200": { description: Failover initiated }
        "409": { description: Failover already in progress }

  /failover/status/{session_id}:
    get:
      summary: Get current failover state for session
      responses:
        "200":
          content:
            application/json:
              schema:
                type: object
                properties:
                  session_id:      { type: string }
                  failover_state:  { type: string }
                  active_bridges:  { type: array }
                  hold_until:      { type: string }

  /failover/pstn/call/{session_id}/{participant_id}:
    post:
      summary: Initiate PSTN outbound call for participant
      responses:
        "200": { description: Call initiated }
        "403": { description: Consent not granted }
        "404": { description: Session not found }

  /failover/recover/{session_id}:
    post:
      summary: Restore session from HOLD state
      responses:
        "200": { description: Session resumed }
        "404": { description: No session in HOLD }

  /failover/log/{session_id}:
    get:
      summary: Retrieve immutable failover audit log for session
      responses:
        "200": { description: Audit log array }
```

---

## ░░ SECTION 10 — NOTIFICATION CONTRACTS ░░

### 10.1 Participant Notifications During Failover

```
NOTIFICATION N1 — RECONNECTING
  Trigger:   Failover F1 or F2 detected
  Channel:   WebSocket push → in-session banner
  Message:   "Connection issue detected. Attempting to reconnect..."
  Timeout:   10 seconds
  If resolved → dismiss banner
  If not → escalate notification

NOTIFICATION N2 — BRIDGE ACTIVATED
  Trigger:   SIP or PSTN bridge activated
  Channel:   WebSocket (if still connected) + Push + SMS
  Message:   "You are being reconnected via phone bridge.
              Please answer the incoming call to continue."
  SMS:       "Ecoskiller: Your session {session_id} reconnect call
              is incoming. Please answer."

NOTIFICATION N3 — SESSION ON HOLD
  Trigger:   All failover paths exhausted
  Channel:   Email + SMS + Push
  Message:   "Session {session_id} is on hold for {duration}.
              Rejoin within this window using the app or
              your registered phone number."

NOTIFICATION N4 — SESSION TERMINATED
  Trigger:   Hold timer expired
  Channel:   Email + Push
  Message:   "Session {session_id} has ended due to connectivity
              failure. Your partial score has been recorded."
  Attachment: Partial score card + audit log reference

NOTIFICATION N5 — SCORE IMPACT ADVISORY
  Trigger:   PSTN mode activated
  Channel:   WebSocket in-session overlay
  Message:   "[PSTN MODE] You are in audio-only mode.
              Limited metrics available. Score noted."
```

---

## ░░ SECTION 11 — OBSERVABILITY REQUIREMENTS ░░

### 11.1 Prometheus Metrics

```
# Failover counts
call_failover_total{failure_class, session_type, outcome}
  Counter — total failover events by class

# Active bridge sessions
call_bridge_active_sessions{bridge_type}
  Gauge — current sessions on PSTN / SIP / TURN

# Recovery time
call_failover_recovery_duration_seconds{bridge_type}
  Histogram — p50, p90, p99 recovery time

# PSTN call cost
call_pstn_cost_units_total{session_type}
  Counter — accumulated PSTN cost units

# Session hold rate
call_session_hold_total
  Counter — sessions entering HOLD state

# Session termination by failover
call_session_terminated_failover_total
  Counter — sessions terminated due to failover exhaustion
```

### 11.2 Grafana Dashboards

```
Dashboard: CALL_FAILOVER_AGENT Health

Panel 1:  Failover Rate (by failure class) — last 1h
Panel 2:  Active PSTN/SIP Bridge Sessions — live gauge
Panel 3:  Recovery Success Rate — percentage
Panel 4:  Session Hold Rate — last 24h
Panel 5:  PSTN Cost Burn Rate — cost/hour
Panel 6:  Average Recovery Time — p50/p90 histogram
Panel 7:  Failover Audit Log Explorer — link to Loki
Panel 8:  Top Failure Classes — bar chart last 24h
```

### 11.3 Alerts

```
ALERT: HighFailoverRate
  condition: call_failover_total > 50 per 5 minutes
  severity: warning
  notify: ops channel

ALERT: PStnBridgeCostAnomaly
  condition: call_pstn_cost_units_total increase > 200% vs prior hour
  severity: warning
  notify: billing + ops

ALERT: SessionHoldRateCritical
  condition: call_session_hold_total > 10 per 15 minutes
  severity: critical
  notify: on-call + ops + Wazuh SIEM

ALERT: AllBridgesDown
  condition: WebRTC + SIP + PSTN health checks all FAIL simultaneously
  severity: CRITICAL — IMMEDIATE
  notify: on-call + Slack + PagerDuty
  action: escalate to Admin Governance Service
```

---

## ░░ SECTION 12 — SECURITY CONTROLS ░░

```
CONTROL S1: PHONE NUMBER PROTECTION
  Phone numbers never stored in plain text.
  Retrieved from HashiCorp Vault at failover initiation.
  Logged in audit table as masked (last 4 digits only).
  Access restricted to CALL_FAILOVER_AGENT service account.

CONTROL S2: PSTN CALL AUTHORIZATION
  Every outbound call requires:
    → Valid session_id in ACTIVE or HOLD state
    → Valid participant_id bound to session
    → participant_pstn_consent record = TRUE
    → Redis failover lock acquired
  Absent any condition → call rejected.

CONTROL S3: SIP TOKEN SECURITY
  SIP credentials generated per-session.
  Short-lived (TTL = session_duration + 60s).
  Signed with HashiCorp Vault transit key.
  Replayed credentials rejected at SIP gateway.

CONTROL S4: FAILOVER LOCK
  Distributed lock via etcd prevents race conditions.
  One failover path active per session per participant at any time.
  Concurrent failover attempts for same participant rejected.

CONTROL S5: AUDIT IMMUTABILITY
  session_failover_log enforces:
    → No UPDATE via row-level security policy
    → No DELETE (admin included)
    → Append-only via insert-only role
  Every log row contains SHA256 integrity hash.
  Tamper detection on hash mismatch logged to Wazuh SIEM.
```

---

## ░░ SECTION 13 — INTERN EXECUTION GUIDE ░░

### 13.1 Local Setup

```bash
# Step 1: Navigate to failover agent service directory
cd /backend/services/call_failover_agent/

# Step 2: Install dependencies
pip install -r requirements.txt --break-system-packages

# Step 3: Copy environment template
cp .env.example .env

# Step 4: Required environment variables
REDIS_URL=redis://localhost:6379
POSTGRES_URL=postgresql://postgres:postgres@localhost:5432/ecoskiller
PSTN_PROVIDER=twilio               # or exotel / plivo
PSTN_ACCOUNT_SID=<from vault>
PSTN_AUTH_TOKEN=<from vault>
SIP_PROVIDER=twilio                # or asterisk
SIP_TRUNK_URI=sip.ecoskiller.com
VAULT_ADDR=http://localhost:8200
VAULT_TOKEN=<dev token>
PROMETHEUS_PORT=9102

# Step 5: Run the agent service
uvicorn main:app --reload --port 8020

# Step 6: Verify health endpoint
curl http://localhost:8020/health
# Expected: {"status": "ok", "bridges": {"pstn": "up", "sip": "up"}}
```

### 13.2 Testing a Failover

```bash
# Simulate F1 (Network dropout) for a test session
curl -X POST http://localhost:8020/failover/trigger \
  -H "Authorization: Bearer <test_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "session_id": "test-session-001",
    "participant_id": "test-user-001",
    "failure_class": "F1"
  }'

# Expected output:
# {
#   "status": "failover_initiated",
#   "recovery_path": "WAIT_RECONNECT → PSTN",
#   "estimated_recovery_ms": 10000
# }

# Check failover status
curl http://localhost:8020/failover/status/test-session-001

# Check audit log
curl http://localhost:8020/failover/log/test-session-001
```

---

## ░░ SECTION 14 — ENFORCEMENT RULES ░░

```
RULE E1:
  No live voice session may operate without CALL_FAILOVER_AGENT
  monitoring active.
  Absence of agent health check → session start BLOCKED.

RULE E2:
  PSTN bridge activation requires prior participant consent.
  Activation without consent record → FORBIDDEN.
  Violation logged to Wazuh SIEM.

RULE E3:
  All failover events must produce an immutable audit record.
  Missing audit record for any failover event
  → STOP EXECUTION of further failover attempts for that session
  → Admin Governance Service notified.

RULE E4:
  Session scoring must be preserved through any failover.
  Score state loss during failover
  → CRITICAL INCIDENT FLAG
  → Partial score reconstruction required.

RULE E5:
  PSTN cost cap must be enforced per session.
  Exceeding cost cap without capping
  → Billing anomaly event emitted
  → Session automatically moved to HOLD mode.

RULE E6:
  Agent must not make scoring decisions.
  CALL_FAILOVER_AGENT has read-only access to score state.
  It may freeze and restore score state only.
  It may NOT modify score values.

RULE E7:
  All bridges must be monitored by Prometheus.
  Any bridge without Prometheus metric emission
  → considered non-operational.

RULE E8:
  Failover audit logs must be retained for minimum 5 years.
  Governed by R60 — Long-Term Archival & Data History Law.
```

---

## ░░ SECTION 15 — ANTIGRAVITY SEAL ░░

```
╔══════════════════════════════════════════════════════════════════╗
║        CALL_FAILOVER_AGENT — ANTIGRAVITY PRODUCTION SEAL        ║
╠══════════════════════════════════════════════════════════════════╣
║  Agent:              CALL_FAILOVER_AGENT                         ║
║  Version:            v1.0-ANTIGRAVITY                            ║
║  System:             ECOSKILLER UNIFIED SAAS                     ║
║  Transport Layer:    WebRTC → TURN → SIP → PSTN → HOLD          ║
║  Execution Mode:     Deterministic — No discretion               ║
║  Scoring Authority:  NONE — Read-Only                           ║
║  Call Drop Policy:   ZERO TOLERANCE                              ║
║  State Preservation: MANDATORY                                   ║
║  Audit Trail:        IMMUTABLE — APPEND ONLY                     ║
║  Cost Governance:    PER SESSION CAP ENFORCED                    ║
║  Consent Gate:       ENFORCED — No bypass                       ║
║  Mutation Policy:    ADD-ONLY via version bump                   ║
║  Interpretation:     NONE PERMITTED                              ║
╠══════════════════════════════════════════════════════════════════╣
║  Absence of this agent in any voice session infrastructure       ║
║  → STOP SESSION LAUNCH                                          ║
║  → REPORT: FAILOVER_AGENT_MISSING                               ║
║  → NO SESSION COMPLETION CLAIM PERMITTED                        ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## ░░ DOCUMENT LOCK ░░

```
Document Status:    SEALED
File:               CALL_FAILOVER_AGENT__PSTN_BRIDGE__ECOSKILLER.md
Mutation Policy:    Add-Only — No line may be removed or altered
Version:            v1.0-ANTIGRAVITY
Authority:          Human declaration only
AI Generation Date: 2026-03-04
Parent System:      ECOSKILLER MASTER EXECUTION PROMPT v12.0+
Parent Reference:   Section H → Service #7 (Voice GD Orchestrator)
Governed By:        R10 Security · R16 Operations · R21 Ops Console
                    R60 Archival · R62 Transparency · P1 Security
```

---

*END OF CALL_FAILOVER_AGENT SEALED PROMPT*
