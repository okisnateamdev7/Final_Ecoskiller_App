# mcp-recruiter-ecoskiller

**Ecoskiller | CAT-RECRUITER — Talent Acquisition & Hiring Management**  
MCP Server in Java | 18 Agents | Priority: HIGH | Security: Production-Grade

---

## Overview

Secure Java MCP server implementing the full Recruiter-Service specification for the Ecoskiller platform. Covers recruiter account lifecycle, job posting quotas, candidate management, team collaboration, subscription metering, analytics, webhooks, and DPDPA 2023 / SOC 2 compliance audit logging.

Built directly from `Recruiter_Service_Technical_Specification.docx` (v1.0, March 2026).

---

## Agents (18)

| # | Tool Name | Responsibility |
|---|-----------|----------------|
| 1 | `recruiter_account_onboard` | Onboard new recruiter, init subscription, emit `recruiter.created` |
| 2 | `recruiter_profile_get` | Retrieve profile from Redis cache (TTL 1h) with RLS enforcement |
| 3 | `recruiter_profile_update` | Update settings, invalidate cache, emit `recruiter.settings.updated` |
| 4 | `recruiter_dashboard_get` | Real-time dashboard metrics (Redis TTL 5min + PostgreSQL materialized view) |
| 5 | `recruiter_applications_list` | RLS-filtered application list with cursor pagination and score filtering |
| 6 | `recruiter_candidate_save` | Bookmark candidate with tag/note, emit `recruiter.candidate.saved` |
| 7 | `recruiter_saved_candidates_list` | Saved candidates with tag filter + bulk action support |
| 8 | `recruiter_notifications_get` | Paginated notifications (application, assessment, team, billing) |
| 9 | `recruiter_notification_mark_read` | Mark single or bulk notifications as read/archived |
| 10 | `recruiter_subscription_get` | Subscription tier, feature gates, quota usage, 30-day trend |
| 11 | `recruiter_team_get` | Team roster with role and invite status filtering |
| 12 | `recruiter_team_invite` | Invite HIRING_MANAGER/INTERVIEWER; generate scoped invite token |
| 13 | `recruiter_team_remove` | Revoke team member + Keycloak session invalidation |
| 14 | `recruiter_subscription_upgrade` | Upgrade tier; emit proration event to billing-service |
| 15 | `recruiter_subscription_cancel` | Cancel subscription; close active jobs via job-service |
| 16 | `recruiter_analytics_get` | Funnel, time-to-hire, pass rates, offer acceptance (ClickHouse) |
| 17 | `recruiter_webhook_register` | Register ATS/HRIS webhook (HTTPS-only; SSRF-protected; HMAC-signed) |
| 18 | `recruiter_audit_log_query` | ClickHouse immutable audit trail (DPDPA 2023 / SOC 2 compliance) |

---

## Security Architecture

| Layer | Detail |
|-------|--------|
| **JWT Validation** | HMAC-SHA256 (HS256), 5-min cache keyed by SHA-256 token hash, constant-time comparison |
| **Input Validation** | Strict allowlist regex for IDs (UUID + alphanumeric), emails, tiers, roles, tags |
| **SSRF Protection** | Webhook URLs: HTTPS only; blocks localhost, 127.x, 10.x, 172.16.x, 192.168.x, 169.254.x, metadata endpoints |
| **SQL Injection** | IDs never interpolated raw; RLS at PostgreSQL engine level; pattern-validated before use |
| **Payload Cap** | 64KB max per request; null byte detection |
| **Rate Limiting** | Token-bucket: 50 req/sec, 500 req/min per client key |
| **Audit Logging** | Structured to stderr (ELK-ready); rolling 2000-event in-memory buffer; log injection prevented |
| **Secrets** | All via `RECRUITER_JWT_SECRET` env var from Kubernetes Secret; insecure default warns loudly |
| **Container** | Non-root (UID 1000), read-only root FS, all capabilities dropped |
| **Network** | Kubernetes NetworkPolicy limits egress to PostgreSQL (5432), Redis (6379), Kafka (9092) only |

---

## Kafka Events Produced

