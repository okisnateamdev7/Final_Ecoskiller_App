# SYSTEM_PERFORMANCE_OPTIMIZATION_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### ECOSKILLER PLATFORM — ANTIGRAVITY SUBSYSTEM

---

```
DOCUMENT CLASS      : ENTERPRISE INFRASTRUCTURE AGENT SPEC
AGENT ID            : SPOA-002
VERSION             : v1.0.0 — SEALED
STATUS              : LOCKED · GOVERNED · DETERMINISTIC
MUTATION POLICY     : ADD-ONLY VIA VERSION BUMP
EXECUTION AUTHORITY : HUMAN DECLARATION ONLY
INTERPRETATION      : NONE PERMITTED
FAILURE MODE        : STOP → REPORT → NO PARTIAL EXECUTION
CLASSIFICATION      : ENTERPRISE OPTIMIZATION — NON-OPTIONAL
DEPENDS ON          : BRA-001 (Backup & Recovery Agent must be operational)
```

---

## SECTION 0 — AGENT DECLARATION

This document defines the **SYSTEM_PERFORMANCE_OPTIMIZATION_AGENT (SPOA)** — a fully
deterministic, protocol-governed performance engineering system for the Ecoskiller platform
operating under the ANTIGRAVITY Enterprise Optimization + Trust Infrastructure layer.

**The SPOA is not a tuning guide. It is enforcement infrastructure for performance contracts.**

Performance without measurement → `REJECTED`
Optimization without baseline → `REJECTED`
Claim of readiness without load test → `STOP EXECUTION`
Runtime deviation from SLO targets → `ALERT → INCIDENT → ENFORCE`

This agent governs: caching strategy, database query performance, API gateway optimization,
real-time subsystem latency, CDN/static asset delivery, media stack performance, event bus
throughput, frontend rendering performance, and platform-wide SLO enforcement.

---

## SECTION 1 — PERFORMANCE CONTRACT (NON-NEGOTIABLE)

These are the platform-wide SLO commitments. Every subsystem is bound to them.
Violation of any SLO triggers an incident and blocks deployment gating.

### 1.1 API Response Time SLOs

| Endpoint Class | P50 Target | P95 Target | P99 Target | Hard Ceiling |
|---|---|---|---|---|
| Auth (login, token refresh) | < 80ms | < 150ms | < 250ms | 500ms |
| Job search (OpenSearch) | < 120ms | < 300ms | < 500ms | 800ms |
| Job apply (write) | < 100ms | < 200ms | < 400ms | 600ms |
| GD session join | < 200ms | < 400ms | < 600ms | 1000ms |
| GD turn command (mute/unmute) | < 50ms | < 100ms | < 150ms | 200ms |
| Dojo match state update | < 60ms | < 120ms | < 200ms | 300ms |
| Billing / invoice generation | < 300ms | < 600ms | < 1000ms | 2000ms |
| Notification dispatch (async) | < 500ms | < 1000ms | < 2000ms | 5000ms |
| Dashboard data load | < 200ms | < 500ms | < 800ms | 1500ms |
| Intelligence score read | < 100ms | < 200ms | < 350ms | 600ms |
| Idea submission (write) | < 150ms | < 300ms | < 500ms | 800ms |
| Royalty balance read | < 100ms | < 200ms | < 350ms | 500ms |
| Admin governance queries | < 300ms | < 700ms | < 1200ms | 2500ms |

### 1.2 System-Level SLOs

| Metric | Target | Critical Threshold |
|---|---|---|
| Platform availability | 99.9% monthly | < 99.5% = P1 incident |
| GD session availability | 99.95% during active hours | Any session drop = P1 |
| API error rate (5xx) | < 0.1% | > 0.5% = alert, > 1% = incident |
| Database query P95 | < 50ms (cached) / < 200ms (cold) | > 500ms = alert |
| Redis command latency | < 2ms P99 | > 10ms = alert |
| Kafka consumer lag | < 500 events per topic | > 5000 = alert |
| WebSocket message delivery | < 20ms P99 | > 100ms = incident |
| Media (Jitsi/LiveKit) join time | < 3 seconds | > 8 seconds = incident |
| Flutter app cold start | < 2.5 seconds | > 5 seconds = alert |
| Next.js SSR time (SEO layer) | < 200ms TTFB | > 800ms = alert |

### 1.3 Throughput Targets

| Traffic Class | Baseline | Peak (3× baseline) | Maximum Tested |
|---|---|---|---|
| HTTP API requests | 500 req/sec | 1500 req/sec | 5000 req/sec |
| WebSocket connections | 2000 concurrent | 6000 concurrent | 20000 concurrent |
| GD sessions concurrent | 50 sessions | 150 sessions | 500 sessions |
| Dojo matches concurrent | 100 matches | 300 matches | 1000 matches |
| Kafka events/sec | 10,000 | 30,000 | 100,000 |
| Search queries/sec | 200 | 600 | 2000 |
| ClickHouse insert rows/sec | 50,000 | 150,000 | 500,000 |

---

## SECTION 2 — AGENT IDENTITY AND PLACEMENT

### 2.1 Architectural Position

```
┌─────────────────────────────────────────────────────────────────────┐
│               ENTERPRISE OPTIMIZATION + TRUST LAYER                 │
│                                                                     │
│  ┌──────────────┐  ┌──────────────┐  ┌─────────────────────────┐   │
│  │  BRA-001     │  │  Compliance  │  │  SPOA-002               │   │
│  │  Backup &    │  │  Engine      │  │  SYSTEM PERFORMANCE     │   │
│  │  Recovery    │  │              │  │  OPTIMIZATION AGENT     │   │
│  └──────────────┘  └──────────────┘  └─────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                                │
     ┌──────────┬───────────────┼────────────┬───────────┐
     ▼          ▼               ▼            ▼           ▼
  Gateway    Data Layer    Realtime     Media Stack   Frontend
 (Kong +    (PG + Redis +  (GD + Dojo   (Jitsi +     (Flutter +
  Envoy +    CH + OS)       WebSocket)   LiveKit)      Next.js)
  NGINX)
```

