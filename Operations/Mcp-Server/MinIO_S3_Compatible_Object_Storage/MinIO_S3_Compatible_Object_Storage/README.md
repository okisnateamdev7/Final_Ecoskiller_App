# mcp-minio-java

**Ecoskiller | MinIO S3-Compatible Object Storage MCP Server**  
MCP Server in Java | 24 Tools | Transport: stdio | Protocol: JSON-RPC 2.0

---

## Overview

Production-grade MCP server that exposes the full MinIO S3 API to Claude.  
Built for the Ecoskiller platform: stores assessment videos, resumes, digital
certificates, coding submissions, and compliance records at scale.

- **Durability:** 11-nines (Reed-Solomon 4+2 erasure coding)  
- **Compliance:** DPDP Act 2023, GDPR-ready, WORM immutable storage  
- **Cost:** 4–5× cheaper than AWS S3 at 100TB+ scale  
- **Multi-cloud:** GCP us-central1 primary + AWS us-east-1 secondary replication  

---

## Tools (24)

### Bucket Operations (6)
| # | Tool | Description |
|---|------|-------------|
| 1 | `bucket_create` | Create bucket (with optional object lock, region) |
| 2 | `bucket_delete` | Delete bucket (force option removes all objects first) |
| 3 | `bucket_list` | List all accessible buckets |
| 4 | `bucket_policy_get` | Get IAM JSON policy for a bucket |
| 5 | `bucket_policy_set` | Apply IAM JSON policy to a bucket |
| 6 | `bucket_versioning_set` | Enable or suspend object versioning |

### Object Operations (8)
| # | Tool | Description |
|---|------|-------------|
| 7 | `object_upload` | Upload object from base64 or text content |
| 8 | `object_download` | Download object (returns base64 or text) |
| 9 | `object_delete` | Delete object or specific version |
| 10 | `object_head` | Get metadata without downloading body |
| 11 | `object_list` | List objects with prefix/pagination |
| 12 | `object_copy` | Server-side copy (zero bandwidth) |
| 13 | `object_tag_set` | Set key-value tags on an object |
| 14 | `object_tag_get` | Get all tags on an object |

### Multipart Upload (3)
| # | Tool | Description |
|---|------|-------------|
| 15 | `multipart_upload_initiate` | Initiate multipart upload for files >5GB |
| 16 | `multipart_upload_complete` | Complete multipart upload with part ETags |
| 17 | `multipart_upload_abort` | Abort in-progress multipart upload |

### Presigned URLs (2)
| # | Tool | Description |
|---|------|-------------|
| 18 | `presigned_url_get` | Generate time-limited GET URL (default 1hr) |
| 19 | `presigned_url_put` | Generate presigned PUT URL for direct browser upload |

### Lifecycle & Compliance (3)
| # | Tool | Description |
|---|------|-------------|
| 20 | `lifecycle_policy_set` | Auto-expiry + cold storage transition rules |
| 21 | `object_lock_configure` | Configure WORM object lock (GOVERNANCE/COMPLIANCE mode) |
| 22 | `object_lock_get` | Get current WORM configuration |

### Operational (2)
| # | Tool | Description |
|---|------|-------------|
| 23 | `health_check` | Cluster health, TLS status, bucket count |
| 24 | `tenant_bucket_provision` | Auto-provision tenant bucket with versioning + lifecycle |

---

## Requirements

- **Java 11+** (tested on Java 17, 21)
- **Maven 3.8+** (for building)
- **MinIO Server** (self-hosted) or any S3-compatible endpoint

---

## Security Model

| Control | Implementation |
|---------|----------------|
| Credentials | Environment variables only — never in code, args, or logs |
| TLS | TLS 1.3 enabled by default; disable only with `MINIO_TLS_DISABLED=true` |
| Input validation | Bucket names, object keys, content types, tenant IDs all validated |
| Rate limiting | 60 calls/min per tool (sliding window) |
| Audit logging | Every call logged to stderr with timestamp, tool, status |
| Error sanitisation | Stack traces never exposed to MCP client |
| Root user | Disable MinIO root user; use service accounts with minimal-privilege policies |

---

## Environment Variables

```bash
# Required
export MINIO_ENDPOINT=https://minio.ecoskiller.internal:9000
export MINIO_ACCESS_KEY=ecoskiller-svc-account
export MINIO_SECRET_KEY=<secret-from-vault>

# Optional
export MINIO_TLS_DISABLED=true    # DEV ONLY — disables TLS cert verification
```

