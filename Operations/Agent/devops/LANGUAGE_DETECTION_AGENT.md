# 🔒 LANGUAGE_DETECTION_AGENT
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
Sibling Agents  : PHONE_SILENCE_SPAM_AGENT v1.0.0
                  PHONE_BACKGROUND_NOISE_DETECTION_AGENT v1.0.0
Governed By     : R28 Intelligence Cost Optimization Law
                  Voice GD Orchestrator · Deterministic Protocol Law
                  R20 Accessibility & Localization Law
Domain Scope    : Voice GD · Dojo Matches · Interview Rooms · All Voice Sessions
Intelligence Cost Class : Traditional ML (R28-2 compliant)
```

---

## ⚠️ SEAL DECLARATION

```
This document is sealed.
No clause may be removed.
No logic may be softened.
No behavior is negotiable.
No LLM or generative AI may be used inside this agent.
No human judgment is permitted during live session execution.
All model choices must comply with R28 — Intelligence Cost Optimization Law.

Violations → STOP EXECUTION → REPORT AGENT-INTEGRITY-BREACH
```

---

## I. AGENT IDENTITY

| Property | Value |
|---|---|
| Agent Name | `LANGUAGE_DETECTION_AGENT` |
| Short Handle | `LDA` |
| Agent Class | Real-Time Language Identification Classifier |
| Layer | STT & ML → Antigravity Sublayer |
| Intelligence Class | Traditional ML (R28-2) — Classification model |
| Domain Scope | All voice sessions: GD, Dojo, Interview |
| Execution Mode | Passive Continuous — per speaking token window |
| Decision Authority | NONE — Classification and event emission only |
| Enforcement Authority | Voice GD Orchestrator (exclusive) |
| Transcription | PERMANENTLY PROHIBITED |
| Raw Audio Retention | PERMANENTLY PROHIBITED |
| LLM Inference | PERMANENTLY PROHIBITED (R28-4 violation) |
| Personality / Accent Judgment | PERMANENTLY PROHIBITED |

---

## II. PURPOSE (NON-NEGOTIABLE)

The `LANGUAGE_DETECTION_AGENT` is a **real-time spoken language identification
classifier** operating in the STT & ML sublayer of Ecoskiller's Antigravity layer.

### Problem It Solves

Ecoskiller operates across five domain tracks:

```
Arts | Commerce | Science | Technology | Administration
```

The platform serves a multilingual, multi-regional user base with mandatory
localization support (R20 — Accessibility & Localization Law). Voice sessions
including Group Discussion, Dojo Matches, and Interviews occur in:

```
EXPECTED PRIMARY LANGUAGES:
  en       — English (primary professional medium)
  hi       — Hindi
  hi-en    — Hinglish (Hindi-English code-switching — dominant in Indian GD sessions)
  ta       — Tamil
  te       — Telugu
  mr       — Marathi
  bn       — Bengali
  gu       — Gujarati
  kn       — Kannada
  ml       — Malayalam
  pa       — Punjabi
  ur       — Urdu

INTERNATIONAL SCOPE (future-gated):
  ar       — Arabic
  fr       — French
  de       — German
  es       — Spanish
  zh       — Mandarin Chinese
  pt       — Portuguese
```

Language detection is required to solve four structural problems:

```
PROBLEM 1 — SESSION LANGUAGE COMPLIANCE
  GD batches and Interview sessions are assigned a declared session language.
  Participants who speak in an unauthorized language gain no legitimate
  advantage but introduce unfair signal contamination into shared sessions.
  Detection enables Orchestrator to flag violations without penalizing
  language ability.

PROBLEM 2 — SCORING ENGINE CONTEXT
  Speaking time metrics, silence analysis, and turn compliance are language-
  agnostic. However, the Scoring Engine must know the declared vs spoken
  language to correctly classify silence vs unfamiliar language hesitation.
  Without this flag, a Hindi-speaking candidate in an English session is
  penalized for hesitation that is environmental, not behavioral.

PROBLEM 3 — DOMAIN TRACK LOCALIZATION
  The Arts and Administration domain tracks explicitly support regional
  language sessions. Language detection confirms that the correct session
  language track is active and feeds analytics on regional language usage
  patterns across the platform.

PROBLEM 4 — CODE-SWITCHING DETECTION (HINGLISH)
  Indian professional sessions frequently involve code-switching between
  Hindi and English within a single turn. The agent must classify this as
  a valid language mode (hi-en) rather than mis-flagging as an English
  violation or Hindi violation, which would produce false compliance events.
```

### What This Agent Does

```
CLASS 1 — LANGUAGE_IDENTIFIED
  Primary spoken language confirmed within the speaking window.
  Emitted once per speaking token when stable identification achieved.

CLASS 2 — LANGUAGE_MISMATCH
  Detected language diverges from session-declared language.
  Sustained mismatch beyond tolerance window → event fired.

CLASS 3 — CODE_SWITCH_DETECTED
  Two language codes alternating within a single token window.
  Primarily covers hi-en (Hinglish) but architecture supports all pairs.
  Classified as valid mode — not a violation.

