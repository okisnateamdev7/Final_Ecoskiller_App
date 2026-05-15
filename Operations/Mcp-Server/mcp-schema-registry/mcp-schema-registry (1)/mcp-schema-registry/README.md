# mcp-schema-registry

**Ecoskiller | CAT-INFRA-13 — Apicurio Schema Registry**
MCP Server in **Java 21** | **22 Tools** | Priority: **MEDIUM** | Secure

---

## Overview

Java MCP server wrapping the [Apicurio Registry](https://www.apicur.io/registry/) REST API v2.
Built from the *Ecoskiller Schema Registry (Apicurio) Documentation v1.0 (March 2025)*.

The Schema Registry is the **single source of truth** for all Kafka event schemas across
Ecoskiller's 50+ microservices. This MCP server exposes every schema governance operation
as a Claude-callable tool — registration, versioning, compatibility checking, validation,
lifecycle management, multi-tenant isolation, and health monitoring.

---

## Tools (22)

### Schema Registration & Versioning (5)

| # | Tool | Description |
|---|------|-------------|
| 1 | `schema_register` | Register a new schema or add a version. Compatibility enforced automatically. |
| 2 | `schema_get` | Retrieve schema content by artifact ID + version. |
| 3 | `schema_get_by_id` | Retrieve schema by global content ID (from Kafka message headers). |
| 4 | `schema_list_versions` | List all versions of a schema with lineage info. |
| 5 | `schema_list_subjects` | List all registered subjects (Kafka topic schemas) in a group. |

### Compatibility Management (4)

| # | Tool | Description |
|---|------|-------------|
| 6 | `compatibility_get` | Get the compatibility mode for a schema subject. |
| 7 | `compatibility_set` | Set compatibility: BACKWARD, FORWARD, FULL, TRANSITIVE variants, NONE. |
| 8 | `compatibility_test` | **Dry-run** — test if a new schema is compatible WITHOUT registering. |
| 9 | `global_rule_set` | Set a global rule (COMPATIBILITY/VALIDITY) applying to all schemas. |

### Schema Validation (2)

| # | Tool | Description |
|---|------|-------------|
| 10 | `schema_validate_content` | Validate a JSON record against a registered schema before publishing to Kafka. |
| 11 | `schema_lint` | Lint a schema for structural errors, missing docs, PII fields, naming conventions. |

### Schema Lifecycle & Governance (4)

| # | Tool | Description |
|---|------|-------------|
| 12 | `schema_update_state` | Set state: ENABLED, DEPRECATED (producers fall back), DISABLED (emergency rollback). |
| 13 | `schema_update_meta` | Update name, description, owner team, domain tag, PII flag — no new version. |
| 14 | `schema_delete` | Delete a schema and ALL versions. Requires `confirm=CONFIRM`. |
| 15 | `schema_delete_version` | Delete a single schema version by version number. |

### Schema Discovery & Search (3)

| # | Tool | Description |
|---|------|-------------|
| 16 | `schema_search` | Full-text search schemas by name, description, artifact ID. |
| 17 | `schema_get_meta` | Get artifact metadata (state, type, labels, timestamps) without schema content. |
| 18 | `schema_export` | Export all schemas in a group — for backup, migration, impact analysis. |

### Multi-Tenant Group Management (2)

| # | Tool | Description |
|---|------|-------------|
| 19 | `group_create` | Create a tenant namespace (group) — schemas are isolated per group. |
| 20 | `group_list` | List all groups (tenant namespaces) in the registry. |

### Schema References (1)

| # | Tool | Description |
|---|------|-------------|
| 21 | `schema_get_references` | Get inbound + outbound schema references for impact analysis. |

### Health & Monitoring (1)

| # | Tool | Description |
|---|------|-------------|
| 22 | `registry_health` | Apicurio system info, version, connection latency, alert flags. |

---

## Security Features

| Feature | Implementation |
|---------|----------------|
| **API Key Auth** | Every tool call validated. SHA-256 hashed. Constant-time comparison (no timing attacks). |
| **Key Rotation** | Two keys supported (MCP_API_KEY + MCP_API_KEY_2) — zero-downtime rotation. |
| **Auth to Apicurio** | Supports `none`, `basic` (username/password), `bearer` (Keycloak JWT). |
| **TLS** | `APICURIO_TLS_VERIFY=true` enforced by default. Custom truststore supported. |
| **Input Sanitisation** | Artifact IDs validated — path traversal (`../`), slashes, and overly long IDs rejected. |
| **Delete Guard** | `schema_delete` requires `confirm=CONFIRM` — prevents accidental deletion. |
| **State Guard** | `schema_delete_version` rejects `version=latest` — must use explicit version number. |
| **Group Isolation** | All operations are scoped to a group — cross-tenant access impossible. |
| **Pub/Sub Allowlist** | Channel prefixes validated (N/A for schema registry but pattern retained). |
| **Audit Logging** | All calls, results, errors, unauthorised attempts → structured JSON → Wazuh SIEM. |
| **Secret Masking** | `api_key`, `password`, `token`, `schema_content` masked in all audit logs. |
| **Zero Hardcoded Secrets** | All config via environment variables (12-factor). |
| **Non-root Container** | Kubernetes runs as UID 1000, read-only rootfs, all Linux capabilities dropped. |

---

## Requirements

- **Java 21+**
- **Maven 3.9+**
- **Apicurio Registry 3.x** running and accessible

---

## Quick Start

### 1. Start Apicurio (in-memory, for dev/test)

```bash
docker run -d -p 8080:8080 apicurio/apicurio-registry-mem:latest
```

### 2. Set environment variables

```bash
export APICURIO_BASE_URL=http://localhost:8080/apis/registry/v2
export APICURIO_AUTH_TYPE=none
export APICURIO_DEFAULT_GROUP=ecoskiller
export DEFAULT_COMPATIBILITY=BACKWARD
export MCP_API_KEY=$(openssl rand -hex 32)
echo "Your MCP API key: $MCP_API_KEY"
```

### 3. Build

```bash
mvn clean package -DskipTests
# Output: target/mcp-schema-registry-server.jar
```

### 4. Run

```bash
java -jar target/mcp-schema-registry-server.jar
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-schema-registry": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-schema-registry-server.jar"],
      "env": {
        "APICURIO_BASE_URL":      "http://localhost:8080/apis/registry/v2",
        "APICURIO_AUTH_TYPE":     "none",
        "APICURIO_DEFAULT_GROUP": "ecoskiller",
        "DEFAULT_COMPATIBILITY":  "BACKWARD",
        "MCP_API_KEY":            "your-secret-key"
      }
    }
  }
}
```

---

## Run Tests

```bash
# Start Apicurio first
docker run -d -p 8080:8080 apicurio/apicurio-registry-mem:latest

# Set test API key
export MCP_API_KEY=test-secret-key-schema

# Run tests
mvn test
```

Many tests (lint, security, protocol) run without Apicurio. Integration tests gracefully
skip if Apicurio is not reachable.

---

## Compatibility Modes Explained

| Mode | When to use | Ecoskiller default |
|------|-------------|-------------------|
| `BACKWARD` | Safe producer evolution — consumers of old schema can read new messages | ✅ Default |
| `FORWARD` | Safe consumer evolution — consumers of new schema can read old messages | — |
| `FULL` | Both BACKWARD + FORWARD — maximum flexibility for independent evolution | Recommended for shared types |
| `BACKWARD_TRANSITIVE` | BACKWARD checked against ALL prior versions, not just latest | — |
| `FORWARD_TRANSITIVE` | FORWARD checked against ALL prior versions | — |
| `FULL_TRANSITIVE` | FULL checked against ALL prior versions | High-governance topics |
| `NONE` | No compatibility check | Dev/test only |

---

## Key Naming Convention

| Tool | Example Artifact ID |
|------|---------------------|
| `schema_register` | `candidate-scored`, `assessment-completed`, `notification-sent` |
| Group (tenant) | `ecoskiller`, `tenant-001`, `acme-corp` |
| Full key | `group=ecoskiller, artifact_id=candidate-scored, version=2` |

---

## Ecoskiller Kafka Topic → Schema Mapping

| Kafka Topic | Artifact ID | Type | Compatibility |
|-------------|-------------|------|---------------|
| `candidate-scored` | `candidate-scored` | AVRO | BACKWARD |
| `assessment-completed` | `assessment-completed` | AVRO | BACKWARD |
| `group-discussion-event` | `group-discussion-event` | AVRO | FULL |
| `notification-sent` | `notification-sent` | AVRO | BACKWARD |
| `royalty-calculated` | `royalty-calculated` | AVRO | FULL_TRANSITIVE |

---

## Protocol

| Property | Value |
|----------|-------|
| Transport | **stdio** (stdin/stdout) |
| Format | **JSON-RPC 2.0** |
| MCP Version | **2024-11-05** |
| Methods | `initialize`, `tools/list`, `tools/call`, `ping` |

---

## File Structure

```
mcp-schema-registry/
├── pom.xml
├── .env.example
├── claude_desktop_config.json
├── k8s-deployment.yaml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/ecoskiller/mcp/schema/
    │   │   ├── McpSchemaRegistryServer.java      ← Entry point, JSON-RPC dispatcher
    │   │   ├── config/
    │   │   │   └── SchemaRegistryConfig.java     ← Env-var config, auth, defaults
    │   │   ├── client/
    │   │   │   └── ApicurioClient.java           ← Full Apicurio REST API v2 client
    │   │   ├── security/
    │   │   │   ├── ApiKeyValidator.java          ← SHA-256, constant-time comparison
    │   │   │   └── AuditLogger.java              ← Structured audit log (Wazuh-ready)
    │   │   └── tools/
    │   │       ├── BaseToolClasses.java          ← McpTool, ToolResult, BaseTool
    │   │       ├── RegistrationTools.java        ← schema_register/get/list (5 tools)
    │   │       ├── CompatibilityTools.java       ← compatibility_* + global_rule (4 tools)
    │   │       ├── RemainingTools.java           ← validation, lifecycle, search,
    │   │       │                                    groups, references, health (13 tools)
    │   │       └── ToolRegistry.java             ← Wires all 22 tools
    │   └── resources/
    │       └── logback.xml
    └── test/
        └── java/com/ecoskiller/mcp/schema/
            └── McpSchemaRegistryServerTest.java  ← 25 tests
```
