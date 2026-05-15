# MinIO MCP Server - Build & Deployment Guide

## 📦 Deliverables

This package contains a production-ready, secure MinIO Object Storage MCP Server written in Java.

### Files Included:

1. **MinIOJavaMCPServer.java** - Main server implementation (1,200+ lines)
   - MCP Protocol 2.0 support
   - 13 S3 operations
   - Security & RBAC
   - Audit logging
   - Multi-tenant isolation

2. **pom.xml** - Maven build configuration
   - Dependency management
   - Fat JAR creation
   - Shade plugin for bundling
   - Code coverage tools

3. **Dockerfile** - Multi-stage container build
   - Secure base image (Alpine)
   - Non-root user
   - Health checks
   - Minimal attack surface

4. **kubernetes-deployment.yaml** - Complete K8s manifest
   - Deployment with 2 replicas
   - Service & ServiceAccount
   - RBAC configuration
   - PodDisruptionBudget
   - HorizontalPodAutoscaler
   - ServiceMonitor for Prometheus

5. **helm-values-prod.yaml** - Helm chart values
   - Production-ready configuration
   - Auto-scaling settings
   - Resource limits
   - Security policies
   - Environment-specific overrides

6. **README.md** - Comprehensive documentation
   - Architecture overview
   - Security architecture
   - Configuration guide
   - Usage examples
   - Troubleshooting

---

## 🔨 Build Instructions

### Prerequisites
```bash
java -version          # Verify Java 17+
mvn -version          # Verify Maven 3.9+
docker --version      # Verify Docker
kubectl version       # Verify Kubernetes CLI
```

### Step 1: Build JAR Locally

```bash
# Clone and navigate
git clone <repo>
cd minio-mcp-server

# Build fat JAR
mvn clean package -DskipTests

# Verify
ls -lh target/minio-mcp-server.jar
```

Expected output: ~50-100 MB JAR file

### Step 2: Build Docker Image

```bash
# Build image
docker build -t ecoskiller/minio-mcp-server:2.0 .

# Verify image
docker images | grep minio-mcp-server

# Test locally
docker run -it \
  -e MINIO_ENDPOINT="localhost:9000" \
  -e MINIO_ACCESS_KEY="minioadmin" \
  -e MINIO_SECRET_KEY="minioadmin" \
  ecoskiller/minio-mcp-server:2.0
```

### Step 3: Push to Registry

```bash
# Login to Harbor
docker login harbor.ecoskiller.internal

# Tag image
docker tag ecoskiller/minio-mcp-server:2.0 \
  harbor.ecoskiller.internal/ecoskiller/minio-mcp-server:2.0

# Push to registry
docker push harbor.ecoskiller.internal/ecoskiller/minio-mcp-server:2.0
```

---

## ☸️ Kubernetes Deployment

### Option 1: Direct Manifest

```bash
# Apply manifests
kubectl apply -f kubernetes-deployment.yaml

# Verify deployment
kubectl -n minio-mcp get pods
kubectl -n minio-mcp logs -f deployment/minio-mcp-server

# Check status
kubectl -n minio-mcp describe pod <pod-name>
```

### Option 2: Helm Chart (Recommended)

```bash
# Add Ecoskiller Helm repo
helm repo add ecoskiller https://charts.ecoskiller.com
helm repo update

# Install chart
helm install minio-mcp ecoskiller/minio-mcp \
  --namespace minio-mcp \
  --create-namespace \
  -f helm-values-prod.yaml \
  --set minio.accessKey=$ACCESS_KEY \
  --set minio.secretKey=$SECRET_KEY

# Verify installation
helm -n minio-mcp list
kubectl -n minio-mcp get all

# Upgrade chart
helm upgrade minio-mcp ecoskiller/minio-mcp \
  -f helm-values-prod.yaml
```

### Step 4: Verify Deployment

```bash
# Check pods are running
kubectl -n minio-mcp get pods -o wide

# Check service
kubectl -n minio-mcp get svc

# Test connectivity
kubectl -n minio-mcp exec -it deployment/minio-mcp-server -- \
  nc -zv minio.default.svc.cluster.local 9000

# View logs
kubectl -n minio-mcp logs -f deployment/minio-mcp-server --all-containers=true
```

