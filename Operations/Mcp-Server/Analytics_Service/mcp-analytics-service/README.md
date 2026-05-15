# mcp-analytics-service

**Ecoskiller | CAT-ANALYTICS — Real-Time Analytics, Dashboards & Business Intelligence**  
MCP Server in **Java 17** | **32 Tools** | Priority: **HIGH** | Security: **Production-grade**

---

## Overview

`mcp-analytics-service` is a secure Java MCP server for the Ecoskiller platform's
analytics and business intelligence layer. It provides Claude (or any MCP client)
with tools to query recruitment funnels, recruiter performance, candidate score
history, platform health, Kafka ingestion status, DPDPA compliance exports, bias
monitoring, and anomaly detection — all with:

- **Keycloak JWT** authentication (OIDC/OAuth2)
- **Role-Based Access Control** (6 roles)
- **Mandatory tenant isolation** (tenant_id injected from JWT on every query)
- **Per-user rate limiting** (120 req/min)
- **Immutable audit trail** (DPDPA 2023 compliance — all data access logged)
- **Input sanitisation** (SQL injection, XSS, null-byte)

---

## Tools (32)

### Dashboard (4)
| Tool | Description | Required Roles |
|------|-------------|----------------|
| `dashboard_get` | Fetch dashboard definition | All authenticated |
| `dashboard_data` | Live dashboard data (Redis-cached 5min) | All authenticated |
| `dashboard_list` | List tenant dashboards | RECRUITER, ANALYTICS_VIEWER, SUPER_ADMIN, OPS_LEAD |
| `dashboard_create` | Create custom dashboard with widgets | ANALYTICS_VIEWER, SUPER_ADMIN |

### Funnel Analytics (3)
| Tool | Description | Required Roles |
|------|-------------|----------------|
| `funnel_job` | Per-job application funnel with drop-off rates | RECRUITER, ANALYTICS_VIEWER, SUPER_ADMIN |
| `funnel_platform` | Platform-wide funnel across all jobs | ANALYTICS_VIEWER, SUPER_ADMIN |
| `funnel_cohort` | Cohort analysis across time/tier/category | ANALYTICS_VIEWER, SUPER_ADMIN |

### Recruiter Analytics (5)
| Tool | Description | Required Roles |
|------|-------------|----------------|
| `recruiter_performance` | KPIs: hire rate, avg score, no-show, time-to-hire | RECRUITER, ANALYTICS_VIEWER, SUPER_ADMIN |
| `recruiter_team_benchmark` | Team benchmarking comparison | ANALYTICS_VIEWER, SUPER_ADMIN |
| `recruiter_noshow_analysis` | No-show patterns & habitual candidate list | RECRUITER, ANALYTICS_VIEWER, SUPER_ADMIN |
| `recruiter_leaderboard` | Top recruiters leaderboard (cached 10min) | ANALYTICS_VIEWER, SUPER_ADMIN |
| `recruiter_time_to_hire` | Time-to-hire with stage bottleneck analysis | RECRUITER, ANALYTICS_VIEWER, SUPER_ADMIN |

### Candidate Analytics (4)
| Tool | Description | Required Roles |
|------|-------------|----------------|
| `candidate_score_history` | Score history across all assessments (DPDPA logged) | RECRUITER, CANDIDATE, ANALYTICS_VIEWER, SUPER_ADMIN |
| `candidate_progress` | Assessment completion & application pipeline | RECRUITER, CANDIDATE, ANALYTICS_VIEWER, SUPER_ADMIN |
| `candidate_score_breakdown` | Dimension breakdown: communication/technical/cultural_fit | RECRUITER, CANDIDATE, ANALYTICS_VIEWER, SUPER_ADMIN |
| `candidate_belt_advancement` | Belt level, progress %, criteria remaining | RECRUITER, CANDIDATE, ANALYTICS_VIEWER, SUPER_ADMIN |

### Platform Health & KPIs (3)
| Tool | Description | Required Roles |
|------|-------------|----------------|
| `platform_health` | Assessments/day, API latency p50/p95/p99, error rate | OPS_LEAD, SUPER_ADMIN |
| `platform_kpi` | Business KPIs: MRR, churn, active recruiters, hires | ANALYTICS_VIEWER, SUPER_ADMIN |
| `platform_model_performance` | Scoring model accuracy, bias metrics | OPS_LEAD, ANALYTICS_VIEWER, SUPER_ADMIN |

### Kafka / Event Ingestion (3)
| Tool | Description | Required Roles |
|------|-------------|----------------|
| `kafka_consumer_lag` | Consumer lag per topic (18 topics, alert >50K) | OPS_LEAD, SUPER_ADMIN |
| `event_ingestion_status` | Pipeline status: events/sec, ClickHouse write latency | OPS_LEAD, SUPER_ADMIN |
| `event_ingestion_history` | Historical ingestion volume for capacity planning | OPS_LEAD, ANALYTICS_VIEWER, SUPER_ADMIN |

