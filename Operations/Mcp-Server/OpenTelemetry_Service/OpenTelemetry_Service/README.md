# EcoSkiller OpenTelemetry MCP Server (Java)

**Secure, Production-Ready OpenTelemetry Service for Distributed Tracing & Observability**

[![Java 17+](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/technologies/downloads/#java17)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](LICENSE)

---

## Overview

The **EcoSkiller OpenTelemetry MCP Server** is a secure, production-ready Model Context Protocol (MCP) implementation in Java that provides distributed tracing, span processing, and observability signal management for the EcoSkiller AI-powered talent assessment platform.

### Key Features

✅ **JSON-RPC 2.0 Compliant** - Full MCP protocol support  
✅ **Secure by Default** - mTLS, TLS 1.3, certificate validation  
✅ **Multi-Tenant Isolation** - Complete tenant data isolation  
✅ **Distributed Tracing** - W3C TraceContext propagation  
✅ **Batch Processing** - Efficient span batching and export  
✅ **Memory Limited** - 400 MiB OOM protection  
✅ **High Performance** - 18+ spans/sec throughput  
✅ **Production Ready** - Kubernetes-native, comprehensive monitoring  

---

## Quick Start

### Prerequisites

- **Java 17+** (OpenJDK, Oracle JDK, or Amazon Corretto)
- **Maven 3.8+**
- **Docker** (for containerized deployment)
- **Kubernetes 1.24+** (for production deployment)

### Build

```bash
# Clone repository
git clone https://github.com/ecoskiller/otel-mcp-java.git
cd otel-mcp-java

# Build with Maven
mvn clean package

# Create fat JAR
mvn assembly:single
```

### Run Locally

```bash
# Start the server
java -jar target/otel-mcp-server-1.0.0-jar-with-dependencies.jar

# Configure via environment variables
export SERVER_PORT=9090
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4317
export SAMPLING_RATE=1.0  # 100% for dev
export ENVIRONMENT=dev
export TLS_ENABLED=false  # Disable TLS for local testing

java -jar target/otel-mcp-server-1.0.0-jar-with-dependencies.jar
```

### Test the Server

```bash
# Health check (ping)
echo '{"jsonrpc":"2.0","method":"ping","id":"1"}' | nc localhost 9090

# Initialize
echo '{"jsonrpc":"2.0","method":"initialize","id":"2"}' | nc localhost 9090

# List tools
echo '{"jsonrpc":"2.0","method":"tools/list","id":"3"}' | nc localhost 9090

# Execute tool
echo '{"jsonrpc":"2.0","method":"tools/call","params":{"name":"trace_collector","arguments":{"tenantId":"tenant-1","traceId":"4bf92f3577b34da6a3ce929d0e0e4736"}},"id":"4"}' | nc localhost 9090
```

---

## Architecture

### Component Overview

```
┌─────────────────────────────────────────────────────────┐
│          EcoSkiller OpenTelemetry MCP Server            │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────────────────────────────────────────────┐  │
│  │         JSON-RPC 2.0 Request Handler              │  │
│  │  (initialize, tools/list, tools/call, ping)      │  │
│  └─────────────────┬────────────────────────────────┘  │
│                    │                                    │
│  ┌─────────────────▼────────────────────────────────┐  │
│  │         Security Manager                         │  │
│  │  (TLS, mTLS, request validation, tenant auth)   │  │
│  └─────────────────┬────────────────────────────────┘  │
│                    │                                    │
│  ┌─────────────────▼────────────────────────────────┐  │
│  │      OpenTelemetry Collector Service             │  │
│  │  (span reception, batching, memory limiting)    │  │
│  └─────────────────┬────────────────────────────────┘  │
│                    │                                    │
│  ┌─────────────────▼────────────────────────────────┐  │
│  │         Agent Services (18 agents)               │  │
│  │  (TAX_COMPLIANCE, ROYALTY_WALLET, etc.)         │  │
│  └─────────────────┬────────────────────────────────┘  │
│                    │                                    │
│  ┌─────────────────▼────────────────────────────────┐  │
│  │    OTLP Export (gRPC to Grafana Tempo)           │  │
│  │         TLS 1.3 Secured Transport                │  │
│  └──────────────────────────────────────────────────┘  │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### Request Flow

1. **Incoming Request** → JSON-RPC 2.0 parsing
2. **Security Validation** → TLS, authentication, authorization
3. **Rate Limiting** → Per-tenant/client rate checks
4. **Agent Dispatch** → Route to appropriate agent service
5. **Trace Collection** → Receive and validate spans
6. **Batch Processing** → Accumulate spans into batches
7. **Memory Management** → Apply 400 MiB limit
8. **Sampling** → Apply parentbased_traceidratio
9. **Export** → Send to OTLP backend (Grafana Tempo)

---

## Configuration

### Environment Variables

```bash
# Server
SERVER_HOST=0.0.0.0
SERVER_PORT=9090
THREAD_POOL_SIZE=16

# Collector
OTEL_EXPORTER_OTLP_ENDPOINT=http://otel-collector.ops.svc.cluster.local:4317
BATCH_SIZE=128
BATCH_TIMEOUT_MS=5000
QUEUE_SIZE=2048
SAMPLING_RATE=0.1  # 10% in prod, 1.0 in dev

# Security
TLS_ENABLED=true
TLS_CERT_PATH=/etc/certs/server.crt
TLS_KEY_PATH=/etc/certs/server.key
TLS_CA_CERT_PATH=/etc/certs/ca.crt
AUTH_REQUIRED=false
API_KEY=your-api-key-here

# Service
SERVICE_NAME=ecoskiller-otel-mcp
SERVICE_VERSION=1.0.0
ENVIRONMENT=production

# Logging
LOG_LEVEL=INFO
LOG_FORMAT=json
```

### application.conf

See `src/main/resources/application.conf` for all configuration options.

---

## Security Architecture

### Multi-Layer Security

#### 1. **TLS/mTLS Transport**
```
Client ──[TLS 1.3]──> Server
       ◄──[mTLS]──┘
```
- Enforced TLS 1.3 (minimum)
- Certificate validation
- Self-signed certificates for dev
- cert-manager integration for prod

#### 2. **Request Validation**
```
JSON-RPC Structure ──> Method Validation ──> Auth Check ──> Tenant Isolation
```

#### 3. **Span Security**
```
Span Reception ──> Format Validation ──> Injection Detection ──> Tenant Isolation
              ──> Rate Limiting ──> Encryption ──> Audit Log
```

#### 4. **Multi-Tenant Isolation**
```
Every Request
    ├── tenantId extraction
    ├── Tenant validation
    ├── Tenant-scoped resource access
    ├── Tenant-scoped output filtering
    └── Audit logging with tenant context
```

### Security Best Practices

1. **Always Enable TLS in Production**
   ```bash
   TLS_ENABLED=true
   TLS_CERT_PATH=/etc/certs/server.crt
   TLS_KEY_PATH=/etc/certs/server.key
   ```

2. **Rotate Certificates**
   - Use cert-manager for automatic rotation
   - Certificates rotate 30 days before expiry

3. **Enable Authentication**
   ```bash
   AUTH_REQUIRED=true
   API_KEY=<strong-random-key-min-32-chars>
   ```

4. **Audit Everything**
   - All requests logged with tenant ID
   - Audit trail in `/logs/audit.log`
   - Immutable audit storage recommended

5. **Network Isolation**
   - Run in dedicated Kubernetes namespace
   - NetworkPolicy restricts ingress/egress
   - Service-to-service mTLS enforced

---

## Agents (18 Available)

| # | Agent | Tool Name | Purpose |
|---|-------|-----------|---------|
| 1 | TAX_COMPLIANCE_AGENT | `tax_compliance` | GST/tax compliance validation |
| 2 | SCHOOL_AUTO_CREATION_AGENT | `school_auto_creation` | School/institution onboarding |
| 3 | ROYALTY_WALLET_AGENT | `royalty_wallet` | Royalty balance management |
| 4 | ROYALTY_SYSTEM_UNIFIED_AGENT | `royalty_system_unified` | Unified royalty operations |
| 5 | ROYALTY_ESCROW_AGENT | `royalty_escrow` | Escrow management |
| 6 | ROYALTY_DISTRIBUTION_AGENT | `royalty_distribution` | Distribute royalty payments |
| 7 | ROYALTY_CALCULATION_AGENT | `royalty_calculation` | Calculate royalty amounts |
| 8 | REVENUE_INGESTION_AGENT | `revenue_ingestion` | Revenue data ingestion |
| 9 | PARENT_DASHBOARD_AGENT | `parent_dashboard` | Parent portal management |
| 10 | MARKET_DEMAND_PREDICTION_AGENT | `market_demand_prediction` | Demand forecasting |
| 11 | LICENSE_GENERATION_AGENT | `license_generation` | License issuance |
| 12 | IDEA_VISIBILITY_CONTROL_AGENT | `idea_visibility_control` | Idea visibility management |
| 13 | IDEA_EVALUATION_AGENT | `idea_evaluation` | Idea assessment |
| 14 | DATA_RETENTION_POLICY_AGENT | `data_retention_policy` | Data retention enforcement |
| 15 | CONTRACT_LIFECYCLE_AGENT | `contract_lifecycle` | Contract management |
| 16 | COMPETITION_ENGINE_AGENT | `competition_engine` | Competition operations |
| 17 | BUSINESS_MATCHING_AGENT | `business_matching` | Business matching logic |
| 18 | AUDIT_VERIFICATION_AGENT | `audit_verification` | Audit trail verification |

### Agent Execution Example

```bash
# Execute royalty calculation
curl -X POST http://localhost:9090 \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "method": "tools/call",
    "params": {
      "name": "royalty_calculation",
      "arguments": {
        "tenantId": "tenant-acme",
        "traceId": "4bf92f3577b34da6a3ce929d0e0e4736",
        "amount": 5000,
        "basis": "idea_licensing"
      }
    },
    "id": "1"
  }'
```

---

## Observability Tools

### 18 Available Tools

```
1. trace_collector          - Central trace aggregation
2. span_processor           - Span filtering/batching
3. metric_exporter          - Prometheus metrics export
4. sampler_config           - Sampling configuration
5. batch_processor          - Batch management
6. memory_limiter           - OOM prevention (400 MiB)
7. otlp_receiver_grpc       - gRPC receiver (port 4317)
8. otlp_receiver_http       - HTTP receiver (port 4318)
9. kafka_processor          - Kafka trace propagation
10. tenant_isolation        - Multi-tenant enforcement
11. security_validator      - Request validation
12. trace_context_propagator - W3C TraceContext
13. resource_attributes     - Service metadata
14. exception_handler       - Error recording
15. performance_monitor     - Health monitoring
16. audit_logger            - Compliance logging
17. custom_span_builder     - Custom span creation
18. latency_analyzer        - Latency attribution
```

### Tool Usage

```bash
# List all tools
curl http://localhost:9090 \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","method":"tools/list","id":"1"}'

# Get tool details
curl http://localhost:9090 \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc":"2.0",
    "method":"tools/call",
    "params":{
      "name":"sampler_config",
      "arguments":{
        "tenantId":"tenant-1",
        "samplingRate":0.5
      }
    },
    "id":"2"
  }'