---

## 🔐 Security Configuration

### Create Kubernetes Secret

```bash
# Create secret with MinIO credentials
kubectl -n minio-mcp create secret generic minio-mcp-credentials \
  --from-literal=MINIO_ENDPOINT="minio.default.svc.cluster.local:9000" \
  --from-literal=MINIO_ACCESS_KEY="your-access-key" \
  --from-literal=MINIO_SECRET_KEY="your-secret-key" \
  --from-literal=TENANT_ID="acme-corp" \
  --from-literal=SERVICE_ACCOUNT_ID="minio-mcp-server"

# Verify secret was created
kubectl -n minio-mcp get secret minio-mcp-credentials
```

### Seal Secret with Sealed-Secrets

```bash
# Install sealed-secrets controller (if not already)
kubectl apply -f https://github.com/bitnami-labs/sealed-secrets/releases/download/v0.24.0/controller.yaml

# Create secret YAML
cat > k8s-secret.yaml << 'YAML'
apiVersion: v1
kind: Secret
metadata:
  name: minio-mcp-credentials
  namespace: minio-mcp
type: Opaque
stringData:
  MINIO_ENDPOINT: "minio.default.svc.cluster.local:9000"
  MINIO_ACCESS_KEY: "your-access-key"
  MINIO_SECRET_KEY: "your-secret-key"
  TENANT_ID: "acme-corp"
YAML

# Seal the secret
kubeseal -f k8s-secret.yaml -w k8s-secret-sealed.yaml

# Apply sealed secret
kubectl apply -f k8s-secret-sealed.yaml
```

### Enable RBAC and NetworkPolicies

```bash
# The kubernetes-deployment.yaml already includes:
# - ServiceAccount with proper RBAC
# - Restricted permissions to only read minio-mcp-credentials
# - Network policies limiting egress to MinIO namespace
# - Pod security context with minimal privileges

# Verify RBAC is working
kubectl auth can-i get secrets --as=system:serviceaccount:minio-mcp:minio-mcp-server
```

---

## 📊 Monitoring & Observability

### Setup Prometheus Scraping

```bash
# The kubernetes-deployment.yaml includes ServiceMonitor
# Prometheus Operator will automatically discover it

# Verify ServiceMonitor
kubectl -n minio-mcp get servicemonitor

# Check Prometheus targets
kubectl -n monitoring port-forward svc/prometheus 9090:9090
# Visit: http://localhost:9090/targets
```

### View Metrics

```bash
# Port-forward to Prometheus
kubectl -n monitoring port-forward svc/prometheus 9090:9090

# Query in Prometheus:
# - minio_mcp_requests_total{method="put_object"}
# - minio_mcp_operations_duration_seconds{quantile="0.95"}
# - minio_mcp_audit_events_total
```

### Setup Grafana Dashboard

```bash
# Port-forward to Grafana
kubectl -n monitoring port-forward svc/grafana 3000:3000

# Login: admin / admin
# Add Prometheus data source: http://prometheus:9090
# Import dashboard ID: 12345 (MinIO MCP Server)
```

---

## 🧪 Testing

### Local Testing

```bash
# Build and run locally
mvn clean package -DskipTests

# Start local MinIO (Docker)
docker run -d \
  -p 9000:9000 \
  -p 9001:9001 \
  -e MINIO_ROOT_USER=minioadmin \
  -e MINIO_ROOT_PASSWORD=minioadmin \
  --name minio \
  minio/minio:latest server /data

# Run MCP server
java -jar target/minio-mcp-server.jar

# In another terminal, test with curl
echo '{"jsonrpc":"2.0","id":1,"method":"tools/list"}' | nc localhost 9000
```

### Kubernetes Integration Testing

```bash
# Create test pod
kubectl -n minio-mcp run test-client \
  --image=curlimages/curl:latest \
  --rm -it --restart=Never \
  -- sh

# Inside pod, test connectivity
curl -X POST http://minio-mcp-server-svc:9000 \
  -d '{"jsonrpc":"2.0","id":1,"method":"initialize"}'
```

### Load Testing

