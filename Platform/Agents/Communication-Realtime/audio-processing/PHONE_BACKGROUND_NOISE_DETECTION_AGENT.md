# 🔒 PHONE_BACKGROUND_NOISE_DETECTION_AGENT
## Audio Processing · Antigravity Layer · Ecoskiller Platform

```
Artifact Class  : Production Agent Specification
Layer           : Audio Processing → Antigravity Sublayer
Status          : FINAL · LOCKED · SEALED · GOVERNED
Mutation Policy : Add-only via version bump
Version         : v1.0.0
Interpretation Authority : NONE
Execution Authority      : Human declaration only
Parent System   : ECOSKILLER — MASTER EXECUTION PROMPT v12.0
Sibling Agent   : PHONE_SILENCE_SPAM_AGENT v1.0.0
Governed By     : Voice GD Orchestrator · Deterministic Protocol Law
Domain Scope    : Voice GD · Dojo Matches · Interview Rooms · Dojo Arts Sessions
```

---

## ⚠️ SEAL DECLARATION

```
This document is sealed.
No clause may be removed.
No logic may be softened.
No behavior is negotiable.
No AI judgment is permitted inside agent boundaries.
No human discretion is permitted during live session execution.

Violations → STOP EXECUTION → REPORT AGENT-INTEGRITY-BREACH
```

---

## I. AGENT IDENTITY

| Property | Value |
|---|---|
| Agent Name | `PHONE_BACKGROUND_NOISE_DETECTION_AGENT` |
| Short Handle | `PBNDA` |
| Agent Class | Passive Ambient Environment Classifier |
| Domain | Antigravity · Audio Processing |
| Scope | All real-time voice sessions on Ecoskiller platform |
| Execution Mode | Continuous Passive Monitoring (session-lifecycle-bound) |
| Decision Authority | NONE — Classification and event emission only |
| Enforcement Authority | Voice GD Orchestrator (exclusive) |
| AI Inference | FORBIDDEN |
| Human Moderation During Session | FORBIDDEN |
| Raw Audio Retention | PERMANENTLY PROHIBITED |

---

## II. PURPOSE (NON-NEGOTIABLE)

The `PHONE_BACKGROUND_NOISE_DETECTION_AGENT` is a **real-time ambient environment
classifier** operating within the Antigravity sublayer of Ecoskiller's Audio Processing
pipeline.

### Problem It Solves

In a purely rule-driven, recruiter-less, AI-less assessment system (Voice GD, Dojo, Interview),
background noise from a participant's physical environment creates three distinct integrity
threats that the `PHONE_SILENCE_SPAM_AGENT` does not cover:

```
THREAT 1 — ENVIRONMENT UNFAIRNESS
  A participant in a noisy environment is measurably disadvantaged
  in turn compliance metrics. Their silence windows are polluted
  by ambient energy. Their score becomes environmentally biased,
  not behaviourally biased.

THREAT 2 — SIGNAL CONTAMINATION
  Background noise bleeds into other participants' audio streams
  via the SFU, degrading the entire session's signal integrity.

THREAT 3 — UNVERIFIED ENVIRONMENT
  A participant may be in a coached or assisted environment
  (multiple voices audible, keyboard dictation, another person
  whispering). The system cannot allow undetected coached sessions
  to corrupt assessment integrity.
```

### What This Agent Does

```
CLASS 1 — AMBIENT_NOISE_SUSTAINED
  Continuous non-speech background energy above threshold.
  Example: fan noise, AC hum, traffic, construction, crowd.

CLASS 2 — TRANSIENT_NOISE_BURST
  Short-duration high-energy intrusion during speaking window.
  Example: door slam, barking dog, vehicle horn, dropped object.

CLASS 3 — MULTI_VOICE_ENVIRONMENT
  More than one distinct voice pattern present in the channel
  during a participant's active speaking token.
  Example: family member speaking, coaching whisper, group room.

CLASS 4 — ECHO_CHAMBER_ENVIRONMENT
  Strong acoustic echo / reverb signature indicating
  no physical sound treatment (empty concrete room, stairwell).
  Creates inaccurate speaking time measurements.

CLASS 5 — DEVICE_PROXIMITY_VIOLATION
  Microphone distance signature indicating device is not
  in front of participant (phone face-down, phone on table far away).
  Results in unreliable audio energy readings.
```

The agent does **not** interpret what is being said.
The agent does **not** evaluate the participant's communication ability.
The agent does **not** penalize participants.
The agent does **not** score sessions.
The agent **only** classifies ambient signal patterns and emits typed events.

---

## III. SYSTEM POSITION (ARCHITECTURE LOCK)

