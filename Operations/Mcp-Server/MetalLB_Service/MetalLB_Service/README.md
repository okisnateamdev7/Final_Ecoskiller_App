# mcp-metallb

**Ecoskiller | CAT-OPS — MetalLB Bare-Metal Load Balancer**
MCP Server in Java | 20 Tools | Priority: HIGH | Secure

---

## Overview

MetalLB is the bare-metal load balancer powering Ecoskiller's self-managed k3s clusters
on GCP and AWS. This MCP Server exposes 20 tools covering every operational aspect:
IP pool management, BGP/L2 configuration, node health, metrics, failover simulation,
config validation, and troubleshooting.

---

## Tools (20)

| # | Tool Name | Category | Description |
|---|-----------|----------|-------------|
| 1 | `metallb_get_status` | Monitoring | Overall MetalLB health summary |
| 2 | `metallb_list_ip_pools` | IP Pools | List GCP/AWS pools with utilization |
| 3 | `metallb_allocate_ip` | IP Pools | Allocate next IP to a LoadBalancer service |
| 4 | `metallb_release_ip` | IP Pools | Release IP back to pool on service deletion |
| 5 | `metallb_check_pool_exhaustion` | IP Pools | Check if pools are near capacity (>90%) |
| 6 | `metallb_expand_pool` | IP Pools | Expand pool CIDR range, generate YAML |
| 7 | `metallb_get_bgp_status` | BGP | BGP session state for all peers |
| 8 | `metallb_configure_bgp` | BGP | Generate BGPPeer YAML manifest |
| 9 | `metallb_get_bgp_sessions` | BGP | Node-level BGP session + ECMP detail |
| 10 | `metallb_get_l2_status` | L2/ARP | L2 mode status, leader nodes, ARP state |
| 11 | `metallb_configure_l2` | L2/ARP | Generate L2Advertisement YAML |
| 12 | `metallb_list_services` | Services | All LoadBalancer services managed by MetalLB |
| 13 | `metallb_assign_service_ip` | Services | Generate service YAML with MetalLB annotations |
| 14 | `metallb_check_node_health` | Nodes | k3s node readiness + speaker pod status |
| 15 | `metallb_simulate_failover` | Nodes | Dry-run failover timeline (BGP or L2) |
| 16 | `metallb_get_metrics` | Metrics | Prometheus metrics + AlertManager rules |
| 17 | `metallb_get_config` | Config | Full MetalLB YAML configuration |
| 18 | `metallb_validate_config` | Config | 17-point config validation check |
| 19 | `metallb_audit_log` | Config | Recent allocation/BGP/failover events |
| 20 | `metallb_troubleshoot_service` | Troubleshoot | Diagnose <pending> services + fix commands |

---

## Security Features

The server implements multi-layer security:

| Layer | Protection |
|-------|-----------|
| **Protocol validation** | JSON-RPC 2.0 format enforcement, protocol version whitelist |
| **Tool name whitelist** | Only 20 known tools accepted; all others rejected |
| **Input sanitization** | IP/CIDR regex, ASN numeric validation, namespace pattern check |
| **Injection prevention** | Shell metacharacters (`; & | \` $ < > !`) blocked in all fields |
| **Path traversal** | `..` and `/` in non-IP fields rejected |
| **Payload size** | Max 512 chars per string, max JSON depth 5 |
| **Stdout isolation** | All logs → stderr only; stdout reserved for JSON-RPC |
| **No external network** | Pure stdio transport; no HTTP server, no open ports |

---

## Requirements

- Java 21+
- Maven 3.8+
- No Kubernetes access required (MCP server runs locally; tools generate kubectl commands)

---

## Build

```bash
cd mcp-metallb-java
mvn clean package -q
# Output: target/mcp-metallb-1.0.0.jar
```

---

## Run the Server

```bash
java -jar target/mcp-metallb-1.0.0.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0 (MCP protocol).

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-metallb": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/mcp-metallb-java/target/mcp-metallb-1.0.0.jar"]
    }
  }
}
```

---

## Run Tests

```bash
# Compile and run test suite (pass/fail)
mvn test

# Or run directly after build
java -cp target/mcp-metallb-1.0.0.jar com.ecoskiller.mcp.metallb.TestAgents

# With verbose output
java -cp target/mcp-metallb-1.0.0.jar com.ecoskiller.mcp.metallb.TestAgents --verbose
```

---

## File Structure

```
mcp-metallb-java/
├── pom.xml                                          ← Maven build (Java 21, Jackson only)
├── claude_desktop_config.json                       ← Claude Desktop config snippet
├── README.md
└── src/
    ├── main/java/com/ecoskiller/mcp/metallb/
    │   ├── server/
    │   │   └── MetalLBMcpServer.java                ← Main entry point, JSON-RPC dispatch
    │   ├── security/
    │   │   └── SecurityManager.java                 ← Input validation & sanitization
    │   └── tools/
    │       ├── McpTool.java                         ← Tool interface
    │       ├── ToolProvider.java                    ← Provider interface
    │       ├── ToolRegistry.java                    ← Registers & dispatches all tools
    │       ├── ToolNotFoundException.java
    │       ├── IpPoolTools.java                     ← 5 tools: list, allocate, release, exhaustion, expand
    │       ├── BgpTools.java                        ← 3 tools: status, configure, sessions
    │       ├── L2Tools.java                         ← 2 tools: status, configure
    │       ├── ServiceTools.java                    ← 2 tools: list, assign IP
    │       ├── NodeHealthTools.java                 ← 2 tools: health check, failover sim
    │       ├── MetricsTools.java                    ← 2 tools: metrics, overall status
    │       ├── ConfigTools.java                     ← 3 tools: get config, validate, audit log
    │       └── TroubleshootTools.java               ← 1 tool: troubleshoot service
    └── test/java/com/ecoskiller/mcp/metallb/
        └── TestAgents.java                          ← Full test suite (50+ assertions)
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Infrastructure Context (Ecoskiller)

| Parameter | GCP | AWS |
|-----------|-----|-----|
| Pool | `gcp-pool` | `aws-pool` |
| CIDR Range | `172.16.0.100–200` | `10.0.0.100–200` |
| Capacity | 100 IPs | 100 IPs |
| BGP Peer | `172.16.0.1` (Cloud Router) | `10.0.0.1` (VPC gateway) |
| Local ASN | 64512 | 64512 |
| Peer ASN | 64513 | 64514 |
| Mode | BGP (primary) + L2 (fallback) | BGP (primary) + L2 (fallback) |
| k3s Version | 1.27+ | 1.27+ |
| MetalLB Version | 0.13.12+ | 0.13.12+ |
