# mcp-candidate-performance-aggregator

**Ecoskiller | candidate-performance-aggregator**  
MCP Server in Java | 20 Tools | Priority: HIGH | Namespace: ecoskiller/prod

The authoritative multi-track score consolidation layer of Ecoskiller.  
Aggregates GD + Interview + Dojo scores → normalized_score + belt level →  
publishes `candidate.performance.computed` to Kafka for certification-engine,  
analytics-service, notification-service, and xi-profile-service.

---

## Architecture Position

```
scoring-engine (upstream)
  ├─ match.scored          (Kafka, 3 partitions)   → DOJO trigger
  ├─ gd.scored.snapshot    (Kafka, 6 partitions)   → GD snapshot
  └─ interview.scored.snapshot (Kafka, 6 partitions) → Interview snapshot
           │
           ▼
┌──────────────────────────────────────────┐
│  candidate-performance-aggregator        │
│  Weighted agg + partial participation   │
│  Rubric normalization + belt mapping    │
│  Redis lock + event dedup              │
│  PostgreSQL + ClickHouse writes        │
└────────────────┬─────────────────────────┘
                 │
    candidate.performance.computed (Kafka, 6 partitions)
                 │
    ┌────────────┼────────────┬──────────────┐
    ▼            ▼            ▼              ▼
cert-engine  analytics   notif-svc   xi-profile-svc
```

---

## Belt Levels (normalized score)

| Belt | Score Range |
|------|-------------|
| BRONZE | 0 – 49 |
| SILVER | 50 – 69 |
| GOLD | 70 – 84 |
| PLATINUM | 85 – 100 |

Thresholds are rubric-configurable per tenant + job role.

---

## Tools (20)

### Score Ingestion
| # | Tool | Roles |
|---|------|-------|
| 1 | `ingest_track_score` | SERVICE_ACCOUNT, ADMIN |
| 2 | `ingest_kafka_event` | SERVICE_ACCOUNT, ADMIN |
| 3 | `ingest_gd_snapshot` | SERVICE_ACCOUNT, ADMIN |
| 4 | `ingest_interview_snapshot` | SERVICE_ACCOUNT, ADMIN |

### Aggregation
| # | Tool | Roles |
|---|------|-------|
| 5 | `compute_aggregate` | SERVICE_ACCOUNT, ADMIN |
| 6 | `retry_aggregation` | ADMIN only |
| 7 | `batch_compute_aggregates` | SERVICE_ACCOUNT, ADMIN |
| 8 | `get_aggregation_status` | Any (CANDIDATE: own only) |

### Rubric & Cycle Config
| # | Tool | Roles |
|---|------|-------|
| 9 | `configure_rubric` | ADMIN only |
| 10 | `get_rubric` | RECRUITER, ADMIN |
| 11 | `configure_cycle` | ADMIN only |
| 12 | `get_cycle_config` | RECRUITER, ADMIN |

### Query
| # | Tool | Roles |
|---|------|-------|
| 13 | `get_aggregate_result` | Any (CANDIDATE: own only) |
| 14 | `get_candidate_breakdown` | Any (CANDIDATE: own only) |
| 15 | `get_aggregation_history` | RECRUITER, ADMIN |
| 16 | `get_score_history` | Any (CANDIDATE: own only) |

### DLQ
| # | Tool | Roles |
|---|------|-------|
| 17 | `get_dlq_entries` | ADMIN only |
| 18 | `resolve_dlq_entry` | ADMIN only |

### Analytics
| # | Tool | Roles |
|---|------|-------|
| 19 | `get_metrics` | ADMIN, SERVICE_ACCOUNT |
| 20 | `get_audit_log` | ADMIN only |

---

## Aggregation Pipeline (compute_aggregate)

```
1. Check idempotency (return cached if already COMPUTED)
2. Acquire distributed lock (agg_lock:{candidate_id}:{cycle_id}, 30s TTL)
3. Read cycle config (Redis cycle_config:{cycle_id} → PostgreSQL fallback)
4. Read tenant rubric (Redis scoring_rubric:{tenant_id}:{job_role_id} → PostgreSQL fallback)
5. Fetch track scores for GD / INTERVIEW / DOJO
6. Detect partial participation → redistribute weights proportionally
7. Weighted aggregation: GD×w1 + Interview×w2 + Dojo×w3
8. Rubric normalization → normalized_score 0-100
9. Belt level mapping: BRONZE / SILVER / GOLD / PLATINUM
10. Build per_track_breakdown (score explainability — DPDPA 2023)
11. Persist to PostgreSQL (candidate_performance_aggregates + audit_trail)
12. Write to ClickHouse (perf_agg_audit_log — columnar, immutable)
13. Publish candidate.performance.computed to Kafka
14. Release distributed lock
```

