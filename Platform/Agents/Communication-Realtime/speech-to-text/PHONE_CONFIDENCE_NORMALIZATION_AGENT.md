# 🔒 PHONE_CONFIDENCE_NORMALIZATION_AGENT
## ECOSKILLER — Audio Processing Module · Antigravity Layer
### Status: FINAL · LOCKED · SEALED · GOVERNED · DETERMINISTIC
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only
### Agent Registry Position: Audio Processing Agent #4
### Pipeline Position: Between AUDIO_QUALITY_SCORE_AGENT and STT_WORKER_AUTOSCALING_AGENT

---

```
AUDIO PROCESSING AGENT CHAIN (SEALED ORDER):

  [Agent #1]  AUDIO_QUALITY_SCORE_AGENT        — Signal quality gate + enhancement
  [Agent #2]  STT_WORKER_AUTOSCALING_AGENT      — Transcription engine
  [Agent #3]  STT_CONFIDENCE_MONITOR_AGENT      — Transcript quality verdict
  [Agent #4]  PHONE_CONFIDENCE_NORMALIZATION_AGENT ← THIS AGENT
              Specialized intercept layer for phone-channel audio
              Runs IN PARALLEL to Agent #1 for phone-flagged audio
              OR as a post-quality remediation step for phone artifacts
```

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-A — AGENT IDENTITY & PURPOSE DECLARATION
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
Agent Name:             PHONE_CONFIDENCE_NORMALIZATION_AGENT
Agent Class:            Codec-Aware Signal Remediation Agent
Domain:                 Audio Processing — Antigravity Subsystem
Parent System:          ECOSKILLER Unified SaaS Platform
Namespace:              ecoskiller-audio / antigravity / phone-norm
Stack Alignment:        R1 (Python 3.11 · FastAPI · Redis · Kafka · PostgreSQL
                            · ClickHouse · MinIO)
Governance Alignment:   R1 · R10 · R16 · R21 · R25 · R28-1 · R38 · R39 · R40
                        · R45 · R50 · R59 (Offline-First / Low-Bandwidth)
