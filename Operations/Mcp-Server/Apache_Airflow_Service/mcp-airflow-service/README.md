# mcp-airflow-service

**Ecoskiller | CAT-AIRFLOW — Apache Airflow Workflow Orchestration & Scheduled Job Management**  
MCP Server in **Java 17** | **28 Tools** | Priority: **HIGH** | Security: **Production-grade**

---

## Overview

`mcp-airflow-service` is a secure Java MCP server that gives Claude (or any MCP client)
full control over the Ecoskiller Apache Airflow 2.9.0 deployment. It exposes tools to:

- Manage DAGs (list, get, pause, unpause, delete, trigger)
- Inspect DAG runs and task instances
- Read task logs (from MinIO `ecoskiller-airflow-logs`)
- Query XCom values (inter-task data passing)
- Trigger the 5 core Ecoskiller scheduled DAGs
- Publish/replay Kafka events from DAG completions
- Monitor Airflow health, Prometheus metrics, and Kubernetes executor pods
- Inspect Airflow connections and variables (sensitive values masked)

All mutating operations (trigger, pause, clear, set state) are **immutably audited**
and protected by **Keycloak JWT + RBAC**.

---

## Tools (28)

### DAG Management (6)
| Tool | Required Roles |
|------|---------------|
| `dag_list` | OPS_LEAD, SUPER_ADMIN |
| `dag_get` | OPS_LEAD, SUPER_ADMIN |
| `dag_pause` | OPS_LEAD, SUPER_ADMIN |
| `dag_unpause` | OPS_LEAD, SUPER_ADMIN |
| `dag_delete` | **SUPER_ADMIN only** |
| `dag_trigger` | OPS_LEAD, SUPER_ADMIN |

### DAG Runs (3)
| Tool | Required Roles |
|------|---------------|
| `dag_run_list` | OPS_LEAD, SUPER_ADMIN |
| `dag_run_get` | OPS_LEAD, SUPER_ADMIN |
| `dag_run_clear` | OPS_LEAD, SUPER_ADMIN |

### Task Instances (5)
| Tool | Required Roles |
|------|---------------|
| `task_instance_list` | OPS_LEAD, SUPER_ADMIN |
| `task_instance_get` | OPS_LEAD, SUPER_ADMIN |
| `task_instance_log` | OPS_LEAD, SUPER_ADMIN |
| `task_instance_clear` | OPS_LEAD, SUPER_ADMIN |
| `task_instance_set_state` | **SUPER_ADMIN only** |

### XCom (2)
| Tool | Required Roles |
|------|---------------|
| `xcom_get` | OPS_LEAD, SUPER_ADMIN |
| `xcom_list` | OPS_LEAD, SUPER_ADMIN |

### Ecoskiller Scheduled DAGs (5)
| Tool | Schedule | Required Roles |
|------|----------|---------------|
| `billing_cycle_trigger` | `0 0 1 * *` IST (1st of month) | **SUPER_ADMIN only** |
| `analytics_report_weekly_trigger` | `0 6 * * 1` IST (Monday) | OPS_LEAD, SUPER_ADMIN |
| `certificate_expiry_check_trigger` | `0 10 * * *` IST (daily) | OPS_LEAD, SUPER_ADMIN |
| `data_retention_cleanup_trigger` | Last Sunday `23:00` IST | COMPLIANCE_OFFICER, SUPER_ADMIN |
| `database_maintenance_trigger` | `0 2 * * 0` IST (Sunday) | OPS_LEAD, SUPER_ADMIN |

### Kafka Events (2)
| Tool | Required Roles |
|------|---------------|
| `kafka_event_status` | OPS_LEAD, SUPER_ADMIN |
| `kafka_event_publish` | **SUPER_ADMIN only** |

### Observability (3)
| Tool | Required Roles |
|------|---------------|
| `airflow_health` | OPS_LEAD, SUPER_ADMIN |
| `dag_metrics` | OPS_LEAD, SUPER_ADMIN |
| `kubernetes_pod_status` | OPS_LEAD, SUPER_ADMIN |

### Connections & Variables (2)
| Tool | Required Roles |
|------|---------------|
| `connection_list` | OPS_LEAD, SUPER_ADMIN |
| `variable_get` | OPS_LEAD, SUPER_ADMIN |

---

## Ecoskiller DAG Catalog

| DAG | Schedule | Purpose | Kafka Output |
|-----|----------|---------|--------------|
| `ecoskiller_billing_cycle_monthly` | `0 0 1 * *` IST | Read ClickHouse usage → billing-service API → invoices | `billing.cycle.started`, `billing.cycle.completed` |
| `ecoskiller_analytics_report_weekly` | `0 6 * * 1` IST | Query ClickHouse → GD/interview/score metrics | `analytics.report.weekly` |
| `ecoskiller_certificate_expiry_daily` | `0 10 * * *` IST | Scan PostgreSQL certifications → notify expiring belts | `belt.expiring` |
| `ecoskiller_data_retention_cleanup_monthly` | Last Sunday `23:00` IST | DPDPA soft-delete after 3yr retention | `data_retention.cleanup_completed` |
| `ecoskiller_database_maintenance_weekly` | `0 2 * * 0` IST | PostgreSQL VACUUM + ClickHouse OPTIMIZE | — |

---

## Security Architecture

