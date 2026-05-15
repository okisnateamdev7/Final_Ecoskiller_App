# 🔒 NETWORK_HEALTH_MONITOR_AGENT
## ECOSKILLER — ANTIGRAVITY AGENT SPECIFICATION
### Status: SEALED · LOCKED · GOVERNED · DETERMINISTIC
### Artifact Class: Production Agent Blueprint
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only

---

## ⚠️ SEAL DECLARATION

```
This document is SEALED.
No ambiguity is permitted.
No inference is permitted.
No implicit behavior is permitted.
No autonomous remediation is permitted without human gate.
Every rule executes exactly as written.
Deviation = STOP EXECUTION → REPORT VIOLATION → NO PARTIAL OUTPUT.

HIGH AVAILABILITY MODE           = ENABLED
NO SINGLE POINT OF FAILURE       = TRUE
MULTI-ZONE DEPLOYMENT            = REQUIRED
AUTO FAILOVER & SELF-HEALING     = ACTIVE
DOMAIN & TENANT ISOLATION        = HARD
ZERO-DOWNTIME DEPLOYMENTS        = MANDATORY
REAL-TIME HEALTH MONITORING      = ENABLED
SLA/SLO TRACKING                 = ENFORCED
RATE LIMITING MODE               = ENABLED
ALL ENDPOINTS                    = LIMITED
ADD-ONLY MUTATION LAW            = ACTIVE
```

---

# SECTION I — AGENT IDENTITY

```
Agent Name:         NETWORK_HEALTH_MONITOR_AGENT (NHMA)
Agent Class:        Antigravity Intelligence Agent
Parent System:      ECOSKILLER — Unified Job + Skill + Project + Education SaaS
Agent Type:         Real-Time Observer + SLO Enforcer + Incident Classifier
Execution Mode:     Deterministic, Continuous, Event-Driven, Blast-Domain-Aware
Human Gate:         MANDATORY for any topology change, failover initiation, or
                    remediation beyond defined auto-healing bounds
AI Autonomy:        ZERO over topology changes, failover decisions beyond
                    defined thresholds, or security incident response
```

**Agent Mission:**

Continuously observe, classify, and report the network and infrastructure health
of the entire Ecoskiller platform — across all blast domains, all four traffic
classes, all media stacks (Jitsi / LiveKit / coturn / WebRTC), all Kubernetes
namespaces, and all ingress / gateway layers — against declared SLO targets from
R49 and R50. Detect degradation before SLA breach. Classify every failure by
severity. Enforce the declared feature-shedding order during degradation.
Alert the correct role with the correct severity. Never auto-heal beyond
defined safe bounds. Never collapse blast domain isolation.

---

# SECTION II — BLAST DOMAIN MAP (LOCKED)

NHMA observes health independently per blast domain.
Saturation or failure in one domain must never degrade another.
Cross-domain health signal mixing is FORBIDDEN.

## 2.1 Declared Blast Domains

| Domain ID | Domain Name | Services Contained | SLO Tier |
|---|---|---|---|
| `BD-CORE` | Ecoskiller Core | Jobs, Skills, Projects, Marketplace, Institutions, Companies, Billing, Payments | 99.9% |
| `BD-DOJO` | Dojo SaaS (Realtime) | Matches, GD Rooms, Scoring, Belts, Certifications, Replays, Mentor Controls, Live Video | 99.95% |
| `BD-TRUST` | Shared Trust Layer | Identity (Keycloak), RBAC (OPA), Tenancy, Audit Logs, etcd | 99.99% |
| `BD-MEDIA` | Media Stack | Jitsi, Jitsi Videobridge, Jicofo, LiveKit, coturn | 99.95% |
| `BD-OPS` | Platform Operations | Prometheus, Grafana, Loki, OpenTelemetry, Wazuh, Velero, CI/CD | Best-effort |
| `BD-SOCIETY` | Society / Offline | Society Batch Layer, CouchDB sync nodes, Temporal workflows | 99.5% |

## 2.2 Kubernetes Namespace Binding (Locked)

| Namespace | Blast Domain | NHMA Watch Scope |
|---|---|---|
| `core` | BD-CORE | Auth, Users, Jobs, Applications |
| `realtime` | BD-DOJO | GD Orchestrator, Dojo Match Engine, Interview Service |
| `media` | BD-MEDIA | Jitsi, LiveKit, coturn |
| `billing` | BD-CORE | Billing & Subscription Service |
| `analytics` | BD-CORE | Analytics Service, ClickHouse |
| `admin` | BD-CORE | Admin Governance Service |
| `ops` | BD-OPS | Prometheus, Grafana, Loki, Wazuh, Velero |
| `society` | BD-SOCIETY | Society microservices, CouchDB, Temporal |

---

# SECTION III — TRAFFIC CLASS DEFINITIONS (LOCKED)

All network health signals must be classified against one of four declared
traffic classes. No traffic class may be merged with another.

| Class | Traffic Type | Examples | SLO Priority |
|---|---|---|---|
| `TC-1` | HTTP APIs (CRUD, queries) | Job CRUD, profile API, application API | HIGH |
| `TC-2` | WebSocket commands | GD mute/unmute, dojo match events, interview timer | CRITICAL |
| `TC-3` | Media (WebRTC / Jitsi / LiveKit) | Audio streams, video streams — NEVER through backend | PROTECTED |
| `TC-4` | Async events (Kafka / RabbitMQ) | Analytics, scoring, billing events | BACKGROUND |

```
RULE: Media (TC-3) NEVER touches backend API layer.
RULE: Backend only issues short-lived tokens for media rooms.
RULE: Rooms = session_id / match_id — no exceptions.
RULE: TC-2 control plane must NEVER be blocked by TC-3 media traffic.
```

---

# SECTION IV — SLO TARGETS (HARD LOCK — FROM R49 / R50 / HA-B)

NHMA enforces the following targets. Violation = SLO BREACH event emitted.

## 4.1 API & Service Latency SLOs (P95 — Hard Lock, R49.3)

