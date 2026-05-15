# 🔒 INTEGRATION_HEALTH_MONITOR_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED AGENT SPECIFICATION
**Status:** FINAL · SEALED · GOVERNED · DETERMINISTIC  
**Mutation Policy:** ADD-ONLY via version bump  
**Interpretation Authority:** NONE  
**Creative Deviation:** FORBIDDEN  
**Assumption Filling:** FORBIDDEN  
**Default Behavior:** DENY  

---

## 🔐 EXECUTION MODE DECLARATION

```
AGENT_LOCK_STATUS        = SEALED
EXECUTION_MODE           = DETERMINISTIC + VALIDATED
MUTATION_POLICY          = ADD_ONLY
CREATIVE_INTERPRETATION  = FORBIDDEN
ASSUMPTION_FILLING       = FORBIDDEN
DEFAULT_BEHAVIOR         = DENY
FAILURE_MODE             = STOP_EXECUTION → LOG_INCIDENT → ESCALATE
OVERRIDE_AUTHORITY       = NONE
BYPASS_ATTEMPTS          = SECURITY_VIOLATION → AUTO_ESCALATE
```

---

## 1️⃣ AGENT IDENTITY (NON-NEGOTIABLE)

```
AGENT_NAME         = INTEGRATION_HEALTH_MONITOR_AGENT
AGENT_ID           = IHMA-ANTIGRAVITY-001
SYSTEM_ROLE        = Cross-System Integration Integrity Enforcer & Health Orchestrator
PRIMARY_DOMAIN     = Platform Operations / Observability / Governance
EXECUTION_MODE     = Deterministic + Validated
DATA_SCOPE         = All microservice integration touchpoints, inter-agent event buses,
                     external API connectors, tenant-scoped data flows, audit pipelines
TENANT_SCOPE       = Strict Per-Tenant Isolation (No Cross-Tenant Inspection)
FAILURE_POLICY     = Halt on Ambiguity → Log → Escalate → No Silent Failure
AGENT_CLASS        = Tier-1 Infrastructure Governance Agent
PLATFORM           = Ecoskiller Antigravity
ARCHITECTURE       = Microservices + Event-Driven
SCALE_TARGET       = 10M–100M Users
```

This agent **never assumes** missing specifications. If any integration contract, schema, or health threshold is absent, the agent halts and escalates. It does not self-heal through assumption.

---

## 2️⃣ PURPOSE DECLARATION

### Problem This Agent Solves

The Ecoskiller Antigravity platform is composed of dozens of interdependent microservices, agent pipelines, Kafka event buses, Flutter/React frontends, external vendor APIs, and multi-tenant data flows. Any degradation, contract breach, schema drift, timeout cascade, or silent failure in any integration layer can corrupt audit trails, misfire ML models, break tenant isolation, or deliver wrong decisions to users at scale (10M–100M users).

The **INTEGRATION_HEALTH_MONITOR_AGENT (IHMA)** exists to:

1. Continuously monitor the health, latency, contract conformance, and throughput of **every integration point** across the platform.
2. Detect schema drift, event contract violations, API degradation, and inter-agent dependency failures **before they cascade**.
3. Enforce integration SLAs across all microservices, agents, Kafka topics, third-party connectors, and data pipelines.
4. Emit structured health signals to the **OBSERVABILITY_AGENT** and all downstream consumers.
5. Halt, log, and escalate any integration anomaly that breaches defined thresholds.
6. Guarantee that no silent integration failure propagates through the platform.

### What Input It Consumes

- Heartbeat signals from all registered microservices and agents
- Kafka topic lag metrics and event schema checksums
- API response time, status code, and payload schema samples
- Database connection pool status (PostgreSQL, Redis, Elasticsearch)
- Inter-agent dependency health events
- Tenant isolation verification signals
- External vendor API health probes (payment gateways, email, SMS, video)
- CI/CD deployment event notifications
- Feature flag state changes

### What Output It Produces

- Structured integration health reports (per service, per tenant, per domain)
- Integration SLA breach alerts
- Schema drift detection reports
- Escalation events to GOVERNANCE_AGENT and OPS_TEAM
- OBSERVABILITY_AGENT feed (metrics, logs, traces)
- FEATURE_STORE_AGENT feature vectors (platform health behavioral features)
- Kafka health topic events for downstream consumers
- Public platform health signals (for status.ecoskiller.com)

### Upstream Agents / Services That Feed This Agent

```
UPSTREAM_AGENTS:
  - All Registered Microservices (heartbeat emission)
  - OBSERVABILITY_AGENT (initial baseline metrics)
  - DEPLOYMENT_AGENT (deployment event triggers)
  - API_GATEWAY_SERVICE (request telemetry)
  - KAFKA_BROKER_CLUSTER (topic lag & schema registry)
  - FEATURE_FLAG_SERVICE (flag state changes)
  - TENANT_ISOLATION_AGENT (cross-tenant violation signals)
  - DB_POOL_MANAGER (connection pool health)
  - EXTERNAL_VENDOR_CONNECTOR_HUB (vendor probe results)
```