```bash
# Install hey load tester
go install github.com/rakyll/hey@latest

# Run load test (100 concurrent requests)
for i in {1..1000}; do
  echo '{"jsonrpc":"2.0","id":'$i',"method":"list_buckets"}' 
done | hey -n 1000 -c 100 -m POST http://localhost:9000
```

---

## 📈 Performance Tuning

### JVM Optimization

```bash
# Edit helm-values-prod.yaml:
jvm:
  heapMin: 1024m      # Increase for high-throughput
  heapMax: 2048m
  gcFlags: "-XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# Or environment variable:
export JAVA_OPTS="-XX:+UseG1GC -Xms1g -Xmx2g -XX:+ParallelRefProcEnabled"
```

### Kubernetes Resources

```bash
# Increase resource limits
kubectl -n minio-mcp patch deployment minio-mcp-server --type='json' \
  -p='[{"op": "replace", "path": "/spec/template/spec/containers/0/resources/limits/memory", "value":"2Gi"}]'

# Increase replica count
kubectl -n minio-mcp scale deployment minio-mcp-server --replicas=5
```

### MinIO Configuration

```bash
# Optimize MinIO client connection pool
MINIO_POOL_SIZE=50
MINIO_CONNECTION_TIMEOUT=30s
MINIO_READ_TIMEOUT=60s
```

---

## 🐛 Troubleshooting

### Pod fails to start

```bash
# Check events
kubectl -n minio-mcp describe pod <pod-name>

# Check logs
kubectl -n minio-mcp logs <pod-name> --all-containers=true

# Check resource constraints
kubectl top node
kubectl top pods -n minio-mcp
```

### Cannot connect to MinIO

```bash
# Verify MinIO is running
kubectl get pods -n storage | grep minio

# Check networking
kubectl exec -it deployment/minio-mcp-server -n minio-mcp -- \
  nslookup minio.default.svc.cluster.local

# Check credentials
kubectl get secret minio-mcp-credentials -n minio-mcp -o yaml
```

### High latency or timeouts

```bash
# Check network policies
kubectl get networkpolicy -n minio-mcp

# Increase timeouts in values
helm upgrade minio-mcp ecoskiller/minio-mcp \
  --set env.CONNECTION_TIMEOUT_MS=60000 \
  --set env.READ_TIMEOUT_MS=120000
```

---

## 📝 Production Checklist

- [ ] Code reviewed and approved
- [ ] Tests passing (unit, integration, load)
- [ ] Docker image scanned for vulnerabilities (Trivy)
- [ ] Helm chart values reviewed
- [ ] Kubernetes RBAC configured correctly
- [ ] Secrets stored in sealed-secrets or HashiCorp Vault
- [ ] Network policies enforced
- [ ] Resource limits and requests set appropriately
- [ ] Monitoring and alerting configured
- [ ] Log aggregation setup (ELK/Loki)
- [ ] Backup and disaster recovery tested
- [ ] Documentation updated
- [ ] Runbooks created
- [ ] On-call rotation established
- [ ] Post-deployment verification completed

---

## 🚀 Deployment Steps (Final)

1. **Build & Test**
   ```bash
   mvn clean package
   docker build -t ecoskiller/minio-mcp-server:2.0 .
   ```

2. **Push to Registry**
   ```bash
   docker push harbor.ecoskiller.internal/ecoskiller/minio-mcp-server:2.0
   ```

3. **Deploy to Kubernetes**
   ```bash
   helm install minio-mcp ecoskiller/minio-mcp \
     -f helm-values-prod.yaml \
     --set minio.accessKey=$KEY \
     --set minio.secretKey=$SECRET
   ```

4. **Verify**
   ```bash
   kubectl -n minio-mcp get pods
   kubectl -n minio-mcp logs -f deployment/minio-mcp-server
   ```

5. **Monitor**
   ```bash
   kubectl -n minio-mcp port-forward svc/prometheus 9090:9090
   # Check http://localhost:9090/targets
   ```

---

## 📞 Support & Issues

- **GitHub Issues**: https://github.com/ecoskiller/minio-mcp-server
- **Documentation**: https://docs.ecoskiller.com/minio-mcp
- **Slack**: #platform-engineering
- **Email**: devops@ecoskiller.com

---

**Version 2.0** | Production Ready | Last Updated: March 10, 2025
