# mcp-rbac-k8s

**Ecoskiller | CAT-RBAC — Kubernetes Role-Based Access Control MCP Server**  
MCP Server in Java | 18 Agents | Priority: HIGH — Security-Critical

---

## Overview

This MCP server implements Ecoskiller's Kubernetes RBAC authorization framework as a
JSON-RPC 2.0 MCP server over stdio. It covers all core RBAC operations across Ecoskiller's
multi-cloud (GCP asia-south1 + AWS ap-south-1) k3s clusters with full GitOps integration
via ArgoCD, SOC2/HIPAA/GDPR audit logging, and multi-tenant isolation enforcement.

---

## Agents (18)

| # | Tool Name | Agent Class | Description |
|---|-----------|-------------|-------------|
| 1 | `rbac_check_access` | AccessCheckAgent | kubectl auth can-i equivalent — returns ALLOW/DENY |
| 2 | `rbac_create_role` | RoleAgent | Create namespace-scoped Role with validation |
| 3 | `rbac_update_role` | RoleAgent | Update Role rules with before/after audit diff |
| 4 | `rbac_delete_role` | RoleAgent | Safe-delete Role (checks active bindings) |
| 5 | `rbac_get_role` | RoleAgent | Get full Role definition + bound subjects |
| 6 | `rbac_list_roles` | RoleAgent | List/filter Roles across namespaces |
| 7 | `rbac_create_role_binding` | RoleBindingAgent | Bind Role to subjects (cross-tenant blocked) |
| 8 | `rbac_delete_role_binding` | RoleBindingAgent | Revoke binding with compliance audit trail |
| 9 | `rbac_list_role_bindings` | RoleBindingAgent | Query bindings by subject/role/namespace |
| 10 | `rbac_create_cluster_role` | ClusterRoleAgent | Create ClusterRole with justification gate |
| 11 | `rbac_delete_cluster_role` | ClusterRoleAgent | Delete ClusterRole (requires CONFIRM_DELETE) |
| 12 | `rbac_list_cluster_roles` | ClusterRoleAgent | List ClusterRoles incl. system roles |
| 13 | `rbac_bind_cluster_role` | ClusterRoleAgent | ClusterRoleBinding or ns-scoped binding |
| 14 | `rbac_list_service_accounts` | AccessCheckAgent | List SAs with over-privilege detection |
| 15 | `rbac_get_user_permissions` | AccessCheckAgent | Aggregate all permissions for a user/SA |
| 16 | `rbac_audit_log_query` | AuditQueryAgent | Query authorization audit log (SOC2/GDPR) |
| 17 | `rbac_validate_policy` | PolicyAgent | YAML validation CI/CD gate (pre-commit) |
| 18 | `rbac_sync_gitops_status` | GitOpsAgent | ArgoCD sync drift detection GCP + AWS |

---

## Security Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    MCP Client (Claude)                       │
└────────────────────────┬────────────────────────────────────┘
                         │  stdin/stdout  JSON-RPC 2.0
┌────────────────────────▼────────────────────────────────────┐
│              McpRbacServer (stdio transport)                  │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  InputSanitizer — strips injections, null bytes      │   │
│  │  length limits, depth limits, pattern blocklist      │   │
│  └──────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  ToolRegistry — routes tools/call to agent handlers  │   │
│  └──────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  AuditLogger — append-only SOC2/GDPR audit trail     │   │
│  │  every TOOL_CALL, TOOL_RESULT, VALIDATION_FAIL       │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
│  Agents:                                                     │
│  ├── RoleAgent          (tools 2–6)                         │
│  ├── RoleBindingAgent   (tools 7–9)  ← cross-tenant guard   │
│  ├── ClusterRoleAgent   (tools 10–13) ← justification gate  │
│  ├── AccessCheckAgent   (tools 1,14,15)                     │
│  ├── AuditQueryAgent    (tool 16)                           │
│  ├── PolicyAgent        (tool 17) ← CI/CD validation gate   │
│  └── GitOpsAgent        (tool 18) ← ArgoCD drift detection  │
└─────────────────────────────────────────────────────────────┘
                         │
         ┌───────────────┴───────────────┐
         ▼                               ▼
  GCP k3s (asia-south1)          AWS k3s (ap-south-1)
  Kubernetes API Server           Kubernetes API Server
  etcd (RBAC store)               etcd (RBAC store)
         │                               │
         └───────────────┬───────────────┘
                         ▼
                  ArgoCD GitOps Sync
               (single Git source of truth)