```
┌──────────────────────────────────────────────────────────────────────────┐
│                       ECOSKILLER AUDIO PIPELINE                          │
├──────────────────────────────────────────────────────────────────────────┤
│  WebRTC Transport Layer                                                  │
│    └─ Microphone capture                                                 │
│    └─ SRTP encryption                                                    │
│    └─ Echo cancellation (hardware layer — NOT this agent's scope)        │
│         ↓                                                                │
│  Jitsi SFU (Jitsi Videobridge)                                           │
│    └─ Audio stream routing only                                          │
│    └─ No signal analysis                                                 │
│         ↓                                                                │
│  ┌───────────────────────────────────────────────────────────────┐      │
│  │                   ANTIGRAVITY SUBLAYER                        │      │
│  │                                                               │      │
│  │   ┌────────────────────────────────────────────────────┐     │      │
│  │   │   PHONE_SILENCE_SPAM_AGENT (sibling)               │     │      │
│  │   │   Detects: phone events, silence abuse, spam audio │     │      │
│  │   └────────────────────────────────────────────────────┘     │      │
│  │                                                               │      │
│  │   ┌────────────────────────────────────────────────────┐     │      │
│  │   │   PHONE_BACKGROUND_NOISE_DETECTION_AGENT  ◄───────┤     │      │
│  │   │   Detects: ambient noise, multi-voice,             │     │      │
│  │   │            transients, echo, proximity             │     │      │
│  │   └──────────────┬─────────────────────────────────────┘     │      │
│  │                  │ typed events                               │      │
│  │                  ▼                                            │      │
│  │   ┌────────────────────────────────────────────────────┐     │      │
│  │   │   Antigravity Event Aggregator                     │     │      │
│  │   │   (deduplication + session-level event merge)      │     │      │
│  │   └──────────────┬─────────────────────────────────────┘     │      │
│  └──────────────────┼────────────────────────────────────────── ┘      │
│                     ▼                                                    │
│  Event Bus — Kafka topic: audio.agent.events                            │
│         ↓                                                                │
│  Voice GD Orchestrator (enforcement decisions via Redis state machine)  │
│  WebSocket Command Channel (mute / session flags / warnings)            │
│  PostgreSQL Audit Log (immutable record)                                 │
│  ClickHouse Analytics (aggregated noise quality metrics)                │
└──────────────────────────────────────────────────────────────────────────┘
```

**Architecture Rules (locked):**
- PBNDA runs in parallel with PHONE_SILENCE_SPAM_AGENT — never sequential.
- PBNDA does not share state with its sibling agent.
- PBNDA output goes through the Antigravity Event Aggregator before the Event Bus.
- PBNDA never accesses Jitsi routing internals.
- PBNDA never receives audio from other participants — single-stream scope only.
- Enforcement is exclusively the Orchestrator's domain.

---

## IV. DETECTION SPECIFICATIONS (LOCKED)

---

### 4.1 — CLASS 1: AMBIENT_NOISE_SUSTAINED

**Definition:** Continuous non-speech low-to-mid energy present throughout the participant's
session window, indicating a noisy physical environment.

| Property | Specification |
|---|---|
| Signal Target | Broadband background energy: 20 Hz – 8000 Hz |
| Detection Basis | SNR (Signal-to-Noise Ratio) measurement — speech signal vs ambient floor |
| SNR Threshold | SNR < 15 dB sustained for ≥ 8 seconds → event fired |
| Measurement Method | `librosa.feature.spectral_flatness` + noise floor estimation via minimum statistics |
| Detection Window | 8-second rolling window |
| Grace Period | First 5 seconds of session join excluded (environment settling) |
| Re-fire Suppression | 30 seconds between repeated events for same participant |
| Output Event | `audio.background_noise.ambient_sustained` |

**Payload:**
```json
{
  "event": "audio.background_noise.ambient_sustained",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "AMBIENT_NOISE_SUSTAINED",
  "snr_db": 11.4,
  "noise_floor_dbfs": -38.2,
  "speech_energy_dbfs": -26.8,
  "detection_window_seconds": 8,
  "severity": "LOW | MEDIUM | HIGH",
  "action_required": "orchestrator_decision"
}
```

**Severity Scale:**
| SNR Range | Severity |
|---|---|
| 10 dB – 15 dB | LOW |
| 5 dB – 9 dB | MEDIUM |
| < 5 dB | HIGH |

---

### 4.2 — CLASS 2: TRANSIENT_NOISE_BURST

**Definition:** Short-duration, high-amplitude impulsive noise event during an active
session window, sufficient to contaminate the measurement frame.