---

## Partial Participation Weight Redistribution

When a candidate misses one or more tracks, weights are redistributed proportionally:

```
Example: Candidate has GD (35%) + Interview (40%), no Dojo (25%)
  Total participated weight = 0.35 + 0.40 = 0.75
  Redistributed GD weight   = 0.35 / 0.75 = 0.467
  Redistributed INT weight  = 0.40 / 0.75 = 0.533
  Dojo contribution         = 0 (absent, not penalized)
```

---

## Security Architecture

| Layer | Mechanism |
|---|---|
| Authentication | Keycloak Bearer JWT on every tool call |
| Authorization | RBAC: CANDIDATE / RECRUITER / ADMIN / SERVICE_ACCOUNT |
| Tenant isolation | `tenant_id` enforced on all operations (mirrors PostgreSQL RLS) |
| Event deduplication | `event_id` idempotency (Redis dedup:{event_id} TTL 48h) |
| Distributed lock | `agg_lock:{candidate_id}:{cycle_id}` SET NX EX 30s |
| Weight validation | GD + Interview + Dojo must sum to 1.0 ± 0.001 |
| Input sanitisation | SQL injection + XSS pattern detection |
| Score validation | 0 ≤ raw_score ≤ 100 enforced on ingestion |
| DLQ safety | Max 3 retries → DLQ after exponential backoff |
| DPDPA 2023 | Score explainability breakdown + ClickHouse audit (3-year retention) |

---

## Kafka Topics

| Topic | Direction | Description |
|---|---|---|
| `match.scored` | **IN** | Dojo score from scoring-engine (aggregation trigger) |
| `gd.scored.snapshot` | **IN** | GD score snapshot from scoring-engine |
| `interview.scored.snapshot` | **IN** | Interview score snapshot |
| `candidate.performance.computed` | **OUT** | Final aggregate → cert-engine, analytics, notif, xi |
| `candidate.performance.aggregated.failed` | **OUT** | Failure alert for ops |
| `candidate.performance.aggregated.dlq` | **OUT** | DLQ after 3 retries |

---

## Default Weights & Thresholds

| Parameter | Default | Env Var |
|---|---|---|
| GD weight | 0.35 (35%) | `CPA_DEFAULT_GD_WEIGHT` |
| Interview weight | 0.40 (40%) | `CPA_DEFAULT_INT_WEIGHT` |
| Dojo weight | 0.25 (25%) | `CPA_DEFAULT_DOJO_WEIGHT` |
| Distributed lock TTL | 30s | `CPA_LOCK_TTL_SECONDS` |
| Dedup cache TTL | 48h | `CPA_DEDUP_TTL_HOURS` |
| Max retries before DLQ | 3 | (hardcoded) |

---

## Environment Variables

| Variable | Default | Description |
|---|---|---|
| `CPA_JWT_SECRET` | `ecoskiller-cpa-dev-secret` | HMAC signing secret (dev) |
| `CPA_STRICT_MODE` | `false` | `true` = enforce RS256 JWT signature |
| `CPA_DEFAULT_GD_WEIGHT` | `0.35` | Default GD track weight |
| `CPA_DEFAULT_INT_WEIGHT` | `0.40` | Default interview track weight |
| `CPA_DEFAULT_DOJO_WEIGHT` | `0.25` | Default dojo track weight |
| `CPA_LOCK_TTL_SECONDS` | `30` | Distributed lock TTL |
| `CPA_DEDUP_TTL_HOURS` | `48` | Event deduplication cache TTL |

---

## Requirements

- **Java 11+** — zero external dependencies (pure JDK)
- Maven 3.x (optional — for build)

---

## Build

```bash
javac -d target/classes $(find src -name "*.java")
printf 'Manifest-Version: 1.0\nMain-Class: com.ecoskiller.mcp.CandidatePerformanceAggregatorServer\n\n' > manifest.mf
jar cfm target/mcp-candidate-performance-aggregator.jar manifest.mf -C target/classes .
```

