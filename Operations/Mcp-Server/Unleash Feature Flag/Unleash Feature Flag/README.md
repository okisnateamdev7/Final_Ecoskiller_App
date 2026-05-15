# unleash-mcp-server

**Ecoskiller | Unleash Feature Flag Service**  
MCP Server in Java | 18 Tools | Security: 10 Layers  
Protocol: JSON-RPC 2.0 / MCP 2024-11-05 | Zero External Dependencies

---

## Overview

Java MCP server implementing the complete Ecoskiller Unleash Feature Flag workflow.
Built from the Ecoskiller technical documentation (v1.0, March 2025) covering 50+ microservices,
multi-tenant scoping, Keycloak RBAC, Kafka event streaming, consistent-hash rollouts,
A/B testing, kill-switches, scheduled launches, and DPDPA 2023 / IT Act 2000 / ISO 27001 compliance.

---

## Tools (18)

| # | Tool | Description |
|---|------|-------------|
| 1  | `create_flag`           | Create flag — always DISABLED, naming convention enforced |
| 2  | `get_flag`              | Full details: status, strategy, variants, metrics, audit trail |
| 3  | `list_flags`            | List with env/status/lifecycle/tenant/strategy/team filters |
| 4  | `enable_flag`           | Enable with immutable audit (DPDPA 2023 compliance) |
| 5  | `disable_flag`          | ⚡ **KILL SWITCH** — WebSocket push < 100ms, MTTR seconds |
| 6  | `delete_flag`           | ARCHIVE (keeps audit) or permanent DELETE |
| 7  | `gradual_rollout`       | Canary: consistent hash(userId)%100, 0→1→5→10→25→50→100% |
| 8  | `update_strategy`       | USERID / TENANT / GROUP / CUSTOM_CONSTRAINT targeting |
| 9  | `add_variant`           | A/B arms: deterministic variant assignment, no flickering |
| 10 | `evaluate_flag`         | Mirror SDK `isEnabled()` + `getVariant()` with reasoning |
| 11 | `schedule_flag`         | `enable_at` / `disable_at` time-window activation |
| 12 | `get_audit_log`         | Immutable log — who/what/when, forensic analysis |
| 13 | `get_metrics`           | Eval counts, enabled%, variant distribution, cleanup hints |
| 14 | `get_variants`          | Variant weights, traffic%, eval counts, payloads |
| 15 | `manage_lifecycle`      | ACTIVE → DEPRECATED → ARCHIVED (prevent flag explosion) |
| 16 | `list_environments`     | dev / test / staging / production with SLA targets |
| 17 | `get_compliance_report` | DPDPA 2023, IT Act 2000, ISO 27001 audit evidence |
| 18 | `bulk_export_import`    | Export all flags as JSON snapshot (GitOps / backup) |

---

## Flag Strategies

| Strategy | Use case | Ecoskiller example |
|----------|----------|--------------------|
| `DEFAULT` | Simple global on/off | Maintenance banner |
| `GRADUAL_ROLLOUT` | Canary release | `scoring-ai-v2-experiment` 0→100% |
| `USERID` | Beta testers | `voice-recognition-beta` for testuser1,2,3 |
| `TENANT` | Enterprise-only features | `premium-analytics-v1` for tenant-001 |
| `GROUP` | User segments | `notif-whatsapp` for beta-testers |
| `CUSTOM_CONSTRAINT` | AND-logic combination | tenant + group + plan |
| `SCHEDULED` | Time-window activation | Global launch at 2025-03-15T10:00Z |

---

## Naming Convention

Follow Ecoskiller's documented pattern: `team-feature-version`

```
scoring-ai-v2-experiment      ← scoring team, AI scorer, v2
ui-new-gd-dashboard-beta      ← UI team, GD dashboard, beta
auth-mfa-enforcement          ← auth team, MFA feature
notif-channel-whatsapp-v1     ← notifications team, WhatsApp, v1
premium-analytics-v1          ← analytics team, premium tier, v1
platform-dark-launch-v2       ← platform team, dark launch, v2
```

---

## Canary Deployment Pattern

```
create_flag  'scoring-ai-v2-experiment'  strategy=GRADUAL_ROLLOUT
enable_flag  → starts active but at 0%
gradual_rollout  1%  → monitor: error rate, latency p95, score distribution (1h)
gradual_rollout 10%  → check: no scoring anomalies, normal distribution (2h)
gradual_rollout 25%  → validate: completion rates, recruiter satisfaction (4h)
gradual_rollout 50%  → monitor: billing impact, support tickets (4h)
gradual_rollout 100% → full rollout ✅

Anomaly at ANY stage:
  disable_flag 'scoring-ai-v2-experiment' reason='score distribution spike'
  → WebSocket push to all 50+ microservice SDKs within 100ms
  → MTTR: seconds (vs 30+ minutes for code redeployment)
```

---

## A/B Testing Pattern

```
create_flag 'ui-new-gd-dashboard-beta'
add_variant  control    weight=34  (existing UI)
add_variant  variant-a  weight=33  payload={"theme":"inline-timer"}
add_variant  variant-b  weight=33  payload={"theme":"sidebar-timer"}
enable_flag

SDK: unleash.getVariant('ui-new-gd-dashboard-beta', {userId})
→ Returns: { name: 'variant-a', enabled: true, payload: {...} }
→ Same userId always gets same variant (deterministic, no flickering)
→ Kafka event: feature-flag-evaluated → ClickHouse → A/B analysis
```

