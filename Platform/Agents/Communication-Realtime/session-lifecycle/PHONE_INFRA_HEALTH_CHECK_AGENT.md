# 📱 PHONE_INFRA_HEALTH_CHECK_AGENT v1.0
**Status:** LOCKED · SEALED · DETERMINISTIC · PRODUCTION-GRADE  
**Mutation Policy:** Add-only via version bump  
**Execution Authority:** Automated monitoring with human-override capability  
**Artifact Class:** Infrastructure Observability & Health Enforcement System  
**Platform Target:** ECOSKILLER Multi-Tenant SaaS Platform  
**Deployment Context:** Multi-Environment (DEV · TEST · STAGING · PRODUCTION)

---

## 🔒 SECTION 1 — SYSTEM IDENTITY & PURPOSE

### A. Agent Identity
```
Agent Name: PHONE_INFRA_HEALTH_CHECK_AGENT
Agent Type: Autonomous Infrastructure Monitoring & Health Enforcement System
Execution Mode: Continuous Real-Time Monitoring
Failure Philosophy: Detect → Alert → Isolate → Report → Prevent Cascade
```

### B. Core Responsibility
The PHONE_INFRA_HEALTH_CHECK_AGENT is a deterministic, rule-driven infrastructure health monitoring system that:

1. **Continuously monitors** all infrastructure components across all environments
2. **Detects failures** before they impact end users
3. **Enforces health contracts** defined in deployment manifests
4. **Triggers automated remediation** where safe and defined
5. **Escalates to human operators** when automated remediation is unsafe
6. **Prevents deployment** of unhealthy services
7. **Maintains immutable audit trail** of all health events

### C. What This Agent Does NOT Do
- Does NOT make architectural decisions
- Does NOT modify infrastructure without human authorization for critical components
- Does NOT interpret ambiguous health signals as "probably healthy"
- Does NOT allow partial deployments of critical services
- Does NOT suppress alerts to reduce noise

---

## 🔒 SECTION 2 — HEALTH CHECK TAXONOMY

### A. Health Check Categories

#### 1. Infrastructure Layer Checks
```yaml
compute_health:
  - kubernetes_cluster_status
  - node_readiness
  - pod_crash_loops
  - resource_exhaustion
  - eviction_rate
  - autoscaler_failures

network_health:
  - ingress_controller_status
  - service_mesh_connectivity
  - dns_resolution
  - tls_certificate_validity
  - load_balancer_availability
  - api_gateway_reachability

storage_health:
  - persistent_volume_claims
  - storage_class_availability
  - disk_space_utilization
  - inode_exhaustion
  - backup_completion_status
```

#### 2. Data Layer Checks
```yaml
database_health:
  postgresql_primary:
    - connection_pool_saturation
    - replication_lag
    - disk_io_latency
    - transaction_deadlocks
    - vacuum_process_health
    - table_bloat_threshold
  
  redis_health:
    - memory_fragmentation
    - eviction_rate
    - key_hit_ratio
    - persistence_sync_status
    - cluster_partition_detection

  opensearch_health:
    - cluster_status_green
    - shard_allocation_failures
    - index_throughput
    - search_latency_p99
    - disk_watermark_violations

  clickhouse_health:
    - replica_sync_status
    - insert_latency
    - merge_process_backlog
    - disk_space_analytics_volume
```

#### 3. Application Service Checks
```yaml
microservice_health:
  auth_service:
    - jwt_issuer_availability
    - keycloak_realm_health
    - token_validation_latency
    - oauth_callback_errors
  
  core_services:
    - api_response_time_p95
    - error_rate_threshold
    - dependency_availability
    - circuit_breaker_status
    - rate_limiter_effectiveness
  
  realtime_services:
    jitsi_health:
      - sfu_videobridge_load
      - jicofo_conference_count
      - coturn_relay_health
      - media_session_failures
    
    websocket_health:
      - connection_count
      - message_delivery_rate
      - reconnection_storm_detection
```

#### 4. External Dependency Checks
```yaml
external_systems:
  payment_gateway:
    - stripe_api_latency
    - webhook_delivery_rate
    - failed_transaction_count
  
  notification_systems:
    - email_delivery_rate
    - sms_gateway_availability
    - push_notification_success_rate
  
  cdn_object_storage:
    - minio_availability
    - object_retrieval_latency
    - replication_health
```

#### 5. Security & Compliance Checks
```yaml
security_posture:
  certificate_management:
    - tls_expiry_warning (30d, 7d, 1d)
    - cert_manager_issuer_health
    - certificate_renewal_failures
  
  secrets_vault:
    - hashicorp_vault_seal_status
    - secret_rotation_schedule
    - unauthorized_access_attempts
  
  access_control:
    - rbac_policy_enforcement
    - tenant_isolation_verification
    - session_hijacking_detection
```

---

## 🔒 SECTION 3 — HEALTH CHECK EXECUTION MODEL

### A. Check Execution Frequency Matrix

| Health Check Category | Execution Interval | Alerting Threshold | Auto-Remediation |
|----------------------|-------------------|-------------------|------------------|
| Critical Path (Auth, DB, API Gateway) | 10 seconds | 2 consecutive failures | Yes (safe actions only) |
| Core Services | 30 seconds | 3 consecutive failures | Yes |
| Background Jobs | 60 seconds | 5 consecutive failures | No (human review) |
| Security Posture | 5 minutes | 1 failure | Immediate alert |
| Cost & Resource Optimization | 15 minutes | N/A | No |
| Certificate Expiry | 1 hour | <30 days remaining | No |

### B. Health Status State Machine

```
HEALTHY → DEGRADED → UNHEALTHY → CRITICAL → FAILED
    ↑         ↓          ↓           ↓          ↓
    └─────────┴──────────┴───────────┴──────────┘
              AUTO-REMEDIATION ATTEMPTS
```

**State Definitions:**
- **HEALTHY**: All checks passing, normal operation
- **DEGRADED**: Non-critical checks failing, service functional
- **UNHEALTHY**: Critical checks failing, service impaired
- **CRITICAL**: Multiple critical failures, service barely functional
- **FAILED**: Service non-functional, user impact confirmed

### C. Remediation Decision Matrix

| Failure Type | Automated Action | Human Escalation Required |
|-------------|-----------------|---------------------------|
| Pod crash loop | Restart pod (max 3 attempts) | After 3 failed restarts |
| Memory leak detected | Scale up replica (temporary) | Yes (investigation) |
| Database connection pool exhausted | Increase pool size (if headroom) | If no headroom available |
| Certificate expiring <7 days | Trigger renewal | If renewal fails |
| Disk space >90% | Trigger cleanup jobs | If cleanup fails |
| Replication lag >5s | Alert only | Yes (immediate) |
| Security breach indicators | Lock affected tenant | Yes (immediate) |

---

## 🔒 SECTION 4 — HEALTH CHECK IMPLEMENTATION SPECIFICATION

### A. Prometheus Metrics Collection

