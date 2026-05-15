# websocket-service-mcp

**Ecoskiller | WebSocket (WSS) — Realtime Bidirectional Session Control**  
MCP Server in Java | 20 Tools | Security: 10 Layers  
Protocol: JSON-RPC 2.0 / MCP 2024-11-05 | Zero External Dependencies  
Kubernetes Namespace: `realtime` | Services: `gd-orchestrator` + `interview-service` + `dojo-match-engine`

---

## Overview

Java MCP server implementing the complete Ecoskiller WebSocket service specification as Claude-callable tools.
Built directly from the specification (v1.0, 2026), covering all three session types, the full GD state
machine, Redis key patterns, JWT authentication flow, multi-tenant isolation, SLO tracking, speaker failover,
and NGINX WebSocket upgrade configuration.

**Architecture Rule (spec §01):** Three strict tiers:
1. REST/HTTPS — client-facing CRUD
2. **WebSocket/WSS — live session control signals** ← this server
3. WebRTC/SRTP via coturn — audio/video media

WebSocket carries **zero audio or video** — control plane only.

---

## Tools (20)

| # | Tool | Description |
|---|------|-------------|
| 1  | `create_session`          | Create GD/Interview/Dojo session with Redis keys |
| 2  | `connect_participant`     | JWT auth + admission (mirrors NGINX auth_request) |
| 3  | `start_gd_session`        | WAITING → INTRO, MUTE_ALL broadcast, TTL set |
| 4  | `advance_gd_state`        | Full GD state machine: INTRO→SPEAKING→OPEN_DISCUSSION→CLOSING→COMPLETED |
| 5  | `raise_hand`              | RAISE_HAND → Redis LIST → QUEUE_UPDATE broadcast |
| 6  | `push_timer_update`       | Server-authoritative TIMER_UPDATE (1s resolution, sub-100ms) |
| 7  | `push_mute_event`         | MUTE_ALL (all) / UNMUTE (speaker only — automated moderator) |
| 8  | `start_interview`         | INTERVIEW_STARTED to BOTH parties simultaneously |
| 9  | `control_interview`       | PAUSE/RESUME/END → Kafka interview.completed |
| 10 | `push_note_lock`          | NOTE_LOCK to RECRUITER only during scoring window |
| 11 | `start_dojo_match`        | MATCH_START + SCENARIO_ASSIGN individually per candidate |
| 12 | `handle_dojo_submission`  | PEER_SUBMITTED (no solution) → MATCH_END if both done |
| 13 | `handle_disconnection`    | Graceful failover — speaker advance without human moderator |
| 14 | `get_session_state`       | Full state + Redis keys + SLO + event log |
| 15 | `list_sessions`           | Tenant-scoped listing with type/environment filters |
| 16 | `get_slo_metrics`         | Interview 99.9% / GD 99% SLO dashboard + Prometheus metrics |
| 17 | `end_session`             | SESSION_END → Kafka gd.completed/interview.completed/match.scored |
| 18 | `get_redis_keys`          | All Redis key patterns per spec §10.3 with TTL values |
| 19 | `validate_jwt`            | JWT format + NGINX auth_request flow simulation |
| 20 | `get_env_config`          | WSS URLs per environment per spec §10.2 |

---

## GD State Machine

```
WAITING → INTRO → SPEAKING:{candidateId} → OPEN_DISCUSSION → CLOSING → COMPLETED
```

| Phase | Duration | Events Pushed |
|-------|----------|---------------|
| INTRO | 60s | STATE_CHANGE, MUTE_ALL (reason: INTRO), TIMER_UPDATE |
| SPEAKING | 90s | SPEAKER_CHANGE, UNMUTE → speaker only, MUTE_ALL → all others, TIMER_UPDATE |
| OPEN_DISCUSSION | 300s | STATE_CHANGE, RAISE_HAND accepted, QUEUE_UPDATE |
| CLOSING | 120s | STATE_CHANGE, MUTE_ALL (reason: CLOSING), TIMER_UPDATE |
| COMPLETED | — | SESSION_END → all clients → Kafka gd.completed |

