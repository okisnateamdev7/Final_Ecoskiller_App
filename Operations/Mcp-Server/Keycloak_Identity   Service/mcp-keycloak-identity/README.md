# mcp-keycloak-identity

**Ecoskiller | CAT-IAM — Keycloak Identity & Access Management**  
MCP Server in Java | 15 Tools | Priority: HIGH | Protocol: MCP 2024-11-05

---

## Overview

Secure Java MCP server for Keycloak IAM on the Ecoskiller platform.  
Covers the full identity lifecycle: OAuth2/OIDC authentication, user CRUD, RBAC,
MFA, session management, audit logging, and compliance — all via stdio JSON-RPC 2.0.

**Architecture position:** Sits between Claude/MCP clients and the Keycloak Admin REST API.
All calls are authenticated, rate-limited, input-validated, and audit-safe.

---

## Tools (15)

| # | Tool Name | Description |
|---|-----------|-------------|
| 1 | `authenticate_user` | OAuth2/OIDC flows: authorization_code, password, client_credentials, refresh_token |
| 2 | `token_management` | Revoke tokens, logout users, get public key (RS256 JWKS) |
| 3 | `token_introspect` | Validate & decode JWT (RFC 7662 introspection endpoint) |
| 4 | `user_management` | Create, get, update, delete, search, lock/unlock users (GDPR-compliant) |
| 5 | `bulk_user_import` | Batch import up to 500 users (campus hiring, recruiter onboarding) |
| 6 | `role_management` | RBAC: create/delete roles, assign/remove roles to users |
| 7 | `session_management` | List/terminate sessions, realm session stats |
| 8 | `mfa_management` | TOTP status, remove TOTP, send MFA setup email, list credentials |
| 9 | `realm_management` | Multi-tenancy: list/create/update/delete realms (campus, corporate) |
| 10 | `identity_provider` | Configure Google, Okta, GitHub, Azure AD federation (OIDC/SAML2) |
| 11 | `user_federation` | LDAP/Active Directory integration, user sync (campus LDAP) |
| 12 | `audit_log` | Query auth events, admin events (DPDP Act 2023 / GDPR / SOC2) |
| 13 | `password_policy` | Set/get password rules, apply Ecoskiller security baseline |
| 14 | `security_health_check` | Run security checks: brute-force, SSL, token TTL, password policy |
| 15 | `compliance_report` | DPDP/GDPR/SOC2 reports: login failures, privilege summary, admin actions |

---

## Requirements

- Java 17+
- Maven 3.8+
- A running Keycloak instance (v20+)

---

## Environment Variables (required before running)

```bash
# Required
export KEYCLOAK_URL="https://keycloak.ecoskiller.com"
export KEYCLOAK_REALM="ecoskiller"
export KEYCLOAK_CLIENT_ID="mcp-iam-client"
export KEYCLOAK_CLIENT_SECRET="your-confidential-client-secret"

# Optional
export KEYCLOAK_TIMEOUT_SECS="10"      # HTTP timeout (default 10)
```

> ⚠️ **Never hardcode secrets.** Use Kubernetes Secrets, AWS SSM, or GCP Secret Manager
> and inject as environment variables at runtime.

---

## Build

```bash
cd mcp-keycloak-identity
mvn clean package -q
# Output: target/mcp-keycloak-identity.jar
```

---

## Run the server

```bash
java -jar target/mcp-keycloak-identity.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).  
All logs go to **stderr** (stdout is reserved for MCP protocol messages).

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-keycloak-identity": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-keycloak-identity/target/mcp-keycloak-identity.jar"],
      "env": {
        "KEYCLOAK_URL": "https://keycloak.ecoskiller.com",
        "KEYCLOAK_REALM": "ecoskiller",
        "KEYCLOAK_CLIENT_ID": "mcp-iam-client",
        "KEYCLOAK_CLIENT_SECRET": "your-secret-here"
      }
    }
  }
}
```

---

## Keycloak Client Setup (one-time)

Before running the server, create a confidential client in Keycloak:

1. Login to Keycloak Admin Console → `ecoskiller` realm
2. Clients → Create → Client ID: `mcp-iam-client`
3. Access Type: `confidential`
4. Service Accounts Enabled: `ON`
5. Standard Flow: `OFF` (this is a service account, not a user login client)
6. Save → Credentials tab → copy `Client Secret`
7. Service Account Roles → assign `realm-admin` role