### 2.2 Namespace and Service Assignment

```
kubernetes namespace : ops (observability), core/realtime (enforcement)
agent responsibilities:
  - SLO contract enforcement via Prometheus alerting rules
  - Cache strategy governance (Redis + NGINX + Kong)
  - Database query performance enforcement (PostgreSQL + ClickHouse)
  - API gateway optimization (Kong + Envoy + NGINX)
  - Event bus throughput management (Kafka + RabbitMQ)
  - Real-time subsystem performance (GD + Dojo + WebSocket)
  - Media stack performance (Jitsi + LiveKit + coturn)
  - Frontend performance contracts (Flutter + Next.js)
  - Load test gate enforcement (Airflow DAGs)
  - Continuous SLO regression detection (Prometheus + Grafana)
```

---

## SECTION 3 — CACHING STRATEGY (LOCKED)

### 3.1 Cache Layer Architecture

```
Request → Kong (L1: Gateway Cache) → NGINX (L2: Static Cache)
       → Redis (L3: Application Cache) → PostgreSQL (Source)

Rule: Never bypass cache layers for read-heavy paths.
Rule: Cache invalidation is event-driven (Kafka), never TTL-only for mutable data.
Rule: No cache warming is manual. All warming is automated at deploy time.
```

### 3.2 Redis Cache Domains (Classified)

| Cache Domain | Key Pattern | TTL | Eviction | Invalidation Trigger |
|---|---|---|---|---|
| Auth JWT tokens | `jwt:{user_id}:{device_id}` | 15 min (access) | LRU | logout, MFA change |
| User profile (read) | `profile:{user_id}` | 30 min | LRU | profile.updated event |
| RBAC permissions | `rbac:{user_id}:{tenant_id}` | 10 min | LRU | role.changed event |
| Job listing page | `jobs:page:{page}:{filters_hash}` | 5 min | LRU | job.created/updated event |
| Job detail | `job:{job_id}` | 15 min | LRU | job.updated event |
| Candidate skill vector | `skill_vec:{user_id}` | 60 min | LFU | skill.updated event |
| Intelligence DNA | `intel_dna:{user_id}` | 120 min | LFU | intelligence.recalculated event |
| GD active session state | `gd:session:{session_id}` | TTL = session_duration | NONE | session.completed event |
| GD turn queue | `gd:queue:{session_id}` | TTL = session_duration | NONE | session.completed event |
| Dojo match state | `dojo:match:{match_id}` | TTL = match_duration | NONE | match.completed event |
| OTP codes | `otp:{user_id}:{type}` | 10 min | NONE | on first use or expiry |
| Rate limit counters | `rl:{user_id}:{endpoint}` | Sliding window | NONE | N/A |
| Slot booking locks | `lock:slot:{slot_id}` | 5 min | NONE | booking.confirmed event |
| Royalty balance (read) | `royalty:balance:{user_id}` | 5 min | LRU | royalty.credited event |
| Billing plan entitlements | `plan:{tenant_id}:{feature}` | 30 min | LRU | subscription.updated event |
| Feature flags | `flag:{flag_id}:{tenant_id}` | 2 min | LRU | Unleash sync event |
| Search suggestion cache | `suggest:{prefix}:{domain}` | 10 min | LRU | index.updated event |

### 3.3 Redis Performance Configuration (Locked)

```yaml
# Redis Production Config — Performance Tuned
maxmemory: 8gb
maxmemory-policy: allkeys-lru
hz: 20
tcp-keepalive: 60
timeout: 300
tcp-backlog: 511
databases: 4
  DB 0: GD state machines + Dojo match state (no eviction — critical)
  DB 1: Application cache (LRU eviction)
  DB 2: Rate limiting + OTPs + locks
  DB 3: Session tokens + auth cache
lazyfree-lazy-eviction: yes
lazyfree-lazy-expire: yes
lazyfree-lazy-server-del: yes
activerehashing: yes
no-appendfsync-on-rewrite: yes
list-max-ziplist-size: -2
hash-max-ziplist-entries: 128
hash-max-ziplist-value: 64
```

### 3.4 Kong Gateway Cache (L1)

```
Plugin: proxy-cache
Config:
  cache_ttl: 300 seconds (read-only endpoints only)
  strategy: memory
  cache_control: true
  vary_headers: [Authorization, X-Tenant-ID]
  response_code: [200, 301, 404]

Cached routes (GET only):
  /api/v1/jobs/{id}
  /api/v1/jobs/search
  /api/v1/candidates/public/{id}
  /api/v1/intelligence/public-profile/{id}
  /api/v1/innovation/marketplace/listings

NOT cached (strict):
  Any POST/PUT/PATCH/DELETE
  /api/v1/gd/*
  /api/v1/dojo/*
  /api/v1/billing/*
  /api/v1/auth/*
  /api/v1/royalty/*
```

### 3.5 NGINX Static Asset Cache (L2)

```nginx
# Static asset cache configuration
location ~* \.(js|css|png|jpg|ico|svg|woff2|ttf)$ {
    expires 1y;
    add_header Cache-Control "public, immutable";
    add_header Vary "Accept-Encoding";
    gzip_static on;
    brotli_static on;
}

# Next.js SSR pages — short cache
location ~* ^/_next/static/ {
    expires 1y;
    add_header Cache-Control "public, immutable";
}

# API proxy — no cache at NGINX level (handled by Kong)
location /api/ {
    proxy_cache off;
    proxy_buffering on;
    proxy_buffer_size 16k;
    proxy_buffers 8 32k;
}
```

---

## SECTION 4 — DATABASE PERFORMANCE (LOCKED)

### 4.1 PostgreSQL Performance Configuration