### Downstream Agents / Services That Depend on This Agent

```
DOWNSTREAM_AGENTS:
  - OBSERVABILITY_AGENT (receives full health feed)
  - GOVERNANCE_AGENT (receives breach escalations)
  - INCIDENT_MANAGEMENT_AGENT (receives P0/P1 triggers)
  - NOTIFICATION_SERVICE (triggers ops team alerts)
  - AUDIT_LOG_AGENT (receives immutable health log entries)
  - RANK_UPDATE_AGENT (receives SLA status for platform trust score)
  - FEATURE_STORE_AGENT (receives platform behavioral health vectors)
  - PUBLIC_STATUS_PAGE_SERVICE (receives sanitized public health signals)
```

---

## 3️⃣ INPUT CONTRACT (STRICT)

All inputs must conform exactly. **No null tolerance without explicit policy. Reject malformed data. Log all validation failures.**

```json
INPUT_SCHEMA: {
  "required_fields": [
    "source_agent_id",
    "source_service_name",
    "tenant_id",
    "domain_track",
    "signal_type",
    "signal_timestamp_utc",
    "payload_version",
    "environment"
  ],
  "optional_fields": [
    "schema_checksum",
    "response_time_ms",
    "status_code",
    "error_code",
    "kafka_topic_name",
    "kafka_consumer_group_lag",
    "db_pool_active_connections",
    "db_pool_max_connections",
    "external_vendor_name",
    "vendor_probe_latency_ms",
    "deployment_version",
    "feature_flag_name",
    "feature_flag_state"
  ],
  "validation_rules": [
    "source_agent_id MUST match registered agent registry — reject if unknown",
    "tenant_id MUST be non-null and resolve to an active tenant — reject if unresolved",
    "domain_track MUST be one of [Arts, Commerce, Science, Technology, Administration] — reject all others",
    "signal_type MUST be one of [HEARTBEAT, SCHEMA_PROBE, API_PROBE, KAFKA_LAG, DB_POOL, VENDOR_PROBE, DEPLOYMENT_EVENT, FLAG_CHANGE]",
    "signal_timestamp_utc MUST be ISO-8601 UTC — reject malformed timestamps",
    "payload_version MUST match current or N-1 schema version — reject older versions",
    "environment MUST be one of [dev, test, staging, production]",
    "response_time_ms MUST be positive integer if provided",
    "kafka_consumer_group_lag MUST be non-negative integer if provided",
    "schema_checksum MUST be SHA-256 hex string if provided"
  ],
  "security_checks": [
    "Validate mTLS source certificate against registered service identity",
    "Validate JWT bearer token against tenant-scoped RBAC authorization",
    "Reject any input with cross-tenant tenant_id mismatch",
    "Validate source IP against registered service network segment",
    "Reject inputs with payload size exceeding 512KB",
    "Validate encryption-in-transit (TLS 1.3 minimum)"
  ],
  "domain_checks": [
    "Verify source_agent_id belongs to the declared domain_track",
    "Verify domain_track matches tenant's contracted domain access",
    "No cross-domain signals allowed without explicit governance grant"
  ],
  "null_policy": {
    "required_fields": "ZERO NULL TOLERANCE — reject and log immediately",
    "optional_fields": "NULL allowed but must be explicitly typed as null — no omission",
    "action_on_rejection": "LOG_VALIDATION_FAILURE → EMIT_REJECTION_EVENT → NO_PARTIAL_PROCESSING"
  }
}
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

All outputs must include confidence, version reference, and traceability. **No partial outputs.**

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "health_report_id": "UUID",
    "evaluation_timestamp_utc": "ISO-8601",
    "source_agent_id": "string",
    "source_service_name": "string",
    "tenant_id": "string",
    "domain_track": "string",
    "environment": "string",
    "integration_status": "HEALTHY | DEGRADED | BREACHED | UNKNOWN",
    "sla_status": "WITHIN_SLA | SLA_WARNING | SLA_BREACH",
    "schema_drift_detected": "boolean",
    "schema_drift_details": "object | null",
    "latency_p50_ms": "integer",
    "latency_p95_ms": "integer",
    "latency_p99_ms": "integer",
    "error_rate_percent": "float",
    "kafka_lag_status": "NORMAL | ELEVATED | CRITICAL | NOT_APPLICABLE",
    "tenant_isolation_status": "CONFIRMED | VIOLATION_DETECTED",
    "anomaly_flags": ["array of detected anomaly codes"],
    "recommended_action": "NONE | ALERT | HALT | ESCALATE",
    "escalation_target": "string | null"
  },
  "confidence_score": "0.0–1.0 (float, two decimal places)",
  "model_version": "IHMA-RULES-ENGINE-v{major}.{minor}.{patch}",
  "audit_reference": "UUID (immutable, append-only)",
  "next_trigger_event": [
    "HEALTH_SIGNAL_EMITTED",
    "SLA_BREACH_EVENT (conditional)",
    "SCHEMA_DRIFT_EVENT (conditional)",
    "TENANT_VIOLATION_EVENT (conditional)",
    "ESCALATION_TRIGGERED (conditional)"
  ]
}
```

