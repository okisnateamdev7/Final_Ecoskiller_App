# mcp-redis-caching-session

**Ecoskiller | CAT-INFRA-02 — Redis In-Memory Caching & Session Store**  
MCP Server in **Java 21** | **20 Tools** | Priority: **HIGH** | Secure

---

## Overview

Java implementation of the MCP server for Ecoskiller's Redis caching and session storage layer.
Based on the *Redis Caching & Session Store Technical Specification v2.0 (March 2026)*.

Redis sits between microservices and PostgreSQL — serving cached data in **<1ms** vs 50ms for DB queries.
This MCP server exposes all Redis operations as tools that Claude and other MCP clients can call.

---

## Tools (20)

### Cache Operations (5)

| # | Tool | Description |
|---|------|-------------|
| 1 | `cache_get` | Retrieve a cached value by namespace + key. Returns hit/miss + TTL. |
| 2 | `cache_set` | Store a value with TTL. Supports `always`, `nx`, `xx` modes. |
| 3 | `cache_delete` | Invalidate a cached entry when the source data changes. |
| 4 | `cache_mget` | Batch retrieve up to 100 keys in a single round-trip (cache warming). |
| 5 | `cache_flush_namespace` | Delete all keys in a namespace. Requires `confirm=CONFIRM`. |

### Session Management (4)

| # | Tool | Description |
|---|------|-------------|
| 6 | `session_create` | Create a user session hash (post-Keycloak login). TTL: 24h. |
| 7 | `session_get` | Retrieve session data by token. Returns all fields + TTL. |
| 8 | `session_refresh` | Extend session TTL (sliding expiry on every authenticated request). |
| 9 | `session_destroy` | Immediately invalidate a session (logout / security event). |

### Leaderboard & Rankings (3)

| # | Tool | Description |
|---|------|-------------|
| 10 | `leaderboard_add` | Add/update a member's score (ZADD). Supports `nx`, `gt` modes. |
| 11 | `leaderboard_get` | Get top-N ranked entries with scores (paginated). |
| 12 | `leaderboard_rank` | Look up a single member's rank, score, and percentile. |

### Rate Limiting (2)

| # | Tool | Description |
|---|------|-------------|
| 13 | `rate_limit_check` | Atomic sliding-window check (Lua script, no race conditions). |
| 14 | `rate_limit_reset` | Clear a rate limit counter (admin). |

### Atomic Counters (3)

| # | Tool | Description |
|---|------|-------------|
| 15 | `counter_increment` | Atomically increment/decrement a named counter (INCR/INCRBY). |
| 16 | `counter_get` | Read the current counter value. |
| 17 | `counter_reset` | Reset a counter to 0. |

### Pub/Sub (1)

| # | Tool | Description |
|---|------|-------------|
| 18 | `pubsub_publish` | Publish an event to a channel (assessment:completed, leaderboard:updated, etc.). |

### Distributed Locks (2)

| # | Tool | Description |
|---|------|-------------|
| 19 | `lock_acquire` | Acquire a distributed mutex (SET NX EX). Returns lock token. |
| 20 | `lock_release` | Release a lock atomically (Lua check-and-delete). Token required. |

### Monitoring (1)

| # | Tool | Description |
|---|------|-------------|
| +1 | `redis_health` | Full health snapshot: memory, hit rate, evictions, replication, alerts. |

---

## Security Features

| Feature | Implementation |
|---------|----------------|
| **API Key Auth** | Every tool call validated. Keys hashed (SHA-256). Constant-time comparison (no timing attacks). |
| **TLS** | Enforced via `REDIS_TLS_ENABLED=true`. TLSv1.3. Custom truststore for internal CAs. |
| **Key Namespace** | All keys prefixed `ecoskiller:{namespace}:{key}`. No cross-namespace access. |
| **Input Validation** | Keys sanitised — rejects control characters, empty values, >512 char keys. |
| **Key Injection** | `\n`, `\r`, spaces in key names are rejected before hitting Redis. |
| **Pub/Sub Allowlist** | Only whitelisted channel prefixes accepted. |
| **Lock Tokens** | Distributed locks require UUID token on release (prevents accidental release). |
| **Lua Atomics** | Rate limit INCR+EXPIRE and lock release are single Lua operations — no TOCTOU. |
| **Audit Logging** | Every call, result, error, and unauthorised attempt logged (structured JSON → Wazuh). |
| **No Secrets in Logs** | API keys always masked as `***MASKED***`. Tokens never logged. |
| **Environment Variables** | Zero hardcoded secrets. All configuration via env vars (12-factor). |
| **Non-root Container** | Kubernetes deployment runs as UID 1000, read-only rootfs. |

---

## Requirements