| Component | P95 Latency Target | Blast Domain |
|---|---|---|
| Auth APIs (Keycloak) | ≤ 200 ms | BD-TRUST |
| Ecoskiller Core APIs | ≤ 300 ms | BD-CORE |
| Dojo Discussion APIs | ≤ 250 ms | BD-DOJO |
| Mentor Command Signals | ≤ 150 ms | BD-DOJO |
| Student Dashboards | ≤ 300 ms | BD-CORE / BD-DOJO |
| Admin Console | ≤ 500 ms | BD-CORE |
| Search (OpenSearch) P95 | ≤ 300 ms | BD-CORE |
| Search Faceted | ≤ 400 ms | BD-CORE |
| Event Processing P95 (Kafka) | ≤ 500 ms | BD-CORE / BD-DOJO |
| WebSocket Command Round-Trip | ≤ 100 ms | BD-DOJO |

## 4.2 Realtime & Media SLOs (R49.5)

| Metric | Target | Blast Domain |
|---|---|---|
| Room Join Time | ≤ 2 s | BD-MEDIA |
| Audio Latency (end-to-end) | ≤ 150 ms | BD-MEDIA |
| Video Latency (end-to-end) | ≤ 300 ms | BD-MEDIA |
| Mentor Control Signal | ≤ 100 ms | BD-DOJO + BD-MEDIA |
| Reconnect Recovery | ≤ 3 s | BD-MEDIA |
| Message Delivery Reliability (Dojo) | ≥ 99.99% | BD-DOJO |

## 4.3 WebRTC Bandwidth Baselines (Per Participant)

| Direction | Voice GD (Jitsi) | Dojo / Interview (LiveKit) |
|---|---|---|
| Upload per user | 40–60 kbps | 60–150 kbps |
| Download per user (5–6 participants) | 150–250 kbps | 200–400 kbps |
| Bandwidth downgrade behavior | Must preserve scoring integrity | Must preserve mentor control signals |

## 4.4 Availability SLOs (HA-B)

| Platform | Monthly Target | Blast Domain |
|---|---|---|
| Ecoskiller Core | ≥ 99.9% | BD-CORE |
| Dojo Real-Time Services | ≥ 99.95% | BD-DOJO + BD-MEDIA |
| Auth & Critical APIs | ≥ 99.99% | BD-TRUST |
| Society / Offline Sync | ≥ 99.5% | BD-SOCIETY |

## 4.5 Incident Response Time Targets

| Milestone | Target |
|---|---|
| Incident detection | ≤ 1 minute |
| Acknowledgement | ≤ 5 minutes |
| Student-impact mitigation | ≤ 10 minutes |
| Full recovery (P95) | ≤ 60 minutes |

---

# SECTION V — SIGNAL CATALOG (LOCKED)

NHMA operates exclusively on declared signals.
No signal outside this catalog may be consumed or acted upon.

## 5.1 Prometheus Metrics Consumed (Locked)

### API & Service Layer

```
http_request_duration_seconds{service, route, status_code, namespace}
http_requests_total{service, method, status_code}
http_errors_total{service, error_type}
grpc_server_handling_seconds{service, grpc_method}
websocket_message_latency_seconds{service, channel}
websocket_connections_active{service}
websocket_connection_errors_total{service}
```

### Media Stack (Jitsi / LiveKit / coturn)

```
jitsi_videobridge_participants_total
jitsi_videobridge_conferences_total
jitsi_videobridge_packet_rate_received
jitsi_videobridge_packet_loss_ratio
jitsi_videobridge_rtt_aggregate_ms
jitsi_videobridge_bitrate_download
jitsi_videobridge_bitrate_upload
jitsi_jicofo_conferences_created_total
jitsi_jicofo_conferences_failed_total
livekit_room_active
livekit_room_participants_total
livekit_packet_loss_ratio{room_id, participant_id}
livekit_rtt_ms{room_id}
livekit_bitrate_kbps{direction, room_id}
coturn_active_allocations
coturn_allocation_errors_total
coturn_relay_bytes_total
coturn_nat_traversal_success_ratio
```

### Kubernetes Infrastructure

```
kube_pod_status_phase{namespace, pod}
kube_pod_container_status_restarts_total{namespace, pod}
kube_deployment_status_replicas_unavailable{namespace, deployment}
kube_node_status_condition{node, condition}
kube_node_cpu_usage_ratio{node}
kube_node_memory_usage_ratio{node}
container_cpu_usage_seconds_total{namespace, pod}
container_memory_working_set_bytes{namespace, pod}
kube_hpa_status_current_replicas{namespace, hpa}
kube_hpa_status_desired_replicas{namespace, hpa}
```

### Data Layer (PostgreSQL / Redis / ClickHouse / OpenSearch)

```
pg_stat_replication_lag_seconds{host}
pg_slow_queries_total{host}
pg_connections_active{host}
pg_connections_max{host}
redis_connected_clients{host}
redis_rejected_connections_total{host}
redis_keyspace_hits_ratio{host}
redis_memory_usage_bytes{host}
clickhouse_query_duration_ms{query_type}
clickhouse_insert_rate{table}
opensearch_search_latency_ms{index}
opensearch_indexing_lag_seconds{index}
```

### Ingress / Gateway Layer (NGINX / Kong / Envoy)

```
nginx_ingress_requests_total{ingress, status}
nginx_ingress_request_duration_seconds{ingress}
kong_http_requests_total{service, route, status_code}
kong_latency_ms{type, service}
kong_bandwidth_bytes{direction, service}
envoy_cluster_upstream_rq_total{cluster}
envoy_cluster_upstream_rq_pending_overflow
envoy_cluster_circuit_breaker_open{cluster}
```

### Security (Wazuh / ModSecurity)

```
wazuh_alerts_total{level, rule_id}
wazuh_events_processed_total
modsecurity_blocked_requests_total{rule_id}
ddos_traffic_volume_bytes{source_ip_bucket}
ddos_mitigation_active{zone}
```

### Event Bus (Kafka)

```
kafka_consumer_group_lag{group, topic, partition}
kafka_producer_request_rate{topic}
kafka_topic_messages_in_rate{topic}
kafka_broker_network_io_bytes{direction}
```

### Society / Offline Sync (CouchDB / Temporal)

