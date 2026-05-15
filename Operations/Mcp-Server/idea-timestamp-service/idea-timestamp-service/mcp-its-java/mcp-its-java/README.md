# mcp-idea-timestamp-service

**Ecoskiller | CAT-XII — idea-timestamp-service**  
MCP Server in Java | 12 Agents | Priority: HIGH  
Namespace: `core` (k3s · GCP asia-south1 + AWS ap-south-1)

---

## What It Does

The `idea-timestamp-service` is the **foundational IP timestamping authority** for Ecoskiller's
Innovation Engine (Module XII). It is Stage 1 in the 7-service pipeline — every candidate idea
must pass through it before fingerprinting, similarity detection, or marketplace listing.

Its exclusive mandate:

> Accept a candidate idea submission → generate a tamper-evident, verifiable timestamp record
> → persist it immutably → signal readiness to the rest of the platform via Kafka.

**Key guarantee:** The `submitted_at` timestamp is anchored to the PostgreSQL transaction clock
(NOT the client's reported time). Combined with the SHA-256 content hash, this constitutes
legally defensible proof of prior art for IP disputes and patent proceedings.

---

## Agents (12 Tools)

| # | Tool | Implements |
|---|------|-----------|
| 1 | `submit_idea` | SHA-256 hash + authoritative PostgreSQL timestamp + MinIO archive + Kafka `idea.submitted` |
| 2 | `revise_idea` | Versioned re-submission (new hash, new timestamp, append-only version record) |
| 3 | `get_timestamp_proof` | Immutable proof record: `submitted_at`, `content_hash`, `minio_etag` (IP evidence) |
| 4 | `list_idea_versions` | All versions from `innovation.idea_versions` (append-only table) |
| 5 | `verify_idea_integrity` | SHA-256 tamper detection — MATCH or MISMATCH with `tamper_evidence` |
| 6 | `get_submission_status` | Lifecycle: `PENDING_FINGERPRINT → VERIFIED_ORIGINAL | UNDER_REVIEW | FLAGGED | LICENSED | ARCHIVED` |
| 7 | `set_visibility` | Update `PRIVATE | INTERNAL | MARKETPLACE` + triggers search-indexer |
| 8 | `archive_idea` | Soft-archive (7-year record retention; existing licenses grandfathered) |
| 9 | `check_rate_limit` | Redis INCR/EXPIRE counter: `tenant:{id}:candidate:{id}:submissions:{date}` |
| 10 | `get_submission_audit` | Append-only audit log (`innovation.idea_submission_audit`) with event_type filter |
| 11 | `verify_minio_object` | MinIO ETag (MD5) dual-layer tamper check (secondary integrity channel) |
| 12 | `get_idea_ownership` | Canonical ownership record from `innovation.ideas` (RLS enforced) |

---

## Idea Lifecycle Pipeline (ITS Position)

```
Stage 1  → idea-timestamp-service        SHA-256 + timestamp + MinIO + Kafka idea.submitted
Stage 2  → idea-dna-fingerprint-engine   Embedding + Qdrant indexing
Stage 3  → idea-similarity-detector      ANN search + CLEAR/REVIEW/FLAG classification
Stage 4A → CLEAR → idea-registry-service VERIFIED_ORIGINAL — eligible for marketplace
Stage 4B → REVIEW/FLAG → admin-service   Human review / dispute workflow
Stage 5  → idea-marketplace-service      Recruiter discovery via OpenSearch
Stage 6  → licensing-contract-service    WORM lock on MinIO + royalty engine activation
```

---

## Security Features

| Feature | Implementation |
|---------|---------------|
| Tool allowlist | Only 12 named tools executable; all others rejected |
| UUID validation | All ID fields validated against strict UUID regex |
| Content injection guard | SQL, XSS, path traversal, template injection patterns rejected |
| Field length limits | title ≤ 200 chars, description ≤ 10 000 chars (per ITS spec §5) |
| Visibility enum check | Only `PRIVATE \| INTERNAL \| MARKETPLACE` accepted |
| Status enum check | Only valid lifecycle states accepted |
| SHA-256 format check | `stored_hash` must be exactly 64 lowercase hex chars |
| ETag format check | `expected_etag` must be exactly 32 hex chars (MD5) |
| Attachment count limit | 0–5 only (per ITS spec §5.2) |
| Ownership enforcement | Only original `candidate_id` may revise, set visibility, or archive |
| Audit log | Append-only; every tool call that mutates state writes an audit entry |
| Logs to stderr | stdout stays clean for MCP JSON-RPC protocol |

---

## Tamper Detection Model

```
SHA-256 (content_hash)   — proves idea CONTENT unchanged
MinIO ETag (minio_etag)  — proves STORAGE OBJECT unchanged
submitted_at             — immutable PostgreSQL clock; cannot be forged
```

Two independent channels. A tamper on either triggers `TAMPER_DETECTED` audit + Kafka event
`fingerprint.tamper_detected` to `innovation-registry-service` and `compliance-audit-service`.

---

## Rate Limiting

Redis key: `tenant:{tenant_id}:candidate:{candidate_id}:submissions:{date}`  
Default limit: **20 submissions / candidate / day** (configurable via admin-service per tenant)  
On limit breach: HTTP 429 + `Retry-After` header (UTC day rollover)

---

## Requirements

- Java 17+
- Maven 3.8+ or manual javac build

---

## Build

```bash
# Using javac + local Jackson (no Maven Central needed)
JACKSON_CP="/usr/share/java/jackson-databind.jar:/usr/share/java/jackson-core.jar:/usr/share/java/jackson-annotations.jar"
SRC_DIR="src/main/java"
OUT_DIR="target/classes"
mkdir -p "$OUT_DIR"
find "$SRC_DIR" -name "*.java" > /tmp/sources.txt
javac -source 17 -target 17 -cp "$JACKSON_CP" -d "$OUT_DIR" @/tmp/sources.txt
```

Or with Maven (requires Maven Central access):
```bash
mvn clean package -DskipTests
```

---

## Run

```bash
java -jar target/mcp-idea-timestamp-service-1.0.0.jar
```

Communicates via **stdin/stdout** JSON-RPC 2.0. All logs go to **stderr**.

---

## Run Tests

```bash
java -cp target/mcp-idea-timestamp-service-1.0.0.jar \
     com.ecoskiller.mcp.its.TestAgents

# With full JSON output:
java -cp target/mcp-idea-timestamp-service-1.0.0.jar \
     com.ecoskiller.mcp.its.TestAgents --verbose
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-idea-timestamp-service": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-idea-timestamp-service-1.0.0.jar"]
    }
  }
}
```

---

## File Structure

```
mcp-its-java/
├── pom.xml
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp/its/
    │   ├── IdeaTimestampMcpServer.java      ← Entry point, JSON-RPC dispatcher
    │   ├── handlers/
    │   │   ├── ToolRegistry.java            ← 12-tool registry + MCP schema
    │   │   ├── SubmissionHandler.java       ← submit_idea, revise_idea
    │   │   ├── TimestampHandler.java        ← get_timestamp_proof, list_idea_versions, get_idea_ownership
    │   │   ├── IntegrityHandler.java        ← verify_idea_integrity, verify_minio_object
    │   │   ├── LifecycleHandler.java        ← get_submission_status, set_visibility, archive_idea
    │   │   └── AuditHandler.java            ← check_rate_limit, get_submission_audit
    │   └── security/
    │       └── SecurityValidator.java       ← Input validation, allowlist, injection guard
    └── test/java/com/ecoskiller/mcp/its/
        └── TestAgents.java                  ← 22-test suite (lifecycle + security)
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Production Integration

| Tool | Production Backend |
|------|--------------------|
| `submit_idea` | Node.js crypto SHA-256 → PostgreSQL `innovation.ideas` + MinIO `putObject` + KafkaJS publish |
| `verify_idea_integrity` | Recompute SHA-256, compare vs PostgreSQL `content_hash` |
| `verify_minio_object` | MinIO JS SDK `statObject().etag()` vs stored `minio_etag` |
| `check_rate_limit` | ioredis `INCR` + `EXPIRE` on Redis 7 key per tenant+candidate+date |
| `get_submission_audit` | PostgreSQL `innovation.idea_submission_audit` (append-only, RLS) |
| Kafka events | `idea.submitted` → `idea-dna-fingerprint-engine`, `analytics-service`, `notification-service`, `search-indexer` |
| MinIO bucket | `ecoskiller-ideas-{tenant_id}` with Object Versioning enabled |
| Encryption | pgcrypto AES-256-CBC for `idea_title` on PRIVATE ideas |
