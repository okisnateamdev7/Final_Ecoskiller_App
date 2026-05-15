# mcp-coturn-service

**Ecoskiller | CAT-03 — NAT Traversal / Media**  
MCP Server in Java | 13 Agents | Priority: MEDIUM

---

## Overview

Self-contained MCP server wrapping the coturn TURN/STUN infrastructure for the Ecoskiller
platform's WebRTC media layer. coturn is the **Phase 1 Critical** NAT traversal service —
without it, ~30% of users on enterprise/mobile networks cannot connect to live assessments.

**Topology**: 4 stateless VM instances — 2× GCP `e2-small` (asia-south1) + 2× AWS `t3.small` (ap-south-1).  
**Not inside k3s** — dedicated VMs with public static/elastic IPs.  
**DNS**: `media.ecoskiller.com` → coturn IP (DNS-only, no proxy).  
**Protocols**: TURN + STUN — UDP 3478, TLS 5349, relay UDP 49152–65535.

---

## Agents (13)

| # | Tool | Description |
|---|------|-------------|
| 1 | `server_status` | Health/liveness of all 4 coturn VM instances |
| 2 | `relay_session_stats` | Active TURN allocations, relay bandwidth, port pool usage |
| 3 | `credential_generate` | HMAC-SHA1 time-limited TURN credentials (RFC 5766) |
| 4 | `credential_validate` | Validate TURN credentials — expiry, HMAC, tamper detection |
| 5 | `config_view` | Read turnserver.conf (static-auth-secret always redacted) |
| 6 | `config_update` | Safely update config params with backup + diff |
| 7 | `firewall_check` | Verify GCP/AWS firewall rules for TURN/relay ports |
| 8 | `dns_check` | Verify media.ecoskiller.com DNS A-record resolution |
| 9 | `connectivity_test` | STUN/TCP probe to confirm coturn is reachable |
| 10 | `log_query` | Query coturn access logs — auth failures, relay events |
| 11 | `instance_manage` | Start/stop/restart/status for coturn process on VM |
| 12 | `peer_deny_audit` | Audit RFC 1918 denied-peer-ip rules (SSRF protection) |
| 13 | `slo_report` | WebRTC connection success rate vs ≥95% SLO target |

---

## Requirements

- **Java 17+** — zero external packages, pure stdlib + JCE (HMAC-SHA1)
- `COTURN_TURN_SECRET` env var for `credential_generate` and `credential_validate`

---

## Build

```bash
cd src
javac --release 17 CoturnMcpServer.java

# Optional: self-contained JAR
mkdir -p ../dist
jar cfe ../dist/mcp-coturn-service.jar CoturnMcpServer *.class
```

---

## Run

```bash
export COTURN_TURN_SECRET=<shared-secret>   # Kubernetes Secret in media namespace

# From source
cd src && java CoturnMcpServer

# From JAR
java -jar dist/mcp-coturn-service.jar
```

Server communicates via **stdin/stdout** JSON-RPC 2.0.

---

## Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `COTURN_TURN_SECRET` | ✅ (for crypto tools) | — | Shared TURN secret. Stored as Kubernetes Secret in `media` namespace. |
| `COTURN_REALM` | — | `media.ecoskiller.com` | TURN realm |
| `COTURN_DNS_NAME` | — | `media.ecoskiller.com` | DNS name for connectivity tests |
| `COTURN_INSTANCES` | — | `coturn-gcp-1:3478,...` | Comma-separated `host:port` list |
| `COTURN_LOG_DIR` | — | `/var/log/coturn` | coturn log directory |
| `COTURN_CONF_PATH` | — | `/etc/coturn/turnserver.conf` | Config file path |
| `COTURN_CRED_TTL_SECS` | — | `86400` (24h) | Default credential TTL |
| `COTURN_MIN_PORT` | — | `49152` | Relay port range start |
| `COTURN_MAX_PORT` | — | `65535` | Relay port range end |
| `COTURN_MCP_AUDIT_LOG` | — | `/var/log/ecoskiller/coturn-mcp-audit.log` | Audit log path |

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-coturn-service": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-coturn-service/dist/mcp-coturn-service.jar"],
      "env": {
        "COTURN_TURN_SECRET": "<secret — from Kubernetes Secret in media namespace>"
      }
    }
  }
}
```

---

## Run Tests

```bash
# Quick pass/fail (57 tests)
python3 test_agents.py

# Verbose with full JSON
python3 test_agents.py --verbose
```

Tests cover:
- MCP protocol: `initialize`, `ping`, `tools/list`, unknown method
- All 13 tool registrations confirmed
- All 13 tool calls (env-not-set handled gracefully)
- **HMAC crypto round-trip**: generate → validate correct → tampered rejected → expired detected → ICE URLs present
- **Security**: shell injection, path traversal, oversized strings, metachar in username, protected-key write attempt, deeply nested args
- **Validation**: missing required args, invalid TTL, port out of range

---

## Security

| Layer | Mechanism |
|-------|-----------|
| `static-auth-secret` | Always redacted in `config_view` output — never readable via MCP |
| `COTURN_TURN_SECRET` | Kubernetes Secret (media namespace) — never hardcoded |
| Protected config keys | `config_update` rejects `static-auth-secret`, `lt-cred-mech`, `use-auth-secret` |
| Input validation | Shell metachar deny-list `[;&\|`$<>()'\"{}!\\]`, path traversal detection, 1024-char limit |
| HMAC credentials | RFC 5766 HMAC-SHA1, time-limited; tamper detection on validate |
| RFC 1918 protection | `peer_deny_audit` verifies `denied-peer-ip` rules block internal k3s network |
| Rate limiting | 100 requests/minute |
| Audit logging | Structured log: timestamp, level, event, request ID |
| Tool name allowlist | Regex `^[a-z][a-z0-9_]{0,63}$` |

---

## coturn Infrastructure Reference

```
Instances   : 4 total (2× GCP asia-south1 e2-small, 2× AWS ap-south-1 t3.small)
Deployment  : Dedicated VMs — NOT inside k3s. Static/Elastic IPs.
DNS         : media.ecoskiller.com → coturn public IP (DNS-only, no proxy, TTL 300s)
Ports       : UDP 3478 (STUN/TURN signalling), TLS 5349, UDP 49152-65535 (relay)
Auth        : HMAC-SHA1 long-term credentials (RFC 5766) — generated by Jitsi prosody
Relay path  : Client ↔ coturn ↔ Jitsi JVB — SRTP never enters k3s service mesh
SLO target  : WebRTC connection success ≥95%. Alert: <90% over 15 min → P1
Business    : ~30% of enterprise/mobile users require TURN relay
Config      : /etc/coturn/turnserver.conf (systemd service or Docker container on VM)
Logs        : /var/log/coturn/turnserver.log → Promtail → Loki → Grafana Media QoS
```

---

## File Structure

```
mcp-coturn-service/
├── src/
│   └── CoturnMcpServer.java     ← Single-file server (13 agents, zero deps)
├── dist/
│   └── mcp-coturn-service.jar   ← Self-contained executable JAR (32KB)
├── test_agents.py               ← 57-test suite
├── claude_desktop_config.json   ← Claude Desktop config snippet
├── Dockerfile                   ← Multi-stage build
└── README.md
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`
