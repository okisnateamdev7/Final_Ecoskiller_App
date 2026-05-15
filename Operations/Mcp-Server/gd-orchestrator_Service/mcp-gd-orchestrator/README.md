# mcp-gd-orchestrator

**Ecoskiller | CAT-GD — Group Discussion Session Orchestration**
MCP Server in Java | 16 Agents | Priority: CRITICAL

---

## Agents (16)

| # | Tool Name | Agent | Description |
|---|-----------|-------|-------------|
| 1 | `session_lifecycle_manager` | SESSION_LIFECYCLE_MANAGER | Full state machine: WAITING → SCHEDULED → ACTIVE → RECORDING → COMPLETED / FAILED |
| 2 | `participant_coordinator` | PARTICIPANT_COORDINATOR | Enrol/track candidates, JWT validation, roster validation (3–5 participants), join method control |
| 3 | `freeswitch_room_provisioner` | FREESWITCH_ROOM_PROVISIONER | Provision/teardown SIP conference rooms `sip:gd-{id}@freeswitch.internal`, 200 MOH channels, auto-record |
| 4 | `webrtc_sip_bridge` | WEBRTC_SIP_BRIDGE | WebRTC ↔ SIP transparent bridge, SDP validation, SIP user agent registration, coturn ICE |
| 5 | `pstn_call_handler` | PSTN_CALL_HANDLER | IVR PIN validation, DPDP Act 2023 consent prompt, ANI tracking, callback, conference transfer |
| 6 | `audio_recording_manager` | AUDIO_RECORDING_MANAGER | Start/stop WAV recording, MinIO multipart upload, checksum validation, DPDP soft-delete |
| 7 | `speaker_turn_tracker` | SPEAKER_TURN_TRACKER | Real-time turn recording, Pyannote diarisation metrics, fairness distribution analysis |
| 8 | `transcription_pipeline` | TRANSCRIPTION_PIPELINE | Whisper STT job orchestration, 12-language support, word-level timestamps, PostgreSQL storage |
| 9 | `scoring_event_emitter` | SCORING_EVENT_EMITTER | Prerequisite validation + emit `gd.session.completed` Kafka event to scoring engine |
| 10 | `dropout_rejoin_handler` | DROPOUT_REJOIN_HANDLER | 2-minute rejoin window, dropout reason tracking, fairness scoring integration |
| 11 | `timeout_duration_enforcer` | TIMEOUT_DURATION_ENFORCER | 5 min minimum / 20 min maximum enforcement, auto-stop, overrun logging |
| 12 | `audio_quality_normaliser` | AUDIO_QUALITY_NORMALISER | PSTN 8kHz ↔ WebRTC 48kHz → unified 16kHz codec, scoring fairness adjustment tags |
| 13 | `redis_state_sync` | REDIS_STATE_SYNC | Redis pub-sub <500ms state sync, `gd-session:{id}` key management, Socket.io broadcast |
| 14 | `consent_compliance_manager` | CONSENT_COMPLIANCE_MANAGER | DPDP Act 2023: consent recording, 90-day audio / 1-year transcript retention, deletion rights |
| 15 | `session_audit_logger` | SESSION_AUDIT_LOGGER | ClickHouse immutable audit log, SOC2 CC7.2 event trail, export for compliance |
| 16 | `scaling_load_manager` | SCALING_LOAD_MANAGER | Session affinity hashing (`session_id % replicas`), HPA metrics reporting, replica rebalancing |

---

## GD Session State Machine

```
WAITING  ──► SCHEDULED  ──► ACTIVE  ──► RECORDING  ──► COMPLETED
   │              │            │                              ▲
   └──────────────┴────────────┴──────────────────► FAILED ──┘

WAITING    : Session created, recruiting ≥3 candidates
SCHEDULED  : Min candidates reached, awaiting operator START
ACTIVE     : Recording live, FreeSWITCH conference running
RECORDING  : Intermediate — finalising WAV file (<1 sec)
COMPLETED  : Kafka event emitted → scoring engine running
FAILED     : No-show / insufficient duration / critical error
```

---

## Requirements

- Java 17+
- No external dependencies — pure Java stdlib

---

## Build

```bash
javac --release 17 -d out \
  src/main/java/io/ecoskiller/mcp/gd/json/Json.java \
  src/main/java/io/ecoskiller/mcp/gd/GDMCPServer.java \
  src/main/java/io/ecoskiller/mcp/gd/GDAgents.java

jar cfe mcp-gd-orchestrator-1.0.0.jar io.ecoskiller.mcp.gd.GDMCPServer -C out .
```