```bash
# Or via Admin REST API:
curl -X POST https://keycloak.ecoskiller.com/admin/realms/ecoskiller/clients \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "mcp-iam-client",
    "secret": "your-secret",
    "serviceAccountsEnabled": true,
    "standardFlowEnabled": false,
    "enabled": true
  }'
```

---

## Run Tests

```bash
mvn test
# or verbose:
mvn test -Dsurefire.useFile=false
```

---

## Security Architecture

```
MCP Client (Claude Desktop / API)
        │ stdin/stdout JSON-RPC 2.0
        ▼
┌──────────────────────────────────────────┐
│         KeycloakMcpServer                │
│  ┌─────────────┐  ┌──────────────────┐  │
│  │ RateLimiter │  │  InputValidator  │  │
│  │ per-tool    │  │  email/UUID/safe │  │
│  └─────────────┘  └──────────────────┘  │
│         │                               │
│  ┌──────▼──────────────────────────┐    │
│  │   Tool Dispatcher (15 tools)    │    │
│  └──────┬──────────────────────────┘    │
└─────────┼────────────────────────────── ┘
          │
┌─────────▼────────────────────────────── ┐
│         KeycloakClient                  │
│  • Admin token auto-refresh (cached)    │
│  • HTTPS-only (Java HttpClient)         │
│  • client_secret from env vars only     │
│  • Secrets NEVER logged                 │
└──────────────────────────────────────── ┘
          │ HTTPS
          ▼
   Keycloak (v20+) on Kubernetes
   PostgreSQL ← Redis ← Kafka
```

**Key security properties:**
- `client_secret` is only read from `KEYCLOAK_CLIENT_SECRET` env var — never from tool args
- Passwords and tokens are never written to logs (stderr)
- Rate limiting: 20 req/min for `authenticate_user`, 5/min for `bulk_user_import`, 60/min default
- All inputs validated: email regex, UUID format, realm name allowlist, max 10KB per field
- `delete_realm` blocked for `master` and `ecoskiller` realms (safety guard)
- Credential objects stripped from all user responses

---

## File Structure

```
mcp-keycloak-identity/
├── pom.xml                                         ← Maven build (Java 17, fat JAR)
├── README.md
├── claude_desktop_config.json                      ← Claude Desktop config snippet
└── src/
    ├── main/java/com/ecoskiller/mcp/keycloak/
    │   ├── server/
    │   │   └── KeycloakMcpServer.java              ← Main MCP server (stdio JSON-RPC 2.0)
    │   ├── tools/
    │   │   ├── McpTool.java                        ← Tool interface
    │   │   ├── KeycloakClient.java                 ← Keycloak Admin REST API client
    │   │   ├── AuthenticateUserTool.java            ← Tool 1
    │   │   ├── TokenManagementTool.java             ← Tool 2
    │   │   ├── TokenIntrospectTool.java             ← Tool 3
    │   │   ├── UserManagementTool.java              ← Tool 4
    │   │   ├── BulkUserImportTool.java              ← Tool 5
    │   │   ├── RoleManagementTool.java              ← Tool 6
    │   │   └── AllRemainingTools.java              ← Tools 7–15 (session, MFA, realm,
    │   │                                               IdP, federation, audit,
    │   │                                               password_policy, health, compliance)
    │   └── security/
    │       ├── RateLimiter.java                    ← Token bucket rate limiter
    │       └── InputValidator.java                 ← Input sanitization & validation
    └── test/java/com/ecoskiller/mcp/keycloak/
        └── KeycloakMcpServerTest.java              ← JUnit 5 tests
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Standard Ecoskiller Roles

| Role | Description |
|------|-------------|
| `candidate` | Assessment taker, read-only access to own data |
| `recruiter` | View candidates, create assessments |
| `hiring-manager` | View results, approve/reject candidates |
| `admin` | Manage recruiters, configure assessments |
| `campus-admin` | Manage campus-specific hiring round |
| `super-admin` | Full platform access including IAM |
| `assessment-taker` | Specific permission to submit assessments |
| `assessment-reviewer` | Permission to review/grade assessments |

---

## Compliance

| Standard | Coverage |
|----------|---------|
| DPDP Act 2023 | Audit logging, data residency, consent management |
| GDPR | Right to erasure (`user_management` delete), data portability, consent |
| SOC2 Type II | Access controls, change management (`audit_log`, `compliance_report`) |
