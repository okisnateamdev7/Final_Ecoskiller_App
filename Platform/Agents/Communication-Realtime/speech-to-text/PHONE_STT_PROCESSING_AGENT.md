# 🔒 PHONE_STT_PROCESSING_AGENT
## STT & ML · Antigravity Layer · Ecoskiller Platform

```
Artifact Class  : Production Agent Specification
Layer           : STT & ML → Antigravity Sublayer
Status          : FINAL · LOCKED · SEALED · GOVERNED
Mutation Policy : Add-only via version bump
Version         : v1.0.0
Interpretation Authority : NONE
Execution Authority      : Human declaration only
Parent System   : ECOSKILLER — MASTER EXECUTION PROMPT v12.0

Antigravity Layer — Full Agent Chain:
  PHONE_SILENCE_SPAM_AGENT               (PSSG)  v1.0.0  Audio Processing  :8080
  PHONE_BACKGROUND_NOISE_DETECTION_AGENT (PBNDA) v1.0.0  Audio Processing  :8081
  LANGUAGE_DETECTION_AGENT               (LDA)   v1.0.0  STT & ML          :8082
  PHONE_LANGUAGE_MODEL_SELECTION_AGENT   (PLMSA) v1.0.0  STT & ML          :8083
  PHONE_STT_PROCESSING_AGENT             (PSTPA) v1.0.0  STT & ML          :8084

Governed By:
  R28 — Intelligence Cost Optimization Law
  Voice GD Orchestrator · Deterministic Protocol Law
  Scoring Engine Contract (immutable)
  Model Registry Service Law
  Data Capture Law — No Audio Storage (Absolute)

Intelligence Cost Class : Traditional ML (R28-2) — Acoustic feature pipeline
Agent Role              : Acoustic Behavioral Signal Extractor
                          Primary data supplier to Scoring Engine
```

---

## ⚠️ SEAL DECLARATION

```
This document is sealed.
No clause may be removed.
No logic may be softened.
No behavior is negotiable.

This agent NEVER produces transcriptions.
This agent NEVER stores audio.
This agent NEVER evaluates speech quality.
This agent NEVER scores participants.
This agent NEVER uses LLM or generative AI.

It extracts only behavioral acoustic signals from enforced protocol windows.
It feeds only numeric measurements to the Scoring Engine.

Violations → STOP EXECUTION → REPORT AGENT-INTEGRITY-BREACH
```

---

## I. AGENT IDENTITY

| Property | Value |
|---|---|
| Agent Name | `PHONE_STT_PROCESSING_AGENT` |
| Short Handle | `PSTPA` |
| Agent Class | Acoustic Behavioral Signal Extractor |
| Layer | STT & ML → Antigravity Sublayer |
| Intelligence Class | Traditional ML (R28-2) — Feature extraction pipeline |
| Domain Scope | Voice GD · Dojo Matches · Interview Rooms · All Voice Sessions |
| Execution Mode | Active — per speaking token window + open discussion round |
| Decision Authority | NONE — Signal extraction and event emission only |
| Enforcement Authority | Voice GD Orchestrator (exclusive) |
| Scoring Authority | Scoring Engine (exclusive) |
| Transcription | PERMANENTLY AND ARCHITECTURALLY PROHIBITED |
| Raw Audio Retention | PERMANENTLY PROHIBITED |
| LLM Inference | PERMANENTLY PROHIBITED (R28-4) |
| Speech Content Analysis | PERMANENTLY PROHIBITED |
| Tone / Emotion / Accent Analysis | PERMANENTLY PROHIBITED |

---

## II. SYSTEM DESIGN PHILOSOPHY (LOCKED)

This agent operates under the core design law of the Ecoskiller Voice GD system:

```
Replace judgment with protocol.
Replace observation with instrumentation.
Replace moderation with state machines.
```

The system explicitly avoids:
```
✗ Confidence scoring
✗ Personality traits
✗ Leadership labels
✗ Communication quality labels
✗ Accent or tone analysis
```

It operates only on:
```
✔ Permission (mic open / closed state)
✔ Time (speaking duration per token, per round, per session)
✔ Compliance (turn taken / skipped / interrupted / incomplete)
✔ Participation (presence, engagement, behavioral pattern)
```

The system does not attempt to "understand" candidates.
It measures behavior under enforced constraints.
Output is numeric, reproducible, and auditable.

---

## III. PURPOSE (NON-NEGOTIABLE)

The `PHONE_STT_PROCESSING_AGENT` is the **acoustic behavioral signal extractor**
of Ecoskiller's Antigravity STT & ML sublayer.

It sits at the terminal position of the Antigravity pipeline — after noise
detection, after language detection, after model selection — and produces
the **numeric behavioral dataset** that the Scoring Engine consumes to
generate reproducible, bias-free GD, Dojo, and Interview scores.

### Problems It Solves

```
PROBLEM 1 — SCORING ENGINE NEEDS NUMERIC SIGNALS, NOT AUDIO
  The Scoring Engine is a rule-based system (R28-1).
  It cannot consume raw audio.
  It requires structured numeric measurements:
    speaking_duration_seconds, silence_ratio, interrupt_count,
    turn_compliance_rate, energy_consistency_score, and more.
  PSTPA is the only source of these measurements.

PROBLEM 2 — BEHAVIORAL SIGNALS MUST BE LANGUAGE-AGNOSTIC
  The Scoring Engine applies identical scoring rules to all languages.
  PSTPA extracts acoustic behavioral signals that are fully
  language-independent: energy, duration, onset count, pause length.
  No language knowledge is required to measure whether a
  candidate spoke for their full 60 seconds.

PROBLEM 3 — TURN COMPLIANCE REQUIRES REAL-TIME ACOUSTIC CONFIRMATION
  The Orchestrator grants speaking tokens and enforces mute/unmute.
  But the Scoring Engine needs confirmed acoustic evidence:
    Did the participant actually speak? For how long?
    Did they fall silent mid-token? How many times?
    Did they attempt to interrupt while muted?
  PSTPA provides this acoustic confirmation layer.

PROBLEM 4 — OPEN DISCUSSION ROUND NEEDS DOMINANCE MEASUREMENT
  After structured turns, all participants are unmuted simultaneously.
  The Scoring Engine penalizes dominance (speaking over others).
  PSTPA tracks speaking time distribution across all participants
  during the open round and measures overlap and dominance ratio.

PROBLEM 5 — ENVIRONMENT-ADJUSTED SIGNALS
  PBNDA flags degraded environments (noise, echo, proximity).
  LDA flags language context.
  PLMSA selects appropriate models.
  PSTPA consumes all these upstream flags and applies
  environment-adjusted feature extraction — ensuring that a
  candidate in a noisy environment is not unfairly scored
  for measurement artifacts, only for behavioral facts.
```

---

## IV. R28 COMPLIANCE DECLARATION (MANDATORY)

