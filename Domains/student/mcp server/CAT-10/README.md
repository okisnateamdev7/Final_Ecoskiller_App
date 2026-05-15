# mcp-10-skill-competition

**Ecoskiller | CAT-10 — Skill & Competition Core**  
MCP Server · Java · 19 Agents · **Antigravity Architecture**

---

## Agents (19)

| # | Tool Name | Agent |
|---|-----------|-------|
| 1 | `skill_xp_calibration` | SKILL_XP_CALIBRATION_AGENT |
| 2 | `skill_rank_engine` | SKILL_RANK_ENGINE_AGENT |
| 3 | `skill_performance_regression` | SKILL_PERFORMANCE_REGRESSION_AGENT |
| 4 | `skill_obsolescence_predictor` | SKILL_OBSOLESCENCE_PREDICTOR_AGENT |
| 5 | `skill_market_benchmark` | SKILL_MARKET_BENCHMARK_AGENT |
| 6 | `skill_improvement_planner` | SKILL_IMPROVEMENT_PLANNER_AGENT |
| 7 | `skill_fraud_detector` | SKILL_FRAUD_DETECTOR_AGENT |
| 8 | `skill_feedback_generator` | SKILL_FEEDBACK_GENERATOR_AGENT |
| 9 | `skill_extraction_classifier` | SKILL_EXTRACTION_CLASSIFIER_AGENT |
| 10 | `skill_demand_forecast` | SKILL_DEMAND_FORECAST_AGENT |
| 11 | `skill_confidence_model` | SKILL_CONFIDENCE_MODEL_AGENT |
| 12 | `skill_challenge_generator` | SKILL_CHALLENGE_GENERATOR_AGENT |
| 13 | `competition_difficulty_calibrator` | COMPETITION_DIFFICULTY_CALIBRATOR_AGENT |
| 14 | `championship_prize_allocation` | CHAMPIONSHIP_ML_PRIZE_ALLOCATION_OPTIMIZER_AGENT |
| 15 | `championship_qualification_filter` | CHAMPIONSHIP_ML_6_QUALIFICATION_FILTER_MODEL_AGENT |
| 16 | `championship_performance_consistency` | CHAMPIONSHIP_ML_6_PERFORMANCE_CONSISTENCY_DETECTOR_AGENT |
| 17 | `championship_live_ranking` | CHAMPIONSHIP_ML_6_LIVE_RANKING_ENGINE_AGENT |
| 18 | `championship_anti_cheat` | CHAMPIONSHIP_ML_6_ANTI_CHEAT_BEHAVIORAL_MODEL |
| 19 | `skill_similarity_embedding` | 27_SKILL_SIMILARITY_EMBEDDING_AGENT |

---

## Requirements

- **Java 11+** — zero external dependencies

---

## Build

```bash
mkdir -p out
javac -d out src/main/java/com/ecoskiller/mcp/*.java
jar cfe mcp-10-skill-competition.jar com.ecoskiller.mcp.McpServer -C out .
```

---

## Run

```bash
# From JAR (recommended for Claude Desktop)
java -jar mcp-10-skill-competition.jar

# From compiled classes
java -cp out com.ecoskiller.mcp.McpServer
```

---

## Run Tests

```bash
javac -d out src/main/java/com/ecoskiller/mcp/*.java
java -cp out com.ecoskiller.mcp.TestAgents            # quick pass/fail
java -cp out com.ecoskiller.mcp.TestAgents --verbose  # full JSON output
```

---

## Claude Desktop Config

`~/Library/Application Support/Claude/claude_desktop_config.json`

```json
{
  "mcpServers": {
    "mcp-10-skill-competition": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-10-skill-competition.jar"]
    }
  }
}
```

---

## File Structure

```
mcp-10-skill-competition/
├── src/main/java/com/ecoskiller/mcp/
│   ├── McpServer.java        ← All 19 agents + JSON-RPC dispatcher
│   ├── JsonParser.java       ← Zero-dependency JSON parser
│   ├── JsonSerializer.java   ← Zero-dependency JSON serializer
│   └── TestAgents.java       ← Full test harness (22 tests)
└── README.md
```

---

## Protocol

| Property | Value |
|----------|-------|
| Transport | **stdio** (stdin/stdout) |
| Format | **JSON-RPC 2.0** |
| MCP Version | **2024-11-05** |
| Architecture | **ANTIGRAVITY** |
| Methods | `initialize`, `tools/list`, `tools/call`, `ping` |

---

## Example

**Request:**
```json
{
  "jsonrpc": "2.0", "id": 1,
  "method": "tools/call",
  "params": {
    "name": "championship_live_ranking",
    "arguments": {
      "championship_id": "CHAMP-2025",
      "user_id": "student-42",
      "live_score": 91.5
    }
  }
}
```

**Response:**
```json
{
  "jsonrpc": "2.0", "id": 1,
  "result": {
    "content": [{
      "type": "text",
      "text": "{\"agent\":\"CHAMPIONSHIP_ML_6_LIVE_RANKING_ENGINE_AGENT\",\"status\":\"success\",\"architecture\":\"ANTIGRAVITY\",\"category\":\"CAT-10\",\"current_rank\":4,\"live_percentile\":96.96,\"engine\":\"ANTIGRAVITY_LIVE_RANK_V4\",...}"
    }]
  }
}
```

---

## Key Antigravity Models

| Agent | Model Tag |
|-------|-----------|
| XP Calibration | `ANTIGRAVITY_V2` |
| Rank Engine | `ANTIGRAVITY_RANK_V3` |
| Performance Regression | `ANTIGRAVITY_REG_V2` |
| Demand Forecast | `ANTIGRAVITY_DEMAND_LSTM_V2` |
| Anti-Cheat | `ANTIGRAVITY_ANTICHEAT_V5` |
| Live Ranking | `ANTIGRAVITY_LIVE_RANK_V4` |
| Similarity Embedding | `ANTIGRAVITY_EMBED_V2` (768-dim) |