### Jobs (2)
| Tool | Description | Required Roles |
|------|-------------|----------------|
| `job_leaderboard` | Top jobs by applications/hires/offer rate | RECRUITER, ANALYTICS_VIEWER, SUPER_ADMIN |
| `job_funnel_daily` | Pre-computed daily funnel metrics per job | RECRUITER, ANALYTICS_VIEWER, SUPER_ADMIN |

### Anomaly Detection & Alerts (3)
| Tool | Description | Required Roles |
|------|-------------|----------------|
| `anomaly_detect` | Detect spikes: no-show >20%, score shift >10%, Kafka lag | OPS_LEAD, ANALYTICS_VIEWER, SUPER_ADMIN |
| `alert_rule_list` | List configured alert rules | OPS_LEAD, SUPER_ADMIN |
| `alert_rule_create` | Create alert: (metric > threshold) → email/Slack/PagerDuty | OPS_LEAD, SUPER_ADMIN |

### Compliance & DPDPA (3)
| Tool | Description | Required Roles |
|------|-------------|----------------|
| `compliance_audit_log` | DPDPA: who accessed which candidate data, when | COMPLIANCE_OFFICER, SUPER_ADMIN |
| `compliance_data_export` | Right-to-access: candidate analytics export ZIP (30-day SLA) | COMPLIANCE_OFFICER, SUPER_ADMIN |
| `bias_monitoring` | Disparate impact ratio, score variance by demographics | COMPLIANCE_OFFICER, SUPER_ADMIN |

### Reports & Metrics (2)
| Tool | Description | Required Roles |
|------|-------------|----------------|
| `report_export` | Export to CSV/JSON/PDF: funnel, performance, compliance | RECRUITER, ANALYTICS_VIEWER, SUPER_ADMIN |
| `metrics_prometheus` | Prometheus operational metrics scrape | OPS_LEAD, SUPER_ADMIN |

---

## Security Architecture

### JWT Authentication (Keycloak OIDC)
- Every request carries `_meta.authorization: Bearer <jwt>`
- Validates: `exp` (expiry), `iss` (issuer), `aud: analytics-service`
- Extracts `realm_access.roles` for RBAC
- Extracts `tenant_id` / `company_id` claim for **mandatory tenant isolation**
- MFA enforced for SUPER_ADMIN (`acr` claim)
- Wire in JWKS signature verification (stub provided) via `nimbus-jose-jwt`

### Six Roles
```
SUPER_ADMIN        — full access to all tools
ANALYTICS_VIEWER   — read-only analytics + export
RECRUITER          — own data + candidate data (tenant-scoped)
CANDIDATE          — own scores and progress only
COMPLIANCE_OFFICER — DPDPA, audit log, bias monitoring
OPS_LEAD           — health, Kafka, platform metrics
```

### Tenant Isolation (Critical)
Every tool call has `_tenant_id` injected from the JWT by the server.  
All ClickHouse query notes include `WHERE tenant_id = '<from_jwt>'`.  
Cross-tenant data access is architecturally impossible — the tenant_id
is sourced from the validated JWT, not from user-supplied arguments.

### Rate Limiting
- 120 requests / 60 seconds per `user_id` (analytics reads are more frequent)
- `SecurityException` on breach, logged as security event

### Immutable Audit Trail (DPDPA 2023)
Every tool call logged with: `user_id`, `tool`, `tenant_id`, `args_summary`, `timestamp`  
Data access tools additionally log: `data_type`, `resource_id`, `purpose`  
Forward to ClickHouse `analytics_audit_log` table for 3-year DPDPA retention.

---

## Requirements

- **Java 17+**
- **Maven 3.8+** (build only)
- Only runtime dependency: `org.json:json:20240303`

---

## Build

```bash
mvn package -DskipTests
# → target/mcp-analytics-service-1.0.0.jar
```

---

## Run

```bash
# Dev mode (no JWT signature validation)
JWT_STRICT_SIGNATURE=false java -jar target/mcp-analytics-service-1.0.0.jar

# Production
KEYCLOAK_ISSUER=https://keycloak.ecoskiller.internal/realms/ecoskiller \
JWT_STRICT_SIGNATURE=true \
java -jar target/mcp-analytics-service-1.0.0.jar
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-analytics-service": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/target/mcp-analytics-service-1.0.0.jar"],
      "env": {
        "JWT_STRICT_SIGNATURE": "false",
        "AUDIT_LOG_FILE": "/tmp/ecoskiller-analytics-audit.log"
      }
    }
  }
}
```

---

## Tests

```bash
mvn package -DskipTests
python3 test_analytics_server.py           # pass/fail
python3 test_analytics_server.py --verbose # full JSON
```

---

## Docker

```bash
docker build -t mcp-analytics-service:1.0.0 .
docker run -e JWT_STRICT_SIGNATURE=false mcp-analytics-service:1.0.0
```

---

## Kubernetes (k3s / HPA 3–15)

```bash
helm upgrade --install mcp-analytics-service ./helm \
  -f helm-values.yaml -n analytics --create-namespace
```

