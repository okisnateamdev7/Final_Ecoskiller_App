# mcp-02-governance

**Ecoskiller | CAT-2 — Governance & Compliance**  
MCP Server in Java | 4 Agents | Priority: HIGH

---

## Agents (4)

| # | Tool Name | Agent |
|---|-----------|-------|
| 1 | `backup_dr` | BACKUP_DR_AGENT_ANTIGRAVITY_SEALED |
| 2 | `data_governance` | DATA_GOVERNANCE_AGENT_ANTIGRAVITY_SEALED |
| 3 | `devsecops` | DEVSECOPS_AGENT_ANTIGRAVITY_SEALED |
| 4 | `legal_policy` | LEGAL_POLICY_AGENT_ANTIGRAVITY_SEALED |

---

## Requirements

- Java 11+  
- Maven 3.6+ (for build only — zero runtime dependencies)

---

## Build

```bash
mvn clean package
# produces: target/mcp-02-governance.jar
```

---

## Run the server

```bash
java -jar target/mcp-02-governance.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-02-governance": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-02-governance/target/mcp-02-governance.jar"]
    }
  }
}
```

---

## Run tests

```bash
bash test_agents.sh           # pass/fail summary  (11 tests)
bash test_agents.sh --verbose # with full JSON output
```

---

## File Structure

```
mcp-02-governance/
├── pom.xml                          ← Maven build (fat JAR, Java 11+)
├── test_agents.sh                   ← Bash test runner (11 tests)
├── README.md
└── src/main/java/ecoskiller/
    └── McpServer.java               ← Main MCP server (all 4 agents)
```

---

## Agent Capabilities

### 1. `backup_dr` — BACKUP_DR_AGENT_ANTIGRAVITY_SEALED
Manages disaster recovery plans, backup schedules, RTO/RPO policies,
and restoration testing for Ecoskiller infrastructure.

**Parameters:** `action`, `target_system`, `backup_type`, `retention_days`

**Sample call:**
```json
{
  "jsonrpc": "2.0", "id": 1,
  "method": "tools/call",
  "params": {
    "name": "backup_dr",
    "arguments": {
      "action": "trigger_backup",
      "target_system": "postgres-prod",
      "backup_type": "full",
      "retention_days": "30"
    }
  }
}
```

---

### 2. `data_governance` — DATA_GOVERNANCE_AGENT_ANTIGRAVITY_SEALED
Enforces data classification, lineage tracking, ownership assignment,
and quality controls across all Ecoskiller data assets.

**Parameters:** `action`, `dataset_id`, `classification_level`, `owner_id`

---

### 3. `devsecops` — DEVSECOPS_AGENT_ANTIGRAVITY_SEALED
Integrates security into CI/CD pipelines, performs SAST/DAST scans,
manages vulnerability remediation, and enforces security gates.

**Parameters:** `action`, `pipeline_id`, `scan_type`, `severity_threshold`

---

### 4. `legal_policy` — LEGAL_POLICY_AGENT_ANTIGRAVITY_SEALED
Handles legal policy creation, regulatory compliance tracking (GDPR,
COPPA, local laws), consent management, and policy versioning.

**Parameters:** `action`, `policy_type`, `jurisdiction`, `effective_date`

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Notes

- **Zero external dependencies** — pure Java standard library only.
- The server reads one JSON-RPC message per line from stdin and writes one JSON line per response to stdout.
- Errors are logged to stderr so they never pollute the JSON-RPC stream.
