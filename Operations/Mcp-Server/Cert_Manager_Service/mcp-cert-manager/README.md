# mcp-cert-manager

**EcoSkiller | Digital Certificate Lifecycle Management**
MCP Server in Java | 12 Agents | Priority: HIGH | Security: Defence-in-Depth

---

## Agents (12)

| # | Tool Name | Agent | Description |
|---|-----------|-------|-------------|
| 1 | `certificate_issue` | CERTIFICATE_ISSUE_AGENT | W3C VC issuance from Kafka scoring events — idempotent |
| 2 | `certificate_verify` | CERTIFICATE_VERIFY_AGENT | Public verification: signature + expiry + CSL revocation |
| 3 | `certificate_revoke` | CERTIFICATE_REVOKE_AGENT | Admin soft-delete with audit log + Kafka event |
| 4 | `certificate_renew` | CERTIFICATE_RENEW_AGENT | Renew on re-assessment or expiry (2-year validity) |
| 5 | `belt_eligibility_check` | BELT_ELIGIBILITY_AGENT | Multi-dimensional score → belt level + gap analysis |
| 6 | `blockchain_anchor` | BLOCKCHAIN_ANCHOR_AGENT | Hash anchoring on Fabric/Ethereum/Polygon, GDPR-compliant |
| 7 | `certificate_share` | CERTIFICATE_SHARE_AGENT | LinkedIn/Twitter/email/QR sharing with GDPR consent gate |
| 8 | `certificate_template_manage` | CERT_TEMPLATE_AGENT | Per-tenant SVG/PDF templates, versioned |
| 9 | `key_management` | KEY_MANAGEMENT_AGENT | Vault-backed RSA/ECDSA/EdDSA keys, quarterly rotation |
| 10 | `credential_status_list` | CREDENTIAL_STATUS_LIST_AGENT | Redis BitString CSL — O(1) revocation lookup |
| 11 | `certificate_audit_log` | CERT_AUDIT_LOG_AGENT | 7-year append-only audit (GDPR/DPDPA) |
| 12 | `certificate_analytics` | CERT_ANALYTICS_AGENT | Issuance, verification, anomaly, expiry forecasts |

---

## Belt Eligibility Thresholds

| Belt | Minimum Score |
|------|--------------|
| Bronze | ≥ 60% |
| Silver | ≥ 75% |
| Gold | ≥ 85% |
| Platinum | ≥ 92% |
| Diamond | ≥ 97% |

---

## Security Model

| Layer | Mechanism |
|-------|-----------|
| Input Sanitisation | Shell injection, path traversal, SQL injection, XSS all blocked |
| UUID Enforcement | Certificate IDs validated as UUID v4 — prevents spoofing |
| Belt Level Allowlist | Only bronze/silver/gold/platinum/diamond accepted |
| Revocation Reason Allowlist | Only 6 approved reasons (prevents arbitrary values) |
| Blockchain Network Allowlist | Only hyperledger-fabric/ethereum/polygon/none |
| Share Channel Allowlist | Only linkedin/twitter/email/qr/url |
| Sign Algorithm Allowlist | Only RS256/ES256/EdDSA |
| Scoring Range Validation | Score must be 0.0–100.0 |
| Rate Limiting | 100 req/60s per tool (sliding window) |
| Tenant Isolation | Cross-tenant certificate operations blocked at every layer |
| GDPR Consent Gate | Share operations require candidate_consent=true |
| Log Sanitisation | Control chars stripped; secrets masked |
| Key Security | Private keys never in code — Vault only, never logged |
| Error Isolation | Stack traces never returned to client |

---

## Requirements

- Java 17+
- Maven 3.8+ (for build)
- Python 3.8+ (for tests — no extra packages)

---

## Build

```bash
mvn package -q
# Produces: target/mcp-cert-manager.jar (~9 MB fat jar)
```

---

## Run

```bash
java -jar target/mcp-cert-manager.jar
```

Communicates via **stdin/stdout** — JSON-RPC 2.0.
All logs go to **stderr** (stdout reserved for protocol).

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-cert-manager": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-cert-manager/target/mcp-cert-manager.jar"],
      "env": {
        "VAULT_ADDR":      "https://vault.ecoskiller.com",
        "VAULT_TOKEN":     "${VAULT_TOKEN}",
        "POSTGRES_URL":    "jdbc:postgresql://...",
        "REDIS_URL":       "redis://redis.ecoskiller.com:6379",
        "KAFKA_BOOTSTRAP": "kafka.ecoskiller.com:9092"
      }
    }
  }
}
```

---

## Run Tests

```bash
mvn package -q
python3 test_agents.py            # 25 tests — pass/fail summary
python3 test_agents.py --verbose  # with full JSON output
```

---

## Certificate Lifecycle (EcoSkiller)

```
Scoring Engine ──kafka──► candidate-scored
                                │
                         cert-manager evaluates
                         belt eligibility
                                │
                    ┌──── score ≥ threshold? ────┐
                    │ YES                         │ NO
                    ▼                             ▼
             Issue W3C VC                   No certificate
             (RS256 signed)
                    │
            ┌───────┴───────┐
            ▼               ▼
        PostgreSQL         Redis CSL
        (store cert)       (fast lookup)
            │
     Optional blockchain
     hash anchoring
            │
     Kafka: certificate-issued
            │
     Notification Service
     (email / SMS)
```

---

## File Structure

```
mcp-cert-manager/
├── pom.xml
├── README.md
├── claude_desktop_config.json
├── test_agents.py                          ← 25 tests + 6 security tests
└── src/main/java/com/ecoskiller/mcp/cert/
    ├── server/
    │   └── CertManagerMcpServer.java       ← Main entrypoint, stdio event loop
    ├── tools/
    │   └── CertManagerTools.java           ← All 12 agent implementations
    ├── security/
    │   └── SecurityValidator.java          ← Validation, allowlists, rate limiting
    └── models/
        ├── JsonRpc.java                    ← JSON-RPC 2.0 envelope types
        ├── ToolResult.java                 ← MCP tools/call response
        └── ToolDefinition.java             ← MCP tool descriptor
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Environment Variables (secrets — never hardcode)

| Variable | Purpose |
|----------|---------|
| `VAULT_ADDR` | HashiCorp Vault address |
| `VAULT_TOKEN` | Vault authentication token |
| `POSTGRES_URL` | PostgreSQL JDBC URL |
| `POSTGRES_USER/PASSWORD` | DB credentials |
| `REDIS_URL` | Redis cluster URL |
| `KAFKA_BOOTSTRAP` | Kafka broker list |
| `BLOCKCHAIN_RPC_URL` | Ethereum/Polygon RPC endpoint |
| `NOTIFICATION_SVC_URL` | Internal notification service |