```
couchdb_httpd_requests_total{method}
couchdb_replication_changes_pending{source, target}
couchdb_sync_lag_seconds{node_id}
temporal_workflow_active_count
temporal_workflow_failed_total
temporal_task_queue_backlog{task_queue}
```

## 5.2 Loki Log Signal Patterns (Locked)

```
Pattern: "circuit breaker OPEN"         → CIRCUIT_BREAKER_OPEN event
Pattern: "connection refused"            → SERVICE_UNREACHABLE event
Pattern: "upstream timeout"             → UPSTREAM_TIMEOUT event
Pattern: "TLS handshake failed"         → TLS_FAILURE event
Pattern: "TURN allocation failed"        → COTURN_FAILURE event
Pattern: "ICE connection failed"         → WEBRTC_ICE_FAILURE event
Pattern: "SFU participant disconnect"    → MEDIA_PARTICIPANT_DROP event
Pattern: "Jicofo conference failed"      → GD_ROOM_CREATION_FAILURE event
Pattern: "Redis connection lost"         → STATE_MACHINE_RISK event
Pattern: "etcd leader election"          → TRUST_LAYER_INSTABILITY event
Pattern: "pod OOMKilled"                 → RESOURCE_EXHAUSTION event
Pattern: "CrashLoopBackOff"             → SERVICE_CRASH_LOOP event
Pattern: "CouchDB sync interrupted"      → OFFLINE_SYNC_INTERRUPTED event
Pattern: "Temporal workflow timeout"     → TEMPORAL_WORKFLOW_FAILURE event
```

## 5.3 Canonical Health Signal Envelope (Sealed)

```json
{
  "signal_id":        "uuid-v4",
  "signal_type":      "METRIC | LOG_PATTERN | SYNTHETIC_PROBE",
  "blast_domain":     "BD-CORE | BD-DOJO | BD-TRUST | BD-MEDIA | BD-OPS | BD-SOCIETY",
  "traffic_class":    "TC-1 | TC-2 | TC-3 | TC-4",
  "namespace":        "core | realtime | media | billing | analytics | admin | ops | society",
  "component":        "string (service or infra component name)",
  "metric_name":      "string (from catalog above)",
  "metric_value":     "numeric",
  "threshold":        "numeric (from SLO table)",
  "breach":           "boolean",
  "severity":         "INFO | WARNING | CRITICAL | FATAL",
  "timestamp_utc":    "ISO-8601",
  "schema_version":   "1.0"
}
```

Schema violations → signal discarded → dead-letter → alert raised.

---

# SECTION VI — HEALTH CHECK DEFINITIONS (LOCKED)

## 6.1 Active Synthetic Probes (Run by NHMA)

| Probe Name | Target | Frequency | SLO Target | Blast Domain |
|---|---|---|---|---|
| `probe_auth_api` | `POST /auth/login` (test user) | 30 s | ≤ 200 ms, HTTP 200 | BD-TRUST |
| `probe_core_api` | `GET /health` (core namespace) | 30 s | ≤ 300 ms, HTTP 200 | BD-CORE |
| `probe_dojo_api` | `GET /dojo/health` | 30 s | ≤ 250 ms, HTTP 200 | BD-DOJO |
| `probe_websocket` | WebSocket connect + ping/pong | 60 s | ≤ 100 ms round-trip | BD-DOJO |
| `probe_jitsi` | Jitsi health endpoint | 30 s | HTTP 200, ≤ 2 s join | BD-MEDIA |
| `probe_livekit` | LiveKit health endpoint | 30 s | HTTP 200 | BD-MEDIA |
| `probe_coturn` | STUN binding request | 60 s | Response ≤ 500 ms | BD-MEDIA |
| `probe_kafka` | Producer + consumer round-trip | 60 s | ≤ 500 ms | BD-CORE |
| `probe_postgres` | `SELECT 1` on primary | 15 s | ≤ 50 ms | BD-TRUST |
| `probe_redis` | `PING` on primary | 15 s | ≤ 10 ms | BD-DOJO |
| `probe_opensearch` | `_cluster/health` | 60 s | Status: green/yellow | BD-CORE |
| `probe_clickhouse` | `SELECT 1` | 60 s | ≤ 100 ms | BD-CORE |
| `probe_ingress_tls` | TLS cert expiry check | 1 h | Expiry > 14 days | BD-TRUST |
| `probe_couchdb_sync` | Replication status check | 5 min | Lag ≤ 300 s | BD-SOCIETY |
| `probe_temporal` | Workflow health endpoint | 5 min | Active workers > 0 | BD-SOCIETY |

## 6.2 Passive Health Signals (Consumed from Prometheus / Loki)

All metrics listed in Section V consumed continuously.
Polling interval: 10 seconds for CRITICAL components, 60 seconds for background.

---

# SECTION VII — ALERT CLASSIFICATION (IMMUTABLE)

## 7.1 Severity Levels (Locked)

| Severity | Definition | Max Time-to-Alert | Auto-Action Permitted |
|---|---|---|---|
| `FATAL` | Student-facing service down, media stack down, auth down, data corruption risk | ≤ 1 min | Auto-restart pod only |
| `CRITICAL` | SLO breach active, circuit breaker open, blast domain degraded, scoring integrity at risk | ≤ 2 min | Auto-restart pod only |
| `WARNING` | SLO approaching breach (>80% of threshold), queue depth growing, reconnect rate elevated | ≤ 5 min | None — alert only |
| `INFO` | Scheduled maintenance window, scaling event, non-impacting probe miss | ≤ 15 min | None — log only |

## 7.2 Alert Routing Matrix (Locked)

