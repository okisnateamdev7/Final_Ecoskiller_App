# dojo-match-engine-mcp

**Ecoskiller | CAT-DOJO — Real-Time Candidate-Interview Matching & Session Allocation**  
MCP Server in **Java 17** | **18 Agents** | **Secure** | Priority: HIGH

---

## Agents (18)

| # | Tool Name | Agent | Purpose |
|---|-----------|-------|---------|
| 1 | `find_match` | FIND_MATCH_AGENT | Core real-time matching algorithm (score, rank, return best match) |
| 2 | `candidate_queue` | CANDIDATE_QUEUE_AGENT | Redis Stream: dojo-candidates:waiting (ENQUEUE/DEQUEUE/REQUEUE) |
| 3 | `interviewer_availability` | INTERVIEWER_AVAILABILITY_AGENT | Real-time availability pool from Redis cache |
| 4 | `skill_compatibility_filter` | SKILL_COMPATIBILITY_FILTER_AGENT | Skill level filter: interviewer >= candidate level |
| 5 | `timezone_constraint_check` | TIMEZONE_CONSTRAINT_CHECK_AGENT | 08:00–18:00 local time validation per interviewer timezone |
| 6 | `fairness_optimizer` | FAIRNESS_OPTIMIZER_AGENT | Anti-bias: repeat prevention, round-robin, load balancing |
| 7 | `match_score_calculator` | MATCH_SCORE_CALCULATOR_AGENT | score = (skill*0.6) + (fairness*0.3) + (utilization*0.1) |
| 8 | `session_initiation` | SESSION_INITIATION_AGENT | WebRTC session URL + PostgreSQL session record creation |
| 9 | `no_show_handler` | NO_SHOW_HANDLER_AGENT | 60s timeout → cancel, requeue, publish dojo.match.noshown |
| 10 | `load_balancer` | LOAD_BALANCER_AGENT | Concurrent capacity pool management (add/remove from pool) |
| 11 | `kafka_event_publisher` | KAFKA_EVENT_PUBLISHER_AGENT | Publish dojo.match.created / dojo.match.noshown events |
| 12 | `match_result_publisher` | MATCH_RESULT_PUBLISHER_AGENT | Notify candidate-service + interviewer-service via HTTP |
| 13 | `fairness_audit_log` | FAIRNESS_AUDIT_LOG_AGENT | Immutable append-only fairness log (compliance / bias audit) |
| 14 | `metrics_collector` | METRICS_COLLECTOR_AGENT | Prometheus metrics: latency, utilization, fairness, no-show rate |
| 15 | `fallback_match_engine` | FALLBACK_MATCH_ENGINE_AGENT | Emergency cross-skill fallback when no perfect match found |
| 16 | `interview_history_tracker` | INTERVIEW_HISTORY_TRACKER_AGENT | Track previous pairings to enforce no-repeat constraint |
| 17 | `concurrent_capacity_manager` | CONCURRENT_CAPACITY_MANAGER_AGENT | Redis INCR/DECR for real-time concurrent slot tracking |
| 18 | `match_confidence_scorer` | MATCH_CONFIDENCE_SCORER_AGENT | HIGH/MEDIUM/LOW confidence band for HR dashboard |

---

## Security Features

| Layer | Implementation |
|-------|----------------|
| **Input Sanitization** | SQL injection pattern detection, XSS prevention, string length limits (1024 chars max) |
| **Tool Allowlist** | Explicit permit list — all unlisted tools denied by default |
| **HMAC-SHA256 Signing** | Request payload signing; key loaded from `DOJO_HMAC_KEY` env var |
| **Rate Limiting** | 120 calls/minute per tool (in-memory, per-JVM) |
| **Nesting Depth Limit** | Max 5 levels of JSON nesting per input |
| **Key Sanitization** | Alphanumeric + dash/underscore keys only |
| **Audit Logging** | Every tool call logged to stderr with timestamp + args keys |
| **Stdout Protection** | All logging goes to stderr; stdout is reserved exclusively for MCP JSON-RPC |

---

## Architecture

