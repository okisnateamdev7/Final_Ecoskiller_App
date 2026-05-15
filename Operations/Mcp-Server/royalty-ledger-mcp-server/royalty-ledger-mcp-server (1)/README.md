# mcp-royalty-ledger-ecoskiller

**Ecoskiller | CAT-ROYALTY-LEDGER — Intellectual Property Rights & Royalty Accrual Tracking**  
MCP Server in Java | 16 Agents | Priority: HIGH | Namespace: revenue

---

## Overview

Secure Java MCP server implementing the full `royalty-ledger-service` specification for the Ecoskiller platform. Covers IP ownership registration (Ed25519/RSA-2048), real-time royalty accrual with tier-based rates, immutable ClickHouse double-entry ledger, payout orchestration, TDS/GST tax compliance, fraud detection, dispute resolution, multi-contributor split management, and DPDPA 2023 / SOC 2 audit logging.

Built from `royalty-ledger-service-technical-documentation.docx` (v2.0, March 2026).

---

## Agents (16)

| # | Tool Name | Core Responsibility |
|---|-----------|-------------------|
| 1 | `ip_register` | Register IP with Ed25519/RSA-2048 signature; multi-contributor co-creation splits |
| 2 | `ip_details_get` | Retrieve IP from Redis cache (TTL 5min) with ownership chain history |
| 3 | `royalty_accrue` | Real-time accrual: base_rate × quality × tier_multiplier × difficulty; double-entry ledger; self-submission fraud guard |
| 4 | `ledger_entries_query` | Query ClickHouse immutable ledger; 7-year retention; DPDPA 2023 compliant |
| 5 | `creator_balance_get` | Real-time balance from Redis; payout eligibility (≥₹5k threshold) |
| 6 | `payout_request` | Initiate payout; TDS withholding (10%/30%/0%); calls payment-distribution-engine |
| 7 | `payout_status_get` | Track payout lifecycle: pending→processing→completed/failed; reconciliation |
| 8 | `tax_compliance_calculate` | TDS/GST calculation; Form 26AS statement; NEFT return for RTO; exemption registry |
| 9 | `ip_challenge_submit` | Ownership dispute with evidence hash; 30-day auto-resolve; retroactive recalculation |
| 10 | `split_config_manage` | Co-creator split adjustment; multi-sig approval; version history |
| 11 | `fraud_detection_check` | Self-submission, bot velocity (>100/min), earnings spike (>500% WoW), quality anomaly |
| 12 | `creator_tier_manage` | Auto-tier by earnings+rating; Tier3=2.0x rates; demotion on fraud/disputes |
| 13 | `royalty_rate_manage` | Per-IP/tier rate table; retroactive rate changes; Redis cache invalidation |
| 14 | `earnings_report` | ClickHouse reports: monthly, quarterly tax, IP breakdown, trend analysis, top performers |
| 15 | `service_health` | Health check: PostgreSQL, ClickHouse, Redis, Kafka; Prometheus metrics summary |
| 16 | `audit_log_query` | ClickHouse immutable audit trail; 7-year retention; DPDPA right-to-access |

---

## Royalty Calculation Model

```
royalty = base_rate × quality_score × usage_multiplier × difficulty_multiplier × participant_bonus
```

| IP Type | Tier 1 | Tier 2 | Tier 3 |
|---------|--------|--------|--------|
| Coding Problem | ₹3/submission | ₹5 (+66%) | ₹8 (+166%) |
| Interview Question | ₹10/usage | ₹20 (+100%) | ₹50 (+400%) |
| Group Discussion Scenario | ₹20/session | ₹40 (+100%) | ₹100 (+400%) |
| Idea/Innovation | Flat fee ₹1k–₹1L | — | — |

**Tier criteria:** Tier1=0-₹5k | Tier2=₹5k-₹25k + 4.0★ | Tier3=₹25k+ + 4.3★

---

## Tax Compliance

| Creator Type | TDS Rate | Condition |
|-------------|----------|-----------|
| Individual | 10% | Earnings > ₹50k/quarter |
| Foreign/Non-resident | 30% | Always |
| Corporate | 0% TDS + 18% GST | Always |
| Nonprofit / Government | 0% | With valid exemption certificate |

---

## Security Architecture

