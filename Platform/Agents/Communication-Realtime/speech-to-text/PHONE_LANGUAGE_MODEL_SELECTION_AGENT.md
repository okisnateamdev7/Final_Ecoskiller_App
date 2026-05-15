# 🔒 PHONE_LANGUAGE_MODEL_SELECTION_AGENT
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

Sibling Agents (Antigravity Layer):
  PHONE_SILENCE_SPAM_AGENT               (PSSG)  v1.0.0  Audio Processing
  PHONE_BACKGROUND_NOISE_DETECTION_AGENT (PBNDA) v1.0.0  Audio Processing
  LANGUAGE_DETECTION_AGENT               (LDA)   v1.0.0  STT & ML

Governed By:
  R28 — Intelligence Cost Optimization Law
  Voice GD Orchestrator · Deterministic Protocol Law
  R20 — Accessibility & Localization Law
  Model Registry Service Law

Domain Scope:
  Voice GD · Dojo Matches · Interview Rooms · All Voice Sessions

Intelligence Cost Class : Rule Engine + Traditional ML (R28-1 + R28-2 compliant)
Agent Role              : Upstream Selector — feeds all STT/ML consumers
```

---

## ⚠️ SEAL DECLARATION

```
This document is sealed.
No clause may be removed.
No logic may be softened.
No behavior is negotiable.
No LLM or generative AI may power selection decisions.
No model may be loaded, swapped, or activated without this agent's selection signal.
No human judgment is permitted during live session execution.
All model choices must comply with R28 — Intelligence Cost Optimization Law.

Violations → STOP EXECUTION → REPORT AGENT-INTEGRITY-BREACH
```

---

## I. AGENT IDENTITY

| Property | Value |
|---|---|
| Agent Name | `PHONE_LANGUAGE_MODEL_SELECTION_AGENT` |
| Short Handle | `PLMSA` |
| Agent Class | Intelligent Model Router & Selector |
| Layer | STT & ML → Antigravity Sublayer |
| Intelligence Class | Rule Engine (R28-1) for routing logic + Traditional ML (R28-2) for quality scoring |
| Domain Scope | All voice sessions on Ecoskiller platform |
| Execution Mode | Pre-session + Per-token adaptive — continuous re-evaluation |
| Decision Authority | Model selection only — no enforcement, no scoring |
| Enforcement Authority | Voice GD Orchestrator (exclusive) |
| Transcription | PERMANENTLY PROHIBITED |
| Raw Audio Retention | PERMANENTLY PROHIBITED |
| LLM Inference | PERMANENTLY PROHIBITED (R28-4) |
| Model Self-Selection | FORBIDDEN — only operates from registered model catalogue |

---

## II. PURPOSE (NON-NEGOTIABLE)

The `PHONE_LANGUAGE_MODEL_SELECTION_AGENT` is the **real-time acoustic model router**
of Ecoskiller's Antigravity STT & ML sublayer.

### The Problem It Solves

The Antigravity layer processes voice streams across a deeply heterogeneous input space:

```
AXIS 1 — LANGUAGE DIVERSITY
  12 active Indian languages + Hinglish code-switching
  Future: 6 international languages
  Each language has optimal acoustic model characteristics

AXIS 2 — AUDIO QUALITY DIVERSITY
  CLEAN     : SNR > 20 dB, no noise events (PBNDA: CLEAN)
  ACCEPTABLE: SNR 10–20 dB, minor ambient (PBNDA: ACCEPTABLE)
  DEGRADED  : SNR 5–10 dB, noise active (PBNDA: DEGRADED)
  COMPROMISED: SNR < 5 dB, multi-voice, echo (PBNDA: COMPROMISED)

AXIS 3 — DEVICE DIVERSITY
  Mobile phone mic (dominant — Indian market)
  Laptop internal mic
  Headset mic (best quality)
  External Bluetooth mic
  Far-field device (tablet on desk)

AXIS 4 — NETWORK & BANDWIDTH DIVERSITY
  High bandwidth (WiFi, 5G) — full model
  Medium bandwidth (4G) — compressed audio, needs adapted model
  Low bandwidth (3G, poor 4G) — degraded signal, needs robust model

AXIS 5 — SESSION TYPE DIVERSITY
  Voice GD (6 participants, turn-based, 60–120 sec tokens)
  Dojo Match (2 participants, structured debate)
  Interview (1:1, longer continuous speech)
  Dojo Arts (regional language dominant sessions)
```

A single STT/ML model applied uniformly across this input space produces:

```
PROBLEM 1 — COST WASTE
  Running Whisper medium on a clean English GD session
  wastes 4× the compute of Whisper tiny with identical output quality.
  R28-5 mandates per-1,000-request cost declaration.
  Uniform over-provisioning violates R28.

PROBLEM 2 — QUALITY FAILURE
  Running Whisper tiny on a Hinglish session with HIGH ambient noise
  produces unreliable acoustic features for downstream LDA.
  Under-provisioning on degraded audio corrupts the entire
  STT & ML layer's output for that session.

PROBLEM 3 — LANGUAGE MISMATCH
  Models optimised for English perform poorly on Tamil or Marathi phonemes.
  A Marathi session routed to an English-tuned model degrades
  language detection confidence, silence thresholds, and
  all downstream scoring context flags.

PROBLEM 4 — DEVICE SIGNATURE BLINDNESS
  Far-field device audio (PBNDA: DEVICE_PROXIMITY_VIOLATION) requires
  a different frequency weighting profile than near-field headset audio.
  Without device-aware model selection, acoustic feature extraction
  systematically underperforms for a large segment of Indian users
  on mobile devices without headsets.
```

### What This Agent Does

```
FUNCTION 1 — PRE-SESSION MODEL ASSIGNMENT
  Before the first speaking token, PLMSA evaluates all available
  session context signals and assigns the optimal model configuration
  to each participant for the full session.

