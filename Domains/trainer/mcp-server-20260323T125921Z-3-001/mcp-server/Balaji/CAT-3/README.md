# MCP-03 Security Server
## Ecoskiller — CAT-03: Security, Tenancy & Migration Control

**Priority:** CRITICAL | **Namespace:** `core` | **Port:** `8003`

---

## Agents (12)

| # | Agent | Tools | Purpose |
|---|-------|-------|---------|
| 1 | `API_ECONOMY_AGENT` | 3 | API billing plans, quota enforcement, usage stats |
| 2 | `AUTH_MIGRATION_AGENT` | 3 | OAuth2/SAML/LDAP migrations, rollback |
| 3 | `CONSENT_MIGRATION_AGENT` | 3 | GDPR/DPDP consent records, audit trails |
| 4 | `ENTERPRISE_INTEGRATION_AGENT` | 3 | SAP/Oracle/Workday connectors, data sync |
| 5 | `ENV_PROMOTION_AGENT` | 3 | DEV→STAGING→PROD promotions, config diffs |
| 6 | `I18N_AGENT` | 3 | Locale config, translation migration, RTL |
| 7 | `KEY_MANAGEMENT_AGENT` | 4 | Key generation, rotation, revocation, KMS |
| 8 | `REGION_MIGRATION_AGENT` | 3 | Cross-region tenant data migration |
| 9 | `STORAGE_CONNECT_AGENT` | 3 | S3/GCS/Azure Blob/MinIO connections |
| 10 | `TENANT_CLONE_AGENT` | 3 | Tenant cloning for franchise/DR/staging |
| 11 | `WEBHOOK_MARKETPLACE_AGENT` | 3 | Webhook registry, delivery, HMAC signing |
| 12 | `ZERO_DOWNTIME_MIGRATION_AGENT` | 4 | Blue-green/canary/shadow migrations |

**Total tools: 38**

---

## Running

```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/mcp-03-security-1.0.0.jar

# Or with Maven
mvn spring-boot:run
```

---

## MCP Protocol Usage

### 1. Initialize (handshake)
```bash
curl -X POST http://localhost:8003/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": "1",
    "method": "initialize",
    "params": { "protocolVersion": "2024-11-05" }
  }'
```

### 2. List all tools
```bash
curl -X POST http://localhost:8003/mcp \
  -H "Content-Type: application/json" \
  -d '{ "jsonrpc": "2.0", "id": "2", "method": "tools/list", "params": {} }'
```

### 3. Call a tool
```bash
curl -X POST http://localhost:8003/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": "3",
    "method": "tools/call",
    "params": {
      "name": "zdm_plan",
      "arguments": {
        "tenant_id": "tenant-abc",
        "migration_type": "DB_SCHEMA",
        "strategy": "BLUE_GREEN"
      }
    }
  }'
```

### 4. Generate a key
```bash
curl -X POST http://localhost:8003/mcp \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": "4",
    "method": "tools/call",
    "params": {
      "name": "key_generate",
      "arguments": {
        "tenant_id": "tenant-abc",
        "key_type": "RSA-4096",
        "purpose": "signing",
        "expiry_days": 365
      }
    }
  }'
```

---

## Health & Info

```bash
GET http://localhost:8003/mcp/health
GET http://localhost:8003/mcp/info
GET http://localhost:8003/actuator/health
```

---

## Tech Stack

- Java 21
- Spring Boot 3.2
- JSON-RPC 2.0 (MCP Protocol 2024-11-05)
- Maven

---

*Ecoskiller Engineering | Sprint 1 — mcp-03-security*