```
Feature Extraction Classification:
  Type            : Traditional ML pipeline (R28-2)
  Method          : Acoustic feature engineering — MFCC, RMS, ZCR,
                    onset detection, pitch tracking, VAD
                    (no neural transcription, no LLM)
  Justification   : Behavioral acoustic signal extraction is a
                    structured feature computation task, not an NLU task.
                    R28-3 (LLM permitted) does NOT apply.
                    R28-2 (Traditional ML) is the correct class.

VAD (Voice Activity Detection) Classification:
  Type            : Traditional ML binary classifier (R28-2)
  Model           : WebRTC VAD (Google, open-source) + energy-threshold rule engine
  Justification   : Binary speech/silence classification — classic ML classification.

Scoring Signal Computation:
  Type            : Rule engine (R28-1)
  Justification   : Deterministic formulas applied to extracted features.
                    No prediction required — pure arithmetic.

LLM Permitted     : NO (R28-4) — all tasks solvable by traditional ML + rules.

Cost Declaration (R28-5 Mandatory):
  Pipeline type   : Acoustic feature extraction + VAD + rule-based scoring signals
  Inference cost  : < $0.003 per token window (self-hosted compute only)
  Monthly at MVP  : < $25/month at 10,000 sessions × avg 12 tokens per participant

Absence of R28 compliance declaration → STOP EXECUTION
```

---

## V. SYSTEM POSITION (ARCHITECTURE LOCK)

```
┌──────────────────────────────────────────────────────────────────────────────────┐
│                          ECOSKILLER AUDIO PIPELINE                               │
├──────────────────────────────────────────────────────────────────────────────────┤
│  WebRTC Transport Layer · Jitsi SFU                                              │
│         ↓  (audio signal metadata only — no raw audio to backend)               │
│  ┌───────────────────────────────────────────────────────────────────────┐      │
│  │                        ANTIGRAVITY SUBLAYER                           │      │
│  │                                                                       │      │
│  │  ┌─ AUDIO PROCESSING LAYER ────────────────────────────────────────┐ │      │
│  │  │  PSSG  :8080  — Phone · Silence · Spam                          │ │      │
│  │  │  PBNDA :8081  — Noise · Echo · Multi-voice · Proximity          │ │      │
│  │  │          │ → writes: agent:pbnda:* Redis                         │ │      │
│  │  └──────────┼──────────────────────────────────────────────────────┘ │      │
│  │             │                                                         │      │
│  │  ┌─ STT & ML LAYER ────────────────────────────────────────────────┐ │      │
│  │  │                                                                  │ │      │
│  │  │  LDA   :8082  — Language Detection                              │ │      │
│  │  │          │ → writes: agent:lda:* Redis                           │ │      │
│  │  │                                                                  │ │      │
│  │  │  PLMSA :8083  — Model Selection · Cost Governance               │ │      │
│  │  │          │ → writes: agent:plmsa:*:model_config Redis            │ │      │
│  │  │                                                                  │ │      │
│  │  │  ┌───────────────────────────────────────────────────────────┐  │ │      │
│  │  │  │   PHONE_STT_PROCESSING_AGENT (PSTPA) :8084  ◄────────────┤  │ │      │
│  │  │  │                                                           │  │ │      │
│  │  │  │   READS:                                                  │  │ │      │
│  │  │  │     · Audio signal frames (PCM metadata via AudioWorklet) │  │ │      │
│  │  │  │     · model_config ← PLMSA Redis                         │  │ │      │
│  │  │  │     · environment_flags ← PBNDA Redis                    │  │ │      │
│  │  │  │     · language_context ← LDA Redis                       │  │ │      │
│  │  │  │     · token_state ← Orchestrator Redis                   │  │ │      │
│  │  │  │     · session_metadata ← PostgreSQL                      │  │ │      │
│  │  │  │                                                           │  │ │      │
│  │  │  │   PRODUCES:                                               │  │ │      │
│  │  │  │     · Acoustic behavioral signals → Redis (per token)    │  │ │      │
│  │  │  │     · Scoring Engine dataset → Kafka                     │  │ │      │
│  │  │  │     · Session behavioral summary → ClickHouse            │  │ │      │
│  │  │  │     · Immutable audit record → PostgreSQL                │  │ │      │
│  │  │  └───────────────────────────────────────────────────────────┘  │ │      │
│  │  └──────────────────────────────────────────────────────────────── ┘ │      │
│  │                                                                       │      │
│  │  Antigravity Event Aggregator → Kafka: audio.agent.events            │      │
│  └───────────────────────────────────────────────────────────────────── ┘      │
│                                                                                  │
│  ┌─ DOWNSTREAM CONSUMERS ──────────────────────────────────────────────────┐   │
│  │  Scoring Engine         ← primary consumer of PSTPA behavioral signals  │   │
│  │  Voice GD Orchestrator  ← receives token_compliance events              │   │
│  │  Analytics Service      ← ClickHouse session behavioral summaries       │   │
│  │  Admin Governance       ← anomaly flags from PSTPA                      │   │
│  └──────────────────────────────────────────────────────────────────────── ┘   │
└──────────────────────────────────────────────────────────────────────────────────┘
```

**Architecture Rules (locked):**
- PSTPA is the terminal processing agent — it runs after all upstream agents complete.
- PSTPA reads model configuration from PLMSA before each token — never self-selects models.
- PSTPA reads environment flags from PBNDA — applies adjustments to feature thresholds.
- PSTPA reads language context from LDA — applies language-aware pause profiling.
- PSTPA never produces transcription text under any condition.
- PSTPA is the exclusive supplier of acoustic behavioral signals to the Scoring Engine.
- Scoring Engine never reads raw audio — PSTPA is the only intermediary.

---

## VI. ACOUSTIC FEATURE EXTRACTION PIPELINE (LOCKED)

### 6.1 — Audio Input Specification

```
INPUT SOURCE:   WebRTC AudioWorklet API — browser-side pre-processing
INPUT FORMAT:   PCM float32 stream, 44100 Hz sample rate
RESAMPLED TO:   16000 Hz (standard for acoustic model input)
FRAME SIZE:     25ms analysis frame, 10ms stride (standard acoustic window)
CHUNK SIZE:     100ms processing chunks (10 frames per chunk)
BUFFER WINDOW:  Rolling 3-second window for feature aggregation

RAW AUDIO POLICY:
  Raw PCM bytes are processed in-memory only.
  Never written to disk.
  Never sent to external services.
  Buffer cleared immediately after each feature extraction cycle.
  Maximum in-memory retention: one 3-second analysis window.
```

### 6.2 — Voice Activity Detection (VAD) — Primary Gate

VAD is the primary gate. All subsequent features are computed only when
VAD confirms active speech. This is the behavioral measurement foundation.

