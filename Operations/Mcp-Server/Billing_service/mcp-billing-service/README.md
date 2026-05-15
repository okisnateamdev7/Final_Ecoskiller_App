# mcp-billing-service

**EcoSkiller | Billing Service MCP Server**  
Java 21 | 18 Tools | MCP 2024-11-05 | Secure | stdio transport

---

## Overview

This is the Model Context Protocol (MCP) server for the **EcoSkiller Billing Service** — the backend financial management microservice responsible for:

- Subscription plan management (free / professional / enterprise)
- Usage metering (GD sessions, interviews, dojo matches, API calls)
- GST-compliant invoice generation and PDF storage in MinIO
- Payment lifecycle (initiate, confirm, refund) via Kill Bill + gateway
- Feature entitlement enforcement (plan-based gate middleware)
- Immutable financial audit logs (ClickHouse 3yr + PostgreSQL ledger 8yr)

Deployed in the **`billing`** Kubernetes namespace. Only the `core` namespace may call it (enforced by NetworkPolicy).

---

## Tools (18)

| # | Tool Name | Description |
|---|-----------|-------------|
| 1 | `billing_list_plans` | List all subscription plans with entitlements |
| 2 | `billing_create_subscription` | Create subscription for a tenant |
| 3 | `billing_get_subscription` | Get subscription details & billing period |
| 4 | `billing_upgrade_subscription` | Upgrade or downgrade plan with pro-rata credit |
| 5 | `billing_cancel_subscription` | Cancel at period end or immediately |
| 6 | `billing_check_entitlement` | Feature gate check — allowed / denied + quota |
| 7 | `billing_record_usage` | Ingest a billable usage event (idempotent) |
| 8 | `billing_get_usage` | Current period usage breakdown + history |
| 9 | `billing_generate_invoice` | Generate GST invoice, store in MinIO |
| 10 | `billing_list_invoices` | Paginated invoice list with status filter |
| 11 | `billing_get_invoice` | Full invoice detail with GST breakdown |
| 12 | `billing_get_invoice_download_url` | MinIO 24h pre-signed PDF URL |
| 13 | `billing_initiate_payment` | Create payment intent, returns gateway URL |
| 14 | `billing_confirm_payment` | Confirm payment from webhook (HMAC verified) |
| 15 | `billing_refund_payment` | Full or partial refund with audit trail |
| 16 | `billing_get_payment_status` | Payment status + ledger references |
| 17 | `billing_get_audit_log` | Immutable financial audit trail |
| 18 | `billing_health` | Liveness / readiness + dependency checks |

---

## Security Design

| Layer | Implementation |
|-------|---------------|
| Input validation | `RequestValidator` — every field validated before business logic |
| UUID format | Strict RFC-4122 pattern enforcement |
| Monetary amounts | Must be positive, max 12 digits, ≤ 2 decimal places |
| String fields | Length-bounded, allow-list character set (no SQL injection) |
| Enum fields | Allow-listed values only (plan_id, currency, status, etc.) |
| Tenant isolation | `tenant_id` from JWT claim — never trusted from request body |
| Audit trail | Every financial mutation writes an immutable audit record |
| Webhook signatures | HMAC-SHA256 verification before payment confirmation |
| Sensitive logging | Exception messages sanitised before any output |
| Container | Distroless JRE, `runAsNonRoot: true`, uid 65532 |
| Secrets | Kubernetes Secrets via RBAC — never in env vars in production |
| Network | NetworkPolicy: billing namespace, ingress from core only |
| mTLS | cert-manager + K8s TLS secrets (Phase 1 infrastructure) |

---

## Requirements

- Java 21+
- Maven 3.9+ (build only)
- No external runtime dependencies (single fat JAR)

---

## Build

```bash
mvn clean package -DskipTests
```

Fat JAR output: `target/billing-mcp-server-1.0.0.jar`

---

## Run Tests

```bash
# All tests (37 assertions across 18 tools)
mvn test

# Specific test class
mvn test -Dtest=BillingToolsTest
mvn test -Dtest=McpDispatchTest
```

---

## Run the Server

```bash
java -jar target/billing-mcp-server-1.0.0.jar
```

