# mcp-wazuh

**Ecoskiller | CAT-08 — Wazuh SIEM Security Monitoring**
MCP Server in Java | 20 Agents | Priority: HIGH | Secure by Design

---

## Agents (20)

| # | Tool Name | Agent | Description |
|---|-----------|-------|-------------|
| 1 | `alert_query` | ALERT_QUERY_AGENT | Query alerts by severity/time/IP/rule/namespace |
| 2 | `alert_get` | ALERT_GET_AGENT | Full detail of a specific alert |
| 3 | `agent_list` | AGENT_LIST_AGENT | List all DaemonSet agents with status filter |
| 4 | `agent_status` | AGENT_STATUS_AGENT | Health/metadata for a specific agent |
| 5 | `agent_restart` | AGENT_RESTART_AGENT | Rate-limited agent restart (RB-04 runbook) |
| 6 | `rule_query` | RULE_QUERY_AGENT | Query 1000+ detection rules by group/level/keyword |
| 7 | `rule_get` | RULE_GET_AGENT | Full rule detail with MITRE ATT&CK mapping |
| 8 | `fim_events` | FIM_EVENTS_AGENT | File Integrity Monitoring events per agent |
| 9 | `vulnerability_scan` | VULNERABILITY_SCAN_AGENT | CVE/CIS vulnerability results per agent |
| 10 | `compliance_report` | COMPLIANCE_REPORT_AGENT | DPDPA/GDPR/PCI-DSS/SOC2/CIS status |
| 11 | `threat_intelligence_check` | THREAT_INTEL_AGENT | IP/hash/MITRE lookup (OTX, MISP, YARA) |
| 12 | `active_response_trigger` | ACTIVE_RESPONSE_AGENT | block_ip / quarantine_pod / revoke_sa_token |
| 13 | `incident_runbook` | INCIDENT_RUNBOOK_AGENT | EcoSkiller runbooks RB-01 to RB-05 |
| 14 | `behavioral_anomaly_query` | BEHAVIORAL_ANOMALY_AGENT | Z-score anomaly detection events |
| 15 | `agent_overview` | AGENT_OVERVIEW_AGENT | Full cluster security dashboard |
| 16 | `kafka_alert_status` | KAFKA_ALERT_STATUS_AGENT | Wazuh → Kafka pipeline health |
| 17 | `forensic_query` | FORENSIC_QUERY_AGENT | Post-incident forensic event search |
| 18 | `manager_health` | MANAGER_HEALTH_AGENT | Wazuh Manager + Elasticsearch health |
| 19 | `false_positive_exception` | FP_EXCEPTION_AGENT | Create/list/delete rule exceptions |
| 20 | `compliance_audit_trail` | AUDIT_TRAIL_AGENT | Regulatory evidence packages (DPDPA/SOC2/PCI/GDPR) |

---

## Requirements

- **Java 17+**
- **Maven 3.8+**
- Wazuh Manager running and accessible (REST API on port 55000)
- Environment variables:
  ```
  WAZUH_API_URL=https://wazuh-manager.ops.svc.cluster.local:55000
  WAZUH_API_TOKEN=<jwt-bearer-token>
  ```

---

## Build

```bash
mvn clean package -DskipTests
# Output: target/mcp-wazuh-1.0.0.jar
```

---

## Run

```bash
export WAZUH_API_URL=https://wazuh-manager.ops.svc.cluster.local:55000
export WAZUH_API_TOKEN=<your-token>
java -jar target/mcp-wazuh-1.0.0.jar
```

