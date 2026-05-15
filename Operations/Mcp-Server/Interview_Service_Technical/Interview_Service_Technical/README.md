# mcp-interview-service (Java)

**Ecoskiller | CAT: One-on-One Video Interview Management**  
MCP Server in Java | 18 Agents | Priority: HIGH | Secure

---

## Agents (18)

| # | Tool Name | Agent Class | Role Required |
|---|-----------|-------------|---------------|
| 1 | `schedule_interview` | ScheduleInterviewAgent | RECRUITER |
| 2 | `get_interview` | GetInterviewAgent | ANY |
| 3 | `update_interview` | UpdateInterviewAgent | RECRUITER |
| 4 | `join_interview` | JoinInterviewAgent | ANY |
| 5 | `pause_interview` | PauseInterviewAgent | RECRUITER |
| 6 | `resume_interview` | ResumeInterviewAgent | RECRUITER |
| 7 | `complete_interview` | CompleteInterviewAgent | RECRUITER |
| 8 | `get_interview_result` | GetInterviewResultAgent | ANY |
| 9 | `submit_feedback` | SubmitFeedbackAgent | RECRUITER |
| 10 | `get_questions` | GetQuestionsAgent | ANY |
| 11 | `log_answer` | LogAnswerAgent | CANDIDATE |
| 12 | `list_interviews` | ListInterviewsAgent | ANY |
| 13 | `reschedule_interview` | RescheduleInterviewAgent | RECRUITER |
| 14 | `grant_recording_consent` | RecordingConsentAgent | CANDIDATE |
| 15 | `delete_recording` | DeleteRecordingAgent | RECRUITER/ADMIN |
| 16 | `get_analytics` | AnalyticsAgent | RECRUITER |
| 17 | `detect_no_show` | NoShowDetectionAgent | RECRUITER |
| 18 | `get_session_health` | SessionHealthAgent | ANY |

---

## Security Architecture

### JWT Validation (Keycloak OIDC)
Every tool call requires a valid `auth_token` (Keycloak JWT). The server validates:
- JWT structure (3 base64url parts)
- Signature (production: RS256 via JWKS endpoint)
- Expiry (`exp` claim)
- Required claims: `sub`, `tenant_id`, role

### Role-Based Access Control (RBAC)
- `RECRUITER` / `INTERVIEWER` / `ADMIN`: full access to all tools
- `CANDIDATE`: restricted to join, get, log_answer, consent, health
- Cross-tenant access is blocked at every layer

### Multi-Tenant Isolation
- All queries include `tenant_id` scope (mirrors PostgreSQL RLS)
- Jitsi room names include `tenant_id` prefix
- Kafka events carry `tenant_id` for downstream isolation

### Rate Limiting
| Tool Category | Limit |
|---------------|-------|
| Most tools | 100 req/min/tenant |
| schedule/complete | 20 req/min/tenant |
| get_analytics | 10 req/min/tenant |
| delete_recording | 5 req/min/tenant |

### Immutable Audit Logging
All tool calls logged to stderr (→ Grafana Loki / ClickHouse):
- `auth_token` and secrets are **always redacted**
- Event type, tool name, tenant_id, timestamp recorded
- Production: async write to ClickHouse (7-year retention, GST compliance)

### Input Sanitisation
- Control characters stripped from all string inputs
- String inputs limited to 4,096 characters
- UUID fields validated with `UUID.fromString()`
- Score fields validated to 1–5 range
- Enum fields validated against allowlist

---

## Requirements

- Java 17+
- Maven 3.8+ (for building)

---

## Build

```bash
cd mcp-interview-service
mvn clean package -DskipTests
```

Produces: `interview-service-mcp.jar` (fat JAR, ~2 MB)

---

## Run

```bash
java -jar interview-service-mcp.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Run Tests

```bash
java -cp interview-service-mcp.jar com.ecoskiller.mcp.interview.TestAgents
java -cp interview-service-mcp.jar com.ecoskiller.mcp.interview.TestAgents --verbose
```

Tests cover all 18 agents + 3 security rejection scenarios (missing JWT, malformed JWT, RBAC violation).

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "ecoskiller-interview-service": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/interview-service-mcp.jar"],
      "env": {
        "KEYCLOAK_JWKS_URL": "https://auth.ecoskiller.internal/realms/ecoskiller/protocol/openid-connect/certs"
      }
    }
  }
}
```

---

## File Structure

```
mcp-interview-service/
├── pom.xml
├── claude_desktop_config.json
├── README.md
└── src/main/java/com/ecoskiller/mcp/interview/
    ├── InterviewMcpServer.java        ← Main server (router + tool definitions)
    ├── TestAgents.java                ← Test all 18 agents + security tests
    ├── model/
    │   └── SecurityContext.java       ← Extracted JWT claims record
    ├── security/
    │   ├── JwtValidator.java          ← Keycloak OIDC JWT validation
    │   ├── RateLimiter.java           ← Token-bucket rate limiter per tenant
    │   └── AuditLogger.java           ← Immutable audit log (DPDPA/GST compliant)
    └── agents/
        ├── BaseAgent.java             ← Shared validation, Kafka, sanitisation
        └── Agents.java                ← All 18 agent implementations
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Production Wiring (TODOs)

| Component | Current (dev) | Production |
|-----------|---------------|------------|
| JWT verification | Base64 decode + stub | Nimbus JOSE RS256 + Keycloak JWKS |
| Rate limiter | In-memory ConcurrentHashMap | Redis `INCR`/`EXPIRE` (Lettuce) |
| Database | Simulated responses | PostgreSQL 15 + pgBouncer + RLS |
| Redis | Simulated | Jedis/Lettuce session state |
| Kafka | Logged to stderr | KafkaProducer (Confluent client) |
| Audit log | stderr JSON | ClickHouse HTTP ingest |
| MinIO | Simulated | MinIO Java SDK + AES-256 |

---

## Kafka Events Published

| Event | Trigger |
|-------|---------|
| `interview.scheduled.acked` | schedule_interview |
| `interview.started` | join_interview (first participant) |
| `interview.paused` | pause_interview |
| `interview.resumed` | resume_interview |
| `interview.completed` | complete_interview |
| `interview.feedback.submitted` | submit_feedback |
| `interview.no_show.detected` | detect_no_show |
| `interview.rescheduled` | reschedule_interview |
| `interview.consent.granted` | grant_recording_consent |
| `interview.recording.deleted` | delete_recording |
