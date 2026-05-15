# mcp-18-governance

**Ecoskiller | CAT-18 — Governance & Core Control**
MCP Server in Java | 2 Agents | Priority: HIGH

---

## Agents

| # | Tool Name                    | Agent                                      |
|---|------------------------------|--------------------------------------------|
| 1 | `api_rate_limit_enforcement` | API_RATE_LIMIT_ENFORCEMENT_AGENT_COMPLETE  |
| 2 | `decision_traceability`      | DECISION_TRACEABILITY_AGENT_COMPLETE       |

---

## Requirements

- Java 17+
- Maven 3.8+

---

## Build

```bash
mvn clean package -q
```

Produces: `target/mcp-18-governance.jar`

---

## Run the server

```bash
java -jar target/mcp-18-governance.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Run tests

```bash
mvn test
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-18-governance": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-18-governance/target/mcp-18-governance.jar"]
    }
  }
}
```

---

## Tool Reference

### 1. `api_rate_limit_enforcement`
Enforces per-client, per-endpoint, and global API rate limits.

| Parameter        | Type    | Required | Description                                                              |
|------------------|---------|----------|--------------------------------------------------------------------------|
| `client_id`      | string  | ✅       | Unique client / tenant identifier                                        |
| `endpoint`       | string  |          | API endpoint being accessed                                              |
| `action`         | string  |          | `check` \| `increment` \| `reset` \| `get_quota` \| `list_violations`   |
| `tier`           | string  |          | `free` (60/min) \| `pro` (300/min) \| `enterprise` (1000/min)           |
| `window_seconds` | integer |          | Rolling window in seconds (default: 60)                                  |
| `max_requests`   | integer |          | Override max requests (0 = use tier default)                             |

**Example:**
```json
{
  "name": "api_rate_limit_enforcement",
  "arguments": {
    "client_id": "tenant-001",
    "endpoint": "/api/v1/jobs/search",
    "action": "check",
    "tier": "pro"
  }
}
```

---

### 2. `decision_traceability`
Records and retrieves immutable decision audit trails.

| Parameter       | Type    | Required | Description                                                                    |
|-----------------|---------|----------|--------------------------------------------------------------------------------|
| `action`        | string  | ✅       | `record` \| `query` \| `get_chain` \| `export` \| `verify_integrity`          |
| `decision_id`   | string  |          | Decision ID (auto-generated for record)                                        |
| `actor_id`      | string  |          | Agent / user / system that made the decision                                   |
| `actor_type`    | string  |          | `agent` \| `user` \| `system` \| `automated_rule`                             |
| `decision_type` | string  |          | `hiring` \| `scoring` \| `rate_limit` \| `fraud_flag` \| `policy_change` etc. |
| `rationale`     | string  |          | Human-readable justification                                                   |
| `inputs_json`   | string  |          | JSON string of inputs                                                          |
| `output_json`   | string  |          | JSON string of decision output                                                 |
| `reference_id`  | string  |          | External reference (job ID, student ID, etc.)                                  |
| `query_filter`  | string  |          | JSON filter for query / export                                                 |
| `limit`         | integer |          | Max records for query (default: 50)                                            |

**Example — record:**
```json
{
  "name": "decision_traceability",
  "arguments": {
    "action": "record",
    "actor_id": "scoring-agent-7",
    "actor_type": "agent",
    "decision_type": "scoring",
    "rationale": "Candidate scored 82/100 on skill assessment.",
    "inputs_json": "{\"candidate_id\":\"C-101\"}",
    "output_json": "{\"score\":82,\"grade\":\"B+\"}",
    "reference_id": "C-101"
  }
}
```

---

## Protocol

- Transport:   **stdio** (stdin/stdout)
- Format:      **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods:     `initialize`, `tools/list`, `tools/call`, `ping`

---

## File Structure

```
mcp-18-governance/
├── pom.xml
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp18/
    │   └── McpServer.java          ← Main MCP server (both agents)
    └── test/java/com/ecoskiller/mcp18/
        └── McpServerTest.java      ← JUnit 5 tests (9 test cases)
```
