# mcp-19-platform-stability

**Ecoskiller | CAT-19 — Platform Stability & Risk**  
MCP Server in **Java** | 6 Agents | Priority: HIGH

---

## Agents (6)

| # | Tool Name | Agent |
|---|-----------|-------|
| 1 | `agent_health_watchdog` | AGENT_HEALTH_WATCHDOG_AGENT |
| 2 | `emergency_platform_lockdown` | EMERGENCY_PLATFORM_LOCKDOWN_AGENT |
| 3 | `insider_threat_monitor` | INSIDER_THREAT_MONITOR_AGENT |
| 4 | `institute_timetable_optimization` | INSTITUTE_TIMETABLE_OPTIMIZATION_AGENT |
| 5 | `integration_health_monitor` | INTEGRATION_HEALTH_MONITOR_AGENT |
| 6 | `model_explainability_diff` | MODEL_EXPLAINABILITY_DIFF_AGENT |

---

## Requirements

- **Java 17+**
- **Maven 3.8+**

---

## Build

```bash
mvn clean package -DskipTests
```

This produces a fat JAR at:
```
target/mcp-19-platform-stability-1.0.0.jar
```

---

## Run the server

```bash
java -jar target/mcp-19-platform-stability-1.0.0.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Run tests

```bash
mvn test
```

12 JUnit 5 tests — 2 per agent.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-19-platform-stability": {
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/to/mcp-19-platform-stability/target/mcp-19-platform-stability-1.0.0.jar"
      ]
    }
  }
}
```

---

## File Structure

```
mcp-19-platform-stability/
├── pom.xml                                        ← Maven build (Java 17, Jackson, JUnit 5)
├── claude_desktop_config.json                     ← Claude Desktop config snippet
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp19/
    │   ├── McpServer.java                         ← JSON-RPC 2.0 dispatcher (stdio)
    │   ├── AgentHandler.java                      ← Agent interface
    │   ├── AgentHealthWatchdogAgent.java           ← Agent 1
    │   ├── EmergencyPlatformLockdownAgent.java    ← Agent 2
    │   ├── InsiderThreatMonitorAgent.java          ← Agent 3
    │   ├── InstituteTimetableOptimizationAgent.java ← Agent 4
    │   ├── IntegrationHealthMonitorAgent.java      ← Agent 5
    │   └── ModelExplainabilityDiffAgent.java       ← Agent 6
    └── test/java/com/ecoskiller/mcp19/
        └── AgentTests.java                        ← 12 JUnit 5 tests (2 per agent)
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| `jackson-databind` | 2.17.1 | JSON parsing & serialisation |
| `junit-jupiter` | 5.10.2 | Unit tests |
| `maven-shade-plugin` | 3.5.3 | Fat JAR packaging |