```

---

## Security Controls

| Control | Implementation |
|---------|---------------|
| **Input Sanitization** | `InputSanitizer` strips null bytes, control chars, template injection (`${`, `#{`), path traversal (`../`), max string 64KB, max depth 10 |
| **Wildcard Blocking** | `RoleAgent` rejects `*` verbs/resources in namespace Roles. ClusterRoleAgent warns but allows with justification |
| **Cross-Tenant Isolation** | `RoleBindingAgent` detects tenant namespace prefix mismatch and hard-blocks cross-tenant bindings |
| **System Role Protection** | `cluster-admin`, `system:*` roles cannot be deleted or overwritten |
| **Deletion Confirmation** | ClusterRole deletion requires `confirm: "CONFIRM_DELETE"` |
| **ClusterRole Justification** | ClusterRole creation requires 20+ char security justification |
| **Audit Logging** | Every tool call logged to `rbac-audit.log` + JUL stderr with timestamp, event type, tool name, context |
| **Prod CI Gate** | `rbac_validate_policy` blocks wildcard perms, `cluster-admin` bindings, wrong apiVersion in prod env |
| **Default Deny** | All access check decisions default to deny unless explicit role match found |

---

## File Structure

```
mcp-rbac-k8s/
├── pom.xml                                          ← Maven build (Java 17, fat JAR)
├── claude_desktop_config.json                       ← Claude Desktop config snippet
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp/rbac/
    │   ├── server/
    │   │   └── McpRbacServer.java                   ← Entry point, JSON-RPC 2.0 router
    │   ├── tools/
    │   │   ├── ToolRegistry.java                    ← Registers all 18 agents
    │   │   ├── SchemaBuilder.java                   ← JSON Schema builder + exceptions
    │   │   ├── RoleAgent.java                       ← Tools 2–6 (Role CRUD)
    │   │   ├── RoleBindingAgent.java                ← Tools 7–9 (Binding CRUD)
    │   │   ├── ClusterRoleAgent.java                ← Tools 10–13 (ClusterRole)
    │   │   ├── AccessCheckAgent.java                ← Tools 1, 14, 15
    │   │   └── AgentImplementations.java            ← Tools 16 (Audit), 17 (Policy), 18 (GitOps)
    │   ├── model/
    │   │   ├── RbacRole.java                        ← Role/ClusterRole domain model
    │   │   ├── PolicyRule.java                      ← Single permission rule
    │   │   ├── RoleBinding.java                     ← Binding domain model
    │   │   └── Subject.java                         ← User/Group/ServiceAccount
    │   ├── security/
    │   │   └── InputSanitizer.java                  ← Input validation & sanitization
    │   └── audit/
    │       └── AuditLogger.java                     ← SOC2/GDPR/HIPAA audit trail
    └── test/java/com/ecoskiller/mcp/rbac/
        └── McpRbacServerTest.java                   ← 15 integration tests (JUnit 5)
```

---

## Requirements

- **Java 17+**
- **Maven 3.8+** (for build)
- No external runtime dependencies (Jackson bundled in fat JAR)

---

## Build

```bash
cd mcp-rbac-k8s
mvn clean package -q
# Output: target/mcp-rbac-k8s-1.0.0-shaded.jar
```

---

## Run the Server

```bash
java -jar target/mcp-rbac-k8s-1.0.0-shaded.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0.
All logs and audit entries go to **stderr** (clean stdout for protocol messages).

---

## Run Tests

```bash
mvn test
# or verbose:
mvn test -pl . --no-transfer-progress
```

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-rbac-k8s": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-rbac-k8s-1.0.0-shaded.jar"]
    }
  }
}
```

---

## Example Tool Calls

### Check if alice can delete secrets in production
```json
{
  "name": "rbac_check_access",
  "arguments": {
    "user": "alice@ecoskiller.com",
    "verb": "delete",
    "resource": "secrets",
    "namespace": "production"
  }
}
```

### Create a pod-reader role
```json
{
  "name": "rbac_create_role",
  "arguments": {
    "name": "pod-reader",
    "namespace": "production",
    "rules": [
      { "verbs": ["get","list","watch"], "resources": ["pods","pods/log"] }
    ]
  }
}
```

### Validate RBAC YAML before GitLab CI merge
```json
{
  "name": "rbac_validate_policy",
  "arguments": {
    "yaml_content": "apiVersion: rbac.authorization.k8s.io/v1\nkind: Role\n...",
    "environment": "prod",
    "strict_mode": true
  }
}
```

### Query denied API calls in last audit window
```json
{
  "name": "rbac_audit_log_query",
  "arguments": {
    "result_filter": "deny",
    "namespace_filter": "production",
    "limit": 50
  }
}
```

### Check ArgoCD sync drift on both clusters
```json
{
  "name": "rbac_sync_gitops_status",
  "arguments": {
    "cluster": "both",
    "only_drifted": true
  }
}
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Production Integration Notes

The current implementation uses **in-memory stores** for roles and bindings.
For production deployment, replace store operations in each Agent class with calls to
the **Kubernetes Java client** (`io.fabric8:kubernetes-client` or `io.kubernetes:client-java`):

| Agent method | Production replacement |
|---|---|
| `RoleAgent.createRole()` | `client.rbac().roles().inNamespace(ns).create(role)` |
| `AccessCheckAgent.checkAccess()` | `client.authorization().v1().subjectAccessReview().create(sar)` |
| `AuditQueryAgent.queryAuditLog()` | ELK/Datadog query via REST API |
| `GitOpsAgent.syncStatus()` | ArgoCD API `GET /api/v1/applications/{name}` |

---

## Compliance Mapping

| Requirement | RBAC Control |
|---|---|
| SOC 2 — Access Control | Role definitions in Git, RoleBindings audited, quarterly review via `rbac_get_user_permissions` |
| SOC 2 — Audit Trail | `AuditLogger` records every RBAC operation with timestamp, user, action |
| HIPAA — Minimum Necessary | Least-privilege enforcement, wildcard blocking, over-privilege flagging |
| GDPR — Data Access Restriction | Tenant namespace isolation, cross-tenant binding hard-block |
| Change Management | All RBAC YAMLs version-controlled in Git, validated by `rbac_validate_policy` CI gate |

---

*Last Updated: March 2025 | Version: 1.0.0 | Status: Production-Ready*
