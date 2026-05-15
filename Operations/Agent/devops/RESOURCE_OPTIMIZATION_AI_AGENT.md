# RESOURCE_OPTIMIZATION_AI_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### ECOSKILLER PLATFORM — ANTIGRAVITY EXECUTION ENGINE

---

```
PROMPT_CLASS                  = AGENT_GENERATION_LOCK
AGENT_ID                      = RESOURCE_OPTIMIZATION_AI_AGENT
EXECUTION_ENGINE              = ANTIGRAVITY
DOMAIN                        = ENTERPRISE_OPTIMIZATION + TRUST_INFRASTRUCTURE
STATUS                        = SEALED · LOCKED · GOVERNED · DETERMINISTIC
MUTATION_POLICY               = ADD_ONLY VIA VERSION BUMP
INTERPRETATION_AUTHORITY      = NONE
CREATIVE_DEVIATION            = FORBIDDEN
ASSUMPTION_FILLING            = FORBIDDEN
IMPLICIT_BEHAVIOR             = FORBIDDEN
DEFAULT_BEHAVIOR              = DENY
FAILURE_MODE                  = STOP_EXECUTION → REPORT → NO_PARTIAL_OUTPUT
VERSION                       = 1.0.0
LAST_SEALED                   = March 2026
```

---

## SECTION 0 — INHERITANCE DECLARATION (MANDATORY — DO NOT SKIP)

This agent **INHERITS** the following locked contracts from the Ecoskiller Master Constitution. It must not redefine, contradict, shadow, or bypass any inherited rule.

| Inherited Contract | Source Document |
|---|---|
| Multi-tenant isolation (tenant_id RLS at DB layer) | Ecoskiller Master Execution Prompt v12.0 |
| RBAC + ABAC permission model | Ecoskiller Auth Layer |
| Event-driven microservice architecture | Ecoskiller Infrastructure v8 |
| Kafka event bus topology and topic registry | Ecoskiller COMPLETE LIST OF TECHS |
| PostgreSQL + Redis + ClickHouse data stack | Ecoskiller Infrastructure v8 |
| Kong API Gateway enforcement | Ecoskiller Services |
| Keycloak identity + JWT session model | Ecoskiller Auth Layer |
| HashiCorp Vault secret management | Ecoskiller v8 Infrastructure Audit |
| Immutable audit log schema | Ecoskiller Governance Layer |
| k3s Kubernetes namespace layout: core, realtime, media, billing, analytics, admin, ops | Ecoskiller Services |
| Prometheus + Loki + Grafana + OpenTelemetry observability stack | Ecoskiller Infrastructure v8 |
| Wazuh SIEM + intrusion detection | Ecoskiller COMPLETE LIST OF TECHS |
| Velero backup + restore | Ecoskiller COMPLETE LIST OF TECHS |
| R25 — Cost-aware minimal viable infrastructure sizing | Ecoskiller Master Prompt R25 |
| Forgejo Actions CI/CD (replaces GitHub Actions) | Ecoskiller v8 Infrastructure Audit |
| Longhorn distributed storage | Ecoskiller Society Architecture |
| Open Policy Agent — centralized policy enforcement | Ecoskiller COMPLETE LIST OF TECHS |
| Antigravity execution rules and UI/Agent lock | Ecoskiller UI Lock |

**Violation of any inherited contract = STOP EXECUTION → REPORT INHERITANCE VIOLATION**

---

## SECTION 1 — AGENT IDENTITY (NON-NEGOTIABLE)

```
AGENT_NAME              = RESOURCE_OPTIMIZATION_AI_AGENT
AGENT_CLASS             = ENTERPRISE_OPTIMIZATION_AGENT
LAYER                   = ENTERPRISE_OPTIMIZATION + TRUST_INFRASTRUCTURE
EXECUTION_TYPE          = RULE_ENGINE + SIGNAL_CONSUMER + ADVISORY_EMITTER
DECISION_AUTHORITY      = ADVISORY_ONLY
AUTONOMOUS_ACTION       = FORBIDDEN (except pre-approved safe actions in Section 9)
HUMAN_OVERRIDE          = ALWAYS_PERMITTED — NEVER_BLOCKED
AI_APPROVAL_POWER       = ZERO
AI_BLOCK_POWER          = ZERO
AI_RESOURCE_DELETION    = FORBIDDEN
AI_SCALING_WITHOUT_GATE = FORBIDDEN
```

### Agent Purpose Statement

The RESOURCE_OPTIMIZATION_AI_AGENT exists to:

1. Continuously observe Kubernetes resource consumption, Kafka throughput, Redis memory pressure, PostgreSQL query load, ClickHouse ingestion rate, media server (Jitsi/LiveKit) session load, and MinIO storage growth across all namespaces in the Ecoskiller k3s cluster.
2. Produce deterministic, rule-gated optimization recommendations for compute scaling, pod replica adjustment, resource limit tuning, storage tiering, media capacity planning, and cost-aware infrastructure right-sizing.
3. Feed trust signals to the Trust Infrastructure layer when resource anomalies indicate abuse, DDoS patterns, tenant over-consumption, or billing fraud.
4. Execute **only the pre-approved safe autonomous actions** defined in Section 9. All other recommendations require human operator approval through the Admin Ops Console.
5. Never delete resources. Never terminate pods. Never modify billing records. Never alter tenant data.

---

## SECTION 2 — PLATFORM RESOURCE MAP (READ-ONLY OBSERVATION SCOPE)

The agent observes the following resource domains. It has **no write authority** outside its own output schema.

### 2.1 Kubernetes Namespaces (k3s — Observation Targets)

| Namespace | Key Services | Resource Signal Type |
|---|---|---|
| `core` | Auth, User, Job, Application, Recruiter | CPU / memory / pod count / DB connection pool |
| `realtime` | Voice GD Orchestrator, Dojo Match Engine, Interview Service | WebSocket connection count / Redis state machine depth / pod CPU burst |
| `media` | Jitsi, LiveKit, coturn | Active session count / bandwidth throughput / SFU participant load |
| `billing` | Billing & Subscription Service, Royalty Accounting Engine | CPU / memory / Kafka consumer lag / DB write rate |
| `analytics` | Analytics Service, ClickHouse, Qdrant, Feature Store | ClickHouse insert rate / query latency / disk growth / vector index size |
| `admin` | Admin Governance Service, Moderation, Audit | Pod CPU / memory / queue depth |
| `ops` | Prometheus, Loki, Grafana, Grafana Tempo, Wazuh, Velero | Self-consumption metrics / disk usage / scrape interval health |
| `society` | Society domain services, Workshop Engine, Franchise Engine | Kafka partition lag / offline sync queue depth |

### 2.2 Data Infrastructure (Observation Targets)

| Component | Observed Metrics |
|---|---|
| PostgreSQL 15 | Active connections, query latency (p50/p95/p99), lock waits, replication lag, table bloat |
| Redis 7 | Memory used/max, eviction rate, GD state machine key count, OTP TTL compliance, lock contention |
| ClickHouse | Insert throughput (rows/sec), merge backlog, query latency, disk usage by table, partition count |
| Apache Kafka | Consumer group lag per topic, partition under-replicated, produce rate, broker disk usage |
| MinIO | Bucket sizes per type (resumes, certificates, invoices, audit), request rate, storage growth velocity |
| Qdrant | Vector index size, query latency, RAM consumption, collection count |
| OpenSearch | Index size, shard count, search latency, heap usage |
| Longhorn | Volume utilization, replica health, replica rebuild rate |

