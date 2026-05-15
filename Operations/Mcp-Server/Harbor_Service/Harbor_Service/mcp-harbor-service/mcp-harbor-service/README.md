# mcp-harbor-service

**Ecoskiller | CAT-05 — Container Registry**  
MCP Server in Java | 12 Agents | Priority: MEDIUM

---

## Overview

Self-hosted Harbor container registry MCP server for the Ecoskiller platform.  
Wraps the **Harbor REST API v2.0** — gives Claude full visibility and control over:
- All 13+ `ecoskiller/*` microservice images stored at `harbor.ecoskiller.internal`
- GitLab CI Trivy scan gates, SHA tagging strategy, and env-latest tag promotions
- Tag retention policy (last-10 SHAs / 90-day auto-delete) and Longhorn PVC GC
- Robot accounts (`HARBOR_USER` / `HARBOR_PASSWORD`), `imagePullSecret` management
- Webhook notifications (push → GitLab CI or Mattermost `#incidents`)
- Prometheus metrics, registry health, and compliance-grade audit logs

---

## Agents (12)

| # | Tool Name | Description |
|---|-----------|-------------|
| 1 | `image_list` | List images in `ecoskiller/*` project |
| 2 | `image_push_status` | Check latest SHA tags, push timestamps, scan status |
| 3 | `image_pull_validate` | Validate a tag exists before Helm deploy |
| 4 | `tag_retention_status` | Inspect retention policy; trigger dry-run or GC |
| 5 | `vulnerability_report` | Trivy CVE scan results per image |
| 6 | `robot_account_manage` | Manage Harbor robot accounts (CI push / k3s pull) |
| 7 | `project_quota_check` | Storage quota and Longhorn PVC usage |
| 8 | `webhook_manage` | Configure push-event webhooks |
| 9 | `image_tag_promote` | Promote SHA → env-latest or semver tag |
| 10 | `registry_health` | Harbor `/health` + Prometheus metrics |
| 11 | `proxy_cache_manage` | DockerHub / GHCR proxy cache rules |
| 12 | `audit_log_query` | Push / pull / delete audit logs |

---

## Requirements

- **Java 17+** — no external packages, pure stdlib
- Harbor instance at `harbor.ecoskiller.internal` (or any `HARBOR_URL`)
- Robot account with push/pull permissions on the `ecoskiller` project

---

## Build

```bash
cd src
javac --release 17 HarborMcpServer.java

# Optional: package as self-contained JAR
mkdir -p ../dist
jar cfe ../dist/mcp-harbor-service.jar HarborMcpServer *.class
```

---

## Run

```bash
# Using compiled classes
export HARBOR_URL=https://harbor.ecoskiller.internal
export HARBOR_USER=robot$gitlab-ci-push
export HARBOR_PASSWORD=<secret>   # stored as Kubernetes Secret in ops namespace

cd src && java HarborMcpServer

# Using JAR
java -jar dist/mcp-harbor-service.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `HARBOR_URL` | ✅ | — | `https://harbor.ecoskiller.internal` |
| `HARBOR_USER` | ✅ | — | Robot account username |
| `HARBOR_PASSWORD` | ✅ | — | Robot account secret (**never hardcode**) |
| `HARBOR_PROJECT` | — | `ecoskiller` | Harbor project namespace |
| `HARBOR_API_TIMEOUT_MS` | — | `10000` | HTTP request timeout (ms) |
| `HARBOR_TLS_VERIFY` | — | `true` | Set `false` for local dev only |
| `HARBOR_MCP_AUDIT_LOG` | — | `/var/log/ecoskiller/harbor-mcp-audit.log` | Audit log path |

**Production**: credentials are mounted from Kubernetes Secrets in the `ops` namespace — never stored in GitLab CI variables or Helm values.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-harbor-service": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-harbor-service/dist/mcp-harbor-service.jar"],
      "env": {
        "HARBOR_URL": "https://harbor.ecoskiller.internal",
        "HARBOR_USER": "robot$gitlab-ci-push",
        "HARBOR_PASSWORD": "<secret>"
      }
    }
  }
}
```

---

## Run Tests

```bash
# Quick pass/fail (37 tests: protocol, all 12 agents, security, validation)
python3 test_agents.py

# With full JSON output
python3 test_agents.py --verbose
```

Tests cover:
- MCP protocol: `initialize`, `ping`, `tools/list`, unknown method
- All 12 tool registrations confirmed
- All 12 tool calls (gracefully handle missing `HARBOR_URL` — server structure is verified)
- **Security**: shell metachar injection, path traversal, oversized strings, invalid tag formats, invalid service names
- **Validation**: missing required arguments

---

## Security

| Layer | Mechanism |
|-------|-----------|
| Credential storage | Kubernetes Secrets (ops namespace) — never in Helm values or env files |
| CI push credentials | `HARBOR_USER` / `HARBOR_PASSWORD` as protected + masked GitLab CI Variables |
| k3s pull credentials | `imagePullSecret` per namespace — read-only robot account |
| Input validation | Shell metachar deny-list, path traversal detection, length limits, tool name allowlist |
| Tag validation | Regex: SHA hex, `env-latest`, `vX.Y.Z` — rejects all other formats |
| Rate limiting | 100 requests/minute per server instance |
| Audit logging | Structured pipe-delimited log: timestamp, level, event, request ID |
| TLS | Verification enabled by default; disable only via `HARBOR_TLS_VERIFY=false` (dev) |
| Robot accounts | Secrets returned **once** on creation/rotation — must be stored immediately |

---

## File Structure

```
mcp-harbor-service/
├── src/
│   └── HarborMcpServer.java    ← Single-file server (all 12 agents, zero deps)
├── dist/
│   └── mcp-harbor-service.jar  ← Self-contained executable JAR
├── test_agents.py              ← Test all 12 agents (37 tests)
├── claude_desktop_config.json  ← Claude Desktop config snippet
├── Dockerfile                  ← Multi-stage build for k3s ops namespace
└── README.md
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Harbor Infrastructure Context

```
Registry URL : harbor.ecoskiller.internal        (CoreDNS — cluster-internal)
Namespace    : ecoskiller/                        (all 13+ custom images)
K8s namespace: ops                                (co-located with Prometheus, Grafana, Loki)
Storage      : Longhorn PVC (3-replica)
Tag strategy : {CI_COMMIT_SHA} + :env-latest + v{semver}
Retention    : last 10 SHAs/env + auto-delete >90 days
Security gate: Trivy --severity CRITICAL,HIGH --exit-code 1 (blocks push on any CVE)
DR posture   : No failover needed — co-located in k3s cluster
CI/CD        : GitLab CI → Trivy scan → push to Harbor → Helm deploy → k3s
```
