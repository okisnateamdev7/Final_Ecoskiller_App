# tie-mcp-server

**Ecoskiller | CAT-TIE — Talent Intelligence Engine**  
MCP Server in Java 17 | 15 Agents | Priority: HIGH | Secure

---

## Overview

`tie-mcp-server` is a **secure, production-grade Java MCP server** for Ecoskiller's  
Talent Intelligence Engine (TIE) — the AI/ML inference layer that powers  
candidate-to-job matching, predictive hiring outcomes, and talent analytics.

**Transport:** stdio (stdin/stdout)  
**Protocol:** JSON-RPC 2.0 / MCP 2024-11-05  
**Runtime:** Java 17+  
**Security:** JWT (Keycloak RS256), RBAC, Rate Limiting, Structured Audit Logging

---

## Agents (15)

| # | Tool Name | Agent | Role Access |
|---|-----------|-------|-------------|
| 1 | `candidate_job_match` | Ensemble ML match scoring (MLP + XGBoost + LightGBM) | All |
| 2 | `batch_score` | Batch 100K+ candidate evaluations vs all open roles | ml_engineer, admin |
| 3 | `predict_offer_acceptance` | XGBoost: probability candidate accepts an offer | All |
| 4 | `predict_retention` | LightGBM: 12-month retention probability | All |
| 5 | `predict_training_duration` | Ramp-up time & phased training cost estimation | All |
| 6 | `talent_market_benchmark` | Salary P10–P90, skill demand index, scarcity score | All |
| 7 | `skill_gap_analysis` | Gap detection with GNN transfer skills & training paths | All |
| 8 | `bias_detection` | EEOC 4/5ths rule, p-value, fairness-aware re-ranking | ml_engineer, admin |
| 9 | `feature_engineering` | Extract versioned ML features → MLflow Feature Store | ml_engineer, admin |
| 10 | `model_deploy` | Stage → canary → production promotion with ONNX export | ml_engineer, admin |
| 11 | `model_evaluate` | AUC-ROC, precision@k, calibration, fairness on holdout | ml_engineer, admin |
| 12 | `ab_test_control` | Shadow deployment: create, status, promote, rollback | ml_engineer, admin |
| 13 | `explainability_shap` | SHAP attributions for any TIE prediction (EU AI Act §13) | All |
| 14 | `talent_graph_query` | GNN skill knowledge graph: related skills, transfer paths | All |
| 15 | `compliance_audit` | GDPR, DPDP, EU AI Act audit reports → Kafka | ml_engineer, admin |

---

## Security Architecture

```
Client Request (JSON-RPC)
       │
       ▼
┌─────────────────────────────┐
│  1. JWT Validation          │  ← Keycloak RS256 (JWKS endpoint)
│     • Signature verify      │    TIE_JWT_BYPASS=true for dev
│     • Expiry check          │
│     • Issuer verify         │
├─────────────────────────────┤
│  2. Rate Limiting           │  ← Per-client sliding window
│     • 100 req / 60s default │    Configurable via ENV
├─────────────────────────────┤
│  3. RBAC                    │  ← Per-tool role enforcement
│     • admin                 │    Roles extracted from JWT
│     • ml_engineer           │    realm_access.roles (Keycloak)
│     • recruiter             │
│     • viewer                │
├─────────────────────────────┤
│  4. Input Validation        │  ← Per-tool validateArgs()
│     • Required field check  │
│     • Type enforcement      │
├─────────────────────────────┤
│  5. Audit Logging           │  ← Structured JSON lines → stderr
│     • Every call logged     │    + optional file (TIE_AUDIT_LOG_FILE)
│     • client, action,       │    Shipped to compliance-audit-service
│       outcome, latency      │    via Kafka in production
└─────────────────────────────┘
       │
       ▼
  Tool Execution
```

### JWT Setup (Production)

1. Uncomment `jose4j` in `pom.xml`
2. Set `KEYCLOAK_ISSUER` env var to your Keycloak realm URL
3. Wire JWKS endpoint into `JwtValidator.verifyToken()` — stub is clearly marked `TODO`
4. Remove `TIE_JWT_BYPASS=true` from all production configs

### Keycloak Realm Roles Required
| Role | Capabilities |
|------|-------------|
| `admin` | All 15 tools + force-deploy override |
| `ml_engineer` | 15 tools incl. model ops, bias, compliance |
| `recruiter` | 7 read-only tools: match, predictions, benchmarks, SHAP, graph |
| `viewer` | Same as recruiter |