### 2.3 Media & Realtime Stack (Observation Targets)

| Component | Observed Metrics |
|---|---|
| Jitsi (Voice GD) | Active rooms count, participants per room, SFU CPU load, coturn relay usage % |
| LiveKit (Dojo + Interviews) | Active sessions, track count, SFU bandwidth Mbps, server-side recording flag |
| coturn | Active TURN relay sessions, relay bandwidth, port exhaustion risk |
| WebSocket connections | Open connection count per namespace, message rate per second |

---

## SECTION 3 — SYSTEM SHAPE (ARCHITECTURE RULES)

```
ARCHITECTURE_STYLE      = EVENT_DRIVEN_MICROSERVICE (inherited)
SERVICE_ISOLATION       = STRICT — agent owns only its output schema
WRITE_AUTHORITY         = resource_optimization schema + Kafka produced topics ONLY
READ_AUTHORITY          = Prometheus metrics API + approved PostgreSQL views + Kafka consumed topics
KUBERNETES_API_ACCESS   = READ + pre-approved safe patch operations only (Section 9)
MEDIA_TOUCH             = FORBIDDEN
DIRECT_DB_WRITE_OTHER   = FORBIDDEN
BILLING_RECORD_TOUCH    = FORBIDDEN
TENANT_DATA_TOUCH       = FORBIDDEN
```

### 3.1 Agent Data Flow

```
[Prometheus Metrics API]          [Kafka Event Bus]         [PostgreSQL Read Views]
         ↓                               ↓                          ↓
         └──────────────────────────────┴──────────────────────────┘
                                         ↓
                            [Signal Ingestion Layer]
                                         ↓
                         [Resource State Aggregator]
                     (per namespace · per service · per tenant)
                                         ↓
                        [Optimization Rule Engine]
                     (deterministic threshold evaluation)
                                         ↓
                   ┌─────────────────────┴──────────────────────┐
                   ↓                                            ↓
        [Recommendation Writer]                  [Anomaly / Trust Signal Writer]
        (resource_optimization schema)            (resource_anomaly table)
                   ↓                                            ↓
        [Kafka: resource.recommendation.*]       [Kafka: resource.anomaly.detected]
                   ↓                                            ↓
       [Admin Ops Console Consumer]              [Trust Infrastructure Consumers]
       [HPA Patch Executor — safe actions]       [Admin Governance Service]
       [Notification Service]                    [Wazuh SIEM]
```

---

## SECTION 4 — KUBERNETES NAMESPACE + DEPLOYMENT

```
NAMESPACE               = analytics
DEPLOYMENT_TYPE         = Kubernetes Deployment (k3s)
REPLICA_POLICY          = Minimum 2 replicas (HA)
SIDECAR                 = OpenTelemetry collector (traces to Grafana Tempo)
RESOURCE_CLASS          = CPU-moderate, memory-stable (metrics aggregation workload)
SCRAPE_INTERVAL         = 30 seconds (Prometheus pull) + 60 seconds (K8s API poll)
```

### 4.1 Mandatory Kubernetes Manifest Requirements

The agent deployment MUST define all of the following. Absence of any = STOP EXECUTION:

- `Deployment` with explicit CPU/memory requests and limits
- `ConfigMap` for all threshold configuration (no hardcoded thresholds)
- `Secret` references via HashiCorp Vault K8s Auth Method (no env-hardcoded credentials)
- `HorizontalPodAutoscaler` (HPA) triggered on CPU > 70%
- `NetworkPolicy`:
  - Allow ingress from `ops` namespace (Prometheus scrape on metrics port)
  - Allow egress to Kafka broker, PostgreSQL, Redis, Kubernetes API server
  - Deny all other ingress
- `ServiceAccount` with RBAC rules scoped to read-only + pre-approved patch targets
- `Liveness` and `Readiness` probes on `/health` endpoint
- `PodDisruptionBudget` — minimum 1 pod available during node drain

---

## SECTION 5 — TECH STACK (LOCKED — NO ADDITIONS)

```
RUNTIME                 = Python 3.11
FRAMEWORK               = FastAPI (async)
METRICS_SOURCE          = Prometheus HTTP API (existing ops namespace)
KUBERNETES_CLIENT       = kubernetes-python (official client)
EVENT_BUS               = Apache Kafka (existing cluster)
STATE_STORE             = Redis 7 (existing cluster — read-only for external state)
PRIMARY_DATABASE        = PostgreSQL 15 (existing cluster — dedicated schema)
ANALYTICS_DATABASE      = ClickHouse (existing cluster — read-only external + write own tables)
TASK_SCHEDULER          = Apache Airflow (existing cluster — DAGs for batch optimization cycles)
OBSERVABILITY           = Prometheus metrics + Loki logs + OpenTelemetry traces
                          (all existing Ecoskiller ops namespace stack)
SECRETS                 = HashiCorp Vault (existing cluster — K8s Auth Method)
CONTAINER               = Docker (existing registry on Forgejo)
ORCHESTRATION           = k3s Kubernetes (existing cluster)
POLICY_ENGINE           = Open Policy Agent (existing — for safe-action gate)
```

**Zero new infrastructure services introduced.**
**Zero paid services introduced.**
**All tools listed above already exist in the Ecoskiller k3s cluster.**

---

## SECTION 6 — DATA MODEL (LOCKED SCHEMA)

### 6.1 PostgreSQL Schema: `resource_optimization`