| Property | Specification |
|---|---|
| Signal Target | Impulsive energy spike: amplitude > 20 dB above rolling RMS baseline |
| Duration Target | Event duration: 50ms – 2000ms |
| Detection Basis | Peak-to-RMS ratio analysis on 50ms frames |
| Threshold | Peak energy ≥ rolling RMS + 20 dB in any 50ms frame |
| Detection Method | `librosa.onset.onset_strength` — onset envelope spike detection |
| Re-fire Suppression | 5 seconds (transients cluster — only first event in cluster fired) |
| Output Event | `audio.background_noise.transient_burst` |

**Payload:**
```json
{
  "event": "audio.background_noise.transient_burst",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "TRANSIENT_NOISE_BURST",
  "peak_dbfs": -14.2,
  "rms_baseline_dbfs": -48.6,
  "peak_to_rms_db": 34.4,
  "burst_duration_ms": 320,
  "active_speaking_token": true,
  "action_required": "orchestrator_decision"
}
```

**Note:** `active_speaking_token` flag is passed to orchestrator.
Transients during active tokens have higher enforcement priority than idle-phase transients.

---

### 4.3 — CLASS 3: MULTI_VOICE_ENVIRONMENT

**Definition:** Detection of more than one simultaneous or near-simultaneous voice
signature in the participant's channel during their active speaking token.

This is the highest-integrity-risk detection class. It indicates a coached or
assisted environment which structurally compromises assessment validity.

| Property | Specification |
|---|---|
| Signal Target | Overlapping fundamental frequency (F0) tracks — multiple pitch sources |
| Detection Basis | Polyphonic pitch estimation — multiple concurrent F0 trajectories |
| Detection Method | `librosa.yin` pitch estimation on overlapping 200ms frames — dual-F0 divergence check |
| Threshold | Two distinct F0 values simultaneously present > 1.5 seconds, frequency distance ≥ 30 Hz |
| Confidence Gate | Must sustain for ≥ 3 consecutive frames to eliminate reflection artifacts |
| Output Event | `audio.background_noise.multi_voice_environment` |

**Payload:**
```json
{
  "event": "audio.background_noise.multi_voice_environment",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "MULTI_VOICE_ENVIRONMENT",
  "primary_f0_hz": 142.3,
  "secondary_f0_hz": 198.7,
  "f0_distance_hz": 56.4,
  "sustained_frames": 5,
  "sustained_duration_ms": 1000,
  "active_speaking_token": true,
  "severity": "CRITICAL",
  "action_required": "orchestrator_decision"
}
```

**This event class always carries severity CRITICAL.**
Orchestrator enforcement for CRITICAL events is mandatory — not advisory.

---

### 4.4 — CLASS 4: ECHO_CHAMBER_ENVIRONMENT

**Definition:** Acoustic signature indicating the participant is in an environment
with excessive reverberation time (RT60), making speaking energy measurements
unreliable and potentially giving unfair scoring outcomes.

| Property | Specification |
|---|---|
| Signal Target | Post-transient decay tail — energy decay rate after speech onset stop |
| Detection Basis | RT60 estimation from energy decay after voice cessation |
| Detection Method | Schroeder backward integration on 500ms post-voice frames |
| Threshold | RT60 estimate > 800ms → event fired |
| Detection Window | Measured once per speaking token — not continuous |
| Re-fire Suppression | Once per session per participant (environment is stable) |
| Output Event | `audio.background_noise.echo_chamber_environment` |

**Payload:**
```json
{
  "event": "audio.background_noise.echo_chamber_environment",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "ECHO_CHAMBER_ENVIRONMENT",
  "rt60_estimate_ms": 1140,
  "threshold_ms": 800,
  "detection_basis": "schroeder_backward_integration",
  "scoring_reliability_flag": "DEGRADED",
  "action_required": "orchestrator_decision"
}
```

**`scoring_reliability_flag: DEGRADED`** is forwarded by the Orchestrator to the
Scoring Engine. The Scoring Engine applies environment-adjusted normalization for
silence window metrics for this participant's session.

---

### 4.5 — CLASS 5: DEVICE_PROXIMITY_VIOLATION

**Definition:** Microphone proximity signature indicates the participant's device
microphone is not in operational speaking position — phone placed face-down, far from
mouth, or device audio routed through an indirect surface.

| Property | Specification |
|---|---|
| Signal Target | Voice energy envelope vs expected near-field proximity profile |
| Detection Basis | High-frequency roll-off analysis — near-field voice has strong 1–4 kHz presence; far-field degrades significantly |
| Detection Method | Spectral centroid tracking + high-frequency energy ratio (1 kHz – 8 kHz vs 80 Hz – 1 kHz) |
| Threshold | HF energy ratio < 0.15 sustained for ≥ 10 seconds during active speaking token |
| Detection Window | 10-second sustained window (eliminates transient mic adjustment) |
| Re-fire Suppression | 60 seconds (allows participant to reposition device) |
| Output Event | `audio.background_noise.device_proximity_violation` |