CLASS 4 — LANGUAGE_UNIDENTIFIED
  Insufficient audio energy or too short a window for confident
  identification. Occurs on very short utterances or high-noise environments.

CLASS 5 — LANGUAGE_UNSTABLE
  Identification confidence oscillating below threshold across frames.
  Indicates ambiguous or overlapping language signal.
```

The agent does **not** transcribe speech.
The agent does **not** evaluate vocabulary, grammar, or communication quality.
The agent does **not** penalize language choices.
The agent does **not** store audio.
The agent **only** classifies language identity from acoustic features and emits typed events.

---

## III. R28 COMPLIANCE DECLARATION (MANDATORY)

Per ECOSKILLER MASTER EXECUTION PROMPT v12.0 — R28 Intelligence Cost Optimization Law:

```
Classification        : Structured classification task
Required Model Class  : Traditional ML (R28-2)
                        "Structured prediction, ranking, matching, classification,
                        recommendation, and fraud detection must use traditional
                        ML models."

LLM Permitted         : NO
                        R28-4: "No LLM may be used for tasks solvable by
                        traditional ML or rules."

Approved Model Stack:
  Primary             : Whisper (OpenAI, self-hosted) — acoustic feature extraction ONLY
                        (no transcription output consumed — feature vectors only)
  Language ID Model   : langdetect / lingua-py / fastText LID model (176 languages)
  Code-Switch Detect  : Custom sliding-window dual-language classifier
                        (Logistic Regression / lightweight GBM — R28-2 compliant)

Cost Declaration (R28-5 Mandatory):
  Model type          : Acoustic feature extraction + lightweight LID classifier
  Inference cost/1K   : < $0.002 (self-hosted — compute cost only)
  Monthly cost at MVP : < $15/month at 10,000 sessions/month

Absence of R28 compliance declaration → STOP EXECUTION
```

---

## IV. SYSTEM POSITION (ARCHITECTURE LOCK)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         ECOSKILLER AUDIO PIPELINE                           │
├─────────────────────────────────────────────────────────────────────────────┤
│  WebRTC Transport Layer                                                     │
│    └─ Microphone capture · SRTP encryption · Echo cancellation             │
│         ↓                                                                   │
│  Jitsi SFU (Jitsi Videobridge)                                              │
│    └─ Audio stream routing only · No signal analysis                        │
│         ↓                                                                   │
│  ┌──────────────────────────────────────────────────────────────────┐      │
│  │                     ANTIGRAVITY SUBLAYER                         │      │
│  │                                                                  │      │
│  │  ┌─ NOISE PROCESSING LAYER ──────────────────────────────────┐  │      │
│  │  │  PHONE_SILENCE_SPAM_AGENT (PSSG) v1.0.0                   │  │      │
│  │  │  PHONE_BACKGROUND_NOISE_DETECTION_AGENT (PBNDA) v1.0.0    │  │      │
│  │  └─────────────────────────────────────────────────────────── ┘  │      │
│  │                        ↓ (parallel — not sequential)             │      │
│  │  ┌─ STT & ML LAYER ──────────────────────────────────────────┐  │      │
│  │  │                                                            │  │      │
│  │  │   LANGUAGE_DETECTION_AGENT (LDA)  ◄──────────────────────┤  │      │
│  │  │   · Acoustic feature extraction                           │  │      │
│  │  │   · Language identification (176 languages)               │  │      │
│  │  │   · Code-switch detection (hi-en primary)                 │  │      │
│  │  │   · Session language compliance check                     │  │      │
│  │  │   · Scoring Engine language context flags                 │  │      │
│  │  └──────────────────┬─────────────────────────────────────── ┘  │      │
│  │                     │ typed events                               │      │
│  │                     ▼                                            │      │
│  │  Antigravity Event Aggregator                                    │      │
│  │  (deduplication + session-level merge across all agents)         │      │
│  └─────────────────────┬────────────────────────────────────────── ┘      │
│                         ↓                                                   │
│  Event Bus — Kafka topic: audio.agent.events                               │
│         ↓                                                                   │
│  Voice GD Orchestrator (enforcement via Redis state machine)               │
│  Scoring Engine (language context flags)                                   │
│  Analytics Service → ClickHouse (session language distribution)            │
│  WebSocket Command Channel (advisory messages to participant)              │
│  PostgreSQL Audit Log (immutable)                                           │
└─────────────────────────────────────────────────────────────────────────────┘
```

**Architecture Rules (locked):**
- LDA operates in the STT & ML layer — parallel to, not downstream of, PSSG and PBNDA.
- LDA receives the same audio signal metadata feed as noise agents — not their output.
- LDA does not depend on noise agent results to function.
- LDA output goes through the Antigravity Event Aggregator before Kafka.
- LDA never stores transcription text — feature vectors only, discarded after inference.
- LDA never accesses audio from other participants — single-stream scope only.
- Enforcement belongs exclusively to the Orchestrator.

---

## V. LANGUAGE DETECTION PIPELINE (TECHNICAL LOCK)

### 5.1 — Audio Feature Extraction

Language identification does **not** require transcription.
It operates on **acoustic features** derived from the audio signal.

