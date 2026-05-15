# candidate-ranking-engine-mcp

**Ecoskiller Platform — Multi-Dimensional Candidate Scoring & Ranking Engine**  
MCP Server in **Java 17** | **18 Agents** | **Secure** | Priority: HIGH

Aggregates raw assessment signals (GD, Interview, Dojo Coding, XI Behavioral Intelligence) into  
normalized, weighted, multi-dimensional candidate scores. Powers recruiter leaderboards,  
shortlisting workflows, and belt certification triggers.

---

## Agents (18)

| # | Tool Name | Purpose |
|---|-----------|---------|
| 1 | `score_aggregator` | Multi-source score aggregation: GD + Interview + Dojo + XI → Redis running aggregate |
| 2 | `weighted_score_calculator` | Applies per-job weight matrix: composite = (gd×0.30)+(interview×0.35)+(dojo×0.25)+(xi×0.10) |
| 3 | `rank_computer` | Ordinal rankings across job cohort; publishes candidate.rank.computed Kafka event |
| 4 | `tie_resolver` | Deterministic tie-breaking: stddev ASC → source_count DESC → completed_at ASC |
| 5 | `dimension_score_normalizer` | Normalises all 8 dimensions (MIN_MAX / Z_SCORE / PERCENTILE) per cohort |
| 6 | `intelligence_signal_ingester` | Ingests XI behavioral vectors from intelligence.signal.emitted Kafka topic |
| 7 | `xi_vector_integrator` | Merges 8-dim XI signals into composite with bias-detection cross-check |
| 8 | `leaderboard_manager` | Redis Sorted Set leaderboard — sub-10ms P99 reads during live assessment windows |
| 9 | `shortlist_generator` | Filtered shortlists above score threshold + CSV export for offline review |
| 10 | `cohort_stats_calculator` | Mean, median, stddev, score distribution — included in all API responses |
| 11 | `kafka_event_consumer` | Manages 5 consumer groups with Avro validation, DLQ routing, manual offset commit |
| 12 | `kafka_event_publisher` | Publishes candidate.rank.computed + ranking.recomputed events with Avro validation |
| 13 | `dlq_handler` | DLQ monitoring, replay, Prometheus alert integration for all 3 DLQ topics |
| 14 | `belt_eligibility_evaluator` | Evaluates belt tier thresholds; triggers certification-engine via Kafka |
| 15 | `rank_recomputer` | Idempotent cohort recomputation with exactly-once guarantees and audit log |
| 16 | `score_correction_handler` | Processes assessment.score.corrected events — triggers corrective recomputation |
| 17 | `metrics_reporter` | Prometheus metrics: latency, Kafka lag, DLQ counts, HPA, health endpoint |
| 18 | `weight_matrix_manager` | Per-job weight matrices in job_scoring_config — versioned, immutable once frozen |

---

## Security Features (8 Layers)

| Layer | Implementation |
|-------|----------------|
| **Tool Allowlist** | 18-tool explicit permit list — deny-by-default |
| **Input Sanitisation** | SQL injection + XSS pattern detection, 2048-char string limit |
| **UUID Validation** | tenant_id, job_id, candidate_id validated against UUID regex |
| **Score Range Guard** | All scores validated 0.0 ≤ score ≤ 100.0 |
| **Kafka Topic Allowlist** | Only known Ecoskiller topics accepted |
| **Belt Tier Allowlist** | Only valid belt tiers (WHITE→BLACK) accepted |
| **HMAC-SHA256** | Request payload signing — key from `RANKING_MCP_HMAC_KEY` env var |
| **Rate Limiting** | 120 calls/minute per tool (thread-safe, in-memory) |
| **Audit Logging** | Every tool call → stderr with timestamp + arg keys |
| **Stdout Protection** | All logs → stderr; stdout exclusively for MCP JSON-RPC |

---

## Kafka Topics

### Consumed (5 inputs)
| Topic | Producer | Partitions |
|-------|----------|-----------|
| `gd.completed` | gd-orchestrator | 6 |
| `interview.completed` | interview-service | 6 |
| `match.scored` | scoring-engine | 3 |
| `intelligence.signal.emitted` | passive-intelligence-engine (XI) | 3 |
| `assessment.score.corrected` | admin-service | 3 |

### Published (2 outputs)
| Topic | Consumers | Retention |
|-------|-----------|-----------|
| `candidate.rank.computed` | certification-engine, notification-service, analytics-service | 30 days |
| `ranking.recomputed` | analytics-service, admin-service | 14 days |

### DLQ (3 dead-letter queues)
`gd.completed.dlq` · `interview.completed.dlq` · `match.scored.dlq`  
Prometheus alert fires for any count > 0 within a 5-minute window.

---

## Scoring Pipeline

```
gd.completed ──────────┐
interview.completed ───┤──→ score_aggregator ──→ weighted_score_calculator
match.scored ──────────┤                              │ (weight matrix from job_scoring_config)
intelligence.signal ───┘                              ↓
                                               xi_vector_integrator
                                                      │
                                               rank_computer
                                                      │
                                         ┌────────────┴────────────┐
                                   tie_resolver           belt_eligibility_evaluator
                                         │                         │
                                   leaderboard_manager    candidate.rank.computed (Kafka)
                                   shortlist_generator             │
                                                          certification-engine
```

---

## REST API (from service spec)

| Endpoint | Agent |
|----------|-------|
| `GET /api/v1/rankings/{job_id}` | `rank_computer` |
| `GET /api/v1/rankings/{job_id}/candidate/{candidate_id}` | `score_aggregator` |
| `GET /api/v1/rankings/{job_id}/shortlist?threshold=N` | `shortlist_generator` |
| `POST /api/v1/rankings/{job_id}/recompute` | `rank_recomputer` |
| `GET /api/v1/rankings/{job_id}/export` | `shortlist_generator` (export_csv=true) |
| `GET /api/v1/health` | `metrics_reporter` |
| `GET /metrics` | `metrics_reporter` |

---

## Build & Run

```bash
mvn clean package -q
RANKING_MCP_HMAC_KEY=your-256-bit-secret java -jar target/candidate-ranking-engine-mcp-1.0.0.jar
mvn test    # 28 tests
```

---

## Connect to Claude Desktop

```json
{
  "mcpServers": {
    "candidate-ranking-engine": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/candidate-ranking-engine-mcp-1.0.0.jar"],
      "env": { "RANKING_MCP_HMAC_KEY": "your-secret-key" }
    }
  }
}
```

---

## File Structure

```
candidate-ranking-engine-mcp/
├── pom.xml
├── README.md
└── src/
    ├── main/java/com/ecoskiller/ranking/
    │   ├── CandidateRankingMcpServer.java     ← Entry point (JSON-RPC 2.0 stdio)
    │   ├── McpAgent.java                      ← Agent interface
    │   ├── security/
    │   │   └── SecurityManager.java           ← 8-layer security
    │   └── agents/
    │       ├── BaseAgent.java                 ← Shared DSL + 8 dimension constants
    │       ├── CoreScoringAgents.java         ← Agents 1–6 (aggregator → XI ingester)
    │       ├── LeaderboardKafkaAgents.java    ← Agents 7–12 (XI integrator → Kafka publisher)
    │       └── BeltMetricsWeightAgents.java   ← Agents 13–18 (DLQ → weight matrix)
    └── test/java/com/ecoskiller/ranking/
        └── CandidateRankingMcpServerTest.java ← 28 tests
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`
