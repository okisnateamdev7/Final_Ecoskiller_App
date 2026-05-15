# mcp-k3s-kubernetes

**Ecoskiller | CAT-K3S — k3s Kubernetes Orchestration MCP Server**  
Secure MCP Server in Java | 20 Agents | Priority: CRITICAL

---

## Agents (20)

| #  | Tool Name                  | Purpose                                         |
|----|----------------------------|-------------------------------------------------|
| 1  | `k3s_cluster_status`       | Full cluster health — nodes, pods, API, etcd    |
| 2  | `k3s_node_management`      | List, drain, cordon, uncordon nodes             |
| 3  | `k3s_pod_lifecycle`        | List, describe, logs, delete pods               |
| 4  | `k3s_deployment_manager`   | Apply, scale, rollback deployments              |
| 5  | `k3s_service_discovery`    | Services, CoreDNS, endpoints, load balancing    |
| 6  | `k3s_ingress_controller`   | NGINX Ingress, TLS (cert-manager), WAF, Keycloak|
| 7  | `k3s_horizontal_autoscaler`| HPA config, thresholds, scaling events          |
| 8  | `k3s_persistent_volume`    | PV/PVC, GCP/AWS storage, snapshots              |
| 9  | `k3s_config_secrets`       | ConfigMaps, Secrets audit (values never exposed)|
| 10 | `k3s_rbac_policy`          | Roles, bindings, privilege audit, PSP           |
| 11 | `k3s_network_policy`       | Cilium eBPF policies, default-deny, flows       |
| 12 | `k3s_rolling_update`       | Zero-downtime deploy, canary, auto-rollback     |
| 13 | `k3s_etcd_backup`          | etcd health, snapshots, compaction, leader      |
| 14 | `k3s_observability`        | Prometheus, Grafana, AlertManager, ELK, Fluent  |
| 15 | `k3s_gitops_argocd`        | ArgoCD sync, Helm, Kustomize, deploy history    |
| 16 | `k3s_multicloud_failover`  | GCP↔AWS failover, replication lag, DNS, RTO     |
| 17 | `k3s_security_scanner`     | Trivy CVE scan, Wazuh SIEM, CIS benchmark       |
| 18 | `k3s_disaster_recovery`    | Restore from etcd/PV snapshots, RTO/RPO verify  |
| 19 | `k3s_compliance_audit`     | API audit logs, SOC 2 evidence, RBAC changes    |
| 20 | `k3s_cluster_upgrade`      | Rolling master/worker upgrade, System Upgrade Controller |

---

## Security Features

| Feature              | Implementation                                         |
|----------------------|--------------------------------------------------------|
| Input validation     | Length limit (128KB), null-byte check                  |
| Method whitelist     | Only `initialize`, `ping`, `tools/list`, `tools/call`  |
| Tool name whitelist  | Exact-match against 20 allowed tool names              |
| Injection prevention | Shell, path traversal, YAML, SQL, XSS pattern blocking |
| Argument sanitization| Per-field regex + length check on all args             |
| Rate limiting        | 60 calls/min per tool (token-bucket, resets each minute)|
| Secrets redaction    | Passwords/tokens/keys redacted from all log output     |
| k8s name validation  | RFC-compliant namespace and resource name regex        |
| Audit logging        | Structured `[AUDIT][ts][type][category]` to stderr     |
| Confirm guard        | Destructive ops (restore, upgrade) require `confirm=CONFIRM` |

---

## Requirements

- **Java 17+** (LTS recommended)
- **Maven 3.8+** (build only)

---

## Build

```bash
cd mcp-k3s-kubernetes
mvn clean package -q
# Produces: target/mcp-k3s-kubernetes-1.0.0.jar (fat JAR with all deps)
```

---

## Run

```bash
java -jar target/mcp-k3s-kubernetes-1.0.0.jar
```

The server communicates via **stdin/stdout** (JSON-RPC 2.0 stdio transport).  
All logs and security audit events go to **stderr** only — stdout is clean JSON-RPC.

---

## Run Tests

```bash
mvn test               # All tests
mvn test -q            # Quiet mode
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-k3s-kubernetes": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/target/mcp-k3s-kubernetes-1.0.0.jar"]
    }
  }
}
```

---

## File Structure

```
mcp-k3s-kubernetes/
├── pom.xml                                              ← Maven build (Java 17, fat JAR)
├── claude_desktop_config.json                           ← Claude Desktop config snippet
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp/k3s/
    │   ├── server/
    │   │   └── McpK3sServer.java                        ← Main server + JSON-RPC dispatcher
    │   ├── agents/
    │   │   ├── K3sAgent.java                            ← Agent interface
    │   │   └── AllAgents.java                           ← All 20 agent implementations
    │   └── security/
    │       ├── SecurityValidator.java                   ← Input validation, rate limiting
    │       └── AuditLogger.java                         ← Structured audit logging
    └── test/java/com/ecoskiller/mcp/k3s/
        └── McpK3sServerTest.java                        ← All tests (agents + security)
```

---

## Protocol

- **Transport:** stdio (stdin/stdout)
- **Format:** JSON-RPC 2.0
- **MCP Version:** 2024-11-05
- **Methods:** `initialize`, `tools/list`, `tools/call`, `ping`

---

## Architecture Context

This MCP server instruments the **k3s Kubernetes layer** of Ecoskiller's multi-cloud platform:

- **GCP** asia-south1: 3 masters (e2-standard-2) + 10 workers (n2-standard-8)
- **AWS** ap-south-1: 3 masters (t3.medium) + 10 workers (c5.xlarge)
- **50+ microservices**: Scoring Engine, Assessment Orchestrator, Notification Service, Keycloak, Kafka, PostgreSQL, Redis, OpenSearch, etc.
- **GitOps**: ArgoCD + Helm + Kustomize watching self-hosted GitLab
- **Observability**: Prometheus + Grafana + ELK Stack + AlertManager
- **Security**: Cilium CNI, ModSecurity WAF, Trivy, Wazuh, RBAC, NetworkPolicy
