# jicofo-mcp-server

**Ecoskiller | Jicofo (Jitsi Conference Focus) MCP Server**  
Java 21 · MCP v2024-11-05 · JSON-RPC 2.0 via stdio · 12 Tools · Security-First

---

## Overview

This MCP server exposes Jicofo's full conference management capabilities as LLM-accessible tools.
It covers the complete Jicofo architecture from the Ecoskiller platform docs:

| # | Tool | Jicofo Section |
|---|------|----------------|
| 1 | `room_lifecycle` | §3.1 / §4.1 — Conference room create/destroy/status |
| 2 | `participant_management` | §3.4 / §4.1 — Join, leave, kick, list participants |
| 3 | `jvb_bridge_selection` | §3.2 / §4.2 — Least-loaded JVB instance selection |
| 4 | `jvb_health_monitor` | §5.4 — Bridge health, thresholds, alerts |
| 5 | `jwt_token_validation` | §3.3 — Room authorization JWT validation |
| 6 | `session_state` | §4.4 — Redis state machine (WAITING→SPEAKING→COMPLETED) |
| 7 | `speaking_turn_enforcement` | §4.3 / §5.2 — Mute/unmute <100ms, GD turn control |
| 8 | `bridge_failover` | §5.5 — Bridge failover <2s interruption |
| 9 | `audio_only_mode` | §5.6 — 40% CPU savings for GD sessions |
| 10 | `pstn_bridge` | §3.6 — FreeSWITCH / phone participant integration |
| 11 | `health_status` | §7.5 — Liveness, readiness, dependency checks |
| 12 | `metrics` | §7.5 — Prometheus metrics proxy |

---

## Security Architecture

```
LLM → MCP tool call
         │
         ▼
  ┌──────────────────────────────────────────────────────┐
  │  JwtValidator                                        │
  │  - Validates HS256 bearer JWT from caller            │
  │  - Checks: signature, expiry, iss=ecoskiller-auth    │
  │  - Reads secret from JICOFO_MCP_JWT_SECRET env var   │
  └───────────────────────┬──────────────────────────────┘
                          │ PASS
                          ▼
  ┌──────────────────────────────────────────────────────┐
  │  RateLimiter                                         │
  │  - 100 requests/minute per caller identity (sub)     │
  │  - Sliding window, thread-safe                       │
  └───────────────────────┬──────────────────────────────┘
                          │ PASS
                          ▼
  ┌──────────────────────────────────────────────────────┐
  │  Tool Dispatch + Input Sanitization                  │
  │  - Strips <>"'`;\ from string params                 │
  │  - Validates session_id / participant_id format      │
  │  - JWT tokens never logged (redacted in audit)       │
  └───────────────────────┬──────────────────────────────┘
                          │
                          ▼
  ┌──────────────────────────────────────────────────────┐
  │  AuditLogger                                         │
  │  - Structured JSON to stderr (Grafana Loki format)   │
  │  - Logs: timestamp, tool, status, caller, args       │
  │  - JWT/token values redacted before logging          │
  └──────────────────────────────────────────────────────┘
```

---

## Requirements

- **Java 21+** (uses switch expressions, Records)
- **Maven 3.8+** (for build)
- **No external service needed** — runs standalone; tools simulate Jicofo responses
  *(Plug in real Jitsi REST API calls in production)*

---

## Build

```bash
cd jicofo-mcp-server

# Compile + test
mvn clean verify

# Build fat JAR (includes all dependencies)
mvn clean package

# Fat JAR location:
# target/jicofo-mcp-server-1.0.0-fat.jar
```

---

## Run

```bash
# DEV mode (no JWT enforcement — only for local testing)
java -jar target/jicofo-mcp-server-1.0.0-fat.jar

