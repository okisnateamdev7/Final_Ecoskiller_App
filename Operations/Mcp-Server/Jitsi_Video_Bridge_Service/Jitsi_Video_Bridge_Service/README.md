# mcp-jitsi-videobridge

**Ecoskiller | CAT-JVB — Real-Time Video Communication & Conference Management**  
MCP Server in **Java** | **14 Agents** | Priority: **PRODUCTION-CRITICAL**  
Security: JWT (HS256) · Rate Limiting · Input Sanitization · No External Dependencies

---

## Agents (14 Tools)

| # | Tool Name | Description |
|---|-----------|-------------|
| 1 | `conference_create` | Create a Jitsi conference for GD/TI/Panel/Case Study (JWT required) |
| 2 | `conference_terminate` | Terminate conference, finalise recording to S3/GCS (JWT required) |
| 3 | `conference_status` | Live status: participants, active speaker, RTP stats |
| 4 | `participant_join` | Register participant join, emit to Kafka `assessment.participants` |
| 5 | `participant_remove` | Moderator kick with reason logging (JWT required) |
| 6 | `active_speaker_detection` | Query/configure ASD — 500ms update interval, per-speaker duration |
| 7 | `bandwidth_management` | REMB stats, bitrate caps, adaptive resolution per participant |
| 8 | `recording_control` | Start/stop/pause Jibri recording to S3/GCS with SSE-KMS (JWT required) |
| 9 | `recording_status` | Check recording status, retrieve encrypted S3 path |
| 10 | `bridge_health` | Prometheus metrics for all 8 JVBS nodes, Jicofo, Jibri, Prosody |
| 11 | `bridge_scaling` | Kubernetes HPA: scale 4–20 replicas, CPU-based triggers (JWT required) |
| 12 | `kafka_event_emit` | Emit structured events to assessment.{participants,speakers,recordings} |
| 13 | `jwt_generate` | Generate HS256 JWT for participant auth, includes role/moderator claims (JWT required) |
| 14 | `network_diagnostics` | Full STUN/TURN/ICE/Jicofo/Jibri connectivity check |

---

## Requirements

- **Java 17+** (uses switch expressions, `List.of`, `Map.of`, text blocks)
- **No external dependencies** — zero JAR files needed

---

## Build & Run

```bash
# Compile
mkdir -p out
find src -name "*.java" | xargs javac -d out

# Set JWT secret (from Kubernetes secret / environment)
export JITSI_JWT_SECRET="your-32-char-minimum-secret-here"

# Run server (communicates via stdin/stdout — MCP stdio transport)
java -cp out io.ecoskiller.mcp.jitsi.server.McpServer
```

---

## Run Tests

```bash
# Quick pass/fail
java -cp out io.ecoskiller.mcp.jitsi.TestRunner

# With full output
java -cp out io.ecoskiller.mcp.jitsi.TestRunner --verbose
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-jitsi-videobridge": {
      "command": "java",
      "args": ["-cp", "/absolute/path/to/mcp-jitsi-java/out",
               "io.ecoskiller.mcp.jitsi.server.McpServer"],
      "env": {
        "JITSI_JWT_SECRET": "<your-secret-from-k8s>"
      }
    }
  }
}
```

---

## Security Architecture

### JWT Authentication (HMAC-SHA256)
Tools that modify infrastructure or access sensitive data require a valid JWT:
- `conference_create`, `conference_terminate`, `participant_remove`
- `recording_control`, `bridge_scaling`, `jwt_generate`

Pass the token in the `_auth_token` argument:
```json
{
  "name": "conference_create",
  "arguments": {
    "_auth_token": "Bearer eyJ...",
    "assessment_id": "gd-12345",
    ...
  }
}
```

JWT claims validated:
- `aud` must be `"jitsi"`
- `iss` must be `"ecoskiller"`
- `exp` must be in the future
- Signature verified with `JITSI_JWT_SECRET` (rotate every 30 days)

### Rate Limiting
- 60 requests / 60 seconds per tool (token bucket)
- Error code `-32029` on limit exceeded

### Input Sanitization
- `assessment_id`: alphanumeric + hyphens only (3–64 chars)
- `participant_name`: HTML special chars stripped (`< > & " '`)
- `storage_path`: must start with `s3://` or `gs://`
- `email`: RFC-5322 format validated
- All integer params validated against documented ranges

### No Secret Leakage
- JWT secret loaded from environment only — never logged
- Error messages sanitized (no stack traces, no file paths)
- JUL logging goes to **stderr** — never pollutes MCP stdio channel

---

## Kafka Events Emitted

| Topic | Events |
|-------|--------|
| `assessment.participants` | `participant_joined`, `participant_left`, `conference_started`, `conference_ended` |
| `assessment.speakers` | `active_speaker_changed`, `speaker_timeout`, `mute_change` |
| `assessment.recordings` | `recording_started`, `recording_paused`, `recording_resumed`, `recording_completed` |

---

## File Structure

```
mcp-jitsi-java/
├── src/
│   ├── main/java/io/ecoskiller/mcp/jitsi/
│   │   ├── server/
│   │   │   ├── McpServer.java          ← Main MCP server (JSON-RPC 2.0 loop)
│   │   │   ├── JsonParser.java         ← Zero-dependency JSON parser
│   │   │   └── JsonSerializer.java     ← Zero-dependency JSON serializer
│   │   ├── security/
│   │   │   ├── JwtValidator.java       ← HS256 JWT validation
│   │   │   └── RateLimiter.java        ← Token-bucket rate limiter
│   │   ├── model/
│   │   │   └── ConferenceStore.java    ← In-memory conference registry
│   │   └── tools/
│   │       ├── ToolHandler.java        ← Tool contract interface
│   │       ├── ConferenceCreateTool.java
│   │       └── AllTools.java           ← Remaining 13 tools
│   └── test/java/io/ecoskiller/mcp/jitsi/
│       └── TestRunner.java             ← Full test suite (35 tests)
└── README.md
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Ecoskiller Integration Notes

- Jitsi host: `jitsi.ecoskiller.io`
- STUN: `stun.ecoskiller.io:3478`
- TURN: `turn.ecoskiller.io:3478` (ephemeral credentials, 1-hour TTL)
- Bridge pods: 8 × JVBS (Kubernetes Deployment, HPA 4–20)
- Recordings: `s3://ecoskiller-recordings/` (SSE-KMS, 90-day retention)
- Metrics: `http://grafana.ecoskiller.local:3000`
- Slack: `#video-infrastructure`
- Runbooks: `https://wiki.ecoskiller.io/runbooks/jitsi`