```
INPUT:  Raw PCM audio stream segment (per speaking token window)
        Sample rate: 44100 Hz (resampled to 16000 Hz for model input)
        Frame size: 25ms frames, 10ms stride

FEATURE EXTRACTION:
  1. Mel-Frequency Cepstral Coefficients (MFCCs)
     - 40 MFCC coefficients per frame
     - Captures phoneme-level acoustic identity

  2. Spectral Features
     - Spectral centroid
     - Spectral rolloff
     - Zero-crossing rate

  3. Prosodic Features
     - Pitch (F0) trajectory
     - Energy envelope
     - Speaking rate estimation

  4. Filter Bank Features (log mel filterbank)
     - 80-channel log mel spectrogram
     - Used as input to fastText LID model

TOOL:   Whisper (self-hosted, tiny/base model)
        → Feature extraction pipeline ONLY
        → Transcription output: DISABLED
        → No text generated, no text stored
        → Only encoder output embeddings consumed

OUTPUT: Float32 feature vector per 3-second analysis window
        Discarded after inference — never persisted
```

### 5.2 — Language Identification Model

```
MODEL:  fastText Language Identification Model
        (Meta AI — open source, self-hosted)
        Supports: 176 languages
        Model file: lid.176.bin (126 MB)
        Inference time: < 5ms per 3-second window

INPUT:  Log mel filterbank features (5.1 output)
OUTPUT: Language probability distribution over 176 language codes
        Top-1 language code + confidence score

CONFIDENCE THRESHOLDS:
  HIGH confidence   : score ≥ 0.85 → LANGUAGE_IDENTIFIED event
  MEDIUM confidence : 0.60 ≤ score < 0.85 → LANGUAGE_IDENTIFIED (flagged: low_confidence)
  LOW confidence    : score < 0.60 → LANGUAGE_UNIDENTIFIED event

MINIMUM WINDOW:
  Identification requires ≥ 2 seconds of active speech energy.
  Windows shorter than 2 seconds → LANGUAGE_UNIDENTIFIED (reason: window_too_short)
```

### 5.3 — Code-Switch Detection (Hinglish / Bilingual)

```
PROBLEM:  Indian professional speech frequently alternates between Hindi (hi)
          and English (en) within a single turn — code-switching.
          A naive LID model fires hi → en → hi → en alternation as false mismatches.

SOLUTION: Sliding dual-window classifier
          - Two parallel 1.5-second windows with 0.5-second overlap
          - If both windows detect different language codes (hi + en)
            with combined confidence ≥ 0.70 → CODE_SWITCH_DETECTED
          - Code-switch is classified as language mode hi-en
          - hi-en is a valid declared session language mode
          - No mismatch event fired for hi-en in English or Hindi sessions

MODEL:    Logistic Regression classifier (R28-2 compliant)
          Features: LID probability pair + transition frequency + energy ratio
          Training: Labeled Hinglish audio corpus (offline — not runtime)
          Inference: < 3ms per window pair

SUPPORTED CODE-SWITCH PAIRS (v1.0.0):
  hi-en   (Hinglish — primary)
  ta-en   (Tamil-English)
  te-en   (Telugu-English)
  mr-en   (Marathi-English)
  bn-en   (Bengali-English)

Future pairs added via MINOR version bump only.
```

### 5.4 — Session Language Compliance Check

```
SESSION LANGUAGE CONTRACT:
  Every GD batch, Dojo Match, and Interview session has a declared language
  stored in PostgreSQL at session creation:
    session.declared_language: "en" | "hi" | "hi-en" | "ta" | ...

  LDA reads the declared language from Redis at session start:
    orchestrator:session:{session_id}:declared_language

COMPLIANCE LOGIC (deterministic rule — not ML):
  IF detected_language == declared_language → compliant
  IF detected_language == hi-en AND declared IN (en, hi) → compliant (code-switch tolerance)
  IF detected_language != declared_language
    AND sustained for ≥ 5 seconds
    AND confidence ≥ 0.75
    → LANGUAGE_MISMATCH event fired

TOLERANCE WINDOW:
  Single-word foreign language intrusions (< 2 seconds) → ignored
  Sustained foreign language (≥ 5 seconds) → event fired
  This prevents false positives from technical terms in regional language sessions
```

---

## VI. DETECTION CLASS SPECIFICATIONS

### 6.1 — CLASS 1: LANGUAGE_IDENTIFIED

Emitted once per speaking token when stable language identification is achieved.

```json
{
  "event": "audio.language.identified",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "LANGUAGE_IDENTIFIED",
  "detected_language": "en",
  "language_name": "English",
  "confidence_score": 0.94,
  "confidence_band": "HIGH | MEDIUM",
  "declared_language": "en",
  "compliance_status": "COMPLIANT | MISMATCH | TOLERANCE",
  "detection_window_seconds": 3.0,
  "analysis_windows_used": 4,
  "code_switch_mode": false,
  "action_required": "scoring_engine_context"
}
```

---

### 6.2 — CLASS 2: LANGUAGE_MISMATCH

Emitted when sustained off-language speaking is detected beyond tolerance.