| Alert Code | Severity | Notified Role(s) | Channel |
|---|---|---|---|
| `AUTH_API_DOWN` | FATAL | Platform Super Admin, Security Admin | PagerDuty + Slack |
| `GD_ROOM_CREATION_FAILURE` | FATAL | Platform Ops Lead, DevOps Admin | PagerDuty + Slack |
| `MEDIA_STACK_DOWN` | FATAL | Platform Ops Lead, DevOps Admin | PagerDuty + Slack |
| `DB_REPLICATION_FAILED` | FATAL | Database Admin, Platform Super Admin | PagerDuty + Slack |
| `REDIS_UNAVAILABLE` | FATAL | DevOps Admin, Platform Ops Lead | PagerDuty + Slack |
| `ETCD_INSTABILITY` | FATAL | Platform Super Admin, DevOps Admin | PagerDuty + SMS |
| `SLO_BREACH_CORE` | CRITICAL | Platform Ops Lead | PagerDuty + Slack |
| `SLO_BREACH_DOJO` | CRITICAL | Platform Ops Lead, Dojo Service Owner | PagerDuty + Slack |
| `SLO_BREACH_AUTH` | CRITICAL | Security Admin, Platform Super Admin | PagerDuty + Slack |
| `CIRCUIT_BREAKER_OPEN` | CRITICAL | DevOps Admin | Slack |
| `BLAST_DOMAIN_ISOLATION_BREACH` | CRITICAL | Platform Super Admin | PagerDuty + Slack |
| `COTURN_FAILURE` | CRITICAL | DevOps Admin, Media Stack Owner | Slack |
| `WEBRTC_ICE_FAILURE_SPIKE` | CRITICAL | DevOps Admin | Slack |
| `KAFKA_CONSUMER_LAG_HIGH` | WARNING | DevOps Admin | Slack |
| `POD_CRASH_LOOP` | CRITICAL | DevOps Admin | Slack |
| `NODE_CPU_SATURATION` | WARNING | DevOps Admin | Slack |
| `TLS_CERT_EXPIRY_APPROACHING` | WARNING | DevOps Admin, Security Admin | Email |
| `WAZUH_HIGH_SEVERITY_ALERT` | CRITICAL | Security Admin, CISO | Slack + Email |
| `DDOS_MITIGATION_ACTIVE` | CRITICAL | Security Admin, Platform Ops Lead | PagerDuty + Slack |
| `MODSECURITY_RULE_SPIKE` | WARNING | Security Admin | Slack |
| `COUCHDB_SYNC_LAG_HIGH` | WARNING | Society Coordinator, DevOps Admin | Slack |
| `TEMPORAL_WORKFLOW_FAILURE` | WARNING | Finance Admin, DevOps Admin | Slack |
| `OFFLINE_SYNC_INTERRUPTED` | WARNING | Society Coordinator | Slack + SMS |
| `HPA_SCALE_LIMIT_REACHED` | WARNING | DevOps Admin, Finance Admin | Slack |
| `COST_ENVELOPE_APPROACHING` | WARNING | Finance Admin | Email |

All alerts emit to: `ecoskiller.nhma.alert.*` Kafka topic (for audit trail).
All alerts written to: Loki (immutable, time-ordered, tenant-aware).
All alerts reference: `signal_id` from originating signal envelope.

---

# SECTION VIII — FEATURE SHEDDING ORDER (LOCKED — R49.11 / R50.11)

When SLOs are breached or blast domain capacity is exhausted,
the following degradation order is deterministic and mandatory.
No deviation permitted. No AI discretion.

## 8.1 Shedding Order (Execute Top-to-Bottom)

```
Order 1 — SHED FIRST:
  - Analytics & reports (ClickHouse writes, Grafana dashboards)
  - Recommendation engine calls
  - Search facets (reduce to basic search only)
  - Background indexing jobs (Airflow DAGs)
  - Non-critical notification digests

Order 2 — SHED SECOND:
  - Admin console operations (throttled, not blocked)
  - Batch export jobs (queued, not dropped)
  - SEO page regeneration (static cache served)

Order 3 — SHED THIRD:
  - Non-realtime analytics APIs
  - Profile enrichment AI calls
  - Non-critical webhook deliveries

PROTECTED — NEVER SHED:
  ✔ Student discussions (GD rooms, Dojo matches)
  ✔ Mentor controls and commands
  ✔ Scoring and assessments
  ✔ Authentication (Keycloak)
  ✔ RBAC enforcement (OPA)
  ✔ Audit logging
  ✔ Active session state (Redis GD state machines)
  ✔ Live session WebSocket command channels
  ✔ Media room token issuance
```

## 8.2 Shedding Trigger Conditions

```
Trigger: Any CRITICAL SLO breach sustained > 30 seconds
Action:  NHMA emits ecoskiller.nhma.shedding_required event
         with recommended shedding_order_step (1, 2, or 3)
         Human Ops Lead MUST confirm within 5 minutes
         If unconfirmed: NHMA escalates to FATAL + PagerDuty

Trigger: Blast domain resource saturation > 90% CPU or memory
Action:  NHMA emits WARNING, starts shedding_readiness_check
         HPA scale-out evaluated first before shedding recommended

Trigger: BD-DOJO or BD-MEDIA FATAL event
Action:  NHMA immediately emits shedding_order_step = PROTECTED_ONLY
         No analytics, no admin, no background jobs
         Student discussions protected at all costs
```

---

# SECTION IX — MEDIA STACK HEALTH RULES (LOCKED)

The media stack is the highest-sensitivity layer of the platform.
NHMA monitors it with dedicated rules derived from the Dojo architecture.

## 9.1 Jitsi / Jitsi Videobridge Health

```
Healthy State:
  - jitsi_jicofo_conferences_failed_total: 0 delta in 60s window
  - jitsi_videobridge_packet_loss_ratio: < 2%
  - jitsi_videobridge_rtt_aggregate_ms: < 150 ms
  - jitsi_videobridge_bitrate_upload: within 40–60 kbps per participant

Degraded State (WARNING):
  - packet_loss_ratio: 2–5%
  - rtt_aggregate_ms: 150–300 ms

Failed State (CRITICAL → FATAL):
  - packet_loss_ratio: > 5%
  - conference_failed_total: delta > 0
  - no active videobridge pods in media namespace

NHMA Response on FATAL:
  - Emit GD_ROOM_CREATION_FAILURE
  - Alert: Platform Ops Lead + DevOps Admin
  - Log: full signal_envelope to audit store
  - DO NOT auto-terminate active sessions
  - Active sessions shed gracefully via GD Orchestrator FSM
```

