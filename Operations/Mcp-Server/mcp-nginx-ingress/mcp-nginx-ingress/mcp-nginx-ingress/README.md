# mcp-nginx-ingress

**Ecoskiller | CAT-NGINX — Kubernetes Edge Gateway Management**  
MCP Server in Java | 18 Tools | Priority: HIGH | Transport: stdio | Protocol: JSON-RPC 2.0

---

## Overview

The NGINX Ingress Controller MCP Server exposes every operational capability of the
Ecoskiller Kubernetes edge gateway as AI-callable tools. It lets Claude Desktop (or
any MCP client) manage ingress routes, TLS certificates, rate limits, WAF rules,
canary deployments, multi-cloud failover, and compliance auditing — all through
natural language.

**Architecture position:** Public Internet → Cloud LB (GCP/AWS) → NGINX Ingress
Controller → Internal Kubernetes services (assessment-service, candidate-service,
interview-engine, jitsi-signaling, session-manager…)

---

## Tools (18)

| # | Tool Name | Purpose |
|---|-----------|---------|
| 1 | `ingress_route_manager` | Create/update/delete Kubernetes Ingress routing rules (host + path → service) |
| 2 | `ssl_tls_manager` | TLS 1.2/1.3 config, cipher suites, HSTS, OCSP stapling, session cache |
| 3 | `rate_limit_controller` | Per-IP / per-user rate limits, burst allowance, token-bucket zones |
| 4 | `load_balancer_config` | Round-robin, least_conn, ip_hash, weighted upstream algorithms |
| 5 | `health_check_monitor` | HTTP/TCP backend health probes, failover, drain, unhealthy threshold |
| 6 | `waf_security_manager` | ModSecurity WAF: SQLi, XSS, command injection, path traversal rules |
| 7 | `auth_enforcement` | OAuth2, OIDC, JWT, mTLS, API key — enforced at edge before backend |
| 8 | `canary_deployment` | Blue-green traffic splitting by weight % with auto-rollback support |
| 9 | `certificate_lifecycle` | Let's Encrypt cert provisioning, auto-renewal, expiry monitoring |
| 10 | `traffic_shaping` | QoS: connection limits, request queuing, gzip/brotli, HTTP/2 |
| 11 | `websocket_proxy` | WebSocket upgrade proxying for Jitsi signaling, live notifications |
| 12 | `request_rewrite_engine` | Header injection/rewrite, URL rewriting, CRLF-safe manipulation |
| 13 | `observability_exporter` | Prometheus metrics, ELK logs, Jaeger traces, Grafana dashboards |
| 14 | `backend_upstream_manager` | Add/remove/drain backend pod IPs, zero-downtime rolling updates |
| 15 | `configmap_hot_reload` | NGINX ConfigMap hot reload (zero-downtime), validate, diff |
| 16 | `multi_cloud_ingress` | GCP GKE + AWS EKS sync, DNS failover, DR status (RTO ~30s) |
| 17 | `ddos_protection` | IP/CIDR blocking, per-IP connection limits, anti-scraping |
| 18 | `audit_compliance` | DPDP Act 2023 / GDPR compliance reports, audit log export |

---

## Requirements

- **Java 11+** (JRE sufficient to run; JDK needed only to compile)
- No external Maven/Gradle dependencies — pure Java stdlib
- Kubernetes cluster access (kubectl, kubeconfig) for live operations

---

## Quick Start

### 1. Compile

```bash
# With JDK 11+
mkdir -p out
find src -name "*.java" > sources.txt
javac -source 11 -target 11 -d out @sources.txt

# Or use the build script
chmod +x build.sh && ./build.sh
```

### 2. Run the server