FUNCTION 2 — PER-TOKEN ADAPTIVE RE-EVALUATION
  At the start of each speaking token, PLMSA re-evaluates current
  audio quality signals from PBNDA and adjusts model selection
  if the environment quality has changed materially.

FUNCTION 3 — MODEL CATALOGUE GOVERNANCE
  PLMSA is the single source of truth for which models are active,
  available, deprecated, or canary-deployed.
  No agent or service may invoke a model not registered in PLMSA's catalogue.

FUNCTION 4 — COST METERING
  PLMSA tracks per-session, per-participant model usage and
  emits cost telemetry to ClickHouse for R28-5 compliance reporting.

FUNCTION 5 — FALLBACK CHAIN MANAGEMENT
  PLMSA maintains ordered fallback chains for every selection slot.
  If a primary model is unavailable, PLMSA activates the next
  fallback without requiring Orchestrator intervention.
```

---

## III. R28 COMPLIANCE DECLARATION (MANDATORY)

Per ECOSKILLER MASTER EXECUTION PROMPT v12.0 — R28 Intelligence Cost Optimization Law:

```
ROUTING LOGIC Classification:
  Type            : Deterministic rule engine (R28-1)
  Rationale       : Model routing is a rule-based decision tree —
                    not a prediction task. Rules are auditable,
                    reproducible, and zero-inference-cost.
  LLM Permitted   : NO (R28-4)

QUALITY SCORING Classification:
  Type            : Traditional ML — Gradient Boosted Classifier (R28-2)
  Rationale       : Audio quality tier prediction from PBNDA signals
                    is a structured classification task. GBM is
                    optimal for tabular signal features.
  LLM Permitted   : NO (R28-4)

Cost Declaration (R28-5 Mandatory):
  Routing engine  : $0.000 per request (pure rule evaluation)
  GBM quality clf : < $0.0001 per request (self-hosted, CPU inference)
  Model load      : One-time per session (not per token)
  
  Monthly cost at MVP (10,000 sessions/month):
    PLMSA itself  : < $2/month
    Model routing overhead: negligible vs. model inference costs saved
    Estimated savings vs. uniform Whisper medium: ~$120/month at MVP