```
# postgresql.conf — Production Performance Tuning
# Assumes: 16 CPU cores, 32GB RAM, NVMe SSD

max_connections = 200          # PgBouncer manages connection pool
shared_buffers = 8GB           # 25% of RAM
effective_cache_size = 24GB    # 75% of RAM
maintenance_work_mem = 2GB
checkpoint_completion_target = 0.9
wal_buffers = 64MB
default_statistics_target = 100
random_page_cost = 1.1         # SSD-optimized
effective_io_concurrency = 200 # SSD-optimized
work_mem = 128MB               # Per-sort, per-hash-join
min_wal_size = 1GB
max_wal_size = 4GB
max_worker_processes = 16
max_parallel_workers_per_gather = 8
max_parallel_workers = 16
max_parallel_maintenance_workers = 4
jit = on
```

### 4.2 PgBouncer Connection Pooling (Mandatory)

```ini
[pgbouncer]
pool_mode = transaction          # Transaction pooling for microservices
max_client_conn = 2000
default_pool_size = 20
min_pool_size = 5
reserve_pool_size = 5
reserve_pool_timeout = 3
server_idle_timeout = 600
client_idle_timeout = 0
max_db_connections = 100
query_wait_timeout = 30
```

**Pool allocation by service:**

| Service | Pool Size | Max Client Conn |
|---|---|---|
| Auth Service | 15 | 300 |
| User Service | 10 | 200 |
| Job Service | 15 | 200 |
| Application Service | 10 | 150 |
| Voice GD Orchestrator | 20 | 100 |
| Dojo Match Engine | 20 | 100 |
| Billing Service | 10 | 100 |
| Royalty Accounting Engine | 10 | 100 |
| Scoring Engine | 10 | 150 |
| Analytics Service (read) | 5 | 50 |

### 4.3 Mandatory Index Policy

All indexes must be declared in Flyway migrations. No ad-hoc index creation in production.

**Core Index Definitions:**

```sql
-- Auth & Users
CREATE INDEX CONCURRENTLY idx_users_email ON users(email) WHERE deleted_at IS NULL;
CREATE INDEX CONCURRENTLY idx_users_tenant ON users(tenant_id, status);
CREATE INDEX CONCURRENTLY idx_sessions_user_device ON user_sessions(user_id, device_id);
CREATE INDEX CONCURRENTLY idx_sessions_token_hash ON user_sessions(token_hash);

-- Jobs
CREATE INDEX CONCURRENTLY idx_jobs_tenant_status ON jobs(tenant_id, status, created_at DESC);
CREATE INDEX CONCURRENTLY idx_jobs_domain ON jobs(domain_track, status) WHERE status = 'active';
CREATE INDEX CONCURRENTLY idx_applications_user ON job_applications(user_id, status);
CREATE INDEX CONCURRENTLY idx_applications_job ON job_applications(job_id, stage);

-- GD Sessions
CREATE INDEX CONCURRENTLY idx_gd_batch_session ON gd_sessions(batch_id, status);
CREATE INDEX CONCURRENTLY idx_gd_participant ON gd_participants(session_id, user_id);
CREATE INDEX CONCURRENTLY idx_gd_scores ON gd_scores(session_id, user_id);

-- Dojo
CREATE INDEX CONCURRENTLY idx_dojo_matches_user ON dojo_matches(user_id, status, created_at DESC);
CREATE INDEX CONCURRENTLY idx_dojo_belts ON dojo_belts(user_id, domain, level);

-- Intelligence
CREATE INDEX CONCURRENTLY idx_intel_profile ON intelligence_profiles(user_id, recalculated_at DESC);
CREATE INDEX CONCURRENTLY idx_intel_events ON intelligence_events(user_id, event_type, created_at DESC);

-- Billing
CREATE INDEX CONCURRENTLY idx_invoices_tenant ON invoices(tenant_id, created_at DESC);
CREATE INDEX CONCURRENTLY idx_subscriptions_tenant ON subscriptions(tenant_id, status);

-- Royalty (Partial indexes for active contracts)
CREATE INDEX CONCURRENTLY idx_royalty_contracts_active
  ON royalty_contracts(idea_id, business_id) WHERE status = 'active';
CREATE INDEX CONCURRENTLY idx_royalty_ledger_user
  ON royalty_ledger(user_id, created_at DESC);

-- Innovation
CREATE INDEX CONCURRENTLY idx_ideas_status ON ideas(status, domain, created_at DESC);
CREATE INDEX CONCURRENTLY idx_ideas_owner ON ideas(owner_id, visibility);
```

### 4.4 Slow Query Enforcement Policy

```
pg_stat_statements: ENABLED (track_all)
log_min_duration_statement = 200ms    (log any query > 200ms)
log_slow_query_alert = 500ms          (Prometheus alert threshold)
log_slow_query_incident = 2000ms      (PagerDuty incident threshold)

Weekly automated slow query report:
  - Airflow DAG: spoa_weekly_slow_query_report
  - Sources: pg_stat_statements, pg_stat_user_indexes
  - Output: Top 20 queries by total_time, unused indexes
  - Destination: Admin governance dashboard + Slack ops channel

Zero-tolerance queries (must never appear in production):
  - Sequential scan on any table > 10,000 rows
  - N+1 query patterns (detected via OpenTelemetry trace analysis)
  - Queries without tenant_id filter on multi-tenant tables
```

### 4.5 ClickHouse Performance Configuration

```sql
-- ClickHouse production settings
-- Partition by month for all time-series tables

CREATE TABLE gd_metrics (
    session_id UUID,
    user_id UUID,
    tenant_id UUID,
    speaking_seconds Float32,
    turns_completed UInt8,
    turns_skipped UInt8,
    interrupt_attempts UInt8,
    silence_seconds Float32,
    event_date Date,
    created_at DateTime
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(event_date)
ORDER BY (tenant_id, session_id, user_id)
TTL event_date + INTERVAL 2 YEAR;

-- Materialized views for dashboard queries (pre-aggregated)
CREATE MATERIALIZED VIEW gd_daily_summary
ENGINE = SummingMergeTree()
PARTITION BY toYYYYMM(event_date)
ORDER BY (tenant_id, event_date)
AS SELECT
    tenant_id,
    event_date,
    count() as sessions_count,
    avg(speaking_seconds) as avg_speaking,
    sum(interrupt_attempts) as total_interrupts
FROM gd_metrics GROUP BY tenant_id, event_date;
```

