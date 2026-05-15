# trivy-mcp-server

**Ecoskiller Platform | ops namespace — Trivy Security Scanner Service**  
MCP Server in Java 17 | 14 Agents | Priority: HIGH | Secure

---

## Overview

`trivy-mcp-server` is a secure, production-grade Java 17 MCP server that exposes
Trivy's vulnerability scanning capabilities as an AI-callable toolset for the
Ecoskiller CI/CD pipeline. It serves as the automated security gatekeeper — scanning
all 13+ Ecoskiller microservice images, IaC definitions, and Kubernetes manifests
before Harbor registry publication.

**Transport:** stdio (stdin/stdout)  
**Protocol:** JSON-RPC 2.0 / MCP 2024-11-05  
**Runtime:** Java 17+  
**Security:** JWT (Keycloak RS256), RBAC, rate limiting, structured audit logging

---

## Agents (14)

| # | Tool | Description | Role Access |
|---|------|-------------|-------------|
| 1 | `scan_image` | Scan Docker image for CVEs (OS, app deps, binaries) | All |
| 2 | `scan_registry` | Scan Harbor registry image by pull reference | sec_ops |
| 3 | `scan_filesystem` | Scan filesystem/repo for vulnerabilities + secrets | All |
| 4 | `scan_iac` | Scan Terraform/OpenTofu HCL for misconfigurations | sec_ops |
| 5 | `scan_k8s_manifests` | Scan K8s YAML/Helm for security misconfigurations | sec_ops |
| 6 | `generate_sbom` | Generate CycloneDX/SPDX SBOM → archive to MinIO | sec_ops |
| 7 | `policy_check` | Evaluate scan vs env policy (dev/stage/prod gate) | All |
| 8 | `trivy_db_update` | Trigger/check vulnerability DB update | sec_ops |
| 9 | `exception_management` | Manage .trivyignore waivers (add/list/revoke/audit) | sec_only |
| 10 | `export_sarif` | Export findings as SARIF 2.1.0 for IDE/GitLab SAST | All |
| 11 | `harbor_tag_metadata` | Write Trivy scan labels to Harbor image metadata | sec_ops |
| 12 | `ci_pipeline_gate` | Final CI/CD pass/fail gate aggregating all scans | All |
| 13 | `compliance_report` | NIST CSF/OWASP/DPDPA 2023/PCI-DSS report | sec_only |
| 14 | `scan_history` | Query historical scan results from ClickHouse | sec_ops |

---

## Security Architecture

```
Client Request (JSON-RPC)
       │
       ▼
┌────────────────────────────────────┐
│  1. JWT Validation                 │  ← Keycloak RS256 (JWKS endpoint)
│     • Signature verify (stub→prod) │    TRIVY_JWT_BYPASS=true for dev
│     • Expiry + issuer check        │
├────────────────────────────────────┤
│  2. Rate Limiting                  │  ← Per-client sliding window
│     • 100 req / 60s default        │    TRIVY_RATE_LIMIT_MAX / _MS env vars
├────────────────────────────────────┤
│  3. RBAC (Keycloak realm roles)    │
│     • admin                        │    Per-tool role enforcement
│     • security_engineer            │    Extracted from JWT realm_access.roles
│     • devops_engineer              │
│     • developer                    │
│     • viewer                       │
├────────────────────────────────────┤
│  4. Input Validation               │  ← Per-tool required field checks
├────────────────────────────────────┤
│  5. Structured Audit Logging       │  ← JSON lines → stderr + TRIVY_AUDIT_LOG_FILE
│     • Every call: client, action,  │    Flows → ClickHouse security.trivy_scans
│       outcome, latency             │    (§11.5 Trivy service spec)
└────────────────────────────────────┘
       │
       ▼
  Tool Execution
```

### RBAC Summary

| Role | Tools Accessible |
|------|-----------------|
| `admin` | All 14 tools |
| `security_engineer` | All 14 tools |
| `devops_engineer` | 12 tools (not exception_management, compliance_report) |
| `developer` | 5 tools: scan_image, scan_filesystem, policy_check, export_sarif, ci_pipeline_gate |
| `viewer` | Same as developer |

### JWT Setup (Production)

1. Uncomment `jose4j` in `pom.xml`
2. Set `KEYCLOAK_ISSUER` env var to your Keycloak realm URL
3. Replace `verifyStructure()` stub with JWKS RS256 — marked `TODO` in `JwtValidator.java`
4. Remove `TRIVY_JWT_BYPASS=true` from all production configs