```yaml
# Core Infrastructure Metrics
prometheus_scrape_configs:
  
  - job_name: 'kubernetes-nodes'
    scrape_interval: 10s
    metrics_path: '/metrics'
    kubernetes_sd_configs:
      - role: node
    relabel_configs:
      - action: labelmap
        regex: __meta_kubernetes_node_label_(.+)
  
  - job_name: 'kubernetes-pods'
    scrape_interval: 30s
    kubernetes_sd_configs:
      - role: pod
    relabel_configs:
      - source_labels: [__meta_kubernetes_pod_annotation_prometheus_io_scrape]
        action: keep
        regex: true
      - source_labels: [__meta_kubernetes_pod_annotation_prometheus_io_path]
        action: replace
        target_label: __metrics_path__
        regex: (.+)
  
  - job_name: 'postgresql-exporter'
    scrape_interval: 15s
    static_configs:
      - targets:
        - postgresql-exporter:9187
  
  - job_name: 'redis-exporter'
    scrape_interval: 15s
    static_configs:
      - targets:
        - redis-exporter:9121
  
  - job_name: 'opensearch-exporter'
    scrape_interval: 30s
    static_configs:
      - targets:
        - opensearch-exporter:9114
  
  - job_name: 'jitsi-exporter'
    scrape_interval: 20s
    static_configs:
      - targets:
        - jitsi-videobridge-exporter:9888
```

### B. Health Check Probe Definitions

#### Kubernetes Liveness & Readiness Probes
```yaml
# Example: Auth Service Health Probes
apiVersion: v1
kind: Pod
metadata:
  name: auth-service
spec:
  containers:
  - name: auth-service
    image: ecoskiller/auth-service:latest
    
    livenessProbe:
      httpGet:
        path: /health/live
        port: 8080
      initialDelaySeconds: 30
      periodSeconds: 10
      timeoutSeconds: 5
      failureThreshold: 3
    
    readinessProbe:
      httpGet:
        path: /health/ready
        port: 8080
      initialDelaySeconds: 10
      periodSeconds: 5
      timeoutSeconds: 3
      failureThreshold: 2
    
    startupProbe:
      httpGet:
        path: /health/startup
        port: 8080
      initialDelaySeconds: 0
      periodSeconds: 5
      timeoutSeconds: 3
      failureThreshold: 30
```

#### Health Endpoint Response Contract
```json
// GET /health/ready
{
  "status": "healthy|degraded|unhealthy",
  "timestamp": "2026-03-04T10:30:00Z",
  "service": "auth-service",
  "version": "1.2.3",
  "environment": "production",
  "dependencies": {
    "postgresql": {
      "status": "healthy",
      "latency_ms": 2.5,
      "connection_pool": {
        "active": 5,
        "idle": 15,
        "max": 20
      }
    },
    "redis": {
      "status": "healthy",
      "latency_ms": 0.8,
      "memory_usage_mb": 1024,
      "eviction_rate": 0
    },
    "keycloak": {
      "status": "healthy",
      "realm_accessible": true,
      "latency_ms": 15.3
    }
  },
  "checks": {
    "database_connection": "pass",
    "cache_connection": "pass",
    "jwt_signing_key_loaded": "pass",
    "oauth_provider_reachable": "pass"
  }
}
```

### C. Grafana Alert Rules (PromQL)

```yaml
# Critical Infrastructure Alerts
groups:
  - name: infrastructure_critical
    interval: 10s
    rules:
      
      # Node Failure Detection
      - alert: KubernetesNodeNotReady
        expr: kube_node_status_condition{condition="Ready",status="true"} == 0
        for: 2m
        labels:
          severity: critical
          component: kubernetes
        annotations:
          summary: "Kubernetes node {{ $labels.node }} not ready"
          description: "Node has been in NotReady state for >2 minutes"
      
      # Pod Crash Loop Detection
      - alert: PodCrashLooping
        expr: rate(kube_pod_container_status_restarts_total[15m]) > 0
        for: 5m
        labels:
          severity: warning
          component: kubernetes
        annotations:
          summary: "Pod {{ $labels.namespace }}/{{ $labels.pod }} crash looping"
          description: "Pod has restarted {{ $value }} times in last 15 minutes"
      
      # Memory Pressure
      - alert: NodeMemoryPressure
        expr: kube_node_status_condition{condition="MemoryPressure",status="true"} == 1
        for: 5m
        labels:
          severity: warning
          component: kubernetes
        annotations:
          summary: "Node {{ $labels.node }} under memory pressure"
      
      # Disk Pressure
      - alert: NodeDiskPressure
        expr: kube_node_status_condition{condition="DiskPressure",status="true"} == 1
        for: 5m
        labels:
          severity: critical
          component: kubernetes
        annotations:
          summary: "Node {{ $labels.node }} under disk pressure"

  - name: database_health
    interval: 15s
    rules:
      
      # PostgreSQL Connection Pool Saturation
      - alert: PostgreSQLConnectionPoolSaturated
        expr: |
          (pg_stat_database_numbackends / pg_settings_max_connections) > 0.8
        for: 2m
        labels:
          severity: warning
          component: postgresql
        annotations:
          summary: "PostgreSQL connection pool >80% utilized"
      
      # Replication Lag
      - alert: PostgreSQLReplicationLag
        expr: |
          pg_replication_lag_seconds > 5
        for: 1m
        labels:
          severity: critical
          component: postgresql
        annotations:
          summary: "PostgreSQL replication lag >5 seconds"
      
      # Redis Memory Fragmentation
      - alert: RedisMemoryFragmentationHigh
        expr: redis_memory_fragmentation_ratio > 1.5
        for: 10m
        labels:
          severity: warning
          component: redis
        annotations:
          summary: "Redis memory fragmentation ratio >1.5"
      
      # OpenSearch Cluster Red
      - alert: OpenSearchClusterRed
        expr: opensearch_cluster_status{color="red"} == 1
        for: 1m
        labels:
          severity: critical
          component: opensearch
        annotations:
          summary: "OpenSearch cluster status is RED"

  - name: application_health
    interval: 30s
    rules:
      
      # API Error Rate
      - alert: APIErrorRateHigh
        expr: |
          sum(rate(http_requests_total{status=~"5.."}[5m])) by (service) 
          / 
          sum(rate(http_requests_total[5m])) by (service) > 0.05
        for: 2m
        labels:
          severity: critical
          component: api
        annotations:
          summary: "Service {{ $labels.service }} error rate >5%"
      
      # API Latency P95
      - alert: APILatencyP95High
        expr: |
          histogram_quantile(0.95, 
            sum(rate(http_request_duration_seconds_bucket[5m])) by (service, le)
          ) > 1.0
        for: 5m
        labels:
          severity: warning
          component: api
        annotations:
          summary: "Service {{ $labels.service }} P95 latency >1s"
      
      # JWT Validation Failures
      - alert: JWTValidationFailureRateHigh
        expr: |
          rate(auth_jwt_validation_failures_total[5m]) > 10
        for: 2m
        labels:
          severity: critical
          component: auth
        annotations:
          summary: "JWT validation failures >10/sec"

  - name: realtime_media_health
    interval: 20s
    rules:
      
      # Jitsi Conference Failures
      - alert: JitsiConferenceFailureRateHigh
        expr: |
          rate(jitsi_conference_creation_failed_total[5m]) > 0.1
        for: 3m
        labels:
          severity: critical
          component: jitsi
        annotations:
          summary: "Jitsi conference creation failure rate >10%"
      
      # WebSocket Connection Failures
      - alert: WebSocketConnectionFailures
        expr: |
          rate(websocket_connection_failures_total[5m]) > 5
        for: 2m
        labels:
          severity: warning
          component: websocket
        annotations:
          summary: "WebSocket connection failures >5/sec"

  - name: security_compliance
    interval: 300s
    rules:
      
      # TLS Certificate Expiry Warning
      - alert: TLSCertificateExpiringWithin30Days
        expr: |
          (cert_manager_certificate_expiration_timestamp_seconds - time()) 
          / 86400 < 30
        labels:
          severity: warning
          component: security
        annotations:
          summary: "TLS certificate {{ $labels.name }} expiring in <30 days"
      
      # TLS Certificate Expiry Critical
      - alert: TLSCertificateExpiringWithin7Days
        expr: |
          (cert_manager_certificate_expiration_timestamp_seconds - time()) 
          / 86400 < 7
        labels:
          severity: critical
          component: security
        annotations:
          summary: "TLS certificate {{ $labels.name }} expiring in <7 days"
      
      # Vault Sealed
      - alert: HashicorpVaultSealed
        expr: vault_core_unsealed == 0
        for: 1m
        labels:
          severity: critical
          component: security
        annotations:
          summary: "Hashicorp Vault is sealed"
      
      # Unauthorized Access Attempts
      - alert: UnauthorizedAccessAttemptsHigh
        expr: |
          rate(api_gateway_unauthorized_requests_total[5m]) > 50
        for: 2m
        labels:
          severity: critical
          component: security
        annotations:
          summary: "Unauthorized API access attempts >50/sec"
```

