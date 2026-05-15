# certification-engine-mcp

**Ecoskiller | Certification Engine — MCP Server (Java)**  
Transport: stdio / JSON-RPC 2.0 | 18 Tools | Priority: HIGH | Security: JWT/RBAC

---

## Overview

This MCP server exposes the full capability surface of the Ecoskiller
`certification-engine` microservice as 18 structured tools consumable by
Claude Desktop or any MCP-compatible client.

The Certification Engine is the domain service responsible for converting
assessment outcomes into trusted, cryptographically verifiable credentials.
It listens to Kafka events from the candidate-ranking-engine and scoring systems,
evaluates eligibility rules, issues digital belt-level certificates, manages
mentor verification workflows for senior tiers, stores certificate files in
MinIO object storage, and publishes outcome events to downstream services.

---

## Security Model

**Every tool call requires a valid `bearer_token` argument.**

| Layer | Mechanism |
|---|---|
| **Authentication** | Keycloak 24.0 JWT — expiry, claim presence, `alg:none` hard rejection |
| **Authorization** | RBAC role hierarchy: `admin` > `recruiter` > `system` > `readonly` |
| **Tenant isolation** | `tenant_id` sourced exclusively from JWT claims — never from tool args |
| **`alg:none` rejection** | Unconditional hard reject — no exceptions |
| **Signature verification** | RS256 via JWKS (see TODO in `SecurityContext.java` for production wiring) |

Required JWT claims on every call:
- `tenant_id` — Ecoskiller tenant UUID
- `role` — one of: `admin`, `recruiter`, `system`, `readonly`
- `sub` — principal identifier
- `exp` — expiry Unix timestamp

---

## Tools (18)

### Certificate Lifecycle

| # | Tool | Role | Description |
|---|------|------|-------------|
| 1 | `issue_certificate` | system | Issue verifiable digital certificate · stores in PostgreSQL + MinIO · publishes `certificate.issued` |
| 2 | `revoke_certificate` | **admin** | Revoke issued certificate · audit-logged · publishes `certification.rejected` |
| 3 | `get_certificate_details` | readonly | Full certificate record: status, scores, dimensions, QR metadata, storage ref |
| 4 | `verify_certificate_qr` | readonly | Tamper-proof QR token verification · returns VALID / REVOKED / EXPIRED |
| 5 | `list_candidate_certificates` | readonly | All certificates for a candidate with optional status filter |

### Belt Management

| # | Tool | Role | Description |
|---|------|------|-------------|
| 6 | `get_belt_hierarchy` | readonly | Full versioned tier list: thresholds, sources, mentor requirements, progression rules |
| 7 | `assign_belt_level` | system | Assign initial belt · creates belt_assignment record · triggers mentor flow for gold/platinum |
| 8 | `get_candidate_belt_status` | readonly | Current belt, history, active cert, next-promotion gap |
| 9 | `promote_belt_level` | **admin** | Promote to next tier · supersedes previous cert · issues new certificate |

### Eligibility Evaluation

| # | Tool | Role | Description |
|---|------|------|-------------|
| 10 | `evaluate_eligibility` | system | Rule-based eligibility check: score vs thresholds + source requirements → eligible belt or gap |
| 11 | `get_eligibility_rules` | recruiter | Versioned rule set: thresholds, sources, mentor flags per belt tier |
| 12 | `update_eligibility_rules` | **admin** | Version-new eligibility rules · archives previous · effective for new rounds only |

### Mentor Verification

| # | Tool | Role | Description |
|---|------|------|-------------|
| 13 | `trigger_mentor_verification` | system | Assign mentor · set 5-day SLA · notify via notification-service · gold/platinum only |
| 14 | `get_mentor_verification_status` | readonly | Check PENDING / APPROVED / REJECTED / EXPIRED · mentor comments · SLA status |

### Kafka Events & Storage

| # | Tool | Role | Description |
|---|------|------|-------------|
| 15 | `ingest_certification_event` | system | Consume Kafka trigger events · schema validate · idempotency check · route to evaluate_eligibility |
| 16 | `publish_certification_event` | system | Publish `certificate.issued` or `certification.rejected` to Kafka · Avro + Apicurio validated |
| 17 | `get_certificate_file` | readonly | Generate time-limited pre-signed MinIO URL for certificate PDF (SSE-S3, no direct public access) |
| 18 | `get_certification_audit_log` | **admin** | Full compliance audit: issuance, revocations, mentor verdicts, eligibility evaluations, rule changes |

---

## Kafka Topics

### Consumed (triggers)
| Topic | Producer |
|---|---|
| `candidate.rank.computed` | candidate-ranking-engine |
| `belt.eligible` | scoring-engine |
| `match.scored` | scoring-engine |
| `gd.completed` | gd-orchestrator |
| `interview.completed` | interview-service |