```sql
-- Optimization Recommendation Registry
CREATE TABLE resource_optimization.recommendation (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    recommendation_id       TEXT UNIQUE NOT NULL,
    tenant_id               UUID,                        -- NULL = platform-wide recommendation
    namespace               TEXT NOT NULL,               -- k8s namespace target
    service_name            TEXT NOT NULL,
    resource_type           TEXT NOT NULL,               -- POD_REPLICA | CPU_LIMIT | MEMORY_LIMIT |
                                                         -- KAFKA_PARTITION | REDIS_EVICTION |
                                                         -- PG_CONNECTION_POOL | STORAGE_TIER |
                                                         -- MEDIA_CAPACITY | HPA_THRESHOLD
    current_value           TEXT NOT NULL,
    recommended_value       TEXT NOT NULL,
    optimization_type       TEXT NOT NULL,               -- SCALE_UP | SCALE_DOWN | RIGHT_SIZE |
                                                         -- TIER_CHANGE | ALERT_ONLY
    priority                TEXT NOT NULL,               -- CRITICAL | HIGH | MEDIUM | LOW
    confidence_score        NUMERIC(5,4) NOT NULL,
    estimated_impact        TEXT NOT NULL,               -- human-readable impact description
    safe_action_eligible    BOOLEAN NOT NULL DEFAULT FALSE,
    status                  TEXT NOT NULL DEFAULT 'PENDING', -- PENDING | APPROVED | APPLIED |
                                                              -- REJECTED | EXPIRED | AUTO_APPLIED
    generated_at            TIMESTAMPTZ NOT NULL DEFAULT now(),
    expires_at              TIMESTAMPTZ NOT NULL,
    reviewed_by             UUID,                        -- operator user_id if manually reviewed
    reviewed_at             TIMESTAMPTZ,
    applied_at              TIMESTAMPTZ,
    rollback_available      BOOLEAN DEFAULT FALSE,
    rollback_applied        BOOLEAN DEFAULT FALSE,
    audit_hash              TEXT NOT NULL
);

-- Resource State Snapshot (point-in-time observation)
CREATE TABLE resource_optimization.resource_snapshot (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    snapshot_id             TEXT NOT NULL,
    namespace               TEXT NOT NULL,
    service_name            TEXT NOT NULL,
    metric_name             TEXT NOT NULL,
    metric_value            NUMERIC(18,6) NOT NULL,
    unit                    TEXT NOT NULL,
    observed_at             TIMESTAMPTZ NOT NULL DEFAULT now(),
    source                  TEXT NOT NULL               -- PROMETHEUS | K8S_API | KAFKA_METRICS | PG_STATS
);

-- Resource Anomaly (Trust Infrastructure signal)
CREATE TABLE resource_optimization.resource_anomaly (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    anomaly_id              TEXT UNIQUE NOT NULL,
    tenant_id               UUID,
    namespace               TEXT NOT NULL,
    service_name            TEXT NOT NULL,
    anomaly_type            TEXT NOT NULL,              -- BURST_ABUSE | DDOS_SIGNAL | TENANT_OVERCONSUMPTION |
                                                        -- STORAGE_BOMB | KAFKA_FLOOD | MEDIA_SESSION_ABUSE |
                                                        -- REDIS_EXHAUSTION | DB_CONNECTION_FLOOD
    severity                TEXT NOT NULL,              -- LOW | MEDIUM | HIGH | CRITICAL
    observed_value          NUMERIC(18,6) NOT NULL,
    threshold_value         NUMERIC(18,6) NOT NULL,
    trust_impact_score      NUMERIC(5,4) NOT NULL,
    detected_at             TIMESTAMPTZ NOT NULL DEFAULT now(),
    resolved_at             TIMESTAMPTZ,
    resolution_note         TEXT,
    escalated_to_admin      BOOLEAN DEFAULT FALSE,
    auto_throttle_applied   BOOLEAN DEFAULT FALSE,
    audit_hash              TEXT NOT NULL
);

-- Safe Action Execution Log (immutable)
CREATE TABLE resource_optimization.safe_action_log (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    action_id               TEXT UNIQUE NOT NULL,
    recommendation_id       TEXT NOT NULL REFERENCES resource_optimization.recommendation(recommendation_id),
    namespace               TEXT NOT NULL,
    service_name            TEXT NOT NULL,
    action_type             TEXT NOT NULL,              -- HPA_PATCH | RESOURCE_LIMIT_PATCH
    previous_state          JSONB NOT NULL,
    applied_state           JSONB NOT NULL,
    opa_policy_result       TEXT NOT NULL,              -- ALLOW | DENY
    applied_at              TIMESTAMPTZ NOT NULL DEFAULT now(),
    applied_by              TEXT NOT NULL DEFAULT 'RESOURCE_OPTIMIZATION_AI_AGENT',
    rollback_state          JSONB,
    rollback_applied        BOOLEAN DEFAULT FALSE,
    audit_hash              TEXT NOT NULL
);
```

### 6.2 Row-Level Security (RLS) — MANDATORY

```sql
ALTER TABLE resource_optimization.recommendation       ENABLE ROW LEVEL SECURITY;
ALTER TABLE resource_optimization.resource_snapshot    ENABLE ROW LEVEL SECURITY;
ALTER TABLE resource_optimization.resource_anomaly     ENABLE ROW LEVEL SECURITY;
ALTER TABLE resource_optimization.safe_action_log      ENABLE ROW LEVEL SECURITY;

-- Tenant-scoped records respect tenant isolation
-- NULL tenant_id records = platform-wide, visible to PLATFORM_ADMIN only
CREATE POLICY tenant_or_platform ON resource_optimization.recommendation
    USING (
        tenant_id IS NULL
        OR tenant_id = current_setting('app.tenant_id')::UUID
    );
```

**Cross-tenant resource data exposure = HARD SECURITY FAILURE**

---

## SECTION 7 — KAFKA EVENT CONTRACTS (LOCKED)

### 7.1 Consumed Topics (Read-Only Signal Sources)

| Kafka Topic | Signal Purpose |
|---|---|
| `gd.completed` | GD session end → release Jitsi room resource signal |
| `match.scored` | Dojo match end → release LiveKit arena resource signal |
| `interview.completed` | Interview end → release LiveKit slot resource signal |
| `invoice.generated` | Billing spike → billing namespace load signal |
| `user.created` | New user baseline → core namespace growth signal |
| `job.applied` | Application burst → core + analytics namespace signal |
| `workshop.session.completed` | Society domain → offline sync queue drain signal |
| `forecast.demand.gd_slot` | GD demand forecast → proactive media capacity signal |
| `forecast.demand.dojo_slot` | Dojo demand forecast → proactive LiveKit capacity signal |
| `forecast.demand.skill` | Skill demand forecast → analytics namespace pre-scale signal |

### 7.2 Produced Topics (Write-Only — Agent Outputs)

| Kafka Topic | Content | Primary Consumers |
|---|---|---|
| `resource.recommendation.scale` | Pod replica / HPA adjustment recommendations | Admin Ops Console, HPA Executor |
| `resource.recommendation.rightsize` | CPU/memory limit tuning recommendations | Admin Ops Console |
| `resource.recommendation.storage` | Storage tier change / cleanup recommendations | Admin Ops Console |
| `resource.recommendation.media` | Jitsi/LiveKit/coturn capacity recommendations | Admin Ops Console, Media Ops |
| `resource.anomaly.detected` | Anomaly trust signals | Admin Governance, Wazuh SIEM, Billing |
| `resource.safe_action.applied` | Confirmation of auto-applied safe actions | Admin Ops Console, Audit Log |
| `resource.safe_action.rolled_back` | Rollback confirmation | Admin Ops Console |

### 7.3 Event Schema Contract (AsyncAPI 2.6.0)

```yaml
asyncapi: 2.6.0
info:
  title: RESOURCE_OPTIMIZATION_AI_AGENT Events
  version: 1.0.0

channels:

  resource.recommendation.scale:
    publish:
      message:
        payload:
          type: object
          required: [recommendation_id, namespace, service_name, resource_type,
                     current_value, recommended_value, optimization_type,
                     priority, confidence_score, safe_action_eligible, generated_at]
          properties:
            recommendation_id:      {type: string}
            tenant_id:              {type: string, format: uuid, nullable: true}
            namespace:              {type: string}
            service_name:           {type: string}
            resource_type:          {type: string}
            current_value:          {type: string}
            recommended_value:      {type: string}
            optimization_type:      {type: string, enum: [SCALE_UP, SCALE_DOWN, RIGHT_SIZE, ALERT_ONLY]}
            priority:               {type: string, enum: [CRITICAL, HIGH, MEDIUM, LOW]}
            confidence_score:       {type: number, minimum: 0, maximum: 1}
            estimated_impact:       {type: string}
            safe_action_eligible:   {type: boolean}
            expires_at:             {type: string, format: date-time}
            generated_at:           {type: string, format: date-time}

  resource.anomaly.detected:
    publish:
      message:
        payload:
          type: object
          required: [anomaly_id, namespace, service_name, anomaly_type,
                     severity, observed_value, threshold_value, trust_impact_score, detected_at]
          properties:
            anomaly_id:             {type: string}
            tenant_id:              {type: string, format: uuid, nullable: true}
            namespace:              {type: string}
            service_name:           {type: string}
            anomaly_type:           {type: string}
            severity:               {type: string, enum: [LOW, MEDIUM, HIGH, CRITICAL]}
            observed_value:         {type: number}
            threshold_value:        {type: number}
            trust_impact_score:     {type: number, minimum: 0, maximum: 1}
            detected_at:            {type: string, format: date-time}
```

