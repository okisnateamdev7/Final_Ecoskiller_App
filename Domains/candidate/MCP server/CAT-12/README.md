# mcp-12-institute-hr

**Ecoskiller | CAT-12 — Institute + HR Predictive Systems**  
MCP Server · Java · 20 Agents · **Antigravity Architecture**

---

## Agents (20)

| # | Tool Name | Agent |
|---|-----------|-------|
| 1  | `team_compatibility_gnn`           | TEAM_COMPATIBILITY_GNN_ANTIGRAVITY |
| 2  | `talent_retrieval_vector_engine`    | TALENT_RETRIEVAL_VECTOR_ENGINE_ANTIGRAVITY |
| 3  | `workforce_gap_model`               | ECOSKILLER_Workforce_Gap_Model_SEALED |
| 4  | `hiring_bias_detector`              | ECOSKILLER_Hiring_Bias_Detector_SEALED |
| 5  | `digital_twin_simulation`           | ECOSKILLER_Digital_Twin_Simulation_Engine_SEALED |
| 6  | `candidate_ranking_model`           | CANDIDATE_RANKING_MODEL_ANTIGRAVITY |
| 7  | `student_distribution_model`        | ANTIGRAVITY_STUDENT_DISTRIBUTION_MODEL |
| 8  | `salary_prediction_model`           | ANTIGRAVITY_SALARY_PREDICTION_MODEL_v1 |
| 9  | `retention_probability_model`       | ANTIGRAVITY_RETENTION_PROBABILITY_MODEL_v1 |
| 10 | `promotion_probability_model`       | Antigravity_Promotion_Probability_Model |
| 11 | `placement_probability_model`       | ANTIGRAVITY_PLACEMENT_PROBABILITY_MODEL |
| 12 | `performance_forecast_model`        | ANTIGRAVITY_PERFORMANCE_FORECAST_MODEL_v1 |
| 13 | `interview_semantic_analyzer`       | Antigravity_Interview_Semantic_Analyzer |
| 14 | `institute_revenue_forecast`        | ANTIGRAVITY_INSTITUTE_REVENUE_FORECAST_MODEL_v1.0 |
| 15 | `institute_ranking_model`           | ANTIGRAVITY_INSTITUTE_RANKING_MODEL |
| 16 | `institute_performance_predictor`   | ANTIGRAVITY_INSTITUTE_PERFORMANCE_PREDICTOR_v1.0 |
| 17 | `curriculum_gap_detector`           | ANTIGRAVITY_CURRICULUM_GAP_DETECTOR_v1.0 |
| 18 | `class_intelligence_aggregator`     | ANTIGRAVITY_CLASS_INTELLIGENCE_AGGREGATOR |
| 19 | `candidate_summary_generator`       | Antigravity_Candidate_Summary_Generator |
| 20 | `benchmark_comparison_engine`       | ANTIGRAVITY_BENCHMARK_COMPARISON_ENGINE_v1.0 |

---

## Requirements

- **Java 11+** — zero external dependencies

---

## Build

```bash
mkdir -p out
javac -d out src/main/java/com/ecoskiller/mcp/*.java
jar cfe mcp-12-institute-hr.jar com.ecoskiller.mcp.McpServer -C out .
```

---

## Run

```bash
# JAR — recommended for Claude Desktop
java -jar mcp-12-institute-hr.jar

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
    "mcp-12-institute-hr": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-12-institute-hr.jar"]
    }
  }
}
```

---

## File Structure

```
mcp-12-institute-hr/
├── src/main/java/com/ecoskiller/mcp/
│   ├── McpServer.java        ← All 20 agents + JSON-RPC dispatcher
│   ├── JsonParser.java       ← Zero-dependency JSON parser
│   ├── JsonSerializer.java   ← Zero-dependency JSON serializer
│   └── TestAgents.java       ← 23-test harness
└── README.md
```

---

## Protocol

| Property      | Value                    |
|---------------|--------------------------|
| Transport     | **stdio** (stdin/stdout) |
| Format        | **JSON-RPC 2.0**         |
| MCP Version   | **2024-11-05**           |
| Architecture  | **ANTIGRAVITY**          |
| Methods       | `initialize`, `tools/list`, `tools/call`, `ping` |

---

## Key Antigravity Models

| Agent | Model Tag |
|-------|-----------|
| Team GNN | `ANTIGRAVITY_TEAM_GNN_V3` |
| Talent Vector Search | `ANTIGRAVITY_TALENT_EMBED_V2` (768-dim) |
| Hiring Bias Detector | `ANTIGRAVITY_BIAS_DETECT_V2` |
| Digital Twin | `ANTIGRAVITY_DIGITAL_TWIN_V3` |
| Candidate Ranking | `ANTIGRAVITY_CAND_RANK_V3` |
| Salary Prediction | `ANTIGRAVITY_SALARY_V1` |
| Retention Model | `ANTIGRAVITY_RETENTION_V1` |
| Promotion Model | `ANTIGRAVITY_PROMO_PROB_V2` |
| Placement Model | `ANTIGRAVITY_PLACEMENT_V2` |
| Interview Semantics | `ANTIGRAVITY_INTERVIEW_SEM_V2` |
| Institute Revenue | `ANTIGRAVITY_INST_REV_FORE_V1` |
| Curriculum Gap | `ANTIGRAVITY_CURRICULUM_GAP_V1` |
| Benchmark Engine | `ANTIGRAVITY_BENCHMARK_COMP_V1` |

---

## Example — Curriculum Gap Detector

**Request:**
```json
{
  "jsonrpc": "2.0", "id": 1,
  "method": "tools/call",
  "params": {
    "name": "curriculum_gap_detector",
    "arguments": {
      "institute_id": "INST-004",
      "program": "B.Tech CSE"
    }
  }
}
```

**Response (excerpt):**
```json
{
  "agent": "ANTIGRAVITY_CURRICULUM_GAP_DETECTOR_v1.0",
  "status": "success",
  "architecture": "ANTIGRAVITY",
  "category": "CAT-12",
  "critical_gap_count": 2,
  "gaps_detected": [
    { "topic": "Generative AI & LLMs", "industry_demand": 0.91, "curriculum_coverage": 0.12, "priority": "CRITICAL" },
    { "topic": "MLOps & Model Deployment", "industry_demand": 0.77, "curriculum_coverage": 0.18, "priority": "CRITICAL" }
  ],
  "curriculum_freshness_score": 0.54,
  "industry_alignment_pct": 61.0
}
```
