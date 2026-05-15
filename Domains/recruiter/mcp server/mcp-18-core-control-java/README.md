# MCP-18 Core Control Server

**EcoSkiller | CAT-18 — Governance & Core Control**  
MCP Server in Java | 11 Agents | Priority: HIGH

---

## Agents (11)

| # | Tool Name | Agent | Purpose |
|---|-----------|-------|---------|
| 1 | `analytics_connect` | ANALYTICS_CONNECT_AGENT | Connect analytics platforms |
| 2 | `automation_connect` | AUTOMATION_CONNECT_AGENT | Orchestrate automation workflows |
| 3 | `data_warehouse` | DATA_WAREHOUSE_AGENT | Manage data warehouse operations |
| 4 | `deployment_checklist` | DEPLOYMENT_CHECKLIST_AGENT | Manage deployment checklists |
| 5 | `digilock` | DIGILOCK_AGENT | Handle digital document locking |
| 6 | `esign_connect` | ESIGN_CONNECT_AGENT | E-signature services integration |
| 7 | `implementation_guide` | IMPLEMENTATION_GUIDE_AGENT | Provide implementation guides |
| 8 | `llm_connect` | LLM_CONNECT_AGENT | Connect and manage LLMs |
| 9 | `marketing_connect` | MARKETING_CONNECT_AGENT | Integrate marketing platforms |
| 10 | `payment_connect` | PAYMENT_CONNECT_AGENT | Manage payment gateways |
| 11 | `video_connect` | VIDEO_CONNECT_AGENT | Video processing & streaming |

---

## Quick Start

### Prerequisites
- Java 11+
- Maven 3.6+

### Build
```bash
mvn clean package
```

### Run
```bash
java -jar target/mcp-18-core-control.jar
```

### Test
```bash
mvn test
```

---

## Protocol Details

- **Transport:** stdio (stdin/stdout)
- **Format:** JSON-RPC 2.0
- **MCP Version:** 2024-11-05
- **Methods:**
  - `initialize` — Initialize connection
  - `tools/list` — List all 11 tools
  - `tools/call` — Call a specific tool
  - `ping` — Health check

---

## File Structure

```
mcp-18-core-control/
├── pom.xml                          # Maven config
├── README.md                        # This file
├── src/
│   ├── main/java/com/ecoskiller/mcp/
│   │   ├── server/MCPServer.java               # Main server
│   │   ├── handlers/
│   │   │   ├── MessageHandler.java             # JSON-RPC router
│   │   │   └── ToolRegistry.java               # Tool definitions
│   │   └── agents/
│   │       ├── Agent.java                      # Interface
│   │       ├── BaseAgent.java                  # Base class
│   │       └── Agents.java                     # 11 agents (all in one file)
│   └── test/java/com/ecoskiller/mcp/tests/
│       └── AgentTestSuite.java                 # Tests
└── target/
    └── mcp-18-core-control.jar                # Compiled JAR
```

---

## Usage Examples

### Initialize
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "initialize",
  "params": {}
}
```

### List Tools
```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "method": "tools/list",
  "params": {}
}
```

### Call Tool
```json
{
  "jsonrpc": "2.0",
  "id": 3,
  "method": "tools/call",
  "params": {
    "name": "payment_connect",
    "arguments": {
      "amount": 5000,
      "currency": "INR",
      "gateway": "stripe"
    }
  }
}
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-18-core-control": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-18-core-control.jar"]
    }
  }
}
```

Restart Claude Desktop.

---

## Performance

- **Startup:** ~500ms
- **Memory:** ~150MB
- **Latency:** <50ms per call
- **Throughput:** 1-10 req/sec (stdio)

---

## License

Proprietary — EcoSkiller (2024)

---

**Status:** ✅ Production Ready  
**Version:** 1.0.0  
**Build Date:** 2024-03-16