---

## Redis Key Patterns (spec §10.3)

```
tenant:{tenantId}:gd:{sessionId}:state          = WAITING|INTRO|SPEAKING:{id}|...
tenant:{tenantId}:gd:{sessionId}:timer          = TTL:Ns  (drives state transitions on expiry)
tenant:{tenantId}:gd:{sessionId}:current_speaker = candidateId
tenant:{tenantId}:gd:{sessionId}:queue          = Redis LIST (LPUSH on RAISE_HAND)
tenant:{tenantId}:gd:{sessionId}:result         = idempotency key for gd.completed Kafka event
```

**No in-process state** — all state in Redis. Pod restarts + HPA do not cause state loss.

---

## WebSocket Event Reference

### GD Orchestrator Events
| msg.type | Target | Payload |
|----------|--------|---------|
| `SPEAKER_CHANGE` | ALL | `{ candidateId, sessionId, turnDuration: 90 }` |
| `MUTE_ALL` | ALL | `{ sessionId, reason: 'INTRO'|'CLOSING' }` |
| `UNMUTE` | Speaker ONLY | `{ candidateId, sessionId }` |
| `TIMER_UPDATE` | ALL | `{ remaining, phase }` |
| `QUEUE_UPDATE` | ALL | `{ queue: [candidateId, ...] }` |
| `STATE_CHANGE` | ALL | `{ state, sessionId }` |
| `SESSION_END` | ALL | `{ sessionId, completedAt }` |

### Interview Service Events
| msg.type | Target | Payload |
|----------|--------|---------|
| `INTERVIEW_STARTED` | BOTH | `{ interviewId, sessionId, startedAt }` |
| `TIMER_UPDATE` | BOTH | `{ elapsed, remaining }` |
| `INTERVIEW_PAUSED` | BOTH | `{ interviewId, pausedAt, reason }` |
| `INTERVIEW_COMPLETED` | BOTH | `{ interviewId, sessionId, completedAt }` |
| `NOTE_LOCK` | RECRUITER ONLY | `{ locked: true|false }` |

### Dojo Match Engine Events
| msg.type | Target | Payload |
|----------|--------|---------|
| `MATCH_START` | BOTH candidates | `{ matchId, scenarioId, durationSeconds }` |
| `SCENARIO_ASSIGN` | Individual | `{ scenarioId, title, description, constraints }` |
| `TIMER_UPDATE` | BOTH | `{ remaining }` |
| `PEER_SUBMITTED` | Opponent only | `{ matchId }` — no solution revealed |
| `MATCH_END` | BOTH | `{ matchId, completedAt }` |

---

## Environment URLs (spec §10.2)

| Environment | URL | Protocol | Notes |
|-------------|-----|----------|-------|
| DEV | `ws://localhost:8080/ws/session/{id}` | WS | Plaintext — dev only, NEVER production |
| TEST | `wss://test-api.ecoskiller.com/ws/session/{id}` | WSS | Let's Encrypt staging cert |
| STAGE | `wss://stage-api.ecoskiller.com/ws/session/{id}` | WSS | Full production topology |
| PROD | `wss://api.ecoskiller.com/ws/session/{id}` | WSS | MetalLB → NGINX → realtime namespace |

---

## Security Architecture (10 Layers)

`InputSanitizer.java` applied to every argument of every tool:

| Layer | Check |
|-------|-------|
| 1 | Shell metacharacters: `;` `&&` `|` `` ` `` `$(` `\n` `%00` |
| 2 | SQL injection: `OR` `SELECT` `UNION` `DROP` `--` `/*` |
| 3 | Script/XSS: `<script>` `javascript:` `eval(` `onload=` |
| 4 | Session ID format: `[a-zA-Z0-9_-]`, max 128 chars |
| 5 | Tenant ID format: isolation boundary enforcement |
| 6 | JWT format: 3 base64url segments — matches NGINX auth_request pre-check |
| 7 | WebSocket URL: `wss?://` pattern only |
| 8 | Oversized input: 512 chars (payload: 4096) |
| 9 | Null byte detection: path truncation prevention |
| 10 | Enum enforcement: session type, state, environment |

**Multi-tenant isolation** — `tenant_id` required on all session operations. Cross-tenant access returns empty (mirrors Redis key namespace isolation).  
**RateLimiter** — 60 req/min sliding window.

---

## SLO Targets

| Service | SLO | Alert |
|---------|-----|-------|
| Interview WSS connection stability | **99.9%** | Prometheus fires on >0.1% drop over 15min |
| GD session completion rate | **99%** | Grafana Session Health dashboard |

---

## Requirements

- **Java 8+** — zero external dependencies

---

## Build & Run

```bash
chmod +x build.sh && ./build.sh

java -jar target/websocket-service-mcp.jar      # Run server
java -cp target/websocket-service-mcp.jar \
  com.ecoskiller.websocket.server.WebSocketMcpServerTest          # Run 98 tests
java -cp target/websocket-service-mcp.jar \
  com.ecoskiller.websocket.server.WebSocketMcpServerTest --verbose # Verbose
```

---

## Connect to Claude Desktop

```json
{
  "mcpServers": {
    "websocket-service-mcp": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/websocket-service-mcp/target/websocket-service-mcp.jar"]
    }
  }
}
```

---

## File Structure

```
websocket-service-mcp/
├── build.sh
├── README.md
├── claude_desktop_config.json
└── src/main/java/com/ecoskiller/websocket/
    ├── server/
    │   ├── WebSocketMcpServer.java         ← Main JSON-RPC 2.0 loop, 20 tools
    │   └── WebSocketMcpServerTest.java     ← 98 integration tests
    ├── model/
    │   ├── Session.java                    ← Full domain: GD/Interview/Dojo state machines
    │   ├── SessionStore.java               ← Tenant-isolated session registry
    │   └── ToolResult.java
    ├── tools/
    │   ├── McpTool.java
    │   ├── CreateSessionTool.java          ← Tool 1
    │   ├── ConnectParticipantTool.java     ← Tool 2
    │   ├── StartGdSessionTool.java         ← Tool 3
    │   ├── AdvanceGdStateTool.java         ← Tool 4
    │   ├── RaiseHandTool.java              ← Tool 5
    │   ├── PushTimerUpdateTool.java        ← Tool 6
    │   ├── PushMuteEventTool.java          ← Tool 7
    │   ├── StartInterviewTool.java         ← Tool 8
    │   ├── ControlInterviewTool.java       ← Tool 9
    │   ├── PushNoteLockTool.java           ← Tool 10
    │   ├── StartDojoMatchTool.java         ← Tool 11
    │   ├── HandleDojoSubmissionTool.java   ← Tool 12
    │   ├── HandleDisconnectionTool.java    ← Tool 13
    │   ├── GetSessionStateTool.java        ← Tool 14
    │   ├── ListSessionsTool.java           ← Tool 15
    │   ├── GetSloMetricsTool.java          ← Tool 16
    │   ├── EndSessionTool.java             ← Tool 17
    │   ├── GetRedisKeysTool.java           ← Tool 18
    │   ├── ValidateJwtTool.java            ← Tool 19
    │   └── GetEnvironmentConfigTool.java   ← Tool 20
    ├── security/
    │   ├── InputSanitizer.java             ← 10-layer injection defense
    │   └── RateLimiter.java               ← 60 req/min sliding window
    └── util/
        ├── JsonUtils.java                  ← Zero-dep JSON-RPC 2.0
        └── Logger.java                    ← Structured JSON stderr logger
```

---

## Protocol

- Transport : **stdio** (stdin/stdout)
- Format    : **JSON-RPC 2.0**
- MCP Ver   : **2024-11-05**
- Methods   : `initialize`, `tools/list`, `tools/call`, `ping`