## 9.2 LiveKit SFU Health

```
Healthy State:
  - livekit_room_active: > 0 (if sessions scheduled)
  - livekit_packet_loss_ratio: < 2%
  - livekit_rtt_ms: < 150 ms

Rules:
  - SFU pools must autoscale by participant count (R50.10)
  - Media scale is isolated from scoring and control plane
  - Replay processing is asynchronous — never blocks live sessions
  - Any global video bottleneck → MEDIA_STACK_DOWN → SYSTEM INVALIDATED
```

## 9.3 coturn TURN/STUN Health

```
Healthy State:
  - coturn_nat_traversal_success_ratio: > 95%
  - coturn_allocation_errors_total: 0 delta in 5 min window
  - coturn_active_allocations: within configured pool capacity

Degraded (WARNING):
  - nat_traversal_success_ratio: 85–95%

Failed (CRITICAL):
  - nat_traversal_success_ratio: < 85%
  - allocation_errors: sustained delta > 0

Context:
  - coturn enables NAT traversal for mobile and restricted networks
  - Rural zone users (BD-SOCIETY) are most dependent on coturn
  - coturn failure must trigger COTURN_FAILURE alert immediately
  - Fallback behavior: NHMA recommends relay pool expansion
    (Human DevOps Admin must execute — NHMA does not auto-provision)
```

## 9.4 WebRTC ICE / NAT Health

```
Signals:
  - ICE_FAILURE events from Loki log pattern matching
  - STUN binding probe failure from probe_coturn synthetic probe

Rules:
  - ICE failure rate > 2% of new connection attempts in 5 min window
    → WEBRTC_ICE_FAILURE_SPIKE event → WARNING
  - ICE failure rate > 10%
    → CRITICAL → alert DevOps Admin
  - ICE failure correlated with coturn_allocation_errors
    → Root cause = coturn pool exhaustion → COTURN_FAILURE

Bandwidth Degradation Rule:
  - If upload per participant drops below 25 kbps sustained > 60 s:
    → NHMA emits BANDWIDTH_DEGRADATION_DETECTED
    → Scoring Integrity Flag raised (from R49.5: "bandwidth downgrade
       must preserve scoring integrity")
    → Alert: GD Orchestrator + Platform Ops Lead
```

---

# SECTION X — INGRESS & GATEWAY HEALTH RULES (LOCKED)

## 10.1 NGINX Ingress

```
Healthy: error_rate < 1% of total requests (5xx)
WARNING: error_rate 1–5%
CRITICAL: error_rate > 5% sustained > 60 s
FATAL: ingress not responding (probe failure)

TLS Certificate Rule:
  - expiry < 14 days → WARNING → alert Security Admin + DevOps Admin
  - expiry < 3 days  → CRITICAL → immediate escalation
  - expired cert     → FATAL   → TLS_FAILURE + PagerDuty
```

## 10.2 Kong API Gateway

```
Healthy: kong_latency_ms (upstream) P95 within declared SLO per route
WARNING: any route P95 exceeds SLO by > 50%
CRITICAL: any route P95 exceeds SLO by > 100%

Rate Limit Health:
  - kong_bandwidth_bytes monitored per tenant per route
  - Sudden bandwidth spike > 10× baseline in 60 s window
    → DDoS signal → NHMA emits DDOS_TRAFFIC_ANOMALY
    → Wazuh correlation check triggered
```

## 10.3 Envoy Circuit Breaker

```
Signal: envoy_cluster_circuit_breaker_open = 1 for any cluster

NHMA Rule:
  - Circuit breaker open → CIRCUIT_BREAKER_OPEN → CRITICAL alert
  - Context: Envoy provides retries and circuit breaking (locked tech stack)
  - NHMA does NOT reset circuit breakers autonomously
  - Alert: DevOps Admin
  - Human must investigate before reset
  - If open > 10 minutes without resolution: escalate to FATAL
```

---

# SECTION XI — SECURITY SIGNAL INTEGRATION (LOCKED)

## 11.1 Wazuh SIEM Integration

```
Wazuh is the SIEM and intrusion detection system (locked tech stack).
NHMA consumes Wazuh alert metrics only — it does not duplicate
Wazuh's intrusion detection logic.

Signals consumed:
  - wazuh_alerts_total{level="critical"} — any non-zero delta → FATAL
  - wazuh_alerts_total{level="high"}    — any non-zero delta → CRITICAL
  - wazuh_alerts_total{level="medium"}  — rate > 10/min      → WARNING

NHMA Response:
  - Emit WAZUH_HIGH_SEVERITY_ALERT event with wazuh rule_id
  - Alert: Security Admin + CISO
  - DO NOT attempt automated remediation
  - Security Admin owns all response actions
```

## 11.2 ModSecurity WAF Health

```
Signal: modsecurity_blocked_requests_total
  - Rate > 100 blocks/min → WARNING → alert Security Admin
  - Rate > 1000 blocks/min → CRITICAL → possible DDoS or attack campaign

NHMA does not modify ModSecurity rules.
All rule changes require Security Admin authorization.
```

## 11.3 DDoS Detection

```
NHMA DDoS detection scope:
  - Traffic volume anomaly (10× baseline in 60 s window per source bucket)
  - Rate: ddos_traffic_volume_bytes spike pattern
  - Correlation with: modsecurity_blocked_requests spike + kong bandwidth spike

On detection:
  NHMA emits: DDOS_TRAFFIC_ANOMALY event
  Severity:   CRITICAL
  Alert:      Security Admin + Platform Ops Lead via PagerDuty + Slack
  Action:     NHMA does not auto-block IP ranges
              Security Admin executes ModSecurity / Wazuh response
              Progressive throttling recommendation emitted as advisory only
```

---

# SECTION XII — HIGH AVAILABILITY & FAILURE MODE RULES (LOCKED)

## 12.1 Node & Pod Health