**Schema deviations from above = STOP EXECUTION**

---

## SECTION 8 — OPTIMIZATION RULE ENGINE (DETERMINISTIC THRESHOLDS)

All rules use **fixed thresholds defined in ConfigMap**. No probabilistic or fuzzy logic. No AI inference on thresholds. Identical input = identical rule output.

### 8.1 Kubernetes Pod Replica Rules

```
RULE_K8S_001 — REPLICA_SCALE_UP:
  Condition:  CPU utilization (5-min avg) > 75% AND pod_count < max_replicas
  Namespace:  any
  Priority:   HIGH if > 75% | CRITICAL if > 90%
  Action:     Emit resource.recommendation.scale (SCALE_UP)
  Safe:       YES — eligible for auto-apply via Section 9

RULE_K8S_002 — REPLICA_SCALE_DOWN:
  Condition:  CPU utilization (30-min avg) < 20% AND pod_count > min_replicas
              AND time_of_day NOT IN peak_window (07:00–22:00 IST)
  Namespace:  any (except media — separate rule)
  Priority:   LOW
  Action:     Emit resource.recommendation.scale (SCALE_DOWN)
  Safe:       YES — eligible for auto-apply via Section 9

RULE_K8S_003 — MEMORY_RIGHT_SIZE:
  Condition:  Memory RSS (7-day avg) < 40% of memory limit
              AND no OOM events in last 7 days
  Priority:   MEDIUM
  Action:     Emit resource.recommendation.rightsize (RIGHT_SIZE)
  Safe:       NO — requires human approval

RULE_K8S_004 — CPU_LIMIT_INCREASE:
  Condition:  CPU throttle rate > 30% (5-min window)
  Priority:   HIGH
  Action:     Emit resource.recommendation.rightsize (SCALE_UP)
  Safe:       NO — requires human approval
```

### 8.2 Media Namespace Rules (Jitsi + LiveKit + coturn)

```
RULE_MEDIA_001 — JITSI_CAPACITY_WARNING:
  Condition:  Active GD rooms > 80% of configured max_rooms
  Priority:   HIGH
  Action:     Emit resource.recommendation.media (SCALE_UP)
              Include: estimated rooms needed from forecast.demand.gd_slot
  Safe:       NO — requires human approval (media infra changes need operator review)

RULE_MEDIA_002 — LIVEKIT_SFU_LOAD:
  Condition:  LiveKit SFU CPU > 70% OR active track count > 80% capacity
  Priority:   HIGH
  Action:     Emit resource.recommendation.media (SCALE_UP)
  Safe:       NO

RULE_MEDIA_003 — COTURN_PORT_EXHAUSTION:
  Condition:  TURN relay port utilization > 85%
  Priority:   CRITICAL
  Action:     Emit resource.recommendation.media (SCALE_UP) + ALERT
  Safe:       NO

RULE_MEDIA_004 — MEDIA_IDLE_ROOMS:
  Condition:  Jitsi rooms with 0 active participants > 0 AND room_age > 10 minutes
  Priority:   MEDIUM
  Action:     Emit resource.recommendation.media (room cleanup advisory)
              NOTE: recommendation only — GD Orchestrator owns room lifecycle
  Safe:       NO — advisory only, GD Orchestrator must execute
```

### 8.3 Redis Rules

```
RULE_REDIS_001 — MEMORY_PRESSURE:
  Condition:  Redis memory_used > 80% of maxmemory
  Priority:   HIGH if > 80% | CRITICAL if > 92%
  Action:     Emit resource.recommendation.rightsize (SCALE_UP or EVICTION_POLICY_REVIEW)
  Safe:       NO

RULE_REDIS_002 — EVICTION_RATE_ANOMALY:
  Condition:  evicted_keys rate > 100 keys/min for 5+ consecutive minutes
              AND no scheduled GD batch within next 30 minutes
  Priority:   HIGH
  Anomaly:    REDIS_EXHAUSTION — emit resource.anomaly.detected
  Safe:       NO

RULE_REDIS_003 — GD_STATE_MACHINE_DEPTH:
  Condition:  GD state machine keys > expected_active_sessions × 1.5
              (excessive state accumulation — possible session leak)
  Priority:   HIGH
  Anomaly:    Emit resource.anomaly.detected (anomaly_type = REDIS_EXHAUSTION)
  Safe:       NO
```

### 8.4 PostgreSQL Rules

```
RULE_PG_001 — CONNECTION_POOL_SATURATION:
  Condition:  Active connections > 85% of max_connections
  Priority:   HIGH if > 85% | CRITICAL if > 95%
  Action:     Emit resource.recommendation.scale (connection pool increase advisory)
              Include: top-5 connection consumers by service
  Safe:       NO

RULE_PG_002 — SLOW_QUERY_ALERT:
  Condition:  p95 query latency > 500ms for any namespace's query class
              over 10-minute window
  Priority:   HIGH
  Action:     Emit resource.recommendation.rightsize (index or query advisory)
  Safe:       NO — advisory only, DBA/developer must act

RULE_PG_003 — REPLICATION_LAG:
  Condition:  Streaming replication lag > 30 seconds
  Priority:   CRITICAL
  Action:     Emit resource.recommendation.scale (ALERT_ONLY)
              Escalate immediately to Admin Ops
  Safe:       NO
```

### 8.5 Kafka Rules

```
RULE_KAFKA_001 — CONSUMER_LAG_WARNING:
  Condition:  Consumer group lag > 10,000 messages on any topic
              sustained for > 5 minutes
  Priority:   HIGH if > 10k | CRITICAL if > 100k
  Action:     Emit resource.recommendation.scale (consumer replica increase)
  Safe:       YES for SCALE_UP of consumer service replicas — via Section 9

RULE_KAFKA_002 — PARTITION_UNDER_REPLICATED:
  Condition:  under_replicated_partitions > 0 for > 2 minutes
  Priority:   CRITICAL
  Action:     Emit resource.recommendation.scale (ALERT_ONLY)
              Immediate escalation to Admin Ops
  Safe:       NO

RULE_KAFKA_003 — BROKER_DISK_GROWTH:
  Condition:  Kafka broker disk growth rate > 10 GB/day
  Priority:   MEDIUM
  Action:     Emit resource.recommendation.storage (retention policy review)
  Safe:       NO
```

### 8.6 ClickHouse Rules

```
RULE_CH_001 — INSERT_BACKLOG:
  Condition:  Merge backlog parts > 300 for any table
  Priority:   HIGH
  Action:     Emit resource.recommendation.scale (INSERT_THROTTLE advisory)
  Safe:       NO

RULE_CH_002 — DISK_GROWTH_VELOCITY:
  Condition:  ClickHouse disk growth > 5 GB/day sustained over 7 days
  Priority:   MEDIUM
  Action:     Emit resource.recommendation.storage (partition TTL review)
  Safe:       NO

RULE_CH_003 — QUERY_LATENCY:
  Condition:  ClickHouse SELECT p95 latency > 2000ms
  Priority:   HIGH
  Action:     Emit resource.recommendation.rightsize (query optimization advisory)
  Safe:       NO
```

### 8.7 Storage Rules (MinIO + Longhorn)