**Payload:**
```json
{
  "event": "audio.background_noise.device_proximity_violation",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "DEVICE_PROXIMITY_VIOLATION",
  "hf_energy_ratio": 0.09,
  "spectral_centroid_hz": 612.4,
  "expected_centroid_range_hz": "1200–3500",
  "sustained_seconds": 14,
  "action_required": "orchestrator_decision"
}
```

---

## V. AGENT EXECUTION STATES

```
STANDBY
  └─ Process alive
  └─ No assigned session
  └─ Awaiting: session.started event from Event Bus

PRE_SESSION_CALIBRATION
  └─ Session assigned
  └─ 5-second noise floor baseline established before first speaking token
  └─ Calibration result stored in Redis: agent:pbnda:{session_id}:{participant_id}:baseline
  └─ Calibration feeds: AMBIENT_NOISE_SUSTAINED threshold personalization

ACTIVE_MONITORING
  └─ All 5 classifiers running in parallel per participant stream
  └─ Event bus write channel open
  └─ Redis state updated on each detection event

TOKEN_AWARE_MODE
  └─ Orchestrator signals active speaking token start/end via Redis key
  └─ PBNDA elevates MULTI_VOICE and TRANSIENT severity during active token
  └─ DEVICE_PROXIMITY_VIOLATION only fires during active token windows

SUPPRESSED
  └─ Orchestrator-issued suppression for specific participant
  └─ Maximum: 60 seconds, non-renewable per session
  └─ Use case: participant acknowledged environment issue, reposition in progress

SESSION_COMPLETE
  └─ Session end event received
  └─ Final environment quality summary emitted to ClickHouse
  └─ Redis keys expired (TTL: 3600 seconds post-session)

FAULT
  └─ Classifier process crash or audio feed interruption
  └─ Emits: audio.agent_fault.pbnda
  └─ Session continues — scoring reliability flag set to ENVIRONMENT_UNVERIFIED
```

---

## VI. BASELINE CALIBRATION PROTOCOL (LOCKED)

Before the first speaking token is issued to any participant, PBNDA runs a
5-second silent calibration window to establish the participant's environmental
noise floor baseline.

```
CALIBRATION SEQUENCE:
1. Session start event received from Orchestrator
2. Participant mic confirmed open (join confirmation from Jitsi)
3. 5-second window: participant not yet speaking (preparation timer active)
4. PBNDA samples: RMS floor, spectral flatness, SNR estimate
5. Calibration stored in Redis as participant-session baseline
6. AMBIENT_NOISE_SUSTAINED threshold adjusted per participant baseline
7. CALIBRATION_COMPLETE emitted to Orchestrator

If calibration window is disrupted (participant joins late):
→ PBNDA uses platform-default baseline values
→ calibration_source: "default" flagged in all payloads
```

**Calibration Redis Key:**
```
agent:pbnda:{session_id}:{participant_id}:baseline
TTL: session_duration + 3600 seconds
Value: {
  "rms_floor_dbfs": -52.3,
  "snr_estimate_db": 18.7,
  "spectral_flatness": 0.14,
  "calibration_source": "measured | default",
  "calibrated_at": "<iso8601>"
}
```

---

## VII. TOKEN AWARENESS PROTOCOL (LOCKED)

PBNDA operates differently depending on whether a participant is holding an
active speaking token. The Orchestrator publishes token state to Redis.

**Redis Token State Key:**
```
orchestrator:token:{session_id}:{participant_id}:active
Value: "true" | "false"
TTL: token_duration_seconds + 30 seconds
```

**PBNDA Token-Aware Behavior:**

| Classifier | Idle Phase | Active Token Phase |
|---|---|---|
| AMBIENT_NOISE_SUSTAINED | Monitor + log | Monitor + fire event |
| TRANSIENT_NOISE_BURST | Monitor + log | Monitor + fire event (elevated severity) |
| MULTI_VOICE_ENVIRONMENT | Monitor only | Fire event immediately (CRITICAL) |
| ECHO_CHAMBER_ENVIRONMENT | Measure once on first token | Log result only |
| DEVICE_PROXIMITY_VIOLATION | Suspended | Active — fire on threshold breach |

---

## VIII. ORCHESTRATOR CONTRACT (ENFORCEMENT INTERFACE)

PBNDA emits events to the Kafka Event Bus only.
The Voice GD Orchestrator consumes and enforces per the following table.
No event class maps to automatic disqualification — Orchestrator applies graduated response.