- **Java 21+** (Eclipse Temurin recommended)
- **Maven 3.9+**
- **Redis 7+** (GCP MemoryStore or AWS ElastiCache in production)

---

## Quick Start

### 1. Set environment variables

```bash
export REDIS_HOST=localhost
export REDIS_PORT=6379
export REDIS_PASSWORD=
export REDIS_TLS_ENABLED=false
export REDIS_KEY_PREFIX=ecoskiller
export MCP_API_KEY=$(openssl rand -hex 32)
echo "Your API key: $MCP_API_KEY"
```

### 2. Build

```bash
mvn clean package -DskipTests
# Output: target/mcp-redis-server.jar
```

### 3. Run

```bash
java -jar target/mcp-redis-server.jar
```

The server listens on **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-redis-caching-session": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-redis-server.jar"],
      "env": {
        "REDIS_HOST":        "localhost",
        "REDIS_PORT":        "6379",
        "REDIS_PASSWORD":    "",
        "REDIS_TLS_ENABLED": "false",
        "REDIS_KEY_PREFIX":  "ecoskiller",
        "MCP_API_KEY":       "your-secret-key"
      }
    }
  }
}
```

---

## Run Tests

Start Redis locally first:
```bash
docker run -d -p 6379:6379 redis:7
```

Run the test suite:
```bash
export MCP_API_KEY=test-secret-key-ecoskiller
mvn test
```

---

## Production Deployment (GCP MemoryStore)

```bash
export REDIS_HOST=10.x.x.x           # MemoryStore internal IP
export REDIS_TLS_ENABLED=true         # REQUIRED for production
export REDIS_PASSWORD=your-password
export MCP_API_KEY=$(openssl rand -hex 32)
```

See `k8s-deployment.yaml` for full Kubernetes setup.

---

## Key Naming Convention

All keys follow: `{prefix}:{namespace}:{key}`

| Tool | Example Key |
|------|-------------|
| cache_set | `ecoskiller:candidate:cand-uuid-123` |
| session_create | `ecoskiller:session:jwt-token-abc` |
| leaderboard_add | `ecoskiller:leaderboard:assessment:456` |
| rate_limit_check | `ecoskiller:ratelimit:submit_assessment:cand-uuid` |
| counter_increment | `ecoskiller:counter:assessments:completed:total` |
| lock_acquire | `ecoskiller:lock:royalty:calc-789` |

---

## Protocol

| Property | Value |
|----------|-------|
| Transport | **stdio** (stdin/stdout) |
| Format | **JSON-RPC 2.0** |
| MCP Version | **2024-11-05** |
| Methods | `initialize`, `tools/list`, `tools/call`, `ping` |

---

## File Structure

```
mcp-redis-caching-session/
├── pom.xml                              ← Maven build (Java 21, fat JAR)
├── .env.example                         ← Environment variable template
├── claude_desktop_config.json           ← Claude Desktop config snippet
├── k8s-deployment.yaml                  ← Kubernetes deployment
├── README.md
└── src/
    ├── main/
    │   ├── java/com/ecoskiller/mcp/redis/
    │   │   ├── McpRedisServer.java       ← Entry point, JSON-RPC 2.0 dispatcher
    │   │   ├── config/
    │   │   │   └── RedisConfig.java      ← TLS pool, env config, key namespacing
    │   │   ├── security/
    │   │   │   ├── ApiKeyValidator.java  ← SHA-256 constant-time key validation
    │   │   │   └── AuditLogger.java     ← Structured audit log (Wazuh-ready)
    │   │   └── tools/
    │   │       ├── McpTool.java          ← Tool interface
    │   │       ├── ToolResult.java       ← Result record
    │   │       ├── BaseTool.java         ← Shared helpers, input validation
    │   │       ├── ToolRegistry.java     ← Wires all 20 tools
    │   │       ├── CacheGetTool.java
    │   │       ├── CacheSetTool.java
    │   │       ├── CacheBatchTools.java  ← cache_delete, cache_mget, cache_flush_namespace
    │   │       ├── SessionTools.java     ← session_create/get/refresh/destroy
    │   │       ├── LeaderboardTools.java ← leaderboard_add/get/rank
    │   │       ├── RateLimitTools.java   ← rate_limit_check/reset (Lua atomic)
    │   │       ├── CounterTools.java     ← counter_increment/get/reset
    │   │       ├── PubSubPublishTool.java
    │   │       ├── LockTools.java        ← lock_acquire/release (Lua atomic)
    │   │       └── RedisHealthTool.java  ← redis_health monitoring
    │   └── resources/
    │       └── logback.xml              ← Structured logging to stderr
    └── test/
        └── java/com/ecoskiller/mcp/redis/
            └── McpRedisServerTest.java  ← 20 integration tests
```
