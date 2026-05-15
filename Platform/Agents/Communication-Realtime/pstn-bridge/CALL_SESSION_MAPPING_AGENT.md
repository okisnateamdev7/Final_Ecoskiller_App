# CALL_SESSION_MAPPING_AGENT
## PSTN & Bridge · Ecoskiller SaaS Platform

```
Status:              SEALED · LOCKED · APPEND-ONLY · NON-NEGOTIABLE
Version:             v1.0
Mutation Policy:     ADD-ONLY via version bump — no modification, no reinterpretation
Interpretation Authority: NONE
Execution Authority: Human declaration only
Execution Engine:    ANTIGRAVITY
Change Policy:       APPEND_ONLY
Parent Domain:       PSTN & Bridge
Sister Domain:       Session & Lifecycle
Depends On:          PHONE_APPEND_ONLY_ENFORCEMENT_AGENT v1.0
                     PHONE_PIPELINE_COMPLETENESS_AGENT v1.0
                     SESSION-COMP-v1 (Session Management Compliance)
```

---

## ⚠️ SEAL DECLARATION

This agent prompt is **sealed and locked**.

Antigravity MUST NOT:
- Allow a PSTN call to join a voice room without a confirmed call_session_map record
- Allow a call_session_map record to be created without a verified phone anchor
- Reuse any call_id across sessions, rooms, or tenants
- Allow cross-room, cross-tenant, or cross-domain call bridging
- Bridge any call whose phone number is unverified, blocked, or absent from the phone ledger
- Permit a stale or expired call token to authenticate into a live media room
- Allow a PSTN participant to acquire GD turn-speaking rights without explicit orchestrator grant
- Silently continue after any bridge failure — always STOP → REPORT → TERMINATE

Violation of any rule in this document → **STOP EXECUTION → REPORT CALL_SESSION_MAP_VIOLATION → TERMINATE BRIDGE SESSION**

---

## 1. AGENT IDENTITY

```
Agent Name:     CALL_SESSION_MAPPING_AGENT
Domain:         PSTN & Bridge
System:         Ecoskiller — Unified Job + Skill + Project + Education + Marketplace SaaS
Layer:          PSTN Bridge Service · Voice GD Orchestrator · Dojo Match Engine ·
                Interview Service · Media Stack (Jitsi / LiveKit / coturn)
Execution Mode: Deterministic · Rule-Driven · No Discretion · No Silent Fallback
```

This agent governs **all PSTN call-to-session mappings** across the Ecoskiller platform.

It enforces a single, non-negotiable architectural law:

> **Every inbound or outbound PSTN call must be deterministically mapped to exactly one active
> platform session. The mapping is immutable after creation, auditable, tenant-isolated,
> domain-locked, and automatically terminated when the session ends.**

No call may exist without a session map.
No session map may exist without a verified phone anchor.
No session map may span across rooms, tenants, or domains.

---

## 2. CORE BRIDGE PHILOSOPHY

```
Replace ad-hoc dial-in        → with deterministic call_session_map binding
Replace anonymous PSTN join   → with phone-ledger-verified participant identity
Replace shared call numbers   → with session-scoped, single-use bridge tokens
Replace manual room routing   → with automated session-to-room resolution
Replace open bridge endpoints → with domain-locked, tenant-scoped ingress gates
Replace silent call drops     → with failure-classified, audited termination events
```

The agent operates only on:

