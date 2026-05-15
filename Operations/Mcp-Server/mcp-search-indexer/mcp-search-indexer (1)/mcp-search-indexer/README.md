# mcp-search-indexer

**Ecoskiller | Search Indexer Service — MCP Server**
Java 17 | 18 Tools | Secure | MCP 2024-11-05 | JSON-RPC 2.0 over stdio

---

## Tools (18)

| # | Tool Name | Agent | Purpose |
|---|-----------|-------|---------|
| 1 | `candidate_search` | CANDIDATE_SEARCH_AGENT | Full-text candidate profile search, BM25, fuzzy, Hindi/English |
| 2 | `job_search` | JOB_SEARCH_AGENT | Job posting search, geospatial queries, facets |
| 3 | `index_document` | INDEX_DOCUMENT_AGENT | Single-doc index/update/upsert (idempotent) |
| 4 | `bulk_index` | BULK_INDEX_AGENT | 10K+ docs/sec batch indexing, DLQ, circuit breaker |
| 5 | `delete_document` | DELETE_DOCUMENT_AGENT | Single/query delete, GDPR right-to-be-forgotten |
| 6 | `reindex` | REINDEX_AGENT | Zero-downtime blue-green reindex, alias cutover |
| 7 | `index_health` | INDEX_HEALTH_AGENT | Shard allocation, JVM heap, query latency, alerts |
| 8 | `event_consumer` | EVENT_CONSUMER_AGENT | Kafka lag, offset, DLQ replay, idempotency |
| 9 | `document_transform` | DOCUMENT_TRANSFORM_AGENT | Domain event → OpenSearch doc, PII exclusion |
| 10 | `faceted_search` | FACETED_SEARCH_AGENT | Multi-dim facets with dynamic sub-facet recomputation |
| 11 | `tenant_index_manager` | TENANT_INDEX_MANAGER_AGENT | Per-tenant index lifecycle, isolation verification |
| 12 | `index_mapping` | INDEX_MAPPING_AGENT | Mapping management, Hindi/Hinglish analyzers |
| 13 | `search_analytics` | SEARCH_ANALYTICS_AGENT | Funnel, job demand, geo heatmap, pipeline velocity |
| 14 | `query_audit_log` | QUERY_AUDIT_LOG_AGENT | Query audit trail, abuse detection, compliance |
| 15 | `ilm_policy` | ILM_POLICY_AGENT | Hot→warm→cold→delete lifecycle, cost optimization |
| 16 | `cluster_manager` | CLUSTER_MANAGER_AGENT | Active-active GCP+AWS, failover, replication, canary |
| 17 | `gdpr_compliance` | GDPR_COMPLIANCE_AGENT | Forget requests (48h SLA), PII check, encryption status |
| 18 | `disaster_recovery` | DISASTER_RECOVERY_AGENT | Snapshots, Kafka replay, RTO ≤ 5min, RPO ≤ 5sec |

---

## Requirements

- Java 17+
- Maven 3.8+ (build only)
- No external runtime dependencies

---

## Build

```bash
mvn package -DskipTests
# Output: target/mcp-search-indexer-1.0.0.jar
```

## Run

```bash
# Development
ENV=dev java -jar target/mcp-search-indexer-1.0.0.jar

# Production
ENV=prod \
AUDIT_LOG_PATH=/var/log/ecoskiller/mcp-search-indexer-audit.log \
  java -jar target/mcp-search-indexer-1.0.0.jar
```

## Run Tests

```bash
mvn test                          # 57 tests — no OpenSearch or Kafka needed
mvn test -Dsurefire.useFile=false  # verbose
```

---

## Connect to Claude Desktop

```json
{
  "mcpServers": {
    "mcp-search-indexer": {
      "command": "java",
      "args": ["-jar", "/path/to/mcp-search-indexer-1.0.0.jar"],
      "env": {
        "ENV":            "prod",
        "AUDIT_LOG_PATH": "/var/log/ecoskiller/mcp-search-indexer-audit.log"
      }
    }
  }
}
```

---

## Security Model

| Control | Implementation |
|---------|---------------|
| **Tenant isolation** | All queries filtered by `tenant_id` — cross-tenant leakage impossible |
| **ID validation** | `[a-zA-Z0-9\-_]{1,128}` regex on all identifiers |
| **Query injection** | Backslash and oversized queries blocked |
| **Doc type allow-list** | Only `candidate\|job\|assessment\|application\|user_profile\|scoring` |
| **Pagination limits** | `limit` max 1000, `offset` ≥ 0, `batch_size` max 1000 |
| **Language allow-list** | `en\|hi\|hinglish\|auto` only |
| **Cluster allow-list** | `gcp\|aws\|primary\|secondary\|all` only |
| **PII exclusion** | email, phone, SSN never indexed in OpenSearch |
| **GDPR 48h SLA** | Forget requests queued with SLA enforcement |
| **TLS 1.3** | All connections: Kafka↔indexer, indexer↔OpenSearch, clients↔API |
| **Audit logging** | Every tool call logged — supports forensic analysis |
| **Destructive gates** | `deprovision`, `failover`, `restore`, `full_recovery` require `confirm=CONFIRM` |

---

## File Structure

```
mcp-search-indexer/
├── pom.xml
├── README.md
├── claude_desktop_config.json
└── src/
    ├── main/java/com/ecoskiller/mcp/search/
    │   ├── SearchIndexerMcpServer.java    ← Main server
    │   ├── McpTool.java                   ← Tool interface
    │   ├── security/
    │   │   ├── InputValidator.java        ← Security validation
    │   │   └── AuditLogger.java           ← Audit trail
    │   └── tools/
    │       ├── BaseTool.java
    │       ├── CandidateSearchTool.java
    │       ├── JobSearchTool.java
    │       ├── IndexDocumentTool.java
    │       ├── BulkIndexTool.java
    │       ├── DeleteDocumentTool.java
    │       ├── ReindexTool.java
    │       ├── IndexHealthTool.java
    │       ├── EventConsumerTool.java
    │       ├── DocumentTransformTool.java
    │       ├── FacetedSearchTool.java
    │       ├── TenantIndexManagerTool.java
    │       ├── IndexMappingTool.java
    │       ├── SearchAnalyticsTool.java
    │       ├── QueryAuditLogTool.java
    │       ├── IlmPolicyTool.java
    │       ├── ClusterManagerTool.java
    │       ├── GdprComplianceTool.java
    │       └── DisasterRecoveryTool.java
    └── test/java/com/ecoskiller/mcp/search/
        └── SearchIndexerMcpServerTest.java  ← 57 tests
```

---

## Performance SLOs (from spec)

| Metric | Target |
|--------|--------|
| Search query latency p95 | < 100ms |
| Indexing throughput | 10K+ docs/sec |
| Event processing lag | < 5 seconds |
| Availability | 99.95% |
| RTO (disaster recovery) | ≤ 5 minutes |
| RPO | ≤ 5 seconds |

---

*Ecoskiller Platform Engineering · Search Indexer MCP Server · v1.0.0 · March 2026*