---

## Requirements

- **Java 17+**
- **Maven 3.8+** (for build)
- No runtime dependencies beyond Jackson (bundled in fat JAR)

---

## Build

```bash
# Build fat JAR
mvn clean package -q

# Output: target/tie-mcp-server-1.0.0.jar
```

---

## Run

```bash
# Dev mode (JWT bypass)
TIE_JWT_BYPASS=true java -jar target/tie-mcp-server-1.0.0.jar

# Production mode
KEYCLOAK_ISSUER=https://auth.ecoskiller.com/realms/ecoskiller \
TIE_AUDIT_LOG_FILE=/var/log/ecoskiller/tie-audit.jsonl \
java -jar target/tie-mcp-server-1.0.0.jar
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "tie-mcp-server": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/tie-mcp-server/target/tie-mcp-server-1.0.0.jar"],
      "env": {
        "TIE_JWT_BYPASS": "true"
      }
    }
  }
}
```

---

## Run Tests

```bash
mvn test

# Expected: 27 tests, all PASS
# Covers: 15 tool schemas, 15 happy-path executions,
#         input validation, RateLimiter, RBAC
```

---

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `TIE_JWT_BYPASS` | `false` | Set `true` to skip JWT validation (dev only!) |
| `KEYCLOAK_ISSUER` | `https://auth.ecoskiller.com/realms/ecoskiller` | Keycloak realm issuer URL |
| `TIE_AUDIT_LOG_FILE` | _(stderr only)_ | Path to write audit JSON lines |
| `TIE_RATE_LIMIT_MAX` | `100` | Max requests per window per client |
| `TIE_RATE_LIMIT_MS` | `60000` | Rate limit window in milliseconds |

---

## File Structure

```
tie-mcp-server/
├── pom.xml                                      ← Maven build (Java 17, fat JAR)
├── README.md
├── claude_desktop_config.json                   ← Claude Desktop config snippet
└── src/
    ├── main/java/com/ecoskiller/tie/mcp/
    │   ├── TIEMCPServer.java                    ← Main server (stdio JSON-RPC loop)
    │   ├── MCPTool.java                         ← Tool contract interface
    │   ├── security/
    │   │   ├── JwtValidator.java                ← Keycloak JWT validation
    │   │   ├── AuditLogger.java                 ← Structured JSON audit log
    │   │   └── RateLimiter.java                 ← Per-client sliding-window limiter
    │   └── tools/
    │       ├── BaseTool.java                    ← Abstract base: schema helpers, role sets
    │       └── ToolRegistry.java                ← Factory + all 15 tool implementations
    └── test/java/com/ecoskiller/tie/mcp/
        └── TIEMCPServerTest.java                ← 27-test suite (JUnit 5)
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

### JWT in Requests

Pass Bearer token in any of these locations (priority order):
1. `params._meta.auth.token` — MCP convention
2. `params.clientInfo.token` — Claude Desktop fallback
3. `params.arguments._bearer` — Per-tool call bearer

---

## Kafka Integration

Tools that publish events use these topics (wired in production):

| Tool | Kafka Topic |
|------|-------------|
| `candidate_job_match` | `talent.match.computed` |
| `predict_offer_acceptance` | `talent.prediction.outcome` |
| `predict_retention` | `talent.prediction.outcome` |
| `bias_detection` | `talent.bias.alert` |
| `compliance_audit` | `compliance.audit.report` |

---

## Production Checklist

- [ ] Replace `JwtValidator.verifyToken()` stub with real JWKS RS256 (jose4j)
- [ ] Set `KEYCLOAK_ISSUER` env var in Kubernetes Secret
- [ ] Set `TIE_AUDIT_LOG_FILE` and ship logs to compliance-audit-service via Filebeat
- [ ] Deploy behind NGINX Ingress with ModSecurity WAF (OWASP CRS 3.x)
- [ ] Enable Trivy scan in GitLab CI (blocks on CRITICAL/HIGH CVEs)
- [ ] Wire Kafka producer in tools that publish events (see Kafka Integration above)
- [ ] Wire Redis cache reads in `candidate_job_match` for <50ms batch score lookups
- [ ] Replace stub ML scores with real ONNX Runtime inference calls
