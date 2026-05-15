# mcp-jitsi-stack

**Ecoskiller | CAT-Jitsi — Jitsi Media Stack MCP Server**
Secure Java MCP Server | 12 Agents | Priority: HIGH

---

## Overview

Real-time audio/video infrastructure MCP server for the Ecoskiller AI-powered talent
assessment platform. Powers Group Discussions (GD), one-on-one interviews, and Dojo
coding sessions via the Jitsi stack: **JVB · Jicofo · Prosody · Jitsi Web**.

All 4 components pinned to **stable-9364**. Media flows:
`Client ↔ coturn ↔ JVB` — **Media NEVER passes through backend API servers.**

---

## Agents (12)

| # | Tool Name | Agent Class |
|---|-----------|-------------|
| 1 | `jvb_stream_routing` | JVB_STREAM_ROUTING_AGENT |
| 2 | `jvb_quality_metrics` | JVB_QUALITY_METRICS_AGENT |
| 3 | `jicofo_room_lifecycle` | JICOFO_ROOM_LIFECYCLE_AGENT |
| 4 | `jicofo_participant_routing` | JICOFO_PARTICIPANT_ROUTING_AGENT |
| 5 | `prosody_jwt_validation` | PROSODY_JWT_VALIDATION_AGENT |
| 6 | `prosody_xmpp_signalling` | PROSODY_XMPP_SIGNALLING_AGENT |
| 7 | `jitsi_web_session` | JITSI_WEB_SESSION_AGENT |
| 8 | `coturn_nat_traversal` | COTURN_NAT_TRAVERSAL_AGENT |
| 9 | `gd_session_orchestrator` | GD_SESSION_ORCHESTRATOR_AGENT |
| 10 | `media_security_audit` | MEDIA_SECURITY_AUDIT_AGENT |
| 11 | `freeswitch_pstn_bridge` | FREESWITCH_PSTN_BRIDGE_AGENT |
| 12 | `jitsi_health_monitor` | JITSI_HEALTH_MONITOR_AGENT |

---

## Security Model

| Layer | Mechanism |
|-------|-----------|
| Raw input | 64 KB size cap — prevents memory exhaustion / DoS |
| Tool dispatch | Strict allow-list — unknown tools rejected (no reflection-based dispatch) |
| Arguments | Depth limit (5), string length cap (2 KB), array size cap (100 elements) |
| Injection | Pattern blocking: SQL, XMPP stanza, shell meta-chars, path traversal |
| Session IDs | `[a-zA-Z0-9_-]{1,128}` only |
| JWT tokens | Shape validation (3-part base64url, ≤ 4096 chars) before any downstream use |
| Error messages | Sanitised — no stack traces, file paths, or internal class names returned |
| Media encryption | DTLS-SRTP end-to-end between client and JVB |
| Auth | JWT HS256, 5-min TTL via Keycloak 24.0 — validated by Prosody |
| Network | Kubernetes media namespace — NetworkPolicy blocks ingress from core namespace |
| CVE scanning | Trivy via GitLab CI — CRITICAL/HIGH blocks deploy |
| Image registry | Harbor (harbor.ecoskiller.internal) — all images scanned before deploy |

---

## Requirements

- **Java 17+**
- **Maven 3.8+** (for build)
- No external runtime dependencies beyond Jackson (bundled in fat JAR)

---

## Build

```bash
cd mcp-jitsi-stack
mvn clean package -q
# Output: target/mcp-jitsi-stack.jar
```

---

## Run the Server

```bash
java -jar target/mcp-jitsi-stack.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).
All logs go to **stderr** — never stdout.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-jitsi-stack": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-jitsi-stack/target/mcp-jitsi-stack.jar"]
    }
  }
}
```

---

## Run Tests

```bash
mvn test                          # All 30+ tests
mvn test -Dsurefire.useFile=false # Verbose output
```

---

## File Structure

