# ecoskiller-cat26-organizer-network-management-mcp

**EcoSkiller | CAT-26 — Organizer & Network Management**
MCP Server in Java | **29 Agents | 58 Tools** | Transport: stdio | ANTIGRAVITY SEALED

---

## All 29 Agents & Tool Prefixes

| # | Agent | Prefix | Tools |
|---|-------|--------|-------|
| 1 | `IMPACT_MEASUREMENT_AGENT` | `impact__` | 2 |
| 2 | `REVENUE_TREND_ANALYTICS_AGENT` | `revenue_trend__` | 2 |
| 3 | `REVENUE_STREAM_DIVERSIFICATION_AGENT` | `rev_diversification__` | 2 |
| 4 | `NETWORK_HEALTH_MONITOR_AGENT` | `network_health__` | 2 |
| 5 | `DROPOUT_RISK_PREDICTION_AGENT` | `dropout_risk__` | 2 |
| 6 | `PHONE_MONITORING_CLOCK_AUTHORITY_AGENT` | `phone_clock__` | 2 |
| 7 | `POLICY_COMPLIANCE_AGENT` | `policy_compliance__` | 2 |
| 8 | `DATA_PRIVACY_COMPLIANCE_AGENT` | `data_privacy__` | 2 |
| 9 | `SAFETY_COMPLIANCE_MONITOR_AGENT` | `safety_compliance__` | 2 |
| 10 | `STANDARD_AUDIT_AGENT` | `std_audit__` | 2 |
| 11 | `CONTRACT_MONITORING_AGENT` | `contract_monitor__` | 2 |
| 12 | `LEGAL_DOCUMENT_MANAGEMENT_AGENT` | `legal_docs__` | 2 |
| 13 | `GOVERNMENT_SCHEME_FUNDING_AGENT` | `govt_scheme__` | 2 |
| 14 | `CSR_FUNDS_ALLOCATION_AGENT` | `csr_funds__` | 2 |
| 15 | `FRAUD_DETECTION_AGENT` | `fraud_detect__` | 2 |
| 16 | `FINANCIAL_ANOMALY_DETECTION_AGENT` | `fin_anomaly__` | 2 |
| 17 | `CRISIS_MANAGEMENT_AGENT` | `crisis__` | 2 |
| 18 | `EMERGENCY_RESPONSE_AGENT` | `emergency__` | 2 |
| 19 | `EMERGENCY_RESPONSE_POLICY_AGENT` | `erp__` | 2 |
| 20 | `FAILURE_CASE_ANALYSIS_AGENT` | `failure_analysis__` | 2 |
| 21 | `FRANCHISE_CONCENTRATION_RISK_AGENT` | `franchise_risk__` | 2 |
| 22 | `NETWORK_EXPANSION_CAP_AGENT` | `expansion_cap__` | 2 |
| 23 | `NETWORK_SHUTDOWN_PROTOCOL_AGENT` | `shutdown_protocol__` | 2 |
| 24 | `RISK_REGISTER_MANAGEMENT_AGENT` | `risk_register__` | 2 |
| 25 | `ROLE_OVERLOAD_MONITOR_AGENT` | `role_overload__` | 2 |
| 26 | `MEDIA_RELATIONS_AGENT` | `media_relations__` | 2 |
| 27 | `PUBLIC_REPUTATION_MONITOR_AGENT` | `pub_reputation__` | 2 |
| 28 | `PRICING_RECALIBRATION_AGENT` | `pricing__` | 2 |
| 29 | `SUCCESSION_AT_MASTER_LEVEL_AGENT` | `succession__` | 2 |

**TOTAL: 58 Tools**

---

## Build

```bash
mvn clean package -DskipTests
```

Output: `target/cat26-organizer-network-management-mcp-1.0.0-ANTIGRAVITY.jar`

---

## Run

```bash
java -jar target/cat26-organizer-network-management-mcp-1.0.0-ANTIGRAVITY.jar
```

Communicates via **stdin/stdout** — JSON-RPC 2.0. Logs go to **stderr**.

---

## Claude Desktop

```json
{
  "mcpServers": {
    "ecoskiller-cat26-organizer-network-management": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/cat26-...jar"]
    }
  }
}
```

---

## File Structure

```
ecoskiller-cat26-mcp/
├── pom.xml
├── claude_desktop_config.json
├── README.md
└── src/main/java/com/ecoskiller/antigravity/cat26/
    ├── Cat26McpServerApplication.java   ← Entry point
    ├── model/McpModels.java             ← JSON-RPC 2.0 models
    ├── agents/
    │   ├── AgentsGroup1.java            ← Agents 1–8  (16 tools)
    │   ├── AgentsGroup2.java            ← Agents 9–16 (16 tools)
    │   ├── AgentsGroup3.java            ← Agents 17–24 (16 tools)
    │   └── AgentsGroup4.java            ← Agents 25–29 (10 tools)
    ├── tools/Cat26ToolRegistry.java     ← Prefix router
    └── server/McpStdioServer.java       ← stdio server loop
```

---

*ECOSKILLER ANTIGRAVITY — CAT-26 — SEALED v1.0.0*