Absence of R28 compliance declaration → STOP EXECUTION
```

---

## IV. SYSTEM POSITION (ARCHITECTURE LOCK)

```
┌───────────────────────────────────────────────────────────────────────────────┐
│                        ECOSKILLER AUDIO PIPELINE                              │
├───────────────────────────────────────────────────────────────────────────────┤
│  WebRTC Transport Layer · Jitsi SFU                                           │
│         ↓                                                                     │
│  ┌──────────────────────────────────────────────────────────────────────┐    │
│  │                       ANTIGRAVITY SUBLAYER                           │    │
│  │                                                                      │    │
│  │  ┌─ AUDIO PROCESSING LAYER ──────────────────────────────────────┐  │    │
│  │  │  PSSG  — Phone · Silence · Spam detection                     │  │    │
│  │  │  PBNDA — Background Noise · Echo · Multi-voice · Proximity    │  │    │
│  │  │          │                                                     │  │    │
│  │  │          │ environment_quality_flags                           │  │    │
│  │  │          │ noise_class · snr_db · proximity_flag              │  │    │
│  │  └──────────┼────────────────────────────────────────────────── ─┘  │    │
│  │             │                                                        │    │
│  │             ▼                                                        │    │
│  │  ┌─ STT & ML LAYER ──────────────────────────────────────────────┐  │    │
│  │  │                                                                │  │    │
│  │  │  ┌───────────────────────────────────────────────────────┐   │  │    │
│  │  │  │   PHONE_LANGUAGE_MODEL_SELECTION_AGENT (PLMSA)  ◄─────┤   │  │    │
│  │  │  │                                                       │   │  │    │
│  │  │  │   INPUTS:                                             │   │  │    │
│  │  │  │     · Session metadata (language, type, domain)       │   │  │    │
│  │  │  │     · PBNDA environment flags (live)                  │   │  │    │
│  │  │  │     · LDA detected language (live — per token)        │   │  │    │
│  │  │  │     · Device signature (proximity flag)               │   │  │    │
│  │  │  │     · Network quality signal (bandwidth tier)         │   │  │    │
│  │  │  │                                                       │   │  │    │
│  │  │  │   OUTPUTS:                                            │   │  │    │
│  │  │  │     · Model selection config → LDA                    │   │  │    │
│  │  │  │     · Model selection config → Future STT consumers   │   │  │    │
│  │  │  │     · Cost telemetry → ClickHouse                     │   │  │    │
│  │  │  │     · Model selection events → Kafka                  │   │  │    │
│  │  │  └───────────────────────────────────────────────────────┘   │  │    │
│  │  │                                                                │  │    │
│  │  │   LANGUAGE_DETECTION_AGENT (LDA)                              │  │    │
│  │  │     └─ Receives: model_config from PLMSA                      │  │    │
│  │  │     └─ Uses: PLMSA-selected models for all inference          │  │    │
│  │  └────────────────────────────────────────────────────────────── ┘  │    │
│  │                                                                      │    │
│  │  Antigravity Event Aggregator → Kafka: audio.agent.events           │    │
│  └──────────────────────────────────────────────────────────────────── ┘    │
│                                                                               │
│  Voice GD Orchestrator · Scoring Engine · ClickHouse · PostgreSQL            │
└───────────────────────────────────────────────────────────────────────────────┘
```

**Architecture Rules (locked):**
- PLMSA is upstream of LDA — LDA waits for PLMSA's model config before processing any token.
- PLMSA reads from PBNDA events and LDA events — it does not produce audio analysis itself.
- PLMSA never invokes models directly — it selects and signals. Invocation belongs to consumers.
- PLMSA is the exclusive model catalogue authority — no agent bypasses it.
- PLMSA runs per-session AND per-token — initial assignment + adaptive updates.

---

## V. MODEL CATALOGUE (LOCKED)

All models available for selection by PLMSA. No unlisted model may be activated.

### 5.1 — Acoustic Feature Extraction Models (Whisper Family)

| Model ID | Base Model | Size | Languages | Best For | Inference Time | Cost/1K |
|---|---|---|---|---|---|---|
| `whisper-tiny-en` | Whisper tiny.en | 39M | en only | Clean English, low cost | ~12ms | ~$0.001 |
| `whisper-tiny-multi` | Whisper tiny | 39M | 99 languages | Clean multilingual | ~15ms | ~$0.001 |
| `whisper-base-multi` | Whisper base | 74M | 99 languages | Acceptable quality, multilingual | ~25ms | ~$0.002 |
| `whisper-small-multi` | Whisper small | 244M | 99 languages | Degraded audio, multilingual | ~60ms | ~$0.005 |
| `whisper-medium-multi` | Whisper medium | 769M | 99 languages | Compromised audio, high accuracy | ~150ms | ~$0.012 |
| `whisper-large-v3` | Whisper large-v3 | 1550M | 99 languages | Reserved: critical failure recovery only | ~350ms | ~$0.028 |

**Deployment constraint:**
- `whisper-large-v3` is loaded on demand only — not resident in memory.
- All other models warm-loaded at namespace start.

### 5.2 — Language Identification Models

| Model ID | Base Model | Size | Languages | Best For | Inference Time | Cost/1K |
|---|---|---|---|---|---|---|
| `fasttext-lid-176` | fastText lid.176.bin | 126MB | 176 languages | Standard multilingual ID | ~5ms | ~$0.0001 |
| `fasttext-lid-compressed` | fastText lid.176.ftz | 917KB | 176 languages | Low-memory fallback | ~8ms | ~$0.0001 |
| `lingua-hi-en` | Lingua (custom) | 18MB | hi, en, hi-en | Hinglish specialist | ~4ms | ~$0.0001 |
| `lingua-south-indian` | Lingua (custom) | 32MB | ta, te, kn, ml | South Indian language cluster | ~4ms | ~$0.0001 |
| `lingua-indo-aryan` | Lingua (custom) | 28MB | mr, bn, gu, pa, ur | Indo-Aryan cluster | ~4ms | ~$0.0001 |

### 5.3 — Code-Switch Detection Models

| Model ID | Base Model | Pairs | Inference Time | Cost/1K |
|---|---|---|---|---|
| `csclf-hi-en-v1` | Logistic Regression | hi-en | ~3ms | ~$0.00005 |
| `csclf-south-v1` | Logistic Regression | ta-en, te-en, kn-en, ml-en | ~3ms | ~$0.00005 |
| `csclf-indo-aryan-v1` | Logistic Regression | mr-en, bn-en, gu-en, pa-en | ~3ms | ~$0.00005 |
| `csclf-universal-v1` | Gradient Boosted (GBM) | All registered pairs | ~8ms | ~$0.0001 |

---

## VI. SELECTION SIGNAL INPUTS (LOCKED)

PLMSA consumes these signals to make selection decisions.
All signals are read from Redis or derived from session metadata — never from raw audio.

### 6.1 — Static Session Signals (Read at session start — from PostgreSQL/Redis)

| Signal | Source | Redis Key | Used For |
|---|---|---|---|
| `declared_language` | Session creation | `orchestrator:session:{id}:declared_language` | LID model cluster selection |
| `session_type` | Session creation | `orchestrator:session:{id}:type` | Token duration, model tier baseline |
| `domain_track` | Session creation | `orchestrator:session:{id}:domain_track` | Regional language model preference |
| `participant_count` | Session creation | `orchestrator:session:{id}:participant_count` | Per-participant resource budgeting |

### 6.2 — Dynamic Audio Quality Signals (Read per token — from PBNDA Redis state)

| Signal | Source Redis Key | Values | Impact on Selection |
|---|---|---|---|
| `environment_quality` | `agent:pbnda:{sid}:{pid}:quality` | CLEAN / ACCEPTABLE / DEGRADED / COMPROMISED | Primary model tier selector |
| `snr_db` | `agent:pbnda:{sid}:{pid}:snr` | Float (dB) | Fine-grained tier boundary adjustment |
| `proximity_flag` | `agent:pbnda:{sid}:{pid}:proximity` | true / false | Frequency weighting profile selection |
| `echo_flag` | `agent:pbnda:{sid}:{pid}:echo` | true / false | RT60-compensated model config |
| `noise_class` | `agent:pbnda:{sid}:{pid}:noise_class` | AMBIENT / TRANSIENT / MULTI_VOICE | Robustness tier override |

### 6.3 — Dynamic Language Signals (Read per token — from LDA Redis state)

| Signal | Source Redis Key | Values | Impact on Selection |
|---|---|---|---|
| `detected_language` | `agent:lda:{sid}:{pid}:detected_lang` | ISO 639-1 code | LID specialist model activation |
| `code_switch_active` | `agent:lda:{sid}:{pid}:code_switch` | true / false | Code-switch classifier selection |
| `lda_confidence` | `agent:lda:{sid}:{pid}:confidence` | Float 0–1 | Model upgrade trigger |
| `language_pair` | `agent:lda:{sid}:{pid}:lang_pair` | e.g. "hi-en" | Specialist pair classifier activation |

### 6.4 — Network Quality Signal (From session join metadata)

| Signal | Source | Values | Impact |
|---|---|---|---|
| `bandwidth_tier` | WebRTC ICE negotiation metadata | HIGH / MEDIUM / LOW | Audio bitrate affects model input quality |
| `packet_loss_pct` | RTCP reports via Jitsi API | Float 0–100 | Degradation compensation model upgrade |

---

## VII. SELECTION DECISION ENGINE (LOCKED)

The selection engine is a **deterministic rule tree (R28-1)** — no ML inference in routing.
Quality scoring for tier placement uses a **GBM classifier (R28-2)** on PBNDA signals.

### 7.1 — Quality Tier Assignment (GBM Classifier)

```
INPUT FEATURES (from PBNDA signals):
  snr_db                 (continuous)
  rms_noise_floor_dbfs   (continuous)
  echo_flag              (binary)
  proximity_flag         (binary)
  noise_class            (categorical: 5 values)
  packet_loss_pct        (continuous)
  bandwidth_tier         (categorical: 3 values)

