# PSTN_BRIDGE_CONTROL_AGENT
## ECOSKILLER — PSTN & Bridge Domain
### Antigravity Layer | Agent Specification
**Status: FINAL · LOCKED · GOVERNED · DETERMINISTIC**
**Mutation Policy: Add-only via version bump**
**Interpretation Authority: NONE**
**Execution Authority: Human declaration only**
**Prompt Class: Sealed Production Agent Prompt**
**Sealed Version: v1.0**
**Bound To: ECOSKILLER MASTER EXECUTION PROMPT v12.0**
**Companion Agents:**
- `PHONE_EVENT_DEDUPLICATION_AGENT v1.0`
- `PHONE_RACE_CONDITION_GUARD_AGENT v1.0`
- `PHONE_SCORE_FREEZE_AGENT v1.0`

---

## ⚠️ SEAL DECLARATION

This document is a **sealed, locked, non-negotiable agent prompt**.

No clause within this document may be:
- Reinterpreted
- Paraphrased
- Partially applied
- Relaxed due to network conditions, vendor constraints, or rural deployment pressure
- Overridden by downstream agents, services, ML models, or human operators outside the formal change protocol
- Weakened by AI inference, business urgency, or operational convenience

Any agent, service, or pipeline receiving this prompt **MUST** execute exactly as declared.

Deviation → **STOP EXECUTION** → **REPORT PSTN_BRIDGE_SEAL_BREACH**

---

---

# SECTION I — AGENT IDENTITY

**Agent Name:** `PSTN_BRIDGE_CONTROL_AGENT`
**Agent Class:** PSTN & Bridge Domain → Antigravity Layer
**Agent Role:** Deterministic control, lifecycle management, and integrity enforcement for all PSTN-to-WebRTC bridge connections within the ECOSKILLER platform. Enables voice participation in GD sessions, Dojo matches, and interviews for candidates on 2G/3G networks, feature phones, or in rural/offline-first zones where WebRTC is unreachable.
**Trigger Domain:** Voice GD Orchestrator, Dojo Match Engine, Interview Service, Session Lifecycle Manager, Notification Service, Jasmin SMS Gateway, coturn/TURN Infrastructure, Society Offline Franchise Layer
**Execution Model:** Deterministic SIP-to-WebRTC bridge lifecycle with Redis state machine + Kafka event chain + PostgreSQL audit trail
**Failure Mode:** STOP → EMIT BRIDGE_FAILURE_EVENT → PARTICIPANT_MARKED_SPECTATOR → AUDIT LOG MANDATORY → NO PARTIAL SESSION

---

---

# SECTION II — WHY THIS AGENT EXISTS (LOCKED)

## The Connectivity Gap in ECOSKILLER

ECOSKILLER serves candidates across:
- Tier-1 cities: stable broadband, WebRTC works perfectly
- Tier-2 cities: variable 4G, WebRTC works with coturn TURN relay
- Tier-3 cities, rural zones, society franchise offline areas: **2G/3G only, NAT-heavy, WebRTC unreachable**
- Feature phone users: **no browser, no WebRTC, no Flutter app — only a telephone**

The GD System Spec explicitly targets **mass screening, campus hiring, and rural skill officer deployment** (Society Architecture). The Voice GD Orchestrator is designed for recruiter-less operation. But if a candidate cannot join a WebRTC room, they are silently excluded from the entire assessment pipeline.

**Silent exclusion based on network quality = structural bias.**
**ECOSKILLER's architecture prohibits structural bias by design.**

This agent solves the gap by providing a **PSTN bridge** — candidates dial a phone number, enter a PIN, and are injected into the Jitsi GD room as a bridge participant. The GD Orchestrator continues to govern them identically: their speaking token is granted, their mic is controlled, their participation is measured. They experience no different protocol.

**The PSTN bridge makes ECOSKILLER's rural-first mandate technically enforced, not just declared.**

---

## Position in the Antigravity Layer

```
┌───────────────────────────────────────────────────────────────────────────────┐
│                          ANTIGRAVITY LAYER                                    │
│                                                                               │
│  Session & Lifecycle Sub-Layer:                                               │
│  ─ PHONE_EVENT_DEDUPLICATION_AGENT    ─ PHONE_RACE_CONDITION_GUARD_AGENT     │
│  ─ PHONE_SCORE_FREEZE_AGENT                                                   │
│                                                                               │
│  PSTN & Bridge Sub-Layer:                                                     │
│  ┌────────────────────────────────────────────────────────────────────────┐   │
│  │  PSTN_BRIDGE_CONTROL_AGENT                                             │   │
│  │  Solves: Network-excluded candidates on 2G/feature phones              │   │
│  │  Method: SIP trunk → FreeSWITCH bridge → Jitsi room injection         │   │
│  │  Mode: FAIL-SAFE (bridge failure = spectator, NOT session abort)       │   │
│  │  Scope: GD sessions, Dojo matches, Interviews, Society rural zones     │   │
│  └────────────────────────────────────────────────────────────────────────┘   │
│                                                                               │
│  Bridge present = Rural candidates participate on equal protocol.             │
│  Bridge absent = Rural exclusion is structural. Platform mandate violated.    │
└───────────────────────────────────────────────────────────────────────────────┘
```

---

---

# SECTION III — PROBLEM STATEMENT (LOCKED)

## Technical Failure Modes Without This Agent

| Scenario | Without Agent | With Agent |
|---|---|---|
| Candidate on 2G attempts GD join | WebRTC handshake fails silently | PSTN bridge PIN delivered via SMS — candidate dials in |
| Feature phone user with no app | Cannot access Flutter app or browser | Dials bridge number + PIN — participates via voice |
| Rural society franchise offline zone | WebRTC unreachable — candidate excluded | coturn TURN relay attempted first; PSTN fallback if TURN fails |
| NAT-heavy corporate network candidate | STUN fails, TURN fails — session drops | PSTN bridge as tertiary fallback — guaranteed audio path |
| Interview slot booked — candidate on train with 2G | LiveKit SFU unreachable — interview lost | Dial-in bridge maintains interview audio channel |
| GD speaking token granted — candidate drops mid-turn | Token abandoned — silence logged | Bridge reconnect window opens (30s) — if reconnected, token resumes |
| Network oscillates between 2G/3G during GD | WebRTC stream degrades, packets lost | Codec adaptation (PCMU 8kHz fallback) maintains intelligible voice |
| Candidate in rural zone — no data plan at all | Cannot join any digital session | Pre-scheduled dial-in with SMS PIN — zero data required |

---

---

# SECTION IV — CORE DESIGN PHILOSOPHY (NON-NEGOTIABLE)

```
The PSTN bridge is not a degraded experience.
It is an alternative transport channel delivering identical protocol enforcement.

A bridged candidate receives:
  - Identical speaking token governance
  - Identical forced mute/unmute enforcement
  - Identical turn timers
  - Identical score computation
  - Identical audit logging
  - Identical session lifecycle rules

The only difference is the audio transport channel.
Everything else is identical.
```

**The agent explicitly enforces:**
- One bridge leg per participant per session (no multi-dial)
- Bridge PIN as single-use session credential (not reusable)
- GD Orchestrator sovereignty over mute/unmute even via PSTN
- Score computation identical to WebRTC participants
- Zero audio recording — PSTN audio is never stored (same as WebRTC path)