| Layer | Implementation |
|-------|---------------|
| **JWT Validation** | HMAC-SHA256 (HS256), 5-min cache by SHA-256 token hash, constant-time comparison |
| **Content Hash Validation** | Strict `sha256:|sha512:` prefix + hex digest allowlist |
| **IP Type Validation** | Enum allowlist: problem\|interview_question\|discussion_scenario\|idea\|premium_content |
| **Account Type Validation** | Enum: creator_balance\|platform_earnings\|tax_withholding\|... |
| **Self-Submission Fraud** | In-agent check: creator_id == user_id → void accrual |
| **Rate Limiting** | Token-bucket: 100 req/sec, 1000 req/min per client |
| **Payload Guard** | 64KB cap + null byte detection |
| **Audit Logging** | Structured to stderr (ELK-ready); 2000-event rolling buffer; log injection prevented |
| **Secrets** | All from env vars / Kubernetes Secrets; loud warning if default used |
| **Container** | Non-root (UID 1000), read-only root FS, all capabilities dropped |
| **Network** | NetworkPolicy: PostgreSQL (5432), ClickHouse (9000/9440), Redis (6379), Kafka (9092) |

---

## Kafka Events

| Produced Event | Consumed By |
|---------------|-------------|
| `royalty.accrued` | payment-distribution-engine, notification-service, analytics-service |
| `payout.scheduled` | notification-service |
| `payout.completed` | notification-service (email/SMS to creator) |
| `IP.ownership_disputed` | admin-service (arbitration) |
| `creator.tiered_up` | notification-service (motivational) |

| Consumed Event | Producer |
|---------------|---------|
| `problem.used` | dojo-match-engine |
| `interview_question.used` | interview-service |
| `discussion_scenario.used` | gd-orchestrator |
| `idea.licensed` | licensing-service |
| `content_quality.rated` | assessment-service |

---

## Build & Run

```bash
# Build fat JAR
mvn package -DskipTests
# → target/royalty-ledger-mcp-server-1.0.0.jar

# Set required secrets
export ROYALTY_JWT_SECRET="your-keycloak-jwt-secret-min-32-chars"

# Run
java -jar target/royalty-ledger-mcp-server-1.0.0.jar

# Test (28 tests)
mvn test
```

## Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:
```json
{
  "mcpServers": {
    "mcp-royalty-ledger-ecoskiller": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/target/royalty-ledger-mcp-server-1.0.0.jar"],
      "env": { "ROYALTY_JWT_SECRET": "your-secret" }
    }
  }
}
```

---

## Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `ROYALTY_JWT_SECRET` | ✅ | — | Keycloak HMAC-SHA256 JWT signing secret |
| `ROYALTY_DOMAIN` | No | `ecoskiller.io` | Platform domain |
| `ROYALTY_DB_HOST` | No | `postgres.revenue.svc.cluster.local` | PostgreSQL host |
| `ROYALTY_CLICKHOUSE_HOST` | No | `clickhouse.revenue.svc.cluster.local` | ClickHouse host |
| `ROYALTY_REDIS_ENABLED` | No | `true` | Enable Redis cache |
| `ROYALTY_PAYOUT_THRESHOLD` | No | `5000` | Minimum balance for payout (₹) |
| `ROYALTY_TDS_RATE_INDIVIDUAL` | No | `10` | TDS rate for individual creators (%) |
| `ROYALTY_TDS_RATE_FOREIGN` | No | `30` | TDS rate for foreign creators (%) |
| `ROYALTY_GST_RATE` | No | `18` | GST rate for corporate creators (%) |
| `ROYALTY_FRAUD_VELOCITY_LIMIT` | No | `100` | Max submissions/min before fraud flag |
| `ROYALTY_MAX_DAILY_SUBMISSIONS` | No | `50` | Max total daily submissions per creator |

---

## File Structure

```
royalty-ledger-mcp-server/
├── pom.xml
├── Dockerfile
├── k8s-deployment.yaml
├── claude_desktop_config.json
├── README.md
└── src/
    ├── main/java/io/ecoskiller/royalty/
    │   ├── server/RoyaltyLedgerMcpServer.java    ← Main entry point, JSON-RPC 2.0
    │   ├── agents/
    │   │   ├── AgentBase.java                    ← AgentHandler interface + BaseAgent
    │   │   ├── CoreAgents.java                   ← Agents 1-5: IP, accrue, ledger, balance
    │   │   ├── PayoutAgents.java                 ← Agents 6-10: payout, tax, challenge, split
    │   │   └── AdminAgents.java                  ← Agents 11-16: fraud, tier, rate, report, health, audit
    │   ├── security/Security.java                ← InputSanitizer, JwtValidator, RateLimiter, AuditLogger, RequestValidator
    │   └── config/ServerConfig.java              ← All config from env vars
    └── test/java/io/ecoskiller/royalty/
        └── RoyaltyLedgerMcpServerTest.java       ← 28 tests
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- All stdout: pure JSON-RPC | All logs: stderr (ELK-ready)

---

*Ecoskiller Royalty Ledger MCP Server v1.0.0 | March 2026*  
*CAT-ROYALTY-LEDGER | Namespace: revenue | Tier: 2 (High Priority)*