## Run

```bash
java -jar target/mcp-candidate-performance-aggregator.jar
```

## Tests

```bash
java -cp target/mcp-candidate-performance-aggregator.jar com.ecoskiller.mcp.TestAgents
java -cp target/mcp-candidate-performance-aggregator.jar com.ecoskiller.mcp.TestAgents --verbose
```

Expected: **31/31 PASSED, 1 SKIPPED** (DLQ resolve skips when no DLQ entries exist)

---

## Connect to Claude Desktop

```json
{
  "mcpServers": {
    "mcp-candidate-performance-aggregator": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/target/mcp-candidate-performance-aggregator.jar"],
      "env": {
        "CPA_JWT_SECRET": "your-keycloak-realm-secret",
        "CPA_STRICT_MODE": "false",
        "CPA_DEFAULT_GD_WEIGHT": "0.35",
        "CPA_DEFAULT_INT_WEIGHT": "0.40",
        "CPA_DEFAULT_DOJO_WEIGHT": "0.25",
        "CPA_LOCK_TTL_SECONDS": "30",
        "CPA_DEDUP_TTL_HOURS": "48"
      }
    }
  }
}
```

---

## File Structure

```
mcp-candidate-performance-aggregator/
├── pom.xml
├── README.md
├── claude_desktop_config.json
└── src/main/java/com/ecoskiller/mcp/
    ├── CandidatePerformanceAggregatorServer.java  ← Entry point (stdio loop)
    ├── TestAgents.java                             ← 32-test integration suite
    ├── protocol/
    │   ├── MCPProtocolHandler.java                ← JSON-RPC 2.0 dispatcher
    │   └── JsonUtil.java                          ← Zero-dependency JSON parser
    ├── security/
    │   └── AggSecurityConfig.java                 ← JWT, RBAC, tenant isolation,
    │                                                  dedup, distributed lock,
    │                                                  weight validation, sanitisation
    ├── models/
    │   ├── AggModels.java                         ← BeltLevel, TrackScore,
    │   │                                             AggregateRecord, ScoringRubric,
    │   │                                             CycleConfig, DlqEntry
    │   ├── AggRepository.java                     ← Thread-safe store
    │   │                                             (→ PostgreSQL + Redis + ClickHouse)
    │   └── AggregationEngine.java                 ← Core aggregation logic:
    │                                                  weighted agg, partial participation,
    │                                                  rubric normalization, belt mapping
    └── tools/
        ├── AggToolRouter.java                     ← 20-tool dispatcher + tools/list
        ├── ScoreIngestionTools.java               ← ingest_track_score,
        │                                             ingest_kafka_event,
        │                                             ingest_gd_snapshot,
        │                                             ingest_interview_snapshot
        └── AllTools.java                          ← AggregationTools + RubricConfigTools
                                                      + QueryDlqTools (16 tools)
```

---

## Production Migration Checklist

| Component | Dev | Production |
|---|---|---|
| JWT verification | HS256 dev-sig | RS256 + Keycloak JWKS endpoint |
| Aggregate store | In-memory ConcurrentHashMap | PostgreSQL 15 with RLS (`candidate_performance_aggregates`) |
| Audit log | In-memory List | ClickHouse `ecoskiller.perf_agg_audit_log` (columnar, immutable) |
| Event dedup | In-memory LinkedHashSet | Redis `SET dedup:{event_id} NX EX 172800` |
| Distributed lock | In-memory ConcurrentHashMap | Redis `SET agg_lock:{candidate}:{cycle} NX EX 30` |
| Track score store | In-memory Map | PostgreSQL `ecoskiller.candidate_track_scores` |
| Rubric cache | In-memory Map | Redis `scoring_rubric:{tenant}:{role}` TTL 300s |
| Cycle config cache | In-memory Map | Redis `cycle_config:{cycle_id}` TTL 600s |
| Kafka consumer | Event payload parsing | KafkaJS consumer group `candidate-performance-aggregator-match.scored-consumer` |
| Kafka producer | In-memory log | KafkaJS → `candidate.performance.computed` (6 partitions) |
| DLQ | In-memory Map | Kafka `candidate.performance.aggregated.dlq` (3 partitions) |
| Metrics | In-memory counters | Prometheus prom-client@15.x + Grafana dashboards |
| Tracing | Logs only | OpenTelemetry / Jaeger W3C TraceContext propagation |
