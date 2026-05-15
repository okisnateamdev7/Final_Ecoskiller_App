# AUDIO_BACKPRESSURE_CONTROL_AGENT
## ECOSKILLER — ANTIGRAVITY AUDIO PROCESSING SUBSYSTEM
### Status: FINAL · LOCKED · SEALED · DETERMINISTIC
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only
### Agent Version: v1.0.0

---

> **SEAL DECLARATION**
> This document is a fully locked, self-contained agent prompt for the
> AUDIO_BACKPRESSURE_CONTROL_AGENT within the Ecoskiller Antigravity
> Audio Processing layer.
> No clause may be removed, weakened, reworded, or reinterpreted.
> Any modification without version bump → INVALID EXECUTION.
>
> Bound to: ECOSKILLER MASTER EXECUTION PROMPT v12.0 (UNIFIED)
> Bound to: SECTION R59 — OFFLINE-FIRST & LOW-BANDWIDTH LAW (ENFORCED)
> Bound to: Voice GD Orchestrator (Section V, Core Microservice #7)
> Bound to: AUDIO_PREPROCESSING_AGENT v1.0.0 (upstream dependency)
> Bound to: AUDIO_STREAM_ROUTING_AGENT v1.0.0 (parallel dependency)
> Bound to: AUTOMATED VOICE GROUP DISCUSSION SYSTEM (Jitsi + Rule-Driven Orchestration)
> Bound to: Ecoskiller Services Infrastructure Audit (Section IX — Security Baseline)
> Bound to: Observability Law (Section VIII — NON-OPTIONAL)
>
> This agent operates INSIDE the live GD session — after admission, during transmission.
> The AUDIO_PREPROCESSING_AGENT operates PRE-ADMISSION (before audio enters SFU).
> The AUDIO_STREAM_ROUTING_AGENT operates AT TOKEN LEVEL (who speaks, when).
> These three agents are adjacent, non-overlapping, and never merged.

---

## SECTION 1 — AGENT IDENTITY

```
Agent Name:              AUDIO_BACKPRESSURE_CONTROL_AGENT
System Context:          Ecoskiller Antigravity Audio Processing Layer
Domain:                  realtime (Kubernetes namespace)
Parent Service:          Voice GD Orchestrator (Service #7)
Layer Position:          IN-SESSION — operates across the active WebRTC transmission
                         pipeline from browser capture through Jitsi SFU to receivers
Execution Model:         Deterministic Rule Engine — ZERO AI INFERENCE
Processing Mode:         Congestion detection, adaptive codec control,
                         packet-loss resilience, jitter buffer management,
                         fairness-under-pressure enforcement
Adjacent Agents:
    Upstream:            AUDIO_PREPROCESSING_AGENT v1.0.0
    Parallel:            AUDIO_STREAM_ROUTING_AGENT v1.0.0
Media Protocol:          WebRTC (SRTP + RTCP feedback loop)
SFU Backend:             Self-hosted Jitsi Videobridge
RTCP Stack:              Browser WebRTC RTCPeerConnection.getStats() API
Codec:                   OPUS (48kHz, variable 6–32kbps, CBR → VBR adaptive)
State Store:             Redis (backpressure state, per-participant network profile)
Database:                PostgreSQL (network event audit, degradation logs)
Event Bus:               Apache Kafka (backpressure.* event namespace)
Analytics Target:        ClickHouse (media QoS dashboards — Section VIII)
Kubernetes Namespace:    realtime
Service Name:            audio-backpressure-control-agent
Service Port:            8082
```

---

## SECTION 2 — AGENT PURPOSE (NON-NEGOTIABLE)

The AUDIO_BACKPRESSURE_CONTROL_AGENT governs the real-time transmission health
of every audio stream **inside an active GD session**.

Its mandate is four-part:

```
MANDATE 1 — CONGESTION DETECTION
    Continuously monitor per-participant WebRTC transmission statistics.
    Detect packet loss, jitter, RTT degradation, and bandwidth collapse
    as they happen — not after they corrupt the session.

MANDATE 2 — ADAPTIVE CODEC CONTROL
    When congestion is detected, instruct the browser to reduce OPUS bitrate
    along a locked degradation ladder.
    When network recovers, restore codec parameters along the same ladder.
    No step may be skipped upward or downward.
    Codec changes are backend-commanded — never participant-initiated.

MANDATE 3 — FAIRNESS-UNDER-PRESSURE
    Network degradation must never cause asymmetric punishment.
    A participant on a degraded network must not lose their speaking token early.
    A participant on a stable network must not receive boosted audio relative
    to a degraded participant in any way that influences GD evaluation.
    Degradation is managed silently and symmetrically.

MANDATE 4 — SURVIVABILITY
    A GD session must survive individual participant network failure.
    Token forfeiture due to network drop follows AUDIO_STREAM_ROUTING_AGENT rules.
    This agent's job is to prevent forfeiture by adapting before collapse.
    Adaptation is exhausted before forfeiture is triggered.
```

This agent does NOT:

```
Grant or revoke speaking tokens              → AUDIO_STREAM_ROUTING_AGENT
Force mute or unmute via Jitsi API           → AUDIO_STREAM_ROUTING_AGENT
Evaluate or score participants               → Scoring Engine (Service #9)
Perform preflight audio measurement          → AUDIO_PREPROCESSING_AGENT
Apply AI inference to any signal             → PROHIBITED (entire system)
Store raw audio                              → PROHIBITED (entire system)
Analyse speech content                       → PROHIBITED (entire system)
Initiate participant ejection                → Admin Governance (Service #14)
Control NAT traversal                        → coturn (TURN/STUN server)
```

---

## SECTION 3 — SYSTEM BOUNDARY (LOCKED)

### 3.1 What the Agent OWNS

| Responsibility                                        | Owner                              |
|-------------------------------------------------------|------------------------------------|
| WebRTC RTCP stats polling (per participant)           | AUDIO_BACKPRESSURE_CONTROL_AGENT   |
| Packet loss rate calculation                          | AUDIO_BACKPRESSURE_CONTROL_AGENT   |
| Jitter measurement and classification                 | AUDIO_BACKPRESSURE_CONTROL_AGENT   |
| RTT measurement and trend analysis                    | AUDIO_BACKPRESSURE_CONTROL_AGENT   |
| Available bandwidth estimation (REMB/TWCC parsing)   | AUDIO_BACKPRESSURE_CONTROL_AGENT   |
| Network pressure level classification (L0–L4)        | AUDIO_BACKPRESSURE_CONTROL_AGENT   |
| OPUS bitrate adaptation commands (browser-targeted)  | AUDIO_BACKPRESSURE_CONTROL_AGENT   |
| OPUS FEC toggle commands                             | AUDIO_BACKPRESSURE_CONTROL_AGENT   |
| OPUS DTX toggle commands                             | AUDIO_BACKPRESSURE_CONTROL_AGENT   |
| Jitter buffer adjustment instructions                | AUDIO_BACKPRESSURE_CONTROL_AGENT   |
| Packet loss concealment mode instructions            | AUDIO_BACKPRESSURE_CONTROL_AGENT   |
| Degradation event logging (PostgreSQL)               | AUDIO_BACKPRESSURE_CONTROL_AGENT   |
| Network pressure Kafka event emission                | AUDIO_BACKPRESSURE_CONTROL_AGENT   |
| Per-participant network profile (Redis)              | AUDIO_BACKPRESSURE_CONTROL_AGENT   |
| Session-level QoS summary output (ClickHouse)        | AUDIO_BACKPRESSURE_CONTROL_AGENT   |
| ROUTING_AGENT pre-forfeit advisory signal            | AUDIO_BACKPRESSURE_CONTROL_AGENT   |
| Participant network degradation UI notification      | AUDIO_BACKPRESSURE_CONTROL_AGENT   |

### 3.2 What the Agent DOES NOT OWN

| Out-of-scope responsibility                           | True Owner                         |
|-------------------------------------------------------|------------------------------------|
| Token grant sequence                                  | AUDIO_STREAM_ROUTING_AGENT         |
| Speaking token forfeiture                             | AUDIO_STREAM_ROUTING_AGENT         |
| Force mute / unmute                                   | AUDIO_STREAM_ROUTING_AGENT         |
| Audio signal preprocessing / normalisation            | AUDIO_PREPROCESSING_AGENT          |
| Preflight admission gate                              | AUDIO_PREPROCESSING_AGENT          |
| Audio codec encoding itself                           | Browser WebRTC stack               |
| SFU packet forwarding / mixing                        | Jitsi Videobridge                  |
| NAT traversal relay                                   | coturn (TURN/STUN)                 |
| GD session state machine                              | Redis + GD Orchestrator            |
| Scoring and evaluation                                | Scoring Engine (Service #9)        |
| Participant authentication                            | Auth Service (Service #1)          |
| Participant ejection / banning                        | Admin Governance (Service #14)     |

### 3.3 CRITICAL BOUNDARY PRINCIPLE (INVIOLABLE)

```
The AUDIO_PREPROCESSING_AGENT is the airport security gate.
    It checks every passenger before boarding.

The AUDIO_STREAM_ROUTING_AGENT is the air traffic controller.
    It decides which plane has runway clearance at any moment.

The AUDIO_BACKPRESSURE_CONTROL_AGENT is the flight operations centre.
    It monitors weather (network conditions) in real time,
    adjusts fuel load (bitrate) and route (codec parameters),
    and ensures the plane lands safely even in turbulence.

None of these three roles overlap.
None of these three agents share logic.
None of these three agents may be merged.
```

---

## SECTION 4 — ARCHITECTURE

### 4.1 Technology Stack (Locked — inherits from ECOSKILLER R1)

```
Runtime (Backend Coordinator):    Node.js
Browser Layer (Stats Reporter):   JavaScript WebRTC API
                                  (RTCPeerConnection.getStats())
State Store:                      Redis (network pressure profiles)
Persistence:                      PostgreSQL (degradation audit logs)
Analytics:                        ClickHouse (media QoS time-series)
Event Bus:                        Apache Kafka (backpressure.* namespace)
Observability:                    Prometheus + Loki + OpenTelemetry
Kubernetes Namespace:             realtime
```

### 4.2 Two-Tier Architecture

```
TIER 1 — BROWSER SIDE (JavaScript, runs in participant's browser)
    ├── RTCPeerConnection.getStats() polling (every 2 seconds)
    ├── RTCP stats parser (packet loss, jitter, RTT, bitrate)
    ├── REMB / TWCC feedback consumer (available bandwidth estimate)
    ├── OPUS encoder parameter controller
    │       (applies backend bitrate/FEC/DTX commands)
    ├── Jitter buffer size reporter
    └── WebSocket → Backend stats reporter

TIER 2 — BACKEND COORDINATOR (Node.js, Kubernetes realtime namespace)
    ├── Stats receiver (WebSocket ingest)
    ├── Pressure level classifier (L0–L4)
    ├── Adaptation command generator
    ├── WebSocket command dispatcher → browser
    ├── Redis network profile writer
    ├── PostgreSQL degradation audit writer
    ├── Kafka event emitter (backpressure.*)
    ├── ClickHouse QoS writer
    └── AUDIO_STREAM_ROUTING_AGENT advisory signal
        (via Redis: backpressure:forfeit_risk:{session_id}:{pid})
```

### 4.3 Dependency Graph

```
AUDIO_BACKPRESSURE_CONTROL_AGENT
    ├── RECEIVES FROM      → Browser WebSocket
    │                        (RTCP stats every 2 seconds per participant)
    ├── READS FROM         → Redis
    │                        (active token: gd:{session_id}:active_token)
    │                        (session config: gd:{session_id}:session_config)
    ├── WRITES TO          → Redis
    │                        (backpressure:pressure_level:{session_id}:{pid})
    │                        (backpressure:codec_profile:{session_id}:{pid})
    │                        (backpressure:forfeit_risk:{session_id}:{pid})
    ├── COMMANDS TO        → Browser (WebSocket)
    │                        (CODEC_ADAPT, FEC_ENABLE, DTX_ENABLE, JITTER_ADJUST)
    ├── WRITES AUDIT TO    → PostgreSQL
    │                        (network_degradation_log, adaptation_event_log)
    ├── WRITES QoS TO      → ClickHouse
    │                        (media_qos_stream — per-participant time-series)
    └── EMITS EVENTS TO    → Kafka (backpressure.* namespace)
```

---

## SECTION 5 — ANTIGRAVITY BACKPRESSURE PHILOSOPHY

### 5.1 The Network Fairness Problem

In the ECOSKILLER GD system, participants connect from:

```
Urban broadband (100 Mbps stable, <20ms RTT)
Tier-2 city 4G (5–20 Mbps, 50–150ms RTT, occasional loss bursts)
Rural 3G / weak WiFi (1–5 Mbps, 150–400ms RTT, frequent loss)
Mobile data during commute (variable, sudden drops)
VPN-tunnelled corporate networks (high jitter, inconsistent bandwidth)
```

Without backpressure control, network disparity causes:

```
PROBLEM 1 — AUDIO DROPOUT (audible stuttering mid-turn)
    Packet loss without FEC → audio gaps → participant appears incoherent
    → unjust negative perception during GD evaluation

PROBLEM 2 — JITTER OVERFLOW (delayed audio bursts)
    Jitter buffer overflow → out-of-order playback → participant sounds
    disorganised → unjust negative perception

PROBLEM 3 — CODEC COLLAPSE (bitrate starvation)
    High bitrate audio during congestion → massive loss → complete audio failure
    → participant loses turn due to network, not ability → scoring injustice

PROBLEM 4 — ASYMMETRIC EXPERIENCE (listener-side degradation)
    One degraded sender degrades the entire room's receive experience
    → unfair burden on other participants
```

### 5.2 Antigravity Backpressure Mandate

```
RULE: Network degradation is invisible to the GD evaluation process.
      The Scoring Engine receives only behavioural data.
      It never receives network quality data.

RULE: Adaptation is applied before audible degradation occurs.
      The agent acts on statistical prediction — not on audible failure.

RULE: Codec downgrade protects the speaking participant's audibility.
      A participant on a poor network sounds clear at 6kbps
      rather than broken at 32kbps.

RULE: Every participant's degradation is managed independently.
      No participant's network problem propagates as a louder problem
      to others beyond what the SFU already handles.

RULE: Codec parameters are backend-commanded.
      Participants cannot manually adjust bitrate, FEC, or DTX.
      The agent has sole authority over codec configuration.
```

---

## SECTION 6 — NETWORK PRESSURE LEVEL SYSTEM (LOCKED)

### 6.1 Pressure Level Definitions

The agent classifies each participant's network into one of five
discrete pressure levels at each 2-second polling interval.

```
LEVEL L0 — NOMINAL (Green)
    Packet Loss:    < 1%
    Jitter:         < 20ms
    RTT:            < 100ms
    Available BW:   > 64 kbps
    Action:         None. Hold OPUS at 32kbps CBR.

LEVEL L1 — MINOR STRESS (Amber-Low)
    Packet Loss:    1% – 3%
    Jitter:         20ms – 50ms
    RTT:            100ms – 200ms
    Available BW:   32–64 kbps
    Action:         Enable OPUS FEC (forward error correction).
                    Hold bitrate at 32kbps.
                    Enlarge jitter buffer (target: +20ms headroom).

LEVEL L2 — MODERATE CONGESTION (Amber-High)
    Packet Loss:    3% – 8%
    Jitter:         50ms – 100ms
    RTT:            200ms – 300ms
    Available BW:   16–32 kbps
    Action:         Reduce OPUS bitrate to 20kbps.
                    FEC remains ON.
                    Enable OPUS DTX (discontinuous transmission on silence).
                    Enlarge jitter buffer further (+40ms headroom).

LEVEL L3 — SEVERE CONGESTION (Red-Low)
    Packet Loss:    8% – 20%
    Jitter:         100ms – 200ms
    RTT:            300ms – 500ms
    Available BW:   8–16 kbps
    Action:         Reduce OPUS bitrate to 12kbps.
                    FEC ON. DTX ON.
                    Enable OPUS packet loss concealment mode (PLC aggressive).
                    Enlarge jitter buffer to maximum (+80ms headroom).
                    Emit backpressure.forfeit_risk_high to Kafka.
                    Set Redis backpressure:forfeit_risk:{session_id}:{pid} = HIGH.
                    Send participant UI advisory: "Your connection is unstable."

LEVEL L4 — CRITICAL / COLLAPSE (Red-High)
    Packet Loss:    > 20%
    Jitter:         > 200ms OR unmeasurable
    RTT:            > 500ms OR unmeasurable
    Available BW:   < 8 kbps
    Action:         Reduce OPUS bitrate to 6kbps (minimum viable voice).
                    FEC ON. DTX ON. PLC aggressive.
                    Emit backpressure.network_collapse to Kafka.
                    Set Redis backpressure:forfeit_risk:{session_id}:{pid} = CRITICAL.
                    Signal AUDIO_STREAM_ROUTING_AGENT (via Redis flag).
                    Participant is given ADAPTATION_GRACE_PERIOD (10 seconds).
                    If L4 persists after grace period → ROUTING_AGENT handles forfeit.
```

### 6.2 Pressure Level Hysteresis Rule (SEALED)

```
Downgrade (worsening):   Triggered on 2 consecutive L(N+1) readings.
                         Prevents single-spike false adaptation.

Upgrade (improving):     Triggered on 5 consecutive L(N-1) readings.
                         Prevents premature bitrate restoration.

Maximum downgrade speed: One level per polling cycle (every 2s).
                         No level may be skipped downward.

Maximum upgrade speed:   One level per 3 polling cycles (every 6s).
                         No level may be skipped upward.

EXAMPLE:
    L0 → L0 → L1 reading → L0 reading → L1 reading → L1 reading
    Decision: DOWNGRADE to L1 (2 consecutive L1 readings confirmed)

    L3 → L2 reading → L2 reading → L2 reading → L2 reading → L2 reading
    Decision: UPGRADE to L2 (5 consecutive L2 readings confirmed)
```

---

## SECTION 7 — ADAPTATION CODEC LADDER (SEALED)

### 7.1 OPUS Codec Adaptation Ladder (Locked)

```
╔══════════╦══════════════╦═════════╦═════════╦══════════════╦══════════════╗
║ Level    ║ Bitrate      ║ FEC     ║ DTX     ║ PLC Mode     ║ Jitter Buf   ║
╠══════════╬══════════════╬═════════╬═════════╬══════════════╬══════════════╣
║ L0       ║ 32 kbps CBR  ║ OFF     ║ OFF     ║ Normal       ║ Default      ║
║ L1       ║ 32 kbps CBR  ║ ON      ║ OFF     ║ Normal       ║ +20ms        ║
║ L2       ║ 20 kbps      ║ ON      ║ ON      ║ Normal       ║ +40ms        ║
║ L3       ║ 12 kbps      ║ ON      ║ ON      ║ Aggressive   ║ +80ms        ║
║ L4       ║ 6 kbps       ║ ON      ║ ON      ║ Aggressive   ║ Maximum      ║
╚══════════╩══════════════╩═════════╩═════════╩══════════════╩══════════════╝

FEC  = Forward Error Correction (OPUS in-band redundancy — adds ~3kbps overhead)
DTX  = Discontinuous Transmission (mutes stream during silence gaps — reduces loss)
PLC  = Packet Loss Concealment (fills gaps with interpolated audio — hides loss)
```

### 7.2 Codec Adaptation Command Format (Browser-Bound)

```json
{
    "event":          "CODEC_ADAPT",
    "session_id":     "uuid",
    "participant_id": "uuid",
    "pressure_level": "L2",
    "opus_config": {
        "bitrate_kbps":  20,
        "fec_enabled":   true,
        "dtx_enabled":   true,
        "plc_mode":      "normal",
        "jitter_buffer_target_ms": 80
    },
    "reason":         "PACKET_LOSS_THRESHOLD_EXCEEDED",
    "timestamp":      "ISO8601"
}
```

### 7.3 Browser-Side OPUS Encoder Parameter Application

```javascript
FUNCTION apply_codec_config(config, peer_connection):

    // Apply bitrate to sender
    senders = peer_connection.getSenders()
    audio_sender = senders.find(s => s.track && s.track.kind === 'audio')

    IF audio_sender IS NULL:
        LOG "NO_AUDIO_SENDER_FOUND"
        RETURN

    params = audio_sender.getParameters()

    IF params.encodings.length === 0:
        params.encodings = [{}]

    params.encodings[0].maxBitrate = config.bitrate_kbps * 1000

    // OPUS-specific parameters via codec capabilities
    // Note: FEC, DTX, PLC are set via OPUS SDP negotiation.
    // For in-session adjustment, use RTCRtpSender.setParameters()
    // where supported, or renegotiate via createOffer/setLocalDescription.
    // Implementation MUST test browser compatibility before applying.

    await audio_sender.setParameters(params)

    // Apply jitter buffer target (Chrome-specific API where available)
    receivers = peer_connection.getReceivers()
    audio_receiver = receivers.find(r => r.track && r.track.kind === 'audio')

    IF audio_receiver AND audio_receiver.jitterBufferTarget IS DEFINED:
        audio_receiver.jitterBufferTarget = config.jitter_buffer_target_ms

    // Report application result to backend
    SEND WebSocket:
    {
        "event":          "CODEC_ADAPT_APPLIED",
        "session_id":     session_id,
        "participant_id": participant_id,
        "applied_config": config,
        "timestamp":      NOW_UTC()
    }
```

---

## SECTION 8 — RTCP STATS COLLECTION (SEALED ALGORITHM)

### 8.1 Stats Collection Loop (Browser Tier)

```javascript
FUNCTION start_stats_collection(session_id, participant_id, peer_connection):

    polling_interval_ms = 2000  // every 2 seconds — fixed, not configurable

    EVERY polling_interval_ms:

        stats_report = await peer_connection.getStats()

        // --- OUTBOUND (sender) stats ---
        outbound = stats_report.filter(s => s.type === 'outbound-rtp' && s.kind === 'audio')
        inbound_remote = stats_report.filter(s => s.type === 'remote-inbound-rtp' && s.kind === 'audio')

        sender_stats = {
            packets_sent:          outbound[0]?.packetsSent ?? 0,
            bytes_sent:            outbound[0]?.bytesSent ?? 0,
            retransmitted_packets: outbound[0]?.retransmittedPacketsSent ?? 0,
            nack_count:            outbound[0]?.nackCount ?? 0,
            target_bitrate:        outbound[0]?.targetBitrate ?? 0
        }

        remote_stats = {
            packets_lost:   inbound_remote[0]?.packetsLost ?? 0,
            jitter:         inbound_remote[0]?.jitter ?? 0,          // seconds
            rtt:            inbound_remote[0]?.roundTripTime ?? 0    // seconds
        }

        // --- INBOUND (receiver) stats ---
        inbound = stats_report.filter(s => s.type === 'inbound-rtp' && s.kind === 'audio')
        inbound_stats = {
            packets_received:      inbound[0]?.packetsReceived ?? 0,
            packets_lost_receive:  inbound[0]?.packetsLost ?? 0,
            jitter_receive:        inbound[0]?.jitter ?? 0,
            concealed_samples:     inbound[0]?.concealedSamples ?? 0,
            total_samples:         inbound[0]?.totalSamplesReceived ?? 1
        }

        // --- CANDIDATE PAIR (network level) ---
        candidate_pair = stats_report.filter(
            s => s.type === 'candidate-pair' && s.nominated === true
        )
        network_stats = {
            available_outgoing_kbps: (candidate_pair[0]?.availableOutgoingBitrate ?? 0) / 1000,
            current_rtt_ms:          (candidate_pair[0]?.currentRoundTripTime ?? 0) * 1000
        }

        // --- COMPUTE DERIVED METRICS ---
        packet_loss_pct = compute_packet_loss_pct(
            remote_stats.packets_lost,
            sender_stats.packets_sent
        )
        jitter_ms = remote_stats.jitter * 1000
        rtt_ms    = remote_stats.rtt * 1000
        concealment_ratio = inbound_stats.concealed_samples / inbound_stats.total_samples

        // --- REPORT TO BACKEND ---
        SEND WebSocket:
        {
            "event":              "RTCP_STATS_SAMPLE",
            "session_id":         session_id,
            "participant_id":     participant_id,
            "packet_loss_pct":    packet_loss_pct,
            "jitter_ms":          jitter_ms,
            "rtt_ms":             rtt_ms,
            "available_kbps":     network_stats.available_outgoing_kbps,
            "concealment_ratio":  concealment_ratio,
            "nack_count_delta":   sender_stats.nack_count,
            "target_bitrate_kbps": sender_stats.target_bitrate / 1000,
            "timestamp":          NOW_UTC()
        }

FUNCTION compute_packet_loss_pct(packets_lost, packets_sent):
    IF packets_sent === 0: RETURN 0
    RETURN min((packets_lost / packets_sent) * 100, 100)
```

### 8.2 Backend Stats Handler and Pressure Classifier

```
FUNCTION handle_rtcp_stats_sample(payload):

    // Load historical window (last 5 samples) from Redis
    history_key = "backpressure:stats_history:{session_id}:{participant_id}"
    history = LRANGE Redis history_key 0 4          // last 5 readings
    LPUSH Redis history_key JSON(payload)
    LTRIM Redis history_key 0 9                     // keep last 10 readings

    // Classify current reading
    current_level = classify_pressure_level(payload)

    // Load current stored level
    stored_level = GET Redis "backpressure:pressure_level:{session_id}:{participant_id}"
                   ?? "L0"

    // Apply hysteresis
    new_level = apply_hysteresis(current_level, stored_level, history)

    // Write new level
    SET Redis "backpressure:pressure_level:{session_id}:{participant_id}" = new_level

    // Generate and dispatch adaptation command if level changed
    IF new_level != stored_level:
        config = CODEC_LADDER[new_level]
        SEND WebSocket to participant: CODEC_ADAPT command (Section 7.2)

    // Set forfeit risk flags
    IF new_level IN ["L3", "L4"]:
        SET Redis "backpressure:forfeit_risk:{session_id}:{participant_id}"
                = (new_level == "L4") ? "CRITICAL" : "HIGH"
    ELSE:
        SET Redis "backpressure:forfeit_risk:{session_id}:{participant_id}" = "NONE"

    // Write to PostgreSQL audit
    INSERT INTO network_degradation_log:
        session_id, participant_id, pressure_level = new_level,
        packet_loss_pct, jitter_ms, rtt_ms, available_kbps, timestamp

    // Write to ClickHouse QoS stream
    INSERT INTO media_qos_stream:
        session_id, participant_id, pressure_level, packet_loss_pct,
        jitter_ms, rtt_ms, available_kbps, codec_bitrate_kbps, timestamp

    // Emit Kafka event
    EMIT Kafka: backpressure.level.changed (if level changed)
    EMIT Kafka: backpressure.sample (always — for ClickHouse analytics)
```

### 8.3 Pressure Level Classifier

```
FUNCTION classify_pressure_level(sample):

    loss = sample.packet_loss_pct
    jitter = sample.jitter_ms
    rtt = sample.rtt_ms
    bw = sample.available_kbps

    // L4 — any single critical threshold breach → immediate L4
    IF loss > 20 OR jitter > 200 OR rtt > 500 OR bw < 8:
        RETURN "L4"

    // L3 — severe
    IF loss > 8 OR jitter > 100 OR rtt > 300 OR bw < 16:
        RETURN "L3"

    // L2 — moderate
    IF loss > 3 OR jitter > 50 OR rtt > 200 OR bw < 32:
        RETURN "L2"

    // L1 — minor
    IF loss > 1 OR jitter > 20 OR rtt > 100 OR bw < 64:
        RETURN "L1"

    // L0 — nominal
    RETURN "L0"
```

### 8.4 Hysteresis Application

```
FUNCTION apply_hysteresis(current_reading, stored_level, history):

    // Convert history to pressure levels
    historical_levels = history.map(s => classify_pressure_level(JSON.parse(s)))

    stored_numeric = LEVEL_TO_INT[stored_level]      // L0=0, L1=1, L2=2, L3=3, L4=4
    current_numeric = LEVEL_TO_INT[current_reading]

    IF current_numeric > stored_numeric:
        // Worsening — require 2 consecutive readings at new level
        recent_two = historical_levels.slice(0, 2)
        all_same = recent_two.every(l => LEVEL_TO_INT[l] >= current_numeric)
        IF all_same:
            RETURN current_reading    // DOWNGRADE confirmed
        ELSE:
            RETURN stored_level       // Hold current level

    ELSE IF current_numeric < stored_numeric:
        // Improving — require 5 consecutive readings at new level
        recent_five = historical_levels.slice(0, 5)
        all_same = recent_five.length === 5 AND
                   recent_five.every(l => LEVEL_TO_INT[l] <= current_numeric)
        IF all_same:
            RETURN INT_TO_LEVEL[stored_numeric - 1]   // UPGRADE one level only
        ELSE:
            RETURN stored_level       // Hold current level

    ELSE:
        RETURN stored_level           // No change
```

---

## SECTION 9 — FORFEIT RISK ADVISORY PROTOCOL (LOCKED)

### 9.1 Purpose

When a participant's pressure level reaches L3 or L4 during their active speaking token,
the AUDIO_BACKPRESSURE_CONTROL_AGENT must signal the AUDIO_STREAM_ROUTING_AGENT
so it can prepare contingency handling — without prematurely forfeiting the token.

### 9.2 Advisory Signal Contract

```
SIGNAL MEDIUM:   Redis key (not Kafka — must be synchronous and immediately readable)

KEY:             backpressure:forfeit_risk:{session_id}:{participant_id}
VALUES:
    "NONE"       →  Network is L0–L2. No risk. Token proceeds normally.
    "HIGH"       →  Network is L3. Backpressure is managing. Token continues.
                    ROUTING_AGENT is aware but takes no action yet.
    "CRITICAL"   →  Network is L4. Adaptation at minimum codec floor.
                    If CRITICAL persists for > ADAPTATION_GRACE_PERIOD (10s):
                    ROUTING_AGENT may initiate forfeit per its own rules.

ADAPTATION_GRACE_PERIOD:
    10 seconds from first CRITICAL reading before ROUTING_AGENT acts.
    Grace period resets if level recovers to HIGH or below.
    Grace period is tracked by ROUTING_AGENT — not by this agent.
```

### 9.3 Participant Advisory Messages

```
WHEN pressure_level becomes L3:
    SEND WebSocket to participant:
    {
        "event":    "NETWORK_ADVISORY",
        "severity": "WARNING",
        "message":  "Your connection quality has dropped.
                     Audio has been optimised to stay clear.
                     Move to a stronger signal if possible.",
        "visible_to_others": false
    }

WHEN pressure_level becomes L4:
    SEND WebSocket to participant:
    {
        "event":    "NETWORK_ADVISORY",
        "severity": "CRITICAL",
        "message":  "Very poor connection detected.
                     Audio is running at minimum quality.
                     Your turn may be affected if connection does not improve.",
        "visible_to_others": false
    }

WHEN pressure_level recovers from L3/L4 to L2 or better:
    SEND WebSocket to participant:
    {
        "event":    "NETWORK_ADVISORY",
        "severity": "RESOLVED",
        "message":  "Your connection has improved. Audio quality restored.",
        "visible_to_others": false
    }
```

---

## SECTION 10 — SESSION-LEVEL AGGREGATED QoS REPORTING

### 10.1 Purpose

At session end, the AUDIO_BACKPRESSURE_CONTROL_AGENT must emit a
per-participant QoS summary. This is written to PostgreSQL for audit
and to ClickHouse for analytics dashboards (Section VIII Observability Law).

This data is NOT sent to the Scoring Engine. It has no bearing on participant scores.

### 10.2 Session QoS Summary Output

```json
{
    "session_id":               "uuid",
    "participant_id":           "uuid",
    "session_duration_s":       600,
    "samples_collected":        300,
    "time_at_l0_pct":           72.0,
    "time_at_l1_pct":           18.0,
    "time_at_l2_pct":           6.5,
    "time_at_l3_pct":           2.5,
    "time_at_l4_pct":           1.0,
    "peak_packet_loss_pct":     14.2,
    "avg_packet_loss_pct":      1.8,
    "peak_jitter_ms":           180.0,
    "avg_jitter_ms":            22.4,
    "peak_rtt_ms":              412.0,
    "avg_rtt_ms":               85.2,
    "min_codec_bitrate_kbps":   12,
    "adaptations_triggered":    5,
    "forfeit_risk_events":      1,
    "forfeit_risk_max_level":   "HIGH",
    "network_collapse_events":  0,
    "timestamp":                "ISO8601"
}
```

### 10.3 ClickHouse Table Schema

```sql
CREATE TABLE media_qos_stream (
    session_id              UUID,
    participant_id          UUID,
    pressure_level          LowCardinality(String),
    packet_loss_pct         Float32,
    jitter_ms               Float32,
    rtt_ms                  Float32,
    available_kbps          Float32,
    codec_bitrate_kbps      UInt8,
    fec_enabled             UInt8,
    dtx_enabled             UInt8,
    concealment_ratio       Float32,
    timestamp               DateTime64(3)
) ENGINE = MergeTree()
ORDER BY (session_id, participant_id, timestamp);

CREATE TABLE media_qos_session_summary (
    session_id              UUID,
    participant_id          UUID,
    session_duration_s      UInt32,
    samples_collected       UInt32,
    time_at_l0_pct          Float32,
    time_at_l1_pct          Float32,
    time_at_l2_pct          Float32,
    time_at_l3_pct          Float32,
    time_at_l4_pct          Float32,
    peak_packet_loss_pct    Float32,
    avg_packet_loss_pct     Float32,
    peak_jitter_ms          Float32,
    avg_jitter_ms           Float32,
    peak_rtt_ms             Float32,
    avg_rtt_ms              Float32,
    min_codec_bitrate_kbps  UInt8,
    adaptations_triggered   UInt16,
    forfeit_risk_events     UInt16,
    forfeit_risk_max_level  LowCardinality(String),
    network_collapse_events UInt16,
    timestamp               DateTime64(3)
) ENGINE = MergeTree()
ORDER BY (session_id, participant_id);
```

---

## SECTION 11 — DATA CAPTURE CONTRACT (LOCKED)

### 11.1 Captured — PostgreSQL Tables

```sql
-- network_degradation_log
CREATE TABLE network_degradation_log (
    log_id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id          UUID NOT NULL,
    participant_id      UUID NOT NULL,
    pressure_level      TEXT NOT NULL,           -- L0 | L1 | L2 | L3 | L4
    packet_loss_pct     NUMERIC(5,2),
    jitter_ms           NUMERIC(8,2),
    rtt_ms              NUMERIC(8,2),
    available_kbps      NUMERIC(10,2),
    timestamp           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- adaptation_event_log
CREATE TABLE adaptation_event_log (
    log_id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id          UUID NOT NULL,
    participant_id      UUID NOT NULL,
    from_level          TEXT NOT NULL,
    to_level            TEXT NOT NULL,
    trigger_metric      TEXT NOT NULL,           -- PACKET_LOSS | JITTER | RTT | BANDWIDTH
    new_bitrate_kbps    SMALLINT,
    fec_enabled         BOOLEAN,
    dtx_enabled         BOOLEAN,
    timestamp           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- forfeit_risk_event_log
CREATE TABLE forfeit_risk_event_log (
    log_id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id          UUID NOT NULL,
    participant_id      UUID NOT NULL,
    risk_level          TEXT NOT NULL,           -- HIGH | CRITICAL
    during_active_token BOOLEAN,
    duration_s          SMALLINT,
    resolved            BOOLEAN,
    timestamp           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- qos_session_summary
CREATE TABLE qos_session_summary (
    summary_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id          UUID NOT NULL,
    participant_id      UUID NOT NULL,
    time_at_l0_pct      NUMERIC(5,2),
    time_at_l1_pct      NUMERIC(5,2),
    time_at_l2_pct      NUMERIC(5,2),
    time_at_l3_pct      NUMERIC(5,2),
    time_at_l4_pct      NUMERIC(5,2),
    peak_packet_loss_pct NUMERIC(5,2),
    avg_packet_loss_pct NUMERIC(5,2),
    min_codec_bitrate_kbps SMALLINT,
    adaptations_triggered  SMALLINT,
    forfeit_risk_events    SMALLINT,
    network_collapse_events SMALLINT,
    generated_at        TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

### 11.2 Captured — Redis Keys (Temporary, TTL-Bounded)

```
backpressure:pressure_level:{session_id}:{participant_id}   → L0|L1|L2|L3|L4
backpressure:codec_profile:{session_id}:{participant_id}    → JSON (current codec config)
backpressure:forfeit_risk:{session_id}:{participant_id}     → NONE|HIGH|CRITICAL
backpressure:stats_history:{session_id}:{participant_id}    → ring buffer (last 10 samples)
backpressure:adaptation_count:{session_id}:{participant_id} → integer
backpressure:grace_start:{session_id}:{participant_id}      → epoch_ms (L4 onset time)
```

### 11.3 NOT Captured — Absolute Prohibition

```
Raw audio samples:                 NEVER STORED
Audio waveforms or spectrograms:   NEVER STORED
Voice recordings of any duration:  NEVER STORED
Speech content or transcripts:     NEVER ANALYSED
Tone, emotion, accent detection:   NEVER PERFORMED
Speaker identification:            NEVER PERFORMED
AI inference on audio signal:      NEVER PERFORMED
Network quality data sent to
Scoring Engine:                    STRICTLY FORBIDDEN
```

Violation of the above → IMMEDIATE SECURITY INCIDENT.

---

## SECTION 12 — KAFKA EVENT SCHEMA (backpressure.* NAMESPACE)

```json
// backpressure.level.changed
{
    "event":          "backpressure.level.changed",
    "session_id":     "uuid",
    "participant_id": "uuid",
    "from_level":     "L0",
    "to_level":       "L2",
    "trigger_metric": "JITTER",
    "timestamp":      "ISO8601"
}

// backpressure.sample (emitted every polling cycle for ClickHouse)
{
    "event":            "backpressure.sample",
    "session_id":       "uuid",
    "participant_id":   "uuid",
    "pressure_level":   "L1",
    "packet_loss_pct":  1.4,
    "jitter_ms":        28.0,
    "rtt_ms":           95.0,
    "available_kbps":   48.0,
    "timestamp":        "ISO8601"
}

// backpressure.forfeit_risk_high
{
    "event":              "backpressure.forfeit_risk_high",
    "session_id":         "uuid",
    "participant_id":     "uuid",
    "pressure_level":     "L3",
    "during_active_token": true,
    "timestamp":          "ISO8601"
}

// backpressure.network_collapse
{
    "event":              "backpressure.network_collapse",
    "session_id":         "uuid",
    "participant_id":     "uuid",
    "pressure_level":     "L4",
    "during_active_token": true,
    "grace_expires_at":   "ISO8601",
    "timestamp":          "ISO8601"
}

// backpressure.network_recovered
{
    "event":          "backpressure.network_recovered",
    "session_id":     "uuid",
    "participant_id": "uuid",
    "recovered_to":   "L1",
    "from_level":     "L3",
    "timestamp":      "ISO8601"
}

// backpressure.session_qos_summary
{
    "event":            "backpressure.session_qos_summary",
    "session_id":       "uuid",
    "participant_id":   "uuid",
    "summary":          { ...QoS summary object from Section 10.2 },
    "timestamp":        "ISO8601"
}
```

---

## SECTION 13 — WEBSOCKET COMMAND PROTOCOL

### 13.1 Backend → Browser Commands

```json
// Codec adaptation instruction
{
    "event":          "CODEC_ADAPT",
    "session_id":     "uuid",
    "participant_id": "uuid",
    "pressure_level": "L2",
    "opus_config": {
        "bitrate_kbps":          20,
        "fec_enabled":           true,
        "dtx_enabled":           true,
        "plc_mode":              "normal",
        "jitter_buffer_target_ms": 80
    },
    "reason":         "JITTER_THRESHOLD_EXCEEDED",
    "timestamp":      "ISO8601"
}

// Network advisory to participant
{
    "event":             "NETWORK_ADVISORY",
    "severity":          "WARNING | CRITICAL | RESOLVED",
    "message":           "string",
    "visible_to_others": false
}

// Stats collection start instruction
{
    "event":            "BEGIN_STATS_COLLECTION",
    "session_id":       "uuid",
    "participant_id":   "uuid",
    "interval_ms":      2000
}

// Stats collection stop instruction (session end)
{
    "event":      "STOP_STATS_COLLECTION",
    "session_id": "uuid"
}
```

### 13.2 Browser → Backend Reports

```json
// RTCP stats sample (every 2 seconds)
{
    "event":              "RTCP_STATS_SAMPLE",
    "session_id":         "uuid",
    "participant_id":     "uuid",
    "packet_loss_pct":    2.1,
    "jitter_ms":          34.5,
    "rtt_ms":             120.0,
    "available_kbps":     45.2,
    "concealment_ratio":  0.003,
    "nack_count_delta":   2,
    "target_bitrate_kbps": 32.0,
    "timestamp":          "ISO8601"
}

// Codec adaptation confirmation
{
    "event":          "CODEC_ADAPT_APPLIED",
    "session_id":     "uuid",
    "participant_id": "uuid",
    "applied_config": { ...opus_config },
    "timestamp":      "ISO8601"
}

// Stats collection stopped (session end)
{
    "event":      "STATS_COLLECTION_STOPPED",
    "session_id": "uuid",
    "timestamp":  "ISO8601"
}
```

---

## SECTION 14 — OPEN DISCUSSION ROUND BACKPRESSURE BEHAVIOUR

During the Open Discussion round, all participants' microphones are unmuted simultaneously.
The AUDIO_BACKPRESSURE_CONTROL_AGENT continues operating identically.

```
Differences during Open Discussion round:
    All participants:   Stats are collected for all simultaneously.
    Token awareness:    Agent still reads active token state from Redis.
                        (All participants are "active" during Open Discussion.)
    Forfeit risk flags: Written for all — ROUTING_AGENT uses them for
                        dominance tracking (L4 participant's open-discussion
                        time is still logged, not penalised further).
    Adaptation:         Applied independently per participant as before.
    Advisory messages:  Still sent privately to degraded participants only.
```

---

## SECTION 15 — FAILURE HANDLING (NON-NEGOTIABLE)

| Failure Event                                    | Agent Action                                                     |
|--------------------------------------------------|------------------------------------------------------------------|
| Browser stats polling fails (getStats() error)   | Log to backend. Continue polling. Do not escalate pressure level.|
| WebSocket from browser disconnects               | Mark participant stats as UNAVAILABLE in Redis. Hold last level. |
| WebSocket reconnects                             | Re-issue BEGIN_STATS_COLLECTION. Resume normal polling.          |
| Backend Redis unavailable                        | Halt adaptation decisions. Log critical error. Emit Kafka alert. |
| ClickHouse unavailable                           | Buffer QoS samples in Redis. Flush on recovery (max 1000 items). |
| PostgreSQL unavailable                           | Buffer audit rows in Redis. Flush on recovery.                   |
| Kafka unavailable                                | Log locally. Emit on reconnect. Do not block adaptation.         |
| L4 persists beyond grace period                  | Set forfeit_risk = CRITICAL. ROUTING_AGENT handles forfeit.      |
| Codec adaptation command not acknowledged        | Retry once (3s). Log CODEC_ADAPT_UNACKNOWLEDGED. Continue.       |
| New participant joins mid-session                | Begin stats collection immediately. Start at L0.                 |
| Participant exits mid-session                    | Stop polling. Emit qos_summary. Clear Redis keys.                |
| Browser does not support getStats()              | Log STATS_API_UNSUPPORTED. No adaptation. Continue session.      |
| Stats report returns all-zero values             | Hold current pressure level. Do not upgrade. Log anomaly.        |

---

## SECTION 16 — R59 LOW-BANDWIDTH LAW COMPLIANCE (MANDATORY)

SECTION R59 — OFFLINE-FIRST & LOW-BANDWIDTH LAW requires the system
to accommodate low-bandwidth users without degrading their experience.

The AUDIO_BACKPRESSURE_CONTROL_AGENT directly implements R59 for the
realtime audio layer:

```
R59 COMPLIANCE MAPPING:

R59 Requirement: "Retry & Backoff Controller"
    → Implemented: Hysteresis upgrade algorithm prevents premature
                   bitrate restoration. Downgrade is aggressive.
                   Upgrade is conservative (5-reading confirmation).

R59 Requirement: "Deferred Action Queue Processor"
    → Implemented: Codec adaptation commands are queued in Redis
                   if WebSocket is temporarily unavailable.
                   Delivered on reconnect.

R59 Requirement: "Offline Sync Service"
    → Implemented: Stats are buffered in Redis if backend is unavailable.
                   ClickHouse/PostgreSQL receive buffered data on recovery.

R59 Requirement: "Offline Status Indicator Banner"
    → Implemented: NETWORK_ADVISORY WebSocket commands drive the
                   Flutter participant HUD (Section 17.3).

R59 Requirement: Low-bandwidth user must not be excluded from service.
    → Implemented: L4 floor codec (6kbps OPUS) is selected specifically
                   to maintain voice intelligibility on 3G connections.
                   Session survivability at 8kbps is the design target.
```

---

## SECTION 17 — UI REQUIREMENTS

### 17.1 In-Session Network Quality Chip (Flutter)

```
Widget Name:    NetworkQualityChip
Location:       Bottom HUD bar — adjacent to LiveAudioQualityChip
Visible to:     Only the participant themselves (not to others)

Display:
    L0 → Green signal bars (4/4)   "Connection: Excellent"
    L1 → Green signal bars (3/4)   "Connection: Good"
    L2 → Amber signal bars (2/4)   "Connection: Fair"
    L3 → Amber signal bars (1/4)   "Connection: Poor" + pulse animation
    L4 → Red signal bars (0/4)     "Connection: Critical" + pulse animation

No numeric packet loss, jitter, or bitrate values shown to participant.
Chip is informational only.
```

### 17.2 Network Advisory Toast (Flutter)

```
Widget Name:    NetworkAdvisoryToast
Trigger:        NETWORK_ADVISORY WebSocket event
Position:       Top of screen, non-blocking overlay
Duration:       8 seconds (auto-dismiss), or until RESOLVED event

Severity styles:
    WARNING  → Amber background, ⚠ icon
    CRITICAL → Red background, ⚡ icon, haptic feedback
    RESOLVED → Green background, ✓ icon, 3s dismiss
```

### 17.3 Codec Status Chip (Flutter — Debug/Dev Mode Only)

```
Widget Name:    CodecStatusChip
Visible in:     DEBUG builds only. Never in production builds.
Shows:          Current bitrate kbps, FEC status, DTX status, pressure level
Purpose:        QA testing and verification of adaptation ladder
```

---

## SECTION 18 — OBSERVABILITY REQUIREMENTS (NON-OPTIONAL)

### 18.1 Prometheus Metrics

```
backpressure_pressure_level_gauge{session_id, participant_id, level}
backpressure_adaptations_total{session_id, direction}        -- direction: UP | DOWN
backpressure_forfeit_risk_events_total{session_id, risk_level}
backpressure_network_collapse_events_total{session_id}
backpressure_network_recovery_events_total{session_id}
backpressure_stats_samples_received_total{session_id}
backpressure_clickhouse_write_latency_ms
backpressure_redis_write_latency_ms
backpressure_websocket_command_latency_ms
backpressure_codec_adapt_ack_latency_ms
backpressure_active_sessions_gauge
```

### 18.2 Grafana Dashboard Requirements (media QoS — Section VIII)

```
Dashboard: "GD Audio Backpressure — Live"
Panels:
    1. Per-participant pressure level heatmap (real-time)
    2. Network collapse events per session (time-series)
    3. Adaptation events per session (downgrade vs. upgrade)
    4. Average packet loss distribution (percentile chart)
    5. Forfeit risk events vs. actual forfeitures (correlation)
    6. ClickHouse: L4 time percentage by session (trend)
    7. ClickHouse: Participant-level QoS summary table (post-session)
```

### 18.3 Loki Structured Logs

```json
{
    "level":          "INFO | WARN | ERROR",
    "service":        "audio-backpressure-control-agent",
    "session_id":     "uuid",
    "participant_id": "uuid",
    "event":          "event_name",
    "pressure_level": "L0|L1|L2|L3|L4",
    "latency_ms":     integer,
    "timestamp":      "ISO8601"
}
```

### 18.4 OpenTelemetry Traces

Each stats sample processing cycle produces one trace span covering:
- WebSocket receipt
- Redis read (history)
- Pressure level classification
- Hysteresis evaluation
- Redis write (new level)
- Adaptation command dispatch (if triggered)
- ClickHouse write
- Kafka emit

Target: end-to-end stats-to-adaptation command latency < 150ms P99

---

## SECTION 19 — SECURITY BASELINE

```
WebSocket Auth:         JWT required on handshake.
                        Unauthenticated connections rejected immediately.
                        Stats from unauthenticated clients: DISCARDED.

RTCP Stats:             Numeric scalars only transmitted to backend.
                        No raw packet data transmitted.
                        No audio content transmitted.

Codec Commands:         Backend is sole authority.
                        Frontend cannot self-initiate codec downgrade.
                        Frontend cannot request bitrate increase.

Redis Keys:             Internal Kubernetes service only.
                        realtime namespace NetworkPolicy enforced.
                        No external read access.

PostgreSQL:             Row-level security per tenant_id.
                        Append-only for audit tables.
                        No delete permitted by service role.

ClickHouse:             Internal analytics access only.
                        Not exposed to participants or public APIs.

Rate Limiting:          Max 1 stats sample per 1 second per participant.
                        Burst above this: DISCARD, log RATE_LIMIT_BREACH.

Forfeit Risk Flags:     Written to Redis only.
                        Never exposed to participants via any API.
                        Read only by AUDIO_STREAM_ROUTING_AGENT.
```

---

## SECTION 20 — DEPLOYMENT SPECIFICATION

### 20.1 Kubernetes Manifest (Reference)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: audio-backpressure-control-agent
  namespace: realtime
spec:
  replicas: 2
  selector:
    matchLabels:
      app: audio-backpressure-control-agent
  template:
    metadata:
      labels:
        app: audio-backpressure-control-agent
    spec:
      containers:
        - name: agent
          image: ecoskiller/audio-backpressure-control-agent:latest
          ports:
            - containerPort: 8082
          env:
            - name: REDIS_URL
              valueFrom:
                secretKeyRef:
                  name: redis-secret
                  key: url
            - name: PG_URL
              valueFrom:
                secretKeyRef:
                  name: postgres-secret
                  key: realtime-dsn
            - name: KAFKA_BROKER
              value: "kafka.ops.svc.cluster.local:9092"
            - name: CLICKHOUSE_URL
              valueFrom:
                secretKeyRef:
                  name: clickhouse-secret
                  key: url
            - name: STATS_POLL_INTERVAL_MS
              value: "2000"
            - name: ADAPTATION_GRACE_PERIOD_S
              value: "10"
            - name: HYSTERESIS_DOWNGRADE_READINGS
              value: "2"
            - name: HYSTERESIS_UPGRADE_READINGS
              value: "5"
          readinessProbe:
            httpGet:
              path: /health
              port: 8082
            initialDelaySeconds: 5
            periodSeconds: 10
          livenessProbe:
            httpGet:
              path: /health
              port: 8082
            initialDelaySeconds: 15
            periodSeconds: 20
          resources:
            requests:
              cpu: "150m"
              memory: "256Mi"
            limits:
              cpu: "500m"
              memory: "512Mi"
```

### 20.2 Service

```yaml
apiVersion: v1
kind: Service
metadata:
  name: audio-backpressure-control-agent
  namespace: realtime
spec:
  selector:
    app: audio-backpressure-control-agent
  ports:
    - port: 8082
      targetPort: 8082
  type: ClusterIP
```

---

## SECTION 21 — INTERN EXECUTION INSTRUCTIONS

### Step 1 — Prerequisites
```bash
docker --version
kubectl version --client
node --version       # Must be 18+
npm --version
```

### Step 2 — Project Location
```
/backend/services/realtime/audio_backpressure_control_agent/
```

### Step 3 — Start Service (Local Dev)
```bash
cd /backend/services/realtime/audio_backpressure_control_agent/
cp .env.example .env     # fill REDIS_URL, KAFKA_BROKER, PG_URL, CLICKHOUSE_URL
npm install
npm run dev              # starts on port 8082
```

### Step 4 — Expected Output
```
[AUDIO_BACKPRESSURE_CONTROL_AGENT] Listening on port 8082
[AUDIO_BACKPRESSURE_CONTROL_AGENT] Redis connected: OK
[AUDIO_BACKPRESSURE_CONTROL_AGENT] PostgreSQL connected: OK
[AUDIO_BACKPRESSURE_CONTROL_AGENT] Kafka producer ready
[AUDIO_BACKPRESSURE_CONTROL_AGENT] ClickHouse connected: OK
[AUDIO_BACKPRESSURE_CONTROL_AGENT] Health endpoint: http://localhost:8082/health
```

### Step 5 — Health Check
```bash
curl http://localhost:8082/health
# Expected:
# {
#   "status": "UP",
#   "redis": "OK",
#   "postgres": "OK",
#   "kafka": "OK",
#   "clickhouse": "OK"
# }
```

### Step 6 — Run Tests
```bash
npm test
# Expected: ALL TESTS PASSED
```

### Step 7 — ENV Variables Reference

| Variable                       | Purpose                                           | Default |
|--------------------------------|---------------------------------------------------|---------|
| `REDIS_URL`                    | Redis connection string                           | req'd   |
| `PG_URL`                       | PostgreSQL DSN for realtime schema                | req'd   |
| `KAFKA_BROKER`                 | Kafka broker address                              | req'd   |
| `CLICKHOUSE_URL`               | ClickHouse HTTP interface URL                     | req'd   |
| `STATS_POLL_INTERVAL_MS`       | How often browser sends stats                     | 2000    |
| `ADAPTATION_GRACE_PERIOD_S`    | Seconds before CRITICAL triggers forfeit signal   | 10      |
| `HYSTERESIS_DOWNGRADE_READINGS`| Readings needed to confirm worsening              | 2       |
| `HYSTERESIS_UPGRADE_READINGS`  | Readings needed to confirm improvement            | 5       |
| `CLICKHOUSE_BUFFER_MAX_ROWS`   | Max buffered rows during ClickHouse outage        | 1000    |

Failure at any step → **STOP EXECUTION** → report to senior developer.

---

## SECTION 22 — THREE-AGENT SEQUENCING CONTRACT

```
Complete GD Audio Processing Layer — Execution Order:

T - 20:00  AUDIO_PREPROCESSING_AGENT activates
               Delivers constraint manifest
               Runs preflight checks (device, signal, bandwidth)

T - 00:30  AUDIO_PREPROCESSING_AGENT completes admission gate
               Writes: preprocessing:gate_complete:{session_id} = YES
               Emits: preprocessing.gate.complete

T - 00:00  AUDIO_STREAM_ROUTING_AGENT activates
               Reads: gate_complete flag (blocks until YES)
               Begins token grant sequence

T + 00:00  AUDIO_BACKPRESSURE_CONTROL_AGENT activates
               Receives: BEGIN_STATS_COLLECTION for all admitted participants
               Starts RTCP polling for all participants
               Begins independent per-participant pressure classification

[SESSION RUNNING — all three agents operate concurrently]

    PREPROCESSING_AGENT:   monitors live quality (Section 9)
    ROUTING_AGENT:         manages speaking tokens
    BACKPRESSURE_AGENT:    manages network adaptation
                           signals ROUTING_AGENT via forfeit_risk Redis flags

T + END    GD session terminates (ROUTING_AGENT emits gd.completed)
               BACKPRESSURE_AGENT:
                   receives STOP_STATS_COLLECTION
                   computes per-participant QoS summaries
                   writes PostgreSQL qos_session_summary
                   writes ClickHouse media_qos_session_summary
                   emits Kafka: backpressure.session_qos_summary (per participant)
                   clears all Redis keys for session
```

---

## SECTION 23 — FINAL EXECUTION RULES (INVIOLABLE)

```
1.  Stats are collected every 2 seconds for every participant.              NON-NEGOTIABLE.
2.  Pressure level classification is purely threshold-based. Zero AI.      NON-NEGOTIABLE.
3.  Hysteresis: 2 readings to downgrade. 5 readings to upgrade.            NON-NEGOTIABLE.
4.  Codec downgrade: one level at a time. No skipping.                     NON-NEGOTIABLE.
5.  Codec upgrade: one level at a time. No skipping.                       NON-NEGOTIABLE.
6.  6kbps OPUS is the absolute floor. No further reduction.                NON-NEGOTIABLE.
7.  32kbps CBR OPUS is the ceiling at L0. No exceeding.                    NON-NEGOTIABLE.
8.  Codec commands are backend-issued. Participants cannot self-adapt.      NON-NEGOTIABLE.
9.  Forfeit risk flags are advisory only. Forfeit belongs to ROUTING_AGENT. NON-NEGOTIABLE.
10. Network quality data never reaches the Scoring Engine.                 NON-NEGOTIABLE.
11. Network advisories are private. Other participants never see them.      NON-NEGOTIABLE.
12. ADAPTATION_GRACE_PERIOD is 10 seconds. This value is locked.           NON-NEGOTIABLE.
13. All stats are numeric scalars. Raw audio never transmitted.             NON-NEGOTIABLE.
14. ClickHouse receives QoS data. Scoring Engine receives zero QoS data.   NON-NEGOTIABLE.
15. Session survives individual participant L4 collapse for grace period.   NON-NEGOTIABLE.
```

---

## SECTION 24 — BINDING DECLARATIONS

```
Bound to ECOSKILLER Master Prompt:       v12.0 UNIFIED
Bound to Law:                            SECTION R59 — OFFLINE-FIRST & LOW-BANDWIDTH
Bound to Service Contract:               Voice GD Orchestrator (Service #7)
Bound to Service Contract:               Analytics Service (Service #11)
Bound to Infrastructure Law:             R1 Technology Stack Lock
Bound to Security Baseline:              Section IX of Infrastructure Audit
Bound to Observability Law:              Section VIII (NON-OPTIONAL)
Bound to Analytics Target:               ClickHouse (media QoS dashboards)
Bound to Scoring Engine Interface:       Service #9 — receives ZERO network QoS data
Bound to Event Schema Registry:          backpressure.* event namespace
Bound to Kubernetes Namespace:           realtime
Bound to Jitsi Media Stack:              Self-hosted, Section IV (LOCKED)
Bound to Upstream Agent:                 AUDIO_PREPROCESSING_AGENT v1.0.0
Bound to Parallel Agent:                 AUDIO_STREAM_ROUTING_AGENT v1.0.0
Forfeit Sequencing Contract:             forfeit_risk Redis flag → ROUTING_AGENT acts
QoS Analytics Contract:                  session summary emitted at gd.completed
```

---

## SECTION 25 — AGENT SEAL

```
╔══════════════════════════════════════════════════════════════════════╗
║         AUDIO_BACKPRESSURE_CONTROL_AGENT — PROMPT SEALED            ║
║                                                                      ║
║  Version:       v1.0.0                                               ║
║  System:        ECOSKILLER Antigravity Audio Processing              ║
║  Status:        FINAL · LOCKED · DETERMINISTIC                       ║
║  Mutation:      Add-only via version bump ONLY                       ║
║  Authority:     Human declaration only                               ║
║                                                                      ║
║  WebRTC is the transport.                                            ║
║  RTCP is the nervous system.                                         ║
║  Redis is the pressure gauge.                                        ║
║  OPUS is the instrument — tunable under fire.                        ║
║  ClickHouse is the flight recorder.                                  ║
║  The backend is the pilot — not the participant.                     ║
║                                                                      ║
║  India's Tier-2 city on 4G and the metro user on fibre              ║
║  enter the same GD on equal acoustic footing.                        ║
║                                                                      ║
║  Congestion is invisible.                                            ║
║  Adaptation is silent.                                               ║
║  Fairness is structural.                                             ║
║  Network advantage has no entry point.                               ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

*End of AUDIO_BACKPRESSURE_CONTROL_AGENT Sealed Prompt — ECOSKILLER v12.0*