| Event Class | Severity | Orchestrator Response |
|---|---|---|
| `AMBIENT_NOISE_SUSTAINED` | LOW | Log to audit. No participant-facing action. |
| `AMBIENT_NOISE_SUSTAINED` | MEDIUM | WebSocket advisory toast to participant: "Improve your environment." Logged. |
| `AMBIENT_NOISE_SUSTAINED` | HIGH | WebSocket warning. Scoring Engine notified: environment flag active. |
| `TRANSIENT_NOISE_BURST` | Any (idle phase) | Log to audit only. |
| `TRANSIENT_NOISE_BURST` | Any (active token) | Log + scoring adjustment for affected frame window. |
| `MULTI_VOICE_ENVIRONMENT` | CRITICAL | Immediate forced mute. Token revoked. Admin review flag set. Session continues for other participants. |
| `ECHO_CHAMBER_ENVIRONMENT` | — | Scoring Engine: apply RT60-adjusted silence normalization for participant. Log. No participant penalty. |
| `DEVICE_PROXIMITY_VIOLATION` | — | WebSocket toast: "Move device closer to your mouth." Speaking token extended by 10 seconds (one-time per session). |

**All enforcement is deterministic. No discretionary judgment permitted.**
**Orchestrator may not deviate from this table without a version-bumped document update.**

---

## IX. SCORING ENGINE INTERFACE (CONTRACT)

PBNDA emits environment quality flags that the Scoring Engine must consume
to apply fair scoring normalization. These flags do not penalize participants —
they protect assessment integrity.

```
Scoring Engine Consumed Events:

1. AMBIENT_NOISE_SUSTAINED (HIGH)
   → scoring_env_flag: NOISE_DEGRADED
   → Effect: Silence window RMS thresholds relaxed by +6 dB for this participant

2. ECHO_CHAMBER_ENVIRONMENT
   → scoring_env_flag: ECHO_DEGRADED
   → scoring_reliability: DEGRADED
   → Effect: Post-speech silence decay not counted against silence score

3. DEVICE_PROXIMITY_VIOLATION
   → scoring_env_flag: PROXIMITY_DEGRADED
   → Effect: Energy-based metrics normalized to device distance coefficient

4. MULTI_VOICE_ENVIRONMENT (CRITICAL)
   → scoring_env_flag: ENVIRONMENT_COMPROMISED
   → scoring_reliability: VOID_PENDING_REVIEW
   → Effect: Session score held pending Admin Governance Service review
```

Scoring Engine must maintain a `participant_environment_context` record per session.
This record is part of the immutable audit trail.

---

## X. TECHNOLOGY BINDING (LOCKED)

| Component | Technology |
|---|---|
| Audio Signal Access | WebRTC `AudioWorklet` API (browser) — pre-processed signal metadata only |
| Server-side Pipeline | Python 3.11 + `librosa 0.10` + `pyaudio` + `numpy` + `scipy` |
| Noise Floor Estimation | Minimum statistics algorithm — `librosa.feature.spectral_flatness` |
| SNR Measurement | RMS-based SNR — `librosa.feature.rms` (512-frame, 44100 Hz) |
| Onset Detection | `librosa.onset.onset_strength` — 50ms frame granularity |
| Pitch Estimation | `librosa.yin` — 200ms frames, 80–500 Hz F0 search range |
| RT60 Estimation | Schroeder backward integration — custom `scipy.signal` implementation |
| Spectral Centroid | `librosa.feature.spectral_centroid` — 2048-point FFT |
| Calibration State | Redis 7 — key namespace `agent:pbnda:*` |
| Token State Read | Redis 7 — key namespace `orchestrator:token:*` (read-only) |
| Event Bus Write | Apache Kafka — topic: `audio.agent.events` (shared with PSSG) |
| Audit Log | PostgreSQL 15 — table: `audio_background_noise_events` |
| Analytics Sink | ClickHouse — table: `session_environment_quality` |
| Deployment | Kubernetes pod — namespace: `realtime` |
| Service Name | `phone-background-noise-detection-agent` |
| Port | Internal only — no external exposure |
| Image Tag Lock | `ecoskiller/pbnda:v1.0.0` |

---

## XI. WHAT THE AGENT DOES NOT DO (HARD PROHIBITION)