---

## File Structure

```
mcp-analytics-service/
├── pom.xml
├── Dockerfile
├── helm-values.yaml
├── claude_desktop_config.json
├── test_analytics_server.py
├── README.md
└── src/main/java/com/ecoskiller/analytics/mcp/
    ├── AnalyticsMcpServer.java          ← Main server (dispatch + security gate)
    ├── ToolHandler.java                 ← Tool interface with RBAC + tenant contract
    ├── security/
    │   ├── JwtValidator.java            ← Keycloak JWT + tenant_id extraction
    │   ├── RateLimiter.java             ← Per-user token-bucket (120 req/min)
    │   └── AuditLogger.java             ← DPDPA-compliant data access audit trail
    └── tools/
        ├── BaseTool.java
        ├── DashboardGetTool.java
        ├── DashboardDataTool.java
        ├── DashboardListTool.java
        ├── DashboardCreateTool.java
        ├── FunnelJobTool.java
        ├── FunnelPlatformTool.java
        ├── FunnelCohortTool.java
        ├── RecruiterPerformanceTool.java
        ├── RecruiterTeamBenchmarkTool.java
        ├── RecruiterNoShowTool.java
        ├── RecruiterLeaderboardTool.java
        ├── RecruiterTimeToHireTool.java
        ├── CandidateScoreHistoryTool.java
        ├── CandidateProgressTool.java
        ├── CandidateScoreBreakdownTool.java
        ├── CandidateBeltAdvancementTool.java
        ├── PlatformHealthTool.java
        ├── PlatformKpiTool.java
        ├── PlatformModelPerformanceTool.java
        ├── KafkaConsumerLagTool.java
        ├── EventIngestionStatusTool.java
        ├── EventIngestionHistoryTool.java
        ├── JobLeaderboardTool.java
        ├── JobFunnelDailyTool.java
        ├── AnomalyDetectTool.java
        ├── AlertRuleListTool.java
        ├── AlertRuleCreateTool.java
        ├── ComplianceAuditLogTool.java
        ├── ComplianceDataExportTool.java
        ├── BiasMontitoringTool.java
        ├── ReportExportTool.java
        └── MetricsPrometheousTool.java
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Kafka Topics Monitored (18)

```
Assessment:  gd.completed, gd.started, interview.completed,
             interview.feedback.submitted, interview.no_show.detected,
             match.completed, score.computed, assessment.quality_metrics

Recruiter:   recruiter.created, recruiter.action.taken,
             recruiter.subscription.upgraded/downgraded/cancelled

Jobs/Apps:   job.created, job.applied, application.submitted,
             application.screened, application.rejected, application.hired

System:      billing.invoice.generated, notification.sent
```

---

## ClickHouse Tables Queried

| Table | Purpose |
|-------|---------|
| `gd_events` | Group discussion event log |
| `interview_events` | Interview event log |
| `match_events` | Coding challenge results |
| `score_events` | Scoring results with dimension breakdown |
| `application_events` | Application pipeline log |
| `recruiter_actions` | Recruiter action log |
| `job_events` | Job posting events |
| `candidate_scores_summary` | Pre-aggregated scores per candidate |
| `recruiter_performance_daily` | Daily recruiter KPIs |
| `job_funnel_daily` | Pre-computed funnel per job per day |
| `daily_metrics` | Platform-wide daily KPIs |
| `top_100_recruiters` | Daily-refreshed leaderboard |
| `analytics_audit_log` | DPDPA data access audit trail |

---

## Security Checklist (Before Go-Live)

- [ ] Implement JWKS signature verification in `JwtValidator.verifySignatureStub()`
- [ ] Set `JWT_STRICT_SIGNATURE=true` in all non-dev environments
- [ ] Add `tenant_id` custom claim to Keycloak token mapper
- [ ] Verify tenant isolation in ClickHouse (row-level security or per-tenant tables)
- [ ] Mount DB credentials via Kubernetes Secrets (never hardcode)
- [ ] Enable ModSecurity WAF on NGINX Ingress
- [ ] Run Trivy: `trivy image mcp-analytics-service:1.0.0`
- [ ] Configure PagerDuty for P0 Kafka lag or ingestion latency alerts
- [ ] Verify DPDPA audit log forwarding to ClickHouse (Fluent Bit pipeline)
- [ ] Set Redis TTL (5 min dashboards, 10 min leaderboards) in production config

---

## DPDPA 2023 Compliance

| Requirement | Tool |
|-------------|------|
| Right to Access | `compliance_data_export` — generates ZIP of all candidate analytics |
| Right to Erasure | Soft-delete candidate_id from ClickHouse tables |
| Right to Explanation | `candidate_score_breakdown` — feature importance |
| 30-day SLA | `sla_deadline` field on every export request |
| Audit Trail | All data access logged via `AuditLogger.logDataAccess()` |
| Bias Monitoring | `bias_monitoring` — monthly disparate impact report |