```
ENGINE 1: WebRTC VAD (Google, open-source)
  Mode:       Mode 3 (most aggressive — minimizes false positives)
  Frame size: 10ms, 20ms, or 30ms (adaptive per network quality)
  Output:     Binary — SPEECH | SILENCE per frame

ENGINE 2: Energy-threshold rule engine (R28-1 backup)
  RMS threshold: dynamically calibrated from PBNDA noise floor baseline
  Formula:    is_speech = (rms_frame > noise_floor + 6dB)
  Used when:  WebRTC VAD confidence < 0.70 due to noise contamination

FUSION RULE (deterministic):
  IF webrtc_vad == SPEECH AND energy_threshold == SPEECH → CONFIRMED_SPEECH
  IF webrtc_vad == SPEECH AND energy_threshold == SILENCE → UNCERTAIN_SPEECH
  IF webrtc_vad == SILENCE AND energy_threshold == SILENCE → CONFIRMED_SILENCE
  IF webrtc_vad == SILENCE AND energy_threshold == SPEECH → NOISE_ARTIFACT
  
  CONFIRMED_SPEECH  → all features extracted
  UNCERTAIN_SPEECH  → energy features only (conservative measurement)
  CONFIRMED_SILENCE → silence counter incremented
  NOISE_ARTIFACT    → flagged, not counted as speech or silence
```

### 6.3 — Acoustic Feature Set (LOCKED)

All features are extracted per speaking token window and aggregated.
No raw audio, no text, no semantic content.

#### Feature Group A — Speaking Duration Signals

| Feature | Computation | Unit | Purpose |
|---|---|---|---|
| `speaking_duration_confirmed` | Sum of CONFIRMED_SPEECH frames × frame_duration | Seconds | Core turn compliance metric |
| `speaking_duration_uncertain` | Sum of UNCERTAIN_SPEECH frames × frame_duration | Seconds | Conservative fallback |
| `silence_duration_total` | Sum of CONFIRMED_SILENCE frames × frame_duration | Seconds | Silence ratio computation |
| `token_duration_total` | Token end_time − token start_time | Seconds | Denominator for all ratios |
| `speaking_ratio` | speaking_duration_confirmed / token_duration_total | Float 0–1 | Primary participation metric |
| `silence_ratio` | silence_duration_total / token_duration_total | Float 0–1 | Silence abuse complement |
| `noise_artifact_ratio` | noise_artifact_frames / total_frames | Float 0–1 | Environment quality flag |

#### Feature Group B — Speaking Continuity Signals

| Feature | Computation | Unit | Purpose |
|---|---|---|---|
| `speech_segment_count` | Number of SPEECH → SILENCE → SPEECH transitions | Integer | Fragmentation measure |
| `longest_continuous_speech_ms` | Max duration of uninterrupted CONFIRMED_SPEECH | Milliseconds | Sustained engagement |
| `pause_count` | Number of CONFIRMED_SILENCE gaps within speech | Integer | Natural pause vs hesitation |
| `avg_pause_duration_ms` | Total pause duration / pause_count | Milliseconds | Fluency proxy (behavior only) |
| `max_pause_duration_ms` | Longest single silence gap during speaking token | Milliseconds | Hesitation detection |
| `speech_onset_count` | Number of voice onset events detected | Integer | Engagement restarts |

#### Feature Group C — Energy & Intensity Signals

| Feature | Computation | Unit | Purpose |
|---|---|---|---|
| `rms_mean_dbfs` | Mean RMS energy across CONFIRMED_SPEECH frames | dBFS | Speaking energy level |
| `rms_std_dbfs` | Standard deviation of RMS across speech frames | dBFS | Energy consistency |
| `rms_peak_dbfs` | Maximum RMS across speech frames | dBFS | Peak effort measurement |
| `energy_consistency_score` | 1 − (rms_std / rms_mean) clamped 0–1 | Float 0–1 | Engagement consistency |
| `energy_decay_rate` | Linear regression slope on RMS over token duration | dBFS/sec | Energy drop-off (fatigue proxy) |

#### Feature Group D — Interrupt & Compliance Signals

| Feature | Computation | Unit | Purpose |
|---|---|---|---|
| `interrupt_attempts` | Speech onset events during another participant's active token | Integer | Protocol violation count |
| `interrupt_suppressed_duration_ms` | Duration of speech attempted while muted | Milliseconds | Persistence of interruption |
| `token_start_latency_ms` | Time from token grant to first speech onset | Milliseconds | Readiness measure |
| `token_end_compliance` | Did speech stop within 500ms of token expiry? | Boolean | Exit compliance |
| `voluntary_yield` | Did participant signal yield before token expiry? | Boolean | Cooperative behavior |
| `mic_open_unused_ms` | Token duration − speaking_duration (when VAD=SILENCE throughout) | Milliseconds | Full silence turn |

#### Feature Group E — Open Discussion Round Signals

These features are computed only during the open discussion phase (all mics unmuted).

| Feature | Computation | Unit | Purpose |
|---|---|---|---|
| `open_round_speaking_ms` | Total CONFIRMED_SPEECH duration in open round | Milliseconds | Open round contribution |
| `open_round_speaking_ratio` | open_round_speaking_ms / open_round_duration_ms | Float 0–1 | Proportional participation |
| `dominance_ratio` | own_speaking_ms / total_group_speaking_ms (open round) | Float 0–1 | Dominance detection |
| `overlap_duration_ms` | Speaking while another participant also CONFIRMED_SPEECH | Milliseconds | Interrupt overlap measure |
| `overlap_count` | Number of overlap events (simultaneous speech with another) | Integer | Interruption frequency |
| `response_latency_ms` | Time from any other participant's speech end to own onset | Milliseconds | Engagement responsiveness |

#### Feature Group F — Session-Level Aggregated Signals

| Feature | Computation | Unit | Purpose |
|---|---|---|---|
| `turns_completed` | Tokens where speaking_ratio > 0.15 | Integer | Turn compliance count |
| `turns_skipped` | Tokens where speaking_ratio < 0.05 AND voluntary_yield = false | Integer | Non-participation count |
| `turns_partial` | Tokens where 0.05 ≤ speaking_ratio < 0.15 | Integer | Partial engagement |
| `total_speaking_ms` | Sum of all speaking_duration_confirmed across all tokens | Milliseconds | Session participation |
| `avg_speaking_ratio` | Mean speaking_ratio across completed turns | Float 0–1 | Consistency measure |
| `compliance_score_raw` | Rule-based formula (Section VIII) | Float 0–100 | Pre-scoring signal |
| `interrupt_total` | Sum of interrupt_attempts across all tokens | Integer | Protocol violation total |
| `network_drop_count` | Tokens lost due to network interruption | Integer | Environment fairness flag |
| `early_exit_flag` | Did participant leave before session end? | Boolean | Commitment flag |
| `rejoin_attempt_count` | Number of rejoin attempts after exit (all denied) | Integer | Protocol audit |

---

## VII. ENVIRONMENT-ADJUSTED EXTRACTION (LOCKED)

