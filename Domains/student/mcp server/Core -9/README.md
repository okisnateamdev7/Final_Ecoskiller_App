# mcp-core-intelligence

**Ecoskiller | CAT-9 — Core Intelligence Architect**  
MCP Server in Java | 19 Agents | Priority: HIGH

---

## Agents (19)

| # | Tool Name | Agent |
|---|-----------|-------|
| 1 | `spatial_pattern_model` | SPATIAL_PATTERN_MODEL_AGENT |
| 2 | `reflective_depth_analyzer` | REFLECTIVE_DEPTH_ANALYZER_AGENT |
| 3 | `population_percentile_engine` | POPULATION_PERCENTILE_ENGINE_AGENT |
| 4 | `open_response_evaluation` | OPEN_RESPONSE_EVALUATION_AGENT |
| 5 | `naturalistic_classification_model` | NATURALISTIC_CLASSIFICATION_MODEL_AGENT |
| 6 | `musical_frequency_model` | MUSICAL_FREQUENCY_MODEL_AGENT |
| 7 | `logical_scoring_model` | LOGICAL_SCORING_MODEL_AGENT |
| 8 | `linguistic_structural_analyzer` | LINGUISTIC_STRUCTURAL_ANALYZER_AGENT |
| 9 | `learning_velocity_model` | LEARNING_VELOCITY_MODEL_AGENT |
| 10 | `kinesthetic_motion_model` | KINESTHETIC_MOTION_MODEL_AGENT |
| 11 | `intrapersonal_behavioral_model` | INTRAPERSONAL_BEHAVIORAL_MODEL_AGENT |
| 12 | `interpersonal_graph_model` | INTERPERSONAL_GRAPH_MODEL_AGENT |
| 13 | `intelligence_report_generator` | INTELLIGENCE_REPORT_GENERATOR_AGENT |
| 14 | `intelligence_growth_timeseries` | INTELLIGENCE_GROWTH_TIME_SERIES_AGENT |
| 15 | `entrepreneurial_risk_model` | ENTREPRENEURIAL_RISK_MODEL_AGENT |
| 16 | `debate_quality_analyzer` | DEBATE_QUALITY_ANALYZER_AGENT |
| 17 | `creativity_divergence_agent` | CREATIVITY_DIVERGENCE_AGENT_AGENT |
| 18 | `cognitive_stability_index` | COGNITIVE_STABILITY_INDEX_AGENT |
| 19 | `ai_collaboration_efficiency_model` | AI_COLLABORATION_EFFICIENCY_MODEL_AGENT |

---

## Requirements

- **Java 11+** (no external libraries — zero dependencies)

---

## Build

```bash
mkdir -p out
javac -d out src/main/java/com/ecoskiller/mcp/*.java
jar cfe mcp-core-intelligence.jar com.ecoskiller.mcp.McpServer -C out .
```

---

## Run the server

```bash
java -jar mcp-core-intelligence.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Run tests

```bash
# Build test
javac -d out src/main/java/com/ecoskiller/mcp/*.java
java -cp out com.ecoskiller.mcp.TestAgents           # quick pass/fail
java -cp out com.ecoskiller.mcp.TestAgents --verbose # with full JSON output
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-core-intelligence": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-core-intelligence.jar"]
    }
  }
}
```

---

## File Structure

```
mcp-core-intelligence/
├── src/main/java/com/ecoskiller/mcp/
│   ├── McpServer.java       ← Main MCP server (all 19 agents)
│   ├── JsonParser.java      ← Zero-dependency JSON parser
│   ├── JsonSerializer.java  ← Zero-dependency JSON serializer
│   └── TestAgents.java      ← Test all 19 agents locally
└── README.md
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Example Request / Response

**Request:**
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "tools/call",
  "params": {
    "name": "population_percentile_engine",
    "arguments": {
      "user_id": "student-42",
      "raw_score": 85,
      "domain": "logical"
    }
  }
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    "content": [{
      "type": "text",
      "text": "{\"agent\":\"POPULATION_PERCENTILE_ENGINE_AGENT\",\"status\":\"computed\",\"percentile\":93.5,\"cohort_label\":\"TOP_10_PERCENT\",...}"
    }]
  }
}
```