```json
{
  "event": "audio.language.mismatch",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "LANGUAGE_MISMATCH",
  "detected_language": "hi",
  "language_name": "Hindi",
  "confidence_score": 0.88,
  "declared_language": "en",
  "mismatch_sustained_seconds": 7.5,
  "tolerance_window_seconds": 5.0,
  "active_speaking_token": true,
  "mismatch_count_this_session": 1,
  "action_required": "orchestrator_decision"
}
```

---

### 6.3 — CLASS 3: CODE_SWITCH_DETECTED

Emitted when bilingual code-switching is confirmed within a speaking token.

```json
{
  "event": "audio.language.code_switch_detected",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "CODE_SWITCH_DETECTED",
  "language_pair": "hi-en",
  "primary_language": "hi",
  "secondary_language": "en",
  "switch_frequency_per_minute": 4.2,
  "combined_confidence": 0.81,
  "declared_language": "en",
  "compliance_status": "TOLERANCE",
  "scoring_flag": "CODE_SWITCH_ACTIVE",
  "action_required": "scoring_engine_context"
}
```

---

### 6.4 — CLASS 4: LANGUAGE_UNIDENTIFIED

Emitted when detection window is insufficient for confident identification.

```json
{
  "event": "audio.language.unidentified",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "LANGUAGE_UNIDENTIFIED",
  "reason": "window_too_short | low_speech_energy | high_noise_interference | confidence_below_threshold",
  "audio_duration_seconds": 1.2,
  "max_confidence_reached": 0.44,
  "noise_flag": true,
  "action_required": "log_only"
}
```

---

### 6.5 — CLASS 5: LANGUAGE_UNSTABLE

Emitted when identification oscillates across frames without convergence.

```json
{
  "event": "audio.language.unstable",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "LANGUAGE_UNSTABLE",
  "oscillating_between": ["en", "hi"],
  "frame_count_analyzed": 12,
  "convergence_achieved": false,
  "max_confidence_per_candidate": {"en": 0.62, "hi": 0.59},
  "probable_cause": "code_switch | noise_contamination | very_short_syllables",
  "action_required": "scoring_engine_context"
}
```

---

## VII. AGENT EXECUTION STATES

```
STANDBY
  └─ Process alive
  └─ No assigned session
  └─ Awaiting: session.started event from Event Bus

SESSION_INIT
  └─ Session assigned
  └─ Declared language read from Redis:
       orchestrator:session:{session_id}:declared_language
  └─ Model warm — fastText LID model loaded to memory
  └─ Whisper encoder ready (no decoder loaded — transcription disabled)
  └─ Calibration window baseline from PBNDA checked (noise context)

TOKEN_MONITORING
  └─ Active speaking token assigned to a participant
  └─ LDA processes audio stream in 3-second analysis windows
  └─ Sliding code-switch detector running in parallel
  └─ Events emitted per detection class

IDLE_MONITORING
  └─ No active speaking token
  └─ LDA continues passive detection at reduced frequency (10-second windows)
  └─ Ambient language environment logged — not evaluated for compliance

SESSION_COMPLETE
  └─ Session end event received
  └─ Final language profile summary emitted to ClickHouse
  └─ In-memory feature vectors cleared
  └─ Redis session keys expired

FAULT
  └─ Model load failure or inference timeout
  └─ Emits: audio.agent_fault.lda
  └─ Session continues unaffected
  └─ Scoring Engine receives: language_context_unavailable flag
```

---

## VIII. SCORING ENGINE LANGUAGE CONTEXT CONTRACT

LDA emits language context flags consumed by the Scoring Engine for fair metric
normalization. These flags are informational — they protect candidates, not penalize them.

```
Context Flag 1: LANGUAGE_CONFIRMED
  Source: LANGUAGE_IDENTIFIED (COMPLIANT)
  Effect: Scoring proceeds with standard parameters for declared language.

Context Flag 2: CODE_SWITCH_ACTIVE
  Source: CODE_SWITCH_DETECTED
  Effect: Hesitation pause thresholds extended by +20% for this token.
          Code-switch transitions counted as valid speech activity.

Context Flag 3: LANGUAGE_MISMATCH_ACTIVE
  Source: LANGUAGE_MISMATCH
  Effect: Scoring Engine logs compliance flag. No automatic score deduction.
          Orchestrator decision determines enforcement action.
          Scoring Engine records language_declared vs language_spoken for audit.

Context Flag 4: LANGUAGE_CONTEXT_DEGRADED
  Source: LANGUAGE_UNIDENTIFIED or LANGUAGE_UNSTABLE
  Effect: Energy-based silence metrics used as fallback.
          Hesitation detection tolerance relaxed by +15% for affected window.

Context Flag 5: LANGUAGE_CONTEXT_UNAVAILABLE
  Source: AGENT_FAULT
  Effect: Scoring proceeds with standard parameters.
          language_verified: false recorded in session audit.
```

---

## IX. ORCHESTRATOR ENFORCEMENT CONTRACT

LDA emits events to Kafka. The Voice GD Orchestrator enforces per this table.
No event class produces automatic session termination or score deduction.