PSTPA consumes upstream environment flags and adjusts feature thresholds
to prevent environmental artifacts from polluting behavioral signals.

### 7.1 — PBNDA Flag Adjustments

| PBNDA Flag | PSTPA Adjustment |
|---|---|
| `AMBIENT_NOISE_SUSTAINED (HIGH)` | RMS speech threshold raised by +6 dB. Noise floor recalibrated. |
| `TRANSIENT_NOISE_BURST (active token)` | Frames within ±200ms of burst excluded from all feature computation. Flagged as `noise_artifact`. |
| `MULTI_VOICE_ENVIRONMENT` | `overlap_count` and `dominance_ratio` marked `ENVIRONMENT_COMPROMISED`. Not used by Scoring Engine. |
| `ECHO_CHAMBER_ENVIRONMENT` | Post-speech decay tail (RT60) excluded from silence computation. `silence_ratio` recalibrated. |
| `DEVICE_PROXIMITY_VIOLATION` | HF energy ratio applied to RMS normalization. `energy_consistency_score` uses proximity-adjusted baseline. |

### 7.2 — LDA Language Context Adjustments

| LDA Flag | PSTPA Adjustment |
|---|---|
| `CODE_SWITCH_ACTIVE (hi-en)` | `avg_pause_duration_ms` threshold extended +20% (code-switching introduces natural inter-language pauses). |
| `LANGUAGE_CONTEXT_DEGRADED` | `token_start_latency_ms` tolerance extended +500ms (uncertain language context → participant may need moment to orient). |
| `LANGUAGE_MISMATCH_ACTIVE` | No feature adjustment. Flag forwarded to Scoring Engine in behavioral packet. |

### 7.3 — PLMSA Model Config Consumption

| PLMSA Config | PSTPA Action |
|---|---|
| `acoustic_model_id` | Load corresponding Whisper encoder config for feature extraction frame. |
| `quality_tier` | Set VAD fusion sensitivity: TIER_1/2 → standard, TIER_3 → energy threshold dominant, TIER_4 → energy threshold exclusive. |
| `echo_compensation` | Apply pre-emphasis filter to RMS computation input. |
| `proximity_hf_weight` | Apply HF energy ratio weighting to spectral centroid computation. |

---

## VIII. SCORING ENGINE BEHAVIORAL PACKET (LOCKED)

PSTPA emits one `BEHAVIORAL_PACKET` per speaking token to the Kafka topic.
The Scoring Engine consumes these packets exclusively.

**This packet is the only permitted input source for behavioral scoring.**
Scoring Engine may not consume raw audio, PBNDA events, LDA events, or
PSSG events for scoring purposes — only PSTPA behavioral packets.

### 8.1 — Token-Level Behavioral Packet

```json
{
  "packet_type": "TOKEN_BEHAVIORAL_PACKET",
  "schema_version": "1.0",
  "producer": "pstpa-v1",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "token_id": "<uuid>",
  "token_sequence": 3,
  "round_type": "INTRO | REBUTTAL | CONCLUSION | OPEN_DISCUSSION",
  "token_duration_total_seconds": 60.0,
  "timestamp_utc": "<iso8601>",

  "vad_summary": {
    "confirmed_speech_seconds": 48.3,
    "confirmed_silence_seconds": 9.2,
    "uncertain_speech_seconds": 1.8,
    "noise_artifact_seconds": 0.7,
    "vad_engine_used": "webrtc_vad | energy_threshold | fusion"
  },

  "speaking_duration": {
    "confirmed_seconds": 48.3,
    "uncertain_seconds": 1.8,
    "speaking_ratio": 0.805,
    "silence_ratio": 0.153,
    "noise_artifact_ratio": 0.012
  },

  "continuity": {
    "speech_segment_count": 4,
    "longest_continuous_speech_ms": 22400,
    "pause_count": 3,
    "avg_pause_duration_ms": 920,
    "max_pause_duration_ms": 2100,
    "speech_onset_count": 4
  },

  "energy": {
    "rms_mean_dbfs": -28.4,
    "rms_std_dbfs": 4.1,
    "rms_peak_dbfs": -18.2,
    "energy_consistency_score": 0.856,
    "energy_decay_rate_dbfs_per_sec": -0.12
  },

  "compliance": {
    "interrupt_attempts": 0,
    "interrupt_suppressed_duration_ms": 0,
    "token_start_latency_ms": 820,
    "token_end_compliance": true,
    "voluntary_yield": false,
    "mic_open_unused_ms": 0
  },

  "environment_context": {
    "quality_tier": "TIER_2",
    "pbnda_flags_active": [],
    "lda_language": "hi-en",
    "lda_compliance": "TOLERANCE",
    "lda_code_switch_active": true,
    "adjustments_applied": ["pause_threshold_extended_20pct"],
    "scoring_reliability": "STANDARD | DEGRADED | VOID_PENDING_REVIEW"
  },

  "turn_result": {
    "turn_taken": true,
    "turn_complete": true,
    "turn_skipped": false,
    "turn_partial": false
  }
}
```

### 8.2 — Open Discussion Round Packet

```json
{
  "packet_type": "OPEN_ROUND_BEHAVIORAL_PACKET",
  "schema_version": "1.0",
  "producer": "pstpa-v1",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "round_duration_seconds": 180.0,
  "timestamp_utc": "<iso8601>",

  "open_round": {
    "speaking_ms": 42300,
    "speaking_ratio": 0.235,
    "dominance_ratio": 0.271,
    "overlap_duration_ms": 3200,
    "overlap_count": 4,
    "response_latency_ms": 680,
    "dominance_flag": false
  },

  "group_context": {
    "total_group_speaking_ms": 156000,
    "participant_count_active": 5,
    "fair_share_ratio": 0.200,
    "deviation_from_fair_share": 0.071
  },

  "environment_context": {
    "quality_tier": "TIER_2",
    "scoring_reliability": "STANDARD"
  }
}
```

### 8.3 — Session Summary Packet

Emitted once per participant at session end. The Scoring Engine uses this
for final aggregate scoring computation.

```json
{
  "packet_type": "SESSION_SUMMARY_PACKET",
  "schema_version": "1.0",
  "producer": "pstpa-v1",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "session_type": "VOICE_GD | DOJO_MATCH | INTERVIEW",
  "timestamp_utc": "<iso8601>",

  "session_aggregates": {
    "turns_completed": 4,
    "turns_skipped": 0,
    "turns_partial": 0,
    "total_token_count": 4,
    "total_speaking_ms": 183200,
    "avg_speaking_ratio": 0.822,
    "avg_energy_consistency": 0.841,
    "interrupt_total": 1,
    "network_drop_count": 0,
    "early_exit_flag": false,
    "rejoin_attempt_count": 0,
    "voluntary_yield_count": 0
  },

  "open_round_summary": {
    "speaking_ratio": 0.235,
    "dominance_ratio": 0.271,
    "overlap_count_total": 4,
    "dominance_flag": false
  },

  "environment_summary": {
    "overall_quality_tier": "TIER_2",
    "pbnda_flags_encountered": ["AMBIENT_NOISE_SUSTAINED_MEDIUM"],
    "adjustments_applied": ["rms_threshold_raised_6db"],
    "language_verified": true,
    "language_compliant": true,
    "scoring_reliability_final": "STANDARD"
  },

  "scoring_input_ready": true
}
```

