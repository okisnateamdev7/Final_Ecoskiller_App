# job-service-mcp (Java)

**Ecoskiller | Job Service MCP Server**  
Java 17 | JSON-RPC 2.0 (stdio) | MCP 2024-11-05 | 16 Tools | Keycloak JWT Security

---

## Tools (16)

| # | Tool | Min Role | Description |
|---|------|----------|-------------|
| 1 | `job_create` | recruiter | Create a job in DRAFT state, publish `job.created` Kafka event |
| 2 | `job_get` | candidate | Get a job by ID with domain isolation |
| 3 | `job_update` | recruiter | Update job fields with optimistic concurrency (CAS) |
| 4 | `job_deactivate` | recruiter | Soft-deactivate (CLOSE) a job |
| 5 | `job_list` | candidate | List jobs with domain/status/pagination filters |
| 6 | `job_bulk_import` | recruiter | Atomic bulk import — all succeed or all rollback |
| 7 | `job_moderate` | **admin** | Approve or reject a job in MODERATION_REQUIRED state |
| 8 | `job_visibility_update` | recruiter | Set PUBLIC \| TENANT_ONLY \| RECRUITER |
| 9 | `job_salary_band_validate` | admin | Validate salary vs domain market-rate band |
| 10 | `job_seo_generate` | recruiter | Generate SEO metadata for React web listing |
| 11 | `job_search_fulltext` | candidate | Full-text search across PUBLISHED jobs |
| 12 | `job_status_transition` | recruiter | Transition status through the state machine |
| 13 | `job_audit_trail` | **admin** | Immutable audit log per job |
| 14 | `job_domain_stats` | **admin** | Aggregate counts per domain and status |
| 15 | `job_health` | service | Health check: PostgreSQL, Redis, Kafka, OpenSearch |
| 16 | `job_schema` | candidate | Returns full JSON schema + state machine |

---

## Security Architecture

Every tool call requires a Keycloak JWT in the `bearer_token` field.

| Layer | Implementation |
|-------|----------------|
| **Authentication** | Keycloak JWT (Bearer token) — sub, realm, roles, domain, exp claims |
| **RBAC** | 4 role hierarchy: `admin > recruiter > candidate > service` |
| **Rate Limiting** | 60 requests/minute per JWT subject (sliding window) |
| **Input Sanitisation** | SQL injection, XSS, path traversal patterns blocked |
| **Domain Isolation** | Jobs only visible within their tenant_id + domain scope |
| **Optimistic Locking** | CAS version check on all update operations |
| **Audit Trail** | Immutable append-only log for every state change |
| **alg:none Attack** | JWT `alg:none` explicitly blocked |
| **Token Expiry** | JWT `exp` claim enforced on every call |

> **Production note:** Replace `validateSignature()` in `SecurityManager` with a JWKS fetch
> from your Keycloak realm's well-known endpoint:
> `https://keycloak.ecoskiller.com/realms/{realm}/protocol/openid-connect/certs`

---

## Job State Machine

```
DRAFT ──────────────────► MODERATION_REQUIRED
  │                                │
  │ (direct if band OK)            ├── APPROVE ──► PUBLISHED ──► CLOSED ──► ARCHIVED
  └──────────────────────────────► │
                                   └── REJECT  ──► REJECTED (terminal)
```

---

## Domain Isolation

Ecoskiller enforces strict domain boundaries across 5 domains:

| Domain | Salary Band (INR/month) |
|--------|-------------------------|
| Arts | ₹15,000 – ₹2,50,000 |
| Commerce | ₹20,000 – ₹5,00,000 |
| Science | ₹25,000 – ₹6,00,000 |
| Technology | ₹30,000 – ₹15,00,000 |
| Administration | ₹20,000 – ₹4,00,000 |

Jobs outside their domain band are flagged as `MODERATION_REQUIRED` automatically.

---

## Kafka Events Published

| Event | Topic Pattern |
|-------|---------------|
| `job.created` | `jobs.<domain>` |
| `job.updated` | `jobs.<domain>` |
| `job.deleted` | `jobs.<domain>` |
| `job.moderation_required` | `jobs.<domain>` |
| `job.approved` | `jobs.<domain>` |
| `job.rejected` | `jobs.<domain>` |
| `job.closed` | `jobs.<domain>` |

---

## Requirements

- Java 17+
- Maven 3.8+

---

## Build

```bash
cd job-service-mcp
mvn clean package -q
```

Produces: `target/job-service-mcp-1.0.0.jar` (fat JAR, ~4 MB)

---

## Run the Server

```bash
java -jar target/job-service-mcp-1.0.0.jar
```

The server reads JSON-RPC 2.0 messages from **stdin** and writes responses to **stdout**.

---

## Run Tests

```bash
mvn test -q
# OR directly:
java -cp target/job-service-mcp-1.0.0.jar com.ecoskiller.mcp.JobServiceMcpTest
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "ecoskiller-job-service": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/job-service-mcp/target/job-service-mcp-1.0.0.jar"]
    }
  }
}
```

---

## Example Tool Call (raw JSON-RPC)

```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "tools/call",
  "params": {
    "name": "job_create",
    "arguments": {
      "bearer_token": "<keycloak-jwt>",
      "title": "Senior Java Engineer",
      "domain": "Technology",
      "description": "Build microservices on GCP + AWS",
      "tenant_id": "tenant-001",
      "recruiter_id": "recruiter-001",
      "salary_min": 80000,
      "salary_max": 200000,
      "location": "Remote"
    }
  }
}
```

---

## File Structure

```
job-service-mcp/
├── pom.xml
├── claude_desktop_config.json
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp/
    │   ├── server/
    │   │   └── JobServiceMcpServer.java      ← Main entry point, stdio event loop
    │   ├── security/
    │   │   └── SecurityManager.java          ← JWT, RBAC, rate limit, sanitisation
    │   ├── model/
    │   │   └── JobDomain.java                ← Enums, salary bands, state machine
    │   └── tools/
    │       ├── ToolRegistry.java             ← Tool registration + routing
    │       └── JobToolHandlers.java          ← All 16 tool implementations
    └── test/java/com/ecoskiller/mcp/
        └── JobServiceMcpTest.java            ← 20 test cases
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

*Ecoskiller Platform | Job Service MCP | v1.0.0 | March 2026 | Confidential*