### JWT Authentication (Keycloak OIDC)
- Every request: `_meta.authorization: Bearer <jwt>`
- Validates: `exp`, `iss`, `aud: airflow-service`
- Extracts `realm_access.roles` for RBAC
- MFA enforced for SUPER_ADMIN
- Stub for JWKS signature verification (wire in `nimbus-jose-jwt` before prod)

### Three Roles
```
SUPER_ADMIN        — all tools (DAG delete, billing trigger, Kafka publish, set state)
OPS_LEAD           — operational tools (list/get/pause/trigger/clear/metrics)
COMPLIANCE_OFFICER — data_retention_cleanup_trigger + read-only access
```

### Rate Limiting
- 100 req / 60s per `user_id`

### Immutable Audit Trail
- Every DAG trigger, pause, clear, state change → `logDagOperation()`
- Compliance operations (data retention) → `logComplianceOperation()`
- Written append-only to stderr (Loki) + `airflow-audit.log`
- Forward via Fluent Bit → ClickHouse for long-term retention

### Input Sanitisation
- Path traversal `../` stripped (critical for DAG ID inputs)
- SQL injection patterns blocked
- Null-bytes removed
- XSS patterns stripped
- Max input length: 8,000 chars

---

## Requirements

- **Java 17+**
- **Maven 3.8+** (build only)
- One dependency: `org.json:json:20240303`

---

## Build

```bash
mvn package -DskipTests
# → target/mcp-airflow-service-1.0.0.jar
```

---

## Run

```bash
# Dev mode
JWT_STRICT_SIGNATURE=false java -jar target/mcp-airflow-service-1.0.0.jar

# Production
KEYCLOAK_ISSUER=https://keycloak.ecoskiller.internal/realms/ecoskiller \
JWT_STRICT_SIGNATURE=true \
AUDIT_LOG_FILE=/app/logs/airflow-audit.log \
java -jar target/mcp-airflow-service-1.0.0.jar
```

---

## Connect to Claude Desktop

```json
{
  "mcpServers": {
    "mcp-airflow-service": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/target/mcp-airflow-service-1.0.0.jar"],
      "env": {
        "JWT_STRICT_SIGNATURE": "false",
        "AUDIT_LOG_FILE": "/tmp/airflow-audit.log"
      }
    }
  }
}
```

---

## Tests

```bash
mvn package -DskipTests
python3 test_airflow_server.py           # pass/fail
python3 test_airflow_server.py --verbose # full JSON
```

---

## Docker

```bash
docker build -t mcp-airflow-service:1.0.0 .
docker run -e JWT_STRICT_SIGNATURE=false mcp-airflow-service:1.0.0
```

---

## Kubernetes (ops namespace, same as Airflow)

```bash
helm upgrade --install mcp-airflow-service ./helm \
  -f helm-values.yaml -n ops --create-namespace
```

---

## File Structure

```
mcp-airflow-service/
├── pom.xml
├── Dockerfile
├── helm-values.yaml
├── claude_desktop_config.json
├── test_airflow_server.py
├── README.md
└── src/main/java/com/ecoskiller/airflow/mcp/
    ├── AirflowMcpServer.java             ← Main server
    ├── ToolHandler.java
    ├── security/
    │   ├── JwtValidator.java             ← Keycloak JWT + RBAC
    │   ├── RateLimiter.java              ← 100 req/min per user
    │   └── AuditLogger.java             ← DAG + compliance audit trail
    └── tools/
        ├── BaseTool.java
        ├── DagListTool.java
        ├── DagGetTool.java
        ├── DagPauseTool.java
        ├── DagUnpauseTool.java
        ├── DagDeleteTool.java
        ├── DagTriggerTool.java
        ├── DagRunListTool.java
        ├── DagRunGetTool.java
        ├── DagRunClearTool.java
        ├── TaskInstanceListTool.java
        ├── TaskInstanceGetTool.java
        ├── TaskInstanceLogTool.java
        ├── TaskInstanceClearTool.java
        ├── TaskInstanceSetStateTool.java
        ├── XComGetTool.java
        ├── XComListTool.java
        ├── BillingCycleTriggerTool.java
        ├── AnalyticsReportWeeklyTool.java
        ├── CertificateExpiryCheckTool.java
        ├── DataRetentionCleanupTool.java
        ├── DatabaseMaintenanceTool.java
        ├── KafkaEventStatusTool.java
        ├── KafkaEventPublishTool.java
        ├── AirflowHealthTool.java
        ├── DagMetricsTool.java
        ├── KubernetesPodStatusTool.java
        ├── ConnectionListTool.java
        └── VariableGetTool.java
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Security Checklist (Before Go-Live)

- [ ] Wire in JWKS signature verification in `JwtValidator.verifySignatureStub()`
- [ ] Set `JWT_STRICT_SIGNATURE=true` in production
- [ ] Add `airflow-service` audience to Keycloak client
- [ ] Store Airflow connections as Kubernetes Secrets (never env vars)
- [ ] Restrict `data_retention_cleanup_trigger` dry_run=true default in ops runbooks
- [ ] Run Trivy: `trivy image mcp-airflow-service:1.0.0`
- [ ] Set up PagerDuty alert for billing_cycle DAG failures (P0)
- [ ] Verify audit log forwarding to ClickHouse for DPDPA compliance
