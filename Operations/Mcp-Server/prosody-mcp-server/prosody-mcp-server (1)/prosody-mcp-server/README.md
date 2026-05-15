# mcp-prosody-ecoskiller

**Ecoskiller | CAT-PROSODY — XMPP Signaling & Presence Management**  
MCP Server in Java | 14 Agents | Priority: HIGH | Security: Production-Grade

---

## Overview

Secure Java MCP server for Prosody XMPP signaling and presence management.  
Covers the full assessment room lifecycle — from room creation through participant management, WebRTC signaling relay, JWT auth, Kafka event emission, rate limiting, and SOC 2 audit logging.

Built from the Ecoskiller Prosody Technical Documentation (v2.0, March 2025).

---

## Agents (14)

| # | Tool Name | Responsibility |
|---|-----------|----------------|
| 1 | `xmpp_room_create` | Create ephemeral MUC room for assessment |
| 2 | `xmpp_room_close` | Graceful room shutdown + Kafka event |
| 3 | `xmpp_room_query` | Query active room state and configuration |
| 4 | `xmpp_participant_join` | Authorize and join participant to room |
| 5 | `xmpp_participant_leave` | Remove participant + trigger room close if last |
| 6 | `xmpp_roster_get` | Get full occupant roster with speaking stats |
| 7 | `xmpp_presence_update` | Broadcast real-time presence (<100ms target) |
| 8 | `xmpp_signaling_relay` | Relay WebRTC SDP/ICE via Jitsi component protocol |
| 9 | `xmpp_jwt_validate` | Validate JWT tokens (HMAC-SHA256, 5-min cache) |
| 10 | `xmpp_connection_health` | Cluster health (pods, DB, Jitsi bridge, Kafka) |
| 11 | `xmpp_metrics_get` | Prometheus metrics (connections, latency, rooms) |
| 12 | `xmpp_rate_limit_control` | View/update limits, ban/unban JIDs |
| 13 | `xmpp_kafka_event_emit` | Emit or retry lifecycle Kafka events |
| 14 | `xmpp_audit_log_query` | Query audit log (GDPR/SOC 2 compliance) |

---

## Security Architecture

| Layer | Implementation |
|-------|----------------|
| **Authentication** | Every tool requires `jwt_token`. Agent 9 validates HMAC-SHA256 JWT. |
| **Input Validation** | `InputSanitizer` — strict allowlist regex for room IDs, user IDs, JIDs. Rejects XML injection, path traversal, null bytes. |
| **Rate Limiting** | Token-bucket limiter: 100 req/sec, 1000 req/min per client. Supports JID banning. |
| **Payload Size Guard** | 64KB max per request; 16KB max for signaling payloads. |
| **Audit Logging** | Every tool call logged to stderr (ELK-ready). Rolling 1000-event in-memory buffer for queries. No full tokens ever logged. |
| **JWT Cache** | Token hash (SHA-256) cached 5 minutes. Constant-time signature comparison (timing-attack safe). |
| **Constant-Time Compare** | `constantTimeEquals()` for HMAC verification — prevents timing attacks. |
| **No Secrets in Code** | All secrets via Kubernetes Secrets → environment variables. |
| **Non-Root Container** | Dockerfile runs as `prosody` user (UID 1000). Read-only root filesystem. |
| **XML Injection Prevention** | Status messages and free-text fields are XML-escaped before embedding in stanzas. |

---

## Requirements

- Java 17+
- Maven 3.8+ (for build)
- No runtime dependencies beyond Jackson (bundled in fat JAR)

---

## Build

```bash
cd prosody-mcp-server
mvn package -DskipTests
# Fat JAR: target/prosody-mcp-server-1.0.0.jar
```

---

## Run

```bash
# Set required environment variables
export PROSODY_JWT_SECRET="your-production-secret-min-32-chars"
export PROSODY_XMPP_DOMAIN="ecoskiller.io"
export PROSODY_DB_HOST="postgres.default.svc.cluster.local"

# Start the MCP server (stdio transport)
java -jar target/prosody-mcp-server-1.0.0.jar
```

---

## Test