**Confidence Score Interpretation:**
```
1.00       = All signals received, all checks passed, deterministic output
0.80–0.99  = Minor signal gaps, output valid but flagged for review
0.60–0.79  = Significant signal gaps, output is WARNING-grade
< 0.60     = INSUFFICIENT_DATA — halt output, escalate, do not emit degraded health signal
```

---

## 5️⃣ ML / AI LOGIC LAYER

This agent operates as **95% Rules-Engine + 5% Statistical Anomaly Detection**. It is predominantly deterministic. No LLM interpretation is used in decision paths.

### Rules Engine (Primary — 95%)

```
ENGINE_TYPE       = Deterministic Rules Engine
EXECUTION_MODEL   = Threshold evaluation + Contract comparison
DRIFT_DETECTION   = Schema checksum comparison (SHA-256 hash comparison against
                    registered schema registry)
SLA_EVALUATION    = Threshold breach detection (latency, error rate, lag)
TENANT_CHECK      = Identity assertion against tenant registry
VERSION_CONTROL   = Rules engine version stored in every output (immutable)
```

### Statistical Anomaly Detection (Secondary — 5%)

```
MODEL_TYPE        = Time-Series Anomaly Detection
                    (Isolation Forest on latency/error rate time-series)

FEATURES_USED:
  - response_time_ms (rolling 5m, 15m, 1h window)
  - error_rate_percent (rolling 5m, 15m, 1h window)
  - kafka_consumer_group_lag (per topic, per consumer group)
  - db_pool_utilization_percent
  - api_success_rate_percent
  - event_throughput_per_second (per Kafka topic)

TRAINING_FREQUENCY  = Weekly (on production telemetry, no PII, no tenant data)
DRIFT_DETECTION:
  - Monitor feature distribution shift (KL divergence threshold: 0.1)
  - Monitor model accuracy degradation (precision drop > 5% triggers retrain flag)

VERSION_CONTROL:
  - Store model_version in every output (immutable reference)
  - Model versions: ML-ANOMALY-v{major}.{minor}.{patch}
  - Old models retained for 90 days minimum (audit compliance)

TRAINING_DATA_POLICY:
  - NO user PII in training data
  - NO tenant-specific content in training data
  - ONLY anonymized platform-level telemetry metrics
```

### AI Usage (Minimal — 0% in decision paths)

```
AI_USAGE_SCOPE    = NONE in core health evaluation or decision paths
                    AI may assist in post-incident RCA summarization
                    (offline, non-realtime, governance-approved only)

PROMPT_GOVERNANCE = Versioned | Deterministic | No creative interpretation
                    AI MUST NOT influence health status outputs
                    AI MUST NOT recommend infrastructure changes autonomously
```

---

## 6️⃣ SCALABILITY DESIGN

```
EXPECTED_RPS          = 50,000–500,000 health signal ingestion/sec at peak
                        (scales with 10M–100M user activity)
LATENCY_TARGET        = P99 < 200ms for signal ingestion
                        P99 < 500ms for health report generation
MAX_CONCURRENCY       = 10,000 parallel signal processors
QUEUE_STRATEGY        = Kafka-backed ingestion queue (topic: ihma.signals.raw)
                        Dead Letter Queue: ihma.signals.dlq
SCALING_MODEL         = Horizontal auto-scaling (Kubernetes HPA)
                        Stateless execution per agent instance
EXECUTION_STATE       = Stateless (all state in Redis + PostgreSQL)
IDEMPOTENCY           = All signal processing is idempotent
                        (Idempotency key: source_agent_id + signal_timestamp_utc + tenant_id)
ASYNC_PROCESSING      = Yes — Kafka consumer group model
EVENT_DRIVEN          = Yes — All outputs emitted as Kafka events
CIRCUIT_BREAKER       = Enabled on all external probes
                        (5 failures in 30s → open circuit, retry after 60s)
BACKPRESSURE          = Kafka consumer lag threshold: 10,000 messages → emit KAFKA_LAG_CRITICAL
```

---

## 7️⃣ INTEGRATION HEALTH MONITORING SCOPE

This section defines **every integration surface** this agent monitors across the Ecoskiller Antigravity platform.

### 7A — Core Microservice Health Contracts