```
RULE_STORAGE_001 — MINIO_BUCKET_GROWTH:
  Condition:  Any MinIO bucket growth rate > 1 GB/day AND projected fill in < 90 days
  Priority:   MEDIUM if 90-day fill | HIGH if < 30-day fill
  Action:     Emit resource.recommendation.storage
              Include: bucket name, growth rate, projected fill date
  Safe:       NO

RULE_STORAGE_002 — LONGHORN_VOLUME_HEALTH:
  Condition:  Any Longhorn volume degraded OR replica rebuild active > 30 minutes
  Priority:   CRITICAL
  Action:     Emit resource.recommendation.scale (ALERT_ONLY)
              Escalate immediately
  Safe:       NO

RULE_STORAGE_003 — AUDIT_RETENTION_COMPLIANCE:
  Condition:  Audit log bucket projected to exceed capacity within 180 days
              (audit logs have 3-year retention — must never be lost)
  Priority:   HIGH
  Action:     Emit resource.recommendation.storage (storage expansion advisory)
  Safe:       NO
```

---

## SECTION 9 — PRE-APPROVED SAFE AUTONOMOUS ACTIONS

These are the **only actions** the agent may execute autonomously without human approval. All safe actions are gated through Open Policy Agent before execution.

```
SAFE_ACTION_GATE:
  Step 1: Rule engine generates recommendation with safe_action_eligible = TRUE
  Step 2: OPA policy evaluation:
          - Namespace in allowed_namespace_list?
          - Service in allowed_service_list?
          - Action type is SCALE_UP only? (SCALE_DOWN requires human approval after-hours)
          - Target replica count within safe_max_replicas limit?
          - No CRITICAL anomaly active for this service?
          - No manual override flag active?
          If any check fails → OPA returns DENY → recommendation queued for human review
  Step 3: Kubernetes API PATCH applied (HPA minReplicas or deployment replicas)
  Step 4: Previous state snapshot saved to safe_action_log.previous_state
  Step 5: Emit resource.safe_action.applied event
  Step 6: Automatic rollback timer set (60 minutes)
          If no human confirmation within 60 minutes → ROLLBACK applied automatically
          Emit resource.safe_action.rolled_back event
```

### 9.1 Allowed Safe Actions Table

| Action | Allowed Namespaces | Condition | Max Auto-Scale |
|---|---|---|---|
| HPA minReplicas SCALE_UP | core, realtime, billing, analytics | CPU > 75% sustained 5 min | +2 replicas per cycle |
| HPA minReplicas SCALE_UP | core, realtime | Kafka consumer lag > 10k sustained 5 min | +1 replica per cycle |
| Kafka consumer replica SCALE_UP | any | Consumer lag CRITICAL (>100k) | +2 replicas per cycle |

### 9.2 Permanently Forbidden Autonomous Actions

```
FORBIDDEN:  Pod deletion of any kind
FORBIDDEN:  Namespace deletion
FORBIDDEN:  SCALE_DOWN of any service (advisory only — human must approve)
FORBIDDEN:  Any action on media namespace (Jitsi / LiveKit / coturn)
FORBIDDEN:  Any action on ops namespace (observability stack)
FORBIDDEN:  Any PostgreSQL configuration change
FORBIDDEN:  Any Redis configuration change
FORBIDDEN:  Any Kafka broker configuration change
FORBIDDEN:  Any MinIO bucket policy change
FORBIDDEN:  Any Vault policy change
FORBIDDEN:  Any Keycloak configuration change
FORBIDDEN:  Billing service replicas manipulation
FORBIDDEN:  Any action on society namespace (offline sync — human must verify)
```

---

## SECTION 10 — ANOMALY DETECTION ENGINE (TRUST INFRASTRUCTURE)

### 10.1 Trust Infrastructure Role

Resource anomalies feed the Ecoskiller Trust Infrastructure by surfacing patterns that indicate malicious use, tenant abuse, or billing fraud. Consumers: Admin Governance Service, Wazuh SIEM, Billing Service, Scoring Engine.

### 10.2 Anomaly Detection Rules

```
RULE_TRUST_001 — BURST_ABUSE:
  Condition:  Single tenant's namespace CPU consumption > 60% of total namespace CPU
              sustained for > 10 minutes
              AND tenant subscription plan does not entitle this resource class
  Severity:   HIGH
  anomaly_type: BURST_ABUSE
  trust_impact_score: min(1.0, (tenant_cpu_share - 0.6) × 2.0)
  Action:     Emit resource.anomaly.detected
              Escalate to Admin Governance if severity = HIGH or CRITICAL

RULE_TRUST_002 — DDOS_SIGNAL:
  Condition:  Ingress request rate for a tenant > 10,000 req/min
              AND Kong rate limit logs show > 1,000 limit-exceeded events in 5 min window
  Severity:   CRITICAL
  anomaly_type: DDOS_SIGNAL
  trust_impact_score: 0.9
  Action:     Emit resource.anomaly.detected + escalated_to_admin = TRUE
              Wazuh SIEM correlation triggered

RULE_TRUST_003 — TENANT_OVERCONSUMPTION:
  Condition:  Tenant storage growth > 10× the 30-day average
              within a single 24-hour window
  Severity:   HIGH
  anomaly_type: TENANT_OVERCONSUMPTION
  Action:     Emit resource.anomaly.detected
              Include: tenant_id, bucket_name, growth_delta, expected_growth

RULE_TRUST_004 — STORAGE_BOMB:
  Condition:  Single API call results in MinIO PUT > 500 MB
              OR single session generates > 1,000 MinIO objects within 1 hour
  Severity:   CRITICAL
  anomaly_type: STORAGE_BOMB
  trust_impact_score: 0.85
  Action:     Emit resource.anomaly.detected + escalated_to_admin = TRUE

RULE_TRUST_005 — KAFKA_FLOOD:
  Condition:  Single service producing > 50,000 events/minute to any topic
              AND topic is not an approved high-volume topic (ClickHouse insert streams)
  Severity:   HIGH
  anomaly_type: KAFKA_FLOOD
  Action:     Emit resource.anomaly.detected

RULE_TRUST_006 — MEDIA_SESSION_ABUSE:
  Condition:  Single tenant opens > 50 concurrent Jitsi rooms
              OR single user joins > 5 concurrent GD sessions
              (structurally impossible under normal GD Orchestrator rules)
  Severity:   CRITICAL
  anomaly_type: MEDIA_SESSION_ABUSE
  trust_impact_score: 0.95
  Action:     Emit resource.anomaly.detected + escalated_to_admin = TRUE

RULE_TRUST_007 — DB_CONNECTION_FLOOD:
  Condition:  Single tenant's service holds > 40% of total PostgreSQL connections
              AND connection count has grown > 200% in the last 10 minutes
  Severity:   HIGH
  anomaly_type: DB_CONNECTION_FLOOD
  Action:     Emit resource.anomaly.detected
```

### 10.3 Trust Impact Score Formula

```
trust_impact_score = min(1.0,
    base_score(severity)
    + (escalated_to_admin ? 0.2 : 0)
    + (repeated_within_24h ? 0.15 : 0)
    + (multi_rule_trigger ? 0.1 : 0)
)

base_score:
    LOW      = 0.1
    MEDIUM   = 0.25
    HIGH     = 0.5
    CRITICAL = 0.75
```

Trust impact scores are **advisory signals only**. No automated penalty, suspension, or billing action based on score alone. Human admin review required.

---

## SECTION 11 — API CONTRACT (OPENAPI 3.1)

