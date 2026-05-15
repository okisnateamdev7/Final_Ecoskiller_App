# mcp-webrtc-ecoskiller

**Ecoskiller | WebRTC — Real-Time Communication MCP Server**  
MCP Server in **Java 17** | **12 Secure Agents** | Priority: HIGH

---

## Overview

Secure Java MCP Server implementing the full WebRTC protocol layer for the
Ecoskiller AI-powered talent assessment platform. Covers GD (Group Discussion),
Interview, Dojo, and PSTN sessions — with mandatory DTLS-SRTP encryption, ICE/TURN
NAT traversal, GD Orchestrator mute control, and DPDPA 2023 audit compliance.

---

## Agents (12)

| # | Tool Name                  | Agent                              | Responsibility                                      |
|---|----------------------------|------------------------------------|-----------------------------------------------------|
| 1 | `webrtc_session_create`    | WEBRTC_SESSION_CREATE_AGENT        | Create GD/Interview/Dojo sessions + Jitsi config    |
| 2 | `webrtc_session_terminate` | WEBRTC_SESSION_TERMINATE_AGENT     | Graceful teardown, TURN release, audit trail        |
| 3 | `webrtc_jwt_issue`         | WEBRTC_JWT_ISSUE_AGENT             | Validate 5-min Keycloak JWT for Prosody room join   |
| 4 | `webrtc_ice_negotiate`     | WEBRTC_ICE_NEGOTIATE_AGENT         | ICE candidate processing, coturn STUN/TURN routing  |
| 5 | `webrtc_sdp_offer_answer`  | WEBRTC_SDP_OFFER_ANSWER_AGENT      | SDP offer/answer, codec negotiation, DTLS-SRTP      |
| 6 | `webrtc_dtls_srtp_status`  | WEBRTC_DTLS_SRTP_STATUS_AGENT      | DTLS handshake status, AES-128-CM, DPDPA evidence   |
| 7 | `webrtc_media_quality`     | WEBRTC_MEDIA_QUALITY_AGENT         | RTCStatsReport MOS score, SLO alerting              |
| 8 | `webrtc_gd_mute_control`   | WEBRTC_GD_MUTE_CONTROL_AGENT       | Turn-based mute/unmute, speaking metrics, AI score  |
| 9 | `webrtc_turn_allocation`   | WEBRTC_TURN_ALLOCATION_AGENT       | coturn TURN relay allocation, HMAC-SHA1 credentials |
|10 | `webrtc_pstn_bridge`       | WEBRTC_PSTN_BRIDGE_AGENT           | FreeSWITCH mod_verto PSTN phone participant bridge  |
|11 | `webrtc_participant_stats` | WEBRTC_PARTICIPANT_STATS_AGENT     | Per-participant RTCStatsReport, STT quality QA      |
|12 | `webrtc_audit_log`         | WEBRTC_AUDIT_LOG_AGENT             | DPDPA 2023 tamper-evident audit log (SHA-256)       |

---

## Security Architecture

### Input Validation
- **Tool name whitelist** — only the 12 defined tools are callable
- **JSON-RPC structural validation** — method injection blocked
- **Session / Tenant / User ID regex** — alphanumeric + hyphens only
- **DTLS fingerprint format** — SHA-256 hex pairs (32 × `XX:`) enforced
- **ICE candidate sanitisation** — injection vectors blocked
- **JWT format validation** — three Base64url segments required
- **E.164 phone number validation** — for PSTN bridge
- **Codec / mode whitelists** — only known-safe values accepted
- **Integer range checking** — all numeric fields bounded
- **Rate limiting** — 30 calls/minute per caller identity (sliding window)

### Media Security
| Layer | Standard | Detail |
|-------|----------|--------|
| Transport | DTLS 1.2/1.3 | RFC 6347 / RFC 9147 — per-session key exchange |
| Media encryption | SRTP AES-128-CM | RFC 3711 — mandatory, no plaintext media |
| Key derivation | DTLS-SRTP | RFC 5764 — from DTLS handshake |
| NAT traversal | ICE + TURN | RFC 8445 + RFC 5766 — coturn relay |
| Audio codec | Opus | RFC 6716 — FEC + PLC, 6–510 kbps adaptive |
| Compliance | DPDPA 2023 | Section 4(b) — audio encrypted in transit |