# PRODUCTION mode (JWT enforced)
export JICOFO_MCP_JWT_SECRET="your-hmac-sha256-secret-minimum-32-chars"
java -jar target/jicofo-mcp-server-1.0.0-fat.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).
Logs go to **stderr** (structured JSON, Grafana Loki compatible).

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "jicofo-mcp": {
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/to/jicofo-mcp-server/target/jicofo-mcp-server-1.0.0-fat.jar"
      ],
      "env": {
        "JICOFO_MCP_JWT_SECRET": "your-hmac-sha256-secret-here",
        "JICOFO_MCP_CALLER_JWT": "optional-server-level-jwt-for-trusted-callers"
      }
    }
  }
}
```

---

## Environment Variables

| Variable | Required | Description |
|----------|----------|-------------|
| `JICOFO_MCP_JWT_SECRET` | **Prod** | HMAC-SHA256 secret for JWT validation. If unset, runs in DEV mode (no JWT checks). |
| `JICOFO_MCP_CALLER_JWT` | Optional | Server-level JWT for trusted callers (bypasses per-request `caller_jwt` arg). |

---

## Tool Call Examples (JSON-RPC 2.0)

### Create a GD room
```json
{
  "jsonrpc": "2.0", "id": "1", "method": "tools/call",
  "params": {
    "name": "room_lifecycle",
    "arguments": {
      "caller_jwt": "eyJhbGciOiJIUzI1NiJ9...",
      "operation": "CREATE",
      "session_id": "gd-2026-batch-001",
      "session_type": "GROUP_DISCUSSION",
      "max_participants": 8,
      "tenant_id": "acme-corp"
    }
  }
}
```

### Mute all participants (GD turn control)
```json
{
  "jsonrpc": "2.0", "id": "2", "method": "tools/call",
  "params": {
    "name": "speaking_turn_enforcement",
    "arguments": {
      "caller_jwt": "eyJhbGciOiJIUzI1NiJ9...",
      "operation": "MUTE_ALL",
      "session_id": "gd-2026-batch-001",
      "source": "GD_ORCHESTRATOR"
    }
  }
}
```

### Trigger bridge failover
```json
{
  "jsonrpc": "2.0", "id": "3", "method": "tools/call",
  "params": {
    "name": "bridge_failover",
    "arguments": {
      "caller_jwt": "eyJhbGciOiJIUzI1NiJ9...",
      "operation": "TRIGGER_FAILOVER",
      "bridge_id": "jvb-aws-2",
      "reason": "CPU_HIGH"
    }
  }
}
```

---

## File Structure

```
jicofo-mcp-server/
├── pom.xml                                          ← Maven build (Java 21, fat JAR)
├── README.md
├── claude_desktop_config.json                       ← Claude Desktop config snippet
└── src/main/java/com/ecoskiller/mcp/jicofo/
    ├── server/
    │   ├── JicofoMcpServer.java                     ← Main MCP server (JSON-RPC 2.0 loop)
    │   └── ToolBase.java                            ← McpTool interface, BaseTool, ToolRegistry
    ├── security/
    │   ├── JwtValidator.java                        ← HS256 JWT validation
    │   └── RateLimiter.java                         ← Sliding window rate limiter + AuditLogger
    └── tools/
        ├── RoomLifecycleTool.java                   ← Tool 1: room_lifecycle
        ├── ParticipantManagementTool.java           ← Tool 2: participant_management
        ├── JvbBridgeSelectionTool.java              ← Tool 3: jvb_bridge_selection
        ├── CoreTools.java                           ← Tools 4–8: health_monitor, jwt, session_state,
        │                                               speaking_turn, bridge_failover
        └── UtilityTools.java                        ← Tools 9–12: audio_only, pstn, health, metrics
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Production Integration Notes

This server is architected for real Jicofo integration. To connect to live services:

1. **Replace JVB calls** in `JvbBridgeSelectionTool` / `JvbHealthMonitorTool` with real Jitsi Bridge REST API calls to `http://jvb-host:8080/about/stats`
2. **Replace Redis operations** in `SessionStateTool` / `ParticipantManagementTool` with `jedis` or `lettuce` Redis client
3. **Replace Kafka events** in tools with `kafka-clients` producer publishing to `gd.orchestrator` topic
4. **Replace XMPP stanzas** in `SpeakingTurnEnforcementTool` with `Smack` library calls to Prosody

---

*Ecoskiller Platform Engineering · March 2026 · Classification: Technical Documentation*
