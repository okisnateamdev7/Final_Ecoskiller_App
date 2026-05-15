# 🔒 PHONE_SILENCE_SPAM_AGENT
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
Governed By     : Voice GD Orchestrator · Deterministic Protocol Law
```

---

## ⚠️ SEAL DECLARATION

```
This document is sealed.
No clause may be removed.
No logic may be softened.
No behavior is negotiable.
Violations → STOP EXECUTION → REPORT AGENT-BREACH
```

---

## I. AGENT IDENTITY

| Property | Value |
|---|---|
| Agent Name | `PHONE_SILENCE_SPAM_AGENT` |
| Agent Class | Audio Signal Enforcement Agent |
| Domain | Antigravity · Audio Processing |
| Scope | Voice GD Sessions · Dojo Matches · Interview Rooms |
| Execution Mode | Passive Continuous + Event-Triggered |
| Decision Authority | NONE — Detection only, enforcement via Orchestrator |
| AI Judgment | FORBIDDEN |
| Human Judgment | FORBIDDEN during session |

---

## II. PURPOSE (NON-NEGOTIABLE)

The `PHONE_SILENCE_SPAM_AGENT` is a **real-time audio signal classifier** embedded in the
Antigravity sublayer of Ecoskiller's Audio Processing pipeline.

Its sole function is to detect and flag three classes of audio contamination
that degrade the integrity of Voice GD, Dojo, and Interview sessions:

```
CLASS 1 — PHONE_EVENT
  Definition: Ringing, notification chimes, SMS tones, vibration rattles,
              call pickup audio, hold music fragments, dial tones.
  Threat:     Disrupts session flow, introduces unfair advantage/distraction.

CLASS 2 — SILENCE_ABUSE
  Definition: Deliberate silence during an active speaking token.
              Mic open + zero signal + turn not voluntarily yielded.
  Threat:     Exploits system timing, wastes group time, signals non-participation.

CLASS 3 — SPAM_AUDIO
  Definition: Background TV/radio, repeated noise loops, keyboard spam,
              pre-recorded speech playback, synthetic voice injection,
              non-session audio streams entering the microphone channel.
  Threat:     Pollutes scoring data, fakes presence, degrades other participants.
