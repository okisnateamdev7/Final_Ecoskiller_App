# mcp-admin-service

**Ecoskiller | CAT-ADMIN — Platform Administration, Compliance & Operations**  
MCP Server in **Java 17** | **37 Tools** | Priority: **HIGH** | Security: **Production-grade**

---

## Overview

`mcp-admin-service` is a secure Java MCP server for the Ecoskiller platform's
administrative control plane. It provides Claude (or any MCP client) with tools
to manage recruiters, process DPDPA 2023 compliance requests, query audit logs,
manage questions, control scoring models, monitor system health, and handle
billing — all with Keycloak JWT authentication, RBAC enforcement, and an
immutable audit trail.

---

## Tools (37)

### Recruiter Management (7)
| Tool | Required Role |
|------|--------------|
| `recruiter_create` | SUPER_ADMIN |
| `recruiter_list` | SUPER_ADMIN, FINANCE_MANAGER |
| `recruiter_get` | SUPER_ADMIN, FINANCE_MANAGER |
| `recruiter_update` | SUPER_ADMIN |
| `recruiter_suspend` | SUPER_ADMIN |
| `recruiter_delete` | SUPER_ADMIN |
| `recruiter_quota_adjust` | SUPER_ADMIN, FINANCE_MANAGER |

### DPDPA 2023 Compliance (6)
| Tool | Required Role |
|------|--------------|
| `dsar_create` | SUPER_ADMIN, COMPLIANCE_OFFICER |
| `dsar_list` | SUPER_ADMIN, COMPLIANCE_OFFICER |
| `dsar_get` | SUPER_ADMIN, COMPLIANCE_OFFICER |
| `dsar_complete` | SUPER_ADMIN, COMPLIANCE_OFFICER |
| `erasure_create` | SUPER_ADMIN, COMPLIANCE_OFFICER |
| `erasure_list` | SUPER_ADMIN, COMPLIANCE_OFFICER |

### Audit Log (1)
| Tool | Required Role |
|------|--------------|
| `audit_log_query` | SUPER_ADMIN, COMPLIANCE_OFFICER |

### Questions & Content (6)
| Tool | Required Role |
|------|--------------|
| `question_list` | SUPER_ADMIN, CONTENT_MANAGER |
| `question_create` | SUPER_ADMIN, CONTENT_MANAGER |
| `question_update` | SUPER_ADMIN, CONTENT_MANAGER |
| `question_delete` | SUPER_ADMIN, CONTENT_MANAGER |
| `question_ab_test_create` | SUPER_ADMIN, CONTENT_MANAGER |
| `question_analytics` | SUPER_ADMIN, CONTENT_MANAGER |

### Scoring Models (4)
| Tool | Required Role |
|------|--------------|
| `model_list` | SUPER_ADMIN, OPS_LEAD |
| `model_metrics` | SUPER_ADMIN, OPS_LEAD |
| `model_promote` | SUPER_ADMIN |
| `model_rollback` | SUPER_ADMIN |

### Score Override (1)
| Tool | Required Role |
|------|--------------|
| `score_override` | SUPER_ADMIN |

### System Health (5)
| Tool | Required Role |
|------|--------------|
| `health_services` | SUPER_ADMIN, OPS_LEAD |
| `health_kafka` | SUPER_ADMIN, OPS_LEAD |
| `health_database` | SUPER_ADMIN, OPS_LEAD |
| `health_kubernetes` | SUPER_ADMIN, OPS_LEAD |
| `service_logs` | SUPER_ADMIN, OPS_LEAD |

### Alerts & Configuration (4)
| Tool | Required Role |
|------|--------------|
| `alert_list` | SUPER_ADMIN, OPS_LEAD |
| `alert_rules_list` | SUPER_ADMIN, OPS_LEAD |
| `alert_rule_create` | SUPER_ADMIN, OPS_LEAD |
| `alert_rule_update` | SUPER_ADMIN, OPS_LEAD |