```bash
mvn test
# Runs 20 tests covering all 14 agents + security + protocol error handling
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-prosody-ecoskiller": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/prosody-mcp-server/target/prosody-mcp-server-1.0.0.jar"],
      "env": {
        "PROSODY_JWT_SECRET": "your-secret-here",
        "PROSODY_XMPP_DOMAIN": "ecoskiller.io"
      }
    }
  }
}
```

---

## Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `PROSODY_JWT_SECRET` | ✅ Yes | — | HMAC-SHA256 key for JWT validation |
| `PROSODY_XMPP_DOMAIN` | No | `ecoskiller.io` | XMPP server domain |
| `PROSODY_DB_HOST` | No | `postgres.default.svc.cluster.local` | PostgreSQL host |
| `PROSODY_REDIS_ENABLED` | No | `false` | Enable Redis session cache |
| `PROSODY_AUDIT_ENABLED` | No | `true` | Enable audit logging to stderr |
| `PROSODY_LOG_LEVEL` | No | `INFO` | Log verbosity |
| `PROSODY_RATE_LIMIT_PER_SEC` | No | `100` | Max requests/second per client |
| `PROSODY_RATE_LIMIT_PER_MIN` | No | `1000` | Max requests/minute per client |

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`
- All stdout is pure JSON-RPC. All logs go to stderr.

---

## File Structure

```
prosody-mcp-server/
├── pom.xml                                          ← Maven build (Java 17, fat JAR)
├── Dockerfile                                       ← Multi-stage, non-root, read-only FS
├── k8s-deployment.yaml                             ← Kubernetes Deployment + Secret + ConfigMap
├── claude_desktop_config.json                      ← Claude Desktop config snippet
├── README.md
└── src/
    ├── main/java/io/ecoskiller/prosody/
    │   ├── server/
    │   │   └── ProsodyMcpServer.java               ← Main entry point, JSON-RPC dispatcher
    │   ├── agents/
    │   │   ├── AgentHandler.java                   ← Interface for all agents
    │   │   ├── BaseAgent.java                      ← Shared helpers, response builders
    │   │   ├── XmppRoomCreateAgent.java            ← Agent 1: create assessment room
    │   │   ├── XmppRoomCloseAgent.java             ← Agent 2: close room + Kafka event
    │   │   ├── XmppRoomAgents.java                 ← Agents 3-7: query, join, leave, roster, presence
    │   │   ├── XmppSignalingAgents.java            ← Agents 8-11: signaling, JWT, health, metrics
    │   │   └── XmppAdminAgents.java                ← Agents 12-14: rate limit, Kafka, audit
    │   ├── security/
    │   │   ├── InputSanitizer.java                 ← Allowlist validation, XML escape
    │   │   ├── RateLimiter.java                    ← Token-bucket rate limiter + JID bans
    │   │   ├── RequestValidator.java               ← Payload size + structure checks
    │   │   ├── AuditLogger.java                    ← Structured audit log (SOC 2 compliant)
    │   │   └── JwtValidator.java                   ← HMAC-SHA256 JWT validation + cache
    │   └── config/
    │       └── ServerConfig.java                   ← Config from environment variables
    └── test/java/io/ecoskiller/prosody/
        └── ProsodyMcpServerTest.java               ← 20 tests (agents + security + protocol)
```

---

## Kafka Topics

| Topic | Events Published |
|-------|-----------------|
| `assessment.events` | `room.created`, `room.closed`, `assessment.started` |
| `participant.presence` | `participant.joined`, `participant.left`, `presence.updated` |
| `signaling.errors` | `signaling.error` |

All events use `room_id` as partition key (guarantees per-room ordering).

---

## XMPP Ports Reference

| Port | Protocol | Purpose |
|------|----------|---------|
| 5222 | XMPP/TCP | Client connections (TLS/STARTTLS) |
| 5280 | HTTP/WebSocket | Web clients (wss://) + Prometheus metrics |
| 5275 | Component Protocol | Jitsi Video Bridge connection |

---

Document: Ecoskiller Prosody MCP Server  
Version: 1.0.0 | March 2026  
Category: CAT-PROSODY  
Security: Production-grade (JWT, rate limiting, audit, input validation)
