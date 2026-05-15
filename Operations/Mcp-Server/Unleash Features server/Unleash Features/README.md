# unleash-mcp-server

**Ecoskiller | Unleash Feature Flag Service**  
MCP Server in Java | 16 Tools | Security: HIGH  
Protocol: JSON-RPC 2.0 / MCP 2024-11-05 | Zero external dependencies

---

## Overview

Java MCP server exposing the full Ecoskiller Unleash Feature Flag workflow as Claude-callable tools.
Covers every pattern from the Ecoskiller documentation: gradual canary releases, instant kill-switches,
A/B testing with variants, targeted rollouts, immutable audit trails, and DPDPA 2023 / ISO 27001
compliance reporting — all with 4 independent security layers.

---

## Tools (16)

| # | Tool | Description |
|---|------|-------------|
| 1  | `create_flag`             | Create a flag — always starts DISABLED (safe default) |
| 2  | `get_flag`                | Full details: status, strategy, variants, audit trail |
| 3  | `list_flags`              | List with env / status / lifecycle / owner filters |
| 4  | `enable_flag`             | Enable with audit trail (DPDPA 2023 compliance) |
| 5  | `disable_flag`            | ⚡ **KILL-SWITCH** — instant disable, < 1 min MTTR |
| 6  | `delete_flag`             | ARCHIVE (keeps audit trail) or permanent DELETE |
| 7  | `update_strategy`         | Change targeting: users / orgs / roles / custom |
| 8  | `gradual_rollout`         | Set canary %: 0 → 5 → 10 → 25 → 50 → 100% |
| 9  | `add_variant`             | Add A/B variant with traffic weight + payload |
| 10 | `get_variants`            | Variant config: weights, traffic %, payloads |
| 11 | `evaluate_flag`           | Evaluate for a user context (mirrors SDK `isEnabled()`) |
| 12 | `get_audit_log`           | Immutable change history — who did what and when |
| 13 | `get_metrics`             | Evaluation counts, enabled %, cleanup candidates |
| 14 | `list_environments`       | dev / test / staging / production |
| 15 | `get_compliance_report`   | DPDPA 2023, IT Act 2000, ISO 27001 audit reports |
| 16 | `manage_flag_lifecycle`   | ACTIVE → PENDING_CLEANUP → ARCHIVED |

---

## Ecoskiller Flag Strategies

| Strategy | When to use | Ecoskiller example |
|----------|-------------|-------------------|
| `STANDARD` | Simple on/off all users | Maintenance mode banner |
| `GRADUAL_ROLLOUT` | Canary releases | `scoring-llm-v1` 0→5→10→25→50→100% |
| `USERID` | Beta testers only | `voice-recognition-beta` for testuser1,2,3 |
| `ORGANIZATION` | Enterprise / tenant features | `premium-analytics-v1` for enterprise orgs |
| `ROLE_BASED` | RECRUITER or CANDIDATE | `new-scoring-ui` for RECRUITER only |
| `CUSTOM_CONSTRAINT` | Combined AND-logic | user + org + role all must match |

---

## Ecoskiller Canary Pattern

```
create_flag  'scoring-llm-v1'   strategy=GRADUAL_ROLLOUT
gradual_rollout 5%   → monitor: score distribution, error rate (4h)
gradual_rollout 10%  → check for outliers, latency spikes (4h)
gradual_rollout 25%  → verify normal score distribution (4h)
gradual_rollout 50%  → monitor billing / recruiter satisfaction (4h)
gradual_rollout 100% → full rollout ✅

If anomaly detected at ANY stage:
  disable_flag 'scoring-llm-v1'  → ⚡ instant rollback < 1 minute
  (vs 30+ min for code redeployment)
```

---

## A/B Testing Pattern

```
create_flag   'recruiter-dashboard-redesign'
add_variant   variant_name=control    weight=33
add_variant   variant_name=variant_a  weight=33  payload={"theme":"blue"}
add_variant   variant_name=variant_b  weight=34  payload={"theme":"green"}
enable_flag   'recruiter-dashboard-redesign'

→ SDK: unleash.getVariant('recruiter-dashboard-redesign', {userId})
→ ClickHouse tracks conversion rates per variant
→ Disable and pick winner after statistical significance
```

---

## Security Architecture

