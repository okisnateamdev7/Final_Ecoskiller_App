# mcp-k8s-networkpolicy

**Ecoskiller | CAT-INFRA — Kubernetes NetworkPolicy**  
MCP Server in Java | 14 Tools | Priority: HIGH

Pod-level network security and traffic control for Ecoskiller's multi-cloud
GCP + AWS k3s clusters. Implements zero-trust network architecture via
Kubernetes NetworkPolicy resources enforced by Cilium (eBPF).

---

## Tools (14)

| # | Tool Name | Description | Role Required |
|---|-----------|-------------|---------------|
| 1 | `apply_network_policy` | Apply/update a NetworkPolicy YAML | WRITE |
| 2 | `get_network_policy` | Fetch a specific policy by name | READ |
| 3 | `list_network_policies` | List all policies, filter by namespace/label | READ |
| 4 | `delete_network_policy` | Delete a policy (with confirm gate) | ADMIN |
| 5 | `validate_network_policy` | Client + server-side dry-run validation | WRITE |
| 6 | `apply_default_deny` | Deploy zero-trust default-deny baseline | ADMIN |
| 7 | `check_pod_connectivity` | Analyse whether two pods can communicate | READ |
| 8 | `get_policy_violations` | Query violation logs from Prometheus + K8s Events | READ |
| 9 | `list_policy_metrics` | Enforcement metrics: allowed/denied/latency | READ |
| 10 | `get_namespace_isolation` | Full isolation posture report for a namespace | READ |
| 11 | `audit_policy_changes` | Policy change audit trail (SOC2/HIPAA/GDPR) | READ |
| 12 | `export_compliance_report` | SOC2/HIPAA/GDPR compliance snapshot | READ |
| 13 | `sync_policies_gitops` | Trigger ArgoCD/Flux reconcile | WRITE |
| 14 | `emergency_allow_traffic` | Temporary allow rule with TTL + audit trail | ADMIN |

---

## Requirements

- Java 17+
- Maven 3.8+ (for build)
- Kubernetes cluster (GCP k3s or AWS k3s) with Cilium CNI

---

## Build

```bash
mvn clean package -DskipTests
# Output: target/mcp-k8s-networkpolicy.jar
```

---

## Run the Server

```bash
# In-cluster (service account token auto-detected)
java -jar target/mcp-k8s-networkpolicy.jar

# Out-of-cluster (with kubeconfig token)
K8S_API_SERVER=https://your-cluster:6443 \
K8S_BEARER_TOKEN=your-token \
MCP_ROLE=WRITE \
java -jar target/mcp-k8s-networkpolicy.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Security Configuration

| Environment Variable | Description | Default |
|---------------------|-------------|---------|
| `MCP_ROLE` | Caller role: `READ`, `WRITE`, or `ADMIN` | `READ` |
| `MCP_API_KEY` | API key for request authentication | (none) |
| `MCP_AUTH_DISABLED` | Set `true` to disable auth (dev only) | `false` |
| `K8S_API_SERVER` | Kubernetes API server URL | Auto-detected (in-cluster) |
| `K8S_BEARER_TOKEN` | Service account bearer token | Auto-detected from SA mount |
| `K8S_INSECURE_TLS` | Skip TLS verification (dev only) | `false` |
| `PROMETHEUS_URL` | Prometheus base URL | `http://prometheus.monitoring.svc:9090` |
| `OPENSEARCH_URL` | OpenSearch URL for audit logs | (none) |
| `ARGOCD_API_URL` | ArgoCD API server URL | `http://argocd-server.argocd.svc` |
| `ARGOCD_TOKEN` | ArgoCD authentication token | (none) |

### RBAC Roles

- **READ** — list, get, metrics, audit, compliance (safe read-only operations)
- **WRITE** — apply, validate, sync (modify policies via GitOps)
- **ADMIN** — delete, emergency-allow, apply-default-deny (destructive/override operations)

### Protected Namespaces

The following namespaces require **ADMIN** role to target:
`kube-system`, `kube-public`, `cert-manager`, `monitoring`, `ops`

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "ecoskiller-k8s-networkpolicy": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-k8s-networkpolicy.jar"],
      "env": {
        "MCP_ROLE": "WRITE",
        "K8S_API_SERVER": "https://your-cluster:6443",
        "K8S_BEARER_TOKEN": "your-service-account-token"
      }
    }
  }
}
```

---

## Deploy to Kubernetes

```bash
# Apply RBAC (ServiceAccount + ClusterRole + ClusterRoleBinding)
kubectl apply -f k8s-rbac.yaml

# Verify service account
kubectl get serviceaccount ecoskiller-mcp-networkpolicy -n ops
```

---

## Run Tests

```bash
mvn test                  # quick pass/fail
mvn test -Dtest="*Test"   # all tests with names
```

---

## File Structure

```
mcp-k8s-networkpolicy/
├── pom.xml
├── k8s-rbac.yaml                          ← K8s ServiceAccount + RBAC + self-protection policy
├── claude_desktop_config.json             ← Claude Desktop config snippet
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp/networkpolicy/
    │   ├── server/
    │   │   └── McpNetworkPolicyServer.java ← Main MCP server (JSON-RPC 2.0 over stdio)
    │   ├── security/
    │   │   └── SecurityManager.java        ← API key auth + RBAC + input sanitisation
    │   ├── tools/
    │   │   ├── ToolHandler.java            ← Interface + schema helpers
    │   │   ├── ToolResult.java             ← Immutable result value class
    │   │   ├── ApplyNetworkPolicyTool.java
    │   │   ├── GetNetworkPolicyTool.java
    │   │   ├── ListNetworkPoliciesTool.java
    │   │   ├── DeleteNetworkPolicyTool.java
    │   │   ├── ValidateNetworkPolicyTool.java
    │   │   ├── ApplyDefaultDenyTool.java
    │   │   ├── CheckPodConnectivityTool.java
    │   │   ├── GetPolicyViolationsTool.java
    │   │   ├── ListPolicyMetricsTool.java
    │   │   ├── GetNamespaceIsolationTool.java
    │   │   ├── AuditPolicyChangesTool.java
    │   │   ├── ExportComplianceReportTool.java
    │   │   ├── SyncPoliciesGitOpsTool.java
    │   │   └── EmergencyAllowTrafficTool.java
    │   └── util/
    │       ├── K8sApiClient.java           ← Lightweight K8s API HTTP client
    │       └── PolicyYamlValidator.java    ← Client-side YAML structural + security validator
    └── test/java/com/ecoskiller/mcp/networkpolicy/
        └── McpNetworkPolicyServerTest.java ← JUnit 5 test suite (30+ tests)
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Security Architecture

```
Claude Desktop
     │ stdio (JSON-RPC)
     ▼
McpNetworkPolicyServer
     │
     ├── SecurityManager ──── 1. API key auth (MCP_API_KEY)
     │                   ├─── 2. RBAC (READ/WRITE/ADMIN per tool)
     │                   ├─── 3. Input sanitisation (injection/bomb patterns)
     │                   └─── 4. Namespace allowlist (protected namespaces)
     │
     ├── ToolHandler (14 tools)
     │        │
     │        ├── PolicyYamlValidator ── client-side YAML check before K8s API
     │        └── K8sApiClient ────────── HTTPS + Bearer token + TLS verify
     │                  │
     │                  ▼
     │           Kubernetes API Server
     │                  │
     │         ┌────────┴────────┐
     │    Cilium CNI          Prometheus
     │   (eBPF enforce)    (metrics/violations)
     │
     └── Audit trail → OpenSearch / ELK Stack
```
