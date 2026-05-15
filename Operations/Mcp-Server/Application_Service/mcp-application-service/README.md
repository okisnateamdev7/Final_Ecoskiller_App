# mcp-application-service

**Ecoskiller | CAT-APP — Hiring & Talent Assessment**
MCP Server in Java | 16 Tools | Priority: HIGH | Namespace: core

---

## Tools (16)

| # | Tool Name | Category | Roles |
|---|-----------|----------|-------|
| 1 | `submit_application` | Core CRUD | CANDIDATE, ADMIN |
| 2 | `get_application` | Core CRUD | CANDIDATE (own), RECRUITER (own jobs), ADMIN |
| 3 | `list_applications` | Core CRUD | RECRUITER (own), ADMIN |
| 4 | `update_application_status` | State Machine | RECRUITER, ADMIN |
| 5 | `withdraw_application` | Core CRUD | CANDIDATE (own), ADMIN |
| 6 | `validate_application_eligibility` | Validation | ALL |
| 7 | `check_duplicate_application` | Validation | ALL |
| 8 | `process_score_event` | Scoring | ADMIN (service account) |
| 9 | `evaluate_belt_eligibility` | Scoring | ALL |
| 10 | `get_application_audit_log` | Audit | CANDIDATE (own), RECRUITER (own), ADMIN |
| 11 | `export_compliance_report` | Compliance | ADMIN |
| 12 | `get_application_statistics` | Analytics | RECRUITER, ADMIN |
| 13 | `search_applications` | Analytics | ALL (scoped) |
| 14 | `get_recruiter_pipeline` | Recruiter | RECRUITER (own), ADMIN |
| 15 | `bulk_update_status` | Recruiter | RECRUITER, ADMIN |
| 16 | `get_assessment_pipeline_status` | Pipeline | CANDIDATE (own), RECRUITER, ADMIN |

---

## Application State Machine

```
APPLIED ──► SCREENED ──► ASSESSED ──┬──► HIRED
   │            │                   └──► REJECTED
   └────────────┴──────────────────────► WITHDRAWN
```

Invalid transitions are rejected with HTTP 409-equivalent errors:
- `REJECTED → HIRED` — blocked
- `HIRED → anything` — blocked
- `WITHDRAWN → anything` — blocked

---

## Security Architecture

| Layer | Mechanism |
|-------|-----------|
| Authentication | Keycloak JWT (Bearer token on every call) |
| Authorisation | RBAC: CANDIDATE / RECRUITER / ADMIN roles |
| Tenant Isolation | `tenant_id` enforced on every query (mirrors PostgreSQL RLS) |
| Input Sanitisation | SQL injection + XSS pattern detection |
| Idempotency | Per-operation key prevents duplicate processing |
| Audit Trail | Immutable log on every state change (ClickHouse in prod) |
| Compliance | DPDPA 2023 / GDPR / GST export support |

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `APP_JWT_SECRET` | `ecoskiller-dev-secret-change-in-prod` | HMAC-HS256 signing secret |
| `APP_STRICT_MODE` | `false` | `true` = enforce signature verification |
| `APP_MAX_PAYLOAD_SIZE` | `65535` | Max input string length |

---

## Requirements

- **Java 11+** (no external dependencies — pure JDK)
- Maven 3.x (for building; optional if using the pre-built JAR)

---

## Build

```bash
# Compile
javac -d target/classes $(find src -name "*.java")

# Package
jar cfe target/mcp-application-service.jar \
    com.ecoskiller.mcp.ApplicationMCPServer \
    -C target/classes .

# Or with Maven
mvn package -q
```

---

## Run the server

```bash
java -jar target/mcp-application-service.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).
All internal logs go to **stderr**; stdout is reserved for MCP messages only.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-application-service": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/target/mcp-application-service.jar"],
      "env": {
        "APP_JWT_SECRET": "your-keycloak-realm-secret",
        "APP_STRICT_MODE": "false"
      }
    }
  }
}
```

---

## Run tests

```bash
# Quick pass/fail
java -cp target/mcp-application-service.jar com.ecoskiller.mcp.TestAgents

# With full JSON output
java -cp target/mcp-application-service.jar com.ecoskiller.mcp.TestAgents --verbose
```

Expected: **22/22 PASSED** (16 tool tests + 6 security/edge-case tests)

---

## File Structure

```
mcp-application-service/
├── pom.xml
├── README.md
├── claude_desktop_config.json
└── src/main/java/com/ecoskiller/mcp/
    ├── ApplicationMCPServer.java        ← Entry point (stdio loop)
    ├── TestAgents.java                  ← 22-test suite
    ├── protocol/
    │   ├── MCPProtocolHandler.java      ← JSON-RPC 2.0 dispatcher
    │   └── JsonUtil.java                ← Zero-dependency JSON parser
    ├── security/
    │   ├── SecurityConfig.java          ← JWT, RBAC, sanitisation, idempotency
    │   └── JwtClaims.java               ← JWT claims value object
    ├── models/
    │   ├── Application.java             ← Domain model + state machine
    │   ├── ApplicationRepository.java   ← Thread-safe in-memory store (→ PostgreSQL)
    │   └── KafkaEventPublisher.java     ← Kafka event stub (→ real producer)
    └── tools/
        ├── ToolRouter.java              ← Central dispatcher + tools/list definition
        ├── ApplicationCrudTools.java    ← submit, get, list, update, withdraw, validate
        ├── ScoringTools.java            ← process_score_event, evaluate_belt_eligibility
        ├── AuditTools.java              ← audit_log, compliance_report
        ├── AnalyticsTools.java          ← statistics, search
        └── RecruiterTools.java          ← pipeline, bulk_update, assessment_status
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Production Migration Checklist

| Component | Dev (current) | Production |
|-----------|---------------|------------|
| JWT verification | HS256 dev-sig passthrough | RS256 + Keycloak public key fetch |
| Data store | In-memory `ConcurrentHashMap` | PostgreSQL 15 + RLS policies |
| Audit log | In-memory list | ClickHouse append-only table |
| Kafka events | Logger stubs | `KafkaProducer` → Apicurio Schema Registry |
| Idempotency | In-memory `LinkedHashSet` | Redis `SET NX EX` |
| Search | Linear scan | OpenSearch via search-indexer-service |
| Secrets | Env vars | Kubernetes Secrets (core namespace) |