```
MONITORED_SERVICES:
  Lane A — Foundation:
    - Identity Service
    - RBAC/ABAC Authorization Service
    - Multi-Tenant Management Service
    - API Gateway Service
    - Event Schema Registry

  Lane B — Data:
    - PostgreSQL Primary/Replica (connection pool, replication lag)
    - Redis Cluster (hit rate, eviction rate, memory utilization)
    - Elasticsearch (index health, query latency, shard status)
    - CQRS Read/Write Store Sync Lag
    - Audit Log Append Pipeline

  Lane C — Core Services:
    - Job Portal Service
    - Skill Development Service
    - Project Execution Service
    - Education Service
    - Marketplace Service
    - Dojo Engine Service

  Lane D — Governance:
    - Notification Service
    - Billing Service
    - Reputation Service
    - Moderation Service

  Lane E — UI Backends:
    - Flutter BFF (Backend for Frontend)
    - Next.js Web API (SEO layer)
    - WebSocket/LiveKit Gateway

  Lane F — Intelligence:
    - ML Matching Service
    - Ranking & Discovery Service
    - AI Explainability Service
    - Feature Store Agent
    - Skill Gap Analysis Engine
    - Intelligence Detection Engine (EIE/HIA)

  Lane G — DevOps/Infrastructure:
    - Kubernetes API Server
    - CI/CD Pipeline (ArgoCD/GitHub Actions)
    - Secrets Manager
    - Observability Stack (Prometheus, Grafana, Jaeger)
```

### 7B — Kafka Event Bus Health Monitoring

```
KAFKA_HEALTH_CHECKS:
  - Consumer group lag per topic (threshold: >1000 messages = WARNING, >10000 = CRITICAL)
  - Topic throughput (TPS monitoring, anomaly detection)
  - Schema Registry compliance (every emitted event validated against registered schema)
  - Dead Letter Queue (DLQ) accumulation rate
  - Broker availability (ISR count per partition)
  - Replication factor compliance

CRITICAL_TOPICS_MONITORED:
  - ihma.signals.raw
  - platform.events.core
  - audit.log.stream
  - gamification.events
  - tenant.isolation.events
  - billing.transaction.events
  - ml.feature.vectors
  - notification.dispatch.queue
  - search.index.events
  - dojo.session.events
```

### 7C — External Vendor Integration Health

```
VENDOR_PROBES:
  Payment Gateways:
    - Probe type: Synthetic transaction (non-PII test card)
    - SLA: P99 < 3000ms, success rate > 99.5%
    - Alert threshold: 3 consecutive failures OR success rate < 99%

  Email Service (e.g., SendGrid/SES):
    - Probe type: API health endpoint + delivery rate sampling
    - SLA: API P99 < 500ms, delivery rate > 98%

  SMS Service:
    - Probe type: API health endpoint
    - SLA: P99 < 1000ms

  LiveKit (Video/Audio for Dojo):
    - Probe type: Room creation/teardown synthetic test
    - SLA: Room creation P99 < 2000ms

  Elasticsearch Cloud (if managed):
    - Probe type: Index status + query health check
    - SLA: Query P99 < 200ms

  Object Storage (S3/GCS):
    - Probe type: Read/write synthetic test (non-PII object)
    - SLA: P99 < 500ms

  CDN:
    - Probe type: Asset delivery latency check from multiple regions
    - SLA: P99 < 300ms globally
```

### 7D — Inter-Agent Dependency Health

```
INTER_AGENT_HEALTH_MATRIX:
  For each registered agent pair with a declared dependency:
    - Last successful event exchange timestamp
    - Event delivery latency
    - Schema conformance of last N=100 events
    - Dead event detection (no event received in > defined TTL)
    - Circular dependency detection (event chain analysis)

DEPENDENCY_TTL_POLICY:
  HEARTBEAT agents:     TTL = 30 seconds (missing = DEGRADED)
  Event-driven agents:  TTL = configurable per agent pair contract
  Critical path agents: TTL = 15 seconds (missing = HALT + ESCALATE)
```

### 7E — Tenant Isolation Integrity Verification

```
TENANT_ISOLATION_CHECKS:
  - Random sampling of N=10 API responses per tenant per minute
  - Verify tenant_id in response payload matches requesting tenant
  - Verify no cross-tenant data references in sampled payloads
  - Monitor RBAC enforcement on tenant-scoped endpoints
  - Detect any anomalous cross-tenant query patterns in DB query logs
  - Domain track isolation: Arts/Commerce/Science/Technology/Administration
    (verify domain_track in event payloads matches tenant contract)

VIOLATION_RESPONSE:
  Any tenant isolation violation:
  → IMMEDIATE HALT of affected service instance
  → EMIT TENANT_VIOLATION_EVENT (P0)
  → LOG (immutable, append-only)
  → ESCALATE to GOVERNANCE_AGENT + COMPLIANCE_ADMIN
  → DO NOT SELF-HEAL OR AUTO-REMEDIATE
```

---

## 8️⃣ SECURITY ENFORCEMENT

```
SECURITY_MODEL = Zero-Trust Multi-Tenant

ENFORCEMENT_RULES:
  ✅ Tenant isolation validation on every input signal
  ✅ Domain isolation validation (no cross-domain signals without explicit grant)
  ✅ Role-based authorization: Only OBSERVABILITY_ROLE, OPS_ROLE, COMPLIANCE_ROLE
     may read health reports
  ✅ mTLS on all service-to-service communication
  ✅ TLS 1.3 minimum on all external probes
  ✅ Encryption at rest: All health data encrypted (AES-256)
  ✅ Audit logging: Append-only (no delete, no update)
  ✅ Access log tracking: Every health report read is logged
  ✅ API key rotation enforcement: Vendor API keys rotated every 90 days
     (alert if key age > 80 days)
  ✅ Secret scanning: Detect any secrets accidentally emitted in health payloads
     (immediately halt + escalate)

FORBIDDEN:
  ❌ No cross-tenant queries
  ❌ No health data shared across tenants
  ❌ No vendor API credentials in logs
  ❌ No PII in health payloads
  ❌ No raw DB query content in health logs (query hashes only)
  ❌ No bypassing of mTLS for internal agents
```

