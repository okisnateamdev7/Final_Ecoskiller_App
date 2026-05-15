# mcp-licensing-contract-java

**Ecoskiller | CAT-17 — Marketplace, Royalty & Compliance**  
MCP Server in **Java** | **10 Tools** | Module XIII — Royalty & Licensing  
Transport: `stdio` | Protocol: JSON-RPC 2.0 | MCP Version: `2024-11-05`

---

## Overview

This is the Java MCP server for `licensing-contract-service` — the legally authoritative contract lifecycle engine of Ecoskiller's Module XIII Royalty & Licensing system.

It creates, versions, governs, and archives legally binding IP licensing agreements between candidates and businesses, enforces minor candidate protections, coordinates multi-party digital signatures, and ensures GST and IT Act 2000 compliance.

---

## Tools (10)

| # | Tool Name | Description | Required Role |
|---|-----------|-------------|---------------|
| 1 | `contract_lifecycle` | Create / get / advance-state contracts through the state machine | `ecoskiller:licensing:create` / `admin` |
| 2 | `contract_amendment` | Submit amendments, retrieve version history | `ecoskiller:licensing:create` |
| 3 | `contract_termination` | Initiate / check termination with compliance gate | `ecoskiller:licensing:admin` |
| 4 | `signature_orchestration` | Request, track, record, and timeout digital signatures | `ecoskiller:licensing:create` |
| 5 | `royalty_rate_validation` | Validate rates (0.01–0.05%) and terms (10–15 yr) per tenant | Any authenticated |
| 6 | `minor_candidate` | Minor status check, guardian consent, age-18 ownership transfer | `ecoskiller:licensing:create` |
| 7 | `contract_search` | Paginated, filtered contract search (metadata only) | `ecoskiller:viewer` |
| 8 | `contract_versions` | Full immutable version history from royalty.contract_versions | `ecoskiller:viewer` |
| 9 | `compliance_audit` | Log / query ClickHouse royalty_audit_log; trigger WORM archive | `ecoskiller:licensing:admin` |
| 10 | `kafka_events` | Publish events, inspect DLQ, list all Kafka topics | `ecoskiller:licensing:admin` |

---

## Contract State Machine

```
DRAFT
  └─► PENDING_REVIEW
        ├─► DRAFT (rejected / amendment required)
        └─► PENDING_SIGNATURE
              ├─► PENDING_REVIEW (timeout or partial reject)
              └─► ACTIVE ──────────────────┐
                    ├─► SUSPENDED          │
                    │     ├─► ACTIVE       │
                    │     └─► TERMINATED ◄─┘
                    └─► TERMINATED (terminal)
```

- All transitions are **atomic PostgreSQL transactions** with advisory locks
- Every transition writes an **immutable version row** to `royalty.contract_versions`
- Every transition is **audit-logged** to ClickHouse `royalty_audit_log`
- `ACTIVE` state triggers `licensing.contract.signed` Kafka event + WORM archive

---

## Security Architecture

### 1. JWT Validation (Keycloak)
- Every tool call requires a `jwt_token` argument (Keycloak Bearer JWT)
- Validates: structure (3-part), `exp` claim (not expired), `nbf` claim, issuer (`KEYCLOAK_ISSUER_URL`)
- In `SIMULATION_MODE=true` (default for dev): accepts any structurally valid JWT
- In production (`SIMULATION_MODE=false`): full issuer validation enforced

### 2. RBAC Role Enforcement
- Roles extracted from `realm_access.roles` claim (Keycloak format) or flat `roles` claim
- Three roles: `ecoskiller:licensing:create`, `ecoskiller:licensing:admin`, `ecoskiller:viewer`
- Terminal state transitions (→ TERMINATED) always require `:admin`

### 3. Input Sanitisation
- **SQL injection** patterns blocked (SELECT, INSERT, DROP, UNION, EXEC, etc.)
- **Path traversal** patterns blocked (`../`, `%2e%2e`, etc.)
- **XSS** patterns blocked (`<script`, `javascript:`, `onerror=`, etc.)
- **UUID validation** for all ID fields (strict regex)
- **GSTIN validation** for Indian GST Identification Numbers
- **Royalty rate guardrails**: rejects rates outside `[DEFAULT_MIN_ROYALTY_RATE, DEFAULT_MAX_ROYALTY_RATE]`
- **Term guardrails**: rejects terms outside `[DEFAULT_MIN_TERM_YEARS, DEFAULT_MAX_TERM_YEARS]`