---

## IX. SCORING ENGINE FORMULA INPUTS (CONTRACT — LOCKED)

The Scoring Engine applies the following deterministic formula using PSTPA signals.
This formula is documented here for traceability. Changes require Scoring Engine
version bump AND PSTPA contract update.

```
BASE SCORE (per participant):

  TURN_SCORE = 0
  FOR each token:
    IF turn_taken == true:
      TURN_SCORE += 10
    IF speaking_ratio >= 0.80:
      TURN_SCORE += 10
    TURN_SCORE -= (interrupt_attempts × 2)

  OPEN_ROUND_SCORE = 0
  IF open_round.dominance_flag == false:
    OPEN_ROUND_SCORE += 10
  IF open_round.speaking_ratio >= fair_share_ratio:
    OPEN_ROUND_SCORE += 5
  OPEN_ROUND_SCORE -= (open_round.overlap_count × 1)

  COMPLIANCE_SCORE = 0
  IF interrupt_total == 0:
    COMPLIANCE_SCORE += 15
  IF early_exit_flag == false:
    COMPLIANCE_SCORE += 5
  IF network_drop_count == 0:
    COMPLIANCE_SCORE += 5

  RAW_SCORE = TURN_SCORE + OPEN_ROUND_SCORE + COMPLIANCE_SCORE

Notes:
  All inputs sourced exclusively from PSTPA SESSION_SUMMARY_PACKET.
  Formula is deterministic — identical inputs produce identical scores.
  Scores are reproducible and auditable.
  No confidence metrics. No leadership inference. Only compliance and participation.
```

---

## X. DETECTION EVENT CLASSES

### 10.1 — BEHAVIORAL_SIGNAL_CAPTURED

Emitted at the end of every speaking token.

```json
{
  "event": "audio.pstpa.behavioral_signal_captured",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "token_id": "<uuid>",
  "signal_class": "BEHAVIORAL_SIGNAL_CAPTURED",
  "packet_reference": "<uuid — references full packet in Kafka>",
  "turn_taken": true,
  "speaking_ratio": 0.805,
  "interrupt_attempts": 0,
  "scoring_reliability": "STANDARD",
  "action_required": "scoring_engine_consume"
}
```

### 10.2 — TURN_ANOMALY_DETECTED

Emitted when extraction reveals a behavioral anomaly requiring Orchestrator attention.

```json
{
  "event": "audio.pstpa.turn_anomaly_detected",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "token_id": "<uuid>",
  "signal_class": "TURN_ANOMALY_DETECTED",
  "anomaly_type": "FULL_SILENCE_TURN | INTERRUPT_BURST | ENERGY_FLATLINE | MIC_OPEN_UNUSED",
  "anomaly_detail": "<string>",
  "action_required": "orchestrator_review"
}
```

**Anomaly Type Definitions:**

| Anomaly Type | Trigger Condition |
|---|---|
| `FULL_SILENCE_TURN` | speaking_ratio < 0.05 AND voluntary_yield = false for full token |
| `INTERRUPT_BURST` | interrupt_attempts ≥ 3 within a single token |
| `ENERGY_FLATLINE` | rms_std_dbfs < 0.5 over 10+ seconds (possible pre-recorded loop) |
| `MIC_OPEN_UNUSED` | mic_open_unused_ms > 80% of token duration |

### 10.3 — SESSION_SUMMARY_READY

Emitted when the session summary packet is complete and ready for Scoring Engine.

```json
{
  "event": "audio.pstpa.session_summary_ready",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "signal_class": "SESSION_SUMMARY_READY",
  "scoring_input_ready": true,
  "scoring_reliability_final": "STANDARD | DEGRADED | VOID_PENDING_REVIEW",
  "packet_reference": "<uuid>",
  "action_required": "scoring_engine_finalize"
}
```

### 10.4 — EXTRACTION_FAULT

Emitted when feature extraction fails for a token window.

```json
{
  "event": "audio.pstpa.extraction_fault",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "token_id": "<uuid>",
  "signal_class": "EXTRACTION_FAULT",
  "fault_reason": "model_unavailable | audio_feed_interrupted | vad_timeout | buffer_overflow",
  "fallback_applied": true,
  "fallback_method": "energy_threshold_only | silence_assumed | network_drop_recorded",
  "action_required": "orchestrator_notification"
}
```

---

## XI. AGENT EXECUTION STATES

```
STANDBY
  └─ Process alive
  └─ VAD engines loaded and warm (WebRTC VAD + energy-threshold)
  └─ PLMSA model catalogue accessible via Redis
  └─ Awaiting: session.started event

SESSION_INIT
  └─ Session metadata read from PostgreSQL
  └─ PBNDA baseline calibration values read from Redis
  └─ PLMSA initial model config read from Redis
  └─ LDA session language context read from Redis
  └─ Feature buffers initialized per participant
  └─ VAD thresholds calibrated from PBNDA noise floor baseline

TOKEN_ACTIVE
  └─ Speaking token assigned to participant
  └─ All Feature Group A–D extraction running (25ms frames)
  └─ VAD fusion active
  └─ Interrupt detection active for all non-token-holder participants
  └─ Environment adjustments applied per current PBNDA flags
  └─ Per-frame signals accumulated in rolling buffer

TOKEN_COMPLETE
  └─ Token end event received from Orchestrator
  └─ Final feature aggregation computed
  └─ TOKEN_BEHAVIORAL_PACKET assembled and published to Kafka
  └─ BEHAVIORAL_SIGNAL_CAPTURED event emitted
  └─ Feature buffers flushed (no data retained between tokens)

OPEN_ROUND_ACTIVE
  └─ All participants unmuted (Orchestrator signal)
  └─ Feature Group E extraction active for all participants simultaneously
  └─ Dominance and overlap tracking across all streams
  └─ OPEN_ROUND_BEHAVIORAL_PACKET assembled on round end

SESSION_COMPLETE
  └─ Session end event received
  └─ SESSION_SUMMARY_PACKET assembled from all token packets
  └─ SESSION_SUMMARY_READY event emitted to Scoring Engine
  └─ Final records written to PostgreSQL and ClickHouse
  └─ All in-memory buffers cleared (zero audio retention)
  └─ Redis keys expired

FAULT
  └─ Extraction pipeline failure
  └─ EXTRACTION_FAULT event emitted per affected token
  └─ Fallback method applied — session never halted
  └─ audio.agent_fault.pstpa emitted to ops
```

---

## XII. WHAT THE AGENT DOES NOT DO (HARD PROHIBITION)