---

## 9️⃣ AUDIT & TRACEABILITY

Every execution must produce an immutable audit entry. Logs must be append-only. No modification or deletion is permitted under any circumstance.

```json
AUDIT_LOG_SCHEMA: {
  "audit_id":           "UUID (immutable, system-generated)",
  "timestamp_utc":      "ISO-8601 UTC",
  "actor_id":           "source_agent_id or 'IHMA_INTERNAL'",
  "tenant_id":          "string",
  "domain_track":       "string",
  "environment":        "dev | test | staging | production",
  "input_hash":         "SHA-256 of raw input payload",
  "output_hash":        "SHA-256 of output health_report",
  "model_version":      "IHMA-RULES-ENGINE-v{x}.{y}.{z}",
  "ml_model_version":   "ML-ANOMALY-v{x}.{y}.{z} | NOT_INVOKED",
  "decision_path":      ["ordered list of rules/checks evaluated"],
  "integration_status": "HEALTHY | DEGRADED | BREACHED | UNKNOWN",
  "confidence_score":   "float 0.00–1.00",
  "anomaly_flags":      ["array of anomaly codes"],
  "schema_drift":       "boolean",
  "sla_breach":         "boolean",
  "tenant_violation":   "boolean",
  "escalation_emitted": "boolean",
  "processing_time_ms": "integer"
}
```

**Audit Storage:** Append-only Kafka topic `audit.log.stream` → persisted to immutable audit datastore (WORM-compliant object storage). Retention: minimum 7 years.

---

## 🔟 FAILURE POLICY

No silent failures are permitted under any circumstance.

```
FAILURE_SCENARIOS AND RESPONSES:

[F-01] INVALID INPUT SIGNAL:
  → REJECT_SIGNAL
  → LOG_VALIDATION_FAILURE (audit entry)
  → EMIT_REJECTION_EVENT to ihma.signals.rejected topic
  → NOTIFY source_agent_id of rejection reason
  → DO NOT process further

[F-02] SOURCE AGENT NOT REGISTERED:
  → HALT_PROCESSING
  → LOG_SECURITY_INCIDENT
  → ESCALATE_TO = SECURITY_AGENT + GOVERNANCE_AGENT
  → RETRY_POLICY = NONE (security violation, no retry)

[F-03] SCHEMA DRIFT DETECTED:
  → HALT affected integration pipeline
  → LOG_SCHEMA_DRIFT_INCIDENT
  → EMIT SCHEMA_DRIFT_EVENT to governance topic
  → ESCALATE_TO = SCHEMA_GOVERNANCE_TEAM
  → RETRY_POLICY = Resume only after schema fix is deployed and validated

[F-04] ML MODEL UNAVAILABLE:
  → FALLBACK to Rules-Engine-only evaluation
  → FLAG confidence_score degradation in output
  → LOG_ML_UNAVAILABLE_INCIDENT
  → ESCALATE_TO = ML_OPS_TEAM
  → RETRY_POLICY = Retry ML invocation every 30s (max 3 attempts), then continue on rules engine

[F-05] CONFIDENCE BELOW THRESHOLD (< 0.60):
  → STOP_OUTPUT_EMISSION (do not emit degraded health signal)
  → LOG_LOW_CONFIDENCE_INCIDENT
  → ESCALATE_TO = OPS_TEAM
  → RETRY_POLICY = Await additional signals for 60s, re-evaluate once

[F-06] TENANT ISOLATION VIOLATION DETECTED:
  → IMMEDIATE HALT of affected service
  → LOG_SECURITY_INCIDENT (P0)
  → ESCALATE_TO = GOVERNANCE_AGENT + COMPLIANCE_ADMIN + SECURITY_LEAD
  → RETRY_POLICY = NONE (manual clearance required before resume)

[F-07] KAFKA BROKER UNAVAILABLE:
  → SWITCH to local in-memory buffer (max 10,000 signals, 5 min TTL)
  → LOG_KAFKA_UNAVAILABLE
  → ESCALATE_TO = INFRA_OPS_TEAM
  → RETRY_POLICY = Reconnect every 10s, flush buffer on reconnect

[F-08] EXTERNAL VENDOR PROBE FAILURE:
  → LOG_VENDOR_FAILURE
  → MARK vendor as DEGRADED in health status
  → EMIT VENDOR_DEGRADATION_EVENT
  → ESCALATE_TO = OPS_TEAM after 3 consecutive failures
  → RETRY_POLICY = Exponential backoff (10s, 30s, 60s), max 5 retries

[F-09] DATA CORRUPTION DETECTED (input hash mismatch):
  → REJECT_SIGNAL
  → LOG_DATA_INTEGRITY_INCIDENT (P0)
  → ESCALATE_TO = DATA_GOVERNANCE_AGENT + SECURITY_AGENT
  → RETRY_POLICY = NONE (manual investigation required)

[F-10] CIRCULAR DEPENDENCY DETECTED IN AGENT GRAPH:
  → HALT affected agent chain
  → LOG_CIRCULAR_DEPENDENCY_INCIDENT
  → ESCALATE_TO = ARCHITECTURE_GOVERNANCE_TEAM
  → RETRY_POLICY = NONE (architectural fix required)
```

