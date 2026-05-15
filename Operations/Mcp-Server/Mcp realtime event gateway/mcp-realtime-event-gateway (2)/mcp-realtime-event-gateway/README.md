# mcp-realtime-event-gateway

**Ecoskiller | Real-Time Event Streaming & WebSocket Gateway**  
MCP Server in Java | 28 Tools | Priority: CRITICAL | Security Level: HIGH

---

## Overview

`realtime-event-gateway` is Ecoskiller's real-time event streaming backbone.  
It streams live assessment events (score updates, speaker changes, timer ticks, presence) to
connected clients via WebSocket with **<100ms p99 latency**.

**Capabilities:**
- 10,000+ concurrent WebSocket connections per pod (30–50k with 3–5 pod cluster)
- 50,000+ events/second throughput (circuit breaker at threshold)
- Kafka consumer group participation (`realtime-event-gateway-group`)
- Redis pub-sub for cross-pod event synchronization
- In-memory ring buffer (last 100 messages) + 5-minute replay window
- Real-time presence tracking per assessment
- Automatic reconnection with exponential backoff (100ms → 2min)

---

## Tools (28)

| # | Tool Name | Category | Description |
|---|-----------|----------|-------------|
| 1 | `connect_client` | Connection | Accept + authenticate WebSocket client (origin check) |
| 2 | `disconnect_client` | Connection | Graceful disconnect + presence grace period |
| 3 | `get_connection_info` | Connection | Per-client stats, auth state, buffer usage |
| 4 | `list_active_connections` | Connection | Pod-level connection inventory |
| 5 | `subscribe_to_assessment` | Subscription | Role-based event topic subscription |
| 6 | `unsubscribe_from_assessment` | Subscription | Remove subscription + presence update |
| 7 | `get_subscriptions` | Subscription | Query subscription state |
| 8 | `publish_event` | Event Routing | Publish event to all assessment subscribers |
| 9 | `route_kafka_event` | Event Routing | Full Kafka → WebSocket → Redis pipeline |
| 10 | `broadcast_to_assessment` | Event Routing | Direct broadcast (bypass Kafka, role-filtered) |
| 11 | `broadcast_redis_pubsub` | Event Routing | Cross-pod Redis pub-sub publish |
| 12 | `get_message_buffer` | Buffer & Replay | Inspect ring buffer for a client |
| 13 | `replay_missed_messages` | Buffer & Replay | Sequential replay on reconnect |
| 14 | `acknowledge_message` | Buffer & Replay | Client ack + retry clear |
| 15 | `track_presence` | Presence | JOIN/LEAVE/HEARTBEAT presence state |
| 16 | `get_presence` | Presence | Live participant list by role |
| 17 | `emit_presence_event` | Presence | Broadcast user_joined/user_left |
| 18 | `get_circuit_breaker_status` | Circuit Breaker | Current state: CLOSED/OPEN/HALF_OPEN |
| 19 | `trip_circuit_breaker` | Circuit Breaker | Manually open (Admin) |
| 20 | `reset_circuit_breaker` | Circuit Breaker | OPEN → HALF_OPEN → CLOSED |
| 21 | `get_kafka_consumer_status` | Kafka | Consumer group lag, partition assignment |
| 22 | `commit_kafka_offset` | Kafka | Mark message as processed (Admin) |
| 23 | `validate_client_token` | Auth | JWT validation + blacklist check |
| 24 | `revoke_client_token` | Auth | Redis token blacklist + force disconnect (Admin) |
| 25 | `send_heartbeat` | Timer & Heartbeat | Ping client, detect stale connections |
| 26 | `emit_timer_tick` | Timer & Heartbeat | Synchronized countdown to all participants |
| 27 | `health_check` | Observability | K8s liveness/readiness probe |
| 28 | `get_metrics` | Observability | Prometheus metrics (Admin) |

---

## Kafka Topics Consumed

| Topic Pattern | Events |
|---------------|--------|
| `assessment.*` | assessment.score.updated, assessment.solution.submitted, assessment.started, assessment.completed |
| `gd.*` | gd.speaker.changed, gd.session.started, gd.session.ended |
| `interview.*` | interview.status.changed, interview.started, interview.ended |
| `timer.*` | timer.tick, timer.expired |

**Consumer group:** `realtime-event-gateway-group`  
**Partitions:** 12 (distributed across pods)  
**Offset strategy:** Commit after successful WebSocket delivery

---

## Event Flow (End-to-End)

```
Backend service
    → Kafka publish (1–2ms)
    → Kafka broker (5–10ms)
    → Gateway consumer fetch (10–20ms)
    → Dedup check (Redis, 2ms)
    → Subscription lookup (HashMap O(1), 1ms)
    → WebSocket delivery (8–10ms)
    → Redis pub-sub cross-pod (5–10ms)
    = 32–55ms average, <100ms p99
```

