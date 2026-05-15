# mcp-16-dojo

**EcoSkiller | CAT-16 — Dojo, Growth & School Ops**  
MCP Server in **Java 17** | 1 Agent | Priority: HIGH

---

## Agent

| # | Tool Name | Agent |
|---|-----------|-------|
| 1 | `daily_mission` | `DAILY_MISSION_AGENT_ANTIGRAVITY` |

---

## Requirements

- Java 17+
- Maven 3.8+

---

## Build

```bash
# From project root
mvn clean package -q

# Output: target/mcp-16-dojo.jar  (fat JAR, all deps included)
```

---

## Run the server

```bash
java -jar target/mcp-16-dojo.jar
```

The server communicates via **stdin/stdout** using **JSON-RPC 2.0** (MCP protocol 2024-11-05).  
All logs go to **stderr** so they never corrupt the MCP stream.

---

## Run tests

```bash
mvn test
```

Expected output: **13 tests — all pass**.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-16-dojo": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-16-dojo/target/mcp-16-dojo.jar"]
    }
  }
}
```

---

## Tool: `daily_mission`

### Required parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `action` | string | One of the 6 actions below |
| `user_id` | string | Unique learner / participant ID |

### Optional parameters
| Parameter | Type | Default | Values |
|-----------|------|---------|--------|
| `difficulty` | string | `medium` | `easy` \| `medium` \| `hard` \| `elite` |
| `subject` | string | `general` | `math`, `science`, `language`, `coding`, `art`, `music`, `sports`, `critical_thinking`, `entrepreneurship`, `general` |
| `tier` | string | `sprout` | `seed` \| `sprout` \| `bloom` \| `master` |
| `date` | string | today | ISO date `yyyy-MM-dd` |
| `mission_id` | string | — | Required for `complete_mission` and `get_mission_status` |

### Actions

| Action | Description |
|--------|-------------|
| `generate_mission` | Create a daily mission for the user |
| `get_mission_status` | Get status of a specific mission |
| `complete_mission` | Mark a mission complete and award XP |
| `get_streak` | Get streak info for a user |
| `list_missions` | List all missions for a given day |
| `reset_daily` | Reset daily missions (admin / test) |

### XP Rewards

| Difficulty | Base XP | easy | medium | hard | elite |
|------------|---------|------|--------|------|-------|
| Base XP | — | 100 | 200 | 350 | 500 |

**Tier multipliers:**  
`seed × 1.0` · `sprout × 1.1` · `bloom × 1.25` · `master × 1.5`

---

## File Structure

```
mcp-16-dojo/
├── pom.xml
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp16/
    │   ├── McpServer.java           ← JSON-RPC 2.0 transport + dispatcher
    │   └── DailyMissionAgent.java   ← DAILY_MISSION_AGENT_ANTIGRAVITY
    └── test/java/com/ecoskiller/mcp16/
        └── DailyMissionAgentTest.java  ← 13 unit tests
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`