```
Pod Restart Rules:
  - Any pod restart count > 3 in 5 min window → WARNING
  - CrashLoopBackOff detected → CRITICAL → POD_CRASH_LOOP
  - OOMKilled detected → CRITICAL → RESOURCE_EXHAUSTION

NHMA Auto-Action Permitted:
  - Emit alert and Kafka event
  - Log to Loki audit store
  - That is all — no auto-scaling, no pod deletion

Human Action Required:
  - DevOps Admin investigates and resolves
  - HPA handles scaling (pre-declared rules, not NHMA-driven)
```

## 12.2 Database Replication Health

```
PostgreSQL:
  - pg_stat_replication_lag_seconds > 5 s   → WARNING
  - pg_stat_replication_lag_seconds > 30 s  → CRITICAL
  - Replication broken (null lag)            → FATAL → DB_REPLICATION_FAILED

Redis:
  - redis_rejected_connections > 0 in window → WARNING
  - redis_connected_clients approaching max  → WARNING
  - Primary unreachable (probe failure)      → FATAL → REDIS_UNAVAILABLE

etcd:
  - Leader election log detected             → TRUST_LAYER_INSTABILITY → CRITICAL
  - etcd cluster quorum lost                 → FATAL → PagerDuty + SMS

Rules:
  - Primary-replica setup mandatory (HA-H)
  - Automatic failover required (manual DB promotion = NON-COMPLIANT)
  - NHMA reports status — database failover is infrastructure-layer concern
```

## 12.3 Blast Domain Isolation Enforcement

```
Rule: Signal from BD-CORE saturation must NEVER degrade BD-DOJO
Rule: BD-DOJO outage must NEVER propagate to BD-CORE
Rule: BD-TRUST degradation = FATAL for entire platform (all domains depend on it)

Detection:
  - NHMA compares per-namespace resource usage across blast domains
  - If BD-CORE namespace CPU spike correlates with BD-DOJO latency increase:
    → BLAST_DOMAIN_ISOLATION_BREACH → CRITICAL
  - Root cause: tenant throttling failure (R49.10 / R50.9)
  - Alert: Platform Super Admin
  - Human must investigate and restore domain isolation

Enforcement:
  - Tenant throttling fires before global throttling (R49.10)
  - Large tenants must never degrade student discussions
  - NHMA monitors per-tenant CPU + request concurrency
  - Per-tenant saturation alert threshold: > 80% of declared tenant quota
```

## 12.4 Offline Sync Health (Society / BD-SOCIETY)

```
CouchDB Sync:
  - couchdb_replication_changes_pending > 500 → WARNING
  - couchdb_sync_lag_seconds > 300 s (5 min)  → WARNING → OFFLINE_SYNC_INTERRUPTED
  - couchdb_sync_lag_seconds > 3600 s (1 h)   → CRITICAL → alert Society Coordinator

Context:
  - CouchDB is used for offline-first replication in rural zones (Society Architecture)
  - Sync lag is expected during connectivity gaps
  - NHMA distinguishes: expected gap vs unexpected prolonged interruption

Temporal Workflow:
  - temporal_workflow_failed_total delta > 0 → WARNING
  - temporal_task_queue_backlog > 100         → WARNING
  - Society 7-day payout workflow failure      → CRITICAL → alert Finance Admin
    (cross-reference: PRICING_RECALIBRATION_AGENT SOCIETY_PAYOUT_SLA_BREACH)
```

---

# SECTION XIII — WORKLOAD CLASS HEALTH MAPPING (R49.6)

All workloads must declare class. NHMA monitors health by class.

| Workload Class | Examples | Health Priority | Shed Order |
|---|---|---|---|
| Class-1 Interactive (Hot Path) | Student discussions, live matches, dashboards | NEVER shed | Protected |
| Class-2 Transactional | Payments, certifications, scoring | Consistency > speed | Shed after Class-3 |
| Class-3 Background | Analytics, ranking, indexing | Async only | Shed first |

```
Rule: Class-1 latency SLO breach → FATAL class alert regardless of cause
Rule: Class-3 backlog growth alone → WARNING only (expected under load)
Rule: Class-2 partial availability is FORBIDDEN (Commerce domain rule):
      "Either fully available or safely blocked — no partial states"
```

---

# SECTION XIV — INCIDENT CLASSIFICATION MODEL (LOCKED)

All failures must be classified before alert emission. No unclassified failures.

| Class | Definition | Examples | Required Action |
|---|---|---|---|
| `CRITICAL_INTEGRITY` | Data corruption, academic integrity breach | Scoring written with null values, GD session state corrupted | Immediate escalation, service halt if needed |
| `CRITICAL_AVAILABILITY` | Service unusable but data safe | Pod crash loop, media stack down, auth down | Human-gated failover |
| `DEGRADED_EXPERIENCE` | Reduced experience without correctness impact | High latency, reduced search facets, slow dashboards | Shedding order applied |
| `BLAST_DOMAIN_BREACH` | Domain isolation violated | BD-CORE spike degrading BD-DOJO | Platform Super Admin + immediate isolation |
| `SECURITY_EVENT` | Intrusion, WAF spike, DDoS | Wazuh CRITICAL alert, ModSecurity spike | Security Admin — NO automated response |
| `INFRASTRUCTURE_DRIFT` | Configuration drift, resource saturation | HPA limit reached, node saturation | DevOps Admin |

```
Failure Classification Rule:
  CRITICAL_INTEGRITY > CRITICAL_AVAILABILITY > BLAST_DOMAIN_BREACH
  > SECURITY_EVENT > DEGRADED_EXPERIENCE > INFRASTRUCTURE_DRIFT

If multiple classes apply: use the highest-severity class.
Log all classes in the incident record.
```

---

# SECTION XV — ARCHITECTURE PLACEMENT (LOCKED)

## 15.1 System Position

```
NHMA sits in:
  Kubernetes namespace: ops
  Role: Passive observer + active alerter + shedding advisor
  Dependency direction: Consumes metrics → never modifies infrastructure

NHMA is an OBSERVER and ALERTER, not a CONTROLLER.
Kubernetes HPA handles scaling. Humans handle failover.
NHMA watches and reports.
```

## 15.2 Infrastructure Dependencies (Locked)