---

## Security Features

| Feature | Detail |
|---------|--------|
| **JWT Validation** | Keycloak RS256, alg:none blocked, expiry + issuer checks |
| **Origin Validation** | WebSocket origin header checked (prevents cross-site hijacking) |
| **Rate Limiting** | 120 req/min MCP-level; 100 events/sec per WebSocket client |
| **Input Sanitization** | Control char stripping, 4096-char field limit, 64KB payload cap |
| **ID Format Allowlist** | client_id/assessment_id: `^[a-zA-Z0-9_-]{1,128}$` |
| **Token Blacklist** | Redis `jwt_{jti}` keys; checked on every connection |
| **Admin Tool Protection** | JWT required: trip/reset circuit breaker, revoke token, metrics |
| **Jackson Hardening** | Default typing disabled (CVE-2017-7525 prevention) |

---

## Requirements

- **Java 17+**
- **Maven 3.8+**

No external services needed for standalone MCP operation.

---

## Build

```bash
mvn clean package -q
```

Output: `target/mcp-realtime-event-gateway-1.0.0.jar`

---

## Run

```bash
java -jar target/mcp-realtime-event-gateway-1.0.0.jar
```

Communicates via **stdin/stdout** (JSON-RPC 2.0). Logs go to **stderr**.

---

## Run Tests

```bash
mvn test
```

30 tests covering: connection lifecycle, subscription routing, event publishing,
Kafka pipeline, circuit breaker, presence tracking, JWT attacks, rate limiting,
origin validation, input sanitization.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-realtime-event-gateway": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-realtime-event-gateway-1.0.0.jar"]
    }
  }
}
```

---

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `MCP_ENV` | `dev` | dev \| test \| stage \| prod |
| `MCP_JWT_VALIDATION_ENABLED` | `false` | Enable Keycloak JWT validation |
| `MCP_KEYCLOAK_ISSUER` | `https://auth.ecoskiller.com/realms/` | Keycloak base URL |
| `MCP_RATE_LIMIT_PER_MINUTE` | `120` | MCP-level request rate limit |
| `MCP_MAX_CONNECTIONS` | `10000` | Max WebSocket connections per pod |
| `MCP_MAX_EVENTS_SEC_GLOBAL` | `50000` | Circuit breaker threshold (events/sec) |
| `MCP_MAX_EVENTS_SEC_CLIENT` | `100` | Per-client event rate limit |
| `MCP_RING_BUFFER_CAPACITY` | `100` | Messages per client ring buffer |
| `MCP_BUFFER_REPLAY_WINDOW_MIN` | `5` | Minutes to retain buffer for replay |
| `MCP_CIRCUIT_BREAKER_THRESHOLD` | `50000` | Events/sec to open circuit |
| `MCP_LOG_LEVEL` | `INFO` | DEBUG \| INFO \| WARNING \| ERROR |

---

## File Structure

```
mcp-realtime-event-gateway/
├── pom.xml
├── README.md
├── claude_desktop_config.json
└── src/
    ├── main/
    │   ├── java/com/ecoskiller/mcp/
    │   │   ├── server/
    │   │   │   └── RealtimeEventGatewayMcpServer.java  ← Main server (28 tools)
    │   │   ├── tools/
    │   │   │   ├── McpTool.java            ← Tool interface
    │   │   │   ├── BaseTool.java           ← Validation, sanitization, schema helpers
    │   │   │   ├── ConnectionTools.java    ← connect, disconnect, get_info, list
    │   │   │   ├── SubscriptionTools.java  ← subscribe, unsubscribe, get_subscriptions
    │   │   │   ├── EventRoutingTools.java  ← publish, route_kafka, broadcast, redis_pubsub
    │   │   │   ├── MessageBufferTools.java ← get_buffer, replay, acknowledge
    │   │   │   └── RemainingTools.java     ← presence, circuit breaker, kafka, auth, timer, health
    │   │   ├── security/
    │   │   │   ├── JwtValidator.java       ← Keycloak JWT validation (alg:none blocked)
    │   │   │   └── RateLimiter.java        ← Sliding-window rate limiter
    │   │   ├── config/
    │   │   │   └── ServerConfig.java       ← Env-var-first configuration
    │   │   └── util/
    │   │       └── JsonUtil.java           ← Hardened Jackson mapper
    │   └── resources/
    │       └── config.properties
    └── test/
        └── java/com/ecoskiller/mcp/
            └── RealtimeEventGatewayTest.java  ← 30 tests
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`