| Event Class | Compliance Status | Orchestrator Response |
|---|---|---|
| `LANGUAGE_IDENTIFIED` | COMPLIANT | No action. Scoring Engine context flag only. |
| `LANGUAGE_IDENTIFIED` | TOLERANCE (code-switch) | No action. Scoring Engine notified. |
| `LANGUAGE_MISMATCH` | mismatch_count = 1 | WebSocket advisory toast: "Please speak in the session language." Logged. |
| `LANGUAGE_MISMATCH` | mismatch_count = 2 | WebSocket warning. Admin review flag set. Session continues. |
| `LANGUAGE_MISMATCH` | mismatch_count ≥ 3 | Scoring Engine: language_compliance: FAILED recorded in audit. No forced mute — assessment integrity flag only. |
| `CODE_SWITCH_DETECTED` | TOLERANCE | No action. Scoring Engine context updated. |
| `LANGUAGE_UNIDENTIFIED` | — | Log only. No action. |
| `LANGUAGE_UNSTABLE` | — | Scoring Engine context flag. No participant-facing action. |
| `AGENT_FAULT` | — | Scoring Engine: language_context_unavailable. Session continues. |

**All enforcement is deterministic. No discretionary judgment permitted.**

---

## X. SESSION LANGUAGE REGISTRY (LOCKED)

All valid session language codes must be registered in this table.
Unregistered codes → rejected at session creation.

| Code | Language | Domain Track Usage | Code-Switch Pair |
|---|---|---|---|
| `en` | English | All tracks | hi-en, ta-en, te-en, mr-en, bn-en |
| `hi` | Hindi | All tracks (India) | hi-en |
| `hi-en` | Hinglish | All tracks (India) | — (is itself a pair) |
| `ta` | Tamil | Arts, Commerce, Administration | ta-en |
| `te` | Telugu | Arts, Commerce, Administration | te-en |
| `mr` | Marathi | Arts, Commerce, Administration | mr-en |
| `bn` | Bengali | Arts, Commerce, Administration | bn-en |
| `gu` | Gujarati | Commerce, Administration | — |
| `kn` | Kannada | Arts, Commerce, Administration | — |
| `ml` | Malayalam | Arts, Commerce, Administration | — |
| `pa` | Punjabi | Arts, Administration | — |
| `ur` | Urdu | Arts, Administration | — |

Adding new language codes requires MINOR version bump + fastText model validation.
International codes (ar, fr, de, es, zh, pt) — future-gated, not active in v1.0.0.

---

## XI. TECHNOLOGY BINDING (LOCKED)

| Component | Technology |
|---|---|
| Runtime | Python 3.11 |
| Audio Input | PCM stream metadata from WebRTC AudioWorklet (browser) |
| Server-side Resampling | `librosa.resample` — 44100 Hz → 16000 Hz |
| Feature Extraction | OpenAI Whisper `tiny` model (self-hosted) — encoder only, decoder disabled |
| MFCC Extraction | `librosa.feature.mfcc` — 40 coefficients, 25ms frames |
| Log Mel Filterbank | `librosa.feature.melspectrogram` — 80 channels |
| Language ID Model | fastText LID `lid.176.bin` — Meta AI (self-hosted, open source) |
| Code-Switch Classifier | Logistic Regression (`scikit-learn`) — sliding dual-window |
| Compliance Rule Engine | Python deterministic rule engine (R28-1 compliant) |
| Session State Read | Redis 7 — `orchestrator:session:{id}:declared_language` (read-only) |
| Agent State Write | Redis 7 — `agent:lda:{session_id}:{participant_id}:state` |
| Event Bus Write | Apache Kafka — topic: `audio.agent.events` |
| Audit Log | PostgreSQL 15 — table: `audio_language_detection_events` |
| Analytics Sink | ClickHouse — table: `session_language_profile` |
| Model Storage | MinIO — bucket: `ecoskiller-ml-models` |
| Deployment | Kubernetes pod — namespace: `realtime` |
| Service Name | `language-detection-agent` |
| Port | Internal only (`:8082`) — no external exposure |
| Image Lock | `ecoskiller/lda:v1.0.0` |

---

## XII. WHAT THE AGENT DOES NOT DO (HARD PROHIBITION)

```
✗ Does NOT transcribe speech — ever, under any condition
✗ Does NOT store audio samples beyond the active processing frame
✗ Does NOT store transcription text — even temporarily
✗ Does NOT evaluate communication quality, vocabulary, or grammar
✗ Does NOT identify individuals from voice characteristics
✗ Does NOT judge language proficiency or accent
✗ Does NOT use LLM or generative AI models (R28-4 violation)
✗ Does NOT apply score deductions (Scoring Engine only)
✗ Does NOT issue mute commands (Orchestrator only)
✗ Does NOT access audio streams of other participants
✗ Does NOT communicate directly with frontend
✗ Does NOT access participant PII — UUID only
✗ Does NOT flag regional language sessions as violations
✗ Does NOT distinguish accents within the same language code
```

Violation of any prohibition above → `STOP EXECUTION` → `REPORT AGENT-INTEGRITY-BREACH`

---

## XIII. KAFKA EVENT SCHEMA (LOCKED)

