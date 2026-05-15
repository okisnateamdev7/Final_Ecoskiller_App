# redis-state-store-mcp

**Ecoskiller | Redis State Store — MCP Server in Java**  
18 Tools | Secure | TLS | Tenant-Scoped | JSON-RPC 2.0 over stdio

---

## Overview

This MCP server exposes the full Ecoskiller Redis State Store Service through 18 tools.
It is the primary interface for Claude and other MCP clients to interact with the Redis
layer that underpins GD orchestration, interview timers, OTP flows, distributed locks,
leaderboards, Pub/Sub, and operational tasks.

```
Transport:  stdio (stdin/stdout)
Protocol:   JSON-RPC 2.0 — MCP 2024-11-05
Language:   Java 17 (no Spring, single fat JAR)
Dependency: org.json only
Security:   TLS in-transit · AUTH · tenant-scoped keys · input validation · audit log
```

---

## 18 Tools

| # | Tool Name | Purpose |
|---|-----------|---------|
| 1 | `gd_session_state` | GD state machine — get/set state, speaker, participants, result |
| 2 | `gd_timer` | TTL-based phase timers with keyspace notification support |
| 3 | `gd_queue` | Raise-hand queue management (Redis LIST, FIFO) |
| 4 | `otp_store` | OTP lifecycle — store, verify (atomic), TTL, invalidate |
| 5 | `distributed_lock` | Atomic SET NX PX locks for slot booking and billing |
| 6 | `rate_limit` | INCR+EXPIRE sliding-window rate counters |
| 7 | `leaderboard` | Live Sorted Set leaderboards — O(log N) updates |
| 8 | `interview_timer` | Interview question/session timers with ms-precision |
| 9 | `session_affinity` | Cross-pod session context for stateless microservices |
| 10 | `dojo_match` | Dojo match state and submission attempt counters |
| 11 | `tenant_config_cache` | Per-tenant per-service config cache |
| 12 | `pubsub_broadcast` | Pub/Sub event broadcast to WebSocket pods |
| 13 | `keyspace_notification` | Verify/enable KEA config for timer-driven events |
| 14 | `health_check` | PING, memory, BGSAVE, uptime diagnostics |
| 15 | `key_inspect` | Tenant-scoped key inspection (OTP values masked) |
| 16 | `billing_meter` | Usage metering INCR counters and billing cycle locks |
| 17 | `backup_status` | BGSAVE status, RPO check, manual trigger |
| 18 | `tenant_key_flush` | Scoped key eviction — never exposes FLUSHALL/FLUSHDB |

---

## Requirements

- Java 17+
- Maven 3.8+ (build only)
- Redis 7 (self-managed k3s StatefulSet, realtime namespace)

---

## Build

```bash
mvn package -DskipTests
# Output: target/redis-state-store-mcp-1.0.0.jar
```

---

## Run

```bash
# Development (no TLS, no auth)
REDIS_HOST=127.0.0.1 REDIS_PORT=6379 ENV=dev \
  java -jar target/redis-state-store-mcp-1.0.0.jar

# Production
REDIS_HOST=redis.realtime.svc.cluster.local \
REDIS_PORT=6379 \
REDIS_PASSWORD=<from-k8s-secret> \
REDIS_TLS_ENABLED=true \
REDIS_TLS_CA_PATH=/etc/ssl/redis/ca.jks \
ENV=prod \
AUDIT_LOG_PATH=/var/log/ecoskiller/redis-mcp-audit.log \
  java -jar target/redis-state-store-mcp-1.0.0.jar
```

---

## Test

```bash
# Run all 40 tests with in-process mock Redis (no live Redis needed)
mvn test

# Verbose
mvn test -pl . -Dsurefire.useFile=false
```

---

## Environment Variables

| Variable | Default | Required in Prod | Description |
|---|---|---|---|
| `REDIS_HOST` | `127.0.0.1` | Yes | Redis hostname |
| `REDIS_PORT` | `6379` | Yes | Redis port |
| `REDIS_PASSWORD` | _(none)_ | **Yes** | Redis AUTH password |
| `REDIS_TLS_ENABLED` | `false` | Recommended | Enable TLS |
| `REDIS_TLS_CA_PATH` | _(none)_ | Yes (if TLS) | CA truststore (JKS) |
| `REDIS_TLS_CERT_PATH` | _(none)_ | Optional | Client cert (PKCS12, for mTLS) |
| `REDIS_TLS_KEY_PATH` | _(none)_ | Optional | Client key (for mTLS) |
| `REDIS_DB` | `0` | No | Redis DB number |
| `REDIS_TIMEOUT_MS` | `2000` | No | Command timeout |
| `REDIS_MAX_RETRIES` | `3` | No | Reconnect attempts |
| `ENV` | `dev` | Yes | `dev\|test\|stage\|prod` |
| `AUDIT_LOG_PATH` | `/var/log/redis-mcp-audit.log` | Yes | Audit log file path |

---

## Connect to Claude Desktop

Copy `claude_desktop_config.json` snippet and update the JAR path:

```json
{
  "mcpServers": {
    "redis-state-store": {
      "command": "java",
      "args": ["-jar", "/path/to/redis-state-store-mcp-1.0.0.jar"],
      "env": {
        "REDIS_HOST":        "127.0.0.1",
        "REDIS_PORT":        "6379",
        "REDIS_PASSWORD":    "yourpassword",
        "REDIS_TLS_ENABLED": "false",
        "ENV":               "dev"
      }
    }
  }
}
```

---

## Security Model

| Control | Implementation |
|---------|----------------|
| **Authentication** | Redis AUTH password via `REDIS_PASSWORD` env var (never hardcoded) |
| **Encryption in transit** | TLS 1.2/1.3 with optional mTLS (client cert + CA verification) |
| **Tenant isolation** | All keys scoped to `tenant:{tenant_id}:` prefix — validated on every call |
| **Input validation** | Regex-validated IDs, allow-listed OTP types & GD states, value size limits, TTL range checks |
| **OTP protection** | OTP values never written to audit log; masked in `key_inspect`; atomic DEL on verify |
| **Lock safety** | Lock release requires token match — prevents accidental release by wrong caller |
| **Flush safety** | `tenant_key_flush` never exposes FLUSHALL/FLUSHDB; requires `confirm=true` for live runs |
| **Audit logging** | Every tool call logged to append-only audit file with tenant, tool, status, timestamp |
| **Container security** | Non-root user, read-only root filesystem, all capabilities dropped |

---

## Redis Key Namespace Reference

All keys follow the tenant-scoped patterns from the Ecoskiller Redis State Store Technical Reference v1.0:

```
tenant:{tid}:gd:{sid}:state              STRING   GD session state enum
tenant:{tid}:gd:{sid}:timer              STRING   Phase countdown (SETEX + keyspace notify)
tenant:{tid}:gd:{sid}:current_speaker    STRING   Active speaker (TTL=90s)
tenant:{tid}:gd:{sid}:queue              LIST     Raise-hand queue (FIFO)
tenant:{tid}:gd:{sid}:participants       SET      Joined participant IDs
tenant:{tid}:gd:{sid}:result             STRING   Session result JSON (TTL=24h)
tenant:{tid}:gd:{sid}:idempotency        STRING   Kafka publish guard (TTL=24h)
tenant:{tid}:interview:{iid}:timer       STRING   Question/session timer
tenant:{tid}:interview:{iid}:question    STRING   Current question index
tenant:{tid}:otp:{uid}:{type}            STRING   OTP value (TTL=900s, in-memory only)
tenant:{tid}:ratelimit:{uid}:{ep}:{date} STRING   Rate counter (INCR+EXPIRE)
tenant:{tid}:lock:slot:{slot_id}         STRING   Slot booking lock (SET NX PX 10s)
tenant:{tid}:lock:billing:{cycle_id}     STRING   Billing cycle lock (SET NX PX 60s)
tenant:{tid}:rankings:{job_id}           SORTED SET Live leaderboard
tenant:{tid}:match:{mid}:state           STRING   Dojo match state JSON
tenant:{tid}:match:{mid}:attempts        STRING   Submission attempt counter
tenant:{tid}:{svc}:config                STRING   Config cache (TTL=300–3600s)
tenant:{tid}:billing:meter:{metric}      STRING   Usage metering counter
session:{sid}:events                     PUB/SUB  Per-session WebSocket broadcast
tenant:{tid}:broadcast                   PUB/SUB  Tenant-wide admin signal channel
```

---

## File Structure

```
redis-state-store-mcp/
├── pom.xml
├── Dockerfile
├── k8s-deployment.yaml
├── claude_desktop_config.json
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp/redis/
    │   ├── RedisStateMcpServer.java       ← Main server, JSON-RPC dispatch
    │   ├── McpTool.java                   ← Tool interface
    │   ├── config/
    │   │   ├── ServerConfig.java          ← Env var config loader
    │   │   └── RedisClient.java           ← TLS-capable Redis client (RESP)
    │   ├── security/
    │   │   ├── InputValidator.java        ← Regex validation, allow-lists
    │   │   └── AuditLogger.java           ← Append-only audit log
    │   ├── model/
    │   │   └── KeyBuilder.java            ← All 20 key patterns (Appendix A)
    │   └── tools/                         ← 18 tool implementations
    │       ├── BaseTool.java
    │       ├── GdSessionStateTool.java
    │       ├── GdTimerTool.java
    │       ├── GdQueueTool.java
    │       ├── OtpStoreTool.java
    │       ├── DistributedLockTool.java
    │       ├── RateLimitTool.java
    │       ├── LeaderboardTool.java
    │       ├── InterviewTimerTool.java
    │       ├── SessionAffinityTool.java
    │       ├── DojoMatchTool.java
    │       ├── TenantConfigCacheTool.java
    │       ├── PubSubBroadcastTool.java
    │       ├── KeyspaceNotificationTool.java
    │       ├── HealthCheckTool.java
    │       ├── KeyInspectTool.java
    │       ├── BillingMeterTool.java
    │       ├── BackupStatusTool.java
    │       └── TenantKeyFlushTool.java
    └── test/java/com/ecoskiller/mcp/redis/
        └── RedisStateMcpServerTest.java   ← 40 tests, in-process mock Redis
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

*Ecoskiller Platform Engineering · Redis State Store MCP Server v1.0.0 · March 2026*