```
✗ Does NOT transcribe speech — ever, architecturally impossible by design
✗ Does NOT store audio samples beyond the active 3-second processing buffer
✗ Does NOT evaluate communication quality, vocabulary, or grammar
✗ Does NOT assess confidence, personality, or leadership
✗ Does NOT analyze tone, emotion, accent, or prosodic meaning
✗ Does NOT identify individuals from voice characteristics
✗ Does NOT use LLM or generative AI (R28-4)
✗ Does NOT score participants — Scoring Engine only
✗ Does NOT issue mute commands — Orchestrator only
✗ Does NOT access audio streams of other participants beyond open round
✗ Does NOT retain inter-token state (buffers flushed after each token packet)
✗ Does NOT communicate directly with frontend
✗ Does NOT access participant PII — UUID only
✗ Does NOT share raw feature vectors externally — only aggregated behavioral packets
```

Violation of any prohibition → `STOP EXECUTION` → `REPORT AGENT-INTEGRITY-BREACH`

---

## XIII. TECHNOLOGY BINDING (LOCKED)

| Component | Technology |
|---|---|
| Runtime | Python 3.11 |
| Audio Input | WebRTC AudioWorklet API — PCM float32, 44100 Hz |
| Resampling | `librosa.resample` — 44100 → 16000 Hz |
| VAD Engine 1 | WebRTC VAD (`webrtcvad` Python bindings) — Mode 3 |
| VAD Engine 2 | Energy-threshold rule engine — `librosa.feature.rms` |
| RMS Extraction | `librosa.feature.rms` — 512-sample frame |
| MFCC Extraction | `librosa.feature.mfcc` — 40 coefficients |
| Onset Detection | `librosa.onset.onset_strength` — voice onset events |
| Pitch Tracking | `librosa.yin` — F0 trajectory (for energy decay context) |
| Spectral Features | `librosa.feature.spectral_centroid`, `spectral_rolloff` |
| Acoustic Models | Whisper encoder (via PLMSA config) — feature extraction only |
| Overlap Detection | Cross-stream energy comparison — `numpy` operations |
| Dominance Ratio | Per-stream speaking_ms / total group speaking_ms |
| Model Config Read | Redis 7 — `agent:plmsa:{sid}:{pid}:model_config` (read-only) |
| PBNDA Flag Read | Redis 7 — `agent:pbnda:{sid}:{pid}:*` (read-only) |
| LDA Context Read | Redis 7 — `agent:lda:{sid}:{pid}:*` (read-only) |
| Token State Read | Redis 7 — `orchestrator:token:{sid}:{pid}:active` (read-only) |
| Agent State Write | Redis 7 — `agent:pstpa:{sid}:{pid}:state` |
| Event Bus Write | Apache Kafka — topic: `audio.scoring.packets` (dedicated) |
| Audit Event Bus | Apache Kafka — topic: `audio.agent.events` (anomalies + faults) |
| Audit Log | PostgreSQL 15 — table: `pstpa_token_records` (immutable) |
| Scoring Packets | PostgreSQL 15 — table: `pstpa_scoring_packets` (immutable) |
| Analytics Sink | ClickHouse — table: `session_behavioral_summary` |
| Deployment | Kubernetes pod — namespace: `realtime` |
| Service Name | `phone-stt-processing-agent` |
| Port | Internal only (`:8084`) — no external exposure |
| Image Lock | `ecoskiller/pstpa:v1.0.0` |

**Dedicated Kafka topic:** `audio.scoring.packets`
PSTPA uses a dedicated topic for behavioral packets (separate from `audio.agent.events`)
to ensure Scoring Engine has an isolated, high-priority consumption channel.

---

## XIV. WHAT THE AGENT DOES NOT DO (HARD PROHIBITION)

```
✗ Does NOT transcribe speech — architecturally impossible by design
✗ Does NOT store audio beyond active 3-second processing buffer
✗ Does NOT evaluate communication quality, vocabulary, or grammar
✗ Does NOT assess confidence, personality, or leadership
✗ Does NOT analyze tone, emotion, accent, or prosodic meaning
✗ Does NOT use LLM or generative AI (R28-4)
✗ Does NOT score participants (Scoring Engine only)
✗ Does NOT issue mute or enforcement commands (Orchestrator only)
✗ Does NOT retain inter-token state (buffers flushed after each packet)
✗ Does NOT access participant PII — UUID only
```

---

## XV. POSTGRESQL TABLES (IMMUTABLE)

### 15.1 — Token Records Table

```sql
CREATE TABLE pstpa_token_records (
  id                          BIGSERIAL      PRIMARY KEY,
  session_id                  UUID           NOT NULL,
  participant_id              UUID           NOT NULL,
  token_id                    UUID           NOT NULL,
  token_sequence              SMALLINT,
  round_type                  VARCHAR(20),
  token_duration_seconds      NUMERIC(8,3),
  speaking_duration_confirmed NUMERIC(8,3),
  speaking_ratio              NUMERIC(5,4),
  silence_ratio               NUMERIC(5,4),
  interrupt_attempts          SMALLINT,
  token_start_latency_ms      INTEGER,
  token_end_compliance        BOOLEAN,
  voluntary_yield             BOOLEAN,
  energy_consistency_score    NUMERIC(5,4),
  rms_mean_dbfs               NUMERIC(6,2),
  turn_taken                  BOOLEAN,
  turn_complete               BOOLEAN,
  quality_tier                VARCHAR(10),
  pbnda_flags_active          JSONB,
  adjustments_applied         JSONB,
  scoring_reliability         VARCHAR(30),
  extraction_fault            BOOLEAN        DEFAULT FALSE,
  fallback_method             VARCHAR(60),
  captured_at                 TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE RULE no_update_pstpa_tokens AS
  ON UPDATE TO pstpa_token_records DO INSTEAD NOTHING;

CREATE RULE no_delete_pstpa_tokens AS
  ON DELETE TO pstpa_token_records DO INSTEAD NOTHING;

CREATE INDEX idx_pstpa_session    ON pstpa_token_records (session_id);
CREATE INDEX idx_pstpa_token      ON pstpa_token_records (token_id);
CREATE INDEX idx_pstpa_round      ON pstpa_token_records (round_type);
CREATE INDEX idx_pstpa_timestamp  ON pstpa_token_records (captured_at);
```

### 15.2 — Scoring Packets Table