```

The agent does **not** interpret speech.
The agent does **not** evaluate communication quality.
The agent does **not** assign scores.
The agent **only** classifies signal patterns and emits typed events.

---

## III. SYSTEM POSITION (ARCHITECTURE LOCK)

```
┌─────────────────────────────────────────────────────────────────┐
│                    ECOSKILLER AUDIO PIPELINE                    │
├─────────────────────────────────────────────────────────────────┤
│  WebRTC Transport Layer (audio capture / SRTP encryption)       │
│       ↓                                                         │
│  Jitsi SFU (stream routing only — no logic)                     │
│       ↓                                                         │
│  ┌─────────────────────────────────────────────┐               │
│  │         ANTIGRAVITY SUBLAYER                │               │
│  │  ┌─────────────────────────────────────┐   │               │
│  │  │   PHONE_SILENCE_SPAM_AGENT  ◄──────┤   │               │
│  │  │   (signal classification only)      │   │               │
│  │  └────────────┬────────────────────────┘   │               │
│  │               │ typed events               │               │
│  │               ▼                            │               │
│  │  ┌─────────────────────────────────────┐  │               │
│  │  │   Event Bus (Kafka / RabbitMQ)      │  │               │
│  │  └─────────────┬───────────────────────┘  │               │
│  └────────────────┼────────────────────────── ┘               │
│                   ▼                                             │
│  Voice GD Orchestrator (enforcement decisions)                  │
│  Redis State Machine (penalty application)                      │
│  WebSocket Command Channel (mute/token revoke)                  │
│  PostgreSQL Audit Log (immutable record)                        │
└─────────────────────────────────────────────────────────────────┘
```

**Rules (locked):**
- Agent is downstream of WebRTC transport, upstream of orchestrator.
- Agent never touches audio routing.
- Agent never issues mute commands directly.
- Agent only writes to the Event Bus.
- Orchestrator reads events and decides enforcement.

---

## IV. DETECTION SPECIFICATIONS

### 4.1 — PHONE_EVENT Detection

| Property | Specification |
|---|---|
| Signal Target | Characteristic frequency bands of phone ringtones (500–4000 Hz tonal bursts), notification blips (< 200ms transient), vibration rattle (40–80 Hz amplitude spike) |
| Detection Window | Rolling 500ms frame |
| Threshold | ≥ 2 consecutive positive frames → event fired |
| False Positive Buffer | 1 second suppression after first event (no duplicate fire) |
| Confidence Model | Rule-based frequency envelope matching — NO ML inference |
| Output Event | `audio.phone_event.detected` |

**Phone Event Payload:**
```json
{
  "event": "audio.phone_event.detected",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "detection_window_ms": 500,
  "signal_class": "PHONE_EVENT",
  "sub_type": "ringtone | notification | vibration_rattle | call_audio",
  "confidence_rule": "frequency_envelope_match",
  "frames_matched": 2,
  "action_required": "orchestrator_decision"
}
```

---

### 4.2 — SILENCE_ABUSE Detection

| Property | Specification |
|---|---|
| Trigger Condition | Speaking token ACTIVE + mic open + RMS energy < threshold for continuous window |
| RMS Threshold | < −60 dBFS sustained |
| Detection Window | Continuous 5-second sliding window |
| Grace Period | First 3 seconds of token excluded (preparation buffer) |
| Voluntary Yield | If participant signals yield via WebSocket → detection suspended |
| Output Event | `audio.silence_abuse.detected` |

**Silence Abuse Payload:**
```json
{
  "event": "audio.silence_abuse.detected",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "token_elapsed_seconds": 8,
  "rms_dbfs": -72.4,
  "grace_period_elapsed": true,
  "voluntary_yield": false,
  "signal_class": "SILENCE_ABUSE",
  "action_required": "orchestrator_decision"
}
```

**Note:** Agent distinguishes silence abuse from network drop.
Network drop → separate event class `audio.network_drop.detected`.
Both are emitted. Orchestrator decides penalty path per class.

---

### 4.3 — SPAM_AUDIO Detection

| Property | Specification |
|---|---|
| Signal Targets | Background broadcast audio (sustained speech not matching mic proximity signature), repeating noise loops (spectral autocorrelation), synthetic voice markers (flat prosody + uniform pitch), keyboard injection loops |
| Detection Basis | Spectral repetition analysis + proximity energy ratio |
| Detection Window | 10-second rolling analysis frame |
| Loop Detection | FFT autocorrelation — repeated spectral pattern within ±5% tolerance |
| Synthetic Voice | Pitch variance < 2 Hz over 5 seconds → flagged |
| Output Event | `audio.spam_audio.detected` |

**Spam Audio Payload:**
```json
{
  "event": "audio.spam_audio.detected",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "SPAM_AUDIO",
  "sub_type": "background_broadcast | noise_loop | synthetic_voice | keyboard_spam",
  "detection_method": "spectral_autocorrelation | proximity_ratio | pitch_variance",
  "analysis_window_seconds": 10,
  "repetition_score": 0.94,
  "action_required": "orchestrator_decision"
}
```

---

## V. AGENT EXECUTION STATES

```
IDLE
  └─ No active session in assigned namespace
  └─ Agent process alive, awaiting session_start event

MONITORING
  └─ Session active
  └─ All three classifiers running in parallel
  └─ Event bus open for writes

SUPPRESSED
  └─ Agent temporarily suppressed for specific participant
  └─ Triggered by orchestrator (e.g., approved background context)
  └─ Max suppression window: 60 seconds, non-renewable per session

FAULT
  └─ Classifier process crash
  └─ Emits: audio.agent_fault.detected
  └─ Orchestrator falls back to raw silence tracking only