**Topic:** `audio.agent.events`
**Partition Key:** `session_id`
**Producer:** `lda-v1`
**Retention:** 7 days
**Schema Version:** `1.0`

```json
{
  "schema_version": "1.0",
  "producer": "lda-v1",
  "event": "<string>",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "LANGUAGE_IDENTIFIED | LANGUAGE_MISMATCH | CODE_SWITCH_DETECTED | LANGUAGE_UNIDENTIFIED | LANGUAGE_UNSTABLE | AGENT_FAULT",
  "detected_language": "<ISO 639-1 code | null>",
  "declared_language": "<ISO 639-1 code>",
  "confidence_score": "<float | null>",
  "compliance_status": "COMPLIANT | MISMATCH | TOLERANCE | null",
  "scoring_flag": "<string | null>",
  "active_speaking_token": "<boolean>",
  "metadata": "<object — class-specific fields>",
  "action_required": "orchestrator_decision | scoring_engine_context | log_only"
}
```

Schema changes require version bump. Breaking changes → `STOP EXECUTION`.

---

## XIV. POSTGRESQL AUDIT TABLE (IMMUTABLE)

```sql
CREATE TABLE audio_language_detection_events (
  id                      BIGSERIAL     PRIMARY KEY,
  session_id              UUID          NOT NULL,
  participant_id          UUID          NOT NULL,
  event_type              VARCHAR(80)   NOT NULL,
  signal_class            VARCHAR(60)   NOT NULL,
  detected_language       VARCHAR(10),
  declared_language       VARCHAR(10),
  confidence_score        NUMERIC(5,4),
  compliance_status       VARCHAR(20),
  scoring_flag            VARCHAR(60),
  active_speaking_token   BOOLEAN       DEFAULT FALSE,
  code_switch_pair        VARCHAR(10),
  mismatch_count          SMALLINT,
  metadata                JSONB,
  detected_at             TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
  orchestrator_ack        BOOLEAN       DEFAULT FALSE,
  scoring_engine_ack      BOOLEAN       DEFAULT FALSE
);

-- Immutability enforcement
CREATE RULE no_update_lang_events AS
  ON UPDATE TO audio_language_detection_events DO INSTEAD NOTHING;

CREATE RULE no_delete_lang_events AS
  ON DELETE TO audio_language_detection_events DO INSTEAD NOTHING;

-- Query indexes
CREATE INDEX idx_lang_session    ON audio_language_detection_events (session_id);
CREATE INDEX idx_lang_class      ON audio_language_detection_events (signal_class);
CREATE INDEX idx_lang_detected   ON audio_language_detection_events (detected_language);
CREATE INDEX idx_lang_compliance ON audio_language_detection_events (compliance_status);
CREATE INDEX idx_lang_timestamp  ON audio_language_detection_events (detected_at);
```

---

## XV. CLICKHOUSE ANALYTICS TABLE

Session-level language profile rolled up for platform analytics.

```sql
CREATE TABLE session_language_profile (
  session_id               UUID,
  participant_id           UUID,
  session_date             Date,
  domain_track             String,
  declared_language        String,
  primary_detected_lang    String,
  language_compliant       UInt8,
  mismatch_events          UInt16,
  code_switch_events       UInt16,
  code_switch_pair         String,
  unidentified_events      UInt16,
  avg_confidence_score     Float32,
  language_verified        UInt8,
  scoring_flag_final       String
) ENGINE = MergeTree()
  ORDER BY (session_date, domain_track, declared_language, session_id);
```

**Platform-Level Analytics Enabled:**
- Language distribution per domain track
- Regional language adoption rates across GD sessions
- Code-switching frequency by geography
- Mismatch rates by session language
- Confidence score distribution by language pair

---

## XVI. KUBERNETES DEPLOYMENT SPEC (LOCKED)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: language-detection-agent
  namespace: realtime
  labels:
    app: language-detection-agent
    layer: antigravity
    domain: stt-ml
    agent-class: language-classifier
    r28-class: traditional-ml
spec:
  replicas: 2
  selector:
    matchLabels:
      app: language-detection-agent
  template:
    metadata:
      labels:
        app: language-detection-agent
    spec:
      containers:
        - name: lda
          image: ecoskiller/lda:v1.0.0
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
            - name: MINIO_URL
              valueFrom:
                secretKeyRef:
                  name: minio-secrets
                  key: url
            - name: MODEL_BUCKET
              value: "ecoskiller-ml-models"
            - name: WHISPER_MODEL
              value: "tiny"
            - name: FASTTEXT_MODEL
              value: "lid.176.bin"
            - name: TRANSCRIPTION_ENABLED
              value: "false"
          resources:
            requests:
              cpu: "400m"
              memory: "512Mi"
            limits:
              cpu: "800m"
              memory: "1Gi"
          livenessProbe:
            httpGet:
              path: /healthz
              port: 8082
            initialDelaySeconds: 15
            periodSeconds: 20
          readinessProbe:
            httpGet:
              path: /readyz
              port: 8082
            initialDelaySeconds: 10
            periodSeconds: 10