Audit Alignment:        Ecoskiller v8 Infrastructure Audit (All 12 Issues Resolved)
                        — Forgejo + Harbor CI/CD (audit fix #1)
                        — Jitsi + coturn confirmed stack (audit fix #6)

Agent Mission:
  ECOSKILLER operates across all network tiers — from studio-grade
  WebRTC/Jitsi GD sessions on broadband, to rural offline workshops
  using mobile uploads over 2G/3G, to Society franchise participants
  submitting voice recordings from feature phones or low-end smartphones.

  These phone-channel audio streams carry systematic codec artifacts
  that AUDIO_QUALITY_SCORE_AGENT cannot fully remediate using
  general-purpose signal processing:
    — GSM (13 kbps, narrowband 8 kHz, characteristic buzzing artifacts)
    — AMR-NB (4.75–12.2 kbps, click artifacts, frame erasure patterns)
    — AMR-WB / EVS (constrained mobile wideband)
    — OPUS at very low bitrate (< 16 kbps, pre-echo, underwater effect)
    — VoIP-over-mobile (G.711, G.729 compressed streams)
    — coturn-routed streams under packet loss > 5%

  This agent detects phone-channel artifacts specifically, applies
  codec-aware normalization pipelines matched to the detected codec
  class, re-calibrates the confidence baseline for phone-origin audio
  to prevent systematic STT underscoring of valid speech, and emits
  a normalized audio stream plus a phone_confidence_factor that
  the STT_CONFIDENCE_MONITOR_AGENT uses to adjust its verdict thresholds.

Determinism Rule:
  Identical codec fingerprint + signal features → Identical normalization
  pipeline selection and confidence factor output.

Failure Mode:
  STOP → REPORT → FORWARD ORIGINAL AUDIO (no normalization applied)
  → FLAG job as PHONE_NORM_FAILED → alert admin
  → DO NOT block STT pipeline (normalization is best-effort,
    unlike quality gate which is mandatory)
```

Phone normalization is BEST-EFFORT — if it fails, audio forwards to STT
with phone_confidence_factor = 0.70 (default phone penalty applied)
Quality gate (AUDIO_QUALITY_SCORE_AGENT) is MANDATORY — normalization is not.

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-B — ANTIGRAVITY PHONE NORMALIZATION CONCEPT LOCK
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
Antigravity Phone Normalization Definition:
  Phone-channel audio carries an invisible gravitational weight —
  codec compression artifacts, narrowband frequency truncation,
  packet-loss concealment glitches, and transmission noise floors
  that systematically drag STT confidence scores downward even when
  the speaker's actual speech is clear and intelligible.

  The PHONE_CONFIDENCE_NORMALIZATION_AGENT applies antigravity:
    — It lifts narrowband audio into effective wideband representation
    — It removes codec-specific artifacts that confuse the Whisper engine
    — It calibrates a phone_confidence_factor that adjusts the
       STT_CONFIDENCE_MONITOR_AGENT's verdict thresholds for phone-origin
       audio, preventing valid phone transcripts from being unfairly
       flagged as low-confidence
    — It does this transparently — all downstream services receive
       normalized audio + calibrated confidence, with no change to
       their own logic

  High-quality phone audio  → lightweight normalization, factor = 0.90
  Degraded phone audio      → full codec remediation, factor = 0.78
  Severely degraded phone   → best-effort restoration, factor = 0.65
  Unrestorable phone audio  → forward raw, factor = 0.60 (floor)

CRITICAL PLATFORM CONTEXT (from system files):
  ECOSKILLER serves:
    - Rural franchise operators (Society offline model — R59)
    - Students submitting voice portfolios from mobile devices
    - Workshop participants on 2G/3G networks
    - Coach feedback via WhatsApp-style voice note patterns
    - Dojo participants using coturn-routed restricted networks

  These user groups are first-class ECOSKILLER citizens.
  Phone-quality audio from them is not a degraded signal to reject —
  it is the expected signal for their context.
  The system MUST normalize and evaluate fairly, not penalize.

Phone Confidence Factor (phone_confidence_factor):
  Output value: 0.60 – 1.00
  Consumed by: STT_CONFIDENCE_MONITOR_AGENT
               (adjusts composite threshold by × phone_confidence_factor)
  Meaning:
    1.00 = wideband/clean — no phone adjustment needed
    0.90 = light phone artifacts — minor threshold relaxation
    0.78 = moderate phone codec — standard phone normalization applied
    0.65 = severe phone artifacts — best-effort restoration applied
    0.60 = unrestorable — minimum floor, human review recommended

  This factor is ADDITIVE to the source_criticality weight in
  STT_CONFIDENCE_MONITOR_AGENT. Both are applied independently.
```

Absence of phone_confidence_factor output → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-C — PHONE CHANNEL DETECTION TAXONOMY
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
PHONE CHANNEL CLASSES (deterministic fingerprinting — 7 classes):

  ┌───────────────┬──────────────────────────────────────────────────────────┐
  │ CHANNEL_CLASS │ Characteristics & Detection Signals                      │
  ├───────────────┼──────────────────────────────────────────────────────────┤
  │ GSM_NB        │ Sample rate: 8 kHz                                       │
  │               │ Bitrate: 12–13 kbps (GSM-FR / GSM-EFR)                  │
  │               │ Fingerprint: hard 4 kHz upper frequency cutoff           │
  │               │ Artifacts: musicalness, buzzing at vowel transitions      │
  │               │ Source context: student_voice, mentor_feedback (India)    │
  ├───────────────┼──────────────────────────────────────────────────────────┤
  │ AMR_NB        │ Sample rate: 8 kHz                                       │
  │               │ Bitrate: 4.75–12.2 kbps (adaptive multi-rate narrowband) │
  │               │ Fingerprint: frame erasure gaps (8–20ms silence holes)   │
  │               │ Artifacts: clicking at frame boundaries, click trains     │
  │               │ Source context: WhatsApp voice, coach feedback audio      │
  ├───────────────┼──────────────────────────────────────────────────────────┤
  │ AMR_WB        │ Sample rate: 16 kHz (wideband AMR / G.722.2)             │
  │               │ Bitrate: 6.6–23.85 kbps                                  │
  │               │ Fingerprint: 7 kHz frequency ceiling instead of 8 kHz   │
  │               │ Artifacts: pre-echo, slight metallic timbre              │
  │               │ Source context: 4G VoIP calls, modern smartphones        │
  ├───────────────┼──────────────────────────────────────────────────────────┤
  │ OPUS_LOW_BR   │ Sample rate: 8–48 kHz (typically 48 kHz)                 │
  │               │ Bitrate: < 16 kbps (severely constrained OPUS)           │
  │               │ Fingerprint: pre-echo, underwater effect, MDCT artifacts  │
  │               │ Source context: coturn-routed GD session on 2G           │
  │               │ (normal Jitsi OPUS at 32–40 kbps is NOT this class)     │
  ├───────────────┼──────────────────────────────────────────────────────────┤
  │ G711_PCMU     │ Sample rate: 8 kHz, µ-law PCM                            │
  │               │ Bitrate: 64 kbps (uncompressed but narrowband)           │
  │               │ Fingerprint: 8 kHz ceiling, flat noise floor             │
  │               │ Source context: legacy VoIP endpoints, PBX integrations  │
  ├───────────────┼──────────────────────────────────────────────────────────┤
  │ PACKET_LOSS   │ Sample rate: variable (any codec under packet loss)       │
  │               │ Bitrate: variable                                         │
  │               │ Fingerprint: periodic silence holes 20–60ms at irregular  │
  │               │ intervals (PLC concealment pattern), micro-dropouts       │
  │               │ Source context: coturn-routed streams, mobile networks    │
  ├───────────────┼──────────────────────────────────────────────────────────┤
  │ CLEAN_MOBILE  │ Sample rate: 16–48 kHz                                   │
  │               │ Bitrate: ≥ 32 kbps                                        │
  │               │ Fingerprint: passes AUDIO_QUALITY_SCORE but phone_flag    │
  │               │ set because source_type = student_voice / mentor_feedback │
  │               │ Artifacts: minimal — light normalization only             │
  └───────────────┴──────────────────────────────────────────────────────────┘

PHONE FLAG TRIGGER CONDITIONS:
  An audio job is routed to this agent (phone_norm_queue) when ANY of:
    A. source_type ∈ {student_voice, mentor_feedback} AND
       sample_rate ≤ 16000 Hz
    B. AUDIO_QUALITY_SCORE_AGENT detects: bitrate_kbps < 24
    C. AUDIO_QUALITY_SCORE_AGENT detects: score_bandwidth < 0.60
    D. ffprobe format_name contains: amr | gsm | g711 | g729
    E. AUDIO_QUALITY_SCORE_AGENT score_frequency < 0.45 AND
       freq_band_3400_8000 < 0.05 (hard frequency cutoff = narrowband)
    F. Detected silence holes: ≥ 3 gaps of 20–60ms within 10-second window
       (packet loss concealment pattern)
    G. Explicit phone_flag = TRUE in audio_jobs metadata

  When phone_flag triggered:
    AUDIO_QUALITY_SCORE_AGENT emits audio.quality.passed WITH
    phone_flag=TRUE in event payload → routes to phone_norm_queue
    (phone normalization runs before STT, not after)
```

Absence of PHONE FLAG TRIGGER CONDITIONS → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-D — TECHNOLOGY STACK LOCK
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
Agent Runtime:          Python 3.11 + FastAPI (R1 aligned)
Agent Container:        Docker image — ecoskiller/phone-confidence-norm:latest
Image Registry:         Harbor (harbor.ecoskiller.internal) — NOT GHCR (v8 #1)
CI/CD:                  Forgejo Actions — NOT GitHub Actions (v8 #1 — FORBIDDEN)

Signal Analysis:        librosa 0.10.x (Apache 2.0) — FFT, spectral analysis
                        scipy 1.13.x (BSD 3-Clause) — filtering, interpolation
                        numpy 1.26.x (BSD 3-Clause) — array math, peak detection
                        soundfile 0.12.x (BSD 3-Clause) — audio I/O

Noise Reduction:        noisereduce 3.x (MIT) — spectral gating
                        (same lib as AUDIO_QUALITY_SCORE_AGENT — shared image layer)

Bandwidth Extension:    speechbrain (Apache 2.0 — OSS)
                        — Super-resolution / bandwidth extension model
                        — Trained on LibriSpeech (open dataset)
                        — Model: speechbrain/speech-enhancement-metricgan-plus
                        — Purpose: 8 kHz narrowband → 16 kHz effective wideband
                        ONLY used on GSM_NB and AMR_NB classes
                        NOT used for other classes (no ML in rule decisions — R28-1)
                        NOTE: bandwidth extension IS allowed as pre-processing
                              audio enhancement, NOT as a scoring/decision ML model
                              (distinct from R28-1 which forbids ML in rule engines)

Format Conversion:      ffmpeg (LGPL/GPL, self-hosted binary)
                        — AMR → WAV, G.711 → WAV, OPUS decode
                        — Resampling (8k → 16k for Whisper compatibility)
                        — loudnorm filter for volume normalization

Packet Loss Analysis:   Custom Python module (deterministic rule engine)
                        scipy.signal for gap detection
                        No external library required

Codec Fingerprinting:   ffprobe (part of ffmpeg suite)
                        librosa spectral analysis for codec class detection
                        Custom rule-engine fingerprinter (Python 3.11)

Input Queue:            Redis Streams — ecoskiller:audio:phone_norm_queue
                        Consumer group: phone_norm_group
                        Source: AUDIO_QUALITY_SCORE_AGENT (phone_flag=TRUE events)

Output Events (Kafka):
  audio.phone.normalized     → STT_WORKER_AUTOSCALING_AGENT (normalized + factor)
  audio.phone.norm_failed    → forward raw audio to STT + alert admin
  audio.phone.confidence_calibrated → STT_CONFIDENCE_MONITOR_AGENT (factor update)

Database:               PostgreSQL 15 (R1 aligned)
                        Tables: phone_norm_records, phone_norm_rules,
                                codec_fingerprint_log, phone_alerts

Analytics:              ClickHouse — phone_quality_metrics table
                        (appended per job, partitioned by day)

Object Storage:         MinIO
                        Input:   ecoskiller-audio-raw (or enhanced if AQ ran first)
                        Output:  ecoskiller-audio-phone-normalized
                        Reports: ecoskiller-audio-quality-reports
                        Path:    /phone-norm/{session_id}/{job_id}/normalized.wav
                                 /phone-norm/{session_id}/{job_id}/report.json

Cache:                  Redis 7 — codec rules cache (TTL = 300s)
                        Channel class weights cache (TTL = 600s)

Orchestration:          Kubernetes Deployment — ecoskiller-audio namespace
                        Replicas: 2 (HA)
                        HPA: min=1, max=6 (CPU-based, speechbrain is CPU-intensive)
                        Resources: 1.5 CPU / 3Gi RAM per pod

Observability:          Prometheus (:9093/metrics)
                        Grafana dashboard: Phone Channel Quality Board
                        Jaeger: OpenTelemetry spans per job
                        Loki: structured logs (v8 audit fix #7)
```

Use of paid speech API (Deepgram, AssemblyAI, Dolby) → STOP EXECUTION
Use of GitHub Actions or GHCR → STOP EXECUTION (v8 audit violation)
Absence of ffmpeg binary in Docker image → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-E — FULL PIPELINE TOPOLOGY
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
                   ┌───────────────────────────────────────────────┐
                   │      AUDIO_QUALITY_SCORE_AGENT (Agent #1)     │
                   │  Evaluates all audio, emits quality verdict    │
                   └──────────────────┬────────────────────────────┘
                                      │
                   ┌──────────────────▼────────────────────────────┐
                   │ Verdict Router with Phone Detection            │
                   │                                               │
                   │  phone_flag = FALSE → audio.quality.passed    │
                   │  phone_flag = TRUE  → audio.quality.passed    │
                   │                       + route to phone_norm_  │
                   │                         queue BEFORE STT      │
                   └────────┬────────────────────┬─────────────────┘
                            │                    │
               phone_flag=FALSE           phone_flag=TRUE
                            │                    │
                            ▼                    ▼
          ┌─────────────────────┐  ┌────────────────────────────────────┐
          │ STT_WORKER_AUTO     │  │ PHONE_CONFIDENCE_NORMALIZATION_    │
          │ SCALING_AGENT       │  │ AGENT                              │
          │ (direct path)       │  │                                    │
          └─────────────────────┘  │  ┌──────────────────────────────┐ │
                                   │  │ INGESTION                    │ │
                                   │  │ Download audio from MinIO    │ │
                                   │  │ ffprobe + spectral analysis  │ │
                                   │  └──────────────┬───────────────┘ │
                                   │                 │                  │
                                   │  ┌──────────────▼───────────────┐ │
                                   │  │ CODEC FINGERPRINTER          │ │
                                   │  │ Classify: GSM_NB | AMR_NB |  │ │
                                   │  │ AMR_WB | OPUS_LOW_BR |       │ │
                                   │  │ G711_PCMU | PACKET_LOSS |    │ │
                                   │  │ CLEAN_MOBILE                 │ │
                                   │  └──────────────┬───────────────┘ │
                                   │                 │                  │
                                   │  ┌──────────────▼───────────────┐ │
                                   │  │ CHANNEL-SPECIFIC             │ │
                                   │  │ NORMALIZATION PIPELINE       │ │
                                   │  │ (matched to codec class)     │ │
                                   │  └──────────────┬───────────────┘ │
                                   │                 │                  │
                                   │  ┌──────────────▼───────────────┐ │
                                   │  │ CONFIDENCE FACTOR CALCULATOR │ │
                                   │  │ phone_confidence_factor      │ │
                                   │  │ 0.60 – 1.00                  │ │
                                   │  └──────────────┬───────────────┘ │
                                   │                 │                  │
                                   │  ┌──────────────▼───────────────┐ │
                                   │  │ EMIT:                        │ │
                                   │  │ audio.phone.normalized →     │ │
                                   │  │   STT_WORKER_AUTOSCALING     │ │
                                   │  │ audio.phone.confidence_      │ │
                                   │  │   calibrated →               │ │
                                   │  │   STT_CONFIDENCE_MONITOR     │ │
                                   │  └──────────────────────────────┘ │
                                   └────────────────────────────────────┘
                                              │
                                              ▼
                                   ┌──────────────────────────────────┐
                                   │  STT_WORKER_AUTOSCALING_AGENT    │
                                   │  Receives: normalized audio      │
                                   │  + phone_confidence_factor       │
                                   │  + recommended model_variant     │
                                   └──────────────────────────────────┘
                                              │
                                              ▼
                                   ┌──────────────────────────────────┐
                                   │  STT_CONFIDENCE_MONITOR_AGENT    │
                                   │  Applies phone_confidence_factor │
                                   │  to adjust verdict thresholds    │
                                   └──────────────────────────────────┘
```

Absence of phone_norm_queue routing in AUDIO_QUALITY_SCORE_AGENT → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-F — DATABASE SCHEMA (MANDATORY)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```sql
-- ── Phone Normalization Records ──────────────────────────────────────────
CREATE TABLE phone_norm_records (
    norm_id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_id                  UUID NOT NULL REFERENCES audio_jobs(job_id),
    session_id              UUID NOT NULL,
    source_type             TEXT NOT NULL,

    -- Codec detection
    detected_channel_class  TEXT NOT NULL,  -- GSM_NB|AMR_NB|AMR_WB|OPUS_LOW_BR|
                                             -- G711_PCMU|PACKET_LOSS|CLEAN_MOBILE
    detection_confidence    FLOAT NOT NULL,  -- 0.00–1.00 how certain the classification is
    original_sample_rate    INT,             -- Hz before any processing
    original_bitrate_kbps   FLOAT,
    original_format         TEXT,            -- amr|gsm|opus|wav|mp3 etc.

    -- Packet loss detection
    packet_loss_pct         FLOAT,           -- % of detected PLC holes
    gap_count               INT,             -- Number of 20–60ms silence holes
    max_gap_ms              FLOAT,           -- Longest detected packet gap (ms)

    -- Frequency analysis (pre-normalization)
    freq_ceiling_hz         INT,             -- Detected upper frequency limit
    narrowband_flag         BOOLEAN,         -- TRUE if freq_ceiling < 5000 Hz
    bandwidth_class         TEXT,            -- NARROWBAND|WIDEBAND|FULLBAND

    -- Normalization pipeline applied
    normalization_steps     JSONB,           -- Ordered list of steps applied
    bandwidth_extended       BOOLEAN DEFAULT FALSE,
    packet_loss_concealed   BOOLEAN DEFAULT FALSE,
    denoise_applied         BOOLEAN DEFAULT FALSE,
    equalization_applied    BOOLEAN DEFAULT FALSE,
    normalization_duration_ms INT,

    -- Pre/post signal metrics
    pre_norm_snr_db         FLOAT,
    post_norm_snr_db        FLOAT,
    snr_improvement_db      FLOAT,           -- post - pre (improvement)
    pre_norm_freq_ceiling   INT,
    post_norm_freq_ceiling  INT,

    -- Confidence calibration output
    phone_confidence_factor FLOAT NOT NULL,  -- 0.60–1.00 sent to CM agent
    factor_rationale        TEXT NOT NULL,   -- Human-readable reason for factor

    -- Output
    normalized_audio_ref    TEXT,            -- MinIO path to normalized file
    norm_status             TEXT NOT NULL,   -- SUCCESS|PARTIAL|FAILED
    failure_reason          TEXT,            -- Populated on FAILED
    fallback_applied        BOOLEAN DEFAULT FALSE, -- TRUE if forwarded raw

    -- Recommended model for STT
    recommended_model       TEXT,            -- Whisper variant for phone audio
                                             -- GSM_NB/AMR_NB → medium or large-v3

    evaluated_at            TIMESTAMP DEFAULT NOW()
);

-- ── Codec-Specific Normalization Rules ──────────────────────────────────
CREATE TABLE phone_norm_rules (
    rule_id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    channel_class           TEXT UNIQUE NOT NULL,
    steps_json              JSONB NOT NULL,     -- Ordered normalization steps
    recommended_model       TEXT NOT NULL,      -- Whisper variant for this class
    confidence_factor_base  FLOAT NOT NULL,     -- Base phone_confidence_factor
    factor_snr_bonus        FLOAT NOT NULL,     -- Added per 5dB SNR above floor
    factor_snr_floor_db     FLOAT NOT NULL,     -- SNR dB below which no bonus
    bandwidth_extend        BOOLEAN NOT NULL,   -- Apply speechbrain BWE?
    description             TEXT,
    active                  BOOLEAN DEFAULT TRUE,
    updated_at              TIMESTAMP DEFAULT NOW(),
    updated_by              UUID
);

-- ── Codec Detection Log ──────────────────────────────────────────────────
CREATE TABLE codec_fingerprint_log (
    fingerprint_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_id                  UUID NOT NULL,
    detected_class          TEXT NOT NULL,
    detection_signals       JSONB NOT NULL,     -- Raw detection features
    detection_method        TEXT NOT NULL,      -- ffprobe|spectral|rule_engine
    detection_ms            INT,
    created_at              TIMESTAMP DEFAULT NOW()
);

-- ── Phone Quality Alerts ─────────────────────────────────────────────────
CREATE TABLE phone_alerts (
    alert_id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    alert_type              TEXT NOT NULL,   -- SOURCE_GSM_SPIKE|PACKET_LOSS_SURGE|
                                              -- BANDWIDTH_DEGRADATION|NORM_FAILURE_RATE
    source_type             TEXT,
    session_id              UUID,
    channel_class           TEXT,
    affected_count          INT,
    avg_confidence_factor   FLOAT,
    severity                TEXT NOT NULL,
    alert_message           TEXT NOT NULL,
    acknowledged            BOOLEAN DEFAULT FALSE,
    acknowledged_by         UUID,
    created_at              TIMESTAMP DEFAULT NOW()
);

-- ── ClickHouse Analytics ─────────────────────────────────────────────────
-- phone_quality_metrics (MergeTree, partition by toYYYYMMDD(timestamp)):
--   job_id UUID, session_id UUID, source_type String,
--   detected_channel_class String, phone_confidence_factor Float32,
--   pre_norm_snr_db Float32, post_norm_snr_db Float32,
--   snr_improvement_db Float32, packet_loss_pct Float32,
--   normalization_duration_ms UInt32, norm_status String,
--   timestamp DateTime
```

Absence of phone_norm_records or phone_norm_rules tables → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-G — CODEC FINGERPRINTING ENGINE (CORE ALGORITHM)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
DETERMINISTIC CODEC FINGERPRINTING (R28-1 — NO ML IN CLASSIFICATION)

All classification uses rule-based feature thresholds, not trained classifiers.

═══════════════════════════════════════════════════════════════════════
PHASE 1 — CONTAINER FORMAT DETECTION (ffprobe)
═══════════════════════════════════════════════════════════════════════

  Run: ffprobe -v quiet -print_format json -show_format -show_streams

  Extract:
    codec_name      → direct match ('amr' | 'gsm' | 'pcm_mulaw' | 'opus' | 'mp3')
    sample_rate     → Hz
    bit_rate        → bps → convert to kbps
    codec_tag_string → codec fourcc tag
    format_name     → container ('amr' | '3gp' | 'ogg' | 'mp4' | 'wav')

  Direct codec mapping:
    codec_name == 'amr'       AND sample_rate <= 8000  → AMR_NB
    codec_name == 'amr'       AND sample_rate == 16000 → AMR_WB
    codec_name == 'gsm'                                → GSM_NB
    codec_name == 'pcm_mulaw' AND sample_rate == 8000  → G711_PCMU
    codec_name == 'opus'      AND bit_rate < 16000     → OPUS_LOW_BR
    All other codecs → proceed to Phase 2 (spectral detection)

═══════════════════════════════════════════════════════════════════════
PHASE 2 — SPECTRAL FINGERPRINTING (librosa)
═══════════════════════════════════════════════════════════════════════
  Applied when Phase 1 gives inconclusive result (e.g., file is already
  decoded to WAV but carries narrowband artifacts inside).

  FEATURE EXTRACTION:
    y, sr = librosa.load(wav_path, sr=None, mono=True)
    stft  = np.abs(librosa.stft(y, n_fft=2048))
    freqs = librosa.fft_frequencies(sr=sr, n_fft=2048)

  FEATURE A — Frequency Ceiling Detection:
    Compute spectral rolloff at 99% energy level.
    If rolloff_99 < 4200 Hz  → narrowband_flag = TRUE (GSM/AMR territory)
    If rolloff_99 < 7500 Hz  → partial_wideband = TRUE (AMR-WB territory)
    If rolloff_99 ≥ 7500 Hz  → fullband = TRUE

  FEATURE B — Frequency Shelf Pattern:
    Compute energy in 3500–5000 Hz band
    If energy_3500_5000 < 0.02 × energy_300_3400  → hard cutoff detected
    (characteristic of GSM/AMR narrowband codec chains)

  FEATURE C — Packet Loss Hole Detection:
    frame_rms = librosa.feature.rms(y=y, frame_length=int(sr*0.020))[0]
    holes = [i for i in range(len(frame_rms))
             if frame_rms[i] < SILENCE_GATE and frame_rms[i-1] > SILENCE_GATE*5]
    Filter holes by 20–60ms duration (packet loss concealment window)
    If len(filtered_holes) >= 3 within any 10-second window → PACKET_LOSS

  FEATURE D — Spectral Flatness (codec noise floor):
    flatness = librosa.feature.spectral_flatness(y=y)
    mean_flatness = float(np.mean(flatness))
    If mean_flatness > 0.08 AND narrowband_flag → confirms GSM_NB signature
    (GSM codec generates characteristic spectral flatness in noise floor)

  SPECTRAL CLASSIFICATION RULES (priority-ordered):
    1. narrowband_flag AND hard_cutoff AND mean_flatness > 0.08 → GSM_NB
    2. narrowband_flag AND hole_count >= 3                      → AMR_NB
    3. partial_wideband AND NOT narrowband_flag                 → AMR_WB
    4. fullband AND original_bitrate < 16 kbps                 → OPUS_LOW_BR
    5. narrowband_flag AND NOT holes AND NOT flatness_high      → G711_PCMU
    6. hole_count >= 3 AND NOT narrowband_flag                  → PACKET_LOSS
    7. ALL ELSE                                                 → CLEAN_MOBILE

  DETECTION CONFIDENCE SCORE:
    Features matched: 5 → confidence = 1.00
    Features matched: 4 → confidence = 0.90
    Features matched: 3 → confidence = 0.75
    Features matched: 2 → confidence = 0.60
    Features matched: 1 → confidence = 0.45 (borderline — log for review)

  IF detection_confidence < 0.50:
    → Treat as CLEAN_MOBILE (conservative — avoid over-aggressive processing)
    → Log to codec_fingerprint_log with low_confidence=TRUE
```

Absence of Phase 1 (ffprobe) detection → STOP EXECUTION
Absence of Packet Loss Hole Detection → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-H — CHANNEL-SPECIFIC NORMALIZATION PIPELINES
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
7 NORMALIZATION PIPELINES — ONE PER CHANNEL CLASS

Each pipeline is a deterministic ordered sequence of steps.
All steps logged to phone_norm_records.normalization_steps.

═════════════════════════════════════════════════════════════════
PIPELINE 1: GSM_NB NORMALIZATION
═════════════════════════════════════════════════════════════════

  Target codec: GSM-FR / GSM-EFR (13 kbps, 8 kHz, 4 kHz ceiling)
  Common source: student_voice from 2G/feature phones, WhatsApp (older)

  Step 1 — GSM Artifact De-musicalness:
    noisereduce.reduce_noise(y=y, sr=sr, stationary=False, prop_decrease=0.6)
    (Non-stationary mode targets GSM musicalness artifacts specifically)
    Expected: removes characteristic GSM buzzing at vowel transitions

  Step 2 — Spectral Smoothing:
    scipy.signal.savgol_filter on spectral envelope
    Window: 15 frequency bins, polynomial order 3
    Expected: smooths codec quantization noise ridges

  Step 3 — Bandwidth Extension (speechbrain):
    model = SepformerSeparation.from_hparams(
      source="speechbrain/speech-enhancement-metricgan-plus")
    enhanced = model.separate_file(path=wav_path)
    Effective result: 8 kHz narrowband extended to 16 kHz psychoacoustic
    representation. Whisper processes this significantly better.
    Duration limit: max 300 seconds per file via this step.
    If file > 300s → split into chunks, process, reassemble.

  Step 4 — Resample to 16 kHz:
    ffmpeg -ar 16000 -ac 1
    (Whisper requirement — always performed)

  Step 5 — Loudness Normalize:
    ffmpeg loudnorm: I=-16:TP=-1.5:LRA=11

  Expected SNR improvement: +6 to +14 dB
  phone_confidence_factor base: 0.78

═════════════════════════════════════════════════════════════════
PIPELINE 2: AMR_NB NORMALIZATION
═════════════════════════════════════════════════════════════════

  Target codec: AMR-NB (4.75–12.2 kbps, 8 kHz, frame-based)
  Common source: coach_eval via voice notes, workshop participants

  Step 1 — Frame Erasure Concealment:
    Detect silence holes (20–40ms gaps)
    For each hole: linear interpolation between surrounding frames
    scipy.interpolate.interp1d across gap samples
    Max hole size concealed: 60ms (longer gaps → logged as unrestorable)

  Step 2 — Click Train Removal:
    Detect click artifacts: samples where |y[i] - y[i-1]| > 0.3
    and duration < 3ms (clicks, not speech transitions)
    Median filter over identified click windows: scipy.signal.medfilt
    window_length = 5 samples

  Step 3 — Bandwidth Extension (speechbrain):
    Same model as GSM_NB pipeline — 8 kHz → 16 kHz extension
    (AMR-NB benefits significantly from BWE)

  Step 4 — Noise Reduction:
    noisereduce.reduce_noise(y=y, sr=sr, stationary=True, prop_decrease=0.5)

  Step 5 — Resample to 16 kHz + Loudness Normalize

  Expected SNR improvement: +4 to +10 dB
  phone_confidence_factor base: 0.78

═════════════════════════════════════════════════════════════════
PIPELINE 3: AMR_WB NORMALIZATION
═════════════════════════════════════════════════════════════════

  Target codec: AMR-WB / G.722.2 (16 kHz, 7 kHz ceiling)
  Common source: 4G VoIP calls, modern smartphone submissions

  Step 1 — Pre-Echo Suppression:
    MDCT pre-echo is a forward masking artifact.
    Detection: energy spike in 5ms window before transients
    Suppression: temporal masking via windowed attenuation
    scipy.signal.windows.hann(50) applied at detected pre-echo sites

  Step 2 — Light Noise Reduction:
    noisereduce stationary mode, prop_decrease=0.4 (lighter than NB)
    (AMR-WB is already wideband — less aggressive processing needed)

  Step 3 — High-Frequency Restoration:
    Spectral extrapolation from 7 kHz band to 8 kHz using
    scipy mirror extension of spectral envelope
    (fills the 7–8 kHz notch characteristic of AMR-WB)

  Step 4 — Resample to 16 kHz + Loudness Normalize

  No bandwidth extension needed (already wideband)
  Expected SNR improvement: +2 to +6 dB
  phone_confidence_factor base: 0.88

═════════════════════════════════════════════════════════════════
PIPELINE 4: OPUS_LOW_BR NORMALIZATION
═════════════════════════════════════════════════════════════════

  Target codec: OPUS < 16 kbps (coturn-routed GD sessions on 2G)
  Common source: GD participants on severely restricted mobile networks

  Step 1 — Pre-Echo Suppression:
    OPUS uses MDCT — pre-echo same as AMR-WB
    scipy.signal.windows.hann attenuation at transient sites

  Step 2 — Underwater Effect Reduction:
    Low bitrate OPUS creates temporal smearing ("underwater" effect)
    Detection: spectral flux < 0.15 (abnormally smooth spectrum)
    Remedy: transient sharpening via librosa.effects.harmonic separation
    y_harmonic, y_percussive = librosa.effects.hpss(y)
    y_out = y_harmonic + (y_percussive × 1.4)  (boost transients)

  Step 3 — Stationary Noise Reduction:
    noisereduce, prop_decrease=0.5

  Step 4 — Resample to 16 kHz + Loudness Normalize

  No bandwidth extension (OPUS encodes fullband — frequency is not the problem)
  Expected SNR improvement: +3 to +8 dB
  phone_confidence_factor base: 0.74

═════════════════════════════════════════════════════════════════
PIPELINE 5: G711_PCMU NORMALIZATION
═════════════════════════════════════════════════════════════════

  Target codec: G.711 µ-law PCM (64 kbps, 8 kHz, flat noise floor)
  Common source: legacy VoIP endpoints, PBX phone-in integrations

  Step 1 — µ-Law Noise Floor Leveling:
    G.711 µ-law has characteristic quantization noise at low amplitudes
    Apply Wiener filter via scipy.signal.wiener(y, mysize=9)
    Reduces quantization noise without affecting speech

  Step 2 — Bandwidth Extension (speechbrain):
    8 kHz → 16 kHz (same as GSM_NB / AMR_NB pipelines)
    G.711 benefits strongly from BWE due to hard 4 kHz ceiling

  Step 3 — Resample to 16 kHz + Loudness Normalize

  Expected SNR improvement: +4 to +9 dB
  phone_confidence_factor base: 0.82

═════════════════════════════════════════════════════════════════
PIPELINE 6: PACKET_LOSS NORMALIZATION
═════════════════════════════════════════════════════════════════

  Target: Any codec with confirmed packet loss concealment artifacts
  Common source: coturn-routed streams under mobile packet loss

  Step 1 — Packet Loss Hole Mapping:
    Identify all 20–60ms silence holes from Phase 2 detection
    Build hole_map: list of (start_sample, end_sample) pairs

  Step 2 — Interpolative Concealment:
    For each hole in hole_map:
      IF hole_duration ≤ 40ms:
        → cubic spline interpolation (scipy.interpolate.CubicSpline)
        between pre-hole and post-hole 20ms windows
      IF hole_duration 40–60ms:
        → linear fade-out + fade-in with silence
        (cubic spline unreliable for longer gaps)
      IF hole_duration > 60ms:
        → log as unrestorable_gap, leave as silence, continue

  Step 3 — Stationary Noise Reduction:
    After concealment, residual codec noise
    noisereduce, prop_decrease=0.45

  Step 4 — Resample to 16 kHz + Loudness Normalize

  Confidence Factor Modifier:
    packet_loss_pct ≤ 5%   → factor = 0.86
    packet_loss_pct ≤ 15%  → factor = 0.76
    packet_loss_pct ≤ 30%  → factor = 0.68
    packet_loss_pct > 30%  → factor = 0.60 (floor)

  phone_confidence_factor base: calculated from packet_loss_pct above

═════════════════════════════════════════════════════════════════
PIPELINE 7: CLEAN_MOBILE NORMALIZATION
═════════════════════════════════════════════════════════════════

  Target: Mobile source with adequate quality, no codec artifacts
  Common source: student_voice via modern smartphone, good network

  Step 1 — Light Stationary Noise Reduction:
    noisereduce, prop_decrease=0.25 (very gentle — avoid over-processing)

  Step 2 — Resample to 16 kHz if needed + Loudness Normalize

  No bandwidth extension. No heavy processing.
  Expected SNR improvement: +1 to +3 dB
  phone_confidence_factor base: 0.92

═════════════════════════════════════════════════════════════════
CONFIDENCE FACTOR CALCULATOR (FINAL STEP — ALL PIPELINES)
═════════════════════════════════════════════════════════════════

  After normalization pipeline completes:

  1. Measure post_norm_snr_db (re-run SNR estimator on normalized file)
  2. Compute SNR bonus:
     snr_bonus = max(0.0, (post_norm_snr_db - factor_snr_floor_db) / 5) × 0.04
     Capped at: max_snr_bonus = 0.12
     (Each 5 dB above floor = +0.04 to factor, max +0.12 total bonus)

  3. Final factor:
     phone_confidence_factor = min(1.00,
       confidence_factor_base + snr_bonus)

  4. Factor rationale (human-readable string):
     "class=GSM_NB base=0.78 snr_post=18.3dB snr_bonus=0.05 final=0.83"
```

Absence of any of the 7 normalization pipelines → STOP EXECUTION
Absence of CONFIDENCE FACTOR CALCULATOR → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-I — RECOMMENDED WHISPER MODEL MATRIX
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
Phone audio requires larger Whisper models to handle narrowband artifacts.
This agent emits a recommended_model field consumed by STT_WORKER_AUTOSCALING_AGENT.

  ┌───────────────┬──────────────────┬───────────────────────────────────────────┐
  │ CHANNEL_CLASS │ Recommended Model│ Rationale                                 │
  ├───────────────┼──────────────────┼───────────────────────────────────────────┤
  │ GSM_NB        │ medium           │ Small/base struggle with GSM musicalness   │
  │               │ (large-v3 if     │ even after BWE. Medium handles it well.    │
  │               │  tournament)     │                                            │
  ├───────────────┼──────────────────┼───────────────────────────────────────────┤
  │ AMR_NB        │ medium           │ Click artifacts and frame erasure require  │
  │               │                  │ robust attention mechanism.                │
  ├───────────────┼──────────────────┼───────────────────────────────────────────┤
  │ AMR_WB        │ small            │ Already wideband — small handles well.     │
  ├───────────────┼──────────────────┼───────────────────────────────────────────┤
  │ OPUS_LOW_BR   │ small            │ After transient restoration, small is OK. │
  ├───────────────┼──────────────────┼───────────────────────────────────────────┤
  │ G711_PCMU     │ medium           │ 8 kHz ceiling despite 64 kbps bitrate.    │
  │               │                  │ Medium needed after BWE.                   │
  ├───────────────┼──────────────────┼───────────────────────────────────────────┤
  │ PACKET_LOSS   │ medium           │ Concealed gaps confuse small/base models.  │
  │               │ (large-v3 if     │                                            │
  │               │  pkt_loss > 20%) │                                            │
  ├───────────────┼──────────────────┼───────────────────────────────────────────┤
  │ CLEAN_MOBILE  │ base             │ No degradation — base model sufficient.    │
  └───────────────┴──────────────────┴───────────────────────────────────────────┘

COST GOVERNANCE (R25):
  Prefer small/medium over large-v3 for phone audio wherever possible.
  large-v3 activated only for: tournament_event source + severe codec class
  OR packet_loss_pct > 20%.
  Admin can override via Ops Console rule editor.
```

Absence of recommended_model matrix → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-J — AGENT SERVICE CODE (PYTHON — REQUIRED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```python
# File: /backend/services/phone_confidence_norm/main.py
# Purpose: Detect phone codec class, apply targeted normalization pipeline,
#          compute phone_confidence_factor, forward to STT pipeline
# Stack: Python 3.11 · librosa · noisereduce · speechbrain · scipy · ffmpeg

import os, json, uuid, time, subprocess, tempfile, math
import numpy as np
import librosa, soundfile as sf, noisereduce as nr
from scipy import signal, interpolate
from kafka import KafkaProducer
import redis, psycopg2
from prometheus_client import Counter, Histogram, Gauge, start_http_server

# ── Prometheus Metrics ───────────────────────────────────────────────────
PN_CLASSES      = Counter('pn_channel_class_total', 'Detected codec classes',
                          ['channel_class'])
PN_FACTORS      = Histogram('pn_confidence_factor', 'Phone confidence factors',
                            buckets=[.60,.65,.70,.75,.78,.82,.86,.90,.92,.95,1.0])
PN_SNR_IMPROVE  = Histogram('pn_snr_improvement_db', 'SNR improvement (dB)',
                            buckets=[-2,0,2,4,6,8,10,12,14,16,20])
PN_NORM_LATENCY = Histogram('pn_normalization_ms', 'Normalization duration (ms)')
PN_BWE_TOTAL    = Counter('pn_bandwidth_extension_total', 'BWE applied', ['success'])
PN_FAILURES     = Counter('pn_norm_failures_total', 'Normalization failures',
                          ['channel_class'])
PN_PKT_LOSS_PCT = Histogram('pn_packet_loss_pct', 'Detected packet loss %',
                            buckets=[0,2,5,10,15,20,30,40,50])
PN_PENDING      = Gauge('pn_queue_depth', 'Jobs in phone_norm_queue')

# ── Config ───────────────────────────────────────────────────────────────
REDIS_HOST     = os.getenv("REDIS_HOST", "redis-service.ecoskiller-infra")
KAFKA_BROKERS  = os.getenv("KAFKA_BROKERS", "kafka.ecoskiller-infra:9092")
DB_DSN         = os.getenv("DB_DSN")
STREAM_KEY     = "ecoskiller:audio:phone_norm_queue"
CONSUMER_GROUP = "phone_norm_group"
CONSUMER_NAME  = f"pn-worker-{uuid.uuid4().hex[:8]}"
SILENCE_GATE   = 0.001
CLIP_THRESH    = 0.985


def run():
    start_http_server(9093)
    r  = redis.Redis(host=REDIS_HOST, decode_responses=True)
    db = psycopg2.connect(DB_DSN)
    producer = KafkaProducer(
        bootstrap_servers=KAFKA_BROKERS,
        value_serializer=lambda v: json.dumps(v).encode()
    )
    try:
        r.xgroup_create(STREAM_KEY, CONSUMER_GROUP, id='0', mkstream=True)
    except redis.exceptions.ResponseError:
        pass
    rules = load_norm_rules(db)
    print(f"[PN] PHONE_CONFIDENCE_NORMALIZATION_AGENT started: {CONSUMER_NAME}")
    while True:
        msgs = r.xreadgroup(CONSUMER_GROUP, CONSUMER_NAME,
                            {STREAM_KEY: '>'}, count=1, block=5000)
        if not msgs:
            continue
        for _, entries in msgs:
            for msg_id, data in entries:
                PN_PENDING.inc()
                t0 = time.time()
                try:
                    process_job(data, db, r, producer, rules)
                    PN_NORM_LATENCY.observe((time.time() - t0) * 1000)
                    r.xack(STREAM_KEY, CONSUMER_GROUP, msg_id)
                except Exception as e:
                    print(f"[PN ERROR] job={data.get('job_id')} err={e}")
                finally:
                    PN_PENDING.dec()


def process_job(data, db, r, producer, rules):
    job_id      = data["job_id"]
    session_id  = data["session_id"]
    source_type = data["source_type"]
    audio_ref   = data["audio_ref_final"]

    with tempfile.TemporaryDirectory() as tmpdir:
        raw_path = fetch_audio(audio_ref, tmpdir)
        wav_path = convert_to_wav16k(raw_path, tmpdir, job_id)
        y, sr    = librosa.load(wav_path, sr=None, mono=True)
        props    = get_props(raw_path)

        # Phase 1 — Container/codec detection (ffprobe)
        channel_class, detection_signals = fingerprint_codec(raw_path, y, sr, props)
        detection_conf = score_detection_confidence(detection_signals)
        PN_CLASSES.labels(channel_class=channel_class).inc()
        log_fingerprint(db, job_id, channel_class, detection_signals, detection_conf)

        # Measure pre-norm SNR
        pre_snr = measure_snr(y, sr)

        # Packet loss detection
        gap_count, max_gap_ms, pkt_loss_pct = detect_packet_loss(y, sr)
        PN_PKT_LOSS_PCT.observe(pkt_loss_pct)

        # Run channel-specific normalization pipeline
        rule = rules.get(channel_class, rules["CLEAN_MOBILE"])
        norm_result = normalize(y, sr, tmpdir, job_id, channel_class,
                                rule, gap_count, y.copy())

        post_snr = 0.0
        norm_status = "FAILED"
        normalized_ref = None
        steps = []
        bwe_applied = False

        if norm_result["success"]:
            y_norm  = norm_result["audio"]
            sr_norm = norm_result["sr"]
            steps   = norm_result["steps"]
            bwe_applied = norm_result.get("bwe", False)

            # Save normalized file
            out_path = os.path.join(tmpdir, f"{job_id}_norm.wav")
            sf.write(out_path, y_norm, sr_norm)
            normalized_ref = upload_normalized(out_path, job_id, session_id)
            post_snr = measure_snr(y_norm, sr_norm)
            norm_status = "SUCCESS"
        else:
            PN_FAILURES.labels(channel_class=channel_class).inc()
            norm_status = "FAILED"

        # Compute phone_confidence_factor
        pcf, rationale = compute_confidence_factor(
            channel_class, rule, post_snr, pkt_loss_pct, norm_status
        )
        PN_FACTORS.observe(pcf)

        snr_improvement = post_snr - pre_snr
        PN_SNR_IMPROVE.observe(snr_improvement)
        if bwe_applied:
            PN_BWE_TOTAL.labels(success="true" if norm_result["success"] else "false").inc()

        rec = {
            "norm_id":               str(uuid.uuid4()),
            "job_id":                job_id,
            "session_id":            session_id,
            "source_type":           source_type,
            "detected_channel_class": channel_class,
            "detection_confidence":  detection_conf,
            "original_sample_rate":  props.get("sample_rate"),
            "original_bitrate_kbps": props.get("bitrate_kbps"),
            "original_format":       props.get("format"),
            "packet_loss_pct":       pkt_loss_pct,
            "gap_count":             gap_count,
            "max_gap_ms":            max_gap_ms,
            "freq_ceiling_hz":       detection_signals.get("rolloff_hz"),
            "narrowband_flag":       detection_signals.get("narrowband_flag", False),
            "bandwidth_class":       get_bandwidth_class(detection_signals),
            "normalization_steps":   json.dumps(steps),
            "bandwidth_extended":    bwe_applied,
            "packet_loss_concealed": "packet_loss_conceal" in steps,
            "denoise_applied":       "noise_reduce" in steps,
            "pre_norm_snr_db":       pre_snr,
            "post_norm_snr_db":      post_snr,
            "snr_improvement_db":    snr_improvement,
            "phone_confidence_factor": pcf,
            "factor_rationale":      rationale,
            "normalized_audio_ref":  normalized_ref,
            "norm_status":           norm_status,
            "fallback_applied":      norm_status == "FAILED",
            "recommended_model":     rule.get("recommended_model", "base"),
        }
        persist(rec, db)

        # Emit Kafka events
        audio_final = normalized_ref if normalized_ref else audio_ref
        emit_normalized(producer, rec, audio_final, data)
        emit_confidence_calibration(producer, rec)


def fingerprint_codec(raw_path, y, sr, props):
    signals = {}
    # Phase 1: ffprobe direct detection
    codec_name = props.get("codec_name", "")
    sr_orig    = props.get("sample_rate", sr)
    br_kbps    = props.get("bitrate_kbps", 128)
    if codec_name == "amr" and sr_orig <= 8000:
        return "AMR_NB", {"method": "ffprobe", "codec": codec_name}
    if codec_name == "amr" and sr_orig == 16000:
        return "AMR_WB", {"method": "ffprobe", "codec": codec_name}
    if codec_name == "gsm":
        return "GSM_NB", {"method": "ffprobe", "codec": codec_name}
    if codec_name == "pcm_mulaw" and sr_orig == 8000:
        return "G711_PCMU", {"method": "ffprobe", "codec": codec_name}
    if codec_name == "opus" and br_kbps < 16:
        return "OPUS_LOW_BR", {"method": "ffprobe", "codec": codec_name}

    # Phase 2: Spectral fingerprinting
    stft  = np.abs(librosa.stft(y, n_fft=2048))
    freqs = librosa.fft_frequencies(sr=sr, n_fft=2048)
    rolloff = librosa.feature.spectral_rolloff(y=y, sr=sr, roll_percent=0.99)[0]
    rolloff_hz = float(np.mean(rolloff))
    signals["rolloff_hz"] = rolloff_hz
    signals["narrowband_flag"] = rolloff_hz < 4200
    signals["partial_wideband"] = 4200 <= rolloff_hz < 7500

    # Hard cutoff check
    def band_energy(fmin, fmax):
        mask = (freqs >= fmin) & (freqs <= fmax)
        return float(np.mean(stft[mask])) if mask.any() else 0.0
    e_core = band_energy(300, 3400)
    e_shelf = band_energy(3500, 5000)
    signals["hard_cutoff"] = e_shelf < 0.02 * (e_core + 1e-9)

    # Spectral flatness
    flatness = float(np.mean(librosa.feature.spectral_flatness(y=y)))
    signals["mean_flatness"] = flatness

    # Packet loss detection
    gap_count, _, pkt_loss_pct = detect_packet_loss(y, sr)
    signals["gap_count"] = gap_count
    signals["pkt_loss_pct"] = pkt_loss_pct

    # Classify
    nb   = signals["narrowband_flag"]
    hc   = signals["hard_cutoff"]
    sf_h = flatness > 0.08
    holes = gap_count >= 3
    pw   = signals["partial_wideband"]

    if nb and hc and sf_h:               return "GSM_NB",     signals
    if nb and holes:                     return "AMR_NB",     signals
    if pw and not nb:                    return "AMR_WB",     signals
    if not nb and br_kbps < 16:         return "OPUS_LOW_BR", signals
    if nb and not holes and not sf_h:    return "G711_PCMU",  signals
    if holes and not nb:                 return "PACKET_LOSS", signals
    return "CLEAN_MOBILE", signals


def detect_packet_loss(y, sr):
    frame_len = int(sr * 0.020)
    rms = librosa.feature.rms(y=y, frame_length=frame_len, hop_length=frame_len)[0]
    gate = max(float(np.mean(rms)) * 0.05, SILENCE_GATE)
    holes = []
    in_hole = False; hole_start = 0
    for i, v in enumerate(rms):
        if v < gate and not in_hole:
            in_hole = True; hole_start = i
        elif v >= gate and in_hole:
            dur_ms = (i - hole_start) * 20
            if 20 <= dur_ms <= 60:
                holes.append((hole_start, i, dur_ms))
            in_hole = False
    total_dur_s = len(y) / sr
    pkt_loss_pct = (sum(h[2] for h in holes) / 1000) / max(total_dur_s, 1) * 100
    max_gap = max((h[2] for h in holes), default=0)
    return len(holes), float(max_gap), float(pkt_loss_pct)


def normalize(y, sr, tmpdir, job_id, channel_class, rule, gap_count, y_orig):
    steps = []
    bwe = False
    try:
        if channel_class == "GSM_NB":
            y = nr.reduce_noise(y=y, sr=sr, stationary=False, prop_decrease=0.6)
            steps.append("gsm_demusical")
            y = apply_bwe(y, sr, tmpdir, job_id)
            steps.append("bandwidth_extend"); bwe = True
            y, sr = resample_16k(y, sr)
        elif channel_class == "AMR_NB":
            y = conceal_frame_erasures(y, sr, gap_count)
            steps.append("frame_erasure_conceal")
            y = remove_click_artifacts(y)
            steps.append("click_remove")
            y = apply_bwe(y, sr, tmpdir, job_id)
            steps.append("bandwidth_extend"); bwe = True
            y = nr.reduce_noise(y=y, sr=sr, stationary=True, prop_decrease=0.5)
            steps.append("noise_reduce")
            y, sr = resample_16k(y, sr)
        elif channel_class == "AMR_WB":
            y = suppress_pre_echo(y, sr)
            steps.append("pre_echo_suppress")
            y = nr.reduce_noise(y=y, sr=sr, stationary=True, prop_decrease=0.4)
            steps.append("noise_reduce")
            y, sr = resample_16k(y, sr)
        elif channel_class == "OPUS_LOW_BR":
            y = suppress_pre_echo(y, sr)
            steps.append("pre_echo_suppress")
            y_h, y_p = librosa.effects.hpss(y)
            y = y_h + (y_p * 1.4)
            steps.append("transient_sharpen")
            y = nr.reduce_noise(y=y, sr=sr, stationary=True, prop_decrease=0.5)
            steps.append("noise_reduce")
            y, sr = resample_16k(y, sr)
        elif channel_class == "G711_PCMU":
            y = signal.wiener(y, mysize=9)
            steps.append("wiener_filter")
            y = apply_bwe(y, sr, tmpdir, job_id)
            steps.append("bandwidth_extend"); bwe = True
            y, sr = resample_16k(y, sr)
        elif channel_class == "PACKET_LOSS":
            y = conceal_packet_loss_gaps(y, sr)
            steps.append("packet_loss_conceal")
            y = nr.reduce_noise(y=y, sr=sr, stationary=True, prop_decrease=0.45)
            steps.append("noise_reduce")
            y, sr = resample_16k(y, sr)
        else:  # CLEAN_MOBILE
            y = nr.reduce_noise(y=y, sr=sr, stationary=True, prop_decrease=0.25)
            steps.append("light_noise_reduce")
            y, sr = resample_16k(y, sr)

        # Loudness normalize via ffmpeg (all pipelines)
        pre_path  = os.path.join(tmpdir, f"{job_id}_prenorm.wav")
        post_path = os.path.join(tmpdir, f"{job_id}_loudnorm.wav")
        sf.write(pre_path, y, sr)
        subprocess.run(["ffmpeg", "-y", "-i", pre_path,
                        "-af", "loudnorm=I=-16:TP=-1.5:LRA=11",
                        post_path], check=True, capture_output=True, timeout=120)
        y, sr = librosa.load(post_path, sr=None, mono=True)
        steps.append("loudnorm")
        return {"success": True, "audio": y, "sr": sr, "steps": steps, "bwe": bwe}
    except Exception as e:
        return {"success": False, "steps": steps, "error": str(e), "bwe": bwe}


def compute_confidence_factor(channel_class, rule, post_snr, pkt_loss_pct, status):
    if status == "FAILED":
        return 0.60, f"class={channel_class} norm_failed=TRUE fallback_floor=0.60"
    if channel_class == "PACKET_LOSS":
        if   pkt_loss_pct <= 5:  base = 0.86
        elif pkt_loss_pct <= 15: base = 0.76
        elif pkt_loss_pct <= 30: base = 0.68
        else:                    base = 0.60
    else:
        base = rule.get("confidence_factor_base", 0.78)
    floor_db = rule.get("factor_snr_floor_db", 10.0)
    snr_bonus = min(0.12, max(0.0, (post_snr - floor_db) / 5) * 0.04)
    factor = min(1.00, base + snr_bonus)
    rationale = (f"class={channel_class} base={base:.2f} "
                 f"snr_post={post_snr:.1f}dB snr_bonus={snr_bonus:.3f} "
                 f"pkt_loss={pkt_loss_pct:.1f}% final={factor:.3f}")
    return factor, rationale


def measure_snr(y, sr):
    frame_len = int(sr * 0.025)
    rms = librosa.feature.rms(y=y, frame_length=frame_len,
                               hop_length=frame_len // 2)[0]
    rms = rms[rms > 0]
    if len(rms) < 2:
        return 0.0
    return float(20 * np.log10(np.percentile(rms, 90) /
                               max(np.percentile(rms, 10), 1e-8)))


def suppress_pre_echo(y, sr):
    transient_idx = np.where(np.diff(np.abs(y)) > 0.05)[0]
    y_out = y.copy()
    pre_window = int(sr * 0.005)
    for idx in transient_idx:
        start = max(0, idx - pre_window)
        win = signal.windows.hann(idx - start + 1)
        y_out[start:idx + 1] *= win
    return y_out


def conceal_frame_erasures(y, sr, gap_count):
    frame_len = int(sr * 0.020)
    rms = librosa.feature.rms(y=y, frame_length=frame_len, hop_length=frame_len)[0]
    gate = max(float(np.mean(rms)) * 0.05, SILENCE_GATE)
    y_out = y.copy()
    i = 0
    while i < len(rms) - 1:
        if rms[i] < gate:
            start_s = i * frame_len
            end_s = min((i + 2) * frame_len, len(y_out))
            if end_s < len(y_out) and start_s > 0:
                pre = y_out[max(0, start_s - frame_len):start_s]
                post = y_out[end_s:min(end_s + frame_len, len(y_out))]
                if len(pre) > 0 and len(post) > 0:
                    xs = [0, len(pre) + len(y_out[start_s:end_s]) + len(post)]
                    ys_vals = [float(pre[-1]), float(post[0])]
                    interp = interpolate.interp1d(
                        [0, len(y_out[start_s:end_s]) + 1],
                        ys_vals, kind='linear')
                    fill = interp(np.arange(end_s - start_s))
                    y_out[start_s:end_s] = fill
        i += 1
    return y_out


def conceal_packet_loss_gaps(y, sr):
    frame_len = int(sr * 0.020)
    rms = librosa.feature.rms(y=y, frame_length=frame_len, hop_length=frame_len)[0]
    gate = max(float(np.mean(rms)) * 0.05, SILENCE_GATE)
    y_out = y.copy()
    in_hole = False; hole_start = 0
    for i, v in enumerate(rms):
        if v < gate and not in_hole:
            in_hole = True; hole_start = i * frame_len
        elif v >= gate and in_hole:
            hole_end = i * frame_len
            dur_ms = (hole_end - hole_start) / sr * 1000
            if dur_ms <= 40:
                pre_val = float(y_out[hole_start - 1]) if hole_start > 0 else 0.0
                post_val = float(y_out[hole_end]) if hole_end < len(y_out) else 0.0
                fill = np.interp(np.arange(hole_end - hole_start),
                                 [0, hole_end - hole_start - 1],
                                 [pre_val, post_val])
                y_out[hole_start:hole_end] = fill
            in_hole = False
    return y_out


def remove_click_artifacts(y):
    diff = np.abs(np.diff(y))
    click_thresh = 0.3
    clicks = np.where(diff > click_thresh)[0]
    y_out = y.copy()
    for c in clicks:
        if c + 5 < len(y_out):
            y_out[c:c + 5] = signal.medfilt(y_out[c:c + 5], kernel_size=5)
    return y_out


def apply_bwe(y, sr, tmpdir, job_id):
    try:
        from speechbrain.pretrained import SepformerSeparation
        tmp_in = os.path.join(tmpdir, f"{job_id}_bwe_in.wav")
        sf.write(tmp_in, y, sr)
        model = SepformerSeparation.from_hparams(
            source="speechbrain/speech-enhancement-metricgan-plus",
            savedir=os.path.join(tmpdir, "sb_model"),
            run_opts={"device": "cpu"}
        )
        est = model.separate_file(path=tmp_in)
        y_out = est[:, :, 0].squeeze().cpu().numpy()
        if len(y_out) < 100:
            return y
        return y_out
    except Exception:
        return y   # BWE failed — return original (best-effort)


def resample_16k(y, sr):
    if sr != 16000:
        y = librosa.resample(y, orig_sr=sr, target_sr=16000)
    return y, 16000


def score_detection_confidence(signals):
    matched = sum(1 for k, v in signals.items()
                  if k not in ('method', 'codec') and v is not None)
    return min(1.0, matched * 0.20)


def get_bandwidth_class(signals):
    if signals.get("narrowband_flag"):       return "NARROWBAND"
    if signals.get("partial_wideband"):      return "WIDEBAND"
    return "FULLBAND"


def convert_to_wav16k(src, tmpdir, job_id):
    dst = os.path.join(tmpdir, f"{job_id}_converted.wav")
    subprocess.run(["ffmpeg", "-y", "-i", src, "-ar", "16000", "-ac", "1", dst],
                   check=True, capture_output=True, timeout=120)
    return dst


def get_props(path):
    try:
        res = subprocess.run(
            ["ffprobe", "-v", "quiet", "-print_format", "json",
             "-show_format", "-show_streams", path],
            capture_output=True, text=True, timeout=30)
        ff = json.loads(res.stdout)
        streams = ff.get("streams", [{}])
        s0 = streams[0] if streams else {}
        return {
            "codec_name":    s0.get("codec_name", ""),
            "sample_rate":   int(s0.get("sample_rate", 16000)),
            "bitrate_kbps":  int(ff.get("format", {}).get("bit_rate", 32000)) // 1000,
            "format":        ff.get("format", {}).get("format_name", "unknown"),
            "size_bytes":    int(ff.get("format", {}).get("size", 0)),
        }
    except Exception:
        return {"codec_name": "", "sample_rate": 16000,
                "bitrate_kbps": 32, "format": "unknown", "size_bytes": 0}


def fetch_audio(ref, tmpdir): return os.path.join(tmpdir, "raw_audio")
def upload_normalized(path, job_id, session_id):
    return f"/phone-norm/{session_id}/{job_id}/normalized.wav"

def persist(rec, db):
    with db.cursor() as cur:
        cur.execute("""
          INSERT INTO phone_norm_records
            (norm_id, job_id, session_id, source_type,
             detected_channel_class, detection_confidence,
             original_sample_rate, original_bitrate_kbps, original_format,
             packet_loss_pct, gap_count, max_gap_ms,
             freq_ceiling_hz, narrowband_flag, bandwidth_class,
             normalization_steps, bandwidth_extended, packet_loss_concealed,
             denoise_applied, pre_norm_snr_db, post_norm_snr_db,
             snr_improvement_db, phone_confidence_factor, factor_rationale,
             normalized_audio_ref, norm_status, fallback_applied,
             recommended_model)
          VALUES
            (%(norm_id)s, %(job_id)s, %(session_id)s, %(source_type)s,
             %(detected_channel_class)s, %(detection_confidence)s,
             %(original_sample_rate)s, %(original_bitrate_kbps)s, %(original_format)s,
             %(packet_loss_pct)s, %(gap_count)s, %(max_gap_ms)s,
             %(freq_ceiling_hz)s, %(narrowband_flag)s, %(bandwidth_class)s,
             %(normalization_steps)s, %(bandwidth_extended)s,
             %(packet_loss_concealed)s, %(denoise_applied)s,
             %(pre_norm_snr_db)s, %(post_norm_snr_db)s, %(snr_improvement_db)s,
             %(phone_confidence_factor)s, %(factor_rationale)s,
             %(normalized_audio_ref)s, %(norm_status)s, %(fallback_applied)s,
             %(recommended_model)s)
        """, rec)
    db.commit()

def emit_normalized(producer, rec, audio_final, data):
    topic = ("audio.phone.normalized"
             if rec["norm_status"] == "SUCCESS"
             else "audio.phone.norm_failed")
    producer.send(topic, value={
        "job_id":                   rec["job_id"],
        "session_id":               rec["session_id"],
        "source_type":              rec["source_type"],
        "audio_ref_final":          audio_final,
        "detected_channel_class":   rec["detected_channel_class"],
        "phone_confidence_factor":  rec["phone_confidence_factor"],
        "recommended_model":        rec["recommended_model"],
        "norm_status":              rec["norm_status"],
        "snr_improvement_db":       rec["snr_improvement_db"],
    })
    producer.flush()

def emit_confidence_calibration(producer, rec):
    producer.send("audio.phone.confidence_calibrated", value={
        "job_id":                 rec["job_id"],
        "session_id":             rec["session_id"],
        "phone_confidence_factor": rec["phone_confidence_factor"],
        "factor_rationale":       rec["factor_rationale"],
        "detected_channel_class": rec["detected_channel_class"],
        "norm_status":            rec["norm_status"],
    })
    producer.flush()

def log_fingerprint(db, job_id, channel_class, signals, conf):
    with db.cursor() as cur:
        cur.execute("""INSERT INTO codec_fingerprint_log
            (job_id, detected_class, detection_signals, detection_method, detection_ms)
            VALUES (%s,%s,%s,%s,%s)""",
            (job_id, channel_class, json.dumps(signals),
             signals.get("method", "spectral"), 0))
    db.commit()

def load_norm_rules(db):
    with db.cursor() as cur:
        cur.execute("SELECT channel_class, steps_json, recommended_model, "
                    "confidence_factor_base, factor_snr_bonus, factor_snr_floor_db, "
                    "bandwidth_extend FROM phone_norm_rules WHERE active=TRUE")
        rows = cur.fetchall()
    return {r[0]: {
        "steps_json": r[1], "recommended_model": r[2],
        "confidence_factor_base": r[3], "factor_snr_bonus": r[4],
        "factor_snr_floor_db": r[5], "bandwidth_extend": r[6]
    } for r in rows} if rows else {}

if __name__ == "__main__":
    run()
```

Absence of fingerprint_codec, detect_packet_loss, normalize, compute_confidence_factor
functions → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-K — KAFKA EVENT CONTRACTS (R4 ALIGNED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# AsyncAPI 2.6.0 — PHONE_CONFIDENCE_NORMALIZATION_AGENT Events

channels:

  audio.phone.normalized:
    publish:
      summary: Phone audio normalized — cleared for STT pipeline
      message:
        payload:
          job_id:                   uuid
          session_id:               uuid
          source_type:              string
          audio_ref_final:          string    # MinIO path to normalized file
          detected_channel_class:   string    # GSM_NB|AMR_NB|AMR_WB|etc.
          phone_confidence_factor:  float     # 0.60–1.00
          recommended_model:        string    # Whisper model variant hint
          norm_status:              string    # SUCCESS|PARTIAL
          snr_improvement_db:       float
    consumers:
      - stt-worker-service    # STT_WORKER_AUTOSCALING_AGENT
                              # Uses audio_ref_final + recommended_model

  audio.phone.norm_failed:
    publish:
      summary: Normalization failed — raw audio forwarded to STT as fallback
      message:
        payload:
          job_id:                   uuid
          session_id:               uuid
          audio_ref_final:          string    # ORIGINAL (un-normalized) ref
          detected_channel_class:   string
          phone_confidence_factor:  float     # always 0.60 (floor)
          norm_status:              string    # always "FAILED"
    consumers:
      - stt-worker-service          # processes raw audio (degraded quality expected)
      - admin-ops-console           # alert in Phone Quality panel

  audio.phone.confidence_calibrated:
    publish:
      summary: Confidence factor emitted for STT_CONFIDENCE_MONITOR calibration
      message:
        payload:
          job_id:                   uuid
          session_id:               uuid
          phone_confidence_factor:  float     # 0.60–1.00
          factor_rationale:         string    # human-readable explanation
          detected_channel_class:   string
          norm_status:              string
    consumers:
      - stt-confidence-monitor-service   # STT_CONFIDENCE_MONITOR_AGENT
                                         # Uses factor to relax verdict thresholds
                                         # for phone-origin audio

  audio.phone.batch_alert:
    publish:
      summary: Multiple phone audio jobs degraded — Jitsi/coturn issue?
      message:
        payload:
          alert_type:               string    # GSM_SPIKE|PKT_LOSS_SURGE|
                                              # BANDWIDTH_DEGRADATION
          source_type:              string
          channel_class:            string
          affected_count:           integer
          avg_confidence_factor:    float
          severity:                 string
    consumers:
      - admin-ops-console
      - notification-service

# Consumed BY this agent:
  audio.quality.passed [phone_flag=TRUE]:
    subscribe:
      summary: Phone-flagged audio from AUDIO_QUALITY_SCORE_AGENT
      source:  Redis Stream ecoskiller:audio:phone_norm_queue
```

Absence of audio.phone.confidence_calibrated event → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-L — STT_CONFIDENCE_MONITOR INTEGRATION CONTRACT
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
HOW phone_confidence_factor IS APPLIED IN STT_CONFIDENCE_MONITOR_AGENT:

  RULE: For any transcript where phone_confidence_factor is present
        (i.e., the job passed through this agent), the verdict thresholds
        in STT_CONFIDENCE_MONITOR_AGENT are multiplied by the factor
        to make them more lenient for phone-origin audio.

  Adjusted threshold formula:
    effective_threshold = base_threshold × phone_confidence_factor
                          (already divided by source_criticality_multiplier)

  Examples:
    phone_confidence_factor = 0.78 (GSM_NB after normalization):
      PASS threshold: 0.75 × 0.78 = 0.585 (relaxed from 0.75)
      REVIEW threshold: 0.55 × 0.78 = 0.429

    phone_confidence_factor = 0.92 (CLEAN_MOBILE):
      PASS threshold: 0.75 × 0.92 = 0.690 (slightly relaxed)
      REVIEW threshold: 0.55 × 0.92 = 0.506

    phone_confidence_factor = 0.60 (norm_failed — floor):
      PASS threshold: 0.75 × 0.60 = 0.450 (significantly relaxed)
      → Human review strongly recommended for any REVIEW/BLOCK here

  DUAL MODIFIER STACK (both applied independently):
    source_criticality_multiplier  → RAISES thresholds (critical sources = stricter)
    phone_confidence_factor        → LOWERS thresholds (phone audio = more lenient)
    Combined: threshold = base / criticality_mult × phone_confidence_factor

  phone_confidence_factor DEFAULT (no phone norm applied):
    If phone_confidence_factor not present in transcript event → default = 1.00
    (No relaxation — standard wideband audio)

  PERSISTENCE:
    phone_confidence_factor stored in confidence_records table as
    additional column: phone_confidence_factor FLOAT DEFAULT 1.00
    (STT_CONFIDENCE_MONITOR_AGENT schema extension — add-only migration)
```

Absence of phone_confidence_factor field in confidence_records schema
→ STOP EXECUTION → ADD-ONLY MIGRATION REQUIRED

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-M — PROMETHEUS METRICS & ALERTS
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
MANDATORY METRICS (exposed on :9093/metrics):

  pn_channel_class_total{channel_class}
    Type: Counter — Detection counts by codec class

  pn_confidence_factor (histogram 0.60–1.00)
    Type: Histogram — Distribution of phone_confidence_factor values

  pn_snr_improvement_db (histogram -2 to +20)
    Type: Histogram — SNR gain from normalization

  pn_normalization_ms (histogram)
    Type: Histogram — Processing duration (target p95 < 8000ms)

  pn_bandwidth_extension_total{success="true|false"}
    Type: Counter — BWE attempts and outcomes

  pn_norm_failures_total{channel_class}
    Type: Counter — Normalization failures by codec class

  pn_packet_loss_pct (histogram 0–50%)
    Type: Histogram — Packet loss % distribution

  pn_queue_depth
    Type: Gauge — Jobs in phone_norm_queue

  pn_low_factor_rate_1h
    Type: Gauge — % of jobs with phone_confidence_factor < 0.70 (rolling 1h)
    Target: < 10% (high rate = coturn / network problem)

REQUIRED GRAFANA ALERT RULES:

  - alert: PN_GSMSpike
    expr: rate(pn_channel_class_total{channel_class="GSM_NB"}[10m]) > 5
    for: 5m
    severity: HIGH
    summary: GSM_NB detections spiking — 2G mobile users increasing rapidly

  - alert: PN_PacketLossSurge
    expr: histogram_quantile(0.75, pn_packet_loss_pct_bucket) > 15
    for: 5m
    severity: CRITICAL
    summary: Packet loss > 15% at p75 — coturn / network infrastructure issue

  - alert: PN_NormFailureRate
    expr: rate(pn_norm_failures_total[10m]) > 2
    for: 5m
    severity: HIGH
    summary: Normalization failures exceeding 2/min — speechbrain or ffmpeg issue

  - alert: PN_LowFactorRate
    expr: pn_low_factor_rate_1h > 0.20
    for: 10m
    severity: HIGH
    summary: > 20% of phone jobs have factor < 0.70 — systematic quality degradation

  - alert: PN_BWEFailureRate
    expr: rate(pn_bandwidth_extension_total{success="false"}[10m]) > 0.5
    for: 5m
    severity: MEDIUM
    summary: Bandwidth extension failures — speechbrain model load issue?

  - alert: PN_SlowNormalization
    expr: histogram_quantile(0.95, pn_normalization_ms_bucket) > 15000
    for: 5m
    severity: MEDIUM
    summary: Phone normalization p95 > 15s — CPU contention or large file
```

Absence of pn_channel_class_total or pn_confidence_factor metrics → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-N — KUBERNETES DEPLOYMENT MANIFEST
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# File: /infra/k8s/production/audio/phone-confidence-norm-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: phone-confidence-norm
  namespace: ecoskiller-audio
  labels:
    app: phone-confidence-norm
    layer: antigravity
spec:
  replicas: 2
  selector:
    matchLabels:
      app: phone-confidence-norm
  template:
    metadata:
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "9093"
        vault.hashicorp.com/agent-inject: "true"
        vault.hashicorp.com/role: "audio-phone-norm"
    spec:
      containers:
        - name: phone-confidence-norm
          image: harbor.ecoskiller.internal/ecoskiller/phone-confidence-norm:latest
          ports:
            - containerPort: 9093    # Prometheus
            - containerPort: 8080    # FastAPI health/REST
          resources:
            requests:
              cpu: "1500m"           # speechbrain BWE is CPU-intensive
              memory: "3Gi"
            limits:
              cpu: "4"
              memory: "8Gi"          # speechbrain model + audio buffers
          env:
            - name: REDIS_HOST
              valueFrom:
                configMapKeyRef:
                  name: audio-config
                  key: redis_host
            - name: KAFKA_BROKERS
              valueFrom:
                configMapKeyRef:
                  name: audio-config
                  key: kafka_brokers
            - name: DB_DSN
              valueFrom:
                secretKeyRef:
                  name: audio-db-secret
                  key: dsn
          livenessProbe:
            httpGet:
              path: /audio/phone-norm/health
              port: 8080
            initialDelaySeconds: 60    # speechbrain model warm-up
            periodSeconds: 20
          readinessProbe:
            httpGet:
              path: /audio/phone-norm/health
              port: 8080
            initialDelaySeconds: 45
            periodSeconds: 15
      terminationGracePeriodSeconds: 180   # allow long normalization to complete

---
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: phone-confidence-norm-hpa
  namespace: ecoskiller-audio
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: phone-confidence-norm
  minReplicas: 1
  maxReplicas: 6
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 55    # speechbrain needs headroom
```

```dockerfile
# File: /backend/services/phone_confidence_norm/Dockerfile
FROM python:3.11-slim
RUN apt-get update && apt-get install -y \
    ffmpeg libsndfile1 \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . .
EXPOSE 9093 8080
CMD ["python", "main.py"]
```

```
# requirements.txt
librosa==0.10.2
noisereduce==3.0.2
soundfile==0.12.1
scipy==1.13.0
numpy==1.26.4
speechbrain==1.0.0
kafka-python==2.0.2
psycopg2-binary==2.9.9
redis==5.0.1
prometheus-client==0.20.0
opentelemetry-sdk==1.24.0
fastapi==0.111.0
uvicorn==0.29.0
```

Memory note: speechbrain model (~400MB) pre-loaded at startup.
Increase initialDelaySeconds if cold start > 45s in staging.

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-O — SEED DATA (REQUIRED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```sql
INSERT INTO phone_norm_rules
  (channel_class, steps_json, recommended_model,
   confidence_factor_base, factor_snr_bonus, factor_snr_floor_db,
   bandwidth_extend, description, active)
VALUES
  ('GSM_NB',      '["gsm_demusical","bandwidth_extend","loudnorm"]',
   'medium',  0.78, 0.04, 10.0, TRUE,
   'GSM narrowband — musicalness removal + bandwidth extension', TRUE),

  ('AMR_NB',      '["frame_erasure_conceal","click_remove","bandwidth_extend","noise_reduce","loudnorm"]',
   'medium',  0.78, 0.04, 10.0, TRUE,
   'AMR narrowband — frame erasure concealment + click removal + BWE', TRUE),

  ('AMR_WB',      '["pre_echo_suppress","noise_reduce","loudnorm"]',
   'small',   0.88, 0.04, 12.0, FALSE,
   'AMR wideband — pre-echo suppression + light denoising', TRUE),

  ('OPUS_LOW_BR', '["pre_echo_suppress","transient_sharpen","noise_reduce","loudnorm"]',
   'small',   0.74, 0.04, 8.0,  FALSE,
   'Low-bitrate OPUS — pre-echo + transient restoration', TRUE),

  ('G711_PCMU',   '["wiener_filter","bandwidth_extend","loudnorm"]',
   'medium',  0.82, 0.04, 10.0, TRUE,
   'G.711 PCM — quantization noise removal + BWE', TRUE),

  ('PACKET_LOSS', '["packet_loss_conceal","noise_reduce","loudnorm"]',
   'medium',  0.76, 0.04, 10.0, FALSE,
   'Packet-loss artifacts — interpolative concealment', TRUE),

  ('CLEAN_MOBILE','["light_noise_reduce","loudnorm"]',
   'base',    0.92, 0.04, 15.0, FALSE,
   'Clean mobile audio — light processing only', TRUE);

-- Schema extension on confidence_records (STT_CONFIDENCE_MONITOR_AGENT)
ALTER TABLE confidence_records
  ADD COLUMN IF NOT EXISTS phone_confidence_factor FLOAT DEFAULT 1.00,
  ADD COLUMN IF NOT EXISTS detected_channel_class   TEXT;
```

Absence of seed data → STOP EXECUTION
Absence of confidence_records schema extension → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-P — ADMIN OPS CONSOLE MODULE (R40 ALIGNED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
REQUIRED CONSOLE MODULE: Phone Channel Quality Panel

  Module Path: System Operations → Audio Processing → Phone Channel

  ── LIVE CODEC DASHBOARD ──────────────────────────────────────────

    1. Codec Class Distribution (donut chart — rolling 24h)
       GSM_NB | AMR_NB | AMR_WB | OPUS_LOW_BR |
       G711_PCMU | PACKET_LOSS | CLEAN_MOBILE
       Insight: shows ECOSKILLER's real-world network tier distribution

    2. Phone Confidence Factor Distribution (histogram)
       Shows how many users are on poor vs adequate phone channels
       Alert threshold: > 20% of jobs factor < 0.70

    3. SNR Improvement Trend (line chart by channel class)
       Shows normalization effectiveness over time

    4. Packet Loss Heatmap (by session / time)
       Identifies coturn configuration issues or regional network problems

    5. Bandwidth Extension Success Rate
       speechbrain BWE success/failure trend

    6. Regional Network Quality Table (by session geography, if available)
       Maps poor codec classes to geographic patterns

  ── NORMALIZATION FAILURE QUEUE ────────────────────────────────────

    7. Normalization Failures Table
       Columns: job_id | source | codec_class | snr | error | age
       Actions:
         - View full fingerprint report
         - Retry normalization (re-enqueue)
         - Discard (forward raw to STT with floor factor)
         - Escalate (BLOCK from STT — admin review required)

  ── NORM RULES EDITOR ──────────────────────────────────────────────

    8. Phone Norm Rules Table (live edit — per channel class)
       Editable: confidence_factor_base | factor_snr_floor_db |
                 recommended_model | bandwidth_extend
       All edits require justification + audit log

    9. Phone Flag Trigger Rules (live edit)
       Editable: bitrate_kbps threshold | score_bandwidth threshold
       for triggering phone_norm_queue routing

  ── PLATFORM HEALTH INSIGHT ────────────────────────────────────────

   10. "Phone Tier Summary" (admin insight card)
       Displays: % of ECOSKILLER audio traffic from each network tier
       Purpose: Informs infrastructure decisions (coturn capacity,
                Society offline expansion, bandwidth requirements)

  All console actions:
    ✔ Super Admin RBAC + MFA (R40 Section B)
    ✔ All edits emitted to audit_logs
    ✔ Rule changes cache-invalidated in Redis immediately

  Absence of Phone Channel Quality Panel in Ops Console → STOP EXECUTION
```

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-Q — INTERN EXECUTION STEPS (R26/R46 ALIGNED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
INTERN ROLE: Python Backend Developer + Basic Kubernetes Intern
PREREQUISITE: AUDIO_QUALITY_SCORE_AGENT deployed (phone_flag routing active)

STEP 1 — Database migrations
  alembic upgrade head
  Expected: phone_norm_records, phone_norm_rules,
            codec_fingerprint_log, phone_alerts created
  + confidence_records schema extended with phone_confidence_factor column

STEP 2 — Seed phone norm rules (7 rules)
  python seed_phone_norm_rules.py
  Expected: 7 channel class rules inserted

STEP 3 — Build Docker image
  docker build -t phone-confidence-norm:local .
  minikube image load phone-confidence-norm:local
  Note: speechbrain downloads model (~400MB) on first run

STEP 4 — Apply manifests
  kubectl apply -f /infra/k8s/dev/audio/phone-confidence-norm-deployment.yaml
  Expected: 2 pods Running (allow 60s warm-up for speechbrain)

STEP 5 — Test with AMR_NB audio
  python /tools/audio/seed_phone_queue.py --codec amr_nb --source mentor_feedback
  Expected: AMR_NB detected → Pipeline 2 applied → factor ≈ 0.80–0.85

STEP 6 — Test with GSM_NB audio
  python /tools/audio/seed_phone_queue.py --codec gsm --source student_voice
  Expected: GSM_NB detected → BWE applied → factor ≈ 0.82–0.88

STEP 7 — Test with packet loss simulation
  python /tools/audio/seed_phone_queue.py --packet-loss 15 --source gd_session
  Expected: PACKET_LOSS detected → concealment applied → factor ≈ 0.76

STEP 8 — Verify Kafka events
  kafka-console-consumer.sh --topic audio.phone.normalized --from-beginning
  Expected: normalized events with phone_confidence_factor visible

  kafka-console-consumer.sh --topic audio.phone.confidence_calibrated
  Expected: factor events consumed by STT_CONFIDENCE_MONITOR_AGENT

STEP 9 — Verify database records
  psql ecoskiller -c "SELECT detected_channel_class, phone_confidence_factor,
    snr_improvement_db FROM phone_norm_records ORDER BY evaluated_at DESC LIMIT 5;"

STEP 10 — Check Prometheus metrics
  kubectl port-forward pod/<pod-name> 9093:9093 -n ecoskiller-audio
  curl http://localhost:9093/metrics | grep pn_

SUCCESS CONDITIONS:
  ✔ 7 channel classes detectable (AMR_NB, GSM_NB, PACKET_LOSS verified)
  ✔ phone_confidence_factor emitted for all normalized jobs
  ✔ audio.phone.normalized events on Kafka
  ✔ audio.phone.confidence_calibrated events on Kafka
  ✔ Prometheus metrics visible on :9093
  ✔ REST API: GET /audio/phone-norm/stats responding

Failure at any step → STOP EXECUTION
```

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION PN-R — PRODUCTION READINESS CHECKLIST
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
No PHONE_CONFIDENCE_NORMALIZATION_AGENT may be declared production-ready unless:

  ✔ phone_norm_records, phone_norm_rules, codec_fingerprint_log,
    phone_alerts tables migrated
  ✔ confidence_records extended with phone_confidence_factor column
  ✔ 7 phone norm rules seeded (one per channel class)
  ✔ Redis Stream ecoskiller:audio:phone_norm_queue consumer group active
  ✔ AUDIO_QUALITY_SCORE_AGENT updated to emit phone_flag=TRUE routing
  ✔ All 4 Kafka output topics provisioned:
      audio.phone.normalized / audio.phone.norm_failed /
      audio.phone.confidence_calibrated / audio.phone.batch_alert
  ✔ Harbor image registry (NOT GHCR — v8 audit #1)
  ✔ Forgejo Actions CI/CD (NOT GitHub Actions — v8 audit #1)
  ✔ 2-replica deployment live, HPA active (min=1, max=6)
  ✔ speechbrain model pre-loaded (BWE active for GSM_NB, AMR_NB, G711_PCMU)
  ✔ All 7 normalization pipelines tested individually
  ✔ Packet loss concealment tested (up to 60ms gap)
  ✔ Codec fingerprinting tested for all 7 classes
  ✔ phone_confidence_factor range verified: 0.60–1.00
  ✔ STT_CONFIDENCE_MONITOR_AGENT confirmed consuming
    audio.phone.confidence_calibrated and applying factor
  ✔ Prometheus metrics on :9093 active
  ✔ All 6 Grafana alert rules active
  ✔ Admin Ops Console Phone Channel Quality Panel deployed
  ✔ Contract validator (R49) passes all phone-norm APIs
  ✔ QA generator (R50) test suite passes
  ✔ AMR_NB audio from coturn-routed session tested end-to-end
  ✔ GSM_NB audio from student voice upload tested end-to-end

Failure → STOP EXECUTION → REPORT PN-AGENT-NOT-READY
```

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# 🔒 SEAL BLOCK
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
PHONE_CONFIDENCE_NORMALIZATION_AGENT — ANTIGRAVITY AUDIO PROCESSING
════════════════════════════════════════════════════════════════════

SYSTEM:                     ECOSKILLER Unified SaaS
LAYER:                      Audio Processing — Antigravity
AGENT REGISTRY POSITION:    #4 (Phone intercept layer)
AGENT VERSION:              v1.0.0
UPSTREAM AGENT:             AUDIO_QUALITY_SCORE_AGENT v1.0.0
DOWNSTREAM AGENTS:          STT_WORKER_AUTOSCALING_AGENT v1.0.0
                            STT_CONFIDENCE_MONITOR_AGENT v1.0.0
SEALED DATE:                2026-03-04
SEALED BY:                  Master Execution Prompt v12.0

PHONE NORM MODE:                BEST-EFFORT (not a hard gate)
FAILURE BEHAVIOR:               FORWARD RAW + factor=0.60 + alert admin
CODEC CLASSES HANDLED:          7 (GSM_NB|AMR_NB|AMR_WB|OPUS_LOW_BR|
                                   G711_PCMU|PACKET_LOSS|CLEAN_MOBILE)
FINGERPRINTING:                 DETERMINISTIC (ffprobe + spectral rules)
ML IN FINGERPRINTING:           FORBIDDEN (R28-1)
BANDWIDTH EXTENSION:            PERMITTED (speechbrain BWE — enhancement,
                                NOT a scoring/decision model — R28-1 scope)
PAID AUDIO APIs:                FORBIDDEN
GITHUB ACTIONS:                 FORBIDDEN (v8 audit fix #1)
GHCR:                           FORBIDDEN (v8 audit fix #1)
FORGEJO + HARBOR:               REQUIRED
PHONE_CONFIDENCE_FACTOR RANGE:  0.60 (floor) – 1.00 (wideband equivalent)
KAFKA OUTPUT TOPICS:            audio.phone.normalized
                                audio.phone.norm_failed
                                audio.phone.confidence_calibrated
                                audio.phone.batch_alert
NORMALIZATION PIPELINES:        7 (one per channel class — ALL REQUIRED)
BWE LIBRARY:                    speechbrain/speech-enhancement-metricgan-plus
CONFIDENCE INTEGRATION:         STT_CONFIDENCE_MONITOR_AGENT threshold
                                multiplied by phone_confidence_factor
OFFLINE SOURCE SUPPORT:         REQUIRED — R59 (workshop/rural mobile)
ADMIN OPS CONSOLE:              REQUIRED (R40)
INTERN EXECUTABLE:              REQUIRED (R46)
CONTRACT VALIDATOR GATE:        REQUIRED (R49)
QA TEST GENERATOR GATE:         REQUIRED (R50)
MUTATION POLICY:                ADD-ONLY VIA VERSION BUMP
INTERPRETATION AUTHORITY:       NONE
EXECUTION AUTHORITY:            HUMAN DECLARATION ONLY

Violation of any seal → STOP EXECUTION
→ REPORT PN-AGENT-SEAL-VIOLATION
→ NO DEPLOYMENT CLAIM PERMITTED
════════════════════════════════════════════════════════════════════
```