```bash
java -cp out com.ecoskiller.mcp.nginx.NginxIngressMcpServer
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0.

### 3. Build a runnable JAR

```bash
./build.sh
java -jar mcp-nginx-ingress.jar
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-nginx-ingress": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-nginx-ingress/mcp-nginx-ingress.jar"]
    }
  }
}
```

Or run directly from compiled classes:

```json
{
  "mcpServers": {
    "mcp-nginx-ingress": {
      "command": "java",
      "args": ["-cp", "/absolute/path/to/mcp-nginx-ingress/out",
               "com.ecoskiller.mcp.nginx.NginxIngressMcpServer"]
    }
  }
}
```

---

## Configuration (Environment Variables)

All config is read from environment variables at startup — no hardcoded secrets.

| Variable | Default | Description |
|----------|---------|-------------|
| `K8S_API_SERVER` | `https://kubernetes.default.svc` | Kubernetes API server URL |
| `KUBE_NAMESPACE` | `default` | Target Kubernetes namespace |
| `INGRESS_CLASS_NAME` | `nginx` | Kubernetes ingressClassName |
| `RATE_LIMIT_RPS` | `100` | Default per-IP rate limit (req/sec) |
| `RATE_LIMIT_BURST` | `200` | Default burst allowance |
| `MAX_CONN_PER_IP` | `20` | Max concurrent connections per IP |
| `SSL_SESSION_CACHE_MB` | `50` | TLS session cache size (MB) |
| `TLS_PROTOCOLS` | `TLSv1.2 TLSv1.3` | Allowed TLS versions |
| `PROMETHEUS_ENDPOINT` | `http://prometheus:9090` | Prometheus server URL |
| `HEALTH_CHECK_INTERVAL` | `10` | Backend health check interval (sec) |
| `HEALTH_CHECK_TIMEOUT` | `5` | Health check timeout (sec) |
| `UNHEALTHY_THRESHOLD` | `3` | Failures before marking backend down |
| `WAF_ENABLED` | `false` | Enable ModSecurity WAF globally |
| `MTLS_ENABLED` | `false` | Enable mutual TLS globally |
| `HSTS_PRELOAD` | `true` | Enable HSTS preload |
| `HSTS_MAX_AGE` | `31536000` | HSTS max-age in seconds (1 year) |
| `PRIMARY_CLOUD` | `gcp` | Primary cloud provider |
| `PRIMARY_REGION` | `asia-south1` | Primary region |
| `SECONDARY_CLOUD` | `aws` | Secondary (DR) cloud provider |
| `SECONDARY_REGION` | `ap-southeast-1` | Secondary region |
| `LOG_LEVEL` | `INFO` | Log level (DEBUG/INFO/WARN/ERROR) |

Example with custom values:

```bash
export KUBE_NAMESPACE=production
export RATE_LIMIT_RPS=200
export WAF_ENABLED=true
export LOG_LEVEL=DEBUG
java -jar mcp-nginx-ingress.jar
```

---

## Run Tests

```bash
# Compile first
./build.sh

# Quick pass/fail
java -cp out com.ecoskiller.mcp.nginx.NginxIngressMcpTestRunner

# With full JSON output
java -cp out com.ecoskiller.mcp.nginx.NginxIngressMcpTestRunner --verbose
```

Test coverage:
- `initialize` handshake
- `ping` / `tools/list`
- All 18 tools with valid arguments
- 5 security rejection tests (shell injection, invalid hostname, out-of-range values, CRLF injection, invalid CIDR)
- Unknown method error handling

---

## Security Design

### Input Validation (RequestValidator.java)
Every argument passes through `RequestValidator` before reaching any tool:

| Check | Details |
|-------|---------|
| **Hostname validation** | RFC-1123 regex, wildcards allowed (`*.ecoskiller.com`), max 253 chars |
| **Path validation** | Must start with `/`, allowlisted charset, no `../` traversal |
| **Shell injection** | Blocks `;`, `&`, `\|`, `` ` ``, `$`, `<`, `>`, `\\` in all string args |
| **CRLF injection** | Header values validated — `\r` and `\n` rejected |
| **CIDR validation** | Format + octet range + prefix range checked |
| **K8s name validation** | RFC-1123 subdomain format enforced |
| **Port range** | 1–65535 enforced |
| **Enum allowlists** | `action`, `algorithm`, `authType`, `logLevel`, `pathType` etc. |
| **Depth limiting** | JSON nesting capped at depth 5 to prevent stack overflow |
| **Length limiting** | All strings capped at 4096 chars; headers at 512 |
| **Deny by default** | Unknown tools and unknown action values rejected |

### No Secrets in Logs
- `ServerConfig.toString()` never prints sensitive values
- Auth URLs truncated in error messages
- `sanitise()` strips file paths and class names from user-facing errors

### JSON-RPC Envelope Validation
- `jsonrpc: "2.0"` required
- Method names validated against `[a-zA-Z0-9_/\-]+` pattern
- All fields depth-checked before dispatch

---

## File Structure

```
mcp-nginx-ingress/
├── build.sh                              ← Compile + build JAR
├── README.md                             ← This file
├── claude_desktop_config.json            ← Claude Desktop config snippet
└── src/main/java/com/ecoskiller/mcp/nginx/
    ├── NginxIngressMcpServer.java         ← Main server (JSON-RPC loop, dispatcher)
    ├── NginxIngressMcpTestRunner.java     ← Test runner (all 18 tools + security)
    ├── NginxTool.java                     ← Tool interface
    ├── config/
    │   └── ServerConfig.java              ← Environment-based configuration
    ├── security/
    │   └── RequestValidator.java          ← Security gate (input validation)
    ├── util/
    │   ├── JsonUtil.java                  ← Zero-dependency JSON parser/serialiser
    │   └── Logger.java                    ← Structured logger (stderr)
    └── tools/
        ├── BaseNginxTool.java             ← Shared helpers (YAML gen, response builders)
        ├── IngressRouteManagerTool.java   ← Tool 1
        ├── SslTlsManagerTool.java         ← Tool 2
        ├── RateLimitControllerTool.java   ← Tool 3
        ├── LoadBalancerConfigTool.java    ← Tool 4
        ├── HealthCheckMonitorTool.java    ← Tool 5
        ├── WafSecurityManagerTool.java    ← Tool 6
        ├── AuthEnforcementTool.java       ← Tool 7
        ├── CanaryDeploymentTool.java      ← Tool 8
        ├── CertificateLifecycleTool.java  ← Tool 9
        ├── TrafficShapingTool.java        ← Tool 10
        ├── WebSocketProxyTool.java        ← Tool 11
        ├── RequestRewriteEngineTool.java  ← Tool 12
        ├── ObservabilityExporterTool.java ← Tool 13
        ├── BackendUpstreamManagerTool.java← Tool 14
        ├── ConfigMapHotReloadTool.java    ← Tool 15
        ├── MultiCloudIngressTool.java     ← Tool 16
        ├── DdosProtectionTool.java        ← Tool 17
        └── AuditComplianceTool.java       ← Tool 18
