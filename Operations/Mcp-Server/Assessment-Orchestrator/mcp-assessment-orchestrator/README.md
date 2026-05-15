# mcp-assessment-orchestrator

**Ecoskiller | assessment-orchestrator**
MCP Server in Java | 20 Tools | Priority: CRITICAL | Namespace: realtime

The central coordination and lifecycle management service for all Ecoskiller
assessment sessions — Group Discussions (GD), Interviews, and Dojo coding matches.

---

## Tools (20)

### Cycle Management
| # | Tool | Role(s) |
|---|------|---------|
| 1 | `create_assessment_cycle` | RECRUITER, ADMIN, SERVICE_ACCOUNT |
| 2 | `get_cycle_status` | Any authenticated |
| 3 | `reschedule_cycle` | ADMIN |
| 4 | `cancel_cycle` | ADMIN, MODERATOR |

### Session Lifecycle
| # | Tool | Role(s) |
|---|------|---------|
| 5 | `create_session` | RECRUITER, ADMIN, SERVICE_ACCOUNT |
| 6 | `join_session` | CANDIDATE, RECRUITER, MODERATOR, ADMIN |
| 7 | `get_session_state` | Any authenticated (CANDIDATE: own sessions only) |
| 8 | `transition_session_state` | MODERATOR, ADMIN, SERVICE_ACCOUNT |
| 9 | `check_quorum` | Any authenticated |

### Session Control (Real-time)
| # | Tool | Role(s) |
|---|------|---------|
| 10 | `control_session` | MODERATOR, ADMIN |
| 11 | `handle_raise_hand` | CANDIDATE (own), ADMIN |
| 12 | `handle_participant_event` | Any authenticated |
| 13 | `process_kafka_event` | SERVICE_ACCOUNT, ADMIN |

### Kafka & Jitsi Tokens
| # | Tool | Role(s) |
|---|------|---------|
| 14 | `emit_completion_event` | MODERATOR, ADMIN, SERVICE_ACCOUNT |
| 15 | `validate_jitsi_token` | Any authenticated |
| 16 | `get_kafka_event_log` | ADMIN, SERVICE_ACCOUNT |

### Audit & Analytics
| # | Tool | Role(s) |
|---|------|---------|
| 17 | `get_session_audit_log` | RECRUITER, MODERATOR, ADMIN |
| 18 | `get_speaking_time_stats` | RECRUITER, MODERATOR, ADMIN |
| 19 | `get_service_health` | Any (including unauthenticated k8s probes) |
| 20 | `configure_track_plan` | ADMIN |

---

## GD Session State Machine

```
SCHEDULED → WAITING → INTRO (60s) → SPEAKING:{cand} (90s×N)
         → OPEN_DISCUSSION (300s) → CLOSING (120s) → COMPLETED
                              ↘ CANCELLED  (from WAITING on NO_QUORUM or MANUAL)
```

All state stored under Redis key: `tenant:{tenant_id}:session:{session_id}:state`

---

## Security Architecture

| Layer | Mechanism |
|---|---|
| Authentication | Keycloak Bearer JWT on every call |
| RBAC | 5 roles: CANDIDATE / RECRUITER / MODERATOR / ADMIN / SERVICE_ACCOUNT |
| Tenant isolation | `tenant_id` scoped on every query (mirrors Redis key namespacing + PostgreSQL RLS) |
| Input sanitisation | SQL injection + XSS pattern detection on all string fields |
| Jitsi JWT issuance | HS256, 5-min TTL, room = session_id, role = moderator/attendee |
| Idempotency | Per-join and per-completion-event deduplication (Redis SET NX EX in prod) |
| Distributed locks | Slot booking via Redis SET NX EX 30s (prevents double-booking) |
| DLQ compliance | Kafka max 3 retries with exponential backoff; events published to *.dlq on failure |

---

## Kafka Topics

### Consumed (upstream triggers)
| Topic | Producer |
|---|---|
| `assessment.cycle.created` | job-service |
| `interview.session.requested` | interview-service |
| `dojo.match.requested` | dojo-match-engine |
| `assessment.cycle.cancelled` | admin-service / recruiter-service |

### Produced (downstream events)
| Topic | Consumers |
|---|---|
| `gd.completed` | scoring-engine, analytics-service, notification-service |
| `interview.completed` | scoring-engine, analytics-service |
| `assessment.session.cancelled` | notification-service, admin-service, analytics-service |
| `assessment.candidate.absent` | notification-service, recruiter-service |
| `assessment.cycle.completed` | certification-engine, analytics-service, billing-service |
| `assessment.session.started` | analytics-service, notification-service |