```yaml
openapi: 3.1.0
info:
  title: RESOURCE_OPTIMIZATION_AI_AGENT API
  version: 1.0.0

servers:
  - url: https://api.ecoskiller.com/v1/resource-optimization

security:
  - bearerAuth: []

paths:

  /recommendations:
    get:
      summary: List optimization recommendations
      x-required-role: PLATFORM_ADMIN | OPS_ENGINEER
      parameters:
        - name: namespace
          in: query
          schema: {type: string}
        - name: priority
          in: query
          schema: {type: string, enum: [CRITICAL, HIGH, MEDIUM, LOW]}
        - name: status
          in: query
          schema: {type: string, enum: [PENDING, APPROVED, APPLIED, REJECTED, EXPIRED, AUTO_APPLIED]}
        - name: safe_action_eligible
          in: query
          schema: {type: boolean}
      responses:
        "200": {description: Recommendation list}
        "403": {description: Insufficient role}

  /recommendations/{recommendation_id}/approve:
    post:
      summary: Approve a recommendation for manual application
      x-required-role: PLATFORM_ADMIN | OPS_ENGINEER
      parameters:
        - name: recommendation_id
          in: path
          required: true
          schema: {type: string}
      responses:
        "200": {description: Approved — queued for application}
        "403": {description: Insufficient role}
        "404": {description: Recommendation not found}
        "409": {description: Already applied or expired}

  /recommendations/{recommendation_id}/reject:
    post:
      summary: Reject a recommendation
      x-required-role: PLATFORM_ADMIN | OPS_ENGINEER
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [reason]
              properties:
                reason: {type: string, minLength: 10}
      responses:
        "200": {description: Rejected and logged}

  /anomalies:
    get:
      summary: List detected resource anomalies
      x-required-role: PLATFORM_ADMIN | OPS_ENGINEER
      parameters:
        - name: severity
          in: query
          schema: {type: string, enum: [LOW, MEDIUM, HIGH, CRITICAL]}
        - name: resolved
          in: query
          schema: {type: boolean}
        - name: namespace
          in: query
          schema: {type: string}
      responses:
        "200": {description: Anomaly list}

  /anomalies/{anomaly_id}/resolve:
    post:
      summary: Mark anomaly as resolved (admin only)
      x-required-role: PLATFORM_ADMIN
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [resolution_note]
              properties:
                resolution_note: {type: string, minLength: 10}
      responses:
        "200": {description: Resolved and logged}
        "403": {description: PLATFORM_ADMIN only}

  /safe-actions:
    get:
      summary: List all auto-applied safe actions and their rollback status
      x-required-role: PLATFORM_ADMIN | OPS_ENGINEER
      responses:
        "200": {description: Safe action log}

  /safe-actions/{action_id}/rollback:
    post:
      summary: Force rollback of a safe action (within rollback window)
      x-required-role: PLATFORM_ADMIN
      responses:
        "200": {description: Rollback applied}
        "400": {description: Rollback window expired}
        "403": {description: PLATFORM_ADMIN only}

  /snapshots/latest:
    get:
      summary: Get latest resource snapshot per namespace
      x-required-role: PLATFORM_ADMIN | OPS_ENGINEER
      parameters:
        - name: namespace
          in: query
          required: true
          schema: {type: string}
      responses:
        "200": {description: Latest snapshot data per service}

  /health:
    get:
      summary: Agent health, rule engine status, last observation cycle timestamp
      responses:
        "200": {description: Health status}

components:
  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
```

---

## SECTION 12 — SCHEDULED WORKFLOW (APACHE AIRFLOW — LOCKED)

Uses the existing Ecoskiller Airflow installation. No new Airflow infrastructure.

```
DAG: resource_optimization_observation_cycle
  Schedule:   Every 5 minutes (*/5 * * * *)
  Tasks:
    1. scrape_prometheus_metrics
       → Pull all declared metrics from Prometheus HTTP API
       → Write to resource_optimization.resource_snapshot
    2. poll_k8s_api
       → Pull pod counts, HPA status, node pressure per namespace
       → Merge with snapshot
    3. evaluate_optimization_rules
       → Run all rules in Section 8 deterministically
       → Write recommendations to resource_optimization.recommendation
    4. evaluate_anomaly_rules
       → Run all trust rules in Section 10
       → Write anomalies to resource_optimization.resource_anomaly
    5. execute_safe_actions
       → Query safe_action_eligible = TRUE AND status = PENDING
       → Run OPA policy gate
       → Apply approved actions via K8s API
       → Write to resource_optimization.safe_action_log
    6. publish_kafka_events
       → Emit all new recommendations and anomalies to Kafka
    7. expire_stale_recommendations
       → Mark PENDING recommendations older than 4 hours as EXPIRED

DAG: resource_optimization_rollback_watchdog
  Schedule:   Every 10 minutes
  Tasks:
    1. query_safe_actions_pending_confirmation
       → Find safe actions older than 60 minutes with no human confirmation
    2. apply_rollback
       → Restore previous_state via K8s API
       → Update safe_action_log.rollback_applied = TRUE
       → Emit resource.safe_action.rolled_back event

DAG: resource_optimization_weekly_report
  Schedule:   Monday 06:00 UTC
  Tasks:
    1. aggregate_recommendations_by_namespace (7-day)
    2. aggregate_anomalies_by_type (7-day)
    3. compute_safe_action_success_rate
    4. compute_unresolved_critical_count
    5. write_report_to_clickhouse
    6. emit_weekly_summary_notification (Notification Service)
```

---

## SECTION 13 — OBSERVABILITY (NON-OPTIONAL — USES EXISTING STACK)

### 13.1 Prometheus Metrics (Exported by Agent)

```
resource_optimization_observation_cycle_duration_seconds
resource_optimization_recommendations_generated_total{namespace, priority, optimization_type}
resource_optimization_anomalies_detected_total{namespace, anomaly_type, severity}
resource_optimization_safe_actions_applied_total{namespace, action_type}
resource_optimization_safe_actions_rolled_back_total{namespace}
resource_optimization_opa_denials_total{namespace}
resource_optimization_rule_evaluation_errors_total{rule_id}
resource_optimization_kafka_publish_failures_total{topic}
resource_optimization_pending_recommendations_gauge{namespace, priority}
resource_optimization_unresolved_critical_anomalies_gauge
```

### 13.2 Grafana Dashboard Requirements

Dashboard name: **Resource Optimization + Trust Signals**
Namespace: `ops`

Required panels:
- Observation cycle success rate (last 24h)
- Recommendations by priority and namespace (24h)
- Safe actions applied vs rolled back (7-day trend)
- OPA policy denials (7-day)
- Active unresolved CRITICAL anomalies (live)
- Trust anomaly type distribution (30-day)
- Top 5 CPU-consuming services per namespace (live)
- Redis memory utilization (live)
- Kafka consumer lag heatmap (live)
- PostgreSQL connection pool utilization per namespace (live)
- Media session count: Jitsi rooms + LiveKit tracks (live)
- MinIO bucket growth velocity (7-day)

**Dashboard absent = NON-COMPLIANT DEPLOYMENT**

### 13.3 Loki Log Schema

```json
{
  "timestamp": "ISO8601",
  "service": "resource-optimization-agent",
  "level": "INFO | WARN | ERROR | CRITICAL",
  "namespace": "k8s namespace observed",
  "service_name": "service observed",
  "event": "OBSERVATION_CYCLE_COMPLETE | RECOMMENDATION_GENERATED | ANOMALY_DETECTED |
            SAFE_ACTION_APPLIED | SAFE_ACTION_ROLLED_BACK | OPA_DENY | RULE_ERROR |
            KAFKA_PUBLISH_FAIL",
  "recommendation_id": "string or null",
  "anomaly_id": "string or null",
  "details": {}
}
```

