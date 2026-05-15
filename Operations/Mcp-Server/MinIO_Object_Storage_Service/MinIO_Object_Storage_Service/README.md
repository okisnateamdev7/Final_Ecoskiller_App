# MinIO Object Storage MCP Server (Java)

**Secure, Production-Grade Enterprise Storage Integration**

Version 2.0 | Production Ready | Enterprise Security | SOC2/ISO27001 Aligned

---

## 📋 Overview

This is a **secure, enterprise-grade MCP (Model Context Protocol) server** written in Java that provides intelligent AI models with S3-compatible object storage capabilities via MinIO. It implements comprehensive security controls, audit logging, encryption, and multi-tenant isolation.

### Key Features

✅ **MCP Protocol 2.0** - Native support for Claude API and other AI models  
✅ **13 S3 Operations** - Complete object storage API coverage  
✅ **Enterprise Security** - RBAC, encryption, multi-tenant isolation, audit logging  
✅ **High Availability** - Kubernetes deployment with auto-scaling and pod disruption budgets  
✅ **Production Ready** - Battle-tested on Ecoskiller platform  
✅ **Zero Trust Architecture** - All requests validated, authenticated, and logged  
✅ **Immutability Enforcement** - Object locking and version control support  

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────┐
│        AI Model (Claude / Other)                │
│     (via MCP Protocol Bidirectional)            │
└────────────────────┬────────────────────────────┘
                     │ JSON-RPC 2.0
                     ▼
┌─────────────────────────────────────────────────┐
│   MinIO MCP Server (Java)                       │
│  ┌────────────────────────────────────────────┐ │
│  │ • MCP Protocol Handler                     │ │
│  │ • 13 S3 Operations                         │ │
│  │ • Security & RBAC Layer                    │ │
│  │ • Audit Logging                            │ │
│  │ • Encryption Management                    │ │
│  │ • Multi-tenant Isolation                   │ │
│  └────────────────────────────────────────────┘ │
└────────────────────┬────────────────────────────┘
                     │ S3 API (TLS 1.3)
                     ▼
┌─────────────────────────────────────────────────┐
│        MinIO Object Storage Cluster             │
│  (GCP/AWS - Multi-cloud, Erasure Coded)        │
└─────────────────────────────────────────────────┘
```

---

## 🔒 Security Architecture

### Authentication & Authorization

- **Service Account Model**: Each deployment uses unique access/secret key pair
- **Kubernetes Secrets RBAC**: Credentials stored in K8s Secrets with role-based access
- **Multi-tenant Isolation**: Bucket names validated against tenant context
- **Operation Whitelisting**: Only 13 predefined S3 operations allowed
- **Key Validation**: Input sanitization on all parameters

### Encryption

- **In Transit**: TLS 1.3 for all MinIO communication
- **At Rest**: AES-256-GCM server-side encryption enabled by default
- **Optional**: KMS integration for customer-managed keys

### Audit & Compliance

- **Immutable Audit Log**: All operations logged with actor, action, timestamp
- **Security Events**: Unauthorized access, failed operations, suspicious patterns
- **DPDPA 2023**: Data retention policies enforced
- **Export**: Audit trail available for compliance audits

### Tenant Isolation

```
Row-Level Security:
- Bucket access validation: bucket_name.contains(tenant_id)
- Key sanitization: no ".." or "/" prefix allowed
- PostgreSQL RLS: Enforced on related metadata
- Kubernetes NetworkPolicy: Pod-level network isolation
```

---

## 📦 Supported Operations

### Core S3 Operations

| Operation | Description | Use Case |
|-----------|-------------|----------|
| `put_object` | Upload file to bucket | Store assessment videos, reports |
| `get_object` | Download file from bucket | Retrieve for processing, playback |
| `delete_object` | Remove file | Lifecycle management, compliance |
| `list_objects` | List files in bucket | Browsing, filtering, discovery |
| `copy_object` | Copy between buckets | Replication, backup, archive |

### Bucket Management

| Operation | Description | Use Case |
|-----------|-------------|----------|
| `create_bucket` | Create new bucket | New tenant onboarding |
| `delete_bucket` | Delete bucket | Tenant offboarding, cleanup |
| `list_buckets` | List all buckets | Inventory, reporting |
| `get_bucket_versioning` | Get version status | Compliance verification |
| `set_bucket_versioning` | Enable versioning | Historical recovery, audit trail |

### Security Operations

| Operation | Description | Use Case |
|-----------|-------------|----------|
| `get_bucket_encryption` | Check encryption settings | Compliance audit |
| `set_bucket_encryption` | Enable AES-256-GCM | Security hardening |
| `get_object_metadata` | Get object headers/checksums | Integrity verification |
| `get_object_acl` | Get access control list | Security audit |

---

## 🚀 Quick Start

### Prerequisites

- **Java**: OpenJDK 17+ (Alpine-based for production)
- **Maven**: 3.9+
- **MinIO**: 8.5.9+ (self-hosted or cloud)
- **Kubernetes**: 1.25+ (for deployment)

### Local Development

```bash
# Clone repository
git clone https://github.com/ecoskiller/minio-mcp-server.git
cd minio-mcp-server