```sql
CREATE TABLE pstpa_scoring_packets (
  id                    BIGSERIAL     PRIMARY KEY,
  packet_id             UUID          NOT NULL UNIQUE,
  session_id            UUID          NOT NULL,
  participant_id        UUID          NOT NULL,
  packet_type           VARCHAR(40)   NOT NULL,
  schema_version        VARCHAR(10)   NOT NULL DEFAULT '1.0',
  packet_payload        JSONB         NOT NULL,
  scoring_input_ready   BOOLEAN       DEFAULT FALSE,
  scoring_reliability   VARCHAR(30),
  scoring_engine_ack    BOOLEAN       DEFAULT FALSE,
  emitted_at            TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

CREATE RULE no_update_pstpa_packets AS
  ON UPDATE TO pstpa_scoring_packets DO INSTEAD NOTHING;

CREATE RULE no_delete_pstpa_packets AS
  ON DELETE TO pstpa_scoring_packets DO INSTEAD NOTHING;

CREATE INDEX idx_scoring_session  ON pstpa_scoring_packets (session_id);
CREATE INDEX idx_scoring_type     ON pstpa_scoring_packets (packet_type);
CREATE INDEX idx_scoring_ready    ON pstpa_scoring_packets (scoring_input_ready);
```

---

## XVI. CLICKHOUSE ANALYTICS TABLE

```sql
CREATE TABLE session_behavioral_summary (
  session_id                UUID,
  participant_id            UUID,
  session_date              Date,
  session_type              String,
  domain_track              String,
  declared_language         String,
  detected_language         String,
  turns_completed           UInt16,
  turns_skipped             UInt16,
  total_speaking_ms         UInt32,
  avg_speaking_ratio        Float32,
  avg_energy_consistency    Float32,
  interrupt_total           UInt16,
  dominance_ratio           Float32,
  dominance_flag            UInt8,
  overlap_count_total       UInt16,
  network_drop_count        UInt16,
  early_exit_flag           UInt8,
  quality_tier_final        String,
  scoring_reliability_final String,
  adjustments_applied       Array(String)
) ENGINE = MergeTree()
  ORDER BY (session_date, session_type, domain_track, session_id);
```

**Analytics Enabled:**
- GD failure rate per domain track and language
- Speaking time distribution across sessions
- Interrupt and dominance patterns per session type
- Scoring anomaly detection inputs (variance analysis)
- Environment quality impact on behavioral scores
- Compliance rate trends by session type and region

---

## XVII. KUBERNETES DEPLOYMENT SPEC (LOCKED)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: phone-stt-processing-agent
  namespace: realtime
  labels:
    app: phone-stt-processing-agent
    layer: antigravity
    domain: stt-ml
    agent-class: behavioral-signal-extractor
    r28-class: traditional-ml
spec:
  replicas: 3
  selector:
    matchLabels:
      app: phone-stt-processing-agent
  template:
    metadata:
      labels:
        app: phone-stt-processing-agent
    spec:
      containers:
        - name: pstpa
          image: ecoskiller/pstpa:v1.0.0
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
            - name: TRANSCRIPTION_ENABLED
              value: "false"
            - name: AUDIO_RETENTION_POLICY
              value: "ZERO_RETENTION"
            - name: MAX_BUFFER_SECONDS
              value: "3"
          resources:
            requests:
              cpu: "500m"
              memory: "768Mi"
            limits:
              cpu: "1000m"
              memory: "1.5Gi"
          livenessProbe:
            httpGet:
              path: /healthz
              port: 8084
            initialDelaySeconds: 15
            periodSeconds: 15
          readinessProbe:
            httpGet:
              path: /readyz
              port: 8084
            initialDelaySeconds: 10
            periodSeconds: 10
```

**Note:** `replicas: 3` — PSTPA is the highest-load agent (active feature extraction per token).
`AUDIO_RETENTION_POLICY=ZERO_RETENTION` enforced at container level.
`MAX_BUFFER_SECONDS=3` — hard cap on in-memory audio buffer.

---

## XVIII. OBSERVABILITY (NON-OPTIONAL)

All metrics exposed on `:8084/metrics` (Prometheus scrape target).

| Metric | Type | Description |
|---|---|---|
| `pstpa_tokens_processed_total` | Counter (labels: round_type) | Total token windows processed |
| `pstpa_session_summaries_total` | Counter | Total session summaries emitted |
| `pstpa_turn_anomalies_total` | Counter (labels: anomaly_type) | Total anomaly detections |
| `pstpa_extraction_faults_total` | Counter (labels: fault_reason) | Extraction failure events |
| `pstpa_avg_speaking_ratio` | Histogram | Distribution of speaking ratios |
| `pstpa_interrupt_rate` | Histogram | Interrupt attempt distribution |
| `pstpa_scoring_packets_emitted` | Counter | Packets sent to Scoring Engine |
| `pstpa_extraction_latency_ms` | Histogram | Per-token extraction pipeline latency |
| `pstpa_active_sessions` | Gauge | Active sessions under extraction |
| `pstpa_audio_buffer_seconds` | Gauge | Must never exceed 3.0 — security guard |
| `pstpa_transcription_guard` | Gauge | Must always be 0 — security breach if > 0 |

**Grafana Dashboard:** `ecoskiller-antigravity-stt-processing`

**Critical Alert Rules:**
- `pstpa_transcription_guard` > 0 → IMMEDIATE CRITICAL ALERT + session halt
- `pstpa_audio_buffer_seconds` > 3.0 → IMMEDIATE CRITICAL ALERT (retention violation)
- `pstpa_extraction_faults_total` rate > 20/min → Infrastructure alert
- `pstpa_extraction_latency_ms` p95 > 200ms → Performance degradation alert

---

## XIX. SECURITY CONSTRAINTS

```
✔ TRANSCRIPTION_ENABLED=false enforced at container level
✔ AUDIO_RETENTION_POLICY=ZERO_RETENTION enforced at container level
✔ MAX_BUFFER_SECONDS=3 hard cap on in-memory audio buffer
✔ Raw PCM bytes never written to disk, network, or any storage layer
✔ Feature vectors (MFCC, RMS, etc.) discarded after packet assembly
✔ Behavioral packets contain only numeric aggregates — no audio content
✔ Participant UUID only — no name, email, or PII in any record
✔ All inter-service communication via internal Kubernetes network only
✔ No external API calls from agent process
✔ Kafka ACL: PSTPA has WRITE-only on audio.scoring.packets and audio.agent.events
✔ PostgreSQL: PSTPA has INSERT-only on pstpa_token_records and pstpa_scoring_packets
✔ Redis: PSTPA has GET-only on orchestrator:token:*, agent:plmsa:*, agent:pbnda:*, agent:lda:*
✔ Redis: PSTPA has SET/GET/EXPIRE on agent:pstpa:* namespace only
✔ ClickHouse: PSTPA has INSERT-only on session_behavioral_summary
✔ Pod runs as non-root — UID 1000
✔ Inter-token buffer flush is mandatory — verified by audit probe on TOKEN_COMPLETE
```

---

## XX. FAILURE MODES & FALLBACK

| Failure | System Response |
|---|---|
| PSTPA pod crash | Kubernetes restarts (3 replicas — minimal service interruption). Affected tokens recorded as `network_drop`. Session continues. |
| WebRTC VAD engine failure | Energy-threshold fallback activated immediately. `vad_engine_used: energy_threshold` flagged in packet. |
| PLMSA model config unavailable | Default config applied: `whisper-tiny-multi`, TIER_2, no echo compensation. Flagged in packet. |
| PBNDA flags unavailable | Default thresholds used. `adjustments_applied: []` in packet. Session continues. |
| Kafka publish failure (scoring packet) | Retry 3× at 100ms backoff. On failure: direct insert to `pstpa_scoring_packets` PostgreSQL. Scoring Engine polls on fallback. |
| Token extraction timeout | EXTRACTION_FAULT emitted. `silence_assumed` fallback — full token counted as silence (conservative). Participant not penalized — network_drop recorded. |
| Buffer overflow (> 3 seconds) | Oldest frames discarded. Alert emitted. No retention violation — excess data never stored. |

No failure mode may halt an active GD, Dojo, or Interview session.

---

## XXI. ANTIGRAVITY LAYER AGENT REGISTRY (COMPLETE — v1.0.0)

| Agent | Handle | Layer | Port | Version | Scope |
|---|---|---|---|---|---|
| `PHONE_SILENCE_SPAM_AGENT` | PSSG | Audio Processing | 8080 | v1.0.0 | Phone events · silence abuse · spam audio |
| `PHONE_BACKGROUND_NOISE_DETECTION_AGENT` | PBNDA | Audio Processing | 8081 | v1.0.0 | Ambient noise · multi-voice · echo · proximity |
| `LANGUAGE_DETECTION_AGENT` | LDA | STT & ML | 8082 | v1.0.0 | Language ID · mismatch · code-switch detection |
| `PHONE_LANGUAGE_MODEL_SELECTION_AGENT` | PLMSA | STT & ML | 8083 | v1.0.0 | Model routing · cost governance · fallback chain |
| `PHONE_STT_PROCESSING_AGENT` | PSTPA | STT & ML | 8084 | v1.0.0 | Acoustic behavioral extraction · Scoring Engine supply |

---

## XXII. VERSION CONTROL LAW

```
Current Version : v1.0.0
Version Format  : MAJOR.MINOR.PATCH