```

---

## VI. WHAT THE AGENT DOES NOT DO (HARD PROHIBITION)

```
✗ Does NOT record audio
✗ Does NOT store audio samples
✗ Does NOT transcribe speech
✗ Does NOT evaluate communication quality
✗ Does NOT apply penalties (orchestrator only)
✗ Does NOT mute participants (orchestrator only)
✗ Does NOT use machine learning inference
✗ Does NOT apply confidence scoring to behavior
✗ Does NOT label personality, emotion, or intent
✗ Does NOT communicate directly with frontend
✗ Does NOT access participant identity — only session + participant UUID
```

Violation of any prohibition above → `STOP EXECUTION` → `REPORT AGENT-INTEGRITY-BREACH`

---

## VII. ORCHESTRATOR CONTRACT (ENFORCEMENT INTERFACE)

The `PHONE_SILENCE_SPAM_AGENT` outputs to the Event Bus only.
The Voice GD Orchestrator consumes events and applies the following enforcement table:

| Event Class | Orchestrator Response |
|---|---|
| `audio.phone_event.detected` | Log to audit. Warn participant via WebSocket toast. On 2nd event: token forfeit. |
| `audio.silence_abuse.detected` | Token marked `silence_used`. Participation score decremented. Next turn still granted. |
| `audio.spam_audio.detected` | Immediate forced mute. Token revoked. Orchestrator flags session for admin review. |
| `audio.network_drop.detected` | Token skipped. No penalty. Logged for fairness record. |
| `audio.agent_fault.detected` | Orchestrator enters degraded mode. Raw silence tracking only. Session continues. |

**All enforcement is deterministic. No discretionary judgment permitted.**

---

## VIII. TECHNOLOGY BINDING (LOCKED)

| Component | Technology |
|---|---|
| Audio Signal Processing | Native WebRTC `AudioWorklet` API (browser-side pre-analysis) + Server-side Python `librosa` / `pyaudio` pipeline |
| RMS Calculation | `librosa.feature.rms` — 512-frame window |
| Frequency Analysis | `numpy.fft` — 2048-point FFT, 44100 Hz sample rate |
| Spectral Autocorrelation | `scipy.signal.correlate` |
| Event Bus | Apache Kafka (topic: `audio.agent.events`) or RabbitMQ (queue: `audio_agent`) |
| State Coordination | Redis — agent state key: `agent:pssg:{session_id}:{participant_id}` |
| Audit Log | PostgreSQL — table: `audio_agent_events` (immutable, append-only) |
| Deployment | Kubernetes pod — namespace: `realtime` |
| Service Name | `phone-silence-spam-agent` |
| Port | Internal only — no external exposure |

---

## IX. KAFKA EVENT SCHEMA (LOCKED)

**Topic:** `audio.agent.events`
**Partition Key:** `session_id`
**Retention:** 7 days (audit window)
**Schema Version:** v1

```json
{
  "schema_version": "1.0",
  "event": "<string — event type>",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "PHONE_EVENT | SILENCE_ABUSE | SPAM_AUDIO | NETWORK_DROP | AGENT_FAULT",
  "sub_type": "<string | null>",
  "detection_method": "<string>",
  "metadata": "<object — class-specific fields>",
  "action_required": "orchestrator_decision"
}
```

Schema changes require version bump. Breaking changes → `STOP EXECUTION`.

---

## X. POSTGRESQL AUDIT TABLE (IMMUTABLE)

```sql
CREATE TABLE audio_agent_events (
  id               BIGSERIAL PRIMARY KEY,
  session_id       UUID        NOT NULL,
  participant_id   UUID        NOT NULL,
  event_type       VARCHAR(80) NOT NULL,
  signal_class     VARCHAR(40) NOT NULL,
  sub_type         VARCHAR(40),
  detection_method VARCHAR(80),
  metadata         JSONB,
  detected_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  orchestrator_ack BOOLEAN     DEFAULT FALSE,
  penalty_applied  BOOLEAN     DEFAULT FALSE
);

-- Immutability enforcement
CREATE RULE no_update_audio_agent_events AS
  ON UPDATE TO audio_agent_events DO INSTEAD NOTHING;

CREATE RULE no_delete_audio_agent_events AS
  ON DELETE TO audio_agent_events DO INSTEAD NOTHING;
```

---

## XI. KUBERNETES DEPLOYMENT SPEC (LOCKED)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: phone-silence-spam-agent
  namespace: realtime
  labels:
    app: phone-silence-spam-agent
    layer: antigravity
    domain: audio-processing
spec:
  replicas: 2
  selector:
    matchLabels:
      app: phone-silence-spam-agent
  template:
    metadata:
      labels:
        app: phone-silence-spam-agent
    spec:
      containers:
        - name: pssg
          image: ecoskiller/phone-silence-spam-agent:v1.0.0
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
          resources:
            requests:
              cpu: "200m"
              memory: "256Mi"
            limits:
              cpu: "500m"
              memory: "512Mi"
          livenessProbe:
            httpGet:
              path: /healthz
              port: 8080
            initialDelaySeconds: 10
            periodSeconds: 15
          readinessProbe:
            httpGet:
              path: /readyz
              port: 8080
            initialDelaySeconds: 5
            periodSeconds: 10
```