# Set environment variables
export MINIO_ENDPOINT="minio.default.svc.cluster.local:9000"
export MINIO_ACCESS_KEY="your-access-key"
export MINIO_SECRET_KEY="your-secret-key"
export TENANT_ID="default"
export MINIO_USE_SSL="true"

# Build fat JAR
mvn clean package -DskipTests

# Run locally
java -jar target/minio-mcp-server.jar
```

### Docker Build

```bash
# Build Docker image
docker build -t ecoskiller/minio-mcp-server:2.0 .

# Run container
docker run -it \
  -e MINIO_ENDPOINT="minio.local:9000" \
  -e MINIO_ACCESS_KEY="minioadmin" \
  -e MINIO_SECRET_KEY="minioadmin" \
  -e TENANT_ID="default" \
  ecoskiller/minio-mcp-server:2.0
```

### Kubernetes Deployment

```bash
# Apply manifest
kubectl apply -f kubernetes-deployment.yaml

# Verify deployment
kubectl -n minio-mcp get pods
kubectl -n minio-mcp logs -f deployment/minio-mcp-server

# Port forward for testing
kubectl -n minio-mcp port-forward svc/minio-mcp-server-svc 9000:9000
```

---

## 🔧 Configuration

### Environment Variables

```bash
# MinIO Connection
MINIO_ENDPOINT=minio.default.svc.cluster.local:9000   # MinIO endpoint
MINIO_ACCESS_KEY=your-access-key                        # AWS_ACCESS_KEY_ID equivalent
MINIO_SECRET_KEY=your-secret-key                        # AWS_SECRET_ACCESS_KEY equivalent
MINIO_USE_SSL=true                                      # Enable TLS/SSL

# MCP Server Configuration
TENANT_ID=default                                       # Multi-tenant context
SERVICE_ACCOUNT_ID=minio-mcp-server                     # Audit trail identity
LOG_LEVEL=INFO                                          # Logging: DEBUG, INFO, WARN, ERROR

# JVM Tuning
JAVA_OPTS="-XX:+UseG1GC -Xms256m -Xmx512m"             # JVM heap settings
```

### Kubernetes Secrets

Store credentials in a sealed secret:

```bash
# Create K8s secret
kubectl -n minio-mcp create secret generic minio-mcp-credentials \
  --from-literal=MINIO_ENDPOINT="minio.default.svc.cluster.local:9000" \
  --from-literal=MINIO_ACCESS_KEY="$ACCESS_KEY" \
  --from-literal=MINIO_SECRET_KEY="$SECRET_KEY" \
  --from-literal=TENANT_ID="acme-corp"

# Apply SealedSecrets for encrypted storage
kubeseal -f k8s-secret.yaml -w k8s-secret-sealed.yaml

# Deploy with sealed secret
kubectl apply -f k8s-secret-sealed.yaml
```

---

## 📝 Usage Examples

### Using with Claude API

```python
# Example: Integration with Claude API
import anthropic

client = anthropic.Anthropic()

response = client.beta.messages.create(
    model="claude-opus-4-20250514",
    max_tokens=2048,
    betas=["mcp-1"],
    tools=[
        {
            "type": "mcp_server",
            "name": "minio-mcp-server",
            "uri": "stdio:///path/to/minio-mcp-server.jar"
        }
    ],
    messages=[
        {
            "role": "user",
            "content": "Upload my assessment report to MinIO bucket 'assessments'"
        }
    ]
)

print(response.content)
```

### MCP Protocol Examples

#### Initialize

```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "initialize",
  "params": {}
}
```

Response:
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    "name": "MinIO Object Storage MCP Server",
    "version": "2.0",
    "tools": [
      {
        "name": "put_object",
        "description": "Upload object to MinIO bucket with encryption and RBAC validation"
      },
      ...
    ]
  }
}
```

#### List Buckets

```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "method": "tools/call",
  "params": {
    "name": "list_buckets",
    "arguments": {}
  }
}
```

Response:
```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "result": {
    "count": 3,
    "tenant": "default",
    "buckets": [
      {
        "name": "default-assessments",
        "created": "2025-03-10T10:30:00Z"
      },
      {
        "name": "default-reports",
        "created": "2025-03-10T10:31:00Z"
      }
    ]
  }
}
```

#### Upload Object

```json
{
  "jsonrpc": "2.0",
  "id": 3,
  "method": "tools/call",
  "params": {
    "name": "put_object",
    "arguments": {
      "bucket": "default-assessments",
      "key": "gd-session-001/video.mp4",
      "data": "<base64-encoded-video-data>",
      "content_type": "video/mp4"
    }
  }
}
```

Response:
```json
{
  "jsonrpc": "2.0",
  "id": 3,
  "result": {
    "status": "success",
    "bucket": "default-assessments",
    "key": "gd-session-001/video.mp4",
    "size_bytes": 209715200,
    "checksum_sha256": "abc123def456...",
    "timestamp": "2025-03-10T10:32:00Z"
  }
}
```

---