---

## 🔒 SECTION 5 — AUTOMATED REMEDIATION PLAYBOOKS

### A. Remediation Execution Rules

```yaml
remediation_policy:
  enabled: true
  require_human_approval_for:
    - data_operations
    - production_deployments
    - security_isolation
    - infrastructure_scaling_beyond_threshold
  
  auto_remediation_allowed:
    - pod_restarts
    - horizontal_pod_autoscaling
    - cache_clearing
    - log_rotation
    - temporary_resource_scaling
  
  max_remediation_attempts: 3
  cooldown_between_attempts: 300s  # 5 minutes
  
  escalation_on_failure: true
  escalation_channels:
    - pagerduty
    - slack_ops_channel
    - email_oncall
```

### B. Remediation Playbook Examples

#### Playbook 1: Pod Crash Loop Recovery
```yaml
playbook_name: pod_crash_loop_recovery
trigger:
  alert: PodCrashLooping
  condition: restarts > 3 in 10 minutes

steps:
  1_collect_diagnostics:
    - fetch_pod_logs (last 1000 lines)
    - fetch_pod_events
    - fetch_resource_usage
    - check_liveness_readiness_probes
  
  2_attempt_graceful_restart:
    - kubectl rollout restart deployment {{ deployment_name }}
    - wait_for_rollout_completion (timeout: 5m)
  
  3_verify_health:
    - check_pod_running_status
    - check_readiness_probe_passing
    - monitor_for_new_crashes (duration: 5m)
  
  4_escalate_if_failed:
    - attach_diagnostics_to_alert
    - page_oncall_engineer
    - create_incident_ticket
```

#### Playbook 2: Database Connection Pool Saturation
```yaml
playbook_name: database_connection_pool_recovery
trigger:
  alert: PostgreSQLConnectionPoolSaturated
  condition: pool_usage > 80%

steps:
  1_identify_long_running_queries:
    - query: SELECT * FROM pg_stat_activity WHERE state = 'active' AND query_start < NOW() - INTERVAL '30 seconds'
    - log_query_details
  
  2_attempt_connection_pool_expansion:
    - if: current_max_connections < infrastructure_limit
      then:
        - increase_pool_size (increment: +10)
        - update_deployment_config
        - rolling_restart_service
    - else:
        - skip_to_step: 4_escalate
  
  3_verify_saturation_reduced:
    - monitor_pool_usage (duration: 5m)
    - check: pool_usage < 70%
  
  4_escalate:
    - notify: Database team
    - action_required: Investigate query performance, add read replicas, or optimize queries
```

#### Playbook 3: Certificate Renewal Automation
```yaml
playbook_name: tls_certificate_renewal
trigger:
  alert: TLSCertificateExpiringWithin30Days
  condition: days_remaining < 30

steps:
  1_attempt_automatic_renewal:
    - cert_manager_request_renewal (certificate: {{ certificate_name }})
    - wait_for_issuance (timeout: 10m)
  
  2_verify_renewal_success:
    - check_certificate_validity
    - check_expiry_date_extended
  
  3_restart_dependent_services:
    - identify_services_using_certificate
    - rolling_restart_affected_services
  
  4_verify_traffic_recovery:
    - check_ingress_tls_handshake_success
    - monitor_for_tls_errors (duration: 5m)
  
  5_escalate_if_failed:
    - alert: Security team
    - create_high_priority_ticket
    - document_failure_reason
```

---

## 🔒 SECTION 6 — ENVIRONMENT-SPECIFIC HEALTH THRESHOLDS

### A. DEV Environment Thresholds
```yaml
environment: dev
health_thresholds:
  relaxed_mode: true
  auto_remediation: limited
  
  kubernetes:
    pod_restart_threshold: 10 (in 30m)
    memory_threshold: 90%
    cpu_threshold: 85%
  
  database:
    connection_pool_threshold: 95%
    replication_lag_threshold: 30s
  
  api:
    error_rate_threshold: 10%
    p95_latency_threshold: 5s
  
  alerting:
    suppress_low_severity: true
    notification_channels:
      - slack_dev_channel
```

### B. TEST Environment Thresholds
```yaml
environment: test
health_thresholds:
  relaxed_mode: false
  auto_remediation: enabled
  
  kubernetes:
    pod_restart_threshold: 5 (in 15m)
    memory_threshold: 85%
    cpu_threshold: 80%
  
  database:
    connection_pool_threshold: 85%
    replication_lag_threshold: 10s
  
  api:
    error_rate_threshold: 5%
    p95_latency_threshold: 2s
  
  alerting:
    suppress_low_severity: false
    notification_channels:
      - slack_qa_channel
      - email_qa_team
```

### C. STAGING Environment Thresholds
```yaml
environment: staging
health_thresholds:
  production_parity: true
  auto_remediation: enabled_with_approval
  
  kubernetes:
    pod_restart_threshold: 3 (in 10m)
    memory_threshold: 80%
    cpu_threshold: 75%
  
  database:
    connection_pool_threshold: 80%
    replication_lag_threshold: 5s
  
  api:
    error_rate_threshold: 2%
    p95_latency_threshold: 1s
  
  security:
    certificate_expiry_warning: 45d
    certificate_expiry_critical: 14d
  
  alerting:
    notification_channels:
      - slack_staging_alerts
      - email_devops_team
      - pagerduty_staging
```