```
ClickHouse Performance Rules:
  - All analytical queries use materialized views (pre-aggregated)
  - Dashboard queries must NOT run against raw event tables
  - max_execution_time = 30 seconds (hard limit per query)
  - max_memory_usage = 4GB per query
  - max_threads = 8 per query (prevent resource monopolization)
  - Async inserts: ENABLED (batch_size=10000, max_delay=1s)
  - Compression: LZ4 for hot data, ZSTD for cold partitions
```

### 4.6 OpenSearch Performance Configuration

```yaml
# opensearch.yml — Performance tuning
indices.memory.index_buffer_size: "20%"
indices.queries.cache.size: "15%"
thread_pool.search.queue_size: 1000
thread_pool.write.queue_size: 500
search.max_buckets: 10000

# Index settings for high-query indices
PUT /candidates/_settings
{
  "index": {
    "number_of_shards": 3,
    "number_of_replicas": 1,
    "refresh_interval": "5s",
    "max_result_window": 10000
  }
}

# Required index mappings (type enforcement — no dynamic mapping)
# Dynamic mapping = OFF on all production indices
PUT /jobs/_mappings
{
  "dynamic": "strict",
  "properties": {
    "title": { "type": "text", "analyzer": "standard" },
    "domain": { "type": "keyword" },
    "tenant_id": { "type": "keyword" },
    "status": { "type": "keyword" },
    "salary_min": { "type": "integer" },
    "salary_max": { "type": "integer" },
    "created_at": { "type": "date" }
  }
}
```

---

## SECTION 5 — API GATEWAY PERFORMANCE (LOCKED)

### 5.1 Kong Performance Configuration

```yaml
# Kong performance configuration
nginx_worker_processes: auto
nginx_worker_connections: 16384
nginx_keepalive_requests: 10000
nginx_keepalive_timeout: 75s
nginx_proxy_read_timeout: 60s
nginx_proxy_send_timeout: 60s
upstream_keepalive_pool_size: 60
upstream_keepalive_max_requests: 1000
upstream_keepalive_idle_timeout: 60s

# Rate limiting (per user + per IP, enforced at Kong level)
plugins:
  rate-limiting:
    second: 50        # per authenticated user
    minute: 1000
    hour: 10000
    policy: redis     # distributed rate limiting via Redis
    fault_tolerant: true
    redis_host: redis-service.core
    redis_port: 6379
    redis_database: 2

  # Request size limiting
  request-size-limiting:
    allowed_payload_size: 10    # MB, default
    size_unit: megabytes

  # Response compression
  response-transformer:
    add.headers: ["Vary: Accept-Encoding"]

  # Circuit breaker via Envoy sidecar (see 5.2)
```

### 5.2 Envoy Circuit Breaker Configuration

```yaml
# Envoy circuit breaker — all upstream services
circuit_breakers:
  thresholds:
    - priority: DEFAULT
      max_connections: 1024
      max_pending_requests: 1024
      max_requests: 2048
      max_retries: 3
      retry_on: 5xx,gateway-error,connect-failure,reset
      num_retries: 2
      retry_back_off:
        base_interval: 100ms
        max_interval: 1000ms
      track_remaining: true

# Outlier detection (automatic host ejection on failure)
outlier_detection:
  consecutive_5xx: 5
  interval: 10s
  base_ejection_time: 30s
  max_ejection_percent: 50
  success_rate_minimum_hosts: 5
  success_rate_request_volume: 100
  success_rate_stdev_factor: 1900
```

### 5.3 NGINX Ingress Tuning

```nginx
# NGINX ingress performance — global
worker_processes auto;
worker_rlimit_nofile 65535;

events {
    worker_connections 16384;
    use epoll;
    multi_accept on;
}

http {
    keepalive_timeout 75s;
    keepalive_requests 10000;
    sendfile on;
    tcp_nopush on;
    tcp_nodelay on;
    server_tokens off;

    # Gzip
    gzip on;
    gzip_comp_level 4;
    gzip_min_length 256;
    gzip_types text/plain text/css application/json application/javascript
               application/x-javascript text/xml application/xml
               application/xml+rss text/javascript image/svg+xml;

    # Upstream keepalive
    upstream backend {
        keepalive 64;
        keepalive_requests 1000;
        keepalive_timeout 75s;
    }

    # Buffer tuning
    client_body_buffer_size 16k;
    client_max_body_size 10m;
    proxy_buffer_size 16k;
    proxy_buffers 8 32k;
    proxy_busy_buffers_size 64k;
}
```

---

## SECTION 6 — REAL-TIME SUBSYSTEM PERFORMANCE (LOCKED)

### 6.1 Voice GD Orchestrator Performance

The GD orchestrator is the most latency-critical service in the platform.
Turn commands (mute/unmute) must be delivered and confirmed in < 50ms P95.

