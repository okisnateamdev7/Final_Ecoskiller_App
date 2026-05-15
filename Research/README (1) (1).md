# mcp-17-royalty

**Ecoskiller | CAT-17 — Marketplace, Royalty & Compliance**  
MCP Server in Python | 18 Agents | Priority: HIGH

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

- Python 3.8+ (no external packages needed)

---

## Run the server

```bash
python3 server.py
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-17-royalty": {
      "command": "python3",
      "args": ["/absolute/path/to/mcp-17-royalty/server.py"]
    }
  }
}
```

---

## Run tests

```bash
python3 test_agents.py           # quick pass/fail
python3 test_agents.py --verbose # with full JSON output
```

---

## File Structure

```
mcp-17-royalty/
├── server.py                  ← Main MCP server (all 18 agents)
├── test_agents.py             ← Test all agents locally
├── claude_desktop_config.json ← Claude Desktop config snippet
└── README.md
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`