MAJOR → Breaking change to behavioral packet schema, Scoring Engine contract,
        feature set definition, or Kafka topic structure
MINOR → New feature group added (add-only), new anomaly type registered,
        new environment adjustment rule added
PATCH → Threshold tuning, VAD sensitivity update, bug fix

All version bumps require:
  ✔ Updated schema_version in all packet types
  ✔ Scoring Engine contract re-validation
  ✔ Migration script if PostgreSQL schema changes
  ✔ Updated Helm chart version
  ✔ ClickHouse schema migration if analytics table changes
  ✔ R28 compliance re-declaration if feature extraction method changes
  ✔ Changelog entry with behavioral impact analysis

No silent feature set changes.
No undocumented threshold mutations.
Scoring formula changes require MAJOR version bump.
```

---

## XXIII. DEPLOYMENT GATE CHECKLIST (MANDATORY)

```
[ ] Kafka topic audio.scoring.packets created (dedicated — separate from audio.agent.events)
[ ] Kafka topic audio.agent.events confirmed — producer pstpa-v1 registered
[ ] PostgreSQL pstpa_token_records table created with immutability rules
[ ] PostgreSQL pstpa_scoring_packets table created with immutability rules
[ ] ClickHouse session_behavioral_summary table created
[ ] Redis agent:pstpa:* namespace writeable
[ ] Redis agent:plmsa:*, agent:pbnda:*, agent:lda:*, orchestrator:token:* read-only confirmed
[ ] Kubernetes deployment applied to realtime namespace (replicas: 3)
[ ] Resource limits confirmed: CPU 1000m / Memory 1.5Gi
[ ] TRANSCRIPTION_ENABLED=false confirmed in deployment spec
[ ] AUDIO_RETENTION_POLICY=ZERO_RETENTION confirmed in deployment spec
[ ] MAX_BUFFER_SECONDS=3 confirmed in deployment spec
[ ] Prometheus scrape config updated for PSTPA pod (port 8084)
[ ] CRITICAL: pstpa_transcription_guard alert rule active (must be 0)
[ ] CRITICAL: pstpa_audio_buffer_seconds alert rule active (must be ≤ 3.0)
[ ] Grafana dashboard ecoskiller-antigravity-stt-processing imported
[ ] Scoring Engine updated to consume from audio.scoring.packets topic
[ ] Scoring Engine contract validated against Section IX formula
[ ] Agent Registry Section XXI confirmed as final for v1.0.0
[ ] End-to-end test: full 60s speaking token → verify TOKEN_BEHAVIORAL_PACKET emitted
[ ] End-to-end test: full silence token → verify FULL_SILENCE_TURN anomaly emitted
[ ] End-to-end test: interrupt attempt → verify interrupt_attempts > 0 in packet
[ ] End-to-end test: PBNDA DEGRADED flag → verify RMS threshold adjustment applied
[ ] End-to-end test: open discussion round → verify OPEN_ROUND_BEHAVIORAL_PACKET
[ ] End-to-end test: session end → verify SESSION_SUMMARY_PACKET + SESSION_SUMMARY_READY
[ ] End-to-end test: simulate extraction fault → verify fallback + EXTRACTION_FAULT event
[ ] Security test: confirm zero bytes written to disk at any point during token processing
[ ] Security test: confirm no transcription text in any log, event, packet, or store
[ ] Security test: confirm buffer cleared on TOKEN_COMPLETE (probe inter-token state)
[ ] Scoring Engine integration test: SESSION_SUMMARY_PACKET → correct score output
[ ] Audit records verified in PostgreSQL after test session
[ ] ClickHouse behavioral summary record generated and verified
[ ] R28 compliance audit signed off before production deployment

Absence of any checkbox → STOP DEPLOYMENT → REPORT GATE-INCOMPLETE
```

---

## XXIV. FINAL ENFORCEMENT DECLARATION

```
This agent is the measurement layer of the world's most
deterministic, recruiter-less, AI-less assessment system.

It does not listen to what candidates say.
It does not understand language.
It does not judge quality.

It measures only what the protocol enforces:
  Did they speak?
  For how long?
  Under what constraints?
  With what compliance?

These measurements are numeric.
They are reproducible.
They are auditable.
They are fair — because they are identical for every participant.

The backend is the law.
The agent measures behavior under that law.
The Scoring Engine renders the verdict from measurement alone.

No audio stored. No speech understood. No judgment made.
Only numbers. Only rules. Only fairness.

SEALED · LOCKED · GOVERNED
Version: v1.0.0
```

---

*Part of ECOSKILLER MASTER EXECUTION PROMPT v12.0 — Antigravity STT & ML Layer*
*Full agent chain: PSSG · PBNDA · LDA · PLMSA · PSTPA — all v1.0.0*
*Governed by: R28 Intelligence Cost Optimization Law · Scoring Engine Contract*
*Mutation Policy: Add-only via version bump · Execution Authority: Human declaration only*