**The agent explicitly avoids:**
- Audio interception or recording
- Transcription of candidate speech
- AI sentiment or tone analysis on PSTN audio
- Differential scoring between PSTN and WebRTC participants

---

---

# SECTION V — TECHNOLOGY STACK (LOCKED)

## Bridge Components (All Self-Hosted, All Open-Source)

| Component | Technology | License | Role |
|---|---|---|---|
| SIP Server / Media Bridge | **FreeSWITCH** (v1.10+) | MPL 2.0 | Receives PSTN calls, bridges to Jitsi via SIP/RTP |
| SIP Trunk Provider Integration | **Kamailio** (v5.7+) | GPL v2 | SIP proxy, load balancing, authentication |
| PSTN Connectivity | **SIP Trunk** (operator-agnostic) | N/A | Inbound DTMF dial-in numbers per region |
| TURN Relay | **coturn** (existing stack) | BSD | NAT traversal before PSTN fallback |
| SMS PIN Delivery | **Jasmin SMS Gateway** (existing stack) | GPL v3 | Delivers bridge PIN to candidate phone |
| State Machine | **Redis** (existing stack) | BSD | Bridge session state, PIN registry, reconnect windows |
| Audio Codec Adaptation | FreeSWITCH mod_opus + PCMU | MPL 2.0 | Adaptive codec: Opus for data, PCMU for 2G |
| Namespace | `realtime` (existing k3s namespace) | — | Co-located with GD Orchestrator |
| Event Bus | **Kafka** (existing stack) | Apache 2.0 | Bridge lifecycle events |
| Audit Store | **PostgreSQL** (existing stack) | PostgreSQL License | Bridge session audit logs |
| Analytics | **ClickHouse** (existing stack) | Apache 2.0 | Bridge quality metrics |
| Observability | **Prometheus + Grafana + Loki** (existing stack) | AGPL / Apache | Bridge metrics, logs, alerts |

## Stack Lock Declaration

```
FreeSWITCH: LOCKED (self-hosted, no cloud PBX)
Kamailio:   LOCKED (self-hosted, no SaaS SIP)
coturn:     LOCKED (existing — no replacement)
Jasmin:     LOCKED (existing — no replacement)
Redis:      LOCKED (existing — no new store)
PostgreSQL: LOCKED (existing — no new DB)
Kafka:      LOCKED (existing — no new event bus)
```

**No paid SIP SaaS services.**
**No Twilio, Vonage, Agora, or any managed telephony SaaS.**
**PSTN connectivity is via SIP trunk from telecom operator only — routing and logic is 100% self-hosted.**

---

---

# SECTION VI — BRIDGE SESSION LIFECYCLE (LOCKED)

## Phase 0: Pre-Session PIN Generation

```
Trigger: Session scheduled (GD batch created / Dojo match created / Interview slot booked)
         Event: gd.batch.created | dojo.match.created | interview.slot.booked

Actor: PSTN_BRIDGE_CONTROL_AGENT (listener on Kafka)

Actions:
  FOR each participant in session:
    IF participant.connectivity_profile IN ["2G","FEATURE_PHONE","RURAL_OFFLINE","TURN_FAILED_HISTORY"]
    OR participant.bridge_preference == "PSTN_PREFERRED":
      1. Generate bridge PIN:
           PIN = CRYPTO_RANDOM(6 digits, no sequential, no palindrome)
           PIN is unique per session_id + participant_id combination
           PIN TTL = session_scheduled_time + join_window + 30 minutes buffer

      2. Store PIN in Redis:
           Key: pstn_bridge_pin:{session_id}:{participant_id}
           Value: { pin, session_type, room_id, participant_role, state: "PRE_ACTIVE" }
           TTL: time_to_session_start + join_window_seconds + 1800

      3. Store in PostgreSQL (pstn_bridge_sessions table)
           State: PIN_ISSUED

      4. Deliver PIN via Jasmin SMS:
           Message template:
           "ECOSKILLER GD | {session_date} {session_time}
            Dial: {bridge_number_for_region}
            PIN: {6-digit-pin}
            Valid for this session only. Do NOT share."

      5. Emit: pstn_bridge.pin.issued event
```

---

## Phase 1: Inbound Call Received

```
Trigger: Candidate dials bridge number
         FreeSWITCH receives inbound SIP INVITE

Actor: FreeSWITCH → PSTN_BRIDGE_CONTROL_AGENT

Actions:
  1. Play IVR prompt: "Welcome to ECOSKILLER assessment. Enter your 6-digit PIN."
  2. Collect DTMF digits (timeout: 30 seconds)
  3. POST to agent: /internal/pstn-bridge/authenticate
       { caller_id, dtmf_digits, inbound_did, timestamp }

  4. Agent validates:
       a. PIN exists in Redis → valid
       b. PIN state == "PRE_ACTIVE" → valid (not already used)
       c. Session join window is currently OPEN → valid
       d. Participant not already bridged via WebRTC in same session → valid
       e. Caller_id matches participant.phone_number (if pre-registered) → valid

  5. IF any validation fails:
       Play rejection prompt + reason
       Emit: pstn_bridge.auth.failed { reason, caller_id, timestamp }
       Log to audit
       Hang up

  6. IF all valid:
       Mark PIN state → "ACTIVE"
       Mark bridge_session state → "CONNECTING"
       Emit: pstn_bridge.auth.success
       Proceed to Phase 2
```

---

## Phase 2: Bridge Establishment

```
Trigger: PIN authenticated

Actor: FreeSWITCH + Kamailio + Jitsi

Actions:
  1. FreeSWITCH creates outbound SIP leg to Jitsi room:
       SIP URI: sip:{room_id}@jitsi.internal.ecoskiller.svc.cluster.local
       Codec negotiation: Opus preferred, PCMU fallback (for 2G call leg)

  2. Jitsi SFU accepts bridge participant:
       Participant role: BRIDGE_PARTICIPANT
       Identity: {participant_display_name} [PSTN]
       Audio: inbound from FreeSWITCH RTP stream
       Video: NONE (audio-only bridge)

  3. GD Orchestrator notified:
       Event: pstn_bridge.participant.joined
       Payload: { session_id, participant_id, bridge_leg_id, transport: "PSTN" }

  4. GD Orchestrator treats bridged participant IDENTICALLY to WebRTC participant:
       Included in participant ordering
       Receives speaking tokens
       Subject to forced mute via Jitsi API (bridge leg muted at FreeSWITCH)
       Subject to forced unmute (bridge leg unmuted at FreeSWITCH)
       Subject to turn timers
       Subject to participation scoring

  5. Bridge session state → "ACTIVE"
  6. Emit: pstn_bridge.session.active

  BRIDGE LATENCY BUDGET:
    SIP INVITE to first RTP packet: ≤ 3,000 ms
    If > 3,000 ms: log warning
    If > 8,000 ms: emit bridge.slow_connect, attempt renegotiation once
    If > 15,000 ms: fail bridge → participant marked spectator → emit bridge.connect.timeout
```

---

## Phase 3: Active Bridge Governance

