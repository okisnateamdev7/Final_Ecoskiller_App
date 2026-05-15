# mcp-idea-similarity-detector

**Ecoskiller | CAT-XII — idea-similarity-detector**  
MCP Server in Java | 13 Agents | Priority: HIGH  
Namespace: `analytics` (k3s · GCP asia-south1 + AWS ap-south-1)

---

## What It Does

The `idea-similarity-detector` is the **IP anti-theft enforcement layer** of Ecoskiller's Innovation
Engine (Module XII). It occupies Stage 3 in the 7-service pipeline — activated asynchronously by
the `idea.fingerprinted` Kafka event emitted upstream by `idea-dna-fingerprint-engine`.

Core function:
> Execute a tenant-scoped ANN vector similarity search against the Qdrant `idea-embeddings`
> collection, compute a weighted copy-probability score, classify into CLEAR/REVIEW/FLAG,
> persist the result to PostgreSQL, and publish the appropriate Kafka event to trigger
> downstream workflows.

**Zero-silent-failure guarantee**: No idea submission can bypass similarity checking.
The three-retry + DLQ pattern with Prometheus alert ensures all failures are surfaced.

---

## Agents (13 Tools)

| # | Tool | Implements |
|---|------|-----------|
| 1 | `run_similarity_check` | ANN top-K=20 + weighted score + CLEAR/REVIEW/FLAG + Kafka publish |
| 2 | `run_similarity_recheck` | Idempotent re-execution: overwrites report, archives previous to audit |
| 3 | `get_similarity_report` | Retrieve `innovation.idea_similarity_reports` record (RLS enforced) |
| 4 | `get_similarity_audit` | Append-only recheck audit trail from `innovation.idea_similarity_audit` |
| 5 | `list_similarity_reports` | Paginated tenant reports with classification + category filter |
| 6 | `get_classification` | CLEAR/REVIEW/FLAG with licensing eligibility + dispute path |
| 7 | `compute_copy_probability` | Direct formula from signals (what-if + threshold tuning) |
| 8 | `get_top_matches` | Top-K similar ideas from cached report or live ANN search |
| 9 | `get_threshold_config` | Per-tenant Redis cached thresholds (TTL 5 min) |
| 10 | `set_threshold_config` | Update thresholds with ordering validation (admin) |
| 11 | `get_dlq_status` | DLQ count + retry policy + Prometheus alert state |
| 12 | `get_consumer_health` | Kafka consumer lag + Qdrant status + HPA config |
| 13 | `get_similarity_metrics` | Prometheus metrics snapshot (histograms, counters, gauges) |

---

## Pipeline Position

```
Stage 1  → idea-timestamp-service         SHA-256 + MinIO + Kafka idea.submitted
Stage 2  → idea-dna-fingerprint-engine    Embedding → Qdrant + Kafka idea.fingerprinted
Stage 3  → idea-similarity-detector   ←  THIS SERVICE
   CLEAR → idea-registry-service          VERIFIED_ORIGINAL → marketplace eligible
   REVIEW→ admin-service                  Human review queue (24h SLA)
   FLAG  → dispute-service               Dispute workflow initiated
Stage 4  → idea-marketplace-service       Recruiter discovery (OpenSearch)
Stage 5  → licensing-contract-service     WORM lock + royalty engine (Module XIII)
```

---

## Copy-Probability Formula

Per ISD spec §3.3:

```
copy_probability = 0.60 × max_cosine_similarity
                 + 0.25 × min(1.0, above_medium_count / 10)
                 + 0.15 × category_match_rate

Clamped to [0.0, 1.0]
```

| Score | Classification | Action |
|-------|---------------|--------|
| < 0.60 | **CLEAR** | `idea.similarity.cleared` → VERIFIED_ORIGINAL |
| 0.60–0.79 | **REVIEW** | `idea.similarity.review` → admin queue, 24h human review |
| ≥ 0.80 | **FLAG** | `idea.similarity.flagged` → dispute workflow |

