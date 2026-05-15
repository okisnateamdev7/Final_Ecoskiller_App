# 🔒 AUDIO_QUALITY_SCORE_AGENT
## ECOSKILLER — Audio Processing Module · Antigravity Layer
### Status: FINAL · LOCKED · SEALED · GOVERNED · DETERMINISTIC
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only
### Agent Registry Position: Audio Processing Agent #3
### Sibling Agents:
###   [1] STT_WORKER_AUTOSCALING_AGENT v1.0.0
###   [2] STT_CONFIDENCE_MONITOR_AGENT v1.0.0

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-A — AGENT IDENTITY & PURPOSE DECLARATION
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
Agent Name:             AUDIO_QUALITY_SCORE_AGENT
Agent Class:            Signal Intelligence & Pre-Processing Agent
Domain:                 Audio Processing — Antigravity Subsystem
Parent System:          ECOSKILLER Unified SaaS Platform
Namespace:              ecoskiller-audio / antigravity / quality
Stack Alignment:        R1 (Python 3.11 · FastAPI · Redis · Kafka · PostgreSQL
                            · ClickHouse · MinIO)
Governance Alignment:   R1 · R10 · R16 · R21 · R25 · R28 · R38 · R39 · R40
                        · R45 · R50 · R59 (Offline-First)