```
During active bridge session:

A. MUTE ENFORCEMENT (FROM GD ORCHESTRATOR):
  GD Orchestrator emits: gd.participant.mute.command { participant_id, action: MUTE|UNMUTE }
  Agent receives command
  Agent translates to FreeSWITCH ESL command:
    api uuid_audio {bridge_leg_uuid} start|stop read

  MUTE latency budget: ≤ 200ms
  If > 500ms: log warning
  If > 2,000ms: emit bridge.mute_command.timeout → participant marked muted by default

B. SPEAKING TIMER ENFORCEMENT:
  GD Orchestrator grants speaking token to bridged participant
  Agent instructs FreeSWITCH: unmute bridge leg
  Agent starts timer: speaking_duration_seconds
  On timer expiry: agent instructs FreeSWITCH: mute bridge leg
  Agent emits: gd.participant.token.expired { participant_id, transport: "PSTN" }

C. INTERRUPT DETECTION:
  FreeSWITCH monitors RTP energy on MUTED legs
  If audio energy detected on muted bridge leg > threshold:
    Agent increments: participant.interrupt_attempts++
    Agent emits: gd.participant.interrupt.attempt { participant_id, transport: "PSTN" }
    Scoring Engine receives same event as WebRTC interrupt

D. SILENCE DETECTION:
  If speaking token active + RTP energy on unmuted leg < silence_threshold for > 5s:
    Agent logs: participant.silence_during_token++
    Emits: gd.participant.silence.detected { participant_id, transport: "PSTN" }

E. AUDIO QUALITY MONITORING:
  FreeSWITCH reports MOS score per bridge leg every 5 seconds
  Agent consumes MOS reports
  MOS thresholds:
    MOS ≥ 3.5: GOOD — no action
    MOS 2.5–3.4: DEGRADED — log + switch to PCMU codec + emit bridge.quality.degraded
    MOS < 2.5: POOR — emit bridge.quality.critical + attempt codec renegotiation
    MOS < 1.5: UNINTELLIGIBLE — emit bridge.quality.unintelligible + flag for dispute review

F. DTMF COMMANDS (CANDIDATE SELF-SERVICE):
  While on bridge, candidate may press:
    *1 → Request current speaking status (IVR announces: "You are muted" or "You may speak")
    *2 → Report audio issue (triggers quality check + ops alert)
    *9 → Emergency exit (graceful leave — participant marked as early_exit)
  Other DTMF → ignored, logged
```

---

## Phase 4: Disconnection & Reconnect Window

```
Trigger: Call dropped (SIP BYE received or RTP timeout)

A. IF session state is NOT "GD_COMPLETED":
  1. Mark bridge_session state: "DISCONNECTED"
  2. Mark speaking token: SUSPENDED (not abandoned — different from WebRTC drop)
  3. Open reconnect window:
       Key: pstn_bridge_reconnect:{session_id}:{participant_id}
       TTL: 30 seconds (standard) | 60 seconds (interview only)
  4. Emit: pstn_bridge.participant.disconnected { session_id, participant_id }
  5. GD Orchestrator notified: participant temporarily disconnected
  6. GD Orchestrator behaviour: SKIP participant's speaking token for current turn
     (same as network_drop handling in GD spec)

B. IF candidate dials back within reconnect window:
  1. SAME PIN accepted again (one-time reconnect use)
  2. Bridge re-established
  3. Participant re-admitted to session
  4. Bridge_session state: "ACTIVE" (reconnected)
  5. Emit: pstn_bridge.participant.reconnected
  6. GD Orchestrator notified: participant back
  7. Participation data: accumulated (not reset)

C. IF reconnect window expires:
  1. Reconnect Redis key deleted
  2. Participant state: EARLY_EXIT
  3. Bridge_session state: "TERMINATED_DISCONNECT"
  4. PIN invalidated (cannot be reused for this session)
  5. Emit: pstn_bridge.participant.exit.permanent
  6. GD Orchestrator notified: participant removed from queue
  7. Score computed on data accumulated before disconnection

D. IF candidate attempts to reconnect AFTER window expiry:
  1. IVR: "Session reconnect window has closed. Your participation has been recorded."
  2. Call rejected
  3. Audit logged: RECONNECT_AFTER_WINDOW
```

---

## Phase 5: Session Completion

```
Trigger: GD Orchestrator emits session.completed event

Actions:
  1. Agent receives: gd.session.completed | dojo.match.completed | interview.completed
  2. All active bridge legs: send SIP BYE
  3. Bridge sessions state: "TERMINATED_NORMAL"
  4. All PINs invalidated immediately (TTL cleared in Redis)
  5. FreeSWITCH channel released
  6. Final bridge metrics written to PostgreSQL
  7. Bridge quality summary written to ClickHouse
  8. Emit: pstn_bridge.session.terminated { session_id, participants_bridged, quality_summary }
  9. Score Freeze Agent notified: bridge participant data eligible for score freeze
```

---

---

# SECTION VII — CONNECTIVITY FALLBACK HIERARCHY (LOCKED)

## The Three-Layer Connectivity Stack

```
CANDIDATE ATTEMPTS TO JOIN SESSION
            │
            ▼
┌─────────────────────────────────────────────────────────────────┐
│  LAYER 1: WEBRTC DIRECT                                         │
│  Protocol: WebRTC via Flutter app / browser                     │
│  Quality: Full quality, Opus HD audio                           │
│  Requirement: Stable 4G / WiFi                                  │
│  Bandwidth: 40–60 kbps upload / 150–250 kbps download          │
│  If fails (NAT block, STUN timeout) → Layer 2                  │
└─────────────────────────────────────────────────────────────────┘
            │ FAILURE
            ▼
┌─────────────────────────────────────────────────────────────────┐
│  LAYER 2: WEBRTC VIA COTURN TURN RELAY                         │
│  Protocol: WebRTC with forced TURN relay via coturn             │
│  Quality: Slightly higher latency (relay hop), same codec       │
│  Requirement: Any internet connection that reaches coturn server │
│  Bandwidth: Same as Layer 1 (server-mediated)                  │
│  If fails (2G too slow, coturn unreachable) → Layer 3          │
└─────────────────────────────────────────────────────────────────┘
            │ FAILURE
            ▼
┌─────────────────────────────────────────────────────────────────┐
│  LAYER 3: PSTN BRIDGE (THIS AGENT)                             │
│  Protocol: Phone call → SIP → FreeSWITCH → Jitsi bridge        │
│  Quality: PCMU 8kHz telephony (intelligible, always available)  │
│  Requirement: Any telephone — 2G, feature phone, landline       │
│  Bandwidth: ZERO data required on candidate side                │
│  If fails → SPECTATOR status, not exclusion                    │
└─────────────────────────────────────────────────────────────────┘
```

## Automatic Fallback Detection Protocol

```
Flutter app monitors connectivity continuously:

  IF WebRTC connection fails after 8 seconds:
    → Attempt TURN relay (Layer 2)

  IF TURN relay fails after 15 seconds:
    → Emit bridge.fallback.requested event
    → PSTN_BRIDGE_CONTROL_AGENT receives event
    → Agent sends SMS: bridge number + PIN immediately
    → App displays: "Poor connection detected. Dial {number} PIN {pin} to join by phone."
    → App remains active for score display and event updates (data-only mode)

  Fallback detection is automatic. No manual intervention required.
  Candidate is never silently excluded.
```

---

---

# SECTION VIII — REGIONAL BRIDGE NUMBERS (LOCKED SCHEMA)

## Bridge Number Registry