OUTPUT:
  quality_tier: TIER_1 | TIER_2 | TIER_3 | TIER_4

TIER DEFINITIONS:
  TIER_1 (CLEAN)       : Best conditions — minimum viable model
  TIER_2 (ACCEPTABLE)  : Good conditions — standard model
  TIER_3 (DEGRADED)    : Poor conditions — robust model
  TIER_4 (COMPROMISED) : Worst conditions — maximum accuracy model

MODEL: GBM Classifier (scikit-learn GradientBoostingClassifier)
  Trained on: labeled PBNDA signal corpus (offline)
  Inference:  < 5ms per classification
  Registry:   model_registry: plmsa-quality-clf-v1
```

### 7.2 — Acoustic Feature Extraction Model Selection (Rule Engine)

```
RULE SET — WHISPER MODEL SELECTION:

IF quality_tier == TIER_1:
  IF declared_language == "en": SELECT whisper-tiny-en
  ELSE:                          SELECT whisper-tiny-multi

IF quality_tier == TIER_2:
  SELECT whisper-base-multi

IF quality_tier == TIER_3:
  SELECT whisper-small-multi

IF quality_tier == TIER_4:
  SELECT whisper-medium-multi

IF proximity_flag == true:
  UPGRADE selected model by one tier
  (e.g., tiny → base, base → small, small → medium)
  REASON: Far-field audio loses HF content; larger model compensates

IF packet_loss_pct > 15%:
  UPGRADE selected model by one tier
  REASON: Packet loss corrupts feature windows; larger model more robust

IF echo_flag == true:
  APPLY echo_compensation_config to selected model
  (pre-emphasis filter: +6dB above 2kHz)

HARD CEILING:
  whisper-large-v3 may ONLY be selected when:
    quality_tier == TIER_4
    AND packet_loss_pct > 25%
    AND snr_db < 3
  Under all other conditions: whisper-medium-multi is the ceiling.
```

### 7.3 — Language Identification Model Selection (Rule Engine)

```
RULE SET — LID MODEL SELECTION:

STEP 1: Language cluster detection
  IF declared_language IN (en):
    primary_lid = fasttext-lid-176
    IF lda_confidence < 0.75 AND detected_language IN (hi, hi-en):
      specialist_lid = lingua-hi-en  (activate as secondary)

  IF declared_language IN (hi, hi-en, ur, pa):
    primary_lid = lingua-hi-en
    secondary_lid = fasttext-lid-176

  IF declared_language IN (ta, te, kn, ml):
    primary_lid = lingua-south-indian
    secondary_lid = fasttext-lid-176

  IF declared_language IN (mr, bn, gu):
    primary_lid = lingua-indo-aryan
    secondary_lid = fasttext-lid-176

  IF declared_language NOT IN registered codes:
    primary_lid = fasttext-lid-176
    EMIT: plmsa.unregistered_language_declared

STEP 2: Low-memory fallback
  IF Redis memory_pressure_flag == true:
    REPLACE fasttext-lid-176 WITH fasttext-lid-compressed
```

### 7.4 — Code-Switch Classifier Selection (Rule Engine)

```
RULE SET — CODE-SWITCH MODEL SELECTION:

IF code_switch_active == false AND declared_language == "en":
  csclf = NONE  (not loaded — zero cost)

IF declared_language IN (hi, hi-en) OR detected_language == "hi-en":
  csclf = csclf-hi-en-v1

IF declared_language IN (ta, te, kn, ml):
  csclf = csclf-south-v1

IF declared_language IN (mr, bn, gu, pa):
  csclf = csclf-indo-aryan-v1

IF lda_confidence < 0.65 AND multiple switches detected:
  csclf = csclf-universal-v1  (upgrade to GBM universal classifier)
