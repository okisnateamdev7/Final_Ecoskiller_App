# Ecoskiller Antigravity MCP Server
## `mcp-01-platform` | CAT-01 — Core Platform & Meta Architecture

---

### Overview

Java MCP Server (JSON-RPC 2.0 over **stdio**) hosting all **11 Antigravity agents** for the Ecoskiller platform.

| Property | Value |
|---|---|
| MCP Server ID | `mcp-01-platform` |
| Namespace | `core` |
| Priority | MEDIUM (Sprint 1 bootstrap) |
| Agents | 11 |
| Java | 21+ |
| Transport | stdio (JSON-RPC 2.0) |

---

### Agents Included

| # | Agent | Tools |
|---|---|---|
| 1 | `ANTIGRAVITY_CONFIGURATION_AGENT` | `get_config`, `set_config`, `list_feature_flags`, `toggle_feature_flag` |
| 2 | `ANTIGRAVITY_DEPENDENCY_RISK_AGENT` | `scan_dependencies`, `check_circular_deps`, `get_dependency_graph` |
| 3 | `ANTIGRAVITY_OBSERVABILITY_AGENT` | `health_check`, `get_metrics`, `get_traces` |
| 4 | `ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT` | `route_request`, `get_routing_table`, `set_rate_limit`, `get_queue_status` |
| 5 | `ANTIGRAVITY_PLATFORM_EVALUATION_AGENT` | `evaluate_platform`, `compare_sprints` |
| 6 | `ANTIGRAVITY_TECH_DEBT_AGENT` | `scan_tech_debt`, `get_debt_report` |
| 7 | `ANTIGRAVITY_TRUTH_ENGINE_AGENT` | `validate_truth`, `get_truth_log` |
| 8 | `ECOSKILLER_ANTIGRAVITY_API_AGENT` | `list_api_versions`, `validate_api_contract` |
| 9 | `ECOSKILLER_ANTIGRAVITY_SCHEMA_COMPATIBILITY_AGENT` | `check_schema_compatibility`, `migrate_schema` |
| 10 | `ECOSKILLER_ANTIGRAVITY_SYSTEM_SETUP_AGENT` | `bootstrap_server`, `teardown_server` |
| 11 | `ECOSKILLER_ANTIGRAVITY_UI_AGENT` | `render_dashboard`, `get_ui_config` |

---

### Build & Run

```bash
# Build
mvn clean package -q

# Run (stdio MCP server)
java -jar target/mcp-01-antigravity-1.0.0.jar

# Test
mvn test
```

---

### MCP Protocol

Implements MCP spec `2024-11-05` over **stdio** transport.

```
Client → Server (stdin):   {"jsonrpc":"2.0","id":1,"method":"initialize","params":{...}}
Server → Client (stdout):  {"jsonrpc":"2.0","id":1,"result":{...}}
```

**Supported methods:**
- `initialize` — handshake
- `tools/list` — list all agent tools
- `tools/call` — invoke any agent tool
- `resources/list` — (empty for this server)
- `prompts/list` — (empty for this server)
- `ping` — health ping

---

### Example Tool Calls

```json
// Health check across all 29 MCP servers
{"jsonrpc":"2.0","id":1,"method":"tools/call",
 "params":{"name":"antigravity_health_check","arguments":{"include_agents":true}}}

// Route a request to the correct MCP server
{"jsonrpc":"2.0","id":2,"method":"tools/call",
 "params":{"name":"antigravity_route_request",
           "arguments":{"intent":"skill score calculation","priority":"HIGH"}}}

// Toggle a feature flag
{"jsonrpc":"2.0","id":3,"method":"tools/call",
 "params":{"name":"antigravity_toggle_feature_flag",
           "arguments":{"flag_name":"ENABLE_TECH_DEBT_SCANNER","enabled":"true","environment":"production"}}}
```

---

### Project Structure

```
mcp-01-antigravity/
├── pom.xml
├── mcp-client-config.json
└── src/
    ├── main/java/com/ecoskiller/antigravity/
    │   ├── server/
    │   │   ├── AntigravityMcpServer.java   ← Entry point + MCP protocol
    │   │   └── AgentRegistry.java
    │   ├── agents/
    │   │   ├── BaseAgent.java
    │   │   ├── ConfigurationAgent.java
    │   │   ├── DependencyRiskAgent.java
    │   │   ├── ObservabilityAgent.java
    │   │   ├── OrchestrationGovernorAgent.java
    │   │   └── RemainingAgents.java        ← Agents 5–11
    │   └── models/
    │       ├── McpTool.java
    │       └── AgentResponse.java
    └── test/
        └── AntigravityMcpServerTest.java   ← 15 unit tests
```

---

### Adding More Agents

1. Create a class extending `BaseAgent`
2. Implement `getName()`, `getTools()`, `execute()`
3. Register in `AntigravityMcpServer.registerAllAgents()`

---

*Ecoskiller Engineering | mcp-01-platform v1.0.0 | Java 21 | MCP 2024-11-05*