### Architecture Principle
> **Media (WebRTC/Jitsi) NEVER passes through backend API servers.**  
> Direct path: Client ↔ coturn ↔ JVB only.

---

## Requirements

- **Java 17+** (uses switch expressions, text blocks)
- **Maven 3.8+** (for build)
- **No runtime secrets** — all crypto lives in Keycloak/coturn/JVB
- **No external network access** — pure stdio MCP server

---

## Build & Run

### 1. Build fat JAR
```bash
cd mcp-webrtc-java
mvn clean package -q
# Output: target/mcp-webrtc-ecoskiller.jar
```

### 2. Run the server
```bash
java -jar target/mcp-webrtc-ecoskiller.jar
```
The server communicates via **stdin/stdout** using JSON-RPC 2.0.

### 3. Run tests
```bash
mvn test             # all 25+ tests
mvn test -q          # quiet (pass/fail only)
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-webrtc-ecoskiller": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/target/mcp-webrtc-ecoskiller.jar"]
    }
  }
}
```

---

## File Structure

```
mcp-webrtc-java/
├── pom.xml                              ← Maven build (Java 17, fat JAR)
├── claude_desktop_config.json          ← Claude Desktop config snippet
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp/webrtc/
    │   ├── server/
    │   │   └── McpWebRTCServer.java    ← Main MCP server (JSON-RPC 2.0 router)
    │   ├── security/
    │   │   └── SecurityValidator.java  ← Input validation, rate limiting, audit
    │   ├── model/
    │   │   └── AgentResult.java        ← Standardised agent result envelope
    │   └── agents/
    │       ├── BaseAgent.java          ← Abstract base: getToolDefinition() + execute()
    │       ├── WebRTCSessionCreateAgent.java
    │       ├── WebRTCSessionTerminateAgent.java
    │       ├── WebRTCJwtIssueAgent.java
    │       ├── WebRTCIceNegotiateAgent.java
    │       ├── WebRTCSdpOfferAnswerAgent.java
    │       ├── WebRTCDtlsSrtpStatusAgent.java
    │       ├── WebRTCMediaQualityAgent.java
    │       ├── WebRTCGdMuteControlAgent.java
    │       ├── WebRTCTurnAllocationAgent.java
    │       ├── WebRTCPstnBridgeAgent.java
    │       ├── WebRTCParticipantStatsAgent.java
    │       └── WebRTCAuditLogAgent.java
    └── test/java/com/ecoskiller/mcp/webrtc/
        └── TestAgents.java             ← 25+ tests covering all agents + security
```

---

## Protocol

| Property | Value |
|----------|-------|
| Transport | **stdio** (stdin/stdout) |
| Format | **JSON-RPC 2.0** |
| MCP Version | **2024-11-05** |
| Methods | `initialize`, `tools/list`, `tools/call`, `ping` |
| Language | **Java 17** |
| Dependencies | `org.json:json:20240303` only |

---

## WebRTC Session Flow (Ecoskiller GD)

```
PHASE 1  →  JWT acquisition (GET /api/v1/auth/media-token)
PHASE 2  →  Jitsi API init (JitsiMeetExternalAPI + getUserMedia)
PHASE 3  →  XMPP connect + JWT validation (Prosody)
PHASE 4  →  SDP offer/answer (via Prosody Jingle → JVB)
PHASE 5  →  ICE gathering (host → srflx/STUN → relay/TURN)
PHASE 6  →  DTLS handshake + SRTP key derivation (AES-128-CM)
PHASE 7  →  Live SRTP media flowing (Client ↔ coturn ↔ JVB)
PHASE 8  →  GD Orchestrator WebSocket mute/unmute control
PHASE 9  →  SESSION_END → api.dispose() → audit trail written
```

---

*Ecoskiller Platform | WebRTC MCP Server | Java 17 | v1.0.0*