## 🛠️ Helm Deployment

### values.yaml

```yaml
# MinIO MCP Server Helm Values

replicaCount: 2

image:
  repository: ecoskiller/minio-mcp-server
  tag: "2.0"
  pullPolicy: IfNotPresent

resources:
  requests:
    cpu: 250m
    memory: 512Mi
  limits:
    cpu: 1000m
    memory: 1Gi

hpa:
  enabled: true
  minReplicas: 2
  maxReplicas: 10
  targetCPUUtilization: 70
  targetMemoryUtilization: 80

config:
  LOG_LEVEL: INFO
  JAVA_OPTS: "-XX:+UseG1GC -Xms256m -Xmx512m"

minio:
  endpoint: "minio.default.svc.cluster.local:9000"
  useSSL: true
  # accessKey and secretKey must be provided via --set or sealed-secrets
```

### Deploy with Helm

```bash
helm install minio-mcp ecoskiller-charts/minio-mcp \
  --namespace minio-mcp \
  --create-namespace \
  -f values.yaml \
  --set minio.accessKey="$ACCESS_KEY" \
  --set minio.secretKey="$SECRET_KEY"
```

---

## 📊 Monitoring & Observability

### Prometheus Metrics

Metrics available at `:9090/metrics`:

```
# HELP minio_mcp_requests_total Total number of requests
# TYPE minio_mcp_requests_total counter
minio_mcp_requests_total{method="put_object"} 1042

# HELP minio_mcp_operations_duration_seconds Operation duration
# TYPE minio_mcp_operations_duration_seconds histogram
minio_mcp_operations_duration_seconds_bucket{method="get_object",le="0.1"} 856
```

### Grafana Dashboard

Import dashboard ID `XXXX` to visualize:
- Request rates by operation
- Success/failure ratios
- Operation latencies (p50/p95/p99)
- Tenant isolation metrics
- Audit event frequency

### Audit Log Export

```bash
# Export audit trail
kubectl -n minio-mcp logs deployment/minio-mcp-server | grep "AUDIT:" > audit.log

# Parse for compliance
cat audit.log | jq -s 'group_by(.actor) | map({actor: .[0].actor, count: length})'
```

---

## 🔍 Troubleshooting

### Connection Failures

```bash
# Check MinIO endpoint connectivity
kubectl -n minio-mcp exec -it deployment/minio-mcp-server -- \
  nc -zv minio.default.svc.cluster.local 9000

# Verify credentials
kubectl -n minio-mcp get secret minio-mcp-credentials -o yaml
```

### Permission Denied Errors

```bash
# Check RBAC configuration
kubectl -n minio-mcp get roles,rolebindings

# Verify service account has secret access
kubectl -n minio-mcp auth can-i get secrets --as=system:serviceaccount:minio-mcp:minio-mcp-server
```

### High Memory Usage

```bash
# Check JVM settings
kubectl -n minio-mcp get deployment minio-mcp-server -o yaml | grep JAVA_OPTS

# Adjust limits
kubectl -n minio-mcp set resources deployment minio-mcp-server \
  --limits=memory=2Gi --requests=memory=1Gi
```

---

## 📋 Compliance & Security

### DPDPA 2023 (India)

✅ Consent logging for all operations  
✅ Data retention policies enforced  
✅ Right to erasure implemented  
✅ Data breach notification ready  

### ISO 27001 / SOC2 Type II

✅ Access controls and RBAC  
✅ Encryption in transit and at rest  
✅ Audit logging and monitoring  
✅ Incident response procedures  

### PCI DSS (if handling payment data)

✅ Never stores payment card data directly  
✅ Uses Kill Bill for payment orchestration  
✅ Encrypted communication only  

---

## 🤝 Contributing

### Development Setup

```bash
# Install dependencies
mvn install

# Run tests
mvn test

# Code formatting
mvn spotless:apply

# Build & deploy to staging
mvn clean package
docker build -t ecoskiller/minio-mcp-server:dev .
docker push ecoskiller/minio-mcp-server:dev
```

### Security Reporting

Found a security vulnerability? Email security@ecoskiller.com with:
- Description of the issue
- Steps to reproduce
- Potential impact
- Your recommendation

---

## 📄 License

Proprietary - Ecoskiller Platform  
© 2024 Ecoskiller. All rights reserved.

---

## 📞 Support

- **Documentation**: https://docs.ecoskiller.com/minio-mcp
- **Issues**: https://github.com/ecoskiller/minio-mcp-server/issues
- **Slack**: #platform-engineering
- **Email**: devops@ecoskiller.com

---

## 🎯 Roadmap

### v2.1 (Q2 2025)
- [ ] OpenTelemetry tracing integration
- [ ] Prometheus remote write for metrics
- [ ] GraphQL API layer
- [ ] WebSocket streaming for large objects

### v3.0 (Q3 2025)
- [ ] Multi-cloud replication dashboard
- [ ] ML-powered anomaly detection
- [ ] Advanced quota management
- [ ] Federated identity (OIDC)

---

**Version 2.0** | Last Updated: March 10, 2025 | Production: ✅ Ready