```

---

## VIII. SELECTION OUTPUT CONTRACT (LOCKED)

PLMSA writes the complete model configuration to Redis for each participant-session.
LDA reads this configuration before any inference.

**Redis Model Config Key:**
```
agent:plmsa:{session_id}:{participant_id}:model_config
TTL: session_duration + 3600 seconds
```

**Model Config Value:**
```json
{
  "config_version": "1.0",
  "generated_at": "<iso8601>",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "selection_trigger": "PRE_SESSION | TOKEN_START | QUALITY_CHANGE | FALLBACK",
  "quality_tier": "TIER_1 | TIER_2 | TIER_3 | TIER_4",
  "quality_tier_source": "gbm_classifier | pbnda_direct | default",
  "acoustic_model": {
    "model_id": "whisper-tiny-multi",
    "transcription_enabled": false,
    "encoder_only": true,
    "echo_compensation": false,
    "pre_emphasis_db": 0
  },
  "lid_model": {
    "primary": "lingua-hi-en",
    "secondary": "fasttext-lid-176",
    "fallback": "fasttext-lid-compressed"
  },
  "csclf_model": {
    "active": true,
    "model_id": "csclf-hi-en-v1",
    "language_pair": "hi-en"
  },
  "upgrade_reason": "<string | null>",
  "cost_estimate_per_token": 0.0024,
  "r28_class": "rule_engine + traditional_ml"
}
```

**Critical field: `transcription_enabled: false` is mandatory in all configs.**
Any config emitted with `transcription_enabled: true` → `STOP EXECUTION` → `SECURITY BREACH`.

---

## IX. DETECTION EVENT CLASSES

### 9.1 — MODEL_SELECTED

Emitted at session start and on each adaptive re-selection.

```json
{
  "event": "audio.plmsa.model_selected",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "MODEL_SELECTED",
  "selection_trigger": "PRE_SESSION | TOKEN_START | QUALITY_CHANGE | FALLBACK",
  "quality_tier": "TIER_1",
  "acoustic_model_id": "whisper-tiny-multi",
  "lid_primary": "lingua-hi-en",
  "csclf_active": true,
  "csclf_model_id": "csclf-hi-en-v1",
  "cost_estimate_per_token": 0.0019,
  "action_required": "lda_consume_config"
}
```

### 9.2 — MODEL_UPGRADED

Emitted when adaptive re-evaluation selects a higher-tier model mid-session.

```json
{
  "event": "audio.plmsa.model_upgraded",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "MODEL_UPGRADED",
  "previous_acoustic_model": "whisper-tiny-multi",
  "new_acoustic_model": "whisper-base-multi",
  "upgrade_reason": "proximity_flag_activated | quality_degraded | packet_loss_high",
  "trigger_signal": "pbnda_proximity_violation | pbnda_quality_change | network_degraded",
  "new_cost_estimate_per_token": 0.0032,
  "action_required": "lda_consume_config"
}
```

### 9.3 — MODEL_DOWNGRADED

Emitted when environment improves and a lighter model is selected to reduce cost.

```json
{
  "event": "audio.plmsa.model_downgraded",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "MODEL_DOWNGRADED",
  "previous_acoustic_model": "whisper-small-multi",
  "new_acoustic_model": "whisper-base-multi",
  "downgrade_reason": "environment_quality_improved",
  "new_cost_estimate_per_token": 0.0024,
  "action_required": "lda_consume_config"
}
```

### 9.4 — FALLBACK_ACTIVATED

Emitted when primary model is unavailable and fallback chain engaged.

```json
{
  "event": "audio.plmsa.fallback_activated",
  "session_id": "<uuid>",
  "participant_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "FALLBACK_ACTIVATED",
  "failed_model_id": "whisper-base-multi",
  "failure_reason": "pod_unavailable | inference_timeout | oom_kill",
  "fallback_model_id": "whisper-tiny-multi",
  "fallback_chain_position": 1,
  "action_required": "lda_consume_config"
}
```

### 9.5 — UNREGISTERED_MODEL_BLOCKED

Emitted when any agent or service attempts to invoke a model not in the catalogue.

```json
{
  "event": "audio.plmsa.unregistered_model_blocked",
  "session_id": "<uuid>",
  "requesting_agent": "<string>",
  "requested_model_id": "<string>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "UNREGISTERED_MODEL_BLOCKED",
  "action_required": "ops_alert_immediate"
}
```

### 9.6 — COST_BUDGET_EXCEEDED

Emitted when per-session model cost projects to exceed defined budget threshold.

```json
{
  "event": "audio.plmsa.cost_budget_exceeded",
  "session_id": "<uuid>",
  "timestamp_utc": "<iso8601>",
  "signal_class": "COST_BUDGET_EXCEEDED",
  "projected_cost_usd": 0.18,
  "budget_threshold_usd": 0.15,
  "current_model_tier": "TIER_4",
  "downgrade_candidate": "whisper-small-multi",
  "action_required": "ops_review"
}
```

---

## X. FALLBACK CHAIN REGISTRY (LOCKED)

For each model slot, PLMSA maintains an ordered fallback chain.
Fallback is automatic — no human intervention during live sessions.

### Acoustic Model Fallback Chains

| Primary | Fallback 1 | Fallback 2 | Fallback 3 (Emergency) |
|---|---|---|---|
| `whisper-tiny-en` | `whisper-tiny-multi` | `fasttext-lid-176` (feature-only mode) | `DEGRADED_INFERENCE` |
| `whisper-tiny-multi` | `whisper-tiny-en` | `fasttext-lid-176` (feature-only mode) | `DEGRADED_INFERENCE` |
| `whisper-base-multi` | `whisper-tiny-multi` | `whisper-tiny-en` | `DEGRADED_INFERENCE` |
| `whisper-small-multi` | `whisper-base-multi` | `whisper-tiny-multi` | `DEGRADED_INFERENCE` |
| `whisper-medium-multi` | `whisper-small-multi` | `whisper-base-multi` | `DEGRADED_INFERENCE` |
| `whisper-large-v3` | `whisper-medium-multi` | `whisper-small-multi` | `DEGRADED_INFERENCE` |

**`DEGRADED_INFERENCE` mode:** Raw spectral features used without Whisper encoder.
fastText lid.176 operates directly on mel spectrogram. Lower accuracy — session continues.

### LID Model Fallback Chains

| Primary | Fallback 1 | Fallback 2 |
|---|---|---|
| `lingua-hi-en` | `fasttext-lid-176` | `fasttext-lid-compressed` |
| `lingua-south-indian` | `fasttext-lid-176` | `fasttext-lid-compressed` |
| `lingua-indo-aryan` | `fasttext-lid-176` | `fasttext-lid-compressed` |
| `fasttext-lid-176` | `fasttext-lid-compressed` | `LANGUAGE_UNIDENTIFIED` |

---

## XI. AGENT EXECUTION STATES

```
STANDBY
  └─ Process alive · Model catalogue loaded into memory
  └─ All registered models verified in MinIO
  └─ GBM quality classifier loaded and warm
  └─ Awaiting: session.started event

SESSION_INIT
  └─ Static session signals read from PostgreSQL/Redis
  └─ Pre-session model configuration computed
  └─ Model config written to Redis
  └─ MODEL_SELECTED event emitted
  └─ Cost estimate logged