### D. PRODUCTION Environment Thresholds
```yaml
environment: production
health_thresholds:
  strict_mode: true
  auto_remediation: enabled_safe_only
  
  kubernetes:
    pod_restart_threshold: 2 (in 5m)
    memory_threshold: 75%
    cpu_threshold: 70%
    node_readiness_critical: 2_consecutive_failures
  
  database:
    connection_pool_threshold: 75%
    replication_lag_threshold: 3s
    transaction_deadlock_threshold: 5 (in 5m)
  
  api:
    error_rate_threshold: 1%
    p95_latency_threshold: 500ms
    p99_latency_threshold: 2s
  
  realtime:
    jitsi_conference_failure_threshold: 2%
    websocket_connection_failure_threshold: 1%
  
  security:
    certificate_expiry_warning: 60d
    certificate_expiry_critical: 21d
    unauthorized_access_threshold: 10 (in 1m)
    vault_seal_status_critical: immediate
  
  alerting:
    notification_channels:
      - pagerduty_critical
      - slack_ops_critical
      - email_oncall_primary
      - email_oncall_secondary
      - sms_oncall (for critical only)
```

---

## 🔒 SECTION 7 — HEALTH CHECK AGENT DEPLOYMENT

### A. Agent Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                PHONE_INFRA_HEALTH_CHECK_AGENT               │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │  Prometheus  │  │   Grafana    │  │     Loki     │     │
│  │   Metrics    │  │  Dashboards  │  │     Logs     │     │
│  │  Collection  │  │  & Alerts    │  │  Aggregation │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
│         │                  │                  │            │
│         └──────────────────┴──────────────────┘            │
│                           │                                │
│                  ┌────────▼────────┐                       │
│                  │  Alert Manager  │                       │
│                  │   Routing &     │                       │
│                  │  Deduplication  │                       │
│                  └────────┬────────┘                       │
│                           │                                │
│         ┌─────────────────┼─────────────────┐             │
│         │                 │                 │             │
│  ┌──────▼──────┐  ┌──────▼──────┐  ┌──────▼──────┐       │
│  │  PagerDuty  │  │    Slack    │  │    Email    │       │
│  │  Incident   │  │   Channel   │  │   Oncall    │       │
│  │  Creation   │  │   Webhook   │  │   Alerts    │       │
│  └─────────────┘  └─────────────┘  └─────────────┘       │
│                                                            │
│  ┌─────────────────────────────────────────────────────┐  │
│  │         Automated Remediation Engine                │  │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │  │
│  │  │  Playbook   │  │ Kubernetes  │  │  Approval   │ │  │
│  │  │  Executor   │  │  API Client │  │   Gateway   │ │  │
│  │  └─────────────┘  └─────────────┘  └─────────────┘ │  │
│  └─────────────────────────────────────────────────────┘  │
│                                                            │
│  ┌─────────────────────────────────────────────────────┐  │
│  │            Audit Trail & Compliance Logger          │  │
│  │  (Immutable append-only log of all health events)  │  │
│  └─────────────────────────────────────────────────────┘  │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

### B. Kubernetes Deployment Manifest

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: monitoring

---
# Prometheus Deployment
apiVersion: apps/v1
kind: Deployment
metadata:
  name: prometheus
  namespace: monitoring
spec:
  replicas: 2
  selector:
    matchLabels:
      app: prometheus
  template:
    metadata:
      labels:
        app: prometheus
    spec:
      serviceAccountName: prometheus
      containers:
      - name: prometheus
        image: prom/prometheus:v2.45.0
        args:
          - '--config.file=/etc/prometheus/prometheus.yml'
          - '--storage.tsdb.path=/prometheus'
          - '--storage.tsdb.retention.time=30d'
          - '--web.enable-lifecycle'
        ports:
        - containerPort: 9090
          name: http
        volumeMounts:
        - name: prometheus-config
          mountPath: /etc/prometheus
        - name: prometheus-storage
          mountPath: /prometheus
        resources:
          requests:
            memory: "2Gi"
            cpu: "1000m"
          limits:
            memory: "4Gi"
            cpu: "2000m"
      volumes:
      - name: prometheus-config
        configMap:
          name: prometheus-config
      - name: prometheus-storage
        persistentVolumeClaim:
          claimName: prometheus-storage

---
# Grafana Deployment
apiVersion: apps/v1
kind: Deployment
metadata:
  name: grafana
  namespace: monitoring
spec:
  replicas: 1
  selector:
    matchLabels:
      app: grafana
  template:
    metadata:
      labels:
        app: grafana
    spec:
      containers:
      - name: grafana
        image: grafana/grafana:10.0.0
        ports:
        - containerPort: 3000
          name: http
        env:
        - name: GF_SECURITY_ADMIN_PASSWORD
          valueFrom:
            secretKeyRef:
              name: grafana-secrets
              key: admin-password
        - name: GF_INSTALL_PLUGINS
          value: "grafana-piechart-panel,grafana-worldmap-panel"
        volumeMounts:
        - name: grafana-storage
          mountPath: /var/lib/grafana
        - name: grafana-datasources
          mountPath: /etc/grafana/provisioning/datasources
        - name: grafana-dashboards-config
          mountPath: /etc/grafana/provisioning/dashboards
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
      volumes:
      - name: grafana-storage
        persistentVolumeClaim:
          claimName: grafana-storage
      - name: grafana-datasources
        configMap:
          name: grafana-datasources
      - name: grafana-dashboards-config
        configMap:
          name: grafana-dashboards-config

---
# AlertManager Deployment
apiVersion: apps/v1
kind: Deployment
metadata:
  name: alertmanager
  namespace: monitoring
spec:
  replicas: 2
  selector:
    matchLabels:
      app: alertmanager
  template:
    metadata:
      labels:
        app: alertmanager
    spec:
      containers:
      - name: alertmanager
        image: prom/alertmanager:v0.26.0
        args:
          - '--config.file=/etc/alertmanager/alertmanager.yml'
          - '--storage.path=/alertmanager'
          - '--cluster.advertise-address=$(POD_IP):9093'
        env:
        - name: POD_IP
          valueFrom:
            fieldRef:
              fieldPath: status.podIP
        ports:
        - containerPort: 9093
          name: http
        - containerPort: 9094
          name: mesh
        volumeMounts:
        - name: alertmanager-config
          mountPath: /etc/alertmanager
        - name: alertmanager-storage
          mountPath: /alertmanager
        resources:
          requests:
            memory: "256Mi"
            cpu: "100m"
          limits:
            memory: "512Mi"
            cpu: "200m"
      volumes:
      - name: alertmanager-config
        configMap:
          name: alertmanager-config
      - name: alertmanager-storage
        persistentVolumeClaim:
          claimName: alertmanager-storage
