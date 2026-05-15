# mcp-01-platform — Ecoskiller Core Platform MCP Server

**CAT-01 | Core Platform & Meta Architecture**
**Namespace:** `core` | **Priority:** MEDIUM | **Agents:** 11

---

## Overview

This is the Java Spring Boot implementation of the `mcp-01-platform` MCP server for the Ecoskiller platform.
It exposes **11 agents** as **MCP tools** using Spring AI's MCP Server framework over HTTP/SSE transport.

---

## Agents & Tools

| # | Agent | Tools Exposed |
|---|-------|---------------|
| 1 | `ANTIGRAVITY_CONFIGURATION_AGENT` | get_config, push_config, validate_config_schema, rollback_config |
| 2 | `ANTIGRAVITY_DEPENDENCY_RISK_AGENT` | scan_dependency_risk, get_dependency_graph, analyze_upgrade_impact |
| 3 | `ANTIGRAVITY_OBSERVABILITY_AGENT` | get_service_metrics, query_logs, get_trace, create_alert_rule |
| 4 | `ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT` | get_orchestration_status, route_agent_call, trip_circuit_breaker, list_active_workflows |
| 5 | `ANTIGRAVITY_PLATFORM_EVALUATION_AGENT` | evaluate_platform_health, check_release_readiness, get_sla_report, capacity_planning |
| 6 | `ANTIGRAVITY_TECH_DEBT_AGENT` | scan_tech_debt, get_debt_backlog, track_debt_trend |
| 7 | `ANTIGRAVITY_TRUTH_ENGINE_AGENT` | resolve_data_conflict, get_data_lineage, verify_audit_trail, assert_truth |
| 8 | `ECOSKILLER_ANTIGRAVITY_API_AGENT` | list_endpoints, provision_api_key, deprecate_version, generate_openapi_spec |
| 9 | `ECOSKILLER_ANTIGRAVITY_SCHEMA_COMPATIBILITY_AGENT` | check_compatibility, register_schema, validate_migration, list_subjects |
| 10 | `ECOSKILLER_ANTIGRAVITY_SYSTEM_SETUP_AGENT` | setup_new_tenant, run_preflight_check, init_seed_data, exchange_service_credentials |
| 11 | `ECOSKILLER_ANTIGRAVITY_UI_AGENT` | toggle_feature_flag, set_theme, configure_navigation, list_component_versions |

**Total tools: 40**

---

## Tech Stack

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring AI 1.0.0** (MCP Server)
- **Transport:** HTTP + SSE (ASYNC mode)
- **Port:** `8081`

---

## Project Structure

```
mcp-01-platform/
├── pom.xml
└── src/main/
    ├── java/com/ecoskiller/mcp/platform/
    │   ├── McpPlatformApplication.java          ← Main entry point
    │   ├── config/
    │   │   └── McpServerConfig.java             ← Registers all 11 agents as MCP tools
    │   └── agents/
    │       ├── AntigravityConfigurationAgent.java
    │       ├── AntigravityDependencyRiskAgent.java
    │       ├── AntigravityObservabilityAgent.java
    │       ├── AntigravityOrchestrationGovernorAgent.java
    │       ├── AntigravityPlatformEvaluationAgent.java
    │       ├── AntigravityTechDebtAgent.java
    │       ├── AntigravityTruthEngineAgent.java
    │       ├── EcoskillerAntigravityApiAgent.java
    │       ├── EcoskillerAntigravitySchemaCompatibilityAgent.java
    │       ├── EcoskillerAntigravitySystemSetupAgent.java
    │       └── EcoskillerAntigravityUiAgent.java
    └── resources/
        └── application.yml
```

---

## Build & Run

```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/mcp-01-platform-1.0.0.jar

# Or with Maven
mvn spring-boot:run
```

---

## MCP Endpoints (SSE Transport)

| Endpoint | Description |
|----------|-------------|
| `GET  http://localhost:8081/sse` | SSE connection endpoint for MCP clients |
| `POST http://localhost:8081/mcp/message` | MCP message endpoint |
| `GET  http://localhost:8081/actuator/health` | Health check |

---

## MCP Client Configuration (Claude Desktop / Cursor)

```json
{
  "mcpServers": {
    "ecoskiller-platform": {
      "url": "http://localhost:8081/sse",
      "type": "sse"
    }
  }
}
```

---

## Adding a New Tool to an Agent

```java
@Tool(name = "antigravity_your_tool_name",
      description = "What this tool does — be descriptive for the LLM.")
public Map<String, Object> yourTool(
        @ToolParam(description = "What this param is") String param1) {
    Map<String, Object> result = new HashMap<>();
    result.put("agent", "AGENT_NAME");
    // ... your logic here
    return result;
}
```

---

## Next MCP Servers to Build

Following the Sprint plan from the Ecoskiller architecture:

| Sprint | Servers |
|--------|---------|
| Sprint 1 | `mcp-03-security`, `mcp-09-intelligence`, `mcp-18-core-control`, `mcp-24-scoring-billing` |
| Sprint 2 | `mcp-02-governance`, `mcp-04-ai-governance`, `mcp-14-ml-safety`, `mcp-16-dojo` |

---

*Ecoskiller Engineering | mcp-01-platform | Java Spring AI MCP Server*