TOKEN_EVALUATION
  └─ Per-token re-evaluation triggered on token.start event
  └─ Dynamic signals re-read from PBNDA and LDA Redis state
  └─ GBM quality tier re-scored
  └─ Model config updated if tier changed
  └─ MODEL_UPGRADED / MODEL_DOWNGRADED emitted if changed
  └─ No change: MODEL_SELECTED emitted with selection_trigger: TOKEN_START

FALLBACK_MODE
  └─ Primary model unavailable
  └─ Fallback chain traversed
  └─ FALLBACK_ACTIVATED emitted
  └─ Session continues uninterrupted

SESSION_COMPLETE
  └─ Final cost summary emitted to ClickHouse
  └─ Model config Redis keys expired (TTL enforced)
  └─ Per-session model usage written to audit log

FAULT
  └─ PLMSA process crash
  └─ All connected agents default to: whisper-tiny-multi + fasttext-lid-176 + no csclf
  └─ audio.agent_fault.plmsa emitted
  └─ Ops alert immediately
```

---

## XII. WHAT THE AGENT DOES NOT DO (HARD PROHIBITION)

```
✗ Does NOT invoke or execute models — selection signals only
✗ Does NOT access raw audio — consumes PBNDA/LDA Redis signals only
✗ Does NOT produce transcriptions — ever
✗ Does NOT store audio samples
✗ Does NOT evaluate participant communication ability
✗ Does NOT penalize any participant based on model tier selection
✗ Does NOT use LLM for any selection decision (R28-4)
✗ Does NOT allow unregistered models to be activated
✗ Does NOT allow transcription_enabled: true in any config (security breach)
✗ Does NOT make enforcement decisions (Orchestrator only)
✗ Does NOT communicate directly with the frontend
✗ Does NOT access participant PII — UUID only
✗ Does NOT operate outside session lifecycle bounds
```

Violation of any prohibition → `STOP EXECUTION` → `REPORT AGENT-INTEGRITY-BREACH`

---

## XIII. TECHNOLOGY BINDING (LOCKED)

| Component | Technology |
|---|---|
| Runtime | Python 3.11 |
| Routing Logic | Custom Python rule engine (zero external dependencies) |
| Quality Tier Classifier | `scikit-learn` GradientBoostingClassifier |
| Session State Read | Redis 7 — `orchestrator:session:*` (read-only) |
| PBNDA Signal Read | Redis 7 — `agent:pbnda:*` (read-only) |
| LDA Signal Read | Redis 7 — `agent:lda:*` (read-only) |
| Config Write | Redis 7 — `agent:plmsa:*` |
| Model Registry Read | PostgreSQL 15 — table: `model_registry` (read-only) |
| Model Storage | MinIO — bucket: `ecoskiller-ml-models` |
| Event Bus Write | Apache Kafka — topic: `audio.agent.events` |
| Audit Log | PostgreSQL 15 — table: `plmsa_selection_events` (immutable) |
| Cost Telemetry | ClickHouse — table: `session_model_cost_telemetry` |
| Deployment | Kubernetes pod — namespace: `realtime` |
| Service Name | `phone-language-model-selection-agent` |
| Port | Internal only (`:8083`) — no external exposure |
| Image Lock | `ecoskiller/plmsa:v1.0.0` |

---

## XIV. POSTGRESQL TABLES (IMMUTABLE)

### 14.1 — Selection Audit Table

```sql
CREATE TABLE plmsa_selection_events (
  id                    BIGSERIAL      PRIMARY KEY,
  session_id            UUID           NOT NULL,
  participant_id        UUID           NOT NULL,
  selection_trigger     VARCHAR(30)    NOT NULL,
  quality_tier          VARCHAR(10)    NOT NULL,
  quality_tier_source   VARCHAR(30),
  acoustic_model_id     VARCHAR(60)    NOT NULL,
  lid_primary           VARCHAR(60),
  lid_secondary         VARCHAR(60),
  csclf_model_id        VARCHAR(60),
  cost_estimate         NUMERIC(10,6),
  upgrade_reason        VARCHAR(120),
  fallback_activated    BOOLEAN        DEFAULT FALSE,
  selected_at           TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE RULE no_update_plmsa_events AS
  ON UPDATE TO plmsa_selection_events DO INSTEAD NOTHING;

CREATE RULE no_delete_plmsa_events AS
  ON DELETE TO plmsa_selection_events DO INSTEAD NOTHING;

CREATE INDEX idx_plmsa_session   ON plmsa_selection_events (session_id);
CREATE INDEX idx_plmsa_tier      ON plmsa_selection_events (quality_tier);
CREATE INDEX idx_plmsa_acoustic  ON plmsa_selection_events (acoustic_model_id);
CREATE INDEX idx_plmsa_timestamp ON plmsa_selection_events (selected_at);
```

### 14.2 — Model Registry Table (Shared — PLMSA is primary reader)

```sql
CREATE TABLE model_registry (
  id               BIGSERIAL     PRIMARY KEY,
  model_id         VARCHAR(80)   NOT NULL UNIQUE,
  model_class      VARCHAR(40)   NOT NULL,
  base_model       VARCHAR(80),
  version          VARCHAR(20)   NOT NULL,
  status           VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',
  r28_class        VARCHAR(30)   NOT NULL,
  cost_per_1k_usd  NUMERIC(10,6) NOT NULL,
  inference_ms_p95 INTEGER,
  storage_path     VARCHAR(200),
  transcription_capable BOOLEAN  DEFAULT FALSE,
  registered_at    TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
  deprecated_at    TIMESTAMPTZ,
  notes            TEXT
);
-- status: ACTIVE | CANARY | DEPRECATED | RESERVED
```

---

## XV. CLICKHOUSE COST TELEMETRY TABLE

```sql
CREATE TABLE session_model_cost_telemetry (
  session_id            UUID,
  participant_id        UUID,
  session_date          Date,
  session_type          String,
  domain_track          String,
  declared_language     String,
  acoustic_model_id     String,
  lid_model_id          String,
  csclf_model_id        String,
  quality_tier_final    String,
  tokens_processed      UInt32,
  total_cost_usd        Float64,
  upgrades_count        UInt16,
  downgrades_count      UInt16,
  fallbacks_count       UInt16,
  r28_compliant         UInt8
) ENGINE = MergeTree()
  ORDER BY (session_date, session_type, domain_track, acoustic_model_id);
```

**Platform Analytics Enabled:**
- Cost per session by model tier and domain track
- Model upgrade/downgrade frequency by region and language
- R28 compliance ratio across sessions
- Cost savings vs. uniform Whisper medium baseline
- Fallback activation frequency by model

---

## XVI. KUBERNETES DEPLOYMENT SPEC (LOCKED)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: phone-language-model-selection-agent
  namespace: realtime
  labels:
    app: phone-language-model-selection-agent
    layer: antigravity
    domain: stt-ml
    agent-class: model-router
    r28-class: rule-engine-traditional-ml
spec:
  replicas: 2
  selector:
    matchLabels:
      app: phone-language-model-selection-agent
  template:
    metadata:
      labels:
        app: phone-language-model-selection-agent
    spec:
      initContainers:
        - name: model-catalogue-validator
          image: ecoskiller/plmsa:v1.0.0
          command: ["python", "validate_catalogue.py"]
          env:
            - name: MINIO_URL
              valueFrom:
                secretKeyRef:
                  name: minio-secrets
                  key: url
            - name: POSTGRES_URL
              valueFrom:
                secretKeyRef:
                  name: postgres-secrets
                  key: url
      containers:
        - name: plmsa
          image: ecoskiller/plmsa:v1.0.0
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
            - name: TRANSCRIPTION_GUARD
              value: "STRICT"
          resources:
            requests:
              cpu: "200m"
              memory: "512Mi"
            limits:
              cpu: "500m"
              memory: "1Gi"
          livenessProbe:
            httpGet:
              path: /healthz
              port: 8083
            initialDelaySeconds: 20
            periodSeconds: 15
          readinessProbe:
            httpGet:
              path: /readyz
              port: 8083
            initialDelaySeconds: 15
            periodSeconds: 10
```

**Init container `model-catalogue-validator` runs before pod start.**
It verifies all registered models are present in MinIO.
If any model is missing → pod fails to start → `STOP DEPLOYMENT`.

---

## XVII. OBSERVABILITY (NON-OPTIONAL)

All metrics exposed on `:8083/metrics` (Prometheus scrape target).

| Metric | Type | Description |
|---|---|---|
| `plmsa_model_selections_total` | Counter (labels: model_id, tier) | Total model selection events |
| `plmsa_model_upgrades_total` | Counter (labels: reason) | Total upgrade events |
| `plmsa_model_downgrades_total` | Counter (labels: reason) | Total downgrade events |
| `plmsa_fallback_activations_total` | Counter (labels: model_id) | Total fallback events |
| `plmsa_unregistered_model_blocks_total` | Counter | Blocked unregistered model requests |
| `plmsa_cost_budget_exceeded_total` | Counter | Cost threshold breach events |
| `plmsa_selection_latency_ms` | Histogram | Rule engine + GBM selection latency |
| `plmsa_quality_tier_distribution` | Gauge (labels: tier) | Live distribution of quality tiers |
| `plmsa_active_sessions` | Gauge | Active sessions under model management |
| `plmsa_transcription_guard_violations` | Counter | Must always be 0 — security breach if > 0 |
| `plmsa_r28_compliance_ratio` | Gauge | Ratio of sessions using rule engine correctly |

**Grafana Dashboard:** `ecoskiller-antigravity-model-selection`

**Critical Alert Rules:**
- `plmsa_transcription_guard_violations` > 0 → IMMEDIATE CRITICAL ALERT + session halt
- `plmsa_unregistered_model_blocks_total` > 0 → Immediate security ops alert
- `plmsa_fallback_activations_total` rate > 10/min → Model infrastructure alert
- `plmsa_selection_latency_ms` p95 > 50ms → Performance alert (selection must be fast)

---

## XVIII. SECURITY CONSTRAINTS

```
✔ PLMSA reads only signal metadata from Redis — no raw audio access
✔ transcription_enabled: false enforced in every model config generated
✔ TRANSCRIPTION_GUARD=STRICT environment variable enforced at container level
✔ Model catalogue validated on pod startup via init container
✔ Unregistered model requests blocked and immediately alerted
✔ All inter-service communication via internal Kubernetes network only
✔ No external API calls from agent process
✔ No external port exposure
✔ Kafka ACL: PLMSA has WRITE-only on audio.agent.events
✔ PostgreSQL: PLMSA has INSERT-only on plmsa_selection_events
✔ PostgreSQL: PLMSA has SELECT-only on model_registry
✔ Redis: PLMSA has GET-only on orchestrator:session:*, agent:pbnda:*, agent:lda:*
✔ Redis: PLMSA has SET/GET/EXPIRE on agent:plmsa:* namespace only
✔ ClickHouse: PLMSA has INSERT-only on session_model_cost_telemetry
✔ MinIO: PLMSA has GET-only on ecoskiller-ml-models (model loading only)
✔ Pod runs as non-root — UID 1000
```

---

## XIX. FAILURE MODES & FALLBACK

| Failure | System Response |
|---|---|
| PLMSA pod crash | All agents default to: `whisper-tiny-multi` + `fasttext-lid-176` + no csclf. Session continues. `audio.agent_fault.plmsa` emitted. |
| GBM classifier unavailable | Fallback: PBNDA `environment_quality` field used directly for tier mapping (CLEAN→T1, ACCEPTABLE→T2, DEGRADED→T3, COMPROMISED→T4). |
| Redis signal read timeout | Last known config retained for up to 2 token windows before default fallback activated. |
| Model missing from MinIO | Fallback chain traversed. Alert emitted. Session not halted. |
| Model inference timeout in consumer | PLMSA receives fault signal → immediately selects lighter fallback model. |
| Cost budget exceeded | Ops review event emitted. Session not halted. Model downgrade suggested but not forced. |

No failure mode may halt an active GD, Dojo, or Interview session.

---

## XX. ANTIGRAVITY LAYER AGENT REGISTRY (UPDATED)

| Agent | Handle | Layer | Port | Version | Scope |
|---|---|---|---|---|---|
| `PHONE_SILENCE_SPAM_AGENT` | PSSG | Audio Processing | 8080 | v1.0.0 | Phone events · silence · spam |
| `PHONE_BACKGROUND_NOISE_DETECTION_AGENT` | PBNDA | Audio Processing | 8081 | v1.0.0 | Ambient noise · multi-voice · echo · proximity |
| `LANGUAGE_DETECTION_AGENT` | LDA | STT & ML | 8082 | v1.0.0 | Language ID · mismatch · code-switch |
| `PHONE_LANGUAGE_MODEL_SELECTION_AGENT` | PLMSA | STT & ML | 8083 | v1.0.0 | Model routing · cost governance · fallback |

Future agents added to this layer must register here via version bump.
Unregistered agents → `STOP EXECUTION`.

---

## XXI. VERSION CONTROL LAW

```
Current Version : v1.0.0
Version Format  : MAJOR.MINOR.PATCH

MAJOR → Breaking change to model config schema, selection rule contract,
        Kafka event schema, or model catalogue structure
MINOR → New model added to catalogue, new language cluster rule added,
        new fallback chain entry (add-only)
PATCH → Threshold tuning, GBM retrain, cost estimate update, bug fix

All version bumps require:
  ✔ Updated schema_version in model config and Kafka events
  ✔ Model Registry entry updated if catalogue changes
  ✔ Updated Helm chart version
  ✔ R28 compliance re-declaration if model class changes
  ✔ Changelog entry with cost impact analysis

No silent model additions.
No undocumented catalogue mutations.
No deployments without init container model validation pass.
```

---

## XXII. DEPLOYMENT GATE CHECKLIST (MANDATORY)

```
[ ] All models in catalogue present in MinIO bucket ecoskiller-ml-models:
    [ ] whisper-tiny-en
    [ ] whisper-tiny-multi
    [ ] whisper-base-multi
    [ ] whisper-small-multi
    [ ] whisper-medium-multi
    [ ] fasttext-lid-176 (lid.176.bin)
    [ ] fasttext-lid-compressed (lid.176.ftz)
    [ ] lingua-hi-en
    [ ] lingua-south-indian
    [ ] lingua-indo-aryan
    [ ] csclf-hi-en-v1
    [ ] csclf-south-v1
    [ ] csclf-indo-aryan-v1
    [ ] csclf-universal-v1
    [ ] plmsa-quality-clf-v1 (GBM quality classifier)
[ ] model_registry PostgreSQL table populated with all 15 catalogue entries
[ ] plmsa_selection_events table created with immutability rules
[ ] ClickHouse session_model_cost_telemetry table created
[ ] Redis access confirmed: orchestrator:session:* GET-only
[ ] Redis access confirmed: agent:pbnda:* GET-only
[ ] Redis access confirmed: agent:lda:* GET-only
[ ] Redis access confirmed: agent:plmsa:* write
[ ] Init container model-catalogue-validator passes on pod start
[ ] TRANSCRIPTION_GUARD=STRICT confirmed in deployment spec
[ ] Prometheus scrape config updated for PLMSA pod (port 8083)
[ ] CRITICAL: plmsa_transcription_guard_violations alert rule active
[ ] CRITICAL: plmsa_unregistered_model_blocks_total alert rule active
[ ] Grafana dashboard ecoskiller-antigravity-model-selection imported
[ ] LDA updated to read model_config from agent:plmsa:* before inference
[ ] Agent Registry Section XX updated
[ ] End-to-end test: clean English session → verify whisper-tiny-en selected
[ ] End-to-end test: PBNDA DEGRADED signal → verify model upgraded to whisper-small
[ ] End-to-end test: Hindi declared → verify lingua-hi-en selected as primary LID
[ ] End-to-end test: proximity_flag=true → verify acoustic model upgraded by one tier
[ ] End-to-end test: simulate primary model failure → verify fallback chain activates
[ ] End-to-end test: attempt unregistered model → verify UNREGISTERED_MODEL_BLOCKED
[ ] Cost telemetry records verified in ClickHouse after test session
[ ] R28 compliance audit signed off before production deployment

Absence of any checkbox → STOP DEPLOYMENT → REPORT GATE-INCOMPLETE
```

---

## XXIII. FINAL ENFORCEMENT DECLARATION

```
This agent exists to guarantee that every voice session on Ecoskiller
is processed by the right model — not the most expensive model,
not the cheapest model, but the contextually correct model.

It protects cost discipline (R28).
It protects assessment fairness (correct language models for every region).
It protects signal integrity (quality-tier-matched acoustic extraction).
It protects security (transcription permanently gated out of all configs).

Selection is the only power this agent holds.
That power is exercised by rules, not judgment.
The catalogue is the law.
The rules are the law.
The cost is measured and reported.

No model runs on Ecoskiller voice infrastructure
without this agent's explicit selection.

SEALED · LOCKED · GOVERNED
Version: v1.0.0
```

---

*Part of ECOSKILLER MASTER EXECUTION PROMPT v12.0 — Antigravity STT & ML Layer*
*Siblings: PSSG v1.0.0 · PBNDA v1.0.0 · LDA v1.0.0*
*Governed by: R28 Intelligence Cost Optimization Law · Model Registry Service Law*
*Mutation Policy: Add-only via version bump · Execution Authority: Human declaration only*