```

---

## Protocol

| Property | Value |
|----------|-------|
| Transport | **stdio** (stdin/stdout) |
| Format | **JSON-RPC 2.0** |
| MCP Version | **2024-11-05** |
| Methods | `initialize`, `tools/list`, `tools/call`, `ping`, `shutdown` |
| Java Version | 11+ |
| Dependencies | None (pure stdlib) |

---

## Example Tool Calls (JSON-RPC)

### List all ingress routes
```json
{
  "jsonrpc": "2.0",
  "method": "tools/call",
  "id": 1,
  "params": {
    "name": "ingress_route_manager",
    "arguments": { "action": "list" }
  }
}
```

### Create a new route
```json
{
  "jsonrpc": "2.0",
  "method": "tools/call",
  "id": 2,
  "params": {
    "name": "ingress_route_manager",
    "arguments": {
      "action":     "create",
      "name":       "ecoskiller-v2-api",
      "host":       "api.ecoskiller.com",
      "path":       "/v2/assessments",
      "service":    "assessment-service-v2",
      "port":       8080,
      "certSecret": "ecoskiller-tls"
    }
  }
}
```

### Enable canary deployment (10% traffic to new version)
```json
{
  "jsonrpc": "2.0",
  "method": "tools/call",
  "id": 3,
  "params": {
    "name": "canary_deployment",
    "arguments": {
      "action":        "enable",
      "service":       "assessment-service-v2",
      "stableService": "assessment-service-v1",
      "weight":        10,
      "host":          "api.ecoskiller.com",
      "path":          "/assessments"
    }
  }
}
```

### Block a malicious IP
```json
{
  "jsonrpc": "2.0",
  "method": "tools/call",
  "id": 4,
  "params": {
    "name": "ddos_protection",
    "arguments": {
      "action": "block-ip",
      "cidr":   "203.0.113.0/24"
    }
  }
}
```

### Generate compliance report
```json
{
  "jsonrpc": "2.0",
  "method": "tools/call",
  "id": 5,
  "params": {
    "name": "audit_compliance",
    "arguments": { "action": "report" }
  }
}
```

---

## Ecoskiller Architecture Context

```
Internet / External Clients
(Candidates, Recruiters, API Consumers, Mobile Apps)
           │  HTTP/HTTPS (Port 80, 443)
           ▼
┌──────────────────────────────────────┐
│  GCP Cloud LB / AWS ALB              │  ← Cloud load balancer
│  (distributes to NGINX pods)         │
└──────────────┬───────────────────────┘
               │
┌──────────────▼───────────────────────┐
│  NGINX Ingress Controller (DaemonSet)│  ← THIS MCP SERVER MANAGES
│  • Request routing                   │
│  • SSL/TLS termination               │
│  • Rate limiting + DDoS protection   │
│  • Auth enforcement (OAuth2/JWT)     │
│  • WAF (ModSecurity)                 │
│  • WebSocket proxying                │
└──────┬─────────┬──────────┬──────────┘
       │         │          │
  ┌────▼────┐ ┌──▼─────┐ ┌─▼──────────┐
  │Candidate│ │Session │ │Interview   │
  │Service  │ │Manager │ │Engine      │
  └─────────┘ └────────┘ └────────────┘
       Kubernetes Internal Service Mesh
```

**Performance:**
- NGINX latency overhead: <5ms
- Handles 50,000+ req/sec per pod
- 10 DaemonSet pods → 500,000 req/sec cluster capacity
- Monthly cost: ~₹3,154 (vs ₹350,000+ for proprietary F5 BIG-IP)

---

## Related Ecoskiller MCP Servers

| Server | Category | Tools |
|--------|----------|-------|
| `mcp-17-royalty` | Marketplace & Royalty | 18 tools |
| **`mcp-nginx-ingress`** | **Edge Gateway** | **18 tools** |
| `mcp-assessment-engine` | Assessment Platform | TBD |
| `mcp-session-manager` | Session Lifecycle | TBD |