> **Never set `MINIO_TLS_DISABLED=true` in production.**

### Kubernetes Secret (recommended)

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: minio-mcp-credentials
  namespace: ecoskiller
type: Opaque
stringData:
  MINIO_ENDPOINT: "https://minio.ecoskiller.internal:9000"
  MINIO_ACCESS_KEY: "ecoskiller-svc"
  MINIO_SECRET_KEY: "your-secret-here"
```

---

## Build & Run

```bash
# Build fat-jar (includes all dependencies)
mvn clean package -q

# Run the server
java -jar target/mcp-minio-server.jar
```

The server reads JSON-RPC 2.0 requests from **stdin** and writes responses to **stdout**.  
All audit logs and diagnostics go to **stderr**.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-minio-ecoskiller": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-minio-server.jar"],
      "env": {
        "MINIO_ENDPOINT":    "https://minio.ecoskiller.internal:9000",
        "MINIO_ACCESS_KEY":  "ecoskiller-svc",
        "MINIO_SECRET_KEY":  "your-secret"
      }
    }
  }
}
```

---

## Connect to Claude Code (claude_desktop_config.json)

```json
{
  "mcp-minio-ecoskiller": {
    "command": "java",
    "args": ["-jar", "/path/to/mcp-minio-server.jar"]
  }
}
```

---

## Test the server manually

```bash
# Initialize handshake
echo '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"2024-11-05","clientInfo":{"name":"test","version":"1.0"}}}' \
  | java -jar target/mcp-minio-server.jar

# List tools
echo '{"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}' \
  | java -jar target/mcp-minio-server.jar

# Health check
echo '{"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"health_check","arguments":{}}}' \
  | java -jar target/mcp-minio-server.jar

# List buckets
echo '{"jsonrpc":"2.0","id":4,"method":"tools/call","params":{"name":"bucket_list","arguments":{}}}' \
  | java -jar target/mcp-minio-server.jar

# Create tenant bucket
echo '{"jsonrpc":"2.0","id":5,"method":"tools/call","params":{"name":"tenant_bucket_provision","arguments":{"tenant_id":"tenant-001","region":"ap-south-1"}}}' \
  | java -jar target/mcp-minio-server.jar

# Generate presigned URL
echo '{"jsonrpc":"2.0","id":6,"method":"tools/call","params":{"name":"presigned_url_get","arguments":{"bucket":"ecoskiller-tenant-001-assets","object_key":"resumes/candidate-xyz.pdf","ttl_seconds":"3600"}}}' \
  | java -jar target/mcp-minio-server.jar
```

---

## Ecoskiller Bucket Naming Convention

| Bucket | Contents |
|--------|----------|
| `ecoskiller-{tenant_id}-assets` | Resumes, certs, invoices, recording files |
| `ecoskiller-{tenant_id}-recordings` | Group discussion and interview video files |
| `ecoskiller-{tenant_id}-submissions` | Code, test answer, project uploads |
| `ecoskiller-compliance-audit` | Immutable audit logs (WORM locked) |
| `ecoskiller-backups-velero` | Kubernetes cluster state backups (Velero) |

---

## File Structure

```
mcp-minio-java/
├── pom.xml                                          ← Maven build (fat-jar)
├── README.md
└── src/main/java/com/ecoskiller/mcp/minio/
    ├── MinioMcpServer.java                          ← Entry point + stdio loop
    ├── protocol/
    │   └── McpProtocol.java                         ← JSON-RPC 2.0 data types
    ├── security/
    │   └── SecurityManager.java                     ← Credentials, validation, rate limit, audit
    └── tools/
        └── MinioToolHandler.java                    ← All 24 tool implementations
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Ecoskiller Architecture Notes

MinIO in the Ecoskiller stack:
- Sits between microservices and Kubernetes persistent volumes
- Primary: GCP `us-central1` (DPDP data residency — India)
- Secondary: AWS `us-east-1` (cross-cloud DR, async replication)
- Backup: AWS S3 Glacier (daily 2AM UTC snapshots, 30-day retention)
- Cluster: 4-node StatefulSet (4+2 erasure coding), scales to 16 nodes
- Throughput: 400–2000 MB/sec (4-node), 1.6–8 GB/sec (16-node)
- Latency: PUT 10–100ms, GET 5–50ms, LIST 100–500ms
- Access: Pre-signed URLs only — no direct public access
- Monitoring: Prometheus metrics + Grafana dashboards + Datadog alerts
