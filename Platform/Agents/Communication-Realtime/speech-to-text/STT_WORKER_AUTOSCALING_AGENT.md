# 🔒 STT_WORKER_AUTOSCALING_AGENT
## ECOSKILLER — Audio Processing Module · Antigravity Layer
### Status: FINAL · LOCKED · SEALED · GOVERNED · DETERMINISTIC
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-A — AGENT IDENTITY & PURPOSE DECLARATION
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
Agent Name:           STT_WORKER_AUTOSCALING_AGENT
Agent Class:          Infrastructure Intelligence Agent
Domain:               Audio Processing — Antigravity Subsystem
Parent System:        ECOSKILLER Unified SaaS Platform
Namespace:            ecoskiller-audio / antigravity
Stack Alignment:      R1 (Python 3.11 · FastAPI · Redis · Kubernetes)
Governance Alignment: R10 · R21 · R25 · R39 · R40 · R45 · R50
Audit Alignment:      Ecoskiller v8 Infrastructure Audit (All 12 Issues Resolved)
Determinism Rule:     Identical load signal → Identical scaling decision
Failure Mode:         STOP → REPORT → NO PARTIAL SCALING ACTION
```

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-B — ANTIGRAVITY CONCEPT LOCK
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
Antigravity Audio Processing Definition:
  The STT (Speech-to-Text) subsystem that operates with
  "antigravity" behavior — meaning it scales UP effortlessly
  under load and scales DOWN precisely under idle conditions,
  as if defying the gravitational drag of over-provisioned
  or under-provisioned infrastructure.

Core Principle:
  Zero latency degradation under burst.
  Zero resource waste under calm.
  Zero human intervention required for steady-state operation.

Audio Sources Handled:
  - Live class / session recordings (Dojo match rooms)
  - Workshop batch recordings (Society offline sync)
  - Coach demo evaluation audio
  - Tournament event audio
  - Student voice portfolio submissions
  - Mentor feedback recordings
  - Arena real-time audio streams

Antigravity Behavior Contract:
  Input:  Audio queue depth signal + active worker count
  Output: Worker target replica count (deterministic)
  Rule:   No worker may be spawned or killed without passing
          the ScalingDecisionGate validation chain
```

Absence of Antigravity behavior contract → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-C — TECHNOLOGY STACK LOCK (AUDIO LAYER)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
STT Engine:             Whisper (OpenAI — self-hosted OSS, open weights)
                        Model variants: tiny / base / small / medium / large-v3
                        Serving: faster-whisper (CTranslate2 backend)
                        Reason: GPU-efficient, Apache-2.0 compatible

Audio Queue:            Redis Streams (aligned with R1 stack)
                        Stream key: ecoskiller:audio:stt_queue
                        Consumer group: stt_worker_group

Worker Runtime:         Python 3.11 + FastAPI (aligned with R1)
Worker Container:       Docker image — ecoskiller/stt-worker:latest
Worker Orchestration:   Kubernetes HPA + KEDA (Kubernetes Event-Driven Autoscaler)
                        KEDA scaler: RedisStreams scaler
                        KEDA version: 2.x (self-hosted, open source)

Metrics Collector:      Prometheus (aligned with R1 stack)
Metrics Exporter:       Custom Redis-streams-exporter (self-hosted)
Autoscaling Signal:     redis_stream_pending_messages{stream="ecoskiller:audio:stt_queue"}

Object Storage:         MinIO (aligned with R1)
                        Bucket: ecoskiller-audio-transcripts
                        Path: /transcripts/{session_id}/{chunk_id}.json