```
WEBSOCKET CONNECTION MANAGEMENT:
  Server: Node.js (cluster mode, worker_threads = CPU cores)
  Library: uWebSockets.js (NOT socket.io — 5× lower overhead)
  Protocol: WebSocket binary frames (MessagePack encoding, NOT JSON)
  Compression: permessage-deflate (DISABLED for GD — adds 5-10ms latency)
  Heartbeat: ping interval = 20s, timeout = 60s
  Connection pooling: Redis Pub/Sub for cross-node message broadcast

TURN ENGINE PERFORMANCE RULES:
  - Redis MULTI/EXEC atomic operations for turn state transitions
  - Lua scripting for complex multi-key GD state updates (single roundtrip)
  - No blocking Redis commands in turn loop (no BLPOP — use Pub/Sub)
  - WebSocket command acknowledgment: client must ACK within 200ms
  - Missed ACK = log event + continue (no retry loop — determinism preserved)
  - Timer precision: server-side only (client timer display is decorative)

REDIS LUA SCRIPT — TURN TRANSITION (ATOMIC):
  KEYS: [session_key, queue_key, active_speaker_key, turn_log_key]
  Atomically: pop next speaker, update active speaker, push log entry
  Ensures: no two speakers active simultaneously — physics-level guarantee

BANDWIDTH OPTIMIZATION:
  - Audio: 40–60 kbps per user (Jitsi Opus codec, 20ms frame)
  - No video (disabled at Jitsi configuration level)
  - SFU routing: Jitsi Videobridge — server mediates all streams
  - Mobile tolerance: 150kbps minimum connection required (enforced at join)
  - Jitsi p2p: DISABLED (use SFU always — predictable latency)
```

### 6.2 Dojo Match Engine Performance

```
LIVEKIT PERFORMANCE CONFIGURATION:
  Room config:
    max_participants: 6 (dojo match cap)
    empty_timeout: 300 seconds
    max_participant_identity: 128 chars
    sync_streams: true
  Publisher config:
    dynacast: true       (adaptive bitrate per subscriber)
    simulcast: false     (dojo is 1:1 or small group — no simulcast needed)
  Token TTL: 120 seconds (short-lived tokens enforced — performance + security)

MATCH STATE MACHINE:
  Technology: Redis Streams (NOT Redis Pub/Sub for Dojo — persistence needed)
  Reason: Match state must survive Redis restart (Streams have persistence)
  Consumer group: dojo-match-engine-{pod_id}
  Delivery guarantee: at-least-once (idempotency enforced at state transition)
  Checkpoint: every 30 seconds to PostgreSQL (P99 write < 10ms target)

SCENARIO LOADING:
  Scenario templates: pre-loaded in Redis on Dojo service startup
  Cache key: scenario:{scenario_id} TTL = 24 hours
  Cold load: PostgreSQL read (< 50ms target)
  Hot load: Redis read (< 2ms target)
```

### 6.3 WebSocket Command Channel Performance

```
ARCHITECTURE: per-namespace WebSocket servers
  Namespace: /gd       → Voice GD Orchestrator (Node.js)
  Namespace: /dojo     → Dojo Match Engine (Node.js)
  Namespace: /interview → Interview Service (Node.js)
  Namespace: /notify   → Notification Service (Go or Node.js)

ENCODING: MessagePack (binary) for all command payloads
  Benchmark: JSON 340 bytes → MessagePack 180 bytes (47% reduction)
  Parse time: JSON 12μs → MessagePack 3μs per message

MESSAGE PRIORITY LANES (within single WebSocket connection):
  Priority 1: mute/unmute commands (GD turn control)
  Priority 2: timer events
  Priority 3: session status changes
  Priority 4: score updates
  Priority 5: notification messages

HORIZONTAL SCALING:
  Redis Pub/Sub for cross-pod message routing
  Sticky sessions: DISABLED (Redis handles state, not pod memory)
  Pod scaling trigger: > 500 concurrent WebSocket connections per pod
```

---

## SECTION 7 — KAFKA EVENT BUS PERFORMANCE (LOCKED)

### 7.1 Kafka Configuration

```properties
# Broker performance tuning
num.network.threads=8
num.io.threads=16
socket.send.buffer.bytes=1048576
socket.receive.buffer.bytes=1048576
socket.request.max.bytes=104857600
log.segment.bytes=1073741824
log.retention.hours=168
log.cleaner.enable=true
num.partitions=6
default.replication.factor=3
min.insync.replicas=2
unclean.leader.election.enable=false
auto.create.topics.enable=false
compression.type=lz4
```

### 7.2 Topic Configuration Matrix

| Topic | Partitions | Replication | Retention | Consumers |
|---|---|---|---|---|
| user.created | 3 | 3 | 7 days | Analytics, Notification |
| job.applied | 6 | 3 | 30 days | Analytics, Scoring |
| gd.completed | 6 | 3 | 90 days | Analytics, Scoring, ClickHouse |
| gd.turn.event | 12 | 3 | 7 days | Analytics, ClickHouse |
| match.scored | 6 | 3 | 90 days | Analytics, Belt Engine |
| belt.eligible | 3 | 3 | 30 days | Certification, Notification |
| invoice.generated | 3 | 3 | 7 years | Billing audit |
| royalty.credited | 3 | 3 | 15 years | Royalty audit, Notification |
| intelligence.updated | 6 | 3 | 30 days | Analytics, Profile |
| idea.submitted | 3 | 3 | 15 years | Innovation, Anti-theft |
| fraud.detected | 3 | 3 | 90 days | Admin, Legal |

### 7.3 Producer Performance Rules

```
acks = all                   (guaranteed delivery — no acks=1 allowed)
retries = 3
retry.backoff.ms = 100
batch.size = 65536           (64KB batching for throughput)
linger.ms = 5                (5ms batching window)
buffer.memory = 67108864     (64MB producer buffer)
compression.type = lz4
max.in.flight.requests.per.connection = 5
enable.idempotence = true    (exactly-once producer semantics)
```

### 7.4 Consumer Performance Rules

```
fetch.min.bytes = 1024
fetch.max.wait.ms = 500
max.poll.records = 500
max.poll.interval.ms = 300000
session.timeout.ms = 45000
heartbeat.interval.ms = 15000
auto.offset.reset = earliest
enable.auto.commit = false    (manual commit after processing)
isolation.level = read_committed
```

### 7.5 Consumer Lag Enforcement