### Reports & Exports (5)
| Tool | Required Role |
|------|--------------|
| `report_generate` | SUPER_ADMIN, FINANCE_MANAGER |
| `report_recruiter_performance` | SUPER_ADMIN, FINANCE_MANAGER |
| `report_assessment_metrics` | SUPER_ADMIN |
| `report_compliance` | SUPER_ADMIN, COMPLIANCE_OFFICER |
| `data_export` | SUPER_ADMIN, COMPLIANCE_OFFICER |

### Billing (1)
| Tool | Required Role |
|------|--------------|
| `invoice_adjust` | SUPER_ADMIN, FINANCE_MANAGER |

---

## Security Architecture

### JWT Authentication (Keycloak OIDC)
- Every request must carry `_meta.authorization: Bearer <jwt>`
- Token is validated: expiry (`exp`), issuer (`iss`), audience (`aud: admin-service`)
- Roles extracted from Keycloak `realm_access.roles`
- **MFA required** for SUPER_ADMIN role (`acr` claim checked)
- Full JWKS signature verification via `nimbus-jose-jwt` (wire in before prod)

### Role-Based Access Control (RBAC)
Five admin roles — no privilege escalation:
- `SUPER_ADMIN` — full access
- `COMPLIANCE_OFFICER` — DPDPA/audit only
- `CONTENT_MANAGER` — questions only
- `OPS_LEAD` — health/alerts only
- `FINANCE_MANAGER` — billing/revenue only

### Rate Limiting
- 100 requests/60 seconds per `admin_id`
- Returns `SecurityException` on breach (logged as security event)

### Immutable Audit Trail
- Every tool call logged: `admin_id`, `tool`, `args (redacted)`, `timestamp`
- Compliance actions additionally log: `resource_type`, `old_value`, `new_value`, `reason`
- Written to stdout (Loki) and `audit.log` file (append-only)
- Forward to ClickHouse `admin_action_log` table for 7+ year retention

### Input Sanitisation
- All string inputs stripped of SQL injection patterns, XSS, null bytes
- Truncated to 10,000 chars max
- Sensitive fields (`password`, `token`, `secret`, etc.) redacted from logs

---

## Requirements

- **Java 17+**
- **Maven 3.8+** (build only)
- No runtime dependencies beyond `org.json:json:20240303`

---

## Build

```bash
mvn package -DskipTests
# → target/mcp-admin-service-1.0.0.jar  (~2MB fat JAR)
```

---

## Run

```bash
# Dev mode (no JWT signature validation)
JWT_STRICT_SIGNATURE=false java -jar target/mcp-admin-service-1.0.0.jar

# Production mode
KEYCLOAK_ISSUER=https://keycloak.ecoskiller.internal/realms/ecoskiller \
JWT_STRICT_SIGNATURE=true \
java -jar target/mcp-admin-service-1.0.0.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-admin-service": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-admin-service/target/mcp-admin-service-1.0.0.jar"],
      "env": {
        "JWT_STRICT_SIGNATURE": "false",
        "AUDIT_LOG_FILE": "/tmp/ecoskiller-admin-audit.log"
      }
    }
  }
}
```

---

## Run Tests

```bash
# Build first
mvn package -DskipTests

# Run test suite
python3 test_admin_server.py           # pass/fail summary
python3 test_admin_server.py --verbose # full JSON output
```

---

## Docker

```bash
# Build
docker build -t mcp-admin-service:1.0.0 .

# Run (dev mode)
docker run -e JWT_STRICT_SIGNATURE=false mcp-admin-service:1.0.0
```

---

## Kubernetes (k3s / GCP asia-south1)

```bash
# Deploy with Helm
helm upgrade --install mcp-admin-service ./helm \
  -f helm-values.yaml \
  -n ops \
  --create-namespace
```

---

## File Structure