Observability:          Grafana + Loki + Jaeger (aligned with v8 audit fix #7 #8)
Tracing:                OpenTelemetry — trace_id injected per audio job

CI/CD:                  Forgejo Actions + Harbor (aligned with v8 audit fix #1)
                        (GitHub Actions + GHCR REPLACED per v8 audit CRITICAL fix)
```

Absence of KEDA or Redis Streams scaler → STOP EXECUTION
Use of GitHub Actions or GHCR in pipeline → STOP EXECUTION (v8 audit violation)

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-D — AGENT ARCHITECTURE (FULL TOPOLOGY)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
┌─────────────────────────────────────────────────────────┐
│            ECOSKILLER AUDIO INGESTION LAYER             │
│                                                         │
│  Live Session  →  workshop-service  →  arena-service    │
│  Coach Demo    →  coach-service                         │
│  Tournament    →  tournament-service                    │
│  Student Voice →  enrollment-service                    │
│                           │                             │
│                    [AudioIngestGateway]                  │
│                     /audio/submit API                   │
│                           │                             │
│                    Redis Stream                         │
│          ecoskiller:audio:stt_queue                     │
│                           │                             │
│          ┌────────────────┴────────────────┐            │
│          │   KEDA ScaledObject             │            │
│          │   RedisStreams Trigger          │            │
│          │   pendingMessages threshold=10  │            │
│          └────────────────┬────────────────┘            │
│                           │                             │
│          Kubernetes HPA ← KEDA scaling decision         │
│                           │                             │
│     ┌─────────┬───────────┴────────────┬──────────┐     │
│   Pod-1     Pod-2        ...         Pod-N          │     │
│   stt-worker stt-worker           stt-worker       │     │
│   (faster-   (faster-             (faster-         │     │
│    whisper)   whisper)             whisper)        │     │
│       │           │                    │            │     │
│       └───────────┴────────────────────┘            │     │
│                   │                                  │     │
│           [Transcript Output]                        │     │
│            MinIO: /transcripts/                      │     │
│            PostgreSQL: transcript_records            │     │
│            Event: audio.transcribed → Kafka          │     │
│                   │                                  │     │
│           [Analytics Hook]                           │     │
│            ClickHouse: audio_metrics                 │     │
│            Prometheus: stt_worker_* metrics          │     │
└─────────────────────────────────────────────────────────┘
```

Absence of any topology node → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-E — DATABASE SCHEMA (MANDATORY)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```sql
-- Audio Job Registry
CREATE TABLE audio_jobs (
    job_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id      UUID NOT NULL,
    source_service  TEXT NOT NULL,       -- 'workshop' | 'arena' | 'coach' | 'tournament' | 'student'
    audio_ref       TEXT NOT NULL,       -- MinIO object path
    language_hint   TEXT DEFAULT 'auto',
    model_variant   TEXT DEFAULT 'base', -- 'tiny' | 'base' | 'small' | 'medium' | 'large-v3'
    status          TEXT NOT NULL DEFAULT 'queued',
                                         -- queued | processing | completed | failed | retrying
    priority        INT DEFAULT 5,       -- 1=urgent, 5=normal, 10=background
    enqueued_at     TIMESTAMP DEFAULT NOW(),
    started_at      TIMESTAMP,
    completed_at    TIMESTAMP,
    worker_pod_id   TEXT,
    retry_count     INT DEFAULT 0,
    error_msg       TEXT
);

-- Transcript Records
CREATE TABLE transcript_records (
    transcript_id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_id          UUID REFERENCES audio_jobs(job_id),
    session_id      UUID NOT NULL,
    content_text    TEXT NOT NULL,
    content_json    JSONB NOT NULL,      -- full Whisper segments with timestamps
    language        TEXT,
    duration_sec    FLOAT,
    word_count      INT,
    confidence_avg  FLOAT,
    model_used      TEXT,
    created_at      TIMESTAMP DEFAULT NOW()
);

-- Worker Scaling Audit Log
CREATE TABLE stt_scaling_events (
    event_id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_type      TEXT NOT NULL,       -- 'scale_up' | 'scale_down' | 'hold'
    trigger_reason  TEXT NOT NULL,
    queue_depth     INT NOT NULL,
    active_workers  INT NOT NULL,
    target_workers  INT NOT NULL,
    keda_decision   JSONB,
    created_at      TIMESTAMP DEFAULT NOW()
);

-- Audio Processing Metrics (ClickHouse compatible)
CREATE TABLE audio_metrics (
    job_id          UUID,
    worker_pod_id   TEXT,
    queue_wait_ms   BIGINT,
    processing_ms   BIGINT,
    audio_duration_sec FLOAT,
    model_variant   TEXT,
    real_time_factor FLOAT,              -- processing_ms / (audio_duration_sec * 1000)
    timestamp       TIMESTAMP DEFAULT NOW()
);
```

Absence of audio_jobs or stt_scaling_events tables → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-F — AUTOSCALING DECISION ENGINE (CORE ALGORITHM)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
ANTIGRAVITY SCALING FORMULA (DETERMINISTIC — RULE ENGINE, NOT ML):

  Aligned with R28-1: Deterministic workflows must use rule engines, NOT ML.

  INPUT SIGNALS:
    Q  = pending_messages in Redis Stream stt_queue
    W  = active_workers (current replica count)
    P  = priority_weight_factor (computed from job priority distribution)
    L  = latency_breach_flag (1 if p95 queue_wait > SLA_THRESHOLD_MS else 0)

  TARGET WORKER FORMULA:
    target_workers = ceil((Q / MESSAGES_PER_WORKER) × P) + (L × BURST_BONUS)

  CONSTANTS (startup defaults — tunable via Admin Ops Console R40):
    MESSAGES_PER_WORKER   = 10    (jobs per worker at any time)
    BURST_BONUS           = 2     (extra workers on latency breach)
    MIN_WORKERS           = 1     (floor — never scale to 0 in active hours)
    MAX_WORKERS           = 50    (ceiling — cost ceiling R25 compliant)
    SLA_THRESHOLD_MS      = 5000  (5 seconds queue wait p95 target)
    SCALE_DOWN_COOLDOWN_S = 120   (2 minutes before removing workers)
    SCALE_UP_COOLDOWN_S   = 15    (15 seconds before adding workers)

  PRIORITY WEIGHT FACTOR:
    P = 1.0   (all normal)
    P = 1.5   (if >30% jobs are priority ≤ 2)
    P = 0.7   (if all jobs are priority 10 / background)

  SCALING GATE CHECKS (ALL MUST PASS before action):
    ✔ Gate-1: Queue signal is fresh (< 10s old from Redis)
    ✔ Gate-2: Cooldown period expired (scale_up or scale_down)
    ✔ Gate-3: target_workers within [MIN_WORKERS, MAX_WORKERS]
    ✔ Gate-4: No active scaling event in progress (idempotency)
    ✔ Gate-5: Worker health ratio > 0.8 (at least 80% of workers healthy)

  IF ANY GATE FAILS:
    → HOLD (no action)
    → LOG to stt_scaling_events
    → Emit Prometheus alert if HOLD persists > 5 minutes
```

Absence of ScalingDecisionGate → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-G — KEDA ScaledObject MANIFEST (REQUIRED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# File: /infra/k8s/production/audio/keda-stt-scaledobject.yaml
apiVersion: keda.sh/v1alpha1
kind: ScaledObject
metadata:
  name: stt-worker-scaledobject
  namespace: ecoskiller-audio
  labels:
    app: stt-worker
    layer: antigravity
spec:
  scaleTargetRef:
    name: stt-worker-deployment
  pollingInterval: 15         # seconds — check every 15s (scale-up speed)
  cooldownPeriod: 120         # seconds — wait 2min before scale-down
  minReplicaCount: 1          # antigravity floor
  maxReplicaCount: 50         # cost ceiling (R25)
  triggers:
    - type: redis-streams
      metadata:
        address: redis-service.ecoskiller-infra.svc.cluster.local:6379
        stream: ecoskiller:audio:stt_queue
        consumerGroup: stt_worker_group
        lagCount: "10"          # scale 1 worker per 10 pending messages
        pendingEntriesCount: "10"
      authenticationRef:
        name: redis-keda-auth
---
apiVersion: keda.sh/v1alpha1
kind: TriggerAuthentication
metadata:
  name: redis-keda-auth
  namespace: ecoskiller-audio
spec:
  secretTargetRef:
    - parameter: password
      name: redis-secret
      key: redis-password
```

Absence of ScaledObject manifest → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-H — STT WORKER SERVICE (PYTHON — REQUIRED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```python
# File: /backend/services/stt_worker_service/main.py
# Purpose: STT Worker — pulled by KEDA, processes Redis Stream audio jobs
# Stack: Python 3.11 + faster-whisper + Redis Streams

import os, time, uuid, json
import redis
from faster_whisper import WhisperModel
from opentelemetry import trace
from prometheus_client import Counter, Histogram, start_http_server

# ── Prometheus Metrics ──────────────────────────────────────────────────────
JOBS_PROCESSED  = Counter('stt_jobs_processed_total', 'Jobs processed', ['status'])
PROCESSING_TIME = Histogram('stt_processing_duration_seconds', 'Processing time')
QUEUE_WAIT_TIME = Histogram('stt_queue_wait_seconds', 'Queue wait time')
RTF_HISTOGRAM   = Histogram('stt_real_time_factor', 'Real-time factor (lower=faster)')

# ── Constants from env ──────────────────────────────────────────────────────
REDIS_HOST      = os.getenv("REDIS_HOST", "redis-service.ecoskiller-infra")
REDIS_PORT      = int(os.getenv("REDIS_PORT", 6379))
STREAM_KEY      = "ecoskiller:audio:stt_queue"
CONSUMER_GROUP  = "stt_worker_group"
CONSUMER_NAME   = f"worker-{uuid.uuid4().hex[:8]}"
MODEL_VARIANT   = os.getenv("WHISPER_MODEL", "base")
BLOCK_MS        = 5000
MAX_RETRIES     = 3

def run():
    # Start Prometheus scrape endpoint
    start_http_server(9090)

    # Load Whisper model
    model = WhisperModel(MODEL_VARIANT, device="cpu", compute_type="int8")

    # Connect Redis
    r = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, decode_responses=True)

    # Ensure consumer group exists
    try:
        r.xgroup_create(STREAM_KEY, CONSUMER_GROUP, id='0', mkstream=True)
    except redis.exceptions.ResponseError:
        pass  # group exists

    print(f"[STT_WORKER] {CONSUMER_NAME} started. Model={MODEL_VARIANT}")

    while True:
        # Read from Redis Stream
        messages = r.xreadgroup(
            CONSUMER_GROUP, CONSUMER_NAME,
            {STREAM_KEY: '>'}, count=1, block=BLOCK_MS
        )
        if not messages:
            continue

        for stream, entries in messages:
            for msg_id, data in entries:
                process_job(r, model, msg_id, data)

def process_job(r, model, msg_id, data):
    job_id      = data.get("job_id")
    audio_path  = data.get("audio_ref")    # MinIO path resolved by worker
    enqueued_at = float(data.get("enqueued_at", time.time()))
    queue_wait  = time.time() - enqueued_at

    QUEUE_WAIT_TIME.observe(queue_wait)

    try:
        t_start = time.time()
        segments, info = model.transcribe(audio_path, beam_size=5)
        content = [{"start": s.start, "end": s.end, "text": s.text} for s in segments]
        duration = time.time() - t_start

        rtf = duration / max(info.duration, 0.001)
        RTF_HISTOGRAM.observe(rtf)
        PROCESSING_TIME.observe(duration)

        # Write transcript to PostgreSQL + MinIO (stub — wired to transcript-service)
        emit_transcript_event(job_id, content, info, duration, MODEL_VARIANT)

        r.xack(STREAM_KEY, CONSUMER_GROUP, msg_id)
        JOBS_PROCESSED.labels(status='success').inc()
        print(f"[OK] job={job_id} rtf={rtf:.2f} duration={duration:.1f}s")

    except Exception as e:
        JOBS_PROCESSED.labels(status='failed').inc()
        retry_count = int(data.get("retry_count", 0)) + 1
        if retry_count <= MAX_RETRIES:
            r.xadd(STREAM_KEY, {**data, "retry_count": retry_count})
        else:
            emit_dead_letter(job_id, str(e))
        r.xack(STREAM_KEY, CONSUMER_GROUP, msg_id)
        print(f"[FAIL] job={job_id} error={e}")

def emit_transcript_event(job_id, content, info, duration, model_used):
    # Publish to Kafka topic: audio.transcribed
    # Consumed by: longitudinal-impact-service, analytics-service, dojo-scoring-service
    pass  # Kafka producer stub — wired per R4 event schema

def emit_dead_letter(job_id, error):
    # Publish to Kafka topic: audio.failed
    pass

if __name__ == "__main__":
    run()
```

```dockerfile
# File: /backend/services/stt_worker_service/Dockerfile
FROM python:3.11-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . .
EXPOSE 9090
CMD ["python", "main.py"]
```

```
# requirements.txt
faster-whisper==1.0.3
redis==5.0.1
opentelemetry-sdk==1.24.0
prometheus-client==0.20.0
```

Absence of worker main.py or Dockerfile → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-I — AUDIO INGEST GATEWAY API (REQUIRED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# OpenAPI 3.1 — Audio Processing Endpoints
# Aligned with R3 API Contract Registry

paths:

  /audio/submit:
    post:
      summary: Submit audio job to STT queue
      requestBody:
        content:
          application/json:
            schema:
              type: object
              required: [session_id, audio_ref, source_service]
              properties:
                session_id:    { type: string, format: uuid }
                audio_ref:     { type: string }   # MinIO path
                source_service:{ type: string }   # workshop|arena|coach|tournament|student
                language_hint: { type: string, default: auto }
                model_variant: { type: string, default: base }
                priority:      { type: integer, default: 5 }
      responses:
        "202": { description: Job enqueued, schema: { job_id: uuid } }
        "422": { description: Validation error }

  /audio/jobs/{job_id}:
    get:
      summary: Get audio job status
      responses:
        "200": { description: Job status + transcript if completed }

  /audio/transcripts/{session_id}:
    get:
      summary: Get all transcripts for a session
      responses:
        "200": { description: Transcript list }

  /audio/worker/health:
    get:
      summary: Worker fleet health (active count, queue depth, p95 latency)
      responses:
        "200": { description: Worker fleet status }

  /audio/worker/scale-status:
    get:
      summary: Current KEDA scaling state + last scaling event
      responses:
        "200": { description: KEDA scaling snapshot }
```

Absence of /audio/submit API contract → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-J — KAFKA EVENT CONTRACTS (ALIGNED WITH R4)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# AsyncAPI 2.6.0 — Audio Processing Events

channels:

  audio.submitted:
    publish:
      message:
        payload:
          job_id:         uuid
          session_id:     uuid
          source_service: string
          priority:       integer
          enqueued_at:    timestamp

  audio.transcribed:
    publish:
      message:
        payload:
          job_id:          uuid
          session_id:      uuid
          transcript_id:   uuid
          language:        string
          duration_sec:    float
          word_count:      integer
          confidence_avg:  float
          model_used:      string
          completed_at:    timestamp

  audio.failed:
    publish:
      message:
        payload:
          job_id:    uuid
          error_msg: string
          retries:   integer

  audio.worker.scaled:
    publish:
      message:
        payload:
          event_type:     string   # scale_up | scale_down | hold
          from_replicas:  integer
          to_replicas:    integer
          trigger_reason: string
          queue_depth:    integer
          timestamp:      timestamp

# Consumers of audio.transcribed:
#   - longitudinal-impact-service (alumni skill tracking)
#   - society-analytics-service (workshop attendance validation)
#   - scoring-offline-service (tournament result linkage)
#   - dojo-service (Dojo live session replay indexing)
```

Absence of audio.transcribed event → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-K — PROMETHEUS METRICS CONTRACT (REQUIRED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
MANDATORY METRICS (all workers must expose on :9090/metrics):

  stt_jobs_processed_total{status="success|failed|retried"}
    Type: Counter
    Description: Total jobs handled by this worker

  stt_processing_duration_seconds{quantile="0.5|0.95|0.99"}
    Type: Histogram
    Description: Time from job pickup to transcript complete

  stt_queue_wait_seconds{quantile="0.5|0.95|0.99"}
    Type: Histogram
    Description: Time job spent in Redis queue before pickup

  stt_real_time_factor{quantile="0.5|0.95"}
    Type: Histogram
    Description: Processing time / audio duration (lower = faster model)
    SLA Target: p95 < 0.5 (process faster than 2x real-time on base model)

  stt_active_workers
    Type: Gauge
    Description: Current replica count (from KEDA)

  stt_queue_depth
    Type: Gauge
    Description: Current pending messages in Redis Stream

  stt_model_load_seconds
    Type: Gauge
    Description: Time taken to load Whisper model on pod startup

GRAFANA DASHBOARD REQUIRED:
  - Queue depth over time
  - Active workers over time (overlay)
  - p95 processing latency
  - Real-time factor distribution
  - Scale-up / scale-down events timeline
  - Failed job rate
  - Worker pod restart rate
```

Absence of any mandatory metric → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-L — KUBERNETES DEPLOYMENT MANIFEST (REQUIRED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# File: /infra/k8s/production/audio/stt-worker-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: stt-worker-deployment
  namespace: ecoskiller-audio
  labels:
    app: stt-worker
    layer: antigravity
spec:
  replicas: 1          # KEDA manages this at runtime
  selector:
    matchLabels:
      app: stt-worker
  template:
    metadata:
      labels:
        app: stt-worker
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "9090"
    spec:
      containers:
        - name: stt-worker
          image: harbor.ecoskiller.internal/ecoskiller/stt-worker:latest
          ports:
            - containerPort: 9090   # Prometheus metrics
          resources:
            requests:
              cpu: "500m"
              memory: "1Gi"
            limits:
              cpu: "2"
              memory: "4Gi"
          env:
            - name: REDIS_HOST
              valueFrom:
                configMapKeyRef:
                  name: audio-config
                  key: redis_host
            - name: WHISPER_MODEL
              valueFrom:
                configMapKeyRef:
                  name: audio-config
                  key: whisper_model
            - name: REDIS_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: redis-secret
                  key: redis-password
          livenessProbe:
            httpGet:
              path: /health
              port: 9090
            initialDelaySeconds: 30
            periodSeconds: 15
          readinessProbe:
            httpGet:
              path: /ready
              port: 9090
            initialDelaySeconds: 20
            periodSeconds: 10
      terminationGracePeriodSeconds: 300   # Allow current job to finish
```

```yaml
# File: /infra/k8s/production/audio/audio-config.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: audio-config
  namespace: ecoskiller-audio
data:
  redis_host: "redis-service.ecoskiller-infra.svc.cluster.local"
  redis_port: "6379"
  whisper_model: "base"         # Override per environment
  minio_endpoint: "minio.ecoskiller-infra.svc.cluster.local:9000"
  minio_bucket: "ecoskiller-audio-transcripts"
  kafka_brokers: "kafka.ecoskiller-infra.svc.cluster.local:9092"
  max_retries: "3"
  sla_threshold_ms: "5000"
```

Absence of deployment manifest or ConfigMap → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-M — ALERT RULES (GRAFANA / PROMETHEUS)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# File: /infra/monitoring/stt-worker-alerts.yaml
groups:
  - name: stt_worker_antigravity_alerts
    rules:

      - alert: STT_QueueDepthCritical
        expr: stt_queue_depth > 500
        for: 2m
        labels:
          severity: critical
          layer: antigravity
        annotations:
          summary: "STT queue depth critical — possible autoscaling failure"
          description: "Queue depth {{ $value }} exceeds 500. Check KEDA ScaledObject."

      - alert: STT_WorkerScaleStuck
        expr: (stt_queue_depth > 100) and (stt_active_workers < 3)
        for: 5m
        labels:
          severity: high
        annotations:
          summary: "STT workers not scaling up despite high queue"

      - alert: STT_HighFailureRate
        expr: rate(stt_jobs_processed_total{status="failed"}[5m]) > 0.1
        for: 3m
        labels:
          severity: high
        annotations:
          summary: "STT job failure rate exceeds 10%"

      - alert: STT_LatencyBreachP95
        expr: histogram_quantile(0.95, stt_queue_wait_seconds_bucket) > 10
        for: 5m
        labels:
          severity: medium
        annotations:
          summary: "STT p95 queue wait exceeds 10 seconds"

      - alert: STT_WorkerAllDown
        expr: stt_active_workers == 0
        for: 1m
        labels:
          severity: critical
        annotations:
          summary: "ALL STT workers are down — audio processing halted"
```

Absence of alert rules → STOP EXECUTION

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-N — ADMIN OPS CONSOLE INTEGRATION (R40 ALIGNED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
STT Worker Autoscaling Agent must be visible and controllable
from the ECOSKILLER Internal Admin & Ops Console (R40).

REQUIRED CONSOLE MODULE: Audio Processing Control Panel

  Module: System Operations → Audio Processing

  Controls exposed to Super Admin:

    1. Real-time queue depth gauge
    2. Active worker count display
    3. p95 latency SLA indicator (green/amber/red)
    4. Scaling event timeline (last 24h)
    5. Manual scale-override (force MIN or MAX replicas)
       — requires justification text
       — emits audit event: audio.worker.manual_override
    6. Worker model variant selector
       (switch between tiny / base / small / medium / large-v3)
    7. Kill-switch: pause all audio ingestion
       (drains queue, halts new jobs, preserves existing)
    8. Replay failed jobs (bulk retry from dead-letter)
    9. Download transcript export (CSV / JSON)
   10. Cost estimate display:
       current_workers × estimated_cpu_cost_per_hour

  All actions are:
    ✔ Audit-logged (audit_logs table)
    ✔ Reversible (except transcript deletion)
    ✔ Protected by Super Admin RBAC + MFA

  Absence of Audio Processing Console Module → STOP EXECUTION
```

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-O — INTERN EXECUTION STEPS (R26 / R46 ALIGNED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
INTERN ROLE: Python Backend Developer + Basic Kubernetes Intern

OBJECTIVE: Run STT Worker Autoscaling Agent locally

────────────────────────────────────────
STEP 1 — Install Prerequisites
────────────────────────────────────────
Install Docker + kubectl + Minikube + KEDA

Install KEDA on Minikube:
  kubectl apply --server-side -f \
    https://github.com/kedacore/keda/releases/download/v2.14.0/keda-2.14.0.yaml

Expected: KEDA pods running in keda namespace
  kubectl get pods -n keda

────────────────────────────────────────
STEP 2 — Create Audio Namespace
────────────────────────────────────────
  kubectl create namespace ecoskiller-audio

────────────────────────────────────────
STEP 3 — Start Redis (local)
────────────────────────────────────────
  kubectl apply -f /infra/k8s/dev/redis-deployment.yaml

────────────────────────────────────────
STEP 4 — Build STT Worker Image
────────────────────────────────────────
  cd /backend/services/stt_worker_service/
  docker build -t stt-worker:local .

Load into Minikube:
  minikube image load stt-worker:local

────────────────────────────────────────
STEP 5 — Apply Kubernetes Manifests
────────────────────────────────────────
  kubectl apply -f /infra/k8s/dev/audio/audio-config.yaml
  kubectl apply -f /infra/k8s/dev/audio/stt-worker-deployment.yaml
  kubectl apply -f /infra/k8s/dev/audio/keda-stt-scaledobject.yaml

────────────────────────────────────────
STEP 6 — Verify Worker Running
────────────────────────────────────────
  kubectl get pods -n ecoskiller-audio

Expected: 1 pod STATUS = Running

────────────────────────────────────────
STEP 7 — Test Autoscaling
────────────────────────────────────────
Submit 50 test messages to Redis Stream:
  python /tools/audio/seed_queue.py --count 50

Watch scaling:
  kubectl get hpa -n ecoskiller-audio -w

Expected: Replica count increases from 1 → 5

────────────────────────────────────────
STEP 8 — Verify Metrics
────────────────────────────────────────
Port-forward a worker pod:
  kubectl port-forward pod/<pod-name> 9090:9090 -n ecoskiller-audio

Open: http://localhost:9090/metrics
Expected: stt_queue_depth, stt_active_workers, stt_jobs_processed_total visible

────────────────────────────────────────
SUCCESS CONDITION
────────────────────────────────────────
  ✔ Workers scale up when queue fills
  ✔ Workers scale down after cooldown
  ✔ Metrics visible in Prometheus
  ✔ Transcripts written to MinIO
  ✔ Events emitted to Kafka topic audio.transcribed

Failure at any step → STOP EXECUTION
```

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-P — COST GOVERNANCE (R25 ALIGNED)
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
Cost-Aware Defaults (Startup-Stage Budget):

  MIN_WORKERS:              1     (always-on floor)
  MAX_WORKERS:              50    (hard ceiling)
  DEFAULT_MODEL:            base  (fastest cost/quality ratio)
  CPU REQUEST per worker:   500m  (~0.5 vCPU)
  MEMORY REQUEST:           1 Gi
  CPU LIMIT:                2 vCPU
  MEMORY LIMIT:             4 Gi

Estimated Monthly Cost at Max Scale (GCP e2-standard-4):
  50 workers × ~$0.134/hr × ~730hr = ~$4,890/month (theoretical max)
  Typical steady-state (3–5 workers): ~$300–500/month

Cost Optimization Rules:
  ✔ Use model=tiny for background/low-priority jobs (2× faster, 4× cheaper)
  ✔ Use model=base for standard jobs
  ✔ Use model=medium ONLY for tournament/certification audio (accuracy critical)
  ✔ Use model=large-v3 ONLY on explicit admin override

Absence of cost-aware model routing → STOP EXECUTION (R25 violation)
```

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# SECTION AG-Q — PRODUCTION READINESS CHECKLIST
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
No STT_WORKER_AUTOSCALING_AGENT may be declared production-ready unless:

  ✔ KEDA ScaledObject deployed and healthy
  ✔ Redis Stream consumer group created
  ✔ audio_jobs and transcript_records tables migrated
  ✔ Worker Docker image built and pushed to Harbor (NOT GHCR)
  ✔ Prometheus metrics scraping active
  ✔ Grafana dashboard deployed
  ✔ All 5 alert rules active
  ✔ Kafka topics audio.submitted / audio.transcribed / audio.failed active
  ✔ MinIO bucket ecoskiller-audio-transcripts exists
  ✔ Admin Ops Console audio module deployed
  ✔ Forgejo CI pipeline builds and deploys worker (NOT GitHub Actions)
  ✔ Scale-up test passed (queue flood → workers respond in < 30s)
  ✔ Scale-down test passed (queue empty → workers reduce after cooldown)
  ✔ Dead-letter retry verified
  ✔ Contract validator (R49) passes for all audio APIs
  ✔ QA generator (R50) produces audio worker test suite
  ✔ R25 cost estimate documented and approved

Failure of any check → STOP EXECUTION → REPORT AUDIO-AGENT-NOT-READY
```

---

# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
# 🔒 SEAL BLOCK
# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
STT_WORKER_AUTOSCALING_AGENT — ANTIGRAVITY AUDIO PROCESSING
════════════════════════════════════════════════════════════

SYSTEM:           ECOSKILLER Unified SaaS
LAYER:            Audio Processing — Antigravity
AGENT VERSION:    v1.0.0
SEALED DATE:      2026-03-04
SEALED BY:        Master Execution Prompt v12.0

STACK LOCK:                       ACTIVE
KEDA AUTOSCALING:                 ACTIVE
REDIS STREAMS TRIGGER:            ACTIVE
WHISPER ENGINE:                   LOCKED (OSS — self-hosted)
GITHUB ACTIONS:                   FORBIDDEN (v8 audit fix #1)
GHCR:                             FORBIDDEN (v8 audit fix #1)
FORGEJO + HARBOR:                 REQUIRED
COST CEILING:                     50 WORKERS MAX
ML IN SCALING DECISIONS:          FORBIDDEN (R28-1)
RULE ENGINE:                      REQUIRED
ADMIN OPS CONSOLE:                REQUIRED (R40)
INTERN EXECUTABLE:                REQUIRED (R46)
CONTRACT VALIDATOR GATE:          REQUIRED (R49)
QA TEST GENERATOR GATE:           REQUIRED (R50)
MUTATION POLICY:                  ADD-ONLY VIA VERSION BUMP
INTERPRETATION AUTHORITY:         NONE
EXECUTION AUTHORITY:              HUMAN DECLARATION ONLY

Violation of any seal → STOP EXECUTION
→ REPORT STT-AGENT-SEAL-VIOLATION
→ NO DEPLOYMENT CLAIM PERMITTED
════════════════════════════════════════════════════════════
```
