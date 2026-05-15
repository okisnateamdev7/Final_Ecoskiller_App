# mcp-auth-service

**Ecoskiller | auth-service**  
MCP Server in Java | 18 Tools | Priority: CRITICAL | Namespace: core

The single source of truth for authentication and authorization in the Ecoskiller platform.
Stateless JWT-based auth with bcrypt hashing, TOTP MFA, OAuth 2.0, Redis session management,
and SOC 2 / GDPR-compliant audit logging.

---

## Tools (18)

### Identity Management
| # | Tool | Roles |
|---|------|-------|
| 1 | `register_user` | Public (no auth) |
| 2 | `login` | Public (no auth) |
| 3 | `logout` | Any authenticated |
| 4 | `reset_password` | Self (with current password) or ADMIN |

### Token Lifecycle
| # | Tool | Roles |
|---|------|-------|
| 5 | `refresh_token` | Any (with valid refresh token) |
| 6 | `validate_token` | Any (API Gateway middleware) |
| 7 | `revoke_token` | Self or ADMIN |
| 8 | `introspect_token` | Resource servers (OAuth RFC 7662) |

### MFA (TOTP / SMS)
| # | Tool | Roles |
|---|------|-------|
| 9 | `enable_mfa` | Any authenticated |
| 10 | `verify_mfa` | Any authenticated |
| 11 | `disable_mfa` | Self (current password + MFA code required) |

### OAuth 2.0
| # | Tool | Roles |
|---|------|-------|
| 12 | `get_oauth_authorization_code` | Any authenticated |
| 13 | `exchange_oauth_code` | OAuth clients |

### Session Management
| # | Tool | Roles |
|---|------|-------|
| 14 | `get_user_session` | Self or ADMIN |
| 15 | `list_user_sessions` | Self or ADMIN |
| 16 | `invalidate_all_sessions` | Self or ADMIN |

### RBAC & Compliance Audit
| # | Tool | Roles |
|---|------|-------|
| 17 | `check_permission` | Any authenticated |
| 18 | `get_security_audit_log` | ADMIN, SUPER_ADMIN |

---

## Security Architecture

| Layer | Mechanism |
|---|---|
| Password hashing | bcrypt (cost 12) — constant-time verification |
| JWT signing | HS256 (dev) / RS256 (prod via `AUTH_STRICT_MODE=true`) |
| JWT access token TTL | 15 minutes (configurable) |
| JWT refresh token TTL | 7 days, with rotation on each exchange |
| Token revocation | JTI blacklist in Redis (in-memory for dev) |
| MFA | RFC 6238 TOTP — 6-digit, 30-second window, ±1 tolerance |
| MFA device lock | 3 consecutive failures → locked (re-provision required) |
| OAuth 2.0 auth codes | Single-use, 10-minute TTL |
| Rate limiting | Max N failed login attempts per user (default 5) |
| Session store | Redis HASH (in-memory for dev) |
| Input sanitisation | SQL injection + XSS pattern detection on all fields |
| Tenant isolation | `tenant_id` enforced on every operation |
| Audit log | Immutable SOC 2 / GDPR compliant event log |

### RBAC Matrix

| Resource | CANDIDATE | RECRUITER | ADMIN | SUPER_ADMIN |
|---|---|---|---|---|
| assessment | read | read, grade | full | full |
| application | read, write | read, write, delete | full | full |
| candidate | read | read | full | full |
| job | read | read, write, delete | full | full |

---

## Environment Variables

| Variable | Default | Description |
|---|---|---|
| `AUTH_JWT_SECRET` | `ecoskiller-auth-dev-secret` | HMAC-HS256 signing secret |
| `AUTH_STRICT_MODE` | `false` | `true` = RS256 + signature enforcement |
| `AUTH_ACCESS_TOKEN_TTL` | `900` | Access token TTL in seconds |
| `AUTH_REFRESH_TOKEN_TTL` | `604800` | Refresh token TTL in seconds (7 days) |
| `AUTH_BCRYPT_COST` | `12` | bcrypt cost factor |
| `AUTH_MAX_LOGIN_ATTEMPTS` | `5` | Max failed logins before lockout |

---

## Kafka Events Published (topic: `auth`)