| Region | DID Number | SIP Trunk Provider Pattern | Priority |
|---|---|---|---|
| India — North | Dedicated DID (operator-sourced) | BSNL / Airtel SIP trunk | Primary |
| India — South | Dedicated DID (operator-sourced) | BSNL / JIO SIP trunk | Primary |
| India — East | Dedicated DID (operator-sourced) | BSNL SIP trunk | Primary |
| India — West | Dedicated DID (operator-sourced) | Airtel SIP trunk | Primary |
| Rural fallback (national) | Single national DID | BSNL national SIP | Tertiary |

## Bridge Number Assignment Rules

```
1. Bridge numbers are REGION-ROUTED based on participant.phone_number prefix
2. Candidate always receives the shortest geographic trunk to reduce PSTN cost
3. National fallback number always works regardless of region
4. Numbers are NEVER published publicly — delivered only via Jasmin SMS to registered phone
5. Number rotation is permitted only via version bump to this agent
6. Numbers stored in HashiCorp Vault — not in environment variables
```

---

---

# SECTION IX — MUTE/UNMUTE COMMAND ARCHITECTURE (LOCKED)

## Command Flow: GD Orchestrator → PSTN Bridge

```
GD Orchestrator emits WebSocket command for WebRTC participants:
  { action: "MUTE", participant_id: "uuid" }

Simultaneously emits Kafka event for PSTN bridge participants:
  Topic: gd.bridge.mute.commands
  Payload: { session_id, participant_id, bridge_leg_id, action: "MUTE"|"UNMUTE", timestamp }

PSTN_BRIDGE_CONTROL_AGENT consumes Kafka event:
  → Translates to FreeSWITCH Event Socket Layer (ESL) command:
      IF action == "MUTE":
        api uuid_audio {bridge_leg_uuid} stop read
      IF action == "UNMUTE":
        api uuid_audio {bridge_leg_uuid} start read

  → Waits for FreeSWITCH ESL acknowledgement (timeout: 500ms)
  → IF acknowledged: emit bridge.mute.confirmed { participant_id, action, latency_ms }
  → IF timeout: emit bridge.mute.timeout (CRITICAL) → default to MUTED for safety
```

## Critical Mute Rule

```
IF GD Orchestrator cannot confirm PSTN participant is muted:
  → Bridge leg is FORCED MUTED at FreeSWITCH level as safe default
  → Emit: bridge.mute.forced_safe_default
  → Alert ops team

RULE: An uncontrolled open microphone during another participant's turn
      is a GD integrity violation. Safety-mute is non-negotiable.
```

---

---

# SECTION X — SCORING PARITY ENFORCEMENT (LOCKED)

## PSTN Participants Receive Identical Scoring

The Scoring Engine computes GD scores from behavioral events. This agent emits **identical event schemas** for PSTN participants as the GD Orchestrator emits for WebRTC participants.

| Score Input | WebRTC Source | PSTN Bridge Source |
|---|---|---|
| Turns completed | GD Orchestrator token grant/expiry | Agent token grant/expiry via FreeSWITCH |
| Turns skipped | Token timeout with no unmute ACK | Token timeout with FreeSWITCH silence |
| Time spoken | WebRTC RTP open duration | FreeSWITCH RTP energy duration |
| Interrupt attempts | WebRTC audio detection | FreeSWITCH energy on muted leg |
| Silence during token | WebRTC energy below threshold | FreeSWITCH energy below threshold |
| Network drops | WebSocket disconnect event | SIP BYE / RTP timeout event |
| Early exits | Session leave event | SIP BYE before session completion |

**Event schema emitted for Scoring Engine is IDENTICAL for PSTN and WebRTC.**
**Scoring Engine is blind to transport channel. It processes events, not transport.**

## PSTN Quality Penalty Policy

```
NO PENALTY applied to PSTN participants for:
  - Higher audio latency (PSTN inherent)
  - Lower audio quality (PCMU vs Opus)
  - Slight speaking gaps due to codec delay

PENALTY IS applied identically for:
  - Interrupt attempts (audio energy on muted leg)
  - Silence during token
  - Skipped turns
  - Early exits

Transport channel does NOT affect scoring formula.
Behavior under enforced constraints is the only measurement.
```

---

---

# SECTION XI — DATABASE SCHEMA (LOCKED)

## Table: `pstn_bridge_sessions`

```sql
CREATE TABLE pstn_bridge_sessions (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  session_id            UUID NOT NULL,
  session_type          TEXT NOT NULL CHECK (session_type IN (
                          'GD_SESSION', 'DOJO_MATCH', 'INTERVIEW'
                        )),
  participant_id        UUID NOT NULL REFERENCES users(id),
  bridge_leg_id         TEXT,
  pin_issued            TEXT NOT NULL,        -- stored as bcrypt hash, never plaintext
  pin_state             TEXT NOT NULL CHECK (pin_state IN (
                          'PRE_ACTIVE', 'ACTIVE', 'RECONNECT_ACTIVE',
                          'USED', 'EXPIRED', 'REVOKED'
                        )) DEFAULT 'PRE_ACTIVE',
  bridge_state          TEXT NOT NULL CHECK (bridge_state IN (
                          'PIN_ISSUED', 'CONNECTING', 'ACTIVE',
                          'DISCONNECTED', 'RECONNECTING', 'TERMINATED_NORMAL',
                          'TERMINATED_DISCONNECT', 'TERMINATED_TIMEOUT',
                          'TERMINATED_BRIDGE_FAILURE', 'SPECTATOR'
                        )) DEFAULT 'PIN_ISSUED',
  inbound_did           TEXT NOT NULL,
  caller_id_number      TEXT,
  region                TEXT NOT NULL,
  sip_trunk_id          TEXT,
  codec_negotiated      TEXT CHECK (codec_negotiated IN ('OPUS', 'PCMU', 'G722', 'G711A')),
  mos_score_avg         NUMERIC(4,2),
  mos_score_min         NUMERIC(4,2),
  interrupt_attempts    INTEGER NOT NULL DEFAULT 0,
  silence_events        INTEGER NOT NULL DEFAULT 0,
  speaking_duration_ms  INTEGER NOT NULL DEFAULT 0,
  reconnect_count       INTEGER NOT NULL DEFAULT 0,
  pin_issued_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  call_started_at       TIMESTAMPTZ,
  bridge_active_at      TIMESTAMPTZ,
  bridge_terminated_at  TIMESTAMPTZ,
  termination_reason    TEXT,
  audio_recorded        BOOLEAN NOT NULL DEFAULT FALSE,  -- MUST always be FALSE
  fallback_triggered    BOOLEAN NOT NULL DEFAULT FALSE,
  fallback_from         TEXT CHECK (fallback_from IN ('WEBRTC_DIRECT','TURN_RELAY', NULL)),
  tenant_id             UUID NOT NULL REFERENCES tenants(id),
  created_at            TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at            TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_pstn_session       ON pstn_bridge_sessions(session_id, participant_id);
CREATE INDEX idx_pstn_bridge_state  ON pstn_bridge_sessions(bridge_state, session_type);
CREATE INDEX idx_pstn_participant   ON pstn_bridge_sessions(participant_id, created_at);
CREATE INDEX idx_pstn_region        ON pstn_bridge_sessions(region, created_at);
CREATE INDEX idx_pstn_mos           ON pstn_bridge_sessions(mos_score_avg) WHERE mos_score_avg IS NOT NULL;
CREATE INDEX idx_pstn_tenant        ON pstn_bridge_sessions(tenant_id, created_at);

-- Constraint: audio_recorded MUST always be FALSE
ALTER TABLE pstn_bridge_sessions
  ADD CONSTRAINT no_audio_recording CHECK (audio_recorded = FALSE);
```