---

## Run the server

```bash
java -jar mcp-gd-orchestrator-1.0.0.jar
```

Communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).
Logs written to **stderr** — stdout stays clean for JSON-RPC.

---

## Run tests (60 tests)

```bash
# Compile tests alongside main sources, then:
java -cp out io.ecoskiller.mcp.gd.GDAgentsTest
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-gd-orchestrator": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-gd-orchestrator-1.0.0.jar"]
    }
  }
}
```

---

## Docker (containerise the MCP server itself)

```bash
docker build -t harbor.ecoskiller.io/mcp-gd-orchestrator:v1.0.0 .
docker push harbor.ecoskiller.io/mcp-gd-orchestrator:v1.0.0
```

---

## Security

| Layer | Mechanism |
|-------|-----------|
| Session IDs | UUID regex allowlist — SQL/path injection blocked |
| Candidate IDs | Alphanumeric/hyphen/underscore, 3–64 chars |
| Phone numbers | E.164 format enforced (`+<country><number>`) |
| PIN codes | Numeric 4–8 digits only; **always masked in output** |
| JWT tokens | 3-segment structure + base64url chars validated; **masked in output** |
| Phone numbers | Partially masked in all output (`+91****10`) |
| SDP payloads | Max 8KB, must contain `v=0` SIP header |
| Payloads | Redis: 4KB limit; Audit detail: 2KB limit |
| GD topics | Allowlist: word chars + punctuation, 5–200 chars |
| State transitions | Validated against allowed graph — no arbitrary jumps |
| Kafka topics | Allowlist: `gd.*` namespace only |
| Recording URLs | Must start with `s3://recordings/` |
| Log injection | Control characters stripped from all logged values |
| Container | Non-root UID 1000, `eclipse-temurin:17-jre-alpine` |
| JVM | `-Xmx256m`, `-XX:+ExitOnOutOfMemoryError` |

---

## Infrastructure Context

| Component | Technology | Role |
|-----------|-----------|------|
| Conference bridge | FreeSWITCH 1.10.x (TCP:8021 Event Socket) | SIP rooms, audio bridge, RTP relay |
| Database | PostgreSQL 15 — `gd_sessions`, `gd_participants`, `gd_transcripts`, `gd_speaker_turns` | Persistent state |
| Cache / Pub-Sub | Redis 7.x — key `gd-session:{id}` | Hot state, <500ms sync |
| Event bus | Apache Kafka 3.6.x — `gd.session.scheduled` → `gd.session.completed` | Async pipeline |
| Audio storage | MinIO — `s3://recordings/{date}/{session_id}.wav` | WAV files, 3-node replication |
| Transcription | Whisper.cpp self-hosted GPU pod (`large-v3`) | STT, word-level timestamps |
| Speaker diarisation | Pyannote 3.0.x HTTP service | Speaker turns (`who spoke when`) |
| Auth | Keycloak 24.0 JWT validation | Candidate identity |
| Deployment | k3s StatefulSet, 3 replicas, session affinity hashing | GCP asia-south1 + AWS ap-south-1 |

---

## File Structure

```
mcp-gd-orchestrator/
├── pom.xml                                                        ← Maven build
├── Dockerfile                                                     ← Multi-stage, non-root, JRE 17 Alpine
├── claude_desktop_config.json
├── README.md
└── src/
    ├── main/java/io/ecoskiller/mcp/gd/
    │   ├── json/Json.java                                         ← Zero-dep JSON (parse + emit)
    │   ├── GDMCPServer.java                                       ← JSON-RPC 2.0 dispatcher + security layer
    │   └── GDAgents.java                                          ← 16 agent implementations
    └── test/java/io/ecoskiller/mcp/gd/
        └── GDAgentsTest.java                                      ← 60 tests (zero-dep, no JUnit)
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Compliance

- **DPDP Act 2023**: Consent recorded before audio capture; recordings deleted after 90 days; transcripts after 1 year; user deletion rights honoured
- **SOC2 CC7.2**: Immutable ClickHouse audit trail for all session events
- **Fairness**: PSTN (8kHz) and WebRTC (48kHz) candidates normalised to 16kHz; scoring model applies quality adjustment tags