Audit Alignment:        Ecoskiller v8 Infrastructure Audit (All 12 Issues Resolved)
                        — Jitsi + coturn confirmed stack (audit fix #6)
                        — Forgejo + Harbor CI/CD (audit fix #1)

Agent Position in Pipeline:
  AUDIO_QUALITY_SCORE_AGENT runs BEFORE the STT_WORKER_AUTOSCALING_AGENT.
  It is the entry gate of the entire audio processing pipeline.

  Raw Audio In → [AUDIO_QUALITY_SCORE_AGENT] → Routing Decision
                    ↓ PASS        → audio.quality.passed   → STT_WORKER_AUTOSCALING_AGENT
                    ↓ DEGRADE     → audio.quality.degraded → STT with downgraded model
                    ↓ ENHANCE     → audio.quality.enhance  → Pre-processing pipeline
                    ↓ REJECT      → audio.quality.rejected → Dead-letter, admin alert

Sibling Dependency Chain:
  This agent → STT_WORKER_AUTOSCALING_AGENT → STT_CONFIDENCE_MONITOR_AGENT

Determinism Rule:
  Identical audio signal features → Identical quality score and routing decision
Failure Mode:
  STOP → REPORT → NO AUDIO FORWARDED TO STT PIPELINE → ALERT ADMIN
```

Absence of AUDIO_QUALITY_SCORE_AGENT at pipeline entry
→ STOP EXECUTION → REPORT AQ-AGENT-MISSING → NO STT PROCESSING PERMITTED

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-B — ANTIGRAVITY QUALITY CONCEPT LOCK
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
Antigravity Audio Quality Definition:
  The AUDIO_QUALITY_SCORE_AGENT applies "antigravity" forces to every
  incoming audio file — lifting poor-quality signals up to an acceptable
  level through pre-processing before they reach the STT engine,
  rather than letting degraded audio fall through and produce bad
  transcripts that then cascade failures into scoring, certification,
  and GD evaluation.

  High quality  → zero friction pass through, no pre-processing overhead
  Degraded audio → intercepted, enhanced (noise reduction, normalization),
                   re-assessed, then forwarded
  Critical noise → rejected at gate, admin alerted, source flagged
  Silence/empty  → identified immediately, returned as REJECT,
                   no STT worker resources consumed

Audio Quality Dimensions Evaluated (6 Signal Pillars):
  1. SNR_SCORE           — Signal-to-Noise Ratio (dB)
  2. CLIPPING_SCORE      — Digital clipping / saturation detection
  3. SILENCE_RATIO_SCORE — Proportion of true silence in file
  4. FREQUENCY_SCORE     — Frequency completeness (speech band 80Hz–8kHz)
  5. BANDWIDTH_SCORE     — Bitrate and sample-rate adequacy
  6. SOURCE_CONTEXT_SCORE — Source type criticality modifier
                            (GD session > coach eval > background upload)

Antigravity Quality Contract:
  Input:   Raw audio file reference (MinIO path) + audio_jobs metadata
  Output:  quality_verdict (PASS | DEGRADE | ENHANCE | REJECT)
           + quality_score (0.00–1.00)
           + enhancement_actions[] (list of pre-processing steps applied)
           + routing_target (stt_queue | enhance_queue | dead_letter)

Critical System Rule:
  No audio file may enter the STT pipeline without a quality_verdict stamp.
  ENHANCE verdict = agent applies pre-processing IN-PLACE before forwarding.
  No STT worker slot may be consumed on a REJECT-class audio file.
```

Absence of quality_verdict gate before STT queue → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-C — AUDIO SOURCES & CONTEXT REGISTRY
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
ECOSKILLER AUDIO SOURCES (ALL MUST BE HANDLED):

  ┌─────────────────────┬──────────────────┬─────────────────────────────────┐
  │ source_type         │ origin_service   │ audio_characteristics           │
  ├─────────────────────┼──────────────────┼─────────────────────────────────┤
  │ gd_session          │ voice-gd-        │ WebRTC/Jitsi SRTP stream        │
  │                     │ orchestrator     │ OPUS codec, 48kHz, multi-       │
  │                     │                  │ speaker, mute enforcement       │
  │                     │                  │ 40–60 kbps upload per user      │
  │                     │                  │ echo cancel + noise suppression │
  │                     │                  │ applied by WebRTC layer         │
  ├─────────────────────┼──────────────────┼─────────────────────────────────┤
  │ dojo_match          │ dojo-service     │ LiveKit SFU, SRTP, OPUS 48kHz   │
  │                     │                  │ 1:1 or small group (2–6)        │
  │                     │                  │ mentor + candidate audio        │
  ├─────────────────────┼──────────────────┼─────────────────────────────────┤
  │ coach_eval          │ coach-service    │ Single speaker, likely mobile   │
  │                     │                  │ Variable codec/quality          │
  │                     │                  │ Network condition variable      │
  ├─────────────────────┼──────────────────┼─────────────────────────────────┤
  │ tournament_event    │ tournament-      │ Structured rounds, turn-based   │
  │                     │ service          │ high-stakes, strict timing      │
  ├─────────────────────┼──────────────────┼─────────────────────────────────┤
  │ workshop_session    │ workshop-service │ Batch recording, Society model  │
  │                     │                  │ Offline-sync, compressed upload │
  │                     │                  │ Low-bandwidth environments      │
  │                     │                  │ R59: offline-first compliant    │
  ├─────────────────────┼──────────────────┼─────────────────────────────────┤
  │ student_voice       │ enrollment-      │ Student solo submission         │
  │                     │ service          │ Mobile mic, variable quality    │
  ├─────────────────────┼──────────────────┼─────────────────────────────────┤
  │ arena_match         │ arena-service    │ WebRTC real-time, OPUS 48kHz    │
  ├─────────────────────┼──────────────────┼─────────────────────────────────┤
  │ mentor_feedback     │ coach-service    │ Async voice note, variable      │
  └─────────────────────┴──────────────────┴─────────────────────────────────┘

WebRTC Audio Stack (confirmed — AUTOMATED_VOICE_GD document + tech list):
  Transport:          SRTP (Secure RTP) — encrypted
  Codec:              OPUS (adaptive, 8kHz–48kHz, mono/stereo)
  Echo Cancellation:  Browser-native WebRTC AEC (Acoustic Echo Cancellation)
  Noise Suppression:  Browser-native WebRTC NS (Noise Suppression)
  SFU Routing:        Jitsi Videobridge (JVB) — GD sessions
                      LiveKit JVB — Dojo matches and interviews
  NAT Traversal:      coturn (STUN/TURN server)
  Bandwidth:          Upload: 40–60 kbps per user
                      Download: 150–250 kbps (5–6 user room)

Offline/Low-Bandwidth Source Handling (R59 aligned):
  workshop_session audio uploaded via offline sync:
    → Expected: lower sample rate, higher compression, possible gaps
    → ENHANCE path activated if SNR < threshold
    → REJECT only if file is corrupted or < 1 second
```

Absence of source context registry → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-D — TECHNOLOGY STACK LOCK
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
Agent Runtime:          Python 3.11 + FastAPI (R1 aligned)
Agent Container:        Docker image — ecoskiller/audio-quality-score:latest
Image Registry:         Harbor (harbor.ecoskiller.internal)
                        NOT GHCR (v8 Infrastructure Audit fix #1 — FORBIDDEN)
CI/CD:                  Forgejo Actions (v8 Infrastructure Audit fix #1)
                        NOT GitHub Actions (FORBIDDEN)

Audio Analysis Library: librosa 0.10.x (Apache 2.0 — OSS)
                        — FFT, spectral analysis, RMS, zero crossing rate
                        — mel spectrograms, frequency band energy
Signal Processing:      scipy 1.13.x (BSD 3-Clause)
                        — bandpass filtering, noise floor estimation
                        — peak detection, silence detection
Audio Enhancement:      noisereduce 3.x (MIT license — OSS)
                        — Spectral gating noise reduction
                        — Stationary + non-stationary noise modes
File I/O:               soundfile 0.12.x (BSD 3-Clause) — read/write WAV/FLAC
                        pydub 0.25.x (MIT) — format conversion (MP3/OPUS/WAV)
Format Conversion:      ffmpeg (LGPL/GPL — self-hosted binary)
                        — OPUS → WAV conversion (for analysis)
                        — Audio normalization (loudnorm filter)
                        — Bitrate inspection

Input Queue:            Redis Streams — ecoskiller:audio:quality_queue
                        Consumer group: quality_score_group
                        (UPSTREAM of ecoskiller:audio:stt_queue)

Output Queues (Kafka):
  audio.quality.passed    → to STT_WORKER_AUTOSCALING_AGENT
  audio.quality.degraded  → to STT with downgraded model hint
  audio.quality.enhance   → to enhancement sub-pipeline (async)
  audio.quality.rejected  → dead-letter + admin alert

Database (Primary):     PostgreSQL 15 (R1 aligned)
                        Tables: audio_quality_records, quality_rules,
                                enhancement_log, quality_alerts

Analytics Store:        ClickHouse — high-volume audio quality metrics
                        Table: audio_quality_metrics (append-only, partitioned by day)

Object Storage:         MinIO
                        Input bucket:    ecoskiller-audio-raw
                        Enhanced bucket: ecoskiller-audio-enhanced
                        Reports bucket:  ecoskiller-audio-quality-reports
                        Path pattern:    /quality/{session_id}/{job_id}/report.json

Cache:                  Redis 7 — quality rules cache (TTL = 300s)
                        Source context weights cache (TTL = 600s)

Orchestration:          Kubernetes Deployment
                        Namespace: ecoskiller-audio
                        Replicas: 2 (HA active-passive)
                        HPA: min=1, max=8 (CPU-triggered)
                        Resources: 1 CPU / 2Gi RAM per pod (librosa is CPU-intensive)

Observability:          Prometheus (:9092/metrics)
                        Grafana dashboard: Audio Quality Signals Board
                        Jaeger tracing: OpenTelemetry spans per job
                        Loki: structured logs (v8 audit fix #7)
```

Use of GitHub Actions or GHCR → STOP EXECUTION (v8 audit violation)
Use of paid audio analysis APIs (AssemblyAI, Deepgram) → STOP EXECUTION (OSS-only)
Absence of librosa, noisereduce, ffmpeg → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-E — FULL PIPELINE TOPOLOGY
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
┌─────────────────────────────────────────────────────────────────────┐
│             ECOSKILLER AUDIO SOURCES                                │
│                                                                     │
│  gd_session   dojo_match   coach_eval   tournament   workshop       │
│  student_voice  arena_match  mentor_feedback                        │
│                         │                                           │
│              AudioIngestGateway /audio/submit                       │
│              (validates metadata, stores raw to MinIO)              │
│                         │                                           │
│              Redis Stream: ecoskiller:audio:quality_queue           │
└──────────────────────────┬──────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────────┐
│              AUDIO_QUALITY_SCORE_AGENT                              │
│                                                                     │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  INGESTION & FORMAT NORMALIZER                               │  │
│  │  ffmpeg: OPUS/MP3/M4A → WAV 16kHz mono (analysis format)    │  │
│  │  Duration gate: < 0.5s → REJECT immediately                  │  │
│  │  File integrity: checksums, format validation                │  │
│  └────────────────────────┬─────────────────────────────────────┘  │
│                           │                                         │
│  ┌────────────────────────▼─────────────────────────────────────┐  │
│  │  6-PILLAR QUALITY SCORING ENGINE (RULE-BASED, R28-1)        │  │
│  │                                                              │  │
│  │  Pillar 1: SNR Estimator       (librosa + scipy)            │  │
│  │  Pillar 2: Clipping Detector   (numpy peak analysis)        │  │
│  │  Pillar 3: Silence Ratio Calc  (librosa RMS gate)           │  │
│  │  Pillar 4: Frequency Scorer    (librosa FFT band energy)    │  │
│  │  Pillar 5: Bandwidth Scorer    (ffprobe bitrate inspection)  │  │
│  │  Pillar 6: Source Context      (weight multiplier from DB)  │  │
│  │                    │                                        │  │
│  │        Composite Quality Score (0.00–1.00)                  │  │
│  │        Quality Verdict Gate                                  │  │
│  └────────────────────────┬─────────────────────────────────────┘  │
│                           │                                         │
│  ┌────────────────────────▼─────────────────────────────────────┐  │
│  │  ENHANCEMENT ENGINE (on ENHANCE verdict only)                │  │
│  │                                                              │  │
│  │  Step A: Noise Reduction    (noisereduce spectral gating)    │  │
│  │  Step B: Volume Normalize   (ffmpeg loudnorm filter)         │  │
│  │  Step C: Silence Trim       (librosa trim)                   │  │
│  │  Step D: Bandpass Filter    (scipy butter bandpass 80–8kHz)  │  │
│  │  Step E: Re-score           (re-run 6-pillar engine)         │  │
│  │  If re-score still REJECT → BLOCK, do not forward           │  │
│  └────────────────────────┬─────────────────────────────────────┘  │
│                           │                                         │
│  ┌────────────────────────▼─────────────────────────────────────┐  │
│  │  VERDICT ROUTER                                              │  │
│  │                                                              │  │
│  │  PASS    → audio.quality.passed  → STT_WORKER queue         │  │
│  │  DEGRADE → audio.quality.degraded → STT (downgraded model)  │  │
│  │  ENHANCE → Enhancement Engine → re-score → route again      │  │
│  │  REJECT  → audio.quality.rejected → dead-letter             │  │
│  └────────────────────────┬─────────────────────────────────────┘  │
│                           │                                         │
│  ┌────────────────────────▼─────────────────────────────────────┐  │
│  │  PERSISTENCE & ANALYTICS                                    │  │
│  │  PostgreSQL: audio_quality_records                          │  │
│  │  ClickHouse: audio_quality_metrics (analytics)              │  │
│  │  MinIO: /quality/{session_id}/{job_id}/report.json          │  │
│  │  Kafka: audio.quality.* events                              │  │
│  │  Prometheus: aq_* metrics on :9092                          │  │
│  └──────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────┘
         │              │               │             │
         ▼              ▼               ▼             ▼
  STT_WORKER_    admin-ops-      notification-  audit-service
  AUTOSCALING_   console         service        (REJECT events)
  AGENT
```

Absence of Enhancement Engine or Verdict Router → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-F — DATABASE SCHEMA (MANDATORY)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```sql
-- ── Audio Quality Records ────────────────────────────────────────────────
CREATE TABLE audio_quality_records (
    quality_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_id              UUID NOT NULL REFERENCES audio_jobs(job_id),
    session_id          UUID NOT NULL,
    source_type         TEXT NOT NULL,   -- gd_session|dojo_match|coach_eval|
                                         -- tournament_event|workshop_session|
                                         -- student_voice|arena_match|mentor_feedback

    -- Raw signal properties (measured before any enhancement)
    file_format         TEXT NOT NULL,   -- wav|opus|mp3|m4a|flac
    file_size_bytes     BIGINT,
    duration_sec        FLOAT NOT NULL,
    sample_rate_hz      INT,             -- 8000|16000|22050|44100|48000
    bit_depth           INT,             -- 16|24|32
    channels            INT,             -- 1=mono, 2=stereo
    bitrate_kbps        FLOAT,

    -- 6-Pillar Scores (0.00–1.00 each, measured pre-enhancement)
    score_snr           FLOAT NOT NULL,  -- Pillar 1: Signal-to-Noise Ratio
    score_clipping      FLOAT NOT NULL,  -- Pillar 2: Clipping / saturation
    score_silence       FLOAT NOT NULL,  -- Pillar 3: Silence ratio
    score_frequency     FLOAT NOT NULL,  -- Pillar 4: Frequency completeness
    score_bandwidth     FLOAT NOT NULL,  -- Pillar 5: Bitrate/samplerate
    score_src_context   FLOAT NOT NULL,  -- Pillar 6: Source weight multiplier

    -- Raw measured values (for audit and analytics)
    snr_db              FLOAT,           -- Measured SNR in decibels
    clipping_pct        FLOAT,           -- % of samples clipping (> 0.99 amplitude)
    silence_pct         FLOAT,           -- % of file that is silence
    speech_pct          FLOAT,           -- % of file with detected speech
    freq_band_80_300    FLOAT,           -- Energy in 80–300 Hz band (low speech)
    freq_band_300_3400  FLOAT,           -- Energy in 300–3400 Hz band (core speech)
    freq_band_3400_8000 FLOAT,           -- Energy in 3.4–8 kHz band (high speech)

    -- Composite result
    composite_score     FLOAT NOT NULL,  -- Weighted final score 0.00–1.00
    verdict             TEXT NOT NULL,   -- PASS|DEGRADE|ENHANCE|REJECT
    routing_target      TEXT NOT NULL,   -- stt_queue|stt_downgrade|enhance_queue|dead_letter

    -- Enhancement tracking
    enhanced            BOOLEAN DEFAULT FALSE,
    enhancement_actions JSONB,           -- List of steps applied
    enhanced_file_ref   TEXT,            -- MinIO path to enhanced file (if applicable)
    post_enhance_score  FLOAT,           -- Composite score AFTER enhancement
    post_enhance_verdict TEXT,           -- Verdict AFTER enhancement

    -- Processing metadata
    analysis_duration_ms INT,            -- How long quality scoring took
    evaluated_at        TIMESTAMP DEFAULT NOW(),
    resolved_at         TIMESTAMP,       -- Set when REJECT reviewed by admin
    resolved_by         UUID
);

-- ── Quality Scoring Rules (admin-configurable via Ops Console) ───────────
CREATE TABLE quality_rules (
    rule_id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    rule_name           TEXT UNIQUE NOT NULL,
    pillar              TEXT NOT NULL,   -- SNR|CLIPPING|SILENCE|FREQUENCY|BANDWIDTH
    threshold_pass      FLOAT NOT NULL,
    threshold_degrade   FLOAT NOT NULL,
    threshold_enhance   FLOAT NOT NULL,
    -- below threshold_enhance → REJECT
    weight              FLOAT NOT NULL,  -- Pillar weight in composite
    active              BOOLEAN DEFAULT TRUE,
    updated_at          TIMESTAMP DEFAULT NOW(),
    updated_by          UUID
);

-- ── Source Context Quality Weights ──────────────────────────────────────
CREATE TABLE audio_source_quality_weights (
    source_type         TEXT PRIMARY KEY,
    criticality_label   TEXT NOT NULL,   -- CRITICAL|HIGH|STANDARD|BACKGROUND
    threshold_multiplier FLOAT NOT NULL, -- Tightens or relaxes all thresholds
    min_duration_sec    FLOAT NOT NULL,  -- Below this → REJECT regardless of quality
    max_duration_sec    FLOAT NOT NULL,  -- Above this → flag for admin review
    allow_enhance       BOOLEAN NOT NULL -- TRUE = try enhancement before REJECT
);

-- ── Enhancement Log ──────────────────────────────────────────────────────
CREATE TABLE enhancement_log (
    enhancement_id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_id              UUID NOT NULL,
    quality_id          UUID NOT NULL REFERENCES audio_quality_records(quality_id),
    step_name           TEXT NOT NULL,   -- noise_reduce|normalize|silence_trim|
                                         -- bandpass_filter|resample
    step_order          INT NOT NULL,
    pre_step_score      FLOAT,
    post_step_score     FLOAT,
    duration_ms         INT,
    success             BOOLEAN NOT NULL,
    error_msg           TEXT,
    created_at          TIMESTAMP DEFAULT NOW()
);

-- ── Quality Alerts ───────────────────────────────────────────────────────
CREATE TABLE audio_quality_alerts (
    alert_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    alert_type          TEXT NOT NULL,   -- SOURCE_DEGRADED|BATCH_LOW_QUALITY|
                                         -- ENHANCEMENT_FAILURE|CODEC_ANOMALY
    source_type         TEXT,
    session_id          UUID,
    affected_count      INT,
    avg_quality_score   FLOAT,
    severity            TEXT NOT NULL,   -- CRITICAL|HIGH|MEDIUM|LOW
    alert_message       TEXT NOT NULL,
    acknowledged        BOOLEAN DEFAULT FALSE,
    acknowledged_by     UUID,
    created_at          TIMESTAMP DEFAULT NOW()
);

-- ── ClickHouse Analytics Table (audio_quality_metrics) ──────────────────
-- Append-only, partitioned by toYYYYMMDD(timestamp)
-- Schema (ClickHouse MergeTree):
--   job_id UUID, session_id UUID, source_type String,
--   snr_db Float32, clipping_pct Float32, silence_pct Float32,
--   composite_score Float32, verdict String, enhanced UInt8,
--   analysis_duration_ms UInt32, timestamp DateTime
```

Absence of audio_quality_records or quality_rules tables → STOP EXECUTION
Absence of ClickHouse analytics table → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-G — 6-PILLAR QUALITY SCORING ENGINE (CORE ALGORITHM)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
RULE ENGINE — DETERMINISTIC (R28-1 COMPLIANT — NO ML IN SCORING PATH)
All thresholds are configurable via quality_rules table.
Default values shown below.

═══════════════════════════════════════════════════════════════════
PILLAR 1 — SNR ESTIMATOR (Signal-to-Noise Ratio)
═══════════════════════════════════════════════════════════════════

  Library:  librosa + scipy
  Method:   Hybrid Percentile + VAD-based SNR estimation

  Algorithm:
    1. Compute RMS energy per 25ms frame (librosa.feature.rms)
    2. Identify noise floor: 10th percentile of frame energies
    3. Identify signal floor: 90th percentile of frame energies
    4. SNR_dB = 20 × log10(signal_energy / noise_energy)
    5. Clamp SNR_dB to range [-10, 40] for scoring

  Score Mapping (configurable):
    SNR_dB ≥ 25  → score_snr = 1.00  (studio / WebRTC-processed quality)
    SNR_dB ≥ 15  → score_snr = 0.80  (good quality — acceptable for STT)
    SNR_dB ≥ 10  → score_snr = 0.60  (medium quality — DEGRADE hint)
    SNR_dB ≥ 5   → score_snr = 0.40  (poor quality — ENHANCE required)
    SNR_dB < 5   → score_snr = 0.20  (very poor — likely REJECT)

  Context: WebRTC GD/Dojo sessions should arrive with SNR ≥ 20 dB
           (echo cancellation + noise suppression applied by browser).
           Workshop offline uploads may have SNR as low as 8–12 dB.

═══════════════════════════════════════════════════════════════════
PILLAR 2 — CLIPPING DETECTOR
═══════════════════════════════════════════════════════════════════

  Library:  numpy
  Method:   Peak amplitude analysis on normalized [-1.0, +1.0] waveform

  Algorithm:
    1. Load audio as float32 array (soundfile)
    2. clipped_samples = count(|sample| > CLIP_THRESHOLD)
       CLIP_THRESHOLD = 0.985 (slight headroom below 0 dBFS)
    3. clipping_pct = clipped_samples / total_samples × 100

  Score Mapping:
    clipping_pct = 0.0%   → score_clipping = 1.00  (no clipping)
    clipping_pct ≤ 0.1%   → score_clipping = 0.90  (negligible)
    clipping_pct ≤ 0.5%   → score_clipping = 0.70  (minor clipping)
    clipping_pct ≤ 2.0%   → score_clipping = 0.50  (noticeable — ENHANCE)
    clipping_pct ≤ 10.0%  → score_clipping = 0.25  (severe — likely REJECT)
    clipping_pct > 10.0%  → score_clipping = 0.05  (completely saturated)

═══════════════════════════════════════════════════════════════════
PILLAR 3 — SILENCE RATIO SCORER
═══════════════════════════════════════════════════════════════════

  Library:  librosa
  Method:   RMS energy gate with adaptive threshold

  Algorithm:
    1. Compute RMS per 20ms frame
    2. SILENCE_GATE = max(RMS_mean × 0.1, ABSOLUTE_FLOOR)
       ABSOLUTE_FLOOR = 0.001 (prevents over-detection in noisy files)
    3. silent_frames = count(frame_rms < SILENCE_GATE)
    4. silence_pct = silent_frames / total_frames × 100
    5. speech_pct = 100 - silence_pct

  Score Mapping:
    silence_pct ≤ 10%  → score_silence = 1.00  (mostly speech — excellent)
    silence_pct ≤ 25%  → score_silence = 0.90  (normal pauses — good)
    silence_pct ≤ 45%  → score_silence = 0.70  (moderate pauses — OK)
    silence_pct ≤ 65%  → score_silence = 0.45  (heavy silence — DEGRADE)
    silence_pct ≤ 85%  → score_silence = 0.20  (near-empty — ENHANCE/REJECT)
    silence_pct > 85%  → score_silence = 0.05  (empty/background only)

  Special Rule:
    IF silence_pct > 95% AND duration_sec < 2.0 → FORCE REJECT
    (Empty recording — no STT resources to be consumed)

═══════════════════════════════════════════════════════════════════
PILLAR 4 — FREQUENCY COMPLETENESS SCORER
═══════════════════════════════════════════════════════════════════

  Library:  librosa FFT (Short-Time Fourier Transform)
  Method:   Speech frequency band energy distribution analysis

  Speech Frequency Bands:
    LOW:  80–300 Hz   (fundamentals, vowels, male voice fundamentals)
    CORE: 300–3400 Hz (core intelligibility band — critical for STT)
    HIGH: 3400–8000 Hz (fricatives, sibilants — s, f, sh sounds)

  Algorithm:
    1. Compute STFT: librosa.stft(y, n_fft=2048, hop_length=512)
    2. Compute magnitude spectrum
    3. Map frequency bins to band ranges
    4. Compute energy proportion for each band
    5. core_ratio = CORE_band_energy / total_speech_band_energy
       (300–3400 Hz should dominate in clean speech)

  Score Mapping:
    core_ratio ≥ 0.60  → score_frequency = 1.00 (full speech spectrum)
    core_ratio ≥ 0.45  → score_frequency = 0.80 (slight roll-off)
    core_ratio ≥ 0.30  → score_frequency = 0.60 (bandwidth limited)
    core_ratio ≥ 0.20  → score_frequency = 0.40 (heavily filtered — ENHANCE)
    core_ratio < 0.20  → score_frequency = 0.20 (not speech / pure noise)

  Context: OPUS codec at 40kbps preserves core speech band well.
           Compressed offline uploads may show heavy roll-off.

═══════════════════════════════════════════════════════════════════
PILLAR 5 — BANDWIDTH ADEQUACY SCORER
═══════════════════════════════════════════════════════════════════

  Library:  ffprobe (via subprocess) + soundfile
  Method:   Sample rate + bit depth + bitrate inspection

  Algorithm:
    sample_rate = soundfile.info(path).samplerate
    bitrate = ffprobe json output → format.bit_rate / 1000  (kbps)
    bit_depth = soundfile.info(path).subtype_info

  Score Mapping (sample_rate component — 40% weight):
    sample_rate ≥ 16000 → sr_score = 1.00 (Whisper minimum met)
    sample_rate = 8000  → sr_score = 0.60 (narrow-band — DEGRADE hint)
    sample_rate < 8000  → sr_score = 0.30 (sub-standard)

  Score Mapping (bitrate component — 60% weight):
    bitrate ≥ 32 kbps → br_score = 1.00 (OPUS 32kbps = excellent for speech)
    bitrate ≥ 16 kbps → br_score = 0.80 (acceptable)
    bitrate ≥ 8 kbps  → br_score = 0.55 (minimal — DEGRADE)
    bitrate < 8 kbps  → br_score = 0.25 (heavily compressed — ENHANCE)

  formula:
    score_bandwidth = (0.4 × sr_score) + (0.6 × br_score)

  Auto-Resample Rule:
    IF sample_rate ≠ 16000 → ffmpeg resample to 16000 Hz for Whisper
    This is always performed regardless of quality score (silent step)

═══════════════════════════════════════════════════════════════════
PILLAR 6 — SOURCE CONTEXT WEIGHT MODIFIER
═══════════════════════════════════════════════════════════════════

  Source:   audio_source_quality_weights table (Redis cached TTL=600s)

  Context Table (seeded defaults):
  ┌──────────────────────┬─────────────┬───────────────┬──────────────────┐
  │ source_type          │ criticality │ threshold_mult│ allow_enhance    │
  ├──────────────────────┼─────────────┼───────────────┼──────────────────┤
  │ tournament_event     │ CRITICAL    │ 1.30          │ FALSE            │
  │ dojo_match           │ CRITICAL    │ 1.25          │ TRUE             │
  │ gd_session           │ HIGH        │ 1.15          │ TRUE             │
  │ coach_eval           │ HIGH        │ 1.10          │ TRUE             │
  │ arena_match          │ HIGH        │ 1.10          │ TRUE             │
  │ workshop_session     │ STANDARD    │ 1.00          │ TRUE             │
  │ student_voice        │ STANDARD    │ 1.00          │ TRUE             │
  │ mentor_feedback      │ BACKGROUND  │ 0.85          │ TRUE             │
  └──────────────────────┴─────────────┴───────────────┴──────────────────┘

  tournament_event + allow_enhance=FALSE:
    → REJECT immediately if below ENHANCE threshold
    → No enhancement attempted on tournament audio (integrity preservation)
    → Admin must manually approve re-submission

═══════════════════════════════════════════════════════════════════
COMPOSITE QUALITY SCORE & VERDICT GATE
═══════════════════════════════════════════════════════════════════

  DEFAULT PILLAR WEIGHTS (admin-configurable via quality_rules):
    W1 (snr)          = 0.35  (most important — overall signal health)
    W2 (clipping)     = 0.15  (distortion detection)
    W3 (silence)      = 0.20  (speech density)
    W4 (frequency)    = 0.15  (spectral completeness)
    W5 (bandwidth)    = 0.15  (encoding adequacy)
    SUM               = 1.00

  COMPOSITE FORMULA:
    raw_score       = (W1×P1) + (W2×P2) + (W3×P3) + (W4×P4) + (W5×P5)
    composite_score = clamp(raw_score, 0.00, 1.00)

  EFFECTIVE THRESHOLDS (base ÷ threshold_multiplier from Pillar 6):
    STANDARD source (multiplier = 1.00):
      composite ≥ 0.72  → PASS     → audio.quality.passed
      composite ≥ 0.50  → DEGRADE  → audio.quality.degraded (downgrade STT model)
      composite ≥ 0.30  → ENHANCE  → run enhancement engine, re-score
      composite < 0.30  → REJECT   → audio.quality.rejected

    CRITICAL source (tournament, multiplier = 1.30) — thresholds raised:
      composite ≥ 0.94  → PASS
      composite ≥ 0.65  → DEGRADE
      composite ≥ 0.39  → REJECT (no enhancement attempted)
      (allow_enhance=FALSE for tournament — any sub-PASS goes to REJECT)

    HIGH source (GD session, multiplier = 1.15):
      composite ≥ 0.83  → PASS
      composite ≥ 0.58  → DEGRADE
      composite ≥ 0.35  → ENHANCE
      composite < 0.35  → REJECT

    BACKGROUND source (mentor_feedback, multiplier = 0.85):
      composite ≥ 0.61  → PASS
      composite ≥ 0.43  → DEGRADE
      composite ≥ 0.26  → ENHANCE
      composite < 0.26  → REJECT

  HARD OVERRIDE RULES (applied BEFORE composite score):
    IF duration_sec < 0.5                          → FORCE REJECT
    IF silence_pct > 95% AND duration_sec < 2.0    → FORCE REJECT
    IF clipping_pct > 25%                          → FORCE REJECT
    IF file format unreadable by ffprobe           → FORCE REJECT
    IF sample_rate < 4000 Hz                       → FORCE REJECT

  DEGRADE ROUTING TABLE (model downgrade hints forwarded to STT worker):
    composite 0.50–0.64  → model hint: "base" (from tiny if tiny was planned)
    composite 0.40–0.49  → model hint: "small" (slower, more tolerant)
    composite 0.30–0.39  → model hint: "medium" (most robust for degraded audio)
    (model upgrade is handled by STT_WORKER_AUTOSCALING_AGENT on receipt)
```

Absence of any Hard Override Rule → STOP EXECUTION
Absence of DEGRADE routing table → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-H — ENHANCEMENT ENGINE (REQUIRED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
ENHANCEMENT PIPELINE (applied only when verdict = ENHANCE)
Ordered steps — each step is logged to enhancement_log table.

  ─────────────────────────────────────────────────────
  Step A: Noise Reduction (noisereduce)
  ─────────────────────────────────────────────────────
  Library: noisereduce 3.x (MIT license)
  Method:  Spectral gating — stationary noise mode

  Algorithm:
    1. Estimate noise profile from first 0.5s (or quietest 10% of file)
    2. Apply spectral subtraction with gating threshold
    3. noisereduce.reduce_noise(y=audio, sr=sr, stationary=True)
  Expected gain: +5 to +15 dB SNR improvement

  ─────────────────────────────────────────────────────
  Step B: Volume Normalization (ffmpeg loudnorm)
  ─────────────────────────────────────────────────────
  ffmpeg -i input.wav -af loudnorm=I=-16:TP=-1.5:LRA=11 output.wav
  Target: EBU R128 standard, integrated loudness -16 LUFS
  Prevents both under-gain (weak signal) and clipping

  ─────────────────────────────────────────────────────
  Step C: Silence Trimming (librosa)
  ─────────────────────────────────────────────────────
  librosa.effects.trim(y, top_db=30)
  Trims leading and trailing silence exceeding 30dB below peak
  Minimum post-trim duration: 1.0 second (else REJECT)

  ─────────────────────────────────────────────────────
  Step D: Bandpass Filter (scipy)
  ─────────────────────────────────────────────────────
  Applied only if score_frequency < 0.45 (Pillar 4 failure)
  scipy.signal.butter(order=4, Wn=[80, 8000], btype='bandpass', fs=sr)
  Removes sub-bass rumble and extreme high-frequency noise
  Preserves 80Hz–8kHz speech intelligibility band

  ─────────────────────────────────────────────────────
  Step E: Resample to 16kHz (ffmpeg — ALWAYS applied)
  ─────────────────────────────────────────────────────
  ffmpeg -i input.wav -ar 16000 -ac 1 output_16k.wav
  Whisper requires 16kHz mono — performed silently on all files
  Not logged as enhancement step (it is always a normalization step)

  ─────────────────────────────────────────────────────
  POST-ENHANCEMENT RE-SCORE
  ─────────────────────────────────────────────────────
  After all enhancement steps:
  1. Re-run all 6 pillars on enhanced file
  2. Compute new composite score (post_enhance_score)
  3. Apply verdict gate again (post_enhance_verdict)
  4. IF post_enhance_verdict = REJECT → FORCE REJECT (no retry)
  5. IF post_enhance_verdict = PASS/DEGRADE → forward to STT queue
     with enhanced file reference

  ENHANCEMENT LIMITS:
    Max enhancement attempts per job: 1 (no iterative loops)
    If enhancement fails (exception): mark REJECT, alert admin
    Enhancement duration limit: 60 seconds per file
    Files > 4 hours cannot be enhanced (memory limit) → segment then enhance
```

Absence of noise reduction or post-enhancement re-score → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-I — AGENT SERVICE CODE (PYTHON — REQUIRED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```python
# File: /backend/services/audio_quality_score/main.py
# Purpose: Score incoming audio quality, enhance if needed, route to STT pipeline
# Stack: Python 3.11 · librosa · noisereduce · scipy · ffmpeg · Redis · Kafka

import os, json, uuid, time, subprocess, tempfile
import numpy as np
import librosa, soundfile as sf, noisereduce as nr
from scipy.signal import butter, sosfilt
from kafka import KafkaProducer
import redis, psycopg2
from prometheus_client import Counter, Histogram, Gauge, start_http_server

# ── Prometheus Metrics ───────────────────────────────────────────────────
AQ_VERDICTS     = Counter('aq_verdicts_total', 'Quality verdicts', ['verdict'])
AQ_SCORES       = Histogram('aq_composite_score', 'Composite quality scores',
                             buckets=[.1,.2,.3,.4,.5,.6,.7,.8,.9,1.0])
AQ_SNR_DB       = Histogram('aq_snr_db', 'Measured SNR in dB',
                             buckets=[-10,-5,0,5,10,15,20,25,30,40])
AQ_SILENCE_PCT  = Histogram('aq_silence_pct', 'Silence percentage',
                             buckets=[5,10,20,30,40,50,60,70,80,90,100])
AQ_ENHANCE_RATE = Counter('aq_enhancement_total', 'Files enhanced', ['success'])
AQ_REJECT_RATE  = Counter('aq_reject_total', 'Files rejected', ['source_type'])
AQ_EVAL_MS      = Histogram('aq_evaluation_ms', 'Analysis duration (ms)')
AQ_REVIEW_DEPTH = Gauge('aq_rejected_pending_review', 'REJECT records pending admin')
P1 = Histogram('aq_p1_snr',       'Pillar 1 scores', buckets=[.1,.2,.3,.4,.5,.6,.7,.8,.9,1.0])
P2 = Histogram('aq_p2_clipping',  'Pillar 2 scores', buckets=[.1,.2,.3,.4,.5,.6,.7,.8,.9,1.0])
P3 = Histogram('aq_p3_silence',   'Pillar 3 scores', buckets=[.1,.2,.3,.4,.5,.6,.7,.8,.9,1.0])
P4 = Histogram('aq_p4_frequency', 'Pillar 4 scores', buckets=[.1,.2,.3,.4,.5,.6,.7,.8,.9,1.0])
P5 = Histogram('aq_p5_bandwidth', 'Pillar 5 scores', buckets=[.1,.2,.3,.4,.5,.6,.7,.8,.9,1.0])

# ── Config ───────────────────────────────────────────────────────────────
REDIS_HOST     = os.getenv("REDIS_HOST", "redis-service.ecoskiller-infra")
KAFKA_BROKERS  = os.getenv("KAFKA_BROKERS", "kafka.ecoskiller-infra:9092")
DB_DSN         = os.getenv("DB_DSN")
MINIO_ENDPOINT = os.getenv("MINIO_ENDPOINT")
STREAM_KEY     = "ecoskiller:audio:quality_queue"
CONSUMER_GROUP = "quality_score_group"
CONSUMER_NAME  = f"aq-worker-{uuid.uuid4().hex[:8]}"
CLIP_THRESHOLD = 0.985
ABSOLUTE_FLOOR = 0.001


def run():
    start_http_server(9092)
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
    rules   = load_rules(db, r)
    weights = load_source_weights(db, r)
    print(f"[AQ] AUDIO_QUALITY_SCORE_AGENT started: {CONSUMER_NAME}")

    while True:
        msgs = r.xreadgroup(CONSUMER_GROUP, CONSUMER_NAME,
                            {STREAM_KEY: '>'}, count=1, block=5000)
        if not msgs:
            continue
        for _, entries in msgs:
            for msg_id, data in entries:
                t0 = time.time()
                try:
                    result = evaluate_and_route(data, db, r, producer, rules, weights)
                    ms = int((time.time() - t0) * 1000)
                    AQ_EVAL_MS.observe(ms)
                    r.xack(STREAM_KEY, CONSUMER_GROUP, msg_id)
                except Exception as e:
                    print(f"[AQ ERROR] job={data.get('job_id')} err={e}")


def evaluate_and_route(data, db, r, producer, rules, weights):
    job_id      = data["job_id"]
    session_id  = data["session_id"]
    source_type = data["source_type"]
    audio_ref   = data["audio_ref"]   # MinIO path → resolved to local temp file

    ctx = weights.get(source_type, {"criticality_label":"STANDARD",
                                    "threshold_multiplier":1.0,
                                    "allow_enhance":True})

    with tempfile.TemporaryDirectory() as tmpdir:
        raw_path = fetch_audio(audio_ref, tmpdir)             # download from MinIO
        wav_path = convert_to_wav16k(raw_path, tmpdir)        # ffmpeg → 16kHz mono WAV
        props    = get_file_properties(raw_path, wav_path)    # duration, sr, bitrate

        # Hard override checks (before scoring)
        reject_reason = hard_override_check(props)
        if reject_reason:
            result = build_reject_result(job_id, session_id, source_type,
                                         props, reject_reason, ctx)
            persist(result, db)
            route(result, producer, db)
            return result

        # Load waveform for analysis
        y, sr = librosa.load(wav_path, sr=None, mono=True)

        # 6-Pillar scoring
        p1, snr_db                          = score_snr(y, sr)
        p2, clipping_pct                    = score_clipping(y)
        p3, silence_pct, speech_pct         = score_silence(y, sr)
        p4, freq_bands                      = score_frequency(y, sr)
        p5                                  = score_bandwidth(props)
        source_mult                         = ctx["threshold_multiplier"]

        # Prometheus
        P1.observe(p1); P2.observe(p2); P3.observe(p3)
        P4.observe(p4); P5.observe(p5)
        AQ_SNR_DB.observe(snr_db)
        AQ_SILENCE_PCT.observe(silence_pct)

        # Composite
        w = rules["weights"]
        composite = w[1]*p1 + w[2]*p2 + w[3]*p3 + w[4]*p4 + w[5]*p5
        composite = max(0.0, min(1.0, composite))
        AQ_SCORES.observe(composite)

        # Verdict
        t   = rules["thresholds"]
        verdict = compute_verdict(composite, source_mult, t,
                                  ctx["allow_enhance"])

        # Enhancement path
        enhanced = False
        enhancement_actions = []
        enhanced_file_ref   = None
        post_score = None
        post_verdict = None

        if verdict == "ENHANCE" and ctx["allow_enhance"]:
            enh_path, actions = enhance_audio(y, sr, tmpdir, p1, p2, p3, p4, job_id, db)
            enhanced = True
            enhancement_actions = actions
            if enh_path:
                AQ_ENHANCE_RATE.labels(success="true").inc()
                y2, sr2 = librosa.load(enh_path, sr=None, mono=True)
                props2   = get_file_properties(enh_path, enh_path)
                p1b, _   = score_snr(y2, sr2)
                p2b, _   = score_clipping(y2)
                p3b, _, _ = score_silence(y2, sr2)
                p4b, _   = score_frequency(y2, sr2)
                p5b      = score_bandwidth(props2)
                post_score = w[1]*p1b + w[2]*p2b + w[3]*p3b + w[4]*p4b + w[5]*p5b
                post_score = max(0.0, min(1.0, post_score))
                post_verdict = compute_verdict(post_score, source_mult, t,
                                               allow_enhance=False)
                enhanced_file_ref = upload_enhanced(enh_path, job_id, session_id)
                verdict = post_verdict
            else:
                AQ_ENHANCE_RATE.labels(success="false").inc()
                verdict = "REJECT"

        AQ_VERDICTS.labels(verdict=verdict).inc()
        if verdict == "REJECT":
            AQ_REJECT_RATE.labels(source_type=source_type).inc()
            AQ_REVIEW_DEPTH.inc()

        result = {
            "quality_id":           str(uuid.uuid4()),
            "job_id":               job_id,
            "session_id":           session_id,
            "source_type":          source_type,
            "file_format":          props["format"],
            "file_size_bytes":      props["size_bytes"],
            "duration_sec":         props["duration"],
            "sample_rate_hz":       props["sample_rate"],
            "bitrate_kbps":         props["bitrate_kbps"],
            "score_snr":            p1, "score_clipping":  p2,
            "score_silence":        p3, "score_frequency": p4,
            "score_bandwidth":      p5, "score_src_context": source_mult,
            "snr_db":               snr_db,
            "clipping_pct":         clipping_pct,
            "silence_pct":          silence_pct,
            "composite_score":      composite,
            "verdict":              verdict,
            "routing_target":       verdict_to_target(verdict),
            "enhanced":             enhanced,
            "enhancement_actions":  json.dumps(enhancement_actions),
            "enhanced_file_ref":    enhanced_file_ref,
            "post_enhance_score":   post_score,
            "post_enhance_verdict": post_verdict,
            "audio_ref_final":      enhanced_file_ref or audio_ref,
        }
        persist(result, db)
        route(result, producer, db)
        return result


def score_snr(y, sr):
    frame_len = int(sr * 0.025)  # 25ms frames
    rms = librosa.feature.rms(y=y, frame_length=frame_len, hop_length=frame_len//2)[0]
    rms = rms[rms > 0]
    if len(rms) < 2:
        return 0.1, -10.0
    noise_rms  = np.percentile(rms, 10)
    signal_rms = np.percentile(rms, 90)
    if noise_rms < 1e-8:
        return 1.0, 40.0
    snr_db = 20 * np.log10(signal_rms / noise_rms)
    snr_db = float(np.clip(snr_db, -10, 40))
    if snr_db >= 25:   score = 1.00
    elif snr_db >= 15: score = 0.80
    elif snr_db >= 10: score = 0.60
    elif snr_db >= 5:  score = 0.40
    else:              score = 0.20
    return score, snr_db


def score_clipping(y):
    clipped  = np.sum(np.abs(y) > CLIP_THRESHOLD)
    clip_pct = (clipped / len(y)) * 100
    if   clip_pct == 0.0:  score = 1.00
    elif clip_pct <= 0.1:  score = 0.90
    elif clip_pct <= 0.5:  score = 0.70
    elif clip_pct <= 2.0:  score = 0.50
    elif clip_pct <= 10.0: score = 0.25
    else:                  score = 0.05
    return score, float(clip_pct)


def score_silence(y, sr):
    frame_len  = int(sr * 0.020)
    rms        = librosa.feature.rms(y=y, frame_length=frame_len,
                                      hop_length=frame_len)[0]
    gate       = max(float(np.mean(rms)) * 0.10, ABSOLUTE_FLOOR)
    silent     = np.sum(rms < gate)
    total      = len(rms)
    sil_pct    = (silent / total) * 100
    speech_pct = 100 - sil_pct
    if   sil_pct <= 10: score = 1.00
    elif sil_pct <= 25: score = 0.90
    elif sil_pct <= 45: score = 0.70
    elif sil_pct <= 65: score = 0.45
    elif sil_pct <= 85: score = 0.20
    else:               score = 0.05
    return score, float(sil_pct), float(speech_pct)


def score_frequency(y, sr):
    stft  = np.abs(librosa.stft(y, n_fft=2048, hop_length=512))
    freqs = librosa.fft_frequencies(sr=sr, n_fft=2048)
    def band_energy(fmin, fmax):
        mask = (freqs >= fmin) & (freqs <= fmax)
        return float(np.mean(stft[mask]))
    low  = band_energy(80,   300)
    core = band_energy(300,  3400)
    high = band_energy(3400, 8000)
    total = low + core + high + 1e-9
    core_ratio = core / total
    bands = {"low": low, "core": core, "high": high}
    if   core_ratio >= 0.60: score = 1.00
    elif core_ratio >= 0.45: score = 0.80
    elif core_ratio >= 0.30: score = 0.60
    elif core_ratio >= 0.20: score = 0.40
    else:                    score = 0.20
    return score, bands


def score_bandwidth(props):
    sr = props.get("sample_rate", 16000)
    br = props.get("bitrate_kbps", 32)
    if   sr >= 16000: sr_s = 1.00
    elif sr == 8000:  sr_s = 0.60
    else:             sr_s = 0.30
    if   br >= 32: br_s = 1.00
    elif br >= 16: br_s = 0.80
    elif br >= 8:  br_s = 0.55
    else:          br_s = 0.25
    return (0.4 * sr_s) + (0.6 * br_s)


def compute_verdict(score, mult, t, allow_enhance=True):
    t_pass    = t["pass"]    / mult
    t_degrade = t["degrade"] / mult
    t_enhance = t["enhance"] / mult
    if   score >= t_pass:    return "PASS"
    elif score >= t_degrade: return "DEGRADE"
    elif score >= t_enhance and allow_enhance: return "ENHANCE"
    else:                    return "REJECT"


def hard_override_check(props):
    if props["duration"] < 0.5:                          return "DURATION_TOO_SHORT"
    if props.get("sample_rate", 16000) < 4000:           return "SAMPLE_RATE_TOO_LOW"
    if not props.get("readable"):                        return "FILE_UNREADABLE"
    return None


def enhance_audio(y, sr, tmpdir, p1, p2, p3, p4, job_id, db):
    actions = []
    try:
        # Step A: Noise reduction
        y = nr.reduce_noise(y=y, sr=sr, stationary=True)
        actions.append("noise_reduce")
        # Step C: Silence trim
        y, _ = librosa.effects.trim(y, top_db=30)
        if len(y) / sr < 1.0:
            return None, actions   # too short after trim
        actions.append("silence_trim")
        # Step D: Bandpass filter (only if frequency score low)
        if p4 < 0.45:
            sos = butter(4, [80/(sr/2), min(8000/(sr/2), 0.999)],
                         btype='bandpass', output='sos')
            y = sosfilt(sos, y)
            actions.append("bandpass_filter")
        # Step B: Normalize via temp WAV + ffmpeg loudnorm
        pre_norm = os.path.join(tmpdir, f"{job_id}_prenorm.wav")
        post_norm = os.path.join(tmpdir, f"{job_id}_enhanced.wav")
        sf.write(pre_norm, y, sr)
        subprocess.run(
            ["ffmpeg", "-y", "-i", pre_norm,
             "-af", "loudnorm=I=-16:TP=-1.5:LRA=11",
             post_norm], check=True, capture_output=True, timeout=60
        )
        actions.append("normalize")
        return post_norm, actions
    except Exception as e:
        return None, actions


def convert_to_wav16k(src, tmpdir):
    dst = os.path.join(tmpdir, "converted_16k.wav")
    subprocess.run(
        ["ffmpeg", "-y", "-i", src, "-ar", "16000", "-ac", "1", dst],
        check=True, capture_output=True, timeout=120
    )
    return dst


def get_file_properties(orig_path, wav_path):
    try:
        info = sf.info(wav_path)
        result = subprocess.run(
            ["ffprobe", "-v", "quiet", "-print_format", "json",
             "-show_format", "-show_streams", orig_path],
            capture_output=True, text=True, timeout=30
        )
        ff = json.loads(result.stdout)
        bitrate = int(ff.get("format", {}).get("bit_rate", 32000)) // 1000
        fmt     = ff.get("format", {}).get("format_name", "unknown")
        size    = int(ff.get("format", {}).get("size", 0))
        return {"duration": info.duration, "sample_rate": info.samplerate,
                "channels": info.channels, "format": fmt,
                "bitrate_kbps": bitrate, "size_bytes": size, "readable": True}
    except Exception:
        return {"duration": 0, "sample_rate": 0, "channels": 0,
                "format": "unknown", "bitrate_kbps": 0,
                "size_bytes": 0, "readable": False}


def verdict_to_target(verdict):
    return {"PASS": "stt_queue", "DEGRADE": "stt_downgrade",
            "ENHANCE": "enhance_queue", "REJECT": "dead_letter"}.get(verdict, "dead_letter")


def fetch_audio(audio_ref, tmpdir):
    # MinIO download stub — wired to MinIO SDK
    return os.path.join(tmpdir, "raw_audio")

def upload_enhanced(path, job_id, session_id):
    # MinIO upload stub → returns enhanced file ref
    return f"/enhanced/{session_id}/{job_id}/enhanced.wav"

def persist(result, db):
    with db.cursor() as cur:
        cur.execute("""
            INSERT INTO audio_quality_records
              (quality_id, job_id, session_id, source_type, file_format,
               file_size_bytes, duration_sec, sample_rate_hz, bitrate_kbps,
               score_snr, score_clipping, score_silence, score_frequency,
               score_bandwidth, score_src_context, snr_db, clipping_pct,
               silence_pct, composite_score, verdict, routing_target,
               enhanced, enhancement_actions, enhanced_file_ref,
               post_enhance_score, post_enhance_verdict)
            VALUES (%(quality_id)s, %(job_id)s, %(session_id)s, %(source_type)s,
                    %(file_format)s, %(file_size_bytes)s, %(duration_sec)s,
                    %(sample_rate_hz)s, %(bitrate_kbps)s,
                    %(score_snr)s, %(score_clipping)s, %(score_silence)s,
                    %(score_frequency)s, %(score_bandwidth)s, %(score_src_context)s,
                    %(snr_db)s, %(clipping_pct)s, %(silence_pct)s,
                    %(composite_score)s, %(verdict)s, %(routing_target)s,
                    %(enhanced)s, %(enhancement_actions)s, %(enhanced_file_ref)s,
                    %(post_enhance_score)s, %(post_enhance_verdict)s)
        """, result)
    db.commit()

def route(result, producer, db):
    verdict = result["verdict"]
    payload = {k: result[k] for k in
               ["job_id","session_id","source_type","composite_score",
                "verdict","routing_target","audio_ref_final",
                "snr_db","silence_pct","enhanced"]}
    if verdict in ("PASS", "DEGRADE"):
        topic = "audio.quality.passed" if verdict == "PASS" else "audio.quality.degraded"
        if verdict == "DEGRADE":
            payload["model_hint"] = degrade_model_hint(result["composite_score"])
        producer.send(topic, value=payload)
    elif verdict == "REJECT":
        producer.send("audio.quality.rejected", value=payload)

def degrade_model_hint(score):
    if score >= 0.50: return "base"
    if score >= 0.40: return "small"
    return "medium"

def load_rules(db, r):
    with db.cursor() as cur:
        cur.execute("SELECT pillar, weight, threshold_pass, threshold_degrade, "
                    "threshold_enhance FROM quality_rules WHERE active=TRUE")
        rows = cur.fetchall()
    PIDX = {"SNR":1,"CLIPPING":2,"SILENCE":3,"FREQUENCY":4,"BANDWIDTH":5}
    w = {}; tp, td, te = [], [], []
    for row in rows:
        idx = PIDX.get(row[0])
        if idx:
            w[idx] = row[1]; tp.append(row[2]); td.append(row[3]); te.append(row[4])
    return {"weights": w,
            "thresholds": {"pass": sum(tp)/len(tp), "degrade": sum(td)/len(td),
                           "enhance": sum(te)/len(te)}}

def load_source_weights(db, r):
    with db.cursor() as cur:
        cur.execute("SELECT source_type, criticality_label, threshold_multiplier, "
                    "allow_enhance FROM audio_source_quality_weights")
        rows = cur.fetchall()
    return {r[0]: {"criticality_label": r[1], "threshold_multiplier": r[2],
                   "allow_enhance": r[3]} for r in rows}

if __name__ == "__main__":
    run()
```

Absence of score_snr, score_clipping, score_silence, score_frequency,
score_bandwidth, enhance_audio functions → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-J — KAFKA EVENT CONTRACTS (R4 ALIGNED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# AsyncAPI 2.6.0 — AUDIO_QUALITY_SCORE_AGENT Events

channels:

  audio.quality.passed:
    publish:
      summary: Audio passed quality gate — forwarded to STT worker
      message:
        payload:
          job_id:           uuid
          session_id:       uuid
          source_type:      string
          composite_score:  float     # ≥ threshold_pass for source type
          verdict:          string    # always "PASS"
          audio_ref_final:  string    # MinIO path (original or enhanced)
          snr_db:           float
          silence_pct:      float
          enhanced:         boolean   # TRUE if enhancement was applied
    consumers:
      - stt-worker-service            # STT_WORKER_AUTOSCALING_AGENT

  audio.quality.degraded:
    publish:
      summary: Audio passed but with degraded quality — STT uses robust model
      message:
        payload:
          job_id:           uuid
          session_id:       uuid
          source_type:      string
          composite_score:  float
          verdict:          string    # always "DEGRADE"
          model_hint:       string    # base|small|medium
          audio_ref_final:  string
          snr_db:           float
          silence_pct:      float
          enhanced:         boolean
    consumers:
      - stt-worker-service            # uses model_hint to select Whisper variant

  audio.quality.rejected:
    publish:
      summary: Audio rejected — too poor for STT processing
      message:
        payload:
          job_id:           uuid
          session_id:       uuid
          source_type:      string
          composite_score:  float
          verdict:          string    # always "REJECT"
          reject_reason:    string    # which pillar/override caused rejection
          snr_db:           float
          silence_pct:      float
          clipping_pct:     float
    consumers:
      - admin-ops-console             # Rejected audio review queue
      - audit-service                 # Immutable audit record
      - notification-service          # Alert to session owner if high-stakes source
      - compliance-service            # Society domain: offline upload reject record

  audio.quality.batch_alert:
    publish:
      summary: Multiple files from same source degraded — systematic issue
      message:
        payload:
          alert_type:       string    # SOURCE_DEGRADED|CODEC_ANOMALY|BANDWIDTH_SPIKE
          source_type:      string
          session_id:       uuid
          affected_count:   integer
          avg_snr_db:       float
          avg_composite:    float
          time_window_min:  integer
          severity:         string
    consumers:
      - admin-ops-console
      - notification-service

# Consumed BY this agent:
  audio.quality.submitted:
    subscribe:
      summary: New audio job submitted for quality evaluation
      source:   AudioIngestGateway → Redis Stream quality_queue
```

Absence of audio.quality.passed event → STOP EXECUTION
Absence of audio.quality.degraded with model_hint → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-K — REST API CONTRACT (R3 ALIGNED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# OpenAPI 3.1 — AUDIO_QUALITY_SCORE_AGENT Endpoints

paths:

  /audio/quality/{job_id}:
    get:
      summary: Get full quality record for a job
      responses:
        "200":
          schema:
            properties:
              quality_id:       uuid
              composite_score:  float
              verdict:          string
              snr_db:           float
              silence_pct:      float
              clipping_pct:     float
              score_snr:        float
              score_clipping:   float
              score_silence:    float
              score_frequency:  float
              score_bandwidth:  float
              enhanced:         boolean
              enhancement_actions: array
              post_enhance_score: float

  /audio/quality/rejected:
    get:
      summary: List all rejected audio jobs pending admin review
      parameters:
        - source_type (query, optional)
        - severity (query, optional)
        - limit (query, default 50)
      responses:
        "200": { description: Paginated rejected audio list }

  /audio/quality/rejected/{quality_id}/resolve:
    post:
      summary: Admin resolves a rejected audio record
      requestBody:
        schema:
          required: [resolution, note]
          properties:
            resolution: string    # ACCEPT_ANYWAY | DISCARD | REQUEST_RESUBMIT
            note:       string
      responses:
        "200": { description: Resolved — downstream notified if ACCEPT_ANYWAY }

  /audio/quality/rules:
    get:  { summary: Get all active quality scoring rules }
    put:
      summary: Update quality rule thresholds (Super Admin only)
      responses:
        "200": { description: Rules updated — Redis cache invalidated }

  /audio/quality/source-weights:
    get:  { summary: Get source criticality weight table }
    put:  { summary: Update source weight (Super Admin only) }

  /audio/quality/stats:
    get:
      summary: Quality stats by source and time window
      parameters:
        - window_hours (default 24)
        - source_type (optional)
      responses:
        "200":
          schema:
            properties:
              total_evaluated:    integer
              pass_count:         integer
              degrade_count:      integer
              enhance_count:      integer
              reject_count:       integer
              pass_rate_pct:      float
              avg_snr_db:         float
              avg_composite:      float
              top_failure_pillar: string
              enhancement_success_rate: float

  /audio/quality/health:
    get:
      summary: Agent health, queue depth, last evaluation timestamp
      responses:
        "200": { description: Agent operational status }
```

Absence of /audio/quality/rejected endpoint → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-L — PROMETHEUS METRICS & ALERT RULES
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
MANDATORY METRICS (exposed on :9092/metrics):

  aq_verdicts_total{verdict="PASS|DEGRADE|ENHANCE|REJECT"}
    Type: Counter — total verdicts by type

  aq_composite_score (histogram 0.1–1.0)
    Type: Histogram — composite quality score distribution

  aq_snr_db (histogram -10 to 40)
    Type: Histogram — measured SNR in dB across all files

  aq_silence_pct (histogram 0–100)
    Type: Histogram — silence percentage distribution

  aq_p1_snr / aq_p2_clipping / aq_p3_silence /
  aq_p4_frequency / aq_p5_bandwidth
    Type: Histogram — individual pillar score distributions

  aq_enhancement_total{success="true|false"}
    Type: Counter — enhancement attempts and outcomes

  aq_reject_total{source_type}
    Type: Counter — rejections broken down by audio source

  aq_rejected_pending_review
    Type: Gauge — REJECT records awaiting admin resolution

  aq_evaluation_ms (histogram)
    Type: Histogram — analysis time per file (target p95 < 3000ms)

  aq_pass_rate_1h
    Type: Gauge — rolling 1-hour pass rate (target > 0.80)

REQUIRED GRAFANA ALERT RULES:

  - alert: AQ_PassRateCritical
    expr: aq_pass_rate_1h < 0.60
    for: 5m
    severity: CRITICAL
    summary: Audio quality pass rate below 60% — systematic degradation

  - alert: AQ_GDSessionRejectionSpike
    expr: rate(aq_reject_total{source_type="gd_session"}[5m]) > 2
    for: 3m
    severity: CRITICAL
    summary: GD session audio rejections spiking — Jitsi quality issue?

  - alert: AQ_TournamentRejection
    expr: aq_reject_total{source_type="tournament_event"} > 0
    for: 0m
    severity: CRITICAL
    summary: Tournament audio rejected — immediate admin action required

  - alert: AQ_EnhancementFailureRate
    expr: rate(aq_enhancement_total{success="false"}[10m]) > 0.3
    for: 5m
    severity: HIGH
    summary: Enhancement pipeline failing — noisereduce/ffmpeg issue?

  - alert: AQ_RejectedQueueOverflow
    expr: aq_rejected_pending_review > 50
    for: 10m
    severity: HIGH
    summary: Rejected audio queue exceeds 50 — admin review required

  - alert: AQ_SlowEvaluation
    expr: histogram_quantile(0.95, aq_evaluation_ms_bucket) > 5000
    for: 5m
    severity: MEDIUM
    summary: Quality evaluation p95 exceeds 5 seconds
```

Absence of aq_verdicts_total or aq_snr_db metrics → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-M — KUBERNETES DEPLOYMENT MANIFEST
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# File: /infra/k8s/production/audio/audio-quality-score-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: audio-quality-score
  namespace: ecoskiller-audio
  labels:
    app: audio-quality-score
    layer: antigravity
spec:
  replicas: 2
  selector:
    matchLabels:
      app: audio-quality-score
  template:
    metadata:
      labels:
        app: audio-quality-score
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "9092"
        vault.hashicorp.com/agent-inject: "true"
        vault.hashicorp.com/role: "audio-quality"
    spec:
      containers:
        - name: audio-quality-score
          image: harbor.ecoskiller.internal/ecoskiller/audio-quality-score:latest
          ports:
            - containerPort: 9092    # Prometheus
            - containerPort: 8080    # FastAPI health/REST
          resources:
            requests:
              cpu: "1"               # librosa is CPU-intensive
              memory: "2Gi"
            limits:
              cpu: "4"
              memory: "6Gi"          # large audio files in memory during analysis
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
              path: /audio/quality/health
              port: 8080
            initialDelaySeconds: 30
            periodSeconds: 15
          readinessProbe:
            httpGet:
              path: /audio/quality/health
              port: 8080
            initialDelaySeconds: 20
            periodSeconds: 10
      terminationGracePeriodSeconds: 120

---
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: audio-quality-score-hpa
  namespace: ecoskiller-audio
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: audio-quality-score
  minReplicas: 1
  maxReplicas: 8
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 60
```

```dockerfile
# File: /backend/services/audio_quality_score/Dockerfile
FROM python:3.11-slim
RUN apt-get update && apt-get install -y ffmpeg libsndfile1 && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . .
EXPOSE 9092 8080
CMD ["python", "main.py"]
```

```
# requirements.txt
librosa==0.10.2
noisereduce==3.0.2
soundfile==0.12.1
scipy==1.13.0
numpy==1.26.4
pydub==0.25.1
kafka-python==2.0.2
psycopg2-binary==2.9.9
redis==5.0.1
prometheus-client==0.20.0
opentelemetry-sdk==1.24.0
fastapi==0.111.0
uvicorn==0.29.0
```

Note: ffmpeg installed as system binary (apt-get), not pip package.
Absence of ffmpeg in Docker image → STOP EXECUTION (format conversion will fail)

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-N — ADMIN OPS CONSOLE MODULE (R40 ALIGNED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
REQUIRED CONSOLE MODULE: Audio Signal Quality Panel

  Module Path: System Operations → Audio Processing → Signal Quality

  ── LIVE SIGNAL DASHBOARD ──────────────────────────────────────────

    1. Live Pass Rate Gauge (rolling 1h / 6h / 24h)
       Alert banner if < 60% (CRITICAL)
       Alert banner if < 75% (WARNING)

    2. SNR Distribution Chart (histogram of measured dB values)
       Overlaid expected range for GD sessions (≥ 20 dB)
       Flags: % of files with SNR < 10 dB (poor quality)

    3. Silence Percentage Distribution (histogram)
       Flags: % of files > 50% silence

    4. Quality Verdict Donut (PASS / DEGRADE / ENHANCE / REJECT)

    5. Per-Source Quality Table:
       | source_type | total | pass% | degrade% | enhance% | reject% | avg_snr |
       Sortable, filterable, clickable per row

    6. Enhancement Pipeline Performance:
       Success rate, average SNR improvement, avg processing time

  ── REJECTED AUDIO REVIEW QUEUE ────────────────────────────────────

    7. Rejected Files Table
       Columns: job_id | source | snr_db | silence% | clipping% | score | age
       Actions per row:
         - Listen  (streams raw audio from MinIO — requires confirm)
         - View report (full 6-pillar JSON breakdown)
         - Accept Anyway  (override reject, forward to STT manually)
         - Discard  (confirm rejection, notify session owner)
         - Request Resubmit (send notification to original submitter)

    8. Bulk actions: Discard All (filtered) | Accept All (filtered)
       Both require Super Admin MFA confirmation

  ── QUALITY RULES EDITOR ───────────────────────────────────────────

    9. Quality Rules Table (live edit)
       Editable: threshold_pass | threshold_degrade |
                 threshold_enhance | pillar_weight
       All edits require justification text + audit log entry

   10. Source Quality Weights Table (live edit)
       Editable: threshold_multiplier | min_duration_sec |
                 max_duration_sec | allow_enhance

   11. Rule Change Audit Trail (immutable, last 30 days)

  ── QUALITY TREND ANALYTICS ────────────────────────────────────────

   12. SNR Trend (7-day rolling avg by source_type)
       Identifies degrading Jitsi / coturn configuration
       or worsening mobile upload quality

   13. Top Failure Pillar Daily Chart
       Which of the 5 pillars most frequently caused REJECT/DEGRADE

   14. GD Session Quality Heatmap (per batch / per day)

  All console actions:
    ✔ Super Admin RBAC + MFA required (R40 Section B)
    ✔ All edits emitted to audit_logs
    ✔ Accept Anyway decision: immutable override record created

  Absence of Audio Signal Quality Panel → STOP EXECUTION
```

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-O — SEED DATA (REQUIRED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```sql
-- Default quality scoring rules
INSERT INTO quality_rules
  (rule_name, pillar, threshold_pass, threshold_degrade, threshold_enhance, weight, active)
VALUES
  ('snr_standard',       'SNR',       0.72, 0.50, 0.30, 0.35, TRUE),
  ('clipping_standard',  'CLIPPING',  0.72, 0.50, 0.30, 0.15, TRUE),
  ('silence_standard',   'SILENCE',   0.72, 0.50, 0.30, 0.20, TRUE),
  ('frequency_standard', 'FREQUENCY', 0.72, 0.50, 0.30, 0.15, TRUE),
  ('bandwidth_standard', 'BANDWIDTH', 0.72, 0.50, 0.30, 0.15, TRUE);

-- Source quality weight registry
INSERT INTO audio_source_quality_weights
  (source_type, criticality_label, threshold_multiplier,
   min_duration_sec, max_duration_sec, allow_enhance)
VALUES
  ('tournament_event', 'CRITICAL',   1.30, 5.0,  7200.0, FALSE),
  ('dojo_match',       'CRITICAL',   1.25, 3.0, 14400.0, TRUE),
  ('gd_session',       'HIGH',       1.15, 5.0, 14400.0, TRUE),
  ('coach_eval',       'HIGH',       1.10, 3.0,  7200.0, TRUE),
  ('arena_match',      'HIGH',       1.10, 3.0,  7200.0, TRUE),
  ('workshop_session', 'STANDARD',   1.00, 1.0, 43200.0, TRUE),
  ('student_voice',    'STANDARD',   1.00, 1.0,  3600.0, TRUE),
  ('mentor_feedback',  'BACKGROUND', 0.85, 1.0,  1800.0, TRUE);
```

Absence of seed migration → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-P — INTERN EXECUTION STEPS (R26/R46 ALIGNED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
INTERN ROLE: Python Backend Developer + Basic Kubernetes Intern
PREREQUISITE: ecoskiller-audio namespace exists, Redis + Kafka running

OBJECTIVE: Deploy and test AUDIO_QUALITY_SCORE_AGENT locally

───────────────────────────────────────────────────────
STEP 1 — Run database migrations
───────────────────────────────────────────────────────
  cd /backend/services/audio_quality_score/
  alembic upgrade head

Expected: audio_quality_records, quality_rules,
          audio_source_quality_weights, enhancement_log,
          audio_quality_alerts tables created

───────────────────────────────────────────────────────
STEP 2 — Seed quality rules and source weights
───────────────────────────────────────────────────────
  python seed_quality_rules.py

Expected: 5 quality rules + 8 source weight rows inserted

───────────────────────────────────────────────────────
STEP 3 — Build Docker image (includes ffmpeg)
───────────────────────────────────────────────────────
  docker build -t audio-quality-score:local .
  minikube image load audio-quality-score:local

───────────────────────────────────────────────────────
STEP 4 — Apply manifests
───────────────────────────────────────────────────────
  kubectl apply -f /infra/k8s/dev/audio/audio-quality-score-deployment.yaml

Check pods:
  kubectl get pods -n ecoskiller-audio

Expected: audio-quality-score-* STATUS = Running (2 pods)

───────────────────────────────────────────────────────
STEP 5 — Test with a clean audio file (PASS path)
───────────────────────────────────────────────────────
  python /tools/audio/seed_quality_queue.py \
    --source gd_session --quality clean

Submits a clean 16kHz WAV to Redis Stream quality_queue.

───────────────────────────────────────────────────────
STEP 6 — Test with a noisy audio file (ENHANCE path)
───────────────────────────────────────────────────────
  python /tools/audio/seed_quality_queue.py \
    --source workshop_session --quality noisy

Expected: ENHANCE verdict → noise reduction applied → re-scored → PASS or DEGRADE

───────────────────────────────────────────────────────
STEP 7 — Test with silence file (REJECT path)
───────────────────────────────────────────────────────
  python /tools/audio/seed_quality_queue.py \
    --source student_voice --quality silence

Expected: FORCE REJECT (silence_pct > 95%)

───────────────────────────────────────────────────────
STEP 8 — Verify database records
───────────────────────────────────────────────────────
  psql ecoskiller -c \
    "SELECT source_type, verdict, snr_db, composite_score
     FROM audio_quality_records ORDER BY evaluated_at DESC LIMIT 5;"

───────────────────────────────────────────────────────
STEP 9 — Verify Kafka routing
───────────────────────────────────────────────────────
  kafka-console-consumer.sh \
    --bootstrap-server localhost:9092 \
    --topic audio.quality.passed --from-beginning

Expected: PASS/DEGRADE verdicts visible on topic

───────────────────────────────────────────────────────
STEP 10 — Check Prometheus metrics
───────────────────────────────────────────────────────
  kubectl port-forward pod/<pod-name> 9092:9092 -n ecoskiller-audio
  curl http://localhost:9092/metrics | grep aq_

Expected:
  aq_verdicts_total{verdict="PASS"} > 0
  aq_snr_db_bucket visible
  aq_composite_score_bucket visible
  aq_enhancement_total{success="true"} > 0

───────────────────────────────────────────────────────
SUCCESS CONDITIONS
───────────────────────────────────────────────────────
  ✔ audio_quality_records rows inserted for all test files
  ✔ PASS/DEGRADE events on audio.quality.passed Kafka topic
  ✔ REJECT events on audio.quality.rejected Kafka topic
  ✔ Enhancement log entries in enhancement_log table
  ✔ Prometheus metrics visible on :9092/metrics
  ✔ REST API responding: GET /audio/quality/stats

Failure at any step → STOP EXECUTION
```

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AQ-Q — PRODUCTION READINESS CHECKLIST
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
No AUDIO_QUALITY_SCORE_AGENT may be declared production-ready unless:

  ✔ audio_quality_records table migrated
  ✔ quality_rules seeded with 5 default rules
  ✔ audio_source_quality_weights seeded with all 8 source types
  ✔ ClickHouse audio_quality_metrics table provisioned
  ✔ Redis Stream ecoskiller:audio:quality_queue consumer group created
  ✔ All 4 Kafka output topics provisioned:
      audio.quality.passed / audio.quality.degraded /
      audio.quality.rejected / audio.quality.batch_alert
  ✔ Docker image includes ffmpeg system binary (apt-get)
  ✔ 2-replica deployment live in ecoskiller-audio namespace
  ✔ HPA active (min=1, max=8)
  ✔ Harbor image registry used (NOT GHCR — v8 audit #1)
  ✔ Forgejo Actions CI/CD pipeline used (NOT GitHub Actions — v8 audit #1)
  ✔ Prometheus metrics on :9092 scraping active
  ✔ All 6 Grafana alert rules active
  ✔ All 5 pillar scoring functions unit tested (R50)
  ✔ Enhancement pipeline tested end-to-end (noisy → enhanced → PASS)
  ✔ Hard override tests pass (silence → FORCE REJECT in < 100ms)
  ✔ Tournament source REJECT path tested (no enhancement attempted)
  ✔ model_hint correctly populated on DEGRADE Kafka events
  ✔ STT_WORKER_AUTOSCALING_AGENT confirmed consuming audio.quality.passed
  ✔ Admin Ops Console Audio Signal Quality Panel deployed
  ✔ Contract validator (R49) passes all quality APIs
  ✔ QA generator (R50) test suite 100% coverage
  ✔ GD session audio from Jitsi (OPUS/SRTP) correctly decoded via ffmpeg
  ✔ Offline workshop sync audio (compressed) correctly enhanced

Failure of any check → STOP EXECUTION → REPORT AQ-AGENT-NOT-READY
```

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# 🔒 SEAL BLOCK
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
AUDIO_QUALITY_SCORE_AGENT — ANTIGRAVITY AUDIO PROCESSING
════════════════════════════════════════════════════════════

SYSTEM:                   ECOSKILLER Unified SaaS
LAYER:                    Audio Processing — Antigravity (Entry Gate)
AGENT REGISTRY POSITION:  #1 of 3 (runs FIRST in pipeline)
AGENT VERSION:            v1.0.0
DOWNSTREAM AGENTS:        STT_WORKER_AUTOSCALING_AGENT v1.0.0
                          STT_CONFIDENCE_MONITOR_AGENT v1.0.0
SEALED DATE:              2026-03-04
SEALED BY:                Master Execution Prompt v12.0

PIPELINE GATE ROLE:       MANDATORY PRE-STT QUALITY GATE
STT WITHOUT GATE:         FORBIDDEN — no audio may enter STT queue
                          without quality_verdict stamp
QUALITY ENGINE:           ACTIVE — 6-PILLAR RULE ENGINE
ML IN SCORING:            FORBIDDEN (R28-1)
ENHANCEMENT ENGINE:       ACTIVE — noisereduce + ffmpeg + scipy
GITHUB ACTIONS:           FORBIDDEN (v8 audit fix #1)
GHCR:                     FORBIDDEN (v8 audit fix #1)
FORGEJO + HARBOR:         REQUIRED
OSS AUDIO LIBS:           librosa · noisereduce · scipy · soundfile · ffmpeg
PAID AUDIO APIs:          FORBIDDEN (AssemblyAI/Deepgram/etc)
TOURNAMENT ENHANCE:       FORBIDDEN (integrity preservation — allow_enhance=FALSE)
HARD REJECT OVERRIDES:    ACTIVE (duration < 0.5s / silence > 95% / clipping > 25%)
KAFKA OUTPUT TOPICS:      audio.quality.passed (cleared)
                          audio.quality.degraded (model_hint forwarded)
                          audio.quality.rejected (dead-letter)
                          audio.quality.batch_alert (batch degradation)
PILLAR WEIGHTS:           W1=0.35 W2=0.15 W3=0.20 W4=0.15 W5=0.15
AUDIO SOURCES COVERED:    gd_session · dojo_match · coach_eval ·
                          tournament_event · workshop_session ·
                          student_voice · arena_match · mentor_feedback
JITSI/WEBRTC HANDLING:    OPUS codec via ffmpeg decode (confirmed)
OFFLINE SYNC HANDLING:    R59 compliant — compressed uploads enhanced
ADMIN OPS CONSOLE:        REQUIRED (R40)
INTERN EXECUTABLE:        REQUIRED (R46)
CONTRACT VALIDATOR:       REQUIRED (R49)
QA TEST GENERATOR:        REQUIRED (R50)
MUTATION POLICY:          ADD-ONLY VIA VERSION BUMP
INTERPRETATION AUTHORITY: NONE
EXECUTION AUTHORITY:      HUMAN DECLARATION ONLY

Violation of any seal → STOP EXECUTION
→ REPORT AQ-AGENT-SEAL-VIOLATION
→ NO DEPLOYMENT CLAIM PERMITTED
════════════════════════════════════════════════════════════
```