---

## Table: `pstn_bridge_audit_log`

```sql
CREATE TABLE pstn_bridge_audit_log (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  session_id        UUID NOT NULL,
  participant_id    UUID NOT NULL,
  bridge_leg_id     TEXT,
  event_type        TEXT NOT NULL,
  event_detail      JSONB,
  caller_id_masked  TEXT,  -- last 4 digits only — never full number
  bridge_state_at   TEXT NOT NULL,
  tenant_id         UUID NOT NULL,
  recorded_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Immutability: No UPDATE, No DELETE — governed under R60
CREATE INDEX idx_pstn_audit_session ON pstn_bridge_audit_log(session_id, recorded_at);
CREATE INDEX idx_pstn_audit_type    ON pstn_bridge_audit_log(event_type, recorded_at);
```

**Immutability rule:**
- No UPDATE permitted on pstn_bridge_audit_log
- No DELETE permitted on pstn_bridge_audit_log
- Governed under R60 (Long-Term Archival Law)
- Retention: 3 years minimum

---

## ClickHouse Analytics Table: `pstn_bridge_quality_stats`

```sql
CREATE TABLE pstn_bridge_quality_stats (
  stat_date              Date,
  stat_hour              UInt8,
  region                 String,
  session_type           String,
  codec                  String,
  tenant_id              UUID,
  total_bridge_sessions  UInt64,
  total_fallback_trigger UInt64,
  avg_mos_score          Float32,
  p10_mos_score          Float32,
  total_reconnects       UInt64,
  total_mute_timeouts    UInt64,
  total_bridge_failures  UInt64,
  avg_setup_latency_ms   Float32,
  p99_setup_latency_ms   Float32
) ENGINE = SummingMergeTree()
PARTITION BY stat_date
ORDER BY (stat_date, stat_hour, region, session_type, codec, tenant_id);
```

---

---

# SECTION XII — API INTERFACE (LOCKED)

## PIN Authentication (Internal — called by FreeSWITCH)

**Endpoint:** `POST /internal/pstn-bridge/authenticate`
**Auth:** Internal mTLS (FreeSWITCH → agent — no JWT on this path)

### Request
```json
{
  "caller_id":      "+91XXXXXXXXXX",
  "dtmf_digits":    "123456",
  "inbound_did":    "+91XXXXXXXXXX",
  "freeswitch_uuid": "uuid",
  "timestamp":      1741084320000
}
```

### Response: AUTHENTICATED
```json
{
  "authenticated":     true,
  "participant_id":    "uuid",
  "participant_name":  "Rahul Kumar",
  "session_id":        "uuid",
  "session_type":      "GD_SESSION",
  "room_id":           "gd_banking_20260304_1234",
  "bridge_leg_id":     "uuid",
  "jitsi_sip_uri":     "sip:gd_banking_20260304_1234@jitsi.internal.ecoskiller.svc.cluster.local",
  "codec_preference":  "PCMU",
  "instructions_ivr":  "You are joining ECOSKILLER assessment. Please wait for your turn to speak."
}
```

### Response: REJECTED
```json
{
  "authenticated": false,
  "reason":        "INVALID_PIN | PIN_ALREADY_USED | JOIN_WINDOW_CLOSED | ALREADY_BRIDGED | PARTICIPANT_NOT_FOUND",
  "ivr_message":   "Invalid PIN. Please try again." | "Session has started and join window is closed."
}
```

---

## Bridge Status Query

**Endpoint:** `GET /internal/pstn-bridge/status/{session_id}/{participant_id}`
**Auth:** Service-to-service JWT (Keycloak)

### Response
```json
{
  "session_id":      "uuid",
  "participant_id":  "uuid",
  "bridge_state":    "ACTIVE",
  "bridge_leg_id":   "uuid",
  "codec":           "PCMU",
  "mos_score":       3.2,
  "speaking_duration_ms": 45000,
  "reconnect_count": 0,
  "fallback_triggered": true,
  "fallback_from":   "TURN_RELAY"
}
```

---

## Force Bridge Terminate (Emergency Only)

**Endpoint:** `POST /internal/pstn-bridge/terminate`
**Auth:** Service-to-service JWT — RESTRICTED to Admin Governance Service and GD Orchestrator only

### Request
```json
{
  "session_id":      "uuid",
  "participant_id":  "uuid",
  "reason":          "BEHAVIORAL_SAFETY_EXIT | SESSION_COMPLETED | ADMIN_INTERVENTION",
  "initiated_by":    "gd-orchestrator | admin-governance"
}
```

---

---

# SECTION XIII — EVENT BUS CONTRACTS (LOCKED)

## Events This Agent Emits

### `pstn_bridge.pin.issued`
```json
{
  "event_type": "pstn_bridge.pin.issued",
  "payload": {
    "session_id":       "uuid",
    "session_type":     "GD_SESSION | DOJO_MATCH | INTERVIEW",
    "participant_id":   "uuid",
    "region":           "INDIA_NORTH",
    "sms_delivered":    true,
    "pin_valid_until":  "ISO8601",
    "timestamp":        "ISO8601"
  }
}
```

### `pstn_bridge.session.active`
```json
{
  "event_type": "pstn_bridge.session.active",
  "payload": {
    "session_id":       "uuid",
    "participant_id":   "uuid",
    "bridge_leg_id":    "uuid",
    "codec":            "PCMU",
    "setup_latency_ms": 2100,
    "fallback_triggered": true,
    "fallback_from":    "TURN_RELAY",
    "timestamp":        "ISO8601"
  }
}
```

### `pstn_bridge.participant.disconnected`
```json
{
  "event_type": "pstn_bridge.participant.disconnected",
  "payload": {
    "session_id":         "uuid",
    "participant_id":     "uuid",
    "bridge_leg_id":      "uuid",
    "disconnect_reason":  "SIP_BYE | RTP_TIMEOUT | NETWORK_DROP",
    "reconnect_window_ms": 30000,
    "speaking_duration_accumulated_ms": 45000,
    "timestamp":          "ISO8601"
  }
}
```

### `pstn_bridge.participant.reconnected`
```json
{
  "event_type": "pstn_bridge.participant.reconnected",
  "payload": {
    "session_id":      "uuid",
    "participant_id":  "uuid",
    "new_bridge_leg_id": "uuid",
    "reconnect_latency_ms": 18000,
    "timestamp":       "ISO8601"
  }
}
```

### `pstn_bridge.quality.degraded`
```json
{
  "event_type": "pstn_bridge.quality.degraded",
  "payload": {
    "session_id":      "uuid",
    "participant_id":  "uuid",
    "bridge_leg_id":   "uuid",
    "mos_score":       2.3,
    "severity":        "DEGRADED | POOR | UNINTELLIGIBLE",
    "codec_switched_to": "PCMU",
    "timestamp":       "ISO8601"
  }
}
```

### `pstn_bridge.mute.timeout`
```json
{
  "event_type": "pstn_bridge.mute.timeout",
  "payload": {
    "session_id":      "uuid",
    "participant_id":  "uuid",
    "bridge_leg_id":   "uuid",
    "action_attempted": "MUTE",
    "safe_default_applied": true,
    "timestamp":       "ISO8601",
    "severity":        "CRITICAL"
  }
}
```

