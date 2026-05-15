# ecoskiller-cat13-enterprise-optimization-trust-mcp

**EcoSkiller | CAT-13 — Enterprise Optimization + Trust Infrastructure**
MCP Server in Java | 18 Agents | 38 Tools | Transport: stdio | Priority: HIGH

---

## Agents & Tools (18 Agents | 38 Tools)

| # | Agent | Tool Prefix | Tools |
|---|-------|------------|-------|
| 1 | `WORK_RELIABILITY_MODEL_87_ANTIGRAVITY_v1.0_SEALED` | `work_reliability__` | 4 |
| 2 | `SYNC_CONFLICT_RESOLVER_ANTIGRAVITY` | `sync_conflict__` | 2 |
| 3 | `STRUCTURED_SKILL_EXTRACTION_MODEL_ANTIGRAVITY_v1.0_SEALED` | `skill_extraction__` | 2 |
| 4 | `SECTION_83_CORPORATE_BENCHMARK_MODEL_LOCKED` | `section83__` | 2 |
| 5 | `SECTION_82_HIRING_ROI_MODEL_LOCKED` | `section82__` | 2 |
| 6 | `SECTION_80_PRODUCTIVITY_INDEX_MODEL_LOCKED` | `section80__` | 2 |
| 7 | `REPUTATION_SCORE_ENGINE_v1.0_LOCKED` | `reputation__` | 3 |
| 8 | `MIGRATION_VALIDATION_ENGINE_ANTIGRAVITY` | `migration__` | 2 |
| 9 | `FRAUD_PATTERN_DETECTION_MODEL_v1.0_LOCKED` | `fraud_pattern__` | 2 |
| 10 | `FAKE_PROFILE_DETECTION_MODEL_ANTIGRAVITY` | `fake_profile__` | 2 |
| 11 | `EXCEL_PATTERN_DETECTION_MODEL_ANTIGRAVITY` | `excel_pattern__` | 1 |
| 12 | `DATA_NORMALIZATION_ENGINE_ANTIGRAVITY_v1.0_SEALED` | `data_norm__` | 2 |
| 13 | `CERTIFICATE_AUTHENTICITY_CLASSIFIER_ANTIGRAVITY` | `cert_auth__` | 2 |
| 14 | `AUTOMATED_SHORTLISTING_ENGINE_LOCKED` | `shortlisting__` | 2 |
| 15 | `ATTRITION_RISK_MODEL_LOCKED` | `attrition__` | 2 |
| 16 | `ANTIGRAVITY_Performance_Metric_Mapper` | `perf_metric__` | 2 |
| 17 | `ANTIGRAVITY_90_Data_Cleaning_Classifier` | `data_clean__` | 2 |
| 18 | `ANTIGRAVITY_89_AI_Field_Mapping_Model` | `ai_field_mapping__` | 2 |

---

## Requirements

- Java 17+
- Maven 3.8+

---

## Build

```bash
cd ecoskiller-cat13-mcp
mvn clean package -DskipTests
```

Fat JAR output: `target/cat13-enterprise-optimization-trust-mcp-1.0.0-ANTIGRAVITY.jar`

---

## Run

```bash
java -jar target/cat13-enterprise-optimization-trust-mcp-1.0.0-ANTIGRAVITY.jar
```

Communicates via **stdin/stdout** — JSON-RPC 2.0 (MCP protocol).  
Logs go to **stderr** (safe for stdio transport).

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "ecoskiller-cat13-enterprise-optimization-trust": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/cat13-enterprise-optimization-trust-mcp-1.0.0-ANTIGRAVITY.jar"]
    }
  }
}
```

---

## File Structure

```
ecoskiller-cat13-mcp/
├── pom.xml
├── claude_desktop_config.json
├── README.md
└── src/main/java/com/ecoskiller/antigravity/cat13/
    ├── Cat13McpServerApplication.java       ← Entry point
    ├── model/
    │   └── McpModels.java                   ← JSON-RPC 2.0 models
    ├── agents/
    │   ├── WorkReliabilityModel87Agent.java ← Agent 1 (4 tools)
    │   ├── AgentsGroup1.java                ← Agents 2–6 (10 tools)
    │   ├── AgentsGroup2.java                ← Agents 7–12 (12 tools)
    │   └── AgentsGroup3.java                ← Agents 13–18 (12 tools)
    ├── tools/
    │   └── Cat13ToolRegistry.java           ← Dispatch router
    └── server/
        └── McpStdioServer.java              ← stdio MCP server loop
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

*ECOSKILLER ANTIGRAVITY — CAT-13 — SEALED v1.0.0*