The server listens on **stdin** and writes to **stdout** using JSON-RPC 2.0.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "ecoskiller-billing": {
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/to/mcp-billing-service/target/billing-mcp-server-1.0.0.jar"
      ]
    }
  }
}
```

---

## Example JSON-RPC Requests

### initialize
```json
{"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"2024-11-05","clientInfo":{"name":"test"}}}
```

### tools/list
```json
{"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}
```

### billing_list_plans
```json
{"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"billing_list_plans","arguments":{}}}
```

### billing_create_subscription
```json
{
  "jsonrpc": "2.0", "id": 4,
  "method": "tools/call",
  "params": {
    "name": "billing_create_subscription",
    "arguments": {
      "tenant_id":       "550e8400-e29b-41d4-a716-446655440000",
      "plan_id":         "professional",
      "billing_email":   "billing@corp.example.com",
      "gst_number":      "27AAPFU0939F1ZV",
      "idempotency_key": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
    }
  }
}
```

### billing_check_entitlement
```json
{
  "jsonrpc": "2.0", "id": 5,
  "method": "tools/call",
  "params": {
    "name": "billing_check_entitlement",
    "arguments": {
      "tenant_id": "550e8400-e29b-41d4-a716-446655440000",
      "feature":   "ai_scoring"
    }
  }
}
```

### billing_generate_invoice
```json
{
  "jsonrpc": "2.0", "id": 6,
  "method": "tools/call",
  "params": {
    "name": "billing_generate_invoice",
    "arguments": {
      "tenant_id":       "550e8400-e29b-41d4-a716-446655440000",
      "subscription_id": "7f3e9012-b3c4-5678-d901-234567890abc",
      "period_start":    "2025-01-01T00:00:00Z",
      "period_end":      "2025-01-31T23:59:59Z",
      "idempotency_key": "c3d4e5f6-a7b8-9012-cdef-345678901abc"
    }
  }
}
```

---

## Kafka Events Emitted

| Tool | Kafka Event |
|------|-------------|
| `billing_create_subscription` | `subscription.created` |
| `billing_upgrade_subscription` | `subscription.updated` |
| `billing_cancel_subscription` | `subscription.cancelled` |
| `billing_generate_invoice` | `invoice.generated` |
| `billing_confirm_payment` | `payment.confirmed`, `invoice.paid`, `subscription.renewed` |
| `billing_refund_payment` | `payment.refunded` |

All topics use replication factor 3 in production with dead-letter queues (`.dlq`).

---

## File Structure

```
mcp-billing-service/
├── src/
│   ├── main/java/com/ecoskiller/billing/
│   │   ├── server/
│   │   │   ├── BillingMcpServer.java        ← Main entry point (stdio JSON-RPC)
│   │   │   ├── ToolNotFoundException.java
│   │   │   └── ValidationException.java
│   │   ├── security/
│   │   │   └── RequestValidator.java        ← Security-first input validation
│   │   └── tools/
│   │       ├── BillingTool.java             ← Tool interface
│   │       ├── AbstractBillingTool.java     ← Shared helpers
│   │       ├── ToolRegistry.java            ← Tool registration + dispatch
│   │       ├── SubscriptionListPlansTool.java
│   │       ├── SubscriptionCreateTool.java
│   │       ├── SubscriptionGetTool.java
│   │       ├── SubscriptionUpgradeTool.java
│   │       ├── SubscriptionCancelTool.java
│   │       ├── SubscriptionCheckEntitlementTool.java
│   │       ├── UsageRecordTool.java
│   │       ├── UsageGetTool.java
│   │       ├── InvoiceGenerateTool.java
│   │       ├── InvoiceListTool.java
│   │       ├── InvoiceGetTool.java
│   │       ├── InvoiceDownloadUrlTool.java
│   │       ├── PaymentInitiateTool.java
│   │       ├── PaymentConfirmTool.java
│   │       ├── PaymentRefundTool.java
│   │       ├── PaymentGetStatusTool.java
│   │       ├── AuditLogTool.java
│   │       └── BillingHealthTool.java
│   └── test/java/com/ecoskiller/billing/
│       ├── BillingToolsTest.java            ← 37 unit tests across all 18 tools
│       └── McpDispatchTest.java             ← JSON-RPC dispatch integration tests
├── pom.xml                                  ← Maven build (Java 21, fat JAR)
├── Dockerfile                               ← Multi-stage build, distroless runtime
├── .gitlab-ci.yml                           ← 5-stage CI/CD pipeline
├── k8s-deployment.yaml                      ← Deployment + Service + NetworkPolicy + HPA
├── claude_desktop_config.json              ← Claude Desktop config snippet
└── README.md
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Compliance Notes

- **GST**: Invoices include CGST + SGST at 18%. `gst_number` field stored per tenant.
- **DPDPA 2023**: No PII in audit logs beyond tenant_id. Billing data retained per contract.
- **PCI-DSS**: Card data NEVER stored. Kill Bill + gateway handles all card processing.
- **Financial Ledger**: Audit records are immutable (append-only). Correction entries reference the original. Retention: 8 years (GST legal requirement).
