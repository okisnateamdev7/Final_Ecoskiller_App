# jitsi-web-client-mcp

**Ecoskiller | CAT-JITSI — Assessment Video Conferencing**  
MCP Server in Java | 15 Agents | Security: JWT + RBAC + Rate Limiting | Priority: HIGH

---

## Agents (15 Tools)

| # | Tool Name                    | Agent / Role                          | Auth Required     |
|---|------------------------------|---------------------------------------|-------------------|
| 1 | `session_create`             | Create assessment room                | admin, interviewer|
| 2 | `session_join`               | Join an assessment room               | all roles         |
| 3 | `session_leave`              | Leave a session gracefully            | all roles         |
| 4 | `session_status`             | Real-time session status              | all roles         |
| 5 | `media_quality_get`          | WebRTC bitrate/latency/codec metrics  | all roles         |
| 6 | `media_device_list`          | List cameras, mics, speakers          | all roles         |
| 7 | `participant_list`           | Roster with presence/speaking status  | all roles         |
| 8 | `participant_mute`           | Mute/unmute a participant             | interviewer, admin|
| 9 | `participant_remove`         | Kick a participant                    | admin only        |
|10 | `recording_start`            | Start Jibri recording                 | interviewer, admin|
|11 | `recording_stop`             | Stop Jibri recording                  | interviewer, admin|
|12 | `recording_status`           | Check recording state                 | all roles         |
|13 | `analytics_session_report`   | Full session analytics report         | interviewer, admin|
|14 | `analytics_event_emit`       | Emit user interaction event           | all roles         |
|15 | `auth_token_validate`        | Validate & decode JWT token           | all roles         |

---

## Requirements

- **Java 17+** (no external packages, no Maven/Gradle required)
- Python 3.x (for running test suite only)

---

## Build

```bash
chmod +x build.sh run.sh
./build.sh
```

---

## Run the Server

```bash
# Development (uses default insecure secret)
./run.sh

# Production (set JWT secret via environment variable)
export JITSI_MCP_JWT_SECRET="your-32+-char-secret-key-here"
./run.sh
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "jitsi-web-client-mcp": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/jitsi-mcp-server/target/jitsi-web-client-mcp.jar"],
      "env": {
        "JITSI_MCP_JWT_SECRET": "your-production-secret-key-here"
      }
    }
  }
}
```

---

## Run Tests

```bash
python3 test_agents.py           # quick pass/fail
python3 test_agents.py --verbose # with full JSON output
```

---

## Security Features

| Feature                    | Implementation                                          |
|----------------------------|---------------------------------------------------------|
| **JWT Authentication**     | HMAC-SHA256 signature verification (no external libs)   |
| **Token Expiry**           | `exp` claim checked on every request                    |
| **Replay Attack Prevention** | `jti` nonce tracking with 1-hour TTL                  |
| **RBAC**                   | Per-tool role enforcement (admin > interviewer > candidate) |
| **Rate Limiting**          | 100 requests/60s sliding window per client              |
| **Input Sanitization**     | Room IDs, user IDs, display names validated + sanitized |
| **Constant-Time Compare**  | HMAC comparison uses constant-time equals (timing-safe) |
| **Audit Logging**          | All auth events, tool calls, and failures logged        |
| **Payload Size Guard**     | Requests >10MB rejected immediately                     |
| **Stderr-only Logging**    | Logs go to stderr — stdout reserved for clean JSON-RPC  |

### JWT Token Structure

All tool calls require a Bearer JWT in `jwt_token` argument:

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
{
  "sub": "cand-001",
  "role": "candidate",
  "assessmentId": "gd-12345",
  "roomId": "gd-12345",
  "exp": 1709000000,
  "iat": 1708996400,
  "iss": "ecoskiller.io",
  "jti": "unique-nonce-per-request"
}
```

**Roles and permissions:**

| Role          | Allowed Tools                                                  |
|---------------|----------------------------------------------------------------|
| `admin`       | All 15 tools                                                   |
| `interviewer` | All except `participant_remove`                               |
| `candidate`   | session_*, media_*, participant_list, recording_status, analytics_event_emit, auth_token_validate |

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## File Structure

```
jitsi-mcp-server/
├── build.sh                          ← Build script (compiles + creates JAR)
├── run.sh                            ← Run script with JVM flags
├── test_agents.py                    ← Test all 15 agents
├── claude_desktop_config.json        ← Claude Desktop config snippet
├── README.md                         ← This file
└── src/main/java/io/ecoskiller/mcp/
    ├── server/
    │   └── JitsiMcpServer.java       ← Main MCP server (JSON-RPC loop)
    ├── security/
    │   ├── SecurityManager.java      ← JWT, RBAC, rate limiting, audit
    │   └── JwtClaims.java            ← JWT claims model
    ├── tools/
    │   ├── ToolRegistry.java         ← All 15 tool implementations
    │   └── ToolResult.java           ← Result + exception types
    └── utils/
        └── JsonUtils.java            ← Zero-dependency JSON serializer/parser
```

---

## Integration with Ecoskiller Infrastructure

This MCP server is designed to integrate with:

- **Prosody XMPP Server** — Room join/leave signaling (WebSocket to `prosody.ecoskiller.io:5280`)
- **Jitsi Video Bridge** — WebRTC media forwarding (VP8/H.264, Opus)
- **Jibri Recording** — Session recording start/stop
- **Analytics Backend** — Kafka events pipeline (`ecoskiller.analytics.jitsi.events`)
- **CDN (Cloudflare/Akamai)** — Static asset delivery for React SPA
- **Assessment Backend APIs** — JWT issuance, room config, participant metadata

---

*Ecoskiller Platform — Jitsi Web Client Service MCP Server*  
*Version 1.0.0 | Java 17 | MCP 2024-11-05*