Server communicates via **stdin/stdout** (JSON-RPC 2.0). All logging goes to **stderr**.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-wazuh": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-wazuh/target/mcp-wazuh-1.0.0.jar"],
      "env": {
        "WAZUH_API_URL":   "https://wazuh-manager.ops.svc.cluster.local:55000",
        "WAZUH_API_TOKEN": "<your-wazuh-jwt-token>"
      }
    }
  }
}
```

---

## Run tests

```bash
mvn test
# No live Wazuh API required — safety gates tested entirely in Java
```

Tests cover:
- **SEC-01–23**: Input validation, injection blocking, rate-limiting
- **SCHEMA-01–03**: All 20 tools expose valid MCP inputSchema, unique names
- **SAFETY-01–07**: Confirm gates, required fields, action allow-lists
- **INJECT-01–08**: Shell injection in IPs, IDs, namespaces, CVE IDs
- **TOOLS-01–10**: Tool-specific business logic validation

---

## Security Architecture

| Threat | Mitigation |
|--------|-----------|
| **Shell injection in params** | Regex allow-lists for every field type |
| **SSRF via Wazuh URL** | URL must be HTTPS; token comes from env only |
| **Credential injection** | Bearer token read from `WAZUH_API_TOKEN` env — never from user args |
| **IP abuse** | Full IPv4 octet validation (0–255 each) |
| **Severity manipulation** | Allow-list: critical/high/medium/low/info only |
| **Namespace escape** | Allow-list: core/realtime/billing/analytics/admin/ops/media/velero/security |
| **Framework tampering** | Allow-list: dpdpa/gdpr/pci-dss/soc2/cis/hipaa/iso27001 |
| **Mass accidental response** | confirm=true required for restart/block/quarantine/revoke |
| **Rapid destructive calls** | 3-second rate-limit per operation key |
| **Response body DoS** | Max 10 MB API response (auto-truncated) |
| **Hanging API calls** | 30-second timeout on all HTTPS requests |
| **Audit trail** | Every tool call logged to `WAZUH_AUDIT` logger (stderr) |
| **Path traversal** | Pattern blocked in all ID and name fields |

---

## EcoSkiller Context

| Property | Value |
|----------|-------|
| Platform | EcoSkiller k3s — ops namespace |
| SIEM | Wazuh 4.7.0 — Manager + Indexer + Dashboard |
| Deployment | DaemonSet (agents, 1 per node) + StatefulSet (manager + indexer) |
| Alert threshold | severity >= 7 → Kafka topic `security.wazuh_alert` |
| Kafka consumers | notification-service, response-automation-service, analytics-service |
| Event volume | 1M events/day (~12 events/sec avg, 500/sec peak) |
| Storage hot | 30 days (Elasticsearch/Wazuh Indexer SSD) |
| Storage warm | 90 days (MinIO compressed) |
| Storage cold | 1+ year (MinIO archive) |
| Compliance | DPDPA 2023, GDPR, PCI-DSS, SOC2 Type II, CIS Kubernetes v1.7 |
| MTTD target | < 10 seconds |
| MTTR target | < 30 seconds |
| Agent uptime | > 99% (all nodes covered) |
| False-positive | < 0.1% target |

---

## EcoSkiller Incident Runbooks

| ID | Severity | Trigger | MITRE |
|----|----------|---------|-------|
| RB-01 | CRITICAL (10) | kubectl exec as root in production pod | T1609 |
| RB-02 | CRITICAL (9) | Unauthorized candidate PII data access | T1078 |
| RB-03 | HIGH (8) | Brute-force on auth-service (50+ fails/5min) | T1110.001 |
| RB-04 | MEDIUM (5) | Wazuh agent offline > 10 minutes | — |
| RB-05 | INFO | False positive alert tuning (< 0.1% target) | — |

---

## EcoSkiller Custom Rules

| Rule ID | Severity | Trigger |
|---------|----------|---------|
| 100001 | 8 (HIGH) | Failed login auth-service > 5 in 2 min → brute force |
| 100002 | 9 (CRITICAL) | billing-service accesses candidate PII without auth flow |
| 100003 | 10 (CRITICAL) | GD Orchestrator pod exec without RBAC → privilege escalation |
| 100004 | 7 (HIGH) | Kubernetes ServiceAccount token from unusual IP → token theft |

---

## Protocol

- **Transport**: stdio (stdin/stdout)
- **Format**: JSON-RPC 2.0
- **MCP Version**: 2024-11-05
- **Methods**: `initialize`, `tools/list`, `tools/call`, `ping`