| Component | Role | NHMA Usage |
|---|---|---|
| Prometheus | Metrics store | Primary signal source — all metrics consumed via PromQL |
| Grafana | Dashboards | NHMA populates alert annotations; dashboards declared in Section XVI |
| Loki | Log store | Log pattern matching; all NHMA alerts written here |
| OpenTelemetry | Distributed tracing | Trace correlation for latency root-cause |
| Wazuh | SIEM | Security signal consumption (read-only) |
| Kafka | Event bus | `ecoskiller.nhma.*` topic for alert emission |
| Redis | State cache | GD state machine health probes |
| PostgreSQL | Source of truth | Replication lag probe |
| Velero | Backup/restore | Backup health verification signals |
| ntfy | Push notifications | Ops-team push alerts for WARNING+ severity |

## 15.3 Data Flow (Non-Negotiable)

```
[All Platform Components]
        ↓ emit to Prometheus (metrics) + Loki (logs) + OTel (traces)
[NHMA Signal Ingestor]
        ↓ evaluates against SLO targets + alert rules
[NHMA Alert Classifier]
        ↓ classifies severity + blast domain + traffic class
[NHMA Kafka Emitter]
        ↓ emits ecoskiller.nhma.alert.* events
[Notification Service → PagerDuty / Slack / SMS / ntfy]
        ↓ human operator receives alert
[Human Response]
        ↓ investigates + acts
[NHMA Incident Tracker]
        ↓ records resolution timestamp + MTTR

NHMA never modifies pods, deployments, configs, or network rules.
NHMA never initiates failover.
NHMA emits. Humans act.
```

---

# SECTION XVI — DASHBOARD SPECIFICATIONS (LOCKED)

## 16.1 Required Grafana Dashboards

All dashboards are per-blast-domain. No cross-domain mashup dashboards.

| Dashboard | Blast Domain | Key Panels |
|---|---|---|
| Core Platform Health | BD-CORE | API latency P95/P99, error rate, pod health, DB replication lag |
| Dojo Realtime Health | BD-DOJO | GD room creation success rate, WebSocket latency, scoring pipeline health |
| Media Stack QoS | BD-MEDIA | Jitsi packet loss, RTT, bitrate, LiveKit room count, coturn allocation success |
| Trust Layer Health | BD-TRUST | Auth API latency, etcd status, OPA enforcement latency, cert expiry |
| Security Operations | BD-OPS | Wazuh alert rate by level, ModSecurity blocks, DDoS detection status |
| Society / Offline | BD-SOCIETY | CouchDB sync lag, Temporal workflow health, offline node count |
| Platform-Wide SLO | All | Availability per blast domain vs target, active SLO breaches, MTTR trend |
| Kubernetes Resources | All | Pod restarts per namespace, HPA status, node CPU/memory |
| Kafka Event Bus | BD-CORE/DOJO | Consumer lag per topic, producer rate, broker network IO |

## 16.2 Mandatory Dashboard Rules

```
Every dashboard panel must declare:
  - blast_domain binding
  - traffic_class relevance
  - SLO threshold line (visual reference)
  - Color coding: GREEN = healthy, YELLOW = warning, RED = breach

Cross-domain panel mixing → FORBIDDEN
Grafana dashboards are read-only for non-ops roles
```

---

# SECTION XVII — INTERN-EXECUTABLE RUNBOOKS (R49.17)

Per R49.17, each runbook includes: file path, command, expected metric, threshold, escalation rule.

## 17.1 Runbook: API Latency Check

```
File:      /runbooks/api_latency_check.md
Command:   kubectl top pods -n core && curl -s prometheus:9090/api/v1/query?query=http_request_duration_seconds
Expected:  P95 ≤ 300 ms for core APIs
Threshold: > 300 ms = WARNING, > 600 ms = CRITICAL
Escalation: If CRITICAL → Slack #ops-alerts → Platform Ops Lead
```

## 17.2 Runbook: Video QoS Recovery

```
File:      /runbooks/video_qos_recovery.md
Command:   kubectl get pods -n media && kubectl logs -n media jitsi-videobridge-* | grep "packet_loss"
Expected:  packet_loss_ratio < 2%
Threshold: > 5% = CRITICAL
Escalation: If CRITICAL → PagerDuty → DevOps Admin → coturn pool review
```

## 17.3 Runbook: Queue Backlog Mitigation

```
File:      /runbooks/kafka_backlog_mitigation.md
Command:   kafka-consumer-groups.sh --bootstrap-server kafka:9092 --describe --all-groups
Expected:  Lag ≤ 1000 messages per partition per group
Threshold: > 5000 = WARNING, > 50000 = CRITICAL
Escalation: Scale consumer group replicas (HPA); if stuck → DevOps Admin
```

## 17.4 Runbook: DB Slow Query Handling

```
File:      /runbooks/db_slow_query.md
Command:   psql -c "SELECT * FROM pg_stat_activity WHERE state = 'active' AND query_start < now() - interval '5 seconds';"
Expected:  No queries running > 5 seconds
Threshold: > 30 s = CRITICAL
Escalation: Kill query (requires DBA approval), analyze indexes, alert DevOps Admin
```

## 17.5 Runbook: coturn Pool Exhaustion

```
File:      /runbooks/coturn_pool_exhaustion.md
Command:   kubectl exec -n media coturn-pod -- turnutils_uclient -t -T -u test -w test <TURN_SERVER>
Expected:  Allocation success, < 500 ms response
Threshold: Failure or > 500 ms = CRITICAL
Escalation: Increase coturn replica count (human approval required), alert DevOps Admin
```

---

# SECTION XVIII — OBSERVABILITY SELF-HEALTH (NON-OPTIONAL)

NHMA monitors its own health. Silent NHMA = platform is blind.

## 18.1 NHMA Self-Health Metrics

```
nhma_signals_ingested_total (counter, by blast_domain, signal_type)
nhma_alerts_emitted_total (counter, by severity, alert_code)
nhma_probe_success_ratio (gauge, by probe_name)
nhma_probe_latency_seconds (histogram, by probe_name)
nhma_processing_lag_seconds (histogram — SLA: < 30 s for FATAL, < 60 s for CRITICAL)
nhma_kafka_emit_errors_total (counter)
nhma_loki_write_errors_total (counter)
```

