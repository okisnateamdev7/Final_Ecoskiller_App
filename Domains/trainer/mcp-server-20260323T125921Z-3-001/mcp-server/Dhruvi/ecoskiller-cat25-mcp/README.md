# mcp-25-analytics-devops

**Ecoskiller | CAT-25 — Analytics & ERP / DevOps & Environment**
MCP Server in Java | 13 Agents | 27 Tests | Priority: HIGH

---

## Agents (13)

### Analytics & ERP (5)
| # | Tool Name | Agent |
|---|-----------|-------|
| 18 | `clickhouse_metric_normalization` | CLICKHOUSE_METRIC_NORMALIZATION_AGENT |
| 19 | `erp_go_report_integration` | ERP_GO_REPORT_INTEGRATION_AGENT |
| 20 | `phone_feature_vector_emission` | PHONE_FEATURE_VECTOR_EMISSION_AGENT |
| 21 | `attendance_behavior_analytics` | ATTENDANCE_BEHAVIOR_ANALYTICS_AGENT |
| 22 | `enrollment_analytics` | ENROLLMENT_ANALYTICS_AGENT |

### DevOps & Environment (8)
| # | Tool Name | Agent |
|---|-----------|-------|
| 23 | `multi_environment_phone_config_validator` | MULTI_ENVIRONMENT_PHONE_CONFIG_VALIDATOR_AGENT |
| 24 | `phone_backup_restore_validation` | PHONE_BACKUP_RESTORE_VALIDATION_AGENT |
| 25 | `phone_end_to_end_trace` | PHONE_END_TO_END_TRACE_AGENT |
| 26 | `phone_external_webhook` | PHONE_EXTERNAL_WEBHOOK_AGENT |
| 27 | `phone_infra_health_check` | PHONE_INFRA_HEALTH_CHECK_AGENT |
| 28 | `phone_monitoring_clock_authority` | PHONE_MONITORING_CLOCK_AUTHORITY_AGENT |
| 29 | `cross_node_time_drift_monitor` | CROSS_NODE_TIME_DRIFT_MONITOR_AGENT |
| 30 | `model_governance_registry` | MODEL_GOVERNANCE_REGISTRY_AGENT |

---

## Requirements

- **Java 11+** (tested on Java 21)
- **Zero external dependencies** — pure Java stdlib only

---

## Build

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
```

Or via convenience script:
```bash
chmod +x build.sh && ./build.sh
```

---

## Run Tests

```bash
./build.sh test             # Expected: 27 passed, 0 failed
./build.sh test --verbose   # With full JSON-RPC output
```

---

## Run the Server

```bash
./build.sh server
# or:
java -cp out com.ecoskiller.mcp.server.McpServer
```

Communicates via **stdin/stdout** using **JSON-RPC 2.0**.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-25-analytics-devops": {
      "command": "java",
      "args": ["-cp", "/absolute/path/to/mcp-25-java/out",
               "com.ecoskiller.mcp.server.McpServer"]
    }
  }
}
```

---

## File Structure

```
mcp-25-java/
├── build.sh
├── claude_desktop_config.json
├── README.md
└── src/main/java/com/ecoskiller/mcp/
    ├── TestAgents.java                                   ← 27 test cases
    ├── server/
    │   └── McpServer.java                               ← stdio entry point
    ├── protocol/
    │   ├── JsonRpcHandler.java                          ← dispatcher (13 agents)
    │   ├── JsonParser.java                              ← zero-dep JSON parser
    │   └── JsonSerializer.java                          ← zero-dep JSON serializer
    └── agents/
        ├── Agent.java                                   ← interface
        ├── BaseAgent.java                               ← schema/arg helpers
        ├── ClickhouseMetricNormalizationAgent.java
        ├── ErpGoReportIntegrationAgent.java
        ├── PhoneFeatureVectorEmissionAgent.java
        ├── AttendanceBehaviorAnalyticsAgent.java
        ├── EnrollmentAnalyticsAgent.java
        ├── MultiEnvironmentPhoneConfigValidatorAgent.java
        ├── PhoneBackupRestoreValidationAgent.java
        ├── PhoneEndToEndTraceAgent.java
        ├── PhoneExternalWebhookAgent.java
        ├── PhoneInfraHealthCheckAgent.java
        ├── PhoneMonitoringClockAuthorityAgent.java
        ├── CrossNodeTimeDriftMonitorAgent.java
        └── ModelGovernanceRegistryAgent.java
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Adding a New Agent

1. Create `agents/MyNewAgent.java` extending `BaseAgent`
2. Implement `toolName()`, `description()`, `inputSchema()`, `execute()`
3. Add `register(new MyNewAgent())` in `JsonRpcHandler`
4. Add test cases in `TestAgents.java`
5. Recompile: `javac -d out $(find src -name "*.java")`