```

---

## Docker Deployment

### Build Docker Image

```bash
# Build image
docker build -t ecoskiller/otel-mcp-server:1.0.0 .

# Tag for registry
docker tag ecoskiller/otel-mcp-server:1.0.0 myregistry.azurecr.io/otel-mcp-server:1.0.0

# Push to registry
docker push myregistry.azurecr.io/otel-mcp-server:1.0.0
```

### Run with Docker Compose

```bash
docker-compose -f docker-compose.yml up -d
docker-compose logs -f otel-mcp-server
```

---

## Kubernetes Deployment

### Deploy to k3s

```bash
# Apply configuration
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secrets.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml

# Verify deployment
kubectl get pods -n observability
kubectl logs -n observability -l app=otel-mcp-server -f

# Port-forward for local testing
kubectl port-forward -n observability svc/otel-mcp-server 9090:9090
```

### Helm Chart

```bash
# Install
helm install otel-mcp-server ./helm/otel-mcp-server \
  -n observability \
  --create-namespace \
  -f helm/values-prod.yaml

# Upgrade
helm upgrade otel-mcp-server ./helm/otel-mcp-server \
  -n observability \
  -f helm/values-prod.yaml

# Uninstall
helm uninstall otel-mcp-server -n observability
```

---

## Monitoring & Observability

### Metrics Exposed

```
otelcol_receiver_accepted_spans{receiver="otlp"}
otelcol_receiver_refused_spans{receiver="otlp"}
otelcol_processor_dropped_spans{processor="batch"}
otelcol_exporter_sent_spans{exporter="otlp"}
otelcol_exporter_send_failed_spans{exporter="otlp"}
process_runtime_go_goroutines
process_runtime_go_mem_inuse_bytes
```

### Prometheus Scrape Config

```yaml
scrape_configs:
  - job_name: 'otel-mcp-server'
    static_configs:
      - targets: ['localhost:9090']
    metrics_path: '/metrics'
    scrape_interval: 30s
    scrape_timeout: 10s
