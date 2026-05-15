# flyway-service-mcp

**Ecoskiller Platform — Database Schema Version Control & Automated Migration Engine**  
MCP Server in **Java 17** | **18 Agents** | **Secure** | Priority: HIGH

PostgreSQL 15 | Helm Init Containers | GitLab CI/CD | 13-Service Schema Governance | DR Schema Consistency

---

## Agents (18)

| # | Tool Name | Purpose |
|---|-----------|---------|
| 1 | `migration_apply` | `flyway migrate` — applies all pending migrations in version order (Helm init container primary op) |
| 2 | `migration_status` | Current state of all migrations per schema (applied / pending / failed) |
| 3 | `migration_validate` | `flyway validate` — CRC32 checksum validation of applied migration files |
| 4 | `migration_repair` | `flyway repair` — removes failed migration entries from flyway_schema_history |
| 5 | `migration_baseline` | `flyway baseline` — marks existing schema as Flyway-managed (first-time adoption) |
| 6 | `migration_info` | `flyway info` — full migration info table (safe read-only ops tool) |
| 7 | `schema_drift_detector` | Detects schema drift across dev/test/stage/prod environments |
| 8 | `schema_history_query` | Direct flyway_schema_history table query with filters |
| 9 | `checksum_validator` | Validates CRC32 checksums proactively before deployment |
| 10 | `multi_schema_manager` | Manages all 15 Ecoskiller schemas simultaneously — version matrix, summary |
| 11 | `helm_init_container_status` | Reports Flyway init container status across all 13 service Helm releases |
| 12 | `kubernetes_secret_validator` | Validates K8s Secret config for Flyway credentials (secretKeyRef enforcement) |
| 13 | `gitlab_pipeline_trigger` | Reports on GitLab CI stages that trigger Flyway (build→scan→deploy pipeline) |
| 14 | `environment_parity_checker` | Verifies all 4 environments have identical migration sets applied |
| 15 | `dr_schema_consistency` | DR Runbook Steps 34 & 45 — schema consistency check post pg_dump restore |
| 16 | `dr_runbook_executor` | Full DR workflow: MinIO → pg_dump restore → Flyway migrate → services start |
| 17 | `rls_policy_tracker` | Tracks PostgreSQL RLS policies deployed as Flyway migration artefacts |
| 18 | `compliance_audit_reporter` | DPDPA 2023 / IT Act 2000 compliance reports from flyway_schema_history |

---

## Security Features

| Layer | Implementation |
|-------|----------------|
| **Tool Allowlist** | Explicit 18-tool permit list — all unlisted tools denied by default |
| **Input Sanitization** | SQL injection detection, path traversal prevention, length limits (2048 chars) |
| **Schema Allowlist** | Only known Ecoskiller schemas accepted (15 schemas). Unknown schemas rejected. |
| **Migration File Validation** | V{n}__{desc}.sql pattern enforced — rejects malformed filenames |
| **Environment Allowlist** | Only dev/test/stage/prod accepted |
| **Production Gates** | Extra confirmation required for dangerous operations on prod (e.g. baseline) |
| **HMAC-SHA256 Signing** | Payload signing/verification — key from `FLYWAY_MCP_HMAC_KEY` env var |
| **Rate Limiting** | 120 calls/minute per tool (in-memory, thread-safe) |
| **Audit Logging** | Every tool call logged to stderr with timestamp + arg keys |
| **Stdout Protection** | All logging → stderr only; stdout is exclusively the MCP JSON-RPC channel |

---

## 13-Service Schema Governance

All 15 schemas are Flyway-managed with **independent migration version sequences**:

| Schema | Service | Notes |
|--------|---------|-------|
| `core` | core-service | V1–V25+, RLS + pgcrypto, gd_topics_bank |
| `billing` | billing-service | 8-year retention (GST compliance) |
| `analytics` | analytics-service | Complements ClickHouse |
| `realtime` | realtime-service | RLS on all tables |
| `admin` | admin-service | Keycloak RBAC governed |
| `notification` | notification-service | Multi-channel delivery logs |
| `scoring` | scoring-engine | Model versioning tracked |
| `certification` | certification-engine | Digital certificate issuance |
| `interview` | interview-service | GDPR-aware retention |
| `job` | job-service + application-service | Core hiring marketplace |
| `phone_bridge` | gd-phone-bridge-service | Added v15 — full RLS on 3 tables |
| `royalty` | Royalty Engine | 15-year retention compliance |
| `innovation` | Idea Marketplace | WORM retention for signed contracts |
| `intelligence` | Intelligence Profile | Longitudinal candidate analytics |
| `legal` | Legal Document Service | Binding agreement metadata |

---

## DR Integration (Steps 34 & 45)

```bash
# DR Runbook Step 34: Post pg_dump restore — schema consistency confirmation
flyway -url=jdbc:postgresql://${PG_HOST}:5432/ecoskiller \
  -schemas=core,billing,analytics,realtime,admin,notification, \
           scoring,certification,interview,job,phone_bridge, \
           royalty,innovation,intelligence,legal \
  -user=${DB_USER} -password=${DB_PASSWORD} migrate

# If exit 0 → schema current ✅
# If exit 1 → checksum issue → escalate ❌

# Monthly DR drill target: pg_dump restore + Flyway migration < 30 minutes
```

---

## Requirements

- **Java 17+**
- **Maven 3.8+**
- Only external dependency: Gson (bundled in fat JAR)

---

## Build & Run

```bash
# Build fat JAR
mvn clean package -q

# Run MCP server (stdio transport)
FLYWAY_MCP_HMAC_KEY=your-256-bit-secret java -jar target/flyway-service-mcp-1.0.0.jar

# Tests (27 tests covering 18 agents + security)
mvn test
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "flyway-service-mcp": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/flyway-service-mcp-1.0.0.jar"],
      "env": {
        "FLYWAY_MCP_HMAC_KEY": "your-secret-key"
      }
    }
  }
}
```

---

## File Structure

```
flyway-service-mcp/
├── pom.xml                                             ← Maven (Java 17, Gson, fat JAR)
├── README.md
└── src/
    ├── main/java/com/ecoskiller/flyway/
    │   ├── FlywayMcpServer.java                        ← Entry point (JSON-RPC 2.0 stdio)
    │   ├── McpAgent.java                               ← Agent interface
    │   ├── security/
    │   │   └── SecurityManager.java                    ← 8-layer security (HMAC, allowlist, sanitize, rate limit)
    │   └── agents/
    │       ├── BaseAgent.java                          ← Shared DSL + response helpers
    │       ├── MigrationLifecycleAgents.java           ← Agents 1–6 (apply, status, validate, repair, baseline, info)
    │       ├── SchemaAndInfraAgents.java               ← Agents 7–12 (drift, history, checksum, multi-schema, helm, k8s)
    │       └── CicdDrComplianceAgents.java             ← Agents 13–18 (gitlab, parity, DR, RLS, compliance)
    └── test/java/com/ecoskiller/flyway/
        └── FlywayMcpServerTest.java                    ← 27 tests
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Environment Variables

| Variable | Required | Description |
|----------|----------|-------------|
| `FLYWAY_MCP_HMAC_KEY` | Recommended | HMAC-SHA256 signing key. Defaults to insecure dev key if not set. |

---

## Flyway Commands Covered

| Command | Agent | Context |
|---------|-------|---------|
| `flyway migrate` | `migration_apply` | Helm init container, DR steps 34 & 45 |
| `flyway info` | `migration_info`, `migration_status` | Ops verification, DR post-restore |
| `flyway validate` | `migration_validate` | CI pre-deploy checksum validation |
| `flyway repair` | `migration_repair` | Post-failure recovery |
| `flyway baseline` | `migration_baseline` | First-time schema adoption (checklist item 57) |