| Event | Consumed By |
|-------|------------|
| `recruiter.created` | notification-service, billing-service, analytics-service |
| `recruiter.settings.updated` | notification-service |
| `recruiter.subscription.upgraded` | billing-service (proration), notification-service |
| `recruiter.subscription.cancelled` | billing-service, notification-service, job-service |
| `recruiter.candidate.saved` | analytics-service |
| `recruiter.team.member.invited` | notification-service (invite email) |
| `recruiter.quota.exceeded` | notification-service, billing-service (upsell) |

## Kafka Events Consumed

`job.created`, `job.updated`, `job.closed`, `application.submitted`, `application.screened`, `assessment.completed`, `score.computed`, `billing.invoice.generated`

---

## Requirements

- Java 17+
- Maven 3.8+ (build)

---

## Build

```bash
cd recruiter-mcp-server
mvn package -DskipTests
# Fat JAR → target/recruiter-mcp-server-1.0.0.jar
```

## Run

```bash
export RECRUITER_JWT_SECRET="your-keycloak-jwt-secret-min-32-chars"
export RECRUITER_TENANT_DOMAIN="ecoskiller.io"
java -jar target/recruiter-mcp-server-1.0.0.jar
```

## Test

```bash
mvn test
# 26 tests: 18 agent tests + 8 security/protocol tests
```

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:
```json
{
  "mcpServers": {
    "mcp-recruiter-ecoskiller": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/target/recruiter-mcp-server-1.0.0.jar"],
      "env": { "RECRUITER_JWT_SECRET": "your-secret" }
    }
  }
}
```

---

## Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `RECRUITER_JWT_SECRET` | ✅ | — | Keycloak HMAC-SHA256 JWT signing secret |
| `RECRUITER_TENANT_DOMAIN` | No | `ecoskiller.io` | XMPP/tenant domain |
| `RECRUITER_DB_HOST` | No | `postgres.core.svc.cluster.local` | PostgreSQL host |
| `RECRUITER_REDIS_ENABLED` | No | `true` | Enable Redis cache |
| `RECRUITER_AUDIT_ENABLED` | No | `true` | Enable audit logging |
| `RECRUITER_RATE_PER_SEC` | No | `50` | Rate limit per second per client |
| `RECRUITER_RATE_PER_MIN` | No | `500` | Rate limit per minute per client |
| `RECRUITER_MAX_JOBS_STARTER` | No | `5` | Starter tier max job postings |
| `RECRUITER_MAX_JOBS_PROFESSIONAL` | No | `25` | Professional tier max job postings |
| `RECRUITER_MAX_JOBS_ENTERPRISE` | No | `999` | Enterprise tier max job postings |

---

## File Structure

```
recruiter-mcp-server/
├── pom.xml
├── Dockerfile
├── k8s-deployment.yaml
├── claude_desktop_config.json
├── README.md
└── src/
    ├── main/java/io/ecoskiller/recruiter/
    │   ├── server/RecruiterMcpServer.java       ← Main entry point, JSON-RPC 2.0 dispatcher
    │   ├── agents/
    │   │   ├── AgentBase.java                   ← AgentHandler interface + BaseAgent
    │   │   ├── AccountAgents.java               ← Agents 1-5: onboard, profile, dashboard, applications
    │   │   ├── CandidateAgents.java             ← Agents 6-10: save, saved list, notifications, subscription
    │   │   ├── TeamAgents.java                  ← Agents 11-15: team, invite, remove, upgrade, cancel
    │   │   └── AnalyticsAgents.java             ← Agents 16-18: analytics, webhook, audit log
    │   ├── security/
    │   │   ├── InputSanitizer.java              ← Allowlist validation, SSRF protection, HTML/SQL escape
    │   │   ├── JwtValidator.java                ← HMAC-SHA256 JWT validation, 5-min cache
    │   │   └── Security.java                   ← RateLimiter, AuditLogger, RequestValidator
    │   └── config/ServerConfig.java            ← All config from env vars
    └── test/java/io/ecoskiller/recruiter/
        └── RecruiterMcpServerTest.java         ← 26 tests
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`
- All stdout is pure JSON-RPC. All logs to stderr.

---

*Ecoskiller Recruiter MCP Server v1.0.0 | March 2026*  
*CAT-RECRUITER | Talent Acquisition & Hiring Management*