### 1. InputSanitizer (`InputSanitizer.java`)
Applied to **every argument** of **every tool call** before any logic runs:
- **Shell metacharacter blocking**: `;` `&&` `|` `` ` `` `$` `()` `<>` `\n` etc.
- **SQL injection prevention**: `OR`, `SELECT`, `UNION`, `--`, `/*`, `EXEC` etc.
- **Flag name enforcement**: only `[a-zA-Z0-9_-]`, max 128 chars, must start alphanumeric
- **Environment enum**: only `dev / test / staging / production` accepted
- **Identifier validation**: safe chars only for userId, orgId, teamName
- **Oversized input**: max 512 chars per string field
- **Null byte detection**: prevents path truncation tricks

### 2. RateLimiter (`RateLimiter.java`)
Sliding-window: **60 requests/minute**. Prevents DoS abuse of the MCP endpoint.

### 3. ApiKeyValidator (`ApiKeyValidator.java`)
Set `UNLEASH_MCP_API_KEY` env var to enforce authentication.  
Constant-time comparison via `MessageDigest.isEqual()` — no timing attacks.

### 4. Immutable Audit Log
Every mutation (`enable`, `disable`, `strategy change`, `variant add`) records:
`user → action: delta` with timestamp. Never deleted. Queryable via `get_audit_log`.

---

## Requirements

- **Java 8+** (no external dependencies — stdlib only)

---

## Build & Run

```bash
chmod +x build.sh
./build.sh

# Run MCP server (stdin/stdout transport)
java -jar target/unleash-mcp-server.jar

# With API key enforcement
UNLEASH_MCP_API_KEY=my-secret java -jar target/unleash-mcp-server.jar

# Run tests
java -cp target/unleash-mcp-server.jar com.ecoskiller.unleash.server.UnleashMcpServerTest

# Verbose tests (full request/response)
java -cp target/unleash-mcp-server.jar com.ecoskiller.unleash.server.UnleashMcpServerTest --verbose
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "unleash-mcp-server": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/unleash-mcp-server/target/unleash-mcp-server.jar"],
      "env": {
        "UNLEASH_MCP_API_KEY": "your-secret-key"
      }
    }
  }
}
```

---

## Compliance Coverage

| Framework | Controls | Status |
|-----------|----------|--------|
| **DPDPA 2023** | Purpose limitation, data fiduciary obligations, grievance redressal | ✅ Audit log per every change |
| **IT Act 2000** | Sec 43A reasonable security, Sec 72A instant disable capability | ✅ Kill-switch + audit |
| **ISO 27001** | A.12.1.2 change management, A.14.2.9 acceptance testing, A.16.1.5 incident response | ✅ Canary + kill-switch |

Use `get_compliance_report framework=ALL` to generate per-framework audit evidence.

---

## File Structure

```
unleash-mcp-server/
├── build.sh
├── README.md
├── claude_desktop_config.json
└── src/main/java/com/ecoskiller/unleash/
    ├── server/
    │   ├── UnleashMcpServer.java          ← Main JSON-RPC 2.0 loop (16 tools)
    │   └── UnleashMcpServerTest.java      ← 76 integration tests
    ├── model/
    │   ├── FeatureFlag.java               ← Flag domain + evaluation engine + audit
    │   ├── FlagStore.java                 ← Thread-safe in-memory registry
    │   └── ToolResult.java
    ├── tools/
    │   ├── McpTool.java                   ← Tool interface
    │   ├── CreateFlagTool.java            ← Tool 1
    │   ├── GetFlagTool.java               ← Tool 2
    │   ├── ListFlagsTool.java             ← Tool 3
    │   ├── EnableFlagTool.java            ← Tool 4
    │   ├── DisableFlagTool.java           ← Tool 5 (kill-switch)
    │   ├── DeleteFlagTool.java            ← Tool 6
    │   ├── UpdateStrategyTool.java        ← Tool 7
    │   ├── GradualRolloutTool.java        ← Tool 8 (canary %)
    │   ├── AddVariantTool.java            ← Tool 9 (A/B variants)
    │   ├── GetVariantsTool.java           ← Tool 10
    │   ├── EvaluateFlagTool.java          ← Tool 11 (SDK evaluation)
    │   ├── GetAuditLogTool.java           ← Tool 12 (compliance audit)
    │   ├── GetMetricsTool.java            ← Tool 13
    │   ├── ListEnvironmentsTool.java      ← Tool 14
    │   ├── GetComplianceReportTool.java   ← Tool 15 (DPDPA/ISO27001)
    │   └── ManageFlagLifecycleTool.java   ← Tool 16
    ├── security/
    │   ├── InputSanitizer.java            ← Shell/SQL/injection prevention
    │   ├── RateLimiter.java               ← 60 req/min sliding window
    │   └── ApiKeyValidator.java           ← Constant-time key validation
    └── util/
        ├── JsonUtils.java                 ← Zero-dep JSON-RPC 2.0 builder/parser
        └── Logger.java                    ← stderr-only logger
```

---

## Protocol

- Transport : **stdio** (stdin/stdout)
- Format    : **JSON-RPC 2.0**
- MCP Ver   : **2024-11-05**
- Methods   : `initialize`, `tools/list`, `tools/call`, `ping`