---

## Policy Rules (from Trivy Service Spec §4.4)

| Environment | CRITICAL | HIGH | MEDIUM | LOW |
|-------------|----------|------|--------|-----|
| dev / test  | WARN only | WARN only | WARN only | Log only |
| stage       | **BLOCK** | **BLOCK** | WARN | Log only |
| prod        | **BLOCK** | **BLOCK** | **BLOCK** | Log only |

---

## Build & Run

```bash
# Prerequisites: Java 17+, Maven 3.8+

# Build fat JAR
mvn clean package -q

# Dev mode (JWT bypass)
TRIVY_JWT_BYPASS=true java -jar target/trivy-mcp-server-1.0.0.jar

# Production mode
KEYCLOAK_ISSUER=https://auth.ecoskiller.com/realms/ecoskiller \
TRIVY_AUDIT_LOG_FILE=/var/log/ecoskiller/trivy-audit.jsonl \
java -jar target/trivy-mcp-server-1.0.0.jar
```

---

## Run Tests

```bash
mvn test
# Expected: 37 tests, all PASS
# Covers: 14 tool schemas, 14+ happy-path executions,
#         policy logic, validation, RateLimiter, RBAC
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "trivy-mcp-server": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/target/trivy-mcp-server-1.0.0.jar"],
      "env": { "TRIVY_JWT_BYPASS": "true" }
    }
  }
}
```

---

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `TRIVY_JWT_BYPASS` | `false` | `true` = skip JWT validation (dev only — NEVER in prod) |
| `KEYCLOAK_ISSUER` | `https://auth.ecoskiller.com/realms/ecoskiller` | Keycloak realm issuer URL |
| `TRIVY_AUDIT_LOG_FILE` | _(stderr only)_ | Path to write structured audit JSON lines |
| `TRIVY_RATE_LIMIT_MAX` | `100` | Max requests per window per client |
| `TRIVY_RATE_LIMIT_MS` | `60000` | Rate limit window in milliseconds |

---

## File Structure

```
trivy-mcp-server/
├── pom.xml
├── README.md
├── claude_desktop_config.json
└── src/
    ├── main/java/com/ecoskiller/trivy/mcp/
    │   ├── TrivyMCPServer.java           ← Main server (stdio JSON-RPC loop)
    │   ├── MCPTool.java                  ← Tool interface
    │   ├── security/
    │   │   ├── JwtValidator.java         ← Keycloak JWT validation
    │   │   ├── AuditLogger.java          ← Structured JSON audit logging
    │   │   └── RateLimiter.java          ← Per-client sliding-window limiter
    │   └── tools/
    │       ├── BaseTool.java             ← Abstract base: helpers, role sets
    │       └── ToolRegistry.java         ← Factory + all 14 implementations
    └── test/java/com/ecoskiller/trivy/mcp/
        └── TrivyMCPServerTest.java       ← 37-test JUnit 5 suite
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Trivy Integration in GitLab CI Pipeline

```yaml
# .gitlab-ci.yml (conceptual — MCP server triggers Trivy logic)
trivy-scan:
  stage: build
  script:
    - >
      echo '{"jsonrpc":"2.0","id":"1","method":"tools/call",
       "params":{"name":"ci_pipeline_gate",
        "arguments":{"pipeline_id":"'$CI_PIPELINE_ID'",
         "service_name":"'$SERVICE_NAME'","environment":"prod",
         "image_scan_result":"PASSED"}}}' | java -jar trivy-mcp-server.jar
  artifacts:
    reports:
      container_scanning: gl-container-scanning-report.sarif
```

---

## Production Checklist

- [ ] Replace `JwtValidator.verifyStructure()` stub with jose4j JWKS RS256
- [ ] Set `KEYCLOAK_ISSUER` in Kubernetes Secret (ops namespace)
- [ ] Set `TRIVY_AUDIT_LOG_FILE` and ship to ClickHouse via Filebeat
- [ ] Deploy behind NGINX Ingress with ModSecurity WAF
- [ ] Enable Trivy image in GitLab CI CronJob for nightly Harbor registry scans
- [ ] Pre-download Trivy DB as ConfigMap for air-gapped/offline scanning
- [ ] Configure Prometheus alert: CRITICAL CVEs → Mattermost #security channel
- [ ] Wire MinIO S3 client in `generate_sbom` for real SBOM archival
- [ ] Wire ClickHouse client in `scan_history` for real historical queries