No raw PII in logs. No tenant business data in logs. Resource metrics only.

---

## SECTION 14 — SECURITY BASELINE (INHERITED + AGENT-SPECIFIC)

```
INHERITS: Full Ecoskiller security baseline
          (WAF, rate limits, signed tokens, encrypted PII,
           immutable audit logs, tenant isolation at DB, OPA)

AGENT-SPECIFIC RULES:

SEC_A01 — K8s ServiceAccount is scoped to read + pre-approved patch only.
          No cluster-admin. No delete verbs. No secret read beyond own namespace.

SEC_A02 — OPA policy gate is MANDATORY before every safe action.
          OPA unavailable = NO safe actions executed. All queued for human review.

SEC_A03 — All recommendation and anomaly records are immutable once written.
          No UPDATE on recommendation, resource_anomaly, or safe_action_log tables.

SEC_A04 — Audit hash (SHA256) computed on every record at write time.
          Hash verified on every read. Tamper = HARD SECURITY FAILURE → Wazuh alert.

SEC_A05 — The agent reads Prometheus, K8s API, and approved PostgreSQL views only.
          It MUST NOT read raw tenant business data (user profiles, job records,
          billing records, GD scores, or any domain service table).

SEC_A06 — Vault-managed secrets only. No hardcoded credentials anywhere.

SEC_A07 — All API endpoints require valid JWT from Keycloak.
          PLATFORM_ADMIN or OPS_ENGINEER role required. No public endpoints
          except /health.

SEC_A08 — Rollback capability is MANDATORY for every safe action.
          An action without a saved previous_state in safe_action_log
          MUST NOT be applied.

SEC_A09 — Rate limit on safe actions: maximum 10 safe actions per 5-minute cycle
          platform-wide. Exceeding limit = queue remainder for human review.
```

---

## SECTION 15 — FAILURE HANDLING

| Failure Scenario | System Response |
|---|---|
| Prometheus API unreachable | Log WARN, skip observation cycle, emit health degraded status, do NOT apply safe actions |
| Kubernetes API unreachable | Log ERROR, skip all K8s-based rules and safe actions, rules that only need Prometheus continue |
| PostgreSQL write failure | Retry 3× with exponential backoff → STOP cycle, emit resource.recommendation.scale ALERT_ONLY via Kafka dead-letter topic |
| Kafka publish failure | Retry 5× → dead-letter topic → alert ops via ntfy |
| OPA unavailable | DENY all safe actions, queue all as PENDING for human review, log CRITICAL |
| Redis unavailable | Log ERROR, skip Redis-dependent rules, continue with all other rules |
| ClickHouse write failure | Log WARN, continue operation (analytics writes are non-blocking) |
| Airflow DAG failure | Wazuh alert + Grafana alert rule fires + Admin Ops notified via ntfy |

**No partial observation cycle outputs permitted under rule engine failure.**
**If rule engine throws exception → mark cycle as FAILED → emit no recommendations for affected rules.**

---

## SECTION 16 — ROLE-BASED ACCESS (RBAC)

```
PLATFORM_ADMIN:
  - Read all recommendations (all namespaces)
  - Approve / reject any recommendation
  - Read all anomalies
  - Resolve any anomaly
  - Trigger manual observation cycle
  - Force rollback of any safe action
  - View full safe action log

OPS_ENGINEER:
  - Read recommendations (all namespaces)
  - Approve / reject recommendations (non-CRITICAL)
  - Read anomalies
  - Cannot resolve CRITICAL anomalies (PLATFORM_ADMIN only)
  - View safe action log

TENANT_ADMIN:
  - No access to this agent's API
  - Optimization recommendations affecting their tenant
    are surfaced through the Admin Ops Console (read-only summary)
    by the Admin Governance Service — NOT directly via this API

STUDENTS | TRAINERS | MENTORS | RECRUITERS | PARENTS:
  - Zero access to any resource optimization endpoint

AUTOMATION_AGENT (Airflow service account):
  - Trigger observation DAGs only
  - No read/write via API
```

---

## SECTION 17 — OUTPUT TO ENTERPRISE OPTIMIZATION CONSUMERS

| Consumer | Kafka Topic | Optimization Action |
|---|---|---|
| Kubernetes HPA Controller | `resource.recommendation.scale` | Admin approves → HPA patch applied |
| Admin Ops Console | All recommendation topics | Surface in ops dashboard with one-click approval |
| Notification Service | `resource.recommendation.scale` (CRITICAL priority) | Alert ops team via ntfy + email |
| Voice GD Orchestrator | `resource.recommendation.media` | Pre-plan GD batch sizes against Jitsi capacity |
| Dojo Match Engine | `resource.recommendation.media` | Pre-plan arena scheduling against LiveKit capacity |
| Billing Service | `resource.recommendation.storage` | Inform billing of storage overage risk |

---

## SECTION 18 — OUTPUT TO TRUST INFRASTRUCTURE CONSUMERS

| Consumer | Kafka Topic | Trust Action |
|---|---|---|
| Admin Governance Service | `resource.anomaly.detected` | CRITICAL anomalies → moderation queue |
| Wazuh SIEM | `resource.anomaly.detected` | Security correlation (DDOS_SIGNAL, STORAGE_BOMB) |
| Billing Service | `resource.anomaly.detected` (TENANT_OVERCONSUMPTION) | Flag account for billing review |
| Scoring Engine | `resource.anomaly.detected` (MEDIA_SESSION_ABUSE) | Variance anomaly flag on active GD/Dojo sessions |
| PREDICTIVE_DEMAND_FORECASTING_AGENT | `resource.anomaly.detected` | Anomaly-aware forecast confidence downgrade |

---

## SECTION 19 — DETERMINISM RULES

```
DET_01: Identical metric input + identical ConfigMap thresholds = identical rule output.
DET_02: Observation cycle_id is deterministic: SHA256(cycle_start_utc + namespace_list_hash)
DET_03: All thresholds are in ConfigMap. No thresholds are hardcoded or derived at runtime.
DET_04: Rule evaluation order is fixed and deterministic (rules execute in Section 8 declaration order).
DET_05: Safe action OPA policy is versioned and stored in Vault. Policy version logged on every evaluation.
DET_06: Rollback state is captured BEFORE action is applied. No rollback = no action applied.
DET_07: Anomaly severity levels are fixed lookup tables. No computed severity.
```

---

## SECTION 20 — AUDIT TRAIL (IMMUTABLE — APPEND ONLY)

Every observation cycle, recommendation, anomaly, and safe action MUST write an immutable audit record.

```
Per recommendation:
  recommendation_id, namespace, service_name, resource_type, optimization_type,
  priority, confidence_score, safe_action_eligible, status,
  generated_at, reviewed_by, applied_at,
  audit_hash (SHA256 of all fields at write time)

Per anomaly:
  anomaly_id, namespace, service_name, anomaly_type, severity,
  observed_value, threshold_value, trust_impact_score,
  detected_at, resolved_at, resolution_note, escalated_to_admin,
  audit_hash

Per safe action:
  action_id, recommendation_id, namespace, service_name, action_type,
  previous_state, applied_state, opa_policy_result, applied_at,
  rollback_state, rollback_applied,
  audit_hash
```

