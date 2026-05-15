# mcp-08-economics
### Ecoskiller · CAT-08 — Economics, Reporting & Strategic Control · MCP Server (Java)

---

## Overview

Java MCP server for **CAT-08: Economics, Reporting & Strategic Control**.  
Exposes **17 agent tools** over MCP JSON-RPC 2.0. Enables LLM clients (Claude, GPT-4, etc.)
to invoke financial intelligence, strategic simulations, and reporting engines with a single tool call.

| | |
|---|---|
| **MCP Protocol** | 2024-11-05 |
| **Port** | 8708 |
| **Java** | 17 |
| **Framework** | Spring Boot 3.2 |
| **Agents** | 17 |

---

## 17 Agents (Tools)

| # | Tool Name | Source Agent |
|---|-----------|-------------|
| 1 | `antitrust_check` | ANTITRUST_AGENT |
| 2 | `board_reporting` | BOARD_REPORTING |
| 3 | `customer_success` | CUSTOMER_SUCCESS |
| 4 | `digital_twin_simulate` | ECOSKILLER_Digital_Twin_Simulation_Engine_SEALED |
| 5 | `hiring_bias_detect` | ECOSKILLER_Hiring_Bias_Detector_SEALED |
| 6 | `workforce_gap_model` | ECOSKILLER_Workforce_Gap_Model_SEALED |
| 7 | `finops` | FINOPS |
| 8 | `idea_submit` | IDEA_SUBMISSION |
| 9 | `idea_version` | IDEA_VERSIONING |
| 10 | `job_portal_migrate` | JOB_PORTAL_MIGRATION |
| 11 | `legacy_erp_migrate` | LEGACY_ERP_MIGRATION |
| 12 | `macro_model` | MACRO_MODEL |
| 13 | `platform_sovereignty` | PLATFORM_SOVEREIGNTY_AGENT |
| 14 | `pricing_strategy` | PRICING_STRATEGY |
| 15 | `revenue_recognition` | REVENUE_RECOGNITION |
| 16 | `seo_optimize` | SEO_AGENT |
| 17 | `tenant_export` | TENANT_EXPORT_AGENT |

---

## Quick Start

```bash
mvn clean package -DskipTests
java -jar target/mcp-08-economics-1.0.0.jar
```

Server: `http://localhost:8708`

---

## Protocol Usage

### Initialize
```json
POST /mcp
{"jsonrpc":"2.0","id":"1","method":"initialize",
 "params":{"protocolVersion":"2024-11-05","clientInfo":{"name":"claude","version":"1.0"}}}
```

### List All 17 Tools
```json
POST /mcp
{"jsonrpc":"2.0","id":"2","method":"tools/list"}
```

### Call a Tool — Example: Board KPIs
```json
POST /mcp
{"jsonrpc":"2.0","id":"3","method":"tools/call",
 "params":{"name":"board_reporting","arguments":{"action":"get_kpis","period":"Q3-2025"}}}
```

### Call a Tool — Example: Digital Twin Stress Test
```json
POST /mcp
{"jsonrpc":"2.0","id":"4","method":"tools/call",
 "params":{"name":"digital_twin_simulate","arguments":{"action":"stress_test"}}}
```

### Call a Tool — Example: Revenue Recognition MRR
```json
POST /mcp
{"jsonrpc":"2.0","id":"5","method":"tools/call",
 "params":{"name":"revenue_recognition","arguments":{"action":"calculate_mrr","period":"2025-09"}}}
```

---

## Architecture

```
McpEconomicsServerApplication (Spring Boot, port 8708)
│
├── McpServerController         POST /mcp  |  GET /mcp/sse  |  GET /mcp/health
├── ToolRegistry                Auto-discovers all @Component AgentTool beans
│
└── 17 Agent Tools
    ├── AntitrustAgent          (CCI/EU/FTC risk analysis)
    ├── BoardReportingAgent     (Board packs, KPIs, investor updates)
    ├── CustomerSuccessAgent    (Health scores, churn, onboarding)
    ├── DigitalTwinSimulationAgent  (Scenario modeling, stress tests)
    ├── HiringBiasDetectorAgent (JD analysis, pipeline audit)
    ├── WorkforceGapModelAgent  (Skill gap, attrition, forecasting)
    ├── FinOpsAgent             (Cloud spend, waste, unit economics)
    ├── IdeaSubmissionAgent     (Innovation pipeline)
    ├── IdeaVersioningAgent     (Versioning, diff, rollback)
    ├── JobPortalMigrationAgent (Naukri, LinkedIn, ATS migration)
    ├── LegacyErpMigrationAgent (Tally, SAP B1, Oracle migration)
    ├── MacroModelAgent         (EdTech market, policy impact)
    ├── PlatformSovereigntyAgent (Vendor lock-in, data localisation)
    ├── PricingStrategyAgent    (Tiers, bundles, competitive analysis)
    ├── RevenueRecognitionAgent (Ind AS 115, MRR/ARR, deferral)
    ├── SeoAgent                (Keywords, audit, rank tracking)
    └── TenantExportAgent       (PDPB/GDPR compliant data export)
```

---

## Port Convention (Ecoskiller MCP Fleet)

| Server | Port |
|--------|------|
| mcp-07-integrations | 8707 |
| **mcp-08-economics** | **8708** |
| mcp-09-intelligence | 8709 |

---

*Ecoskiller Engineering · mcp-08-economics v1.0.0*