```

**Note:** Higher CPU/memory than noise agents due to Whisper encoder inference.
`TRANSCRIPTION_ENABLED=false` is a mandatory environment variable.
Absence of this variable → pod startup blocked by init container check.

---

## XVII. MODEL GOVERNANCE (LOCKED)

Per ECOSKILLER Master Execution Prompt — Model Registry Service requirement:

```
Model 1: Whisper tiny (encoder only)
  Version          : openai/whisper-tiny
  Usage            : Acoustic feature extraction — encoder output only
  Transcription    : Disabled (decoder not loaded)
  Storage          : MinIO bucket: ecoskiller-ml-models/whisper/
  Registry entry   : model_registry table: whisper-tiny-encoder-v1
  Retraining       : NOT APPLICABLE (pre-trained, self-hosted)
  License          : MIT (open source — self-hosted permitted)

Model 2: fastText LID lid.176.bin
  Version          : cc.176.bin (fastText language identification)
  Usage            : Language probability distribution — 176 languages
  Storage          : MinIO bucket: ecoskiller-ml-models/fasttext/
  Registry entry   : model_registry table: fasttext-lid-176-v1
  Retraining       : NOT APPLICABLE (pre-trained, self-hosted)
  License          : CC-BY-SA 3.0 (self-hosted permitted)

Model 3: Code-Switch Logistic Regression Classifier
  Version          : ecoskiller-csclf-v1.0.0
  Usage            : Hinglish + bilingual code-switch detection
  Storage          : MinIO bucket: ecoskiller-ml-models/csclf/
  Registry entry   : model_registry table: codesw-clf-v1
  Training Data    : Labeled Hinglish audio corpus (offline)
  Retraining       : Triggered by AI/ML retraining trigger panel (human-initiated)
  License          : Internal (Ecoskiller proprietary)
```

All model updates require Model Registry version bump + human execution log.
No silent model swaps permitted.

---

## XVIII. OBSERVABILITY (NON-OPTIONAL)

All metrics exposed on `:8082/metrics` (Prometheus scrape target).

| Metric | Type | Description |
|---|---|---|
| `lda_language_identified_total` | Counter | Total successful identifications |
| `lda_language_mismatch_total` | Counter | Total mismatch events |
| `lda_code_switch_total` | Counter | Total code-switch detections |
| `lda_unidentified_total` | Counter | Total unidentified windows |
| `lda_agent_fault_total` | Counter | Model/inference fault events |
| `lda_inference_latency_ms` | Histogram | End-to-end inference latency per window |
| `lda_confidence_score_dist` | Histogram | Confidence score distribution |
| `lda_active_sessions` | Gauge | Active monitored sessions |
| `lda_language_distribution` | Counter (labels: lang) | Detected language frequency |
| `lda_transcription_enabled` | Gauge | Must always be 0 (transcription disabled guard) |

**Grafana Dashboard:** `ecoskiller-antigravity-stt-ml`
**Critical Alert Rules:**
- `lda_transcription_enabled` > 0 → IMMEDIATE CRITICAL ALERT (security breach)
- `lda_agent_fault_total` > 0 → Immediate ops notification
- `lda_inference_latency_ms` p95 > 500ms → Performance alert

---

## XIX. SECURITY CONSTRAINTS

```
✔ Agent receives PCM audio metadata — processed in-memory only
✔ Raw audio bytes never written to disk or any storage layer
✔ Feature vectors (MFCC, mel spectrogram) discarded immediately after inference
✔ No transcription text generated, stored, or transmitted — ever
✔ Participant UUID only — no name, email, or PII in agent scope
✔ Whisper decoder not loaded — transcription architecturally disabled
✔ TRANSCRIPTION_ENABLED=false enforced at container level
✔ All inter-service communication via internal Kubernetes cluster network only
✔ No external API calls from agent process
✔ Kafka ACL: LDA has WRITE-only on audio.agent.events
✔ PostgreSQL: LDA has INSERT-only on audio_language_detection_events
✔ Redis: LDA has GET-only on orchestrator:session:* (declared language read)
✔ Redis: LDA has SET/GET/EXPIRE on agent:lda:* namespace only
✔ ClickHouse: LDA has INSERT-only on session_language_profile
✔ MinIO: LDA has GET-only on ecoskiller-ml-models bucket (model loading only)
✔ Pod runs as non-root — UID 1000
✔ No cross-participant stream access permitted
```

---

## XX. FAILURE MODES & FALLBACK

| Failure | System Response |
|---|---|
| LDA pod crash | Kubernetes restarts. Session continues. Scoring Engine receives `language_context_unavailable`. |
| Whisper model load failure | fastText direct spectral input fallback used. `model_fallback: true` flagged in events. |
| fastText inference timeout (>100ms) | Window skipped. `LANGUAGE_UNIDENTIFIED` (reason: inference_timeout) emitted. |
| Redis declared_language unavailable | Default declared_language: `en` assumed. `declared_language_source: default` flagged. |
| Kafka publish failure | Retry 3× at 100ms backoff. On failure: direct PostgreSQL write. ClickHouse deferred. |
| High noise interference (PBNDA flag) | LDA raises unidentified_threshold by +10% for affected participant. |

No failure mode may halt an active GD, Dojo, or Interview session.
Graceful degradation is mandatory.

---

## XXI. ANTIGRAVITY LAYER AGENT REGISTRY (UPDATED)

| Agent | Handle | Layer | Version | Scope |
|---|---|---|---|---|
| `PHONE_SILENCE_SPAM_AGENT` | PSSG | Audio Processing | v1.0.0 | Phone events, silence abuse, spam audio |
| `PHONE_BACKGROUND_NOISE_DETECTION_AGENT` | PBNDA | Audio Processing | v1.0.0 | Ambient noise, transients, multi-voice, echo, proximity |
| `LANGUAGE_DETECTION_AGENT` | LDA | STT & ML | v1.0.0 | Language ID, mismatch, code-switch, compliance |

Future agents added to this layer must register here via version bump.
Unregistered agents → `STOP EXECUTION`.

---

## XXII. VERSION CONTROL LAW

```
Current Version : v1.0.0
Version Format  : MAJOR.MINOR.PATCH