```
Alert: consumer lag > 500 events on any critical topic
Incident: consumer lag > 5000 events on any critical topic
Auto-scale trigger: lag > 1000 events sustained > 2 minutes
  → Kubernetes HPA scales consumer Deployment (max 10 replicas)

Lag monitoring: Prometheus kafka_consumer_group_lag metric
Dashboard: SPOA-KAFKA-HEALTH in Grafana
```

---

## SECTION 8 — FRONTEND PERFORMANCE CONTRACTS (LOCKED)

### 8.1 Flutter App Performance Rules

```
RENDERING RULES (NON-NEGOTIABLE):
  - No setState calls that rebuild subtrees wider than necessary
  - const constructors: MANDATORY on all stateless widgets
  - ListView.builder: REQUIRED for all lists > 20 items
  - RepaintBoundary: wrap all animation widgets
  - Avoid Opacity widget — use AnimatedOpacity or FadeTransition
  - Image.network: ALWAYS specify cacheWidth + cacheHeight
  - Pagination threshold: 25 items per page (standard), 10 for complex cards

STATE MANAGEMENT (Riverpod — Locked):
  - AsyncNotifierProvider for all async data
  - No FutureBuilder inside Scaffold body (causes full rebuild on hot data)
  - Provider granularity: one provider per logical data domain
  - Avoid StateProvider for complex state (use Notifier)

NETWORK CALLS:
  - HTTP/2 multiplexing: ENABLED (Dart http package + dio)
  - Response caching: dio-cache-interceptor (stale-while-revalidate pattern)
  - Image caching: cached_network_image (disk + memory cache)
  - Max concurrent requests: 6 per host
  - Request timeout: 10s connect, 30s receive

PERFORMANCE METRICS (measured via flutter DevTools + Firebase Performance):
  Frame render time: < 16ms (60fps) — CRITICAL
  Jank threshold: > 32ms frame = jank event logged
  Cold start: < 2.5s on mid-range Android (Pixel 5 equivalent)
  Route transition: < 300ms
  List scroll: 60fps maintained with 1000+ items (virtualization enforced)
```

### 8.2 Next.js SSR/ISR Performance Rules (SEO Layer)

```javascript
// Required Next.js performance configuration

// next.config.js
module.exports = {
  compress: true,
  poweredByHeader: false,
  reactStrictMode: true,
  swcMinify: true,

  images: {
    formats: ['image/avif', 'image/webp'],
    minimumCacheTTL: 86400,
    deviceSizes: [640, 768, 1024, 1280, 1920],
  },

  experimental: {
    optimizeCss: true,
    scrollRestoration: true,
  },

  // Bundle analyzer: run on every build in CI
  // Block deploy if bundle size increases > 10% without approval
};

// ISR configuration for job pages
export async function getStaticProps({ params }) {
  return {
    props: { job: await fetchJob(params.id) },
    revalidate: 300,  // 5-minute ISR — jobs don't change every second
  };
}

// Dynamic routes: fallback = 'blocking' (NOT true — avoids layout flash)
export async function getStaticPaths() {
  return { paths: [], fallback: 'blocking' };
}
```

```
CORE WEB VITALS TARGETS (enforced in CI via Lighthouse CI):
  LCP (Largest Contentful Paint): < 2.5s
  FID (First Input Delay): < 100ms
  CLS (Cumulative Layout Shift): < 0.1
  TTFB (Time to First Byte): < 200ms (SSR requirement)
  FCP (First Contentful Paint): < 1.2s

Lighthouse CI gate: score < 85 on any metric → BLOCK DEPLOYMENT
```

---

## SECTION 9 — LOAD TESTING AND PERFORMANCE GATE (MANDATORY)

### 9.1 Load Test Gate Enforcement

```
RULE: No service may be promoted to production without passing
      its load test profile at >= 3× baseline throughput.
RULE: Load tests run automatically in staging on every release candidate.
RULE: Any SLO violation during load test = deployment BLOCKED.
RULE: Exemption requires Platform Architect sign-off + written justification.
```

### 9.2 Load Test Profiles (k6 — Required)

**Profile 1 — API Baseline**
```javascript
// k6 — API Gateway Load Test
export const options = {
  stages: [
    { duration: '2m', target: 500 },   // ramp-up
    { duration: '5m', target: 500 },   // sustained baseline
    { duration: '2m', target: 1500 },  // 3× spike
    { duration: '3m', target: 1500 },  // sustained spike
    { duration: '2m', target: 0 },     // ramp-down
  ],
  thresholds: {
    http_req_duration: ['p(95)<300', 'p(99)<500'],
    http_req_failed: ['rate<0.001'],   // < 0.1% error rate
    http_reqs: ['rate>500'],           // minimum throughput
  },
};
```

**Profile 2 — GD Session Concurrency**
```javascript
// k6 — Voice GD Concurrent Sessions
export const options = {
  scenarios: {
    gd_sessions: {
      executor: 'constant-vus',
      vus: 50,              // 50 concurrent GD sessions
      duration: '10m',
    },
    gd_spike: {
      executor: 'ramping-vus',
      startVUs: 50,
      stages: [{ duration: '3m', target: 150 }],  // 3× spike
      startTime: '10m',
    },
  },
  thresholds: {
    'ws_connecting': ['p(95)<400'],
    'ws_msgs_sent': ['rate>100'],
    'ws_session_duration': ['p(99)<600000'],  // 10 min session
  },
};
```

**Profile 3 — Database Stress**
```javascript
// k6 — Mixed read/write under load
export const options = {
  stages: [
    { duration: '3m', target: 200 },
    { duration: '10m', target: 200 },
    { duration: '2m', target: 0 },
  ],
  thresholds: {
    'http_req_duration{type:read}': ['p(95)<150'],
    'http_req_duration{type:write}': ['p(95)<300'],
    'http_req_failed': ['rate<0.001'],
  },
};
```

### 9.3 Airflow Load Test DAGs