```
✗ Does NOT record audio — ever, under any condition
✗ Does NOT store audio samples beyond the active processing frame
✗ Does NOT transcribe speech
✗ Does NOT identify individuals from voice characteristics
✗ Does NOT evaluate communication style or quality
✗ Does NOT issue mute commands — Orchestrator only
✗ Does NOT apply scoring penalties — Scoring Engine only
✗ Does NOT use machine learning inference or neural models
✗ Does NOT access audio streams of other participants
✗ Does NOT communicate directly with frontend
✗ Does NOT access participant PII — UUID only
✗ Does NOT operate outside session lifecycle bounds
✗ Does NOT share internal state with PHONE_SILENCE_SPAM_AGENT
```

Violation of any prohibition above → `STOP EXECUTION` → `REPORT AGENT-INTEGRITY-BREACH`

---

## XII. KAFKA EVENT SCHEMA (LOCKED)

**Topic:** `audio.agent.events`
**Partition Key:** `session_id`
**Retention:** 7 days
**Producer:** `pbnda-v1`
**Schema Version:** `1.0`

```json
{
  "schema_version": "1.0",
  "producer": "pbnda-v1",
  "event": "<string>",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "AMBIENT_NOISE_SUSTAINED | TRANSIENT_NOISE_BURST | MULTI_VOICE_ENVIRONMENT | ECHO_CHAMBER_ENVIRONMENT | DEVICE_PROXIMITY_VIOLATION | AGENT_FAULT",
  "severity": "LOW | MEDIUM | HIGH | CRITICAL | null",
  "active_speaking_token": true,
  "calibration_source": "measured | default",
  "scoring_env_flag": "<string | null>",
  "metadata": "<object — class-specific fields>",
  "action_required": "orchestrator_decision"
}
```

Schema changes require version bump. Breaking changes → `STOP EXECUTION`.

---

## XIII. POSTGRESQL AUDIT TABLE (IMMUTABLE)

```sql
CREATE TABLE audio_background_noise_events (
  id                    BIGSERIAL    PRIMARY KEY,
  session_id            UUID         NOT NULL,
  participant_id        UUID         NOT NULL,
  event_type            VARCHAR(80)  NOT NULL,
  signal_class          VARCHAR(60)  NOT NULL,
  severity              VARCHAR(20),
  active_speaking_token BOOLEAN      DEFAULT FALSE,
  calibration_source    VARCHAR(20),
  scoring_env_flag      VARCHAR(60),
  snr_db                NUMERIC(6,2),
  rt60_estimate_ms      INTEGER,
  f0_primary_hz         NUMERIC(8,2),
  f0_secondary_hz       NUMERIC(8,2),
  hf_energy_ratio       NUMERIC(6,4),
  metadata              JSONB,
  detected_at           TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  orchestrator_ack      BOOLEAN      DEFAULT FALSE,
  scoring_engine_ack    BOOLEAN      DEFAULT FALSE
);

-- Immutability enforcement
CREATE RULE no_update_bg_noise_events AS
  ON UPDATE TO audio_background_noise_events DO INSTEAD NOTHING;

CREATE RULE no_delete_bg_noise_events AS
  ON DELETE TO audio_background_noise_events DO INSTEAD NOTHING;

-- Indexes for audit query performance
CREATE INDEX idx_bg_noise_session    ON audio_background_noise_events (session_id);
CREATE INDEX idx_bg_noise_signal     ON audio_background_noise_events (signal_class);
CREATE INDEX idx_bg_noise_detected   ON audio_background_noise_events (detected_at);
```

---

## XIV. CLICKHOUSE ANALYTICS TABLE

Session-level environment quality rolled up for analytics dashboards and
scoring fairness reports.

```sql
CREATE TABLE session_environment_quality (
  session_id              UUID,
  participant_id          UUID,
  session_date            Date,
  ambient_noise_events    UInt16,
  transient_burst_events  UInt16,
  multi_voice_events      UInt16,
  echo_chamber_detected   UInt8,
  proximity_violations    UInt16,
  avg_snr_db              Float32,
  max_severity_reached    String,
  scoring_env_flag        String,
  calibration_source      String,
  environment_quality     Enum8('CLEAN'=1, 'ACCEPTABLE'=2, 'DEGRADED'=3, 'COMPROMISED'=4)
) ENGINE = MergeTree()
  ORDER BY (session_date, session_id, participant_id);
```

**Environment Quality Derivation:**
| Condition | Quality Label |
|---|---|
| No events fired | `CLEAN` |
| LOW ambient only | `ACCEPTABLE` |
| MEDIUM ambient or any transients | `DEGRADED` |
| HIGH ambient, echo, proximity, or MULTI_VOICE | `COMPROMISED` |

---

## XV. KUBERNETES DEPLOYMENT SPEC (LOCKED)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: phone-background-noise-detection-agent
  namespace: realtime
  labels:
    app: phone-background-noise-detection-agent
    layer: antigravity
    domain: audio-processing
    agent-class: environment-classifier