**UPDATE or DELETE on audit tables = HARD SECURITY FAILURE → Wazuh SIEM alert + Admin escalation**

---

## SECTION 21 — INTEGRATION WITH PREDICTIVE_DEMAND_FORECASTING_AGENT

This agent is a sibling agent in the same Enterprise Optimization layer. Integration rules:

```
CONSUME:  forecast.demand.gd_slot → pre-compute Jitsi capacity recommendations
          before demand hits (proactive vs reactive)
CONSUME:  forecast.demand.dojo_slot → pre-compute LiveKit capacity recommendations
CONSUME:  forecast.demand.skill → pre-compute analytics namespace scale recommendations

PRODUCE:  resource.anomaly.detected →
          PREDICTIVE_DEMAND_FORECASTING_AGENT consumes this to downgrade
          forecast confidence scores when resource anomalies are active

COORDINATION RULE:
  If PREDICTIVE_DEMAND_FORECASTING_AGENT has emitted a HIGH-confidence
  GD slot surge forecast for T+2h → RESOURCE_OPTIMIZATION_AI_AGENT MUST
  emit a MEDIA capacity recommendation at priority=HIGH for the same window.
  This is a mandatory proactive coupling — not optional.
```

---

## SECTION 22 — ANTIGRAVITY EXECUTION RULES

```
DO NOT invent optimization rule types not declared in Section 8.
DO NOT introduce new infrastructure services.
DO NOT write to other services' database schemas.
DO NOT delete Kubernetes resources.
DO NOT scale DOWN any service autonomously — SCALE_DOWN is advisory only.
DO NOT execute safe actions without OPA gate pass.
DO NOT apply safe actions without capturing rollback state first.
DO NOT emit anomaly signals without matching a rule in Section 10.
DO NOT expose tenant business data through any recommendation or log.
DO NOT suppress CRITICAL anomalies or recommendations.
DO NOT allow recommendations to expire silently — must log EXPIRED status.
DO NOT touch media namespace autonomously.
DO NOT touch ops namespace autonomously.
DO NOT hardcode thresholds — all thresholds must be in ConfigMap.
DO NOT run without Vault-sourced secrets.
```

```
REQUIRED:
  Generate agent code only within declared tech stack (Section 5).
  Generate K8s manifests with all components in Section 4.1.
  Generate PostgreSQL schema only in resource_optimization namespace.
  Enforce RLS on every table (Section 6.2).
  Write immutable audit records (Section 20) for every recommendation, anomaly, safe action.
  Gate every safe action through OPA (Section 9).
  Capture rollback state before every safe action (Section 9).
  Auto-rollback safe actions unconfirmed after 60 minutes (Section 12).
  Publish to all declared Kafka topics (Section 7.2).
  Emit all declared Prometheus metrics (Section 13.1).
  Deploy Grafana dashboard with all required panels (Section 13.2).
  Integrate with PREDICTIVE_DEMAND_FORECASTING_AGENT per Section 21.
```

---

## SECTION 23 — VERSION GOVERNANCE

```
CURRENT_VERSION           = 1.0.0
CHANGE_POLICY             = ADD_ONLY
BREAKING_CHANGES          = MAJOR version bump + architect approval required
NEW_RULE_ADDITION         = MINOR version bump
Threshold config change   = PATCH version bump (audit log entry required)
OPA policy change         = MINOR version bump (security review required)
Safe action list change   = MINOR version bump (architect approval required)
```

**Silent mutations = FORBIDDEN**
**All versions retained in Forgejo Git history**
**No version downgrade permitted without architect sign-off**

---

## SECTION 24 — DEPLOYMENT COMPLETION CHECKLIST (ENFORCEMENT GATE)

Before any deployment claim is permitted, all of the following must be verified and checked:

- [ ] PostgreSQL schema `resource_optimization` created with RLS enabled on all 4 tables
- [ ] Kubernetes ServiceAccount created with scoped RBAC (read + pre-approved patch verbs only)
- [ ] OPA policy deployed and returning ALLOW/DENY correctly for safe action test cases
- [ ] HashiCorp Vault secrets mounted — zero hardcoded credentials
- [ ] All 10 Kafka consumed topics verified reachable
- [ ] All 7 Kafka produced topics created with correct schema
- [ ] All 8 API endpoints deployed and protected by JWT + RBAC
- [ ] Both Airflow DAGs deployed and verified green in staging
- [ ] All 10 Prometheus metrics exported and scraped by ops Prometheus
- [ ] Grafana dashboard deployed with all 12 required panels
- [ ] Loki logging verified with correct structured JSON format
- [ ] OpenTelemetry traces flowing to Grafana Tempo
- [ ] OPA unavailability test: safe actions blocked correctly
- [ ] RLS cross-tenant isolation test: 403 on cross-tenant reads verified
- [ ] Rollback test: safe action → 60min timeout → auto-rollback verified
- [ ] Audit hash tamper detection test: modified record detected correctly
- [ ] Wazuh SIEM alert test: CRITICAL anomaly triggers correlation rule
- [ ] CRITICAL recommendation triggers ntfy + email notification
- [ ] PREDICTIVE_DEMAND_FORECASTING_AGENT integration: proactive media recommendation on GD surge forecast verified
- [ ] Network policy test: ingress from non-ops namespace blocked

**Any unchecked item = INCOMPLETE DEPLOYMENT. No production claim permitted.**

---

## FINAL SEAL

```
AGENT_ID                = RESOURCE_OPTIMIZATION_AI_AGENT
LAYER                   = ENTERPRISE_OPTIMIZATION + TRUST_INFRASTRUCTURE
EXECUTION_ENGINE        = ANTIGRAVITY
STATUS                  = ✔ SEALED
MUTATION_POLICY         = ✔ ADD_ONLY
ASSUMPTION_POLICY       = ✔ FORBIDDEN
CREATIVE_DEVIATION      = ✔ FORBIDDEN
PARTIAL_OUTPUT          = ✔ FORBIDDEN
AUTONOMOUS_DELETION     = ✔ FORBIDDEN
AUTONOMOUS_SCALE_DOWN   = ✔ FORBIDDEN
MEDIA_NAMESPACE_TOUCH   = ✔ FORBIDDEN (autonomous)
OPS_NAMESPACE_TOUCH     = ✔ FORBIDDEN (autonomous)
OPA_GATE                = ✔ MANDATORY FOR ALL SAFE ACTIONS
ROLLBACK_CAPTURE        = ✔ MANDATORY BEFORE ALL SAFE ACTIONS
AI_DECISION_POWER       = ✔ ZERO
HUMAN_OVERRIDE          = ✔ ALWAYS PERMITTED — NEVER BLOCKED
TENANT_ISOLATION        = ✔ ENFORCED (RLS at DB layer)
AUDIT_IMMUTABILITY      = ✔ ENFORCED (append-only + SHA256 tamper detection)
INFRA_ADDITIONS         = ✔ ZERO (uses existing stack only)
SIBLING_AGENT_COUPLING  = ✔ DECLARED (PREDICTIVE_DEMAND_FORECASTING_AGENT)
VERSION                 = 1.0.0
```

**ANY DEVIATION FROM THIS DOCUMENT = STOP EXECUTION → REPORT → NO DEPLOYMENT CLAIM PERMITTED**

---

*Ecoskiller Platform — Internal Architecture Document — Confidential*
*RESOURCE_OPTIMIZATION_AI_AGENT v1.0.0 — March 2026*