---

## XII. OBSERVABILITY (NON-OPTIONAL)

All metrics exposed on `:8080/metrics` (Prometheus scrape target).

| Metric | Description |
|---|---|
| `pssg_phone_events_total` | Counter — total phone events detected |
| `pssg_silence_abuse_total` | Counter — total silence abuse detections |
| `pssg_spam_audio_total` | Counter — total spam audio detections |
| `pssg_agent_fault_total` | Counter — classifier fault events |
| `pssg_event_publish_latency_ms` | Histogram — Kafka publish latency |
| `pssg_active_sessions` | Gauge — active monitored sessions |

Grafana dashboard: `ecoskiller-antigravity-audio`
Alert thresholds defined in Prometheus rules — not in this document.

---

## XIII. SECURITY CONSTRAINTS

```
✔ Agent receives only audio signal metadata (RMS values, spectral vectors)
✔ Raw audio bytes NEVER written to disk or memory beyond processing frame
✔ Participant UUID only — no name, email, or PII in agent scope
✔ All inter-service communication via internal Kubernetes cluster network
✔ No external API calls from agent process
✔ No external port exposure
✔ Kafka topic ACL: agent has WRITE-only permission
✔ PostgreSQL: agent has INSERT-only permission on audio_agent_events
✔ Redis: agent has SET/GET on own namespace keys only
```

---

## XIV. FAILURE MODES & FALLBACK

| Failure | System Response |
|---|---|
| Agent pod crash | Kubernetes restarts pod. Session continues. Orchestrator enters degraded mode. |
| Kafka publish failure | Retry 3x with 100ms backoff. On failure: write to PostgreSQL directly. |
| Redis unreachable | Agent continues classification. State persistence suspended until reconnect. |
| False positive flood | Orchestrator rate-limits event consumption. Admin alert triggered. |

No failure mode may halt an active GD session.
Graceful degradation is mandatory.

---

## XV. VERSION CONTROL LAW

```
Current Version : v1.0.0
Version Format  : MAJOR.MINOR.PATCH

MAJOR → Breaking change to event schema or detection contract
MINOR → New detection sub-type added (add-only)
PATCH → Internal tuning (threshold adjustment, bug fix)

All version bumps require:
  ✔ Updated schema version in Kafka payload
  ✔ Migration script if PostgreSQL schema changes
  ✔ Updated Helm chart version
  ✔ Changelog entry

No silent mutations.
No undocumented deployments.
```

---

## XVI. INTEGRATION CHECKLIST (DEPLOYMENT GATE)

```
[ ] Kafka topic audio.agent.events created with correct retention
[ ] PostgreSQL audio_agent_events table created with immutability rules
[ ] Redis namespace keys configured for realtime namespace
[ ] Kubernetes deployment applied to realtime namespace
[ ] Prometheus scrape config updated for pssg pod
[ ] Grafana dashboard ecoskiller-antigravity-audio imported
[ ] Voice GD Orchestrator updated to consume audio.agent.events topic
[ ] Orchestrator enforcement table validated against Section VII
[ ] Agent health endpoint /healthz returning 200
[ ] Agent readiness endpoint /readyz returning 200
[ ] End-to-end test: inject synthetic phone signal → verify event emitted
[ ] End-to-end test: inject sustained silence → verify event emitted
[ ] End-to-end test: inject looped audio → verify event emitted
[ ] Audit log entries verified in PostgreSQL after test session

Absence of any checkbox → STOP DEPLOYMENT → REPORT GATE-INCOMPLETE
```

---

## XVII. FINAL ENFORCEMENT DECLARATION

```
This agent is part of ECOSKILLER's structural fairness guarantee.
Its purpose is to make the assessment environment honest,
not to punish participants beyond what the protocol prescribes.

Detection is mandatory.
Enforcement is the Orchestrator's jurisdiction.
Recording is permanently prohibited.
Judgment is permanently prohibited.

The backend is the law.
The agent enforces signal integrity.
Candidates operate under constraint.
Output is measurable, auditable behavior.

SEALED · LOCKED · GOVERNED
Version: v1.0.0
```

---

*Part of ECOSKILLER MASTER EXECUTION PROMPT v12.0 — Antigravity Audio Processing Layer*
*Mutation Policy: Add-only via version bump · Execution Authority: Human declaration only*
