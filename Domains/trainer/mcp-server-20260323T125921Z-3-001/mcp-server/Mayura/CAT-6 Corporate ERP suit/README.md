# mcp-06-corporate-erp

**Ecoskiller | CAT-6 — Corporate ERP Suite**  
MCP Server in Java | 12 Agents | Priority: HIGH

---

## Agents (12)

| #  | Tool Name          | Agent |
|----|--------------------|-------|
| 17 | `accounting`       | ACCOUNTING_AGENT__ANTIGRAVITY_CORPORATE_ERP |
| 18 | `accounting_sync`  | ACCOUNTING_SYNC_AGENT |
| 19 | `asset`            | ASSET_AGENT_ANTIGRAVITY_ERP |
| 20 | `budget`           | BUDGET_AGENT |
| 21 | `dms`              | DMS_AGENT_ANTIGRAVITY_ERP |
| 22 | `erp_analytics`    | ERP_ANALYTICS_AGENT |
| 23 | `gst_connect`      | GST_CONNECT_AGENT |
| 24 | `hrms`             | HRMS_AGENT_ANTIGRAVITY_SEALED |
| 25 | `ledger_migration` | LEDGER_MIGRATION_AGENT |
| 26 | `payroll`          | PAYROLL_AGENT_ANTIGRAVITY_ERP |
| 27 | `procurement`      | PROCUREMENT_AGENT_ANTIGRAVITY_ERP |
| 28 | `regulatory`       | REGULATORY_AGENT_ANTIGRAVITY_ERP |

---

## Requirements

- Java 11+
- Maven 3.6+ *(build only — zero runtime dependencies)*

---

## Build

```bash
mvn clean package
# produces: target/mcp-06-corporate-erp.jar
```

---

## Run the server

```bash
java -jar target/mcp-06-corporate-erp.jar
```

Communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-06-corporate-erp": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-06-corporate-erp/target/mcp-06-corporate-erp.jar"]
    }
  }
}
```

---

## Run tests

```bash
bash test_agents.sh            # 27 tests — pass/fail summary
bash test_agents.sh --verbose  # with full JSON output
```

---

## File Structure

```
mcp-06-corporate-erp/
├── pom.xml                              ← Maven build (fat JAR, Java 11+)
├── test_agents.sh                       ← Bash test runner (27 tests)
├── README.md
└── src/main/java/ecoskiller/
    └── McpServer.java                   ← Main MCP server (all 12 agents)
```

---

## Agent Capabilities

### 17. `accounting` — ACCOUNTING_AGENT__ANTIGRAVITY_CORPORATE_ERP
Double-entry bookkeeping, journal entries, trial balance, P&L, balance sheets, period-end closing. IND-AS & IFRS compliant.  
**Params:** `action`, `account_code`, `debit`, `credit`, `narration`, `fiscal_year`

---

### 18. `accounting_sync` — ACCOUNTING_SYNC_AGENT
Bi-directional sync with Tally, QuickBooks, Zoho Books, SAP FICO. Supports incremental & full-sync modes with conflict resolution.  
**Params:** `action`, `source_platform`, `target_platform`, `entity_type`, `sync_mode`, `period`

---

### 19. `asset` — ASSET_AGENT_ANTIGRAVITY_ERP
Fixed asset tracking: acquisition, SLM/WDV depreciation, disposal, revaluation, insurance, physical verification.  
**Params:** `action`, `asset_id`, `asset_category`, `acquisition_date`, `depreciation_method`, `location`

---

### 20. `budget` — BUDGET_AGENT
Budget preparation, cost-centre allocation, variance analysis, forecast revisions, budget vs actuals.  
**Params:** `action`, `budget_id`, `cost_centre`, `department`, `fiscal_year`, `amount`

---

### 21. `dms` — DMS_AGENT_ANTIGRAVITY_ERP
Corporate document lifecycle: ingest, OCR, index, version, retrieve, retention-policy enforcement, role-based access.  
**Params:** `action`, `document_id`, `document_type`, `department`, `tags`, `retention_policy`

---

### 22. `erp_analytics` — ERP_ANALYTICS_AGENT
Cross-module KPI dashboards, cash-flow forecasts, ageing reports, trend analysis, anomaly detection from live ERP data.  
**Params:** `action`, `report_type`, `module`, `date_from`, `date_to`, `format`

---

### 23. `gst_connect` — GST_CONNECT_AGENT
India GST: GSTR-1/2A/3B filing, e-invoicing (IRN + QR), e-way bills, ITC reconciliation, GSTIN validation.  
**Params:** `action`, `gstin`, `return_type`, `period`, `invoice_id`, `amount`

---

### 24. `hrms` — HRMS_AGENT_ANTIGRAVITY_SEALED
Full HR lifecycle: recruitment, onboarding, profiles, appraisals, leave balances, separation, org-chart management.  
**Params:** `action`, `employee_id`, `department`, `designation`, `join_date`, `status`

---

### 25. `ledger_migration` — LEDGER_MIGRATION_AGENT
Migrates chart of accounts, ledger masters, opening balances, and transactions from Tally/SAP to Ecoskiller ERP with rollback.  
**Params:** `action`, `source_system`, `ledger_id`, `migration_mode`, `fiscal_year`, `validate_only`

---

### 26. `payroll` — PAYROLL_AGENT_ANTIGRAVITY_ERP
Monthly payroll: salary computation, PF/ESI/PT/TDS deductions, pay-slip generation, NEFT bank file, Form-16.  
**Params:** `action`, `employee_id`, `pay_period`, `salary_component`, `tds_applicable`, `bank_account`

---

### 27. `procurement` — PROCUREMENT_AGENT_ANTIGRAVITY_ERP
Procure-to-pay: indent → PO → GRN → three-way match → vendor payment. Spend analytics and vendor rating.  
**Params:** `action`, `po_id`, `vendor_id`, `item_code`, `quantity`, `unit_price`, `delivery_date`

---

### 28. `regulatory` — REGULATORY_AGENT_ANTIGRAVITY_ERP
Corporate filings: TDS 24Q/26Q, GSTR-1/3B, MCA-AOC4, PF-ECR, ESI challan, FEMA FC-GPR. Compliance score tracking.  
**Params:** `action`, `regulation_type`, `filing_period`, `entity_id`, `due_date`, `jurisdiction`

---

## Protocol

| Property    | Value             |
|-------------|-------------------|
| Transport   | stdio (stdin/stdout) |
| Format      | JSON-RPC 2.0      |
| MCP Version | 2024-11-05        |
| Methods     | `initialize`, `tools/list`, `tools/call`, `ping` |

---

## Sample call

```bash
echo '{"jsonrpc":"2.0","id":1,"method":"tools/call","params":{"name":"gst_connect","arguments":{"action":"file_return","gstin":"27AAAAA0000A1Z5","return_type":"GSTR3B","period":"062025","amount":"500000"}}}' \
  | java -jar target/mcp-06-corporate-erp.jar
```
