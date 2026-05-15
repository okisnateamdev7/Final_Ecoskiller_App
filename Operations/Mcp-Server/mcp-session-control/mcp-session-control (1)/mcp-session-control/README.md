# mcp-session-control

**Ecoskiller | CAT-REALTIME-06 — Session Control Service**
MCP Server in **Java 21** | **24 Tools** | Priority: **HIGH** | Secure

---

## Overview

Java MCP server wrapping the Ecoskiller **Session-Control-Service** REST API.
Built from the *Session-Control-Service Technical Documentation v1.0 (March 2026)*.

Session-Control-Service is the **authoritative source of truth** for all assessment session state
across Ecoskiller's hiring platform. This MCP server exposes the full session lifecycle as
Claude-callable tools — from provisioning a new session to terminating it, with full participant
management, Jitsi WebRTC room control, FSM state transitions, and compliance audit logging.

---

## Tools (24)

### Session Lifecycle (6)

| # | Tool | Description |
|---|------|-------------|
| 1 | `session_create` | Provision a new assessment session — allocates Jitsi room, validates participants. |
| 2 | `session_get` | Retrieve session metadata, current state, participants, Jitsi room ID. |
| 3 | `session_list` | List sessions filtered by tenant, state, or assessment type. |
| 4 | `session_update_state` | Admin FSM transition with optimistic locking (version number). |
| 5 | `session_extend` | Extend session expiry time (max 1 hour per extension). |
| 6 | `session_archive` | Archive a COMPLETED session to cold storage. Requires `confirm=CONFIRM`. |

### Participant Management (6)

| # | Tool | Description |
|---|------|-------------|
| 7 | `participant_join` | JWT-validated join request. Returns Jitsi room credentials. |
| 8 | `participant_leave` | Record voluntary or network-error disconnection. |
| 9 | `participant_list` | List all participants with roles, presence status, heartbeat. |
| 10 | `participant_heartbeat` | Update last heartbeat — prevents 2-minute auto-eviction. |
| 11 | `participant_evict` | Force-evict a participant (ASSESSOR/MODERATOR only). Requires `confirm=CONFIRM`. |
| 12 | `participant_update_role` | Change participant role mid-session (promote/demote). |

### Jitsi Room Management (3)

| # | Tool | Description |
|---|------|-------------|
| 13 | `jitsi_get_token` | Generate a 5-minute Jitsi JWT token for WebRTC room access. |
| 14 | `jitsi_mute` | Mute/unmute participant audio or video. Use `participant_id=ALL` for room-wide mute. |
| 15 | `jitsi_force_end` | Force-end Jitsi room and disconnect all participants. Requires `confirm=CONFIRM`. |

### Audit & Compliance (3)

| # | Tool | Description |
|---|------|-------------|
| 16 | `session_audit_log` | Immutable audit log — all state changes, participant actions (GDPR/SOC2/HIPAA). |
| 17 | `session_events` | Ordered list of Kafka events published (session.created, participant_joined, etc.). |
| 18 | `session_export` | Full session export — metadata + participants + audit + events (GDPR requests). |

### State Machine Shortcuts (4)

| # | Tool | Description |
|---|------|-------------|
| 19 | `session_start` | WAITING_FOR_PARTICIPANTS → IN_PROGRESS. Publishes session.started event. |
| 20 | `session_suspend` | IN_PROGRESS → SUSPENDED (emergency pause). Timer pauses. |
| 21 | `session_resume` | SUSPENDED → IN_PROGRESS. Timer resumes. |
| 22 | `session_terminate` | Any active state → COMPLETED. Force-ends room, triggers analytics. Requires `confirm=CONFIRM`. |

### Health & Monitoring (2)

| # | Tool | Description |
|---|------|-------------|
| 23 | `service_health` | Kubernetes liveness + readiness probes, service latency. |
| 24 | `session_stats` | Real-time stats: active sessions, participant counts, type distribution. |

---

## Session State Machine (FSM)

```
CREATED → WAITING_FOR_PARTICIPANTS → IN_PROGRESS ⇌ SUSPENDED
                                         ↓
                                     COMPLETED → ARCHIVED
```

Optimistic locking via `version` field prevents concurrent update conflicts.

---

## Security Features

| Feature | Implementation |
|---------|----------------|
| **API Key Auth** | Every call validated. SHA-256 hashed. Constant-time comparison (no timing attacks). |
| **Key Rotation** | MCP_API_KEY + MCP_API_KEY_2 — zero-downtime rotation. |
| **JWT Auth to Service** | Bearer token (Keycloak service account) forwarded on every API call. |
| **TLS** | `SESSION_SERVICE_TLS_VERIFY=true` enforced by default. |
| **UUID Validation** | Session IDs and participant IDs validated — path traversal (`../`) rejected. |
| **Safety Guards** | `confirm=CONFIRM` required for destructive ops: archive, evict, terminate, force-end. |
| **Optimistic Locking** | Version number prevents race conditions on state transitions (409 on conflict). |
| **Idempotency** | idempotency_key on write operations prevents double-processing. |
| **Role Enforcement** | Evict/mute require ASSESSOR/MODERATOR — enforced by session-control-service. |
| **Duration Limit** | Session duration capped at SESSION_MAX_DURATION_SECS (default: 4 hours). |
| **Audit Logging** | All calls, errors, auth failures → structured JSON → Wazuh SIEM (stderr). |
| **Secret Masking** | api_key, token, jwt, participantToken always masked in audit logs. |
| **Non-root Container** | Kubernetes: UID 1000, read-only rootfs, all Linux caps dropped. |

---

## Requirements

- **Java 21+**
- **Maven 3.9+**
- **Session-Control-Service** running and accessible

---

## Quick Start

```bash
# Export config
export SESSION_SERVICE_BASE_URL=http://localhost:3000
export SESSION_SERVICE_AUTH_TYPE=none
export MCP_API_KEY=$(openssl rand -hex 32)
echo "API Key: $MCP_API_KEY"

# Build
mvn clean package -DskipTests

# Run
java -jar target/mcp-session-control-server.jar
```

---

## Run Tests

```bash
export MCP_API_KEY=test-secret-key-session
export SESSION_SERVICE_BASE_URL=http://localhost:3000
mvn test
```

Security/validation tests run without the service. Integration tests require it.

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
mcp-session-control/
├── pom.xml
├── .env.example
├── claude_desktop_config.json
├── k8s-deployment.yaml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/ecoskiller/mcp/session/
    │   │   ├── McpSessionControlServer.java       ← Entry point, JSON-RPC dispatcher
    │   │   ├── config/SessionControlConfig.java   ← Env-var config
    │   │   ├── client/SessionControlClient.java   ← Full REST API client
    │   │   ├── security/
    │   │   │   ├── ApiKeyValidator.java
    │   │   │   └── AuditLogger.java
    │   │   └── tools/
    │   │       ├── BaseToolClasses.java           ← McpTool, ToolResult, BaseTool
    │   │       ├── SessionLifecycleTools.java     ← 6 lifecycle tools
    │   │       ├── ParticipantTools.java          ← 6 participant tools
    │   │       ├── RemainingTools.java            ← 12 Jitsi/audit/FSM/health tools
    │   │       └── ToolRegistry.java              ← Wires all 24 tools
    │   └── resources/logback.xml
    └── test/
        └── McpSessionControlServerTest.java       ← 22 tests
```