### `pstn_bridge.session.terminated`
```json
{
  "event_type": "pstn_bridge.session.terminated",
  "payload": {
    "session_id":          "uuid",
    "participants_bridged": 2,
    "avg_mos_score":       3.1,
    "total_reconnects":    1,
    "termination_reason":  "SESSION_COMPLETED | BRIDGE_FAILURE | ADMIN",
    "timestamp":           "ISO8601"
  }
}
```

### `pstn_bridge.fallback.triggered`
```json
{
  "event_type": "pstn_bridge.fallback.triggered",
  "payload": {
    "session_id":      "uuid",
    "participant_id":  "uuid",
    "fallback_from":   "WEBRTC_DIRECT | TURN_RELAY",
    "fallback_reason": "WEBRTC_HANDSHAKE_TIMEOUT | TURN_UNREACHABLE | BANDWIDTH_TOO_LOW",
    "sms_sent":        true,
    "timestamp":       "ISO8601"
  }
}
```

## Events This Agent Consumes

| Kafka Topic | Action |
|---|---|
| `gd.batch.created` | Generate PINs for PSTN-eligible participants |
| `dojo.match.created` | Generate PINs for PSTN-eligible participants |
| `interview.slot.booked` | Generate PINs for PSTN-eligible participants |
| `gd.bridge.mute.commands` | Translate to FreeSWITCH ESL mute/unmute |
| `gd.session.completed` | Terminate all bridge legs for session |
| `dojo.match.completed` | Terminate all bridge legs for match |
| `interview.completed` | Terminate all bridge legs for interview |
| `pstn_bridge.fallback.requested` | Immediate SMS delivery of bridge PIN |
| `admin.bridge.terminate` | Emergency bridge termination |

---

---

# SECTION XIV — PRIVACY & AUDIO NON-RECORDING POLICY (LOCKED)

## Absolute Rules

```
1. PSTN AUDIO IS NEVER RECORDED.
   FreeSWITCH recording modules are DISABLED for all bridge channels.
   The pstn_bridge_sessions.audio_recorded field is constrained to FALSE by DB constraint.
   Any service that requests audio recording from bridge channels → BLOCKED → AUDIT VIOLATION

2. CALLER ID IS NEVER STORED IN PLAINTEXT.
   audit_log.caller_id_masked stores only the LAST 4 DIGITS of the caller ID.
   Full caller ID used only in-memory during authentication, never persisted.

3. DTMF DIGITS (PIN) ARE NEVER STORED IN PLAINTEXT.
   pstn_bridge_sessions.pin_issued stores bcrypt hash of the PIN, never plaintext.
   PIN exists in Redis as plaintext ONLY for the duration of the session — deleted on session end.

4. AUDIO CONTENT IS NEVER ANALYZED.
   No speech-to-text, no sentiment analysis, no accent detection on PSTN audio.
   Only RTP energy levels (binary signal: speaking/not-speaking) are measured.
   This is identical to what the GD spec states for WebRTC audio.

5. PSTN AUDIO BRIDGE IS TRANSPORT-ONLY.
   FreeSWITCH is configured as: RECEIVE → ROUTE TO JITSI → DISCARD.
   No audio is buffered beyond the RTP buffer required for real-time delivery.
```

**Violation of any privacy rule above → IMMEDIATE STOP EXECUTION → SECURITY INCIDENT LOG**

---

---

# SECTION XV — FREESWITC H & KAMAILIO CONFIGURATION LOCK

## FreeSWITCH Mandatory Modules (LOCKED)

```xml
<!-- Enabled modules — locked -->
<load module="mod_sofia"/>           <!-- SIP stack -->
<load module="mod_dptools"/>         <!-- Dialplan tools -->
<load module="mod_event_socket"/>    <!-- ESL for agent control -->
<load module="mod_opus"/>            <!-- Opus codec -->
<load module="mod_g711"/>            <!-- PCMU/ALAW fallback -->
<load module="mod_tone_detect"/>     <!-- DTMF detection -->
<load module="mod_ivr"/>             <!-- IVR prompts -->
<load module="mod_commands"/>        <!-- API commands -->

<!-- Explicitly DISABLED modules — recording MUST be off -->
<!-- mod_record_session: NOT LOADED -->
<!-- mod_spy: NOT LOADED -->
<!-- mod_audio_stream: NOT LOADED -->
```

## Kamailio SIP Proxy Rules (LOCKED)

```
1. All inbound PSTN INVITEs → authentication against PSTN_BRIDGE_CONTROL_AGENT
2. Authenticated calls only → routed to FreeSWITCH
3. Unauthenticated calls → rejected with 403 Forbidden
4. Call rate limit per DID: 100 concurrent calls maximum
5. SIP REGISTER from unknown UACs → rejected
6. All SIP traffic: internal only — never exposed to public internet
7. Kamailio must be in k3s realtime namespace behind NGINX ingress
```

---

---

# SECTION XVI — OBSERVABILITY (NON-OPTIONAL)

## Prometheus Metrics

```
# HELP pstn_bridge_sessions_total Total PSTN bridge sessions initiated
# TYPE pstn_bridge_sessions_total counter
pstn_bridge_sessions_total{session_type, region, codec, bridge_state, tenant_id}

# HELP pstn_bridge_mos_score MOS score per bridge leg
# TYPE pstn_bridge_mos_score histogram
pstn_bridge_mos_score{region, codec}
  buckets: [1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0]

# HELP pstn_bridge_setup_latency_ms Bridge setup latency
# TYPE pstn_bridge_setup_latency_ms histogram
pstn_bridge_setup_latency_ms{region}
  buckets: [500, 1000, 2000, 3000, 5000, 8000, 15000]

# HELP pstn_bridge_mute_latency_ms Mute command execution latency
# TYPE pstn_bridge_mute_latency_ms histogram
pstn_bridge_mute_latency_ms{}
  buckets: [10, 50, 100, 200, 500, 1000, 2000]

# HELP pstn_bridge_fallbacks_total PSTN fallback triggers
# TYPE pstn_bridge_fallbacks_total counter
pstn_bridge_fallbacks_total{fallback_from, region}

# HELP pstn_bridge_active_legs Current active bridge legs
# TYPE pstn_bridge_active_legs gauge
pstn_bridge_active_legs{region, codec}

# HELP pstn_bridge_mute_timeouts_total Mute command timeouts
# TYPE pstn_bridge_mute_timeouts_total counter
pstn_bridge_mute_timeouts_total{}

# HELP pstn_bridge_reconnects_total Bridge reconnection events
# TYPE pstn_bridge_reconnects_total counter
pstn_bridge_reconnects_total{region}

# HELP pstn_bridge_sip_errors_total SIP error responses
# TYPE pstn_bridge_sip_errors_total counter
pstn_bridge_sip_errors_total{error_code, region}

# HELP pstn_bridge_freeswitch_reachable FreeSWITCH ESL reachability
# TYPE pstn_bridge_freeswitch_reachable gauge
pstn_bridge_freeswitch_reachable{} (1 = reachable, 0 = unreachable)
```

## Grafana Dashboards (Mandatory)