```

### Grafana Dashboards

Pre-built dashboards available:
- `OTel Collector Health`
- `Span Processing Pipeline`
- `Tenant Isolation Compliance`
- `Latency Analysis`
- `Memory Usage`
- `Request Rate`

---

## API Reference

### JSON-RPC 2.0 Methods

#### 1. `initialize`
Initialize the MCP server
```json
{
  "jsonrpc": "2.0",
  "method": "initialize",
  "id": "1"
}
```

#### 2. `tools/list`
List all available tools
```json
{
  "jsonrpc": "2.0",
  "method": "tools/list",
  "id": "2"
}
```

#### 3. `tools/call`
Execute a tool
```json
{
  "jsonrpc": "2.0",
  "method": "tools/call",
  "params": {
    "name": "trace_collector",
    "arguments": {
      "tenantId": "tenant-1",
      "traceId": "4bf92f3577b34da6a3ce929d0e0e4736"
    }
  },
  "id": "3"
}
```

#### 4. `ping`
Health check
```json
{
  "jsonrpc": "2.0",
  "method": "ping",
  "id": "4"
}
```

#### 5. `shutdown`
Graceful shutdown
```json
{
  "jsonrpc": "2.0",
  "method": "shutdown",
  "id": "5"
}
```

---

## Performance Tuning

### JVM Tuning

```bash
# Heap memory
-Xms256m -Xmx512m