```
mcp-admin-service/
├── pom.xml                                   ← Maven build (Java 17, fat JAR)
├── Dockerfile                                ← Multi-stage, non-root
├── helm-values.yaml                          ← Kubernetes deployment config
├── claude_desktop_config.json                ← Claude Desktop config snippet
├── test_admin_server.py                      ← Test all 37 tools
├── README.md
└── src/main/java/com/ecoskiller/admin/mcp/
    ├── AdminMcpServer.java                   ← Main server (dispatch + security gate)
    ├── ToolHandler.java                      ← Tool interface with RBAC contract
    ├── security/
    │   ├── JwtValidator.java                 ← Keycloak JWT validation + role extraction
    │   ├── RateLimiter.java                  ← Per-admin token-bucket rate limiting
    │   └── AuditLogger.java                  ← Immutable audit trail (stdout + file)
    └── tools/
        ├── BaseTool.java                     ← Abstract base (response helpers, schema builders)
        ├── RecruiterCreateTool.java
        ├── RecruiterListTool.java
        ├── RecruiterGetTool.java
        ├── RecruiterUpdateTool.java
        ├── RecruiterSuspendTool.java
        ├── RecruiterDeleteTool.java
        ├── RecruiterQuotaAdjustTool.java
        ├── DsarCreateTool.java
        ├── DsarListTool.java
        ├── DsarGetTool.java
        ├── DsarCompleteTool.java
        ├── ErasureCreateTool.java
        ├── ErasureListTool.java
        ├── AuditLogQueryTool.java
        ├── QuestionListTool.java
        ├── QuestionCreateTool.java
        ├── QuestionUpdateTool.java
        ├── QuestionDeleteTool.java
        ├── QuestionABTestTool.java
        ├── QuestionAnalyticsTool.java
        ├── ModelListTool.java
        ├── ModelMetricsTool.java
        ├── ModelPromoteTool.java
        ├── ModelRollbackTool.java
        ├── ScoreOverrideTool.java
        ├── HealthServicesTool.java
        ├── HealthKafkaTool.java
        ├── HealthDatabaseTool.java
        ├── HealthKubernetesTool.java
        ├── ServiceLogsTool.java
        ├── AlertListTool.java
        ├── AlertRulesListTool.java
        ├── AlertRuleCreateTool.java
        ├── AlertRuleUpdateTool.java
        ├── ReportGenerateTool.java
        ├── ReportRecruiterPerformanceTool.java
        ├── ReportAssessmentMetricsTool.java
        ├── ReportComplianceTool.java
        ├── DataExportTool.java
        └── InvoiceAdjustTool.java
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Security Checklist (Before Go-Live)

- [ ] Implement JWKS signature verification in `JwtValidator.verifySignature()`
- [ ] Set `JWT_STRICT_SIGNATURE=true` in production
- [ ] Mount Kubernetes secrets (never hardcode credentials)
- [ ] Enable ModSecurity WAF in front of NGINX Ingress
- [ ] Configure Fluent Bit to forward audit.log → ClickHouse
- [ ] Verify MFA enforcement for SUPER_ADMIN logins in Keycloak
- [ ] Run Trivy scan: `trivy image mcp-admin-service:1.0.0`
- [ ] Set up PagerDuty integration for P0 security alerts
- [ ] Review Network Policy (restrict ingress to admin-portal only)
- [ ] Enable Wazuh SIEM integration

---

## DPDPA 2023 Compliance

| Requirement | Implementation |
|-------------|----------------|
| Right to Access (DSAR) | `dsar_create` → generates ZIP export |
| Right to Erasure | `erasure_create` → soft-delete + 7yr encrypted archive |
| Right to Explanation | Calls scoring-engine API for feature importance |
| 30-day SLA | `sla_deadline` field on every DSAR |
| Audit Trail | Immutable ClickHouse log, 7+ years |
| Data Minimisation | PII redacted from audit log fields |
