# mcp-gitlab-ci

**EcoSkiller | GitLab CI/CD Pipeline Management**
MCP Server in Java | 13 Agents | Priority: HIGH | Security: Defence-in-Depth

---

## Agents (13)

| # | Tool Name | Agent | Description |
|---|-----------|-------|-------------|
| 1 | `pipeline_trigger` | PIPELINE_TRIGGER_AGENT | Trigger CI pipeline with branch-based env routing |
| 2 | `pipeline_status` | PIPELINE_STATUS_AGENT | Real-time pipeline stage breakdown |
| 3 | `pipeline_cancel` | PIPELINE_CANCEL_AGENT | Cancel pipeline + cleanup K8s executor pods |
| 4 | `container_vulnerability_scan` | CONTAINER_VULN_SCAN_AGENT | Trivy CVE scan — blocks CRITICAL/HIGH |
| 5 | `helm_deploy` | HELM_DEPLOY_AGENT | `helm upgrade --install --atomic` to target env |
| 6 | `helm_rollback` | HELM_ROLLBACK_AGENT | One-click rollback (< 1 min), audit-logged |
| 7 | `kubernetes_health_check` | K8S_HEALTH_CHECK_AGENT | Pod readiness + /healthz + P95 latency |
| 8 | `merge_request_gate` | MERGE_REQUEST_GATE_AGENT | Coverage ≥ 80%, no CVEs, approvals met |
| 9 | `artifact_retention` | ARTIFACT_RETENTION_AGENT | List/purge artifacts (30d dev, 90d prod) |
| 10 | `deployment_audit_log` | DEPLOYMENT_AUDIT_LOG_AGENT | Immutable ClickHouse audit queries |
| 11 | `harbor_registry_manage` | HARBOR_REGISTRY_AGENT | List images, GC, enforce tag retention |
| 12 | `slack_notify` | SLACK_NOTIFY_AGENT | #deployments channel + PagerDuty P0 |
| 13 | `environment_promote` | ENVIRONMENT_PROMOTE_AGENT | Enforced chain: dev→test→stage→prod |

---

## Security Model

| Layer | Mechanism |
|-------|-----------|
| Input Sanitisation | Regex blocks shell injection, path traversal, SQL injection, XSS |
| Rate Limiting | 100 requests / 60 seconds per tool (sliding window) |
| Argument Length Caps | All string fields capped (32–2048 chars per field) |
| Environment Allowlist | Only `dev`, `test`, `stage`, `prod` accepted |
| Branch Allowlist | Only `feature/*`, `develop`, `staging`, `main` accepted |
| Git SHA Validation | Must match `[0-9a-f]{7,40}` |
| Service Name Validation | Alphanumeric + dash/underscore only, max 80 chars |
| Promotion Chain | Cannot skip environments (dev→prod rejected) |
| Log Sanitisation | Control chars stripped; secrets masked (`[MASKED]`) |
| Error Isolation | Stack traces never returned to client |
| Secrets | Never in code — injected via K8s Secrets / env vars |

---

## Requirements

- Java 17+
- Maven 3.8+ (for build)
- Python 3.8+ (for tests, no extra packages)

---

## Build

```bash
mvn package -q
# Produces: target/mcp-gitlab-ci.jar (fat jar, ~8 MB)
```

---

## Run the server

```bash
java -jar target/mcp-gitlab-ci.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).
Logs go to **stderr** (never stdout — stdout is reserved for protocol).

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-gitlab-ci": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-gitlab-ci/target/mcp-gitlab-ci.jar"],
      "env": {
        "GITLAB_URL":        "https://gitlab.ecoskiller.com",
        "HARBOR_URL":        "https://harbor.ecoskiller.com",
        "SLACK_WEBHOOK_URL": "${SLACK_WEBHOOK_URL}",
        "KUBECONFIG_PROD":   "${KUBECONFIG_PROD}"
      }
    }
  }
}
```

---

## Run tests

```bash
mvn package -q                        # build first
python3 test_agents.py                # pass/fail summary (22 tests)
python3 test_agents.py --verbose      # with full JSON output
```

---

## Pipeline Flow (EcoSkiller)

```
git push feature/* → DEV  (auto)
                   ↓
         develop merge → TEST  (auto)
                       ↓
              staging branch → STAGE  (manual gate)
                             ↓
                    main + 2 approvals → PROD  (approval required)
```

---

## File Structure

```
mcp-gitlab-ci/
├── pom.xml                            ← Maven build (Java 17, fat jar)
├── README.md
├── claude_desktop_config.json         ← Claude Desktop config snippet
├── test_agents.py                     ← Test all 13 agents + 3 security tests
└── src/main/java/com/ecoskiller/mcp/gitlab/
    ├── server/
    │   └── GitLabCIMcpServer.java     ← Main entrypoint, stdio event loop
    ├── tools/
    │   └── GitLabCITools.java         ← All 13 agent implementations
    ├── security/
    │   └── SecurityValidator.java     ← Input validation, rate limiting
    └── models/
        ├── JsonRpc.java               ← JSON-RPC 2.0 envelope types
        ├── ToolDefinition.java        ← MCP tool schema descriptor
        └── ToolResult.java            ← MCP tools/call response wrapper
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Environment Variables (secrets — never hardcode)

| Variable | Purpose |
|----------|---------|
| `GITLAB_URL` | Self-hosted GitLab base URL |
| `HARBOR_URL` | Harbor private registry URL |
| `MINIO_ENDPOINT` | MinIO artifact storage URL |
| `SLACK_WEBHOOK_URL` | Slack incoming webhook (K8s Secret) |
| `KUBECONFIG_DEV/TEST/STAGE/PROD` | Per-env kubeconfig (K8s Secret) |
| `PAGERDUTY_ROUTING_KEY` | PagerDuty Events API key |