---

## Requirements

- **Java 11+** — zero external dependencies (pure JDK)
- Maven 3.x (optional — for build only)

---

## Build

```bash
# Compile
javac -d target/classes $(find src -name "*.java")

# Package
jar cfe target/mcp-assessment-orchestrator.jar \
    com.ecoskiller.mcp.AssessmentOrchestratorMCPServer \
    -C target/classes .

# Or with Maven
mvn package -q
```

---

## Run

```bash
java -jar target/mcp-assessment-orchestrator.jar
```

Transport: **stdio** (JSON-RPC 2.0). All logs → stderr; stdout = MCP messages only.

---

## Connect to Claude Desktop

```json
{
  "mcpServers": {
    "mcp-assessment-orchestrator": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/target/mcp-assessment-orchestrator.jar"],
      "env": {
        "APP_JWT_SECRET": "your-keycloak-realm-secret",
        "JITSI_JWT_SECRET": "your-jitsi-shared-secret",
        "JITSI_APP_ID": "ecoskiller",
        "JITSI_DOMAIN": "meet.ecoskiller.internal",
        "APP_STRICT_MODE": "false",
        "GD_INTRO_DURATION_SECONDS": "60",
        "GD_SPEAKING_DURATION_SECONDS": "90",
        "GD_OPEN_DISCUSSION_DURATION_SECONDS": "300",
        "GD_CLOSING_DURATION_SECONDS": "120",
        "QUORUM_CANCEL_LEAD_TIME_MINUTES": "2"
      }
    }
  }
}
```

---

## Run tests

```bash
# Quick pass/fail (28 tests)
java -cp target/mcp-assessment-orchestrator.jar com.ecoskiller.mcp.TestAgents

# With full JSON output
java -cp target/mcp-assessment-orchestrator.jar com.ecoskiller.mcp.TestAgents --verbose
```

Expected: **28/28 PASSED**

---

## File Structure

```
mcp-assessment-orchestrator/
├── pom.xml
├── README.md
├── claude_desktop_config.json
└── src/main/java/com/ecoskiller/mcp/
    ├── AssessmentOrchestratorMCPServer.java   ← Entry point
    ├── TestAgents.java                         ← 28-test suite
    ├── protocol/
    │   ├── MCPProtocolHandler.java             ← JSON-RPC 2.0 dispatcher
    │   └── JsonUtil.java                       ← Zero-dependency JSON parser
    ├── security/
    │   ├── SecurityConfig.java                 ← JWT, RBAC, Jitsi issuance, locks
    │   └── JwtClaims.java                      ← JWT claims value object
    ├── models/
    │   ├── AssessmentCycle.java                ← Cycle domain model
    │   ├── AssessmentSession.java              ← Session + full GD state machine
    │   ├── OrchestratorRepository.java         ← Thread-safe store (→ Redis + PostgreSQL)
    │   └── KafkaEventPublisher.java            ← 6 Kafka topic stubs (→ KafkaJS)
    └── tools/
        ├── ToolRouter.java                     ← 20-tool dispatcher + tools/list
        ├── CycleManagementTools.java           ← create, status, reschedule, cancel
        ├── SessionLifecycleTools.java          ← create, join, state, transition, quorum
        ├── SessionControlTools.java            ← control, raise_hand, participant, kafka_event
        ├── KafkaTokenTools.java                ← completion, jitsi_validate, kafka_log
        └── AuditAnalyticsTools.java            ← audit, speaking_time, health, track_plan
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Production Migration Checklist

| Component | Dev | Production |
|---|---|---|
| JWT verification | HS256 dev-sig passthrough | RS256 + Keycloak JWKS endpoint |
| Session state | In-memory domain model | Redis 7 (`SETNX`, `TTL`, Keyspace Notifications) |
| Persistent records | In-memory repository | PostgreSQL 15 with RLS |
| Kafka events | Logger stubs + in-memory log | KafkaJS 2.x + Apicurio Schema Registry |
| Audit log | In-memory list | ClickHouse (immutable, 3-year retention) |
| Distributed locks | `ConcurrentHashMap` | Redis `SET NX EX 30` |
| Idempotency store | `LinkedHashSet` | Redis `SET NX EX 86400` |
| Jitsi tokens | HS256 (dev secret) | HS256 (Kubernetes Secret: `jitsi-jwt-secret`) |
| WebSocket commands | Log stub | `ws` library Node.js persistent connections |