- **Identity** (is this caller's phone verified in the phone ledger?)
- **Mapping** (which session, room, and tenant does this call belong to?)
- **State** (what is the current lifecycle state of this call-session binding?)
- **Enforcement** (is this call permitted to speak, muted, or terminated per orchestrator rules?)
- **Audit** (is every call lifecycle event immutably logged?)

The agent does **not** operate on:
- GD topic assignment or scoring logic
- Interview scheduling or slot management
- Billing computation

---

## 3. PSTN & BRIDGE ARCHITECTURE (NON-NEGOTIABLE)

### 3.1 Architectural Separation of Responsibilities

```
LAYER                   RESPONSIBILITY
────────────────────────────────────────────────────────────────────────────────
PSTN Ingress            Receive inbound call, extract caller_id (E.164)
Phone Ledger Gate       Verify caller_id exists as verified in user_phone_ledger
Session Resolver        Map caller_id + bridge_token → active session_id + room_id
Call-Session Mapper     Create immutable call_session_map record
Media Bridge            Inject PSTN audio stream into Jitsi/LiveKit room
GD/Dojo Orchestrator    Grant or deny speaking token to PSTN participant
WebSocket Channel       Relay mute/unmute commands to bridge participant
Audit Writer            Immutably log every call-session lifecycle event
```

### 3.2 Critical Architectural Rules (LOCKED)

```
RULE-1: Media never touches backend.
  PSTN audio is injected directly into Jitsi/LiveKit SFU.
  Backend issues bridge tokens only — it never handles raw audio.

RULE-2: Backend decides everything.
  Jitsi does not decide mute state.
  LiveKit does not decide turn order.
  The GD/Dojo Orchestrator commands the bridge via WebSocket.

RULE-3: Rooms = session_id / match_id.
  A PSTN call's room target is derived from session_id or match_id only.
  No hardcoded room names. No guessable room identifiers.

RULE-4: One call → one session map → one room.
  A single PSTN call may never be bridged to more than one room simultaneously.
  A single room may accept PSTN participants only within the declared capacity.

RULE-5: Cross-domain bridging is FORBIDDEN.
  A GD session in domain_track = commerce cannot accept a PSTN participant
  whose user account is domain_track = arts.

RULE-6: Cross-tenant bridging is FORBIDDEN.
  Tenant A's bridge token cannot resolve to Tenant B's session.
```

### 3.3 Traffic Class Declaration

```
This agent handles Traffic Class 2 (WebSocket commands) and
Traffic Class 3 (Media — WebRTC/Jitsi/LiveKit — never through API).

Traffic Class 1 (HTTP CRUD): call_session_map creation, token issuance, status query.
Traffic Class 2 (WebSocket): mute/unmute relay to PSTN bridge participant.
Traffic Class 3 (Media): PSTN audio injected directly into Jitsi/LiveKit SFU.
Traffic Class 4 (Async): call lifecycle events published to Kafka.

Media traffic MUST NEVER route through API gateway.
```

---

## 4. CALL-SESSION MAP DATA CONTRACT (LOCKED)

### 4.1 Primary Schema

```sql
-- Call-Session Map Table (APPEND-ONLY for lifecycle events, see §4.2)
CREATE TABLE call_session_map (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    call_id             UUID NOT NULL UNIQUE,        -- Immutable, cryptographically generated per call
    session_id          UUID NOT NULL,               -- FK → active session (GD / Interview / Dojo)
    session_type        TEXT NOT NULL                -- ENUM: gd | interview | dojo_match
                        CHECK (session_type IN ('gd','interview','dojo_match')),
    room_id             TEXT NOT NULL,               -- Jitsi room name OR LiveKit room name
    room_type           TEXT NOT NULL                -- ENUM: jitsi | livekit
                        CHECK (room_type IN ('jitsi','livekit')),
    tenant_id           UUID NOT NULL,               -- Tenant isolation — hard enforced
    domain_track        TEXT NOT NULL,               -- ENUM: arts | commerce | science | others
    user_id             UUID NOT NULL,               -- Resolved from phone_ledger
    phone_ledger_id     UUID NOT NULL,               -- FK → user_phone_ledger.id (verified record)
    caller_e164         TEXT NOT NULL,               -- E.164 of inbound/outbound PSTN number
    bridge_token        TEXT NOT NULL UNIQUE,        -- Short-lived signed token issued to PSTN bridge
    bridge_token_issued_at   TIMESTAMPTZ NOT NULL,
    bridge_token_expires_at  TIMESTAMPTZ NOT NULL,   -- Max TTL: 300 seconds from issue
    bridge_token_used_at     TIMESTAMPTZ,            -- Nulled until bridge joins room
    call_state          TEXT NOT NULL DEFAULT 'pending'
                        CHECK (call_state IN (
                            'pending',       -- Token issued, call not yet connected
                            'connected',     -- PSTN call in room, audio bridged
                            'muted',         -- Forced mute by orchestrator
                            'speaking',      -- Speaking token active
                            'suspended',     -- Temporarily suspended (network drop)
                            'terminated',    -- Call ended normally
                            'failed',        -- Bridge failure
                            'evicted'        -- Forcibly removed by orchestrator
                        )),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    connected_at        TIMESTAMPTZ,
    terminated_at       TIMESTAMPTZ,
    termination_reason  TEXT,                        -- ENUM: session_ended | timeout | eviction |
                                                     --       failure | user_hangup | duplicate
    speaking_turns_completed  INTEGER DEFAULT 0,
    speaking_turns_skipped    INTEGER DEFAULT 0,
    interrupt_attempts        INTEGER DEFAULT 0,
    mic_open_seconds          INTEGER DEFAULT 0,
    silence_during_token_sec  INTEGER DEFAULT 0,
    network_drops             INTEGER DEFAULT 0
);

-- Row-Level Security: tenant isolation
ALTER TABLE call_session_map ENABLE ROW LEVEL SECURITY;

-- Indexes
CREATE INDEX idx_csm_session_id     ON call_session_map(session_id);
CREATE INDEX idx_csm_user_id        ON call_session_map(user_id);
CREATE INDEX idx_csm_call_state     ON call_session_map(call_state);
CREATE INDEX idx_csm_tenant_id      ON call_session_map(tenant_id);
CREATE UNIQUE INDEX idx_csm_call_id ON call_session_map(call_id);
```

### 4.2 Lifecycle Event Ledger (Append-Only Audit)

```sql
-- Immutable lifecycle event log — append only, no UPDATE, no DELETE
CREATE TABLE call_session_events (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    call_id         UUID NOT NULL REFERENCES call_session_map(call_id),
    event_type      TEXT NOT NULL,   -- See §4.3 for exhaustive event list
    event_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
    payload         JSONB,           -- Event-specific metadata
    initiated_by    TEXT NOT NULL,   -- ENUM: system | orchestrator | user | admin
    tenant_id       UUID NOT NULL
);

-- Hard lock: no UPDATE, no DELETE permitted on event log
REVOKE UPDATE ON call_session_events FROM ecoskiller_app_role;
REVOKE DELETE ON call_session_events FROM ecoskiller_app_role;
```

### 4.3 Exhaustive Call Lifecycle Event Types (LOCKED)

```
EVENT TYPE                      TRIGGER
────────────────────────────────────────────────────────────────────────────────
call.bridge.token_issued        Bridge token generated for PSTN participant
call.bridge.connected           PSTN audio successfully injected into room
call.bridge.muted               Orchestrator forced mute on PSTN participant
call.bridge.unmuted             Orchestrator granted speaking token to PSTN participant
call.bridge.speaking_started    PSTN participant mic opened (speaking turn started)
call.bridge.speaking_ended      Speaking token expired or completed
call.bridge.interrupt_attempt   PSTN participant attempted to speak out of turn
call.bridge.silence_detected    Token held but no audio received
call.bridge.network_drop        PSTN connection interrupted mid-session
call.bridge.reconnect_denied    Rejoin attempt after disconnect — denied
call.bridge.suspended           Temporary suspension pending reconnect window
call.bridge.token_expired       Bridge token expired before call connected
call.bridge.duplicate_detected  Second call attempt for same session from same user
call.bridge.evicted             Orchestrator forcibly removed participant
call.bridge.session_terminated  Parent session ended — bridge auto-closed
call.bridge.failed              Bridge failure (SFU error, coturn failure)
call.bridge.admin_override      Admin-initiated bridge termination

Unknown event type → REPORT UNKNOWN_EVENT_TYPE → STOP
```

### 4.4 Uniqueness & Integrity Rules

```
call_id:         Unique globally — never reused across sessions, tenants, or time
bridge_token:    Unique globally — single-use, expires 300 seconds from issue
session_id:      One PSTN participant may hold at most one active call_session_map per session
user_id:         Same user may not have two 'connected' or 'speaking' maps in same session
phone_ledger_id: Must reference a 'verified' record in user_phone_ledger
caller_e164:     Must match the E.164 of the referenced phone_ledger_id record
room_id:         Must correspond to an active, non-expired session room
tenant_id:       Must match the tenant_id of the referenced session_id
domain_track:    Must match the domain_track of the user's active session
```

---

## 5. CALL-SESSION MAPPING LIFECYCLE (DETERMINISTIC)

### 5.1 Phase 0 — Pre-Bridge Gate (Phone Ledger Check)

```
FUNCTION pre_bridge_gate(caller_e164, session_id, bridge_token):

  1. Resolve phone_ledger_record:
     SELECT * FROM user_phone_ledger
     WHERE phone_e164 = caller_e164
       AND status = 'verified'
       AND tenant_id = resolve_tenant(session_id)

  IF NOT FOUND:
    → DENY BRIDGE ENTRY
    → EMIT call.bridge.failed { reason: NO_VERIFIED_PHONE }
    → RETURN { allowed: false, reason: NO_VERIFIED_PHONE }

  2. Verify bridge_token:
     - Signature valid
     - Not expired (now() < bridge_token_expires_at)
     - Not already used (bridge_token_used_at IS NULL)
     - Belongs to this session_id

  IF token invalid / expired / used:
    → DENY BRIDGE ENTRY
    → EMIT call.bridge.token_expired OR call.bridge.failed
    → RETURN { allowed: false, reason: TOKEN_INVALID }

  3. Check domain isolation:
     user.domain_track must match session.domain_track

  IF mismatch:
    → DENY BRIDGE ENTRY
    → EMIT call.bridge.failed { reason: DOMAIN_MISMATCH }
    → RETURN { allowed: false, reason: DOMAIN_MISMATCH }

  4. Check for existing active map (duplicate detection):
     IF active call_session_map exists for (user_id, session_id):
       → DENY BRIDGE ENTRY
       → EMIT call.bridge.duplicate_detected
       → RETURN { allowed: false, reason: DUPLICATE_BRIDGE_ATTEMPT }

  → PERMIT BRIDGE ENTRY
  → PROCEED TO PHASE 1
```

### 5.2 Phase 1 — Call-Session Map Creation

```
FUNCTION create_call_session_map(caller_e164, session_id, bridge_token):

  BEGIN TRANSACTION

  1. Generate call_id = crypto_uuid()
  2. Resolve room_id from session_id (GD batch / Interview slot / Dojo match_id)
  3. Resolve room_type from session_type (jitsi | livekit)
  4. INSERT INTO call_session_map (all fields, call_state = 'pending')
  5. Write call.bridge.token_issued to call_session_events
  6. Mark bridge_token_used_at = now() (single-use consumed)

  COMMIT

  IF commit fails:
    → ROLLBACK
    → EMIT call.bridge.failed { reason: MAP_CREATION_FAILURE }
    → STOP

  → RETURN { call_id, room_id, room_type, call_state: 'pending' }
```

### 5.3 Phase 2 — Bridge Connection (PSTN Audio Injection)

```
On PSTN call connected to SFU:

  1. UPDATE call_session_map SET
       call_state = 'connected',
       connected_at = now()
     WHERE call_id = ?

  2. Write call.bridge.connected to call_session_events

  3. Notify GD/Dojo Orchestrator via WebSocket:
     { event: 'pstn_participant_joined', call_id, session_id, user_id, room_id }

  4. Orchestrator adds PSTN participant to:
     - Speaking queue (if GD turn-based)
     - Participant state machine (Redis)

  Default state on join: MUTED
  PSTN participant NEVER auto-unmuted on connect.
  First unmute requires explicit orchestrator grant only.
```

### 5.4 Phase 3 — Orchestrator-Controlled Speaking (PSTN in GD)

```
Turn-Based Speaking Rules for PSTN Participants:

  PSTN participants participate in the SAME turn queue as WebRTC participants.
  Sorting: by joined_at (call_session_map.connected_at for PSTN)
  
  On speaking token grant:
    Orchestrator → WebSocket → Bridge Service → Jitsi/LiveKit API → force_unmute(call_id)
    Orchestrator → UPDATE call_session_map SET call_state = 'speaking'
    Orchestrator → Write call.bridge.unmuted + call.bridge.speaking_started
    Countdown timer: identical to WebRTC participants (Intro: 60s, Rebuttal: 30s, Conclusion: 45s)
  
  On speaking token expiry:
    Bridge Service → Jitsi/LiveKit API → force_mute(call_id)
    Orchestrator → UPDATE call_session_map SET call_state = 'muted'
    Write call.bridge.speaking_ended
    Increment speaking_turns_completed

  On interrupt attempt (PSTN participant audio detected while muted):
    Detected by SFU audio activity monitor
    → Bridge Service enforces mute re-assertion
    → Increment interrupt_attempts
    → Write call.bridge.interrupt_attempt
    → Score penalized per GD scoring rules

  On silence during token (no audio during open mic window):
    Detected by SFU audio level monitor (threshold: < 10% of mean level for >5s)
    → Increment silence_during_token_sec
    → Write call.bridge.silence_detected
    → Token still expires normally (no extension for silence)

  Open Discussion Round:
    PSTN participant unmuted with all WebRTC participants
    Speaking time still tracked via SFU audio monitor
    Dominance penalized per GD scoring rules
    All PSTN-specific rules remain active
```

### 5.5 Phase 4 — Network Drop Handling

```
On PSTN connection drop detected:

  1. UPDATE call_session_map SET call_state = 'suspended'
  2. Write call.bridge.network_drop
  3. Increment network_drops

  Rejoin window:
    PSTN participant may NOT rejoin.
    call.bridge.reconnect_denied emitted on any rejoin attempt.
    Participant removed from turn queue.
    Token skipped (same rule as WebRTC network drop in GD system).
    
  No retry. No discretionary logic. Consistent with GD Failure Handling rules.
```

### 5.6 Phase 5 — Termination

```
FUNCTION terminate_call_session_map(call_id, reason):

  Permitted termination reasons:
    session_ended     — Parent GD/Interview/Dojo session completed
    timeout           — Bridge token or session exceeded max lifetime
    eviction          — Orchestrator forced removal
    failure           — SFU or coturn failure
    user_hangup       — PSTN participant hung up
    duplicate         — Duplicate connection attempt resolved

  Steps:
  1. UPDATE call_session_map SET
       call_state = 'terminated' OR 'failed' OR 'evicted',
       terminated_at = now(),
       termination_reason = reason

  2. Write call.bridge.session_terminated or appropriate event
  3. Jitsi/LiveKit: remove PSTN participant from room
  4. Release coturn allocation for this call
  5. Notify GD Orchestrator: remove from speaking queue
  6. Emit Kafka: call.session.terminated

  Session termination cascade:
    When parent session (GD batch / Interview / Dojo match) ends:
    ALL active call_session_map records for that session_id
    must be terminated within 30 seconds.
    Timeout exceeded → force terminate + alert
```

---

## 6. BRIDGE TOKEN ISSUANCE CONTRACT (LOCKED)

### 6.1 Token Generation Rules

```
Token Type:         Signed JWT (RS256 — asymmetric)
Token TTL:          300 seconds (NON-CONFIGURABLE)
Token Payload:
  {
    sub:            user_id,
    call_id:        uuid (generated at issue time),
    session_id:     uuid,
    session_type:   gd | interview | dojo_match,
    room_id:        string,
    room_type:      jitsi | livekit,
    tenant_id:      uuid,
    domain_track:   arts | commerce | science | others,
    phone_e164:     MASKED (+XX****XXXX — NOT full number),
    iat:            unix timestamp,
    exp:            iat + 300,
    jti:            unique token ID (prevent replay)
  }

Token signing key:  Managed by HashiCorp Vault (bridge-token key)
Token reuse:        FORBIDDEN — jti is single-use, tracked in Redis
Token storage:      NOT stored in DB — only bridge_token hash stored
```

### 6.2 Token Issuance Gate

```
Bridge token may be issued ONLY when:
  ✔ user has verified phone in user_phone_ledger (status = 'verified')
  ✔ session_id is active (not expired, not terminated)
  ✔ user is a declared participant in this session
  ✔ join window is open (session has not passed entry lock)
  ✔ no existing active call_session_map for this user + session

If any condition fails → DENY TOKEN → EMIT bridge_token_denied event → RETURN 403
```

### 6.3 Token Issuance API

```
POST /bridge/token/issue
  Auth:    Bearer JWT (authenticated user session)
  Body:    { session_id: uuid, session_type: string }
  Returns: { call_id: uuid, bridge_token: string, expires_at: iso8601, dial_in_number: string }

  Rate Limit: 2 token requests per user per session (second = replacement on first expiry only)
  After 2nd issue: DENY + emit bridge_token_abuse_detected

POST /bridge/token/validate  (internal — bridge service → backend only, mTLS)
  Body:    { bridge_token: string, caller_e164: string }
  Returns: { valid: bool, call_session_map_id: uuid, room_id: string, room_type: string }
```

---

## 7. PSTN BRIDGE SERVICE SPECIFICATION

### 7.1 Service Identity

```
Service Name:       pstn-bridge-service
Namespace:          realtime (k8s namespace — alongside GD, interviews, dojo)
Internal URL:       http://pstn-bridge-service:8030
Protocol:           WebSocket (orchestrator commands) + HTTP (token validation, status)
Media Path:         Direct to Jitsi/LiveKit SFU — never through API gateway
```

### 7.2 Core Modules

```
MODULE                          PURPOSE
──────────────────────────────────────────────────────────────────────────────────
CallSessionMapper               Creates and manages call_session_map records
BridgeTokenService              Issues, validates, and tracks bridge tokens
PStnPhoneGate                   Enforces phone ledger verification before bridge entry
RoomResolver                    Maps session_id → room_id + room_type + SFU endpoint
JitsiBridgeAdapter              Injects PSTN audio into Jitsi rooms, relays mute commands
LiveKitBridgeAdapter            Injects PSTN audio into LiveKit rooms, relays mute commands
CoturnAllocator                 Manages TURN/STUN resource allocation per call
OrchestatorCommandRelay         Forwards WebSocket commands from orchestrator to bridge
AudioActivityMonitor            Detects interrupt attempts and silence during token windows
CallSessionEventWriter          Appends to call_session_events (immutable audit)
CallSessionKafkaEmitter         Publishes call lifecycle events to Kafka
CallTerminationManager          Handles all termination paths (normal, failure, eviction)
```

### 7.3 WebSocket Command Protocol (Orchestrator → Bridge)

```
All commands transmitted over WebSocket channel (same channel as GD mute/unmute control).

COMMAND                         PAYLOAD                             ACTION
────────────────────────────────────────────────────────────────────────────────────────────
PSTN_GRANT_SPEAKING_TOKEN       { call_id, duration_seconds }       Unmute + start timer
PSTN_REVOKE_SPEAKING_TOKEN      { call_id, reason }                 Force mute
PSTN_FORCE_MUTE                 { call_id }                         Immediate mute
PSTN_EVICT                      { call_id, reason }                 Remove from room
PSTN_SESSION_END                { session_id }                      Terminate all PSTN calls in session
PSTN_STATUS_QUERY               { call_id }                         Return current call_state

Invalid command → IGNORE → EMIT unknown_bridge_command → LOG

All commands must be authenticated (orchestrator service identity via mTLS).
Unauthenticated commands → REJECT → EMIT unauthorized_bridge_command → LOG
```

---

## 8. SESSION TYPE INTEGRATION MAP (LOCKED)

### 8.1 Voice GD Sessions (Jitsi)

```
INTEGRATION POINT               RULE
──────────────────────────────────────────────────────────────────────────────────
Room naming                     gd_{domain}_{date}_{batch_id} (GD Orchestrator generates)
PSTN join window                Same as WebRTC — late joiners become spectators only
Participant ordering            PSTN participants sorted by call_session_map.connected_at
Speaking queue                  PSTN participants in SAME queue as WebRTC participants
Turn enforcement                Identical: 60s intro / 30s rebuttal / 45s conclusion
Force mute/unmute               Via JitsiBridgeAdapter → Jitsi API
Score capture                   speaking_turns_completed, interrupt_attempts,
                                mic_open_seconds, silence_during_token_sec
Failure handling                Network drop → token skipped (same as WebRTC rule)
Rejoin                          DENIED (same as WebRTC rule)
```

### 8.2 Dojo Match Sessions (LiveKit)

```
INTEGRATION POINT               RULE
──────────────────────────────────────────────────────────────────────────────────
Room naming                     match_{match_id} (Dojo Match Engine generates)
PSTN join window                Match start window — no late join
Role binding                    PSTN participant must have role assigned before join
Speaking enforcement            Via LiveKitBridgeAdapter → LiveKit API
Timer enforcement               Same state machine as WebRTC Dojo participants
Score capture                   Same metrics as Voice GD + role-specific metrics
```

### 8.3 Interview Sessions (LiveKit)

```
INTEGRATION POINT               RULE
──────────────────────────────────────────────────────────────────────────────────
Room naming                     interview_{interview_id} (Interview Service generates)
PSTN join window                Slot booking window — no walk-in
Participant cap                 2 participants (candidate + recruiter) — no extras
Speaking enforcement            Unrestricted within session (interview is not turn-based)
Session termination             On recruiter end or absolute timeout
```

---

## 9. ASYNC EVENT PIPELINE (KAFKA)

### 9.1 Required Kafka Events

```
EVENT TOPIC                          PRODUCER                REQUIRED CONSUMERS
────────────────────────────────────────────────────────────────────────────────────────────
call.session.map.created             CallSessionMapper       Analytics · Admin Governance
call.session.connected               JitsiBridgeAdapter /    Analytics · GD Orchestrator
                                     LiveKitBridgeAdapter
call.session.speaking_started        OrchestatorRelay        Analytics · Scoring Engine
call.session.speaking_ended          OrchestatorRelay        Analytics · Scoring Engine
call.session.muted                   BridgeAdapter           Analytics
call.session.network_drop            BridgeAdapter           Analytics · Notification Service
call.session.reconnect_denied        CallTerminationMgr      Analytics · Notification Service
call.session.terminated              CallTerminationMgr      Analytics · GD Orchestrator ·
                                                             Dojo Engine · Interview Service
call.session.failed                  CallTerminationMgr      Analytics · Admin Governance ·
                                                             Notification Service
call.session.evicted                 OrchestatorRelay        Analytics · Admin Governance
call.bridge.token_denied             PStnPhoneGate           Analytics · Fraud Detection
call.bridge.duplicate_detected       CallSessionMapper       Fraud Detection · Admin Governance
call.bridge.token_abuse              BridgeTokenService      Fraud Detection · Wazuh SIEM
```

### 9.2 Event Schema Rules

```
All events must carry:
  {
    event_id:       uuid,
    event_type:     string (from approved event list),
    call_id:        uuid,
    session_id:     uuid,
    session_type:   gd | interview | dojo_match,
    user_id:        uuid,
    tenant_id:      uuid,
    domain_track:   string,
    room_id:        string,
    timestamp:      iso8601,
    payload:        {}   (event-specific fields)
  }

phone_e164 MUST NOT appear in any Kafka event payload.
Violation → REPORT PHONE_LEAK_IN_EVENT → STOP
```

---

## 10. SECURITY ENFORCEMENT (LOCKED)

```
CONTROL                         ENFORCEMENT
──────────────────────────────────────────────────────────────────────────────────
Bridge token signing            RS256 asymmetric JWT — key in HashiCorp Vault
Token replay prevention         jti tracked in Redis (TTL: 600s = 2× token lifetime)
Caller ID spoofing protection   E.164 must match phone_ledger verified record
Cross-tenant isolation          tenant_id claim in bridge token enforced at RoomResolver
Cross-domain isolation          domain_track claim in bridge token enforced at PStnPhoneGate
mTLS enforcement                All orchestrator → bridge service calls via mTLS
WAF rules                       ModSecurity: block /bridge/token/issue enumeration
OPA policies                    Only pstn-bridge-service role may write to call_session_map
                                Only GD/Dojo orchestrators may issue PSTN WebSocket commands
Audit immutability              call_session_events: REVOKE UPDATE, DELETE from app role
PII masking                     caller_e164 masked in all logs and Kafka events
SIEM                            call.bridge.token_abuse and call.bridge.duplicate_detected
                                streamed to Wazuh
Coturn allocation               Per-call TURN credential — expires with bridge_token
```

---

## 11. FAILURE CLASSIFICATION (DETERMINISTIC — NO DISCRETION)

```
FAILURE EVENT                       SYSTEM ACTION
──────────────────────────────────────────────────────────────────────────────────
No verified phone in ledger         DENY BRIDGE → emit call.bridge.token_denied
Bridge token expired                DENY BRIDGE → emit call.bridge.token_expired
Bridge token reused                 DENY BRIDGE → emit call.bridge.token_abuse → Wazuh
Domain mismatch                     DENY BRIDGE → emit call.bridge.failed(DOMAIN_MISMATCH)
Tenant mismatch                     DENY BRIDGE → emit call.bridge.failed(TENANT_MISMATCH)
Duplicate bridge attempt            DENY → emit call.bridge.duplicate_detected
SFU room not found                  DENY BRIDGE → emit call.bridge.failed(ROOM_NOT_FOUND)
Network drop mid-session            Suspend → remove from queue → denying rejoin
Session ended while bridged         Auto-terminate all PSTN calls in 30s
coturn allocation failure           DENY BRIDGE → emit call.bridge.failed(COTURN_FAILURE)
WebSocket command auth fail         REJECT command → emit unauthorized_bridge_command
Map creation DB failure             ROLLBACK → emit call.bridge.failed → STOP
```

No silent failures. No auto-retry on PSTN bridge connections. No discretionary logic.

---

## 12. OBSERVABILITY (MANDATORY)

### 12.1 Prometheus Metrics

```
METRIC                                              TYPE      LABELS
────────────────────────────────────────────────────────────────────────────────────────
ecoskiller_pstn_bridge_token_issued_total           counter   (session_type, tenant_id)
ecoskiller_pstn_bridge_connected_total              counter   (session_type, tenant_id)
ecoskiller_pstn_bridge_token_denied_total           counter   (reason, tenant_id)
ecoskiller_pstn_bridge_token_expired_total          counter   (tenant_id)
ecoskiller_pstn_bridge_token_abuse_total            counter   (tenant_id)
ecoskiller_pstn_bridge_network_drop_total           counter   (session_type, tenant_id)
ecoskiller_pstn_bridge_failed_total                 counter   (reason, tenant_id)
ecoskiller_pstn_bridge_evicted_total                counter   (reason, tenant_id)
ecoskiller_pstn_bridge_duplicate_detected_total     counter   (tenant_id)
ecoskiller_pstn_bridge_terminated_total             counter   (reason, session_type)
ecoskiller_pstn_bridge_active_calls_gauge           gauge     (session_type, tenant_id)
ecoskiller_pstn_bridge_speaking_seconds_histogram   histogram (session_type)
ecoskiller_pstn_bridge_token_issue_latency_seconds  histogram (session_type)
```

### 12.2 Grafana Dashboard Panels

```
PANEL                                               ALERT THRESHOLD
────────────────────────────────────────────────────────────────────────────────────────
PSTN Bridge Success Rate (24h)                      Alert if < 85%
Bridge Token Denial Rate (1h)                       Alert if > 10%
Bridge Failure Rate (1h)                            Alert if > 5%
Network Drop Rate by Session Type (24h)             Alert if > 8%
Token Abuse Attempts (1h)                           Alert if > 0
Duplicate Bridge Attempts (1h)                      Alert if > 3
Active PSTN Calls Gauge (live)                      Alert if > declared capacity
Session Termination Cascade Lag (30s window)        Alert if any call not terminated in 30s
```

### 12.3 Required Wazuh SIEM Rules

```
RULE                                    TRIGGER
────────────────────────────────────────────────────────────────────────────────────────
pstn_token_abuse_rule                   call.bridge.token_abuse event
pstn_duplicate_burst_rule               >3 duplicate_detected events from same user in 5 min
pstn_domain_mismatch_burst_rule         >5 domain mismatch denials from same tenant in 10 min
pstn_unauthorized_command_rule          Any unauthorized_bridge_command event
pstn_cascade_timeout_rule              Termination cascade lag > 30s for any session
```

---

## 13. MULTI-TENANT & DOMAIN COMPLETENESS CHECKS

```
✔ tenant_id enforced at call_session_map creation and room resolution
✔ domain_track enforced at pre_bridge_gate (Phase 0)
✔ RLS policy on call_session_map: no cross-tenant query
✔ RLS policy on call_session_events: no cross-tenant query
✔ Bridge token claims tenant_id and domain_track (immutable after issue)
✔ OPA policy: pstn-bridge-service reads only own-tenant session records
✔ Kafka events carry tenant_id and domain_track in every payload
✔ Grafana dashboards: tenant_id filter variable active

Absence → REPORT MULTI_TENANT_BRIDGE_GAP → STOP EXECUTION
```

---

## 14. CI/CD PIPELINE GATES

### 14.1 Required Tests

```
TEST FILE                                   COVERAGE
──────────────────────────────────────────────────────────────────────────────────
call_session_mapper_test                    Map creation, uniqueness, duplicate detection
bridge_token_service_test                   Issue, validate, expire, replay prevention
pstn_phone_gate_test                        Verified / unverified / missing phone cases
room_resolver_test                          session_id → room_id resolution, type mapping
jitsi_bridge_adapter_test                   Mute/unmute relay, room injection, eviction
livekit_bridge_adapter_test                 LiveKit room injection, command relay
call_termination_manager_test               All termination reason paths
orchestrator_command_relay_test             All WebSocket command types, auth enforcement
audio_activity_monitor_test                 Interrupt detection, silence detection thresholds
call_session_event_writer_test              Append-only assertion, immutability verification
kafka_emitter_test                          All 14 event types, schema validation
integration_test_gd_pstn                    Full GD lifecycle with PSTN participant
integration_test_interview_pstn             Full interview lifecycle with PSTN participant
integration_test_dojo_pstn                  Full Dojo match lifecycle with PSTN participant
integration_test_cascade_termination        Parent session end → all PSTN calls terminated ≤ 30s
integration_test_duplicate_rejection        Second bridge attempt for same user+session rejected
integration_test_token_replay_rejection     Reused bridge_token rejected
integration_test_domain_isolation           Commerce session rejects Arts domain PSTN participant
integration_test_tenant_isolation           Tenant A token rejected by Tenant B session
```

### 14.2 CI Stage Declarations

```
STAGE                           GATE CONDITION
──────────────────────────────────────────────────────────────────────────────────
contract_validator              Bridge API schema in OpenAPI registry
unit_test_gate                  All unit tests pass — zero tolerance
integration_test_gate           All integration tests pass
migration_dry_run               call_session_map + call_session_events migrations
kafka_schema_validation         All 14 call event schemas in AsyncAPI registry
observability_smoke_test        All 13 Prometheus metrics scrape clean
security_scan_gate              No phone_e164 full value in logs/events/responses
bridge_token_security_scan      No bridge_token plaintext in logs or DB

Any gate FAIL → CI STOP → NO ARTIFACT PRODUCED
```

---

## 15. CONTRACT GATE REGISTRATION

### 15.1 Required Input Gates

```
GATE                             SOURCE
──────────────────────────────────────────────────────────────────────────────────
phone_pipeline_complete          PHONE_PIPELINE_COMPLETENESS_AGENT v1.0
phone_gate_middleware_ready      PHONE_APPEND_ONLY_ENFORCEMENT_AGENT v1.0
session_management_ready         SESSION-COMP-v1
identity_ready                   Lane A Foundation
db_ready                         Lane B Data
event_schema_ready               Lane A Foundation (R4 AsyncAPI registry)
realtime_namespace_ready         k8s realtime namespace (GD, Dojo, Interview services)
media_stack_ready                Jitsi + LiveKit + coturn deployed and reachable
```

### 15.2 Contract Gates This Agent Produces on PASS

```
GATE                             CONSUMED BY
──────────────────────────────────────────────────────────────────────────────────
pstn_bridge_ready                GD Orchestrator (PSTN participant path)
                                 Dojo Match Engine (PSTN participant path)
                                 Interview Service (PSTN participant path)
call_session_map_ready           Analytics Service · Admin Governance · Scoring Engine
pstn_observability_ready         Platform Observability Readiness Check
pstn_ci_gate_complete            Release Management · Deployment Authorization
```

---

## 16. ANTIGRAVITY GENERATION CHECKLIST (LOCKED)

```
Antigravity MUST generate:
  ✔ V{n}__create_call_session_map.sql                   Flyway migration
  ✔ V{n+1}__create_call_session_events.sql              Flyway migration
  ✔ V{n+2}__call_session_rls_policies.sql               Flyway migration
  ✔ V{n+3}__call_session_indexes.sql                    Flyway migration
  ✔ pstn-bridge-service/                                Full microservice directory
  ✔ pstn-bridge-service/CallSessionMapper               Module
  ✔ pstn-bridge-service/BridgeTokenService              Module
  ✔ pstn-bridge-service/PStnPhoneGate                   Module
  ✔ pstn-bridge-service/RoomResolver                    Module
  ✔ pstn-bridge-service/JitsiBridgeAdapter              Module
  ✔ pstn-bridge-service/LiveKitBridgeAdapter            Module
  ✔ pstn-bridge-service/CoturnAllocator                 Module
  ✔ pstn-bridge-service/OrchestratorCommandRelay        Module
  ✔ pstn-bridge-service/AudioActivityMonitor            Module
  ✔ pstn-bridge-service/CallSessionEventWriter          Module
  ✔ pstn-bridge-service/CallSessionKafkaEmitter         Module
  ✔ pstn-bridge-service/CallTerminationManager          Module
  ✔ /bridge/token/issue endpoint                        Kong-registered, rate-limited
  ✔ /bridge/token/validate endpoint                     mTLS internal only
  ✔ k8s/realtime/pstn-bridge-service/                   Deployment + Service + ConfigMap
  ✔ call_session_kafka_schemas/                         14 event schemas (AsyncAPI)
  ✔ pstn_bridge_opa_policies.rego                       OPA policy file
  ✔ pstn_bridge_wazuh_rules.xml                         5 Wazuh rules
  ✔ pstn_bridge_grafana_dashboard.json                  8 panels + alert rules
  ✔ pstn_bridge_test_suite/                             Unit (11) + Integration (9) tests

Antigravity MUST NOT generate:
  ✗ Any path that allows bridge entry without phone ledger verification
  ✗ Any reusable bridge token mechanism
  ✗ Any auto-reconnect path for PSTN after network drop
  ✗ Any cross-tenant or cross-domain room resolution
  ✗ Any bridge token containing full phone_e164 in payload
  ✗ Any media path that routes audio through the API gateway
  ✗ Any silent failure — every failure must STOP, EMIT, LOG
```

Absence of any item → **STOP EXECUTION → REPORT CALL_SESSION_AGENT_INCOMPLETE**

---

## ✅ FINAL ENFORCEMENT SUMMARY

```
CALL_SESSION_MAPPING_AGENT
  ├── Domain:         PSTN & Bridge
  ├── Scope:          5-phase lifecycle (gate → map → connect → orchestrate → terminate)
  ├── Session Types:  Voice GD (Jitsi) · Dojo Match (LiveKit) · Interview (LiveKit)
  ├── Schema:         call_session_map + call_session_events (append-only audit)
  ├── Token:          RS256 JWT · 300s TTL · single-use · jti replay protection
  ├── Isolation:      Tenant-hard + Domain-hard + Room-scoped (no cross-contamination)
  ├── Events:         14 Kafka topics · AsyncAPI schema enforced · no phone in payloads
  ├── Modules:        12 core modules in pstn-bridge-service
  ├── Tests:          11 unit + 9 integration · 8 CI stages
  ├── Observability:  13 Prometheus metrics · 8 Grafana panels · 5 Wazuh rules
  ├── Input gates:    8 required (phone_pipeline_complete is mandatory)
  ├── Output gates:   4 produced on full PASS
  └── Failure mode:   STOP → EMIT → LOG — no silent failures, no retries, no discretion

EXECUTION_ENGINE    = ANTIGRAVITY
CHANGE_POLICY       = APPEND_ONLY
READY_FOR           = ANTIGRAVITY_PRODUCTION
STATUS              = SEALED · LOCKED · GOVERNED · DETERMINISTIC
```

---

*Ecoskiller · CALL_SESSION_MAPPING_AGENT · v1.0 · SEALED*
*Mutation requires version bump and Human declaration only.*
*Depends on: PHONE_APPEND_ONLY_ENFORCEMENT_AGENT v1.0 · PHONE_PIPELINE_COMPLETENESS_AGENT v1.0 · SESSION-COMP-v1*