### 4. Rate Limiting
- In-memory sliding window per actor (`sub` claim): max **60 requests/minute**
- Concurrent-safe using `ConcurrentHashMap` + `AtomicInteger`

### 5. Audit Logging
- All security events logged to `stderr` (never stdout — stdout is reserved for JSON-RPC)
- Production: all state events written to ClickHouse `royalty_audit_log` (immutable, append-only)

---

## Kafka Topics

### Published (8 topics)
| Topic | Consumers | Partitions |
|-------|-----------|------------|
| `licensing.contract.signed` | Royalty Accounting Engine, Royalty Wallet, analytics-service, notification-service | 6 |
| `licensing.contract.amended` | Royalty Accounting Engine, analytics-service, notification-service | 3 |
| `licensing.contract.terminated` | Royalty Wallet, Royalty Accounting Engine, analytics-service, notification-service | 3 |
| `licensing.contract.signature.timeout` | notification-service, admin-service | 3 |
| `licensing.ownership.transferred` | Innovation Trust Governance, notification-service, user-service | 3 |
| `document.generation.requested` | Legal Document Generation Service | 3 |
| `signature.requested` | Digital Signature Service | 3 |
| `archive.requested` | Immutable Archive Service | 3 |

### Consumed (5 topics)
| Topic | Producer |
|-------|----------|
| `idea.marketplace.license.intent` | Idea Marketplace Service (XII) |
| `document.generated` | Legal Document Generation Service |
| `document.signed` | Digital Signature Service |
| `innovation.trust.consent.approved` | Innovation Trust Governance Service |
| `licensing.ownership.transfer.due` | Apache Airflow (scheduled) |

**DLQ Policy:** Max 3 retries with exponential backoff → `*.dlq` topics  
**Consumer group naming:** `licensing-contract-service-{topic}-consumer`

---

## Build

### Requirements
- Java 17+
- Maven 3.6+

### Build fat-jar
```bash
mvn package -DskipTests
# Output: target/licensing-contract-mcp-1.0.0.jar
```

### Run directly
```bash
java -jar target/licensing-contract-mcp-1.0.0.jar
```

The server reads JSON-RPC 2.0 from **stdin** and writes responses to **stdout**.  
All logs go to **stderr**.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "ecoskiller-licensing-contract": {
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/to/target/licensing-contract-mcp-1.0.0.jar"
      ],
      "env": {
        "SIMULATION_MODE": "true",
        "KEYCLOAK_ISSUER_URL": "http://keycloak.ecoskiller.internal/realms/ecoskiller",
        "DEFAULT_MIN_ROYALTY_RATE": "0.0001",
        "DEFAULT_MAX_ROYALTY_RATE": "0.0005",
        "DEFAULT_MIN_TERM_YEARS": "10",
        "DEFAULT_MAX_TERM_YEARS": "15",
        "SIGNATURE_TTL_DAYS": "7"
      }
    }
  }
}
```

---

## Environment Variables

| Variable | Source | Description | Default |
|----------|--------|-------------|---------|
| `SIMULATION_MODE` | ConfigMap | `true` = dev mode (skip JWT issuer check) | `true` |
| `KEYCLOAK_ISSUER_URL` | ConfigMap | Keycloak realm URL | `http://keycloak.ecoskiller.internal/realms/ecoskiller` |
| `DEFAULT_MIN_ROYALTY_RATE` | ConfigMap | Platform minimum royalty rate | `0.0001` (0.01%) |
| `DEFAULT_MAX_ROYALTY_RATE` | ConfigMap | Platform maximum royalty rate | `0.0005` (0.05%) |
| `DEFAULT_MIN_TERM_YEARS` | ConfigMap | Minimum licensing term (years) | `10` |
| `DEFAULT_MAX_TERM_YEARS` | ConfigMap | Maximum licensing term (years) | `15` |
| `SIGNATURE_TTL_DAYS` | ConfigMap | Days before unsigned party times out | `7` |
| `MINOR_CACHE_TTL_SECONDS` | ConfigMap | Redis TTL for minor status cache | `3600` |
| `RATE_CONFIG_CACHE_TTL_SECONDS` | ConfigMap | Redis TTL for rate config cache | `600` |
| `KAFKA_BROKERS` | Kubernetes Secret | Kafka broker connection string | — |
| `POSTGRES_URI` | Kubernetes Secret | PostgreSQL connection (RLS-scoped) | — |
| `REDIS_URL` | Kubernetes Secret | Redis for locks + cache | — |
| `MINIO_ENDPOINT` | ConfigMap | MinIO internal endpoint | — |
| `CLICKHOUSE_URL` | ConfigMap | ClickHouse HTTP endpoint | — |
| `SCHEMA_REGISTRY_URL` | ConfigMap | Apicurio Schema Registry URL | — |