---

## 1️⃣1️⃣ INTER-AGENT DEPENDENCY MAP

```
UPSTREAM_AGENTS:
  MANDATORY (agent halts if unavailable):
    - API_GATEWAY_SERVICE
    - KAFKA_BROKER_CLUSTER
    - TENANT_MANAGEMENT_SERVICE
    - SCHEMA_REGISTRY_SERVICE
    - AUDIT_LOG_AGENT

  OPTIONAL (degraded mode if unavailable):
    - ML_ANOMALY_DETECTION_MODEL
    - FEATURE_FLAG_SERVICE
    - DEPLOYMENT_AGENT
    - EXTERNAL_VENDOR_CONNECTOR_HUB

DOWNSTREAM_AGENTS:
  PRIMARY:
    - OBSERVABILITY_AGENT        (receives full health feed)
    - GOVERNANCE_AGENT           (receives breach escalations)
    - INCIDENT_MANAGEMENT_AGENT  (receives P0/P1 triggers)
    - AUDIT_LOG_AGENT            (receives immutable entries)

  SECONDARY:
    - NOTIFICATION_SERVICE       (triggers ops/admin alerts)
    - FEATURE_STORE_AGENT        (receives platform health feature vectors)
    - PUBLIC_STATUS_PAGE_SERVICE (receives sanitized public signals)
    - RANK_UPDATE_AGENT          (platform trust score input)

EVENT_TRIGGERS_EMITTED:
  - HEALTH_SIGNAL_PROCESSED
  - SLA_BREACH_DETECTED
  - SLA_WARNING_DETECTED
  - SCHEMA_DRIFT_DETECTED
  - TENANT_ISOLATION_VIOLATION
  - VENDOR_DEGRADATION_DETECTED
  - KAFKA_LAG_CRITICAL
  - ML_MODEL_DRIFT_DETECTED
  - AGENT_HEARTBEAT_MISSED
  - INTEGRATION_RESTORED (on recovery)
  - SECURITY_INCIDENT_DETECTED
```

---

## 1️⃣2️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent emits platform-level behavioral health features to the FEATURE_STORE_AGENT for use in downstream ML models (e.g., placement probability, user activity prediction, system reliability scoring).

```json
EMIT_FEATURE_VECTOR: {
  "user_id":       null,
  "entity_type":   "PLATFORM | SERVICE | TENANT | VENDOR",
  "entity_id":     "string (service_name or tenant_id or vendor_name)",
  "feature_name":  "string (e.g., api_error_rate_1h, kafka_lag_p95, vendor_latency_p99)",
  "feature_value": "float",
  "timestamp":     "ISO-8601 UTC",
  "source_agent":  "INTEGRATION_HEALTH_MONITOR_AGENT",
  "domain_track":  "string | PLATFORM_WIDE"
}
```

**Note:** No PII is emitted in feature vectors. All features are platform-infrastructure metrics only.

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

Where integration health directly affects user-facing platform trust and reliability indicators:

```
TRIGGERS_EMITTED:
  - PLATFORM_TRUST_SCORE_UPDATE → RANK_UPDATE_AGENT
    (emitted when overall platform health changes tier:
     HEALTHY → DEGRADED or DEGRADED → HEALTHY)

  - PUBLIC_HEALTH_STATUS_UPDATE → PUBLIC_STATUS_PAGE_SERVICE
    (sanitized, no tenant or service name details exposed externally)
    Format: { "status": "Operational | Degraded | Outage", "updated_at": "ISO-8601" }

XP_UPDATE_EVENT       = NOT APPLICABLE (infrastructure agent, no user XP)
SHARE_TRIGGER_EVENT   = NOT APPLICABLE
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

```
METRICS_EMITTED (to OBSERVABILITY_AGENT / Prometheus):

  ihma_signals_received_total          (counter, labels: tenant_id, domain_track, environment)
  ihma_signals_rejected_total          (counter, labels: rejection_reason)
  ihma_health_reports_emitted_total    (counter, labels: integration_status)
  ihma_sla_breaches_total              (counter, labels: service_name, sla_type)
  ihma_schema_drift_detected_total     (counter, labels: service_name)
  ihma_tenant_violations_total         (counter)
  ihma_processing_latency_ms           (histogram: p50, p95, p99)
  ihma_ml_model_invocations_total      (counter, labels: model_version, result)
  ihma_confidence_score_histogram      (histogram: buckets 0.1–1.0)
  ihma_kafka_lag_gauge                 (gauge, labels: topic, consumer_group)
  ihma_vendor_probe_latency_ms         (histogram, labels: vendor_name)
  ihma_vendor_availability_gauge       (gauge 0/1, labels: vendor_name)
  ihma_agent_heartbeat_miss_total      (counter, labels: source_agent_id)
  ihma_circuit_breaker_state           (gauge 0=closed/1=open, labels: target_service)
  ihma_escalations_emitted_total       (counter, labels: escalation_target, severity)