```
DAG: spoa_load_test_pre_release
  Trigger: On release candidate tag in GitLab
  Tasks:
    provision_k6_runner →
    run_api_baseline_test →
    assert_api_slo_pass →
    run_gd_concurrency_test →
    assert_gd_slo_pass →
    run_database_stress_test →
    assert_db_slo_pass →
    generate_load_test_report →
    gate_deployment_approval  [BLOCKS CD if any assertion fails]

DAG: spoa_weekly_regression_test
  Schedule: 0 5 * * 0 (Sunday 5am UTC)
  Tasks: run all 3 profiles → compare against previous week →
         alert if P95 regressed > 10% → write to spoa_regression_log table

DAG: spoa_weekly_slow_query_report
  Schedule: 0 6 * * 1 (Monday 6am UTC)
  Tasks: query pg_stat_statements → rank by total_time →
         identify unused indexes → generate report → push to ops Slack channel
```

---

## SECTION 10 — OBSERVABILITY FOR PERFORMANCE (NON-OPTIONAL)

### 10.1 Required Prometheus Metrics

```
# API Layer
kong_http_requests_total{service, route, status}
kong_latency_bucket{service, type, le}       # histogram
nginx_http_requests_total
nginx_http_request_duration_seconds_bucket

# Database
pg_stat_statements_total_time{query, user, database}
pg_stat_statements_calls
pg_database_size_bytes
pgbouncer_pool_size{database, user}
pgbouncer_waiting_clients

# Redis
redis_commands_duration_seconds_bucket{cmd}
redis_keyspace_hits_total
redis_keyspace_misses_total
redis_memory_used_bytes
redis_connected_clients

# GD Performance
gd_turn_command_latency_ms_bucket{command_type, le}
gd_session_active_count
gd_websocket_connections_total
gd_turn_queue_depth{session_id}

# Dojo Performance
dojo_match_active_count
dojo_state_transition_latency_ms_bucket
dojo_livekit_join_latency_ms_bucket

# Kafka
kafka_consumer_lag{topic, group, partition}
kafka_producer_request_latency_ms_bucket
kafka_consumer_fetch_rate

# Frontend (emitted by Next.js + Flutter)
frontend_lcp_seconds_bucket
frontend_fid_milliseconds_bucket
frontend_cls_score_bucket
frontend_cold_start_seconds_bucket
```

### 10.2 Grafana Dashboards (Required)

```
SPOA-OVERVIEW
  - P50/P95/P99 API latency per service (last 1h, 6h, 24h)
  - Error rate by service
  - Active GD sessions
  - Active Dojo matches
  - Kafka consumer lag heatmap
  - Redis hit/miss ratio

SPOA-DATABASE
  - PostgreSQL connection pool utilization
  - Slow query count (> 200ms, > 500ms, > 2s bands)
  - Cache hit ratio by service
  - Index usage vs seq scan ratio
  - PgBouncer wait queue depth

SPOA-REALTIME
  - GD turn command latency (histogram)
  - WebSocket connection count by namespace
  - Jitsi active rooms
  - LiveKit participant count
  - coturn relay usage %

SPOA-KAFKA
  - Consumer lag per topic (threshold lines at 500, 5000)
  - Producer throughput (events/sec per topic)
  - Broker disk usage

SPOA-FRONTEND
  - Core Web Vitals (LCP, FID, CLS) by page
  - Flutter cold start percentiles
  - Route transition latency
  - API call duration from client perspective
```

### 10.3 OpenTelemetry Trace Requirements

```
All microservices must instrument:
  - Span: every HTTP handler (auto-instrumented via SDK)
  - Span: every database query (pg-opentelemetry or JDBC plugin)
  - Span: every Redis command (redis-opentelemetry)
  - Span: every Kafka produce/consume
  - Span: every external service call
  - Custom span: GD turn cycle start → command sent → ACK received
  - Custom span: Dojo state transition

Trace sampling rate:
  Production: 5% base, 100% for error traces, 100% for P99 outliers
  Staging: 100%

Trace storage: Grafana Tempo (self-hosted in ops namespace)
Retention: 7 days hot, 30 days cold (MinIO)
```

---

## SECTION 11 — SCALING POLICY (LOCKED)

### 11.1 Kubernetes HPA Rules

```yaml
# Horizontal Pod Autoscaler — Voice GD Orchestrator
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: gd-orchestrator-hpa
  namespace: realtime
spec:
  minReplicas: 3
  maxReplicas: 20
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 60
    - type: External
      external:
        metric:
          name: gd_websocket_connections_total
        target:
          type: AverageValue
          averageValue: 500   # scale when > 500 connections per pod
  behavior:
    scaleUp:
      stabilizationWindowSeconds: 30
      policies:
        - type: Pods
          value: 2
          periodSeconds: 60
    scaleDown:
      stabilizationWindowSeconds: 300   # slow scale-down — preserve sessions
```

### 11.2 Service-Level Scaling Triggers

| Service | Scale-Up Trigger | Min Replicas | Max Replicas |
|---|---|---|---|
| Auth Service | CPU > 60% OR RPS > 300/pod | 3 | 15 |
| Job Service | CPU > 60% | 2 | 10 |
| GD Orchestrator | WS connections > 500/pod | 3 | 20 |
| Dojo Match Engine | Active matches > 100/pod | 2 | 15 |
| Scoring Engine | Queue depth > 200 | 2 | 8 |
| Notification Service | Kafka lag > 500 | 2 | 10 |
| Analytics Service | CPU > 70% | 1 | 5 |
| Billing Service | CPU > 60% | 2 | 6 |

### 11.3 Resource Request and Limit Policy

```yaml
# Resource requests and limits — ALL services must declare these
# No service may run without resource declarations

resources:
  requests:           # Guaranteed resources (scheduler uses this)
    cpu: "250m"
    memory: "256Mi"
  limits:             # Hard ceiling (OOM kill above this)
    cpu: "1000m"
    memory: "1Gi"

# GD Orchestrator (higher — latency critical)
resources:
  requests:
    cpu: "500m"
    memory: "512Mi"
  limits:
    cpu: "2000m"
    memory: "2Gi"

# ClickHouse (analytical — high memory)
resources:
  requests:
    cpu: "2000m"
    memory: "8Gi"
  limits:
    cpu: "8000m"
    memory: "32Gi"
```