### Published (outcomes)
| Topic | Consumers |
|---|---|
| `certificate.issued` | notification-service, analytics-service, admin-service |
| `certification.rejected` | notification-service, analytics-service |

---

## Belt Tier Reference

| Belt | Min Score | Sources Required | Mentor Required |
|---|---|---|---|
| Bronze | 60.0 | GD | No |
| Silver | 75.0 | GD + Interview | No |
| Gold | 88.0 | GD + Interview + Dojo | **Yes** |
| Platinum | 95.0 | GD + Interview + Dojo + Intelligence | **Yes (senior)** |

---

## Requirements

- **Java 17+**
- **Maven 3.8+** (for build)
- **Python 3.8+** (for test script only)

---

## Build

```bash
mvn clean package -q
# Produces: target/certification-engine-mcp-1.0.0.jar
```

---

## Run

```bash
java -jar target/certification-engine-mcp-1.0.0.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0.  
All logging goes to **stderr** — stdout is reserved for the protocol.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "certification-engine": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/target/certification-engine-mcp-1.0.0.jar"]
    }
  }
}
```

---

## Run Tests

```bash
python3 test_server.py             # Quick pass/fail (33 tests)
python3 test_server.py --verbose   # Full JSON output
python3 test_server.py --test issue_certificate   # Single test
```

Test coverage:
- Protocol: `initialize`, `ping`, `tools/list`, unknown method
- Security: missing token, expired token, RBAC violations, `alg:none` rejection
- All 18 tools across admin / recruiter / system / readonly roles
- Input validation: invalid belt_level, invalid topic, missing args

---

## File Structure

```
certification-engine-mcp/
├── pom.xml
├── README.md
├── claude_desktop_config.json
├── test_server.py
└── src/main/java/com/ecoskiller/mcp/
    ├── server/
    │   ├── CertificationEngineMcpServer.java  ← Main server + JSON-RPC dispatch
    │   └── McpTool.java                        ← Tool interface + RBAC hierarchy
    ├── security/
    │   └── SecurityContext.java                ← JWT validation + tenant guard
    └── tools/
        ├── BaseTool.java                        ← Schema builder + arg helpers
        ├── CertificateLifecycleTools.java       ← Tools 1–5
        ├── BeltManagementTools.java             ← Tools 6–9
        ├── EligibilityTools.java                ← Tools 10–12
        └── OperationsTools.java                 ← Tools 13–18
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Production Wiring Notes

1. **Signature verification**: See `TODO` in `SecurityContext.java` — integrate
   `nimbus-jose-jwt` with a cached JWKS fetch from your Keycloak realm JWKS URI:
   `https://auth.ecoskiller.com/realms/ecoskiller/protocol/openid-connect/certs`

2. **PostgreSQL**: Connect in each tool's `execute()` to tables:
   `certificates`, `belt_assignments`, `certification_eligibility_rules`,
   `mentor_verification_requests`, `certification_audit_log`

3. **MinIO**: Wire `issue_certificate` and `get_certificate_file` to your MinIO
   client — bucket per tenant (`ecoskiller-certs-{tenant_id}`), SSE-S3 enabled,
   pre-signed URL generation with expiry.

4. **Kafka**: Wire `ingest_certification_event` to KafkaConsumer and
   `publish_certification_event` to KafkaProducer. Avro schemas validated against
   Apicurio Schema Registry at `https://schema-registry.ecoskiller.internal`.

5. **Tenant isolation**: `securityCtx.assertTenantMatch()` is available in every
   tool — call it whenever tool args contain a `tenant_id` to prevent cross-tenant
   data access.

6. **DLQ policy**: All Kafka consumers follow the platform standard —
   3 retries with exponential backoff (1s, 4s, 16s), then publish to `{topic}.dlq`.
   Prometheus alert fires on any DLQ event count > 0 in a 5-minute window.

---

## Infrastructure Dependencies

| Component | Version | Role |
|---|---|---|
| Apache Kafka | 3.7.0 | Event bus — consume triggers, publish outcomes |
| PostgreSQL | 15 | Certificate records, belt assignments, audit log (RLS-enforced) |
| Redis | 7 | Session/eligibility cache (tenant-namespaced keys) |
| MinIO | Latest | Certificate PDF storage — per-tenant bucket, SSE-S3, pre-signed URLs |
| Apicurio Schema Registry | Self-hosted | Avro schema validation for all Kafka events |
| Keycloak | 24.0 | JWT issuance, RBAC, tenant realm isolation |
| OpenTelemetry Collector | Latest | Distributed trace propagation across service boundaries |
| Kubernetes (k3s) | CNCF | Pod scheduling, HPA, ConfigMap/Secret mounting |
| Flyway | Latest | PostgreSQL schema version control |

---

*Ecoskiller Platform Engineering · certification-engine-mcp · v1.0.0 · March 2026*