| Event | Trigger |
|---|---|
| `auth.user_registered` | New user registered |
| `auth.user_login` | Successful login |
| `auth.user_logout` | Logout or session invalidation |
| `auth.mfa_enabled` | MFA activated (after verify_mfa) |
| `auth.mfa_disabled` | MFA removed |
| `auth.token_revoked` | Token blacklisted |
| `auth.password_reset` | Password changed |

---

## Requirements

- **Java 11+** — zero external dependencies (pure JDK)
- Maven 3.x (for building; optional)

---

## Build

```bash
# Compile
javac -d target/classes $(find src -name "*.java")

# Package
printf 'Manifest-Version: 1.0\nMain-Class: com.ecoskiller.mcp.AuthServiceMCPServer\n\n' \
    > /tmp/manifest.mf
jar cfm target/mcp-auth-service.jar /tmp/manifest.mf -C target/classes .

# Or with Maven
mvn package -q
```

---

## Run

```bash
java -jar target/mcp-auth-service.jar
```

Transport: **stdio** (JSON-RPC 2.0). All logs → stderr; stdout = MCP messages only.

---

## Connect to Claude Desktop

```json
{
  "mcpServers": {
    "mcp-auth-service": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/target/mcp-auth-service.jar"],
      "env": {
        "AUTH_JWT_SECRET": "your-keycloak-realm-secret",
        "AUTH_STRICT_MODE": "false",
        "AUTH_ACCESS_TOKEN_TTL": "900",
        "AUTH_REFRESH_TOKEN_TTL": "604800",
        "AUTH_BCRYPT_COST": "12",
        "AUTH_MAX_LOGIN_ATTEMPTS": "5"
      }
    }
  }
}
```

---

## Run Tests

```bash
# Quick pass/fail (30 tests)
java -cp target/mcp-auth-service.jar com.ecoskiller.mcp.TestAgents

# With full JSON output
java -cp target/mcp-auth-service.jar com.ecoskiller.mcp.TestAgents --verbose
```

Expected: **30/30 PASSED**

---

## File Structure

```
mcp-auth-service/
├── pom.xml
├── README.md
├── claude_desktop_config.json
└── src/main/java/com/ecoskiller/mcp/
    ├── AuthServiceMCPServer.java        ← Entry point (stdio loop)
    ├── TestAgents.java                  ← 30-test integration suite
    ├── protocol/
    │   ├── MCPProtocolHandler.java      ← JSON-RPC 2.0 dispatcher
    │   └── JsonUtil.java                ← Zero-dependency JSON parser/serialiser
    ├── security/
    │   └── AuthSecurityConfig.java      ← bcrypt, JWT, TOTP, OAuth, RBAC, sessions,
    │                                       rate-limiting, sanitisation, audit
    ├── models/
    │   └── KafkaEventPublisher.java     ← Kafka event stubs (→ node-rdkafka in prod)
    └── tools/
        ├── AuthToolRouter.java          ← 18-tool dispatcher + tools/list definition
        ├── IdentityTools.java           ← register, login, logout, reset_password
        ├── TokenTools.java              ← refresh, validate, revoke, introspect
        ├── MfaTools.java                ← enable_mfa, verify_mfa, disable_mfa
        └── OAuthSessionRbacTools.java   ← OAuth, session, RBAC, audit (3 classes in 1 file)
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Production Migration Checklist

| Component | Dev | Production |
|---|---|---|
| Password hashing | HMAC-SHA256 (JDK-only) | bcryptjs (Node.js) / Spring Security BCrypt |
| JWT signing | HS256 dev-sig | RS256 with Keycloak private key (Vault-stored) |
| Token blacklist | In-memory LinkedHashMap | Redis `SET jti EX <ttl>` |
| Session store | In-memory ConcurrentHashMap | Redis Cluster `HSET session:<id>` |
| Refresh token store | In-memory Map | Redis `SET refresh:<jti> EX 604800` |
| Login rate-limit | In-memory counter | Redis `INCR login_attempts:<userId> EX 3600` |
| Audit log | In-memory List | Elasticsearch (structured JSON) |
| Kafka events | Logger stubs | node-rdkafka → topic `auth` (3 partitions) |
| MFA secret storage | In-memory Map | Vault / AWS Secrets Manager (encrypted) |
| OAuth client registry | Hardcoded validation | PostgreSQL `oauth_clients` table |