---

## SECTION 12 — PERFORMANCE FAILURE MODES AND HARD STOPS

### 12.1 SLO Breach Response Protocol

```
SEVERITY CLASSIFICATION:
  P1 — SLO breach affecting GD sessions, auth, or billing in production
       Response: Immediate page (PagerDuty), 15-minute resolution target
       Escalation: CTO-level notification at 30 minutes

  P2 — SLO breach affecting job search, dojo, or scoring
       Response: On-call alert, 1-hour resolution target

  P3 — SLO degradation (approaching threshold, not breached)
       Response: Slack ops notification, 4-hour resolution target

  P4 — Performance regression detected in weekly test
       Response: Ticket created, 1 sprint resolution target
```

### 12.2 SPOA Hard Stop Conditions

Any of the following blocks deployment pipeline:

```
HS-01: Load test SLO failure for any P1 endpoint
HS-02: GD turn command P95 latency > 200ms in staging
HS-03: API error rate > 0.5% sustained for > 5 minutes
HS-04: Redis cache hit ratio < 70% on job search path
HS-05: PostgreSQL connection pool exhaustion (> 95% utilization)
HS-06: Kafka consumer lag > 5000 on gd.completed or royalty.credited topics
HS-07: Memory leak detected (pod memory growing > 20% per hour)
HS-08: Lighthouse CI score < 85 on any Core Web Vital
HS-09: Flutter cold start regression > 20% vs. previous release baseline
HS-10: N+1 query pattern detected in OpenTelemetry traces for any release

On HARD STOP:
  → CI/CD pipeline: BLOCKED
  → Deployment gate: CLOSED
  → Incident auto-created in Admin Governance dashboard
  → On-call engineer paged
  → Manual sign-off required to override (Platform Architect only)
```

---

## SECTION 13 — IMPLEMENTATION CHECKLIST (LOCKED)

```
[ ] PostgreSQL performance config applied and verified under load
[ ] PgBouncer deployed with correct pool sizes per service
[ ] All mandatory indexes created via Flyway migrations
[ ] pg_stat_statements enabled and slow query logging active
[ ] Redis performance config applied (maxmemory, eviction policies, DB separation)
[ ] All cache domains populated with correct TTLs and invalidation triggers
[ ] Redis Lua scripts deployed for GD atomic operations
[ ] Kong proxy-cache plugin configured for read-only routes
[ ] Kong rate-limiting plugin configured (Redis backend)
[ ] Envoy circuit breakers configured for all upstream services
[ ] NGINX performance config applied (worker threads, keepalive, gzip)
[ ] Kafka topics created with correct partition and retention config
[ ] Kafka producer idempotence enabled
[ ] Kafka consumer lag alerts configured in Prometheus
[ ] ClickHouse materialized views created for all dashboard queries
[ ] ClickHouse async inserts enabled
[ ] OpenSearch index mappings set to dynamic: strict (no dynamic mapping)
[ ] WebSocket server using uWebSockets.js with MessagePack encoding
[ ] GD sticky sessions disabled (Redis Pub/Sub for cross-pod routing)
[ ] LiveKit dynacast enabled, simulcast disabled
[ ] HPA configured for all services in scaling policy table
[ ] Resource requests and limits declared on all Deployments
[ ] All Prometheus metrics emitting (validated via /metrics endpoint)
[ ] Grafana dashboards imported (SPOA-OVERVIEW, SPOA-DATABASE, SPOA-REALTIME, SPOA-KAFKA, SPOA-FRONTEND)
[ ] OpenTelemetry SDK instrumented in all services
[ ] Grafana Tempo deployed in ops namespace
[ ] k6 load test scripts written for all 3 profiles
[ ] Airflow load test DAG deployed and validated in staging
[ ] Lighthouse CI configured in GitLab CI pipeline
[ ] Flutter performance profiling baseline captured and stored
[ ] All SPOA hard stop conditions wired into CI/CD pipeline gates
[ ] SPOA operational declaration signed by: Platform Architect + DevOps Lead
```

---

## SECTION 14 — AGENT VERSION CONTROL

```
AGENT ID           : SPOA-002
VERSION            : 1.0.0
SEALED BY          : Platform Architecture Authority
SEAL DATE          : [TO BE FILLED ON EXECUTION]
NEXT REVIEW        : 6 months post-seal or after any major load test failure
MUTATION POLICY    : All changes require version bump + re-seal
BREAKING CHANGE    : Requires Platform Architect approval + load test sign-off

DEPENDENCY        : BRA-001 must be operational before SPOA-002 can be declared active
                    Performance data is covered by BRA-001 backup policy (ClickHouse T2)

VERSION HISTORY:
  v1.0.0 — Initial sealed specification
             Covers: API SLO contracts, caching strategy (Redis + Kong + NGINX),
                     PostgreSQL/ClickHouse/OpenSearch performance, PgBouncer,
                     Envoy circuit breakers, Kafka throughput tuning,
                     GD turn engine latency enforcement, Dojo LiveKit config,
                     WebSocket MessagePack encoding, Flutter rendering rules,
                     Next.js Core Web Vitals enforcement, k6 load test profiles,
                     HPA scaling policy, Airflow performance DAGs,
                     OpenTelemetry tracing, Grafana dashboards,
                     10 hard stop conditions, full implementation checklist
```

---

```
END OF SPECIFICATION

SYSTEM_PERFORMANCE_OPTIMIZATION_AGENT v1.0.0
STATUS: SEALED · LOCKED · GOVERNED
ECOSKILLER PLATFORM — ANTIGRAVITY SUBSYSTEM
ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE

"Performance is a contract. This agent enforces it."
```