MAJOR → Breaking change to event schema, language compliance contract,
        model architecture, or Orchestrator enforcement table
MINOR → New language code registered, new code-switch pair added,
        new scoring context flag added (add-only)
PATCH → Threshold tuning, confidence band adjustment, bug fix

All version bumps require:
  ✔ Updated schema_version in Kafka payload
  ✔ Migration script if PostgreSQL schema changes
  ✔ Updated Model Registry entry if model version changes
  ✔ Updated Helm chart version
  ✔ ClickHouse schema migration if analytics table changes
  ✔ R28 compliance re-declaration if model class changes
  ✔ Changelog entry with rationale

No silent mutations.
No silent model upgrades.
No undocumented language additions.
```

---

## XXIII. DEPLOYMENT GATE CHECKLIST (MANDATORY)

```
[ ] Kafka topic audio.agent.events confirmed — producer lda-v1 registered
[ ] PostgreSQL audio_language_detection_events table created with immutability rules
[ ] ClickHouse session_language_profile table created
[ ] Redis orchestrator:session:* read-only access confirmed for LDA
[ ] Redis agent:lda:* namespace writeable
[ ] MinIO bucket ecoskiller-ml-models accessible — models present:
    [ ] whisper/tiny model files
    [ ] fasttext/lid.176.bin
    [ ] csclf/codesw-clf-v1.pkl (code-switch classifier)
[ ] Model Registry entries created for all 3 models
[ ] TRANSCRIPTION_ENABLED=false confirmed in deployment spec
[ ] Kubernetes deployment applied to realtime namespace
[ ] Resource limits confirmed: CPU 800m / Memory 1Gi
[ ] Prometheus scrape config updated for LDA pod (port 8082)
[ ] CRITICAL: lda_transcription_enabled alert rule active in Prometheus
[ ] Grafana dashboard ecoskiller-antigravity-stt-ml imported
[ ] Voice GD Orchestrator updated to consume LDA language mismatch events
[ ] Scoring Engine updated to consume LDA scoring context flags
[ ] Agent Registry in Section XXI updated
[ ] Session Language Registry in Section X loaded into PostgreSQL
[ ] End-to-end test: English audio → verify LANGUAGE_IDENTIFIED (en, COMPLIANT)
[ ] End-to-end test: Hindi audio in English session → verify LANGUAGE_MISMATCH
[ ] End-to-end test: Hinglish audio → verify CODE_SWITCH_DETECTED (hi-en, TOLERANCE)
[ ] End-to-end test: 1-second utterance → verify LANGUAGE_UNIDENTIFIED (window_too_short)
[ ] End-to-end test: confirm zero bytes written to disk during inference
[ ] End-to-end test: confirm no transcription text in any log, event, or store
[ ] Audit log entries verified in PostgreSQL after each test class
[ ] ClickHouse session_language_profile record generated after test session
[ ] R28 compliance audit signed off before production deployment

Absence of any checkbox → STOP DEPLOYMENT → REPORT GATE-INCOMPLETE
```

---

## XXIV. FINAL ENFORCEMENT DECLARATION

```
Language is not a performance metric.
Language is a session parameter.

This agent does not judge how well a candidate speaks.
It confirms which language they are speaking in,
and whether it matches the declared session contract.

It protects candidates who code-switch legitimately.
It flags candidates who breach the session language contract.
It never penalizes — only informs.

Transcription is permanently and architecturally disabled.
Audio is never stored.
Identity is never inferred from voice.

The backend is the law.
The agent reads the language signal.
The Orchestrator enforces the contract.
The Scoring Engine receives fair context.

R28 is enforced. Traditional ML only.
No LLM. No generative AI. No neural transcription.

SEALED · LOCKED · GOVERNED
Version: v1.0.0
```

---

*Part of ECOSKILLER MASTER EXECUTION PROMPT v12.0 — Antigravity STT & ML Layer*
*Siblings: PHONE_SILENCE_SPAM_AGENT v1.0.0 · PHONE_BACKGROUND_NOISE_DETECTION_AGENT v1.0.0*
*Governed by: R28 Intelligence Cost Optimization Law · R20 Accessibility & Localization Law*
*Mutation Policy: Add-only via version bump · Execution Authority: Human declaration only*
