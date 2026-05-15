# AUDIO_PREPROCESSING_AGENT
## ECOSKILLER — ANTIGRAVITY AUDIO PROCESSING SUBSYSTEM
### Status: FINAL · LOCKED · SEALED · DETERMINISTIC
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only
### Agent Version: v1.0.0

---

> **SEAL DECLARATION**
> This document is a fully locked, self-contained agent prompt for the
> AUDIO_PREPROCESSING_AGENT component within the Ecoskiller Audio Processing layer.
> No clause may be removed, weakened, reworded, or reinterpreted.
> Any modification without version bump → INVALID EXECUTION.
>
> Bound to: ECOSKILLER MASTER EXECUTION PROMPT v12.0 (UNIFIED)
> Bound to: Voice GD Orchestrator (Section V, Core Microservice #7)
> Bound to: AUDIO_STREAM_ROUTING_AGENT v1.0.0 (upstream dependency)
> Bound to: AUTOMATED VOICE GROUP DISCUSSION SYSTEM (Jitsi + Rule-Driven Orchestration)
> Bound to: Dojo Match Engine (Core Microservice #8)
>
> This agent operates BEFORE audio enters the Jitsi SFU.
> The AUDIO_STREAM_ROUTING_AGENT operates AFTER audio is inside the SFU.
> These two agents are adjacent, non-overlapping, and never merged.

---

## SECTION 1 — AGENT IDENTITY

```
Agent Name:              AUDIO_PREPROCESSING_AGENT
System Context:          Ecoskiller Antigravity Audio Processing Layer
Domain:                  realtime (Kubernetes namespace)
Parent Service:          Voice GD Orchestrator / Dojo Match Engine
Layer Position:          PRE-SFU — operates at browser capture layer
                         and browser-to-SFU pipeline entry gate
Execution Model:         Deterministic Rule Engine — NO AI INFERENCE
Processing Mode:         Signal quality control, compliance gating,
                         bandwidth normalization, device readiness validation
Adjacent Agent:          AUDIO_STREAM_ROUTING_AGENT (downstream)
Media Protocol:          WebRTC (browser MediaStream API layer)
SFU Backend:             Self-hosted Jitsi Videobridge
                         (receives only preprocessed, gated streams)
State Store:             Redis (preprocessing state, device readiness flags)
Database:                PostgreSQL (device capability logs, quality audit)
Event Bus:               Apache Kafka (preprocessing.* event namespace)
```

---

## SECTION 2 — AGENT PURPOSE (NON-NEGOTIABLE)

The AUDIO_PREPROCESSING_AGENT controls and validates the audio signal
**from the moment the participant's microphone is captured by the browser**
to the moment that audio stream is **admitted into the Jitsi SFU**.

Its mandate is threefold:

```
MANDATE 1 — QUALITY GATE
    Ensure only clean, usable audio enters the conference room.
    Reject or flag streams that fail minimum signal quality thresholds.

MANDATE 2 — FAIRNESS NORMALISATION
    Ensure no participant enters the GD with an unfair hardware advantage.
    Loud microphones, professional equipment, and silent rooms
    must not create asymmetric advantage over budget device users.
    All participants enter the discussion on equal acoustic footing.

MANDATE 3 — COMPLIANCE ENFORCEMENT
    Ensure all browser-side audio constraints required by the GD protocol
    are applied and confirmed before the participant is admitted to the session.
    Non-compliant streams are blocked at the gate — not mid-session.
```

This agent does NOT:

```
Decide who speaks next                  → AUDIO_STREAM_ROUTING_AGENT
Grant or revoke speaking tokens         → AUDIO_STREAM_ROUTING_AGENT
Force mute or unmute via Jitsi API      → AUDIO_STREAM_ROUTING_AGENT
Evaluate speech content                 → PROHIBITED (entire system)
Apply AI inference to audio             → PROHIBITED (entire system)
Store raw audio                         → PROHIBITED (entire system)
```

---

## SECTION 3 — SYSTEM BOUNDARY (LOCKED)

### 3.1 What the Agent OWNS

| Responsibility                              | Owner                         |
|---------------------------------------------|-------------------------------|
| Device capability preflight check          | AUDIO_PREPROCESSING_AGENT     |
| MediaStream constraint enforcement          | AUDIO_PREPROCESSING_AGENT     |
| Echo cancellation configuration             | AUDIO_PREPROCESSING_AGENT     |
| Noise suppression configuration             | AUDIO_PREPROCESSING_AGENT     |
| Auto gain control configuration             | AUDIO_PREPROCESSING_AGENT     |
| Volume normalisation (target dBFS)          | AUDIO_PREPROCESSING_AGENT     |
| Silence / no-mic detection at join          | AUDIO_PREPROCESSING_AGENT     |
| Signal quality scoring (SNR gate)           | AUDIO_PREPROCESSING_AGENT     |
| Bandwidth compliance check                  | AUDIO_PREPROCESSING_AGENT     |
| Stream admission gate (allow / block)       | AUDIO_PREPROCESSING_AGENT     |
| Device readiness log (PostgreSQL)           | AUDIO_PREPROCESSING_AGENT     |
| Preprocessing failure event emission        | AUDIO_PREPROCESSING_AGENT     |
| Participant preflight result reporting      | AUDIO_PREPROCESSING_AGENT     |

### 3.2 What the Agent DOES NOT OWN

| Out-of-scope responsibility                  | True Owner                    |
|----------------------------------------------|-------------------------------|
| SFU stream routing                           | Jitsi Videobridge             |
| NAT traversal                                | coturn (TURN/STUN)            |
| Speaking token grants                        | AUDIO_STREAM_ROUTING_AGENT    |
| Force mute / unmute                          | AUDIO_STREAM_ROUTING_AGENT    |
| GD session state machine                     | Redis + GD Orchestrator       |
| Session scoring                              | Scoring Engine (Service #9)   |
| Room creation                                | Voice GD Orchestrator         |
| Participant authentication                   | Auth Service (Service #1)     |
| Media token issuance                         | Voice GD Orchestrator         |
| Raw audio encoding                           | Browser WebRTC stack          |

### 3.3 CRITICAL BOUNDARY PRINCIPLE (INVIOLABLE)

```
The AUDIO_PREPROCESSING_AGENT is the door.
The AUDIO_STREAM_ROUTING_AGENT is the traffic controller inside the room.

The door checks every person before entry.
The traffic controller manages flow once inside.

They share no logic.
They share no state.
They communicate only through PostgreSQL readiness flags
and Kafka preprocessing.* events.
```

---

## SECTION 4 — AGENT ARCHITECTURE

### 4.1 Technology Stack (Locked — inherits from ECOSKILLER R1)

```
Runtime (Backend Coordination):   Node.js
Browser Layer (Constraint Apply): JavaScript WebRTC API (MediaDevices + MediaStream)
State Store:                      Redis (preflight flags, device readiness cache)
Persistence:                      PostgreSQL (device audit, quality logs)
Event Bus:                        Apache Kafka (preprocessing.* namespace)
Observability:                    Prometheus + Loki + OpenTelemetry
Kubernetes Namespace:             realtime
```

### 4.2 Two-Tier Architecture

```
TIER 1 — BROWSER SIDE (JavaScript, runs in participant's browser)
    ├── MediaDevices.getUserMedia() with constraint manifest
    ├── AudioContext API (measurement only — no recording)
    ├── AnalyserNode (SNR measurement, silence detection)
    └── WebSocket → Backend preflight result reporter

TIER 2 — BACKEND COORDINATION (Node.js service, Kubernetes realtime namespace)
    ├── Preflight result receiver (WebSocket)
    ├── Admission gate controller (allow / deny join)
    ├── Redis flag writer (device_ready:{session_id}:{participant_id})
    ├── PostgreSQL audit writer
    └── Kafka event emitter (preprocessing.*)
```

### 4.3 Dependency Graph

```
AUDIO_PREPROCESSING_AGENT
    ├── READS FROM         → Redis (session_config, admitted_participants list)
    ├── WRITES TO          → Redis (device_ready flags, preflight_result flags)
    ├── RECEIVES FROM      → Browser WebSocket (preflight measurement results)
    ├── WRITES AUDIT TO    → PostgreSQL (device_capability_log, preflight_audit)
    ├── EMITS EVENTS TO    → Kafka (preprocessing.preflight.passed,
    │                                preprocessing.preflight.failed,
    │                                preprocessing.stream.admitted,
    │                                preprocessing.stream.blocked)
    └── SIGNALS TO         → AUDIO_STREAM_ROUTING_AGENT
                             (via Redis flag: stream_admitted:{session_id}:{pid})
```

---

## SECTION 5 — ANTIGRAVITY PREPROCESSING PHILOSOPHY

### 5.1 The Fairness Problem

In any multi-participant voice session, participants arrive with:

```
Hardware variance:
    - Professional USB microphone (flat response, high SNR)
    - Laptop built-in mic (narrow range, low SNR)
    - Budget earphone mic (compressed, narrow bandwidth)
    - Mobile phone (variable, position-dependent)

Environment variance:
    - Silent office / studio
    - Home with background noise (AC, TV, traffic, family)
    - Public space (café, campus)
    - Outdoor environment

Network variance:
    - High-bandwidth wired connection (100 Mbps+)
    - Mobile 4G (variable 5–50 Mbps)
    - Mobile 3G or poor WiFi (1–5 Mbps)
```

Without preprocessing, these variances create structural unfairness:
a participant with a good microphone in a quiet room sounds authoritative,
while a participant on a budget device in a noisy environment sounds uncertain.

### 5.2 Antigravity Normalisation Mandate

```
RULE: Every participant's audio stream entering the Jitsi SFU
      must conform to the ECOSKILLER Normalised Audio Profile.

RULE: No participant's hardware or environment may create
      a perceptible advantage over others in the GD evaluation.

RULE: The Scoring Engine receives behavioural data only —
      mic-open duration, turn completion, interrupt counts.
      It never receives audio quality judgements.

RULE: Normalisation is applied pre-SFU, invisibly, deterministically.
      Participants are unaware of normalisation occurring.
```

### 5.3 ECOSKILLER Normalised Audio Profile (LOCKED)

```
Target Output Level:        -18 dBFS (RMS)
Peak Ceiling:               -6 dBFS
Sample Rate:                48,000 Hz
Bit Depth:                  16-bit PCM
Channel Configuration:      Mono (forced — stereo forbidden in GD)
Echo Cancellation:          ENABLED (browser AEC)
Noise Suppression:          ENABLED (browser NS)
Auto Gain Control:          ENABLED (AGC target = -18 dBFS)
High-Pass Filter:           ENABLED (cutoff = 80 Hz — removes rumble)
Minimum SNR Gate:           12 dB (streams below this are flagged)
Maximum Silence Duration:   5 seconds before silence alert
Codec:                      OPUS (48kHz, 32kbps CBR mono — forced by SFU token)
Packet Loss Resilience:     FEC enabled via OPUS configuration
```

These values are stored in PostgreSQL `gd_audio_profile` and loaded at session init.
They cannot be modified by participants.
They cannot be modified mid-session.

---

## SECTION 6 — PREFLIGHT CHECK SYSTEM (SEALED ALGORITHM)

### 6.1 Purpose

Every participant must pass a mandatory audio preflight check
before their stream is admitted to the GD room.

Preflight is executed during the preparation timer phase
(10–20 minutes before GD start, as defined in the GD Orchestrator).

No participant may enter the Jitsi room without a `PREFLIGHT_PASSED` flag
recorded in Redis.

### 6.2 Preflight Check Sequence (Browser Tier)

```
FUNCTION run_preflight(session_id, participant_id):

    STEP 1 — DEVICE EXISTENCE CHECK
        result = await navigator.mediaDevices.enumerateDevices()
        audio_inputs = result.filter(d => d.kind === 'audioinput')

        IF audio_inputs.length === 0:
            → EMIT preflight_result: FAILED, reason: NO_MIC_DETECTED
            → DISPLAY UI: "No microphone found. Connect a microphone to continue."
            RETURN FAILED

    STEP 2 — PERMISSION REQUEST
        try:
            stream = await navigator.mediaDevices.getUserMedia({
                audio: ECOSKILLER_CONSTRAINT_MANIFEST,
                video: false
            })
        catch PermissionDeniedError:
            → EMIT preflight_result: FAILED, reason: PERMISSION_DENIED
            → DISPLAY UI: "Microphone access denied. Allow access to continue."
            RETURN FAILED
        catch NotFoundError:
            → EMIT preflight_result: FAILED, reason: DEVICE_NOT_FOUND
            RETURN FAILED

    STEP 3 — SIGNAL LEVEL MEASUREMENT (5 SECONDS)
        audioContext = new AudioContext({ sampleRate: 48000 })
        source = audioContext.createMediaStreamSource(stream)
        analyser = audioContext.createAnalyser()
        analyser.fftSize = 2048
        source.connect(analyser)

        // Measure RMS over 5 seconds (100 samples × 50ms intervals)
        rms_samples = []
        FOR 100 iterations, 50ms apart:
            data = new Float32Array(analyser.frequencyBinCount)
            analyser.getFloatTimeDomainData(data)
            rms = sqrt(mean(data.map(x => x * x)))
            rms_samples.push(rms)

        rms_average = mean(rms_samples)
        peak_rms = max(rms_samples)
        silence_count = rms_samples.filter(s => s < SILENCE_THRESHOLD).length

    STEP 4 — SNR ESTIMATION
        noise_floor_rms = mean(rms_samples.slice(0, 10))  // first 0.5s = ambient
        speech_rms = mean(rms_samples.slice(10, 100))
        snr_db = 20 * log10(speech_rms / (noise_floor_rms + EPSILON))

    STEP 5 — THRESHOLD EVALUATION

        checks = {
            has_signal:       rms_average > MIN_SIGNAL_THRESHOLD,
            not_silent:       silence_count < 90,               // <90% silence
            snr_adequate:     snr_db >= SNR_GATE_DB (12 dB),
            level_in_range:   rms_average within [-30 dBFS, -6 dBFS]
        }

        IF all checks pass:
            preflight_status = "PASSED"
        ELSE IF snr_db < 6:
            preflight_status = "FAILED"
            reason = "SIGNAL_TOO_NOISY"
        ELSE IF rms_average < MIN_SIGNAL_THRESHOLD:
            preflight_status = "FAILED"
            reason = "NO_SIGNAL_DETECTED"
        ELSE IF silence_count >= 90:
            preflight_status = "FAILED"
            reason = "MIC_APPEARS_MUTED"
        ELSE:
            preflight_status = "WARNED"  // admitted with quality flag
            reason = "SUBOPTIMAL_SIGNAL"

    STEP 6 — REPORT TO BACKEND
        SEND WebSocket to backend:
        {
            "event":            "PREFLIGHT_RESULT",
            "session_id":       session_id,
            "participant_id":   participant_id,
            "status":           preflight_status,
            "reason":           reason,
            "rms_average_dbfs": to_dBFS(rms_average),
            "peak_rms_dbfs":    to_dBFS(peak_rms),
            "snr_db":           snr_db,
            "silence_percent":  (silence_count / 100) * 100,
            "device_label":     audio_inputs[0].label ?? "UNKNOWN",
            "timestamp":        NOW_UTC()
        }

    STEP 7 — CLOSE MEASUREMENT CONTEXT
        audioContext.close()
        // DO NOT retain stream reference
        // DO NOT record any audio
        // Measurement only — zero audio storage
```

### 6.3 Preflight Result Handling (Backend Tier)

```
FUNCTION handle_preflight_result(payload):

    // Write to PostgreSQL
    INSERT INTO device_capability_log:
        session_id, participant_id, status, reason,
        rms_average_dbfs, peak_rms_dbfs, snr_db,
        silence_percent, device_label, timestamp

    IF payload.status == "PASSED":
        SET Redis:
            KEY = "preprocessing:device_ready:{session_id}:{participant_id}"
            VALUE = "PASSED"
            TTL = session_max_duration_seconds + 600

        EMIT Kafka: preprocessing.preflight.passed

    ELSE IF payload.status == "WARNED":
        SET Redis:
            KEY = "preprocessing:device_ready:{session_id}:{participant_id}"
            VALUE = "WARNED"
            TTL = session_max_duration_seconds + 600

        SEND WebSocket to participant:
        {
            "event":   "PREFLIGHT_WARNING",
            "message": "Audio quality is suboptimal. Session will proceed.
                        Ensure microphone is close and background noise is reduced.",
            "reason":  payload.reason
        }
        EMIT Kafka: preprocessing.preflight.warned

    ELSE IF payload.status == "FAILED":
        SET Redis:
            KEY = "preprocessing:device_ready:{session_id}:{participant_id}"
            VALUE = "FAILED"
            TTL = 3600

        SEND WebSocket to participant:
        {
            "event":       "PREFLIGHT_FAILED",
            "reason":      payload.reason,
            "ui_message":  PREFLIGHT_FAIL_MESSAGE_MAP[payload.reason],
            "retry_allowed": true,
            "retry_window_closes_at": session_start_time - 120  // 2 min before start
        }
        EMIT Kafka: preprocessing.preflight.failed
```

### 6.4 Preflight Failure UI Message Map (Locked)

```
NO_MIC_DETECTED:
    "No microphone detected on your device.
     Connect a microphone or headset and click Retry."

PERMISSION_DENIED:
    "Microphone access was blocked by your browser.
     Click the lock icon in your browser address bar,
     allow microphone access, then click Retry."

DEVICE_NOT_FOUND:
    "Your microphone could not be accessed.
     Check that it is connected and not in use by another app."

SIGNAL_TOO_NOISY:
    "Too much background noise detected.
     Move to a quieter location or use a headset, then click Retry."

NO_SIGNAL_DETECTED:
    "No audio signal detected.
     Ensure your microphone is not muted and is working, then click Retry."

MIC_APPEARS_MUTED:
    "Your microphone appears to be muted in hardware or OS settings.
     Unmute it and click Retry."
```

### 6.5 Retry Policy

```
Maximum retries:    3
Retry window:       Open until (session_start_time - 120 seconds)
After window close: No further retries permitted
                    Participant joins as SPECTATOR if still FAILED

SPECTATOR status:
    - Admitted to Jitsi room (audio-only receive)
    - Microphone permanently muted by AUDIO_STREAM_ROUTING_AGENT
    - Speaking tokens not allocated
    - Logged in PostgreSQL as PREFLIGHT_FAILED_SPECTATOR
    - Scoring Engine marks participant: did_not_qualify = true
```

---

## SECTION 7 — CONSTRAINT MANIFEST (LOCKED)

### 7.1 ECOSKILLER_CONSTRAINT_MANIFEST

Applied to every `getUserMedia()` call across all participant browsers.
This manifest is served from the backend (not hardcoded in frontend).
It is loaded from PostgreSQL `gd_audio_profile` at session init.
Frontend fetches it via authenticated REST call before preflight begins.

```javascript
ECOSKILLER_CONSTRAINT_MANIFEST = {
    audio: {
        // Device selection
        deviceId:               "default",         // use OS default mic

        // Channel enforcement
        channelCount:           { exact: 1 },       // mono — stereo forbidden

        // Sample rate
        sampleRate:             { ideal: 48000 },

        // Echo cancellation — browser AEC
        echoCancellation:       { exact: true },

        // Noise suppression — browser NS
        noiseSuppression:       { exact: true },

        // Auto gain control — normalises input level toward target
        autoGainControl:        { exact: true },

        // Latency target (realtime priority)
        latency:                { ideal: 0.01 },    // 10ms target

        // Volume target — AGC will attempt to reach -18 dBFS RMS
        // (Cannot be set via constraint — enforced by AGC + post-processing)
    },
    video: false                                    // video disabled — always
}
```

### 7.2 Constraint Application Verification

After `getUserMedia()` resolves, the browser-side agent must verify
that the browser honoured the constraints:

```javascript
FUNCTION verify_constraints(stream):

    track = stream.getAudioTracks()[0]
    settings = track.getSettings()
    capabilities = track.getCapabilities()

    verification = {
        channelCount_ok:       settings.channelCount === 1,
        echoCancellation_ok:   settings.echoCancellation === true,
        noiseSuppression_ok:   settings.noiseSuppression === true,
        autoGainControl_ok:    settings.autoGainControl === true,
        sampleRate_ok:         settings.sampleRate >= 44100
    }

    failed = Object.entries(verification).filter(([k, v]) => !v)

    IF failed.length > 0:
        LOG to backend: CONSTRAINT_VERIFICATION_FAILURE
        // Do not block admission — some browsers report partial capability
        // Flag in PostgreSQL for audit
        // Session proceeds — constraint gaps logged only
    ELSE:
        LOG to backend: CONSTRAINT_VERIFICATION_PASSED
```

---

## SECTION 8 — STREAM ADMISSION GATE (SEALED)

### 8.1 Gate Trigger

The stream admission gate is evaluated at one moment only:

```
Gate activates at: T = session_start_time - 30 seconds
```

At this moment the backend evaluates every admitted participant's
`preprocessing:device_ready:{session_id}:{participant_id}` Redis flag.

### 8.2 Gate Decision Logic

```
FUNCTION evaluate_admission_gate(session_id):

    participants = QUERY PostgreSQL:
        SELECT participant_id FROM gd_participants
        WHERE session_id = :session_id AND join_status = 'ADMITTED'

    FOR EACH participant_id:

        flag = GET Redis "preprocessing:device_ready:{session_id}:{participant_id}"

        SWITCH flag:

            CASE "PASSED":
                SET Redis "preprocessing:stream_admitted:{session_id}:{participant_id}" = "YES"
                EMIT Kafka: preprocessing.stream.admitted
                LOG PostgreSQL: admission_gate_log (ADMITTED)

            CASE "WARNED":
                SET Redis "preprocessing:stream_admitted:{session_id}:{participant_id}" = "YES"
                EMIT Kafka: preprocessing.stream.admitted_with_warning
                LOG PostgreSQL: admission_gate_log (ADMITTED_WITH_WARNING)

            CASE "FAILED":
                SET Redis "preprocessing:stream_admitted:{session_id}:{participant_id}" = "SPECTATOR"
                SEND WebSocket to participant:
                    {
                        "event":   "ADMITTED_AS_SPECTATOR",
                        "reason":  "PREFLIGHT_NOT_PASSED"
                    }
                EMIT Kafka: preprocessing.stream.blocked
                LOG PostgreSQL: admission_gate_log (SPECTATOR)

            CASE NULL (no flag found — participant never ran preflight):
                SET Redis "preprocessing:stream_admitted:{session_id}:{participant_id}" = "SPECTATOR"
                EMIT Kafka: preprocessing.stream.blocked
                LOG PostgreSQL: admission_gate_log (SPECTATOR, reason: NO_PREFLIGHT)

    // Signal to AUDIO_STREAM_ROUTING_AGENT that gate evaluation is complete
    SET Redis "preprocessing:gate_complete:{session_id}" = "YES"
    EMIT Kafka: preprocessing.gate.complete { session_id, admitted_count, spectator_count }
```

### 8.3 Gate Completion Contract

The `AUDIO_STREAM_ROUTING_AGENT` must NOT begin token grants
until it reads `preprocessing:gate_complete:{session_id}` = "YES" from Redis.

This is a hard sequencing dependency between the two agents.

---

## SECTION 9 — LIVE STREAM QUALITY MONITOR (POST-ADMISSION)

### 9.1 Purpose

After a participant is admitted and the GD is running,
the AUDIO_PREPROCESSING_AGENT continues to monitor stream health
for the purpose of failure detection and audit — not re-evaluation.

No admission decisions are made mid-session.
Monitor output is logging only.

### 9.2 Monitor Behaviour (Browser Tier, Runs During GD)

```
FUNCTION live_quality_monitor(session_id, participant_id, stream):

    // Activate only during this participant's speaking token window
    // Receives TOKEN_GRANTED event from AUDIO_STREAM_ROUTING_AGENT via WebSocket

    ON EVENT "TOKEN_GRANTED" where speaker_id == this participant_id:
        START monitoring

    ON EVENT "TOKEN_EXPIRED" OR "FORCE_MUTED":
        STOP monitoring

    WHILE monitoring:
        Every 1000ms:
            measure RMS, peak, silence
            SEND WebSocket to backend:
            {
                "event":          "LIVE_QUALITY_SAMPLE",
                "session_id":     session_id,
                "participant_id": participant_id,
                "rms_dbfs":       current_rms_dbfs,
                "is_silent":      rms < SILENCE_THRESHOLD,
                "timestamp":      NOW_UTC()
            }
```

### 9.3 Backend Live Monitor Handler

```
FUNCTION handle_live_quality_sample(payload):

    // Write to Redis time-series (short TTL — not stored permanently)
    LPUSH Redis "live_quality:{session_id}:{participant_id}" JSON(payload)
    LTRIM Redis "live_quality:{session_id}:{participant_id}" 0 59  // keep last 60s

    // Silence during token detection — feed to AUDIO_STREAM_ROUTING_AGENT
    IF payload.is_silent:
        INCR Redis "live_silence_ticks:{session_id}:{participant_id}"

    // Network drop proxy detection
    // If no sample received for > 3 seconds during active token window
    // → backend already handles via AUDIO_STREAM_ROUTING_AGENT token forfeit
    // This agent only logs the gap for audit
```

---

## SECTION 10 — OPEN DISCUSSION ROUND PREPROCESSING

During the Open Discussion round, the AUDIO_PREPROCESSING_AGENT
does not change its behaviour. The normalisation constraints remain active.
The live monitor remains active for all participants simultaneously.

```
Open Discussion Preprocessing Rules:
    All participant streams:    remain normalised at -18 dBFS target
    All participant streams:    echo cancellation active
    All participant streams:    noise suppression active
    Dominance detection:        NOT a preprocessing function
                                (belongs to AUDIO_STREAM_ROUTING_AGENT)
    Audio mixing:               NOT a preprocessing function
                                (belongs to Jitsi Videobridge SFU)
```

---

## SECTION 11 — BANDWIDTH COMPLIANCE GATE

### 11.1 Purpose

The OPUS codec at 32 kbps CBR mono is the locked audio codec for all GD sessions.
Before any participant's stream is admitted to the SFU, their network
must demonstrate sufficient bandwidth to sustain this codec without degradation.

### 11.2 Bandwidth Check (Browser Tier)

```
FUNCTION check_bandwidth_compliance(session_id, participant_id):

    // Minimum required per participant for OPUS 32kbps mono
    MINIMUM_UPLOAD_KBPS = 64      // 2× codec bitrate — safety headroom
    MINIMUM_DOWNLOAD_KBPS = 256   // receive up to 6 participants × 32kbps + headroom

    // Use WebRTC built-in stats — no external speed test service
    peer_connection = create test RTCPeerConnection to coturn STUN endpoint
    stats = await peer_connection.getStats()

    // Extract available bandwidth from ICE candidate pair stats
    candidate_pair = stats.filter(s => s.type === 'candidate-pair' && s.nominated)
    available_outgoing_kbps = candidate_pair.availableOutgoingBitrate / 1000
    rtt_ms = candidate_pair.currentRoundTripTime * 1000

    compliance = {
        upload_ok:  available_outgoing_kbps >= MINIMUM_UPLOAD_KBPS,
        rtt_ok:     rtt_ms < 400  // > 400ms RTT = poor realtime experience
    }

    SEND WebSocket to backend:
    {
        "event":               "BANDWIDTH_CHECK_RESULT",
        "session_id":          session_id,
        "participant_id":      participant_id,
        "upload_kbps":         available_outgoing_kbps,
        "rtt_ms":              rtt_ms,
        "upload_compliant":    compliance.upload_ok,
        "rtt_compliant":       compliance.rtt_ok,
        "overall_compliant":   compliance.upload_ok && compliance.rtt_ok
    }

    peer_connection.close()
```

### 11.3 Bandwidth Non-Compliance Handling

```
IF overall_compliant == false AND upload_compliant == false:
    DISPLAY UI warning:
        "Your internet connection may be too slow for this session.
         Move closer to your WiFi router or switch to a better connection."
    // Does NOT block admission — flagged for audit only
    LOG PostgreSQL: bandwidth_compliance_log (NON_COMPLIANT)

IF rtt_compliant == false:
    DISPLAY UI warning:
        "High network latency detected. Audio may be delayed.
         Try switching networks if possible."
    // Does NOT block admission — flagged for audit only
    LOG PostgreSQL: bandwidth_compliance_log (HIGH_LATENCY)

// Bandwidth non-compliance never blocks GD admission.
// It is an advisory flag only.
// Blocking is reserved for PREFLIGHT audio signal failure only.
```

---

## SECTION 12 — DATA CAPTURE CONTRACT (LOCKED)

### 12.1 Captured — PostgreSQL Tables

```sql
-- device_capability_log
CREATE TABLE device_capability_log (
    log_id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id          UUID NOT NULL,
    participant_id      UUID NOT NULL,
    preflight_status    TEXT NOT NULL,  -- PASSED | WARNED | FAILED
    reason              TEXT,
    rms_average_dbfs    NUMERIC(6,2),
    peak_rms_dbfs       NUMERIC(6,2),
    snr_db              NUMERIC(6,2),
    silence_percent     NUMERIC(5,2),
    device_label        TEXT,
    constraint_verified BOOLEAN,
    timestamp           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- admission_gate_log
CREATE TABLE admission_gate_log (
    log_id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id          UUID NOT NULL,
    participant_id      UUID NOT NULL,
    gate_decision       TEXT NOT NULL,  -- ADMITTED | ADMITTED_WITH_WARNING | SPECTATOR
    reason              TEXT,
    timestamp           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- bandwidth_compliance_log
CREATE TABLE bandwidth_compliance_log (
    log_id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id          UUID NOT NULL,
    participant_id      UUID NOT NULL,
    upload_kbps         NUMERIC(8,2),
    rtt_ms              NUMERIC(8,2),
    upload_compliant    BOOLEAN,
    rtt_compliant       BOOLEAN,
    overall_compliant   BOOLEAN,
    timestamp           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- constraint_verification_log
CREATE TABLE constraint_verification_log (
    log_id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id          UUID NOT NULL,
    participant_id      UUID NOT NULL,
    channelcount_ok     BOOLEAN,
    echocancellation_ok BOOLEAN,
    noisesuppression_ok BOOLEAN,
    autogaincontrol_ok  BOOLEAN,
    samplerate_ok       BOOLEAN,
    timestamp           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

### 12.2 Captured — Redis Keys (Temporary, TTL-Bounded)

```
preprocessing:device_ready:{session_id}:{participant_id}   → PASSED | WARNED | FAILED
preprocessing:stream_admitted:{session_id}:{participant_id} → YES | SPECTATOR
preprocessing:gate_complete:{session_id}                    → YES
live_quality:{session_id}:{participant_id}                  → last 60 samples (ring)
live_silence_ticks:{session_id}:{participant_id}            → count
```

### 12.3 NOT Captured — Absolute Prohibition

```
Raw audio samples:                 NEVER STORED
Audio waveforms or spectrograms:   NEVER STORED
Voice recordings of any duration:  NEVER STORED
Speech recognition output:         NEVER PERFORMED
Tone, emotion, accent analysis:    NEVER PERFORMED
Speaker identification:            NEVER PERFORMED
AI inference on audio signal:      NEVER PERFORMED
```

The AnalyserNode is used for numeric RMS/SNR measurement only.
It does not record, buffer, or transmit audio data.
It is closed and garbage collected after preflight measurement completes.

Violation of the above → IMMEDIATE SECURITY INCIDENT.

---

## SECTION 13 — KAFKA EVENT SCHEMA (preprocessing.* NAMESPACE)

```json
// preprocessing.preflight.passed
{
    "event":          "preprocessing.preflight.passed",
    "session_id":     "uuid",
    "participant_id": "uuid",
    "snr_db":         14.2,
    "rms_average_dbfs": -22.1,
    "timestamp":      "ISO8601"
}

// preprocessing.preflight.warned
{
    "event":          "preprocessing.preflight.warned",
    "session_id":     "uuid",
    "participant_id": "uuid",
    "reason":         "SUBOPTIMAL_SIGNAL",
    "snr_db":         9.4,
    "timestamp":      "ISO8601"
}

// preprocessing.preflight.failed
{
    "event":          "preprocessing.preflight.failed",
    "session_id":     "uuid",
    "participant_id": "uuid",
    "reason":         "SIGNAL_TOO_NOISY",
    "retry_count":    2,
    "timestamp":      "ISO8601"
}

// preprocessing.stream.admitted
{
    "event":          "preprocessing.stream.admitted",
    "session_id":     "uuid",
    "participant_id": "uuid",
    "admission_type": "FULL | WITH_WARNING",
    "timestamp":      "ISO8601"
}

// preprocessing.stream.blocked
{
    "event":          "preprocessing.stream.blocked",
    "session_id":     "uuid",
    "participant_id": "uuid",
    "reason":         "PREFLIGHT_NOT_PASSED | NO_PREFLIGHT",
    "timestamp":      "ISO8601"
}

// preprocessing.gate.complete
{
    "event":          "preprocessing.gate.complete",
    "session_id":     "uuid",
    "admitted_count": 5,
    "spectator_count": 1,
    "timestamp":      "ISO8601"
}
```

---

## SECTION 14 — WEBSOCKET COMMAND PROTOCOL

### 14.1 Backend → Browser Commands

```json
// Constraint manifest delivery
{
    "event":       "CONSTRAINT_MANIFEST",
    "session_id":  "uuid",
    "manifest":    { ...ECOSKILLER_CONSTRAINT_MANIFEST }
}

// Preflight start instruction
{
    "event":      "BEGIN_PREFLIGHT",
    "session_id": "uuid",
    "duration_s": 5
}

// Preflight warning feedback
{
    "event":      "PREFLIGHT_WARNING",
    "message":    "Audio quality is suboptimal. Session will proceed.",
    "reason":     "SUBOPTIMAL_SIGNAL"
}

// Preflight failure feedback
{
    "event":         "PREFLIGHT_FAILED",
    "reason":        "SIGNAL_TOO_NOISY",
    "ui_message":    "Too much background noise detected...",
    "retry_allowed": true,
    "retry_closes":  "ISO8601"
}

// Admitted as spectator
{
    "event":  "ADMITTED_AS_SPECTATOR",
    "reason": "PREFLIGHT_NOT_PASSED"
}

// Bandwidth advisory
{
    "event":      "BANDWIDTH_ADVISORY",
    "message":    "Your internet connection may be slow.",
    "severity":   "WARNING"
}
```

### 14.2 Browser → Backend Reports

```json
// Preflight result
{
    "event":              "PREFLIGHT_RESULT",
    "session_id":         "uuid",
    "participant_id":     "uuid",
    "status":             "PASSED | WARNED | FAILED",
    "reason":             "string | null",
    "rms_average_dbfs":   -22.1,
    "peak_rms_dbfs":      -14.8,
    "snr_db":             14.2,
    "silence_percent":    12.0,
    "device_label":       "MacBook Pro Microphone",
    "timestamp":          "ISO8601"
}

// Constraint verification result
{
    "event":              "CONSTRAINT_VERIFICATION_RESULT",
    "session_id":         "uuid",
    "participant_id":     "uuid",
    "channelcount_ok":    true,
    "echocancellation_ok":true,
    "noisesuppression_ok":true,
    "autogaincontrol_ok": true,
    "samplerate_ok":      true
}

// Bandwidth check result
{
    "event":            "BANDWIDTH_CHECK_RESULT",
    "session_id":       "uuid",
    "participant_id":   "uuid",
    "upload_kbps":      128.4,
    "rtt_ms":           45.2,
    "upload_compliant": true,
    "rtt_compliant":    true,
    "overall_compliant":true
}

// Live quality sample
{
    "event":          "LIVE_QUALITY_SAMPLE",
    "session_id":     "uuid",
    "participant_id": "uuid",
    "rms_dbfs":       -19.3,
    "is_silent":      false,
    "timestamp":      "ISO8601"
}
```

---

## SECTION 15 — FAILURE HANDLING (NON-NEGOTIABLE)

| Failure Event                          | Agent Action                                                  |
|----------------------------------------|---------------------------------------------------------------|
| Browser blocks mic permission          | PREFLIGHT_FAILED, reason: PERMISSION_DENIED. Retry offered.   |
| No mic hardware found                  | PREFLIGHT_FAILED, reason: NO_MIC_DETECTED. Retry offered.     |
| Signal below minimum SNR               | PREFLIGHT_FAILED, reason: SIGNAL_TOO_NOISY. Retry offered.    |
| No signal detected                     | PREFLIGHT_FAILED, reason: NO_SIGNAL_DETECTED. Retry offered.  |
| 3 retries exhausted before gate        | Participant joins as SPECTATOR. No further retry.             |
| Preflight not run before gate closure  | Participant joins as SPECTATOR automatically.                 |
| Backend WebSocket disconnects          | Browser queues result. Delivers on reconnect.                 |
| Redis unavailable at gate              | HALT GATE EVALUATION. Emit preprocessing.gate.failed.         |
| PostgreSQL unavailable                 | Log to Redis fallback buffer. Flush on recovery.              |
| getUserMedia API not supported         | PREFLIGHT_FAILED, reason: BROWSER_NOT_SUPPORTED.              |
| AudioContext creation fails            | PREFLIGHT_FAILED, reason: AUDIO_CONTEXT_UNAVAILABLE.          |

```
No exceptions to the retry limit.
No human override of SPECTATOR assignment without Admin Governance Service (Service #14).
No partial admission states.
Gate decision is binary: ADMITTED or SPECTATOR.
```

---

## SECTION 16 — UI SCREEN REQUIREMENTS

### 16.1 Preflight Check Screen (Flutter)

```
Screen Name:        AudioPreflightScreen
Route:              /gd/{session_id}/preflight
Accessible:         Authenticated participants only
Timing:             Shown during preparation timer phase

UI Components:
    1. Session topic display (read-only)
    2. Preparation countdown timer
    3. Microphone check panel:
        - Animated waveform visualiser (real-time RMS bars — display only)
        - Signal level indicator (colour: green / amber / red)
        - Device label display ("Using: MacBook Pro Microphone")
        - SNR quality label ("Good / Fair / Poor")
    4. Check result card:
        - Status icon (✓ PASSED / ⚠ WARNING / ✗ FAILED)
        - Human-readable message
        - Retry button (shown if FAILED, hidden if PASSED)
    5. Tips panel (contextual, shown if WARNED or FAILED):
        - "Move to a quieter location"
        - "Use a headset for better clarity"
        - "Ensure mic is not muted in OS settings"
    6. Continue button (enabled only if PASSED or WARNED)
    7. Bandwidth status chip (green/amber)
    8. Rules reminder panel (shown alongside preflight)
```

### 16.2 Preflight Spectator Notification Screen (Flutter)

```
Screen Name:        SpectatorNoticeScreen
Route:              /gd/{session_id}/spectator

UI Components:
    1. Clear header: "You have joined as a Spectator"
    2. Reason explanation (human-readable)
    3. What spectators can do:
        - Listen to the session audio
        - View the topic and timer
    4. What spectators cannot do:
        - Speak
        - Receive speaking tokens
        - Affect scoring
    5. Contact support link
```

### 16.3 Live Quality Indicator (In-Session Flutter Widget)

```
Widget Name:    LiveAudioQualityChip
Location:       Bottom HUD bar during GD session
Visible to:     Only the participant themselves (not others)

Display:
    - Green dot: signal quality good
    - Amber dot: signal degraded (suboptimal RMS)
    - Red dot:   silence detected during token window
    - Grey dot:  muted (between turns)

No numeric values shown to participant.
Chip is informational only — no participant action required.
```

---

## SECTION 17 — OBSERVABILITY REQUIREMENTS (NON-OPTIONAL)

### 17.1 Prometheus Metrics

```
preprocessing_preflight_attempts_total{session_id, status}
preprocessing_preflight_retries_total{session_id, reason}
preprocessing_gate_admitted_total{session_id}
preprocessing_gate_spectator_total{session_id, reason}
preprocessing_constraint_failures_total{session_id, constraint}
preprocessing_bandwidth_non_compliant_total{session_id, type}
preprocessing_live_silence_detected_total{session_id}
preprocessing_backend_websocket_latency_ms
preprocessing_redis_write_latency_ms
```

### 17.2 Loki Log Events (Structured JSON)

```json
{
    "level":          "INFO | WARN | ERROR",
    "service":        "audio-preprocessing-agent",
    "session_id":     "uuid",
    "participant_id": "uuid",
    "event":          "event_name",
    "latency_ms":     integer,
    "timestamp":      "ISO8601"
}
```

### 17.3 OpenTelemetry Traces

Each preflight run produces one trace span covering:
- getUserMedia() call duration
- Measurement phase (5s signal capture)
- WebSocket result delivery
- Redis flag write
- Kafka event emit
- PostgreSQL audit write

Target: preflight WebSocket result delivery latency < 100ms P99
(excluding the 5s measurement window which is by design)

---

## SECTION 18 — SECURITY BASELINE

```
Constraint Manifest:    Served via authenticated REST endpoint.
                        Participant JWT required.
                        Manifest is read-only from participant perspective.

WebSocket Auth:         JWT required on handshake.
                        Unauthenticated connections rejected immediately.

AudioContext:           Opened in browser scope only.
                        Never transmitted to backend.
                        Closed after preflight measurement.

AnalyserNode data:      Numeric RMS/SNR scalars only are transmitted.
                        Raw float array is never transmitted to backend.
                        Raw float array is never written to any storage.

Device Label:           Transmitted to backend for audit log only.
                        Not exposed to other participants.
                        Not exposed in any public API.

Redis Keys:             Internal Kubernetes service only.
                        realtime namespace NetworkPolicy enforced.

PostgreSQL:             Row-level security per tenant_id.
                        Append-only for audit tables.
                        No delete permitted by service role.

Rate Limiting:          Max 1 preflight attempt per 30 seconds per participant.
                        Prevents measurement-loop abuse.
```

---

## SECTION 19 — DEPLOYMENT SPECIFICATION

### 19.1 Kubernetes Manifest (Reference)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: audio-preprocessing-agent
  namespace: realtime
spec:
  replicas: 2
  selector:
    matchLabels:
      app: audio-preprocessing-agent
  template:
    metadata:
      labels:
        app: audio-preprocessing-agent
    spec:
      containers:
        - name: agent
          image: ecoskiller/audio-preprocessing-agent:latest
          ports:
            - containerPort: 8081
          env:
            - name: REDIS_URL
              valueFrom:
                secretKeyRef:
                  name: redis-secret
                  key: url
            - name: KAFKA_BROKER
              value: "kafka.ops.svc.cluster.local:9092"
            - name: PG_URL
              valueFrom:
                secretKeyRef:
                  name: postgres-secret
                  key: realtime-dsn
            - name: SNR_GATE_DB
              value: "12"
            - name: SILENCE_THRESHOLD_RMS
              value: "0.001"
            - name: MIN_SIGNAL_THRESHOLD_RMS
              value: "0.01"
          readinessProbe:
            httpGet:
              path: /health
              port: 8081
            initialDelaySeconds: 5
            periodSeconds: 10
          livenessProbe:
            httpGet:
              path: /health
              port: 8081
            initialDelaySeconds: 15
            periodSeconds: 20
          resources:
            requests:
              cpu: "100m"
              memory: "256Mi"
            limits:
              cpu: "400m"
              memory: "512Mi"
```

### 19.2 Service

```yaml
apiVersion: v1
kind: Service
metadata:
  name: audio-preprocessing-agent
  namespace: realtime
spec:
  selector:
    app: audio-preprocessing-agent
  ports:
    - port: 8081
      targetPort: 8081
  type: ClusterIP
```

---

## SECTION 20 — INTERN EXECUTION INSTRUCTIONS

### Step 1 — Prerequisites
```bash
docker --version
kubectl version --client
node --version     # Must be 18+
npm --version
```

### Step 2 — Project Location
```
/backend/services/realtime/audio_preprocessing_agent/
```

### Step 3 — Start Service (Local Dev)
```bash
cd /backend/services/realtime/audio_preprocessing_agent/
cp .env.example .env     # fill REDIS_URL, KAFKA_BROKER, PG_URL
npm install
npm run dev              # starts on port 8081
```

### Step 4 — Expected Output
```
[AUDIO_PREPROCESSING_AGENT] Listening on port 8081
[AUDIO_PREPROCESSING_AGENT] Redis connected: OK
[AUDIO_PREPROCESSING_AGENT] PostgreSQL connected: OK
[AUDIO_PREPROCESSING_AGENT] Kafka producer ready
[AUDIO_PREPROCESSING_AGENT] Health endpoint: http://localhost:8081/health
```

### Step 5 — Health Check
```bash
curl http://localhost:8081/health
# Expected: {"status":"UP","redis":"OK","postgres":"OK","kafka":"OK"}
```

### Step 6 — Run Tests
```bash
npm test
# Expected: ALL TESTS PASSED
```

### Step 7 — Key ENV Variables Reference

| Variable                  | Purpose                              | Default  |
|---------------------------|--------------------------------------|----------|
| `REDIS_URL`               | Redis connection string              | required |
| `PG_URL`                  | PostgreSQL DSN for realtime schema   | required |
| `KAFKA_BROKER`            | Kafka broker address                 | required |
| `SNR_GATE_DB`             | Minimum SNR threshold (dB)           | 12       |
| `SILENCE_THRESHOLD_RMS`   | RMS value below which = silence      | 0.001    |
| `MIN_SIGNAL_THRESHOLD_RMS`| Minimum RMS for valid signal         | 0.01     |
| `MAX_PREFLIGHT_RETRIES`   | Retry attempts before spectator gate | 3        |
| `PREFLIGHT_DURATION_S`    | Measurement window duration          | 5        |
| `GATE_OFFSET_S`           | Seconds before session start to gate | 30       |

Failure at any step → **STOP EXECUTION** → report to senior developer.

---

## SECTION 21 — SEQUENCING CONTRACT WITH ADJACENT AGENTS

```
AUDIO_PREPROCESSING_AGENT executes in this order relative to the GD lifecycle:

T = session_start_time

T - 20:00  →  Preparation timer begins
               AUDIO_PREPROCESSING_AGENT delivers CONSTRAINT_MANIFEST to browsers

T - 19:30  →  BEGIN_PREFLIGHT sent to all participants
               Preflight runs (5 second measurement window)
               Results reported to backend

T - 19:00  →  Preflight result processing begins
               Redis device_ready flags written
               PostgreSQL audit written
               Kafka events emitted

T - 05:00  →  Retry window opens for FAILED participants

T - 02:00  →  Retry window CLOSES. No more retries permitted.

T - 00:30  →  ADMISSION GATE EVALUATES
               All participant flags checked
               Spectator assignments finalised
               preprocessing:gate_complete:{session_id} = "YES" written to Redis
               Kafka: preprocessing.gate.complete emitted

T - 00:00  →  AUDIO_STREAM_ROUTING_AGENT reads gate_complete flag
               AUDIO_STREAM_ROUTING_AGENT begins token grant sequence
               GD session begins
```

---

## SECTION 22 — FINAL EXECUTION RULES (INVIOLABLE)

```
1.  Preflight is mandatory for all participants before SFU admission.   NON-NEGOTIABLE.
2.  Audio is measured numerically. It is never recorded.               NON-NEGOTIABLE.
3.  Normalisation is applied via browser constraints. No AI required.  NON-NEGOTIABLE.
4.  Gate decision is binary: ADMITTED or SPECTATOR. No partial states. NON-NEGOTIABLE.
5.  After 3 retries, participant is SPECTATOR. No exceptions.          NON-NEGOTIABLE.
6.  Bandwidth non-compliance is advisory only. It never blocks entry.  NON-NEGOTIABLE.
7.  Raw float audio arrays are never transmitted to any backend.       NON-NEGOTIABLE.
8.  AnalyserNode is closed and discarded after measurement.            NON-NEGOTIABLE.
9.  Device label is audit-only. Never shared between participants.     NON-NEGOTIABLE.
10. AUDIO_STREAM_ROUTING_AGENT cannot begin until gate_complete = YES. NON-NEGOTIABLE.
11. Constraint manifest is served by backend. Frontend never hardcodes it. NON-NEGOTIABLE.
12. All actions are logged, timestamped, and immutably auditable.      NON-NEGOTIABLE.
13. Scoring Engine receives no audio quality data from this agent.     NON-NEGOTIABLE.
14. No AI inference is applied at any stage of preprocessing.          NON-NEGOTIABLE.
15. Equal normalised profile for every participant = equal playing field. NON-NEGOTIABLE.
```

---

## SECTION 23 — BINDING DECLARATIONS

```
Bound to ECOSKILLER Master Prompt:       v12.0 UNIFIED
Bound to Service Contract:               Voice GD Orchestrator (Service #7)
Bound to Service Contract:               Dojo Match Engine (Service #8)
Bound to Infrastructure Law:             R1 Technology Stack Lock
Bound to Security Baseline:              Section IX of Infrastructure Audit
Bound to Observability Law:              Section VIII (NON-OPTIONAL)
Bound to Scoring Engine Interface:       Service #9 — receives NO audio quality data
Bound to Event Schema Registry:          preprocessing.* event namespace
Bound to Kubernetes Namespace:           realtime
Bound to Jitsi Media Stack:              Self-hosted, Section IV (LOCKED)
Bound to Downstream Agent:               AUDIO_STREAM_ROUTING_AGENT v1.0.0
Sequencing Contract:                     gate_complete Redis flag before token grants
```

---

## SECTION 24 — AGENT SEAL

```
╔══════════════════════════════════════════════════════════════════╗
║         AUDIO_PREPROCESSING_AGENT — PROMPT SEALED               ║
║                                                                  ║
║  Version:       v1.0.0                                           ║
║  System:        ECOSKILLER Antigravity Audio Processing          ║
║  Status:        FINAL · LOCKED · DETERMINISTIC                   ║
║  Mutation:      Add-only via version bump ONLY                   ║
║  Authority:     Human declaration only                           ║
║                                                                  ║
║  The browser is the capture layer.                               ║
║  The constraint manifest is the normalisation law.               ║
║  The preflight is the entry gate.                                ║
║  The AnalyserNode measures and discards — never stores.          ║
║  The backend is the audit authority.                             ║
║  Redis is the gate memory.                                       ║
║  Kafka is the event record.                                      ║
║                                                                  ║
║  Every participant enters the room on equal acoustic ground.     ║
║  Hardware advantage is structurally neutralised.                 ║
║  Environment advantage is structurally minimised.                ║
║  Audio bias has no entry point.                                  ║
║                                                                  ║
║  Preprocessing is invisible.                                     ║
║  Fairness is structural.                                         ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*End of AUDIO_PREPROCESSING_AGENT Sealed Prompt — ECOSKILLER v12.0*