```

### C. AlertManager Routing Configuration

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: alertmanager-config
  namespace: monitoring
data:
  alertmanager.yml: |
    global:
      resolve_timeout: 5m
      
      # PagerDuty
      pagerduty_url: 'https://events.pagerduty.com/v2/enqueue'
      
      # Slack
      slack_api_url: 'https://hooks.slack.com/services/YOUR/WEBHOOK/URL'
      
      # SMTP
      smtp_from: 'alerts@ecoskiller.com'
      smtp_smarthost: 'smtp.sendgrid.net:587'
      smtp_auth_username: 'apikey'
      smtp_auth_password: '{{ SENDGRID_API_KEY }}'
      smtp_require_tls: true
    
    # Routing tree
    route:
      receiver: 'default'
      group_by: ['alertname', 'cluster', 'service', 'severity']
      group_wait: 10s
      group_interval: 10s
      repeat_interval: 12h
      
      routes:
        # Critical production alerts → PagerDuty + Slack + Email
        - match:
            severity: critical
            environment: production
          receiver: 'pagerduty-critical'
          continue: true
        
        - match:
            severity: critical
            environment: production
          receiver: 'slack-critical'
          continue: true
        
        - match:
            severity: critical
            environment: production
          receiver: 'email-oncall'
        
        # Warning production alerts → Slack + Email
        - match:
            severity: warning
            environment: production
          receiver: 'slack-ops'
          continue: true
        
        - match:
            severity: warning
            environment: production
          receiver: 'email-ops'
        
        # Staging alerts → Slack only
        - match:
            environment: staging
          receiver: 'slack-staging'
        
        # Test environment → Slack dev channel
        - match:
            environment: test
          receiver: 'slack-dev'
        
        # Dev environment → Suppressed (logged only)
        - match:
            environment: dev
          receiver: 'null'
    
    # Receiver definitions
    receivers:
      - name: 'default'
        webhook_configs:
          - url: 'http://health-agent-logger:8080/webhook/default'
      
      - name: 'pagerduty-critical'
        pagerduty_configs:
          - service_key: '{{ PAGERDUTY_SERVICE_KEY }}'
            severity: 'critical'
            description: '{{ .GroupLabels.alertname }}: {{ .CommonAnnotations.summary }}'
            details:
              firing: '{{ .Alerts.Firing | len }}'
              resolved: '{{ .Alerts.Resolved | len }}'
      
      - name: 'slack-critical'
        slack_configs:
          - channel: '#ops-critical'
            title: '🚨 CRITICAL: {{ .GroupLabels.alertname }}'
            text: |-
              *Environment:* {{ .GroupLabels.environment }}
              *Service:* {{ .GroupLabels.service }}
              *Summary:* {{ .CommonAnnotations.summary }}
              *Description:* {{ .CommonAnnotations.description }}
            color: 'danger'
            send_resolved: true
      
      - name: 'slack-ops'
        slack_configs:
          - channel: '#ops-alerts'
            title: '⚠️ Warning: {{ .GroupLabels.alertname }}'
            text: '{{ .CommonAnnotations.summary }}'
            color: 'warning'
            send_resolved: true
      
      - name: 'slack-staging'
        slack_configs:
          - channel: '#staging-alerts'
            title: '{{ .GroupLabels.alertname }}'
            text: '{{ .CommonAnnotations.summary }}'
      
      - name: 'slack-dev'
        slack_configs:
          - channel: '#dev-alerts'
            title: '{{ .GroupLabels.alertname }}'
            text: '{{ .CommonAnnotations.summary }}'
      
      - name: 'email-oncall'
        email_configs:
          - to: 'oncall-primary@ecoskiller.com,oncall-secondary@ecoskiller.com'
            from: 'alerts@ecoskiller.com'
            subject: 'CRITICAL: {{ .GroupLabels.alertname }}'
            html: |
              <h2>Critical Alert</h2>
              <p><strong>Summary:</strong> {{ .CommonAnnotations.summary }}</p>
              <p><strong>Description:</strong> {{ .CommonAnnotations.description }}</p>
              <p><strong>Environment:</strong> {{ .GroupLabels.environment }}</p>
              <p><strong>Service:</strong> {{ .GroupLabels.service }}</p>
      
      - name: 'email-ops'
        email_configs:
          - to: 'devops-team@ecoskiller.com'
            from: 'alerts@ecoskiller.com'
            subject: 'Warning: {{ .GroupLabels.alertname }}'
      
      - name: 'null'
        webhook_configs:
          - url: 'http://localhost:9099/null'
    
    # Alert inhibition rules
    inhibit_rules:
      # Inhibit warning alerts if critical alert is firing
      - source_match:
          severity: 'critical'
        target_match:
          severity: 'warning'
        equal: ['alertname', 'cluster', 'service']
      
      # Inhibit pod alerts if node is down
      - source_match:
          alertname: 'KubernetesNodeNotReady'
        target_match_re:
          alertname: 'Pod.*'
        equal: ['node']
```

---

## 🔒 SECTION 8 — GRAFANA DASHBOARD SPECIFICATIONS

### A. Infrastructure Overview Dashboard

```json
{
  "dashboard": {
    "title": "ECOSKILLER Infrastructure Health - Overview",
    "uid": "infra-overview",
    "panels": [
      {
        "title": "Cluster Health Score",
        "type": "gauge",
        "targets": [
          {
            "expr": "avg(up{job=\"kubernetes-nodes\"}) * 100"
          }
        ],
        "thresholds": [
          { "value": 95, "color": "green" },
          { "value": 90, "color": "yellow" },
          { "value": 0, "color": "red" }
        ]
      },
      {
        "title": "Node Status",
        "type": "stat",
        "targets": [
          {
            "expr": "count(kube_node_status_condition{condition=\"Ready\",status=\"true\"})"
          }
        ]
      },
      {
        "title": "Pod Health",
        "type": "timeseries",
        "targets": [
          {
            "expr": "sum(kube_pod_status_phase{phase=\"Running\"}) by (namespace)",
            "legendFormat": "{{namespace}}"
          }
        ]
      },
      {
        "title": "Active Alerts",
        "type": "table",
        "targets": [
          {
            "expr": "ALERTS{alertstate=\"firing\"}"
          }
        ]
      },
      {
        "title": "CPU Usage by Node",
        "type": "heatmap",
        "targets": [
          {
            "expr": "100 - (avg by (node) (irate(node_cpu_seconds_total{mode=\"idle\"}[5m])) * 100)"
          }
        ]
      },
      {
        "title": "Memory Usage by Node",
        "type": "heatmap",
        "targets": [
          {
            "expr": "100 * (1 - (node_memory_MemAvailable_bytes / node_memory_MemTotal_bytes))"
          }
        ]
      }
    ]
  }
}
```

### B. Database Health Dashboard

