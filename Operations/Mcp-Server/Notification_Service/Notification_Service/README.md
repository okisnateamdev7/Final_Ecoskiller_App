# mcp-notification-service

**Ecoskiller | CAT-18 — Notification Service**
MCP Server in Java | 14 Agents | Priority: HIGH | Namespace: communication

---

## Agents (14 Tools)

| # | Tool Name | Agent |
|---|-----------|-------|
| 1 | `send_notification` | NOTIFICATION_SEND_AGENT |
| 2 | `queue_notification` | QUEUE_MANAGEMENT_AGENT |
| 3 | `get_notification_status` | STATUS_TRACKER_AGENT |
| 4 | `retry_failed_notification` | RETRY_AGENT |
| 5 | `get_user_preferences` | PREFERENCE_READ_AGENT |
| 6 | `update_user_preferences` | PREFERENCE_WRITE_AGENT |
| 7 | `suppress_address` | SUPPRESSION_AGENT |
| 8 | `check_suppression` | SUPPRESSION_CHECK_AGENT |
| 9 | `get_notification_history` | AUDIT_QUERY_AGENT |
| 10 | `render_template` | TEMPLATE_RENDER_AGENT |
| 11 | `get_queue_metrics` | METRICS_AGENT |
| 12 | `bulk_send_notifications` | BULK_SEND_AGENT |
| 13 | `cancel_notification` | CANCEL_AGENT |
| 14 | `compliance_data_request` | COMPLIANCE_AGENT |

---

## Requirements

- **Java 17+** — zero external dependencies (pure stdlib)
- No Maven/Gradle needed to run the pre-built JAR

---

## Build

```bash
javac NotificationMCPServer.java
jar cfe NotificationMCPServer.jar NotificationMCPServer *.class
```

---

## Run the server

```bash
# Run directly from source
java NotificationMCPServer

# Or run from pre-built JAR
java -jar NotificationMCPServer.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Run tests

```bash
java NotificationMCPServer --test           # quick pass/fail
java NotificationMCPServer --test --verbose # with JSON output

# From JAR
java -jar NotificationMCPServer.jar --test
```

Expected output: **29 passed, 0 failed** (22 functional + 7 security tests).

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "ecoskiller-notification-mcp": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/NotificationMCPServer.jar"]
    }
  }
}
```

---

## Channels & Cost Priority

Per spec, the server enforces cost-aware channel preference:

| Channel | Cost | Provider |
|---------|------|----------|
| `in_app` | Free | Internal WebSocket + PostgreSQL |
| `push` | Free | Firebase FCM / Apple APN |
| `email` | $0.0001/msg | SendGrid / AWS SES / Postfix |
| `sms` | $0.01/msg | Twilio / AWS SNS |

---

## Security Features

| Feature | Implementation |
|---------|---------------|
| Input validation | JSON structure + field type + length checks |
| Tool allowlist | Only known tool names accepted |
| Email validation | RFC5321 regex enforced |
| Phone validation | E.164 format (+country code) enforced |
| Control char rejection | Null bytes / control chars blocked in all string fields |
| Rate limiting | 200 req/min per client (token bucket, in-memory) |
| Deduplication | 5-minute window per (event_id + user_id + type), SHA-256 keyed |
| Phone masking | `+91*****3210` format in all responses |
| Address hashing | SHA-256 hash stored in suppression lists (not plaintext) |
| Tenant isolation | tenant_id validated against allowlist pattern |
| Error sanitization | Stack traces never sent to client; secret fields redacted |

---

## Kafka Topics Consumed (25+)

**Assessment:** gd.completed, interview.scheduled, interview.completed, score.computed, belt.eligibility.determined, match.completed

**Application:** application.submitted, application.screened, application.hired, offer.extended, offer.accepted, offer.rejected

**Recruiter/Job:** recruiter.created, recruiter.subscription.upgraded, job.created, job.closed, recruiter.team.member.invited

**User/System:** user.created, password.reset.requested, email.verification.requested, compliance.data_request, billing.invoice.generated

---

## Database Schema (PostgreSQL)

```sql
-- Queue of pending/sent notifications
notification_queue (
  id, user_id, tenant_id, notification_type, channel,
  status, attempt_count, scheduled_at, sent_at, error_reason
)

-- In-app notifications (30-day retention)
notification_center (
  id, user_id, notification_type, title, body,
  metadata, created_at, read_at, archived_at
)

-- Immutable audit trail (3-year retention, DPDPA §8(7))
notification_audit_log (
  notification_id, user_id, channel, status, timestamp, error_reason
)

-- User preferences (Redis-cached, 1h TTL)
notification_preferences (
  user_id, email_enabled, sms_enabled, push_enabled,
  quiet_hours_start, quiet_hours_end, frequency_threshold
)

-- Suppression lists (addresses stored as SHA-256 hashes)
email_suppression_list (email_hash, suppress_reason, suppressed_at, tenant_id)
sms_suppression_list   (phone_hash, suppress_reason, suppressed_at, tenant_id)
```

---

## Rate Limits (per spec)

| User type | Channel | Limit |
|-----------|---------|-------|
| Candidate | Email | Max 10/day |
| Candidate | SMS | Max 1/day |
| Candidate | Push | Max 5/day |
| Candidate | In-app | Unlimited |
| Recruiter | Email | Max 1000/day |

---

## Retry Strategy (Exponential Backoff)

```
Attempt 1: immediate
Attempt 2: +1 min  (transient failure)
Attempt 3: +5 min
Attempt 4: +15 min
Attempt 5: +1 hour → mark FAILED permanently, alert admin
```

Max retries: **3 for email**, **2 for SMS**.
Hard bounces and spam complaints → **no retry, permanent suppression**.

---

## DPDPA 2023 Compliance

| Right | Tool | Behaviour |
|-------|------|-----------|
| Right to Access | `compliance_data_request` (type=`access`) | Returns full audit trail JSON, 30-day SLA |
| Right to Erasure | `compliance_data_request` (type=`erasure`) | Suppresses address, purges operational logs, retains 3yr audit |
| Right to Portability | `compliance_data_request` (type=`portability`) | JSON export, 48h download link |
| Opt-out | `update_user_preferences` (email_enabled=false) | Adds to suppression list + audit entry |

---

## Deployment (Kubernetes)

```yaml
namespace: communication
replicas: 9-20 (HPA: queue_depth > 10K or CPU > 70%)
port: 8080
resources:
  requests: { cpu: 500m, memory: 1Gi }
  limits:   { cpu: 1000m, memory: 2Gi }
```

---

## File Structure

```
mcp-notification-service/
├── NotificationMCPServer.java   ← Single-file server (all 14 agents)
├── NotificationMCPServer.jar    ← Pre-built executable JAR (29 KB)
├── claude_desktop_config.json   ← Claude Desktop config snippet
└── README.md
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`
