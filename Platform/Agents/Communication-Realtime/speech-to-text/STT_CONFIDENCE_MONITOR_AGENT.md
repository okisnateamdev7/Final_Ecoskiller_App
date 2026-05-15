# 🔒 STT_CONFIDENCE_MONITOR_AGENT
## ECOSKILLER — Audio Processing Module · Antigravity Layer
### Status: FINAL · LOCKED · SEALED · GOVERNED · DETERMINISTIC
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only
### Sibling Agent: STT_WORKER_AUTOSCALING_AGENT v1.0.0

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION CM-A — AGENT IDENTITY & PURPOSE DECLARATION
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
Agent Name:             STT_CONFIDENCE_MONITOR_AGENT
Agent Class:            Quality Assurance Intelligence Agent
Domain:                 Audio Processing — Antigravity Subsystem
Parent System:          ECOSKILLER Unified SaaS Platform
Namespace:              ecoskiller-audio / antigravity / confidence
Stack Alignment:        R1 (Python 3.11 · FastAPI · Redis · Kafka · PostgreSQL)
Governance Alignment:   R10 · R16 · R21 · R28 · R38 · R39 · R40 · R50
Audit Alignment:        Ecoskiller v8 Infrastructure Audit (All 12 Issues Resolved)
Sibling Dependency:     STT_WORKER_AUTOSCALING_AGENT v1.0.0
                        (Consumes: audio.transcribed Kafka topic)
                        (Produces: audio.confidence.low / audio.requeue / audio.flagged)

Determinism Rule:       Identical transcript input → Identical confidence verdict
Failure Mode:           STOP → REPORT → FLAG TRANSCRIPT → NO SILENT PASS

Agent Mission:
  After each transcript is produced by the STT_WORKER_AUTOSCALING_AGENT,
  the STT_CONFIDENCE_MONITOR_AGENT evaluates the quality of the result
  across five confidence dimensions, applies SLA-bound verdicts,
  triggers automatic remediation actions, and surfaces low-quality
  transcripts for human review — ensuring zero silent failures
  reach downstream consumers (scoring, analytics, certification).
```

Absence of this agent while STT_WORKER_AUTOSCALING_AGENT is active
→ STOP EXECUTION → REPORT CONFIDENCE-MONITOR-MISSING

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION CM-B — ANTIGRAVITY CONFIDENCE CONCEPT LOCK
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
Antigravity Confidence Definition:
  The STT_CONFIDENCE_MONITOR_AGENT operates with "antigravity" behavior
  in the quality dimension — it lifts poor-quality transcripts up
  for remediation instead of letting them sink silently into
  downstream systems, and it does so with zero latency for
  high-confidence results (they pass through instantly).

  High confidence  → pass instantly, zero friction, zero delay
  Low confidence   → intercepted, escalated, requeued or flagged
  Critical failure → blocked, quarantined, human review triggered

Quality Dimensions Monitored (5 Pillars):
  1. WORD_CONFIDENCE     — Per-word Whisper token probability scores
  2. SEGMENT_COHERENCE   — Logical continuity across speech segments
  3. LANGUAGE_CONSISTENCY — Detected language vs declared hint match
  4. AUDIO_QUALITY_SIGNAL — Duration, silence ratio, noise heuristics
  5. SOURCE_CRITICALITY   — Domain importance weight of the audio source
                           (tournament/certification > workshop > background)

Antigravity Behavior Contract:
  Input:  audio.transcribed Kafka event + transcript_records row
  Output: confidence_verdict (PASS | REVIEW | REQUEUE | BLOCK)
          + confidence_score (0.00 – 1.00)
          + remediation_action (NONE | UPGRADE_MODEL | HUMAN_REVIEW | QUARANTINE)
  Rule:   No transcript may reach a downstream service
          without a confidence_verdict stamp from this agent
```

