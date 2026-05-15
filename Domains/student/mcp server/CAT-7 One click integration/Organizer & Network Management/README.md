# mcp-26-organizer

**Ecoskiller | CAT-26 — Organizer & Network Management**  
MCP Server in Java | 29 Agents | Priority: HIGH

---

## Agents (29)

| # | Tool Name | Agent |
|---|-----------|-------|
| 1 | `user_registration` | USER_REGISTRATION_AGENT |
| 2 | `kyc_verification` | KYC_VERIFICATION_AGENT |
| 3 | `coordinator_onboarding` | COORDINATOR_ONBOARDING_AGENT |
| 4 | `master_organizer_onboarding` | MASTER_ORGANIZER_ONBOARDING_AGENT |
| 5 | `rural_block_onboarding` | RURAL_BLOCK_ONBOARDING_AGENT |
| 6 | `role_assignment` | ROLE_ASSIGNMENT_AGENT |
| 7 | `household_id_linking` | HOUSEHOLD_ID_LINKING_AGENT |
| 8 | `society_mapping` | SOCIETY_MAPPING_AGENT |
| 9 | `resource_allocation` | RESOURCE_ALLOCATION_AGENT |
| 10 | `tournament_management` | TOURNAMENT_MANAGEMENT_AGENT |
| 11 | `event_calendar_sync` | EVENT_CALENDAR_SYNC_AGENT |
| 12 | `exposition_management` | EXPOSITION_MANAGEMENT_AGENT |
| 13 | `workshop_creation` | WORKSHOP_CREATION_AGENT |
| 14 | `workshop_enrollment` | WORKSHOP_ENROLLMENT_AGENT |
| 15 | `workshop_attendance_tracking` | WORKSHOP_ATTENDANCE_TRACKING_AGENT |
| 16 | `trainer_assignment` | TRAINER_ASSIGNMENT_AGENT |
| 17 | `skill_category_configuration` | SKILL_CATEGORY_CONFIGURATION_AGENT |
| 18 | `digital_badge_issuance` | DIGITAL_BADGE_ISSUANCE_AGENT |
| 19 | `certificate_generation` | CERTIFICATE_GENERATION_AGENT |
| 20 | `commission_distribution_engine` | COMMISSION_DISTRIBUTION_ENGINE_AGENT |
| 21 | `payment_gateway_integration` | PAYMENT_GATEWAY_INTEGRATION_AGENT |
| 22 | `revenue_split_automation` | REVENUE_SPLIT_AUTOMATION_AGENT |
| 23 | `cash_flow_stability` | CASH_FLOW_STABILITY_AGENT |
| 24 | `incentive_bonus_calculation` | INCENTIVE_BONUS_CALCULATION_AGENT |
| 25 | `wallet_balance_tracking` | WALLET_BALANCE_TRACKING_AGENT |
| 26 | `refund_processing` | REFUND_PROCESSING_AGENT |
| 27 | `performance_scoring` | PERFORMANCE_SCORING_AGENT |
| 28 | `longitudinal_impact_analytics` | LONGITUDINAL_IMPACT_ANALYTICS_AGENT |

> **Note:** Agent 1 (`USER_REGISTRATION_AGENT`) is listed as #2 in the original spec but is the first
> registered tool. All 29 unique agents from the spec are implemented.

---

## Requirements

| Requirement | Version |
|-------------|---------|
| Java | **17+** |
| Maven | 3.8+ |
| Jackson Databind | 2.16.1 (bundled via shade plugin) |

---

## Build

```bash
# From project root
mvn clean package -q

# Output: target/mcp-26-organizer-all.jar  (fat/uber JAR, ~3 MB)
```

---

## Run the server

```bash
java -jar target/mcp-26-organizer-all.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Run tests

```bash
mvn test
```

28 parameterised JUnit 5 tests — one per agent.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-26-organizer": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-26-organizer/target/mcp-26-organizer-all.jar"]
    }
  }
}
```

Replace `/absolute/path/to/` with your actual project path.

---

## File Structure

```
mcp-26-organizer/
├── pom.xml                                          ← Maven build (Java 17, shade plugin)
├── claude_desktop_config.json                       ← Claude Desktop config snippet
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp26/
    │   ├── McpServer.java                           ← JSON-RPC 2.0 stdio server (main)
    │   ├── AgentRegistry.java                       ← All 29 agents registered here
    │   └── ToolDefinition.java                      ← Tool metadata model
    └── test/java/com/ecoskiller/mcp26/
        └── AgentRegistryTest.java                   ← JUnit 5 parameterised tests (28 cases)
```

---

## Protocol

| Property | Value |
|----------|-------|
| Transport | **stdio** (stdin/stdout) |
| Format | **JSON-RPC 2.0** |
| MCP Version | **2024-11-05** |
| Methods | `initialize`, `tools/list`, `tools/call`, `ping` |

---

## Manual test (quick smoke test)

```bash
# Build first
mvn clean package -q

# Pipe a tools/list request
echo '{"jsonrpc":"2.0","id":1,"method":"tools/list","params":{}}' \
  | java -jar target/mcp-26-organizer-all.jar \
  | python3 -m json.tool | head -30
```

Expected: JSON with `"tools"` array listing all 29 tools.