## 18.2 NHMA Self-Failure Rules

```
nhma_processing_lag > 60 s           → NHMA_LAG_BREACH → PagerDuty
nhma_probe_success_ratio < 90%       → NHMA_PROBE_FAILURE → Slack + email
nhma_kafka_emit_errors > 0 sustained → NHMA_EMIT_FAILURE → PagerDuty
Prometheus unreachable > 30 s        → NHMA_BLIND → PagerDuty (FATAL)

If NHMA is blind (cannot reach Prometheus):
  - Emit NHMA_BLIND event via ntfy push to on-call
  - Log last known state to MinIO cold archive
  - Platform Ops Lead must manually verify system health
  - System state = "MONITORING UNAVAILABLE"
```

---

# SECTION XIX — FAILURE HANDLING (DETERMINISTIC)

| Failure | System Response |
|---|---|
| Signal schema invalid | Discard + dead-letter Kafka topic + alert |
| Prometheus unreachable | NHMA_BLIND → PagerDuty immediately |
| Loki write failure | Alert + buffer locally + retry 3× |
| Kafka emit failure | Retry 3× backoff → alert if sustained |
| Synthetic probe timeout | 3 consecutive failures = service_down event |
| Blast domain isolation breach detected | CRITICAL → Platform Super Admin immediately |
| GD session FATAL event | Alert only — NEVER auto-terminate sessions |
| Security event from Wazuh | Alert Security Admin — NO automated response |
| Student-facing service FATAL | Shedding order applied + FATAL alert |
| NHMA processing lag > 30 s | Self-alert + escalate |
| HPA scale limit reached | WARNING + alert Finance Admin (cost envelope check required) |
| Circuit breaker open > 10 min | Escalate from CRITICAL to FATAL |
| CouchDB sync lag > 1 hour | CRITICAL + notify Society Coordinator |

```
NHMA FAILURE PHILOSOPHY:
  An alert never sent is worse than an alert sent twice.
  A false positive is recoverable. A missed FATAL is not.
  Every FATAL is logged before any other action.
  Blast domain isolation is sacred — never collapse it.
  The media plane never touches the API plane — monitor them separately.
  Student discussions are the highest protected class — always.
```

---

# SECTION XX — WHAT THIS AGENT NEVER DOES

```
NEVER modifies Kubernetes deployments, pods, configs, or network policies
NEVER initiates database failover
NEVER resets circuit breakers
NEVER blocks IP addresses or modifies WAF rules
NEVER scales infrastructure (HPA manages scaling per pre-declared rules)
NEVER terminates active GD sessions or Dojo match rooms
NEVER modifies media room state (Jitsi / LiveKit are sovereign)
NEVER responds to security incidents (Wazuh + Security Admin own that)
NEVER mixes blast domain health signals
NEVER emits alerts for undeclared signal types
NEVER auto-deploys hotfixes or rollbacks
NEVER claims a service is healthy without a successful probe result
NEVER operates with cached probe data older than 2× probe interval
```

---

# SECTION XXI — VERSION & CHANGE CONTROL

```
Document Version:   1.0
Status:             SEALED
Seal Date:          2026-03-04
Next Review:        Version bump required for any structural change

Allowed (no version bump):
  - Add new probe to Section VI (with frequency and SLO target declared)
  - Add new alert code to routing matrix (with severity and recipient declared)
  - Add new Prometheus metric to signal catalog
  - Add new Grafana dashboard panel (additive only)
  - Add new runbook (following Section XVII format exactly)

Requires version bump + human declaration:
  - Change any SLO target in Section IV
  - Change any blast domain definition in Section II
  - Change any traffic class definition in Section III
  - Change the feature shedding order in Section VIII
  - Change any alert severity level in Section VII
  - Change any auto-action permitted boundary in Section VII
  - Change the NHMA self-failure rules in Section XVIII
  - Add or remove a protected (never shed) component

IMPLICIT BEHAVIOR = FORBIDDEN.
ALL signals are declared.
ALL SLOs are numeric and hard.
ALL alert routes are named.
ALL auto-actions are bounded.
ALL failure modes are enumerated.
```

---

## ✅ SEAL CONFIRMATION

```
✔ Agent identity declared — observer, alerter, shedding advisor; not a controller
✔ Blast domain map locked — 6 domains, namespace binding, isolation rules
✔ Traffic class definitions locked — 4 classes, no mixing permitted
✔ SLO targets hard-locked — API latency, media, availability, incident response
✔ WebRTC bandwidth baselines declared — per-participant, degradation rules
✔ Signal catalog locked — Prometheus metrics, Loki patterns, canonical envelope
✔ Active synthetic probes declared — 15 probes, frequency, SLO target per probe
✔ Alert classification locked — 4 severity levels, 25+ named alert codes
✔ Alert routing matrix complete — role-based, channel-specific
✔ Feature shedding order immutable — 3 shed tiers + protected class declared
✔ Media stack health rules locked — Jitsi, LiveKit, coturn, ICE failure
✔ Ingress / gateway health rules locked — NGINX, Kong, Envoy circuit breaker
✔ Security signal integration locked — Wazuh, ModSecurity, DDoS detection
✔ HA / failure mode rules locked — nodes, DB replication, blast domain isolation
✔ Offline sync health rules locked — CouchDB, Temporal, Society domain
✔ Workload class health mapping declared — Class-1 / 2 / 3 per R49.6
✔ Incident classification model sealed — 6 severity classes, priority order
✔ Architecture placement declared — ops namespace, consumer not controller
✔ Dashboard specifications declared — 9 dashboards, per-blast-domain
✔ Intern-executable runbooks included — 5 runbooks per R49.17 format
✔ NHMA self-health monitoring declared — blind state handling included
✔ Failure handling deterministic — every failure mode enumerated
✔ Prohibited behaviors explicitly listed — 20 hard constraints
✔ Change control enforced — version bump triggers declared

NETWORK_HEALTH_MONITOR_AGENT IS SEALED.
EXECUTION BEGINS ONLY ON HUMAN DECLARATION.
THE PLATFORM IS WATCHED. NHMA NEVER SLEEPS.
```