```json
{
  "dashboard": {
    "title": "ECOSKILLER Database Health",
    "uid": "database-health",
    "panels": [
      {
        "title": "PostgreSQL Connection Pool",
        "type": "timeseries",
        "targets": [
          {
            "expr": "pg_stat_database_numbackends",
            "legendFormat": "Active Connections"
          },
          {
            "expr": "pg_settings_max_connections",
            "legendFormat": "Max Connections"
          }
        ]
      },
      {
        "title": "Replication Lag",
        "type": "timeseries",
        "targets": [
          {
            "expr": "pg_replication_lag_seconds",
            "legendFormat": "{{replica}}"
          }
        ]
      },
      {
        "title": "Query Performance (P95)",
        "type": "timeseries",
        "targets": [
          {
            "expr": "histogram_quantile(0.95, sum(rate(pg_stat_statements_total_time_seconds_bucket[5m])) by (le))"
          }
        ]
      },
      {
        "title": "Redis Memory Usage",
        "type": "gauge",
        "targets": [
          {
            "expr": "redis_memory_used_bytes / redis_memory_max_bytes * 100"
          }
        ]
      },
      {
        "title": "OpenSearch Cluster Status",
        "type": "stat",
        "targets": [
          {
            "expr": "opensearch_cluster_status"
          }
        ],
        "valueMappings": [
          { "value": 0, "text": "RED", "color": "red" },
          { "value": 1, "text": "YELLOW", "color": "yellow" },
          { "value": 2, "text": "GREEN", "color": "green" }
        ]
      }
    ]
  }
}
```

### C. Application Services Dashboard

```json
{
  "dashboard": {
    "title": "ECOSKILLER Application Services",
    "uid": "app-services",
    "panels": [
      {
        "title": "Request Rate by Service",
        "type": "timeseries",
        "targets": [
          {
            "expr": "sum(rate(http_requests_total[5m])) by (service)",
            "legendFormat": "{{service}}"
          }
        ]
      },
      {
        "title": "Error Rate by Service",
        "type": "timeseries",
        "targets": [
          {
            "expr": "sum(rate(http_requests_total{status=~\"5..\"}[5m])) by (service) / sum(rate(http_requests_total[5m])) by (service) * 100",
            "legendFormat": "{{service}}"
          }
        ]
      },
      {
        "title": "P95 Latency by Service",
        "type": "timeseries",
        "targets": [
          {
            "expr": "histogram_quantile(0.95, sum(rate(http_request_duration_seconds_bucket[5m])) by (service, le))",
            "legendFormat": "{{service}}"
          }
        ]
      },
      {
        "title": "JWT Validation Rate",
        "type": "stat",
        "targets": [
          {
            "expr": "rate(auth_jwt_validations_total[5m])"
          }
        ]
      },
      {
        "title": "Active WebSocket Connections",
        "type": "timeseries",
        "targets": [
          {
            "expr": "websocket_active_connections"
          }
        ]
      }
    ]
  }
}
```

---

## 🔒 SECTION 9 — HEALTH CHECK AUDIT TRAIL

### A. Audit Log Schema

```sql
-- PostgreSQL Audit Table
CREATE TABLE infrastructure_health_audit (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    timestamp TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    environment TEXT NOT NULL,
    check_type TEXT NOT NULL,
    component TEXT NOT NULL,
    health_status TEXT NOT NULL CHECK (health_status IN ('healthy', 'degraded', 'unhealthy', 'critical', 'failed')),
    previous_status TEXT,
    alert_triggered BOOLEAN DEFAULT FALSE,
    alert_name TEXT,
    alert_severity TEXT,
    remediation_attempted BOOLEAN DEFAULT FALSE,
    remediation_action TEXT,
    remediation_success BOOLEAN,
    human_escalation BOOLEAN DEFAULT FALSE,
    metadata JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Indexes for performance
CREATE INDEX idx_health_audit_timestamp ON infrastructure_health_audit (timestamp DESC);
CREATE INDEX idx_health_audit_environment ON infrastructure_health_audit (environment);
CREATE INDEX idx_health_audit_component ON infrastructure_health_audit (component);
CREATE INDEX idx_health_audit_status ON infrastructure_health_audit (health_status);
CREATE INDEX idx_health_audit_alert ON infrastructure_health_audit (alert_triggered) WHERE alert_triggered = TRUE;

-- Partition by month for scalability
CREATE TABLE infrastructure_health_audit_2026_03 PARTITION OF infrastructure_health_audit
    FOR VALUES FROM ('2026-03-01') TO ('2026-04-01');
```

### B. Audit Event Examples

```json
// Example 1: Pod crash detected and recovered
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "timestamp": "2026-03-04T10:15:23Z",
  "environment": "production",
  "check_type": "kubernetes_pod_health",
  "component": "auth-service",
  "health_status": "unhealthy",
  "previous_status": "healthy",
  "alert_triggered": true,
  "alert_name": "PodCrashLooping",
  "alert_severity": "warning",
  "remediation_attempted": true,
  "remediation_action": "kubectl rollout restart deployment/auth-service",
  "remediation_success": true,
  "human_escalation": false,
  "metadata": {
    "pod_name": "auth-service-7d8f9b6c5-k2j9x",
    "namespace": "core",
    "restart_count": 3,
    "last_exit_code": 137,
    "remediation_duration_seconds": 45
  }
}

// Example 2: Database replication lag requiring human intervention
{
  "id": "b2c3d4e5-f6a7-8901-bcde-f23456789012",
  "timestamp": "2026-03-04T14:22:10Z",
  "environment": "production",
  "check_type": "database_replication",
  "component": "postgresql-replica-01",
  "health_status": "critical",
  "previous_status": "degraded",
  "alert_triggered": true,
  "alert_name": "PostgreSQLReplicationLag",
  "alert_severity": "critical",
  "remediation_attempted": false,
  "remediation_action": null,
  "remediation_success": null,
  "human_escalation": true,
  "metadata": {
    "replication_lag_seconds": 127,
    "wal_receiver_status": "streaming",
    "replica_server": "postgresql-replica-01.ecoskiller.internal",
    "pagerduty_incident_id": "Q1XYZABC123"
  }
}

// Example 3: TLS certificate auto-renewed successfully
{
  "id": "c3d4e5f6-a7b8-9012-cdef-34567890123",
  "timestamp": "2026-03-04T02:00:05Z",
  "environment": "production",
  "check_type": "security_certificate",
  "component": "api.ecoskiller.com",
  "health_status": "healthy",
  "previous_status": "degraded",
  "alert_triggered": true,
  "alert_name": "TLSCertificateExpiringWithin30Days",
  "alert_severity": "warning",
  "remediation_attempted": true,
  "remediation_action": "cert-manager automatic renewal",
  "remediation_success": true,
  "human_escalation": false,
  "metadata": {
    "certificate_cn": "api.ecoskiller.com",
    "old_expiry": "2026-03-15T00:00:00Z",
    "new_expiry": "2026-06-04T00:00:00Z",
    "issuer": "letsencrypt-prod"
  }
}
```

---

## 🔒 SECTION 10 — INTEGRATION WITH CI/CD PIPELINE

### A. Pre-Deployment Health Gate

