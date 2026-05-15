# OpenTofu MCP Server (Java)

**Infrastructure-as-Code Orchestration via Model Context Protocol (MCP)**

[![Java 17+](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/technologies/downloads/#java17)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![OpenTofu](https://img.shields.io/badge/OpenTofu-1.6.0+-green.svg)](https://opentofu.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](LICENSE)

---

## 🎯 Overview

The **OpenTofu MCP Server** is a production-ready, secure Infrastructure-as-Code (IaC) orchestration service that enables AI agents to manage cloud infrastructure declaratively across GCP and AWS. It implements the Model Context Protocol (MCP) using JSON-RPC 2.0 for seamless integration with Claude and other AI platforms.

### Key Features

✅ **Multi-Cloud Management** - Unified OpenTofu operations across GCP and AWS  
✅ **15 Integrated Tools** - plan, apply, destroy, state management, cost estimation, etc.  
✅ **Secure by Default** - TLS 1.3, mTLS, multi-tenant isolation, authentication  
✅ **State Management** - Distributed state locking, snapshots, rollback support  
✅ **Environment Isolation** - dev, test, stage, prod workspaces  
✅ **Audit Logging** - Full compliance with audit trails  
✅ **JSON-RPC 2.0 Compliant** - Full MCP protocol support  
✅ **Production Ready** - Kubernetes-native, comprehensive monitoring  

---

## 📦 Architecture

### Components

```
OpenTofu MCP Server
├── JSON-RPC 2.0 Handler
│   ├── initialize
│   ├── tools/list
│   ├── tools/call
│   ├── state/list
│   ├── state/lock
│   ├── state/unlock
│   └── ping
├── OpenTofu Orchestrator
│   ├── plan, apply, destroy
│   ├── import, refresh, validate
│   ├── workspace management
│   ├── state operations
│   └── cost estimation
├── State Manager
│   ├── Lock management (DynamoDB/GCS)
│   ├── State snapshots
│   ├── Concurrency control
│   └── Drift detection
└── Security Layer
    ├── TLS 1.3/mTLS
    ├── Multi-tenant isolation
    ├── Request validation
    ├── Rate limiting
    └── Audit logging
```

### 15 Available Tools

| # | Tool | Purpose |
|----|------|---------|
| 1 | `plan` | Generate execution plan |
| 2 | `apply` | Apply infrastructure changes |
| 3 | `destroy` | Destroy managed infrastructure |
| 4 | `import` | Import existing resources |
| 5 | `refresh` | Update local state |
| 6 | `validate` | Validate configuration |
| 7 | `fmt` | Format configuration files |
| 8 | `init` | Initialize working directory |
| 9 | `workspace_list` | List workspaces |
| 10 | `workspace_select` | Select workspace |
| 11 | `workspace_create` | Create workspace |
| 12 | `state_list` | List state resources |
| 13 | `state_show` | Show resource details |
| 14 | `state_rm` | Remove from state |
| 15 | `cost_estimate` | Estimate cost impact |

---

## 🚀 Quick Start

### Prerequisites

```bash
java -version          # Java 17+
mvn -version          # Maven 3.8+
tofu -version         # OpenTofu 1.6.0+
```

### Build

```bash
git clone https://github.com/ecoskiller/opentofu-mcp-java.git
cd opentofu-mcp-java

# Build with Maven
mvn clean package

# Create fat JAR
mvn assembly:single
```

### Run Locally

```bash
# Configure environment (optional)
export SERVER_PORT=9092
export OPENTOFU_WORKING_DIR=/path/to/terraform
export STATE_BACKEND=s3
export AWS_REGION=us-east-1
export TLS_ENABLED=false

# Run server
java -jar target/opentofu-mcp-server-1.0.0-jar-with-dependencies.jar
```

### Test Connection

```bash
# Ping server
echo '{"jsonrpc":"2.0","method":"ping","id":"1"}' | nc localhost 9092

# Initialize
echo '{"jsonrpc":"2.0","method":"initialize","id":"2"}' | nc localhost 9092

# List tools
echo '{"jsonrpc":"2.0","method":"tools/list","id":"3"}' | nc localhost 9092

# Execute plan for dev environment
curl -X POST http://localhost:9092 \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "method": "tools/call",
    "params": {
      "name": "plan",
      "arguments": {
        "environment": "dev",
        "workspace": "dev"
      }
    },
    "id": "4"
  }'
```

---

## 🐳 Docker Deployment

### Build Docker Image

```bash
# Build
docker build -t ecoskiller/opentofu-mcp-server:1.0.0 .

# Run
docker run -p 9092:9092 \
  -e OPENTOFU_WORKING_DIR=/workspace \
  -e STATE_BACKEND=s3 \
  -e AWS_REGION=us-east-1 \
  -v ~/.aws:/root/.aws:ro \
  ecoskiller/opentofu-mcp-server:1.0.0
```

### Docker Compose (with local state)

```bash
# Start full stack
docker-compose up -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f opentofu-mcp-server

# Cleanup
docker-compose down -v
```

---

## ☸️ Kubernetes Deployment

### Prerequisites

```bash
kubectl cluster-info
kubectl get nodes
```

### Deploy

```bash
# Create namespace
kubectl create namespace infrastructure

# Create ConfigMap
kubectl create configmap opentofu-config \
  --from-file=application.conf \
  -n infrastructure

# Create secrets for AWS credentials
kubectl create secret generic aws-credentials \
  --from-literal=access-key-id=$AWS_ACCESS_KEY_ID \
  --from-literal=secret-access-key=$AWS_SECRET_ACCESS_KEY \
  -n infrastructure

# Deploy
kubectl apply -f k8s-deployment.yaml

# Verify
kubectl get pods -n infrastructure
kubectl logs -n infrastructure -l app=opentofu-mcp-server -f

# Port-forward
kubectl port-forward -n infrastructure svc/opentofu-mcp-server 9092:9092
```

---

## 🔒 Security

### Features

✅ **TLS 1.3 & mTLS** - Encrypted transport with certificate validation  
✅ **Multi-Tenant Isolation** - Complete workspace isolation  
✅ **Authentication** - Optional API key authentication  
✅ **Authorization** - Role-based workspace access control  
✅ **Request Validation** - SQL injection & XSS protection  
✅ **Rate Limiting** - Per-client request throttling  
✅ **Audit Logging** - Complete operation audit trail  
✅ **State Locking** - Prevent concurrent modifications  

### Enable TLS

```bash
# Generate self-signed certificate
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout server.key -out server.crt \
  -subj "/CN=opentofu-mcp-server"

# Configure
export TLS_ENABLED=true
export TLS_CERT_PATH=/path/to/server.crt
export TLS_KEY_PATH=/path/to/server.key

# Run
java -jar opentofu-mcp-server-1.0.0-jar-with-dependencies.jar
```

### Enable Authentication

```bash
export AUTH_REQUIRED=true
export API_KEY=$(openssl rand -base64 32)

# Use in requests
curl -X POST https://localhost:9092 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $API_KEY" \
  -d '{...}'
```

See [SECURITY.md](SECURITY.md) for comprehensive security documentation.

---

## 📊 Configuration

### Environment Variables

```bash
# Server
SERVER_HOST=0.0.0.0
SERVER_PORT=9092
THREAD_POOL_SIZE=16

# OpenTofu
OPENTOFU_BINARY=tofu
OPENTOFU_WORKING_DIR=/workspace/terraform
STATE_BACKEND=s3  # s3, gcs, local

# AWS
AWS_REGION=us-east-1
AWS_ACCESS_KEY_ID=***
AWS_SECRET_ACCESS_KEY=***
S3_BUCKET=ecoskiller-terraform-state
DYNAMODB_TABLE=ecoskiller-terraform-locks

# GCP
GCP_PROJECT=ecoskiller-prod
GCP_REGION=us-central1
GOOGLE_APPLICATION_CREDENTIALS=/path/to/creds.json

# Security
TLS_ENABLED=true
TLS_CERT_PATH=/etc/certs/server.crt
TLS_KEY_PATH=/etc/certs/server.key
AUTH_REQUIRED=false
API_KEY=

# Environment
ENVIRONMENT=production
LOG_LEVEL=INFO
```

See [application.conf](src/main/resources/application.conf) for all options.

---

## 📚 API Reference

### JSON-RPC 2.0 Methods

#### `initialize`
Initialize the MCP server and retrieve capabilities.

```json
{
  "jsonrpc": "2.0",
  "method": "initialize",
  "id": "1"
}
```

#### `tools/list`
List all available tools.

```json
{
  "jsonrpc": "2.0",
  "method": "tools/list",
  "id": "2"
}
```

#### `tools/call`
Execute a tool.

```json
{
  "jsonrpc": "2.0",
  "method": "tools/call",
  "params": {
    "name": "plan",
    "arguments": {
      "environment": "prod",
      "workspace": "prod"
    }
  },
  "id": "3"
}
```

#### `state/list`
List resources in state.

```json
{
  "jsonrpc": "2.0",
  "method": "state/list",
  "params": {
    "environment": "prod"
  },
  "id": "4"
}
```

#### `state/lock` & `state/unlock`
Acquire/release state lock.

```json
{
  "jsonrpc": "2.0",
  "method": "state/lock",
  "params": {
    "environment": "prod"
  },
  "id": "5"
}
```

#### `ping`
Health check.

```json
{
  "jsonrpc": "2.0",
  "method": "ping",
  "id": "6"
}
```

See [API.md](API.md) for complete API documentation with examples.

---

## 🔧 Operations

### Plan and Apply Workflow

```bash
# 1. Generate plan
curl -X POST http://localhost:9092 \
  -d '{
    "jsonrpc":"2.0",
    "method":"tools/call",
    "params":{
      "name":"plan",
      "arguments":{"environment":"prod"}
    },
    "id":"1"
  }'

# 2. Review plan output (shown in response)

# 3. Apply changes
curl -X POST http://localhost:9092 \
  -d '{
    "jsonrpc":"2.0",
    "method":"tools/call",
    "params":{
      "name":"apply",
      "arguments":{"environment":"prod"}
    },
    "id":"2"
  }'
```

### Multi-Environment Management

```bash
# Plan for different environments
curl http://localhost:9092 -d '{...,"environment":"dev",...}'
curl http://localhost:9092 -d '{...,"environment":"test",...}'
curl http://localhost:9092 -d '{...,"environment":"stage",...}'
curl http://localhost:9092 -d '{...,"environment":"prod",...}'
```

### Workspace Operations

```bash
# List workspaces
curl http://localhost:9092 -d '{..., "name":"workspace_list",...}'

# Create workspace
curl http://localhost:9092 -d '{..., "name":"workspace_create", "arguments":{"workspaceName":"feature-x"},...}'

# Select workspace
curl http://localhost:9092 -d '{..., "name":"workspace_select", "arguments":{"workspaceName":"feature-x"},...}'
```

### State Management

```bash
# List state resources
curl http://localhost:9092 -d '{"method":"state/list", "params":{"environment":"prod"},...}'

# Show specific resource
curl http://localhost:9092 -d '{"method":"state/show", "params":{"environment":"prod"},...}'

# Remove from state
curl http://localhost:9092 -d '{"method":"state/rm", "params":{"environment":"prod", "address":"aws_instance.web"},...}'
```

---

## 📈 Monitoring & Observability

### Metrics

- `opentofu_plan_duration_seconds` - Plan execution time
- `opentofu_apply_duration_seconds` - Apply execution time
- `opentofu_state_lock_acquire_time` - Lock acquisition time
- `opentofu_requests_total` - Total requests by method
- `opentofu_errors_total` - Errors by type

### Logging

- Application logs: `logs/opentofu-mcp-server.log`
- Audit logs: `logs/audit.log` (all operations)
- Request logs: Structured JSON format with trace IDs

### Health Check

```bash
curl -X POST http://localhost:9092 \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","method":"ping","id":"health"}'

# Response
{
  "jsonrpc": "2.0",
  "result": {
    "status": "pong",
    "timestamp": 1700000000000
  },
  "id": "health"
}
```

---

## 🧪 Testing

### Unit Tests

```bash
mvn test
```

### Integration Tests

```bash
mvn verify -Pintegration
```

### Load Testing

```bash
# Using hey
hey -n 10000 -c 100 -m POST \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","method":"ping","id":"1"}' \
  http://localhost:9092
```

---

## 📋 Project Structure

```
opentofu-mcp-java/
├── pom.xml                          # Maven build
├── README.md                        # This file
├── API.md                           # API documentation
├── SECURITY.md                      # Security guide
├── Dockerfile                       # Container image
├── docker-compose.yml              # Local development
├── k8s-deployment.yaml             # Kubernetes manifests
├── build-and-deploy.sh             # Build script
│
└── src/main/java/com/ecoskiller/opentofu/
    ├── server/
    │   └── OpenTofuMCPServer.java          # Main server
    ├── orchestrator/
    │   └── OpenTofuOrchestrator.java       # IaC operations
    ├── state/
    │   └── StateManager.java               # State management
    ├── security/
    │   ├── SecurityManager.java            # Request validation
    │   └── TLSManager.java                 # Certificate handling
    ├── handler/
    │   ├── RequestHandler.java             # Interface
    │   └── HandlerImpl.java                 # Implementations
    ├── config/
    │   └── OpenTofuConfiguration.java      # Configuration
    └── utils/
        └── ...

└── src/main/resources/
    ├── application.conf            # HOCON configuration
    └── logback.xml                 # Logging configuration
```

---

## 🚢 Deployment Checklist

- [ ] Review security settings in SECURITY.md
- [ ] Generate TLS certificates
- [ ] Configure environment variables
- [ ] Set up S3/GCS state backend
- [ ] Test locally with Docker Compose
- [ ] Build Docker image
- [ ] Push to container registry
- [ ] Create Kubernetes namespace
- [ ] Apply ConfigMaps and Secrets
- [ ] Deploy to Kubernetes
- [ ] Verify pods are running
- [ ] Test with real Terraform configurations
- [ ] Enable monitoring and alerting
- [ ] Document runbooks

---

## 🆘 Troubleshooting

### Cannot connect to server
```bash
# Check if port is listening
netstat -tlnp | grep 9092

# Check logs
tail -f logs/opentofu-mcp-server.log
```

### TLS certificate errors
```bash
# Verify certificate
openssl x509 -in server.crt -text -noout

# Regenerate if needed
TLS_ENABLED=false java -jar opentofu-mcp-server-1.0.0-jar-with-dependencies.jar
```

### State lock timeout
```bash
# Release stuck lock manually
aws dynamodb delete-item \
  --table-name ecoskiller-terraform-locks \
  --key '{"ID":{"S":"prod"}}'
```

### Out of memory
```bash
# Increase JVM heap
java -Xmx2g -Xms512m -jar opentofu-mcp-server-1.0.0-jar-with-dependencies.jar
```

---

## 📞 Support

- **Documentation**: See README.md and API.md
- **Issues**: GitHub Issues
- **Security**: security@ecoskiller.com
- **Email**: ops@ecoskiller.com

---

## 📄 License

Apache License 2.0 - See [LICENSE](LICENSE)

---

## 🙏 Contributing

Contributions welcome! Please follow:
1. Code review via pull requests
2. Comprehensive tests for new features
3. Documentation updates
4. Security best practices

---

**Made with ❤️ for EcoSkiller Infrastructure Automation**

**Version**: 1.0.0  
**Status**: Production Ready ✅  
**Last Updated**: 2026-03-21