```
stdin (JSON-RPC 2.0)
        │
        ▼
DojoMcpServer.run()
        │
        ├── SecurityManager.validateRpcStructure()
        ├── SecurityManager.isAllowedTool()
        ├── SecurityManager.sanitizeInputs()
        ├── SecurityManager.auditLog()
        ├── SecurityManager.checkRateLimit()
        │
        ▼
   McpAgent.execute(sanitizedArgs)
        │
        ├── FindMatchAgent           → Matching algorithm
        ├── CandidateQueueAgent      → Redis Stream management
        ├── InterviewerAvailAgent    → Pool queries
        ├── SkillCompatFilter        → Skill filtering
        ├── TimezoneCheckAgent       → TZ validation
        ├── FairnessOptimizerAgent   → Bias prevention
        ├── MatchScoreCalcAgent      → Scoring formula
        ├── SessionInitiationAgent   → WebRTC + DB
        ├── NoShowHandlerAgent       → Timeout recovery
        ├── LoadBalancerAgent        → Capacity pool
        ├── KafkaEventPublisher      → Kafka topics
        ├── MatchResultPublisher     → HTTP callbacks
        ├── FairnessAuditLogAgent    → Immutable log
        ├── MetricsCollectorAgent    → Prometheus metrics
        ├── FallbackMatchEngine      → Emergency matching
        ├── InterviewHistoryTracker  → History + no-repeat
        ├── ConcurrentCapacityMgr    → Redis INCR/DECR
        └── MatchConfidenceScorer    → HR confidence band
        │
        ▼
stdout (JSON-RPC 2.0 response)
```

---

## Matching Algorithm

```
FindMatch(candidate):
  1. available = interviewers where current < max_concurrent AND today < max_daily
  2. skill_filtered = available where interviewer_skill[lang] >= candidate_level
  3. tz_filtered = skill_filtered where 08:00 <= local_time(interviewer.tz) < 18:00
  4. fair_filtered = tz_filtered where candidate NOT IN interviewer.previous_candidates
  5. score = (skill_match_ratio * 0.6) + (fairness_bonus * 0.3) + (utilization * 0.1)
  6. return top match | fallback if allow_fallback=true | UNAVAILABLE_INTERVIEWER error
```

**Time complexity:** O(I) where I = number of interviewers (< 500 typical)  
**Target latency:** < 5 seconds (99th percentile)

---

## Requirements

- **Java 17+**
- **Maven 3.8+**
- **No runtime external dependencies** beyond Gson (bundled in fat JAR)

---

## Build & Run

```bash
# Build fat JAR
mvn clean package -q

# Run MCP server (stdio transport)
java -jar target/dojo-match-engine-mcp-1.0.0.jar

# Set HMAC key (production — REQUIRED)
DOJO_HMAC_KEY=your-secret-256-bit-key java -jar target/dojo-match-engine-mcp-1.0.0.jar
```

---

## Run Tests

```bash
mvn test
```

Test coverage: 24 tests covering all 18 agents + full security layer.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "dojo-match-engine": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/dojo-match-engine-mcp-1.0.0.jar"],
      "env": {
        "DOJO_HMAC_KEY": "your-secret-key-here"
      }
    }
  }
}
```

---

## File Structure

```
dojo-match-engine-mcp/
├── pom.xml                                          ← Maven build (Java 17, Gson, fat JAR)
├── README.md
└── src/
    ├── main/java/com/ecoskiller/dojo/
    │   ├── DojoMcpServer.java                       ← MCP server entry point (JSON-RPC 2.0)
    │   ├── McpAgent.java                            ← Agent interface
    │   ├── security/
    │   │   └── SecurityManager.java                 ← HMAC, sanitization, allowlist, rate limit, audit
    │   └── agents/
    │       ├── BaseAgent.java                       ← Common scaffolding + DSL
    │       ├── Agents_2_to_9.java                   ← Agents 2–9 (Queue → NoShow)
    │       └── Agents_10_to_18.java                 ← Agents 10–18 (LoadBalancer → Confidence)
    └── test/java/com/ecoskiller/dojo/
        └── DojoMcpServerTest.java                   ← 24 tests (agents + security)
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Environment Variables

| Variable | Required | Description |
|----------|----------|-------------|
| `DOJO_HMAC_KEY` | **Recommended** | HMAC-SHA256 signing key. If not set, uses insecure dev default. |

---

## Kafka Topics

| Topic | Direction | Consumers |
|-------|-----------|-----------|
| `dojo.match.created` | OUT | candidate-service, interviewer-service, analytics-service |
| `dojo.match.noshown` | OUT | candidate-service (requeue), interviewer-service (reliability) |
| `dojo.session.started` | OUT | analytics-service |
| `dojo.session.completed` | OUT | scoring-pipeline |

---

## Prometheus Metrics

| Metric | Type | Description |
|--------|------|-------------|
| `dojo_matches_per_minute` | Counter | Match throughput rate |
| `dojo_match_latency_ms` | Histogram | p50/p95/p99 matching latency |
| `dojo_interviewer_utilization` | Gauge | Per-interviewer utilization % |
| `dojo_fairness_score` | Gauge | Global fairness score (0.0–1.0) |
| `dojo_noshown_rate` | Gauge | No-show rate % |