spec:
  replicas: 2
  selector:
    matchLabels:
      app: phone-background-noise-detection-agent
  template:
    metadata:
      labels:
        app: phone-background-noise-detection-agent
    spec:
      containers:
        - name: pbnda
          image: ecoskiller/pbnda:v1.0.0
          env:
            - name: KAFKA_BROKER
              valueFrom:
                secretKeyRef:
                  name: kafka-secrets
                  key: broker_url
            - name: REDIS_URL
              valueFrom:
                secretKeyRef:
                  name: redis-secrets
                  key: url
            - name: POSTGRES_URL
              valueFrom:
                secretKeyRef:
                  name: postgres-secrets
                  key: url
            - name: CLICKHOUSE_URL
              valueFrom:
                secretKeyRef:
                  name: clickhouse-secrets
                  key: url
          resources:
            requests:
              cpu: "300m"
              memory: "384Mi"
            limits:
              cpu: "700m"
              memory: "768Mi"
          livenessProbe:
            httpGet:
              path: /healthz
              port: 8081
            initialDelaySeconds: 10
            periodSeconds: 15
          readinessProbe:
            httpGet:
              path: /readyz
              port: 8081
            initialDelaySeconds: 5
            periodSeconds: 10
```

**Note:** PBNDA is allocated higher CPU than PSSG due to polyphonic pitch estimation
and RT60 calculation workload. Resource limits are non-negotiable.

---

## XVI. OBSERVABILITY (NON-OPTIONAL)

All metrics exposed on `:8081/metrics` (Prometheus scrape target).
Port 8081 is reserved for PBNDA to avoid collision with PSSG on port 8080.

| Metric | Type | Description |
|---|---|---|
| `pbnda_ambient_noise_total` | Counter | Total ambient noise events |
| `pbnda_transient_burst_total` | Counter | Total transient burst detections |
| `pbnda_multi_voice_total` | Counter | Total multi-voice environment detections |
| `pbnda_echo_chamber_total` | Counter | Total echo chamber detections |
| `pbnda_proximity_violation_total` | Counter | Total device proximity violations |
| `pbnda_agent_fault_total` | Counter | Classifier fault events |
| `pbnda_calibration_default_ratio` | Gauge | Ratio of sessions using default calibration |
| `pbnda_event_publish_latency_ms` | Histogram | Kafka publish latency |
| `pbnda_active_sessions` | Gauge | Active monitored sessions |
| `pbnda_critical_events_total` | Counter | Total CRITICAL severity events (multi-voice) |

**Grafana Dashboard:** `ecoskiller-antigravity-audio-environment`
**Alert Rules:**
- `pbnda_multi_voice_total` rate > 5/min → PagerDuty alert
- `pbnda_agent_fault_total` > 0 → Immediate ops notification
- `pbnda_calibration_default_ratio` > 0.20 → Review join-flow timing

---

## XVII. SECURITY CONSTRAINTS

```
✔ Agent receives signal metadata only — RMS, spectral vectors, F0 estimates
✔ Raw audio bytes never written to disk or retained beyond active processing frame
✔ Participant UUID only — no name, email, or PII in agent scope
✔ All inter-service communication via internal Kubernetes cluster network only
✔ No external API calls from agent process
✔ No external port exposure
✔ Kafka topic ACL: PBNDA has WRITE-only permission on audio.agent.events
✔ PostgreSQL: PBNDA has INSERT-only on audio_background_noise_events
✔ Redis: PBNDA has SET/GET/EXPIRE on agent:pbnda:* namespace only
✔ Redis: PBNDA has GET-only (read-only) on orchestrator:token:* namespace
✔ ClickHouse: PBNDA has INSERT-only on session_environment_quality
✔ PBNDA pod runs as non-root — UID 1000
✔ No cross-participant stream access permitted at any level
```

---

## XVIII. FAILURE MODES & FALLBACK

| Failure | System Response |
|---|---|
| PBNDA pod crash | Kubernetes restarts pod. Session continues. Scoring Engine receives `ENVIRONMENT_UNVERIFIED` flag for all affected participants. |
| Calibration window missed (late join) | Default baseline values used. `calibration_source: "default"` flagged in all payloads. No session halt. |
| Kafka publish failure | Retry 3× with 100ms backoff. On failure: write directly to PostgreSQL. ClickHouse write deferred to next retry cycle. |
| Redis unreachable | Calibration data lost. Agent continues classification using default thresholds. State persistence suspended. |
| F0 estimation timeout (>200ms) | MULTI_VOICE detection frame skipped. Logged as `detection_timeout`. No false positive fired. |
| RT60 estimation insufficient data | ECHO_CHAMBER detection deferred to next token. Max 3 deferrals before `echo_detection_unavailable` logged. |

**No failure mode may halt an active GD, Dojo, or Interview session.**
Graceful degradation is mandatory.

---

## XIX. ANTI-GRAVITY LAYER AGENT REGISTRY

PBNDA is registered as the second agent in the Antigravity Audio Processing sublayer.
All agents in this layer share the same Event Bus topic and audit governance.

| Agent | Handle | Version | Scope |
|---|---|---|---|
| `PHONE_SILENCE_SPAM_AGENT` | PSSG | v1.0.0 | Phone events, silence abuse, spam audio |
| `PHONE_BACKGROUND_NOISE_DETECTION_AGENT` | PBNDA | v1.0.0 | Ambient noise, transients, multi-voice, echo, proximity |

**Future agents added to this layer must:**
- Register in this table via version bump
- Use the same Kafka topic with distinct `producer` field
- Use distinct Redis namespace prefixes
- Use distinct PostgreSQL audit tables
- Use distinct Prometheus port numbers

---

## XX. VERSION CONTROL LAW

```
Current Version : v1.0.0
Version Format  : MAJOR.MINOR.PATCH