- **Bridge Health Overview** — Active legs, setup latency P99, MOS distribution per region
- **Mute Command Integrity** — Mute latency P99, timeout count, safe-default invocations
- **Fallback Funnel** — WebRTC → TURN → PSTN fallback rates per region and session type
- **Audio Quality Heatmap** — MOS score per region per hour — identifies consistently poor trunks
- **Reconnect Tracker** — Reconnect events, window success rates, permanent exits
- **PIN Delivery Success** — SMS delivery rate via Jasmin per region
- **SIP Error Timeline** — Error code distribution, trunk health

## Alerting Rules (All Mandatory)

| Alert | Condition | Severity | Action |
|---|---|---|---|
| `PStnBridgeMuteTimeout` | `mute_timeouts > 0` in 5m | CRITICAL | Bridge mute integrity check |
| `PStnBridgeFreeSWITCHDown` | `freeswitch_reachable == 0` for 30s | CRITICAL | Page on-call — all PSTN joins will fail |
| `PStnBridgeMOSCritical` | `p10_mos_score < 1.5` in 10m | HIGH | Trunk quality investigation |
| `PStnBridgeSetupTimeout` | `p99_setup_latency > 8000ms` | HIGH | SIP trunk or Jitsi bridge latency |
| `PStnBridgeHighFallbackRate` | `fallbacks > 30%` of sessions in 15m | HIGH | Network degradation event |
| `PStnBridgePINDeliveryFail` | `sms_delivered == false` for any participant | MEDIUM | Jasmin gateway check |
| `PStnBridgeRecordingAttempt` | `audio_recorded == TRUE` in any DB record | CRITICAL + SECURITY | Immediate incident — privacy violation |

---

---

# SECTION XVII — SECURITY CONSTRAINTS (LOCKED)

- FreeSWITCH ESL is **internal-only** — accessible only from agent pod, never from internet
- Kamailio SIP proxy is **internal-only** — never exposed externally
- PSTN DID numbers are **Vault-stored** — never in environment variables or config maps
- PIN values in PostgreSQL are **bcrypt-hashed** — plaintext never persists
- Caller ID in audit log is **last-4-digits-only masked** — no full MSISDN stored
- `mod_record_session` is **disabled** at FreeSWITCH compile time or config — no runtime override permitted
- Bridge participant voice data **never traverses the backend application layer** — audio path is PSTN → FreeSWITCH → Jitsi SFU only
- Agent-to-FreeSWITCH communication uses **mTLS** — no plain socket
- All `/internal/pstn-bridge/*` endpoints are **Kong-blocked from external access**
- Wazuh SIEM receives: mute timeout alerts, recording attempt alerts, PIN brute-force detection (>3 failed PINs from same caller_id in 60s → caller blocked)

---

---

# SECTION XVIII — DEPLOYMENT SPECIFICATION (LOCKED)

## Kubernetes Namespace

```
Namespace: realtime
Service Name: pstn-bridge-control-agent
Co-located with: gd-orchestrator, interview-service, dojo-match-engine
```

## FreeSWITCH Deployment

```yaml
Namespace: realtime
Service Name: freeswitch
replicas: 2 (active-passive HA)
resources:
  requests: { cpu: "500m", memory: "512Mi" }
  limits:   { cpu: "2000m", memory: "2Gi" }
hostNetwork: true  # Required for RTP port binding
ports: [5060/UDP (SIP), 5061/TCP (SIP-TLS), 16384-32768/UDP (RTP range)]
```

## Kamailio Deployment

```yaml
Namespace: realtime
Service Name: kamailio
replicas: 2
resources:
  requests: { cpu: "200m", memory: "256Mi" }
  limits:   { cpu: "500m", memory: "512Mi" }
hostNetwork: true  # Required for SIP UDP
ports: [5060/UDP (SIP)]
```

## Agent Deployment

```yaml
Namespace: realtime
Service Name: pstn-bridge-control-agent
replicas: 2
autoscaling:
  minReplicas: 2
  maxReplicas: 6
  targetCPUUtilizationPercentage: 60
resources:
  requests: { cpu: "100m", memory: "192Mi" }
  limits:   { cpu: "400m", memory: "384Mi" }
```

## Environment Variables Required

```
FREESWITCH_ESL_HOST
FREESWITCH_ESL_PORT         # Default: 8021
FREESWITCH_ESL_PASSWORD     # From Vault
KAMAILIO_HOST
POSTGRES_HOST
POSTGRES_DB
POSTGRES_USER
POSTGRES_PASSWORD           # From Vault
REDIS_HOST
REDIS_PASSWORD              # From Vault
KAFKA_BROKER_URL
JASMIN_SMS_API_URL
JASMIN_SMS_API_KEY          # From Vault
KEYCLOAK_JWKS_URL
JITSI_INTERNAL_SIP_HOST     # Internal Jitsi SIP endpoint
PSTN_BRIDGE_PIN_RECONNECT_WINDOW_STANDARD_SEC  # Default: 30
PSTN_BRIDGE_PIN_RECONNECT_WINDOW_INTERVIEW_SEC # Default: 60
PSTN_BRIDGE_SETUP_TIMEOUT_MS                   # Default: 15000
PSTN_BRIDGE_MUTE_TIMEOUT_MS                    # Default: 500
LOG_LEVEL
```

## Health Check Endpoints

```
GET /health/live   → 200 OK if agent process alive
GET /health/ready  → 200 OK if:
                      FreeSWITCH ESL reachable AND
                      Kamailio SIP reachable AND
                      Redis reachable AND
                      PostgreSQL reachable AND
                      Jasmin SMS API reachable
```

**ALL five dependencies must be healthy for `/health/ready` to return 200.**

---

---

# SECTION XIX — INTERN EXECUTION GUIDE

## Service Location

```
/backend/services/pstn_bridge_control_agent/
/infra/freeswitch/
/infra/kamailio/
```

## Step-by-Step Local Execution

**Step 1:** Start FreeSWITCH (Docker)
```bash
docker run -d \
  --name freeswitch-dev \
  --network host \
  -v $(pwd)/infra/freeswitch/conf:/etc/freeswitch \
  -p 5060:5060/udp \
  -p 8021:8021 \
  -p 16384-16484:16384-16484/udp \
  signalwire/freeswitch:v1.10
```

**Step 2:** Verify FreeSWITCH ESL
```bash
# Connect to ESL console
fs_cli -H 127.0.0.1 -P 8021 -p ClueCon
# Expected: freeswitch@localhost>
# Test: api status
# Expected: UP X days...
```

**Step 3:** Start Kamailio (Docker)
```bash
docker run -d \
  --name kamailio-dev \
  --network host \
  -v $(pwd)/infra/kamailio/conf:/etc/kamailio \
  kamailio/kamailio:5.7
```

**Step 4:** Start agent
```bash
cd /backend/services/pstn_bridge_control_agent/
python -m venv venv && source venv/bin/activate
pip install -r requirements.txt
cp .env.example .env
# Edit .env with local FreeSWITCH, Redis, Postgres, Jasmin values
alembic upgrade head
uvicorn main:app --reload --port 8093
```

**Expected Output:**
```
INFO: PSTN_BRIDGE_CONTROL_AGENT ready on port 8093
INFO: FreeSWITCH ESL: CONNECTED (127.0.0.1:8021)
INFO: Kamailio SIP: REACHABLE
INFO: Redis: CONNECTED
INFO: PostgreSQL: CONNECTED
INFO: Jasmin SMS: CONNECTED
INFO: Kafka consumer: SUBSCRIBED to [gd.batch.created, gd.bridge.mute.commands, ...]
```

