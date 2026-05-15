# mcp-helm-service

**Ecoskiller | Helm Service**  
MCP Server in Java | 15 Agents | Priority: HIGH

---

## Two Helms in One Server

This MCP server covers **both** meanings of "Helm" in the Ecoskiller platform:

### 1. Helm v3.x — Kubernetes Package Manager (10 tools)
Used in the CI/CD pipeline: `GitLab CI → Harbor (self-hosted) → Helm → k3s`  
Manages all 50 Ecoskiller service deployments (13 custom microservices + 37 infrastructure charts).

### 2. Helm — GD Session Orchestrator Microservice (5 tools)
The deterministic control plane for Group Discussion assessment sessions.  
Eliminates recruiter bias through strict protocol enforcement, computed speaking order, and numeric-only metrics.

---

## Agents (15 Tools)

### Helm K8s Package Manager

| # | Tool | Description |
|---|------|-------------|
| 1 | `helm_install` | Deploy new release. RFC 1123 name validation, 64 KB values limit. |
| 2 | `helm_upgrade` | Upgrade or install. Increments revision. Blue/green support. |
| 3 | `helm_rollback` | Roll back to previous revision. `confirm_uninstall` not required. |
| 4 | `helm_uninstall` | Remove release. **Requires `confirm_uninstall=true`** to prevent accidents. |
| 5 | `helm_list_releases` | List releases across namespaces with status summary. |
| 6 | `helm_get_status` | Detailed release status + K8s resource counts. |
| 7 | `helm_get_values` | User-supplied and chart-default values for a release. |
| 8 | `helm_get_history` | Full revision history (run before rollback to pick target). |
| 9 | `helm_lint_chart` | Validate chart syntax. Runs in GitLab CI lint stage. |
| 10 | `helm_diff_upgrade` | Preview changes before upgrade (helm-diff plugin equivalent). |

### Helm Session Orchestrator

| # | Tool | Description |
|---|------|-------------|
| 11 | `session_create` | Create GD room. Returns deterministic speaking protocol. |
| 12 | `session_join` | Register participant. Requires `consent_given=true` (DPDP 2023). |
| 13 | `session_get_status` | Session state, speaking queue, current speaker, time remaining. |
| 14 | `session_advance_turn` | Mark turn complete, mute/unmute via Jitsi commands, advance queue. |
| 15 | `session_get_metrics` | Aggregated behavioral metrics (no audio/text stored, ever). |

---

## Security Features

| Layer | Detail |
|-------|--------|
| Tool allowlist | 15 tools only — unknown tool names rejected |
| Release name | RFC 1123: lowercase alphanumeric + hyphens, max 53 chars (Kubernetes limit) |
| Namespace allowlist | `dev | test | stage | prod | core | analytics | billing | monitoring | infrastructure` |
| Environment enum | `dev | test | stage | prod` only |
| Chart version | SemVer format validated (e.g. `1.2.3` or `1.2.3-alpha.1`) |
| Values YAML | 64 KB size limit + shell/injection guard |
| Uninstall guard | `confirm_uninstall=true` required |
| Rollback guard | Revision must be ≥ 1 (no negative revision rollback) |
| Session consent | `consent_given=true` required for `session_join` (DPDP 2023) |
| Participant limits | `max_participants: 2–20` |
| Duration limits | intro: 10–300s, rebuttal: 10–120s, conclusion: 10–180s |
| Injection guard | Shell cmd chaining (`&&`, `||`, backticks), SQL, template injection, path traversal |
| Logs to stderr | stdout clean for MCP JSON-RPC protocol |

---

## Build & Run

```bash
JACKSON_CP="/usr/share/java/jackson-databind.jar:/usr/share/java/jackson-core.jar:/usr/share/java/jackson-annotations.jar"
find src/main/java -name "*.java" > /tmp/sources.txt
javac -source 17 -target 17 -cp "$JACKSON_CP" -d target/classes @/tmp/sources.txt
java -jar target/mcp-helm-service-1.0.0.jar
```

---

## Run Tests

```bash
java -cp target/mcp-helm-service-1.0.0.jar com.ecoskiller.mcp.helm.TestAgents
java -cp target/mcp-helm-service-1.0.0.jar com.ecoskiller.mcp.helm.TestAgents --verbose
```

---

## Connect to Claude Desktop

```json
{
  "mcpServers": {
    "mcp-helm-service": {
      "command": "java",
      "args": ["-jar", "/path/to/mcp-helm-service-1.0.0.jar"]
    }
  }
}
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)  
- Format: **JSON-RPC 2.0**  
- MCP Version: **2024-11-05**
