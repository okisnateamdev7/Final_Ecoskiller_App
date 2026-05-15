# mcp-ife

**Ecoskiller | CAT-XII — Idea Fingerprint Engine**  
MCP Server in Java | 14 Agents | Priority: HIGH

---

## What It Does

The Idea Fingerprint Engine (IFE) is the cryptographic and semantic foundation for Ecoskiller's
intellectual property protection layer. It computes deterministic SHA-3-256 fingerprints (Idea DNA),
generates 384-dim Sentence-BERT embeddings, and runs sub-100ms ANN similarity search against the
Qdrant vector corpus (1M+ ideas). It powers duplicate detection, plagiarism risk scoring, and
marketplace recommendations.

---

## Agents (14 Tools)

| # | Tool Name | Agent |
|---|-----------|-------|
| 1 | `compute_fingerprint` | SHA-3-256 Idea DNA fingerprinting (canonicalize → hash → store) |
| 2 | `compute_fingerprint_batch` | Async batch job for 10K+ ideas/day (returns job_id) |
| 3 | `generate_embedding` | 384-dim Sentence-BERT semantic embedding (L2-normalized) |
| 4 | `similarity_search` | Qdrant HNSW ANN cosine similarity, tenant-scoped, top-K |
| 5 | `verify_fingerprint` | Tamper detection — recompute and compare against stored record |
| 6 | `get_fingerprint` | Retrieve stored fingerprint + metadata by idea_id |
| 7 | `get_similarity_report` | Full duplicate / plagiarism report from persistence layer |
| 8 | `detect_plagiarism` | 4-level pipeline: exact → semantic → coordinated → behavior |
| 9 | `index_embedding` | Qdrant upsert with tenant-scoped payload filter |
| 10 | `delete_embedding` | Tombstone removal (GDPR / archive / tenant delete) |
| 11 | `reindex_tenant` | Full tenant re-index (weekly maintenance job) |
| 12 | `get_batch_job_status` | Poll async batch job progress (QUEUED/RUNNING/COMPLETED/FAILED) |
| 13 | `list_embedding_models` | Available Sentence-BERT models + latency + use-case |
| 14 | `compute_risk_score` | Weighted plagiarism formula: 0.4×exact + 0.3×semantic + … |

---

## Security Features

- **Tool allowlist** — only 14 named tools can be executed (no arbitrary dispatch)
- **Input size limits** — 50 KB max per text field; 500 idea max per batch
- **UUID validation** — all ID fields validated against strict UUID regex before dispatch
- **Injection guard** — SQL, XSS, and path-traversal patterns rejected at SecurityValidator
- **Embedding bounds** — vector dimension (384) and NaN/Inf values validated
- **Similarity score bounds** — all float inputs clamped [0.0, 1.0]
- **Logging to stderr** — stdout stays clean for MCP JSON-RPC protocol
- **SHA-3-256** — NIST-standard, collision-resistant, one-way fingerprinting (2^256 space)
- **Tenant isolation** — all queries scoped by tenant_id (maps to Qdrant payload filter + PostgreSQL RLS)

---

## Similarity Tiers (per IFE spec §11)

| Cosine Similarity | Tier |
|---|---|
| ≥ 0.90 | VERY_SIMILAR_LIKELY_DUPLICATE |
| 0.75 – 0.90 | RELATED_WORTH_REVIEWING |
| 0.50 – 0.75 | SOMEWHAT_RELATED_FOR_CONTEXT |
| < 0.50 | NOT_RELATED |

## Plagiarism Risk Tiers (per IFE spec §12)

| Risk Score | Classification | Action |
|---|---|---|
| ≥ 0.80 | FLAG | BLOCK submission |
| 0.60 – 0.79 | REVIEW | Flag for human review within 24h |
| 0.50 – 0.59 | SOFT_FLAG | Accept with internal note |
| < 0.50 | CLEAR | ACCEPT and publish |

---

## Requirements

- Java 17+
- Maven 3.8+ (for build)
- No external runtime dependencies (Jackson bundled in fat JAR)

---

## Build

```bash
cd mcp-ife-java
mvn clean package -q
# → target/mcp-ife-1.0.0.jar
```

---

## Run the Server

```bash
java -jar target/mcp-ife-1.0.0.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).  
All logs go to **stderr** — stdout is reserved for protocol messages.

---

## Run Tests

```bash
# Compile test class (included in fat JAR)
java -cp target/mcp-ife-1.0.0.jar com.ecoskiller.mcp.ife.TestAgents

# With verbose JSON output
java -cp target/mcp-ife-1.0.0.jar com.ecoskiller.mcp.ife.TestAgents --verbose
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-ife": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-ife-java/target/mcp-ife-1.0.0.jar"]
    }
  }
}
```

---

## File Structure

```
mcp-ife-java/
├── pom.xml                                        ← Maven build (Java 17, fat JAR)
├── claude_desktop_config.json                     ← Claude Desktop config snippet
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp/ife/
    │   ├── IdeaFingerprintMcpServer.java          ← Entry point, JSON-RPC dispatcher
    │   ├── handlers/
    │   │   ├── ToolRegistry.java                  ← 14-tool registry + MCP schema definitions
    │   │   ├── FingerprintHandler.java            ← SHA-3-256 Idea DNA (tools 1, 5, 6)
    │   │   ├── EmbeddingHandler.java              ← Sentence-BERT 384-dim embeddings (tool 3)
    │   │   ├── SimilarityHandler.java             ← Qdrant ANN search + reports (tools 4, 7)
    │   │   ├── PlagiarismHandler.java             ← 4-level detection + risk scorer (tools 8, 14)
    │   │   └── Handlers.java                     ← IndexHandler + BatchHandler + ModelHandler (tools 2, 9-13)
    │   ├── security/
    │   │   └── SecurityValidator.java             ← Input validation, allowlist, injection guard
    │   └── protocol/
    │       └── McpProtocol.java                   ← Protocol constants
    └── test/java/com/ecoskiller/mcp/ife/
        └── TestAgents.java                        ← Test all 14 agents (pass/fail + verbose)
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Production Integration (Ecoskiller Platform)

In production this server wraps calls to:

| IFE Tool | Production Backend |
|---|---|
| `compute_fingerprint` | Java `MessageDigest.getInstance("SHA3-256")` → PostgreSQL `innovation.fingerprints` |
| `generate_embedding`  | Python `sentence-transformers` service via gRPC |
| `similarity_search`   | Qdrant REST API (`/collections/idea-embeddings/points/search`) |
| `index_embedding`     | Qdrant upsert with `tenant_id` payload filter |
| Kafka events          | KafkaProducer → `fingerprint.computed`, `embedding.computed`, `similarity.analyzed`, `plagiarism.detected` |
| Persistence           | PostgreSQL 15 with RLS on `innovation.*` schema |
| Caching               | Redis 7 (fingerprint TTL 24h, embedding TTL 7d, search TTL 1h) |