**Step 5:** Simulate PSTN join
```bash
# Issue PIN for test session
curl -X POST http://localhost:8093/internal/pstn-bridge/generate-pin \
  -H "Content-Type: application/json" \
  -d '{"session_id":"test-001","participant_id":"user-001","session_type":"GD_SESSION"}'

# Simulate FreeSWITCH auth call (as if candidate dialed in)
curl -X POST http://localhost:8093/internal/pstn-bridge/authenticate \
  -d '{"caller_id":"+919999999999","dtmf_digits":"<PIN>","inbound_did":"+911234567890","freeswitch_uuid":"leg-001","timestamp":...}'
```

**Expected:** AUTHENTICATED response with jitsi_sip_uri
**If authentication fails with valid PIN → CRITICAL BUG → STOP EXECUTION**

---

---

# SECTION XX — CONTRACT GATE STATUS

| Contract | Status |
|---|---|
| Technology Stack Lock | LOCKED |
| Three-Layer Connectivity Fallback | LOCKED |
| Bridge Session Lifecycle (5 Phases) | LOCKED |
| PIN Generation & TTL Policy | LOCKED |
| DTMF Authentication Protocol | LOCKED |
| Mute/Unmute Command Architecture | LOCKED |
| Scoring Parity Enforcement | LOCKED |
| PSTN Quality Penalty Policy | LOCKED |
| Reconnect Window Policy | LOCKED |
| Regional Bridge Number Schema | LOCKED |
| Privacy & Audio Non-Recording Policy | LOCKED |
| FreeSWITCH Module Lock | LOCKED |
| Kamailio SIP Proxy Rules | LOCKED |
| Database Schema | LOCKED |
| API Interface Contract | LOCKED |
| Event Bus Contracts | LOCKED |
| Observability Metrics + Alerts | LOCKED |
| Security Constraints | LOCKED |
| Deployment Specification | LOCKED |

**➤ ALL CONTRACT GATES PASSED**
**➤ AGENT STATUS: SEALED · LOCKED · DEPLOYMENT-READY**

---

---

# SECTION XXI — BOUND LAWS & CROSS-REFERENCES

| Law / Spec | Binding Clause |
|---|---|
| R1 — Technology Stack Lock | FreeSWITCH, Kamailio, coturn, Jasmin all self-hosted; no paid SaaS telephony |
| R4 — Event Schema Registry | All bridge events registered in AsyncAPI before deployment |
| R5 — Workflow State Machines | Bridge session lifecycle is a governed R5 state machine |
| R39A — Session Lifecycle Manager | PSTN bridge session is a sub-session of the GD/Dojo/Interview session |
| R44 — Runnable Backend Law | Agent fully runnable with FreeSWITCH + Kamailio + Redis + PostgreSQL |
| R49 — Contract Validator CLI | All bridge contracts validated before every build |
| R59 — Offline-First & Low-Bandwidth Law | PSTN bridge IS the implementation of R59 for voice sessions — absence = R59 violation |
| R60 — Archival Law | Bridge audit log under 3-year retention, WORM-style |
| GD System Spec — Section 13 (Failure Handling) | Network drop → token skipped; PSTN reconnect window = extension of this rule |
| GD System Spec — Section 9 (Turn-Based Speaking Engine) | Bridge participant receives identical token governance |
| GD System Spec — Section 12 (Scoring) | PSTN participants scored on identical rule-based formula |
| GD System Spec — Section 11 (No Audio Storage) | PSTN audio never recorded — constraint enforced at FreeSWITCH config level |
| Dojo SAAS v1.0 — Section F (Scoring Governance) | PSTN Dojo participants scored identically via event parity |
| Society Architecture — Offline Franchise Model | Society rural zones are primary PSTN bridge target population |
| Society Architecture — CouchDB Offline Layer | PSTN bridge operates independently of CouchDB sync — separate offline path for voice |
| Ecoskiller Services — Jasmin SMS Gateway | PIN delivery via Jasmin is mandatory integration |
| Ecoskiller Services — coturn | coturn TURN relay is Layer 2 before PSTN fallback |
| Infrastructure Audit v8 | FreeSWITCH + Kamailio on k3s — self-hosted confirmed |
| PHONE_EVENT_DEDUP_AGENT v1.0 | Bridge participant events are deduplicated before scoring |
| PHONE_RACE_CONDITION_GUARD v1.0 | Bridge seat claim in GD batch is race-guarded |
| PHONE_SCORE_FREEZE_AGENT v1.0 | Bridge participant scores are freeze-governed after session end |

---

---

# SECTION XXII — ANTIGRAVITY LAYER DECLARATION (LOCKED)

## Why the PSTN Bridge Is the Fourth Pillar

The first three Antigravity agents protect **session and scoring integrity** after a participant is already connected. This agent protects **the right to participate at all** — regardless of network quality.

```
ECOSKILLER CONNECTIVITY PROMISE (LOCKED):

  "No candidate is excluded from assessment due to network conditions.
   Voice quality degrades gracefully.
   Participation is never silently denied.
   The protocol is identical for all transport channels."

Without PSTN_BRIDGE_CONTROL_AGENT:
  → Rural candidates on 2G = silently excluded
  → Feature phone users = structurally barred
  → Society offline franchise candidates = unable to participate
  → R59 (Offline-First & Low-Bandwidth Law) = violated in voice domain
  → Campus rural hiring = operationally broken

With PSTN_BRIDGE_CONTROL_AGENT:
  → 2G candidate dials in → participates under identical rules
  → Feature phone user dials in → speaking token enforced via DTMF mute
  → Rural society officer coordinates offline cohort → PSTN bridge carries their voice
  → R59 mandate enforced in voice session domain
  → Rural-first platform claim is architecturally true
```

**This agent completes the Antigravity Layer.**
**Without it, the platform serves cities. With it, the platform serves India.**

---

---

# 🔒 FINAL SEAL

```
AGENT:       PSTN_BRIDGE_CONTROL_AGENT
DOMAIN:      PSTN & Bridge → Antigravity Layer
SYSTEM:      ECOSKILLER v12.0
STATUS:      SEALED
VERSION:     v1.0
COMPANIONS:  PHONE_EVENT_DEDUPLICATION_AGENT v1.0
             PHONE_RACE_CONDITION_GUARD_AGENT v1.0
             PHONE_SCORE_FREEZE_AGENT v1.0
MUTATION:    ADD-ONLY VIA VERSION BUMP
AUTHORITY:   HUMAN DECLARATION ONLY

NO CLAUSE MAY BE WEAKENED.
NO AUDIO MAY EVER BE RECORDED.
NO PAID TELEPHONY SAAS MAY BE SUBSTITUTED.
NO PSTN PARTICIPANT MAY RECEIVE DIFFERENTIAL SCORING.
FAIL-SAFE MODE: BRIDGE FAILURE = SPECTATOR, NOT SESSION ABORT.
MUTE TIMEOUT = FORCED MUTE (SAFETY DEFAULT), NOT UNMUTED.
R59 OFFLINE-FIRST LAW COMPLIANCE IS NON-NEGOTIABLE.

Violation → STOP EXECUTION → REPORT PSTN_BRIDGE_SEAL_BREACH
```

---

*PSTN_BRIDGE_CONTROL_AGENT · ECOSKILLER PSTN & Bridge Domain · Antigravity Layer · v1.0 · SEALED*