# GC tuning (G1GC)
-XX:+UseG1GC -XX:MaxGCPauseMillis=200

# Thread stack
-Xss256k

# Full command
java -Xms256m -Xmx512m \
  -XX:+UseG1GC -XX:MaxGCPauseMillis=200 \
  -jar target/otel-mcp-server-1.0.0-jar-with-dependencies.jar
```

### Configuration Optimization

```bash
# Increase batch size for high volume
BATCH_SIZE=256

# Increase queue for bursty traffic
QUEUE_SIZE=4096

# Adjust thread pool
THREAD_POOL_SIZE=32

# Increase batch timeout for better latency
BATCH_TIMEOUT_MS=10000
```

### Memory Limits

```bash
# Kubernetes resource limits
resources:
  limits:
    memory: "512Mi"
    cpu: "1000m"
  requests:
    memory: "256Mi"
    cpu: "500m"
```

---

## Troubleshooting

### Common Issues

#### 1. **TLS Connection Failed**
```
ERROR: io.grpc.netty.shaded.io.grpc.netty.NettyServerProvider - Failed to create server
```
**Solution:**
```bash
# Check certificate validity
openssl x509 -in /etc/certs/server.crt -text -noout

# Regenerate self-signed certificate
# Or disable TLS for debugging:
TLS_ENABLED=false
```

#### 2. **Out of Memory (OOM)**
```
ERROR: java.lang.OutOfMemoryError: Java heap space
```
**Solution:**
```bash
# Check memory usage
ps aux | grep otel-mcp-server

# Increase heap (up to 800m max)
java -Xmx800m -jar otel-mcp-server-1.0.0-jar-with-dependencies.jar

# OR reduce batch size
BATCH_SIZE=64
QUEUE_SIZE=1024
```

#### 3. **High Latency**
```
p95 latency > 500ms
```
**Solution:**
```bash
# Increase batch timeout
BATCH_TIMEOUT_MS=10000

# Check OTLP backend connectivity
telnet otel-collector.ops.svc.cluster.local 4317

# Monitor span queue depth
# Check logs for dropped spans
grep "dropping span" logs/otel-mcp-server.log
```

#### 4. **Authentication Failed**
```
ERROR: Invalid authentication token
```
**Solution:**
```bash
# Disable auth for debugging
AUTH_REQUIRED=false

# Verify API key format
# Must be at least 32 characters of alphanumeric + -_
```

---

## Testing

### Unit Tests

```bash
mvn test
mvn test -Dtest=OTelCollectorServiceTest
```

### Integration Tests

```bash
# Start test container
docker-compose -f docker-compose.test.yml up -d

# Run tests
mvn verify -Pintegration

# Cleanup
docker-compose -f docker-compose.test.yml down
```

### Load Testing

```bash
# Using Apache JMeter
jmeter -n -t load-test.jmx -l results.jtl -j jmeter.log

# Using hey
hey -n 10000 -c 100 -m POST \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","method":"ping","id":"1"}' \
  http://localhost:9090
```

---

## License

Apache License 2.0 - See [LICENSE](LICENSE)

---

## Support & Contributing

- **Documentation:** See `/docs` directory
- **Issues:** GitHub Issues
- **Security:** See [SECURITY.md](SECURITY.md)
- **Contributing:** See [CONTRIBUTING.md](CONTRIBUTING.md)

---

## Roadmap

- [ ] WebSocket support for real-time span streaming
- [ ] gRPC server for native clients
- [ ] Prometheus remote write exporter
- [ ] OpenSearch integration for span search
- [ ] Jaeger integration
- [ ] Advanced sampling strategies
- [ ] Dynamic configuration updates
- [ ] Service mesh integration (Istio)

---

**Made with ❤️ for EcoSkiller Platform**