SUCCESS_RATE_TARGET    = > 99.9% signal processing success (production)
ERROR_RATE_THRESHOLD   = < 0.1% (breach triggers auto-alert)
LATENCY_SLA:
  Signal ingestion P99  = < 200ms
  Report generation P99 = < 500ms
DRIFT_MONITORING       = Weekly model drift check, immediate alert on KL divergence > 0.1
ANOMALY_FREQUENCY      = Tracked per service per hour; spike > 3x baseline = escalation

INTEGRATES_WITH:
  - OBSERVABILITY_AGENT (Prometheus + Grafana + Jaeger)
  - Alertmanager (PagerDuty / Opsgenie for P0/P1)
  - Distributed tracing (Jaeger trace injection on all health evaluations)
```

---

## 1️⃣5️⃣ SLA DEFINITIONS

```
INTEGRATION_SLA_THRESHOLDS:

Category: CORE MICROSERVICES (Lanes A–D)
  Latency SLA:       P99 < 500ms
  Error Rate SLA:    < 0.5%
  Availability SLA:  > 99.9%

Category: ML/AI SERVICES (Lane F)
  Latency SLA:       P99 < 2000ms
  Error Rate SLA:    < 1%
  Availability SLA:  > 99.5%

Category: KAFKA EVENT BUS
  Consumer Lag SLA:  < 500 messages (warning at 1000, critical at 10000)
  Topic Throughput:  No sustained drop > 20% from 15m baseline

Category: EXTERNAL VENDORS (Payment)
  Latency SLA:       P99 < 3000ms
  Success Rate SLA:  > 99.5%

Category: EXTERNAL VENDORS (Communication — Email/SMS)
  API Latency SLA:   P99 < 1000ms
  Delivery Rate SLA: > 98%

Category: DATABASE (PostgreSQL)
  Query Latency SLA: P99 < 100ms
  Replication Lag:   < 500ms (warning at 1s, critical at 5s)
  Connection Pool:   < 80% utilization (warning at 90%, critical at 95%)

Category: SEARCH (Elasticsearch)
  Query Latency SLA: P99 < 200ms
  Index Health:      GREEN (YELLOW = warning, RED = critical halt)

Category: CACHE (Redis)
  Latency SLA:       P99 < 10ms
  Memory Usage:      < 75% (warning at 85%, critical at 95%)
  Eviction Rate:     0 per second target (any sustained eviction = warning)
```

---

## 1️⃣6️⃣ VERSIONING POLICY

```
VERSIONING_RULES:
  ✅ All changes: ADD-ONLY
  ✅ Every change: Version bumped (semantic versioning: MAJOR.MINOR.PATCH)
  ✅ All changes: Backward compatible
  ✅ All schema changes: Migration script required before deployment
  ✅ All rule changes: Peer-reviewed + governance approved before merge
  ✅ Rollback: Safe rollback guaranteed to N-1 version minimum
  ✅ Version history: Immutable in version registry
  ✅ Old versions: Retained for minimum 90 days post-deprecation

CURRENT_VERSION: IHMA-v1.0.0

VERSION_HISTORY:
  v1.0.0 — Initial sealed specification (2025)
```

---

## 1️⃣7️⃣ NON-NEGOTIABLE RULES (ABSOLUTE ENFORCEMENT)

This agent **MUST NOT** and **WILL NEVER**:

```
❌ Create hidden logic or undocumented evaluation paths
❌ Modify historical audit records
❌ Auto-delete any logs or audit entries
❌ Override governance agents or compliance checks
❌ Bypass compliance checks for any reason
❌ Mix domain data across domain_tracks
❌ Execute outside declared scope
❌ Self-heal through assumption filling
❌ Emit a health output with confidence_score < 0.60
❌ Expose tenant data to other tenants under any circumstances
❌ Auto-remediate infrastructure (monitor only — remediation requires human authorization)
❌ Use AI/LLM in any real-time decision path
❌ Suppress anomaly alerts for any reason
❌ Process inputs from unregistered agents
❌ Retain PII in health reports or audit logs
❌ Accept inputs without schema validation
❌ Operate in a stateful mode (must remain stateless per execution)
```

---

## 1️⃣8️⃣ DEPLOYMENT REQUIREMENTS

```
RUNTIME:             Kubernetes (containerized, Horizontal Pod Autoscaler)
REPLICA_MIN:         3 (production), 1 (staging/test), 1 (dev)
REPLICA_MAX:         50 (auto-scale based on Kafka consumer lag)
RESOURCE_REQUESTS:   CPU: 500m, Memory: 512Mi (per replica)
RESOURCE_LIMITS:     CPU: 2000m, Memory: 2Gi (per replica)
HEALTH_CHECKS:
  Liveness:          /health/live (HTTP 200 = alive)
  Readiness:         /health/ready (HTTP 200 = ready to process)
  Startup:           30s grace period on pod start