---

## Security Architecture (10 Layers)

`InputSanitizer.java` — applied to **every argument** of **every tool**:

| Layer | Check | Blocked example |
|-------|-------|-----------------|
| 1 | Shell metacharacters | `; rm -rf /` |
| 2 | SQL injection patterns | `OR 1=1 --` |
| 3 | Script injection | `<script>alert(1)</script>` |
| 4 | Flag name format | `../etc/passwd`, `flag; inject` |
| 5 | Environment enum | `unknown-env` |
| 6 | Identifier safety | `user$(curl evil.com)` |
| 7 | Percentage range | `101`, `-5` |
| 8 | Oversized input | 513+ character strings |
| 9 | Null bytes | `flag\0suffix` |
| 10 | Plan enum | `SUPERADMIN` |

**RateLimiter** — sliding window, 60 req/min, prevents DoS.  
**ApiKeyValidator** — constant-time SHA-256, set `UNLEASH_MCP_API_KEY` env var.  
**Audit trail** — every mutation recorded, never deleted (compliance requirement).

---

## Requirements

- **Java 8+** — no external dependencies, stdlib only

---

## Build & Run

```bash
chmod +x build.sh && ./build.sh

# Run MCP server (stdin/stdout)
java -jar target/unleash-mcp-server.jar

# With API key enforcement
UNLEASH_MCP_API_KEY=my-secret java -jar target/unleash-mcp-server.jar

# Run tests (95 tests)
java -cp target/unleash-mcp-server.jar com.ecoskiller.unleash.server.UnleashMcpServerTest

# Verbose output
java -cp target/unleash-mcp-server.jar com.ecoskiller.unleash.server.UnleashMcpServerTest --verbose
```

---

## Connect to Claude Desktop

`~/Library/Application Support/Claude/claude_desktop_config.json`:

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

## Compliance

| Framework | Controls Covered |
|-----------|-----------------|
| **DPDPA 2023** | Purpose limitation (kill-switch), data fiduciary audit log, grievance redressal timeline |
| **IT Act 2000** | Sec 43A reasonable security (audited changes), Sec 72A cross-tenant isolation |
| **ISO 27001** | A.12.1.2 change management, A.14.2.9 acceptance testing (canary), A.16.1.5 incident response (kill-switch) |

---

## SLA Targets (from Ecoskiller documentation)

| Metric | Target |
|--------|--------|
| Flag evaluation latency (local cache) | < 10ms p95 |
| Flag update propagation (WebSocket) | < 100ms |
| Throughput per replica | 10K+ evaluations/sec |
| Availability | 99.95% (multi-region GCP + AWS) |
| RTO | < 5 minutes |
| RPO | < 1 minute |

---

## File Structure

```
unleash-mcp-server/
├── build.sh
├── README.md
├── claude_desktop_config.json
└── src/main/java/com/ecoskiller/unleash/
    ├── server/
    │   ├── UnleashMcpServer.java        ← JSON-RPC 2.0 loop, 18 tools registered
    │   └── UnleashMcpServerTest.java    ← 95 integration tests
    ├── model/
    │   ├── FeatureFlag.java             ← Full domain model + 7-strategy evaluation engine
    │   ├── FlagStore.java              ← Thread-safe ConcurrentHashMap registry
    │   └── ToolResult.java
    ├── tools/
    │   ├── McpTool.java                ← Tool interface
    │   ├── CreateFlagTool.java         ← Tool 1
    │   ├── GetFlagTool.java            ← Tool 2
    │   ├── ListFlagsTool.java          ← Tool 3
    │   ├── EnableFlagTool.java         ← Tool 4
    │   ├── DisableFlagTool.java        ← Tool 5 (kill switch)
    │   ├── DeleteFlagTool.java         ← Tool 6
    │   ├── GradualRolloutTool.java     ← Tool 7 (consistent hash canary)
    │   ├── UpdateStrategyTool.java     ← Tool 8
    │   ├── AddVariantTool.java         ← Tool 9 (A/B variants)
    │   ├── EvaluateFlagTool.java       ← Tool 10 (SDK mirror)
    │   ├── ScheduleFlagTool.java       ← Tool 11 (time-window)
    │   ├── GetAuditLogTool.java        ← Tool 12 (compliance)
    │   ├── GetMetricsTool.java         ← Tool 13
    │   ├── GetVariantsTool.java        ← Tool 14
    │   ├── ManageLifecycleTool.java    ← Tool 15
    │   ├── ListEnvironmentsTool.java   ← Tool 16
    │   ├── GetComplianceReportTool.java← Tool 17
    │   └── BulkExportImportTool.java   ← Tool 18 (GitOps export)
    ├── security/
    │   ├── InputSanitizer.java         ← 10 injection defence layers
    │   ├── RateLimiter.java            ← 60 req/min sliding window
    │   └── ApiKeyValidator.java        ← Constant-time SHA-256
    └── util/
        ├── JsonUtils.java              ← Zero-dep JSON-RPC 2.0 builder/parser
        └── Logger.java                 ← stderr-only structured logger
```

---

## Protocol

- Transport : **stdio** (stdin/stdout)
- Format    : **JSON-RPC 2.0**
- MCP Ver   : **2024-11-05**
- Methods   : `initialize`, `tools/list`, `tools/call`, `ping`
