# mcp-17-royalty

**Ecoskiller | CAT-17 — Marketplace, Royalty & Compliance**
MCP Server in Java | 18 Agents | Priority: HIGH

---

## Agents (18)

| # | Tool Name | Agent |
|---|-----------|-------|
| 1 | `tax_compliance` | TAX_COMPLIANCE_AGENT |
| 2 | `school_auto_creation` | SCHOOL_AUTO_CREATION_AGENT |
| 3 | `royalty_wallet` | ROYALTY_WALLET_AGENT |
| 4 | `royalty_system_unified` | ROYALTY_SYSTEM_UNIFIED_AGENT |
| 5 | `royalty_escrow` | ROYALTY_ESCROW_AGENT |
| 6 | `royalty_distribution` | ROYALTY_DISTRIBUTION_AGENT |
| 7 | `royalty_calculation` | ROYALTY_CALCULATION_AGENT |
| 8 | `revenue_ingestion` | REVENUE_INGESTION_AGENT |
| 9 | `parent_dashboard` | PARENT_DASHBOARD_AGENT |
| 10 | `market_demand_prediction` | MARKET_DEMAND_PREDICTION_AGENT |
| 11 | `license_generation` | LICENSE_GENERATION_AGENT |
| 12 | `idea_visibility_control` | IDEA_VISIBILITY_CONTROL_AGENT |
| 13 | `idea_evaluation` | IDEA_EVALUATION_AGENT |
| 14 | `data_retention_policy` | DATA_RETENTION_POLICY_AGENT |
| 15 | `contract_lifecycle` | CONTRACT_LIFECYCLE_AGENT |
| 16 | `competition_engine` | COMPETITION_ENGINE_AGENT |
| 17 | `business_matching` | BUSINESS_MATCHING_AGENT |
| 18 | `audit_verification` | AUDIT_VERIFICATION_AGENT |

---

## Requirements

- Java 17+
- Maven 3.8+ (build only)
- No external runtime dependencies

---

## Build

```bash
mvn package -DskipTests
# Output: target/mcp-17-royalty-1.0.0.jar
```

## Run

```bash
# Development
ENV=dev java -jar target/mcp-17-royalty-1.0.0.jar

# Production
ENV=prod AUDIT_LOG_PATH=/var/log/ecoskiller/mcp-17-royalty-audit.log \
  java -jar target/mcp-17-royalty-1.0.0.jar
```

## Run Tests

```bash
mvn test                         # 49 tests — no live dependencies needed
mvn test -Dsurefire.useFile=false  # verbose output
```

---

## Security Model

| Control | Implementation |
|---------|---------------|
| **Input validation** | All IDs regex-validated `[a-zA-Z0-9\-_]{1,128}` |
| **Amount safety** | No negatives, no NaN/Inf, max 1 billion cap |
| **Rate validation** | `royalty_rate` enforced 0.0–1.0 |
| **Enum allow-lists** | Jurisdictions, payout methods, currencies, statuses |
| **XSS prevention** | `<>'";\` blocked in free-text fields |
| **Audit logging** | Every call logged (tool, subject, status, timestamp) |
| **Sensitive data** | SSN, PAN, bank accounts never logged |
| **Minor protection** | Guardian consent required; payouts to custodial accounts only |
| **Fraud gate** | Amounts > ₹1M flagged for multi-sig approval |
| **7-year retention** | Immutable ledger, GAAP-compliant data retention policy |

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## File Structure

```
mcp-17-royalty/
├── pom.xml
├── README.md
├── claude_desktop_config.json
└── src/
    ├── main/java/com/ecoskiller/mcp/royalty/
    │   ├── RoyaltyEngineMcpServer.java   ← Main server + dispatch
    │   ├── McpTool.java                  ← Tool interface
    │   ├── security/
    │   │   ├── InputValidator.java       ← Security validation layer
    │   │   └── AuditLogger.java          ← Append-only audit log
    │   └── tools/
    │       ├── BaseTool.java             ← Shared helpers + financial math
    │       ├── TaxComplianceTool.java
    │       ├── SchoolAutoCreationTool.java
    │       ├── RoyaltyWalletTool.java
    │       ├── RoyaltySystemUnifiedTool.java
    │       ├── RoyaltyEscrowTool.java
    │       ├── RoyaltyDistributionTool.java
    │       ├── RoyaltyCalculationTool.java
    │       ├── RevenueIngestionTool.java
    │       ├── ParentDashboardTool.java
    │       ├── MarketDemandPredictionTool.java
    │       ├── LicenseGenerationTool.java
    │       ├── IdeaVisibilityControlTool.java
    │       ├── IdeaEvaluationTool.java
    │       ├── DataRetentionPolicyTool.java
    │       ├── ContractLifecycleTool.java
    │       ├── CompetitionEngineTool.java
    │       ├── BusinessMatchingTool.java
    │       └── AuditVerificationTool.java
    └── test/java/com/ecoskiller/mcp/royalty/
        └── RoyaltyEngineMcpServerTest.java  ← 49 tests
```

---

*Ecoskiller Platform Engineering · MCP-17 Royalty Engine · v1.0.0 · March 2026*
