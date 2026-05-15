# mcp-07-integrations
### Ecoskiller · CAT-07 — One-Click Integrations · MCP Server (Java)

---

## Overview

Java-based MCP (Model Context Protocol) server for **CAT-07: One-Click Integrations**.  
Exposes **18 agent tools** over standard MCP JSON-RPC 2.0, enabling LLM clients (Claude, GPT-4, etc.)
to trigger real integrations from a single tool call.

| | |
|---|---|
| **MCP Protocol** | 2024-11-05 |
| **Transport** | HTTP POST + SSE |
| **Port** | 8707 |
| **Java** | 17 |
| **Framework** | Spring Boot 3.2 |
| **Agents** | 18 |

---

## 18 Agents (Tools)

| # | Tool Name | Agent |
|---|-----------|-------|
| 1 | `sso_integrate` | SSO_INTEGRATION_AGENT |
| 2 | `calendar_sync` | CALENDAR_SYNC_AGENT |
| 3 | `analytics_connect` | ANALYTICS_CONNECT_AGENT |
| 4 | `automation_connect` | AUTOMATION_CONNECT_AGENT |
| 5 | `data_warehouse_connect` | DATA_WAREHOUSE_AGENT |
| 6 | `deployment_checklist` | DEPLOYMENT_CHECKLIST_AGENT |
| 7 | `digilocker_connect` | DIGILOCKER_AGENT |
| 8 | `digilocker_quick_reference` | DIGILOCKER_QUICK_REFERENCE_AGENT |
| 9 | `digilocker_technical_spec` | DIGILOCKER_TECHNICAL_SPEC_AGENT |
| 10 | `esign_connect` | ESIGN_CONNECT_AGENT |
| 11 | `implementation_guide` | IMPLEMENTATION_GUIDE_AGENT |
| 12 | `llm_connect` | LLM_CONNECT_AGENT |
| 13 | `marketing_connect` | MARKETING_CONNECT_AGENT |
| 14 | `payment_connect` | PAYMENT_CONNECT_AGENT |
| 15 | `prompt_integrity_verify` | PROMPT_INTEGRITY_VERIFIER_AGENT |
| 16 | `cat07_readme` | README_AGENT |
| 17 | `video_connect` | VIDEO_CONNECT_AGENT |
| 18 | `whatsapp_connect` | WHATSAPP_CONNECT_AGENT |

---

## Quick Start

```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/mcp-07-integrations-1.0.0.jar

# Or with Maven
mvn spring-boot:run
```

Server starts at: `http://localhost:8707`

---

## MCP Protocol Usage

### 1. Initialize
```json
POST /mcp
{
  "jsonrpc": "2.0", "id": "1", "method": "initialize",
  "params": { "protocolVersion": "2024-11-05",
               "clientInfo": { "name": "my-client", "version": "1.0" } }
}
```

### 2. List Tools
```json
POST /mcp
{ "jsonrpc": "2.0", "id": "2", "method": "tools/list" }
```

### 3. Call a Tool
```json
POST /mcp
{
  "jsonrpc": "2.0", "id": "3", "method": "tools/call",
  "params": {
    "name": "payment_connect",
    "arguments": {
      "action": "create_order",
      "tenant_id": "school_001",
      "gateway": "razorpay",
      "amount_paise": "50000",
      "currency": "INR"
    }
  }
}
```

### 4. Health Check
```
GET /mcp/health
```

---

## Architecture

```
McpIntegrationsServerApplication (Spring Boot)
│
├── McpServerController  (POST /mcp, GET /mcp/sse)
│   └── dispatches JSON-RPC methods
│
├── ToolRegistry
│   └── auto-discovers all @Component AgentTool beans
│
└── 18 Agent Tools (all implement AgentTool via BaseAgentTool)
    ├── SsoIntegrationAgent
    ├── CalendarSyncAgent
    ├── AnalyticsConnectAgent
    ├── AutomationConnectAgent
    ├── DataWarehouseAgent
    ├── DeploymentChecklistAgent
    ├── DigiLockerAgent
    ├── DigiLockerQuickReferenceAgent
    ├── DigiLockerTechnicalSpecAgent
    ├── ESignConnectAgent
    ├── ImplementationGuideAgent
    ├── LlmConnectAgent
    ├── MarketingConnectAgent
    ├── PaymentConnectAgent
    ├── PromptIntegrityVerifierAgent
    ├── ReadmeAgent
    ├── VideoConnectAgent
    └── WhatsAppConnectAgent
```

---

## Adding a New Agent

1. Create a class in `agents/` extending `BaseAgentTool`
2. Annotate with `@Component`
3. Implement `getName()`, `getDescription()`, `getInputSchema()`, `doExecute()`
4. ToolRegistry auto-registers it — no wiring needed

---

*Ecoskiller Engineering · mcp-07-integrations v1.0.0*