---

## Production Integration Points

In `SIMULATION_MODE=false`, each tool connects to:

| Component | Purpose |
|-----------|---------|
| **PostgreSQL 15** | `royalty.contracts`, `royalty.contract_versions`, `royalty.contract_signatures`, `royalty.platform_rate_config` — all RLS-scoped by `tenant_id` |
| **Redis 7.x** | Advisory locks (`contract_lock:{id}`, SET NX EX 30s), rate config cache, minor status cache |
| **Apache Kafka 3.7.0** | Event publishing + consumption for the full contract lifecycle pipeline |
| **MinIO** | `ecoskiller-royalty-contracts-{tenant_id}` bucket, GOVERNANCE-mode Object Lock, 15-year WORM retention, SSE-S3 encryption |
| **ClickHouse** | `royalty_audit_log` — immutable append-only audit trail |
| **Keycloak 24.0** | JWT validation (JWKS endpoint), RBAC role enforcement |
| **Apicurio Schema Registry** | Avro/JSON schema validation for all Kafka events |

---

## File Structure

```
mcp-licensing-contract-java/
├── pom.xml                                          ← Maven build (fat-jar via shade plugin)
├── README.md
├── claude_desktop_config.json                       ← Claude Desktop config snippet
├── test_mcp.sh                                      ← Quick functional test script
└── src/main/java/com/ecoskiller/mcp/licensing/
    ├── LicensingContractMCPServer.java              ← Main server: MCP dispatch, tools/list, initialize
    ├── security/
    │   └── SecurityManager.java                     ← JWT validation, RBAC, sanitisation, rate limiting
    ├── util/
    │   └── JsonRpcHandler.java                      ← JSON-RPC 2.0 envelope builder
    └── tools/
        ├── BaseTool.java                            ← Abstract base: shared helpers
        ├── ContractLifecycleTool.java               ← create | get | advance_state
        ├── ContractAmendmentTool.java               ← submit_amendment | get_amendments
        ├── ContractTerminationTool.java             ← initiate | check_status
        ├── SignatureOrchestrationTool.java          ← request_signatures | get_status | record_signed | handle_timeout
        ├── RoyaltyRateValidationTool.java           ← validate | get_config | set_config
        ├── MinorCandidateTool.java                  ← check_minor_status | record_guardian_consent | trigger_ownership_transfer
        ├── ContractSearchTool.java                  ← paginated filtered search
        ├── ContractVersionsTool.java                ← full version history
        ├── ComplianceAuditTool.java                 ← log_event | query_audit_trail | check_worm_archive | trigger_archive
        └── KafkaEventTool.java                      ← publish_event | inspect_dlq | list_topics
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Compliance

| Standard | How Enforced |
|----------|-------------|
| **India IT Act 2000** | Keycloak-verified digital signatures + 15-year MinIO WORM archive |
| **Indian Contract Act Section 11** | Minor candidate guardian consent gate + age-18 ownership transfer |
| **DPDPA 2023** | `consent_reference_id` linked to Innovation Trust Governance Service; PII anonymisation on erasure |
| **GST / SAC 9973** | `licensee_gstin`, `hsn_sac_code`, `royalty_nature` fields stored per contract for GST-compliant invoice generation |
| **Multi-Tenant Isolation** | PostgreSQL RLS on all `royalty.*` tables; Kafka events include `tenant_id`; MinIO bucket per tenant |