```yaml
# GitLab CI/CD Health Check Gate
stages:
  - build
  - test
  - health_check
  - deploy

production_health_check:
  stage: health_check
  image: curlimages/curl:latest
  only:
    - production
  script:
    # 1. Check cluster health
    - |
      CLUSTER_HEALTH=$(curl -s http://prometheus.monitoring.svc:9090/api/v1/query \
        --data-urlencode 'query=avg(up{job="kubernetes-nodes"})' | jq -r '.data.result[0].value[1]')
      
      if (( $(echo "$CLUSTER_HEALTH < 0.95" | bc -l) )); then
        echo "ERROR: Cluster health below 95% ($CLUSTER_HEALTH)"
        exit 1
      fi
    
    # 2. Check active critical alerts
    - |
      CRITICAL_ALERTS=$(curl -s http://prometheus.monitoring.svc:9090/api/v1/query \
        --data-urlencode 'query=count(ALERTS{alertstate="firing",severity="critical"})' | jq -r '.data.result[0].value[1]')
      
      if [ "$CRITICAL_ALERTS" != "null" ] && [ "$CRITICAL_ALERTS" -gt 0 ]; then
        echo "ERROR: $CRITICAL_ALERTS critical alerts firing"
        exit 1
      fi
    
    # 3. Check database replication lag
    - |
      REPLICATION_LAG=$(curl -s http://prometheus.monitoring.svc:9090/api/v1/query \
        --data-urlencode 'query=max(pg_replication_lag_seconds)' | jq -r '.data.result[0].value[1]')
      
      if (( $(echo "$REPLICATION_LAG > 5" | bc -l) )); then
        echo "ERROR: Database replication lag >5s ($REPLICATION_LAG)"
        exit 1
      fi
    
    # 4. Check error rate across services
    - |
      ERROR_RATE=$(curl -s http://prometheus.monitoring.svc:9090/api/v1/query \
        --data-urlencode 'query=sum(rate(http_requests_total{status=~"5.."}[5m])) / sum(rate(http_requests_total[5m]))' | jq -r '.data.result[0].value[1]')
      
      if (( $(echo "$ERROR_RATE > 0.01" | bc -l) )); then
        echo "ERROR: Error rate >1% ($ERROR_RATE)"
        exit 1
      fi
    
    - echo "✅ All health checks passed - deployment authorized"

deploy_production:
  stage: deploy
  only:
    - production
  needs:
    - production_health_check
  script:
    - kubectl apply -f k8s/production/
    - kubectl rollout status deployment/auth-service -n core
```

### B. Post-Deployment Health Verification

```yaml
production_health_verification:
  stage: verify
  only:
    - production
  needs:
    - deploy_production
  script:
    # Wait for deployment to stabilize
    - sleep 60
    
    # 1. Verify all pods are running
    - |
      PENDING_PODS=$(kubectl get pods -n core --field-selector=status.phase!=Running -o json | jq '.items | length')
      if [ "$PENDING_PODS" -gt 0 ]; then
        echo "ERROR: $PENDING_PODS pods not running"
        kubectl get pods -n core
        exit 1
      fi
    
    # 2. Verify readiness probes passing
    - |
      NOT_READY=$(kubectl get pods -n core -o json | jq '[.items[] | select(.status.conditions[] | select(.type=="Ready" and .status!="True"))] | length')
      if [ "$NOT_READY" -gt 0 ]; then
        echo "ERROR: $NOT_READY pods not ready"
        kubectl get pods -n core -o wide
        exit 1
      fi
    
    # 3. Smoke test critical endpoints
    - |
      curl -f https://api.ecoskiller.com/health || exit 1
      curl -f https://app.ecoskiller.com || exit 1
    
    # 4. Verify no new alerts triggered
    - sleep 120  # Wait for metrics to update
    - |
      NEW_ALERTS=$(curl -s http://prometheus.monitoring.svc:9090/api/v1/query \
        --data-urlencode 'query=count(ALERTS{alertstate="firing"} and changes(ALERTS[5m]) > 0)' | jq -r '.data.result[0].value[1]')
      
      if [ "$NEW_ALERTS" != "null" ] && [ "$NEW_ALERTS" -gt 0 ]; then
        echo "WARNING: $NEW_ALERTS new alerts triggered after deployment"
        # Don't fail, but notify
      fi
    
    - echo "✅ Post-deployment health verification passed"
  
  allow_failure: false
```

---

## 🔒 SECTION 11 — INCIDENT RESPONSE INTEGRATION

### A. Runbook Auto-Generation

When an alert fires, the health agent automatically generates a runbook:

```markdown
# Incident Runbook: PostgreSQLReplicationLag

**Alert:** PostgreSQLReplicationLag  
**Severity:** Critical  
**Triggered:** 2026-03-04 14:22:10 UTC  
**Environment:** Production  
**Component:** postgresql-replica-01  

---

## Current Status

- **Replication Lag:** 127 seconds
- **WAL Receiver Status:** Streaming
- **Primary Server:** postgresql-primary.core.svc
- **Replica Server:** postgresql-replica-01.core.svc

---

## Immediate Actions (First 5 Minutes)

1. **Verify network connectivity:**
   ```bash
   kubectl exec -n core postgresql-replica-01-0 -- ping -c 3 postgresql-primary.core.svc
   ```

2. **Check replica logs:**
   ```bash
   kubectl logs -n core postgresql-replica-01-0 --tail=100 | grep -i error
   ```

3. **Check primary server load:**
   ```bash
   kubectl top pod -n core postgresql-primary-0
   ```

4. **Verify WAL streaming:**
   ```bash
   kubectl exec -n core postgresql-primary-0 -- psql -U postgres -c "SELECT * FROM pg_stat_replication;"
   ```

---

## Investigation Steps (5-15 Minutes)

5. **Check for long-running transactions on primary:**
   ```sql
   SELECT pid, now() - pg_stat_activity.query_start AS duration, query, state
   FROM pg_stat_activity
   WHERE state != 'idle'
   ORDER BY duration DESC LIMIT 10;
   ```

6. **Check for replication slot issues:**
   ```sql
   SELECT slot_name, active, restart_lsn, confirmed_flush_lsn
   FROM pg_replication_slots;
   ```

7. **Verify disk I/O on replica:**
   ```bash
   kubectl exec -n core postgresql-replica-01-0 -- iostat -x 1 5
   ```

---

## Resolution Options

### Option A: Restart WAL Receiver (Low Risk)
```bash
kubectl exec -n core postgresql-replica-01-0 -- \
  psql -U postgres -c "SELECT pg_wal_replay_pause();"

kubectl exec -n core postgresql-replica-01-0 -- \
  psql -U postgres -c "SELECT pg_wal_replay_resume();"
```

### Option B: Rebuild Replica from Backup (High Risk - Data Loss Possible)
**⚠️ REQUIRES APPROVAL FROM DATABASE TEAM**
```bash
# Stop replica
kubectl scale statefulset postgresql-replica-01 -n core --replicas=0