Thresholds are configurable per tenant (Redis TTL 5 min cache via admin-service).

---

## Security Features

| Layer | Implementation |
|-------|---------------|
| Tool allowlist | 13 tools only — unknown tool names rejected |
| UUID validation | All ID fields: `idea_id`, `tenant_id`, `candidate_id`, `vector_id`, `report_id` |
| Probability bounds | All cosine/probability values validated to `[0.0, 1.0]` |
| top_k bounds | `1–100` enforced |
| Threshold ordering | `flag_threshold > review_threshold > min_similarity_floor` |
| Category injection | Length limit + injection pattern guard |
| Pagination bounds | `limit: 1–500`, `page: 0–10000` |
| SQL/XSS/path guard | Injection regex on all string inputs |
| Logs to stderr | stdout clean for MCP JSON-RPC |

---

## DLQ & Retry Policy (spec Appendix B)

```
Consumer group: idea-similarity-detector-idea.fingerprinted-consumer
Retry 1: after 1s
Retry 2: after 4s
Retry 3: after 16s
→ DLQ: idea.fingerprinted.dlq

Alert: Prometheus fires on DLQ count > 0 within 5-minute window
```

---

## Kafka Events Published

| Event | Condition | Consumers |
|-------|-----------|-----------|
| `idea.similarity.cleared` | copy_probability < 0.60 | idea-registry-service, notification-service, analytics-service |
| `idea.similarity.review` | 0.60–0.79 | admin-service, notification-service |
| `idea.similarity.flagged` | ≥ 0.80 | notification-service, admin-service, dispute-service |

---

## Requirements

- Java 17+

---

## Build & Run

```bash
JACKSON_CP="/usr/share/java/jackson-databind.jar:/usr/share/java/jackson-core.jar:/usr/share/java/jackson-annotations.jar"
find src/main/java -name "*.java" > /tmp/sources.txt
javac -source 17 -target 17 -cp "$JACKSON_CP" -d target/classes @/tmp/sources.txt

java -jar target/mcp-idea-similarity-detector-1.0.0.jar
```

---

## Run Tests

```bash
java -cp target/mcp-idea-similarity-detector-1.0.0.jar com.ecoskiller.mcp.isd.TestAgents
java -cp target/mcp-idea-similarity-detector-1.0.0.jar com.ecoskiller.mcp.isd.TestAgents --verbose
```

---

## Connect to Claude Desktop

```json
{
  "mcpServers": {
    "mcp-idea-similarity-detector": {
      "command": "java",
      "args": ["-jar", "/path/to/mcp-idea-similarity-detector-1.0.0.jar"]
    }
  }
}
```

---

## File Structure

```
mcp-isd-java/
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp/isd/
    │   ├── IdeaSimilarityDetectorMcpServer.java   ← Entry point, JSON-RPC dispatcher
    │   ├── engine/
    │   │   ├── VectorEngine.java                  ← ANN search simulation (Qdrant HNSW)
    │   │   └── ScoringEngine.java                 ← Copy-probability formula + classification
    │   ├── handlers/
    │   │   ├── ToolRegistry.java                  ← 13-tool registry + MCP schema
    │   │   ├── SimilarityCheckHandler.java        ← run_similarity_check, run_similarity_recheck
    │   │   ├── ReportHandler.java                 ← get_similarity_report, audit, list
    │   │   ├── ClassificationHandler.java         ← get_classification, compute_copy_probability, top_matches
    │   │   ├── ThresholdHandler.java              ← get/set_threshold_config (Redis simulation)
    │   │   └── OperationsHandler.java             ← DLQ, consumer health, Prometheus metrics
    │   └── security/
    │       └── SecurityValidator.java             ← 13-tool allowlist + all input validation
    └── test/java/com/ecoskiller/mcp/isd/
        └── TestAgents.java                        ← 28-test suite
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)  
- Format: **JSON-RPC 2.0**  
- MCP Version: **2024-11-05**  
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`