MAJOR → Breaking change to event schema, detection contract, or Orchestrator enforcement table
MINOR → New detection sub-class added (add-only) or new Orchestrator response mapping
PATCH → Threshold tuning, bug fix, calibration adjustment

All version bumps require:
  ✔ Updated schema_version in Kafka payload
  ✔ Migration script if PostgreSQL schema changes
  ✔ Updated Helm chart version
  ✔ ClickHouse schema migration if analytics table changes
  ✔ Changelog entry with detection rationale

No silent mutations.
No undocumented threshold changes.
No deployments without Helm chart version increment.
```

---

## XXI. DEPLOYMENT GATE CHECKLIST (MANDATORY)

```
[ ] Kafka topic audio.agent.events confirmed — retention verified
[ ] PostgreSQL audio_background_noise_events table created with immutability rules
[ ] ClickHouse session_environment_quality table created
[ ] Redis namespace agent:pbnda:* confirmed writeable
[ ] Redis namespace orchestrator:token:* confirmed read-only for PBNDA
[ ] Kubernetes deployment applied to realtime namespace
[ ] Resource limits confirmed: CPU 700m / Memory 768Mi
[ ] Prometheus scrape config updated for PBNDA pod (port 8081)
[ ] Grafana dashboard ecoskiller-antigravity-audio-environment imported
[ ] Alert rules configured: multi_voice rate, fault total, calibration default ratio
[ ] Voice GD Orchestrator updated to consume PBNDA event classes
[ ] Scoring Engine updated to consume scoring_env_flag from PBNDA events
[ ] Agent Registry in Section XIX updated
[ ] End-to-end test: inject broadband noise → verify AMBIENT_NOISE_SUSTAINED fired
[ ] End-to-end test: inject impulse signal → verify TRANSIENT_NOISE_BURST fired
[ ] End-to-end test: inject dual-pitch audio → verify MULTI_VOICE_ENVIRONMENT fired (CRITICAL)
[ ] End-to-end test: inject reverb tail → verify ECHO_CHAMBER_ENVIRONMENT fired
[ ] End-to-end test: simulate far-field mic → verify DEVICE_PROXIMITY_VIOLATION fired
[ ] End-to-end test: verify no raw audio written to disk or any storage layer
[ ] Audit log entries verified in PostgreSQL after each test class
[ ] ClickHouse environment_quality record generated after test session

Absence of any checkbox → STOP DEPLOYMENT → REPORT GATE-INCOMPLETE
```

---

## XXII. FINAL ENFORCEMENT DECLARATION

```
This agent exists to protect the fairness of a purely rule-driven,
recruiter-less, AI-less assessment system.

Environmental unfairness is not the candidate's fault.
Environmental fraud is a structural integrity breach.

This agent distinguishes between them.
It protects honest candidates from environmental penalties.
It flags fraudulent environmental manipulation for human review.

Detection is mandatory.
Recording is permanently prohibited.
Judgment is permanently prohibited.
Enforcement belongs to the Orchestrator alone.

The backend is the law.
The agent reads the environment.
The environment cannot be hidden.

SEALED · LOCKED · GOVERNED
Version: v1.0.0
```

---

*Part of ECOSKILLER MASTER EXECUTION PROMPT v12.0 — Antigravity Audio Processing Layer*
*Sibling: PHONE_SILENCE_SPAM_AGENT v1.0.0*
*Mutation Policy: Add-only via version bump · Execution Authority: Human declaration only*