# Remove old data
kubectl exec -n core postgresql-replica-01-0 -- rm -rf /var/lib/postgresql/data/*

# Restore from primary using pg_basebackup
kubectl exec -n core postgresql-replica-01-0 -- \
  pg_basebackup -h postgresql-primary.core.svc -D /var/lib/postgresql/data -U replication -Fp -Xs -P

# Restart replica
kubectl scale statefulset postgresql-replica-01 -n core --replicas=1
```

---

## Escalation Path

1. **First 10 minutes:** DevOps on-call engineer
2. **After 10 minutes:** Database team + Platform lead
3. **After 30 minutes:** CTO + Incident commander

---

## Post-Incident Actions

- [ ] Update replication monitoring thresholds if needed
- [ ] Review primary database query performance
- [ ] Consider scaling primary resources
- [ ] Document root cause in post-mortem
```

---

## 🔒 SECTION 12 — COST OPTIMIZATION HEALTH CHECKS

### A. Resource Waste Detection

```yaml
cost_optimization_alerts:
  
  - alert: UnderutilizedNodes
    expr: |
      avg(100 - (avg by (node) (irate(node_cpu_seconds_total{mode="idle"}[1h])) * 100)) < 30
      and
      avg(100 * (1 - (node_memory_MemAvailable_bytes / node_memory_MemTotal_bytes))) < 40
    for: 24h
    labels:
      severity: info
      category: cost_optimization
    annotations:
      summary: "Node {{ $labels.node }} underutilized for 24h"
      recommendation: "Consider reducing node count or instance size"
  
  - alert: OverProvisionedPods
    expr: |
      max_over_time(container_memory_working_set_bytes[24h]) / 
      kube_pod_container_resource_requests{resource="memory"} < 0.5
    for: 7d
    labels:
      severity: info
      category: cost_optimization
    annotations:
      summary: "Pod {{ $labels.namespace }}/{{ $labels.pod }} using <50% of requested memory"
  
  - alert: UnusedPersistentVolumes
    expr: |
      kube_persistentvolumeclaim_status_phase{phase="Bound"} == 0
    for: 168h  # 7 days
    labels:
      severity: info
      category: cost_optimization
    annotations:
      summary: "PVC {{ $labels.namespace }}/{{ $labels.persistentvolumeclaim }} not bound for 7 days"
```

---

## 🔒 SECTION 13 — COMPLIANCE & GOVERNANCE HEALTH CHECKS

### A. Security Compliance Monitoring

```yaml
compliance_health_checks:
  
  # Ensure all pods run as non-root
  - check: pods_running_as_root
    query: |
      count(kube_pod_container_info{container!="",container_id!=""} 
        unless on(namespace, pod, container) 
        kube_pod_container_security_context_run_as_non_root == 1)
    threshold: 0
    severity: critical
  
  # Verify network policies exist for all namespaces
  - check: namespaces_without_network_policies
    query: |
      count(kube_namespace_created) 
      - 
      count(kube_networkpolicy_created)
    threshold: 0
    severity: high
  
  # Check for secrets not using encryption at rest
  - check: unencrypted_secrets
    query: |
      kube_secret_type{type!="kubernetes.io/service-account-token"} 
      unless on(namespace, secret) 
      kube_secret_annotations{annotation="encrypted-at-rest"}
    threshold: 0
    severity: critical
  
  # Verify resource limits set on all pods
  - check: pods_without_resource_limits
    query: |
      count(kube_pod_container_info{container!=""} 
        unless on(namespace, pod, container) 
        kube_pod_container_resource_limits)
    threshold: 0
    severity: medium
```

---

## 🔒 SECTION 14 — FINAL ENFORCEMENT & GOVERNANCE

### A. Health Agent Execution Contract

```yaml
health_agent_contract:
  version: "1.0"
  
  mandatory_capabilities:
    - continuous_infrastructure_monitoring
    - real_time_alert_generation
    - automated_safe_remediation
    - human_escalation_for_critical
    - immutable_audit_logging
    - multi_environment_support
    - compliance_verification
  
  prohibited_behaviors:
    - suppressing_critical_alerts
    - auto_remediation_without_safety_check
    - modifying_production_without_approval
    - allowing_partial_unhealthy_deployments
  
  failure_modes:
    detect: immediate
    alert: within_10_seconds
    remediate: within_2_minutes (if safe)
    escalate: within_5_minutes (if unsafe)
  
  audit_requirements:
    retention: 2_years
    immutability: enforced
    searchability: full_text_and_metadata
    compliance: SOC2_GDPR_HIPAA_ready
```

### B. Production Readiness Certification

```
PHONE_INFRA_HEALTH_CHECK_AGENT may only be declared 
PRODUCTION-READY when ALL conditions are met:

✅ Prometheus deployed and scraping all targets
✅ Grafana dashboards created for all layers
✅ AlertManager configured with routing rules
✅ PagerDuty integration tested
✅ Slack integration tested  
✅ Email notifications tested
✅ Automated remediation playbooks deployed
✅ Health check probes on all services
✅ Audit trail database created
✅ Multi-environment thresholds configured
✅ CI/CD health gates active
✅ Incident runbook auto-generation working
✅ Cost optimization alerts enabled
✅ Security compliance checks active
✅ All critical path checks passing
✅ Human escalation paths documented
✅ 7-day monitoring data retention verified

Absence of ANY requirement above:
→ STOP EXECUTION  
→ REPORT INCOMPLETE HEALTH SYSTEM  
→ NO PRODUCTION DEPLOYMENT PERMITTED
```

---

## 🔒 SECTION 15 — SEALED & LOCKED

**Artifact Status:** PRODUCTION-READY · DETERMINISTIC · GOVERNANCE-ENFORCED  
**Version:** 1.0  
**Last Updated:** 2026-03-04  
**Modification Policy:** Add-only via version bump  
**Interpretation Authority:** NONE  
**Execution Authority:** Automated with human override capability  

This document defines the complete, deterministic, and enforceable infrastructure health monitoring system for the ECOSKILLER multi-tenant SaaS platform. No deployment may proceed to production without full health agent operational status.

**END OF SPECIFICATION**

---

## 📋 APPENDIX A — INTERN EXECUTION CHECKLIST

### Step-by-Step Deployment for Interns

#### Phase 1: Install Monitoring Stack (30 minutes)
```bash
# 1. Create monitoring namespace
kubectl create namespace monitoring

# 2. Deploy Prometheus
kubectl apply -f infra/monitoring/prometheus/

# 3. Deploy Grafana
kubectl apply -f infra/monitoring/grafana/

# 4. Deploy AlertManager
kubectl apply -f infra/monitoring/alertmanager/

# 5. Verify all pods running
kubectl get pods -n monitoring
```

#### Phase 2: Configure Data Sources (15 minutes)
```bash
# 1. Access Grafana
kubectl port-forward -n monitoring svc/grafana 3000:3000

# 2. Login (admin / <password-from-secret>)

# 3. Add Prometheus data source
# URL: http://prometheus.monitoring.svc:9090

# 4. Import dashboards from infra/monitoring/dashboards/
```

#### Phase 3: Test Alerting (15 minutes)
```bash
# 1. Trigger test alert
kubectl run test-alert --image=busybox --restart=Never -- sh -c "exit 1"

# 2. Verify alert appears in Grafana
# 3. Verify Slack notification received
# 4. Clean up test pod
kubectl delete pod test-alert
```

#### Phase 4: Enable Health Probes (20 minutes)
```bash
# 1. Update all service deployments with health probes
# (Already done if using provided manifests)

# 2. Verify probes working
kubectl describe pod <pod-name> -n core | grep -A 10 "Liveness\|Readiness"
```

**Expected Result:** Full monitoring stack operational, alerts flowing, health probes active.

---

**END OF DOCUMENT**