```
mcp-jitsi-stack/
├── pom.xml                                  ← Maven build (Java 17, fat JAR)
├── claude_desktop_config.json               ← Claude Desktop config snippet
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp/jitsi/
    │   ├── server/
    │   │   └── JitsiMcpServer.java          ← Main MCP server (JSON-RPC 2.0 stdio)
    │   ├── agents/
    │   │   ├── AgentHandler.java            ← Agent interface
    │   │   ├── AgentResult.java             ← Standard result envelope
    │   │   ├── JvbStreamRoutingAgent.java   ← Agent 1: JVB SFU stream routing
    │   │   ├── JvbQualityMetricsAgent.java  ← Agent 2: JVB QoS / Grafana metrics
    │   │   ├── JicofoRoomLifecycleAgent.java← Agent 3: Conference room lifecycle
    │   │   ├── JicofoParticipantRouting...  ← Agent 4: Participant routing, mute/unmute
    │   │   ├── ProsodyJwtValidationAgent.java← Agent 5: JWT validation (5-min TTL)
    │   │   ├── ProsodyXmppSignallingAgent.. ← Agent 6: XMPP / ICE / SDP
    │   │   ├── JitsiWebSessionAgent.java    ← Agent 7: Browser/Flutter session config
    │   │   ├── CoturnNatTraversalAgent.java ← Agent 8: TURN/STUN NAT traversal
    │   │   ├── GdSessionOrchestratorAgent.. ← Agent 9: GD Redis state machine
    │   │   ├── MediaSecurityAuditAgent.java ← Agent 10: SRTP/CVE/network audit
    │   │   ├── FreeswitchPstnBridgeAgent... ← Agent 11: PSTN mod_verto bridge
    │   │   └── JitsiHealthMonitorAgent.java ← Agent 12: Full-stack health + runbook
    │   └── security/
    │       └── McpSecurityManager.java      ← Centralised security enforcement
    └── test/java/com/ecoskiller/mcp/jitsi/
        └── JitsiMcpServerTest.java          ← 30+ JUnit 5 tests (agents + security + protocol)
```

---

## Protocol

- **Transport:** stdio (stdin/stdout)
- **Format:** JSON-RPC 2.0
- **MCP Version:** 2024-11-05
- **Methods:** `initialize`, `tools/list`, `tools/call`, `ping`

---

## Architecture Reference

```
auth-service (Keycloak 24.0)
        │ issues JWT (5-min TTL, HS256)
        ▼
Jitsi Prosody (XMPP + JWT validation)  ← port 5222/5347/5280
        │ XMPP/Jingle
        ▼
Jitsi Jicofo (Conference Focus)        ← room lifecycle, JVB selection
        │ REST API
        ▼
Jitsi JVB (SFU)                        ← UDP 10000 / RTP/SRTP
        ▲
        │ RTP/SRTP (via coturn if NAT)
Browser / Flutter / PSTN (FreeSWITCH mod_verto:8081)

coturn VM: media.ecoskiller.com
  UDP 3478 (STUN/TURN) · 5349 (TURNS) · 49152-65535 (relay)

Kubernetes: media namespace
  GCP c2-standard-4 / AWS c5.xlarge (4 vCPU) · 2 nodes · 50 GB SSD
  NetworkPolicy: NO ingress from core namespace
```

---

## Key Rules (from Ecoskiller Infrastructure v15)

1. **Room naming:** `roomName` MUST equal `session_id` or `match_id` — required for ClickHouse AI scoring correlation.
2. **Media isolation:** Media NEVER passes through backend API servers (Architecture Principle #5).
3. **Audio-only GD:** `startWithVideoMuted: true` for all GD sessions — enables objective AI scoring.
4. **Pre-join disabled:** `prejoinPageEnabled: false` — GD speaking clock starts at room entry.
5. **Version pinned:** `stable-9364` — do NOT change to unstable.
6. **JWT TTL:** 5-minute tokens only — re-issue on expiry; Prosody rejects expired tokens.

---

*Ecoskiller Platform | Jitsi Media Stack (JVB · Jicofo · Prosody · Web) | v1.0*
