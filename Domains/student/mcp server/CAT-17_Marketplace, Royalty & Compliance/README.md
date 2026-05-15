# mcp-17-royalty (Java)

**Ecoskiller | CAT-17 — Marketplace, Royalty & Compliance**  
MCP Server in **Java 17** | **18 Agents** | Priority: HIGH

---

## Agents (18)

| # | Tool Name | Agent |
|---|-----------|-------|
| 1  | `tax_compliance`           | TAX_COMPLIANCE_AGENT_COMPLETE |
| 2  | `royalty_system_unified`   | ROYALTY_SYSTEM_UNIFIED |
| 3  | `royalty_calculation`      | ROYALTY_CALCULATION_AGENT_SEALED |
| 4  | `revenue_ingestion`        | REVENUE_INGESTION_AGENT_SEALED |
| 5  | `royalty_wallet`           | ROYALTY_WALLET_AGENT_v1.0.0 |
| 6  | `royalty_escrow`           | ROYALTY_ESCROW_AGENT |
| 7  | `royalty_distribution`     | ROYALTY_DISTRIBUTION_AGENT |
| 8  | `license_generation`       | LICENSE_GENERATION_AGENT_SEALED |
| 9  | `idea_visibility_control`  | IDEA_VISIBILITY_CONTROL_AGENT_IVCA-v1.0.0 |
| 10 | `idea_evaluation`          | IDEA_EVALUATION_AGENT_SPEC |
| 11 | `business_matching`        | BUSINESS_MATCHING_AGENT_SEALED |
| 12 | `market_demand_prediction` | MARKET_DEMAND_PREDICTION_AGENT_SPEC |
| 13 | `competition_engine`       | COMPETITION_ENGINE_AGENT_COMPLETE |
| 14 | `data_retention_policy`    | DATA_RETENTION_POLICY_AGENT |
| 15 | `audit_verification`       | AUDIT_VERIFICATION_AGENT |
| 16 | `contract_lifecycle`       | CONTRACT_LIFECYCLE_AGENT_v1.0.0 |
| 17 | `school_auto_creation`     | SCHOOL_AUTO_CREATION_AGENT |
| 18 | `parent_dashboard`         | PARENT_DASHBOARD_AGENT_SPEC |

---

## Requirements

- Java 17+
- Maven 3.9+ (for building from source)

---

## Build

```bash
# Clone / navigate to project root
cd mcp-17-royalty-java

# Build fat JAR (includes Jackson, no external classpath needed)
mvn clean package

# Output: target/mcp-17-royalty-1.0.0-jar-with-dependencies.jar
```

---

## Run the server

```bash
java -jar target/mcp-17-royalty-1.0.0-jar-with-dependencies.jar
```

The server communicates via **stdin/stdout** using **JSON-RPC 2.0** (MCP protocol).  
No ports are opened. No network configuration required.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json` (macOS)  
or `%APPDATA%\Claude\claude_desktop_config.json` (Windows):

```json
{
  "mcpServers": {
    "mcp-17-royalty": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-17-royalty-1.0.0-jar-with-dependencies.jar"]
    }
  }
}
```

---

## Run tests

```bash
mvn test
```

The test suite (`Mcp17AgentTest.java`) covers:
- Registry: all 18 tools registered
- Schema: all tools have valid `type: object` JSON Schemas
- Execute: 30+ business-logic assertions across all agents
- JSON-RPC routing: unknown tools return null

---

## Docker

```bash
# Build image
docker build -t ecoskiller/mcp-17-royalty:1.0.0 .

# Run (pipe stdio for MCP)
docker run -i ecoskiller/mcp-17-royalty:1.0.0
```

---

## Project Structure

```
mcp-17-royalty-java/
├── pom.xml                                    ← Maven build (Java 17, Jackson 2.17, fat JAR)
├── Dockerfile                                 ← Multi-stage build (maven:17 → jre-alpine)
├── claude_desktop_config.json                 ← Claude Desktop config snippet
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp17/
    │   ├── McpServer.java                     ← Main entrypoint — stdio JSON-RPC loop
    │   ├── ToolDefinition.java                ← Abstract base: schema helpers, response builders
    │   ├── AgentRegistry.java                 ← Registers all 18 agents
    │   └── agents/
    │       ├── TaxComplianceAgent.java
    │       ├── RoyaltySystemUnifiedAgent.java
    │       ├── RoyaltyCalculationAgent.java
    │       ├── RevenueIngestionAgent.java
    │       ├── RoyaltyWalletAgent.java
    │       ├── RoyaltyEscrowAgent.java
    │       ├── RoyaltyDistributionAgent.java
    │       ├── LicenseGenerationAgent.java
    │       ├── IdeaVisibilityControlAgent.java
    │       ├── IdeaEvaluationAgent.java
    │       ├── BusinessMatchingAgent.java
    │       ├── MarketDemandPredictionAgent.java
    │       ├── CompetitionEngineAgent.java
    │       ├── DataRetentionPolicyAgent.java
    │       ├── AuditVerificationAgent.java
    │       ├── ContractLifecycleAgent.java
    │       ├── SchoolAutoCreationAgent.java
    │       └── ParentDashboardAgent.java
    └── test/java/com/ecoskiller/mcp17/
        └── Mcp17AgentTest.java                ← JUnit 5 — 30+ test cases
```

---

## Protocol

| Property | Value |
|----------|-------|
| Transport | **stdio** (stdin/stdout) |
| Format | **JSON-RPC 2.0** |
| MCP Version | **2024-11-05** |
| Methods | `initialize`, `tools/list`, `tools/call`, `ping` |

---

## Adding a new agent

1. Create `src/main/java/com/ecoskiller/mcp17/agents/MyNewAgent.java` extending `ToolDefinition`
2. Add `new MyNewAgent()` to `AgentRegistry.java`
3. Add a test case to `Mcp17AgentTest.java`
4. `mvn clean package`
