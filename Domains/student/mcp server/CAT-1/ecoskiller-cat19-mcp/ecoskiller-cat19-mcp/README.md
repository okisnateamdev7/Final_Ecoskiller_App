# ECOSKILLER ANTIGRAVITY — CAT-19 MCP Server
## Platform Stability & Risk

```
╔══════════════════════════════════════════════════════════════════╗
║   ECOSKILLER ANTIGRAVITY — CAT-19 MCP SERVER                   ║
║   19. Platform Stability & Risk                                 ║
║   Version: 1.0.0-ANTIGRAVITY-SEALED                            ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## Agents & Tools

### 1. POLICY_VERSION_CONTROL_AGENT
Tracks all policy document versions, detects unauthorized changes, and enforces approval workflows.

| Tool | Description |
|------|-------------|
| `policy_version_control__get_policy_version` | Fetch current version + optional history |
| `policy_version_control__compare_versions` | Diff two versions with risk classification |
| `policy_version_control__approve_policy` | Submit approval/rejection for a pending version |
| `policy_version_control__detect_unauthorized_changes` | Scan all policies for hash drift |

---

### 2. REGULATORY_COMPLIANCE_MAPPING_AGENT
Maps platform features to applicable regulations (DPDP, IT Act, GST, UGC, AICTE, ISO-27001) and flags gaps.

| Tool | Description |
|------|-------------|
| `regulatory_compliance__map_feature_to_regulations` | Map a feature to all applicable regulations |
| `regulatory_compliance__run_compliance_gap_analysis` | Full gap analysis per framework |
| `regulatory_compliance__get_compliance_scorecard` | Real-time compliance health dashboard |

---

### 3. RESOURCE_CONSUMPTION_GUARD_AGENT
Monitors CPU/memory/storage/API quotas per tenant and service. Enforces limits and triggers throttling.

| Tool | Description |
|------|-------------|
| `resource_guard__get_consumption_snapshot` | Real-time resource usage snapshot |
| `resource_guard__set_resource_limit` | Set soft/hard limits for any resource |
| `resource_guard__get_overconsumption_alerts` | List all active overconsumption alerts |
| `resource_guard__trigger_throttle` | Manually throttle or hard-stop a service |

---

### 4. REVENUE_SHARE_RECONCILIATION_AGENT
Reconciles revenue splits across all stakeholder tiers. Detects discrepancies, manages escrow holds.

| Tool | Description |
|------|-------------|
| `revenue_reconciliation__reconcile_period` | Full reconciliation for a billing period |
| `revenue_reconciliation__get_payout_breakdown` | Step-by-step payout calculation for a stakeholder |
| `revenue_reconciliation__flag_discrepancy` | Flag a discrepancy and create escrow hold |
| `revenue_reconciliation__get_platform_revenue_summary` | Platform-level revenue + GST summary |

---

## Build & Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Build
```bash
cd ecoskiller-cat19-mcp
mvn clean package -DskipTests
```

### Run
```bash
java -jar target/cat19-platform-stability-risk-mcp-1.0.0-ANTIGRAVITY.jar
```

Server starts on **port 8019**.

---

## Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/mcp` | JSON-RPC 2.0 message handler |
| GET | `/mcp/sse` | SSE transport (Claude Desktop) |
| GET | `/mcp/health` | Health check |
| GET | `/mcp/manifest` | Agent + tool manifest |

---

## Claude Desktop Integration

Copy `claude_desktop_config.json` entries into your Claude Desktop config at:
- macOS: `~/Library/Application Support/Claude/claude_desktop_config.json`
- Windows: `%APPDATA%\Claude\claude_desktop_config.json`

Update the JAR path before use.

---

## Tool Naming Convention

All tools follow the pattern:
```
{agent_prefix}__{action}
```

| Agent | Prefix |
|-------|--------|
| POLICY_VERSION_CONTROL_AGENT | `policy_version_control__` |
| REGULATORY_COMPLIANCE_MAPPING_AGENT | `regulatory_compliance__` |
| RESOURCE_CONSUMPTION_GUARD_AGENT | `resource_guard__` |
| REVENUE_SHARE_RECONCILIATION_AGENT | `revenue_reconciliation__` |

---

*ECOSKILLER ANTIGRAVITY — CAT-19 — SEALED v1.0.0*