Absence of confidence_verdict stamp on transcripts → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION CM-C — TECHNOLOGY STACK LOCK
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
Agent Runtime:          Python 3.11 + FastAPI (R1 aligned)
Agent Container:        Docker image — ecoskiller/stt-confidence-monitor:latest
Image Registry:         Harbor (harbor.ecoskiller.internal) — NOT GHCR (v8 audit #1)
CI/CD:                  Forgejo Actions — NOT GitHub Actions (v8 audit #1)

Input Stream:           Kafka topic — audio.transcribed
                        Consumer group: confidence_monitor_group
                        Broker: kafka.ecoskiller-infra.svc.cluster.local:9092

Output Streams (Kafka):
  audio.confidence.passed     → downstream consumers cleared
  audio.confidence.low        → review queue for human action
  audio.requeue               → STT_WORKER reprocesses with upgraded model
  audio.flagged               → quarantine — blocked from downstream

Database:               PostgreSQL 15 (R1 aligned)
                        Tables: confidence_records, confidence_rules,
                                confidence_alerts, requeue_log

Cache:                  Redis 7 — short-lived confidence rule cache (TTL=300s)
Object Storage:         MinIO — confidence reports stored alongside transcripts
                        Bucket: ecoskiller-audio-transcripts
                        Path: /confidence/{transcript_id}/report.json

Observability:
  Metrics:              Prometheus (custom exporter on :9091/metrics)
  Dashboards:           Grafana (aligned with R1 + v8 audit fix #7)
  Tracing:              Jaeger via OpenTelemetry (v8 audit fix #8)
  Logs:                 Loki + Promtail (v8 audit fix #7)

Orchestration:          Kubernetes Deployment (single replica — event-driven)
                        No KEDA required (throughput tracks STT worker output)
                        HPA on CPU: min=1, max=5

Scoring Framework:      Rule-engine ONLY (R28-1 — no ML for quality decisions)
                        Traditional weighted scoring (no LLM in hot path)
```

Use of GitHub Actions or GHCR → STOP EXECUTION (v8 audit violation)
Use of ML/LLM in core confidence scoring → STOP EXECUTION (R28-1 violation)

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION CM-D — FULL AGENT TOPOLOGY
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
┌──────────────────────────────────────────────────────────────┐
│         STT_WORKER_AUTOSCALING_AGENT (upstream)              │
│         Kafka topic: audio.transcribed                       │
└──────────────────────────┬───────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────────┐
│           STT_CONFIDENCE_MONITOR_AGENT                       │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  INGESTION LAYER                                    │    │
│  │  Kafka Consumer: audio.transcribed                  │    │
│  │  Loads: transcript_records from PostgreSQL          │    │
│  │  Loads: confidence_rules from Redis cache           │    │
│  └──────────────────┬──────────────────────────────────┘    │
│                     │                                        │
│  ┌──────────────────▼──────────────────────────────────┐    │
│  │  CONFIDENCE ENGINE (5-PILLAR RULE ENGINE)           │    │
│  │                                                     │    │
│  │  Pillar 1: Word Confidence Scorer                   │    │
│  │  Pillar 2: Segment Coherence Checker                │    │
│  │  Pillar 3: Language Consistency Validator           │    │
│  │  Pillar 4: Audio Quality Signal Analyser            │    │
│  │  Pillar 5: Source Criticality Weighter              │    │
│  │                    │                                │    │
│  │  → Composite Confidence Score (0.00–1.00)           │    │
│  │  → Confidence Verdict Gate                          │    │
│  └──────────────────┬──────────────────────────────────┘    │
│                     │                                        │
│  ┌──────────────────▼──────────────────────────────────┐    │
│  │  VERDICT ROUTER                                     │    │
│  │                                                     │    │
│  │  PASS    → audio.confidence.passed (Kafka)          │    │
│  │  REVIEW  → audio.confidence.low (Kafka)             │    │
│  │            + Admin Ops Console alert                │    │
│  │  REQUEUE → audio.requeue (Kafka)                    │    │
│  │            + upgrade model variant                  │    │
│  │  BLOCK   → audio.flagged (Kafka)                    │    │
│  │            + quarantine record                      │    │
│  └──────────────────┬──────────────────────────────────┘    │
│                     │                                        │
│  ┌──────────────────▼──────────────────────────────────┐    │
│  │  PERSISTENCE LAYER                                  │    │
│  │  PostgreSQL: confidence_records                     │    │
│  │  MinIO: /confidence/{transcript_id}/report.json     │    │
│  │  Prometheus: confidence_* metrics                   │    │
│  └─────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────┘
                           │
          ┌────────────────┼────────────────┐
          ▼                ▼                ▼
  scoring-offline-  longitudinal-    society-analytics-
  service           impact-service   service
  (Dojo scoring)    (alumni track)   (workshop reports)
```

Absence of any topology layer → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION CM-E — DATABASE SCHEMA (MANDATORY)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```sql
-- ── Confidence Verdict Records ───────────────────────────────────────────
CREATE TABLE confidence_records (
    confidence_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transcript_id       UUID NOT NULL REFERENCES transcript_records(transcript_id),
    job_id              UUID NOT NULL REFERENCES audio_jobs(job_id),
    session_id          UUID NOT NULL,
    source_service      TEXT NOT NULL,

    -- 5-Pillar Scores (each 0.00–1.00)
    score_word_conf     FLOAT NOT NULL,   -- Pillar 1
    score_seg_coherence FLOAT NOT NULL,   -- Pillar 2
    score_lang_consist  FLOAT NOT NULL,   -- Pillar 3
    score_audio_quality FLOAT NOT NULL,   -- Pillar 4
    score_src_criticality FLOAT NOT NULL, -- Pillar 5 (weight modifier)

    composite_score     FLOAT NOT NULL,   -- Weighted final score 0.00–1.00
    verdict             TEXT NOT NULL,    -- PASS | REVIEW | REQUEUE | BLOCK
    remediation_action  TEXT NOT NULL,    -- NONE | UPGRADE_MODEL | HUMAN_REVIEW | QUARANTINE
    upgraded_model      TEXT,             -- Populated if REQUEUE action taken
    requeue_count       INT DEFAULT 0,    -- Times this job has been requeued
    model_used          TEXT NOT NULL,    -- Whisper variant that produced transcript
    evaluated_at        TIMESTAMP DEFAULT NOW(),
    resolved_at         TIMESTAMP,        -- Set when human resolves REVIEW/BLOCK
    resolved_by         UUID,             -- Admin user_id who resolved
    resolution_note     TEXT
);

-- ── Confidence Scoring Rules (admin-configurable) ────────────────────────
CREATE TABLE confidence_rules (
    rule_id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    rule_name           TEXT UNIQUE NOT NULL,
    pillar              TEXT NOT NULL,   -- WORD_CONF | SEG_COHERENCE | LANG_CONSIST |
                                         -- AUDIO_QUALITY | SRC_CRITICALITY
    threshold_pass      FLOAT NOT NULL,  -- Score >= this → PASS
    threshold_review    FLOAT NOT NULL,  -- Score >= this → REVIEW
    threshold_requeue   FLOAT NOT NULL,  -- Score >= this → REQUEUE
    -- below threshold_requeue → BLOCK
    weight              FLOAT NOT NULL,  -- Pillar weight in composite formula
    active              BOOLEAN DEFAULT TRUE,
    updated_at          TIMESTAMP DEFAULT NOW(),
    updated_by          UUID             -- Admin who last changed rule
);

-- ── Source Criticality Registry ──────────────────────────────────────────
CREATE TABLE source_criticality_weights (
    source_service      TEXT PRIMARY KEY,  -- 'tournament' | 'certification' |
                                           -- 'coach' | 'workshop' | 'student' | 'arena'
    criticality_label   TEXT NOT NULL,     -- CRITICAL | HIGH | STANDARD | BACKGROUND
    weight_multiplier   FLOAT NOT NULL,    -- Applied to composite score thresholds
    min_model_required  TEXT NOT NULL      -- Minimum Whisper variant for this source
);

-- ── Requeue Audit Log ────────────────────────────────────────────────────
CREATE TABLE requeue_log (
    requeue_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_id              UUID NOT NULL,
    transcript_id       UUID NOT NULL,
    original_model      TEXT NOT NULL,
    upgraded_model      TEXT NOT NULL,
    trigger_verdict     TEXT NOT NULL,
    trigger_score       FLOAT NOT NULL,
    trigger_pillar      TEXT NOT NULL,   -- Which pillar caused requeue
    requeued_at         TIMESTAMP DEFAULT NOW()
);

-- ── Confidence Alerts ────────────────────────────────────────────────────
CREATE TABLE confidence_alerts (
    alert_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    alert_type          TEXT NOT NULL,   -- BATCH_LOW | WORKER_DEGRADED | SOURCE_SPIKE | RULE_BREACH
    source_service      TEXT,
    session_id          UUID,
    affected_count      INT,
    avg_score           FLOAT,
    alert_message       TEXT NOT NULL,
    severity            TEXT NOT NULL,   -- CRITICAL | HIGH | MEDIUM | LOW
    acknowledged        BOOLEAN DEFAULT FALSE,
    acknowledged_by     UUID,
    created_at          TIMESTAMP DEFAULT NOW()
);
```

Absence of confidence_records or confidence_rules tables → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION CM-F — CONFIDENCE SCORING ENGINE (CORE ALGORITHM)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
RULE ENGINE — DETERMINISTIC (R28-1 COMPLIANT — NO ML IN SCORING PATH)

═══════════════════════════════════════════════════════════════
PILLAR 1 — WORD CONFIDENCE SCORER
═══════════════════════════════════════════════════════════════

  Source:  Whisper segment-level avg_logprob and no_speech_prob fields
           from transcript_records.content_json

  Formula:
    word_scores  = [exp(seg["avg_logprob"]) for seg in segments]
    silence_pen  = mean([seg["no_speech_prob"] for seg in segments])
    score_P1     = mean(word_scores) × (1.0 - silence_pen)

  Score range: 0.00 – 1.00
  Interpretation:
    ≥ 0.80 → HIGH confidence (clear speech, strong token probabilities)
    0.60–0.79 → MEDIUM confidence
    0.40–0.59 → LOW confidence
    < 0.40  → CRITICAL — likely noise, silence, or unsupported language

═══════════════════════════════════════════════════════════════
PILLAR 2 — SEGMENT COHERENCE CHECKER
═══════════════════════════════════════════════════════════════

  Source:  Transcript segments — timestamps, word counts

  Checks:
    A. Gap Detection:
       gaps = [seg[i].start - seg[i-1].end for i in range(1, n)]
       large_gap_ratio = count(gap > GAP_THRESHOLD_S) / len(gaps)
       GAP_THRESHOLD_S = 3.0 seconds

    B. Segment Density:
       words_per_second = total_words / total_duration
       density_ok = 0.5 ≤ words_per_second ≤ 6.0

    C. Empty Segment Ratio:
       empty_ratio = count(seg.text.strip() == "") / total_segments

  Formula:
    gap_score     = 1.0 - large_gap_ratio
    density_score = 1.0 if density_ok else 0.5
    empty_score   = 1.0 - empty_ratio
    score_P2      = mean([gap_score, density_score, empty_score])

═══════════════════════════════════════════════════════════════
PILLAR 3 — LANGUAGE CONSISTENCY VALIDATOR
═══════════════════════════════════════════════════════════════

  Source:  info.language (Whisper detected) vs audio_jobs.language_hint

  Rules:
    IF language_hint == "auto":
      score_P3 = 1.0  (no expectation set — no penalty)
    ELIF detected_language == language_hint:
      score_P3 = 1.0
    ELIF detected_language in RELATED_LANGUAGE_MAP[language_hint]:
      score_P3 = 0.75  (e.g., hi vs ur — related scripts)
    ELSE:
      score_P3 = 0.30  (language mismatch — may be wrong audio/wrong job)

  RELATED_LANGUAGE_MAP:
    "hi": ["ur", "mr", "ne"]
    "en": ["en-US", "en-GB", "en-IN"]
    "ta": ["ml", "kn"]

═══════════════════════════════════════════════════════════════
PILLAR 4 — AUDIO QUALITY SIGNAL ANALYSER
═══════════════════════════════════════════════════════════════

  Source:  audio_jobs table + transcript metadata

  Checks:
    A. Duration Sanity:
       IF duration_sec < 2.0 → quality_flag = SUSPICIOUS_SHORT
       IF duration_sec > 14400 → quality_flag = SUSPICIOUS_LONG (4hr max)

    B. Real-Time Factor:
       rtf = processing_ms / (duration_sec × 1000)
       IF rtf > 0.95 → model was struggling (score penalty)

    C. Silence Dominance:
       silence_ratio = count(seg no_speech_prob > 0.9) / total_segments
       IF silence_ratio > 0.5 → audio is mostly silence

    D. Retry History:
       IF retry_count ≥ 2 → inherits quality penalty

  Formula:
    dur_score     = 1.0 if 2.0 ≤ duration_sec ≤ 14400 else 0.4
    rtf_score     = max(0.0, 1.0 - (rtf - 0.5) × 2) if rtf > 0.5 else 1.0
    silence_score = 1.0 - silence_ratio
    retry_pen     = max(0.0, 1.0 - (retry_count × 0.15))
    score_P4      = mean([dur_score, rtf_score, silence_score]) × retry_pen

═══════════════════════════════════════════════════════════════
PILLAR 5 — SOURCE CRITICALITY WEIGHTER
═══════════════════════════════════════════════════════════════

  Source:  source_criticality_weights table

  Source → Criticality → Weight Multiplier:
    tournament       → CRITICAL    → 1.40  (thresholds raised 40%)
    certification    → CRITICAL    → 1.40
    coach_eval       → HIGH        → 1.20
    arena_match      → HIGH        → 1.20
    workshop_session → STANDARD    → 1.00
    student_voice    → STANDARD    → 1.00
    background_sync  → BACKGROUND  → 0.80  (thresholds relaxed 20%)

  This pillar does not produce its own score.
  It produces weight_multiplier applied to verdict thresholds.

═══════════════════════════════════════════════════════════════
COMPOSITE CONFIDENCE SCORE
═══════════════════════════════════════════════════════════════

  DEFAULT PILLAR WEIGHTS (admin-configurable via confidence_rules):
    W1 (word_confidence)    = 0.40
    W2 (seg_coherence)      = 0.20
    W3 (lang_consistency)   = 0.15
    W4 (audio_quality)      = 0.25
    SUM                     = 1.00

  COMPOSITE FORMULA:
    raw_score = (W1 × P1) + (W2 × P2) + (W3 × P3) + (W4 × P4)
    composite_score = raw_score  (clamped 0.00–1.00)

═══════════════════════════════════════════════════════════════
VERDICT GATE (DETERMINISTIC — THRESHOLD TABLE)
═══════════════════════════════════════════════════════════════

  Effective thresholds = base threshold ÷ weight_multiplier (P5)

  SOURCE: STANDARD (multiplier = 1.00)
    composite_score ≥ 0.75 → PASS        → NONE
    composite_score ≥ 0.55 → REVIEW      → HUMAN_REVIEW
    composite_score ≥ 0.35 → REQUEUE     → UPGRADE_MODEL
    composite_score <  0.35 → BLOCK      → QUARANTINE

  SOURCE: CRITICAL (multiplier = 1.40) — thresholds effectively raised:
    composite_score ≥ 0.90 → PASS        → NONE
    composite_score ≥ 0.72 → REVIEW      → HUMAN_REVIEW
    composite_score ≥ 0.50 → REQUEUE     → UPGRADE_MODEL
    composite_score <  0.50 → BLOCK      → QUARANTINE

  SOURCE: BACKGROUND (multiplier = 0.80) — thresholds relaxed:
    composite_score ≥ 0.60 → PASS        → NONE
    composite_score ≥ 0.44 → REVIEW      → HUMAN_REVIEW
    composite_score ≥ 0.28 → REQUEUE     → UPGRADE_MODEL
    composite_score <  0.28 → BLOCK      → QUARANTINE

═══════════════════════════════════════════════════════════════
MODEL UPGRADE LADDER (REQUEUE PATH)
═══════════════════════════════════════════════════════════════

  When verdict = REQUEUE, the agent upgrades the Whisper model variant:
    tiny    → base
    base    → small
    small   → medium
    medium  → large-v3
    large-v3 → BLOCK (cannot upgrade further → force REVIEW verdict)

  Max requeue_count per job: 2
  If requeue_count > 2 → override verdict to BLOCK regardless of score
```

Absence of Composite Formula or Verdict Gate → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION CM-G — SEED DATA: CONFIDENCE RULES (DEFAULT STATE)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```sql
-- Default confidence rules — loaded on first deploy via seed migration

INSERT INTO confidence_rules
  (rule_name, pillar, threshold_pass, threshold_review, threshold_requeue, weight, active)
VALUES
  ('word_conf_standard',    'WORD_CONF',        0.75, 0.55, 0.35, 0.40, TRUE),
  ('seg_coherence_standard','SEG_COHERENCE',    0.75, 0.55, 0.35, 0.20, TRUE),
  ('lang_consist_standard', 'LANG_CONSIST',     0.75, 0.55, 0.35, 0.15, TRUE),
  ('audio_quality_standard','AUDIO_QUALITY',    0.75, 0.55, 0.35, 0.25, TRUE);

INSERT INTO source_criticality_weights
  (source_service, criticality_label, weight_multiplier, min_model_required)
VALUES
  ('tournament',       'CRITICAL',    1.40, 'medium'),
  ('certification',    'CRITICAL',    1.40, 'medium'),
  ('coach_eval',       'HIGH',        1.20, 'small'),
  ('arena_match',      'HIGH',        1.20, 'small'),
  ('workshop_session', 'STANDARD',    1.00, 'base'),
  ('student_voice',    'STANDARD',    1.00, 'base'),
  ('background_sync',  'BACKGROUND',  0.80, 'tiny');
```

Absence of seed data migration → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION CM-H — AGENT SERVICE CODE (PYTHON — REQUIRED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```python
# File: /backend/services/stt_confidence_monitor/main.py
# Purpose: Consume audio.transcribed events, evaluate confidence, route verdict
# Stack: Python 3.11 + Kafka + PostgreSQL + Redis + FastAPI

import os, json, math, time, uuid
from kafka import KafkaConsumer, KafkaProducer
from prometheus_client import Counter, Histogram, Gauge, start_http_server
import psycopg2, redis

# ── Prometheus Metrics ───────────────────────────────────────────────────
VERDICTS_TOTAL      = Counter('cm_verdicts_total', 'Verdicts issued', ['verdict'])
COMPOSITE_SCORE     = Histogram('cm_composite_score', 'Composite confidence scores',
                                buckets=[0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0])
REQUEUE_TOTAL       = Counter('cm_requeue_total', 'Jobs requeued for model upgrade',
                              ['original_model', 'upgraded_model'])
BLOCK_TOTAL         = Counter('cm_block_total', 'Jobs blocked and quarantined',
                              ['source_service'])
REVIEW_QUEUE_DEPTH  = Gauge('cm_review_queue_depth', 'Transcripts awaiting human review')
EVAL_LATENCY        = Histogram('cm_evaluation_latency_seconds',
                                'Time to evaluate one transcript')
P1_SCORES           = Histogram('cm_pillar1_word_conf', 'Pillar 1 scores',
                                buckets=[0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0])
P2_SCORES           = Histogram('cm_pillar2_seg_coherence', 'Pillar 2 scores',
                                buckets=[0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0])
P3_SCORES           = Histogram('cm_pillar3_lang_consist', 'Pillar 3 scores',
                                buckets=[0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0])
P4_SCORES           = Histogram('cm_pillar4_audio_quality', 'Pillar 4 scores',
                                buckets=[0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0])

# ── Config ───────────────────────────────────────────────────────────────
KAFKA_BROKERS  = os.getenv("KAFKA_BROKERS", "kafka.ecoskiller-infra:9092")
REDIS_HOST     = os.getenv("REDIS_HOST", "redis-service.ecoskiller-infra")
DB_DSN         = os.getenv("DB_DSN")    # injected via Vault secret
GAP_THRESHOLD  = float(os.getenv("GAP_THRESHOLD_S", "3.0"))
MAX_REQUEUE    = int(os.getenv("MAX_REQUEUE_COUNT", "2"))

# ── Model Upgrade Ladder ─────────────────────────────────────────────────
UPGRADE_LADDER = {"tiny": "base", "base": "small",
                  "small": "medium", "medium": "large-v3"}


def run():
    start_http_server(9091)

    consumer = KafkaConsumer(
        "audio.transcribed",
        bootstrap_servers=KAFKA_BROKERS,
        group_id="confidence_monitor_group",
        value_deserializer=lambda m: json.loads(m.decode("utf-8")),
        auto_offset_reset="earliest",
        enable_auto_commit=False,
    )
    producer = KafkaProducer(
        bootstrap_servers=KAFKA_BROKERS,
        value_serializer=lambda v: json.dumps(v).encode("utf-8"),
    )
    db   = psycopg2.connect(DB_DSN)
    rc   = redis.Redis(host=REDIS_HOST, decode_responses=True)
    rules = load_rules(db, rc)

    print("[CM] STT_CONFIDENCE_MONITOR_AGENT started")

    for msg in consumer:
        event = msg.value
        t0 = time.time()
        try:
            result = evaluate(event, db, rc, rules)
            persist(result, db)
            route(result, producer, db)
            EVAL_LATENCY.observe(time.time() - t0)
            consumer.commit()
        except Exception as e:
            print(f"[CM ERROR] job={event.get('job_id')} err={e}")
            # Do not commit — Kafka will redeliver


def evaluate(event, db, rc, rules):
    job_id        = event["job_id"]
    transcript_id = event["transcript_id"]
    source_svc    = event["source_service"]
    model_used    = event["model_used"]
    duration_sec  = event.get("duration_sec", 0)
    language_hint = event.get("language_hint", "auto")
    retry_count   = event.get("retry_count", 0)
    requeue_count = event.get("requeue_count", 0)
    segments      = load_segments(transcript_id, db)
    criticality   = load_criticality(source_svc, rc, db)

    # ── Pillar 1: Word Confidence
    p1 = score_word_confidence(segments)

    # ── Pillar 2: Segment Coherence
    p2 = score_segment_coherence(segments, duration_sec)

    # ── Pillar 3: Language Consistency
    detected_lang = event.get("language", "unknown")
    p3 = score_language_consistency(detected_lang, language_hint)

    # ── Pillar 4: Audio Quality
    rtf = event.get("real_time_factor", 0.3)
    p4  = score_audio_quality(duration_sec, rtf, segments, retry_count)

    # ── Composite (weighted)
    w  = rules["weights"]
    composite = (w[1]*p1 + w[2]*p2 + w[3]*p3 + w[4]*p4)
    composite = max(0.0, min(1.0, composite))

    # ── Verdict (P5 multiplier applied to thresholds)
    m     = criticality["weight_multiplier"]
    t     = rules["thresholds"]
    t_pass    = t["pass"]    / m
    t_review  = t["review"]  / m
    t_requeue = t["requeue"] / m

    if requeue_count > MAX_REQUEUE:
        verdict, action = "BLOCK", "QUARANTINE"
    elif composite >= t_pass:
        verdict, action = "PASS", "NONE"
    elif composite >= t_review:
        verdict, action = "REVIEW", "HUMAN_REVIEW"
    elif composite >= t_requeue:
        verdict, action = "REQUEUE", "UPGRADE_MODEL"
    else:
        verdict, action = "BLOCK", "QUARANTINE"

    upgraded_model = None
    if action == "UPGRADE_MODEL":
        upgraded_model = UPGRADE_LADDER.get(model_used, None)
        if upgraded_model is None:
            verdict, action = "BLOCK", "QUARANTINE"

    # ── Prometheus
    VERDICTS_TOTAL.labels(verdict=verdict).inc()
    COMPOSITE_SCORE.observe(composite)
    P1_SCORES.observe(p1); P2_SCORES.observe(p2)
    P3_SCORES.observe(p3); P4_SCORES.observe(p4)
    if verdict == "BLOCK":
        BLOCK_TOTAL.labels(source_service=source_svc).inc()

    return {
        "confidence_id":     str(uuid.uuid4()),
        "transcript_id":     transcript_id,
        "job_id":            job_id,
        "session_id":        event["session_id"],
        "source_service":    source_svc,
        "score_word_conf":   p1,
        "score_seg_coherence": p2,
        "score_lang_consist":  p3,
        "score_audio_quality": p4,
        "score_src_criticality": m,
        "composite_score":   composite,
        "verdict":           verdict,
        "remediation_action": action,
        "upgraded_model":    upgraded_model,
        "requeue_count":     requeue_count,
        "model_used":        model_used,
    }


def score_word_confidence(segments):
    if not segments:
        return 0.0
    probs     = [math.exp(s.get("avg_logprob", -1.0)) for s in segments]
    silences  = [s.get("no_speech_prob", 0.0) for s in segments]
    return sum(probs) / len(probs) * (1.0 - sum(silences) / len(silences))


def score_segment_coherence(segments, duration_sec):
    if len(segments) < 2:
        return 1.0 if segments else 0.0
    gaps       = [segments[i]["start"] - segments[i-1]["end"]
                  for i in range(1, len(segments))]
    large      = sum(1 for g in gaps if g > GAP_THRESHOLD) / len(gaps)
    total_words = sum(len(s.get("text","").split()) for s in segments)
    wps         = total_words / max(duration_sec, 1)
    density_ok  = 0.5 <= wps <= 6.0
    empty_ratio = sum(1 for s in segments if not s.get("text","").strip()) / len(segments)
    return (1.0 - large + (1.0 if density_ok else 0.5) + (1.0 - empty_ratio)) / 3.0


def score_language_consistency(detected, hint):
    RELATED = {"hi": {"ur","mr","ne"}, "en": {"en-US","en-GB","en-IN"}, "ta": {"ml","kn"}}
    if hint == "auto":
        return 1.0
    if detected == hint:
        return 1.0
    if detected in RELATED.get(hint, set()):
        return 0.75
    return 0.30


def score_audio_quality(duration, rtf, segments, retry_count):
    dur_ok  = 2.0 <= duration <= 14400
    dur_s   = 1.0 if dur_ok else 0.4
    rtf_s   = max(0.0, 1.0 - (rtf - 0.5) * 2) if rtf > 0.5 else 1.0
    sil     = sum(1 for s in segments if s.get("no_speech_prob",0) > 0.9)
    sil_s   = 1.0 - (sil / max(len(segments), 1))
    retry_p = max(0.0, 1.0 - retry_count * 0.15)
    return ((dur_s + rtf_s + sil_s) / 3.0) * retry_p


def load_segments(transcript_id, db):
    with db.cursor() as cur:
        cur.execute("SELECT content_json FROM transcript_records "
                    "WHERE transcript_id = %s", (transcript_id,))
        row = cur.fetchone()
    return row[0] if row else []


def load_criticality(source_svc, rc, db):
    key = f"criticality:{source_svc}"
    cached = rc.get(key)
    if cached:
        return json.loads(cached)
    with db.cursor() as cur:
        cur.execute("SELECT criticality_label, weight_multiplier, min_model_required "
                    "FROM source_criticality_weights WHERE source_service = %s",
                    (source_svc,))
        row = cur.fetchone()
    result = {"criticality_label": row[0], "weight_multiplier": row[1],
              "min_model_required": row[2]} if row else \
             {"criticality_label": "STANDARD", "weight_multiplier": 1.0,
              "min_model_required": "base"}
    rc.setex(key, 300, json.dumps(result))
    return result


def load_rules(db, rc):
    with db.cursor() as cur:
        cur.execute("SELECT pillar, weight, threshold_pass, threshold_review, "
                    "threshold_requeue FROM confidence_rules WHERE active = TRUE")
        rows = cur.fetchall()
    w = {}; t_pass_list = []; t_rev_list = []; t_req_list = []
    PILLAR_IDX = {"WORD_CONF":1,"SEG_COHERENCE":2,"LANG_CONSIST":3,"AUDIO_QUALITY":4}
    for row in rows:
        idx = PILLAR_IDX.get(row[0])
        if idx:
            w[idx] = row[1]
            t_pass_list.append(row[2])
            t_rev_list.append(row[3])
            t_req_list.append(row[4])
    return {
        "weights": w,
        "thresholds": {
            "pass":    sum(t_pass_list) / len(t_pass_list),
            "review":  sum(t_rev_list)  / len(t_rev_list),
            "requeue": sum(t_req_list)  / len(t_req_list),
        }
    }


def persist(result, db):
    with db.cursor() as cur:
        cur.execute("""
            INSERT INTO confidence_records
              (confidence_id, transcript_id, job_id, session_id, source_service,
               score_word_conf, score_seg_coherence, score_lang_consist,
               score_audio_quality, score_src_criticality,
               composite_score, verdict, remediation_action,
               upgraded_model, requeue_count, model_used)
            VALUES (%(confidence_id)s, %(transcript_id)s, %(job_id)s,
                    %(session_id)s, %(source_service)s,
                    %(score_word_conf)s, %(score_seg_coherence)s,
                    %(score_lang_consist)s, %(score_audio_quality)s,
                    %(score_src_criticality)s,
                    %(composite_score)s, %(verdict)s, %(remediation_action)s,
                    %(upgraded_model)s, %(requeue_count)s, %(model_used)s)
        """, result)
    db.commit()


def route(result, producer, db):
    verdict = result["verdict"]
    payload = {
        "job_id":          result["job_id"],
        "transcript_id":   result["transcript_id"],
        "session_id":      result["session_id"],
        "source_service":  result["source_service"],
        "composite_score": result["composite_score"],
        "verdict":         verdict,
    }
    if verdict == "PASS":
        producer.send("audio.confidence.passed", value=payload)

    elif verdict == "REVIEW":
        producer.send("audio.confidence.low", value=payload)
        create_alert(db, result, "BATCH_LOW")
        REVIEW_QUEUE_DEPTH.inc()

    elif verdict == "REQUEUE":
        requeue_payload = {**payload,
                           "upgraded_model": result["upgraded_model"],
                           "requeue_count":  result["requeue_count"] + 1}
        producer.send("audio.requeue", value=requeue_payload)
        REQUEUE_TOTAL.labels(original_model=result["model_used"],
                             upgraded_model=result["upgraded_model"]).inc()
        log_requeue(db, result)

    elif verdict == "BLOCK":
        producer.send("audio.flagged", value=payload)
        create_alert(db, result, "SOURCE_SPIKE")
    producer.flush()


def create_alert(db, result, alert_type):
    with db.cursor() as cur:
        cur.execute("""
            INSERT INTO confidence_alerts
              (alert_type, source_service, session_id,
               affected_count, avg_score, alert_message, severity)
            VALUES (%s,%s,%s,1,%s,%s,%s)
        """, (alert_type, result["source_service"], result["session_id"],
              result["composite_score"],
              f"Transcript {result['transcript_id']} verdict={result['verdict']} "
              f"score={result['composite_score']:.3f}",
              "CRITICAL" if result["verdict"]=="BLOCK" else "HIGH"))
    db.commit()


def log_requeue(db, result):
    with db.cursor() as cur:
        cur.execute("""
            INSERT INTO requeue_log
              (job_id, transcript_id, original_model, upgraded_model,
               trigger_verdict, trigger_score, trigger_pillar)
            VALUES (%s,%s,%s,%s,%s,%s,'COMPOSITE')
        """, (result["job_id"], result["transcript_id"],
              result["model_used"], result["upgraded_model"],
              result["verdict"], result["composite_score"]))
    db.commit()


if __name__ == "__main__":
    run()
```

Absence of 5 scoring functions (score_word_confidence, score_segment_coherence,
score_language_consistency, score_audio_quality, persist) → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION CM-I — KAFKA EVENT CONTRACTS (ALIGNED WITH R4)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# AsyncAPI 2.6.0 — STT Confidence Monitor Events

channels:

  audio.confidence.passed:
    publish:
      summary: Transcript passed confidence check — cleared for downstream
      message:
        payload:
          job_id:           uuid
          transcript_id:    uuid
          session_id:       uuid
          source_service:   string
          composite_score:  float    # ≥ threshold_pass for source type
          verdict:          string   # always "PASS"
    consumers:
      - scoring-offline-service     # Dojo match scoring
      - longitudinal-impact-service # Alumni tracking
      - society-analytics-service   # Workshop reporting
      - dojo-analytics-service      # Live session analytics

  audio.confidence.low:
    publish:
      summary: Transcript confidence below threshold — human review required
      message:
        payload:
          job_id:           uuid
          transcript_id:    uuid
          session_id:       uuid
          source_service:   string
          composite_score:  float
          verdict:          string   # always "REVIEW"
    consumers:
      - admin-ops-console           # Alert in Internal Ops Console (R40)
      - notification-service        # Alert to platform moderators

  audio.requeue:
    publish:
      summary: Transcript requeued for reprocessing with upgraded model
      message:
        payload:
          job_id:           uuid
          transcript_id:    uuid
          session_id:       uuid
          source_service:   string
          composite_score:  float
          upgraded_model:   string   # New model variant to use
          requeue_count:    integer
          verdict:          string   # always "REQUEUE"
    consumers:
      - stt-worker-service          # STT_WORKER_AUTOSCALING_AGENT re-ingests job

  audio.flagged:
    publish:
      summary: Transcript blocked — quarantined, cannot reach downstream
      message:
        payload:
          job_id:           uuid
          transcript_id:    uuid
          session_id:       uuid
          source_service:   string
          composite_score:  float
          verdict:          string   # always "BLOCK"
    consumers:
      - admin-ops-console           # Quarantine queue in Internal Ops Console
      - audit-service               # Audit log entry (R60)
      - compliance-service          # Compliance record (Society domain)

  audio.confidence.batch_alert:
    publish:
      summary: Batch degradation alert — multiple transcripts below threshold
      message:
        payload:
          alert_type:       string
          source_service:   string
          affected_count:   integer
          avg_score:        float
          time_window_min:  integer
          severity:         string
    consumers:
      - admin-ops-console
      - notification-service
```

Absence of audio.confidence.passed event contract → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION CM-J — REST API CONTRACT (ALIGNED WITH R3)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# OpenAPI 3.1 — STT Confidence Monitor Endpoints

paths:

  /audio/confidence/{transcript_id}:
    get:
      summary: Get confidence record for a transcript
      responses:
        "200":
          description: Full confidence record with all 5 pillar scores
          schema:
            type: object
            properties:
              transcript_id:        uuid
              composite_score:      float
              verdict:              string
              remediation_action:   string
              score_word_conf:      float
              score_seg_coherence:  float
              score_lang_consist:   float
              score_audio_quality:  float
              model_used:           string
              evaluated_at:         timestamp

  /audio/confidence/review-queue:
    get:
      summary: List all transcripts awaiting human review (REVIEW or BLOCK verdict)
      parameters:
        - name: source_service
          in: query
        - name: severity
          in: query
        - name: limit
          in: query
          default: 50
      responses:
        "200": { description: Paginated review queue }

  /audio/confidence/review-queue/{confidence_id}/resolve:
    post:
      summary: Admin resolves a flagged/review transcript
      requestBody:
        content:
          application/json:
            schema:
              type: object
              required: [resolution, note]
              properties:
                resolution: { type: string }  # ACCEPT | REJECT | REPROCESS
                note:       { type: string }
      responses:
        "200": { description: Resolved — downstream notified }

  /audio/confidence/rules:
    get:
      summary: Get all active confidence scoring rules
    put:
      summary: Update confidence rule thresholds (Super Admin only)
      responses:
        "200": { description: Rules updated — cache invalidated }

  /audio/confidence/stats:
    get:
      summary: Aggregate confidence stats by time window and source
      parameters:
        - name: window_hours
          in: query
          default: 24
        - name: source_service
          in: query
      responses:
        "200":
          description: Stats
          schema:
            type: object
            properties:
              total_evaluated:    integer
              pass_count:         integer
              review_count:       integer
              requeue_count:      integer
              block_count:        integer
              avg_composite_score: float
              pass_rate_pct:      float
              top_failure_pillar: string

  /audio/confidence/health:
    get:
      summary: Agent health + review queue depth + last evaluation timestamp
      responses:
        "200": { description: Agent operational status }
```

Absence of /audio/confidence/review-queue endpoint → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION CM-K — PROMETHEUS METRICS CONTRACT
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
MANDATORY METRICS (exposed on :9091/metrics):

  cm_verdicts_total{verdict="PASS|REVIEW|REQUEUE|BLOCK"}
    Type: Counter
    Description: Total verdicts issued by verdict type

  cm_composite_score (histogram — buckets 0.1 to 1.0)
    Type: Histogram
    Description: Distribution of composite confidence scores

  cm_pillar1_word_conf (histogram)
    cm_pillar2_seg_coherence (histogram)
    cm_pillar3_lang_consist (histogram)
    cm_pillar4_audio_quality (histogram)
    Type: Histogram
    Description: Per-pillar score distributions

  cm_requeue_total{original_model, upgraded_model}
    Type: Counter
    Description: Model upgrade requeue events

  cm_block_total{source_service}
    Type: Counter
    Description: Blocked transcripts per source service

  cm_review_queue_depth
    Type: Gauge
    Description: Number of transcripts pending human resolution

  cm_evaluation_latency_seconds (histogram)
    Type: Histogram
    Description: Time to evaluate one transcript (target p95 < 200ms)

  cm_pass_rate_1h
    Type: Gauge
    Description: Rolling 1-hour pass rate (target > 0.85)

  cm_rules_cache_age_seconds
    Type: Gauge
    Description: Age of confidence_rules cache in Redis

DERIVED GRAFANA ALERTS (computed from above metrics):

  cm_pass_rate_1h < 0.70
    → alert: BATCH_QUALITY_DEGRADATION (CRITICAL)

  cm_block_total rate > 5/min for source="tournament"
    → alert: TOURNAMENT_AUDIO_QUALITY_SPIKE (CRITICAL)

  cm_review_queue_depth > 100
    → alert: REVIEW_QUEUE_OVERFLOWING (HIGH)

  cm_requeue_total rate > 20/min
    → alert: MODEL_UPGRADE_STORM (HIGH)
       Indicates systematic audio quality problem or wrong model config

  cm_evaluation_latency_seconds p95 > 1.0
    → alert: CONFIDENCE_MONITOR_SLOW (MEDIUM)
```

Absence of cm_verdicts_total or cm_composite_score metrics → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION CM-L — KUBERNETES DEPLOYMENT MANIFEST
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# File: /infra/k8s/production/audio/stt-confidence-monitor-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: stt-confidence-monitor
  namespace: ecoskiller-audio
  labels:
    app: stt-confidence-monitor
    layer: antigravity
spec:
  replicas: 2          # Active-passive HA pair
  selector:
    matchLabels:
      app: stt-confidence-monitor
  template:
    metadata:
      labels:
        app: stt-confidence-monitor
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "9091"
        vault.hashicorp.com/agent-inject: "true"
        vault.hashicorp.com/role: "audio-monitor"
    spec:
      containers:
        - name: stt-confidence-monitor
          image: harbor.ecoskiller.internal/ecoskiller/stt-confidence-monitor:latest
          ports:
            - containerPort: 9091    # Prometheus metrics
            - containerPort: 8080    # FastAPI health / REST
          resources:
            requests:
              cpu: "250m"
              memory: "512Mi"
            limits:
              cpu: "1"
              memory: "1Gi"
          env:
            - name: KAFKA_BROKERS
              valueFrom:
                configMapKeyRef:
                  name: audio-config
                  key: kafka_brokers
            - name: REDIS_HOST
              valueFrom:
                configMapKeyRef:
                  name: audio-config
                  key: redis_host
            - name: DB_DSN
              valueFrom:
                secretKeyRef:
                  name: audio-db-secret
                  key: dsn
            - name: GAP_THRESHOLD_S
              value: "3.0"
            - name: MAX_REQUEUE_COUNT
              value: "2"
          livenessProbe:
            httpGet:
              path: /audio/confidence/health
              port: 8080
            initialDelaySeconds: 20
            periodSeconds: 15
          readinessProbe:
            httpGet:
              path: /audio/confidence/health
              port: 8080
            initialDelaySeconds: 10
            periodSeconds: 10
      terminationGracePeriodSeconds: 60

---
# HPA — scales with evaluation throughput
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: stt-confidence-monitor-hpa
  namespace: ecoskiller-audio
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: stt-confidence-monitor
  minReplicas: 1
  maxReplicas: 5
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 65
```

```dockerfile
# File: /backend/services/stt_confidence_monitor/Dockerfile
FROM python:3.11-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . .
EXPOSE 9091 8080
CMD ["python", "main.py"]
```

```
# requirements.txt
kafka-python==2.0.2
psycopg2-binary==2.9.9
redis==5.0.1
prometheus-client==0.20.0
opentelemetry-sdk==1.24.0
fastapi==0.111.0
uvicorn==0.29.0
```

Absence of deployment manifest or HPA → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION CM-M — ADMIN OPS CONSOLE MODULE (R40 ALIGNED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
STT_CONFIDENCE_MONITOR_AGENT must be visible and controllable
from the ECOSKILLER Internal Admin & Ops Console (R40).

REQUIRED CONSOLE MODULE: Audio Quality Control Panel

  Module Path: System Operations → Audio Processing → Confidence Monitor

  Panel Sections:

  ── LIVE DASHBOARD ──────────────────────────────────────────

    1. Pass Rate Gauge (rolling 1h, 6h, 24h)
       Target: > 85% PASS rate
       Alert threshold: < 70% triggers CRITICAL banner

    2. Composite Score Distribution (histogram chart)
       Real-time bucketed view of all recent scores

    3. Verdict Breakdown Donut (PASS / REVIEW / REQUEUE / BLOCK)

    4. Per-Source Pass Rate Table:
       | source_service | total | pass% | review% | block% | avg_score |
       (sortable, filterable)

    5. Top Failure Pillar Indicator
       (Which of the 5 pillars is dragging scores lowest today)

    6. Model Upgrade Activity (requeue rate over time)

  ── REVIEW QUEUE ────────────────────────────────────────────

    7. Human Review Queue Table (all REVIEW + BLOCK verdicts)
       Columns: transcript_id | source | score | pillar flags | age
       Actions per row:
         - Listen (streams audio from MinIO)
         - View transcript (JSON segments)
         - Accept (mark as acceptable despite low score)
         - Reject (mark as unusable — notify originating service)
         - Reprocess (force requeue with large-v3 model)

    8. Bulk actions: Accept All | Reject All | Reprocess All (filtered)

  ── SCORING RULES EDITOR ────────────────────────────────────

    9. Confidence Rules Table (live edit — R40 compliant)
       Editable: threshold_pass | threshold_review | threshold_requeue | weight
       All edits: require justification text + emit audit event

   10. Source Criticality Table (live edit)
       Editable: weight_multiplier | min_model_required per source_service

   11. Rule Change History (immutable audit trail)

  ── ALERT HISTORY ───────────────────────────────────────────

   12. Confidence Alerts Log (last 7 days)
       Filter by: severity | source_service | alert_type
       Acknowledge button per alert

  All console actions:
    ✔ Require Super Admin RBAC + MFA (R40 Section B)
    ✔ Emit to audit_logs table
    ✔ Reversible where possible (Accept/Reject can be undone within 1h)
    ✔ Session-timeout enforced (R40 Section B)

  Absence of Audio Quality Control Panel in Ops Console → STOP EXECUTION
```

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION CM-N — DOWNSTREAM INTEGRATION CONTRACTS
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
RULE: No downstream service may consume a transcript
      without a PASS confidence verdict from this agent.

ENFORCED INTEGRATION POINTS:

  scoring-offline-service (Dojo tournament scoring):
    MUST check: confidence_records.verdict = 'PASS'
    IF verdict ≠ PASS → scoring halted, mentor notified
    Aligned with: Dojo SECTION F — Scoring Governance Lock
                  Dojo SECTION T2 — Scoring Validity & Reliability Lock

  longitudinal-impact-service (alumni tracking):
    MUST check: confidence_records.composite_score ≥ 0.75
    IF below threshold → flag record, do not update alumni skill vector

  society-analytics-service (workshop attendance/impact):
    MUST check: confidence_records.verdict ∈ ['PASS', 'REVIEW']
    REVIEW verdict → analytics marked as low_confidence_flag = TRUE
    Aligned with: R61 — Data Network Effect Analytics Law

  dojo-analytics-service (Dojo session replay):
    MUST check: confidence_records.verdict = 'PASS'
    IF BLOCK → replay transcript section marked [UNAVAILABLE]

  certification pipeline (R72 — Lifetime Skill Passport):
    MUST check: confidence_records.verdict = 'PASS'
                AND composite_score ≥ 0.80 (elevated threshold)
    IF below → certification blocked until human review resolved

CONTRACT ENFORCEMENT MECHANISM:
  audio.confidence.passed event MUST be emitted before
  any downstream Kafka topic carries the transcript payload.
  Downstream services MUST NOT consume audio.transcribed directly.
  They MUST only consume audio.confidence.passed.

Violation of downstream bypass → STOP EXECUTION
→ REPORT CONFIDENCE-GATE-BYPASSED
→ NO CERTIFICATION OR SCORING CLAIM PERMITTED
```

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION CM-O — INTERN EXECUTION STEPS (R26 / R46 ALIGNED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
INTERN ROLE: Python Backend Developer + Basic Kubernetes Intern
PREREQUISITE: STT_WORKER_AUTOSCALING_AGENT already deployed

OBJECTIVE: Deploy and test STT_CONFIDENCE_MONITOR_AGENT locally

────────────────────────────────────────────────────────────
STEP 1 — Run database migrations
────────────────────────────────────────────────────────────
  cd /backend/services/stt_confidence_monitor/
  alembic upgrade head

Expected Output:
  Running upgrade -> head — confidence_records, confidence_rules,
  source_criticality_weights, requeue_log, confidence_alerts tables created

────────────────────────────────────────────────────────────
STEP 2 — Seed default confidence rules
────────────────────────────────────────────────────────────
  python seed_confidence_rules.py

Expected Output:
  Inserted 4 confidence rules + 7 source criticality weights

────────────────────────────────────────────────────────────
STEP 3 — Build Docker image
────────────────────────────────────────────────────────────
  docker build -t stt-confidence-monitor:local .
  minikube image load stt-confidence-monitor:local

────────────────────────────────────────────────────────────
STEP 4 — Apply Kubernetes manifests
────────────────────────────────────────────────────────────
  kubectl apply -f /infra/k8s/dev/audio/stt-confidence-monitor-deployment.yaml

Check pods:
  kubectl get pods -n ecoskiller-audio

Expected: stt-confidence-monitor-* STATUS = Running (2 pods)

────────────────────────────────────────────────────────────
STEP 5 — Trigger a test transcript event
────────────────────────────────────────────────────────────
  python /tools/audio/send_test_transcript.py \
    --source workshop_session \
    --score-mode low

This sends a synthetic audio.transcribed Kafka event
with low avg_logprob values to test REQUEUE path.

────────────────────────────────────────────────────────────
STEP 6 — Verify verdict was issued
────────────────────────────────────────────────────────────
  psql ecoskiller -c "SELECT verdict, composite_score FROM confidence_records
                      ORDER BY evaluated_at DESC LIMIT 5;"

Expected:
  verdict  | composite_score
  REQUEUE  | 0.42
  PASS     | 0.87
  ...

────────────────────────────────────────────────────────────
STEP 7 — Verify Kafka routing
────────────────────────────────────────────────────────────
  kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic audio.confidence.passed --from-beginning

Expected: PASS verdicts appearing in topic

────────────────────────────────────────────────────────────
STEP 8 — Check Prometheus metrics
────────────────────────────────────────────────────────────
  kubectl port-forward pod/<monitor-pod-name> 9091:9091 -n ecoskiller-audio
  curl http://localhost:9091/metrics | grep cm_

Expected:
  cm_verdicts_total{verdict="PASS"} > 0
  cm_composite_score_bucket visible
  cm_review_queue_depth shown

────────────────────────────────────────────────────────────
SUCCESS CONDITIONS
────────────────────────────────────────────────────────────
  ✔ confidence_records rows inserted for each transcript
  ✔ audio.confidence.passed events on Kafka for PASS verdicts
  ✔ audio.requeue events on Kafka for REQUEUE verdicts
  ✔ Prometheus metrics visible
  ✔ Review queue API responding: GET /audio/confidence/review-queue
  ✔ Confidence rules editable via PUT /audio/confidence/rules

Failure at any step → STOP EXECUTION
```

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION CM-P — PRODUCTION READINESS CHECKLIST
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
No STT_CONFIDENCE_MONITOR_AGENT may be declared production-ready unless:

  ✔ confidence_records table migrated
  ✔ confidence_rules table seeded with defaults
  ✔ source_criticality_weights table seeded with all 7 source types
  ✔ Kafka consumer group confidence_monitor_group created
  ✔ All 4 output Kafka topics provisioned
      (audio.confidence.passed / audio.confidence.low /
       audio.requeue / audio.flagged)
  ✔ Docker image built and pushed to Harbor (NOT GHCR — v8 audit #1)
  ✔ 2-replica deployment live in ecoskiller-audio namespace
  ✔ HPA active (min=1, max=5)
  ✔ Prometheus metrics scraping on :9091
  ✔ All 5 Grafana alert rules active
  ✔ 5-pillar scoring verified via unit tests (R50)
  ✔ REQUEUE model upgrade ladder tested end-to-end
  ✔ Admin Ops Console Audio Quality Panel deployed
  ✔ Downstream bypass prevention verified:
       scoring-offline-service ONLY consumes audio.confidence.passed
  ✔ Contract validator (R49) passes for all confidence APIs
  ✔ QA generator (R50) test suite passes with 100% coverage
  ✔ Forgejo CI pipeline builds agent (NOT GitHub Actions — v8 audit #1)
  ✔ BLOCK verdict quarantine tested for tournament source
  ✔ Rule edit via Admin Console tested + audit log confirmed

Failure of any check → STOP EXECUTION → REPORT CONFIDENCE-MONITOR-NOT-READY
```

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# 🔒 SEAL BLOCK
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
STT_CONFIDENCE_MONITOR_AGENT — ANTIGRAVITY AUDIO PROCESSING
════════════════════════════════════════════════════════════

SYSTEM:               ECOSKILLER Unified SaaS
LAYER:                Audio Processing — Antigravity
AGENT VERSION:        v1.0.0
SIBLING AGENT:        STT_WORKER_AUTOSCALING_AGENT v1.0.0
SEALED DATE:          2026-03-04
SEALED BY:            Master Execution Prompt v12.0

CONFIDENCE ENGINE:                ACTIVE (5-PILLAR RULE ENGINE)
ML/LLM IN SCORING PATH:           FORBIDDEN (R28-1)
RULE ENGINE:                      REQUIRED AND LOCKED
DOWNSTREAM BYPASS:                FORBIDDEN (Transcript gate enforced)
GITHUB ACTIONS:                   FORBIDDEN (v8 audit fix #1)
GHCR:                             FORBIDDEN (v8 audit fix #1)
FORGEJO + HARBOR:                 REQUIRED
KAFKA CONSUMER GROUP:             confidence_monitor_group (LOCKED)
OUTPUT TOPICS:                    audio.confidence.passed (clearance)
                                  audio.confidence.low (review)
                                  audio.requeue (model upgrade)
                                  audio.flagged (quarantine)
PILLAR WEIGHTS:                   W1=0.40 W2=0.20 W3=0.15 W4=0.25
MODEL UPGRADE LADDER:             tiny→base→small→medium→large-v3→BLOCK
MAX REQUEUE COUNT:                2 (hard ceiling per job)
ADMIN OPS CONSOLE:                REQUIRED (R40)
INTERN EXECUTABLE:                REQUIRED (R46)
CONTRACT VALIDATOR GATE:          REQUIRED (R49)
QA TEST GENERATOR GATE:           REQUIRED (R50)
MUTATION POLICY:                  ADD-ONLY VIA VERSION BUMP
INTERPRETATION AUTHORITY:         NONE
EXECUTION AUTHORITY:              HUMAN DECLARATION ONLY

Violation of any seal → STOP EXECUTION
→ REPORT CM-AGENT-SEAL-VIOLATION
→ NO DEPLOYMENT CLAIM PERMITTED
════════════════════════════════════════════════════════════
```
