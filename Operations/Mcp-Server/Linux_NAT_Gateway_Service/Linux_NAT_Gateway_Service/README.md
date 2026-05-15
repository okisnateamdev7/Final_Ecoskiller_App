# mcp-nat-gateway

**Ecoskiller | Linux NAT Gateway — Egress Traffic Management**  
MCP Server in Java (zero external dependencies) | 18 Agents | Priority: HIGH

---

## Overview

Linux NAT Gateway MCP server for Ecoskiller's multi-cloud Kubernetes infrastructure.
Routes egress traffic from private k3s pod IPs through iptables SNAT/masquerade
on dedicated Linux VMs in GCP (asia-south1) and AWS (ap-south-1).

| Field | Details |
|-------|---------|
| Protocol | JSON-RPC 2.0 over stdio |
| MCP Version | 2024-11-05 |
| Language | Java 17+ (no external dependencies) |
| Clouds | GCP (172.16.0.1 VIP) + AWS (10.0.0.1 VIP) |
| Pod CIDRs | GCP 10.244.0.0/16, AWS 10.245.0.0/16 |
| HA | Primary + Secondary via keepalived VRRP (failover < 1s) |
| Agents | 18 tools |

---

## Agents (18)

| # | Tool Name | Description |
|---|-----------|-------------|
| 1 | `snat_masquerade` | Simulate SNAT translation: pod IP → gateway public IP, conntrack entry |
| 2 | `conntrack_manager` | Inspect connection tracking table: stats, lookup by pod IP, flush expired |
| 3 | `egress_policy` | Manage FORWARD chain whitelist rules — add/remove/check allowed CIDRs |
| 4 | `vrrp_failover` | VRRP HA state: primary/secondary role, simulate failover, conntrackd sync |
| 5 | `performance_tuning` | Sysctl sizing for 10k–1M connections, NIC offloading, throughput estimates |
| 6 | `traffic_monitor` | Live egress flows: top pods by bytes, top destinations, syslog format |
| 7 | `iptables_rules` | Generate complete iptables ruleset for NAT, FORWARD, MSS clamping |
| 8 | `packet_flow_diagnostics` | Diagnose: hangs, drops-after-5min, conntrack full, failover outages |
| 9 | `nat_session_timeout` | TCP/UDP/ICMP conntrack timeout configuration via sysctl |
| 10 | `mtu_fragmentation` | MTU settings, MSS clamping, PMTUD black hole prevention |
| 11 | `audit_trail` | SOC2/ISO27001 audit queries: egress logs, iptables changes, export |
| 12 | `prometheus_metrics` | Prometheus metrics, PromQL queries, alerting rules, node exporter config |
| 13 | `health_check` | Full gateway health: kernel forwarding, iptables, conntrack, VIP, VRRP |
| 14 | `keepalived_config` | Generate keepalived.conf for primary/secondary VRRP configuration |
| 15 | `egress_whitelist` | Pre-configured whitelist: Stripe, SendGrid, OpenAI, DNS — add/remove CIDRs |
| 16 | `connection_rate_limiter` | iptables hashlimit rate-limiting per pod to prevent connection exhaustion |
| 17 | `sysctl_parameters` | Complete sysctl config file (small/medium/large profiles) |
| 18 | `gateway_status` | Full status dashboard: VRRP role, conntrack util, egress summary, both clouds |

---

## Security Features

| Feature | Implementation |
|---------|---------------|
| Input validation | All IPs validated against IPv4/CIDR regex |
| Shell injection protection | 7 dangerous pattern checks (`;`, `&`, `|`, path traversal, exec/eval, XSS, SQL) |
| Rate limiting | 30 calls/min per tool (sliding window) |
| Destructive op guard | `confirm: true` required for iptables_rules, egress_policy, sysctl_parameters, nat_session_timeout, egress_whitelist |
| Audit logging | Every tool call + result logged with timestamp to stderr + file |
| Arg sanitization | All string inputs truncated to max length and pattern-checked |
| No external deps | Zero third-party libraries — no supply chain attack surface |

---

## Requirements

- Java 17+ (OpenJDK or Oracle JDK)

---

## Build

```bash
chmod +x build.sh
./build.sh
```

Output: `target/mcp-nat-gateway.jar`

---

## Run

```bash
java --enable-preview -jar target/mcp-nat-gateway.jar
```

The server communicates via **stdin/stdout** using JSON-RPC 2.0.

---

## Connect to Claude Desktop

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "mcp-nat-gateway": {
      "command": "java",
      "args": ["--enable-preview", "-jar", "/absolute/path/to/mcp-nat-gateway/target/mcp-nat-gateway.jar"]
    }
  }
}
```

---

## Test

```bash
python3 test_agents.py           # quick pass/fail (36 tests)
python3 test_agents.py --verbose # with full JSON output
```

Test coverage includes: all 18 agents, security rejection tests (shell injection, missing confirm, invalid IP).

---

## File Structure

```
mcp-nat-gateway/
├── build.sh
├── claude_desktop_config.json
├── README.md
├── test_agents.py
└── src/main/java/com/ecoskiller/mcp/nat/
    ├── server/
    │   └── NatGatewayMcpServer.java    ← Main MCP server + JSON-RPC dispatch
    ├── agents/
    │   ├── NatAgent.java               ← Agent interface
    │   └── AllAgents.java              ← All 17 agent implementations
    │   └── SnatMasqueradeAgent.java    ← Agent 1 (separate for clarity)
    ├── model/
    │   └── ToolResult.java             ← Structured result with JSON serialization
    ├── security/
    │   └── SecurityManager.java        ← Input validation, rate limiting, audit
    └── util/
        ├── JsonUtil.java               ← Zero-dep JSON-RPC serializer/parser
        └── Logger.java                 ← Structured audit-grade logger
```

---

## Protocol

- Transport: **stdio** (stdin/stdout)
- Format: **JSON-RPC 2.0**
- MCP Version: **2024-11-05**
- Methods: `initialize`, `tools/list`, `tools/call`, `ping`

---

## Infrastructure Context

Ecoskiller runs 50 services (13 custom microservices + 37 infra) on k3s clusters
across GCP asia-south1 and AWS ap-south-1 (active-active multi-cloud).
Pod CIDRs are private (unreachable from outside cluster). This NAT Gateway
enables all outbound traffic from pods to external services (Stripe, SendGrid,
OpenAI, etc.) while enforcing centralized egress policy, logging every connection,
and providing < 1s HA failover via VRRP keepalived.

Compliance: SOC2 CC6.1 egress control + ISO27001 A.13.1 network controls.