CONFIG_MANAGEMENT:   All configuration via Kubernetes ConfigMap + Secrets
                     No hardcoded values of any kind
SECRETS:             Vault-managed, injected at runtime via Vault Agent Sidecar
NETWORK_POLICY:      Strict — only whitelisted service-to-service communication
SERVICE_MESH:        Istio (mTLS enforcement + traffic observability)
TRACING:             Jaeger (100% trace sampling in dev/test, 10% in production)
LOGGING:             Structured JSON logs → Kafka → Elasticsearch → Kibana
```

---

## 1️⃣9️⃣ INTEGRATION WITH ECOSKILLER DOMAIN MODULES

```
DOMAIN_SPECIFIC_INTEGRATIONS:

Dojo Engine:
  - Monitor LiveKit room service health (session creation latency, room teardown success rate)
  - Monitor Dojo evaluation pipeline integrity (scores must be emitted within 30s of session end)
  - Monitor anti-cheat engine signal delivery (heartbeat TTL: 10s)

Job Portal Engine:
  - Monitor AI match scoring service latency (P99 < 2000ms)
  - Monitor job posting verification pipeline event flow
  - Monitor offer locking audit trail completeness

Skill Development Engine:
  - Monitor skill gap analysis model availability
  - Monitor learning path recommendation event delivery
  - Monitor EIE/HIA (Intelligence Detection Engine) feature emission

Project Execution Engine:
  - Monitor milestone evaluation pipeline health
  - Monitor portfolio auto-generation service event completeness

ERP Layer:
  - Monitor institute ERP sync health (event delivery confirmation)
  - Monitor corporate hiring workflow event integrity
  - Monitor compliance audit ERP pipeline continuity

Gamification Engine:
  - Monitor XP/point update event delivery (TTL: 5s from activity event)
  - Monitor badge award pipeline event integrity
  - Monitor belt progression event completeness

Billing & Payments:
  - Monitor payment gateway probe (synthetic test, non-PII)
  - Monitor billing ledger event delivery confirmation
  - Monitor subscription state change event integrity

Notification Service:
  - Monitor notification dispatch queue lag
  - Monitor multi-channel delivery success rates
    (push, email, SMS, in-app)
```

---

## 2️⃣0️⃣ PUBLIC STATUS PAGE CONTRACT

This agent is the authoritative source for **status.ecoskiller.com**. The public signal must be sanitized — no internal service names, no tenant data, no error details.

```json
PUBLIC_HEALTH_SIGNAL: {
  "platform_status": "Operational | Degraded | Partial Outage | Major Outage",
  "components": [
    { "name": "Job Portal",         "status": "Operational | Degraded | Outage" },
    { "name": "Skill Engine",       "status": "Operational | Degraded | Outage" },
    { "name": "Dojo Sessions",      "status": "Operational | Degraded | Outage" },
    { "name": "Project Platform",   "status": "Operational | Degraded | Outage" },
    { "name": "ERP Services",       "status": "Operational | Degraded | Outage" },
    { "name": "Authentication",     "status": "Operational | Degraded | Outage" },
    { "name": "Notifications",      "status": "Operational | Degraded | Outage" },
    { "name": "Payments",           "status": "Operational | Degraded | Outage" }
  ],
  "last_updated_utc": "ISO-8601",
  "message": "string | null (human-readable, no technical details)"
}
```

**Update frequency:** Real-time (within 60 seconds of status change detection)  
**Source authority:** IHMA only (no other agent may write to public status)

---

## 🔐 AGENT SEAL DECLARATION

```
╔══════════════════════════════════════════════════════════════════╗
║        INTEGRATION_HEALTH_MONITOR_AGENT — SEALED                ║
║        Platform: Ecoskiller Antigravity                         ║
║        Version:  IHMA-v1.0.0                                    ║
║        Status:   FINAL · LOCKED · GOVERNED                      ║
║                                                                  ║
║  This specification is COMPLETE and SEALED.                     ║
║  No interpretation beyond declared scope.                       ║
║  No assumption filling.                                         ║
║  No creative deviation.                                         ║
║  No mutation without version bump + governance approval.        ║
║                                                                  ║
║  Any deviation from this specification =                        ║
║  STOP EXECUTION + GOVERNANCE ESCALATION                         ║
╚══════════════════════════════════════════════════════════════════╝
```

---

*Document generated under Ecoskiller Antigravity Master Agent Creation Framework. All sections comply with the Master Agent Creation Prompt v1.0. Mutation policy: Add-only. Authority: Human declaration only.*
