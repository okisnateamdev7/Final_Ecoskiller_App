# mcp-11-championship-parent-ai

**Ecoskiller | CAT-11 — Championship Advanced + Parent Predictive AI**  
MCP Server · Java · 20 Agents · **Antigravity Architecture**

---

## Agents (20)

| # | Tool Name | Agent |
|---|-----------|-------|
| 1  | `regional_distribution_engine`       | REGIONAL_DISTRIBUTION_ENGINE_ANTIGRAVITY |
| 2  | `parent_llm_insight_narrative`        | PARENT_LLM_INSIGHT_NARRATIVE_GENERATOR_ANTIGRAVITY |
| 3  | `parent_llm_career_guidance`          | PARENT_LLM_CAREER_GUIDANCE_GENERATOR_ANTIGRAVITY |
| 4  | `learning_behavior_drift`             | LEARNING_BEHAVIOR_DRIFT_MODEL_ANTIGRAVITY |
| 5  | `leaderboard_stability_model`         | LEADERBOARD_STABILITY_MODEL_PREDICTIVE_AI_ANTIGRAVITY |
| 6  | `income_prediction_model`             | INCOME_PREDICTION_MODEL_ANTIGRAVITY |
| 7  | `career_probability_model`            | CAREER_PROBABILITY_MODEL_ANTIGRAVITY |
| 8  | `weakness_impact_model`               | ANTIGRAVITY_WEAKNESS_IMPACT_MODEL_v1 |
| 9  | `spectator_analytics_model`           | ANTIGRAVITY_Spectator_Analytics_Model |
| 10 | `score_normalization_engine`          | ANTIGRAVITY_Score_Normalization_Engine |
| 11 | `plateau_detection_model`             | ANTIGRAVITY_PLATEAU_DETECTION_MODEL_v1 |
| 12 | `peer_comparison_engine`              | ANTIGRAVITY_PEER_COMPARISON_ENGINE_v1 |
| 13 | `parent_llm_growth_strategy`          | ANTIGRAVITY_Parent_LLM_Growth_Strategy_Explainer |
| 14 | `learning_behavior_drift_v2`          | ANTIGRAVITY_Learning_Behavior_Drift_Model |
| 15 | `dropout_risk_model`                  | ANTIGRAVITY_DROPOUT_RISK_MODEL_v1 |
| 16 | `competition_forecast_engine`         | ANTIGRAVITY_Competition_Forecast_Engine |
| 17 | `championship_problem_generation`     | ANTIGRAVITY_Championship_LLM_AI_Problem_Generation_Agent |
| 18 | `championship_ai_judging`             | ANTIGRAVITY_Championship_LLM_AI_Judging_Agent |
| 19 | `champion_authenticity_model`         | ANTIGRAVITY_Champion_Authenticity_Model |
| 20 | `academic_intelligence_correlation`   | ANTIGRAVITY_Academic_Intelligence_Correlation_Engine |

---

## Requirements

- **Java 11+** — zero external dependencies

---

## Build

```bash
mkdir -p out
javac -d out src/main/java/com/ecoskiller/mcp/*.java
jar cfe mcp-11-championship-parent-ai.jar com.ecoskiller.mcp.McpServer -C out .
```

---

## Run

```bash
# JAR (recommended for Claude Desktop)
java -jar mcp-11-championship-parent-ai.jar

# From compiled classes
java -cp out com.ecoskiller.mcp.McpServer
```

---

## Tests

```bash
javac -d out src/main/java/com/ecoskiller/mcp/*.java
java -cp out com.ecoskiller.mcp.TestAgents
java -cp out com.ecoskiller.mcp.TestAgents --verbose
```

Expected: **23 tests — 23 passed | 0 failed**

---

## Claude Desktop Config

`~/Library/Application Support/Claude/claude_desktop_config.json`

```json
{
  "mcpServers": {
    "mcp-11-championship-parent-ai": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-11-championship-parent-ai.jar"]
    }
  }
}
```

---

## File Structure

```
mcp-11-championship-parent-ai/
├── src/main/java/com/ecoskiller/mcp/
│   ├── McpServer.java        ← All 20 agents + dispatcher
│   ├── JsonParser.java       ← Zero-dependency JSON parser
│   ├── JsonSerializer.java   ← Zero-dependency JSON serializer
│   └── TestAgents.java       ← 23-test harness
└── README.md
```

---

## Protocol

| Property      | Value                   |
|---------------|-------------------------|
| Transport     | **stdio** (stdin/stdout)|
| Format        | **JSON-RPC 2.0**        |
| MCP Version   | **2024-11-05**          |
| Architecture  | **ANTIGRAVITY**         |
| Methods       | `initialize`, `tools/list`, `tools/call`, `ping` |

---

## Key Antigravity Models

| Agent | Model Tag |
|-------|-----------|
| Score Normalization | `ANTIGRAVITY_SCORE_NORM_V3` |
| Dropout Risk | `ANTIGRAVITY_DROPOUT_V1` |
| Competition Forecast | `ANTIGRAVITY_COMP_FORECAST_V3` |
| Champion Authenticity | `ANTIGRAVITY_AUTHENTIC_V2` |
| LLM Problem Generation | `ANTIGRAVITY_PROBGEN_LLM_V4` |
| LLM AI Judging | `ANTIGRAVITY_JUDGE_LLM_V3` |
| Career Probability | `ANTIGRAVITY_CAREER_PROB_V3` |
| Income Prediction | `ANTIGRAVITY_INCOME_PRED_V2` |
| Parent Narrative | `ANTIGRAVITY_PARENT_LLM_NARR_V3` |
| Peer Comparison | `ANTIGRAVITY_PEER_COMP_V1` |

---

## Example — Score Normalization

**Request:**
```json
{
  "jsonrpc": "2.0", "id": 1,
  "method": "tools/call",
  "params": {
    "name": "score_normalization_engine",
    "arguments": {
      "student_id": "s-001",
      "raw_score": 82,
      "cohort_mean": 70,
      "cohort_std_dev": 12
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
      "text": "{\"agent\":\"ANTIGRAVITY_Score_Normalization_Engine\",\"status\":\"success\",\"architecture\":\"ANTIGRAVITY\",\"category\":\"CAT-11\",\"z_score\":1.0,\"normalized_score\":60.0,\"percentile\":84.0,\"normalization_method\":\"Z_SCORE_TO_T_SCALE\"}"
    }]
  }
}
```
